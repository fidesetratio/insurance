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
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.bac.support.form_agen;
import com.ekalife.utils.CheckSum;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentFormController;
import com.ekalife.utils.parent.ParentMultiController;
import com.ekalife.utils.view.XLSCreatorFreeSimasRumah;
import com.ekalife.utils.ExcelRead;

public class InputPasUploadController extends ParentMultiController{
	
	public ModelAndView input_pas_upload( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {

		//User currentUser = (User) session.getAttribute("currentUser");
		Map<String, Object> cmd = new HashMap<String, Object>();
		
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		cmd.put("user_id", lus_id);
		
		String submit = request.getParameter("upload");
		List<String> successMessageList = new ArrayList<String>();
		
		if(submit != null){
		
		Upload upload = new Upload();
		upload.setDaftarFile(new ArrayList<MultipartFile>(10));
		ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
		binder.bind(request);
		MultipartFile mf = upload.getFile1();
		String fileName = mf.getOriginalFilename();
		String directory = props.getProperty("temp.dir.fileupload"); //file_fp.replaceAll(fileName, "");
		
		String dest=directory + "/" + fileName ;
		File outputFile = new File(dest);
	    FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
		
		ExcelRead excelRead = new ExcelRead();
		Pas pasExcel = null;
		List<List> pasExcelList = excelRead.readExcelFile(directory + "\\", fileName);
		
		int index = 1;
		for(int i = 1 ; i < pasExcelList.size() ; i++){
			try{
				pasExcel = new Pas();
				Date end_date = elionsManager.selectSysdate();
				end_date.setYear(end_date.getYear()+1);
				end_date.setDate(end_date.getDate()-1);
				
				pasExcel.setMsp_pas_beg_date(elionsManager.selectSysdate());
				pasExcel.setMsp_pas_end_date(end_date);
				pasExcel.setNo_kartu(pasExcelList.get(i).get(0).toString());
				pasExcel.setPin(pasExcelList.get(i).get(1).toString());
				//data pemegang polis
				pasExcel.setMsp_pas_nama_pp(pasExcelList.get(i).get(2).toString());
				pasExcel.setMsp_pas_tmp_lhr_pp(pasExcelList.get(i).get(3).toString());
				pasExcel.setMsp_pas_dob_pp (new Date(pasExcelList.get(i).get(4).toString()));
				pasExcel.setMsp_sex_pp(Integer.parseInt(pasExcelList.get(i).get(5).toString().replace(".0", "")));
				pasExcel.setMsp_identity_no(pasExcelList.get(i).get(6).toString());
				pasExcel.setMsp_address_1(pasExcelList.get(i).get(7).toString());
				pasExcel.setMsp_city(pasExcelList.get(i).get(8).toString());
				pasExcel.setMsp_postal_code(pasExcelList.get(i).get(9).toString());
				pasExcel.setMsp_pas_phone_number(pasExcelList.get(i).get(10).toString());
				pasExcel.setMsp_mobile(pasExcelList.get(i).get(11).toString());
				pasExcel.setMsp_pas_email(pasExcelList.get(i).get(12).toString());
				pasExcel.setLsre_id(Integer.parseInt(pasExcelList.get(i).get(13).toString().replace(".0", "")));
				//data tertanggung
				pasExcel.setMsp_full_name(pasExcelList.get(i).get(14).toString());
				pasExcel.setMsp_pas_tmp_lhr_tt(pasExcelList.get(i).get(15).toString());
				pasExcel.setMsp_date_of_birth(new Date(pasExcelList.get(i).get(16).toString()));
				//data PAS
				pasExcel.setProduk(Integer.parseInt(pasExcelList.get(i).get(17).toString().replace(".0", "")));
				pasExcel.setMsp_flag_cc(Integer.parseInt(pasExcelList.get(i).get(18).toString().replace(".0", "")));
				pasExcel.setLscb_id(Integer.parseInt(pasExcelList.get(i).get(19).toString().replace(".0", "")));
				//data agen
				pasExcel.setMsag_id(pasExcelList.get(i).get(20).toString());
				pasExcel.setKode_ao(pasExcelList.get(i).get(21).toString());
				pasExcel.setPribadi(Integer.parseInt(pasExcelList.get(i).get(22).toString().replace(".0", "")));
				//data rekening pemegang polis
				pasExcel.setLsbp_id(pasExcelList.get(i).get(23).toString());//cari
				pasExcel.setMsp_no_rekening(pasExcelList.get(i).get(24).toString());
				pasExcel.setMsp_rek_cabang(pasExcelList.get(i).get(25).toString());
				pasExcel.setMsp_rek_kota(pasExcelList.get(i).get(26).toString());
				pasExcel.setMsp_rek_nama(pasExcelList.get(i).get(27).toString());
				//data rekening pemegang polis (autodebet)
				pasExcel.setLsbp_id_autodebet(pasExcelList.get(i).get(28).toString());//cari
				pasExcel.setMsp_no_rekening_autodebet(pasExcelList.get(i).get(29).toString());
				pasExcel.setMsp_rek_nama_autodebet(pasExcelList.get(i).get(30).toString());
				if(!"".equals(pasExcelList.get(i).get(31).toString())){
					pasExcel.setMsp_tgl_debet(new Date(pasExcelList.get(i).get(31).toString()));
				}
				if(!"".equals(pasExcelList.get(i).get(32).toString())){
					pasExcel.setMsp_tgl_valid(new Date(pasExcelList.get(i).get(32).toString()));
				}
				//asuransi kebakaran
				pasExcel.setMsp_fire_code_name(pasExcelList.get(i).get(33).toString());
				pasExcel.setMsp_fire_name(pasExcelList.get(i).get(34).toString());
				pasExcel.setMsp_fire_identity(pasExcelList.get(i).get(35).toString());
				pasExcel.setMsp_fire_date_of_birth2(pasExcelList.get(i).get(36).toString());
				pasExcel.setMsp_fire_occupation2(pasExcelList.get(i).get(37).toString());
				pasExcel.setMsp_fire_occupation(pasExcelList.get(i).get(38).toString());
				pasExcel.setMsp_fire_type_business2(pasExcelList.get(i).get(39).toString());
				pasExcel.setMsp_fire_type_business(pasExcelList.get(i).get(40).toString());
				pasExcel.setMsp_fire_source_fund2(pasExcelList.get(i).get(41).toString());
				pasExcel.setMsp_fire_source_fund(pasExcelList.get(i).get(42).toString());
				pasExcel.setMsp_fire_addr_code(pasExcelList.get(i).get(43).toString());
				pasExcel.setMsp_fire_address_1(pasExcelList.get(i).get(44).toString());
				pasExcel.setMsp_fire_postal_code(pasExcelList.get(i).get(45).toString());
				pasExcel.setMsp_fire_phone_number(pasExcelList.get(i).get(46).toString());
				pasExcel.setMsp_fire_mobile(pasExcelList.get(i).get(47).toString());
				pasExcel.setMsp_fire_email(pasExcelList.get(i).get(48).toString());
				//informasi obyek pertanggungan
				pasExcel.setMsp_fire_insured_addr_code(pasExcelList.get(i).get(49).toString());
				pasExcel.setMsp_fire_insured_addr(pasExcelList.get(i).get(50).toString());
				pasExcel.setMsp_fire_insured_addr_no(pasExcelList.get(i).get(51).toString());
				pasExcel.setMsp_fire_insured_postal_code(pasExcelList.get(i).get(52).toString());
				pasExcel.setMsp_fire_insured_city(pasExcelList.get(i).get(53).toString());
				pasExcel.setMsp_fire_insured_phone_number(pasExcelList.get(i).get(54).toString());
				pasExcel.setMsp_fire_insured_addr_envir(Integer.parseInt(pasExcelList.get(i).get(55).toString().replace(".0", "")));
				pasExcel.setMsp_fire_ins_addr_envir_else(pasExcelList.get(i).get(56).toString());
				pasExcel.setMsp_fire_okupasi(pasExcelList.get(i).get(57).toString());
				pasExcel.setMsp_fire_okupasi_else(pasExcelList.get(i).get(58).toString());
				//=============
				if(pasExcel.getReg_spaj() == null)pasExcel.setReg_spaj("");
				if(pasExcel.getPribadi() == null)pasExcel.setPribadi(0);
				if(pasExcel.getMsp_fire_name() != null){
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					Date begDate = elionsManager.selectSysdate();
					Date endDate = elionsManager.selectSysdate();
					endDate.setMonth(endDate.getMonth() + 6);
					endDate.setDate(endDate.getDate() - 1);
					pasExcel.setMsp_fire_beg_date(begDate);
					pasExcel.setMsp_fire_end_date(endDate);
				}
				pasExcel.setLus_id(lus_id);
				pasExcel.setLus_login_name(user.getLus_full_name());
				pasExcel.setMsp_flag_pas(1);
				pasExcel = uwManager.insertPas(pasExcel, user);
				if(pasExcel.getStatus() == 1){
					//request.setAttribute("successMessage","proses insert gagal");
					//successMessageList.add(index+". "+"proses insert gagal");
					successMessageList.add("proses insert gagal");
				}else{
	//				Integer msp_id = uwManager.selectGetPasIdFromFireId(pas.getMsp_fire_id());
	//				request.setAttribute("msp_id",msp_id);
					//successMessageList.add(index+". "+"insert sukses. Fire Id : "+pasExcel.getMsp_fire_id());
					successMessageList.add("insert sukses. Fire Id : "+pasExcel.getMsp_fire_id());
					//request.setAttribute("successMessage","insert sukses. Fire Id : "+pas.getMsp_fire_id());
				}
			}catch (Exception e) {
				//successMessageList.add(index+". "+"proses insert gagal");
				successMessageList.add("proses insert gagal");
			}
			index++;
		}
		}
		
		request.setAttribute("successMessage",successMessageList);
		
		return new ModelAndView("uw/input_pas_upload", cmd);

    }
	
}