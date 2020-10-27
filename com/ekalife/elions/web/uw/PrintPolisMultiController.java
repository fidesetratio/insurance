package com.ekalife.elions.web.uw;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.AksesHist;
import com.ekalife.elions.model.CekValidPrintPolis;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.CommandUploadBac;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Endors;
import com.ekalife.elions.model.Insured;
import com.ekalife.elions.model.MstInboxHist;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Position;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.Simcard;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.elions.web.uw.support.WordingPdfViewer;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.EncryptUtils;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.MergePDF;
import com.ekalife.utils.PdfUtils;
import com.ekalife.utils.Products;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.jasper.JasperScriptlet;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentMultiController;
import com.ekalife.utils.view.PDFViewer;
import com.ibatis.common.resources.Resources;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import id.co.sinarmaslife.std.model.vo.DropDown;
/**
 * 
 * @author Yusuf
 * @since 20 April 2006
 * Class Utama Print Polis, berhubungan juga dengan PrintPolisPrintingController (dalam package yg sama)
 * Dimana PrintPolisPrintingController lah yang akhirnya melakukan proses generate PDF untuk di print
 * Sedangkan class ini lebih berfungsi untuk validasi, simpan history ke database, mengontrol alur di menu print polis, dll..  
 */
@SuppressWarnings("unchecked")
public class PrintPolisMultiController extends ParentMultiController{
	protected final Log logger = LogFactory.getLog( getClass() );
	DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
	
	//hati2 merubah nilai ini, ada yang di hard code ke printpolis_viewer.jsp dan printpolis.jsp
	public static final int ALL = -1;
	
	public static final int POLIS_DUPLEX = 0; //Polis + Manfaat
	public static final int POLIS_QUADRUPLEX = 1; //Polis + Manfaat + Surat Polis + Alokasi Dana
	public static final int POLIS = 2;
	public static final int MANFAAT = 3;
	public static final int SURAT_POLIS = 4;
	public static final int ALOKASI_DANA = 5;
	public static final int SERTIFIKAT_POWERSAVE = 6;
	public static final int TANDA_TERIMA_POLIS = 7;
	public static final int KARTU_PREMI = 8;
	public static final int SYARAT_UMUM_KHUSUS = 9;
	public static final int TUNGGAKAN_PREMI = 10; //BELUM DIBUAT
	public static final int SERTIFIKAT_GUTHRIE = 11; //KHUSUS PRODUK GUTHRIE
	public static final int SURAT_PERJANJIAN_HUTANG = 12;
	public static final int POLIS_DMTM = 13;
	public static final int SURAT_SIMCARD = 14;
	public static final int SURAT_BREAKABLE = 15;
	public static final int PANDUAN_VIRTUAL_ACCOUNT = 16;
	public static final int SYARAT_UMUM_KHUSUS_EAS = 17; //ini khusus untuk rudy, jadi gak perlu divalidasi, gak perlu javascript print
	public static final int ENDORSEMEN = 18;//KHUSUS RIDER SWINE FLU
	public static final int SYARAT_KHUSUS = 19; //ssk untuk swine flu khusus BSM saja
	public static final int ENDORS_EKASEHAT_ADMEDIKA = 20; // Endorsemen Polis + Petunjuk Penggunaan Kartu + List Provider
	public static final int POLIS_PAS = 21;
	public static final int TANDA_TERIMA_POLIS_DUPLIKAT = 22;
	public static final int KWITANSI = 23;
	public static final int POLIS_TERM = 24;
	public static final int POLIS_QUADRUPLEX_PLUS_HADIAH = 25;
	public static final int ENDORS_GMIT = 28;
	public static final int POLIS_QUADRUPLEX_MEDICAL_PLUS = 29;
	public static final int FINISH = 11;
	public static final int SURAT_PENJAMINAN_PROVIDER = 30;
	public static final int SERTIFIKAT_TERM_ROP = 31;
	public static final int GENERATE_ALL = 32;
	public static final int GENERATE_OUTSOURCE = 33;	
	
	/*
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
	*/
	
	public ModelAndView info(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map m = new HashMap();
		User user = (User) request.getSession().getAttribute("currentUser");
		ArrayList<HashMap> datapolis = new ArrayList<HashMap>();
		
		if(user.getJn_bank()==2 || user.getJn_bank()==16){
			datapolis = bacManager.selectDaftarPolisBSMBelumPrint(user.getLus_id());
			m.put("datapolis", datapolis);
		}
		
		return new ModelAndView("uw/printpolis/printpolis_info", m);
	}
	
	public ModelAndView otorisasi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map m = new HashMap();
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		m.put("user_id", lus_id);
		
		String cab_bank = "";
		Integer jn_bank = -1;
		Map data_valid = (HashMap) this.elionsManager.select_validbank(lus_id);
		if(data_valid != null){
			cab_bank = (String) data_valid.get("CAB_BANK");
			jn_bank = (Integer) data_valid.get("JN_BANK");
		}
		
		if(cab_bank == null){
			cab_bank = "";
		}
		m.put("cabang_bank", cab_bank);
		m.put("jn_bank", jn_bank);
		
		Integer dari = ServletRequestUtils.getIntParameter(request, "dari", 1);
		Integer sampai = ServletRequestUtils.getIntParameter(request, "sampai", 30);
		String kata = ServletRequestUtils.getStringParameter(request, "kata", "");
		String tipe = ServletRequestUtils.getStringParameter(request, "tipe", "");
		String pilter = ServletRequestUtils.getStringParameter(request, "pilter", "");
		Integer tambah = 0; 
	
		if("prev".equals(ServletRequestUtils.getStringParameter(request, "action", ""))){
			if(dari > 30) tambah = -30;
		}else if("next".equals(ServletRequestUtils.getStringParameter(request, "action", ""))) {
			tambah = 30;
		}
		dari += tambah;
		sampai += tambah;
		
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List<Policy> daftarPolis = ("SSS".equals(currentUser.getCab_bank().trim()) || currentUser.getLde_id().equals("11")) ? 
				elionsManager.selectDaftarPolisOtorisasiUwSimasPrima(dari, sampai) : 
				elionsManager.selectDaftarPolisOtorisasiBankSinarmas(Integer.valueOf(currentUser.getLus_id()), dari, sampai);
		m.put("daftarPolis", daftarPolis);
		Integer myself = 1;
		myself += tambah;
		Integer page = sampai/30;
		
		m.put("myself", myself);
		m.put("page", page);
		
		if(request.getParameter("search")!=null){
			if("SSS".equals(user.getCab_bank().trim()) || user.getJn_bank().intValue() == 3) {
				if(kata==null){
//					if(user.getJn_bank().intValue() == 3){
//						if(user.getFlag_approve().intValue()==1){
//							daftarPolis = 
//								elionsManager.selectDaftarPolisOtorisasiSekuritas(Integer.valueOf(currentUser.getLus_id()), dari, sampai);
//						}
//					}else{
//						daftarPolis = 
//							("SSS".equals(currentUser.getCab_bank().trim()) || currentUser.getLde_id().equals("11")) ? 
//									elionsManager.selectDaftarPolisOtorisasiUwSimasPrima(dari, sampai) : 
//										elionsManager.selectDaftarPolisOtorisasiBankSinarmas(Integer.valueOf(currentUser.getLus_id()), dari, sampai);
//					}
					daftarPolis = ("SSS".equals(currentUser.getCab_bank().trim()) || currentUser.getLde_id().equals("11")) ? 
							elionsManager.selectDaftarPolisOtorisasiUwSimasPrima(dari, sampai) : 
							elionsManager.selectDaftarPolisOtorisasiBankSinarmas(Integer.valueOf(currentUser.getLus_id()), dari, sampai);
					m.put("daftarPolis", daftarPolis);
				}else
					m.put("daftarPolis", elionsManager.selectFilterOtorisasi(tipe, kata, pilter,jn_bank));
			}else{
				if(kata==null){
					daftarPolis = 
						("SSS".equals(currentUser.getCab_bank().trim()) || currentUser.getLde_id().equals("11")) ? 
								elionsManager.selectDaftarPolisOtorisasiUwSimasPrima(dari, sampai) : 
									elionsManager.selectDaftarPolisOtorisasiBankSinarmas(Integer.valueOf(currentUser.getLus_id()), dari, sampai);
					m.put("daftarPolis", daftarPolis);
				}else
					m.put("daftarPolis", 
							elionsManager.selectFilterOtorisasi(tipe, kata, pilter,jn_bank));
			}
		}
		
		if(request.getParameter("save") != null) {
			String alasan = ServletRequestUtils.getStringParameter(request, "alasan", "");
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			if(alasan.equals("") || spaj.equals("")) {
				m.put("pesanError", "Harap Pilih Nomor Register dan Alasan Pencetakan Ulang untuk melanjutkan.");
			}else {
				elionsManager.updateOtorisasiBankSinarmas(spaj, alasan, currentUser);
			}
		}
		
		m.put("dari", dari);
		m.put("sampai", sampai);
		
