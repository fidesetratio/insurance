package com.ekalife.elions.web.bac.support;

import id.co.sinarmaslife.std.spring.util.Email;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
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
import com.ekalife.elions.model.Hadiah;
import com.ekalife.elions.model.Hcp;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.Products;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.f_validasi;

/**
 * @author HEMILDA
 * validator editbacController
 * untuk validasi penginputan bac
 */

public class Editbacvalidator implements Validator{

	protected final Log logger = LogFactory.getLog(getClass());
	
	private ElionsManager elionsManager;
	private BacManager bacManager;
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
	
	public void setBacManager(BacManager bacManager) {
		this.bacManager = bacManager;
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
		
		//2& 16 =  BANK SINARMAS && BANK SINARMAS Syariah
		if(jenis == 2 || jenis== 16) {
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
			
			Integer mu_lspd_id = elionsManager.selectPosisiUlink(spaj);
			if(mu_lspd_id != null){
				if(mu_lspd_id.intValue() >= 42){
					err.reject("", "Polis ini sudah di-FUND, tidak boleh melakukan perubahan data. Terima kasih.");
				}
			}
			
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
		
		//APABILA INPUTAN BANK
//		if (!cab_bank.equalsIgnoreCase("")){
//			if ((hubungan.intValue() != 1 ) && (hubungan.intValue() != 7 ) && (hubungan.intValue() != 4 ) &&(hubungan.intValue() != 2 )&&(hubungan.intValue() != 5 )){
//				err.rejectValue("pemegang.lsre_id","","PEMEGANG: Hubungan dengan tertanggung yang diperbolehkan hanya diri sendiri, adik/kakak kandung, anak kandung, orang tua kandung,suami istri, Silahkan pilih kembali.");
//			}
//		}
//		if(edit.getDatausulan().isPsave){
//			if(uwManager.selectCountMstSurrender(edit.getDatausulan().getKopiSPAJ())==0){
//				err.reject("", "Polis ini belum dilakukan proses Surrender, sehingga tidak dapat dilakukan pengkonversian, Harap hubungi Departemen Life Benefit.");
//			}
//		}
		
		//apabila inputan bank, CIF harus divalidasi
		if (!cab_bank.equalsIgnoreCase("") && edit.getPemegang().getJn_bank().intValue()!=3)
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mspo_nasabah_dcif","","PEMEGANG: Silahkan lengkapi No CIF.");
				
		//apabila inputan BII, no_pb harus diisi
		if(edit.getPemegang().getJn_bank().intValue() == 0 || edit.getPemegang().getJn_bank().intValue() == 1)
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.no_pb","","PEMEGANG: Silahkan lengkapi No PB.");
		
		//tambahan Yusuf (10/7/08) atas permintaan Rudy : bila sudah ada pembyaaran powersave, gak boleh edit sebelum dicancel oleh Life Benefit pembayarannya
		if(elionsManager.selectCountProsaveBayar(spaj) > 0) {
			err.reject("", "SPAJ ini sudah mempunyai pembayaran Powersave. Pembayaran harus di CANCEL terlebih dahulu oleh LIFE BENEFIT sebelum data bisa diedit.");
		}
		
		//nomor seri
		String nomor_seri= edit.getPemegang().getMspo_no_blanko();
		if ((nomor_seri==null||nomor_seri.equals("")) && !edit.getCurrentUser().getLca_id().equals("58") && !edit.getCurrentUser().getLus_id().equals("2661") ){
			
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mspo_no_blanko", "", "PEMEGANG: Harap Lengkapi No Blanko");
//			nomor_seri="";
			
		}
		
		
		String no_blanko = "";
		Integer jumlah_blanko = null;
		if(!edit.getCurrentUser().getLca_id().equals("58")){
			no_blanko = edit.getPemegang().getMspo_no_blanko();
			jumlah_blanko = (Integer) this.elionsManager.cekNoBlanko(no_blanko);
		
			if (jumlah_blanko.intValue() > 0){
				edit.getPemegang().setCek_blanko(1);
//				err.rejectValue("pemegang.mspo_no_blanko","","PEMEGANG: NO BLANKO YANG DICANTUMKAN SUDAH PERNAH DIGUNAKAN UNTUK PRODUK YANG SAMA");
//				ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mspo_no_blanko", "", "PEMEGANG: NO BLANKO YANG DICANTUMKAN SUDAH PERNAH DIGUNAKAN UNTUK PRODUK YANG SAMA");
			}else{
				edit.getPemegang().setCek_blanko(0);
			}
		}else{
			no_blanko = "MA"+ FormatString.rpad("0", Long.toString(elionsManager.select_counter(71, "58")), 7);
			edit.getPemegang().setMspo_no_blanko(no_blanko);
			nomor_seri = edit.getPemegang().getMspo_no_blanko();
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

		/**
		 * @author HEMILDA
		 * kalau produk worksite maka no blanko boleh isi boleh tidak
		 */
		if (flag_worksite != null && !edit.getCurrentUser().getLus_id().equals("2661")){
			if (flag_worksite.intValue() == 0){
				String hasil_nomor_seri = pp.no_seri(nomor_seri);
				//apabila bukan inputan bank
				if (cab_bank.equalsIgnoreCase("")){
					if(hasil_nomor_seri.trim().length()!=0 ){
						edit.getPemegang().setMspo_no_blanko(f_validasi.convert_karakter(nomor_seri));
						if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0){
							err.rejectValue("pemegang.mspo_no_blanko","","PEMEGANG:" +hasil_nomor_seri);
						}
					}
				}

			}else{
				edit.getPemegang().setMspo_no_blanko("");
//				cek nama perusahaan kalau sudah dipilih
				String kode_perusahaan = edit.getPemegang().getMspo_customer();
				String nama_perusahaan="";
				if(kode_perusahaan!=null){
					Map data1= (HashMap) this.elionsManager.select_namacompany(kode_perusahaan);
					if (data1!=null){		
						nama_perusahaan = (String)data1.get("COMPANY_NAMA");
					}
				}else{
					kode_perusahaan="";
				}
				edit.getPemegang().setMspo_customer_nama(nama_perusahaan);
				String nama_karyawan="";
				String cabang_karyawan="";
				String dept_karyawan="";
				String nik_worksite = edit.getPemegang().getNik();
				if (nik_worksite ==null){
					nik_worksite="";
				}
				
				/**
				 * @author HEMILDA
				 * khusus pt ekalife
				 */
				if (nama_perusahaan.toUpperCase().contains("EKA LIFE") 
						|| nama_perusahaan.toUpperCase().contains("EKALIFE") 
						|| nama_perusahaan.toUpperCase().contains("ASURANSI JIWA SINARMAS") 
						|| nama_perusahaan.toUpperCase().contains("ASURANSI JIWA SINAR MAS")
						|| nama_perusahaan.toUpperCase().contains("ASURANSI JIWA SINARMAS MSIG"))
				{
					Map data2= (HashMap) this.elionsManager.select_nik_karyawan(nik_worksite);

					if (data2!=null){		
						nama_karyawan = (String)data2.get("MCL_FIRST");
						cabang_karyawan = (String)data2.get("LCA_NAMA");
						dept_karyawan = (String)data2.get("LDE_DEPT");
						edit.getDatausulan().setFlag_worksite_ekalife(new Integer(1));
						edit.getEmployee().setNama(nama_karyawan);
						edit.getEmployee().setCabang(cabang_karyawan);
						edit.getEmployee().setDept(dept_karyawan);
						edit.getEmployee().setNik(nik_worksite);
						edit.getEmployee().setNo_urut(new Integer(1));
//						edit.getDatausulan().setMste_flag_el(new Integer(1));
					}	
				}
			}
		}
		edit.getPemegang().setMspo_no_blanko(f_validasi.convert_karakter(nomor_seri));
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
		
		if(edit.getPemegang().getMspo_flag_spaj()>0){
			no_blanko = edit.getPemegang().getMspo_no_blanko();
			jumlah_blanko = (Integer) this.elionsManager.cekNoBlanko(no_blanko);
			if(jumlah_blanko>0 && spaj.equals("")){
				err.rejectValue("pemegang.mspo_no_blanko", "", "No blanko sudah pernah digunakan untuk Polis Lain");			
			}
			if(no_blanko.length()!=12){
				err.rejectValue("pemegang.mspo_no_blanko", "", "Untuk Jenis Form SPAJ BARU no blanko harus berjumlah 12 digit ");
			}
		}
		
		//cek data sama no ktp dan no blanko
		//cek no blanko sudah pernah dipakai  atau belum
		if (!spaj.equalsIgnoreCase("")){
			// Yusup A. : no blanko terdiri dari min 1 char, max 4 char di awal & 6 digit angka
			// Update(Rev Deddy) : no blanko terdiri dari min 0 char, max 4 char di awal & sisanya min 1, max 6  digit angka
//			if(!no_blanko.matches("^[a-zA-Z]{1,4}\\d{6}")) {
//				err.rejectValue("pemegang.mspo_no_blanko","","no blanko terdiri dari min 1 char, max 4 char di awal & 6 digit angka tanpa ada spasi");
//			}
			if(edit.getCurrentUser().getLca_id().equals("58")){
				
			}else{
				if(edit.getPemegang().getLus_id()!=2661){
						
					no_blanko = edit.getPemegang().getMspo_no_blanko();
					if(edit.getPemegang().getMspo_flag_spaj()==0){
						edit.getTertanggung().setMste_no_vacc("");
						if(no_blanko.length()>11){
							err.rejectValue("pemegang.mspo_no_blanko", "", " Untuk Jenis Form SPAJ LAMA no blanko max 11 digit (KETENTUAN : min 0, max 4 char di awal; sisanya min 1, max 7  digit angka)");
						}
						for(int i=0; i<no_blanko.length();i++){
							String digit ;
							digit = no_blanko.substring(i, i+1);
		//					if(i==0 & !digit.toLowerCase().matches("^[a-z]")){
		//						err.rejectValue("pemegang.mspo_no_blanko", "", "no blanko min 1, max 4 char/huruf untuk digit awalnya (KETENTUAN : min 0, max 4 char di awal; sisanya min 1, max 6  digit angka)");
		//					}
							if(i>=4 && digit.toLowerCase().matches("^[a-z]")){
								err.rejectValue("pemegang.mspo_no_blanko", "", "Untuk Jenis Form SPAJ LAMA  no blanko digit ke"+(i+1) +" harus berupa angka (KETENTUAN : min 0, max 4 char di awal; sisanya min 1, max 6  digit angka)");
							}
						}
						String digitakhir = no_blanko.substring(no_blanko.length()-1, no_blanko.length());
						if(!f_validasi.f_validasi_numerik(digitakhir)){
							err.rejectValue("pemegang.mspo_no_blanko", "", "Untuk Jenis Form SPAJ LAMA  digit Terakhir harus berupa angka (KETENTUAN : min 0, max 4 char di awal; sisanya min 1, max 6 digit angka)");
						}
						
						if (no_blanko.equalsIgnoreCase("") || no_blanko==null){
							no_blanko="-";
						}
						jumlah_blanko = (Integer) this.elionsManager.cekNoBlanko(no_blanko);
						if (jumlah_blanko == null){
							jumlah_blanko = 0;
						}
					}else{
						
						if(no_blanko.length()!=12){
							err.rejectValue("pemegang.mspo_no_blanko", "", "Untuk Jenis Form SPAJ BARU no blanko harus berjumlah 12 digit ");
						}
//						
				    }
			    }
				
		    }
			String ktp = edit.getPemegang().getMspe_no_identity();
			if (ktp == null){
				ktp ="";
			}
			String lsbs_id=null;
			if(edit.getDatausulan().getLsbs_id()!=null){
				lsbs_id = edit.getDatausulan().getLsbs_id().toString();
				if (lsbs_id== null){
					lsbs_id ="";
				}
			}
			
			String lsdbs_number = null;
			if(edit.getDatausulan().getLsdbs_number()!=null){
				lsdbs_number=edit.getDatausulan().getLsdbs_number().toString();
				if (lsdbs_number == null){
					lsdbs_number = "";
				}
			}
			
			Double up = edit.getDatausulan().getMspr_tsi();
			if (up ==null){
				up = new Double(0);
			}
			
			Integer jumlah_ktp = (Integer) this.elionsManager.cek_data_sama(spaj, ktp, lsbs_id, lsdbs_number, up);
			if (jumlah_ktp.intValue() > 0){
				edit.getDatausulan().setFlag_ktp(1);
				List<Map<String, Object>> data_spaj = this.elionsManager.cek_spaj_sama1(spaj, ktp, lsbs_id, lsdbs_number, up);
				if (data_spaj != null){
					Map m = (Map)data_spaj.get(0);
					edit.getDatausulan().setSpaj_ktp(m.get("REG_SPAJ").toString());
				}else{
					edit.getDatausulan().setSpaj_ktp("");
				}
			}else{
				edit.getDatausulan().setFlag_ktp(0);
				edit.getDatausulan().setSpaj_ktp("");
			}
		}
		//Untuk Input Dicek Blankonya
		if (spaj.equalsIgnoreCase("")){
			if(edit.getPemegang().getLus_id()!=2661){

				no_blanko = edit.getPemegang().getMspo_no_blanko();
				if(edit.getPemegang().getMspo_flag_spaj()==0){
					edit.getTertanggung().setMste_no_vacc("");
					if(no_blanko.length()>11){
						err.rejectValue("pemegang.mspo_no_blanko", "", " Untuk Jenis Form SPAJ LAMA no blanko max 11 digit (KETENTUAN : min 0, max 4 char di awal; sisanya min 1, max 7  digit angka)");
					}
					for(int i=0; i<no_blanko.length();i++){
						String digit ;
						digit = no_blanko.substring(i, i+1);
						//					if(i==0 & !digit.toLowerCase().matches("^[a-z]")){
						//						err.rejectValue("pemegang.mspo_no_blanko", "", "no blanko min 1, max 4 char/huruf untuk digit awalnya (KETENTUAN : min 0, max 4 char di awal; sisanya min 1, max 6  digit angka)");
						//					}
						if(i>=4 && digit.toLowerCase().matches("^[a-z]")){
							err.rejectValue("pemegang.mspo_no_blanko", "", "Untuk Jenis Form SPAJ LAMA  no blanko digit ke"+(i+1) +" harus berupa angka (KETENTUAN : min 0, max 4 char di awal; sisanya min 1, max 6  digit angka)");
						}
					}
					String digitakhir = no_blanko.substring(no_blanko.length()-1, no_blanko.length());
					if(!f_validasi.f_validasi_numerik(digitakhir)){
						err.rejectValue("pemegang.mspo_no_blanko", "", "Untuk Jenis Form SPAJ LAMA  digit Terakhir harus berupa angka (KETENTUAN : min 0, max 4 char di awal; sisanya min 1, max 6 digit angka)");
					}

					if (no_blanko.equalsIgnoreCase("") || no_blanko==null){
						no_blanko="-";
					}
					jumlah_blanko = (Integer) this.elionsManager.cekNoBlanko(no_blanko);
					if (jumlah_blanko == null){
						jumlah_blanko = 0;
					}
				}else{

					if(no_blanko.length()!=12){
						err.rejectValue("pemegang.mspo_no_blanko", "", "Untuk Jenis Form SPAJ BARU no blanko harus berjumlah 12 digit ");
					}
					//				
				}
			}
		}
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

		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			Date tgl_expiredPIC = edit.getContactPerson().getNo_identity_expired();
			logger.info(edit.getContactPerson().getLside_id());
			if("4,5,7,9".indexOf(edit.getContactPerson().getLside_id().toString())<=-1){
				if (tgl_expiredPIC==null)err.rejectValue("contactPerson.no_identity_expired","","PIC: Harap Lengkapi Tanggal Kadaluarsa Bukti Identitas PIC.");
				//ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mspe_no_identity_expired","","PEMEGANG: Harap Lengkapi Tanggal Kadaluarsa Bukti Identitas Pemegang Polis.");
				//if (tgl_expired == null)ValidationUtils.rejectIfEmptyOrWhitespace(err, "contactPerson.no_identity_expired","","PIC: Harap Lengkapi Tanggal Kadaluarsa Bukti Identitas PIC.");
			}else{}
		}else{
			if("4,5,7,9".indexOf(edit.getPemegang().getLside_id().toString())<=-1){
				Date tgl_expired = edit.getPemegang().getMspe_no_identity_expired();
//				ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mspe_no_identity_expired","","PEMEGANG: Harap Lengkapi Tanggal Kadaluarsa Bukti Identitas Pemegang Polis.");
				if (tgl_expired==null)err.rejectValue("pemegang.mspe_no_identity_expired","","PEMEGANG: Harap Lengkapi Tanggal Kadaluarsa Bukti Identitas Pemegang Polis.");
			}else{}
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
		
		//Andhika (31/05/2013)
		Integer lsne_tgh=edit.getAddressbilling().getLsne_id();
		if (lsne_tgh==null)
		{
			lsne_tgh=0;
		}
		if (tagih.equalsIgnoreCase("2"))
		{
			if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
				lsne_tgh=edit.getContactPerson().getLsne_id();
			}else{
				lsne_tgh=edit.getPemegang().getLsne_id();
			}
		}else{
			if (tagih.equalsIgnoreCase("3"))
			{
				if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
					lsne_tgh=edit.getContactPerson().getLsne_id();
				}else{
					lsne_tgh=edit.getPemegang().getLsne_id();
				}
			}
		}
		String hasil_lsne_tgh = pp.lsne_tgh(lsne_tgh.toString());
		if (hasil_lsne_tgh.trim().length()!=0)
		{
			edit.getAddressbilling().setLsne_id(lsne_tgh);
			if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				err.rejectValue("addressbilling.lsne_id","","PEMEGANG:" +hasil_lsne_tgh);
			}
		}
		edit.getAddressbilling().setLsne_id(lsne_tgh);		
		
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
		if (telp_tgh==null){
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
		
		if(hp1_tgh.equals("")){
			err.rejectValue("addressbilling.no_hp","","PEMEGANG: Harap lengkapi No Handphone alamat penagihan");
		}
		
		if(hp1_tgh.trim().length()>1){
			String validHp=f_validasi.cek_nohp(edit.getAddressbilling().getNo_hp(), true);
			if(validHp.trim().length()!=0){
				err.rejectValue("addressbilling.no_hp","","PEMEGANG:" +validHp);
			}
		}else{
			err.rejectValue("addressbilling.no_hp","","PEMEGANG: Jika Tidak ada no hp Harap isi No Handphone alamat penagihan dengan 0000" );
		}
		
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
		if(hp1_tgh.trim().length()!=0){
			String validHp=f_validasi.cek_nohp(edit.getAddressbilling().getNo_hp(), true);
			if(validHp.trim().length()!=0){
				err.rejectValue("addressbilling.no_hp","","PEMEGANG:" +validHp);
			}
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
		
//		*Hubungan Pembayar Premi Dengan Pemegang Polis
		Integer lsre_id_pre = edit.getPemegang().getLsre_id_premi();
		edit.getPemegang().setLsre_id_premi(lsre_id_pre);
		
//		*Sumber pendanaan Diri Sendiri atau Bukan
		Integer dana_from = edit.getPemegang().getMkl_dana_from();
		edit.getPemegang().setMkl_dana_from(dana_from);
		
//		*Sumber Penghasilan Diri Sendiri atau Bukan
		Integer hasil_from = edit.getPemegang().getMkl_hasil_from();
		edit.getPemegang().setMkl_hasil_from(hasil_from);
		
		Integer smbr_hasil_from = edit.getPemegang().getMkl_smbr_hasil_from();
		edit.getPemegang().setMkl_smbr_hasil_from(smbr_hasil_from);
		
//		*Sumber Premi Perorangan atau Badan Usaha
		Integer sumber_premi = edit.getPemegang().getMkl_sumber_premi();
		edit.getPemegang().setMkl_sumber_premi(sumber_premi);
		
//		*Sumber pendanaan pembelian asuransi
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
//		String sumber_penghasilan_oth = edit.getPemegang().getDanaa2();
		String sumber_penghasilan_oth = edit.getPemegang().getShasil();
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
//				edit.getPemegang().setDanaa2(f_validasi.convert_karakter(sumber_penghasilan_oth));
				edit.getPemegang().setShasil(f_validasi.convert_karakter(sumber_penghasilan_oth));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
					if(edit.getDatausulan().getJenis_pemegang_polis() == 0){
//						err.rejectValue("pemegang.danaa2","","PEMEGANG:" +hasil_sumber_penghasilan);
						err.rejectValue("pemegang.shasil","","PEMEGANG:" +hasil_sumber_penghasilan);
					}
				}
			}
