package com.ekalife.elions.web.uw;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Payment;
import com.ekalife.utils.parent.ParentFormController;

public class VoidPaymentFormController extends ParentFormController {
	NumberFormat f2=new DecimalFormat("00");
	NumberFormat f5=new DecimalFormat("00000");
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
		//binder.registerCustomEditor(BigDecimal.class, null, decimalEditor);
		binder.registerCustomEditor(BigDecimal.class, null, new CustomNumberEditor( BigDecimal.class, new DecimalFormat("###,##0.00") , true ));
	}
	
	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		Map map = new HashMap();
		
		return map;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		String nomor=ServletRequestUtils.getStringParameter(request,"nomor","");
		SimpleDateFormat formatDate =new SimpleDateFormat("dd/MM/yyyy");
		String spaj=null,nopolis=null;
		Payment payment = new Payment();
		if(! nomor.equals("")){
			int pos=nomor.indexOf('~');
			spaj=nomor.substring(0,pos);
			nopolis=nomor.substring(pos+1,nomor.length());
			spaj = spaj.replace(".", "");
		}
		if(spaj!=null){
			payment.setLsPayment(uwManager.selectListPayment(spaj));
		}

		command.setPayment(payment);
		command.setNomor(nomor);
		return command;
	}
	
//	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
//		
//	}
//	
//	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
//		Command command=(Command)cmd;
//		
//		return new ModelAndView("uw/voidpayment",err.getModel()).addObject("submitSuccess","true").addAllObjects(this.referenceData(request,command,err));
//	}
	
}