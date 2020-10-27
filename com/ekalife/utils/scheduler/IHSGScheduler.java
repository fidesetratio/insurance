package com.ekalife.utils.scheduler;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import com.ekalife.elions.dao.FinanceDao;
import com.ekalife.utils.parent.ParentScheduler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @spring.bean Class ini digunakan dengan cara mengaktifkan sebagai spring bean, 
 * menggunakan teknik 'screenscraping' halaman HTML nilai IHSG dari website. 
 * 
 * Class ini <b>harus</b> direvisi apabila format / tampilan html dari website berubah.
 * 
 * @author Yusuf
 * @since 8 Mei 2013
 */
public class IHSGScheduler extends ParentScheduler{
	protected static final Log logger = LogFactory.getLog( IHSGScheduler.class );
	private static final String PROXY_HOST = "ekaproxy";
	private static final String PROXY_PORT = "8080";
	private static final String PROXY_USER = "java";
	private static final String PROXY_PASS = "14041985";	
	private static final String PROXY_DOMAIN = "ekalife";
	
	public static final String URL_WEBSITE = "http://widgets.duniainvestasi.com/price_widget?codes=COMPOSITE&w=200";	
	
	//main method, only for testing
	public static void main(String[] args){
		try {
			IHSGScheduler i = new IHSGScheduler(null);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e);
		}
	}
	
	public IHSGScheduler(FinanceDao financeDao ) throws HttpException, IOException, ParseException { //taro di constructor aja biar gak repot

		String html = null; 			//full html yang ditarik dari website
		String updateTerakhir = null; 	//string berisi tgl update terakhir dari nilai IHSG
		String nilaiTerakhir = null; 	//string berisi nilai terakhir IHSG
		
        Date lnu_tgl = null; 			//updateTerakhir yg sudah di convert menjadi Date
		Double lnu_nilai = null;		//nilaiTerakhir yg sudah di convert menjadi Double 

		//1. Tarik html dari url website
		HttpClient httpclient = new HttpClient();

		httpclient.getHostConfiguration().setProxy(PROXY_HOST, Integer.parseInt(PROXY_PORT));
		httpclient.getState().setProxyCredentials(
				new AuthScope(PROXY_HOST, Integer.parseInt(PROXY_PORT)),
				new NTCredentials(PROXY_USER, PROXY_PASS, PROXY_HOST, PROXY_DOMAIN));
		
		GetMethod httpget = new GetMethod(URL_WEBSITE);
		try {
			httpclient.executeMethod(httpget);
			html = httpget.getResponseBodyAsString();
		} finally {
			httpget.releaseConnection();
		}
		
		//logger.info(html);
		
		//2. Olah Stringnya untuk mendapatkan tgl update terakhir nilai IHSG
        Pattern p = Pattern.compile("(?i)Stock Price on\\p{Blank}{1,}<br/>"); //cari kata "Stock Price on <br/>", case insensitive 
        Matcher m = p.matcher(html);
        m.find();
        updateTerakhir = html.substring(m.end(), m.end()+9).trim(); //format datenya dd-MMM-yy, 9 karakter, jadi ambil 9 karakter untuk tgl update terakhir nilai IHSG
		
        //3. Olah ulang stringnya, untuk mendapatkan nilai terakhir IHSG
        p = Pattern.compile("(?i)COMPOSITE</a></td><td class=\"text_right\">"); //cari kata COMPOSITE</a></td><td class="text_right">, case insensitive 
        m = p.matcher(html);
        m.find();
        nilaiTerakhir = html.substring(m.end(), m.end()+12).trim(); //ambil 10-12 karakter terakhir, contoh: 15,123.4567
        nilaiTerakhir = nilaiTerakhir.substring(0, nilaiTerakhir.indexOf("<")); //pastikan tidak terambil string "<", agar bisa diconvert ke angka

        //4. Pada titik ini, updateTerakhir harus berisi tanggal dalam format dd-MMM-yy, dan nilaiTerakhir harus berisi angka dalam format X,XXX,XXX.XXX
		DateFormat df = new SimpleDateFormat("dd-MMM-yy");
		lnu_tgl = df.parse(updateTerakhir);
		
		NumberFormat nf = new DecimalFormat("#,##0.0000;(#,##0.0000)");
		lnu_nilai = nf.parse(nilaiTerakhir).doubleValue();
        
		logger.info("IHSG");
		logger.info(lnu_tgl);
		logger.info(lnu_nilai);
		
		//5. Terakhir, insert ke LST_NAB_ULINK
		if(lnu_tgl != null && lnu_nilai != null && financeDao != null) {
			financeDao.insertLstNabUlink("99", 0, lnu_tgl, lnu_nilai);
		}
        
	}
	
}