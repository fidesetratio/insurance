package com.ekalife.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;

import id.co.sinarmaslife.std.spring.util.Email;

/**
 * Class yang digunakan untuk me-redirect error yang terjadi di aplikasi ke
 * halaman bersangkutan (view bernama 'exception')
 * 
 * @author Yusuf
 * @since 01/11/2005
 */
public class CommonExceptionResolver implements HandlerExceptionResolver {
	protected final Log logger = LogFactory.getLog( getClass() );
	
	private Email email;
	private Properties props;
	private String jdbcName;
	
	public void setJdbcName(String jdbcName) {this.jdbcName = jdbcName;}	
	
	public void setProps(Properties props) {
		this.props = props;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	/**
	 * Fungsi untuk menampilkan error pada aplikasi ke halaman 'exception'
	 * 
	 * @param request
	 *            Object request
	 * @param response
	 *            Object response
	 * @param handler
	 *            Object handler
	 * @param exception
	 *            Exception yang terjadi
	 * @return ModelAndView 'exception'
	 */
	
//	Servlet.service() for servlet [spring] in context with path [] threw exception [Request processing failed; nested exception is java.lang.NullPointerException] with root cause
	
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception exception) {
		logger.info("CommonExceptionResolver-resolveException");
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy (hh:mm:ss)");
		Date now = new Date();
		
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        if(currentUser!=null){
        	StringBuffer stackTrace = new StringBuffer();

    		stackTrace.append("\n===== START ERROR [E-Lions][" + df.format(now) + "] ===============");
    		stackTrace.append("\n\n- Class : " + handler);
    		if(currentUser!=null) stackTrace.append("\n- User : " + currentUser.getName() + " [" + currentUser.getLus_id() + "] ");
    		
    		stackTrace.append("\n- Request Parameters : ");
    		Enumeration paramNames = request.getParameterNames();
    		if(paramNames!=null){
    			while(paramNames.hasMoreElements()) {
        			String paramName = (String) paramNames.nextElement();
        			stackTrace.append("\n   " + paramName + " = " + request.getParameter(paramName));
        		}
    		}
    		

    		stackTrace.append("\n- Request QueryString : " + request.getQueryString());
    		stackTrace.append("\n- Request URL : " + request.getRequestURL());		

    		stackTrace.append("\n- Exception : \n");
    		StringWriter sw = null;
    		PrintWriter pw = null;
    		try{
    		sw = new StringWriter();
    		pw = new PrintWriter(sw);
    		Throwable e = Common.getRootCause(exception); //get root exception		
    		e.printStackTrace(pw);
    		stackTrace.append(sw);
    		
    		stackTrace.append("\n- Request Headers: ");
    		Enumeration headerNames = request.getHeaderNames();
    		while(headerNames.hasMoreElements()) {
    			String headerName = (String) headerNames.nextElement();
    			stackTrace.append("\n  " + headerName + " = " + request.getHeader(headerName));
    		}
    		
    		stackTrace.append("\n- Request Method : " + request.getMethod());
    		stackTrace.append("\n- Request Protocol : " + request.getProtocol());
    		stackTrace.append("\n- Request RemoteAddr : " + request.getRemoteAddr());
    		stackTrace.append("\n- Request RemoteHost : " + request.getRemoteHost());
    		stackTrace.append("\n- Request RemotePort : " + request.getRemotePort());
    		stackTrace.append("\n- Request RequestURI : " + request.getRequestURI());
    		stackTrace.append("\n- Request Scheme : " + request.getScheme());
    		stackTrace.append("\n- Request ServerName : " + request.getServerName());
    		stackTrace.append("\n- Request ServerPort : " + request.getServerPort());
    		stackTrace.append("\n- Request ServletPath : " + request.getServletPath());
    		stackTrace.append("\n- Session No Pre: " + request.getSession().getAttribute("no_pre"));		
    				
    		stackTrace.append("\n\n===== END ERROR [E-Lions][" + df.format(now) + "] ===============");

    		}finally{
    			try {
    				if(sw!=null){
    					sw.flush();
    					sw.close();
    				}
    				if(pw!=null){
    					pw.flush();
    					pw.close();
    				}
    				
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				logger.error("ERROR :", e);
    			}
    		}
    		
    		logger.error(stackTrace);
//    		logger.info(stackTrace);
    		
    		/**
    		 * klo error max upload size maka alert aja
    		 */
    		if( stackTrace.toString().contains("SizeLimitExceededException")){
    	 		ServletOutputStream out = null;
    			try {
    				out = response.getOutputStream();
    				out.println("<script>alert('Maksimum ukuran file Upload 5 Mb terlampaui. /nHarap upload file dengan ukuran di bawah ukuran maksimal.');history.go(-1);</script>");
    	    		
    	    		
    			} catch( Exception e1) { 
    				// TODO Auto-generated catch block
    				logger.error(e1.getMessage());
    				logger.error("ERROR :", e1);
    			}finally{
    				try {
    					if(out!=null){
    						out.flush();
    						out.close();
    					}
    					
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					logger.error("ERROR :", e);
    				}
    			}
    			return null;
    		}
    		
    		EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
    				null, 0, 0, now, null, 
    				false,
    				props.getProperty("admin.ajsjava"),
    				//Yusuf (8 Feb 2012) Penambahan daftar penerima email error, agar tahu bila ada error
    				//TOLONG konfirmasi dulu ke saya kalau mau menambah/kurang email penerima ini.
    				new String[] {
    					"ryan@sinarmasmsiglife.co.id","ridhaal@sinarmasmsiglife.co.id", 
    					"chandra.aripin@sinarmasmsiglife.co.id", 
    					"trifena_y@sinarmasmsiglife.co.id",
    					"randy@sinarmasmsiglife.co.id"
    				}, 
    				null, null, 
    				"ERROR pada E-Lions [" + (jdbcName != null ? jdbcName : "") + "]", 
    				"Pesan dikirim oleh : " + currentUser.getName() + " ["+currentUser.getDept()+"]\n\n" + stackTrace.toString(), 
    				null, null);
    		
    		//email tambahan ke himmia & ucup, kalo ada rate powersave yg dobel
    		if(handler!=null)
    		if(handler.toString().contains("EditBacController") 
    				&& stackTrace.toString().contains("executeQueryForObject returned too many results")
    				&& stackTrace.toString().contains("BacDao.selectbungaprosave")) {
    			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
    					null, 0, 0, now, null, 
    					false,
    					props.getProperty("admin.ajsjava"),
    					new String[] {props.getProperty("admin.yusuf"), "himmia@sinarmasmsiglife.co.id"}, 
    					null, null, 
    					"[AJSJAVA] Ada Error Rate Powersave Double!", 
    					"Pesan dikirim oleh : " + currentUser.getName() + " ["+currentUser.getDept()+"]\n\n" + 
    					request.getQueryString(), 
    					null, null);
    		}
        }

		return new ModelAndView("common/exception");
	}

}
