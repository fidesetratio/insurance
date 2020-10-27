package com.ekalife.utils.scheduler;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentScheduler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @spring.bean Class ini untuk scheduling e-mail sender otomatis
 *
 */
public class WarningTtp extends ParentScheduler{
	protected final Log logger = LogFactory.getLog( getClass() );
	//main method
	public void main() throws Exception{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";
		Date yesterday = elionsManager.selectSysdate(-1);
		Date today = elionsManager.selectSysdate(0);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		logger.info("E-MAIL SCHEDULER AT " + new Date());
		long start = System.currentTimeMillis();
		
		if(jdbcName.equals("eka8i") && 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				) {
//		if(jdbcName.equals("ekatest")) {
			try {
				String outputDir = props.getProperty("pdf.dir.report") + "\\warningttp";
				String outputFilename = "warning_ttp"+dateFormat.format(today)+".pdf";
				
				Map<String, Comparable> params = new HashMap<String, Comparable>();
				
				

				List<Map> warning_ttp =elionsManager.warning_ttp();
//				STRING PEMEGANG =WARNING_TTP.
				for(Map m : warning_ttp) {
					boolean emailCabangValid = false;
					boolean emailAgenValid = false;
					String css = props.getProperty("email.uw.css.satu")
					+ props.getProperty("email.uw.css.dua");
					
					String reg_spaj = (String) m.get("REG_SPAJ");
					String no_polis = (String) m.get("MSPO_POLICY_NO");
					String pemegang = (String) m.get("PEMEGANG");
					String lca_id = (String) m.get("LCA_ID");
					String email_AO=(String)m.get("MAIL_AO");
					
//				JasperUtils.exportReportToPdf(
//						props.getProperty("report.uw.warning_ttp")+".jasper", 
//						outputDir, 
//						outputFilename, 
//						params, 
//						getConnection(), 
//						PdfWriter.AllowPrinting, null, null);
//				

				
				List<File> attachments = new ArrayList<File>();
				File sourceFile = new File(outputDir+"\\"+outputFilename);
				attachments.add(sourceFile);
				
				String emailCabang = uwManager.selectEmailCabangFromSpaj(reg_spaj);
				
				Map infoAgen = elionsManager.selectEmailAgen(reg_spaj);
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
				//validasi email cabang
				if(emailCabang!=null) {
					if(!emailCabang.trim().equals("")) {
						if(emailCabang.toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
							emailCabangValid = true;
							alamat += ";" + emailCabang;
						}
					}
				}
//				
//				cc="irene_o@sinarmasmsiglife.co.id";
				if(lca_id.equals("42")){
					cc="santi@sinarmasmsiglife.co.id;martino@sinarmasmsiglife.co.id;huluk@sinarmasmsiglife.co.id;desy@sinarmasmsiglife.co.id";
				}else{
					cc="santi@sinarmasmsiglife.co.id;ani_chrys@sinarmasmsiglife.co.id;martino@sinarmasmsiglife.co.id;huluk@sinarmasmsiglife.co.id;desy@sinarmasmsiglife.co.id";
				}
				if(alamat != null) {
					if(!alamat.trim().equals("")) {
						try {
							email.send(true, 
									props.getProperty("admin.ajsjava"), 
									alamat.split(";"), 
									cc.split(";"),
									null, 
									//"Pembayaran Komisi tanggal " + df.format(yesterday) + " (13:31) s/d " + df.format(today) + " (13:30)",
									"Pengembalian TTP an " + pemegang + "  Nomor Polis  "+ no_polis+ "",
									"<table width=100% class=satu>"
									+"<tr><td colspan='3'>Harap TTP untuk nasabah  di bawah ini segera dikirimkan ke bagian Underwriting : </td></tr>"
									+"<tr><td colspan='3'>&nbsp;</td></tr>"
									+ "<tr><td width='236'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;No. Polis 	  </td><td width='5'>:</td><td width='767'>" + FormatString.nomorPolis(no_polis) + "</td></tr>"
									+ "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Nama Pemegang Polis </td><td>:</td><td>" + pemegang + ""+ "</td></tr>" 
									+ "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Produk		  </td><td>:</td><td>" +(String)m.get("LSDBS_NAME") + "</td></tr>"
									+ "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Tanggal kirim Polis</td><td>:</td><td>" +(String)m.get("TGL_KIRIM") + "</td></tr>"
									+"<tr><td colspan='3'>&nbsp;</td></tr>"
									+"<tr><td colspan='3'>Kami tunggu TTP <b>paling lambat tanggal " + (String)m.get("TGL_TERIMA") + "</b></td></tr> "
									+"<tr><td colspan='3'>Apabila TTP diterima setelah tanggal tersebut  maka komisi pertama tidak akan diproses.</td></tr>"
									+"<tr><td colspan='3'>&nbsp;</td></tr>"
									+"<tr><td colspan='3'>Demikian yang dapat kami sampaikan. Terima kasih .</td></tr>"
									+"<tr><td colspan='3'>&nbsp;</td></tr>"
									+"<tr><td colspan='3'>&nbsp;</td></tr>"
									+"<tr><td><img src='cid:myLogo'></td><td colspan='2'>&nbsp;</td></tr>"
									+"<tr><td><font size='2'>Santi Oktaviana</font></td><td colspan='2'>&nbsp;</td></tr>"
									+"<tr><td><font size='2'>Underwriting Dept.</font> </td><td colspan='2'>&nbsp;</td></tr>"
									+"<tr><td><font size='2'>PT Asuransi Jiwa Sinarmas MSIG Tbk. </font></td><td colspan='2'>&nbsp;</td></tr>"
									+"<tr><td><font size='2'>Wisma Eka Jiwa Lt.8 </font></td><td colspan='2'>&nbsp;</td></tr>"
									+"<tr><td><font size='2'>JL. Mangga Dua Raya, Jkt 10730</font> </td><td colspan='2'>&nbsp;</td></tr>"
									+"<tr><td><font size='2'>Telp.+62(021)6257808 </font></td><td colspan='2'>&nbsp;</td></tr>"
									+"<tr><td><font size='2'>Fax. +62(021)6257779</font> </td><td colspan='2'>&nbsp;</td></tr>"
									+"<tr><td colspan='3'><font size='1'>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.</font></td></tr>"
									+"</table>", 
									attachments);
					
						}catch(Exception e) {}
					}
				}
			}

			} catch (Exception e) {
				email.send(false, 
						props.getProperty("admin.ajsjava"), 
						new String[]{"yusuf@sinarmasmsiglife.co.id"}, 
						null,
						null, 
						"ERROR Saat Generate WARNING TTP " + df.format(today) + " (13:30)",
						"E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
						null);
				logger.error("ERROR :", e);
				err=e.getLocalizedMessage();
				desc = "ERROR";
			}
			try {
				uwManager.insertMstSchedulerHist(
						InetAddress.getLocalHost().getHostName(),
						"SCHEDULER WARNING TTP", bdate, new Date(), desc,err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		
		}
		
        long end = System.currentTimeMillis();
        logger.info("E-MAIL TTP SCHEDULER FINISHED AT " + new Date() + " in " + ( (float) (end-start) / 1000) + " SECONDS.");
	}

}