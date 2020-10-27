package com.ekalife.elions.web.report;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.jasper.Report;
import com.ekalife.utils.parent.ParentJasperReportingController;

public class BancassController extends ParentJasperReportingController{
	
	public ModelAndView bancass_produksi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report ;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysDate = elionsManager.selectSysdate();

		List<DropDown> reportPathList = new ArrayList<DropDown>();
		//reportPathList.add(new DropDown(props.getProperty("report.bancass.stabil_produksi_all"), "Report Produksi Stabil Link"));
		reportPathList.add(new DropDown(props.getProperty("report.bancass.nb_bancass_by_cabang"), "Report Produksi Stabil Link Berdasarkan Tgl Produksi"));
		reportPathList.add(new DropDown(props.getProperty("report.bancass.nb_bancass_by_cabang_summary"), "Report Produksi Stabil Link Berdasarkan Tgl Produksi (SUMMARY)"));
		reportPathList.add(new DropDown(props.getProperty("report.bancass.nb_bancass_by_tgl_bdate"), "Report Produksi Stabil Link Berdasarkan Tgl Mulai Input"));
		
		report = new Report("Stabil Link Report", reportPathList, Report.PDF, null);
		
		report.addParamDefault("username", "username", 200, currentUser.getName(), false);
		report.addParamDefault("cab_bank", "cab_bank", 200, currentUser.getCab_bank(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
		
		return prepareReport(request, response, report);
	}

	/**
	 * Report kanwil bsm
	 * yusuf
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView kanwil_bsm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map m = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List<DropDown> daftarReport = new ArrayList<DropDown>();
		
		daftarReport.add(new DropDown("New Business Simas Prima", "report.bac.nb_bank_sinarmas", "New Business Per Tgl Cetak Sertifikat"));
		daftarReport.add(new DropDown("New Business Simas Prima", "report.bac.nb_bank_sinarmas_input", "New Business Per Tgl Input Spaj"));
		daftarReport.add(new DropDown("New Business Simas Prima", "report.bac.nb_bank_sinarmas_produksi", "New Business Per Tgl Produksi"));
		daftarReport.add(new DropDown("New Business Simas Prima", "report.bac.view_jatuh_tempo", "Laporan Jatuh Tempo"));
		daftarReport.add(new DropDown("New Business Simas Prima", "report.bac.view_rollover", "Laporan Rollover Per Tgl Produksi"));

		daftarReport.add(new DropDown("New Business Stabil Link", "report.bac.nb_bank_sinarmas_stabil_tgl_cetak", "New Business Per Tgl Cetak Sertifikat"));
		daftarReport.add(new DropDown("New Business Stabil Link", "report.bac.nb_bank_sinarmas_stabil_input", "New Business Per Tgl Input Spaj"));
		daftarReport.add(new DropDown("New Business Stabil Link", "report.bac.nb_bank_sinarmas_stabil_produksi", "New Business Per Tgl Produksi"));
		daftarReport.add(new DropDown("New Business Stabil Link", "report.bac.view_stabil_jatuh_tempo", "Laporan Jatuh Tempo"));		

		daftarReport.add(new DropDown("Produksi Simas Prima", "report.bac.total_produksi", "Total Produksi (Berdasarkan Tanggal Produksi)"));
		daftarReport.add(new DropDown("Produksi Simas Prima", "report.bac.total_outstanding", "Total Outstanding (Berdasarkan Tanggal Produksi)"));
		daftarReport.add(new DropDown("Produksi Simas Prima", "report.bac.total_break", "Total Break / Cair (Berdasarkan Tanggal Produksi)"));
		daftarReport.add(new DropDown("Produksi Simas Prima", "report.bac.total_break_tgl_cair", "Total Break / Cair (Berdasarkan Tanggal Pencairan)"));
		daftarReport.add(new DropDown("Produksi Simas Prima", "report.bac.total_break_tgl_bayar", "Total Break / Bayar (Berdasarkan Tanggal Bayar)"));

		daftarReport.add(new DropDown("Outstanding", "report.bac.outstanding_summary_per_branch", "Summary Outstanding"));
		
		m.put("daftarCabang", uwManager.selectCabangBsmByWilayah(currentUser.getCab_bank()));
		m.put("daftarReport", daftarReport);
		m.put("sysDate", defaultDateFormat.format(elionsManager.selectSysdate()));
		
		return new ModelAndView("report/kanwil_bsm", "cmd", m);
	}
		
	public ModelAndView kanwil_bsm_report(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report = new Report("BSM", props.getProperty(ServletRequestUtils.getRequiredStringParameter(request, "path")), Report.PDF, null);
		Date sysDate = elionsManager.selectSysdate();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		report.addParamDefault("username", "username", 200, currentUser.getName(), false);
		report.addParamDefault("cab_bank", "cab_bank", 200, "", false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
		return prepareReport(request, response, report);
	}
		
}