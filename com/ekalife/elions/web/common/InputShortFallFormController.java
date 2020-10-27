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

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.IncomeCalc;
import com.ekalife.elions.model.Nasabah;
import com.ekalife.elions.model.ProtectCalc;
import com.ekalife.elions.model.SurplusCalc;
import com.ekalife.utils.parent.ParentFormController;

public class InputShortFallFormController extends ParentFormController {
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
		command.setShowTab(1); //tab yang ditampilkan adalah tab 1
		String nomor=ServletRequestUtils.getStringParameter(request,"nomor","");
		Nasabah nasabah = new Nasabah();
		SurplusCalc surplusCalc = new SurplusCalc();
		ProtectCalc protectCalc = new ProtectCalc();
		IncomeCalc incomeCalc = new IncomeCalc();
		String kdNasabah=null,noReferral=null;
		if(! nomor.equals("")){
			int pos=nomor.indexOf('~');
			noReferral=nomor.substring(0,pos);
			kdNasabah=nomor.substring(pos+1,nomor.length());
		}
		nasabah = elionsManager.selectMstNasabah(kdNasabah);
		surplusCalc = elionsManager.selectMstSurplusCalc(kdNasabah);
		protectCalc = elionsManager.selectMstProtectCalc(kdNasabah);
		incomeCalc = elionsManager.selectMstIncomeCalc(kdNasabah);
		command.setNasabah(nasabah);
		command.setSurplusCalc(surplusCalc);
		command.setProtectCalc(protectCalc);
		command.setIncomeCalc(incomeCalc);
		return command;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		SurplusCalc surplusCalc = new SurplusCalc();
		ProtectCalc protectCalc = new ProtectCalc();
		IncomeCalc incomeCalc = new IncomeCalc();
		surplusCalc = command.getSurplusCalc();
		protectCalc = command.getProtectCalc();
		incomeCalc = command.getIncomeCalc();
		if(request.getParameter("save")!= null){
			elionsManager.updateMstSurplusCalc(surplusCalc);
			elionsManager.updateMstProtectCalc(protectCalc);
			elionsManager.updateMstIncomeCalc(incomeCalc);
		}
		
		return new ModelAndView("common/shortfall_calculator",err.getModel()).addObject("submitSuccess","true");
	}
}