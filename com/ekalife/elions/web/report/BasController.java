package com.ekalife.elions.web.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.ClientHistory;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Mia;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.ExcelRead;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.jasper.Report;
import com.ekalife.utils.parent.ParentJasperReportingController;
import com.google.gson.Gson;
import com.ibatis.common.resources.Resources;

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
 * Report2 yang digunakan di modul BAS (branch admin support)
 * @author Yusuf Sutarko
 * @since Feb 19, 2007 (3:05:35 PM)
 */
public class BasController extends ParentJasperReportingController{
	protected final Log logger = LogFactory.getLog( getClass() );
	/**
	 * Report Summary Input Cross-Selling
	 * @author Yusuf
	 * @since Aug 15, 2008 (10:59:36 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView cross_selling_input(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysdate = elionsManager.selectSysdate();
		
		Report report = new Report("Summary Input Cross-Selling",props.getProperty("report.cs.summary_input"),Report.PDF, null);
		report.addParamDate("Tanggal Input", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		report.addParamDefault("cabang", "lca_id", 0, currentUser.getCabang(), false);
		report.addParamDefault("lus_id", "lus_id", 0, currentUser.getLus_id(), false);
		report.addParamDefault("username", "username", 0, currentUser.getName(), false);
		
		return prepareReport(request, response, report);
	}
	
	/**
	 * Report Summary Komisi Cross-Selling
	 * @author Yusuf
	 * @since Aug 15, 2008 (10:59:36 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView cross_selling_komisi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysdate = elionsManager.selectSysdate();
		
		Report report = new Report("Summary Komisi Cross-Selling",props.getProperty("report.cs.summary_komisi"),Report.PDF, null);
		report.addParamDate("Tanggal Produksi", "tanggal", true, new Date[] {sysdate, sysdate}, true);
		report.addParamDefault("cabang", "lca_id", 0, currentUser.getCabang(), false);
		report.addParamDefault("lus_id", "lus_id", 0, currentUser.getLus_id(), false);
		report.addParamDefault("username", "username", 0, currentUser.getName(), false);
		
		return prepareReport(request, response, report);		
	}
	
	/**
	 * Sistem Kontrol SPAJ - Laporan Outstanding SPAJ di Cabang
	 * @author Yusuf Sutarko
	 * @since Mar 22, 2007 (9:49:43 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView outstanding_cabang(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id");
		String lus_id=ServletRequestUtils.getStringParameter(request, "lus_id");
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		
		Report report;
		if(lca_id.equals("0")){
			report = new Report("Laporan Outstanding SPAJ Semua Cabang",props.getProperty("report.bas.outstanding_spaj_all_cabang"),Report.PDF, null);
		}else{
			if(lus_id.equals("0"))
				report = new Report("Laporan Outstanding SPAJ per Branch Admin",props.getProperty("report.bas.outstanding_spaj_cabang_new"),Report.PDF, null);
			else
				report = new Report("Laporan Outstanding SPAJ per Branch Admin",props.getProperty("report.bas.outstanding_spaj_cabang_admin"),Report.PDF, null);
		}	
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		report.addParamDefault("lca_id", "lca_id", 0,lca_id, false);
		report.addParamDefault("lus_id", "lus_id", 0, lus_id, false);
		//report.setLink("javascript:formpost.submit();");
		report.addParamDefault("lca_id", "lca_id", 0,lca_id,  false);
		
		return prepareReport(request, response, report);
	}

	/**
	 * Sistem Kontrol SPAJ - Laporan Outstanding SPAJ per Agen di Cabang
	 * @author Yusuf Sutarko
	 * @since Mar 22, 2007 (9:50:01 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView outstanding_agen(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id");
		String msag_id=ServletRequestUtils.getStringParameter(request, "msag_id");
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tgl1");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tgl2");
		Report report;
		Date sysDate = elionsManager.selectSysdate();
		report = new Report("Laporan Outstanding SPAJ per Agen",props.getProperty("report.bas.outstanding_spaj_agen"),Report.PDF, null);
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		report.addParamDefault("msag_id", "msag_id", 0,msag_id, false);
		report.addParamDefault("lus_id", "lus_id", 0, currentUser.getLca_id(), false);
		report.setLink("javascript:formpost.submit();");
		report.addParamDefault("lca_id", "lca_id", 0,lca_id,  false);

		return prepareReport(request, response, report);
	}

	/**
	 * Sistem Kontrol SPAJ - Laporan Pemakaian SPAJ per Agen
	 * @author Yusuf Sutarko
	 * @since Mar 22, 2007 (9:50:13 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView spaj_agen(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		Date sysDate = elionsManager.selectSysdate();
		report = new Report("Formulir Permintaan SPAJ ke Kantor Pusat",props.getProperty("report.form_spaj"), Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true,new Date[] {sysDate, sysDate}, true);
		//kalo reportQueryMethod tidak di-set, maka report akan mengambil data langsung dari database
		//report.setReportQueryMethod("");

		return prepareReport(request, response, report);
	}

	/**
	 * Sistem Kontrol SPAJ - Laporan Hilang SPAJ 
	 * @author Yusuf Sutarko
	 * @since Mar 22, 2007 (9:50:27 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView spaj_hilang(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Report report;
		Date sysDate = elionsManager.selectSysdate();
		report = new Report("Laporan SPAJ Hilang",props.getProperty("report.bas.spaj_hilang"),Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true,new Date[] {sysDate, sysDate}, true);
		return prepareReport(request, response, report);
	}

    /**
	 * Sistem Kontrol SPAJ - Covering Letter GA
	 * @author Ferry Harlim
	 * @since May 15, 2007 (9:15:10 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView covering_letter_ga(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		String msf_id=ServletRequestUtils.getStringParameter(request, "msf_id");
		report = new Report("Covering Letter GA",props.getProperty("report.bas.covering_letter_ga"),Report.PDF, null);
		report.addParamDefault("msf_id", "msf_id", 0, msf_id, false);
		return prepareReport(request, response, report);
	}

	/**
	 * Sistem Kontrol SPAJ - Form Permintaan SPAJ di Cabang
	 * @author Ferry Harlim
	 * @since May 16, 2007 (13:35:00 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView form_permintaan_spaj_cabang(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		String msf_id=ServletRequestUtils.getStringParameter(request, "msf_id",null);
		report = new Report("Form Permintaan SPAJ",props.getProperty("report.bas.form_permintaan_spaj"),Report.PDF, null);
		report.addParamDefault("msf_id", "msf_id", 0, msf_id, false);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView form_status_marketing (HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String kode=ServletRequestUtils.getStringParameter(request, "kode","");
		String keterangan=ServletRequestUtils.getStringParameter(request, "keterangan","");
		String nama_leader=ServletRequestUtils.getStringParameter(request, "nama_leader","");
		String jabatan=ServletRequestUtils.getStringParameter(request, "jabatan","");
		String nama_admin=ServletRequestUtils.getStringParameter(request, "nama_admin","");
		report = new Report("Form Status Marketing",props.getProperty("report.bas.form_status_marketing"),Report.PDF, null);
		ArrayList<Map> jenis = new ArrayList<Map>();
		Map tmp = new HashMap();
		tmp.put("KEY", "1");
		tmp.put("VALUE", "PERPANJANG KONTRAK");
		jenis.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "2");
		tmp.put("VALUE", "TURUN LEVEL");
		jenis.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "3");
		tmp.put("VALUE", "TERMINATE");
		jenis.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "4");
		tmp.put("VALUE", "NAIK LEVEL");
		jenis.add(tmp);
		report.addParamText("Kode", "kode", 20, kode, true);
		report.addParamSelectWithoutAll("Status", "jenis", jenis, "", false);
		report.addParamText("Keterangan", "keterangan",100, keterangan, true);
		report.addParamText("Nama Leader", "nama_leader", 40, nama_leader, true);
		report.addParamText("Jabatan", "jabatan", 20, jabatan, true);
		report.addParamText("Nama Admin", "nama_admin", 40, nama_admin, true);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView fitrah_card(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Report report;
		String msf_id=ServletRequestUtils.getStringParameter(request, "msf_id","");
		String jenis=ServletRequestUtils.getStringParameter(request, "jenis", "");
		
		report = new Report("Fitrah Card",props.getProperty("report.bas.fitrah_card"),Report.PDF, null);
		if(jenis.equals("pusat")){
			report.addParamDefault("msf_id", "msf_id", 0, msf_id, false);
			//report.addParamDefault("jenis", "jenis", 0, jenis, false);
			//report.setReportQueryMethod("selectReportFitrahCard");
		}else if(jenis.equals("agen")){
			report = new Report("Fitrah Card Agen",props.getProperty("report.bas.fitrah_card_agen"),Report.PDF, null);
			String msag_id = ServletRequestUtils.getStringParameter(request, "msag_id", "");
			String no_blanko = ServletRequestUtils.getStringParameter(request, "no_blanko", "");
			//(update tgl 29-4-2009)no_blanko didapat dari no_form yg sudah pernah tersimpan sebelumnya.
			if(!msf_id.equals("")){
				no_blanko = uwManager.selectMstformGetNoBlanko(msf_id);
			}
			Agen agen = elionsManager.selectAgentByMsagId(msag_id);
			//String msab_nama = agen.getMsab_nama();
			report.addParamDefault("msab_nama", "msab_nama", 0, agen.getMsab_nama(), false);
			report.addParamDefault("msf_id", "msf_id", 0, msf_id, false);
			report.addParamDefault("msag_id", "msag_id", 0, msag_id, false);
			report.addParamDefault("no_blanko", "no_blanko", 0, no_blanko, false);
			report.addParamDefault("lsle_name", "lsle_name", 0, agen.getLsle_name(), false);
			report.addParamDefault("lsrg_nama", "lsrg_nama", 0, agen.getLsrg_nama(), false);
			report.addParamDefault("lca_nama", "lca_nama", 0, agen.getLca_nama(), false);
			//report.addParamDefault("jenis", "jenis", 0, jenis, false);
			//report.setReportQueryMethod("selectReportFitrahCard");
		}
		
		return prepareReport(request, response, report);
	}

    public ModelAndView ekspedisiSpaj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info( "*-*-*-* BasController.ekspedisi" );
		Report report = ( Report ) request.getSession().getAttribute( "report" );
        return prepareReport(request, response, report);
	}

    /**
	 * Sistem Update Nasabah(Pengkinian) - Form laporan data vendor
	 * @param request
	 * @param response
	 * @return
	 * @author Ferry Harlim
	 */
	public ModelAndView laporan_data_vendor(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		Date sysDate = elionsManager.selectSysdate();
		report = new Report("Laporan Data Vendor",props.getProperty("report.upload.laporan_data_vendor"),Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true,new Date[] {sysDate, sysDate}, true);
		return prepareReport(request, response, report);
	}

	/**
	 * Sistem Update Nasabah(Pengkinian) - Form laporan BAS Cek Data Dan Input Data
	 * @param request
	 * @param response
	 * @return
	 * @author Ferry Harlim
	 */
	public ModelAndView laporan_bas_cek_data_inputdata(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		String lcaId=ServletRequestUtils.getStringParameter(request, "lca_id","0");
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		if(lcaId.equals("0"))	
			report = new Report("Laporan Bas Cek Data Input Data All",props.getProperty("report.upload.laporan_bas_cek_data_inputdata_all"),Report.PDF, null);
		else
			report = new Report("Laporan Bas Cek Data Input Data per Cabang",props.getProperty("report.upload.laporan_bas_cek_data_inputdata_cabang"),Report.PDF, null);
		report.addParamDefault("lca_id", "lca_id", 5, lcaId, false);
		report.addParamDefault("tanggal", "tanggalAwal", 12, tanggalAwal, false);
		report.addParamDefault("tanggal", "tanggalAkhir", 12, tanggalAkhir, false);
		ArrayList<ClientHistory> lsReport=new ArrayList();
		ArrayList lsData = Common.serializableList(elionsManager.selectbasCekDataInputData(tanggalAwal,tanggalAkhir,lcaId));
		for(int i=0;i<lsData.size();i++){
			ClientHistory clientHis=(ClientHistory)lsData.get(i);
			ClientHistory clientNasabah =(ClientHistory) elionsManager.selectDataNasabahUpdate(clientHis.getMspo_policy_no());
			clientNasabah.setRetour(clientHis.getRetour());
			clientNasabah.setKirim(clientHis.getKirim());
			clientNasabah.setMsap_area_code1(clientHis.getMsap_area_code1());
			clientNasabah.setMsap_phone1(clientHis.getMsap_phone1());
			clientNasabah.setMsap_area_code2(clientHis.getMsap_area_code2());
			clientNasabah.setMsap_phone2(clientHis.getMsap_phone2());
			clientNasabah.setMsap_area_code3(clientHis.getMsap_area_code3());
			clientNasabah.setMsap_phone3(clientHis.getMsap_phone3());
			clientNasabah.setNo_hp(clientHis.getNo_hp());
			clientNasabah.setNo_hp2(clientHis.getNo_hp2());
			logger.info("nopolis="+clientHis.getMspo_policy_no());
			ClientHistory clientVendor=(ClientHistory) elionsManager.selectDataVendor(clientHis.getMspo_policy_no());
			if(clientVendor!=null){//jika data vendor nya sudah di update, isi datanya.
				clientNasabah.setMsch_tgl_kirim(clientVendor.getMsch_tgl_kirim());
				clientNasabah.setMsch_tgl_terima(clientVendor.getMsch_tgl_terima());
				clientNasabah.setMsch_penerima(clientVendor.getMsch_penerima());
			}
			lsReport.add(clientNasabah);
		}
		
		report.setResultList(lsReport);
		return prepareReport(request, response, report);
	}
	/**
	 * Sistem Update Nasabah(Pengkinian) - Form laporan data yang Belum di kirim balik
	 * @param request
	 * @param response
	 * @return
	 * @author Ferry Harlim
	 */
	public ModelAndView laporan_bas_cek_data_balik(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		String lcaId=ServletRequestUtils.getStringParameter(request, "lca_id","0");
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		if(lcaId.equals("0"))	
			report = new Report("Laporan Bas Cek Data Balik All",props.getProperty("report.upload.laporan_bas_cek_data_balik_all"),Report.PDF, null);
		else
			report = new Report("Laporan Bas Cek Data Balik per Cabang",props.getProperty("report.upload.laporan_bas_cek_data_balik_cabang"),Report.PDF, null);
		report.addParamDefault("lca_id", "lca_id", 5, lcaId, false);
		report.addParamDefault("tanggal", "tanggalAwal", 12, tanggalAwal, false);
		report.addParamDefault("tanggal", "tanggalAkhir", 12, tanggalAkhir, false);
		ArrayList<ClientHistory> lsReport=new ArrayList();
		ArrayList lsData = Common.serializableList(elionsManager.selectbasCekDataBalik(tanggalAwal,tanggalAkhir,lcaId));
		for(int i=0;i<lsData.size();i++){
			ClientHistory clientHis=(ClientHistory)lsData.get(i);
			ClientHistory clientVendor =(ClientHistory) elionsManager.selectDataVendor(clientHis.getMspo_policy_no());
			clientVendor.setRetour(clientHis.getRetour());
			clientVendor.setKirim(clientHis.getKirim());
			clientVendor.setMsap_area_code1(clientHis.getMsap_area_code1());
			clientVendor.setMsap_phone1(clientHis.getMsap_phone1());
			clientVendor.setMsap_area_code2(clientHis.getMsap_area_code2());
			clientVendor.setMsap_phone2(clientHis.getMsap_phone2());
			clientVendor.setMsap_area_code3(clientHis.getMsap_area_code3());
			clientVendor.setMsap_phone3(clientHis.getMsap_phone3());
			clientVendor.setNo_hp(clientHis.getNo_hp());
			clientVendor.setNo_hp2(clientHis.getNo_hp2());
			clientVendor.setMsch_bas_tgl_input(null);
			//query data nasabah update
			//ClientHistory clientNasabah=(ClientHistory) elionsManager.selectDataNasabahUpdate(clientHis.getMspo_policy_no());
			//clientVendor.setMsch_bas_tgl_input(clientNasabah.getMsch_tgl_upload());
			//clientVendor.setMsch_bas_tgl_terima(clientNasabah.getMsch_bas_tgl_terima());
			lsReport.add(clientVendor);
		}
		
		report.setResultList(lsReport);
		return prepareReport(request, response, report);
	}
	
