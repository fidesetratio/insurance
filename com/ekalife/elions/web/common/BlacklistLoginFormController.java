/**
 * 
 */
package com.ekalife.elions.web.common;

import id.co.sinarmaslife.std.util.Session;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.AksesHist;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

/**
 * @author Andy
 * 
 */
public class BlacklistLoginFormController extends ParentFormController {

	@Override
	/**
	 * Apabila session masih exist, langsung masuk menu utama
	 */
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		if(currentUser != null) {
			String kmid = request.getParameter("kmid");
			response.sendRedirect(request.getContextPath() + "/uw/input_blacklist_customer.htm?kopiMclId="+kmid+"&flagRefresh=refresh");
		}
		
		return super.handleRequestInternal(request, response);
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		
		User user = new User();
		user.setName(Session.getCookie(request, "elions.userName"));
//		Properties props = Resources.getResourceAsProperties("jdbc.properties");
		Properties props = new Properties();// Resources.getResourceAsProperties("jdbc.properties");
		
		props.load(new FileInputStream("C:\\EkaWeb\\jdbc_properties\\jdbc.properties"));
		String kmid = request.getParameter("kmid");
		request.setAttribute("kmid", kmid);
		
		if ("eka8i".equalsIgnoreCase(props.get("ajsdb.jdbc.name").toString())) {
			request.getSession().setAttribute("deebee", "Eka8i");
		} else if ("ekatest".equalsIgnoreCase(props.get("ajsdb.jdbc.name").toString()) || "ajstest".equalsIgnoreCase(props.get("ajsdb.jdbc.name").toString())) {
			user.setPass("AA");
			request.getSession().setAttribute("deebee", "EkaTest");
		} else
			request.getSession().setAttribute("deebee", "Unknown!");

		if(!request.getRemoteAddr().startsWith("128.21.")) {
			if(!ServletRequestUtils.getStringParameter(request, "eka_web", "").equals("PASS")) {
				//request.setAttribute("notAllowed", "true");
			}
		}

		return user;
	}

	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		
		User user = (User) cmd;

		HttpSession session = request.getSession(true);
		ServletContext context = session.getServletContext();
		Map users = (HashMap) context.getAttribute("users");
		if(users == null) users = new HashMap();
		
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "name", "user.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "pass", "user.pass");

		if(!request.getRemoteAddr().startsWith("128.21.")) {
			if(!ServletRequestUtils.getStringParameter(request, "eka_web", "").equals("PASS")) {
				//request.setAttribute("notAllowed", "true");
				//err.reject("", "Maaf, tetapi anda tidak mempunyai hak akses");
			}
		}
		
		if(!err.hasErrors()) {
			String pass = user.getPass();
			user = elionsManager.selectLoginAuthentication(user);

			if (user == null)
				err.reject("user.error", "Nama atau password salah");
			else {
				if(user.getCab_bank() == null) user.setCab_bank("");
				try {
					//Cara 1 : User login di 2 tempat, dilarang
					/**
					if(users.containsKey(user.getLus_id())) {
		    			Map tmp = (HashMap) users.get(user.getLus_id());
		    			User loggedUser = (User) tmp.get("user");
						err.reject("user.alreadySignedIn", new String[] {loggedUser.getIp()}, "Maaf, tetapi USER ID anda sedang digunakan di komputer dengan IP [{0}]. Silahkan hubungi EDP");
					}else {
						user.setIp(request.getRemoteAddr());
						user.setAksesCabang(elionsManager.selectAksesCabang(Integer.parseInt(user.getLus_id())));
						session.setAttribute("currentUser", user);
					}
					**/
					
					//Cara 2 : User login di 2 tempat, tempat pertamanya di tendang
					/***/
		    		String userName= user.getName();
		    		if(users.containsKey(userName)) {
		    			Map tmp = (HashMap) users.get(userName);
		    			HttpSession currentSession = (HttpSession) tmp.get("session");
		    			currentSession.invalidate();
		    			users.remove(userName); 
		    		}
					user.setIp(request.getRemoteAddr());
	
					if (props.getProperty("access.viewer.region").indexOf(user.getLde_id()) > -1) {
						user.setAksesCabang(elionsManager.selectAksesCabang_lar(Integer
								.parseInt(user.getLus_id())));
					}else{
						user.setAksesCabang(elionsManager.selectAksesCabang(Integer
								.parseInt(user.getLus_id())));
					}
	
					user.setScreenWidth(ServletRequestUtils.getIntParameter(request, "screenWidth", 800));
					user.setScreenHeight(ServletRequestUtils.getIntParameter(request, "screenHeight", 600));
					
			        double angka = (double) user.getScreenWidth() / (double) user.getScreenHeight();
			        if(angka > 1.4) {
			        	user.setWideScreen(true);
			        }else {
			        	user.setWideScreen(false);
			        }
					
					user.setPass(pass);
					session.setAttribute("currentUser", user);
					/***/
				}catch(Exception e) {
					err.reject("", "Silahkan LOGIN kembali karena session anda sudah berakhir.");
				}
				
			}
		}

	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		User user = (User) command;
		Session.setCookie(response, "elions.userName", user.getName(), 30);
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		AksesHist a = new AksesHist();
		a.setLus_id(Integer.valueOf(currentUser.getLus_id()));
		a.setMsah_date(currentUser.getLoginTime());
		a.setMsah_jenis(1);
		a.setMsah_spaj(null);
		a.setMsah_uri("/common/blacklistLogin.htm");
		elionsManager.insertAksesHist(a);
		
		String kmid = request.getParameter("kmid");
		return new ModelAndView(new RedirectView(request.getContextPath() + "/uw/input_blacklist_customer.htm?kopiMclId="+kmid+"&flagRefresh=refresh"));
	}

}