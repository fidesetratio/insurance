package com.ekalife.utils;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.ekalife.elions.service.ElionsManager;

import id.co.sinarmaslife.std.util.DateUtil;
import id.co.sinarmaslife.std.util.StringUtil;
/**
 * Class berisi fungsi-fungsi umum, seperti generate menu, generate xml
 * 
 * @author Yusuf
 * @since 01/11/2005
 */
public class Common implements Serializable {
	protected final static Log logger = LogFactory.getLog( Common.class );
	private static final long serialVersionUID = 6370080722430452621L;
	private static List<Map> result;
	private static Map<String, String> map;
	
	public static Map<String, List> XML_CACHE;

	/**
	 * Fungsi rekursif untuk mengambil exception paling atas (sumber dari exception), 
	 * agar tahu errornya apa, dan jelas error message nya
	 * 
	 * @author Yusuf
	 * @since 6 Feb 2012
	 * @param ex exception yang di throw
	 * @return exception parent nya
	 */
	public static Throwable getRootCause(Throwable ex){
		Throwable bapak = ex.getCause();
		if(bapak != null) return getRootCause(bapak);
		else return ex;
	}
	
	/**
	 * Get Mime Type from file
	 * 
	 * @author Yusuf
	 * @since Mar 22, 2011 (1:28:25 PM)
	 *
	 * @param f
	 * @return
	 */
	public static String getMimeType(File f){
		//http://stackoverflow.com/questions/51438/getting-a-files-mime-type-in-java
		MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
		return mimeTypesMap.getContentType(f);
		//http://www.rgagnon.com/javadetails/java-0487.html
		//return new MimetypesFileTypeMap().getContentType(f);
	}
	
	//Yusuf (14 Sep 2011) - Selalu ambil tahun terbaru
	public static String getCopyrightYear(){
		int first = 2005;
		int now = Calendar.getInstance().get(Calendar.YEAR);
		if(first == now) return String.valueOf(now);
		else return first + "-" + now;
	}
	
	//Yusuf (8 Jun 2011) - Show System Properties
    public static void printSystemProperties(String[] args) {
        for (Map.Entry<Object, Object> e : System.getProperties().entrySet()) {
            logger.info(e);
        }
    }	
	
    private static void treeWalk(Element element) {
        for ( int i = 0, size = element.nodeCount(); i < size; i++ ) {
            Node node = element.node(i);
            if ( node instanceof Element ) {
            	Element e = (Element) node;
            	if(e.getName().equals("Position")) {
            		if(map!=null) result.add(map);
            		map = new HashMap<String, String>();
            	}else map.put(e.getName(), e.getTextTrim());
                treeWalk( (Element) node );
            }
        }
    }
    
    private static String treeSearch(String key, String col, String val, Element element) {
        for ( int i = 0, size = element.nodeCount(); i < size; i++ ) {
            Node node = element.node(i);
            if ( node instanceof Element ) {
            	Element e = (Element) node;
            	if(e.getName().equals("Position")) {
            		if(e.elementTextTrim(key).equals(val)) {
            			return e.elementTextTrim(col);
            		}
            	}else return treeSearch( key, col, val, (Element) node );
            }
        }
        return null;
    }
    
	/**
	 * Fungsi untuk search suatu nilai dari file XML (untuk yang Positions)
	 * 
	 * @param namaFile
	 *            namaFile lengkapnya (tanpa path, case sensitive), contohnya: WARGANEGARA.xml
	 * @param colKey
	 * 			kolom primary key nya
	 * @param colName
	 * 			kolom yang dicari
	 * @param value
	 * 			nilai yg dibandingkan
	 * @return String yang berisi data dari file xml tersebut
	 * @see File XML di folder /xml
	 */
    public static String searchXml(String namaFile, String colKey, String colName, Object value) {//, HttpServletRequest request){
        
    	SAXReader reader = new SAXReader();
        Document document;
		try {
			document = reader.read(namaFile);
			if (value==null)
			{
				return "";
			}else{
				return treeSearch(colKey, colName, value.toString(), document.getRootElement());
			}
		} catch (Exception e) {
			logger.error("ERROR :", e);
			return null;
		}
	}

	/**
	 * Fungsi untuk men-generate list dari file XML (untuk yang Positions)
	 * 
	 * @param namaFile
	 *            namaFile lengkapnya (tanpa path, case sensitive), contohnya: WARGANEGARA.xml
	 * @param sortColumn
	 * 			kolom untuk pengurutannya
	 * @param request
	 * 			HttpServletRequest, yang digunakan untuk mengambil contextpath
	 * @return List yang berisi data dari file xml tersebut
	 * @see File XML di folder /xml
	 * @deprecated Non ThreadSafe method, please change to {@link DroplistManager}
	 */
    @SuppressWarnings("unchecked")
	public static List xmlToList(String namaFile, String sortColumn, HttpServletRequest request){
		try {
			result = new ArrayList<Map>();
			result = getXmlCache(namaFile);
			
			if(result.size() == 0) {
				SAXReader reader = new SAXReader();
		        Document document;
				document = reader.read(request.getSession().getServletContext().getResource("/xml/" + namaFile).toString());
				
				map = null;
		        treeWalk(document.getRootElement());
		        if(map!=null) result.add(map);	        
		        Collections.sort(result, new FieldComparator(sortColumn));
		        
		        setXmlCache(namaFile, result);
			}
	        return result;
		} catch (DocumentException e) {
			logger.error("ERROR :", e);
			return null;
		} catch (MalformedURLException e) {
			logger.error("ERROR :", e);
			return null;
		}
	}
    