	/**
	 * Laporan Update Data yang dilakukan oleh BAS.
	 * @param request
	 * @param response
	 * @return
	 * @author Ferry Harlim
	 */
	public ModelAndView laporan_bas_update_data(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		Date sysDate = elionsManager.selectSysdate();
		report = new Report("Laporan Bas Update Data",props.getProperty("report.upload.laporan_bas_update_data"),Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true,new Date[] {sysDate, sysDate}, true);
		return prepareReport(request, response, report);
	}

	/**
	 * Laporan Update Data yang dilakukan oleh Nasabah (via E-Policy).
	 * @param request
	 * @param response
	 * @return
	 * @author Ferry Harlim
	 */
	public ModelAndView laporan_nasabah_update_data(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		Date sysDate = elionsManager.selectSysdate();
		report = new Report("Laporan Nasabah Update Data",props.getProperty("report.upload.laporan_nasabah_update_data"),Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true,new Date[] {sysDate, sysDate}, true);
		return prepareReport(request, response, report);
	}

	public ModelAndView csf_summary(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		report = new Report("Call Summary Per Spaj",props.getProperty("report.cs.csf_summary"),Report.PDF, null);
		report.addParamText("Nomor SPAJ", "spaj", 11, ServletRequestUtils.getStringParameter(request, "spaj", ""), true);
		report.addParamDefault("User", "username", 50, currentUser.getName(), true);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView csf_summary_list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysDate = elionsManager.selectSysdate();
		report = new Report("Call Summary Per Periode",props.getProperty("report.cs.csf_summary_list"),Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true,new Date[] {sysDate, sysDate}, true);
		report.addParamDefault("User", "username", 50, currentUser.getName(), true);
		return prepareReport(request, response, report);
	}

	/**
	 * Report permintaan spaj percabang
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * @author Yusup_A
	 * @since Jan 14, 2009 (3:17:52 PM)
	 */
	public ModelAndView report_permintaanSpaj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report = null;
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id");
		String lus_id=ServletRequestUtils.getStringParameter(request, "lus_id");
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		String jenis=ServletRequestUtils.getStringParameter(request, "jenis","");
		String jn_report=ServletRequestUtils.getStringParameter(request, "jn_report","0");
		
		//permintaan Desy untuk view per User
		String params_lus_id = "";
		if(lus_id.equals("0")) params_lus_id = "AND b.jenis = 0";
		else params_lus_id = "AND b.jenis = 0 AND i.lus_id = "+lus_id;
		
		//jn_report = 2, permintaan Yune tidak ada tgl kirim, tgl terima, posisi, keterangan dan Jenis SPAJ PAS dan Simas Card tidak ditampilkan
		if(lca_id.equals("0") && jenis.equals("0")){
			if(jn_report.equals("0")){
				report = new Report("Laporan Permintaan SPAJ Semua Cabang",props.getProperty("report.bas.permintaanSpaj_all_cabang"),Report.PDF, null);
			}else if(jn_report.equals("1")){
				report = new Report("Laporan Permintaan SPAJ Semua Cabang",props.getProperty("report.bas.permintaanSpaj_all_cabang_rekap"),Report.PDF, null);
			}
		}else if(!lca_id.equals("0")){
			if(jn_report.equals("0")){
				report = new Report("Laporan Permintaan SPAJ per Branch Admin",props.getProperty("report.bas.permintaanSpaj_admin"),Report.PDF, null);
			}else if(jn_report.equals("1")){
				report = new Report("Laporan Permintaan SPAJ per Branch Admin",props.getProperty("report.bas.permintaanSpaj_admin_rekap"),Report.PDF, null);
			}
		}else if(!jenis.equals("0")) {
			if(jn_report.equals("0")){
				report = new Report("Laporan Permintaan SPAJ Semua Cabang",props.getProperty("report.bas.permintaanSpaj_per_jenis"),Report.PDF, null);
			}else if(jn_report.equals("1")){
				report = new Report("Laporan Permintaan SPAJ Semua Cabang",props.getProperty("report.bas.permintaanSpaj_per_jenis_rekap"),Report.PDF, null);
			}
		}
		
		report.addParamDefault("lca_id", "lca_id", 0,lca_id, false);
		report.addParamDefault("lus_id", "lus_id", 0, lus_id, false);
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		report.addParamDefault("jenis", "jenis", 0,jenis,  false);
		report.addParamDefault("params_lus_id", "params_lus_id", 0, params_lus_id,  false);
		
		return prepareReport(request, response, report);
	}

	public ModelAndView report_pembatalanCabangSpaj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report = null;
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		
		report = new Report("Laporan Pembatalan Cabang SPAJ",props.getProperty("report.bas.permbatalan_spaj_cabang"),Report.PDF, null);
		
