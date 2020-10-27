package com.ekalife.elions.web.common;

import id.co.sinarmaslife.std.util.Report;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentMultiController;
import com.ibatis.common.resources.Resources;

/**
 * Semua yg berhubungan dgn console, termasuk compile report dan fungsi2 lain, etc2
 * 
 * @author Yusuf
 * @since Jul 8, 2008 (9:59:33 AM)
 */
public class ConsoleMultiController extends ParentMultiController {

	/* Window Utama */
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		HttpSession session = request.getSession();
		User currentUser = (User)session.getAttribute("currentUser");
		Map map = new HashMap();
		return new ModelAndView("common/console/main", map);
	}
	
	/* Window Daftar User yang sedang Login */
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		ServletContext context = request.getSession().getServletContext();
		Map cmd = new HashMap();
    	HashMap users = (HashMap)context.getAttribute("users");
		
    	// Kick User
		if(request.getParameter("tendang")!=null) {
    		String lus_id = ServletRequestUtils.getRequiredStringParameter(request, "lus_id");
    		if(users.containsKey(lus_id)) {
    			Map tmp = (HashMap) users.get(lus_id);
    			HttpSession currentSession = (HttpSession) tmp.get("session");
    			currentSession.invalidate();
    			users.remove(lus_id); 
    		}
    	// Kick All Offline User
    	}else if(request.getParameter("tendangYangMati")!=null) {
    		List<String> daftar = new ArrayList<String>();
    		for(Object o : users.keySet()) {
    			String lus_id = (String) o;
        		if(users.containsKey(lus_id)) {
        			Map tmp = (HashMap) users.get(lus_id);
        			User user = (User) tmp.get("user");
        			String result = ajaxManager.ping(user.getIp());
        			if(result.contains("dead")) {
            			HttpSession currentSession = (HttpSession) tmp.get("session");
            			currentSession.invalidate();
            			users.remove(lus_id); 
        			}
        			daftar.add(result);
        		}
    		}
    		cmd.put("daftar", daftar);
    	}
		
		return new ModelAndView("common/console/login", cmd);
	}
	
	/* Window Daftar User Branch Admin */
	public ModelAndView branch(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		ServletContext context = request.getSession().getServletContext();
		HashMap users = (HashMap) context.getAttribute("users");
		Map cmd = new HashMap();
		
		String lca_id = ServletRequestUtils.getStringParameter(request, "lca_id", "");
		String lde_id = ServletRequestUtils.getStringParameter(request, "lde_id", "");
		int onoff = ServletRequestUtils.getIntParameter(request, "onoff", 1);
		
		List<User> daftarUser = elionsManager.selectAllUsers(lca_id, lde_id);
		for(User user : daftarUser) {
			if(users.containsKey(user.getLus_id())) {
				user.setOnline(1);
				Map tmp = (HashMap) users.get(user.getLus_id());
				User temp = (User) tmp.get("user");
				user.setDivision(temp.getDivision());
				user.setIp(temp.getIp());
				user.setLoginTime(temp.getLoginTime());
				user.setJumlahSpaj(elionsManager.selectJumlahSpajAksesHist(user.getLus_id()));
			}else {
				user.setOnline(0);
			}
		}
		
		if(onoff == 1) {
			List<User> daftarUserOnline = new ArrayList<User>();
			for(User user : daftarUser) {
				if(user.getOnline().intValue() == 1) daftarUserOnline.add(user);
			}
			cmd.put("daftarUser", daftarUserOnline);
		}else {
			cmd.put("daftarUser", daftarUser);
		}
		
		cmd.put("lca_id", lca_id);
		cmd.put("lde_id", lde_id);
		cmd.put("onoff", onoff);
		cmd.put("daftarDept", uwManager.selectAllDepartment());
		cmd.put("daftarCabang", elionsManager.selectAllBranchesAndDepartments(null, lde_id));
		
		return new ModelAndView("common/console/branch", cmd);
	}
	
	/* Window Aktivitas User Branch Admin*/
	public ModelAndView activity(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		String lus_id = ServletRequestUtils.getStringParameter(request, "lus_id", "");
		ServletContext context = request.getSession().getServletContext();
		Map cmd = new HashMap();
		cmd.putAll(elionsManager.selectAksesHist(lus_id));
    	HashMap users = (HashMap) context.getAttribute("users");
		if(users.containsKey(lus_id)) {
			Map tmp = (HashMap) users.get(lus_id);
			User user = (User) tmp.get("user");
			cmd.put("user", user);
		}
		return new ModelAndView("common/console/activity", cmd);
	}
	
	/* Window Maintenance Jasper Report */
	public ModelAndView report(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		HttpSession session = request.getSession();
		User currentUser = (User)session.getAttribute("currentUser");
		Map cmd = new HashMap();

		List<String> reportList = Report.listAllReports(props);
		String[] allReports = StringUtils.commaDelimitedListToStringArray(StringUtils.collectionToCommaDelimitedString(reportList));
		String[] compileReports = null;
		String[] compileResults = null;
		
		// Compile All Report
		if(request.getParameter("recompileReports")!=null) {
			compileReports = allReports;
			compileResults = Report.compileReports(compileReports, getServletContext(), props);
		// Compile Selected Report(s)
		}else if(request.getParameter("recompileSingleReport")!=null) {
			compileReports = ServletRequestUtils.getStringParameters(request, "selectedReport");
			compileResults = Report.compileReports(compileReports, getServletContext(), props);
		}
		
		cmd.put("reports", reportList);
		cmd.put("compiledReports", compileReports);
		cmd.put("compiledStatus", compileResults);
		
		return new ModelAndView("common/console/report", cmd);
	}
	
	/* Window Other Tools */
	public ModelAndView other(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException, IOException{
		HttpSession session = request.getSession();
		User currentUser = (User)session.getAttribute("currentUser");
		Map cmd = new HashMap();
		
    	StringBuffer poffertjes = new StringBuffer();
    	for(Object s : this.props.keySet()) {
    		String key = (String) s;
    		poffertjes.append(key + "=" + this.props.getProperty((String) s) + "\n");
    	}
    	
    	cmd.put("props", poffertjes);
    	cmd.put("appStatus", props.getProperty("app.status"));
		
    	// Reset iBatis Cache
    	if(request.getParameter("resetIbatisCache")!=null) {
    		elionsManager.resetCommonIbatisCache();
    	// Block All Access 
    	}else if(request.getParameter("block")!=null) {
    		props.setProperty("app.status", "0");
    	// Open All Access
    	}else if(request.getParameter("unblock")!=null) {
    		props.setProperty("app.status", "1");
//    	}else if(request.getParameter("deletePdf") != null) {
//    		String daftarSpaj = request.getParameter("spajList");
//    		String[] spaj = StringUtils.commaDelimitedListToStringArray(daftarSpaj);
//    		for(int i=0; i<spaj.length; i++) {
//    			File polis = new File(
//    					props.getProperty("pdf.dir.export")+"\\"+
//    					elionsManager.selectCabangFromSpaj(spaj[i])+"\\"+
//    					spaj[i]+"\\polis_all.pdf");
//    	        if(polis.exists()) polis.delete();
//    		}
    	// Reload Properties file
    	}else if(request.getParameter("reloadProperties") != null) {
    		FileInputStream in = null;
    		try {
				in = new FileInputStream(Resources.getResourceAsFile("ekalife.properties"));
				this.props.load(in);
			} catch (Exception e) {
				logger.error("ERROR :", e);
			}finally{
				 if(in!=null)in.close();
			}
    	}
		
		return new ModelAndView("common/console/other", cmd);
	}
	
}