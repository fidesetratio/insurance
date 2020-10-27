package com.ekalife.utils.scheduler;

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

import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentScheduler;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @spring.bean Class ini untuk scheduling e-mail sender otomatis
 * 
 * @author Yusuf Sutarko
 * @since Mar 14, 2008 (9:48:41 AM)
 */
public class EmailScheduler extends ParentScheduler{
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
		
		//1. Report List Bayar Komisi (Menu dapat diakses di Entry > UW > Tanda Terima > List Bayar Komisi)
		if(jdbcName.equals("eka8i") 
				&& 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				) 
		{
			Connection conn = null;
			try {
				conn = this.elionsManager.getUwDao().getDataSource().getConnection();
				String outputDir = props.getProperty("pdf.dir.report") + "\\list_byr_komisi";
				String outputFilename = "list_byr_komisi_"+dateFormat.format(today)+".pdf";
				
				Map<String, Comparable> params = new HashMap<String, Comparable>();
				//params.put("tanggalAwal", df.format(yesterday));
				params.put("tanggalAkhir", df.format(today));
				params.put("user", "SYSTEM");
				
				//Yusuf (29/07/09) - Request Iwen, karena bila scheduler tidak keluar, marketing marah2
				//list_pembayaran_komisi_2 = per tgl kemaren jam 13.31 s/d tgl hari ini jam 13.30 -> TIDAK DIPAKAI
				//list_pembayaran_komisi_3 = akumulatif dari dulu sampai dengan hari ini jam 13.30
				
				JasperUtils.exportReportToPdf(
						props.getProperty("report.uw.list_pembayaran_komisi_3")+".jasper", 
						outputDir, 
						outputFilename, 
						params, 
						conn, 
						PdfWriter.AllowPrinting, null, null);
				
				List<File> attachments = new ArrayList<File>();
				File sourceFile = new File(outputDir+"\\"+outputFilename);
				attachments.add(sourceFile);
				
				email.send(true, 
						props.getProperty("admin.ajsjava"), 
						new String[]{"murni@sinarmasmsiglife.co.id", "rosita@sinarmasmsiglife.co.id"}, 
						new String[]{props.getProperty("admin.yusuf"), "riyadi@sinarmasmsiglife.co.id", "dewi_andriyati@sinarmasmsiglife.co.id", "timmy@sinarmasmsiglife.co.id"},
						null, 
						//"List Bayar Komisi Tanggal " + df.format(yesterday) + " (13:31) s/d " + df.format(today) + " (13:30)",
						//"Berikut adalah Laporan List Bayar Komisi Tanggal " + df.format(yesterday) + " (13:31) s/d " + df.format(today) + " (13:30)"
						"List Bayar Komisi Sampai Dengan Tanggal " + df.format(today) + " (13:30)",
						"Berikut adalah Laporan List Bayar Komisi Sampai Dengan Tanggal " + df.format(today) + " (13:30)"
						+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
						attachments);

				List<Map> listBayarKomisi = elionsManager.selectListBayarKomisi(
						df.format(yesterday) + " 13:31",
						df.format(today) + " 13:30");
				
				for(Map m : listBayarKomisi) {
					boolean emailCabangValid = false;
					boolean emailAgenValid = false;

					//tarik email cabang dan email agen
					String reg_spaj = (String) m.get("REG_SPAJ");
					String emailCabang = uwManager.selectEmailCabangFromSpaj(reg_spaj);
					
					Map infoAgen = elionsManager.selectEmailAgen(reg_spaj);
					String emailAgen = (String) infoAgen.get("MSPE_EMAIL");
					
					//String alamat = props.getProperty("admin.yusuf");
					String alamat = "";
					
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
					
					if(alamat != null) {
						if(!alamat.trim().equals("")) {
							try {
								email.send(true, 
										props.getProperty("admin.ajsjava"), 
										alamat.split(";"), 
										null,
										null, 
										//"Pembayaran Komisi tanggal " + df.format(yesterday) + " (13:31) s/d " + df.format(today) + " (13:30)",
										"Pembayaran Komisi Sampai Dengan Tanggal " + df.format(today) + " (13:30)",
										"Komisi Polis " + m.get("NO_POLIS") + " atas nama " + m.get("PEMEGANG") + " (= Pemegang Polis) sedang diproses dibagian Finance." 
										+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
										null);
							}catch(Exception e) {}
						}
					}
				}
			} catch (Exception e) {
				email.send(false, 
						props.getProperty("admin.ajsjava"), 
						new String[]{props.getProperty("admin.yusuf")}, 
						null,
						null, 
						//"Pembayaran Komisi tanggal " + df.format(yesterday) + " (13:31) s/d " + df.format(today) + " (13:30)",
						"ERROR Saat Generate Pembayaran Komisi Sampai Dengan Tanggal " + df.format(today) + " (13:30)",
						"E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
						null);
				logger.error("ERROR :", e);
				err=e.getLocalizedMessage();
				desc = "ERROR";
			}finally{
				this.elionsManager.getUwDao().closeConn(conn);
			}	
			
			try {
				uwManager.insertMstSchedulerHist(
						InetAddress.getLocalHost().getHostName(),
						"SCHEDULER EMAIL KOMISI", bdate, new Date(), desc,err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
			
		}
		
        long end = System.currentTimeMillis();
        logger.info("E-MAIL SCHEDULER FINISHED AT " + new Date() + " in " + ( (float) (end-start) / 1000) + " SECONDS.");
	}

}