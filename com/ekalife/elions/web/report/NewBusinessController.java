package com.ekalife.elions.web.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.jasper.JasperReportsCustomView;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.jasper.Report;
import com.ekalife.utils.parent.ParentJasperReportingController;
import com.lowagie.text.pdf.PdfWriter;

import id.co.sinarmaslife.std.model.vo.DropDown;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

/**
 * @author Yusuf
 * @since June 5, 2008
 */
public class NewBusinessController extends ParentJasperReportingController{
	
	private String PressButton; 
	
	public ModelAndView bank_muamalat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Date sysdate = elionsManager.selectSysdate();
		
		List<DropDown> reportPathList = new ArrayList<DropDown>();
		reportPathList.add(new DropDown(props.getProperty("report.muamalat.bank_muamalat"), "SPAJ DIAKSEP"));
		reportPathList.add(new DropDown(props.getProperty("report.muamalat.tolak_bank_muamalat"), "SPAJ DITOLAK"));
		Report report;
		report = new Report("Report Bank Muamalat", reportPathList, Report.PDF, null);
		//report.addParamSelect("status", "status", elionsManager.selectListStatusMuamalat(), "AKSEP", false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		return prepareReport(request, response, report);
		
		
//		Map cmd = new HashMap();
//		Date sysdate = elionsManager.selectSysdateSimple();
//		HttpSession session = request.getSession();
//		User currentUser = (User) session.getAttribute("currentUser");
//		
//		String tglAwal = ServletRequestUtils.getStringParameter(request, "tglAwal", defaultDateFormat.format(FormatDate.add(sysdate, Calendar.DATE, -1)));
//		String tglAkhir = ServletRequestUtils.getStringParameter(request, "tglAkhir", defaultDateFormat.format(sysdate));
//		cmd.put("tglAwal", tglAwal);
//		cmd.put("tglAkhir", tglAkhir);
//		cmd.put("reportPath", "report/uw.htm?window=loading_screen");
//		
//		if(request.getParameter("show") != null){
//			String status = ServletRequestUtils.getStringParameter(request, "status", "");
//			
//			session.setAttribute("tglAwal", tglAwal);
//			session.setAttribute("tglAkhir", tglAkhir);
//			session.setAttribute("status", status);
//			
//			disini merupakan proses untuk menampilkan ke report
//			if(status.equals("AKSEP") || status.equals("TOLAK")){
//				Map<JRExporterParameter, Object> exp = new HashMap<JRExporterParameter, Object>(); 
//				exp.put(JRPdfExporterParameter.IS_ENCRYPTED, Boolean.TRUE);
//				exp.put(JRPdfExporterParameter.IS_128_BIT_KEY, Boolean.TRUE);
//				exp.put(JRPdfExporterParameter.PERMISSIONS, new Integer(PdfWriter.AllowPrinting));
//				
//				Map params = new HashMap();
//				
//				params.put("format", "PDF");
//				params.put("tglAwal", tglAwal);
//				params.put("tglAkhir", tglAkhir);
//				
//				if(status.equals("AKSEP")){
//					String reportPath = props.getProperty("report.muamalat.bank_muamalat") + ".jasper";
//					//AbstractJasperReportsView jasperViewer = new JasperReportsAppletView(); 
//					AbstractJasperReportsView jasperViewer = new JasperReportsCustomView(response, props, exp, null, null, "bank_muamalat", false);
//					jasperViewer.setExporterParameters(exp);
//					jasperViewer.setUrl("/WEB-INF/classes/"+reportPath);
//					Object dataSource;
//					dataSource = this.getDataSource();
//					jasperViewer.setJdbcDataSource( (DataSource) dataSource);
//					
//					params.put("dataSource", dataSource);
//					
//					jasperViewer.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()));
//					cmd.put("reportPath", jasperViewer);
//					cmd.put("status", status);
//					
//					return new ModelAndView(jasperViewer, params);
//				}else if(status.equals("TOLAK")){
//					
//				}
//				
//			}
//			
//			cmd.put("status", status);
//			cmd.put("reportPath", "report/nb.pdf?window=show_report_muamalat");
//		}
		
//		return new ModelAndView("report/bank_muamalat", "cmd", cmd);
	}
	
	public ModelAndView show_report_muamalat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<JRExporterParameter, Object> exp = new HashMap<JRExporterParameter, Object>(); 
		exp.put(JRPdfExporterParameter.IS_ENCRYPTED, Boolean.TRUE);
		exp.put(JRPdfExporterParameter.IS_128_BIT_KEY, Boolean.TRUE);
		exp.put(JRPdfExporterParameter.PERMISSIONS, new Integer(PdfWriter.AllowPrinting));
		
		HttpSession session = request.getSession();
		
