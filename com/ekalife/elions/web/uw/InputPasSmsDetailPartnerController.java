package com.ekalife.elions.web.uw;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.bac.support.form_agen;
import com.ekalife.utils.CheckSum;
import com.ekalife.utils.Common;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.parent.ParentFormController;

import id.co.sinarmaslife.std.spring.util.Email;

public class InputPasSmsDetailPartnerController extends ParentFormController{
	private long accessTime;
	protected final Log logger = LogFactory.getLog(getClass());
	private Email email;
	
	public void setEmail(Email email) {
		this.email = email;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		
		Pas pas=(Pas)cmd;
		
		String tambah_input_asuransi = request.getParameter("tambah_input_asuransi");
		
		// menentukan pas save langsung muncul konfirmasi transfer ke uw pada pas_detail
		String flag_posisi=ServletRequestUtils.getStringParameter(request, "flag_posisi",null);
		request.setAttribute("flag_posisi", flag_posisi);
		
		form_agen d_agen= new form_agen(); 
		
		Date nowDate = elionsManager.selectSysdate();
		
		if(pas.getMsp_pas_beg_date() == null){
			nowDate = elionsManager.selectSysdate();
		}else{
			nowDate = pas.getMsp_pas_beg_date();
		}
		
		//khusus untuk BP, tidak perlu ada validasi
//		Agen agen = new Agen();
//		String agen_hsl = "";
//		if(pas.getMsag_id() == null)pas.setMsag_id("");
//		if(!"".equals(pas.getMsag_id())){
//			agen = uwManager.select_detilagen2(pas.getMsag_id());
//			if(agen.getMsag_id() != null){
//				agen_hsl = d_agen.sertifikasi_agen(pas.getMsag_id(), 0,agen.getMsag_sertifikat(),agen.getMsag_berlaku(), nowDate);
//			}else{
//				err.reject("","Agen : Kode Agen tidak berlaku");
//			}
//		}
//		
//		if(!"".equals(agen_hsl)){
//			err.reject("","Agen : Kode Agen tidak berlaku");
//		}
		
		if(pas.getMsp_agama() == null)pas.setMsp_agama("");
		if(pas.getMsp_occupation_else() == null)pas.setMsp_occupation_else("");
		pas.setLscb_id(3);
		pas.setLsre_id(1);
	//	pas.setMsp_flag_cc(2);
		//pas.setPribadi(1);
		
		// pas.setProduk(5);		
		if(pas.getJenis_bp() != null){
			if(pas.getJenis_bp() == 1){
				pas.setProduk(6);
				pas.setPremi("74000");
				pas.setMsp_premi("74000");
		     } else{
					pas.setProduk(5);
					pas.setPremi("74000");	
					pas.setMsp_premi("74000");
			} }else {
				pas.setProduk(5);
				pas.setPremi("74000");	
				pas.setMsp_premi("74000");
		}
		if(pas.getLsre_id() == 1){
			pas.setMsp_full_name(pas.getMsp_pas_nama_pp());
			pas.setMsp_pas_tmp_lhr_tt(pas.getMsp_pas_tmp_lhr_pp());
			pas.setMsp_date_of_birth(pas.getMsp_pas_dob_pp());
			pas.setMsp_identity_no_tt(pas.getMsp_identity_no());
		}			
		
		//PAS
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_nama_pp", "","PAS : Nama Pemegang harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_tmp_lhr_pp", "","PAS : Tempat Lahir Pemegang harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_dob_pp", "","PAS : Tanggal Lahir Pemegang harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_full_name", "","PAS : Nama Tertanggung harus diisi");
//		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_email", "","PAS : Alamat Email harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_identity_no", "","PAS : No. Identitas harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_tmp_lhr_tt", "","PAS : Tempat Lahir Tertanggung harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_date_of_birth", "","PAS : Tgl. Lahir Tertanggung harus diisi");
		if(pas.getLsag_id() == 6){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_agama", "","PAS : Agama lainnya harus diisi");
		}
		if(pas.getMsp_occupation().equals("LAINNYA")){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_occupation_else", "","PAS : Pekerjaan lainnya harus diisi");
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_postal_code", "","PAS : Kode Pos harus diisi");
		//ValidationUtils.rejectIfEmptyOrWhitespace(err, "pin", "","PAS : PIN harus diisi");
		//ValidationUtils.rejectIfEmptyOrWhitespace(err, "no_kartu", "","PAS : No. Kartu harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_address_1", "","PAS : Alamat harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_area_code_rumah", "","PAS : Kode Area harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_phone_number", "","PAS : No. Telepon harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_mobile", "","PAS : No. HP harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_identity_no", "","PAS : No. Identitas harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_city", "","PAS : Kota harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "lscb_id", "","PAS : Pembayaran harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_flag_cc", "","PAS : Cara Bayar harus diisi");
		
		Integer umurPp = 0;
		Integer umurTt = 0;
		try{
			f_hit_umur umr = new f_hit_umur();
			
			SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
			SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
			SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
			Date sysdate = elionsManager.selectSysdate();
			int tahun2 = Integer.parseInt(sdfYear.format(sysdate));
			int bulan2 = Integer.parseInt(sdfMonth.format(sysdate));
			int tanggal2 = Integer.parseInt(sdfDay.format(sysdate));
			
			if(pas.getMsp_pas_dob_pp() != null){
				
				int tahun1 = Integer.parseInt(sdfYear.format(pas.getMsp_pas_dob_pp()));
				int bulan1 = Integer.parseInt(sdfMonth.format(pas.getMsp_pas_dob_pp()));
				int tanggal1 = Integer.parseInt(sdfDay.format(pas.getMsp_pas_dob_pp()));
				
				umurPp=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
			}
			if(pas.getMsp_date_of_birth() != null){
				int tahun1 = Integer.parseInt(sdfYear.format(pas.getMsp_date_of_birth()));
				int bulan1 = Integer.parseInt(sdfMonth.format(pas.getMsp_date_of_birth()));
				int tanggal1 = Integer.parseInt(sdfDay.format(pas.getMsp_date_of_birth()));
				umurTt=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
			}
		}catch(Exception e){
			
		}
		
		if(!(umurPp>=17 && umurPp<=85)){ //batas umur 17 s/d 85 tahun
			err.reject("","PAS : Umur pemegang minimal 17 tahun & maksimal 85 tahun");
		}
		if(!(umurTt>=17 && umurTt<=80)){ //batas umur 17 s/d 80 tahun
			err.reject("","PAS : Umur tertanggung minimal 17 tahun & maksimal 80 tahun");
		}
		
		if(!Common.isEmpty(pas.getMsp_pas_email())){
			try {
				InternetAddress.parse(pas.getMsp_pas_email().trim());
			} catch (AddressException e) {
				err.reject("","PAS : email tidak valid");
			} finally {
				if(!pas.getMsp_pas_email().trim().toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
					err.reject("","PAS : email tidak valid");
				}
			}
		}
		try{
			pas.setMsp_postal_code(pas.getMsp_postal_code().trim());
			int x = Integer.parseInt( pas.getMsp_postal_code());
		}catch(Exception e){
			err.reject("","PAS : Kode Pos harus diisi angka");
		}
		
		if(!Common.isEmpty(pas.getMsp_pas_phone_number())){
			if(!Common.validPhone(pas.getMsp_pas_phone_number().trim())){
				err.reject("","PAS : No. Telepon harus diisi angka");
			}
		}
		if(!Common.isEmpty(pas.getMsp_pas_phone_number())){
			try{
				String x = pas.getMsp_pas_phone_number().trim().replace("0", "").replace("-", "");
				if(Common.isEmpty(x)){
					err.reject("","PAS : No. Telepon tidak boleh diisi '0' atau '-'");
				}
			}catch(Exception e){
				err.reject("","PAS : No. Telepon tidak boleh diisi '0' atau '-'");
			}
		}
		
		String lsdbs_number = "x";
		if(pas.getPin() != null){
			//CheckSum checkSum = new CheckSum();
			lsdbs_number = uwManager.selectCekPin(pas.getPin(), 1);
			if(lsdbs_number == null)lsdbs_number = "x";
		}
//		if("x".equals(lsdbs_number)){
//			err.reject("","PAS : Produk tidak ditemukan. Mohon cek PIN");
//		}
		
//		String lsdbs_number_no_kartu = "x";
//		if(pas.getNo_kartu() != null){
//			//CheckSum checkSum = new CheckSum();
//			lsdbs_number_no_kartu = uwManager.selectCekNoKartu(pas.getNo_kartu(), 1);
//			if(lsdbs_number_no_kartu == null)lsdbs_number_no_kartu = "x";
//		}
//		if("x".equals(lsdbs_number) || !lsdbs_number.equals(lsdbs_number_no_kartu)){
//			err.reject("","PAS : Produk tidak ditemukan. Mohon cek No. Kartu & PIN");
//		}
		
		//AGEN
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msag_id", "","AGEN : Kode Agen harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msag_id_pp", "","AGEN : Kode Agen Referensi harus diisi");
		
		//REKENING
		if(!"".equals(pas.getLsbp_id()) && !"0".equals(pas.getLsbp_id())){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "lsbp_id", "","REKENING : Bank harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_no_rekening", "","REKENING : No. Rekening harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_rek_cabang", "","REKENING : Cabang harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_rek_kota", "","REKENING : Kota harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_rek_nama", "","REKENING : Atas Nama harus diisi");
		}
		if(pas.getMsp_flag_cc() != null && (pas.getMsp_flag_cc() == 2 || pas.getMsp_flag_cc() == 1)){//tabungan/kartu kredit
			if(!"BPGETBP".equals(pas.getJenis_pas())){
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "lsbp_id_autodebet", "","REKENING (AUTODEBET) : Bank harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_no_rekening_autodebet", "","REKENING (AUTODEBET) : No. Rekening harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_tgl_debet", "","REKENING (AUTODEBET) : Tanggal debet harus diisi");
				if(pas.getMsp_flag_cc() == 1){
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_tgl_valid", "","REKENING (AUTODEBET) : Tanggal valid harus diisi");
				}
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_rek_nama_autodebet", "","REKENING (AUTODEBET) : Atas Nama harus diisi");
			}
		}
		
		
		String bank_pp1=pas.getLsbp_id();
		String nama_bank_rekclient="";
		if(bank_pp1!=null){
			Map data1= (HashMap) this.elionsManager.select_bank1(bank_pp1);
			if (data1!=null){		
				nama_bank_rekclient = (String)data1.get("BANK_NAMA");
				pas.setLsbp_nama(nama_bank_rekclient);
			}
		}
		
