package com.ekalife.elions.web.uw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.CariSpaj;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @author Yusuf
 *
 */
public class SpajController extends ParentController {
	protected final Log logger = LogFactory.getLog( getClass() );
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		String lssaId = ServletRequestUtils.getStringParameter(request, "lssaId", null);
		String lsspId = ServletRequestUtils.getStringParameter(request, "lssp_id", null);
		String msenaksep = ServletRequestUtils.getStringParameter(request, "msenaksep", null);
		String win = ServletRequestUtils.getStringParameter(request, "win", null);
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		map.put("user_id", lus_id);
		
		String cab_bank = "";
		Integer jn_bank = -1;
		Map data_valid = (HashMap) this.elionsManager.select_validbank(lus_id);
		if(data_valid != null){
			cab_bank = (String)data_valid.get("CAB_BANK");
			jn_bank = (Integer) data_valid.get("JN_BANK");
		}
		
		if(cab_bank == null){
			cab_bank = "";
		}
		map.put("cabang_bank", cab_bank);
		map.put("jn_bank", jn_bank);
		
		if(request.getParameter("search")!=null || request.getParameter("refresh")!=null ){
			String tgl_lahir = "";
			Integer centang = ServletRequestUtils.getIntParameter(request, "centang", 0);
			if(centang == 1) {
				tgl_lahir = request.getParameter("tgl_lahir");
			}
			
			 String noPage = "";
	         String noRow = "20";
	         if(request.getParameter("cPage") == null || request.getParameter("cPage") == "" ){
	         	noPage = "1";
	         }else{
	         	noPage = request.getParameter("cPage");
	         }
	         
	         String totalPage="";        
	         if("SSS,M35,M01".indexOf(user.getCab_bank().trim())>=0 && !user.getCab_bank().isEmpty()){
	        	 	totalPage  = bacManager.selectTotalFilterSpaj2SimasPrima(
								ServletRequestUtils.getRequiredStringParameter(request, "posisi"),
								request.getParameter("tipe").toString(), 
								request.getParameter("kata"), 
								ServletRequestUtils.getStringParameter(request, "pilter", "="), jn_bank, tgl_lahir );
			}else if(cab_bank.equalsIgnoreCase("")) {
				totalPage  =  bacManager.selectTotalFilterSpaj2(
							ServletRequestUtils.getRequiredStringParameter(request, "posisi"),
							request.getParameter("tipe").toString(), 
							request.getParameter("kata"), 
							ServletRequestUtils.getStringParameter(request, "pilter", "="), lssaId, lsspId, tgl_lahir);
			}else{
				totalPage  = bacManager.selectTotalFilterSpaj2_valid(
							ServletRequestUtils.getRequiredStringParameter(request, "posisi"),
							request.getParameter("tipe").toString(), 
							request.getParameter("kata"), 
							ServletRequestUtils.getStringParameter(request, "pilter", "="), lssaId, lus_id, lsspId, tgl_lahir);
			}
	         
	         
	         
	         // HANDLE PAGE LESS OR PAGE OVER
	         if(Integer.parseInt(noPage) < 1){
	        	 noPage = "1";
	         }
	         if(Integer.parseInt(noPage) > Integer.parseInt(totalPage)){
	          	noPage = totalPage;
	          }
			
	        Map params = new HashMap();		
	 		params.put("noRow", noRow);
	 		params.put("noPage", noPage);
	 		
//			if("SSS".equals(user.getCab_bank().trim()) || user.getJn_bank().intValue() == 3) {
			if("SSS,M35,M01".indexOf(user.getCab_bank().trim())>=0 && !user.getCab_bank().isEmpty()){
				map.put("listSpaj", 
						Common.serializableList(elionsManager.selectFilterSpaj2SimasPrima(
							ServletRequestUtils.getRequiredStringParameter(request, "posisi"),
							request.getParameter("tipe").toString(), 
							request.getParameter("kata"), 
							ServletRequestUtils.getStringParameter(request, "pilter", "="), jn_bank, tgl_lahir, params )));
			}else if(cab_bank.equalsIgnoreCase("")) {
				if(msenaksep==null){
					map.put("listSpaj", 
							Common.serializableList(elionsManager.selectFilterSpaj2(
								ServletRequestUtils.getRequiredStringParameter(request, "posisi"),
								request.getParameter("tipe").toString(), 
								request.getParameter("kata"), 
								ServletRequestUtils.getStringParameter(request, "pilter", "="), lssaId, lsspId, tgl_lahir, params)));
				}else{
					map.put("listSpaj", 
							Common.serializableList(elionsManager.selectFilterSpaj2(
								ServletRequestUtils.getRequiredStringParameter(request, "posisi"),
								request.getParameter("tipe").toString(), 
								request.getParameter("kata"), 
								ServletRequestUtils.getStringParameter(request, "pilter", "="), lssaId, lsspId, tgl_lahir, params)));					
				}
			}else{
				map.put("listSpaj", 
						Common.serializableList(elionsManager.selectFilterSpaj2_valid(
							ServletRequestUtils.getRequiredStringParameter(request, "posisi"),
							request.getParameter("tipe").toString(), 
							request.getParameter("kata"), 
							ServletRequestUtils.getStringParameter(request, "pilter", "="), lssaId, lus_id, lsspId, tgl_lahir, params)));
			}
			
			int currPage = Integer.parseInt(noPage); 
			request.setAttribute("currPage", noPage);
	    	request.setAttribute("firstPage", 1 + "");
	    	request.setAttribute("lastPage", totalPage);        	
	    	request.setAttribute("nextPage", (currPage + 1) + "");
	    	request.setAttribute("previousPage", (currPage - 1) + "");   
			
	    	int spajPega = 0;
	    	
			//tambahan query untuk menandai blacklist atau bukan
			List<CariSpaj> listSpaj = (List) map.get("listSpaj");
			ArrayList<CariSpaj> listShowSpaj = new ArrayList<CariSpaj>();			
			for(CariSpaj cariSpaj : listSpaj){
				List akses = user.getAksesCabang();
				String region = elionsManager.selectCabangFromSpaj_lar(cariSpaj.getReg_spaj());
				if("01,05,11,12,29,34,39,21".indexOf(user.getLde_id()) <0){
					for(int i=0;i<akses.size();i++){
						if (props.getProperty("access.viewer.region").indexOf(user.getLde_id()) >=0) {									
								if(akses.contains(region)){
									listShowSpaj.add(cariSpaj);	
									break;
								}
							
						}else{
							region = region.substring(0, 2);
							
							String lca = (String)akses.get(i).toString().substring(0, 2);						
							if(lca.equals(region)){
									listShowSpaj.add(cariSpaj);
									break;
							}
						}
					}					
				
				}				
				String bl = uwManager.selectCekBlacklist(cariSpaj.getReg_spaj());
				if(cariSpaj.getReg_spaj().equals(bl)){
					cariSpaj.setMcl_blacklist(1);
				}
				
				spajPega = bacManager.selectCekSpajNonPega(cariSpaj.getReg_spaj());
				
				//user2 siap2u agency masuk ke LDE_ID BAS, tp cuma bisa akses yang region siap2u
				if(("6318".equals(user.getLus_id()) || "6319".equals(user.getLus_id()) || "6320".equals(user.getLus_id()) ||
					"6321".equals(user.getLus_id()) || "6322".equals(user.getLus_id())) && "37M1".equalsIgnoreCase(region.substring(0,4)) ){ 
						listShowSpaj.add(cariSpaj);
				}
			}
			
			
			//cek apaka SPAJ E-LIONS atau SPAJ PEGA
			if(spajPega > 0){
				if (listShowSpaj.isEmpty()) {
//					redirect = request.getContextPath() + "/include/page/blocked.jsp?jenis=branch";
//					return new ModelAndView("include/page/blocked.jsp?jenis=branch", "cmd", map);
					logger.info(request.getContextPath());
					return new ModelAndView( new RedirectView( request.getContextPath()+"/include/page/blocked.jsp?jenis=pegaprod" ) );
				}
			}
			
			if("01,05,11,12,29,34,39,21".indexOf(user.getLde_id()) <0){
				if (listShowSpaj.isEmpty()) {
//					redirect = request.getContextPath() + "/include/page/blocked.jsp?jenis=branch";
//					return new ModelAndView("include/page/blocked.jsp?jenis=branch", "cmd", map);
					logger.info(request.getContextPath());
					return new ModelAndView( new RedirectView( request.getContextPath()+"/include/page/blocked.jsp?jenis=branch" ) );
				}else{
					map.put("listSpaj", listShowSpaj);
				}
			}
			
			//user2 siap2u agency masuk ke LDE_ID BAS, tp cuma bisa akses yang region siap2u
			if("6318".equals(user.getLus_id()) || "6319".equals(user.getLus_id()) || "6320".equals(user.getLus_id()) ||
			   "6321".equals(user.getLus_id()) || "6322".equals(user.getLus_id()) ){
				if (listShowSpaj.isEmpty()) { 
					logger.info(request.getContextPath());
					return new ModelAndView( new RedirectView( request.getContextPath()+"/include/page/blocked.jsp?jenis=branch" ) );
				}else{
					map.put("listSpaj", listShowSpaj);
				}
			}

			for(int i=0; i<listSpaj.size();i++){
				HashMap worksite = Common.serializableMap(elionsManager.selectPerusahaanWorksite(listSpaj.get(i).getReg_spaj()));
				if(worksite != null){
					String gelar = (String) worksite.get("MCL_GELAR");
					String perusahaan = (String) worksite.get("MCL_FIRST");
					String nik = (String) worksite.get("NIK");
					
					if(gelar == null) gelar = "";
					if(perusahaan == null) perusahaan = "";
					if(nik == null) nik = "";
					
					listSpaj.get(i).getCariSpaj().setPerusahaan(gelar + " " + perusahaan);
					listSpaj.get(i).getCariSpaj().setNik(nik);
				}
			}
			//========================================
		}
		map.put("lssaId", lssaId);
		map.put("lssp_id", lsspId);
		map.put("flag", ServletRequestUtils.getStringParameter(request, "flag", "0"));
		map.put("win", win);
		
		return new ModelAndView("uw/spaj", "cmd", map);
	}

}