package com.ekalife.elions.web.uw;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.mail.MailException;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Tmms;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentController;

public class PaFreeDetailController extends ParentController {
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		//Map<String, Object> map = new HashMap<String, Object>();
		
		String tipe = request.getParameter("tipe");
		String kata = request.getParameter("kata");
		String pilter = request.getParameter("pilter");
		String pdfName = request.getParameter("pdfName");
		String emailName = request.getParameter("emailName");
		String nama = request.getParameter("nama");
		String mailPdfPolis = request.getParameter("mailPdfPolis");
		
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
		String setUrlForPdf = props.getProperty("pdf.dir.export")+"\\pa\\"+ "MI_PA_";
		if(pdfName != null){
			if(emailSoftcopy(emailName, pdfName, nama, pdfName)){
				request.setAttribute("error_email", "E-MAIL SUKSES DIKIRIM");
			}else{
				request.setAttribute("error_email", "E-MAIL GAGAL DIKIRIM");
			}
		}
		
		 String noPage = "";
         String noRow = "20";
         if(request.getParameter("cPage") == null || request.getParameter("cPage") == "" ){
         	noPage = "1";
         }else{
         	noPage = request.getParameter("cPage");
         }
                 
         String totalPage  = uwManager.selectTotalFreePaTmmsList(tmms_id, msag_id, tipe, kata, pilter);
         
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
		List<Tmms> tmmsList = uwManager.selectFreePaTmmsList(tmms_id, msag_id, tipe, kata, pilter, params);
		
		int currPage = Integer.parseInt(noPage); 
		request.setAttribute("currPage", noPage);
    	request.setAttribute("firstPage", 1 + "");
    	request.setAttribute("lastPage", totalPage);        	
    	request.setAttribute("nextPage", (currPage + 1) + "");
    	request.setAttribute("previousPage", (currPage - 1) + "");        
		
		//map.put("pasList", pas);
		request.setAttribute("setUrlForPdf", setUrlForPdf);
		
		request.setAttribute("tmmsList", tmmsList);
		return new ModelAndView("uw/pa_free_detail");
		//return tmmsList;
	}
	
	public boolean emailSoftcopy(String emailTo, String pdfName, String nama, String noPolis){
		
		String setUrlForPdf = props.getProperty("pdf.dir.export")+"\\pa\\"+ "MI_PA_";
		String fileName = setUrlForPdf+pdfName+".pdf";
		
		File softcopy = new File(fileName);
		
		List<File> attachments = new ArrayList<File>();
		attachments.add(softcopy);
		
		try {
			email.send(true, 
					"info@sinarmasmsiglife.co.id",
					//new String[]{"andy@sinarmasmsiglife.co.id"}, null,
					//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
					new String[]{emailTo}, null,//new String[]{"policy_service@sinarmasmsiglife.co.id"},
					null,
					"Bukti Kepesertaan Asuransi Personal Accident Softcopy ",//outputFilename,
					"Kepada Yth Bpk/ Ibu " + nama + "<br/>" +
					"di tempat " + "<br/>" +
					"<br/>" +
					"Dengan Hormat, " + "<br/>" +
					"Bersama ini kami kirimkan Bukti Kepesertaan Asuransi Personal Accident softcopy dengan no. "+noPolis+", " + "<br/>" +
					"sesuai dengan permohonan aplikasi melalui Team Sales kami yang Anda sampaikan. " + "<br/>" +
					"<br/>" +
//					"Untuk menjaga keamanan Bukti Kepesertaan Asuransi Personal Accident Softcopy Anda, Bukti " + "<br/>" +
//					"Kepesertaan Asuransi Personal Accident Softcopy Anda sudah kami lindungi dengan password  " + "<br/>" +
//					"berupa tanggal lahir Anda dalam format dd-mm-yyyy (contoh: 14-04-1985).  " + "<br/>" +
//					"<br/>" +
					"Mohon agar Softcopy Polis ini disimpan dan dicetak apabila diperlukan " + "<br/>" +
					"<br/>" +
					"Hormat kami, " + "<br/>" +
					"PT Asuransi Jiwa Sinarmas MSIG Tbk. " + "<br/>" +
					"Wisma Eka Jiwa Lt. 8, " + "<br/>" +
					"Jl. Mangga Dua Raya, Jakarta 10730 " + "<br/>",
					attachments);
		} catch (MailException e) {
			return false;
		} catch (MessagingException e) {
			return false;
		}
		return true;
	}
	
}
