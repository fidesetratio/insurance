package com.ekalife.utils.parent;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;

import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.jasper.JasperReportsAppletView;
import com.ekalife.utils.jasper.JasperReportsCustomView;
import com.ekalife.utils.jasper.JasperReportsExcelApiView;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.jasper.Report;
import com.lowagie.text.pdf.PdfWriter;

import id.co.sinarmaslife.std.model.vo.DropDown;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

/**
 * <p>
 * Merupakan controller yang merupakan parent dari semua controller yang
 * akan menyediakan fungsi generate report dengan jasperreports.
 * Sebenarnya ini adalah implementasi multiactioncontroller.
 * </p>
 * @author Yusuf
 * @since 16/08/2006
 * @see abstract parent beans pada applicationContext.xml
 */
public abstract class ParentJasperReportingController extends ParentMultiController {
	
	protected Connection connection = null;
	
	protected Connection getConnection() {
		if(this.connection==null) {
			try {
				this.connection = this.elionsManager.getUwDao().getDataSource().getConnection();
			} catch (SQLException e) {
				logger.error("ERROR :", e);
			}
		}
		return this.connection;
	}
	
	protected void closeConnection(Connection conn){
		try {
            conn.close();
        } catch (SQLException e) { /* ignored */}
	}

    public DataSource getDataSource()
    {
        return this.elionsManager.getUwDao().getDataSource();
    }

