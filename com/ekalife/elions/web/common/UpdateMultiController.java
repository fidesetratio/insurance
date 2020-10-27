package com.ekalife.elions.web.common;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentMultiController;

/**
 * @author Yusuf
 *
 */
public class UpdateMultiController extends ParentMultiController {



	public ModelAndView proposal(HttpServletRequest request, HttpServletResponse response)throws ServletRequestBindingException, ParseException{
		
		String download = ServletRequestUtils.getStringParameter(request, "download", "");
		String dir = props.getProperty("upload.proposal");
		
		if(!download.equals("")) {

			try {
				User currentUser = (User)request.getSession().getAttribute("currentUser");
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				File file = new File(dir+"//"+download);
				Date modified_date = sdf.parse(sdf.format(file.lastModified()));
				Map map = new HashMap();
				map.put("lus_id", currentUser.getLus_id());
				map.put("jenis", "Proposal");
				map.put("filename", download);
				map.put("modified_date", modified_date);
				String filetype ="";
				if(download.contains("upas")){
					filetype="Update Proposal Agency";
				}else if(download.contains("upbsm")){
					filetype="Update Proposal Bank Sinarmas";
				}else if(download.contains("update")){
					filetype="Update Proposal Regional";
				}else if(download.contains("upfa")){
					filetype="Update Proposal Bancassurance";
				}else if(download.contains("Full_Agency_System")){
					filetype="Proposal Agency System";
				}else if(download.contains("full_bsm_Syariah")){
					filetype="Proposal BSM Syariah";
				}else if(download.contains("full_bsm")){
					filetype="Proposal BSM";
				}else if(download.contains("Full_Regional")){
					filetype="Proposal Regional";
				}
				map.put("filetype", filetype);
				uwManager.insertMstHistDownload(map);
				FileUtils.downloadFile("attachment;", dir, download, response);
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
			} catch (IOException e) {
				logger.error("ERROR :", e);
			}
			return null;

		}else {
			
			Map cmd = new HashMap();
			List<DropDown> daftarFile = FileUtils.listFilesInDirectory(dir);
			
			for(DropDown d : daftarFile) {
				if(d.getKey().toLowerCase().startsWith("upas_")) {
					d.setDesc("Update Proposal hanya untuk Agency System (Untuk Regional Tidak perlu download proposal yang Agency System)");
				}else if(d.getKey().toLowerCase().startsWith("update_absen")) {
					d.setDesc("Update Absensi");
				}else if(d.getKey().toLowerCase().startsWith("update_")) {
					d.setDesc("Update Proposal Regional (Proposal Biasa)");
				}else if(d.getKey().toLowerCase().startsWith("upfa_")) {
					d.setDesc("Update Proposal Bancassurance");
				}else if(d.getKey().toLowerCase().startsWith("upbsm_")) {
					d.setDesc("Update Proposal Bank Sinarmas");
				}else {
					d.setDesc("");
				}
			}
			
			cmd.put("daftarFile", daftarFile);
			return new ModelAndView("update_proposal/update", cmd);
			
		}
	}

	public ModelAndView update_data_vendor(HttpServletRequest request, HttpServletResponse response)throws ServletRequestBindingException, Exception{
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Map map=new HashMap();
		Upload upload = new Upload();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
		binder.bind(request);
		String tDest=props.getProperty("upload.dir.data_vendor")+FormatDate.toStringWithOutSlash(elionsManager.selectSysdate());
		File destDir = new File(tDest);
		if(!destDir.exists()) destDir.mkdirs();
		if(upload.getFile1()!=null)
		if(upload.getFile1().isEmpty()==false){
			if(upload.getFile1().getSize()>(1024*1024)){
				map.put("err","Maksimal ukuran file 1 MB");
			}else{
				String dest=tDest+"\\"+upload.getFile1().getOriginalFilename();
				File outputFile = new File(dest);
			    upload.getFile1().transferTo(outputFile);
			    //proses insert ke tabel;
			    InputStream in = new FileInputStream(dest);
			    //map.put("outputFile", in);
			    map.put("suc","Berhasil Upload file "+upload.getFile1().getOriginalFilename());
			    //read file excell dan upload ke table eka.mst_client_history
			    Integer count=elionsManager.prosesUploadClientHistory(in,currentUser);
			    map.put("count", count);
			    in.close();
			}
		}
		//
		return new ModelAndView("update_proposal/update_data_vendor",map);
	}

	/**Untuk Memperbaiki kolom yang gagal di upload data vendor (msch_tgl_kirim,msch_tgl_terima) 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 * @throws Exception
	 */	
	public ModelAndView proses_missing_data_vendor(HttpServletRequest request, HttpServletResponse response)throws ServletRequestBindingException, Exception{
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String lusId=ServletRequestUtils.getStringParameter(request, "lusId");
		Map map=new HashMap();
		if(currentUser.getLus_id().equals("534")){
			Upload upload = new Upload();
			ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
			binder.bind(request);
			String tDest=props.getProperty("upload.dir.data_vendor")+FormatDate.toStringWithOutSlash(elionsManager.selectSysdate());
			File destDir = new File(tDest);
			if(!destDir.exists()) destDir.mkdirs();
			if(upload.getFile1()!=null)
			if(upload.getFile1().isEmpty()==false){
				if(upload.getFile1().getSize()>(1024*1024)){
					map.put("err","Maksimal ukuran file 1 MB");
				}else{
					String dest=tDest+"\\"+upload.getFile1().getOriginalFilename();
					File outputFile = new File(dest);
				    upload.getFile1().transferTo(outputFile);
				    //proses insert ke tabel;
				    InputStream in = new FileInputStream(dest);
				    //map.put("outputFile", in);
				    map.put("suc","Berhasil Upload file "+upload.getFile1().getOriginalFilename());
				    //read file excell dan upload ke table eka.mst_client_history
				    Integer count=elionsManager.prosesMissingUpdateDataVendor(in,lusId);
				    map.put("count", count);
				    in.close();
				}
			}
		}else{	
			map.put("access", "You Have Not Access Right.. Only Ferry Harlim");
		}
		map.put("lsUser",elionsManager.selectLstUser2());
		//
		return new ModelAndView("update_proposal/proses_missing_data_vendor",map);
	}


}
	