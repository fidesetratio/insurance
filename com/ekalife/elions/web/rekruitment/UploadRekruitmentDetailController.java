package com.ekalife.elions.web.rekruitment;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.ekalife.utils.parent.ParentController;

/**
 * Class untuk upload dokumen new business
 * 
 * @author Yusuf
 * @since Feb 3, 2009 (10:29:01 AM)
 */
public class UploadRekruitmentDetailController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String agent = ServletRequestUtils.getRequiredStringParameter(request, "agent");
		String jenis = ServletRequestUtils.getRequiredStringParameter(request, "jenis");
//		String dir = props.getProperty("pdf.dir.export") + "/" + lca_id;
		String dir = props.getProperty("pdf.dir.export.rekruitment");
		if(jenis != null && !"".equals(jenis)){
			if("1".equals(jenis)){
				dir = dir +  "\\" + "Regional"+ "\\"+agent;
			}else if("2".equals(jenis)){
				dir = dir +  "\\" + "Agency"+ "\\"+agent;
			}
		}
		
		Map<String, Object> cmd = new HashMap<String, Object>();
		Upload upload = new Upload();

		List<Scan> daftarScan = uwManager.selectLstScan("DS",null);
		
//		String[] daftarFileName 	= props.getProperty("upload.scan.file").split(",");
//		String[] daftarFileTitle 	= props.getProperty("upload.scan.nama").split(",");
//		String[] daftarFileRequired = props.getProperty("upload.scan.required").split(",");

		upload.setDaftarFile(new ArrayList<MultipartFile>(daftarScan.size()));

		ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
		binder.bind(request);
		//User currentUser = (User) request.getSession().getAttribute("currentUser");
				
//		cmd.put("reg_spaj", 	reg_spaj);
//		cmd.put("direktori", dir + "/" + reg_spaj);
		cmd.put("direktori", dir);
		
		List<DropDown> daftarFile = new ArrayList<DropDown>();
		for(Scan s : daftarScan) {
			daftarFile.add(new DropDown(s.nmfile, s.ket, String.valueOf(s.wajib)));
		}
		cmd.put("daftarFile", daftarFile);
		
		if(request.getParameter("upload")!=null) {
			
//			File destDir = new File(dir + "/" + reg_spaj);
			Date dateNow = elionsManager.selectSysdateSimple();
			DateFormat ff = new SimpleDateFormat("yyyyMMdd");
//			DateFormat yf = new SimpleDateFormat("yyyy");
//			DateFormat mf = new SimpleDateFormat("MM");
//			DateFormat df = new SimpleDateFormat("dd");
			DateFormat hm = new SimpleDateFormat("hhmmss");
			String dateFormatFf = ff.format(dateNow);
//			String dateFormatYf = yf.format(dateNow);
//			String dateFormatMf = mf.format(dateNow);
//			String dateFormatDf = df.format(dateNow);
			String dateFormatHm = hm.format(dateNow);
			File destDir = new File(dir);
			if(!destDir.exists()) destDir.mkdir();
			
			for(int i=0; i<upload.getDaftarFile().size(); i++) {
				MultipartFile file = (MultipartFile) upload.getDaftarFile().get(i);
				
				if(file.isEmpty()==false){
//					String fileName = reg_spaj + daftarScan.get(i).getNmfile() + " " + "WWW.pdf";
					String fileName =  agent + "_" + daftarScan.get(i).getNmfile() + "_" + dateFormatFf + "_" + dateFormatHm + ".pdf";
					String dest = destDir.getPath() +"/"+ fileName; //file.getOriginalFilename();
					File outputFile = new File(dest);
				    FileCopyUtils.copy(file.getBytes(), outputFile);
				}
			}

			cmd.put("pesan", "Data berhasil di-upload.");
			
		} else {
			//cmd.put("pesan", "Silahkan isi minimal satu file untuk di-upload");
		}

//		List<DropDown> daftarAda = FileUtils.listFilesInDirectory(dir +"/"+ reg_spaj);
		List<DropDown> daftarAda = FileUtils.listFilesInDirectory(dir);
		cmd.put("daftarAda", 	daftarAda);

		return new ModelAndView("rekruitment/upload_rekruitment_detail", cmd);
	}
	  
}