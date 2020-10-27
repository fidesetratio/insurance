package com.ekalife.elions.web.bac;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.CmdPromo;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

public class PromoController extends ParentFormController{

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
		String beg_date_promo = "14/09/2016";
		String end_date_promo = "31/05/2017";	
		String kanwil_7 = "S161";
		String kanwil_11 = "S279";
		CmdPromo command = new CmdPromo();
		command.setListSpaj(bacManager.selectListPromoChecklistFreeProd());
		Integer countkw7all=0,countkw7app=0,countkw7rej=0,countkw7wait=0 ; 
		Integer countkw11all=0,countkw11app=0,countkw11rej=0,countkw11wait=0 ; 
		
		List kw7all = bacManager.selectCountandvalidSpajPromo(beg_date_promo,end_date_promo,kanwil_7, "1",null);
		if(!kw7all.isEmpty() ){
			Map kw7 = (HashMap) kw7all.get(0);
			countkw7all = ((BigDecimal)kw7.get("TOTAL_FREESPAJ")).intValue();
			command.setJum_spaj_kw7_all(countkw7all);
		}
		
		List kw7app = bacManager.selectCountandvalidSpajPromo(beg_date_promo,end_date_promo,kanwil_7, "1",1);
		if(!kw7app.isEmpty() ){
			Map kw7 = (HashMap) kw7app.get(0);
			countkw7app = ((BigDecimal)kw7.get("TOTAL_FREESPAJ")).intValue();
			command.setJum_spaj_kw7_approve(countkw7app);
		}
		
		List kw7rej = bacManager.selectCountandvalidSpajPromo(beg_date_promo,end_date_promo,kanwil_7, "1",2);
		if(!kw7rej.isEmpty() ){
			Map kw7 = (HashMap) kw7rej.get(0);
			countkw7rej = ((BigDecimal)kw7.get("TOTAL_FREESPAJ")).intValue();
			command.setJum_spaj_kw7_reject(countkw7rej);
		}
		
		List kw7wait = bacManager.selectCountandvalidSpajPromo(beg_date_promo,end_date_promo,kanwil_7, "1",0);
		if(!kw7wait.isEmpty() ){
			Map kw7 = (HashMap) kw7wait.get(0);
			countkw7wait = ((BigDecimal)kw7.get("TOTAL_FREESPAJ")).intValue();
			command.setJum_spaj_kw7_waiting(countkw7wait);
		}
		
		List kw11all = bacManager.selectCountandvalidSpajPromo(beg_date_promo,end_date_promo,kanwil_11, "1",null);
		if(!kw11all.isEmpty() ){
			Map kw11 = (HashMap) kw11all.get(0);
			countkw11all = ((BigDecimal)kw11.get("TOTAL_FREESPAJ")).intValue();
			command.setJum_spaj_kw11_all(countkw11all);
		}
		
		List kw11app = bacManager.selectCountandvalidSpajPromo(beg_date_promo,end_date_promo,kanwil_11, "1",1);
		if(!kw11app.isEmpty() ){
			Map kw11 = (HashMap) kw11app.get(0);
			countkw11app = ((BigDecimal)kw11.get("TOTAL_FREESPAJ")).intValue();
			command.setJum_spaj_kw11_approve(countkw11app);
		}
		
		List kw11rej = bacManager.selectCountandvalidSpajPromo(beg_date_promo,end_date_promo,kanwil_11, "1",2);
		if(!kw11rej.isEmpty() ){
			Map kw11 = (HashMap) kw11rej.get(0);
			countkw11rej = ((BigDecimal)kw11.get("TOTAL_FREESPAJ")).intValue();
			command.setJum_spaj_kw11_reject(countkw11rej);
		}
		
		List kw11wait = bacManager.selectCountandvalidSpajPromo(beg_date_promo,end_date_promo,kanwil_11, "1",0);
		if(!kw11wait.isEmpty() ){
			Map kw11 = (HashMap) kw11wait.get(0);
			countkw11wait = ((BigDecimal)kw11.get("TOTAL_FREESPAJ")).intValue();
			command.setJum_spaj_kw11_waiting(countkw11wait);
		}
		return command;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		CmdPromo command = (CmdPromo) cmd;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		CmdPromo command = (CmdPromo) cmd;
		
		if(request.getParameter("btnApprove") != null || request.getParameter("btnReject") != null){
			command = (CmdPromo) bacManager.prosesApproveSpajFree(command, currentUser,  request,  response, elionsManager, uwManager);
			command.setListSpaj(bacManager.selectListPromoChecklistFreeProd());
			err.rejectValue("pesan", "", command.getPesan());
		}
		
		return new ModelAndView("bac/checklist_free_product", err.getModel()).addAllObjects(this.referenceData(request,command,err));
	}
}
