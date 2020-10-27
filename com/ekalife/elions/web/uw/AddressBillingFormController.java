/**
 * 
 */
package com.ekalife.elions.web.uw;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Inputan Untuk edit address billing, bisa diakses via viewer oleh UW
 * 
 * @author yusuf
 * @since Jul 15, 2009 (11:41:06 AM)
 */
public class AddressBillingFormController extends ParentFormController {

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, 	doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, 	integerEditor); 
	}	

	@Override
	protected Map referenceData(HttpServletRequest request, Object cmd, Errors errors) throws Exception {
		Map refData = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List<Map> lsTitle = new ArrayList<Map>();
		lsTitle = uwManager.selectTitle();
		refData.put("gelar", lsTitle);
		refData.put("infoDetailUser", elionsManager.selectInfoDetailUser(Integer.valueOf(currentUser.getLus_id())));
		return refData;
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String reg_spaj 	= ServletRequestUtils.getRequiredStringParameter(request, "reg_spaj");
		AddressBilling ab 	= elionsManager.selectAddressBilling(reg_spaj);
		
		return ab;
	}

	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Map map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Integer title =  ServletRequestUtils.getIntParameter(request, "gelar");
        AddressBilling update = (AddressBilling)command;
        update.setLti_id(title);
        Pemegang pemegang = elionsManager.selectpp(update.getReg_spaj());
        if(!update.getReg_spaj().equals("")){
        uwManager.updateMstAddressBiling(update, pemegang,currentUser);
        map.put("reg_spaj", update.getReg_spaj());
        map.put("pesan", "Data Berhasil Diupdate");}
        //uwManager.saveAddressBilling(, currentUser);

        return new ModelAndView(new RedirectView(request.getContextPath()+"/uw/addressbilling.htm")).addAllObjects(map);
	}
	
}