		return new ModelAndView("uw/printpolis/printpolis_otorisasi", m);
	}
	
	public ModelAndView alasan(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Command command = new Command();
		BindException errors;
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		command.setSpaj(ServletRequestUtils.getRequiredStringParameter(request, "spaj"));
		command.setPosition(new Position());
		command.getPosition().setMsps_date(new Date());
		command.getPosition().setLus_login_name(currentUser.getName());
		command.getPosition().setReg_spaj(command.getSpaj());
		
		if(request.getParameter("save") != null) {
			errors = new BindException(bindAndValidate(request, command, true));
			if(!errors.hasErrors()) {
				command.getPosition().setLspd_id(6);
				command.getPosition().setLssp_id(1);
				command.getPosition().setLus_id(currentUser.getLus_id());
				elionsManager.insertMstPositionSpaj(command.getPosition().getLus_id(), command.getPosition().getMsps_desc(), command.getPosition().getReg_spaj(),0);
				logger.info("berhasil");
			}
		} else {
			errors = new BindException(bindAndValidate(request, command, false));
		}

		command.setDaftarPosisi(uwManager.selectAlasanPendingPrintPolis(command.getSpaj()));
		
		return new ModelAndView("uw/printpolis/printpolis_alasan", errors.getModel());

	}
	
	//kirim softcopy polis
	public ModelAndView email(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");

		CommandUploadBac uploader = new CommandUploadBac();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(uploader);
		binder.bind(request);
		uploader.setErrorMessages(new ArrayList<String>());

		if(request.getParameter("kirim") != null || request.getParameter("test") != null) {
			String jenisKirim = null;
			if(request.getParameter("kirim") != null) jenisKirim = "kirim";
			else if(request.getParameter("test") != null) jenisKirim = "test";
			
			Simcard simcard = uwManager.selectSimasCardBySpaj(uploader.getSpaj());
			List daftarSebelumnya = uwManager.selectSimasCard(uploader.getSpaj());
			List isAgen = uwManager.selectIsSimasCardClientAnAgent(uploader.getSpaj()); 
			
			if(simcard==null && daftarSebelumnya.isEmpty() && isAgen.isEmpty()){
//				uploader.getErrorMessages().add("Silakan Menginput Simas Card Terlebih dahulu");
			}
			
			//VALIDASI TAMBAHAN : DIAKSES CABANG
			if(!currentUser.getLde_id().equals("11") && elionsManager.selectPrintCabangAtauPusat(uploader.getSpaj())==1){
				if(!Common.isEmpty(currentUser.getCab_bank())){
					//Mencegah apabila BSM/SEKURITAS kebuka akses ini, usernya tidak dapat akses.
					uploader.getErrorMessages().add("Maaf, Anda tidak memiliki hak akses untuk fasilitas ini");
				}else{
					int count= elionsManager.selectCekPrintUlang(uploader.getSpaj(), "VALID PENGIRIMAN SOFTCOPY");
					if(count<1){
						uploader.getErrorMessages().add("Maaf, Anda tidak dapat melakukan Pengiriman Softcopy Karena Belum diVALID oleh U/W.Silakan hubungi dept U/W.");
					}
				}
			}
			
			//VALIDASI 0: CC, SUBJECT, DAN MESSAGE HARUS VALID
			if(uploader.getEmailsubject().trim().equals("") || uploader.getEmailmessage().trim().equals("")) {
				uploader.getErrorMessages().add("Silahkan lengkapi e-mail yang akan dikirim.");
			}
			if(jenisKirim.equals("test")) if (uploader.getEmailcc().trim().equals("")) {
				uploader.getErrorMessages().add("Untuk test kirim, anda harus memasukkan minimal 1 email di kolom CC");
			}
			if(!uploader.getEmailcc().trim().equals("")) {
				for(String cc : uploader.getEmailcc().split(";")) {
					if(!cc.toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
						uploader.getErrorMessages().add("Silahkan isi e-mail yang benar pada kolom CC");
						break;
					}
				}
			}

			//VALIDASI 1: EKA.MST_POLICY.MSPO_JENIS_TERBIT HARUS = 1
//			if(elionsManager.selectJenisTerbitPolis(uploader.getSpaj())!=1) {
//				uploader.getErrorMessages().add("Polis ini harus diterbitkan dalam bentuk HARDCOPY.");
//			}
			
			if(jenisKirim.equals("kirim")) {
				//VALIDASI 2: EKA.MST_INSURED.MSTE_TGL_KIRIM_POLIS HARUS NULL (BELUM PERNAH KIRIM)
				Map map =this.elionsManager.validationPrintPolis(uploader.getSpaj()); 
				if(map.get("MSTE_TGL_KIRIM_POLIS")!=null) {
					// ini di disable
					//uploader.getErrorMessages().add("Softcopy polis ini sudah pernah dikirim.");
				}
				//VALIDASI 3: EMAIL NASABAH HARUS VALID
				int emailValid=0;
				if(!uploader.getEmailto().trim().equals("")) {
					if(uploader.getEmailto().toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
						emailValid=2;
					}
				}else emailValid=1;
				if(emailValid == 0) uploader.getErrorMessages().add("Alamat email nasabah [" + email + "] tidak valid. Silahkan konfirmasi dengan ITwebandmobile@sinarmasmsiglife.co.id");
				else if(emailValid == 1) uploader.getErrorMessages().add("Nasabah tersebut tidak mempunyai alamat e-mail. Silahkan konfirmasi dengan ITwebandmobile@sinarmasmsiglife.co.id");
			}
			//VALIDASI 4: BEA METERAI HARUS ADA SALDONYA SEGALA
			//String validasiMeterai = uwManager.validasiBeaMeterai();
			//if(validasiMeterai != null) uploader.getErrorMessages().add(validasiMeterai);			
			
			//KALO VALIDASI LEWAT SEMUA, BARU KIRIM
			if(uploader.getErrorMessages().isEmpty()) {
				
				//HAPUS FILE SEBELUMNYA
				File dir = new File(props.getProperty("pdf.dir.export")+"\\"+
						elionsManager.selectCabangFromSpaj(uploader.getSpaj())+"\\"+uploader.getSpaj()+"\\"+"polis_utama.pdf");
				if(dir.exists()) {
					dir.delete();
				}

				if(uwManager.selectJenisTerbitPolis(uploader.getSpaj())==0) {
					elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "KIRIM SOFTCOPY POLIS (E-LIONS) KE "+uploader.getEmailto(), uploader.getSpaj(),0);
				}
				
				if(jenisKirim.equals("kirim") && uwManager.selectJenisTerbitPolis(uploader.getSpaj())==1 ) {
					//update dulu datanya, biar tanggal kirim polisnya gak kosong
					this.elionsManager.updatePolicyAfterSendSoftcopy(uploader.getSpaj(), currentUser.getLus_id(), 6, 1);
				}

				//UNTUK POLIS DM/TM, yang diemail softcopy nya beda
				List detBisnis = elionsManager.selectDetailBisnis(uploader.getSpaj());
				String elesbees = (String) ((Map) detBisnis.get(0)).get("BISNIS");
				String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
				
				boolean polis=false, manfaat=false, surat=false, alokasi=false, sertifikat=false, tandaterima=false, kartu=false, ssu=false;
				
		        //1. GENERATE DULU FILE-NYA
				//khusus DM/TM
// Andhika (03/06/2013)				
//				if(elesbees.equals("142") && lsdbs.equals("008")) {
//					polis 		= dokumen(uploader.getSpaj(), props.getProperty("pdf.polis_dmtm"), request, response);
//					surat		= dokumen(uploader.getSpaj(), props.getProperty("pdf.surat_polis"), request, response);
//
//					if(!polis) uploader.getErrorMessages().add("Dokumen Polis DM/TM tidak berhasil dihasilkan. Silahkan hubungi EDP.");
//					if(!surat) uploader.getErrorMessages().add("Dokumen Surat Ucapan DM/TM tidak berhasil dihasilkan. Silahkan hubungi EDP.");
//				//polis lainnya
//				}else
				if(elesbees.equals("164") && (lsdbs.equals("001") || lsdbs.equals("002") || lsdbs.equals("008") || lsdbs.equals("012"))){
					surat 		= dokumen(uploader.getSpaj(), props.getProperty("pdf.surat_polis"), request, response);
					alokasi 	= dokumen(uploader.getSpaj(), props.getProperty("pdf.alokasi_dana"), request, response);
					sertifikat 	= dokumen(uploader.getSpaj(), props.getProperty("pdf.sertifikat_powersave"), request, response);
					tandaterima = dokumen(uploader.getSpaj(), props.getProperty("pdf.tanda_terima_polis"), request, response);
					//kartu 		= dokumen(uploader.getSpaj(), props.getProperty("pdf.kartu_premi"), request, response);
					ssu 		= dokumen(uploader.getSpaj(), props.getProperty("pdf.ssu"), request, response);				

					if(!surat) 			uploader.getErrorMessages().add("Dokumen Surat Ucapan Terima kasih tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					if(!alokasi) 		uploader.getErrorMessages().add("Dokumen Surat Alokasi Dana tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					if(!sertifikat) 	uploader.getErrorMessages().add("Dokumen Sertifikat Powersave/Stable Link tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					if(!tandaterima)	uploader.getErrorMessages().add("Dokumen Tanda Terima Polis tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					//if(!kartu) 			uploader.getErrorMessages().add("Dokumen Kartu Premi tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					if(!ssu) 			uploader.getErrorMessages().add("Dokumen Syarat-syarat Polis tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
				}else if(elesbees.equals("186") && (lsdbs.equals("001") || lsdbs.equals("003"))){
					surat 		= dokumen(uploader.getSpaj(), props.getProperty("pdf.surat_polis"), request, response);
					alokasi 	= dokumen(uploader.getSpaj(), props.getProperty("pdf.alokasi_dana"), request, response);
					sertifikat 	= dokumen(uploader.getSpaj(), props.getProperty("pdf.sertifikat_powersave"), request, response);
					tandaterima = dokumen(uploader.getSpaj(), props.getProperty("pdf.tanda_terima_polis"), request, response);
					//kartu 		= dokumen(uploader.getSpaj(), props.getProperty("pdf.kartu_premi"), request, response);
					ssu 		= dokumen(uploader.getSpaj(), props.getProperty("pdf.ssu"), request, response);
					
					if(!surat) 			uploader.getErrorMessages().add("Dokumen Surat Ucapan Terima kasih tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					if(!alokasi) 		uploader.getErrorMessages().add("Dokumen Surat Alokasi Dana tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					if(!sertifikat) 	uploader.getErrorMessages().add("Dokumen Sertifikat Powersave/Stable Link tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					if(!tandaterima)	uploader.getErrorMessages().add("Dokumen Tanda Terima Polis tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					//if(!kartu) 			uploader.getErrorMessages().add("Dokumen Kartu Premi tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					if(!ssu) 			uploader.getErrorMessages().add("Dokumen Syarat-syarat Polis tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					
				}else if(elesbees.equals("187")){
					polis 		= dokumen(uploader.getSpaj(), props.getProperty("pdf.polis_pas"), request, response);
					tandaterima = dokumen(uploader.getSpaj(), props.getProperty("pdf.tanda_terima_polis"), request, response);
					surat 		= dokumen(uploader.getSpaj(), props.getProperty("pdf.surat_polis"), request, response);
					
					if(!polis) uploader.getErrorMessages().add("Dokumen Polis PAS tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					if(!tandaterima)	uploader.getErrorMessages().add("Dokumen Tanda Terima Polis tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					if(!surat) 			uploader.getErrorMessages().add("Dokumen Surat Ucapan Terima kasih tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
				}else if(elesbees.equals("196")){
					polis 		= dokumen(uploader.getSpaj(), props.getProperty("pdf.polis_term"), request, response);
					tandaterima = dokumen(uploader.getSpaj(), props.getProperty("pdf.tanda_terima_polis"), request, response);
					surat 		= dokumen(uploader.getSpaj(), props.getProperty("pdf.surat_polis"), request, response);
					
					if(!polis) uploader.getErrorMessages().add("Dokumen Polis PAS tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					if(!tandaterima)	uploader.getErrorMessages().add("Dokumen Tanda Terima Polis tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					if(!surat) 			uploader.getErrorMessages().add("Dokumen Surat Ucapan Terima kasih tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
				}else {
					polis 		= dokumen(uploader.getSpaj(), props.getProperty("pdf.polis"), request, response);
					manfaat 	= dokumen(uploader.getSpaj(), props.getProperty("pdf.manfaat"), request, response);
					surat 		= dokumen(uploader.getSpaj(), props.getProperty("pdf.surat_polis"), request, response);
					alokasi 	= dokumen(uploader.getSpaj(), props.getProperty("pdf.alokasi_dana"), request, response);
					//sertifikat 	= dokumen(uploader.getSpaj(), props.getProperty("pdf.sertifikat_powersave"), request, response);
					tandaterima = dokumen(uploader.getSpaj(), props.getProperty("pdf.tanda_terima_polis"), request, response);
					//kartu 		= dokumen(uploader.getSpaj(), props.getProperty("pdf.kartu_premi"), request, response);
					ssu 		= dokumen(uploader.getSpaj(), props.getProperty("pdf.ssu"), request, response);				

					if(!polis) 			uploader.getErrorMessages().add("Dokumen Polis tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					if(!manfaat) 		uploader.getErrorMessages().add("Dokumen Manfaat Polis tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					if(!surat) 			uploader.getErrorMessages().add("Dokumen Surat Ucapan Terima kasih tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					if(!alokasi) 		uploader.getErrorMessages().add("Dokumen Surat Alokasi Dana tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					//if(!sertifikat) 	uploader.getErrorMessages().add("Dokumen Sertifikat Powersave/Stable Link tidak berhasil dihasilkan. Silahkan hubungi EDP.");
					if(!tandaterima)	uploader.getErrorMessages().add("Dokumen Tanda Terima Polis tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
					//if(!kartu) 			uploader.getErrorMessages().add("Dokumen Kartu Premi tidak berhasil dihasilkan. Silahkan hubungi EDP.");
					if(!ssu) 			uploader.getErrorMessages().add("Dokumen Syarat-syarat Polis tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
				}
				
				if(uploader.getErrorMessages().isEmpty()) {
					
					//2. GABUNG PDF NYA MASUKKIN KE ATTACHMENT
					String cabang = elionsManager.selectCabangFromSpaj(uploader.getSpaj());
			        String userDir = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+uploader.getSpaj()+"\\";
	
					List<DropDown> fileList = new ArrayList<DropDown>();
					//List<DropDown> fileList2 = new ArrayList<DropDown>();

// Andhika (03/06/2013)
//					if(elesbees.equals("142") && lsdbs.equals("008")) {
//						//fileList.add(new DropDown(PdfUtils.createFirstPage(props, userDir), props.getProperty("company.name")));
//						fileList.add(new DropDown(userDir+props.getProperty("pdf.surat_polis")+".pdf", "Surat Ucapan Terima Kasih", "NONE"));
//						fileList.add(new DropDown(userDir+props.getProperty("pdf.polis_dmtm")+".pdf", "Cover Polis", "NONE"));
//					}else 
					if(elesbees.equals("164") && (lsdbs.equals("001") ||lsdbs.equals("002") || lsdbs.equals("008"))){
						fileList.add(new DropDown(userDir+props.getProperty("pdf.surat_polis")+".pdf", "Surat Ucapan Terima Kasih", "ALL"));
						fileList.add(new DropDown(userDir+props.getProperty("pdf.alokasi_dana")+".pdf", "Laporan Alokasi Dana", "ALL"));
						if(lsdbs.equals("001")){
							fileList.add(new DropDown(userDir+props.getProperty("pdf.sertifikat_powersave")+".pdf", "Sertifikat Stable Link", "ALL"));
						}else{
							fileList.add(new DropDown(userDir+props.getProperty("pdf.sertifikat_powersave")+".pdf", "Sertifikat Simas Stabil Link", "ALL"));
						}
						fileList.add(new DropDown(userDir+props.getProperty("pdf.ssu")+".pdf", "Syarat-syarat Polis", "FIRST_PAGE_ONLY"));
					}else if(elesbees.equals("186") && (lsdbs.equals("001") || lsdbs.equals("003"))){
						fileList.add(new DropDown(userDir+props.getProperty("pdf.surat_polis")+".pdf", "Surat Ucapan Terima Kasih", "ALL"));
						fileList.add(new DropDown(userDir+props.getProperty("pdf.alokasi_dana")+".pdf", "Laporan Alokasi Dana", "ALL"));
						if(lsdbs.equals("001")){
							fileList.add(new DropDown(userDir+props.getProperty("pdf.sertifikat_powersave")+".pdf", "Sertifikat Progressive Link", "ALL"));
						}else{
							fileList.add(new DropDown(userDir+props.getProperty("pdf.sertifikat_powersave")+".pdf", "Sertifikat Progressive Link BSM", "ALL"));
						}
						fileList.add(new DropDown(userDir+props.getProperty("pdf.ssu")+".pdf", "Syarat-syarat Polis", "FIRST_PAGE_ONLY"));
					}else if(elesbees.equals("187")){
						fileList.add(new DropDown(userDir+props.getProperty("pdf.polis_pas")+".pdf", "Cover Polis", "NONE"));
						fileList.add(new DropDown(userDir+props.getProperty("pdf.surat_polis")+".pdf", "Surat Ucapan Terima Kasih", "NONE"));
					}else if(elesbees.equals("196")){
						fileList.add(new DropDown(userDir+props.getProperty("pdf.polis_term")+".pdf", "Cover Polis", "NONE"));
						fileList.add(new DropDown(userDir+props.getProperty("pdf.surat_polis")+".pdf", "Surat Ucapan Terima Kasih", "NONE"));
					}else {
						//fileList.add(new DropDown(PdfUtils.createFirstPage(props, userDir), props.getProperty("company.name")));
						fileList.add(new DropDown(userDir+props.getProperty("pdf.surat_polis")+".pdf", "Surat Ucapan Terima Kasih", "ALL"));
						fileList.add(new DropDown(userDir+props.getProperty("pdf.polis")+".pdf", "Cover Polis", "ALL"));
						fileList.add(new DropDown(userDir+props.getProperty("pdf.manfaat")+".pdf", "Manfaat Polis", "ALL"));
						fileList.add(new DropDown(userDir+props.getProperty("pdf.alokasi_dana")+".pdf", "Laporan Alokasi Dana", "ALL"));
						//fileList.add(new DropDown(userDir+props.getProperty("pdf.sertifikat_powersave")+".pdf", "Sertifikat Powersave/Stable Link", "ALL"));
						fileList.add(new DropDown(userDir+props.getProperty("pdf.ssu")+".pdf", "Syarat-syarat Polis", "FIRST_PAGE_ONLY"));
					}
			        
					//kalau ada upload file dokumen tambahan, save file nya
					File file1 = new File(userDir+"softcopy_attachment_1.pdf");
					File file2 = new File(userDir+"softcopy_attachment_2.pdf");
					File file3 = new File(userDir+"softcopy_attachment_3.pdf");
					
					if(!uploader.getFile1().isEmpty()) {
						uploader.getFile1().transferTo(file1);
					}
					if(!uploader.getFile2().isEmpty()) {
						uploader.getFile2().transferTo(file2);
					}
					if(!uploader.getFile3().isEmpty()) {
						uploader.getFile3().transferTo(file3);
					}

					if(file1.exists()) fileList.add(new DropDown(userDir+"softcopy_attachment_1.pdf", request.getParameter("namafile1"), "NONE"));
					if(file2.exists()) fileList.add(new DropDown(userDir+"softcopy_attachment_2.pdf", request.getParameter("namafile2"), "NONE"));
					if(file3.exists()) fileList.add(new DropDown(userDir+"softcopy_attachment_3.pdf", request.getParameter("namafile3"), "NONE"));					
					
					//TODO Yusuf - file ini gak perlu kan?
					//fileList.add(new DropDown(TANDA_TERIMA_POLIS+".pdf", userDir+TANDA_TERIMA_POLIS+".pdf"));
					//fileList.add(new DropDown(KARTU_PREMI+".pdf", userDir+KARTU_PREMI+".pdf"));
					
					//req bu titis logo syariah
					if (products.syariah(elesbees, lsdbs)){
						PdfUtils.addTableOfContentsSyariah(fileList, props, userDir);
					}
					else{
						PdfUtils.addTableOfContents(fileList, props, userDir);
					}
					
					//String userPassword = props.getProperty("pdf.userPassword");
					String userPassword = defaultDateFormatStripes.format(uwManager.selectTanggalLahirPemegang(uploader.getSpaj()));
					File softcopy = PdfUtils.combinePdfWithOutline(fileList, userDir, "softcopy.pdf", props, userPassword);
					if (products.syariah(elesbees, lsdbs)){
						softcopy = PdfUtils.addBarcodeAndLogoSyariah(fileList, softcopy, props, userPassword);
					}else{
						softcopy = PdfUtils.addBarcodeAndLogo(fileList, softcopy, props, userPassword);
					}
					//File dokumenlain = PdfUtils.combinePdfWithOutline(fileList2, userDir, "dokumen.pdf", props, userPassword);
					
					List<File> attachments = new ArrayList<File>();
					attachments.add(softcopy);
					//attachments.add(dokumenlain);
					
					if(jenisKirim.equals("kirim")) {

						try {
							if(products.unitLink(uwManager.selectBusinessId(uploader.getSpaj())) 
									&& uwManager.selectJenisTerbitPolis(uploader.getSpaj())==1) {
								this.elionsManager.updateUlink(3, uploader.getSpaj(), defaultDateFormatReversed.format(this.elionsManager.selectMuTglTrans(uploader.getSpaj())));
							}
							
//							if(uploader.getEmailcc().trim().equals("")) {
//								uploader.setEmailcc("policy_service@sinarmasmsiglife.co.id");
//							}
							
							//
							//logger.info(uploader.getEmailto().replaceAll(" ", ""));
							//logger.info(uploader.getEmailcc().replaceAll(" ", ""));
							//logger.info(currentUser.getEmail());
							String bcc = null;
							bcc = "adrian_n@sinarmasmsiglife.co.id"; //buat ngecekin Softcopy-polis kekirim ke nasah atau tidak
							
							HashMap em = bacManager.selectMstConfig(6, "prosesEmailSoftcopyPolis", "prosesEmailSoftcopyPolis");
					        bcc = em.get("NAME")!=null?em.get("NAME").toString():null;
								
							//1. kirim yang asli ke nasabah dan agen
							EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
									null, 0, 0, new Date(), null, true,
									"policy_service@sinarmasmsiglife.co.id", 
									uploader.getEmailto().replaceAll(" ", "").split(";"), 
									(uploader.getEmailcc().trim().equals("")? null : uploader.getEmailcc().replaceAll(" ", "").split(";")), 
									new String[] {currentUser.getEmail(),bcc},
//									new String[]{"deddy@sinarmasmsiglife.co.id"},null,null,
									uploader.getEmailsubject(), uploader.getEmailmessage(), attachments, uploader.getSpaj());
									//"ajsjava@sinarmasmsiglife.co.id", new String[] {"deddy@sinarmasmsiglife.co.id", "d1m3nt0r_885@yahoo.com"}, new String[] {"deddy@sinarmasmsiglife.co.id", "d1m3nt0r_885@yahoo.com"}, new String[] {"deddy@sinarmasmsiglife.co.id", "d1m3nt0r_885@yahoo.com"},			
							//"ajsjava@sinarmasmsiglife.co.id", new String[] {props.getProperty("admin.yusuf"), "yusufsutarko@gmail.com"}, new String[] {props.getProperty("admin.yusuf"), "yusufsutarko@gmail.com"}, new String[] {props.getProperty("admin.yusuf"), "yusufsutarko@gmail.com"},
							//"policy_service@sinarmasmsiglife.co.id", new String[] {props.getProperty("admin.yusuf"), "yusufsutarko@gmail.com"}, new String[] {props.getProperty("admin.yusuf"), "yusufsutarko@gmail.com"}, new String[] {props.getProperty("admin.yusuf"), "yusufsutarko@gmail.com"},							
							//uploader.getEmailsubject(), uploader.getEmailmessage(), attachments);
							//2. kirim tanpa attachment ke orang2 ini
//							String emailcc2 = request.getParameter("emailcc2");
//							if(emailcc2 == null) emailcc2 = "";
//							
//							email.sendWithAttachments(
//									"policy_service@sinarmasmsiglife.co.id", (emailcc2.trim().equals("")? null : emailcc2.split(";")), null,
//									uploader.getEmailsubject(), uploader.getEmailmessage(), null);
						}catch(Exception e) {
							logger.error("ERROR :", e);
							throw e;
						}
						
					}
				
					request.setAttribute("success", "SPAJ dengan nomor "+uploader.getSpaj()+" berhasil di e-mail ke " + uploader.getEmailto());
				}
			}
			
		} else {
			Map info = uwManager.selectInformasiEmailSoftcopy(uploader.getSpaj());
			
			//penambahan telemarketing@banksinarmas.com di TO email (dmtm) - (101623)
			if(elionsManager.selectLcaIdBySpaj(uploader.getSpaj()).equals("40")){
				if ((String) info.get("MSPE_EMAIL")==null){
					uploader.setEmailto("telemarketing@banksinarmas.com");
				}else{
					uploader.setEmailto("telemarketing@banksinarmas.com;"+(String) info.get("MSPE_EMAIL"));
				}
			}else{
				uploader.setEmailto((String) info.get("MSPE_EMAIL"));
			}
			
			uploader.setEmailsubject("Softcopy Polis Asuransi atas nama " + info.get("GELAR") + " " + info.get("MCL_FIRST") + " " + uploader.getSpaj() + "/" + info.get("MSPO_POLICY_NO"));
			StringBuffer pesan = new StringBuffer();
			String kalimatPertama = "";
			
			List detBisnis = elionsManager.selectDetailBisnis(uploader.getSpaj());
			String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
			String lsdbsName = (String) ((Map) detBisnis.get(0)).get("LSDBS_NAME");
			
			if(uwManager.selectJenisTerbitPolis(uploader.getSpaj())==1) { //softcopy
				
				String link = 
					"http://www.sinarmasmsiglife.co.id/E-Policy/common/confirm.htm?spaj="+uploader.getSpaj()+"&auth="+ EncryptUtils.encode(("yusufsutarko"+uploader.getSpaj()).getBytes());
				
				//dm/tm
				if(lsbs.equals("142") && lsdbs.equals("008")) {
					kalimatPertama = "Bersama ini kami sampaikan Polis Asuransi sesuai dengan Permintaan Asuransi yang Anda sampaikan melalui telepon kepada telemarketing kami. Polis asuransi ini kami kirimkan dalam bentuk Softcopy.\n\n"; 
				}else {
					kalimatPertama = "Sesuai dengan permintaan Anda, dengan ini kami sampaikan Polis Softcopy asuransi beserta lampirannya.\n\n";
				}

				pesan.append("Kepada Yth.\n");
				pesan.append(info.get("GELAR") + " " + info.get("MCL_FIRST")+"\n");
				pesan.append("di tempat\n\n");
				pesan.append("Dengan Hormat,\n");
				pesan.append("Bersama ini kami kirimkan softcopy Polis " + lsdbsName + " untuk Nomor Polis. " + info.get("MSPO_POLICY_NO_FORMAT")+"\n");
				pesan.append("dengan password : tanggal lahir format dd-mm-yyyy \n");
				pesan.append("Informasi lebih lanjut dapat disampaikan ke : " + "cs@sinarmasmsiglife.co.id");
//				pesan.append(kalimatPertama);
//				pesan.append("Untuk menjaga keamanan polis Anda, Polis Softcopy Anda sudah kami lindungi dengan password berupa tanggal lahir Anda dalam format dd-mm-yyyy (contoh: 14-04-1985)\n");
//				pesan.append("Mohon cetak polis nomor <strong>" + info.get("MSPO_POLICY_NO_FORMAT") + "</strong> terlampir untuk keperluan klaim Anda.\n\n");
//				pesan.append("Apabila Anda sudah menerima, membaca dan memahami isi dari Polis Softcopy asuransi nomor "+
//						"<strong>" + info.get("MSPO_POLICY_NO_FORMAT") + "</strong> ini, mohon sampaikan konfirmasi persetujuan Anda melalui website kami di "+
//						"<a href='"+link+"'>"+link+"</a>" +
//						"\nApabila dalam waktu 21 hari sejak tanggal pengiriman Polis Softcopy ini kami belum menerima konfirmasi tersebut, "+
//						"maka kami anggap Polis telah diterima dan disetujui.\n\n"+
//						"Mohon Polis Softcopy ini disimpan dan dicetak apabila diperlukan.\n\n");
//				pesan.append("Hormat kami, \n");
//				pesan.append("PT Asuransi Jiwa Sinarmas MSIG Tbk.");
			}else { //hardcopy

				//dm/tm
				if(lsbs.equals("142") && lsdbs.equals("008")) {
					kalimatPertama = "Bersama ini kami sampaikan Polis Asuransi sesuai dengan Permintaan Asuransi yang Anda sampaikan melalui telepon kepada telemarketing kami. Polis asuransi ini kami kirimkan dalam bentuk Softcopy dan Hardcopy. Polis Hardcopy akan dikirimkan melalui pos tercatat ke alamat Anda.\n\n"; 
				}else {
					kalimatPertama = "Bersama ini kami sampaikan Polis Asuransi sesuai dengan Surat Permintaan Asuransi yang Anda sampaikan. Polis asuransi ini kami kirimkan dalam bentuk Softcopy dan Hardcopy. Polis Hardcopy akan disampaikan oleh Marketing kami atau expedisi.\n\n";
				}

				pesan.append("Kepada Yth.\n");
				pesan.append(info.get("GELAR") + " " + info.get("MCL_FIRST")+"\n");
				pesan.append("di tempat\n\n");
				pesan.append("Dengan Hormat,\n");
				pesan.append("Bersama ini kami kirimkan softcopy Polis " + lsdbsName + " untuk Nomor Polis. " + info.get("MSPO_POLICY_NO_FORMAT")+"\n");
				pesan.append("dengan password : tanggal lahir format dd-mm-yyyy \n");
				pesan.append("Informasi lebih lanjut dapat disampaikan ke : " + "cs@sinarmasmsiglife.co.id");
//				pesan.append(kalimatPertama);
//				pesan.append("Untuk menjaga keamanan polis Anda, Polis Softcopy Anda sudah kami lindungi dengan password berupa tanggal lahir Anda dalam format dd-mm-yyyy (contoh: 14-04-1985).\n");
//				pesan.append("Mohon Polis Softcopy ini disimpan dan dicetak apabila diperlukan.\n\n");
//				pesan.append("Hormat kami, \n");
//				pesan.append("PT Asuransi Jiwa Sinarmas MSIG Tbk.\n\n\n");
//				pesan.append("nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions. Harap tidak me-reply email ini.Meterai dibubuhkan pada hardcopy Polis");
			}
			
			uploader.setEmailmessage(pesan.toString().replaceAll("\n", "</br>"));

			//email agen
			Map infoAgen = elionsManager.selectEmailAgen(uploader.getSpaj());
			String emailAgen = (String) infoAgen.get("MSPE_EMAIL");
			if(emailAgen!=null) {
				if(!emailAgen.trim().equals("")) {
					if(emailAgen.toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
						uploader.setEmailcc(emailAgen);
					}
				}
			}
			
			//kalo agen gak punya email, dikirim ke branch admin nya
			if(uploader.getEmailcc()==null) {
				String emailCabang = uwManager.selectEmailCabangFromSpaj(uploader.getSpaj());
				if(emailCabang!=null) {
					if(!emailCabang.trim().equals("")) {
						if(emailCabang.toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
							uploader.setEmailcc(emailCabang);
						}
					}
				}
			}
			
			//if(uploader.getEmailcc() == null) uploader.setEmailcc("hadi@sinarmasmsiglife.co.id");
			//Req Sari Sutini via Helpdesk 27539 - khusus Mall, email CC ditambahkan ke inge, ningrum, apriyani, dan sutini.
			if(elionsManager.selectLcaIdBySpaj(uploader.getSpaj()).equals("58")){
				uploader.setEmailcc(uploader.getEmailcc()+";apriyani@sinarmasmsiglife.co.id;sutini@sinarmasmsiglife.co.id");
			}
			//MANTA (10/6/2013) - Tambahan khusus DMTM (Req Sari Sutini)
			else if(elionsManager.selectLcaIdBySpaj(uploader.getSpaj()).equals("40")){
				if(lsbs.equals("163")){
					uploader.setEmailcc(uploader.getEmailcc()+";Maria_P@sinarmasmsiglife.co.id;Lylianty@sinarmasmsiglife.co.id;paul.a.tarigan@banksinarmas.com;tri.w.utami@banksinarmas.com");
				}else{
					uploader.setEmailcc(uploader.getEmailcc()+";Maria_P@sinarmasmsiglife.co.id;Lylianty@sinarmasmsiglife.co.id");
				}
			}
			//MANTA (20/11/2017) - Tambahan khusus SIAP2U Agency (37 M1 05)
			else if( "37M105".equals(elionsManager.selectCabangFromSpaj_lar(uploader.getSpaj())) ){
				if((lsbs.equals("190") && lsdbs.equals("009")) || (lsbs.equals("200") && lsdbs.equals("007"))){
					uploader.setEmailcc(uploader.getEmailcc()+";lyna@smileultimate.co.id");
					uploader.setEmailto(uploader.getEmailto()+";polis.siap2u@gmail.com");
				}
			}
			
			//Ridhaal - ERBE (Helpdesk 137909)
			if(elionsManager.selectLcaIdBySpaj(uploader.getSpaj()).equals("73")){
				uploader.setEmailcc("spaj.msig@erbecorp.com;efesty@erbecorp.com");
			}
		}
		
		return new ModelAndView("uw/printpolis/printpolis_softcopy", "cmd", uploader);
	}
	
	private boolean dokumen(String spaj, String jenis, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		String reportPath = null;
		Object dataSource = null;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String cabang = elionsManager.selectCabangFromSpaj(spaj);
		String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;

		if(PdfUtils.isExist(exportDirectory + "\\" + jenis + ".pdf")) {
			return true;
		}
		
		//1. POLIS
		if(props.getProperty("pdf.polis").equals(jenis)) {
			dataSource = this.elionsManager.getUwDao().getDataSource();
			reportPath = props.getProperty("report.polis."+products.kategoriPrintPolis(spaj));
			logger.info("JENIS POLIS : " + reportPath);
			params.put("reportPath", "/WEB-INF/classes/"+reportPath);
			params.put("spaj", spaj);
			//params.put("meterai", "Rp. 6.000,-"); //Req Timmy Untuk Materai dihilangkan helpdesk 74274
			params.put("izin", elionsManager.selectIzinMeteraiTerakhir());
			params.put("tipePolis", "");
			params.put("ingrid", props.getProperty("images.ttd.direksi"));
		//2. MANFAAT POLIS
		}else if(props.getProperty("pdf.manfaat").equals(jenis)) {
			List<Map> temp = new ArrayList<Map>(); 
			Map<String, String> map = new HashMap<String, String>();
			map.put("1", "1"); temp.add(map);
			dataSource = temp;
			params = this.elionsManager.prosesCetakManfaat(spaj, currentUser.getLus_id(), request);
			reportPath = ((String) params.get("reportPath")).substring(17);
		//3. SURAT UCAPAN POLIS
		}else if(props.getProperty("pdf.surat_polis").equals(jenis)) {
			dataSource = this.elionsManager.getUwDao().getDataSource();
			
			List tmp = elionsManager.selectDetailBisnis(spaj);
			String lsbs = (String) ((Map) tmp.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) tmp.get(0)).get("DETBISNIS");
			
			String va = uwManager.selectVirtualAccountSpaj(spaj);
			Integer flag_cc = elionsManager.select_flag_cc(spaj);
			
//			if(va != null){
			if(flag_cc ==0){
				params.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid
				
				List cekSpajPromo = bacManager.selectCekSpajPromo(  null , spaj,  "1"); // cek spaj free sudah terdaftar atau belum MST_FREE_SPAJ
				
				if(!cekSpajPromo.isEmpty()){
					reportPath = props.get("report.surat_polis.va_promo").toString();
				}else if(lsbs.equals("217") && lsdbs.equals("002")){ //untuk ERbe di set menggunakan surat_polis
					reportPath = props.get("report.surat_polis").toString();
				}else if(products.syariah(lsbs, lsdbs)){
					reportPath = props.get("report.surat_polis.syariah").toString();
				}else{
					reportPath = props.get("report.surat_polis.va").toString();
				}	
				
			  //reportPath = props.get("report.surat_polis.va").toString();
				params.put("reportPath", "/WEB-INF/classes/" + reportPath);
				
			}else if(products.syariah(lsbs, lsdbs) && !lsbs.equals("166")) { //khusus amanah, tetep pake yang link
				params.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid
				reportPath = props.get("report.surat_polis.syariah").toString();
				params.put("reportPath", "/WEB-INF/classes/" + reportPath);
			
			}else if(lsbs.equals("142") && lsdbs.equals("008")) { //khusus DM/TM, suratnya beda
				params.put("hamid", props.get("images.ttd.gideon")); //ttd pak gideon
				reportPath = props.get("report.surat_polis.dmtm").toString();
				params.put("reportPath", "/WEB-INF/classes/" + reportPath);
			
			} else {
				params.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid
				if (products.syariah(lsbs, lsdbs)){
					reportPath = props.get("report.surat_polis.syariah").toString();
					params.put("reportPath", "/WEB-INF/classes/" + reportPath);
				}
				else{
				reportPath = props.get("report.surat_polis").toString();
				params.put("reportPath", "/WEB-INF/classes/" + reportPath);
				}
				}
			params.put("props", props);
			params.put("spaj", spaj);
			
		//4. LAPORAN ALOKASI DANA / SURAT UNIT LINK
		}else if(props.getProperty("pdf.alokasi_dana").equals(jenis)) {
			List viewUlink = this.elionsManager.selectViewUlink(spaj);
			if(viewUlink.size()==0) return true;
			params = this.elionsManager.cetakSuratUnitLink(viewUlink, spaj, true, 1, 1,0);
			params.put("elionsManager", this.elionsManager);
			reportPath = ((String) params.get("reportPath")).substring(17);
			dataSource = viewUlink;
		//5. SERTIFIKAT POWERSAVE/STABLELINK
		}else if(props.getProperty("pdf.sertifikat_powersave").equals(jenis)) {
			Map detBisnis = (Map) elionsManager.selectDetailBisnis(spaj).get(0);
			String businessId = (String) detBisnis.get("BISNIS");
			if(products.powerSave(businessId)) {
				Map premiProdukUtama = this.elionsManager.selectPremiProdukUtama(spaj);
				String kurs = (String) premiProdukUtama.get("LKU_ID");
				BigDecimal premi = (BigDecimal) premiProdukUtama.get("MSPR_PREMIUM");
				if((kurs.equals("01") && premi.doubleValue()>=200000000) || (kurs.equals("02") && premi.doubleValue()>=20000)) {
					dataSource = this.elionsManager.getUwDao().getDataSource();
					
					if(businessId.equals("158")) {
						reportPath = props.getProperty("report.sertifikat_powersave_bulanan");
					}else {
						reportPath = props.getProperty("report.sertifikat_powersave");
					}
					
					params = new HashMap<String, Object>();
					params.put("spaj", spaj);
					params.put("ingrid", props.getProperty("images.ttd.direksi"));
					params.put("reportPath", "/WEB-INF/classes/"+reportPath);
				}else {
					return true;
				}
			}else {
				return true;
			}			
		//6. TANDA TERIMA POLIS
		}else if(props.getProperty("pdf.tanda_terima_polis").equals(jenis)) {
			dataSource = this.elionsManager.getUwDao().getDataSource();
			List detBisnis = elionsManager.selectDetailBisnis(spaj);
			String lsbs_id = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
//			String lsbs_id = FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 3);
			if(products.unitLink(lsbs_id))
				if(products.syariah(lsbs_id, lsdbs)){
					reportPath = props.getProperty("report.tandaterimapolis.syariah");
				}else{
					reportPath = props.getProperty("report.tandaterimapolis.link");
				}
			else if(lsbs_id.equals("187")){
				reportPath = props.getProperty("report.tandaterimapolis.pas");
			}
			else 
				reportPath = props.getProperty("report.tandaterimapolis");
			params.put("spaj", spaj);
			params.put("reportPath", "/WEB-INF/classes/"+reportPath);
		//7. KARTU PREMI
		}else if(props.getProperty("pdf.kartu_premi").equals(jenis)) {
			dataSource = this.elionsManager.getUwDao().getDataSource();
			reportPath = props.getProperty("report.kartu_premi");
			params.put("spaj", spaj);
			params.put("reportPath", "/WEB-INF/classes/"+reportPath);
		//8. WORDING / SYARAT-SYARAT UMUM DAN KHUSUS POLIS
		}else if(props.getProperty("pdf.ssu").equals(jenis)) {
			List<File> pdfFiles = WordingPdfViewer.listFileProduct(elionsManager, uwManager, props, spaj);
			PdfUtils.combinePdf(pdfFiles, exportDirectory, jenis+".pdf");
			if(!pdfFiles.isEmpty()) return true;
		//9. POLIS DM/TM
		}else if(props.getProperty("pdf.polis_dmtm").equals(jenis)) {
			String dir = props.getProperty("pdf.dir.syaratpolis");
			List detBisnis = elionsManager.selectDetailBisnis(spaj);
			String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");

			PdfReader reader = new PdfReader(dir + "/" + lsbs + "-" + lsdbs + "-SOFTCOPY.pdf");
			FileOutputStream fileOutput = new FileOutputStream(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+props.getProperty("pdf.polis_dmtm")+".pdf");
			BufferedOutputStream bos = new BufferedOutputStream(fileOutput);
			PdfStamper stamper = new PdfStamper(reader, bos);
			
			PdfContentByte cb = stamper.getOverContent(1);
	        for (int j=1; j <= reader.getNumberOfPages(); j++){
                //Add Contents hanya pada Halaman 1
                if(j == 1) {
                	cb.beginText();
                	BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
                	
                	cb.setFontAndSize(bf, 12);
                	cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS ASURANSI JIWA PERORANGAN POWER SAVE", 299, 744, 0);

                	bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
                	cb.setFontAndSize(bf, 9);

                	//Kode Bea Meterai
                	String meterai = elionsManager.selectIzinMeteraiTerakhir();
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, meterai,	 							426, 767, 0);
                	
                	//Bagian Label
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "No. Polis", 							20, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pemegang Polis", 					20, 705, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Umur", 								20, 693, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Alamat", 							20, 681, 0);

                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":",				 					95, 705, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 693, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 681, 0);

                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tertanggung", 						308, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Umur", 								308, 705, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Masa Asuransi", 						308, 693, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Uang Pertanggungan", 				308, 681, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Besarnya Premi Sekaligus", 			308, 669, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Masa Garansi Investasi (MGI)", 		308, 657, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tingkat Investasi Pada MGI Pertama", 308, 645, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nilai Tunai Akhir MGI Pertama", 		308, 633, 0);
                	
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":",		 							458, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 705, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 693, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 681, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 669, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 657, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 645, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 633, 0);
                	
                	//Bagian Kiri
                	Map m = elionsManager.selectPolisPowersaveDMTM(spaj);
                	
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("MSPO_POLICY_NO_FORMAT"), 	100, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("PP_NAMA"), 				100, 705, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (BigDecimal) m.get("PP_UMUR") + " Tahun",	100, 693, 0);
                	
                	//Bagian Alamat Rumah
                	int monyong = 0;
//                	String[] alamat = StringUtil.pecahParagraf((String) m.get("ALAMAT_RUMAH"), 32);
//                	for(int i=0; i<alamat.length; i++) {
//                		monyong = 12 * i;
//                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i],							100, 681-monyong, 0);
//                	}
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("KOTA_RUMAH"), 			100, 669-monyong, 0);

                	//Bagian Kanan
                	JasperScriptlet jasper = new JasperScriptlet();
                	
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("TT_NAMA"), 463, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (BigDecimal) m.get("TT_UMUR") + " Tahun", 463, 705, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("MASA_ASURANSI"), 463, 693, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrency((String) m.get("LKU_SYMBOL"), (BigDecimal) m.get("MSPR_TSI")), 463, 681, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrency((String) m.get("LKU_SYMBOL"), (BigDecimal) m.get("MSPR_PREMIUM")), 463, 669, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (BigDecimal) m.get("MPS_JANGKA_INV") + " Bulan", 463, 657, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (BigDecimal) m.get("MPS_RATE") + "% Per Tahun", 463, 645, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrency((String) m.get("LKU_SYMBOL"), (BigDecimal) m.get("NILAI_TUNAI")), 463, 633, 0);
                	
                	
                	cb.endText();
                }
                if(stamper!=null){
                	stamper.close();
                }
	        }
			return true;
		}else if(props.getProperty("pdf.polis_pas").equals(jenis) || props.getProperty("pdf.polis_term").equals(jenis)) {
			String dir = props.getProperty("pdf.dir.syaratpolis");
			List detBisnis = elionsManager.selectDetailBisnis(spaj);
			String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
			
			List rider = elionsManager.selectRiderPolisPas(spaj);
			
	        File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj);
	        if(!userDir.exists()) {
	            userDir.mkdirs();
	        }

			List<String> pdfs = new ArrayList<String>();
			boolean suksesMerge = false;
			String PolisSSU = "";
			String SSK = "";
			
			PolisSSU = dir + "\\" + lsbs + "-" + lsdbs +".pdf";
			pdfs.add(PolisSSU);
			if(rider.size()>0){
				for(int i=1;i<detBisnis.size();i++){
					String lsbs_rider = (String) ((Map) detBisnis.get(i)).get("BISNIS");
					String lsdbs_rider = (String) ((Map) detBisnis.get(i)).get("DETBISNIS");
					SSK = dir + "\\" +"RIDER"+"\\"+ lsbs_rider + "-" + lsdbs_rider +".pdf";
					pdfs.add(SSK);
				}
			}
			OutputStream output = new FileOutputStream(props.getProperty("pdf.template.pas")+"\\MergeSSUSSK.pdf");
			if(lsbs.equals("196")){
				output = new FileOutputStream(props.getProperty("pdf.template.term")+"\\MergeSSUSSK.pdf");
			}
			if (lsbs.equals("073")&& lsdbs.equals("008"))
			{
				output = new FileOutputStream(props.getProperty("pdf.template.pa")+"\\MergeSSUSSK.pdf");
			}
			suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			
			PdfReader reader = new PdfReader(props.getProperty("pdf.template.pas")+"\\MergeSSUSSK.pdf");
			
			String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+props.getProperty("pdf.polis_pas")+".pdf";
			if(lsbs.equals("196")){
				reader = new PdfReader(props.getProperty("pdf.template.suryamas")+"\\MergeSSUSSK-TERM.pdf");
				outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+props.getProperty("pdf.polis_term")+".pdf";
			}
			if(lsbs.equals("073")&& lsdbs.equals("008")){
				reader = new PdfReader(props.getProperty("pdf.template.suryamas")+"\\MergeSSUSSK-PA.pdf");
				outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+props.getProperty("pdf.polis_pa")+".pdf";
			}
			File file = null;
			file = new File(outputName);
			PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(file));
	        PdfContentByte cb = stamp.getOverContent(1);
	        		cb = stamp.getOverContent(1);
                	cb.beginText();
                	//BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
                	//BaseFont arial = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
                	BaseFont bf = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALNB.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
                	BaseFont arialbd = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALBD.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
                	//BaseFont arial_narrow = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALN.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        			//BaseFont arial_narrow_bold = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALNB.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
                	cb.setFontAndSize(bf, 12);
                	if(lsbs.equals("196")){
                		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS TERM INSURANCE", 299, 756, 0);
                	}else if (lsbs.equals("073")&& lsdbs.equals("008")){
                		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS PERSONAL ACCIDENT (RISIKO A)", 299, 756, 0);
                	}
                	else{
                		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS PERSONAL ACCIDENT SINARMASLIFE (PAS)", 299, 756, 0);
                	}
                	

                	//bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
                	bf = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALN.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
                	cb.setFontAndSize(bf, 8);

                	//Kode Bea Meterai
//                	String meterai = elionsManager.selectIzinMeteraiTerakhir();
//                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, meterai,	 							426, 767, 0);
                	
                	//Bagian Label
                	if(!lsbs.equals("196")){
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "No. Kartu", 							20, 729, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Mulai Asuransi", 					20, 669, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 729, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 669, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nama Paket", 						308, 705, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									440, 705, 0);
                	}
                	
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "No. Polis", 							20, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pemegang Polis", 					20, 705, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tgl Lahir/Umur", 					20, 693, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Masa Asuransi", 						20, 681, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Alamat", 							20, 657, 0);

                	
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":",				 					95, 705, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 693, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 681, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 657, 0);

                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tertanggung", 						308, 729, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tgl Lahir/Umur", 					308, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Uang Pertanggungan", 				308, 693, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Premi", 								308, 681, 0);
                	if(rider.size()>0){
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Asuransi Tambahan", 					308, 669, 0);
//                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tingkat Investasi Pada MGI Pertama", 308, 645, 0);
//                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nilai Tunai Akhir MGI Pertama", 		308, 633, 0);
                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									440, 669, 0);