		String bank_pp2=pas.getLsbp_id_autodebet();
		String nama_bank_rekclient_autodebet="";
		if(bank_pp2!=null){
			Map data1= (HashMap) this.elionsManager.select_bank2(bank_pp2);
			if (data1!=null){		
				nama_bank_rekclient_autodebet = (String)data1.get("BANK_NAMA");
				pas.setLsbp_nama_autodebet(nama_bank_rekclient_autodebet);
			}
		}
		
		request.setAttribute("kode_nama_list",DroplistManager.getInstance().get("KODE_NAMA.xml","ID",request));
		request.setAttribute("kode_alamat_list",DroplistManager.getInstance().get("KODE_ALAMAT.xml","ID",request));
		request.setAttribute("kode_okupasi_list",DroplistManager.getInstance().get("KODE_OKUPASI.xml","ID",request));
		request.setAttribute("kode_obyek_sekitar_list",DroplistManager.getInstance().get("KODE_OBYEK_SEKITAR.xml","ID",request));
		request.setAttribute("carabayar_pas",DroplistManager.getInstance().get("CARABAYAR_PAS.xml","ID",request));
		request.setAttribute("autodebet_pas",DroplistManager.getInstance().get("AUTODEBET_PAS.xml","ID",request));
		request.setAttribute("relasi_pas",DroplistManager.getInstance().get("RELATION_PAS.xml","ID",request));
		request.setAttribute("produk_pas",DroplistManager.getInstance().get("PRODUK_PAS.xml","ID",request));
		request.setAttribute("sumber_pendanaan",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","ID",request));
		request.setAttribute("bidang_industri",DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","ID",request));
		request.setAttribute("pekerjaan",DroplistManager.getInstance().get("PEKERJAAN_PAS.xml","ID",request));
		request.setAttribute("select_agama", elionsManager.selectDropDown("eka.lst_agama", "lsag_id", "lsag_name", "", "lsag_name", null));
		request.setAttribute("gelar_company", elionsManager.selectGelar(1));
		request.setAttribute("select_negara", elionsManager.selectDropDown("eka.lst_negara", "lsne_id", "lsne_note", "", "lsne_urut", null));
		request.setAttribute("select_pendidikan", elionsManager.selectDropDown("eka.lst_education", "lsed_id", "lsed_name", "", "lsed_name", null));
		request.setAttribute("select_marital",DroplistManager.getInstance().get("MARITAL.xml","ID",request));
		request.setAttribute("select_pekerjaan",DroplistManager.getInstance().get("KLASIFIKASI_PEKERJAAN.xml","",request));
		request.setAttribute("select_identitas", elionsManager.selectDropDown("eka.lst_identity", "lside_id", "lside_name", "", "lside_id", null));
		
		request.setAttribute("addError", "addError");
	}

	protected ModelAndView onSubmit( HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors ) throws Exception
    {

		Pas pas=(Pas)cmd;
		//User currentUser = (User) session.getAttribute("currentUser");
		Map<String, Object> map = new HashMap<String, Object>();
		
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		Integer flagNew = 1;
		map.put("user_id", lus_id);
		
		Cmdeditbac edit = new Cmdeditbac();
		
		pas.setLscb_id(3);
		pas.setLsre_id(1);
	//	pas.setMsp_flag_cc(2);
		//pas.setPribadi(1);
		
		//pas.setProduk(5);
		if(pas.getJenis_bp() != null){
			if(pas.getJenis_bp() == 1){
				pas.setProduk(6);
				pas.setPremi("74000");
				pas.setMsp_premi("74000");
		     } else{
					pas.setProduk(5);
					pas.setPremi("74000");	
					pas.setMsp_premi("74000");
			} }else {
				pas.setProduk(5);
				pas.setPremi("74000");
				pas.setMsp_premi("74000");
		}
		
		String tambah_input_asuransi = request.getParameter("tambah_input_asuransi");
		
		// menentukan pas save langsung muncul konfirmasi transfer ke uw pada pas_detail
		String flag_posisi=ServletRequestUtils.getStringParameter(request, "flag_posisi",null);
		request.setAttribute("flag_posisi", flag_posisi);
		
		if(pas.getReg_spaj() == null)pas.setReg_spaj("");
		if(pas.getMsp_warga_else() == null)pas.setMsp_warga_else("");
		if(pas.getMsp_pendidikan_else() == null)pas.setMsp_pendidikan_else("");
		if(pas.getMsp_occupation_else() == null)pas.setMsp_occupation_else("");
		if(pas.getMsp_company_name() == null)pas.setMsp_company_name("");
		if(pas.getMsp_company_jabatan() == null)pas.setMsp_company_jabatan("");
		if(pas.getMsp_company_address() == null)pas.setMsp_company_address("");
		if(pas.getMsp_company_postal_code() == null)pas.setMsp_company_postal_code("");
		if(pas.getPribadi() == null)pas.setPribadi(0);
		if(pas.getMsp_cek_ktp() == null)pas.setMsp_cek_ktp(0);
		if(pas.getMsp_cek_kk() == null)pas.setMsp_cek_kk(0);
		if(pas.getMsp_cek_npwp() == null)pas.setMsp_cek_npwp(0);
		if(pas.getMsp_cek_bukti_bayar() == null)pas.setMsp_cek_bukti_bayar(0);
		if(pas.getMsp_cek_rekening() == null)pas.setMsp_cek_rekening(0);
		if(pas.getMsp_cek_ktp_uw() == null)pas.setMsp_cek_ktp_uw(0);
		if(pas.getMsp_cek_kk_uw() == null)pas.setMsp_cek_kk_uw(0);
		if(pas.getMsp_cek_npwp_uw() == null)pas.setMsp_cek_npwp_uw(0);
		if(pas.getMsp_cek_bukti_bayar_uw() == null)pas.setMsp_cek_bukti_bayar_uw(0);
		if(pas.getMsp_cek_rekening_uw() == null)pas.setMsp_cek_rekening_uw(0);
		if(pas.getMsp_cek_srt_keterangan() == null)pas.setMsp_cek_srt_keterangan(0);
		if(pas.getMsp_cek_srt_keterangan_uw() == null) pas.setMsp_cek_srt_keterangan_uw(0);
		if(pas.getMsp_cek_akte_kelahiran() == null)pas.setMsp_cek_akte_kelahiran(0);
		if(pas.getMsp_cek_akte_kelahiran_uw() == null) pas.setMsp_cek_akte_kelahiran_uw(0);
		
		try{
			if("0".equals(tambah_input_asuransi)){
				pas.setMsp_fire_name("");
				if(Common.isEmpty(pas.getMsp_fire_id())){
					pas.setMsp_fire_code_name("");
					pas.setMsp_fire_identity("");
					pas.setMsp_fire_date_of_birth2("");
					pas.setMsp_fire_date_of_birth(null);
					pas.setMsp_fire_occupation2("");
					pas.setMsp_fire_occupation("");
					pas.setMsp_fire_type_business2("");
					pas.setMsp_fire_type_business("");
					pas.setMsp_fire_source_fund2("");
					pas.setMsp_fire_source_fund("");
					pas.setMsp_fire_addr_code("");
					pas.setMsp_fire_address_1("");
					pas.setMsp_fire_postal_code("");
					pas.setMsp_fire_phone_number("");
					pas.setMsp_fire_mobile("");
					pas.setMsp_fire_email("");
					pas.setMsp_fire_insured_addr_code("");
					pas.setMsp_fire_insured_addr("");
					pas.setMsp_fire_insured_addr_no("");
					pas.setMsp_fire_insured_postal_code("");
					pas.setMsp_fire_insured_city("");
					pas.setMsp_fire_insured_phone_number("");
					pas.setMsp_fire_insured_addr_envir(null);
					pas.setMsp_fire_ins_addr_envir_else("");
					pas.setMsp_fire_okupasi("");
					pas.setMsp_fire_okupasi_else("");
					pas.setMsp_fire_beg_date(null);
					pas.setMsp_fire_end_date(null);
				}
			}else{
				if(Common.isEmpty(pas.getMsp_fire_id())){
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					Date begDate = (Date) pas.getMsp_pas_beg_date().clone();
					Date endDate = (Date) pas.getMsp_pas_beg_date().clone();
					endDate.setMonth(endDate.getMonth() + 6);
					endDate.setDate(endDate.getDate() - 1);
					pas.setMsp_fire_beg_date(begDate);
					pas.setMsp_fire_end_date(endDate);
				}
			}
			
			pas.setLus_id(lus_id);
			pas.setLus_login_name(user.getLus_full_name());
	//		pas.setLspd_id(1);
	//		pas.setLssp_id(10);//POLICY IS BEING PROCESSED 
			pas.setMsp_kode_sts_sms("00");
			Date endDatePas = (Date) pas.getMsp_pas_beg_date().clone();
			endDatePas.setYear(endDatePas.getYear()+1);
			endDatePas.setDate(endDatePas.getDate()-1);
			pas.setMsp_pas_end_date(endDatePas);
				pas = uwManager.updatePas(pas);//request, pas, errors,"input",user,errors);
				request.setAttribute("successMessage","edit sukses. Fire Id : "+pas.getMsp_fire_id());
			//uwManager.updatePas1(pas);//request, pas, errors,"input",user,errors);
		}catch (Exception e) {
			request.setAttribute("successMessage","edit gagal");
		}
		
//		schedulerPas();
		
		//request.setAttribute("successMessage","sukses");
		request.setAttribute("kode_nama_list",DroplistManager.getInstance().get("KODE_NAMA.xml","ID",request));
		request.setAttribute("kode_alamat_list",DroplistManager.getInstance().get("KODE_ALAMAT.xml","ID",request));
		request.setAttribute("kode_okupasi_list",DroplistManager.getInstance().get("KODE_OKUPASI.xml","ID",request));
		request.setAttribute("kode_obyek_sekitar_list",DroplistManager.getInstance().get("KODE_OBYEK_SEKITAR.xml","ID",request));
		request.setAttribute("carabayar_pas",DroplistManager.getInstance().get("CARABAYAR_PAS.xml","ID",request));
		request.setAttribute("autodebet_pas",DroplistManager.getInstance().get("AUTODEBET_PAS.xml","ID",request));
		request.setAttribute("relasi_pas",DroplistManager.getInstance().get("RELATION_PAS.xml","ID",request));
		request.setAttribute("produk_pas",DroplistManager.getInstance().get("PRODUK_PAS.xml","ID",request));
		request.setAttribute("sumber_pendanaan",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","ID",request));
		request.setAttribute("bidang_industri",DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","ID",request));
		request.setAttribute("pekerjaan",DroplistManager.getInstance().get("PEKERJAAN_PAS.xml","ID",request));
		
		return new ModelAndView(
        "uw/input_pas_sms_detail_partner").addObject("cmd",pas);

    }
	
	protected void initBinder(HttpServletRequest arg0, ServletRequestDataBinder binder) throws Exception {
		logger.debug("EditBacController : initBinder");
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		logger.debug("EditBacController : formBackingObject");
        this.accessTime = System.currentTimeMillis();
        HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		Pas pas=new Pas();
		String msp_id=ServletRequestUtils.getStringParameter(request, "msp_id",null);
		
		// menentukan pas save langsung muncul konfirmasi transfer ke uw pada pas_detail
		String flag_posisi=ServletRequestUtils.getStringParameter(request, "flag_posisi",null);
		request.setAttribute("flag_posisi", flag_posisi);
		
		
		Map<String, Object> refData = new HashMap<String, Object>();
		//schedulerPas();
		request.setAttribute("kode_nama_list",DroplistManager.getInstance().get("KODE_NAMA.xml","ID",request));
		request.setAttribute("kode_alamat_list",DroplistManager.getInstance().get("KODE_ALAMAT.xml","ID",request));
		request.setAttribute("kode_okupasi_list",DroplistManager.getInstance().get("KODE_OKUPASI.xml","ID",request));
		request.setAttribute("kode_obyek_sekitar_list",DroplistManager.getInstance().get("KODE_OBYEK_SEKITAR.xml","ID",request));
		request.setAttribute("carabayar_pas",DroplistManager.getInstance().get("CARABAYAR_PAS.xml","ID",request));
		request.setAttribute("autodebet_pas",DroplistManager.getInstance().get("AUTODEBET_PAS.xml","ID",request));
		request.setAttribute("relasi_pas",DroplistManager.getInstance().get("RELATION_PAS.xml","ID",request));
		request.setAttribute("produk_pas",DroplistManager.getInstance().get("PRODUK_PAS.xml","ID",request));
		request.setAttribute("sumber_pendanaan",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","ID",request));
		request.setAttribute("bidang_industri",DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","ID",request));
		request.setAttribute("pekerjaan",DroplistManager.getInstance().get("PEKERJAAN_PAS.xml","ID",request));
		request.setAttribute("select_agama", elionsManager.selectDropDown("eka.lst_agama", "lsag_id", "lsag_name", "", "lsag_name", null));
		request.setAttribute("gelar_company", elionsManager.selectGelar(1));
		request.setAttribute("select_negara", elionsManager.selectDropDown("eka.lst_negara", "lsne_id", "lsne_note", "", "lsne_urut", null));
		request.setAttribute("select_pendidikan", elionsManager.selectDropDown("eka.lst_education", "lsed_id", "lsed_name", "", "lsed_name", null));
		request.setAttribute("select_marital",DroplistManager.getInstance().get("MARITAL.xml","ID",request));
		request.setAttribute("select_pekerjaan",DroplistManager.getInstance().get("KLASIFIKASI_PEKERJAAN.xml","",request));
		request.setAttribute("select_identitas", elionsManager.selectDropDown("eka.lst_identity", "lside_id", "lside_name", "", "lside_id", null));
//		String jenisQuery;
//		if(currentUser.getLde_id().equals("29") && currentUser.getLca_id().equals("01")){
//			jenisQuery = "bpgetbp";
//		}else{
//			jenisQuery = "partner";	
//		}
		List<Pas> pasList = uwManager.selectAllPasList(msp_id, null, null, null, null, null, null, null, null,null,null);
		pas = pasList.get(0);
		pas.setKode_ao(pas.getMsag_id_pp());
		
		String msag_id_pp_cari = request.getParameter("msag_id_pp_cari");
		
		if(msag_id_pp_cari != null){
			
//			if(currentUser.getLde_id().equals("29") && currentUser.getLca_id().equals("01")){
//				String jenisQuery;
//				jenisQuery = "bpgetbp";
//				List<Pas> pasList = uwManager.selectAllPasList(msp_id, null, null, null, null, null, null, jenisQuery,null,null,null);
//				if(pasList != null && pasList.size()>0){
//					pas = pasList.get(0);
//				}
//			}
//			else{
			List<Map> agenPas = new ArrayList<Map>();
			
			//a.msag_id, b.mcl_first, b.MSPE_PLACE_BIRTH, b.MSPE_DATE_BIRTH, b.MSPE_SEX, b.MSPE_NO_IDENTITY, c.ALAMAT_RUMAH, c.KOTA_RUMAH,
			//c.KD_POS_RUMAH, c.TELPON_RUMAH, c.NO_HP, c.EMAIL, a.LBN_ID, (select x.lsbp_id from eka.lst_bank x where x.lbn_id=a.lbn_id) lsbp_id
			
			// PASBP#09117758917811#Albert Einstain#14041985#0510057891526789# Jl. Kota Bambu No. 14 RT.012/007 Sawah Besar #Jakarta Barat#12000
			agenPas = uwManager.selectDetailAgen(msag_id_pp_cari);
			if(!agenPas.isEmpty()){
			if( agenPas.get(0).get("MSAG_ID") != null){
				pas.setMsag_id(agenPas.get(0).get("MSAG_ID").toString());
				pas.setMsag_id_pp(agenPas.get(0).get("MSAG_ID").toString());
			
			if(agenPas.get(0).get("MCL_FIRST") != null){
				pas.setMsp_pas_nama_pp(agenPas.get(0).get("MCL_FIRST").toString());
				pas.setMsp_full_name(agenPas.get(0).get("MCL_FIRST").toString());
				pas.setMsp_rek_nama(agenPas.get(0).get("MCL_FIRST").toString());
				pas.setMsp_rek_nama_autodebet(agenPas.get(0).get("MCL_FIRST").toString());
			}
			if(agenPas.get(0).get("MSPE_PLACE_BIRTH") != null){
				pas.setMsp_pas_tmp_lhr_pp(agenPas.get(0).get("MSPE_PLACE_BIRTH").toString());
				pas.setMsp_pas_tmp_lhr_tt(agenPas.get(0).get("MSPE_PLACE_BIRTH").toString());
			}
			if(agenPas.get(0).get("MSPE_DATE_BIRTH") != null){
				pas.setMsp_pas_dob_pp((Date) agenPas.get(0).get("MSPE_DATE_BIRTH"));
				pas.setMsp_date_of_birth((Date) agenPas.get(0).get("MSPE_DATE_BIRTH"));
			}
			if(agenPas.get(0).get("MCL_FIRST") != null)pas.setMsp_full_name(agenPas.get(0).get("MCL_FIRST").toString());
			if(agenPas.get(0).get("MSPE_PLACE_BIRTH") != null)pas.setMsp_pas_tmp_lhr_tt(agenPas.get(0).get("MSPE_PLACE_BIRTH").toString());
			if(agenPas.get(0).get("MSPE_DATE_BIRTH") != null)pas.setMsp_date_of_birth((Date) agenPas.get(0).get("MSPE_DATE_BIRTH"));
			if(agenPas.get(0).get("MSPE_SEX") != null)pas.setMsp_sex_pp(Integer.parseInt(agenPas.get(0).get("MSPE_SEX").toString()));
			if(agenPas.get(0).get("MSPE_NO_IDENTITY") != null)pas.setMsp_identity_no(agenPas.get(0).get("MSPE_NO_IDENTITY").toString());
			if(agenPas.get(0).get("ALAMAT_RUMAH") != null)pas.setMsp_address_1(agenPas.get(0).get("ALAMAT_RUMAH").toString());
			if(agenPas.get(0).get("KOTA_RUMAH") != null)pas.setMsp_city(agenPas.get(0).get("KOTA_RUMAH").toString());
			if(agenPas.get(0).get("KD_POS_RUMAH") != null)pas.setMsp_postal_code(agenPas.get(0).get("KD_POS_RUMAH").toString());
			if(agenPas.get(0).get("TELPON_RUMAH") != null)pas.setMsp_pas_phone_number(agenPas.get(0).get("TELPON_RUMAH").toString());
			if(agenPas.get(0).get("NO_HP") != null)pas.setMsp_mobile(agenPas.get(0).get("NO_HP").toString());
			if(agenPas.get(0).get("EMAIL") != null)pas.setMsp_pas_email(agenPas.get(0).get("EMAIL").toString());
			if(agenPas.get(0).get("LSBP_ID") != null){
				pas.setLsbp_id(agenPas.get(0).get("LSBP_ID").toString());
				pas.setLsbp_id_autodebet(agenPas.get(0).get("LSBP_ID").toString());
			}
			if(agenPas.get(0).get("MSAG_TABUNGAN") != null){
				pas.setMsp_no_rekening((agenPas.get(0).get("MSAG_TABUNGAN").toString().replace(".", "")));
				pas.setMsp_no_rekening_autodebet((agenPas.get(0).get("MSAG_TABUNGAN").toString().replace(".", "")));
			}
			if(agenPas.get(0).get("LSNE_ID") != null)pas.setMsp_warga(Integer.parseInt(agenPas.get(0).get("LSNE_ID").toString()));
			if(agenPas.get(0).get("MSPE_STS_MRT") != null)pas.setMsp_status_nikah(Integer.parseInt(agenPas.get(0).get("MSPE_STS_MRT").toString()));
			if(agenPas.get(0).get("LSAG_ID") != null)pas.setLsag_id(Integer.parseInt(agenPas.get(0).get("LSAG_ID").toString()));
			if(agenPas.get(0).get("LSED_ID") != null)pas.setMsp_pendidikan(Integer.parseInt(agenPas.get(0).get("LSED_ID").toString()));
			if(agenPas.get(0).get("MKL_KERJA") != null)pas.setMsp_occupation(agenPas.get(0).get("MKL_KERJA").toString());
			//Pas agenPas = new Pas();
			//agenPas = uwManager.selectAgenPas(msag_id_pp_cari);
			}
			}else{
				request.setAttribute("cari_agen_status", "Kode Agen tidak ditemukan");
			}
//		}
		}
		
		//SRI RAHAYU : Berikut adalah kode BP untuk penutup PAS Hikmah atas nama Heni Mujiati (702219)
		pas.setMsag_id("702219");
		
		pas.setLscb_id(3);
		pas.setLsre_id(1);
	//	pas.setMsp_flag_cc(2);
		//pas.setPribadi(1);
		// pas.setProduk(5);
			pas.setJenis_bp(0);			
			
			
			pas.setProduk(5);
			pas.setPremi("74000");	
			pas.setMsp_premi("74000");		
		
		
		List sumberPendanaanList = DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","ID",request);
		List bidangIndustriList = DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","ID",request);
		List pekerjaanList = DroplistManager.getInstance().get("PEKERJAAN_PAS.xml","ID",request);
		
		for(int i = 0 ; i < sumberPendanaanList.size() ; i++){
			Map map = (Map) sumberPendanaanList.get(i);
			String id = (String) map.get("ID");
			if(id.equals(pas.getMsp_fire_source_fund())){
				pas.setMsp_fire_source_fund2(id);
				break;
			}else{
				pas.setMsp_fire_source_fund2("LAINNYA");
			}
		}
		
		for(int i = 0 ; i < bidangIndustriList.size() ; i++){
			Map map = (Map) bidangIndustriList.get(i);
			String id = (String) map.get("ID");
			if(id.equals(pas.getMsp_fire_type_business())){
				pas.setMsp_fire_type_business2(id);
				break;
			}else{
				pas.setMsp_fire_type_business2("LAINNYA");
			}
		}
		
		for(int i = 0 ; i < pekerjaanList.size() ; i++){
			Map map = (Map) pekerjaanList.get(i);
			String id = (String) map.get("ID");
			if(id.equals(pas.getMsp_fire_occupation())){
				pas.setMsp_fire_occupation2(id);
				break;
			}else{
				pas.setMsp_fire_occupation2("LAIN-LAIN");
			}
		}
		
		CheckSum checkSum = new CheckSum();
		
		if(pas.getPin() != null){
			String lsdbs_number = uwManager.selectCekPin(pas.getPin(), 1);
			//if(lsdbs_number == null)lsdbs_number = "x";
			pas.setProduk(Integer.parseInt(lsdbs_number));
		}
		
		
		String bank_pp1=pas.getLsbp_id();
		String nama_bank_rekclient="";
		if(bank_pp1!=null){
			Map data1= (HashMap) this.elionsManager.select_bank1(bank_pp1);
			if (data1!=null){		
				nama_bank_rekclient = (String)data1.get("BANK_NAMA");
				pas.setLsbp_nama(nama_bank_rekclient);
			}
		}
		
		String bank_pp2=pas.getLsbp_id_autodebet();
		String nama_bank_rekclient_autodebet="";
		if(bank_pp2!=null){
			Map data1= (HashMap) this.elionsManager.select_bank2(bank_pp2);
			if (data1!=null){		
				nama_bank_rekclient_autodebet = (String)data1.get("BANK_NAMA");
				pas.setLsbp_nama_autodebet(nama_bank_rekclient_autodebet);
			}
		}
		
//		if(pas.getMsp_pas_beg_date() == null){
//			Date end_date = elionsManager.selectSysdate();
//			end_date.setYear(end_date.getYear()+1);
//			end_date.setDate(end_date.getDate()-1);
//			
//			pas.setMsp_pas_beg_date(elionsManager.selectSysdate());
//			pas.setMsp_pas_end_date(end_date);
//		}
		
//		if(pas.getMsp_fire_beg_date() == null){
//			Date end_date = elionsManager.selectSysdate();
//			end_date.setYear(end_date.getYear()+1);
//			end_date.setDate(end_date.getDate()-1);
//			
//			pas.setMsp_fire_beg_date(elionsManager.selectSysdate());
//			pas.setMsp_fire_end_date(end_date);
//			
//		}
		
		return pas;
	}

}