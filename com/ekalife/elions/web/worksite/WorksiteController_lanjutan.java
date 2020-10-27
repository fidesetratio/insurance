package com.ekalife.elions.web.worksite;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentController;

public class WorksiteController_lanjutan extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();

		return new ModelAndView("worksite/worksite_lanjutan", map);
	}	

}
