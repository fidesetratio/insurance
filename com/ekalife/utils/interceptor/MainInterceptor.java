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
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.NDC;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ekalife.elions.model.AksesHist;
import com.ekalife.elions.model.User;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.Common;
import com.ekalife.utils.Otorisasi;
import com.ekalife.utils.Products;

public class MainInterceptor extends HandlerInterceptorAdapter {

	protected final Log logger = LogFactory.getLog( getClass() );
	
	private Otorisasi otorisasi;
	private Products products;
	private ElionsManager elionsManager;
	private UwManager uwManager;
	private BacManager bacManager;
	private Properties props;
	
	public void setUwManager(UwManager uwManager) {this.uwManager = uwManager;}
	public void setBacManager(BacManager bacManager) {this.bacManager = bacManager;}
	public void setProducts(Products products) {this.products = products;}
	public void setOtorisasi(Otorisasi otorisasi) {this.otorisasi = otorisasi;}
	public void setElionsManager(ElionsManager elionsManager) {this.elionsManager = elionsManager;}
	public void setProps(Properties props) {this.props = props;}
	
	//Main Method
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws Exception {

		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String ac = request.getParameter("ac");
				
		//disable cache (http://stackoverflow.com/questions/2156077/how-can-i-disable-tomcat-caching-completly)
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.

		String redirect = null;
		boolean authorized = true;

		//logger.info("============================= : " + requestUri);		
		
		/**
		 * Exclude them
		 * Patar Timotius
		 */
		if(requestUri.indexOf("webservice/gadgetonlinewebservice.htm")  > -1){
			return true;
		}
		
		String frame = ServletRequestUtils.getStringParameter(request, "frame", "");
		
		String url="";
		url=this.bacManager.getUrlDatabase(url);
		
		if(currentUser != null) {
			if(currentUser.getPass().equals("123") && !frame.equals("changepassword")) { //apabila belum ganti password, silahkan ganti password
				authorized = false;
				redirect = contextPath + "/common/menu.htm?frame=changepassword&warning=1";
			}	
			
			if(currentUser.getLus_nik()==null && currentUser.getJn_bank().intValue()!=2 && currentUser.getJn_bank().intValue()!=3 && currentUser.getJn_bank().intValue()!=16  && !frame.equals("isinik")){
				authorized = false;
				redirect = contextPath + "/common/menu.htm?frame=isinik&warning=1";
				
			}
			
			String passValid=Common.isPasswordValid(currentUser.getPass(), "", currentUser.getLus_change());
			if(!passValid.isEmpty() && url.equalsIgnoreCase("jdbc:oracle:thin:@128.21.22.31:1522:ajsdb")){
				authorized = false;
				redirect = contextPath + "/common/menu.htm?frame=changepassword&flag=1&warn="+passValid;
			}
			
		}
		
		if(authorized)
		if ((authorized = otorisasi.validasiApplicationBlocked()) == false) {
			redirect = contextPath + "/include/page/blocked.jsp?jenis=blocked";
		}
		
		if(authorized)
		if ((authorized = otorisasi.validasiSession(session, currentUser)) == false) {
			redirect = contextPath + "/include/page/blocked.jsp?jenis=redirect";
		}
		
		if(authorized)
		if ((authorized = otorisasi.validasiUnderwriting(request, currentUser)) == false) {
			redirect = contextPath + "/include/page/blocked.jsp?jenis=uw";
		} 
		
		if(authorized)
		if ((authorized = otorisasi.validasiAkses(request, currentUser)) == false) {
			redirect = request.getContextPath() + "/include/page/blocked.jsp?jenis=branch";
		} 
		
		if(authorized)
		if((authorized = otorisasi.validasiUAT(request, currentUser)) == false) {
			redirect = request.getContextPath() + "/include/page/blocked.jsp?jenis=uat";
		}
		//TODO
		if(authorized)
			if ((authorized = otorisasi.validasiOtorisasiInputSpaj(request, currentUser)) == false) {
				redirect = contextPath + "/include/page/blocked.jsp?jenis=otorisasi";
			} 
		
		if(authorized)
			if ((authorized = otorisasi.validasiOtorisasiInputTopUp(request, currentUser)) == false) {
				redirect = contextPath + "/include/page/blocked.jsp?jenis=otorisasi";
			} 

		//print sph hanya life benefit atau underwriting yang boleh - Yusuf - 11/10/2007
		if(requestUri.indexOf("report/bac.")>-1) {
			if(ServletRequestUtils.getStringParameter(request, "window", "").indexOf("sph")>-1) {
//				if(!currentUser.getLde_id().equals("27")) {
//					authorized = false;
//					redirect = contextPath + "/include/page/blocked.jsp?jenis=sph";
//				}
				
				//sph hanya untuk produk powersave
				String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
				if(!spaj.equals("")) {
					List d = elionsManager.selectDetailBisnis(spaj);
					if(!d.isEmpty()) {
						Map detBisnis = (Map) d.get(0);
						String businessId = (String) detBisnis.get("BISNIS");
						if(!(products.powerSave(businessId) || products.stableLink(businessId))) {
							authorized = false;
							redirect = contextPath + "/include/page/blocked.jsp?jenis=sph";
						}
					}
				}
				
			}
		}
		
		/* untuk user uw corp biar bs view di lions : menu akum new */
		/* closed, user access directly from elions
		if(requestUri.indexOf("uw/viewer.htm") > -1 && currentUser == null) {
			if(ServletRequestUtils.getStringParameter(request, "window", "").indexOf("akum_new")>-1){
				redirect = contextPath + "/uw/viewer.htm?window=akum_new";
				authorized = true;
				currentUser = new User();
				currentUser.setLca_id("01");
				currentUser.setLus_id("0");
				currentUser.setPass("Akum123456");
				currentUser.setLde_id("11");
				currentUser.setLus_nik("123456789");
				session.setAttribute("currentUser", currentUser);
			}
		}
		*/
		
		/* untuk user uw corp biar bs view di lions : menu worksheet */
		if(requestUri.indexOf("uw/uw.htm") > -1 && currentUser == null) {
			if(ServletRequestUtils.getStringParameter(request, "window", "").indexOf("main")>-1){
				
				int akses = 0;
				if("1".equals(props.getProperty("use.token"))) {
					String tkey = request.getParameter("tkey");
					if(!StringUtils.isEmpty(tkey)) {
						akses = uwManager.validateUrlKey(tkey, url);
					}
				}else {
					akses = 1;
				}
				
				if(akses == 1) {
					redirect = contextPath + "/uw/uw.htm?window=main";
					authorized = true;
					currentUser = new User();
					currentUser.setLca_id("01");
					currentUser.setLus_id("0");
					currentUser.setPass("Main123456");
					currentUser.setLde_id("07");
					currentUser.setLus_admin(0);
					currentUser.setLus_nik("123456789");
					session.setAttribute("currentUser", currentUser);
				}else {
					redirect = contextPath + "/include/page/blocked.jsp?jenis=process";
				}
			}
			//untuk direksi Bpk CK & Hamid agar bs akseptasi spt
			if(ServletRequestUtils.getStringParameter(request, "window", "").indexOf("aksepsp")>-1){
				
				int akses = 0;
				if("1".equals(props.getProperty("use.token"))) {
					String tkey = request.getParameter("tkey");
					if(!StringUtils.isEmpty(tkey)) {
						akses = uwManager.validateUrlKey(tkey, url);
					}
				}else {
					akses = 1;
				}
				
				if(akses == 1) {
					String ld=request.getParameter("id");
					String rs=request.getParameter("spaj");
					String pp=request.getParameter("pp");
					String status=request.getParameter("status");
					String email=request.getParameter("email");
					String flag=request.getParameter("flag");
					redirect = contextPath+"/uw/uw.htm?window=aksepsp&id="+ld+"&spaj="+rs+"&pp="+pp+"&status="+status+"&email="+email+"&flag="+flag;
					authorized=false;				
					currentUser = new User();				
					currentUser.setLus_id(ld);
					currentUser.setPass("Ajs12345");
					//currentUser.setLca_id("01");				
					currentUser.setLde_id(flag);
					currentUser.setLus_admin(0);					
					currentUser.setLus_nik("123456789");
					session.setAttribute("currentUser", currentUser);
				}else {
					redirect = contextPath + "/include/page/blocked.jsp?jenis=process";
				}
			
				
			}
			if(ServletRequestUtils.getStringParameter(request, "window", "").indexOf("rekklaim")>-1){
				
				int akses = 0;
				if("1".equals(props.getProperty("use.token"))) {
					String tkey = request.getParameter("tkey");
					if(!StringUtils.isEmpty(tkey)) {
						akses = uwManager.validateUrlKey(tkey, url);
					}
				}else {
					akses = 1;
				}
				
				if(akses == 1) {
					String lus_id=request.getParameter("id");
					String reg_spaj=request.getParameter("spaj");
					String inbox=request.getParameter("inbox");
					String email=request.getParameter("email");
					String lde_id=request.getParameter("lde");
					String login_name=request.getParameter("name");
					Integer flag_aksep= ServletRequestUtils.getIntParameter(request, "flag_aksep", 0);//0 belum aksep, 1 : setuju aksep, 2 : tidak setuju aksep.
					redirect = contextPath+"/uw/uw.htm?window=rekklaim&id="+lus_id+"&spaj="+reg_spaj+"&inbox="+inbox+"&email="+email+"&lde="+lde_id+"&name="+login_name+"&flag_aksep="+flag_aksep;
					authorized=false;				
					currentUser = new User();
					currentUser.setLus_id(lus_id);
					currentUser.setPass("Reklaim1234");
					currentUser.setLde_id(lde_id);
					currentUser.setName(login_name);
					currentUser.setLca_id("01");
					currentUser.setEmail(email);
					currentUser.setLde_id("01");
					currentUser.setLus_nik("123445566");
					session.setAttribute("currentUser", currentUser);
				}else {
					redirect = contextPath + "/include/page/blocked.jsp?jenis=process";
				}
			}
			if(ServletRequestUtils.getStringParameter(request, "window", "").indexOf("snowsdireksi")>-1){
				
				int akses = 0;
				if("1".equals(props.getProperty("use.token"))) {
					String tkey = request.getParameter("tkey");
					if(!StringUtils.isEmpty(tkey)) {
						akses = uwManager.validateUrlKey(tkey, url);
					}
				}else {
					akses = 1;
				}
				
				if(akses == 1) {
					System.out.println("akses : "+akses);
					String lus_id=request.getParameter("id");
					String reg_spaj=request.getParameter("spaj");
					String inbox=request.getParameter("inbox");
					String email=request.getParameter("email");
					String lde_id=request.getParameter("lde");
					String login_name=request.getParameter("name");
					String jns=request.getParameter("jns");
					Integer flag_aksep= ServletRequestUtils.getIntParameter(request, "flag_aksep", 0);//0 belum aksep, 1 : setuju aksep, 2 : tidak setuju aksep.
					redirect = contextPath+"/uw/uw.htm?window=snowsdireksi&id="+lus_id+"&spaj="+reg_spaj+"&inbox="+inbox+"&email="+email+"&lde="+lde_id+"&name="+login_name+"&flag_aksep="+flag_aksep+"&jns="+jns;
					authorized=false;				
					currentUser = new User();
					currentUser.setLus_id(lus_id);
					currentUser.setPass("Reklaim1234");
					currentUser.setLde_id(lde_id);
					currentUser.setName(login_name);
					currentUser.setLca_id("01");
					currentUser.setEmail(email);
					currentUser.setLde_id("01");
					currentUser.setLus_nik("123445566");
					session.setAttribute("currentUser", currentUser);
				}else {
					redirect = contextPath + "/include/page/blocked.jsp?jenis=process";
				}
			}
			if(ServletRequestUtils.getStringParameter(request, "window", "").indexOf("snowsdireksiaksep")>-1){
				
				int akses = 0;
				if("1".equals(props.getProperty("use.token"))) {
					String tkey = request.getParameter("tkey");
					if(!StringUtils.isEmpty(tkey)) {
						akses = uwManager.validateUrlKey(tkey, url);
					}
				}else {
					akses = 1;
				}
				
				if(akses == 1) {
					String lus_id=request.getParameter("id");
					String reg_spaj=request.getParameter("spaj");
					String inbox=request.getParameter("inbox");
					String email=request.getParameter("email");
					String lde_id=request.getParameter("lde");
					String login_name=request.getParameter("name");
					String jns=request.getParameter("jns");
					String idx=request.getParameter("idx");
					Integer status= ServletRequestUtils.getIntParameter(request, "status", 0);//0 belum aksep, 1 : setuju aksep, 2 : tidak setuju aksep.
					redirect = contextPath+"/uw/uw.htm?window=snowsdireksiaksep&id="+lus_id+"&spaj="+reg_spaj+"&inbox="+inbox+"&email="+email+"&lde="+lde_id+"&name="+login_name+"&status="+status+"&jns="+jns+"&idx="+idx;
					authorized=false;				
					currentUser = new User();
					currentUser.setLus_id(lus_id);
					currentUser.setPass("Reklaim1234");
					currentUser.setLde_id(lde_id);
					currentUser.setName(login_name);
					currentUser.setLca_id("01");
					currentUser.setEmail(email);
					currentUser.setLde_id("01");
					currentUser.setLus_nik("123445566");
					session.setAttribute("currentUser", currentUser);
				}else {
					redirect = contextPath + "/include/page/blocked.jsp?jenis=process";
				}
			}
			if(ServletRequestUtils.getStringParameter(request, "window", "").indexOf("aksepsplb")>-1){
				
				int akses = 0;
				if("1".equals(props.getProperty("use.token"))) {
					String tkey = request.getParameter("tkey");
					if(!StringUtils.isEmpty(tkey)) {
						akses = uwManager.validateUrlKey(tkey, url);
					}
				}else {
					akses = 1;
				}
				
				if(akses == 1) {
					String ld=request.getParameter("id");
					String rs=request.getParameter("spaj");				
					String email=request.getParameter("email");
					String lde_id=request.getParameter("lde");
					String login_name=request.getParameter("name");
					redirect = contextPath+"/uw/uw.htm?window=aksepsplb&id="+ld+"&spaj="+rs+"&email="+email+"&lde="+lde_id+"&name="+login_name;
					authorized=false;				
					currentUser = new User();
					currentUser.setLus_id(ld);
					currentUser.setPass("AksepLB1234");
					currentUser.setLde_id(lde_id);
					currentUser.setName(login_name);
					currentUser.setLca_id("01");
					currentUser.setEmail(email);
					currentUser.setLde_id("01");
					currentUser.setLus_nik("123445566");
					session.setAttribute("currentUser", currentUser);
				}else {
					redirect = contextPath + "/include/page/blocked.jsp?jenis=process";
				}
			}
			if(ServletRequestUtils.getStringParameter(request, "window", "").indexOf("sphlions")>-1){
				
				int akses = 0;
				if("1".equals(props.getProperty("use.token"))) {
					String tkey = request.getParameter("tkey");
					if(!StringUtils.isEmpty(tkey)) {
						akses = uwManager.validateUrlKey(tkey, url);
					}
				}else {
					akses = 1;
				}
				
				if(akses == 1) {
					authorized = false;
					request.getSession().removeAttribute("currentUser");
					String spaj = ServletRequestUtils.getStringParameter(request, "reg_spaj");
					String path_file = ServletRequestUtils.getStringParameter(request, "path_file");
					String nama_file = ServletRequestUtils.getStringParameter(request, "nama_file");
					String ttd = ServletRequestUtils.getStringParameter(request, "ttd");//0 = mieyoen 1 = ety
	//				Type sudah tidak dipakai lagi, berdasarkan nama file, apabila SPH2 berarti SPH yang Half, selain itu SPH yang full.
	//				Integer type = ServletRequestUtils.getIntParameter(request, "type");//1 : jenis SPH non logo AJSMSIG, 2 : jenis SPH logo AJSMSIG
					redirect = contextPath+"/uw/uw.htm?window=sphlions&reg_spaj="+spaj+"&path_file="+path_file+"&nama_file="+nama_file+"&ttd="+ttd;
					currentUser = new User();
					currentUser.setLca_id("01");
					currentUser.setLus_id("0");
					currentUser.setPass("Sph123456");
					currentUser.setLde_id("07");
					currentUser.setLus_admin(0);
					currentUser.setLus_nik("123456789");
					session.setAttribute("currentUser", currentUser);
				}else {
					redirect = contextPath + "/include/page/blocked.jsp?jenis=process";
				}
			}
			
			/* closed, not used anymore
			if(ServletRequestUtils.getStringParameter(request, "window", "").indexOf("aksepEmailBlast")>-1){
				String spaj=request.getParameter("spaj");
				String flag=request.getParameter("flag");
				redirect = contextPath + "/uw/uw.htm?window=aksepEmailBlast&spaj="+spaj+"&flag="+flag;
				authorized = false;
				request.getSession().removeAttribute("currentUser");
				currentUser = new User();
				currentUser.setLca_id("01");
				currentUser.setLus_id("2661");
				currentUser.setPass("aksepEmailBlast1");
				currentUser.setLde_id("07");
				currentUser.setLus_admin(0);
				currentUser.setLus_nik("123456789");
				session.setAttribute("currentUser", currentUser);
			}
			*/
			
			/* closed, not used anymore
			if(ServletRequestUtils.getStringParameter(request, "window", "").indexOf("aksepEmailBlastNew")>-1){
				String s_id1 = ServletRequestUtils.getStringParameter(request, "ID1", "");
				String s_id2 = ServletRequestUtils.getStringParameter(request, "ID2", "");
				
				redirect = contextPath + "/uw/uw.htm?window=aksepEmailBlastNew&id1="+s_id1+"&id2="+s_id2;
				authorized = false;
				request.getSession().removeAttribute("currentUser");
				currentUser = new User();
				currentUser.setLca_id("01");
				currentUser.setLus_id("2661");
				currentUser.setPass("aksepEmailBlast123456");
				currentUser.setLde_id("07");
				currentUser.setLus_admin(0);
				currentUser.setLus_nik("123456789");
				session.setAttribute("currentUser", currentUser);
			}
			*/
			//helpdesk [133348] email validasi transaksi Direksi/ Dept Head
			if(ServletRequestUtils.getStringParameter(request, "window", "").indexOf("paymentintegration")>-1){
				
				int akses = 0;
				if("1".equals(props.getProperty("use.token"))) {
					String tkey = request.getParameter("tkey");
					if(!StringUtils.isEmpty(tkey)) {
						akses = uwManager.validateUrlKey(tkey, url);
					}
				}else {
					akses = 1;
				}
				
				if(akses == 1) {
					System.out.println("akses : "+akses);
					String lus_id=request.getParameter("id");
					String login_name=request.getParameter("name");
					String reg_spaj=request.getParameter("spaj");
					String jns=request.getParameter("jns");
					String email=request.getParameter("email");
					String lde_id=request.getParameter("lde");
					redirect = contextPath+"/uw/uw.htm?window=paymentintegration&id="+lus_id+"&spaj="+reg_spaj+"&email="+email+"&lde="+lde_id+"&name="+login_name+"&jns="+jns;
					authorized=false;				
					currentUser = new User();
					currentUser.setLus_id(lus_id);
					currentUser.setPass("Reklaim1234");
					currentUser.setLde_id(lde_id);
					currentUser.setName(login_name);
					currentUser.setLca_id("01");
					currentUser.setEmail(email);
					currentUser.setLde_id("01");
					currentUser.setLus_nik("123445566");
					session.setAttribute("currentUser", currentUser);
				}else {
					redirect = contextPath + "/include/page/blocked.jsp?jenis=process";
				}
			}
			//helpdesk [133348] email validasi transaksi Direksi/ Dept Head
			if(ServletRequestUtils.getStringParameter(request, "window", "").indexOf("paymentintegrationaksep")>-1){
				
				int akses = 0;
				if("1".equals(props.getProperty("use.token"))) {
					String tkey = request.getParameter("tkey");
					if(!StringUtils.isEmpty(tkey)) {
						akses = uwManager.validateUrlKey(tkey, url);
					}
				}else {
					akses = 1;
				}
				
				if(akses == 1) {
					System.out.println("akses : "+akses);
					String lus_id=request.getParameter("id");
					String batch_no=request.getParameter("batch_no");
					String email=request.getParameter("email");
					String lde_id=request.getParameter("lde");
					String login_name=request.getParameter("name");
					String status=request.getParameter("status");
					String ls_jenis=request.getParameter("ls_jenis");
					String idx_aksep=request.getParameter("idx");
					redirect = contextPath+"/uw/uw.htm?window=paymentintegrationaksep&id=" + lus_id + "&batch_no=" + batch_no + "&email=" + email + "&lde=" + lde_id + "&name=" + login_name + "&status=" + status + "&ls_jenis=" + ls_jenis + "&idx=" + idx_aksep;
					authorized=false;				
					currentUser = new User();
					currentUser.setLus_id(lus_id);
					currentUser.setPass("Reklaim1234");
					currentUser.setLde_id(lde_id);
					currentUser.setName(login_name);
					currentUser.setLca_id("01");
					currentUser.setEmail(email);
					currentUser.setLde_id("01");
					currentUser.setLus_nik("123445566");
					session.setAttribute("currentUser", currentUser);
				}else {
					redirect = contextPath + "/include/page/blocked.jsp?jenis=process";
				}
			}
		} 
		
		
		/* closed, wrong url
		if(requestUri.indexOf("/spajonline.htm") > -1){
			request.getSession().removeAttribute("currentUser");
			redirect = contextPath + "/bac/bac.htm";
			authorized = false;
			currentUser = new User();
			currentUser.setLca_id("01");
			currentUser.setLus_id("2661");//User SPAJ online
			currentUser.setPass("Spaj1234");
			currentUser.setLde_id("01");
			currentUser.setLus_admin(0);
			currentUser.setLus_bas(0);
			currentUser.setCab_bank("");
			currentUser.setJn_bank(-1);
			currentUser.setMsag_id_ao(request.getParameter("kodeAgent"));
			currentUser.setLus_nik("123456");
			session.setAttribute("currentUser", currentUser);
		}
		*/
		
		/* closed, not accessed everywhere 
		if(requestUri.indexOf("/biodataonline.htm") > -1){
			
			authorized = false;
			currentUser = new User();
			currentUser.setLca_id("01");
			currentUser.setLus_id("2661");//User SPAJ online
			currentUser.setPass("Biodata123");
			currentUser.setLde_id("01");
			currentUser.setLus_admin(0);
			currentUser.setLus_bas(0);
			currentUser.setCab_bank("");
			currentUser.setJn_bank(-1);
			//jangan lupa di encrypt
			currentUser.setMsag_id_ao(request.getParameter("kodeAgent"));
			currentUser.setLus_nik("123456");
			if(request.getParameter("kodeAgent")!=null){
				request.getSession().removeAttribute("currentUser");
				redirect = contextPath + "/rekruitment/rekrut.htm";
				session.setAttribute("currentUser", currentUser);
			}
			
		}
		*/
		
		/**
		 * Interceptor untuk hadiah dan stable save program hadiah
		 * @author alfian_h
		 * 
		 * http://localhost/E-Lions/bas/hadiah.htm?window=json&t=transf&_spaj=01200700284&lspdID=86&mh_no=1
		 */
		if(requestUri.indexOf("bas/hadiah.htm") > -1 && currentUser == null){
			
			if(ServletRequestUtils.getStringParameter(request, "window","").indexOf("upload")>-1){
				
				int akses = 0;
				if("1".equals(props.getProperty("use.token"))) {
					String tkey = request.getParameter("tkey");
					if(!StringUtils.isEmpty(tkey)) {
						akses = uwManager.validateUrlKey(tkey, url);
					}
				}else {
					akses = 1;
				}
				
				if(akses == 1) {
					String spaj = ServletRequestUtils.getStringParameter(request, "_spaj");
					int pos = ServletRequestUtils.getRequiredIntParameter(request, "lspdID");
					int mh = ServletRequestUtils.getRequiredIntParameter(request, "mh_no");
					
					String path = contextPath + "/bas/hadiah.htm?window=upload&t=upPath&_spaj="+spaj+"&lspdID="+pos+"&mh_no="+mh;
					logger.info("Path "+path);
	//				currentUser = new User();
	//				currentUser.setLca_id("01");
	//				currentUser.setLus_id("0");
	//				currentUser.setPass("123456");
	//				currentUser.setLde_id("15");
	//				currentUser.setPathHadiah(path);
	//				logger.info("Path Hadiah currentUser " +currentUser.getPathHadiah());
	//				session.setAttribute("currentUser", currentUser);
					session.setAttribute("pathHadiah", path);
					//logger.info("Path Hadiah Session"+session.getAttribute("pathHadiah"));
					//authorized = true;
					redirect = contextPath + "/common/login.htm";
				}else {
					redirect = contextPath + "/include/page/blocked.jsp?jenis=process";
				}
			}
			
			if(ServletRequestUtils.getStringParameter(request, "window","").indexOf("reschedule")>-1){
				
				int akses = 0;
				if("1".equals(props.getProperty("use.token"))) {
					String tkey = request.getParameter("tkey");
					if(!StringUtils.isEmpty(tkey)) {
						akses = uwManager.validateUrlKey(tkey, url);
					}
				}else {
					akses = 1;
				}
				
				if(akses == 1) {
					String hadiah_spaj = ServletRequestUtils.getStringParameter(request, "hadiah_spaj");
					String path = contextPath + "/bas/hadiah.htm?window=reschedule&hadiah_spaj="+hadiah_spaj;
					
					redirect = path;
					authorized = true;
					currentUser = new User();
					currentUser.setLca_id("01");
					currentUser.setLus_id("0");
					currentUser.setPass("LIons123456");
					currentUser.setLde_id("07");
					currentUser.setLus_admin(0);
					currentUser.setLus_nik("123456789");
					session.setAttribute("currentUser", currentUser);
				}else {
					redirect = contextPath + "/include/page/blocked.jsp?jenis=process";
				}
			}
		}
		
		/*
		if(requestUri.indexOf("/mallnon.htm") > -1) { 
				request.getSession().removeAttribute("currentUser");
//					request.getSession().invalidate();
//				UserSession.logout(request);
				redirect = contextPath + "/bac/bacmall.htm";
				authorized = false;
				currentUser = new User();
				currentUser.setLca_id("58");
				currentUser.setLus_id("2326");
				currentUser.setPass("MallNoaa123");
				currentUser.setLde_id("01");
				currentUser.setLus_admin(0);
				currentUser.setLus_bas(0);
				currentUser.setCab_bank("");
				currentUser.setJn_bank(-1);
				Tamu tamu = uwManager.selectMstTamu(request.getParameter("a"));
				currentUser.setMall_mspo_plan_provider(request.getParameter("a"));
				currentUser.setMall_email(tamu.getEmail());
				currentUser.setMall_hp(tamu.getNo_hp());
				currentUser.setMall_kd_area_telp(tamu.getKode_area_telp());
				currentUser.setMall_lcb_no(tamu.getLcb_no());
				currentUser.setMall_msag_id(tamu.getMsag_id());
				currentUser.setMall_nama_penutup(tamu.getNama_penutup());
				currentUser.setMall_nama_pp(tamu.getNama_tamu());
				currentUser.setMall_telp(tamu.getNo_telp());
				currentUser.setMall_tgl_lhr_pp(tamu.getTgl_lahir());
				Datausulan planUtama = uwManager.selectMstProposal(request.getParameter("a"));
				currentUser.setData_usulan(planUtama);
				currentUser.setLus_nik("123456789");
				List<Datausulan> planRider = uwManager.selectMstProposalRider(request.getParameter("a"));
				if(planUtama!=null){
					currentUser.getData_usulan().setFlag_include(0);
					currentUser.getData_usulan().setDaftaRider(planRider);					
				}
				
				session.setAttribute("currentUser", currentUser);
		}
		*/
		
		/* closed, not accessed everywhere 
		if(requestUri.indexOf("/cflentry.htm") > -1) { 
			request.getSession().removeAttribute("currentUser");
//				request.getSession().invalidate();
//			UserSession.logout(request);
			redirect = contextPath + "/bac/editcfl.htm";
			authorized = false;
			currentUser = new User();
			currentUser.setLca_id("01");
			currentUser.setLus_id("2647");
			currentUser.setPass("CfL200704");
			currentUser.setLde_id("19");
			currentUser.setLus_admin(0);
			currentUser.setLus_bas(0);
			currentUser.setCab_bank("");
			currentUser.setJn_bank(-1);
			currentUser.setLus_nik("123456789");
			
			session.setAttribute("currentUser", currentUser);
		}
		*/
		
		if(requestUri.indexOf("/snowsTransfer.htm") > -1) { 
			
			int akses = 0;
			if("1".equals(props.getProperty("use.token"))) {
				String tkey = request.getParameter("tkey");
				if(!StringUtils.isEmpty(tkey)) {
					akses = uwManager.validateUrlKey(tkey, url);
				}
			}else {
				akses = 1;
			}
			
			if(akses == 1) {
				request.getSession().removeAttribute("currentUser");
				redirect = contextPath + "/common/menu.htm?frame=main";
				String reg_spaj = request.getParameter("a");
				String lus_id = request.getParameter("b");
				currentUser = new User();
				currentUser.setLus_id(lus_id);
				currentUser.setLus_nik("123456789");
				currentUser = elionsManager.selectLoginAuthenticationByLusId(currentUser);
				session.setAttribute("snows_reg_spaj", reg_spaj);
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				currentUser.setScreenWidth(new Double (dim.getWidth()).intValue());
				currentUser.setScreenHeight(new Double (dim.getHeight()).intValue());
		        double angka = (double) dim.getWidth() / (double) dim.getHeight();
		        if(angka > 1.4) {
		        	currentUser.setWideScreen(true);
		        }else {
		        	currentUser.setWideScreen(false);
		        }
				//currentUser.setPass(uwManager.selectPasswordFromUsername(currentUser.getName()));
				if(currentUser.getCab_bank()==null)currentUser.setCab_bank("");
				session.setAttribute("currentUser", currentUser);
				authorized = false;
			}else {
				redirect = contextPath + "/include/page/blocked.jsp?jenis=process";
			}
		}
		
		/**
		 * Interceptor untuk hadiah dan stable save program hadiah
		 * @author alfian_h
		 * 
		 * http://localhost/E-Lions/bas/hadiah.htm?window=json&t=transf&_spaj=01200700284&lspdID=86&mh_no=1
		 */
		/* closed, double, sudah ada diatas
		if(requestUri.indexOf("bas/hadiah.htm") > -1 && currentUser == null){
			
			if(ServletRequestUtils.getStringParameter(request, "window","").indexOf("upload")>-1){
				String spaj = ServletRequestUtils.getStringParameter(request, "_spaj");
				int pos = ServletRequestUtils.getRequiredIntParameter(request, "lspdID");
				int mh = ServletRequestUtils.getRequiredIntParameter(request, "mh_no");
				
				String path = contextPath + "/bas/hadiah.htm?window=upload&t=upPath&_spaj="+spaj+"&lspdID="+pos+"&mh_no="+mh;
				logger.info("Path "+path);
//				currentUser = new User();
//				currentUser.setLca_id("01");
//				currentUser.setLus_id("0");
//				currentUser.setPass("123456");
//				currentUser.setLde_id("15");
//				currentUser.setPathHadiah(path);
//				logger.info("Path Hadiah currentUser " +currentUser.getPathHadiah());
//				session.setAttribute("currentUser", currentUser);
				session.setAttribute("pathHadiah", path);
				//logger.info("Path Hadiah Session"+session.getAttribute("pathHadiah"));
				//authorized = true;
				redirect = contextPath + "/common/login.htm";
			}
			
			if(ServletRequestUtils.getStringParameter(request, "window","").indexOf("reschedule")>-1){
				String hadiah_spaj = ServletRequestUtils.getStringParameter(request, "hadiah_spaj");
				String path = contextPath + "/bas/hadiah.htm?window=reschedule&hadiah_spaj="+hadiah_spaj;
				
				redirect = path;
				authorized = true;
				currentUser = new User();
				currentUser.setLca_id("01");
				currentUser.setLus_id("0");
				currentUser.setPass("123456");
				currentUser.setLde_id("07");
				currentUser.setLus_admin(0);
				currentUser.setLus_nik("123456789");
				session.setAttribute("currentUser", currentUser);
			}
		}
		*/
		
		/**
		 * Interceptor untuk Input keterangan upload scan atau transfer spaj ke mst_position_spaj
		 * @author canpri
		 * 
		 * http://localhost/E-Lions/bas/spaj.htm?window=ket_upload_transfer?id=2998&spaj=01201300199
		 */
		if(requestUri.indexOf("bas/spaj.htm") > -1 && currentUser == null){
			if(ServletRequestUtils.getStringParameter(request, "window","").indexOf("ket_upload_transfer")>-1){
				
				int akses = 0;
				if("1".equals(props.getProperty("use.token"))) {
					String tkey = request.getParameter("tkey");
					if(!StringUtils.isEmpty(tkey)) {
						akses = uwManager.validateUrlKey(tkey, url);
					}
				}else {
					akses = 1;
				}
				
				if(akses == 1) {
					String spaj = ServletRequestUtils.getStringParameter(request, "spaj","");
					String lus_id = ServletRequestUtils.getStringParameter(request, "id","");
					
					redirect = contextPath + "/bas/spaj.htm?window=ket_upload_transfer&id="+lus_id+"&spaj="+spaj;
					currentUser = new User();
					currentUser.setLus_id(lus_id);
					currentUser = elionsManager.selectLoginAuthenticationByLusId(currentUser);
					currentUser.setPass(uwManager.selectPasswordFromUsername(currentUser.getName()));
					session.setAttribute("currentUser", currentUser);
					authorized = false;
				}else {
					redirect = contextPath + "/include/page/blocked.jsp?jenis=process";
				}
			}
		}
		
		/**
		 * Interceptor untuk Akseptasi permintaan brosur
		 * @author canpri
		 * 
		 * http://localhost/E-Lions/bas/cabang.htm?window=approveBrosurByEmail&app=1&msf_id=
		 */
		if(requestUri.indexOf("bas/cabang.htm") > -1 && currentUser == null){
			if(ServletRequestUtils.getStringParameter(request, "window","").indexOf("approveBrosurByEmail")>-1){
				
				int akses = 0;
				if("1".equals(props.getProperty("use.token"))) {
					String tkey = request.getParameter("tkey");
					if(!StringUtils.isEmpty(tkey)) {
						akses = uwManager.validateUrlKey(tkey, url);
					}
				}else {
					akses = 1;
				}
				
				if(akses == 1) {
					String msf_id = ServletRequestUtils.getStringParameter(request, "msf_id","");
					String app = ServletRequestUtils.getStringParameter(request, "app","1");
					
					redirect = contextPath + "/bas/cabang.htm?window=approveBrosurByEmail&app="+app+"&msf_id="+msf_id;
					currentUser = new User();
					currentUser.setLus_id("3041");//PRATIDINA INDAH SAFITRI
					currentUser = elionsManager.selectLoginAuthenticationByLusId(currentUser);
					currentUser.setPass(uwManager.selectPasswordFromUsername(currentUser.getName()));
					session.setAttribute("currentUser", currentUser);
					authorized = false;
				}else {
					redirect = contextPath + "/include/page/blocked.jsp?jenis=process";
				}
			}
		}
		
		// login dari SSO
		if(requestUri.indexOf("/login_model.htm") > -1 && currentUser == null){
			redirect = contextPath + "/common/login_model.htm?ac="+ac;
			authorized = false;
			return true;
		}
		
		//khusus viewer ss boleh
		if(requestUri.indexOf("printpolis")>-1){
			if(ServletRequestUtils.getStringParameter(request, "window", "").indexOf("viewer_ss")>-1) {
				return true;
			}
		}
		
		//khusus link dari EAS, boleh
		if(ServletRequestUtils.getStringParameter(request, "eas", "").equals("1")) {
			return true;
		}
		
		// Khusus IB BSIM dari EWS
		if(ServletRequestUtils.getStringParameter(request, "bsmib", "").equals("1")) {
		    return true;
		}
		
		/* closed, not accessed everywhere 
		if(ServletRequestUtils.getStringParameter(request, "window", "").indexOf("printfromcfl")>-1){
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj","");
			redirect = contextPath +"/uw/printpolis.htm?window=printfromcfl&spaj="+spaj;
			currentUser = new User();
			authorized=true;				
			currentUser = new User();				
			currentUser.setLus_id("0");
			currentUser.setPass("Ajs12345");
			//currentUser.setLca_id("01");				
			currentUser.setLde_id("11");
			currentUser.setLus_admin(0);	
			currentUser.setJn_bank(-1);
			currentUser.setLus_nik("123456789");
			session.setAttribute("currentUser", currentUser);
		}
		*/
		
		
		if (authorized) {
			if(currentUser != null) {
				NDC.push("["+requestUri + "]"+"["+currentUser.getLus_id()+"/"+currentUser.getLca_id()+"/"+currentUser.getLde_id()+"/"+currentUser.getLdi_id());
				AksesHist a = new AksesHist();
				a.setLus_id(Integer.valueOf(currentUser.getLus_id()));
				a.setMsah_date(new Date());
				a.setMsah_jenis(1);
				a.setMsah_ip(currentUser.getIp());
				a.setJenis_id(13);
				a.setMsah_spaj(
						ServletRequestUtils.getStringParameter(request, "reg_spaj", 
								ServletRequestUtils.getStringParameter(request, "spaj", 
										ServletRequestUtils.getStringParameter(request, "editSPAJ", 
												ServletRequestUtils.getStringParameter(request, "showSPAJ", "")))));
				a.setMsah_uri(requestUri.substring(requestUri.indexOf(contextPath)+contextPath.length()));
				//if(!a.getMsah_spaj().equals("")) 
					elionsManager.insertAksesHist(a);
			}
			return authorized;
		} else {
			response.sendRedirect(redirect);
			return authorized;
		}

	}

	//Post-Request Method
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object command, ModelAndView errors)
			throws Exception {
		if(logger.isDebugEnabled())
			logger.debug("~~~ ACCESSING URI = " + request.getRequestURL());
			
		NDC.pop();
	}

}