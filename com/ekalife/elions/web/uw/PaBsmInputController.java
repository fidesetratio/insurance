package com.ekalife.elions.web.uw;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.bac.support.form_agen;
import com.ekalife.utils.Common;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.parent.ParentFormController;

import id.co.sinarmaslife.std.spring.util.Email;

public class PaBsmInputController extends ParentFormController{
	private long accessTime;
	protected final Log logger = LogFactory.getLog(getClass());
	private Email email;
	
	public void setEmail(Email email) {
		this.email = email;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		// Apabila proses cek kartu maka skip validasi dulu
		boolean cek_kartu = ServletRequestUtils.getBooleanParameter(request, "cek_kartu", false);
		if(cek_kartu) return;
		
		Pas pas=(Pas)cmd;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		// Validasi kartu pas
		validasiKartu(request, pas, err);
		
		// menentukan pas save langsung muncul konfirmasi transfer ke uw pada pas_detail
		String flag_posisi=ServletRequestUtils.getStringParameter(request, "flag_posisi",null);
		request.setAttribute("flag_posisi", flag_posisi);
		
		Date nowDate = elionsManager.selectSysdate();
		
		if(pas.getMsp_pas_beg_date() == null){
			nowDate = elionsManager.selectSysdate();
		}else{
			nowDate = pas.getMsp_pas_beg_date();
		}
		
		if(pas.getProduct_code() == null)pas.setProduct_code("");
		
		if(!"typeafree".equals(pas.getInput_type()) && !"73".equals(pas.getProduct_code()) && !"default".equals(pas.getProduct_code())){
			form_agen d_agen= new form_agen(); 
			Agen agen = new Agen();
			String agen_hsl = "";
			if(pas.getMsag_id() == null)pas.setMsag_id("");
			if(!"".equals(pas.getMsag_id())){
				agen = uwManager.select_detilagen2(pas.getMsag_id());
				if(agen.getMsag_id() != null){
					agen_hsl = d_agen.sertifikasi_agen(pas.getMsag_id(), 0,agen.getMsag_sertifikat(),agen.getMsag_berlaku(), nowDate);
				}else{
					err.reject("","Agen : Kode Agen tidak berlaku");
				}
			}
			
			if(!"".equals(agen_hsl)){
				err.reject("","Agen : Kode Agen tidak berlaku");
			}
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
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_postal_code", "","PAS : Kode Pos harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_address_1", "","PAS : Alamat harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_phone_number", "","PAS : No. Telepon harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_mobile", "","PAS : No. HP harus diisi");
		
		//20190307 Mark Valentino - Perbaikan PA Konven
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_email", "","PAS : Alamat Email harus diisi");	
		
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
		if(!(umurTt>=1 && umurTt<=60)){ //batas 1 s/d 60 tahun
			err.reject("","PAS : Umur tertanggung minimal 1 tahun & maksimal 60 tahun");
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
		
		if(!Common.isEmpty(pas.getMsp_mobile())) {
			if(!Common.validPhone(pas.getMsp_mobile().trim())) {
				err.reject("", "PAS : No. HP harus diisi angka");
			}
			
			if(pas.getMsp_mobile().trim().length() > 20) {
				err.reject("", "PAS : No. HP terlalu panjang, max 20 digit");
			}
		}
		
		if(!Common.isEmpty(pas.getMsp_mobile2())) {
			if(!Common.validPhone(pas.getMsp_mobile2().trim())) {
				err.reject("", "PAS : No. HP2 harus diisi angka");
			}
			
			if(pas.getMsp_mobile2().trim().length() > 20) {
				err.reject("", "PAS : No. HP2 terlalu panjang, max 20 digit");
			}
		}
		
		//AGEN
		if(!"typeafree".equals(pas.getInput_type()) && !"73".equals(pas.getProduct_code()) && !"default".equals(pas.getProduct_code())){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msag_id", "","AGEN : Kode Agen harus diisi");
		}

		//REKENING
        pas.setLsbp_id("156");// default bank sinarmas untuk semua produk
        //pas.setLsbp_id_autodebet("156");// default bank sinarmas untuk semua produk
        pas.setPanjang_no_rek(uwManager.select_panj_rek1(pas.getLsbp_id()));
        
		if(!"73".equals(pas.getProduct_code())){
			if(!"".equals(pas.getLsbp_id()) && !"0".equals(pas.getLsbp_id())){
				
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "lsbp_id", "","REKENING : Bank harus diisi");
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_no_rekening", "","REKENING : No. Rekening harus diisi");
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_rek_cabang", "","REKENING : Cabang harus diisi");
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_rek_kota", "","REKENING : Kota harus diisi");
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_rek_nama", "","REKENING : Atas Nama harus diisi");
					
					// Validasi panjang no rek bank sinarmas
//					if("156".equals(pas.getLsbp_id())) {
//						Integer rek_length = pas.getMsp_no_rekening().length();
//						if(rek_length > 0 && rek_length < 10)
//							err.reject("", "REKENING : No. Rekening Bank Sinarmas harus 10 digit");
//					}
					Integer rek_length = pas.getMsp_no_rekening().length();
					Integer max_length = uwManager.select_panj_rek1(pas.getLsbp_id());
                    if(rek_length > 0 && rek_length < max_length)
                        err.reject("", "REKENING : No. Rekening harus " + max_length + " digit");
			}
		}
		if(pas.getMsp_flag_cc() != null && (pas.getMsp_flag_cc() == 2 || pas.getMsp_flag_cc() == 1)){//tabungan/kartu kredit
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "lsbp_id_autodebet", "","REKENING (AUTODEBET) : Bank harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_no_rekening_autodebet", "","REKENING (AUTODEBET) : No. Rekening harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_tgl_debet", "","REKENING (AUTODEBET) : Tanggal debet harus diisi");
			if(pas.getMsp_flag_cc() == 1){
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_tgl_valid", "","REKENING (AUTODEBET) : Tanggal valid harus diisi");
			}
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_rek_nama_autodebet", "","REKENING (AUTODEBET) : Atas Nama harus diisi");
			
			if("73".equals(pas.getProduct_code())) {
				// Validasi panjang no rek bank sinarmas
//				if("156".equals(pas.getLsbp_id())) {
//					Integer rek_length = pas.getMsp_no_rekening_autodebet().length();
//					if(rek_length > 0 && rek_length < 10)
//						err.reject("", "REKENING (AUTODEBET) : No. Rekening Bank Sinarmas harus 10 digit");
//				}
				Integer rek_length = pas.getMsp_no_rekening_autodebet().length();
				Integer max_length = uwManager.select_panj_rek1(pas.getLsbp_id());
                if(rek_length > 0 && rek_length < max_length)
                    err.reject("", "REKENING (AUTODEBET) : No. Rekening harus " + max_length + " digit");
				
				// Validasi Debet
				String validasiDebet = ServletRequestUtils.getStringParameter(request, "validasiDebet", null);
				if(validasiDebet == null)
					err.reject("", "VALIDASI (AUTODEBET) : Validasi autodebet harus diisi");
				else if(validasiDebet.equals("0"))
					err.reject("", "VALIDASI (AUTODEBET) : Harus ada surat kuasa debet yang sudah diisi dan ditandatangani nasabah");
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
			
			if("73".equals(pas.getProduct_code())){
				// Copy data dari autodebet ke rek
				HashMap<String, Object> bank = uwManager.selectKotaBank(bank_pp2);
				if(bank != null) {
					pas.setMsp_rek_cabang((String) bank.get("CABANG"));
					pas.setMsp_rek_kota((String) bank.get("KOTA"));
				}
			}
		}
		
		if("73".equals(pas.getProduct_code())){
			// Copy data dari autodebet ke rek
			if(pas.getMsp_rek_nama_autodebet() != null && !"".equals(pas.getMsp_rek_nama_autodebet())) {
				pas.setMsp_rek_nama(pas.getMsp_rek_nama_autodebet());
			}
			if(pas.getMsp_no_rekening_autodebet() != null && !"".equals(pas.getMsp_no_rekening_autodebet())) {
				pas.setMsp_no_rekening(pas.getMsp_no_rekening_autodebet());
			}
		}
		
		request.setAttribute("kode_nama_list",DroplistManager.getInstance().get("KODE_NAMA.xml","ID",request));
		request.setAttribute("kode_alamat_list",DroplistManager.getInstance().get("KODE_ALAMAT.xml","ID",request));
		request.setAttribute("kode_okupasi_list",DroplistManager.getInstance().get("KODE_OKUPASI.xml","ID",request));
		request.setAttribute("kode_obyek_sekitar_list",DroplistManager.getInstance().get("KODE_OBYEK_SEKITAR.xml","ID",request));
		request.setAttribute("carabayar_pas",DroplistManager.getInstance().get("CARABAYAR_PAS.xml","ID",request));
		request.setAttribute("autodebet_pas",DroplistManager.getInstance().get("AUTODEBET_PAS.xml","ID",request));
		request.setAttribute("relasi_pas",DroplistManager.getInstance().get("RELATION_PAS.xml","ID",request));
		
		if("typeafree".equals(pas.getInput_type())){
			Map<String,String> produk = new HashMap<String,String>();
			produk.put("ID", "1");
			produk.put("PRODUK", "PLAN A");
			Map<String,String> produk_up = new HashMap<String,String>();
			produk_up.put("VALUE", "50000000");
			produk_up.put("LABEL", "Rp 50.000.000,-");
			Map<String,String> relasi = new HashMap<String,String>();
			relasi.put("ID", "1");
			relasi.put("RELATION", "DIRI SENDIRI");
			List<Map> produkList = new ArrayList<Map>();
			produkList.add(produk);
			List<Map> produkUpList = new ArrayList<Map>();
			produkUpList.add(produk_up);
			List<Map> relasiList = new ArrayList<Map>();
			relasiList.add(relasi);
			request.setAttribute("produk_pa_bsm",produkList);
			request.setAttribute("produk_pa_up_bsm",produkUpList);
			request.setAttribute("relasi_pas",relasiList);
		} else if("73".equals(pas.getProduct_code())) {
			Map<String,String> relasi = new HashMap<String,String>();
			relasi.put("ID", "1");
			relasi.put("RELATION", "DIRI SENDIRI");
			List<Map> relasiList = new ArrayList<Map>();
			request.setAttribute("relasi_pas",relasiList);
			relasiList.add(relasi);
			HashMap<String, Object> autodebet = new HashMap<String, Object>();
			autodebet.put("ID", "2");
			autodebet.put("AUTODEBET", "TABUNGAN");
			ArrayList<HashMap<String, Object>> autodebetList = new ArrayList<HashMap<String, Object>>();
			autodebetList.add(autodebet);
			request.setAttribute("autodebet_pas", autodebetList);
		} else if("default".equals(pas.getInput_type())) {
			request.setAttribute("autodebet_pas", new ArrayList());
			Map<String,String> relasi = new HashMap<String,String>();
			relasi.put("ID", "1");
			relasi.put("RELATION", "DIRI SENDIRI");
			List<Map> relasiList = new ArrayList<Map>();
			relasiList.add(relasi);
			request.setAttribute("relasi_pas",relasiList);
			request.setAttribute("produk_pa_bsm", new ArrayList());
			request.setAttribute("produk_pa_up_bsm", new ArrayList());
		} else {
			//request.setAttribute("produk_pa_bsm",DroplistManager.getInstance().get("PRODUK_PA_BSM.xml","ID",request));
			//request.setAttribute("produk_pa_up_bsm",DroplistManager.getInstance().get("PRODUK_PA_UP_BSM.xml","ID",request));
			request.setAttribute("relasi_pas",DroplistManager.getInstance().get("RELATION_PAS.xml","ID",request));
		}
		request.setAttribute("sumber_pendanaan",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","ID",request));
		request.setAttribute("bidang_industri",DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","ID",request));
		request.setAttribute("pekerjaan",DroplistManager.getInstance().get("PEKERJAAN_PAS.xml","ID",request));
		
		//request.setAttribute("addError", "addError");
				//err.reject("");
		
		// Referral BSM
		if("73".equals(pas.getProduct_code())) {
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "lrb_id", "", "REFERRAL : Agen Penutup harus diisi");
		}
	}
	
//	protected ModelAndView onAddnewrow( HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors ) throws Exception
//    {
//		CmdInputBlacklist detiledit = (CmdInputBlacklist) cmd;
//		
//		return new ModelAndView(
//        "uw/input_blacklist_customer").addObject("cmd",detiledit);
//    }

	protected ModelAndView onSubmit( HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors ) throws Exception
    {

		Pas pas=(Pas)cmd;
		//User currentUser = (User) session.getAttribute("currentUser");
		Map<String, Object> map = new HashMap<String, Object>();
		
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		map.put("user_id", lus_id);
		//user.setLca_id("58");
		Cmdeditbac edit = new Cmdeditbac();
		
		if(pas.getReg_spaj() == null)pas.setReg_spaj("");
		if(pas.getPribadi() == null)pas.setPribadi(0);
		
		if("insert".equals(request.getParameter("kata"))){
				pas.setPremi(getPremi(new Double(pas.getMsp_up()), pas.getProduk(), pas.getInput_type()) + "");
				pas.setMsp_premi(pas.getPremi());
				pas.setLus_id(lus_id);
				pas.setLus_login_name(user.getLus_full_name());
			//menentukan jenis pas (individu, dmtm, mallinsurance, ap/bp)
				pas.setJenis_pas("PABSM");
			//=================================
				// Jika no kartu = 0 maka di null kan
				if("0".equals(pas.getNo_kartu()))
					pas.setNo_kartu(null);
				// Pd pas bsm no sertifikat = no kartu
				if("73".equals(pas.getProduct_code())) {
					pas.setNo_sertifikat(pas.getNo_kartu());
					// Jika reff_id nya kosong, copy dari lrb_id
					if(pas.getReff_id() == null)
						pas.setReff_id(pas.getLrb_id());
				}
			pas = uwManager.insertPaTemp(pas, user);
			if(pas.getStatus() == 1){
				request.setAttribute("successMessage","proses insert gagal");
			}else if(pas.getStatus() == 2){
				request.setAttribute("successMessage","proses transfer gagal");
			}else{
				Integer msp_id = uwManager.selectGetPasIdFromFireId(pas.getMsp_fire_id());
				request.setAttribute("msp_id",msp_id);
				request.setAttribute("successMessage","insert sukses");
			}
			
//			pas.setLus_id(lus_id);
//			pas.setLus_login_name(user.getLus_full_name());
//			edit=this.uwManager.prosesPas(request, "insert", pas, errors,"input",user,errors);
//			request.setAttribute("successMessage","insert sukses");
		}
		
//		schedulerPas();
		
		request.setAttribute("kode_nama_list",DroplistManager.getInstance().get("KODE_NAMA.xml","ID",request));
		request.setAttribute("kode_alamat_list",DroplistManager.getInstance().get("KODE_ALAMAT.xml","ID",request));
		request.setAttribute("kode_okupasi_list",DroplistManager.getInstance().get("KODE_OKUPASI.xml","ID",request));
		request.setAttribute("kode_obyek_sekitar_list",DroplistManager.getInstance().get("KODE_OBYEK_SEKITAR.xml","ID",request));
		request.setAttribute("carabayar_pas",DroplistManager.getInstance().get("CARABAYAR_PAS.xml","ID",request));
		request.setAttribute("autodebet_pas",DroplistManager.getInstance().get("AUTODEBET_PAS.xml","ID",request));
		request.setAttribute("relasi_pas",DroplistManager.getInstance().get("RELATION_PAS.xml","ID",request));
		if("typeafree".equals(pas.getInput_type())){
			Map<String,String> produk = new HashMap<String,String>();
			produk.put("ID", "1");
			produk.put("PRODUK", "PLAN A");
			Map<String,String> produk_up = new HashMap<String,String>();
			produk_up.put("VALUE", "50000000");
			produk_up.put("LABEL", "Rp 50.000.000,-");
			Map<String,String> relasi = new HashMap<String,String>();
			relasi.put("ID", "1");
			relasi.put("RELATION", "DIRI SENDIRI");
			List<Map> produkList = new ArrayList<Map>();
			produkList.add(produk);
			List<Map> produkUpList = new ArrayList<Map>();
			produkUpList.add(produk_up);
			List<Map> relasiList = new ArrayList<Map>();
			relasiList.add(relasi);
			request.setAttribute("produk_pa_bsm",produkList);
			request.setAttribute("produk_pa_up_bsm",produkUpList);
			request.setAttribute("relasi_pas",relasiList);
		} else if("73".equals(pas.getProduct_code())) {
			Map<String,String> relasi = new HashMap<String,String>();
			relasi.put("ID", "1");
			relasi.put("RELATION", "DIRI SENDIRI");
			List<Map> relasiList = new ArrayList<Map>();
			request.setAttribute("relasi_pas",relasiList);
			relasiList.add(relasi);
			HashMap<String, Object> autodebet = new HashMap<String, Object>();
			autodebet.put("ID", "2");
			autodebet.put("AUTODEBET", "TABUNGAN");
			ArrayList<HashMap<String, Object>> autodebetList = new ArrayList<HashMap<String, Object>>();
			autodebetList.add(autodebet);
			request.setAttribute("autodebet_pas", autodebetList);
		} else if("default".equals(pas.getInput_type())) {
			request.setAttribute("autodebet_pas", new ArrayList());
			Map<String,String> relasi = new HashMap<String,String>();
			relasi.put("ID", "1");
			relasi.put("RELATION", "DIRI SENDIRI");
			List<Map> relasiList = new ArrayList<Map>();
			relasiList.add(relasi);
			request.setAttribute("relasi_pas",relasiList);
			request.setAttribute("produk_pa_bsm", new ArrayList());
			request.setAttribute("produk_pa_up_bsm", new ArrayList());
		} else {
			request.setAttribute("produk_pa_bsm",DroplistManager.getInstance().get("PRODUK_PA_BSM.xml","ID",request));
			request.setAttribute("produk_pa_up_bsm",DroplistManager.getInstance().get("PRODUK_PA_UP_BSM.xml","ID",request));
			request.setAttribute("relasi_pas",DroplistManager.getInstance().get("RELATION_PAS.xml","ID",request));
		}
		request.setAttribute("sumber_pendanaan",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","ID",request));
		request.setAttribute("bidang_industri",DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","ID",request));
		request.setAttribute("pekerjaan",DroplistManager.getInstance().get("PEKERJAAN_PAS.xml","ID",request));
		
		return new ModelAndView(
        "uw/pa_bsm_input").addObject("cmd",pas);

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
		String input_type=ServletRequestUtils.getStringParameter(request, "input_type",null);
		Map<String, Object> refData = new HashMap<String, Object>();
		//currentUser.setLca_id("58");
		request.setAttribute("kode_nama_list",DroplistManager.getInstance().get("KODE_NAMA.xml","ID",request));
		request.setAttribute("kode_alamat_list",DroplistManager.getInstance().get("KODE_ALAMAT.xml","ID",request));
		request.setAttribute("kode_okupasi_list",DroplistManager.getInstance().get("KODE_OKUPASI.xml","ID",request));
		request.setAttribute("kode_obyek_sekitar_list",DroplistManager.getInstance().get("KODE_OBYEK_SEKITAR.xml","ID",request));
		request.setAttribute("carabayar_pas",DroplistManager.getInstance().get("CARABAYAR_PAS.xml","ID",request));
		request.setAttribute("autodebet_pas",DroplistManager.getInstance().get("AUTODEBET_PAS.xml","ID",request));
		
		input_type = "default";
		if(currentUser.getJn_bank() != 2 && !"01".equals(currentUser.getLde_id())){
			input_type = "typeafree";
		}
		pas.setInput_type(input_type);
		
		if("typeafree".equals(pas.getInput_type())){
			Map<String,String> produk = new HashMap<String,String>();
			produk.put("ID", "1");
			produk.put("PRODUK", "PLAN A");
			Map<String,String> produk_up = new HashMap<String,String>();
			produk_up.put("VALUE", "50000000");
			produk_up.put("LABEL", "Rp 50.000.000,-");
			Map<String,String> relasi = new HashMap<String,String>();
			relasi.put("ID", "1");
			relasi.put("RELATION", "DIRI SENDIRI");
			List<Map> produkList = new ArrayList<Map>();
			produkList.add(produk);
			List<Map> produkUpList = new ArrayList<Map>();
			produkUpList.add(produk_up);
			List<Map> relasiList = new ArrayList<Map>();
			relasiList.add(relasi);
			request.setAttribute("produk_pa_bsm",produkList);
			request.setAttribute("produk_pa_up_bsm",produkUpList);
			request.setAttribute("relasi_pas",relasiList);
			pas.setMsp_flag_cc(0);
		} else if("default".equals(pas.getInput_type())) {//kosong
			request.setAttribute("autodebet_pas", new ArrayList());
			Map<String,String> relasi = new HashMap<String,String>();
			relasi.put("ID", "1");
			relasi.put("RELATION", "DIRI SENDIRI");
			List<Map> relasiList = new ArrayList<Map>();
			relasiList.add(relasi);
			request.setAttribute("relasi_pas",relasiList);
			request.setAttribute("produk_pa_bsm", new ArrayList());
			request.setAttribute("produk_pa_up_bsm", new ArrayList());
		}else{
			//request.setAttribute("produk_pa_bsm",DroplistManager.getInstance().get("PRODUK_PA_BSM.xml","ID",request));
			//request.setAttribute("produk_pa_up_bsm",DroplistManager.getInstance().get("PRODUK_PA_UP_BSM.xml","ID",request));
			request.setAttribute("relasi_pas",DroplistManager.getInstance().get("RELATION_PAS.xml","ID",request));
		}
		
		request.setAttribute("sumber_pendanaan",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","ID",request));
		request.setAttribute("bidang_industri",DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","ID",request));
		request.setAttribute("pekerjaan",DroplistManager.getInstance().get("PEKERJAAN_PAS.xml","ID",request));
		
		
		pas.setPribadi(0);
		pas.setLscb_id(3);
		pas.setLsbp_id("156");// default bank sinarmas untuk semua produk
		//pas.setLsbp_id_autodebet("156");// default bank sinarmas untuk semua produk
		
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
		
		pas.setPanjang_no_rek(uwManager.select_panj_rek1(pas.getLsbp_id()));
		
		Date end_date = elionsManager.selectSysdate();
		end_date.setYear(end_date.getYear()+1);
		end_date.setDate(end_date.getDate()-1);
		
		pas.setMsp_pas_beg_date(elionsManager.selectSysdate());
		pas.setMsp_pas_end_date(end_date);
		
		
		return pas;
	}
	
	@Override
	protected boolean isFormChangeRequest(HttpServletRequest request) {
		boolean cek_kartu = ServletRequestUtils.getBooleanParameter(request, "cek_kartu", false);
		if(cek_kartu)
			return true;
		
		return false;
	}
	
	@Override
	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Pas pas = (Pas) command;
		String input_type = ServletRequestUtils.getStringParameter(request, "input_type", null);
		pas.setInput_type(input_type);
		validasiKartu(request, pas, errors);
		if(!errors.hasErrors()) {
			if("typeafree".equals(input_type))
				request.setAttribute(
					"msgKartu", "Produk : PAS Plan A Free" + 
					"\\nUP : 50000000\\nPremi : 0\\nCara Bayar : Free"
				);
			else {
				String produk = ((ArrayList<HashMap>) request.getAttribute("produk_pa_bsm")).get(0).get("PRODUK").toString();
				String up = ((ArrayList<HashMap>) request.getAttribute("produk_pa_up_bsm")).get(0).get("VALUE").toString();
				String premi = request.getAttribute("premi").toString();
				request.setAttribute(
					"msgKartu", 
					"Kartu dapat digunakan.\\nNo. Kartu : " + pas.getNo_kartu() + 
					"\\nProduk : " + produk + 
					"\\nUP : " + up + 
					"\\nPremi : " + premi + 
					"\\nCara Bayar : AUTODEBET TABUNGAN"
				);
			}
		}
	}
	
	private void validasiKartu(HttpServletRequest request, Pas pas, BindException errors) {
		String no_kartu = pas.getNo_kartu();
//		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "no_kartu", "", "PAS : No. Kartu harus diisi");
		
		boolean no_kartu_harus_isi = true;
		
		if(pas.getInput_type().equals("typeafree")){
			no_kartu_harus_isi = false;
		}
		
		if(!no_kartu_harus_isi){
			pas.setInput_type("typeafree");
			if(pas.getProduct_code() != null) pas.setProduct_code(null);
			Map<String,String> produk = new HashMap<String,String>();
			produk.put("ID", "1");
			produk.put("PRODUK", "PLAN A");
			Map<String,String> produk_up = new HashMap<String,String>();
			produk_up.put("VALUE", "50000000");
			produk_up.put("LABEL", "Rp 50.000.000,-");
			Map<String,String> relasi = new HashMap<String,String>();
			relasi.put("ID", "1");
			relasi.put("RELATION", "DIRI SENDIRI");
			List<Map> produkList = new ArrayList<Map>();
			produkList.add(produk);
			List<Map> produkUpList = new ArrayList<Map>();
			produkUpList.add(produk_up);
			List<Map> relasiList = new ArrayList<Map>();
			relasiList.add(relasi);
			request.setAttribute("produk_pa_bsm",produkList);
			request.setAttribute("produk_pa_up_bsm",produkUpList);
			request.setAttribute("relasi_pas",relasiList);
			pas.setMsp_flag_cc(0);
		}else{
		if(no_kartu == null || no_kartu.trim().equals(""))
			errors.reject("", "PAS : No. Kartu harus diisi");
//		else if(!"0".equals(no_kartu) && no_kartu.length() < 6)
		else if(no_kartu.length() < 6){
			errors.reject("", "PAS : Masukkan No. Kartu dengan lengkap");
//		else if("0".equals(no_kartu)) {
//			
		} else {
			HashMap<String, Object> kartu = uwManager.selectDetailKartuPas(no_kartu);
			if(kartu == null || kartu.isEmpty())
				errors.reject("", "PAS : Kartu tidak ditemukan");
			else if(!((BigDecimal)kartu.get("FLAG_ACTIVE")).equals(new BigDecimal(0)))
				errors.reject("", "PAS : Kartu sudah pernah digunakan");
			else {
				pas.setNo_kartu(kartu.get("NO_KARTU").toString());
				// Produk
				pas.setProduct_code(kartu.get("PRODUCT_CODE").toString());
				HashMap<String, Object> produk = new HashMap<String, Object>();
				produk.put("ID", kartu.get("PLAN").toString());
				produk.put("PRODUK", kartu.get("NAMA_PLAN").toString());
				ArrayList<HashMap<String, Object>> produkList = new ArrayList<HashMap<String, Object>>();
				produkList.add(produk);
				request.setAttribute("produk_pa_bsm",produkList);
				
				// Relasi
				HashMap<String, Object> relasi = new HashMap<String, Object>();
				relasi.put("ID", "1");
				relasi.put("RELATION", "DIRI SENDIRI");
				ArrayList<HashMap<String, Object>> relasiList = new ArrayList<HashMap<String, Object>>();
				relasiList.add(relasi);
				request.setAttribute("relasi_pas",relasiList);
				
				// UP
				NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMANY);// Format angka germany sama seperti indo
				HashMap<String, Object> produkUp = new HashMap<String, Object>();
				produkUp.put("VALUE", kartu.get("UP").toString());
				produkUp.put("LABEL", "Rp. " + nf.format((BigDecimal)kartu.get("UP")) + ",-");
				ArrayList<HashMap<String, Object>> produkUpList = new ArrayList<HashMap<String, Object>>();
				produkUpList.add(produkUp);
				request.setAttribute("produk_pa_up_bsm", produkUpList);
				
				// Premi
				request.setAttribute("premi", getPremi(new Double(kartu.get("UP").toString()), new Integer(kartu.get("PLAN").toString()), pas.getInput_type()));
				
				// Cara bayar
				pas.setMsp_flag_cc(2);
				HashMap<String, Object> autodebet = new HashMap<String, Object>();
				autodebet.put("ID", "2");
				autodebet.put("AUTODEBET", "TABUNGAN");
				ArrayList<HashMap<String, Object>> autodebetList = new ArrayList<HashMap<String, Object>>();
				autodebetList.add(autodebet);
				request.setAttribute("autodebet_pas", autodebetList);
				
				// No VA
				request.setAttribute("no_va", (String) kartu.get("NO_VA"));
			}
		}
		}
	}
	
	public int getPremi(Double up, int produk, String input_type){
		//perhitungan premi
		Double premi;
		
		if("typeafree".equals(input_type)){
			//FREE
			premi = new Double(0);
		}else{
			//idec_premi = (idec_up * idec_rate) / ii_permil;
			double rate = 0.00;
			if(produk == 1 || produk == 12){
				rate = 0.65;
			}else if(produk == 2 || produk == 13){
				rate = 1.45;
			}else if(produk == 3 || produk == 14){
//				rate = 3.05;
				rate = 3.00;
			}
			premi = (up * rate) / new Double(1000);
		}
		
		premi = Math.ceil(premi);
		
		return premi.intValue();
	}
	

}