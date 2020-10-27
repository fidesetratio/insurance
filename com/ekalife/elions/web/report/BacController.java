package com.ekalife.elions.web.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.DateFormat;
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

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Company_ws;
import com.ekalife.elions.model.KuesionerPelayananDetails;
import com.ekalife.elions.model.PowersaveCair;
import com.ekalife.elions.model.Simcard;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.cross_selling.Fat;
import com.ekalife.elions.model.cross_selling.FatHistory;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.jasper.Report;
import com.ekalife.utils.parent.ParentJasperReportingController;
import com.ekalife.utils.view.XLSCreatorFrProduksiCair;
import com.ekalife.utils.view.XLSCreatorFrProduksiCairMGI;
import com.ibatis.common.resources.Resources;
import com.lowagie.text.pdf.PdfWriter;

import id.co.sinarmaslife.std.model.vo.DropDown;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * @author Yusuf
 * @since June 20, 2006
 */
public class BacController extends ParentJasperReportingController{
	protected final Log logger = LogFactory.getLog( getClass() );
	/**
	 * Report Baru Bank Sinarmas
	 * Semua report BSM yang baru, akan dimasukkan disini, agar tidak membingungkan pihak BSM
	 * Nanti bila development untuk report2 disini sudah selesai, report2 BSM yg dicontroller ini tidak dipakai lagi
	 * 
	 * Yusuf (26/01/10)
	 */
	public ModelAndView bsm(HttpServletRequest request, HttpServletResponse response)throws Exception {
		Map cmd = new HashMap();
		cmd.put("sysDate", defaultDateFormat.format(elionsManager.selectSysdate(0)));
		
		//Daftar Utama Report2
		List<DropDown> daftarJenis = new ArrayList<DropDown>();
		daftarJenis.add(new DropDown("1", "Report Produksi", 	"Report Produksi Berdasarkan Tanggal Input"));
		daftarJenis.add(new DropDown("2", "Report Produksi", 	"Report Produksi Berdasarkan Akseptasi UW"));
		daftarJenis.add(new DropDown("3", "Report Pencairan", 	"Report Pencairan Berdasarkan Tanggal Input"));
		daftarJenis.add(new DropDown("4", "Report Pencairan", 	"Report Pencairan Berdasarkan Tanggal Cair Aktual"));
		daftarJenis.add(new DropDown("5", "Report Outstanding", "Report Outstanding Berdasarkan Input Cabang"));
		daftarJenis.add(new DropDown("6", "Report Outstanding",	"Report Outstanding Berdasarkan Akseptasi UW"));
		daftarJenis.add(new DropDown("7", "Report Outstanding",	"Report Total Outstanding Berdasarkan Tanggal Produksi"));
		cmd.put("daftarJenis", daftarJenis);
		
		//Daftar Produk
		List<DropDown> daftarProduk = new ArrayList<DropDown>();
		daftarProduk.add(new DropDown("142002", "Simas Prima"));
		daftarProduk.add(new DropDown("164002", "Simas Stabil Link"));
		daftarProduk.add(new DropDown("175002", "Simas Prima Syariah"));
		cmd.put("daftarProduk", daftarProduk);
		
		//Daftar Kurs
		List<DropDown> daftarKurs = new ArrayList<DropDown>();
		daftarKurs.add(new DropDown("01", "[IDR] Rupiah"));
		daftarKurs.add(new DropDown("02", "[USD] Dollar"));
		cmd.put("daftarKurs", daftarKurs);
		
		//Daftar Format Tampilan Report
		List<DropDown> daftarFormat = new ArrayList<DropDown>();
		daftarFormat.add(new DropDown("PDF", "PDF"));
		daftarFormat.add(new DropDown("XLS", "Excel"));
		daftarFormat.add(new DropDown("HTML", "HTML"));
		cmd.put("daftarFormat", daftarFormat);
		
		return new ModelAndView("bac/bsm", cmd);
	}
	
	
	
