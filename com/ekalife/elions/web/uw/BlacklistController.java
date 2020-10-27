/**
 * @author  : Ferry Harlim
 * @created : Aug 20, 2007 11:34:46 AM
 */
package com.ekalife.elions.web.uw;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentController;


public class BlacklistController extends ParentController {
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		//map.put("daftarSPAJ", this.uwManager.selectDaftarSPAJ("2", 1,null,null));
		return new ModelAndView("uw/blacklist", map);
	}

}