package com.ekalife.utils.scheduler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import com.ekalife.utils.parent.ParentScheduler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @spring.bean Class ini digunakan dengan cara mengaktifkan sebagai spring bean, 
 * menggunakan teknik 'screenscraping' halaman HTML Kurs Pajak dari situs resmi Departemen Keuangan. 
 * Class ini <b>harus</b> direvisi apabila format / tampilan halaman dari Depkeu tersebut berubah.
 * 
 * 
 * @author Yusup_A
 * @since 26/05/09
 */
public class TaxCurrencyScheduler extends ParentScheduler{
	protected static final Log logger = LogFactory.getLog( TaxCurrencyScheduler.class );

	//private static final String PROXY_HOST = "ekabackup";
	private static final String PROXY_HOST = "ekaproxy";
	private static final String PROXY_PORT = "8080";
	//private static final String PROXY_USER = "internet_yusuf";
	//private static final String PROXY_PASS = "ysf";
	private static final String PROXY_USER = "internet_yusup";
	private static final String PROXY_PASS = "140786";	
	private static final String PROXY_DOMAIN = "ekalife";
	
	public static final String URL_KURS_DEPKEU = "http://www.depkeu.go.id/ind/Currency/";
	
	private String updateTerakhir;
	public String getUpdateTerakhir() {return updateTerakhir;}

	//main method, only for testing
	public static void main(String[] args) {
		TaxCurrencyScheduler c = new TaxCurrencyScheduler();
		try {
			c.setNumberFormat(NumberFormat.getInstance());
			//c.insertDataDepkeu(prosesDataDepkeu(c.getResponseFromWeb(URL_KURS_DEPKEU)),false);
		} catch (Exception e) {logger.error("ERROR :", e);}
	}
	
	//main method
	public void main() throws Exception{
		logger.info("SYNCHRONIZING TAX CURRENCY AT " + new Date());
		//long start = System.currentTimeMillis();
		String kursMessage = insertDataDepkeu(prosesDataDepkeu(getResponseFromWeb(URL_KURS_DEPKEU)),false);
		String err="";
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		if(jdbcName.equals("eka8i") && 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				) {
			Date bdate 	= new Date();
			
			if(kursMessage.trim().equals("")) {
				email.send(false, 
						"ajsjava@sinarmasmsiglife.co.id", 
						new String[] {"yusup_a@sinarmasmsiglife.co.id"}, 
						new String[] {"yusup_a@sinarmasmsiglife.co.id"}, null,
//						new String[] {"yusuf@sinarmasmsiglife.co.id"}, new String[] {}, 
						"PERHATIAN! ERROR saat penarikan kurs pajak dari website Depkeu per tanggal " + df.format(new Date()), 
						"Harap hubungi IT untuk mengkonfirmasi hal ini. Terima kasih.", 
						null);
				try {
					uwManager.insertMstSchedulerHist(
							InetAddress.getLocalHost().getHostName(),
							"SCHEDULER TAX CURRENCY", bdate, new Date(), "ERROR", err);
				} catch (UnknownHostException e) {
					logger.error("ERROR :", e);
				}
			
			//apabila berhasil, email
			}else {
				logger.info(" tax email");
				//if(!kursMessage.trim().equals("skip")) {
					try {
						email.send(false, 
								"ajsjava@sinarmasmsiglife.co.id", 
								//new String[] {"yusup_a@sinarmasmsiglife.co.id"},
								//new String[] {"yusup_a@sinarmasmsiglife.co.id"}, null,
								new String[] {"trilestari@sinarmasmsiglife.co.id"}, 
								new String[] {props.getProperty("admin.yusuf"),"yusup_a@sinarmasmsiglife.co.id"}, new String[] {}, 
								"Konfirmasi Kurs Pajak Tanggal " + df.format(new Date()), 
								kursMessage + "NB: Informasi kurs ini di-email secara otomatis melalui sistem E-Lions.", 
								null);
						
					}catch(Exception e) {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						err=e.getLocalizedMessage();
						email.send(false, 
								"ajsjava@sinarmasmsiglife.co.id", 
								new String[] {"yusup_a@sinarmasmsiglife.co.id"}, 
								new String[] {}, new String[] {}, 
								"ERROR Konfirmasi Kurs Pajak Tanggal " + df.format(new Date()), 
								sw.toString(), 
								null);
					}					
				//}
				try {
					uwManager.insertMstSchedulerHist(
							InetAddress.getLocalHost().getHostName(),
							"SCHEDULER TAX CURRENCY", bdate, new Date(), "OK",err);
				} catch (UnknownHostException e) {
					logger.error("ERROR :", e);
				}
			}
		}
		
		long end = System.currentTimeMillis();
        //logger.info("KURS BERHASIL INSERT PADA " + new Date() + " DALAM " + ( (float) (end-start) / 1000) + " DETIK.");
	}
	
