package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.spring.util.Email;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.AddressNew;
import com.ekalife.elions.model.BlackList;
import com.ekalife.elions.model.BlackListFamily;
import com.ekalife.elions.model.Client;
import com.ekalife.elions.model.CmdInputBlacklist;
import com.ekalife.elions.model.DetBlackList;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentController;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import org.apache.commons.fileupload.FileUploadException;



public class InputBlacklistCustController extends ParentFormController{
	private long accessTime;
	protected final Log logger = LogFactory.getLog(getClass());
	private Email email;
		
	protected String dok_att_list;
	
	public void setEmail(Email email) {
		this.email = email;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		
		CmdInputBlacklist detiledit = (CmdInputBlacklist) cmd;
		String mclId = request.getParameter("kopiMclId");
		String baru = request.getParameter("baru");
		String addnewrow = request.getParameter("addnewrow");
		String addnewalias = request.getParameter("addnewalias");
		String addnewidentitas = request.getParameter("addnewidentitas");
		String delIndex = request.getParameter("delIndex");
		
			   dok_att_list = request.getParameter("doc_att_list");		
		String download = request.getParameter("downloadbutton");
		String upload= request.getParameter("upload");
		
		if(delIndex == null)delIndex="";		
//		if(mclId!="") {
//			//kalau pakai tombol cari
//			request.setAttribute("flagSave", "save");
//			request.setAttribute("kopiMclId", mclId);
//		}
		
				
		
	if((!"CARI".equals(request.getParameter("submitKopiMclId")) && !"BARU".equals(baru)) && !"DOWNLOAD".equals(download) && !"Upload".equals(upload)|| "ADD".equals(addnewrow) || "ADD".equals(addnewalias) || "ADD".equals(addnewidentitas) || !"".equals(delIndex)) {
	        
			if(detiledit.getClient().getMcl_first() == null){detiledit.getClient().setMcl_first("");}
			String i_ktp = request.getParameter("i_ktp");String i_sim = request.getParameter("i_sim");String i_kk = request.getParameter("i_kk");
			String i_paspor = request.getParameter("i_paspor");String i_akte_lahir = request.getParameter("i_akte_lahir");String i_kims_kitas = request.getParameter("i_kims_kitas");
			String alias_1 = request.getParameter("alias_1");String alias_2 = request.getParameter("alias_2");String alias_3 = request.getParameter("alias_3");
			String alias_4 = request.getParameter("alias_4");String alias_5 = request.getParameter("alias_5");
			if("ADD".equals(addnewrow)){
				DetBlackList detBlackList = new DetBlackList();
				detBlackList.setLdbl_diagnosa("");
				detBlackList.setLdbl_tgl_kejadian2("");
				detBlackList.setLdbl_tgl_kejadian_to2("");
				detiledit.getDetBlackListAll().add(detBlackList);
			}else if("ADD".equals(addnewalias)){
				request.setAttribute("errAlias", "");
				String maxAlias = request.getParameter("maxAlias");
				if("0".equals(maxAlias)){alias_1 = "1";}
				else if("1".equals(maxAlias)){alias_2 = "1";}
				else if("2".equals(maxAlias)){alias_3 = "1";}
				else if("3".equals(maxAlias)){alias_4 = "1";}
				else if("4".equals(maxAlias)){alias_5 = "1";
				}
				if("1".equals(alias_1)&&"1".equals(alias_2)&&"1".equals(alias_3)&&"1".equals(alias_4)&&"1".equals(alias_5)){request.setAttribute("errAlias", "(MAX)");}
			}else if("ADD".equals(addnewidentitas)){
				request.setAttribute("errIdentitas", "");
				String lside_id = request.getParameter("lside_id");
				if(lside_id != null){
				if("1".equals(lside_id)){i_ktp = "1";
				}else if("2".equals(lside_id)){i_sim = "1";
				}else if("3".equals(lside_id)){i_paspor = "1";
				}else if("4".equals(lside_id)){i_kk = "1";
				}else if("5".equals(lside_id)){i_akte_lahir = "1";
				}else if("7".equals(lside_id)){i_kims_kitas = "1";
				}
				}
			}else if(!"".equals(delIndex)){
				detiledit.getDetBlackListAll().remove(Integer.parseInt(delIndex));
			}
			request.setAttribute("i_ktp", i_ktp);request.setAttribute("i_sim", i_sim);request.setAttribute("i_kk", i_kk);
			request.setAttribute("i_paspor", i_paspor);request.setAttribute("i_akte_lahir", i_akte_lahir);request.setAttribute("i_kims_kitas", i_kims_kitas);
			request.setAttribute("alias_1", alias_1);request.setAttribute("alias_2", alias_2);request.setAttribute("alias_3", alias_3);
			request.setAttribute("alias_4", alias_4);request.setAttribute("alias_5", alias_5);
			if(!"".equals(detiledit.getBlacklist().getLbl_no_identity())){detiledit.getClient().setLside_id(1);}
			else if(!"".equals(detiledit.getBlacklist().getLbl_no_identity_sim())){detiledit.getClient().setLside_id(2);}
			else if(!"".equals(detiledit.getBlacklist().getLbl_no_identity_paspor())){detiledit.getClient().setLside_id(3);}
			else if(!"".equals(detiledit.getBlacklist().getLbl_no_identity_kk())){detiledit.getClient().setLside_id(4);}
			else if(!"".equals(detiledit.getBlacklist().getLbl_no_identity_akte_lahir())){detiledit.getClient().setLside_id(5);}
			else if(!"".equals(detiledit.getBlacklist().getLbl_no_identity_kims_kitas())){detiledit.getClient().setLside_id(7);}
			else{detiledit.getClient().setLside_id(0);}
			String fontColor = "black";
	        if(detiledit.getClient() != null && detiledit.getBlacklist() != null){
	        	//if(detiledit.getClient().getMcl_blacklist() != null){
	        		//if("2".equals(detiledit.getBlacklist().getLbl_sts_cust()) && detiledit.getClient().getMcl_blacklist() == 1){
	        		if("2".equals(detiledit.getBlacklist().getLbl_sts_cust()) && detiledit.getBlacklist().getLbl_nama() != null){
	        			fontColor = "blue";
	        		}
	        	//}
	        	String regSpaj="";
	        	if(detiledit.getBlacklist() != null){
					regSpaj = detiledit.getBlacklist().getReg_spaj();
	        	}else if(detiledit.getBlacklist() == null){
					detiledit.setBlacklist(new BlackList());
					regSpaj = request.getParameter("regSpaj");
					detiledit.getBlacklist().setReg_spaj(regSpaj);
					detiledit.getBlacklist().setLbl_sumber_info("");
					detiledit.getBlacklist().setLbl_sumber_info2("");
					detiledit.getBlacklist().setLbl_sts_cust("");
				}
	        	if(regSpaj == null)regSpaj="";
				if(!"".equals(regSpaj)){
					detiledit.getBlacklist().setLbl_sts_cust("2");
					request.setAttribute("regSpaj", regSpaj);
					request.setAttribute("statusForm", "true");
				}else{
					request.setAttribute("regSpaj", regSpaj);
					request.setAttribute("statusForm", "");
				}
				if(mclId == null)mclId = "";
				if(!"".equals(mclId)){
					request.setAttribute("kopiMclId", mclId);
				}
	        }
	        request.setAttribute("fontColor", fontColor);
	      	        
			detiledit.getBlacklist().setLbl_alamat(detiledit.getAddressNew().getAlamat_rumah());
			detiledit.getBlacklist().setLbl_nama(detiledit.getClient().getMcl_first());
			if(detiledit.getClient().getMspe_date_birth() != null){
				detiledit.getBlacklist().setLbl_tgl_lahir(detiledit.getClient().getMspe_date_birth());
			}
			List lsJnsId=elionsManager.selectAllLstIdentity();
			request.setAttribute("lsJnsId", lsJnsId);
								
			//null to empty string
			if(detiledit.getBlacklist().getReg_spaj() == null){detiledit.getBlacklist().setReg_spaj("");}
			if(detiledit.getBlacklist().getLbl_sumber_info() == null){detiledit.getBlacklist().setLbl_sumber_info("");}
			if(detiledit.getBlacklist().getLbl_sumber_info2() == null){detiledit.getBlacklist().setLbl_sumber_info2("");}
			if(detiledit.getBlacklist().getLbl_sumber_informasi() == null){detiledit.getBlacklist().setLbl_sumber_informasi("");}
			if(detiledit.getBlacklist().getLbl_tgl_lahir2() == null){detiledit.getBlacklist().setLbl_tgl_lahir2("");}
			if(detiledit.getBlacklist().getLbl_sts_cust() == null){detiledit.getBlacklist().setLbl_sts_cust("");}
			if(detiledit.getBlacklist().getLbl_nama_alias_1() == null){detiledit.getBlacklist().setLbl_nama_alias_1("");}
			if(detiledit.getBlacklist().getLbl_nama_alias_2() == null){detiledit.getBlacklist().setLbl_nama_alias_2("");}
			if(detiledit.getBlacklist().getLbl_nama_alias_3() == null){detiledit.getBlacklist().setLbl_nama_alias_3("");}
			if(detiledit.getBlacklist().getLbl_nama_alias_4() == null){detiledit.getBlacklist().setLbl_nama_alias_4("");}
			if(detiledit.getBlacklist().getLbl_nama_alias_5() == null){detiledit.getBlacklist().setLbl_nama_alias_5("");}
			if(detiledit.getBlacklist().getLbl_no_identity() == null){detiledit.getBlacklist().setLbl_no_identity("");}
			if(detiledit.getBlacklist().getLbl_no_identity_kk() == null){detiledit.getBlacklist().setLbl_no_identity_kk("");}
			if(detiledit.getBlacklist().getLbl_no_identity_sim() == null){detiledit.getBlacklist().setLbl_no_identity_sim("");}
			if(detiledit.getBlacklist().getLbl_no_identity_paspor() == null){detiledit.getBlacklist().setLbl_no_identity_paspor("");}
			if(detiledit.getBlacklist().getLbl_no_identity_kims_kitas() == null){detiledit.getBlacklist().setLbl_no_identity_kims_kitas("");}
			if(detiledit.getBlacklist().getLbl_no_identity_akte_lahir() == null){detiledit.getBlacklist().setLbl_no_identity_akte_lahir("");}
//			if(detiledit.getBlacklist().getLbl_tgl_kejadian2() == null){detiledit.getBlacklist().setLbl_tgl_kejadian2("");}
//			if(detiledit.getBlacklist().getLbl_tgl_kejadian_to2() == null){detiledit.getBlacklist().setLbl_tgl_kejadian_to2("");}
//			if(detiledit.getDetBlacklist().getLdbl_tgl_kejadian2() == null){detiledit.getDetBlacklist().setLdbl_tgl_kejadian2("");}
//			if(detiledit.getDetBlacklist().getLdbl_tgl_kejadian_to2() == null){detiledit.getDetBlacklist().setLdbl_tgl_kejadian_to2("");}
//			if(detiledit.getDetBlacklist().getLdbl_diagnosa() == null){detiledit.getDetBlacklist().setLdbl_diagnosa("");}
			for(int i = 0 ; i < detiledit.getDetBlackListAll().size() ; i++){
				if(detiledit.getDetBlackListAll().get(i).getLdbl_tgl_kejadian2() == null){detiledit.getDetBlackListAll().get(i).setLdbl_tgl_kejadian2("");}
				if(detiledit.getDetBlackListAll().get(i).getLdbl_tgl_kejadian_to2() == null){detiledit.getDetBlackListAll().get(i).setLdbl_tgl_kejadian_to2("");}
				if(detiledit.getDetBlackListAll().get(i).getLdbl_diagnosa() == null){detiledit.getDetBlackListAll().get(i).setLdbl_diagnosa("");}
			}
			if(detiledit.getBlacklist().getLbl_flag_alasan() == null){detiledit.getBlacklist().setLbl_flag_alasan("");}
			if(detiledit.getBlacklist().getLbl_alasan() == null){detiledit.getBlacklist().setLbl_alasan("");}
			if(detiledit.getClient().getMcl_first() == null){detiledit.getClient().setMcl_first("");}
			if(detiledit.getClient().getMspe_place_birth() == null){detiledit.getClient().setMspe_place_birth("");}
			if(detiledit.getClient().getMspe_date_birth2() == null){detiledit.getClient().setMspe_date_birth2("");}
			if(detiledit.getClient().getMspe_sex() == null){detiledit.getClient().setMspe_sex(null);}
			if(detiledit.getClient().getLside_id() == null){detiledit.getClient().setLside_id(null);}
			if(detiledit.getClient().getMspe_no_identity() == null){detiledit.getClient().setMspe_no_identity("");}
			if(detiledit.getClient().getMspe_sts_mrt() == null){detiledit.getClient().setMspe_sts_mrt("");}
			//if(detiledit.getClient().getMcl_blacklist() == null){detiledit.getClient().setMcl_blacklist(0);}
			if(detiledit.getBlacklistfamily().getLblf_tgllhr_sis() == null){detiledit.getBlacklistfamily().setLblf_tgllhr_sis("");}
			if(detiledit.getBlacklistfamily().getLblf_nama_si() == null){detiledit.getBlacklistfamily().setLblf_nama_si("");}
			if(detiledit.getBlacklistfamily().getLblf_tgllhr_anak1s() == null){detiledit.getBlacklistfamily().setLblf_tgllhr_anak1s("");}
			if(detiledit.getBlacklistfamily().getLblf_nama_anak1() == null){detiledit.getBlacklistfamily().setLblf_nama_anak1("");}
			if(detiledit.getBlacklistfamily().getLblf_tgllhr_anak2s() == null){detiledit.getBlacklistfamily().setLblf_tgllhr_anak2s("");}
			if(detiledit.getBlacklistfamily().getLblf_nama_anak2() == null){detiledit.getBlacklistfamily().setLblf_nama_anak2("");}
			if(detiledit.getBlacklistfamily().getLblf_tgllhr_anak3s() == null){detiledit.getBlacklistfamily().setLblf_tgllhr_anak3s("");}
			if(detiledit.getBlacklistfamily().getLblf_nama_anak3() == null){detiledit.getBlacklistfamily().setLblf_nama_anak3("");}
			if(detiledit.getAddressNew().getAlamat_rumah() == null){detiledit.getAddressNew().setAlamat_rumah("");}
			if(detiledit.getAddressNew().getTelpon_rumah() == null){detiledit.getAddressNew().setTelpon_rumah("");}
			if(detiledit.getAddressNew().getTelpon_kantor() == null){detiledit.getAddressNew().setTelpon_kantor("");}
			if(detiledit.getAddressNew().getArea_code_rumah() == null){detiledit.getAddressNew().setArea_code_rumah("");}
			if(detiledit.getAddressNew().getArea_code_kantor() == null){detiledit.getAddressNew().setArea_code_kantor("");}
			if(detiledit.getAddressNew().getNo_hp() == null){detiledit.getAddressNew().setNo_hp("");}
			if(detiledit.getAddressNew().getEmail() == null){detiledit.getAddressNew().setEmail("");}
			
			DateFormat myDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			if(!detiledit.getClient().getMspe_date_birth2().equals("")){
			//if(detiledit.getClient().getMspe_date_birth2() != ""){
				detiledit.getClient().setMspe_date_birth(myDateFormat.parse(detiledit.getClient().getMspe_date_birth2()));
			}
			for(int i = 0 ; i < detiledit.getDetBlackListAll().size() ; i++){
				//if(detiledit.getDetBlackListAll().get(i).getLdbl_tgl_kejadian2() != ""){
				if(!detiledit.getDetBlackListAll().get(i).getLdbl_tgl_kejadian2().equals("")){	
					detiledit.getDetBlackListAll().get(i).setLdbl_tgl_kejadian(myDateFormat.parse(detiledit.getDetBlackListAll().get(i).getLdbl_tgl_kejadian2()));
				}
				//if(detiledit.getDetBlackListAll().get(i).getLdbl_tgl_kejadian_to2() != ""){
				if(!detiledit.getDetBlackListAll().get(i).getLdbl_tgl_kejadian2().equals("")){		
					detiledit.getDetBlackListAll().get(i).setLdbl_tgl_kejadian_to(myDateFormat.parse(detiledit.getDetBlackListAll().get(i).getLdbl_tgl_kejadian_to2()));
				}
			}
//			if(detiledit.getDetBlacklist().getLdbl_tgl_kejadian2() != ""){
//				detiledit.getDetBlacklist().setLdbl_tgl_kejadian(myDateFormat.parse(detiledit.getDetBlacklist().getLdbl_tgl_kejadian2()));
//			}
//			if(detiledit.getDetBlacklist().getLdbl_tgl_kejadian_to2() != ""){
//				detiledit.getDetBlacklist().setLdbl_tgl_kejadian_to(myDateFormat.parse(detiledit.getDetBlacklist().getLdbl_tgl_kejadian_to2()));
//			}
			
			String flagConfSave = request.getParameter("flagConfSave");
			
			if("ADD".equals(addnewrow) || "ADD".equals(addnewalias) || "ADD".equals(addnewidentitas) || !"".equals(delIndex) ){
				request.setAttribute("addError", "addError");
				err.reject("");
			}else
			if("YES".equals(flagConfSave)){
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "client.mcl_first", "","Nama Lengkap harus diisi");
				if(!(detiledit.getBlacklist().getLbl_sumber_info2().equals("4") || detiledit.getBlacklist().getLbl_sumber_info2().equals("5"))){
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "client.mspe_date_birth", "","tanggal lahir harus diisi");
				}
				//ValidationUtils.rejectIfEmptyOrWhitespace(err, "client.mspe_no_identity", "","no.bukti identitas harus diisi");
			}else if(!"YES".equals(flagConfSave)){
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklist.lbl_sumber_info", "","Sumber Informasi 1 harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklist.lbl_sumber_info2", "","Sumber Informasi 2 harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklist.lbl_sumber_informasi", "","Sumber Informasi 3 harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklist.lbl_sts_cust", "","Status Customer harus diisi");
				//ValidationUtils.rejectIfEmptyOrWhitespace(err, "client.mcl_id_new", "","teslscb_id");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "client.mcl_first", "","Nama Lengkap harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "client.mspe_place_birth", "","tempat lahir harus diisi");
				if(!(detiledit.getBlacklist().getLbl_sumber_info2().equals("4") || detiledit.getBlacklist().getLbl_sumber_info2().equals("5"))){
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "client.mspe_date_birth", "","tanggal lahir harus diisi");
				}
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "client.mspe_sex", "","jenis kelamin harus diisi");
				//ValidationUtils.rejectIfEmptyOrWhitespace(err, "client.lside_id", "","bukti identitas harus diisi");
				//ValidationUtils.rejectIfEmptyOrWhitespace(err, "client.mspe_no_identity", "","no.bukti identitas harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "client.mspe_sts_mrt", "","Status harus diisi");

				for(int i = 0 ; i < detiledit.getDetBlackListAll().size() ; i++){
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "detBlackListAll["+i+"].ldbl_tgl_kejadian2", "","tgl kejadian "+(i+1)+" harus diisi");
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "detBlackListAll["+i+"].ldbl_tgl_kejadian_to2", "","tgl kejadian s/d "+(i+1)+" harus diisi");
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "detBlackListAll["+i+"].ldbl_diagnosa", "","diagnosa "+(i+1)+" harus diisi");
				}
				
