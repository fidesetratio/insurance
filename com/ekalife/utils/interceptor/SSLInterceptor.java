package com.ekalife.utils.interceptor;

/**
 * <p>
 * Konsep class ini dibantu oleh spring, dimana semua controller, pada saat diakses akan 
 * melalui interceptor ini terlebih dahulu (lihat setting di xml-nya spring), dan saat di-intercept
 * bisa melakukan pengecekan hak akses dan lainnya, dan bisa melakukan redirect apabila ternyata
 * user tidak mempunyai hak akses.
 * </p>
 * <p>
 * Class ini berfungsi :
 * <ul>
 *  <li>Mencegah user masuk menu tertentu tanpa LOGIN</li>
 *  <li>Mencegah user membuka spaj yang tidak sesuai hak per cabang nya</li>
 *  <li>Dan fungsi2 lainnya</li>
 * </ul>
 * </p>
 * @author Yusuf
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.NDC;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ekalife.elions.model.User;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.Otorisasi;
import com.ekalife.utils.Products;

public class SSLInterceptor extends HandlerInterceptorAdapter {

	protected final Log logger = LogFactory.getLog( getClass() );
	
	private Otorisasi otorisasi;
	private Products products;
	private ElionsManager elionsManager;
	private UwManager uwManager;
	private BacManager bacManager;
	
	public void setUwManager(UwManager uwManager) {this.uwManager = uwManager;}
	public void setBacManager(BacManager bacManager) {this.bacManager = bacManager;}
	public void setProducts(Products products) {this.products = products;}
	public void setOtorisasi(Otorisasi otorisasi) {this.otorisasi = otorisasi;}
	public void setElionsManager(ElionsManager elionsManager) {this.elionsManager = elionsManager;}
	
	//Main Method
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws Exception {

		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();

		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 1);
		
/** 1. Aktifkan SSL (HTTPS) khusus untuk halaman2 di securedPages */
		
		
    	String[] securedPages = new String[] {//ini untuk page yang ga butuh https
    			
    	};
    	
    	String[] securedPages2 = new String[] {//ini untuk yang perlu https
    			"common/login.htm"
//    			,"bac/editcfl.htm"
    	};
    	
    	String[] serverNameNotReqired = new String[] {//ini untuk yang tdk perlu https
    			"bertho",
    			"ajsjava",
    			"ajsjavai64",
    			"localhost",
    			"yusuf7",
    			"YUSUF7.sinarmaslife.co.id",
    			"dianataliexp",
    			"yusupxp",
    			"deddy7",
    			"fadly7",
    			"andy7",
    			"Adrian_n7",
    			"samuelxp",
    			"andhika-laptop",
    			"ekaweb",
    			"ajsjavaext",
    			"ajsjavae64",
    			"ubuntu",
    			"ryan-laptop",
    			"alfian_h",
    			"antasari7",
    			"lufi-lap",
    			"128.21.34.9",
    			"128.21.34.10",
    			"128.21.31.195",
    			"elions.poc-msig.com",
    			"poc-msig.com",
    			"hayatin7"
    	};
    	
    	
    	String[] serverNameRequired = new String[] {//ini untuk yang perlu https
    			"www.sinarmasmsiglife.co.id",
    			"www.sinarmaslife.co.id",
    			"www.sinarmasmsig.co.id",
    			"www.sinarmasmsiglife.com",
    			"www3.sinarmasmsiglife.co.id",
    			"elions.sinarmasmsiglife.co.id", 
    			"elions.sinarmaslife.co.id",
    			"elions.sinarmasmsig.co.id",
    	};
    	
    	boolean sslRequired = false;
    	boolean sslRequired1 = false;
    	
    	for(String page : securedPages)
        	if(request.getRequestURI().contains(page)) sslRequired = true;
    			
//    	for(String page2 : securedPages2)
//        	if(request.getRequestURI().contains(page2)) sslRequired1 = true;
    	
    	
//    	for(String server : serverNameNotReqired)
//        	if(request.getServerName().equals(server.toLowerCase())) sslRequired1 = false;
    			
    //	logger.info(request.getServerName() +" " + request.getRequestURI()+ " " + request.getScheme());
    	for(String server : serverNameRequired)
        	if(request.getServerName().equals(server.toLowerCase())) {
        		for(String page2 : securedPages2)
                	if(request.getRequestURI().contains(page2)) sslRequired1 = true;
        	}
    	
    	/**
    	 * coding enforce ssl manual
    	 */ 
    	
		if(sslRequired1) {
			if(request.getScheme().equals("http")) {
				response.sendRedirect(
						"https://" + request.getServerName() + request.getRequestURI() + 
						(request.getQueryString()!=null?"?" +request.getQueryString():""));
				return false;
			}
//		}else if((!session.isNew())&&(currentUser!=null)) {
//			if(request.getScheme().equals("http")) {
//				response.sendRedirect(
//						"https://" + request.getServerName() + request.getRequestURI() + 
//						(request.getQueryString()!=null?"?" +request.getQueryString():""));
//			}
		}/*else{
			if(request.getScheme().equals("https")) {
				response.sendRedirect(
						"http://" + request.getServerName() + request.getRequestURI() +
						(request.getQueryString()!=null?"?" +request.getQueryString():""));
				return false;
			}
		}*/
		

		return true;

	}

	//Post-Request Method
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object command, ModelAndView errors)
			throws Exception {
		if(logger.isDebugEnabled())
			logger.debug(request.getRequestURL());
			
		NDC.pop();
	}

}