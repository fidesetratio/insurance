package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.spring.util.Email;

import java.io.File;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.BindUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;


import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.AddressNew;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Akseptasi;
import com.ekalife.elions.model.BlackList;
import com.ekalife.elions.model.BlackListFamily;
import com.ekalife.elions.model.Client;
import com.ekalife.elions.model.CmdInputBlacklist;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DetBlackList;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Reas;
import com.ekalife.elions.model.ReffBii;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.utils.CheckSum;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentFormController;

public class AkseptasiFireController extends ParentFormController{
	private long accessTime;
	protected final Log logger = LogFactory.getLog(getClass());
	private Email email;
	
	public void setEmail(Email email) {
		this.email = email;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		
		//Pas pas = (Pas) cmd;
//		String mclId = request.getParameter("kopiMclId");
//		String baru = request.getParameter("baru");
//		String addnewrow = request.getParameter("addnewrow");
//		String addnewalias = request.getParameter("addnewalias");
//		String addnewidentitas = request.getParameter("addnewidentitas");
//		String delIndex = request.getParameter("delIndex");
//		
//		if(delIndex == null)delIndex="";
//		
////		if(mclId!="") {
////			//kalau pakai tombol cari
////			request.setAttribute("flagSave", "save");
////			request.setAttribute("kopiMclId", mclId);
////		}
//		
//				if(detiledit.getBlacklistfamily().getLblf_nama_anak3() != ""){
//					ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklistfamily.lblf_tgllhr_anak3", "","tgl lahir anak 3 harus diisi");
//				}
//				//==================================================
//				//ValidationUtils.rejectIfEmptyOrWhitespace(err, "client.mkl_kerja", "","pekerjaan harus diisi");
//				ValidationUtils.rejectIfEmptyOrWhitespace(err, "addressNew.alamat_rumah", "","alamat rumah harus diisi");
//				ValidationUtils.rejectIfEmptyOrWhitespace(err, "addressNew.area_code_rumah", "","code area telepon rumah harus diisi");
//				ValidationUtils.rejectIfEmptyOrWhitespace(err, "addressNew.telpon_rumah", "","telepon rumah harus diisi");
//				//ValidationUtils.rejectIfEmptyOrWhitespace(err, "addressNew.alamat_kantor", "","alamat kantor harus diisi");
//				//ValidationUtils.rejectIfEmptyOrWhitespace(err, "addressNew.telpon_kantor", "","telepon kantor harus diisi");
//				ValidationUtils.rejectIfEmptyOrWhitespace(err, "addressNew.no_hp", "","no.hp harus diisi");
//				//ValidationUtils.rejectIfEmptyOrWhitespace(err, "client.mspe_email", "","email harus diisi");
//				//ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklist.lbl_tgl_kejadian", "","tgl.kejadian/rawat harus diisi");
//				//ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklist.lbl_diagnosa", "","diagnosa harus diisi");
//				ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklist.lbl_alasan", "","keterangan/alasan harus diisi");
//			}
//		}
//		//if(("".equals(detiledit.getClient().getMcl_id()) && "SAVE".equals(request.getParameter("save"))) || (!"".equals(detiledit.getClient().getMcl_id()) && detiledit.getClient().getMcl_blacklist() == 0 && "SAVE".equals(request.getParameter("save")))){
//		
	}
	