//				detiledit.getClient().setMspe_date_birth(new Date(detiledit.getClient().getMspe_date_birth2()));
//				detiledit.getBlacklist().setLbl_tgl_kejadian(new Date(detiledit.getBlacklist().getLbl_tgl_kejadian2()));
//				detiledit.getBlacklist().setLbl_tgl_kejadian_to(new Date(detiledit.getBlacklist().getLbl_tgl_kejadian_to2()));
				// untuk validasi keluarga==========================
//				detiledit.getBlacklistfamily().setLblf_tgllhr_si(new Date(detiledit.getBlacklistfamily().getLblf_tgllhr_sis()));
//				detiledit.getBlacklistfamily().setLblf_tgllhr_anak1(new Date(detiledit.getBlacklistfamily().getLblf_tgllhr_anak1s()));
//				detiledit.getBlacklistfamily().setLblf_tgllhr_anak2(new Date(detiledit.getBlacklistfamily().getLblf_tgllhr_anak2s()));
//				detiledit.getBlacklistfamily().setLblf_tgllhr_anak3(new Date(detiledit.getBlacklistfamily().getLblf_tgllhr_anak3s()));

				if(!detiledit.getBlacklistfamily().getLblf_tgllhr_sis().equals("")){
					detiledit.getBlacklistfamily().setLblf_tgllhr_si(myDateFormat.parse(detiledit.getBlacklistfamily().getLblf_tgllhr_sis()));
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklistfamily.lblf_nama_si", "","nama suami/istri harus diisi");
				}
				if(!detiledit.getBlacklistfamily().getLblf_nama_si().equals("")){
				//if(detiledit.getBlacklistfamily().getLblf_nama_si() != ""){
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklistfamily.lblf_tgllhr_si", "","tgl lahir suami/istri harus diisi");
				}
				if(!detiledit.getBlacklistfamily().getLblf_tgllhr_anak1s().equals("")){
				//if(detiledit.getBlacklistfamily().getLblf_tgllhr_anak1s() != ""){	
					detiledit.getBlacklistfamily().setLblf_tgllhr_anak1(myDateFormat.parse(detiledit.getBlacklistfamily().getLblf_tgllhr_anak1s()));
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklistfamily.lblf_nama_anak1", "","nama anak 1 harus diisi");
				}
				if(!detiledit.getBlacklistfamily().getLblf_nama_anak1().equals("")){
				//if(detiledit.getBlacklistfamily().getLblf_nama_anak1() != ""){	
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklistfamily.lblf_tgllhr_anak1", "","tgl lahir anak 1 harus diisi");
				}
				if(!detiledit.getBlacklistfamily().getLblf_tgllhr_anak2s().equals("")){
				//if(detiledit.getBlacklistfamily().getLblf_tgllhr_anak2s() != ""){
					detiledit.getBlacklistfamily().setLblf_tgllhr_anak2(myDateFormat.parse(detiledit.getBlacklistfamily().getLblf_tgllhr_anak2s()));
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklistfamily.lblf_nama_anak2", "","nama anak 2 harus diisi");
				}
				if(!detiledit.getBlacklistfamily().getLblf_nama_anak2().equals("")){
				//if(detiledit.getBlacklistfamily().getLblf_nama_anak2() != ""){	
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklistfamily.lblf_tgllhr_anak2", "","tgl lahir anak 2 harus diisi");
				}
				if(!detiledit.getBlacklistfamily().getLblf_tgllhr_anak3s().equals("")){
				//if(detiledit.getBlacklistfamily().getLblf_tgllhr_anak3s() != ""){	
					detiledit.getBlacklistfamily().setLblf_tgllhr_anak3(myDateFormat.parse(detiledit.getBlacklistfamily().getLblf_tgllhr_anak3s()));
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklistfamily.lblf_nama_anak3", "","nama anak 3 harus diisi");
				}
				if(!detiledit.getBlacklistfamily().getLblf_nama_anak3().equals("")){
				//if(detiledit.getBlacklistfamily().getLblf_nama_anak3() != ""){	
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklistfamily.lblf_tgllhr_anak3", "","tgl lahir anak 3 harus diisi");
				}
				//==================================================
				//ValidationUtils.rejectIfEmptyOrWhitespace(err, "client.mkl_kerja", "","pekerjaan harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "addressNew.alamat_rumah", "","alamat rumah harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "addressNew.area_code_rumah", "","code area telepon rumah harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "addressNew.telpon_rumah", "","telepon rumah harus diisi");
				//ValidationUtils.rejectIfEmptyOrWhitespace(err, "addressNew.alamat_kantor", "","alamat kantor harus diisi");
				//ValidationUtils.rejectIfEmptyOrWhitespace(err, "addressNew.telpon_kantor", "","telepon kantor harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "addressNew.no_hp", "","no.hp harus diisi");
				//ValidationUtils.rejectIfEmptyOrWhitespace(err, "client.mspe_email", "","email harus diisi");
				//ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklist.lbl_tgl_kejadian", "","tgl.kejadian/rawat harus diisi");
				//ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklist.lbl_diagnosa", "","diagnosa harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklist.lbl_flag_alasan", "","pilihan keterangan/alasan harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "blacklist.lbl_alasan", "","keterangan/alasan harus diisi");
									
			}
			
			if(detiledit.getBlacklist().getLbl_id() != null){
				Integer lbl_id = detiledit.getBlacklist().getLbl_id();
				String dir = props.getProperty("pdf.dir.attentionlist");
				String tDest = dir + "\\" + lbl_id.toString();
				List<DropDown> daftarAda = FileUtils.listFilesInDirectory(tDest);			
			request.setAttribute("daftarAda", daftarAda);   
			}
			 
		}
		//if(("".equals(detiledit.getClient().getMcl_id()) && "SAVE".equals(request.getParameter("save"))) || (!"".equals(detiledit.getClient().getMcl_id()) && detiledit.getClient().getMcl_blacklist() == 0 && "SAVE".equals(request.getParameter("save")))){
		if("".equals(detiledit.getClient().getMcl_id()) && "SAVE".equals(request.getParameter("save"))){
			Integer blacklistCount = uwManager.selectExistBlacklist(detiledit.getClient().getMspe_date_birth2(), detiledit.getClient().getMcl_first(), detiledit.getClient().getMspe_no_identity());
			if(blacklistCount > 0){
				request.setAttribute("addError", "addError");
				request.setAttribute("addDuplicateError", "addDuplicateError");
				String searchExistFlag = request.getParameter("searchExistFlag");
				if("EXIST".equals(searchExistFlag)){
					request.setAttribute("searchExistFlag", searchExistFlag);
				}
				err.reject("");
			}
		}		
		
	}
	