	protected ModelAndView prepareReport(HttpServletRequest request, HttpServletResponse response, Report report) throws Exception {
		HttpSession session = request.getSession();
		String uri = request.getRequestURI();
		String format = uri.substring(uri.lastIndexOf(".")+1);

		String[] nm_report = null;
		if(report.getReportPath() != null) {
			nm_report= new String[] {report.getReportPath().substring(report.getReportPath().lastIndexOf("/")+1, report.getReportPath().length())};
		}else {
			nm_report = new String[report.getReportPathList().size()];
			for(int i=0; i<report.getReportPathList().size(); i++) {
				DropDown tmp = (DropDown) report.getReportPathList().get(i);
				nm_report= new String[] {tmp.getKey().substring(tmp.getKey().lastIndexOf("/")+1, tmp.getKey().length())};
			}
		}
		for(String test : nm_report) {
			int cek=props.getProperty("report.excel.disabled").indexOf(test);
			if(cek>=0)
				report.setFlagExcel(true);
		}
		
		report.changeUri(request);
		
		//apabila ada path untuk backup pdf nya, backup pdf nya
		if(report.getBackupPdfPath() != null) {
			Map params=new HashMap();
			String outputDir = report.getBackupPdfPath();
			params=report.bindParameters(request);
//			getConnection();
			Connection conn = null;
			try {

				conn = this.getDataSource().getConnection();
				JasperUtils.exportReportToPdf(report.getReportPath()+".jasper", outputDir, null, params, conn, PdfWriter.AllowPrinting, null, null);	
			}catch(Exception e){
				this.closeConnection(conn);
		        throw e;
			}finally{
				this.closeConnection(conn);
			}
			
		}
		
		if(report.getFlagEmail()){
			Map params=new HashMap();
			String outputDir=props.getProperty("report.email.dir");
			String nama = request.getParameter("window");
			SimpleDateFormat formatDate =new SimpleDateFormat("dd/MM/yyyy");
			Date MSAG_BEG_DATE = formatDate.parse(request.getParameter("tanggalAwal"));
			Date MSAG_END_DATE = formatDate.parse(request.getParameter("tanggalAkhir"));
			String outputXls = nama + " " + FormatDate.toIndonesian(MSAG_BEG_DATE) + "-" + FormatDate.toIndonesian(MSAG_END_DATE) + ".xls";
			String outputPdf = nama + "_" + FormatDate.toIndonesian(MSAG_BEG_DATE) + "-" + FormatDate.toIndonesian(MSAG_END_DATE) + ".pdf";
			
			if(report.getFlag())
				report.setTo(props.getProperty("report.email.to"));
			else
				report.setTo(props.getProperty("report.email.to1"));
			
			params=report.bindParameters(request);
			//getConnection();
			Connection conn = null;
			try {

				conn = this.getDataSource().getConnection();
				JasperUtils.exportReportToPdf(report.getReportPath()+".jasper", outputDir, outputPdf, params, conn, PdfWriter.AllowPrinting, null, null);
				JasperUtils.exportReportToXls(report.getReportPath()+".jasper", outputDir, outputXls, params, conn);
			}catch(Exception e){
				this.closeConnection(conn);
		        throw e;
			}finally{
				this.closeConnection(conn);
			}
			
			List<File> attachments = new ArrayList<File>();
			if(ServletRequestUtils.getStringParameter(request, "isXls").equals("true")) {
				File sourceFile = new File(outputDir + outputXls);
				attachments.add(sourceFile);
			}
			if(ServletRequestUtils.getStringParameter(request, "isPDF").equals("true")) {
				File sourceFile = new File(outputDir + outputPdf);
				attachments.add(sourceFile);
			}
			
			email.send(false, "it@sinarmasmsiglife.co.id", report.getReportEmailTo(), null, null, report.getReportSubject(), 
					report.getReportMessage()+"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions."
					, attachments);
			
			ServletOutputStream os = response.getOutputStream();
			os.print("<script>alert('E-mail berhasil dikirim.');</script>");
			os.close();		
		}else if(report.getFlagEmailCab()){
			request.setAttribute("daftarEmailCabang", elionsManager.selectDaftarEmailCabang());
			
			Map params=new HashMap();
			String outputDir=props.getProperty("report.email.dir");
			String outputFile=props.getProperty("report.email.nama");
			String lca_id = (String) report.getParameterValue("lca_id");
			if(report.getFlagEC())
				report.setTo(elionsManager.selectEmailAddr(lca_id));//ngambil email cabang
			else
				report.setTo(elionsManager.selectEmailAddr(lca_id));
			
			params=report.bindParameters(request);
			//getConnection();
			Connection conn = null;
			try {

				conn = this.getDataSource().getConnection();
				JasperUtils.exportReportToPdf(report.getReportPath()+".jasper", outputDir, outputFile, params, conn, PdfWriter.AllowPrinting, null, null);
			}catch(Exception e){
				this.closeConnection(conn);
		        throw e;
			}finally{
				this.closeConnection(conn);
			}
		}else if(report.getReportCanBeEmailed() && ServletRequestUtils.getStringParameter(request, "email_ucup", "").equals("email")) {
			getConnection();
			String outputDir=props.getProperty("report.email.dir");
			String outputFile = ServletRequestUtils.getStringParameter(request, "spaj", "") + "_sph.pdf";
			Map params = report.bindParameters(request);
			Connection conn = null;
			try {

				conn = this.getDataSource().getConnection();
				JasperUtils.exportReportToPdf(report.getReportPath()+".jasper", outputDir, outputFile, params, conn, PdfWriter.AllowPrinting, null, null);
			}catch(Exception e){
				this.closeConnection(conn);
		        throw e;
			}finally{
				this.closeConnection(conn);
			}
				
			List<File> attachments = new ArrayList<File>();
			File sourceFile = new File(props.getProperty("report.email.dir")+"\\"+outputFile);
			attachments.add(sourceFile);
			email.send(false, props.getProperty("admin.ajsjava"), report.getReportEmailTo(), null, null, report.getReportSubject(), 
					report.getReportMessage()+"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions."
					, attachments);
			ServletOutputStream os = response.getOutputStream();
			os.print("<script>alert('E-mail berhasil dikirim.');</script>");
			os.close();
			return null;
		} 
		
		if(format.equals("htm")) {
			session.setAttribute("report", report);
			return new ModelAndView("report/template", "report", report);
		}else if(format.equals("applet") && request.getParameter("biarGakLooping") == null ) {
			StringBuffer url = new StringBuffer(request.getRequestURL() + "?" + request.getQueryString() + "&biarGakLooping=true");
			Enumeration p = request.getParameterNames(); 
			while(p.hasMoreElements()) {
				String name = (String) p.nextElement();
				url.append("&" + name + "=" + request.getParameter(name)); 
			}
			String escapedUrl = StringUtils.replace(StringUtils.replace(StringUtils.replace(StringUtils.replace(url.toString(), "/", "%2F"), "?", "%3F"), "=", "%3D"), "&", "%26");
			response.sendRedirect(request.getContextPath()+"/include/applet.jsp?zoom_index=1&report_url="+escapedUrl);
			return null;
		}else {
			
			List lsReport=report.getResultList();
			if(session.getAttribute("report")!=null) {
				String reportPath = report.getReportPath();
				report = (Report) session.getAttribute("report");
				if(reportPath != null) report.setReportPath(reportPath);
			}
			Object dataSource;
			if(report.getReportQueryMethod()!=null) {
				Class[] parameterTypes = new Class[] {Map.class};
				Method queryMethod;
				Object[] arguments = new Object[] {report.bindParameters(request)};

				try {
					Class elionsManagerClass = ElionsManager.class;
					queryMethod = elionsManagerClass.getMethod(report.getReportQueryMethod(), parameterTypes);
					dataSource = (List) queryMethod.invoke(elionsManager, arguments);
				} catch (NoSuchMethodException e) {
					try{
						Class uwManagerClass = UwManager.class;
						queryMethod = uwManagerClass.getMethod(report.getReportQueryMethod(), parameterTypes);
						dataSource = (List) queryMethod.invoke(uwManager, arguments);
					}catch (NoSuchMethodException err){
						Class uwManagerClass = BacManager.class;
						queryMethod = uwManagerClass.getMethod(report.getReportQueryMethod(), parameterTypes);
						dataSource = (List) queryMethod.invoke(bacManager, arguments);
					}
					
				}
				
			}else if (report.getResultList()!= null) {
				dataSource = lsReport;
			}else dataSource = elionsManager.getUwDao().getDataSource();
			return generateReport(dataSource, request, response, report.bindParameters(request), ServletRequestUtils.getStringParameter(request, "attachment", "1"), null, null);
		}
	}

