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
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;


import com.ekalife.elions.model.AddressNew;
import com.ekalife.elions.model.BlackList;
import com.ekalife.elions.model.BlackListFamily;
import com.ekalife.elions.model.Client;
import com.ekalife.elions.model.CmdInputBlacklist;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.DetBlackList;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.ReffBii;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

public class BpOnlineDetailController extends ParentFormController{
	private long accessTime;
	protected final Log logger = LogFactory.getLog(getClass());
	private Email email;
	
	public void setEmail(Email email) {
		this.email = email;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		
//		CmdInputBlacklist detiledit = (CmdInputBlacklist) cmd;
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
		//Pas detiledit = (Pas) cmd;
		
		String msp_id = request.getParameter("msp_id");
		String action = request.getParameter("action");
		String pas_type = request.getParameter("pas_type");
		
		User user = (User) request.getSession().getAttribute("currentUser");
		List<Pas> pas = uwManager.selectAllPasList(msp_id, "1", null, null, null, "pas", null, "bp_online", null,null,null);
		Integer lus_id = Integer.parseInt(user.getLus_id());
		Pas p = pas.get(0);
		
		if("transfer".equals(action)){

			if(p.getReg_spaj() == null)p.setReg_spaj("");
			p.setLus_login_name(user.getLus_full_name());
			//p = uwManager.updatePas1(p);//request, pas, errors,"input",user,errors);
			if(pas_type != null && pas_type.equals("2")){
				String msag_id = uwManager.selectKodeAgentFromMstKusioner(p.getMsp_fire_id());
				if( msag_id != null && !"".equals(msag_id)){
					p.setMsag_id_pp(msag_id);
					p.setMsag_id(msag_id);
					p = uwManager.transferPas(p, lus_id);//request, pas, errors,"input",user,errors);
					if(p.getStatus() == 1){
						request.setAttribute("successMessage","proses transfer gagal");
					}else{
						request.setAttribute("successMessage","transfer sukses");
					}
				}else{
					request.setAttribute("successMessage","tidak bisa di transfer, karena belum mempunyai kode agent. Harap hubungi Bagian Agency Support");
				}
			}
			else{
				p = uwManager.transferPas(p, lus_id);//request, pas, errors,"input",user,errors);
				if(p.getStatus() == 1){
					request.setAttribute("successMessage","proses transfer gagal");
				}else{
					request.setAttribute("successMessage","transfer sukses");
				}
			}
			if(p.getStatus() == 1){
				pas = uwManager.selectAllPasList(msp_id, "1", null, null, null, "pas", null, "bp_online",null,null,null);
			}else{
				pas = uwManager.selectAllPasList(null, "1", null, null, null, "pas", null, "bp_online",null,null,null);
			}
		}
		
		return new ModelAndView(
        "uw/bp_online_detail").addObject("cmd",pas);

    }
	
	protected void initBinder(HttpServletRequest arg0, ServletRequestDataBinder binder) throws Exception {
		logger.debug("EditBacController : initBinder");
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		//logger.debug("EditBacController : formBackingObject");
        this.accessTime = System.currentTimeMillis();
        HttpSession session = request.getSession();
		User user = (User) session.getAttribute("currentUser");
		//Map<String, Object> map = new HashMap<String, Object>();
		
		String tipe = request.getParameter("tipe");
		String kata = request.getParameter("kata");
		String pilter = request.getParameter("pilter");
		String pas_type = request.getParameter("pas_type");
		List<Pas> pas = uwManager.selectAllPasList(null, "1", tipe, kata, pilter, "pas", null, "bp_online",null,null,null);
		
		//for(int i = 0 ; i < pas.size(); i++){
			//pas.get(i).setMsag_id(null);
		//}
		
		int countregbponline = uwManager.countregbponline();
		request.setAttribute("countregbponline", countregbponline);
		
		//map.put("pasList", pas);
		//request.setAttribute("cmd", pas);
		return pas;
	}
	
}
