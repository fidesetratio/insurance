package com.ekalife.elions.web.viewer;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentController;

/**
 * @author Yusuf
 *
 */
public class AgencyController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		
		if(request.getParameter("search")!=null){
			map.put("listagen", 
					elionsManager.selectFilteragency(
						request.getParameter("tipe").toString(), 
						request.getParameter("kata").toUpperCase(), 
						ServletRequestUtils.getStringParameter(request, "pilter", "=")));
		} 
		
		map.put("flag", ServletRequestUtils.getStringParameter(request, "flag", "0"));
		
		return new ModelAndView("uw/viewer/agency", "cmd", map);
	}

}