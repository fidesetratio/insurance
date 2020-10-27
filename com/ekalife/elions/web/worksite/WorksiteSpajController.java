/**
 * 
 */
package com.ekalife.elions.web.worksite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentController;

public class WorksiteSpajController extends ParentController{

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map map = new HashMap();
		String flag=request.getParameter("flag");
		if(flag==null)	flag="0";
		
		if(request.getParameter("search")!=null){
			List result = 
				this.elionsManager.selectFilterSpaj3(
						ServletRequestUtils.getRequiredIntParameter(request, "posisi"),
						request.getParameter("tipe").toString(), 
						request.getParameter("kata"), 
						ServletRequestUtils.getStringParameter(request, "pilter", "="));
			map.put("listSpaj", result);
		}
		map.put("flag",flag);
		
		return new ModelAndView("worksite/cariworksite", "cmd", map);
	}

}
