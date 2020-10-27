/**
 * 
 */
package com.ekalife.elions.web.uw;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentFormController;

/**
 * @author Yusuf
 * 
 */
public class PaymentFormController extends ParentFormController {

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		return new HashMap();
	}

	protected ModelAndView handleInvalidSubmit(HttpServletRequest request,
            HttpServletResponse response)
			throws Exception{
		return new ModelAndView("common/duplicate");
	}	
	
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map = new HashMap();
		List daftarSPAJ = this.uwManager.selectDaftarSPAJPayment("4", 1,null,null);
		Date now = elionsManager.selectSysdate();
		map.put("startDate", defaultDateFormat.format(now));
		map.put("endDate", defaultDateFormat.format(now));
		map.put("daftarSPAJ", daftarSPAJ);
		map.put("snow_spaj", ServletRequestUtils.getStringParameter(request, "snow_spaj", ""));
		return map;
	}
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		//validasi sebelom submit
		//BindException err = binder.getErrors();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		Integer topupBerkala; 
		//boolean blm = false;
		if((topupBerkala=this.elionsManager.validationTopupBerkala(spaj))!=null){ //apakah ada topup berkala/tunggal ?
			if(topupBerkala == 1 || topupBerkala == 2){ //2 = tunggal, 1 = berkala
				if(this.elionsManager.validationAlreadyPaid(spaj, 1 ,2)==0){//belom lunas topupnya
					//blm = true;
					request.setAttribute("pesan", "Billing Top-up belum lunas.");
				}
			}
		}

	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		return new ModelAndView("uw/payment", errors.getModel()).addObject("submitSuccess", "true").addAllObjects(this.referenceData(request));

	}

}