	public ModelAndView daftar_ro_dan_jatuhtempo_report(HttpServletRequest request, HttpServletResponse response)throws Exception {
		Report report;
		String startDate = ServletRequestUtils.getStringParameter(request, "startDate", "");
		String endDate = ServletRequestUtils.getStringParameter(request, "endDate", "");
		String cnc = ServletRequestUtils.getStringParameter(request, "cnc");
		String tanggal = ServletRequestUtils.getStringParameter(request, "tanggal", "");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		if(cnc.equals("0") || cnc.equals("Stable Link")){
			if(tanggal.equals("mpc_bdate") || tanggal.equals("Tanggal Rollover")){
				report = new Report("Daftar Jatuh Tempo Stable Link", props.getProperty("report.bac.roll_over_slink"), Report.PDF, null);
			}else{
				report = new Report("Daftar RollOver Stable Link", props.getProperty("report.bac.jatuh_tempo_slink"), Report.PDF, null);
			}
		}else{
			if(tanggal.equals("mpc_bdate") || tanggal.equals("Tanggal Rollover")){
				report = new Report("Daftar Jatuh Tempo Stable Save", props.getProperty("report.bac.roll_over_ssave"), Report.PDF, null);
			}else{
				report = new Report("Daftar RollOver Stable Save", props.getProperty("report.bac.jatuh_tempo_ssave"), Report.PDF, null);
			}
		}
		
		report.addParamDefault("lus_id", "lus_id", 100, currentUser.getLus_id(), false);
		report.addParamDefault("startDate", "startDate", 200, startDate, false);
		report.addParamDefault("endDate", "endDate", 200, endDate, false);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView daftar_pembayaran_report(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		
		report = new Report("Daftar Pembayaran", props.getProperty("report.bac.daftar_bayar"), Report.PDF, null);
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		Map m = new HashMap();
		String produk = ServletRequestUtils.getStringParameter(request, "produk", "");
		String cabang = ServletRequestUtils.getStringParameter(request, "cabang", "");
		String tanggal = ServletRequestUtils.getStringParameter(request, "tanggal", "");
		String startDate = ServletRequestUtils.getStringParameter(request, "startDate", "");
		String endDate = ServletRequestUtils.getStringParameter(request, "endDate", "");
		String cnc = ServletRequestUtils.getStringParameter(request, "cnc");

		//
		m.put("produk", produk);
		m.put("cabang", cabang);
		m.put("tanggal", tanggal);
		m.put("startDate", startDate);
		m.put("endDate", endDate);
		m.put("jenis", currentUser.getJn_bank());
		m.put("cnc", cnc);

//        		daftarTanggal.add(new DropDown("mpc_bdate", "Tanggal Rollover"));
//		daftarTanggal.add(new DropDown("mpc_edate", "Tanggal Jatuh Tempo"));
//		daftarTanggal.add(new DropDown("mpc_cair", "Tanggal Cair Aktual"));
//		daftarTanggal.add(new DropDown("tgl_input", "Tanggal Input"));
        String ketBdskTgl;
        if( tanggal.equals( "mpc_bdate" ) )
        {
            ketBdskTgl = "Berdasarkan Tanggal Rollover";
        }
        else if( tanggal.equals( "mpc_edate" ) )
        {
            ketBdskTgl = "Berdasarkan Tanggal Jatuh Tempo";
        }
        else if( tanggal.equals( "mpc_cair" ) )
        {
            ketBdskTgl = "Berdasarkan Tanggal Cair Aktual";
        }
        else if( tanggal.equals( "tgl_input" ) )
        {
            ketBdskTgl = "Berdasarkan Tanggal Input";
        }
        else
        {
            ketBdskTgl = "Berdasarkan <belum dikenal, hub IT>";
        }
        
        if(tanggal.equals( "mpc_edate" )){//apabila jatuh tempo, enddate+1
        	m.put("tanggal", tanggal+"+1");
        }

        report.addParamDefault("username", "username", 200, currentUser.getName(), false);
		report.addParamDefault("startDate", "startDate", 200, startDate, false);
		report.addParamDefault("endDate", "endDate", 200, endDate, false);
        report.addParamDefault( "ketBdskTgl", "ketBdskTgl", 200, ketBdskTgl, false );

        List<PowersaveCair> daftar = elionsManager.selectReportCair(m);
		List<PowersaveCair> daftarTampil = new ArrayList<PowersaveCair>();
		
		//Yusuf 4/3/10 - Request Edy Kohar via email, kalo BP rate nya 100%, tampilkan saja, krn perhitungannya pasti sudah final
		for(PowersaveCair p : daftar){
			if(p.lsbs_id.intValue() == 164){
				int bprate = uwManager.selectBPRatePerTransaksiSlink(p.reg_spaj, p.mpc_tu_ke);
				//bila TGL NAB sudah ada, tampilkan datanya
				if(p.mpc_tgl_nab_bp != null){
					daftarTampil.add(p);
				//bila nilai BP rate = 100%, berarti tidak ada BP untuk nasabah, tampilkan juga datanya
				}else if(bprate == 100){
					daftarTampil.add(p);
				}
			}else{
				daftarTampil.add(p);
			}
		}
		
		report.setResultList(daftarTampil);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView kuesioner_pelayanan_report(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		
		report = new Report("Daftar Pembayaran", props.getProperty("report.kuesioner_pelayanan"), Report.PDF, null);
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		Map m = new HashMap();
		String startDate = ServletRequestUtils.getStringParameter(request, "startDate", "");
		String endDate = ServletRequestUtils.getStringParameter(request, "endDate", "");
		SimpleDateFormat formatDate =new SimpleDateFormat("dd/MM/yyyy");
		
		Date startDate2 = formatDate.parse(startDate);
		Date endDate2 = formatDate.parse(endDate);
		startDate2.setHours(00);
		startDate2.setMinutes(00);
		startDate2.setSeconds(00);
		endDate2.setHours(23);
		endDate2.setMinutes(59);
		endDate2.setSeconds(59);
		//
		m.put("startDate", startDate);
		m.put("endDate", endDate);
		m.put("jenis", currentUser.getJn_bank());


        report.addParamDefault("username", "username", 200, currentUser.getName(), false);
		report.addParamDefault("startDate", "startDate", 200, startDate, false);
		report.addParamDefault("endDate", "endDate", 200, endDate, false);

		List<KuesionerPelayananDetails> daftarTampil = new ArrayList<KuesionerPelayananDetails>();
		daftarTampil = elionsManager.selectMstKuesionerPelayananByInsertDate(startDate2, endDate2);
		
		report.setResultList(daftarTampil);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView daftar_ro_dan_jatuhtempo_slink(HttpServletRequest request, HttpServletResponse response)throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysdate = elionsManager.selectSysdateSimple();
		Map m = new HashMap();
		
		List<DropDown> daftarReport = new ArrayList<DropDown>();
		daftarReport.add(new DropDown("0", "Stable Link"));
		daftarReport.add(new DropDown("1", "Stable Save"));
		m.put("daftarReport", daftarReport);
		
		List<DropDown> daftarTanggal = new ArrayList<DropDown>();
		daftarTanggal.add(new DropDown("mpc_bdate", "Tanggal Rollover"));
		daftarTanggal.add(new DropDown("mpc_edate", "Tanggal Jatuh Tempo"));
		m.put("daftarTanggal", daftarTanggal);
		
		String cabang = ServletRequestUtils.getStringParameter(request, "cabang", currentUser.getCab_bank());
		String tanggal = ServletRequestUtils.getStringParameter(request, "produk", "mpc_bdate");
		String startDate = ServletRequestUtils.getStringParameter(request, "startDate", defaultDateFormat.format(sysdate));
		String endDate = ServletRequestUtils.getStringParameter(request, "endDate", defaultDateFormat.format(sysdate));
		m.put("cabang", cabang);
		m.put("startDate", startDate);
		m.put("endDate", endDate);
		
		return new ModelAndView("bac/reportjatuhtemporollover", m);
	}
	
	public ModelAndView daftar_pembayaran(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		int jenis = currentUser.getJn_bank().intValue();
		Date sysdate = elionsManager.selectSysdateSimple();
		Map m = new HashMap();
		
		//
		List<DropDown> daftarReport = new ArrayList<DropDown>();
		daftarReport.add(new DropDown("1", "Cash"));
		daftarReport.add(new DropDown("0", "Non-Cash"));
		m.put("daftarReport", daftarReport);
		
		//
		List<DropDown> daftarProduk = new ArrayList<DropDown>();
		if(jenis == 2) {
			daftarProduk.add(new DropDown("ALL", "ALL"));
			daftarProduk.add(new DropDown("142-002", "SIMAS PRIMA"));
			daftarProduk.add(new DropDown("158-006", "SIMAS PRIMA MANFAAT BULANAN"));
			daftarProduk.add(new DropDown("164-002", "SIMAS STABIL LINK"));
			daftarProduk.add(new DropDown("175-002", "SIMAS PRIMA SYARIAH"));
			daftarProduk.add(new DropDown("182-007", "MAXI SAVE SILVER SYARIAH"));
			daftarProduk.add(new DropDown("182-008", "MAXI SAVE GOLD SYARIAH"));
			daftarProduk.add(new DropDown("182-009", "MAXI SAVE PLATINUM SYARIAH"));
// *tambahan POWERLINK
			daftarProduk.add(new DropDown("120-010", "SIMAS POWER LINK 5"));
			daftarProduk.add(new DropDown("120-011", "SIMAS POWER LINK 10"));
			daftarProduk.add(new DropDown("120-012", "SIMAS POWER LINK SINGLE"));




		}else if(jenis == 3) {
			daftarProduk.add(new DropDown("ALL", "ALL"));
			daftarProduk.add(new DropDown("142-009", "DANAMAS PRIMA"));
			daftarProduk.add(new DropDown("158-014", "DANAMAS PRIMA MANFAAT BULANAN"));
			daftarProduk.add(new DropDown("164-008", "STABLE LINK SMS"));
		}
		m.put("daftarProduk", daftarProduk);
		
		//
		List<DropDown> daftarCabang = new ArrayList<DropDown>();
		
		if(currentUser.getCab_bank().trim().equals("SSS")) {
			daftarCabang.add(0, new DropDown("ALL", "ALL"));
			daftarCabang.addAll(
				elionsManager.selectDropDown(
					"EKA.LST_CABANG_BII", 
					"LCB_NO", 
					"NAMA_CABANG", 
					"", "NAMA_CABANG", 
					"JENIS = " + jenis + "AND FLAG_AKTIF=1")); 
		}else {
			daftarCabang.addAll(
				elionsManager.selectDropDown(
					"EKA.LST_CABANG_BII", 
					"LCB_NO", 
					"NAMA_CABANG", 
					"", "NAMA_CABANG", 
					"JENIS = " + jenis + " AND LCB_NO = '" + currentUser.getCab_bank() + "'"+ "AND FLAG_AKTIF=1") ); //kalo admin, cuman boleh akses cabangnya 
		}
		m.put("daftarCabang", daftarCabang);
		
		//
		List<DropDown> daftarTanggal = new ArrayList<DropDown>();
		daftarTanggal.add(new DropDown("mpc_bdate", "Tanggal Rollover"));
		daftarTanggal.add(new DropDown("mpc_edate", "Tanggal Jatuh Tempo"));
		daftarTanggal.add(new DropDown("mpc_cair", "Tanggal Cair Aktual"));
		daftarTanggal.add(new DropDown("tgl_input", "Tanggal Input"));
		m.put("daftarTanggal", daftarTanggal);
		
		//
		String produk = ServletRequestUtils.getStringParameter(request, "produk", "ALL");
		String cabang = ServletRequestUtils.getStringParameter(request, "cabang", 
			(currentUser.getCab_bank().trim().equals("SSS") ? "ALL" : currentUser.getCab_bank())); //kalo admin, cuman boleh akses cabangnya
		String tanggal = ServletRequestUtils.getStringParameter(request, "produk", "mpc_bdate");
		String startDate = ServletRequestUtils.getStringParameter(request, "startDate", defaultDateFormat.format(sysdate));
		String endDate = ServletRequestUtils.getStringParameter(request, "endDate", defaultDateFormat.format(sysdate));
		
		//
		m.put("produk", produk);
		m.put("cabang", cabang);
		m.put("tanggal", tanggal);
		m.put("startDate", startDate);
		m.put("endDate", endDate);
		
		return new ModelAndView("bac/reportcair", m);
	}	
	
	public ModelAndView maturity_report(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("Report Maturity", props.getProperty("report.bac.maturity"), Report.PDF, null);
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		Map m = new HashMap();
		String username = ServletRequestUtils.getStringParameter(request, "username", "");
		String pilih = ServletRequestUtils.getStringParameter(request, "pilih", "");
		String awal = ServletRequestUtils.getStringParameter(request, "awal", "");
		String akhir = ServletRequestUtils.getStringParameter(request, "akhir", "");
		String startDate = ServletRequestUtils.getStringParameter(request, "startDate", "");
		String endDate = ServletRequestUtils.getStringParameter(request, "endDate", "");
		//
		//m.put("username", username);
		m.put("pilih", pilih);
		m.put("awal", awal);
		m.put("akhir", akhir);
		m.put("startDate", startDate);
		m.put("endDate", endDate);
		//m.put("jenis", currentUser.getJn_bank());
		
		report.addParamDefault("username", "username", 200, currentUser.getName(), false);
		report.addParamDefault("awal", "awal", 200, awal, false);
		report.addParamDefault("akhir", "akhir", 200, akhir, false);
		report.addParamDefault("startDate", "startDate", 200, startDate, false);
		report.addParamDefault("endDate", "endDate", 200, endDate, false);
		
		report.setResultList(uwManager.selectReportMaturity(m));
		return prepareReport(request, response, report);
	}
	
	public ModelAndView Laporan_nasabah_slink(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		Report report;
		report = new Report("Laporan Nasabah Slink", props.getProperty("report.bca.laporan_nasabah_slink"), Report.PDF, null);
		String NoPolis = ServletRequestUtils.getStringParameter(request, "NoPolis", "");
		if(!NoPolis.equals("")){
			NoPolis = elionsManager.selectGetSpaj(NoPolis);
			
		}
		report.addParamText("No Polis/No SPAJ", "NoPolis", 30, NoPolis, true);
		//report.setResultList(uwManager.selectDataNasabahSlink(NoPolis));
		
		return prepareReport(request, response, report);
	}
	
	public ModelAndView maturity(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		int jenis = currentUser.getJn_bank().intValue();
		Date sysdate = elionsManager.selectSysdateSimple();
		Map m = new HashMap();
		
		m.put("username",currentUser.getLus_full_name());
		
		//
		List<DropDown> daftarPilihan = new ArrayList<DropDown>();
		daftarPilihan.add(new DropDown("mpr_rate", "Interest(%) "));
		daftarPilihan.add(new DropDown("mpr_deposit", "Nominal"));
		m.put("daftarPilihan", daftarPilihan);
		
		//
		String pilih = ServletRequestUtils.getStringParameter(request, "pilih", "mpr_rate");
		String awal = ServletRequestUtils.getStringParameter(request, "awal", "");
		String akhir = ServletRequestUtils.getStringParameter(request, "akhir", "");
		String startDate = ServletRequestUtils.getStringParameter(request, "startDate", defaultDateFormat.format(sysdate));
		String endDate = ServletRequestUtils.getStringParameter(request, "endDate", defaultDateFormat.format(sysdate));
		//
		m.put("pilih", pilih);
		m.put("awal", awal);
		m.put("akhir", akhir);
		m.put("startDate", startDate);
		m.put("endDate", endDate);
		
		return new ModelAndView("bac/reportmaturity", m);
	}	
	
	public ModelAndView simas_card_distribution(HttpServletRequest request, HttpServletResponse response ) throws Exception {
		Report report;
		report = new Report("Pemakaian Simas Card", props.getProperty("report.simcard.distribution"), Report.PDF, null);
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysDate = elionsManager.selectSysdate();
//		report.addParamDate("tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		report.addParamDate("Tanggal", "tgl", true, new Date[] {new Date(), sysDate}, true);
//		report.addParamSelect("Jenis", "jenis", elionsManager.selectJenisSimasCard(), null, true);
		report.addParamSelect("cabang", "cabang", elionsManager.selectCabangSimasCard(), "ALL", true);
//		report.addParamSelect("cabang", "cabang", elionsManager.selectlstCabang2(), null, true);
		report.addParamDefault("User ID", "lus_id", 20, currentUser.getLus_full_name(), false);
		report.setReportQueryMethod("selectReportSimasCardDistribution");
		return prepareReport(request, response, report);
	}
	
	public ModelAndView simas_card(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");

		int cetak = ServletRequestUtils.getRequiredIntParameter(request, "cetak");
		int jenis = ServletRequestUtils.getRequiredIntParameter(request, "jenis");
		String cabang = ServletRequestUtils.getRequiredStringParameter(request, "cabang");
		String cabangBII = ServletRequestUtils.getStringParameter(request, "cabangBII", "");
		String cabangBankSinarmas = ServletRequestUtils.getStringParameter(request, "cabangBankSinarmas", "");
		int jumlah_print = ServletRequestUtils.getRequiredIntParameter(request, "jumlah_print"); 
		String format = ServletRequestUtils.getRequiredStringParameter(request, "format");
		
		if(cetak == 0) {
			report = new Report("Kartu Simas Card", props.getProperty("report.simcard.kartu"), format, null);
		}else if(cetak == 2){
			report = new Report("Kartu Simas Card Baru", props.getProperty("report.simcard.kartu.new"), format, null);
		}else {
			report = new Report("Surat Simas Card", props.getProperty("report.simcard.surat"), format, null);
		}
		
		report.setResultList(elionsManager.selectCetakSimasCard(jenis, cabang, cetak, cabangBII, cabangBankSinarmas, jumlah_print));
		String print = ServletRequestUtils.getStringParameter(request, "print", "false");
		
		if(request.getParameter("print").equals("print")) {
			for(int i =0;i<report.getResultList().size();i++){
				Date sysdate = elionsManager.selectSysdateSimple();
				Simcard simCard = (Simcard)report.getResultList().get(i);
				String no_kartu = simCard.getNo_kartu();
				if(cetak == 0 || cetak ==2){
					elionsManager.updateKartuSimasCard(1, sysdate, jenis, no_kartu);
					Simcard simcard = elionsManager.selectSimcard(Integer.toString(jenis), no_kartu);
					String reg_spaj = simcard.getReg_spaj();
					this.elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "SIMAS CARD SUDAH DICETAK", reg_spaj, 0);
				}else {
					elionsManager.updateSuratSimasCard(2, jenis, no_kartu);
				}
			}
		}
		
		
		return prepareReport(request, response, report);		
	}
	
	public ModelAndView simas_card_vip(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");

		int cetak = ServletRequestUtils.getRequiredIntParameter(request, "cetak");
		int jenis = ServletRequestUtils.getRequiredIntParameter(request, "jenis");
		String cabang = ServletRequestUtils.getRequiredStringParameter(request, "cabang");
		String cabangBII = ServletRequestUtils.getStringParameter(request, "cabangBII", "");
		String cabangBankSinarmas = ServletRequestUtils.getStringParameter(request, "cabangBankSinarmas", "");
		int jumlah_print = ServletRequestUtils.getRequiredIntParameter(request, "jumlah_print"); 
		String format = ServletRequestUtils.getRequiredStringParameter(request, "format");
		
		if(cetak == 0) {
			report = new Report("Kartu Vip Card", props.getProperty("report.vipcard.kartu"), format, null);
		}else if(cetak == 2){
			report = new Report("Kartu Vip Card Baru", props.getProperty("report.vipcard.kartu.new"), format, null);
		}else {
			report = new Report("Surat Vip Card", props.getProperty("report.vipcard.surat"), format, null);
		}
		
		report.setResultList(uwManager.selectCetakVipCard(jenis, cabang, cetak, jumlah_print));
		String print = ServletRequestUtils.getStringParameter(request, "print", "false");
		
		if(request.getParameter("print").equals("print")) {
			for(int i =0;i<report.getResultList().size();i++){
				Date sysdate = elionsManager.selectSysdateSimple();
				Simcard simCard = (Simcard)report.getResultList().get(i);
				String no_kartu = simCard.getNo_kartu();
				if(cetak == 0 || cetak ==2){
					elionsManager.updateKartuSimasCard(1, sysdate, jenis, no_kartu);
					Simcard simcard = elionsManager.selectSimcard(Integer.toString(jenis), no_kartu);
					String reg_spaj = simcard.getReg_spaj();
					this.elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "SHARIA VIP CARD SUDAH DICETAK", reg_spaj, 0);
				}else {
					elionsManager.updateSuratSimasCard(2, jenis, no_kartu);
				}
			}
		}
		
		
		return prepareReport(request, response, report);		
	}
	
