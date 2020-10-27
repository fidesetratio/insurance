package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.spring.util.Email;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.NumberFormat;
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
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.BindUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;


import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.PaTmmsFree;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Reas;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.Tmms;
import com.ekalife.elions.model.TmmsBill;
import com.ekalife.elions.model.TmmsDBill;
import com.ekalife.elions.model.TmmsDet;
import com.ekalife.elions.model.Transfer;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.bac.support.form_agen;
import com.ekalife.utils.Common;
import com.ekalife.utils.CheckSum;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentFormController;

public class PaFreeInputController extends ParentFormController{
	private long accessTime;
	protected final Log logger = LogFactory.getLog(getClass());
	private Email email;
	
	public void setEmail(Email email) {
		this.email = email;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		
		PaTmmsFree paTmmsFree=(PaTmmsFree)cmd;
		
		Tmms tmms = paTmmsFree.getTmms();
		TmmsDet tmmsDet = paTmmsFree.getTmmsDet();
		//TmmsBill tmmsBill = paTmmsFree.getTmmsBill();
		//TmmsDBill tmmsDBill = paTmmsFree.getTmmsDBill();
		
		//TMMS
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "tmms.holder_name", "","Nama Pemegang harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "tmms.sex", "","Jenis Kelamin harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "tmms.bod_tempat", "","Tempat lahir harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "tmms.bod_holder", "","Tanggal Lahir harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "tmms.no_identitas", "","No KTP harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "tmms.status", "","Status harus diisi");
//		ValidationUtils.rejectIfEmptyOrWhitespace(err, "tmms.email", "","Alamat Email harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "tmms.address1", "","Alamat harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "tmms.city", "","Kota harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "tmms.postal_code", "","Kode Pos harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "tmms.home_phone", "","No Telepon harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "tmms.mobile_no", "","No HP harus diisi");
		
		Integer umur = 0;
		try{
			f_hit_umur umr = new f_hit_umur();
			
			SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
			SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
			SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
			
			if(paTmmsFree.getTmms().getBod_holder() != null){
				Date nowDate = elionsManager.selectSysdate();
				int tahun1 = Integer.parseInt(sdfYear.format(paTmmsFree.getTmms().getBod_holder()));
				int bulan1 = Integer.parseInt(sdfMonth.format(paTmmsFree.getTmms().getBod_holder()));
				int tanggal1 = Integer.parseInt(sdfDay.format(paTmmsFree.getTmms().getBod_holder()));
				int tahun2 = Integer.parseInt(sdfYear.format(nowDate));
				int bulan2 = Integer.parseInt(sdfMonth.format(nowDate));
				int tanggal2 = Integer.parseInt(sdfDay.format(nowDate));
			umur=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
		}
		}catch(Exception e){
			
		}
		
		if(!(umur>=17 && umur<=59)){ //batas umur 17 s/d 59 tahun
			err.reject("","PAS : Umur pemegang minimal 17 tahun & maksimal 59 tahun");
		}
		
		//tmmsDet.getId();
		//tmmsDet.getUrut();
		//tmmsDet.getNama_peserta();
		//tmmsDet.getSex();
		//tmmsDet.getBod_peserta();
		//tmmsDet.getUsia();
		//tmmsDet.getRelasi();
		//tmmsDet.getProduct_code();
		//tmmsDet.getPlan();
		
		//tmmsBill.getId();
		//tmmsBill.getPremi_ke();
		//tmmsBill.getTahun_ke();
		//tmmsBill.getBeg_date();
		//tmmsBill.getEnd_date();
		//tmmsBill.getJumlah_premi();
		//tmmsBill.getJumlah_disc();
		//tmmsBill.getFlag_paid();
		//tmmsBill.getPosisi();
		//tmmsBill.getTgl_input();
		//tmmsBill.getJumlah_komisi();
		//tmmsBill.getBill_mode();
		//tmmsBill.getJumlah_bayar();
		//tmmsBill.getJumlah_bayar2();
		//tmmsBill.getFlag_aktif();
		//tmmsBill.getFlag_recur();
		
		if(!Common.isEmpty(tmms.getEmail())){
			try {
				InternetAddress.parse(tmms.getEmail().trim());
			} catch (AddressException e) {
				err.reject("","PAS : email tidak valid");
			} finally {
				if(!tmms.getEmail().trim().toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
					err.reject("","PAS : email tidak valid");
				}
			}
		}
		try{
			int x = Integer.parseInt( tmms.getPostal_code().trim());
		}catch(Exception e){
			err.reject("","PAS : Kode Pos harus diisi angka");
		}
		if(!Common.isEmpty(tmms.getHome_phone())){
			if(!Common.validPhone(tmms.getHome_phone().trim())){
				err.reject("","PAS : No. Telepon harus diisi angka");
			}
		}
		if(!Common.isEmpty(tmms.getHome_phone())){
			try{
				String x = tmms.getHome_phone().trim().replace("0", "").replace("-", "");
				if(Common.isEmpty(x)){
					err.reject("","PAS : No. Telepon tidak boleh diisi '0' atau '-'");
				}
			}catch(Exception e){
				err.reject("","PAS : No. Telepon tidak boleh diisi '0' atau '-'");
			}
		}
		
	}
	
