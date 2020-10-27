package com.ekalife.utils.scheduler;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.util.StringUtils;

import bixo.config.FetcherPolicy;
import bixo.config.FetcherPolicy.RedirectMode;
import bixo.datum.FetchedDatum;
import bixo.datum.ScoredUrlDatum;
import bixo.exceptions.AbortedFetchException;
import bixo.exceptions.AbortedFetchReason;
import bixo.exceptions.BaseFetchException;
import bixo.exceptions.IOFetchException;
import bixo.exceptions.RedirectFetchException;
import bixo.exceptions.RedirectFetchException.RedirectExceptionReason;
import bixo.fetcher.BaseFetcher;
import bixo.fetcher.FetchedResult;
import bixo.fetcher.SimpleHttpFetcher;
import bixo.utils.ConfigUtils;

import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.util.StringUtils;

import bixo.exceptions.BaseFetchException;
import bixo.fetcher.FetchedResult;
import bixo.fetcher.SimpleHttpFetcher;
import bixo.utils.ConfigUtils;

import com.ekalife.elions.dao.CommonDao;
import com.ekalife.elions.model.Currency;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.parent.ParentScheduler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @spring.bean Class ini digunakan dengan cara mengaktifkan sebagai spring bean, 
 * menggunakan teknik 'screenscraping' halaman HTML Kurs Tengah dari situs resmi Bank Indonesia. 
 * Class ini <b>harus</b> direvisi apabila format / tampilan halaman dari BI tersebut berubah.
 * 
 * Yusuf 20090407 : Class ini juga sekarang dibuat untuk mengambil kurs pajak dari depkeu
 * 
 * @author Yusuf
 * @since 01/09/06
 */
public class CurrencyScheduler extends ParentScheduler{
	
	protected final static Log logger = LogFactory.getLog( CurrencyScheduler.class );
	//private static final String PROXY_HOST = "ekabackup";
	private static final String PROXY_HOST = "128.21.32.31";
	private static final String PROXY_PORT = "8080";
	private static final String PROXY_USER = "java";
	private static final String PROXY_PASS = "14041985";
	private static final String PROXY_DOMAIN = "";
	
	/* REFF :
	 * http://stackoverflow.com/questions/5280577/apache-httpclient-throws-java-net-socketexception-connection-reset-for-many-dom
	 * http://www.massapi.com/source/github/84/51/845171782/src/test/java/bixo/fetcher/SimpleHttpFetcherTest.java.html#265
	 * http://www.massapi.com/source/github/84/51/845171782/src/main/java/bixo/fetcher/BaseFetcher.java.html
	 * https://github.com/bixo/bixo/blob/master/examples/src/main/java/bixo/examples/webmining/DemoWebMiningTool.java
	 * http://crawler-commons.github.io/crawler-commons/0.6/apidocs/index.html?crawlercommons/fetcher/BaseFetchException.html
	 * 
	 */
	
	//Yusuf - (14/7/08) - Mulai Juli 2008, websitenya berubah tampilan + berubah link
	//Deddy - (23/12/13) - Mulai 23 Dec 2013, website BI berubah tampilan & Link.
	//public static final String URL_KURS_BI = "http://m.bi.go.id/web/id/Moneter/Kurs+Bank+Indonesia/Kurs+Transaksi/";
	//public static final String URL_KURS_BI = "http://www.bi.go.id/web/id/Moneter/Kurs+Bank+Indonesia/Kurs+Uang+Kertas+Asing/";	
	public static final String URL_KURS_BI = "http://www.bi.go.id/id/moneter/informasi-kurs/transaksi-bi/Default.aspx";	
	
	public static final String URL_KURS_DEPKEU = "http://www.depkeu.go.id/ind/Currency/";
	
	private String updateTerakhir;
	public void setUpdateTerakhir(String updateTerakhir) {this.updateTerakhir = updateTerakhir;}
	
	private String emailMessage;
	public String getEmailMessage() { return emailMessage; }
	public void setEmailMessage(String emailMessage) { this.emailMessage = emailMessage; }

	//main method, only for testing
	public static void main(String[] args) {
		CurrencyScheduler c = new CurrencyScheduler(null, true);
		logger.info(c.getEmailMessage());
	}
	
	//constructor
	public CurrencyScheduler(CommonDao commonDao, boolean isTesting) {	
		logger.info("SYNCHRONIZING DAILY CURRENCY AT " + new Date());
		long start = System.currentTimeMillis();
		
		try {
			this.setEmailMessage(insertDataBI(processDataBI(fetchUrlSource(URL_KURS_BI)), isTesting, commonDao));
			//processDataBI(fetchUrlSource(URL_KURS_BI));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e1);
		}
		
		long end = System.currentTimeMillis();
        logger.info("KURS BERHASIL INSERT PADA " + new Date() + " DALAM " + ( (float) (end-start) / 1000) + " DETIK.");
	}
	
