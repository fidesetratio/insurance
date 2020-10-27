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

public class InputPasMallDetailController extends ParentFormController{
	private long accessTime;
	protected final Log logger = LogFactory.getLog(getClass());
	private Email email;
	
	public void setEmail(Email email) {
		this.email = email;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		
		Pas pas=(Pas)cmd;
		
		String tambah_input_asuransi = request.getParameter("tambah_input_asuransi");
		User user = (User) request.getSession().getAttribute("currentUser");
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
		
//		if(!"".equals(agen_hsl)){
//			err.reject("","Agen : Kode Agen tidak berlaku");
//		}
		
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
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "no_kartu", "","PAS : No. Kartu harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_address_1", "","PAS : Alamat harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_phone_number", "","PAS : No. Telepon harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_mobile", "","PAS : No. HP harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_identity_no", "","PAS : No. Identitas harus diisi");
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
		
		if(!(umurPp>=17 && umurPp<=85)){ //batas umur 17 s/d 85 tahun
			err.reject("","PAS : Umur pemegang minimal 17 tahun & maksimal 85 tahun");
		}
		if(!(umurTt>=1 && umurTt<=80)){ //batas 6 bulan s/d 80 tahun
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
//		if("x".equals(lsdbs_number)){
//			err.reject("","PAS : Produk tidak ditemukan. Mohon cek PIN");
//		}
		
		String lsdbs_number_no_kartu = "x";
		if(pas.getNo_kartu() != null){
			//CheckSum checkSum = new CheckSum();
			lsdbs_number_no_kartu = uwManager.selectCekNoKartu(pas.getNo_kartu(), "PAS", 1);
			if(lsdbs_number_no_kartu == null)lsdbs_number_no_kartu = "x";
		}
			if("x".equals(lsdbs_number_no_kartu)){
				err.reject("","PAS : Produk tidak ditemukan. Mohon cek No. Kartu");
			}
		
		//AGEN
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msag_id", "","AGEN : Kode Agen harus diisi");
		
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
		
		Cmdeditbac edit = new Cmdeditbac();
		
		String tambah_input_asuransi = request.getParameter("tambah_input_asuransi");
			tambah_input_asuransi = "0";
		
		// menentukan pas save langsung muncul konfirmasi transfer ke uw pada pas_detail
		String flag_posisi=ServletRequestUtils.getStringParameter(request, "flag_posisi",null);
		request.setAttribute("flag_posisi", flag_posisi);
		
		if(pas.getReg_spaj() == null)pas.setReg_spaj("");
		if(pas.getPribadi() == null)pas.setPribadi(0);
		try{
			if("0".equals(tambah_input_asuransi)){
				if(Common.isEmpty(pas.getMsp_fire_id())){
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
			//uwManager.updatePas1(pas);//request, pas, errors,"input",user,errors);
			request.setAttribute("successMessage","edit sukses. Fire Id : "+pas.getMsp_fire_id());
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
        "uw/input_pas_mall_detail").addObject("cmd",pas);

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
		
		List<Pas> pasList = uwManager.selectAllPasList(msp_id, null, null, null, null, null, null, "mall",null,null,null);
		pas = pasList.get(0);
		
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
		
//		if(!"58".equals(currentUser.getLca_id())){
//			if(pas.getPin() != null){
//				String lsdbs_number = uwManager.selectCekPin(pas.getPin(), 1);
//				//if(lsdbs_number == null)lsdbs_number = "x";
//				pas.setProduk(Integer.parseInt(lsdbs_number));
//			}
//		}else if("58".equals(currentUser.getLca_id())){
//			if(pas.getNo_kartu() != null){
//				String lsdbs_number = uwManager.selectCekNoKartu(pas.getNo_kartu(), 1);
//				//if(lsdbs_number == null)lsdbs_number = "x";
//				pas.setProduk(Integer.parseInt(lsdbs_number));
//			}
//		}
		
		if(pas.getPin() != null){
			String lsdbs_number = uwManager.selectCekPin(pas.getPin(), 1);
			//if(lsdbs_number == null)lsdbs_number = "x";
			pas.setProduk(Integer.parseInt(lsdbs_number));
		}else{
			String lsdbs_number = uwManager.selectCekNoKartu(pas.getNo_kartu(), "PAS", 1);
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