    public static void setXmlCache(String namaCache, List result) {
    	try {
    		XML_CACHE.put(namaCache, result);
    	}catch (Exception e) {
			logger.error("ERROR", e);
		}
    }
    
    public static List getXmlCache(String namaCache) {
    	List list = new ArrayList();
    	
    	try {
    		if(XML_CACHE == null) {
    			XML_CACHE = new HashMap<String, List>();
    		}
    		list = XML_CACHE.get(namaCache);
    		if(list == null) {
    			list = new ArrayList();
    		}
    	}catch (Exception e) {
			logger.error("ERROR", e);
		}
    	
    	return list;
    }

	/**
	 * Fungsi untuk men-generate menu dari database
	 * 
	 * @param path
	 *            context path dari aplikasi
	 * @param mgr
	 *            TransactionManager untuk menjalankan perintah SQL
	 * @return String javascript untuk generate menu (memakai Milonic untuk
	 *         javascriptnya)
	 * @see www.milonic.com
	 */
	@SuppressWarnings("unchecked")
	public static String generateJavascriptMenu(String lus_id, String path, ElionsManager mgr, String path_menu) {
		SortedMap bapak = new TreeMap();
		List parents = mgr.selectMenu("IN");
//		List childs= mgr.selectAllUserMenu();
		List childs= mgr.selectAllUserMenuWithAccessRights(Integer.parseInt(lus_id));
		StringBuffer result = new StringBuffer();
		String ikon="";
		
		for(int i=0; i<parents.size(); i++) {
			Map parent = (HashMap) parents.get(i);
			StringBuffer tmp = 
				new StringBuffer("with(milonic=new menuname(\"menu"+parent.get("MENU_ID")+"\")){\n"
					+(parent.get("TINGKAT").toString().equals("0")?
							"alwaysvisible=1;left=0;margin=2;orientation=\"horizontal\";style=XPMainStyle;top=0;aI(\"status=Menu;text=Menu;clickfunction=openIFrame('mainFrame','"+path+path_menu+"');createLoadingMessage();\");\n":
								"margin=2;overflow=\"scroll\";style=XPMenuStyle;"));
			bapak.put(parent.get("MENU_ID"), tmp);
		}
		for(int i=0; i<childs.size(); i++) {
			Map child = (HashMap) childs.get(i);
			StringBuffer tmp = (StringBuffer) bapak.get(child.get("PARENT_MENU_ID"));
			if(child.get("ICON")!=null) ikon = "image="+path+"/"+child.get("ICON")+";";
			else ikon="";
			
			if(bapak.containsKey(child.get("MENU_ID"))) {
				tmp.append( 
					"aI(\""+ikon+"showmenu=menu"+child.get("MENU_ID")+";text="+child.get("NAMA_MENU")+";\");\n");
			}else {
				tmp.append(
					"aI(\""+ikon+"status="+child.get("NAMA_MENU")+";text="+child.get("NAMA_MENU")+";"
					+(
						child.get("NAMA_MENU").equals("Logout")?
								"clickfunction=log_out('"+path+"/"+child.get("LINK_MENU")+"');":
									(!child.get("LINK_MENU").equals("DISABLED")?
											//"url=javascript:mm_openUrl('your_page.htm');createLoadingMessage();" : 
											"clickfunction=openIFrame('mainFrame','"+path+"/"+child.get("LINK_MENU")+"');createLoadingMessage();":
												"clickfunction=alert('Maaf tetapi menu ini belum dapat diakses.');")
					)
					+"\");\n");
			}
			bapak.remove(child.get("PARENT_MENU_ID"));
			bapak.put(child.get("PARENT_MENU_ID"), tmp);
		}

		for(Iterator it = bapak.keySet().iterator(); it.hasNext();) {
			result.append(bapak.get(it.next()).toString()+"}\n");
		}
		
		
		
		return result.toString();
	}
	
