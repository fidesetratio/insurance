package com.ekalife.elions.web.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.btpp.Btpp;
import com.ekalife.elions.model.tts.Tts;
import com.ekalife.utils.Common;
import com.ekalife.utils.FHit;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.MergePDF;
import com.ekalife.utils.Print;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.jasper.Report;
import com.ekalife.utils.parent.ParentJasperReportingController;
import com.google.gson.Gson;
import com.ibatis.common.resources.Resources;
import com.lowagie.text.pdf.PdfWriter;

import id.co.sinarmaslife.std.model.vo.DropDown;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
/**
 * @author Yusuf
 * @since June 20, 2006
 */
public class UnderWritingController extends ParentJasperReportingController{
	protected final Log logger = LogFactory.getLog( getClass() );
	
	public ModelAndView new_business_production(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("New Business Production For National Per Plan", props.getProperty("report.uw.new_business_production"), Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView new_business_production_mallassurance(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("New Business Production Mall Assurance", props.getProperty("report.uw.new_business_production_mallassurance"), Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView new_business_production_dmtm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("New Business Production DMTM", props.getProperty("report.uw.new_business_production_dmtm"), Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView new_business_production_mallassurance_fa(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("Laporan Production Mall Assurance", props.getProperty("report.uw.new_business_production_mallassurance_fa"), Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView acceptedByUw(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		report = new Report("Report Accepted By UW", props.getProperty("report.uw.acceptedByUw"), Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		report.addParamDefault("lusId", "lusId", 0,currentUser.getLus_id(), false);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView list_polis_powersave(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("List Polis Powersave By Name", props.getProperty("report.uw.list_polis_powersave"), Report.PDF, null);
		report.addParamText("Nama Pemegang Polis", "nama", 100, null, true);
		report.addParamText("Identitas (Tanpa Titik)", "identitas", 100, null, true);
		report.addParamDate("Tanggal Lahir", "tgl_lahir", false, new Date[] {new Date()}, true);
		return prepareReport(request, response, report);
	}

	public ModelAndView printpolis(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("Print Polis", props.getProperty("report.print_polis"), Report.PDF, null);
		report.addParamSelect("cabang", "cabang", elionsManager.selectlstCabang2(), null, true);
		report.addParamSelect("produk", "produk", elionsManager.selectProductCombined(), null, true);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		report.setReportQueryMethod("selectReportPrintPolis");
		return prepareReport(request, response, report);
	}
	
	public ModelAndView pendingprintUW(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		report = new Report("Report Pending Print Polis", props.getProperty("report.uw.pendingprintUW"), Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		report.addParamDefault("lusId", "lusId", 0, currentUser.getLus_id(), false);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView printpolisulang (HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		report = new Report("Print Polis Ulang", props.getProperty("report.print_polis_ulang"), Report.PDF, null);
		
		List<Map> jenis = new ArrayList<Map>();
		Map tmp = new HashMap();
		tmp.put("KEY", "PRINT ULANG POLIS");
		tmp.put("VALUE", "PRINT ULANG POLIS");
		jenis.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "PRINT DUPLIKAT POLIS");
		tmp.put("VALUE", "PRINT DUPLIKAT POLIS");
		jenis.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "ENDORS NON MATERIAL");
		tmp.put("VALUE", "ENDORS NON MATERIAL");
		jenis.add(tmp);
		
		report.addParamSelect("jenis", "jenis", jenis, "ALL", true);
		report.addParamDate("tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		report.setReportQueryMethod("selectJenisPrintPolisUlang");
		return prepareReport(request, response, report);
	}

	public ModelAndView hcp_salah(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("Surat HCP Salah", props.getProperty("report.hcp_salah"), Report.PDF, null);
		return prepareReport(request, response, report);
	}

	public ModelAndView goldlink(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		
		Report report;
		report = new Report("Summary Gold Link dan Pro Link", props.getProperty("report.summary_goldlink"), Report.PDF, null);
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		report.addParamDefault("lus_login_name", "lus_login_name", 15, currentUser.getName(), false);
		report.addParamDate("Periode Akseptasi", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		
		return prepareReport(request, response, report);
	}
	
	public ModelAndView proses30hari(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("Summary 30 hari", props.getProperty("report.summary_30hari"), Report.PDF, null);
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		report.addParamDefault("lus_login_name", "lus_login_name", 15, currentUser.getName(), false);

		return prepareReport(request, response, report);
	}
	
	public ModelAndView pengantar_ttp(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();

		Report report;
		report = new Report("Pengantar TTP", props.getProperty("report.tandaterimapolis.pengantar"), Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", false, new Date[] {sysdate}, true);
		
		return prepareReport(request, response, report);
	}
	
	public ModelAndView cover_letter(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		List<DropDown> reportPathList = new ArrayList<DropDown>();
		//reportPathList.add(new DropDown(props.getProperty("report.cover_letter_all"), "Covering Letter"));
		//reportPathList.add(new DropDown(props.getProperty("report.cover_letter"), "Covering Letter per Admin"));
		reportPathList.add(new DropDown(props.getProperty("report.cover_letter_all_bancassurance"), "Covering Letter Bancassurance"));
		//reportPathList.add(new DropDown(props.getProperty("report.cover_letter_all_worksite"), "Covering Letter Worksite"));
		
		Report report;
		report = new Report("Covering Letter Bancassurance", reportPathList, Report.PDF, null);
		report.addParamSelect("Branch Admin", "lar_id", uwManager.selectAdminRegion(), null, true);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		
		//kalo reportQueryMethod tidak di-set, maka report akan mengambil data langsung dari database
		//report.setReportQueryMethod("");

		return prepareReport(request, response, report);
	}
	
	public ModelAndView list_pending(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		List<DropDown> reportPathList = new ArrayList<DropDown>();
		reportPathList.add(new DropDown(props.getProperty("report.listpendingall"), "List Pending TTP"));
		reportPathList.add(new DropDown(props.getProperty("report.listpending"), "List Pending TTP per Cabang"));
		
		
		Report report;
		report = new Report("List Pending TTP", reportPathList, Report.PDF, null);
		report.addParamSelect("Cabang", "lca_id", elionsManager.selectlstCabang2(), null, true);
		report.addParamDate("Tanggal", "tgl", true, new Date[] {sysdate, sysdate}, true);
		
		//kalo reportQueryMethod tidak di-set, maka report akan mengambil data langsung dari database
		//report.setReportQueryMethod("");

		return prepareReport(request, response, report);
	}
	public ModelAndView cover_letter_worksite(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		
		Report report;
		report = new Report("Covering Letter Worksite", props.getProperty("report.cover_letter_all_worksite"), Report.PDF, null);
		report.addParamSelect("Perusahaan", "mspo_customer", elionsManager.select_nama_perusahaan2(), null, true);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		//kalo reportQueryMethod tidak di-set, maka report akan mengambil data langsung dari database
		//report.setReportQueryMethod("");

		return prepareReport(request, response, report);
	}
	
	public ModelAndView uwbyunderwriter(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map m = new HashMap();
		return new ModelAndView("report/uwbyunderwriter", m);
	}
	
	public ModelAndView summarystatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		//elionsManager.selectMstPositionSpajAccepted(regSpaj, lspdId, lssaId, lsspId)
		return new ModelAndView("report/summarystatus",map);
	}
	
	public ModelAndView summaryprintpolis(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		map.put("cabang",elionsManager.selectlstCabang());
		map.put("sysdate",defaultDateFormat.format(new Date()));
		return new ModelAndView("report/summaryprintpolis",map);
	}
	//listOut
	public ModelAndView list_outstanding_premi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param= new HashMap();
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		
		param.put("tglAwal",tglAwal);
		param.put("tglAkhir", tglAkhir);
		param.put("lca_id",lca_id);	
		Report report;
		if (lca_id=="0"||lca_id.equals("0")){
			report = new Report("List Out Standing Premi",props.getProperty("report.uw.list_outstanding_premi"),Report.PDF, null);
			report.addParamDefault("tglAwal", "tanggalAwal", 0,tglAwal,  false);
			report.addParamDefault("tglAkhir", "tanggalAkhir", 0,tglAkhir,  false);
			report.addParamDefault("lca_id", "lca_id", 0,lca_id, false);
			request.getSession().setAttribute("report", report);
			return prepareReport(request, response, report);
		}else{
			report = new Report("List Out Standing Premi Per Cabang",props.getProperty("report.uw.list_outstanding_premiCb"),Report.PDF, null);
			report.addParamDefault("tglAwal", "tanggalAwal", 0,tglAwal,  false);
			report.addParamDefault("tglAkhir", "tanggalAkhir", 0,tglAkhir,  false);
			report.addParamDefault("lca_id", "lca_id", 0,lca_id, false);
			request.getSession().setAttribute("report", report);
			return prepareReport(request, response, report);}
	}
	
	public ModelAndView print_polis_per_useruw(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param= new HashMap();
		
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		String lus_id=ServletRequestUtils.getStringParameter(request, "id");
//		if(lus_id==null) lus_id="0";

		param.put("tglAwal",tglAwal);
		param.put("tglAkhir", tglAkhir);
		param.put("lus_id",lus_id);	
		
		Report report;
		report = new Report("Print Polis Per User",props.getProperty("report.uw.print_polis_per_useruw"),Report.PDF, null);
		report.addParamDefault("tglAwal", "tanggalAwal", 0,tglAwal,  false);
		report.addParamDefault("tglAkhir", "tanggalAkhir", 0,tglAkhir,  false);
		report.addParamDefault("lus_id", "lus_id", 0,lus_id, false);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
//		}
		
	}
	
	public ModelAndView print_polis_per_useruw_all(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param= new HashMap();
		
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
	
		param.put("tglAwal",tglAwal);
		param.put("tglAkhir", tglAkhir);
		
		Report report;
		report = new Report("Print Polis All User",props.getProperty("report.uw.print_polis_per_useruw_all"),Report.PDF, null);
		report.addParamDefault("tglAwal", "tanggalAwal", 0,tglAwal,  false);
		report.addParamDefault("tglAkhir", "tanggalAkhir", 0,tglAkhir,  false);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);

		
	}
	
	public ModelAndView summary_status(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/*Report report;
		report = new Report("Summay Status", props.getProperty("report.summary_status"));
		report.addParamDate("tgl", "tgl", true, false);
		report.setReportQueryMethod("selectSummaryStatusAccepted");*/
		Report report;
		report = new Report("Summary Status", props.getProperty("report.summary_status"), Report.HTML, null);
		report.addParamDate("tgl", "tgl", false, new Date[] {new Date()}, true);
		report.addParamSelect("cabang", "cabang", elionsManager.selectlstCabang2(), null, true);
		report.setReportQueryMethod("selectSummaryStatusAccepted");
		return prepareReport(request, response, report);
	}

	public ModelAndView list_peserta(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("list_peserta", props.getProperty("report.worksite.list_peserta"), Report.HTML, null);
		report.addParamText("tgl1", "tgl1", 8, null, true);
		report.addParamText("tgl2", "tgl2",8, null, true);
		report.addParamText("customer", "customer", 12, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView invoice(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("invoice", props.getProperty("report.worksite.invoice"), Report.HTML, null);
		report.addParamText("customer", "customer", 12, null, true);
		report.addParamText("no_invoice", "no_invoice", 7, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView kwitansi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("kwitansi", props.getProperty("report.worksite.kwitansi"), Report.HTML, null);
		report.addParamText("kode", "kode", 12, null, true);
		report.addParamText("no_bill", "no_bill", 11, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView tidak_ada_data(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("tidak_ada_data", props.getProperty("report.worksite.tidak_ada_data"), Report.HTML, null);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}

	public ModelAndView list_peserta_lanjutan(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("list_peserta_lanjutan", props.getProperty("report.worksite.list_peserta_lanjutan"), Report.HTML, null);
		report.addParamText("tgl1", "tgl1", 8, null, true);
		report.addParamText("tgl2", "tgl2",8, null, true);
		report.addParamText("customer", "customer", 12, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView invoice_lanjutan(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("invoice_lanjutan", props.getProperty("report.worksite.invoice_lanjutan"), Report.HTML, null);
		report.addParamText("customer", "customer", 12, null, true);
		report.addParamText("no_invoice", "no_invoice", 7, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView kwitansi_spaj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("kwitansi_spaj", props.getProperty("report.endowmen.kwitansi_spaj"), Report.HTML, null);
		report.addParamText("kode", "kode", 12, null, true);
		report.addParamText("no_bill", "no_bill", 11, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView invoice_spaj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("invoice_spaj", props.getProperty("report.endowmen.invoice_spaj"), Report.HTML, null);
		report.addParamText("kode", "kode", 12, null, true);
		report.addParamText("no_bill", "no_bill", 11, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}

	public ModelAndView undertable(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy");
		Report report;
		report = new Report("Kompensasi Bagian Payroll/Finance", props.getProperty("report.worksite.undertable"), Report.HTML, null);
		report.addParamSelect("Perusahaan", "mcl_id", elionsManager.select_nama_perusahaan2(), null, true);
//		int tahun = Integer.parseInt(df.format(elionsManager.selectSysdate()));
		int tahun = Integer.parseInt(df.format(new Date()));
		report.addParamMonth("Bulan", "tgl", true, tahun-20, tahun, true);
		//kalo reportQueryMethod tidak di-set, maka report akan mengambil data langsung dari database
		//report.setReportQueryMethod("");

		return prepareReport(request, response, report);
	}

	public ModelAndView titipanpremi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");		
		String lusid = ServletRequestUtils.getStringParameter(request, "lusID", "");
		String ttd_jenis = ServletRequestUtils.getStringParameter(request, "ttdjenis", "");
		String keter;
		Boolean suksesMerge = false;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		
		if(ttd_jenis.equals("1")){
			elionsManager.insertMstPositionSpaj(lusid, "Tanda Tangan Sesuai Dengan Bukti Identitas", spaj, 0);
			keter="Tanda tangan sudah dicek dan sesuai dengan bukti identitas.";
		}else{
			elionsManager.insertMstPositionSpaj(lusid, "Tanda Tangan Tidak Sesuai Dengan Bukti Identitas", spaj, 0);
			keter="Tanda tangan sudah dicek dan tidak sesuai dengan bukti identitas.";
		}
		
		report = new Report("titipanpremi", props.getProperty("report.titipanpremi.worksheet"), Report.HTML, null);
		
		 String cabang = elionsManager.selectCabangFromSpaj(spaj);
			
			File dir = new File(
					props.getProperty("pdf.dir.export") + "\\" + cabang + "\\" + spaj);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			
		    File fileDir = new File(report.getReportPath());
		    String dest = dir +"\\";
		    File outputFile = new File(dest);
		    
			Map<String, Comparable> params = new HashMap<String, Comparable>();
			params.put("spaj", spaj);
			params.put("user", currentUser.getName());
			params.put("keter", keter);
			
			Connection conn = null;
			try {
				//conn = this.getDataSource().getConnection();
				conn = this.getUwManager().getUwDao().getDataSource().getConnection();
				JasperUtils.exportReportToPdf(
						props.getProperty("report.titipanpremi.worksheet")+".jasper", 
						dest, 
						""+spaj+"TP.pdf", 
						params, 
						conn, 
						PdfWriter.AllowPrinting, null, null);
			}catch(Exception e){
	              throw e;
			}finally{
				this.closeConnection(conn);
			}
					//dimerge
			List<String> pdfs = new ArrayList<String>();
			String uwTTP = dir + "\\"+spaj+"TP.pdf";
			String worksite=props.getProperty("pdf.dir.export") +"\\UwWorksheet.pdf";
			 OutputStream output = new FileOutputStream(dir+ "\\"+spaj+"MERGE.pdf");
		        pdfs.add(uwTTP);
		        pdfs.add(worksite);
		        suksesMerge = MergePDF.concatPDFs(pdfs, output, false);	
		      
		        // delete PDF TP nya
		        Print.deleteAllFile(new File(uwTTP));
		        //ambil hasil mergenya , terus tampilin
		        FileUtils.downloadFile("inline", dest, spaj+"MERGE.pdf", response);
		        
		report.addParamDefault("spaj", "spaj", 15, spaj, false);
		report.addParamDefault("user", "user", 100, currentUser.getName(), false);
		report.addParamDefault("keter", "keter", 0, keter, false);
		request.getSession().setAttribute("report", report);
		//return prepareReport(request, response, report);
		return null;
	}
	
	public ModelAndView cetak_tts(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		String mstNo=request.getParameter("mst_no");
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		List lsTts=elionsManager.selectAllMstTts(mstNo,"1",null,currentUser.getLca_id());
		Tts tts=(Tts)lsTts.get(0);
		String flag="0";
		String desc=request.getParameter("desc");
		String kd_cab=tts.getLst_kd_cab();
		String ok=request.getParameter("ok");
		if(ok==null)
			ok="";
		boolean print=true;
		
//		if(mstNo!=null){
//			if( currentUser.getLca_id().trim().equalsIgnoreCase(kd_cab.trim()) 
//					&& currentUser.getLus_id().equals(""+tts.getLus_id())	){
//				//update table 
//				if(tts.getFlag_print().intValue()==0){
//					elionsManager.prosesCetakTts(tts,1,"AWAL CETAK");
//				}else if(tts.getFlag_print().intValue()==1){
//					if(ok.equals("1")){
//						elionsManager.prosesCetakTts(tts,2,desc.toUpperCase());
//					}else{
//						flag="1";
//						print=false;
//					}
//				}else{
//					print=false;
//					flag="2";
//				}	
//			}else{
//				print=false;
//				flag="2";
//			}
//			//
//			if(tts.getMst_flag_batal()==1){
//				print=false;
//				flag="3";
//			}
//			
//		}
		
		if(print){
//			String[] to={props.getProperty("email.report_tts")};
			List lsPrintTts=elionsManager.selectLstHistoryPrintTts(mstNo);
			Map mHisPrint=(HashMap)lsPrintTts.get(lsPrintTts.size()-1);
			BigDecimal ke=(BigDecimal)mHisPrint.get("KE");
			//
			report = new Report("Kwitansi Tts", props.getProperty("report.finance.tts.kwitansi_tts"), Report.HTML, null);
			report.addParamText("mst_no", "mst_no", 16, null, true);
			request.getSession().setAttribute("report", report);
			return prepareReport(request, response, report);
		}else{
			Map map=new HashMap();
			map.put("flag",flag);
			map.put("mst_no",mstNo);
			return new ModelAndView("finance/cetak",map);
		}
		
	}
	
	public ModelAndView view_tts(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
//		Date sysDate = elionsManager.selectSysdate();
		report = new Report("Summary TTS",props.getProperty("report.finance.tts.view_tts"),Report.PDF, null);
		report.addParamSelect("Cabang", "Cabang", elionsManager.selectlstCabang(), null, true);
		report.addParamSelect("Nama Admin", "NamaAdmin", elionsManager.selectLstUser2(), null, true);
		report.addParamSelect("Print ", "Print", lsPrint(), null, true);
		//report.addParamDate("Tanggal", "tanggal", true,new Date[] {sysDate, sysDate}, true);
		//kalo reportQueryMethod tidak di-set, maka report akan mengambil data langsung dari database
		report.setReportQueryMethod("selectViewerTts");
		
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView summary_spaj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		String spaj=request.getParameter("spaj");
		report = new Report("Summary SPAJ",props.getProperty("report.bac.summary_spaj"),Report.PDF, null);
		report.addParamDefault("spaj", "spaj", 15, spaj, false);
		request.getSession().setAttribute("report", report);
		List lsRider=elionsManager.selectRider(spaj);
		String rider="",invest="";
		if(lsRider.isEmpty()==false){
			for(int i=0;i<lsRider.size();i++)
				rider=rider+(String)lsRider.get(i)+",";
		}else
			rider="-";
		//
		List lsInvest=elionsManager.selectInvest(spaj);
		if(lsInvest.isEmpty()==false){
			for(int i=0;i<lsInvest.size();i++){
				invest=invest+(String)lsInvest.get(i)+",";
			}
		}else
			invest="-";
		report.addParamDefault("invest","invest",0,invest,false);
		report.addParamDefault("rider", "rider", 0, rider, false);
		return prepareReport(request, response, report);
	}
	
	public List lsPrint(){
		List list=new ArrayList();
		Map map=new HashMap();
		map.put("KEY", new Integer(1));
		map.put("VALUE", "Print");
		list.add(map);
		map=new HashMap();
		map.put("KEY", new Integer(0));
		map.put("VALUE", "Not Print");
		list.add(map);
		return list;
	}
	
	public ModelAndView komisiperagen(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("view_komisi_peragen", props.getProperty("report.view_komisi_peragen"), Report.HTML, null);
		report.addParamText("kode_agen", "kode_agen", 6, null, true);
		report.addParamText("id", "id", 4, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView komisipertglbayar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("view_komisi_tglbayar", props.getProperty("report.view_komisi_tglbayar"), Report.HTML, null);
		report.addParamText("kode_agen", "kode_agen", 6, null, true);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		report.addParamText("id", "id", 4, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView komisiperttp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("view_komisi_ttp", props.getProperty("report.view_komisi_ttp"), Report.HTML, null);
		report.addParamText("kode_agen", "kode_agen", 6, null, true);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		report.addParamText("id", "id", 4, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView komisipertglproduksi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("view_komisi_tglproduksi", props.getProperty("report.view_komisi_tglproduksi"), Report.HTML, null);
		report.addParamText("kode_agen", "kode_agen", 6, null, true);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		report.addParamText("id", "id", 4, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView komisipercabang(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("view_komisi_percabang", props.getProperty("report.view_komisi_percabang"), Report.HTML, null);
		report.addParamText("cabang", "cabang", 6, null, true);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		report.addParamText("id", "id", 4, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	
	public ModelAndView komisipercabangall(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("view_komisi_percabang_all", props.getProperty("report.view_komisi_percabang_all"), Report.HTML, null);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		report.addParamText("id", "id", 4, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}	
	
	public ModelAndView viewer_produksi_perkodeagen(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("produksi_perkodeagen", props.getProperty("report.produksi_perkodeagen"), Report.HTML, null);
		report.addParamText("kode_agen", "kode_agen", 6, null, true);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		report.addParamText("id", "id", 4, null, true);
		report.setCustomParameters(new HashMap());
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}

	public ModelAndView produksi_perkodeagen_rm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("produksi_perkodeagen_rm", props.getProperty("report.produksi_perkodeagen_rm"), Report.HTML, null);
		report.addParamText("kode_agen", "kode_agen", 6, null, true);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		report.addParamText("id", "id", 4, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView viewer_produksi_percabang(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("produksi_percabang", props.getProperty("report.produksi_percabang"), Report.HTML, null);
		report.addParamText("cabang", "cabang", 6, null, true);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		report.addParamText("id", "id", 4, null, true);
		report.setCustomParameters(new HashMap());
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView viewer_produksi_percabang_all(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("produksi_percabang_all", props.getProperty("report.produksi_percabang_all"), Report.HTML, null);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		report.addParamText("id", "id", 4, null, true);
		report.setCustomParameters(new HashMap());
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView viewer_produksi_perlevel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("produksi_perlevel", props.getProperty("report.produksi_perlevel"), Report.HTML, null);
		report.addParamText("level", "level", 6, null, true);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		report.addParamText("id", "id", 4, null, true);
		report.setCustomParameters(new HashMap());
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}

	public ModelAndView viewer_summary_input(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("summary_input", props.getProperty("report.summary_input"), Report.HTML, null);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		report.addParamText("id", "id", 4, null, true);		
		report.setCustomParameters(new HashMap());
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView viewer_summary_input_guthrie(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("summary_input_guthrie", props.getProperty("report.summary_input_guthrie"), Report.HTML, null);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		report.addParamText("id", "id", 4, null, true);		
		report.setCustomParameters(new HashMap());
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView viewer_agen_bonus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("viewer_agen_bonus", props.getProperty("report.viewer_agen_bonus"), Report.HTML, null);
		report.addParamText("kode_agen", "kode_agen", 6, null, true);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		report.addParamText("id", "id", 4, null, true);		
		report.setCustomParameters(new HashMap());
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}	
	
	public ModelAndView viewer_matibayar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("laporan_kematian", props.getProperty("report.laporan_kematian"), Report.HTML, null);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		report.addParamText("lstb_id", "lstb_id", 4, null, true);			
		report.addParamText("id", "id", 4, null, true);		
		report.setCustomParameters(new HashMap());
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}	
	
	public ModelAndView viewer_matipending(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("laporan_kematian_pending", props.getProperty("report.laporan_kematian_pending"), Report.HTML, null);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		report.addParamText("lstb_id", "lstb_id", 4, null, true);	
		report.addParamText("id", "id", 4, null, true);		
		report.setCustomParameters(new HashMap());
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView viewer_sehat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("laporan_kesehatan", props.getProperty("report.laporan_kesehatan"), Report.HTML, null);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		report.addParamText("lstb_id", "lstb_id", 4, null, true);			
		report.addParamText("id", "id", 4, null, true);		
		report.setCustomParameters(new HashMap());
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	/**
	 * Report service level yang baru
	 * 
	 * @author Yusuf
	 * @since May 13, 2008 (9:02:35 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView servicelevel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		
		Report report;
		report = new Report("Service Level", props.getProperty("report.service_level"), Report.PDF, null);
		report.addParamDate("Tanggal Akseptasi", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		report.addParamDefault("User", "user_id", 37, currentUser.getName(), true);
		report.addParamDefault("Jenis Report", "jenis", 0, "servicelevel", false);
		
		//ALL, Agency, Agency (Unit-Link), Bancassurance, Bancassurance (Unit-Link), Worksite, Worksite (Unit-Link)
		List<Map> daftarDist = new ArrayList<Map>();
		Map tmp = new HashMap();
		tmp.put("KEY", "Agency");
		tmp.put("VALUE", "Agency");
		daftarDist.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "Agency (Unit-Link)");
		tmp.put("VALUE", "Agency (Unit-Link)");
		daftarDist.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "Bancassurance");
		tmp.put("VALUE", "Bancassurance");
		daftarDist.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "Bancassurance (Unit-Link)");
		tmp.put("VALUE", "Bancassurance (Unit-Link)");
		daftarDist.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "Worksite");
		tmp.put("VALUE", "Worksite");
		daftarDist.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "Worksite (Unit-Link)");
		tmp.put("VALUE", "Worksite (Unit-Link)");
		daftarDist.add(tmp);		
		report.addParamSelect("Distribusi", "dist", daftarDist, "ALL", true);
		
		report.setReportQueryMethod("selectReportServiceLevel");
		
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	/**
	 * Report Service Level Reinstate
	 * 
	 * @author Yusuf
	 * @since Aug 28, 2008 (6:36:17 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView servicelevel_reinstate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		DateFormat MMyyyy = new SimpleDateFormat("MM/yyyy");
		Date tanggalSatu = defaultDateFormat.parse("01/" + MMyyyy.format(sysdate));
		
		Report report;
		report = new Report("Service Level Reinstate", props.getProperty("report.uw.servicelevel_reinstate"), Report.PDF, null);
		report.addParamDate("Tanggal Terima Reinstate", "tanggal", true, new Date[] {tanggalSatu, sysdate}, true);
		report.addParamDefault("User", "user_id", 37, currentUser.getName(), true);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView service_level_pasbp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		DateFormat MMyyyy = new SimpleDateFormat("MM/yyyy");
		Date tanggalSatu = defaultDateFormat.parse("01/" + MMyyyy.format(sysdate));
		
		Report report;
		report = new Report("Service Level PAS Business Partner", props.getProperty("report.uw.service_level_pasbp"), Report.PDF, null);
		report.addParamDate("Tanggal Produksi", "tanggal", true, new Date[] {tanggalSatu, sysdate}, true);
		report.addParamDefault("User", "user_id", 37, currentUser.getName(), true);
		return prepareReport(request, response, report);
	}
	
public ModelAndView monitorpolis(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		ArrayList<HashMap> reportList = new ArrayList<HashMap>();
		HashMap map = new HashMap();
		map.put("KEY", "0");
		map.put("VALUE", "ALL");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "1");
		map.put("VALUE", "Bancassurance (Unit-Link)");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "2");
		map.put("VALUE", "Bancassurance (Unit-Link) Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "3");
		map.put("VALUE", "Bancassurance (Unit-Link) Non Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "4");
		map.put("VALUE", "Bancassurance (Non Link)");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "5");
		map.put("VALUE", "Bancassurance (Non Link) Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "6");
		map.put("VALUE", "Bancassurance (Non Link) Non Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "7");
		map.put("VALUE", "Worksite (Unit Link)");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "8");
		map.put("VALUE", "Worksite (Unit Link) Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "9");
		map.put("VALUE", "Worksite (Unit Link) Non Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "10");
		map.put("VALUE", "Worksite (Non Link)");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "11");
		map.put("VALUE", "Worksite (Non Link) Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "12");
		map.put("VALUE", "Worksite (Non Link) Non Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "13");
		map.put("VALUE", "Agency Regional (Unit Link)");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "14");
		map.put("VALUE", "Agency Regional (Unit Link) Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "15");
		map.put("VALUE", "Agency Regional (Unit Link) Non Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "16");
		map.put("VALUE", "Agency Regional (Non Link)");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "17");
		map.put("VALUE", "Agency Regional (Non Link) Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "18");
		map.put("VALUE", "Agency Regional (Non Link) Non Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "19");
		map.put("VALUE", "DMTM (Unit Link)");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "20");
		map.put("VALUE", "DMTM (Unit Link) Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "21");
		map.put("VALUE", "DMTM (Unit Link) Non Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "22");
		map.put("VALUE", "DMTM (Non Link)");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "23");
		map.put("VALUE", "DMTM (Non Link) Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "24");
		map.put("VALUE", "DMTM (Non Link) Non Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "25");
		map.put("VALUE", "MNC (Unit Link)");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "26");
		map.put("VALUE", "MNC (Unit Link) Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "27");
		map.put("VALUE", "MNC (Unit Link) Non Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "28");
		map.put("VALUE", "MNC (Non Link)");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "29");
		map.put("VALUE", "MNC (Non Link) Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "30");
		map.put("VALUE", "MNC (Non Link) Non Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "31");
		map.put("VALUE", "EFC (Unit Link)");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "32");
		map.put("VALUE", "EFC (Unit Link) Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "33");
		map.put("VALUE", "EFC (Unit Link) Non Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "34");
		map.put("VALUE", "EFC (Non Link)");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "35");
		map.put("VALUE", "EFC (Non Link) Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "36");
		map.put("VALUE", "EFC (Non Link) Non Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "37");
		map.put("VALUE", "MALLASSURANCE (ALL)");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "38");
		map.put("VALUE", "ALL (Unit-Link)");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "39");
		map.put("VALUE", "ALL (Unit-Link) Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "40");
		map.put("VALUE", "ALL (Unit-Link) Non Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "41");
		map.put("VALUE", "ALL (Non Link)");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "42");
		map.put("VALUE", "ALL (Non Link) Provider");
		reportList.add(map);
		map = new HashMap();
		map.put("KEY", "43");
		map.put("VALUE", "ALL (Non Link) Non Provider");
		reportList.add(map);
		
	
		
		String extend ="";
		
//		List<DropDown> reportPathList = new ArrayList<DropDown>();
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued"), "ALL"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_bancass_unitlink"), "Bancassurance (UNIT-LINK)"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_bancass_unitlink_pro"), "Bancassurance (UNIT-LINK) Provider"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_bancass_unitlink_nonpro"), "Bancassurance (UNIT-LINK) Non-Provider"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_bancass"), "Bancassurance (NON-LINK)"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_bancass_pro"), "Bancassurance (NON-LINK) Provider"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_bancass_nonpro"), "Bancassurance (NON-LINK) Non-Provider"));	
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_worksite"), "Worksite (NON-LINK)"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_worksite_pro"), "Worksite (NON-LINK) Provider"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_worksite_nonpro"), "Worksite (NON-LINK) Non-Provider"));		
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_worksite_unitlink"), "Worksite (UNIT-LINK)"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_worksite_unitlink_pro"), "Worksite (UNIT-LINK) Provider"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_worksite_unitlink_nonpro"), "Worksite (UNIT-LINK) Non-Provider"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_agencyreg_unitlink"), "Agency-Regional (UNIT-LINK)"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_agencyreg_unitlink_pro"), "Agency-Regional (UNIT-LINK) Provider"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_agencyreg_unitlink_nonpro"), "Agency-Regional (UNIT-LINK) Non-Provider"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_agencyreg"), "Agency-Regional (NON-LINK)"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_agencyreg_pro"), "Agency-Regional (NON-LINK) Provider"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_agencyreg_nonpro"), "Agency-Regional (NON-LINK) Non-Provider"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_mnc"), "MNC (NON-LINK)"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_mnc_pro"), "MNC (NON-LINK) Provider"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_mnc_nonpro"), "MNC (NON-LINK) Non-Provider"));	
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_mnc_unitlink"), "MNC (UNIT-LINK)"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_mnc_unitlink_pro"), "MNC (UNIT-LINK) Provider"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_mnc_unitlink_nonpro"), "MNC (UNIT-LINK) Non-Provider"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_fcd"), "FCD (NON-LINK)"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_fcd_pro"), "FCD (NON-LINK) Provider"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_fcd_nonpro"), "FCD (NON-LINK) Non-Provider"));	
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_fcd_unitlink"), "FCD (UNIT-LINK)"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_fcd_unitlink_pro"), "FCD (UNIT-LINK) Provider"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_fcd_unitlink_nonpro"), "FCD (UNIT-LINK)Non-Provider"));
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String pilihReport = ServletRequestUtils.getStringParameter(request, "reportList","");
		Report report;
		
		report = new Report("Monitoring Penerbitan Polis", props.getProperty("report.monitor_polis_issued_new"), Report.PDF, null);
		report.addParamDate("Tanggal (Terima Spaj)", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		report.addParamDefault("User", "user_id", 37, currentUser.getName(), true);
		report.addParamSelectWithoutAll("Report", "reportList", reportList, "", false);
				
		if(pilihReport.equals("0")){
			report.addParamDefault("judul", "judul", 30, "All Distribution", true);
			report.addParamDefault("lsgb", "lsgb", 10, "all", false);
			report.addParamDefault("provider", "provider", 5, "all" , false);
			report.addParamDefault("dist", "dist", 10, "all", false);
			//extend ="AND exists (SELECT 1 FROM eka.mst_slink WHERE msl_new in (1,2) AND reg_spaj = a.reg_spaj AND msl_tahun_ke = a.msbi_tahun_ke AND msl_premi_ke = a.msbi_premi_ke)";
		}else if(pilihReport.equals("1")){
			report.addParamDefault("judul", "judul", 30, "Bancassurance Unit-Link", true);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "all" , false);
			report.addParamDefault("dist", "dist", 10, "bancass", false);
			extend ="AND l.id_dist =9 ";
		}else if(pilihReport.equals("2")){
			report.addParamDefault("judul", "judul", 30, "Bancassurance Unit-Link Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "prov" , false);
			report.addParamDefault("dist", "dist", 10, "bancass", false);
			extend ="AND l.id_dist =9 ";
		}else if(pilihReport.equals("3")){
			report.addParamDefault("judul", "judul", 30, "Bancassurance Unit-Link Non Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "nonprov" , false);
			report.addParamDefault("dist", "dist", 10, "bancass", false);
			extend ="AND l.id_dist =9 ";
		}else if(pilihReport.equals("4")){
			report.addParamDefault("judul", "judul", 30, "Bancassurance Non-Link", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "all" , false);
			report.addParamDefault("dist", "dist", 10, "bancass", false);
			extend ="AND l.id_dist =9 ";
		}else if(pilihReport.equals("5")){
			report.addParamDefault("judul", "judul", 30, "Bancassurance Non-Link Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "prov" , false);
			report.addParamDefault("dist", "dist", 10, "bancass", false);
			extend ="AND l.id_dist =9 ";
		}else if(pilihReport.equals("6")){
			report.addParamDefault("judul", "judul", 30, "Bancassurance Non-Link Non Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "nonprov" , false);
			report.addParamDefault("dist", "dist", 10, "bancass", false);
			extend ="AND l.id_dist =9 ";
		}else if(pilihReport.equals("7")){
			report.addParamDefault("judul", "judul", 30, "Worksite Unit-Link", false);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "all" , false);
			report.addParamDefault("dist", "dist", 10, "ws", false);
			extend ="AND l.id_dist =7 ";
		}else if(pilihReport.equals("8")){
			report.addParamDefault("judul", "judul", 30, "Worksite Unit-Link Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "prov" , false);
			report.addParamDefault("dist", "dist", 10, "ws", false);
			extend ="AND l.id_dist =7 ";
		}else if(pilihReport.equals("9")){
			report.addParamDefault("judul", "judul", 30, "Worksite Unit-Link Non Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "nonprov" , false);
			report.addParamDefault("dist", "dist", 10, "ws", false);
			extend ="AND l.id_dist =7 ";
		}else if(pilihReport.equals("10")){
			report.addParamDefault("judul", "judul", 30, "Worksite Non-Link", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "all" , false);
			report.addParamDefault("dist", "dist", 10, "ws", false);
			extend ="AND l.id_dist =7 ";
		}else if(pilihReport.equals("11")){
			report.addParamDefault("judul", "judul", 30, "Worksite Non-Link Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "prov" , false);
			report.addParamDefault("dist", "dist", 10, "ws", false);
			extend ="AND l.id_dist =7 ";
		}else if(pilihReport.equals("12")){
			report.addParamDefault("judul", "judul", 30, "Worksite Non-Link Non Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "nonprov" , false);
			report.addParamDefault("dist", "dist", 10, "ws", false);
			extend ="AND l.id_dist =7 ";
		}else if(pilihReport.equals("13")){
			report.addParamDefault("judul", "judul", 30, "Agency Regional Unit-Link", false);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "all" , false);
			report.addParamDefault("dist", "dist", 10, "agreg", false);
			extend ="AND l.id_dist  in (1,2) ";
		}else if(pilihReport.equals("14")){
			report.addParamDefault("judul", "judul", 30, "Agency Regional Unit-Link Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "prov" , false);
			report.addParamDefault("dist", "dist", 10, "agreg", false);
			extend ="AND l.id_dist  in (1,2) ";
		}else if(pilihReport.equals("15")){
			report.addParamDefault("judul", "judul", 30, "Agency Regional Unit-Link Non Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "nonprov" , false);
			report.addParamDefault("dist", "dist", 10, "agreg", false);
			extend ="AND l.id_dist  in (1,2) ";
		}else if(pilihReport.equals("16")){
			report.addParamDefault("judul", "judul", 30, "Agency Regional Non-Link", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "all" , false);
			report.addParamDefault("dist", "dist", 10, "agreg", false);
			extend ="AND l.id_dist  in (1,2) ";
		}else if(pilihReport.equals("17")){
			report.addParamDefault("judul", "judul", 30, "Agency Regional Non-Link Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "prov" , false);
			report.addParamDefault("dist", "dist", 10, "agreg", false);
			extend ="AND l.id_dist  in (1,2) ";
		}else if(pilihReport.equals("18")){
			report.addParamDefault("judul", "judul", 30, "Agency Regional Non-Link Non Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "nonprov" , false);
			report.addParamDefault("dist", "dist", 10, "agreg", false);
			extend ="AND l.id_dist  in (1,2) ";
		}else if(pilihReport.equals("19")){
			report.addParamDefault("judul", "judul", 30, "DMTM Unit-Link", false);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "all" , false);
			report.addParamDefault("dist", "dist", 10, "dmtm", false);
			extend ="AND l.id_dist =8 ";
		}else if(pilihReport.equals("20")){
			report.addParamDefault("judul", "judul", 30, "DMTM Unit-Link Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "prov" , false);
			report.addParamDefault("dist", "dist", 10, "dmtm", false);
			extend ="AND l.id_dist =8 ";
		}else if(pilihReport.equals("21")){
			report.addParamDefault("judul", "judul", 30, "DMTM Unit-Link Non Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "nonprov" , false);
			report.addParamDefault("dist", "dist", 10, "dmtm", false);
			extend ="AND l.id_dist =8 ";
		}else if(pilihReport.equals("22")){
			report.addParamDefault("judul", "judul", 30, "DMTM Non-Link", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "all" , false);
			report.addParamDefault("dist", "dist", 10, "dmtm", false);
			extend ="AND l.id_dist =8 ";
		}else if(pilihReport.equals("23")){
			report.addParamDefault("judul", "judul", 30, "DMTM Non-Link Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "prov" , false);
			report.addParamDefault("dist", "dist", 10, "dmtm", false);
			extend ="AND l.id_dist =8 ";
		}else if(pilihReport.equals("24")){
			report.addParamDefault("judul", "judul", 30, "DMTM Non-Link Non Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "nonprov" , false);
			report.addParamDefault("dist", "dist", 10, "dmtm", false);
			extend ="AND l.id_dist =8 ";
		}else if(pilihReport.equals("25")){
			report.addParamDefault("judul", "judul", 30, "MNC Unit-Link", false);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "all" , false);
			report.addParamDefault("dist", "dist", 10, "mnc", false);
			extend ="AND l.id_dist =6 ";
		}else if(pilihReport.equals("26")){
			report.addParamDefault("judul", "judul", 30, "MNC Unit-Link Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "prov" , false);
			report.addParamDefault("dist", "dist", 10, "mnc", false);
			extend ="AND l.id_dist =6 ";
		}else if(pilihReport.equals("27")){
			report.addParamDefault("judul", "judul", 30, "MNC Unit-Link Non Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "nonprov" , false);
			report.addParamDefault("dist", "dist", 10, "mnc", false);
			extend ="AND l.id_dist =6 ";
		}else if(pilihReport.equals("28")){
			report.addParamDefault("judul", "judul", 30, "MNC Non-Link", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "all" , false);
			report.addParamDefault("dist", "dist", 10, "mnc", false);
			extend ="AND l.id_dist =6 ";
		}else if(pilihReport.equals("29")){
			report.addParamDefault("judul", "judul", 30, "MNC Non-Link Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "prov" , false);
			report.addParamDefault("dist", "dist", 10, "mnc", false);
			extend ="AND l.id_dist =6 ";
		}else if(pilihReport.equals("30")){
			report.addParamDefault("judul", "judul", 30, "MNC Non-Link Non Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "nonprov" , false);
			report.addParamDefault("dist", "dist", 10, "mnc", false);
			extend ="AND l.id_dist =6 ";
		}else if(pilihReport.equals("31")){
			report.addParamDefault("judul", "judul", 30, "EFC Unit-Link", false);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "all" , false);
			report.addParamDefault("dist", "dist", 10, "efc", false);
			extend ="AND l.id_dist =11 ";
		}else if(pilihReport.equals("32")){
			report.addParamDefault("judul", "judul", 30, "EFC Unit-Link Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "prov" , false);
			report.addParamDefault("dist", "dist", 10, "efc", false);
			extend ="AND l.id_dist =11 ";
		}else if(pilihReport.equals("33")){
			report.addParamDefault("judul", "judul", 30, "EFC Unit-Link Non Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "nonprov" , false);
			report.addParamDefault("dist", "dist", 10, "efc", false);
			extend ="AND l.id_dist =11 ";
		}else if(pilihReport.equals("34")){
			report.addParamDefault("judul", "judul", 30, "EFC Non-Link", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "all" , false);
			report.addParamDefault("dist", "dist", 10, "efc", false);
			extend ="AND l.id_dist =11 ";
		}else if(pilihReport.equals("35")){
			report.addParamDefault("judul", "judul", 30, "EFC Non-Link Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "prov" , false);
			report.addParamDefault("dist", "dist", 10, "efc", false);
			extend ="AND l.id_dist =11 ";
		}else if(pilihReport.equals("36")){
			report.addParamDefault("judul", "judul", 30, "EFC Non-Link Non Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "nonprov" , false);
			report.addParamDefault("dist", "dist", 10, "efc", false);
			extend ="AND l.id_dist =11 ";
		}else if(pilihReport.equals("37")){
			report.addParamDefault("judul", "judul", 30, "MALLASSURANCE (ALL)", false);
			report.addParamDefault("lsgb", "lsgb", 10, "all", false);
			report.addParamDefault("provider", "provider", 5, "all" , false);
			report.addParamDefault("dist", "dist", 10, "mall", false);
			extend ="AND l.id_dist =10 ";
		}else if(pilihReport.equals("38")){
			report.addParamDefault("judul", "judul", 30, "All Unit-Link", true);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "all" , false);
			report.addParamDefault("dist", "dist", 10, "all", false);			
		}else if(pilihReport.equals("39")){
			report.addParamDefault("judul", "judul", 30, "All Unit-Link Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "prov" , false);
			report.addParamDefault("dist", "dist", 10, "all", false);			
		}else if(pilihReport.equals("40")){
			report.addParamDefault("judul", "judul", 30, "All Unit-Link Non Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "link", false);
			report.addParamDefault("provider", "provider", 5, "nonprov" , false);
			report.addParamDefault("dist", "dist", 10, "all", false);		
		}else if(pilihReport.equals("41")){
			report.addParamDefault("judul", "judul", 30, "All Non-Link", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "all" , false);
			report.addParamDefault("dist", "dist", 10, "all", false);			
		}else if(pilihReport.equals("42")){
			report.addParamDefault("judul", "judul", 30, "All Non-Link Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "prov" , false);
			report.addParamDefault("dist", "dist", 10, "all", false);			
		}else if(pilihReport.equals("43")){
			report.addParamDefault("judul", "judul", 30, "All Non-Link Non Provider", false);
			report.addParamDefault("lsgb", "lsgb", 10, "nonlink", false);
			report.addParamDefault("provider", "provider", 5, "nonprov" , false);
			report.addParamDefault("dist", "dist", 10, "all", false);			
		}
		
		report.setCustomParameters(new HashMap());
		report.getCustomParameters().put("products", products);
		report.setReportQueryMethod("monitorPolisIssued");
		request.getSession().setAttribute("report", report);
		
		
//		report = new Report("Monitoring Penerbitan Polis", reportPathList, Report.PDF, null);
//		report.addParamDate("Tanggal (Terima Spaj)", "tanggal", true, new Date[] {sysdate, sysdate}, true);
//		report.addParamDefault("User", "user_id", 37, currentUser.getName(), true);
		
//		report.addParamSelectWithoutAll("Report", "reportList", reportList, "", false);
//		report.setCustomParameters(new HashMap());
//		report.getCustomParameters().put("products", products);		
//		request.getSession().setAttribute("report", report);
		
		return prepareReport(request, response, report);
	}
	
	public ModelAndView monitorpolis_old(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		List<DropDown> reportPathList = new ArrayList<DropDown>();
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued"), "ALL"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_bancass_unitlink"), "Bancassurance (UNIT-LINK)"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_bancass_unitlink_pro"), "Bancassurance (UNIT-LINK) Provider"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_bancass_unitlink_nonpro"), "Bancassurance (UNIT-LINK) Non-Provider"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_bancass"), "Bancassurance (NON-LINK)"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_bancass_pro"), "Bancassurance (NON-LINK) Provider"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_bancass_nonpro"), "Bancassurance (NON-LINK) Non-Provider"));	
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_worksite"), "Worksite (NON-LINK)"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_worksite_pro"), "Worksite (NON-LINK) Provider"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_worksite_nonpro"), "Worksite (NON-LINK) Non-Provider"));		
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_worksite_unitlink"), "Worksite (UNIT-LINK)"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_worksite_unitlink_pro"), "Worksite (UNIT-LINK) Provider"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_worksite_unitlink_nonpro"), "Worksite (UNIT-LINK) Non-Provider"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_agencyreg_unitlink"), "Agency-Regional (UNIT-LINK)"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_agencyreg_unitlink_pro"), "Agency-Regional (UNIT-LINK) Provider"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_agencyreg_unitlink_nonpro"), "Agency-Regional (UNIT-LINK) Non-Provider"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_agencyreg"), "Agency-Regional (NON-LINK)"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_agencyreg_pro"), "Agency-Regional (NON-LINK) Provider"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_agencyreg_nonpro"), "Agency-Regional (NON-LINK) Non-Provider"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_mnc"), "MNC (NON-LINK)"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_mnc_pro"), "MNC (NON-LINK) Provider"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_mnc_nonpro"), "MNC (NON-LINK) Non-Provider"));	
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_mnc_unitlink"), "MNC (UNIT-LINK)"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_mnc_unitlink_pro"), "MNC (UNIT-LINK) Provider"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_mnc_unitlink_nonpro"), "MNC (UNIT-LINK) Non-Provider"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_fcd"), "FCD (NON-LINK)"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_fcd_pro"), "FCD (NON-LINK) Provider"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_fcd_nonpro"), "FCD (NON-LINK) Non-Provider"));	
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_fcd_unitlink"), "FCD (UNIT-LINK)"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_fcd_unitlink_pro"), "FCD (UNIT-LINK) Provider"));
		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_fcd_unitlink_nonpro"), "FCD (UNIT-LINK)Non-Provider"));
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		Report report;
		report = new Report("Monitoring Penerbitan Polis", reportPathList, Report.PDF, null);
		report.addParamDate("Tanggal (Terima Spaj)", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		report.addParamDefault("User", "user_id", 37, currentUser.getName(), true);
		
		report.setCustomParameters(new HashMap());
		report.getCustomParameters().put("products", products);		
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView servicelevelytd(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		List<DropDown> reportPathList = new ArrayList<DropDown>();
		
		List<Map> repot = new ArrayList<Map>();
		Map map = new HashMap();
		map.put("KEY", "PRODUKSI");
		map.put("VALUE", "REPORT NEW BUSINESS (Tanggal Produksi)");
		repot.add(map);
		map = new HashMap();
		map.put("KEY", "KIRIMPOLIS");
		map.put("VALUE", "REPORT NEW BUSINESS (Tanggal Kirim Polis)");
		repot.add(map);
		
		Integer data_year = sysdate.getYear() ;
		List<Map> tahun = new ArrayList<Map>();
		Map tmp = new HashMap();
		tmp.put("KEY", data_year+1900-5);
		tmp.put("VALUE", data_year+1900-5);
		tahun.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", data_year+1900-4);
		tmp.put("VALUE", data_year+1900-4);
		tahun.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", data_year+1900-3);
		tmp.put("VALUE", data_year+1900-3);
		tahun.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", data_year+1900-2);
		tmp.put("VALUE", data_year+1900-2);
		tahun.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", data_year+1900-1);
		tmp.put("VALUE", data_year+1900-1);
		tahun.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", data_year+1900);
		tmp.put("VALUE", data_year+1900); 
		tahun.add(tmp);
		
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		Report report;
		report = new Report("New Business", reportPathList, Report.PDF, null);
		String pilihRepot = ServletRequestUtils.getStringParameter(request, "repot","");
		String pilihJenis = ServletRequestUtils.getStringParameter(request, "tahun");
		if(pilihRepot.equals("KIRIMPOLIS")){
			report = new Report("REPORT NEW BUSINESS ", props.getProperty("report.summary_terbit_polis"), Report.PDF, null);
		}else{
			report = new Report("REPORT NEW BUSINESS ", props.getProperty("report.service_level_ytd"), Report.PDF, null);
		}
		
		report.addParamSelectWithoutAll("Report", "repot", repot, "", false);
		report.addParamSelectWithoutAll("Tahun", "tahun", tahun, "", false);
//		reportPathList.add(new DropDown(props.getProperty("report.service_level_ytd"), "NEW BUSINESS2008(Tanggal Produksi)"));
//		reportPathList.add(new DropDown(props.getProperty("report.service_level_ytd2009"), "NEW BUSINESS2009(Tanggal Produksi)"));
//		reportPathList.add(new DropDown(props.getProperty("report.service_level_ytd2010"), "NEW BUSINESS2010(Tanggal Produksi)"));
//		reportPathList.add(new DropDown(props.getProperty("report.summary_terbit_polis"), "NEW BUSINESS2010(Tanggal Kirim Polis)"));
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		report.addParamDefault("User", "user_id", 37, currentUser.getName(), true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView report_sub_standard(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		List<DropDown> reportPathList = new ArrayList<DropDown>();
		
		List<Map> repot = new ArrayList<Map>();
		Map map = new HashMap();
		map.put("KEY", "PRODUKSI");
		map.put("VALUE", "REPORT SUB STANDARD (Tanggal Produksi)");
		repot.add(map);
		map = new HashMap();
		map.put("KEY", "AKSEPTASI");
		map.put("VALUE", "REPORT SUB STANDARD (Tanggal Akseptasi)");
		repot.add(map);
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		Report report;
		report = new Report("SUB STANDARD", reportPathList, Report.PDF, null);
		String pilihRepot = ServletRequestUtils.getStringParameter(request, "repot","");
		String pilihJenis = ServletRequestUtils.getStringParameter(request, "tahun");
		
		if(pilihRepot.equals("AKSEPTASI")){
			report = new Report("REPORT SUB STANDARD ", props.getProperty("report.sub_standard_akseptasi"), Report.PDF, null);
		}else{
			report = new Report("REPORT SUB STANDARD ", props.getProperty("report.sub_standard_produksi"), Report.PDF, null);
		}
		
		report.addParamSelectWithoutAll("Report", "repot", repot, "", false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		report.addParamDefault("User", "user_id", 37, currentUser.getName(), true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView summaryterbitpolis(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		List<DropDown> reportPathList = new ArrayList<DropDown>();
		reportPathList.add(new DropDown(props.getProperty("report.summary_terbit_polis"), "NEW BUSINESS2010"));
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		Report report;
		//if(reportPathList.)
		report = new Report("Summary Penerbitan Polis NB", reportPathList, Report.PDF, null);
		report.addParamDate("Tanggal (Terima Spaj)", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		report.addParamDefault("User", "user_id", 37, currentUser.getName(), true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView polisterbit(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		List<DropDown> reportPathList = new ArrayList<DropDown>();
		reportPathList.add(new DropDown(props.getProperty("report.polis_terbit_uw"), "Polis Terbit UW"));
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		Report report;
		report = new Report("New Business", reportPathList, Report.PDF, null);
		report.addParamDate("Tanggal (Terima Spaj)", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		report.addParamDefault("User", "user_id", 37, currentUser.getName(), true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView servicepolis(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		List<DropDown> reportPathList = new ArrayList<DropDown>();
		reportPathList.add(new DropDown(props.getProperty("report.service_polis_issued"), "ALL"));
		reportPathList.add(new DropDown(props.getProperty("report.service_polis_issued_new"), "ALL(HITUNGAN JAM)"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_bancass_unitlink"), "Bancassurance (UNIT-LINK)"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_bancass"), "Bancassurance (NON-LINK)"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_worksite"), "Worksite"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_agencyreg_unitlink"), "Agency-Regional (UNIT-LINK)"));
//		reportPathList.add(new DropDown(props.getProperty("report.monitor_polis_issued_agencyreg"), "Agency-Regional (NON-LINK)"));
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		Report report;
		report = new Report("Service Level Polis", reportPathList, Report.PDF, null);
		report.addParamDate("Tanggal (Kirim Polis)", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		report.addParamDefault("User", "user_id", 37, currentUser.getName(), true);
		
		report.addParamDefault("kodeao", "kodeao", 0, props.getProperty("cabang.ao"), false);
		report.addParamDefault("kodews", "kodews", 0, props.getProperty("cabang.ws"), false);
		report.addParamDefault("kodeagency", "kodeagency", 0, props.getProperty("cabang.agency"), false);
		report.addParamDefault("kodeartha", "kodeartha", 0, props.getProperty("cabang.artha"), false);
		report.addParamDefault("kodefcd", "kodefcd", 0, props.getProperty("cabang.fcd"), false);
		report.addParamDefault("kodemall", "kodemall", 0, props.getProperty("cabang.mall"), false);
		
		if("POST".equals(request.getMethod())
				&& props.getProperty("report.service_polis_issued_new").equals(request.getParameter("reportPath"))) {
			report.setReportQueryMethod("selectReportSLPolicyIssued");
		}
	
		/*List<Map> daftarDist = new ArrayList<Map>();
		Map tmp = new HashMap();
		tmp.put("KEY", "Agency");
		tmp.put("VALUE", "Agency");
		daftarDist.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "Agency (Unit-Link)");
		tmp.put("VALUE", "Agency (Unit-Link)");
		daftarDist.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "Bancassurance");
		tmp.put("VALUE", "Bancassurance");
		daftarDist.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "Bancassurance (Unit-Link)");
		tmp.put("VALUE", "Bancassurance (Unit-Link)");
		daftarDist.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "Worksite");
		tmp.put("VALUE", "Worksite");
		daftarDist.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "Worksite (Unit-Link)");
		tmp.put("VALUE", "Worksite (Unit-Link)");
		daftarDist.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "MallAssurance");
		tmp.put("VALUE", "MallAssurance");
		daftarDist.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "MallAssurance (Unit-Link)");
		tmp.put("VALUE", "MallAssurance (Unit-Link)");
		daftarDist.add(tmp);
		report.addParamSelect("Distribusi", "dist", daftarDist, "ALL", true);
		
		report.setReportQueryMethod("selectReportServiceLevel");
		*/
		report.setCustomParameters(new HashMap());
		report.getCustomParameters().put("products", products);		
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}

	public ModelAndView permohonan_bea(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("permohonan_bea_materai", props.getProperty("report.permohonan_bea_materai"), Report.HTML, null);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);		
	}	
	
	public ModelAndView estimasi_bea(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("estimasi_bea_meterai", props.getProperty("report.estimasi_bea_meterai"), Report.HTML, null);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView estimasi_perproduk(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("estimasi_produk", props.getProperty("report.estimasi_produk"), Report.HTML, null);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView pembubuhan_bea(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("pembubuhan_bea_materai", props.getProperty("report.pembubuhan_bea_materai"), Report.HTML, null);
		report.addParamText("kode", "kode", 22, null, true);
		report.addParamText("jumlah_setoran", "jumlah_setoran", 30, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}	
	
	public ModelAndView kwitansi_pt_worksite(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("kwitansi_pt", props.getProperty("report.worksite.kwitansi_pt"), Report.HTML, null);
		report.addParamText("kode", "kode", 22, null, true);
		report.addParamText("tgl1", "tgl1", 22, null, true);
		report.addParamText("tgl2", "tgl2", 22, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}	

	public ModelAndView slip_pot_komisi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		String spaj=ServletRequestUtils.getStringParameter(request, "spaj");
		report = new Report("slip_potongan_komisi", props.getProperty("report.uw.slip_potongan_komisi"), Report.HTML, null);
		report.addParamText("spaj", "spaj", 22, request.getParameter("spaj"), true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}	

	public ModelAndView view_list_pot_komisi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
//		Report report;
//		report = new Report("list_pembayaran_komisi", props.getProperty("report.uw.list_pembayaran_komisi"), Report.HTML, null);
//		report.addParamText("tgl1", "tgl1", 6, null, true);
//		report.addParamText("tgl2", "tgl2", 6, null, true);
//		report.addParamText("user", "user", 50, currentUser.getLus_full_name(), true);
//		request.getSession().setAttribute("report", report);
//		return prepareReport(request, response, report);
//		Map map=new HashMap();
//		return new ModelAndView("uw/view_list_pot_komisi",map);
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		
		Report report;
		report = new Report("list_pemotongan_komisi", props.getProperty("report.uw.list_pemotongan_komisi"), Report.PDF, null);
		report.addParamDefault("user", "user", 15, currentUser.getName(), false);
		report.addParamDate("Periode Komisi", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		

		return prepareReport(request, response, report);

	}	
	
	public ModelAndView view_list_pem_komisi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		
		Report report;
		report = new Report("list_pembayaran_komisi", props.getProperty("report.uw.list_pembayaran_komisi"), Report.PDF, null);
		report.addParamDefault("user", "user", 15, currentUser.getName(), false);
		report.addParamDate("Periode Komisi", "tanggal", true, new Date[] {sysdate, sysdate}, true);
//		report.setFlagEmail(true);
		report.setFlag(true);
		
		return prepareReport(request, response, report);

	}	
	
	public ModelAndView lap_pemakaian(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("lap_pemakaian_blanko", props.getProperty("report.lap_pemakaian_blanko"), Report.HTML, null);
		report.addParamText("user_id", "user_id", 4, null, true);
		report.addParamText("user", "user", 30, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}		
	
	public ModelAndView view_list_polis_asm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		
		Report report;
		report = new Report("list_polis_asm", props.getProperty("report.uw.list_polis_asm"), Report.PDF, null);
		report.addParamDefault("user", "user", 15, currentUser.getName(), false);
		report.addParamDate("Periode Komisi", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		report.setFlagEmail(true);
		
		return prepareReport(request, response, report);

	}	
	
	public ModelAndView followup_bill(HttpServletRequest request, HttpServletResponse response) throws Exception {		
		Report report;
		report = new Report("followup_billing", props.getProperty("report.followup_billing"), Report.HTML, null);
		report.addParamText("admin", "admin", 6, null, true);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}		
	
	public ModelAndView view_top_producer(HttpServletRequest request, HttpServletResponse response) throws Exception {		Report report;
		report = new Report("top_producer", props.getProperty("report.top_producer"), Report.HTML, null);
		report.addParamText("id", "id", 6, null, true);
		report.addParamText("tahun", "tahun", 6, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView softcopypolis(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("softcopy_polis", props.getProperty("report.softcopy_polis"), Report.HTML, null);
		report.addParamText("user", "user", 30, null, true);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}	
	
	public ModelAndView simassehat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("simas_sehat", props.getProperty("report.simas_sehat"), Report.HTML, null);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("user", "user", 30, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}	
	
	public ModelAndView softcopypolis_perhari(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("softcopy_polis_perhari", props.getProperty("report.softcopy_polis_perhari"), Report.HTML, null);
		report.addParamText("tgl1", "tgl1", 6, null, true);
		report.addParamText("tgl2", "tgl2", 6, null, true);
		report.addParamText("tanggal1", "tanggal1", 6, null, true);
		report.addParamText("tanggal2", "tanggal2", 6, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}		

	public ModelAndView report_akseptasi_khusus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("Report Akseptasi Khusus", props.getProperty("report.list_akseptasi_khusus"), Report.HTML, null);
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id");
		String lus_id=ServletRequestUtils.getStringParameter(request, "lus_id");
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		String window = ServletRequestUtils.getStringParameter(request, "window");
		String cekMail = ServletRequestUtils.getStringParameter(request, "cekMail"); 
		String to = request.getParameter("to");
		String isPDF = request.getParameter("isPDF");
		String isXls = request.getParameter("isXls");
		String cc=null;

		if(lca_id.equals("0")){//all cabang
			report = new Report("Laporan Akseptasi Khusus Semua Cabang",props.getProperty("report.uw.list_akseptasi_khusus_all"),Report.PDF, null);
			report.setReportEmailTo(new String[] {"Sugeng@ekalife2000.com", "hansen@sinarmasmsiglife.co.id", "jos@sinarmasmsiglife.co.id", "ike_b@sinarmasmsiglife.co.id","Fachrizal@sinarmasmsiglife.co.id"});
		}else{//per cabang		
			report = new Report("Laporan Akseptasi Khusus Per Cabang",props.getProperty("report.uw.list_akseptasi_khusus"),Report.PDF, null);	
		}
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		report.addParamDefault("lca_id", "lca_id", 0,lca_id, false);
		report.addParamDefault("window", "window", 0, window, false);
		report.addParamDefault("cekMail", "cekMail", 0, cekMail, false);
		report.addParamDefault("to", "to", 0, to, false);
		report.addParamDefault("isPDF", "isPDF", 0, isPDF, false);
//		report.setFlagEmailCab(true); //u/ pengiriman email ke cabang
		report.setFlagEC(true);
		if(cekMail.equals("true")) {
			
			SimpleDateFormat formatDate =new SimpleDateFormat("dd/MM/yyyy");
			Date MSAG_BEG_DATE = formatDate.parse(tanggalAwal);
			Date MSAG_END_DATE = formatDate.parse(tanggalAkhir);
			if (lca_id.equals("06")||lca_id.equals("05")||lca_id.equals("18")||lca_id=="05"||lca_id=="22"||lca_id.equals("22")||lca_id=="11"||lca_id.equals("23")||lca_id.equals("13")){
				to= ("Sugeng@ekalife2000.com;jos@sinarmasmsiglife.co.id");
				String[] mailTo = to.split(";");
				report.setReportEmailTo(new String[] {"Sugeng@ekalife2000.com", "jos@sinarmasmsiglife.co.id", "hanifah@sinarmasmsiglife.co.id"});
//				report = new Report("Laporan Akseptasi Khusus Semua Cabang",props.getProperty("report.uw.list_akseptasi_khusus_all"),Report.PDF, null);
//				report.isFlagEmail();
			}else if (lca_id.equals("32")||lca_id.equals("35")||lca_id.equals("47")||lca_id=="32"||lca_id=="51"||lca_id.equals("04")||lca_id.equals("10")){
				to=("Sugeng@ekalife2000.com;hansen@sinarmasmsiglife.co.id;hanifah@sinarmasmsiglife.co.id");
				String[] mailTo = to.split(";");
				report.setReportEmailTo(new String[] {"Sugeng@ekalife2000.com", "hansen@sinarmasmsiglife.co.id", "hanifah@sinarmasmsiglife.co.id"});
//				report.isFlagEmail();
			}else{
				to= ("hanifah@sinarmasmsiglife.co.id");
				String[] mailTo = to.split(";");
				report.setReportEmailTo(new String[] {"Sugeng@ekalife2000.com", "hansen@sinarmasmsiglife.co.id", "jos@sinarmasmsiglife.co.id", "ike_b@sinarmasmsiglife.co.id","Fachrizal@sinarmasmsiglife.co.id","hanifah@sinarmasmsiglife.co.id"});
//				report.setFlagEmail(true);
			}
			report.setFlagEmail(true);
			report.setReportSubject("Report Akseptasi Khusus");
			report.setReportMessage("Akseptasi Khusus per tanggal " + FormatDate.toIndonesian(MSAG_BEG_DATE) + " - " + FormatDate.toIndonesian(MSAG_END_DATE));
		}
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}	
	
	public ModelAndView perbaikanpolis(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("perbaikanpolis", props.getProperty("report.perbaikan_polis"), Report.HTML, null);
		report.addParamText("nopolis", "nopolis", 30, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}	
	
	/***
	 * Dian natalia
	 * u/ pint BTPP
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView cetak_btpp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		String mstNo=request.getParameter("mst_no");
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		List lsBtpp=elionsManager.selectAllMstBtpp(mstNo,"1",null,currentUser.getLca_id());
		Btpp btpp=(Btpp)lsBtpp.get(0);
		String flag="0";
		String desc=request.getParameter("desc");
		String kd_cab=btpp.getLst_kd_cab();
		String ok=request.getParameter("ok");
		if(ok==null)
			ok="";
		boolean print=true;
		
		if(print){

			List lsPrintBtpp=elionsManager.selectLstHistoryPrintBtpp(mstNo);	
			report = new Report("Kwitansi Btpp", props.getProperty("report.bas.btpp.kwitansi_btpp"), Report.PDF, null);
			report.addParamText("mst_no", "mst_no", 16, mstNo, false);
			request.getSession().setAttribute("report", report);
			return prepareReport(request, response, report);
		}else{
			Map map=new HashMap();
			map.put("flag",flag);
			map.put("mst_no",mstNo);
			return new ModelAndView("bas/cetak_btpp",map);
		}
		
	}
	
	/**
	 * Report KYC - New Business Case
	 * @author Yusuf Sutarko
	 * @since Aug 31, 2007 (11:21:18 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView kyc_nb(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//List list = elionsManager.selectKycNewBusiness(tgl_awal, tgl_akhir);
		return null;
	}
	
	/**
	 * Report KYC - Putus Kontrak / Redemption
	 * @author Yusuf Sutarko
	 * @since Aug 31, 2007 (11:21:18 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView kyc_surrender(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}
	
	public ModelAndView provider_akseptasi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
//		Date sysDate = elionsManager.selectSysdate();
		Date sysDate = new Date();
		report = new Report("List Akseptasi Eka Sehat Provider",props.getProperty("report.uw.provider_akseptasi"),Report.XLS, null);
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		Date tglAwal=null;
		if(tanggalAwal!=null)
			tglAwal=FormatDate.toDateWithSlash(tanggalAwal);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {tglAwal, sysDate}, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	
	public ModelAndView view_btpp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
//		Date sysDate = elionsManager.selectSysdate();
		report = new Report("Summary BTPP",props.getProperty("report.bas.btpp.view_btpp"),Report.PDF, null);
		report.addParamSelect("Cabang", "Cabang", elionsManager.selectlstCabang(), null, true);
		report.addParamSelect("Nama Admin", "NamaAdmin", elionsManager.selectLstUser2(), null, true);
		report.addParamSelect("Print ", "Print", lsPrint(), null, true);
		//report.addParamDate("Tanggal", "tanggal", true,new Date[] {sysDate, sysDate}, true);
		//kalo reportQueryMethod tidak di-set, maka report akan mengambil data langsung dardatabase
		report.setReportQueryMethod("selectViewerBtpp");
		
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	/**Fungsi : Untuk Menampilkan report PEP
	Keterangan :	
	Polis yang dikeluarkan dalam report ini adalah polis dengan  :	
	o	tidak ada minimal premi
	o	Pekerjaan yang tergolong PEP/HRC dengan tanda v pada kolom PEP	
		
	* 
	* @param request
	* @param response
	* @return
	* @throws Exception
	* @author Iga Ukiarwan 
	*/
	public ModelAndView report_pep(HttpServletRequest request, HttpServletResponse response) throws Exception {
	Report report;
	report = new Report("Report PEP", props.getProperty("report.report_pep"), Report.XLS, null);
	//Date sysdate=elionsManager.selectSysdate();
	SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
	String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
	
	Date tglAwal=null,tglAkhir=null;
	if(tanggalAwal!=null)
	tglAwal=FormatDate.toDateWithSlash(tanggalAwal);
	if(tanggalAkhir!=null)
	tglAkhir=FormatDate.toDateWithSlash(tanggalAkhir);
	report.addParamDate("Tanggal Terima SPAJ", "tanggal", true, new Date[] {tglAwal, tglAkhir}, true);
	
//	ArrayList lsPep=Common.serializableList(bacManager.selectReportPep(tanggalAwal,tanggalAkhir));
	
	report.setResultList(Common.serializableList(bacManager.selectReportPep(tanggalAwal,tanggalAkhir)));

	return prepareReport(request, response, report);
	}
	
	/**Fungsi : Untuk Menampilkan report Daily Monitoring KYC- New Business Case
				Keterangan :	
				Polis yang dikeluarkan dalam report ini adalah polis dengan  :	
				- premi yang dibayar >=  Rp. 100 juta ( untuk premi Link termasuk Top Up )  ATAU	
				- polis dengan pekerjaan : ibu rumah tangga, high risk customer (lihat sheet High Risk Customer)	
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author Ferry Harlim
	 */
	public ModelAndView report_new_business_case(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("Report Daily Monitoring KYC", props.getProperty("report.report_new_business_case"), Report.PDF, null);
//		Date sysdate=elionsManager.selectSysdate();
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");

		Date tglAwal=null,tglAkhir=null;
		if(tanggalAwal!=null)
			tglAwal=FormatDate.toDateWithSlash(tanggalAwal);
		if(tanggalAkhir!=null)
			tglAkhir=FormatDate.toDateWithSlash(tanggalAkhir);
		report.addParamDate("Tanggal Terima SPAJ", "tanggal", true, new Date[] {tglAwal, tglAkhir}, true);
		
		ArrayList lsKyc=Common.serializableList(elionsManager.selectKYCnewBis_utama(tanggalAwal,tanggalAkhir));
		
		report.setResultList(Common.serializableList(elionsManager.selectLsKycNewBus(lsKyc)));
		
		return prepareReport(request, response, report);
	}
	
	public ModelAndView report_top_up_case(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("Report Daily Monitoring KYC", props.getProperty("report.report_top_up_case"), Report.PDF, null);
//		Date sysdate=elionsManager.selectSysdate();
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");

		Date tglAwal=null,tglAkhir=null;
		if(tanggalAwal!=null)
			tglAwal=FormatDate.toDateWithSlash(tanggalAwal);
		if(tanggalAkhir!=null)
			tglAkhir=FormatDate.toDateWithSlash(tanggalAkhir);
		report.addParamDate("Tanggal Terima SPAJ", "tanggal", true, new Date[] {tglAwal, tglAkhir}, true);
		
		List KYCtopUp=uwManager.selectKYCtopup_main(tanggalAwal,tanggalAkhir);
	
		report.setResultList(uwManager.selectLsKycTopUp(KYCtopUp));
		
		return prepareReport(request, response, report);
	}	
	
	/**Fungsi : Untuk Menampilkan report Daily Monitoring KYC- Putus Kontrak,redemption, NT
	 * 			Jenis Transaksi :  	PK = Pemutusan Kontrak		
     *                     			NT = pengambilan Nilai Tunai		
     *                     			RD All = redemption unit link / penarikan unit link seluruhnya		
     *                     			RD = redemption unit link / penarikan unit link		sebagian
	 *	    			   			NB = New Business
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author Ferry Harlim
	 */
	public ModelAndView report_new_business_case2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("Report Daily Monitoring KYC", props.getProperty("report.report_new_business_case2"), Report.PDF, null);
//		Date sysdate=elionsManager.selectSysdate();
		Date sysdate=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		
		
		report.addParamDate("Tanggal Transaksi", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		List lsKyc=elionsManager.selectKYCnewBis_utamaPK(tanggalAwal,tanggalAkhir);
		report.setResultList(elionsManager.selectLsKycNewBusPutusKontrak(lsKyc));
		return prepareReport(request, response, report);
	}	
	
	/**
	 * dian Natalia
	 * digunakan untuk meliahat detail produksi polis cabang
	 * 	 */
	
	public ModelAndView report_uwCetakPolisCabang(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		Map param= new HashMap();
		List lsRegion=new ArrayList();
		List lsAdmin=new ArrayList();
		List lsAgency = new ArrayList();
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());		
		String All=ServletRequestUtils.getStringParameter(request, "All");
		String lsrg = ServletRequestUtils.getStringParameter(request, "lsrg");
		Integer lock=0;
		Integer flag=ServletRequestUtils.getIntParameter(request, "l");
		if(flag==null)
		param.put("lock",lock);
		param.put("lsRegion", lsRegion);
		param.put("lsAdmin", lsAdmin);
		param.put("lca_id",lca_id);
		param.put("tglAwal",tglAwal);
		param.put("tglAkhir",tglAkhir);
		param.put("lsAgency", lsAgency);
//		param.put("tgl", elionsManager.selectSysdate());
		param.put("tgl", new Date());
	
		if (request.getParameter("Cari")!=null){
			if(lca_id.equals("0")){//all cabang
				report = new Report("Laporan Cetak POlis Cabang",props.getProperty("report.bas.cetakAllPolisCabang"),Report.PDF, null);
				report.addParamDefault("tglAwal", "tanggalAwal", 0,tglAwal,  false);
				report.addParamDefault("tglAkhir", "tanggalAkhir", 0,tglAkhir,  false);
				report.addParamDefault("lca_id", "lca_id", 0,lca_id, false);
				request.getSession().setAttribute("report", report);
				return prepareReport(request, response, report);
			}else//per cabang
				report = new Report("Laporan Cetak POlis Cabang",props.getProperty("report.bas.cetakPolisCabang"),Report.PDF, null);
		
				report.addParamDefault("tglAwal", "tanggalAwal", 0,tglAwal,  false);
				report.addParamDefault("tglAkhir", "tanggalAkhir", 0,tglAkhir,  false);
				report.addParamDefault("lca_id", "lca_id", 0,lca_id, false);	
				request.getSession().setAttribute("report", report);
				return prepareReport(request, response, report);
		}
	
		return new ModelAndView("report/report_uwCetakPolisCabang",param);
	}
	
	public ModelAndView cetakPolisCabang(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map param= new HashMap();
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		String lsrg = ServletRequestUtils.getStringParameter(request, "lsrg");
		param.put("tglAwal",tglAwal);
		param.put("tglAkhir", tglAkhir);
		param.put("lca_id",lca_id);	
		Report report;
		report = new Report("Laporan Cetak POlis Cabang",props.getProperty("report.bas.cetakPolisCabang"),Report.PDF, null);
		report.addParamDefault("tglAwal", "tanggalAwal", 0,tglAwal,  false);
		report.addParamDefault("tglAkhir", "tanggalAkhir", 0,tglAkhir,  false);
		report.addParamDefault("lca_id", "lca_id", 0,lca_id, false);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
		
	}
	/**
	 * dian Natalia
	 * digunakan untuk akseptasi khusus
	 */
	public ModelAndView akseptasiPendingKomisi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		Report report;
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		map.put("tanggalAwal",tanggalAwal);
		map.put("tanggalAkhir", tanggalAkhir);
		
		if (request.getParameter("show")!=null){
			report = new Report("Akseptasi Pending Komisi",props.getProperty("report.uw.reportAkseptasiKhusus"),Report.PDF, null);
			report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
			report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
			request.getSession().setAttribute("report", report);
			return prepareReport(request, response, report);
		}
		return new ModelAndView("report/akseptasiPendingKomisi",map);
	}
	public ModelAndView reportAkseptasiKhusus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map param= new HashMap();
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		String window = ServletRequestUtils.getStringParameter(request, "window");
		String cekMail = ServletRequestUtils.getStringParameter(request, "cekMail"); 
		
		String to = request.getParameter("to");
		String isPDF = request.getParameter("isPDF");
		String isXls = request.getParameter("isXls");
		
		param.put("tanggalAwal",tanggalAwal);
		param.put("tanggalAkhir", tanggalAkhir);
		
	
		Report report;
		report = new Report("Akseptasi Khusus",props.getProperty("report.uw.reportAkseptasiKhusus"),Report.PDF, null);
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		report.addParamDefault("window", "window", 0, window, false);
		report.addParamDefault("cekMail", "cekMail", 0, cekMail, false);
		report.addParamDefault("to", "to", 0, to, false);
		report.addParamDefault("cc", "cc", 0, to, false);
		report.addParamDefault("isPDF", "isPDF", 0, isPDF, false);
		report.addParamDefault("isXls", "isXls", 0, isXls, false);
		report.setFlagEmailCab(true); //u/ pengiriman email ke cabang
		report.setFlagEC(true);
		request.getSession().setAttribute("report", report);
		
		if(cekMail.equals("true")) {
			String[] mailTo = to.split(";");
			SimpleDateFormat formatDate =new SimpleDateFormat("dd/MM/yyyy");
			Date MSAG_BEG_DATE = formatDate.parse(tanggalAwal);
			Date MSAG_END_DATE = formatDate.parse(tanggalAkhir);
			
			report.setFlagEmail(true);
			report.setReportEmailTo(mailTo);
			report.setReportSubject("Report Akseptasi Khusus");
			report.setReportMessage("Report Akseptasi Khusus per tanggal " + FormatDate.toIndonesian(MSAG_BEG_DATE) + " - " + FormatDate.toIndonesian(MSAG_END_DATE));
		}
		
		return prepareReport(request, response, report);
		
	}

	public ModelAndView extraPremi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
				Map param= new HashMap();
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		param.put("tglAwal",tglAwal);
		param.put("tglAkhir", tglAkhir);
		param.put("lca_id",lca_id);	
		Report report;
		report = new Report("Report Extra Premi",props.getProperty("report.uw.summarExtraPremi"),Report.PDF, null);
		report.addParamDefault("tglAwal", "tanggalAwal", 0,tglAwal,  false);
		report.addParamDefault("tglAkhir", "tanggalAkhir", 0,tglAkhir,  false);
		report.addParamDefault("lca_id", "lca_id", 0,lca_id, false);
		report.addParamDefault("lus_login_name", "lus_login_name", 15, currentUser.getName(), false);
		report.setFlagEmailCab(true); //u/ pengiriman email ke cabang
		report.setFlagEC(true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
		
	}
	public ModelAndView reportFurtherRequirement(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map param=new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		List lsCabang=new ArrayList();
		List lsAdmin=new ArrayList();
			lsCabang=elionsManager.selectlstCabangForAkseptasiKhusus();
			DropDown cabang=new DropDown();
			cabang.setKey("0");
			cabang.setValue("ALL");
			lsCabang.add(0, cabang);
		List lsCabangToday=elionsManager.selectlstCabangForAkseptasiKhususToday();	
		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());
		Integer flag=ServletRequestUtils.getIntParameter(request, "l");
		if(flag==null)
			lus_id=currentUser.getLus_id();
		param.put("lsCabang", lsCabang);
		if(lsCabangToday.isEmpty())
			param.put("flagCabang",1);
		else
			param.put("flagCabang",0);
		param.put("lsCabangToday",lsCabangToday);
		param.put("lca_id",lca_id);
		param.put("lus_id",lus_id);
//		param.put("tgl", elionsManager.selectSysdate());
		param.put("tgl", new Date());
		return new ModelAndView("report/reportFurtherRequirement", param);
	}
	
	public ModelAndView list_further_requirement(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("Report Akseptasi Khusus", props.getProperty("report.list_akseptasi_khusus"), Report.HTML, null);
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id");
		String lus_id=ServletRequestUtils.getStringParameter(request, "lus_id");
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		String window = ServletRequestUtils.getStringParameter(request, "window");
		String cekMail = ServletRequestUtils.getStringParameter(request, "cekMail"); 
		String to = request.getParameter("to");
		String isPDF = request.getParameter("isPDF");
		String isXls = request.getParameter("isXls");
		if(lca_id.equals("0"))//all cabang  
			report = new Report("Report List Further Requirement",props.getProperty("report.uw.reportFurtherRequirement_all"),Report.PDF, null);
		else//per cabang
			report = new Report("Report List Further Requirement",props.getProperty("report.uw.reportFurtherRequirement"),Report.PDF, null);
	
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		report.addParamDefault("lca_id", "lca_id", 0,lca_id, false);
		//report.setFlagEmailCab(true); //u/ pengiriman email ke cabang
		//report.setFlagEC(true);

		return prepareReport(request, response, report);
	}

	public ModelAndView reportDecline(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		Report report;
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		map.put("tanggalAwal",tanggalAwal);
		map.put("tanggalAkhir", tanggalAkhir);
		
		if (request.getParameter("show")!=null){
			report = new Report("Report Decline",props.getProperty("report.uw.reportDecline"),Report.PDF, null);
			report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
			report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
			request.getSession().setAttribute("report", report);
			return prepareReport(request, response, report);
		}
		return new ModelAndView("report/reportDecline",map);
	}
	public ModelAndView report_decline(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		String lus_id=ServletRequestUtils.getStringParameter(request, "lus_id");
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		String window = ServletRequestUtils.getStringParameter(request, "window");
		String cekMail = ServletRequestUtils.getStringParameter(request, "cekMail"); 
		String to = request.getParameter("to");
		String isPDF = request.getParameter("isPDF");
		String isXls = request.getParameter("isXls");
		
		report = new Report("Report Decline", props.getProperty("report.uw.reportDecline"), Report.HTML, null);

		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		report.addParamDefault("window", "window", 0, window, false);
		report.addParamDefault("cekMail", "cekMail", 0, cekMail, false);
		report.addParamDefault("to", "to", 0, to, false);
		report.addParamDefault("isPDF", "isPDF", 0, isPDF, false);
		report.addParamDefault("isXls", "isXls", 0, isXls, false);
		report.setFlagEmailCab(true); //u/ pengiriman email ke cabang
		report.setFlagEC(true);
		
		if(cekMail.equals("true")) {
			String[] mailTo = to.split(";");
			SimpleDateFormat formatDate =new SimpleDateFormat("dd/MM/yyyy");
			Date MSAG_BEG_DATE = formatDate.parse(tanggalAwal);
			Date MSAG_END_DATE = formatDate.parse(tanggalAkhir);
			
			report.setFlagEmail(true);
			report.setReportEmailTo(mailTo);
			report.setReportSubject("Report Decline");
			report.setReportMessage("Report Decline per tanggal " + FormatDate.toIndonesian(MSAG_BEG_DATE) + " - " + FormatDate.toIndonesian(MSAG_END_DATE));
		}

		return prepareReport(request, response, report);
	}
	public ModelAndView postponed(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		Report report;
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		map.put("tanggalAwal",tanggalAwal);
		map.put("tanggalAkhir", tanggalAkhir);
		
		if (request.getParameter("show")!=null){
			report = new Report("Daftar Postponed",props.getProperty("report.uw.reportPostponed"),Report.PDF, null);
			report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
			report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
			request.getSession().setAttribute("report", report);
			return prepareReport(request, response, report);
		}
		return new ModelAndView("report/postponed",map);
	}
	
	public ModelAndView polis_expired_lbh90hr(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map param= new HashMap();
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		String window = ServletRequestUtils.getStringParameter(request, "window");
		String cekMail = ServletRequestUtils.getStringParameter(request, "cekMail"); 
		
		String to = request.getParameter("to");
		String isPDF = request.getParameter("isPDF");
		String isXls = request.getParameter("isXls");
		
		param.put("tanggalAwal",tanggalAwal);
		param.put("tanggalAkhir", tanggalAkhir);
		
	
		Report report;
		report = new Report("Polis Expired",props.getProperty("report.uw.report_polis_expired"),Report.PDF, null);
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		report.addParamDefault("window", "window", 0, window, false);
		report.addParamDefault("cekMail", "cekMail", 0, cekMail, false);
		report.addParamDefault("to", "to", 0, to, false);
		report.addParamDefault("cc", "cc", 0, to, false);
		report.addParamDefault("isPDF", "isPDF", 0, isPDF, false);
		report.addParamDefault("isXls", "isXls", 0, isXls, false);
		report.setFlagEmailCab(true); //u/ pengiriman email ke cabang
		report.setFlagEC(true);
		request.getSession().setAttribute("report", report);
		
		if(cekMail.equals("true")) {
			String[] mailTo = to.split(";");
			SimpleDateFormat formatDate =new SimpleDateFormat("dd/MM/yyyy");
			Date MSAG_BEG_DATE = formatDate.parse(tanggalAwal);
			Date MSAG_END_DATE = formatDate.parse(tanggalAkhir);
			
			report.setFlagEmail(true);
			report.setReportEmailTo(mailTo);
			report.setReportSubject("Report Akseptasi Khusus");
			report.setReportMessage("Report Akseptasi Khusus per tanggal " + FormatDate.toIndonesian(MSAG_BEG_DATE) + " - " + FormatDate.toIndonesian(MSAG_END_DATE));
		}
		
		return prepareReport(request, response, report);
	}
	
	public ModelAndView reportPostponed(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lus_id=ServletRequestUtils.getStringParameter(request, "lus_id");
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		String window = ServletRequestUtils.getStringParameter(request, "window");
		String cekMail = ServletRequestUtils.getStringParameter(request, "cekMail"); 
		String to = request.getParameter("to");
		String isPDF = request.getParameter("isPDF");
		String isXls = request.getParameter("isXls");
		
		report = new Report("Daftar SPAJ Postponed", props.getProperty("report.uw.reportPostponed"), Report.HTML, null);

		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		report.addParamDefault("window", "window", 0, window, false);
		report.addParamDefault("cekMail", "cekMail", 0, cekMail, false);
		report.addParamDefault("to", "to", 0, to, false);
		report.addParamDefault("isPDF", "isPDF", 0, isPDF, false);
		report.addParamDefault("isXls", "isXls", 0, isXls, false);
		report.setFlagEmailCab(true); //u/ pengiriman email ke cabang
		report.setFlagEC(true);
		
		if(cekMail.equals("true")) {
			String[] mailTo = to.split(";");
			SimpleDateFormat formatDate =new SimpleDateFormat("dd/MM/yyyy");
			Date MSAG_BEG_DATE = formatDate.parse(tanggalAwal);
			Date MSAG_END_DATE = formatDate.parse(tanggalAkhir);
			
			report.setFlagEmail(true);
			report.setReportEmailTo(mailTo);
			report.setReportSubject("Report Postponed");
			report.setReportMessage("Report Postponed per tanggal " + FormatDate.toIndonesian(MSAG_BEG_DATE) + " - " + FormatDate.toIndonesian(MSAG_END_DATE));
		}

		return prepareReport(request, response, report);
	}
	
	public ModelAndView reportFurtherRBanca(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		Report report;
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		String tim=ServletRequestUtils.getStringParameter(request, "tim");
		map.put("tanggalAwal",tanggalAwal);
		map.put("tanggalAkhir", tanggalAkhir);
		map.put("tim", tim);
		
		if (request.getParameter("show")!=null){
			if(tim.equals("1")){
			report = new Report("Further Requirement Bancassurance Tim 1",props.getProperty("report.uw.reportFurtherRBancaTim1"),Report.PDF, null);
			report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
			report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
			report.addParamDefault("tim", "tim", 0,tim,  false);
			request.getSession().setAttribute("report", report);
			return prepareReport(request, response, report);
			}else
			report = new Report("Further Requirement Bancassurance tim 2",props.getProperty("report.uw.reportFurtherRBanca"),Report.PDF, null);
			report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
			report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
			report.addParamDefault("tim", "tim", 0,tim,  false);
			request.getSession().setAttribute("report", report);
			return prepareReport(request, response, report);
		}
		return new ModelAndView("report/reportFurtherRBanca",map);
	}
	
	public ModelAndView listFutherRBanca(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lus_id=ServletRequestUtils.getStringParameter(request, "lus_id");
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		String tim=ServletRequestUtils.getStringParameter(request, "tim");
		if(tim.equals("1")){
			report = new Report("Further Requirement Bancassurance Tim 1",props.getProperty("report.uw.reportFurtherRBancaTim1"),Report.PDF, null);
			report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
			report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
//			report.addParamDefault("tim", "tim", 0,tim,  false);
			report.setFlagEmailCab(true); //u/ pengiriman email ke cabang
			report.setFlagEC(true);
		}else
		report = new Report("Further Requirement Bancassurance",props.getProperty("report.uw.reportFurtherRBanca"),Report.PDF, null);

		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		report.addParamDefault("tim", "tim", 0,tim,  false);
		report.setFlagEmailCab(true); //u/ pengiriman email ke cabang
		report.setFlagEC(true);

		return prepareReport(request, response, report);
	}
	
	public ModelAndView report_CetakPolisCabangBas(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		Map param= new HashMap();
		List lsRegion=new ArrayList();
		List lsAdmin=new ArrayList();
		List lsAgency = new ArrayList();
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());		
		String All=ServletRequestUtils.getStringParameter(request, "All");
		String lsrg = ServletRequestUtils.getStringParameter(request, "lsrg");
		Integer lock=0;
		Integer flag=ServletRequestUtils.getIntParameter(request, "l");
		if(flag==null)
		param.put("lock",lock);
		param.put("lsRegion", lsRegion);
		param.put("lsAdmin", lsAdmin);
		param.put("lca_id",lca_id);
		param.put("tglAwal",tglAwal);
		param.put("tglAkhir",tglAkhir);
		param.put("lsAgency", lsAgency);
//		param.put("tgl", elionsManager.selectSysdate());
		param.put("tgl", new Date());
	
		if (request.getParameter("Cari")!=null){
			if(lca_id.equals("0")){//all cabang
				report = new Report("Laporan Cetak POlis Cabang Bas",props.getProperty("report.bas.cetakAllPolisCabangBas"),Report.PDF, null);
				report.addParamDefault("tglAwal", "tanggalAwal", 0,tglAwal,  false);
				report.addParamDefault("tglAkhir", "tanggalAkhir", 0,tglAkhir,  false);
				report.addParamDefault("lca_id", "lca_id", 0,lca_id, false);
				request.getSession().setAttribute("report", report);
				return prepareReport(request, response, report);
			}else//per cabang
				report = new Report("Laporan Cetak POlis Cabang",props.getProperty("report.bas.cetakPolisCabang"),Report.PDF, null);
		
				report.addParamDefault("tglAwal", "tanggalAwal", 0,tglAwal,  false);
				report.addParamDefault("tglAkhir", "tanggalAkhir", 0,tglAkhir,  false);
				report.addParamDefault("lca_id", "lca_id", 0,lca_id, false);	
				request.getSession().setAttribute("report", report);
				return prepareReport(request, response, report);
		}
	
		return new ModelAndView("report/report_CetakPolisCabangBas",param);
	}
	public ModelAndView cetakPolisCabangBas(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map param= new HashMap();
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		String lsrg = ServletRequestUtils.getStringParameter(request, "lsrg");
		param.put("tglAwal",tglAwal);
		param.put("tglAkhir", tglAkhir);
		param.put("lca_id",lca_id);	
		Report report;
		report = new Report("Laporan Cetak POlis Cabang Untuk Bas",props.getProperty("report.bas.cetakPolisCabangBas"),Report.PDF, null);
		report.addParamDefault("tglAwal", "tanggalAwal", 0,tglAwal,  false);
		report.addParamDefault("tglAkhir", "tanggalAkhir", 0,tglAkhir,  false);
		report.addParamDefault("lca_id", "lca_id", 0,lca_id, false);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
		
	}
	
	/**Fungsi : Untuk Menampilkan report U/W Reinstatement Work Sheet
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author Ferry Harlim
	 * @Date 04/02/2008
	 */
	public ModelAndView reportReinstatementWorkSheet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		String nospaj=ServletRequestUtils.getStringParameter(request, "nospaj");
		String businessId = FormatString.rpad("0", uwManager.selectBusinessId(nospaj), 3);
		if (products.unitLink(businessId)){
			report = new Report("Report Uw Reinstatement Work Sheet Link", props.getProperty("report.uw.report_reinstatement_worksheetlink"), Report.PDF, null);//link
			}else {
				report = new Report("Report Uw Reinstatement Work Sheet Non Link", props.getProperty("report.uw.report_reinstatement_work_sheet"), Report.PDF, null);//non link
			}
		report.addParamDefault("nospaj", "nospaj", 0,nospaj,  false);
		//medis pemegang dan tertanggung
		String medis_pp="",medis_tt="";
		String hasilPp="",hasilTt="";
		Double sar=0.0,sarPp=0.0,sarTt=0.0;
		Integer lstbId=1;
		Policy policy=elionsManager.selectDw1Underwriting(nospaj, null, lstbId);
		Map tertanggung=elionsManager.selectTertanggung(nospaj);
		Datausulan dataUsulan=elionsManager.selectDataUsulanutama(nospaj);
		Date ldtBill=elionsManager.selectmaxMsbiEndDateMstBilling(nospaj);
		Integer umurTt,umurPp,tahunKe,lsbsId,lsdbsNumber,pctPremi;
		//tanggal pembayaran masukin dari JSP aja.
//		Date ldtBayar=elionsManager.selectSysdate();
		Date ldtBayar=new Date();
		
		//Yusuf (7 Apr 2011) - Untuk umur pp dan tt, bukan dihitung dari umur PP ditambah tahunKe
		//melainkan dihitung ulang tgl lahir pp/tt dibandingkan sysdate
		Date birthPP = uwManager.selectTanggalLahirClient(policy.getMspo_policy_holder());
		Date birthTT = (Date) tertanggung.get("MSPE_DATE_BIRTH");
		tahunKe= FHit.getTahunKe(ldtBayar, dataUsulan.getMste_beg_date(), policy.getMspo_pay_period());
		//umurTt=(Integer) tertanggung.get("MSTE_AGE")+ tahunKe;
		//umurPp=policy.getMspo_age()+ tahunKe;

		f_hit_umur umr= new f_hit_umur();
		Integer tahun1,tahun2, bulan1, bulan2, tanggal1, tanggal2;
		tanggal1 = ldtBayar.getDate();
		bulan1 = (ldtBayar.getMonth())+1;
		tahun1 = (ldtBayar.getYear())+1900;
		
		//tgl lahir PP
		tanggal2 = birthPP.getDate();
		bulan2=(birthPP.getMonth())+1;
		tahun2=(birthPP.getYear())+1900;
		umurPp = umr.umur(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);
		//tgl lahir PP
		tanggal2 = birthTT.getDate();
		bulan2=(birthTT.getMonth())+1;
		tahun2=(birthTT.getYear())+1900;
		umurTt = umr.umur(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);
		
		lsbsId=dataUsulan.getLsbs_id();
		lsdbsNumber=dataUsulan.getLsdbs_number();
		String lamaLapse=ServletRequestUtils.getStringParameter(request, "lamaLapse","0");
		String tglBayar=ServletRequestUtils.getStringParameter(request, "tglBayar",null);
		
		sar=dataUsulan.getMspr_tsi();
		if ("045, 053, 054, 073, 095, 105, 106, 111, 123, 124, 158".indexOf(FormatString.rpad("0", String.valueOf(lsbsId), 3)) > 0)
			sar=0.0;
		
		if (lsbsId == 167){
			Integer sarId[]={240, 200, 180, 160, 100};
			sar=sar*sarId[lsdbsNumber]/100;			
		}else if (lsbsId == 172){
			sarPp=sar;
			sar/=2;
		}else if (lsbsId == 173){
			sar=0.0;
			sarPp=dataUsulan.getMspr_tsi();
			if (lsdbsNumber>1){
				sar=dataUsulan.getMspr_tsi()*2;
				if (dataUsulan.getLku_id()=="02")
					sar=dataUsulan.getMspr_tsi()*3.5;
				sarPp=sar;
				sar=0.0;
			}				
		}	
		/*
		of_set_rider()
		*/
		sarTt=sar;
		pctPremi=1;
		
			
		if (tahunKe >= 5 && "074, 076, 096, 099, 135, 136".indexOf(FormatString.rpad("0", String.valueOf(lsbsId), 3)) > 0){
			medis_pp="";
			medis_tt="";
		}else{
			medis_pp=cekMedis(medis_pp, sar, dataUsulan.getLku_id(), umurPp);
			if (medis_pp != ""){
				if (Integer.parseInt(lamaLapse) < 90){
					medis_pp="NM";
				}
			}
			medis_tt=cekMedis(medis_tt, sar, dataUsulan.getLku_id(), umurTt);
			if (medis_tt != ""){
				if (Integer.parseInt(lamaLapse) < 90){
					medis_tt="NM";
				}
			}			
		}
		
		//export to ebserver (req mas echo LB dan mba arin UW)-ryan
		File dir = new File(
				props.getProperty("pdf.dir.export") + "\\" +
				elionsManager.selectCabangFromSpaj(nospaj) + "\\" +
				nospaj);
		if(!dir.exists()) {
			dir.mkdirs();
		}
	    File fileDir = new File(report.getReportPath());
	    String dest = dir +"\\";
	    String no_inbox=uwManager.selectIDinbox(nospaj);
	    String dest2=dir +"\\"+"REINS_"+no_inbox+"\\";
	    File outputFile = new File(dest);
	    File outputFile2 = new File(dest2);
		Map<String, Comparable> params = new HashMap<String, Comparable>();
		params.put("nospaj", nospaj);
		params.put("lamaLapse", lamaLapse);
		params.put("tglBayar", tglBayar);
		params.put("medis_pp", medis_pp);
		params.put("medis_tt", medis_tt);
		
		Connection conn = null;
		try {
			//conn = this.getDataSource().getConnection();
			conn = this.getUwManager().getUwDao().getDataSource().getConnection();
			JasperUtils.exportReportToPdf(
					report.getReportPath()+".jasper", 
					dest, 
					nospaj+"WORKSHEET.pdf", 
					params, 
					conn, 
					PdfWriter.AllowPrinting, null, null);
			
			JasperUtils.exportReportToPdf(
					report.getReportPath()+".jasper", 
					dest2, 
					nospaj+"WORKSHEET.pdf", 
					params, 
					conn, 
					PdfWriter.AllowPrinting, null, null);
		}catch(Exception e){
            throw e;
		}finally{
			closeConnection(conn);
		}
		
		report.addParamDefault("lamaLapse", "lamaLapse", 0,lamaLapse,  false);
		report.addParamDefault("tglBayar", "tglBayar", 0,tglBayar,  false);
		report.addParamDefault("medis_pp", "medis_pp", 0,medis_pp,  false);
		report.addParamDefault("medis_tt", "medis_tt", 0,medis_tt,  false);
		return prepareReport(request, response, report);
	}	
	
	
	/**Fungsi : Untuk Menampilkan report Worksheet UW Renewal
	 * 
	 * @author Randy
	 * @Date 12 May 2016
	 */
	public ModelAndView reportWorksheetUwRenewal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String nospaj=ServletRequestUtils.getStringParameter(request, "nospaj","");
		String area1=ServletRequestUtils.getStringParameter(request, "a1","");
		String area2=ServletRequestUtils.getStringParameter(request, "a2","");		
		String area3=ServletRequestUtils.getStringParameter(request, "a3","");
		String area5=ServletRequestUtils.getStringParameter(request, "a5","");
		String namatt=ServletRequestUtils.getStringParameter(request, "a6","");
		String area4 = FormatNumber.convertToTwoDigit(new BigDecimal(ServletRequestUtils.getDoubleParameter(request, "a4", 0)));//premi
		String prod=ServletRequestUtils.getStringParameter(request, "p1","");

		report = new Report("Report Worksheet UW Renewal", props.getProperty("report.uw.worksheet_uw_renewal"), Report.PDF, null);	
		report.addParamDefault("User", "user_id", 37, currentUser.getName(), true);
		report.addParamDefault("nospaj", "nospaj", 0, nospaj,  false);
		report.addParamDefault("area1", "area1", 0, area1,  false);
		report.addParamDefault("area2", "area2", 0, area2,  false);
		report.addParamDefault("area3", "area3", 0, area3,  false);
		report.addParamDefault("area4", "area4", 0, area4,  false);
		report.addParamDefault("area5", "area5", 0, area5,  false);
		report.addParamDefault("namatt", "namatt", 0, namatt,  false);
		report.addParamDefault("prod", "prod", 0, prod,  false);
		
		//export to ebserver
		File dir = new File(props.getProperty("pdf.dir.export") + "\\" + elionsManager.selectCabangFromSpaj(nospaj) + "\\" + nospaj);
		if(!dir.exists()) {
			dir.mkdirs();
		}
//	    File fileDir = new File(report.getReportPath());
	    String dest = dir +"\\";
//	    File outputFile = new File(dest);
	    
		Map<String, Comparable> params = new HashMap<String, Comparable>();
		params.put("nospaj", nospaj);
		params.put("area1", area1);
		params.put("area2", area2);
		params.put("area3", area3);
		params.put("area4", area4);
		params.put("area5", area5);
		params.put("prod", prod);
		params.put("namatt", namatt);
		params.put("user_id", currentUser.getName());
		List<Map> data = new ArrayList<Map>();	
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//		Date sysdate = elionsManager.selectSysdate();
		Date nowDate = new Date();
		String nama_file = "Worksheet_UW_Renewal_"+df.format(nowDate)+".pdf";
	
		Connection conn = null;
		try {
			//conn = this.getDataSource().getConnection();
			conn = this.getUwManager().getUwDao().getDataSource().getConnection();
			JasperUtils.exportReportToPdf(report.getReportPath()+".jasper", dest, nama_file, params, conn, PdfWriter.AllowPrinting, null, null);
		}catch(Exception e){
            throw e;
		}finally{
			closeConnection(conn);
		}

		HashMap param = new HashMap();
		param.put("err", "PEMBUATAN WORKSHEET GAGAL.");
		
		File file = new File(dest+"\\"+nama_file);
		if (!file.exists()){
			logger.info("===== FILE TIDAK ADA =====");
			return reportWorksheetUwRenewal(request, response);
		}else{
			logger.info("===== FILE ADA ======");
			param.put("err", "PEMBUATAN WORKSHEET BERHASIL.");
		}
//		return prepareReport(request, response, report);
//		return new ModelAndView(new RedirectView(request.getContextPath()
//				+ "/uw/view.htm?p=v&showSPAJ="+nospaj));
		return new ModelAndView("akseptasi_ssh/worksheet_uw_renewal",param);
	}
	
	/**Fungsi : Untuk Menampilkan Hasil Akseptasi Renewal
	 * 
	 * @author Randy
	 * @Date 25 May 2016
	 */
	public ModelAndView printSuratRenewal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection conn = null;
		
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String jenis = ServletRequestUtils.getStringParameter(request, "a1"); //jenis aksep
		String nospaj = ServletRequestUtils.getStringParameter(request, "nospaj");
        String subject = ""; 
        String msps_desc = "";
        report = null;
        String nama_file = "";
        String dest = "";
        List<Map> data = new ArrayList<Map>();	
        
		data = uwManager.selectUwRenewal(nospaj);
		
        Map x = data.get(0);
        String no_polis = x.get("NO_POLIS").toString();
        String nama_pp = x.get("NAMA_PP").toString();

		// jenis 5 (accepted) dan 2 (borderline) tidak ada report. hanya record di uw info.
		// jenis 4 (extra premi) report terbagi stand-alone (PDF) & rider ()
		// stand alone terbagi Cara bayar autodebet (PDF) & transfer (PDF)
		// rider terbagi rider non-topup (PDF) & rider topup (XLS)
        
		if(jenis.equals("5")){//renewal accepted
			subject = "AKSEPTASI RENEWAL ACCEPTED";
			msps_desc = "RENEWAL ACCEPTED";
			
		}else if(jenis.equals("2")){//renewal borderline standart
			subject = "AKSEPTASI RENEWAL BORDERLINE STANDARD";
			msps_desc = "RENEWAL BORDERLINE STANDARD";
			
		}else{
			String  ket = ServletRequestUtils.getStringParameter(request, "a2","0"); //keterangan
			String  kode = ServletRequestUtils.getStringParameter(request, "p1","");//produk
			String  ketx = ServletRequestUtils.getStringParameter(request, "a4","");//premi ulink
			String  cara = ServletRequestUtils.getStringParameter(request, "c1","");//cara bayar
//			String  nmr = ServletRequestUtils.getStringParameter(request, "nm","");//nmr surat
			List<Map> ax = new ArrayList<Map>();	
			Integer xax = 0;
			ax = bacManager.selectProdRenewal(nospaj, kode);
	        Map xy = ax.get(0);
	        String prod = xy.get("PROD").toString();
			Integer lsbs = Integer.parseInt(xy.get("LSBS_ID").toString());

			if(ket.equals(null)||ket.equals("")||ket.equals("0")){
				xax = 1;
			}else{
				ket = FormatNumber.convertToTwoDigit(new BigDecimal(ServletRequestUtils.getDoubleParameter(request, "a2", 0))); //keterangan EXTRA PREMI
			}
			ketx = FormatNumber.convertToTwoDigit(new BigDecimal(ServletRequestUtils.getDoubleParameter(request, "a4", 0)));
			
//			String no_s = ServletRequestUtils.getStringParameter(request, "a3",""); //nosurat
			String no_s = ServletRequestUtils.getStringParameter(request, "nm",""); //nosurat
			Integer okd = 1;	
			
			if (xax == 1){
				if(lsbs < 300){
					if(cara.equals("auto")){//report cara bayar AUTODEBET (PDF) (STAND ALONE)
						report = new Report("Report Worksheet UW Renewal Extra", props.getProperty("report.uw.renewal_accepted_uw"), Report.PDF, null);
					}else{//report cara bayar TRANSFER (PDF) (STAND ALONE)
						report = new Report("Report Worksheet UW Renewal Extra", props.getProperty("report.uw.renewal_accepted"), Report.PDF, null);
					}
					okd=0;
				}else{//report NON TOP UP (PDF) (RIDER)
					report = new Report("Report Worksheet UW Renewal Extra", props.getProperty("report.uw.renewal_decline"), Report.PDF, null);
					okd=0;
				}
			}else{//report TOP UP (XLS) (RIDER - TOP UP)
				report = new Report("Report Worksheet UW Renewal Extra", props.getProperty("report.uw.renewal_extra"), Report.PDF, null);		
				okd=1;
			}
			report.addParamDefault("User", "user_id", 37, currentUser.getName(), true);
			report.addParamDefault("nospaj", "nospaj", 0, nospaj,  false);
			report.addParamDefault("no_s", "no_s", 0, no_s,  false);
			report.addParamDefault("ket", "ket", 0, ket,  false);
			report.addParamDefault("ketx", "ketx", 0, ketx,  false);
			report.addParamDefault("prod", "prod", 0, prod,  false);
			
			File dir = new File(props.getProperty("pdf.dir.export") + "\\" + elionsManager.selectCabangFromSpaj(nospaj) + "\\" + nospaj);
			if(!dir.exists()) {dir.mkdirs();}
			File fileDir = new File(report.getReportPath());
			dest = dir +"\\";
			File outputFile = new File(dest);
				Map<String, Comparable> params = new HashMap<String, Comparable>();
				params.put("nospaj", nospaj);
				params.put("no_s", no_s);
				params.put("ket", ket);
				params.put("ketx", ketx);
				params.put("prod", prod);
			if (okd==0) {
				subject = "AKSEPTASI RENEWAL EXTRA PREMI";
				nama_file = "Renewal an "+nama_pp+" "+no_polis+".pdf";
				
				try {
					//conn = this.getDataSource().getConnection();
					conn = this.getUwManager().getUwDao().getDataSource().getConnection();
					JasperUtils.exportReportToPdf(report.getReportPath()+".jasper", dest, nama_file, params, conn, PdfWriter.AllowPrinting, null, null);
				}catch(Exception e){
		            throw e;
				}finally{
					closeConnection(conn);
				}
				
			}else{
				subject = "AKSEPTASI RENEWAL EXTRA PREMI";
				if(nama_pp.contains("/")){
					nama_pp = nama_pp.replace("/", " atau ");
				}
				nama_file = "Renewal an "+nama_pp+" "+no_polis+".xls";
				try {
					//conn = this.getDataSource().getConnection();
					conn = this.getUwManager().getUwDao().getDataSource().getConnection();
					JasperUtils.exportReportToXls(report.getReportPath()+".jasper", dest, nama_file, params, conn);
				}finally{
					closeConnection(conn);
				}
			}
			
			String  xtrapremi = ServletRequestUtils.getStringParameter(request, "a7","");//xtra premi viewer
			String  kettambah = ServletRequestUtils.getStringParameter(request, "a6","");//keterangan tambahan
			if(xtrapremi.equals("")){
				msps_desc = "RENEWAL EXTRA PREMI";
			}else{
				if(kettambah.equals("")){
					msps_desc = "RENEWAL EXTRA PREMI: "+xtrapremi+"%";
				}else{
					msps_desc = "RENEWAL EXTRA PREMI: "+xtrapremi+"%, "+kettambah.toUpperCase();
				}
			}
		}
		int proses = bacManager.prosesUwRenewal(currentUser,nospaj,dest,nama_file,subject,jenis,msps_desc);
		HashMap param = new HashMap();
		
		
		if (proses==1){
			return printSuratRenewal(request, response);
		}else if(proses==0){
			param.put("err", "PEMBUATAN SURAT AKSEPTASI GAGAL.");
			return null;
		}else{
//			return prepareReport(request, response, report);
//			return new ModelAndView(new RedirectView(request.getContextPath()
//					+ "/uw/view.htm?p=v&showSPAJ="+nospaj));
			param.put("err", "PEMBUATAN SURAT AKSEPTASI BERHASIL, DAN EMAIL TELAH TERKIRIM KE LIFE BENEFIT.");
			return new ModelAndView("akseptasi_ssh/akseptasi_renewal",param);
		}
	}
	
	
	private String cekMedis(String hasil, Double sar, String kurs, Integer umur) {
		Integer sarId, ageId;
		List<String> hasil2;

		sarId=elionsManager.selectLstMedicalRangeSar(sar, kurs);
		if( sarId == null )
			return "";
		ageId=elionsManager.selectLstMedicalRangeAge(umur);
		if( ageId == null )
			return "";
		hasil2=elionsManager.selectLstJenisMedical(ageId, sarId, kurs);
		for (String a : hasil2) {
			hasil += a;
		}
		return hasil;
	}
	
//	private Integer riderBaru setRider(Integer riderBaru[], Integer lsbsId, Integer lsdbsNumber) {
//
//		return riderBaru;
//	}	

	/**Fungsi : Untuk Menampilkan Surat Konfirmasi Pemulihan Polis
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author Ferry Harlim
	 * @Date 19/02/2008
	 */
	public ModelAndView suratKonfirmasiPemulihanPolis(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Report report;
		String nomor=request.getParameter("nomor");
		String getPath="";
		String pappt ="";
		String flag=ServletRequestUtils.getStringParameter(request, "flag","0");
		String spaj=nomor.substring(0,nomor.indexOf("-"));
		String reinsNo=nomor.substring(nomor.indexOf("~")+1,nomor.length());
		Map mReinsate=new HashMap();
		Integer liAccept = null,liReas,liPrint;
		Date ldtTgl;
		boolean pro=true;
		int info=0;
		mReinsate=(HashMap)elionsManager.selectUwReinstate(spaj,reinsNo);
		if(mReinsate!=null){
			liAccept=(Integer)mReinsate.get("MSUR_ACCEPT");
			liReas=(Integer)mReinsate.get("LSPD_ID");
			liPrint=(Integer)mReinsate.get("MSUR_PRINT");
			ldtTgl=(Date)mReinsate.get("MSUR_TGL_BATAS_PAID");
			Date tgl_spaj_doc = (Date) mReinsate.get("TGL_BERKAS_LENGKAP_UW");
		}
		
		int lsgb_id = uwManager.selectIsPolisUnitlink(spaj);
		
		//Yusuf (23 Jun 2011) - Req Mas Eko LB, bila unit link ada perbedaan di ASUMSI PERHITUNGAN PREMI nya, tidak ada bunga tunggakan
		if(lsgb_id == 17){
			
			if(liAccept==0){
				report = new Report("Surat Konfirmasi Pemulihan Polis", props.getProperty("report.uw.surat_konfirmasi_pemulihan_ditolak_polis"), Report.PDF, null);
				getPath=props.getProperty("report.uw.surat_konfirmasi_pemulihan_ditolak_polis")+".jasper";
			}else{
				report = new Report("Surat Konfirmasi Pemulihan Polis", props.getProperty("report.uw.surat_konfirmasi_pemulihan_polis.link"), Report.PDF, null);
				getPath=props.getProperty("report.uw.surat_konfirmasi_pemulihan_polis.link")+".jasper";
			}
			pappt = uwManager.getPeriodeAkhirPremiPokokTertunggak(spaj);
			report.addParamDefault("pappt", "pappt", 0, pappt, false);
		}else{			
			if(liAccept==0){
				report = new Report("Surat Konfirmasi Pemulihan Polis", props.getProperty("report.uw.surat_konfirmasi_pemulihan_ditolak_polis"), Report.PDF, null);
				getPath=props.getProperty("report.uw.surat_konfirmasi_pemulihan_ditolak_polis")+".jasper";
			}else{
				report = new Report("Surat Konfirmasi Pemulihan Polis", props.getProperty("report.uw.surat_konfirmasi_pemulihan_polis"), Report.PDF, null);
				getPath=props.getProperty("report.uw.surat_konfirmasi_pemulihan_polis")+".jasper";
			}
					
		}
		
		report.addParamDefault("nospaj", "nospaj", 0,spaj,  false);
		report.addParamDefault("reinsno", "reinsno", 0, reinsNo, false);
		
		//Yusuf (16/03/2011) - Request Numan, biaya materai tertunggak sekarang bukan dari billing, melainkan diinput oleh mas Eko
		//Double stamp=elionsManager.selectBiayaMateraiFromBilling(spaj);
		Double stamp=uwManager.selectBiayaMateraiTertunggak(reinsNo);
		if(stamp==null)
			stamp=0.0;
		
		Integer li_print=elionsManager.selectMstUwReinstateMsurPrint(spaj,reinsNo);
		User currentUser = (User) request.getSession().getAttribute("currentUser");        
		if(li_print==1 && flag.equals("0")){
			Map map=new HashMap();
			map.put("spaj",spaj);
			map.put("reins",reinsNo);
			map.put("info", 1);
			return new ModelAndView("uw_reinstate/surat","cmd",map);
		}else {
			elionsManager.prosesPrintSuratKonfirmasiPemulihanPolis(spaj,reinsNo,currentUser.getLus_id(),new Integer(1),currentUser);
			flag="1";
			report.addParamDefault("stamp", "stamp", 0, stamp.toString(),  false);
			report.addParamDefault("nomor", "nomor", 0,nomor,  false);
			report.addParamDefault("flag", "flag", 0,flag,  false);
			String no_inbox=uwManager.selectIDinbox(spaj);
				File dir = new File(
						props.getProperty("pdf.dir.export") + "\\" +
						elionsManager.selectCabangFromSpaj(spaj) + "\\" +
						spaj.trim());
				if(!dir.exists()) {
					dir.mkdirs();
				}
			    File fileDir = new File(report.getReportPath());
			    String dest = dir +"\\";
			    String dest2=dir +"\\"+"REINS_"+no_inbox+"\\";
			    File outputFile = new File(dest);
			    File outputFile2 = new File(dest2);
			    Map<String, Comparable> params = new HashMap<String, Comparable>();
				params.put("nospaj", spaj);
				params.put("reinsNo", reinsNo);
				params.put("pappt", pappt);			
			    List<Map> reportSummary = uwManager.selectReportSuratReins(spaj,reinsNo);
				
			  JasperUtils.exportReportToPdf(getPath, dest, spaj.trim()+"REINS.pdf", params, reportSummary, PdfWriter.AllowPrinting, null, null);
			  JasperUtils.exportReportToPdf(getPath, dest2, spaj.trim()+"REINS.pdf", params, reportSummary, PdfWriter.AllowPrinting, null, null);
				
		}
		return prepareReport(request, response, report);
	}	
	

	public ModelAndView report_PremiNonCash(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map param= new HashMap();
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");

		report = new Report("Report Premi Non Cash", props.getProperty("report.uw.report_PremiNonCash"), Report.PDF, null);
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView report_black_list(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map param= new HashMap();
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");

		report = new Report("Report Attention List", props.getProperty("report.uw.report_black_list"), Report.PDF, null);
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView report_pas(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map param= new HashMap();
		String liniBisnis=ServletRequestUtils.getStringParameter(request, "liniBisnis");
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");

		report = new Report("Report Pas", props.getProperty("report.uw.report_pas"), Report.PDF, null);
		report.addParamDefault("liniBisnis", "liniBisnis", 0,liniBisnis,  false);
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView report_pas_bp(HttpServletRequest request, HttpServletResponse response) throws Exception{
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		
		Report report;
		Map<Object, Object> mplist = null;
		List<Map> plist = new ArrayList<Map>();
		mplist = new HashMap<Object, Object>(); 
		mplist.put("KEY", "");
		mplist.put("VALUE", "ALL");
		plist.add(mplist);
		mplist = new HashMap<Object, Object>(); 
		mplist.put("KEY", "1");
		mplist.put("VALUE", "Ada");
		plist.add(mplist);
		mplist = new HashMap<Object, Object>(); 
		mplist.put("KEY", "0");
		mplist.put("VALUE", "Tidak");
		plist.add(mplist);
		report = new Report("Report Pas BP", props.getProperty("report.uw.report_pas_bp"), Report.PDF, null);
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		//report.addParamText("User", "name", 15, currentUser.getName(), false);
		report.addParamText("User", "name", 15, "", false);
		report.addParamSelectWithoutAll("Polis", "epolis", plist, null, true);
		//report.addParamDefault("lus_login_name", "lus_login_name", 15, currentUser.getName(), false);
		report.addParamDate("Periode PAS BP", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		
		return prepareReport(request, response, report);
		
		//Map param= new HashMap();
		//String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		//String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");

//		report = new Report("Report Pas", props.getProperty("report.uw.report_pas_bp"), Report.PDF, null);
//		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
//		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
//		request.getSession().setAttribute("report", report);
//		return prepareReport(request, response, report);
	}
	
	public ModelAndView report_dbd_bp(HttpServletRequest request, HttpServletResponse response) throws Exception{
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		
		Report report;
		Map<Object, Object> mplist = null;
		List<Map> plist = new ArrayList<Map>();
		mplist = new HashMap<Object, Object>(); 
		mplist.put("KEY", "");
		mplist.put("VALUE", "ALL");
		plist.add(mplist);
		mplist = new HashMap<Object, Object>(); 
		mplist.put("KEY", "1");
		mplist.put("VALUE", "Ada");
		plist.add(mplist);
		mplist = new HashMap<Object, Object>(); 
		mplist.put("KEY", "0");
		mplist.put("VALUE", "Tidak");
		plist.add(mplist);
		report = new Report("Report DBD BP", props.getProperty("report.uw.report_dbd_bp"), Report.PDF, null);
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		//report.addParamText("User", "name", 15, currentUser.getName(), false);
		report.addParamText("User", "name", 15, "", false);
		report.addParamSelectWithoutAll("Polis", "epolis", plist, null, true);
		//report.addParamDefault("lus_login_name", "lus_login_name", 15, currentUser.getName(), false);
		report.addParamDate("Periode DBD BP", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		
		return prepareReport(request, response, report);
		
		//Map param= new HashMap();
		//String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		//String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");

//		report = new Report("Report Pas", props.getProperty("report.uw.report_pas_bp"), Report.PDF, null);
//		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
//		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
//		request.getSession().setAttribute("report", report);
//		return prepareReport(request, response, report);
	}
	
	public ModelAndView report_dbd_agency(HttpServletRequest request, HttpServletResponse response) throws Exception{
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		
		Report report;
		Map<Object, Object> mplist = null;
		List<Map> plist = new ArrayList<Map>();
		mplist = new HashMap<Object, Object>(); 
		mplist.put("KEY", "");
		mplist.put("VALUE", "ALL");
		plist.add(mplist);
		mplist = new HashMap<Object, Object>(); 
		mplist.put("KEY", "1");
		mplist.put("VALUE", "Ada");
		plist.add(mplist);
		mplist = new HashMap<Object, Object>(); 
		mplist.put("KEY", "0");
		mplist.put("VALUE", "Tidak");
		plist.add(mplist);
		report = new Report("Report DBD Agency", props.getProperty("report.uw.report_dbd_agency"), Report.PDF, null);
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		//report.addParamText("User", "name", 15, currentUser.getName(), false);
		report.addParamText("User", "name", 15, "", false);
		report.addParamSelectWithoutAll("Polis", "epolis", plist, null, true);
		//report.addParamDefault("lus_login_name", "lus_login_name", 15, currentUser.getName(), false);
		report.addParamDate("Periode DBD Agency", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		
		return prepareReport(request, response, report);
		
	}
	
	
	public ModelAndView auto_debet(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map param= new HashMap();
		String spaj=ServletRequestUtils.getStringParameter(request, "spaj");

		report = new Report("Report History Auto Debet", props.getProperty("report.uw.report_auto_debet"), Report.PDF, null);
		report.addParamDefault("spaj", "spaj", 0,spaj,  false);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView report_rekonsiliasi(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map param= new HashMap();
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
//		Date tanggal = elionsManager.selectSysdateSimple();
		
		String sysdate = defaultDateFormat.format(new Date());
		User currentUser = (User) request.getSession().getAttribute("currentUser");

		report = new Report("Report Rekonsiliasi", props.getProperty("report.uw.report_rekonsiliasi"), Report.PDF, null);
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		report.addParamDefault("user", "user", 0,currentUser.getName(),  false);
		report.addParamDefault("harini", "harini", 0,sysdate,  false);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView ttp_blm_dikembalikan_tgl(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map param= new HashMap();
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");

		report = new Report("Laporan TTP Belum Dikembalikan Per Tanggal", props.getProperty("report.uw.ttp_blm_dikembalikan_tgl"), Report.PDF, null);
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	public ModelAndView ttp_blm_dikembalikan_cab(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map param= new HashMap();
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id");
		param.put("lca_id",lca_id);	
		Report report;
		report = new Report("Laporan TTP Belum Dikembalikan Per Cabang",props.getProperty("report.uw.ttp_blm_dikembalikan_cab"),Report.PDF, null);
		
		report.addParamDefault("lca_id", "lca_id", 0,lca_id, false);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
		
	}
	/* diganti : /E-Lions/common/util.htm?window=data_nasabah
	public ModelAndView data_polis_sinarmas_sekuritas(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map param= new HashMap();
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");

		
		report = new Report("Report Data Nasabah", props.getProperty("report.uw.data_polis_sinarmas_sekuritas"), Report.PDF, null);
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	*/
	
	public ModelAndView report_print_medis(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		String nospaj=ServletRequestUtils.getStringParameter(request, "nospaj");

		report = new Report("Report Premi Non Cash", props.getProperty("report.uw.print_medis"), Report.PDF, null);
		report.addParamDefault("nospaj", "nospaj", 0,nospaj,  false);
		return prepareReport(request, response, report);		
	}
	
	public ModelAndView list_summary_komisi(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		String monthFrom=ServletRequestUtils.getStringParameter(request, "monthFrom");
		String yearFrom=ServletRequestUtils.getStringParameter(request, "yearFrom");
		String periode = yearFrom + monthFrom;
		List<Map<String, String>> temp = uwManager.selectListSummaryKomisiForPrint(periode, null);
		Double total_nominal = new Double("0.0");
		Double total_pajak = new Double("0.0");
		if( temp != null ){
			for ( int i = 0 ; i < temp.size() ; i ++ ){
				if( temp.get(i).get("JUMLAH_NOMINAL")!= null ){
				Double jmlh_nominal = new Double(temp.get(i).get("JUMLAH_NOMINAL").toString());
				Double jmlh_pajak = new Double(temp.get(i).get("JUMLAH_PAJAK").toString());
				total_nominal = total_nominal + jmlh_nominal;
				total_pajak = total_pajak + jmlh_pajak;
				}
			}
		}
		String format_jumlah_nominal = "Rp.     "+FormatNumber.convertToTwoDigit(new BigDecimal(total_nominal+""));
		String format_jumlah_pajak = "Rp.     "+FormatNumber.convertToTwoDigit(new BigDecimal(total_pajak+""));
		report = new Report("Report Komisi", props.getProperty("report.worksite.komisi"), Report.PDF, null);
		report.addParamDefault("periode", "periode", 0,periode,  false);
		report.addParamDefault("total_nominal", "total_nominal", 0, format_jumlah_nominal,  false);
		report.addParamDefault("total_pajak", "total_pajak", 0,format_jumlah_pajak,  false);
		report.setResultList(uwManager.selectListSummaryKomisiForPrint(periode, null));
		return prepareReport(request, response, report);		
	}
	
	
	public ModelAndView report_filing(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		String tahun = ServletRequestUtils.getStringParameter(request, "tahun","");
		String bulan = ServletRequestUtils.getStringParameter(request, "bulan","");
		
		report = new Report("Report Filing", props.getProperty("report.uw.filing_spaj"), Report.PDF, null);
		report.addParamDefault("tahun", "tahun", 0,tahun,  true);
		report.addParamDefault("bulan", "bulan", 0,bulan,  true);
		
		return prepareReport(request, response, report);
	}
	
	public ModelAndView report_bea_materai(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param= new HashMap();
		
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
	
		param.put("tglAwal",tglAwal);
		param.put("tglAkhir", tglAkhir);
		
		Report report;
		report = new Report("Report Bea Materai",props.getProperty("report.uw.report_bea_materai"),Report.PDF, null);
		report.addParamDefault("tglAwal", "tanggalAwal", 0,tglAwal,  false);
		report.addParamDefault("tglAkhir", "tanggalAkhir", 0,tglAkhir,  false);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);

		
	}
	
	public ModelAndView surat_penawaran(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		Integer counter = 1;
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj");
		String em = ServletRequestUtils.getStringParameter(request, "em");
		String premi = ServletRequestUtils.getStringParameter(request, "premi");
		
		//no surat
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyM");
		List<HashMap> noCounter = uwManager.selectNoSuratPenawaran();
//		String yearMon = sdf.format(elionsManager.selectSysdate());
		String yearMon = sdf.format(new Date());
		if(Integer.parseInt(yearMon.substring(4)) == Integer.parseInt(noCounter.get(1).get("MSCO_VALUE").toString())) {
			counter = Integer.parseInt(noCounter.get(0).get("MSCO_VALUE").toString())+1;
		}
		List<Integer> addCounter = new ArrayList<Integer>();
		addCounter.add(0, counter);
		addCounter.add(1, Integer.parseInt(yearMon.substring(4)));
		addCounter.add(2, Integer.parseInt(yearMon.substring(0,4)));
		
		uwManager.updateNoSuratPenawaran(addCounter);
		
		
		report = new Report("Surat Penawaran", props.getProperty("report.uw.surat_penawaran"), Report.PDF, null);
		report.addParamDefault("no_surat", "no_surat", 0,FormatString.rpad("0", counter.toString(),3)+"/EM_RE/UND/"+FormatNumber.angkaRomawi(yearMon.substring(4))+"/"+yearMon.substring(2,4),  false);
		report.addParamDefault("spaj", "spaj", 0,spaj,  false);
		report.addParamDefault("em", "em", 0,em,  false);
		report.addParamDefault("premi", "premi", 0,premi,  false);
		
		return prepareReport(request, response, report);
		//return null;
	}
	
	public ModelAndView report_bridge(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param= new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		Date sysDate = elionsManager.selectSysdate();
		Date sysDate = new Date();
		
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");

		param.put("tanggalAwal",tglAwal);
		param.put("tanggalAkhir", tglAkhir);
		
		Report report;
		report = new Report("Report Bridge",props.getProperty("report.bridge.bridge_input_date"),Report.PDF, null);
		report.addParamDate("TANGGAL INPUT", "tanggal", true, new Date[] {sysDate, sysDate}, true);
		request.getSession().setAttribute("report", report);
		report.addParamDefault("username", "username", 200, currentUser.getName(), false);
		return prepareReport(request, response, report);
//		return null;
	}
	
	/**
     * Report Claim Analysis
     * E-Lions/report/uw.htm?window=reportclaimAnalysis
     */
    public ModelAndView reportclaimAnalysis(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	int json = ServletRequestUtils.getIntParameter(request, "json", 0);
//    	String print = defaultDateFormat.format(uwManager.selectSysdateTruncated(0));
    	String print = defaultDateFormat.format(new Date());
    	
    	//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		
    		String jenis_report = ServletRequestUtils.getStringParameter(request, "jenis_report");
    		String jenis_polis = ServletRequestUtils.getStringParameter(request, "jenis_polis");
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		String grup_bank = ServletRequestUtils.getStringParameter(request, "grup_bank");
    		String nama_bank = ServletRequestUtils.getStringParameter(request, "nama_bank");
    		String jenis_periode = ServletRequestUtils.getStringParameter(request, "jenis_periode","1");
    		
    		List data = new ArrayList();
    		String report = null;
    		String polis = null;
    		Integer tc = null;
    		String periode = "";
    		
    		if(jenis_periode.equals("1")){
    			periode = "Date Submitted";
    		}else if(jenis_periode.equals("2")){
    			periode = "Date Event";
    		}else if(jenis_periode.equals("3")){
    			periode = "Date Paid";
    		}
    		
    		if(jenis_polis.equals("1")){//All Polis
    			polis = "ALL";
    			grup_bank = "ALL";
    			nama_bank = "ALL";
				if(jenis_report.equals("1")){
					report = "report.claim.selectreportClaimBasedAgeDeath";
					data = uwManager.selectreportClaimBasedAgeDeath(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("2")){
					report = "report.claim.selectreportClaimBasedDurationPolicy";
					data = uwManager.selectreportClaimBasedPolicyDuration(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("3")){
					report = "report.claim.selectreportDetailClaim";
					data = uwManager.selectreportDetailClaim(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("4")){//by branch
					report = "report.claim.selectreportCODByBranch";
					data = uwManager.selectreportCODByBranch(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("5")){//Claim by COD
					report = "report.claim.selectreportClaimByCOD";
					data = uwManager.selectreportClaimByCOD(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
					tc = uwManager.selectreportClaimByCOD_TotalCase(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("6")){//COD Based on Medis
					report = "report.claim.selectreportClaimByMedis";
					List data1 = uwManager.selectreportClaimByMedis(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
					List num_medis = uwManager.selectGetTotalClaimByMedis(bdate, edate, currentUser.getLus_id(), polis, "1", grup_bank, nama_bank, jenis_periode);
					List num_non = uwManager.selectGetTotalClaimByMedis(bdate, edate, currentUser.getLus_id(), polis, "0", grup_bank, nama_bank, jenis_periode);
					int medis = 0;
					int non = 0;
					
					Map md = (HashMap) num_medis.get(0);
					Map nm = (HashMap) num_non.get(0);
					
					Integer md_sum_polis = ((BigDecimal) md.get("TOTAL_POLIS")).intValue();
					Integer md_sum_extra = ((BigDecimal) md.get("EXTRA_PREMI")).intValue();
					
					Integer nm_sum_polis = ((BigDecimal) nm.get("TOTAL_POLIS")).intValue();
					Integer nm_sum_extra = ((BigDecimal) nm.get("EXTRA_PREMI")).intValue();
					
					for(int i=0;i<data1.size();i++){
						Map n = (HashMap) data1.get(i);
						HashMap m = new HashMap();
						if(n.get("MEDICAL").toString().equals("Non Medis")){
							non +=1;
							m.put("MEDICAL", n.get("MEDICAL"));
							m.put("MEDICAL_NMBR", n.get("MEDICAL_NMBR"));
							m.put("TOTAL_POLIS", n.get("TOTAL_POLIS"));
							m.put("TOTAL_POLIS2", nm_sum_polis.toString());
							m.put("EXTRA_PREMI2", nm_sum_extra.toString());
							m.put("EXTRA_PREMI", n.get("EXTRA_PREMI"));
							m.put("ICD", n.get("ICD"));
							m.put("SEBAB", n.get("SEBAB"));
							m.put("SUM_RUPIAH", n.get("SUM_RUPIAH"));
							m.put("SUM_DOLLAR", n.get("SUM_DOLLAR"));
						}else{
							medis +=1;
							m.put("MEDICAL", n.get("MEDICAL"));
							m.put("MEDICAL_NMBR", n.get("MEDICAL_NMBR"));
							m.put("TOTAL_POLIS", n.get("TOTAL_POLIS"));
							m.put("TOTAL_POLIS2", md_sum_polis.toString());
							m.put("EXTRA_PREMI2", md_sum_extra.toString());
							m.put("EXTRA_PREMI", n.get("EXTRA_PREMI"));
							m.put("ICD", n.get("ICD"));
							m.put("SEBAB", n.get("SEBAB"));
							m.put("SUM_RUPIAH", n.get("SUM_RUPIAH"));
							m.put("SUM_DOLLAR", n.get("SUM_DOLLAR"));
						}
						
						data.add(m);
					}
					
					
					if(non==0){
						HashMap m = new HashMap();
						m.put("MEDICAL", "Non Medis");
						m.put("MEDICAL_NMBR", 0);
						m.put("TOTAL_POLIS2", "0");
						m.put("EXTRA_PREMI2", "0");
						m.put("TOTAL_POLIS", new BigDecimal(0));
						m.put("EXTRA_PREMI", new BigDecimal(0));
						m.put("IDC", null);
						m.put("SEBAB", null);
						m.put("SUM_RUPIAH", null);
						m.put("SUM_DOLLAR", null);
						
						data.add(m);
					}
					
					if(medis==0){
						HashMap m = new HashMap();
						m.put("MEDICAL", "Medis");
						m.put("MEDICAL_NMBR", 1);
						m.put("TOTAL_POLIS2", "0");
						m.put("EXTRA_PREMI2", "0");
						m.put("TOTAL_POLIS", new BigDecimal(0));
						m.put("EXTRA_PREMI", new BigDecimal(0));
						m.put("IDC", null);
						m.put("SEBAB", null);
						m.put("SUM_RUPIAH", null);
						m.put("SUM_DOLLAR", null);
						
						data.add(m);
					}
				}else if(jenis_report.equals("7")){//COD Based on Entry Age
					List age =  new ArrayList();
					String[] age1 = {"0 - 5", "6 - 10", "11 - 15", "16 - 20", "21 - 25", "26 - 30", "31 - 35", "36 - 40", "41 - 45", "46 - 50", "51 - 55", "56 - 60", "61 >"};
					for(int z=0;z<age1.length;z++){
						HashMap x = new HashMap();
						
						x.put("CAT", age1[z]);
						
						age.add(x);
					}
					report = "report.claim.selectreportClaimBasedEntryAge";
					List tmp = uwManager.selectreportClaimBasedEntryAge(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
					
					for (int i=0;i<age.size();i++){
						HashMap m = new HashMap();
						Map n = (HashMap) age.get(i);
						
						Integer index = null; 
						for(int j=0;j<tmp.size();j++){
							Map s = (HashMap) tmp.get(j);
							
							if(n.get("CAT").toString().trim().equals(s.get("UMUR").toString().trim())){
								index = j;
							}
						}
						
						if(index==null){
							m.put("UMUR", n.get("CAT"));
							m.put("USIA_0_6", new BigDecimal(0));
							m.put("USIA_7_12", new BigDecimal(0));
							m.put("USIA_2_YR", new BigDecimal(0));
							m.put("USIA_3_YR", new BigDecimal(0));
							m.put("USIA_4_YR", new BigDecimal(0));
							m.put("USIA_5_YR", new BigDecimal(0));
							m.put("USIA_6_YR", new BigDecimal(0));
							m.put("USIA_7_YR", new BigDecimal(0));
							m.put("USIA_8_YR", new BigDecimal(0));
							m.put("USIA_9_YR", new BigDecimal(0));
							m.put("USIA_10_YR", new BigDecimal(0));
							m.put("USIA_10_YR_UP", new BigDecimal(0));
							m.put("USIA_ALL", new BigDecimal(0));
						}else{
							Map dt = (HashMap) tmp.get(index);
							
							m.put("UMUR", dt.get("UMUR"));
							m.put("USIA_0_6", dt.get("USIA_0_6"));
							m.put("USIA_7_12", dt.get("USIA_7_12"));
							m.put("USIA_2_YR", dt.get("USIA_2_YR"));
							m.put("USIA_3_YR", dt.get("USIA_3_YR"));
							m.put("USIA_4_YR", dt.get("USIA_4_YR"));
							m.put("USIA_5_YR", dt.get("USIA_5_YR"));
							m.put("USIA_6_YR", dt.get("USIA_6_YR"));
							m.put("USIA_7_YR", dt.get("USIA_7_YR"));
							m.put("USIA_8_YR", dt.get("USIA_8_YR"));
							m.put("USIA_9_YR", dt.get("USIA_9_YR"));
							m.put("USIA_10_YR", dt.get("USIA_10_YR"));
							m.put("USIA_10_YR_UP", dt.get("USIA_10_YR_UP"));
							m.put("USIA_ALL", dt.get("USIA_ALL"));
						}
						
						data.add(m);
					}
				}else if(jenis_report.equals("8")){//EX-GRATIA Claim
					report = "report.claim.selectreportExGratiaClaim";
					data = uwManager.selectreportExGratiaClaim(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("9")){//By Product
					report = "report.claim.selectreportClaimByProduct";
					data = uwManager.selectreportClaimByProduct(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("10")){//By SA
					List assure =  new ArrayList();
					String[] assure1 = { "up to  10",">10 - 25",">26 - 50",">51 - 100",">101 - 125",">126 - 150",">151 - 200",">201 - 250",">251 - 300",">301 - 350",
										">351 - 400",">401 - 450",">451 - 500",">501 - 600",">601 - 700",">701 - 800",">801 - 900",">901 - 1.000",">1.001 - 1.250",
										">1.251 - 1.500",">1.501 - 1.750",">1.751 - 2.000",">2.001 - 2.500",">2.501 - 3.000",">3.001 - 3.500",">3.501 - 4.000",">4.001 - 4.500",
										">4.501 - 5.000",">5.001 - 6.000",">6.001 - 7.000",">7.001 - 8.000",">8.001 - 9.000",">9.001 - 10.000",">10.000"};
					for(int z=0;z<assure1.length;z++){
						HashMap x = new HashMap();
						
						x.put("CAT", assure1[z]);
						
						assure.add(x);
					}
					report = "report.claim.selectreportClaimBySA";
					List tmp = uwManager.selectreportClaimBySA(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
					
					for (int i=0;i<assure.size();i++){
						HashMap m = new HashMap();
						Map n = (HashMap) assure.get(i);
						
						Integer in = i +1;
						
						Integer index = null; 
						for(int j=0;j<tmp.size();j++){
							Map s = (HashMap) tmp.get(j);
							
							if(in == ((BigDecimal)s.get("URUT")).intValue()){
								index = j;
							}
						}
						
						if(index==null){
							m.put("URUT", new BigDecimal(in));
							m.put("ASSURED", n.get("CAT"));
							m.put("YR0_1", new BigDecimal(0));
							m.put("YR2_5", new BigDecimal(0));
							m.put("YR6_10", new BigDecimal(0));
							m.put("YR11_15", new BigDecimal(0));
							m.put("YR16_20", new BigDecimal(0));
							m.put("YR21_25", new BigDecimal(0));
							m.put("YR26_30", new BigDecimal(0));
							m.put("YR31_35", new BigDecimal(0));
							m.put("YR36_40", new BigDecimal(0));
							m.put("YR41_45", new BigDecimal(0));
							m.put("YR46_50", new BigDecimal(0));
							m.put("YR51_55", new BigDecimal(0));
							m.put("YR56_60", new BigDecimal(0));
							m.put("YR61_65", new BigDecimal(0));
							m.put("YR66_70", new BigDecimal(0));
							m.put("YR71_UP", new BigDecimal(0));
							m.put("YR_ALL", new BigDecimal(0));
						}else{
							Map dt = (HashMap) tmp.get(index);
							
							m.put("URUT", dt.get("URUT"));
							m.put("ASSURED", n.get("CAT"));
							m.put("YR0_1", dt.get("YR0_1"));
							m.put("YR2_5", dt.get("YR2_5"));
							m.put("YR6_10", dt.get("YR6_10"));
							m.put("YR11_15", dt.get("YR11_15"));
							m.put("YR16_20", dt.get("YR16_20"));
							m.put("YR21_25", dt.get("YR21_25"));
							m.put("YR26_30", dt.get("YR26_30"));
							m.put("YR31_35", dt.get("YR31_35"));
							m.put("YR36_40", dt.get("YR36_40"));
							m.put("YR41_45", dt.get("YR41_45"));
							m.put("YR46_50", dt.get("YR46_50"));
							m.put("YR51_55", dt.get("YR51_55"));
							m.put("YR56_60", dt.get("YR56_60"));
							m.put("YR61_65", dt.get("YR61_65"));
							m.put("YR66_70", dt.get("YR66_70"));
							m.put("YR71_UP", dt.get("YR71_UP"));
							m.put("YR_ALL", dt.get("YR_ALL"));
						}
						data.add(m);
					}
				}else if(jenis_report.equals("11")){//By Claim Paid
					List assure =  new ArrayList();
					String[] assure1 = { "up to  10",">10 - 25",">26 - 50",">51 - 100",">101 - 125",">126 - 150",">151 - 200",">201 - 250",">251 - 300",">301 - 350",
							">351 - 400",">401 - 450",">451 - 500",">501 - 600",">601 - 700",">701 - 800",">801 - 900",">901 - 1.000",">1.001 - 1.250",
							">1.251 - 1.500",">1.501 - 1.750",">1.751 - 2.000",">2.001 - 2.500",">2.501 - 3.000",">3.001 - 3.500",">3.501 - 4.000",">4.001 - 4.500",
							">4.501 - 5.000",">5.001 - 6.000",">6.001 - 7.000",">7.001 - 8.000",">8.001 - 9.000",">9.001 - 10.000",">10.000"};
					for(int z=0;z<assure1.length;z++){
						HashMap x = new HashMap();
						
						x.put("CAT", assure1[z]);
						
						assure.add(x);
					}
					report = "report.claim.selectreportClaimByPaid";
					List tmp = uwManager.selectreportClaimByPaid(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
					
					for (int i=0;i<assure.size();i++){
						HashMap m = new HashMap();
						Map n = (HashMap) assure.get(i);
						
						Integer in = i +1;
						
						Integer index = null; 
						for(int j=0;j<tmp.size();j++){
							Map s = (HashMap) tmp.get(j);
							
							if(in == ((BigDecimal)s.get("URUT")).intValue()){
								index = j;
							}
						}
						
						if(index==null){
							m.put("URUT", new BigDecimal(in));
							m.put("ASSURED", n.get("CAT"));
							m.put("YR0_1", new BigDecimal(0));
							m.put("YR2_5", new BigDecimal(0));
							m.put("YR6_10", new BigDecimal(0));
							m.put("YR11_15", new BigDecimal(0));
							m.put("YR16_20", new BigDecimal(0));
							m.put("YR21_25", new BigDecimal(0));
							m.put("YR26_30", new BigDecimal(0));
							m.put("YR31_35", new BigDecimal(0));
							m.put("YR36_40", new BigDecimal(0));
							m.put("YR41_45", new BigDecimal(0));
							m.put("YR46_50", new BigDecimal(0));
							m.put("YR51_55", new BigDecimal(0));
							m.put("YR56_60", new BigDecimal(0));
							m.put("YR61_65", new BigDecimal(0));
							m.put("YR66_70", new BigDecimal(0));
							m.put("YR71_UP", new BigDecimal(0));
							m.put("YR_ALL", new BigDecimal(0));
						}else{
							Map dt = (HashMap) tmp.get(index);
							
							m.put("URUT", dt.get("URUT"));
							m.put("ASSURED", n.get("CAT"));
							m.put("YR0_1", dt.get("YR0_1"));
							m.put("YR2_5", dt.get("YR2_5"));
							m.put("YR6_10", dt.get("YR6_10"));
							m.put("YR11_15", dt.get("YR11_15"));
							m.put("YR16_20", dt.get("YR16_20"));
							m.put("YR21_25", dt.get("YR21_25"));
							m.put("YR26_30", dt.get("YR26_30"));
							m.put("YR31_35", dt.get("YR31_35"));
							m.put("YR36_40", dt.get("YR36_40"));
							m.put("YR41_45", dt.get("YR41_45"));
							m.put("YR46_50", dt.get("YR46_50"));
							m.put("YR51_55", dt.get("YR51_55"));
							m.put("YR56_60", dt.get("YR56_60"));
							m.put("YR61_65", dt.get("YR61_65"));
							m.put("YR66_70", dt.get("YR66_70"));
							m.put("YR71_UP", dt.get("YR71_UP"));
							m.put("YR_ALL", dt.get("YR_ALL"));
						}
						data.add(m);
					}
				}else{
					ServletOutputStream err = response.getOutputStream();
					err.println("<script>alert('Error!');window.close();</script>");
					err.close();
				}
    		}else if(jenis_polis.equals("2")){//Polis Individu
    			polis = "INDIVIDU";
    			grup_bank = "ALL";
    			nama_bank = "ALL";
				if(jenis_report.equals("1")){
					report = "report.claim.selectreportClaimBasedAgeDeath";
					data = uwManager.selectreportClaimBasedAgeDeath(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("2")){
					report = "report.claim.selectreportClaimBasedDurationPolicy";
					data = uwManager.selectreportClaimBasedPolicyDuration(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("3")){
					report = "report.claim.selectreportDetailClaim";
					data = uwManager.selectreportDetailClaim(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("4")){//by branch
					report = "report.claim.selectreportCODByBranch";
					data = uwManager.selectreportCODByBranch(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("5")){//Claim by COD
					report = "report.claim.selectreportClaimByCOD";
					data = uwManager.selectreportClaimByCOD(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
					tc = uwManager.selectreportClaimByCOD_TotalCase(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("6")){//COD Based on Medis
					report = "report.claim.selectreportClaimByMedis";
					List data1 = uwManager.selectreportClaimByMedis(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
					List num_medis = uwManager.selectGetTotalClaimByMedis(bdate, edate, currentUser.getLus_id(), polis, "1", grup_bank, nama_bank, jenis_periode);
					List num_non = uwManager.selectGetTotalClaimByMedis(bdate, edate, currentUser.getLus_id(), polis, "0", grup_bank, nama_bank, jenis_periode);
					int medis = 0;
					int non = 0;
					
					Map md = (HashMap) num_medis.get(0);
					Map nm = (HashMap) num_non.get(0);
					
					Integer md_sum_polis = ((BigDecimal) md.get("TOTAL_POLIS")).intValue();
					Integer md_sum_extra = ((BigDecimal) md.get("EXTRA_PREMI")).intValue();
					
					Integer nm_sum_polis = ((BigDecimal) nm.get("TOTAL_POLIS")).intValue();
					Integer nm_sum_extra = ((BigDecimal) nm.get("EXTRA_PREMI")).intValue();
					
					for(int i=0;i<data1.size();i++){
						Map n = (HashMap) data1.get(i);
						HashMap m = new HashMap();
						if(n.get("MEDICAL").toString().equals("Non Medis")){
							non +=1;
							m.put("MEDICAL", n.get("MEDICAL"));
							m.put("MEDICAL_NMBR", n.get("MEDICAL_NMBR"));
							m.put("TOTAL_POLIS", n.get("TOTAL_POLIS"));
							m.put("TOTAL_POLIS2", nm_sum_polis.toString());
							m.put("EXTRA_PREMI2", nm_sum_extra.toString());
							m.put("EXTRA_PREMI", n.get("EXTRA_PREMI"));
							m.put("ICD", n.get("ICD"));
							m.put("SEBAB", n.get("SEBAB"));
							m.put("SUM_RUPIAH", n.get("SUM_RUPIAH"));
							m.put("SUM_DOLLAR", n.get("SUM_DOLLAR"));
						}else{
							medis +=1;
							m.put("MEDICAL", n.get("MEDICAL"));
							m.put("MEDICAL_NMBR", n.get("MEDICAL_NMBR"));
							m.put("TOTAL_POLIS", n.get("TOTAL_POLIS"));
							m.put("TOTAL_POLIS2", md_sum_polis.toString());
							m.put("EXTRA_PREMI2", md_sum_extra.toString());
							m.put("EXTRA_PREMI", n.get("EXTRA_PREMI"));
							m.put("ICD", n.get("ICD"));
							m.put("SEBAB", n.get("SEBAB"));
							m.put("SUM_RUPIAH", n.get("SUM_RUPIAH"));
							m.put("SUM_DOLLAR", n.get("SUM_DOLLAR"));
						}
						
						data.add(m);
					}
					
					
					if(non==0){
						HashMap m = new HashMap();
						m.put("MEDICAL", "Non Medis");
						m.put("MEDICAL_NMBR", 0);
						m.put("TOTAL_POLIS2", "0");
						m.put("EXTRA_PREMI2", "0");
						m.put("TOTAL_POLIS", new BigDecimal(0));
						m.put("EXTRA_PREMI", new BigDecimal(0));
						m.put("IDC", null);
						m.put("SEBAB", null);
						m.put("SUM_RUPIAH", null);
						m.put("SUM_DOLLAR", null);
						
						data.add(m);
					}
					
					if(medis==0){
						HashMap m = new HashMap();
						m.put("MEDICAL", "Medis");
						m.put("MEDICAL_NMBR", 1);
						m.put("TOTAL_POLIS2", "0");
						m.put("EXTRA_PREMI2", "0");
						m.put("TOTAL_POLIS", new BigDecimal(0));
						m.put("EXTRA_PREMI", new BigDecimal(0));
						m.put("IDC", null);
						m.put("SEBAB", null);
						m.put("SUM_RUPIAH", null);
						m.put("SUM_DOLLAR", null);
						
						data.add(m);
					}
				}else if(jenis_report.equals("7")){//COD Based on Entry Age
					List age =  new ArrayList();
					String[] age1 = {"0 - 5", "6 - 10", "11 - 15", "16 - 20", "21 - 25", "26 - 30", "31 - 35", "36 - 40", "41 - 45", "46 - 50", "51 - 55", "56 - 60", "61 >"};
					for(int z=0;z<age1.length;z++){
						HashMap x = new HashMap();
						
						x.put("CAT", age1[z]);
						
						age.add(x);
					}
					report = "report.claim.selectreportClaimBasedEntryAge";
					List tmp = uwManager.selectreportClaimBasedEntryAge(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
					
					for (int i=0;i<age.size();i++){
						HashMap m = new HashMap();
						Map n = (HashMap) age.get(i);
						
						Integer index = null; 
						for(int j=0;j<tmp.size();j++){
							Map s = (HashMap) tmp.get(j);
							
							if(n.get("CAT").toString().trim().equals(s.get("UMUR").toString().trim())){
								index = j;
							}
						}
						
						if(index==null){
							m.put("UMUR", n.get("CAT"));
							m.put("USIA_0_6", new BigDecimal(0));
							m.put("USIA_7_12", new BigDecimal(0));
							m.put("USIA_2_YR", new BigDecimal(0));
							m.put("USIA_3_YR", new BigDecimal(0));
							m.put("USIA_4_YR", new BigDecimal(0));
							m.put("USIA_5_YR", new BigDecimal(0));
							m.put("USIA_6_YR", new BigDecimal(0));
							m.put("USIA_7_YR", new BigDecimal(0));
							m.put("USIA_8_YR", new BigDecimal(0));
							m.put("USIA_9_YR", new BigDecimal(0));
							m.put("USIA_10_YR", new BigDecimal(0));
							m.put("USIA_10_YR_UP", new BigDecimal(0));
							m.put("USIA_ALL", new BigDecimal(0));
						}else{
							Map dt = (HashMap) tmp.get(index);
							
							m.put("UMUR", dt.get("UMUR"));
							m.put("USIA_0_6", dt.get("USIA_0_6"));
							m.put("USIA_7_12", dt.get("USIA_7_12"));
							m.put("USIA_2_YR", dt.get("USIA_2_YR"));
							m.put("USIA_3_YR", dt.get("USIA_3_YR"));
							m.put("USIA_4_YR", dt.get("USIA_4_YR"));
							m.put("USIA_5_YR", dt.get("USIA_5_YR"));
							m.put("USIA_6_YR", dt.get("USIA_6_YR"));
							m.put("USIA_7_YR", dt.get("USIA_7_YR"));
							m.put("USIA_8_YR", dt.get("USIA_8_YR"));
							m.put("USIA_9_YR", dt.get("USIA_9_YR"));
							m.put("USIA_10_YR", dt.get("USIA_10_YR"));
							m.put("USIA_10_YR_UP", dt.get("USIA_10_YR_UP"));
							m.put("USIA_ALL", dt.get("USIA_ALL"));
						}
						
						data.add(m);
					}
				}else if(jenis_report.equals("8")){//EX-GRATIA Claim
					report = "report.claim.selectreportExGratiaClaim";
					data = uwManager.selectreportExGratiaClaim(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("9")){//By Product
					report = "report.claim.selectreportClaimByProduct";
					data = uwManager.selectreportClaimByProduct(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("10")){//By SA
					List assure =  new ArrayList();
					String[] assure1 = { "up to  10",">10 - 25",">26 - 50",">51 - 100",">101 - 125",">126 - 150",">151 - 200",">201 - 250",">251 - 300",">301 - 350",
							">351 - 400",">401 - 450",">451 - 500",">501 - 600",">601 - 700",">701 - 800",">801 - 900",">901 - 1.000",">1.001 - 1.250",
							">1.251 - 1.500",">1.501 - 1.750",">1.751 - 2.000",">2.001 - 2.500",">2.501 - 3.000",">3.001 - 3.500",">3.501 - 4.000",">4.001 - 4.500",
							">4.501 - 5.000",">5.001 - 6.000",">6.001 - 7.000",">7.001 - 8.000",">8.001 - 9.000",">9.001 - 10.000",">10.000"};
					for(int z=0;z<assure1.length;z++){
						HashMap x = new HashMap();
						
						x.put("CAT", assure1[z]);
						
						assure.add(x);
					}
					report = "report.claim.selectreportClaimBySA";
					List tmp = uwManager.selectreportClaimBySA(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
					
					for (int i=0;i<assure.size();i++){
						HashMap m = new HashMap();
						Map n = (HashMap) assure.get(i);
						
						Integer in = i +1;
						
						Integer index = null; 
						for(int j=0;j<tmp.size();j++){
							Map s = (HashMap) tmp.get(j);
							
							if(in == ((BigDecimal)s.get("URUT")).intValue()){
								index = j;
							}
						}
						
						if(index==null){
							m.put("URUT", new BigDecimal(in));
							m.put("ASSURED", n.get("CAT"));
							m.put("YR0_1", new BigDecimal(0));
							m.put("YR2_5", new BigDecimal(0));
							m.put("YR6_10", new BigDecimal(0));
							m.put("YR11_15", new BigDecimal(0));
							m.put("YR16_20", new BigDecimal(0));
							m.put("YR21_25", new BigDecimal(0));
							m.put("YR26_30", new BigDecimal(0));
							m.put("YR31_35", new BigDecimal(0));
							m.put("YR36_40", new BigDecimal(0));
							m.put("YR41_45", new BigDecimal(0));
							m.put("YR46_50", new BigDecimal(0));
							m.put("YR51_55", new BigDecimal(0));
							m.put("YR56_60", new BigDecimal(0));
							m.put("YR61_65", new BigDecimal(0));
							m.put("YR66_70", new BigDecimal(0));
							m.put("YR71_UP", new BigDecimal(0));
							m.put("YR_ALL", new BigDecimal(0));
						}else{
							Map dt = (HashMap) tmp.get(index);
							
							m.put("URUT", dt.get("URUT"));
							m.put("ASSURED", n.get("CAT"));
							m.put("YR0_1", dt.get("YR0_1"));
							m.put("YR2_5", dt.get("YR2_5"));
							m.put("YR6_10", dt.get("YR6_10"));
							m.put("YR11_15", dt.get("YR11_15"));
							m.put("YR16_20", dt.get("YR16_20"));
							m.put("YR21_25", dt.get("YR21_25"));
							m.put("YR26_30", dt.get("YR26_30"));
							m.put("YR31_35", dt.get("YR31_35"));
							m.put("YR36_40", dt.get("YR36_40"));
							m.put("YR41_45", dt.get("YR41_45"));
							m.put("YR46_50", dt.get("YR46_50"));
							m.put("YR51_55", dt.get("YR51_55"));
							m.put("YR56_60", dt.get("YR56_60"));
							m.put("YR61_65", dt.get("YR61_65"));
							m.put("YR66_70", dt.get("YR66_70"));
							m.put("YR71_UP", dt.get("YR71_UP"));
							m.put("YR_ALL", dt.get("YR_ALL"));
						}
						data.add(m);
					}
				}else if(jenis_report.equals("11")){//By Claim Paid
					List assure =  new ArrayList();
					String[] assure1 = { "up to  10",">10 - 25",">26 - 50",">51 - 100",">101 - 125",">126 - 150",">151 - 200",">201 - 250",">251 - 300",">301 - 350",
							">351 - 400",">401 - 450",">451 - 500",">501 - 600",">601 - 700",">701 - 800",">801 - 900",">901 - 1.000",">1.001 - 1.250",
							">1.251 - 1.500",">1.501 - 1.750",">1.751 - 2.000",">2.001 - 2.500",">2.501 - 3.000",">3.001 - 3.500",">3.501 - 4.000",">4.001 - 4.500",
							">4.501 - 5.000",">5.001 - 6.000",">6.001 - 7.000",">7.001 - 8.000",">8.001 - 9.000",">9.001 - 10.000",">10.000"};
					for(int z=0;z<assure1.length;z++){
						HashMap x = new HashMap();
						
						x.put("CAT", assure1[z]);
						
						assure.add(x);
					}
					report = "report.claim.selectreportClaimByPaid";
					List tmp = uwManager.selectreportClaimByPaid(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
					
					for (int i=0;i<assure.size();i++){
						HashMap m = new HashMap();
						Map n = (HashMap) assure.get(i);
						
						Integer in = i +1;
						
						Integer index = null; 
						for(int j=0;j<tmp.size();j++){
							Map s = (HashMap) tmp.get(j);
							
							if(in == ((BigDecimal)s.get("URUT")).intValue()){
								index = j;
							}
						}
						
						if(index==null){
							m.put("URUT", new BigDecimal(in));
							m.put("ASSURED", n.get("CAT"));
							m.put("YR0_1", new BigDecimal(0));
							m.put("YR2_5", new BigDecimal(0));
							m.put("YR6_10", new BigDecimal(0));
							m.put("YR11_15", new BigDecimal(0));
							m.put("YR16_20", new BigDecimal(0));
							m.put("YR21_25", new BigDecimal(0));
							m.put("YR26_30", new BigDecimal(0));
							m.put("YR31_35", new BigDecimal(0));
							m.put("YR36_40", new BigDecimal(0));
							m.put("YR41_45", new BigDecimal(0));
							m.put("YR46_50", new BigDecimal(0));
							m.put("YR51_55", new BigDecimal(0));
							m.put("YR56_60", new BigDecimal(0));
							m.put("YR61_65", new BigDecimal(0));
							m.put("YR66_70", new BigDecimal(0));
							m.put("YR71_UP", new BigDecimal(0));
							m.put("YR_ALL", new BigDecimal(0));
						}else{
							Map dt = (HashMap) tmp.get(index);
							
							m.put("URUT", dt.get("URUT"));
							m.put("ASSURED", n.get("CAT"));
							m.put("YR0_1", dt.get("YR0_1"));
							m.put("YR2_5", dt.get("YR2_5"));
							m.put("YR6_10", dt.get("YR6_10"));
							m.put("YR11_15", dt.get("YR11_15"));
							m.put("YR16_20", dt.get("YR16_20"));
							m.put("YR21_25", dt.get("YR21_25"));
							m.put("YR26_30", dt.get("YR26_30"));
							m.put("YR31_35", dt.get("YR31_35"));
							m.put("YR36_40", dt.get("YR36_40"));
							m.put("YR41_45", dt.get("YR41_45"));
							m.put("YR46_50", dt.get("YR46_50"));
							m.put("YR51_55", dt.get("YR51_55"));
							m.put("YR56_60", dt.get("YR56_60"));
							m.put("YR61_65", dt.get("YR61_65"));
							m.put("YR66_70", dt.get("YR66_70"));
							m.put("YR71_UP", dt.get("YR71_UP"));
							m.put("YR_ALL", dt.get("YR_ALL"));
						}
						data.add(m);
					}
				}else{
					ServletOutputStream err = response.getOutputStream();
					err.println("<script>alert('Error!');window.close();</script>");
					err.close();
				}
    		}else if(jenis_polis.equals("3")){//Polis MRI
    			polis = "MRI";
				if(jenis_report.equals("1")){
					report = "report.claim.selectreportClaimBasedAgeDeath";
					data = uwManager.selectreportClaimBasedAgeDeath(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("2")){
					report = "report.claim.selectreportClaimBasedDurationPolicy";
					data = uwManager.selectreportClaimBasedPolicyDuration(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("3")){
					report = "report.claim.selectreportDetailClaim";
					data = uwManager.selectreportDetailClaim(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("4")){//by branch
					report = "report.claim.selectreportCODByBranch";
					data = uwManager.selectreportCODByBranch(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("5")){//Claim by COD
					report = "report.claim.selectreportClaimByCOD";
					data = uwManager.selectreportClaimByCOD(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
					tc = uwManager.selectreportClaimByCOD_TotalCase(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("6")){//COD Based on Medis
					report = "report.claim.selectreportClaimByMedis";
					List data1 = uwManager.selectreportClaimByMedis(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
					List num_medis = uwManager.selectGetTotalClaimByMedis(bdate, edate, currentUser.getLus_id(), polis, "1", grup_bank, nama_bank, jenis_periode);
					List num_non = uwManager.selectGetTotalClaimByMedis(bdate, edate, currentUser.getLus_id(), polis, "0", grup_bank, nama_bank, jenis_periode);
					int medis = 0;
					int non = 0;
					
					Map md = (HashMap) num_medis.get(0);
					Map nm = (HashMap) num_non.get(0);
					
					Integer md_sum_polis = ((BigDecimal) md.get("TOTAL_POLIS")).intValue();
					Integer md_sum_extra = ((BigDecimal) md.get("EXTRA_PREMI")).intValue();
					
					Integer nm_sum_polis = ((BigDecimal) nm.get("TOTAL_POLIS")).intValue();
					Integer nm_sum_extra = ((BigDecimal) nm.get("EXTRA_PREMI")).intValue();
					
					for(int i=0;i<data1.size();i++){
						Map n = (HashMap) data1.get(i);
						HashMap m = new HashMap();
						if(n.get("MEDICAL").toString().equals("Non Medis")){
							non +=1;
							m.put("MEDICAL", n.get("MEDICAL"));
							m.put("MEDICAL_NMBR", n.get("MEDICAL_NMBR"));
							m.put("TOTAL_POLIS", n.get("TOTAL_POLIS"));
							m.put("TOTAL_POLIS2", nm_sum_polis.toString());
							m.put("EXTRA_PREMI2", nm_sum_extra.toString());
							m.put("EXTRA_PREMI", n.get("EXTRA_PREMI"));
							m.put("ICD", n.get("ICD"));
							m.put("SEBAB", n.get("SEBAB"));
							m.put("SUM_RUPIAH", n.get("SUM_RUPIAH"));
							m.put("SUM_DOLLAR", n.get("SUM_DOLLAR"));
						}else{
							medis +=1;
							m.put("MEDICAL", n.get("MEDICAL"));
							m.put("MEDICAL_NMBR", n.get("MEDICAL_NMBR"));
							m.put("TOTAL_POLIS", n.get("TOTAL_POLIS"));
							m.put("TOTAL_POLIS2", md_sum_polis.toString());
							m.put("EXTRA_PREMI2", md_sum_extra.toString());
							m.put("EXTRA_PREMI", n.get("EXTRA_PREMI"));
							m.put("ICD", n.get("ICD"));
							m.put("SEBAB", n.get("SEBAB"));
							m.put("SUM_RUPIAH", n.get("SUM_RUPIAH"));
							m.put("SUM_DOLLAR", n.get("SUM_DOLLAR"));
						}
						
						data.add(m);
					}
					
					
					if(non==0){
						HashMap m = new HashMap();
						m.put("MEDICAL", "Non Medis");
						m.put("MEDICAL_NMBR", 0);
						m.put("TOTAL_POLIS2", "0");
						m.put("EXTRA_PREMI2", "0");
						m.put("TOTAL_POLIS", new BigDecimal(0));
						m.put("EXTRA_PREMI", new BigDecimal(0));
						m.put("IDC", null);
						m.put("SEBAB", null);
						m.put("SUM_RUPIAH", null);
						m.put("SUM_DOLLAR", null);
						
						data.add(m);
					}
					
					if(medis==0){
						HashMap m = new HashMap();
						m.put("MEDICAL", "Medis");
						m.put("MEDICAL_NMBR", 1);
						m.put("TOTAL_POLIS2", "0");
						m.put("EXTRA_PREMI2", "0");
						m.put("TOTAL_POLIS", new BigDecimal(0));
						m.put("EXTRA_PREMI", new BigDecimal(0));
						m.put("IDC", null);
						m.put("SEBAB", null);
						m.put("SUM_RUPIAH", null);
						m.put("SUM_DOLLAR", null);
						
						data.add(m);
					}
				}else if(jenis_report.equals("7")){//COD Based on Entry Age
					List age =  new ArrayList();
					String[] age1 = {"0 - 5", "6 - 10", "11 - 15", "16 - 20", "21 - 25", "26 - 30", "31 - 35", "36 - 40", "41 - 45", "46 - 50", "51 - 55", "56 - 60", "61 >"};
					for(int z=0;z<age1.length;z++){
						HashMap x = new HashMap();
						
						x.put("CAT", age1[z]);
						
						age.add(x);
					}
					report = "report.claim.selectreportClaimBasedEntryAge";
					List tmp = uwManager.selectreportClaimBasedEntryAge(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
					
					for (int i=0;i<age.size();i++){
						HashMap m = new HashMap();
						Map n = (HashMap) age.get(i);
						
						Integer index = null; 
						for(int j=0;j<tmp.size();j++){
							Map s = (HashMap) tmp.get(j);
							
							if(n.get("CAT").toString().trim().equals(s.get("UMUR").toString().trim())){
								index = j;
							}
						}
						
						if(index==null){
							m.put("UMUR", n.get("CAT"));
							m.put("USIA_0_6", new BigDecimal(0));
							m.put("USIA_7_12", new BigDecimal(0));
							m.put("USIA_2_YR", new BigDecimal(0));
							m.put("USIA_3_YR", new BigDecimal(0));
							m.put("USIA_4_YR", new BigDecimal(0));
							m.put("USIA_5_YR", new BigDecimal(0));
							m.put("USIA_6_YR", new BigDecimal(0));
							m.put("USIA_7_YR", new BigDecimal(0));
							m.put("USIA_8_YR", new BigDecimal(0));
							m.put("USIA_9_YR", new BigDecimal(0));
							m.put("USIA_10_YR", new BigDecimal(0));
							m.put("USIA_10_YR_UP", new BigDecimal(0));
							m.put("USIA_ALL", new BigDecimal(0));
						}else{
							Map dt = (HashMap) tmp.get(index);
							
							m.put("UMUR", dt.get("UMUR"));
							m.put("USIA_0_6", dt.get("USIA_0_6"));
							m.put("USIA_7_12", dt.get("USIA_7_12"));
							m.put("USIA_2_YR", dt.get("USIA_2_YR"));
							m.put("USIA_3_YR", dt.get("USIA_3_YR"));
							m.put("USIA_4_YR", dt.get("USIA_4_YR"));
							m.put("USIA_5_YR", dt.get("USIA_5_YR"));
							m.put("USIA_6_YR", dt.get("USIA_6_YR"));
							m.put("USIA_7_YR", dt.get("USIA_7_YR"));
							m.put("USIA_8_YR", dt.get("USIA_8_YR"));
							m.put("USIA_9_YR", dt.get("USIA_9_YR"));
							m.put("USIA_10_YR", dt.get("USIA_10_YR"));
							m.put("USIA_10_YR_UP", dt.get("USIA_10_YR_UP"));
							m.put("USIA_ALL", dt.get("USIA_ALL"));
						}
						
						data.add(m);
					}
				}else if(jenis_report.equals("8")){//EX-GRATIA Claim
					report = "report.claim.selectreportExGratiaClaim";
					data = uwManager.selectreportExGratiaClaim(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("9")){//By Product
					report = "report.claim.selectreportClaimByProduct";
					data = uwManager.selectreportClaimByProduct(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				}else if(jenis_report.equals("10")){//By SA
					List assure =  new ArrayList();
					String[] assure1 = { "up to  10",">10 - 25",">26 - 50",">51 - 100",">101 - 125",">126 - 150",">151 - 200",">201 - 250",">251 - 300",">301 - 350",
							">351 - 400",">401 - 450",">451 - 500",">501 - 600",">601 - 700",">701 - 800",">801 - 900",">901 - 1.000",">1.001 - 1.250",
							">1.251 - 1.500",">1.501 - 1.750",">1.751 - 2.000",">2.001 - 2.500",">2.501 - 3.000",">3.001 - 3.500",">3.501 - 4.000",">4.001 - 4.500",
							">4.501 - 5.000",">5.001 - 6.000",">6.001 - 7.000",">7.001 - 8.000",">8.001 - 9.000",">9.001 - 10.000",">10.000"};
					for(int z=0;z<assure1.length;z++){
						HashMap x = new HashMap();
						
						x.put("CAT", assure1[z]);
						
						assure.add(x);
					}
					report = "report.claim.selectreportClaimBySA";
					List tmp = uwManager.selectreportClaimBySA(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
					
					for (int i=0;i<assure.size();i++){
						HashMap m = new HashMap();
						Map n = (HashMap) assure.get(i);
						
						Integer in = i +1;
						
						Integer index = null; 
						for(int j=0;j<tmp.size();j++){
							Map s = (HashMap) tmp.get(j);
							
							if(in == ((BigDecimal)s.get("URUT")).intValue()){
								index = j;
							}
						}
						
						if(index==null){
							m.put("URUT", new BigDecimal(in));
							m.put("ASSURED", n.get("CAT"));
							m.put("YR0_1", new BigDecimal(0));
							m.put("YR2_5", new BigDecimal(0));
							m.put("YR6_10", new BigDecimal(0));
							m.put("YR11_15", new BigDecimal(0));
							m.put("YR16_20", new BigDecimal(0));
							m.put("YR21_25", new BigDecimal(0));
							m.put("YR26_30", new BigDecimal(0));
							m.put("YR31_35", new BigDecimal(0));
							m.put("YR36_40", new BigDecimal(0));
							m.put("YR41_45", new BigDecimal(0));
							m.put("YR46_50", new BigDecimal(0));
							m.put("YR51_55", new BigDecimal(0));
							m.put("YR56_60", new BigDecimal(0));
							m.put("YR61_65", new BigDecimal(0));
							m.put("YR66_70", new BigDecimal(0));
							m.put("YR71_UP", new BigDecimal(0));
							m.put("YR_ALL", new BigDecimal(0));
						}else{
							Map dt = (HashMap) tmp.get(index);
							
							m.put("URUT", dt.get("URUT"));
							m.put("ASSURED", n.get("CAT"));
							m.put("YR0_1", dt.get("YR0_1"));
							m.put("YR2_5", dt.get("YR2_5"));
							m.put("YR6_10", dt.get("YR6_10"));
							m.put("YR11_15", dt.get("YR11_15"));
							m.put("YR16_20", dt.get("YR16_20"));
							m.put("YR21_25", dt.get("YR21_25"));
							m.put("YR26_30", dt.get("YR26_30"));
							m.put("YR31_35", dt.get("YR31_35"));
							m.put("YR36_40", dt.get("YR36_40"));
							m.put("YR41_45", dt.get("YR41_45"));
							m.put("YR46_50", dt.get("YR46_50"));
							m.put("YR51_55", dt.get("YR51_55"));
							m.put("YR56_60", dt.get("YR56_60"));
							m.put("YR61_65", dt.get("YR61_65"));
							m.put("YR66_70", dt.get("YR66_70"));
							m.put("YR71_UP", dt.get("YR71_UP"));
							m.put("YR_ALL", dt.get("YR_ALL"));
						}
						data.add(m);
					}
				}else if(jenis_report.equals("11")){//By Claim Paid
					List assure =  new ArrayList();
					String[] assure1 = { "up to  10",">10 - 25",">26 - 50",">51 - 100",">101 - 125",">126 - 150",">151 - 200",">201 - 250",">251 - 300",">301 - 350",
							">351 - 400",">401 - 450",">451 - 500",">501 - 600",">601 - 700",">701 - 800",">801 - 900",">901 - 1.000",">1.001 - 1.250",
							">1.251 - 1.500",">1.501 - 1.750",">1.751 - 2.000",">2.001 - 2.500",">2.501 - 3.000",">3.001 - 3.500",">3.501 - 4.000",">4.001 - 4.500",
							">4.501 - 5.000",">5.001 - 6.000",">6.001 - 7.000",">7.001 - 8.000",">8.001 - 9.000",">9.001 - 10.000",">10.000"};
					for(int z=0;z<assure1.length;z++){
						HashMap x = new HashMap();
						
						x.put("CAT", assure1[z]);
						
						assure.add(x);
					}
					report = "report.claim.selectreportClaimByPaid";
					List tmp = uwManager.selectreportClaimByPaid(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
					
					for (int i=0;i<assure.size();i++){
						HashMap m = new HashMap();
						Map n = (HashMap) assure.get(i);
						
						Integer in = i +1;
						
						Integer index = null; 
						for(int j=0;j<tmp.size();j++){
							Map s = (HashMap) tmp.get(j);
							
							if(in == ((BigDecimal)s.get("URUT")).intValue()){
								index = j;
							}
						}
						
						if(index==null){
							m.put("URUT", new BigDecimal(in));
							m.put("ASSURED", n.get("CAT"));
							m.put("YR0_1", new BigDecimal(0));
							m.put("YR2_5", new BigDecimal(0));
							m.put("YR6_10", new BigDecimal(0));
							m.put("YR11_15", new BigDecimal(0));
							m.put("YR16_20", new BigDecimal(0));
							m.put("YR21_25", new BigDecimal(0));
							m.put("YR26_30", new BigDecimal(0));
							m.put("YR31_35", new BigDecimal(0));
							m.put("YR36_40", new BigDecimal(0));
							m.put("YR41_45", new BigDecimal(0));
							m.put("YR46_50", new BigDecimal(0));
							m.put("YR51_55", new BigDecimal(0));
							m.put("YR56_60", new BigDecimal(0));
							m.put("YR61_65", new BigDecimal(0));
							m.put("YR66_70", new BigDecimal(0));
							m.put("YR71_UP", new BigDecimal(0));
							m.put("YR_ALL", new BigDecimal(0));
						}else{
							Map dt = (HashMap) tmp.get(index);
							
							m.put("URUT", dt.get("URUT"));
							m.put("ASSURED", n.get("CAT"));
							m.put("YR0_1", dt.get("YR0_1"));
							m.put("YR2_5", dt.get("YR2_5"));
							m.put("YR6_10", dt.get("YR6_10"));
							m.put("YR11_15", dt.get("YR11_15"));
							m.put("YR16_20", dt.get("YR16_20"));
							m.put("YR21_25", dt.get("YR21_25"));
							m.put("YR26_30", dt.get("YR26_30"));
							m.put("YR31_35", dt.get("YR31_35"));
							m.put("YR36_40", dt.get("YR36_40"));
							m.put("YR41_45", dt.get("YR41_45"));
							m.put("YR46_50", dt.get("YR46_50"));
							m.put("YR51_55", dt.get("YR51_55"));
							m.put("YR56_60", dt.get("YR56_60"));
							m.put("YR61_65", dt.get("YR61_65"));
							m.put("YR66_70", dt.get("YR66_70"));
							m.put("YR71_UP", dt.get("YR71_UP"));
							m.put("YR_ALL", dt.get("YR_ALL"));
						}
						data.add(m);
					}
				}else{
					ServletOutputStream err = response.getOutputStream();
					err.println("<script>alert('Error!');window.close();</script>");
					err.close();
				}
    		}else{
    			
    		}
    		
    		if(data.size() > 0){ //bila ada data
	    		ServletOutputStream sos = response.getOutputStream();
	    		logger.info("Report "+props.getProperty(report));
	    		File sourceFile = Resources.getResourceAsFile(props.getProperty(report) + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);
	    		params.put("type", polis);
	    		params.put("print", print);
	    		params.put("periode", periode);
	    		params.put("grup_bank", ServletRequestUtils.getStringParameter(request, "grup_bank2", "ALL"));
	    		params.put("nama_bank", ServletRequestUtils.getStringParameter(request, "nama_bank2", "ALL"));
	    		if(tc!=null){
	    			params.put("tc", tc.toString());
	    		}
	    		

	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));

		    	if(request.getParameter("showXLS") != null){
		    		response.setContentType("application/vnd.ms-excel");
		            JRXlsExporter exporter = new JRXlsExporter();
		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
		            exporter.exportReport();
		    	}else if(request.getParameter("showPDF") != null){
		    		JasperExportManager.exportReportToPdfStream(jasperPrint, sos);
		    	}
	    		sos.close();
    		}else{ //bila tidak ada data
    			ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Tidak ada data');window.close();</script>");
    			sos.close();
    		}

    	}else if(json == 0){
    		Map<String, Object> m = new HashMap<String, Object>();

//        	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
//        	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(30)));
    		Calendar cal = Calendar.getInstance();
    		cal.add(Calendar.DAY_OF_MONTH, 30);
    		Date sebulan = cal.getTime();
        	
        	m.put("bdate", defaultDateFormat.format(new Date()));
        	m.put("edate", defaultDateFormat.format(sebulan));
        	
        	m.put("listGrupBank", elionsManager.selectDropDown(
    				"eka.lst_customer_group_mri a", "a.lcg_group", "a.lcg_nama", "", "2", ""));
	    	m.put("listNamaBank", elionsManager.selectDropDown(
				"eka.mst_det_customer_group_mri a, eka.lst_customer_group_mri b, eka.mst_client_new c", "c.mcl_id", "c.mcl_first", "", "2", 
				"a.MDC_GROUP = b.LCG_GROUP and a.mdc_mcl_id = c.mcl_id"));

        	return new ModelAndView("report/reportclaimAnalysis", m);
    	}else if(json == 1){ 
    		String lcg = ServletRequestUtils.getStringParameter(request, "lcg", "ALL");
    		logger.info(lcg);
    		List<DropDown> result = elionsManager.selectDropDown(
    			"eka.mst_det_customer_group_mri a, eka.lst_customer_group_mri b, eka.mst_client_new c", "c.mcl_id", "c.mcl_first", "", "2", 
    			"a.MDC_GROUP = b.LCG_GROUP and a.mdc_mcl_id = c.mcl_id " +(lcg.equals("ALL") ?  "" : " and b.lcg_group = '" +lcg+ "' ")+ "");
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();
    	}
    	
    	return null;
    }
    
    public ModelAndView report_endors(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String no = ServletRequestUtils.getStringParameter(request, "no","");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj","");
		Report report = null;
		File dir = new File(
				props.getProperty("pdf.dir.export") + "\\" +
				elionsManager.selectCabangFromSpaj(spaj) + "\\" +
				spaj);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
	    report = new Report("Report Endorse",props.getProperty("report.uw.report_history_endors"),Report.PDF, null);
	    File fileDir = new File(report.getReportPath());
	    String dest = dir +"\\";
	    File outputFile = new File(dest);
	   // String dir = props.getProperty("pdf.dir.report") + "\\polis_expired\\";
		Map<String, Comparable> params = new HashMap<String, Comparable>();
		params.put("no", no);
		
		Connection conn = null;
		try {
			//conn = this.getDataSource().getConnection();
			conn = this.getUwManager().getUwDao().getDataSource().getConnection();
			JasperUtils.exportReportToPdf(
					props.getProperty("report.uw.report_history_endors")+".jasper", 
					dest, 
					"endorsment ["+no+"].pdf", 
					params, 
					conn, 
					PdfWriter.AllowPrinting, null, null);
		}catch(Exception e){
            throw e;
		}finally{
			closeConnection(conn);
		}
		

		report.addParamDefault("no", "no", 0,no,  false);
		return prepareReport(request, response, report);
	}
    
    /**
     * Report UW Individu
     * E-Lions/report/uw.htm?window=reportUWIndividu&type=Agency-WS
     */
    public ModelAndView reportUWIndividu(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	String type = ServletRequestUtils.getStringParameter(request, "type");
//    	String print = defaultDateFormat.format(uwManager.selectSysdateTruncated(0));
    	String print = defaultDateFormat.format(new Date());
    	
    	if(type.equals("Bancass-2"))type = "Bancass 2";
    	if(type.equals("WS-Payroll"))type = "WS Payroll Deduction";
    	if(type.equals("WS_MNC_FCD"))type = "WS MNC FCD Bancass-1";
    	if(type.equals("Agency-WS"))type = "Agency";//hapus setelah update link menu dari Agency-WS menjadi Agency

    	//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		String report = ServletRequestUtils.getStringParameter(request, "report");
    		String status = ServletRequestUtils.getStringParameter(request, "status");
    		String produk = ServletRequestUtils.getStringParameter(request, "produk");
    		
    		ArrayList data = new ArrayList();
    		
    		String prop_report ;
//    		if(report.equals("Agency")){
//    			data =  Common.serializableList(uwManager.selectreportUWIndividu(bdate, edate, currentUser.getLus_id(), status, produk));
//    		}else if(report.equals("WS Payroll Deduction")){
    		prop_report = props.getProperty("report.uw.individu.ws.payroll");
			data = Common.serializableList(uwManager.selectreportUWIndividu_ws_payroll(bdate, edate, currentUser.getLus_id(), status, produk));
    		//}
//    		else if(report.equals("Agency")){
//				data = uwManager.selectreportUWIndividu_Agency(bdate, edate, currentUser.getLus_id(), status, produk);
//    		}else if(report.equals("Bancass-1")){
//				data = uwManager.selectreportUWIndividu_Bancass1(bdate, edate, currentUser.getLus_id(), status, produk);
//    		}else if(report.equals("Bancass 2")){
//				data = uwManager.selectreportUWIndividu_Bancass2(bdate, edate, currentUser.getLus_id(), status, produk);
//    		}else if(report.equals("Sekuritas")){
//				data = uwManager.selectreportUWIndividu_Sekuritas(bdate, edate, currentUser.getLus_id(), status, produk);
//    		}else if(report.equals("WS Payroll Deduction")){
//    			prop_report = props.getProperty("report.uw.individu.ws.payroll");
//				data = uwManager.selectreportUWIndividu_ws_payroll(bdate, edate, currentUser.getLus_id(), status, produk);
//    		}
//    		}else if(report.equals("WS MNC FCD Bancass-1")){
//				data = uwManager.selectreportUWIndividu_WS_MNC_FCD(bdate, edate, currentUser.getLus_id(), status, produk);
//    		}else{
//    			
//    		}
    		
    		if(data.size() > 0){ //bila ada data
	    		ServletOutputStream sos = response.getOutputStream();
	    		File sourceFile = Resources.getResourceAsFile(prop_report + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);
	    		params.put("print", print);
	    		params.put("status", status);
	    		params.put("report", type);
	    		params.put("produk", produk);

	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));
	    		
		    	if(request.getParameter("showXLS") != null){
		    		response.setContentType("application/vnd.ms-excel");
		            JRXlsExporter exporter = new JRXlsExporter();
		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
		            exporter.exportReport();
		    	}else if(request.getParameter("showPDF") != null){
		    		JasperExportManager.exportReportToPdfStream(jasperPrint, sos);
		    	}
		    	if(sos!=null){
					sos.flush();
					sos.close();
				}
				
	    		sos.close();
    		}else{ //bila tidak ada data
    			ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Tidak ada data');window.close();</script>");
    			if(sos!=null){
					sos.flush();
					sos.close();
				}
    		}

    	}else if(type.equals("Agency")){
    		Map<String, Object> m = new HashMap<String, Object>();

//        	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
//        	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(30)));
    		Calendar cal = Calendar.getInstance();
    		cal.add(Calendar.DAY_OF_MONTH, 30);
    		Date sebulan = cal.getTime();
        	m.put("bdate", defaultDateFormat.format(new Date()));
        	m.put("edate", defaultDateFormat.format(sebulan));
        	m.put("type", type);

        	return new ModelAndView("report/UWIndividu_agency", m);
//    	}else if(type.equals("Bancass-1")){
//    		Map<String, Object> m = new HashMap<String, Object>();
//
//        	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
//        	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(30)));
//        	m.put("type", type);
//
//        	return new ModelAndView("report/UWIndividu_bancass1", m);
//    	}else if(type.equals("Bancass 2")){
//    		Map<String, Object> m = new HashMap<String, Object>();
//
//        	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
//        	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(30)));
//        	m.put("type", type);
//
//        	return new ModelAndView("report/UWIndividu_bancass2", m);
//    	}else if(type.equals("Sekuritas")){
//    		Map<String, Object> m = new HashMap<String, Object>();
//
//        	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
//        	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(30)));
//        	m.put("type", type);
//
//        	return new ModelAndView("report/UWIndividu_sekuritas", m);
    	}else if(type.equals("WS Payroll Deduction")){
    		Map<String, Object> m = new HashMap<String, Object>();

//        	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
//        	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(30)));

    		Calendar cal = Calendar.getInstance();
    		cal.add(Calendar.DAY_OF_MONTH, 30);
    		Date sebulan = cal.getTime();
    		
        	m.put("bdate", defaultDateFormat.format(new Date()));
        	m.put("edate", defaultDateFormat.format(sebulan));
        	m.put("type", type);

        	return new ModelAndView("report/UWIndividu_ws_payroll", m);
    	}
//    	}else if(type.equals("WS MNC FCD Bancass-1")){
//    		Map<String, Object> m = new HashMap<String, Object>();
//
//        	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
//        	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(30)));
//        	m.put("type", type);
//
//        	return new ModelAndView("report/UWIndividu_WS_MNC_FCD", m);
//    	}else{
////    		Map<String, Object> m = new HashMap<String, Object>();
////
////        	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
////        	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(30)));
////        	m.put("type", type);
////
////        	return new ModelAndView("report/UWIndividu", m);
    	
    	
    	return null;
    }
    
    /**
     * Report SPAJ Refund dan Batal
     * E-Lions/report/uw.htm?window=reportSPAJ_Refund_Batal
     */
    public ModelAndView reportSPAJ_Refund_Batal(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
//    	String print = defaultDateFormat.format(uwManager.selectSysdateTruncated(0));
    	String print = defaultDateFormat.format(new Date());

    	//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		SimpleDateFormat bln = new SimpleDateFormat("MMMM");
    		String jenis_report = ServletRequestUtils.getStringParameter(request, "jenis_report");
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		
    		String year = FormatDate.getYearFourDigit(defaultDateFormat.parse(bdate));
    		//String month = FormatDate.getMonth(defaultDateFormat.parse(bdate));
    		String month = bln.format(defaultDateFormat.parse(bdate));
    		
    		List data = new ArrayList();
    		String report = null;
    		String tgl = null;
    		
    		if(jenis_report.equals("1")){//detail
        		tgl = month+" "+year;
    			report = "report.refund.detail";
    			data = uwManager.selectreportDetailSPAJRefundBatal(bdate, currentUser.getLus_id());
    		}else if(jenis_report.equals("2")){//summary
    			tgl = year;
    			report = "report.refund.summary";
    			data = uwManager.selectreportSummarySPAJRefundBatal(bdate, currentUser.getLus_id());
    		}else{
    			ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Error');window.close();</script>");
    			sos.close();
    		}
    		
    		if(data.size() > 0){ //bila ada data
	    		ServletOutputStream sos = response.getOutputStream();
	    		File sourceFile = Resources.getResourceAsFile(props.getProperty(report) + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("periode", tgl);
	    		params.put("tgl", print);

	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));

		    	if(request.getParameter("showXLS") != null){
		    		response.setContentType("application/vnd.ms-excel");
		            JRXlsExporter exporter = new JRXlsExporter();
		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
		            exporter.exportReport();
		    	}else if(request.getParameter("showPDF") != null){
		    		JasperExportManager.exportReportToPdfStream(jasperPrint, sos);
		    	}
	    		sos.close();
    		}else{ //bila tidak ada data
    			ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Tidak ada data');window.close();</script>");
    			sos.close();
    		}

    	}else{
    		Map<String, Object> m = new HashMap<String, Object>();

//        	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
        	m.put("bdate", defaultDateFormat.format(new Date()));
        	
        	return new ModelAndView("report/reportspajRefundBatal", m);
    	}
    	
    	return null;
    }
    public ModelAndView report_aksep_spt(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("Report Akseptasi SPT By Email", props.getProperty("report.uw.aksep_spt_byEmail"), Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		return prepareReport(request, response, report);
	}
    
    public ModelAndView list_ibank(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String no_trx = request.getParameter("no_trx");
		String norek_ajs = request.getParameter("norek_ajs");
		
		/*no_trx ="FT11300TTM89"+"\\"+"SKB";
		norek_ajs ="0002184702";*/
		List mstDrekDetBasedNoTrx = uwManager.selectMstDrekDet2( no_trx, null, null, norek_ajs ); 
		if(mstDrekDetBasedNoTrx.size() > 0){ //bila ada data
    		ServletOutputStream sos = response.getOutputStream();
    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.uw.list_ibank") + ".jasper");
    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

    		Map<String, Object> params = new HashMap<String, Object>();


    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(mstDrekDetBasedNoTrx));

	    	
	    		response.setContentType("application/vnd.ms-excel");
	            JRXlsExporter exporter = new JRXlsExporter();
	            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
	            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
	            exporter.exportReport();
	    	
    		sos.close();
		}else{ //bila tidak ada data
			ServletOutputStream sos = response.getOutputStream();
			sos.println("<script>alert('Tidak ada data');window.close();</script>");
			sos.close();
		}
		return null;
	}
    
	public ModelAndView reportKartuEkaSehat(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map param= new HashMap();
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");

		report = new Report("Report Kartu Admedika", props.getProperty("report.uw.reportKartuEkaSehat"), Report.PDF, null);
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView report_pencairan_case(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("Report Daily Monitoring KYC", props.getProperty("report.report_top_up_case"), Report.PDF, null);
//		Date sysdate=elionsManager.selectSysdate();
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");

		Date tglAwal=null,tglAkhir=null;
		if(tanggalAwal!=null)
			tglAwal=FormatDate.toDateWithSlash(tanggalAwal);
		if(tanggalAkhir!=null)
			tglAkhir=FormatDate.toDateWithSlash(tanggalAkhir);
		report.addParamDate("Tanggal Terima SPAJ", "tanggal", true, new Date[] {tglAwal, tglAkhir}, true);
		
		List KYCpencairan=uwManager.selectKYCpencairan_main(tanggalAwal,tanggalAkhir);
	
		report.setResultList(uwManager.selectLsKycPencairan(KYCpencairan));
		
		return prepareReport(request, response, report);
	}
	
    /**
     * Akseptasi Endorsement material
     * @autor Andhika (01/07/2013)
     */	
	public ModelAndView permohonanaksep(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		
        String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
        String cabang = elionsManager.selectCabangFromSpaj(spaj);
		
		File dir = new File(
				props.getProperty("pdf.dir.endors") + "\\" + cabang + "\\" + spaj);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		report = new Report("permohonanaksep", props.getProperty("report.titipanpremi.worksheet.endorse"), Report.HTML, null);
	    File fileDir = new File(report.getReportPath());
	    String dest = dir +"\\";
	    File outputFile = new File(dest);
	    
		Map<String, Comparable> params = new HashMap<String, Comparable>();
		params.put("spaj", spaj);
		
		Connection conn = null;
		try {
			//conn = this.getDataSource().getConnection();
			conn = this.getUwManager().getUwDao().getDataSource().getConnection();
			JasperUtils.exportReportToPdf(
					props.getProperty("report.titipanpremi.worksheet.endorse")+".jasper", 
					dest, 
					""+spaj+" ENDORS_MATERIAL.pdf", 
					params, 
					conn, 
					PdfWriter.AllowPrinting, null, null);
		}catch(Exception e){
            throw e;
		}finally{
			closeConnection(conn);
		}
		
		report.addParamDefault("no", "no", 0,spaj,  false); 
		
		report.addParamText("spaj", "spaj", 15, null, true);
		report.addParamText("user", "user", 50, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
//	public ModelAndView reportEndorsmentMaterial(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Report report;
//		report = new Report("reportEndorsmentMaterial", props.getProperty("report.report_endorsment_material"), Report.HTML, null);
//		report.addParamText("spaj", "spaj", 15, null, true);
//		report.addParamText("user", "user", 50, null, true);
//		request.getSession().setAttribute("report", report);
//		return prepareReport(request, response, report);
//	}	
	
    public ModelAndView reportEndorsmentMaterial( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "report.report_endorsment_material" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
//        Date sysDate = elionsManager.selectSysdate();
        Date sysDate = new Date();
        
        List<DropDown> reportPathList = new ArrayList<DropDown>();
        
        String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
        String cabang = elionsManager.selectCabangFromSpaj(spaj);
        
        	reportPathList.add( new DropDown(
                props.getProperty( "report.report_endorsment_material" ),
                "Endorsment Material"
        	) );

        String namaprod = "";	
        String lsdbsnumber = "";
        String jn_bank = Integer.toString(currentUser.getJn_bank());
        
    	report = new Report( "Report Endorsment Material", reportPathList, Report.PDF, null );
		report.addParamDefault("username", "username", 200, currentUser.getLus_full_name(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDefault("jn_bank", "jn_bank", 200, jn_bank, false);
		report.addParamDefault("namaprod", "namaprod", 200, namaprod, false);
		report.addParamDefault("lsdbsnumber", "lsdbsnumber", 200, lsdbsnumber, false);
    	report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
    	return prepareReport( request, response, report );
    }	
    
    /**
     * Report Claim Analysis Kesehatan
     * E-Lions/report/uw.htm?window=reportclaimKesehatan
     * @autor Canpri
     * @since Jul, 29 2013
     */
    public ModelAndView reportclaimKesehatan(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	int json = ServletRequestUtils.getIntParameter(request, "json", 0);
//    	String print = defaultDateFormat.format(uwManager.selectSysdateTruncated(0));
    	String print = defaultDateFormat.format(new Date());

    	//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		
    		String jenis_report = ServletRequestUtils.getStringParameter(request, "jenis_report");
    		String jenis_polis = ServletRequestUtils.getStringParameter(request, "jenis_polis");
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		String jenis_periode = ServletRequestUtils.getStringParameter(request, "jenis_periode","1");
    		
    		List data = new ArrayList();
    		String report = null;
    		Integer tc = null;
    		String periode = "";
    		
    		if(jenis_periode.equals("1")){
    			periode = "Date Submitted";
    		}else if(jenis_periode.equals("2")){
    			periode = "Date Event";
    		}else if(jenis_periode.equals("3")){
    			periode = "Date Paid";
    		}
    		
    		if(jenis_report.equals("1")){
				report = "report.claim.selectreportHealthClaimBasedAge";
				data = uwManager.selectreportHealthClaimBasedAge(bdate, edate, currentUser.getLus_id(), jenis_periode);
			}else if(jenis_report.equals("2")){
				report = "report.claim.selectreportHealthClaimBasedDurationPolicy";
				data = uwManager.selectreportHealthClaimBasedDurationPolicy(bdate, edate, currentUser.getLus_id(), jenis_periode);
			}else if(jenis_report.equals("3")){
				report = "report.claim.selectreportDetailHealthClaim";
				data = uwManager.selectreportDetailHealthClaim(bdate, edate, currentUser.getLus_id(), jenis_periode);
			}else if(jenis_report.equals("4")){//by branch
				report = "report.claim.selectreportHealthClaimByBranch";
				data = uwManager.selectreportHealthClaimByBranch(bdate, edate, currentUser.getLus_id(), jenis_periode);
			}else if(jenis_report.equals("5")){//Claim by Disease
				report = "report.claim.selectreportHealthClaimByCOD";
				data = uwManager.selectreportHealthClaimByCOD(bdate, edate, currentUser.getLus_id(), jenis_periode);
				tc = uwManager.selectreportHealthClaimByCOD_TotalCase(bdate, edate, currentUser.getLus_id(), jenis_periode);
			}else if(jenis_report.equals("6")){//Claim Based on Medis
				report = "report.claim.selectreportHealthClaimByMedis";
				List data1 = uwManager.selectreportHealthClaimByMedis(bdate, edate, currentUser.getLus_id(),jenis_periode);
				List num_medis = uwManager.selectGetTotalHealthClaimByMedis(bdate, edate, currentUser.getLus_id(), "1", jenis_periode);
				List num_non = uwManager.selectGetTotalHealthClaimByMedis(bdate, edate, currentUser.getLus_id(), "0", jenis_periode);
				int medis = 0;
				int non = 0;
				
				Map md = (HashMap) num_medis.get(0);
				Map nm = (HashMap) num_non.get(0);
				
				Integer md_sum_polis = ((BigDecimal) md.get("TOTAL_POLIS")).intValue();
				Integer md_sum_extra = ((BigDecimal) md.get("EXTRA_PREMI")).intValue();
				
				Integer nm_sum_polis = ((BigDecimal) nm.get("TOTAL_POLIS")).intValue();
				Integer nm_sum_extra = ((BigDecimal) nm.get("EXTRA_PREMI")).intValue();
				
				for(int i=0;i<data1.size();i++){
					Map n = (HashMap) data1.get(i);
					HashMap m = new HashMap();
					if(n.get("MEDICAL").toString().equals("Non Medis")){
						non +=1;
						m.put("MEDICAL", n.get("MEDICAL"));
						m.put("MEDICAL_NMBR", n.get("MEDICAL_NMBR"));
						m.put("TOTAL_POLIS", n.get("TOTAL_POLIS"));
						m.put("TOTAL_POLIS2", nm_sum_polis.toString());
						m.put("EXTRA_PREMI2", nm_sum_extra.toString());
						m.put("EXTRA_PREMI", n.get("EXTRA_PREMI"));
						m.put("ICD", n.get("ICD"));
						m.put("SEBAB", n.get("SEBAB"));
						m.put("SUM_RUPIAH", n.get("SUM_RUPIAH"));
						m.put("SUM_DOLLAR", n.get("SUM_DOLLAR"));
					}else{
						medis +=1;
						m.put("MEDICAL", n.get("MEDICAL"));
						m.put("MEDICAL_NMBR", n.get("MEDICAL_NMBR"));
						m.put("TOTAL_POLIS", n.get("TOTAL_POLIS"));
						m.put("TOTAL_POLIS2", md_sum_polis.toString());
						m.put("EXTRA_PREMI2", md_sum_extra.toString());
						m.put("EXTRA_PREMI", n.get("EXTRA_PREMI"));
						m.put("ICD", n.get("ICD"));
						m.put("SEBAB", n.get("SEBAB"));
						m.put("SUM_RUPIAH", n.get("SUM_RUPIAH"));
						m.put("SUM_DOLLAR", n.get("SUM_DOLLAR"));
					}
					
					data.add(m);
				}
				
				
				if(non==0){
					HashMap m = new HashMap();
					m.put("MEDICAL", "Non Medis");
					m.put("MEDICAL_NMBR", 0);
					m.put("TOTAL_POLIS2", "0");
					m.put("EXTRA_PREMI2", "0");
					m.put("TOTAL_POLIS", new BigDecimal(0));
					m.put("EXTRA_PREMI", new BigDecimal(0));
					m.put("IDC", null);
					m.put("SEBAB", null);
					m.put("SUM_RUPIAH", null);
					m.put("SUM_DOLLAR", null);
					
					data.add(m);
				}
				
				if(medis==0){
					HashMap m = new HashMap();
					m.put("MEDICAL", "Medis");
					m.put("MEDICAL_NMBR", 1);
					m.put("TOTAL_POLIS2", "0");
					m.put("EXTRA_PREMI2", "0");
					m.put("TOTAL_POLIS", new BigDecimal(0));
					m.put("EXTRA_PREMI", new BigDecimal(0));
					m.put("IDC", null);
					m.put("SEBAB", null);
					m.put("SUM_RUPIAH", null);
					m.put("SUM_DOLLAR", null);
					
					data.add(m);
				}
			}else if(jenis_report.equals("7")){//Claim Based on Entry Age
				List age =  new ArrayList();
				String[] age1 = {"0 - 5", "6 - 10", "11 - 15", "16 - 20", "21 - 25", "26 - 30", "31 - 35", "36 - 40", "41 - 45", "46 - 50", "51 - 55", "56 - 60", "61 >"};
				for(int z=0;z<age1.length;z++){
					HashMap x = new HashMap();
					
					x.put("CAT", age1[z]);
					
					age.add(x);
				}
				report = "report.claim.selectreportHealthClaimBasedEntryAge";
				List tmp = uwManager.selectreportHealthClaimBasedEntryAge(bdate, edate, currentUser.getLus_id(), jenis_periode);
				
				for (int i=0;i<age.size();i++){
					HashMap m = new HashMap();
					Map n = (HashMap) age.get(i);
					
					Integer index = null; 
					for(int j=0;j<tmp.size();j++){
						Map s = (HashMap) tmp.get(j);
						
						if(n.get("CAT").toString().trim().equals(s.get("UMUR").toString().trim())){
							index = j;
						}
					}
					
					if(index==null){
						m.put("UMUR", n.get("CAT"));
						m.put("USIA_0_6", new BigDecimal(0));
						m.put("USIA_7_12", new BigDecimal(0));
						m.put("USIA_2_YR", new BigDecimal(0));
						m.put("USIA_3_YR", new BigDecimal(0));
						m.put("USIA_4_YR", new BigDecimal(0));
						m.put("USIA_5_YR", new BigDecimal(0));
						m.put("USIA_6_YR", new BigDecimal(0));
						m.put("USIA_ALL", new BigDecimal(0));
					}else{
						Map dt = (HashMap) tmp.get(index);
						
						m.put("UMUR", dt.get("UMUR"));
						m.put("USIA_0_6", dt.get("USIA_0_6"));
						m.put("USIA_7_12", dt.get("USIA_7_12"));
						m.put("USIA_2_YR", dt.get("USIA_2_YR"));
						m.put("USIA_3_YR", dt.get("USIA_3_YR"));
						m.put("USIA_4_YR", dt.get("USIA_4_YR"));
						m.put("USIA_5_YR", dt.get("USIA_5_YR"));
						m.put("USIA_6_YR", dt.get("USIA_6_YR"));
						m.put("USIA_ALL", dt.get("USIA_ALL"));
					}
					
					data.add(m);
				}
			}else if(jenis_report.equals("8")){//EX-GRATIA Claim
				report = "report.claim.selectreportExGratiaHealthClaim";
				data = uwManager.selectreportExGratiaHealthClaim(bdate, edate, currentUser.getLus_id(), jenis_periode);
			}else if(jenis_report.equals("9")){//By Product
				report = "report.claim.selectreportHealthClaimByProduct";
				data = uwManager.selectreportHealthClaimByProduct(bdate, edate, currentUser.getLus_id(), jenis_periode);
			}/*else if(jenis_report.equals("10")){//By SA*/
				/*List assure =  new ArrayList();
				String[] assure1 = { "up to  10",">10 - 25",">26 - 50",">51 - 100",">101 - 125",">126 - 150",">151 - 200",">201 - 250",">251 - 300",">301 - 350",
									">351 - 400",">401 - 450",">451 - 500",">501 - 600",">601 - 700",">701 - 800",">801 - 900",">901 - 1.000",">1.001 - 1.250",
									">1.251 - 1.500",">1.501 - 1.750",">1.751 - 2.000",">2.001 - 2.500",">2.501 - 3.000",">3.001 - 3.500",">3.501 - 4.000",">4.001 - 4.500",
									">4.501 - 5.000",">5.001 - 6.000",">6.001 - 7.000",">7.001 - 8.000",">8.001 - 9.000",">9.001 - 10.000",">10.000"};
				for(int z=0;z<assure1.length;z++){
					HashMap x = new HashMap();
					
					x.put("CAT", assure1[z]);
					
					assure.add(x);
				}
				report = "report.claim.selectreportClaimBySAKesehatan";
				List tmp = uwManager.selectreportClaimBySAKesehatan(bdate, edate, currentUser.getLus_id(), jenis_periode);
				
				for (int i=0;i<assure.size();i++){
					HashMap m = new HashMap();
					Map n = (HashMap) assure.get(i);
					
					Integer in = i +1;
					
					Integer index = null; 
					for(int j=0;j<tmp.size();j++){
						Map s = (HashMap) tmp.get(j);
						
						if(in == ((BigDecimal)s.get("URUT")).intValue()){
							index = j;
						}
					}
					
					if(index==null){
						m.put("URUT", new BigDecimal(in));
						m.put("ASSURED", n.get("CAT"));
						m.put("YR0_1", new BigDecimal(0));
						m.put("YR2_5", new BigDecimal(0));
						m.put("YR6_10", new BigDecimal(0));
						m.put("YR11_15", new BigDecimal(0));
						m.put("YR16_20", new BigDecimal(0));
						m.put("YR21_25", new BigDecimal(0));
						m.put("YR26_30", new BigDecimal(0));
						m.put("YR31_35", new BigDecimal(0));
						m.put("YR36_40", new BigDecimal(0));
						m.put("YR41_45", new BigDecimal(0));
						m.put("YR46_50", new BigDecimal(0));
						m.put("YR51_55", new BigDecimal(0));
						m.put("YR56_60", new BigDecimal(0));
						m.put("YR61_65", new BigDecimal(0));
						m.put("YR66_70", new BigDecimal(0));
						m.put("YR71_UP", new BigDecimal(0));
						m.put("YR_ALL", new BigDecimal(0));
					}else{
						Map dt = (HashMap) tmp.get(index);
						
						m.put("URUT", dt.get("URUT"));
						m.put("ASSURED", n.get("CAT"));
						m.put("YR0_1", dt.get("YR0_1"));
						m.put("YR2_5", dt.get("YR2_5"));
						m.put("YR6_10", dt.get("YR6_10"));
						m.put("YR11_15", dt.get("YR11_15"));
						m.put("YR16_20", dt.get("YR16_20"));
						m.put("YR21_25", dt.get("YR21_25"));
						m.put("YR26_30", dt.get("YR26_30"));
						m.put("YR31_35", dt.get("YR31_35"));
						m.put("YR36_40", dt.get("YR36_40"));
						m.put("YR41_45", dt.get("YR41_45"));
						m.put("YR46_50", dt.get("YR46_50"));
						m.put("YR51_55", dt.get("YR51_55"));
						m.put("YR56_60", dt.get("YR56_60"));
						m.put("YR61_65", dt.get("YR61_65"));
						m.put("YR66_70", dt.get("YR66_70"));
						m.put("YR71_UP", dt.get("YR71_UP"));
						m.put("YR_ALL", dt.get("YR_ALL"));
					}
					data.add(m);
				}*/
			/*}else if(jenis_report.equals("11")){//By Claim Paid
				List assure =  new ArrayList();
				String[] assure1 = { "up to  10",">10 - 25",">26 - 50",">51 - 100",">101 - 125",">126 - 150",">151 - 200",">201 - 250",">251 - 300",">301 - 350",
						">351 - 400",">401 - 450",">451 - 500",">501 - 600",">601 - 700",">701 - 800",">801 - 900",">901 - 1.000",">1.001 - 1.250",
						">1.251 - 1.500",">1.501 - 1.750",">1.751 - 2.000",">2.001 - 2.500",">2.501 - 3.000",">3.001 - 3.500",">3.501 - 4.000",">4.001 - 4.500",
						">4.501 - 5.000",">5.001 - 6.000",">6.001 - 7.000",">7.001 - 8.000",">8.001 - 9.000",">9.001 - 10.000",">10.000"};
				for(int z=0;z<assure1.length;z++){
					HashMap x = new HashMap();
					
					x.put("CAT", assure1[z]);
					
					assure.add(x);
				}
				report = "report.claim.selectreportClaimByPaid";
				List tmp = uwManager.selectreportClaimByPaid(bdate, edate, currentUser.getLus_id(), polis, grup_bank, nama_bank, jenis_periode);
				
				for (int i=0;i<assure.size();i++){
					HashMap m = new HashMap();
					Map n = (HashMap) assure.get(i);
					
					Integer in = i +1;
					
					Integer index = null; 
					for(int j=0;j<tmp.size();j++){
						Map s = (HashMap) tmp.get(j);
						
						if(in == ((BigDecimal)s.get("URUT")).intValue()){
							index = j;
						}
					}
					
					if(index==null){
						m.put("URUT", new BigDecimal(in));
						m.put("ASSURED", n.get("CAT"));
						m.put("YR0_1", new BigDecimal(0));
						m.put("YR2_5", new BigDecimal(0));
						m.put("YR6_10", new BigDecimal(0));
						m.put("YR11_15", new BigDecimal(0));
						m.put("YR16_20", new BigDecimal(0));
						m.put("YR21_25", new BigDecimal(0));
						m.put("YR26_30", new BigDecimal(0));
						m.put("YR31_35", new BigDecimal(0));
						m.put("YR36_40", new BigDecimal(0));
						m.put("YR41_45", new BigDecimal(0));
						m.put("YR46_50", new BigDecimal(0));
						m.put("YR51_55", new BigDecimal(0));
						m.put("YR56_60", new BigDecimal(0));
						m.put("YR61_65", new BigDecimal(0));
						m.put("YR66_70", new BigDecimal(0));
						m.put("YR71_UP", new BigDecimal(0));
						m.put("YR_ALL", new BigDecimal(0));
					}else{
						Map dt = (HashMap) tmp.get(index);
						
						m.put("URUT", dt.get("URUT"));
						m.put("ASSURED", n.get("CAT"));
						m.put("YR0_1", dt.get("YR0_1"));
						m.put("YR2_5", dt.get("YR2_5"));
						m.put("YR6_10", dt.get("YR6_10"));
						m.put("YR11_15", dt.get("YR11_15"));
						m.put("YR16_20", dt.get("YR16_20"));
						m.put("YR21_25", dt.get("YR21_25"));
						m.put("YR26_30", dt.get("YR26_30"));
						m.put("YR31_35", dt.get("YR31_35"));
						m.put("YR36_40", dt.get("YR36_40"));
						m.put("YR41_45", dt.get("YR41_45"));
						m.put("YR46_50", dt.get("YR46_50"));
						m.put("YR51_55", dt.get("YR51_55"));
						m.put("YR56_60", dt.get("YR56_60"));
						m.put("YR61_65", dt.get("YR61_65"));
						m.put("YR66_70", dt.get("YR66_70"));
						m.put("YR71_UP", dt.get("YR71_UP"));
						m.put("YR_ALL", dt.get("YR_ALL"));
					}
					data.add(m);
				}
			}*/else{
				ServletOutputStream err = response.getOutputStream();
				err.println("<script>alert('Error!');window.close();</script>");
				err.close();
			}
				
			if(data.size() > 0){ //bila ada data
	    		ServletOutputStream sos = response.getOutputStream();
	    		logger.info("Report "+props.getProperty(report));
	    		File sourceFile = Resources.getResourceAsFile(props.getProperty(report) + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);
	    		params.put("print", print);
	    		params.put("periode", periode);
	    		if(tc!=null){
	    			params.put("tc", tc.toString());
	    		}
	    		

	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));

		    	if(request.getParameter("showXLS") != null){
		    		response.setContentType("application/vnd.ms-excel");
		            JRXlsExporter exporter = new JRXlsExporter();
		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
		            exporter.exportReport();
		    	}else if(request.getParameter("showPDF") != null){
		    		JasperExportManager.exportReportToPdfStream(jasperPrint, sos);
		    	}
	    		sos.close();
    		}else{ //bila tidak ada data
    			ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Tidak ada data');window.close();</script>");
    			sos.close();
    		}
    	}else{
    		Map<String, Object> m = new HashMap<String, Object>();

    		Calendar cal = Calendar.getInstance();
    		cal.add(Calendar.DAY_OF_MONTH, 30);
    		Date sebulan = cal.getTime();
    		
        	m.put("bdate", defaultDateFormat.format(new Date()));
        	m.put("edate", defaultDateFormat.format(sebulan));
        	
        	return new ModelAndView("report/reportclaimAnalysisKesehatan", m);
    	}
    	
    	return null;
    }
    
    /**
     * Report PA BSIM request Jelita Samosir (helpdesk #46870)
     * @author Daru
     * @since Jan 30, 2014
     */
    public ModelAndView report_pa_bsim(HttpServletRequest request, HttpServletResponse response) throws Exception {
//    	Date sysdate = elionsManager.selectSysdate();
    	Date sysdate = new Date();
    	
		Report report;
		/*Map<Object, Object> mplist = null;
		List<Map> plist = new ArrayList<Map>();
		mplist = new HashMap<Object, Object>(); 
		mplist.put("KEY", "");
		mplist.put("VALUE", "ALL");
		plist.add(mplist);
		mplist = new HashMap<Object, Object>(); 
		mplist.put("KEY", "1");
		mplist.put("VALUE", "Ada");
		plist.add(mplist);
		mplist = new HashMap<Object, Object>(); 
		mplist.put("KEY", "0");
		mplist.put("VALUE", "Tidak");
		plist.add(mplist);*/
		report = new Report("Report PA BSIM", props.getProperty("report.uw.report_pa_bsim"), Report.PDF, null);
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		//report.addParamText("User", "name", 15, "", false);
		//report.addParamSelectWithoutAll("Polis", "epolis", plist, null, true);
		report.addParamDate("Periode", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		
		return prepareReport(request, response, report);		
    }
    
    public ModelAndView penolakan_medical(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String prodkes = ServletRequestUtils.getStringParameter(request, "prodkes", "");
		String plan = null,bisnisNumber = null;
		HashMap data = Common.serializableMap(uwManager.selectDataUsulan(spaj));
		Integer lsbs_id = (Integer) data.get("LSBS_ID");
		Integer lsdbs_number = (Integer) data.get("LSDBS_NUMBER");
		String lsdbs_name=(String)data.get("LSDBS_NAME");
		String nopol=(String)data.get("MSPO_POLICY_NO_FORMAT");		
		String cabang = elionsManager.selectCabangFromSpaj(spaj);
		String seq = "";
		report = new Report("Surat Penolakan SMiLe Medical", props.getProperty("report.surat_penolakan"), Report.PDF, null);
		
		
      	if(!prodkes.equals("") && !prodkes.equals("All")){
			plan=FormatString.getPlan(prodkes);
			bisnisNumber=FormatString.getBisnisNumber(prodkes);	
			seq=bacManager.sequenceNoSuratTolakKesehatan(170,"01");
			Map<String, Comparable> params = new HashMap<String, Comparable>();
			params.put("spaj", spaj);
			params.put("plan", plan);
			params.put("bisnisNumber", bisnisNumber);
			params.put("seq", seq);
			File dir = new File(
					props.getProperty("pdf.dir.endors") + "\\" + cabang + "\\" + spaj);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			String dest = dir +"\\";
			 File outputFile = new File(dest);
			
			Connection conn = null;
			try {
				//conn = this.getDataSource().getConnection();
				conn = this.getUwManager().getUwDao().getDataSource().getConnection();
				JasperUtils.exportReportToPdf(
							props.getProperty("report.surat_penolakan")+".jasper", 
							dest, 
							""+spaj+" ENDORS_SP_MEDICAL.pdf",  
							params, 
							conn, 
							PdfWriter.AllowPrinting, null, null);
			}catch(Exception e){
	            throw e;
			}finally{
				closeConnection(conn);
			}
			 
			elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "PRINT SURAT PENOLAKAN PERPANJANGAN PRODUK KESEHATAN ", spaj, 0);
		}
		
      	report.addParamDefault("No.Polis", "nopol", 30, nopol, true);
      	report.addParamDefault("SPAJ", "lsdbs_name", 30, lsdbs_name, true);
      	report.addParamDefault("Produk Utama", "spaj", 50, spaj, true);
		report.addParamSelect("Produk Kesehatan Yang Dibatalkan", "prodkes", bacManager.selectProdKes(spaj), "PILIH PRODUK", true);			
		report.addParamDefault("plan", "plan", 30, plan, false);
		report.addParamDefault("bisnisNumber", "bisnisNumber", 30, bisnisNumber, false);
		report.addParamDefault("seq", "seq", 100, seq, false);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
    
    /**
     * Report SPT dan NON SPT Simas Prima
     * E-Lions/report/uw.htm?window=reportuwsimasprima
     * @autor Rahmayanti
     * @since Oct, 08 2014
     */
    public ModelAndView reportUwSimasPrima(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	User currentUser=(User)request.getSession().getAttribute("currentUser");
    	HashMap hm=new HashMap();
    	if(request.getParameter("showReport")!=null)
    	{
    		String bdate=ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate=ServletRequestUtils.getStringParameter(request, "edate");
    		String simasPrima=ServletRequestUtils.getStringParameter(request, "SimasPrima");
    		String s_reps, jenis="";
    		ArrayList data=new ArrayList();
    		if(simasPrima.equals("0"))
    		{
    			
    			data=Common.serializableList(bacManager.reportSimasSpt(bdate, edate));
    			s_reps=props.getProperty("report.simas.spt");
    			jenis="LIST SPT SIMAS PRIMA";
    		}
    		else
    		{
    			data=Common.serializableList(bacManager.reportSimasNonSpt(bdate, edate));
    			s_reps=props.getProperty("report.simas.spt");
    			jenis="LIST NON SPT SIMAS PRIMA";
    		}
    		
    		if(data.size()>0)
    		{
    			ServletOutputStream out=response.getOutputStream();
    			File sourceFile = Resources.getResourceAsFile(s_reps + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
	    		HashMap<String, Object> map=new HashMap<String, Object>();
	    		map.put("bdate", bdate);
	    		map.put("edate", edate);
	    		map.put("SimasPrima", simasPrima);
	    		map.put("jenis", jenis);
	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(data));
	    		if(request.getParameter("showXLS")!=null)
	    		{
	    			response.setContentType("application/vnd.ms-excel");
		            JRXlsExporter exporter = new JRXlsExporter();
		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, out);
		            exporter.exportReport();	    			
	    		}
	    		else if(request.getParameter("showPDF")!=null)
	    			JasperExportManager.exportReportToPdfStream(jasperPrint, out);
	    		
    			if(out!=null)
    				out.close();
    			
    		}
    		else
        	{
        		ServletOutputStream out=response.getOutputStream();
        		out.println("<script>alert('Tidak ada data');window.close();</script>");
        		if(out!=null)
        			out.close();
        	}
    	}
    	return new ModelAndView("report/reportUwSimasPrima",hm);
    }
    
    /**
     * Report upload scan, req Mba Titis (helpdesk #58024) 
     * @author Daru
     * @since Oct 23, 2014
     */
    public ModelAndView report_upload_scan(HttpServletRequest request, HttpServletResponse response) throws Exception {
//    	Date today = bacManager.selectSysdate();
    	Date today = new Date();
    	User currentUser = (User) request.getSession().getAttribute("currentUser");
    	Report report = new Report("Report Upload Scan", props.getProperty("report.uw.report_upload_scan"), Report.PDF, null);
		
    	ArrayList<HashMap> statusList = new ArrayList<HashMap>();
		HashMap<String, Object> statusMap = new HashMap<String, Object>();
		statusMap.put("KEY", "0");
		statusMap.put("VALUE", "All");
		statusList.add(statusMap);
		statusMap = new HashMap<String, Object>();
		statusMap.put("KEY", "1");
		statusMap.put("VALUE", "Further Requirement");
		statusList.add(statusMap);
		statusMap = new HashMap<String, Object>();
		statusMap.put("KEY", "2");
		statusMap.put("VALUE", "Akseptasi Khusus");
		statusList.add(statusMap);
		report.addParamSelectWithoutAll("Status", "status", statusList, "All", true);
//    	report.addParamSelect("Status", "status", statusList, "All", true);
    	
    	ArrayList<HashMap> produkList = new ArrayList<HashMap>();
    	HashMap<String, Object> produkMap = new HashMap<String, Object>();
    	produkMap.put("KEY", "0");
    	produkMap.put("VALUE", "All");
    	produkList.add(produkMap);
    	produkMap = new HashMap<String, Object>();
    	produkMap.put("KEY", "1");
    	produkMap.put("VALUE", "Link");
    	produkList.add(produkMap);
    	produkMap = new HashMap<String, Object>();
    	produkMap.put("KEY", "2");
    	produkMap.put("VALUE", "Non Link");
    	produkList.add(produkMap);
    	report.addParamSelectWithoutAll("Produk", "produk", produkList, "All", true);
//    	report.addParamSelect("Produk", "produk", produkList, "All", true);
    	
    	report.addParamDate("Tanggal", "tgl", true, new Date[] { today, today }, true);
		report.setReportQueryMethod("select_report_upload_scan");
		
		return prepareReport(request, response, report);
//    	HashMap map = new HashMap();
//    	String today = defaultDateFormat.format(uwManager.selectSysdateTruncated(0));
//    	String tglAwal = ServletRequestUtils.getStringParameter(request, "tglAwal", today);
//    	String tglAkhir = ServletRequestUtils.getStringParameter(request, "tglAkhir", today);
//    	map.put("tglAwal", tglAwal);
//    	map.put("tglAkhir", tglAwal);
    	
//    	if(request.getParameter("showReport") != null) {
//    		Integer status = ServletRequestUtils.getIntParameter(request, "status", 0);
//    		Integer tipe = ServletRequestUtils.getIntParameter(request, "product", 0);
//    		
//    		ArrayList dataSource = Common.serializableList(bacManager.select_report_upload_scan(status, tipe, tglAwal, tglAkhir));
//    		
//    		if(dataSource.size() > 0) {
//    			HashMap params = new HashMap();
//        		params.put("status", status);
//        		params.put("tipe", tipe);
//        		params.put("tglAwal", tglAwal);
//        		params.put("tglAkhir", tglAkhir);
//        		
//        		ServletOutputStream os = response.getOutputStream();
//        		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.uw.report_upload_scan") + ".jasper");
//        		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
//        		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(dataSource));
//        		
//        		if(request.getParameter("showPDF") != null)
//        			JasperExportManager.exportReportToPdfStream(jasperPrint, os);
//        		else if(request.getParameter("showXLS") != null) {
//        			response.setContentType("application/vnd.ms-excel");
//        			JRXlsExporter exporter = new JRXlsExporter();
//        			exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
//        			exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, os);
//        			exporter.exportReport();
//        		}
//        		
//        		if(os != null)
//        			os.close();
//    		} else {
//    			ServletOutputStream out=response.getOutputStream();
//        		out.println("<script>alert('Tidak ada data');window.close();</script>");
//        		if(out!=null)
//        			out.close();
//    		}
//    		
//    		return null;
//    	}
//    	
//    	return new ModelAndView("report/report_upload_scan", map);
    }
    
    /**
     * Report UW & Collection, req  Rizkiyano & Ningrum (helpdesk #57344) 
     * @author Lufi
     * @since Oct 30, 2014
     *
     */
    //Update (27/02/2015)-Tambah report khusus danamas/power save syariah
    public ModelAndView report_uw_collection(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser=(User)request.getSession().getAttribute("currentUser");
    	HashMap map = new HashMap();
    	Report report;
    	ArrayList<Map> jenis = new ArrayList<Map>();
		HashMap tmp = new HashMap();
		tmp.put("KEY", "1");
		tmp.put("VALUE", "SIMAS PRIMA");
		jenis.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "2");
		tmp.put("VALUE", "DANAMAS PRIMA/POWERSAVE SYARIAH");
		jenis.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "3");
		tmp.put("VALUE", "LAIN-LAIN");
		jenis.add(tmp);
//		Date sysDate = elionsManager.selectSysdate();
    	HashMap mapReport = new HashMap();
    	
    	mapReport=bacManager.selectMstConfig(6, "Report UW Collection", "report.uw.report_uw_collection");
		report = new Report("Report UW & Collection",mapReport.get("NAME").toString(), Report.PDF, null);
		report.addParamSelect("jenis", "jenis", jenis, "ALL", true);
		report.addParamDefault("username", "username", 0, currentUser.getName(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		report.setReportQueryMethod("selectReportUwCollection");    	
		return prepareReport(request, response, report);
    }
    
    /**
     * Report error edit bac
     * E-Lions/report/uw.htm?window=reporterror
     * @autor Rahmayanti
     * @since Des, 01 2014
     */
    public ModelAndView reportError(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	User currentUser=(User)request.getSession().getAttribute("currentUser");
//    	Date sysDate = elionsManager.selectSysdate(); 
    	Date sysDate = new Date();
    	HashMap hm = new HashMap();
    	
    	if(request.getParameter("showReport")!=null)
    	{
    		String bdate=ServletRequestUtils.getStringParameter(request, "bdate", FormatDate.toString(sysDate));
    		String edate=ServletRequestUtils.getStringParameter(request, "edate", FormatDate.toString(sysDate));
    		String s_reps= props.getProperty("report.error");
    		ArrayList data=new ArrayList();
			data=Common.serializableList(bacManager.reportError(bdate, edate));
    		if(data.size()>0)
    		{
    			ServletOutputStream out=response.getOutputStream();
    			File sourceFile = Resources.getResourceAsFile(s_reps + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
	    		HashMap<String, Object> map=new HashMap<String, Object>();
	    		map.put("bdate", bdate);
	    		map.put("edate", edate);
	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(data));
	    		if(request.getParameter("showXLS")!=null)
	    		{
	    			response.setContentType("application/vnd.ms-excel");
		            JRXlsExporter exporter = new JRXlsExporter();
		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, out);
		            exporter.exportReport();	    			
	    		}
	    		else if(request.getParameter("showPDF")!=null)
	    			JasperExportManager.exportReportToPdfStream(jasperPrint, out);
	    		
    			if(out!=null)
    				out.close();
    			
    		}
    		else
        	{
        		ServletOutputStream out=response.getOutputStream();
        		out.println("<script>alert('Tidak ada data');window.close();</script>");
        		if(out!=null)
        			out.close();
        	}
    		return null;
    	}    	
    	return new ModelAndView("report/reportError",hm);
    }
    
    /**
     * Report Pending Simas Series, req Preselia (helpdesk #57344), req Feri 
     * @author Lufi, Rahmayanti
     * @since jan 22, 2015
     * E-Lions/report/uw.htm?window=pending_simas
     */
    public ModelAndView pending_simas(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		Date sysDate = elionsManager.selectSysdate();
    	HashMap mapReport = new HashMap();
    	mapReport=bacManager.selectMstConfig(6, "Report Pending Simas Series", "report.pending.simas");
		report = new Report("Report Pending Simas Series",mapReport.get("NAME").toString(), Report.PDF, null);
		report.addParamDefault("username", "username", 0, currentUser.getName(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		
		return prepareReport(request, response, report);
	}
    
    public ModelAndView reportUWIndividuNew(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser=(User)request.getSession().getAttribute("currentUser");
    	HashMap map = new HashMap();
    	Report report;
    	ArrayList<Map> jenis = new ArrayList<Map>();
		HashMap tmp = new HashMap();	
		tmp = new HashMap();
		tmp.put("KEY", "1");
		tmp.put("VALUE", "UW PROSES");
		jenis.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "2");
		tmp.put("VALUE", "UW HELPDESK");
		jenis.add(tmp);
//		Date sysDate = elionsManager.selectSysdate();
    	HashMap mapReport = new HashMap();
    	
    	mapReport=bacManager.selectMstConfig(6, "Report U/W Individu", "report.uw.individu");
		report = new Report("Report U/W Individu",mapReport.get("NAME").toString(), Report.PDF, null);
		report.addParamSelect("jenis", "jenis", jenis, "ALL", true);
		report.addParamDefault("username", "username", 0, currentUser.getName(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		report.setReportQueryMethod("selectreportUWIndividu");    	
		return prepareReport(request, response, report);
    }
    
    //Rahmayanti - Report QC
    public ModelAndView reportQc(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		Date sysdate = elionsManager.selectSysdate();		
		Date sysdate = new Date();
		if(request.getParameter("showReport")!=null)
    	{
			String bdate=ServletRequestUtils.getStringParameter(request, "bdate",FormatDate.toString(sysdate));
			String edate=ServletRequestUtils.getStringParameter(request, "edate",FormatDate.toString(sysdate));
    		String s_reps= props.getProperty("report.uw.qc");
    		ArrayList data=new ArrayList();
			data=Common.serializableList(bacManager.reportQc(bdate, edate));
    		if(data.size()>0)
    		{
    			ServletOutputStream out=response.getOutputStream();
    			File sourceFile = Resources.getResourceAsFile(s_reps + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
	    		map.put("bdate", bdate);
	    		map.put("edate", edate);
	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(data));
	    		if(request.getParameter("showXLS")!=null)
	    		{
	    			response.setContentType("application/vnd.ms-excel");
		            JRXlsExporter exporter = new JRXlsExporter();
		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, out);
		            exporter.exportReport();	    			
	    		}
	    		else if(request.getParameter("showPDF")!=null)
	    			JasperExportManager.exportReportToPdfStream(jasperPrint, out);
	    		
    			if(out!=null)
    				out.close();
    			
    		}
    		else
        	{
        		ServletOutputStream out=response.getOutputStream();
        		out.println("<script>alert('Tidak ada data');window.close();</script>");
        		if(out!=null)
        			out.close();
        	}
    	}    
		return new ModelAndView("uw/report_qc", map);
	}
    
    /**
     * Report SL Polis Diterima (helpdesk #69037)
     * @author Lufi
     * @since April 16, 2015
     * E-Lions/report/uw.htm?window=slpolis_diterima
     */
    public ModelAndView slpolis_diterima(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		Date sysDate = elionsManager.selectSysdate();
    	HashMap mapReport = new HashMap();
    	mapReport=bacManager.selectMstConfig(6, "Report SL POLIS DITERIMA", "report.sl.polisditerima");
		report = new Report("Report SL POLIS DITERIMA",mapReport.get("NAME").toString(), Report.PDF, null);
		report.addParamDefault("username", "username", 0, currentUser.getName(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		
		return prepareReport(request, response, report);
	}
    
    public ModelAndView reportAutomatedUw(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		Date sysdate = elionsManager.selectSysdate();	 //2017-07-18 00:00:00.0	
		Date sysdate = new Date();
		if(request.getParameter("showReport")!=null)
    	{
			String bdate=ServletRequestUtils.getStringParameter(request, "bdate",FormatDate.toString(sysdate));
			String edate=ServletRequestUtils.getStringParameter(request, "edate",FormatDate.toString(sysdate));
    		String s_reps= props.getProperty("report.uw.automated");
    		ArrayList data=new ArrayList();
			data=Common.serializableList(bacManager.reportAutomatedUw(bdate, edate));
    		if(data.size()>0)
    		{
    			ServletOutputStream out=response.getOutputStream();
    			File sourceFile = Resources.getResourceAsFile(s_reps + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
	    		map.put("bdate", bdate);
	    		map.put("edate", edate);
	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(data));
	    		if(request.getParameter("showXLS")!=null)
	    		{
	    			response.setContentType("application/vnd.ms-excel");
		            JRXlsExporter exporter = new JRXlsExporter();
		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, out);
		            exporter.exportReport();	    			
	    		}
	    		else if(request.getParameter("showPDF")!=null)
	    			JasperExportManager.exportReportToPdfStream(jasperPrint, out);
	    		
    			if(out!=null)
    				out.close();
    			
    		}
    		else
        	{
        		ServletOutputStream out=response.getOutputStream();
        		out.println("<script>alert('Tidak ada data');window.close();</script>");
        		if(out!=null)
        			out.close();
        	}
    	}    
		return new ModelAndView("uw/report_automated", map);
	}
    
    /**
     * Report Declined req Hayatin (#69889)
     * @author Daru
     * @since May 12, 2015
     */
    public ModelAndView report_spaj_declined(HttpServletRequest request, HttpServletResponse response) throws Exception {
//    	Date today = bacManager.selectSysdate();
    	Date today = new Date();
    	User currentUser = (User) request.getSession().getAttribute("currentUser");
    	Report report = new Report("Report Declined", props.getProperty("report.uw.spaj_declined"), Report.PDF, null);
    	report.addParamDate("Tanggal Proses SPAJ", "tanggal", true, new Date[] { today, today }, true);
		report.addParamDefault("User", "user_id", 37, currentUser.getName(), true);
		
		return prepareReport(request, response, report);
    }
    
    /**
     * Report SPAJ PENDING req Hayatin (#87877)
     * @author Randy
     * @since April 15, 2016
     */
    public ModelAndView report_spaj_pending(HttpServletRequest request, HttpServletResponse response) throws Exception {
//    	Date today = bacManager.selectSysdate();
    	Date today = new Date();
    	User currentUser = (User) request.getSession().getAttribute("currentUser");
    	Report report = new Report("Report SPAJ Pending", props.getProperty("report.uw.spaj_pending"), Report.PDF, null);
    	report.addParamDate("Tanggal Proses SPAJ", "tanggal", true, new Date[] { today, today }, true);
		report.addParamDefault("User", "user_id", 37, currentUser.getName(), true);
		
		return prepareReport(request, response, report);
    }
    
    /**
     * Report Komisi Belum di Proses / TTP belum di proses
     * req Timmy (#74409)
     * @author Daru
     * @Since July 24, 2015
     */
    public ModelAndView report_uw_komisi_belum_proses(HttpServletRequest request, HttpServletResponse response) throws Exception {
//    	Date today = bacManager.selectSysdate();
    	Date today = new Date();
    	Report report = new Report("Report Komisi Belum di Proses", props.getProperty("report.uw.komisi_belum_proses"), Report.PDF, null);
    	report.addParamDate("Tanggal Scan TTP", "tanggal", true, new Date[] { today, today }, true);    	
    	return prepareReport(request, response, report);
    }
    
    public ModelAndView report_snows_nb(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser=(User)request.getSession().getAttribute("currentUser");
    	HashMap map = new HashMap();
    	Report report;
    	ArrayList<Map> jenis = new ArrayList<Map>();
		HashMap tmp = new HashMap();
		tmp.put("KEY", "1");
		tmp.put("VALUE", "SIMAS SERIES");
		jenis.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "2");
		tmp.put("VALUE", "NON-SIMAS SERIES");
		jenis.add(tmp);		
//		Date sysDate = elionsManager.selectSysdate();
    	HashMap mapReport = new HashMap();
    	
    	mapReport=bacManager.selectMstConfig(6, "Report SNOWS NB", "report.uw.report_snows_nb");
		report = new Report("Report SNOWS NB",mapReport.get("NAME").toString(), Report.PDF, null);
		report.addParamSelect("Jenis", "jenis", jenis, "ALL", true);
		report.addParamDefault("username", "username", 0, currentUser.getName(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);	
		//report.addParamDate("Tanggal", "tanggal", false, new Date[] {sysDate}, false);
		report.setReportQueryMethod("selectReportSnowsNB");    	
		return prepareReport(request, response, report);
    }
    
    //Ryan - Report Stock SPAJ
    public ModelAndView reportStockSpaj(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		Date sysdate = elionsManager.selectSysdate();		
		Date sysdate = new Date();
	
			String bdate=ServletRequestUtils.getStringParameter(request, "bdate",FormatDate.toString(sysdate));
			String edate=ServletRequestUtils.getStringParameter(request, "edate",FormatDate.toString(sysdate));
			String format=ServletRequestUtils.getStringParameter(request, "format","");
			String flag=ServletRequestUtils.getStringParameter(request, "flag","");
    		String s_reps= props.getProperty("report.uw.stockSPAJ");
    		ArrayList data=new ArrayList();
			data=Common.serializableList(bacManager.selectStokSPAJSummary(bdate, edate,Integer.parseInt(flag)));
    		if(data.size()>0)
    		{
    			ServletOutputStream out=response.getOutputStream();
    			File sourceFile = Resources.getResourceAsFile(s_reps + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
	    		map.put("bdate", bdate);
	    		map.put("edate", edate);
	    		map.put("flag", flag);
	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(data));
	    		if(format.equals("excel"))
	    		{
	    			response.setContentType("application/vnd.ms-excel");
		            JRXlsExporter exporter = new JRXlsExporter();
		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, out);
		            exporter.exportReport();	    			
	    		}
	    		else if(format.equals("pdf"))
	    			JasperExportManager.exportReportToPdfStream(jasperPrint, out);
	    		
    			if(out!=null)
    				out.close();
    		}
    		else
        	{
        		ServletOutputStream out=response.getOutputStream();
        		out.println("<script>alert('Tidak ada data');window.close();</script>");
        		if(out!=null)
        			out.close();
        	}
    	    
		return null;
	}
    
    public ModelAndView report_sla_uwIndividu(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser=(User)request.getSession().getAttribute("currentUser");
    	HashMap map = new HashMap();
    	Report report;
    	ArrayList<Map> jenis = new ArrayList<Map>();
		HashMap tmp = new HashMap();
		tmp.put("KEY", "1");
		tmp.put("VALUE", "HANIFAH");
		jenis.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "2");
		tmp.put("VALUE", "HAYATIN");
		jenis.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "3");
		tmp.put("VALUE", "SUPRIYATI");
		jenis.add(tmp);		
//		Date sysDate = elionsManager.selectSysdate();
    	HashMap mapReport = new HashMap();    	
    	mapReport = bacManager.selectMstConfig(6, "Report SLA UW INDIVIDU", "report.uw.sla_individu");
		report = new Report("Report SLA UW INDIVIDU",mapReport.get("NAME").toString(), Report.PDF, null);
		report.addParamSelect("Jenis", "jenis", jenis, "ALL", true);
		report.addParamDefault("username", "username", 0, currentUser.getName(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		report.setReportQueryMethod("selectReportSlaUwIndividu");
		return prepareReport(request, response, report);
    }
    
    /**
     * Report Production NB (helpdesk #78317)
     * @author Lufi
     * @since Oktober 23, 2015
     * E-Lions/report/uw.htm?window=report_production_nb
     */
    
    public ModelAndView report_production_nb(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		Date sysDate = elionsManager.selectSysdate();
    	HashMap mapReport = new HashMap();
    	mapReport=bacManager.selectMstConfig(6, "Report Production NB", "report.prod.nb");
		report = new Report("Report Production NB",mapReport.get("NAME").toString(), Report.PDF, null);
		report.addParamDefault("username", "username", 0, currentUser.getName(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		
		return prepareReport(request, response, report);
    	
    }
    
    /**
     * Report UW Pending Input Tanggal Kirim Polis
     * req Timmy (#78527)
     * @author Daru
     * @since Nov 5, 2015
     */
    public ModelAndView report_pending_kirim_polis(HttpServletRequest request, HttpServletResponse response) throws Exception {
//    	Date today = bacManager.selectSysdate();
    	Date today = new Date();
    	HashMap map = bacManager.selectMstConfig(6, "Report Pending Kirim Polis", "report.uw.pending_kirim_polis");
    	Report report = new Report("Report Pending Kirim Polis", map.get("NAME").toString(), Report.PDF, null);
    	report.addParamDate("Tanggal Akseptasi", "tanggal", true, new Date[] { today, today }, true);
    	
    	return prepareReport(request, response, report);
    }
    
    /**
     * Report Total SMS Out Per Bulan
     * @author Daru
     * @since Jan 5, 2016
     */
    public ModelAndView report_total_sms_out_bulanan(HttpServletRequest request, HttpServletResponse response) throws Exception {
//    	Date today = bacManager.selectSysdate();
    	Date today = new Date();
    	DateFormat mf = new SimpleDateFormat("MM");
    	DateFormat yf = new SimpleDateFormat("yyyy");
    	String month = ServletRequestUtils.getStringParameter(request, "month", mf.format(today));
    	String year = ServletRequestUtils.getStringParameter(request, "year", yf.format(today));
    	ArrayList<HashMap<String, Object>> data = bacManager.selectReportTotalSmsOutBulanan(month, year);
    	
    	if(data.size() > 0) {
    		HashMap<String, Object> reportParam = new HashMap<String, Object>();
			reportParam.put("periodDate", new SimpleDateFormat("MMyyyy").parse(month + year));
			reportParam.put("month", month);
			reportParam.put("year", year);
    		
    		ServletOutputStream os = response.getOutputStream();
    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report_total_sms_out_bulanan") + ".jasper");
    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportParam, new JRBeanCollectionDataSource(data));
    		
    		if(request.getParameter("showPDF") != null)
    			JasperExportManager.exportReportToPdfStream(jasperPrint, os);
    		else if(request.getParameter("showXLS") != null) {
    			response.setContentType("application/vnd.ms-excel");
    			JRXlsExporter exporter = new JRXlsExporter();
    			exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
    			exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, os);
    			exporter.exportReport();
    		}
    		
    		if(os != null)
    			os.close();
		} else {
			ServletOutputStream out=response.getOutputStream();
    		out.println("<script>alert('Tidak ada data');window.close();</script>");
    		if(out!=null)
    			out.close();
		}
    	
    	return null;
    }
  
    /**
     * @author Ridhaal
     * @since 13 Jun 2016
     * @category Report Dana Sejahtera DMTM Collection
     * @param Produk (all data)
     * http://localhost/E-Lions/report/uw.htm?window=report_dana_sejahtera_dmtm
     * link menu = report/uw.htm?window=report_dana_sejahtera_dmtm
     */
    public ModelAndView report_dana_sejahtera_dmtm(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Report report;
		report = new Report("Report Dana Sejahtera DMTM",props.getProperty("report.uw.report_dana_sejahtera_dmtm"),Report.PDF, null);
		report.addParamSelect("Produk", "produk", bacManager.selectProductDMTMDS(), null, true);
		report.addParamText("Kode Agen", "kdagen", 7, "027199" , true);
		report.setReportQueryMethod("selectReportProductDMTMDS");
		return prepareReport(request, response, report);
								
    }   
    
    /**
     * @author Ridhaal
     * @since 08 Sept 2016
     * @category Report Further dan Non Further Collection
     * @param by tanggal awal dan akhir transfer to bas
     * http://localhost/E-Lions/report/uw.htm?window=report_collection
     * link menu = report/uw.htm?window=report_collection
     */
	public ModelAndView report_collection(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
//	    Date sysDate = elionsManager.selectSysdate();
	    Date sysDate = new Date();
	        
	        List<DropDown> reportPathList = new ArrayList<DropDown>();
	        
	        	reportPathList.add( new DropDown(
	                props.getProperty( "report.further_collection" ),
	                "FURTHER COLLECTION"
	        	) );
	        	reportPathList.add( new DropDown(
	                props.getProperty( "report.nonfurther_collection" ),
	                "NON FURTHER COLLECTION"
	        	) );
	        	
	                
    	report = new Report( "Report Collection", reportPathList, Report.PDF, null );
    	
		report.addParamDefault("username", "username", 200, currentUser.getLus_full_name(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
    	report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
    	return prepareReport( request, response, report );
	}
	
    /**
     * report_spaj_freeze (helpdesk #102144)
     * @author Randy
     */
    
  public ModelAndView report_spaj_freeze(HttpServletRequest request, HttpServletResponse response) throws Exception {
	User currentUser = (User) request.getSession().getAttribute("currentUser");
	Report report = new Report("Report SPAJ Freeze", props.getProperty("report.uw.spaj_freeze"), Report.PDF, null);
//	report.addParamDefault("username", "username", 0, currentUser.getName(), false);
//	report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
	report.addParamDate("Tanggal Input", "tanggal", true, new Date[] {new Date(), new Date()}, true);
	return prepareReport(request, response, report);
  }
	
    /**
     * @author Ridhaal
     * @since 06 Apr 2017
     * @category Report Free Smile Medical (buy 1 get 1)
     * @param by kanwil, produk, tanggal awal dan akhir transfer to bas
     * http://localhost/E-Lions/report/uw.htm?window=report_freeSmileMedical
     * link menu = report/uw.htm?window=report_freeSmileMedical
     */
	public ModelAndView report_freeSmileMedical(HttpServletRequest request, HttpServletResponse response) throws Exception {
						
		Report report;
		report = new Report("Print Polis", props.getProperty("report.free_smilemedical"), Report.PDF, null);
		report.addParamSelect("Kantor Wilayah", "kanwil", bacManager.selectKanwil711(), null, true);
		report.addParamSelect("produk", "produk", bacManager.selectProducFreeP1(), null, true);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		report.setReportQueryMethod("selectReportProductFreeSmileM");
		return prepareReport(request, response, report);
	}
	
	/**Fungsi : Untuk Menampilkan report Daily Monitoring AMLCFT Monitoring
	Keterangan :	
	Polis yang dikeluarkan dalam report ini adalah polis dengan  :	
	-total maksimal high risk = 6 level yang terdiri dari nama pemegang polis yang masuk attention list dengan keterangan ada di 
	-list teroris, pekerjaan nasabah high risk, jabatan nasabah high risk, sumber dana high risk, asal negara high risk, bidang usaha high risk.
	-report/uw.htm?window=report_AMLCFT_Monitoring
	* @param request
	* @param response
	* @return
	* @throws Exception
	* @author Ridhaal
	*/
	public ModelAndView report_AMLCFT_Monitoring (HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("Report AMLCFT Monitoring", props.getProperty("report.report_AMLCFT_Monitoring"), Report.PDF, null);
		//Date sysdate=elionsManager.selectSysdate();
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		
		Date tglAwal=null,tglAkhir=null;
		if(tanggalAwal!=null)
		tglAwal=FormatDate.toDateWithSlash(tanggalAwal);
		if(tanggalAkhir!=null)
		tglAkhir=FormatDate.toDateWithSlash(tanggalAkhir);
		report.addParamDate("Tanggal Input SPAJ", "tanggal", true, new Date[] {tglAwal, tglAkhir}, true);
		report.setResultList(Common.serializableList(bacManager.selectlistAMLCFT_Monitoring(tanggalAwal,tanggalAkhir)));
		
		return prepareReport(request, response, report);
	}  
}