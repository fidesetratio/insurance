/**
 * 
 */
package com.ekalife.elions.web.uw;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentFormController;

/**
 * @author Yusuf
 * 
 */
public class CancelFormController extends ParentFormController {

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		Map cmd = new HashMap();
		cmd.put("spaj", ServletRequestUtils.getStringParameter(request, "spaj", ""));
		cmd.put("verify", "");
		cmd.put("alasan", "");
		return cmd;
	}

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		//validasi sebelum form di load
		BindingResult err = binder.getBindingResult();
		
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		
		Date ldt_sys = this.elionsManager.selectMst_default(1);
		Date ldt_1 = this.elionsManager.selectSysdate(0);
		if(ldt_sys.compareTo(ldt_1)<=0){
			ldt_1 = ldt_sys;
		}
		Date ldt_tgl_prod = this.elionsManager.selectPolicyPrintDate(spaj);
		if(ldt_tgl_prod!=null){
			long a = FormatDate.dateDifference(ldt_tgl_prod, ldt_1, true);
			if(a >= 30){
				//err.reject("payment.30days");
			}
		}

		//ditutup 17/04/2007	
		//	if(products.unitLink(elionsManager.selectBusinessId(spaj))) {
		//		err.reject("payment.isUnitLinked");
		//}
		if(err.getErrorCount()>0)request.setAttribute("hasInitErrors", "true");
	}

	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		//Validasi setelah submit
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String verify = ServletRequestUtils.getStringParameter(request, "verify", "").toUpperCase();
		String alasan = ServletRequestUtils.getStringParameter(request, "alasan", "");
		
		if(spaj.equals(""))err.reject("payment.noSPAJ");
		else if(verify.equals(""))err.reject("payment.inputPassword");
		else if(alasan.equals(""))err.reject("payment.inputAlasan");
		else{
			if(!this.elionsManager.validationVerify(6).get("PASSWORD").toString().trim().equals(verify)){
				err.reject("payment.notAuthorized");
			}			
		}

	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String from = ServletRequestUtils.getStringParameter(request, "from", "payment");
		if("payment".equals(from))
			this.elionsManager.cancelPolisFromPayment(ServletRequestUtils.getStringParameter(request, "spaj", ""), ServletRequestUtils.getStringParameter(request, "alasan", ""), currentUser);
		else if("tandaterima".equals(from))
			this.elionsManager.cancelPolisFromTandaTerimaPolis(ServletRequestUtils.getStringParameter(request, "spaj", ""), ServletRequestUtils.getStringParameter(request, "alasan", ""), currentUser);
		
		return new ModelAndView("uw/cancel", errors.getModel()).addObject("submitSuccess", "true");
	}
	
	protected ModelAndView handleInvalidSubmit(HttpServletRequest request,
            HttpServletResponse response)
			throws Exception{
		return new ModelAndView("common/duplicate");
	}	
}