//                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 645, 0);
//                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 633, 0);
                	}
                	
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":",		 							440, 729, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									440, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									440, 693, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									440, 681, 0);
                	
                	
                	//Bagian Kiri
                	
                	Map m = new HashMap();
                	if(lsbs.equals("196")){
                		m = elionsManager.selectPolisPas(spaj);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, StringUtil.nomorPAS((String) m.get("NO_KARTU")), 	100, 729, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("MSPR_BEG_DATE")),	100, 669, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT,  m.get("PAKET")==null?"":(String) m.get("PAKET"), 445, 705, 0);
                	}else{
                		m = uwManager.selectPolisBiasa(spaj);
                	}
                	
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("MSPO_POLICY_NO_FORMAT"), 	100, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("NAMA_PP"), 				100, 705, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("TGL_LAHIR_PP"))+"/"+ (BigDecimal) m.get("USIA_PP") + " Tahun",	100, 693, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "1 tahun dan dapat diperpanjang",	100, 681, 0);
                	
                	//Bagian Alamat Rumah
                	int monyong = 0;
//                	String[] alamat = StringUtil.pecahParagraf((String) m.get("ALAMAT_RUMAH"), 32);
//                	for(int i=0; i<alamat.length; i++) {
//                		monyong = 12 * i;
//                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i],							100, 681-monyong, 0);
//                	}
                	int monyong2 = 0;
                	String[] alamat = StringUtil.pecahParagrafLineBreaksInclude((String) m.get("ALAMAT"), 32);
                	for(int i=0; i<alamat.length; i++) {
                		monyong2 = 12 * i;
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i] , 			20, 647-monyong2, 0);
                	}
//                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i] , 			20, 645, 0);

                	//Bagian Kanan
                	JasperScriptlet jasper = new JasperScriptlet();
                	
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("NAMA_TT"), 445, 729, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("TGL_LAHIR_TT"))+"/"+ (BigDecimal) m.get("USIA_TT") + " Tahun", 445, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrency((String) m.get("LKU_SYMBOL"), (BigDecimal) m.get("MSPR_TSI")), 445, 693, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrency((String) m.get("LKU_SYMBOL"), (BigDecimal) m.get("MSPR_PREMIUM"))+" per "+ m.get("LSCB_PAY_MODE").toString().toLowerCase(), 445, 681, 0);
                	if(rider.size()>0){
	                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "ASURANSI DEMAM BERDARAH", 445, 669, 0);
	                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "- Premi : Gratis", 450, 657, 0);
                	}
//                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (BigDecimal) m.get("MPS_RATE") + "% Per Tahun", 463, 645, 0);
//                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrency((String) m.get("LKU_SYMBOL"), (BigDecimal) m.get("NILAI_TUNAI")), 463, 633, 0);
//                	if (lsbs.equals("187")&& lsdbs.equals("013")){ //#20082019 igaukiarwan #request bu titis prod PAS ada tanda tangan direksi
                		int penambahYFooter=0;
                    	int pengurangXdetail=0;
                    	Date sysdate = elionsManager.selectSysdate();
                    	String direksi ="Chief Operating & IT Officer";
                    	if(products.productChannel88(Integer.parseInt(lsbs), Integer.parseInt(lsdbs))){
                    		penambahYFooter=30;
                    		direksi="Chief Operating & IT Officer";
                    	}
                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Jakarta, " + FormatDate.toIndonesian(sysdate), 451-pengurangXdetail, 607+penambahYFooter, 0);
                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "PT Asuransi Jiwa Sinarmas MSIG Tbk.", 451-pengurangXdetail, 595+penambahYFooter, 0);
                    	
                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Andrew Bain", 451-pengurangXdetail, 535+penambahYFooter, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "__________", 451-pengurangXdetail, 535+penambahYFooter, 0);
                		if(products.productChannel88(Integer.parseInt(lsbs), Integer.parseInt(lsdbs))){
                			cb.setFontAndSize(arialbd, 8);
                        }
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, direksi, 451-pengurangXdetail, 526+penambahYFooter, 0);
                    	
                    	cb.endText();
                    	
                    	Image img = Image.getInstance(Resources.getResourceAsFile(props.getProperty("images.ttd.direksi").trim()).getAbsolutePath());
        				
        				img.setAbsolutePosition(450-pengurangXdetail, 545+penambahYFooter);		
        				img.scaleAbsolute(120, 34);
        				cb.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 450-pengurangXdetail, 545+penambahYFooter);
        				cb.stroke();
//                	}
//                	else{
                		cb.endText();
//                	}
                	
//                }
//	        }
            if(stamp!=null){
            	stamp.close();
            }
            
            return true;
		}else {
			return false;
		}
		
		//3 buah tujuan, TAMPILKAN_PDF, CETAK_KE_LAYAR atau SIMPAN_KE_PDF, tapi yg tampilkan PDF ditaro diatas
		params.put("props", props);
		Connection  conn = null;
		int iErr = 0;
		try {
			conn = this.elionsManager.getUwDao().getDataSource().getConnection();
			JasperUtils.exportReportToPdf(
					reportPath+".jasper", exportDirectory, jenis+".pdf", 
					params, conn, 
					null, props.getProperty("pdf.ownerPassword"), props.getProperty("pdf.userPassword"));
		} catch (Exception e) {
			logger.error("ERROR :", e);
			iErr =1;
		}finally{
			this.elionsManager.getUwDao().closeConn(conn);
			if (iErr == 1){
				return false;
			}
		}

		return true;

	}

	//validasi print polis untuk orang cabang
	public ModelAndView validforprint(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> cmd = new HashMap<String, Object>();
		String spaj = ServletRequestUtils.getRequiredStringParameter(request, "spaj");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		CekValidPrintPolis cvpp = new CekValidPrintPolis();
		String All=ServletRequestUtils.getStringParameter(request, "All");
		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());
		Pemegang pemegang = elionsManager.selectpp(spaj);
		String emailto = ServletRequestUtils.getStringParameter(request, "emailto", "");
		String emailcc = ServletRequestUtils.getStringParameter(request, "emailcc", ""); 
		String emailsubject = ServletRequestUtils.getStringParameter(request, "emailsubject", ""); 
		String emailmessage = ServletRequestUtils.getStringParameter(request, "emailmessage", "");
		String keterangan = ServletRequestUtils.getStringParameter(request, "keterangan", "");
		String polut = ServletRequestUtils.getStringParameter(request, "polut","0");
		String manfaat = ServletRequestUtils.getStringParameter(request, "manfaat","0");
		String lapadan = ServletRequestUtils.getStringParameter(request, "lapadan","0");
		String ttp = ServletRequestUtils.getStringParameter(request, "ttp","0");
		String ssu = ServletRequestUtils.getStringParameter(request, "ssu","0");
		String sppp = ServletRequestUtils.getStringParameter(request, "sppp","0");
		String kp = ServletRequestUtils.getStringParameter(request, "kp","0");
		String keycab= ServletRequestUtils.getStringParameter(request, "lca_id","0");
		String valid= ServletRequestUtils.getStringParameter(request, "validasi","0");
		String emailCab;
		String team_name=uwManager.selectBancassTeam(spaj);
		String lsbs_id=uwManager.selectLsbsId(spaj);
		String lsdbs_number = uwManager.selectLsdbsNumber(spaj);
		String file_tambahan = ServletRequestUtils.getStringParameter(request, "file_tambahan",null);
		if(products.unitLink(lsbs_id) || (lsbs_id.equals("164") && lsdbs_number.equals("11"))){
			List errors = new ArrayList();
			List<Date> asdf = uwManager.selectSudahProsesNab(spaj);
			boolean oke = true;
			for(Date d : asdf){
				if(d == null) {
					oke = false;
					break;
				}
			}
			if(asdf.size()==0){
				oke = false;
			}
			if(!oke) {
				errors.add("Polis tidak dapat di proses valid karena belum dilakukan proses NAB.");
				return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
			}
		}
	
		
		//user menekan tombol OK untuk cek kelengkapan
		if(request.getParameter("ok") != null) {
			if(polut==null|| polut.equalsIgnoreCase("0")||manfaat==null|| manfaat.equalsIgnoreCase("0")||lapadan==null|| lapadan.equalsIgnoreCase("0")
					|| ttp==null||ttp.equalsIgnoreCase("0")||ssu==null||ssu.equalsIgnoreCase("0")||sppp==null||sppp.equalsIgnoreCase("0")){
				cmd.put("errors1", "Silahkan melengkapi terlebih dahulu");
			}else if (polut!=null&& manfaat!=null&&lapadan!=null){
				cvpp.setReg_spaj(spaj);
				cvpp.setCek_polis_utama(polut);
				cvpp.setCek_manfaat(manfaat);
				cvpp.setCek_alo_dana(lapadan);
				cvpp.setTtp(ttp);
				cvpp.setSsu(ssu);
				cvpp.setSppp(sppp);
				cvpp.setKp(kp);
				if(elionsManager.selectCountCekValid(spaj)>0){
					uwManager.deleteCekValid(spaj);
				}
				elionsManager.insertLstCekValid(cvpp);
				cmd.put("errors1", "Data sudah lengkap, Silahkan melanjutkan ke proses pengiriman email");
				
			}
		}
		
		List selectStampHist=elionsManager.selectStampHist(spaj);
		List lsRegion=new ArrayList();
		if(team_name==null)team_name= "";			
	    lsRegion=elionsManager.selectAllLstCab();
		
		List lsAdmin=new ArrayList();
		
		String lca_id;
		//lca_id = ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		lca_id = pemegang.getLca_id();
		StringBuffer result = new StringBuffer();
		List<String> emailCC = new ArrayList();
		emailCC.add(currentUser.getEmail());
		if (!(lca_id.equals("09") ||lca_id.equals("44")||keycab.equals("44")||keycab=="44")){
			emailCC.add("iway@sinarmasmsiglife.co.id");
		}
		if(lsbs_id.equals("164") && lsdbs_number.equals("11")){//req Novie via Helpdesk, cc ditambahkan novie dan inge apabila produk new simas stabil link
			emailCC.add("novie@sinarmasmsiglife.co.id");
			emailCC.add("inge@sinarmasmsiglife.co.id");
		}
		
		Map cabang=new HashMap();
		if (All !=null){
			cabang=new HashMap();
			cabang.put("KEY",currentUser.getLca_id());
			cabang.put("VALUE",currentUser.getCabang());
			
			lsRegion.add(cabang);
			lsAdmin=elionsManager.selectAllUserInCabang(lca_id); //all
			
		}else if(team_name.toUpperCase().equals("TEAM YANTI SUMIRKAN") || team_name.toUpperCase().equals("TEAM JAN ROSADI")){
			cabang=new HashMap();
			cabang.put("KEY","44");
			cabang.put("VALUE","BANCASSURANCE(YANTI)");
			lsRegion.clear();
			lsRegion.add(cabang);
			cmd.put("team","1");
		}else{
			cabang=new HashMap();
			cabang.put("KEY",currentUser.getLca_id());
			cabang.put("VALUE",currentUser.getCabang());
			lsRegion.add(cabang);

		}
		if(request.getParameter("cari") != null) {
			if(keycab.equals("44")){//email to untuk bancassurance (YANTI)
				emailCab = "jefry_s@sinarmasmsiglife.co.id;destyana@sinarmasmsiglife.co.id;aderohman@sinarmasmsiglife.co.id;fabry@sinarmasmsiglife.co.id";
			}else{
				emailCab= uwManager.selectEmailCabang(keycab);
			}
			if (emailCab!=null){
				emailto=emailCab;
			}else if(currentUser.getLca_id().equals("58")){
				emailto="Lylianty@sinarmasmsiglife.co.id";
			}else{
				emailto="";
			}
			
			if(keycab.equals("45")){
				emailCC.add("tasya@sinarmasmsiglife.co.id");
				emailCC.add("hendry@sinarmasmsiglife.co.id");
				emailCC.add("patricia@sinarmasmsiglife.co.id");
			}
			
		}
		if(!currentUser.getLca_id().equals("58")){
			for(String email : emailCC) {
				if(email != null) if(!email.trim().equals("")) result.append(email+";");
			}
			if(keycab.equals("44"))result.append("tities@sinarmasmsiglife.co.id;");//tambahan email to untuk bancassurance (YANTI)
		}else{
			emailto="Lylianty@sinarmasmsiglife.co.id";
			result.append("UnderwritingDMTM@sinarmasmsiglife.co.id");
		}
		
		String mstm_bulan = elionsManager.mstm_bulan();
		if(selectStampHist!= null){
			elionsManager.deletemstStampHist(spaj);
			elionsManager.mstStampMaterai(mstm_bulan);
			
			//user menekan tombol kirim valid for print
			if(request.getParameter("kirim") != null) {
				if(Integer.parseInt(valid)==0){
					cmd.put("errors", "Silahkan Pilih Salah Satu Proses Valid");
				}
				if(emailto.trim().equals("") || emailsubject.trim().equals("") || emailmessage.trim().equals("")) {
					cmd.put("errors", "Silahkan lengkapi dahulu");
				}else {
					if(Integer.parseInt(valid)==1){//valid untuk print polis di cabang
						uwManager.updateValidForPrint(
								spaj, emailto.split(";"), emailcc.split(";"), emailsubject, emailmessage,
								currentUser, keterangan, file_tambahan);
						
					}else if(Integer.parseInt(valid)==2){
						elionsManager.insertMstPositionSpaj(currentUser.getLus_id(),"VALID PENGIRIMAN SOFTCOPY "+keterangan,spaj, 0);
						//tambah pertanyaan lanjut ke proses email softcopy, bila validnya untuk softcopy
						cmd.put("tanyaEmail", "true");
					}					
				}
			}else {
				if (lca_id.equals("09") ||lca_id.equals("44")||keycab.equals("44")||keycab=="44"){
//					String e = "clara_n@sinarmasmsiglife.co.id;iis@sinarmasmsiglife.co.id;grisye@sinarmasmsiglife.co.id;erwin_k@sinarmasmsiglife.co.id";
//					String e = "deddy@sinarmasmsiglife.co.id";
					int jn_bank = elionsManager.selectIsInputanBank(spaj);
					String emailInputter = uwManager.selectEmailUserInputFromSpaj(spaj);
					if(currentUser.getLde_id().equals("11") && jn_bank==2){
						String emailCabangBank = uwManager.selectEmailCabangBankSinarmas(spaj);
				    	if(emailCabangBank != null) emailInputter = emailCabangBank;
					}
					if(emailInputter != null && emailto.equals("")) emailto = emailInputter;
//					emailto= e;
					cmd.put("emailto", emailto);
					cmd.put("lca_id", keycab);
					cmd.put("emailCab",  emailto);
				}else if(currentUser.getLca_id().equals("58")){
					cmd.put("emailto", "Lylianty@sinarmasmsiglife.co.id");
					cmd.put("lock", "1");
				}else if(lca_id.equals("42") || keycab.equals("42")){//MANTA
					String emailtmp = uwManager.selectEmailCabangFromSpaj(spaj);
					if(emailtmp.equals("corporate.sby@sinarmasmsiglife.co.id")){
						emailtmp = "setyo@sinarmasmsiglife.co.id";
					}
					if(emailtmp.equals("salihara_sby@yahoo.com")){
						emailtmp = "arthamas.stargroup@gmail.com";
					}
					cmd.put("emailto", emailtmp);
					cmd.put("lca_id", keycab);
					cmd.put("emailCab",  uwManager.selectEmailCabang(keycab));
				}else{
					String emailtmp = uwManager.selectEmailCabangFromSpaj(spaj);
					if(emailtmp.equals("salihara_sby@yahoo.com")){
						emailtmp = "arthamas.stargroup@gmail.com";
					}
					cmd.put("emailto", emailtmp);
					cmd.put("lca_id", keycab);
					cmd.put("emailCab",  uwManager.selectEmailCabang(keycab));
				}
				
				cmd.put("lsRegion", lsRegion);			
				cmd.put("emailcc", result.toString());
				if(!currentUser.getLca_id().equals("58")){
					cmd.put("emailsubject", "[Underwriting] Polis Nomor Registrasi [" + FormatString.nomorSPAJ(spaj) + "] atas nama "+pemegang.getMcl_first()+" sudah bisa dicetak/diproses Softcopy.");
					cmd.put("emailmessage", 
							"Dh,\n"+
							"Polis dengan nomor registrasi " + FormatString.nomorSPAJ(spaj) + " sudah bisa dicetak/diproses Softcopy.\n\n"+
							"Terima kasih,\n"+
							currentUser.getName() + "\n" + currentUser.getDept());
				}else{
					cmd.put("emailsubject", "[BAS-MallAssurance] Cetak Ulang Polis Nomor Registrasi [" + FormatString.nomorSPAJ(spaj) + "] atas nama "+pemegang.getMcl_first()+".");
					cmd.put("emailmessage", 
							"Dh,\n"+
							"Polis dengan nomor registrasi " + FormatString.nomorSPAJ(spaj) + " telah dilakukan pencetakan ulang.\n\n"+
							"Terima kasih,\n"+
							currentUser.getName() + "\n" + currentUser.getDept());
				}
				
			}
		}else {
			
			//user menekan tombol kirim valid for print
			if(request.getParameter("kirim") != null) {
			//	Integer contReasTemp= elionsManager.selectCountCekValid(spaj);
				//if (contReasTemp>0){
				if(Integer.parseInt(valid)==0){
					cmd.put("errors", "Silahkan Pilih Salah Satu Proses Valid");
				}
				if(emailto.trim().equals("") || emailsubject.trim().equals("") || emailmessage.trim().equals("")) {
					cmd.put("errors", "Silahkan lengkapi dahulu");
				}else {
					if(Integer.parseInt(valid)==1){//valid untuk print polis di cabang
						uwManager.updateValidForPrint(
								spaj, emailto.split(";"), emailcc.split(";"), emailsubject, emailmessage,
								currentUser, keterangan, file_tambahan);
					}else if(Integer.parseInt(valid)==2){
						elionsManager.insertMstPositionSpaj(currentUser.getLus_id(),"VALID PENGIRIMAN SOFTCOPY "+keterangan,spaj, 0);
						//tambah pertanyaan lanjut ke proses email softcopy, bila validnya untuk softcopy
						cmd.put("tanyaEmail", "true");
					}
					
				}
		//		}else{
			//		cmd.put("errors", "Silahkan melengkapi daftar cek kelengkapan data");
			//	}
			}else {
				cmd.put("lca_id", lca_id);
				cmd.put("lsRegion", lsRegion);
				cmd.put("emailto", uwManager.selectEmailCabang(spaj.substring(0,2)));
				cmd.put("emailcc", currentUser.getEmail());
				cmd.put("emailsubject", "[Underwriting] Polis Nomor Registrasi [" + FormatString.nomorSPAJ(spaj) + "] atas nama "+pemegang.getMcl_first()+" sudah bisa dicetak/diproses Softcopy.");
				cmd.put("emailmessage", 
					"Dh,\n"+
					"Polis dengan nomor registrasi " + FormatString.nomorSPAJ(spaj) + " sudah bisa dicetak/diproses Softcopy..\n\n"+
					"Terima kasih,\n"+
					currentUser.getName() + "\n" + currentUser.getDept());
			}
		}
		cmd.put("lsbs_id", products.unitLinkNew(lsbs_id));
		return new ModelAndView("uw/printpolis/printpolis_validforprint", cmd);
	}
	
	public ModelAndView viewer_info(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String spaj = ServletRequestUtils.getRequiredStringParameter(request, "spaj");
		Date tglCetak = elionsManager.selectPrintDatePolis(spaj);
		Calendar tgl_mulai_ssu = new GregorianCalendar(2006, 8, 20);
		List<String> pesan = new ArrayList<String>();
		
		if(tglCetak == null) {
			pesan.add("- Untuk mencetak SSU harus melakukan pencetakan polis terlebih dahulu.");
		}else if(tglCetak.before(tgl_mulai_ssu.getTime())) {
			pesan.add("- SSU/SSK yang benar adalah yang ada di tangan nasabah.");
		} 
		
		return new ModelAndView("uw/printpolis/printpolis_viewer_info", "pesan", pesan);
	}
	
	//Print Polis yang ada di Viewer
	public ModelAndView viewer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String spaj = ServletRequestUtils.getRequiredStringParameter(request, "spaj");
		String businessId = this.uwManager.selectBusinessId(spaj);

		Map m = null;
				
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		if( //orang bancass (GRISYE dan JELITA), boleh view dan print HANYA COVER POLIS
				!currentUser.getLde_id().equals("01") &&
				!currentUser.getLde_id().equals("11") &&
				(Integer.valueOf(currentUser.getLus_id()) == 39 ||
				Integer.valueOf(currentUser.getLus_id()) == 672)
				) {
			m = this.referenceData(spaj, businessId, false, true, true, currentUser);
			m.put("orangBancass", "true");
		}else {
			m = this.referenceData(spaj, businessId, false, true, false, currentUser);
			if(Integer.valueOf(currentUser.getLus_id()) == 49 || Integer.valueOf(currentUser.getLus_id()) == 74 || Integer.valueOf(currentUser.getLus_id()) == 420 || Integer.valueOf(currentUser.getLus_id()) == 574) {
				m.put("bolehCetakSeenakJidat", "true");
			}
		}
		
		if(currentUser.getLde_id().equals("11")) {
			m.put("orangUnderwriting", "true");
		}

		m.put("spaj", spaj);
		
		return new ModelAndView("uw/printpolis/printpolis_viewer", m);
	}
	
	//LINK2 YANG ADA DI PRINT POLIS, SEPERTI HALAMAN DEPAN DAN TRANSFER POLIS, PRINT ULANG, PRINT
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String spaj = request.getParameter("spaj");
		String businessId = this.uwManager.selectBusinessId(spaj);
		String snow_spaj = ServletRequestUtils.getStringParameter(request, "snow_spaj", "");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map m = this.referenceData(spaj, businessId, false, false, false, currentUser);
		m.put("warning", uwManager.warningBeaMeterai(currentUser.getJn_bank().intValue()));
		m.put("snow_spaj", snow_spaj);
		
		//Yusuf - 14/12/2007 - flag yang menandakan polis inputan bank
		List daftarSpaj = null;
		
		//apabila inputan bank, daftar spaj yang visible hanya yang inputan dia saja
		/*
		if (cab_bank.equalsIgnoreCase(""))
		{
			lsDaftarSpaj=this.elionsManager.selectDaftarSPAJ("1",1,null,null);
		}else if("SSS".equals(user.getCab_bank())){ //user admin simas prima
			lsDaftarSpaj=this.elionsManager.selectDaftarSpajUwSimasPrima(1, jn_bank);
		}else{
			lsDaftarSpaj=this.elionsManager.selectDaftarSPAJ_valid("1",1,null,lus_id);
		}
		*/
		
		if("SSS".equals(currentUser.getCab_bank().trim()) || currentUser.getJn_bank().intValue() == 3) {
			daftarSpaj = this.uwManager.selectDaftarSpajUwSimasPrima(6, currentUser.getJn_bank(), currentUser.getFlag_approve(),Integer.parseInt(currentUser.getLus_id()),currentUser.getCab_bank().trim());
		}else if(elionsManager.selectIsUserInputBank(Integer.valueOf(currentUser.getLus_id()))>-1) {
			daftarSpaj = this.uwManager.selectDaftarSpajInputanBank(Integer.valueOf(currentUser.getLus_id()), 1, 6);
		}else if(currentUser.getLca_id().equals("55")){
			daftarSpaj = this.uwManager.selectDaftarSpajInputanASM(Integer.valueOf(currentUser.getLus_id()), 1, 6);
		}else if(currentUser.getLca_id().equals("58")){
			daftarSpaj = this.uwManager.selectDaftarSpajInputanMall(Integer.valueOf(currentUser.getLus_id()), 1, 6);
		}
		else {
			////daftarSpaj = this.uwManager.selectDaftarSPAJ("6", 1,null,null);
			//daftarSpaj = this.uwManager.selectDaftarSPAJ("6", 3,null,null);
			daftarSpaj = this.uwManager.selectDaftarSPAJ("1000", 3,null,null);
		}
		
		if(elionsManager.selectIsSupervisorOrPincabBankSinarmas(Integer.valueOf(currentUser.getLus_id()))>0 || currentUser.getLde_id().equals("11")) {
			m.put("isSupervisorOrPincabOrUw", true);
		}
		
		return new ModelAndView("uw/printpolis/printpolis", m).addObject("daftarSPAJ", daftarSpaj);
	}

	public ModelAndView transfer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String spaj = request.getParameter("spaj");
		User currentUser = (User) request.getSession().getAttribute("currentUser");		
		Map m = new HashMap();
		m.put("transfer", "true");
		
		String user = currentUser.getLus_full_name();	
		Map mTertanggung = new HashMap();
		Date tglPrintPolis = null;
		Date tglKirim = null;
		Date tglTerimaAdmedika = null;
		Date tglTerimaSpaj=null;
		Integer lssaId=null;
		if(spaj!=null) {
			mTertanggung=elionsManager.selectTertanggung(spaj);
			tglKirim = (Date) mTertanggung.get("MSTE_TGL_KIRIM_POLIS");
			tglTerimaAdmedika = (Date) mTertanggung.get("MSTE_TGL_TERIMA_ADMEDIKA");
			tglTerimaSpaj = (Date)mTertanggung.get("MSTE_TGL_TERIMA_SPAJ");
			lssaId=(Integer)mTertanggung.get("LSSA_ID");
			tglPrintPolis= (Date) mTertanggung.get("MSPO_DATE_PRINT");
		}
		
		//Yusuf - 5/4/2007 - Apabila powersave, harus cetak sertifikat kalo diatas $20k / Rp200jt
		List detail = elionsManager.selectDetailBisnis(spaj);
		Map det = (HashMap) detail.get(0);
		String bisnis = (String) det.get("BISNIS");
		String detbisnis = (String) det.get("DETBISNIS");
		
