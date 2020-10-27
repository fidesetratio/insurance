package com.ekalife.utils.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ekalife.elions.model.User;

/**
 * 
 * @author Yusuf
 * @since 21/04/2006
 * 
 * BELUM BISA DIGUNAKAN, MASIH ERROR, BENTROK SAMA VALIDATOR
 * 
 * Class yang digunakan untuk meng-intercept semua request, dan mengecek apakah user melakukan double submit.
 * Di-set untuk meng-intercept semua request pada controller2 yang di manage oleh spring framework
 * Letakkan tag  <input type="hidden" name="uniqueRequestToken" value="${sessionScope.uniqueRequestToken}">
 * di setiap halaman JSP (dalam <form>)
 */
public class DoubleSubmitInterceptor extends HandlerInterceptorAdapter {

	protected final Log logger = LogFactory.getLog( getClass() );

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws Exception {
		if(logger.isDebugEnabled()) {
			HttpSession session = request.getSession();
			User currentUser = (User) session.getAttribute("currentUser");
			logger.debug("Request by [" + currentUser.getName() + "] for " +request.getRequestURI());

			logger.debug("REQUEST ATTRIBUTES ------------------------------");
			Enumeration attributes = request.getAttributeNames();
			do {
				String attribute = (String) attributes.nextElement();
				logger.debug(attribute + " = " + request.getAttribute(attribute));
			}while(attributes.hasMoreElements());

			logger.debug("REQUEST PARAMETERS ------------------------------");
			Enumeration parameters = request.getParameterNames();
			do {
				String parameter = (String) parameters.nextElement();
				logger.debug(parameter + " = " + request.getParameter(parameter));
			}while(parameters.hasMoreElements());			

			logger.debug("SESSION ATTRIBUTES ------------------------------");
			Enumeration attributes2 = request.getSession().getAttributeNames();
			do {
				String attribute2 = (String) attributes2.nextElement();
				logger.debug(attribute2 + " = " + request.getSession().getAttribute(attribute2));
			}while(attributes2.hasMoreElements());

			try {
				logger.info("NIH = " + request.getSession().getAttribute("cmd.errorMessages"));
			}catch(Exception e) {}
			
		}

		if("GET".equals(request.getMethod())) {
			generateToken(request);
		}else if("POST".equals(request.getMethod())) {
			if(!isTokenValid(request)) {
				response.sendRedirect(request.getContextPath()+"/include/page/doublesubmit.jsp");
				return false;
			}else {
				resetToken(request);
			}
		}
		
		return true;
	}

	private void generateToken(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String token = RandomStringUtils.randomAlphanumeric(25);
		if(logger.isDebugEnabled()) {
			logger.debug("Generating token ["+token+"]");
		}	
		session.setAttribute("lastRequestUri", 
				request.getRequestURI() +"?"+ request.getQueryString());
		session.setAttribute("uniqueRequestToken", 
				token);
	}
	
	private void resetToken(HttpServletRequest request) {
		if(logger.isDebugEnabled()) {
			logger.debug("Resetting Token");
		}	
		HttpSession session = request.getSession();
		session.removeAttribute("uniqueRequestToken");
	}
	
	private boolean isTokenValid(HttpServletRequest request) {
		String sessionToken = (String) request.getSession().getAttribute("uniqueRequestToken");
		String requestToken = (String) request.getParameter("uniqueRequestToken");
		try {
			HttpSession session = request.getSession();
			if(logger.isDebugEnabled()) {
				User currentUser = (User) session.getAttribute("currentUser");
				logger.debug("Checking sessionToken["+sessionToken+"] VS requestToken["+requestToken+"] for [" + currentUser.getName() + "] for request " +request.getRequestURI());
			}
			if(sessionToken.equals(requestToken)) return true;
		}catch(Exception e) {logger.error("ERROR :", e);}
		return false;
	}
	
//	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object command, ModelAndView errors) throws Exception {
//		NDC.pop();
//	}

}
