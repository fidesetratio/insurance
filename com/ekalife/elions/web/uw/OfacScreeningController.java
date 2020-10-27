package com.ekalife.elions.web.uw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.MstTransUlink;
import com.ekalife.elions.model.OfacSertifikat;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentMultiController;

public class OfacScreeningController extends ParentMultiController{
	
	//private static String IDENTIFIER = "pabsmsy";
	
	public ModelAndView list_spaj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("EditBacController : formBackingObject");
        HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		String lus_id = null;
		String lde_id = currentUser.getLde_id();
		
				String appr_spaj = ServletRequestUtils.getStringParameter(request, "appr_spaj", "");
				String reje_spaj = ServletRequestUtils.getStringParameter(request, "reje_spaj", "");
				String flag_refresh = ServletRequestUtils.getStringParameter(request, "flag_refresh", null);
		    	String action = request.getParameter("action");
		    	String spaj = null;
		    	String nopol = null;
		    	String spaj2 = null;
		    	String possible = null;
		    	String matched = null;
		    	
				String tipe = request.getParameter("tipe");
				String tipestatus = request.getParameter("tipestatus");
				List<Map<String, String>> availableSearchType = new ArrayList<Map<String,String>>();
				Map<String,String> m = new HashMap();
				m.put("label", "polis");
				m.put("value", "polis");
				availableSearchType.add(m);
				m = new HashMap();
				m.put("label", "spaj");
				m.put("value", "spaj");
				
				availableSearchType.add(m);
				m = new HashMap();
				m.put("label", "sertifikat");
				m.put("value", "sertifikat");
				availableSearchType.add(m);
				
				
				
				List<Map<String, String>> availableSearchStatus = new ArrayList<Map<String,String>>();
				Map<String,String> ms = new HashMap();
				/*ms.put("label", "All");
				ms.put("value", "all");
				availableSearchStatus.add(ms);*/
				ms = new HashMap();
				ms.put("label", "POSSIBLE");
				ms.put("value", "possible");
				
				availableSearchStatus.add(ms);
			/*	ms = new HashMap();
				ms.put("label", "MATCHED");
				ms.put("value", "matched");
				availableSearchStatus.add(ms);*/
				
				if(appr_spaj.trim()!=""){
					
					Integer type_data = bacManager.selectSertifikatYN(appr_spaj);
					
					if(type_data==1){
						
						Integer lspd_id_policy = bacManager.selectPositionPolicy(appr_spaj);					
						Integer lspd_id_insured = bacManager.selectPositionInsured(appr_spaj);
						
						if(lspd_id_policy == 200 && lspd_id_insured == 200){
							bacManager.ApproveMstPolicy(appr_spaj, currentUser.getLus_id());
							bacManager.ApproveMstInsured(appr_spaj, currentUser.getLus_id());
							bacManager.insertMstPositionSpaj(currentUser.getLus_id(), "OFAC RESULT : APPROVED", appr_spaj, 0);
							
							bacManager.ApproveRejectProcessed(appr_spaj, currentUser.getLus_id(),6);
							
							request.setAttribute("message","SPAJ berhasil diapprove");
						}
					}else if(type_data==2){
						
						/*List<OfacSertifikat> sertifikat = new ArrayList<OfacSertifikat>();
						sertifikat = bacManager.selectSertifikat(appr_spaj);
						
						if (!(sertifikat.isEmpty())){
							Integer mofs_status = null;
							String mofs_status_message = null;
							String mofs_type = null;
							String mofs_id = null;
									
							for (int i = 0; i < sertifikat.size(); i++) { 		      
									mofs_type = sertifikat.get(i).getMofs_type();
									mofs_id = sertifikat.get(i).getMofs_id();
									mofs_status = 4;
									mofs_status_message = "NEGATIVE";
									
									bacManager.ApproveRejectSertifikat(mofs_id, appr_spaj, mofs_status, mofs_type, mofs_status_message, currentUser.getLus_id());
						      } 
						
						}
						
						Integer mofs_status = 4;
						String mofs_status_message = "NEGATIVE";
						bacManager.ApproveRejectSertifikat(appr_spaj, mofs_status, mofs_status_message, currentUser.getLus_id());*/
						
						bacManager.ApproveRejectProcessed(appr_spaj, currentUser.getLus_id(),6);
						
						request.setAttribute("message","SPAJ berhasil diapprove");
					}
					
				}
				