	protected ModelAndView onSubmit( HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors ) throws Exception
    {
		//Pas pas=(Pas)cmd;
		
		List lsJnsId=elionsManager.selectAllLstIdentity();
		Map<String, Object> map = new HashMap<String, Object>();
		
		User user = (User) request.getSession().getAttribute("currentUser");
		String msp_id = request.getParameter("msp_id");
		String action = request.getParameter("action");
		String alasan = request.getParameter("isi_alasan");
		String jenisDist = request.getParameter("jenisDist");
		
		Integer lus_id = Integer.parseInt(user.getLus_id());
		Integer flagNew = 1;
		map.put("user_id", lus_id);
		List<Pas> pas = new ArrayList<Pas>();
		Pas p = null;
		if(! "reject all".equals(action)){
			pas = uwManager.selectAllPasList(msp_id, "2", null, null, null, "fire", jenisDist, null,null,null,null);
			p = pas.get(0);
		}
		
		request.setAttribute("jenisDist", jenisDist);
		
		if("aksep".equals(action)){
			//p.setLus_id(lus_id);
			p.setLus_id_uw_fire(lus_id);
			p.setLus_login_name(user.getLus_full_name());
			//p.setMsp_pas_accept_date(elionsManager.selectSysdate());
			p.setMsp_fire_accept_date(elionsManager.selectSysdate());
			//p.setMsp_flag_aksep(1);
			//p.setLspd_id(2);
			//p.setMsp_fire_export_flag(0);
			p.setMsp_fire_export_flag(0);
			//p.setLssp_id(10);
			//p.setlssa_id(5);
			p.setMsp_kode_sts_sms("00");
			p = uwManager.updatePas(p, action);//request, pas, errors,"input",user,errors);
			if(p.getStatus() == 1){
				request.setAttribute("successMessage","proses aksep gagal");
			}else{
				request.setAttribute("successMessage","aksep sukses");
			}
//		}else if("transfer".equals(action)){
//			Cmdeditbac edit = new Cmdeditbac();
//			p.setLus_id(lus_id);
//			p.setLus_login_name(user.getLus_full_name());
//			p.setLspd_id(99);
//			edit=this.uwManager.prosesPas(request, "update", p, errors,"input",user,errors);
//			request.setAttribute("successMessage","transfer sukses");
			//hasil=this.elionsManager.prosesTransferPembayaran(transfer,flagNew,errors);
		}else if("resend".equals(action)){
			Cmdeditbac edit = new Cmdeditbac();
			//p.setLus_id(lus_id);
			p.setLus_id_uw_fire(lus_id);
			p.setLus_login_name(user.getLus_full_name());
			p.setMsp_fire_export_flag(2);
			p = uwManager.updatePas(p, action);
			if(p.getStatus() == 1){
				request.setAttribute("successMessage","proses resend gagal");
			}else{
				request.setAttribute("successMessage","resend sukses");
			}
			//hasil=this.elionsManager.prosesTransferPembayaran(transfer,flagNew,errors);
		}else if("reject".equals(action)){
			Cmdeditbac edit = new Cmdeditbac();
			//p.setLus_id(lus_id);
			p.setLus_id_uw_fire(lus_id);
			p.setLus_login_name(user.getLus_full_name());
			p.setMsp_fire_export_flag(6);
			p.setMsp_fire_fail_desc(alasan);
			p.setMsp_fire_fail_date(elionsManager.selectSysdate());
			p = uwManager.updatePas(p, action);
			if(p.getStatus() == 1){
				request.setAttribute("successMessage","proses reject gagal");
			}else{
				request.setAttribute("successMessage","reject sukses");
			}
			//hasil=this.elionsManager.prosesTransferPembayaran(transfer,flagNew,errors);
		}else if("reject all".equals(action)){
			String ckra[] = request.getParameterValues("ckra");
			int r  = uwManager.rejectSelectedFire(ckra, lus_id, user.getLus_full_name(), alasan, jenisDist, elionsManager.selectSysdate());
			if(r == 1){
				request.setAttribute("successMessage","proses reject all gagal");
			}else{
				request.setAttribute("successMessage","reject all sukses");
			}
			//hasil=this.elionsManager.prosesTransferPembayaran(transfer,flagNew,errors);
		}
			
		pas = uwManager.selectAllPasList(null, "2", null, null, null, "fire", null, null,null,null,null);
		
		return new ModelAndView(
        "uw/akseptasi_fire").addObject("cmd",pas);

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
		//Map<String, Object> map = new HashMap<String, Object>();
		
		String tipe = request.getParameter("tipe");
		String kata = request.getParameter("kata");
		String pilter = request.getParameter("pilter");
		String jenisDist = request.getParameter("jenisDist");
		request.setAttribute("jenisDist", jenisDist);
		
		List<Pas> pas = uwManager.selectAllPasList(null, "2", tipe, kata, pilter, "fire", jenisDist, null,null,null,null);
		//map.put("pasList", pas);
		//request.setAttribute("cmd", pas);
		request.setAttribute("result","");
		return pas;
	}
	
}
