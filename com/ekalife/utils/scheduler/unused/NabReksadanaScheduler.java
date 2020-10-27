package com.ekalife.utils.scheduler.unused;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import com.ekalife.elions.model.Reksadana;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.parent.ParentScheduler;

/**
 * @spring.bean Class ini digunakan dengan cara mengaktifkan sebagai spring bean, 
 * menggunakan teknik 'screenscraping' halaman HTML NAB Reksadana dari situs bisnis indonesia 
 * Class ini <b>harus</b> direvisi apabila format / tampilan halaman dari website tersebut berubah.
 * 
 * @author Yusuf
 * @since 21/11/2007
 * @deprecated class ini sudah gak dipake, katanya maunya di click aja, bukan di-reschedule,
 * yang dipake fungsi2nya aja
 */
public class NabReksadanaScheduler extends ParentScheduler{

	//private static final String PROXY_HOST = "ekabackup";
	private static final String PROXY_HOST = "ekaproxy";
	private static final String PROXY_PORT = "8080";
	private static final String PROXY_USER = "infovesta";
	private static final String PROXY_PASS = "1nfovesta";
	//private static final String PROXY_USER = "internet_yusup";
	//private static final String PROXY_PASS = "140786";	
	private static final String PROXY_DOMAIN = "ekalife";
	//public static final String URL_NAB_REKSADANA = "http://web.bisnis.com/edisi-cetak/edisi-harian/tabel_reksadana/";	
	public static final String URL_NAB_PENDAPATAN_TETAP = "http://www.infovesta.com/isd/infovesta/umum/reksa.jsp?tipe=pt";

	private String updateTerakhir;

	public String getUpdateTerakhir() {
		return updateTerakhir;
	}

	//main method
	public void main() throws Exception {
		//logger.info("SYNCH NAB REKSADANA AT " + new Date());
		/*long start = System.currentTimeMillis();
		try {
			processData(getResponseFromWeb(URL_NAB_PENDAPATAN_TETAP));
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
        long end = System.currentTimeMillis();*/
        //logger.info("SYNCH SELESAI PADA " + new Date() + " DALAM " + ( (float) (end-start) / 1000) + " DETIK.");
	}

