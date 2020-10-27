package com.ekalife.elions.web.uw_reinstate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Reinstate;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentFormController;

public class ReinsReasFormController extends ParentFormController {
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		
		String nomor=request.getParameter("nomor");
		String spaj=nomor.substring(0,nomor.indexOf("-"));
		String reinsNo=nomor.substring(nomor.indexOf("~")+1,nomor.length());
		String noPolis=nomor.substring(nomor.indexOf("-")+1,nomor.indexOf("~"));
		Reinstate reins=new Reinstate();
		
		reins.setNoSpaj(spaj);
		reins.setReinsNo(reinsNo);
		reins.setPoPolicyNo(noPolis);
		reins.setFlag(new Integer(1));
		return reins;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Reinstate reins=(Reinstate)cmd;
		reins.setFlag(new Integer(0));
		Integer lspdId;
		String lspdPosition;
		Map map;
			map=(HashMap) elionsManager.selectUwReinstate(reins.getNoSpaj(),reins.getReinsNo());
		
		lspdId=(Integer)map.get("LSPD_ID");
		lspdPosition=(String)map.get("LSPD_POSITION");
		if(lspdId.intValue()==106)
			err.reject("","TIdak Bisa Reas , posisi document ada di "+lspdPosition);
		
		String lsLlapse;
		int liTahun,liBulan = 0,liHari,liHariBulan;
		
		if(lspdId.intValue()==99){
			err.reject("","Reas Polis ini Masih Inforce");
		}else if(lspdId.intValue()==103){
			Date ldtNextPrm=(Date)elionsManager.selectMsre_next_prm_date(reins.getNoSpaj());
			Date ldtNow = (Date)elionsManager.selectDate();
			lsLlapse=FormatDate.getUmur(ldtNextPrm,ldtNow);
			//
			liTahun = Integer.parseInt(lsLlapse.substring(0,2));
			liBulan= Integer.parseInt(lsLlapse.substring(2,4));
			liHari= Integer.parseInt(lsLlapse.substring(4,6));
		
			if (liHari > 15)
				liHariBulan = 1;
			else
				liHariBulan= 0;
			
			liBulan= ( 12 * liTahun) + liBulan+ liHariBulan;
			
		}else if(lspdId.intValue()==104){
			err.reject("","Polis ini di posisi MATURITY");
		}else if(lspdId.intValue() !=18){
			err.reject("","Polis Ini ada di posisi "+lspdPosition);
		}
		reins.setBulan(new Integer(liBulan));
		reins.setLspdId(lspdId);
	
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Reinstate reins=(Reinstate)cmd;
		User currentUser = (User) request.getSession().getAttribute("currentUser");        
		String hasil = elionsManager.prosesReinsReas(reins.getNoSpaj(), currentUser.getLus_id(), reins.getReinsNo(),reins.getLspdId(),reins.getBulan());

		return new ModelAndView("uw_reinstate/reins_reas",err.getModel()).addObject("submitSuccess", "true").addObject("pesan", hasil);
	}	

}
