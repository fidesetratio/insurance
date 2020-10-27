package com.ekalife.elions.web.uw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Reinsurer;
import com.ekalife.utils.parent.ParentFormController;

public class ReinsurerFormController extends ParentFormController {

	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		Map<String, List> map = new HashMap<String, List>();
		
		map.put("lsHslReas",uwManager.selectLsInsurer());
		
		return map;		
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Reinsurer reinsurer = new Reinsurer();
		Integer id = ServletRequestUtils.getIntParameter(request, "id",-1);
		
		if(id != -1) {
			reinsurer = uwManager.getDataReinsurer(id);
			reinsurer.setType_reinsurer(1);
		}
		else reinsurer.setType_reinsurer(0);
		
		return reinsurer;
	}
	
	/*protected boolean isFormChangeRequest(HttpServletRequest request) {
		String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode", "");
		if(submitMode.equals("lsrei_id")) {
			return true;
		}
		return false;
	}
	
	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object cmd) throws Exception {
		Reinsurer reinsurer = (Reinsurer) cmd;
		Reinsurer fillData = uwManager.getDataReinsurer(reinsurer.getLsrei_id());
		if(fillData != null) {
			reinsurer.setLsre_nama(fillData.getLsre_nama());
			reinsurer.setLsre_alamat(fillData.getLsre_alamat());
			reinsurer.setLsre_telpon(fillData.getLsre_telpon());
			reinsurer.setLsre_fax(fillData.getLsre_fax());
			reinsurer.setLsre_contact(fillData.getLsre_contact());
			reinsurer.setLsre_account_gl(fillData.getLsre_account_gl());
			reinsurer.setLsre_email(fillData.getLsre_email());
		}
	}*/
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException errors) throws Exception {
		String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode", "");
		if(submitMode.equals("lsrei_id")) {
			Reinsurer reinsurer = (Reinsurer) cmd;
			Reinsurer fillData = uwManager.getDataReinsurer(reinsurer.getLsrei_id());
			if(fillData != null) {
				reinsurer.setLsre_nama(fillData.getLsre_nama());
				reinsurer.setLsre_alamat(fillData.getLsre_alamat());
				reinsurer.setLsre_telpon(fillData.getLsre_telpon());
				reinsurer.setLsre_fax(fillData.getLsre_fax());
				reinsurer.setLsre_contact(fillData.getLsre_contact());
				reinsurer.setLsre_account_gl(fillData.getLsre_account_gl());
				reinsurer.setLsre_email(fillData.getLsre_email());	
				errors.reject("", "");
			}	
		}
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors) throws Exception {
		Map map = new HashMap();
		Reinsurer reinsurer = (Reinsurer) cmd;
		
		map.put("success", uwManager.prosesReinsurer(reinsurer));
		map.put("id", reinsurer.getLsrei_id());
		
		return new ModelAndView(new RedirectView(request.getContextPath()+"/uw/reinsurer.htm")).addAllObjects(map);
	}
}