	/**
	 * Fungsi ini melakukan koneksi ke alamat Kurs Tengah BI
	 * @return Seluruh isi html halaman tersebut dalam bentuk String
	 */
	public String getResponseFromWeb(String url) throws Exception{
		HttpClient httpclient = new HttpClient();
		
		//(13 Okt 2015) Deddy - di ajsjava bs tanpa proxy
	/*	httpclient.getHostConfiguration().setProxy(PROXY_HOST, Integer.parseInt(PROXY_PORT));
		httpclient.getState().setProxyCredentials(
		new AuthScope(PROXY_HOST, Integer.parseInt(PROXY_PORT)),	
		new NTCredentials(PROXY_USER, PROXY_PASS, PROXY_HOST, PROXY_DOMAIN));*/
		GetMethod httpget = new GetMethod(url);
		try {
			httpclient.executeMethod(httpget);
			return httpget.getResponseBodyAsString();
		} finally {
			httpget.releaseConnection();
		}
	}
	
	/**
	 * Fungsi ini memproses String HTML hasil fungsi getResponseFromWeb() untuk menarik data kurs BI
	 * @param data String hasil getResponseFromWeb()
	 * @return List dari kurs yang ada di halaman tersebut
	 */
	public List processDataBI(String data) throws Exception{
		Pattern p;
		Matcher m;
		
		// untuk tes koneksi internet
//		logger.info("---- test ----");
//		logger.info(data);
        data = StringUtils.replace(data, "Update Terakhir\r\n", "Update Terakhir");
        
//        p = Pattern.compile("(?i)update terakhir\\p{Blank}{1,}");
//        m = p.matcher(data);
//        m.find();
//        logger.info(m.end());
//        logger.info(m.end()+16);
//        updateTerakhir = data.substring(m.end(), m.end()+16).trim();
        
        p = Pattern.compile("(?i)update terakhir");
        m = p.matcher(data);
        m.find();
        updateTerakhir = data.substring(m.end()+2, m.end()+270).trim();
        updateTerakhir = updateTerakhir.substring(updateTerakhir.indexOf("\">")+2, updateTerakhir.indexOf("</span>"));
//        logger.info(updateTerakhir);
        
        //grab datanya
//        p = Pattern.compile("<tr.*><td.*>.*</font>.*<td.*>.*</font>.*<td.*>.*</font>.*<td.*>.*</font>");
        p = Pattern.compile("<table.*id=\"ctl00_PlaceHolderMain_biWebKursTransaksiBI_GridView1\".*>.*");
        m = p.matcher(data);
        m.find();
        String a="";
//        a=data.substring(m.start(), 178140);
        a=data.substring(m.start(), m.start()+20885);

        List<String[]> daftarKurs = new ArrayList<String[]>();
        String[] kurs = null;
        
        String[] tes = a.split("</tr>");
        String[] asdf = m.group().split("</tr>");
        for(int y=1;y<tes.length; y++){//tidak dipakai index 0 karena hanya row table.
        	String[] jenis_kurs = tes[y].split("</td>");
        	kurs = new String[4];
//        	if(jenis_kurs.toString().startsWith("<tr>")){
        	if(jenis_kurs[0].startsWith("<tr>")){
	        	for(int z=0;z<4;z++){
	        		String nilai = jenis_kurs[z].toString().substring(jenis_kurs[z].toString().lastIndexOf(">")+1, jenis_kurs[z].toString().trim().length()).trim();
	        		kurs[z] = nilai;
	        	}
	        	daftarKurs.add(kurs);
        	}
        }
        
//        for(int i=0; i<asdf.length; i++) {
//        	String[] qwer = asdf[i].split("</font>");
//			kurs = new String[4];
//        	for(int j=0; j<qwer.length; j++) {
//        		String[] zxcv = qwer[j].split("<font.*>");
//        		if(j!=qwer.length-1 || i==asdf.length-1) {
//        			kurs[j] = zxcv[zxcv.length-1]; 
//        		}
//        	}
//        	daftarKurs.add(kurs);
//        }
        return daftarKurs;
	}
	
