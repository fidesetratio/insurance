package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.spring.util.Email;
import id.co.sinarmaslife.std.util.FileUtil;

import java.io.File;
import java.math.BigDecimal;
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
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Reas;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.Transfer;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.bac.support.form_agen;
import com.ekalife.utils.Common;
import com.ekalife.utils.CheckSum;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentFormController;
import com.ekalife.utils.view.XLSCreatorFreeSimasRumah;
import com.ekalife.utils.view.XLSCreatorPas;

public class StatusPasPartnerFormController extends ParentFormController{
	
	private long accessTime;
	protected final Log logger = LogFactory.getLog(getClass());
	private Email email;
	
	public void setEmail(Email email) {
		this.email = email;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		
		Pas pas=(Pas)cmd;
//		List lsUser = elionsManager.selectLstUser();
//		HttpSession session = request.getSession();
//		User currentUser = (User) session.getAttribute("currentUser");
//		
//		request.setAttribute("lsUser", lsUser);
//		request.setAttribute("LUS_ID", currentUser.getLus_id());
		
		//STATUS
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_ket_status", "","Keterangan harus diisi");
	}
//    }

	protected ModelAndView onSubmit( HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors ) throws Exception
    {

		Pas pas=(Pas)cmd;
		//User currentUser = (User) session.getAttribute("currentUser");
		
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		
		String flag_posisi=ServletRequestUtils.getStringParameter(request, "flag_posisi",null);
		String action=ServletRequestUtils.getStringParameter(request, "action",null);
		request.setAttribute("flag_posisi", flag_posisi);
		
		if(pas.getReg_spaj() == null)pas.setReg_spaj("");
		try{
			pas.setMsp_tgl_status(elionsManager.selectSysdateSimple());
			pas.setLus_id(lus_id);
			pas.setLus_login_name(user.getLus_full_name());
			int x = uwManager.prosesStatusPasPartnerFormController(pas,request,errors, action);
			if(x == 1){
				request.setAttribute("successMessage","edit status sukses");
			}else{
				request.setAttribute("successMessage","edit status gagal");
			}
		}catch (Exception e) {
			request.setAttribute("successMessage","edit status gagal");
		}
		
		return new ModelAndView(
        "uw/status_pas_partner").addObject("cmd",pas);

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
		List lsUser = elionsManager.selectLstUser();
		Pas pas=new Pas();
		String msp_id=ServletRequestUtils.getStringParameter(request, "msp_id",null);
		
		// menentukan pas save langsung muncul konfirmasi transfer ke uw pada pas_detail
		String flag_posisi=ServletRequestUtils.getStringParameter(request, "flag_posisi",null);
		request.setAttribute("flag_posisi", flag_posisi);
		request.setAttribute("lsUser", lsUser);
		request.setAttribute("LUS_ID", currentUser.getLus_id());
		
		List<Map> statusList = uwManager.selectStatusPasList(msp_id, null);
		request.setAttribute("statusList", statusList);
		
		List<Pas> pasList = uwManager.selectAllPasList(msp_id, null, null, null, null, null, null, null,null,null,null);
		pas = pasList.get(0);
		
		//Siti Maulani, Cahyani Prajaningrum : resign: ganti email: permintaan eko
		String emailTo = "widia_a@sinarmasmsiglife.co.id;srirahayu@sinarmasmsiglife.co.id";
		//Stephanus Rudy, Underwriting Agency & Worksite
		String emailCc = currentUser.getEmail();
		//emailAdmin = uwManager.selectPasEmailAdminCabang(pas.getNo_kartu());
//		List<String> emailAdminList = uwManager.selectReportPasEmailList(pas.getLus_id());
//		if(emailAdminList.size() > 0){
//			emailAdmin = emailAdminList.get(0);
//		}
//		Map agen = uwManager.selectEmailAgen2(pas.getMsag_id());
//		if(agen != null){
//			emailAgen = (String) agen.get("MSPE_EMAIL");
//		}
		
		if(pas.getMsp_tgl_status() == null){
			pas.setMsp_tgl_status(elionsManager.selectSysdateSimple());
		}
		
		pas.setEmailTo(emailTo);
		pas.setEmailCc(emailCc);
		
		return pas;
	}
	
}