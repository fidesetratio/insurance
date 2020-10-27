package com.ekalife.elions.web.uw;

import java.io.FileInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentController;

public class ViewerPaFreeController extends ParentController {
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		//Map<String, Object> map = new HashMap<String, Object>();
		
		// Filter search jenis PA (Free PA / PA BSM) - Daru 05 Dec 2013
		Integer paType = ServletRequestUtils.getIntParameter(request, "paType", 0);
		request.setAttribute("paType", paType);
		
		String tipe = request.getParameter("tipe");
		String kata = request.getParameter("kata");
		String pilter = request.getParameter("pilter");
		String pdfName = request.getParameter("pdfName");
		//String tmms_id = request.getParameter("tmms_id");
		String tmms_id = null;
		String msag_id = currentUser.getMall_msag_id();
		//if(tmms_id == null)tmms_id = "";
		
		//MALLINSURANCE_FREE_PA
		
		String np = request.getParameter("np");
		
		if(np != null){
			request.setAttribute("popUpInsert", "open");
		}
		if("".equals(pdfName))pdfName = null;
		//String setUrlForPdf = props.getProperty("pdf.dir.export")+"\\pa\\"+ "MI_PA_";
		String setUrlForPdf = "";
		if(paType.equals(0)) {//Free PA
			setUrlForPdf = props.getProperty("pdf.dir.export")+"\\pa\\"+ "MI_PA_";
		} else {//PA BSM
			setUrlForPdf = props.getProperty("pdf.dir.export")+"\\cplan\\25\\"+pdfName+"\\";
		}
		if(pdfName != null){
			//download pdf
			String fileName = setUrlForPdf+pdfName+".pdf";
			
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
		    try{
		    	
		    	response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Attachment;filename="+fileName);
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
		    	
				in = new FileInputStream(setUrlForPdf+pdfName+".pdf");
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
		
		//List<Tmms> tmmsList = uwManager.selectFreePaTmmsList(tmms_id, msag_id, tipe, kata, pilter);
		//map.put("pasList", pas);
		
		//Pagination
		PagedListHolder pagedList = new PagedListHolder();
		
		if(paType.equals(0)) {//Free PA
			pagedList = new PagedListHolder(uwManager.selectFreePaTmmsList(tmms_id, msag_id, tipe, kata, pilter, null));
		} else {//PA BSM
			pagedList = new PagedListHolder(uwManager.selectAllPasList(null, "1", tipe, kata, pilter, "pabsm", null, "pabsm",null,null,null));
		}
		
		//Set brp item per halaman
		pagedList.setPageSize(30);
		//Check halaman yg di request
		Integer page = ServletRequestUtils.getIntParameter(request, "page", 1);
		pagedList.setPage(page - 1);
		
		request.setAttribute("list", pagedList);
		
		request.setAttribute("setUrlForPdf", setUrlForPdf);
		
		//request.setAttribute("tmmsList", tmmsList);
		return new ModelAndView("uw/viewer/viewer_pa_free");
		//return tmmsList;
	}
	
}