	public ModelAndView nb_stabil_all(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysDate = elionsManager.selectSysdate();
		
		List<DropDown> reportPathList = new ArrayList<DropDown>();
		
		reportPathList.add(new DropDown(props.getProperty("report.bac.nb_stabil_by_aksep_all"), "New Business Per Tanggal Akseptasi"));
		
		reportPathList.add(new DropDown(props.getProperty("report.bac.nb_stabil_by_referral_all"), "New Business Per Referral"));
		reportPathList.add(new DropDown(props.getProperty("report.bac.nb_stabil_by_cetak_all"), "New Business Per Tgl Cetak Sertifikat"));
		reportPathList.add(new DropDown(props.getProperty("report.bac.nb_stabil_by_input_all"), "New Business Per Tgl Input Spaj"));
		reportPathList.add(new DropDown(props.getProperty("report.bac.nb_stabil_by_produksi_all"), "New Business Per Tgl Produksi"));
		reportPathList.add(new DropDown(props.getProperty("report.bac.view_stabil_by_jatuh_tempo_all"), "Laporan Jatuh Tempo"));
		//reportPathList.add(new DropDown(props.getProperty("report.bac.view_stabil_by_rollover_all"), "Laporan Rollover Per Tgl Produksi"));
		
		report = new Report("Stabil Link Reports", reportPathList, Report.PDF, null);
		
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
		return prepareReport(request, response, report);
		
	}
	
	public ModelAndView nb_powersave_all(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysDate = elionsManager.selectSysdate();
		
		List<DropDown> reportPathList = new ArrayList<DropDown>();
		
		reportPathList.add(new DropDown(props.getProperty("report.bac.nb_powersave_by_referral"), "New Business Per Referral"));
		reportPathList.add(new DropDown(props.getProperty("report.bac.nb_powersave_by_aksep"), "New Business Per Tgl Cetak Sertifikat"));
		reportPathList.add(new DropDown(props.getProperty("report.bac.nb_powersave_by_input"), "New Business Per Tgl Input Spaj"));
		reportPathList.add(new DropDown(props.getProperty("report.bac.nb_powersave_by_produksi"), "New Business Per Tgl Produksi"));
		reportPathList.add(new DropDown(props.getProperty("report.bac.view_jatuh_tempo_powersave"), "Laporan Jatuh Tempo"));
		reportPathList.add(new DropDown(props.getProperty("report.bac.view_rollover_powersave"), "Laporan Rollover Per Tgl Produksi"));
		
		report = new Report("PowerSave Reports", reportPathList, Report.PDF, null);
		
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
		return prepareReport(request, response, report);
		
	}
	
	public ModelAndView nb_stabil_topup(HttpServletRequest request, HttpServletResponse response)throws Exception{
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysDate = elionsManager.selectSysdate();

		List<DropDown> reportPathList = new ArrayList<DropDown>();
		if("SSS".equals(currentUser.getCab_bank().trim())) {
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bsm_stabil_topup"), "Top Up Stable Link Per Tanggal Akseptasi"));
			
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bsm_stabil_referral_all_topup"), "Top Up Stable Link Per Referral"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bsm_stabil_all_topup"), "Top Up Stable Link Per Tgl Cetak Sertifikat"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bsm_stabil_input_all_topup"), "Top Up Stable Link Per Tgl Input Spaj"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bsm_stabil_produksi_all_topup"), "Top Up Stable Link Per Tgl Produksi"));
			//reportPathList.add(new DropDown(props.getProperty("report.bac.view_stabil_jatuh_tempo_all"), "Laporan Jatuh Tempo"));
			//reportPathList.add(new DropDown(props.getProperty("report.bac.view_stabil_rollover_all"), "Laporan Rollover Per Tgl Produksi"));
		
		//Report untuk CS/Spv/Pincab Bank Sinarmas
		}else {
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bsm_stabil_tgl_cetak_topup"), "Top Up Stable Link Per Tgl Cetak Sertifikat"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bsm_stabil_input_topup"), "Top Up Stable Link Per Tgl Input Spaj"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bsm_stabil_produksi_topup"), "Top Up Stable Link Per Tgl Produksi"));
			//reportPathList.add(new DropDown(props.getProperty("report.bac.view_stabil_jatuh_tempo"), "Laporan Jatuh Tempo"));
