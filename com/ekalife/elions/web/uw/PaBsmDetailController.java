package com.ekalife.elions.web.uw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class PaBsmDetailController extends ParentController{

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("EditBacController : formBackingObject");
        HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		String lus_id =null;
		String lde_id = currentUser.getLde_id();
		
		String pdfName="";
		
		if(lde_id.equals("11") || lde_id.equals("01") || lde_id.equals("06")){
			lus_id = null;
					
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
				
				/*
				if("transfer".equals(action)){
					pas = new ArrayList<Pas>();
					pas = uwManager.selectAllPasList(msp_id, "1", null, null, null, "pabsm", null, "pabsm",null,null,null);
					Integer lus_id = Integer.parseInt(currentUser.getLus_id());
					Pas p = pas.get(0);
					//Date end_date = elionsManager.selectSysdate();
					//end_date.setYear(end_date.getYear()+1);
					//end_date.setDate(end_date.getDate()-1);
					p.setLus_id(lus_id);
					p.setLus_login_name(currentUser.getLus_full_name());
					p.setMsp_tgl_transfer(elionsManager.selectSysdate());
					//p.setMsp_fire_beg_date(elionsManager.selectSysdate());
					//p.setMsp_fire_end_date(end_date);
					p = uwManager.transferCplan(p, currentUser);//request, pas, errors,"input",user,errors);
					if(p.getStatus() == 1){
						request.setAttribute("successMessage","proses transfer gagal");
						pas = uwManager.selectAllPasList(msp_id, "1", null, null, null, "pabsm", null, "pabsm",null,null,null);
					}else{
						request.setAttribute("successMessage","transfer sukses");
						pas = uwManager.selectAllPasList(null, "1", null, null, null, "pabsm", null, "pabsm",null,null,null);
					}
				}else{*/
				// Adrian -- source aslinya	
				//pas = uwManager.selectAllPasList(null, "1", tipe, kata, pilter, "pabsm", null, "pabsm",null,lus_id,null);
				
				if("transfer".equals(action)) {
					pas = uwManager.selectAllPasList(msp_id, "1", null, null, null, "pabsm", null, "pabsm", null, null, null);
					Pas p = pas.get(0);
					// Transfer pabsm - Daru 13 Jan 2015
					if("73".equals(p.getProduct_code())) {
						HashMap<String, Object> kartu = uwManager.selectDetailKartuPas(p.getNo_kartu());
						p.setMsag_id(kartu.get("MSAG_ID").toString());
						
						Cmdeditbac edit = new Cmdeditbac();
						p.setLus_id_uw_pas(0);
						p.setLus_login_name("SYSTEM");
						//langsung set ke posisi payment
						p.setLspd_id(4);
						BindException errors = new BindException(new HashMap(), "Pas");
						edit = uwManager.prosesPas(request, "update", p, null, "input", currentUser, errors);
						
						if(edit.getPemegang().getMspo_policy_no() == null)
							request.setAttribute("successMessage","proses submit gagal");
						else
							request.setAttribute("successMessage","submit sukses dengan SPAJ : "+edit.getPemegang().getReg_spaj()+" dan No. Polis : "+edit.getPemegang().getMspo_policy_no());
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
		        String totalPage = uwManager.selectTotalAllPasList(null, null, tipe, kata, pilter, "pabsm", null, "pabsm",null,lus_id,param);
	            
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
				pas = uwManager.selectAllPasList(null, null, tipe, kata, pilter, "pabsm", null, "pabsm",null,lus_id,params);
					//}
				int currPage = Integer.parseInt(noPage); 
				request.setAttribute("currPage", noPage);
	        	request.setAttribute("firstPage", 1 + "");
	        	request.setAttribute("lastPage", totalPage);        	
	        	request.setAttribute("nextPage", (currPage + 1) + "");
	        	request.setAttribute("previousPage", (currPage - 1) + "");  
				
				for(int i = 0 ; i < pas.size() ; i++){
					if("0".equals(pas.get(i).getMsp_premi())){
						pas.get(i).setInput_type("PA PLAN A FREE");
					} else if(pas.get(i).getNo_kartu() != null && !pas.get(i).getNo_kartu().trim().equals("")) {
						HashMap<String, Object> kartu = uwManager.selectDetailKartuPas(pas.get(i).getNo_kartu());
						if(kartu == null )
							{pas.remove(i);
							i--;}
						else
							pas.get(i).setInput_type(kartu.get("NAMA_PLAN").toString().toUpperCase());
						if(kartu != null && kartu.get("NO_VA") != null)
							pas.get(i).setNo_va((String) kartu.get("NO_VA"));
					}else{
						pas.get(i).setInput_type("STANDART");
					}
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
//	         String totalPage = uwManager.selectTotalAllPasList(null, "1", tipe, kata, pilter, "pabsm", null, "pabsm",null,lus_id,param);
	         String totalPage = uwManager.selectTotalAllPasList(null, null, tipe, kata, pilter, "pabsm", null, "pabsm",null,lus_id,param);
             
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
//			pas = uwManager.selectAllPasList(null, "1", tipe, kata, pilter, "pabsm", null, "pabsm",null,lus_id,params);
			pas = uwManager.selectAllPasList(null, null, tipe, kata, pilter, "pabsm", null, "pabsm",null,lus_id,params);
			//}
			
			int currPage = Integer.parseInt(noPage); 
			request.setAttribute("currPage", noPage);
        	request.setAttribute("firstPage", 1 + "");
        	request.setAttribute("lastPage", totalPage);        	
        	request.setAttribute("nextPage", (currPage + 1) + "");
        	request.setAttribute("previousPage", (currPage - 1) + "");        	
        	
			
			for(int i = 0 ; i < pas.size() ; i++){
				if("0".equals(pas.get(i).getMsp_premi())){
					pas.get(i).setInput_type("FREE");
				} else if(pas.get(i).getNo_kartu() != null && !pas.get(i).getNo_kartu().trim().equals("")) {
					HashMap<String, Object> kartu = uwManager.selectDetailKartuPas(pas.get(i).getNo_kartu());
					pas.get(i).setInput_type(kartu.get("NAMA_PLAN").toString().toUpperCase());
					if(kartu.get("NO_VA") != null)
						pas.get(i).setNo_va((String) kartu.get("NO_VA"));
				}else{
					pas.get(i).setInput_type("STANDART");
				}
			}
			
			request.setAttribute("pasList", pas);
						
		}
		
		if(request.getParameter("viewpdf")!=null){
			pdfName = request.getParameter("pdfName");	
			
		if("".equals(pdfName))pdfName = null;
		String setUrlForPdf = props.getProperty("pdf.dir.export")+"\\cplan\\25\\"+pdfName+"\\";
		String product_code = request.getParameter("product_code");
		if("73".equals(product_code))
			setUrlForPdf = "\\\\ebserver\\pdfind\\Polis\\bsm\\73\\";
//			setUrlForPdf = "\\\\ebserver\\pdfind\\Polis_Testing\\bsm\\73\\";
		if(pdfName != null){
			//download pdf
			String fileName = setUrlForPdf+pdfName+".pdf";
			if("73".equals(product_code)) {
				String msp_id = request.getParameter("msp_id");
				String product_sub_code = request.getParameter("product_sub_code");
				List<Pas> pas = new ArrayList<Pas>();
//				pas = uwManager.selectAllPasList(msp_id, "1", null, null, null, "pabsm", null, "pabsm", null, null, null);
				pas = uwManager.selectAllPasList(msp_id, null, null, null, null, "pabsm", null, "pabsm", null, null, null);
				Pas p = pas.get(0);
				HashMap<String, Object> kartu = uwManager.selectDetailKartuPas(p.getNo_kartu());
				String no_polis_induk = kartu.get("NO_POLIS_INDUK").toString();
				
				fileName = setUrlForPdf + no_polis_induk + "-" + "073" + "-" + pdfName + ".pdf";
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
					String nama_plan = kartu.get("NAMA_PLAN").toString();
					try {
						ITextPdf.generateSertifikatPaBsmV2(p.getNo_kartu(), no_polis_induk, product_code, product_sub_code, nama_plan, p.getMsp_up(), p.getMsp_premi(), elionsManager);
					} catch(Exception e) {
						logger.error("ERROR :", e);
						return new ModelAndView("uw/pa_bsm_detail");						
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
		
		
		request.setAttribute("setUrlForPdf", setUrlForPdf);
		
		return null;
		}
		
		request.setAttribute("lde_id", lde_id);
		return new ModelAndView("uw/pa_bsm_detail");
	}
	
}
