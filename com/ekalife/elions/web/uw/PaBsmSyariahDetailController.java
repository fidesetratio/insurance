package com.ekalife.elions.web.uw;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.User;
import com.ekalife.utils.ITextPdf;
import com.ekalife.utils.parent.ParentController;

public class PaBsmSyariahDetailController extends ParentController{
	
	private static String IDENTIFIER = "pabsmsy";

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("EditBacController : formBackingObject");
        HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		String lus_id =null;
		String lde_id = currentUser.getLde_id();
		
		String pdfName="";
		
		if(lde_id.equals("11") || lde_id.equals("01") || lde_id.equals("06")){
			lus_id = null;
					
		    	String action = request.getParameter("action");
				String msp_id = request.getParameter("msp_id");
				String tipe = request.getParameter("tipe");
				String kata = request.getParameter("kata");
				String pilter = request.getParameter("pilter");
					   pdfName = request.getParameter("pdfName");
				
				List<Pas> pas = new ArrayList<Pas>();
				
				String tgl_lahir = "";
				int centang = ServletRequestUtils.getIntParameter(request, "centang", 0);
				if(centang == 1) {
					tgl_lahir = request.getParameter("tgl_lahir");
				}
				
				if("".equals(tgl_lahir))tgl_lahir = null;
				
				
				if("transfer".equals(action)) {
					
					System.out.println("msp_id="+msp_id);
					pas = uwManager.selectAllPasList(msp_id, "1", null, null, null, IDENTIFIER, null, IDENTIFIER, null, null, null);
				
					Pas p = pas.get(0);
					
					if("205".equals(p.getProduct_code())) {
						Cmdeditbac edit = new Cmdeditbac();
						p.setLus_id_uw_pas(0);
						p.setLus_login_name("SYSTEM");
						//langsung set ke posisi payment
						p.setLspd_id(4);
						BindException errors = new BindException(new HashMap(), "Pas");
						edit = uwManager.prosesPas(request, "update", p, null, "input", currentUser, errors);
						if(edit.getPemegang().getMspo_policy_no() == null)
							request.setAttribute("successMessage","proses submit gagal");
						else{
							String nopolis = p.getMspo_policy_no();
							String no_polis_induk = nopolis;
							String lsbs_number = p.getProduct_code();
							Integer lsdbs_number = p.getProduk();
							Integer cara_bayar = p.getLscb_id();
							
							String reg_spaj = p.getReg_spaj();
							String fileName = ITextPdf.generateSertifikatPaBsmSyariah(nopolis,reg_spaj, nopolis, lsbs_number, lsdbs_number,  p.getMsp_up(), p.getMsp_premi(), elionsManager,props,bacManager,cara_bayar);
							request.setAttribute("successMessage","submit sukses dengan SPAJ : "+edit.getPemegang().getReg_spaj()+" dan No. Polis : "+edit.getPemegang().getMspo_policy_no());
							
						};
					}
				}
				String noPage = "";
		        String noRow = "20";
		        if(request.getParameter("cPage") == null || request.getParameter("cPage") == "" ){
		        	noPage = "1";
	            }else{
	            	noPage = request.getParameter("cPage");
	            }
		        
		        Map param = new HashMap();
		        param.put("tgl_lahir", tgl_lahir);
		        param.put("noRow", noRow);
//		        String totalPage = uwManager.selectTotalAllPasList(null, "1", tipe, kata, pilter, "pabsm", null, "pabsm",null,lus_id,param);
		        if(currentUser.getJn_bank() > -1)
		        	param.put("jn_bank", currentUser.getJn_bank());
		        if(currentUser.getCab_bank() != null && !currentUser.getCab_bank().trim().equals(""))
		        	param.put("cab_bank", currentUser.getCab_bank());
		        String totalPage = uwManager.selectTotalAllPasList(null, null, tipe, kata, pilter,IDENTIFIER, null,IDENTIFIER,null,lus_id,param);
	            
	            // HANDLE PAGE LESS OR PAGE OVER
	            if(Integer.parseInt(noPage) < 1){
	            	noPage = "1";
	            }
	            
	            if(Integer.parseInt(noPage) > Integer.parseInt(totalPage)){
	            	noPage = totalPage;
	            }
				
				Map params = new HashMap();
				params.put("tgl_lahir", tgl_lahir);
				params.put("noRow", noRow);
				params.put("noPage", noPage);
//				pas = uwManager.selectAllPasList(null, "1", tipe, kata, pilter, "pabsm", null, "pabsm",null,lus_id,params);
		        if(currentUser.getJn_bank() > -1)
		        	params.put("jn_bank", currentUser.getJn_bank());
		        if(currentUser.getCab_bank() != null && !currentUser.getCab_bank().trim().equals(""))
		        	params.put("cab_bank", currentUser.getCab_bank());
		        params.put("order_by", "create_date_desc");
				pas = uwManager.selectAllPasList(null, null, tipe, kata, pilter, IDENTIFIER, null, IDENTIFIER,null,lus_id,params);
					//}
				int currPage = Integer.parseInt(noPage); 
				request.setAttribute("currPage", noPage);
	        	request.setAttribute("firstPage", 1 + "");
	        	request.setAttribute("lastPage", totalPage);        	
	        	request.setAttribute("nextPage", (currPage + 1) + "");
	        	request.setAttribute("previousPage", (currPage - 1) + "");  
				
	        	for(int i = 0 ; i < pas.size() ; i++){
						pas.get(i).setInput_type("STANDART");					
				}
								
				request.setAttribute("pasList", pas);
				
			
		}else{
			lus_id =currentUser.getLus_id();
			
			//Map<String, Object> map = new HashMap<String, Object>();
			//currentUser.setLca_id("58");
			String action = request.getParameter("action");
			String msp_id = request.getParameter("msp_id");
			String tipe = request.getParameter("tipe");
			String kata = request.getParameter("kata");
			String pilter = request.getParameter("pilter");
					pdfName = request.getParameter("pdfName");
			
			List<Pas> pas = new ArrayList<Pas>();
			
			String tgl_lahir = "";
			int centang = ServletRequestUtils.getIntParameter(request, "centang", 0);
			if(centang == 1) {
				tgl_lahir = request.getParameter("tgl_lahir");
			}
			if("".equals(tgl_lahir))tgl_lahir = null;
			
			 String noPage = "";
	         String noRow = "20";
	         if(request.getParameter("cPage") == null || request.getParameter("cPage") == "" ){
             	noPage = "1";
             }else{
             	noPage = request.getParameter("cPage");
             }
	         
	         Map param = new HashMap();
	         param.put("tgl_lahir", tgl_lahir);
	         param.put("noRow", noRow);
	         String totalPage = uwManager.selectTotalAllPasList(null, null, tipe, kata, pilter, IDENTIFIER, null, IDENTIFIER,null,lus_id,param);
             
             // HANDLE PAGE LESS OR PAGE OVER
             if(Integer.parseInt(noPage) < 1){
             	noPage = "1";
             }
             
             if(Integer.parseInt(noPage) > Integer.parseInt(totalPage)){
             	noPage = totalPage;
             }
			
			Map params = new HashMap();
			params.put("tgl_lahir", tgl_lahir);
			params.put("noRow", noRow);
			params.put("noPage", noPage);
			pas = uwManager.selectAllPasList(null, null, tipe, kata, pilter, IDENTIFIER, null, IDENTIFIER,null,lus_id,params);
			
			int currPage = Integer.parseInt(noPage); 
			request.setAttribute("currPage", noPage);
        	request.setAttribute("firstPage", 1 + "");
        	request.setAttribute("lastPage", totalPage);        	
        	request.setAttribute("nextPage", (currPage + 1) + "");
        	request.setAttribute("previousPage", (currPage - 1) + "");       
        	
        	
        	
			
			for(int i = 0 ; i < pas.size() ; i++){
			
					pas.get(i).setInput_type("STANDART");
			}
			
			request.setAttribute("pasList", pas);
						
		}
		
