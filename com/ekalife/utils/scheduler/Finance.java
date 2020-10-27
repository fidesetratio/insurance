package com.ekalife.utils.scheduler;

import java.io.File;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.mail.MailException;

import com.ekalife.utils.FormatDate;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentScheduler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @spring.bean Class ini untuk scheduling e-mail sender otomatis dept UW
 * 
* @author dian 
 * @since june 17, 2009 (1:38:40 PM)
 */
public class Finance extends ParentScheduler{
	protected final Log logger = LogFactory.getLog( getClass() );
	
	public void send(boolean isHtml, String from, String[] to, String[] cc, String[] bcc, String subject, String message, List<File> attachments) 
			throws MailException, MessagingException{
		//enable untuk debugging saja
//		to = new String[] {"yusup_a@sinarmasmsiglife.co.id"};
//		cc = new String[] {"yusup_a@sinarmasmsiglife.co.id"};
//		bcc = new String[] {"yusup_a@sinarmasmsiglife.co.id"};
		
		this.email.send(isHtml, from, to, cc, bcc, subject, message, attachments);
	}
	
	//main method
	public void main() throws Exception{
		if(jdbcName.equals("eka8i") && 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				) {
			//1. Summary untuk status2 aksep yg masih terpending, seperti further dkk
			summaryExpired();
		}
	}

