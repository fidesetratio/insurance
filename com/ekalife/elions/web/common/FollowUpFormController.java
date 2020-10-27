package com.ekalife.elions.web.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Aktivitas;
import com.ekalife.elions.model.Command;
import com.ekalife.utils.parent.ParentFormController;

public class FollowUpFormController extends ParentFormController {
	NumberFormat f2=new DecimalFormat("00");
	NumberFormat f5=new DecimalFormat("00000");
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
		//binder.registerCustomEditor(BigDecimal.class, null, decimalEditor);
		binder.registerCustomEditor(BigDecimal.class, null, new CustomNumberEditor( BigDecimal.class, new DecimalFormat("###,##0.00") , true ));
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		Aktivitas aktivitas = new Aktivitas();
		Date sysdate = elionsManager.selectSysdateSimple();
		String nomor=ServletRequestUtils.getStringParameter(request,"nomor","");
		String kdNasabah=null,noReferral=null;
		if(! nomor.equals("")){
			int pos=nomor.indexOf('~');
			noReferral=nomor.substring(0,pos);
			kdNasabah=nomor.substring(pos+1,nomor.length());
		}
		String mns_kd_nasabah = kdNasabah;
		aktivitas.setKd_aktivitas(7);
		aktivitas.setTgl_pert(sysdate);
		aktivitas.setMns_kd_nasabah(mns_kd_nasabah);
		command.setAktivitas(aktivitas);
		command.setSysdate(sysdate);
		return command;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		Aktivitas aktivitas = command.getAktivitas();
		if(request.getParameter("save")!=null){
			elionsManager.prosesInputFollowUp(aktivitas);
		}
		return new ModelAndView("common/follow_up",err.getModel()).addObject("submitSuccess","true");
	}
	
}