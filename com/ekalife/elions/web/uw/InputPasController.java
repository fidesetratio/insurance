package com.ekalife.elions.web.uw;

import java.text.SimpleDateFormat;
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

public class InputPasController extends ParentFormController{
	private long accessTime;
	protected final Log logger = LogFactory.getLog(getClass());
	private Email email;
	
	public void setEmail(Email email) {
		this.email = email;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		
		Pas pas=(Pas)cmd;
		User user = (User) request.getSession().getAttribute("currentUser");
		
		//user.setLca_id("58");
		
		form_agen d_agen= new form_agen(); 
		
		String tambah_input_asuransi = request.getParameter("tambah_input_asuransi");
		
		Date nowDate = elionsManager.selectSysdate();
		
		if(pas.getMsp_pas_beg_date() == null){
			nowDate = elionsManager.selectSysdate();
		}else{
			nowDate = pas.getMsp_pas_beg_date();
		}
		
		if(!"58".equals(user.getLca_id())){
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
		if(!"58".equals(user.getLca_id())){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_tmp_lhr_pp", "","PAS : Tempat Lahir Pemegang harus diisi");
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_dob_pp", "","PAS : Tanggal Lahir Pemegang harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_full_name", "","PAS : Nama Tertanggung harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_email", "","PAS : Alamat Email harus diisi");
		if(!"58".equals(user.getLca_id())){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_identity_no", "","PAS : No. Identitas harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_tmp_lhr_tt", "","PAS : Tempat Lahir Tertanggung harus diisi");
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_date_of_birth", "","PAS : Tgl. Lahir Tertanggung harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_postal_code", "","PAS : Kode Pos harus diisi");
		if(!"58".equals(user.getLca_id()) && pas.getNew_card() != 1){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pin", "","PAS : PIN harus diisi");
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "no_kartu", "","PAS : No. Kartu harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_address_1", "","PAS : Alamat harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_phone_number", "","PAS : No. Telepon harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_mobile", "","PAS : No. HP harus diisi");
		if(!"58".equals(user.getLca_id())){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_identity_no_tt", "","PAS : No. Identitas harus diisi");
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_city", "","PAS : Kota harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "lscb_id", "","PAS : Pembayaran harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_flag_cc", "","PAS : Cara Bayar harus diisi");
		
		Integer umurPp = 0;
		Integer umurTt = 0;
		Integer umurBlnTt = 0;
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
				
				if(umurTt < 1){
					f_hit_umur a = new f_hit_umur();
					umurBlnTt = a.bulan(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
				}
			}
		}catch(Exception e){
			
		}
		
		if(!(umurPp>=17&&umurPp<=85)){ //batas umur 17 s/d 85 tahun
			err.reject("","PAS : Umur pemegang minimal 17 tahun & maksimal 85 tahun");
		}
		if(!(umurTt>=1&&umurTt<=80)){ //batas 6 bulan s/d 80 tahun
			//cek bulan
			if(umurTt < 1){
				if(umurBlnTt < 6){
					err.reject("","PAS : Umur tertanggung minimal 6 bulan & maksimal 80 tahun");
				}
			}else{
				err.reject("","PAS : Umur tertanggung minimal 6 bulan & maksimal 80 tahun");
			}
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
		if(!"58".equals(user.getLca_id())){
			
			if(pas.getPin() != null){
				//CheckSum checkSum = new CheckSum();
				lsdbs_number = uwManager.selectCekPin(pas.getPin(), 0);
				if(lsdbs_number == null)lsdbs_number = "x";
			}
		}
//		if("x".equals(lsdbs_number)){
//			err.reject("","PAS : Produk tidak ditemukan. Mohon cek PIN");
//		}
		
		String lsdbs_number_no_kartu = "x";
		if(pas.getNo_kartu() != null){
			//CheckSum checkSum = new CheckSum();
			lsdbs_number_no_kartu = uwManager.selectCekNoKartu(pas.getNo_kartu(), "PAS", 0);
			// Check kartu baru (1 kartu bisa buat semua paket)
			if(lsdbs_number_no_kartu == null) {
			    lsdbs_number_no_kartu = uwManager.selectCekNoKartu(pas.getNo_kartu(), "CARD", 0);
			}
			if(lsdbs_number_no_kartu == null)lsdbs_number_no_kartu = "x";
		}
		if("58".equals(user.getLca_id())){
			if("x".equals(lsdbs_number_no_kartu)){
				err.reject("","PAS : Produk tidak ditemukan. Mohon cek No. Kartu");
			}else{
				pas.setProduk(Integer.parseInt(lsdbs_number_no_kartu));
			}
		}else{
			if(("x".equals(lsdbs_number) || (!lsdbs_number.equals(lsdbs_number_no_kartu) && !lsdbs_number.equals("05"))) && pas.getNew_card() != 1){
				err.reject("","PAS : Produk tidak ditemukan. Mohon cek No. Kartu & PIN");
			}else if(pas.getNew_card() != 1){
			    pas.setProduk(Integer.parseInt(lsdbs_number_no_kartu));
			}
		}
		
		//AGEN
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msag_id", "","AGEN : Kode Agen harus diisi");
		
		//REKENING
		// mall : inputan rekening boleh kosong untuk sementara
		if(!"58".equals(user.getLca_id())){
			//REKENING
			if(!"".equals(pas.getLsbp_id()) && !"0".equals(pas.getLsbp_id())){
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "lsbp_id", "","REKENING : Bank harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_no_rekening", "","REKENING : No. Rekening harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_rek_cabang", "","REKENING : Cabang harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_rek_kota", "","REKENING : Kota harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_rek_nama", "","REKENING : Atas Nama harus diisi");
			}
			if(pas.getMsp_flag_cc() != null && (pas.getMsp_flag_cc() == 2 || pas.getMsp_flag_cc() == 1)){//tabungan/kartu kredit
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "lsbp_id_autodebet", "","REKENING (AUTODEBET) : Bank harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_no_rekening_autodebet", "","REKENING (AUTODEBET) : No. Rekening harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_tgl_debet", "","REKENING (AUTODEBET) : Tanggal debet harus diisi");
				if(pas.getMsp_flag_cc() == 1){
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_tgl_valid", "","REKENING (AUTODEBET) : Tanggal valid harus diisi");
				}
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_rek_nama_autodebet", "","REKENING (AUTODEBET) : Atas Nama harus diisi");
			}
		}
		
		if(!"58".equals(user.getLca_id())){
		if("1".equals(tambah_input_asuransi)){
			//ASURANSI KEBAKARAN 
			if(pas.getMsp_fire_insured_addr_envir() != 5){
				pas.setMsp_fire_ins_addr_envir_else("");
			}else{
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_ins_addr_envir_else", "","Lainnya harus diisi");
			}
			
			if(!"L".equals(pas.getMsp_fire_okupasi())){
				pas.setMsp_fire_okupasi_else("");
			}else{
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_okupasi_else", "","Lainnya harus diisi");
			}
			
			if(!"LAINNYA".equals(pas.getMsp_fire_source_fund2())){
				pas.setMsp_fire_source_fund(pas.getMsp_fire_source_fund2());
			}
			
			if(!"LAINNYA".equals(pas.getMsp_fire_type_business2())){
				pas.setMsp_fire_type_business(pas.getMsp_fire_type_business2());
			}
			
			if(!"LAIN-LAIN".equals(pas.getMsp_fire_occupation2())){
				pas.setMsp_fire_occupation(pas.getMsp_fire_occupation2());
			}
			
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_code_name", "","Kode Nama harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_name", "","Nama harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_identity", "","No Identitas harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_date_of_birth2", "","Tanggal Lahir harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_occupation", "","Jenis Pekerjaan harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_type_business", "","Bidang Usaha harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_source_fund", "","Sumber Dana harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_address_1", "","Alamat harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_postal_code", "","Kode Pos harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_phone_number", "","No Telp harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_mobile", "","No HP harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_email", "","Email harus diisi");
			//---------------------------------------------------------------------------------------------------------------------------------
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_insured_addr_code", "","Kode Alamat Pertanggungan harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_insured_addr", "","Alamat Pertanggungan harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_insured_addr_no", "","No Rumah Pertanggungan harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_insured_postal_code", "","Kode Pos Pertanggungan harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_insured_city", "","Kota Pertanggungan harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_insured_phone_number", "","Telepon Pertanggungan harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_insured_addr_envir", "","Jenis Bangunan Disekeliling Pertanggungan harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_okupasi", "","Penggunaan Bangunan harus diisi");
			
			if(!Common.isEmpty(pas.getMsp_fire_email())){
				try {
					InternetAddress.parse(pas.getMsp_fire_email().trim());
				} catch (AddressException e) {
					err.reject("","ASURANSI KEBAKARAN : email tidak valid");
				} finally {
					if(!pas.getMsp_fire_email().trim().toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
						err.reject("","ASURANSI KEBAKARAN : email tidak valid");
					}
				}
			}
			if(!Common.isEmpty(pas.getMsp_fire_phone_number())){
				if(!Common.validPhone(pas.getMsp_fire_phone_number().trim())){
					err.reject("","ASURANSI KEBAKARAN : No. Telepon harus diisi angka");
				}
			}
			if(!Common.isEmpty(pas.getMsp_fire_insured_phone_number())){
				if(!Common.validPhone(pas.getMsp_fire_insured_phone_number().trim())){
					err.reject("","ASURANSI KEBAKARAN : No. Telepon Pertanggungan harus diisi angka");
				}
			}
			try{
				String x = pas.getMsp_fire_phone_number().trim().replace("0", "").replace("-", "");
				if(Common.isEmpty(x)){
					err.reject("","ASURANSI KEBAKARAN : No. Telepon tidak boleh diisi '0' atau '-'");
				}
			}catch(Exception e){
				err.reject("","ASURANSI KEBAKARAN : No. Telepon tidak boleh diisi '0' atau '-'");
			}
			try{
				String x = pas.getMsp_fire_insured_phone_number().trim().replace("0", "").replace("-", "");
				if(Common.isEmpty(x)){
					err.reject("","ASURANSI KEBAKARAN : No. Telepon Pertanggungan tidak boleh diisi '0' atau '-'");
				}
			}catch(Exception e){
				err.reject("","ASURANSI KEBAKARAN : No. Telepon Pertanggungan tidak boleh diisi '0' atau '-'");
			}
			try{
				String x = pas.getMsp_fire_insured_addr_no().replaceAll("[a-z]", "").replaceAll("[A-Z]", "");
				if(Common.isEmpty(x)){
					err.reject("","ASURANSI KEBAKARAN : No Rumah Pertanggungan tidak boleh diisi huruf saja");
				}
			}catch(Exception e){
				err.reject("","ASURANSI KEBAKARAN : No Rumah Pertanggungan tidak boleh diisi huruf saja");
			}
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
		
		//request.setAttribute("addError", "addError");
				//err.reject("");
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
		Integer flagNew = 1;
		map.put("user_id", lus_id);
		//user.setLca_id("58");
		String tambah_input_asuransi = request.getParameter("tambah_input_asuransi");
		if("58".equals(user.getLca_id())){
			tambah_input_asuransi = "0";// untuk mallinsurance tidak ada input fire sekaligus
		}
		Cmdeditbac edit = new Cmdeditbac();
		
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
		
		
		if(pas.getReg_spaj() == null)pas.setReg_spaj("");
		if(pas.getPribadi() == null)pas.setPribadi(0);
		
		if(pas.getNew_card() == 1) {
		    pas.setMsp_premi(pas.getPremi());
		}
		
		if("insert".equals(request.getParameter("kata"))){
			if("0".equals(tambah_input_asuransi)){
				pas.setMsp_fire_code_name("");
				pas.setMsp_fire_name("");
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
			}else{
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date begDate = (Date) pas.getMsp_pas_beg_date().clone();
				Date endDate = (Date) pas.getMsp_pas_beg_date().clone();
				endDate.setMonth(endDate.getMonth() + 6);
				endDate.setDate(endDate.getDate() - 1);
				pas.setMsp_fire_beg_date(begDate);
				pas.setMsp_fire_end_date(endDate);
			}
			pas.setLus_id(lus_id);
			pas.setLus_login_name(user.getLus_full_name());
			//menentukan jenis pas (individu, dmtm, mallinsurance, ap/bp)
			if(pas.getMsag_id_pp() != null){
				pas.setJenis_pas("AP/BP");
			}else if("58".equals(user.getLca_id())){
				pas.setJenis_pas("MALLINSURANCE");
			}else{
				pas.setJenis_pas("INDIVIDU");
			}
			pas.setMsp_flag_pas(1);
			Date endDatePas = (Date) pas.getMsp_pas_beg_date().clone();
			endDatePas.setYear(endDatePas.getYear()+1);
			endDatePas.setDate(endDatePas.getDate()-1);
			pas.setMsp_pas_end_date(endDatePas);
			//=================================
			pas = uwManager.insertPas(pas, user);
			if(pas.getStatus() == 1){
				request.setAttribute("successMessage","proses insert gagal");
			}else{
				Integer msp_id = uwManager.selectGetPasIdFromFireId(pas.getMsp_fire_id());
				request.setAttribute("msp_id",msp_id);
				request.setAttribute("successMessage","insert sukses. Fire Id : "+pas.getMsp_fire_id());
			}
			
//			pas.setLus_id(lus_id);
//			pas.setLus_login_name(user.getLus_full_name());
//			edit=this.uwManager.prosesPas(request, "insert", pas, errors,"input",user,errors);
//			request.setAttribute("successMessage","insert sukses");
		}else if("update".equals(request.getParameter("kata"))){
			pas.setLus_id(lus_id);
			pas.setLus_login_name(user.getLus_full_name());
			pas.setLspd_id(1);
			pas.setLssp_id(10);//POLICY IS BEING PROCESSED 
			pas.setMsp_kode_sts_sms("00");
			Date endDatePas = (Date) pas.getMsp_pas_beg_date().clone();
			endDatePas.setYear(endDatePas.getYear()+1);
			endDatePas.setDate(endDatePas.getDate()-1);
			pas.setMsp_pas_end_date(endDatePas);
			pas = uwManager.updatePas(pas);//request, pas, errors,"input",user,errors);
			if(pas.getStatus() == 1){
				request.setAttribute("successMessage","proses edit gagal");
			}else{
				request.setAttribute("successMessage","edit sukses");
			}
			
		}
		
//		schedulerPas();
		
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
        "uw/input_pas").addObject("cmd",pas);

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
		Map<String, Object> refData = new HashMap<String, Object>();
		//currentUser.setLca_id("58");
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
		
		List sumberPendanaanList = DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","ID",request);
		List bidangIndustriList = DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","ID",request);
		List pekerjaanList = DroplistManager.getInstance().get("PEKERJAAN_PAS.xml","ID",request);
		
		pas.setPribadi(0);
		
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
		
		Date end_date = elionsManager.selectSysdate();
		end_date.setYear(end_date.getYear()+1);
		end_date.setDate(end_date.getDate()-1);
		
		pas.setMsp_pas_beg_date(elionsManager.selectSysdate());
		pas.setMsp_pas_end_date(end_date);
		
		if("58".equals(currentUser.getLca_id())){
		if(currentUser.getMall_nama_pp() != null){
			String kdArea = currentUser.getMall_kd_area_telp();
			if(kdArea == null)kdArea = "";
			if(kdArea.equals("null"))kdArea = "";
			pas.setMsp_pas_email(currentUser.getMall_email());
			pas.setMsag_id(currentUser.getMall_msag_id());
			pas.setMsp_mobile(currentUser.getMall_hp());
			pas.setMsp_pas_nama_pp(currentUser.getMall_nama_pp());
			pas.setMsp_pas_phone_number(kdArea + currentUser.getMall_telp());
			pas.setMsp_pas_dob_pp(currentUser.getMall_tgl_lhr_pp());
			pas.setMspo_plan_provider(currentUser.getMall_mspo_plan_provider());
			try{
			pas.setLrb_id(Integer.parseInt(currentUser.getMall_lrb_id().trim()));
			}catch(Exception e){
				logger.info(e);
			}
			
		}
		}else{
			// khusus pas yg biasa
			pas.setMsp_admin_date(pas.getMsp_pas_beg_date());
		}
		
		return pas;
	}
	

}