		if(request.getParameter("viewpdf")!=null){
			pdfName = request.getParameter("pdfName");	
			
		if("".equals(pdfName))pdfName = null;
		String setUrlForPdf = props.getProperty("pdf.dir.export")+"\\cplan\\09\\"+pdfName+"\\";
		String product_code = request.getParameter("product_code");
		if(pdfName != null){
			/**
			 * setup pdf nanti di pikirin caranya.
			 * sekarang harus fokus jalan atau tidak nya.
			 */
			//download pdf
			String fileName = setUrlForPdf+pdfName+".pdf";
			if("205".equals(product_code)) {
				String msp_id = request.getParameter("msp_id");
				String product_sub_code = request.getParameter("product_sub_code");
				List<Pas> pas = new ArrayList<Pas>();
				pas = uwManager.selectAllPasList(msp_id, null, null, null, null, IDENTIFIER, null, IDENTIFIER, null, null, null);
				Pas p = pas.get(0);
				//HashMap<String, Object> kartu = uwManager.selectDetailKartuPas(p.getNo_kartu());
				//String no_polis_induk = kartu.get("NO_POLIS_INDUK").toString();
				String nopolis = p.getMspo_policy_no();
				String no_polis_induk = nopolis;
				String lsbs_number = p.getProduct_code();
				Integer lsdbs_number = p.getProduk();
				String reg_spaj = p.getReg_spaj();
				int cara_bayar = p.getLscb_id();
				
				
				
				fileName = setUrlForPdf + no_polis_induk + "-" + "205" + "-" + pdfName + ".pdf";
				
				String outputName = props.getProperty("pdf.dir.export") + "\\bsm\\205\\" + nopolis + "-" + lsbs_number + "-" + nopolis + ".pdf";
				
				fileName = outputName;
				File file = new File(fileName);
				// Jika belum jadi no polis, maka tiap view delete dulu pdf yg lama
				if(file.exists() && p.getMspo_policy_no() == null) {
					try {
						file.delete();
					} catch(Exception e) {
						logger.error("ERROR :", e);
					}
				}
				
				
				
				if(!file.exists()) {
					String nama_plan = "";
					try {
					 fileName = 	ITextPdf.generateSertifikatPaBsmSyariah(nopolis,reg_spaj, nopolis, lsbs_number, lsdbs_number,  p.getMsp_up(), p.getMsp_premi(), elionsManager,props,bacManager,cara_bayar);
					} catch(Exception e) {
						logger.error("ERROR :", e);
					}
				}
			}
			
//			FileInputStream in = new FileInputStream(setUrlForPdf+pdfName+".pdf");
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try {
				
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Attachment;filename="+fileName);
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				
				in = new FileInputStream(fileName);
				ouputStream = response.getOutputStream();
				
				IOUtils.copy(in, ouputStream);
			}finally {
				try {
					if(in != null) {
						in.close();
					}
					if(ouputStream != null) {
						ouputStream.flush();
					    ouputStream.close();
					}
				}catch (Exception e) {
					logger.error("ERROR :", e);
				}
			}
		    
			//==================
		}
		
		
		
		return null;
		}
		
		request.setAttribute("lde_id", lde_id);
		return new ModelAndView("uw/pa_bsm_syariah_detail");
	}
	
}
