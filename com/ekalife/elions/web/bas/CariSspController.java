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
public class CariSspController extends ParentController {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map map = new HashMap();
		
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		map.put("user_id", lus_id);
		
		if(request.getParameter("search") != null){
			String id = request.getParameter("id");
			String nama = request.getParameter("nama");
			String no_kanwill = request.getParameter("no_kanwill");
			String no_rek = request.getParameter("no_rek");
			
			List listSsp = uwManager.selectSearchSsp(id, nama, no_kanwill, no_rek);
			map.put("listSsp", listSsp);
		}
		
		return new ModelAndView("bas/cari_ssp", "cmd", map);
	}

}
