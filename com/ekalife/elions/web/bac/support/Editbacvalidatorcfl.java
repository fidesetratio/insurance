package com.ekalife.elions.web.bac.support;

import id.co.sinarmaslife.std.spring.util.Email;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import produk_asuransi.n_prod;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Hcp;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.Simas;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.Products;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.f_validasi;

/**
 * @author HEMILDA
 * validator editbacController
 * untuk validasi penginputan bac
 */

public class Editbacvalidatorcfl implements Validator{

	protected final Log logger = LogFactory.getLog(getClass());
	
	private ElionsManager elionsManager;
	private UwManager uwManager;
	private DateFormat defaultDateFormat;
	private Email email;
	private Products products;
	
	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	public void setEmail(Email email) {
		this.email = email;
	}
	
	public void setDefaultDateFormat(DateFormat defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
	}

	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}
	
	public void setUwManager(UwManager uwManager) {
		this.uwManager = uwManager;
	}

	public boolean supports(Class data) {
		return Cmdeditbac.class.isAssignableFrom(data);
	}

	public void validate(Object cmd, Errors err) {
	}
	
	
	//validasi tambahan untuk otorisasi
	public void validateOtorisasi(Cmdeditbac detiledit, Errors err, int jenis) {
		
		String spv = detiledit.getPemegang().getSpv();
		String valid1=elionsManager.selectUserName(detiledit.getCurrentUser().getValid_bank_1());
		String valid2=elionsManager.selectUserName(detiledit.getCurrentUser().getValid_bank_2());
		String pass1 = detiledit.getPemegang().getPass1();
		String pass2 = detiledit.getPemegang().getPass2();
		String password1 = detiledit.getPemegang().getPassword1();
		String password2 = detiledit.getPemegang().getPassword2();
		double premi = detiledit.getDatausulan().getMspr_premium();
		String kurs = detiledit.getDatausulan().getKurs_p();
		String spaj = detiledit.getPemegang().getReg_spaj();

		if (pass1 == null) pass1 = "";
		if (pass2 == null) pass2 = "";
		if (spaj == null) spaj ="";
		if (spv == null) spv ="";
		
		//2 = BANK SINARMAS
		if(jenis == 2) {
			//limit bank sinarmas
			double limit = 2000000000;
			if (kurs.equalsIgnoreCase("02")) limit = 200000;

			//cukup validasi SPV saja bila dibawah limit
			if (premi < limit){
//				ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.pass1","","Silahkan mengisi terlebih dahulu password SPV/PINCAB.");
//				if (!(pass1.equalsIgnoreCase(password1) || pass1.equalsIgnoreCase(password2)) && !err.hasErrors()){
//					err.rejectValue("pemegang.pass1","","Password SPV/PINCAB yang anda masukkan salah.");
//				}
//				if (!(spv.equalsIgnoreCase(valid1)||spv.equalsIgnoreCase(valid2)) && !err.hasErrors()){
//					err.rejectValue("pemegang.spv","","Nama SPV/PINCAB yang anda masukkan salah.");
//				}
				
			//validasi SPV dan kacab dua2nya bila diatas limit
			// ditutup atas kebijakan dari sinarmas bahwah hanya spv yang masukian pwdnya
			//email dari yusuf 27/01/2009
			}else if (premi >= limit){
//				ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.pass1","","Silahkan mengisi terlebih dahulu password SPV/PINCAB.");
//				ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.pass1","","Silahkan mengisi terlebih dahulu password Kepala Cabang.");
//				if (!(pass1.equalsIgnoreCase(password1) || pass1.equalsIgnoreCase(password2)|| !spv.equalsIgnoreCase(valid2)) && !err.hasErrors()){
//					err.rejectValue("pemegang.pass1","","Password SPV/PINCAB yang anda masukkan salah.");
//				}
//				if (!pass2.equalsIgnoreCase(password2) && !err.hasErrors()){
//					err.rejectValue("pemegang.pass2","","Password Kepala Cabang yang anda masukkan salah.");
//				}
			}
		
		//3 = SINARMAS SEKURITAS
		}else if(jenis == 3) {
			/*
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.otorotor","","Silahkan pilih USER OTORISASI untuk melanjutkan.");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.pass3","","Silahkan masukkan PASSWORD OTORISASI untuk melanjutkan.");
			
			if(!err.hasErrors()) {
				int lus_id = Integer.parseInt(detiledit.getPemegang().getOtorotor());
				String pass = elionsManager.validationOtorisasiSekuritas(lus_id);
				if(!detiledit.getPemegang().getPass3().trim().equalsIgnoreCase(pass)) {
					err.rejectValue("pemegang.pass3","","Password Otorisasi yang anda masukkan salah.");
				}
			}
			*/
			//VALIDASI DI-DISABLE, JADINYA SAAT TRANSFER
		}
	
	}
	
	//--------------------------pemegang------------------------------------------
	/**
	 * @author HEMILDA
	 * validasi halaman pemegang polis
	 */
	public void validatepp(Object cmd, Errors err)  throws ServletException,IOException{
		Cmdeditbac edit= (Cmdeditbac) cmd;
		logger.debug("EditBacValidator : validate page validatepp");
		form_pemegang_polis pp =new form_pemegang_polis();
		
		//
		edit.getDatausulan().setKeterangan_fund("");
		if (edit.getDatausulan().getFlag_worksite_ekalife() == null){
			edit.getDatausulan().setFlag_worksite_ekalife(0);
		}
	
		String spaj = edit.getPemegang().getReg_spaj();
		if (spaj == null){
			spaj ="";
		}

		if (spaj.equalsIgnoreCase("")){
			edit.getPemegang().setMspo_input_date(new Date());
		}
		
		if(edit.getPemegang().getMspo_input_date()==null){
			edit.getPemegang().setMspo_input_date(new Date());
		}
		
		String sumber_id=edit.getPemegang().getSumber_id();
		String nama_sumber="";
		if(sumber_id!=null)
		{
			Map data1= (HashMap) this.elionsManager.select_sumberBisnis(sumber_id);

			if (data1!=null)
			{		
				nama_sumber = (String)data1.get("NAMA_SUMBER");
			}
		}else{
			sumber_id="";
		}
		edit.getPemegang().setNama_sumber(nama_sumber);
		
		Integer jumlah_cancel = 0;
		/**
		 * @author HEMILDA
		 * cek endors atau bukan
		 * kalau row dari mst cancel maka endors
		 */
		Integer status_gutri = 0;
		Integer flag_gutri = 0;
		Integer mste_flag_guthrie = edit.getPemegang().getMste_flag_guthrie();
		if (mste_flag_guthrie == null){
			mste_flag_guthrie = 0;
		}
		
		// hanya dijalankan kalau edit
		if (!spaj.equalsIgnoreCase("")){
			
//			Integer mu_lspd_id = elionsManager.selectPosisiUlink(spaj);
//			if(mu_lspd_id != null){
//				if(mu_lspd_id.intValue() >= 42){
//					err.reject("", "Polis ini sudah di-FUND, tidak boleh melakukan perubahan data. Terima kasih.");
//				}
//			}
			
			jumlah_cancel = (Integer)this.elionsManager.count_mst_cancel(spaj);
			status_gutri = (Integer)this.elionsManager.selectstsgutri(spaj);
			
			Integer status_polis = this.elionsManager.selectPositionSpaj(spaj);
			if (status_polis == null){
				status_polis = 1;
				this.elionsManager.savelssaid(edit.getPemegang().getReg_spaj(), status_polis);
			}
			Integer posisi_dok = 0;
			Map dataa = (HashMap) this.elionsManager.selectPositiondok(spaj);
			if (dataa!=null){		
				posisi_dok = new Integer(Integer.parseInt(dataa.get("LSPD_ID").toString()));
			}
			edit.getHistory().setStatus_polis(status_polis);
			edit.getDatausulan().setLspd_id(posisi_dok);
			
			if (status_gutri==null){
				status_gutri = 0;
			}
		}else{
			jumlah_cancel = 0;
		}
		edit.getPemegang().setJumlah_cancel(jumlah_cancel);
		
		if (status_gutri.intValue() == 59 || status_gutri.intValue() == 60){
			flag_gutri = 1;
			if (mste_flag_guthrie.intValue() == 0){
				mste_flag_guthrie = flag_gutri;
			}
		}
		edit.getDatausulan().setFlag_gutri(flag_gutri);
		edit.getPemegang().setMste_flag_guthrie(mste_flag_guthrie);
		
		Map data_valid = (HashMap) this.elionsManager.select_validbank(edit.getPemegang().getLus_id());
		if (data_valid != null)	{
			edit.getPemegang().setCab_bank((String)data_valid.get("CAB_BANK"));
			edit.getPemegang().setJn_bank((Integer) data_valid.get("JN_BANK"));
			edit.getPemegang().setPassword1((String)data_valid.get("PASS1"));
			edit.getPemegang().setPassword2((String)data_valid.get("PASS2"));
		}else{
			edit.getPemegang().setCab_bank("");
			edit.getPemegang().setJn_bank(-1);
			edit.getPemegang().setPassword1("");
			edit.getPemegang().setPassword2("");
		}

		Integer hubungan = edit.getPemegang().getLsre_id();
		String cab_bank = edit.getPemegang().getCab_bank();
		if (cab_bank == null){
			cab_bank="";
		}
		
		Integer flag_worksite = edit.getDatausulan().getFlag_worksite();
		
		//Apabila agama lain-lain wajib isi
		if(edit.getPemegang().getLsag_id() == null)edit.getPemegang().setLsag_id(1);
		if(edit.getPemegang().getLsag_id()==6){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mcl_agama","","PEMEGANG: Silahkan sebutkan agama lainnya.");
		}
		
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			if(edit.getContactPerson().getLsag_id() == null)edit.getContactPerson().setLsag_id(1);
			if(edit.getContactPerson().getLsag_id()==6){
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "contactPerson.agama","","PIC: Silahkan sebutkan agama lainnya.");
			}
		}

		//nama pp
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			// nama perusahaan
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mcl_first", "", "PEMEGANG: Silahkan masukkan nama Badan Hukum / Usaha");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "contactPerson.nama_lengkap", "", "PIC: Silahkan masukkan nama lengkap");
		}else{
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mcl_first","","PEMEGANG: Silahkan masukkan nama Pemegang Polis");
		}
		
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "personal.mpt_siup", "", "PEMEGANG: Silahkan masukkan No. SIUP");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "personal.mpt_npwp", "", "PEMEGANG: Silahkan masukkan No. NPWP");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "personal.tgl_siup", "", "PEMEGANG: Harap Lengkapi Tanggal Terdaftar SIUP.");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "personal.tgl_npwp", "", "PEMEGANG: Harap Lengkapi Tanggal Terdaftar NPWP.");
		}
		
		edit.getPersonal().setMcl_first(f_validasi.convert_karakter(edit.getPersonal().getMcl_first()));
		edit.getContactPerson().setNama_lengkap(f_validasi.convert_karakter(edit.getContactPerson().getNama_lengkap()));
		edit.getPemegang().setMcl_first(f_validasi.convert_karakter(edit.getPemegang().getMcl_first()));
		
		//gelar_pp
		edit.getPemegang().setMcl_gelar(f_validasi.convert_karakter(edit.getPemegang().getMcl_gelar()));
		
		//gelar_pic
		edit.getContactPerson().setMcl_gelar(f_validasi.convert_karakter(edit.getContactPerson().getMcl_gelar()));

		// ibu_kandung_pp
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "contactPerson.mspe_mother", "", "PIC: Harap Lengkapi Nama Ibu Kandung");
		}else{
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mspe_mother", "", "PEMEGANG: Harap Lengkapi Nama Ibu Kandung");
		}
		edit.getContactPerson().setMspe_mother(f_validasi.convert_karakter(edit.getContactPerson().getMspe_mother()));
		edit.getPemegang().setMspe_mother(f_validasi.convert_karakter(edit.getPemegang().getMspe_mother()));
		
		//nomor_id_pp
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "contactPerson.no_identity", "", "PIC: Harap Lengkapi Nomor Identitas");
		}else{
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mspe_no_identity", "", "PEMEGANG: Harap Lengkapi Nomor Identitas");
		}
		edit.getContactPerson().setNo_identity(f_validasi.convert_karakter(edit.getContactPerson().getNo_identity()));
		edit.getPemegang().setMspe_no_identity(f_validasi.convert_karakter(edit.getPemegang().getMspe_no_identity()));
		
		//tanggal lahir pp
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mspe_date_birth","","PEMEGANG: Harap Lengkapi Tanggal Lahir Pemegang Polis.");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "contactPerson.date_birth","","PIC: Harap Lengkapi Tanggal Lahir PIC.");
		}else{
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mspe_date_birth","","PEMEGANG: Harap Lengkapi Tanggal Lahir Pemegang Polis.");
		}
		
		//tempat_lahir_pp
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mspe_place_birth", "", "PEMEGANG: Harap Lengkapi Tempat Lahir Pemegang Polis");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "contactPerson.place_birth", "", "PIC: Harap Lengkapi Tempat Lahir PIC");
		}else{
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mspe_place_birth", "", "PEMEGANG: Harap Lengkapi Tempat Lahir Pemegang Polis");
		}
		edit.getContactPerson().setPlace_birth(f_validasi.convert_karakter(edit.getContactPerson().getPlace_birth()));
		edit.getPemegang().setMspe_place_birth(f_validasi.convert_karakter(edit.getPemegang().getMspe_place_birth()));
		
		
		edit.getPersonal().setMcl_first(f_validasi.convert_karakter(edit.getPersonal().getMcl_first()));
		
		
		//validasi no hp aktif tgl 10 agustus 2009-dian
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "contactPerson.no_hp", "", "PIC: Harap Lengkapi No Handphone");
		}else{
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.no_hp", "", "PEMEGANG: Harap Lengkapi No Handphone");
		}
		edit.getContactPerson().setNo_hp(f_validasi.convert_karakter(edit.getContactPerson().getNo_hp()));
		edit.getPemegang().setNo_hp(f_validasi.convert_karakter(edit.getPemegang().getNo_hp()));

		//suami istri
		String nama_suamiistri = edit.getPemegang().getNama_si();
		if (nama_suamiistri==null){
			nama_suamiistri = "";
			edit.getPemegang().setNama_si(nama_suamiistri);
		}

		if (nama_suamiistri.equalsIgnoreCase("")){
			edit.getPemegang().setTgllhr_si(null);
		}else{
			if(Common.isEmpty(edit.getPemegang().getTgllhr_si())){
//				err.rejectValue("pemegang.tgllhr_si","","PEMEGANG: Silakan Masukkan tanggal lahir Suami/Istri terlebih dahulu");
			}
		}
		
		
		//anak 1
		String nama_anak1 = edit.getPemegang().getNama_anak1();
		if (nama_anak1 == null){
			nama_anak1 = "";
			edit.getPemegang().setNama_anak1(nama_anak1);
		}
		if (nama_anak1.equalsIgnoreCase("")){
			edit.getPemegang().setTgllhr_anak1(null);
		}
		
		//anak 2
		String nama_anak2 = edit.getPemegang().getNama_anak2();
		if (nama_anak2 == null){
			nama_anak2 = "";
			edit.getPemegang().setNama_anak2(nama_anak2);
		}
		if (nama_anak2.equalsIgnoreCase("")){
			edit.getPemegang().setTgllhr_anak2(null);
		}
		
		//anak 3
		String nama_anak3 = edit.getPemegang().getNama_anak3();
		if (nama_anak3 == null){
			nama_anak3 = "";
			edit.getPemegang().setNama_anak3(nama_anak3);
		}
		if (nama_anak3.equalsIgnoreCase("")){
			edit.getPemegang().setTgllhr_anak3(null);
		}
		
		//alamat_rumah_pp
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.alamat_rumah", "", "PEMEGANG: Harap Lengkapi Alamat Badan Hukum / Usaha");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "contactPerson.alamat_rumah", "", "PIC: Harap Lengkapi Alamat Rumah PIC");
		}else{
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.alamat_rumah", "", "PEMEGANG: Harap Lengkapi Alamat Rumah Pemegang Polis");
		}
		edit.getContactPerson().setAlamat_rumah(f_validasi.convert_karakter(edit.getContactPerson().getAlamat_rumah()));
		edit.getPemegang().setAlamat_rumah(f_validasi.convert_karakter(edit.getPemegang().getAlamat_rumah()));
			
		if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0) {
			//kota_rumah_pp
			if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.kota_rumah", "", "PEMEGANG: Harap Lengkapi Kota Badan Hukum / Usaha");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "contactPerson.kota_rumah", "", "PIC: Harap Lengkapi Kota Rumah PIC");
				//ValidationUtils.rejectIfEmptyOrWhitespace(err, "contactPerson.kd_pos_rumah", "", "PIC: Harap Lengkapi Kode Pos Rumah PIC");
			}else{
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.kota_rumah", "", "PEMEGANG: Harap Lengkapi Kota Rumah Pemegang Polis");
			}
			edit.getContactPerson().setKota_rumah(f_validasi.convert_karakter(edit.getContactPerson().getKota_rumah()));
			edit.getPemegang().setKota_rumah(f_validasi.convert_karakter(edit.getPemegang().getKota_rumah()));
			//kode_pos_rumah_pp	
			//ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.kd_pos_rumah", "", "PEMEGANG: Harap Lengkapi Kode Pos Rumah Pemegang Polis");
			edit.getContactPerson().setKd_pos_rumah(f_validasi.convert_karakter(edit.getContactPerson().getKd_pos_rumah()));
			edit.getPemegang().setKd_pos_rumah(f_validasi.convert_karakter(edit.getPemegang().getKd_pos_rumah()));
		}
		
		// BARU REVISI SAMPAI SINI
//		edit.getPemegang().getArea_code_rumah()
//		edit.getPemegang().getTelpon_rumah()
//		edit.getPemegang().getArea_code_rumah2()
//		edit.getPemegang().getTelpon_rumah2()
//		edit.getPemegang().getNo_hp()
//		edit.getPemegang().getNo_hp2()
		
//		if(!Common.isEmpty(edit.getPemegang().getTelpon_rumah())){
//			 if(!f_validasi.f_validasi_numerik(edit.getPemegang().getTelpon_rumah())) {
//					err.rejectValue("pemegang.telpon_rumah","","PEMEGANG: ");
//			 }
//		}
		
		//kode_area_rumah_pp
		String kd_area_rmh_pp=edit.getPemegang().getArea_code_rumah();
		if (kd_area_rmh_pp==null)
		{
			kd_area_rmh_pp="";
		}
		String hasil_kd_area_rmh_pp = pp.kode_area_rmh_pp(kd_area_rmh_pp);
		if (hasil_kd_area_rmh_pp.trim().length()!=0)
		{
			edit.getPemegang().setArea_code_rumah(f_validasi.convert_karakter(kd_area_rmh_pp));
			//if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			//{
				if (kd_area_rmh_pp.trim().length()==0)
				{
					kd_area_rmh_pp="-";
					hasil_kd_area_rmh_pp = "";
				}
				edit.getPemegang().setArea_code_rumah(f_validasi.convert_karakter(kd_area_rmh_pp));
				
				if (!hasil_kd_area_rmh_pp.equalsIgnoreCase(""))
				{
					err.rejectValue("pemegang.area_code_rumah","","PEMEGANG:" +hasil_kd_area_rmh_pp);
				}
			/*}else{
				if (kd_area_rmh_pp.trim().length()==0)
				{
					kd_area_rmh_pp="-";
					hasil_kd_area_rmh_pp = "";
				}
				edit.getPemegang().setArea_code_rumah(f_validasi.convert_karakter(kd_area_rmh_pp));

			}*/
		}
		edit.getPemegang().setArea_code_rumah(f_validasi.convert_karakter(kd_area_rmh_pp));
		
		String kd_area_rmh_pp2=edit.getPemegang().getArea_code_rumah2();
		if (kd_area_rmh_pp2==null)
		{
			kd_area_rmh_pp2="";
		}
		String hasil_kd_area_rmh_pp2 = pp.kode_area_rmh_pp2(kd_area_rmh_pp2);
		if (hasil_kd_area_rmh_pp2.trim().length()!=0)
		{
			edit.getPemegang().setArea_code_rumah2(f_validasi.convert_karakter(kd_area_rmh_pp2));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("pemegang.area_code_rumah2","","PEMEGANG:" +hasil_kd_area_rmh_pp2);
			}
		}
		edit.getPemegang().setArea_code_rumah2(f_validasi.convert_karakter(kd_area_rmh_pp2));	
		
		//	kode_area_rumah_pic
		String kd_area_rmh_pic="";
		String kd_area_rmh_pic2="";
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			kd_area_rmh_pic=edit.getContactPerson().getArea_code_rumah();
			if (kd_area_rmh_pic==null)
			{
				kd_area_rmh_pic="";
			}
			String hasil_kd_area_rmh_pic = pp.kode_area_rmh_pp(kd_area_rmh_pic);
			if (hasil_kd_area_rmh_pic.trim().length()!=0)
			{
				edit.getContactPerson().setArea_code_rumah(f_validasi.convert_karakter(kd_area_rmh_pic));
				//if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				//{
					if (kd_area_rmh_pic.trim().length()==0)
					{
						kd_area_rmh_pic="-";
						hasil_kd_area_rmh_pic = "";
					}
					edit.getContactPerson().setArea_code_rumah(f_validasi.convert_karakter(kd_area_rmh_pic));
					
					if (!hasil_kd_area_rmh_pic.equalsIgnoreCase(""))
					{
						err.rejectValue("contactPerson.area_code_rumah","","PIC:" +hasil_kd_area_rmh_pic);
					}
				/*}else{
					if (kd_area_rmh_pic.trim().length()==0)
					{
						kd_area_rmh_pic="-";
						hasil_kd_area_rmh_pic = "";
					}
					edit.getContactPerson().setArea_code_rumah(f_validasi.convert_karakter(kd_area_rmh_pic));
	
				}*/
			}
			edit.getContactPerson().setArea_code_rumah(f_validasi.convert_karakter(kd_area_rmh_pic));
			
			kd_area_rmh_pic2=edit.getContactPerson().getArea_code_rumah2();
			if (kd_area_rmh_pic2==null)
			{
				kd_area_rmh_pic2="";
			}
			String hasil_kd_area_rmh_pic2 = pp.kode_area_rmh_pp2(kd_area_rmh_pic2);
			if (hasil_kd_area_rmh_pic2.trim().length()!=0)
			{
				edit.getContactPerson().setArea_code_rumah2(f_validasi.convert_karakter(kd_area_rmh_pic2));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("contactPerson.area_code_rumah2","","PIC:" +hasil_kd_area_rmh_pic2);
				}
			}
			edit.getContactPerson().setArea_code_rumah2(f_validasi.convert_karakter(kd_area_rmh_pic2));	
		}
			
		//telpon_rumah_pp
		String telp_rmh_pp0=edit.getPemegang().getTelpon_rumah();
		if (telp_rmh_pp0==null)
		{
			telp_rmh_pp0="";
		}
		String hasil_telp_rmh_pp0 = pp.telpon_rmh_pp(telp_rmh_pp0);
		if (hasil_telp_rmh_pp0.trim().length()!=0)
		{
			edit.getPemegang().setTelpon_rumah(f_validasi.convert_karakter(telp_rmh_pp0));
			if (telp_rmh_pp0.trim().length()==0)
			{
				telp_rmh_pp0="-";
				hasil_telp_rmh_pp0 ="";
			}
			edit.getPemegang().setTelpon_rumah(f_validasi.convert_karakter(telp_rmh_pp0));

			if (!hasil_telp_rmh_pp0.equalsIgnoreCase(""))
			{
				err.rejectValue("pemegang.telpon_rumah","","PEMEGANG:" +hasil_telp_rmh_pp0);
			}
		}

		edit.getPemegang().setTelpon_rumah(f_validasi.convert_karakter(telp_rmh_pp0));
		
		String telp_rmh_pp02=edit.getPemegang().getTelpon_rumah2();
		if (telp_rmh_pp02==null)
		{
			telp_rmh_pp02="";
		}
		String hasil_telp_rmh_pp02 = pp.telpon_rmh_pp2(telp_rmh_pp02);
		if (hasil_telp_rmh_pp02.trim().length()!=0)
		{
			edit.getPemegang().setTelpon_rumah2(f_validasi.convert_karakter(telp_rmh_pp02));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("pemegang.telpon_rumah2","","PEMEGANG:" +hasil_telp_rmh_pp02);
			}
		}
		edit.getPemegang().setTelpon_rumah2(f_validasi.convert_karakter(telp_rmh_pp02));	
		
		//	telpon_rumah_pic
		String telp_rmh_pic0="";
		String telp_rmh_pic02="";
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			telp_rmh_pic0=edit.getContactPerson().getTelpon_rumah();
			if (telp_rmh_pic0==null)
			{
				telp_rmh_pic0="";
			}
			String hasil_telp_rmh_pic0 = pp.telpon_rmh_pp(telp_rmh_pic0);
			if (hasil_telp_rmh_pic0.trim().length()!=0)
			{
				edit.getContactPerson().setTelpon_rumah(f_validasi.convert_karakter(telp_rmh_pic0));
				if (telp_rmh_pic0.trim().length()==0)
				{
					telp_rmh_pic0="-";
					hasil_telp_rmh_pic0 ="";
				}
				edit.getContactPerson().setTelpon_rumah(f_validasi.convert_karakter(telp_rmh_pic0));

				if (!hasil_telp_rmh_pic0.equalsIgnoreCase(""))
				{
					err.rejectValue("contactPerson.telpon_rumah","","PIC:" +hasil_telp_rmh_pic0);
				}
			}

			edit.getContactPerson().setTelpon_rumah(f_validasi.convert_karakter(telp_rmh_pic0));
			
			telp_rmh_pic02=edit.getContactPerson().getTelpon_rumah2();
			if (telp_rmh_pic02==null)
			{
				telp_rmh_pic02="";
			}
			String hasil_telp_rmh_pic02 = pp.telpon_rmh_pp2(telp_rmh_pic02);
			if (hasil_telp_rmh_pic02.trim().length()!=0)
			{
				edit.getContactPerson().setTelpon_rumah2(f_validasi.convert_karakter(telp_rmh_pic02));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("contactPerson.telpon_rumah2","","PIC:" +hasil_telp_rmh_pic02);
				}
			}
			edit.getContactPerson().setTelpon_rumah2(f_validasi.convert_karakter(telp_rmh_pic02));
		}
		
		//hp pp 
		String hp_pp=edit.getPemegang().getNo_hp();
		if (hp_pp==null)
		{
			hp_pp="";
		}
		if (telp_rmh_pp0.equalsIgnoreCase("-") && hp_pp.equalsIgnoreCase(""))
		{
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				if(edit.getDatausulan().getJenis_pemegang_polis() == 0){
					err.rejectValue("pemegang.no_hp","","PEMEGANG: Jika telpon rumah tidak ada, harap isi handphone.");
				}
			}
		}
		String hasil_hp_pp = pp.handphone_pp1(hp_pp);
		//logger.info(hasil_hp_pp);
		if (hasil_hp_pp.trim().length()!=0)
		{
			edit.getPemegang().setNo_hp(f_validasi.convert_karakter(hp_pp));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("pemegang.no_hp","","PEMEGANG:" +hasil_hp_pp);
			}
		}
		edit.getPemegang().setNo_hp(f_validasi.convert_karakter(hp_pp));
		
		String hp_pp2=edit.getPemegang().getNo_hp2();
		if (hp_pp2==null)
		{
			hp_pp2="";
		}
		String hasil_hp_pp2 = pp.handphone_pp2(hp_pp2);
		if (hasil_hp_pp2.trim().length()!=0)
		{
			edit.getPemegang().setNo_hp2(f_validasi.convert_karakter(hp_pp2));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("pemegang.no_hp2","","PEMEGANG:" +hasil_hp_pp2);
			}
		}
		edit.getPemegang().setNo_hp2(f_validasi.convert_karakter(hp_pp2));
		
		//hp pic
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			String hp_pic=edit.getContactPerson().getNo_hp();
			if (hp_pic==null)
			{
				hp_pic="";
			}
			if (telp_rmh_pic0.equalsIgnoreCase("-") && hp_pic.equalsIgnoreCase(""))
			{
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("contactPerson.no_hp","","PIC: Jika telpon rumah tidak ada, harap isi handphone.");
				}
			}
			String hasil_hp_pic = pp.handphone_pp1(hp_pic);
			if("Silahkan masukkan No Handphone-1 pemegang polis dalam bentuk numerik".equals(hasil_hp_pic)){
				hasil_hp_pic = "Silahkan masukkan No Handphone-1 PIC dalam bentuk numerik";
			}
			//logger.info(hasil_hp_pic);
			if (hasil_hp_pic.trim().length()!=0)
			{
				edit.getContactPerson().setNo_hp(f_validasi.convert_karakter(hp_pic));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("contactPerson.no_hp","","PIC:" +hasil_hp_pic);
				}
			}
			edit.getContactPerson().setNo_hp(f_validasi.convert_karakter(hp_pic));
			
			String hp_pic2=edit.getContactPerson().getNo_hp2();
			if (hp_pic2==null)
			{
				hp_pic2="";
			}
			String hasil_hp_pic2 = pp.handphone_pp2(hp_pic2);
			if("Silahkan masukkan No Handphone-2 pemegang polis dalam bentuk numerik".equals(hasil_hp_pic)){
				hasil_hp_pic = "Silahkan masukkan No Handphone-2 PIC dalam bentuk numerik";
			}
			if (hasil_hp_pic2.trim().length()!=0)
			{
				edit.getContactPerson().setNo_hp2(f_validasi.convert_karakter(hp_pic2));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("contactPerson.no_hp2","","PIC:" +hasil_hp_pic2);
				}
			}
			edit.getContactPerson().setNo_hp2(f_validasi.convert_karakter(hp_pic2));
		}
		
		
		//alamat_kantor_pp		
		String almt_knt_pp=edit.getPemegang().getAlamat_kantor();
		if (almt_knt_pp==null)
		{
			almt_knt_pp="";
		}
		if (almt_knt_pp.trim().length()!=0)
		{
			edit.getPemegang().setAlamat_kantor(f_validasi.convert_karakter(almt_knt_pp));
		}
		
		//	alamat_kantor_pic	
		String almt_knt_pic = "";
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			almt_knt_pic=edit.getContactPerson().getAlamat_kantor();
			if (almt_knt_pic==null)
			{
				almt_knt_pic="";
			}
			if (almt_knt_pic.trim().length()!=0)
			{
				edit.getContactPerson().setAlamat_kantor(f_validasi.convert_karakter(almt_knt_pic));
			}
		}
		
		//kota_kantor_pp		
		String kota_knt_pp=edit.getPemegang().getKota_kantor();
		String hasil_kota_knt_pp="";
		if (kota_knt_pp==null)
		{
			kota_knt_pp = "";
			edit.getPemegang().setKota_kantor("");
			kota_knt_pp=edit.getPemegang().getKota_kantor();
		}
		
		if (almt_knt_pp.trim().length()!=0)
		{
			hasil_kota_knt_pp=pp.kota_kantor_pp(kota_knt_pp);
			if(hasil_kota_knt_pp.trim().length()!=0)
			{
				edit.getPemegang().setKota_kantor(f_validasi.convert_karakter(kota_knt_pp));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("pemegang.kota_kantor","","PEMEGANG:" +hasil_kota_knt_pp);
				}
			}
		}
		edit.getPemegang().setKota_kantor(f_validasi.convert_karakter(kota_knt_pp));
		
		//kota_kantor_pic	
		String kota_knt_pic = "";
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			kota_knt_pic=edit.getContactPerson().getKota_kantor();
			String hasil_kota_knt_pic="";
			if (kota_knt_pic==null)
			{
				kota_knt_pic = "";
				edit.getContactPerson().setKota_kantor("");
				kota_knt_pic=edit.getContactPerson().getKota_kantor();
			}
			
			if (almt_knt_pic.trim().length()!=0)
			{
				hasil_kota_knt_pic=pp.kota_kantor_pp(kota_knt_pic);
				if(hasil_kota_knt_pic.trim().length()!=0)
				{
					edit.getContactPerson().setKota_kantor(f_validasi.convert_karakter(kota_knt_pic));
					if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
					{
						err.rejectValue("contactPerson.kota_kantor","","PIC:" +hasil_kota_knt_pic);
					}
				}
			}
			edit.getContactPerson().setKota_kantor(f_validasi.convert_karakter(kota_knt_pic));
		}

		//kode_pos_kantor_pp		
		String kd_pos_knt_pp=edit.getPemegang().getKd_pos_kantor();
		if (kd_pos_knt_pp==null)
		{
			kd_pos_knt_pp="";
		}
		String hasil_kd_pos_knt_pp = pp.kode_pos_ktr_pp(kd_pos_knt_pp);
		if (hasil_kd_pos_knt_pp.trim().length()!=0)
		{
			edit.getPemegang().setKd_pos_kantor(f_validasi.convert_karakter(kd_pos_knt_pp));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("pemegang.kd_pos_kantor","","PEMEGANG:" +hasil_kd_pos_knt_pp);
			}
		}
		edit.getPemegang().setKd_pos_kantor(f_validasi.convert_karakter(kd_pos_knt_pp));
		
		//	kota_kantor_pic	
		String kd_pos_knt_pic= "";
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			kd_pos_knt_pic=edit.getContactPerson().getKd_pos_kantor();
			if (kd_pos_knt_pic==null)
			{
				kd_pos_knt_pic="";
			}
			String hasil_kd_pos_knt_pic = pp.kode_pos_ktr_pp(kd_pos_knt_pic);
			if (hasil_kd_pos_knt_pic.trim().length()!=0)
			{
				edit.getContactPerson().setKd_pos_kantor(f_validasi.convert_karakter(kd_pos_knt_pic));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("contactPerson.kd_pos_kantor","","PIC:" +hasil_kd_pos_knt_pic);
				}
			}
			edit.getContactPerson().setKd_pos_kantor(f_validasi.convert_karakter(kd_pos_knt_pic));
		}
			
		//area_kantor_pp
		String kd_area_knt_pp=edit.getPemegang().getArea_code_kantor();
		if (kd_area_knt_pp==null)
		{
			kd_area_knt_pp="";
		}
		String hasil_kd_area_knt_pp = pp.kode_area_ktr_pp(kd_area_knt_pp);
		if (hasil_kd_area_knt_pp.trim().length()!=0)
		{
			edit.getPemegang().setArea_code_kantor(f_validasi.convert_karakter(kd_area_knt_pp));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				if(edit.getDatausulan().getJenis_pemegang_polis() == 0){
					err.rejectValue("pemegang.area_code_kantor","","PEMEGANG:" +hasil_kd_area_knt_pp);
				}
			}
		}
		edit.getPemegang().setArea_code_kantor(f_validasi.convert_karakter(kd_area_knt_pp));
		
		String kd_area_knt_pp2=edit.getPemegang().getArea_code_kantor2();
		if (kd_area_knt_pp2==null)
		{
			kd_area_knt_pp2="";
		}
		String hasil_kd_area_knt_pp2 = pp.kode_area_ktr_pp(kd_area_knt_pp2);
		if (hasil_kd_area_knt_pp2.trim().length()!=0)
		{
			edit.getPemegang().setArea_code_kantor2(f_validasi.convert_karakter(kd_area_knt_pp2));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				if(edit.getDatausulan().getJenis_pemegang_polis() == 0){
					err.rejectValue("pemegang.area_code_kantor2","","PEMEGANG:" +hasil_kd_area_knt_pp2);
				}
			}
		}
		edit.getPemegang().setArea_code_kantor2(f_validasi.convert_karakter(kd_area_knt_pp2));		
		
		
		//	area_kantor_pic	
		String kd_area_knt_pic="";
		String kd_area_knt_pic2="";
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			kd_area_knt_pic=edit.getContactPerson().getArea_code_kantor();
			if (kd_area_knt_pic==null)
			{
				kd_area_knt_pic="";
			}
			String hasil_kd_area_knt_pic = pp.kode_area_ktr_pp(kd_area_knt_pic);
			if (hasil_kd_area_knt_pic.trim().length()!=0)
			{
				edit.getContactPerson().setArea_code_kantor(f_validasi.convert_karakter(kd_area_knt_pic));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("contactPerson.area_code_kantor","","PIC:" +hasil_kd_area_knt_pic);
				}
			}
			edit.getContactPerson().setArea_code_kantor(f_validasi.convert_karakter(kd_area_knt_pic));
			
			kd_area_knt_pic2=edit.getContactPerson().getArea_code_kantor2();
			if (kd_area_knt_pic2==null)
			{
				kd_area_knt_pic2="";
			}
			String hasil_kd_area_knt_pic2 = pp.kode_area_ktr_pp(kd_area_knt_pic2);
			if (hasil_kd_area_knt_pic2.trim().length()!=0)
			{
				edit.getContactPerson().setArea_code_kantor2(f_validasi.convert_karakter(kd_area_knt_pic2));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("contactPerson.area_code_kantor2","","PIC:" +hasil_kd_area_knt_pic2);
				}
			}
			edit.getContactPerson().setArea_code_kantor2(f_validasi.convert_karakter(kd_area_knt_pic2));	
		}

		
		//telpon_kantor_pp
		String telp_knt_pp=edit.getPemegang().getTelpon_kantor();
		if (telp_knt_pp==null)
		{
			telp_knt_pp="";
		}
		String hasil_telp_knt_pp = pp.telpon_ktr_pp(telp_knt_pp);
		if (hasil_telp_knt_pp.trim().length()!=0)
		{
			edit.getPemegang().setTelpon_kantor(f_validasi.convert_karakter(telp_knt_pp));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				if(edit.getDatausulan().getJenis_pemegang_polis() == 0){
					err.rejectValue("pemegang.telpon_kantor","","PEMEGANG:" +hasil_telp_knt_pp);
				}
			}
		}
		edit.getPemegang().setTelpon_kantor(f_validasi.convert_karakter(telp_knt_pp));
		
		String telp_knt_pp2=edit.getPemegang().getTelpon_kantor2();
		if (telp_knt_pp2==null)
		{
			telp_knt_pp2="";
		}
		String hasil_telp_knt_pp2 = pp.telpon_ktr_pp(telp_knt_pp2);
		if (hasil_telp_knt_pp2.trim().length()!=0)
		{
			edit.getPemegang().setTelpon_kantor2(f_validasi.convert_karakter(telp_knt_pp2));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				if(edit.getDatausulan().getJenis_pemegang_polis() == 0){
					err.rejectValue("pemegang.telpon_kantor2","","PEMEGANG:" +hasil_telp_knt_pp2);
				}
			}
		}
		edit.getPemegang().setTelpon_kantor2(f_validasi.convert_karakter(telp_knt_pp2));	
		
		
		//	telpon_kantor_pic
		String telp_knt_pic="";
		String telp_knt_pic2="";
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			telp_knt_pic=edit.getContactPerson().getTelpon_kantor();
			if (telp_knt_pic==null)
			{
				telp_knt_pic="";
			}
			String hasil_telp_knt_pic = pp.telpon_ktr_pp(telp_knt_pic);
			if (hasil_telp_knt_pic.trim().length()!=0)
			{
				edit.getContactPerson().setTelpon_kantor(f_validasi.convert_karakter(telp_knt_pic));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("contactPerson.telpon_kantor","","PIC:" +hasil_telp_knt_pic);
				}
			}
			edit.getContactPerson().setTelpon_kantor(f_validasi.convert_karakter(telp_knt_pic));
			
			telp_knt_pic2=edit.getContactPerson().getTelpon_kantor2();
			if (telp_knt_pic2==null)
			{
				telp_knt_pic2="";
			}
			String hasil_telp_knt_pic2 = pp.telpon_ktr_pp(telp_knt_pic2);
			if (hasil_telp_knt_pic2.trim().length()!=0)
			{
				edit.getContactPerson().setTelpon_kantor2(f_validasi.convert_karakter(telp_knt_pic2));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("contactPerson.telpon_kantor2","","PIC:" +hasil_telp_knt_pic2);
				}
			}
			edit.getContactPerson().setTelpon_kantor2(f_validasi.convert_karakter(telp_knt_pic2));		
		}


		//kode area fax pp	
		String area_fax = edit.getPemegang().getArea_code_fax();
		if (area_fax==null)
		{
			area_fax="";
		}
		String hasil_area_fax = pp.kode_area_fax(area_fax);
		if (hasil_area_fax.trim().length()!=0)
		{
			edit.getPemegang().setArea_code_fax(f_validasi.convert_karakter(area_fax));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("pemegang.area_code_fax","","PEMEGANG:" +hasil_area_fax);
			}
		}		
		
		
		//	kode area fax pic	
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			String area_fax_pic = edit.getContactPerson().getArea_code_fax();
			if (area_fax_pic==null)
			{
				area_fax_pic="";
			}
			String hasil_area_fax_pic = pp.kode_area_fax(area_fax_pic);
			if (hasil_area_fax_pic.trim().length()!=0)
			{
				edit.getContactPerson().setArea_code_fax(f_validasi.convert_karakter(area_fax_pic));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("contactPerson.area_code_fax","","PIC:" +hasil_area_fax_pic);
				}
			}	
		}
		
		
		//fax penagihan	
		String fax1 = edit.getAddressbilling().getMsap_fax1();
		if (fax1==null)
		{
			fax1="";
		}
		String hasil_fax1 = pp.fax_pp(fax1);
		if (hasil_fax1.trim().length()!=0)
		{
			edit.getAddressbilling().setMsap_fax1(f_validasi.convert_karakter(fax1));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("addressbilling.msap_fax1","","PEMEGANG:" +hasil_fax1);
			}
		}
		
		//kode area fax penagihan	
		String area_fax1 = edit.getAddressbilling().getMsap_area_code_fax1();
		if (area_fax1==null)
		{
			area_fax1="";
		}
		String hasil_area_fax1 = pp.kode_area_fax(area_fax1);
		if (hasil_area_fax1.trim().length()!=0)
		{
			edit.getAddressbilling().setMsap_area_code_fax1(f_validasi.convert_karakter(area_fax1));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("addressbilling.msap_area_code_fax1","","PEMEGANG:" +hasil_area_fax1);
			}
		}		
		
		//fax pp	
		String fax = edit.getPemegang().getNo_fax();
		if (fax==null)
		{
			fax="";
		}
		String hasil_fax = pp.fax_pp(fax);
		if (hasil_fax.trim().length()!=0)
		{
			edit.getPemegang().setNo_fax(f_validasi.convert_karakter(fax));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("pemegang.no_fax","","PEMEGANG:" +hasil_fax);
			}
		}	
		
		//	fax pic	
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			String fax_pic = edit.getContactPerson().getNo_fax();
			if (fax_pic==null)
			{
				fax_pic="";
			}
			String hasil_fax_pic = pp.fax_pp(fax_pic);
			if (hasil_fax_pic.trim().length()!=0)
			{
				edit.getContactPerson().setNo_fax(f_validasi.convert_karakter(fax_pic));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("contactPerson.no_fax","","PIC:" +hasil_fax_pic);
				}
			}	
		}
		
		Integer jenis_terbit = edit.getPemegang().getMspo_jenis_terbit();
		if (jenis_terbit ==null)
		{
			jenis_terbit = new Integer(0);
		}
		
		//email pp
		String email_pp1=edit.getPemegang().getEmail();
		
		if(email_pp1==null)
		{
			email_pp1="";
		}
		String hasil_email_pp1 = pp.email_pp1(email_pp1);

		if (jenis_terbit.intValue() == 1)
		{
			if (email_pp1.equalsIgnoreCase(""))
			{
				hasil_email_pp1 = "Jenis Terbit Polis adalah Softcopy , harus isi alamat email terlebih dahulu.";
			}
		}
		
		if (hasil_email_pp1.trim().length()!=0)
		{
			edit.getPemegang().setEmail(f_validasi.convert_karakter(email_pp1));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("pemegang.email","","PEMEGANG:" +hasil_email_pp1);
			}
		}
		edit.getPemegang().setEmail(f_validasi.convert_karakter(email_pp1));
		
	
		//	email pic
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			String email_pic1=edit.getContactPerson().getEmail();
			
			if(email_pic1==null)
			{
				email_pic1="";
			}
			String hasil_email_pic1 = pp.email_pp1(email_pic1);

//			if (jenis_terbit.intValue() == 1)
//			{
//				if (email_pp1.equalsIgnoreCase(""))
//				{
//					hasil_email_pp1 = "Jenis Terbit Polis adalah Softcopy , harus isi alamat email terlebih dahulu.";
//				}
//			}
			
			if (hasil_email_pic1.trim().length()!=0)
			{
				edit.getContactPerson().setEmail(f_validasi.convert_karakter(email_pic1));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("contactPerson.email","","PIC:" +hasil_email_pic1);
				}
			}
			edit.getContactPerson().setEmail(f_validasi.convert_karakter(email_pic1));
		}
	
		
		//email_penagihan
		String email_pp=edit.getAddressbilling().getE_mail();
		
		if(email_pp==null)
		{
			email_pp="";
		}
		String hasil_email_pp = pp.email_pp(email_pp);

		if (hasil_email_pp.trim().length()!=0)
		{
			edit.getAddressbilling().setE_mail(f_validasi.convert_karakter(email_pp));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("addressbilling.e_mail","","PEMEGANG:" +hasil_email_pp);
			}
		}
		edit.getAddressbilling().setE_mail(f_validasi.convert_karakter(email_pp));
		
			
		// alamat tagihan
//		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
//			edit.getAddressbilling().setTagih("2");
//		}
		String almt_tgh=edit.getAddressbilling().getMsap_address();
		if (almt_tgh==null)
		{
			almt_tgh="";
		}
		String tagih =edit.getAddressbilling().getTagih();
		if (tagih==null)
		{
			tagih="";
		}
		if (tagih.equalsIgnoreCase("2"))
		{
			if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
				almt_tgh=edit.getContactPerson().getAlamat_rumah().trim();
			}else{
				almt_tgh=edit.getPemegang().getAlamat_rumah().trim();
			}
		}else{
			if (tagih.equalsIgnoreCase("3"))
			{
				if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
					almt_tgh=almt_knt_pic.trim();
				}else{
					almt_tgh=almt_knt_pp.trim();
				}
			}
		}
		
		String hasil_almt_tagih = pp.alamat_tagih(almt_tgh);
		if (hasil_almt_tagih.trim().length()!=0)
		{
			edit.getAddressbilling().setMsap_address(f_validasi.convert_karakter(almt_tgh));
			err.rejectValue("addressbilling.msap_address","","PEMEGANG:" +hasil_almt_tagih);
		}
		edit.getAddressbilling().setMsap_address(f_validasi.convert_karakter(almt_tgh));
		
			
		String kota_tgh=edit.getAddressbilling().getKota_tgh();
		
		if (tagih.equalsIgnoreCase("2"))
		{
			if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
				kota_tgh=edit.getContactPerson().getKota_rumah();
			}else{
				kota_tgh=edit.getPemegang().getKota_rumah();
			}
		}else{
			if (tagih.equalsIgnoreCase("3"))
			{
				if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
					kota_tgh=kota_knt_pic;
				}else{
					kota_tgh=kota_knt_pp;
				}
			}
		}
		edit.getAddressbilling().setKota_tgh(kota_tgh);
		edit.getAddressbilling().setKode_kabupaten(null);
		String hasil_kota_tagih_pp="";
		if (kota_tgh==null)
		{
			kota_tgh = "";
			edit.getAddressbilling().setKota_tgh("");
			kota_tgh=edit.getAddressbilling().getKota_tgh();
		}
		
		if (tagih.equalsIgnoreCase("1"))
		{
			hasil_kota_tagih_pp=pp.kota_tagih(kota_tgh);
			if ( hasil_kota_tagih_pp.trim().length()!=0)
			{
				edit.getAddressbilling().setKota_tgh(f_validasi.convert_karakter(kota_tgh));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("addressbilling.kota_tagih","","PEMEGANG:" +hasil_kota_tagih_pp);
				}
			}
			edit.getAddressbilling().setKota_tgh((f_validasi.convert_karakter(kota_tgh)));
		}
		
		Integer kota_kabupaten =  this.elionsManager.select_kabupaten(kota_tgh);
		if (kota_kabupaten!=null)
		{
			edit.getAddressbilling().setKode_kabupaten(kota_kabupaten);
		}
		
		//kd_pos_tgh	
		String kd_pos_tgh=edit.getAddressbilling().getMsap_zip_code();
		if (kd_pos_tgh==null)
		{
			kd_pos_tgh="";
		}
		if (tagih.equalsIgnoreCase("2"))
		{
			if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
				kd_pos_tgh=edit.getContactPerson().getKd_pos_rumah();
			}else{
				kd_pos_tgh=edit.getPemegang().getKd_pos_rumah();
			}
		}else{
			if (tagih.equalsIgnoreCase("3"))
			{
				if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
					kd_pos_tgh=kd_pos_knt_pic;
				}else{
					kd_pos_tgh=kd_pos_knt_pp;
				}
			}
		}
		String hasil_kd_pos_tgh = pp.kd_pos_tgh(kd_pos_tgh);
		if (hasil_kd_pos_tgh.trim().length()!=0)
		{
			edit.getAddressbilling().setMsap_zip_code(f_validasi.convert_karakter(kd_pos_tgh));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("addressbilling.msap_zip_code","","PEMEGANG:" +hasil_kd_pos_tgh);
			}
		}
		edit.getAddressbilling().setMsap_zip_code(f_validasi.convert_karakter(kd_pos_tgh));
		
			
		//kd_area_tgh
		String kd_area_tgh=edit.getAddressbilling().getMsap_area_code1();
		if (kd_area_tgh==null)
		{
			kd_area_tgh="";
		}
		if (tagih.equalsIgnoreCase("2"))
		{
			if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
				kd_area_tgh=kd_area_rmh_pic;
			}else{
				kd_area_tgh=kd_area_rmh_pp;
			}
		}else{
			if (tagih.equalsIgnoreCase("3"))
			{
				if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
					kd_area_tgh=kd_area_knt_pic;
				}else{
					kd_area_tgh=kd_area_knt_pp;
				}
			}
		}
		String hasil_kd_area_tgh = pp.kd_area_tgh(kd_area_tgh);
		if (hasil_kd_area_tgh.trim().length()!=0)
		{
			edit.getAddressbilling().setMsap_area_code1(f_validasi.convert_karakter(kd_area_tgh));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("addressbilling.msap_area_code1","","PEMEGANG:" +hasil_kd_area_tgh);
			}
		}
		edit.getAddressbilling().setMsap_area_code1(f_validasi.convert_karakter(kd_area_tgh));
		
		String kd_area_tgh2=edit.getAddressbilling().getMsap_area_code2();
		if (kd_area_tgh2==null)
		{
			kd_area_tgh2="";
		}
		if (tagih.equalsIgnoreCase("2"))
		{
			if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
				kd_area_tgh2=kd_area_rmh_pic2;
			}else{
				kd_area_tgh2=kd_area_rmh_pp2;
			}
		}else{
			if (tagih.equalsIgnoreCase("3"))
			{
				if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
					kd_area_tgh2=kd_area_knt_pic2;
				}else{
					kd_area_tgh2=kd_area_knt_pp2;
				}
			}
		}
		String hasil_kd_area_tgh2 = pp.kd_area_tgh(kd_area_tgh2);
		if (hasil_kd_area_tgh2.trim().length()!=0)
		{
			edit.getAddressbilling().setMsap_area_code2(f_validasi.convert_karakter(kd_area_tgh2));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("addressbilling.msap_area_code2","","PEMEGANG:" +hasil_kd_area_tgh2);
			}
		}
		edit.getAddressbilling().setMsap_area_code2(f_validasi.convert_karakter(kd_area_tgh2));
		
		String kd_area_tgh3=edit.getAddressbilling().getMsap_area_code3();
		if (kd_area_tgh3==null)
		{
			kd_area_tgh3="";
		}

		String hasil_kd_area_tgh3 = pp.kd_area_tgh(kd_area_tgh3);
		if (hasil_kd_area_tgh3.trim().length()!=0)
		{
			edit.getAddressbilling().setMsap_area_code3(f_validasi.convert_karakter(kd_area_tgh3));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("addressbilling.msap_area_code3","","PEMEGANG:" +hasil_kd_area_tgh3);
			}
		}
		edit.getAddressbilling().setMsap_area_code3(f_validasi.convert_karakter(kd_area_tgh3));		
		
			
		//telp_tgh
		String telp_tgh=edit.getAddressbilling().getMsap_phone1();
		if (telp_tgh==null)
		{
			telp_tgh="";
		}
		if (tagih.equalsIgnoreCase("2"))
		{
			if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
				telp_tgh=telp_rmh_pic0;
			}else{
				telp_tgh=telp_rmh_pp0;
			}
		}else{
			if (tagih.equalsIgnoreCase("3"))
			{
				if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
					telp_tgh=telp_knt_pic;
				}else{
					telp_tgh=telp_knt_pp;
				}
			}
		}
		
		String hasil_telp_tgh = pp.telp_tgh(telp_tgh);
		if (hasil_telp_tgh.trim().length()!=0)
		{
			edit.getAddressbilling().setMsap_phone1(f_validasi.convert_karakter(telp_tgh));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("addressbilling.msap_phone1","","PEMEGANG:" +hasil_telp_tgh);
			}
		}
		edit.getAddressbilling().setMsap_phone1(f_validasi.convert_karakter(telp_tgh));
		
		String telp_tgh2=edit.getAddressbilling().getMsap_phone2();
		if (telp_tgh2==null)
		{
			telp_tgh2="";
		}
		if (tagih.equalsIgnoreCase("2"))
		{
			if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
				telp_tgh2=telp_rmh_pic02;
			}else{
				telp_tgh2=telp_rmh_pp02;
			}
		}else{
			if (tagih.equalsIgnoreCase("3"))
			{
				if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
					telp_tgh2=telp_knt_pic2;
				}else{
					telp_tgh2=telp_knt_pp2;
				}
			}
		}
		
		String hasil_telp_tgh2 = pp.telp_tgh(telp_tgh2);
		if (hasil_telp_tgh2.trim().length()!=0)
		{
			edit.getAddressbilling().setMsap_phone2(f_validasi.convert_karakter(telp_tgh2));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("addressbilling.msap_phone2","","PEMEGANG:" +hasil_telp_tgh2);
			}
		}
		edit.getAddressbilling().setMsap_phone2(f_validasi.convert_karakter(telp_tgh2));
		
		String telp_tgh3=edit.getAddressbilling().getMsap_phone3();
		if (telp_tgh3==null)
		{
			telp_tgh3="";
		}
		
		String hasil_telp_tgh3 = pp.telp_tgh(telp_tgh3);
		if (hasil_telp_tgh3.trim().length()!=0)
		{
			edit.getAddressbilling().setMsap_phone3(f_validasi.convert_karakter(telp_tgh3));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("addressbilling.msap_phone3","","PEMEGANG:" +hasil_telp_tgh3);
			}
		}
		edit.getAddressbilling().setMsap_phone3(f_validasi.convert_karakter(telp_tgh3));		
			
		//hp1_tgh
		String hp1_tgh=edit.getAddressbilling().getNo_hp();
		if (hp1_tgh==null)
		{
			hp1_tgh="";
		}
		String hasil_hp1_tgh = pp.hp1_tgh(hp1_tgh);
		if (hasil_hp1_tgh.trim().length()!=0)
		{
			edit.getAddressbilling().setNo_hp(f_validasi.convert_karakter(hp1_tgh));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("addressbilling.no_hp","","PEMEGANG:" +hasil_hp1_tgh);
			}
		}
		edit.getAddressbilling().setNo_hp(f_validasi.convert_karakter(hp1_tgh));
		
		String hp1_tgh2=edit.getAddressbilling().getNo_hp2();
		if (hp1_tgh2==null)
		{
			hp1_tgh2="";
		}
		String hasil_hp1_tgh2 = pp.hp2_tgh(hp1_tgh2);
		if (hasil_hp1_tgh2.trim().length()!=0)
		{
			edit.getAddressbilling().setNo_hp2(f_validasi.convert_karakter(hp1_tgh2));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("addressbilling.no_hp2","","PEMEGANG:" +hasil_hp1_tgh2);
			}
		}
		edit.getAddressbilling().setNo_hp2(f_validasi.convert_karakter(hp1_tgh2));
		
		//tujuan membeli asuransi
		String tujuan_asr= edit.getPemegang().getMkl_tujuan();
		String tujuan_asr_oth= edit.getPemegang().getTujuana();
		String hasil_tujuan_asr="";
		if (tujuan_asr==null)
		{
			tujuan_asr="Lain - Lain";
		}
		if (tujuan_asr.trim().toUpperCase().equalsIgnoreCase("LAIN - LAIN"))
		{
			hasil_tujuan_asr=pp.cek_tujuan_asuransi(tujuan_asr_oth);
			if (hasil_tujuan_asr.trim().length()!=0)
			{
				edit.getPemegang().setTujuana(f_validasi.convert_karakter(tujuan_asr_oth));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{ 
					err.rejectValue("pemegang.tujuana","","PEMEGANG:" +hasil_tujuan_asr);
				}
			}
			edit.getPemegang().setTujuana(f_validasi.convert_karakter(tujuan_asr_oth));
		}else{
			edit.getPemegang().setTujuana("");
		}
		
		//penghasilan kotor per tahun
		String penghasilan= edit.getPemegang().getMkl_penghasilan();
		edit.getPemegang().setMkl_penghasilan(penghasilan);
		
		//sumber pendanaan pembelian asuransi
		String sumber_dana= edit.getPemegang().getMkl_pendanaan();
		String sumber_dana_oth= edit.getPemegang().getDanaa();
		String hasil_sumber_dana="";
		if (sumber_dana==null)
		{
			sumber_dana="Lainnya";
		}
		if (sumber_dana.trim().toUpperCase().equalsIgnoreCase("LAINNYA"))
		{
			hasil_sumber_dana=pp.cek_sumber_dana(sumber_dana_oth);
			if (hasil_sumber_dana.trim().length()!=0)
			{
				edit.getPemegang().setDanaa(f_validasi.convert_karakter(sumber_dana_oth));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("pemegang.danaa","","PEMEGANG:" +hasil_sumber_dana);
				}
			}
			edit.getPemegang().setDanaa(f_validasi.convert_karakter(sumber_dana_oth));
		}else{
			edit.getPemegang().setDanaa("");
		}

		//sumber penghasilan
		String sumber_penghasilan = edit.getPemegang().getMkl_smbr_penghasilan();
		String sumber_penghasilan_oth = edit.getPemegang().getDanaa2();
		String hasil_sumber_penghasilan = "";
		if (sumber_penghasilan==null)
		{
			sumber_penghasilan="Lainnya";
		}
		if (sumber_penghasilan.trim().toUpperCase().equalsIgnoreCase("LAINNYA"))
		{
			hasil_sumber_penghasilan = pp.cek_sumber_penghasilan(sumber_penghasilan_oth);
			if (hasil_sumber_penghasilan.trim().length()!=0)
			{
				edit.getPemegang().setDanaa2(f_validasi.convert_karakter(sumber_penghasilan_oth));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					if(edit.getDatausulan().getJenis_pemegang_polis() == 0){
						err.rejectValue("pemegang.danaa2","","PEMEGANG:" +hasil_sumber_penghasilan);
					}
				}
			}
			edit.getPemegang().setDanaa2(f_validasi.convert_karakter(sumber_penghasilan_oth));
		}else{
			edit.getPemegang().setDanaa2("");
		}

		//	untuk input perusahaan (mkl_kerja)
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			edit.getPemegang().setMkl_kerja("Karyawan");
			edit.getPemegang().setKerjaa("");
		}
		
		//Klasifikasi Pekerjaan
		String pekerjaan= edit.getPemegang().getMkl_kerja();
		String pekerjaan_oth= edit.getPemegang().getKerjaa();
		String hasil_pekerjaan="";
		if (pekerjaan==null)
		{
			pekerjaan="Lainnya";
		}
		if (pekerjaan.trim().toUpperCase().equalsIgnoreCase("LAINNYA"))
		{
			hasil_pekerjaan=pp.cek_pekerjaan(pekerjaan_oth);
			if (hasil_pekerjaan.trim().length()!=0)
			{
				edit.getPemegang().setKerjaa(f_validasi.convert_karakter(pekerjaan_oth));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{ 
					if(edit.getDatausulan().getJenis_pemegang_polis() == 0){
						err.rejectValue("pemegang.kerjaa","","PEMEGANG:" +hasil_pekerjaan);
					}
				}
			}
			edit.getPemegang().setKerjaa(f_validasi.convert_karakter(pekerjaan_oth));
		}else{
			edit.getPemegang().setKerjaa("");
		}

		String jabatan="";
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			jabatan = edit.getContactPerson().getMpn_job_desc();
		}else{
			jabatan = edit.getPemegang().getKerjab();
		}
		String hasil_jabatan="";
		if (jabatan==null)
		{
			jabatan="";
		}

		if (pekerjaan.trim().equalsIgnoreCase("Karyawan"))
		{
			hasil_jabatan=pp.cek_jabatan(jabatan);
			if (hasil_jabatan.trim().length()!=0)
			{
				edit.getPemegang().setKerjab(f_validasi.convert_karakter(jabatan));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
						err.rejectValue("contactPerson.mpn_job_desc","","PIC:" + " Harap Lengkapi Jabatan PIC");
					}else{
						err.rejectValue("pemegang.kerjab","","PEMEGANG:" +hasil_jabatan);
					}
				}
			}
			edit.getPemegang().setKerjab(f_validasi.convert_karakter(jabatan));

		}else{
			edit.getPemegang().setKerjab("");
		}

		//klasifikasi bidang industri
		String bidang= edit.getPemegang().getMkl_industri();
		String bidang_oth= edit.getPemegang().getIndustria();
		String hasil_bidang="";
		if (bidang==null)
		{
			bidang="Lainnya";
		}
		if (bidang.trim().toUpperCase().equalsIgnoreCase("LAINNYA"))
		{
			hasil_bidang=pp.cek_bidang(bidang_oth);
			if (hasil_bidang.trim().length()!=0)
			{
				edit.getPemegang().setIndustria(f_validasi.convert_karakter(bidang_oth));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("pemegang.industria","","PEMEGANG:" +hasil_bidang);
				}
			}
			edit.getPemegang().setIndustria(f_validasi.convert_karakter(bidang_oth));
		}else{
			edit.getPemegang().setIndustria("");
		}
		

			
		Integer valid= edit.getDatausulan().getIndeks_validasi();
		if (valid==null)
		{
			valid=new Integer(0);
		}
		edit.getDatausulan().setIndeks_validasi(valid);

		edit.getDatausulan().setLi_umur_ttg(edit.getTertanggung().getMste_age());
		edit.getDatausulan().setLi_umur_pp(edit.getPemegang().getMste_age());
		edit.getPemegang().setUsiapp(edit.getPemegang().getMste_age());
		edit.getTertanggung().setUsiattg(edit.getTertanggung().getMste_age());

		if (edit.getDatausulan().getSpecta_save()==null)
		{
			edit.getDatausulan().setSpecta_save(new Integer(0));
		}
		
		// khusus produk specta , ketika centang langsung terisi kode plan dan kursnya serta kode agen 018567
		if (edit.getDatausulan().getSpecta_save().intValue() == 1)
		{
			edit.getDatausulan().setLsbs_id(155);
			edit.getDatausulan().setLsdbs_number(2);
			edit.getDatausulan().setKodeproduk("2");
			edit.getDatausulan().setPlan("155~X2");
			if (edit.getDatausulan().getKurs_p() == null)
			{
				edit.getDatausulan().setKurs_p("01");
			}
			if (edit.getDatausulan().getKurs_p().equalsIgnoreCase(""))
			{
				edit.getDatausulan().setKurs_p("01");
			}
			edit.getDatausulan().setKurs_premi("01");
			edit.getAgen().setMsag_id("018567");
		}
		
	// untuk input perusahaan (lsre_id)
//	if(hubungan  == null){
//		hubungan = 0;
//		edit.getPemegang().setLsre_id(6);// lain-lain
//	}
	//
		
	// akan dijalankan kalau belum tekan submit
	if (valid.intValue()==0)
		{
			//set isi tertanggung sama dengan pp karena diri sendiri
			if( hubungan.intValue()==1)
			{
				edit.getTertanggung().setAlamat_kantor(edit.getPemegang().getAlamat_kantor());
				edit.getTertanggung().setAlamat_rumah(edit.getPemegang().getAlamat_rumah());
				edit.getTertanggung().setArea_code_kantor(edit.getPemegang().getArea_code_kantor());
				edit.getTertanggung().setArea_code_rumah(edit.getPemegang().getArea_code_rumah());
				edit.getTertanggung().setDanaa(edit.getPemegang().getDanaa());
				edit.getTertanggung().setDanaa2(edit.getPemegang().getDanaa2());
				edit.getTertanggung().setEmail(edit.getPemegang().getEmail());
				edit.getTertanggung().setIndustria(edit.getPemegang().getIndustria());
				edit.getTertanggung().setKd_pos_kantor(edit.getPemegang().getKd_pos_kantor());
				edit.getTertanggung().setKd_pos_rumah(edit.getPemegang().getKd_pos_rumah());
				edit.getTertanggung().setKerjaa(edit.getPemegang().getKerjaa());
				edit.getTertanggung().setKerjab(edit.getPemegang().getKerjab());
				edit.getTertanggung().setKota_kantor(edit.getPemegang().getKota_kantor());
				edit.getTertanggung().setKota_rumah(edit.getPemegang().getKota_rumah());
				edit.getTertanggung().setLgj_id(edit.getPemegang().getLgj_id());
				edit.getTertanggung().setLgj_note(edit.getPemegang().getLgj_note());
				edit.getTertanggung().setLjb_id(edit.getPemegang().getLjb_id());
				//edit.getTertanggung().setLjb_note(edit.getPemegang().getLjb_note());
				edit.getTertanggung().setLsag_id(edit.getPemegang().getLsag_id());
				edit.getTertanggung().setLsag_name(edit.getPemegang().getLsag_name());
				edit.getTertanggung().setLscb_id(edit.getPemegang().getLscb_id());
				edit.getTertanggung().setLsed_id(edit.getPemegang().getLsed_id());
				edit.getTertanggung().setLsed_name(edit.getPemegang().getLsed_name());
				edit.getTertanggung().setLside_id(edit.getPemegang().getLside_id());
				edit.getTertanggung().setLside_name(edit.getPemegang().getLside_name());
				edit.getTertanggung().setLsne_id(edit.getPemegang().getLsne_id());
				edit.getTertanggung().setLsne_note(edit.getPemegang().getLsne_note());
				edit.getTertanggung().setLsre_id(edit.getPemegang().getLsre_id());
				edit.getTertanggung().setLsre_relation(edit.getPemegang().getLsre_relation());
				edit.getTertanggung().setLti_id(edit.getPemegang().getLti_id());
				edit.getTertanggung().setMcl_first(edit.getPemegang().getMcl_first());
				edit.getTertanggung().setMcl_gelar(edit.getPemegang().getMcl_gelar());
				edit.getTertanggung().setMcl_id(edit.getPemegang().getMcl_id());
				edit.getTertanggung().setMkl_industri(edit.getPemegang().getMkl_industri());
				edit.getTertanggung().setMkl_kerja(edit.getPemegang().getMkl_kerja());
				edit.getTertanggung().setMkl_pendanaan(edit.getPemegang().getMkl_pendanaan());
				edit.getTertanggung().setMkl_penghasilan(edit.getPemegang().getMkl_penghasilan());
				edit.getTertanggung().setMkl_smbr_penghasilan(edit.getPemegang().getMkl_smbr_penghasilan());
				edit.getTertanggung().setMkl_tujuan(edit.getPemegang().getMkl_tujuan());
				edit.getTertanggung().setMpn_job_desc(edit.getPemegang().getMpn_job_desc());
				edit.getTertanggung().setMsag_id(edit.getPemegang().getMsag_id());
				edit.getTertanggung().setMspe_date_birth(edit.getPemegang().getMspe_date_birth());
				edit.getTertanggung().setMspe_lama_kerja(edit.getPemegang().getMspe_lama_kerja());
				edit.getTertanggung().setMspe_mother(edit.getPemegang().getMspe_mother());
				edit.getTertanggung().setMspe_no_identity(edit.getPemegang().getMspe_no_identity());
				edit.getTertanggung().setMspe_place_birth(edit.getPemegang().getMspe_place_birth());
				edit.getTertanggung().setMspe_sex(edit.getPemegang().getMspe_sex());
				edit.getTertanggung().setMspe_sex2(edit.getPemegang().getMspe_sex2());
				edit.getTertanggung().setMspe_sts_mrt(edit.getPemegang().getMspe_sts_mrt());
				edit.getTertanggung().setMspo_policy_no(edit.getPemegang().getMspo_policy_no());
				edit.getTertanggung().setMste_age(edit.getPemegang().getMste_age());
				edit.getTertanggung().setMste_insured_no(edit.getPemegang().getMste_insured_no());
				edit.getTertanggung().setMste_standard(edit.getPemegang().getMste_standard());
				edit.getTertanggung().setNo_hp(edit.getPemegang().getNo_hp());
				edit.getTertanggung().setNo_hp2(edit.getPemegang().getNo_hp2());
				edit.getTertanggung().setReg_spaj(edit.getPemegang().getReg_spaj());
				edit.getTertanggung().setTelpon_kantor(edit.getPemegang().getTelpon_kantor());
				edit.getTertanggung().setTelpon_rumah(edit.getPemegang().getTelpon_rumah());
				edit.getTertanggung().setTujuana(edit.getPemegang().getTujuana());
				edit.getTertanggung().setTelpon_kantor2(edit.getPemegang().getTelpon_kantor2());
				edit.getTertanggung().setArea_code_kantor2(edit.getPemegang().getArea_code_kantor2());
				edit.getTertanggung().setTelpon_rumah2(edit.getPemegang().getTelpon_rumah2());
				edit.getTertanggung().setArea_code_rumah2(edit.getPemegang().getArea_code_rumah2());
				edit.getTertanggung().setNo_fax(edit.getPemegang().getNo_fax());
				edit.getTertanggung().setArea_code_fax(edit.getPemegang().getArea_code_fax());
				edit.getDatausulan().setLi_umur_ttg(edit.getPemegang().getMste_age());
				edit.getTertanggung().setUsiattg(edit.getPemegang().getMste_age());
				
				edit.getTertanggung().setNama_si(edit.getPemegang().getNama_si());
				edit.getTertanggung().setNama_anak1(edit.getPemegang().getNama_anak1());
				edit.getTertanggung().setNama_anak2(edit.getPemegang().getNama_anak2());
				edit.getTertanggung().setNama_anak3(edit.getPemegang().getNama_anak3());
				edit.getTertanggung().setTgllhr_si(edit.getPemegang().getTgllhr_si());
				edit.getTertanggung().setTgllhr_anak1(edit.getPemegang().getTgllhr_anak1());
				edit.getTertanggung().setTgllhr_anak2(edit.getPemegang().getTgllhr_anak2());
				edit.getTertanggung().setTgllhr_anak3(edit.getPemegang().getTgllhr_anak3());
				edit.getTertanggung().setMcl_agama(edit.getPemegang().getMcl_agama());
		
			}else{
				//dijalankan kalau edit
				if (!spaj.equalsIgnoreCase(""))
				{
					edit.setTertanggung((Tertanggung)this.elionsManager.selectttg(edit.getPemegang().getReg_spaj()));
				}else if (spaj.equalsIgnoreCase("")){
					if(edit.getCurrentUser().getLca_id().equals("58")){
						edit.setTertanggung(new Tertanggung());
					}else{
						//bukan diri sendiri tidak perlu kosongin data tertanggung, karena kemungkinan banyak data yang sama dengan pemegang polis
						edit.getTertanggung().setAlamat_kantor(edit.getPemegang().getAlamat_kantor());
						edit.getTertanggung().setAlamat_rumah(edit.getPemegang().getAlamat_rumah());
						edit.getTertanggung().setArea_code_kantor(edit.getPemegang().getArea_code_kantor());
						edit.getTertanggung().setArea_code_rumah(edit.getPemegang().getArea_code_rumah());
						edit.getTertanggung().setDanaa(edit.getPemegang().getDanaa());
						edit.getTertanggung().setDanaa2(edit.getPemegang().getDanaa2());
						//edit.getTertanggung().setMkl_smbr_penghasilan(edit.getPemegang().getMkl_smbr_penghasilan());
						edit.getTertanggung().setEmail(edit.getPemegang().getEmail());
						edit.getTertanggung().setIndustria(edit.getPemegang().getIndustria());
						edit.getTertanggung().setKd_pos_kantor(edit.getPemegang().getKd_pos_kantor());
						edit.getTertanggung().setKd_pos_rumah(edit.getPemegang().getKd_pos_rumah());
						edit.getTertanggung().setKerjaa(edit.getPemegang().getKerjaa());
						edit.getTertanggung().setKerjab(edit.getPemegang().getKerjab());
						edit.getTertanggung().setKota_kantor(edit.getPemegang().getKota_kantor());
						edit.getTertanggung().setKota_rumah(edit.getPemegang().getKota_rumah());
						//edit.getTertanggung().setLgj_id(edit.getPemegang().getLgj_id());
						//edit.getTertanggung().setLgj_note(edit.getPemegang().getLgj_note());
						//edit.getTertanggung().setLjb_id(edit.getPemegang().getLjb_id());
						//edit.getTertanggung().setLjb_note(edit.getPemegang().getLjb_note());
						//edit.getTertanggung().setLsag_id(edit.getPemegang().getLsag_id());
						//edit.getTertanggung().setLsag_name(edit.getPemegang().getLsag_name());
						//edit.getTertanggung().setLscb_id(edit.getPemegang().getLscb_id());
						//edit.getTertanggung().setLsed_id(edit.getPemegang().getLsed_id());
						//edit.getTertanggung().setLsed_name(edit.getPemegang().getLsed_name());
						//edit.getTertanggung().setLside_id(edit.getPemegang().getLside_id());
						//edit.getTertanggung().setLside_name(edit.getPemegang().getLside_name());
						//edit.getTertanggung().setLsne_id(edit.getPemegang().getLsne_id());
						//edit.getTertanggung().setLsne_note(edit.getPemegang().getLsne_note());
						//edit.getTertanggung().setLsre_id(edit.getPemegang().getLsre_id());
						//edit.getTertanggung().setLsre_relation(edit.getPemegang().getLsre_relation());
						//edit.getTertanggung().setMcl_first("");
						////edit.getTertanggung().setMcl_gelar(edit.getPemegang().getMcl_gelar());
						//edit.getTertanggung().setMcl_id(edit.getPemegang().getMcl_id());
						edit.getTertanggung().setMkl_industri(edit.getPemegang().getMkl_industri());
						edit.getTertanggung().setMkl_kerja(edit.getPemegang().getMkl_kerja());
						edit.getTertanggung().setMkl_pendanaan(edit.getPemegang().getMkl_pendanaan());
						edit.getTertanggung().setMkl_penghasilan(edit.getPemegang().getMkl_penghasilan());
						edit.getTertanggung().setMkl_tujuan(edit.getPemegang().getMkl_tujuan());
						edit.getTertanggung().setMpn_job_desc(edit.getPemegang().getMpn_job_desc());
						//edit.getTertanggung().setMsag_id(edit.getPemegang().getMsag_id());
						//edit.getTertanggung().setMspe_date_birth(edit.getPemegang().getMspe_date_birth());
						edit.getTertanggung().setMspe_lama_kerja(edit.getPemegang().getMspe_lama_kerja());
						//edit.getTertanggung().setMspe_mother(edit.getPemegang().getMspe_mother());
						//edit.getTertanggung().setMspe_no_identity(edit.getPemegang().getMspe_no_identity());
						//edit.getTertanggung().setMspe_place_birth(edit.getPemegang().getMspe_place_birth());
						//edit.getTertanggung().setMspe_sex(edit.getPemegang().getMspe_sex());
						//edit.getTertanggung().setMspe_sex2(edit.getPemegang().getMspe_sex2());
						//edit.getTertanggung().setMspe_sts_mrt(edit.getPemegang().getMspe_sts_mrt());
						//edit.getTertanggung().setMspo_policy_no(edit.getPemegang().getMspo_policy_no());
						//edit.getTertanggung().setMste_age(edit.getPemegang().getMste_age());
						//edit.getTertanggung().setMste_insured_no(edit.getPemegang().getMste_insured_no());
						//edit.getTertanggung().setMste_standard(edit.getPemegang().getMste_standard());
						edit.getTertanggung().setNo_hp(edit.getPemegang().getNo_hp());
						edit.getTertanggung().setNo_hp2(edit.getPemegang().getNo_hp2());
						//edit.getTertanggung().setReg_spaj(edit.getPemegang().getReg_spaj());
						edit.getTertanggung().setTelpon_kantor(edit.getPemegang().getTelpon_kantor());
						edit.getTertanggung().setTelpon_rumah(edit.getPemegang().getTelpon_rumah());
						edit.getTertanggung().setTujuana(edit.getPemegang().getTujuana());
						edit.getTertanggung().setTelpon_kantor2(edit.getPemegang().getTelpon_kantor2());
						edit.getTertanggung().setArea_code_kantor2(edit.getPemegang().getArea_code_kantor2());
						edit.getTertanggung().setTelpon_rumah2(edit.getPemegang().getTelpon_rumah2());
						edit.getTertanggung().setArea_code_rumah2(edit.getPemegang().getArea_code_rumah2());
						edit.getTertanggung().setNo_fax(edit.getPemegang().getNo_fax());
						edit.getTertanggung().setArea_code_fax(edit.getPemegang().getArea_code_fax());
						//edit.getDatausulan().setLi_umur_ttg(edit.getPemegang().getMste_age());
						//edit.getTertanggung().setUsiattg(edit.getPemegang().getMste_age());
					}
					
//					 tambahan untuk badan usaha (hubungan antara majukan/karyawan)
					if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
//						data PIC = data tertanggung, karena kemungkinan banyak data yang sama dengan PIC
						edit.getTertanggung().setAlamat_kantor(edit.getContactPerson().getAlamat_kantor());
						edit.getTertanggung().setAlamat_rumah(edit.getContactPerson().getAlamat_rumah());
						edit.getTertanggung().setArea_code_kantor(edit.getContactPerson().getArea_code_kantor());
						edit.getTertanggung().setArea_code_rumah(edit.getContactPerson().getArea_code_rumah());
						edit.getTertanggung().setDanaa(edit.getPemegang().getDanaa());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setDanaa2(edit.getPemegang().getDanaa2());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setEmail(edit.getContactPerson().getEmail());
						edit.getTertanggung().setIndustria(edit.getPemegang().getIndustria());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setKd_pos_kantor(edit.getContactPerson().getKd_pos_kantor());
						edit.getTertanggung().setKd_pos_rumah(edit.getContactPerson().getKd_pos_rumah());
						edit.getTertanggung().setKerjaa(edit.getPemegang().getKerjaa());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setKerjab(edit.getPemegang().getKerjab());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setKota_kantor(edit.getContactPerson().getKota_kantor());
						edit.getTertanggung().setKota_rumah(edit.getContactPerson().getKota_rumah());
//						edit.getTertanggung().setLgj_id(edit.getContactPerson().getLgj_id());//DARI PEMEGANG BADAN USAHA
//						edit.getTertanggung().setLgj_note(edit.getContactPerson().getLgj_note());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setLjb_id(edit.getContactPerson().getLjb_id());
						//edit.getTertanggung().setLjb_note(edit.getContactPerson().getLjb_note());
						edit.getTertanggung().setLsag_id(edit.getContactPerson().getLsag_id());
						edit.getTertanggung().setLsag_name(edit.getPemegang().getLsag_name());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setLscb_id(edit.getPemegang().getLscb_id());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setLsed_id(edit.getContactPerson().getLsed_id());
						edit.getTertanggung().setLsed_name(edit.getPemegang().getLsed_name());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setLside_id(edit.getContactPerson().getLside_id());
						edit.getTertanggung().setLside_name(edit.getPemegang().getLside_name());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setLsne_id(edit.getContactPerson().getLsne_id());
						edit.getTertanggung().setLsne_note(edit.getPemegang().getLsne_note());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setLsre_id(edit.getPemegang().getLsre_id());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setLsre_relation(edit.getPemegang().getLsre_relation());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setLti_id(edit.getContactPerson().getLti_id());
						edit.getTertanggung().setMcl_first(edit.getContactPerson().getNama_lengkap());
						edit.getTertanggung().setMcl_gelar(edit.getContactPerson().getMcl_gelar());
						edit.getTertanggung().setMcl_id(edit.getContactPerson().getMcl_id());
						edit.getTertanggung().setMkl_industri(edit.getPemegang().getMkl_industri());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setMkl_kerja(edit.getPemegang().getMkl_kerja());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setMkl_pendanaan(edit.getPemegang().getMkl_pendanaan());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setMkl_penghasilan(edit.getPemegang().getMkl_penghasilan());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setMkl_smbr_penghasilan(edit.getPemegang().getMkl_smbr_penghasilan());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setMkl_tujuan(edit.getPemegang().getMkl_tujuan());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setMpn_job_desc(edit.getContactPerson().getMpn_job_desc());
						edit.getTertanggung().setMsag_id(edit.getPemegang().getMsag_id());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setMspe_date_birth(edit.getContactPerson().getDate_birth());
						edit.getTertanggung().setMspe_lama_kerja(edit.getContactPerson().getLama_kerja());
						edit.getTertanggung().setMspe_mother(edit.getContactPerson().getMspe_mother());
						edit.getTertanggung().setMspe_no_identity(edit.getContactPerson().getNo_identity());
						edit.getTertanggung().setMspe_place_birth(edit.getContactPerson().getPlace_birth());
						edit.getTertanggung().setMspe_sex(edit.getContactPerson().getMste_sex());
						//edit.getTertanggung().setMspe_sex2(edit.getContactPerson().getMspe_sex2());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setMspe_sts_mrt(edit.getContactPerson().getSts_mrt());
						edit.getTertanggung().setMspo_policy_no(edit.getPemegang().getMspo_policy_no());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setMste_age(edit.getContactPerson().getMste_age());
						edit.getTertanggung().setMste_insured_no(edit.getPemegang().getMste_insured_no());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setMste_standard(edit.getPemegang().getMste_standard());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setNo_hp(edit.getContactPerson().getNo_hp());
						edit.getTertanggung().setNo_hp2(edit.getContactPerson().getNo_hp2());
						edit.getTertanggung().setReg_spaj(edit.getPemegang().getReg_spaj());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setTelpon_kantor(edit.getContactPerson().getTelpon_kantor());
						edit.getTertanggung().setTelpon_rumah(edit.getContactPerson().getTelpon_rumah());
						edit.getTertanggung().setTujuana(edit.getPemegang().getTujuana());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setTelpon_kantor2(edit.getContactPerson().getTelpon_kantor2());
						edit.getTertanggung().setArea_code_kantor2(edit.getContactPerson().getArea_code_kantor2());
						edit.getTertanggung().setTelpon_rumah2(edit.getContactPerson().getTelpon_rumah2());
						edit.getTertanggung().setArea_code_rumah2(edit.getContactPerson().getArea_code_rumah2());
						edit.getTertanggung().setNo_fax(edit.getContactPerson().getNo_fax());
						edit.getTertanggung().setArea_code_fax(edit.getContactPerson().getArea_code_fax());
						edit.getDatausulan().setLi_umur_ttg(edit.getContactPerson().getMste_age());
						edit.getTertanggung().setUsiattg(edit.getContactPerson().getMste_age());
						
						edit.getTertanggung().setNama_si(edit.getContactPerson().getNama_si());
						edit.getTertanggung().setNama_anak1(edit.getContactPerson().getNama_anak1());
						edit.getTertanggung().setNama_anak2(edit.getContactPerson().getNama_anak2());
						edit.getTertanggung().setNama_anak3(edit.getContactPerson().getNama_anak3());
						edit.getTertanggung().setTgllhr_si(edit.getContactPerson().getTgllhr_si());
						edit.getTertanggung().setTgllhr_anak1(edit.getContactPerson().getTgllhr_anak1());
						edit.getTertanggung().setTgllhr_anak2(edit.getContactPerson().getTgllhr_anak2());
						edit.getTertanggung().setTgllhr_anak3(edit.getContactPerson().getTgllhr_anak3());
						edit.getTertanggung().setMcl_agama(edit.getContactPerson().getAgama());
					}
				}
				
				
			}
			if (edit.getDatausulan().getStatus_submit() == null)
			{
				edit.getDatausulan().setStatus_submit("");
			}
			String status="";
			if ( edit.getPemegang().getReg_spaj() != null && !edit.getPemegang().getReg_spaj().equalsIgnoreCase("") )
			{
				status="edit";
				if (edit.getDatausulan().getStatus_submit().equalsIgnoreCase("ulang"))
				{
					status ="input";
					edit.getPemegang().setReg_spaj("");
				}
				
			}else{
				status ="input";
			}
			if (status.equalsIgnoreCase("edit"))
			{
				Integer status_polis = this.elionsManager.selectPositionSpaj(edit.getPemegang().getReg_spaj());
				if (status_polis == null)
				{
					status_polis = new Integer(1);
					this.elionsManager.savelssaid(edit.getPemegang().getReg_spaj(), status_polis);
				}
				
				// kalau gutri lama tidak cek status polisnya.. karena gutri polis lama diperbaharui
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0)
				{
					if (status_polis.intValue() == 5)
					{
						err.rejectValue("pemegang.reg_spaj","","Spaj ini sudah diaksep tidak boleh diedit.");
					}
				}
				List data_rider =this.elionsManager.select_hcp(edit.getPemegang().getReg_spaj());
				if(products.unitLink(edit.getDatausulan().getLsbs_id().toString())){
					Integer index_data_rider= new Integer(0);
						if (data_rider == null)
						{
							index_data_rider= new Integer(0);
						}else{
							index_data_rider =new Integer(data_rider.size());
						}
					
					if (index_data_rider.intValue() == 0)
					{
						edit.getDatausulan().setFlag_rider_hcp(new Integer(0));
						edit.getDatausulan().setCek_flag_hcp(new Integer(0));
					}else{
						edit.getDatausulan().setFlag_rider_hcp(new Integer(1));
						edit.getDatausulan().setCek_flag_hcp(new Integer(1));
					}
				}
			}
			edit.getPemegang().setStatus(status);
		}
	
		// dijalankan kalau edit
		if (!spaj.equalsIgnoreCase("") )
		{
			if (flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				//cek simultan pp kalau sudah pernah simultan tidak boleh diedit lagi
				Integer jumlah_simultan_pp = (Integer)this.elionsManager.count_simultan(edit.getPemegang().getMcl_id());
				if (jumlah_simultan_pp == null)
				{
					jumlah_simultan_pp = new Integer(0);
				}
				if (jumlah_simultan_pp.intValue() > 0)
				{
					String mcl_id = edit.getPemegang().getMcl_id();
					String mcl_first = edit.getPemegang().getMcl_first();
					Date tgl_lahir = edit.getPemegang().getMspe_date_birth();
					Integer tanggal = tgl_lahir.getDate();
					Integer bulan = tgl_lahir.getMonth()+1;
					Integer tahun = tgl_lahir.getYear()+1900;
					String mspe_date_birth = Integer.toString(tahun) + FormatString.rpad("0",Integer.toString(bulan),2)+FormatString.rpad("0",Integer.toString(tanggal),2);
					Integer jumlah_client_simultan = (Integer) this.elionsManager.count_client_simultan(mcl_id, mcl_first, mspe_date_birth);
					if (jumlah_client_simultan==null)
					{
						jumlah_client_simultan=new Integer(0);
					}
					if (jumlah_client_simultan.intValue() == 0)
					{
						err.rejectValue("pemegang.mcl_first","","Spaj ini sudah simultan, data pemegang polis tidak boleh diedit.");
					}
				}
			}
		}
	}
	
//---------------------------tertanggung--------------------------------	
	public void validatettg(Object cmd, Errors err) {
	try {
		logger.debug("EditBacValidator : validate page validatettg");
		Cmdeditbac edit= (Cmdeditbac) cmd;
		
		form_tertanggung a =new form_tertanggung();
		//untuk endors
		Integer jumlah_cancel = edit.getPemegang().getJumlah_cancel();
		Integer flag_gutri = edit.getDatausulan().getFlag_gutri();
		Integer mste_flag_guthrie = edit.getPemegang().getMste_flag_guthrie();
		
		//nama_ttg
		String nama_ttg=edit.getTertanggung().getMcl_first();
		if (nama_ttg==null)
		{
			nama_ttg="";
		}
		String hasil_nama_ttg = a.nama_ttg(nama_ttg);
		if(hasil_nama_ttg.trim().length()!=0)
		{
			edit.getTertanggung().setMcl_first(f_validasi.convert_karakter(nama_ttg));
			err.rejectValue("tertanggung.mcl_first","","HALAMAN TERTANGGUNG :" +hasil_nama_ttg);
		}
		edit.getTertanggung().setMcl_first(f_validasi.convert_karakter(nama_ttg));
		
		//gelar_ttg
		String gelar_ttg=edit.getTertanggung().getMcl_gelar();
		if (gelar_ttg==null)
		{
			gelar_ttg="";
		}
		edit.getTertanggung().setMcl_gelar(f_validasi.convert_karakter(gelar_ttg));
		
		// ibu_kandung_ttg
		String nm_ibu_ttg=edit.getTertanggung().getMspe_mother();
		if (nm_ibu_ttg==null)
		{
			nm_ibu_ttg="";
		}
		edit.getTertanggung().setMspe_mother(f_validasi.convert_karakter(nm_ibu_ttg));
		
		//nomor_id_ttg
		String nomor_id_ttg=edit.getTertanggung().getMspe_no_identity();
		if (nomor_id_ttg==null)
		{
			nomor_id_ttg="";
		}
		String hasil_nomor_id_ttg = a.nomor_id_ttg(nomor_id_ttg);
		if (hasil_nomor_id_ttg.trim().length()!=0)
		{
			edit.getTertanggung().setMspe_no_identity(f_validasi.convert_karakter(nomor_id_ttg));
			err.rejectValue("tertanggung.mspe_no_identity","","HALAMAN TERTANGGUNG :" +hasil_nomor_id_ttg);
		}
		edit.getTertanggung().setMspe_no_identity(f_validasi.convert_karakter(nomor_id_ttg));
		
		Date tgl_lahir_ttg = edit.getTertanggung().getMspe_date_birth();
		if ( tgl_lahir_ttg == null)
		{
			err.rejectValue("tertanggung.mspe_date_birth","","HALAMAN TERTANGGUNG : Tanggal Lahir Tertanggung masih kosong, Silahkan isi tanggal lahir tertanggung terlebih dahulu");
		}
		
		//tempat_lahir_ttg
		String tmpt_lahir_ttg=edit.getTertanggung().getMspe_place_birth();
		if (tmpt_lahir_ttg==null)
		{
			tmpt_lahir_ttg="";
		}
		String hasil_tmpt_lhr_ttg = a.tempat_lahir_ttg(tmpt_lahir_ttg);
		if (hasil_tmpt_lhr_ttg.trim().length()!=0)
		{
			edit.getTertanggung().setMspe_place_birth(f_validasi.convert_karakter(tmpt_lahir_ttg));
			err.rejectValue("tertanggung.mspe_place_birth","","HALAMAN TERTANGGUNG :" +hasil_tmpt_lhr_ttg);
		}
		edit.getTertanggung().setMspe_place_birth(f_validasi.convert_karakter(tmpt_lahir_ttg));
		
		String status_marital = edit.getTertanggung().getMspe_sts_mrt();
		String nama_suamiistri = edit.getTertanggung().getNama_si();
		if (nama_suamiistri==null)
		{
			nama_suamiistri = "";
			edit.getTertanggung().setNama_si(nama_suamiistri);
		}
		
//		Apabila agama lain-lain wajib isi
		if(edit.getTertanggung().getLsag_id()==6){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "tertanggung.mcl_agama","","Tertanggung: Silahkan sebutkan agama lainnya.");
		}

	/*	if (status_marital.equalsIgnoreCase("2"))
		{
			if (nama_suamiistri.equalsIgnoreCase(""))
			{
				err.rejectValue("tertanggung.nama_si","","HALAMAN TERTANGGUNG :Nama Suami/Istri masih kosong, silahkan isi nama suami/istri terlebih dahulu.");
			}
			Date tanggal_lahir_suamiistri = edit.getPemegang().getTgllhr_si();
			if (tanggal_lahir_suamiistri == null)
			{
				err.rejectValue("tertanggung.tgllhr_si","","HALAMAN TERTANGGUNG :Tanggal Lahir suami/istri tertanggung masih kosong, Silahkan isi tanggal lahir suami/istri terlebih dahulu");
			}
		}else{*/
			if (nama_suamiistri.equalsIgnoreCase(""))
			{
				edit.getTertanggung().setTgllhr_si(null);
			}
		//}
		
		String nama_anak1 = edit.getTertanggung().getNama_anak1();
		if (nama_anak1 == null)
		{
			nama_anak1="";
			edit.getTertanggung().setNama_anak1(nama_anak1);
		}
		if (nama_anak1.equalsIgnoreCase(""))
		{
			edit.getTertanggung().setTgllhr_anak1(null);
		}
		
		String nama_anak2 = edit.getTertanggung().getNama_anak2();
		if (nama_anak2 == null)
		{
			nama_anak2 = "";
			edit.getTertanggung().setNama_anak2(nama_anak2);
		}
		if (nama_anak2.equalsIgnoreCase(""))
		{
			edit.getTertanggung().setTgllhr_anak2(null);
		}
		
		String nama_anak3 = edit.getTertanggung().getNama_anak3();
		if (nama_anak3 == null)
		{
			nama_anak3="";
			edit.getTertanggung().setNama_anak3(nama_anak3);
		}
		if (nama_anak3.equalsIgnoreCase(""))
		{
			edit.getTertanggung().setTgllhr_anak3(null);
		}
		
		//alamat_rumah_ttg
		String almt_rmh_ttg=edit.getTertanggung().getAlamat_rumah();
		if (almt_rmh_ttg==null)
		{
			almt_rmh_ttg="";
		}
		String hasil_almt_rmh_ttg = a.alamat_rumah_ttg(almt_rmh_ttg);
		if (hasil_almt_rmh_ttg.trim().length()!=0)
		{
			edit.getTertanggung().setAlamat_rumah(f_validasi.convert_karakter(almt_rmh_ttg));
			err.rejectValue("tertanggung.alamat_rumah","","HALAMAN TERTANGGUNG :" +hasil_almt_rmh_ttg);
		}
		edit.getTertanggung().setAlamat_rumah(f_validasi.convert_karakter(almt_rmh_ttg));
		
		//kota_rumah_ttg
		String kota_rmh_ttg=edit.getTertanggung().getKota_rumah();
		String hasil_kota_rumah_ttg="";
		if (kota_rmh_ttg==null)
		{
			kota_rmh_ttg = "";
			edit.getTertanggung().setKota_rumah("");
			kota_rmh_ttg=edit.getTertanggung().getKota_rumah();
		}
		
		hasil_kota_rumah_ttg=a.kota_rumah_ttg(kota_rmh_ttg);
		if ( hasil_kota_rumah_ttg.trim().length()!=0)
		{
			edit.getTertanggung().setKota_rumah(f_validasi.convert_karakter(kota_rmh_ttg));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("tertanggung.kota_rumah","","HALAMAN TERTANGGUNG :" +hasil_kota_rumah_ttg);
			}
		}
		edit.getTertanggung().setKota_rumah(f_validasi.convert_karakter(kota_rmh_ttg));
		
		
		
		//kode_pos_rumah_ttg		
		String kd_pos_rmh_ttg0=edit.getTertanggung().getKd_pos_rumah();
		if (kd_pos_rmh_ttg0==null)
		{
			kd_pos_rmh_ttg0="";
		}
		String hasil_kd_pos_rmh_ttg0 = a.kode_pos_rmh_ttg(kd_pos_rmh_ttg0);
		if (hasil_kd_pos_rmh_ttg0.trim().length()!=0)
		{
			edit.getTertanggung().setKd_pos_rumah(f_validasi.convert_karakter(kd_pos_rmh_ttg0));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("tertanggung.kd_pos_rumah","","HALAMAN TERTANGGUNG :" +hasil_kd_pos_rmh_ttg0);
			}
		}
		edit.getTertanggung().setKd_pos_rumah(f_validasi.convert_karakter(kd_pos_rmh_ttg0));			
		
		//kode_area_rumah_ttg
		String kd_area_rmh_ttg=edit.getTertanggung().getArea_code_rumah();
		if (kd_area_rmh_ttg==null)
		{
			kd_area_rmh_ttg="";
		}
		String hasil_kd_area_rmh_ttg = a.kode_area_rmh_ttg(kd_area_rmh_ttg);
		if (hasil_kd_area_rmh_ttg.trim().length()!=0)
		{
			edit.getTertanggung().setArea_code_rumah(f_validasi.convert_karakter(kd_area_rmh_ttg));
			//if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			//{
				if (kd_area_rmh_ttg.trim().length()==0)
				{
					kd_area_rmh_ttg="-";
					hasil_kd_area_rmh_ttg ="";
				}
				edit.getTertanggung().setArea_code_rumah(f_validasi.convert_karakter(kd_area_rmh_ttg));	

				if (!hasil_kd_area_rmh_ttg.equalsIgnoreCase(""))
				{
					err.rejectValue("tertanggung.area_code_rumah","","HALAMAN TERTANGGUNG :" +hasil_kd_area_rmh_ttg);
				}
			/*}else{
				if (kd_area_rmh_ttg.trim().length()==0)
				{
					kd_area_rmh_ttg="-";
					hasil_kd_area_rmh_ttg ="";
				}
				edit.getTertanggung().setArea_code_rumah(f_validasi.convert_karakter(kd_area_rmh_ttg));	

			}*/
		}
		
		edit.getTertanggung().setArea_code_rumah(f_validasi.convert_karakter(kd_area_rmh_ttg));	
		
		String kd_area_rmh_ttg2=edit.getTertanggung().getArea_code_rumah2();
		if (kd_area_rmh_ttg2==null)
		{
			kd_area_rmh_ttg2="";
		}
		String hasil_kd_area_rmh_ttg2 = a.kode_area_rmh_ttg2(kd_area_rmh_ttg2);
		if (hasil_kd_area_rmh_ttg2.trim().length()!=0)
		{
			edit.getTertanggung().setArea_code_rumah2(f_validasi.convert_karakter(kd_area_rmh_ttg2));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("tertanggung.area_code_rumah2","","HALAMAN TERTANGGUNG :" +hasil_kd_area_rmh_ttg2);
			}
		}
		edit.getTertanggung().setArea_code_rumah2(f_validasi.convert_karakter(kd_area_rmh_ttg2));		
		
		//kode area fax ttg	
		String area_fax = edit.getTertanggung().getArea_code_fax();
		if (area_fax==null)
		{
			area_fax="";
		}
		String hasil_area_fax = a.kode_area_fax(area_fax);
		if (hasil_area_fax.trim().length()!=0)
		{
			edit.getTertanggung().setArea_code_fax(f_validasi.convert_karakter(area_fax));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("tertanggung.area_code_fax","","HALAMAN TERTANGGUNG :" +hasil_area_fax);
			}
		}		
		
		//fax pp	
		String fax = edit.getTertanggung().getNo_fax();
		if (fax==null)
		{
			fax="";
		}
		String hasil_fax = a.fax_pp(fax);
		if (hasil_fax.trim().length()!=0)
		{
			edit.getTertanggung().setNo_fax(f_validasi.convert_karakter(fax));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("tertanggung.no_fax","","HALAMAN TERTANGGUNG :" +hasil_fax);
			}
		}
		
		//telpon_rumah_ttg
		String telp_rmh_ttg0=edit.getTertanggung().getTelpon_rumah();
		if (telp_rmh_ttg0==null)
		{
			telp_rmh_ttg0="";
		}
		String hasil_telp_rmh_ttg0 = a.telpon_rmh_ttg(telp_rmh_ttg0);
		if (hasil_telp_rmh_ttg0.trim().length()!=0)
		{
			edit.getTertanggung().setTelpon_rumah(f_validasi.convert_karakter(telp_rmh_ttg0));
			//if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			//{
				if (telp_rmh_ttg0.trim().length()==0)
				{
					telp_rmh_ttg0="-";
					hasil_telp_rmh_ttg0 ="";
				}
				edit.getTertanggung().setTelpon_rumah(f_validasi.convert_karakter(telp_rmh_ttg0));	

				if (!hasil_telp_rmh_ttg0.equalsIgnoreCase(""))
				{
					err.rejectValue("tertanggung.telpon_rumah","","HALAMAN TERTANGGUNG :" +hasil_telp_rmh_ttg0);
				}
		/*	}else{
				if (telp_rmh_ttg0.trim().length()==0)
				{
					telp_rmh_ttg0="-";
					hasil_telp_rmh_ttg0 ="";
				}
				edit.getTertanggung().setTelpon_rumah(f_validasi.convert_karakter(telp_rmh_ttg0));	

			}*/
		}

		edit.getTertanggung().setTelpon_rumah(f_validasi.convert_karakter(telp_rmh_ttg0));	
		
		String telp_rmh_ttg02=edit.getTertanggung().getTelpon_rumah2();
		if (telp_rmh_ttg02==null)
		{
			telp_rmh_ttg02="";
		}
		String hasil_telp_rmh_ttg02 = a.telpon_rmh_ttg2(telp_rmh_ttg02);
		if (hasil_telp_rmh_ttg02.trim().length()!=0)
		{
			edit.getTertanggung().setTelpon_rumah2(f_validasi.convert_karakter(telp_rmh_ttg02));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("tertanggung.telpon_rumah2","","HALAMAN TERTANGGUNG :" +hasil_telp_rmh_ttg02);
			}
		}
		edit.getTertanggung().setTelpon_rumah2(f_validasi.convert_karakter(telp_rmh_ttg02));			

		//hp ttg
		String hp_ttg=edit.getTertanggung().getNo_hp();
		if (hp_ttg==null)
		{
			hp_ttg="";
		}
		
		if (telp_rmh_ttg0.equalsIgnoreCase("-") && hp_ttg.equalsIgnoreCase(""))
		{
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("tertanggung.no_hp","","HALAMAN TERTANGGUNG : Jika telpon rumah tidak ada, harap isi handphone.");
			}
		}		
		
		String hasil_hp_ttg = a.handphone_ttg1(hp_ttg);
		if (hasil_hp_ttg.trim().length()!=0)
		{
			edit.getTertanggung().setNo_hp(f_validasi.convert_karakter(hp_ttg));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("tertanggung.no_hp","","HALAMAN TERTANGGUNG :" +hasil_hp_ttg);
			}
		}
		edit.getTertanggung().setNo_hp(f_validasi.convert_karakter(hp_ttg));
		
		String hp_ttg2=edit.getTertanggung().getNo_hp2();
		if (hp_ttg2==null)
		{
			hp_ttg2="";
		}
		String hasil_hp_ttg2 = a.handphone_ttg2(hp_ttg2);
		if (hasil_hp_ttg2.trim().length()!=0)
		{
			edit.getTertanggung().setNo_hp2(f_validasi.convert_karakter(hp_ttg2));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("tertanggung.no_hp2","","HALAMAN TERTANGGUNG :" +hasil_hp_ttg2);
			}
		}
		edit.getTertanggung().setNo_hp2(f_validasi.convert_karakter(hp_ttg2));		
		
		//email ttg
		String email_ttg=edit.getTertanggung().getEmail();
		
		if(email_ttg==null)
		{
			email_ttg="";
		}
		String hasil_email_ttg = a.email_ttg(email_ttg);

		if (hasil_email_ttg.trim().length()!=0)
		{
			edit.getTertanggung().setEmail(f_validasi.convert_karakter(email_ttg));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("tertanggung.email","","HALAMAN TERTANGGUNG :" +hasil_email_ttg);
			}
		}
		edit.getTertanggung().setEmail(f_validasi.convert_karakter(email_ttg));
		
		//alamat_kantor_ttg		
		String almt_knt_ttg=edit.getTertanggung().getAlamat_kantor();
		if (almt_knt_ttg==null)
		{
			almt_knt_ttg="";
		}
		edit.getTertanggung().setAlamat_kantor(f_validasi.convert_karakter(almt_knt_ttg));
		
		//kota_kantor_ttg		
		String kota_knt_ttg=edit.getTertanggung().getKota_kantor();
		String hasil_kota_knt_ttg="";
		if (kota_knt_ttg==null)
		{
			kota_knt_ttg = "";
			edit.getTertanggung().setKota_kantor("");
			kota_knt_ttg=edit.getTertanggung().getKota_kantor();
		}
		
		if (almt_knt_ttg.trim().length()!=0)
		{
			hasil_kota_knt_ttg=a.kota_kantor_ttg(kota_knt_ttg);
			if(hasil_kota_knt_ttg.trim().length()!=0)
			{
				edit.getTertanggung().setKota_kantor(f_validasi.convert_karakter(kota_knt_ttg));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("tertanggung.kota_kantor","","HALAMAN TERTANGGUNG :" +hasil_kota_knt_ttg);
				}
			}
		}
		edit.getTertanggung().setKota_kantor(f_validasi.convert_karakter(kota_knt_ttg));

		//kode_pos_kantor_ttg		
		String kd_pos_knt_ttg=edit.getTertanggung().getKd_pos_kantor();
		if (kd_pos_knt_ttg==null)
		{
			kd_pos_knt_ttg="";
		}
		String hasil_kd_pos_knt_ttg = a.kode_pos_ktr_ttg(kd_pos_knt_ttg);
		if (hasil_kd_pos_knt_ttg.trim().length()!=0)
		{
			edit.getTertanggung().setKd_pos_kantor(f_validasi.convert_karakter(kd_pos_knt_ttg));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("tertanggung.kd_pos_kantor","","HALAMAN TERTANGGUNG :" +hasil_kd_pos_knt_ttg);
			}
		}
		edit.getTertanggung().setKd_pos_kantor(f_validasi.convert_karakter(kd_pos_knt_ttg));
				
		//area_kantor_ttg
		String kd_area_knt_ttg=edit.getTertanggung().getArea_code_kantor();
		if (kd_area_knt_ttg==null)
		{
			kd_area_knt_ttg="";
		}
		String hasil_kd_area_knt_ttg = a.kode_area_ktr_ttg(kd_area_knt_ttg);
		if (hasil_kd_area_knt_ttg.trim().length()!=0)
		{
			edit.getTertanggung().setArea_code_kantor(f_validasi.convert_karakter(kd_area_knt_ttg));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("tertanggung.area_code_kantor","","HALAMAN TERTANGGUNG :" +hasil_kd_area_knt_ttg);
			}
		}
		edit.getTertanggung().setArea_code_kantor(f_validasi.convert_karakter(kd_area_knt_ttg));
		
		String kd_area_knt_ttg2=edit.getTertanggung().getArea_code_kantor2();
		if (kd_area_knt_ttg2==null)
		{
			kd_area_knt_ttg2="";
		}
		String hasil_kd_area_knt_ttg2 = a.kode_area_ktr_ttg(kd_area_knt_ttg2);
		if (hasil_kd_area_knt_ttg2.trim().length()!=0)
		{
			edit.getTertanggung().setArea_code_kantor2(f_validasi.convert_karakter(kd_area_knt_ttg2));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("tertanggung.area_code_kantor2","","HALAMAN TERTANGGUNG :" +hasil_kd_area_knt_ttg2);
			}
		}
		edit.getTertanggung().setArea_code_kantor2(f_validasi.convert_karakter(kd_area_knt_ttg2));		

		//telpon_kantor_ttg
		String telp_knt_ttg=edit.getTertanggung().getTelpon_kantor();
		if (telp_knt_ttg==null)
		{
			telp_knt_ttg="";
		}
		String hasil_telp_knt_ttg = a.telpon_ktr_ttg(telp_knt_ttg);
		if (hasil_telp_knt_ttg.trim().length()!=0)
		{
			edit.getTertanggung().setTelpon_kantor(f_validasi.convert_karakter(telp_knt_ttg));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("tertanggung.telpon_kantor","","HALAMAN TERTANGGUNG :" +hasil_telp_knt_ttg);
			}
		}
		edit.getTertanggung().setTelpon_kantor(f_validasi.convert_karakter(telp_knt_ttg));	
		
		String telp_knt_ttg2=edit.getTertanggung().getTelpon_kantor2();
		if (telp_knt_ttg2==null)
		{
			telp_knt_ttg2="";
		}
		String hasil_telp_knt_ttg2 = a.telpon_ktr_ttg(telp_knt_ttg2);
		if (hasil_telp_knt_ttg2.trim().length()!=0)
		{
			edit.getTertanggung().setTelpon_kantor2(f_validasi.convert_karakter(telp_knt_ttg2));
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("tertanggung.telpon_kantor2","","HALAMAN TERTANGGUNG :" +hasil_telp_knt_ttg2);
			}
		}
		edit.getTertanggung().setTelpon_kantor2(f_validasi.convert_karakter(telp_knt_ttg2));			
		
		//tujuan membeli asuransi
		String tujuan_asr_ttg= edit.getTertanggung().getMkl_tujuan();
		String tujuan_asr_oth_ttg=  edit.getTertanggung().getTujuana();
		if (tujuan_asr_oth_ttg==null)
		{
			tujuan_asr_oth_ttg="";
		}
		if (tujuan_asr_ttg==null)
		{
			tujuan_asr_ttg="Lain - Lain";
			edit.getTertanggung().setMkl_tujuan("Lain - Lain");
		}
		String hasil_tujuan_asr_ttg="";	
		if (tujuan_asr_ttg.trim().toUpperCase().equalsIgnoreCase("LAIN - LAIN"))
		{
			hasil_tujuan_asr_ttg=a.cek_tujuan_asuransi(tujuan_asr_oth_ttg);
			if (hasil_tujuan_asr_ttg.trim().length()!=0)
			{
				edit.getTertanggung().setTujuana(f_validasi.convert_karakter(tujuan_asr_oth_ttg));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("tertanggung.tujuana","","HALAMAN TERTANGGUNG :" +hasil_tujuan_asr_ttg);
				}
			}
			edit.getTertanggung().setTujuana(f_validasi.convert_karakter(tujuan_asr_oth_ttg));
		}else{
			edit.getTertanggung().setTujuana("");
		}
		
		//sumber pendanaan pembelian asuransi
		String sumber_dana_ttg= edit.getTertanggung().getMkl_pendanaan();
		String sumber_dana_oth_ttg= edit.getTertanggung().getDanaa();
		String hasil_sumber_dana_ttg="";
		if (sumber_dana_oth_ttg==null)
		{
			sumber_dana_oth_ttg="";
		}
		if (sumber_dana_ttg==null)
		{
			sumber_dana_ttg="Lainnya";
			edit.getTertanggung().setMkl_pendanaan("Lainnya");
		}
		if (sumber_dana_ttg.trim().toUpperCase().equalsIgnoreCase("LAINNYA"))
		{
			hasil_sumber_dana_ttg=a.cek_sumber_dana(sumber_dana_oth_ttg);
			if (hasil_sumber_dana_ttg.trim().length()!=0)
			{
				edit.getTertanggung().setDanaa(f_validasi.convert_karakter(sumber_dana_oth_ttg));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("tertanggung.danaa","","HALAMAN TERTANGGUNG :" +hasil_sumber_dana_ttg);
				}
			}
			edit.getTertanggung().setDanaa(f_validasi.convert_karakter(sumber_dana_oth_ttg));
		}else{
			edit.getTertanggung().setDanaa(f_validasi.convert_karakter(""));
		}

		//sumber penghasilan
		String sumber_penghasilan = edit.getTertanggung().getMkl_smbr_penghasilan();
		String sumber_penghasilan_oth = edit.getTertanggung().getDanaa2();
		String hasil_sumber_penghasilan = "";
		if (sumber_penghasilan==null)
		{
			sumber_penghasilan="Lainnya";
		}
		if (sumber_penghasilan.trim().toUpperCase().equalsIgnoreCase("LAINNYA"))
		{
			hasil_sumber_penghasilan = a.cek_sumber_penghasilan(sumber_penghasilan_oth);
			if (hasil_sumber_penghasilan.trim().length()!=0)
			{
				edit.getTertanggung().setDanaa2(f_validasi.convert_karakter(sumber_penghasilan_oth));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("tertanggung.danaa2","","HALAMAN TERTANGGUNG :" +hasil_sumber_penghasilan);
				}
			}
			edit.getTertanggung().setDanaa2(f_validasi.convert_karakter(sumber_penghasilan_oth));
		}else{
			edit.getTertanggung().setDanaa2("");
		}

		
		//Klasifikasi Pekerjaan
		String pekerjaan_ttg= edit.getTertanggung().getMkl_kerja();
		String pekerjaan_oth_ttg= edit.getTertanggung().getKerjaa();
		String hasil_pekerjaan_ttg="";
		if (pekerjaan_oth_ttg==null)
		{
			pekerjaan_oth_ttg="";
		}
		if (pekerjaan_ttg==null)
		{
			pekerjaan_ttg="Lainnya";
			edit.getTertanggung().setMkl_kerja("Lainnya");
		}
		if (pekerjaan_ttg.trim().toUpperCase().equalsIgnoreCase("LAINNYA"))
		{
			hasil_pekerjaan_ttg=a.cek_pekerjaan(pekerjaan_oth_ttg);
			if (hasil_pekerjaan_ttg.trim().length()!=0)
			{
				edit.getTertanggung().setKerjaa(f_validasi.convert_karakter(pekerjaan_oth_ttg));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("tertanggung.kerjaa","","HALAMAN TERTANGGUNG :" +hasil_pekerjaan_ttg);
				}
			}
			edit.getTertanggung().setKerjaa(f_validasi.convert_karakter(pekerjaan_oth_ttg));
		}else{
			edit.getTertanggung().setKerjaa(f_validasi.convert_karakter(""));
		}

		String jabatan_ttg=edit.getTertanggung().getKerjab();
		
		String hasil_jabatan_ttg="";
		if (pekerjaan_ttg.trim().equalsIgnoreCase("Karyawan"))
		{
			hasil_jabatan_ttg=a.cek_jabatan(jabatan_ttg);
			if (hasil_jabatan_ttg.trim().length()!=0)
			{
				edit.getTertanggung().setKerjab(f_validasi.convert_karakter(jabatan_ttg));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("tertanggung.kerjab","","HALAMAN TERTANGGUNG :" +hasil_jabatan_ttg);
				}
			}
			edit.getTertanggung().setKerjab(f_validasi.convert_karakter(jabatan_ttg));
		}else{
			edit.getTertanggung().setKerjab("");
		}
		
		//klasifikasi bidang industri
		String bidang_ttg= edit.getTertanggung().getMkl_industri();
		String bidang_oth_ttg= edit.getTertanggung().getIndustria();
		String hasil_bidang_ttg="";
		if (bidang_oth_ttg==null)
		{
			bidang_oth_ttg="";
		}
		if (bidang_ttg==null)
		{
			bidang_ttg = "Lainnya";
			edit.getTertanggung().setMkl_industri("Lainnya");
		}
		if (bidang_ttg.trim().toUpperCase().equalsIgnoreCase("LAINNYA"))
		{
			hasil_bidang_ttg=a.cek_bidang(bidang_oth_ttg);
			if (hasil_bidang_ttg.trim().length()!=0)
			{
				edit.getTertanggung().setIndustria(f_validasi.convert_karakter(bidang_oth_ttg));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					err.rejectValue("tertanggung.industria","","HALAMAN TERTANGGUNG :" +hasil_bidang_ttg);
				}
			}
			edit.getTertanggung().setIndustria(f_validasi.convert_karakter(bidang_oth_ttg));
		}else{
			edit.getTertanggung().setIndustria("");
		}

		//umur beasiswa
		Integer beasiswa=edit.getTertanggung().getMspo_umur_beasiswa();
		if (beasiswa==null)
		{
			beasiswa=new Integer(0);
		}
		String hasil_beasiswa = a.beasiswa_ttg(beasiswa.toString());
		if (hasil_beasiswa.trim().length()!=0)
		{
			edit.getTertanggung().setMspo_umur_beasiswa(beasiswa);
			err.rejectValue("tertanggung.mspo_umur_beasiswa","","HALAMAN TERTANGGUNG :" +hasil_beasiswa);
		}
		
		Date tgl_begin_polis = edit.getDatausulan().getMste_beg_date();
		if (tgl_begin_polis==null)
		{
			Calendar tgl_sekarang = Calendar.getInstance(); 
			String  v_strInputDate = FormatString.rpad("0",Integer.toString(tgl_sekarang.get(Calendar.DATE)),2);
			v_strInputDate = v_strInputDate.concat("/");
			v_strInputDate = v_strInputDate.concat(FormatString.rpad("0",Integer.toString(tgl_sekarang.get(Calendar.MONTH)+1),2));
			v_strInputDate = v_strInputDate.concat("/");
			v_strInputDate = v_strInputDate.concat(Integer.toString(tgl_sekarang.get(Calendar.YEAR)));	
			String v_strDateNow = v_strInputDate;
			tgl_begin_polis = defaultDateFormat.parse(v_strDateNow);
			edit.getDatausulan().setMste_beg_date(tgl_begin_polis);
		}
		
		String spaj = edit.getPemegang().getReg_spaj();
		if (spaj == null)
		{
			spaj = "";
		}
		if (!spaj.equalsIgnoreCase(""))
		{
			//cek simultan ttg, kalau sudah pernah simultan tidak bisa diedit
			Integer jumlah_simultan_ttg = (Integer)this.elionsManager.count_simultan(edit.getTertanggung().getMcl_id());
			if (jumlah_simultan_ttg == null)
			{
				jumlah_simultan_ttg = new Integer(0);
			}
			if (jumlah_simultan_ttg.intValue() > 0)
			{
				if (flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
				String mcl_id = edit.getTertanggung().getMcl_id();
				String mcl_first = edit.getTertanggung().getMcl_first();
				Date tgl_lahir = edit.getTertanggung().getMspe_date_birth();
				Integer tanggal = tgl_lahir.getDate();
				Integer bulan = tgl_lahir.getMonth()+1;
				Integer tahun = tgl_lahir.getYear()+1900;
				String mspe_date_birth = Integer.toString(tahun) + FormatString.rpad("0",Integer.toString(bulan),2)+FormatString.rpad("0",Integer.toString(tanggal),2);
				Integer jumlah_client_simultan = (Integer) this.elionsManager.count_client_simultan(mcl_id, mcl_first, mspe_date_birth);
				if (jumlah_client_simultan==null)
				{
					jumlah_client_simultan=new Integer(0);
				}
				if (jumlah_client_simultan.intValue() == 0)
				{
					err.rejectValue("tertanggung.mcl_first","","Spaj ini sudah simultan, data tertanggung tidak boleh diedit.");
				}
				}
			}
		}
		Integer jumlah_r =new Integer(edit.getDatausulan().getDaftaRider().size());
		if (jumlah_r==null)
		{
			jumlah_r=new Integer(0);
		}
		
		Integer jmlrider=jumlah_r;
		edit.getDatausulan().setJmlrider(new Integer(jmlrider.intValue()));
		
		
	} catch (ParseException e) {
		logger.error("ERROR :", e);
	}
}

	//-------------------------data usulan ----------------------------------------
	public void validateddu(Object cmd, Errors err)  throws ServletException,IOException,Exception
	{
		try {
			logger.debug("EditBacValidator : validate page validateddu");
			Integer hasil=new Integer(0);
			Boolean cek_pmode=new Boolean(false);
			Boolean cek_kurs_up=new Boolean(false);
			Boolean cek_kurs_premi = new Boolean(false);
			String hasil_kurs_up="";
			String hasil_cek_pmode="";
			Integer tanggal=new Integer(0);
			Integer bulan=new Integer(0);
			Integer tahun=new Integer(0);
									
			Integer li_umur_ttg=new Integer(0);
			Integer li_umur_pp=new Integer(0);
			Integer li_umur_pic=new Integer(0);
			String hasil_kurs_premi="";
			Double min_up=new Double(0);
			String hasil_min_up="";
			String hasil_premi="";
			Integer umur_beasiswa=null;
			String hasil_beasiswa="";
			Integer ins_period=new Integer(0);
			Integer pay_period=new Integer(0);
			String hasil_up="";
			String kurs="";
			String hasil_cek_usia="";
			String tgl_end="";
			String bln_end="";
			String thn_end="";
			Integer tanggal1=new Integer(0);
			Integer bulan1=new Integer(0);
			Integer tahun1=new Integer(0);
			Integer tanggal2=new Integer(0);
			Integer bulan2=new Integer(0);
			Integer tahun2=new Integer(0);	
			Integer tanggal3=new Integer(0);
			Integer bulan3=new Integer(0);
			Integer tahun3=new Integer(0);	
			Integer tanggal4=new Integer(0);
			Integer bulan4=new Integer(0);
			Integer tahun4=new Integer(0);	
			Integer ii_class=new Integer(0);
			Integer klas= new Integer(0);
			String relation_ttg="";
			String hasil_klas="";		
			Integer age=new Integer(0);
			Integer jmlh_rider=new Integer(0);
			Double total_premi_sementara=new Double(0);
			Double rate_plan=new Double(0);
			Integer flag_account=new Integer(0);
			Integer flag_powersave = new Integer(0);
			Boolean isBungaSimponi=new Boolean(false);
			Boolean isBonusTahapan=new Boolean(false);
			Integer flag_as=new Integer(0);
			Integer faktor=new Integer(0);			
			Boolean flag_bao=new Boolean(false);
			String hasil_beg_date="";
			Integer flag_worksite = new Integer(0);
			Integer flag_endowment = new Integer(0);
			Integer flag_bao1=new Integer(0);
			Cmdeditbac edit= (Cmdeditbac) cmd;
			f_validasi data = new f_validasi();
			NumberFormat f = new DecimalFormat("#,##0.00;(#,##0.00)");
			form_data_usulan a =new form_data_usulan();
			a.setProducts(products);
			Integer kode_flag = new Integer(0);
			Integer flag_rider = new Integer(0);
			Integer pmode_id = null;
			Double li_pct_biaya = new Double(0); 
			//cek nama bank autodebet kalau sudah pernah diisi
			String bank_pp=edit.getAccount_recur().getLbn_id();	
			String nama_bank_autodebet="";
			Integer flag_ekalink = new Integer(0);
			String hasil_rider = "";
			Integer flag_hcp = new Integer(0);	
			edit.getDatausulan().setFlag_hcp(new Integer(0));
			Integer number_utama=new Integer(0);
			String status = edit.getPemegang().getStatus();
			Integer flag_karyawan = edit.getDatausulan().getMste_flag_el();
			if (flag_karyawan==null)
			{
				flag_karyawan= new Integer(0);
				edit.getDatausulan().setMste_flag_el(flag_karyawan);
			}
			String reg_spaj = edit.getDatausulan().getReg_spaj();
			
			if (status==null)
			{
				status= "input";
			}
			Integer flag_rider_hcp = edit.getDatausulan().getFlag_rider_hcp();
			if (flag_rider_hcp == null)
			{
				flag_rider_hcp = new Integer(0);
			}
			
			Integer alertEkaSehat = edit.getDatausulan().getAlertEkaSehat();
			if(alertEkaSehat == null){
				alertEkaSehat = new Integer(0);
			}
			
			Integer cek_flag_hcp = edit.getDatausulan().getCek_flag_hcp();
			if (cek_flag_hcp == null)
			{
				cek_flag_hcp = new Integer(0);
			}
			
			if(bank_pp!=null)
			{
				Map data1= (HashMap) this.elionsManager.select_bank2(bank_pp);

				if (data1!=null)
				{		
					nama_bank_autodebet = (String)data1.get("BANK_NAMA");
				}
			}else{
				bank_pp="";
			}
			
			//cek nama bank rek client kalau sudah pernah diisi
			edit.getAccount_recur().setLbn_nama(nama_bank_autodebet);
			String bank_pp1=edit.getRekening_client().getLsbp_id();
			String nama_bank_rekclient="";
			if(bank_pp1!=null)
			{
				Map data1= (HashMap) this.elionsManager.select_bank1(bank_pp1);

				if (data1!=null)
				{		
					nama_bank_rekclient = (String)data1.get("BANK_NAMA");
				}
			}else{
				bank_pp1="";
			}
			edit.getRekening_client().setLsbp_nama(nama_bank_rekclient);
			
			//cek nama perusahaan kalau sudah dipilih
			String kode_perusahaan = edit.getPemegang().getMspo_customer();
			String nama_perusahaan="";
			if(kode_perusahaan!=null)
			{
				Map data1= (HashMap) this.elionsManager.select_namacompany(kode_perusahaan);
				if (data1!=null)
				{		
					nama_perusahaan = (String)data1.get("COMPANY_NAMA");
				}
			}else{
				kode_perusahaan="";
			}
			edit.getPemegang().setMspo_customer_nama(nama_perusahaan);
			
			// cek cabang dari user id
			String lca_id = edit.getPemegang().getCbg_lus_id();
			if (lca_id.trim().length()==1)
			{
				lca_id="0"+lca_id;
			}
			edit.getPemegang().setLca_id(lca_id);
			
			if (edit.getDatausulan().getTipeproduk()==null)
			{
				edit.getDatausulan().setTipeproduk(new Integer(1));
			}
			
			Integer flag_special = edit.getPowersave().getFlag_special();
			if (status.equalsIgnoreCase("edit"))
			{
				flag_special = uwManager.getFlagSpecial(edit.getDatausulan().getReg_spaj());
			}
			if(flag_special==null){
				flag_special= new Integer(0);
			}
			edit.getPowersave().setFlag_special(flag_special);
			
			//plan
			String bisnis=edit.getDatausulan().getPlan();
			if (bisnis==null)
			{
				bisnis="0~X0";
			}
			String hasil_bisnis = a.bisnis(bisnis);
			if(hasil_bisnis.trim().length()!=0)
			{
				err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN :" +hasil_bisnis);
			}
	
	
			int letak=0;
			letak=bisnis.indexOf("X");
			Integer kode_produk=null;
			Integer number_produk=null;
			kode_produk=new Integer(Integer.parseInt(bisnis.substring(0,letak-1)));
			number_produk=new Integer(Integer.parseInt(bisnis.substring(letak+1,bisnis.length())));
			edit.getDatausulan().setLsbs_id(kode_produk);
			edit.getDatausulan().setLsdbs_number(number_produk);
	
			Date tgl_beg_date = edit.getDatausulan().getMste_beg_date();
			
			String kurs_up=edit.getDatausulan().getLku_id();
			if (kurs_up==null)
			{
				kurs_up="";
			}
			String kurs_premi=edit.getDatausulan().getKurs_p();
			if (kurs_premi==null)
			{
				kurs_premi="";
			}
			Double up=edit.getDatausulan().getMspr_tsi();
			Double premi=edit.getDatausulan().getMspr_premium();
			Double diskon_karyawan = 1.0;
			if (up==null)
			{
				up=new Double(0);
			}
			edit.getDatausulan().setMspr_tsi(up);
			if (premi==null)
			{
				premi= new Double(0);
			}
			edit.getDatausulan().setMspr_premium(premi);
			
			String nama_produk="";
			if(hasil_bisnis.trim().length()==0)
			{
				if (Integer.toString(kode_produk.intValue()).trim().length()==1)
				{
					nama_produk="produk_asuransi.n_prod_0"+kode_produk;	
				}else{
					nama_produk="produk_asuransi.n_prod_"+kode_produk;	
				}
	
				try{
					Class aClass = Class.forName( nama_produk );
					n_prod produk = (n_prod)aClass.newInstance();
					produk.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
					produk.cek_flag_agen(kode_produk.intValue(),number_produk.intValue(), edit.getPowersave().getFlag_bulanan());
					
					Integer flag_horison = new Integer(produk.flag_horison);
					edit.getDatausulan().setFlag_horison(flag_horison);
					
					Integer flag_simas = new Integer(produk.simas);
					edit.getDatausulan().setFlag_simas(flag_simas);
					
					int flag_platinum = produk.flag_platinumlink;
					int flag_cutipremi = produk.flag_cuti_premi; 
					//kode produk
					produk.of_set_bisnis_no(number_produk.intValue());
					produk.ii_bisnis_no=(number_produk.intValue());
					produk.ii_bisnis_id=(kode_produk.intValue());
					flag_ekalink = produk.flag_ekalink;
					if(edit.getPemegang().getLsre_id()==1){
						produk.samePPTT=1;
					}else{
						produk.samePPTT=0;
					}
					edit.getDatausulan().setFlag_ekalink(flag_ekalink);
					
					Integer flag_bulanan = new Integer(produk.flag_powersavebulanan);
					edit.getDatausulan().setFlag_bulanan(flag_bulanan);
					
					produk.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
					flag_powersave = new Integer(produk.of_get_bisnis_no(edit.getPowersave().getFlag_bulanan()));
					if (produk.flag_powersavebulanan == 1)
					{
						produk.cek_flag_agen(kode_produk.intValue(), number_produk.intValue(), edit.getPowersave().getFlag_bulanan());
						flag_powersave = new Integer(produk.flag_powersave);
					}
					edit.getPowersave().setFlag_powersave(flag_powersave);
					

					//flag
					flag_account=new Integer(produk.flag_account);
					
					isBungaSimponi=new Boolean(produk.isBungaSimponi);
					flag_worksite = new Integer(produk.flag_worksite);
					flag_endowment = new Integer(produk.flag_endowment);
					
					Integer flag_bungasimponi=new Integer(0);
					if (isBungaSimponi.booleanValue()==true)
					{
						flag_bungasimponi=new Integer(1);
					}else{
						flag_bungasimponi=new Integer(0);
					}
						
					isBonusTahapan=new Boolean(produk.isBonusTahapan);
					Integer flag_bonustahapan = new Integer(0);
					if (isBonusTahapan.booleanValue()==true)
					{
						flag_bonustahapan=new Integer(1);
					}else{
						flag_bonustahapan=new Integer(0);
					}
					flag_as=new Integer(produk.flag_as);
					edit.getDatausulan().setFlag_as(flag_as);
					edit.getDatausulan().setIsBonusTahapan(flag_bonustahapan);
					edit.getDatausulan().setIsBungaSimponi(flag_bungasimponi);
					edit.getDatausulan().setFlag_worksite(flag_worksite);
					edit.getDatausulan().setFlag_endowment(flag_endowment);
					
					klas=new Integer(0);
					ii_class=new Integer(0);
					edit.getDatausulan().setMspr_class(klas);
					produk.ii_class = klas.intValue();
					
					Date tgl_spaj = edit.getPemegang().getMspo_spaj_date();
					if (tgl_spaj==null)
					{
						Calendar tgl_sekarang = Calendar.getInstance(); 
						String  v_strInputDate = FormatString.rpad("0",Integer.toString(tgl_sekarang.get(Calendar.DATE)),2);
						v_strInputDate = v_strInputDate.concat("/");
						v_strInputDate = v_strInputDate.concat(FormatString.rpad("0",Integer.toString(tgl_sekarang.get(Calendar.MONTH)+1),2));
						v_strInputDate = v_strInputDate.concat("/");
						v_strInputDate = v_strInputDate.concat(Integer.toString(tgl_sekarang.get(Calendar.YEAR)));	
						String v_strDateNow = v_strInputDate;
						tgl_spaj = defaultDateFormat.parse(v_strDateNow);
						edit.getPemegang().setMspo_spaj_date(tgl_spaj);
					}
					
					Date tgl_begin_polis = edit.getDatausulan().getMste_beg_date();
					if (tgl_begin_polis==null)
					{
						Calendar tgl_sekarang = Calendar.getInstance(); 
						String  v_strInputDate = FormatString.rpad("0",Integer.toString(tgl_sekarang.get(Calendar.DATE)),2);
						v_strInputDate = v_strInputDate.concat("/");
						v_strInputDate = v_strInputDate.concat(FormatString.rpad("0",Integer.toString(tgl_sekarang.get(Calendar.MONTH)+1),2));
						v_strInputDate = v_strInputDate.concat("/");
						v_strInputDate = v_strInputDate.concat(Integer.toString(tgl_sekarang.get(Calendar.YEAR)));	
						String v_strDateNow = v_strInputDate;
						
						tgl_begin_polis = defaultDateFormat.parse(v_strDateNow);
						edit.getDatausulan().setMste_beg_date(tgl_begin_polis);
					}
					
					//yusuf - stable link
					//bila konversi psave ke slink, begdatenya <> tgl begdate polis, melainkan memakai tgl ro terbaru
					//yg sudah di set di edit bac controller
					if(products.stableLink(String.valueOf(produk.ii_bisnis_id)) && !edit.getDatausulan().isPsave) {
						edit.getPowersave().setBegdate_topup(tgl_begin_polis);
					}//Deddy - stable save
					 // sama dengan kondisi di atas
					else if(products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no) && !edit.getDatausulan().isPsave){
						edit.getDatausulan().setConvert(2);
						edit.getPowersave().setBegdate_topup(tgl_begin_polis);
					}
					if( edit.getDatausulan().getFlag_as()==2 && (produk.ii_bisnis_id==183 || produk.ii_bisnis_id==189 || produk.ii_bisnis_id==193 || produk.ii_bisnis_id==195)){
						diskon_karyawan=0.7;
					}
					
					//end date polis	
					//tgl beg date
					tanggal1= edit.getDatausulan().getMste_beg_date().getDate();
					bulan1 = (edit.getDatausulan().getMste_beg_date().getMonth())+1;
					tahun1 = (edit.getDatausulan().getMste_beg_date().getYear())+1900;
					
					//tgl lahir ttg
					tanggal2=edit.getTertanggung().getMspe_date_birth().getDate();
					bulan2=(edit.getTertanggung().getMspe_date_birth().getMonth())+1;
					tahun2=(edit.getTertanggung().getMspe_date_birth().getYear())+1900;
	
					//tgl lahir pp
					tanggal3=edit.getPemegang().getMspe_date_birth().getDate();
					bulan3=(edit.getPemegang().getMspe_date_birth().getMonth())+1;
					tahun3=(edit.getPemegang().getMspe_date_birth().getYear())+1900;
					
					//tgl lahir pic
					if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
						tanggal4=edit.getContactPerson().getDate_birth().getDate();
						bulan4=(edit.getContactPerson().getDate_birth().getMonth())+1;
						tahun4=(edit.getContactPerson().getDate_birth().getYear())+1900;
					}
									
					//hit umur ttg, pp, pic
					f_hit_umur umr= new f_hit_umur();
					li_umur_ttg =  umr.umur(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);
					li_umur_pp = umr.umur(tahun3,bulan3,tanggal3,tahun1,bulan1,tanggal1);
					if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
						li_umur_pic = umr.umur(tahun4,bulan4,tanggal4,tahun1,bulan1,tanggal1);
					}
					if ( produk.usia_nol == 1)
					{
						if (li_umur_ttg.intValue() == 0 )
						{
							li_umur_ttg = 1;
						}
					}
					
					//set umur ttg dan pp ke n_prod
					produk.of_set_usia_tt(li_umur_ttg.intValue());
					produk.of_set_usia_pp(li_umur_pp.intValue());
					
					edit.getPemegang().setMste_age(li_umur_pp);
					edit.getTertanggung().setMste_age(li_umur_ttg);
					if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
						edit.getContactPerson().setMste_age(li_umur_pic);
					}
					
					edit.getDatausulan().setFlag_rider(new Integer(produk.flag_rider));
					flag_rider = new Integer(produk.flag_rider);
					//cek usia
					produk.of_set_usia_tt(li_umur_ttg.intValue());
					produk.of_set_usia_pp(li_umur_pp.intValue());
					produk.of_set_age();
					
					age=new Integer(produk.ii_age);
					
					Integer jumlah_cancel = edit.getPemegang().getJumlah_cancel();
					Integer flag_gutri = edit.getDatausulan().getFlag_gutri();
					Integer mste_flag_guthrie = edit.getPemegang().getMste_flag_guthrie();
					
					if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
					{
						if(produk.ii_bisnis_id==164){
							produk.is_kurs_id = edit.getDatausulan().getKurs_premi();
							if (edit.getDatausulan().getTotal_premi_kombinasi() == null)
							{
								edit.getDatausulan().setTotal_premi_kombinasi(new Double(0));
								err.rejectValue("datausulan.total_premi_kombinasi","","HALAMAN DATA USULAN : Untuk cara premi tersebut , Silahkan masukkan total premi terlebih dahulu.");
							}
							hasil_cek_usia=produk.of_check_usia_case_premi(tahun2.intValue(),bulan2.intValue(),tanggal2.intValue(),tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),pay_period.intValue(),number_produk.intValue(), edit.getDatausulan().getTotal_premi_kombinasi());
						}else{
							hasil_cek_usia=produk.of_check_usia(tahun2.intValue(),bulan2.intValue(),tanggal2.intValue(),tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),pay_period.intValue(),number_produk.intValue());
						}
						if (hasil_cek_usia.trim().length()!=0)
						{
							err.rejectValue("datausulan.mste_beg_date","","HALAMAN DATA USULAN :" +hasil_cek_usia);
						}
					}
					
					//ryan - usia minimum PA Resiko A
					if(produk.ii_bisnis_no==8) {
						if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
						{
							if (li_umur_pp.intValue()<17)
							{
								err.rejectValue("datausulan.mste_beg_date","","HALAMAN DATA USULAN :Umur pemegang polis minimum 18 tahun");
							}
						}
					}
					else if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
					{
						if (li_umur_pp.intValue()<17)
						{
							err.rejectValue("datausulan.mste_beg_date","","HALAMAN DATA USULAN :Umur pemegang polis minimum 17 tahun");
										
						}
					}
					
					if(produk.ii_bisnis_id==177 || produk.ii_bisnis_id==207 || (produk.ii_bisnis_id==186 && produk.ii_bisnis_no!=3) ){
						ins_period = edit.getDatausulan().getMspr_ins_period();
						produk.ii_contract_period = ins_period;
					}else{
						ins_period = new Integer(produk.of_get_conperiod(number_produk.intValue()));
					}
					
					// cari tgl akhir masa berlaku polis
					produk.of_set_begdate(tahun1.intValue(),bulan1.intValue(),tanggal1.intValue());
					tanggal= new Integer(produk.idt_end_date.getTime().getDate());
					bulan= new Integer(produk.idt_end_date.getTime().getMonth()+1);
					tahun= new Integer(produk.idt_end_date.getTime().getYear()+1900);
					
					tgl_end=Integer.toString(tanggal.intValue());
					bln_end = Integer.toString(bulan.intValue());
					thn_end = Integer.toString(tahun.intValue());
					if ((tgl_end.equalsIgnoreCase("0")==true) || (bln_end.equalsIgnoreCase("0")==true) || (thn_end.equalsIgnoreCase("0")==true))
					{
						edit.getDatausulan().setMste_end_date(null);
					}else{
						String tanggal_end_date = FormatString.rpad("0",tgl_end,2)+"/"+FormatString.rpad("0",bln_end,2)+"/"+thn_end;
						edit.getDatausulan().setMste_end_date(defaultDateFormat.parse(tanggal_end_date));
					}
					edit.getDatausulan().setLi_umur_ttg((li_umur_ttg));
					edit.getDatausulan().setLi_umur_pp((li_umur_pp));
					edit.getPemegang().setUsiapp((li_umur_pp));
					edit.getTertanggung().setUsiattg((li_umur_ttg));
						
					Double persen = new Double(0);
					edit.getPemegang().setMste_pct_dplk(new Double(0));
					produk.cek_guthrie(mste_flag_guthrie);
					
					//cek kurs up dan premi 
					if (produk.flag_uppremi==0){
						kurs=kurs_up;
						for ( int m=0 ; m< produk.indeks_is_forex; m++ )
						{
							if (kurs_up.equalsIgnoreCase(produk.is_forex[m]))
							{
								cek_kurs_up=new Boolean(true);
							}
						}
						if (cek_kurs_up.booleanValue()==false)
						{
							hasil_kurs_up="Plan ini tidak bisa dengan kurs tersebut, Silahkan memilih dengan kurs up lain";
							hasil_kurs_premi="";
							err.rejectValue("datausulan.lku_id","","HALAMAN DATA USULAN :" +hasil_kurs_up);
						}
					}
							
					if (produk.flag_uppremi==1){
						kurs=kurs_premi;
						for ( int n=0 ; n< produk.indeks_is_forex; n++ )
						{
							if (kurs_premi.equalsIgnoreCase(produk.is_forex[n]))
							{
								cek_kurs_premi=new Boolean(true);
							}
						}
						if (cek_kurs_premi.booleanValue()==false)
						{
							hasil_kurs_premi="Plan ini tidak bisa dengan kurs tersebut, Silahkan memilih dengan kurs premi lain";
							hasil_kurs_up="";	
							err.rejectValue("datausulan.lku_id","","HALAMAN DATA USULAN :" +hasil_kurs_premi);	
						}
					}			
					
					f_hit_umur n_date=  new f_hit_umur();
					Date tgl_input = edit.getPemegang().getMspo_input_date();
					Integer t1 = tgl_input.getDate();
					Integer b1 = tgl_input.getMonth()+1;
					Integer th1 = tgl_input.getYear()+1900;
					Integer hari = n_date.hari1( 2007,12, 31,th1, b1, t1);
					
					//flag
					produk.of_set_kurs(kurs);
					edit.getDatausulan().setKurs_premi(kurs);
					edit.getDatausulan().setKurs_p(kurs);
					kode_flag=new Integer(produk.kode_flag);
					edit.getDatausulan().setKode_flag(kode_flag);
					flag_bao=new Boolean(produk.isProductBancass);
					
					if (flag_bao.booleanValue()==true)
					{
						flag_bao1=new Integer(1);
					}else{
						flag_bao1=new Integer(0);
					}
					
					Double total_premi_tu = new Double(0);
					edit.getDatausulan().setFlag_bao(flag_bao1);
					Integer cara_premi = edit.getDatausulan().getCara_premi();
					if (cara_premi == null)
					{
						cara_premi = new Integer(0);
					}
					pmode_id = edit.getDatausulan().getLscb_id();
					
					// untuk produk link
					if (kode_flag.intValue() > 1 )
					{
						if (cara_premi.intValue()==1)
						{
							String pil_kombinasi = edit.getDatausulan().getKombinasi();
							Double premi_pokok = new Double(0);
							Double premi_tp = new Double(0);
							Double totalpremi_kombinasi = edit.getDatausulan().getTotal_premi_kombinasi();
							//pilihan kombinasi persentase antara premi pokok dengan premi top up
							if (pil_kombinasi!=null)
							{
								if (totalpremi_kombinasi == null)
								{
									totalpremi_kombinasi =new Double(0);
									edit.getDatausulan().setTotal_premi_kombinasi(totalpremi_kombinasi);
									err.rejectValue("datausulan.total_premi_kombinasi","","HALAMAN DATA USULAN : Untuk cara premi tersebut , Silahkan masukkan total premi terlebih dahulu.");
								}//else{
								
								// khusus untuk stable link - revisi Yusuf (28/04/08)
								if (kode_flag.intValue() == 11 || kode_flag.intValue() == 15){
									//RUPIAH
									if (kurs.equalsIgnoreCase("01")){
										if(produk.ii_bisnis_id==164 || produk.ii_bisnis_id==174){
											if (totalpremi_kombinasi.doubleValue() > 20000000){
												premi_pokok = new Double(20000000);
												premi_tp = new Double(totalpremi_kombinasi.doubleValue() - 20000000);
											}else {
												premi_pokok = totalpremi_kombinasi;
											}
										}else if(produk.ii_bisnis_id==186){
											if(edit.getDatausulan().getLscb_id()==3){
												if (totalpremi_kombinasi.doubleValue() > 2000000){
													premi_pokok = new Double(2000000);
													premi_tp = new Double(totalpremi_kombinasi.doubleValue() - 2000000);
												}else {
													premi_pokok = totalpremi_kombinasi;
												}
											}else if(edit.getDatausulan().getLscb_id()==2){
												Double faktor_premi = new Double(2000000/2);
												if (totalpremi_kombinasi.doubleValue() > faktor_premi){
													premi_pokok = new Double(faktor_premi);
													premi_tp = new Double(totalpremi_kombinasi.doubleValue() - faktor_premi);
												}else {
													premi_pokok = totalpremi_kombinasi;
												}
											}else if(edit.getDatausulan().getLscb_id()==1){
												Double faktor_premi = new Double(2000000/4);
												if (totalpremi_kombinasi.doubleValue() > faktor_premi){
													premi_pokok = new Double(faktor_premi);
													premi_tp = new Double(totalpremi_kombinasi.doubleValue() - faktor_premi);
												}else {
													premi_pokok = totalpremi_kombinasi;
												}
											}else if(edit.getDatausulan().getLscb_id()==6){
												Double faktor_premi = new Double(2000000/12);
												if (totalpremi_kombinasi.doubleValue() > faktor_premi){
													premi_pokok = new Double(faktor_premi);
													premi_tp = new Double(totalpremi_kombinasi.doubleValue() - faktor_premi);
												}else {
													premi_pokok = totalpremi_kombinasi;
												}
											}
											
										}
										
									//DOLLAR
									}else{
										if(produk.ii_bisnis_id==164 || produk.ii_bisnis_id==174){
											if (totalpremi_kombinasi.doubleValue() > 2000){
												premi_pokok = new Double(2000);
												premi_tp = new Double(totalpremi_kombinasi.doubleValue() - 2000);
											}else {
												premi_pokok = totalpremi_kombinasi;
											}
										}else if(produk.ii_bisnis_id==186){
											if(edit.getDatausulan().getLscb_id()==3){
												if (totalpremi_kombinasi.doubleValue() > 200){
													premi_pokok = new Double(200);
													premi_tp = new Double(totalpremi_kombinasi.doubleValue() - 200);
												}else {
													premi_pokok = totalpremi_kombinasi;
												}
											}else if(edit.getDatausulan().getLscb_id()==2){
												Double faktor_premi = new Double(200/2);
												if (totalpremi_kombinasi.doubleValue() > 200){
													premi_pokok = new Double(faktor_premi);
													premi_tp = new Double(totalpremi_kombinasi.doubleValue() - faktor_premi);
												}else {
													premi_pokok = totalpremi_kombinasi;
												}
											}else if(edit.getDatausulan().getLscb_id()==1){
												Double faktor_premi = new Double(200/4);
												if (totalpremi_kombinasi.doubleValue() > 200){
													premi_pokok = new Double(faktor_premi);
													premi_tp = new Double(totalpremi_kombinasi.doubleValue() - faktor_premi);
												}else {
													premi_pokok = totalpremi_kombinasi;
												}
											}else if(edit.getDatausulan().getLscb_id()==6){
												Double faktor_premi = new Double(200/12);
												if (totalpremi_kombinasi.doubleValue() > 200){
													premi_pokok = new Double(faktor_premi);
													premi_tp = new Double(totalpremi_kombinasi.doubleValue() - faktor_premi);
												}else {
													premi_pokok = totalpremi_kombinasi;
												}
											}
											
											if (totalpremi_kombinasi.doubleValue() > 200){
												premi_pokok = new Double(200);
												premi_tp = new Double(totalpremi_kombinasi.doubleValue() - 200);
											}else {
												premi_pokok = totalpremi_kombinasi;
											}
										}
										
									}
									if(edit.getDatausulan().getLsbs_id()==186){
										edit.getDatausulan().setCara_premi(0);
									}else{
										edit.getDatausulan().setCara_premi(1);
									}
									
									edit.getDatausulan().setMspr_premium(premi_pokok);
									edit.getInvestasiutama().getDaftartopup().setPremi_berkala(0.);
									edit.getInvestasiutama().getDaftartopup().setPil_berkala(null);
									edit.getInvestasiutama().getDaftartopup().setPremi_tunggal(premi_tp);
									if(premi_tp > 0) edit.getInvestasiutama().getDaftartopup().setPil_tunggal(2);
									total_premi_tu = premi_tp;
									premi = premi_pokok;
									Double total_seluruhnya = new Double(premi_pokok.doubleValue()+ premi_tp.doubleValue());
									edit.getInvestasiutama().setTotal_premi_sementara(total_seluruhnya);
									edit.getDatausulan().setTotal_premi_sementara(total_seluruhnya);
									
								}else{
									//pengecekan kombinasi berapa saja yang diperbolehkan
									String hasil_kombinasi="";
									int indeks_kombinasi=produk.indeks_kombinasi;
									Integer ok = new Integer(0);
									for (int j=0 ; j <indeks_kombinasi; j++)
									{
										if (pil_kombinasi.equalsIgnoreCase(produk.kombinasi[j]))
										{
											ok = new Integer(1);
										}
									}
									if (ok.intValue() == 0)
									{
										hasil_kombinasi="Kombinasi premi tersebut tidak boleh untuk plan tersebut.";
										edit.getDatausulan().setTotal_premi_kombinasi(new Double(0));
										err.rejectValue("datausulan.kombinasi", "", "HALAMAN DATA USULAN : "+hasil_kombinasi);
									}
									
									if (pil_kombinasi.equalsIgnoreCase("A"))
									{
										premi_pokok = totalpremi_kombinasi;
									}else if  (pil_kombinasi.equalsIgnoreCase("B"))
										{
											premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 90 /100);
											premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 10 /100);
										}else if  (pil_kombinasi.equalsIgnoreCase("C"))
											{
												premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 80 /100);
												premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 20 /100);
											}else if  (pil_kombinasi.equalsIgnoreCase("D"))
												{
													premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 70 /100);
													premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 30 /100);
												}else  if  (pil_kombinasi.equalsIgnoreCase("E"))
													{
														premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 60 /100);
														premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 40 /100);
													}else if  (pil_kombinasi.equalsIgnoreCase("F"))
														{
															premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 50 /100);
															premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 50 /100);
														}else if  (pil_kombinasi.equalsIgnoreCase("G"))
															{
																premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 40 /100);
																premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 60 /100);
															}else if  (pil_kombinasi.equalsIgnoreCase("H"))
																{
																	premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 30 /100);
																	premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 70 /100);
																}else if  (pil_kombinasi.equalsIgnoreCase("I"))
																	{
																		premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 20 /100);
																		premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 80 /100);
																	}else if  (pil_kombinasi.equalsIgnoreCase("J"))
																		{
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 10 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 90 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("K"))
																		{
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 95 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 5 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("L"))
																		{
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 85 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 15 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("M"))
																		{
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 75 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 25 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("N"))
																		{
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 65 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 35 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("O"))
																		{
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 55 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 45 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("P"))
																		{
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 45 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 55 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("Q"))
																		{
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 35 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 65 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("R"))
																		{
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 25 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 85 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("S"))
																		{
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 15 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 85 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("T"))
																		{
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 5 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 95 /100);
																		}
										edit.getDatausulan().setMspr_premium(premi_pokok);
										edit.getInvestasiutama().getDaftartopup().setPremi_berkala(premi_tp);
										edit.getInvestasiutama().getDaftartopup().setPil_berkala(1);
										total_premi_tu = premi_tp;
										premi = premi_pokok;
										Double total_seluruhnya = new Double(premi_pokok.doubleValue()+ premi_tp.doubleValue());
										edit.getInvestasiutama().setTotal_premi_sementara(total_seluruhnya);
									}
							}
						}
					}else{
						if (cara_premi.intValue() == 1)
						{
							err.rejectValue("datausulan.cara_premi","","HALAMAN DATA USULAN : Produk ini bukan produk investasi ,Silahkan pilih cara premi yang hanya mengisi premi pokok .");
						}
						edit.getDatausulan().setTotal_premi_kombinasi(new Double(0));
						edit.getInvestasiutama().setTotal_premi_sementara(new Double(0));
						edit.getDatausulan().setKombinasi(null);
					}
					
					//tambahan Yusuf - cara premi untuk stable link harus isi total premi, bukan isi premi pokok saja
					if((products.stableLink(String.valueOf(produk.ii_bisnis_id)))) {
//							|| produk.ii_bisnis_id==186) && 1 != cara_premi) {
						if((products.stableLink(String.valueOf(produk.ii_bisnis_id)) && produk.ii_bisnis_id!=186 && 1 != cara_premi)){
							err.rejectValue("datausulan.cara_premi","","HALAMAN DATA USULAN : Khusus STABLE LINK, harus pilih ISI TOTAL PREMI DAN PILIH KOMBINASI (kombinasinya tidak perlu diisi).");
						}
					}
					
					if(produk.ii_bisnis_id==186 && 1==cara_premi){
						err.rejectValue("datausulan.cara_premi","","HALAMAN DATA USULAN : Untuk PROGRESSIVE LINK, harus pilih ISI PREMI.");
					}

					produk.ii_pmode=pmode_id.intValue();	
					
					if (produk.flag_mediplan == 1)
					{
						rate_plan=new Double(produk.of_get_rate());
						premi=new Double(produk.idec_premi_main);
						up = new Double(produk.idec_up);
						edit.getDatausulan().setMspr_tsi(up);
					}
					
					//up
					if (hasil_up.trim().length()==0)
					{
												
						if (produk.flag_uppremi==0 && produk.flag_default_up==0)
						{
							hasil_up="";
							//edit.getDatausulan().setMspr_tsi(up);
							//edit.getDatausulan().setMspr_premium(null);
							hasil_up=produk.of_alert_min_up(up.doubleValue());
							
							if (hasil_up.trim().length()!=0)
							{
								err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :" +hasil_up);
							}else{
								hasil_up=produk.of_alert_max_up(up.doubleValue());	
								if (hasil_up.trim().length()!=0)
								{
									err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :" +hasil_up);
								}else{
									hasil_up=produk.cek_kelipatan_up(up.doubleValue());	
									if (hasil_up.trim().length()!=0)
									{
										err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :" +hasil_up);
									}
								}	
							}

							produk.of_set_up(up.doubleValue());	
						}
						
						if(produk.flag_default_up==1){
							up = produk.set_default_up(up.doubleValue());
						}
				
					//premi
					if (hasil_premi.trim().length()==0)
					{
						if (produk.flag_uppremi==1)
						{
							hasil_premi="";
							//edit.getDatausulan().setMspr_tsi(null);
							//edit.getDatausulan().setMspr_premium(premi);
							if (mste_flag_guthrie == 1)
							{
								produk.idec_min_up01 = 50000;
							}
							
							if(edit.getDatausulan().getLsbs_id()==186 && edit.getDatausulan().getLsdbs_number()==5){//succesful family save
								hasil_premi=produk.of_alert_min_premi_With_flag_bulanan(premi.doubleValue(), flag_bulanan);
							}else if(edit.getDatausulan().getLsbs_id()==121){
								hasil_premi=produk.of_alert_min_premi(total_premi_tu!=null?premi.doubleValue()+total_premi_tu.doubleValue():premi.doubleValue());
							}else if(edit.getDatausulan().getLsbs_id()==141){
								hasil_premi=produk.of_alert_min_premi(up.doubleValue());
							}else{
								if(edit.getDatausulan().getLsbs_id()==164 && edit.getDatausulan().getMste_flag_el()==1){
									hasil_premi=produk.of_alert_min_premi_karyawan(premi.doubleValue());
								}else{
									if(edit.getInvestasiutama().getTotal_premi_sementara()!=null){
										if(edit.getInvestasiutama().getTotal_premi_sementara()>premi){
											hasil_premi=produk.of_alert_min_premi(edit.getInvestasiutama().getTotal_premi_sementara());
										}else{
											hasil_premi=produk.of_alert_min_premi(premi.doubleValue());
										}
									}else{
										hasil_premi=produk.of_alert_min_premi(premi.doubleValue());
									}
								}
							}
	
							if (hasil_premi.trim().length()!=0)
							{
								err.rejectValue("datausulan.mspr_premium","","HALAMAN DATA USULAN :" +hasil_premi);
							}else{
								if(edit.getDatausulan().getLsbs_id()==141){
									hasil_premi=produk.of_alert_max_premi(premi.doubleValue(), up.doubleValue()); // *khusus EDUVEST, isian berupa premi, namun dicek untuk max dan min UP nya serta di set UP nya.
								}else{
									hasil_premi=produk.of_alert_max_premi(premi.doubleValue(), up.doubleValue());
								}
								if (hasil_premi.trim().length()!=0)
								{
									err.rejectValue("datausulan.mspr_premium","","HALAMAN DATA USULAN :" +hasil_premi);
								}else{
									hasil_premi=produk.cek_kelipatan_premi(premi.doubleValue());	
								}
							}

						}
					}		
	
					//cek cara bayar
					cek_pmode=new Boolean(false);
					for ( int i=1 ; i<produk.indeks_ii_pmode_list; i++ )
					{
						if (Integer.toString(pmode_id.intValue()).equalsIgnoreCase(Integer.toString(produk.ii_pmode_list[i])))
						{
							cek_pmode=new Boolean(true);
						}
					}
					
					if(products.progressiveLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id))){
						if(products.progressiveLink(Integer.toString(produk.ii_bisnis_id)) ){
							edit.getPowersave().setMsl_bp_rate(new Double(90));
						}
						if(products.progressiveLink(Integer.toString(produk.ii_bisnis_id)) && produk.ii_bisnis_no==3 ){
							if(pmode_id==6){//bulanan
								edit.getPowersave().setMsl_mgi(12);
								edit.getPowersave().setMps_jangka_inv("12");
							}
						}else{
							if(pmode_id==3){//tahunan
								edit.getPowersave().setMsl_mgi(12);
								edit.getPowersave().setMps_jangka_inv("12");
							}else if(pmode_id==2){//semesteran
								edit.getPowersave().setMsl_mgi(6);
								edit.getPowersave().setMps_jangka_inv("6");
							}else if(pmode_id==1){//triwulanan
								edit.getPowersave().setMsl_mgi(3);
								edit.getPowersave().setMps_jangka_inv("3");
							}else if(pmode_id==6){//bulanan
								edit.getPowersave().setMsl_mgi(1);
								edit.getPowersave().setMps_jangka_inv("1");
							}
						}
					}
	
					if (cek_pmode.booleanValue()==false)
					{
						hasil_cek_pmode="Cara bayar tersebut tidak bisa untuk plan ini.";
						err.rejectValue("datausulan.lscb_id","","HALAMAN DATA USULAN :" +hasil_cek_pmode);
					}
	
					if (hasil_cek_pmode.trim().length()==0)
					{
						produk.of_set_pmode(pmode_id.intValue());
						if(produk.ii_bisnis_id==177 || produk.ii_bisnis_id==207 || (produk.ii_bisnis_id==186 && produk.ii_bisnis_no!=3) || produk.ii_bisnis_id==191 ){
							pay_period = edit.getDatausulan().getMspo_pay_period();
						}else{
							pay_period = new Integer(produk.of_get_payperiod());
						}
						
						if (produk.flag_uppremi==0)
						{
							rate_plan=new Double(produk.of_get_rate());
							if (rate_plan.doubleValue()==0 && produk.flag_endowment == 1)
							{
								err.rejectValue("datausulan.kodeproduk","","HALAMAN DATA USULAN :Rate Premi untuk plan ini belum ada.");
							}
							
							premi=new Double(produk.idec_premi_main);
							
							if (produk.flag_mediplan == 1)
							{
								up = new Double(produk.idec_up);
								edit.getDatausulan().setMspr_tsi(up);
							}
							if (premi.doubleValue() == 0)
							{
								err.rejectValue("datausulan.mspr_premium","","HALAMAN DATA USULAN :Premi masih nol, silahkan cek rate, dan up nya lagi.");

							}
							if(kode_produk.intValue()==130 || kode_produk.intValue()==45){
								if(kurs.equals("01")){
									if(premi.doubleValue() > new Double(1000000000)){
										err.rejectValue("datausulan.mspr_premium","","HALAMAN DATA USULAN :Premi maximum Rp.1.000.000.000,-");
									}
								}else{
									if(premi.doubleValue() > new Double(100000)){
										err.rejectValue("datausulan.mspr_premium","","HALAMAN DATA USULAN :Premi maximum $100.000,-");
									}
								}
								
							}
							
						}
						double fltpersen=0;
						edit.getDatausulan().setFlag_uppremi(produk.flag_uppremi);
						if (produk.flag_uppremi==1)
						{
							if ((produk.flag_uppremiopen==1 && pmode_id.intValue()!=0 )|| (flag_ekalink.intValue() == 1) || (produk.flag_artha == 1) || (produk.flag_cerdas_global ==1))
							{
								fltpersen = produk.of_get_fltpersen(pmode_id);
	
								if (Double.toString(up.doubleValue()).equalsIgnoreCase("0.0") && (produk.flag_cerdas_global !=1)){
									produk.of_set_premi(premi.doubleValue());
									up=new Double(produk.idec_up);
									if (flag_horison.intValue() == 1)
									{
										up = new Double((100 - persen.doubleValue())/100 * up.doubleValue());
									}
									if (up.doubleValue() == 0)
									{
										err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :Up masih nol, silahkan cek rate, dan premi nya lagi.");
									}
								}else{
									/*double min_up1=0;
									produk.of_set_premi(premi.doubleValue());
									min_up1=produk.idec_up;
											
									if (up.doubleValue() < min_up1)
									{
										hasil_up="Up Minimum untuk plan ini "+ f.format(min_up1);
										err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :" +hasil_up);
									}*/	
									hasil_up = produk.cek_min_up(premi,up,kurs,pmode_id);
									
									hasil_up = produk.cek_max_up(li_umur_ttg,kode_produk,premi,up,new Double(fltpersen),pmode_id,kurs);
									if (hasil_up.trim().length()!=0)
									{
										err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :" +hasil_up);
									}
									
								}
								
								// untuk worksite yang bisa ubah up
								if (produk.flag_worksite == 1 && produk.flag_uppremiopen==1 )
								{	
									hasil_up = produk.cek_min_up(premi,up,kurs,pmode_id);
									if (hasil_up.trim().length()!=0)
									{
										up = produk.min_up(premi, up, kurs, pmode_id);
									}
								
									hasil_up = produk.cek_max_up(li_umur_ttg,kode_produk,premi,up,new Double(fltpersen),pmode_id,kurs);
									if (hasil_up.trim().length()!=0)
									{
										err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :" +hasil_up);
									}
								}
								
							}else{
								if(edit.getDatausulan().getLsbs_id()==164 && edit.getDatausulan().getMste_flag_el()==1){
									produk.of_set_premi_karyawan(premi.doubleValue());
								}else{
									produk.of_set_premi(premi.doubleValue());
								}
								
								up = new Double(produk.idec_up);
								//khusus produk horizon yang mengisi persentase dplk
								if (flag_horison.intValue() == 1)
								{
									up = new Double((100 - persen.doubleValue())/100 * up.doubleValue());
								}
								if (pmode_id.intValue()==0 && produk.kode_flag > 1 && produk.kode_flag !=11  && produk.kode_flag != 15 && produk.flag_worksite == 0)
								{
									if (kurs.equalsIgnoreCase("01"))
									{
										if (up < 15000000)
										{
											up = new Double(15000000);
										}
									}else{
										if (up < 1500)
										{
											up = new Double(1500);
										}
									}
								}
								
								if (produk.flag_worksite == 1)
								{
									hasil_up = produk.cek_min_up(premi,up,kurs,pmode_id);
									if (hasil_up.trim().length()!=0)
									{
										if (produk.flag_worksite == 1)
										{
											up = produk.min_up(premi, up, kurs, pmode_id);
										}else{
											err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :" +hasil_up);
									
										}
									}
								}
								
							}
						}
					}
				}
					if(produk.ii_bisnis_id==195 || produk.ii_bisnis_id==204){
						edit.getDatausulan().setMspo_provider(2);
					}
					
				//(Deddy)- Untuk Cerdas, Upnya maksimal tergantung pada usia tertanggung, rumus : factor * UP yang diinput	
				if(kode_produk.intValue() == 120 || kode_produk.intValue() == 127){
					if(edit.getDatausulan().getMspr_tsi()< up){
						BigDecimal upstring= new BigDecimal(up);
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Up minimum : "+ upstring);
					}else {
						Double factor = 0.0;
						if(li_umur_ttg >= 1 && li_umur_ttg<=19){
							factor = 50.0;
						}else if(li_umur_ttg >= 20 && li_umur_ttg <= 29){
							factor = 40.0;
						}else if(li_umur_ttg >= 30 && li_umur_ttg <= 39){
							factor = 30.0;
						}else if(li_umur_ttg >= 40 && li_umur_ttg <= 49){
							factor = 20.0;
						}else if(li_umur_ttg >= 50 && li_umur_ttg <= 60){
							factor = 10.0;
						}
						Double factor_premi = 1.0;
						if(edit.getDatausulan().getLscb_id()==6){//bulanan
							factor_premi = 10.0;
						}else if(edit.getDatausulan().getLscb_id()==1){//triwulanan
							factor_premi = 100.0 / 27.0;
						}else if(edit.getDatausulan().getLscb_id()==2){//semesteran
							factor_premi = 100.0 / 52.5;
						}
						Double up_sementara = factor * (edit.getDatausulan().getMspr_premium() * factor_premi);
						BigDecimal upstringlagi = new BigDecimal(up_sementara);
						if(edit.getDatausulan().getMspr_tsi()> up_sementara){
							err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Up maximum : "+ upstringlagi);
						}else up = edit.getDatausulan().getMspr_tsi();
					}
				}
				
				if (kode_produk.intValue() == 63 || kode_produk.intValue() == 173)
				{
					li_umur_ttg=li_umur_pp ;
					edit.getTertanggung().setUsiattg((li_umur_ttg));
				}
				
				if(kode_produk.intValue() == 183 || kode_produk.intValue() == 189 || kode_produk.intValue() == 193|| produk.ii_bisnis_id==195){//UP untuk Eka Sehat
					up = produk.idec_up;
				}
					
					edit.getDatausulan().setMspr_tsi(up);
					edit.getDatausulan().setMspr_premium(premi * diskon_karyawan);
					edit.getDatausulan().setRate_plan((rate_plan));
					edit.getDatausulan().setFlag_account((flag_account));
					edit.getDatausulan().setMspr_ins_period((ins_period));
					edit.getDatausulan().setMspo_pay_period((pay_period));
					
					Integer platinum = edit.getDatausulan().getMspo_installment();
					if ( produk.flag_platinumlink == 1)
					{
						if (platinum==null)
						{
							edit.getDatausulan().setMspo_installment(pay_period);
						}else{
							if (platinum.intValue()>pay_period.intValue())
							{
								err.rejectValue("datausulan.mspo_installment","","Cuti Premi masimal sama dengan masa pembayaran.");
							}else{
								if (platinum.intValue()<2)
								{
									err.rejectValue("datausulan.mspo_installment","","Cuti Premi minimal 2.");
								}
							}
						}
					}else{
						if (produk.flag_cuti_premi == 1)
						{
							if (platinum==null)
							{
								edit.getDatausulan().setMspo_installment(null);
							}else{
								if(kode_produk.intValue() == 121){
									if (pmode_id.intValue() !=0)
									{
										if (platinum.intValue()%5!=0)
										{
											err.rejectValue("datausulan.mspo_installment","","Cuti Premi harus kelipatan 5.Contoh : 5tahun, 10 tahun, 20tahun dan seterusnya.");
										}else{
											if (platinum.intValue()<5)
											{
												err.rejectValue("datausulan.mspo_installment","","Cuti Premi minimal 5.");
											}
										}
									}else{
										edit.getDatausulan().setMspo_installment(new Integer(1));
									}
								}else{
									if (pmode_id.intValue() !=0)
									{
										if (platinum.intValue()>20)
										{
											err.rejectValue("datausulan.mspo_installment","","Cuti Premi maximal 20.");
										}else{
											if (platinum.intValue()<1)
											{
												err.rejectValue("datausulan.mspo_installment","","Cuti Premi minimal 1.");
											}
										}
									}else{
										edit.getDatausulan().setMspo_installment(new Integer(1));
									}
								}
							}
						}else{
							edit.getDatausulan().setMspo_installment(null);
						}					
					}
					
					//Integer flag_gutri = edit.getDatausulan().getFlag_gutri();
				
				/*
				 * rider
				 */
									
				Integer hubungan = edit.getPemegang().getLsre_id();
				List dtrd = edit.getDatausulan().getDaftaRider();
				Integer valid= edit.getDatausulan().getIndeks_validasi();
				if (valid==null)
				{
					valid=new Integer(0);
				}
				edit.getDatausulan().setIndeks_validasi(valid);
				
				//Tambahan oleh Yusuf (22/2/2008) 
				//Hidup Bahagia ada Rider yang otomatis (semacam rider include), tapi ada premi nya
				/**
					815 payor
					7-5
					8-10
					9-15
					10-20
					
					814 waiver
					6-5
					7-10
					8-15
					9-20
				 */
				if(kode_produk.intValue() == 167) {
					dtrd.clear();
					if(hubungan != 1) { //jika PP <> TT
						//a. dan Pemegang Polis berusia 17 <= x <= (60-MPP) tahun, maka diwajibkan mengikuti asuransi tambahan (Rider) Payor’s Clause
						if(17 <= li_umur_pp && li_umur_pp <= (60 - pay_period)) {
							//HANYA BOLEH DAN WAJIB IKUT PAYOR'S CLAUSE
							if(number_produk.intValue() >= 1 && number_produk <= 4) {						
								int number_rider = number_produk+6;
								Datarider rider = new Datarider(815, number_rider, 0, 1, 0, (double) 0, (double) 0, (double) 0, 0, 0, 815+"~X"+number_rider);
								dtrd.add(rider);
							}
						//b. dan Pemegang Polis berusia >(60-MPP) tahun, maka tidak dapat mengikuti asuransi tambahan (Rider) Payor’s Clause
						}else if(li_umur_pp > (60 - pay_period)) {
							//TIDAK BOLEH IKUT PAYOR'S CLAUSE
						}
					}else { //jika PP = TT
						// dan Pemegang Polis berusia 17 <= x <= (55-MPP) tahun, maka diwajibkan mengikuti asuransi tambahan (Rider) Waiver of Premium Disability
						if(17 <= li_umur_pp && li_umur_pp <= (55 - pay_period)) {
							//WAJIB IKUT WPD
							if(number_produk.intValue() >= 1 && number_produk <= 4) {						
								int number_rider = number_produk+5;
								Datarider rider = new Datarider(814, number_rider, 0, 1, 0, (double) 0, (double) 0, (double) 0, 0, 0, 814+"~X"+number_rider);
								dtrd.add(rider);
							}
						}
					}
				}
				//END OF Tambahan oleh Yusuf (22/2/2008)
				
				//Deddy - SSP PRO mendapatkan TERM rider gratis
//				if(kode_produk.intValue()==186 && number_produk.intValue()==3){
//					Datarider rider = new Datarider(818, 1, 0, 1, 0, (double) 0, (double) 0, (double) 0, 0, 1, 818+"~X"+1);
//					dtrd.add(rider);
//				}
				
				Integer factor_up =0;
				Integer jumlah_semua_rider=new Integer(0);
				Double total_premi_rider=new Double(0);
				Integer jumlah_r =new Integer(dtrd.size());
				if (jumlah_r==null)
				{
					jumlah_r=new Integer(0);
				}
				
				Integer jmlrider=jumlah_r;
				edit.getDatausulan().setJmlrider(new Integer(jmlrider.intValue() + 1));
				
				Integer tgl_awal_rider = new Integer(0);
				Integer bln_awal_rider = new Integer(0);
				Integer thn_awal_rider = new Integer(0);
				String tanggal_awal_rider =null;
				Integer[] unit_i;
				String hasil_unit1_i;
				String hasil_class1_i;
				Integer[] klases_i;
				Integer[] kd_rider_i;
				String[] nama_rider_i;
				String hasil_rider_i;
				String[] rider_i;
				Double[] sum_i;
				String[] sum_1_i;
				Date[] end_date_rider_i;
				Date[] beg_date_rider_i;
				Date[] end_pay_rider_i;
				Double[] rate_i;
				Integer[] percent_i;
				Integer[] ins_rider_i;
				int[] letak_2;
				Integer[] kode_rider_i;
				Integer[] number_rider_i;
				Integer[] rider_single_i;
				Double[] premi_rider_i;
				Double[] up_pa_rider_i;
				Double[] up_pb_rider_i;
				Double[] up_pc_rider_i;
				Double[] up_pd_rider_i;
				Double[] up_pm_rider_i;
				Integer[] insured_i;
				Integer[] flag_include_i;				
				
				Integer[] unit;
				String hasil_unit1;
				String hasil_class1;
				Integer[] klases;
				Integer[] factor_up1;
				Integer[] kd_rider;
						
				String[] nama_rider;
				String[] nama_rider2;

				String[] rider;
				Double[] sum;
				String[] sum_1;
				Date[] end_date_rider;
				Date[] beg_date_rider;
				Date[] end_pay_rider;
				Double[] rate;
				Integer[] percent;
				Integer[] ins_rider;
				int[] letak_1;
				Integer[] kode_rider;
				Integer[] number_rider;	
				Integer[] rider_single;
				Integer jumlah_rider=new Integer(0);
				Double[] premi_rider;
				Double[] up_pa_rider;
				Double[] up_pb_rider;
				Double[] up_pc_rider;
				Double[] up_pd_rider;
				Double[] up_pm_rider;
				//Double[] premi_tahunan;
				Integer[] insured;
				Integer[] flag_include;
				Double[] mspr_extra;
				Integer[] factor_x;
						
				factor_x = new Integer[jmlrider.intValue()+1];
				unit = new Integer[jmlrider.intValue()+1];
				kd_rider = new Integer[jmlrider.intValue()+1];
				klases =  new Integer[jmlrider.intValue()+1];
				nama_rider= new String[jmlrider.intValue()+1];
				nama_rider2= new String[jmlrider.intValue()+1];
				rider = new String[jmlrider.intValue()+1]; 
				sum = new Double[jmlrider.intValue()+1];
				sum_1=  new String[jmlrider.intValue()+1];
				end_date_rider = new Date[jmlrider.intValue()+1];
				beg_date_rider= new Date[jmlrider.intValue()+1];
				end_pay_rider= new Date[jmlrider.intValue()+1];
				rate= new Double[jmlrider.intValue()+1];
				percent=new Integer[jmlrider.intValue()+1];
				ins_rider= new Integer[jmlrider.intValue()+1];
				letak_1= new int[jmlrider.intValue()+1];
				kode_rider= new Integer[jmlrider.intValue()+1];
				number_rider= new Integer[jmlrider.intValue()+1];
				rider_single=new Integer[jmlrider.intValue()+1];
				premi_rider = new Double[jmlrider.intValue()+1];
				//premi_tahunan = new Double[jmlrider.intValue()+1];
				up_pa_rider = new Double[jmlrider.intValue()+1];	
				up_pb_rider = new Double[jmlrider.intValue()+1];	
				up_pc_rider = new Double[jmlrider.intValue()+1];	
				up_pd_rider = new Double[jmlrider.intValue()+1];	
				up_pm_rider = new Double[jmlrider.intValue()+1];	
				insured = new Integer[jmlrider.intValue()+1];	
				flag_include = new Integer[jmlrider.intValue()+1];
				mspr_extra = new Double[jmlrider.intValue()+1];
				hasil_rider="";
				Integer jumlah_flag_include= new Integer(0);
				Integer FlagSwineFlu= 0;
				Date SebelasBelasOktoberDuaRibuSembilan = defaultDateFormat.parse("11/10/2009");
				Date SatuJanuariDuaRibuSepuluh = defaultDateFormat.parse("1/1/2010");
				Date LimaBelasJuniDuaRibuSepuluh = defaultDateFormat.parse("15/6/2010");
				
				
				produk.riderInclude(number_produk.intValue());

				List dtrd1 = new ArrayList();
				
				
				if(kode_produk.intValue() == 183 || kode_produk.intValue() == 189 || kode_produk.intValue() == 193|| produk.ii_bisnis_id==195 || products.stableSave(kode_produk.intValue(), number_produk.intValue()) || products.stableLink(kode_produk.toString()) || 
				   kode_produk.intValue() == 145 ||kode_produk.intValue() == 146 || kode_produk.intValue() == 163 || products.SuperSejahtera(kode_produk.toString()) || kode_produk.intValue()==40 || 
				   products.unitLinkNew(kode_produk.toString()) || (kode_produk.intValue()==143 && (number_produk>=4 && number_produk<=7)) ||
				   (kode_produk.intValue()==144 && number_produk==4) || (kode_produk.intValue()==158 && (number_produk==13 || (number_produk>=15 && number_produk<=16))) ||
				   (kode_produk.intValue()==134 && number_produk==1) || (kode_produk.intValue()==166 && number_produk==1)){
						if(kode_produk.intValue() == 183 || kode_produk.intValue() == 189 || kode_produk.intValue() == 193 || products.unitLinkNew(kode_produk.toString()) || (kode_produk.intValue()==134 && number_produk==1) || (kode_produk.intValue()==166 && number_produk==1)){
							if(jumlah_r>0){
								for (int k =0 ; k <jumlah_r.intValue();k++)
								{
									Datarider rd= (Datarider)dtrd.get(k);
									kode_rider[k]=rd.getLsbs_id();
									number_rider[k]=rd.getLsdbs_number();
									
									Map kode_product_map = new HashMap();
									kode_product_map.put("kode_bisnis", kode_rider[k]);
									kode_product_map.put("no_bisnis", number_rider[k]);
									Map data4 = (HashMap) uwManager.selectNamaPlan(kode_product_map);
									if (data4!=null)
									{		
										rd.setLsdbs_name(((String)data4.get("LSDBS_NAME")).toUpperCase());
									}
									
									if( edit.getDatausulan().getFlag_as()==2 && (kode_rider[k].intValue()==823 || kode_rider[k].intValue()==825)){
										diskon_karyawan=0.7;
									}
									if(((kode_rider[k].intValue()==820 || kode_rider[k].intValue()==825) && (number_rider[k].intValue()>=1 && number_rider[k].intValue()<=15)) || (kode_produk.intValue()==183 && (number_produk.intValue()>=1 && number_produk.intValue()<=30)) || (kode_produk.intValue()==189 && (number_produk.intValue()>=1 && number_produk.intValue()<=15)) || (kode_produk.intValue()==193 && (number_produk.intValue()>=1 && number_produk.intValue()<=15)) ){
										unit[k] = new Integer(0);
										kd_rider[k] = new Integer(0);
										klases[k] =  new Integer(0);
										nama_rider[k]= "";
										nama_rider2[k]= "";
										rider[k] = ""; 
										factor_x[k] = new Integer(0);
										sum[k] = new Double(0);
										end_date_rider[k] = null;
										beg_date_rider[k]=null;
										end_pay_rider[k]=null;
										rate[k]= new Double(0);
										percent[k]=new Integer(0);
										ins_rider[k]= new Integer(0);
										kode_rider[k]= new Integer(0);
										number_rider[k]= new Integer(0);
										rider_single[k]= new Integer(0);
										premi_rider[k] = new Double(0);
										//premi_tahunan[k] = new Double(0);
										up_pa_rider[k] = new Double(0);	
										up_pb_rider[k] = new Double(0);	
										up_pc_rider[k] = new Double(0);	
										up_pd_rider[k] = new Double(0);	
										up_pm_rider[k] = new Double(0);	
										insured[k] = new Integer(0);	
										flag_include[k] = new Integer(0);
										mspr_extra[k] = new Double(0);
	
										unit[k]=rd.getMspr_unit();
										if (unit[k] == null)
										{
											unit[k] = new Integer(0);
											rd.setMspr_unit(new Integer(0));
										}
										klases[k]=rd.getMspr_class();
										if (klases[k] == null)
										{
											klases[k] = new Integer(0);
											rd.setMspr_class(new Integer(0));
										}
										kode_rider[k]=rd.getLsbs_id();
										
										number_rider[k]=rd.getLsdbs_number();
										ins_rider[k]=rd.getMspr_ins_period();
										insured[k] = rd.getMspr_tt();
										rate[k] = rd.getMspr_rate();
										sum[k] = rd.getMspr_tsi();
										premi_rider[k] =rd.getMspr_premium();
										
										beg_date_rider[k]=rd.getMspr_beg_date();
										end_pay_rider[k]=rd.getMspr_end_pay();
										end_date_rider[k]=rd.getMspr_end_date();
										rider[k]=rd.getPlan_rider();	
										flag_include[k] = new Integer(0);
										Double up_rider = new Double(0);
										//include rider swine flu/flu babi 822
										Integer[]kode_rider_swine;
										kode_rider_swine= new Integer[jmlrider.intValue()+1];
										kode_rider_swine[k]=822;
										if ((Integer.toString(kode_rider[k].intValue())).trim().length()==1)
										{
											nama_rider[k]="produk_asuransi.n_prod_0"+kode_rider[k];	
										}else{
											nama_rider[k]="produk_asuransi.n_prod_"+kode_rider[k];	
										}
						
										if (rider[k].equalsIgnoreCase("0~X0"))
										{
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Silahkan memilih Plan Rider ke "+(k+1)+" terlebih dahulu.");
										}
										
										if ((Integer.toString(kode_rider_swine[k].intValue())).trim().length()==1)
										{
											nama_rider2[k]="produk_asuransi.n_prod_0"+kode_rider_swine[k];	
										}else{
											nama_rider2[k]="produk_asuransi.n_prod_"+kode_rider_swine[k];	
										}
						
										if (rider[k].equalsIgnoreCase("0~X0"))
										{
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Silahkan memilih Plan Rider ke "+(k+1)+" terlebih dahulu.");
										}
										
										if (!nama_rider[k].equalsIgnoreCase("produk_asuransi.n_prod_00"))
										{
											//cek rider 
											try{
												Class aClass1 = Class.forName(nama_rider[k]);
												n_prod produk1 = (n_prod)aClass1.newInstance();
												Class aClass2 = Class.forName(nama_rider2[k]);
												n_prod produk2 = (n_prod)aClass2.newInstance();
												up_rider = produk1.of_get_up(premi.doubleValue(),up.doubleValue(),unit[k].intValue(),produk.flag_jenis_plan,kode_rider[k].intValue(),number_rider[k].intValue(),flag_platinum);
												
												up_rider = up_rider * 10;
												up_rider = produk2.cek_maks_up_rider(up_rider, kurs);
											}catch (ClassNotFoundException e)
											{
												logger.error("ERROR :", e);
											} catch (InstantiationException e) {
												logger.error("ERROR :", e);
											} catch (IllegalAccessException e) {
												logger.error("ERROR :", e);
											}
										}
//										if(edit.getCurrentUser().getJn_bank()==2){
										if(products.stableLink(kode_produk.toString())){
											if(edit.getPowersave().getMsl_spaj_lama()!=null){//apabila surrender endors, maka begdatenya ambil bdate dari slink, bukan dari insured.
												if(edit.getPowersave().getBegdate_topup().after(SebelasBelasOktoberDuaRibuSembilan) && edit.getPowersave().getBegdate_topup().before(SatuJanuariDuaRibuSepuluh)){
													Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, up_rider, 0, 0, 822+"~X"+1);
													if(dtrd.size()!=0){
														dtrd.add(rider_includer);
													}
												}
											}else{
												if(edit.getDatausulan().getMste_beg_date().after(SebelasBelasOktoberDuaRibuSembilan) && edit.getDatausulan().getMste_beg_date().before(SatuJanuariDuaRibuSepuluh)){
													Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, up_rider, 0, 0, 822+"~X"+1);
													if(dtrd.size()!=0){
														dtrd.add(rider_includer);
													}
												}
											}
										}else{
											if(edit.getDatausulan().getMste_beg_date().after(SebelasBelasOktoberDuaRibuSembilan) && edit.getDatausulan().getMste_beg_date().before(SatuJanuariDuaRibuSepuluh) ){
												Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, up_rider, 0, 0, 822+"~X"+1);
												if(dtrd.size()!=0){
													dtrd.add(rider_includer);
												}
											}
										}
											
//										}else {
//											Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, up_rider, 0, 0, 822+"~X"+1);
//											if(dtrd.size()!=0){
//												dtrd.add(rider_includer);
//											}
//										}
									}
								}
							}else{
								if(kode_produk.intValue() == 183 || kode_produk.intValue() == 189 || kode_produk.intValue() == 193){
									Double up_rider = new Double(0);
									up_rider = produk.of_get_min_up();
									up_rider = up_rider * 10;
									String nama_swine;
									Integer kode_rider_swine = 822;
									if ((Integer.toString(kode_rider_swine.intValue())).trim().length()==1)
									{
										nama_swine="produk_asuransi.n_prod_0"+kode_rider_swine;	
									}else{
										nama_swine="produk_asuransi.n_prod_"+kode_rider_swine;	
									}
									
									if (!nama_swine.equalsIgnoreCase("produk_asuransi.n_prod_00"))
									{
										//cek rider 
										try{
											Class aClass1 = Class.forName(nama_swine);
											n_prod produk1 = (n_prod)aClass1.newInstance();
											
											up_rider = produk1.cek_maks_up_rider(up_rider, kurs);
											if(edit.getDatausulan().getMste_beg_date().after(SebelasBelasOktoberDuaRibuSembilan) && edit.getDatausulan().getMste_beg_date().before(SatuJanuariDuaRibuSepuluh)){
												Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, up_rider, 0, 0, 822+"~X"+1);
												dtrd.add(rider_includer);
											}
										}catch (ClassNotFoundException e)
										{
											logger.error("ERROR :", e);
										} catch (InstantiationException e) {
											logger.error("ERROR :", e);
										} catch (IllegalAccessException e) {
											logger.error("ERROR :", e);
										}
									}
								}
							}
						}else if(products.stableSave(kode_produk.intValue(),number_produk.intValue()) || products.stableLink(kode_produk.toString()) || (kode_produk.intValue()==143 && (number_produk>=4 && number_produk<=7)) ||
								(kode_produk.intValue()==144 && number_produk==4) || (kode_produk.intValue()==158 && (number_produk==13 || (number_produk>=15 && number_produk<=16)))){
							//if(edit.getCurrentUser().getJn_bank()==2){
								if(edit.getPowersave().getMsl_spaj_lama()!=null){//apabila surrender endors, maka begdatenya ambil bdate dari slink, bukan dari insured.
									if(edit.getPowersave().getBegdate_topup().after(SebelasBelasOktoberDuaRibuSembilan) && edit.getPowersave().getBegdate_topup().before(SatuJanuariDuaRibuSepuluh)){
										if(edit.getDatausulan().getLku_id().equals("01")){
											if(products.stableLink(kode_produk.toString()) && kode_produk!=186){	
												if(edit.getDatausulan().getTotal_premi_sementara()>=100000000){
													Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) 20000000.0, 0, 0, 822+"~X"+1);
													dtrd.add(rider_includer);	
												}
											}else {
												if(kode_produk!=186){
													if(edit.getDatausulan().getMspr_premium()>=100000000){
														Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) 20000000.0, 0, 0, 822+"~X"+1);
														dtrd.add(rider_includer);	
													}
												}
											}
											
										}else if(edit.getDatausulan().getLku_id().equals("02")){
											if(products.stableLink(kode_produk.toString()) && kode_produk!=186){
												if(edit.getDatausulan().getTotal_premi_sementara()>=10000){
													Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) 2000.0, 0, 0, 822+"~X"+1);
													dtrd.add(rider_includer);	
												}
											}else{
												if(kode_produk!=186){
													if(edit.getDatausulan().getMspr_premium()>=10000){
														Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) 2000.0, 0, 0, 822+"~X"+1);
														dtrd.add(rider_includer);	
													}
												}
											}
										}
									}
								}else{
									if(edit.getDatausulan().getMste_beg_date().after(SebelasBelasOktoberDuaRibuSembilan) && edit.getDatausulan().getMste_beg_date().before(SatuJanuariDuaRibuSepuluh)){
										if(edit.getDatausulan().getLku_id().equals("01")){
											if(products.stableLink(kode_produk.toString()) && kode_produk!=186){	
												if(edit.getDatausulan().getTotal_premi_sementara()>=100000000){
													Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) 20000000.0, 0, 0, 822+"~X"+1);
													dtrd.add(rider_includer);	
												}
											}else {
												if(kode_produk!=186){
													if(edit.getDatausulan().getMspr_premium()>=100000000){
														Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) 20000000.0, 0, 0, 822+"~X"+1);
														dtrd.add(rider_includer);	
													}
												}
											}
											
										}else if(edit.getDatausulan().getLku_id().equals("02")){
											if(products.stableLink(kode_produk.toString()) && kode_produk!=186){
												if(edit.getDatausulan().getTotal_premi_sementara()>=10000){
													Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) 2000.0, 0, 0, 822+"~X"+1);
													dtrd.add(rider_includer);	
												}
											}else{
												if(kode_produk!=186){
													if(edit.getDatausulan().getMspr_premium()>=10000){
														Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) 2000.0, 0, 0, 822+"~X"+1);
														dtrd.add(rider_includer);	
													}
												}
											}
										}
									}
								}
						}else if(kode_produk.intValue() == 145 ||kode_produk.intValue() == 146 || kode_produk.intValue() == 163 || products.SuperSejahtera(kode_produk.toString()) || kode_produk.intValue()==40){
							Double up_rider = new Double(0);
							up_rider = edit.getDatausulan().getMspr_tsi() * 0.1;
							if(edit.getDatausulan().getLku_id().equals("01")){
								if(up_rider>=20000000.0){
									up_rider=20000000.0;
								}
							}else if(edit.getDatausulan().getLku_id().equals("02")){
								if(up_rider>=2000.0){
									up_rider=2000.0;
								}
							}
							
								if(edit.getDatausulan().getMste_beg_date().after(SebelasBelasOktoberDuaRibuSembilan) && edit.getDatausulan().getMste_beg_date().before(SatuJanuariDuaRibuSepuluh)){
									if(edit.getDatausulan().getLku_id().equals("01")){
										if(edit.getDatausulan().getMspr_tsi()>=100000000){
											Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) up_rider, 0, 0, 822+"~X"+1);
											dtrd.add(rider_includer);	
										}
									}else if(edit.getDatausulan().getLku_id().equals("02")){
										if(edit.getDatausulan().getMspr_tsi()>=10000){
											Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) up_rider, 0, 0, 822+"~X"+1);
											dtrd.add(rider_includer);	
										}
									}
								}
						}
						jumlah_r=new Integer(dtrd.size());
						jmlrider=new Integer(dtrd.size());
						
				}
				
				//Start validation swine Flu free double
				if(dtrd.size()>0){
					for (int k =0 ; k <dtrd.size();k++){
						
						Datarider rd= (Datarider)dtrd.get(k);
						kode_rider[k]=rd.getLsbs_id();
						number_rider[k]=rd.getLsdbs_number();
						if(kode_rider[k].intValue()==822 && number_rider[k].intValue()==1) FlagSwineFlu+=1;
						if(FlagSwineFlu>1){
							dtrd.remove(k-1);
							FlagSwineFlu-=1;
						}
					}
					jumlah_r=new Integer(dtrd.size());
					jmlrider=new Integer(dtrd.size());
					edit.getDatausulan().setJmlrider(jmlrider);
					edit.getDatausulan().setJumlah_seluruh_rider(jmlrider);
				}
				//END validation swine Flu
				
//				(deddy)- REQ ASRI : JIKA USIA _TT =50 tahun, dan (mengambil eka sehat stand alone atau (produk link dan ada rider eka sehat) maka diberikan warning dan set harus pemeriksaan kesehatanl )
				if((products.unitLink(Integer.toString(produk.ii_bisnis_id)) || produk.ii_bisnis_id==183 || produk.ii_bisnis_id==189 || produk.ii_bisnis_id==193) && edit.getTertanggung().getMste_age()==50  ){
					if(produk.ii_bisnis_id==183 || produk.ii_bisnis_id==189 || produk.ii_bisnis_id==193){
						edit.getDatausulan().setAlertEkaSehat(1);
						edit.getDatausulan().setMste_medical(1);
					}else{
						for(int i=0;i<jumlah_rider;i++){
							Datarider banding1 = (Datarider)dtrd.get(i);
							if((banding1.getLsbs_id()==820 || banding1.getLsbs_id()==823) && banding1.getLsdbs_number()<16){
								edit.getDatausulan().setAlertEkaSehat(1);
								edit.getDatausulan().setMste_medical(1);
							}
						}
					}
				}
				
				//ambil data dari list
				for (int k =0 ; k <jumlah_r.intValue();k++)
				{
					
					unit[k] = new Integer(0);
					kd_rider[k] = new Integer(0);
					klases[k] =  new Integer(0);
					nama_rider[k]= "";
					rider[k] = ""; 
					sum[k] = new Double(0);
					end_date_rider[k] = null;
					beg_date_rider[k]=null;
					end_pay_rider[k]=null;
					rate[k]= new Double(0);
					percent[k]=new Integer(0);
					ins_rider[k]= new Integer(0);
					kode_rider[k]= new Integer(0);
					number_rider[k]= new Integer(0);
					rider_single[k]= new Integer(0);
					premi_rider[k] = new Double(0);
					//premi_tahunan[k] = new Double(0);
					up_pa_rider[k] = new Double(0);	
					up_pb_rider[k] = new Double(0);	
					up_pc_rider[k] = new Double(0);	
					up_pd_rider[k] = new Double(0);	
					up_pm_rider[k] = new Double(0);	
					insured[k] = new Integer(0);	
					flag_include[k] = new Integer(0);
					mspr_extra[k] = new Double(0);

					Datarider rd= (Datarider)dtrd.get(k);
					kode_rider[k]=rd.getLsbs_id();
					
					number_rider[k]=rd.getLsdbs_number();

					unit[k]=rd.getMspr_unit();
					if (unit[k] == null)
					{
						unit[k] = new Integer(0);
						rd.setMspr_unit(new Integer(0));
					}
					klases[k]=rd.getMspr_class();
					if (klases[k] == null)
					{
						klases[k] = new Integer(0);
						rd.setMspr_class(new Integer(0));
					}
					ins_rider[k]=rd.getMspr_ins_period();
					insured[k] = rd.getMspr_tt();
					rate[k] = rd.getMspr_rate();
					sum[k] = rd.getMspr_tsi();
					premi_rider[k] =rd.getMspr_premium();
					
					beg_date_rider[k]=rd.getMspr_beg_date();
					end_pay_rider[k]=rd.getMspr_end_pay();
					end_date_rider[k]=rd.getMspr_end_date();
					rider[k]=rd.getPlan_rider();	
					flag_include[k] = new Integer(0);
					
					// untuk RIDER
					if (kode_rider[k].intValue() <900 )
					{
						if ((Integer.toString(kode_rider[k].intValue())).trim().length()==1)
						{
							nama_rider[k]="produk_asuransi.n_prod_0"+kode_rider[k];	
						}else{
							nama_rider[k]="produk_asuransi.n_prod_"+kode_rider[k];	
						}
		
						if (rider[k].equalsIgnoreCase("0~X0"))
						{
							err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Silahkan memilih Plan Rider ke "+(k+1)+" terlebih dahulu.");
						}
						
						
						if (!nama_rider[k].equalsIgnoreCase("produk_asuransi.n_prod_00"))
						{
							//cek rider 
							try{
								Class aClass1 = Class.forName(nama_rider[k]);
								n_prod produk1 = (n_prod)aClass1.newInstance();
								produk1.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
	
								Boolean type_rider=new Boolean(false);
								produk1.li_lama=(ins_rider[k].intValue());
								//logger.info(produk1.li_lama);
								//int lama_bayar =(ins_rider[k].intValue());
								kd_rider[k]=new Integer(produk1.flag_rider_baru);
								li_pct_biaya = new Double(produk1.li_pct_biaya); 
								if (produk.flag_rider==0)
								{
									klases[k] = new Integer(produk1.set_klas(klases[k].intValue()));
									unit[k] = new Integer(produk1.set_unit(unit[k].intValue()));
								}
								
								if ((kode_rider[k].intValue()==819) && ((number_rider[k].intValue()>=1 && number_rider[k].intValue()<=20) || (number_rider[k].intValue()>=141 && number_rider[k].intValue()<=160) || (number_rider[k].intValue()>=281 && number_rider[k].intValue()<=300) || (number_rider[k].intValue()>=381 && number_rider[k].intValue()<=390)))
								{
									edit.getDatausulan().setFlag_hcp(new Integer(1));
									flag_hcp = new Integer(1);
									number_utama = number_rider[k];
								}
								
								if(products.provider(Integer.toString(produk.ii_bisnis_id))){
									if( kode_rider[k].intValue()==823 || kode_rider[k].intValue()==825 || kode_rider[k].intValue()==826||kode_rider[k].intValue()==832||kode_rider[k].intValue()==833){
										edit.getDatausulan().setMspo_provider(2);
									}
								}
								
								jumlah_rider=jmlrider;
		
								//cek rider boleh dipilih atau ga
								for ( int i=0 ; i< produk.indeks_rider_list; i++ )
								{
									if (kode_rider[k].intValue()==produk.ii_rider[i])
									{
										type_rider=new Boolean(true);
									}
								}
								
								
								//(Deddy) validasi kalau untuk kode_rider 813 dan 818, untuk powersave, stabil link, dan stable save  (813 CI) number_rider 4&5 dan (818 TERM) number_rider 3&4
								// untuk multiinvest (813 CI) hanya number_rider 6 dan (818 TERM) hanya number_rider 5
								if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.multiInvest(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no) ){
									if((products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no))  ){
										if(kode_rider[k].intValue()==813){
											if(number_rider[k].intValue() != 4 && number_rider[k].intValue() != 5){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Plan Produk ini, hanya bisa memilih yang CI - 4" );
											}
										}else if(kode_rider[k].intValue()==818){
											if(number_rider[k].intValue() != 3 && number_rider[k].intValue() != 4){
												if((produk.ii_bisnis_id==186 && produk.ii_bisnis_no==3)){
													
												}else{
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Plan Produk ini, hanya bisa memilih yang TERM - 4" );
												}
											}
										}
									}else if(products.multiInvest(Integer.toString(produk.ii_bisnis_id))){
										if(kode_rider[k].intValue()==813){
											if(number_rider[k].intValue() != 6){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Plan Produk ini, hanya bisa memilih yang CI - 8" );
											}
										}else if(kode_rider[k].intValue()==818){
											if(number_rider[k].intValue() != 5){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Plan Produk ini, hanya bisa memilih yang TERM - 8" );
											}
										}
									}
								}
								
								//TODO:DEDDY -> Validasi ini berlaku per tanggal 1 april 2010
								//Validasi untuk produk Platinum Link, harus PA resiko ABD; HCP Family semua plan
//								if(produk.ii_bisnis_id==134){
//									if(kode_rider[k].intValue()==810 && number_rider[k].intValue()!=3){
//										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Platinum Link, jenis Rider PA hanya bisa mengambil yang risiko ABD" );
//									}
//								}
								
								if(products.SuperSejahtera(Integer.toString(produk.ii_bisnis_id)) && produk.ii_bisnis_no==5){
									if(kode_rider[k].intValue()== 802){
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Super Sejahtera Sekaligus, tidak dapat mengambil Plan Rider Payor" );
									}
									if(kode_rider[k].intValue()== 804){
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Super Sejahtera Sekaligus, tidak dapat mengambil Plan Rider Waiver" );
									}
								}
								
								//(Deddy) validasi untuk Eka Sehat Utama(183) : apabila produk utamanya Eka Sehat, maka rider jenis Spouse dan Child baru dapat diambil setelah proses input bac selesai.
								//validasi untuk Eka Sehat Rider(820) : apabila produk utama bukan Eka Sehat, maka rider yg dapat diambil di input BAC hanya yang basic.
								//Untuk spouse dan Child dapat ditambah di tombol Eka Sehat/HCP Family.
								if(!status.toString().equals("edit")){
									if(produk.ii_bisnis_id==183 || produk.ii_bisnis_id==189 || produk.ii_bisnis_id==193){
										if(kode_rider[k].intValue()==820 || kode_rider[k].intValue()==825){
											if(number_rider[k].intValue() > 15){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk jenis Spouse dan Child, tidak dapat diinput disini.Lakukan penginputan Spouse dan Child di tombol SM/Eka Sehat/HCP Family di atas." );
											}else if(number_rider[k].intValue() <= 15){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Produk Utama merupakan jenis BASIC, rider Eka Sehat BASIC tidak dapat diambil." );
											}
										}
									}else if(produk.ii_bisnis_id!=183 && produk.ii_bisnis_id!=189 && produk.ii_bisnis_id!=193 ){
										if(kode_rider[k].intValue()==820 || kode_rider[k].intValue()==825){
											if(number_rider[k].intValue() > 15 && number_rider[k].intValue() < 91){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Eka Sehat yang dapat diambil saat ini hanya jenis BASIC. Lakukan penginputan Spouse dan Child di tombol SM/Eka Sehat/HCP Family di atas." );
											}
										}
									}
									
									//(Deddy) validasi untuk Smart Medicare(178)
									if(produk.ii_bisnis_id==178){
										if(kode_rider[k].intValue()==821){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Penambahan Tertanggung Tambahan dapat dilakukan setelah proses submit dengan menekan tombol SM/Eka Sehat/HCP Family di atas." );
										}
									}
								}
								
								
								
								//(Deddy)validasi untuk produk utama EkaWaktu, apabila pembayaran SEKALIGUS maka tidak dapat mengambil rider Waiver
								if(produk.ii_bisnis_id==169){
									if(produk.ib_single_premium){
										if(kode_rider[k].intValue()==814){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Cara Bayar Sekaligus, tidak bisa memilih Rider Waiver" );
										}
									}
									
								}
								
								if(products.unitLink(Integer.toString(produk.ii_bisnis_id))){
									if(kode_rider[k].intValue()==820){
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Plan Unit Link, rider Eka Sehat yang diambil harus Provider" );
									}
								}
								
								//(Deddy)validasi HCP untuk produk dasar powersave, stabil link,  dan stable save
								if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no) ){
									if(kode_rider[k].intValue()==819){
										if(number_rider[k].intValue() <=280 && number_rider[k].intValue() >= 381){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Plan Produk ini, hanya bisa memilih HCPF 4" );
										}
									}
								}else if(products.multiInvest(Integer.toString(produk.ii_bisnis_id))){
									if(kode_rider[k].intValue()==819){
										if(number_rider[k].intValue() <=380 || number_rider[k].intValue() >= 431){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Plan Produk ini, hanya bisa memilih HCPF 8" );
										}
									}
								}
								
								//(Deddy)validasi untuk rider di produk cerdas care
								if(produk.ii_bisnis_id == 120){
									if(produk.ii_bisnis_no == 1 || produk.ii_bisnis_no == 4 || produk.ii_bisnis_no == 7 || produk.ii_bisnis_no == 10){
										if(produk1.ii_bisnis_id == 814){
											if(number_rider[k].intValue() != 4 ){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider WAIVER yang dapat dipilih hanya 5 TPD");
											}
										}else if(produk1.ii_bisnis_id == 815){
											if(number_rider[k].intValue() != 4 ){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider PAYOR yang dapat dipilih hanya 5 TPD");
											}
										}else if(produk1.ii_bisnis_id == 816){
											if(number_rider[k].intValue() != 2){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider WAIVER yang dapat dipilih hanya 5 CI");
											}
										}else if(produk1.ii_bisnis_id == 817){
											if(number_rider[k].intValue() != 2){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider PAYOR yang dapat dipilih hanya 5 CI");
											}
										}
									}else {
										if(produk1.ii_bisnis_id == 814){
											if(number_rider[k].intValue() != 5 ){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider WAIVER yang dapat dipilih hanya 10 TPD");
											}
										}else if(produk1.ii_bisnis_id == 815){
											if( number_rider[k].intValue() != 5){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider PAYOR yang dapat dipilih hanya 10 TPD");
											}
										}else if(produk1.ii_bisnis_id == 816){
											if(number_rider[k].intValue() != 3){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider WAIVER yang dapat dipilih hanya 10 CI");
											}
										}else if(produk1.ii_bisnis_id == 817){
											if(number_rider[k].intValue() != 3){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider PAYOR yang dapat dipilih hanya 10 CI");
											}
										}
									}
									if(produk1.ii_bisnis_id == 812){
										if(number_rider[k].intValue() != 1){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider TPD yg bisa dipilih hanya selain yg TPD(Cerdas Incl)");
										}
									}else if(produk1.ii_bisnis_id == 813){
										if(number_rider[k].intValue() > 3){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini tidak bisa memilih jenis CI 4 atau CI 8");
										}
									}else if(produk1.ii_bisnis_id == 818){
										if(number_rider[k].intValue() != 2){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini tidak bisa memilih jenis TERM 4 atau TERM 8");
										}
									}else if(produk1.ii_bisnis_id == 819){
										if(number_rider[k].intValue() >= 281 && number_rider[k].intValue() <= 430){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini tidak bisa memilih jenis HCPF 4 atau HCPF 8");
										}
									}
									
								}
								//Cerdas New
								if(produk.ii_bisnis_id==121){
									if(produk.ii_bisnis_no==1 || produk.ii_bisnis_no==2){
										//PA ABD
										if(produk1.ii_bisnis_id ==810 && number_rider[k].intValue()!=3){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih PA jenis ABD");
										}
										if(produk1.ii_bisnis_id==812 && number_rider[k].intValue()==2){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih TPD Biasa,bukan Include");
										}
										//Only for HCP Family
										if(produk1.ii_bisnis_id==819 && number_rider[k].intValue()>=281){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih HCP Family");
										}else if(produk1.ii_bisnis_id==819 && number_rider[k].intValue()<281){
											if(!(number_rider[k].intValue()>=141 && number_rider[k].intValue()<=160)){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Plan HCP Family, silakan memilih yg type BASIC");
											}
										}
										//CI
										if(produk1.ii_bisnis_id==813 && number_rider[k].intValue()!=1){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih jenis Critical Illness biasa");
										}
										//Waiver 60TPD
										if(produk1.ii_bisnis_id==814 && number_rider[k].intValue()!=1){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih Waiver 60 TPD");
										}
										//Payor 25 TPD/Death
										if(produk1.ii_bisnis_id==815 && number_rider[k].intValue()!=1){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih Payor 25 TPD/Death");
										}
										//Waiver 60 CI
										if(produk1.ii_bisnis_id==816 && number_rider[k].intValue()!=1){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih Waiver 60 CI");
										}
										//Payor 25 CI
										if(produk1.ii_bisnis_id==817 && number_rider[k].intValue()!=4){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih Payor 25 CI");
										}
									}
								}
								
								//(Deddy)Khusus Powersave, stabil dan stable save, untuk ridernya apabila bayar single, maka rider selanjutnya harus single juga(kecuali rider HCP)
								if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no) ){
									if(jumlah_rider>1){
										int jmlh_error_single = 0;
										for(int i=0;i<jumlah_rider;i++){
											for(int j = i+1; j<(jumlah_rider-1);j++){
												Datarider banding1 = (Datarider)dtrd.get(i);
												Datarider banding2 = (Datarider)dtrd.get(j);
												if(banding1.getLsbs_id()==813 || banding1.getLsbs_id()==818){
													if(banding2.getLsbs_id()==813 || banding2.getLsbs_id()==818){
														if((banding1.getLsbs_id()==813 && banding1.getLsdbs_number()==5) && !(banding2.getLsbs_id()==818 && banding2.getLsdbs_number()==4)){
															jmlh_error_single = jmlh_error_single + 1;
														}else if(!(banding1.getLsbs_id()==813 && banding1.getLsdbs_number()==5) && (banding2.getLsbs_id()==818 && banding2.getLsdbs_number()==4)){
															jmlh_error_single = jmlh_error_single + 1;
														}else if((banding1.getLsbs_id()==818 && banding1.getLsdbs_number()==4) && !(banding2.getLsbs_id()==813 && banding2.getLsdbs_number()==5)){
															jmlh_error_single = jmlh_error_single + 1;
														}else if(!(banding1.getLsbs_id()==818 && banding1.getLsdbs_number()==4) && (banding2.getLsbs_id()==813 && banding2.getLsdbs_number()==5)){
															jmlh_error_single = jmlh_error_single + 1;
														}
													}
												}
												
												
											}
										}
										if(jmlh_error_single>0){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Rider TERM dan CI, bila salah satu Single, maka yang lainnya harus single");
										}
									}
								}
								
									
								//(7/5/08) validasi tambahan oleh yusuf untuk dana sejahtera new (163), apabila rider 801 harus sesuai mppnya, misalnya dana sejahtera 5, harus penyakit kritis mpp 5
								if(produk.ii_bisnis_id == 163 && produk1.ii_bisnis_id == 801) {
									if(number_produk.intValue() == 1 && number_rider[k].intValue() != 2) {
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Harap Pilih Rider Penyakit Kritis dengan MPP = 5");
									}else if(number_produk.intValue() == 2 && number_rider[k].intValue() != 3) {
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Harap Pilih Rider Penyakit Kritis dengan MPP = 10");
									}else if(number_produk.intValue() == 3 && number_rider[k].intValue() != 4) {
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Harap Pilih Rider Penyakit Kritis dengan MPP = 15");
									}else if(number_produk.intValue() == 4 && number_rider[k].intValue() != 5) {
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Harap Pilih Rider Penyakit Kritis dengan MPP = 20");
									}else if(number_produk.intValue() == 5 && number_rider[k].intValue() != 1) {
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Harap Pilih Rider Penyakit Kritis tanpa MPP");
									}
								}
										
								if (type_rider.booleanValue()==false)
								{
									hasil_rider="Untuk Plan utama ini tidak bisa memilih rider ke "+(k+1);
									err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
								}else{
									
									hasil_rider = produk.cek_rider_number(number_rider[k].intValue(),kode_rider[k].intValue(),li_umur_ttg.intValue(),li_umur_pp.intValue(),up.doubleValue(),premi.doubleValue(),pmode_id.intValue(),hubungan.intValue(),kurs, pay_period);
									if (hasil_rider.trim().length()!=0)
									{
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
									}
									produk1.ii_bisnis_no=number_rider[k].intValue();
									produk1.of_set_bisnis_no(produk1.ii_bisnis_no);
									produk1.ii_bisnis_id=kode_rider[k].intValue();
									produk1.ii_bisnis_id_utama=kode_rider[k].intValue();
									produk1.of_set_usia_tt(li_umur_ttg.intValue());
									produk1.of_set_usia_pp(li_umur_pp.intValue());
									produk1.of_set_pmode(pmode_id.intValue());
									produk1.li_lbayar=pay_period.intValue();
									produk1.is_kurs_id=kurs;
									if (unit[k]==null)
									{
										unit[k]=new Integer(0);
									}
									if (unit[k].intValue()==0)
									{
										produk1.set_unit(unit[k].intValue());
										unit[k]=new Integer(produk1.iiunit);
									}
									if (klases[k]==null)
									{
										klases[k] = new Integer(0);
									}
									if (klases[k].intValue()==0)
									{
										produk1.set_klas(klases[k].intValue());
										klases[k] = new Integer(produk1.iiclass);
									}
									if(edit.getPowersave().getMsl_spaj_lama()!=null){
										String lsbs_id_kopi = uwManager.selectLsbsId(edit.getPowersave().getMsl_spaj_lama());
										if(products.powerSave(lsbs_id_kopi)){
											if(kode_rider[k].intValue()==822){
												beg_date_rider[k]=edit.getPowersave().getBegdate_topup();
												end_date_rider[k]=FormatDate.add(FormatDate.add(beg_date_rider[k], Calendar.YEAR, 1), Calendar.DATE, -1);
											}
										}else{
											end_date_rider[k]=defaultDateFormat.parse(FormatString.rpad("0",Integer.toString(tanggal.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan.intValue()),2)+"/"+tahun.intValue());
											beg_date_rider[k]=edit.getDatausulan().getMste_beg_date();
										}
									}else{
										end_date_rider[k]=defaultDateFormat.parse(FormatString.rpad("0",Integer.toString(tanggal.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan.intValue()),2)+"/"+tahun.intValue());
										beg_date_rider[k]=edit.getDatausulan().getMste_beg_date();
									}
									
									if (edit.getPemegang().getStatus().equalsIgnoreCase("edit"))
									{
										// pengecekan kalau ada penambahan rider tidak boleh sama dengan rider include
										int idx_rider1=produk.indeks_rider_include;
										
										for (int j=0 ; j <idx_rider1; j++)
										{
											if (kode_rider[k].intValue()==produk.rider_include[j])
											{
												flag_include[k]=new Integer(1);		
												jumlah_flag_include = new Integer(jumlah_flag_include.intValue() + 1);
											}
										}
									}
										
								if (valid.intValue()==0)
								{
										//rider pilihan tidak boleh sama dengan rider include
										if (hasil_rider.trim().length()==0)
										{
											int idx_rider1=produk.indeks_rider_include;
											for (int j=0 ; j <idx_rider1; j++)
											{
													if (kode_rider[k].intValue()==produk.rider_include[j])
													{
														
														if (flag_include[k].intValue()==0)
														{
															hasil_rider="Rider ke "+(k+1)+" tidak bisa dipilih karena merupakan rider include.";
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
														}
													}
											}
										}
									
									//validasari umur, hubungan , class, unit rider
									if (flag_include[k].intValue()==0)
									{
										if (hasil_rider.trim().length()==0)
										{
											int lama_bayar1 = ins_rider[k].intValue();
											hasil_rider=produk1.of_check_usia_rider(tahun2.intValue(),bulan2.intValue(),tanggal2.intValue(),tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),lama_bayar1,number_produk.intValue(),number_rider[k].intValue());
											if (hasil_rider.trim().length()!=0)
											{
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
											}else{
												if (flag_gutri.intValue() == 0 && edit.getPemegang().getMste_flag_guthrie().intValue() == 0)
												{
													hasil_rider=produk1.cek_hubungan(relation_ttg);
												}
													if (hasil_rider.trim().length()!=0)
													{
														err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
													}else{
														if (kode_produk.intValue() != 1)
														{
														hasil_rider=produk.cek_rider(pmode_id.intValue(),kode_rider[k].intValue(),produk1.flag_rider,produk.flag_jenis_plan) ;
														}
														if (hasil_rider.trim().length()!=0)
														{
															hasil_rider=hasil_rider +(k+1);
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
														}else{
															hasil_class1=produk1.range_class(li_umur_ttg.intValue(),klases[k].intValue());
															if (hasil_class1.trim().length()!=0)
															{
																String hasil_class=hasil_class1+" "+(k+1);
																err.rejectValue("datausulan.daftaRider["+k+"].mspr_class","","HALAMAN DATA USULAN :" +hasil_class);
															}
												
															hasil_unit1=produk1.range_unit(unit[k].intValue());
															if (hasil_unit1.trim().length()!=0)
															{
																hasil_unit1=hasil_unit1+" "+(k+1);
																err.rejectValue("datausulan.daftaRider["+k+"].mspr_unit","","HALAMAN DATA USULAN :" +hasil_unit1);
															}
														}
													}
											}
										}
									}
								}
										
								//cari rate rider , premi rider, persentase rider
								//Yusuf (25/02/2008) - Hidup Bahagia
									if(produk.ii_bisnis_id == 167) {
										if(produk1.ii_bisnis_id == 814) { //dengan rider 814, tarik rate nya usia PP = 0
											rate[k] = elionsManager.selectRateRider(produk1.is_kurs_id, li_umur_ttg, 0, produk1.ii_bisnis_id, produk1.ii_bisnis_no);
										}else if(produk1.ii_bisnis_id == 815) { //dengan rider 815, tarik rate nya usia TT = 0
											rate[k] = elionsManager.selectRateRider(produk1.is_kurs_id, 0, li_umur_pp, produk1.ii_bisnis_id, produk1.ii_bisnis_no);
										}
									//(Deddy) rate rider untuk produk dasar powersave, stabil link, stable save dan multiinvest
									}else if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.multiInvest(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no) ){
										 
										if(produk1.ii_bisnis_id == 813 || produk1.ii_bisnis_id == 818){
											rate[k] = elionsManager.selectRateRider(produk1.is_kurs_id, li_umur_ttg, 0, produk1.ii_bisnis_id, produk1.ii_bisnis_no);
										}else rate[k] = produk1.of_get_rate1(klases[k],produk.flag_jenis_plan,number_rider[k],li_umur_ttg,li_umur_pp);
										
									}else if(products.SuperSejahtera(Integer.toString(produk.ii_bisnis_id))){
										if(produk1.ii_bisnis_id == 801){
											rate[k] = elionsManager.selectRateRider(produk1.is_kurs_id, li_umur_ttg, 0, produk1.ii_bisnis_id, produk1.ii_bisnis_no);
										}else{
											rate[k] = produk1.of_get_rate1(klases[k],produk.flag_jenis_plan,number_rider[k],li_umur_ttg,li_umur_pp);	
										}
									}else {
										rate[k] = produk1.of_get_rate1(klases[k],produk.flag_jenis_plan,number_rider[k],li_umur_ttg,li_umur_pp);	
									}
									
									Double up_sementara= up;
									//PENYAKIT KRITIS, rate premi dikali dari UP rider 
									if(produk1.ii_bisnis_id == 801) {
										up_sementara= rd.getMspr_tsi();
									//untuk menentukan UP rider TERM 4 dan CI 4 dari produk powersave, stabil link , dan stable save
									//(Deddy) untuk UP rider CI 4 & SINGLE dan TERM 4 & SINGLE, user input sendiri UP nya. 
									}else if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no) ){
										if(produk1.ii_bisnis_id == 813 || produk1.ii_bisnis_id == 818){
											if(produk1.ii_bisnis_id == 813){
												if(produk1.ii_bisnis_no==4 || produk1.ii_bisnis_no==5){
													up_sementara= rd.getMspr_tsi();
												}else up_sementara= up;
											}else if(produk1.ii_bisnis_id == 818){
												if(produk1.ii_bisnis_no==3 || produk1.ii_bisnis_no==4){
													up_sementara= rd.getMspr_tsi();
												}else up_sementara= up;
											}
											
										}
										up_sementara=rd.getMspr_tsi();
										if(produk1.ii_bisnis_id== 822 && produk1.ii_bisnis_no==1){
											if(edit.getDatausulan().getLku_id().equals("01")){
												up_sementara=20000000.0;
											}else if(edit.getDatausulan().getLku_id().equals("02")){
												up_sementara=2000.0;
											}
										}
									}else {
										up_sementara= up;
									}
									
									if(produk1.ii_bisnis_id==822){
										if(produk1.ii_bisnis_no==1){
											if(edit.getDatausulan().getLku_id().equals("01")){
												up_sementara=20000000.0;
											}else if(edit.getDatausulan().getLku_id().equals("02")){
												up_sementara=2000.0;
											}
										}else if(produk1.ii_bisnis_no==2){
											if(products.stableSave(produk.ii_bisnis_id,produk.ii_bisnis_no) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || (produk.ii_bisnis_id==143 && (produk.ii_bisnis_no>=4 && produk.ii_bisnis_no<=6)) ||
											  (produk.ii_bisnis_id==144 && produk.ii_bisnis_no==4) || (produk.ii_bisnis_id==158 && (produk.ii_bisnis_no>=15 && produk.ii_bisnis_no<=16))){
												if(edit.getDatausulan().getLku_id().equals("01")){
													up_sementara=20000000.0;
												}else if(edit.getDatausulan().getLku_id().equals("02")){
													up_sementara=2000.0;
												}
											}else {
												up_sementara=0.1 * up;
											}
											
										}
									}
									
									up_sementara=	produk1.cek_maks_up_rider(up_sementara.doubleValue(),produk1.is_kurs_id);
									
									Double rate_rider = new Double(1.);
									if(edit.getDatausulan().getLscb_id()==3){
										rate_rider = new Double(1.);
									}else if(edit.getDatausulan().getLscb_id()==6){
										rate_rider = new Double(0.12);
									}else if(edit.getDatausulan().getLscb_id()==2){
										rate_rider = new Double(0.65);
									}else if(edit.getDatausulan().getLscb_id()==1){
										rate_rider = new Double(0.35);
									}
									//Yusuf (25/02/2008) - Hidup Bahagia, Premi Ridernya tidak 0, rate dikali dari premi
									if(produk.ii_bisnis_id == 167) {
										premi_rider[k] = produk1.hit_premi_rider(rate[k], premi, 1, premi);
									//(Deddy) premi rider untuk mginya dijadikan 0 bila belum memilih mgi
									}else if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no) ){
										if(produk1.ii_bisnis_id == 813 && (produk1.ii_bisnis_no==4 || produk1.ii_bisnis_no==5)){
											Double max_up_rider = up * new Double(0.5);
											if(up_sementara> max_up_rider){
												up_sementara = max_up_rider;
											}
										}else if(produk1.ii_bisnis_id == 818 && (produk1.ii_bisnis_no==3 || produk1.ii_bisnis_no==4)){
											if(up_sementara>up){
												up_sementara = up;
											}
										}
										rd.setMspr_tsi(up_sementara);
										String mgi_number = edit.getPowersave().getMps_jangka_inv();
										if(mgi_number==null || mgi_number.equals("")){
											mgi_number = "0";
										}
										Double mgi = Double.parseDouble(mgi_number);
										if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) ){
									//		klo powersave bulanan, untuk faktornya langsung tembak 0.1 aj
											if (produk.flag_powersavebulanan == 1){
												premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara, 0.1, premi);
									
											//untuk powersave non bulanan, faktornya tergantung pada MGI.
											//MGI 3 = 0.275, 6 = 0.525, 12 = 1
											}else {
												Double factor=0.0;
												Double diskon = 0.0;
												Double premi_tampung = premi;
												if(mgi==3){
													factor = 0.270;
												}else if(mgi==6){
													factor = 0.525;
												}else if(mgi==1){
													factor = 0.1;
												}else factor = 1.0;
												if(kode_rider[k]==820){//untuk rider eka sehat,tidak perlu dikalikan faktor mgi nya.
													factor = 1.0;
												}
												if(kode_rider[k]==820 && ((number_rider[k]>=1 || number_rider[k]<=15) || (number_rider[k]>=91 || number_rider[k]<=105))){
													premi = elionsManager.selectRateRider(kurs, li_umur_ttg, 0, kode_rider[k], number_rider[k]);
													//premi = uwManager.selectPremiSuperSehat(kurs, li_umur_ttg, kode_rider[k], number_rider[k]);
												}else if(kode_rider[k]==820 && (number_rider[k]>=16)){
													diskon = 0.975;//diskon sebesar 2.5%
													premi = elionsManager.selectRateRider(kurs, li_umur_ttg, 0, kode_rider[k], number_rider[k]);
													//premi = uwManager.selectPremiSuperSehat(kurs, li_umur_ttg, kode_rider[k], number_rider[k]);
													premi = premi * diskon;
												}
												premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara, factor, premi);
												//premi = premi_tampung;
											}
										}else if(products.stableLink(Integer.toString(produk.ii_bisnis_id)) ){
											Double factor=0.0;
											if(mgi==3){
												factor = 0.270;
											}else if(mgi==6){
												factor = 0.525;
											}else if(mgi==1){
												factor = 0.1;
											}else factor = 1.0;
											
											premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara, factor, premi);
										}else if(products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no)){
											if(products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id))){
												premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara, 0.1, premi);
											}else {
												premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara, 1, premi);
											}
										}
										
										//(Deddy) Khusus HCP powersave/ stable save, ditampilkan nilai preminya.
										if(produk1.ii_bisnis_id == 819 ){
											premi_rider[k] = elionsManager.selectRateRider(produk1.is_kurs_id, li_umur_ttg, 0, produk1.ii_bisnis_id, produk1.ii_bisnis_no);
										}
										
										//premi_tahunan[k] = produk1.hit_premi_rider(rate[k], up_sementara, 1, premi);
									//else, premi dikali dari up rider 
									}else if(produk.ii_bisnis_id==182 && ((produk.ii_bisnis_no>=1 && produk.ii_bisnis_no<=6)) ||(produk.ii_bisnis_no>=10 && produk.ii_bisnis_no<=12)){
										if(produk1.ii_bisnis_id == 819){//hcpf8 untuk multi invest syariah
											premi_rider[k] = elionsManager.selectRateRider(produk1.is_kurs_id, li_umur_ttg, 0, produk1.ii_bisnis_id, produk1.ii_bisnis_no);
										}else{//rider term dan CI
											premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi);
										}
									}else if(produk.ii_bisnis_id==183 || produk.ii_bisnis_id==189 || produk.ii_bisnis_id==193 || products.multiInvest(Integer.toString(produk.ii_bisnis_id))){//eka sehat
										Double diskon = 0.0;
										Double premi_tampung = premi;
										if((kode_rider[k]==820 || kode_rider[k]==823 || kode_rider[k]==825) && ((number_rider[k]>=1 && number_rider[k]<=15) || (number_rider[k]>=91 && number_rider[k]<=105))){
											premi = elionsManager.selectRateRider(kurs, li_umur_ttg, 0, kode_rider[k], number_rider[k]);
											//premi = uwManager.selectPremiSuperSehat(kurs, li_umur_ttg, kode_rider[k], number_rider[k]);
											premi = premi * diskon_karyawan * rate_rider;
										}else if((kode_rider[k]==820 || kode_rider[k]==823 || kode_rider[k]==825) && ((number_rider[k]>=16 && number_rider[k]<=90) || (number_rider[k]>=106 && number_rider[k]<=195) )){
											diskon = 0.975;//diskon sebesar 2.5%
											premi = elionsManager.selectRateRider(kurs, li_umur_ttg, 0, kode_rider[k], number_rider[k]);
											//premi = uwManager.selectPremiSuperSehat(kurs, li_umur_ttg, kode_rider[k], number_rider[k]);
											premi = premi * diskon * diskon_karyawan;
										} 
										premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi);
										if(produk1.ii_bisnis_id == 819){//hcpf8 untuk multi invest syariah
											premi_rider[k] = elionsManager.selectRateRider(produk1.is_kurs_id, li_umur_ttg, 0, produk1.ii_bisnis_id, produk1.ii_bisnis_no);
											Integer lscb_id = edit.getDatausulan().getLscb_id();
											Double factor = new Double(1.0);
											if(lscb_id==1){
												factor = 0.27;
											}else if(lscb_id==2){
												factor = 0.525;
											}else if(lscb_id==6){
												factor = 0.1;
											}else factor = 1.0;
											premi_rider[k] = factor.doubleValue() *premi_rider[k];
										}
										//premi = premi_tampung;
									}else if(produk.ii_bisnis_id==178){//smart medicare
										Double premi_tampung = premi;
										if(kode_rider[k]==821 && (number_rider[k]>=16)){
											premi = elionsManager.selectRateRider(kurs, li_umur_ttg, 0, kode_rider[k], number_rider[k]);
										}
										premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi);
										premi = premi_tampung;
									}else if(produk.ii_bisnis_id==169){
										if(kode_rider[k]==810){
											premi_rider[k] =  (up_sementara/1000) * 2;
										}else{
											premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi);
										}
									}else if(!products.unitLink(Integer.toString(produk.ii_bisnis_id))){
										if(produk1.ii_bisnis_id == 823){
											premi_rider[k] = elionsManager.selectRateRider(kurs, li_umur_ttg, 0, kode_rider[k], number_rider[k]);
											premi_rider[k] = premi_rider[k] * rate_rider;
										}else{
											premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi);
										}
									}else if(produk.ii_bisnis_id==191){
										if(produk1.ii_bisnis_id == 811){
											premi_rider[k] = produk1.rate_rider * produk1.idec_pct_list[pmode_id];
										}else if(produk1.ii_bisnis_id == 814){
											Double factor = 1.;
											if(edit.getDatausulan().getLscb_id()==1){
												factor = 4.;
											}else if(edit.getDatausulan().getLscb_id()==2){
												factor = 2.;
											}else if(edit.getDatausulan().getLscb_id()==6){
												factor = 12.;
											}else factor = 1.0;
											Double premi_tahunan = premi * factor;
											premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi_tahunan);
										}else if(produk1.ii_bisnis_id == 823){
											premi_rider[k] = elionsManager.selectRateRider(kurs, li_umur_ttg, 0, kode_rider[k], number_rider[k]);
											premi_rider[k] = premi_rider[k] * rate_rider;
										}else{
											premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi);
										}
									}
									else {
										premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi);
									}
									
									if(kode_rider[k]==822){
										if(number_rider[k]==1){
											premi_rider[k]= 0.0;
										}else if(number_rider[k]==2){
											Integer lscb_id = edit.getDatausulan().getLscb_id();
											Double factor = new Double(1.0);
											if(lscb_id==1){
												factor = 0.27;
											}else if(lscb_id==2){
												factor = 0.525;
											}else if(lscb_id==6){
												factor = 0.1;
											}else factor = 1.0;
											if(products.stableSave(produk.ii_bisnis_id,produk.ii_bisnis_no) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || (produk.ii_bisnis_id==143 && (produk.ii_bisnis_no>=4 && produk.ii_bisnis_no<=6)) ||
											   (produk.ii_bisnis_id==144 && produk.ii_bisnis_no==4) || (produk.ii_bisnis_id==158 && (produk.ii_bisnis_no>=15 && produk.ii_bisnis_no<=16)) ||
											   (kode_produk.intValue() == 145 ||kode_produk.intValue() == 146 || kode_produk.intValue() == 163)){
												if(kurs.equals("01")){
													if(premi<100000000.0){
														premi_rider[k] = factor.doubleValue() * 0.05 * rd.getMspr_tsi();
													}
												}else if(kurs.equals("02")){
													if(premi<10000.0){
														premi_rider[k] = factor.doubleValue() * 0.05 * rd.getMspr_tsi();
													}
												}
											//}else if(products.unitLink(Integer.toString(produk.ii_bisnis_id))){
											}else{
												premi_rider[k] = factor.doubleValue() * 0.05 * rd.getMspr_tsi();
											}
											 
										}
									}
									
									percent[k]=new Integer(0);
			
									Integer tanggal_akhir_bayar1=new Integer(0);
									Integer bulan_akhir_bayar1 = new Integer(0);
									Integer tahun_akhir_bayar1=new Integer(0);
									String tgl_akhir_bayar1 ="";
									String tanggal_1_1="";
									
									Integer tanggal_akhir_polis1=null;
									Integer bulan_akhir_polis1=null;
									Integer tahun_akhir_polis1 = null;
									String tgl_akhir_polis1=null;
									Integer flag_platinumlink = new Integer(produk.flag_platinumlink);
									if (flag_platinumlink == null)
									{
										flag_platinumlink = new Integer(0);
									}
									edit.getDatausulan().setFlag_platinumlink(flag_platinumlink);
									if (produk.flag_platinumlink==1 )
									{
										//rider platinum link tanggal end date berdasarkan cuti premi
										int cuti_premi = edit.getDatausulan().getMspo_installment().intValue();
										produk.wf_set_rider(tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(), cuti_premi,li_umur_ttg.intValue(),li_umur_pp.intValue());
										tanggal_akhir_polis1=new Integer(produk.ldt_edate.getTime().getDate());
										bulan_akhir_polis1=new Integer(produk.ldt_edate.getTime().getMonth()+1);
										tahun_akhir_polis1=new Integer(produk.ldt_edate.getTime().getYear()+1900);
										tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis1.intValue()),2)+"/"+Integer.toString(tahun_akhir_polis1.intValue());
										tgl_awal_rider = new Integer(produk.adt_bdate.getTime().getDate());
										bln_awal_rider = new Integer(produk.adt_bdate.getTime().getMonth()+1);
										thn_awal_rider = new Integer(produk.adt_bdate.getTime().getYear()+1900);
										tanggal_awal_rider = FormatString.rpad("0",Integer.toString(tgl_awal_rider.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bln_awal_rider.intValue()),2)+"/"+Integer.toString(thn_awal_rider.intValue());
										
										if ( number_produk.intValue()==2)
										{
											beg_date_rider[k]=defaultDateFormat.parse(tanggal_awal_rider);
										}
										rd.setMspr_beg_date(beg_date_rider[k]);
										
										if (produk1.ldt_epay!=null)
										{
											tanggal_akhir_bayar1=new Integer(produk.ldt_epay.getTime().getDate());
											bulan_akhir_bayar1=new Integer(produk.ldt_epay.getTime().getMonth()+1);
											tahun_akhir_bayar1=new Integer(produk.ldt_epay.getTime().getYear()+1900);
											tgl_akhir_bayar1=Integer.toString(tanggal_akhir_bayar1.intValue())+"/"+Integer.toString(bulan_akhir_bayar1.intValue())+"/"+Integer.toString(tahun_akhir_bayar1.intValue());
											tanggal_1_1=FormatString.rpad("0",Integer.toString(tanggal_akhir_bayar1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_bayar1.intValue()),2)+"/"+Integer.toString(tahun_akhir_bayar1.intValue())	;					
										}else{
											tgl_akhir_bayar1="";
											tanggal_1_1="";
										}
									
									}else{
										int flag_sementara = produk.flag_jenis_plan;
										if (produk.flag_worksite == 1)
										{
											/*if (produk.flag_medivest == 1)
											{
												flag_sementara = 5;
											}*/
										}
										
										// tanggal end date rider. end pay rider
										produk1.wf_set_premi(tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),pmode_id.intValue(),tahun.intValue(),bulan.intValue(),tanggal.intValue(),ins_period.intValue(),flag_sementara,age.intValue(),pay_period.intValue(),produk.flag_cerdas_siswa, li_umur_pp.intValue(),produk1.ii_bisnis_id,produk1.ii_bisnis_no);
												
										tanggal_akhir_polis1=new Integer(produk1.ldt_edate.getTime().getDate());
										bulan_akhir_polis1=new Integer(produk1.ldt_edate.getTime().getMonth()+1);
										tahun_akhir_polis1=new Integer(produk1.ldt_edate.getTime().getYear()+1900);
										tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis1.intValue()),2)+"/"+Integer.toString(tahun_akhir_polis1.intValue());
										
										if (produk1.ldt_epay!=null)
										{
											tanggal_akhir_bayar1=new Integer(produk1.ldt_epay.getTime().getDate());
											bulan_akhir_bayar1=new Integer(produk1.ldt_epay.getTime().getMonth()+1);
											tahun_akhir_bayar1=new Integer(produk1.ldt_epay.getTime().getYear()+1900);
											tgl_akhir_bayar1=Integer.toString(tanggal_akhir_bayar1.intValue())+"/"+Integer.toString(bulan_akhir_bayar1.intValue())+"/"+Integer.toString(tahun_akhir_bayar1.intValue());
											tanggal_1_1=FormatString.rpad("0",Integer.toString(tanggal_akhir_bayar1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_bayar1.intValue()),2)+"/"+Integer.toString(tahun_akhir_bayar1.intValue())	;					
											/*if (produk.flag_jenis_plan == 6)
											{
												tgl_akhir_bayar1=tgl_akhir_polis1;
												tanggal_1_1=tgl_akhir_polis1;
											}*/
										}else{
											tgl_akhir_bayar1="";
											tanggal_1_1="";
										}
										rd.setMspr_beg_date(tgl_beg_date );
									}
									if(tahun_akhir_polis1>tahun){
										tahun_akhir_polis1=tahun;
										if(bulan_akhir_polis1>bulan){
											bulan_akhir_polis1= bulan;
										}
										tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis1.intValue()),2)+"/"+Integer.toString(tahun_akhir_polis1.intValue());
									}
									
									if(tahun_akhir_bayar1>tahun ){
										tahun_akhir_bayar1=tahun;
										if(bulan_akhir_bayar1>bulan-1){
											bulan_akhir_bayar1= bulan-1;
										}
										tgl_akhir_bayar1=Integer.toString(tanggal_akhir_bayar1.intValue())+"/"+Integer.toString(bulan_akhir_bayar1.intValue())+"/"+Integer.toString(tahun_akhir_bayar1.intValue());
										tanggal_1_1=FormatString.rpad("0",Integer.toString(tanggal_akhir_bayar1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_bayar1.intValue()),2)+"/"+Integer.toString(tahun_akhir_bayar1.intValue())	;	
									}
									
									
									if ( tgl_akhir_polis1.trim().length()!=0)
									{
										rd.setMspr_end_date(defaultDateFormat.parse(tgl_akhir_polis1));
									}else{
										rd.setMspr_end_date(null);
									}
									 if (tgl_akhir_bayar1.trim().length()!=0)
									 {
										 rd.setMspr_end_pay(defaultDateFormat.parse(tgl_akhir_bayar1));
									 }else{
										 rd.setMspr_end_pay(null);
									 }
									 if (produk.flag_platinumlink==1)
									 {
										 int cuti_premi = edit.getDatausulan().getMspo_installment().intValue();
										 rd.setMspr_ins_period(new Integer(produk.li_insured));
									 }else{
										 rd.setMspr_ins_period(new Integer(produk1.li_insured));
										 if((produk.ii_bisnis_id!=183 && produk.ii_bisnis_id!=189 && produk.ii_bisnis_id!=193) && (produk1.ii_bisnis_id==820 || produk1.ii_bisnis_id==823 || produk1.ii_bisnis_id==825)){
											 rd.setMspr_ins_period(new Integer(produk.ii_contract_period));
										 }
									 }
									 if(edit.getPowersave().getMsl_spaj_lama()!=null){
										 String lsbs_id_kopi = uwManager.selectLsbsId(edit.getPowersave().getMsl_spaj_lama());
										 if(products.powerSave(lsbs_id_kopi)){
											 rd.setMspr_beg_date(beg_date_rider[k]);
											 rd.setMspr_end_date(end_date_rider[k]);
										 }
									 }
								}
											
								//(Deddy)hitung up rider berdasarkan nilai up yg diinput user (untuk produk utama powersave, stabil link dan stablesave)
								if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id,produk.ii_bisnis_no)){
									if(produk1.ii_bisnis_id == 813 || produk1.ii_bisnis_id == 818 || produk1.ii_bisnis_id == 822){
										if(produk1.ii_bisnis_id == 813){
											if(produk1.ii_bisnis_no==4 || produk1.ii_bisnis_no==5){
												sum[k]= new Double(produk1.of_get_up(premi.doubleValue(),rd.getMspr_tsi().doubleValue(),unit[k].intValue(),5,kode_rider[k].intValue(),number_rider[k].intValue(),flag_platinum));
											}
										}else if(produk1.ii_bisnis_id == 818){
											if(produk1.ii_bisnis_no==3 || produk1.ii_bisnis_no==4){
												sum[k]= new Double(produk1.of_get_up(premi.doubleValue(),rd.getMspr_tsi().doubleValue(),unit[k].intValue(),produk.flag_jenis_plan,kode_rider[k].intValue(),number_rider[k].intValue(),flag_platinum));
											}
										}else if(produk1.ii_bisnis_id==822){
											sum[k]= new Double(produk1.of_get_up(premi.doubleValue(),rd.getMspr_tsi().doubleValue(),unit[k].intValue(),produk.flag_jenis_plan,kode_rider[k].intValue(),number_rider[k].intValue(),flag_platinum));
										}
									}else sum[k]= new Double(produk1.of_get_up(premi.doubleValue(),up.doubleValue(),unit[k].intValue(),produk.flag_jenis_plan,kode_rider[k].intValue(),number_rider[k].intValue(),flag_platinum));
								}else {
									//hitung up rider
									if(produk1.ii_bisnis_id==822){
										sum[k]= new Double(produk1.of_get_up(premi.doubleValue(),rd.getMspr_tsi().doubleValue(),unit[k].intValue(),produk.flag_jenis_plan,kode_rider[k].intValue(),number_rider[k].intValue(),flag_platinum));
									}else sum[k]= new Double(produk1.of_get_up(premi.doubleValue(),up.doubleValue(),unit[k].intValue(),produk.flag_jenis_plan,kode_rider[k].intValue(),number_rider[k].intValue(),flag_platinum));
								}
								
								sum[k] = produk1.cek_maks_up_rider(sum[k],produk1.is_kurs_id);
								up_pa_rider[k] = new Double(produk1.up_pa);	
								up_pb_rider[k] = new Double(produk1.up_pb);	
								up_pc_rider[k] = new Double(produk1.up_pc);	
								up_pd_rider[k] = new Double(produk1.up_pd);	
								up_pm_rider[k] = new Double(produk1.up_pm);	
								
								//(Deddy)
								if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no) ){
									if((produk1.ii_bisnis_id == 813 && produk1.ii_bisnis_no == 5) || (produk1.ii_bisnis_id == 818 && produk1.ii_bisnis_no == 4)){
										if(edit.getPowersave().getMpr_cara_bayar_rider()!=null){
											edit.getPowersave().setMpr_cara_bayar_rider(edit.getPowersave().getMpr_cara_bayar_rider());
											edit.getDatausulan().setLscb_id_rider(3);
										}else{
//											edit.getPowersave().setMpr_cara_bayar_rider(2);
											edit.getPowersave().setMpr_cara_bayar_rider(0);
										}
									}
								}
								
								//Yusuf - Term Rider, UP nya bisa dirubah, jadi tidak diset
								if(produk1.ii_bisnis_id == 818 && produk1.ii_bisnis_no == 2) {
									if(rd.getMspr_tsi()==0){
										rd.setMspr_tsi(sum[k]);
									}
								}else {
									rd.setMspr_tsi(sum[k]);
								}
								
								rd.setLku_id(kurs);
								rd.setMspr_rate(rate[k]);
	
								if (flag_include[k].intValue()==1)
								{
									rd.setMspr_premium(new Double(0));
									rd.setMspr_persen(new Integer(0));
									rd.setMspr_tt(new Integer(0));
								}else{
										
									if (produk.flag_rider==1)
									{
										if (produk.flag_platinumlink ==0)
										{
											//Yusuf (25/02/2008) Khusus Hidup Bahagia, Dana Sejahtera New dan simas maxi save , ridernya ada preminya!
											if(produk.ii_bisnis_id == 167 || produk.ii_bisnis_id == 163 || produk.ii_bisnis_id==191 || produk.ii_bisnis_id == 188 || produk.ii_bisnis_id==194) { 
												BigDecimal bulat = new BigDecimal(premi_rider[k]);
												bulat = bulat.setScale(0,BigDecimal.ROUND_HALF_UP);
												rd.setMspr_premium(bulat.doubleValue());
											}else {
												rd.setMspr_premium(new Double(0));
											}
											rd.setMspr_persen(percent[k]);
											rd.setMspr_tt(insured[k]);
										}else{
												produk1.count_rate(klases[k].intValue(),unit[k].intValue(),kode_produk.intValue(),number_rider[k].intValue(),kurs,li_umur_ttg.intValue(),li_umur_pp.intValue(),up.doubleValue(),premi.doubleValue(),pmode_id.intValue(),1,ins_period.intValue(),pay_period.intValue());
												String mbu_jml="";
												Double mbu_jumlah=new Double(produk1.mbu_jumlah);
												if(produk.ii_bisnis_id!=166){
													mbu_jumlah = premi_rider[k];
												}
												BigDecimal mbu1 = new BigDecimal(mbu_jumlah.doubleValue());
												mbu1=mbu1.setScale(0,BigDecimal.ROUND_HALF_UP);
												mbu_jumlah=new Double(mbu1.doubleValue());
												mbu_jml=Double.toString(mbu_jumlah.doubleValue());
												premi_rider[k] = mbu_jumlah;
												rd.setMspr_premium(premi_rider[k]);
												rd.setMspr_persen(percent[k]);
												rd.setMspr_tt(insured[k]);		
												
												mbu_jumlah=new Double(produk1.mbu_persen);
												mbu_jumlah= rate[k];
												mbu1 = new BigDecimal(mbu_jumlah.doubleValue());
												mbu1=mbu1.setScale(3,BigDecimal.ROUND_HALF_UP);
												mbu_jumlah=new Double(mbu1.doubleValue());
												mbu_jml=Double.toString(mbu_jumlah.doubleValue());
												rd.setMspr_rate(mbu_jumlah);
										}
									}else{
//										 khusus platinum link tidak ada biaya
										rd.setMspr_premium(premi_rider[k]);
										rd.setMspr_persen(percent[k]);
										rd.setMspr_tt(insured[k]);	
									}
								}
								total_premi_rider=new Double(total_premi_rider.doubleValue() + rd.getMspr_premium().doubleValue());	
								
								rd.setMspr_unit(unit[k]);
								rd.setMspr_class(klases[k]);
								rd.setMspr_tsi_pa_a(up_pa_rider[k]);
								rd.setMspr_tsi_pa_b(up_pb_rider[k]);
								rd.setMspr_tsi_pa_c(up_pc_rider[k]);
								rd.setMspr_tsi_pa_d(up_pd_rider[k]);
								rd.setMspr_tsi_pa_m(up_pm_rider[k]);
								rd.setLsbs_id(kode_rider[k]);
								rd.setLsdbs_number(number_rider[k]);
								rd.setPlan_rider(rider[k]);
								rd.setFlag_include(flag_include[k]);
								rd.setMspr_extra(mspr_extra[k]);
						}	//tutup try produk rider
						
						catch (ClassNotFoundException e)
						{
							logger.error("ERROR :", e);
						} catch (InstantiationException e) {
							logger.error("ERROR :", e);
						} catch (IllegalAccessException e) {
							logger.error("ERROR :", e);
						}	
					}
				}else{
					if (!edit.getPemegang().getReg_spaj().equalsIgnoreCase(""))
					{
						Datarider data_rider = (Datarider) this.elionsManager.selectrider_perkode(edit.getPemegang().getReg_spaj(), Integer.toString(kode_rider[k]));
						if (data_rider != null)
						{
							rd = data_rider;
						}
					}
				}
			}
				edit.getDatausulan().setJumlah_seluruh_rider((jmlrider));
				edit.getDatausulan().setJmlrider_include(new Integer(0));
				edit.getDatausulan().setJmlrider((jmlrider));
				
				
				//RIDER INCLUDE
				/*
				 * define variable untuk menyimpan rider include , sekedar menampilkan saja
				 */
				Integer jm_s  = new Integer(0);		
				//kalau rider include belum diinsert ke model n pertama kali input. pada saat sudah submit tidak dijalankan lagi
			if (( jumlah_flag_include.intValue()!=produk.indeks_rider_include) )
			{
				int flag_sement = produk.flag_jenis_plan;
				edit.getDatausulan().setFlag_jenis_plan(flag_sement );
				if (valid.intValue()==0)
				{		
				if (produk.indeks_rider_include>1)
				{
					jm_s =new Integer(produk.indeks_rider_include);
					if (jm_s.intValue()==1)
					{
						jm_s=new Integer(0);
					}
				}
					jmlh_rider=new Integer(jumlah_r.intValue()+jm_s.intValue());

					edit.getDatausulan().setJumlah_seluruh_rider((jmlh_rider));
					edit.getDatausulan().setJmlrider_include(jm_s);
					edit.getDatausulan().setJmlrider((jmlrider));

						kode_rider_i = new Integer[jmlh_rider.intValue()+1];
						kd_rider_i = new Integer[jmlh_rider.intValue()+1];
						sum_i = new Double[jmlh_rider.intValue()+1];
						rate_i = new Double[jmlh_rider.intValue()+1];
						percent_i = new Integer[jmlh_rider.intValue()+1];
						premi_rider_i = new Double[jmlh_rider.intValue()+1];
						rider_i = new String[jmlh_rider.intValue()+1];
						nama_rider_i = new String[jmlh_rider.intValue()+1];
						up_pa_rider_i = new Double[jmlh_rider.intValue()+1];
						up_pb_rider_i = new Double[jmlh_rider.intValue()+1];	
						up_pc_rider_i = new Double[jmlh_rider.intValue()+1];
						up_pd_rider_i = new Double[jmlh_rider.intValue()+1];	
						up_pm_rider_i = new Double[jmlh_rider.intValue()+1];
						number_rider_i = new Integer[jmlh_rider.intValue()+1];
						end_date_rider_i = new Date[jmlh_rider.intValue()+1];
						beg_date_rider_i = new Date[jmlh_rider.intValue()+1];
						klases_i = new Integer[jmlh_rider.intValue()+1];
						unit_i = new Integer[jmlh_rider.intValue()+1];
						insured_i = new Integer[jmlh_rider.intValue()+1];
						end_pay_rider_i = new Date[jmlh_rider.intValue()+1];
						flag_include_i = new Integer[jmlh_rider.intValue()+1];
						if (jumlah_r.intValue()>0)
						{

							for (int m=0;m<jumlah_r.intValue();m++)
							{

								kode_rider_i[m]=kode_rider[m];

								kd_rider_i[m] = kd_rider[m];
							}
						}
						int nomor_produk_include=0;
						int j_rider=0;
						if (produk.indeks_rider_include>1)
						{
							Integer index = jumlah_r;
							List data_rider_include = new ArrayList();
	
							for (int k=0 ; k <produk.indeks_rider_include; k++)
							{
								Datarider dr1 = new Datarider();
								if (index.intValue() == 0 && k==0)
								{
									index=new Integer(0);
								}else{
									if (index.intValue() > 0 && k==0)
									{
										index=new Integer(index.intValue());
									}else{
										index=new Integer(index.intValue()+1);
										
									}
								}
								Datarider rd1= new Datarider();
	
								kode_rider_i[index.intValue()]=new Integer(produk.rider_include[k]);
								kd_rider_i[index.intValue()]=new Integer(produk.flag_rider_baru);
								
								if ((Integer.toString(kode_rider_i[index.intValue()].intValue())).trim().length()==1)
								{
									nama_rider_i[index.intValue()]="produk_asuransi.n_prod_0"+kode_rider_i[index.intValue()];	
								}else{
									nama_rider_i[index.intValue()]="produk_asuransi.n_prod_"+kode_rider_i[index.intValue()];	
								}
								
								if (!nama_rider_i[index.intValue()].equalsIgnoreCase("produk_asuransi.n_prod_00"))
								{
									//cek rider 
									try{
										Class aClass2 = Class.forName( nama_rider_i[index.intValue()] );
										n_prod produk2 = (n_prod)aClass2.newInstance();
										produk2.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());

										boolean type_rider=false;
										produk.cek_rider_include(number_produk.intValue(),kode_rider_i[index.intValue()].intValue(),li_umur_ttg.intValue(),li_umur_pp.intValue(),up.doubleValue(),premi.doubleValue(),pmode_id.intValue());
										nomor_produk_include=produk.nomor_rider_include;
										//set ke n_prod
										produk2.ii_bisnis_no=nomor_produk_include;
										produk2.ii_bisnis_id=kode_rider_i[index.intValue()].intValue();
										produk2.ii_bisnis_id_utama=kode_rider_i[index.intValue()].intValue();
										produk2.of_set_usia_tt(li_umur_ttg.intValue());
										produk2.of_set_usia_pp(li_umur_pp.intValue());
										produk2.of_set_pmode(pmode_id.intValue());
										produk2.li_lbayar=pay_period.intValue();
										produk2.is_kurs_id=kurs;	
										
										int flag_sementara = produk.flag_jenis_plan;
										edit.getDatausulan().setFlag_jenis_plan(flag_sementara );

										if (produk.flag_worksite == 1)
										{
											if (produk.flag_medivest == 1)
											{
												flag_sementara = 5;
											}
										}
										
										//cari end date  berlaku dan end pay  rider
										produk2.wf_set_premi(tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),pmode_id.intValue(),tahun.intValue(),bulan.intValue(),tanggal.intValue(),ins_period.intValue(),flag_sementara,age.intValue(),pay_period.intValue(),produk.flag_cerdas_siswa, li_umur_pp.intValue(),produk2.ii_bisnis_id,produk2.ii_bisnis_no);
										int tanggal_akhir_polis2=produk2.ldt_edate.getTime().getDate();
										int bulan_akhir_polis2=produk2.ldt_edate.getTime().getMonth()+1;
										int tahun_akhir_polis2=produk2.ldt_edate.getTime().getYear()+1900;
										String tgl_akhir_polis2=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis2),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis2),2)+"/"+Integer.toString(tahun_akhir_polis2);
									
										int tanggal_akhir_bayar2=0;
										int bulan_akhir_bayar2=0;
										int tahun_akhir_bayar2=0;
										String tgl_akhir_bayar2="";
										String tanggal_2_2="";
										if (produk2.ldt_epay!=null)
										{
											tanggal_akhir_bayar2=produk2.ldt_epay.getTime().getDate();
											bulan_akhir_bayar2=produk2.ldt_epay.getTime().getMonth()+1;
											tahun_akhir_bayar2=produk2.ldt_epay.getTime().getYear()+1900;
											tgl_akhir_bayar2=FormatString.rpad("0",Integer.toString(tanggal_akhir_bayar2),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_bayar2),2)+"/"+Integer.toString(tahun_akhir_bayar2);
											tanggal_2_2=FormatString.rpad("0",Integer.toString(tanggal_akhir_bayar2),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_bayar2),2)+"/"+Integer.toString(tahun_akhir_bayar2)	;					
		
										}else{
											tgl_akhir_bayar2=null;
											tanggal_2_2=null;					
										}
										
										//cari up
										sum_i[index.intValue()]=new Double(produk2.of_get_up(premi.doubleValue(),up.doubleValue(),produk.units,produk.flag_jenis_plan,kode_rider_i[index.intValue()].intValue(),nomor_produk_include,flag_platinum));
										sum_i[index.intValue()] = produk2.cek_maks_up_rider(sum_i[index.intValue()],produk2.is_kurs_id);
										up_pa_rider_i[index.intValue()] = new Double(produk2.up_pa);	
										up_pb_rider_i[index.intValue()] = new Double(produk2.up_pb);	
										up_pc_rider_i[index.intValue()] = new Double(produk2.up_pc);	
										up_pd_rider_i[index.intValue()] = new Double(produk2.up_pd);	
										up_pm_rider_i[index.intValue()] = new Double(produk2.up_pm);	
		
										rate_i[index.intValue()]=new Double(produk2.of_get_rate1(produk.klases,produk.flag_jenis_plan,nomor_produk_include,li_umur_ttg.intValue(),li_umur_pp.intValue() ));	
										rider_i[index.intValue()]=kode_rider_i[index.intValue()]+"~X"+Integer.toString(nomor_produk_include);
										
										rd1.setMspr_tt(new Integer(0));
										rd1.setPlan_rider(rider_i[index.intValue()]);
										rd1.setLsbs_id(kode_rider_i[index.intValue()]);
										rd1.setLsdbs_number(new Integer(nomor_produk_include));
										if(produk.ii_bisnis_id==169){
											if(rd1.getLsbs_id()==810){
												premi_rider_i[index.intValue()]=new Double((up.doubleValue()/1000) * 2);
											}
										}else{
											premi_rider_i[index.intValue()]=new Double(produk2.hit_premi_rider(rate_i[index.intValue()].doubleValue(),up.doubleValue(),produk2.idec_pct_list[pmode_id.intValue()],premi.doubleValue()));	
										}
										rd1.setMspr_tsi(sum_i[index.intValue()]);
										rd1.setMspr_unit(new Integer(produk.units));
										rd1.setMspr_class(new Integer(produk.klases));
										rd1.setMspr_ins_period(new Integer(produk2.li_insured));
										//asdasad
										if(edit.getPowersave().getMsl_spaj_lama()!=null){
											String lsbs_id_kopi = uwManager.selectLsbsId(edit.getPowersave().getMsl_spaj_lama());
											if(products.powerSave(lsbs_id_kopi)){
												rd1.setMspr_end_date(edit.getPowersave().getEnddate_topup());
												rd1.setMspr_beg_date(edit.getPowersave().getBegdate_topup());
											}else{
												rd1.setMspr_end_date(defaultDateFormat.parse(tgl_akhir_polis2));
												rd1.setMspr_beg_date(tgl_beg_date );
											}
										}else{
											rd1.setMspr_end_date(defaultDateFormat.parse(tgl_akhir_polis2));
											rd1.setMspr_beg_date(tgl_beg_date );
										}
										if(rd1.getLsbs_id()==822){
											rd1.setMspr_end_pay(null);
										}else{
											rd1.setMspr_end_pay(defaultDateFormat.parse(tgl_akhir_bayar2));
										}
										rd1.setMspr_end_pay(defaultDateFormat.parse(tgl_akhir_bayar2));
										rd1.setLku_id(kurs);
										rd1.setMspr_rate(rate_i[index.intValue()]);
										rd1.setMspr_persen(new Integer(0));
										rd1.setMspr_premium(new Double(0));
										rd1.setMspr_tsi_pa_a(up_pa_rider_i[index.intValue()]);
										rd1.setMspr_tsi_pa_b(up_pb_rider_i[index.intValue()]);
										rd1.setMspr_tsi_pa_c(up_pc_rider_i[index.intValue()]);
										rd1.setMspr_tsi_pa_d(up_pd_rider_i[index.intValue()]);
										rd1.setMspr_tsi_pa_m(up_pm_rider_i[index.intValue()]);
										rd1.setFlag_include(new Integer(1));
										
										rd1.setMspr_tt_include(new Integer(0));
										rd1.setPlan_rider_include(rider_i[index.intValue()]);
										rd1.setLsbs_id_include(kode_rider_i[index.intValue()]);
										rd1.setLsdbs_number_include(new Integer(nomor_produk_include));
										rd1.setMspr_tsi_include(sum_i[index.intValue()]);
										rd1.setMspr_unit_include(new Integer(produk.units));
										rd1.setMspr_class_include(new Integer(produk.klases));
										rd1.setMspr_ins_period_include(new Integer(produk2.li_insured));
										rd1.setMspr_end_date_include(defaultDateFormat.parse(tgl_akhir_polis2));
										rd1.setMspr_end_pay_include(defaultDateFormat.parse(tgl_akhir_bayar2));
										rd1.setMspr_beg_date_include(tgl_beg_date );
										
										rd1.setLku_id_include(kurs);
										rd1.setMspr_rate_include(rate_i[index.intValue()]);
										rd1.setMspr_persen_include(new Integer(0));
										rd1.setMspr_premium_include(new Double(0));
										rd1.setMspr_tsi_pa_a_include(up_pa_rider_i[index.intValue()]);
										rd1.setMspr_tsi_pa_b_include(up_pb_rider_i[index.intValue()]);
										rd1.setMspr_tsi_pa_c_include(up_pc_rider_i[index.intValue()]);
										rd1.setMspr_tsi_pa_d_include(up_pd_rider_i[index.intValue()]);
										rd1.setMspr_tsi_pa_m_include(up_pm_rider_i[index.intValue()]);	
										
										data_rider_include.add(rd1);
								//tutup try produk rider included
									}
									catch (ClassNotFoundException e)
									{
										logger.error("ERROR :", e);
									} catch (InstantiationException e) {
										logger.error("ERROR :", e);
									} catch (IllegalAccessException e) {
										logger.error("ERROR :", e);
									}
								}
							}
							edit.getDatausulan().setDaftaRider(data_rider_include);
						}
						//edit.getDatausulan().setDaftaRider(dtrd1);
						//dtrd = edit.getDatausulan().getDaftaRider();
						
						//pengecekan untuk lsbs_id sama , lsdbs_number tidak boleh ada yang sama
							if (jmlh_rider.intValue()>0)
							{
								for (int r=0 ; r <(jmlh_rider.intValue()); r++)
								{
									for (int w=(r+1); w <=(jmlh_rider.intValue()-1); w++)
									{
										if ((kode_rider_i[r].intValue())==kode_rider_i[w].intValue())
										{
											if ((kode_rider_i[r].intValue() != 819) && (kode_rider_i[r].intValue() != 820))
											{
												hasil_rider="Rider tidak boleh ada yang sama";
												//err.rejectValue("datausulan.daftaRider["+w+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
											}
										}
										if ((kode_rider_i[r].intValue() == 819 && kode_rider_i[w].intValue()==811) ||(kode_rider_i[r].intValue() == 811 && kode_rider_i[w].intValue()==819))
										{
											hasil_rider="Tidak boleh ambil Rider HCP dan HCP Family bersamaan.";
											err.rejectValue("datausulan.daftaRider["+w+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
										}
										if (hasil_rider.trim().length()==0)
										{
											if (kd_rider_i[r].intValue()>0 && kd_rider_i[w].intValue()>0)
											{
												if (kd_rider_i[r].intValue()==kd_rider_i[w].intValue())
												{
													hasil_rider="Rider Critical Illness hanya bisa diambil SATU & Rider TPD hanya bisa diambil SATU";
													err.rejectValue("datausulan.daftaRider["+w+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
												}
											}
										}
										
									}
								}
							}
							
//							if (jmlh_rider.intValue()>0)
//							{//pengecekan apabila rider waiver/payor sudah diambil, tidak bisa ambil rider waiver/payor kembali
//								Integer flagSudahAmbilRiderFreePremi=0;
//								for (int r=0 ; r <(jmlh_rider.intValue()); r++)
//								{
//									if(kode_rider_i[r].intValue() == 814 || kode_rider_i[r].intValue() == 815 || kode_rider_i[r].intValue() == 816 || kode_rider_i[r].intValue() == 817){
//										flagSudahAmbilRiderFreePremi=flagSudahAmbilRiderFreePremi+1;
//									}
//								}
//								if(flagSudahAmbilRiderFreePremi>1){
//									err.rejectValue("datausulan.daftaRider[0].plan_rider","","HALAMAN DATA USULAN : Apabila sudah mengambil rider Waiver/Payor, tidak dapat mengambil rider waiver/payor kembali" );
//								}
//							}
							
						}
				}
			
			List daftaHcp = edit.getDatausulan().getDaftahcp();
			Integer index_hcp = new Integer(0);
			if (daftaHcp != null)
			{
				index_hcp = new Integer(daftaHcp.size());
			}
			Integer banyak_hcp = new Integer(0);
			Boolean hcp_up = false;

			List peserta = edit.getDatausulan().getDaftapeserta();
			Integer jumlah_hcpbasic = new Integer(0);
			if (jmlrider.intValue() > 0)
			{
				Integer index_dtrd = new Integer(0);
				if (dtrd != null)
				{
					index_dtrd = new Integer(dtrd.size());
				}
				
				//pengecekan rider tidak boleh ada  yang sama dalam 1 lsbs_id.
				//validasi hcpf
				for (int i = 0 ; i <index_dtrd.intValue() ; i++)
				{
					Datarider rider1= (Datarider)dtrd.get(i);
					if (rider1.getLsbs_id().intValue() == 819)
					{
						if ((rider1.getLsdbs_number().intValue() >= 1 && rider1.getLsdbs_number().intValue() <= 20) || (rider1.getLsdbs_number().intValue() >= 141 && rider1.getLsdbs_number().intValue() <= 160) || (rider1.getLsdbs_number().intValue() >= 281 && rider1.getLsdbs_number().intValue() <= 300) || (rider1.getLsdbs_number().intValue() >= 381 && rider1.getLsdbs_number().intValue() <= 390))
						{
							jumlah_hcpbasic = new Integer(jumlah_hcpbasic.intValue() + 1);
						}
						for (int j =(i+1); j <=index_dtrd.intValue() - 1 ; j++)
						{
							Datarider rider2= (Datarider)dtrd.get(j);
							if ((rider1.getLsbs_id().intValue() == rider2.getLsbs_id().intValue())&&(rider1.getLsdbs_number().intValue() == rider2.getLsdbs_number().intValue()))
							{
								hasil_rider="Rider tidak boleh ada yang sama";
								err.rejectValue("datausulan.daftaRider["+j+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
							}
	
						}
						if ((rider1.getLsdbs_number().intValue() >=21 && rider1.getLsdbs_number().intValue() <=140) || (rider1.getLsdbs_number().intValue() >=161 && rider1.getLsdbs_number().intValue() <=280) || (rider1.getLsdbs_number().intValue() >=301 && rider1.getLsdbs_number().intValue() <=380) || (rider1.getLsdbs_number().intValue() >=391 && rider1.getLsdbs_number().intValue() <=400))
						{
							int jumlah_sementara =  rider1.getLsdbs_number().intValue() - number_utama.intValue();
							int hasil_mod = jumlah_sementara % 20;
							if (hasil_mod  > 0)
							{
								hasil_rider="Rider HCPF Spouse dan HCPF Child tidak boleh berbeda jenis HCPF R atau HCPF D dengan HCPF Basic ";
								err.rejectValue("datausulan.daftaRider["+i+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
							}
						}
						if (status.equalsIgnoreCase("input") || flag_rider_hcp.intValue() == 0 )
						{
							if ((rider1.getLsbs_id().intValue() == 819) && ((rider1.getLsdbs_number().intValue() >=21 && rider1.getLsdbs_number().intValue() <= 140) || (rider1.getLsdbs_number().intValue() >=161 && rider1.getLsdbs_number().intValue() <= 280) || (rider1.getLsdbs_number().intValue() >=301 && rider1.getLsdbs_number().intValue() <=380) || (rider1.getLsdbs_number().intValue() >=391 && rider1.getLsdbs_number().intValue() <=430)))
							{
								hasil_rider="Rider HCPF SPOUSE dan CHILD ke "+ (i+1)+" tidak bisa dipilih.Silahkan melakukan penambahan di menu HCP FAMILY. ";
								err.rejectValue("datausulan.daftaRider["+i+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
							}
						}
						if ((flag_hcp.intValue() == 0) && ((rider1.getLsdbs_number().intValue() >=21 && rider1.getLsdbs_number().intValue() <= 140) || (rider1.getLsdbs_number().intValue() >=161 && rider1.getLsdbs_number().intValue() <= 280) || (rider1.getLsdbs_number().intValue() >=301 && rider1.getLsdbs_number().intValue() <=380) || (rider1.getLsdbs_number().intValue() >=391 && rider1.getLsdbs_number().intValue() <=430)))
						{
							hasil_rider="Rider HCPF SPOUSE dan CHILD ke "+ (i+1)+" tidak bisa dipilih kalau tidak mengambil HCPF BASIC. ";
							err.rejectValue("datausulan.daftaRider["+i+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
						}
					}
				}
				if (jumlah_hcpbasic.intValue() > 1)
				{
					hasil_rider="Rider HCPF BASIC tidak boleh lebih dari satu. ";
					err.rejectValue("datausulan.daftaRider[0].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
	
				}
	
				List data_sementara = new ArrayList();
				List data_sementara1 = new ArrayList();
				Integer jumlah_hcp = new Integer(0);
				
				if (hasil_rider.trim().length() == 0)
				{	
					if (flag_rider_hcp.intValue() == 0 && cek_flag_hcp.intValue() == 0 )
					{
						// rider semuanya termasuk hcpf
						if (dtrd == null)
						{
							index_dtrd = new Integer(0);
						}else{
							index_dtrd = new Integer(dtrd.size());
						}
						for (int i=0;i<index_dtrd.intValue();i++)
						{
							Datarider rider1= (Datarider)dtrd.get(i);
							Datarider dr =  new Datarider();
							dr = rider1;
							data_sementara.add(dr);
						}
					}else{
						for (int i=0;i<index_dtrd.intValue();i++)
						{
							// rider tanpa hcpf
							Datarider rider1= (Datarider)dtrd.get(i);
							if (rider1.getLsbs_id().intValue() != 819 )
							{
								Datarider dr =  new Datarider();
								dr = rider1;
								data_sementara.add(dr);
							}else{
								jumlah_hcp = new Integer(jumlah_hcp.intValue() + 1);
							}
						}
					}
				}

				// tidak boleh ada perubahan hcpf selain basic
				if (flag_rider_hcp.intValue() == 1)
				{
					List j_peserta = edit.getDatausulan().getDaftapeserta();
					Integer index_peserta = new Integer(0);
					if (j_peserta != null)
					{
						index_peserta = new Integer(j_peserta.size());
					}
					if (index_peserta.intValue() <jumlah_hcp.intValue())
					{
						hasil_rider="Penambahan Rider HCPF selain Basic harus di menu HCP Family, tidak bisa dilakukan di window utama penginputan spaj.Silahkan tambahkan HCPF setelah mensubmit spaj ini. ";
						err.rejectValue("datausulan.daftaRider[0].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
					}
				}
			
				//list hcpf aja
				if (daftaHcp != null)
				{
					index_hcp = new Integer(daftaHcp.size());
				}else{
					index_hcp = new Integer(0);	
				}
				//list rider termasuk hcpf
				if (dtrd == null)
				{
					index_dtrd = new Integer(0);
				}else{
					index_dtrd = new Integer(dtrd.size());
				}
				int selisih = 0;
				
				//membandingkan list hcpf saja dengan list hcpf dalam  list rider semua dengan memisahkan hcpf dalam list semua rider
			if (hasil_rider.trim().length() == 0)
			{		
				edit.getDatausulan().setCek_flag_hcp(new Integer(1));
				if(!status.equals("edit")){
					for (int i=0 ; i < index_hcp.intValue() ; i++ )
					{
						Hcp bf1= (Hcp)daftaHcp.get(i);
						for (int j = 0 ; j <index_dtrd.intValue() ; j ++)
						{
							Datarider rider1= (Datarider)dtrd.get(j);
							if ((rider1.getLsbs_id().intValue() == 819))
							{
								//selisih karena child, spouse lsdbs_number berlaku kelipatan
								if (rider1.getLsdbs_number().intValue()>= bf1.getLsdbs_number().intValue())
								{
									selisih = rider1.getLsdbs_number().intValue() - bf1.getLsdbs_number().intValue();
								}else{
									selisih = bf1.getLsdbs_number().intValue() - rider1.getLsdbs_number().intValue();
								}
	//							set rider ke hcp (kalau ganti plan)
								if ((bf1.getLsdbs_number().intValue()>=1 && bf1.getLsdbs_number().intValue() <=20) || (bf1.getLsdbs_number().intValue()>=141 && bf1.getLsdbs_number().intValue() <=160) || (bf1.getLsdbs_number().intValue() >= 281 && bf1.getLsdbs_number().intValue() <= 300) || (bf1.getLsdbs_number().intValue() >= 381 && bf1.getLsdbs_number().intValue() <= 390))
								{
									if ((rider1.getLsdbs_number().intValue() >= 1 && rider1.getLsdbs_number().intValue() <= 20) || (rider1.getLsdbs_number().intValue() >= 141 && rider1.getLsdbs_number().intValue() <= 160) || (rider1.getLsdbs_number().intValue() >= 281 && rider1.getLsdbs_number().intValue() <= 300) || (rider1.getLsdbs_number().intValue() >= 381 && rider1.getLsdbs_number().intValue() <= 390))//basic
									{
											banyak_hcp = new Integer (banyak_hcp.intValue() + 1);
	
											Hcp data1 = new Hcp();
											Datarider data2 = new Datarider();
											data1 = bf1;
											data1.setLku_id(rider1.getLku_id());
											data1.setLsbs_id(rider1.getLsbs_id());
											data1.setLsdbs_number(rider1.getLsdbs_number());
											data1.setMspr_beg_date(rider1.getMspr_beg_date());
											data1.setMspr_class(rider1.getMspr_class());
											data1.setMspr_end_date(rider1.getMspr_end_date());
											data1.setMspr_end_pay(rider1.getMspr_end_pay());
											data1.setMspr_extra(rider1.getMspr_extra());
											data1.setMspr_ins_period(rider1.getMspr_ins_period());
											data1.setMspr_persen(rider1.getMspr_persen());
											data1.setMspr_premium(rider1.getMspr_premium());
											data1.setMspr_rate(rider1.getMspr_rate());
											data1.setMspr_tsi(rider1.getMspr_tsi());
											data1.setMspr_tsi_pa_a(rider1.getMspr_tsi_pa_a());
											data1.setMspr_tsi_pa_b(rider1.getMspr_tsi_pa_b());
											data1.setMspr_tsi_pa_c(rider1.getMspr_tsi_pa_c());
											data1.setMspr_tsi_pa_d(rider1.getMspr_tsi_pa_d());
											data1.setMspr_tsi_pa_m(rider1.getMspr_tsi_pa_m());
											data1.setMspr_tt(rider1.getMspr_tt());
											data1.setMspr_unit(rider1.getMspr_unit());
											data1.setPlan_rider(rider1.getPlan_rider());
											data_sementara1.add(data1);
											
											data2.setLku_id(rider1.getLku_id());
											data2.setLsbs_id(rider1.getLsbs_id());
											data2.setLsdbs_number(rider1.getLsdbs_number());
											data2.setMspr_beg_date(rider1.getMspr_beg_date());
											data2.setMspr_class(rider1.getMspr_class());
											data2.setMspr_end_date(rider1.getMspr_end_date());
											data2.setMspr_end_pay(rider1.getMspr_end_pay());
											data2.setMspr_extra(rider1.getMspr_extra());
											data2.setMspr_ins_period(rider1.getMspr_ins_period());
											data2.setMspr_persen(rider1.getMspr_persen());
											data2.setMspr_premium(rider1.getMspr_premium());
											data2.setMspr_rate(rider1.getMspr_rate());
											data2.setMspr_tsi(rider1.getMspr_tsi());
											data2.setMspr_tsi_pa_a(rider1.getMspr_tsi_pa_a());
											data2.setMspr_tsi_pa_b(rider1.getMspr_tsi_pa_b());
											data2.setMspr_tsi_pa_c(rider1.getMspr_tsi_pa_c());
											data2.setMspr_tsi_pa_d(rider1.getMspr_tsi_pa_d());
											data2.setMspr_tsi_pa_m(rider1.getMspr_tsi_pa_m());
											data2.setMspr_tt(rider1.getMspr_tt());
											data2.setMspr_unit(rider1.getMspr_unit());
											data2.setPlan_rider(rider1.getPlan_rider());
											data_sementara.add(data2);
											
											j = index_dtrd.intValue();
	
									}
								} else if (((bf1.getLsdbs_number().intValue() >=21) && (bf1.getLsdbs_number().intValue() <=40)) || ((bf1.getLsdbs_number().intValue() >=161) && (bf1.getLsdbs_number().intValue() <=180)) || ((bf1.getLsdbs_number().intValue() >=301) && (bf1.getLsdbs_number().intValue() <=320)) || ((bf1.getLsdbs_number().intValue() >=390) && (bf1.getLsdbs_number().intValue() <=400)))//spouse
										{
									 		if((rider1.getLsdbs_number().intValue() >=21 && rider1.getLsdbs_number().intValue() <=40) || (rider1.getLsdbs_number().intValue() >=161 && rider1.getLsdbs_number().intValue() <=180) || (rider1.getLsdbs_number().intValue() >=301 && rider1.getLsdbs_number().intValue() <=320) || (rider1.getLsdbs_number().intValue() >=390 && rider1.getLsdbs_number().intValue() <=400) )
									 		{
												banyak_hcp = new Integer (banyak_hcp.intValue() + 1);
	
												Hcp data1 = new Hcp();
												Datarider data2 = new Datarider();
												data1 = bf1;
												data1.setLku_id(rider1.getLku_id());
												data1.setLsbs_id(rider1.getLsbs_id());
												data1.setLsdbs_number(rider1.getLsdbs_number());
												data1.setMspr_beg_date(rider1.getMspr_beg_date());
												data1.setMspr_class(rider1.getMspr_class());
												data1.setMspr_end_date(rider1.getMspr_end_date());
												data1.setMspr_end_pay(rider1.getMspr_end_pay());
												data1.setMspr_extra(rider1.getMspr_extra());
												data1.setMspr_ins_period(rider1.getMspr_ins_period());
												data1.setMspr_persen(rider1.getMspr_persen());
												data1.setMspr_premium(rider1.getMspr_premium());
												data1.setMspr_rate(rider1.getMspr_rate());
												data1.setMspr_tsi(rider1.getMspr_tsi());
												data1.setMspr_tsi_pa_a(rider1.getMspr_tsi_pa_a());
												data1.setMspr_tsi_pa_b(rider1.getMspr_tsi_pa_b());
												data1.setMspr_tsi_pa_c(rider1.getMspr_tsi_pa_c());
												data1.setMspr_tsi_pa_d(rider1.getMspr_tsi_pa_d());
												data1.setMspr_tsi_pa_m(rider1.getMspr_tsi_pa_m());
												data1.setMspr_tt(rider1.getMspr_tt());
												data1.setMspr_unit(rider1.getMspr_unit());
												data1.setPlan_rider(rider1.getPlan_rider());
												data_sementara1.add(data1);
												
												data2.setLku_id(rider1.getLku_id());
												data2.setLsbs_id(rider1.getLsbs_id());
												data2.setLsdbs_number(rider1.getLsdbs_number());
												data2.setMspr_beg_date(rider1.getMspr_beg_date());
												data2.setMspr_class(rider1.getMspr_class());
												data2.setMspr_end_date(rider1.getMspr_end_date());
												data2.setMspr_end_pay(rider1.getMspr_end_pay());
												data2.setMspr_extra(rider1.getMspr_extra());
												data2.setMspr_ins_period(rider1.getMspr_ins_period());
												data2.setMspr_persen(rider1.getMspr_persen());
												data2.setMspr_premium(rider1.getMspr_premium());
												data2.setMspr_rate(rider1.getMspr_rate());
												data2.setMspr_tsi(rider1.getMspr_tsi());
												data2.setMspr_tsi_pa_a(rider1.getMspr_tsi_pa_a());
												data2.setMspr_tsi_pa_b(rider1.getMspr_tsi_pa_b());
												data2.setMspr_tsi_pa_c(rider1.getMspr_tsi_pa_c());
												data2.setMspr_tsi_pa_d(rider1.getMspr_tsi_pa_d());
												data2.setMspr_tsi_pa_m(rider1.getMspr_tsi_pa_m());
												data2.setMspr_tt(rider1.getMspr_tt());
												data2.setMspr_unit(rider1.getMspr_unit());
												data2.setPlan_rider(rider1.getPlan_rider());
												data_sementara.add(data2);
												j = index_dtrd.intValue();
											}
										}else if ((bf1.getLsdbs_number().intValue() >= 41 && bf1.getLsdbs_number().intValue() <= 140) || (bf1.getLsdbs_number().intValue() >= 181 && bf1.getLsdbs_number().intValue() <= 280) || (bf1.getLsdbs_number().intValue() >= 321 && bf1.getLsdbs_number().intValue() <= 380) || (bf1.getLsdbs_number().intValue() >= 401 && bf1.getLsdbs_number().intValue() <= 430))//child
											{
												if ((rider1.getLsdbs_number().intValue() >= 41 && rider1.getLsdbs_number().intValue() <= 140) || (rider1.getLsdbs_number().intValue() >= 181 && rider1.getLsdbs_number().intValue() <= 280) || (rider1.getLsdbs_number().intValue() >= 321 && rider1.getLsdbs_number().intValue() <= 380) || (rider1.getLsdbs_number().intValue() >= 401 && rider1.getLsdbs_number().intValue() <= 430))
												{
													if (selisih <=19)
													{
														banyak_hcp = new Integer (banyak_hcp.intValue() + 1);
		
														Hcp data1 = new Hcp();
														Datarider data2 = new Datarider();
														data1 = bf1;
														data1.setLku_id(rider1.getLku_id());
														data1.setLsbs_id(rider1.getLsbs_id());
														data1.setLsdbs_number(rider1.getLsdbs_number());
														data1.setMspr_beg_date(rider1.getMspr_beg_date());
														data1.setMspr_class(rider1.getMspr_class());
														data1.setMspr_end_date(rider1.getMspr_end_date());
														data1.setMspr_end_pay(rider1.getMspr_end_pay());
														data1.setMspr_extra(rider1.getMspr_extra());
														data1.setMspr_ins_period(rider1.getMspr_ins_period());
														data1.setMspr_persen(rider1.getMspr_persen());
														data1.setMspr_premium(rider1.getMspr_premium());
														data1.setMspr_rate(rider1.getMspr_rate());
														data1.setMspr_tsi(rider1.getMspr_tsi());
														data1.setMspr_tsi_pa_a(rider1.getMspr_tsi_pa_a());
														data1.setMspr_tsi_pa_b(rider1.getMspr_tsi_pa_b());
														data1.setMspr_tsi_pa_c(rider1.getMspr_tsi_pa_c());
														data1.setMspr_tsi_pa_d(rider1.getMspr_tsi_pa_d());
														data1.setMspr_tsi_pa_m(rider1.getMspr_tsi_pa_m());
														data1.setMspr_tt(rider1.getMspr_tt());
														data1.setMspr_unit(rider1.getMspr_unit());
														data1.setPlan_rider(rider1.getPlan_rider());
														data_sementara1.add(data1);
														
														data2.setLku_id(rider1.getLku_id());
														data2.setLsbs_id(rider1.getLsbs_id());
														data2.setLsdbs_number(rider1.getLsdbs_number());
														data2.setMspr_beg_date(rider1.getMspr_beg_date());
														data2.setMspr_class(rider1.getMspr_class());
														data2.setMspr_end_date(rider1.getMspr_end_date());
														data2.setMspr_end_pay(rider1.getMspr_end_pay());
														data2.setMspr_extra(rider1.getMspr_extra());
														data2.setMspr_ins_period(rider1.getMspr_ins_period());
														data2.setMspr_persen(rider1.getMspr_persen());
														data2.setMspr_premium(rider1.getMspr_premium());
														data2.setMspr_rate(rider1.getMspr_rate());
														data2.setMspr_tsi(rider1.getMspr_tsi());
														data2.setMspr_tsi_pa_a(rider1.getMspr_tsi_pa_a());
														data2.setMspr_tsi_pa_b(rider1.getMspr_tsi_pa_b());
														data2.setMspr_tsi_pa_c(rider1.getMspr_tsi_pa_c());
														data2.setMspr_tsi_pa_d(rider1.getMspr_tsi_pa_d());
														data2.setMspr_tsi_pa_m(rider1.getMspr_tsi_pa_m());
														data2.setMspr_tt(rider1.getMspr_tt());
														data2.setMspr_unit(rider1.getMspr_unit());
														data2.setPlan_rider(rider1.getPlan_rider());
														data_sementara.add(data2);
														j = index_dtrd.intValue();
													}
												}
											}
							}
						}
					}
				}
				
				//bandingkan list hcpf hasil pemisahan list rider semuanya, dengan list hcpf aja
				Integer index_sementara = new Integer(0);
				if (data_sementara1 != null)
				{
					index_sementara = new Integer(data_sementara1.size());
				}
				if (index_sementara.intValue() != index_hcp)
				{
					hasil_rider="Perubahan tipe HCPF Child harus sama child ke nya untuk memudahkan pengupdatean. ";
					//err.rejectValue("datausulan.daftaRider[0].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);

				}

				// pengabungan list hcpf aja dengan list selain hcpf dalam array baru
				if (hasil_rider.trim().length() == 0)
				{
				
					edit.getDatausulan().setDaftahcp(data_sementara1);
					edit.getDatausulan().setDaftaRider(data_sementara);
			
					if (dtrd == null)
					{
						index_dtrd = new Integer(0);
					}else{
						index_dtrd = new Integer(dtrd.size());
					}
					if (flag_rider_hcp.intValue() == 0 && cek_flag_hcp.intValue() == 0)
					{
						for (int i = 0 ; i < index_dtrd.intValue() ; i++)
						{
							Datarider rider1= (Datarider)dtrd.get(i);
							if (rider1.getLsbs_id().intValue() == 819)
							{
								Hcp data1 = new Hcp();
								Datarider data2 = new Datarider();
								data1.setLku_id(rider1.getLku_id());
								data1.setLsbs_id(rider1.getLsbs_id());
								data1.setLsdbs_number(rider1.getLsdbs_number());
								data1.setMspr_beg_date(rider1.getMspr_beg_date());
								data1.setMspr_class(rider1.getMspr_class());
								data1.setMspr_end_date(rider1.getMspr_end_date());
								data1.setMspr_end_pay(rider1.getMspr_end_pay());
								data1.setMspr_extra(rider1.getMspr_extra());
								data1.setMspr_ins_period(rider1.getMspr_ins_period());
								data1.setMspr_persen(rider1.getMspr_persen());
								data1.setMspr_premium(rider1.getMspr_premium());
								data1.setMspr_rate(rider1.getMspr_rate());
								data1.setMspr_tsi(rider1.getMspr_tsi());
								data1.setMspr_tsi_pa_a(rider1.getMspr_tsi_pa_a());
								data1.setMspr_tsi_pa_b(rider1.getMspr_tsi_pa_b());
								data1.setMspr_tsi_pa_c(rider1.getMspr_tsi_pa_c());
								data1.setMspr_tsi_pa_d(rider1.getMspr_tsi_pa_d());
								data1.setMspr_tsi_pa_m(rider1.getMspr_tsi_pa_m());
								data1.setMspr_tt(rider1.getMspr_tt());
								data1.setMspr_unit(rider1.getMspr_unit());
								data1.setPlan_rider(rider1.getPlan_rider());
								
								data1.setDiscount(new Double(0));
								data1.setKelamin(edit.getTertanggung().getMspe_sex());
								data1.setLsre_id(edit.getTertanggung().getLsre_id());
								data1.setNama(edit.getTertanggung().getMcl_first());
								data1.setNo_urut(new Integer(1));
								data1.setPlan_rider(rider1.getPlan_rider());
								data1.setPremi(rider1.getMspr_premium());
								data1.setReg_spaj(edit.getTertanggung().getReg_spaj());
								data1.setTgl_lahir(edit.getTertanggung().getMspe_date_birth());
								data1.setUmur(edit.getTertanggung().getMste_age());
								
								data_sementara1.add(data1);
							}
							
						}
						edit.getDatausulan().setDaftahcp(data_sementara1);
					}
						
					//pengesetan  khusus produk simas
					List data_sementara2 = new ArrayList();
					Integer index_peserta  = new Integer(0);
					if (peserta == null)
					{
						index_peserta = new Integer(0);
					}else{
						index_peserta = new Integer(peserta.size());
					}
					for (int i = 0 ; i < index_hcp.intValue() ; i++)
					{
						Hcp bf1= (Hcp)daftaHcp.get(i);
						
						for (int j = 0 ; j <index_peserta.intValue() ; j ++)
						{
							//set hcp ke peserta ( kalau ganti plan)
							Simas simas= (Simas)peserta.get(j);
							if (bf1.getNo_urut().intValue() == simas.getNo_urut().intValue())
							{
								Simas data1 = new Simas();
								data1.setDiscount(bf1.getDiscount());
								data1.setKelamin(bf1.getKelamin());
								data1.setLsbs_id(bf1.getLsbs_id());
								data1.setLsdbs_number(bf1.getLsdbs_number());
								data1.setLsre_id(bf1.getLsre_id());
								data1.setNama(bf1.getNama());
								data1.setNo_urut(bf1.getNo_urut());
								data1.setPlan_rider(bf1.getPlan_rider());
								data1.setPremi(bf1.getPremi());
								data1.setReg_spaj(bf1.getReg_spaj());
								data1.setTgl_lahir(bf1.getTgl_lahir());
								data1.setUmur(bf1.getUmur());
								data_sementara2.add(data1);
							}
						}
					}
					edit.getDatausulan().setDaftapeserta(data_sementara2);
	
					//hitung ulang umur peserta
					if (flag_rider_hcp.intValue() == 1)
					{
						Map data_rider = this.elionsManager.validbac(edit, err, "hcp","windowutama");
					}
				}
			}
		}
			
		if (flag_simas.intValue() == 1)
		{
			Map data_rider = this.elionsManager.validbac(edit, err, "simas","windowutama");
		}
		
		if(kode_flag.intValue()>1)
		{
			// link
			total_premi_sementara=new Double(total_premi_rider.doubleValue()+premi.doubleValue() + total_premi_tu.doubleValue());
			edit.getDatausulan().setTotal_premi_rider((total_premi_rider));
			edit.getDatausulan().setTotal_premi_sementara((total_premi_sementara));
			edit.getInvestasiutama().setTotal_premi_sementara(total_premi_sementara);
		}else{
			//selain link
			edit.getDatausulan().setTotal_premi_rider(new Double(0));
			edit.getDatausulan().setTotal_premi_sementara(new Double(0));
			edit.getInvestasiutama().setTotal_premi_sementara(new Double(0));
		}
		}catch (ClassNotFoundException e)
		{
			logger.error("ERROR :", e);
		} catch (InstantiationException e) {
			logger.error("ERROR :", e);
		} catch (IllegalAccessException e) {
			logger.error("ERROR :", e);
		}
	}

			String spaj = edit.getPemegang().getReg_spaj();
			if (spaj == null)
			{
				spaj ="";
			}
			//cek no blanko sudah pernah dipakai  atau belum
			if (!spaj.equalsIgnoreCase(""))
			{
				Integer kode = edit.getDatausulan().getLsbs_id();
				Integer number= edit.getDatausulan().getLsdbs_number();
				String no_blanko = edit.getPemegang().getMspo_no_blanko();
				if (no_blanko == null)
				{
					no_blanko ="-";
				}
				if (no_blanko.equalsIgnoreCase(""))
				{
					no_blanko="-";
				}
				Integer jumlah_blanko = (Integer) this.elionsManager.count_no_blanko_perspaj(Integer.toString(kode), Integer.toString(number), no_blanko, spaj);
				if (jumlah_blanko == null)
				{
					jumlah_blanko = new Integer(0);
				}
				if (jumlah_blanko.intValue() > 0)
				{
					edit.getPemegang().setCek_blanko(new Integer(1));
				}else{
					edit.getPemegang().setCek_blanko(new Integer(0));
				}
				
				String ktp = edit.getPemegang().getMspe_no_identity();
				if (ktp == null)
				{
					ktp ="";
				}
				String lsbs_id = edit.getDatausulan().getLsbs_id().toString();
				if (lsbs_id== null)
				{
					lsbs_id ="";
				}
				String lsdbs_number = edit.getDatausulan().getLsdbs_number().toString();
				if (lsdbs_number == null)
				{
					lsdbs_number = "";
				}
				up = edit.getDatausulan().getMspr_tsi();
				if (up ==null)
				{
					up = new Double(0);
				}
				
				//cek ktp sama (orang yang sama)
				Integer jumlah_ktp = (Integer) this.elionsManager.cek_data_sama(spaj, ktp, lsbs_id, lsdbs_number, up);
				if (jumlah_ktp.intValue() > 0)
				{
					edit.getDatausulan().setFlag_ktp(new Integer(1));
					List data_spaj = this.elionsManager.cek_spaj_sama1(spaj, ktp, lsbs_id, lsdbs_number, up);
					if (data_spaj != null)
					{
						Map m = (Map)data_spaj.get(0);
						edit.getDatausulan().setSpaj_ktp(m.get("REG_SPAJ").toString());
					}else{
						edit.getDatausulan().setSpaj_ktp("");
					}
				}else{
					edit.getDatausulan().setFlag_ktp(new Integer(0));
					edit.getDatausulan().setSpaj_ktp("");
				}	
				

			}else{
				Integer kode = edit.getDatausulan().getLsbs_id();
				Integer number= edit.getDatausulan().getLsdbs_number();
				String no_blanko = edit.getPemegang().getMspo_no_blanko();
				if (no_blanko == null)
				{
					no_blanko ="-";
				}
				if (no_blanko.equalsIgnoreCase(""))
				{
					no_blanko="-";
				}
				Integer jumlah_blanko = (Integer) this.elionsManager.count_no_blanko(Integer.toString(kode), Integer.toString(number), no_blanko);
				if (jumlah_blanko == null)
				{
					jumlah_blanko = new Integer(0);
				}
				if (jumlah_blanko.intValue() > 0)
				{
					edit.getPemegang().setCek_blanko(new Integer(1));
				}else{
					edit.getPemegang().setCek_blanko(new Integer(0));
				}
				
				String ktp = edit.getPemegang().getMspe_no_identity();
				if (ktp == null)
				{
					ktp ="";
				}
				String lsbs_id = edit.getDatausulan().getLsbs_id().toString();
				if (lsbs_id== null)
				{
					lsbs_id ="";
				}
				String lsdbs_number = edit.getDatausulan().getLsdbs_number().toString();
				if (lsdbs_number == null)
				{
					lsdbs_number = "";
				}
				up = edit.getDatausulan().getMspr_tsi();
				if (up ==null)
				{
					up = new Double(0);
				}
				Integer jumlah_ktp = (Integer) this.elionsManager.cek_data_baru_sama(ktp, lsbs_id, lsdbs_number, up);
				if (jumlah_ktp.intValue() > 0)
				{
					edit.getDatausulan().setFlag_ktp(new Integer(1));
					List data_spaj = this.elionsManager.cek_spaj_sama(ktp, lsbs_id, lsdbs_number, up);
					if (data_spaj != null)
					{
						Map m = (Map)data_spaj.get(0);
						edit.getDatausulan().setSpaj_ktp(m.get("REG_SPAJ").toString());					
					}else{
						edit.getDatausulan().setSpaj_ktp("");
					}
				}else{
					edit.getDatausulan().setFlag_ktp(new Integer(0));
					edit.getDatausulan().setSpaj_ktp("");
				}
				
				Integer flag_uppremi = edit.getDatausulan().getFlag_uppremi();
				if (flag_uppremi.intValue()==0)
				{
					Date tgl_lahir_ttg = edit.getTertanggung().getMspe_date_birth();
					String tgl = (Integer.toString(tgl_lahir_ttg.getYear()+1900))+(FormatString.rpad("0",Integer.toString(tgl_lahir_ttg.getMonth()+1),2))+(FormatString.rpad("0",Integer.toString(tgl_lahir_ttg.getDate()),2));
					String nama = edit.getTertanggung().getMcl_first();
					Double p = edit.getDatausulan().getMspr_tsi();
					if (spaj.equalsIgnoreCase(""))
					{
						Integer jumlah_dobel = (Integer) this.elionsManager.cek_polis_dobel_tsi(nama, tgl, lsbs_id, lsdbs_number, p);
						if (jumlah_dobel.intValue() > 0)
						{
							edit.getDatausulan().setPolis_dobel(new Integer(1));
							
						}else{
							edit.getDatausulan().setPolis_dobel(new Integer(0));
						}
					}
				}else{
					Date tgl_lahir_ttg = edit.getTertanggung().getMspe_date_birth();
					String tgl = (Integer.toString(tgl_lahir_ttg.getYear()+1900))+(FormatString.rpad("0",Integer.toString(tgl_lahir_ttg.getMonth()+1),2))+(FormatString.rpad("0",Integer.toString(tgl_lahir_ttg.getDate()),2));
					String nama = edit.getTertanggung().getMcl_first();
//					String site = edit.getAgen().getLca_id().toString().concat(edit.getAgen().getLwk_id().toString().concat(edit.getAgen().getLsrg_id().toString()));
					
					if (spaj.equalsIgnoreCase(""))
					{
						Integer jumlah_dobel = (Integer) this.elionsManager.cek_polis_dobel(nama, tgl, lsbs_id, lsdbs_number, premi,null);
						if (jumlah_dobel.intValue() > 0)
						{
							edit.getDatausulan().setPolis_dobel(new Integer(1));
							
						}else{
							edit.getDatausulan().setPolis_dobel(new Integer(0));
						}
					}
				}
			}
		
		if (!(status.equalsIgnoreCase("input")) && hasil_rider.trim().length() == 0)
		{
			if( edit.getDatausulan().getIndeks_validasi().intValue()==0)
			{
				Double premi_tunggal = new Double(0);
				premi_tunggal = edit.getInvestasiutama().getDaftartopup().getPremi_tunggal();
				if (premi_tunggal == null)
				{
					premi_tunggal = new Double(0);
					edit.getInvestasiutama().getDaftartopup().setPremi_tunggal(premi_tunggal);
				}
				Integer pil_tunggal = edit.getInvestasiutama().getDaftartopup().getPil_tunggal();
				if (pil_tunggal == null)
				{
					pil_tunggal = new Integer(0);
					edit.getInvestasiutama().getDaftartopup().setPil_tunggal(pil_tunggal);
				}
				
				Double premi_berkala = new Double(0);
				premi_berkala = edit.getInvestasiutama().getDaftartopup().getPremi_berkala();
				if (premi_berkala == null)
				{
					premi_berkala = new Double(0);
					edit.getInvestasiutama().getDaftartopup().setPremi_berkala(premi_berkala);
				}
				Integer pil_berkala = new Integer(0);
				pil_berkala = edit.getInvestasiutama().getDaftartopup().getPil_berkala();
				if (pil_berkala == null)
				{
					pil_berkala = new Integer(0);
					edit.getInvestasiutama().getDaftartopup().setPil_berkala(pil_berkala);
				}
				
				// hitung biaya, minus, fund
				if (kode_flag.intValue() == 1  || kode_flag.intValue()== 11  || kode_flag.intValue()==15)
				{
					Integer flag_bulanan = edit.getDatausulan().getFlag_bulanan();
					if(status.equals("edit")){
						edit.getPowersave().setMsl_spaj_lama(uwManager.selectCekSpajSebelumSurrender(edit.getDatausulan().getReg_spaj()));
					}
//					Map data1 = this.elionsManager.hitbac(cmd,err,kurs,up,flag_account ,Common.isEmpty(edit.getAgen().getLca_id())?lca_id:edit.getAgen().getLca_id() ,autodebet,flag_bao1, null,null,null,premi,null, null,null,null, null, null ,null,null,null,flag_powersave,flag_bulanan ,"bungapowersave");
					edit.getInvestasiutama().setJmlh_biaya(new Integer(0));
					edit.getInvestasiutama().setDaftarbiaya(new ArrayList());
				}else if (kode_flag.intValue()>1 && kode_flag.intValue()!= 11 && kode_flag.intValue()!=15)
					{
						Map data2 = null;
						if (flag_hcp.intValue() == 0 || flag_rider_hcp.intValue() == 0)
						{
							data2 = this.elionsManager.hitbac(cmd,err,kurs,up,null ,null ,null,null, kode_flag,flag_rider,pmode_id,premi,premi_berkala,premi_tunggal, flag_as,pil_berkala,pil_tunggal,null,null,null ,null,null,null,"biayaexcell");
						}else{
							data2 = this.elionsManager.hitbac(cmd,err,kurs,up,null ,null ,null,null, kode_flag,flag_rider,pmode_id,premi,premi_berkala,premi_tunggal, flag_as,pil_berkala,pil_tunggal,null,null,null ,null,null,null,"hcp");
						}
						Double ld_premi_invest = (Double)data2.get("ld_premi_invest");
						Boolean flag_minus1 = (Boolean)data2.get("flag_minus");
						Double jumlah_minus = (Double)data2.get("jumlah_minus");
						Map data3 = this.elionsManager.hitbac(cmd,err,null,null,null ,null ,null,null, kode_flag,null,null,null,premi_berkala,premi_tunggal,null,null,null,li_pct_biaya,ld_premi_invest,flag_minus1,jumlah_minus,null,null,"fundexcell1");
					}else{
						edit.getInvestasiutama().setJmlh_biaya(new Integer(0));
						edit.getInvestasiutama().setDaftarbiaya(new ArrayList());
					}
			}
		}else{
			if (flag_powersave.intValue() == 6)
			{
				if (edit.getPowersave().getMps_roll_over()==null)
				{
					edit.getPowersave().setMps_roll_over(new Integer(3));
				}
			}
		}
		
		String kurs_rek = edit.getRekening_client().getMrc_kurs();
		if (kurs_rek == null)
		{
			kurs_rek ="";
		}
		if (kurs_rek.equalsIgnoreCase(""))
		{
			String kurs_utama = edit.getDatausulan().getKurs_p();
			kurs_rek = kurs_utama;
		}
		edit.getRekening_client().setMrc_kurs(kurs_rek);
		
		if(products.powerSave(edit.getDatausulan().getLsbs_id().toString()) ||
				products.stableLink(edit.getDatausulan().getLsbs_id().toString()) ||
				products.stableSave(edit.getDatausulan().getLsbs_id(), edit.getDatausulan().getLsdbs_number()) ||
				products.stableSavePremiBulanan(edit.getDatausulan().getLsbs_id().toString())){
			edit.getDatausulan().setFlag_paymode_rider(1);
		}
		
		if(edit.getDatausulan().getLsbs_id().intValue()==186&& edit.getDatausulan().getLsdbs_number().intValue()==3){
			edit.getDatausulan().setMspr_ins_period(5);
			edit.getDatausulan().setMspo_pay_period(5);
		}
		//logger.info(edit.getPowersave().getMsl_employee());
		if(!Common.isEmpty(edit.getPowersave().getMsl_employee())){  			
			if(edit.getPowersave().getMsl_employee()==1){
				logger.info(edit.getPowersave().getMsl_employee());
				edit.getPowersave().setMps_employee(1);
				logger.info(edit.getPowersave().getMsl_employee());
			}
		}
		
	} catch (ParseException e) {
		logger.error("ERROR :", e);
	}
	
}

//------------investasi ----------------------------------
	public void validateinvestasi(Object cmd, Errors err)  throws ServletException,IOException,Exception
	{
		logger.debug("EditBacValidator : validate page validateinvestasi");
		Cmdeditbac edit= (Cmdeditbac) cmd;
		form_investasi a =new form_investasi();
		f_hit_umur umr= new f_hit_umur();

		Integer kode_flag=new Integer(0);
		
		Double total_premi_sementara=new Double(0);
			
		Integer jns_top_up_tunggal=new Integer(0);
		Integer jns_top_up_berkala=new Integer(0);
		Double topup_tunggal=new Double(0);
		Double topup_berkala=new Double(0);
		String hasil_topup1="";
		
		String hasil_jns_top_up="";
		String nama_produk="";
		Double premi1=new Double(0);
		String total_premium="";
		Double total_premium1=new Double(0);
		String hasil_total_premium="";
		Integer flag_powersave = new Integer(0);
		boolean flag_minus = false;
		Double jumlah_minus = new Double(0);
		Double ld_premi_invest=new Double(0);
		form_data_usulan form_b =new form_data_usulan();
		form_b.setProducts(products);
		
		String kurs=edit.getDatausulan().getLku_id();
		if (kurs==null)
		{
			kurs="";
		}
		Integer pmode=edit.getDatausulan().getLscb_id();
		Double up=edit.getDatausulan().getMspr_tsi();	
		if (up==null)
		{
			new Double(0);
		}
		Double premi=edit.getDatausulan().getMspr_premium();
		if (premi==null)
		{
			premi=new Double(0);
		}
		Integer kode_produk= edit.getDatausulan().getLsbs_id();
		Integer number_produk=edit.getDatausulan().getLsdbs_number();

		String lca_id = edit.getPemegang().getCbg_lus_id();
		if (lca_id.trim().length()==1)
		{
			lca_id="0"+lca_id;
		}
		edit.getPemegang().setLca_id(lca_id);

		Double total_rider_sementara=edit.getDatausulan().getTotal_premi_rider();
		if (total_rider_sementara==null)
		{
			total_rider_sementara= new Double(0);
		}
		
		edit.getDatausulan().setCara_premi(new  Integer(0));
		edit.getDatausulan().setTotal_premi_kombinasi(new Double(0));
		
		
		//yusuf - stable link
		if(products.stableLink(String.valueOf(kode_produk))) {
			if(kode_produk==186){
				edit.getDatausulan().setCara_premi(0);
			}else{
				edit.getDatausulan().setCara_premi(1);
			}
			
			if(edit.getInvestasiutama().getDaftartopup().getPremi_tunggal()==null){
				edit.getInvestasiutama().getDaftartopup().setPremi_tunggal(new  Double(0));
			}
			
			edit.getDatausulan().setTotal_premi_kombinasi(
					edit.getDatausulan().getMspr_premium() + 
					edit.getInvestasiutama().getDaftartopup().getPremi_tunggal());
		}
		
		if(kode_produk.intValue()==121){
			edit.getDatausulan().setCara_premi(1);
			edit.getDatausulan().setTotal_premi_kombinasi(
					edit.getDatausulan().getMspr_premium() + 
					edit.getInvestasiutama().getDaftartopup().getPremi_berkala());
		}else{
			edit.getDatausulan().setKombinasi("");
		}
		
		if (Integer.toString(kode_produk.intValue()).trim().length()==1)
		{
			nama_produk="produk_asuransi.n_prod_0"+kode_produk;	
		}else{
			nama_produk="produk_asuransi.n_prod_"+kode_produk;	
		}
		
		try{
			
			Class aClass = Class.forName( nama_produk );
			n_prod produk = (n_prod)aClass.newInstance();
			produk.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
			
			produk.cek_flag_agen(kode_produk.intValue(),number_produk.intValue(), edit.getPowersave().getFlag_bulanan());
			
			Integer flag_account = new Integer(produk.flag_account);
			
			Integer flag_rider = new Integer(produk.flag_rider);
			Boolean flag_bao=new Boolean(produk.isProductBancass);
			Integer flag_ekalink = new Integer(produk.flag_ekalink);
			edit.getDatausulan().setFlag_ekalink(flag_ekalink);
			

			
			Integer flag_bulanan = new Integer(produk.flag_powersavebulanan);
			edit.getDatausulan().setFlag_bulanan(flag_bulanan);
			
			Integer flag_bao1=new Integer(0);
			if (flag_bao.booleanValue()==true)
			{
				flag_bao1=new Integer(1);
			}else{
				flag_bao1=new Integer(0);
			}
			
			produk.ii_bisnis_no=number_produk.intValue();
			produk.ii_bisnis_id=kode_produk.intValue();
			
			
			produk.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
			flag_powersave = new Integer(produk.of_get_bisnis_no(edit.getPowersave().getFlag_bulanan()));
			if (produk.flag_powersavebulanan == 1)
			{
				produk.cek_flag_agen(kode_produk.intValue(), number_produk.intValue(), edit.getPowersave().getFlag_bulanan());
				flag_powersave = new Integer(produk.flag_powersave);
			}
			edit.getPowersave().setFlag_powersave(flag_powersave);
			produk.of_set_kurs(kurs);
			edit.getDatausulan().setKurs_premi(kurs);
			edit.getDatausulan().setKurs_p(kurs);
			kode_flag=new Integer(produk.kode_flag);
			Integer flag_as = new Integer(produk.flag_as);
			if (flag_as.intValue() == 2)
			{
				edit.getEmployee().setTgl_proses(edit.getDatausulan().getMste_beg_date());
			}
			String bunga_simponi="";
			Double bonus_tahapan = null;
			
			//(Deddy)dicomment dulu
//			if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(Integer.toString(produk.ii_bisnis_id)) ){
//				if(edit.getPowersave().getMpr_cara_bayar_rider()==null){
//					edit.getPowersave().setMpr_cara_bayar_rider(0);
//				}
//			}
			
			if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no)){
				if(edit.getDatausulan().getDaftaRider().size()!=0){
					edit.getPowersave().setFlag_rider(1);//Ada Rider
				}else edit.getPowersave().setFlag_rider(0);//Non Rider
			}
			
			Double tot_rider = 0.0;
			if(edit.getDatausulan().getDaftaRider().size()!=0){
				
				List<Datarider> xx = edit.getDatausulan().getDaftaRider();
				for(Datarider datarider : xx){
					tot_rider =tot_rider + datarider.getMspr_premium();
				}
			edit.getDatausulan().setTotal_premi_rider(tot_rider);
				
			}
			
			//(Deddy)-cara bayar rider untuk powersave stabil link dan stablesave
			if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no)) {
				String jenis_produk = "";
				if(products.powerSave(Integer.toString(produk.ii_bisnis_id))){
					jenis_produk = "PowerSave";
					if((produk.ii_bisnis_id==158 && (produk.ii_bisnis_no==13 || produk.ii_bisnis_no==15 || produk.ii_bisnis_no==16))){
						jenis_produk = "StableSave";
					}
				}else if(products.stableLink(Integer.toString(produk.ii_bisnis_id))){
					if(produk.ii_bisnis_id==186){
						jenis_produk = "ProgressiveLink";
					}else{
						jenis_produk = "StabilLink";
					}
				}else if(products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no)){
					jenis_produk = "StableSave";
				}
				if( edit.getDatausulan().getDaftaRider().size() != 0){
					List<Datarider> xx = edit.getDatausulan().getDaftaRider();
					if(edit.getPowersave().getMpr_cara_bayar_rider()==null){
						for(Datarider datarider : xx){
							if(datarider.getLsbs_id()==822 && datarider.getLsdbs_number()==1){
								
							}else{
								err.rejectValue("powersave.mpr_cara_bayar_rider","","HALAMAN INVESTASI : Untuk Produk "+jenis_produk+", pilih salah satu cara bayar ridernya!" );
							}
						}
					}else if(edit.getPowersave().getMpr_cara_bayar_rider()!=null){
						if(edit.getPowersave().getMpr_cara_bayar_rider()==0 || edit.getPowersave().getMpr_cara_bayar_rider()==2){
							for(Datarider datarider : xx){
								if((datarider.getLsbs_id()==813 && datarider.getLsdbs_number()==5) || (datarider.getLsbs_id()==818 && datarider.getLsdbs_number()==4)){
									if(edit.getDatausulan().getLscb_id_rider()!=0){
//										err.rejectValue("datausulan.lscb_id_rider","", "HALAMAN INVESTASI: Cara bayar rider harus Sekaligus");
									}
								}else if((datarider.getLsbs_id()==813 && datarider.getLsdbs_number()==4) || (datarider.getLsbs_id()==818 && datarider.getLsdbs_number()==3)){
									if(edit.getDatausulan().getLscb_id_rider()==0){
										err.rejectValue("datausulan.lscb_id_rider","", "HALAMAN INVESTASI: Cara bayar rider tidak dapat memilih Sekaligus, silakan mengganti ridernya menjadi TERM-4/CI-4 sekaligus");
									}
								}
							}
						}
					}
					
					//(Deddy) - Req Rudi: Validasi ini tidak jadi dipakai, krn baik rollover premi maupun all, bunganya akan dipotong saat dibalikin ke nasabah maupun di rollover lagi
//					if(edit.getPowersave().getMpr_cara_bayar_rider()!=null){
//						if(edit.getPowersave().getMpr_cara_bayar_rider()==1 && edit.getPowersave().getMps_roll_over()==1){
//							err.rejectValue("powersave.mpr_cara_bayar_rider","","HALAMAN INVESTASI : Apabila Cara Bayar yang dipilih Potong Bunga, maka jenis RollOver Nilai Tunai(All) tidak dapat dipilih");
//						}
//					}
					
					String valid_cara_bayar_rider = "";
					if(edit.getPowersave().getMpr_cara_bayar_rider()!=1 && edit.getPowersave().getMpr_cara_bayar_rider()!=3){
						for(Datarider datarider : xx){
							if(datarider.getLsbs_id()!=822){
								if(Integer.parseInt(edit.getPowersave().getMps_jangka_inv())==3){
									if(edit.getDatausulan().getLscb_id_rider()!=1 && edit.getDatausulan().getLscb_id_rider()!=6){
//										valid_cara_bayar_rider = "TRIWULANAN, SEMESTERAN, TAHUNAN & SEKALIGUS";
										valid_cara_bayar_rider = "BULANAN, & TRIWULANAN";
								}
								}else if(Integer.parseInt(edit.getPowersave().getMps_jangka_inv())==6){
	//								if(edit.getDatausulan().getLscb_id_rider()==1 || edit.getDatausulan().getLscb_id_rider()==6){
									if(edit.getDatausulan().getLscb_id_rider()!=1 && edit.getDatausulan().getLscb_id_rider()!=6 && edit.getDatausulan().getLscb_id_rider()!=2){
										valid_cara_bayar_rider = "BULANAN, TRIWULANAN, & SEMESTERAN";
//											valid_cara_bayar_rider = "SEMESTERAN, TAHUNAN & SEKALIGUS";
									}
								}
								else if(Integer.parseInt(edit.getPowersave().getMps_jangka_inv())==12){
									//if(edit.getDatausulan().getLscb_id_rider()==1 || edit.getDatausulan().getLscb_id_rider()==2 || edit.getDatausulan().getLscb_id_rider()==6){
									if(edit.getDatausulan().getLscb_id_rider()!=1 && edit.getDatausulan().getLscb_id_rider()!=2 && edit.getDatausulan().getLscb_id_rider()!=6 && edit.getDatausulan().getLscb_id_rider()!=3){
										valid_cara_bayar_rider = "BULANAN, TRIWULANAN, SEMESTERAN, & TAHUNAN";
//										valid_cara_bayar_rider = "TAHUNAN & SEKALIGUS";
									}
								}else if(Integer.parseInt(edit.getPowersave().getMps_jangka_inv())>12){
									if(edit.getDatausulan().getLscb_id_rider()==1 || edit.getDatausulan().getLscb_id_rider()==2 || edit.getDatausulan().getLscb_id_rider()==3 || edit.getDatausulan().getLscb_id_rider()==6){
	//								if(edit.getDatausulan().getLscb_id_rider()!=1 && edit.getDatausulan().getLscb_id_rider()!=2 && edit.getDatausulan().getLscb_id_rider()!=3 && edit.getDatausulan().getLscb_id_rider()!=6){
//										valid_cara_bayar_rider = "BULANAN, TRIWULANAN, SEMESTERAN, & TAHUNAN";
									}
								}
							}
							if(!valid_cara_bayar_rider.equals("")){
								err.rejectValue("datausulan.lscb_id_rider", "", "HALAMAN INVESTASI:Untuk produk "+jenis_produk+", hanya dapat memilih cara bayar rider "+valid_cara_bayar_rider+" untuk MGI/MTI "+edit.getPowersave().getMps_jangka_inv()+" bulan");
							}
						}
					}
					
					if(Integer.parseInt(edit.getPowersave().getMps_jangka_inv())>12){
						for(Datarider datarider : xx){
							if((datarider.getLsbs_id()==813 && datarider.getLsdbs_number()!=5) || (datarider.getLsbs_id()==818 && datarider.getLsdbs_number()!=4)){
//								err.rejectValue("powersave.mps_jangka_inv","", "HALAMAN INVESTASI: Untuk MGI >12, Rider Term 4/ CI 4 yang dipilih harus Term 4/ CI 4 Sekaligus.silakan diubah di Data Usulan!");
							}
						}
					}
					
				}
				
			}
			

			
			//bunga simponi
			String hsl_bunga_simponi="";
			if (produk.isBungaSimponi==true)
			{
				Date tgl_max=null;
				Date tgl_begin_date_polis = edit.getDatausulan().getMste_beg_date();
				
				tgl_max = (Date)this.elionsManager.select_bungasimponi(kurs,tgl_begin_date_polis);
				edit.getPemegang().setTgl_mspo_under_table(tgl_max);
				if (tgl_max==null)
				{
					hsl_bunga_simponi="Bunga Simponi belum diinput !!";
					err.rejectValue("pemegang.mspo_under_table","","HALAMAN INVESTASI :" +hsl_bunga_simponi);
				}
				
				Double bunga_s=null;
				if (hsl_bunga_simponi.trim().length()==0 && tgl_max!=null)
				{

					bunga_s = this.elionsManager.select_bunga_simponi(kurs,tgl_max);

					if (bunga_s==null)
					{
						hsl_bunga_simponi="Bunga Simponi belum diinput !!";
						err.rejectValue("pemegang.mspo_under_table","","HALAMAN INVESTASI :" +hsl_bunga_simponi);
					}						
				}
		
				if (hsl_bunga_simponi.trim().length()==0 && tgl_max!=null)
				{
					bunga_simponi = Double.toString(bunga_s.doubleValue());
					edit.getPemegang().setMspo_under_table(new Double(bunga_simponi));
				}
			}
			//bonus tahapan
			String hsl_bonus_tahapan ="";
			if (produk.isBonusTahapan==true)
			{
				bonus_tahapan = edit.getPemegang().getBonus_tahapan();
				if (bonus_tahapan==null)
				{
					bonus_tahapan=new Double(0);
				}
				hsl_bonus_tahapan= a.cek_bonus_tahapan(Double.toString(bonus_tahapan.doubleValue()),produk.isBonusTahapan);
				if (hsl_bonus_tahapan.trim().length()!=0)
				{
					err.rejectValue("pemegang.bonus_tahapan","","HALAMAN INVESTASI :" +hsl_bonus_tahapan);
				}else{
					edit.getPemegang().setBonus_tahapan(bonus_tahapan);
				}
			}
			//int flag_biaya_tambahan= produk.flag_biaya_tambahan;

//		 produk excellink
		if (kode_flag.intValue()>1 )
		{
//			cek top up
			topup_tunggal= edit.getInvestasiutama().getDaftartopup().getPremi_tunggal();
			if (topup_tunggal==null)
			{
				topup_tunggal = new Double(0);
				edit.getInvestasiutama().getDaftartopup().setPremi_tunggal(topup_tunggal);
			}
			topup_berkala= edit.getInvestasiutama().getDaftartopup().getPremi_berkala();
			if (topup_berkala==null)
			{
				topup_berkala = new Double(0);
				edit.getInvestasiutama().getDaftartopup().setPremi_berkala(topup_berkala);
			}
			
			jns_top_up_tunggal=edit.getInvestasiutama().getDaftartopup().getPil_tunggal();
			jns_top_up_berkala = edit.getInvestasiutama().getDaftartopup().getPil_berkala();
			if (jns_top_up_tunggal==null)
			{
				jns_top_up_tunggal=new Integer(0);
				edit.getInvestasiutama().getDaftartopup().setPil_tunggal(jns_top_up_tunggal);
			}
			if (jns_top_up_berkala==null)
			{
				jns_top_up_berkala=new Integer(0);
				edit.getInvestasiutama().getDaftartopup().setPil_berkala(jns_top_up_berkala);
			}			
			if (((jns_top_up_berkala.intValue())==1 || (jns_top_up_berkala.intValue())==3)&& (pmode.intValue())==0)
			{
				hasil_jns_top_up="Cara Bayar Sekaligus hanya untuk pilihan Top-Up Tunggal ";
				err.rejectValue("investasiutama.daftartopup.pil_tunggal","","HALAMAN INVESTASI :" +hasil_jns_top_up);
			}
			if (((jns_top_up_berkala.intValue())==1 || (jns_top_up_tunggal.intValue())==2) && (kode_flag.intValue()==3) )
			{
				hasil_jns_top_up="Silahkan pilih Top-Up Platinum ";
				err.rejectValue("investasiutama.daftartopup.pil_berkala","","HALAMAN INVESTASI :" +hasil_jns_top_up);		
			}
			
			if (jns_top_up_tunggal.intValue()==2)
			{
				edit.getDatausulan().setLi_trans_tunggal(new Integer(2));
			}else if (jns_top_up_berkala.intValue()==3)
				{
				edit.getDatausulan().setLi_trans_berkala(new Integer(6));
				}
			
			hasil_jns_top_up=a.jenis_topup(topup_tunggal,Integer.toString(jns_top_up_tunggal.intValue()));
			if (hasil_jns_top_up.trim().length()!=0)
			{
				err.rejectValue("investasiutama.daftartopup.pil_tunggal","","HALAMAN INVESTASI :" +hasil_jns_top_up);	
			}else{
				if (jns_top_up_tunggal.intValue()==0)
				{
					hasil_topup1 = a.cek_topup(topup_tunggal,Integer.toString(jns_top_up_tunggal.intValue()));
					if (hasil_topup1.trim().length()!=0)
					{
						err.rejectValue("investasiutama.daftartopup.pil_tunggal","","HALAMAN INVESTASI :" +hasil_topup1);
					}
				}else{
					hasil_topup1 = a.jmlh_topup(topup_tunggal.toString());
					if (hasil_topup1.trim().length()!=0)
					{
						err.rejectValue("investasiutama.daftartopup.pil_tunggal","","HALAMAN INVESTASI :" +hasil_topup1);
					}
				}
			}
			
			hasil_jns_top_up=a.jenis_topup(topup_berkala,Integer.toString(jns_top_up_berkala.intValue()));
			if (hasil_jns_top_up.trim().length()!=0)
			{
				err.rejectValue("investasiutama.daftartopup.pil_berkala","","HALAMAN INVESTASI :" +hasil_jns_top_up);	
			}else{
				if (jns_top_up_berkala.intValue()==0)
				{
					hasil_topup1 = a.cek_topup(topup_berkala,Integer.toString(jns_top_up_berkala.intValue()));
					if (hasil_topup1.trim().length()!=0)
					{
						err.rejectValue("investasiutama.daftartopup.pil_berkala","","HALAMAN INVESTASI :" +hasil_topup1);
					}
				}else{
					hasil_topup1 = a.jmlh_topup(topup_berkala.toString());
					if (hasil_topup1.trim().length()!=0)
					{
						err.rejectValue("investasiutama.daftartopup.pil_berkala","","HALAMAN INVESTASI :" +hasil_topup1);
					}
				}
			}
			
			if (hasil_jns_top_up.trim().length()==0)
			{
				Double factor = 1.;
				if(pmode==1){
					factor = 4.;
				}else if(pmode==2){
					factor = 2.;
				}else if(pmode==6){
					factor = 12.;
				}
				//apabila ada topup tunggal + berkala
				if (jns_top_up_tunggal.intValue() >0 && jns_top_up_berkala.intValue() >0) {
					hasil_jns_top_up=produk.min_topup(pmode, premi , (topup_tunggal+topup_berkala) * factor, kurs, jns_top_up_tunggal.intValue());
					if (hasil_jns_top_up.trim().length()!=0) {
						err.rejectValue("investasiutama.daftartopup.pil_tunggal","","HALAMAN INVESTASI :" +hasil_jns_top_up);	
					}
				}else {
					//apabila hanya topup tunggal
					if (jns_top_up_tunggal.intValue() >0) {
						hasil_jns_top_up=produk.min_topup(pmode,premi , topup_tunggal * factor, kurs, jns_top_up_tunggal.intValue());
						if (hasil_jns_top_up.trim().length()!=0) {
							err.rejectValue("investasiutama.daftartopup.pil_tunggal","","HALAMAN INVESTASI :" +hasil_jns_top_up);	
						}
					//apabila hanya topup berkala
					}else if (jns_top_up_berkala.intValue() >0) {
						if(kode_produk.intValue()!=121){
							hasil_jns_top_up=produk.min_topup(pmode,premi , topup_berkala * factor, kurs, jns_top_up_berkala.intValue());
							if (hasil_jns_top_up.trim().length()!=0) {
								err.rejectValue("investasiutama.daftartopup.pil_berkala","","HALAMAN INVESTASI :" +hasil_jns_top_up);	
							}
						}
					}
				}
				
				//apabila tidak ada topup
				if (jns_top_up_berkala.intValue() == 0 && jns_top_up_tunggal.intValue() == 0) {
					if(edit.getDatausulan().getMste_flag_el()==1){
						hasil_jns_top_up=produk.min_total_premi_karyawan(pmode, premi, kurs);
					}else{
						hasil_jns_top_up=produk.min_total_premi(pmode, premi, kurs);
					}
					if (hasil_jns_top_up.trim().length()!=0)
					{
						err.rejectValue("investasiutama.daftartopup.pil_berkala","","HALAMAN INVESTASI :" +hasil_jns_top_up);	
					}
				}
			}
			
			//muamalat - mabrur
			if(kode_flag == 14) {
				if(topup_tunggal != null) if(topup_tunggal.doubleValue() > 0) 
					err.rejectValue("investasiutama.daftartopup.pil_tunggal","","HALAMAN INVESTASI : PRODUK Bank Muamalat DI NEW BUSINESS TIDAK BOLEH ADA TOPUP!");
				if(topup_berkala != null) if(topup_berkala.doubleValue() > 0) 
					err.rejectValue("investasiutama.daftartopup.pil_berkala","","HALAMAN INVESTASI : PRODUK Bank Muamalat DI NEW BUSINESS TIDAK BOLEH ADA TOPUP!");
			}

			Date tanggal_beg_date_polis = edit.getDatausulan().getMste_beg_date();
			Date tanggal_surat = umr.f_add_months(tanggal_beg_date_polis.getYear()+1900,tanggal_beg_date_polis.getMonth()+1, tanggal_beg_date_polis.getDate(),6);
			edit.getInvestasiutama().setMu_tgl_surat(tanggal_surat);
			int t1 = tanggal_surat.getDate();
			int t2 = tanggal_surat.getMonth()+1;
			int t3 = tanggal_surat.getYear()+1900;
			if (total_rider_sementara==null)
			{
				total_rider_sementara = new Double(0);
			}
			Double total_premi = edit.getDatausulan().getMspr_premium();
			if (total_premi==null)
			{
				total_premi= new Double(0);
			}
			Double total_premi_rider = edit.getDatausulan().getTotal_premi_rider();
			if (total_premi_rider==null)
			{
				total_premi_rider= new Double(0);
			}
			total_premi_sementara = new Double(total_premi.doubleValue()+total_premi_rider.doubleValue());
			//total_premi_sementara=new Double(total_rider_sementara.doubleValue()+total_premi.doubleValue()) ;
			total_premium1=	new Double(total_premi_sementara.doubleValue()+topup_tunggal.doubleValue()+topup_berkala.doubleValue());
			edit.getDatausulan().setTotal_premi_sementara(total_premium1);
			edit.getInvestasiutama().setTotal_premi_sementara(total_premium1);
		
			if (kode_flag.intValue()==3 && total_premium1.doubleValue() <50000000)
			{
				hasil_total_premium="Total Premi Excellink Platinum Rp 50 juta !!!";
				err.rejectValue("investasiutama.total_premi","","HALAMAN INVESTASI :" +hasil_total_premium);
			}
			
			if ( kode_flag.intValue() != 11 && kode_flag.intValue() != 15)
			{
				Integer flag_rider_hcp = edit.getDatausulan().getFlag_rider_hcp();
				if (flag_rider_hcp == null)
				{
					flag_rider_hcp = new Integer(0);
				}
				Integer flag_hcp = edit.getDatausulan().getFlag_hcp();
				if (flag_hcp == null)
				{
					flag_hcp = new Integer(0);
				}
				Map data = null;
				if (flag_hcp.intValue() == 0 || flag_rider_hcp.intValue() == 0)
				{
					data = this.elionsManager.hitbac(cmd,err,kurs,up,null ,null ,null,null, kode_flag,flag_rider,pmode,premi,topup_berkala,topup_tunggal, flag_as,jns_top_up_berkala,jns_top_up_tunggal,null,null,null ,null,null,null,"biayaexcell");
				}else{
					data = this.elionsManager.hitbac(cmd,err,kurs,up,null ,null ,null,null, kode_flag,flag_rider,pmode,premi,topup_berkala,topup_tunggal, flag_as,jns_top_up_berkala,jns_top_up_tunggal,null,null,null ,null,null,null,"hcp");
				}
				String hasil_biaya = (String)data.get("total_persen");
				if (hasil_biaya !=null)
				{
					if (!hasil_biaya.equalsIgnoreCase(""))
					{
						err.rejectValue("investasiutama.total_persen","","HALAMAN INVESTASI :" +hasil_biaya);	
					}
				}
				
				String mu_jlh_tu = (String)data.get("daftartopup.premi_tunggal");
				if (mu_jlh_tu !=null)
				{
					if (!mu_jlh_tu.equalsIgnoreCase(""))
					{
						err.rejectValue("investasiutama.daftartopup.premi_tunggal","","HALAMAN INVESTASI :" +mu_jlh_tu);
					}
				}
				
				mu_jlh_tu = (String)data.get("daftartopup.premi_berkala");
				if (mu_jlh_tu !=null)
				{
					if (!mu_jlh_tu.equalsIgnoreCase(""))
					{
						err.rejectValue("investasiutama.daftartopup.premi_berkala","","HALAMAN INVESTASI :" +mu_jlh_tu);
					}
				}
				
				String mu_periodic_tu= (String)data.get("daftartopup.pil_tunggal");
				if (mu_periodic_tu !=null)
				{
					if (!mu_periodic_tu.equalsIgnoreCase(""))
					{
						err.rejectValue("investasiutama.daftartopup.pil_tunggal","","HALAMAN INVESTASI :" +mu_periodic_tu);
					}
				}
				
				mu_periodic_tu= (String)data.get("daftartopup.pil_berkala");
				if (mu_periodic_tu !=null)
				{
					if (!mu_periodic_tu.equalsIgnoreCase(""))
					{
						err.rejectValue("investasiutama.daftartopup.pil_berkala","","HALAMAN INVESTASI :" +mu_periodic_tu);
					}
				}
	
				ld_premi_invest = (Double)data.get("ld_premi_invest");
				flag_minus = ((Boolean)data.get("flag_minus")).booleanValue();
				jumlah_minus = (Double)data.get("jumlah_minus");
				
				Double li_pct_biaya = new Double(produk.li_pct_biaya); 
				
				Map data1 = this.elionsManager.hitbac(cmd,err,null,null,null ,null ,null,null, kode_flag,null,null,null,topup_berkala,topup_tunggal ,null,null,null,li_pct_biaya,ld_premi_invest,new Boolean(flag_minus),jumlah_minus,null,null,"fundexcell");
				String hasil_total= (String)data1.get("total_persen");
				if (hasil_total !=null)
				{
					if (!hasil_total.equalsIgnoreCase(""))
					{
						err.rejectValue("investasiutama.total_persen","","HALAMAN INVESTASI :" +hasil_total);
					}
				}
			}// tutup kode_flag.intValue() != 11
		}// tutup if kode_flag.intValue()>1
		
			Integer flag_ref_telemarketing = edit.getTertanggung().getMste_flag_ref_telemarket();
			if(flag_ref_telemarketing==null){
				flag_ref_telemarketing = new Integer(0);
				edit.getTertanggung().setMste_flag_ref_telemarket(flag_ref_telemarketing);
			}
			
			//rek client
			if ((flag_account.intValue()==2)||(flag_account.intValue()==3) )
			{
				if (kode_flag.intValue()!=1 )
				{
				
					
					String kota_rek = edit.getRekening_client().getMrc_kota();
					if (kota_rek == null)
					{
						kota_rek ="";
					}else{
						kota_rek = f_validasi.convert_karakter(kota_rek);
					}
					edit.getRekening_client().setMrc_kota(kota_rek);
					
					Integer jenis_nasabah = edit.getRekening_client().getMrc_jn_nasabah();
				
					if (jenis_nasabah ==null)
					{
						jenis_nasabah = new Integer(0);
						edit.getRekening_client().setMrc_jn_nasabah(jenis_nasabah);
					}
					String rek_bank_pp1=edit.getRekening_client().getMrc_no_ac();
					if (rek_bank_pp1==null)
					{
						rek_bank_pp1="";
						edit.getRekening_client().setMrc_no_ac(rek_bank_pp1);
					}
					String bank_pp1=edit.getRekening_client().getLsbp_id();
					if (bank_pp1==null)
					{
						bank_pp1="";
						edit.getRekening_client().setLsbp_id(bank_pp1);
					}
					String nama_bank_rekclient="";
					if(bank_pp1!=null)
					{
						Map data1= (HashMap) this.elionsManager.select_bank1(bank_pp1);
	
						if (data1!=null)
						{		
							nama_bank_rekclient = (String)data1.get("BANK_NAMA");
						}
					}
					edit.getRekening_client().setLsbp_nama(nama_bank_rekclient);
					
					
					String atasnama1=edit.getRekening_client().getMrc_nama();
					if (atasnama1==null)
					{
						atasnama1="";
						edit.getRekening_client().setMrc_nama(atasnama1);
					}
					
					Integer kuasa = edit.getRekening_client().getMrc_kuasa();
					if (kuasa == null)
					{
						kuasa = new Integer(0);
					}

					if (kuasa.intValue()== 1)
					{
						Date tgl_surat = edit.getRekening_client().getTgl_surat();
						
						if (tgl_surat == null)
						{
							err.rejectValue("rekening_client.tgl_surat","","HALAMAN INVESTASI : Tanggal surat belum diisi, silahkan isi tanggal surat terlebih dahulu.");
						}
					}
					edit.getRekening_client().setMrc_kuasa(kuasa);
					
					String keterangan_rek = edit.getRekening_client().getNotes();
					if (keterangan_rek==null)
					{
						keterangan_rek = "";
						edit.getRekening_client().setNotes(keterangan_rek);
					}

					
					Integer jenis_tab = edit.getRekening_client().getMrc_jenis();
					if (jenis_tab==null)
					{
						jenis_tab=new Integer(0);
						edit.getRekening_client().setMrc_jenis(jenis_tab);
					}
					String kota_bank=edit.getRekening_client().getMrc_kota();
					if (kota_bank==null)
					{
						kota_bank="";
						edit.getRekening_client().setMrc_kota(kota_bank);
					}
					String cabang_bank=edit.getRekening_client().getMrc_cabang();	
					if(cabang_bank==null)
					{
						cabang_bank="";
						edit.getRekening_client().setMrc_cabang(cabang_bank);
					}
					
					Integer flag_gmanual_dmtm = uwManager.selectCountMstTempDMTM(edit.getTertanggung().getReg_spaj());
					if(flag_gmanual_dmtm>=1){
						
					}else{
						String hasil_jenis_tab=a.jenis_tab(Integer.toString(jenis_tab.intValue()),lca_id,flag_account.intValue());
						if (hasil_jenis_tab.trim().length()!=0)
						{
							err.rejectValue("rekening_client.mrc_jenis","","HALAMAN INVESTASI :" +hasil_jenis_tab);
						}
					}
					
					if(products.muamalat(produk.ii_bisnis_id, produk.ii_bisnis_no) && jenis_tab.intValue() == 3) {
						err.rejectValue("rekening_client.mrc_jenis","","HALAMAN INVESTASI : Produk Bank Muamalat tidak boleh jenis tabungan : CREDIT CARD");
					}
					
						if (edit.getAgen().getKode_regional()!=null )
						{
							if (!(edit.getAgen().getKode_regional().trim().equalsIgnoreCase("")))
							{
								if (FormatString.rpad("0",(edit.getAgen().getKode_regional().substring(0,4)),4).equalsIgnoreCase("0914"))
								{
									String flag_jenis_nasabah=a.cek_jenis_nasabah(Integer.toString(jenis_nasabah.intValue()));
									if (flag_jenis_nasabah.trim().length()!=0)
									{
										err.rejectValue("rekening_client.mrc_jn_nasabah","","HALAMAN INVESTASI :" +flag_jenis_nasabah);
									}
								}
							}
						}
				}
			}
			
			// khusus powersave, validasi jenis bunga, jangka waktu, rollover, bunga
			if (kode_flag.intValue() == 1 || kode_flag.intValue() == 11 || kode_flag.intValue() == 15)
			{
				
				//STABLE LINK - Yusuf (28/04/2008)
				if (kode_flag.intValue() == 11 || kode_flag.intValue() == 15){
					Powersave p = edit.getPowersave();
					
					SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
					String lji_id = kurs.equalsIgnoreCase("01") ? "22" : "23";
					
					if(kode_flag.intValue() == 15){
						lji_id = kurs.equalsIgnoreCase("01") ? "30" : "31";
					}
					Map data_nab = new HashMap();
					//UNIT UNTUK PREMI POKOK
					if(edit.getDatausulan().isPsave || edit.getPowersave().getMsl_spaj_lama()!=null){
						data_nab = (HashMap) this.elionsManager.select_tminus(lji_id,edit.getPowersave().getBegdate_topup(), 3);
					}else {
						data_nab = (HashMap) this.elionsManager.select_tminus(lji_id,edit.getDatausulan().getMste_beg_date(), 3);
					}
					
					Date tgl_nab=null;
					if (data_nab != null){
						p.setMsl_tgl_nab((Date)data_nab.get("LNU_TGL"));
						p.setMsl_nab((Double)data_nab.get("LNU_NILAI"));
					}
					if(p.getMsl_nab() == null) p.setMsl_nab(0.);
					
					//Deddy - Untuk testing, apabila dicomment err.reject maka msl_nab harus ada nilai(!0)
					//if (p.getMsl_nab() == 0) p.setMsl_nab(10.0);
					if (p.getMsl_nab() == 0) err.rejectValue("powersave.msl_nab","","HALAMAN INVESTASI : Nilai Nab Untuk Premi Pokok ("+df.format(FormatDate.add(p.getBegdate_topup(), Calendar.DATE, -3))+") tidak ada, silahkan konfirmasi dengan administrator ");
				
					p.setMsl_unit(premi.doubleValue() / p.getMsl_nab().doubleValue());
					
					//APABILA ADA PREMI TU, HITUNG UNIT UNTUK PREMI TU
					if(edit.getInvestasiutama().getDaftartopup().getPil_tunggal().intValue() != 0) {
						if(p.getBegdate_topup() == null) {
							err.rejectValue("powersave.begdate_topup","","HALAMAN INVESTASI : Apabila produk STABLE LINK dan ada Top-Up, maka harus mengisi begdate Top-Up");
						}else {
							Map data_nab_tu = (HashMap) this.elionsManager.select_tminus(lji_id,p.getBegdate_topup(), 3);
							
							Date tgl_nab_tu=null;
							if (data_nab_tu != null){
								p.setMsl_tgl_nab_tu((Date)data_nab.get("LNU_TGL"));
								p.setMsl_nab_tu((Double)data_nab.get("LNU_NILAI"));
							}
							if(p.getMsl_nab_tu() == null) p.setMsl_nab_tu(0.);
							
							if (p.getMsl_nab_tu() == 0) err.rejectValue("powersave.msl_nab","","HALAMAN INVESTASI : Nilai Nab Untuk Premi Top-Up ("+df.format(FormatDate.add(p.getBegdate_topup(), Calendar.DATE, -3))+") tidak ada, silahkan konfirmasi dengan administrator ");
						
							p.setMsl_unit_tu(edit.getInvestasiutama().getDaftartopup().getPremi_tunggal() / p.getMsl_nab_tu());
						}
					}
					
					if(edit.getPowersave().getMsl_bp_rate()==null) {
						err.rejectValue("powersave.msl_bp_rate","","HALAMAN INVESTASI : Harap Isi Persentase Rate (Faktor Biaya Investasi) dengan angka diantara 70 - 100 %");
					}else {
						double rate = edit.getPowersave().getMsl_bp_rate();
						if(rate < 70 || rate > 100) {
							err.rejectValue("powersave.msl_bp_rate","","HALAMAN INVESTASI : Harap Isi Persentase Rate (Faktor Biaya Investasi) dengan angka diantara 70 - 100 %");
						}
					}
					//Yusuf - Start 10 Sept 09 - BP Rate ambil dari fungsi saja, dan ada perubahan
					/*
					if(!edit.getPowersave().getMps_jangka_inv().trim().equals("")){
						edit.getPowersave().setMsl_bp_rate(
								uwManager.getBPrate(
										edit.getPowersave().getBegdate_topup(),
										Integer.valueOf(edit.getPowersave().getMps_jangka_inv()), 
										edit.getPowersave().getFlag_bulanan()));
					}*/
					
				}
				//END OF STABLE LINK
//				(Deddy) validasi baru untuk stable save BII
				if(produk.ii_bisnis_id==143 && produk.ii_bisnis_no==5){
					//jika platinum min premi Rp. 250.000.000/US$ 25,000
					Double batas_premi = new Double(250000000.0);
					if(edit.getRekening_client().getMrc_jn_nasabah().intValue()==1){
						if(edit.getDatausulan().getLku_id().equals("01")){
							if(edit.getDatausulan().getMspr_premium()<batas_premi.doubleValue()){
								err.rejectValue("rekening_client.mrc_jn_nasabah","","HALAMAN INVESTASI : Untuk Jenis Nasabah Platinum dan Produk Stable Save Bii, Premi minimum Rp.250000000,-" );
							}
						}else if(edit.getDatausulan().getLku_id().equals("02")){
							batas_premi = new Double(25000);
							if(edit.getDatausulan().getMspr_premium()<batas_premi.doubleValue()){
								err.rejectValue("rekening_client.mrc_jn_nasabah","","HALAMAN INVESTASI : Untuk Jenis Nasabah Platinum dan Produk Stable Save Bii, Premi minimum US$25000" );
							}
						}
					}else if(edit.getRekening_client().getMrc_jn_nasabah().intValue()==2){
						if(edit.getDatausulan().getLku_id()=="01"){
							if(edit.getDatausulan().getMspr_premium()<new Double(25000000)){
								err.rejectValue("rekening_client.mrc_jn_nasabah","","HALAMAN INVESTASI : Untuk Jenis Nasabah Platinum dan Produk Stable Save Bii, Premi minimum Rp.25000000,-" );
							}
						}else if(edit.getDatausulan().getLku_id()=="02"){
							if(edit.getDatausulan().getMspr_premium()<new Double(2500)){
								err.rejectValue("rekening_client.mrc_jn_nasabah","","HALAMAN INVESTASI : Untuk Jenis Nasabah Platinum dan Produk Stable Save Bii, Premi minimum US$2500" );
							}
						}
					}
				}
				
				String hasil_jns_top_up0="";
				String jns_top_up0=edit.getPowersave().getMps_jangka_inv();
				if (jns_top_up0 == null || jns_top_up0.equalsIgnoreCase(""))
				{
					hasil_jns_top_up0="Jangka Waktu bunga powersave belum dipilih.";
					err.rejectValue("powersave.mps_jangka_inv","","HALAMAN INVESTASI :" +hasil_jns_top_up0);
				}else{
					int mgi = Integer.parseInt(jns_top_up0);
					produk.cek_mgi(number_produk.intValue());
					int indeks_mgi=produk.indeks_mgi;
					Integer ok = new Integer(0);
					for (int j=0 ; j <indeks_mgi; j++)
					{
						if (mgi == produk. mgi[j])
						{
							ok = new Integer(1);
						}
					}
					
					//special case Yusuf - 9 jan 2009 - stable link (164) mgi 1 bulan, mulai 9 jan 2009 di hold
					int lsbs_id = edit.getDatausulan().getLsbs_id().intValue();
					Date begdate = edit.getDatausulan().getMste_beg_date();
					Date sembilanJanuari = defaultDateFormat.parse("09/01/2009");
					
					if (ok.intValue() == 0) {
						
						if(mgi == 1 && products.stableLink(String.valueOf(lsbs_id)) && begdate.before(sembilanJanuari)) {
							
						}else {
							hasil_jns_top_up0="Jangka Waktu bunga powersave tersebut tidak bisa dipilih untuk produk ini.";
							err.rejectValue("powersave.mps_jangka_inv","","HALAMAN INVESTASI :" +hasil_jns_top_up0);							
						}
						
					}else{
						String hasil_premi = produk.of_get_min_up_permgi(number_produk.intValue(), mgi, premi, kurs);
						if (hasil_premi.length() != 0)
						{
							err.rejectValue("datausulan.mspr_premium","","HALAMAN DATA USULAN :" +hasil_premi);
						}
					}
					
					if(products.stableSave(lsbs_id, edit.getDatausulan().getLsdbs_number()) && edit.getPowersave().getFlag_bulanan()==1 && mgi==1){
						err.rejectValue("powersave.mps_jangka_inv","", "HALAMAN INVESTASI :Untuk Produk Stable Save Manfaat Bulanan, tidak dapat mengambil MTI 1 bulan");
					}
					
					if(lsbs_id==186 && edit.getDatausulan().getLsdbs_number()==3 && mgi!=12){
						err.rejectValue("powersave.mps_jangka_inv","", "HALAMAN INVESTASI :Untuk Produk SSP PRO, Hanya bisa mengambil MTI 12 bulan");
					}
					
//					if(products.stableLink(Integer.toString(lsbs_id)) && edit.getPowersave().getFlag_bulanan()>0 && mgi<=12){
//						err.rejectValue("powersave.mps_jangka_inv","", "HALAMAN INVESTASI :Untuk Produk Stable Link Manfaat Triwulanan/Semesteran/Tahunan, Hanya bisa mengambil MTI 24 dan 36 bulan");
//					}
					
					if (hasil_jns_top_up0.equalsIgnoreCase(""))
					{
						hasil_jns_top_up0=produk.cek_min_mgi(mgi, kurs, premi);
						if (!hasil_jns_top_up0.equalsIgnoreCase(""))
						{
							err.rejectValue("powersave.mps_jangka_inv","","HALAMAN INVESTASI :" +hasil_jns_top_up0);
						}
					}else{
						err.rejectValue("powersave.mps_jangka_inv","","HALAMAN INVESTASI :" +hasil_jns_top_up0);
					}
				}
				if (hasil_jns_top_up0.equalsIgnoreCase(""))
				{
					Map data = this.elionsManager.hitbac(cmd,err,kurs,up,flag_account ,Common.isEmpty(edit.getAgen().getLca_id())?lca_id:edit.getAgen().getLca_id() ,2,flag_bao1, null,null,null,premi,null, null,null,null,null,null, null, null ,null,flag_powersave ,flag_bulanan,"bungapowersave");
					hasil_jns_top_up0 = (String)data.get("mps_jangka_inv");
					if (hasil_jns_top_up0 !=null)
					{
						err.rejectValue("powersave.mps_jangka_inv","","HALAMAN INVESTASI :" +hasil_jns_top_up0);
						
					}
					//(Deddy)set premi rider setelah memilih mgi
						List<Datarider> xx = edit.getDatausulan().getDaftaRider();
						edit.getDatausulan().setTotal_premi_rider(0.0);
						for(Datarider datarider : xx){
							String mgi_number = edit.getPowersave().getMps_jangka_inv();
							if(mgi_number==null || mgi_number.equals("")){
								mgi_number = "0";
							}
							Double mgi = Double.parseDouble(mgi_number);
							Integer lscb_id_rider =3;
							if(mgi==1){
								lscb_id_rider = 6;
							}else if(mgi==3){
								lscb_id_rider = 1;
							}else if(mgi==6){
								lscb_id_rider = 2;
							}else if(mgi==12){
								lscb_id_rider = 3;
							}else {
								lscb_id_rider = mgi.intValue();
							}
							if(!Common.isEmpty(edit.getPowersave().getMpr_cara_bayar_rider()) ){
								if(edit.getPowersave().getMpr_cara_bayar_rider()==1 || edit.getPowersave().getMpr_cara_bayar_rider()==3){
									edit.getDatausulan().setLscb_id_rider(lscb_id_rider);
								}
							}
							
							if ((mgi==1 && produk.ii_bisnis_id!= 188) || produk.flag_powersavebulanan == 1 || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no)){
								Double premi_tahunan = ( (datarider.getMspr_rate()/1000) * datarider.getMspr_tsi() );
								if(datarider.getLsbs_id().intValue()==819 || datarider.getLsbs_id().intValue()==820 || datarider.getLsbs_id().intValue()==823 ||datarider.getLsbs_id().intValue()==825 ||datarider.getLsbs_id().intValue()==826){
									premi_tahunan =  elionsManager.selectRateRider(kurs, edit.getTertanggung().getMste_age(), 0, datarider.getLsbs_id(), datarider.getLsdbs_number());
								}
								datarider.setMrs_premi_tahunan(premi_tahunan);
								if(products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no)){
//									Double factor=0.0;
//									if(edit.getDatausulan().getLscb_id_rider()==1){
//										factor = 0.270;
//									}else if(edit.getDatausulan().getLscb_id_rider()==2){
//										factor = 0.525;
//									}else if(edit.getDatausulan().getLscb_id_rider()==6){
//										factor = 0.1;
//									}else factor = 1.0;
									Double factor=0.0;
									if(mgi==3){
										factor = 0.270;
									}else if(mgi==6){
										factor = 0.525;
									}else if(mgi==1){
										factor = 0.1;
									}else factor = 1.0;
									
									datarider.setMspr_premium(premi_tahunan * factor);
								}else{
									datarider.setMspr_premium(premi_tahunan * 0.1);
								}
								edit.getDatausulan().setTotal_premi_rider(edit.getDatausulan().getTotal_premi_rider() + datarider.getMspr_premium());
								edit.getDatausulan().setDaftaRider(xx);
							}else {
								Integer flag_looping=12;
								Double factor=0.0;
								if(edit.getDatausulan().getLscb_id_rider()==1){
									flag_looping=4;
									factor = 0.270;
								}else if(edit.getDatausulan().getLscb_id_rider()==2){
									flag_looping=6;
									factor = 0.525;
								}else if(edit.getDatausulan().getLscb_id_rider()==6){
									flag_looping=1;
									factor = 0.1;
								}else factor = 1.0;
								int loopingpremi=0;//0 hanya ambil premi tahunan.
									loopingpremi=mgi.intValue()/flag_looping;
								
								Double premi_tahunan = ( (datarider.getMspr_rate()/1000) * datarider.getMspr_tsi() );
								
								if(datarider.getLsbs_id().intValue()==820 || datarider.getLsbs_id().intValue()==823  || datarider.getLsbs_id().intValue()==825 || datarider.getLsbs_id().intValue()==826 ){
									Integer size = edit.getDatausulan().getDaftaRider().size();
									if(size==null){
										size=0;
									}
									for(int b=0;b < size.intValue();b++){
										Datarider tampung = (Datarider) edit.getDatausulan().getDaftaRider().get(b);
										Double diskon = 1.0;
										Double premi_tampung = premi;
										if(tampung.getLsbs_id()==820 || tampung.getLsbs_id()==823 || tampung.getLsbs_id()==825){
											factor=1.0;
											if(edit.getDatausulan().getLscb_id_rider()==1){
												factor = 0.35;
											}else if(edit.getDatausulan().getLscb_id_rider()==2){
												factor = 0.65;
											}else if(edit.getDatausulan().getLscb_id_rider()==6){
												factor = 0.12;
											}else factor = 1.0;
										}
										if((tampung.getLsbs_id()==820 || tampung.getLsbs_id()==823 || tampung.getLsbs_id()==825) && (tampung.getLsdbs_number().intValue()>=1 && tampung.getLsdbs_number().intValue()<=15)){
											premi_tahunan = premi = elionsManager.selectRateRider(kurs, edit.getTertanggung().getMste_age(), 0, tampung.getLsbs_id(), tampung.getLsdbs_number());
											//premi_tahunan = premi= uwManager.selectPremiSuperSehat(kurs, edit.getTertanggung().getMste_age(), tampung.getLsbs_id(), tampung.getLsdbs_number());
										}else if((tampung.getLsbs_id()==820 || tampung.getLsbs_id()==823 || tampung.getLsbs_id()==825) && tampung.getLsdbs_number().intValue()>=16 ){
											diskon = 0.975;//diskon sebesar 2.5%
											premi = elionsManager.selectRateRider(kurs, edit.getTertanggung().getMste_age(), 0, tampung.getLsbs_id(), tampung.getLsdbs_number());
											//premi= uwManager.selectPremiSuperSehat(kurs, edit.getTertanggung().getMste_age(), tampung.getLsbs_id(), tampung.getLsdbs_number());
											premi_tahunan = premi = premi * diskon;
										}else if((tampung.getLsbs_id()==819 && (uwManager.selectPesertaUtamaOrTambahan(tampung.getLsbs_id(), tampung.getLsdbs_number()) ) ) || (tampung.getLsbs_id()==826 && tampung.getLsdbs_number()<=10) ){
											premi_tahunan = premi = elionsManager.selectRateRider(kurs, edit.getTertanggung().getMste_age(), 0, tampung.getLsbs_id(), tampung.getLsdbs_number());
										}else if((tampung.getLsbs_id()==819 && ((!uwManager.selectPesertaUtamaOrTambahan(tampung.getLsbs_id(), tampung.getLsdbs_number())) ) ) || (tampung.getLsbs_id()==826 && tampung.getLsdbs_number()>10) ){
											diskon = 0.9;//diskon sebesar 10%
											premi = elionsManager.selectRateRider(kurs, edit.getTertanggung().getMste_age(), 0, tampung.getLsbs_id(), tampung.getLsdbs_number());
											//premi= uwManager.selectPremiSuperSehat(kurs, edit.getTertanggung().getMste_age(), tampung.getLsbs_id(), tampung.getLsdbs_number());
											premi_tahunan = premi = premi * diskon;
										}
										if(mgi>12){
											for(int i=0;i<loopingpremi-1;i++){
												if((tampung.getLsbs_id()==820 || tampung.getLsbs_id()==823 || tampung.getLsbs_id()==825) && (tampung.getLsdbs_number().intValue()>=1 && tampung.getLsdbs_number().intValue()<=15)){
													premi_tahunan += premi = elionsManager.selectRateRider(kurs, edit.getTertanggung().getMste_age()+(i+1), 0, tampung.getLsbs_id(), tampung.getLsdbs_number());
													//premi_tahunan = premi= uwManager.selectPremiSuperSehat(kurs, edit.getTertanggung().getMste_age(), tampung.getLsbs_id(), tampung.getLsdbs_number());
												}else if((tampung.getLsbs_id()==820 || tampung.getLsbs_id()==823 || tampung.getLsbs_id()==825) && tampung.getLsdbs_number().intValue()>=16 ){
													diskon = 0.975;//diskon sebesar 2.5%
													Double premi_before_discount = elionsManager.selectRateRider(kurs, edit.getTertanggung().getMste_age()+(i+1), 0, tampung.getLsbs_id(), tampung.getLsdbs_number());
													premi_before_discount = premi_before_discount* diskon;
													//premi= uwManager.selectPremiSuperSehat(kurs, edit.getTertanggung().getMste_age(), tampung.getLsbs_id(), tampung.getLsdbs_number());
													premi_tahunan = premi += premi_before_discount;
												}else if((tampung.getLsbs_id()==819 && (tampung.getLsdbs_number()>=281 && tampung.getLsdbs_number()<=300) ) || (tampung.getLsbs_id()==826 && tampung.getLsdbs_number()<=10) ){
													premi_tahunan += premi = elionsManager.selectRateRider(kurs, edit.getTertanggung().getMste_age()+(i+1), 0, tampung.getLsbs_id(), tampung.getLsdbs_number());
												}else if((tampung.getLsbs_id()==819 && (tampung.getLsdbs_number()>=301) ) || (tampung.getLsbs_id()==826 && tampung.getLsdbs_number()>10) ){
													diskon = 0.9;//diskon sebesar 10%
													Double premi_before_discount = elionsManager.selectRateRider(kurs, edit.getTertanggung().getMste_age()+(i+1), 0, tampung.getLsbs_id(), tampung.getLsdbs_number());
													premi_before_discount = premi_before_discount* diskon;
													//premi= uwManager.selectPremiSuperSehat(kurs, edit.getTertanggung().getMste_age(), tampung.getLsbs_id(), tampung.getLsdbs_number());
													premi_tahunan = premi += premi_before_discount;
												}
											}
										}
									}
								}
								if(datarider.getLsbs_id().intValue()==811 || datarider.getLsbs_id().intValue()==819 || datarider.getLsbs_id().intValue()==822){
									premi_tahunan = datarider.getMspr_premium();
									if(datarider.getLsbs_id().intValue()==811 || datarider.getLsbs_id().intValue()==819){
										premi_tahunan = elionsManager.selectRateRider(kurs, edit.getTertanggung().getMste_age(), 0, datarider.getLsbs_id().intValue(), datarider.getLsdbs_number().intValue());
									}
									if(mgi>12){
										for(int i=0;i<loopingpremi-1;i++){
											if((datarider.getLsbs_id()==819 && ((datarider.getLsdbs_number()>=281 && datarider.getLsdbs_number()<=300) || (datarider.getLsdbs_number()>=431 && datarider.getLsdbs_number()<=450)) ) || (datarider.getLsbs_id()==826 && datarider.getLsdbs_number()<=10) ){
												premi_tahunan += premi = elionsManager.selectRateRider(kurs, edit.getTertanggung().getMste_age()+(i+1), 0, datarider.getLsbs_id(), datarider.getLsdbs_number());
											}else if((datarider.getLsbs_id()==819 && ((datarider.getLsdbs_number()>=301 && datarider.getLsdbs_number()<=430) || (datarider.getLsdbs_number()>=451)) ) || (datarider.getLsbs_id()==826 && datarider.getLsdbs_number()>10) ){
												Double diskon = 0.9;//diskon sebesar 10%
												Double premi_before_discount = elionsManager.selectRateRider(kurs, edit.getTertanggung().getMste_age()+(i+1), 0, datarider.getLsbs_id(), datarider.getLsdbs_number());
												premi_before_discount = premi_before_discount* diskon;
												premi_tahunan = premi += premi_before_discount;
											}
										}
									}
								}
								if((datarider.getLsbs_id().intValue()==813 && datarider.getLsdbs_number().intValue()!=5) || (datarider.getLsbs_id().intValue()==818 && datarider.getLsdbs_number().intValue()!=4)){
									Double rate_rider = elionsManager.selectRateRider(kurs, edit.getTertanggung().getMste_age(), 0, datarider.getLsbs_id().intValue(), datarider.getLsdbs_number().intValue());
									premi_tahunan = ( (rate_rider/1000) * datarider.getMspr_tsi() );
									if(mgi>12){
										for(int i=0;i<loopingpremi-1;i++){
											rate_rider = elionsManager.selectRateRider(kurs, edit.getTertanggung().getMste_age()+(i+1), 0, datarider.getLsbs_id(), datarider.getLsdbs_number());
											premi_tahunan += premi = ( (rate_rider/1000) * datarider.getMspr_tsi() );
										}
									}
								}
								
								datarider.setMrs_premi_tahunan(premi_tahunan);
								datarider.setMspr_premium(premi_tahunan * factor);
								edit.getDatausulan().setTotal_premi_rider(edit.getDatausulan().getTotal_premi_rider() + datarider.getMspr_premium());
								edit.getDatausulan().setDaftaRider(xx);
							}
						}
					
					//validasi tidak dipakai. Sesuai hasil meeting, kondisi seperti ini akan masuk kategori kurang bayar saja. 	
//					//(Deddy)- validasi untuk premi rider tidak boleh lebih dari bunga
//					if(edit.getDatausulan().getTotal_premi_rider()>edit.getPowersave().getMps_prm_interest()){
//						err.rejectValue("powersave.mps_prm_interest","","HALAMAN INVESTASI : BUNGA tidak boleh lebih kecil dari PREMI RIDER.");
//					}
					
					Double rate = edit.getPowersave().getMps_rate(); 
					if (rate == null)
					{
						rate = new Double(0);
					}
					if (rate.doubleValue()==0)
					{
						err.rejectValue("powersave.mps_rate","","HALAMAN INVESTASI : Rate masih nol, silahkan masukkan rate yang benar.");
					}
					
					String hasil_jns_top_up1 = (String)data.get("mps_roll_over");
					if (hasil_jns_top_up1 !=null)
					{
						if (!hasil_jns_top_up1.equalsIgnoreCase(""))
						{
							err.rejectValue("powersave.mps_roll_over","","HALAMAN INVESTASI :" +hasil_jns_top_up1);
						}
					}
						
					String hasil_jenis_tab = (String)data.get("mrc_jenis");
					if (hasil_jenis_tab!=null)
					{
						if (!hasil_jenis_tab.equalsIgnoreCase(""))
						{
							err.rejectValue("rekening_client.mrc_jenis","","HALAMAN INVESTASI :" +hasil_jenis_tab);
						}
					}
					
					String hasil_cabang_bank = (String)data.get("mrc_cabang");
					if (hasil_cabang_bank !=null)
					{
						if (!hasil_cabang_bank.equalsIgnoreCase(""))
						{
							err.rejectValue("rekening_client.mrc_cabang","","HALAMAN INVESTASI :" +hasil_cabang_bank);
						}
					}	
				
					String kota_rek = edit.getRekening_client().getMrc_kota();
					if (kota_rek == null)
					{
						kota_rek ="";
					}else{
						kota_rek = f_validasi.convert_karakter(kota_rek);
					}
					edit.getRekening_client().setMrc_kota(kota_rek);
					String hasil_rek_bank_pp1 = (String)data.get("mrc_no_ac");
					if (hasil_rek_bank_pp1 !=null)
					{
						if (!hasil_rek_bank_pp1.equalsIgnoreCase(""))
						{
							err.rejectValue("rekening_client.mrc_no_ac","","HALAMAN INVESTASI :" +hasil_rek_bank_pp1);
						}
					}
					
	
					String hasil_bank1 = (String)data.get("lsbp_id");
					if (hasil_bank1 !=null)
					{
						if (!hasil_bank1.equalsIgnoreCase(""))
						{
							err.rejectValue("rekening_client.lsbp_id","","HALAMAN INVESTASI :" +hasil_bank1);
						}
					}
					
					if (flag_powersave.intValue() == 3
							&& edit.getDatausulan().getKurs_premi()
									.equalsIgnoreCase("02") 
						|| flag_powersave.intValue() == 23
						&& edit.getDatausulan().getKurs_premi()
						.equalsIgnoreCase("02")) {
						if (!edit
								.getDatausulan()
								.getKurs_premi()
								.equalsIgnoreCase(
										edit.getRekening_client().getMrc_kurs())) {
							err
									.rejectValue(
											"rekening_client.mrc_kurs",
											"",
											"HALAMAN INVESTASI : Untuk PowerSave Bulanan Dollar, harus memiliki rekening U$");
						}

						// String bl = edit.getRekening_client().getLsbp_id();
//						if (!edit.getRekening_client().getLsbp_id()
//								.equalsIgnoreCase("45")) {
//							err
//									.rejectValue(
//											"rekening_client.lsbp_id",
//											"",
//											"HALAMAN INVESTASI : Untuk PowerSave Bulanan Dollar, harus memiliki rekening U$ di BII.");
//						}
					}
						
						String hasil_atasnama1 = (String)data.get("mrc_nama");
						if (hasil_atasnama1 !=null)
						{
							if (!hasil_atasnama1.equalsIgnoreCase(""))
							{
								err.rejectValue("rekening_client.mrc_nama","","HALAMAN INVESTASI :" +hasil_atasnama1);
							}
						}	
						
						String flag_jenis_nasabah = (String)data.get("mrc_jn_nasabah");
						if (flag_jenis_nasabah !=null)
						{
							if (!flag_jenis_nasabah.equalsIgnoreCase(""))
							{
								err.rejectValue("rekening_client.mrc_jn_nasabah","","HALAMAN INVESTASI :" +flag_jenis_nasabah);
							}
						}
						
						String hasil_tgl = (String)data.get("tgl");
						if (hasil_tgl != null)
						{
							if(! hasil_tgl.equalsIgnoreCase(""))
							{
								err.rejectValue("rekening_client.tgl_surat","","HALAMAN INVESTASI :" +hasil_tgl);
							}
						}
				}
			}
			
			//penerima manfaat
			List benef = edit.getDatausulan().getDaftabenef();
			Integer jumlah_r =new Integer(benef.size());
			if (jumlah_r==null)
			{
				jumlah_r=new Integer(0);
			}
			Integer jmlh_penerima=jumlah_r;
			
			edit.getDatausulan().setJml_benef(new Integer(jmlh_penerima.intValue()));
			String[] nama_m;
			Date[] tgllhr_m;
			Integer[] relation_m;
			Double[] persen_m;
			Integer[] sex_m;
			String hsl_tgl_m="";	
			String hsl_persen_m="";		
			String hsl_nama_m="";
			nama_m = new String[(jmlh_penerima.intValue())+1];
			tgllhr_m = new Date[(jmlh_penerima.intValue())+1];
			persen_m = new Double[(jmlh_penerima.intValue())+1];
			relation_m = new Integer[(jmlh_penerima.intValue())+1];
			sex_m = new Integer[(jmlh_penerima.intValue())+1];
			Double total_persen = 0.;
			BigDecimal tot_persen = new BigDecimal(0);
			for (int k=0 ; k < (jmlh_penerima.intValue()) ; k++)
			{
				nama_m[k]="";
				tgllhr_m[k]=null;
				relation_m[k]=new Integer(1);
				
				Benefeciary bf1= (Benefeciary)benef.get(k);
				
				nama_m[k]=bf1.getMsaw_first();	
				tgllhr_m[k]=bf1.getMsaw_birth();				
				persen_m[k]=bf1.getMsaw_persen();
				relation_m[k]=bf1.getLsre_id();	
				sex_m[k]=bf1.getMsaw_sex();	
				

				if (nama_m[k].trim().length()==0)
				{
					hsl_nama_m="Nama penerima manfaat ke " +(k+1) +" harus diisi.";
				}
				if (hsl_nama_m.trim().length()!=0)
				{
					err.rejectValue("datausulan.daftabenef["+k+"].msaw_first","","HALAMAN INVESTASI :" +hsl_nama_m);
				}
				
				if (tgllhr_m[k]==null)
				{
					hsl_tgl_m = "Tanggal lahir penerima manfaat ke " +(k+1) +" harus diisi.";
				}
				if (hsl_tgl_m.trim().length()!=0)
				{
					err.rejectValue("datausulan.daftabenef["+k+"].msaw_birth","","HALAMAN INVESTASI :" +hsl_tgl_m);
				}					
				
				hsl_persen_m=a.persen_manfaat(Integer.toString(persen_m[k].intValue()));
				if (hsl_persen_m.trim().length()!=0)
				{
					hsl_persen_m= hsl_persen_m + " , persen ke "+ (k+1);
				}
				if(persen_m[k]==null){
					persen_m[k]=0.;
				}
				total_persen +=persen_m[k];
				tot_persen = tot_persen.add(BigDecimal.valueOf(persen_m[k]));
				
				if (hsl_persen_m.trim().length()!=0)
				{
					err.rejectValue("datausulan.daftabenef["+k+"].msaw_persen","","HALAMAN INVESTASI :" +hsl_persen_m);
				}
				
				if (nama_m[k].trim().equals(edit.getTertanggung().getMcl_first().trim())){
					err.rejectValue("datausulan.daftabenef["+k+"].msaw_first","","HALAMAN INVESTASI : Tertanggung tidak dapat menjadi Ahli Waris" );
				}
				
				String cab_bank = edit.getPemegang().getCab_bank();
				int jn_bank = edit.getPemegang().getJn_bank();
				if (cab_bank == null)
				{
					cab_bank="";
				}
				//apabila inputan bank
				if (!cab_bank.equalsIgnoreCase(""))
				{
					if ((relation_m[k].intValue() != 7 ) && (relation_m[k].intValue() != 4 ) &&(relation_m[k].intValue() != 2 )&&(relation_m[k].intValue() != 5 ))
					{
						//err.rejectValue("datausulan.daftabenef["+k+"].lsre_id","","HALAMAN INVESTASI : Hubungan dengan ahli waris ke "+(k+1)+" yang diperbolehkan hanya  adik/kakak kandung, anak kandung, orang tua kandung,suami istri, Silahkan pilih kembali.");
					}
				}else{
					//eka siswa emas itu boleh diri sendiri jatiningsih 26/12/2007.
					if (relation_m[k].intValue() == 1 && edit.getDatausulan().getLsbs_id()!=31 && edit.getDatausulan().getLsbs_id()!=172)
					{
						err.rejectValue("datausulan.daftabenef["+k+"].lsre_id","","HALAMAN INVESTASI : Hubungan dengan ahli waris ke "+(k+1)+" tidak boleh diri sendiri, Silahkan pilih kembali.");
					}
				}
				
			}
			if(edit.getDatausulan().getLsbs_id()!=183 && edit.getDatausulan().getLsbs_id()!=193 && edit.getDatausulan().getLsbs_id()!=131 && edit.getDatausulan().getLsbs_id()!=132){
				if(tot_persen.compareTo(BigDecimal.valueOf(100)) !=0  ){
					err.reject("","HALAMAN INVESTASI : Jumlah Persentase bagian Penerima Manfaat asuransi harus 100%");
				}
			}
			
			List benef2 = new ArrayList();
			for (int k=0 ; k < (jmlh_penerima.intValue()) ; k++)
			{
				Benefeciary pmanfaat = new Benefeciary();
				pmanfaat.setMsaw_first(nama_m[k]);
				pmanfaat.setMsaw_birth(tgllhr_m[k]);
				pmanfaat.setLsre_id(relation_m[k]);
				pmanfaat.setMsaw_sex(sex_m[k]);
				pmanfaat.setMsaw_persen(persen_m[k]);
				benef2.add(pmanfaat);
			}
			
			if (products.stableLink(edit.getDatausulan().getLsbs_id().toString()))
			{
				Date tgl_begdate_topup = edit.getPowersave().getBegdate_topup();
				if (tgl_begdate_topup==null)
				{
					tgl_begdate_topup = edit.getDatausulan().getMste_beg_date();
				}
			}
			
	
		}
		catch (ClassNotFoundException e)
		{
			logger.error("ERROR :", e);
		} catch (InstantiationException e) {
			logger.error("ERROR :", e);
		} catch (IllegalAccessException e) {
			logger.error("ERROR :", e);
		}	

		if((edit.getDatausulan().getLsbs_id().intValue() != 158 && edit.getPowersave().getMpr_breakable() != null) || (edit.getDatausulan().getLsbs_id().intValue() != 176 && edit.getPowersave().getMpr_breakable() != null) ) {
			if(edit.getPowersave().getMpr_breakable().intValue() == 1) {
				err.reject("","HALAMAN INVESTASI : Pilihan 'Breakable' hanya bisa dipilih untuk produk Powersave Bulanan");
			}
		}
		
		if(edit.getDatausulan().getMste_flag_el()==null){
			edit.getDatausulan().setMste_flag_el(new Integer(0));
		}
		if(edit.getDatausulan().getMste_flag_el()==1){
			String kode_perusahaan = edit.getPemegang().getMspo_customer();
			if (kode_perusahaan==null)
			{
				kode_perusahaan="";
				edit.getPemegang().setMspo_customer(kode_perusahaan);
			}
			
			String nama_perusahaan="";
			if(kode_perusahaan!=null)
			{
				Map data1= (HashMap) this.elionsManager.select_namacompany(kode_perusahaan);
				if (data1!=null)
				{		
					nama_perusahaan = (String)data1.get("COMPANY_NAMA");
				}
			}
			edit.getPemegang().setMspo_customer(kode_perusahaan);
			edit.getPemegang().setMspo_customer_nama(nama_perusahaan);
			if (nama_perusahaan.toUpperCase().contains("EKA LIFE") || nama_perusahaan.toUpperCase().contains("EKALIFE"))
			{
//				edit.getDatausulan().setMste_flag_el(new Integer(1));
				edit.getDatausulan().setFlag_as(2);
			}
		}
	}
	
	public void validatequestionare(Object cmd, Errors err) 
	{
		logger.debug("EditBacValidator : validate page validatequestionare");
		Cmdeditbac edit= (Cmdeditbac) cmd;
		String kode_regional="";
		String nama_regional="";
		String kode_penutup="";
		String nama_penutup="";
		String cabang="";
		Integer ulink=new Integer(0);
		Integer sertifikasi=new Integer(0);
		Date tanggal_sertifikat=null;
		
		/** Set default untuk kode_cabang, wakil dan region dari agent*/
		Map data;
		data = (HashMap) this.elionsManager.selectagenpenutup(edit.getAgen().getMsag_id());
		if (data!=null)
		{		
			edit.getAgen().setMcl_first((String)data.get("NAMA"));
			edit.getAgen().setKode_regional((String)data.get("REGIONID"));
			edit.getAgen().setLca_id((String)data.get("LCAID"));
		}
		
		edit.getAddressbilling().setRegion(edit.getAgen().getKode_regional());
		
		if (kode_penutup.equalsIgnoreCase("000000"))
		{
			nama_penutup=edit.getAgen().getMcl_first();
			if (nama_penutup==null)
			{
				nama_penutup="";
				edit.getAgen().setMcl_first(nama_penutup);
			}
		}
		
		/** Validasi untuk questionare yang harus diisi*/
		if(edit.getMedQuest().getMsadm_sehat()==null){
			err.reject("","HALAMAN QUESTIONARE : Silakan diisi terlebih dahulu pernyataan mengenai kesehatan");
		}
		
		if(edit.getMedQuest().getMsadm_operasi()==null){
			err.reject("","HALAMAN QUESTIONARE : Silakan diisi terlebih dahulu apakah anda pernah melakukan operasi");
		}
		
		if(edit.getMedQuest().getMsadm_penyakit()==null){
			err.reject("","HALAMAN QUESTIONARE : Silakan diisi terlebih dahulu pernyataan mengenai penyakit");
		}
		
		if(edit.getMedQuest().getMsadm_medis()==null){
			err.reject("","HALAMAN QUESTIONARE : Silakan diisi terlebih dahulu pernyataan mengenai medical");
		}
	
	}
}