	protected ModelAndView onSubmit( HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors ) throws Exception
    {

		PaTmmsFree paTmmsFree=(PaTmmsFree)cmd;
		//User currentUser = (User) session.getAttribute("currentUser");
		Map<String, Object> map = new HashMap<String, Object>();
		
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		Integer flagNew = 1;
		map.put("user_id", lus_id);
		
//		if(paTmmsFree.getReg_spaj() == null)paTmmsFree.setReg_spaj("");
//		if(paTmmsFree.getPribadi() == null)paTmmsFree.setPribadi(0);
//		
//		if("insert".equals(request.getParameter("kata"))){
//			paTmmsFree.setLus_id(lus_id);
//			paTmmsFree.setLus_login_name(user.getLus_full_name());
//			paTmmsFree = uwManager.insertPaTmmsFree(paTmmsFree);
//			if(paTmmsFree.getStatus() == 1){
//				request.setAttribute("successMessage","proses insert gagal");
//			}else{
//				Integer msp_id = uwManager.selectGetPaTmmsFreeIdFromFireId(paTmmsFree.getMsp_fire_id());
//				request.setAttribute("msp_id",msp_id);
//				request.setAttribute("successMessage","insert sukses. Fire Id : "+paTmmsFree.getMsp_fire_id());
//			}
//			
////			paTmmsFree.setLus_id(lus_id);
////			paTmmsFree.setLus_login_name(user.getLus_full_name());
		paTmmsFree = uwManager.insertPaTmmsFree(paTmmsFree);
		if(paTmmsFree.getTmms().getId() == null){
			request.setAttribute("successMessage","insert gagal");
		}else{
			request.setAttribute("tmms_id",paTmmsFree.getTmms().getId());
			request.setAttribute("successMessage","insert sukses");
		}
////			request.setAttribute("successMessage","insert sukses");
//		}else if("update".equals(request.getParameter("kata"))){
//			paTmmsFree.setLus_id(lus_id);
//			paTmmsFree.setLus_login_name(user.getLus_full_name());
//			paTmmsFree.setLspd_id(1);
//			paTmmsFree.setLssp_id(10);
//			paTmmsFree.setMsp_kode_sts_sms("00");
//			paTmmsFree = uwManager.updatePaTmmsFree(paTmmsFree);//request, paTmmsFree, errors,"input",user,errors);
//			if(paTmmsFree.getStatus() == 1){
//				request.setAttribute("successMessage","proses edit gagal");
//			}else{
//				request.setAttribute("successMessage","edit sukses");
//			}
//			
//		}
		
		return new ModelAndView(
        "uw/pa_free_input").addObject("cmd",paTmmsFree);

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
		PaTmmsFree paTmmsFree=new PaTmmsFree();
		String msp_id=ServletRequestUtils.getStringParameter(request, "msp_id",null);
		Map<String, Object> refData = new HashMap<String, Object>();
		
		paTmmsFree.setTmms(new Tmms());
		paTmmsFree.setTmmsBill(new TmmsBill());
		paTmmsFree.setTmmsDBill(new TmmsDBill());
		paTmmsFree.setTmmsDet(new TmmsDet());
		
		if(currentUser.getMall_nama_pp() != null){
			String kdArea = currentUser.getMall_kd_area_telp();
			if(kdArea == null)kdArea = "";
			if(kdArea.equals("null"))kdArea = "";
			paTmmsFree.getTmms().setEmail(currentUser.getMall_email());
			paTmmsFree.getTmms().setKd_agent(currentUser.getMall_msag_id());
			paTmmsFree.getTmms().setMobile_no(currentUser.getMall_hp());
			paTmmsFree.getTmms().setHolder_name(currentUser.getMall_nama_pp());
			paTmmsFree.getTmms().setHome_phone(kdArea + currentUser.getMall_telp());
			paTmmsFree.getTmms().setBod_holder(currentUser.getMall_tgl_lhr_pp());
			paTmmsFree.getTmms().setMspo_plan_provider(currentUser.getMall_mspo_plan_provider());
		}
		
		return paTmmsFree;
	}

}