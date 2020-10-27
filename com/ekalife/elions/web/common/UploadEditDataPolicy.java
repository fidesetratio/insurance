package com.ekalife.elions.web.common;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentController;

public class UploadEditDataPolicy extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> cmd = new HashMap<String, Object>();
		
		Upload upload = new Upload();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
		binder.bind(request);		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String s_jenisEdit = ServletRequestUtils.getStringParameter(request, "jenisEdit", "0");
		ArrayList<DropDown> listJenisEdit=new ArrayList<DropDown>();
		listJenisEdit.add(new DropDown("0","------PILIH DISTRIBUSI---------"));
		if(currentUser.getLus_id().equals("2998")){
			listJenisEdit.add(new DropDown("1","------EDIT NO BLANKO---------"));
			listJenisEdit.add(new DropDown("2","------EDIT NO MEMBER---------"));
			listJenisEdit.add(new DropDown("3","------UPDATE NO CONNOTE---------"));
			listJenisEdit.add(new DropDown("4","------UPDATE POD---------"));
			listJenisEdit.add(new DropDown("5","------UPDATE NIK---------"));
		}else {
			listJenisEdit.add(new DropDown("3","------UPDATE NO CONNOTE---------"));
			listJenisEdit.add(new DropDown("4","------UPDATE POD---------"));
			listJenisEdit.add(new DropDown("6","------SMS Polis Retur-------------")); //helpdesk [149354] Project SMS Polis Retur
		}
		if(request.getParameter("proses")!=null) {
			if(upload.getFile1().isEmpty()==false){
				if(s_jenisEdit.equalsIgnoreCase("5")){
					request.setAttribute("pesan", bacManager.UpdateDataNIK(currentUser, upload));
				}else if(s_jenisEdit.equalsIgnoreCase("4")){
					request.setAttribute("pesan", bacManager.insertDataJne(currentUser, upload));
				}else if(s_jenisEdit.equalsIgnoreCase("6")){ //helpdesk [149354] Project SMS Polis Retur
					request.setAttribute("pesan", bacManager.upload_sms_polis_retur(currentUser, upload));
				}else{
					request.setAttribute("pesan", bacManager.updateDataPolicy(currentUser, upload, s_jenisEdit));					
				}
			}else request.setAttribute("pesan", "Data tidak berhasil diupload");
		}
		cmd.put("jenisEdit", listJenisEdit)	;
		return new ModelAndView("common/upload_edit", cmd);
	}
	
}