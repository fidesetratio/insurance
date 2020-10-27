package com.ekalife.elions.web.bac;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentMultiController;

/**
 * Menu Endors New Business, saat ini hanya bisa digunakan untuk tambah rider powersave
 * 
 * @author yusuf
 * @since 20 Apr 09
 */
public class EndorsMultiController extends ParentMultiController {
	/**
	 * Untuk modul main Endors
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 * Filename : EndorsMultiController.java
	 * Create By : Bertho Rafitya Iwasurya
	 * Date Created : Oct 13, 2009 11:01:19 AM
	 */
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		Map map = new HashMap();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", ServletRequestUtils.getStringParameter(request, "reg_spaj"));
		return new ModelAndView("uw/endors/main", map);
	}

}