package com.ekalife.servlet;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.ajaxtags.servlets.BaseAjaxServlet;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.ekalife.elions.service.AjaxManager;

public class AjaxAutoCompleteServlet extends BaseAjaxServlet{

	private AjaxManager ajaxManager;
	
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		XmlWebApplicationContext context = (XmlWebApplicationContext) servletContext.getAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.spring");		
		ajaxManager = (AjaxManager) context.getBean("ajaxManager");
	}
	
	public String getXmlContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    String query = request.getParameter(request.getParameter("s"));
	    String jenis = request.getParameter("q");
	    
	    List list = null;
	    
	    if(jenis.equalsIgnoreCase("wilayah")){
	    	list = ajaxManager.select_wilayah(query);
	    	return new AjaxXmlBuilder().addItems(list, "ID", "WILAYAH").toString();
	    }

	    else if(jenis.equalsIgnoreCase("user")) {
	    	list = ajaxManager.selectUserList(query);
		    return new AjaxXmlBuilder().addItems(list, "LUS_LOGIN_NAME", "LUS_LOGIN_NAME").toString();
	    }
	    
	    else if(jenis.equalsIgnoreCase("region")) {
	    	list = ajaxManager.listRegion(query);
		    return new AjaxXmlBuilder().addItems(list, "REGION_NAME", "REGION_ID").toString();
	    }
	    
	    else if(jenis.equalsIgnoreCase("region2")) {
	    	list = ajaxManager.listRegion2(query.toUpperCase().trim());
		    return new AjaxXmlBuilder().addItems(list, "LSRG_NAMA", "REGION_ID").toString();
	    }
	    else if(jenis.equalsIgnoreCase("icd")) {
	    	list = ajaxManager.listIcd(query);
	    	return new AjaxXmlBuilder().addItems(list, "CODE", "ID").toString();
	    }
	    return null;
	}

}