//			edit.getPemegang().setDanaa2(f_validasi.convert_karakter(sumber_penghasilan_oth));
			edit.getPemegang().setShasil(f_validasi.convert_karakter(sumber_penghasilan_oth));
		}else{
//			edit.getPemegang().setDanaa2("");
			edit.getPemegang().setShasil("");
		}

		//	untuk input perusahaan (mkl_kerja)
		if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
			edit.getPemegang().setMkl_kerja("Karyawan");
			edit.getPemegang().setKerjaa("");
		}
		
		//Klasifikasi Pekerjaan Pemegang
		String pekerjaan= edit.getPemegang().getMkl_kerja();
		String pekerjaan_oth= edit.getPemegang().getKerjaa();
		String hasil_pekerjaan="";
		if (pekerjaan==null)
		{
			pekerjaan="Lainnya";
		}
		if(pekerjaan.trim().toUpperCase().equalsIgnoreCase("IBU")||pekerjaan.trim().toUpperCase().equalsIgnoreCase("PELAJAR")||pekerjaan.trim().toUpperCase().equalsIgnoreCase("PEJABAT")
				   ||pekerjaan.trim().toUpperCase().equalsIgnoreCase("PNS")||pekerjaan.trim().toUpperCase().equalsIgnoreCase("PENGACARA/ADVOKAT")||pekerjaan.trim().toUpperCase().equalsIgnoreCase("POLRI")
				   ||pekerjaan.trim().toUpperCase().equalsIgnoreCase("TNI")||pekerjaan.trim().toUpperCase().equalsIgnoreCase("HAKIM")||pekerjaan.trim().toUpperCase().equalsIgnoreCase("AKUNTAN/KONSULTASI KEUANGAN")){
					edit.getPemegang().setMkl_red_flag(1);
					edit.getTertanggung().setMkl_red_flag(1);
				}else{
					edit.getPemegang().setMkl_red_flag(0);
					edit.getTertanggung().setMkl_red_flag(0);
					//edit.getInvestasiutama().getDaftartopup().setRedFlag_topup_berkala(0);
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

		if (pekerjaan.trim().equalsIgnoreCase("Karyawan Swasta"))
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
		edit.getTertanggung().setMste_flag_hadiah(0);

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
		
//		cek no blanko sudah pernah dipakai  atau belum
		if (!spaj.equalsIgnoreCase(""))
		{
			Integer kode_produk = edit.getDatausulan().getLsbs_id();
//			Integer number_produk = edit.getDatausulan().getLsdbs_number();
//			String no_blanko = edit.getPemegang().getMspo_no_blanko();
//			
//			if (no_blanko == null)
//			{
//				no_blanko ="-";
//			}
//			if (no_blanko.equalsIgnoreCase(""))
//			{
//				no_blanko="-";
//			}
			//Integer jumlah_blanko = (Integer) this.elionsManager.count_no_blanko_perspaj(Integer.toString(kode_produk), Integer.toString(number_produk), no_blanko, spaj);
			if(!edit.getCurrentUser().getLca_id().equals("58")){
				jumlah_blanko = (Integer) this.elionsManager.cekNoBlanko(no_blanko);
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
			}
			
			
			String ktp = edit.getPemegang().getMspe_no_identity();
			if (ktp == null)
			{
				ktp ="";
			}
			String lsbs_id=null;
			if(edit.getDatausulan().getLsbs_id()!=null){
				lsbs_id = edit.getDatausulan().getLsbs_id().toString();
				if (lsbs_id== null){
					lsbs_id ="";
				}
			}
			
			String lsdbs_number = null;
			if(edit.getDatausulan().getLsdbs_number()!=null){
				lsdbs_number=edit.getDatausulan().getLsdbs_number().toString();
				if (lsdbs_number == null){
					lsdbs_number = "";
				}
			}
			Double up = edit.getDatausulan().getMspr_tsi();
			if (up ==null)
			{
				up = new Double(0);
			}
			Integer jumlah_ktp = (Integer) this.elionsManager.cek_data_sama(spaj, ktp, lsbs_id, lsdbs_number, up);
			if (jumlah_ktp.intValue() > 0)
			{
				edit.getDatausulan().setFlag_ktp(new Integer(1));
				List<Map<String, Object>> data_spaj = this.elionsManager.cek_spaj_sama1(spaj, ktp, lsbs_id, lsdbs_number, up);
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
			if(!edit.getCurrentUser().getLca_id().equals("58")){
				no_blanko = edit.getPemegang().getMspo_no_blanko();

				if (!no_blanko.equalsIgnoreCase(""))
				{
					if (!no_blanko.equalsIgnoreCase("-"))
					{
						String spaj_err = this.elionsManager.blanko_sama(no_blanko);
						if (spaj_err == null)
						{
							spaj_err = "";
						}
						edit.getPemegang().setKeterangan_blanko("NONE");
						if (!spaj_err.equalsIgnoreCase(""))
						{
							edit.getPemegang().setKeterangan_blanko("No Blanko ini sudah pernah diinput pada no spaj "+spaj_err);
							edit.getPemegang().setKeterangan_blanko_spaj(spaj_err);
						}
					}
				}
			}
			 
				
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
//				edit.getTertanggung().setDanaa2(edit.getPemegang().getDanaa2());
				edit.getTertanggung().setShasil(edit.getPemegang().getShasil());
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
		/*		if(edit.getPemegang().getFlag_upload()==3){
				edit.getTertanggung().setMste_no_vacc(no_virtual_acc);}*/
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
				edit.getTertanggung().setLsre_id_premi(edit.getPemegang().getLsre_id_premi());
				edit.getTertanggung().setMkl_smbr_penghasilan(edit.getPemegang().getMkl_smbr_penghasilan());
				edit.getTertanggung().setMkl_tujuan(edit.getPemegang().getMkl_tujuan());
				edit.getTertanggung().setMpn_job_desc(edit.getPemegang().getMpn_job_desc());
				edit.getTertanggung().setMsag_id(edit.getPemegang().getMsag_id());
				edit.getTertanggung().setMspe_date_birth(edit.getPemegang().getMspe_date_birth());
				edit.getTertanggung().setMspe_lama_kerja(edit.getPemegang().getMspe_lama_kerja());
				edit.getTertanggung().setMspe_mother(edit.getPemegang().getMspe_mother());
				edit.getTertanggung().setMspe_no_identity(edit.getPemegang().getMspe_no_identity());
				edit.getTertanggung().setMspe_no_identity_expired(edit.getPemegang().getMspe_no_identity_expired());
				edit.getTertanggung().setMspe_place_birth(edit.getPemegang().getMspe_place_birth());
				edit.getTertanggung().setMspe_sex(edit.getPemegang().getMspe_sex());
				edit.getTertanggung().setMspe_sex2(edit.getPemegang().getMspe_sex2());
				edit.getTertanggung().setMkl_dana_from(edit.getPemegang().getMkl_dana_from());
				edit.getTertanggung().setMkl_hasil_from(edit.getPemegang().getMkl_hasil_from());
				edit.getTertanggung().setMkl_smbr_hasil_from(edit.getPemegang().getMkl_smbr_hasil_from());
				edit.getTertanggung().setMkl_sumber_premi(edit.getPemegang().getMkl_sumber_premi());
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
				edit.getTertanggung().setMkl_red_flag(edit.getPemegang().getMkl_red_flag());
				
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
//						edit.getTertanggung().setDanaa2(edit.getPemegang().getDanaa2());
						edit.getTertanggung().setShasil(edit.getPemegang().getShasil());
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
				/*		if(edit.getPemegang().getFlag_upload()==3){
						edit.getTertanggung().setMste_no_vacc(no_virtual_acc);}*/
						edit.getTertanggung().setMkl_industri(edit.getPemegang().getMkl_industri());
						edit.getTertanggung().setMkl_kerja(edit.getPemegang().getMkl_kerja());
						edit.getTertanggung().setMkl_pendanaan(edit.getPemegang().getMkl_pendanaan());
						edit.getTertanggung().setMkl_smbr_penghasilan(edit.getPemegang().getMkl_smbr_penghasilan());
						edit.getTertanggung().setMkl_penghasilan(edit.getPemegang().getMkl_penghasilan());
						 edit.getTertanggung().setMkl_dana_from(edit.getPemegang().getMkl_dana_from());
						 edit.getTertanggung().setMkl_hasil_from(edit.getPemegang().getMkl_hasil_from());
						 edit.getTertanggung().setMkl_smbr_hasil_from(edit.getPemegang().getMkl_smbr_hasil_from());
						 edit.getTertanggung().setMkl_sumber_premi(edit.getPemegang().getMkl_sumber_premi());
						 edit.getTertanggung().setLsre_id_premi(edit.getPemegang().getLsre_id_premi());
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
//						edit.getTertanggung().setDanaa2(edit.getPemegang().getDanaa2());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setShasil(edit.getPemegang().getShasil());
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
						edit.getTertanggung().setLsre_id_premi(edit.getPemegang().getLsre_id_premi());
						edit.getTertanggung().setMkl_smbr_penghasilan(edit.getPemegang().getMkl_smbr_penghasilan());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setMkl_tujuan(edit.getPemegang().getMkl_tujuan());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setMpn_job_desc(edit.getContactPerson().getMpn_job_desc());
						edit.getTertanggung().setMsag_id(edit.getPemegang().getMsag_id());//DARI PEMEGANG BADAN USAHA
						edit.getTertanggung().setMspe_date_birth(edit.getContactPerson().getDate_birth());
						edit.getTertanggung().setMspe_lama_kerja(edit.getContactPerson().getLama_kerja());
						edit.getTertanggung().setMspe_mother(edit.getContactPerson().getMspe_mother());
						edit.getTertanggung().setMspe_no_identity(edit.getContactPerson().getNo_identity());
						edit.getTertanggung().setMspe_no_identity_expired(edit.getContactPerson().getNo_identity_expired());
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
						edit.getTertanggung().setMkl_red_flag(edit.getPemegang().getMkl_red_flag());
						
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
				List<Hcp> data_rider =this.elionsManager.select_hcp(edit.getPemegang().getReg_spaj());
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

		Date tgl_expired = edit.getTertanggung().getMspe_no_identity_expired(); 
		if("4,5,7,9".indexOf(edit.getTertanggung().getLside_id().toString())<=-1){
			if (tgl_expired == null)err.rejectValue("tertanggung.mspe_no_identity_expired","","HALAMAN TERTANGGUNG : Tanggal expired no. identitas masih kosong");	
		}

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
//		String sumber_penghasilan_oth = edit.getTertanggung().getDanaa2();
		String sumber_penghasilan_oth = edit.getTertanggung().getShasil();
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
//				edit.getTertanggung().setDanaa2(f_validasi.convert_karakter(sumber_penghasilan_oth));
				edit.getTertanggung().setShasil(f_validasi.convert_karakter(sumber_penghasilan_oth));
				if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
				{
//					err.rejectValue("tertanggung.danaa2","","HALAMAN TERTANGGUNG :" +hasil_sumber_penghasilan);
					err.rejectValue("tertanggung.shasil","","HALAMAN TERTANGGUNG :" +hasil_sumber_penghasilan);
				}
			}
//			edit.getTertanggung().setDanaa2(f_validasi.convert_karakter(sumber_penghasilan_oth));
			edit.getTertanggung().setShasil(f_validasi.convert_karakter(sumber_penghasilan_oth));
		}else{
//			edit.getTertanggung().setDanaa2("");
			edit.getTertanggung().setShasil("");
		}

		
		//Klasifikasi Pekerjaan Tertanggung
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
		
			if(pekerjaan_ttg.trim().toUpperCase().equalsIgnoreCase("IBU")||pekerjaan_ttg.trim().toUpperCase().equalsIgnoreCase("PELAJAR")||pekerjaan_ttg.trim().toUpperCase().equalsIgnoreCase("PEJABAT")
				   ||pekerjaan_ttg.trim().toUpperCase().equalsIgnoreCase("PNS")||pekerjaan_ttg.trim().toUpperCase().equalsIgnoreCase("PENGACARA/ADVOKAT")||pekerjaan_ttg.trim().toUpperCase().equalsIgnoreCase("POLRI")
				   ||pekerjaan_ttg.trim().toUpperCase().equalsIgnoreCase("TNI")||pekerjaan_ttg.trim().toUpperCase().equalsIgnoreCase("HAKIM")||pekerjaan_ttg.trim().toUpperCase().equalsIgnoreCase("AKUNTAN/KONSULTASI KEUANGAN")){
					edit.getTertanggung().setMkl_red_flag(1);
					edit.getPemegang().setMkl_red_flag(1);
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
		if (pekerjaan_ttg.trim().equalsIgnoreCase("Karyawan Swasta"))
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
	//	edit.getPemegang().setMste_tgl_recur(tgl_begin_polis);
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
		//	edit.getPemegang().setMste_tgl_recur(tgl_begin_polis);
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
		if(edit.getCurrentUser().getLca_id().equals("58")){
			if (edit.getDatausulan().getTipeproduk()==null)
			{
				edit.getDatausulan().setTipeproduk(new Integer(13));
			}
		}
		if(edit.getDatausulan().getDaftaRider()!=null){
			Integer jumlah_r =new Integer(edit.getDatausulan().getDaftaRider().size());
			if (jumlah_r==null)
			{
				jumlah_r=new Integer(0);
			}
			
			Integer jmlrider=jumlah_r;
			edit.getDatausulan().setJmlrider(new Integer(jmlrider.intValue()));
		}
		if(edit.getDatausulan().getDaftarplus()!=null){
			Integer jumlah_p =new Integer(edit.getDatausulan().getDaftarplus().size());
			if (jumlah_p==null)
			{
				jumlah_p=new Integer(0);
			}
			
			Integer jml_peserta=jumlah_p;
			edit.getDatausulan().setJml_peserta(new Integer(jml_peserta.intValue()));
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
		
		Integer flag_sph = edit.getDatausulan().getMspo_flag_sph();
		if(flag_sph==null){
			flag_sph=0;
		}
		
		edit.getDatausulan().setCara_premi(new  Integer(0));
		edit.getDatausulan().setTotal_premi_kombinasi(new Double(0));
		
		
		//referensi(Tambang emas)
		//validasi apakah polis ini ada id referralnya
		/*if((edit.getAgen().getId_ref()==null || edit.getAgen().getId_ref().equals("")) || (edit.getAgen().getName_ref()==null || edit.getAgen().getName_ref().equals(""))){
			Integer ref = uwManager.selectCekRef(edit.getPemegang().getMcl_first(), defaultDateFormat.format(edit.getPemegang().getMspe_date_birth()), edit.getTertanggung().getMcl_first(), defaultDateFormat.format(edit.getTertanggung().getMspe_date_birth()));
			if(ref>0)edit.getAgen().setPesan_ref("Polis ini memiliki Id Referral");
		}else{
			edit.getAgen().setPesan_ref(null);
		}*/
		Integer ref = uwManager.selectCekRef(edit.getPemegang().getMcl_first(), defaultDateFormat.format(edit.getPemegang().getMspe_date_birth()), edit.getTertanggung().getMcl_first(), defaultDateFormat.format(edit.getTertanggung().getMspe_date_birth()));
		if(ref>0){
			edit.getAgen().setPesan_ref("Polis ini memiliki Id Referral");
		}else{
			edit.getAgen().setPesan_ref(null);
		}
		//end referensi
		
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
			
//			Integer autodebet=edit.getDatausulan().getMste_flag_cc();
//			if (autodebet==null)
//			{
//				autodebet=new Integer(0);
//				edit.getDatausulan().setMste_flag_cc(autodebet);
//			}			
			
			//autodebet
			Integer autodebet=edit.getDatausulan().getMste_flag_cc();
			if (autodebet == null)
			{
				err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN :Silakan isi terlebih dahulu bentuk pembayaran Premi");
			}
			edit.getDatausulan().setMste_flag_cc(autodebet);
			
			if(autodebet==0){
				edit.setAccount_recur(new Account_recur());
			}
			
			if(kode_produk.intValue()==190 && edit.getDatausulan().getLscb_id()==6 &&  "5,6".indexOf(edit.getDatausulan().getLsdbs_number().toString())<0){
				if(autodebet!=1 && autodebet!=2 && !edit.getFlag_special_case().equals("1")){
					err.rejectValue("datausulan.plan","","Untuk Produk Eka Bridge Link, apabila cara bayar bulanan harus auto debet/Tabungan ");
				}
			}
						
			if(edit.getCurrentUser().getLca_id().equals("58")){
				if(kode_produk.intValue() !=143 && kode_produk.intValue() !=164 && kode_produk.intValue() !=188){
					if(autodebet==0 && edit.getDatausulan().getLscb_id()!=3){
						err.rejectValue("datausulan.plan","","HALAMAN INVESTASI :Untuk Bentuk Pembayaran Premi secara TUNAI, hanya bisa periode TAHUNAN");
					}else if(autodebet==2 && edit.getDatausulan().getLscb_id()==0){
						err.rejectValue("datausulan.plan","","HALAMAN INVESTASI :Untuk Bentuk Pembayaran Premi secara TABUNGAN/AUTODEBET, tidak bisa SEKALIGUS");
					}
				}
			}
			if(kode_produk.intValue() == 185){//Super Sejahtera New
				if(edit.getDatausulan().getLscb_id()!=6 && edit.getDatausulan().getMste_flag_cc()==3){
					err.rejectValue("datausulan.mste_flag_cc","","HALAMAN DATA USULAN : Untuk cara Bayar Non Bulanan, bentuk pembayaran premi tidak bisa dilakukan melalui pemotongan gaji(PAYROLL).");
				}
			}
			
			if(products.stableSavePremiBulanan(edit.getDatausulan().getLsbs_id().toString())){
				if(autodebet>2){
					//Metode bayar  : Transfer / Autodebet Rek atau KK. 					
                    //Tidak dianjurkan  Autodebet KK  - karena dikenakan fee sebesar 2.5%
//					err.rejectValue("datausulan.mste_flag_cc", "", "HALAMAN DATA USULAN: Metode Bayar untuk Progressive Save hanya bisa Transfer/AutoDebet Rekening atau Kartu Kredit");
				}
			}
			
			if(products.progressiveLink(edit.getDatausulan().getLsbs_id().toString())){
				if(autodebet!=6 && edit.getDatausulan().getLsdbs_number()==3){
					err.rejectValue("datausulan.mste_flag_cc", "", "HALAMAN DATA USULAN: Metode Bayar untuk SSP PRO hanya bisa Debet SSP");
				}else if(autodebet==6 && edit.getDatausulan().getLsdbs_number()!=3){
					err.rejectValue("datausulan.mste_flag_cc", "", "HALAMAN DATA USULAN: Metode Bayar untuk Progressive Link selain Debet SSP");
				}
			}
			
			if(edit.getPowersave().getMps_roll_over()==null && (products.stableLink(edit.getDatausulan().getLsbs_id().toString()) || products.powerSave(edit.getDatausulan().getLsbs_id().toString()) || products.stableSave(edit.getDatausulan().getLsbs_id(), edit.getDatausulan().getLsdbs_number()))){
				err.rejectValue("powersave.mps_roll_over", "", "HALAMAN INVESTASI : Untuk Produk Jenis PowerSave/Stable Link/Stable Save, silakan mengisi Jenis RollOver Terlebih dahulu");
			}
			
			if(edit.getPowersave().getMps_jangka_inv()!=null){
				if(!edit.getPowersave().getMps_jangka_inv().equals("")){
					//Deddy (27 Dec 2012) - Info from Yudha by Phone: Ketentuan khusus New Simas Stabil Link (164-11), MTI harus 36 bulan dan Rollover yang bisa dipilih hanya autobreak.
					if(Integer.parseInt(edit.getPowersave().getMps_jangka_inv())!=36 && edit.getDatausulan().getLsbs_id()==164 && edit.getDatausulan().getLsdbs_number()==11 ){
						err.rejectValue("powersave.mps_jangka_inv","","HALAMAN INVESTASI : Untuk Produk NEW SIMAS STABIL LINK, Hanya bisa mengambil MTI 36 Bulan." );
					}
					if(edit.getPowersave().getMps_roll_over()!=3  && edit.getDatausulan().getLsbs_id()==164 && edit.getDatausulan().getLsdbs_number()==11){
						err.rejectValue("powersave.mps_roll_over","","HALAMAN INVESTASI : Untuk Produk NEW SIMAS STABIL LINK, Hanya bisa Autobreak." );
					}
				}
			}
			
			Integer flag_account = new Integer(produk.flag_account);
			if (autodebet.intValue() == 3)
			{
				flag_account = new Integer(2);
			}
			
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
			
			if (kode_flag.intValue() > 1 )
			{
				Integer flag_excell80plus = new Integer(produk.flag_excell80plus);
				int flag_debet = produk.flag_debet;
				String hasil_autodebet=form_b.autodebet(produk.ii_bisnis_id, produk.ii_bisnis_no, Integer.toString(autodebet.intValue()),lca_id,flag_account.intValue(),pmode.intValue(),flag_debet,flag_excell80plus.intValue());
				if (hasil_autodebet.trim().length()!=0 /*&& (produk.ii_bisnis_id!=120 &&(produk.ii_bisnis_no!=19||produk.ii_bisnis_no!=20||produk.ii_bisnis_no!=21))*/)
				{
					edit.setPesan("PERHATIAN!!! MOHON DIPERHATIKAN APAKAH BENAR CARA BAYAR TUNAI, KARENA SEHARUSNYA AUTODEBET!");
	//				err.rejectValue("datausulan.mste_flag_cc","","HALAMAN DATA USULAN :" +hasil_autodebet);
					if(produk.ii_bisnis_id==164){
						//req Pak Himmia : UNTUK STABLE LINK, harus pilih tunai
						err.rejectValue("datausulan.mste_flag_cc","","HALAMAN DATA USULAN :" +hasil_autodebet);
					}
				}
			}
			
			if(produk.ii_bisnis_id==153 && (produk.ii_bisnis_no==6 || produk.ii_bisnis_no==7)){
				if(edit.getTertanggung().getMste_dth()==1){
					
				}
			}else{
				edit.getTertanggung().setMste_dth(null);
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
//					*Cek jangka waktu
					if(edit.getPowersave().getMps_jangka_inv()=="")
					{
						err.rejectValue("powersave.mps_jangka_inv","","HALAMAN INVESTASI : Untuk Produk "+jenis_produk+", pilih JANGKA WAKTU terlebih dahulu!" );
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
			if(edit.getDatausulan().getLku_id().equals("01")){//Rupiah
				if(topup_berkala >= 100000000){
				edit.getPemegang().setMkl_red_flag(1);
				edit.getTertanggung().setMkl_red_flag(1);
				edit.getInvestasiutama().getDaftartopup().setRedFlag_topup_berkala(1);
				}else{
				edit.getInvestasiutama().getDaftartopup().setRedFlag_topup_berkala(0);}
			}
			if(edit.getDatausulan().getLku_id().equals("02")){//Dolar
				if(topup_berkala >=10000){
				edit.getPemegang().setMkl_red_flag(1);
				edit.getTertanggung().setMkl_red_flag(1);
				edit.getInvestasiutama().getDaftartopup().setRedFlag_topup_berkala(1);
				}else{
				edit.getInvestasiutama().getDaftartopup().setRedFlag_topup_berkala(0);}
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
					}
				}
				
				//apabila tidak ada topup
				if (jns_top_up_berkala.intValue() == 0 && jns_top_up_tunggal.intValue() == 0) {
					if("1,4".indexOf(edit.getDatausulan().getMste_flag_el().toString())>-1){
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
			
			if ( kode_flag.intValue() != 11 && kode_flag.intValue() != 15 && kode_flag.intValue() != 16)
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
					
				}else if(products.unitLinkNew(edit.getDatausulan().getLsbs_id().toString()) && (flag_hcp.intValue() == 1|| flag_rider_hcp.intValue() == 1)){								
					//FIX ME Sementara untuk unitLInk yg ngambil rider hcp
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
		
		//muamalat (mabrur, saqinah, ikhlas)
		boolean muamalat = products.muamalat(produk.ii_bisnis_id, produk.ii_bisnis_no);
		
			//autodebet
			if (muamalat || (autodebet.intValue() >0)||(flag_account.intValue()==1)||(flag_account.intValue()==3))
			{
				
				Date tgl_debet=edit.getPemegang().getMste_tgl_recur();
				
				String tgltgl_debet = "";
				String blntgl_debet = "";
				String thntgl_debet = "";
				if (tgl_debet !=null)
				{
					tgltgl_debet = Integer.toString(tgl_debet.getDate());
					blntgl_debet = Integer.toString(tgl_debet.getMonth() + 1) ;
					thntgl_debet = Integer.toString(tgl_debet.getYear() + 1900);
				}
					
				String rek_bank_pp=edit.getAccount_recur().getMar_acc_no();
				if (rek_bank_pp==null)
				{
					rek_bank_pp="";
					edit.getAccount_recur().setMar_acc_no(rek_bank_pp);
				}
				//ryan
				String atasnama=edit.getAccount_recur().getMar_holder();
				if (atasnama==null)
				{
					atasnama="";
					edit.getAccount_recur().setMar_holder(atasnama);
				}
				
				Integer flag_set_auto = edit.getAccount_recur().getFlag_set_auto();
				if (flag_set_auto == null)
				{
					flag_set_auto = new Integer(0);
				}
				edit.getAccount_recur().setFlag_set_auto(flag_set_auto);
				if(flag_set_auto==1){
					edit.getAccount_recur().setTgl_debet(edit.getPemegang().getMste_tgl_recur());
					//edit.getPemegang().setMste_tgl_recur(edit.getDatausulan().getMste_beg_date());
				}else{
					edit.getAccount_recur().setTgl_debet(edit.getDatausulan().getMste_beg_date());
					edit.getPemegang().setMste_tgl_recur(edit.getDatausulan().getMste_beg_date());
				}
				
				String bank_pp=edit.getAccount_recur().getLbn_id();	
				if (bank_pp==null)
				{
					bank_pp="";
					edit.getAccount_recur().setLbn_id(bank_pp);
				}
				Integer flag_nb=edit.getAccount_recur().getFlag_autodebet_nb();
				if (flag_nb==null)
				{
					flag_nb=0;
					edit.getAccount_recur().setFlag_autodebet_nb(flag_nb);
				}
				String nama_bank_autodebet="";
				Integer max_digit=0;
				Integer min_digit=0;
				if(bank_pp!=null)
				{
					Map data1= (HashMap) this.elionsManager.select_bank2(bank_pp);

					if (data1!=null)
					{		
						nama_bank_autodebet = (String)data1.get("BANK_NAMA");
						max_digit = ((BigDecimal) data1.get("MAX_DIGIT")).intValue();
						min_digit = ((BigDecimal) data1.get("MIN_DIGIT")).intValue();
					}
				}
				edit.getAccount_recur().setLbn_nama(nama_bank_autodebet);
				Date sysdate=elionsManager.selectSysdate();
				Date tgl_valid=edit.getAccount_recur().getMar_expired();
				String tglvalid = "";
				String blnvalid = "";
				String thnvalid = "";			

				if (tgl_valid!=null)
				{
					if(tgl_valid.before(sysdate)){
						err.rejectValue("account_recur.mar_expired","","HALAMAN INVESTASI :Kartu Kredit yang digunakan Sudah Expired");
					}
					tglvalid = Integer.toString(tgl_valid.getDate());
					blnvalid = Integer.toString(tgl_valid.getMonth() + 1);
					thnvalid = Integer.toString(tgl_valid.getYear() + 1900);			
				}
				
				String hasil_tgl_debet = a.debet(Integer.toString(autodebet.intValue()),thntgl_debet,blntgl_debet,tgltgl_debet,lca_id,flag_account.intValue());
				if (hasil_tgl_debet.trim().length()!=0)
				{
					err.rejectValue("pemegang.mste_tgl_recur","","HALAMAN INVESTASI :" +hasil_tgl_debet);
				}	
				
				String hasil_valid = a.valid(Integer.toString(autodebet.intValue()),thnvalid,blnvalid,tglvalid,lca_id,flag_account.intValue());
				if (hasil_valid.trim().length()!=0)
				{
					err.rejectValue("account_recur.mar_expired","","HALAMAN INVESTASI :" +hasil_valid +" Expired");
				}
				String hasil_rek_bank_pp = a.no_rek(rek_bank_pp, lca_id, flag_account.intValue(), Integer.toString(autodebet.intValue()), max_digit, min_digit);
				if (hasil_rek_bank_pp.trim().length()!=0)
				{
					err.rejectValue("account_recur.mar_acc_no","","HALAMAN INVESTASI :" +hasil_rek_bank_pp);
				}
				
				String hasil_bank = a.cek_bank(bank_pp,lca_id,flag_account.intValue(),Integer.toString(autodebet.intValue()));
				if (hasil_bank.trim().length()!=0)
				{
					err.rejectValue("account_recur.lbn_id","","HALAMAN INVESTASI :" +hasil_bank);
				}
				
				String hasil_atasnama=a.cek_atas_nama(atasnama,lca_id,flag_account.intValue(),Integer.toString(autodebet.intValue()));
				if (hasil_atasnama.trim().length()!=0)
				{
					err.rejectValue("account_recur.mar_holder","","HALAMAN INVESTASI :" +hasil_atasnama);
				}
				
				String flag_jn_tabungan=a.flag_jn_tabungan(edit.getAccount_recur().getFlag_jn_tabungan(), edit.getAccount_recur().getLbn_nama());
				if (flag_jn_tabungan.trim().length()!=0)
				{
					err.rejectValue("account_recur.flag_jn_tabungan","","HALAMAN INVESTASI :" +flag_jn_tabungan);
				}
			}
			
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
					Integer max_digit=0;
					Integer min_digit=0;
					if(bank_pp1!=null)
					{
						Map data1= (HashMap) this.elionsManager.select_bank1(bank_pp1);
	
						if (data1!=null)
						{		
							nama_bank_rekclient = (String)data1.get("BANK_NAMA");
							max_digit = ((BigDecimal) data1.get("MAX_DIGIT")).intValue();
							min_digit = ((BigDecimal) data1.get("MIN_DIGIT")).intValue();
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
						
						String hasil_cabang_bank=a.cek_cabang_bank(cabang_bank,lca_id,flag_account.intValue(),Integer.toString(autodebet.intValue()));
						if (hasil_cabang_bank.trim().length()!=0)
						{
							err.rejectValue("rekening_client.mrc_cabang","","HALAMAN INVESTASI :" +hasil_cabang_bank);
						}
						
						String hasil_rek_bank_pp1 = a.no_rek1(rek_bank_pp1, lca_id, flag_account.intValue(), Integer.toString(autodebet.intValue()), max_digit, min_digit);
						if (hasil_rek_bank_pp1.trim().length()!=0)
						{
							err.rejectValue("rekening_client.mrc_no_ac","","HALAMAN INVESTASI :" +hasil_rek_bank_pp1);
						}
						
						String hasil_bank1 = a.cek_bank1(bank_pp1,lca_id,flag_account.intValue(),Integer.toString(autodebet.intValue()));
						if (hasil_bank1.trim().length()!=0)
						{
							err.rejectValue("rekening_client.lsbp_id","","HALAMAN INVESTASI :" +hasil_bank1);
						}
					}
					
					
					if(products.muamalat(produk.ii_bisnis_id, produk.ii_bisnis_no) && jenis_tab.intValue() == 3) {
						err.rejectValue("rekening_client.mrc_jenis","","HALAMAN INVESTASI : Produk Bank Muamalat tidak boleh jenis tabungan : CREDIT CARD");
					}
					
					String hasil_atasnama1=a.cek_atas_nama1(atasnama1,lca_id,flag_account.intValue(),Integer.toString(autodebet.intValue()));
					if (hasil_atasnama1.trim().length()!=0)
					{
						err.rejectValue("rekening_client.mrc_nama","","HALAMAN INVESTASI :" +hasil_atasnama1);
					}
					
	
					//if ((flag_bao1.intValue()==1) && ((flag_account.intValue()==2) || (flag_account.intValue()==3)))
					//{
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
					//}
				}
			}
			//FLag untuk Penanda Rekening A/n PEMEGANG POLIS ATAU BUKAN, untuk Sekarang hanya diberlakukan untuk simasprima - RYAN
			if((edit.getDatausulan().getLsbs_id()==142 && edit.getDatausulan().getLsdbs_number()==2)&&edit.getDatausulan().tipeproduk==14){
				if(edit.getRekening_client().getMrc_pp()==null)err.rejectValue("rekening_client.mrc_pp","","HALAMAN INVESTASI : Harap Memilih " +
						"Nomor Rekening Yang Digunakan A/n Pemegang Polis Atau Bukan");
				else {
					if(edit.getRekening_client().getMrc_pp()==0){
						err.rejectValue("rekening_client.mrc_pp","","HALAMAN INVESTASI : Khusus produk Simas Prima rekening yang digunakan harus atas nama Pemegang Polis");
					}
				}
				
			}
			//untuk produk bank muamalat, banknya harus muamalat (touche..)
			if(muamalat) {
				if(edit.getRekening_client().getLsbp_id().equals("151") && edit.getAccount_recur().getLbn_nama().toUpperCase().contains("MUAMALAT")) {
					//no problemo
				}else {
					err.rejectValue("rekening_client.lsbp_id","","HALAMAN INVESTASI : Untuk Produk Bank Muamalat, Rekening Bank maupun Rekening Autodebet harus BANK MUAMALAT");
				}
				
				if(edit.getDatausulan().getMspo_nasabah_acc() != null) {
					if(edit.getDatausulan().getMspo_nasabah_acc().length() < 16) {
						err.rejectValue("datausulan.mspo_nasabah_acc","","HALAMAN INVESTASI : Untuk Produk Bank Muamalat, Nomor Kartu Fitrah Harus Diisi 16 Digit");
					}
				}
			}
			
			// khusus powersave, validasi jenis bunga, jangka waktu, rollover, bunga
			if (kode_flag.intValue() == 1 || kode_flag.intValue() == 11 || kode_flag.intValue() == 15 || kode_flag.intValue() == 16)
			{
				
				//STABLE LINK - Yusuf (28/04/2008)
				if (kode_flag.intValue() == 11 || kode_flag.intValue() == 15 || kode_flag.intValue() == 16){
					Powersave p = edit.getPowersave();
					
					SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
					String lji_id = kurs.equalsIgnoreCase("01") ? "22" : "23";
					
					if(kode_flag.intValue() == 15){
						lji_id = kurs.equalsIgnoreCase("01") ? "30" : "31";
					}else if(kode_flag.intValue() == 16){
						lji_id = "34";
					}
					Map data_nab = new HashMap();
					
					if(kode_flag.intValue() != 16){
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
					}
					
					if(edit.getPowersave().getMsl_bp_rate()==null) {
						err.rejectValue("powersave.msl_bp_rate","","HALAMAN INVESTASI : Harap Isi Persentase Rate (Faktor Biaya Investasi) dengan angka diantara 70 - 100 %");
					}else {
						double rate = edit.getPowersave().getMsl_bp_rate();
						if(produk.ii_bisnis_id==164 && produk.ii_bisnis_no==11){
							if(rate!=25){
								err.rejectValue("powersave.msl_bp_rate","","HALAMAN INVESTASI : Persentase Rate (Faktor Biaya Investasi) harus 25%");
							}
						}else{
							if(rate < 70 || rate > 100) {
								err.rejectValue("powersave.msl_bp_rate","","HALAMAN INVESTASI : Harap Isi Persentase Rate (Faktor Biaya Investasi) dengan angka diantara 70 - 100 %");
							}
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

				//Anta - Untuk PowerSave Syariah BSIM
				if(produk.ii_bisnis_id==175 && produk.ii_bisnis_no==2){
					if(jns_top_up0.equals("24")){
						err.rejectValue("powersave.mps_jangka_inv","","HALAMAN INVESTASI : Untuk PowerSave Syariah BSIM, jangka waktu investasi 24 bulan tidak berlaku.");
					}
				}
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
					Date satufeb15 = defaultDateFormat.parse("01/02/2015");
					Date sysdate=elionsManager.selectSysdate();
					
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
					
					if(lsbs_id==184 && edit.getDatausulan().getLsdbs_number()==6 && mgi!=36){
						err.rejectValue("powersave.mps_jangka_inv","", "HALAMAN INVESTASI :Untuk Produk Stable Save Hadiah, Hanya bisa mengambil MTI 36 bulan");
					}
					
//					if(products.stableLink(Integer.toString(lsbs_id)) && edit.getPowersave().getFlag_bulanan()>0 && mgi<=12){
//						err.rejectValue("powersave.mps_jangka_inv","", "HALAMAN INVESTASI :Untuk Produk Stable Link Manfaat Triwulanan/Semesteran/Tahunan, Hanya bisa mengambil MTI 24 dan 36 bulan");
//					}
					
					//Anta - Untuk Smile Progressive Save Syariah
					if(lsbs_id==207){
						if(pmode==3 && mgi!=12){
							err.rejectValue("powersave.mps_jangka_inv","", "HALAMAN INVESTASI : Untuk cara bayar TAHUNAN hanya bisa mengambil MTI 12 bulan");
						}else if(pmode==2 && mgi!=6){
							err.rejectValue("powersave.mps_jangka_inv","", "HALAMAN INVESTASI : Untuk cara bayar SEMESTERAN hanya bisa mengambil MTI 6 bulan");
						}else if(pmode==1 && mgi!=3){
							err.rejectValue("powersave.mps_jangka_inv","", "HALAMAN INVESTASI : Untuk cara bayar TRIWULANAN hanya bisa mengambil MTI 3 bulan");
						}else if(pmode==6 && mgi!=1){
							err.rejectValue("powersave.mps_jangka_inv","", "HALAMAN INVESTASI : Untuk cara bayar BULANAN hanya bisa mengambil MTI 1 bulan");
						}
					}
					
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
					
					if ((edit.getDatausulan().getLsbs_id()==142 && edit.getDatausulan().getLsdbs_number()==2) ||
					    (edit.getDatausulan().getLsbs_id()==158 && edit.getDatausulan().getLsdbs_number()==6) ||
						(edit.getDatausulan().getLsbs_id()==175 && edit.getDatausulan().getLsdbs_number()==2)){
						if(sysdate.after(satufeb15) && mgi ==1){
							err.rejectValue("powersave.mps_jangka_inv","", "HALAMAN INVESTASI : Pertanggal "+satufeb15+" , SIMAS PRIMA/ POWER SAVE SYARIAH /POWER SAVE MANFAAT BULANAN tidak bisa mengambil Jangka Waktu 1 Bulan");
						}
					}
				}
				
				if (hasil_jns_top_up0.equalsIgnoreCase(""))
				{
					Map data = this.elionsManager.hitbac(cmd,err,kurs,up,flag_account ,Common.isEmpty(edit.getAgen().getLca_id())?lca_id:edit.getAgen().getLca_id() ,autodebet,flag_bao1, null,null,null,premi,null, null,null,null,null,null, null, null ,null,flag_powersave ,flag_bulanan,"bungapowersave");
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
								Double premi_tahunan_utama = 0.;
								Double premi_tahunan_tambahan = 0.;
								
								if(datarider.getLsbs_id().intValue()==820 || datarider.getLsbs_id().intValue()==823  || datarider.getLsbs_id().intValue()==825 || datarider.getLsbs_id().intValue()==826 ){
									Integer size = edit.getDatausulan().getDaftaRider().size();
									if(size==null){
										size=0;
									}
									for(int b=0;b < size.intValue();b++){//FIXEKASEHAT
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
												premi_tahunan_utama = premi_tahunan;
											//premi_tahunan = premi= uwManager.selectPremiSuperSehat(kurs, edit.getTertanggung().getMste_age(), tampung.getLsbs_id(), tampung.getLsdbs_number());
										}else if((tampung.getLsbs_id()==820 || tampung.getLsbs_id()==823 || tampung.getLsbs_id()==825) && tampung.getLsdbs_number().intValue()>=16 ){
											diskon = 0.975;//diskon sebesar 2.5% //FIXIN
											premi = elionsManager.selectRateRider(kurs, edit.getTertanggung().getMste_age(), 0, tampung.getLsbs_id(), tampung.getLsdbs_number());
											//premi= uwManager.selectPremiSuperSehat(kurs, edit.getTertanggung().getMste_age(), tampung.getLsbs_id(), tampung.getLsdbs_number());
											premi_tahunan = premi = premi * diskon;
												premi_tahunan_tambahan = premi_tahunan;
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
														premi_tahunan_utama = premi_tahunan;
													//premi_tahunan = premi= uwManager.selectPremiSuperSehat(kurs, edit.getTertanggung().getMste_age(), tampung.getLsbs_id(), tampung.getLsdbs_number());
												}else if((tampung.getLsbs_id()==820 || tampung.getLsbs_id()==823 || tampung.getLsbs_id()==825) && tampung.getLsdbs_number().intValue()>=16 ){
													diskon = 0.975;//diskon sebesar 2.5%
													Double premi_before_discount = elionsManager.selectRateRider(kurs, edit.getTertanggung().getMste_age()+(i+1), 0, tampung.getLsbs_id(), tampung.getLsdbs_number());
													premi_before_discount = premi_before_discount* diskon;
													//premi= uwManager.selectPremiSuperSehat(kurs, edit.getTertanggung().getMste_age(), tampung.getLsbs_id(), tampung.getLsdbs_number());
													premi_tahunan = premi += premi_before_discount;
														premi_tahunan_tambahan = premi_tahunan;
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
//										for(int i=0;i<loopingpremi-1;i++){
//											rate_rider = elionsManager.selectRateRider(kurs, edit.getTertanggung().getMste_age()+(i+1), 0, datarider.getLsbs_id(), datarider.getLsdbs_number());
//											premi_tahunan += premi = ( (rate_rider/1000) * datarider.getMspr_tsi() );
//											logger.info((rate_rider/1000) * datarider.getMspr_tsi());
//											logger.info(premi_tahunan);
//										}
									}
								}
								
								datarider.setMrs_premi_tahunan(premi_tahunan);
								if(datarider.getLsbs_id().intValue()==820 || datarider.getLsbs_id().intValue()==823  || datarider.getLsbs_id().intValue()==825){
									if(datarider.getLsdbs_number().intValue()>=16){
										datarider.setMspr_premium(premi_tahunan_tambahan * factor);
									}else{
										datarider.setMspr_premium(premi_tahunan_utama * factor);
									}
								}
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
			
//			if ((kode_flag.intValue() == 1) || (kode_flag.intValue() == 11))
//			{
//				if ( edit.getPowersave().getMps_jenis_plan().intValue() >0)
//				{
//					if (edit.getDatausulan().getKurs_p().equalsIgnoreCase("01"))
//					{
//						if (edit.getPowersave().getMps_rate().doubleValue() >11.5)
//						{
//							err.rejectValue("powersave.mps_rate","","HALAMAN INVESTASI : Bunga Special max 11.5" );
//						}
//					}else{
//						if (edit.getPowersave().getMps_rate().doubleValue() >6.5)
//						{
//							err.rejectValue("powersave.mps_rate","","HALAMAN INVESTASI : Bunga Special max 6.5" );
//						}
//
//					}
//				}
//			}
			
			//list dth
			List dth = edit.getTertanggung().getDaftarDth();
			Integer jumlah_dth = dth.size();
			if(jumlah_dth==null)jumlah_dth= new Integer(0);
			Integer jmlh_dth = jumlah_dth;
			edit.getTertanggung().setJml_dth(jmlh_dth.intValue());
			
			//MANTA
			if(produk.ii_bisnis_id==153 && (produk.ii_bisnis_no==6 || produk.ii_bisnis_no==7)){
				if(edit.getTertanggung().getMste_dth()==1 && (edit.getTertanggung().getJml_dth()==0 || edit.getTertanggung().getDaftarDth().isEmpty())){
					err.rejectValue("tertanggung.mste_dth","", "HALAMAN INVESTASI : Data Penggunaan Dana Talangan Haji Belum Diisi");
				}
			}
			
			//penerima manfaat
			List benef = edit.getDatausulan().getDaftabenef();
			Integer jumlah_r =benef.size();
			if (jumlah_r==null)jumlah_r=new Integer(0);
			
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
			if(edit.getCurrentUser().getLca_id().equals("58") || edit.getCurrentUser().getLus_id().equals("2661")){
				if (jmlh_penerima.intValue()>4){
					err.reject("","HALAMAN INVESTASI : Jumlah Penerima Manfaat asuransi harus Maksimal 4 Orang");
				}
			}
			
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
			if("54,131,132,148,183,189,193,195,197,201,204".indexOf(edit.getDatausulan().getLsbs_id().toString())<=-1 && edit.getPemegang().getFlag_upload()!=3){
				if(tot_persen.compareTo(BigDecimal.valueOf(100)) !=0  ){
					err.reject("","HALAMAN INVESTASI : Jumlah Persentase bagian Penerima Manfaat asuransi harus 100%");
				}
			}
			
			if("54,131,132,148,183,189,193,195,197,201,204".indexOf(edit.getDatausulan().getLsbs_id().toString())>=0){
				if(jmlh_penerima>0){
					err.reject("","HALAMAN INVESTASI : Untuk Produk ini tidak ada penerima manfaat");
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
	//logger.info("ok");

		if((edit.getDatausulan().getLsbs_id().intValue() != 158 && edit.getPowersave().getMpr_breakable() != null) || (edit.getDatausulan().getLsbs_id().intValue() != 176 && edit.getPowersave().getMpr_breakable() != null) ) {
			if(edit.getPowersave().getMpr_breakable().intValue() == 1) {
				err.reject("","HALAMAN INVESTASI : Pilihan 'Breakable' hanya bisa dipilih untuk produk Powersave Bulanan");
			}
		}
		
		/**
		 * TAMBAHAN DARI BERTHO 13/11/2008
		 * Buat validasi rekening bank jika usernya bank Sinarmas maka digitnya tidak boleh lebih dari 10 digit
		 */
		
		if(edit.getCurrentUser().getJn_bank().intValue() == 2){ //BANK SINARMAS
			if(edit.getRekening_client().getMrc_no_ac().trim().length()!=10){
				err.rejectValue("rekening_client.mrc_no_ac","","HALAMAN INVESTASI : Limit digit rekening Bank Sinarmas tidak boleh lebih/kurang dari 10 digit");
			}
			if(edit.getAccount_recur().getMar_acc_no()!=null){
				if(edit.getAccount_recur().getMar_acc_no().trim().length()!=10){
					err.rejectValue("account_recur.mar_acc_no","","HALAMAN INVESTASI : Limit digit rekening (Auto Debet) Bank Sinarmas tidak boleh lebih/kurang dari 10 digit");
				}
			}
		}
		
		if(edit.getAccount_recur().getMar_acc_no()!=null){
			List listRekAjsMsig = uwManager.selectRekAJSMSIG();
			for(int i=0; i<listRekAjsMsig.size();i++){
				Map MapRekAjsMsig = (Map)listRekAjsMsig.get(i);
				String rekAjsMsig = (String)MapRekAjsMsig.get("REK_AJS");
				if(rekAjsMsig!=null){
					if(rekAjsMsig.compareTo(edit.getAccount_recur().getMar_acc_no())==0){
						err.rejectValue("account_recur.mar_acc_no","","HALAMAN INVESTASI : No Rekening Autodebet yg dimasukkan merupakan No Rekening AJS MSIG, Silakan dicrosscek Kembali.");
					}
				}
				
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
		
		//(LUFI) jika sudah ada billing cek apakah total premi berubah , jika berubah tidak bisa next
		Integer countBilling = bacManager.selectCountMstBillingNB(edit.getPemegang().getReg_spaj());
		if(countBilling>0){
			Double totalPremiNB=0.0;
			Double totalPremiBilling=0.0;
			if(products.unitLink(edit.getDatausulan().getLsbs_id().toString())){
				totalPremiNB = edit.getInvestasiutama().getDaftartopup().getPremi_berkala()+edit.getInvestasiutama().getDaftartopup().getPremi_tunggal()+edit.getDatausulan().getMspr_premium();
				totalPremiBilling = bacManager.selectTotalPremiNewBusiness(edit.getPemegang().getReg_spaj());
				if(!totalPremiNB.equals(totalPremiBilling)){
					err.reject("","HALAMAN INVESTASI : Untuk perubahan premi tidak bisa dilakukan karena SPAJ ini sudah diproses billing ");
				}
			}else if(products.powerSave(edit.getDatausulan().getLsbs_id().toString()) || products.stableLink(edit.getDatausulan().getLsbs_id().toString()) || products.stableSavePremiBulanan(edit.getDatausulan().getLsbs_id().toString()) 
					|| products.stableSave(edit.getDatausulan().getLsbs_id(), edit.getDatausulan().getLsdbs_number())){
				totalPremiNB = edit.getDatausulan().getMspr_premium();
				totalPremiBilling = bacManager.selectTotalPremiNewBusiness(edit.getPemegang().getReg_spaj());
				if(!totalPremiNB.equals(totalPremiBilling)){
					err.reject("","HALAMAN INVESTASI : Untuk perubahan premi tidak bisa dilakukan karena SPAJ ini sudah diproses billing");
				}
			}else if(("183,189,193,201".indexOf(edit.getDatausulan().getLsbs_id().toString())>-1 )){
				Double tot_rider = 0.0;
				if(edit.getDatausulan().getDaftaRider().size()!=0){
					List<Datarider> xx = edit.getDatausulan().getDaftaRider();
					for(Datarider datarider : xx){
						tot_rider =tot_rider + datarider.getMspr_premium();
					}
				}
				totalPremiNB = edit.getDatausulan().getMspr_premium()+tot_rider;
				totalPremiBilling = bacManager.selectTotalPremiNewBusiness(edit.getPemegang().getReg_spaj());
				if(!totalPremiNB.equals(totalPremiBilling)){
					err.reject("","HALAMAN INVESTASI : Untuk perubahan premi tidak bisa dilakukan karena SPAJ ini sudah diproses billing");
				}
			}else{
				totalPremiNB = edit.getDatausulan().getMspr_premium();
				totalPremiBilling = bacManager.selectTotalPremiNewBusiness(edit.getPemegang().getReg_spaj());
				if(!totalPremiNB.equals(totalPremiBilling)){
					err.reject("","HALAMAN INVESTASI : Untuk perubahan premi tidak bisa dilakukan karena SPAJ ini sudah diproses billing");
				}
			}
			
		}
		
		//  di comment sampe ada perintah lbh lanjut dari bsm 
		// cek no rekening bsm (Yusup_A)
		/*logger.info("*-*-*-* start iso8583 *-*-*-*");
		if(edit.getRekening_client().getLsbp_id().equals("156") ) { 
			Boolean exist = false;
			Boolean holder = false;
			
			Iso8583Client ic = new Iso8583Client();
			if(ic.mssg.equals("true")) {
				while(ic.data.length == 0) {;}
				
				ic.data = new String[] {};
				logger.info("\n-=* send Transfer Inquiry Mssg *=-");
				ic.sendMssgToServer(ic.isoMssgTransferInquiry(ic.checkNoRek(edit.getRekening_client().getMrc_no_ac())));
				while(ic.data.length == 0) {;}
				String mrc_nama = "";
				if(ic.data[48] != null && !ic.data[48].trim().equals("")) {
					exist = true;
					String nama = ic.data[48].substring(0, 30);
					if(edit.getRekening_client().getMrc_nama().length() < 30) mrc_nama = edit.getRekening_client().getMrc_nama();
					else mrc_nama = edit.getRekening_client().getMrc_nama().substring(0, 30);
					
					logger.info(nama);
					if(nama.trim().equals(mrc_nama.toUpperCase()))
						holder = true;
				}
				
				ic.data = new String[] {};
				logger.info("\n-=* send SignOff Mssg *=-");
				ic.sendMssgToServer(ic.isoMssgSignOff());
				//while(ic.data.length == 0) {;}
				ic.CloseConnection();
				
				if(!exist) {
					err.rejectValue("rekening_client.mrc_no_ac","","HALAMAN INVESTASI : no rekening tidak ada");
				}
				else if(!holder) {
					err.rejectValue("rekening_client.mrc_nama","","HALAMAN INVESTASI : nama pemengang rekening tidak cocok dengan no rekening yang dipakai");
				}
				else if(!mrc_nama.equals(edit.getPemegang().getMcl_first()) && !edit.getPemegang().getConfNm().equals("ok")) {
					err.rejectValue("rekening_client.mrc_nama","","");
					edit.getPemegang().setConfNm("tidak cocok");
				}
			}
			else {
				err.rejectValue("rekening_client.mrc_no_ac","","HALAMAN INVESTASI : tidak dapat terhubung dengan server bank sinarmas");
			}
		}*/
		
	}
	
	public void validateagen(Object cmd, Errors err) 
	{
		logger.debug("EditBacValidator : validate page validateagen");
		Cmdeditbac edit= (Cmdeditbac) cmd;
		form_agen d_agen= new form_agen(); 

		Integer hasil=new Integer(0);
		Date tgldibuat=null;
		String kode_regional="";
		String nama_regional="";
		String kode_penutup="";
		String nama_penutup="";
		String kode_broker="";
		String nama_broker="";
		String bank_broker="";
		String no_rek_broker="";
		Integer ao=new Integer(0);
		String kode_ao="";
		String mspo_leader="";
		String nama_ao="";
		Integer pribadi=new Integer(0);
		Integer followup=new Integer(0);
		String kode_regional_penagihan="";
		String nama_regional_penagihan="";
		String hasil_agen="";
		String hasil_agen_leader = "";
		Integer kode_produk=null;
		Integer nomor_produk = null;
		Integer ulink=new Integer(0);
		Integer sertifikasi=new Integer(0);
		String cabang="";
		Date tanggal_sertifikat=null;
		int tgl_sertifikat;
		int bln_sertifikat;
		int thn_sertifikat;
		String hasil_tanggaldibuat="";
		String hasil_ao="";
		String hasil_nama_penutup="";
		String cbg="";
		Integer komisi_bii=new Integer(0);
		Integer flag_account=new Integer(0);
		Integer flag_as=new Integer(0);
		Integer flag_jenis_plan = new Integer(0);
		String hasil_nik="";
		String nama_karyawan="";
		String cabang_karyawan="";
		String dept_karyawan="";
		String status_karyawan = "";
		Integer flag_worksite = new Integer(0);
		Integer flag_endowment = new Integer(0);
		Integer autodebet = edit.getDatausulan().getMste_flag_cc();	
		Integer jumlah_cancel = edit.getPemegang().getJumlah_cancel();
		Integer flag_gutri = edit.getDatausulan().getFlag_gutri();
		Integer mste_flag_guthrie = edit.getPemegang().getMste_flag_guthrie();
		Integer flag_karyawan = edit.getDatausulan().getMste_flag_el();
		if (flag_karyawan==null)
		{
			flag_karyawan= new Integer(0);
		}
		
		//khusus karyawan ekalife
		if (flag_karyawan.intValue() == 1 || flag_karyawan.intValue() == 4)
		{
			String kode_perusahaan = edit.getPemegang().getMspo_customer();
			if (kode_perusahaan==null)
			{
				kode_perusahaan="";
				edit.getPemegang().setMspo_customer(kode_perusahaan);
			}
			
			//			NIK_Karyawan xxx
			hasil_nik = "bukan karyawan tetap";
			String nik=edit.getEmployee().getNik();
			if (nik==null)
			{
				nik="";
				edit.getEmployee().setNik(nik);
			}
			if (nik.trim().length()==0)
			{
				hasil_nik="Khusus produk karyawan harus mengisi NIK Karyawan terlebih dahulu pada halaman pemegang polis";
				err.rejectValue("employee.nik","","HALAMAN DETIL AGEN :" +hasil_nik);
			}else{
				hasil_nik = d_agen.nik_karyawan(nik);
				if (hasil_nik.trim().length()!=0)
				{
					err.rejectValue("employee.nik","","HALAMAN DETIL AGEN :" +hasil_nik);
				}else{

					Map data1= (HashMap) this.elionsManager.select_nik_karyawan(nik);
//					Map data2 = (HashMap) this.uwManager.select_nik_karyawan_ttp(nik);
					
					if (data1!=null)
					{		
						nama_karyawan = (String)data1.get("MCL_FIRST");
						cabang_karyawan = (String)data1.get("LCA_NAMA");
						dept_karyawan = (String)data1.get("LDE_DEPT");
						status_karyawan = (String)data1.get("STS_KRY");
					}else if(data1==null){
							hasil_nik="NIK yang dimasukan harus NIK dari Karyawan tetap/percobaan/kontrak.";
							err.rejectValue("employee.nik","","HALAMAN DETIL AGEN :" +hasil_nik);
						
					}else{
						hasil_nik="NIK tidak terdaftar.";
						err.rejectValue("employee.nik","","HALAMAN DETIL AGEN :" +hasil_nik);
					}
				}
			}
			edit.getEmployee().setNama(nama_karyawan);
			edit.getEmployee().setCabang(cabang_karyawan);
			edit.getEmployee().setDept(dept_karyawan);
			
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
		}else{
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
		
		if (autodebet==null)
		{
			autodebet = new Integer(0);
			edit.getDatausulan().setMste_flag_cc(autodebet);
		}
		
		if(autodebet==0){
			edit.setAccount_recur(new Account_recur());
		}
		kode_produk = edit.getDatausulan().getLsbs_id();
		nomor_produk = edit.getDatausulan().getLsdbs_number();
		tgldibuat = edit.getPemegang().getMspo_spaj_date();
		if (tgldibuat == null)
		{
			err.rejectValue("pemegang.mspe_date_birth","","HALAMAN DETIL AGEN : Tanggal Spaj masih kosong, Silahkan isi tanggal spaj terlebih dahulu");
		}
		
		kode_penutup = edit.getAgen().getMsag_id();
		if (kode_penutup ==null)
		{
			kode_penutup="";
			edit.getAgen().setMsag_id(kode_penutup);
		}
		
		//MANTA - Broker
		if(!edit.getCurrentUser().getLca_id().equals("58")){
			kode_broker = edit.getBroker().getLsb_id();
			nama_broker = edit.getBroker().getLsb_nama();
			if (kode_broker == null)
			{
				kode_broker = "";
				nama_broker = "";
				edit.getBroker().setLsb_id(kode_broker);
				edit.getBroker().setLsb_nama(nama_broker);
			}
			bank_broker = edit.getBroker().getLbn_id();
			if (bank_broker == null)
			{
				bank_broker = "";
				edit.getBroker().setLbn_id(bank_broker);
			}else{
				String nama_bank_broker = "";
				Map data1= (HashMap) this.elionsManager.select_bank2(bank_broker);
				if (data1!=null){		
					nama_bank_broker = (String) data1.get("BANK_NAMA");
					//cek nama bank rek client kalau sudah pernah diisi
					edit.getBroker().setLbn_nama(nama_bank_broker);
				}
			}
			no_rek_broker = edit.getBroker().getNo_account();
			if (no_rek_broker == null)
			{
				no_rek_broker = "";
				edit.getBroker().setNo_account(no_rek_broker);
			}
		}
 
		if(products.stableLink(edit.getDatausulan().getLsbs_id().toString()) && edit.getDatausulan().getLsbs_id()!=186){
				List daftar = elionsManager.selectPunyaStableLinkBAC(
						edit.getDatausulan().getLku_id(), 
						edit.getPemegang().getMspe_date_birth(), edit.getPemegang().getMcl_first(),
						edit.getTertanggung().getMspe_date_birth(), edit.getTertanggung().getMcl_first(),
						kode_penutup,
						edit.getDatausulan().getLsbs_id(),
						edit.getDatausulan().getLsdbs_number());
				if(!daftar.isEmpty()) {
					for(int i=0;i<daftar.size();i++){
						Map m = (Map) daftar.get(i);
						String polis = (String) m.get("MSPO_POLICY_NO_FORMAT");
						String spaj = (String) m.get("REG_SPAJ");
						
//						err.reject("", "Maaf, Polis baru tidak dapat diterbitkan. Silahkan lakukan Top-Up terhadap Polis dengan nomor SPAJ/POLIS " + polis + "/" + spaj + ". ");
						Integer surrender = uwManager.selectSudahSurrender(spaj);
						Integer countPremi = uwManager.selectCountPremiSlink(spaj);
						Integer cair = uwManager.selectCountPowersaveCair(spaj);
						if(surrender==0 && cair<countPremi){
							if(edit.getPemegang().getStatus().equalsIgnoreCase("edit")){
								if(!edit.getDatausulan().getReg_spaj().equals(spaj)){
									err.reject("", "Maaf, Pemegang Polis ini sudah mempunyai polis Stable Link baru dengan nomor POLIS/SPAJ " + polis + "/" + spaj + ". ");
								}
							}else{
								err.reject("", "Maaf, Pemegang Polis ini sudah mempunyai polis Stable Link baru dengan nomor POLIS/SPAJ " + polis + "/" + spaj + ". ");
							}
						}
					}
				}
		}
		
		ao = edit.getPemegang().getMspo_ref_bii();
		if (ao==null)
		{
			ao= new Integer(0);
			edit.getPemegang().setMspo_ref_bii(ao);
		}
		kode_ao = edit.getPemegang().getMspo_ao();
		if (kode_ao==null)
		{
			kode_ao="";
			edit.getPemegang().setMspo_ao(kode_ao);
		}
		nama_ao = edit.getPemegang().getNama_ao();
		if (nama_ao ==null)
		{
			nama_ao ="";
			edit.getPemegang().setNama_ao(nama_ao);
		}
		pribadi = edit.getPemegang().getMspo_pribadi();
		if (pribadi==null)
		{
			pribadi=new Integer(0);
			edit.getPemegang().setMspo_pribadi(pribadi);
		}
		followup = edit.getPemegang().getMspo_follow_up();
		if (followup==null)
		{
			followup=new Integer(0);
			edit.getPemegang().setMspo_follow_up(followup);
		}
		kode_regional_penagihan = edit.getAddressbilling().getRegion();
		if (kode_regional_penagihan==null)
		{
			kode_regional_penagihan="";
			edit.getAddressbilling().setRegion(kode_regional_penagihan);
		}
		cbg = edit.getPemegang().getLca_id();
		if(ao == null)
		{
			ao =new Integer(0);
		}
		
		mspo_leader=edit.getPemegang().getMspo_leader();
		if(mspo_leader==null){
			mspo_leader="";
			edit.getPemegang().setMspo_leader(mspo_leader);
		}
		
		hasil_agen=d_agen.kode_agen(kode_penutup);
		
//		hasil_agen_leader=d_agen.kode_agen_leader(mspo_leader);
		if(edit.getDatausulan().getLsbs_id().intValue()==155 || edit.getDatausulan().getLsbs_id().intValue()==134 || (edit.getDatausulan().getLsbs_id().intValue()==142 && edit.getDatausulan().getLsdbs_number().intValue()==2) || (edit.getDatausulan().getLsbs_id().intValue()==175 && edit.getDatausulan().getLsdbs_number().intValue()==2) || (edit.getDatausulan().getLsbs_id().intValue()==164 && edit.getDatausulan().getLsdbs_number().intValue()==2)){
			hasil_agen_leader="";
			hasil_agen="";
		}
		if(!edit.getCurrentUser().getCab_bank().equals("") && (edit.getCurrentUser().getLca_id().equals("58"))){
			hasil_agen_leader="";
		}
		
		if(edit.getDatausulan().getLsbs_id().intValue()==96 && (edit.getDatausulan().getLsdbs_number().intValue()>12 && edit.getDatausulan().getLsdbs_number().intValue()<16)){
			if(!(edit.getAgen().getLwk_id().equals("A8") || edit.getAgen().getLwk_id().equals("A9") || edit.getAgen().getLwk_id().equals("B1"))){
				err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN : Khusus Produk Holyland Invest Bethany, agent penutupnya harus agent Bethanny" );
			}
		}
		if(kode_penutup!=""){
			if(uwManager.selectAgentActiveOrNot(kode_penutup)==0){
				err.rejectValue("agen.msag_id","", "HALAMAN DETIL AGEN : Agen tersebut Terdaftar Tidak Aktif, silahkan hubungi Agency Support.");
			}else{
				Map mapAgent = elionsManager.selectagenpenutup(kode_penutup);
				kode_regional=(String)mapAgent.get("REGIONID");
				edit.getAgen().setKode_regional(kode_regional);
				edit.getAgen().setLca_id(FormatString.rpad("0",(edit.getAgen().getKode_regional().substring(0,2)),2));
				edit.getAgen().setLwk_id(FormatString.rpad("0",(edit.getAgen().getKode_regional().substring(2,4)),2));
				if( (edit.getAgen().getLca_id().equals("37") && (edit.getAgen().getLwk_id().equals("B2") || edit.getAgen().getLwk_id().equals("B3") )) && !(edit.getDatausulan().getLsbs_id().intValue()==187 && edit.getDatausulan().getLsdbs_number().intValue()==5) ){
					err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN : Kode Agent Penutup ini hanya bisa untuk tutupan PAS BP!" );
				}
				
				if((Integer)mapAgent.get("FLAG_CABANG_SYARIAH") == 1 && bacManager.selectLineBusLstBisnis(edit.getDatausulan().getLsbs_id().toString())!=3 ){
					err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN : Untuk Cabang/Wakil dari agen Penutup ini produk yang diambil harus produk syariah" );
				}
			}
		//kode region penutup 37B2 dan 37B3 hanya bisa untuk tutupan PAS BP
		
		}
		
		String lcaid = edit.getAgen().getLca_id();
		Integer flagcc = edit.getDatausulan().getMste_flag_cc();

		/**
		 * @author Andhika
		 * kal0 lca_id agen 52 and bayar payor harus isi nama perusahaan dan nik karyawan
		 */
		if(lcaid==null){
			lcaid="0";
		}
		if(lcaid==null) err.rejectValue("agen.lca_id","","Harap isi kode agent terlebih dahulu");
		if(lcaid.equalsIgnoreCase("52") && flagcc == 3){
			String nik_ws = edit.getPemegang().getNik();
			String mspo_customer = edit.getPemegang().getMspo_customer();
			
			if(mspo_customer.equalsIgnoreCase("")){
				hasil_nik = "Harus mengisi Nama Perusahanaan terlebih dahulu.";
				err.rejectValue("employee.nik","","HALAMAN DETIL AGEN :" +hasil_nik);
			}
			if(nik_ws.equalsIgnoreCase("")){
				hasil_nik = "Harus mengisi NIK Karyawan terlebih dahulu.";
				err.rejectValue("employee.nik","","HALAMAN DETIL AGEN :" +hasil_nik);
			}
		}
		
		if(edit.getDatausulan().getLsbs_id().intValue()==196 && edit.getPemegang().getJn_bank().intValue()!=3 && !edit.getAgen().getLca_id().equals("62")){
			if ( !(edit.getAgen().getLca_id().equals("37") && edit.getAgen().getLwk_id().equals("B9"))){
				err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN : Khusus produk Term Insurance ini hanya untuk SURYAMAS AGUNG AGENCY");
			}
		}
		if(edit.getDatausulan().getLsbs_id().intValue()==73 && edit.getDatausulan().getLsdbs_number().intValue()==8){
			if ( !(edit.getAgen().getLca_id().equals("37") && edit.getAgen().getLwk_id().equals("B9"))){
				err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN : Khusus produk PA Resiko A ini hanya untuk SURYAMAS AGUNG AGENCY");
			}
		}
		if(edit.getDatausulan().getLsbs_id().intValue()==191){
			if ( !(edit.getAgen().getLca_id().equals("58") || edit.getAgen().getLca_id().equals("40"))){
				err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN : Khusus produk Rencana Cerdas ini hanya untuk MALLASSURANCE dan DM/TM");
			}
		}
		//ryan
		if(edit.getDatausulan().getLsbs_id().intValue()==142 && edit.getDatausulan().getLsdbs_number().intValue()==10||edit.getDatausulan().getLsbs_id().intValue()==164 && edit.getDatausulan().getLsdbs_number().intValue()==10){
			if ( !(edit.getAgen().getLca_id().equals("09")||edit.getAgen().getLca_id().equals("58")||edit.getAgen().getLca_id().equals("42")||edit.getAgen().getLca_id().equals("40"))){
				err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN : Khusus produk Ini ini hanya untuk BANCASS, MALL, DMTM, DAN WORKSITE ");
			}
		}
		
		//MANTA - Khusus Jalur Distribusi MNC
			if(!edit.getCurrentUser().getLca_id().equals("58")){
				//edit.getBroker().setFlagbroker(false);
				if(edit.getBroker().isFlagbroker() == true){
					if(edit.getAgen().getLca_id().equals("62") && ((edit.getDatausulan().getLsbs_id().intValue()==140 && edit.getDatausulan().getLsdbs_number().intValue()==2) || (edit.getDatausulan().getLsbs_id().intValue()==196 && edit.getDatausulan().getLsdbs_number().intValue()==1) ||
					  (edit.getDatausulan().getLsbs_id().intValue()==197 && edit.getDatausulan().getLsdbs_number().intValue()==1) || edit.getDatausulan().getLsbs_id().intValue()==205)){
						if(edit.getBroker().getLsb_nama().equalsIgnoreCase("")){
							err.rejectValue("broker.lsb_id","","HALAMAN DETIL AGEN : Harap isi Kode Broker dengan benar");
						}
						if(edit.getBroker().getLbn_id().equalsIgnoreCase("")){
							err.rejectValue("broker.lbn_id","","HALAMAN DETIL AGEN : Harap isi Bank Broker");
						}
						if(edit.getBroker().getNo_account().equalsIgnoreCase("")){
							err.rejectValue("broker.no_account","","HALAMAN DETIL AGEN : Harap isi No Rekening Broker");
						}
						//edit.getBroker().setFlagbroker(true);
					}
				}
			}

		//lufi Validasi Reginal Agen Arco,SMP&BSM DMTM
		if(edit.getDatausulan().getLsbs_id().intValue()==183 && (edit.getDatausulan().getLsdbs_number().intValue()>=31 && edit.getDatausulan().getLsdbs_number()<=45)){
			if(!edit.getAgen().getKode_regional().equals("400200")){
				//err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN : Khusus produk Ini ini hanya untuk Agen Arco,silahkan cek kode agen penutupnya ");
			}
		}
		
		if(edit.getDatausulan().getLsbs_id().intValue()==189 && (edit.getDatausulan().getLsdbs_number().intValue()>=15 && edit.getDatausulan().getLsdbs_number()<=30)){
			if(!edit.getAgen().getKode_regional().equals("400201")){
				//err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN : Khusus produk Ini ini hanya untuk Agen SMP,silahkan cek kode agen penutupnya ");
			}
		}
		
		if(edit.getDatausulan().getLsbs_id().intValue()==163 && (edit.getDatausulan().getLsdbs_number().intValue()>=6 && edit.getDatausulan().getLsdbs_number()<=10)){
			if(!edit.getAgen().getKode_regional().equals("400102")){
				//err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN : Khusus produk Ini ini hanya untuk Agen Bancass Sinarmas ,silahkan cek kode agen penutupnya");
			}
		}
		
		//referensi(Tambang emas)
		//validasi jika ada referral maka wajib isi id referral
		/*if((edit.getAgen().getId_ref()==null || edit.getAgen().getId_ref().equals("")) || (edit.getAgen().getName_ref()==null || edit.getAgen().getName_ref().equals(""))){
			Integer ref = uwManager.selectCekRef(edit.getPemegang().getMcl_first(), defaultDateFormat.format(edit.getPemegang().getMspe_date_birth()), edit.getTertanggung().getMcl_first(), defaultDateFormat.format(edit.getTertanggung().getMspe_date_birth()));
			if(ref>0)edit.getAgen().setPesan_ref("Polis ini memiliki Id Referral");
		}else{
			edit.getAgen().setPesan_ref(null);
		}*/
		
		if((edit.getAgen().getId_ref()==null || edit.getAgen().getId_ref().equals("")) || (edit.getAgen().getName_ref()==null || edit.getAgen().getName_ref().equals(""))){
			Integer ref = uwManager.selectCekRef(edit.getPemegang().getMcl_first(), defaultDateFormat.format(edit.getPemegang().getMspe_date_birth()), edit.getTertanggung().getMcl_first(), defaultDateFormat.format(edit.getTertanggung().getMspe_date_birth()));
			if(ref>0)err.rejectValue("agen.id_ref","","HARAP ISI ID REFERRAL DENGAN BENAR");
		}
		//end referensi
		
		if(hasil_agen.trim().length()==0 )
		{
			String spaj = edit.getPemegang().getReg_spaj();
			//Integer jumlah_cancel;
			/*if (!spaj.equalsIgnoreCase(""))
			{
				jumlah_cancel = (Integer)this.elionsManager.count_mst_cancel(spaj);
			}else{
				jumlah_cancel = new Integer(0);
			}*/
			Map data;
			if (jumlah_cancel.intValue() == 0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
			{
				data = (HashMap) this.elionsManager.selectagenpenutup(kode_penutup);
			}else{
				data = (HashMap) this.elionsManager.selectagenpenutup_endors(kode_penutup);
			}
			if (data!=null)
			{		
				nama_penutup=(String)data.get("NAMA");
				kode_regional=(String)data.get("REGIONID");
				nama_regional = (String)data.get("REGION");
				ulink=((Integer)data.get("ULINK"));
				sertifikasi = ((Integer)data.get("SERTIFIKAT"));
				cabang = (String)data.get("LCAID");
				tanggal_sertifikat = (Date)data.get("BERLAKU");
				edit.getAgen().setLca_id(cabang);
				edit.getAgen().setMsag_asnew((Integer)data.get("MSAG_ASNEW"));

				if (FormatString.rpad("0",(kode_regional.substring(0,4)),4).equalsIgnoreCase("0903"))
				{
					komisi_bii=new Integer(1);
				}
				String a = FormatString.rpad("0",(kode_regional.substring(0,2)),2);
				String b =FormatString.rpad("0",(kode_regional.substring(0,4)),4);
				//logger.info(a);
				//logger.info(b);
				if (FormatString.rpad("0",(kode_regional.substring(0,2)),2).equalsIgnoreCase("09") && !(FormatString.rpad("0",(kode_regional.substring(0,4)),4).equalsIgnoreCase("0914")))
				{
					ao=new Integer(1);
				}

				
				if (kode_regional_penagihan.equalsIgnoreCase(""))
				{
					kode_regional_penagihan = kode_regional;
					
				}
				
			}else{
				hasil_agen="Agen tersebut tidak terdaftar, silahkan hubungi Agency Support atau IT div.";
				err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hasil_agen);
				
			}			
			Date tanggal_beg_date = edit.getDatausulan().getMste_beg_date();
			if(edit.getDatausulan().getLsbs_id().intValue()!=155 && edit.getDatausulan().getLsbs_id().intValue()!=134 && (edit.getDatausulan().getLsbs_id().intValue()!=142 || edit.getDatausulan().getLsdbs_number().intValue()!=2) && (edit.getDatausulan().getLsbs_id().intValue()!=175 || edit.getDatausulan().getLsdbs_number().intValue()!=2) && (edit.getDatausulan().getLsbs_id().intValue()!=164 || edit.getDatausulan().getLsdbs_number().intValue()!=2) ){
				if(edit.getCurrentUser().getCab_bank().equals("")  ) {
					if((!edit.getCurrentUser().getLca_id().equals("58") && edit.getCurrentUser().getJn_bank().intValue() != 2 && edit.getCurrentUser().getJn_bank().intValue() != 3)){
						if (hasil_agen.trim().length()==0)
						{
							if (jumlah_cancel.intValue() == 0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
							{
								if(!cbg.equalsIgnoreCase("09")){
									hasil_agen=d_agen.sertifikasi_agen(kode_penutup,ulink.intValue(),sertifikasi.intValue(),tanggal_sertifikat, tanggal_beg_date);
									if (hasil_agen.trim().length()!=0)
									{
										if (jumlah_cancel.intValue() == 0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
										{
											err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hasil_agen);
										}
									}
								}
							}
						}
					}
				}
			}
		}else{
			err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hasil_agen);
		}
		
		if(!edit.getCurrentUser().getLca_id().equals("58")){
			if(hasil_agen.trim().length()==0)
			{	
				String nama_produk="";
				if (Integer.toString(kode_produk.intValue()).trim().length()==1)
				{
					nama_produk="produk_asuransi.n_prod_0"+kode_produk;	
				}else{
					nama_produk="produk_asuransi.n_prod_"+kode_produk;	
				}
				try{
					Class aClass1 = Class.forName( nama_produk );
					n_prod produk1 = (n_prod)aClass1.newInstance();
					produk1.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
					produk1.cek_flag_agen(kode_produk.intValue(),nomor_produk.intValue(), edit.getPowersave().getFlag_bulanan());
			
					flag_worksite = new Integer(produk1.flag_worksite);
					flag_endowment = new Integer(produk1.flag_endowment);
					Integer flag_ekalink = new Integer(produk1.flag_ekalink);
					edit.getDatausulan().setFlag_ekalink(flag_ekalink);
					produk1.of_set_bisnis_no(nomor_produk.intValue());
					produk1.ii_bisnis_no=(nomor_produk.intValue());
					produk1.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
					Integer flag_powersave = new Integer(produk1.of_get_bisnis_no(edit.getPowersave().getFlag_bulanan()));
					flag_account= new Integer(produk1.flag_account);
					if (autodebet.intValue() == 3)
					{
						flag_account = new Integer(2);
					}
					flag_as=new Integer(produk1.flag_as);
					flag_jenis_plan = new Integer(produk1.flag_jenis_plan);
					edit.getDatausulan().setFlag_jenis_plan(flag_jenis_plan);
					if(edit.getDatausulan().getMste_flag_el()==1 || edit.getDatausulan().getMste_flag_el()==4){
						flag_as=2;
						produk1.flag_as= flag_as;
					}
					if (produk1.flag_as==2 && kode_produk!=164)
					{
						hasil_agen=d_agen.agen_link_karyawan(kode_penutup);
						if (hasil_agen.trim().length()!=0)
						{
							err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hasil_agen);
						}
					}else{
						//AGEN BARU
						if (kode_penutup.equalsIgnoreCase("000000"))
						{
							nama_penutup=edit.getAgen().getMcl_first();
							if (nama_penutup==null)
							{
								nama_penutup="";
								edit.getAgen().setMcl_first(nama_penutup);
							}
							hasil_nama_penutup=d_agen.nama_penutup(nama_penutup);
	
							if (hasil_nama_penutup.trim().length()!=0)
							{
								err.rejectValue("agen.mcl_first","","HALAMAN DETIL AGEN :" +hasil_nama_penutup);
							}
							if (cbg.equalsIgnoreCase("01") || cbg.equalsIgnoreCase("07") || cbg.equalsIgnoreCase("09"))
							{
								kode_regional=cbg.concat("0100");
							}else{
								kode_regional=cbg.concat("0000");
							}
	
							Map data = (HashMap) this.elionsManager.selectregional(kode_regional);
	
							if (data!=null)
							{		
								nama_regional = (String)data.get("LSRG_NAMA");
							}
						}else{
							//Cek produk eka sarjana Mandiri, agen yg boleh menginput hanya pihak Corporate Marketing, Worksite, Regional, Hybrid, Agency (SK No.  014/ /AJS-SK/II/2009)
							//Tambahan untuk produk Ekawaktu, agen yg boleh input sama seperti Eka sarjana Mandiri (SK No. 056/AJS-SK/V/2009)
							//Tambahan untuk produk Ekalink Family bisa dijual agent worksite juga(Awalnya : agency dan Regional saja) (IM No.054/IM-DIR/VIII/2010)
							if(kode_produk==159 || kode_produk==173 || kode_produk==172 || kode_produk==169 || kode_produk==179 || kode_produk==180 || kode_produk==183 || kode_produk==189 || kode_produk==193){
								//Yusuf 11/3/09
								if(kode_regional.substring(0, 2).equalsIgnoreCase("42")){
									flag_worksite = 1;
									edit.getDatausulan().setFlag_worksite(flag_worksite);
								}else {
									flag_worksite = 0;
									edit.getDatausulan().setFlag_worksite(flag_worksite);
								}
	//							if(kode_regional.substring(0, 2).equalsIgnoreCase("37") || kode_regional.substring(0, 2).equalsIgnoreCase("46") ){
	//								flag_worksite=0;
	//							}
	//							hasil_agen = d_agen.agen_worksite_as(kode_regional);
	//							if (hasil_agen.trim().length()!=0)
	//							{
	//								err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hasil_agen);
	//							}
							}else {
								if (produk1.flag_as==1)
								{
									hasil_agen=d_agen.agen_as(kode_regional);
									if (hasil_agen.trim().length()!=0)
									{
										err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hasil_agen);
									}
								}else if ((produk1.flag_as==0))
									{
										if (produk1.isProductBancass==false)
										{
											if (produk1.flag_worksite == 1)
											{
												hasil_agen=d_agen.agen_worksite(kode_regional);
												if (hasil_agen.trim().length()!=0)
												{
													err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hasil_agen);
												}	
											}else if (produk1.flag_artha == 1)
												{
													hasil_agen=d_agen.agen_artha(kode_regional);
													if (hasil_agen.trim().length()!=0)
													{
														err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hasil_agen);
													}
													if (kode_produk.intValue()== 162)
													{
														if (kode_regional==null)
														{
															kode_regional="";
														}
														if (nomor_produk.intValue()<5)
														{
															//arthamas
															if (!(kode_regional.substring(0,2).equalsIgnoreCase("46")))
															{
																String hsl="Plan ini, agen penutupnya harus agen arthamas ."	;
																err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hsl);
															}
														}else{
															//regional
	//														if ((kode_regional.substring(0,2).equalsIgnoreCase("46")))
	//														{
	//															String hsl="Plan ini, agen penutupnya tidak boleh agen arthamas ."	;
	//															err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hsl);
	//														}
														}
													}
												}
	//										else if(edit.getDatausulan().getLsbs_id()!=183){
	//												hasil_agen=d_agen.agen_rg(kode_regional);
	//												if (hasil_agen.trim().length()!=0)
	//												{
	//													err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hasil_agen);
	//												}		
	//										}
										}else{
											if (flag_powersave == 6)
											{
												hasil_agen = d_agen.agen_permata(kode_regional);
												if (hasil_agen.trim().length()!=0)
												{
													err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hasil_agen);
												}
											}else{
												hasil_agen=d_agen.agen_bao(kode_regional);
												if (hasil_agen.trim().length()!=0)
												{
													err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hasil_agen);
												}else{
													hasil_agen = d_agen.agen_reff_bii(kode_regional,produk1.flag_reff_bii);
													if (hasil_agen.trim().length()!=0)
													{
														//err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hasil_agen);
													}
												}
											}
										}
									}else if((produk1.flag_as==3 && (kode_produk==182 && nomor_produk!=13) && (kode_produk==182 && nomor_produk!=14) && (kode_produk==182 && nomor_produk!=15)))
										{
											hasil_agen=d_agen.agen_rg_as(kode_regional);
											if (hasil_agen.trim().length()!=0)
											{
												err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hasil_agen);
											}	
										}
									}
								}
							}
					
					if (FormatString.rpad("0",(kode_regional.substring(0,2)),2).equalsIgnoreCase("09"))
					{
						ao=new Integer(1);
					}
						
					if (pribadi == null)
					{
						pribadi =new Integer(0);
					}
	
					if (ao.intValue()==0)
					{
						kode_ao="";
						nama_ao="";
					}else{
						hasil_ao = d_agen.kode_ao(kode_ao);
						if (hasil_ao.trim().length()!=0)
						{
							err.rejectValue("pemegang.mspo_ao","","HALAMAN DETIL AGEN :" +hasil_ao);
						}else{
		
							Map data = (HashMap) this.elionsManager.selectagenao(kode_ao);
	
							if (data!=null)
							{		
								nama_ao = (String)data.get("nama");
							}else{
								hasil_ao="Kode AO tidak terdaftar.";
								err.rejectValue("pemegang.mspo_ao","","HALAMAN DETIL AGEN :" +hasil_ao);
							}
						}
					}
					edit.getPemegang().setMspo_ref_bii(ao);
					edit.getPemegang().setMspo_pribadi(pribadi);
					edit.getDatausulan().setMspr_discount(new Double(0));
		
					if (flag_as.intValue()==2)
					{
						//NIK_Karyawan
						String nik=edit.getEmployee().getNik();
						if (nik==null)
						{
							nik="";
							edit.getEmployee().setNik(nik);
						}
						if (nik.trim().length()==0)
						{
							hasil_nik="Khusus produk karyawan harus mengisi NIK Karyawan terlebih dahulu pada halaman pemegang polis";
							err.rejectValue("employee.nik","","HALAMAN DETIL AGEN :" +hasil_nik);
						}else{
							hasil_nik = d_agen.nik_karyawan(nik);
							if (hasil_nik.trim().length()!=0)
							{
								err.rejectValue("employee.nik","","HALAMAN DETIL AGEN :" +hasil_nik);
							}else{
	
								Map data1= (HashMap) this.elionsManager.select_nik_karyawan(nik);
//								Map data2= (HashMap) this.uwManager.select_nik_karyawan_ttp(nik);
								
								if (data1!=null)
								{		
									nama_karyawan = (String)data1.get("MCL_FIRST");
									cabang_karyawan = (String)data1.get("LCA_NAMA");
									dept_karyawan = (String)data1.get("LDE_DEPT");
									status_karyawan = (String)data1.get("STS_KRY");
								}else if(data1==null){
										hasil_nik="NIK yang dimasukan harus NIK dari Karyawan tetap/percobaan/kontrak.";
										err.rejectValue("employee.nik","","HALAMAN DETIL AGEN :" +hasil_nik);
									
								}else{
									hasil_nik="NIK tidak terdaftar.";
									err.rejectValue("employee.nik","","HALAMAN DETIL AGEN :" +hasil_nik);
								}
							}
						}
						edit.getEmployee().setNama(nama_karyawan);
						edit.getEmployee().setCabang(cabang_karyawan);
						edit.getEmployee().setDept(dept_karyawan);
						Integer jns_top_up_berkala=edit.getInvestasiutama().getDaftartopup().getPil_berkala();
						if (jns_top_up_berkala == null)
						{
							jns_top_up_berkala = new Integer(0);
						}
						Integer jns_top_up_tunggal=edit.getInvestasiutama().getDaftartopup().getPil_tunggal();
						if (jns_top_up_tunggal == null)
						{
							jns_top_up_tunggal = new Integer(0);
						}
						Double premi_pokok = edit.getDatausulan().getMspr_premium();
						if (premi_pokok ==null)
						{
							premi_pokok = new Double(0);
						}
						if ((jns_top_up_tunggal.intValue() == 2 || jns_top_up_tunggal.intValue() == 0 ) && jns_top_up_berkala.intValue() == 0)
						{
							edit.getEmployee().setPotongan(premi_pokok);
						}
						if (jns_top_up_berkala.intValue() == 1)
						{
							Double topup1 = edit.getInvestasiutama().getDaftartopup().getPremi_berkala();
							if (topup1 == null)
							{
								topup1 = new Double(0);
							}
							Double total_potongan = new Double(premi_pokok.doubleValue() + topup1.doubleValue() );
							edit.getEmployee().setPotongan(total_potongan);
						}
					}
					
					edit.getAgen().setKode_regional(kode_regional);
					edit.getAgen().setLsrg_nama(nama_regional);
					edit.getAgen().setMsag_id(kode_penutup);
					edit.getAgen().setMcl_first(nama_penutup);
					
					//MANTA - Broker
					if(!edit.getCurrentUser().getLca_id().equals("58")){
						edit.getBroker().setLsb_id(kode_broker);
						edit.getBroker().setLsb_nama(nama_broker);
						edit.getBroker().setLbn_id(bank_broker);
						edit.getBroker().setNo_account(no_rek_broker);
					}
 
					edit.getPemegang().setMspo_ref_bii(ao);
					edit.getPemegang().setMspo_ao(kode_ao);
					edit.getPemegang().setMspo_pribadi(pribadi);
					edit.getAddressbilling().setRegion(kode_regional_penagihan);
					edit.getPemegang().setNama_ao(nama_ao);
					edit.getPemegang().setMspo_komisi_bii(komisi_bii);
					edit.getPemegang().setMspo_follow_up(followup);
					
					if(edit.getPemegang().getStatus().equalsIgnoreCase("edit")){
						//Deddy - 3 Jan 2011 - (REQ Rudy) Penambahan apabila proses edit, dan lca_id sebelumnya 01, maka tidak bisa diganti dengan lca_id lain(dikarenakan user suka input msag_id yg memiliki komisi)
						//(bila minta dibukain, special case dengan comment bagian ini)
						if(!StringUtil.isEmpty(edit.getDatausulan().getReg_spaj())){
							if( FormatString.rpad("0",(edit.getDatausulan().getReg_spaj().substring(0,2)),2).equalsIgnoreCase("01")&& !FormatString.rpad("0",(kode_regional.substring(0,2)),2).equalsIgnoreCase("01")){
								err.rejectValue("agen.msag_id","","Untuk Pengeditan dari cabang Pusat menjadi cabang selain Pusat tidak dapat dilakukan.");
							}
						}
					}
					
	
	
				// KHUSUS WORKSITE  harus input perusahaan , ekalife harus input nik
					if (flag_worksite.intValue() == 1)
					{
						String hasil_worksite= "";
						String kode_perusahaan = edit.getPemegang().getMspo_customer();
						if (kode_perusahaan==null)
						{
							kode_perusahaan="";
							edit.getPemegang().setMspo_customer(kode_perusahaan);
						}
						
						if (kode_perusahaan.equalsIgnoreCase(""))
						{
							//err.rejectValue("pemegang.mspo_customer","","HALAMAN DETIL AGEN :" +"Silahkan mengisi nama perusahaan terlebih dahulu.");
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
						
						// 1 masih karyawan aktif
						edit.getPemegang().setDewasa(new Integer(1));
						
						String nik_worksite = edit.getPemegang().getNik();
						if (nik_worksite ==null)
						{
							nik_worksite="";
						}
						if (nik_worksite.trim().equalsIgnoreCase(""))
						{
							//err.rejectValue("pemegang.nik","","HALAMAN DETIL AGEN :" +"Silahkan mengisi Nik karyawan terlebih dahulu.");
						}
						edit.getPemegang().setNik(nik_worksite);
						
						if (nama_perusahaan.toUpperCase().contains("EKA LIFE") || nama_perusahaan.toUpperCase().contains("EKALIFE"))
						{
//							edit.getDatausulan().setMste_flag_el(new Integer(1));
							//edit.getPemegang().setMspo_customer("080000000024");
	
							if (kode_produk.intValue() == 141)
							{
								pribadi = new Integer(1);
							}
							edit.getPemegang().setMspo_pribadi(pribadi); // 1 kalau karyawan eka life jadi ada komisi
							Map data2= (HashMap) this.elionsManager.select_nik_karyawan(nik_worksite);
	
							if (data2!=null)
							{		
								nama_karyawan = (String)data2.get("MCL_FIRST");
								cabang_karyawan = (String)data2.get("LCA_NAMA");
								dept_karyawan = (String)data2.get("LDE_DEPT");
								edit.getEmployee().setNik(nik_worksite);
								edit.getDatausulan().setFlag_worksite_ekalife(new Integer(1));
								edit.getEmployee().setNama(nama_karyawan);
								edit.getEmployee().setCabang(cabang_karyawan);
								edit.getEmployee().setDept(dept_karyawan);
								edit.getDatausulan().setMste_flag_el(new Integer(1));
							}else{
								hasil_nik="NIK tidak terdaftar.";
								err.rejectValue("pemegang.nik","","HALAMAN DETIL AGEN :" +hasil_nik);
							}	
						}else{
							edit.getDatausulan().setFlag_worksite_ekalife(new Integer(0));
						}
					}else{
						if(edit.getPemegang().getMspo_customer().equals("")){
							edit.getPemegang().setMspo_customer("");
							edit.getPemegang().setMspo_customer_nama("");
							edit.getPemegang().setDewasa(null);
							edit.getPemegang().setNik("");
						}
						
					}				
					//produk endowment 20 PT PERKASA ABADI
					if (flag_endowment.intValue() == 1)
					{
						
						String nama_perusahaan=produk1.perusahaan[nomor_produk.intValue()]; //1 "KSO.PERKASA ABADI";
						Double disc =new Double(0);
						Double jumlah_disc = new Double(0);
						Double premi = edit.getDatausulan().getMspr_premium();
						disc = produk1.discount[nomor_produk.intValue()];
						jumlah_disc = new Double(disc.doubleValue() * premi.doubleValue() /100); 
						edit.getDatausulan().setMspr_discount(jumlah_disc);
						
						String kode_perusahaan = "";
						Map data = (HashMap) this.elionsManager.select_company_endow(nama_perusahaan);
						if (data!=null)
						{
							kode_perusahaan = (String)data.get("COMPANY_ID");
						}
						edit.getPemegang().setMspo_customer(kode_perusahaan);
						edit.getPemegang().setMspo_customer_nama(nama_perusahaan);
						edit.getPemegang().setDewasa(new Integer(1));
					}
					
					//Deddy(10 aug 2012) - efektif tgl 15 aug 2012, validasi khusus mnc.
					if(edit.getAgen().getLca_id().equals("62")){
						if(kode_produk.intValue() == 140 || kode_produk.intValue() == 141 || kode_produk.intValue() == 159){//From Novie(22 Nov 2012) - Baru berjalan untuk 3 plan produk : smile link, medivest, dan eduvest
						//apabila tutupan mnc, harus memilih salah satu special offer.
							if(edit.getTertanggung().getMste_flag_special_offer()==null || edit.getTertanggung().getMste_flag_special_offer()==0){
								//(LUFI(08092013))request Aktuari dan UW MNC boleh ambil none special Offer
								//err.rejectValue("tertanggung.mste_flag_special_offer","","HALAMAN DETIL AGEN :KHUSUS UNTUK TUTUPAN MNC, special offer silakan dipilih salah satu");
							}else{
								if(edit.getTertanggung().getMste_flag_special_offer()==2){
									Double premi_additional = edit.getDatausulan().getMspr_premium() * 0.4;
									if (kode_produk.intValue() == 140 || kode_produk.intValue() == 141)
									{
										 premi_additional = edit.getDatausulan().getMspr_premium() * 0.3;
									}
									edit.getDatausulan().setPremi_additional_unit(premi_additional);
								}else{
									edit.getDatausulan().setPremi_additional_unit(0.);
								}
							}
						}
					}else{
						//apabila selain mnc, lgsg set 0 flag special offer.
						edit.getTertanggung().setMste_flag_special_offer(0);
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
			}
		
		}	
		
		//validate hadiah
		if(edit.getDatausulan().getLsbs_id()==184 && edit.getDatausulan().getLsdbs_number()==6){
			Hadiah lh_id = uwManager.selectLh_id(edit.getDatausulan().getMspr_premium());
			
			if(lh_id!=null && edit.getPemegang().getFlag_standard()==0){
				if(edit.getPemegang().getDaftar_hadiah().isEmpty())
					err.rejectValue("pemegang.flag_standard","","HALAMAN DETIL AGEN : Harap pilih hadiah");
			}
		}
		if(edit.getPemegang().getDaftar_hadiah()!=null){
			if(!edit.getPemegang().getDaftar_hadiah().isEmpty()){
				
				Double premi = (double) 0;
				Double premi_hadiah = (double) 0;
				Integer unit = 0;
				
				for(int i=0;i<edit.getPemegang().getDaftar_hadiah().size();i++){
					Hadiah hadiah = new Hadiah();
					
					hadiah = edit.getPemegang().getDaftar_hadiah().get(i);
					
					premi_hadiah = hadiah.lh_harga * hadiah.mh_quantity;
					
					premi = premi + premi_hadiah;
					if(hadiah.mh_quantity==0)unit += 1;
					
				}
				
				if(premi > edit.getDatausulan().getMspr_premium() && unit > 0){
					err.rejectValue("pemegang.flag_standard","","HALAMAN DETIL AGEN : Hadiah melebihi premi dan unit hadiah tidak boleh 0");
				}else if(premi > edit.getDatausulan().getMspr_premium()){
					err.rejectValue("pemegang.flag_standard","","HALAMAN DETIL AGEN : Hadiah melebihi premi");
				}else if(unit > 0){
					err.rejectValue("pemegang.flag_standard","","HALAMAN DETIL AGEN : Unit hadiah tidak boleh 0");
				}
			}
	   }	
	}
}