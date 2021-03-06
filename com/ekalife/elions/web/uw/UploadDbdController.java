package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Scan;
import com.ekalife.elions.model.Upload;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentController;

public class UploadDbdController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, Object> cmd = new HashMap<String, Object>();
		Upload upload = new Upload();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
		
		String msp_fire_id = ServletRequestUtils.getRequiredStringParameter(request, "msp_fire_id");
		String dir = props.getProperty("pdf.dir.export") + "\\dbd";
		
		List<Scan> daftarScan = uwManager.selectLstScan("UW","1");
		
		upload.setDaftarFile(new ArrayList<MultipartFile>(daftarScan.size()));

		ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
		binder.bind(request);
				
		cmd.put("msp_fire_id",msp_fire_id);
		cmd.put("direktori", dir + "\\" + msp_fire_id);
		
		List<DropDown> daftarFile = new ArrayList<DropDown>();
		for(Scan s : daftarScan) {
			daftarFile.add(new DropDown(s.nmfile, s.ket, String.valueOf(s.wajib)));
		}
		cmd.put("daftarFile", daftarFile);	
		
		if(request.getParameter("upload")!=null) {
			File destDir = new File(dir + "\\" + df.format(elionsManager.selectSysdate()));
			if(!destDir.exists()) destDir.mkdir();
			destDir = new File(dir + "\\" + df.format(elionsManager.selectSysdate()) + "\\" + msp_fire_id);
			if(!destDir.exists()) destDir.mkdir();
			
			for(int i=0; i<upload.getDaftarFile().size(); i++) {
				MultipartFile file = (MultipartFile) upload.getDaftarFile().get(i);
				
				if(file.isEmpty()==false){
					File fileDir = new File(destDir.getPath());
					String fileName = msp_fire_id + " " + daftarScan.get(i).getNmfile() + " " + FormatString.getFileNo(fileDir.list(), daftarScan.get(i).getNmfile())+".pdf";
					String dest = destDir.getPath() + "\\" + fileName; //file.getOriginalFilename();
					File outputFile = new File(dest);
				    FileCopyUtils.copy(file.getBytes(), outputFile);
				}
			}
			cmd.put("pesan", "Data berhasil di-upload.");
		}
		
		List<DropDown> daftarAda = FileUtils.listFilesInDirectory(dir + "\\" + df.format(elionsManager.selectSysdate()) + "\\" + msp_fire_id);
		cmd.put("daftarAda", 	daftarAda);		
		
		return new ModelAndView("uw/upload_dbd", cmd);
	}
}
