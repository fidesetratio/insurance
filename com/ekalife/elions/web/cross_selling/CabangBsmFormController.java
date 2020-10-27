package com.ekalife.elions.web.cross_selling;

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
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.CabangBii;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Inputan untuk rubah hirarki cabang BSM
 * 
 * @author Yusuf
 * @since Apr 16, 2010 (9:03:54 AM)
 */
public class CabangBsmFormController extends ParentFormController {

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, 	doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, 	integerEditor); 
		binder.registerCustomEditor(Date.class, null, 		dateEditor);
	}

	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map refData = new HashMap();
		return refData;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		List<CabangBii> daftarCabang = uwManager.selectAllCabangBsm(1);
		return null;
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
		
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		return new ModelAndView(new RedirectView(request.getContextPath()+"/cross_selling/cabang_bsm.htm"));
	}
	
}