//		if(lca_id.equals("0") && jenis.equals("0")){
//			report = new Report("Laporan Permintaan SPAJ Semua Cabang",props.getProperty("report.bas.permbatalan_spaj_cabang"),Report.PDF, null);
//		}else if(!lca_id.equals("0")){
//			report = new Report("Laporan Permintaan SPAJ per Branch Admin",props.getProperty("report.bas.permintaanSpaj_admin"),Report.PDF, null);
//		}else if(!jenis.equals("0")) {
//			report = new Report("Laporan Permintaan SPAJ Semua Cabang",props.getProperty("report.bas.permintaanSpaj_per_jenis"),Report.PDF, null);
//		}
		
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		
		return prepareReport(request, response, report);
	}
	
	/**
	 * Report pertanggung jawaban spaj percabang, per no permintaan & per jenis spaj
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * @author Yusup_A
	 * @since Jan 14, 2009 (3:17:55 PM)
	 */	
	public ModelAndView report_pertanggungjawabanSpaj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report = null;
		String msf_id = ServletRequestUtils.getStringParameter(request, "msf_id","");
		String prefix = ServletRequestUtils.getStringParameter(request, "prefix");
		String flag = ServletRequestUtils.getStringParameter(request, "flag");
		
		if(prefix.equals("F") && flag.equals("0")) {
			// khusus fitrah > 21 all cabang
			report = new Report("Laporan Petanggungjawaban SPAJ",props.getProperty("report.bas.prtgjwbFormSpajFitrah"),Report.PDF, null);
		}
		else {
			report = new Report("Laporan Petanggungjawaban SPAJ",props.getProperty("report.bas.prtgjwbFormSpaj"),Report.PDF, null);			
		}
		
		report.addParamDefault("msf_id", "msf_id", 0,msf_id, false);
		report.addParamDefault("prefix", "prefix", 0,prefix, false);
		report.addParamDefault("flag", "flag", 0,flag, false);
		
		return prepareReport(request, response, report);
	}
	
	public ModelAndView report_history_fitrah(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report = null;
		
		String ag = ServletRequestUtils.getStringParameter(request,"ag","");
		String ab = ServletRequestUtils.getStringParameter(request,"ab","");
		String jn1 = ServletRequestUtils.getStringParameter(request,"jn1","");
		String jn2 = ServletRequestUtils.getStringParameter(request,"jn2","");
		String jn3 = ServletRequestUtils.getStringParameter(request,"jn3","");
		String bl = ServletRequestUtils.getStringParameter(request,"bl","");
		String st = ServletRequestUtils.getStringParameter(request,"st","");
		String en = ServletRequestUtils.getStringParameter(request,"en","");
		String cab = ServletRequestUtils.getStringParameter(request,"cab","");
		String st2 = ServletRequestUtils.getStringParameter(request,"st2","");
		String en2 = ServletRequestUtils.getStringParameter(request,"en2","");
		String flag = ServletRequestUtils.getStringParameter(request,"flag","");
		
		if(flag.equals("flag1")) {
			if(jn1.equals("0")) // all
				report = new Report("Laporan Petanggungjawaban SPAJ",props.getProperty("report.bas.histAllByAgent"),Report.PDF, null);			
			else if(jn1.equals("1")) // semua jenis kecuali fitrah
				report = new Report("Laporan Petanggungjawaban SPAJ",props.getProperty("report.bas.histNonFitrahByAgent"),Report.PDF, null);
			else // per jenis
				report = new Report("Laporan Petanggungjawaban SPAJ",props.getProperty("report.bas.histFitrahByAgent"),Report.PDF, null);

		}else if(flag.equals("flag2")) {
			if(jn2.equals("0")) 
				report = new Report("Laporan Petanggungjawaban SPAJ",props.getProperty("report.bas.histAllByNo"),Report.PDF, null);
			else if(jn2.equals("1")) 
				report = new Report("Laporan Petanggungjawaban SPAJ",props.getProperty("report.bas.histNonFitrahByNo"),Report.PDF, null); 
			else	
				report = new Report("Laporan Petanggungjawaban SPAJ",props.getProperty("report.bas.histFitrahByNo"),Report.PDF, null);
		}else if(flag.equals("flag3")) {
			if(jn3.equals("0")) 
				report = new Report("Laporan Petanggungjawaban SPAJ",props.getProperty("report.bas.histAllByDate"),Report.PDF, null);
			else if(jn3.equals("1"))
				report = new Report("Laporan Petanggungjawaban SPAJ",props.getProperty("report.bas.histNonFitrahByDate"),Report.PDF, null);
			else	
				report = new Report("Laporan Petanggungjawaban SPAJ",props.getProperty("report.bas.histFitrahByDate"),Report.PDF, null); 
		}else if(flag.equals("flag4")) {
			if(cab.equals("0")) 
				report = new Report("Laporan Petanggungjawaban SPAJ",props.getProperty("report.bas.histAllByCab"),Report.PDF, null); 
			else 
				report = new Report("Laporan Petanggungjawaban SPAJ",props.getProperty("report.bas.histByCab"),Report.PDF, null); 
		}
		 
		report.addParamDefault("ag","ag",0,ag,false); 
		report.addParamDefault("ab","ab",0,ab,false);
		report.addParamDefault("jn1","jn1",0,jn1,false);
		report.addParamDefault("jn2","jn2",0,jn2,false);
		report.addParamDefault("jn3","jn3",0,jn3,false);
		report.addParamDefault("bl","bl",0,bl,false);
		report.addParamDefault("st","st",0,st,false);
		report.addParamDefault("en","en",0,en,false);
		report.addParamDefault("cab","cab",0,cab,false);
		report.addParamDefault("st2","st2",0,st2,false);
		report.addParamDefault("en2","en2",0,en2,false);
		
		return prepareReport(request, response, report);
	}
	
	public ModelAndView report_fitrah_blm_prtgjwb(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String flag = ServletRequestUtils.getStringParameter(request, "flag","");
		Report report = null;
		
		if(flag.equals("1")) {
			report = new Report("Laporan Petanggungjawaban SPAJ",props.getProperty("report.bas.formSpajFitrahBlmPrtgjwb"),Report.PDF, null);
		}
		else if(flag.equals("2")) {
			report = new Report("Laporan Petanggungjawaban SPAJ",props.getProperty("report.bas.formSpajFitrahPrtgjwb"),Report.PDF, null);
		}
		
		return prepareReport(request, response, report);
	}

    /**
	 * Sistem Laporan Roll Over Stabil Link per Cabang
	 * @author Samuel Baktiar
	 * @since Nov 10, 2009 (9:50:01 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */

    public ModelAndView daftar_stable_ro(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysDate = elionsManager.selectSysdate();

		ArrayList<DropDown> reportPathList = new ArrayList<DropDown>();
		reportPathList.add(new DropDown(props.getProperty("report.bas.daftar_stable_ro"), "Daftar Stable Link Roll Over"));
		reportPathList.add(new DropDown(props.getProperty("report.bas.daftar_stable_jt"), "Daftar Stable Link Jatuh Tempo"));
		reportPathList.add(new DropDown(props.getProperty("report.bas.daftar_stable_save_ro"), "Daftar Stable Save Roll Over"));
		reportPathList.add(new DropDown(props.getProperty("report.bas.daftar_stable_save_jt"), "Daftar Stable Save Jatuh Tempo"));
		
		report = new Report("Daftar Stable Roll Over Dan Jatuh Tempo", reportPathList, Report.PDF, null);

		report.addParamDefault("username", "username", 0, currentUser.getName(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
		return prepareReport(request, response, report);
	}
    


    public ModelAndView report_fitrah_bmi(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Report report = null;
    	String jenis = ServletRequestUtils.getStringParameter(request, "jenis","0");
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
    	if(jenis.equals("0")) {
    		report = new Report("Report Fitrah Muamalat All",props.getProperty("report.bas.fitrahMuamalatAll"),Report.PDF, null);
    	}
    	else {
    		report = new Report("Report Fitrah Muamalat by Cabang",props.getProperty("report.bas.fitrahMuamalatCab"),Report.PDF, null);
    		report.addParamDefault("jenis", "jenis", 0, jenis, false);
    	}
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		
    	return prepareReport(request, response, report);
    }
    
    public ModelAndView report_permintaan_spaj_bandara(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Report report = null;
    	String id = ServletRequestUtils.getStringParameter(request, "id","0");
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");    	
 
    	if(id.equals("0")) {
    		report = new Report("Permintaan Blanko Spaj oleh Bandara All",props.getProperty("report.bas.travel_ins_all"),Report.PDF, null);
    	}
    	else {
    		report = new Report("Permintaan Blanko Spaj oleh Bandara",props.getProperty("report.bas.travel_ins_admin"),Report.PDF, null);
    		report.addParamDefault("id", "id", 0, id, false);
    	}
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		
		return prepareReport(request, response, report);
    }
    
    public ModelAndView report_pertanggungjawaban_spaj_bandara(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Report report = null;
    	String id = ServletRequestUtils.getStringParameter(request, "id","0");
    	String jenis = ServletRequestUtils.getStringParameter(request, "jenis","0");
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir"); 
		
		if(jenis.equals("0")) {
	    	if(id.equals("0")) {
	    		report = new Report("Pertanggungjawaban Spaj oleh Bandara All",props.getProperty("report.bas.prtgjwb_travel_ins_all"),Report.PDF, null);
	    	}
	    	else {
	    		report = new Report("Pertanggungjawaban Spaj oleh Bandara",props.getProperty("report.bas.prtgjwb_travel_ins_admin"),Report.PDF, null);
	    		report.addParamDefault("id", "id", 0, id, false);
	    	}
		}
		else {
	    	if(id.equals("0")) {
	    		report = new Report("Blanko Travel Insurance di Bandara All",props.getProperty("report.bas.travel_ins_belum_jadi_all"),Report.PDF, null);
	    	}
	    	else {
	    		report = new Report("Blanko Travel Insurance di Bandara",props.getProperty("report.bas.travel_ins_belum_jadi_admin"),Report.PDF, null);
	    		report.addParamDefault("id", "id", 0, id, false);
	    	}
		}
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		
    	return prepareReport(request, response, report);
    }
    
    public ModelAndView perm_bandara(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Report report = new Report("Permintaan Bandara",props.getProperty("report.bas.permintaan_bandara"),Report.PDF, null);
		String msf_id = ServletRequestUtils.getStringParameter(request, "msf_id","");
		
		report.addParamDefault("msf_id", "msf_id", 0, msf_id, false);
		return prepareReport(request, response, report);
    }
    
    public ModelAndView pas(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Report report = null;
		String msf_id=ServletRequestUtils.getStringParameter(request, "msf_id","");
		String jenis=ServletRequestUtils.getStringParameter(request, "jenis", "");    	
    	
		if(jenis.equals("admin")) {
			report = new Report("Permintaan PAS admin",props.getProperty("report.bas.permintaan_pas"),Report.PDF, null);
		}
		else if(jenis.equals("agen")) {
			report = new Report("Permintaan PAS agent",props.getProperty("report.bas.permintaan_agent_pas"),Report.PDF, null);
		}
		report.addParamDefault("msf_id", "msf_id", 0, msf_id, false);
		
		
		return prepareReport(request, response, report);
    }  
    
    public ModelAndView report_outstanding_all(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User) request.getSession().getAttribute("currentUser"); 
    	String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
    	Report report = null;
    	
    	if(currentUser.getLde_id().equals("01") || currentUser.getLde_id().equals("29"))
    		report = new Report("Report Outstanding Agent All",props.getProperty("report.bas.outstanding_all"),Report.PDF, null);
    	else {
    		report = new Report("Report Outstanding Agent All",props.getProperty("report.bas.outstanding_admin"),Report.PDF, null);
    		report.addParamDefault("lus_id", "lus_id", 0, currentUser.getLus_id(), false);
    	}
    		
    	report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		
		return prepareReport(request, response, report);
    }
    
    public ModelAndView data_nasabah_ma(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String isPDF = request.getParameter("isPDF");
		String isXls = request.getParameter("isXls");
		Date sysDate = elionsManager.selectSysdate();
		
		ArrayList<DropDown> reportPathList = new ArrayList<DropDown>();
		reportPathList.add(new DropDown(props.getProperty("report.bas.data_nasabah_ma"), "Report Data Nasabah"));
		reportPathList.add(new DropDown(props.getProperty("report.bas.prospek_nasabah_ma"), "Report Prospek Nasabah"));
		reportPathList.add(new DropDown(props.getProperty("report.bas.nasabah_per_usher"), "Nasabah per Usher"));
		//reportPathList.add(new DropDown(props.getProperty("report.bas.nasabah_per_fa"), "Nasabah per FA"));
		reportPathList.add(new DropDown(props.getProperty("report.bas.nasabah_closing"), "Nasabah Closing"));
		
		Report report = new Report("Mall Assurance Reports", reportPathList, Report.PDF, null);;

		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
		report.addParamDefault("isPDF", "isPDF", 0, isPDF, false);
		report.addParamDefault("isXls", "isXls", 0, isXls, false);
		
		return prepareReport(request, response, report);
    }
    
    /**
     * Report Summary Inputan Japanese Desk (Dari Sistem E-Corporate), req Yusy (Helpdesk #21490)
     * http://yusuf7/E-Lions/report/bas.htm?window=japanese_desk
     * Yusuf - 5 Oct 2011
     */
    public ModelAndView japanese_desk(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");

    	//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		ArrayList data = Common.serializableList(uwManager.selectReportSummaryJapaneseDesk(bdate, edate));
    		
    		if(data.size() > 0){ //bila ada data
	    		ServletOutputStream sos = response.getOutputStream();
	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.japanese_desk") + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);
	    		params.put("user", currentUser.getName());

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
    	
    	//halaman depan
    	}else{
        	Map<String, Object> m = new HashMap<String, Object>();

        	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
	    	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
	    	
	    	return new ModelAndView("report/japanese_desk", m);
    	}
    	
		return null;
    }
    
    /**
     * Report JT Powersave untuk cabang, req Yune (Helpdesk #21103)
     * http://yusufxp/E-Lions/report/bas.htm?window=jt_powersave
     * Yusuf - 12 Sep 2011
     * 
     * Canpri - 04 Juni 2013
     * Tambah report untuk Stable Link dan penambahan kategori FU
     */
    public ModelAndView jt_powersave(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	int json = ServletRequestUtils.getIntParameter(request, "json", 0);

    	//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		
    		String lca = ServletRequestUtils.getStringParameter(request, "cabang");
    		String lwk = ServletRequestUtils.getStringParameter(request, "wakil");
    		String lsrg = ServletRequestUtils.getStringParameter(request, "region");
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		String kategori = ServletRequestUtils.getStringParameter(request, "kategori", "All");
    		String report = ServletRequestUtils.getStringParameter(request, "report", "power_save_jt");
    		
    		String lsfu = "and a.msl_tahun_ke = h.msbi_tahun_ke(+) and a.msl_premi_ke = h.msbi_premi_ke(+)";
    		
    		if(kategori.equals("butuh_dana")){lsfu = "and a.msl_tahun_ke = h.msbi_tahun_ke and a.msl_premi_ke = h.msbi_premi_ke and h.lsfu_id = 15";}
    		else if(kategori.equals("bunga")){lsfu = "and a.msl_tahun_ke = h.msbi_tahun_ke and a.msl_premi_ke = h.msbi_premi_ke and h.lsfu_id = 16";}
			else if(kategori.equals("lain-lain")){lsfu = "and a.msl_tahun_ke = h.msbi_tahun_ke and a.msl_premi_ke = h.msbi_premi_ke and h.lsfu_id = 0";}
    		
    		if(report.equals("power_save_jt")){
    			ArrayList data = Common.serializableList(uwManager.selectReportJTPowersave(lca, lwk, lsrg, bdate, edate, currentUser.getLus_id(), kategori));
        		
        		if(data.size() > 0){ //bila ada data
    	    		ServletOutputStream sos = response.getOutputStream();
    	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.jt_powersave") + ".jasper");
    	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

    	    		Map<String, Object> params = new HashMap<String, Object>();
    	    		params.put("bdate", bdate);
    	    		params.put("edate", edate);
    	    		params.put("cabang", ServletRequestUtils.getStringParameter(request, "cabang2", "ALL"));
    	    		params.put("wakil", ServletRequestUtils.getStringParameter(request, "wakil2", "ALL"));
    	    		params.put("region", ServletRequestUtils.getStringParameter(request, "region2", "ALL"));

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
    		}else if(report.equals("slink_jt")){
    			ServletOutputStream sos = response.getOutputStream();
	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.daftar_stable_jt_fu") + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("tanggalAwal", bdate);
	    		params.put("tanggalAkhir", edate);
	    		params.put("username", currentUser.getName());
	    		params.put("lus_id", currentUser.getLus_id());
	    		params.put("lsfu", lsfu);

	    		//JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, this.getDataSource().getConnection());
	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, this.getUwManager().getUwDao().getDataSource().getConnection());
	    		
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
    		}else if(report.equals("slink_ro")){
    			ServletOutputStream sos = response.getOutputStream();
	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.daftar_stable_ro_fu") + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("tanggalAwal", bdate);
	    		params.put("tanggalAkhir", edate);
	    		params.put("username", currentUser.getName());
	    		params.put("lus_id", currentUser.getLus_id());
	    		params.put("lsfu", lsfu);

	    		//JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, this.getDataSource().getConnection());
	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, this.getUwManager().getUwDao().getDataSource().getConnection());

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
    		}
    		
    	//halaman depan
    	}else if(json == 0){
        	Map<String, Object> m = new HashMap<String, Object>();

        	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
	    	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(30)));
	    	
	    	m.put("listCabang", elionsManager.selectDropDown(
				"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lca_id", "eka.utils.cabang(c.lca_id)", "", "2", 
				"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id group by c.lca_id"));
	    	m.put("listWakil", elionsManager.selectDropDown(
				"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lwk_id", "eka.utils.kanwil_nama(c.lca_id, c.lwk_id)", "", "2", 
				"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id group by c.lca_id, c.lwk_id"));
	    	m.put("listRegion", elionsManager.selectDropDown(
				"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lsrg_id", "c.lsrg_nama", "", "2", 
				"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id group by c.lsrg_id, c.lsrg_nama"));

	    	return new ModelAndView("report/jt_powersave", m);
	    	
    	//tarik data wakil dari data cabang (ajax)
    	}else if(json == 1){ 
    		String lca = ServletRequestUtils.getStringParameter(request, "lca", "ALL");
    		ArrayList<DropDown> result = Common.serializableList(elionsManager.selectDropDown(
    			"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lwk_id", "eka.utils.kanwil_nama(c.lca_id, c.lwk_id)", "", "2", 
    			"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id " +(lca.equals("ALL") ?  "" : " and c.lca_id = '" +lca+ "' ")+ " group by c.lca_id, c.lwk_id"));
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();

       	//tarik data region dari data cabang dan region (ajax)
    	}else if(json == 2){
    		String lca = ServletRequestUtils.getStringParameter(request, "lca", "ALL");
    		String lwk = ServletRequestUtils.getStringParameter(request, "lwk", "ALL");
    		ArrayList<DropDown> result = Common.serializableList(elionsManager.selectDropDown(
    			"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lsrg_id", "c.lsrg_nama", "", "2", 
    			"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id " +(lca.equals("ALL") ? " " : " and c.lca_id = '" +lca+ "' ")+(lwk.equals("ALL") ? " " : " and c.lwk_id = '" +lwk+ "' ")+ " group by c.lsrg_id, c.lsrg_nama"));
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();
    	}
    	
		return null;
    }

    /**
     * Report Summary Input Agen, req Bebby (Helpdesk #20907)
     * http://yusufxp/E-Lions/report/bas.htm?window=summary_input_agen
     * Yusuf - 12 Sep 2011
     * 
     * Canpri - 18 Mar 2013
     * penambahan view report digroup peruser
     */
    public ModelAndView summary_input_agen(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	int json = ServletRequestUtils.getIntParameter(request, "json", 0);

    	//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		
    		String cabang = ServletRequestUtils.getStringParameter(request, "cabang");
    		String userinput = ServletRequestUtils.getStringParameter(request, "userinput");
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		String jn_report = ServletRequestUtils.getStringParameter(request, "jn_report", "0");
    		String userBas = ServletRequestUtils.getStringParameter(request, "seluserBas", "ALL");
    		ArrayList data = Common.serializableList(uwManager.selectReportSummaryInputRegisterAgen(cabang, userinput, bdate, edate, userBas, jn_report));
    		
    		if(data.size() > 0){ //bila ada data
	    		ServletOutputStream sos = response.getOutputStream();
	    		File sourceFile = null;
	    		
	    		if(jn_report.equals("0")){
	    			sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.summary_input_agen") + ".jasper");
	    		}else{
	    			sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.summary_input_agen_peruser") + ".jasper");
	    		}
	    		
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);
	    		params.put("cabang", ServletRequestUtils.getStringParameter(request, "cabang2", "ALL"));
	    		params.put("userinput", ServletRequestUtils.getStringParameter(request, "userinput2", "ALL"));

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
    	
    	//halaman depan
    	}else if(json == 0){
        	Map<String, Object> m = new HashMap<String, Object>();
        	m.put("sekarang", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
        	m.put("besok", defaultDateFormat.format(uwManager.selectSysdateTruncated(1)));

        	m.put("listCabang", elionsManager.selectDropDown(
    				"eka.lst_user", "lca_id", "eka.utils.cabang(lca_id)", "", "2", 
    				"lus_bas = 1 and lus_active = 1 group by lca_id"));
        	m.put("listUser", elionsManager.selectDropDown(
        			"eka.lst_user", "lus_id", "lus_login_name", "", "2", "lus_bas = 1 and lus_active = 1"));
        	
        	ArrayList<DropDown> userBas = Common.serializableList(uwManager.selectUserBasSummaryInput(props.getProperty("user.bas.summaryinput")));
	    	m.put("userBas", userBas);
	    	
	    	Integer bas = 0;
	    	if("690, 3041, 3179, 113, 5, 55".indexOf(currentUser.getLus_id()) != -1){
				bas = 1;
			}
	    	m.put("bas", bas);
        	
        	return new ModelAndView("report/summary_input_agen", m);
	    	
    	//tarik data userinput dari data cabang (ajax)
    	}else if(json == 1){ 
    		String cabang = ServletRequestUtils.getStringParameter(request, "cabang", "ALL");
    		//select lus_id, lus_login_name from eka.lst_user where lca_id = '53' and lus_bas = 1 and lus_active = 1
    		ArrayList<DropDown> result = Common.serializableList(elionsManager.selectDropDown(
    			"eka.lst_user", "lus_id", "lus_login_name", "", "2", "lca_id = '" +cabang+ "' and lus_bas = 1 and lus_active = 1"));
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();

    	}
    	
		return null;
    }    
    
    /**
     * Report Print Polis Cabang
     * E-Lions/report/bas.htm?window=reportprintpoliscabang
     */
    public ModelAndView reportprintpoliscabang(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	int json = ServletRequestUtils.getIntParameter(request, "json", 0);

    	//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		
    		String lca = ServletRequestUtils.getStringParameter(request, "cabang");
    		String lwk = ServletRequestUtils.getStringParameter(request, "wakil");
    		String lsrg = ServletRequestUtils.getStringParameter(request, "region");
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		ArrayList data = Common.serializableList(uwManager.selectreportPrintPolisCabang(lca, lwk, lsrg, bdate, edate, currentUser.getLus_id()));
    		
    		if(data.size() > 0){ //bila ada data
	    		ServletOutputStream sos = response.getOutputStream();
	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.reportPrintPolisCabang") + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);
	    		params.put("cabang", ServletRequestUtils.getStringParameter(request, "cabang2", "ALL"));
	    		params.put("wakil", ServletRequestUtils.getStringParameter(request, "wakil2", "ALL"));
	    		params.put("region", ServletRequestUtils.getStringParameter(request, "region2", "ALL"));

	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));

		    	if(request.getParameter("showXLS") != null){
		    		response.setContentType("application/vnd.ms-excel");
		            JRXlsExporter exporter = new JRXlsExporter();
		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
		            exporter.exportReport();
		    	}else if(request.getParameter("showPDF") != null){//
		    		response.setContentType( "application/pdf" );
		    		response.setHeader("Content-disposition",
		    		                  "attachment; filename=" +
		    		                  "PrintPolisCabang.pdf" );
		    		JasperExportManager.exportReportToPdfStream(jasperPrint, sos);
		    	}
	    		sos.close();
    		}else{ //bila tidak ada data
    			ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Tidak ada data');window.close();</script>");
    			sos.close();
    		}
    	
    	//halaman depan
    	}else if(json == 0){
        	Map<String, Object> m = new HashMap<String, Object>();

        	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
	    	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(30)));
	    	
	    	m.put("listCabang", elionsManager.selectDropDown(
				"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lca_id", "eka.utils.cabang(c.lca_id)", "", "2", 
				"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id group by c.lca_id"));
	    	m.put("listWakil", elionsManager.selectDropDown(
				"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lwk_id", "eka.utils.kanwil_nama(c.lca_id, c.lwk_id)", "", "2", 
				"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id group by c.lca_id, c.lwk_id"));
	    	m.put("listRegion", elionsManager.selectDropDown(
				"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lsrg_id", "c.lsrg_nama", "", "2", 
				"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id group by c.lsrg_id, c.lsrg_nama"));

	    	return new ModelAndView("report/reportPrintPolisCabang", m);
	    	
    	//tarik data wakil dari data cabang (ajax)
    	}else if(json == 1){ 
    		String lca = ServletRequestUtils.getStringParameter(request, "lca", "ALL");
    		ArrayList<DropDown> result = Common.serializableList(elionsManager.selectDropDown(
    			"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lwk_id", "eka.utils.kanwil_nama(c.lca_id, c.lwk_id)", "", "2", 
    			"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id " +(lca.equals("ALL") ?  "" : " and c.lca_id = '" +lca+ "' ")+ " group by c.lca_id, c.lwk_id"));
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();

       	//tarik data region dari data cabang dan region (ajax)
    	}else if(json == 2){
    		String lca = ServletRequestUtils.getStringParameter(request, "lca", "ALL");
    		String lwk = ServletRequestUtils.getStringParameter(request, "lwk", "ALL");
    		ArrayList<DropDown> result = Common.serializableList(elionsManager.selectDropDown(
    			"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lsrg_id", "c.lsrg_nama", "", "2", 
    			"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id " +(lca.equals("ALL") ? " " : " and c.lca_id = '" +lca+ "' ")+(lwk.equals("ALL") ? " " : " and c.lwk_id = '" +lwk+ "' ")+ " group by c.lsrg_id, c.lsrg_nama"));
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();
    	}
    	
		return null;
    }
    
    /**
     * Report Simas Card Cabang
     * E-Lions/report/bas.htm?window=reportsimascardcabang
     */
    public ModelAndView reportsimascardcabang(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	int json = ServletRequestUtils.getIntParameter(request, "json", 0);
    	ArrayList lsAdmin=new ArrayList();
		String lus_id = ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());
    	
    		Map admin=new HashMap();
    		admin.put("KEY", currentUser.getLus_id());
    		admin.put("VALUE", currentUser.getName());
    		lsAdmin.add(admin);
    		
    	//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		
    		String lca = ServletRequestUtils.getStringParameter(request, "cabang");
    		String lwk = ServletRequestUtils.getStringParameter(request, "wakil");
    		String lsrg = ServletRequestUtils.getStringParameter(request, "region");
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		ArrayList data = Common.serializableList(uwManager.selectreportSimascardCabang(lca, lwk, lsrg, bdate, edate, currentUser.getLus_id()));
    		
    		if(data.size() > 0){ //bila ada data
	    		ServletOutputStream sos = response.getOutputStream();
	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.reportSimascardCabang") + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);
	    		params.put("cabang", ServletRequestUtils.getStringParameter(request, "cabang2", "ALL"));
	    		params.put("wakil", ServletRequestUtils.getStringParameter(request, "wakil2", "ALL"));
	    		params.put("region", ServletRequestUtils.getStringParameter(request, "region2", "ALL"));
	    		params.put("lsAdmin", lsAdmin);
	    		
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
    	
    	//halaman depan
    	}else if(json == 0){
        	Map<String, Object> m = new HashMap<String, Object>();

        	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
	    	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(30)));
	    	
	    	m.put("lsAdmin", lsAdmin);
	    	
	    	m.put("listCabang", elionsManager.selectDropDown(
				"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lca_id", "eka.utils.cabang(c.lca_id)", "", "2", 
				"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id group by c.lca_id"));
	    	m.put("listWakil", elionsManager.selectDropDown(
				"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lwk_id", "eka.utils.kanwil_nama(c.lca_id, c.lwk_id)", "", "2", 
				"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id group by c.lca_id, c.lwk_id"));
	    	m.put("listRegion", elionsManager.selectDropDown(
				"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lsrg_id", "c.lsrg_nama", "", "2", 
				"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id group by c.lsrg_id, c.lsrg_nama"));
	    	//lsAdmin=elionsManager.selectAllUserInCabang(lca_id);

	    	return new ModelAndView("report/reportSimascardCabang", m);
	    	
    	//tarik data wakil dari data cabang (ajax)
    	}else if(json == 1){ 
    		String lca = ServletRequestUtils.getStringParameter(request, "lca", "ALL");
    		ArrayList<DropDown> result = Common.serializableList(elionsManager.selectDropDown(
    			"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lwk_id", "eka.utils.kanwil_nama(c.lca_id, c.lwk_id)", "", "2", 
    			"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id " +(lca.equals("ALL") ?  "" : " and c.lca_id = '" +lca+ "' ")+ " group by c.lca_id, c.lwk_id"));
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();

       	//tarik data region dari data cabang dan region (ajax)
    	}else if(json == 2){
    		String lca = ServletRequestUtils.getStringParameter(request, "lca", "ALL");
    		String lwk = ServletRequestUtils.getStringParameter(request, "lwk", "ALL");
    		ArrayList<DropDown> result = Common.serializableList(elionsManager.selectDropDown(
    			"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lsrg_id", "c.lsrg_nama", "", "2", 
    			"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id " +(lca.equals("ALL") ? " " : " and c.lca_id = '" +lca+ "' ")+(lwk.equals("ALL") ? " " : " and c.lwk_id = '" +lwk+ "' ")+ " group by c.lsrg_id, c.lsrg_nama"));
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();
    	}
    	
		return null;
    }
    
    /**
     * @author Deddy
     * @since 23 Oct 2012
     * @category Report Summary Download Proposal dari User Cabang
     * @param tglAwal, tglAkhir
     * http://deddy7/E-Lions/report/bas.htm?window=hist_dl_proposal_cb
     */
    public ModelAndView hist_dl_proposal_cb(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map param= new HashMap();
		Report report;
		report = new Report("Laporan Download Proposal Dari User Cabang",props.getProperty("report.bas.daftar_dl_proposal"),Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
		
	}
    
    /**
     * @author randy
     */
    public ModelAndView gm_report(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		Date sysDate = elionsManager.selectSysdate();
		
        String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal","");
    	String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir","");
    	
		report = new Report("GM Report",props.getProperty("report.bas.gm_report"),Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true,new Date[] {sysDate, sysDate}, true);
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0, tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0, tanggalAkhir,  false);
		
		return prepareReport(request, response, report);
		
	}
    
    public ModelAndView summary_qa(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		Date sysDate = elionsManager.selectSysdate();
		
        String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal","");
    	String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir","");
    	
		report = new Report("Summary QA",props.getProperty("report.bas.summary_qa"),Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true,new Date[] {sysDate, sysDate}, true);
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0, tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0, tanggalAkhir,  false);
		
		return prepareReport(request, response, report);
		
	}
    
    /**
     * @author Canpri
     * @since 2 Nov 2012
     * @category Report List Data upload belum transfer ke uw
     * @param tglAwal, tglAkhir
     * http://localhost/E-Lions/report/bas.htm?window=report_data_upload
     */
    public ModelAndView report_data_upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	Map<String, Object> m = new HashMap<String, Object>();

    	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
    	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(7)));
    	m.put("kode","0");
    	String lusID = currentUser.getLus_id();
    	
    	//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		String jenis = ServletRequestUtils.getStringParameter(request, "jenis", "0");
    		Integer jn_trans = ServletRequestUtils.getIntParameter(request, "type", 0);
    		
    		String jn_tgl = "";
    		if(jenis.equals("0")){
    			jn_tgl = "Input";
    		}else{
    			jn_tgl = "Scan";
    		}
    		
    		ArrayList data = Common.serializableList(uwManager.selectReportDataUpload(bdate, edate, jenis, jn_trans));
    		
    		if(data.size() > 0){ //bila ada data
	    		ServletOutputStream sos = response.getOutputStream();
	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.reportDataUpload") + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);
	    		params.put("pdate", defaultDateFormat.format(elionsManager.selectSysdate()));
	    		params.put("jenis", jn_tgl);
	    		if(jn_trans==0){
	    			params.put("jn_transfer", "(Belum Transfer ke Speedy)");
	    			params.put("jn_trans", "0");
	    		}else if(jn_trans==1){
	    			params.put("jn_transfer", "(Back to BAS)");
	    			params.put("jn_trans", "1");
	    		}

	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));

		    	if(request.getParameter("showXLS") != null){
	    			Report report;
	    			report = new Report("Report Data Upload",props.getProperty("report.bas.reportDataUpload"),Report.XLS, null);
		    		
	    			String dest =  "\\\\ebserver\\pdfind\\Report\\temp\\";
	    			String nama_file = "Report_Data_Upload_"+lusID+".xls";
	    			
	    			String query1 ="1 = 1";
	    			if(jenis.equals("0")){	
	    				query1 = "a.mspo_input_date between to_date('"+bdate+"','dd/MM/yyyy') and to_date('"+edate+"','dd/MM/yyyy')+1"; //tgl input
	    			}else{
	    				query1 = "b.tgl_input between to_date('"+bdate+"','dd/MM/yyyy') and to_date('"+edate+"','dd/MM/yyyy')+1"; //tgl scan
	    			}

	    			String query2 ="1 = 1";
	    			String query3 ="1 = 1";
	    			if(jn_trans==0){	
	    				query2 = "not exists(select reg_spaj from eka.mst_position_spaj where reg_spaj = a.reg_spaj and lspd_id = 27) and a.lspd_id in (1)";//belum transfer speedy
	    				query3 = "exists (select 1 from eka.mst_position_spaj where reg_spaj = a.reg_spaj and msps_desc like'BACK TRANSFER KE ADMIN:%')";
	    			}else{
	    				query2 = "exists(select reg_spaj from eka.mst_position_spaj where reg_spaj = a.reg_spaj and lspd_id = 27) and e.lssa_id = 3 and a.lspd_id in (1,2,27)"; //back to BAS
	    			}
	    			
	    			params.put("query1", query1);
	    			params.put("query2", query2);
	    			params.put("query3", query3);
	    			
	    			Connection conn = null;
	    			
	    			try{
	    				conn = this.elionsManager.getUwDao().getDataSource().getConnection();
	    				JasperUtils.exportReportToXls(report.getReportPath()+".jasper", dest, nama_file, params, conn);
	    			}finally {
						this.elionsManager.getUwDao().closeConn(conn);
					}
	    			
	    			File file = new File(dest+"\\"+nama_file);
	    			
	    			if (file.exists()){
	    				FileInputStream in = null;
	    				ServletOutputStream ouputStream = null;
	    				try {
	    					
    						response.setContentType("application/vnd.ms-excel");
		    				response.setHeader("Content-Disposition","inline; filename=" + nama_file);
		    				response.setHeader("Expires", "0");
		    				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		    				response.setHeader("Pragma", "public");
	    					
		    				in = new FileInputStream(file);
		    				ouputStream = response.getOutputStream();
		    				
		    				IOUtils.copy(in, ouputStream);
	    				}finally {
	    					try {
	    						if(in != null) {
	    							in.close();
	    						}
	    						if(ouputStream != null) {
	    							ouputStream.flush();
	    							ouputStream.close();
	    						}
	    					}catch (Exception e) {
								logger.error("ERROR :", e);
							}
	    				}
	    			    
	    			    FileUtils.forceDelete(file);
	    			}
		    		
		    	}else if(request.getParameter("showPDF") != null){
		    		JasperExportManager.exportReportToPdfStream(jasperPrint, sos);
		    	}
		    	sos.close();
    		}else{ //bila tidak ada data
    			ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Tidak ada data');window.close();</script>");
    			sos.close();
    		}
    		return null;
    	}
    	
    	return new ModelAndView("report/reportDataUpload", m);
    }
    
    /**
     * @author Canpri
     * @since 12 Nov 2012
     * @category Report Followup Billing Premi Lanjutan
     * @param bdate, edate, cabang
     * http://localhost/E-Lions/report/bas.htm?window=reportFollowupBillingPL
     */
    public ModelAndView reportFollowupBillingPL(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	
    	Map<String, Object> m = new HashMap<String, Object>();
    	
    	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
    	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(30)));
    	
    	ArrayList<DropDown> listCab = new ArrayList<DropDown>();
		listCab.add(new DropDown("ALL", "ALL"));
		listCab.addAll(elionsManager.selectDropDown("EKA.LST_CABANG", "LCA_ID || '~' || LCA_NAMA", "LCA_NAMA", "", "LCA_NAMA", currentUser.getLus_bas().intValue() == 1 ? "lca_id = '" + currentUser.getLca_id() + "'" : ""));
		
		m.put("listCab", listCab);
		
		//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		String cbng = ServletRequestUtils.getStringParameter(request, "cabang", "ALL");
    		Integer jn_report = ServletRequestUtils.getIntParameter(request, "jn_report", 0);
    		
    		String report = "";
    		String lca = "";
    		String lca_nama= "";
    		Integer jml = 0;
    		ArrayList data =   new ArrayList();
    		
    		if(cbng.equals("ALL")){
    			lca = "ALL";
        		lca_nama = "ALL";
    		}else{
    			lca = cbng.substring(0, cbng.indexOf("~"));
        		lca_nama = cbng.substring(cbng.indexOf("~")+1,cbng.length());
    		}
    		
    		if(jn_report==0){
    			data = Common.serializableList(uwManager.reportFollowupBillingPL(bdate, edate, lca, jn_report));
    			report = props.getProperty("report.bas.reportFollowupBillingPL") + ".jasper";
    		}else{
    			data = Common.serializableList(uwManager.reportFollowupBillingPL(bdate, edate, lca, jn_report));
    			report = props.getProperty("report.bas.reportFollowupBillingPLKategori") + ".jasper";
    		}
    		
    		if(data.size() > 0){ //bila ada data
	    		ServletOutputStream sos = response.getOutputStream();
	    		File sourceFile = Resources.getResourceAsFile(report);
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);
	    		params.put("cabang", lca_nama);
