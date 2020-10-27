package com.ekalife.elions.web.bas;

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
import java.util.Properties;

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
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.elions.web.bac.support.form_agen;
import com.ekalife.utils.CheckSum;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentController;
import com.ekalife.utils.parent.ParentFormController;
import com.ekalife.utils.parent.ParentMultiController;
import com.ekalife.utils.view.XLSCreatorFreeSimasRumah;
import com.ekalife.utils.ExcelRead;

public class AgencyUploadFormController extends ParentController{

	public ModelAndView handleRequest( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {

		Map<String, Object> cmd = new HashMap<String, Object>();
		String err = "";
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		cmd.put("user_id", lus_id);
		
		//untuk uploadan dari pas ap/bp online
		String pas_reg_id = null;
		//===========
		
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
			Mia agencyExcel = null;
			List<List> agencyExcelList = new ArrayList<List>();
					
			try{
				String dest=directory + "/" + fileName ;
				File outputFile = new File(dest);
			    FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
				agencyExcelList = excelRead.readExcelFile(directory + "\\", fileName);
			}catch (Exception e) {
				Map<String,String> map = new HashMap<String, String>();
				map.put("sts", "FAILED");
				map.put("msg", "FAILED:: akses file gagal");
				successMessageList.add(map);
			}
			
			for(int i = 1 ; i < agencyExcelList.size() ; i++){
				if(agencyExcelList.get(i) != null){
				if(agencyExcelList.get(i).size() > 0){
				if(!agencyExcelList.get(i).get(0).toString().isEmpty()){
					Map<String,String> map = new HashMap<String, String>();
					try{
						agencyExcel = new Mia();
						SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						Calendar cal = Calendar.getInstance();
						
						//otomatis
							agencyExcel.setLus_id(Integer.parseInt(user.getLus_id()));
							agencyExcel.setMia_input_date(df.format(elionsManager.selectSysdate()));
							agencyExcel.setMia_tgl_aktif(df.format(elionsManager.selectSysdate()));		
							agencyExcel.setMia_awal_kontrak(df.format(elionsManager.selectSysdate()));		
							agencyExcel.setMia_tgl_berkas(df.format(elionsManager.selectSysdate()));	
							agencyExcel.setLca_id("37");//agency system	
							agencyExcel.setStatus(0);
							cal.setTime(elionsManager.selectFAddMonths(agencyExcel.getMia_awal_kontrak(), 12));
							cal.add(Calendar.DATE, -1);
							agencyExcel.setMia_akhir_kontrak(df.format(cal.getTime()));
						//
						agencyExcel.setMia_nama(agencyExcelList.get(i).get(0).toString());
						agencyExcel.setMia_birth_place(agencyExcelList.get(i).get(1).toString());
						agencyExcel.setMia_birth_date(agencyExcelList.get(i).get(2).toString());
						agencyExcel.setMia_alamat(agencyExcelList.get(i).get(3).toString());
						agencyExcel.setMia_ktp(agencyExcelList.get(i).get(4).toString());
						agencyExcel.setMia_recruiter(agencyExcelList.get(i).get(5).toString());
						
						agencyExcel.setPendidikan(agencyExcelList.get(i).get(8).toString());//pendidikan
						agencyExcel.setStatus_nikah(agencyExcelList.get(i).get(9).toString());//status
						
						//untuk uploadan dari pas ap/bp online
						if(agencyExcelList.get(i).get(6) != null){
							if("AP/BP ONLINE".equals(agencyExcelList.get(i).get(6).toString())){
								pas_reg_id = agencyExcelList.get(i).get(7).toString();
							}
						}
						//================
						
						//validasi & set nilai
						err = "";
						if(!agencyExcel.getMia_nama().equals("") && !agencyExcel.getMia_birth_date().equals("")) {
							Integer ll_row = uwManager.getAgenBlackList(agencyExcel.getMia_nama(),agencyExcel.getMia_birth_date());
							if(ll_row > 0)
								err = "Agent ini masuk Attention List..!!!";
						}
						
						if(!agencyExcel.getMia_recruiter().equals("")) {
							Integer ll_row = uwManager.wf_cek_rekruter(agencyExcel.getMia_recruiter().replace(".0", ""));
							if(ll_row <= 0)
								err = "Kode Rekruiter Tidak Ada / Tidak Aktif";
							else 
								agencyExcel.setMia_level_recruit(uwManager.getLevelRecruiter(agencyExcel.getMia_recruiter().replace(".0", ""),null));
						}
		//				if(cmd.getMia().getFlagExist() == 0) {
		//					if(!cmd.getMia().getMia_nama().equals("") && !cmd.getMia().getMia_birth_date().equals("")) {
		//						String msag_id = uwManager.isAgentExist(cmd.getMia().getMia_nama(),cmd.getMia().getMia_birth_date());
		//						if(!msag_id.equals("")) {
		//							cmd.getMia().setFlagExist(1);
		//							err.reject("","Agent "+msag_id+ " memiliki data nama dan tanggal lahir yang sama dengan agent yang diinput. Tekan Simpan jika tetep ingin melanjutkan");
		//						}					
		//					}
		//				}
						//
						if("".equals(err)){
							agencyExcel.setMia_agensys_id(uwManager.saveMasterInputAgenUpload(agencyExcel, pas_reg_id));
						}else{
							map.put("sts", "FAILED");
							map.put("msg", "FAILED:: "+agencyExcelList.get(i).get(0).toString()+"("+agencyExcelList.get(i).get(4).toString()+") : "+err);
							successMessageList.add(map);
						}
						
						if(agencyExcel.getMia_agensys_id() != null){
							if(agencyExcel.getMia_agensys_id().length() > 10) {
								map.put("sts", "FAILED");
								map.put("msg", "FAILED:: "+agencyExcelList.get(i).get(0).toString()+"("+agencyExcelList.get(i).get(4).toString()+") : "+agencyExcel.getMia_agensys_id());
								successMessageList.add(map);
							}else{
								map.put("sts", "SUCCEED");
								map.put("msg", "SUCCEED:: "+agencyExcelList.get(i).get(0).toString()+"("+agencyExcelList.get(i).get(4).toString()+") : Register - "+agencyExcel.getMia_agensys_id());
								successMessageList.add(map);
							}
						}else if(agencyExcel.getMia_agensys_id() == null){
							map.put("sts", "FAILED");
							map.put("msg", "FAILED:: "+agencyExcelList.get(i).get(0).toString()+"("+agencyExcelList.get(i).get(4).toString()+") : "+"-");
							successMessageList.add(map);
						}
					}catch (Exception e) {
						logger.error("ERROR :", e);
						map.put("sts", "FAILED");
						map.put("msg", "FAILED:: "+agencyExcelList.get(i).get(0).toString()+"("+agencyExcelList.get(i).get(4).toString()+") : proses insert gagal");
						successMessageList.add(map);
					}
				}
				}
				}
			}
		}
		
		request.setAttribute("successMessage",successMessageList);
		
		return new ModelAndView("bas/agency_upload", cmd);

    }
	
}