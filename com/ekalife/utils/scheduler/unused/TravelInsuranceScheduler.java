package com.ekalife.utils.scheduler.unused;

import java.io.File;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentScheduler;
/**
 * @spring.bean Class ini untuk scheduling e-mail sender otomatis
 * 
 * @author Yusuf Sutarko
 * @since 11 Mar 2010
 */
public class TravelInsuranceScheduler extends ParentScheduler{
	protected final Log logger = LogFactory.getLog( getClass() );
	//main method
	public void main() throws Exception{
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df2 = new SimpleDateFormat("hh:mm");
		Date today = elionsManager.selectSysdate(0);//df.parse("03/03/2010");// 
				
		logger.info("SUMMARY SCHEDULER AT " + new Date());
		long start = System.currentTimeMillis();
		
		if(jdbcName.equals("eka8i")) {
			Connection conn = null;
			try {
				conn = this.elionsManager.getUwDao().getDataSource().getConnection();
				List<File> attachments = new ArrayList<File>();
				
				//1. Report 1 : Summary RK (ENTRY > BAC > SUMMARY > BIASA > ALL)
				String outputDir = props.getProperty("pdf.dir.report") + "\\summary_rk\\";
				String outputFilename = "summary_rk_"+dateFormat.format(today)+".xls";
				
				Map<String, Comparable> params = new HashMap<String, Comparable>();
				params.put("tglAwal", today);
				params.put("tglAkhir", today);
				params.put("user_print", "SYSTEM");
				params.put("lus_id", "All");
				
				List<Map> reportSummary = uwManager.selectReportSummaryBiasa(params);
				//JasperUtils.exportReportToPdf(props.getProperty("report.summary.biasa")+".jasper", outputDir, outputFilename, params, reportSummary, PdfWriter.AllowPrinting, null, null);
				JasperUtils.exportReportToXls(props.getProperty("report.summary.biasa") + ".jasper", 
						outputDir, outputFilename, params, reportSummary, null);
				
				File sourceFile = new File(outputDir+"\\"+outputFilename);
				attachments.add(sourceFile);
				
				//2. Report 2 : Summary Premi Non Cash (REPORT > UNDERWRITING > REPORT PREMI NON CASH)
				outputFilename = "summary_rk_noncash_"+dateFormat.format(today)+".xls";
				
				params = new HashMap<String, Comparable>();
				params.put("tanggalAwal", df.format(today));
				params.put("tanggalAkhir", df.format(today));
				
				JasperUtils.exportReportToXls(props.getProperty("report.uw.report_PremiNonCash") + ".jasper", 
						outputDir, outputFilename, params, conn);
				
				sourceFile = new File(outputDir+"\\"+outputFilename);
				attachments.add(sourceFile);
				
				//3. Email the reports
				List<String> daftarEmailUnderwriter = new ArrayList<String>();
				for(Map m : reportSummary){
					String email = (String) m.get("LUS_EMAIL");
					if(!daftarEmailUnderwriter.contains(email)) daftarEmailUnderwriter.add(email);
				}
				String[] underwriters = new String[daftarEmailUnderwriter.size() + 2];
				for(int i=0; i<underwriters.length; i++){
					underwriters[i] = daftarEmailUnderwriter.get(i);
				}
				underwriters[underwriters.length-2] = "ingrid@sinarmasmsiglife.co.id";
				underwriters[underwriters.length-1] = "asriwulan@sinarmasmsiglife.co.id";
				
				email.send(true, 
						props.getProperty("admin.ajsjava"),
					
						new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
						//new String[]{"gesti@sinarmasmsiglife.co.id", "arnold@sinarmasmsiglife.co.id", "julina.hasan@sinarmasmsiglife.co.id"},
						//underwriters,
						null, 
						"Summary RK dari UW " + df.format(today),
						"Berikut adalah Laporan Summary RK dari UW."
						+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
						attachments);
				
				/* 
				for(Map m : reportSummary) {
					int add = 0;
					boolean sukses = false;
					
					while(!sukses){
						try {
							sukses = false;
							elionsManager.insertMstPositionSpaj("0", "Kirim Summary RK ke Accounting(" + df2.format((Date) m.get("TGL_INPUT")) + "|" + m.get("NO_PRE") + "|" + m.get("NO_VOUCHER") + "|" + numberFormat.format(((BigDecimal)m.get("JUMLAH")).doubleValue()) + ")", (String) m.get("KEY_JURNAL"), add);
							add = 0; sukses = true;
						} catch (Exception e) {
							add++; sukses = false;
						}
					}
				}
				*/
				
			} catch (Exception e) {
				email.send(false, 
						props.getProperty("admin.ajsjava"), 
						new String[]{props.getProperty("admin.yusuf")}, 
						null,
						null, 
						"ERROR Saat Generate Summary RK dari UW",
						"E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
						null);
				logger.error("ERROR :", e);
			}finally {
				this.elionsManager.getUwDao().closeConn(conn);
			}
			
		}
		
        long end = System.currentTimeMillis();
        logger.info("SUMMARY SCHEDULER FINISHED AT " + new Date() + " in " + ( (float) (end-start) / 1000) + " SECONDS.");
	}

}