//	protected ModelAndView onAddnewrow( HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors ) throws Exception
//    {
//		CmdInputBlacklist detiledit = (CmdInputBlacklist) cmd;
//		
//		return new ModelAndView(
//        "uw/input_blacklist_customer").addObject("cmd",detiledit);
//    }

	protected ModelAndView onSubmit( HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors ) throws Exception
    {
		CmdInputBlacklist detiledit = (CmdInputBlacklist) cmd;
		
		List lsJnsId=elionsManager.selectAllLstIdentity();
		request.setAttribute("lsJnsId", lsJnsId);		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		detiledit.setCurrentUser(currentUser);
		detiledit.getClient().setLus_id(Integer.parseInt(currentUser.getLus_id()));
		
		String mclId = request.getParameter("kopiMclId");
		String getRegSpaj = request.getParameter("regSpaj");
		String getNoPolis = request.getParameter("noPolis");
				
		if(mclId == null)mclId = "";
		if(!"".equals(mclId)){
			request.setAttribute("kopiMclId", mclId);
		}
		
		// TEKAN TOMBOL CARI
        if("CARI".equals(request.getParameter("submitKopiMclId"))) {
        	String searchExistFlag = request.getParameter("searchExistFlag");
			if("EXIST".equals(searchExistFlag)){
				request.setAttribute("searchExistFlag", searchExistFlag);
			}
        	request.setAttribute("s", 3);
        	request.setAttribute("kopiMclId", mclId);
        	detiledit.setClient(new Client());
    		detiledit.setPemegang(new Pemegang());
    		detiledit.setBlacklist(new BlackList());
    		detiledit.setDetBlackListAll(new ArrayList<DetBlackList>());
    		detiledit.setBlacklistfamily(new BlackListFamily());
    		detiledit.setAddressNew(new AddressNew());
    			
			detiledit.setClient(uwManager.selectAllClientBlacklist(mclId));
			detiledit.setAddressNew(elionsManager.selectAllAddressNew(mclId));
			detiledit.setBlacklist(uwManager.selectAllBlacklist(detiledit.getClient().getMcl_first(), detiledit.getClient().getMspe_date_birth(), mclId));
			if(detiledit.getBlacklist() != null){
				if(getRegSpaj != null){
					detiledit.getBlacklist().setReg_spaj(getRegSpaj);
				}
				if(getNoPolis != null){
					detiledit.getBlacklist().setNo_policy(getNoPolis);
				}
				detiledit.setDetBlackListAll((List<DetBlackList>) uwManager.selectAllDetBlacklist(detiledit.getBlacklist().getLbl_id().toString()));
			}
			String regSpaj = "";
			String noPolis = "";
			if(detiledit.getClient().getMspe_date_birth() != null){
				detiledit.getClient().setMspe_date_birth2(defaultDateFormat.format(detiledit.getClient().getMspe_date_birth()));
			}
			for(int i = 0 ; i < detiledit.getDetBlackListAll().size() ; i++){
				if(detiledit.getDetBlackListAll().get(i).getLdbl_tgl_kejadian2() == null){detiledit.getDetBlackListAll().get(i).setLdbl_tgl_kejadian2("");}
				if(detiledit.getDetBlackListAll().get(i).getLdbl_tgl_kejadian_to2() == null){detiledit.getDetBlackListAll().get(i).setLdbl_tgl_kejadian_to2("");}
				if(detiledit.getDetBlackListAll().get(i).getLdbl_diagnosa() == null){detiledit.getDetBlackListAll().get(i).setLdbl_diagnosa("");}
			}
			
			if(detiledit.getBlacklist() != null){
				regSpaj = detiledit.getBlacklist().getReg_spaj();
				noPolis = detiledit.getBlacklist().getNo_policy();
				if(regSpaj == null){regSpaj = "";}
				if(noPolis == null){noPolis = "";}
				int lblId = detiledit.getBlacklist().getLbl_id();
				List<BlackListFamily> blf = uwManager.selectAllBlacklistFamily(lblId);
				for(int i = 0 ; i < blf.size() ; i++){
					if(blf.get(i).getLsre_id() == 5){
						detiledit.getBlacklistfamily().setLblf_nama_si(blf.get(i).getLblf_nama());
						detiledit.getBlacklistfamily().setLblf_tgllhr_sis(defaultDateFormat.format(blf.get(i).getLblf_tgl_lahir()));
					}else if(blf.get(i).getLsre_id() == 4){
						if(detiledit.getBlacklistfamily().getLblf_nama_anak1() == null){
							detiledit.getBlacklistfamily().setLblf_nama_anak1(blf.get(i).getLblf_nama());
							detiledit.getBlacklistfamily().setLblf_tgllhr_anak1s(defaultDateFormat.format(blf.get(i).getLblf_tgl_lahir()));
						}else if(detiledit.getBlacklistfamily().getLblf_nama_anak2() == null){
							detiledit.getBlacklistfamily().setLblf_nama_anak2(blf.get(i).getLblf_nama());
							detiledit.getBlacklistfamily().setLblf_tgllhr_anak2s(defaultDateFormat.format(blf.get(i).getLblf_tgl_lahir()));
						}else if(detiledit.getBlacklistfamily().getLblf_nama_anak3() == null){
							detiledit.getBlacklistfamily().setLblf_nama_anak3(blf.get(i).getLblf_nama());
							detiledit.getBlacklistfamily().setLblf_tgllhr_anak3s(defaultDateFormat.format(blf.get(i).getLblf_tgl_lahir()));
						}
					}
				}
			}else if(detiledit.getBlacklist() == null){
				detiledit.setBlacklist(new BlackList());
				regSpaj = request.getParameter("regSpaj");
				noPolis = request.getParameter("noPolis");
				detiledit.getBlacklist().setReg_spaj(regSpaj);
				detiledit.getBlacklist().setNo_policy(noPolis);
				detiledit.getBlacklist().setLbl_sumber_info("");
				detiledit.getBlacklist().setLbl_sumber_info2("");
				detiledit.getBlacklist().setLbl_sts_cust("");
			}
			if(regSpaj == null)regSpaj="";
			if(noPolis == null)noPolis="";
			
			if(!"".equals(regSpaj)){
				String productName = uwManager.selectProductName(regSpaj);
				if(productName == null){productName="";}
				detiledit.getBlacklist().setLbl_asuransi(productName);
				detiledit.getBlacklist().setLbl_sts_cust("2");
				request.setAttribute("regSpaj", regSpaj);
				request.setAttribute("noPolis", noPolis);
				request.setAttribute("statusForm", "true");
			}else{
				request.setAttribute("regSpaj", regSpaj);
				request.setAttribute("noPolis", noPolis);
				request.setAttribute("statusForm", "");
			}
		}
		
        String fontColor = "black";
        Map m = new HashMap();
     
        // KALAU TEKAN TOMBOL SAVE
      //  if("SAVE".equals(request.getParameter("save")) && detiledit.getClient().getMcl_id() == "") {
        if("SAVE".equals(request.getParameter("save")) && detiledit.getClient().getMcl_id().equals("")) {
        
        	try{
	        	mclId = uwManager.prosesInputBlacklist(detiledit, currentUser);
	        	email(currentUser, mclId, detiledit.getClient().getMcl_first(), detiledit.getClient().getMspe_place_birth(), detiledit.getClient().getMspe_date_birth2());
	        	request.setAttribute("s", 1);
	        	request.setAttribute("kopiMclId", mclId);
	        	
        	}catch (Exception e) {
				// TODO: handle exception
        		request.setAttribute("s", 4);
			}
        }else if(!detiledit.getClient().getMcl_id().equals("") && "SAVE".equals(request.getParameter("save"))){	
       // }else if(detiledit.getClient().getMcl_id() != "" && "SAVE".equals(request.getParameter("save"))){
        	//BlackList bl = uwManager.selectAllBlacklist(detiledit.getClient().getMcl_first(), detiledit.getClient().getMspe_date_birth(), mclId);
        	try{
	         	uwManager.prosesUpdateBlacklist(detiledit, currentUser);
	        	email(currentUser, detiledit.getClient().getMcl_id(), detiledit.getClient().getMcl_first(), detiledit.getClient().getMspe_place_birth(), detiledit.getClient().getMspe_date_birth2());
	        	request.setAttribute("s", 2);
	        	request.setAttribute("kopiMclId", mclId);
	        	//if(bl == null){
	        		//email(currentUser, detiledit.getClient().getMcl_first());
	        	//}
	        	//if("2".equals(detiledit.getBlacklist().getLbl_sts_cust()) && detiledit.getClient().getMcl_blacklist() == 1){
	        	
	        	if("2".equals(detiledit.getBlacklist().getLbl_sts_cust()) && detiledit.getBlacklist().getLbl_nama() != null){
					fontColor = "blue";
				}
	        	request.setAttribute("fontColor", fontColor);
        	}catch (Exception e) {
				// TODO: handle exception
        		request.setAttribute("s", 5);
			}
        }else if("BARU".equals(request.getParameter("baru"))){
    		detiledit.setClient(new Client());
    		detiledit.setPemegang(new Pemegang());
    		detiledit.setBlacklist(new BlackList());
    		detiledit.setDetBlackListAll(new ArrayList<DetBlackList>());
    		detiledit.setBlacklistfamily(new BlackListFamily());
    		detiledit.setAddressNew(new AddressNew());
    		detiledit.setCurrentUser(new User());
    		detiledit.getClient().setLus_id(Integer.parseInt(currentUser.getLus_id()));
    		detiledit.setCurrentUser(currentUser);
        } else if("Upload".equals(request.getParameter("upload"))) {
        	
        	try{
	        	Integer lbl_id = detiledit.getBlacklist().getLbl_id();	        		        	
	        	String dir = props.getProperty("pdf.dir.attentionlist");
	        	String tDest = dir + "\\" + lbl_id.toString();	
	        		        	      	
	        		Upload upload = new Upload();
		            ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
		            binder.bind(request);
		            if( !upload.getFile1().isEmpty() )
		            {
		            	String fileName=upload.getFile1().getOriginalFilename();	        
	        		 
	        		 	File destDir = new File(tDest);
	        		 	if (!destDir.exists()) destDir.mkdir();	        		 	        		
		            	String dest = tDest + "\\" + fileName;		            
		            	
		            	File outputFile = new File(dest);
		             	FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
		            		            	       	
		            } 
		            request.setAttribute("s", 3);	
	        	}catch (Exception e) {
	        		// TODO: handle exception	        		
	        		logger.info( "Error pada upload PDF class InputBlacklistCustController.onSubmit" );	  
	                logger.info(e);

					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);

			        String[] itMail = new String[]{"andy@sinarmasmsiglife.co.id","adrian_n@sinarmasmsiglife.co.id"};

	        		email.send(	                  
	        				true,
	        				props.getProperty("admin.ajsjava"), 	
	                		itMail,	                	
	                		null,
	                		null,
	                        "ERROR Proses Upload Pdf AttentionList di E-Lions",
	                        sw.toString(),
	                        null
	                );
	        		
	        		request.setAttribute("s", 6);	        		
				}  
        	
        	
        }else if("DOWNLOAD".equals(request.getParameter("downloadbutton"))) {
        	Integer lbl_id = detiledit.getBlacklist().getLbl_id();       
        	String dir = props.getProperty("pdf.dir.attentionlist");
        	String tDest = dir + "\\" + lbl_id.toString();
        	FileUtils.downloadFile("attachment;", tDest, request.getParameter("doc_att_list"), response);
        	
        	request.setAttribute("kopiMclId", mclId);
        	request.setAttribute("s", 3);        					
        } else{
        	if(detiledit.getClient() != null && detiledit.getBlacklist() != null){
        		//if(detiledit.getClient().getMcl_blacklist() != null){
        			//if("2".equals(detiledit.getBlacklist().getLbl_sts_cust()) && detiledit.getClient().getMcl_blacklist() == 1){
        			if("2".equals(detiledit.getBlacklist().getLbl_sts_cust()) && detiledit.getBlacklist().getLbl_nama() != null){
        				fontColor = "blue";
        			}
        		//}
        	}
        	request.setAttribute("fontColor", fontColor);
        }
        
    	if(detiledit.getBlacklist().getLbl_id() != null){
			Integer lbl_id = detiledit.getBlacklist().getLbl_id();
			String dir = props.getProperty("pdf.dir.attentionlist");
			String tDest = dir + "\\" + lbl_id.toString();
			List<DropDown> daftarAda = FileUtils.listFilesInDirectory(tDest);			
			request.setAttribute("daftarAda", daftarAda);   
		}
    	
        request.setAttribute("lsJnsId", lsJnsId);    
		return new ModelAndView(
        "uw/input_blacklist_customer").addObject("cmd",detiledit);

    }
	
	protected void initBinder(HttpServletRequest arg0, ServletRequestDataBinder binder) throws Exception {
		logger.debug("EditBacController : initBinder");
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		logger.debug("EditBacController : formBackingObject");
        this.accessTime = System.currentTimeMillis();
        HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		List lsJnsId=elionsManager.selectAllLstIdentity();
		
		//String id = request.getParameter("id");
		CmdInputBlacklist detiledit;
		String lde_id = currentUser.getLde_id();
		detiledit = new CmdInputBlacklist();//(CmdInputBlacklist) session.getAttribute("dataInputSpaj");
		detiledit.setClient(new Client());
		detiledit.setPemegang(new Pemegang());
		detiledit.setBlacklist(new BlackList());
		detiledit.setDetBlackListAll(new ArrayList<DetBlackList>());
		detiledit.setBlacklistfamily(new BlackListFamily());
		detiledit.setAddressNew(new AddressNew());
		detiledit.setCurrentUser(new User());	
		String newStatus = "new";
		//detiledit.getClient().setLside_id(1);
		detiledit.getClient().setMspe_no_identity("");
		detiledit.getBlacklist().setLbl_nama_alias_1("");
		detiledit.getBlacklist().setLbl_nama_alias_2("");
		detiledit.getBlacklist().setLbl_nama_alias_3("");
		detiledit.getBlacklist().setLbl_nama_alias_4("");
		detiledit.getBlacklist().setLbl_nama_alias_5("");
		detiledit.getBlacklist().setLbl_no_identity("");
		detiledit.getBlacklist().setLbl_no_identity_akte_lahir("");
		detiledit.getBlacklist().setLbl_no_identity_kims_kitas("");
		detiledit.getBlacklist().setLbl_no_identity_kk("");
		detiledit.getBlacklist().setLbl_no_identity_paspor("");
		detiledit.getBlacklist().setLbl_no_identity_sim("");
		DetBlackList detBlackList = new DetBlackList();
		detBlackList.setLdbl_diagnosa("");
		detBlackList.setLdbl_tgl_kejadian2("");
		detBlackList.setLdbl_tgl_kejadian_to2("");
		detiledit.getDetBlackListAll().add(detBlackList);					
	
		String searchExistFlag = request.getParameter("searchExistFlag");
		if("EXIST".equals(searchExistFlag)){
			request.setAttribute("searchExistFlag", searchExistFlag);
		}
		
		String flagRefresh = request.getParameter("flagRefresh");
		if("refresh".equals(flagRefresh)){		
			String mclId = request.getParameter("kopiMclId");
			String getRegSpaj = request.getParameter("regSpaj");
			String getNoPolis = request.getParameter("noPolis");
			if(mclId == null)mclId = "";
	        if(!"".equals(mclId)) {
	        	newStatus = "exist";
	        	request.setAttribute("kopiMclId", mclId);
				detiledit.setClient(uwManager.selectAllClientBlacklist(mclId));
				detiledit.setAddressNew(elionsManager.selectAllAddressNew(mclId));
				detiledit.setBlacklist(uwManager.selectAllBlacklist(detiledit.getClient().getMcl_first(), detiledit.getClient().getMspe_date_birth(), mclId));
				if(detiledit.getClient() != null){
					if(detiledit.getClient().getMspe_no_identity() == null){detiledit.getClient().setMspe_no_identity("");}
				}
				if(detiledit.getBlacklist() != null){
					if(detiledit.getBlacklist().getLbl_nama_alias_1() == null){detiledit.getBlacklist().setLbl_nama_alias_1("");}
					if(detiledit.getBlacklist().getLbl_nama_alias_2() == null){detiledit.getBlacklist().setLbl_nama_alias_2("");}
					if(detiledit.getBlacklist().getLbl_nama_alias_3() == null){detiledit.getBlacklist().setLbl_nama_alias_3("");}
					if(detiledit.getBlacklist().getLbl_nama_alias_4() == null){detiledit.getBlacklist().setLbl_nama_alias_4("");}
					if(detiledit.getBlacklist().getLbl_nama_alias_5() == null){detiledit.getBlacklist().setLbl_nama_alias_5("");}
					if(detiledit.getBlacklist().getLbl_no_identity_akte_lahir() == null){detiledit.getBlacklist().setLbl_no_identity_akte_lahir("");}
					if(detiledit.getBlacklist().getLbl_no_identity_kims_kitas() == null){detiledit.getBlacklist().setLbl_no_identity_kims_kitas("");}
					if(detiledit.getBlacklist().getLbl_no_identity_kk() == null){detiledit.getBlacklist().setLbl_no_identity_kk("");}
					if(detiledit.getBlacklist().getLbl_no_identity_paspor() == null){detiledit.getBlacklist().setLbl_no_identity_paspor("");}
					if(detiledit.getBlacklist().getLbl_no_identity_sim() == null){detiledit.getBlacklist().setLbl_no_identity_sim("");}
					if(getRegSpaj != null){
						detiledit.getBlacklist().setReg_spaj(getRegSpaj);
					}
					if(getNoPolis != null){
						detiledit.getBlacklist().setNo_policy(getNoPolis);
					}
					detiledit.setDetBlackListAll((List<DetBlackList>) uwManager.selectAllDetBlacklist(detiledit.getBlacklist().getLbl_id().toString()));
				}
				String regSpaj = "";
				String noPolis = "";
				if(detiledit.getClient().getMspe_date_birth() != null){
					detiledit.getClient().setMspe_date_birth2(defaultDateFormat.format(detiledit.getClient().getMspe_date_birth()));
				}
				if(detiledit.getDetBlackListAll() != null){
					for(int i = 0 ; i < detiledit.getDetBlackListAll().size() ; i++){
						if(detiledit.getDetBlackListAll().get(i).getLdbl_tgl_kejadian() != null){detiledit.getDetBlackListAll().get(i).setLdbl_tgl_kejadian2(defaultDateFormat.format(detiledit.getDetBlackListAll().get(i).getLdbl_tgl_kejadian()));}
						if(detiledit.getDetBlackListAll().get(i).getLdbl_tgl_kejadian_to() != null){detiledit.getDetBlackListAll().get(i).setLdbl_tgl_kejadian_to2(defaultDateFormat.format(detiledit.getDetBlackListAll().get(i).getLdbl_tgl_kejadian_to()));}
					}
				}
				
				if(detiledit.getBlacklist() != null){
					regSpaj = detiledit.getBlacklist().getReg_spaj();
					noPolis = detiledit.getBlacklist().getNo_policy();
					if(regSpaj == null){regSpaj = "";}
					if(noPolis == null){noPolis = "";}
					int lblId = detiledit.getBlacklist().getLbl_id();
					List<BlackListFamily> blf = uwManager.selectAllBlacklistFamily(lblId);
					for(int i = 0 ; i < blf.size() ; i++){
						if(blf.get(i).getLsre_id() == 5){
							detiledit.getBlacklistfamily().setLblf_nama_si(blf.get(i).getLblf_nama());
							detiledit.getBlacklistfamily().setLblf_tgllhr_sis(defaultDateFormat.format(blf.get(i).getLblf_tgl_lahir()));
						}else if(blf.get(i).getLsre_id() == 4){
							if(detiledit.getBlacklistfamily().getLblf_nama_anak1() == null){
								detiledit.getBlacklistfamily().setLblf_nama_anak1(blf.get(i).getLblf_nama());
								detiledit.getBlacklistfamily().setLblf_tgllhr_anak1s(defaultDateFormat.format(blf.get(i).getLblf_tgl_lahir()));
							}else if(detiledit.getBlacklistfamily().getLblf_nama_anak2() == null){
								detiledit.getBlacklistfamily().setLblf_nama_anak2(blf.get(i).getLblf_nama());
								detiledit.getBlacklistfamily().setLblf_tgllhr_anak2s(defaultDateFormat.format(blf.get(i).getLblf_tgl_lahir()));
							}else if(detiledit.getBlacklistfamily().getLblf_nama_anak3() == null){
								detiledit.getBlacklistfamily().setLblf_nama_anak3(blf.get(i).getLblf_nama());
								detiledit.getBlacklistfamily().setLblf_tgllhr_anak3s(defaultDateFormat.format(blf.get(i).getLblf_tgl_lahir()));
							}
						}
					}
				}else if(detiledit.getBlacklist() == null){
					detiledit.setBlacklist(new BlackList());
					regSpaj = request.getParameter("regSpaj");
					noPolis = request.getParameter("noPolis");
					detiledit.getBlacklist().setReg_spaj(regSpaj);
					detiledit.getBlacklist().setNo_policy(noPolis);
					detiledit.getBlacklist().setLbl_sumber_info("");
					detiledit.getBlacklist().setLbl_sumber_info2("");
					detiledit.getBlacklist().setLbl_sts_cust("");
				}
				if(regSpaj == null)regSpaj="";
				if(noPolis == null)noPolis="";
				if(!"".equals(regSpaj)){
					String productName = uwManager.selectProductName(regSpaj);
					if(productName == null){productName="";}
					detiledit.getBlacklist().setLbl_asuransi(productName);
					detiledit.getBlacklist().setLbl_sts_cust("2");
					request.setAttribute("regSpaj", regSpaj);
					request.setAttribute("noPolis", noPolis);
					request.setAttribute("statusForm", "true");
				}else{
					request.setAttribute("regSpaj", regSpaj);
					request.setAttribute("noPolis", noPolis);
					request.setAttribute("statusForm", "");
				}
				
			}
			
	        String fontColor = "black";
	        if(detiledit.getClient() != null && detiledit.getBlacklist() != null){
	        	//if(detiledit.getClient().getMcl_blacklist() != null){
	        		//if("2".equals(detiledit.getBlacklist().getLbl_sts_cust()) && detiledit.getClient().getMcl_blacklist() == 1){
	        		if("2".equals(detiledit.getBlacklist().getLbl_sts_cust()) && detiledit.getBlacklist().getLbl_nama() != null){
	        			fontColor = "blue";
	        		}
	        	//}
	        }
	        request.setAttribute("fontColor", fontColor);
	        request.setAttribute("lsJnsId", lsJnsId);
	        
	    	if(detiledit.getBlacklist().getLbl_id() != null){
				Integer lbl_id = detiledit.getBlacklist().getLbl_id();
				String dir = props.getProperty("pdf.dir.attentionlist");
				String tDest = dir + "\\" + lbl_id.toString();
				List<DropDown> daftarAda = FileUtils.listFilesInDirectory(tDest);			
				request.setAttribute("daftarAda", daftarAda);   
			}
						
		}
		
		detiledit.setCurrentUser(currentUser);
		detiledit.getClient().setLus_id(Integer.parseInt(currentUser.getLus_id()));
			
		detiledit.setCurrentUser(currentUser);
		request.setAttribute("lsJnsId", lsJnsId);
		request.setAttribute("newStatus", newStatus);
		
		return detiledit;
	}
	

	protected void email(User currentUser, String mclId, String nama, String tempat, String tglLahir){
//		"sunarti@sinarmasmsiglife.co.id","sisti@sinarmasmsiglife.co.id",
//		"yulikusuma@sinarmasmsiglife.co.id",
//		"edi@sinarmasmsiglife.co.id","lendra@sinarmasmsiglife.co.id",
		//"ingrid@sinarmasmsiglife.co.id","asriwulan@sinarmasmsiglife.co.id"//,
//		"hayatin@sinarmasmsiglife.co.id","ariani@sinarmasmsiglife.co.id","novie@sinarmasmsiglife.co.id}"},
		//"andy@sinarmasmsiglife.co.id"
//		- UW         : Ingrid, Irene, Inge
//		- Claim      : Sunarti, dr. Sisti, dr.Rachel
//		- LB/CS     : SR. Annalisa, Ety
//		- Auditor    : Edi, Lendra
//		- Legal       :Yuli K, Primita
		
		String dept = "";
		// case Sriningrum Dawiyati deptnya diganti jadi Underwriting
		if("Sriningrum Dawiyati".equals(currentUser.getLus_full_name().trim())){
			dept = "Underwriting";
		}else{
			dept = currentUser.getDept();
		}
		if(tglLahir == null)tglLahir="data tidak ada";
		if("".equals(tglLahir))tglLahir="data tidak ada";
		try {
			email.send(true,
					props.getProperty("admin.ajsjava"), 
					new String[] {"newbusinessdept@sinarmasmsiglife.co.id", "Underwriting@sinarmasmsiglife.co.id",
				    "legal@sinarmasmsiglife.co.id", "Compliance@sinarmasmsiglife.co.id", "claim@sinarmasmsiglife.co.id", "bas@sinarmasmsiglife.co.id"},							
					new String[] {"Yurika@sinarmasmsiglife.co.id", "pandu@sinarmasmsiglife.co.id"},
					new String[] {"andy@sinarmasmsiglife.co.id"}, 
					//null,
					"Info Attention List customer a/n " + nama, 
					"Telah dilakukan up date Attention List customer a/n."+nama+". Tempat & tanggal lahir : "+tempat+", "+tglLahir+". Bagi yang memerlukan silahkan lihat di <a href=\"http://ajsjavai64/E-Lions/blacklistLogin.htm?kmid="+mclId+"\">Attention List customer</a>." +
					"<br><br>User :<br>" +
					currentUser.getLus_full_name() +
					"<br>Dept." + dept
					, null);
		}catch(Exception e){
			logger.error("ERROR :", e);
		}
		//String x = "";
	}

}
