package com.ekalife.elions.web.bac;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

public class CancelBacFormController extends ParentFormController{
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		command.setSpaj(request.getParameter("spaj"));
		return command;
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		Integer li_pos;
		BigDecimal li_aksep;
		Integer li_reas_polis;
		String ls_pos;
		int ar_pos=1;
		int insured=1;
		Map map=new HashMap();
		map=(HashMap) elionsManager.selectF_check_posisi(command.getSpaj());
		li_pos=(Integer)map.get("LSPD_ID");
		ls_pos=(String)map.get("LSPD_POSITION");
		//validasi Posisi SPAJ harus di BAC
		if(li_pos.intValue()!=ar_pos )
			err.reject("batalkan.posisi",new Object[]{ls_pos},null);
		
		map=(HashMap) elionsManager.selectWf_get_status(command.getSpaj(),insured);
		li_aksep=(BigDecimal)map.get("LSSA_ID");
		//sudah aksep tidak bisa di batalkan
		if(li_aksep.intValue()==5){
			err.reject("batalkan.aksep");
		}
		
		String textarea=request.getParameter("textarea");
		if(textarea==null || textarea.equals("")){
			err.reject("","Silahkan Isi alasan Pembatalan");
		}
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lus_id=currentUser.getLus_id();
		String ls_alasan=request.getParameter("textarea");
		//cancel polis dari uw dan bac prosesnya sama
		this.elionsManager.cancelPolisFromUw(command.getSpaj(),ls_alasan,lus_id);
		
		return new ModelAndView("bac/cancelBac", err.getModel()).addObject("submitSuccess", "true");
	}
}
