package com.ekalife.elions.web.common;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.FileUtils;
import com.ekalife.utils.parent.ParentController;

public class DownloadController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String download = ServletRequestUtils.getStringParameter(request, "download", "");
		String dir = props.getProperty("download.folder");
		
		if(!download.equals("")) {
			try {
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
				d.setDesc("");
			}
			
			cmd.put("daftarFile", daftarFile);
			return new ModelAndView("common/download", cmd);
		}
	}
	
}