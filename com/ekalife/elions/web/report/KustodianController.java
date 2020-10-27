package com.ekalife.elions.web.report;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.sql.SQLException;
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

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.jasper.Report;
import com.ekalife.utils.parent.ParentJasperReportingController;
import com.lowagie.text.pdf.PdfWriter;

public class KustodianController extends ParentJasperReportingController {

	DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
	
	//LAYAR UTAMA
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		//User currentUser = (User) request.getSession().getAttribute("currentUser");
		int jenis = ServletRequestUtils.getRequiredIntParameter(request, "jenis");
		
		List<String> daftarMenu = new ArrayList<String>();

		if(jenis == 1) { //new business
			daftarMenu.add("Laporan Kustodian New Business");
			daftarMenu.add("Summary Kustodian New Business");
			daftarMenu.add("Excellink Karyawan");
				List<DropDown> daftarExcellinkKaryawan = new ArrayList<DropDown>();
				daftarExcellinkKaryawan.add(new DropDown("New Business", props.getProperty("report.kustodian.excellink_karyawan.nb")));
				daftarExcellinkKaryawan.add(new DropDown("Successive", props.getProperty("report.kustodian.excellink_karyawan.succ")));
				map.put("daftarExcellinkKaryawan", daftarExcellinkKaryawan);
			daftarMenu.add("Surat Permintaan Alokasi Investasi"); //TODO (Yusuf) -> Cek Apakah ini juga dipakai di proses lanjutan-nya
		}else if(jenis == 0) { //transaksi TODO (Yusuf) -> BELUM DIKERJAKAN SEMUA
			daftarMenu.add("Laporan Kustodian Transaksi");
			daftarMenu.add("Summary Kustodian Transaksi");
			daftarMenu.add("Surat Transaksi");
			daftarMenu.add("Summary Transaksi Excellink");
			daftarMenu.add("Print Surat 6 Bulanan");
			daftarMenu.add("Excellink Karyawan");
		}else if(jenis == 2) { //successive TODO (Yusuf) -> BELUM DIKERJAKAN SEMUA
			daftarMenu.add("Laporan Kustodian Successive");
			daftarMenu.add("Summary Kustodian Successive");
			daftarMenu.add("Surat Successive");
			daftarMenu.add("Excellink Karyawan");
		}
		
		//viewer backup folder laporan dan surat
		String dirLaporan=props.getProperty("pdf.dir.kustodian")+"\\laporan";
		String dirSurat=props.getProperty("pdf.dir.kustodian")+"\\surat";
		
//		if(request.getparameter("srclap")!=null){		
//			list<dropdown> daftarfilelaporan = fileutils.listfilesindirectory(dirlaporan);
//			list<dropdown> daftarfilesurat = fileutils.listfilesindirectory(dirsurat);
//			map.put("tanggalfile",request.getparameter("tanggalfile"));
//			map.put("daftarfilelaporan", daftarfilelaporan);
//			map.put("daftarfilesurat", daftarfilesurat);
//		}
		
		map.put("dirLaporan", dirLaporan.replace("\\", "\\\\")+"\\\\");
		map.put("dirSurat", dirSurat.replace("\\", "\\\\")+"\\\\");
		
		map.put("daftarMenu", daftarMenu);
		map.put("now", new Date());
		map.put("mengetahui", props.getProperty("printing.namaUnderwriter"));
		//map.put("jenisInvest", elionsManager.selectLstJenisInvest());
		
		map.put("cutoff", props.getProperty("time.cutoff.kustodian"));
		
