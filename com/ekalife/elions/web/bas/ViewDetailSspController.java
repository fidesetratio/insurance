package com.ekalife.elions.web.bas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentController;

/**
 * @author : Daru
 * @since : Apr 15, 2013
 */
public class ViewDetailSspController extends ParentController {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map map = new HashMap();
		
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		map.put("user_id", lus_id);
		
		if(request.getParameter("id") != null){
			String id = request.getParameter("id");
			Map ssp = uwManager.selectViewSsp(id);
			map.put("ssp", ssp);
			List sspBill = uwManager.selectViewSspBill(id);
			map.put("sspBill", sspBill);
		}
		
		return new ModelAndView("bas/view_detail_ssp",map);
	}

}