	public void summaryExpired() throws Exception{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";

		try {
			Date yesterday = elionsManager.selectSysdate(-1);
			Date today = elionsManager.selectSysdate(0);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			//Deddy (13/3/2009) tambahan untuk menentukan tahun -1 dari sekarang 
			DateFormat year = new SimpleDateFormat("yyyy");
			String yearbefore = year.format(FormatDate.add(today, Calendar.YEAR, -1) );
			
			//Deddy(16/3/2009) tambahan untuk menentukan bulan1-12 dari tahun sekarang
			DateFormat monthyear = new SimpleDateFormat("mmyyyy");
			String month1 = "01"+ year.format(today);
			String month2 = "02"+ year.format(today);
			String month3 = "03"+ year.format(today);
			String month4 = "04"+ year.format(today);
			String month5 = "05"+ year.format(today);
			String month6 = "06"+ year.format(today);
			String month7 = "07"+ year.format(today);
			String month8 = "08"+ year.format(today);
			String month9 = "09"+ year.format(today);
			String month10 = "10"+ year.format(today);
			String month11 = "11"+ year.format(today);
			String month12 = "12"+ year.format(today);
			
			
			logger.info("UW SCHEDULER AT " + new Date());
			long start = System.currentTimeMillis();
			
			//Report Summary (Menu dapat diakses di Entry > UW > Akseptasi Khusus > Summary)
			
			String outputDir = props.getProperty("pdf.dir.report") + "\\summary_akseptasi\\" + dateFormat.format(today) + "\\";
//			String outputDir = "D:\\Test\\";
			
			Map<String, Comparable> params = new HashMap<String, Comparable>();
			params.put("tanggal", df.format(today));
			params.put("user", "SYSTEM");
			
			//passing parameter ke reportnya
			params.put("yearbefore", yearbefore);
			params.put("month1", month1);
			params.put("month2", month2);
			params.put("month3", month3);
			params.put("month4", month4);
			params.put("month5", month5);
			params.put("month6", month6);
			params.put("month7", month7);
			params.put("month8", month8);
			params.put("month9", month9);
			params.put("month10", month10);
			params.put("month11", month11);
			params.put("month12", month12);

			Map<String, List<Map>> distribusi 			= new HashMap<String, List<Map>>();
			Map<String, List<Map>> daftarReport 		= new HashMap<String, List<Map>>();
			Map<String, List<File>> daftarAttachment1 	= new HashMap<String, List<File>>();
			Map<String, List<File>> daftarAttachment2 	= new HashMap<String, List<File>>();
			
			daftarAttachment1.put("Agency", 	new ArrayList<File>());
			daftarAttachment1.put("Hybrid", 	new ArrayList<File>());
			daftarAttachment1.put("Regional", 	new ArrayList<File>());
			daftarAttachment1.put("Bancass1", 	new ArrayList<File>());
			daftarAttachment1.put("Bancass2", 	new ArrayList<File>());
			daftarAttachment1.put("Bancass3", 	new ArrayList<File>());
			daftarAttachment1.put("Worksite", 	new ArrayList<File>());
			daftarAttachment1.put("DMTM", 		new ArrayList<File>());
			
			daftarAttachment2.put("Agency", 	new ArrayList<File>());
			daftarAttachment2.put("Hybrid", 	new ArrayList<File>());
			daftarAttachment2.put("Regional", 	new ArrayList<File>());
			daftarAttachment2.put("Bancass1", 	new ArrayList<File>());
			daftarAttachment2.put("Bancass2", 	new ArrayList<File>());
			daftarAttachment2.put("Bancass3", 	new ArrayList<File>());
			daftarAttachment2.put("Worksite", 	new ArrayList<File>());
			daftarAttachment2.put("DMTM", 		new ArrayList<File>());
			
			String b;
			//Daftar Semua Report
			daftarReport.put("Finance", elionsManager.financeTopUp(false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12)); 				//5. polis expired
			
			//Looping Utama dari daftar semua report
			for(String r : daftarReport.keySet()) {
				
				params.put("banyakMaunya", r);
				
				//Parameter Tambahan
				if(r.equals("Expired")) {
					params.put("judul", "Follow-Up Polis Expired");
				}
		
				if(r.equals("Expired")) {
					params.put("note",
						"Note:\n" +
						"Data yang masuk disini adalah data :\n" +
						"* Polis yang expired > 90 hari ( Polis Expired > 90 Hari )");
				}else {
					params.put("note", "");
				}
				
				//Daftar pembagian distribusi, direset setiap loop
				distribusi.put("Agency", 	new ArrayList<Map>());
				distribusi.put("Hybrid", 	new ArrayList<Map>());
				distribusi.put("Regional", 	new ArrayList<Map>());
				distribusi.put("Bancass1", 	new ArrayList<Map>());
				distribusi.put("Bancass2", 	new ArrayList<Map>());
				distribusi.put("Bancass3", 	new ArrayList<Map>());
				distribusi.put("Worksite", 	new ArrayList<Map>());
				distribusi.put("DMTM", 		new ArrayList<Map>());
				
				List<Map> report = daftarReport.get(r);
				
				//Looping untuk membagi2 hasil query ke masing2 distribusi
				for(Map m : report) {
					String lca_id = (String) m.get("LCA_ID");
					String team_name = (String) m.get("TEAM_NAME");
					BigDecimal jenis = (BigDecimal) m.get("JN_BANK");
					int jn_bank = (jenis == null ? 0 : jenis.intValue());
					
					if(team_name== null){
						team_name= "";
					}
					
					if("37,52".indexOf(lca_id) > -1){
//					if(lca_id.equals("37")) { //Agency
						((List<Map>) distribusi.get("Agency")).add(m);
					}else if(lca_id.equals("46")) { //Hybrid
						((List<Map>) distribusi.get("Hybrid")).add(m);
					}else if(lca_id.equals("09")) { //Bancassurance
						if(team_name.toUpperCase().equals("TEAM JAN ROSADI")) { //Bancassurance2
							((List<Map>) distribusi.get("Bancass2")).add(m);
						}else if(team_name.toUpperCase().equals("TEAM DEWI")) { //Bancassurance3
							((List<Map>) distribusi.get("Bancass3")).add(m);
						}else { //Bancassurance1
							((List<Map>) distribusi.get("Bancass1")).add(m);
						}
					}else if(lca_id.equals("08") || lca_id.equals("42")) { //Worksite
						((List<Map>) distribusi.get("Worksite")).add(m);
					}else if(lca_id.equals("55")) { //DM/TM
						((List<Map>) distribusi.get("DMTM")).add(m);
					}else { //Regional
						((List<Map>) distribusi.get("Regional")).add(m);
					}
				}
				
				
				//Looping untuk menyimpan file PDF berdasarkan masing2 distribusi
				for(String s : distribusi.keySet()) {
					List<Map> daftar = distribusi.get(s);
					
					String cab_bank = "";
					if(s.equals("Bancass1")) cab_bank = "1";
					else if(s.equals("Bancass2")) cab_bank = "2";
					
					
					params.put("cab_bank", cab_bank);
					if(!daftar.isEmpty()) {

						//Bagian ini untuk menghasilkan file dalam bentuk pdf
//						String outputFilename = r + "_" + s + ".pdf";
//						JasperUtils.exportReportToPdf(
//								props.getProperty("report.uw.summary." + r) + ".jasper", 
//								outputDir, outputFilename, params, daftar, PdfWriter.AllowPrinting, null, null);
						
//						Bagian ini untuk menghasilkan file dalam bentuk Xcel
						String outputFilename = r + "_" + s + ".xls";
						JasperUtils.exportReportToXls(props.getProperty("report.uw.summary." + r) + ".jasper", 
								outputDir, outputFilename, params, daftar, props.getProperty("report.uw.summary.sub.total")+ ".jasper");

//						buat testing						
//						JasperUtils.exportReportToXls(props.getProperty("report.uw.summary." + r) + ".jasper", 
//								props.getProperty("upload.dir"), outputFilename, params, daftar, props.getProperty("report.uw.summary.sub.total")+ ".jasper");
						
						if(r.equals("Akseptasi_Khusus") || r.equals("Further_Requirements")) {
							List<File> attachments = daftarAttachment1.get(s);
							attachments.add(new File(outputDir + "\\" + outputFilename));
						}else {
							List<File> attachments = daftarAttachment2.get(s);
							attachments.add(new File(outputDir + "\\" + outputFilename));
						}						
					}
				}
			}
			Integer flag =0;
			//Looping untuk send email per masing2 distribusi
			for(String a : daftarAttachment1.keySet()) {
				List<File> attachments1 = daftarAttachment1.get(a);
				List<File> attachments2 = daftarAttachment2.get(a);
				String to = "";
				String cc = "";
				
				to = "Liana;Djunaedi;"; 
				cc = "Ety;Aprina;Kamarudinsyah"; 				
				//to dan cc nya @sinarmasmsiglife.co.id
				String[] emailTo = to.split(";");
				String[] emailCc = cc.split(";");
				
				for(int y=0; y<emailTo.length; y++){
					emailTo[y] = emailTo[y].concat("@sinarmasmsiglife.co.id");
				}
				for(int y=0; y<emailCc.length; y++){
					emailCc[y] = emailCc[y].concat("@sinarmasmsiglife.co.id");
				}
				
				//E-mail 1 : Akseptasi_Khusus & Further_Requirements
				if(!attachments2.isEmpty()) {
					Integer file=attachments2.size();
					
					if (a.equals("Agency")||a.equals("Hybrid")||a.equals("Regional")){
						if (flag==1){
						send(true, "ajsjava@sinarmasmsiglife.co.id", 
								emailTo, emailCc, null,
								//new String[]{"Deddy@sinarmasmsiglife.co.id,Yusup_a@sinarmasmsiglife.co.id"},null,null,
								"List SPAJ Top Up Stablelink Agency/Hybrid/Regional  " + a + " s/d " + df.format(yesterday), 
								"List SPAJ Stablelink " + a + " s/d " + df.format(yesterday) + 
								"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", attachments2);
						}else{
							
						}
					
					}else
						send(true, "ajsjava@sinarmasmsiglife.co.id", 
							emailTo, emailCc, null,
							//new String[]{"Deddy@sinarmasmsiglife.co.id,Yusup_a@sinarmasmsiglife.co.id"},null,null,
							"List SPAJ Top Up Stablelink   " + a + " s/d " + df.format(yesterday), 
							"List SPAJ Top Up Stablelink  " + a + " s/d " + df.format(yesterday) + 
							"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", attachments2);
				}
				
			}

			long end = System.currentTimeMillis();
			logger.info("Finance SCHEDULER FINISHED AT " + new Date() + " in " + ( (float) (end-start) / 1000) + " SECONDS.");
		} catch (Exception e) {
			logger.error("ERROR :", e);
			err=e.getLocalizedMessage();
			desc = "ERROR";
		}		
		try {
			uwManager.insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					"SCHEDULER SUMMARY TOPUP FINANCE", bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	
}