	/**
	 * Fungsi ini melakukan koneksi ke alamat Kurs Tengah BI
	 * @return Seluruh isi html halaman tersebut dalam bentuk String
	 */
	public String getResponseFromWeb(String url) throws Exception{
		HttpClient httpclient = new HttpClient();
		
		// di ajsjava bs tanpa proxy ato pk ekabackup
		/*httpclient.getHostConfiguration().setProxy(PROXY_HOST, Integer.parseInt(PROXY_PORT));
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
	 * Fungsi ini memproses String HTML hasil fungsi getResponseFromWeb() untuk menarik data kurs pajak depkeu
	 * @param data String hasil getResponseFromWeb()
	 * @return List dari kurs yang ada di halaman tersebut
	 */	
	public List prosesDataDepkeu(String data) {
		Pattern p;
		Matcher m;

		// untuk tes koneksi internet
		/*logger.info("---- test ----");
		logger.info(data);*/
		
		// range tanggal, ambil option pertama
        p = Pattern.compile(".*</option>");
        m = p.matcher(data);
        m.find();
        String[] date = m.group().replace("</option>", "").trim().split(" s.d. ");
        
        //grab datanya
        p = Pattern.compile("<tr.*><td.*>");
        m = p.matcher(data);
        m.find();        
 
        List<String[]> daftarKurs = new ArrayList<String[]>();
        
        String[] asdf = m.group().split("</tr>");
        for(int i=1; i<asdf.length; i++) {
        	String[] qwer = asdf[i].replace("&nbsp;", "").trim().split("</td>");
    		String name = qwer[1].replace(qwer[1].substring(qwer[1].indexOf('<'),qwer[1].indexOf('>')+1), "");	
			String getCurr = qwer[3].replace(qwer[3].substring(qwer[3].indexOf('<'),qwer[3].indexOf('>')+1), "");	
			if(name.contains("USD") || name.contains("SGD") || name.contains("EUR")) {
				String[] kurs = new String[4];
				kurs[0] = date[0];
				kurs[1] = date[1];
	    		if(name.contains("USD")) kurs[2] = "02";
	    		else if(name.contains("SGD")) kurs[2] = "03";
	    		else if(name.contains("EUR")) kurs[2] = "04";
				kurs[3] = getCurr.substring(0,getCurr.indexOf('<'));
				daftarKurs.add(kurs);				
			}
        }        
        return daftarKurs;
	}
	
	/**
	 * Fungsi ini melakukan insert data kurs pajak ke database. Saat ini baru terbatas di USD dan SGD & EUR
	 * @param daftar List berisi data kurs yang ditarik
	 * @throws Exception
	 */	
	private String insertDataDepkeu(List daftar, boolean isTesting) throws Exception {
		StringBuffer hasil = new StringBuffer();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date now = new Date();
		
		String[] kurs = (String[]) daftar.get(0);
		hasil.append("Kurs pajak per tanggal " + kurs[0].replace("-","/") + " s/d " + kurs[1].replace("-","/")+"\n");
		hasil.append("------------------------------------------------\n");
		
		for(int i=0; i<daftar.size(); i++) {
			kurs = (String[]) daftar.get(i);
			Map param = new HashMap();
			//Long dateDiff = FormatDate.dateDifference(df.parse(kurs[0].replace("-","/")), now, true);
			
			//logger.info(dateDiff);
			
			param.put("ltc_currency", numberFormat.parse(kurs[3]).doubleValue());
			param.put("ltc_beg_date", kurs[0].replace("-","/"));
			param.put("ltc_end_date", kurs[1].replace("-","/"));
			param.put("lku_id", kurs[2]);
			
			if(isTesting) {
				logger.info("currency : "+param.get("ltc_currency"));
				logger.info("beg date : "+param.get("ltc_beg_date"));
				logger.info("end date : "+param.get("ltc_end_date"));
				logger.info("lku id : "+param.get("lku_id"));
				logger.info("");
			}
			else {
				// tarik data pada hari ke 1 dr range tanggal kurs pajak
				//if(dateDiff == 0) {
				try {
					uwManager.insertTaxCurrency(param);
				} catch (Exception e) {
					logger.error("ERROR :", e);
				}
				
					if(kurs[2].equals("02")) hasil.append("Mata Uang  = USD \n");
					else if(kurs[2].equals("03")) hasil.append("Mata Uang  = SGD \n");
					else if(kurs[2].equals("04")) hasil.append("Mata Uang  = EUR \n");
					
					hasil.append("Nilai kurs = " + numberFormat.parse(kurs[3]).doubleValue()+"\n\n");
				//}
				//else return "skip";
			}
		}
		hasil.append("\n\n");
		return hasil.toString();
	}
}