	/**
	 * Fungsi ini melakukan insert data kurs ke database. Saat ini baru terbatas di US$ dan SING$
	 * @param daftar List berisi data kurs yang ditarik
	 * @throws Exception
	 */
	private String insertDataBI(List daftar, boolean isTesting, CommonDao commonDao) throws Exception {
		StringBuffer hasil = new StringBuffer(); 
		
		
		for(int i=0; i<daftar.size(); i++) {
			String[] kurs = (String[]) daftar.get(i);
	    	if(		kurs[0].trim().equalsIgnoreCase("USD") || 
	    			kurs[0].trim().equalsIgnoreCase("SGD") || 
	    			kurs[0].trim().equalsIgnoreCase("EUR") ||
	    			kurs[0].trim().equalsIgnoreCase("JPY")) {
	    		
	    		//nilai pembagi ini biasanya 1, tapi khusus untuk JPY, di websitenya BI pembaginya 100, jadi harus dibagi juga
    			double pembagi  = NumberFormat.getInstance(Locale.ENGLISH).parse(kurs[1]).doubleValue();

    			double kursJual = NumberFormat.getInstance(Locale.ENGLISH).parse(kurs[2]).doubleValue() / pembagi; 
    			double kursBeli = NumberFormat.getInstance(Locale.ENGLISH).parse(kurs[3]).doubleValue() / pembagi;
    			
    			//kurs tengah = ( jual + beli ) / 2
    			double kursTengah = FormatNumber.round(((kursJual+kursBeli)/2), 0);
    			
    			Currency kurus = new Currency();
    			kurus.setLku_id(
    				kurs[0].trim().equalsIgnoreCase("USD")?"02":
    				kurs[0].trim().equalsIgnoreCase("SGD")?"03":
    				kurs[0].trim().equalsIgnoreCase("EUR")?"04":
    				kurs[0].trim().equalsIgnoreCase("JPY")?"06":"");
    			
	    		kurus.setLkh_currency(new Double(kursTengah).intValue());
	    		kurus.setLkh_kurs_beli(new Double(FormatNumber.round(0.99 * kurus.getLkh_currency(),0)).intValue());
				kurus.setLkh_kurs_jual(new Double(FormatNumber.round(1.01 * kurus.getLkh_currency(),0)).intValue());

				if(isTesting) {
					logger.info("");
					logger.info("KURS          = " + kurs[0]);
					logger.info("NILAI         = " + kurs[1]);
					logger.info("KURS JUAL     = " + kurs[2]);
					logger.info("KURS BELI     = " + kurs[3]);
					logger.info("FLAG INSERT   = " + kurus.getFlag_insert());
					logger.info("LKU_ID        = " + kurus.getLku_id());
					logger.info("LKH_CURRENCY  = " + kurus.getLkh_currency());
					logger.info("LKH_DATE      = " + kurus.getLkh_date());
					logger.info("LKH_KURS_BELI = " + kurus.getLkh_kurs_beli());
					logger.info("LKH_KURS_JUAL = " + kurus.getLkh_kurs_jual());
				}else {
					if(!kurus.getLku_id().equals("04"));
						commonDao.insertCurrency(kurus);
	    				//elionsManager.insertCurrency(kurus);
				}

    			//email USD, SGD, EUR, JPY
    			//if(kurus.getLku_id().equals("02") || kurus.getLku_id().equals("03") || kurus.getLku_id().equals("04")) {
	    			hasil.append(kurs[0].trim() + "\n" + "-----\n");
	    			hasil.append("KURS JUAL   = " + NumberFormat.getInstance().format(kurus.getLkh_kurs_jual()) + "\n");
	    			hasil.append("KURS TENGAH = " + NumberFormat.getInstance().format(kurus.getLkh_currency()) + "\n");
	    			hasil.append("KURS BELI   = " + NumberFormat.getInstance().format(kurus.getLkh_kurs_beli()) + "\n\n");
    			//}
	    	}
		}
		hasil.append("\n\n");
		
		return hasil.toString();
	}
	
		private String fetchUrlSource(String urls) throws IOException {
		
        //FetcherPolicy policy = new FetcherPolicy();
        //policy.setMinResponseRate(FetcherPolicy.NO_MIN_RESPONSE_RATE);
		
        //BaseFetcher fetcher = new SimpleHttpFetcher(1, policy, ConfigUtils.BIXO_TEST_AGENT);
		SimpleHttpFetcher fetcher = new SimpleHttpFetcher(ConfigUtils.BIXO_TEST_AGENT);
        fetcher.setDefaultMaxContentSize(1000000);
        
		try {
			//FetchedDatum result = fetcher.get(new ScoredUrlDatum(urls));
		    //logger.info(new String(result.getContentBytes()));
		    FetchedResult result = fetcher.fetch(urls);
		    //logger.info(new String(result.getContent()));
		    return new String(result.getContent());
		} catch (BaseFetchException e) {
		    logger.error("ERROR :", e);
		    return "";
		}
    }
}