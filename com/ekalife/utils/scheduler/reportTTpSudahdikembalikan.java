package com.ekalife.utils.scheduler;

import java.io.File;
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
 * @spring.bean Class ini untuk scheduling e-mail sender otomatis
 *
 */
public class reportTTpSudahdikembalikan extends ParentScheduler{
	protected final Log logger = LogFactory.getLog( getClass() );
	
	public void send(boolean isHtml, String from, String[] to, String[] cc, String[] bcc, String subject, String message, List<File> attachments) 
			throws MailException, MessagingException{
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
			Date bdate 	= new Date();
			String desc	= "OK";
			String err	= "";
			Calendar curr = Calendar.getInstance();
			curr.setTime(new Date());
			//int lastDay = curr.getActualMaximum(Calendar.DAY_OF_MONTH);
			int halfMonth = 15;
			String lastMonthLastDay = "";
			Calendar curr2 = Calendar.getInstance();
			curr2.set(curr.get(Calendar.YEAR), curr.get(Calendar.MONTH), halfMonth);
			
			if(curr2.get(Calendar.DAY_OF_WEEK) == 1) halfMonth = 17;
			//else if (curr2.get(Calendar.DAY_OF_WEEK) == 7) halfMonth = 17;
			if(halfMonth == curr.get(Calendar.DATE)) {
				curr2.set(curr.get(Calendar.YEAR), curr.get(Calendar.MONTH)-1, 1);
				lastMonthLastDay = (new Integer(curr2.getActualMaximum(Calendar.DAY_OF_MONTH))).toString()
									+"/"+
									(new Integer(curr2.get(Calendar.MONTH)+1)).toString()
									+"/"+
									(new Integer(curr2.get(Calendar.YEAR))).toString();
				//lastMonthLastDay = "30/7/2009";
			}
			
			//if(halfMonth == curr.get(Calendar.DATE) || 1 == curr.get(Calendar.DATE)) {
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
					
					logger.info("UW SCHEDULER KHUSUS BSM AT " + new Date());
					long start = System.currentTimeMillis();
					
					String outputDir = props.getProperty("pdf.dir.report") + "\\summary_akseptasi\\" + dateFormat.format(today) + "\\";
					
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
					Map<String, List<File>> daftarAttachment  = new HashMap<String, List<File>>();
					
					//Daftar Semua Report
					daftarReport.put("ttp_blm_dikembalikan", elionsManager.warning_ttp_komisigaDiproses());
					//Looping Utama dari daftar semua report
					for(String r : daftarReport.keySet()) {
						
						params.put("banyakMaunya", r);
						
						//Parameter Tambahan
						if(r.equals("ttp_blm_dikembalikan")) {
							params.put("judul", "Follow-Up Polis Expired");
						}
						
						
						if(r.equals("ttp_blm_dikembalikan")) {
							params.put("note",
								"Data yang masuk disini adalah data:\n" +
								"* Polis diaksep dengan kondisi khusus (polis yang sudah diaksep tetapi masih diperlukan data tambahan)");
						}else if(r.equals("Further_Requirements")) {
							params.put("note",
								"Note:\n" +
								"Data yang masuk disini adalah data :\n" +
								"* Polis yang belum diaksep, masih perlu data tambahan ( Further requirement )");
						}else {
							params.put("note", "");
						}
						
				
						List<Map> report = daftarReport.get(r);
						
						for(Map m : report){
							String spaj = (String)m.get("REG_SPAJ");
							String polis = (String)m.get("MSPO_POLICY_NO_FORMAT");
//							String lus_id = (String)m.get("LUS_ID");
							boolean emailCabangValid = false;
							boolean emailAgenValid = false;
							Integer jejak=0;
							jejak=elionsManager.count_ttp_komisigaDiproses(spaj);
							String emailCabang = uwManager.selectEmailCabangFromSpaj(spaj);
							Integer jmlh=0;
							jmlh= elionsManager.count_sdhlewatKom(spaj);

							
							Map infoAgen = elionsManager.selectEmailAgen(spaj);
							String emailAgen = (String) infoAgen.get("MSPE_EMAIL");
							
							
							String alamat = "";
							String cc = "";
							
							//validasi email agen
							if(emailAgen!=null) {
								if(!emailAgen.trim().equals("")) {
									if(emailAgen.toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
										emailAgenValid = true;
										alamat += ";" + emailAgen;
									}
								}
							}
//							validasi email cabang
							if(emailCabang!=null) {
								if(!emailCabang.trim().equals("")) {
									if(emailCabang.toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
										emailCabangValid = true;
										alamat += ";" + emailCabang;
									}
								}
							}
							if ((jejak.equals(0)|| jejak==0)&&(jmlh.equals(0)||jmlh==0)){
								
								uwManager.insertMst_position_spaj_ttp( "KOMISI NB TIDAK DIBAYAR KARENA TTP LEWAT 40 HARI", spaj);
							}
//								uwManager.insertMst_position_spaj_ttp(spaj, "KOMISI NB TIDAK DIBAYAR KARENA TTP LEWAT 30 HARI");
								elionsManager.updateMscoTdkAdaKomisi(spaj);
								if(alamat != null) {
									if(!alamat.trim().equals("")) {
										try {
											email.send(true, 
													props.getProperty("admin.ajsjava"), 
													alamat.split(";"), 
													cc.split(";"),
													null, 
													"Pemberitahuan Komisi Pertama  Polis No " + polis +" tidak dapat dibayarkan",
													"<table width=100% class=satu>"
													+"<tr><td colspan='3'>Diinformasikan bahwa Komisi Pertama Polis  No : "+  polis + " tidak dapat dibayarkan  karena TTP   diterima di  Kantor Pusat melebihi   1 bulan  setelah polis diterbitkan" + "</td></tr>"
													+"<tr><td colspan='3'>&nbsp;</td></tr>"
													+"<tr><td colspan='3'>&nbsp;</td></tr>"
													+"<tr><td colspan='3'>Demikian yang dapat kami sampaikan.</td></tr>"
													+"<tr><td colspan='3'>Terima kasih .</td></tr>"
													+"<tr><td colspan='3'>&nbsp;</td></tr>"
													+"<tr><td colspan='3'>&nbsp;</td></tr>"
													+"<tr><td><img src='cid:myLogo'></td><td colspan='2'>&nbsp;</td></tr>"
													+"<tr><td><font size='2'>Abu Jamal</font></td><td colspan='2'>&nbsp;</td></tr>"
													+"<tr><td><font size='2'>Underwriting Dept.</font> </td><td colspan='2'>&nbsp;</td></tr>"
													+"<tr><td><font size='2'>PT Asuransi Jiwa Sinarmas MSIG Tbk. </font></td><td colspan='2'>&nbsp;</td></tr>"
													+"<tr><td><font size='2'>Wisma Eka Jiwa Lt.8 </font></td><td colspan='2'>&nbsp;</td></tr>"
													+"<tr><td><font size='2'>JL. Mangga Dua Raya, Jkt 10730</font> </td><td colspan='2'>&nbsp;</td></tr>"
													+"<tr><td><font size='2'>Telp.+62(021)6257808 </font></td><td colspan='2'>&nbsp;</td></tr>"
													+"<tr><td><font size='2'>Fax. +62(021)6257779</font> </td><td colspan='2'>&nbsp;</td></tr>"
													+"<tr><td colspan='3'><font size='1'>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.</font></td></tr>"
													+"</table>",null);
									
										}catch(Exception e) {}
									}
								}
//							}else{
//							}
						}
						
						List<Map> temp = new ArrayList<Map>();
						Map<String, List<Map>> cabang = new HashMap<String, List<Map>>();
						//Looping untuk menyimpan file PDF berdasarkan masing2 distribusi
				
					//email semua report
					for (String s : daftarReport.keySet()) {
						
//						String team_name = (String) m.get("TEAM_NAME");
						List<Map> report1 = daftarReport.get(r);
						
						for(Map m : report){
							
							String spaj = (String)m.get("REG_SPAJ");
							String polis = (String)m.get("MSPO_POLICY_NO_FORMAT");
//							String lus_id = (String)m.get("LUS_ID");
							boolean emailCabangValid = false;
							boolean emailAgenValid = false;
							Integer jejak=0;
							jejak=elionsManager.count_ttp_komisigaDiproses(spaj);
							String emailCabang = uwManager.selectEmailCabangFromSpaj(spaj);
							Integer jmlh=0;
							jmlh= elionsManager.count_sdhlewatKom(spaj);
//							if ((jejak.equals(0)|| jejak==0)&&(jmlh.equals(0)||jmlh==0)){
									String outputFilename = r + "_" + ".xls";
									JasperUtils.exportReportToXls(props.getProperty("report.uw.summary." + r) + ".jasper", 
											outputDir, outputFilename, params, report, props.getProperty("report.uw.summary.sub.total")+ ".jasper");
									if(daftarAttachment.get(s) == null) {
										List<File> x = new ArrayList<File>();
										x.add(new File(outputDir + "\\" + outputFilename));
										daftarAttachment.put(s,x);
										String[] to = new String[] {};
										String[] cc = new String[] {};
										List<File> attachments = daftarAttachment.get(s);
										StringBuffer pesan = new StringBuffer();
										pesan.append("Berikut Adalah Summary TTP Belum Di Kembalikan "+ " s/d "+df.format(yesterday));
										pesan.append("<br/><br/><br/><br/>");
							
										send(true, "admin.ajsjava", 
											new String[]{"santi@sinarmasmsiglife.co.id","ingrid@sinarmasmsiglife.co.id","asriwulan@sinarmasmsiglife.co.id"},
											cc,
											null,
											"Summary TTP Belum dikembalikan" +" s/d " + df.format(yesterday), 
											pesan.toString(), attachments);	
									}
							}
						}
//						}
					}
					long end = System.currentTimeMillis();
					logger.info("UW SCHEDULER BSM FINISHED AT " + new Date() + " in " + ( (float) (end-start) / 1000) + " SECONDS.");
				}catch (Exception e) {
					logger.error("ERROR :", e);
					err=e.getLocalizedMessage();
					desc = "ERROR";
				}	
				try {
					uwManager.insertMstSchedulerHist(
							InetAddress.getLocalHost().getHostName(),
							"SCHEDULER TTP SUDAH KEMBALI", bdate, new Date(), desc,err);
				} catch (UnknownHostException e) {
					logger.error("ERROR :", e);
				}
				
			}
		}

}