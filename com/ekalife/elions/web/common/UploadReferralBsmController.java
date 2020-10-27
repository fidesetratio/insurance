package com.ekalife.elions.web.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentController;

public class UploadReferralBsmController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> cmd = new HashMap<String, Object>();
		
		Upload upload = new Upload();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
		binder.bind(request);		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		if(request.getParameter("upload")!=null) {
			if(upload.getFile1().isEmpty()==false){
				request.setAttribute("pesan", bacManager.uploadReferralBSM(currentUser, upload));
			}else request.setAttribute("pesan", "Data tidak berhasil diupload");
		}
			
		return new ModelAndView("common/upload_referral_bsm", cmd);
	}
	
}