//		if(products.powerSave(bisnis) && Integer.valueOf(bisnis) != 155 &&
//				!(Integer.valueOf(bisnis)==158 && Integer.valueOf(detbisnis)==5) &&
//				!(Integer.valueOf(bisnis)==158 && Integer.valueOf(detbisnis)==8) &&
//				!(Integer.valueOf(bisnis)==158 && Integer.valueOf(detbisnis)==9) &&
//				!(Integer.valueOf(bisnis)==158 && Integer.valueOf(detbisnis)==10) &&
//				!(Integer.valueOf(bisnis)==158 && Integer.valueOf(detbisnis)==11) &&
//				!(Integer.valueOf(bisnis)==158 && Integer.valueOf(detbisnis)==12)&&
//				!(Integer.valueOf(bisnis)==143 && Integer.valueOf(detbisnis)==1) &&
//				!(Integer.valueOf(bisnis)==143 && Integer.valueOf(detbisnis)==2)) {
//			Map premiProdukUtama = this.elionsManager.selectPremiProdukUtama(spaj);
//			String kurs = (String) premiProdukUtama.get("LKU_ID");
//			BigDecimal premi = (BigDecimal) premiProdukUtama.get("MSPR_PREMIUM");
//			if((kurs.equals("01") && premi.doubleValue()>=200000000) || (kurs.equals("02") && premi.doubleValue()>=20000)) {
//				String cabang = elionsManager.selectCabangFromSpaj(spaj);
//		        File fileName= new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+props.getProperty("pdf.sertifikat_powersave")+".pdf");
//		        if(!fileName.exists()) {
//		        	m.put("error", "Anda belum mencetak sertifikat powersave/stable link!");
//		        }		
//			}
//		}
		
		int isInputanBank = -1;
		isInputanBank = elionsManager.selectIsInputanBank(spaj);
		String lca_id = elionsManager.selectLcaIdBySpaj(spaj);
		//Yusuf - 25-09-08 - Bila bukan inputan bank, harus ada validasi checklist
//		if(isInputanBank != 2 && isInputanBank != 3) {//permintaan tities 09062010
		boolean cekcek= elionsManager.selectValidasiCheckListBySpaj(spaj);
		if(!cekcek){
			m.put("error", "Harap Input CHECKLIST DOKUMEN POLIS Terlebih Dahulu!");
		}		
		
		/*
		 * nambah fusngsi email
		 * dian natalia
		*/
//		if(!currentUser.getLde_id().equals("11")) {
//			email.sendWithAttachments("it@sinarmasmsiglife.co.id", new String[]{"hayatin@sinarmasmsiglife.co.id"}, null,  
//					"Polis ["+spaj+"] sudah ditransfer", 
//					"Polis ["+spaj+"] sudah ditransfer ke TTP oleh " + user + " pada tanggal " + tglKirim + "", null, true);
//		}
		 
		//tambahan validasi untuk produk simas prima jikalau akseptasi khusus tidak bisa transfer
		//21/02/2008 -- Yusuf
		//jn bank =2 bahwa polis tersebut produk simas prima
		//lssa_id =10 akseptasi khusus
		Integer count=elionsManager.selectCountProductSimasPrimaAkseptasiKhusus(spaj, 1,10, isInputanBank);
		List <Map> dataInbox = uwManager.selectMstInbox(spaj,"1"); 
		/*if(dataInbox.isEmpty()){
			bacManager.prosesSnows(spaj, currentUser.getLus_id(), 202, 212);
		}*/
		Integer li_jum = this.elionsManager.selectSpajCancel(spaj);
		Integer mspo_provider = uwManager.selectGetMspoProvider(spaj);
		if(isInputanBank==13){//jika mall, tidak perlu pengecekan validasi di atas
			count = 0;
		}
	    if(mspo_provider==2){
			int jumlahEnrolmentAdmedika=uwManager.selectjumlahAdmedika(spaj);
			if(tglTerimaAdmedika==null && jumlahEnrolmentAdmedika>0){		
				m.put("error", "Tanggal Terima Admedika belum diisi. Silakan isi terlebih dahulu dengan menekan tombol TGL TERIMA ADMEDIKA pada menu UW > PRINT POLIS");
			}
	    }
//		tglKirim=
		if(spaj==null) {
			m.put("error", "Nomor SPAJ tidak ditemukan. Harap hubungi ITwebandmobile@sinarmasmsiglife.co.id dengan pesan error ini. Terima kasih.");
		}else if(tglKirim==null) {
			m.put("error", "Tanggal Kirim Spaj masih Kosong. Silahkan isi terlebih dahulu dengan menekan tombol TGL KIRIM pada menu UW > PRINT POLIS.");
	    }else if(tglPrintPolis==null){
	    	m.put("error", "Print Polis/ Sertifikat belum dilakukan. Silahkan print terlebih dahulu sebelum melakukan transfer.");
	    }else if(count>0){
		   m.put("error","Produk Simas Prima / DanamasPrima Masih Terakseptasi Khusus. Tidak Bisa di transfer");
			
		// PROJECT: POWERSAVE & STABLE LINK, rubah nih nanti, jangan cuma inputan bsm + sms, tapi semua save dan stable link
		}else if (lssaId!=5 && isInputanBank != 2 && isInputanBank != 3 && !lca_id.equals("58") && cekcek!=false && !(bisnis.equals("175") && detbisnis.equals("002"))){
			m.put("error", "Status Polis belum di AKSEPTASI. Tidak bisa transfer sebelum Update Status Polis.");
		}else if( ( (isInputanBank==2 && !bisnis.equals("120")) || (isInputanBank==3) ) && tglTerimaSpaj==null && currentUser.getLde_id().equals("11"))	{
			m.put("error", "Khusus Untuk Produk BSM selain SimPoL, Sekuritas, silakan mengisi tanggal terima SPAJ terlebih dahulu sebelum melakukan transfer.");
		}else if(!dataInbox.isEmpty()){
			Map inbox = dataInbox.get(0);
			Integer countInboxPosisiUW = uwManager.selectInboxCheckingLspdId(spaj,202);
			if ((((BigDecimal) inbox.get("LSPD_ID")).intValue()==202 || ((BigDecimal) inbox.get("LSPD_ID")).intValue()==211) && ( (products.productBsmFlowStandardIndividu(Integer.parseInt(bisnis), Integer.parseInt(detbisnis)) || li_jum>0) || ( (products.powerSave(bisnis) || products.stableLink(bisnis)) && currentUser.getLde_id().equals("11") ) ) ){
				m.put("error","Transfer Polis tidak bisa dilakukan, karena belum melakukan Transfer dokumen ke Imaging/Filling.");
			}
//			if("09,58".indexOf(lca_id.toString()) <0 && countInboxPosisiUW >0){
//				m.put("error","Silakan melakukan Upload Scan dokumen terlebih dahulu sebelum transfer.");
//			}
		}
		//validasi cek simas card untuk Powersave
		List detBisnis = elionsManager.selectDetailBisnis(spaj);
		String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
		String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
		
		Simcard simcard = uwManager.selectSimasCardBySpaj(spaj);
		List daftarSebelumnya = uwManager.selectSimasCard(spaj);
		List isAgen = uwManager.selectIsSimasCardClientAnAgent(spaj); 
		//daftarSebelumnya.removeAll(daftarSebelumnya);
//		if(simcard==null && daftarSebelumnya.isEmpty()){  #iga 03/04/2020 tidak ada lagi simascard
//				if(((lsbs.equals("143") && (lsdbs.equals("1")  || lsdbs.equals("3") || lsdbs.equals("7"))) || (lsbs.equals("144") && lsdbs.equals("1")) || lsbs.equals("183") || lsbs.equals("116")) && isAgen.isEmpty()){
//						m.put("error","Silakan masukkan No Simas Card terlebih dahulu sebelum transfer.");
//				}
//			}
		
		
			/** Transfer **/
		String trn = null;
		if(m.get("error") == null) {
			//Yusuf - 14/12/2007 - Apabila inputan bank -> maka dari print polis transfernya bukan ke komisi/filling, melainkan ke underwriting
			trn =  this.elionsManager.transferPrintPolisToFillingAtauTandaTerimaPolis(spaj, currentUser, isInputanBank);
			if(trn!=null) {
				m.put("success", "Polis berhasil di-transfer ke " + trn);
				/*
				 * nambah fungsi email
				 * dian natalia
				 */
				if(!currentUser.getLde_id().equals("11")) {
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, 0, new Date(), null, false, props.getProperty("admin.ajsjava"), new String[]{"hayatin@sinarmasmsiglife.co.id"}, null, null,  
							"Polis ["+spaj+"] sudah ditransfer", 
							"Polis ["+spaj+"] sudah ditransfer ke "+trn+" oleh " + user + " pada tanggal " + tglKirim + "", null, spaj);
				}
			} else {
				m.put("error", "Maaf, telah terjadi kesalahan dalam proses transfer. Harap hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
			}
		}

		return new ModelAndView("uw/printpolis/printpolis", m);
	}

	public ModelAndView wording(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String spaj = request.getParameter("spaj");
		Map m = new HashMap();
		if(spaj==null)
			m.put("error", "Nomor SPAJ tidak ditemukan. Harap hubungi ITwebandmobile@sinarmasmsiglife.co.id dengan pesan error ini. Terima kasih.");
		else {
			String businessId = this.uwManager.selectBusinessId(spaj);
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			m = this.referenceData(spaj, businessId, false, false, false, currentUser);
//			response.sendRedirect(request.getContextPath()+"/reports/generatepdf.pdf?spaj="+spaj);
//			return null;
		}
		m.put("spaj", spaj);
		return new ModelAndView("uw/printpolis/printpolis_generate", m);
	}

	public ModelAndView printotorisasipolis(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		Map map = new HashMap();
		Date now = this.elionsManager.selectSysdateSimple();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		map.put("tgl", now);
		map.put("penis", ServletRequestUtils.getIntParameter(request, "jenis", -1));
		map.put("status", this.elionsManager.selectStatusPolis(ServletRequestUtils.getIntParameter(request, "status", 1)));
		
		if(currentUser.getJn_bank().intValue() == 3) {
			map.put("daftarOtor", elionsManager.selectDropDown(
					"EKA.LST_USER", "LUS_ID", "LUS_LOGIN_NAME", "", "LUS_LOGIN_NAME", 
					"cab_bank = '"+currentUser.getCab_bank()+"' AND jn_bank = 3 AND lus_active = 1 AND flag_approve = 1 AND lus_id <> " + currentUser.getLus_id()));
		}else if(currentUser.getJn_bank().intValue() == 2){
			map.put("daftarOtor", elionsManager.selectDropDown(
					"EKA.LST_USER", "LUS_ID", "LUS_LOGIN_NAME", "", "LUS_LOGIN_NAME", 
					"cab_bank = '"+currentUser.getCab_bank()+"' AND jn_bank = 2 AND valid_bank_1 is null AND valid_bank_2 is null AND lus_active = 1 AND cab_bank is not null AND lus_id <> " + currentUser.getLus_id()));
		}
				
		//user menekan tombol print ulang
		if(request.getParameter("save")!=null) {
			String password = ServletRequestUtils.getStringParameter(request, "password", "");
			String ket = ServletRequestUtils.getStringParameter(request, "ket", "");
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj");
			int jenis = ServletRequestUtils.getIntParameter(request, "penis", -1);
			int alasan1 = ServletRequestUtils.getIntParameter(request, "alasan1", -1);
			int alasan2 = ServletRequestUtils.getIntParameter(request, "alasan2", -1);
			
			Map data_valid = (HashMap) this.elionsManager.select_validbank(Integer.parseInt(currentUser.getLus_id()));
			String pass_spv = (String)data_valid.get("PASS1");
			
			int lus = ServletRequestUtils.getIntParameter(request, "otorotor", -1);
			if(lus == -1) map.put("err", "Harap pilih salah satu user otorisasi terlebih dahulu");
			else pass_spv = uwManager.validationOtorisasiSekuritas(lus);

			if(map.get("err") == null) {
				//validasi2
				if(!password.equalsIgnoreCase(pass_spv)){
					map.put("err", "Password yang anda masukkan salah");
				}else {
					//proses print ulang
					try {
						
						String lsbs_id = uwManager.selectBusinessId(spaj);
						String reportUrl = request.getContextPath()+"/reports/sertifikat.pdf?spaj="+spaj;
						
						//APABILA POLIS INPUTAN BANK SINARMAS
						int jn_bank = elionsManager.selectIsInputanBank(spaj);
						if(currentUser.getCab_bank() != null && jn_bank == 2) {
							//apabila bank sinarmas, tarik counter untuk nomor sertifikat
							String sek = elionsManager.sequenceSertifikatSimasPrima();
							reportUrl += "&seri=" + sek;
							this.elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", currentUser.getLus_id(), 6, 1, "PRINT SERTIFIKAT SIMAS PRIMA("+sek+")", true, currentUser);
							
						//APABILA POLIS INPUTAN SINARMAS SEKURITAS
						}else if(currentUser.getCab_bank() != null && jn_bank == 3) {
							//apabila sinarmas sekuritas, tarik counter untuk nomor sertifikat
							String sek = elionsManager.sequenceSertifikatSimasSekuritas();
							reportUrl += "&seri=" + sek;
							this.elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", currentUser.getLus_id(), 6, 1, "PRINT SERTIFIKAT SIMAS SEKURITAS("+sek+")", true, currentUser);
								
						//APABILA STABLE LINK
						}else if(products.stableLink(lsbs_id)) {
							this.elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", currentUser.getLus_id(), 6, 1, "PRINT POLIS (E-LIONS)", true, currentUser);
						}
						
						response.sendRedirect(reportUrl);
						return null;
						
					} catch (IOException e) {
						logger.error("ERROR :", e);
					}
				}
			}
			
			map.put("password", password);
			map.put("ket", ket);
			map.put("spaj", spaj);
			map.put("jenis", jenis);
			map.put("alasan1", alasan1);
			map.put("alasan2", alasan2);
		}
		//
		
		return new ModelAndView("uw/printpolis/printpolis_oto", map);
	}
	//ryan
	public ModelAndView printulangpolis(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException, IOException{
		User user = (User) request.getSession().getAttribute("currentUser");
		Map map = new HashMap();
		Date now = this.elionsManager.selectSysdateSimple();
		
		List<DropDown> daftarJenis = new ArrayList<DropDown>();
		daftarJenis.add(new DropDown("1", "PRINT ULANG POLIS"));
		daftarJenis.add(new DropDown("2", "PRINT DUPLIKAT POLIS"));
		daftarJenis.add(new DropDown("3", "ENDORS NON MATERIAL"));
		daftarJenis.add(new DropDown("4", "PRINT ULANG SSU/SSK"));
		daftarJenis.add(new DropDown("5", "PRINT TANDA TERIMA"));
		daftarJenis.add(new DropDown("6", "MANFAAT POLIS"));
		daftarJenis.add(new DropDown("7", "SURAT POLIS"));
		daftarJenis.add(new DropDown("8", "ALOKASI DANA"));
		daftarJenis.add(new DropDown("9", "SURAT SIMCARD"));
		daftarJenis.add(new DropDown("10", "PANDUAN_VIRTUAL_ACCOUNT"));
		daftarJenis.add(new DropDown("11", "POLIS_DMTM"));
		daftarJenis.add(new DropDown("12", "KARTU_PREMI")); 
		daftarJenis.add(new DropDown("13", "SERTIFIKAT POWERSAVE/STABLE LINK"));
		daftarJenis.add(new DropDown("14", "SERTIFIKAT DUPLIKAT POWERSAVE/STABLE LINK"));
		
		List<DropDown> daftarAlasanPrintUlang = new ArrayList<DropDown>();
		daftarAlasanPrintUlang.add(new DropDown("1", "Kertas kotor, rusak"));
		daftarAlasanPrintUlang.add(new DropDown("2", "Edit Nama PP/TTG, Ahli Waris"));
		daftarAlasanPrintUlang.add(new DropDown("3", "Edit Alamat"));
		daftarAlasanPrintUlang.add(new DropDown("4", "Edit Rate"));
		daftarAlasanPrintUlang.add(new DropDown("5", "Edit Tanggal Efektif Polis"));
		daftarAlasanPrintUlang.add(new DropDown("6", "Polis belum pernah diterima nasabah")); //khusus yang ini, ada tambahan klausula di manfaatnya
		daftarAlasanPrintUlang.add(new DropDown("7", "Lain-lain"));

		List<DropDown> daftarAlasanPrintEndors = new ArrayList<DropDown>();
		daftarAlasanPrintEndors.add(new DropDown("1", "Kertas kotor, rusak"));
		daftarAlasanPrintEndors.add(new DropDown("2", "Endors alamat"));
		daftarAlasanPrintEndors.add(new DropDown("3", "Endors Ahli Waris"));
		daftarAlasanPrintEndors.add(new DropDown("4", "Endors nama PP/TTG"));
		daftarAlasanPrintEndors.add(new DropDown("5", "Lain-lain"));

		map.put("daftarJenis", daftarJenis);
		map.put("daftarAlasanPrintUlang", daftarAlasanPrintUlang);
		map.put("daftarAlasanPrintEndors", daftarAlasanPrintEndors);
		
		map.put("tgl", now);
		map.put("penis", ServletRequestUtils.getIntParameter(request, "jenis", -1));
		map.put("status", this.elionsManager.selectStatusPolis(ServletRequestUtils.getIntParameter(request, "status", 1)));
		
		//user menekan tombol print ulang
		if(request.getParameter("save")!=null) {
			String password = ServletRequestUtils.getStringParameter(request, "password", "");
			String ket = ServletRequestUtils.getStringParameter(request, "ket", "");
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj");
			int jenis = ServletRequestUtils.getIntParameter(request, "penis", -1);
			int alasan1 = ServletRequestUtils.getIntParameter(request, "alasan1", -1);
			int alasan2 = ServletRequestUtils.getIntParameter(request, "alasan2", -1);
			
			//generate sequence kalo print duplikat polis
			String seq = "";
			if(jenis == 2 || jenis ==14) seq = elionsManager.sequenceDuplikatPolis(spaj);			
			
			//validasi2
			if(!password.equalsIgnoreCase(this.elionsManager.validationVerify(1).get("PASSWORD").toString())){
				map.put("err", "Password yang anda masukkan salah");
			}else if(ket.trim().equals("")) {
				map.put("err", "Harap isi keterangan");
			}else if(jenis == -1) {
				map.put("err", "Harap pilih Jenis Print Ulang");
			}else {
				//proses print ulang
				List detBisnis = elionsManager.selectDetailBisnis(spaj);
				String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
				String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
				
				//khusus DM/TM
				if(lsbs.equals("142") && lsdbs.equals("008")) {
//					Map tmp = new HashMap();
//					tmp.put("spaj", spaj);
//					tmp.put("jenisDokumen", POLIS_QUADRUPLEX);
//					tmp.put("tipe", "HARDCOPY");
//					return new ModelAndView("pdfViewer", tmp);
				}

				String jenis_ulangan = "";
				for(DropDown d : daftarJenis) {
					if(jenis == Integer.parseInt(d.getKey())) {
						jenis_ulangan= d.getValue();
						break;
					}
				}
				if(jenis==4) {
					 Integer printType=9;
					 if( SYARAT_UMUM_KHUSUS == printType || SYARAT_UMUM_KHUSUS_EAS == printType || SYARAT_KHUSUS == printType) {
							Map map1 = new HashMap();
							map1.put("spaj", spaj);
							map1.put("jenisDokumen", printType);
							return new ModelAndView("pdfViewer", map1);
					 
					 }

					}
				 if(jenis==5) {
					 Integer printType=7;
					 if( TANDA_TERIMA_POLIS == printType) {
						 String format = ".pdf";
						if(format.toLowerCase().endsWith(".pdf")) {
							if(request.getParameter("generate")!=null) {
									format = ".pdf?";
							}else {
									format = ".pdf?print=true&";
							}
						 int referal=0;
						String reportUrl = request.getContextPath()+"/reports/tandaterimapolis"+format+"spaj="+spaj+"&referal="+referal;
						response.sendRedirect(reportUrl);
						}
					 }else if( TANDA_TERIMA_POLIS_DUPLIKAT == printType) {
						 String format = ".pdf";
						if(format.toLowerCase().endsWith(".pdf")) {
							if(request.getParameter("generate")!=null) {
									format = ".pdf?";
							}else {
									format = ".pdf?print=true&";
							}
						 int referal=0;
						String reportUrl = request.getContextPath()+"/reports/tandaterimapolisduplikat"+format+"spaj="+spaj+"&referal="+referal;
						response.sendRedirect(reportUrl);
						}
					 }

					}
				 if(jenis==6) {
					 Integer printType=3;
					 if( MANFAAT == printType) {
						 String format = ".pdf";
						if(format.toLowerCase().endsWith(".pdf")) {
							if(request.getParameter("generate")!=null) {
									format = ".pdf?";
							}else {
									format = ".pdf?print=true&";
							}
						int referal=0;
						String	reportUrl = request.getContextPath()+"/reports/manfaat"+format+"spaj="+spaj;
						response.sendRedirect(reportUrl);
						}
					 }

					}
				 if(jenis==7) {
					 Integer printType=4;
					 if( SURAT_POLIS == printType ) {
						 String format = ".pdf";
						if(format.toLowerCase().endsWith(".pdf")) {
							if(request.getParameter("generate")!=null) {
									format = ".pdf?";
							}else {
									format = ".pdf?print=true&";
							}
						int referal=0;
						
						String reportUrl = request.getContextPath()+"/reports/suratpolis"+format+"spaj="+spaj;
						response.sendRedirect(reportUrl);
						}
					 }}	
				 if(jenis==8) {
					 Integer printType=5;
					 if( ALOKASI_DANA == printType ) {
						 String format = ".pdf";
						if(format.toLowerCase().endsWith(".pdf")) {
							if(request.getParameter("generate")!=null) {
									format = ".pdf?";
							}else {
									format = ".pdf?print=true&";
							}
						int referal=0;
						String reportUrl = request.getContextPath()+"/reports/alokasidana"+format+"spaj="+spaj;
						response.sendRedirect(reportUrl);
						}
					 }}	
				 if(jenis==9) {
					 Integer printType=14;
					 if( SURAT_SIMCARD == printType ) {
						 String format = ".pdf";
						if(format.toLowerCase().endsWith(".pdf")) {
							if(request.getParameter("generate")!=null) {
									format = ".pdf?";
							}else {
									format = ".pdf?print=true&";
							}
						int referal=0;
						String reportUrl = request.getContextPath()+"/reports/surat_simcard"+format+"spaj="+spaj;
						response.sendRedirect(reportUrl);
						}
					 }}	
				 
					 if(jenis==10) {
						 Integer printType=16;
						 if( PANDUAN_VIRTUAL_ACCOUNT == printType ) {
							 Map map2 = new HashMap();
								map2.put("spaj", spaj);
								map2.put("jenisDokumen", PANDUAN_VIRTUAL_ACCOUNT);
								return new ModelAndView("pdfViewer", map2);
							}
						 }
					 
					 if(jenis==11) {
						 Integer printType=16;
						 if( POLIS_DMTM == printType ) {
							 List tmp = elionsManager.selectDetailBisnis(spaj);
							 lsbs = (String) ((Map) tmp.get(0)).get("BISNIS");
							lsdbs = (String) ((Map) tmp.get(0)).get("DETBISNIS");
						if(lsbs.equals("142") && lsdbs.equals("008")) {
							//boleh
						}else {
							List errors = new ArrayList();
							errors.add("Menu ini hanya dapat digunakan untuk Polis DM/TM");
							return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
						}
						Map map3 = new HashMap();
						map3.put("spaj", spaj);
						if(lsbs.equals("142") && lsdbs.equals("008")) {
							map3.put("jenisDokumen", POLIS_QUADRUPLEX);
						}else{
							map3.put("jenisDokumen", POLIS_DMTM);
						}
						map3.put("tipe", "HARDCOPY");
						return new ModelAndView("pdfViewer", map);
						 }
					}
					 if(jenis==12) {
						 Integer printType=8;
						 if(KARTU_PREMI == printType ) {
							 String format = ".pdf";
							if(format.toLowerCase().endsWith(".pdf")) {
								if(request.getParameter("generate")!=null) {
										format = ".pdf?";
								}else {
										format = ".pdf?print=true&";
								}
							int referal=0;
							String reportUrl = request.getContextPath()+"/reports/kartupremi"+format+"spaj="+spaj;
							response.sendRedirect(reportUrl);
							}
						 }}
					 if(jenis==13 || jenis==14) {
						 Integer printType=6;
						 if(SERTIFIKAT_POWERSAVE == printType ) {
							 String format = ".pdf";
							if(format.toLowerCase().endsWith(".pdf")) {
								if(request.getParameter("generate")!=null) {
										format = ".pdf?";
								}else {
										format = ".pdf?print=true&";
								}
							int referal=0;
							String reportUrl = request.getContextPath()+"/reports/sertifikat"+format+"spaj="+spaj+"&referal="+referal+"&seq="+seq;
							response.sendRedirect(reportUrl);
							}
						 }}
					
				this.uwManager.insertLst_ulangan(
						spaj, now, jenis_ulangan, ServletRequestUtils.getIntParameter(request, "_status"), user.getLus_id(), ket + (!seq.equals("")?(" (" + seq + ")"):""));

				//Yusuf - 15 okt 08 - request mba asri, untuk semua jenis cetak ulang, tanggal cetak diupdate tanggal cetak terbaru
//				this.elionsManager.updateMst_policyPrintDate(spaj, "mspo_date_print");

				//HAPUS FILE SEBELUMNYA
				if (jenis!=5&&jenis!=6&&jenis!=8){
					if (jenis==2){
						String mi_id = "", statusAksep = "UW TELAH MELAKUKAN PRINT DUPLIKAT POLIS";						
						Integer lspd_id_from= 0, lspd_id_from2 = 0; 
						List <Map> mapInbox = uwManager.selectMstInbox(spaj, "6");	
						HashMap inbox = (HashMap) mapInbox.get(0);
						if(mapInbox!=null){
							mi_id = (String) inbox.get("MI_ID");
							lspd_id_from = ((BigDecimal) inbox.get("LSPD_ID")).intValue();
							lspd_id_from2 = 203;
							if(lspd_id_from==202){
								bacManager.updateMstInboxLspdId(mi_id, lspd_id_from2, lspd_id_from, 1, null, null, null, statusAksep, 0);
								MstInboxHist mstInboxHist = new MstInboxHist(mi_id, lspd_id_from, lspd_id_from2, null, null, statusAksep, Integer.parseInt(user.getLus_id()), now, null,0,0);
								bacManager.insertMstInboxHist(mstInboxHist);					
							}
						}
						File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+
							elionsManager.selectCabangFromSpaj(spaj)+"\\"+spaj+"\\"+"polis_all_duplicate.pdf");
						if(userDir.exists()) {
							userDir.delete();
						}
					} else {
						File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+
							elionsManager.selectCabangFromSpaj(spaj)+"\\"+spaj+"\\"+"polis_all.pdf");
						if(userDir.exists()) {
							userDir.delete();
						}
					}
					
				}//RYAN
				if (jenis==13){
					File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+
						elionsManager.selectCabangFromSpaj(spaj)+"\\"+spaj+"\\"+"sertifikat_powersave.pdf");
					if(userDir.exists()) {
						userDir.delete();
					}
				}
				if (jenis==14){
					File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+
						elionsManager.selectCabangFromSpaj(spaj)+"\\"+spaj+"\\"+"sertifikat_powersave_duplicate.pdf");
					if(userDir.exists()) {
						userDir.delete();
					}
				}
				//bila link, update
				if(products.unitLink(lsbs) && !products.stableLink(lsbs)) {
					this.elionsManager.updateUlink(3, spaj, df2.format(this.elionsManager.selectMuTglTrans(spaj)));
				}
				
				String ketek = "";
				if(alasan1 > -1) {
					for(DropDown d : daftarAlasanPrintUlang) {
						if(alasan1 == Integer.parseInt(d.getKey())) {
							ketek = d.getValue();
							break;
						}
					}
				}else if(alasan2 > -1) {
					for(DropDown d : daftarAlasanPrintEndors) {
						if(alasan2 == Integer.parseInt(d.getKey())) {
							ketek = d.getValue();
							break;
						}
					}
				}
				ketek = jenis_ulangan + " (" + ketek + ": " + ket + ")";
				