	/**
	 * Fungsi ini melakukan koneksi ke alamat Kurs Tengah BI
	 * @return Seluruh isi html halaman tersebut dalam bentuk String
	 */
	public String getResponseFromWeb(String url) throws Exception{
		//return FileUtils.readFileAsString("D:\\WorkspaceMyEclipse5\\E-Lions-server\\JavaSource\\com\\ekalife\\utils\\tabel_reksadana.txt");
		HttpClient httpclient = new HttpClient();
		
		// di ajsjava bs tanpa proxy ato pk ekabackup
		httpclient.getHostConfiguration().setProxy(PROXY_HOST, Integer.parseInt(PROXY_PORT));
		httpclient.getState().setProxyCredentials(
				new AuthScope(PROXY_HOST, Integer.parseInt(PROXY_PORT)),
				new NTCredentials(PROXY_USER, PROXY_PASS, PROXY_HOST, PROXY_DOMAIN));
		
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
	 * @throws ParseException 
	 * @author ryan & alfian_h
	 * 
	 * 31/03/2016 Merubah replaceAll tag html dengan regex; ex : baris2[j] = baris2[j].replaceAll("\\<[^>]*>", "");
	 */
	public List<Reksadana> processData(String data, List<Map> nabReksadanaList, Date ird_trans_date) throws ParseException {
		Pattern p1,p2;
		Matcher m1,m2;
		NumberFormat nf = new DecimalFormat("#,##0.0000000000");
        List<Reksadana> result = new ArrayList<Reksadana>();
        List<Reksadana> result2 = new ArrayList<Reksadana>();
        
        //untuk infovesta2 design web baru
        data = data.replace("\"", "");
        data = data.replace("<tbody aria-relevant=all aria-live=polite role=alert>", "<tbody>");
        data = data.replace("<tbody role= rowgroup >", "<tbody>");
        data = data.replace("<tr class=odd align=center>", "<tr>");
        data = data.replace("<td class=  align=left>", "<td>");
        data = data.replace("<td class=  style=color: red>", "<td>");
        data = data.replace("<td class=  style=color: green>", "<td>");
        data = data.replace("<tr.*>", "<tr>");
        
        p1 = Pattern.compile("<tbody>.*");
        m1 = p1.matcher(data);
        m1.find();
        //logger.info(data.substring(m1.end()));
        /*p2 = Pattern.compile("<tr.*>.*");
        m2 = p2.matcher(data.substring(m1.end()));
        m2.find();
        logger.info(data.substring(m2.end()));*/ 
        StringBuffer sb = new StringBuffer();
        String REPLACE = "";
        
        //Yang lama
        //String[] baris = data.substring(m1.end()).split("</tr>");
        /*for(int i=0; i<(baris.length - 1); i++) {
        	String[] baris2 = baris[i].split("</td>");
        	Reksadana reksadana = new Reksadana();
        	//logger.info("i :"+i);
        	for(int j=0; j<(baris2.length - 1); j++) {
        		baris2[j] = baris2[j].trim();
        		baris2[j] = baris2[j].replaceAll("(<tr.*\\n)","");
            	baris2[j] = baris2[j].replaceAll("(<tr.*><b>|</b>)", "");
            	baris2[j] = baris2[j].replaceAll("<td.*>", "");
            	baris2[j] = baris2[j].replace("&nbsp","");
            	baris2[j] = baris2[j].trim();
            	if(j == 0){
            		baris2[j] = baris2[j].replaceAll("<tr.*>", "");
            		reksadana.setIre_reksa_name(baris2[j]);
            	}else if(j == 1){
            		if(baris2[j].equals("") || baris2[j].equals("-"))baris2[j] = "0.0";
            		baris2[j] = baris2[j].replace(",","");
            		reksadana.setIrd_nab_up(FormatNumber.round(new Double(baris2[j]),2));
            		reksadana.setIrd_nav(FormatNumber.round(new Double(baris2[j]),2));
            	}
            	else if(j == 2){
            		if(baris2[j].equals("") || baris2[j].equals("-"))baris2[j] = "0.0";
            		reksadana.setIrd_last_1d(FormatNumber.round(new Double(baris2[j]), 2));
            	}
            	else if(j == 3){
            		if(baris2[j].equals("") || baris2[j].equals("-"))baris2[j] = "0.0";
            		//reksadana.setIrd_last_7d(FormatNumber.round(new Double(baris2[j]), 2));
            		reksadana.setIrd_last_30d(FormatNumber.round(new Double(baris2[j]), 2));
            	}
            	else if(j == 4){
            		if(baris2[j].equals("") || baris2[j].equals("-"))baris2[j] = "0.0";
            		//reksadana.setIrd_last_30d(FormatNumber.round(new Double(baris2[j]), 2));
            		reksadana.setIrd_last_oney(FormatNumber.round(new Double(baris2[j]), 2));
            		reksadana.setIrd_last_oneyr(FormatNumber.round(new Double(baris2[j]), 2));
            	}
            	else if(j == 5){
            		if(baris2[j].equals("") || baris2[j].equals("-"))baris2[j] = "0.0";
            		//reksadana.setIrd_last_oney(FormatNumber.round(new Double(baris2[j]), 2));
            		//reksadana.setIrd_last_oneyr(FormatNumber.round(new Double(baris2[j]), 2));
            		reksadana.setIrd_last_3yr(FormatNumber.round(new Double(baris2[j]), 2));
            	}
        	}
        	result.add(reksadana);
        }*/
        
        //yang baru
        String[] baris = data.split("</tr>");
        for(int i=0; i<=(baris.length - 1); i++) {
        	String[] baris2 = baris[i].split("</td>");
        	Reksadana reksadana = new Reksadana();
        	//logger.info("i :"+i);
        	for(int j=0; j<(baris2.length); j++) {
        		/*baris2[j] = baris2[j].trim();
            	baris2[j] = baris2[j].replaceAll("</div>", "");
        		baris2[j] = baris2[j].replaceAll("(<tr.*\\n)","");
            	baris2[j] = baris2[j].replaceAll("(<tr.*><b>|</b>)", "");
            	baris2[j] = baris2[j].replaceAll("<span style=color:green>", "");
            	baris2[j] = baris2[j].replaceAll("<span style=color:red>", "");
            	baris2[j] = baris2[j].replaceAll("</span>", "");
            	baris2[j] = baris2[j].replace("&nbsp","");
            	baris2[j] = baris2[j].replaceAll("<div.*>", "");
            	baris2[j] = baris2[j].replaceAll("<td.*>", "");
            	baris2[j] = baris2[j].replace("&nbsp","");*/
            	baris2[j] = baris2[j].trim();
            	baris2[j] = baris2[j].replaceAll("\\<[^>]*>", "");
            	baris2[j] = baris2[j].trim();
            	
            	if(j == 0){
            		
            	}else if(j == 1){
            		baris2[j] = baris2[j].replaceAll("<tr.*>", "");
            		reksadana.setIre_reksa_name(baris2[j]);
            	}else if(j == 2){
            		if(baris2[j].equals("") || baris2[j].equals("-"))baris2[j] = "0.0";
            		baris2[j] = baris2[j].replace(",","");
            		reksadana.setIrd_nab_up(FormatNumber.round(new Double(baris2[j]),2));
            		reksadana.setIrd_nav(FormatNumber.round(new Double(baris2[j]),2));
            	}else if(j == 3){
            		
            	}else if(j == 4){
            		if(baris2[j].equals("") || baris2[j].equals("-"))baris2[j] = "0.0";
            		reksadana.setIrd_last_1d(FormatNumber.round(new Double(baris2[j]), 2));
            	}else if(j == 5){
            		if(baris2[j].equals("") || baris2[j].equals("-"))baris2[j] = "0.0";
            		reksadana.setIrd_last_30d(FormatNumber.round(new Double(baris2[j]), 2));
            	}else if(j == 6){
            		if(baris2[j].equals("") || baris2[j].equals("-"))baris2[j] = "0.0";
            		reksadana.setIrd_last_oney(FormatNumber.round(new Double(baris2[j]), 2));
            		reksadana.setIrd_last_oneyr(FormatNumber.round(new Double(baris2[j]), 2));
            	}else if(j == 7){
            		if(baris2[j].equals("") || baris2[j].equals("-"))baris2[j] = "0.0";
            		reksadana.setIrd_last_3yr(FormatNumber.round(new Double(baris2[j]), 2));
            	}
        	}
        	result.add(reksadana);
        }
        
        // filter list nab reksadana
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatDate =new SimpleDateFormat("dd/MM/yyyy");
        String trans_date = sdf.format(ird_trans_date);
        for(int i = 0 ; i < nabReksadanaList.size() ; i++){
        	for(int j = 0 ; j < result.size() ; j++){
        		if(result.get(j).getIre_reksa_name()!=null){
	        		if(result.get(j).getIre_reksa_name().toUpperCase().contains(nabReksadanaList.get(i).get("IRE_REKSA_NAME").toString().replace("REKSA DANA", "").replace("REKSADANA", "").replace("RD ", "").trim().toUpperCase())){
	        			int idx = result.get(j).getIre_reksa_name().toUpperCase().indexOf(nabReksadanaList.get(i).get("IRE_REKSA_NAME").toString().replace("REKSA DANA", "").replace("REKSADANA", "").replace("RD ", "").trim().toUpperCase());
	        			int idx_1 = result.get(j).getIre_reksa_name().length()-1;
	        			int idx_2 = nabReksadanaList.get(i).get("IRE_REKSA_NAME").toString().replace("REKSA DANA", "").replace("REKSADANA", "").replace("RD ", "").trim().length()-1;
	        			if((idx + idx_2) == idx_1){
	            			result2.add(new Reksadana(result.get(j).getIre_reksa_name(), nabReksadanaList.get(i).get("IRE_REKSA_NO").toString().toUpperCase(), ird_trans_date, result.get(j).getIrd_nab_up(), result.get(j).getIrd_nav(), result.get(j).getIrd_last_30d(), result.get(j).getIrd_last_oney(), result.get(j).getIrd_last_oneyr(), result.get(j).getIrd_last_1d(), result.get(j).getIrd_last_7d(), result.get(j).getIrd_last_3yr() ));
	            			// remove dicomment karena ada beberapa data dobel yang diperlukan
	//            			result.remove(j);
	//            			j--;
	        			}
	        		}
        		}
        	}
        }
        
        //logger.info(data.substring(m.end()));
        
        /*//Step 1 : Cari tahu tanggal update terakhirnya kapan
        p = Pattern.compile("(?i)Nilai aktiva bersih dan hasil investasi berbagai reksa dana hingga\\p{Blank}{1,}");
        m = p.matcher(data);
        m.find();
        //logger.info(data.substring(m.end(), m.end()+100).trim());
        updateTerakhir = data.substring(m.end(), m.end()+20).trim();
        updateTerakhir = updateTerakhir.substring(0, updateTerakhir.indexOf("<"));
        logger.info("TANGGAL UPDATE TERAKHIR = " + updateTerakhir);
        
        //Step 2 : gunakan regular exp untuk menarik data
        p = Pattern.compile("<tr.*><td.*>.*<td.*>.*<td.*>.*<td.*>.*");
        m = p.matcher(data);
        m.find();


        //String[] kurs;
       
        String[] baris = data.substring(m.end()).split("</tr>\r\n");
        String judul = "PENDAPATAN TETAP";
        int pos = -1;
        String tmp="";
        
        logger.info("REKSADANA = " + judul);
        
        // Untuk testing
        //logger.info(data);
        
        for(int i=0; i<(baris.length - 1); i++) {
        	
        	//ambilnya sampe yang mana? saat ini baru sampe reksadana, index dst tidak dimasukkan
        	if(baris[i].indexOf("INDEKS") > -1) {
        		break;
        	}
        	
        	//apabila judul reksadana
        	//if((pos = baris[i].indexOf("<td colspan=\"5\" class=\"judul1\"><div align=\"left\">")) > -1) {
        		//pos += 49;
        		//judul = baris[i].substring(pos, baris[i].indexOf("</div>", pos));
                //logger.info("REKSADANA = " + judul);
        	
        	 Perubahan per tanggal 29/05/2009 - Yusup A 
        	if((pos = baris[i].indexOf("<td colspan=\"5\" align=\"left\">")) > -1) {
                pos += 29;
        		judul = baris[i].substring(pos, baris[i].indexOf("</td>",pos));
                logger.info("REKSADANA = " + judul);                
            //apabila isi reksadana
        	}else {
        		Reksadana r = new Reksadana();
        		//logger.info("BARIS " + i + " = " + baris[i]);
        		
        		//IRD_REKSA_NAME
        		pos = baris[i].indexOf("<td class=\"isi\">")+16;
        		r.setIre_reksa_name(baris[i].substring(pos, baris[i].indexOf("</td>", pos)));
        		
        		//IRD_NAV
        		pos = baris[i].indexOf("<td class=\"isi1\">", pos)+17;
        		tmp = baris[i].substring(pos, baris[i].indexOf("</td>", pos));
        		tmp = tmp.replaceAll("\\.", "").replaceAll("\\,", "."); //hapus seluruh tanda titik, lalu ganti seluruh tanda koma jadi titik
        		try {r.setIrd_nav(nf.parse(tmp).doubleValue());}catch(ParseException e) {r.setIrd_nav((double) 0);}
        		
        		//IRD_LAST_30D
        		pos = baris[i].indexOf("<td class=\"isi1\">", pos)+17;
        		tmp = baris[i].substring(pos, baris[i].indexOf("</td>", pos));
        		tmp = tmp.replaceAll("\\.", "").replaceAll("\\,", "."); //hapus seluruh tanda titik, lalu ganti seluruh tanda koma jadi titik
        		try {r.setIrd_last_30d(nf.parse(tmp).doubleValue());}catch(ParseException e) {r.setIrd_last_30d((double) 0);}
        		
        		//IRD_LAST_ONEY
        		pos = baris[i].indexOf("<td class=\"isi1\">", pos)+17;
        		tmp = baris[i].substring(pos, baris[i].indexOf("</td>", pos));
        		tmp = tmp.replaceAll("\\.", "").replaceAll("\\,", "."); //hapus seluruh tanda titik, lalu ganti seluruh tanda koma jadi titik
        		try {r.setIrd_last_oney(nf.parse(tmp).doubleValue());}catch(ParseException e) {r.setIrd_last_oney((double) 0);}
        		
        		//IRD_LAST_ONEYR
        		pos = baris[i].indexOf("<td class=\"isi1\">", pos)+17;
        		tmp = baris[i].substring(pos, baris[i].indexOf("</td>", pos));
        		tmp = tmp.replaceAll("\\.", "").replaceAll("\\,", "."); //hapus seluruh tanda titik, lalu ganti seluruh tanda koma jadi titik
        		try {r.setIrd_last_oneyr(nf.parse(tmp).doubleValue());}catch(ParseException e) {r.setIrd_last_oneyr((double) 0);}
        		
        		r.setIrd_trans_date(FormatDate.stringToDate(updateTerakhir));
        		
//            	logger.info(r.nama + " = [" + nf.format(r.nab) + "]" + 
//            			"[" + nf.format(r.inv_bln) + "]" + 
//            			"[" + nf.format(r.inv_thn) + "]" + 
//            			"[" + nf.format(r.inv_riil_thn) + "]");
        		
    			List<Reksadana> danareksa = elionsManager.selectReksadana(r.getIre_reksa_name());
    			if(!danareksa.isEmpty()) {
    				for(Reksadana a : danareksa) {
    					r.setIre_reksa_no(a.getIre_reksa_no());
    					// value = unit * nav
    					r.setIrd_unit(a.getIrd_unit());
    					r.setIrd_value(r.getIrd_unit() * r.getIrd_nav());
    					List udah = elionsManager.selectDetailReksadanaByDate(r);
    					if(udah.isEmpty()) {
    		        		result.add(r);
    					}
    				}
    			}        		
        	}        	
        }*/
        
        return result2;
	}
	
	public String processDataTgl(String data) throws ParseException {

		String tgl = "";
		String hasil = "";
		String hasil2 = "";
		
		String compile = "Reksa Dana hingga tanggal : ";
		
		int start = data.indexOf("Reksa Dana hingga tanggal : ");
		int end = start + compile.length() + 25;
		
		hasil = data.substring(start, end);
		
		int start2 = hasil.indexOf(": ") + 1;
		int end2 = hasil.indexOf("</b></font>");
		
		hasil2 = hasil.substring(start2, end2);
		
		tgl = hasil2.trim();
		
		tgl = tgl.replace('-', '/').replace("Jan", "01").replace("Feb", "02").replace("Mar", "03").replace("Apr", "04").replace("May", "05").replace("Jun", "06")
		.replace("Jul", "07").replace("Aug", "08").replace("Sep", "09").replace("Oct", "10").replace("Nov", "11").replace("Dec", "12");
        
        return tgl;
	}
	
}