		return new ModelAndView("uw/kustodian_main", "cmd", map);
	}
	
	//PROSES TRANSFER
	public ModelAndView TransferKustodian(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] spaj = request.getParameterValues("pilihSpaj");
		if(spaj == null) {
			ServletOutputStream out = response.getOutputStream();
			out.println("<script>alert('Harap pilih minimal 1 nomor Polis / SPAJ.');window.close();</script>");
			out.flush();
			return null;
		}

		int posisi = ServletRequestUtils.getRequiredIntParameter(request, "lspd_id");
		List errors = this.elionsManager.transferKustodianToNabOrFilling(posisi, spaj);
		if(errors.size()>0) {
			return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
		}else {
			ServletOutputStream out = response.getOutputStream();
			out.println("<script>alert('Proses Transfer Berhasil.');window.close();</script>");
			out.flush();
		}
		return null;
	}
	
	//LAPORAN-LAPORAN
	public ModelAndView LaporanKustodianNewBusiness(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String jamdua=(request.getParameter("jamdua").equals("0")?"before":"after");
		String debKred=(request.getParameter("dk").equals("D")?"debet":"kredit");
		if(request.getParameter("laporan") != null) {
			if(request.getParameter("jenis").equals("kustodi")) {
				this.uwManager.updateStatusPrintUnitLink(42);

				String dk = request.getParameter("dk");
				int posisi = ServletRequestUtils.getRequiredIntParameter(request, "lspd_id");
				//String spaj[] = request.getParameterValues("pilihSpaj");
				
				List<Map> list = uwManager.selectDaftarSPAJUnitLink(42);
				List<String> listBaru = new ArrayList<String>();
				for(Map m : list) {
					listBaru.add((String) m.get("REG_SPAJ"));
				}
				
				Report report;
				report = new Report("Laporan Kustodian", props.getProperty("report.kustodian.laporan"), Report.PDF, null);
				report.addParamText("dk", "dk", 1, null, true);
				report.addParamText("kaki", "kaki", 200, null, false);
				//report.addParamText("tanggal", "tanggal", 50, null, false);
				List jam2=elionsManager.selectLaporanJamDua(listBaru, ServletRequestUtils.getRequiredIntParameter(request, "jamdua"));
				if(!jam2.isEmpty()){
					report.setResultList(uwManager.selectLaporanKustodian(dk,jam2 ));
				}else{
					report.setResultList(new ArrayList());
				}
				
				//backup file ke folder jika diprint..
				String uri = request.getRequestURI();
				String format = uri.substring(uri.lastIndexOf(".")+1);
				if(!format.equals("html")) {
					String outputFile="\\"+new SimpleDateFormat("ddMMyyyy").format(elionsManager.selectSysdate())+".pdf";
					String outputDir=props.getProperty("pdf.dir.kustodian")+"\\laporan\\kustodi\\"+jamdua+"\\"+debKred;
					//report.setBackupPdfPath(outputDir+outputFile);
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("reportPath", "/WEB-INF/classes/"+props.getProperty("report.kustodian.laporan"));
					params.put("dk", dk);
					params.put("kaki", request.getParameter("kaki"));
					params.put("tanggal", request.getParameter("tanggal"));
					JasperUtils.exportReportToPdf(
							props.getProperty("report.kustodian.laporan")+".jasper", 
							outputDir, 
							outputFile , 
							params, 
							report.getResultList(), 
							PdfWriter.AllowPrinting, null, null);
				}
				
				request.getSession().setAttribute("report", report);
				request.setAttribute("report", report);
				return prepareReport(request, response, report);
				
			}else if(request.getParameter("jenis").equals("finance")) {
				this.uwManager.updateStatusPrintUnitLink(42);

				String dk = request.getParameter("dk");
				int posisi = ServletRequestUtils.getRequiredIntParameter(request, "lspd_id");
				//String spaj[] = request.getParameterValues("pilihSpaj");
				
				List<Map> list = uwManager.selectDaftarSPAJUnitLink(42);
				List<String> listBaru = new ArrayList<String>();
				for(Map m : list) {
					listBaru.add((String) m.get("REG_SPAJ"));
				}
				
				Report report;
				report = new Report("Laporan Kustodian Finance", props.getProperty("report.kustodian.laporan.fin"), Report.PDF, null);
				report.addParamText("dk", "dk", 1, null, true);
				report.addParamText("kaki", "kaki", 200, null, false);
				report.addParamText("tanggal", "tanggal", 50, null, false);
				
				List jam2=elionsManager.selectLaporanJamDua(listBaru, ServletRequestUtils.getRequiredIntParameter(request, "jamdua"));
				if(!jam2.isEmpty()){
					report.setResultList(uwManager.selectLaporanKustodianFinance(dk, jam2));
				}else{
					report.setResultList(new ArrayList());
				}
				//backup file ke folder jika diprint..
				String uri = request.getRequestURI();
				String format = uri.substring(uri.lastIndexOf(".")+1);
				if(!format.equals("html")) {
					String outputFile="\\"+new SimpleDateFormat("ddMMyyyy").format(elionsManager.selectSysdate())+".pdf";
					String outputDir=props.getProperty("pdf.dir.kustodian")+"\\laporan\\finance\\"+jamdua+"\\"+debKred;
					//report.setBackupPdfPath(outputDir+outputFile);
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("reportPath", "/WEB-INF/classes/"+props.getProperty("report.kustodian.laporan.fin"));
					params.put("dk", dk);
					params.put("kaki", request.getParameter("kaki"));
					params.put("tanggal", request.getParameter("tanggal"));
					JasperUtils.exportReportToPdf(
							props.getProperty("report.kustodian.laporan.fin")+".jasper", 
							outputDir, 
							outputFile , 
							params, 
							report.getResultList(), 
							PdfWriter.AllowPrinting, null, null);
				}
				
				request.getSession().setAttribute("report", report);
				return prepareReport(request, response, report);
			}
		}else if(request.getParameter("transfer") != null) {
			return TransferKustodian(request, response);
		}
		
		return null;		
	}
	
	public ModelAndView laporan_kustodian(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report("Summary Kustodian", props.getProperty("report.kustodian.summary"), Report.PDF, null);
		report.addParamDate("Tanggal", "tgl", true, null, true);
		report.addParamText("Posisi", "lspd_id", 3, null, true);
		report.addParamText("dk", "dk", 1, null, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
		
	public ModelAndView excellink_karyawan(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy");
		int tahun = Integer.parseInt(df.format(elionsManager.selectSysdate()));
		Report report;
		report = new Report("Excellink Karyawan", (String) null, Report.PDF, null);
		report.addParamMonth("Tanggal", "tgl", false, tahun-30, tahun, true);
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}

	/**
	 * Surat Alokasi Investasi
	 * 
	 * @author Yusuf
	 * @since Sep 2, 2008 (8:56:08 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView alokasi_investasi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		int jamdua = ServletRequestUtils.getRequiredIntParameter(request, "jamdua");
		int repot = ServletRequestUtils.getRequiredIntParameter(request, "repot");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal","");
		String dist = ServletRequestUtils.getStringParameter(request, "dist", "");
		String reportPath = null;
		
		if(jamdua == 0) {
			if(repot == 0) {
				reportPath = props.getProperty("report.kustodian.surat_alokasi_investasi_sebelum14");
			}else if(repot == 1) {
				reportPath = props.getProperty("report.kustodian.surat_alokasi_investasi_ekalink_sebelum14");				
			}
		}else if(jamdua == 1) {
			if(repot == 0) {
				reportPath = props.getProperty("report.kustodian.surat_alokasi_investasi_sesudah14");
			}else if(repot == 1) {
				reportPath = props.getProperty("report.kustodian.surat_alokasi_investasi_ekalink_sesudah14");
			}
		}
		
		report = new Report("Surat Permintaan Alokasi Investasi", reportPath, Report.PDF, null);
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		report.addParamDefault("nama", "nama", 0, currentUser.getLus_full_name(), false);
		report.addParamDefault("dist", "dist", 0, dist, false);
		report.addParamDate("Tanggal", "tanggalAwal", false, new Date[] {new Date()}, true);
		report.addParamText("dk", "dk", 1, null, true);
		report.addParamText("mengetahui", "mengetahui", 100, request.getParameter("mengetahui"), true);
		
		if(tglAwal.equals(""))tglAwal=new SimpleDateFormat("ddMMyyyy").format(elionsManager.selectSysdate(1));
		
		//backup file ke folder jika diprint..
		String uri = request.getRequestURI();
		String format = uri.substring(uri.lastIndexOf(".")+1);
		if(format.equals("pdf")) {
			String jamduaa=(request.getParameter("jamdua").equals("0")?"before":"after");
			String debKred=(request.getParameter("dk").equals("D")?"debet":"kredit");
			String biasa=(request.getParameter("repot").equals("0")?"biasa":"ekalink");
			String outputFile="\\"+tglAwal.replace("/", "")+".pdf";
			String outputDir=props.getProperty("pdf.dir.kustodian")+"\\surat\\"+debKred+"\\"+jamduaa+"\\"+biasa;
			report.setBackupPdfPath(outputDir+outputFile);
//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("reportPath", "/WEB-INF/classes/"+reportPath);
//			params.put("dk", request.getParameter("dk"));
//			params.put("mengetahui", request.getParameter("mengetahui"));
//			params.put("tanggalAwal", request.getParameter("tanggal"));
//			params.put("nama", currentUser.getLus_full_name());
//			JasperUtils.exportReportToPdf(
//					reportPath+".jasper", 
//					outputDir, 
//					outputFile , 
//					params, 
//					report.getResultList(), 
//					PdfWriter.AllowPrinting, null, null);
		}
		
		request.getSession().setAttribute("report", report);
		return prepareReport(request, response, report);
	}
	
}