package com.ekalife.elions.web.uw;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentFormController;

public class UbahReasFormController extends ParentFormController {

//	User currentUser;
//	String spaj,lusId,inPass;
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Map map=new HashMap();
		String spaj=request.getParameter("spaj");
		User currentUser = (User) request.getSession().getAttribute("currentUser");        
		String lusId=currentUser.getLus_id();
		String userAkses=this.props.getProperty("reas.akses");
		int pos=0;
		pos=userAkses.indexOf(FormatString.rpad("0", lusId, 3));
		if(pos<=0)
			map.put("info",new Integer(1));
		else
			map.put("info",new Integer(0));
		
		map.put("spaj",spaj);
		return map;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		String inPass=request.getParameter("inpass");
		Integer liReas;
		Map param=(HashMap)cmd;
		String spaj=(String) param.get("spaj");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map map=elionsManager.selectWfGetStatus(spaj,new Integer(1));
		liReas=(Integer)map.get("MSTE_REAS");
		if(liReas==null)
			liReas=new Integer(0);
		if(liReas.intValue()!=2){
			err.reject("","Polis dengan Spaj ("+spaj+") TIDAK FACULTATIVE");
		}
		//
		currentUser.setPass(inPass);
		currentUser=elionsManager.selectLoginAuthentication(currentUser);
		if(currentUser==null)
			err.reject("","Password Anda Salah..");
		
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Map param=(HashMap)cmd;
		String spaj=(String) param.get("spaj");
		
		elionsManager.prosesUbahReas(spaj);
		return new ModelAndView("uw/ubah_reas", err.getModel()).addObject("submitSuccess", "true");
	}
	
	
}
