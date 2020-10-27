/**
 * 
 * @author ferryh
 *
 */
package com.ekalife.elions.web.uw;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

public class CancelUwFormController extends ParentFormController {

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Map map=new HashMap();
		String spaj=request.getParameter("spaj");
		map.put("spaj",spaj);
		
		return (HashMap)map;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Map map =new HashMap();
		String spaj=request.getParameter("spaj");
		Integer li_pos;
		BigDecimal li_aksep;
		Integer li_reas_polis;
		String ls_pos;
		int ar_pos=1;
		int insured=1;
		map=(HashMap) elionsManager.selectF_check_posisi(spaj);
		li_pos=(Integer)map.get("LSPD_ID");
		ls_pos=(String)map.get("LSPD_POSITION");
		if(logger.isDebugEnabled())logger.debug("lipos= "+li_pos);
		if(logger.isDebugEnabled())logger.debug("lspos= "+ls_pos);
		
		if(li_pos.intValue()==ar_pos || li_pos.intValue()==95)
			err.reject("batalkan.posisi",new Object[]{ls_pos},null);
		
		map=(HashMap) elionsManager.selectWf_get_status(spaj,insured);
		li_aksep=(BigDecimal)map.get("LSSA_ID");
		
		if(li_aksep.intValue()==5){
			err.reject("batalkan.aksep");
		}
		
		String textarea=request.getParameter("textarea");
		if(textarea==null || textarea.equals("")){
			err.reject("","Silahkan Isi alasan Pembatalan");
		}
		
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lus_id=currentUser.getLus_id();
		
		String spaj=request.getParameter("spaj");
		String ls_alasan=request.getParameter("textarea");
		this.elionsManager.cancelPolisFromUw(spaj,ls_alasan,lus_id);
		
		return new ModelAndView("uw/cancelUw", err.getModel()).addObject("submitSuccess", "true");
		
	}
}