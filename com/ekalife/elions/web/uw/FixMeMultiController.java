package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.spring.util.Email;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Mia;
import com.ekalife.elions.model.Nasabah;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.elions.web.bac.support.form_agen;
import com.ekalife.utils.CheckSum;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentFormController;
import com.ekalife.utils.parent.ParentMultiController;
import com.ekalife.utils.view.XLSCreatorFreeSimasRumah;
import com.ekalife.utils.ExcelRead;


public class FixMeMultiController extends ParentMultiController {
	
	public ModelAndView fix_pas_begdate(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{

		Map<String, Object> cmd = new HashMap<String, Object>();
		String err = "";
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		cmd.put("user_id", lus_id);
		
		String submit = request.getParameter("upload");
		List<Map> successMessageList = new ArrayList<Map>();
		
		if(submit != null){
		
			Upload upload = new Upload();
			String file_fp = request.getParameter("file_fp");
			upload.setDaftarFile(new ArrayList<MultipartFile>(10));
			ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
			binder.bind(request);
			MultipartFile mf = upload.getFile1();
			String fileName = mf.getOriginalFilename();
			String directory = props.getProperty("temp.dir.fileupload"); //file_fp.replaceAll(fileName, "");
			
			ExcelRead excelRead = new ExcelRead();		
			Pas pasExcel = null;
			
			List<List> excelList = new ArrayList<List>();
					
			try{
				String dest=directory + "/" + fileName ;
				File outputFile = new File(dest);
			    FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
			    excelList = excelRead.readExcelFile(directory + "\\", fileName);
			}catch (Exception e) {
				Map<String,String> map = new HashMap<String, String>();
				map.put("sts", "FAILED");
				map.put("msg", "FAILED:: akses file gagal");
				successMessageList.add(map);
			}
		
			String reg_id = "";
			for(int i = 1 ; i < excelList.size() ; i++){
				if(!excelList.get(i).get(0).toString().isEmpty()){
					Map<String,String> map = new HashMap<String, String>();
					try{
						pasExcel = new Pas();
						SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						Calendar cal = Calendar.getInstance();
						
						reg_id = excelList.get(i).get(0).toString();
						Date beg_date = df.parse(excelList.get(i).get(1).toString());
						Date end_date = (Date) beg_date.clone();
						end_date.setYear(end_date.getYear()+1);
						end_date.setDate(end_date.getDate()-1);
						
					
//						//VALIDASI FIELD EXCEL		
					    err = "";
					    
					    if(Common.isEmpty(reg_id)){
							err = err+ " REG ID harus diisi,";
						}
//										
						if(Common.isEmpty(beg_date)){
							err = err+ " BEGDATE harus diisi dgn Format: MM/DD/YYYY,";
						}
						
//												
						if("".equals(err)){
							// Proses Update Data PAS 
							Map result = uwManager.fixBegDatePas(reg_id, beg_date, end_date, lus_id);		
							Integer problem = (Integer) result.get("err");
							String nama_posisi = (String) result.get("position");
							// Error Update Data
							if(problem == 1){
								map.put("sts", "FAILED");
								map.put("msg", "FAILED:: "+reg_id+"("+nama_posisi+") : Proses Update Gagal");							
								successMessageList.add(map);
							}else if(problem == 2){
								map.put("sts", "FAILED");
								map.put("msg", "FAILED:: "+reg_id+"("+nama_posisi+") : Data tidak ditemukan");							
								successMessageList.add(map);
							}else if(problem == 3){
								map.put("sts", "FAILED");
								map.put("msg", "FAILED:: "+reg_id+"("+nama_posisi+") : Data tidak dapat diproses karena posisi dokumen");							
								successMessageList.add(map);
							}else{
								map.put("sts", "SUCCEED");
								map.put("msg", "SUCCEED:: "+reg_id+"("+nama_posisi+") : Proses Update Sukses");									
								successMessageList.add(map);
							}	
						}else{
							// Error Validasi Data
							 map.put("sts", "FAILED");
							 map.put("msg", "FAILED:: "+reg_id+"(-) : "+err);					
							successMessageList.add(map);
						}
//						
				}catch (Exception e) {
					logger.error("ERROR :", e);
					map.put("sts", "FAILED");
					map.put("msg", "FAILED:: "+reg_id+"(-) : Proses Update Gagal");		
						successMessageList.add(map);
				}
				}
			}
		}		
		request.setAttribute("successMessage",successMessageList);		
		return new ModelAndView("uw/fix/fix_pas_begdate", cmd);
    }	
}