//			reportPathList.add(new DropDown(props.getProperty("report.bac.view_stabil_rollover"), "Laporan Rollover Per Tgl Produksi"));
		}
		
		report = new Report("Simas Stabil Link Top Up Reports", reportPathList, Report.PDF, null);
		
		report.addParamDefault("username", "username", 200, currentUser.getName(), false);
		report.addParamDefault("cab_bank", "cab_bank", 200, currentUser.getCab_bank(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
		return prepareReport(request, response, report);
		
	}
	
	public ModelAndView nb_stabil(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysDate = elionsManager.selectSysdate();

		List<DropDown> reportPathList = new ArrayList<DropDown>();
		if("SSS".equals(currentUser.getCab_bank().trim())) {
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_stabil"), "New Business Per Tanggal Akseptasi"));
			
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_stabil_referral_all"), "New Business Per Referral"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_stabil_all"), "New Business Per Tgl Cetak Sertifikat"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_stabil_input_all"), "New Business Per Tgl Input Spaj"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_stabil_produksi_all"), "New Business Per Tgl Produksi"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.view_stabil_jatuh_tempo_all"), "Laporan Jatuh Tempo"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.view_stabil_rollover_all"), "Laporan Rollover Per Tgl Produksi"));
		
		//Report untuk CS/Spv/Pincab Bank Sinarmas
		}else {
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_stabil_tgl_cetak"), "New Business Per Tgl Cetak Sertifikat"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_stabil_input"), "New Business Per Tgl Input Spaj"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_stabil_produksi"), "New Business Per Tgl Produksi"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.view_stabil_jatuh_tempo"), "Laporan Jatuh Tempo"));
//			reportPathList.add(new DropDown(props.getProperty("report.bac.view_stabil_rollover"), "Laporan Rollover Per Tgl Produksi"));
		}
		
		report = new Report("Simas Stabil Link Reports", reportPathList, Report.PDF, null);
		
		report.addParamDefault("username", "username", 200, currentUser.getName(), false);
		report.addParamDefault("cab_bank", "cab_bank", 200, currentUser.getCab_bank(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView nb_progressive(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysDate = elionsManager.selectSysdate();

		List<DropDown> reportPathList = new ArrayList<DropDown>();
		if("SSS".equals(currentUser.getCab_bank().trim())) {
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_prog"), "New Business Per Tanggal Akseptasi"));
			
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_prog_referral_all"), "New Business Per Referral"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_prog_all"), "New Business Per Tgl Cetak Sertifikat"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_prog_input_all"), "New Business Per Tgl Input Spaj"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_prog_produksi_all"), "New Business Per Tgl Produksi"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.view_prog_jatuh_tempo_all"), "Laporan Jatuh Tempo"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.view_prog_rollover_all"), "Laporan Rollover Per Tgl Produksi"));
		
		//Report untuk CS/Spv/Pincab Bank Sinarmas
		}else {
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_prog_tgl_cetak"), "New Business Per Tgl Cetak Sertifikat"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_prog_input"), "New Business Per Tgl Input Spaj"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_prog_produksi"), "New Business Per Tgl Produksi"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.view_prog_jatuh_tempo"), "Laporan Jatuh Tempo"));
//			reportPathList.add(new DropDown(props.getProperty("report.bac.view_stabil_rollover"), "Laporan Rollover Per Tgl Produksi"));
		}
		
		report = new Report("Progressive Link BSM Reports", reportPathList, Report.PDF, null);
		
		report.addParamDefault("username", "username", 200, currentUser.getName(), false);
		report.addParamDefault("cab_bank", "cab_bank", 200, currentUser.getCab_bank(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView nb_sms(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysDate = elionsManager.selectSysdate();

		List<DropDown> reportPathList = new ArrayList<DropDown>();
		if("SSS".equals(currentUser.getCab_bank().trim())) {
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_sms"), "New Business Per Tanggal Akseptasi"));
			
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_sms_referral_all"), "New Business Per Referral"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_sms_all"), "New Business Per Tgl Cetak Sertifikat"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_sms_input_all"), "New Business Per Tgl Input Spaj"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_sms_produksi_all"), "New Business Per Tgl Produksi"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.view_sms_jatuh_tempo_all"), "Laporan Jatuh Tempo"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.view_sms_rollover_all"), "Laporan Rollover Per Tgl Produksi"));
		
		//Report untuk CS/Spv/Pincab Bank Sinarmas
		}else {
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_sms_tgl_cetak"), "New Business Per Tgl Cetak Sertifikat"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_sms_input"), "New Business Per Tgl Input Spaj"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_sms_produksi"), "New Business Per Tgl Produksi"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.view_sms_jatuh_tempo"), "Laporan Jatuh Tempo"));
//			reportPathList.add(new DropDown(props.getProperty("report.bac.view_stabil_rollover"), "Laporan Rollover Per Tgl Produksi"));
		}
		
		report = new Report("Stable Link SMS Reports", reportPathList, Report.PDF, null);
		
		report.addParamDefault("username", "username", 200, currentUser.getName(), false);
		report.addParamDefault("cab_bank", "cab_bank", 200, currentUser.getCab_bank(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView pembayaran_bunga(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		report = new Report("Report Pembayaran Bunga", props.getProperty("report.bac.pembayaran_bunga"), Report.PDF, null);
		
		Date now = this.elionsManager.selectSysdate();
		int mpb_flag_bs = ServletRequestUtils.getIntParameter(request, "mpb_flag_bs", -1);
		String lcb_no = ServletRequestUtils.getStringParameter(request, "lcb_no", "");
		String startDate = ServletRequestUtils.getStringParameter(request, "startDate", defaultDateFormat.format(now));
		String endDate = ServletRequestUtils.getStringParameter(request, "endDate", defaultDateFormat.format(now));
		String flag_bs = Integer.toString(mpb_flag_bs);
		
		List daftar =elionsManager.selectMstProSave(mpb_flag_bs, lcb_no, defaultDateFormat.parse(startDate), defaultDateFormat.parse(endDate));
		
		report.addParamDefault("mpb_flag_bs", "mpb_flag_bs", 200, flag_bs, false);
		report.addParamDefault("lcb_no", "lcb_no", 200, lcb_no, false);
		report.addParamDefault("startDate", "startDate", 200, startDate, false);
		report.addParamDefault("endDate", "endDate", 200, endDate, false);
		report.setResultList(daftar);
		
		return prepareReport(request, response, report);
	}
	
	public ModelAndView list_rk(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysDate = elionsManager.selectSysdate();

		List<DropDown> reportPathList = new ArrayList<DropDown>();
		report = new Report("List RK", props.getProperty("report.bac.list_rk"), Report.PDF, null);
		
		Date now = this.elionsManager.selectSysdate();
		int lsrek_id = ServletRequestUtils.getIntParameter(request, "lsrek_id", -1);
		String startDate = ServletRequestUtils.getStringParameter(request, "startDate", defaultDateFormat.format(now));
		String endDate = ServletRequestUtils.getStringParameter(request, "endDate", defaultDateFormat.format(now));
		int lsbp_id = ServletRequestUtils.getIntParameter(request, "lsbp_id", 156);
		String kode = ServletRequestUtils.getStringParameter(request, "kode", "");
		String startNominal = ServletRequestUtils.getStringParameter(request, "startNominal", "0");
		String endNominal = ServletRequestUtils.getStringParameter(request, "endNominal", "99999999999");
		List daftarDrek = this.elionsManager.selectMstDrek(lsrek_id, lsbp_id, kode, defaultDateFormat.parse(startDate), defaultDateFormat.parse(endDate), Double.valueOf(startNominal), Double.valueOf(endNominal));
		
		report.addParamDefault("username", "username", 200, currentUser.getName(), false);
		report.addParamDefault("tanggalAwal", "tanggalAwal", 200, startDate, false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 200, endDate, false);
		report.setResultList(daftarDrek);
		
		return prepareReport(request, response, report);
	}
	
	public ModelAndView summary_ws_det (HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;

		report = new Report("Summary ws", props.getProperty("report.worksite.company_ws_det"), Report.PDF, null);
		
		String mcl_id= ServletRequestUtils.getStringParameter(request, "mcl_id", "");
		String jenis = ServletRequestUtils.getStringParameter(request, "jenis", "");
		List<Company_ws> daftarSummary = this.uwManager.selectMstSummaryWsDet(mcl_id, jenis);
		report.setResultList(daftarSummary);
		
		return prepareReport(request, response, report);
	}
	
	public ModelAndView nb_bank(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String flag = ServletRequestUtils.getStringParameter(request, "flag","");
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysDate = elionsManager.selectSysdate();
		HashMap mapReport = new HashMap();
		List<DropDown> reportPathList = new ArrayList<DropDown>();
		String isPDF = request.getParameter("isPDF");
		String isXls = request.getParameter("isXls");
		//Report Total Produksi
		if(flag.equals("1")) { 
			String cab = currentUser.getCab_bank();
			Integer jn_bank = currentUser.getJn_bank();
			if(jn_bank==2){//untuk bank sinarmas
				if(cab != null) {
					if(cab.trim().equals("SSS")) {
						reportPathList.add(new DropDown(props.getProperty("report.bac.total_produksi_all"), "Total Produksi (Berdasarkan Tanggal Produksi)"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.total_outstanding_all"), "Total Outstanding (Berdasarkan Tanggal Produksi)"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.total_break_all"), "Total Break / Cair (Berdasarkan Tanggal Produksi)"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.total_break_tgl_cair_all"), "Total Break / Cair (Berdasarkan Tanggal Pencairan Pinjaman)"));
//						reportPathList.add(new DropDown(props.getProperty("report.bac.total_break_tgl_bayar_all"), "Total Break / Bayar (Berdasarkan Tanggal Bayar)"));
					}else {
						reportPathList.add(new DropDown(props.getProperty("report.bac.total_produksi"), "Total Produksi (Berdasarkan Tanggal Produksi)"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.total_outstanding"), "Total Outstanding (Berdasarkan Tanggal Produksi)"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.total_break"), "Total Break / Cair (Berdasarkan Tanggal Produksi)"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.total_break_tgl_cair"), "Total Break / Cair (Berdasarkan Tanggal Pencairan Pinjaman)"));
						//reportPathList.add(new DropDown(props.getProperty("report.bac.total_break_tgl_bayar"), "Total Break / Bayar (Berdasarkan Tanggal Bayar)"));
					}
				}
			}else if(jn_bank==3){ // tambahan untuk sinarmas sekuritas
				reportPathList.add(new DropDown(props.getProperty("report.bac.total_produksi_all"), "Total Produksi (Berdasarkan Tanggal Produksi)"));
				reportPathList.add(new DropDown(props.getProperty("report.bac.total_outstanding_all"), "Total Outstanding (Berdasarkan Tanggal Produksi)"));
				reportPathList.add(new DropDown(props.getProperty("report.bac.total_break_all"), "Total Break / Cair (Berdasarkan Tanggal Produksi)"));
				reportPathList.add(new DropDown(props.getProperty("report.bac.total_break_tgl_cair_all"), "Total Break / Cair (Berdasarkan Tanggal Pencairan Pinjaman)"));
				//reportPathList.add(new DropDown(props.getProperty("report.bac.total_break_tgl_bayar_all"), "Total Break / Bayar (Berdasarkan Tanggal Bayar)"));
			}else {
				if(cab != null) {
					if(cab.trim().equals("SSS")) {
						reportPathList.add(new DropDown(props.getProperty("report.bac.total_produksi_all"), "Total Produksi (Berdasarkan Tanggal Produksi)"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.total_outstanding_all"), "Total Outstanding (Berdasarkan Tanggal Produksi)"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.total_break_all"), "Total Break / Cair (Berdasarkan Tanggal Produksi)"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.total_break_tgl_cair_all"), "Total Break / Cair (Berdasarkan Tanggal Pencairan Pinjaman)"));
						//reportPathList.add(new DropDown(props.getProperty("report.bac.total_break_tgl_bayar_all"), "Total Break / Bayar (Berdasarkan Tanggal Bayar)"));
					}else if(currentUser.getLus_id().equals("692")){	//request UW untuk dapat tarik report sama seperti user Bancass		
						mapReport = bacManager.selectMstConfig(6, "Report Produksi Simas Prima ALL WILAYAH", "report.bsm.simasprima_all_wil");
						reportPathList.add(new DropDown(mapReport.get("NAME").toString(), "Report Produksi Simas Prima ALL WILAYAH"));
				    }else {
						reportPathList.add(new DropDown(props.getProperty("report.bac.total_produksi"), "Total Produksi (Berdasarkan Tanggal Produksi)"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.total_outstanding"), "Total Outstanding (Berdasarkan Tanggal Produksi)"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.total_break"), "Total Break / Cair (Berdasarkan Tanggal Produksi)"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.total_break_tgl_cair"), "Total Break / Cair (Berdasarkan Tanggal Pencairan Pinjaman)"));
						//reportPathList.add(new DropDown(props.getProperty("report.bac.total_break_tgl_bayar"), "Total Break / Bayar (Berdasarkan Tanggal Bayar)"));
					}
				}
			}
			
				
		//Report Outstanding Summary
		}else if(flag.equals("2")) { 
			String cab = currentUser.getCab_bank();
			
			if(cab != null) {
				if(cab.trim().equals("SSS") || currentUser.getLus_id().equals("403") || currentUser.getLus_id().equals("692") || currentUser.getLus_id().equals("845") ||
				   currentUser.getLus_id().equals("39") || currentUser.getLus_id().equals("834")) {
					//bulanan
					reportPathList.add(new DropDown(props.getProperty("report.bac.outstanding_summary"), "Monthly Summary Outstanding"));
					//harian
                    // commented by samuel by Yusuf request
//                    reportPathList.add(new DropDown(props.getProperty("report.bac.outstanding_summary_daily"), "Daily Summary Outstanding"));
				}else {
					if (currentUser.getLus_id().equals("847")){//IBT SBY_SIMASPRIMA
						reportPathList.add(new DropDown(props.getProperty("report.bac.os_ibt"), "Summary Outstanding"));
					}else if(currentUser.getLus_id().equals("1053")){//BALI yulistyaWAti
						reportPathList.add(new DropDown(props.getProperty("report.bac.os_bali"), "Summary Outstanding"));
					}else if(currentUser.getLus_id().equals("583")){//MAKasar TIEN
						reportPathList.add(new DropDown(props.getProperty("report.bac.os_makasar"), "Summary Outstanding"));
					}
					else if(currentUser.getLus_id().equals("1916")){//MANADO BLM ADA USER
						reportPathList.add(new DropDown(props.getProperty("report.bac.os_manado"), "Summary Outstanding"));
					}
					else{
						reportPathList.add(new DropDown(props.getProperty("report.bac.outstanding_summary_per_branch"), "Summary Outstanding"));
					}
				}
			}else {
				reportPathList.add(new DropDown(props.getProperty("report.bac.outstanding_summary"), "Summary Outstanding"));
			}

		//Report Untuk Underwriting
		}else if(flag.equals("3")) { 
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_belum_uw"), "Polis Sudah Cetak Namun SPAJ Belum Diterima AJS (SIMAS PRIMA)"));

			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_stabil_belum_uw"), "Polis Sudah Cetak Namun SPAJ Belum Diterima AJS (SIMAS STABIL LINK)"));
			
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_danamas_prima_uw"), "Polis Sudah Cetak Namun SPAJ Belum Diterima AJS (DANA PRIMA)"));
		//Report2 untuk User Bank Sinarmas Syariah
		}else if(flag.equals("16")) { 
			reportPathList.add(new DropDown(props.getProperty("report.bac.outstanding_summary_sinarmas_syariah"), "Monthly Summary Outstanding"));
		//Report2 untuk User Bank Sinarmas
		}else {
			if(currentUser.getJn_bank()==2){//report untuk bank sinarmas
//				Report untuk Super User Bank Sinarmas
				if("SSS".equals(currentUser.getCab_bank().trim())) {
					reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_referral_all"), "New Business Per Referral"));
					reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_all"), "New Business Per Tgl Cetak Sertifikat"));
					reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_input_all"), "New Business Per Tgl Input Spaj"));
					reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_produksi_all"), "New Business Per Tgl Produksi"));
					reportPathList.add(new DropDown(props.getProperty("report.bac.view_jatuh_tempo_all"), "Laporan Jatuh Tempo"));
					reportPathList.add(new DropDown(props.getProperty("report.bac.view_rollover_all"), "Laporan Rollover Per Tgl Produksi"));
				
				//Report untuk CS/Spv/Pincab Bank Sinarmas
				}else {
					if(currentUser.getName().equals("SBY_SIMASPRIMA")){
						reportPathList.add(new DropDown(props.getProperty("report.bac.sby_nb_bank_sinarmas"), "New Business Per Tgl Cetak Sertifikat"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.sby_nb_bank_sinarmas_input"), "New Business Per Tgl Input Spaj"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.sby_nb_bank_sinarmas_produksi"), "New Business Per Tgl Produksi"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.sby_view_jatuh_tempo"), "Laporan Jatuh Tempo"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.sby_view_rollover"), "Laporan Rollover Per Tgl Produksi"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.sby_nb_bank_slink_produksi"), "New Business Per Tgl Produksi SLINK"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.sby_nb_individu_produksi"), "New Business Per Tgl Produksi INDIVIDU"));
						//reportPathList.add(new DropDown(props.getProperty("report.bac.sby_nb_bank_slink_input"), "New Business Per Tgl Input Spaj SLINK"));
//					}else if(currentUser.getLus_id().equals("583")){
//						reportPathList.add(new DropDown(props.getProperty("report.bac.sby_view_rollover_makasar"), "Laporan Rollover Per Tgl Produksi"));
//					}else if(currentUser.getLus_id().equals("1053")){
//						reportPathList.add(new DropDown(props.getProperty("report.bac.sby_view_rollover_bali"), "Laporan Rollover Per Tgl Produksi"));
//					}
//					else if(currentUser.getLus_id().equals("583")){//MANADO BLM AD USERNYA
//						reportPathList.add(new DropDown(props.getProperty("report.bac.sby_view_rollover_manado"), "Laporan Rollover Per Tgl Produksi"));
					}else{
						reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas"), "New Business Per Tgl Cetak Sertifikat"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_input"), "New Business Per Tgl Input Spaj"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_produksi"), "New Business Per Tgl Produksi"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.view_jatuh_tempo"), "Laporan Jatuh Tempo"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.view_rollover"), "Laporan Rollover Per Tgl Produksi"));
					}
				}
			}else if(currentUser.getJn_bank()==3){//report untuk sinarmas sekuritas
				reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_referral_all"), "New Business Per Referral"));
				reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_all"), "New Business Per Tgl Cetak Sertifikat"));
				reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_input_all"), "New Business Per Tgl Input Spaj"));
				reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_produksi_all"), "New Business Per Tgl Produksi"));
				reportPathList.add(new DropDown(props.getProperty("report.bac.view_jatuh_tempo_all"), "Laporan Jatuh Tempo"));
				reportPathList.add(new DropDown(props.getProperty("report.bac.view_rollover_all"), "Laporan Rollover Per Tgl Produksi"));
			}else if(currentUser.getLde_id().equals("11") || currentUser.getLde_id().equals("39") ){//request UW untuk dapat tarik report sama seperti user UW_SIMASPRIMA			
				reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_referral_all"), "New Business Per Referral"));
				reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_all"), "New Business Per Tgl Cetak Sertifikat"));
				reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_input_all"), "New Business Per Tgl Input Spaj"));
				reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_produksi_all"), "New Business Per Tgl Produksi"));
				reportPathList.add(new DropDown(props.getProperty("report.bac.view_jatuh_tempo_all"), "Laporan Jatuh Tempo"));
				reportPathList.add(new DropDown(props.getProperty("report.bac.view_rollover_all"), "Laporan Rollover Per Tgl Produksi"));			
		    }else if(currentUser.getLus_id().equals("692")){	//request UW untuk dapat tarik report sama seperti user Bancass			
		    	mapReport = bacManager.selectMstConfig(6, "Report Produksi Simas Prima ALL WILAYAH", "report.bsm.simasprima_all_wil");
				reportPathList.add(new DropDown(mapReport.get("NAME").toString(), "Report Produksi Simas Prima ALL WILAYAH"));
		    }else {
				reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas"), "New Business Per Tgl Cetak Sertifikat"));
				reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_input"), "New Business Per Tgl Input Spaj"));
				reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_produksi"), "New Business Per Tgl Produksi"));
				reportPathList.add(new DropDown(props.getProperty("report.bac.view_jatuh_tempo"), "Laporan Jatuh Tempo"));
				reportPathList.add(new DropDown(props.getProperty("report.bac.view_rollover"), "Laporan Rollover Per Tgl Produksi"));
			}
						
		}
		report = new Report("Simas Prima Reports", reportPathList, Report.PDF, null);
		
		String jn_bank = "0";
		String cab_bank = "";
		if(currentUser.getLde_id().equals("11") || currentUser.getLde_id().equals("39") ){
			jn_bank="2";
			cab_bank="SSS";
			currentUser.setValid_bank_1(845);
			currentUser.setValid_bank_2(845);
			
		}else{
			jn_bank=Integer.toString(currentUser.getJn_bank());
			cab_bank=currentUser.getCab_bank();
		}
		
		
		report.addParamDefault("username", "username", 200, currentUser.getName(), false);
		report.addParamDefault("cab_bank", "cab_bank", 200, cab_bank, false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDefault("jn_bank", "jn_bank", 200, jn_bank, false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
		report.addParamDefault("isPDF", "isPDF", 0, isPDF, false);
		report.addParamDefault("isXls", "isXls", 0, isXls, false);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView sph(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		//Date sysDate = elionsManager.selectSysdate();
		String lsbs_id = "";
		Integer lsdbs_no=0;
		
		String path = null;
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		if(!spaj.equals("")) {
			spaj = spaj.replace(".", "");
			
			String cabang = elionsManager.selectCabangFromSpaj(spaj);
			if(cabang == null) {
				spaj = elionsManager.selectSpajFromPolis(spaj);
				cabang = elionsManager.selectCabangFromSpaj(spaj);
			}
			
			if(spaj == null){
    			ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Nomor yang ada masukkan tidak terdaftar. Pastikan kembali no.SPAJ / no.POLIS yang diinput sudah benar!');</script>");
    			sos.close();
				return null;
			}
			
			path = props.getProperty("pdf.dir.export").trim()+"\\"+cabang.trim()+"\\"+spaj.trim()+"\\sph_blank.pdf";
			lsbs_id = uwManager.selectBusinessId(spaj);
			lsdbs_no = uwManager.selectBusinessNumber(spaj);
		}
		
		
		//APABILA STABLE LINK
		if(products.stableLink(lsbs_id)){
			if(lsbs_id.equals("174")){// Andhika - Stable Link Syariah
				report = new Report("Surat Perjanjian Pinjaman Lain-Lain", props.getProperty("report.sph_stable_syariah"), Report.PDF, path);
			}else{
				report = new Report("Surat Perjanjian Pinjaman Lain-Lain", props.getProperty("report.sph_stable"), Report.PDF, path);
			}
		//APABILA POLIS INPUTAN BANK SINARMAS
		}else if(lsbs_id.equals("175") ) {//Anta - Powersave Syariah BSIM
			report = new Report("Surat Perjanjian Pinjaman Polis", props.getProperty("report.sph_landscape_syariah"), Report.PDF, path);
		}else if(currentUser.getCab_bank() != null && (currentUser.getJn_bank().intValue() == 2 || currentUser.getJn_bank().intValue() == 3)) {
			//report = new Report("Surat Perjanjian Pinjaman Polis", "com/ekalife/elions/reports/sph/sph_blank", Report.PDF, path);
			report = new Report("Surat Perjanjian Pinjaman Polis", props.getProperty("report.sph_banksinarmas"), Report.PDF, path);
		}else {
			report = new Report("Surat Perjanjian Pinjaman Polis", props.getProperty("report.sph_landscape"), Report.PDF, path);
		}
		report.addParamText("Nomor Polis / Nomor Register SPAJ", "spaj", 17, null, true);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView sph_rate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		//Date sysDate = elionsManager.selectSysdate();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		
		//validasi akses
		if(!spaj.equals("")) {
			spaj = elionsManager.selectSpajFromPolis(spaj);
			boolean gakBoleh = false;
			//1. akses cabang, untuk polis biasa
			List akses = currentUser.getAksesCabang();
			String region = elionsManager.selectCabangFromSpaj_lar(spaj);
			if (!akses.contains(region)) {
				gakBoleh = true;
			}
			
			//2. akses per user only, untuk polis2 inptan bank (Yusuf =- 14/12/2007)
			int isInputanBank = elionsManager.selectIsInputanBank(spaj);
			if(isInputanBank==2 || isInputanBank==3) {
				if(elionsManager.selectIsUserYangInputBank(spaj, Integer.valueOf(currentUser.getLus_id())) < 1){
					gakBoleh = true;
				}
			}
			
			if(currentUser.getLca_id().equals("01")) { //kantor pusat tidak dibatasi
				gakBoleh = false;
			}
			
			if(gakBoleh) {
				response.sendRedirect(request.getContextPath() + "/include/page/blocked.jsp?jenis=branch");
				return null;
			}
			
		}
		
		String path = null;
		if(!spaj.equals("")) {
			String cabang = elionsManager.selectCabangFromSpaj(spaj);
			path = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\sph_rate.pdf";
		}
		
		//APABILA POLIS INPUTAN BANK SINARMAS / SINARMAS SEKURITAS
		if(currentUser.getJn_bank().intValue() == 2 || currentUser.getJn_bank().intValue() == 3) {
			report = new Report("Surat Perjanjian Pinjaman Polis", props.getProperty("report.sph_banksinarmas_rate"), Report.PDF, path);
		}else {
			report = new Report("Surat Perjanjian Pinjaman Polis", props.getProperty("report.sph_landscape_rate"), Report.PDF, path);
		}
		report.addParamText("Nomor Register SPAJ / Nomor Polis", "spaj", 17, null, true);
		report.addParamDate("Tanggal Pengajuan", "ajudate", false, new Date[] {elionsManager.selectTanggalJatuhTempoPowersave(spaj)}, true);
		if(currentUser.getLca_id().equals("01") && currentUser.getCab_bank() == null) {
			report.setReportCanBeEmailed(true);
			report.setReportEmailTo(new String[] {"grisye@sinarmasmsiglife.co.id", "yantisumirkan@sinarmasmsiglife.co.id", "lylianty@sinarmasmsiglife.co.id"});
			//report.setReportEmailTo(new String[] {props.getProperty("admin.yusuf")});
			report.setReportSubject("[Sinarmasmsiglife IT] Surat Perjanjian Pinjaman Polis " + ServletRequestUtils.getStringParameter(request, "spaj", ""));
			report.setReportMessage(
					ServletRequestUtils.getStringParameter(request, "reportMessage", 
					"Dh,<br>Berikut dokumen Surat Perjanjian Pinjaman Polis untuk nomor register / nomor polis : " + spaj+"<br>"+
					"<br>Keterangan:<br>"
					));
		}
		
		return prepareReport(request, response, report);
	}

	public ModelAndView sph_email(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		//bii - SPECTA
		map.put("listABM", elionsManager.selectABM());
		
		//bii - platinum dan smart invest
		map.put("listABM2", elionsManager.selectABM2());
		
		//mayapada
		map.put("listMayapada", elionsManager.selectCabangMayapada());
		
		//uob
		map.put("listUOB", elionsManager.selectCabangUOB());
		
		List<Map> listProduk = new ArrayList<Map>();
		Map<String, String> p = new HashMap<String, String>();
		p.put("lsbs", "142004"); //mayapada
		p.put("nama", "MY SAVING INVESTA");
		listProduk.add(p);
		p = new HashMap<String, String>();
		p.put("lsbs", "142005"); //uob
		p.put("nama", "PRIVILEGE SAVE");
		listProduk.add(p);
		p = new HashMap<String, String>();
		p.put("lsbs", "155001"); //bii
		p.put("nama", "PLATINUM SAVE");
		listProduk.add(p);
		p = new HashMap<String, String>();
		p.put("lsbs", "155002"); //bii
		p.put("nama", "SPECTA SAVE");
		listProduk.add(p);
		p = new HashMap<String, String>();
		p.put("lsbs", "155003"); //bii
		p.put("nama", "SMART INVEST");
		listProduk.add(p);
		map.put("listProduk", listProduk);
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date now = new Date();
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");

		String area = ServletRequestUtils.getStringParameter(request, "area", "");
		String area2 = ServletRequestUtils.getStringParameter(request, "area2", "");
		String cab_mayapada = ServletRequestUtils.getStringParameter(request, "cab_mayapada", "");
		String cab_uob = ServletRequestUtils.getStringParameter(request, "cab_uob", "");
		Date startDate = df.parse(ServletRequestUtils.getStringParameter(request, "startDate", df.format(now)));
		Date endDate = df.parse(ServletRequestUtils.getStringParameter(request, "endDate", df.format(now)));
		Date ajuDate = df.parse(ServletRequestUtils.getStringParameter(request, "ajudate", df.format(now)));
		String emailfrom = ServletRequestUtils.getStringParameter(request, "emailfrom", currentUser.getEmail());
		String emailto = ServletRequestUtils.getStringParameter(request, "emailto", "");
		String emailcc = ServletRequestUtils.getStringParameter(request, "emailcc", "");
		String emailsubject = ServletRequestUtils.getStringParameter(request, "emailsubject", "");
		String emailmessage = ServletRequestUtils.getStringParameter(request, "emailmessage", "");
		String produk = ServletRequestUtils.getStringParameter(request, "produk", "142004");
		
		String pass = ServletRequestUtils.getStringParameter(request, "setPass","");
		//map.put("pass", pass);	
		
//		String area = ServletRequestUtils.getStringParameter(request, "area", "020701");
//		String cab_mayapada = ServletRequestUtils.getStringParameter(request, "cab_mayapada", "R21");
//		String cab_uob = ServletRequestUtils.getStringParameter(request, "cab_uob", "U12");
//		Date startDate = df.parse(ServletRequestUtils.getStringParameter(request, "startDate", "01/01/2008"));
//		Date endDate = df.parse(ServletRequestUtils.getStringParameter(request, "endDate", "01/03/2008"));
//		Date ajuDate = df.parse(ServletRequestUtils.getStringParameter(request, "ajuDate", df.format(now)));
//		String emailfrom = ServletRequestUtils.getStringParameter(request, "emailfrom", props.getProperty("admin.yusuf"));
//		String emailto = ServletRequestUtils.getStringParameter(request, "emailto", props.getProperty("admin.yusuf"));
//		String emailcc = ServletRequestUtils.getStringParameter(request, "emailcc", props.getProperty("admin.yusuf"));
//		String emailsubject = ServletRequestUtils.getStringParameter(request, "emailsubject", "test");
//		String emailmessage = ServletRequestUtils.getStringParameter(request, "emailmessage", "test");
//		String produk = ServletRequestUtils.getStringParameter(request, "produk", "155002");

		if(request.getParameter("tarik") != null) {
			List<HashMap> daftarSpaj = null;
			if(produk.equals("142004")) { //mayapada
				daftarSpaj = elionsManager.selectSpajFromLCB(startDate, endDate, cab_mayapada, produk);
			}else if(produk.equals("142005")) { //uob
				daftarSpaj = elionsManager.selectSpajFromLCB(startDate, endDate, cab_uob, produk);
			}else if(produk.equals("155001")) { //bii - platinum save
				daftarSpaj = elionsManager.selectSpajFromABM2(startDate, endDate, area2, produk);
			}else if(produk.equals("155002")) { //bii - specta save
				daftarSpaj = elionsManager.selectSpajFromABM(startDate, endDate, area, produk);
			}else if(produk.equals("155003")) { //bii - smart invest
				daftarSpaj = elionsManager.selectSpajFromABM2(startDate, endDate, area2, produk);
			}
			session.setAttribute("daftarSpajSphEmail", daftarSpaj);
		} else if(request.getParameter("send") != null) {
			
			if(emailfrom == null || emailto == null || emailsubject == null || emailmessage == null ) {
				request.setAttribute("message", "Harap lengkapi data dengan benar terlebih dahulu");
			} else if(emailfrom.equals("") || emailto.equals("") || emailsubject.equals("") || emailmessage.equals("")) {
				request.setAttribute("message", "Harap lengkapi data dengan benar terlebih dahulu");
			} else {
				List<HashMap> daftarSpaj = (List<HashMap>) session.getAttribute("daftarSpajSphEmail");
				if(daftarSpaj == null) daftarSpaj = new ArrayList<HashMap>();
				
				DateFormat df2 = new SimpleDateFormat("yyyyMMddhhmmss");
				File dir= new File(props.getProperty("pdf.dir.sph")+"\\"+currentUser.getLus_id()+"\\"+df2.format(now)+"\\"+area);
				if(!dir.exists()) dir.mkdirs();
				
				Connection conn = null;
				try {
					//conn = this.getDataSource().getConnection();
					conn = this.getUwManager().getUwDao().getDataSource().getConnection();
					for(HashMap m : daftarSpaj) {
						String reg_spaj = (String) m.get("REG_SPAJ");
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("ajudate", ajuDate);
						params.put("props", props);
						params.put("spaj", reg_spaj);
	
						String reportPath;
	
						//Yusuf 12/3/09 - Request Wulan : Semuanya SPH Blank aja, tidak cuman mayapada
						//if(produk.equals("142004")) reportPath = session.getServletContext().getRealPath("WEB-INF") + "/classes/com/ekalife/elions/reports/sph/sph_landscape.jasper";
						//else reportPath = session.getServletContext().getRealPath("WEB-INF") + "/classes/com/ekalife/elions/reports/sph/sph_landscape_rate.jasper";
						reportPath = session.getServletContext().getRealPath("WEB-INF") + "/classes/com/ekalife/elions/reports/sph/sph_landscape.jasper";
						
						if(!pass.equals("")) {
							String password = elionsManager.createPass(reg_spaj);
							JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, params, conn);
							JRPdfExporter jrpdfexporter = new JRPdfExporter();
							jrpdfexporter.setParameters(	JasperUtils.getPdfExporterParameter(PdfWriter.AllowPrinting, null, password));
							jrpdfexporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
							jrpdfexporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, dir+"\\"+reg_spaj+".pdf");
							jrpdfexporter.exportReport();						
						}
						else {
							JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, params, conn);
							JasperExportManager.exportReportToPdfFile(jasperPrint, dir+"\\"+reg_spaj+".pdf");						
						}
	
					}
				}finally {
					closeConnection(conn);
				}

				request.setAttribute("message", "E-mail berhasil dikirim. Jumlah SPH adalah: " + daftarSpaj.size() + " spaj.");				
				
				try {
					List<File> daftarSPH = FileUtils.listFilesInDirectory2(dir.toString());
					if(!daftarSPH.isEmpty()) {
						boolean page = false;
						if(daftarSPH.size() > 20) page = true; 
						for(int i=0; i<(daftarSPH.size()/20)+1; i++) {
							logger.info(
									"Dari " + (20*i) + " sampai " + ((20*(i+1)) > daftarSPH.size() ? daftarSPH.size() : (20*(i+1)))
							);
							email.send( false,
									emailfrom, emailto.split(";"), emailcc.split(";"), null, 
									emailsubject + (page ? (" (E-mail ke "+(i+1)+" dari "+((daftarSPH.size()/20)+1)+")"):"" ), 
									emailmessage, daftarSPH.subList((20*i), (20*(i+1)) > daftarSPH.size() ? daftarSPH.size() : (20*(i+1))));
						}
						session.removeAttribute("daftarSpajSphEmail");
					}else {
						request.setAttribute("message", "Tidak ada SPH dengan tanggal jatuh tempo antara " + df.format(startDate) + " dan " + df.format(endDate));
					}
				} catch (RuntimeException e) {
					logger.error("ERROR :", e);
					request.setAttribute("message", "E-mail GAGAL dikirim. Harap cek apakah alamat e-mail yang anda masukkan valid.");
				}
				
			}
				
		}
		
		request.setAttribute("area", area);
		request.setAttribute("cab_mayapada", cab_mayapada);
		request.setAttribute("cab_uob", cab_uob);
		request.setAttribute("startDate", df.format(startDate));
		request.setAttribute("endDate", df.format(endDate));
		request.setAttribute("ajuDate", df.format(ajuDate));
		request.setAttribute("emailfrom", emailfrom);
		request.setAttribute("emailto", emailto);
		request.setAttribute("emailcc", emailcc);
		request.setAttribute("emailsubject", emailsubject);
		request.setAttribute("emailmessage", emailmessage);
		request.setAttribute("produk", produk);

		return new ModelAndView("bac/sph", map);
	}
	
	public ModelAndView summary_biasa(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		List<DropDown> reportPathList = new ArrayList<DropDown>();
		reportPathList.add(new DropDown(props.getProperty("report.summary.biasa"), "Summary Biasa"));
		reportPathList.add(new DropDown(props.getProperty("report.summary.bankbook_pending"), "Summary Bank Book Pending"));
		reportPathList.add(new DropDown(props.getProperty("report.summary.bankbook"), "Summary Bank Book"));
		reportPathList.add(new DropDown(props.getProperty("report.summary.all"), "Summary All"));
		reportPathList.add(new DropDown(props.getProperty("report.summary.register_spaj"), "Register SPAJ per User"));
		Date sysDate = elionsManager.selectSysdate();
		
		report = new Report("Summary Biasa", reportPathList, Report.PDF, null);
		report.addParamDate("Tanggal", "tgl", true, new Date[] {sysDate, sysDate}, true);
		
		//IT atau UW, bisa print
		if(currentUser.getLde_id().equals("01") || currentUser.getLde_id().equals("11")) {
			report.addParamSelect("User ID", "lus_id",	
				elionsManager.selectDropDownHashMap("EKA.LST_USER", "LUS_ID", "LUS_LOGIN_NAME", "LUS_LOGIN_NAME", "LDE_ID = 11"), "All", true);
		}else {
			report.addParamDefault("User ID", "lus_id", 20, currentUser.getLus_id(), false);
		}
		report.setReportQueryMethod("selectReportSummaryBiasa");
		report.addParamDefault("User Print", "user_print", 0, currentUser.getName(), false);
		
		return prepareReport(request, response, report);
	}
	
	public ModelAndView summary_biasa_guthrie(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		List<DropDown> reportPathList = new ArrayList<DropDown>();
		reportPathList.add(new DropDown(props.getProperty("report.summary.biasa_guthrie"), "Summary Biasa Guthrie"));
		
		Date sysDate = elionsManager.selectSysdate();
		
		report = new Report("Summary Biasa Guthrie", reportPathList, Report.PDF, null);
		report.addParamDate("Tanggal", "tgl", true, new Date[] {sysDate, sysDate}, true);
		report.addParamDefault("User ID", "lus_id", 20, currentUser.getLus_id(), false);
		
		return prepareReport(request, response, report);
	}

	public ModelAndView summary_recurring(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");

		List<DropDown> reportPathList = new ArrayList<DropDown>();
		reportPathList.add(new DropDown(props.getProperty("report.summary.recurring"), "New Business (Saving)"));
		reportPathList.add(new DropDown(props.getProperty("report.summary.recurring_cc"), "New Business (Credit Card)"));
		reportPathList.add(new DropDown(props.getProperty("report.summary.recurring_payment"), "Payment"));

		Date sysDate = elionsManager.selectSysdate();
		
		report = new Report("Summary Recurring", reportPathList, Report.PDF, null);
		report.addParamDate("Tanggal", "tgl", true, new Date[] {sysDate, sysDate}, true);
		report.addParamDefault("User", "user", 20, currentUser.getName(), true);
		report.addParamDefault("Print Date", "tanggal", 20, completeDateFormat.format(elionsManager.selectSysdateSimple()), true);

		return prepareReport(request, response, report);
	}
	
	public ModelAndView summary_per_plan(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		Date sysDate = elionsManager.selectSysdate();
		report = new Report("Summary Per Plan",props.getProperty("report.summary.perplan"),Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true,new Date[] {sysDate, sysDate}, true);
		//kalo reportQueryMethod tidak di-set, maka report akan mengambil data langsung dari database
		//report.setReportQueryMethod("");

		return prepareReport(request, response, report);
	}
	
	public ModelAndView report_absensi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report = null;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysdate = elionsManager.selectSysdate();
		
		String admin = ServletRequestUtils.getStringParameter(request, "admin","");
		String msag_id = ServletRequestUtils.getStringParameter(request, "msag_id","");
		String tanggalAwal = ServletRequestUtils.getStringParameter(request, "tanggalAwal");
		String tanggalAkhir = ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
		
		if(msag_id.equals("0")) {
			report = new Report("Absensi Agent All",props.getProperty("report.bac.absensi_agen_all"),Report.PDF, null);
		}
		else {
			report = new Report("Absensi Agent",props.getProperty("report.bac.absensi_agen"),Report.PDF, null);
			report.addParamDefault("msag_id", "msag_id", 0, msag_id, false);
		}
		
		report.addParamDefault("lus_id", "lus_id", 0, !admin.equals("") ? admin : currentUser.getLus_id(), false);
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);

		return prepareReport(request, response, report);
	}	

	public ModelAndView surat_fat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		Report report = null;
		User currentUser = (User) request.getSession().getAttribute("currentUser");

		String path = null;
		String fatid = ServletRequestUtils.getStringParameter(request, "fatid");
		Fat fat = new Fat();
		if(!fatid.equals("")) {
			
			//validasi posisi
			fat = uwManager.selectFat(fatid);
			if(fat.posisi.intValue() != 4) {
				PrintWriter out = response.getWriter();
				out.print("<script>window.alert('Anda hanya dapat mencetak surat yang berada pada posisi [4. PRINT]'); window.close();</script>");
				return null;
			}
			
			path = props.getProperty("pdf.dir.fat").trim() + "\\" + fatid + ".pdf";
		}
		
		if(fat.ket_jbt.equals("BST")){
			report = new Report("Surat Pengajuan BST", props.getProperty("report.cross_selling.bst_new"), Report.PDF, path);
		}else if(fat.ket_jbt.equals("FATS")){
			report = new Report("Surat Pengajuan FATS", props.getProperty("report.cross_selling.fats_new"), Report.PDF, path);
		}else{
			report = new Report("Surat Pengajuan FAT", props.getProperty("report.cross_selling.fat_new"), Report.PDF, path);
		}
		report.addParamText("Id FAT", "fatid", 12, null, true);
		
		//sebelum show report, simpan dulu datanya
		uwManager.saveFatLetter(fatid, currentUser);
		
		return prepareReport(request, response, report);
	}	
	
	public ModelAndView nametag_fat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");

		String path = null;
		String fatid = ServletRequestUtils.getStringParameter(request, "fatid");
		if(!fatid.equals("")) {
			
			//validasi posisi
			Fat fat = uwManager.selectFat(fatid);
			if(fat.posisi.intValue() != 4) {
				PrintWriter out = response.getWriter();
				out.print("<script>window.alert('Anda hanya dapat mencetak nametag yang berada pada posisi [4. PRINT]'); window.close();</script>");
				return null;
			}
			
			path = props.getProperty("pdf.dir.fat").trim() + "\\nametag\\" + fatid + "_nametag.pdf";

			report = null;
			if(fat.ket_jbt.equals("BST")){
				report = new Report("Surat Pengajuan BST", props.getProperty("report.cross_selling.nametag.bst"), Report.PDF, path);
			}else if(fat.ket_jbt.equals("FAT")){
				report = new Report("Surat Pengajuan FAT", props.getProperty("report.cross_selling.nametag.fat"), Report.PDF, path);
			}else if(fat.ket_jbt.equals("FATS")){
				report = new Report("Surat Pengajuan FATS", props.getProperty("report.cross_selling.nametag.fats"), Report.PDF, path);
			}else if(fat.ket_jbt.equals("BAC")){
				report = new Report("Surat Pengajuan BAC", props.getProperty("report.cross_selling.nametag.bac"), Report.PDF, path);
			}
			report.addParamText("Id FAT", "fatid", 12, null, true);
			
			//sebelum show report, simpan dulu historynya
			//insert history
			Date sysdate = elionsManager.selectSysdate();
			FatHistory fh = new FatHistory(fatid, fat.posisi, Integer.parseInt(currentUser.getLus_id()), null, sysdate, "CETAK NAMETAG");
			uwManager.insertLstFatHistory(fh);
			
			return prepareReport(request, response, report);
		}
		
		return null;
	}	
	
	public ModelAndView nb_bank_bsim(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String flag = ServletRequestUtils.getStringParameter(request, "flag","");
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysDate = elionsManager.selectSysdate();

		List<DropDown> reportPathList = new ArrayList<DropDown>();
		String isPDF = request.getParameter("isPDF");
		String isXls = request.getParameter("isXls");
		//Report Total Produksi
		if(flag.equals("")) { 
			String cab = currentUser.getCab_bank();
			Integer jn_bank = currentUser.getJn_bank();
			if(jn_bank==16){//untuk bank sinarmas
				if(cab != null) {
					if(cab.trim().equals("SSS")) {
						reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_syariah_all"), "New Business Per Tgl Cetak Sertifikat"));
						reportPathList.add(new DropDown(props.getProperty("report.bac.nb_bank_sinarmas_input_all_pwr"), "New Business Per Tgl Input Spaj"));
					}
				}
			}
			
		//Report Outstanding Summary
		}else if(flag.equals("2")) {}else if(flag.equals("3")) {}else {}
		report = new Report("Powersave Syariah", reportPathList, Report.PDF, null);
		
		String jn_bank = "16";
		
		report.addParamDefault("username", "username", 200, currentUser.getName(), false);
		report.addParamDefault("cab_bank", "cab_bank", 200, currentUser.getCab_bank(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDefault("jn_bank", "jn_bank", 200, jn_bank, false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
		report.addParamDefault("isPDF", "isPDF", 0, isPDF, false);
		report.addParamDefault("isXls", "isXls", 0, isXls, false);
		return prepareReport(request, response, report);
	}
	
	public ModelAndView suratendors(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
/*
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		//Date sysDate = elionsManager.selectSysdate();
		String lsbs_id = "";
		Integer lsdbs_no=0;
		
		String path = null;
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");*/
	/*	if(!spaj.equals("")) {
			spaj = spaj.replace(".", "");
			
			String cabang = elionsManager.selectCabangFromSpaj(spaj);
			if(cabang == null) {
				spaj = elionsManager.selectSpajFromPolis(spaj);
				cabang = elionsManager.selectCabangFromSpaj(spaj);
			}
			
			path = props.getProperty("pdf.dir.export").trim()+"\\"+cabang.trim()+"\\"+spaj.trim()+"\\sph_blank.pdf";
			lsbs_id = uwManager.selectBusinessId(spaj);
			lsdbs_no = uwManager.selectBusinessNumber(spaj);
		}
		
		String a = props.getProperty("report.surat_endors");
		
		//APABILA STABLE LINK
		if(products.stableLink(lsbs_id)){
			report = new Report("Surat Perjanjian Pinjaman Lain-Lain", props.getProperty("report.sph_stable"), Report.PDF, path);
		//APABILA POLIS INPUTAN BANK SINARMAS
		}else if(lsbs_id.equals("175") && lsdbs_no == 2) {//Anta - Powersave Syariah BSIM
			report = new Report("Surat Perjanjian Pinjaman Polis", props.getProperty("report.sph_landscape_syariah"), Report.PDF, path);
		}else if(currentUser.getCab_bank() != null && (currentUser.getJn_bank().intValue() == 2 || currentUser.getJn_bank().intValue() == 3)) {
			//report = new Report("Surat Perjanjian Pinjaman Polis", "com/ekalife/elions/reports/sph/sph_blank", Report.PDF, path);
			report = new Report("Surat Perjanjian Pinjaman Polis", props.getProperty("report.sph_banksinarmas"), Report.PDF, path);
		}else {
			report = new Report("Surat Perjanjian Pinjaman Polis", props.getProperty("report.sph_landscape"), Report.PDF, path);
		}
		report.addParamText("Nomor Polis / Nomor Register SPAJ", "spaj", 17, null, true);
		return prepareReport(request, response, report);*/
		
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		//Date sysDate = elionsManager.selectSysdate();
		String lsbs_id = "";
		Integer lsdbs_no=0;
		
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		
		report = new Report("Surat endors Jatuh Tempo", props.getProperty("report.surat_endors"), Report.PDF, null);
		report.addParamText("Nomor Polis / Nomor Register SPAJ", "spaj", 17, null, true);
		report.addParamDefault("spaj", "spaj", 200, spaj, false);
		return prepareReport(request, response, report);
	}
	
	/**
	 * Report Stabil Link 36 bulan
	 * Andhika (08/02/2013)
	 */	
	public ModelAndView nb_stabil_36(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysDate = elionsManager.selectSysdate();

		List<DropDown> reportPathList = new ArrayList<DropDown>();
		
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_stabil_36_input"), "SPAJ Input"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.nb_stabil_36_input_transfer_uw"), "SPAJ Input Transfer Ke UW"));
			
		report = new Report("Stabil Link 36 bulan Report", reportPathList, Report.PDF, null);
		
		report.addParamDefault("username", "username", 200, currentUser.getName(), false);
		report.addParamDefault("cab_bank", "cab_bank", 200, currentUser.getCab_bank(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
		return prepareReport(request, response, report);
	}
	
	/**
	 * Report Polis Pribadi Agent
	 * Andhika (16/01/2013)
	 */	
	public ModelAndView polis_pribadi_agen(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysDate = elionsManager.selectSysdate();

		List<DropDown> reportPathList = new ArrayList<DropDown>();
		
			reportPathList.add(new DropDown(props.getProperty("report.bac.polis_pribadi_agen"), "Polis Pribadi Agen"));
			
		report = new Report("Report Polis Pribadi Agen", reportPathList, Report.PDF, null);
		
		report.addParamDefault("username", "username", 200, currentUser.getName(), false);
		report.addParamDefault("cab_bank", "cab_bank", 200, currentUser.getCab_bank(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
		return prepareReport(request, response, report);
	}
	
	  public ModelAndView nationwide_bsm( HttpServletRequest request, HttpServletResponse response ) throws Exception
	    {
	        Report report;
	        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
	        Date sysDate = elionsManager.selectSysdate();
	        String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal","");
			String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir","");
			Date feb01 = defaultDateFormat.parse("31/12/2012");
			
			if (!tanggalAwal.equals("") && !tanggalAkhir.equals("")){
			Date awal = defaultDateFormat.parse(tanggalAwal);
			Date akhir = defaultDateFormat.parse(tanggalAkhir);
			if(awal.before(feb01) || akhir.before(feb01) ) {
				PrintWriter out = response.getWriter();
				out.print("<script>window.alert('Tanggal Yang Dipilih Tidak Boleh Kurang Dari Tahun 2013'); window.close();</script>");
				return null;
				}
			}
	        List<DropDown> reportPathList = new ArrayList<DropDown>();
	        // dibawah ini buwat nambahin jenis2 report
	        reportPathList.add( new DropDown(
	                props.getProperty( "report.polis.nationwide_bsm" ),
	                "Report Laporan Produksi Nationwide BSM"
	        ) );

	        report = new Report( "Report Nation Wide BSM", reportPathList, Report.PDF, null );

	        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
	        report.addParamDefault( "username", "username", 0, currentUser.getLus_full_name(), false );
	        report.addParamDefault( "lus_id", "lus_id", 200, currentUser.getLus_id(), false );
	        report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
	    	report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
			report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);

	        return prepareReport( request, response, report );
	    }
	
	public ModelAndView reportRef(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User)request.getSession().getAttribute("currentUser");
		
		if(request.getParameter("showReport") != null){
			String bdate =  ServletRequestUtils.getStringParameter(request, "bdate");
			String edate =  ServletRequestUtils.getStringParameter(request, "edate");
			
			List data = uwManager.selectReportRef(bdate,edate);
			
			if(data.size() > 0){ //bila ada data
	    		ServletOutputStream sos = response.getOutputStream();
	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bac.reportRef") + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);
	    		params.put("pdate", defaultDateFormat.format(elionsManager.selectSysdate()));

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

        	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
        	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(30)));
        	
        	return new ModelAndView("report/reportRef", m);
		}
		return null;
	}
	
	/**
	 * Service Level simas prima series
	 * Andhika (06/09/2013)
	 */	
	public ModelAndView sl_simasprima_series(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date sysDate = elionsManager.selectSysdate();

		List<DropDown> reportPathList = new ArrayList<DropDown>();
		
			reportPathList.add(new DropDown(props.getProperty("report.bac.sl1_simasprima_series"), "Service Level 1"));
			reportPathList.add(new DropDown(props.getProperty("report.bac.sl2_simasprima_series"), "Service Level 2"));
			
		report = new Report("Service Level Simas Prima", reportPathList, Report.PDF, null);
		
		report.addParamDefault("username", "username", 200, currentUser.getName(), false);
		report.addParamDefault("cab_bank", "cab_bank", 200, currentUser.getCab_bank(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
		return prepareReport(request, response, report);
	}	
	
	public ModelAndView reportProduksiCair(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User)request.getSession().getAttribute("currentUser");
		
		if(request.getParameter("showReport") != null){
			String bdate =  ServletRequestUtils.getStringParameter(request, "bdate");
			String edate =  ServletRequestUtils.getStringParameter(request, "edate");
			String jn_report = ServletRequestUtils.getStringParameter(request, "jn_report", "0");
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			String outputDir = props.getProperty("pdf.dir.report") + "\\produksi_cair_direksi\\";
			String outputFilename = "fr_prod_cair" + sdf.format(elionsManager.selectSysdate()) + ".xls";
			SimpleDateFormat formatDate =new SimpleDateFormat("dd/MM/yyyy");
			List reportProduksiCair = bacManager.selectreportProduksiCair( formatDate.parse(bdate), formatDate.parse(edate), Integer.parseInt(jn_report));
			if(reportProduksiCair.size()>0){
				if(Integer.parseInt(jn_report)==0){
					XLSCreatorFrProduksiCair xlsCreatorFrProduksiCair = new XLSCreatorFrProduksiCair();
					xlsCreatorFrProduksiCair.buildExcelDocument("sheet1", outputDir+"\\"+outputFilename, reportProduksiCair, formatDate.parse(bdate),formatDate.parse(edate));
				}else if(Integer.parseInt(jn_report)==1){
					XLSCreatorFrProduksiCairMGI xlsCreatorFrProduksiCair = new XLSCreatorFrProduksiCairMGI();
					xlsCreatorFrProduksiCair.buildExcelDocument("sheet1", outputDir+"\\"+outputFilename, reportProduksiCair, formatDate.parse(bdate),formatDate.parse(edate));
				}
				File l_file = new File(outputDir+"//"+outputFilename);
				FileInputStream in = null;
				ServletOutputStream ouputStream = null;
				try{
					
					response.setContentType("application/force-download");//application/force-download
					response.setHeader("Content-Disposition", "Attachment;filename="+l_file);
					response.setHeader("Expires", "0");
					response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					response.setHeader("Pragma", "public");
					
					in = new FileInputStream(l_file);
				    ouputStream = response.getOutputStream();
				    
				    IOUtils.copy(in, ouputStream);
				}catch (Exception e) {
					logger.error("ERROR :", e);
				}finally {
					try {
						if(in != null) {
							in.close();
						}
						if(ouputStream != null) {
							ouputStream.close();
						}
					}catch (Exception e) {
						logger.error("ERROR :", e);
					}
				}
			}else{
				ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Tidak ada data');window.close();</script>");
    			sos.close();
			}
		}else{
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("jn_report", 0);
	    	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
	    	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(30)));
	    	return new ModelAndView("report/reportProduksiCair", m);
		}
		return null;
	}
	
    /**
     * @author Andhika
     * @since Dec 02, 2013 (11:30:00 AM)
     */    
    public ModelAndView summary_ratio( HttpServletRequest request, HttpServletResponse response ) throws Exception{
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();
        
        List<DropDown> reportPathList = new ArrayList<DropDown>();
        
        	reportPathList.add( new DropDown(
                props.getProperty( "report.bac.summary_ratio_individu" ),
                "Individu"
        	) );
        	reportPathList.add( new DropDown(
                props.getProperty( "report.bac.summary_ratio_mri" ),
                "MRI"
        	) );
        	
        String namaprod = "";	
        String lsdbsnumber = "";
        
    	report = new Report( "Report Summary Ratio Accepted", reportPathList, Report.PDF, null );
    	
		report.addParamDefault("username", "username", 200, currentUser.getLus_full_name(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDefault("namaprod", "namaprod", 200, namaprod, false);
		report.addParamDefault("lsdbsnumber", "lsdbsnumber", 200, lsdbsnumber, false);
    	report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
    	return prepareReport( request, response, report );
    }	
    
    /**
     * @author Andhika
     * @since Dec 02, 2013 (11:30:00 AM)
     */    
    public ModelAndView summary_ratio_prod( HttpServletRequest request, HttpServletResponse response ) throws Exception{
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();
        
        List<DropDown> reportPathList = new ArrayList<DropDown>();
        
        	reportPathList.add( new DropDown(
                props.getProperty( "report.bac.summary_ratio_individu_prod" ),
                "Individu"
        	) );
        	reportPathList.add( new DropDown(
                props.getProperty( "report.bac.summary_ratio_mri_prod" ),
                "MRI"
        	) );
        	
        String namaprod = "";	
        String lsdbsnumber = "";
        
    	report = new Report( "Report Summary Ratio Production", reportPathList, Report.PDF, null );
    	
		report.addParamDefault("username", "username", 200, currentUser.getLus_full_name(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDefault("namaprod", "namaprod", 200, namaprod, false);
		report.addParamDefault("lsdbsnumber", "lsdbsnumber", 200, lsdbsnumber, false);
    	report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
    	return prepareReport( request, response, report );
    }
    
    /**
     * @author Andhika
     * @since Dec 02, 2013 (11:30:00 AM)
     */    
    public ModelAndView polis_all_distribution( HttpServletRequest request, HttpServletResponse response ) throws Exception{
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();
        
        List<DropDown> reportPathList = new ArrayList<DropDown>();
        
        String namaprod = "";	
        String lsdbsnumber = "";
        
    	reportPathList.add( new DropDown(
                props.getProperty( "report.bac.polis_all_distribution" ),
                "Report Distribusi berdasarkan Agen"
        	) );
        
    	report = new Report( "Report Distribution Polis berdasarkan Agen", reportPathList, Report.PDF, null );
    	
		report.addParamDefault("username", "username", 200, currentUser.getLus_full_name(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDefault("namaprod", "namaprod", 200, namaprod, false);
		report.addParamDefault("lsdbsnumber", "lsdbsnumber", 200, lsdbsnumber, false);
    	report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
    	return prepareReport( request, response, report );
    }    
    
    /**
     * @author Andhika
     * @since Dec 02, 2013 (11:30:00 AM)
     */    
    public ModelAndView report_berdasarkan_agen( HttpServletRequest request, HttpServletResponse response ) throws Exception{
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();
        
        List<DropDown> reportPathList = new ArrayList<DropDown>();
        	
        	reportPathList.add(new DropDown(props.getProperty("report.bac.report_by_agent"), "All"));
    		reportPathList.add(new DropDown(props.getProperty("report.bac.report_by_agent_agency"), "Agency"));
    		reportPathList.add(new DropDown(props.getProperty("report.bac.report_by_agent_bancass"), "Bancass"));
    		reportPathList.add(new DropDown(props.getProperty("report.bac.report_by_agent_fcd"), "FCD"));
    		reportPathList.add(new DropDown(props.getProperty("report.bac.report_by_agent_mnc"), "MNC"));
    		reportPathList.add(new DropDown(props.getProperty("report.bac.report_by_agent_mallassurance"), "Mallassurance"));
    		reportPathList.add(new DropDown(props.getProperty("report.bac.report_by_agent_worksite"), "Worksite"));
    		reportPathList.add(new DropDown(props.getProperty("report.bac.report_by_agent_regional"), "Regional"));
        
    	report = new Report( "Report Polis Berdasarkan Agen", reportPathList, Report.PDF, null );
    	
		report.addParamDefault("username", "username", 200, currentUser.getLus_full_name(), false);
    	report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
    	return prepareReport( request, response, report );
    }
}