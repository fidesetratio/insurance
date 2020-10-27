package com.ekalife.elions.web.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentMultiController;

/**
 * Dashboard utama
 * 
 * @author Yusuf
 * @since 19 Jan 2011
 */
public class HomeMultiController extends ParentMultiController {

	/**
	 * Layar Utama 
	 */
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		HttpSession session = request.getSession();
		User currentUser = (User)session.getAttribute("currentUser");
		
		return new ModelAndView("home/home", null);
	}

}