//				this.elionsManager.insertMstPositionSpaj(spaj, ketek.toUpperCase(), user.getLus_id());
				
				try {
					//jpu = jenis print ulang
					if(jenis!=13 && jenis!=14){
						
						//cek program hadiah apa bukan
						Integer hadiah = 0;
						Product produk = uwManager.selectMstProductInsuredUtamaFromSpaj(spaj);
						if(produk.getLsbs_id()==184 && produk.getLsdbs_number()==6){
							Tertanggung ttg = elionsManager.selectttg(spaj);
							int mgi = uwManager.selectMasaGaransiInvestasi(spaj,1,1);
							double pr = produk.getMspr_premium();
							long premi =  (long) pr;
							//if(ttg.getMste_flag_hadiah()==1 && premi >= 70000000 && mgi==36){
							if(ttg.getMste_flag_hadiah()==1 && mgi==36){
								Map team_name = uwManager.selectInfoAgen2(spaj);
								
								String lca_id = uwManager.selectLcaIdMstPolicyBasedSpaj(spaj);
								
								hadiah = 1;
								/*if(team_name!=null){
									String team = (String)team_name.get("TEAM");
									if(team.equals("Team Yanti Sumirkan")){
										hadiah = 1;
									}
								}else if(lca_id.equals("58")){
									hadiah = 1;
								}*/
							}
						}
						Integer i_countMedPlus=bacManager.selectMedPlusRiderAddon(spaj);
						
						if(hadiah==0 && i_countMedPlus==0){
							response.sendRedirect(request.getContextPath()+"/reports/polis_quadruplex.pdf?seq="+seq+"&jpu="+jenis+"&print=true&tamb="+alasan1+"&spaj="+spaj);
						}else if(hadiah>0){
							response.sendRedirect(request.getContextPath()+"/reports/polis_quadruplex_plus_hadiah.pdf?seq="+seq+"&jpu="+jenis+"&print=true&tamb="+alasan1+"&spaj="+spaj);
						}else if(i_countMedPlus>0){
							response.sendRedirect(request.getContextPath()+"/reports/polis_quadruplex_medical_plus.pdf?seq="+seq+"&jpu="+jenis+"&print=true&tamb="+alasan1+"&spaj="+spaj);
						}
						
						
					//response.sendRedirect(request.getContextPath()+"/reports/polis_quadruplex.pdf?seq="+seq+"&jpu="+jenis+"&print=true&tamb="+alasan1+"&spaj="+spaj);
					}
				} catch (IOException e) {
					logger.error("ERROR :", e);
				}
			}
			
			map.put("password", password);
			map.put("ket", ket);
			map.put("spaj", spaj);
			map.put("jenis", jenis);
			map.put("alasan1", alasan1);
			map.put("alasan2", alasan2);
		}
		//
		
		return new ModelAndView("uw/printpolis/printpolis_ulang", map);
	}

	/*
	public ModelAndView printulangpolis(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		Map map = new HashMap();
		//map.put("p_w", this.elionsManager.validationVerify(tipe).get("PASSWORD").toString());

		if(request.getParameter("save")!=null || request.getParameter("regenerate")!=null) {
			String password = ServletRequestUtils.getStringParameter(request, "password", "");
			String ket = ServletRequestUtils.getStringParameter(request, "ket", "");
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj");

			Date tglCetak = elionsManager.selectPrintDatePolis(spaj);
			Calendar tgl_mulai_ssu = new GregorianCalendar(2006, 8, 20);
			String lsbs_id = elionsManager.selectBusinessId(spaj);
			
			// DI-DISABLE KARENA SERING DI-ENDORSE
//			if(tglCetak != null) {
//				if(tglCetak.before(tgl_mulai_ssu.getTime()) && elionsManager.selectSpajCancel(spaj)==0 && !lsbs_id.equals("140")) {
//					map.put("err", "Tidak ada history Cover Polis");
//				}
//			}
			
			if(!map.isEmpty()) {
				
			}else if(!password.equalsIgnoreCase(this.elionsManager.validationVerify(1).get("PASSWORD").toString())){
				map.put("err", "Password yang anda masukkan salah");
			}else if(ket.trim().equals("")) {
				map.put("err", "Harap isi keterangan");
			}else {

				List detBisnis = elionsManager.selectDetailBisnis(spaj);
				String elesbees = (String) ((Map) detBisnis.get(0)).get("BISNIS");
				String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
				//khusus DM/TM
				if(elesbees.equals("142") && lsdbs.equals("008")) {
					Map tmp = new HashMap();
					tmp.put("spaj", spaj);
					tmp.put("jenisDokumen", POLIS_DMTM);
					tmp.put("tipe", "HARDCOPY");
					return new ModelAndView("pdfViewer", tmp);
				}

				User user = (User) request.getSession().getAttribute("currentUser");

				//TODO (Yusuf) -> UNTUK DEBUGGING, DISABLE
				this.elionsManager.insertLst_ulangan(
						spaj, this.elionsManager.selectSysdateSimple(),
						(ServletRequestUtils.getIntParameter(request, "jenis", -1)==1?"PRINT DUPLIKAT POLIS":""),
						ServletRequestUtils.getIntParameter(request, "_status"), user.getLus_id(), ket);
				//REQUEST DR. INGRID (12 OKT 2006), GAK DI GENERATE ULANG
				//this.elionsManager.updateMst_policyPrintDate(spaj, "mspo_date_print");
				String lsbs = elionsManager.selectBusinessId(spaj);
				if(products.unitLink(lsbs_id) && !lsbs.equals("164")) {
					this.elionsManager.updateUlink(3, spaj, df2.format(this.elionsManager.selectMuTglTrans(spaj)));
				}

				if(request.getParameter("regenerate")!=null) {
					//HAPUS FILE SEBELUMNYA
					File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+
							elionsManager.selectCabangFromSpaj(spaj)+"\\"+spaj+"\\"+"polis_all.pdf");
					if(userDir.exists()) {
						userDir.delete();
					}
					//UPDATE TANGGAL CETAK
					this.elionsManager.updateMst_policyPrintDate(spaj, "mspo_date_print");
				}
				
				try {
					//TODO (Yusuf) -> Tambahin query string d=true apabila ingin header 'ORIGINAL' dihilangkan
					response.sendRedirect(request.getContextPath()+"/reports/polis_quadruplex.pdf?print=true&spaj="+spaj);
				} catch (IOException e) {
					logger.error("ERROR :", e);
				}
			}
		}

		map.put("tgl", this.elionsManager.selectSysdateSimple());
		map.put("jenis", (ServletRequestUtils.getIntParameter(request, "jenis", -1)==1?"PRINT DUPLIKAT POLIS":""));
		map.put("status", this.elionsManager.selectStatusPolis(ServletRequestUtils.getIntParameter(request, "status", 1)));
		
		return new ModelAndView("uw/printpolis/printpolis_ulang", map);
	}
	*/
	
	/**
	 * Viewer untuk SSU, di-link dari aplikasi nya eka, untuk product info
	 * 
	 * @author Yusuf
	 * @since Jul 1, 2008 (11:39:28 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView viewer_ss(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String file = ServletRequestUtils.getStringParameter(request, "file", "");
		String direktori = props.getProperty("pdf.dir.syaratpolis");
		String lsbs_id = FormatString.rpad("0", ServletRequestUtils.getStringParameter(request, "lsbs", ""), 3);
		
		if(!file.equals("")) {
			FileUtils.downloadFile("inline;", direktori, file, response);
			return null;
		}else {
			Map cmd = new HashMap();
			List<DropDown> daftarFile = FileUtils.listFilesInDirectoryStartsWith(direktori, lsbs_id);
			cmd.put("daftarFile", daftarFile);
			return new ModelAndView("uw/printpolis/viewer_ss", cmd);
		}
	}
	
	public ModelAndView print(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int printType = ServletRequestUtils.getRequiredIntParameter(request, "printType");
		String spaj = request.getParameter("spaj");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Pemegang pmg =elionsManager.selectpp(spaj);
		Date feb01 = defaultDateFormat.parse("01/02/2012");
		boolean isValidated=true;
		boolean isBancass = ServletRequestUtils.getBooleanParameter(request, "isbancass", false);
		boolean isViewer = ServletRequestUtils.getBooleanParameter(request, "isviewer", false);
		String format = ServletRequestUtils.getStringParameter(request, "format", "");
		int isInputanBank= -1;
		isInputanBank = elionsManager.selectIsInputanBank(spaj);
		Integer flag = uwManager.flag_upload_scan(spaj);
		//efektif yg upload scan berjalan setelah tgl 1 feb 2012, sebelum tgl itu, tidak liat ke flag_upload_scan
		if (flag == 0 && isInputanBank == -1 && isViewer==false && pmg.getMspo_input_date().after(feb01) && pmg.getMspo_plan_provider()==null){
			List errors = new ArrayList();
			errors.add("Tidak dapat print polis karena belum melakukan proses upload scan.");
			return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
		}
		else{
		//kalo dipanggil dari programnya EAS, langsung tembus aja
		if(SYARAT_UMUM_KHUSUS_EAS == printType){
			currentUser = new User();
			currentUser.setLca_id("01");
		}
		
		if(currentUser.getLca_id().equals("58")){
			String cabang = elionsManager.selectCabangFromSpaj(spaj);
			String path = props.getProperty("pdf.dir.export").trim() + "\\" + 
							cabang.trim() + "\\" + 
							spaj.trim() + "\\" + 
							spaj.trim() + "BSB 001.pdf";
			File file = new File(path);
			if(!file.exists()){
				List errors = new ArrayList();
				errors.add("Maaf, tetapi anda tidak bisa melakukan pencetakan apabila belum melakukan upload BSB.");
				return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
			}
		}

		//VALIDASI DI DEPAN, KHUSUS UNTUK PRINT POLIS CABANG
		String cabang = currentUser.getLca_id();
		if("01, 11, 12,55,58,37".indexOf(cabang.trim())==-1 && currentUser.getCab_bank().equals("")) {
			if(uwManager.validationPrintPolisCabang(spaj) != 1) {
				List errors = new ArrayList();
				errors.add("Maaf, tetapi anda tidak bisa melakukan pencetakan. Silahkan konfirmasi dengan bagian UNDERWRITING terlebih dahulu.");
				return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
			}
		}
		
		if(POLIS_DMTM != printType && SURAT_POLIS != printType) {
			//validasi polis DM/TM, baru bisa produk 142-8 saja
			List detBisnis = elionsManager.selectDetailBisnis(spaj);
			String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
//			if(lsbs.equals("142") && lsdbs.equals("008")) {
//				List errors = new ArrayList();
//				errors.add("Untuk Polis DM/TM, anda hanya dapat mencetak [POLIS KHUSUS PRODUK DM/TM] dan [SURAT UCAPAN TERIMA KASIH]");
//				return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
//			}
		}
		
		if(ENDORS_EKASEHAT_ADMEDIKA == printType){
			Integer isEndorsAdmedika = uwManager.selectGetMspoProvider(spaj);
			if(isEndorsAdmedika!=null){
				if(isEndorsAdmedika.intValue()!=2){
					List errors = new ArrayList();
					errors.add("Endors Eka Sehat Hanya bisa diprint apabila case Eka Sehat Admedika");
					return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
				}
			}else{
				List errors = new ArrayList();
				errors.add("Endors Eka Sehat Hanya bisa diprint apabila case Eka Sehat Admedika");
				return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
			}
		}
		
		if(ENDORSEMEN == printType || SYARAT_KHUSUS == printType){
			//if(uwManager.selectCountSwineFlu(spaj)==0){
			List detBisnis = elionsManager.selectDetailBisnis(spaj);
			String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
			if(products.powerSave(lsbs) || products.stableLink(lsbs) || products.stableSave(Integer.parseInt(lsbs), Integer.parseInt(lsdbs)) || lsbs.equals("183") || lsbs.equals("189") || lsbs.equals("193")){
				if(lsbs.equals("183") || lsbs.equals("189") || lsbs.equals("193")){
					List RiderSwineFlu = uwManager.selectswineflu(spaj);
					if(RiderSwineFlu.size()==0){
						List errors = new ArrayList();
						//errors.add("Endorsemen hanya bisa dipilih apabila Polis tersebut Include dengan rider Swine Flu-A(H1N1)");
						errors.add("Endorsemen/SSK Untuk produk Eka Sehat Stand Alone hanya bisa diprint Apabila memiliki rider Swine Flu");
						return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
					}
				}else{
					if(detBisnis.size()==1){
						List errors = new ArrayList();
						//errors.add("Endorsemen hanya bisa dipilih apabila Polis tersebut Include dengan rider Swine Flu-A(H1N1)");
						errors.add("Endorsemen/SSK hanya bisa dipilih apabila ada rider/asuransi tambahan Untuk produk utama Jenis Powersave, Stable Link, dan Stable Save");
						return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
					}else{
						Integer msen_auto_riderSwineFlu = uwManager.selectMstEndorsAutoRider(spaj);
						if(msen_auto_riderSwineFlu!=null){
							if(msen_auto_riderSwineFlu==2){
								Integer total_rider = uwManager.selectRiderSave(spaj).size();
								if(total_rider==0){
									List errors = new ArrayList();
									errors.add("Untuk Swine Flunya,silakan print di Endorsemen (Menu Input Top-Up Stable Link)");
									return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
								}
							}else if(msen_auto_riderSwineFlu==3){
								Integer total_rider = uwManager.selectRiderSave(spaj).size();
								if(total_rider==0){
									List errors = new ArrayList();
									errors.add("Untuk Swine Flunya,Silakan print Endorsemen di bagian RollOver");
									return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
								}
							}
						}
					}
						
				}
			}else{
				List RiderSwineFlu = uwManager.selectswineflu(spaj);
				if(RiderSwineFlu.size()==0){
						List errors = new ArrayList();
						errors.add("Endorsemen/SSK hanya bisa dipilih Untuk produk utama Jenis Powersave, Stable Link, Stable Save, Dan Eka Sehat Stand Alone");
						return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
				}else{
					if(products.unitLinkNew(lsbs)){
						List errors = new ArrayList();
						errors.add("Endorsemen/SSK hanya bisa dipilih Untuk produk utama Jenis Powersave, Stable Link, Stable Save, Dan Eka Sehat Stand Alone");
						return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
					}
				}
			}
			//}
		}
		
		Map detBisnis = (Map) elionsManager.selectDetailBisnis(spaj).get(0);
		String businessId = (String) detBisnis.get("BISNIS");
		String lsbs_name = (String) detBisnis.get("LSBS_NAME");
		String lsdbs_name = (String) detBisnis.get("LSDBS_NAME");
		String lsdbs = (String) detBisnis.get("DETBISNIS");
		
		format = ServletRequestUtils.getStringParameter(request, "format", "");

		if(POLIS_DMTM == printType && format.toLowerCase().endsWith(".html")) {
			List errors = new ArrayList();
			errors.add("Untuk Polis DM/TM, tidak bisa preview");
			return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
		}
		
		//untuk simas prima referal, yang mau dicetaknya dalam bentuk polis
		int referal = ServletRequestUtils.getIntParameter(request, "referal", 0);
		
		if( (SYARAT_UMUM_KHUSUS == printType || SYARAT_UMUM_KHUSUS_EAS == printType || SYARAT_KHUSUS == printType) && ServletRequestUtils.getBooleanParameter(request, "isviewer", false)==false) { //untuk lihat SSU/SSK, validasi tanggalnya saja
			isValidated = true;
		}else if(format.toLowerCase().endsWith(".pdf")) {
			if(request.getParameter("generate")!=null) {
				format = ".pdf?";
			}else if(SYARAT_UMUM_KHUSUS == printType || SYARAT_UMUM_KHUSUS_EAS == printType || SYARAT_KHUSUS == printType){
				format = ".pdf?print=false&";
			}else {
				format = ".pdf?print=true&";
			}
			if(request.getParameter("e") != null) {
				if(request.getParameter("e").equals("w")) isValidated=false;
			}
		}else if(format.toLowerCase().endsWith(".html")) {
			format = ".html?attached=1&";
			isValidated = false;
		}else if(format.toLowerCase().endsWith(".jasper")) {
			format = ".jasper?";
			isValidated = false;
		}

		//Print ALL
		//update (LUFI -OKT 2015)Bagian program ini digunakan untuk cetak polis all(polis,manfaat,alokasi dana,ssu,endors,ttp,suratsimascard)
		if( ALL == printType) {
			
//			if(request.getParameter("finish")!=null) 
//				return new ModelAndView("uw/printpolis/printpolis_all").addObject("finish", "true");
//			
			Map parameters = this.referenceData(spaj, businessId, true, false, false, currentUser);
			
//			
//			int printProgress = ServletRequestUtils.getRequiredIntParameter(request, "printProgress");
//			if(request.getParameter("next")!=null || request.getParameter("start")!=null) 
//				printProgress = nextProgress(printProgress, (List) parameters.get("reportList"));
//			
//			List errors = validasi(printProgress, spaj, businessId, request);
//
//			if(request.getParameter("ulang")!=null) printProgress = POLIS_QUADRUPLEX;
//			else if(request.getParameter("finish")!=null) printProgress = FINISH;
//						
//			parameters.put("errors", errors);
//			if(errors.size()>0) printProgress = prevProgress(printProgress, (List) parameters.get("reportList"));
//			parameters.put("printProgress", String.valueOf(printProgress));//			
//			return new ModelAndView("uw/printpolis/printpolis_all", parameters);
			
			if(products.stableLink(businessId) || (businessId.equals("187") || businessId.equals("196")&&!lsdbs.equals("002") || businessId.equals("073")&& lsdbs.equals("008")) 
				|| (businessId.equals("142")&& lsdbs.equals("002")) || (businessId.equals("142")&& lsdbs.equals("008"))) {
				List errors = new ArrayList();
				errors.add("Produk ini tidak bisa menggunakan menu Print All");
				return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
			}else if(businessId.equals("223")){ //request bu titis product 223 bukan sertifikat| iga 02072019
//				List errors = new ArrayList();
//				errors.add("Produk ini tidak ada Cover Polis dan Manfaat. Silahkan gunakan pilihan SERTIFIKAT TERM ROP");
//				return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
				return new ModelAndView("uw/printpolis/printpolis_all", parameters);
			}			
			
			List validasi = new ArrayList();
			if(isValidated) {
				validasi = validasi(printType, spaj, businessId, request);
			}
			String reportUrl = "";
			if(validasi.size() > 0)return new ModelAndView("uw/printpolis/printpolis_err", "error", validasi);
			if(products.unitLink(businessId) && !products.stableLink(businessId)) {
				this.elionsManager.updateUlink(3, spaj, df2.format(this.elionsManager.selectMuTglTrans(spaj)));
			}
			if(isValidated && !isBancass) {
				//this.elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", currentUser.getLus_id(), 6, 1, "PRINT POLIS ALL(E-LIONS)", true, currentUser);
				response.sendRedirect(request.getContextPath()+"/reports/printall.pdf?spaj="+spaj+"&flag="+2);
			}
			return null;
		}
		if( GENERATE_OUTSOURCE == printType ){	
			printType = ServletRequestUtils.getRequiredIntParameter(request, "printType");
			spaj = request.getParameter("spaj");
			currentUser = (User) request.getSession().getAttribute("currentUser");
			pmg =elionsManager.selectpp(spaj);
			
			isValidated=true;
			isBancass = ServletRequestUtils.getBooleanParameter(request, "isbancass", false);
			isViewer = ServletRequestUtils.getBooleanParameter(request, "isviewer", false);
			format = ServletRequestUtils.getStringParameter(request, "format", "");
			isInputanBank= -1;
			isInputanBank = elionsManager.selectIsInputanBank(spaj);
			flag = uwManager.flag_upload_scan(spaj);
				
			detBisnis = (Map) elionsManager.selectDetailBisnis(spaj).get(0);
			businessId = (String) detBisnis.get("BISNIS");
			lsbs_name = (String) detBisnis.get("LSBS_NAME");
			lsdbs_name = (String) detBisnis.get("LSDBS_NAME");
			lsdbs = (String) detBisnis.get("DETBISNIS");
			int lsdbsNumber = Integer.parseInt(lsdbs);
			
			format = ServletRequestUtils.getStringParameter(request, "format", "");
			
//			Boolean listExclude = null;
//			//listExclude = ((businessId.equals("142")) && ("002,004,006,007".indexOf(Integer.parseInt(lsdbs)) > 0))
//			listExclude = ((businessId.equals("142")) && ("002,004,006,007".indexOf(Integer.toString(lsdbsNumber)) > 0))				
//			|| (businessId.equals("175") && (lsdbs.equals("002")))
//			|| (businessId.equals("212") && (lsdbs.equals("006")))
//			|| (businessId.equals("223") && (lsdbs.equals("001"))) //Smile Life Syariah 								
//			|| (businessId.equals("187")); //PAS			
			
			boolean listExclude = 
					//Bancass
					(businessId.equals("142") && ("002,004,006,007".indexOf(lsdbs)>-1)) //SIMAS PRIMA
					|| (businessId.equals("175") && lsdbs.equals("002")) //POWER SAVE SYARIAH BSM
					|| (businessId.equals("212") && lsdbs.equals("006")) //SMART LIFE PROTECTION (BUKOPIN)
					|| (businessId.equals("223") && lsdbs.equals("001")) //Smile Life Syariah
					//DMTM
					|| (businessId.equals("187") && ("011,012,013,014".indexOf(lsdbs)>-1)) //SMART ACCIDENT CARE
					|| (businessId.equals("205") && ("005,006,007,008".indexOf(lsdbs)>-1)) //PAS SYARIAH
					|| (businessId.equals("197") && lsdbs.equals("002")) //CI CANCER
					|| (businessId.equals("203")) //DEMAM BERDARAH
					|| (businessId.equals("73") && ("014,015".indexOf(lsdbs)>-1)) //PA
					|| (businessId.equals("212") && lsdbs.equals("008")); //SMILE LIFE PRO			
			
			if(listExclude) {
				List error = new ArrayList();
				error.add("Produk ini tidak bisa menggunakan menu Generate Outsource");
				return new ModelAndView("uw/printpolis/printpolis_err", "error", error);
			}	
			
			//Cek Kelengkapan
			List errors = new ArrayList();
			cabang = elionsManager.selectCabangFromSpaj(spaj);
			String path ="";
			String pathTemp = "";
			path = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
			pathTemp = props.getProperty("pdf.dir")+"\\"+cabang+"\\"+spaj;				
			File destDir = new File(path);
			if(!destDir.exists()) {		
				errors.add("File-file Pendukung belum ada.");				
				return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
			}
				
			List validasi = new ArrayList();
			if(isValidated) {
				validasi = validasi(printType, spaj, businessId, request);				
			}
			String reportUrl = "";
			if(validasi.size() > 0)return new ModelAndView("uw/printpolis/printpolis_err", "error", validasi);
			if(products.unitLink(businessId) && !products.stableLink(businessId)) {
				elionsManager.updateUlink(3, spaj, df2.format(elionsManager.selectMuTglTrans(spaj)));
			}

			if(isValidated && !isBancass) {
				
				String hasilGenerate = null;
				Connection conn = null;			
				conn = bacManager.getUwDao().getDataSource().getConnection();
				ServletContext context = this.getServletContext();
				AutoGenerateController aGenerate = new AutoGenerateController();
				hasilGenerate = aGenerate.generateAllRds(context, request, response, elionsManager, uwManager, bacManager, props, products);			
				if (hasilGenerate == null){
					List msgSukses = new ArrayList();
					msgSukses.add("Proses Generate Outsource Sukses.");
					return new ModelAndView("uw/printpolis/printpolis_message", "error", msgSukses);			
				}else{
					errors.add("Proses Generate Outsource Gagal.");
					return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
				}
					
			}
		}		
		//Mark Valentino 20180906 Menu Generate All Hanya Untuk Product : Simas Magna Link
		if( GENERATE_ALL == printType) {
			List error = new ArrayList();
//			error.add("Maaf untuk sementara menu Generate All dinonaktifkan.");
//			return new ModelAndView("uw/printpolis/printpolis_err", "error", error);			
			AutoGenerate aGenerate = new AutoGenerate();
			return aGenerate.generateAll(request, response, elionsManager, uwManager, bacManager, props, products);
		}else {
			List validasi = new ArrayList();
			if(isValidated) {
				validasi = validasi(printType, spaj, businessId, request);
			}
			
			String reportUrl = "";
			
			if(request.getParameter("ulang")!=null) printType= POLIS_QUADRUPLEX;
			else if(request.getParameter("finish")!=null) printType= FINISH;
			
			if(validasi.size() > 0) {
				return new ModelAndView("uw/printpolis/printpolis_err", "error", validasi);

			}else if( POLIS == printType || POLIS_DUPLEX == printType || POLIS_QUADRUPLEX == printType ) {
				
				if(products.stableLink(businessId)) {
					List errors = new ArrayList();
					errors.add("Produk Stable Link tidak ada Cover Polis dan Manfaat. Silahkan gunakan pilihan SERTIFIKAT");
					return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
				}
				
				if(businessId.equals("187") || businessId.equals("196")&&!lsdbs.equals("002") || businessId.equals("073")&& lsdbs.equals("008")){
					List errors = new ArrayList();
					errors.add("Produk Term/PA/PAS tidak ada Cover Polis dan Manfaat. Silahkan gunakan pilihan POLIS KHUSUS PAS/PA/TERM");
					return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
//				}else if(businessId.equals("223")){ //request bu titis product 223 bukan sertifikat| iga 02072019
//					List errors = new ArrayList();
//					errors.add("Produk ini tidak ada Cover Polis dan Manfaat. Silahkan gunakan pilihan SERTIFIKAT TERM ROP");
//					return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
					
				}
				
				//TODO (Yusuf) -> UNTUK DEBUGGING, DISABLE
				if(isValidated && !isBancass) {
					this.elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", currentUser.getLus_id(), 6, 1, "PRINT POLIS (E-LIONS)", true, currentUser);
					//SEND E-MAIL KE ADMIN
//					email.send(
//							new String[] {props.getProperty("admin.yusuf")}, null,
//							"Print Polis [" +lsbs_name +"-"+ lsdbs_name+ "] dengan SPAJ " + spaj + " oleh "+ currentUser.getName() + " ["+currentUser.getDept()+"]",
//							spaj, currentUser);			
				}
				String lsbs_id = uwManager.selectBusinessId(spaj);
				if(POLIS_QUADRUPLEX == printType && products.unitLink(lsbs_id) && !products.stableLink(lsbs_id)) {
					this.elionsManager.updateUlink(3, spaj, df2.format(this.elionsManager.selectMuTglTrans(spaj)));
				}
				
				String jenisPolis;
				
				switch(printType) {
					case POLIS : jenisPolis = "polis";break;
					case POLIS_DUPLEX : jenisPolis = "polis_duplex";break;
					case POLIS_QUADRUPLEX : jenisPolis = "polis_quadruplex";break;
					default: jenisPolis="polis";
				}
				Integer i_countMedPlus=bacManager.selectMedPlusRiderAddon(spaj);
				
				if(i_countMedPlus>0){
					printType = POLIS_QUADRUPLEX_MEDICAL_PLUS;
					jenisPolis = "polis_quadruplex_medical_plus";
				}
				//jika program hadiah dan tutupan bancass 1
				// tmabahan khusus polis Medical Plus dengan peserta banyak
				if(jenisPolis.equals("polis_quadruplex")){
					Product produk = uwManager.selectMstProductInsuredUtamaFromSpaj(spaj);
					
					
					if(produk.getLsbs_id()==184){
						Tertanggung ttg = elionsManager.selectttg(spaj);
						int mgi = uwManager.selectMasaGaransiInvestasi(spaj,1,1);
						double pr = produk.getMspr_premium();
						long premi =  (long) pr;
						if(ttg.getMste_flag_hadiah()==1 && premi >= 70000000 && mgi==36){
							Map team_name = uwManager.selectInfoAgen2(spaj);
							
							if(team_name!=null){
								String team = (String)team_name.get("TEAM");
								if(team.equals("Team Yanti Sumirkan")){
									printType = POLIS_QUADRUPLEX_PLUS_HADIAH;
									jenisPolis = "polis_quadruplex_plus_hadiah";
								}
							}
							/*if(team_name!=null){
								if(team_name.toUpperCase().equals("TEAM YANTI SUMIRKAN")){
									printType = POLIS_QUADRUPLEX_PLUS_HADIAH;
									jenisPolis = "polis_quadruplex_plus_hadiah";
								}
							}*/
						}
					}
				}
				
				reportUrl = request.getContextPath()+"/reports/"+jenisPolis+format+"spaj="+spaj;
				
			}else if( MANFAAT == printType ) {
				
				if(products.stableLink(businessId)) {
					List errors = new ArrayList();
					errors.add("Produk Stable Link tidak ada Cover Polis dan Manfaat. Silahkan gunakan pilihan SERTIFIKAT");
					return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
				}
				
				reportUrl = request.getContextPath()+"/reports/manfaat"+format+"spaj="+spaj;
				elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK MANFAAT", spaj,0);
				
			}else if( SURAT_POLIS == printType ) {
				reportUrl = request.getContextPath()+"/reports/suratpolis"+format+"spaj="+spaj;
				elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK SURAT_POLIS", spaj,0);
				
			}else if( ENDORS_GMIT == printType ) {
				reportUrl = request.getContextPath()+"/reports/endors_gmit"+format+"spaj="+spaj;
				elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK ENDORS GMIT", spaj,0);

			}else if( SURAT_SIMCARD == printType ) {
				reportUrl = request.getContextPath()+"/reports/surat_simcard"+format+"spaj="+spaj;
				elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK SURAT_SIMCARD", spaj,0);
//				Simcard simascard = this.uwManager.selectSimasCardBySpaj(spaj);
				int jenis=0;
				List isAgen = uwManager.selectIsSimasCardClientAnAgent(spaj); 
				if(isAgen.size()>0) { //apabila pemegang seorang agent
					jenis=5; 
				}
//				if(simascard!=null){
//					elionsManager.updateSuratSimasCard(2, jenis, simascard.getMrc_no_kartu());
//				}
				
				
			}else if( SURAT_BREAKABLE == printType ) {
				reportUrl = request.getContextPath()+"/reports/surat_breakable"+format+"spaj="+spaj;
				elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK SURAT_BREAKABLE", spaj,0);
				
			}else if( TANDA_TERIMA_POLIS == printType ) {
				reportUrl = request.getContextPath()+"/reports/tandaterimapolis"+format+"spaj="+spaj+"&referal="+referal;
				elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK TANDA_TERIMA_POLIS", spaj,0);
			}else if( TANDA_TERIMA_POLIS_DUPLIKAT == printType ) {
				reportUrl = request.getContextPath()+"/reports/tandaterimapolisduplikat"+format+"spaj="+spaj+"&referal="+referal;
				elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK TANDA_TERIMA_POLIS_DUPLIKAT", spaj,0);
			}else if(ENDORSEMEN == printType){
				reportUrl = request.getContextPath()+"/reports/endorsemen"+format+"spaj="+spaj;
				elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK ENDORSEMEN", spaj,0);
			}else if( ALOKASI_DANA == printType ) {
				//TODO (Yusuf) -> UNTUK DEBUGGING, DISABLE
				if(products.unitLink(businessId)) {
					if(!products.stableLink(businessId)) {
						this.elionsManager.updateUlink(3, spaj, df2.format(
								this.elionsManager.selectMuTglTrans(spaj)
								//(Date) elionsManager.selectSerbaGuna(
								//		"SELECT mu_tgl_trans FROM eka.mst_ulink WHERE mu_ke = 1 AND reg_spaj = rpad(replace("+spaj+",'.'),11,' ')").get(0).get("MU_TGL_TRANS")								
						));						
					}
					reportUrl = request.getContextPath()+"/reports/alokasidana"+format+"spaj="+spaj;
					elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK ALOKASI_DANA", spaj,0);
				}else {
					return noReportData(response, null);
				}
			}else if( SERTIFIKAT_POWERSAVE == printType ) {
				reportUrl = request.getContextPath()+"/reports/sertifikat"+format+"spaj="+spaj+"&referal="+referal;
				//APABILA POLIS INPUTAN BANK SINARMAS
				/*
				 *  ini di-disable, proses insert dan update dipindahkan setelah SPV mengisi pass (di window printotorisasipolis)
				int jn_bank = currentUser.getJn_bank().intValue();
				if(isValidated && currentUser.getCab_bank() != null && jn_bank == 2) {
					//apabila bank sinarmas, tarik counter untuk nomor sertifikat
					String sek = elionsManager.sequenceSertifikatSimasPrima();
					reportUrl += "&seri=" + sek;
					this.elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", currentUser.getLus_id(), 6, 1, "PRINT SERTIFIKAT SIMAS PRIMA("+sek+")", true, currentUser);
					
				}else if(isValidated && currentUser.getCab_bank() != null && jn_bank == 3) {
					//apabila sinarmas sekuritas, tarik counter untuk nomor sertifikat
					String sek = elionsManager.sequenceSertifikatSimasSekuritas();
					reportUrl += "&seri=" + sek;
					this.elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", currentUser.getLus_id(), 6, 1, "PRINT SERTIFIKAT SIMAS SEKURITAS("+sek+")", true, currentUser);
						
				}else if(businessId.equals("164")) {
					this.elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", currentUser.getLus_id(), 6, 1, "PRINT POLIS (E-LIONS)", true, currentUser);
				}
				*/
				
				//Anta - Khusus PowerSave 
				if(businessId.equals("175") && lsdbs.equals("002")) {
					this.elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", currentUser.getLus_id(), 6, 1, "PRINT SERTIFIKAT", true, currentUser);
				}

				int jn_bank = currentUser.getJn_bank().intValue();
				if(isValidated && !isBancass) {
					if(products.stableLink(businessId) && jn_bank != 2 ) {
						this.elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", currentUser.getLus_id(), 6, 1, "PRINT POLIS (E-LIONS)", true, currentUser);
					}else if(jn_bank == 3 || jn_bank==2){
						this.elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", currentUser.getLus_id(), 6, 1, "PRINT POLIS (E-LIONS)", true, currentUser);
					}else if(jn_bank !=2){
						this.elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", currentUser.getLus_id(), 6, 1, "PRINT POLIS (E-LIONS)", true, currentUser);
					}
				}
				
			}else if( SERTIFIKAT_GUTHRIE == printType ) {
				reportUrl = request.getContextPath()+"/reports/guthrie"+format+"spaj="+spaj;
				elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK SERTIFIKAT_GUTHRIE", spaj,0);
				this.elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", currentUser.getLus_id(), 6, 1, "PRINT POLIS (E-LIONS)", false, currentUser);

			}else if( KARTU_PREMI == printType ) {
				reportUrl = request.getContextPath()+"/reports/kartupremi"+format+"spaj="+spaj;
				elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK KARTU_PREMI", spaj,0);
				
			}else if( SYARAT_UMUM_KHUSUS == printType || SYARAT_UMUM_KHUSUS_EAS == printType || SYARAT_KHUSUS == printType) {
				if(ServletRequestUtils.getBooleanParameter(request, "isviewer", false)) {
					Map map = new HashMap();
					map.put("spaj", spaj);
					map.put("jenisDokumen", printType);
					map.put("isviewer", isViewer);
					return new ModelAndView("pdfViewer", map);
//					return errors;
				}else{
					elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK SYARAT_UMUM_KHUSUS", spaj,0);
					Map map = new HashMap();
					map.put("spaj", spaj);
					map.put("jenisDokumen", printType);
					return new ModelAndView("pdfViewer", map);
				}

			}else if( PANDUAN_VIRTUAL_ACCOUNT == printType ) {
				List tmp = elionsManager.selectDetailBisnis(spaj);
				String lsbs = (String) ((Map) tmp.get(0)).get("BISNIS");
				String lsdbs_number=(String) ((Map) tmp.get(0)).get("DETBISNIS");
				if(lsbs.equals("182") && "007, 008, 009".indexOf(lsdbs_number) > 0 || businessId.equals("196")) {
					List errors = new ArrayList();
					errors.add("Menu ini tidak dapat digunakan untuk Produk ini");
					return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
					
				}else{
				elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK PANDUAN_VIRTUAL_ACCOUNT", spaj,0);
				Map map = new HashMap();
				map.put("spaj", spaj);
				map.put("jenisDokumen", PANDUAN_VIRTUAL_ACCOUNT);
				return new ModelAndView("pdfViewer", map);
				}		

			}else if( POLIS_DMTM == printType ) {
				
				//validasi polis DM/TM, baru bisa produk 142-8 saja
				List tmp = elionsManager.selectDetailBisnis(spaj);
				String lsbs = (String) ((Map) tmp.get(0)).get("BISNIS");
				if(lsbs.equals("142") && lsdbs.equals("008")) {
					//boleh
				}else {
					List errors = new ArrayList();
					errors.add("Menu ini hanya dapat digunakan untuk Polis DM/TM");
					return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
				}
				elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK POLIS_DMTM", spaj,0);
				Map map = new HashMap();
				map.put("spaj", spaj);
				if(lsbs.equals("142") && lsdbs.equals("008")) {
					map.put("jenisDokumen", POLIS_QUADRUPLEX);
				}else{
					map.put("jenisDokumen", POLIS_DMTM);
				}
				map.put("tipe", "HARDCOPY");
				return new ModelAndView("pdfViewer", map);
				
			}else if( POLIS_PAS == printType ) {
				
				//validasi polis DM/TM, baru bisa produk 142-8 saja
				List tmp = elionsManager.selectDetailBisnis(spaj);
				String lsbs = (String) ((Map) tmp.get(0)).get("BISNIS");
				if(!lsbs.equals("187") && !lsbs.equals("205")) {
					List errors = new ArrayList();
					errors.add("Menu ini hanya dapat digunakan untuk Polis Produk PAS");
					return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
				}
				elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", currentUser.getLus_id(), 6, 1, "CETAK POLIS_PAS", false, currentUser);
				Map map = new HashMap();
				map.put("spaj", spaj);
				map.put("jenisDokumen", POLIS_PAS);
				map.put("tipe", "HARDCOPY");
				return new ModelAndView("pdfViewer", map);
				
			}else if( POLIS_TERM == printType ) {
				
				//validasi polis DM/TM, baru bisa produk 142-8 saja
				List tmp = elionsManager.selectDetailBisnis(spaj);
				String lsbs = (String) ((Map) tmp.get(0)).get("BISNIS");
				String detbisnis=(String)((Map) tmp.get(0)).get("DETBISNIS");
				if(!products.productChannel88(Integer.parseInt(lsbs), Integer.parseInt(lsdbs))) {
					List errors = new ArrayList();
					errors.add("Menu ini hanya dapat digunakan untuk Polis Produk Term & PA Channel 88");
					return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
				}
				
				if(lsbs.equals("196") && detbisnis.equals("002")){
					List errors = new ArrayList();
					errors.add("Untuk Produk Term ini Silahkan pilih Menu POLIS,MANFAAT,ALOKASI,DANA");
					return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
				}
				if(!isViewer){
					elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK POLIS_TERM", spaj,0);
				}
					Map map = new HashMap();
					map.put("spaj", spaj);
					map.put("jenisDokumen", POLIS_TERM);
					map.put("tipe", "HARDCOPY");
					return new ModelAndView("pdfViewer", map);
				
			}else if( TUNGGAKAN_PREMI == printType ) {
				//TODO (Yusuf) -> BELUM DIBUAT
				return null;	
			}else if( ENDORS_EKASEHAT_ADMEDIKA == printType){
//				TODO (Deddy) -> IN PROGRESS
				//return null;
				elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK ENDORS_EKASEHAT_ADMEDIKA", spaj,0);
				Map map = new HashMap();
				map.put("spaj", spaj);
				map.put("jenisDokumen", printType);
				return new ModelAndView("pdfViewer", map);
				
			}else if( ENDORS_GMIT == printType){
				elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK ENDORS GMIT", spaj,0);
				Map map = new HashMap();
				map.put("spaj", spaj);
				map.put("jenisDokumen", printType);
				return new ModelAndView("pdfViewer", map);
				
			}else if( KWITANSI == printType ) {
				reportUrl = request.getContextPath()+"/reports/kwitansi"+format+"spaj="+spaj;
				Boolean ok = false;
				do{
					try{
						elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK KWITANSI", spaj,0);
						ok=true;
					}catch(Exception e){};
				}while (!ok);
				//elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK KWITANSI", spaj,0);
			}else if( SURAT_PENJAMINAN_PROVIDER == printType ) {
				
				//validasi polis DMMT/BTN, hanya untuk produk :
				//-  Smart Hospital Care  195 (16 - 24)
				//-  Smart Medical Care  183 (76 – 90)
				//-  Smart Accident Care paket Single, Ceria, Ideal  187 (12,13,14)

				List tmp = elionsManager.selectDetailBisnis(spaj);
				String lsbs = (String) ((Map) tmp.get(0)).get("BISNIS");
				String lsdbs_id = (String) ((Map) tmp.get(0)).get("DETBISNIS");
				Integer lsdbs_number = Integer.parseInt(lsdbs_id);
//				if((lsbs.equals("195") &&  "016,017,018,019,020,021,022,023,024".indexOf(lsdbs_id) > 0 ) 
//					|| (lsbs.equals("183") &&  "076,077,078,079,080,081,082,083,084,085,086,087,088,089,090".indexOf(lsdbs_id) > 0 ))
//				{
				if((lsbs.equals("195") &&  (lsdbs_number>=16 && lsdbs_number<=24) ) 
					|| (lsbs.equals("183") &&  (lsdbs_number>=76 && lsdbs_number<=90) ))
				{
					
					Map map = new HashMap();
					map.put("spaj", spaj);
					map.put("jenisDokumen", SURAT_PENJAMINAN_PROVIDER);
					map.put("tipe", "HARDCOPY");
					elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK SURAT PENJAMINAN PROVIDER", spaj,0);
					return new ModelAndView("pdfViewer", map);
					
				}else{
					List errors = new ArrayList();
					errors.add("Menu ini hanya dapat digunakan untuk Polis Produk Smart Hospital Care/Smart Medical Care");
					return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
				}
			
			}else if( SERTIFIKAT_TERM_ROP == printType ) {
				
				//validasi polis DM/TM, baru bisa produk 142-8 saja
				List tmp = elionsManager.selectDetailBisnis(spaj);
				String lsbs = (String) ((Map) tmp.get(0)).get("BISNIS");
				String detbisnis=(String)((Map) tmp.get(0)).get("DETBISNIS");
				
				if( !(lsbs.equals("223") || lsbs.equals("212") || (lsbs.equals("197") && detbisnis.equals("002"))  )){
					List errors = new ArrayList();
					errors.add("Khusus untuk produk Term");
					return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
				}
				
				elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", currentUser.getLus_id(), 6, 1, "CETAK SERTIFIKAT", true, currentUser);				
				//elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK SERTIFIKAT", spaj,0);
				Map map = new HashMap();
				map.put("spaj", spaj);
				map.put("jenisDokumen", printType);
				map.put("lsbs", lsbs);
				map.put("detbisnis", detbisnis);
				map.put("lus_id",currentUser.getLus_id());
				return new ModelAndView("pdfViewer", map);
			
			}

			reportUrl += "&isViewer=" + isViewer;
			
			if(format.indexOf(".jasper")>-1) {
				String kabur = FormatString.escapeHTML(reportUrl);
				response.sendRedirect(request.getContextPath()+"/include/page/applet.jsp?zoom_index=1&report_url="+kabur);
			}else {
				response.sendRedirect(reportUrl);
			}
			return null;
		}
	}
  
}

	//DATA MENU
	private Map referenceData(String spaj, String businessId, boolean printAll, boolean isViewer, boolean isBancass, User currentUser) {
		Map map = new HashMap();
		List reportList = new ArrayList();
		Map temp = null;
		
		businessId = FormatString.rpad("0", businessId, 3);

		List detBisnis = elionsManager.selectDetailBisnis(spaj);
		String elesbees = "";
		String lsdbs = "";
		
		if(!detBisnis.isEmpty()) {
			elesbees = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
		}
		
		String cabang = elionsManager.selectCabangFromSpaj(spaj);

		String va = uwManager.selectVirtualAccountSpaj(spaj);

		//APABILA POLIS INPUTAN BII
		if(currentUser.getCab_bank() != null && (currentUser.getJn_bank().intValue() == 0 || currentUser.getJn_bank().intValue() == 1)) {
			//Polis dan Manfaat
			temp = new HashMap(); temp.put("g","Lembar Kuning");
			temp.put("kode", POLIS_QUADRUPLEX); temp.put("judul", "Polis, Manfaat, Surat, Alokasi Dana"); reportList.add(temp);
			//Tanda Terima Polis
			temp = new HashMap(); temp.put("g","Lainnya"); 
			temp.put("kode", TANDA_TERIMA_POLIS); temp.put("judul", "Tanda Terima Polis"); reportList.add(temp);
			//Tanda Terima Polis Duplikat
			temp = new HashMap(); temp.put("g","Lainnya"); 
			temp.put("kode", TANDA_TERIMA_POLIS_DUPLIKAT); temp.put("judul", "Tanda Terima Polis Duplikat"); reportList.add(temp);
			//Syarat Umum/Khusus
			if(spaj==null || PDFViewer.checkFileProduct(this.elionsManager, this.uwManager, props, spaj)) {
					temp = new HashMap(); temp.put("g","Lainnya"); 
					temp.put("kode", SYARAT_UMUM_KHUSUS); temp.put("judul", "Syarat-syarat Umum / Khusus"); reportList.add(temp);
			}
		//APABILA POLIS INPUTAN BANK SINARMAS
		}else if(currentUser.getCab_bank() != null && currentUser.getJn_bank().intValue() == 2) {
			//Polis dan Manfaat
			temp = new HashMap(); temp.put("g","Lembar Kuning");
			temp.put("kode", POLIS_QUADRUPLEX); temp.put("judul", "Polis, Manfaat, Surat, Alokasi Dana (Khusus Simas Power Link)"); reportList.add(temp);
			//Sertifikat Powersave/Stable Link (untuk polis inputan bank tidak dibatasi)
			temp = new HashMap(); temp.put("g","Dokumen Sertifikat"); 
			temp.put("kode", SERTIFIKAT_POWERSAVE); temp.put("judul", "Sertifikat Polis"); reportList.add(temp);
			//Tanda Terima Polis
			temp = new HashMap(); temp.put("g","Dokumen Sertifikat"); 
			temp.put("kode", TANDA_TERIMA_POLIS); temp.put("judul", "Tanda Terima Sertifikat"); reportList.add(temp);
			//Surat Perjanjian Pinjaman Polis
			temp = new HashMap(); temp.put("g","Dokumen Sertifikat");
			temp.put("kode", SURAT_PERJANJIAN_HUTANG); temp.put("judul", "Surat Perjanjian Pinjaman Polis"); reportList.add(temp);
			//Laporan Alokasi Dana, bila SIMAS STABIL LINK
			temp = new HashMap(); temp.put("g","Dokumen Sertifikat"); 
			temp.put("kode", ALOKASI_DANA); temp.put("judul", "Laporan Alokasi Dana Awal (Khusus Simas Stabil Link)"); reportList.add(temp);
			temp = new HashMap(); temp.put("g","Dokumen Sertifikat");
			temp.put("kode", ENDORSEMEN); temp.put("judul", "Endorsemen"); reportList.add(temp);
			temp = new HashMap(); temp.put("g","Dokumen Sertifikat");
			temp.put("kode", SYARAT_KHUSUS); temp.put("judul", "SSK Swine Flu"); reportList.add(temp);
			temp = new HashMap(); temp.put("g","Lainnya"); 
			temp.put("kode", SYARAT_UMUM_KHUSUS); temp.put("judul", "Syarat-syarat Umum / Khusus (Khusus Simas Power Link)"); reportList.add(temp);			
		//Lufi--APABILA POLIS INPUTAN BSIM
		}else if(currentUser.getCab_bank() != null && currentUser.getJn_bank().intValue() == 16) {
			//Surat Simas Card
			temp = new HashMap(); temp.put("g","Lainnya");
			temp.put("kode", SURAT_SIMCARD); temp.put("judul", "Surat Simas Card"); reportList.add(temp);
			//Sertifikat Powersave/Stable Link (untuk polis inputan bank tidak dibatasi)
			temp = new HashMap(); temp.put("g","Dokumen Sertifikat"); 
			temp.put("kode", SERTIFIKAT_POWERSAVE); temp.put("judul", "Sertifikat Polis"); reportList.add(temp);
			//Tanda Terima Polis
			temp = new HashMap(); temp.put("g","Dokumen Sertifikat"); 
			temp.put("kode", TANDA_TERIMA_POLIS); temp.put("judul", "Tanda Terima Sertifikat"); reportList.add(temp);
			//Surat Perjanjian Pinjaman Polis
			temp = new HashMap(); temp.put("g","Dokumen Sertifikat");
			temp.put("kode", SURAT_PERJANJIAN_HUTANG); temp.put("judul", "Surat Perjanjian Pinjaman Polis"); reportList.add(temp);

		//APABILA POLIS INPUTAN SINARMAS SEKURITAS
		}else if(currentUser.getCab_bank() != null && currentUser.getJn_bank().intValue() == 3) {
			//Sertifikat Powersave/Stable Link (untuk polis inputan bank tidak dibatasi)
			temp = new HashMap(); temp.put("g","Dokumen Sertifikat"); 
			temp.put("kode", SERTIFIKAT_POWERSAVE); temp.put("judul", "Sertifikat Power Save/Stable Link"); reportList.add(temp);
			//Tanda Terima Polis
			temp = new HashMap(); temp.put("g","Dokumen Sertifikat"); 
			temp.put("kode", TANDA_TERIMA_POLIS); temp.put("judul", "Tanda Terima Sertifikat"); reportList.add(temp);
			//Surat Perjanjian Pinjaman Polis
			temp = new HashMap(); temp.put("g","Dokumen Sertifikat");
			temp.put("kode", SURAT_PERJANJIAN_HUTANG); temp.put("judul", "Surat Perjanjian Pinjaman Polis"); reportList.add(temp);
//			Laporan Alokasi Dana, Stable Link SMS
			temp = new HashMap(); temp.put("g","Dokumen Sertifikat"); 
			temp.put("kode", ALOKASI_DANA); temp.put("judul", "Laporan Alokasi Dana Awal (Khusus Stable Link SMS)"); reportList.add(temp);
			temp = new HashMap(); temp.put("g","Lainnya"); 
			temp.put("kode", SYARAT_UMUM_KHUSUS); temp.put("judul", "Syarat-syarat Umum / Khusus"); reportList.add(temp);
		}else if(isBancass) {
			//Polis Utama
			temp = new HashMap(); temp.put("g","Lembar Kuning"); 
			temp.put("kode", POLIS); temp.put("judul", "Polis Utama"); reportList.add(temp);
		}else {
			if(isViewer) {
				if(!currentUser.getLca_id().equals("37")){
				//Polis Utama
				temp = new HashMap(); temp.put("g","Lembar Kuning"); 
				temp.put("kode", POLIS); temp.put("judul", "Polis Utama"); reportList.add(temp);
				//Manfaat
				temp = new HashMap(); temp.put("g","Lembar Kuning"); 
				temp.put("kode", MANFAAT); temp.put("judul", "Manfaat Polis"); reportList.add(temp);
				//Surat
				temp = new HashMap(); temp.put("g","Lembar Kuning"); 
				temp.put("kode", SURAT_POLIS); temp.put("judul", "Surat"); reportList.add(temp);
				//Laporan Alokasi Dana / Surat UnitLink
				if(spaj==null || products.unitLink(businessId)) {
					temp = new HashMap(); temp.put("g","Lembar Kuning"); 
					temp.put("kode", ALOKASI_DANA); temp.put("judul", "Laporan Alokasi Dana"); reportList.add(temp);
				}
				
				if(currentUser.getLus_id().equals("49")){
					//Kartu Premi (enabled hanya bila mba Wesni minta untuk kontes agen of the year)
					temp = new HashMap(); temp.put("g","Lembar Kuning"); 
					temp.put("kode", KARTU_PREMI); temp.put("judul", "Kartu Premi"); reportList.add(temp);
					//Sertifikat Powersave/Stable Link (untuk polis inputan bank tidak dibatasi)
					temp = new HashMap(); temp.put("g","Dokumen Sertifikat"); 
					temp.put("kode", SERTIFIKAT_POWERSAVE); temp.put("judul", "Sertifikat Polis"); reportList.add(temp);
				}
				}
			}else {
				//Print ALL
				//TODO (Yusuf) > ini disable / gak? (fitur print ALL)
				//temp = new HashMap(); temp.put("g",""); 
				//temp.put("kode", ALL); temp.put("judul", "ALL"); reportList.add(temp);
	
				//Polis dan Manfaat
				temp = new HashMap(); temp.put("g","Lembar Kuning");
				temp.put("kode", POLIS_QUADRUPLEX); temp.put("judul", "Polis, Manfaat, Surat, Alokasi Dana"); reportList.add(temp);
				//Polis All
				temp = new HashMap(); temp.put("g","Lembar Kuning");
				temp.put("kode", ALL); temp.put("judul", "Polis All"); reportList.add(temp);
				//Manfaat
				temp = new HashMap(); temp.put("g","Lembar Kuning"); 
				temp.put("kode", MANFAAT); temp.put("judul", "Manfaat Polis"); reportList.add(temp);
				//Surat
				temp = new HashMap(); temp.put("g","Lembar Kuning"); 
				temp.put("kode", SURAT_POLIS); temp.put("judul", "Surat"); reportList.add(temp);
				//Laporan Alokasi Dana / Surat UnitLink
				temp = new HashMap(); temp.put("g","Lembar Kuning"); 
				temp.put("kode", ALOKASI_DANA); temp.put("judul", "Laporan Alokasi Dana"); reportList.add(temp);
			}
			
			if(cabang.equals("58")){
				temp = new HashMap(); temp.put("g","Lembar Kuning");
				temp.put("kode", POLIS_QUADRUPLEX); temp.put("judul", "Polis, Manfaat, Surat, Alokasi Dana"); reportList.add(temp);
				
				temp = new HashMap(); temp.put("g","Lembar Kuning"); 
				temp.put("kode", POLIS); temp.put("judul", "Polis Utama"); reportList.add(temp);
				
				temp = new HashMap(); temp.put("g","Lembar Kuning"); 
				temp.put("kode", MANFAAT); temp.put("judul", "Manfaat Polis"); reportList.add(temp);
				
				temp = new HashMap(); temp.put("g","Lembar Kuning"); 
				temp.put("kode", SURAT_POLIS); temp.put("judul", "Surat"); reportList.add(temp);
				
				temp = new HashMap(); temp.put("g","Lembar Kuning"); 
				temp.put("kode", ALOKASI_DANA); temp.put("judul", "Laporan Alokasi Dana"); reportList.add(temp);
				
				//Surat Simas Card
				temp = new HashMap(); temp.put("g","Lainnya");
				temp.put("kode", SURAT_SIMCARD); temp.put("judul", "Surat Simas Card"); reportList.add(temp);
			}else if(currentUser.getLca_id().equals("37")){
					temp = new HashMap(); temp.put("g","Lembar Kuning"); 
					temp.put("kode", POLIS); temp.put("judul", "Polis Utama"); reportList.add(temp);
					
					temp = new HashMap(); temp.put("g","Lembar Kuning"); 
					temp.put("kode", MANFAAT); temp.put("judul", "Manfaat Polis"); reportList.add(temp);
			}else{
				//Polis DMTM (khusus produk DM/TM)
				temp = new HashMap(); temp.put("g","Lembar Kuning");
				temp.put("kode", POLIS_DMTM); temp.put("judul", "Polis khusus produk DM/TM"); reportList.add(temp);
				
				temp = new HashMap(); temp.put("g","Lembar Kuning");
				temp.put("kode", POLIS_PAS); temp.put("judul", "Polis khusus produk PAS"); reportList.add(temp);
				
				temp = new HashMap(); temp.put("g","Lembar Kuning");
				temp.put("kode", POLIS_TERM); temp.put("judul", "Polis khusus produk TERM & PA CHANNEL 88"); reportList.add(temp);
				
				temp = new HashMap(); temp.put("g","Lainnya");
				temp.put("kode", SYARAT_KHUSUS); temp.put("judul", "SSK Swine Flu"); reportList.add(temp);
				
				//Surat Simas Card
				temp = new HashMap(); temp.put("g","Lainnya");
				temp.put("kode", SURAT_SIMCARD); temp.put("judul", "Surat Simas Card"); reportList.add(temp);
				
				//Surat Breakable
				temp = new HashMap(); temp.put("g","Lainnya");
				temp.put("kode", SURAT_BREAKABLE); temp.put("judul", "Surat Breakable"); reportList.add(temp);
				
				//Sertifikat Guthrie (Khusus Produk Guthrie)
				temp = new HashMap(); temp.put("g","Lainnya"); 
				temp.put("kode", SERTIFIKAT_GUTHRIE); temp.put("judul", "Sertifikat Guthrie"); reportList.add(temp);
				
				//Endorsemen khusus rider Swine Flu
				//if(uwManager.selectCountSwineFlu(spaj)>0){
					temp = new HashMap(); temp.put("g","Lainnya"); 
					temp.put("kode", ENDORSEMEN); temp.put("judul", "Endorsemen"); reportList.add(temp);
					
				//
				temp = new HashMap(); temp.put("g","Lainnya");
				temp.put("kode", SURAT_PENJAMINAN_PROVIDER); temp.put("judul", "Surat Penjaminan Provider"); reportList.add(temp);
				
				temp = new HashMap(); temp.put("g","Lainnya");
				temp.put("kode", SERTIFIKAT_TERM_ROP); temp.put("judul", "Sertifikat Term ROP"); reportList.add(temp);
			}

			
			//}
			
			//Sertifikat Powersave/Stable Link (khusus powersave diatas Rp. 200.000.000,- atau $20.000,-)
			int isInputanBank = elionsManager.selectIsInputanBank(spaj);
			if(spaj==null) {
				temp = new HashMap(); temp.put("g","Lainnya"); 
				temp.put("kode", SERTIFIKAT_POWERSAVE); temp.put("judul", "Sertifikat Power Save/Stable Link"); reportList.add(temp);
			}else if(products.powerSave(businessId)) {
				Map premiProdukUtama = this.elionsManager.selectPremiProdukUtama(spaj);
				String kurs = (String) premiProdukUtama.get("LKU_ID");
				BigDecimal premi = (BigDecimal) premiProdukUtama.get("MSPR_PREMIUM");
				if((kurs.equals("01") && premi.doubleValue()>=200000000) || (kurs.equals("02") && premi.doubleValue()>=20000) || isInputanBank==2 || isInputanBank==3 || isInputanBank==16) {
					temp = new HashMap(); temp.put("g","Lainnya"); 
					temp.put("kode", SERTIFIKAT_POWERSAVE); temp.put("judul", "Sertifikat Power Save/Stable Link"); reportList.add(temp);
				}
			//Sertifikat Simas Prima / Stable link
			}else if((isInputanBank==2 || isInputanBank==3) || products.stableLink(elesbees)) {
				temp = new HashMap(); temp.put("g","Lainnya"); 
				temp.put("kode", SERTIFIKAT_POWERSAVE); temp.put("judul", "Sertifikat Power Save/Stable Link"); reportList.add(temp);
			}
			
			if(!currentUser.getLca_id().equals("37")){
			//Tanda Terima Polis
			temp = new HashMap(); temp.put("g","Lainnya"); 
			temp.put("kode", TANDA_TERIMA_POLIS); temp.put("judul", "Tanda Terima Polis"); reportList.add(temp);
			//Tanda Terima Polis Duplikat
			temp = new HashMap(); temp.put("g","Lainnya"); 
			temp.put("kode", TANDA_TERIMA_POLIS_DUPLIKAT); temp.put("judul", "Tanda Terima Polis Duplikat"); reportList.add(temp);
			//Kartu Premi
			if(!printAll) {
				temp = new HashMap(); temp.put("g","Lainnya"); 
				temp.put("kode", KARTU_PREMI); temp.put("judul", "Kartu Premi"); reportList.add(temp);
			}
			//Syarat Umum/Khusus
			if(spaj==null || PDFViewer.checkFileProduct(this.elionsManager, this.uwManager, props, spaj)) {
					temp = new HashMap(); temp.put("g","Lainnya"); 
					temp.put("kode", SYARAT_UMUM_KHUSUS); temp.put("judul", "Syarat-syarat Umum / Khusus"); reportList.add(temp);
			}
			}
			//Tunggakan Premi (Bukan Pada SPAJ Baru) 
			//temp = new HashMap(); temp.put("g","Lainnya"); 
			//temp.put("kode", TUNGGAKAN_PREMI); temp.put("judul", "Tunggakan Premi"); reportList.add(temp);
			
			if((!cabang.equals("58")) && (!currentUser.getLca_id().equals("37"))){
				//Surat Perjanjian Pinjaman Polis
				temp = new HashMap(); temp.put("g","Lainnya");
				temp.put("kode", SURAT_PERJANJIAN_HUTANG); temp.put("judul", "Surat Perjanjian Pinjaman Polis"); reportList.add(temp);
			
				temp = new HashMap(); temp.put("g","Lainnya"); 
				temp.put("kode", ENDORS_EKASEHAT_ADMEDIKA); temp.put("judul", "Endors Smile Medical Admedika"); reportList.add(temp);
				
				temp = new HashMap(); temp.put("g","Lainnya"); 
				temp.put("kode", ENDORS_GMIT); temp.put("judul", "Endorsment GMIT"); reportList.add(temp);
			}
		}
		
		//Panduan Virtual Account
		//if(va != null){
		if(currentUser.getJn_bank().intValue() != 2 && currentUser.getJn_bank().intValue() != 16  && currentUser.getJn_bank().intValue() != 20 && (!currentUser.getLca_id().equals("37"))){
			temp = new HashMap(); temp.put("g","Lainnya");
			temp.put("kode", PANDUAN_VIRTUAL_ACCOUNT); temp.put("judul", "Panduan Rekening Billing"); reportList.add(temp);
		}

		if(!currentUser.getLca_id().equals("37")){		
		temp = new HashMap(); temp.put("g","Lainnya");
		temp.put("kode", KWITANSI); temp.put("judul", "Kwitansi"); reportList.add(temp);
		}

		//Mark Valentino 20180906 Penambahan Menu Dropdown List : Generate All & Generate Outsource
		Map temp1 = null;
		Map temp2 = null;		
		if(currentUser.getLca_id().equals("01")){
		temp1 = new HashMap(); temp1.put("g","Lainnya");
		temp1.put("kode", GENERATE_ALL); temp1.put("judul", "Generate All"); reportList.add(temp1);
		temp2 = new HashMap(); temp2.put("g","Lainnya");		
		temp2.put("kode", GENERATE_OUTSOURCE); temp2.put("judul", "Generate Outsource"); reportList.add(temp2);		
		}
		
		map.put("reportList", reportList);

		return map;
	}
	
	//VALIDASI2 SEBELUM PRINT POLIS
	private List validasi(int printProgress, String spaj, String businessId, HttpServletRequest request) throws Exception{

		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List errors = new ArrayList();
		
		Date tglCetak = elionsManager.selectPrintDatePolis(spaj);
		Calendar tgl_mulai_ssu = new GregorianCalendar(2006, 8, 20);
		String file = ServletRequestUtils.getStringParameter(request, "file", "");
		String format = ServletRequestUtils.getStringParameter(request, "format", "");

		List detBisnis = elionsManager.selectDetailBisnis(spaj);
		String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
		String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
		
		String cabang = elionsManager.selectCabangFromSpaj(spaj);
		
//		if(POLIS_DMTM != printProgress && SURAT_POLIS != printProgress) {
//			//validasi polis DM/TM, baru bisa produk 142-8 saja
//			if(lsbs.equals("142") && lsdbs.equals("008")) {
//				errors.add("Untuk Polis DM/TM mohon gunakan menu POLIS KHUSUS PRODUK DM/TM atau menu SURAT POLIS");
//			}
//		}
		
		//muamalat
		if(products.muamalat(Integer.parseInt(lsbs), Integer.parseInt(lsdbs))) {
			//validasi polis muamalat - mabrur, hanya bisa cetak alokasi dana saja
			if(lsbs.equals("153") && lsdbs.equals("005")) {
				if(ALOKASI_DANA != printProgress && SURAT_SIMCARD != printProgress) {
					errors.add("Khusus Produk Bank Muamalat (Mabrur), hanya dapat melakukan pencetakan LAPORAN ALOKASI DANA (UNIT LINK)");
					return errors;
				}
			}else{
				errors.add("Produk Bank Muamalat, tidak perlu melakukan pencetakan dokumen");
				return errors;
			}
		}
		Simcard simcard = uwManager.selectSimasCardBySpaj(spaj);
		List daftarSebelumnya = uwManager.selectSimasCard(spaj);
		List isAgen = uwManager.selectIsSimasCardClientAnAgent(spaj); 
		//daftarSebelumnya.removeAll(daftarSebelumnya);
		if(simcard==null && daftarSebelumnya.isEmpty()){
			if( !lsbs.equals("187") && !products.bethany(Integer.parseInt(lsbs), Integer.parseInt(lsdbs)) && !products.muamalat(Integer.parseInt(lsbs), Integer.parseInt(lsdbs)) ){
				if(!isAgen.isEmpty() && !FormatString.rpad("0",(spaj.substring(0,2)),2).equalsIgnoreCase("09")){
					if(elionsManager.selectCekPrintUlang(spaj, "%POLIS TUTUPAN AGENT%")<=0){
						elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "POLIS TUTUPAN AGENT, AGENT SUDAH PERNAH MENDAPATKAN SIMASCARD DARI DISTRIBUTION SUPPORT", spaj, 0);
					}
				}
				else{
//					if((!((lsbs.equals("143") && (lsdbs.equals("1") || lsdbs.equals("3") || lsdbs.equals("7"))) || 
//							(lsbs.equals("144") && lsdbs.equals("1")) || 
//							(lsbs.equals("212") && lsdbs.equals("010")) || //Chandra A - Smile Proteksi APKLI tidak ada simas card
//							lsbs.equals("183") || 
//							lsbs.equals("116"))) && isAgen.isEmpty()){
//						errors.add("Silakan masukkan No Simas Card terlebih dahulu sebelum melakukan proses print");  #iga 03/04/2020 tidak ada lagi simascard
//					}
				    
				}
			}
		}else{
			if(!daftarSebelumnya.isEmpty()){
				if(elionsManager.selectCekPrintUlang(spaj, "%SUDAH PERNAH DAPAT SIMAS CARD%")<=0){
					Map SimasCardSebelumnya = (Map) daftarSebelumnya.get(0);
					if(!Common.isEmpty(SimasCardSebelumnya.get("REG_SPAJ"))){
						String reg_spaj_lama = (String) SimasCardSebelumnya.get("REG_SPAJ");
						String mspo_policy_no_lama = uwManager.selectNoPolisFromSpaj(reg_spaj_lama);
						//MANTA - 14/02/2014
						if(mspo_policy_no_lama == null){
							mspo_policy_no_lama = uwManager.selectGetMspoPolicyNo(reg_spaj_lama);
						}
						String msps_desc = elionsManager.selectMstPositionSpajMspsDesc(spaj, 5);
						if(!reg_spaj_lama.equals(spaj)){
							if(Common.isEmpty(msps_desc) && errors.isEmpty() ){
								elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "SUDAH PERNAH DAPAT SIMAS CARD DENGAN NO POLIS "+ FormatString.nomorPolis(mspo_policy_no_lama), spaj, 0);
							}
							errors.add("Polis ini sudah pernah mendapatkan Simas Card pada no polis "+ FormatString.nomorPolis(mspo_policy_no_lama));
						}
					}else{
						SimasCardSebelumnya = (Map) daftarSebelumnya.get(0);
						if(Common.isEmpty((String) SimasCardSebelumnya.get("REG_SPAJ"))){
							//Deddy - ini untuk simas card agent yg diinput manual
							String no_kartu_sebelumnya = (String) SimasCardSebelumnya.get("NO_KARTU");
							elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "SUDAH PERNAH DAPAT SIMAS CARD DENGAN NO : "+ no_kartu_sebelumnya , spaj, 0);
						}else{
							String reg_spaj_lama = (String) SimasCardSebelumnya.get("REG_SPAJ");
							String mspo_policy_no_lama = uwManager.selectNoPolisFromSpaj(reg_spaj_lama);
							String msps_desc = elionsManager.selectMstPositionSpajMspsDesc(spaj, 5);
							if(!reg_spaj_lama.equals(spaj)){
								if(Common.isEmpty(msps_desc) && errors.isEmpty() ){
									elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "SUDAH PERNAH DAPAT SIMAS CARD DENGAN NO POLIS "+ FormatString.nomorPolis(mspo_policy_no_lama), spaj, 0);
								}
								errors.add("Polis ini sudah pernah mendapatkan Simas Card pada no polis "+ FormatString.nomorPolis(mspo_policy_no_lama));
							}
						}
					}
				}
			}
		}
		
		if("58".indexOf(cabang)<=-1){
			if(products.unitLink(lsbs) || (lsbs.equals("164") && lsdbs.equals("11"))){
				List<Date> asdf = uwManager.selectSudahProsesNab(spaj);
				boolean oke = true;
				for(Date d : asdf){
					if(d == null) {
						oke = false;
						break;
					}
				}
				if(asdf.size()==0){
					oke = false;
				}
				if(!oke) errors.add("Polis tidak dapat dicetak karena belum dilakukan proses NAB.");
			}
		}
		
		Integer cekProsesBatal = bacManager.selectCountSpajMstRefund(spaj);
		
		if(cekProsesBatal>0)errors.add("Polis tidak dapat dicetak karena dalam proses Pembatalan.");
		
		//KALAU SSU, MAU DIBUKA DARI MANA SAJA, CUKUP SATU VALIDASI
		//**DIAN
		if( SYARAT_UMUM_KHUSUS == printProgress || SYARAT_KHUSUS == printProgress) { //untuk lihat SSU/SSK, gak perlu validasi
			if(ServletRequestUtils.getBooleanParameter(request, "isviewer", false)) {
				return errors;
			}else{
			int count= elionsManager.selectCekPrintUlang(spaj, "CETAK SYARAT_UMUM_KHUSUS");
			if(count>0&&((currentUser.getJn_bank()!=2||currentUser.getJn_bank()!=3) && !currentUser.getLca_id().equals("58"))){
				errors.add("Maaf, Pencetakan SSU/SSK hanya dapat dilakukan 1 kali.");
				request.setAttribute("printUlang", spaj);
			}else{
				if(currentUser.getLde_id().equals("11")){ //kalo underwriting, boleh
				
				}else if(tglCetak == null) { 
					errors.add("Maaf, tetapi untuk melihat SSU harus melakukan pencetakan polis/sertifikat terlebih dahulu.");
				} else if(tglCetak.before(tgl_mulai_ssu.getTime())) {
					errors.add("Tidak ada SSU/SSK.");
//				} else if("045, 130, 140".indexOf(businessId) > -1) {
				} else if("045, 130".indexOf(businessId) > -1) {
					errors.add("SSU/SSK untuk produk ini PRE-PRINTED");
				}}
			return errors;
			}
		}
		if(TANDA_TERIMA_POLIS == printProgress) { //tanda terima
			if(ServletRequestUtils.getBooleanParameter(request, "isviewer", false)) {
				return errors;
			}else{
				int count= elionsManager.selectCekPrintUlang(spaj, "CETAK TANDA_TERIMA_POLIS");
				if(count>0&&(currentUser.getJn_bank().equals(""))){
					errors.add("Maaf, Pencetakah Tanda Terima Polis hanya dapat dilakukan 1 kali.");
					request.setAttribute("printUlang", spaj);
				}
			}
		}
		
		if( MANFAAT == printProgress ) { //manfaat
			if(ServletRequestUtils.getBooleanParameter(request, "isviewer", false)) {
				return errors;
			}else{
				int count= elionsManager.selectCekPrintUlang(spaj, "CETAK MANFAAT");
				if(count>0&&(currentUser.getJn_bank().equals(""))){
					errors.add("Maaf, Pencetakan Manfaat Polis hanya dapat dilakukan sekali.");
					request.setAttribute("printUlang", spaj);
				}
			}
		}
		if( SURAT_POLIS == printProgress ) {
			if(ServletRequestUtils.getBooleanParameter(request, "isviewer", false)) {
				return errors;
			}else{
				int count= elionsManager.selectCekPrintUlang(spaj, "CETAK SURAT_POLIS");
				if(count>0&&(currentUser.getJn_bank().equals(""))){ 
					errors.add("Maaf, Pencetakan Surat Polis hanya dapat dilakukan sekali.");
					request.setAttribute("printUlang", spaj);
				}
			}
		}
		//KALAU Surat Perjanjian Pinjaman Polis, MAU DIBUKA DARI MANA SAJA, HANYA BOLEH UNTUK PRODUK SAVE
		if(SURAT_PERJANJIAN_HUTANG == printProgress && !products.powerSave(businessId)) {
			errors.add("Maaf, SPH hanya bisa dicetak untuk produk SAVE");
		}
		if( ALOKASI_DANA == printProgress ) {
			if(ServletRequestUtils.getBooleanParameter(request, "isviewer", false)) {
				return errors;
			}else{
				int count= elionsManager.selectCekPrintUlang(spaj, "CETAK ALOKASI_DANA");
				if(count>0&&(currentUser.getJn_bank().equals(""))){ 
					errors.add("Maaf, tetapi anda  bisa melakukan pencetakkan Laporan Alokasi Dana / Surat Excellink hanya 1 kali ");
					request.setAttribute("printUlang", spaj);
				}
			}
			
		}
		
		if( PANDUAN_VIRTUAL_ACCOUNT == printProgress ) {
			int count= elionsManager.selectCekPrintUlang(spaj, "CETAK PANDUAN_VIRTUAL_ACCOUNT");
			if(count>0&&(currentUser.getJn_bank().equals(""))){
				errors.add("Maaf, tetapi anda  bisa melakukan pencetakkan PANDUAN_VIRTUAL_ACCOUNT hanya 1 kali ");
				request.setAttribute("printUlang", spaj);
			
			}
		}
		if( POLIS_DMTM == printProgress ){
			int count= elionsManager.selectCekPrintUlang(spaj, "CETAK POLIS_DMTM");
			if(count>0&&(currentUser.getJn_bank().equals(""))){
				errors.add("Maaf, tetapi anda  bisa melakukan pencetakkan POLIS_DMTM hanya 1 kali ");
				request.setAttribute("printUlang", spaj);
			
			}
		}
		
		if( POLIS_PAS == printProgress ){
			int count= elionsManager.selectCekPrintUlang(spaj, "CETAK POLIS_PAS");
			if(count>0&&(currentUser.getJn_bank().equals(""))){
				errors.add("Maaf, tetapi anda  bisa melakukan pencetakkan POLIS_PAS hanya 1 kali ");
				request.setAttribute("printUlang", spaj);
			
			}
		}
		
		if( POLIS_TERM == printProgress ){
			int count= elionsManager.selectCekPrintUlang(spaj, "CETAK POLIS_TERM");
			if(count>0&&(currentUser.getJn_bank().equals(""))){
				errors.add("Maaf, tetapi anda  bisa melakukan pencetakkan POLIS_TERM hanya 1 kali ");
				request.setAttribute("printUlang", spaj);
			
			}
		}
		
		if( KARTU_PREMI == printProgress ){
			int count= elionsManager.selectCekPrintUlang(spaj, "CETAK KARTU_PREMI");
			if(count>0&&(currentUser.getJn_bank().equals(""))){
				errors.add("Maaf, tetapi anda  bisa melakukan pencetakkan KARTU_PREMI hanya 1 kali ");
				request.setAttribute("printUlang", spaj);
			
			}
		}
		if(SERTIFIKAT_POWERSAVE == printProgress) {
			int count= elionsManager.selectCekPrintUlang(spaj, "PRINT POLIS (E-LIONS)");
			Map map =this.elionsManager.validationPrintPolis(spaj); 
			if(count>0&&map.get("ORI")!=null&&(currentUser.getJn_bank()!=2||currentUser.getJn_bank()!=3)){
				errors.add("Maaf, tetapi anda  bisa melakukan pencetakkan SERTIFIKAT hanya 1 kali ");
				request.setAttribute("printUlang", spaj);
				request.setAttribute("jenis", 13);
			}
		}
		
		//KALAU DIBUKA DARI VIEWER, LEWATIN VALIDASI
		if(ServletRequestUtils.getBooleanParameter(request, "isviewer", false)) {
			return errors;
		}
		
		//VALIDASI UNTUK PRINTING SEMUA JENIS DOKUMEN