	/**
	 * Fungsi untuk mengecek apakah suatu object kosong/null atau tidak
	 * @param cek Object yang akan di cek (dapat berupa String/Integer/Long/Double/Map/List/ etc...)
	 * @return boolean true apabila kosong, false bila tidak
	 */	
	public static boolean isEmpty(Object cek) {
		if(cek==null) return true;
		else	if(cek instanceof String) {
			String tmp = (String) cek;
				if(tmp.trim().equals("")) return true;
				else return false;
		}else if(cek instanceof List) {
			List tmp = (List) cek;
			return tmp.isEmpty();
		}else if(cek instanceof Map){
			return ((Map) cek).isEmpty();
		}else if(cek instanceof Integer || cek instanceof Long|| cek instanceof Double|| cek instanceof Float|| cek instanceof BigDecimal || cek instanceof Date){
			return false;
		}
		return true;
	}
	
	public static Object getBean(ServletContext servletContext, String beanName) {
		//org.springframework.web.context.WebApplicationContext.ROOT
		//org.springframework.web.servlet.FrameworkServlet.CONTEXT.spring
		XmlWebApplicationContext context = (XmlWebApplicationContext) servletContext.getAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.spring");		
		return context.getBean(beanName);
	}
	
	public static void printRequestParameters(HttpServletRequest request) {
		Enumeration params = request.getParameterNames();
		while(params.hasMoreElements()) {
			String name = (String) params.nextElement();
			logger.info(name + " = " + request.getParameter(name));
		}
	}
	
	public static boolean validPhone(String no){
		 boolean isNumberFlag=false;
		 String [] splitNo=no.trim().split("");
		
		for (int i = 0; i < splitNo.length; i++) {
			String c=splitNo[i];
			if(i==1){
				if(c.equals("+"))c="0";
			}
			
			char[] x=c.toCharArray();
			 
			 for (int j = 0; j < x.length; j++) {
	           	 char y =x[j];
	           	 if ((y >= '0') && (y <= '9')){ // numeric
	            	 isNumberFlag=true;
	            	 continue;
	             }else{
	            	 return false;
	             }
	        }
			
		}
		
		return isNumberFlag;
	}
	
	public static ArrayList serializableList(List dataList){
		if(dataList!=null){
			return new ArrayList(dataList);
		}else{
			return null;
		}
	}
	
	public static HashMap serializableMap(Map dataMap){
		if(dataMap!=null){
			return new HashMap(dataMap);
		}else{
			return null;
		}
	}
	
	public static String isPasswordValid(String password, String oldPassword, Date changePassword){
//		boolean valid = false;
		String error = "";
		
//		String numberLine = "124567890";
		
		int expDays = 90;
		int minChar = 8;
//		int minNum = 1;
//		int minUp = 1;
//		int minLow = 1;
		boolean minNum = false;
		boolean minUp = false;
		boolean minLow = false;
		
		if(!StringUtil.isEmpty(password)){
			int lnPass = password.length();
			//min char 8
			if(lnPass >= minChar){
				Date nowDate = new Date();
//				for(int i = 0 ; i < lnPass ; i++){
//					String c = password.charAt(i) + "";
//					//min 1 number
//					if(numberLine.contains(c))minNum--;
//					//min 1 uppercase
//					if(c.equals(c.toUpperCase()))minUp--;
//					//min 1 lowercase
//					if(c.equals(c.toLowerCase()))minLow--;
//				}
				
				minNum = password.matches("^(?=.*\\d).+$");
				minUp = password.matches("^(?=.*[A-Z]).+$");
				minLow = password.matches("^(?=.*[a-z]).+$");
				
				if(StringUtil.isEmpty(oldPassword))oldPassword = "";
				if(changePassword == null)changePassword = (Date) nowDate.clone();
				
//				if		(!(minNum <= 0))error = "Jumlah angka minimal/paling sedikit ada 1";
//				else if	(!(minLow <= 0))error = "Jumlah huruf kecil minimal/paling sedikit ada 1";
//				else if	(!(minUp <= 0))error = "Jumlah huruf besar minimal/paling sedikit ada 1";
//				else if	((password.equals(oldPassword)))error = "Password baru tidak boleh sama dengan password lama";
//				else if	(!(DateUtil.dateDifference(changePassword, nowDate, true) < expDays))error = "Password expired";
				
				if		(!minNum)error = "Jumlah angka minimal/paling sedikit ada 1";
				else if	(!minLow)error = "Jumlah huruf kecil minimal/paling sedikit ada 1";
				else if	(!minUp)error = "Jumlah huruf besar minimal/paling sedikit ada 1";
				else if	((password.equals(oldPassword)))error = "Password baru tidak boleh sama dengan password lama";
				else if	(!(DateUtil.dateDifference(changePassword, nowDate, false) < expDays))error = "Password expired";
				
//				if(!(
//						minNum <= 0 && 
//						minLow <= 0 && 
//						minUp <= 0 && 
//						!password.equals(oldPassword) && 
//						DateUtil.dateDifference(changePassword, nowDate, true) < expDays
//					)){
//					valid = true;
//				}
			}else{
				error = "Password minimal 8 karakter";
			}
		}
		
		return error;
	}
	
}
