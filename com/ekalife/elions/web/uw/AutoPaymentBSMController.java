package com.ekalife.elions.web.uw;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.CmdAutoPaymentVA;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

public class AutoPaymentBSMController extends ParentFormController{

	//Fungsi ini untuk mengubah bentuk angka baik type data double, integer, dan date. Misal: hasil keluarnya 12.234AF menjadi 12.234.567
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	protected Map<String, Object> referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("listCaraBayar", elionsManager.selectDropDown("eka.lst_payment_type", "lsjb_id", "lsjb_type", "", "lsjb_id", "lsjb_type_bank = 1"));
//		map.put("listMerchant", elionsManager.selectDropDown("eka.lst_merchant_fee", "id_merchant", "nama", "", "id_merchant", null));
		return map;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		CmdAutoPaymentVA command = new CmdAutoPaymentVA();
		command.setListSpaj(bacManager.selectListPaymentBSM());
		return command;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		CmdAutoPaymentVA command = (CmdAutoPaymentVA) cmd;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		CmdAutoPaymentVA command = (CmdAutoPaymentVA) cmd;
		
		if(request.getParameter("btnSubmit") != null){
			command = (CmdAutoPaymentVA) bacManager.prosesAutoPaymentBSM(command, currentUser);
			command.setListSpaj(bacManager.selectListPaymentBSM());
			err.rejectValue("pesan", "", command.getPesan());
		}
		
		return new ModelAndView("uw/autopayment_bsm", err.getModel()).addAllObjects(this.referenceData(request,command,err));
	}
}