//		try {
//			String trimmed = FormatString.rpad("0", Integer.valueOf(businessId).toString(), 2);
//			n_prod produk = (n_prod) Class.forName("produk_asuransi.n_prod_"+trimmed).newInstance();
//			if(produk.isInvestasi && this.elionsManager.validationNAB(spaj)>0 && !businessId.equals("165")) {
//				errors.add("Untuk produk Excellink, harap lakukan proses NAB terlebih dahulu.");
//			}
//		}catch(ClassNotFoundException e) {
//			errors.add("Maaf, tetapi Produk "+businessId+" belum diimplementasikan. Harap konfirmasi dengan EDP.");
//		}
		
		if(this.elionsManager.validationPositionSPAJ(spaj)!=6) {
			errors.add("Harap cek posisi SPAJ.");
		}
//		
//		if(this.elionsManager.validationTglKirim(spaj)== null){
//			errors.add("Harap tanggal kirim di isi");
//		}
//		
		if(uwManager.selectJenisTerbitPolis(spaj)==1) {
			errors.add("Polis ini harus diterbitkan dalam bentuk SOFTCOPY (E-MAIL).");
			return errors;
		}

		//VALIDASI UNTUK PRINT POLIS UTAMA
		if( ( POLIS == printProgress || POLIS_DUPLEX == printProgress || POLIS_QUADRUPLEX == printProgress || POLIS_DMTM == printProgress || POLIS_PAS == printProgress || POLIS_TERM == printProgress || GENERATE_OUTSOURCE == printProgress)
				&& request.getParameter("authorized")==null || ALL == printProgress) {
			
			//String validasiMeterai = uwManager.validasiBeaMeterai();
			//if(validasiMeterai != null) errors.add(validasiMeterai);
			
			String lsbs_id = uwManager.selectBusinessId(spaj);
			
			if(tglCetak != null) {
				if(tglCetak.before(tgl_mulai_ssu.getTime()) && elionsManager.selectSpajCancel(spaj)==0 && !lsbs_id.equals("140")) {
					errors.add("Tidak ada history Cover Polis");
				}
			}
			
			if(currentUser.getJn_bank()==2){
				if(!lsbs_id.equals("120")){
					errors.add("Print polis, manfaat, alokasi dana, saat ini hanya untuk produk Simas Power Link. Selain Produk Simas Power Link, Silakan print Sertifikat Polis.");
				}
			}
			
			if(POLIS_DMTM == printProgress) {
				//validasi polis DM/TM, baru bisa produk 142-8 saja
				if(lsbs.equals("142") && lsdbs.equals("008")) {
					//boleh
				}else {
					errors.add("Menu ini hanya dapat digunakan untuk Polis DM/TM");
				}
			}
			
			if(POLIS_PAS == printProgress){
				if(!lsbs.equals("187") && !lsbs.equals("205")){
					errors.add("Menu ini hanya dapat digunakan untuk Produk PAS");
				}
			}
			
			if(POLIS_TERM == printProgress){
				if(! (products.productChannel88(Integer.parseInt(lsbs), Integer.parseInt(lsdbs)))){
					errors.add("Menu ini hanya dapat digunakan untuk Produk TERM & PA Channel 88");
				}
				if(lsbs.equals("196") && lsdbs.equals("002")){					
					errors.add("Untuk Produk Term ini Silahkan pilih Menu POLIS,MANFAAT,ALOKASI,DANA");
					
				}
			}

			// PROJECT: POWERSAVE & STABLE LINK 
			//untuk produk2 yg bisa cetak di cabang, terutama powersave dan stable link, hasil scan harus lengkap dulu
			/*
			if(errors.isEmpty()) {
				if(products.powerSave(lsbs) || products.stableLink(lsbs)){
					String lca_id = elionsManager.selectCabangFromSpaj(spaj);
					String dir = props.getProperty("pdf.dir.export") + "/" + lca_id;
					
					String[] daftarFileName 	= props.getProperty("upload.scan.file").split(",");
					String[] daftarFileTitle 	= props.getProperty("upload.scan.nama").split(",");
					String[] daftarFileRequired = props.getProperty("upload.scan.required").split(",");

					List<DropDown> daftarAda = FileUtils.listFilesInDirectory(dir +"/"+ spaj);
					
					for(int i=0; i<daftarFileName.length; i++){
						if(daftarFileRequired[i].equals("1")){ //apabila required, maka harus divalidasi harus ada
							boolean ada = false;
							for(DropDown d : daftarAda){
								//if contains spaj+nama file (contoh : 01200700283SPAJ.pdf
								if(d.getKey().toUpperCase().contains(spaj) && d.getKey().toUpperCase().contains(daftarFileName[i])){
									ada = true;
								}
							}
							if(!ada) errors.add("Harap Scan Dokumen " + daftarFileTitle[i] + " terlebih dahulu");
						}
					}
					
				}
			}
			*/
			
			//validasi print ulang
			if(errors.isEmpty()) {
				Map map =this.elionsManager.validationPrintPolis(spaj); 
				if(map.get("ORI")!=null) {
					errors.add("Maaf, tetapi polis ini sudah pernah dicetak.");
					request.setAttribute("printUlang", spaj);
					return errors;
				}
			}
			
		}
		
		//VALIDASI UNTUK SURAT ALOKASI DANA / SURAT EXCELLINK
		

		
		//VALIDASI UNTUK SERTIFIKAT GUTHRIE
		else if( SERTIFIKAT_GUTHRIE == printProgress ) {
			if(Integer.valueOf(businessId).intValue() != 89) {
				errors.add("Polis ini bukan produk Ultra Sejahtera (PT. Guthrie Pecconina Indonesia).");
			}
		}
		
		//VALIDASI UNTUK SERTIFIKAT POWERSAVE/STABLE LINK
		else if( SERTIFIKAT_POWERSAVE == printProgress ) {
			int isInputanBank = elionsManager.selectIsInputanBank(spaj);
			String lde_id = currentUser.getLde_id();
			
			int referal = ServletRequestUtils.getIntParameter(request, "referal", 0);
			
			if((isInputanBank == 2 || isInputanBank == 3) && referal == 0 && (currentUser.getJn_bank().intValue() == 2 || currentUser.getJn_bank().intValue() == 3)) { //apabila polis inputan bank sinarmas. untuk sinarmas sekuritas, gak perlu otorisasi lagi
				Map map =this.elionsManager.validationPrintPolis(spaj); 
				//String validasiMeterai = uwManager.validasiBeaMeterai();
				//if(validasiMeterai != null) errors.add(validasiMeterai);
				if(map.get("ORI")!=null) {
					errors.add("Maaf, tetapi pencetakan sertifikat hanya bisa dilakukan satu kali.");
					errors.add("Untuk mencetak ulang, silahkan otorisasi pencetakan ulang menggunakan user SPV / KaOps, kemudian cetak kembali menggunakan user Admin/CS.");
				}
				
				if(errors.isEmpty()) {
					//tambahan otorisasi 1 layer, masukkan password SPV
					if(isInputanBank != 3){
						errors.add("Silahkan masukkan password SPV Anda pada halaman berikut untuk melakukan pencetakan.");
					}
					request.setAttribute("printNeedAuthorization", spaj);
				}
				
			}else {
//				if(products.powerSave(businessId)) {
//					Map premiProdukUtama = this.elionsManager.selectPremiProdukUtama(spaj);
//					String kurs = (String) premiProdukUtama.get("LKU_ID");
//					BigDecimal premi = (BigDecimal) premiProdukUtama.get("MSPR_PREMIUM");
//					if((kurs.equals("01") && premi.doubleValue()>=200000000) || (kurs.equals("02") && premi.doubleValue()>=20000)) {
//					}else {
//						errors.add("Maaf, tetapi polis ini tidak memenuhi kriteria print Sertifikat.");
//					}
//				}else 
//					
//				if(!products.stableLink(businessId)){
//					errors.add("Maaf, tetapi polis ini bukan termasuk Polis Power Save / Stable Link.");
//				}
			}
		}
		
		//VALIDASI UNTUK TANDA TERIMA POLIS
