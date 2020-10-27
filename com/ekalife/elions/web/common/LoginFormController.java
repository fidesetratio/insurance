/**
 * 
 */
package com.ekalife.elions.web.common;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.AksesHist;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentFormController;

import id.co.sinarmaslife.std.util.Session;
/**
 * @author Yusuf
 * 
 */
public class LoginFormController extends ParentFormController {
	protected final Log logger = LogFactory.getLog( getClass() );

	@Override
	/**
	 * Apabila session masih exist, langsung masuk menu utama
	 */
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.addHeader("X-FRAME-OPTIONS", "DENY");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		if(currentUser != null) {
			response.sendRedirect(request.getContextPath() + "/common/menu.htm?frame=main");
		}
		
		return super.handleRequestInternal(request, response);
	}	
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		request.setAttribute("copyrightYear", Common.getCopyrightYear());
		
		User user = new User();
		user.setName(Session.getCookie(request, "elions.userName"));
		//Properties props = new Properties();// Resources.getResourceAsProperties("jdbc.properties");
		String url="";
		url=this.bacManager.getUrlDatabase(url);		
		//props.load(new FileInputStream("C:\\EkaWeb\\jdbc_properties\\jdbc.properties"));
		
		if (url.equalsIgnoreCase("jdbc:oracle:thin:@128.21.22.31:1522:ajsdb")) {
			request.getSession().setAttribute("deebee", "Eka8i");
		} else if ( url.equalsIgnoreCase("jdbc:oracle:thin:@128.21.34.43:1521:ajsdb")) {
			user.setPass("AA");
			request.getSession().setAttribute("deebee", "EkaTest");
		}else if (url.equalsIgnoreCase("jdbc:oracle:thin:@128.21.34.48:1521:ajsdb")) {
			user.setPass("AA");
			request.getSession().setAttribute("deebee", "AJSUAT");
		}else
			request.getSession().setAttribute("deebee", "Unknown!");

		if(!request.getRemoteAddr().startsWith("128.21.")) {
			if(!ServletRequestUtils.getStringParameter(request, "eka_web", "").equals("PASS")) {
				//request.setAttribute("notAllowed", "true");
			}
		}

		//logger.info(this.props.getProperty("company.name"));
		
		//remove this, untuk test session replication saja
//		HttpSession session = request.getSession();
//		Integer tambah = (Integer) session.getAttribute("tambah");
//		if(tambah == null) tambah = new Integer(1);
//		else tambah = new Integer(tambah.intValue() + 1);
//		session.setAttribute("tambah", tambah);
		
		return user;
	}

	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		
		User user = (User) cmd;

		HttpSession session = request.getSession(true);
		ServletContext context = session.getServletContext();
		Map users = (HashMap) context.getAttribute("users");
		if(users == null) users = new HashMap();
		String s_db=(String) request.getSession().getAttribute("deebee");	
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "name", "user.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "pass", "user.pass");

		if(!request.getRemoteAddr().startsWith("128.21.")) {
			if(!ServletRequestUtils.getStringParameter(request, "eka_web", "").equals("PASS")) {
				//request.setAttribute("notAllowed", "true");
				//err.reject("", "Maaf, tetapi anda tidak mempunyai hak akses");
			}
		}	
		
		if(s_db.equalsIgnoreCase("Eka8i")){
			if(!user.getPass().equalsIgnoreCase("123")){
				String passValid=Common.isPasswordValid(user.getPass(), "", user.getLus_change());
				if(!passValid.isEmpty()){
					
					err.reject("user.valid", passValid);
				}
			}
		}else if(s_db.equalsIgnoreCase("EkaTest") || s_db.equalsIgnoreCase("AJSUAT") ){
			user.setPass("AA");
		}
		if(!err.hasErrors()) {
			String pass = user.getPass();
			user = elionsManager.selectLoginAuthentication(user);			
			//
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
					
//					user.setPass(pass);
					Properties props = new Properties();// Resources.getResourceAsProperties("jdbc.properties");
					props.load(new FileInputStream("C:\\EkaWeb\\jdbc_properties\\jdbc.properties"));
					if ("eka8i".equalsIgnoreCase(props.get("ajsdb.jdbc.name").toString())) {
						user.setFlag_user_test(0);
					} else if ("ekatest".equalsIgnoreCase(props.get("ajsdb.jdbc.name").toString()) || "ajstest".equalsIgnoreCase(props.get("ajsdb.jdbc.name").toString())) {
						user.setFlag_user_test(1);
					} 
					session.setAttribute("currentUser", user);
					/***/
				}catch(Exception e) {
					logger.error("LOGIN onBind", e);
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
		a.setMsah_uri("/common/login.htm");
		elionsManager.insertAksesHist(a);
		
		return new ModelAndView(new RedirectView(request.getContextPath() + "/common/menu.htm?frame=main"));
	}

}