//		AbstractJasperReportsView jasperViewer = new JasperReportsAppletView(); 
		AbstractJasperReportsView jasperViewer = new JasperReportsCustomView(response, props, exp, null, null, "bank_muamalat", false, null, false);
		jasperViewer.setExporterParameters(exp);
		
		Map params = new HashMap();
		String status = (String) session.getAttribute("status");
		String tglAwal = (String) session.getAttribute("tglAwal");
		String tglAkhir = (String) session.getAttribute("tglAkhir");
		params.put("tglAwal", (String) session.getAttribute("tglAwal"));
		params.put("tglAKhir", (String) session.getAttribute("tglAkhir"));
		
		if(status.equals("AKSEP") || status.equals("TOLAK")){
			if(status.equals("AKSEP")){
				String reportPath = props.getProperty("report.muamalat.bank_muamalat") + ".jasper";
				jasperViewer.setUrl("/WEB-INF/classes/"+reportPath);
				
			}else if(status.equals("TOLAK")){
				String reportPath = props.getProperty("report.muamalat.tolak_bank_muamalat") + ".jasper";
				jasperViewer.setUrl("/WEB-INF/classes/"+reportPath);
			}
		}
		
		Object dataSource;
		//dataSource = this.getDataSource();
		dataSource = this.getUwManager().getUwDao().getDataSource().getConnection();
		jasperViewer.setJdbcDataSource( (DataSource) dataSource);
		
		params.put("dataSource", dataSource);
		
		jasperViewer.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()));
		
		return new ModelAndView(jasperViewer, params);
	}
	
	
	public ModelAndView pengiriman_polis(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//
		Map<JRExporterParameter, Object> exp = new HashMap<JRExporterParameter, Object>(); 
		exp.put(JRPdfExporterParameter.IS_ENCRYPTED, Boolean.TRUE);
		exp.put(JRPdfExporterParameter.IS_128_BIT_KEY, Boolean.TRUE);
		exp.put(JRPdfExporterParameter.PERMISSIONS, new Integer(PdfWriter.AllowPrinting));
		//
		String reportPath = props.getProperty("report.uw.list_pengiriman_polis") + ".jasper";
		//AbstractJasperReportsView jasperViewer = new JasperReportsAppletView(); 
		AbstractJasperReportsView jasperViewer = new JasperReportsCustomView(response, props, exp, null, null, "list_pengiriman", false, null, false);
		jasperViewer.setExporterParameters(exp);
		jasperViewer.setUrl("/WEB-INF/classes/"+reportPath);
		jasperViewer.setReportDataKey("dataSource");
		//
		Map params = new HashMap();
		HttpSession session = request.getSession();
		List result = (List) session.getAttribute("listPengirimanPolis");
		User currentUser = (User) session.getAttribute("currentUser"); 
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
		Date sysdate = elionsManager.selectSysdate();
		String from = null;
		
		params.put("dataSource", result);
		params.put("format", "PDF");
		params.put("userName", currentUser.getName());
		params.put("startDate", (String) session.getAttribute("startDate"));
		params.put("endDate", (String) session.getAttribute("endDate"));
		params.put("sysDate", df2.format((Date) session.getAttribute("sysDate")));
		jasperViewer.setSubReportDataKeys((String[]) params.get("subReportDatakeys"));
		//params.put("subDS", JasperReportsUtils.convertReportData(result));
		//
	
		if(PressButton != null){
			if(PressButton == "Save") {	
				if(currentUser.getLde_id().equals("11")) {
					from = "UW";
					String directory = props.getProperty("pdf.dir.report")+"\\"+"Pengiriman_Polis_LB"+"\\"+from;
					List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
					int i=1;
					String outputDir = "\\\\ebserver\\pdfind\\Report\\pengiriman_polis_lb\\uw\\"+from+"_"+df.format(sysdate)+"("+i+")"+".pdf";
					for(int x=0; x<daftarFile.size(); x++){
						DropDown y = daftarFile.get(x);
						String cek =  y.getKey();
						if(outputDir.contains(y.getKey())){
							i= i+1;
							outputDir = "\\\\ebserver\\pdfind\\Report\\pengiriman_polis_lb\\uw\\"+from+"_"+df.format(sysdate)+"("+i+")"+".pdf";
							//break;
						}
					}
					JasperUtils.exportReportToPdf(reportPath, outputDir, null, params, result, PdfWriter.AllowPrinting, null, null);
				}else if(currentUser.getLde_id().equals("27") || currentUser.getLde_id().equals("12")) {
					from = "LB";
					String directory = props.getProperty("pdf.dir.report")+"\\"+"Pengiriman_Polis_LB"+"\\"+from;
					List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
					int i=1;
					String outputDir = "\\\\ebserver\\pdfind\\Report\\pengiriman_polis_lb\\lb\\"+from+"_"+df.format(sysdate)+"("+i+")"+".pdf";
					for(int x=0; x<daftarFile.size(); x++){
						DropDown y = daftarFile.get(x);
						String cek =  y.getKey();
						if(outputDir.contains(y.getKey())){
							i= i+1;
							outputDir = "\\\\ebserver\\pdfind\\Report\\pengiriman_polis_lb\\lb\\"+from+"_"+df.format(sysdate)+"("+i+")"+".pdf";
							//break;
						}
					}
					JasperUtils.exportReportToPdf(reportPath, outputDir, null, params, result, PdfWriter.AllowPrinting, null, null);
				}else {from = "Lain2";
				String directory = props.getProperty("pdf.dir.report")+"\\"+"Pengiriman_Polis_LB"+"\\"+from;
				List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
				int i=1;
				String outputDir = "\\\\ebserver\\pdfind\\Report\\pengiriman_polis_lb\\"+from+"_"+df.format(sysdate)+"("+i+")"+".pdf";
					for(int x=0; x<daftarFile.size(); x++){
						DropDown y = daftarFile.get(x);
						//String cek =  y.getKey();
						if(outputDir.contains(y.getKey())){
							i= i+1;
							outputDir = "\\\\ebserver\\pdfind\\Report\\pengiriman_polis_lb\\"+from+"_"+df.format(sysdate)+"("+i+")"+".pdf";
							//break;
						}
					}
				JasperUtils.exportReportToPdf(reportPath, outputDir, null, params, result, PdfWriter.AllowPrinting, null, null);
				}
			}
			else if(PressButton == "Show_Report"){
				if(currentUser.getLde_id().equals("11")) {
					from = "UW";
					String directory = props.getProperty("pdf.dir.report")+"\\"+"Pengiriman_Polis_LB"+"\\"+"uw_show_report"+"\\";
					List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
					int i=1;
					String outputDir = "\\\\ebserver\\pdfind\\Report\\pengiriman_polis_lb\\uw_show_report\\"+from+"_"+df.format(sysdate)+"("+i+")"+".pdf";
					for(int x=0; x<daftarFile.size(); x++){
						DropDown y = daftarFile.get(x);
						//String cek =  y.getKey();
						if(outputDir.contains(y.getKey())){
							i= i+1;
							outputDir = "\\\\ebserver\\pdfind\\Report\\pengiriman_polis_lb\\uw_show_report\\"+from+"_"+df.format(sysdate)+"("+i+")"+".pdf";
							//break;
						}
					}
					JasperUtils.exportReportToPdf(reportPath, outputDir, null, params, result, PdfWriter.AllowPrinting, null, null);
				}else if(currentUser.getLde_id().equals("27") || currentUser.getLde_id().equals("12")) {
					from = "LB";
					String directory = props.getProperty("pdf.dir.report")+"\\"+"Pengiriman_Polis_LB"+"\\"+"lb_show_report"+"\\";
					List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
					int i=1;
					String outputDir = "\\\\ebserver\\pdfind\\Report\\pengiriman_polis_lb\\lb_show_report\\"+from+"_"+df.format(sysdate)+"("+i+")"+".pdf";
					for(int x=0; x<daftarFile.size(); x++){
						DropDown y = daftarFile.get(x);
						//String cek =  y.getKey();
						if(outputDir.contains(y.getKey())){
							i= i+1;
							outputDir = "\\\\ebserver\\pdfind\\Report\\pengiriman_polis_lb\\lb_show_report\\"+from+"_"+df.format(sysdate)+"("+i+")"+".pdf";
							//break;
						}
					}
					JasperUtils.exportReportToPdf(reportPath, outputDir, null, params, result, PdfWriter.AllowPrinting, null, null);
				}else {from = "Lain2";
				String directory = props.getProperty("pdf.dir.report")+"\\"+"Pengiriman_Polis_LB"+"\\"+from;
				List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
				int i=1;
				String outputDir = "\\\\ebserver\\pdfind\\Report\\pengiriman_polis_lb\\"+from+"_"+df.format(sysdate)+"("+i+")"+".pdf";
					for(int x=0; x<daftarFile.size(); x++){
						DropDown y = daftarFile.get(x);
						//String cek =  y.getKey();
						if(outputDir.contains(y.getKey())){
							i= i+1;
							outputDir = "\\\\ebserver\\pdfind\\Report\\pengiriman_polis_lb\\"+from+"_"+df.format(sysdate)+"("+i+")"+".pdf";
							//break;
						}
					}
				JasperUtils.exportReportToPdf(reportPath, outputDir, null, params, result, PdfWriter.AllowPrinting, null, null);
				}	
			}
		}
		jasperViewer.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()));
		return new ModelAndView(jasperViewer, params);		
	}
	//TODO
	public ModelAndView list_polis_main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map cmd = new HashMap();
		Date sysdate = elionsManager.selectSysdateSimple();
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		List daftar = new ArrayList();
		List daftarHistory = new ArrayList();
		List<DropDown> lsTolak = new ArrayList();
		lsTolak = elionsManager.selectAllLstTolak();
		session.removeAttribute("listPengirimanPolis");
		//session.removeAttribute("startDate");
		session.removeAttribute("endDate");
		session.removeAttribute("sysDate");
		session.removeAttribute("hsStartDate");
		session.removeAttribute("hsEndDate");

		String startDate = ServletRequestUtils.getStringParameter(request, "startDate", defaultDateFormat.format(FormatDate.add(sysdate, Calendar.DATE, -14)));
		String endDate = ServletRequestUtils.getStringParameter(request, "endDate", defaultDateFormat.format(FormatDate.add(sysdate, Calendar.DATE, -13)));
		//String startDate = ServletRequestUtils.getStringParameter(request, "startDate", "06/07/2007");
		//String endDate = ServletRequestUtils.getStringParameter(request, "endDate", "07/07/2007");
		String hsStartDate = ServletRequestUtils.getStringParameter(request, "hsStartDate", defaultDateFormat.format(FormatDate.add(sysdate, Calendar.DATE, -14)));
		String hsEndDate = ServletRequestUtils.getStringParameter(request, "hsEndDate", defaultDateFormat.format(FormatDate.add(sysdate, Calendar.DATE, -13)));
		String tambahan = ServletRequestUtils.getStringParameter(request, "tambahan", "");
		String userName = ServletRequestUtils.getStringParameter(request, "UserName", "");
		
		int kondisi = ServletRequestUtils.getIntParameter(request, "kondisi", 4);
		String from = null;
		cmd.put("startDate", startDate);
		cmd.put("endDate", endDate);
		cmd.put("hsStartDate", hsStartDate);
		cmd.put("hsEndDate", hsEndDate);
		cmd.put("userName", userName);
		cmd.put("tambahan", tambahan);
		cmd.put("kondisi", kondisi);
		cmd.put("reportPath", "report/uw.htm?window=loading_screen");
		request.setAttribute("lsTolak", lsTolak);
		if(request.getParameter("show") != null || request.getParameter("show_report") != null || request.getParameter("save") != null || request.getParameter("save_report_history") !=null || request.getParameter("download") != null || request.getParameter("viewer_report_history") !=null) {

			//show report / save
			
			if(request.getParameter("show_report") != null || request.getParameter("save") != null) {
				if(currentUser.getLde_id().equals("27")){
					int counter = 0;
					PressButton = "Show_Report";
					String[] daftarSpaj = ServletRequestUtils.getStringParameters(request, "reg_spaj");
					String[] daftarNoPolis = ServletRequestUtils.getStringParameters(request, "mspo_policy_no_format");
					String[] daftarNamaProduk = ServletRequestUtils.getStringParameters(request, "lsdbs_name");
					String[] daftarTglAksep = ServletRequestUtils.getStringParameters(request, "mste_tgl_aksep");
					String[] daftarTglKirim = ServletRequestUtils.getStringParameters(request, "mste_tgl_kirim_lb");
					StringBuffer daftarUpdate = new StringBuffer("");
					
					List<Map> listSpaj = new ArrayList<Map>();
					List<Map> listTolak = new ArrayList<Map>(); 
					
					for(int i=0; i<daftarSpaj.length; i++) {
						int pilih = ServletRequestUtils.getIntParameter(request, "valid" + i, 0);
						String keterangan = ServletRequestUtils.getStringParameter(request, "keterangan" + i, "");
						
						
						if(pilih == 1) {
							Map m = new HashMap();
							counter += pilih;
							daftarUpdate.append(daftarSpaj[i] + ",");
							//keterangan bisa kosong apabila yg dipilih aksep/terima
							keterangan = ServletRequestUtils.getStringParameter(request, "keterangantambah" + i, "");
							
							m.put("msps_desc", keterangan);
							m.put("reg_spaj", daftarSpaj[i]);
							listSpaj.add(m);
						}else if(pilih == 2){
							Map m = new HashMap();
							
							if(keterangan.equals("Lain-lain") ){
								keterangan = ServletRequestUtils.getStringParameter(request, "keterangantambah" + i, "");
								if(keterangan.equals("")){
									cmd.put("pesan", "Keterangan harus diisi");
									daftar.addAll(uwManager.selectListPengirimanPolis(daftarUpdate.toString().split(",")));
									cmd.put("ukuran", daftar.size());
									cmd.put("daftar", daftar);

									return new ModelAndView("report/list_polis", "cmd", cmd);
								}
							}
							
							m.put("reg_spaj", daftarSpaj[i]);
							m.put("mspo_policy_no_format", daftarNoPolis[i]);
							m.put("lsdbs_name", daftarNamaProduk[i]);
							m.put("mste_tgl_aksep", daftarTglAksep[i]);
							m.put("mste_tgl_kirim_lb", daftarTglKirim[i]);
							m.put("msps_desc", keterangan);
							listSpaj.add(m);
							listTolak.add(m);
						}else if(pilih == 0){
							Map m = new HashMap();
							m.put("reg_spaj", daftarSpaj[i]);
							listSpaj.add(m);
						}
						
					}
					
					if(request.getParameter("save") != null) {
						PressButton= "Save";
							for(int a=0;a<daftarSpaj.length;a++){
								int valid = ServletRequestUtils.getIntParameter(request, "valid"+a, 0);
								if(valid == 1){
									uwManager.updatePengirimanPolisLB(currentUser, listSpaj, "MSTE_TGL_TERIMA_LB", sysdate, a);
								}else if(valid == 2){
									uwManager.tolakPengirimanPolis(currentUser, listSpaj, "MSTE_TGL_KIRIM_LB", null, a);
								}
							}
							List<Map> kirimBalik = listTolak;
							if(kirimBalik.size()!=0){
								DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
								String   spajLoop="\nNO POLIS           NAMA PRODUK                        TANGGAL AKSEPTASI  TANGGAL KIRIM  KETERANGAN\n";
								spajLoop=spajLoop+"======================================================================================================\n";
								int emailBancass = 0;
								for (int i = 0; i < kirimBalik.size(); i++) {
									 Map hasilMap=kirimBalik.get(i);
									 String noPolis = (String) hasilMap.get("mspo_policy_no_format");
									 String jenisProduk = (String) hasilMap.get("lsdbs_name");
									 String tglAksep = (String) hasilMap.get("mste_tgl_aksep");
									 String tglKirim = (String) hasilMap.get("mste_tgl_kirim_lb");
									 String ket = (String) hasilMap.get("msps_desc");
									
									 String spaj = (String) hasilMap.get("reg_spaj");

									spajLoop=spajLoop+ noPolis +" "+" "+FormatString.rpadRataKiri(" ", jenisProduk, 40)+" "+" "+tglAksep+" "+" "+tglKirim+" "+"    "+ket+"\n\n";
									if(elionsManager.selectIsInputanBank(spaj)==2) {
								    	 emailBancass = 1;
									} 
								}
								//if(emailBancass ==1){
									// String emailtambahan = props.getProperty("bancassuance.further_requirement_simasprima");
									//email.send(false, "ajsjava@sinarmasmsiglife.co.id", new String[] {"hadi@sinarmasmsiglife.co.id", emailtambahan}, new String[]{props.getProperty("admin.yusuf"), "Deddy@sinarmasmsiglife.co.id", "ingrid@sinarmasmsiglife.co.id","Timmy@sinarmasmsiglife.co.id","Fikki@sinarmasmsiglife.co.id","Ariani@sinarmasmsiglife.co.id","novie@sinarmasmsiglife.co.id"}, null, "PENGIRIMAN COPY POLIS DARI LIFE BENEFIT", "PESAN INI TERKIRIM HANYA SEBAGAI REMINDER BAHWA ADA COPY POLIS YANG BELUM DIAKSEPTASI LIFE BENEFIT.\n"+spajLoop+" \nnb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", null);
									// email.send(false, "ajsjava@sinarmasmsiglife.co.id", new String[] {"Deddy@sinarmasmsiglife.co.id"}, null, null, "PENGIRIMAN COPY POLIS DARI LIFE BENEFIT", "PESAN INI TERKIRIM HANYA SEBAGAI REMINDER BAHWA ADA COPY POLIS YANG BELUM DIAKSEPTASI LIFE BENEFIT.\n"+spajLoop+" \nnb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", null);
								//}else {
									email.send(false, "ajsjava@sinarmasmsiglife.co.id", new String[] {"hadi@sinarmasmsiglife.co.id"}, new String[]{"Deddy@sinarmasmsiglife.co.id", "ingrid@sinarmasmsiglife.co.id","Timmy@sinarmasmsiglife.co.id","Fikki@sinarmasmsiglife.co.id","novie@sinarmasmsiglife.co.id","asriwulan@sinarmasmsiglife.co.id"}, null, "PENGIRIMAN COPY POLIS DARI LIFE BENEFIT", "PESAN INI TERKIRIM HANYA SEBAGAI REMINDER BAHWA ADA COPY POLIS YANG BELUM DIAKSEPTASI LIFE BENEFIT.\n"+spajLoop+" \nnb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", null);
									//email.send(false, "ajsjava@sinarmasmsiglife.co.id", new String[] {"Deddy@sinarmasmsiglife.co.id"}, null, null, "PENGIRIMAN COPY POLIS DARI LIFE BENEFIT", "PESAN INI TERKIRIM HANYA SEBAGAI REMINDER BAHWA ADA COPY POLIS YANG BELUM DIAKSEPTASI LIFE BENEFIT.\n"+spajLoop+" \nnb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", null);
								//}
								
							}
													
						
						cmd.put("pesan", "Tanggal Pengiriman Berhasil Diupdate ["+completeDateFormat.format(sysdate)+"]. Jumlah Polis adalah " + counter);
					}
					//TODO
					//tarik
					daftar.addAll(uwManager.selectListPengirimanPolis(daftarUpdate.toString().split(",")));
					
					session.setAttribute("listPengirimanPolis", daftar);
					session.setAttribute("startDate", startDate);
					session.setAttribute("endDate", endDate);
					session.setAttribute("sysDate", sysdate);
					cmd.put("reportPath", "report/nb.pdf?window=pengiriman_polis");
					
				}else {
					int counter = 0;
					PressButton = "Show_Report";
					String[] daftarSpaj = ServletRequestUtils.getStringParameters(request, "reg_spaj");
					StringBuffer daftarUpdate = new StringBuffer("");
					StringBuffer daftarDetailSphOrSpt = new StringBuffer("");
					for(int i=0; i<daftarSpaj.length; i++) {
						int pilihSph = ServletRequestUtils.getIntParameter(request, "ucup" + i, 0);
						int pilihSpt = ServletRequestUtils.getIntParameter(request, "checkBoxSpt" + i, 0);
						counter += pilihSph;
						if( pilihSph == 1 && pilihSpt == 0) {
						 	daftarUpdate.append(daftarSpaj[i] + ",");
						 	daftarDetailSphOrSpt.append( "SPH"+ ",");
						}
						else if( pilihSph == 1 && pilihSpt == 1 )
						{
						 	daftarUpdate.append(daftarSpaj[i] + ",");
						 	daftarDetailSphOrSpt.append( "SPHSPT" + ",");
						}
						else if( pilihSph == 0 && pilihSpt == 1)
						{
						 	daftarUpdate.append(daftarSpaj[i] + ",");
						 	daftarDetailSphOrSpt.append( "SPT" + ",");
						}
					}
				
					if(request.getParameter("save") != null) {
						//update datanya	
						PressButton= "Save";
						if (currentUser.getLde_id().equals("11")) {
							//TODO
							uwManager.updatePengirimanPolisUW(currentUser, daftarUpdate.toString().split(","), "MSTE_TGL_KIRIM_LB", sysdate, daftarDetailSphOrSpt.toString().split(","));
						}else {uwManager.updatePengirimanPolisUW(currentUser, daftarUpdate.toString().split(","), "MSTE_TGL_KIRIM_LB", sysdate, daftarDetailSphOrSpt.toString().split(",")); 
						} 
						cmd.put("pesan", "Tanggal Pengiriman Berhasil Diupdate ["+completeDateFormat.format(sysdate)+"]. Jumlah Polis adalah " + counter);
					}
					
					//tarik
					daftar.addAll(uwManager.selectListPengirimanPolis(daftarUpdate.toString().split(",")));
					
					session.setAttribute("listPengirimanPolis", daftar);
					session.setAttribute("startDate", startDate);
					session.setAttribute("endDate", endDate);
					session.setAttribute("sysDate", sysdate);
					cmd.put("reportPath", "report/nb.pdf?window=pengiriman_polis");
				
				}
			}
			
			
			if(request.getParameter("save_report_history") != null || request.getParameter("download") != null || request.getParameter("viewer_report_history") !=null) {
				
				if(currentUser!= null) {
					if(currentUser.getLde_id().equals("11")) {
						from = "UW";
					}else if (currentUser.getLde_id().equals("27") || currentUser.getLde_id().equals("12")) {
							from = "LB";
					}else from = "Lain2";
				}
				
				if(request.getParameter("download") != null){
					String tampung = ServletRequestUtils.getStringParameter(request, "tampung");
					List<DropDown> daftarFile = FileUtils.listFilesInDirectory(tampung);
				}else {
					String tampung = props.getProperty("pdf.dir")+"\\"+"Pengiriman_Polis_LB";
					List<DropDown> daftarFile = FileUtils.listFilesInDirectory(tampung);
				}
				
				String fileName = request.getParameter("filename");
					if(fileName != null) {
						if(from.equals("UW")) {
							String tampung = ServletRequestUtils.getStringParameter(request, "tampung");
							List<DropDown> daftarFile = FileUtils.listFilesInDirectory(tampung);
							FileUtils.downloadFile("inline;", tampung, fileName, response);
							return null;
						}else if(from.equals("LB")){
							String tampung = ServletRequestUtils.getStringParameter(request, "tampung");
							List<DropDown> daftarFile = FileUtils.listFilesInDirectory(tampung);
							FileUtils.downloadFile("inline;", tampung, fileName, response);
							return null;
						}else {
							String tampung = ServletRequestUtils.getStringParameter(request, "tampung");
							List<DropDown> daftarFile = FileUtils.listFilesInDirectory(tampung);
							FileUtils.downloadFile("inline;", tampung, fileName, response);
							return null;
						}
					}else {
						if(request.getParameter("save_report_history") != null){
							if(currentUser.getLde_id().equals("11")) {
								from = "UW";
								String tampung = "\\\\"+props.getProperty("pdf.dir.report2")+"\\\\"+"Pengiriman_Polis_LB"+"\\\\"+from;
								List<DropDown> daftarFile = FileUtils.listFilesInDirectoryHistory(tampung,hsStartDate,hsEndDate);
								cmd.put("tampung", tampung);
								cmd.put("daftarFile", daftarFile);
								return new ModelAndView("report/list_polis", "cmd", cmd);
							}else if(currentUser.getLde_id().equals("27") || currentUser.getLde_id().equals("12")) {
								from = "LB";
								String tampung ="\\\\"+ props.getProperty("pdf.dir.report2")+"\\\\"+"Pengiriman_Polis_LB"+"\\\\"+from;
								List<DropDown> daftarFile = FileUtils.listFilesInDirectoryHistory(tampung,hsStartDate,hsEndDate);
								cmd.put("tampung", tampung);
								cmd.put("daftarFile", daftarFile);
								return new ModelAndView("report/list_polis", "cmd", cmd);
							}else {
								String tampung = "\\\\"+props.getProperty("pdf.dir.report2")+"\\\\"+"Pengiriman_Polis_LB";
								List<DropDown> daftarFile = FileUtils.listFilesInDirectoryHistory(tampung,hsStartDate,hsEndDate);
								cmd.put("tampung", tampung);
								cmd.put("daftarFile", daftarFile);
								return new ModelAndView("report/list_polis", "cmd", cmd);
							}
						}else if (request.getParameter("viewer_report_history") !=null){
							if(currentUser.getLde_id().equals("11")) {
								String tampung = "\\\\"+props.getProperty("pdf.dir.report2")+"\\\\"+"Pengiriman_Polis_LB"+"\\\\"+"uw_show_report";
								List<DropDown> daftarFile = FileUtils.listFilesInDirectoryHistory(tampung,hsStartDate,hsEndDate);
								cmd.put("tampung", tampung);
								cmd.put("daftarFile", daftarFile);
								return new ModelAndView("report/list_polis", "cmd", cmd);
							}else if(currentUser.getLde_id().equals("27") || currentUser.getLde_id().equals("12")) {
								String tampung = "\\\\"+props.getProperty("pdf.dir.report2")+"\\\\"+"Pengiriman_Polis_LB"+"\\\\"+"lb_show_report";
								List<DropDown> daftarFile = FileUtils.listFilesInDirectoryHistory(tampung,hsStartDate,hsEndDate);
								cmd.put("tampung", tampung);
								cmd.put("daftarFile", daftarFile);
								return new ModelAndView("report/list_polis", "cmd", cmd);
							}else {
								String tampung = "\\\\"+props.getProperty("pdf.dir.report2")+"\\"+"Pengiriman_Polis_LB";
								List<DropDown> daftarFile = FileUtils.listFilesInDirectoryHistory(tampung,hsStartDate,hsEndDate);
								cmd.put("tampung", tampung);
								cmd.put("daftarFile", daftarFile);
								return new ModelAndView("report/list_polis", "cmd", cmd);
							}
						}
					}
			}
			
			if(daftar.isEmpty()) {
				daftar = uwManager.selectListPengirimanPolis(startDate, endDate, kondisi);
				if(!tambahan.equals("")) daftar.addAll(uwManager.selectListPengirimanPolis(tambahan.split(",")));
			}
		}
		
		
		cmd.put("ukuran", daftar.size());
		cmd.put("daftar", daftar);

		return new ModelAndView("report/list_polis", "cmd", cmd);
	}
	
	
}