//		else if( TANDA_TERIMA_POLIS == printProgress) {
//			List a = elionsManager.selectDetailBisnis(spaj);
//			Map map = (Map) a.get(0);
//			String lsdbs_number = (String) map.get("DETBISNIS");
//			List	validasi = validasi(TANDA_TERIMA_POLIS, spaj, businessId, request);
		
//			String reportUrl = "";
		
//			if(request.getParameter("ulang")!=null) TANDA_TERIMA_POLIS= POLIS_QUADRUPLEX;
//			else if(request.getParameter("finish")!=null) TANDA_TERIMA_POLIS= FINISH;
//		
//			if(validasi.size() > 0) {
//				return (List) new ModelAndView("uw/printpolis/printpolis_err", "error", validasi);
//
//			}
//			if(
//					((businessId.equals("155") && "002, 003".indexOf(lsdbs_number)>-1) ||
//					(businessId.equals("158") && "008, 009".indexOf(lsdbs_number)>-1))
//			) {
//				if(uwManager.validasiSpajAsli(spaj) == 0) {
//					errors.add("Maaf, tetapi dokumen SPAJ asli yang original (bukan copy/fax) harus diterima terlebih dahulu sebelum mencetak tanda terima.");
//				}
//			}				
//		}

		//TODO (Yusuf) -> UNTUK DEBUGGING, DISABLE
		return errors;
		//return new ArrayList();
	}

	//FUNGSI2 PEMBANTU
	private int nextProgress(int printProgress, List reportList) {
		for(int i=0; i<reportList.size(); i++) {
			Map baris = (HashMap) reportList.get(i);
			int kode = ((Integer) baris.get("kode"));
			if(ALL == kode) kode = -2;
			if(kode == printProgress && i != (reportList.size()-1)) {
				return ((Integer) ((HashMap) reportList.get(i+1)).get("kode"));
			}
		}
		return printProgress;
	}
	
	private int prevProgress(int printProgress, List reportList) {
		for(int i=reportList.size()-1; i>=0; i--) {
			Map baris = (HashMap) reportList.get(i);
			int kode = ((Integer) baris.get("kode"));
			if(POLIS_QUADRUPLEX == kode) return ALL;
			else if(kode == printProgress && i != 0) {
				return ((Integer) ((HashMap) reportList.get(i-1)).get("kode"));
			}
		}
		return printProgress;
	}	
	
	public ModelAndView cek_spv(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		Map map = new HashMap ();
		Date now = this.elionsManager.selectSysdateSimple();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		map.put("tgl", now);
		map.put("status", this.elionsManager.selectStatusPolis(ServletRequestUtils.getIntParameter(request, "status", 1)));
		
		if(request.getParameter("submit")!=null) {
			String password = ServletRequestUtils.getStringParameter(request, "password", "");
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj");
			Map data_valid = (HashMap) this.elionsManager.select_validbank(Integer.parseInt(currentUser.getLus_id()));
			String pass_spv = (String)data_valid.get("PASS1");
			int lus = 724;
			pass_spv = uwManager.validationOtorisasiSekuritas(lus);

			if(map.get("err") == null) {
				//validasi2
				if(password.equals("")){
					map.put("err", "Password masih kosong");
				}else if (!password.equalsIgnoreCase(pass_spv)){
					map.put("err", "Password yang anda masukkan salah");
				}else {
					//proses buka halaman edit spaj.
					try {
						PrintWriter out = response.getWriter();
						AksesHist a = new AksesHist();
						a.setLus_id(lus);
						a.setMsah_date(currentUser.getLoginTime());
						a.setMsah_jenis(1);
						a.setMsah_spaj(null);
						a.setMsah_uri("/bac/edit.htm");
						elionsManager.insertAksesHist(a);
						String Url = request.getContextPath()+"/bac/edit.htm?data_baru=true&showSPAJ="+spaj;
						response.sendRedirect(Url);
						return null;
						
					} catch (IOException e) {
						logger.error("ERROR :", e);
					}
				}
			}
			
			map.put("password", password);
			map.put("spaj", spaj);
			
		}
		
		return new ModelAndView("uw/printpolis/printpolis_otospv", map);
	}
	
	/**
	  Rahmayanti
	 
	 * 
	 *endorsment polis
	 */	
	public ModelAndView suratendorsment(HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		HttpSession session=request.getSession();
		User currentUser = (User) request.getSession().getAttribute("currentUser");	
		
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj","");
		String str1, str2, str3, s_spaj, s_rep ="";
		str1=spaj.substring(6,spaj.length());
		str2=spaj.substring(2,6);	
		str3=spaj.substring(0,2);
		s_spaj = str3+"."+str2+"."+str1;
		Integer endorse=1;
		
		Map<String, Object> params = new HashMap<String, Object>();
		ArrayList data=new ArrayList();
		
		
		if(request.getParameter("savePenyakit")!=null)
		{
			try
			{							
				String penyakit=ServletRequestUtils.getStringParameter(request, "penyakit","");
				if(!penyakit.equals(""))
				{
					if(NumberUtils.isNumber(penyakit))
					{
						map.put("error", "Nama Penyakit Tidak Boleh Angka");
					}
					else
					{
						Endors cek_endors = uwManager.selectMstEndors(spaj, "", "");
						if(cek_endors==null)
						{
							Integer mspo_provider=uwManager.selectGetMspoProvider(spaj);	
							if(mspo_provider!=2)
								bacManager.updateMspoProvider(spaj, 2);
							uwManager.prosesEndorsemen(spaj, Integer.parseInt(uwManager.selectBusinessId(spaj)),endorse);
							bacManager.updateMspoProvider(spaj, mspo_provider);								
						}
							response.sendRedirect(request.getContextPath()+"/reports/endors_penyakit.pdf?spaj="+spaj+"&penyakit="+penyakit);
							elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK ENDORS PENYAKIT", spaj,0);
					}
				}
				else
				{
					map.put("error", "Nama Penyakit Harus Diisi");					
				}
			}catch (Exception e) {
				logger.error("ERROR :", e);
			}
			
		}
		
		else if(request.getParameter("saveUp")!=null)
		{	
			try
			{
						
				String up=ServletRequestUtils.getStringParameter(request, "up","");
				String pasal=ServletRequestUtils.getStringParameter(request, "pasal","");
				if(!up.equals("")&&!up.equals("0")&&!pasal.equals(""))
				{
				
					String big=new String(up.replaceAll("\\.", ""));
					int n_up = Integer.parseInt(big);
					Endors cek_endors = uwManager.selectMstEndors(spaj, "", "");
					if(cek_endors==null)
					{
						Integer mspo_provider=uwManager.selectGetMspoProvider(spaj);	
						if(mspo_provider!=2)
							bacManager.updateMspoProvider(spaj, 2);
						uwManager.prosesEndorsemen(spaj, Integer.parseInt(uwManager.selectBusinessId(spaj)),endorse);
						bacManager.updateMspoProvider(spaj, mspo_provider);		
						bacManager.updateMstProductInsured(spaj, n_up);
					}		
					response.sendRedirect(request.getContextPath()+"/reports/endors_ssu.pdf?spaj="+spaj+"&up="+big+"&pasal="+pasal);
					elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "CETAK ENDORS SSU", spaj,0);
				}
				else
				{
				
					if(pasal.equals(""))
						map.put("error", "Pasal Harus Diisi");
					else
						map.put("error", "Nominal Uang Pertanggungan Harus Diisi");
				}
			}
			catch (Exception e) {
				logger.error("ERROR :", e);
			}
		}	
		map.put("spaj", s_spaj);
		return new ModelAndView("uw/surat_endorsment",map);	
		
	}
	
	//Rahmayanti - QC
	public ModelAndView qcControl(HttpServletRequest request, HttpServletResponse response)throws Exception{
		HashMap map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");	
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj","");
		String flag = ServletRequestUtils.getStringParameter(request, "flag", null);
		List errors = new ArrayList();
		
		if(!currentUser.getLde_id().equals("11")){
			errors.add("Hanya User U/W yang bisa melakukan proses ini!");
			return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
		}
		else{
			List selectMstPositionQc;
			if(request.getParameter("btnQc1")!=null){
				selectMstPositionQc = bacManager.selectMstPositionQc(spaj,0);
				if(!selectMstPositionQc.isEmpty()){
	    			errors.add("QA 1 Sudah di Proses");
	    			return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
				}
				else{
					elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "QC1_"+currentUser.getLus_full_name(), spaj, 0);
				}
			}
			if(request.getParameter("btnQc2")!=null){
				selectMstPositionQc = bacManager.selectMstPositionQc(spaj,1);
				if(!selectMstPositionQc.isEmpty()){
					errors.add("QA 2 Sudah di Proses");
	    			return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
				}
				else{
					elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "QC2_"+currentUser.getLus_full_name(), spaj, 0);
				}
			}
		}
		
		Map select_p = elionsManager.selectPemegang(spaj);
		String pemegang = (String)select_p.get("MCL_FIRST");
		Map select_t = elionsManager.selectTertanggung(spaj);
		String tertanggung = (String) select_t.get("MCL_FIRST");
		List select_ahliwaris = bacManager.selectMstBenefeciary(spaj);
		List select_peserta =  bacManager.select_semua_mst_peserta2(spaj);
		map.put("select_ahliwaris", select_ahliwaris);
		map.put("select_peserta", select_peserta);
		map.put("pemegang", pemegang);
		map.put("tertanggung", tertanggung);
		map.put("flag",flag);
		return new ModelAndView("uw/printpolis/qc",map);		
	}
	
	public ModelAndView printfromcfl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String success  = ""; 
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj");
		response.sendRedirect(request.getContextPath()+"/reports/printall.pdf?spaj="+spaj+"&flag="+1);		
		return null;
	}
	
	//Mark Valentino 20190322 - Untuk memanggil private function "validasi" dari class lain
    public List publicValidasi(int printProgress, String spaj, String businessId, HttpServletRequest request) throws Exception  {
        return this.validasi(printProgress, spaj, businessId, request);
    }	
}