	protected ModelAndView generateReport(Object dataSource, HttpServletRequest request, HttpServletResponse response, 
			Map<String, Object> params, String isAttached, String spaj, String namaFile) throws MalformedURLException{

		
		AbstractJasperReportsView jasperViewer = null;
		String reportPath = params.get("reportPath").toString();
		String uri = request.getRequestURI();
		boolean isViewer = ServletRequestUtils.getBooleanParameter(request, "isViewer", false); 

		/** Kasih object connection untuk jaga2, siapa tau subreport perlu */
		params.put("koneksi", getConnection());
		
		if(params.get("logoQr") != null){
			//adr
			this.props.put("logoQr", params.get("logoQr"));
		}
		if(params.get("Print") != null){
			this.props.put("Print", params.get("Print"));
		}		
		
		/** Format Reportnya **/
		String format = "html";
		try { format = uri.substring(uri.lastIndexOf(".")+1); } catch(Exception e) {}
		if(format.equals("htm"))
			try {
				format = ServletRequestUtils.getRequiredStringParameter(request, "show");
			} catch (ServletRequestBindingException e) {
				logger.error("ERROR :", e);
			}
		params.put("format", format);

		/** Exporter Parameter **/
		Map<JRExporterParameter, Object> exp = new HashMap<JRExporterParameter, Object>(); 
		if(ServletRequestUtils.getStringParameter(request, "print", "").equals("true")) {
			exp.put(JRPdfExporterParameter.IS_ENCRYPTED, Boolean.TRUE);
			exp.put(JRPdfExporterParameter.IS_128_BIT_KEY, Boolean.TRUE);
			exp.put(JRPdfExporterParameter.PERMISSIONS, new Integer(PdfWriter.AllowPrinting));			
		} else if(format.equals("html")) {
			//request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
			exp.put(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
			exp.put(JRHtmlExporterParameter.IMAGES_URI, request.getContextPath() + "/image?image=");
			//exp.put(JRHtmlExporterParameter.IMAGES_URI, request.getContextPath()+"/include/image/");
			//exp.put(JRExporterParameter.PAGE_INDEX, new Integer(0));
			exp.put(JRHtmlExporterParameter.HTML_HEADER, "<html><head><script>function rubah(angka){	rekur(document.childNodes, angka);	var tabel = document.getElementsByTagName('table');	if(tabel.length > 0){		if(tabel.style){			if(tabel.style.width){				tabel.style.width = parseInt(tabel.style.width.replace('px', '')) + (angka*30) + 'px';			}		}	}}function rekur(childNude, angka){	if(childNude.length > 0){		for (var iNode = 0; iNode < childNude.length; iNode++){			var oSubject = childNude.item(iNode);			if(oSubject.style) {				if(oSubject.style.fontSize) {					var ukuran = parseInt(oSubject.style.fontSize.replace('px', ''));					oSubject.style.fontSize = (ukuran + angka) + 'px';				}			}			rekur(oSubject.childNodes, angka);		}	}}</script><head><body style='text-align: center;'><input type='button' value='Perkecil (-)' onclick='rubah(-2);'><input type='button' value='Perbesar (+)' onclick='rubah(2);'>");			exp.put(JRHtmlExporterParameter.HTML_FOOTER, "</body></html>");
			
//script dibawah ini jangan dihapus! ini dipakai untuk html header			
/*
<html><head><script>
function rubah(angka){	
	rekur(document.childNodes, angka);
	var tabel = document.getElementsByTagName('table');
	if(tabel.length > 0){
		if(tabel.style){
			if(tabel.style.width){
				tabel.style.width = parseInt(tabel.style.width.replace('px', '')) + (angka*30) + 'px';
			}
		}
	}
}
function rekur(childNude, angka){
	if(childNude.length > 0){
		for (var iNode = 0; iNode < childNude.length; iNode++){
			var oSubject = childNude.item(iNode);
			if(oSubject.style) {
				if(oSubject.style.fontSize) {
					var ukuran = parseInt(oSubject.style.fontSize.replace('px', ''));
					oSubject.style.fontSize = (ukuran + angka) + 'px';
				}
			}
			rekur(oSubject.childNodes, angka);
		}
	}
}
</script><head><body style='text-align: center;'>
<input type='button' value='Perkecil (-)' onclick='rubah(-2);'><input type='button' value='Perbesar (+)' onclick='rubah(2);'>
 */			
			
			Map imagesMap = new HashMap();
			request.getSession().setAttribute("IMAGES_MAP", imagesMap);
			exp.put(JRHtmlExporterParameter.IMAGES_MAP, imagesMap);
		}else if(format.equals("pdf")) {
			exp.put(JRPdfExporterParameter.IS_ENCRYPTED, Boolean.TRUE);
			exp.put(JRPdfExporterParameter.IS_128_BIT_KEY, Boolean.TRUE);
			exp.put(JRPdfExporterParameter.PERMISSIONS, new Integer(PdfWriter.AllowPrinting));
			//exp.put(JRPdfExporterParameter.PERMISSIONS, new Integer(PdfWriter.AllowCopy));
			//exp.put(JRPdfExporterParameter.USER_PASSWORD, elionsManager.validationVerify(1).get("PASSWORD").toString().toLowerCase());
			exp.put(JRPdfExporterParameter.OWNER_PASSWORD, props.getProperty("pdf.ownerPassword"));
			//exp.put("net.sf.jasperreports.engine.export.JRPdfExporterParameter.PERMISSIONS", new Integer(PdfWriter.AllowPrinting));			
			//AllowPrinting, AllowModifyContents, AllowCopy, AllowModifyAnnotations, AllowFillIn, AllowScreenReaders, AllowAssembly and AllowDegradedPrinting 
		}
		
		String va = uwManager.selectVirtualAccountSpaj(spaj);
		
		/** Setting Viewer-nya **/
		if(format.equals("jasper")) {
			jasperViewer = new JasperReportsAppletView(); 
		}else if(request.getParameter("print")!=null) {
			jasperViewer = new JasperReportsCustomView(response, props, exp, spaj, elionsManager.selectCabangFromSpaj(spaj), 
					namaFile, true, va, isViewer);
		}else if(format.equals("pdf")) {
			jasperViewer = new JasperReportsCustomView(response, props, exp, spaj, elionsManager.selectCabangFromSpaj(spaj), 
					namaFile, false, va, isViewer);
		}else if(format.equals("xls")) {
			jasperViewer = new JasperReportsExcelApiView();
			if(isAttached.equals("0")) {
				Properties props = new Properties();
				props.put("Content-Disposition", "attachment");
				jasperViewer.setHeaders(props);		
			}
		}else {
			jasperViewer = new JasperReportsMultiFormatView();			
			if(isAttached.equals("0")) {
				Properties props = new Properties();
				props.put("Content-Disposition", "attachment");
				jasperViewer.setHeaders(props);		
			}
		}
		jasperViewer.setExporterParameters(exp);
		/** Kalau dalam .jrxml, set compilernya **/
		//jasperViewer.setReportCompiler(new JRJdtCompiler());
		//jasperViewer.setUrl(reportPath);
		
		/** Kalau dalam .jasper, langsung saja **/
		jasperViewer.setUrl(reportPath+".jasper");
		
		/** Jenis DataSource bisa 2 macam, javax.sql.DataSource atau java.util.List biasa **/
		if(dataSource instanceof DataSource) {
			jasperViewer.setJdbcDataSource( (DataSource) dataSource );
		}else if(dataSource instanceof List){
			if(params.get("subReportDatakeys")!=null) {
				jasperViewer.setReportDataKey("dataSource");
				jasperViewer.setSubReportDataKeys((String[]) params.get("subReportDatakeys"));
			}
			params.put("dataSource", dataSource);
		}
		
		if(logger.isDebugEnabled()) logger.debug("Showing Report " + reportPath);
		
		jasperViewer.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()));
		if(namaFile != null){
			if(namaFile.length()>6){
				if(namaFile.substring(0, 6).equals("TOP_UP")){
					String lsbs = FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 3);
					if(lsbs.equals("188")){
						uwManager.updatePrintPSaveTopup(spaj, new Integer((String) params.get("tu_ke")));
					}else{
						uwManager.updatePrintStableLinkTopup(spaj, new Integer((String) params.get("tu_ke")));
					}
				}else if(namaFile.substring(0, 6).equals("POWERS")){ 
					uwManager.updatePrintPSaveTopup(spaj, new Integer((String) params.get("tu_ke")));
				}
			}
		}
		
		return new ModelAndView(jasperViewer, params);
		
	}
	
	public ModelAndView loading_screen(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("report/template_loading");
	}
	
	
}