//	public ModelAndView nb_bank(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Report report;
//		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		Date sysDate = elionsManager.selectSysdate();
//
//		List<DropDown> reportPathList = new ArrayList<DropDown>();
//		if("SSS".equals(currentUser.getCab_bank())) {
//			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_referral_all"), "New Business Per Referral"));
//			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_all"), "New Business Per Tgl Cetak Sertifikat"));
//			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_input_all"), "New Business Per Tgl Input Spaj"));
//			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_produksi_all"), "New Business Per Tgl Produksi"));
//			reportPathList.add(new DropDown(props.getProperty("report.bac.view_jatuh_tempo_all"), "Laporan Jatuh Tempo"));
//			reportPathList.add(new DropDown(props.getProperty("report.bac.view_rollover_all"), "Laporan Rollover Per Tgl Produksi"));
//		}else {
//			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas"), "New Business Per Tgl Cetak Sertifikat"));
//			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_input"), "New Business Per Tgl Input Spaj"));
//			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_produksi"), "New Business Per Tgl Produksi"));
//			reportPathList.add(new DropDown(props.getProperty("report.bac.view_jatuh_tempo"), "Laporan Jatuh Tempo"));
//			reportPathList.add(new DropDown(props.getProperty("report.bac.view_rollover"), "Laporan Rollover Per Tgl Produksi"));
//		}
//		
//		report = new Report("Simas Prima Reports", reportPathList, Report.PDF, null);
//		
//		report.addParamDefault("username", "username", 200, currentUser.getName(), false);
//		report.addParamDefault("cab_bank", "cab_bank", 200, currentUser.getCab_bank(), false);
//		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
//		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
//		return prepareReport(request, response, report);
//	}
	
//}