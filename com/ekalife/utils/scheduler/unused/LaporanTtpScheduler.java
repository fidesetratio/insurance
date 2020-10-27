package com.ekalife.utils.scheduler.unused;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.mail.MailException;

import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentScheduler;

/**
 * @spring.bean Class ini untuk scheduling e-mail sender otomatis
 * 
 */
public class LaporanTtpScheduler extends ParentScheduler {

	public void send(boolean isHtml, String from, String[] to, String[] cc,
			String[] bcc, String subject, String message, List<File> attachments)
			throws MailException, MessagingException {
		// enable untuk debugging saja
		//to = new String[] {"yusuf@sinarmasmsiglife.co.id"};
		this.email.send(isHtml, from, to, cc, bcc, subject, message, attachments);
	}

	// main method
	public void main() throws Exception {
		if(jdbcName.equals("eka8i") && (InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))) {
			Date bdate = new Date();
			String desc = "OK";
			String err="";
			Connection conn = null;
			try {
				conn = this.elionsManager.getUwDao().getDataSource().getConnection();
				Date yesterday = elionsManager.selectSysdate(-1);
				Date today = elionsManager.selectSysdate(0);
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

				String outputDir = props.getProperty("pdf.dir.report")
						+ "\\lap_ttp_harian\\" + dateFormat.format(today)
						+ "\\";

				Map<String, Comparable> params = new HashMap<String, Comparable>();
				List<String> daftarReport = new ArrayList<String>();
				List<File> daftarAttachment = new ArrayList<File>();

				// Daftar Semua Report
				daftarReport.add("lap_ttp_harian");
				daftarReport.add("lap_ttp_bulanan");
				daftarReport.add("lap_ttp_tahunan");
				
				// Looping Utama dari daftar semua report, export dalam bentuk XLS, masukkan dalam daftar attachment untuk di email
				for (int i=0; i<daftarReport.size(); i++) {
					String reportName = daftarReport.get(i);
					JasperUtils.exportReportToXls(
							props.getProperty("report.uw.summary." + reportName) + ".jasper",
							outputDir,  reportName + ".xls", params, conn);
					
					daftarAttachment.add(new File(outputDir + "\\" + reportName + ".xls"));
				}
				
				// email semua report dalam 1 email
				StringBuffer pesan = new StringBuffer();
				pesan.append("Berikut Adalah Summary Proses Input TTP " + df.format(yesterday));
				pesan.append("<br/><br/><br/><br/>");

				send(true, "admin.ajsjava",
						new String[] {
								"ariani@sinarmasmsiglife.co.id",
								"ingrid@sinarmasmsiglife.co.id",
								"asriwulan@sinarmasmsiglife.co.id",
								"ariani@sinarmasmsiglife.co.id" },
						null, 
						null, 
						"Summary Proses Input TTP " + " s/d " + df.format(yesterday), pesan.toString(), daftarAttachment);
				
			} catch (Exception e) {
				logger.error("ERROR :", e);
				err=e.getLocalizedMessage();
				desc = "ERROR";
			}finally {
				this.elionsManager.getUwDao().closeConn(conn);
			}
			try {
				uwManager.insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(), "SCHEDULER PROSES INPUT TTP", bdate, new Date(), desc,err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}

		}
	}

}