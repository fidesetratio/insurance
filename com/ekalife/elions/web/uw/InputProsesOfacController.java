/**
 * @author  : Ryan F
 * @created : May 03, 2011, 8:37:46 AM
 * 
 * Ini merupakan Controller buat Endorse Non Material Bancass II req Novie.
 */
package com.ekalife.elions.web.uw;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Comment;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentMultiController;

public class InputProsesOfacController extends ParentMultiController {
	
	// Halaman Depan dari Endorse (Tampilan Awal).
	public ModelAndView comment (HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.debug("EditBacController : formBackingObject");
        HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		
		/*Integer mofs_id =ServletRequestUtils.getIntParameter(request, "mofs_id",0);
		Integer add_num =ServletRequestUtils.getIntParameter(request, "add_num",0);
		String type = ServletRequestUtils.getStringParameter(request, "type", "");*/
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String comment = ServletRequestUtils.getStringParameter(request, "comment", "");
		String nama = ServletRequestUtils.getStringParameter(request, "nama", "");
		
		if (comment==""){
			
			request.setAttribute("warning","Comment Tidak Boleh Kosong");
			/*request.setAttribute("mofs_id",mofs_id);
			request.setAttribute("type",type);*/
			request.setAttribute("spaj",spaj);
			/*request.setAttribute("add_num",add_num);*/
			request.setAttribute("nama",nama);
			
		}else{
			
			bacManager.insertComment(0, 0, spaj, "", comment, nama);
			
			request.setAttribute("message","data berhasil diinput");
			request.setAttribute("disabled","disabled");
		}
		
		return new ModelAndView("uw/input_comment");
	}
	
	public ModelAndView tampil (HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.debug("EditBacController : formBackingObject");
        HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		
		/*Integer mofs_id =ServletRequestUtils.getIntParameter(request, "mofs_id",0);
		Integer add_num = ServletRequestUtils.getIntParameter(request, "add_num",0);
		String type = ServletRequestUtils.getStringParameter(request, "type", "");*/
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String nama = ServletRequestUtils.getStringParameter(request, "nama", "");
		
		String lde_id = currentUser.getLde_id();
		
		Comment comment = new Comment();
		comment = bacManager.selectComment(spaj);
		
		//if ((comment.getComm_id()).isEmpty()){
		if (comment==null){
			/*request.setAttribute("mofs_id",mofs_id);
			request.setAttribute("type",type);*/
			request.setAttribute("spaj",spaj);/*
			request.setAttribute("add_num",add_num);*/
			request.setAttribute("nama",nama);
			
		}else{
			request.setAttribute("comment",comment.getComm_comment());
			request.setAttribute("disabled","disabled");
		}
		
		request.setAttribute("lde_id", lde_id);
		return new ModelAndView("uw/input_comment");
	}

}