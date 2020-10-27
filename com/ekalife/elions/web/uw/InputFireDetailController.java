package com.ekalife.elions.web.uw;

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
import com.ekalife.utils.CheckSum;
import com.ekalife.utils.Common;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.parent.ParentFormController;

import id.co.sinarmaslife.std.spring.util.Email;

public class InputFireDetailController extends ParentFormController{
	private long accessTime;
	protected final Log logger = LogFactory.getLog(getClass());
	private Email email;
	
	public void setEmail(Email email) {
		this.email = email;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		
		Pas pas=(Pas)cmd;
		
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
		
		//ASURANSI KEBAKARAN 
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_code_name", "","ASURANSI KEBAKARAN : Kode Nama harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_name", "","ASURANSI KEBAKARAN : Nama harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_identity", "","ASURANSI KEBAKARAN : No Identitas harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_date_of_birth2", "","ASURANSI KEBAKARAN : Tanggal Lahir harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_occupation", "","ASURANSI KEBAKARAN : Jenis Pekerjaan harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_type_business", "","ASURANSI KEBAKARAN : Bidang Usaha harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_source_fund", "","ASURANSI KEBAKARAN : Sumber Dana harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_address_1", "","ASURANSI KEBAKARAN : Alamat harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_postal_code", "","ASURANSI KEBAKARAN : Kode Pos harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_phone_number", "","ASURANSI KEBAKARAN : No Telp harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_mobile", "","ASURANSI KEBAKARAN : No HP harus diisi");
		//ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_email", "","ASURANSI KEBAKARAN : Email harus diisi");
		//---------------------------------------------------------------------------------------------------------------------------------
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_insured_addr_code", "","ASURANSI KEBAKARAN : Kode Alamat Pertanggungan harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_insured_addr", "","ASURANSI KEBAKARAN : Alamat Pertanggungan harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_insured_addr_no", "","ASURANSI KEBAKARAN : No Rumah Pertanggungan harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_insured_postal_code", "","ASURANSI KEBAKARAN : Kode Pos Pertanggungan harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_insured_city", "","ASURANSI KEBAKARAN : Kota Pertanggungan harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_insured_phone_number", "","ASURANSI KEBAKARAN : Telepon Pertanggungan harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_insured_addr_envir", "","ASURANSI KEBAKARAN : Jenis Bangunan Disekeliling Pertanggungan harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_okupasi", "","ASURANSI KEBAKARAN : Penggunaan Bangunan harus diisi");
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
		
		if(pas.getReg_spaj() == null)pas.setReg_spaj("");
		
		pas.setLus_id(lus_id);
		pas.setLus_login_name(user.getLus_full_name());
//		pas.setLspd_id(1);
//		pas.setLssp_id(10);
		pas.setMsp_kode_sts_sms("00");
		pas = uwManager.updatePas(pas);//request, pas, errors,"input",user,errors);
		//uwManager.updatePas1(pas);//request, pas, errors,"input",user,errors);
		request.setAttribute("successMessage","edit sukses. Fire Id : "+pas.getMsp_fire_id());
		
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
        "uw/input_fire_detail").addObject("cmd",pas);

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
		
		List<Pas> pasList = uwManager.selectAllPasList(msp_id, null, null, null, null, null, null, null,null,null,null);
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
		
		if(pas.getPin() != null){
			String lsdbs_number = uwManager.selectCekPin(pas.getPin(), 1);
			if(lsdbs_number == null)lsdbs_number = "x";
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
		
		if(pas.getMsp_fire_beg_date() == null){
			Date end_date = elionsManager.selectSysdate();
			end_date.setYear(end_date.getYear()+1);
			end_date.setDate(end_date.getDate()-1);
			
			pas.setMsp_fire_beg_date(elionsManager.selectSysdate());
			pas.setMsp_fire_end_date(end_date);
			
		}
		
		return pas;
	}
	
}