				if(reje_spaj.trim()!=""){
					Integer type_data = bacManager.selectSertifikatYN(reje_spaj);
					
					if(type_data==1){
						
					
						Integer lspd_id_policy = bacManager.selectPositionPolicy(reje_spaj);					
						Integer lspd_id_insured = bacManager.selectPositionInsured(reje_spaj);
						
						if(lspd_id_policy == 200 && lspd_id_insured == 200){
							bacManager.RejectMstPolicy(reje_spaj, currentUser.getLus_id());
							bacManager.RejectMstInsured(reje_spaj, currentUser.getLus_id());
							bacManager.insertMstPositionSpaj(currentUser.getLus_id(), "OFAC RESULT : REJECT", reje_spaj, 0);
							
							bacManager.ApproveRejectProcessed(reje_spaj, currentUser.getLus_id(),95);
							
							List<MstTransUlink> transUlink = new ArrayList<MstTransUlink>();
							transUlink = bacManager.selectCheckMstTransUlink(reje_spaj);
							
							if (!(transUlink.isEmpty())){
										
								for (int i = 0; i < transUlink.size(); i++) { 
									String	reg_spaj = transUlink.get(i).getReg_spaj();
									Integer	mu_ke = transUlink.get(i).getMu_ke();
									Integer	mtu_ke	= transUlink.get(i).getMtu_ke();
										
									bacManager.updateMstTransUlink(reg_spaj, mu_ke, mtu_ke, currentUser.getLus_id());
							      } 	
								
								bacManager.updateMsUlink(reje_spaj, currentUser.getLus_id());
							}
							
							request.setAttribute("message","SPAJ berhasil direject");
						}
					
					}else if(type_data==2){
						
						bacManager.ApproveRejectProcessed(reje_spaj, currentUser.getLus_id(),95);
						
						request.setAttribute("message","SPAJ berhasil direject");
					}
				}
				
				String noPage = ServletRequestUtils.getStringParameter(request, "cPage", "0");

				String inputan = ServletRequestUtils.getStringParameter(request, "nomorspajpolis", "");
				
				if (flag_refresh==null){
					if (!(tipe==null)){
						if (!(inputan==""))
						{
							if(tipe.trim().equalsIgnoreCase("polis")){
								 nopol = request.getParameter("nomorspajpolis").trim();
								 
								 if(tipestatus.trim().equalsIgnoreCase("possible")){
									 possible = "1";
								 }else if(tipestatus.trim().equalsIgnoreCase("matched")){
									 matched = "1";
								 }
							}else if(tipe.trim().equalsIgnoreCase("spaj")){
								spaj = request.getParameter("nomorspajpolis").trim();
								
								if(tipestatus.trim().equalsIgnoreCase("possible")){
									 possible = "1";
								 }else if(tipestatus.trim().equalsIgnoreCase("matched")){
									 matched = "1";
								 }
								
							}else if(tipe.trim().equalsIgnoreCase("sertifikat")){
								spaj2 = request.getParameter("nomorspajpolis").trim();
								
								if(tipestatus.trim().equalsIgnoreCase("possible")){
									 possible = "1";
								 }else if(tipestatus.trim().equalsIgnoreCase("matched")){
									 matched = "1";
								 }
							}
						}
					}
				}
				
		        String noRow = "10";
		        if(noPage == "0" ){
		        	noPage = "1";
	            }else{
	            	noPage = noPage;
	            }
		        
		        Map param = new HashMap();
		        String totalPage = bacManager.selectTotalListSPAJ(nopol,spaj,noRow,spaj2, possible, matched);
	            
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

				List ofacList = new ArrayList();
				
				ofacList = bacManager.selectOfacList(nopol,spaj, noRow, noPage,spaj2, possible, matched);
			
				request.setAttribute("ofacList", ofacList);
				
				int currPage = Integer.parseInt(noPage); 
				request.setAttribute("currPage", noPage);
	        	request.setAttribute("firstPage", 1 + "");
	        	request.setAttribute("lastPage", totalPage);        	
	        	request.setAttribute("nextPage", (currPage + 1) + "");
	        	request.setAttribute("previousPage", (currPage - 1) + "");
	        	request.setAttribute("type",tipe);
	        	request.setAttribute("type_status",tipestatus);
	        	request.setAttribute("nomor",inputan);
	        	request.setAttribute("availableSearchType", availableSearchType);
	        	request.setAttribute("availableSearchStatus", availableSearchStatus);
	        	

		request.setAttribute("lde_id", lde_id);
		return new ModelAndView("uw/ofac_screening");
	}
	
}