//	    		if(jn_report==1){
//	    			for(int i=0; i<data.size();i++){
//	    				Map mm = (Map) data.get(i);
//	    				Integer count = (Integer) mm.get("JML_POLIS");
//	    				jml += count;
//	    			}
//	    			params.put("jml", jml);
//	    		}

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
    		return null;
    	}

		return new ModelAndView("report/reportFollowupBillingPL", m);
    }
    
    /**
     * @author Canpri
	 * Permintaan Brosur - Covering Brosur GA
	 * @since February 06, 2013 (1:15:10 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView covering_brosur_ga(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		String msf_id=ServletRequestUtils.getStringParameter(request, "msf_id");
		
		report = new Report("Covering Brosur GA",props.getProperty("report.bas.covering_brosur_ga"),Report.PDF, null);
		
		report.addParamDefault("msf_id", "msf_id", 0, msf_id, false);
		return prepareReport(request, response, report);
	}
	
	/**
     * @author Canpri
     * @since 17 Apr 2013
     * @category Report jatuh tempo power save 4 tahun
     * @param bdate, edate
     * http://localhost/E-Lions/report/bas.htm?window=jt_powersave4thn
     */
    public ModelAndView jt_powersave4thn(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	
    	Map<String, Object> m = new HashMap<String, Object>();
    	
    	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
    	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(30)));
    	
    	//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		
    		ArrayList data = Common.serializableList(uwManager.selectReportJTPowersave4thn(bdate, edate));
    		
    		if(data.size() > 0){ //bila ada data
	    		ServletOutputStream sos = response.getOutputStream();
	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.jt_powersave4thn") + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);
	    		params.put("pdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));

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
    		return null;
    	}
    
    	return new ModelAndView("report/jt_powersave4thn", m);
    }
    
    /**
     * Report Polis Powersave dan Stable Link untuk cabang
     * @author Canpri
     * @since 22 Apr 2013
     * @param bdate, edate
     * http://localhost/E-Lions/report/bas.htm?window=powersave_stableLink
     */
    public ModelAndView powersave_stableLink(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	int json = ServletRequestUtils.getIntParameter(request, "json", 0);

    	//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		
    		String lca = ServletRequestUtils.getStringParameter(request, "cabang");
    		String lwk = ServletRequestUtils.getStringParameter(request, "wakil");
    		String lsrg = ServletRequestUtils.getStringParameter(request, "region");
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		String polis = ServletRequestUtils.getStringParameter(request, "polis");
    		String agent = ServletRequestUtils.getStringParameter(request, "agent","none");
    		
    		ArrayList data = new ArrayList();
    		
    		if(polis.equals("powersave")){
    			data = Common.serializableList(uwManager.selectReport_Powersave(lca, lwk, lsrg, bdate, edate, currentUser.getLus_id(), agent));
    		}else{
    			data = Common.serializableList(uwManager.selectReport_Stablelink(lca, lwk, lsrg, bdate, edate, currentUser.getLus_id(), agent));
    		}
    		
    		if(data.size() > 0){ //bila ada data
    			ServletOutputStream sos = response.getOutputStream();
    			
    			File sourceFile = null;
    			if(polis.equals("powersave")){
    				sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.report_powersave") + ".jasper");
    			}else{
    				sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.report_stablelink") + ".jasper");
    			}
	    		
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);
	    		params.put("cabang", ServletRequestUtils.getStringParameter(request, "cabang2", "ALL"));
	    		params.put("wakil", ServletRequestUtils.getStringParameter(request, "wakil2", "ALL"));
	    		params.put("region", ServletRequestUtils.getStringParameter(request, "region2", "ALL"));

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
    	
    	//halaman depan
    	}else if(json == 0){
        	Map<String, Object> m = new HashMap<String, Object>();

        	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
	    	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(30)));
	    	
	    	m.put("listCabang", elionsManager.selectDropDown(
				"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lca_id", "eka.utils.cabang(c.lca_id)", "", "2", 
				"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id group by c.lca_id"));
	    	m.put("listWakil", elionsManager.selectDropDown(
				"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lwk_id", "eka.utils.kanwil_nama(c.lca_id, c.lwk_id)", "", "2", 
				"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id group by c.lca_id, c.lwk_id"));
	    	m.put("listRegion", elionsManager.selectDropDown(
				"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lsrg_id", "c.lsrg_nama", "", "2", 
				"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id group by c.lsrg_id, c.lsrg_nama"));

	    	return new ModelAndView("report/powersave_stableLink", m);
	    	
    	//tarik data wakil dari data cabang (ajax)
    	}else if(json == 1){ 
    		String lca = ServletRequestUtils.getStringParameter(request, "lca", "ALL");
    		ArrayList<DropDown> result = Common.serializableList(elionsManager.selectDropDown(
    			"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lwk_id", "eka.utils.kanwil_nama(c.lca_id, c.lwk_id)", "", "2", 
    			"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id " +(lca.equals("ALL") ?  "" : " and c.lca_id = '" +lca+ "' ")+ " group by c.lca_id, c.lwk_id"));
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();

       	//tarik data region dari data cabang dan region (ajax)
    	}else if(json == 2){
    		String lca = ServletRequestUtils.getStringParameter(request, "lca", "ALL");
    		String lwk = ServletRequestUtils.getStringParameter(request, "lwk", "ALL");
    		ArrayList<DropDown> result = Common.serializableList(elionsManager.selectDropDown(
    			"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lsrg_id", "c.lsrg_nama", "", "2", 
    			"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id " +(lca.equals("ALL") ? " " : " and c.lca_id = '" +lca+ "' ")+(lwk.equals("ALL") ? " " : " and c.lwk_id = '" +lwk+ "' ")+ " group by c.lsrg_id, c.lsrg_nama"));
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();
    	}
    	
		return null;
    }
    
    /**
     * Report Produksi Mingguan
     * @author Canpri
     * @since 23 Apr 2013
     * @param 
     * http://localhost/E-Lions/report/bas.htm?window=report_produksi_mingguan
     */
    public ModelAndView report_produksi_mingguan(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	int json = ServletRequestUtils.getIntParameter(request, "json", 0);
    	
    	Map<String, Object> m = new HashMap<String, Object>();

    	//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		String lus_id = ServletRequestUtils.getStringParameter(request, "lus_id","");
    		String kntr_pemasaran = ServletRequestUtils.getStringParameter(request, "kntr_pemasaran","");
    		
    		//List data = null;
    		ArrayList data = Common.serializableList(uwManager.selectReport_Produksi_Mingguan(bdate, edate, lus_id));
    		
    		
    		if(data.size() > 0){ //bila ada data
    			ServletOutputStream sos = response.getOutputStream();
    			File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.report_produksi_mingguan") + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);
	    		params.put("knt_pemasaran", kntr_pemasaran);

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
    	
    	//halaman depan
    	}else if(json == 0){

        	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(-7)));
        	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
	    	
	    	return new ModelAndView("report/produksi_mingguan",m);
	    	
    	//tarik data user (ajax)
    	}else if(json == 1){ 
    		String term = ServletRequestUtils.getRequiredStringParameter(request, "term");
			Object result = ajaxManager.selectUserList2(term.toUpperCase().trim());
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();
    	}
    
    	return null;
    }
    
    public ModelAndView report_further(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map param= new HashMap();
		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		Report report;
		report = new Report("Summary Further Branch",props.getProperty("report.bas.further_req"),Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
		
    }
    
    
    /**
     * Report Lisensi Agen
     * @author Canpri
     * @since 29 Nov 2013
     * @param 
     * http://localhost/E-Lions/report/bas.htm?window=report_lisensi_agen
     */
    public ModelAndView report_lisensi_agen(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	Map<String, Object> m = new HashMap<String, Object>();
    	int json = ServletRequestUtils.getIntParameter(request, "json", 0);
    	
    	String bdate = ServletRequestUtils.getStringParameter(request, "bdate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
		String edate = ServletRequestUtils.getStringParameter(request, "edate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
    	
		//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		String lca = ServletRequestUtils.getStringParameter(request, "cabang");
    		String lwk = ServletRequestUtils.getStringParameter(request, "wakil");
    		String lsrg = ServletRequestUtils.getStringParameter(request, "region");
    		
    		ArrayList data = Common.serializableList(bacManager.selectReport_LisensiAgent(bdate, edate, "1", currentUser.getLus_id(), currentUser.getLus_full_name(), lca, lwk, lsrg));
    		
    		if(data.size() > 0){ //bila ada data
    			ServletOutputStream sos = response.getOutputStream();
    			File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.report_lisensi_agen") + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);
	    		params.put("jenis", "1");
	    		params.put("pdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));

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
    		m.put("listCabang", elionsManager.selectDropDown(
				"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lca_id", "eka.utils.cabang(c.lca_id)", "", "2", 
				"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id and c.lca_id not in (09) group by c.lca_id"));
	    	m.put("listWakil", elionsManager.selectDropDown(
				"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lwk_id", "eka.utils.kanwil_nama(c.lca_id, c.lwk_id)", "", "2", 
				"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id and c.lca_id not in (09) group by c.lca_id, c.lwk_id"));
	    	m.put("listRegion", elionsManager.selectDropDown(
				"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lsrg_id", "c.lsrg_nama", "", "2", 
				"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id and c.lca_id not in (09) group by c.lsrg_id, c.lsrg_nama"));
    		
    		m.put("bdate", bdate);
    		m.put("edate", edate);
    		
    		return new ModelAndView("report/lisensi_agen",m);
    	}else if(json == 1){ 
    		String lca = ServletRequestUtils.getStringParameter(request, "lca", "ALL");
    		List<DropDown> result = elionsManager.selectDropDown(
    			"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lwk_id", "eka.utils.kanwil_nama(c.lca_id, c.lwk_id)", "", "2", 
    			"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id " +(lca.equals("ALL") ?  "" : " and c.lca_id = '" +lca+ "' ")+ " group by c.lca_id, c.lwk_id");
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();

       	//tarik data region dari data cabang dan wilayah (ajax)
    	}else if(json == 2){
    		String lca = ServletRequestUtils.getStringParameter(request, "lca", "ALL");
    		String lwk = ServletRequestUtils.getStringParameter(request, "lwk", "ALL");
    		List<DropDown> result = elionsManager.selectDropDown(
    			"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lsrg_id", "c.lsrg_nama", "", "2", 
    			"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id " +(lca.equals("ALL") ? " " : " and c.lca_id = '" +lca+ "' ")+(lwk.equals("ALL") ? " " : " and c.lwk_id = '" +lwk+ "' ")+ " group by c.lsrg_id, c.lsrg_nama");
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();
    	}
		
    	return null;
    }
    
    /**
     * Report Surat Domisili
     * @author Canpri
     * @since 1o Mar 2014
     * @param 
     * http://localhost/E-Lions/report/bas.htm?window=report_surat_domisili
     */
    public ModelAndView report_surat_domisili(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	Map<String, Object> m = new HashMap<String, Object>();
    	
    	String bdate = ServletRequestUtils.getStringParameter(request, "bdate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
		String edate = ServletRequestUtils.getStringParameter(request, "edate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
    	
    	if(request.getParameter("showReport") != null){
    		String lca = ServletRequestUtils.getStringParameter(request, "cabang");
    		String cabang = ServletRequestUtils.getStringParameter(request, "cabang2");
    		
    		ArrayList data = Common.serializableList(bacManager.selectAddressRegionLcaId(lca));
    		
    		if(data.size() > 0){ //bila ada data
    			ServletOutputStream sos = response.getOutputStream();
    			File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.report_surat_domisili") + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("cabang", cabang);
	    		params.put("pdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));

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
    		return null;
    	}
    	
		m.put("listCabang", elionsManager.selectDropDown("eka.lst_user a, eka.lst_user_admin b, eka.lst_addr_region c, eka.lst_cabang d", "d.lca_id", "d.lca_nama", "", "2", 
				"a.lus_id = b.lus_id and b.lar_id = c.lar_id and c.lar_lca_id = d.lca_id and a.lus_id = "+currentUser.getLus_id()+" group by d.lca_id, d.lca_nama"));
		
		return new ModelAndView("report/surat_domisili",m);
    }
    
    /**
     * Report Nilai Ujian CS Branch
     * @author Canpri
     * @since 13 Mar 2014
     * @param 
     * http://localhost/E-Lions/report/bas.htm?window=report_nilai_ujian
     */
    public ModelAndView report_nilai_ujian(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	Map<String, Object> m = new HashMap<String, Object>();
    	
    	Connection con = null;
    	ResultSet rs = null;
    	Statement st = null;
    	
    	ArrayList data = new ArrayList();
    	
    	try{
    		String dbFileName = "\\\\apache\\EkaWeb\\basbook\\db\\bas_mb_07.mdb";
    		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
    		String url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=" + 
							dbFileName + ";";
			con = DriverManager.getConnection(url);
			
			st = con.createStatement();
			rs = st.executeQuery("SELECT dt_lvl1.user_id, dt_lvl1.f_date, dt_lvl1.credit," +
					"dt_user.user_name, dt_user.login_name FROM dt_lvl1 INNER JOIN dt_user ON dt_lvl1.user_id = dt_user.user_id");
			if(rs.next()) { //ada data
				
				while(rs.next()){
					Map map = new HashMap();
					String user_id = rs.getString(1);
					Date tgl = rs.getDate(2);
					String nilai = rs.getString(3);
					String user_name  = rs.getString(4);
					String login_name = rs.getString(5);
					
					map.put("user_id", user_id);
					map.put("user_name", user_name);
					map.put("user_login", login_name);
					map.put("tgl", tgl);
					map.put("nilai", nilai);
					
					data.add(map);
				}
				
				if(request.getParameter("showReport") != null){
					ServletOutputStream sos = response.getOutputStream();
	    			File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.report_nilai_ujian") + ".jasper");
		    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
	
		    		Map<String, Object> params = new HashMap<String, Object>();
		    		params.put("user", currentUser.getLus_full_name());
		    		params.put("pdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
	
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
				}
				
			}else { //gak ada data
				ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Tidak ada data');window.close();</script>");
    			sos.close();
			}
			return null;
    	}catch(SQLException e){
    		e.getLocalizedMessage();
    		email.send(true, props.getProperty("admin.ajsjava"), new String[]{"canpri@sinarmasmsiglife.co.id"}, null, null, "Error Report Nilai Ujian", e.getLocalizedMessage().toString(), null);
		} finally {
			try {
				if(rs != null) rs.close();
				if(st != null) st.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				//e.getMessage();
				logger.error("ERROR :", e);
			}
		}
    	
    	m.put("data", data);
    	return new ModelAndView("report/nilai_ujian",m);
    }
    
    /**
     * Report Input Harian
     * @author Canpri
     * @since 13 Mar 2014
     * @param 
     * http://localhost/E-Lions/report/bas.htm?window=report_input_harian
     */
    public ModelAndView report_input_harian(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	Map<String, Object> m = new HashMap<String, Object>();
    	SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	
    	List produk = elionsManager.selectLstBisnis();
    	
    	String bdate = ServletRequestUtils.getStringParameter(request, "bdate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
		String edate = ServletRequestUtils.getStringParameter(request, "edate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
		
    	if(request.getParameter("showReport") != null){
    		String lsbs_id = ServletRequestUtils.getStringParameter(request, "produk","");
    		String prod_name = ServletRequestUtils.getStringParameter(request, "prod_text","ALL");
    		Integer type = ServletRequestUtils.getIntParameter(request, "type", 0);
    		String report = "";
    		String jn_report = "";
    		
    		ArrayList data = new ArrayList();
    		if(type==0){
    			data = Common.serializableList(bacManager.selectReportInputHarian(bdate, edate, lsbs_id));
    			report = props.getProperty("report.bas.report_input_harian") + ".jasper";
    		}else if(type==1){
    			data = Common.serializableList(bacManager.selectReportInputHarian(bdate, edate, "simpol"));
    			report = props.getProperty("report.bas.report_input_harian_new") + ".jasper";
    			jn_report = "SIMPOL";
    		}else if(type==2){
    			data = Common.serializableList(bacManager.selectReportInputHarian(bdate, edate, "agency"));
    			report = props.getProperty("report.bas.report_input_harian_new") + ".jasper";
    			jn_report = "AGENCY + EFC";
    		}
    		
    		if(data.size() > 0){ //bila ada data
    			ServletOutputStream sos = response.getOutputStream();
    			File sourceFile = Resources.getResourceAsFile(report);
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
	    		
	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);
	    		params.put("pdate", dt.format(new Date()));
	    		params.put("produk", prod_name);
	    		params.put("jn_report", jn_report);

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
    		return null;
		}
    	
    	
    	m.put("l_produk", produk);
    	m.put("bdate", bdate);
    	m.put("edate", edate);
    	return new ModelAndView("report/report_input_harian",m);
    }
    
    /**
     * Report Gagal Debet dan Outstanding Aging
     * @author Canpri
     * @since 13 Mar 2014
     * @param 
     * http://localhost/E-Lions/report/bas.htm?window=report_gagalDebetdanAging
     */
    public ModelAndView report_gagalDebetdanAging(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	Map<String, Object> m = new HashMap<String, Object>();
    	
    	String bdate = ServletRequestUtils.getStringParameter(request, "bdate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
		String edate = ServletRequestUtils.getStringParameter(request, "edate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
    	
    	if(request.getParameter("showReport") != null){
    		String lca_id =  ServletRequestUtils.getStringParameter(request, "cabang", "ALL");
    		String cabang =  ServletRequestUtils.getStringParameter(request, "cabang2", "ALL");
    		String jenis =  ServletRequestUtils.getStringParameter(request, "jenis", "1");
    		
    		if(jenis.equals("")){
    			ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Harap pilih jenis report!');window.close();</script>");
    			sos.close();
    		}else{
    			String report = "";
    			ArrayList data = Common.serializableList(bacManager.selectReportGagalDebetdanAging(bdate, edate, lca_id, jenis));
	    		
    			if(jenis.equals("1")){//list gagal debet
    				report = props.getProperty("report.bas.report_gagaldebet") + ".jasper";
    			}else{//list outstanding aging
    				report = props.getProperty("report.bas.report_aging") + ".jasper";
    			}
    			
	    		if(data.size() > 0){ //bila ada data
	    			ServletOutputStream sos = response.getOutputStream();
	    			File sourceFile = Resources.getResourceAsFile(report);
		    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
	
		    		Map<String, Object> params = new HashMap<String, Object>();
		    		params.put("pdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
		    		params.put("cabang", cabang);
		    		params.put("bdate", bdate);
		    		params.put("edate", edate);
	
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
    		}
    		
    		return null;
		}
    	
    	m.put("listCabang", elionsManager.selectDropDown(
				"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lca_id", "eka.utils.cabang(c.lca_id)", "", "2", 
				"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id and c.lca_id not in (09) group by c.lca_id"));
    	m.put("bdate", bdate);
    	m.put("edate", edate);
    	
    	return new ModelAndView("report/report_gagalDebetdanAging",m);
    }
    
    /**
     * Report Pending Spaj Bas, req Yune(Helpdesk #20907)
     * http://apache/E-Lions/report/bas.htm?window=report_pending_spaj_bas
     * Lufi - 23 May 2014     
     */
    public ModelAndView report_pending_spaj_bas(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");

    	//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		
    		String cabang = ServletRequestUtils.getStringParameter(request, "cabang");    	
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		String jn_report = ServletRequestUtils.getStringParameter(request, "jn_report", "0");
    		
    		ArrayList data = new ArrayList();
    		
    		if(request.getParameter("showReport") != null){ //bila ada data
	    		ServletOutputStream sos = response.getOutputStream();
	    		File sourceFile = null;
	    		
	    		if(jn_report.equals("0")){
	    			data=Common.serializableList(bacManager.selectDataListPendingBas(cabang, bdate, edate));
	    			sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.list_pending") + ".jasper");
	    		}else{
	    			data=Common.serializableList(bacManager.selectDataPersentasePendingBas(cabang, bdate, edate));
	    			sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.persentase_pending") + ".jasper");
	    		}
	    		
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("awal", bdate);
	    		params.put("akhir", edate);
	    		params.put("cabang", ServletRequestUtils.getStringParameter(request, "cabang2", "ALL"));
	    		

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
    		return null;
    	} 
    	Map<String, Object> m = new HashMap<String, Object>();
    	m.put("sekarang", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
    	m.put("besok", defaultDateFormat.format(uwManager.selectSysdateTruncated(1)));
    	m.put("listCabang", elionsManager.selectDropDown(
				"eka.lst_user", "lca_id", "eka.utils.cabang(lca_id)", "", "2", 
				"lus_bas = 1 and lus_active = 1 group by lca_id"));   	
        return new ModelAndView("report/report_pending_spaj_bas", m);   	
    }    
    
    /**
     * Report Followup
     * @author Canpri / Rahmayanti (penambahan list jatuh tempo)
     * @since 9 Juni 2014 / 17 Juli 2014
     * @param 
     * http://localhost/E-Lions/report/bas.htm?window=report_followup
     */
    public ModelAndView report_followup(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HashMap<String, Object> hm_map = new HashMap<String, Object>();
    	
    	String s_bdate = ServletRequestUtils.getStringParameter(request, "bdate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
		String s_edate = ServletRequestUtils.getStringParameter(request, "edate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
    	
		if(request.getParameter("showReport") != null){
			String s_jenis = ServletRequestUtils.getStringParameter(request, "jenis", "1");
			String s_jn_tgl = ServletRequestUtils.getStringParameter(request, "jn_tgl", "1");
			
			String s_jenis_report = "";
			String s_jenis_tanggal = "";
			String s_rep = "";
			
			if(s_jenis.equals("0"))s_jenis_report = "Aging (Follow Up)";
			else if(s_jenis.equals("1"))s_jenis_report = "Jatuh tempo (Follow Up)";
			else if(s_jenis.equals("2"))s_jenis_report = "Aging (Follow Up)";
			else if(s_jenis.equals("3"))s_jenis_report = "List Jatuh Tempo";
			
			if(s_jn_tgl.equals("0"))s_jenis_tanggal = "Billing";
			else if(s_jn_tgl.equals("1"))s_jenis_tanggal = "Followup";
			
			ArrayList data = new ArrayList();
			
			if(s_jenis.equals("3")){
				data=Common.serializableList(bacManager.reportFollowupTempo(s_bdate, s_edate, s_jenis, s_jn_tgl));
				s_rep = props.getProperty("report.bas.report_followup_tempo");
			}else{
				data = Common.serializableList(bacManager.selectFollowupKategori(s_bdate, s_edate, s_jenis, s_jn_tgl));
				s_rep = props.getProperty("report.bas.followup.kategori");
			}
			
			if(data.size() > 0){ //bila ada data
    			ServletOutputStream sos = response.getOutputStream();
    			File sourceFile = Resources.getResourceAsFile(s_rep + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("pdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
	    		params.put("jenis_report", s_jenis_report);
	    		params.put("jenis_tgl", s_jenis_tanggal);
	    		params.put("bdate", s_bdate);
	    		params.put("edate", s_edate);

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
			return null;
		}
		
		hm_map.put("bdate", s_bdate);
		hm_map.put("edate", s_edate);
    	return new ModelAndView("report/reportFollowup", hm_map);
    }
    
    /**
     * Report Stock Spaj Cabang
     * @author Canpri
     * @since 9 Juni 2014
     * @param 
     * http://localhost/E-Lions/report/bas.htm?window=report_stock_spaj
     */
    public ModelAndView report_stock_spaj(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HashMap<String, Object> hm_map = new HashMap<String, Object>();
    	String judul ="Report Stock SPAJ";
    	String s_bdate = ServletRequestUtils.getStringParameter(request, "bdate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
		String s_edate = ServletRequestUtils.getStringParameter(request, "edate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
		String s_mode = ServletRequestUtils.getStringParameter(request, "mode","");
		
		if(s_mode.equalsIgnoreCase("report")){
			String user = ServletRequestUtils.getStringParameter(request, "seluserBas","");
			Report r_report = null;
			String s_jenis = ServletRequestUtils.getStringParameter(request, "jenis","Cabang");
			String s_user = "";
			
			if(s_jenis.equalsIgnoreCase("Cabang")) {
				if(user.equals(""))s_user = "and a.mss_jenis = 0";
				else s_user = "and a.mss_jenis = 0 and a.lus_id = '"+user+"'";
				
				r_report = new Report("Report Stock Spaj Cabang",props.getProperty("report.bas.stok_spaj_cabang"),Report.PDF, null);
	    	}
	    	else {
				if(user.equals(""))s_user = "and a.msf_amount > 0";
				else s_user = "and a.msf_amount > 0 and a.lus_id = '"+user+"'";		 
	    		
	    		r_report = new Report("Report Stock Spaj Agent",props.getProperty("report.bas.stok_spaj_agent"),Report.PDF, null);
	    	}
			
			r_report.addParamDefault("bdate", "bdate", 0, s_bdate,  false);
			r_report.addParamDefault("edate", "edate", 0, s_edate,  false);
			r_report.addParamDefault("mode", "mode", 0, s_mode,  false);
			r_report.addParamDefault("jenis", "jenis", 0, s_jenis,  false);
			r_report.addParamDefault("user", "user", 0, s_user,  false);
			r_report.addParamDefault("seluserBas", "seluserBas", 0, user,  false);
			
			return prepareReport(request, response, r_report);
		}
		
		hm_map.put("bdate", s_bdate);
		hm_map.put("edate", s_edate);
    	return new ModelAndView("report/reportStokSpaj", hm_map);
    }
    
    /**
     * Report E-SPAJ
     * @author Randy
     * @since 15 Juli 2016
     * @param 
     * http://localhost/E-Lions/report/bas.htm?window=report_e_spaj
     */
    public ModelAndView report_e_spaj(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	Map<String, Object> m = new HashMap<String, Object>();
    	SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	    	
    	String bdate = ServletRequestUtils.getStringParameter(request, "bdate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
		String edate = ServletRequestUtils.getStringParameter(request, "edate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
		
    	if(request.getParameter("showReport") != null){
    		ArrayList data = new ArrayList();
//    			data = Common.serializableList(bacManager.selectGadgetisHere());
    			data = Common.serializableList(bacManager.selectReportESpaj(bdate, edate));
    			String report = props.getProperty("report.bas.report_e_spaj") + ".jasper";
    		
    		
    		if(data.size() > 0){ //bila ada data
    			ServletOutputStream sos = response.getOutputStream();
    			File sourceFile = Resources.getResourceAsFile(report);
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
	    		
	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);
	    		params.put("pdate", dt.format(new Date()));

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
    		return null;
		}
    	
    	m.put("bdate", bdate);
    	m.put("edate", edate);
    	return new ModelAndView("report/reportESpaj",m);
    }
    
    public ModelAndView reportFiestaVisa(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HashMap<String, Object> hm_map = new HashMap<String, Object>();
    	
    	String s_bdate = ServletRequestUtils.getStringParameter(request, "bdate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
		String s_edate = ServletRequestUtils.getStringParameter(request, "edate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
		String s_mode = ServletRequestUtils.getStringParameter(request, "mode","");
		String judul ="Report Fiesta Visa";
		if(s_mode.equalsIgnoreCase("report")){
			String user = ServletRequestUtils.getStringParameter(request, "seluserBas","");
			Report r_report = null;
			String s_jenis = ServletRequestUtils.getStringParameter(request, "jenis","Cabang");
			String s_user = "";
			
			r_report=new Report("Report Fiesta Visa",props.getProperty("report.bas.visa"),Report.PDF, null);
			
			r_report.addParamDefault("bdate", "bdate", 0, s_bdate,  false);
			r_report.addParamDefault("edate", "edate", 0, s_edate,  false);
			r_report.addParamDefault("mode", "mode", 0, s_mode,  false);
			r_report.addParamDefault("jenis", "jenis", 0, s_jenis,  false);
			r_report.addParamDefault("user", "user", 0, s_user,  false);
			r_report.addParamDefault("seluserBas", "seluserBas", 0, user,  false);
			
			return prepareReport(request, response, r_report);
		}
		
		hm_map.put("bdate", s_bdate);
		hm_map.put("edate", s_edate);
		hm_map.put("judul", judul);
    	return new ModelAndView("report/reportStokSpaj", hm_map);
		
    }
    
    public ModelAndView reportTheGreat(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HashMap<String, Object> hm_map = new HashMap<String, Object>();
    	
    	String s_bdate = ServletRequestUtils.getStringParameter(request, "bdate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
		String s_edate = ServletRequestUtils.getStringParameter(request, "edate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
		String s_mode = ServletRequestUtils.getStringParameter(request, "mode","");
		String judul ="Report SLA";
		HashMap mapReport = new HashMap();
		if(s_mode.equalsIgnoreCase("report")){
			String user = ServletRequestUtils.getStringParameter(request, "seluserBas","");
			Report r_report = null;
			String s_jenis = ServletRequestUtils.getStringParameter(request, "jenis","");
			String s_user = "";
			if(s_jenis.equalsIgnoreCase("Great")) {
				mapReport=bacManager.selectMstConfig(6, "Report Team The Great", "report.bas.thegreat");
				/*String value =  "/WEB-INF/classes/" + mapReport.get("NAME") + ".jrxml";
				String value2 =  "//WEB-INF/classes/" + mapReport.get("NAME") + ".jasper";
				//"/WEB-INF/classes/" + value + ".jrxml";
				File report = new File(value2);
				if (!report.exists()){
					JasperCompileManager.compileReportToFile(value);
				}*/
				r_report=new Report("Report SPAJ Team The Great",props.getProperty("report.bas.thegreat"),Report.PDF, null);
				r_report.addParamDefault("cabang", "cabang", 0, "Team The Great",  false);
				r_report.addParamDefault("where", "where", 0, "Team The Great",  false);
			} else if (s_jenis.equalsIgnoreCase("Agency")) {
				mapReport=bacManager.selectMstConfig(6, "Report SLA SIMPOL", "report.sla.simpol");
			/*	String value =  "E:/Java/Tomcat7/webapps/E-Lionsx/WEB-INF/classes/" + mapReport.get("NAME") + ".jrxml";
				String value2 =  "/WEB-INF/classes/" + mapReport.get("NAME") + ".jasper";
				//"/WEB-INF/classes/" + value + ".jrxml";
				File report = new File(value2);
				if (!report.exists()){
					JasperCompileManager.compileReportToFile(value);
				}*/
				r_report=new Report("Report SPAJ SLA SIMPOL",props.getProperty("report.bas.report_sla_agency"),Report.PDF, null);
				r_report.addParamDefault("cabang", "cabang", 0, "Report SPAJ SLA SIMPOL",  false);
				r_report.addParamDefault("where", "where", 0, "Team The Great",  false);
			}else{
				mapReport=bacManager.selectMstConfig(6, "Report SLA SIMPRIM", "report.sla.simprim");
				/*String value =  "/WEB-INF/classes/" + mapReport.get("NAME") + ".jrxml";
				String value2 =  "/WEB-INF/classes/" + mapReport.get("NAME") + ".jasper";
				//"/WEB-INF/classes/" + value + ".jrxml";
				File report = new File(value2);
				if (!report.exists()){
					JasperCompileManager.compileReportToFile(value);
				}*/
				r_report=new Report("Report SPAJ SLA SIMAS PRIMA",props.getProperty("report.bas.report_sla_bancass"),Report.PDF, null);
				r_report.addParamDefault("cabang", "cabang", 0, "Report SPAJ SLA SIMPOL",  false);
				r_report.addParamDefault("where", "where", 0, "Team The Great",  false);
			}
			
			r_report.addParamDefault("bdate", "bdate", 0, s_bdate,  false);
			r_report.addParamDefault("edate", "edate", 0, s_edate,  false);
			r_report.addParamDefault("mode", "mode", 0, s_mode,  false);
			r_report.addParamDefault("jenis", "jenis", 0, s_jenis,  false);
			r_report.addParamDefault("user", "user", 0, s_user,  false);
			r_report.addParamDefault("seluserBas", "seluserBas", 0, user,  false);
			
			return prepareReport(request, response, r_report);
		}
		
		hm_map.put("bdate", s_bdate);
		hm_map.put("edate", s_edate);
		hm_map.put("judul", judul);
    	return new ModelAndView("report/reportStokSpaj", hm_map);
		
    }
    
    
    /**
     * Report Komisi Agen pending
     * @author Canpri
     * @since 9 Juni 2014
     * @param 
     * http://localhost/E-Lions/report/bas.htm?window=komisi_agen_pending
     */
    public ModelAndView komisi_agen_pending(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	HashMap<String, Object> hm_map = new HashMap<String, Object>();
    	HttpSession session = request.getSession();
    	int json = ServletRequestUtils.getIntParameter(request, "json", 0);
    	
    	if(json==1){
    		String s_msag_id = ServletRequestUtils.getStringParameter(request, "msag_id", "");
    		
    		Agen result = bacManager.select_detilagen3(s_msag_id);
    		
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();
    	}else{
    		String s_msag_id = ServletRequestUtils.getStringParameter(request, "msag_id", "");
    		String s_nm_agent = ServletRequestUtils.getStringParameter(request, "nm_agent", "");
    		String s_polis = ServletRequestUtils.getStringParameter(request, "polis", "");
    		
    		if(request.getParameter("btnView") != null){
        		ArrayList ls_komisi = Common.serializableList(bacManager.selectKomisiPendingAgent(s_msag_id, s_polis));
        		
        		hm_map.put("ls_komisi", ls_komisi);
        		session.setAttribute("komisi_pending", ls_komisi);
        	}
    		
    		if(request.getParameter("btnKirim") != null){
    			Upload upload = new Upload();
    			ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
    			binder.bind(request);
    			
    			Integer i_jn_alasan = ServletRequestUtils.getIntParameter(request, "selPending", 1);
    			String s_ket = ServletRequestUtils.getStringParameter(request, "ket", "");
    			String[] to = null;
    			String[] cc = null;
    			
    			if(i_jn_alasan==1){
    				to = new String[]{"monica@sinarmasmsiglife.co.id", "siti_p@sinarmasmsiglife.co.id", "yolanda@sinarmasmsiglife.co.id"};
    				cc = new String[]{"mariadf@sinarmasmsiglife.co.id"};
    			}else if(i_jn_alasan==2){
    				to = new String[]{"wenny@sinarmasmsiglife.co.id", "wasisti.ardi@sinarmasmsiglife.co.id"};
    				cc = new String[]{"tri.handini@sinarmasmsiglife.co.id"};
    			}else if(i_jn_alasan==3){
    				to = new String[]{"riyadi@sinarmasmsiglife.co.id"};
    				cc = new String[]{"wenny@sinarmasmsiglife.co.id", "murni@sinarmasmsiglife.co.id", "ursula@sinarmasmsiglife.co.id"};
    			}else{
    				to = new String[]{"canpri@sinarmasmsiglife.co.id"};
    			}
    			
    			if(!upload.getFile1().isEmpty()){
    				String dest = props.getProperty("pdf.dir.doc.agent") +"\\"+upload.getFile1().getOriginalFilename();
    				File outputFile = new File(dest);
    				FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
    				
    				ArrayList<File> attachments = new ArrayList<File>();
			        attachments.add(outputFile);
			        
			        ArrayList ls_komisi = (ArrayList) session.getAttribute("komisi_pending");
			        
			        StringBuffer mssg = new StringBuffer();
			        mssg.append("<table border='1' cellpadding='0' cellspacing='0' style='font-size:12px;'><tr><th>No</th><th>No Polis</th><th>Nama Pemegang</th><th>Jumlah Komisi</th>");
			        mssg.append("<th>Tahun Ke</th><th>Premi Ke</th><th>Kode Agent</th><th>Nama Agent</th><th>Status Aktif</th>");
			        mssg.append("<th>Status Sertifikat</th><th>Tabungan</th><th>Status Bill</th><th>Akseptasi Khusus</th></tr>");
			        
			        for(int i=0;i<ls_komisi.size();i++){
			        	Map m = (Map) ls_komisi.get(i);
			        	
			        	mssg.append("<tr><td>"+i+1+".</td><td>"+(String)m.get("MSPO_POLICY_NO")+"</td><td>"+(String)m.get("PEMEGANG")+"</td>");
			        	mssg.append("<td>"+((BigDecimal)m.get("JUMLAH_KOMISI")).doubleValue()+"</td><td>"+((BigDecimal)m.get("MSBI_TAHUN_KE")).intValue()+"</td><td>"+((BigDecimal)m.get("MSBI_PREMI_KE")).intValue()+"</td>");
						mssg.append("<td>"+(String)m.get("AGENT_PENUTUP")+"</td><td>"+(String)m.get("NAMA_AGENT")+"</td><td>"+(String)m.get("STS_AKTIF")+"</td><td>"+(String)m.get("STS_SERTIFIKASI")+"</td>");
						mssg.append("<td>"+(String)m.get("BANK")+"</td><td>"+(String)m.get("STAT_BILL")+"</td><td>"+(String)m.get("AKSEPTASI_KHUSUS")+"</td></tr>");
						
			        }
			        
			        mssg.append("</table>");
			        
			        EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, elionsManager.selectSysdate(), null, true, 
							props.getProperty("admin.ajsjava"), to, cc, null, 
							"Komis Pending Agent", s_ket+"\n\n"+mssg.toString(), attachments, null);
			        
			        hm_map.put("pesan", "Dokumen berhasil dikirim");
			        
			        session.removeAttribute("komisi_pending");
    			}else{
    				hm_map.put("pesan", "Harap upload dokumen yang di butuhkan");
    			}
    		}
    		
    		hm_map.put("msag_id", s_msag_id);
    		hm_map.put("nm_agent", s_nm_agent);
    		hm_map.put("polis", s_polis);
    	}
    	
    	return new ModelAndView("report/komisi_agen_pending", hm_map);
    }
    
    /**
     * @author lufi
     * @since nov 27, 2014 (11:30:00 AM)
     */    
    public ModelAndView smilePrioritas(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HashMap<String, Object> cmd = new HashMap<String, Object>();
    	User currentUser = (User) request.getSession().getAttribute("currentUser");		
		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
		String edate = ServletRequestUtils.getStringParameter(request, "edate");
		String jn_report = ServletRequestUtils.getStringParameter(request, "jenisReport");		
		String report = ServletRequestUtils.getStringParameter(request, "showReport");		
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");	
		List<DropDown> jenisReport = new ArrayList<DropDown>();			
		jenisReport.add(new DropDown("0","Report Produksi"));
		jenisReport.add(new DropDown("1","Report Input Data"));	
		
		ArrayList data=new ArrayList();
		HashMap mapReport = new HashMap();
		if(request.getParameter("showReport") != null){ 			
    		File sourceFile = null;
			if(jn_report.equals("0")){
				mapReport=bacManager.selectMstConfig(6, "Report Produksi Smile Prioritas", "report.produksi.sp");				
			}else if(jn_report.equals("1")){
				mapReport=bacManager.selectMstConfig(6, "Report Input Smile Prioritas", "report.input.sp");	
			}
			data=Common.serializableList(bacManager.selectDataSmilePrioritas(bdate,edate,jn_report,1));
			sourceFile = Resources.getResourceAsFile(mapReport.get("NAME") + ".jasper");
			if(data!=null){
				ServletOutputStream sos = response.getOutputStream();
				JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);  		

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
			}else{
				ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Tidak ada data');window.close();</script>");
    			sos.close();
			}
			return null;
		}
		cmd.put("bdate", dt.format(elionsManager.selectSysdate()));	
		cmd.put("edate", dt.format(elionsManager.selectSysdate()));
		cmd.put("jenisReport", jenisReport);
    	return new ModelAndView("report/smileprioritas", cmd);
    }
    
    /**
     * @author Canpri, Rahmayanti
     * @since jun 25, 2015 (11:30:00 AM) -  okt 01, 2015
     * http://localhost/E-Lions/report/bas.htm?window=follbill
     */    
    public ModelAndView follbill(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	HashMap<String, Object> hm_map = new HashMap<String, Object>();
    	
    	if(request.getParameter("showReport") != null){
    		String type = ServletRequestUtils.getStringParameter(request, "type");
    		String a_beg_date = ServletRequestUtils.getStringParameter(request, "a_beg_date");
    		String a_end_date = ServletRequestUtils.getStringParameter(request, "a_end_date");
    		
    		String s_rep = "";
    		
    		if(type.equals("0"))s_rep = props.getProperty("report.bas.report_aging_followup_kategori");
    		else if(type.equals("1"))s_rep = props.getProperty("report.bas.report_aging_followup_user");
    		if(request.getParameter("showXLS") != null||request.getParameter("showPDF") != null){
//    			bacManager.updateListAgingFollowup(a_beg_date, a_end_date);
    		}
    		
    		ArrayList data = new ArrayList(); 
    		data = Common.serializableList(bacManager.selectReportAgingFollowup(a_beg_date, a_end_date));
    		if(data.size() > 0){ //bila ada data
    			ServletOutputStream sos = response.getOutputStream();
    			File sourceFile = Resources.getResourceAsFile(s_rep + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("pdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
	    		params.put("a_bdate", a_beg_date);
	    		params.put("a_edate", a_end_date);

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
    		return null;
    	}
//    	if(request.getParameter("showReport_uf") != null){
//    		String type = ServletRequestUtils.getStringParameter(request, "type2");
//    		String a_beg_date = ServletRequestUtils.getStringParameter(request, "a_beg_date_uf");
//    		String a_end_date = ServletRequestUtils.getStringParameter(request, "a_end_date_uf");
//    		
//    		String s_rep = "";
//    		
//    		if(type.equals("0"))s_rep = props.getProperty("report.bas.report_aging_followup_kategori");
//    		else if(type.equals("1"))s_rep = props.getProperty("report.bas.report_aging_followup_user");
//    		if(request.getParameter("showXLS_uf") != null||request.getParameter("showPDF_uf") != null){
////    			bacManager.updateListAgingFollowup(a_beg_date, a_end_date);
//    		}
//    		
//    		ArrayList data = new ArrayList(); 
//    		data = Common.serializableList(bacManager.selectReportAgingFollowup(a_beg_date, a_end_date));
//    		if(data.size() > 0){ //bila ada data
//    			ServletOutputStream sos = response.getOutputStream();
//    			File sourceFile = Resources.getResourceAsFile(s_rep + ".jasper");
//	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
//
//	    		Map<String, Object> params = new HashMap<String, Object>();
//	    		params.put("pdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
//	    		params.put("a_bdate", a_beg_date);
//	    		params.put("a_edate", a_end_date);
//
//	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));
//
//		    	if(request.getParameter("showXLS_uf") != null){
//		    		response.setContentType("application/vnd.ms-excel");
//		            JRXlsExporter exporter = new JRXlsExporter();
//		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
//		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
//		            exporter.exportReport();
//		    	}else if(request.getParameter("showPDF_uf") != null){
//		    		JasperExportManager.exportReportToPdfStream(jasperPrint, sos);
//		    	}
//	    		sos.close();
//    		}else{ //bila tidak ada data
//    			ServletOutputStream sos = response.getOutputStream();
//    			sos.println("<script>alert('Tidak ada data');window.close();</script>");
//    			sos.close();
//    		}
//    	}
    	
    	if(request.getParameter("upload") != null){
    		String beg_date_aging = ServletRequestUtils.getStringParameter(request, "beg_date", null);
    		String end_date_aging = ServletRequestUtils.getStringParameter(request, "end_date", null);
    		if(beg_date_aging!=null && end_date_aging!=null){
        		Upload upload = new Upload();
        		
    			ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
    			binder.bind(request);
    			MultipartFile mf = upload.getFile1();
    			String fileName = mf.getOriginalFilename();
    			String directory = props.getProperty("temp.dir.fileupload"); //file_fp.replaceAll(fileName, "");
    			
    			ExcelRead excelRead = new ExcelRead();
    			Mia agencyExcel = null;
    			
    			List<List> ExcelList = new ArrayList<List>();
    					
    			try{
    				String dest=directory + "/" + fileName ;
    				File outputFile = new File(dest);
    			    FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
    				ExcelList = excelRead.readExcelFile(directory + "\\", fileName);
    			}catch (Exception e) {
    				hm_map.put("sts", "FAILED");
    				hm_map.put("msg", "FAILED:: akses file gagal");
    			}
    			
    			for(int i = 1 ; i < ExcelList.size() ; i++){
    				if(ExcelList.get(i) != null){
    				if(ExcelList.get(i).size() > 0){
    				if(!ExcelList.get(i).get(0).toString().isEmpty()){
    					Map<String,String> map = new HashMap<String, String>();
    					try{
    						String polis = ExcelList.get(i).get(0).toString();
    						String pemegang = ExcelList.get(i).get(1).toString();    						
    						if(bacManager.selectAgingFollowup(polis, beg_date_aging, end_date_aging) == 0){
    							bacManager.insertAgingFollowup(polis, pemegang, beg_date_aging, end_date_aging);
    						}
    						
    					}catch (Exception e) {
    						TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
    					}
    				}
    				}
    				}
    			}
    			if(ExcelList==null)	hm_map.put("sts", "FAILED");
    			else hm_map.put("sts", "Upload sukses");
    		}else{
    			hm_map.put("err", "Tanggal Aging Harap Diisi");
    		}
    	}
    	
    	return new ModelAndView("report/follbill", hm_map);
    }
    
    /**
     * @author Ryan
     * @since 22 Agustus 2012
     * @category Report Back To Bas
     * @param tglAwal, tglAkhir
     * http://localhost/E-Lions/report/bas.htm?window=report_back_tobas
     */
    public ModelAndView report_back_tobas(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	Map<String, Object> m = new HashMap<String, Object>();
    	List<DropDown> userBas = uwManager.selectUserBasSummaryInput(props.getProperty("user.bas.summaryinput"));
    	m.put("userBas", userBas);
    	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
    	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(30)));
    	m.put("kode","1");
    	
    	//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		String jenis = ServletRequestUtils.getStringParameter(request, "jenis", "0");
    		Integer i_trans = ServletRequestUtils.getIntParameter(request, "type", 0);
    		String userBasId = ServletRequestUtils.getStringParameter(request, "seluserBas", "ALL");
    		
    		String jn_tgl = "";
    		if(jenis.equals("0")){
    			jn_tgl = "Input";
    		}else{
    			jn_tgl = "Scan";
    		}
    		
    		ArrayList data = Common.serializableList(bacManager.selectReportDataBackToBas(bdate, edate, jenis, i_trans,userBasId ));
    		
    		if(data.size() > 0){ //bila ada data
	    		ServletOutputStream sos = response.getOutputStream();
	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.reportErrorBackToBas") + ".jasper");
	    		//File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.reportDataUpload") + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);
	    		params.put("pdate", defaultDateFormat.format(elionsManager.selectSysdate()));
	    		params.put("jenis", jn_tgl);
	    		if(i_trans==0)params.put("jn_transfer", "(Belum Transfer ke Speedy)");
	    		else if(i_trans==1)params.put("jn_transfer", "(Back to BAS)");

	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));
	    		m.put("userBas", userBas);
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
    		m.put("userBas", userBas);
    		return null;
    	}
    	m.put("userBas", userBas);
    	return new ModelAndView("report/reportDataUpload", m);
    }
    
    /**
     * @author Ryan
     * @since 21 Nov 2015
     * @category Report Penerimaan Berkas
     * @param tglAwal, tglAkhir
     * http://localhost/E-Lions/report/bas.htm?window=report_penerimaan_berkas
     */
    public ModelAndView report_penerimaan_berkas(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User) request.getSession().getAttribute("currentUser");
    	Date today = bacManager.selectSysdate();
    	Map param= new HashMap();
//		String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
//		String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		Report report;
		report = new Report("Report Penerimaan Berkas",props.getProperty("report.bas.report_penerimaan_bSPAJ"),Report.PDF, null);
		report.addParamDate("Tanggal Produksi", "tanggal", true, new Date[] { today, today }, true);
		

//		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
//		report.addParamDefault("bdate", "tanggalAwal", 0,tanggalAwal,  false);
//		report.addParamDefault("edate", "tanggalAkhir", 0,tanggalAkhir,  false);
//		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
		
    	/*User currentUser = (User) request.getSession().getAttribute("currentUser");
    	Map<String, Object> m = new HashMap<String, Object>();
    	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
    	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(30)));
    	m.put("kode","3");
    	//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		String jenis = ServletRequestUtils.getStringParameter(request, "type", "");
    		
    		
    		ArrayList data = Common.serializableList(bacManager.selectReportDataPenerimaanBSPAJ(bdate, edate, jenis ));
    		
    		if(data.size() > 0){ //bila ada data
	    		ServletOutputStream sos = response.getOutputStream();
	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.report_penerimaan_bSPAJ") + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);
	    		params.put("jenis", jenis);

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
    	}
    	return new ModelAndView("report/reportDataUpload", m);*/
    }    
    
    /**
     * @author Rahmayanti
     * @since 02/11/2015
     * @category QA 1
     * http://localhost/E-Lions/report/bas.htm?window=qa_1
     */
    public ModelAndView qa_1(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HashMap<String, Object> hm_map = new HashMap<String, Object>();
    	User currentUser = (User) request.getSession().getAttribute("currentUser");
    	hm_map.put("posisi", 218);
    	hm_map.put("daftarSPAJ", bacManager.daftarSPAJ(currentUser, "218"));
    	return new ModelAndView("bas/qa", hm_map);    	
    }

    /**
     * @author Rahmayanti
     * @since 02/11/2015
     * @category QA 2
     * http://localhost/E-Lions/report/bas.htm?window=qa_2
     */
    public ModelAndView qa_2(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HashMap<String, Object> hm_map = new HashMap<String, Object>();
    	User currentUser = (User) request.getSession().getAttribute("currentUser");
    	hm_map.put("posisi", 219);
    	hm_map.put("daftarSPAJ", bacManager.daftarSPAJ(currentUser, "219"));
    	return new ModelAndView("bas/qa", hm_map);   	
    }
    
    /**
     * @author Rahmayanti, Ridhaal
     * @since 02/11/2015, 25/03/2015
     * @category Transfer to Qa 2 , diubah dari QA1 Transfer to Speedy
     * http://localhost/E-Lions/report/bas.htm?window=qa_2
     */
    public ModelAndView transferTo_Qa2(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HashMap<String, Object> hm_map = new HashMap<String, Object>();
    	User currentUser = (User) request.getSession().getAttribute("currentUser");
    	String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
    	Integer i_owner = ServletRequestUtils.getIntParameter(request, "owner",1);
		Integer i_parent = ServletRequestUtils.getIntParameter(request, "parent",1);
		Integer i_agent = ServletRequestUtils.getIntParameter(request, "agent",1);
		Integer i_hq = ServletRequestUtils.getIntParameter(request, "health_quest",1);
		
		Integer i_posisi = elionsManager.selectPosisiDokumenBySpaj(spaj);
		Integer lsbs_id = Integer.parseInt(uwManager.selectLsbsId(spaj));
		Integer lsdbs_no = Integer.parseInt(uwManager.selectLsdbsNumber(spaj));
		
		if(i_posisi==218||i_posisi==219){
			StringBuffer err_speedy = new StringBuffer();
			StringBuffer suc_speedy = new StringBuffer();
			Tertanggung ttg = elionsManager.selectttg(spaj);
			Datausulan datausulan = elionsManager.selectDataUsulanutama(spaj);
			Map temp = elionsManager.selectKonfirmasiTransferBac(spaj);
			
			if(temp != null) {
				
				//compare signature in all form
				if(i_owner == 0){
					err_speedy.append("<br>- Owner's & Insured's Signature tidak sama");
				}	
				if(ttg.getMste_age() < 17){
					if(i_parent == 0){
						err_speedy.append("<br>- Parent's Signature tidak ada");
					}
				}
				if(i_agent == 0){
					err_speedy.append("<br>- Agent's Signature tidak sama/tidak ada");
				}
				if(i_posisi==218 &&i_hq == 0){
					err_speedy.append("<br>- Further unclean, Harap back to admin");
				}
				
				if(err_speedy.length()<=0){
					if(i_posisi==218){ 
						if( (lsbs_id==190 && lsdbs_no==9) || (lsbs_id==200 && lsdbs_no==7) || (lsbs_id==212 && (lsdbs_no==6 || lsdbs_no==9 || lsdbs_no==10 || lsdbs_no==14)) || (lsbs_id==223 && lsdbs_no==1) || // add lsdbs_no 14 nana
							(lsbs_id==208 && (lsdbs_no>20 && lsdbs_no<25) && datausulan.getMspr_tsi().doubleValue()<=100000000.0) ||
							(lsbs_id==217 && lsdbs_no==2) ||
							(lsbs_id==223 && lsdbs_no==2) || //helpdesk [138638] produk baru SLP Syariah (223-2)
							(lsbs_id==142 && lsdbs_no==13)){ //helpdesk [133346] produk baru 142-13 Smart Investment Protection
							HashMap<String, Object> mapAutoAccept = new HashMap<String, Object>();
							Pemegang pmg = elionsManager.selectpp(spaj);
							mapAutoAccept = Common.serializableMap(bacManager.ProsesAutoAccept(spaj, 1, pmg, ttg, currentUser, request, elionsManager));
							hm_map.put("flag_sukses", 3);
							hm_map.put("successMessage", mapAutoAccept.get("success").toString());
						}else{
							bacManager.transferPosisi(currentUser, spaj, 27, "TRANSFER KE SPEEDY", "tgl_transfer_uw_speedy", 0);
							hm_map.put("flag_sukses", 3);
							hm_map.put("successMessage", "Proses Transfer ke New Business berhasil dilakukan.");	
						}					
					}
				}else{
					hm_map.put("lsError",err_speedy.toString());					
				}
			}
		}
    	return new ModelAndView("common/info_proses", hm_map);    	
    }
    
    /**
	 * Controller untuk transfer ke QA ke Admin
	 * @author Rahmayanti, Ridhaal
	 * @since 03/11/2015, , 25/03/2015
	 * @param request
	 * @param response
	 * @return
	 * @link http://localhost/E-Lions/report/bas.htm?window=backtoAdmin
	 */
	public ModelAndView backtoAdmin(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HashMap hm_map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		Integer create_id = Integer.parseInt(currentUser.getLus_id());
		
		String s_spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");	
		String emailQa1 = null;
		Policy policy = bacManager.selectMstPolicyAll(s_spaj);
		Integer lspd_id = policy.getLspd_id(), ext = 0;	
		String keterangan = "", mi_id = null;
		Integer lspd_id_from = 0, lspd_id_from2 = 0;
		StringBuffer message = new StringBuffer();
		Date nowDate = bacManager.selectSysdate();
		List <Map> mapInbox = bacManager.selectMstInbox(s_spaj, "1");
		List <Map> selectUserQa1 = bacManager.selectUserQa(s_spaj, 207);
		HashMap selectUserQa =(HashMap) selectUserQa1.get(0);
		String userQa1 = (String) selectUserQa.get("LUS_FULL_NAME");
		BigDecimal createId = (BigDecimal) selectUserQa.get("LOCK_ID");
		emailQa1 = (String) selectUserQa.get("LUS_EMAIL");
		if(!mapInbox.isEmpty()){
			HashMap inbox =(HashMap) mapInbox.get(0);
			mi_id = (String) inbox.get("MI_ID");
			keterangan = (String) inbox.get("MI_DESC");
			if(keterangan!=null){
				keterangan =  keterangan.substring(keterangan.indexOf(":")+1, keterangan.length());
			}else{
				keterangan = "";
			}
		}
		
		if(request.getParameter("btnKirim") != null){
			String get_to = ServletRequestUtils.getStringParameter(request, "to", "");
			String get_cc = ServletRequestUtils.getStringParameter(request, "cc", "");
			String isi = ServletRequestUtils.getStringParameter(request, "isi", "");
			String subject = ServletRequestUtils.getStringParameter(request, "subject", "");
			Integer flag_kategori = ServletRequestUtils.getIntParameter(request, "flagKategori");
			
			String[] to = get_to.split(";");
			String[] cc = get_cc.split(";");
			
			bacManager.transferPosisi(currentUser, s_spaj, 1, "BACK TRANSFER KE ADMIN: "+isi.toUpperCase(), "tgl_back_to_admin", 0);
			bacManager.updateMstInboxLspdId(mi_id, 207, 218, 1, null, null, null, "FR :"+isi, 0);
			bacManager.updateMstInboxHistLpsdId(mi_id, "FR :"+isi, 218, 207, flag_kategori);
			Pemegang pemegang = new Pemegang();
			pemegang = elionsManager.selectpp(s_spaj);
			Tertanggung tertanggung = elionsManager.selectttg(s_spaj);
			Datausulan datausulan = (Datausulan)this.elionsManager.selectDataUsulanutama(s_spaj);
//			if(createId.intValue()==690) ext = 8750;
//			if(createId.intValue()==516) ext = 8381;
//			if(createId.intValue()==113) ext = 8949;
//			if(createId.intValue()==3041) ext = 5105;
//			if(createId.intValue()==3725) ext = 5229;
//			if(createId.intValue()==133) ext = 5105;
			
			//kirim email
			
			String Jenis_kategori = "";
			
			if (flag_kategori == 1) {
				Jenis_kategori = "Kekurangan Dokumen";
			}else if (flag_kategori == 2) {
				Jenis_kategori = "Kekurangan/kesalahan pengisian data";
			}else if (flag_kategori == 3) {
				Jenis_kategori = "Kekurangan Dokumen dan Kekurangan/kesalahan pengisian data";
			}
			
			String css = props.getProperty("email.uw.css.satu")
			             + props.getProperty("email.uw.css.dua");
		   
		    String footer = props.getProperty("email.uw.footer");
		    HashMap info = Common.serializableMap(bacManager.selectInformasiCabangFromSpaj(s_spaj));		    
			String cabang = (String) info.get("NAMA_CABANG");	
			HashMap gen = Common.serializableMap(elionsManager.selectEmailAgen(s_spaj));
			String namaAgen = (String) gen.get("MCL_FIRST");
			DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");			
			List lsProduk=elionsManager.selectMstProductInsured(s_spaj);
			Product produk=(Product)lsProduk.get(0);
			List<DropDown> ListImage = new ArrayList<DropDown>();
			ListImage.add(new DropDown("myLogo",props.getProperty("pdf.dir.syaratpolis")+"\\images\\ekalife.gif"));
			String pesan = css+"<table width=100% >"
						+ (cabang!=null	? 	"<tr><td>Cabang   	</td><td>:</td><td>" + cabang + "</td></tr>" : "")+ "</td><td> Tanggal:" + bacManager.selectSysdate() + "</td></tr>"						
						+ "<tr><td>Agen   		</td><td>:</td><td>" + namaAgen + "</td></tr>"
						+ "<tr><td>No. Spaj 	</td><td>:</td><td>" + FormatString.nomorPolis(s_spaj) + "<td></tr>"
						+ "<tr><td>Produk		</td><td>:</td><td>"+produk.getLsdbs_name()+"("+produk.getLsbs_id()+")"+"<td colspan=2></tr>" 
						+ "<tr><td>A/N			</td><td>:</td><td colspan=2>" + pemegang.getMcl_first() + " (Pemegang) -- " + tertanggung.getMcl_first() + " (Tertanggung) " + "</td></tr>" 						
						+ "<tr><td>UP</td><td>:</td><td>"+produk.getLku_symbol()+" "+ df.format(produk.getMspr_tsi()) +"</td></tr>"
						+ "<tr><td>Premi</td><td>:</td><td>"+produk.getLku_symbol()+" "+ df.format(produk.getMspr_premium())+"</td></tr>"
						+ "<tr><td>Cara Bayar</td><td>:</td><td>"+datausulan.getLscb_pay_mode()+"</td></tr>"		
						+ "<tr><td>Status   	</td><td>:</td><td>" + "INCOMPLETE DOCUMENT" + "<td></tr>"  
						+ "<tr><td>Kategori   	</td><td>:</td><td>" + Jenis_kategori + "<td></tr>" 
						+ "<tr><td>Keterangan	</td><td>:</td><td>" + isi + "<td></tr>" 
						+ "<tr><td>User Proses 	</td><td>:</td><td>" + userQa1 + " [" + currentUser.getDept() + " ]" +" Ext. "+ext+"<td></tr></br>" 									
					    +"</table>";
			
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, elionsManager.selectSysdate(), null, 
					true, "ajsjava@sinarmasmsiglife.co.id", 
					to, 
					cc,  
					null, 
					subject, 
					pesan, 
					null, s_spaj);
							
			hm_map.put("speedy", 0);
			hm_map.put("sukses", 1);
			hm_map.put("successMessage", "SPAJ "+s_spaj+" berhasil ditansfer ke Admin");
			hm_map.put("flag_sukses", 3);
			return new ModelAndView("common/info_proses", hm_map);   		
		}else{
			
				String emailProses = uwManager.selectEmailUser(currentUser.getLus_id());
				String to = bacManager.selectEmailAdminInputter(s_spaj)+";";
				String cc = uwManager.selectEmailCabangFromSpaj(s_spaj)+";"+"qabas@sinarmasmsiglife.co.id;";
				if(emailProses!=null)cc = cc+emailProses+";";
				if(emailQa1!=null)cc = cc+emailQa1+";";
				if(to==null)to=uwManager.selectEmailCabangFromSpaj(s_spaj)+";";
				if(to.equals("null;"))to=uwManager.selectEmailCabangFromSpaj(s_spaj)+";";
				
				 if ("09,58".indexOf(policy.getLca_id()) >= 0 ) {// Khusus Simpol
				    	String emailCabangBank = uwManager.selectEmailCabangBankSinarmas(s_spaj);
				    	String emailAoBank = uwManager.selectEmailAoBankSinarmas(s_spaj);
				    	if(emailCabangBank != null) to = to.concat(emailCabangBank+";");
				    	if(emailAoBank != null) to = to.concat( emailAoBank +";" );
				    }
						
				message.append(keterangan);
				hm_map.put("to", to);
				hm_map.put("cc", cc);
				hm_map.put("subject", "Transfer to Admin SPAJ "+s_spaj);
				hm_map.put("speedy", 0);
				hm_map.put("sukses", 0);
				hm_map.put("message", message.toString());
				return new ModelAndView("bas/backtoadmin", hm_map);
		}	
	}
	
	/**
	 * Report Daily Aksep
	 * @author Randy
	 * @since 8 Nov 2016
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView report_daily_aksep(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Report report;
		Date sysDate = elionsManager.selectSysdate();
		
        String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal","");
    	String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir","");
    	String user=ServletRequestUtils.getStringParameter(request, "user","");
    	
		report = new Report("Report Daily Aksep",props.getProperty("report.uw.report_daily_aksep"),Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true,new Date[] {sysDate, sysDate}, true);
		report.addParamSelect("User", "user", bacManager.selectReportUserDailyAksep(), "ALL", true);
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0, tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0, tanggalAkhir,  false);
		report.addParamDefault("user", "user", 0, user,  false);
		
		return prepareReport(request, response, report);
	}
	
	public ModelAndView report_cek_no_hp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
    	HashMap map = new HashMap();
    	Report report;
    	ArrayList<Map> jenis = new ArrayList<Map>();
		HashMap tmp = new HashMap();
		tmp.put("KEY", "1");
		tmp.put("VALUE", "NASABAH");
		jenis.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "2");
		tmp.put("VALUE", "AGEN");
		jenis.add(tmp);
		Date sysDate = elionsManager.selectSysdate();
    	HashMap mapReport = new HashMap();    	
    	mapReport=bacManager.selectMstConfig(6, "Report Cek No Hp", "report.bas.cek_no_hp");
		report = new Report("Report Cek No Hp",mapReport.get("NAME").toString(), Report.PDF, null);
		report.addParamSelect("Jenis", "jenis", jenis, "ALL", true);
		report.addParamDefault("username", "username", 0, currentUser.getName(), false);		
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		report.setReportQueryMethod("selectReportCekNoHp");    	
		return prepareReport(request, response, report);
	}
	
	public ModelAndView reportDataEspajBas(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User)request.getSession().getAttribute("currentUser");
		
    	List<Map> jenis = new ArrayList<Map>();
		Map tmp = new HashMap();
		tmp.put("KEY", "1");
		tmp.put("VALUE", "BELUM DIPROSES OLEH BAS");
		jenis.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "2");
		tmp.put("VALUE", "SUDAH DIPROSES OLEH BAS");
		jenis.add(tmp);
		
		List<Map> bank = new ArrayList<Map>();
		Map jbank = new HashMap();
		jbank.put("KEY", "43");
		jbank.put("VALUE", "BTN");
		bank.add(jbank);
		
		jbank = new HashMap();
		jbank.put("KEY", "44");
		jbank.put("VALUE", "BJB");
		bank.add(jbank);
		
		jbank = new HashMap();
		jbank.put("KEY", "51");
		jbank.put("VALUE", "JATIM");
		bank.add(jbank);
		
    	Map reportpath = new HashMap();
    	reportpath = bacManager.selectMstConfig(6, "Report Data ESPAJ", "report.bas.reportDataEspajBas");
		Report report = new Report("Report Data ESPAJ", reportpath.get("NAME").toString(), Report.XLS, null);
		report.addParamSelect("Jenis", "jenis", jenis, "ALL", true);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		report.addParamSelect("Bank", "bank", bank, "ALL", true);
		report.setReportQueryMethod("selectDataEspajBas");
		return prepareReport(request, response, report);
	}
}