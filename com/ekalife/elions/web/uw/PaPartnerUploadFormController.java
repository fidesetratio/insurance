package com.ekalife.elions.web.uw;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import produk_asuransi.n_prod;
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.TmSales;
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.ContactPerson;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.PembayarPremi;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Personal;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.ReffBii;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.elions.process.UploadSpajTemp;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.utils.Common;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.ExcelRead;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.ITextPdf;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.f_replace;
import com.ekalife.utils.f_validasi;
import com.ekalife.utils.parent.ParentController;
import com.lowagie.text.DocumentException;

import produk_asuransi.produk_simpel.n_prod_u_01_187;
import produk_asuransi.produk_simpel.n_prod_u_04_203;
import produk_asuransi.produk_simpel.n_prod_u_11_056;
import produk_asuransi.produk_simpel.n_prod_u_11_060;
import produk_asuransi.produk_simpel.n_prod_u_11_061;
import produk_asuransi.produk_simpel.n_prod_u_11_205;
import produk_asuransi.produk_simpel.n_prod_u_15_073;

public class PaPartnerUploadFormController extends ParentController{
	protected final static Log logger = LogFactory.getLog( PaPartnerUploadFormController.class);
	private Calendar tgl_sekarang = Calendar.getInstance(); 
	private Integer fmYearSpaj=new Integer(tgl_sekarang.get(Calendar.YEAR));
	private Integer tanggal=new Integer(0);
	private Integer bulan=new Integer(0);
	private Integer tahun=new Integer(0);
	private Integer tanggalEd=new Integer(0);
	private Integer bulanEd=new Integer(0);
	private Integer tahunEd=new Integer(0);
	private Integer tanggal1=new Integer(0);
	private Integer bulan1=new Integer(0);
	private Integer tahun1=new Integer(0);
	private SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
	private SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
	private SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
//	private SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMyyyy");
	private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	
	List<Pas> agenList = new ArrayList<Pas>();
	List<TmSales> tmSalesList = new ArrayList<TmSales>();
	String jenis_pas;
	String tanggalAwal;
	n_prod_u_01_187 n_prod_u_01_187;
	n_prod_u_11_205 n_prod_u_11_205;
	n_prod_u_11_056 n_prod_u_11_056;
	n_prod_u_11_060 n_prod_u_11_060;
	n_prod_u_11_061 n_prod_u_11_061;
	n_prod_u_15_073 n_prod_u_15_073;
	n_prod_u_04_203 n_prod_u_04_203;
		
	@SuppressWarnings("deprecation")
	public ModelAndView handleRequest( HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		Map<String, Object> cmd = new HashMap<String, Object>();
		String err = "";
		String valid = "true";

		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		cmd.put("user_id", lus_id);
		
		List<Map> successMessageList = new ArrayList<Map>();
				
		String submit = request.getParameter("upload");
		String transfer = request.getParameter("transfer");	
		String submit_pasfree = request.getParameter("upload_pasfree");
		String transfer2 = request.getParameter("transfer2");
		String transfer3 = request.getParameter("transfer3");		
		String search = request.getParameter("search");
		String refresh = request.getParameter("refresh");	
		String submit_smartsave = request.getParameter("upload_smartsave");
		
		//UNTUK DATA YANG MASUK KE INDIVIDU - AJSDB
		if(submit != null){
			 tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
			agenList.clear();
			
			Upload upload = new Upload();
			String file_fp = request.getParameter("file_fp");
			upload.setDaftarFile(new ArrayList<MultipartFile>(10));
			ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
			binder.bind(request);
			MultipartFile mf = upload.getFile1();
			String fileName = mf.getOriginalFilename();
			String directory = props.getProperty("temp.dir.fileupload");
		//	String directory = file_fp.replaceAll(fileName, "");
			
			
			ExcelRead excelRead = new ExcelRead();		
			Pas agencyExcel = null;
			
			List<List> agencyExcelList = new ArrayList<List>();
					
			try{
				String dest=directory + "/" + fileName ;
				File outputFile = new File(dest);
			    FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
				agencyExcelList = excelRead.readExcelFileNew(directory + "\\", fileName);
				
			}catch (Exception e) {
				e.printStackTrace();
				Map<String,String> map = new HashMap<String, String>();
				map.put("sts", "FAILED");
				map.put("msg", "FAILED:: akses file gagal");
				successMessageList.add(map);
			}
		
//			int jmlh_header = agencyExcelList.get(0).size();
//			// Untuk jenis_pas = "BP SMS" jmlh_kolom Excel = 27
//			// Untuk jenis_pas = "AP/BP" jmlh_kolom Excel = 23
//			if (jmlh_header>=27) {
//				jenis_pas = "BP SMS";
//			}else{
//				jenis_pas = "AP/BP";
//			}
			
			if(agencyExcelList.size()>50){
				Map<String,String> map = new HashMap<String, String>();
				map.put("sts", "FAILED");
				map.put("msg", "FAILED:: Upload tidak dapat dilakukan, Maksimal jumlah data upload 50!");
				successMessageList.add(map);
				request.setAttribute("successMessage",successMessageList);	
				
			}else{	
			
			int jenis_upload_produk = Integer.parseInt(request.getParameter("jenis_upload_produk"));
			request.setAttribute("jenis_upload_produk", jenis_upload_produk);
			if(jenis_upload_produk == 1){
				jenis_pas = "AP/BP";
			}else if(jenis_upload_produk == 2){
				jenis_pas = "BP SMS";
			}else if(jenis_upload_produk == 3){
				jenis_pas = "BP CARD";
			}else if(jenis_upload_produk == 4){
				jenis_pas = "PAS SYARIAH";
			}else if(jenis_upload_produk == 6){
				jenis_pas = "SMART ACCIDENT CARE";
			}else if(jenis_upload_produk == 10){
				jenis_pas = "NISSAN PA";
			}else if(jenis_upload_produk == 11){
				jenis_pas = "NISSAN DBD";
			}else{
				jenis_pas = "";
			}
			
			if(!StringUtil.isEmpty(jenis_pas)){
				Map<String,String> map = new HashMap<String, String>();
				map.put("sts", "SUCCEED");
				int size = agencyExcelList.size()-1;
				map.put("msg", "*Jumlah data "+ jenis_pas +" diproses: "+size);
				successMessageList.add(map);
				
			for(int i = 1 ; i < agencyExcelList.size() ; i++){
				if(!agencyExcelList.get(i).get(0).toString().isEmpty()){
					//Map<String,String> map = new HashMap<String, String>();
					map = new HashMap<String, String>();
					try{
						agencyExcel = new Pas();
						SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						Calendar cal = Calendar.getInstance();
					
					
						if(jenis_pas.equals("BP SMS") ){
							//Produk Upload PAS Business Partner
							//@author : Adrian @since : Jan 21, 2015
							n_prod_u_01_187 = new n_prod_u_01_187();
							List rowAgencyExcelList = new ArrayList();
							rowAgencyExcelList = agencyExcelList.get(i);
							agencyExcel = n_prod_u_01_187.set_prod_u_01_187_SMS(rowAgencyExcelList);
																					
							List carabayarList = DroplistManager.getInstance().get("CARABAYAR_PAS.xml","ID",request);						
							for(int k = 0 ; k < carabayarList.size() ; k++){
								Map map2 = (Map) carabayarList.get(k);
								String id =  (String) map2.get("ID");
								String carabayar = (String) map2.get("PAYMODE");
								if(carabayar.equalsIgnoreCase(agencyExcelList.get(i).get(19).toString().trim())){
									agencyExcel.setLscb_id(Integer.parseInt(id));						
								}
							}
							
						}else if(jenis_pas.equals("PAS SYARIAH") ){
							agencyExcel.setMsp_pas_create_date(elionsManager.selectSysdate());
							
							//Produk Upload PAS Business Partner
							//@author : Adrian @since : Jan 21, 2015
							n_prod_u_11_205 = new n_prod_u_11_205();
							List rowAgencyExcelList = new ArrayList();
							rowAgencyExcelList = agencyExcelList.get(i);
							agencyExcel = n_prod_u_11_205.set_prod_u_11_205(rowAgencyExcelList);
						 	
						}else if(jenis_pas.equals("SMART ACCIDENT CARE") ){
							agencyExcel.setMsp_pas_create_date(elionsManager.selectSysdate());
							
							//Produk Upload PAS Business Partner
							//@author : Adrian @since : Jan 21, 2015
							n_prod_u_01_187 = new n_prod_u_01_187();
							List rowAgencyExcelList = new ArrayList();
							rowAgencyExcelList = agencyExcelList.get(i);
							agencyExcel = n_prod_u_01_187.set_prod_u_01_187_SAC(rowAgencyExcelList);	
							
						}else if(jenis_pas.equals("NISSAN PA")){
							agencyExcel.setMsp_pas_create_date(elionsManager.selectSysdate());
							
							n_prod_u_15_073 = new n_prod_u_15_073();
							List rowAgencyExcelList = new ArrayList();
							rowAgencyExcelList = agencyExcelList.get(i);
							agencyExcel = n_prod_u_15_073.set_prod_u_15_073(rowAgencyExcelList);	
							
						}else if(jenis_pas.equals("NISSAN DBD")){
							agencyExcel.setMsp_pas_create_date(elionsManager.selectSysdate());
							
							n_prod_u_04_203 = new n_prod_u_04_203();
							List rowAgencyExcelList = new ArrayList();
							rowAgencyExcelList = agencyExcelList.get(i);
							agencyExcel = n_prod_u_04_203.set_prod_u_04_203(rowAgencyExcelList);	
							
						}else{	 //PAS AP-BP & BP-CARD	
							//Produk Upload PAS Business Partner
							//@author : Adrian @since : Jan 21, 2015
							n_prod_u_01_187 = new n_prod_u_01_187();
							List rowAgencyExcelList = new ArrayList();
							rowAgencyExcelList = agencyExcelList.get(i);
							agencyExcel = n_prod_u_01_187.set_prod_u_01_187(rowAgencyExcelList);
							
							agencyExcel.setMsp_pas_create_date(elionsManager.selectSysdate());							
							try{							
								Date beg_date = df.parse(tanggalAwal);					
								agencyExcel.setMsp_pas_beg_date(beg_date);
							}
							catch (Exception e) {
								logger.error("ERROR :", e);						
							}
							Date end_date = (Date) agencyExcel.getMsp_pas_beg_date().clone();
							end_date.setYear(end_date.getYear()+1);
							end_date.setDate(end_date.getDate()-1);
							agencyExcel.setMsp_pas_end_date(end_date);
					    	
							Date debetDate = (Date) agencyExcel.getMsp_pas_beg_date().clone();
							debetDate.setMonth(debetDate.getMonth() + 6);
							debetDate.setDate(debetDate.getDate() - 1);
							agencyExcel.setMsp_tgl_debet(debetDate);
							
							agencyExcel.setMsp_admin_date(agencyExcel.getMsp_pas_beg_date());
														
							//No.kartu
							if(jenis_pas.equals("BP CARD")){								
								rowAgencyExcelList = agencyExcelList.get(i);
								agencyExcel = n_prod_u_01_187.set_prod_u_01_187_Card(rowAgencyExcelList);
							}							
						}
						
						agencyExcel.setJenis_pas(jenis_pas);
						//otomatis
						agencyExcel.setLus_id(Integer.parseInt(user.getLus_id()));
						agencyExcel.setLus_id_uw_pas(Integer.parseInt(user.getLus_id()));
						
						//common						
						List<Map> lsWargaNegara = new ArrayList<Map>();						
					    lsWargaNegara=elionsManager.selectAllLstNegara();
						for(int j = 0 ; j < lsWargaNegara.size(); j++){ 
							if(lsWargaNegara.get(j).get("LSNE_NOTE").toString().equalsIgnoreCase(agencyExcelList.get(i).get(5).toString())){							
								agencyExcel.setMsp_warga(Integer.parseInt(lsWargaNegara.get(j).get("LSNE_ID").toString()));		
								break;
								}	
							} 
						
						List maritalList = DroplistManager.getInstance().get("MARITAL.xml","ID",request);						
						for(int k = 0 ; k < maritalList.size() ; k++){
							Map map2 = (Map) maritalList.get(k);
							String id =  (String) map2.get("ID");
							String marital = (String) map2.get("MARITAL");
							if(marital.equalsIgnoreCase(agencyExcelList.get(i).get(6).toString().trim())){
								agencyExcel.setMsp_status_nikah(Integer.parseInt(id));								
							}else{								
							}
						}
						
						List agamaList = DroplistManager.getInstance().get("AGAMA.xml","ID",request);						
						for(int k = 0 ; k < agamaList.size() ; k++){
							Map map2 = (Map) agamaList.get(k);
							String id =  (String) map2.get("ID");
							String agama = (String) map2.get("AGAMA");
							if(agama.equalsIgnoreCase(agencyExcelList.get(i).get(7).toString().trim())){
								agencyExcel.setLsag_id(Integer.parseInt(id));							
							}else{								
							}
						}
						
						if(!jenis_pas.equals("PAS SYARIAH")){
							if(!(jenis_pas.equals("SMART ACCIDENT CARE") || jenis_pas.equals("NISSAN PA") || jenis_pas.equals("NISSAN DBD")) ){
								List autodebetList = DroplistManager.getInstance().get("AUTODEBET_PAS.xml","ID",request);						
								for(int k = 0 ; k < autodebetList.size() ; k++){
									Map map2 = (Map) autodebetList.get(k);
									String id =  (String) map2.get("ID");
									String autodebet = (String) map2.get("AUTODEBET");
									if(autodebet.equalsIgnoreCase(agencyExcelList.get(i).get(18).toString().trim())){
										agencyExcel.setMsp_flag_cc(Integer.parseInt(id));						
									}else{								
									}
								}						

								List<Map> lsBankPusat = new ArrayList<Map>();
								lsBankPusat = uwManager.selectBankPusat();
								for(int j = 0 ; j < lsBankPusat.size(); j++){ 
									if(lsBankPusat.get(j).get("LSBP_NAMA").toString().equalsIgnoreCase(agencyExcelList.get(i).get(20).toString().trim())){							
										agencyExcel.setLsbp_id(lsBankPusat.get(j).get("LSBP_ID").toString());								
										break;
									}
								}
							}
						}						
																									
					
						//*** VALIDASI FIELD EXCEL ***		
					    err = "";
					   
					    if(jenis_pas.equals("BP SMS") ){
					    	err = err + n_prod_u_01_187.validate_prod_u_01_187_SMS();
					    			
					    	if(Common.isEmpty(agencyExcel.getNo_kartu()) || agencyExcel.getNo_kartu() == null || agencyExcel.getNo_kartu() == ""){
					    		err = err+ " Update PAS BP SMS: No.Kartu tidak boleh kosong!,";
					    	}else{
					    		String card = uwManager.selectCekNoKartu(agencyExcel.getNo_kartu(), "CARD", 0);
					    		if(StringUtil.isEmpty(card)){
					    			err = err+ "Update PAS BP SMS: No.Kartu telah dipakai sebelumnya!,";
					    		}
					    	}
					    	
					    	if(agencyExcel.getMsag_id_pp().length()!=6 || agencyExcel.getMsag_id_pp()== null || agencyExcel.getMsag_id_pp()== "") {
								err = err+ " Kode Agen PP harus diisi 6 Digit,";
							}else
							{
								Agen agen = new Agen();					
								if(agencyExcel.getMsag_id_pp() == null)agencyExcel.setMsag_id_pp("");
								if(!"".equals(agencyExcel.getMsag_id_pp())){
									agen = uwManager.select_detilagen_bp(agencyExcel.getMsag_id_pp());															 
								    if(agen.getMsag_id() == null) err = err+ " Agen : Kode Agen PP tidak berlaku,";
								}	
							}					    	
					    }else if(jenis_pas.equals("BP CARD") ){
					    	if(Common.isEmpty(agencyExcel.getNo_kartu()) || agencyExcel.getNo_kartu() == null || agencyExcel.getNo_kartu() == ""){
					    		err = err+ jenis_pas+": No.Kartu tidak boleh kosong!,";
					    	}else{
					    		String card = uwManager.selectCekNoKartu(agencyExcel.getNo_kartu(), "CARD", 0);
					    		if(StringUtil.isEmpty(card)){
					    			err = err+ jenis_pas+": No.Kartu telah dipakai sebelumnya!,";
					    		}
					    	}					    	
					    }else if(jenis_pas.equals("PAS SYARIAH") || jenis_pas.equals("SMART ACCIDENT CARE")
					    		 || jenis_pas.equals("NISSAN PA") || jenis_pas.equals("NISSAN DBD")){							
							if(agencyExcel.getMsag_id().length()!=6 || agencyExcel.getMsag_id()== null || agencyExcel.getMsag_id()== "") {
								err = err+ " Kode Agen harus diisi 6 Digit,";
							}else
							{
								Agen agen = new Agen();					
								if(agencyExcel.getMsag_id() == null)agencyExcel.setMsag_id("");
								if(!"".equals(agencyExcel.getMsag_id())){
									agen = uwManager.select_detilagen2(agencyExcel.getMsag_id());															 
								    if(agen.getMsag_id() == null) err = err+ " Agen : Kode Agen tidak berlaku,";
								    
								    String lsrg_nama="";	   
								    lsrg_nama = uwManager.select_region(agen.getLca_id(),agen.getLwk_id(),agen.getLsrg_id());
								    
								    if(lsrg_nama==null || lsrg_nama=="") {
										err = err+ " Region Kode Agen DMTM bukan DMTM-TSR / DMTM-BSIM / DMTM-ARCO / DMTM-SMP-BTN / DMTM-BANKDKI / DMTM-VASCO / DMTM-DENA / DMTM-MARZ / DMTM - SYNERGYS / DMTM - SSI / DMTM - PT AUSINDO PRATAMA KARYA / DMTM - PT ABHIPRAYA /DMTM - PT KAYZAN JAYA PERSADA  /  DMTM,";
								    }
								    else if(!lsrg_nama.trim().equalsIgnoreCase("DMTM - TSR") && !lsrg_nama.trim().equalsIgnoreCase("DMTM - BSIM") &&
								    		!lsrg_nama.trim().equalsIgnoreCase("DMTM - ARCO") && !lsrg_nama.trim().equalsIgnoreCase("DMTM - SMP") &&
								    		!lsrg_nama.trim().equalsIgnoreCase("DMTM - BTN") && !lsrg_nama.trim().equalsIgnoreCase("DMTM - BTN SEMARANG") && !lsrg_nama.trim().equalsIgnoreCase("DMTM - BANK DKI") &&
								    		!lsrg_nama.trim().equalsIgnoreCase("DMTM - DENA AKHYARO NUSANTARA") && !lsrg_nama.trim().equalsIgnoreCase("DMTM - VASCO") &&
								    		!lsrg_nama.trim().equalsIgnoreCase("DMTM - MARZ") && !lsrg_nama.trim().equalsIgnoreCase("DMTM - SYNERGYS") && 
								    		!lsrg_nama.trim().equalsIgnoreCase("DMTM - SEVEN STARS INDONESIA")  && !lsrg_nama.trim().equalsIgnoreCase("DMTM - PT AUSINDO PRATAMA KARYA") && 
								    		!lsrg_nama.trim().equalsIgnoreCase("DMTM - PT DATA KOMUNIKA TAM") && 
								    		!lsrg_nama.trim().equalsIgnoreCase("DMTM - PT ABHIPRAYA") &&
								    		!lsrg_nama.trim().equalsIgnoreCase("DMTM - PT KAYZAN JAYA PERSADA")
								    		){
								    	err = err+ " Region Kode Agen DMTM bukan DMTM-TSR / DMTM-BSIM / DMTM-ARCO / DMTM-SMP-BTN / DMTM-BANKDKI / DMTM-VASCO / DMTM-DENA / DMTM-MARZ / DMTM - SYNERGYS / DMTM - SSI  / DMTM - PT AUSINDO PRATAMA KARYA / DMTM - PT ABHIPRAYA / DMTM,";
								    }
								    
								}
							}
							
							if(jenis_pas.equals("PAS SYARIAH")){
								err = err + n_prod_u_11_205.validate_prod_u_11_205();
								 if(!agencyExcelList.get(i).get(30).toString().trim().isEmpty()){
							    	 String mspPasBegDate = agencyExcelList.get(i).get(30).toString().trim();						    
							    	 String[] arr_mspPasBegDate = mspPasBegDate.split("/");						    								
								     if(arr_mspPasBegDate[0].length()!=2 || arr_mspPasBegDate[1].length()!=2 || arr_mspPasBegDate[2].length()!=4){
								    	err = err+ " PAS SYARIAH: TGL_BEG_DATE harus dalam Format dd/MM/YYYY !,";
								     }
							    }
							    if(!agencyExcelList.get(i).get(34).toString().trim().isEmpty()){
							    	 String ccValidDate = agencyExcelList.get(i).get(34).toString().trim();						    
							    	 String[] arr_ccValidDate = ccValidDate.split("/");						    								
								     if(arr_ccValidDate[0].length()!=2 || arr_ccValidDate[1].length()!=2 || arr_ccValidDate[2].length()!=4){
								    	err = err+ " PAS SYARIAH: TGL EXPIRED CREDIT-CARD harus dalam Format dd/MM/YYYY !,";
								     }
							    }
							}else{
								String namaProduk = "";
								if(jenis_pas.equals("NISSAN PA")) {
									namaProduk = "NISSAN PA";
									err = err + n_prod_u_15_073.validate_prod_u_15_073();
								}else if(jenis_pas.equals("NISSAN DBD")) {
									namaProduk = "NISSAN DBD";
									err = err + n_prod_u_04_203.validate_prod_u_04_203();
								}else {
									namaProduk = "SMART ACCIDENT CARE";
									err = err + n_prod_u_01_187.validate_prod_u_01_187_SAC();
								}
								
								 if(!agencyExcelList.get(i).get(30).toString().trim().isEmpty()){
							    	 String mspPasBegDate = agencyExcelList.get(i).get(30).toString().trim();						    
							    	 String[] arr_mspPasBegDate = mspPasBegDate.split("/");						    								
								     if(arr_mspPasBegDate[0].length()!=2 || arr_mspPasBegDate[1].length()!=2 || arr_mspPasBegDate[2].length()!=4){
								    	err = err+ " "+namaProduk+": TGL_BEG_DATE harus dalam Format dd/MM/YYYY !,";
								     }
							    }
							    if(!agencyExcelList.get(i).get(34).toString().trim().isEmpty()){
							    	 String ccValidDate = agencyExcelList.get(i).get(34).toString().trim();						    
							    	 String[] arr_ccValidDate = ccValidDate.split("/");						    								
								     if(arr_ccValidDate[0].length()!=2 || arr_ccValidDate[1].length()!=2 || arr_ccValidDate[2].length()!=4){
								    	err = err+ " "+namaProduk+": TGL EXPIRED CREDIT-CARD harus dalam Format dd/MM/YYYY !,";
								     }
							    }
							}
							
//							 if(!agencyExcelList.get(i).get(30).toString().trim().isEmpty()){
//						    	 String mspPasBegDate = agencyExcelList.get(i).get(30).toString().trim();						    
//						    	 String[] arr_mspPasBegDate = mspPasBegDate.split("/");						    								
//							     if(arr_mspPasBegDate[0].length()!=2 || arr_mspPasBegDate[1].length()!=2 || arr_mspPasBegDate[2].length()!=4){
//							    	err = err+ " PAS SYARIAH: TGL_BEG_DATE harus dalam Format dd/MM/YYYY !,";
//							     }
//						    }
//						    if(!agencyExcelList.get(i).get(34).toString().trim().isEmpty()){
//						    	 String ccValidDate = agencyExcelList.get(i).get(34).toString().trim();						    
//						    	 String[] arr_ccValidDate = ccValidDate.split("/");						    								
//							     if(arr_ccValidDate[0].length()!=2 || arr_ccValidDate[1].length()!=2 || arr_ccValidDate[2].length()!=4){
//							    	err = err+ " PAS SYARIAH: TGL EXPIRED CREDIT-CARD harus dalam Format dd/MM/YYYY !,";
//							     }
//						    }
						    
						}else{  // AP/BP							
							if(agencyExcel.getMsag_id().length()!=6 || agencyExcel.getMsag_id()== null || agencyExcel.getMsag_id()== "") {
								err = err+ " Kode Agen BP harus diisi 6 Digit,";
							}else
							{
								Agen agen = new Agen();					
								if(agencyExcel.getMsag_id() == null)agencyExcel.setMsag_id("");
								if(!"".equals(agencyExcel.getMsag_id())){
									agen = uwManager.select_detilagen_bp(agencyExcel.getMsag_id());															 
								    if(agen.getMsag_id() == null) err = err+ " Agen : Kode Agen BP tidak berlaku,";
								}	
							}
						}
					    
					 //validate2 ~ common 
					 if(jenis_pas.equals("PAS SYARIAH")){
						 err = err + n_prod_u_11_205.validate_common();
					 }else if(jenis_pas.equals("SMART ACCIDENT CARE")){
						 err = err + n_prod_u_01_187.validate_common();
					 }else if(jenis_pas.equals("NISSAN PA")){
						 err = err + n_prod_u_15_073.validate_common();
					 }else if(jenis_pas.equals("NISSAN DBD")){
						 err = err + n_prod_u_04_203.validate_common();
					 } else {
						 err = err + n_prod_u_01_187.validate_common(); 
					 }
					 
					    		
					   if(!agencyExcelList.get(i).get(4).toString().trim().isEmpty()){
					    	 String mspPasDobPP = agencyExcelList.get(i).get(4).toString().trim();						    
					    	 String[] arr_mspPasDobPP = mspPasDobPP.split("/");						    								
						     if(arr_mspPasDobPP[0].length()!=2 || arr_mspPasDobPP[1].length()!=2 || arr_mspPasDobPP[2].length()!=4){
						    	err = err+ " PAS-Partner: TGL_LAHIR_PP harus dalam Format dd/MM/YYYY !,";
						     }
					    }
					    if(!agencyExcelList.get(i).get(16).toString().trim().isEmpty()){
					    	 String mspDateOfBirth = agencyExcelList.get(i).get(16).toString().trim();						    
					    	 String[] arr_mspDateOfBirth = mspDateOfBirth.split("/");						    								
						     if(arr_mspDateOfBirth[0].length()!=2 || arr_mspDateOfBirth[1].length()!=2 || arr_mspDateOfBirth[2].length()!=4){
						    	err = err+ " PAS-Partner: TGL_LAHIR_TTG harus dalam Format dd/MM/YYYY !,";
						     }
					    }					   
						
						Integer umurPp = 0;
						Integer umurTt = 0;	
						Integer umurBlnTt = 0;
						try{
							f_hit_umur umr = new f_hit_umur();
							
							SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
							SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
							SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
							Date sysdate = elionsManager.selectSysdate();
							int tahun2 = Integer.parseInt(sdfYear.format(sysdate));
							int bulan2 = Integer.parseInt(sdfMonth.format(sysdate));
							int tanggal2 = Integer.parseInt(sdfDay.format(sysdate));
							
							if(agencyExcel.getMsp_pas_dob_pp() != null){								
								int tahun1 = Integer.parseInt(sdfYear.format(agencyExcel.getMsp_pas_dob_pp()));
								int bulan1 = Integer.parseInt(sdfMonth.format(agencyExcel.getMsp_pas_dob_pp()));
								int tanggal1 = Integer.parseInt(sdfDay.format(agencyExcel.getMsp_pas_dob_pp()));
								
								umurPp=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
							}
							if(agencyExcel.getMsp_date_of_birth() != null){
								int tahun1 = Integer.parseInt(sdfYear.format(agencyExcel.getMsp_date_of_birth()));
								int bulan1 = Integer.parseInt(sdfMonth.format(agencyExcel.getMsp_date_of_birth()));
								int tanggal1 = Integer.parseInt(sdfDay.format(agencyExcel.getMsp_date_of_birth()));
							
								umurTt=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
								agencyExcel.setMsp_age(umurTt);
								
								if(umurTt < 1){
									f_hit_umur a = new f_hit_umur();
									umurBlnTt = a.bulan(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
								}
							}
						}catch(Exception e){							
						}
												
						if(!(umurPp>=17 && umurPp<=85)){ //batas umur 17 s/d 85 tahun					
							err = err+ " PAS : Umur pemegang polis minimal 17 tahun & maksimal 85 tahun,";
						}						
						if(jenis_pas.equals("PAS SYARIAH") || jenis_pas.equals("SMART ACCIDENT CARE"))
						{							
							if((umurBlnTt<6 && umurTt<1) || (umurTt>80)){ //pas-syariah batas umur 6 bulan s/d 80 tahun		
								err = err+ " PAS : Umur tertanggung minimal 6 bulan & maksimal 80 tahun,";
							}
						}else if(jenis_pas.equals("NISSAN PA"))
						{							
							if(!(umurTt>=17 && umurTt<=60)){ //batas umur 17 s/d 60 tahun							
								err = err+ " PAS : Umur tertanggung minimal 17 tahun & maksimal 60 tahun,";
							}	
						}else if(jenis_pas.equals("NISSAN DBD"))
						{							
							if(!(umurTt>=17 && umurTt<=65)){ //batas umur 17 s/d 65 tahun							
								err = err+ " PAS : Umur tertanggung minimal 17 tahun & maksimal 65 tahun,";
							}	
						}else{
							if(!(umurTt>=17 && umurTt<=80)){ //batas umur 17 s/d 80 tahun							
								err = err+ " PAS : Umur tertanggung minimal 17 tahun & maksimal 80 tahun,";
							}							
						}			
						
						int aktif = 1;
						Map agentMap = elionsManager.selectagenpenutup(agencyExcel.getMsag_id());
						if(agentMap == null){
							aktif = 0;
						}
						if(aktif == 0){// agen tidak aktif		
							err = err+ " Agen tidak aktif,";
						}
						
						if(!"".equals(err)){
							 valid = "false";
							// Error Validasi Data
							 map.put("sts", "FAILED");
							 map.put("msg", "FAILED (Validation):: No. "+agencyExcelList.get(i).get(0).toString()+" PAS-PP: "+agencyExcelList.get(i).get(1).toString()+"(PAS-TT: "+agencyExcelList.get(i).get(14).toString()+") : "+err);					
							successMessageList.add(map);
						}else{
							map = new HashMap<String, String>();
							
							map.put("sts", "SUCCEED");
							 map.put("msg", "SUCCEED (Validation):: No. "+agencyExcelList.get(i).get(0).toString()+" PAS-PP: "+agencyExcelList.get(i).get(1).toString()+"(PAS-TT: "+agencyExcelList.get(i).get(14).toString()+") ");					
							successMessageList.add(map);
							
						}
						
				}catch (Exception e) {
					logger.error("ERROR :", e);
						map.put("sts", "FAILED");
						map.put("msg", "FAILED (Try):: PAS-PP: "+agencyExcelList.get(i).get(1).toString()+"(PAS-TT: "+agencyExcelList.get(i).get(14).toString()+") : Format nama kolom / data upload ada yg tidak sesuai!!! (Tanggal harus diisi dgn Format: DD/mm/yyyy)");
						successMessageList.add(map);
				}
				}
			}
			
			//IF (valid=true) INSERT IS ALLOWED
			//TIDAK ADA VALIDASI EXCEL
			if("true".equals(valid)){
				for(int i = 1 ; i < agencyExcelList.size() ; i++){
					if(!agencyExcelList.get(i).get(0).toString().isEmpty()){
						map = new HashMap<String, String>();
					
						agencyExcel = new Pas();
						SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						Calendar cal = Calendar.getInstance();
										
						if(jenis_pas.equals("BP SMS") ){
							//Produk Upload PAS Business Partner
							//@author : Adrian @since : Jan 21, 2015
							n_prod_u_01_187 = new n_prod_u_01_187();
							List rowAgencyExcelList = new ArrayList();
							rowAgencyExcelList = agencyExcelList.get(i);
							agencyExcel = n_prod_u_01_187.set_prod_u_01_187_SMS(rowAgencyExcelList);
																					
							List carabayarList = DroplistManager.getInstance().get("CARABAYAR_PAS.xml","ID",request);						
							for(int k = 0 ; k < carabayarList.size() ; k++){
								Map map2 = (Map) carabayarList.get(k);
								String id =  (String) map2.get("ID");
								String carabayar = (String) map2.get("PAYMODE");
								if(carabayar.equalsIgnoreCase(agencyExcelList.get(i).get(19).toString().trim())){
									agencyExcel.setLscb_id(Integer.parseInt(id));						
								}
							}
							
						}else if(jenis_pas.equals("PAS SYARIAH") ){
							agencyExcel.setMsp_pas_create_date(elionsManager.selectSysdate());
							
							//Produk Upload PAS Business Partner
							//@author : Adrian @since : Jan 21, 2015
							n_prod_u_11_205 = new n_prod_u_11_205();
							List rowAgencyExcelList = new ArrayList();
							rowAgencyExcelList = agencyExcelList.get(i);
							agencyExcel = n_prod_u_11_205.set_prod_u_11_205(rowAgencyExcelList);
						 	
						}else if(jenis_pas.equals("SMART ACCIDENT CARE") ){
							agencyExcel.setMsp_pas_create_date(elionsManager.selectSysdate());
							
							n_prod_u_01_187 = new n_prod_u_01_187();
							List rowAgencyExcelList = new ArrayList();
							rowAgencyExcelList = agencyExcelList.get(i);
							agencyExcel = n_prod_u_01_187.set_prod_u_01_187_SAC(rowAgencyExcelList);	
							
						}else if(jenis_pas.equals("NISSAN PA") ){
							agencyExcel.setMsp_pas_create_date(elionsManager.selectSysdate());
							
							n_prod_u_15_073 = new n_prod_u_15_073();
							List rowAgencyExcelList = new ArrayList();
							rowAgencyExcelList = agencyExcelList.get(i);
							agencyExcel = n_prod_u_15_073.set_prod_u_15_073(rowAgencyExcelList);	
							
						}else if(jenis_pas.equals("NISSAN DBD") ){
							agencyExcel.setMsp_pas_create_date(elionsManager.selectSysdate());
							
							n_prod_u_04_203 = new n_prod_u_04_203();
							List rowAgencyExcelList = new ArrayList();
							rowAgencyExcelList = agencyExcelList.get(i);
							agencyExcel = n_prod_u_04_203.set_prod_u_04_203(rowAgencyExcelList);	
							
						}else{	 //PAS AP-BP & BP-CARD
							//Produk Upload PAS Business Partner
							//@author : Adrian @since : Jan 21, 2015
							n_prod_u_01_187 = new n_prod_u_01_187();
							List rowAgencyExcelList = new ArrayList();
							rowAgencyExcelList = agencyExcelList.get(i);
							agencyExcel = n_prod_u_01_187.set_prod_u_01_187(rowAgencyExcelList);
								
							agencyExcel.setMsp_pas_create_date(elionsManager.selectSysdate());							
							try{							
								Date beg_date = df.parse(tanggalAwal);					
								agencyExcel.setMsp_pas_beg_date(beg_date);
							}
							catch (Exception e) {
								logger.error("ERROR :", e);						
							}
							Date end_date = (Date) agencyExcel.getMsp_pas_beg_date().clone();
							end_date.setYear(end_date.getYear()+1);
							end_date.setDate(end_date.getDate()-1);
							agencyExcel.setMsp_pas_end_date(end_date);
					    	
							Date debetDate = (Date) agencyExcel.getMsp_pas_beg_date().clone();
							debetDate.setMonth(debetDate.getMonth() + 6);
							debetDate.setDate(debetDate.getDate() - 1);
							agencyExcel.setMsp_tgl_debet(debetDate);
							
							agencyExcel.setMsp_admin_date(agencyExcel.getMsp_pas_beg_date());							
							
							//No.kartu
							if(jenis_pas.equals("BP CARD")){								
								rowAgencyExcelList = agencyExcelList.get(i);
								agencyExcel = n_prod_u_01_187.set_prod_u_01_187_Card(rowAgencyExcelList);
							}							
						}
						
						agencyExcel.setJenis_pas(jenis_pas);
						//otomatis
						agencyExcel.setLus_id(Integer.parseInt(user.getLus_id()));
						agencyExcel.setLus_id_uw_pas(Integer.parseInt(user.getLus_id()));
						
						//common						
						List<Map> lsWargaNegara = new ArrayList<Map>();						
					    lsWargaNegara=elionsManager.selectAllLstNegara();
						for(int j = 0 ; j < lsWargaNegara.size(); j++){ 
							if(lsWargaNegara.get(j).get("LSNE_NOTE").toString().equalsIgnoreCase(agencyExcelList.get(i).get(5).toString())){							
								agencyExcel.setMsp_warga(Integer.parseInt(lsWargaNegara.get(j).get("LSNE_ID").toString()));		
								break;
								}	
							} 
						
						List maritalList = DroplistManager.getInstance().get("MARITAL.xml","ID",request);						
						for(int k = 0 ; k < maritalList.size() ; k++){
							Map map2 = (Map) maritalList.get(k);
							String id =  (String) map2.get("ID");
							String marital = (String) map2.get("MARITAL");
							if(marital.equalsIgnoreCase(agencyExcelList.get(i).get(6).toString().trim())){
								agencyExcel.setMsp_status_nikah(Integer.parseInt(id));								
							}else{								
							}
						}
						
						List agamaList = DroplistManager.getInstance().get("AGAMA.xml","ID",request);						
						for(int k = 0 ; k < agamaList.size() ; k++){
							Map map2 = (Map) agamaList.get(k);
							String id =  (String) map2.get("ID");
							String agama = (String) map2.get("AGAMA");
							if(agama.equalsIgnoreCase(agencyExcelList.get(i).get(7).toString().trim())){
								agencyExcel.setLsag_id(Integer.parseInt(id));							
							}else{								
							}
						}
						
						if(!jenis_pas.equals("PAS SYARIAH")){
							if(!(jenis_pas.equals("SMART ACCIDENT CARE") || jenis_pas.equals("NISSAN PA") || jenis_pas.equals("NISSAN DBD") )){
								List autodebetList = DroplistManager.getInstance().get("AUTODEBET_PAS.xml","ID",request);						
								for(int k = 0 ; k < autodebetList.size() ; k++){
									Map map2 = (Map) autodebetList.get(k);
									String id =  (String) map2.get("ID");
									String autodebet = (String) map2.get("AUTODEBET");
									if(autodebet.equalsIgnoreCase(agencyExcelList.get(i).get(18).toString().trim())){
										agencyExcel.setMsp_flag_cc(Integer.parseInt(id));						
									}else{								
									}
								}						

								List<Map> lsBankPusat = new ArrayList<Map>();
								lsBankPusat = uwManager.selectBankPusat();
								for(int j = 0 ; j < lsBankPusat.size(); j++){ 
									if(lsBankPusat.get(j).get("LSBP_NAMA").toString().equalsIgnoreCase(agencyExcelList.get(i).get(20).toString().trim())){							
										agencyExcel.setLsbp_id(lsBankPusat.get(j).get("LSBP_ID").toString());								
										break;
									}
								}
							}
						}		
						
						// Hitung Umur PP & TT
						Integer umurPp = 0;
						Integer umurTt = 0;
						try{
							f_hit_umur umr = new f_hit_umur();							
							SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
							SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
							SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
							Date sysdate = elionsManager.selectSysdate();
							int tahun2 = Integer.parseInt(sdfYear.format(sysdate));
							int bulan2 = Integer.parseInt(sdfMonth.format(sysdate));
							int tanggal2 = Integer.parseInt(sdfDay.format(sysdate));							
							
							if(agencyExcel.getMsp_date_of_birth() != null){
								int tahun1 = Integer.parseInt(sdfYear.format(agencyExcel.getMsp_date_of_birth()));
								int bulan1 = Integer.parseInt(sdfMonth.format(agencyExcel.getMsp_date_of_birth()));
								int tanggal1 = Integer.parseInt(sdfDay.format(agencyExcel.getMsp_date_of_birth()));
							
								umurTt=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
								agencyExcel.setMsp_age(umurTt);
							}
						}catch(Exception e){							
						}
						
				/*
				// Proses Insert Data PAS 
				if(jenis_pas.equals("BP SMS")){
					agencyExcel = uwManager.updateBPSMS(agencyExcel);
				} else{	
					agencyExcel = uwManager.insertPas(agencyExcel, user);
				}									
					
				// Error Insert Data
				if(agencyExcel.getStatus() == 1){
					if(jenis_pas.equals("BP SMS")){
						map.put("sts", "FAILED");
						map.put("msg", "FAILED (Update BP-SMS):: No. "+agencyExcelList.get(i).get(0).toString()+" PAS-PP: "+agencyExcelList.get(i).get(1).toString()+"(PAS-TT: "+agencyExcelList.get(i).get(14).toString()+") : Proses Update BP SMS Gagal!! (Fire.Id tidak ditemukan/ Data PAS BP-SMS sudah ditransfer UW)");							
					} else{	
						map.put("sts", "FAILED");
						map.put("msg", "FAILED (Insert):: No. "+agencyExcelList.get(i).get(0).toString()+" PAS-PP: "+agencyExcelList.get(i).get(1).toString()+"(PAS-TT: "+agencyExcelList.get(i).get(14).toString()+") : Proses Insert Gagal");							
					}
					 successMessageList.add(map);
				}else{
					agencyExcel.setMsp_fire_id(agencyExcel.getMsp_fire_id());	
					if(jenis_pas.equals("BP SMS")){
						map.put("sts", "SUCCEED");
						map.put("msg", "SUCCEED (Update BP-SMS):: No. "+agencyExcelList.get(i).get(0).toString()+" PAS-PP: "+agencyExcelList.get(i).get(1).toString()+"(PAS-TT: "+agencyExcelList.get(i).get(14).toString()+") : FireId - "+agencyExcel.getMsp_fire_id());				
					} else{
						map.put("sts", "SUCCEED");
						map.put("msg", "SUCCEED (Insert):: No. "+agencyExcelList.get(i).get(0).toString()+" PAS-PP: "+agencyExcelList.get(i).get(1).toString()+"(PAS-TT: "+agencyExcelList.get(i).get(14).toString()+") : FireId - "+agencyExcel.getMsp_fire_id());									
					}
						successMessageList.add(map);
					request.setAttribute("success",successMessageList);		
					agenList.add(agencyExcel);
				}	
				*/
				 agenList.add(agencyExcel);					 
				 if(!StringUtil.isEmpty(jenis_pas)){
					 request.setAttribute("success","true");
				 }
			 }
			 }
			}			
			}else{
				Map<String,String> map = new HashMap<String, String>();
				map.put("sts", "FAILED");
				map.put("msg", "FAILED:: UPLOAD PRODUK: JENIS UPLOAD PRODUK TIDAK DITEMUKAN");
				successMessageList.add(map);
				request.setAttribute("successMessage",successMessageList);	
			}
		}		
		}
		
		//UNTUK DATA YANG MASUK KE INDIVIDU - AJSDB
		if(transfer!= null){
			/* (Proses Transfer to Akseptasi UW)
			for(Pas pasx : agenList){
				String msp_fire_id = pasx.getMsp_fire_id();
				String msp_id = uwManager.selectMspIdFromMspFireId(msp_fire_id);
				pasx.setMsp_id(msp_id);
				
				Map<String,String> map = new HashMap<String, String>();
				// Proses transfer ke Akseptasi UW -- lspd_id=2
				pasx = uwManager.transferPas(pasx, lus_id);	
				
				if(pasx.getStatus() == 1){
					 map.put("sts", "FAILED");
					 map.put("msg", "FAILED (TRANSFER):: FireId: "+pasx.getMsp_fire_id()+"(PAS PP: "+pasx.getMsp_pas_nama_pp()+" ) : Proses TRF to Akseptasi UW Gagal");							
					successMessageList.add(map);
				}else{											    
					map.put("sts", "SUCCEED");
					map.put("msg", "SUCCEED (TRANSFER TO AKSEPTASI UW):: FireId: "+pasx.getMsp_fire_id()+"(PAS PP: "+pasx.getMsp_pas_nama_pp()+ " )" );									
					successMessageList.add(map);
				}	
			
			}
			*/
			// Proses Akseptasi dan Transfer ke Payment
			/*
			for(Pas pasx : agenList){
				Map<String, Object> map = new HashMap<String, Object>();
				String msp_fire_id = pasx.getMsp_fire_id();
			
				String msp_id =  uwManager.selectMspIdFromMspFireId(msp_fire_id);
				List<Pas> pas = uwManager.selectAllPasList(msp_id, "1", null, null, null, "pas", null, "pas_aksep",null,null,null);
				Pas p = pas.get(0);  
			
				p.setLus_id_uw_pas(lus_id);
				p.setLus_login_name(user.getLus_full_name());
				p.setMsp_pas_accept_date(elionsManager.selectSysdate());
				p.setMsp_flag_aksep(1);
				p.setLspd_id(2);
				p.setLssp_id(10);//POLICY IS BEING PROCESSED 
				p.setMsp_kode_sts_sms("00");
				p = uwManager.updatePas(p);
			    // Process TRF to PAYMENT
				int aktif = 1;
				Map agentMap = elionsManager.selectagenpenutup(p.getMsag_id());
				if(agentMap == null){
					aktif = 0;
				}
				if(aktif == 0){// agen tidak aktif
					request.setAttribute("nama",p.getMsp_fire_id());
					request.setAttribute("msp_id",p.getMsp_id());
					request.setAttribute("agen_baru",elionsManager.selectMsagLeader(p.getMsag_id()));
					//request.setAttribute("successMessage","Agen tidak aktif");
					 map.put("sts", "FAILED");
					 map.put("msg", "FAILED (TRANSFER):: No. "+ pasx.getId_ref().trim()+" FireId: "+p.getMsp_fire_id()+"(PAS PP: "+p.getMsp_pas_nama_pp()+" ) : Agen tidak aktif");							
					successMessageList.add(map);
					
				}else{// agen aktif
					Cmdeditbac edit = new Cmdeditbac();
					//p.setLus_id(lus_id);
					p.setLus_id_uw_pas(lus_id);
					p.setLus_login_name(user.getLus_full_name());
					//langsung set ke posisi payment
					p.setLspd_id(4);
					 
					BindException errors = new BindException(new HashMap(), "cmd");
					//BindException errors;
					//binding data
					//errors = new BindException(bindAndValidate(request, cmd, false));
					
					edit=this.uwManager.prosesPas(request, "update", p, errors,"input",user,errors);
					try {
						
						String directory = props.getProperty("pdf.dir")+"\\Polis\\fire\\";
						
						List<DropDown> daftarFile = FileUtil.listFilesInDirectory(directory);
						List<DropDown> daftarFile2 = new ArrayList<DropDown>();
						
						List<DropDown> boleh = new ArrayList<DropDown>();
						boleh.add(new DropDown(p.getMsp_fire_id(), "PAS"));
						
						if(p.getMsp_fire_id()!=null && edit.getPemegang().getMspo_policy_no()!=null){
						
							for(DropDown d : daftarFile) {
								for(DropDown s : boleh) {
									if(d.getKey().toLowerCase().contains(s.getKey()) && !d.getKey().toLowerCase().contains("decrypted")) {
										d.setDesc(s.getValue());
										daftarFile2.add(d);
									}
								}
							}
							
							for(DropDown d2 : daftarFile2){
								copyfile("\\ebserver\\pdfind\\Polis\\fire\\"+d2.getKey(), "\\ebserver\\pdfind\\Polis\\"+edit.getPemegang().getLca_id()+"\\"+d2.getKey().replace(p.getMsp_fire_id(), "SPAJ"));
								deleteFile("\\ebserver\\pdfind\\Polis\\fire\\", d2.getKey(), null);
							}
						
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("ERROR :", e);
					}
					if(edit.getPemegang().getMspo_policy_no() == null){					
						 map.put("sts", "FAILED");
						 map.put("msg", "FAILED (TRANSFER TO PAYMENT):: No."+pasx.getId_ref().trim()+" FireId: "+p.getMsp_fire_id()+"(PAS PP: "+p.getMsp_pas_nama_pp()+" ) : Proses TRF to PAYMENT Gagal");							
						successMessageList.add(map);
					}else{
						//request.setAttribute("successMessage","transfer sukses dengan SPAJ : "+edit.getPemegang().getReg_spaj()+" dan No. Polis : "+edit.getPemegang().getMspo_policy_no());
						map.put("sts", "SUCCEED");
						map.put("msg", "Transfer ke payment Sukses:: No."+pasx.getId_ref().trim()+" FireId: "+p.getMsp_fire_id()+"(PAS PP: "+p.getMsp_pas_nama_pp()+ " ) dengan SPAJ : "+edit.getPemegang().getReg_spaj()+" dan No. Polis : "+edit.getPemegang().getMspo_policy_no() );									
						successMessageList.add(map);
					}
					
					//hasil=this.elionsManager.prosesTransferPembayaran(transfer,flagNew,errors);
				}
			
		}*/
			successMessageList = new ArrayList<Map>();
			successMessageList = uwManager.prosesMultiTrfPaymentPas(request,jenis_pas, agenList, user);
			request.setAttribute("successMessage",successMessageList);		
		}
				
		//UNTUK DATA YANG MASUK KE TM - EBDB
		if(transfer2!= null){
			successMessageList = new ArrayList<Map>();
//			successMessageList = regFreePaDmtm(tmSalesList, tanggalAwal);
			successMessageList = bacManager.processRegFreePaDmtm(tmSalesList, tanggalAwal, jenis_pas);
			request.setAttribute("successMessage",successMessageList);					
		}
		
		//user menekan tombol download template
		if(request.getParameter("dl_pasbp") != null || request.getParameter("dl_pasbpsms") != null || request.getParameter("dl_pasbpcard") != null || request.getParameter("dl_passyariah") != null || request.getParameter("dl_pasfree") != null || request.getParameter("dl_sac") != null
		        /*|| request.getParameter("dl_pasfree_bankdki") != null || request.getParameter("dl_dbdfree_bankdki") != null*/ || request.getParameter("dl_pasdbdfree_bankdki") != null || request.getParameter("dl_nissanpa") != null || request.getParameter("dl_nissandbd") != null || request.getParameter("dl_smartsave_medicalplus") != null){
			String fileName = "";
			String fileDir = props.getProperty("download.folder.template");
			if(request.getParameter("dl_pasbp") != null){
				fileName = "template_pasbp.xls";
			}else if(request.getParameter("dl_pasbpsms") != null){
				fileName = "template_pasbpsms.xls";
			}else if(request.getParameter("dl_pasbpcard") != null){
				fileName = "template_pasbpcard.xls";
			}else if(request.getParameter("dl_passyariah") != null){
				fileName = "template_passyariah.xls";
			}else if(request.getParameter("dl_pasfree") != null){
				fileName = "template_pasfree.xls";
			}else if(request.getParameter("dl_sac") != null
					|| request.getParameter("dl_nissanpa") != null
					|| request.getParameter("dl_nissandbd") != null){
				fileName = "template_passac.xls";
			}/*else if(request.getParameter("dl_pasfree_bankdki") != null){
			    fileName = "template_pasfree_bankdki.xls";
			}else if(request.getParameter("dl_dbdfree_bankdki") != null){
			    fileName = "template_dbdfree_bankdki.xls";
			}*/else if(request.getParameter("dl_pasdbdfree_bankdki") != null){
			    fileName = "template_pasdbdfree_bankdki.xls";
			} else if(request.getParameter("dl_smartsave_medicalplus") != null){
			    fileName = "template_smartsave_medicalplus.xls";
			}
			
			FileUtils.downloadFile("attachment;", fileDir, fileName, response);
			//return null;
					
			//load halaman
		}
		
		//PAS-FREE
		//UNTUK DATA YANG MASUK KE TM - EBDB
		if(submit_pasfree != null){			
			tmSalesList.clear();						
			valid = "true";
			tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
						
			Upload upload = new Upload();
			String file_fp = request.getParameter("file_fp");
			upload.setDaftarFile(new ArrayList<MultipartFile>(10));
			ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
			binder.bind(request);
			MultipartFile mf = upload.getFile1();
			String fileName = mf.getOriginalFilename();
			String directory = props.getProperty("temp.dir.fileupload");
		//	String directory = file_fp.replaceAll(fileName, "");
						
			ExcelRead excelRead = new ExcelRead();		
			TmSales tmSales = null;			
			List<List> agencyExcelList = new ArrayList<List>();

            Integer jenis_upload_produk = ServletRequestUtils.getIntParameter(request, "jenis_upload_produk", 0);
            
            if(jenis_upload_produk == 7) {
                jenis_pas = "FREE PA BANK DKI";
            } else if(jenis_upload_produk == 8) {
                jenis_pas = "FREE DBD BANK DKI";
            } else if(jenis_upload_produk == 9) {
                jenis_pas = "FREE PA & DBD BANK DKI";
            } else {
                jenis_pas = "PAS FREE";
            }
					
			try{
				String dest=directory + "/" + fileName ;
				File outputFile = new File(dest);
			    FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
			    
			    if(jenis_upload_produk == 7 || jenis_upload_produk == 8 || jenis_upload_produk == 9) {
			        agencyExcelList = excelRead.readExcelFileNew(directory + "\\", fileName);
			    } else {
			        agencyExcelList = excelRead.readExcelFile(directory + "\\", fileName);
			    }
				
			}catch (Exception e) {
				e.printStackTrace();
				Map<String,String> map = new HashMap<String, String>();
				map.put("sts", "FAILED");
				map.put("msg", "FAILED:: akses file gagal");
				successMessageList.add(map);
			}		
//			int jmlh_header = agencyExcelList.get(0).size();
//			// Untuk jenis_pas = "BP SMS" jmlh_kolom Excel = 27
//			// Untuk jenis_pas = "AP/BP" jmlh_kolom Excel = 23
//			if (jmlh_header>=27) {
//				jenis_pas = "BP SMS";
//			}else{
//				jenis_pas = "AP/BP";
//			}
			
			if(agencyExcelList.size()>50){
				Map<String,String> map = new HashMap<String, String>();
				map.put("sts", "FAILED");
				map.put("msg", "FAILED:: Upload tidak dapat dilakukan, Maksimal jumlah data upload 50!");
				successMessageList.add(map);
				request.setAttribute("successMessage",successMessageList);					
			}else{		
					Map<String,String> map = new HashMap<String, String>();
					map.put("sts", "SUCCEED");
					int size = agencyExcelList.size()-1;
					map.put("msg", "*Jumlah data "+ jenis_pas +" diproses: "+size);
					successMessageList.add(map);
					
				for(int i = 1 ; i < agencyExcelList.size() ; i++){
					if(!agencyExcelList.get(i).get(0).toString().isEmpty()){
						//Map<String,String> map = new HashMap<String, String>();
						map = new HashMap<String, String>();
						try{
							tmSales = new TmSales();
							SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
							//SET Class_11_056 ***	
							n_prod_u_11_056 = new n_prod_u_11_056();
							n_prod_u_11_060 = new n_prod_u_11_060();
                            n_prod_u_11_061 = new n_prod_u_11_061();
							
							List rowAgencyExcelList = new ArrayList();
							rowAgencyExcelList = agencyExcelList.get(i);
//							tmSales = n_prod_u_11_056.set_prod_u_11_056(rowAgencyExcelList);												
							
							if(jenis_upload_produk == 7 || (jenis_upload_produk == 9 && rowAgencyExcelList.size() > 7 && "PA".equalsIgnoreCase(rowAgencyExcelList.get(7).toString()))) {
							    tmSales = n_prod_u_11_060.set_prod_u_11_060(rowAgencyExcelList);
							} else if(jenis_upload_produk == 8 || (jenis_upload_produk == 9 && rowAgencyExcelList.size() > 7 && "DBD".equalsIgnoreCase(rowAgencyExcelList.get(7).toString()))) {
							    tmSales = n_prod_u_11_061.set_prod_u_11_061(rowAgencyExcelList);
							} else {
							    tmSales = n_prod_u_11_056.set_prod_u_11_056(rowAgencyExcelList);
							}
							
							//VALIDASI FIELD EXCEL ***		
						    err = "";
//						    err = err + n_prod_u_11_056.validate_prod_u_11_056();
						    
						    if(jenis_upload_produk == 7 || (jenis_upload_produk == 9 && rowAgencyExcelList.size() > 7 && "PA".equalsIgnoreCase(rowAgencyExcelList.get(7).toString()))) {
						        err = err + n_prod_u_11_060.validate_prod_u_11_060();
						    } else if(jenis_upload_produk == 8 || (jenis_upload_produk == 9 && rowAgencyExcelList.size() > 7 && "DBD".equalsIgnoreCase(rowAgencyExcelList.get(7).toString()))) {
						        err = err + n_prod_u_11_061.validate_prod_u_11_061();
						    } else {
						        err = err + n_prod_u_11_056.validate_prod_u_11_056();
						    }
						    
						    if(!agencyExcelList.get(i).get(4).toString().trim().isEmpty()){
						    	 String Dob = agencyExcelList.get(i).get(4).toString().trim();						    
						    	 String[] arr_Dob = Dob.split("/");						    								
							     if(arr_Dob[0].length()!=2 || arr_Dob[1].length()!=2 || arr_Dob[2].length()!=4){
							    	err = err+ " TGL_LAHIR harus dalam Format dd/MM/YYYY !,";
							     }
						    }
					    	Integer umur = 0;
					    	try{
								f_hit_umur umr = new f_hit_umur();
								
								SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
								SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
								SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
								Date sysdate = elionsManager.selectSysdate();
								int tahun2 = Integer.parseInt(sdfYear.format(sysdate));
								int bulan2 = Integer.parseInt(sdfMonth.format(sysdate));
								int tanggal2 = Integer.parseInt(sdfDay.format(sysdate));
								
								if(tmSales.getBod_holder() != null){								
									int tahun1 = Integer.parseInt(sdfYear.format(tmSales.getBod_holder()));
									int bulan1 = Integer.parseInt(sdfMonth.format(tmSales.getBod_holder()));
									int tanggal1 = Integer.parseInt(sdfDay.format(tmSales.getBod_holder()));
									
									umur=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
								}
					    	}
					    	catch (Exception e) {}
					    	
//					    	err = err+ "  Umur Tertanggung minimal 1 tahun & maksimal 65 tahun,";
					    	
					    	if(jenis_upload_produk != 7 && jenis_upload_produk != 8 && jenis_upload_produk != 9) {
					    	    err = err+ "  Umur Tertanggung minimal 1 tahun & maksimal 65 tahun,";
					    	}
					    	
					    	if(jenis_upload_produk == 9 && (rowAgencyExcelList.size() < 8 || Common.isEmpty(rowAgencyExcelList.get(7).toString()))) {
					    	    err += " PLAN  harus diisi Format: PA/DBD,";  
					    	}
					    						    	
							if(!"".equals(err)){
								 valid = "false";
								// Error Validasi Data
								 map.put("sts", "FAILED");
								 map.put("msg", "FAILED (Validation):: No. "+agencyExcelList.get(i).get(0).toString()+" " + jenis_pas.replace(" ", "-") + ": "+agencyExcelList.get(i).get(1).toString()+": "+err);					
								successMessageList.add(map);
							}else{
								map = new HashMap<String, String>();								
								map.put("sts", "SUCCEED");
								map.put("msg", "SUCCEED (Validation):: No. "+agencyExcelList.get(i).get(0).toString()+" " + jenis_pas.replace(" ", "-") + ": "+agencyExcelList.get(i).get(1).toString());					
								successMessageList.add(map);								
							}							
						}catch (Exception e) {
							 valid = "false";
							logger.error("ERROR :", e);
								map.put("sts", "FAILED");
								map.put("msg", "FAILED (Try):: " + jenis_pas.replace(" ", "-") + ": "+agencyExcelList.get(i).get(1).toString()+" : Format nama kolom / data upload ada yg tidak sesuai!!! (Tanggal harus diisi dgn Format: DD/mm/yyyy)");
								successMessageList.add(map);
						}	
					}
				}
				
				if("true".equals(valid)){
					for(int i = 1 ; i < agencyExcelList.size() ; i++){								
						if(!agencyExcelList.get(i).get(0).toString().isEmpty()){
							//Map<String,String> map = new HashMap<String, String>();
							map = new HashMap<String, String>();
								
							//SET Class_11_056 ***	
							n_prod_u_11_056 = new n_prod_u_11_056();
                            n_prod_u_11_060 = new n_prod_u_11_060();
                            n_prod_u_11_061 = new n_prod_u_11_061();
							
							List rowAgencyExcelList = new ArrayList();
							rowAgencyExcelList = agencyExcelList.get(i);
//							tmSales = n_prod_u_11_056.set_prod_u_11_056(rowAgencyExcelList);									
							
							if(jenis_upload_produk == 7 || (jenis_upload_produk == 9 && rowAgencyExcelList.size() > 7 && "PA".equalsIgnoreCase(rowAgencyExcelList.get(7).toString()))) {
							    tmSales = n_prod_u_11_060.set_prod_u_11_060(rowAgencyExcelList);
							} else if(jenis_upload_produk == 8 || (jenis_upload_produk == 9 && rowAgencyExcelList.size() > 7 && "DBD".equalsIgnoreCase(rowAgencyExcelList.get(7).toString()))) {
							    tmSales = n_prod_u_11_061.set_prod_u_11_061(rowAgencyExcelList);
							} else {
							    tmSales = n_prod_u_11_056.set_prod_u_11_056(rowAgencyExcelList);
							}
							
							tmSalesList.add(tmSales);
						}
					}						
					//successMessageList = new ArrayList<Map>();
					//successMessageList = regFreePaDmtm(tmSalesList, tanggalAwal);
				   request.setAttribute("success2","true");
				   request.setAttribute("jenis_pas", jenis_pas);
				}
			}
		}
		
		
		//SMART SAVE + SMiLe Medical(+)
		if(submit_smartsave != null){			
			agenList.clear();						
			valid = "true";
			tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
						
			Upload upload = new Upload();
			String file_fp = request.getParameter("file_fp");
			upload.setDaftarFile(new ArrayList<MultipartFile>(10));
			ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
			binder.bind(request);
			MultipartFile mf = upload.getFile1();
			String fileName = mf.getOriginalFilename();
			//String directory = props.getProperty("temp.dir.fileupload");
			String directory = "C:\\EkaWeb\\temp_user_input";
		//	String directory = file_fp.replaceAll(fileName, "");
						
			ExcelRead excelRead = new ExcelRead();		
			//TmSales tmSales = null;		
			Pas agencyExcel = null;
			List<List> agencyExcelList = new ArrayList<List>();

            Integer jenis_upload_produk = ServletRequestUtils.getIntParameter(request, "jenis_upload_produk", 0);
            
            if(jenis_upload_produk == 10) {
                jenis_pas = "SMART SAVE & SMiLe MEDICAL(+)";
            } 
					
			try{
				String dest=directory + "/" + fileName ;
				File outputFile = new File(dest);
			    FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
			    			   
			     agencyExcelList = excelRead.readExcelFileNew(directory + "\\", fileName);
			  //   agencyExcelList = excelRead.readExcelFile(directory + "\\", fileName);
			 
				
			}catch (Exception e) {
				e.printStackTrace();
				Map<String,String> map = new HashMap<String, String>();
				map.put("sts", "FAILED");
				map.put("msg", "FAILED:: akses file gagal");
				successMessageList.add(map);
			}		
//			int jmlh_header = agencyExcelList.get(0).size();
//			// Untuk jenis_pas = "BP SMS" jmlh_kolom Excel = 27
//			// Untuk jenis_pas = "AP/BP" jmlh_kolom Excel = 23
//			if (jmlh_header>=27) {
//				jenis_pas = "BP SMS";
//			}else{
//				jenis_pas = "AP/BP";
//			}
			
			if(agencyExcelList.size()>50){
				Map<String,String> map = new HashMap<String, String>();
				map.put("sts", "FAILED");
				map.put("msg", "FAILED:: Upload tidak dapat dilakukan, Maksimal jumlah data upload 50!");
				successMessageList.add(map);
				request.setAttribute("successMessage",successMessageList);					
			}else{		
					Map<String,String> map = new HashMap<String, String>();
					map.put("sts", "SUCCEED");
					int size = agencyExcelList.size()-1;
					map.put("msg", "*Jumlah data "+ jenis_pas +" diproses: "+size);
					successMessageList.add(map);
					
				for(int i = 1 ; i < agencyExcelList.size() ; i++){
					if(!agencyExcelList.get(i).get(0).toString().isEmpty()){
						//Map<String,String> map = new HashMap<String, String>();
						map = new HashMap<String, String>();
						try{
							agencyExcel = new Pas();
							SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						
							List rowAgencyExcelList = new ArrayList();
							rowAgencyExcelList = agencyExcelList.get(i);
//							tmSales = n_prod_u_11_056.set_prod_u_11_056(rowAgencyExcelList);												
							
							agencyExcel.setProduct_code("142");
						     							
							agencyExcel.setMsp_full_name(rowAgencyExcelList.get(1).toString());
							agencyExcel.setMsp_pas_tmp_lhr_tt(rowAgencyExcelList.get(2).toString());
							
							try{							
								Date msp_date_of_birth = df.parse(rowAgencyExcelList.get(3).toString());					
								agencyExcel.setMsp_date_of_birth(msp_date_of_birth);
								
								
								Date beg_date = df.parse(rowAgencyExcelList.get(7).toString());	
								agencyExcel.setMsp_pas_beg_date(beg_date);
							}
							catch (Exception e) {
								e.printStackTrace();						
							}				
							agencyExcel.setMsp_identity_no_tt(rowAgencyExcelList.get(4).toString());
							agencyExcel.setMspe_mother(rowAgencyExcelList.get(5).toString());
							agencyExcel.setMspo_plan_provider(rowAgencyExcelList.get(8).toString());
							
							if(agencyExcel.getMspo_plan_provider().trim() != null){
								if(agencyExcel.getMspo_plan_provider().trim().equalsIgnoreCase("RI-300")){
									agencyExcel.setPremi("60000000");
									agencyExcel.setMsp_premi("60000000");
									agencyExcel.setMsp_up("60000000");
								}else if(agencyExcel.getMspo_plan_provider().trim().equalsIgnoreCase("RI-400")){
									agencyExcel.setPremi("80000000");
									agencyExcel.setMsp_premi("80000000");
									agencyExcel.setMsp_up("80000000");
								}else if(agencyExcel.getMspo_plan_provider().trim().equalsIgnoreCase("RI-500")){
									agencyExcel.setPremi("100000000");
									agencyExcel.setMsp_premi("100000000");
									agencyExcel.setMsp_up("100000000");
								}
							}
							
							agencyExcel.setMsp_rek_nama(rowAgencyExcelList.get(9).toString());							
							agencyExcel.setMsp_no_rekening(rowAgencyExcelList.get(10).toString());
							agencyExcel.setMsp_pas_phone_number(rowAgencyExcelList.get(6).toString());
							
							List<Map> lsBankPusat = new ArrayList<Map>();
							lsBankPusat = uwManager.selectBankPusat();
							for(int j = 0 ; j < lsBankPusat.size(); j++){ 
								if(lsBankPusat.get(j).get("LSBP_NAMA").toString().equalsIgnoreCase(rowAgencyExcelList.get(11).toString().trim())){ 						
									agencyExcel.setLsbp_id(lsBankPusat.get(j).get("LSBP_ID").toString());								
									break;
								}
							}
							agencyExcel.setMsp_rek_cabang(rowAgencyExcelList.get(12).toString());
							agencyExcel.setMsp_rek_kota(rowAgencyExcelList.get(13).toString());
							
							agencyExcel.setMsaw_first1(rowAgencyExcelList.get(14).toString());
							agencyExcel.setLsre_relation1("4");
							if( rowAgencyExcelList.get(16) != null ){
							if( !rowAgencyExcelList.get(16).toString().equals("") ){
								agencyExcel.setMsaw_persen1(Double.valueOf(rowAgencyExcelList.get(16).toString()));
							}
							}
							
							agencyExcel.setMsaw_first2(rowAgencyExcelList.get(17).toString());
							agencyExcel.setLsre_relation2("4");
							if( rowAgencyExcelList.get(19) != null ){
								if( !rowAgencyExcelList.get(19).toString().equals("") ){
									agencyExcel.setMsaw_persen2(Double.valueOf(rowAgencyExcelList.get(19).toString()));
								}
							}
							
							agencyExcel.setMsaw_first3(rowAgencyExcelList.get(20).toString());
							agencyExcel.setLsre_relation3("4");
							if( rowAgencyExcelList.get(22) != null ){
								if( !rowAgencyExcelList.get(22).toString().equals("") ){
									agencyExcel.setMsaw_persen3(Double.valueOf(rowAgencyExcelList.get(22).toString()));
								}
							}
							
														
							agencyExcel.setMsaw_first4(rowAgencyExcelList.get(23).toString());
							agencyExcel.setLsre_relation4("4");
							if( rowAgencyExcelList.get(25) != null ){
								if( !rowAgencyExcelList.get(25).toString().equals("") ){
									agencyExcel.setMsaw_persen4(Double.valueOf(rowAgencyExcelList.get(25).toString()));
								}
							}
							
							
							
							//VALIDASI FIELD EXCEL ***		
						    err = "";
//						    err = err + n_prod_u_11_056.validate_prod_u_11_056();
						    						  						    
						    if(!agencyExcelList.get(i).get(3).toString().trim().isEmpty()){
						    	 String Dob = agencyExcelList.get(i).get(3).toString().trim();						    
						    	 String[] arr_Dob = Dob.split("/");						    								
							     if(arr_Dob[0].length()!=2 || arr_Dob[1].length()!=2 || arr_Dob[2].length()!=4){
							    	err = err+ " TGL_LAHIR harus dalam Format dd/MM/YYYY !,";
							     }
						    }
						    if(!agencyExcelList.get(i).get(7).toString().trim().isEmpty()){
						    	 String Dob = agencyExcelList.get(i).get(7).toString().trim();						    
						    	 String[] arr_Dob = Dob.split("/");						    								
							     if(arr_Dob[0].length()!=2 || arr_Dob[1].length()!=2 || arr_Dob[2].length()!=4){
							    	err = err+ " TGL Mulai Asuransi harus dalam Format dd/MM/YYYY !,";
							     }
						    }
						    
						    
					    	Integer umur = 0;
					    	try{
								f_hit_umur umr = new f_hit_umur();
								
								SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
								SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
								SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
								Date sysdate = elionsManager.selectSysdate();
								int tahun2 = Integer.parseInt(sdfYear.format(sysdate));
								int bulan2 = Integer.parseInt(sdfMonth.format(sysdate));
								int tanggal2 = Integer.parseInt(sdfDay.format(sysdate));
								
								if(agencyExcel.getMsp_date_of_birth() != null){								
									int tahun1 = Integer.parseInt(sdfYear.format(agencyExcel.getMsp_date_of_birth()));
									int bulan1 = Integer.parseInt(sdfMonth.format(agencyExcel.getMsp_date_of_birth()));
									int tanggal1 = Integer.parseInt(sdfDay.format(agencyExcel.getMsp_date_of_birth()));
									
									umur=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
								}
					    	}
					    	catch (Exception e) {}
					    	
					    	agencyExcel.setMsp_age(umur);
					    	
					    	
					    	if( agencyExcel.getMsp_age() == null) {
					    		err = err+ "  Umur Tertanggung belum diset atau salah format,";
					    	}else if(umur < 20 || agencyExcel.getMsp_age()<20 ){
					    		err = err+ "  Umur Tertanggung minimal 20 tahun,";
					    	}else if(umur > 60 || agencyExcel.getMsp_age()>60 ){
					    		err = err+ "  Umur Tertanggung maksimal 60 tahun,";
					    	}
					    	
					    
					    	if( agencyExcel.getLsbp_id() == null) {
					    		err = err+ " Kolom Bank belum diisi atau salah format,";
					    	}
					    	if( agencyExcel.getMsp_no_rekening() == null) {
					    		err = err+ " Kolom No.Rek KUD belum diisi,";
					    	}
					    	
					    	if(Common.isEmpty(agencyExcel.getMsp_no_rekening())) {
					    		err = err+ " Kolom No.Rek KUD belum diisi,";
					    	}
					    	if(Common.isEmpty(agencyExcel.getMsp_rek_cabang())) {
					    		err = err+ " Kolom Rek.Cabang Bank belum diisi,";
					    	}
					    	if(Common.isEmpty(agencyExcel.getMsp_rek_kota())) {
					    		err = err+ " Kolom Kota belum diisi,";
					    	}
					    	if(agencyExcel.getMspo_plan_provider().trim() == null || (!agencyExcel.getMspo_plan_provider().trim().equalsIgnoreCase("RI-300")
					    	&& !agencyExcel.getMspo_plan_provider().trim().equalsIgnoreCase("RI-400") && !agencyExcel.getMspo_plan_provider().trim().equalsIgnoreCase("RI-500"))){
					    		err = err+ " Kolom Plan belum diisi sesuai format: RI-300/RI-400/RI-500,";
					    	}
					    	if(agencyExcel.getMsp_rek_nama().trim() == null){
					    		err = err+ " Kolom nama pemilik Rek. belum diisi,";
					    	}
					    	
					    	
							
							if(!"".equals(err)){
								 valid = "false";
								// Error Validasi Data
								 map.put("sts", "FAILED");
								 map.put("msg", "FAILED (Data Validation):: No. "+agencyExcelList.get(i).get(0).toString()+" " + jenis_pas.replace(" ", "-") + ": Nama Tertanggung ("+agencyExcelList.get(i).get(1).toString()+"): "+err);					
								successMessageList.add(map);
							}else{
								map = new HashMap<String, String>();								
								map.put("sts", "SUCCEED");
								map.put("msg", "SUCCEED (Data Validation):: No. "+agencyExcelList.get(i).get(0).toString()+" " + jenis_pas.replace(" ", "-") + ": Nama Tertanggung ("+agencyExcelList.get(i).get(1).toString()+") ");					
								successMessageList.add(map);								
							}							
						}catch (Exception e) {
							 valid = "false";
							e.printStackTrace();
								map.put("sts", "FAILED");
								map.put("msg", "FAILED (Try):: " + jenis_pas.replace(" ", "-") + ": "+agencyExcelList.get(i).get(1).toString()+" : Format nama kolom / data upload ada yg tidak sesuai!!! (Tanggal harus diisi dgn Format: DD/mm/yyyy)");
								successMessageList.add(map);
						}	
					}
				}
				
				if("true".equals(valid)){
					for(int i = 1 ; i < agencyExcelList.size() ; i++){								
						if(!agencyExcelList.get(i).get(0).toString().isEmpty()){
							//Map<String,String> map = new HashMap<String, String>();
							map = new HashMap<String, String>();
								
							agencyExcel = new Pas();
							SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						
							List rowAgencyExcelList = new ArrayList();
							rowAgencyExcelList = agencyExcelList.get(i);
//							tmSales = n_prod_u_11_056.set_prod_u_11_056(rowAgencyExcelList);												
							
							agencyExcel.setProduct_code("142");
						     							
							agencyExcel.setMsp_full_name(rowAgencyExcelList.get(1).toString());
							agencyExcel.setMsp_pas_tmp_lhr_tt(rowAgencyExcelList.get(2).toString());
							
							try{							
								Date msp_date_of_birth = df.parse(rowAgencyExcelList.get(3).toString());					
								agencyExcel.setMsp_date_of_birth(msp_date_of_birth);
								
								
								Date beg_date = df.parse(rowAgencyExcelList.get(7).toString());	
								agencyExcel.setMsp_pas_beg_date(beg_date);
							}
							catch (Exception e) {
								e.printStackTrace();						
							}				
							
							Integer umur = 0;
					    	try{
								f_hit_umur umr = new f_hit_umur();
								
								SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
								SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
								SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
								Date sysdate = elionsManager.selectSysdate();
								int tahun2 = Integer.parseInt(sdfYear.format(sysdate));
								int bulan2 = Integer.parseInt(sdfMonth.format(sysdate));
								int tanggal2 = Integer.parseInt(sdfDay.format(sysdate));
								
								if(agencyExcel.getMsp_date_of_birth() != null){								
									int tahun1 = Integer.parseInt(sdfYear.format(agencyExcel.getMsp_date_of_birth()));
									int bulan1 = Integer.parseInt(sdfMonth.format(agencyExcel.getMsp_date_of_birth()));
									int tanggal1 = Integer.parseInt(sdfDay.format(agencyExcel.getMsp_date_of_birth()));
									
									umur=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
								}
					    	}
					    	catch (Exception e) {}
					    	
					    	agencyExcel.setMsp_age(umur);
							
							
							agencyExcel.setMsp_identity_no_tt(rowAgencyExcelList.get(4).toString());
							agencyExcel.setMspe_mother(rowAgencyExcelList.get(5).toString());
							agencyExcel.setMspo_plan_provider(rowAgencyExcelList.get(8).toString());
							
							if(agencyExcel.getMspo_plan_provider().trim() != null){
								if(agencyExcel.getMspo_plan_provider().trim().equalsIgnoreCase("RI-300")){
									agencyExcel.setPremi("60000000");
									agencyExcel.setMsp_premi("60000000");
									agencyExcel.setMsp_up("60000000");
								}else if(agencyExcel.getMspo_plan_provider().trim().equalsIgnoreCase("RI-400")){
									agencyExcel.setPremi("80000000");
									agencyExcel.setMsp_premi("80000000");
									agencyExcel.setMsp_up("80000000");
								}else if(agencyExcel.getMspo_plan_provider().trim().equalsIgnoreCase("RI-500")){
									agencyExcel.setPremi("100000000");
									agencyExcel.setMsp_premi("100000000");
									agencyExcel.setMsp_up("100000000");
								}
							}
							
						
							
							agencyExcel.setMsp_rek_nama(rowAgencyExcelList.get(9).toString());							
							agencyExcel.setMsp_no_rekening(rowAgencyExcelList.get(10).toString());
							agencyExcel.setMsp_pas_phone_number(rowAgencyExcelList.get(6).toString());
							
							List<Map> lsBankPusat = new ArrayList<Map>();
							lsBankPusat = uwManager.selectBankPusat();
							for(int j = 0 ; j < lsBankPusat.size(); j++){ 
								if(lsBankPusat.get(j).get("LSBP_NAMA").toString().equalsIgnoreCase(rowAgencyExcelList.get(11).toString().trim())){ 						
									agencyExcel.setLsbp_id(lsBankPusat.get(j).get("LSBP_ID").toString());								
									break;
								}
							}
							agencyExcel.setMsp_rek_cabang(rowAgencyExcelList.get(12).toString());
							agencyExcel.setMsp_rek_kota(rowAgencyExcelList.get(13).toString());
							
							agencyExcel.setMsaw_first1(rowAgencyExcelList.get(14).toString());
							agencyExcel.setLsre_relation1("4");
							if( rowAgencyExcelList.get(16) != null ){
							if( !rowAgencyExcelList.get(16).toString().equals("") ){
								agencyExcel.setMsaw_persen1(Double.valueOf(rowAgencyExcelList.get(16).toString()));
							}
							}
							
							agencyExcel.setMsaw_first2(rowAgencyExcelList.get(17).toString());
							agencyExcel.setLsre_relation2("4");
							if( rowAgencyExcelList.get(19) != null ){
								if( !rowAgencyExcelList.get(19).toString().equals("") ){
									agencyExcel.setMsaw_persen2(Double.valueOf(rowAgencyExcelList.get(19).toString()));
								}
							}
							
							agencyExcel.setMsaw_first3(rowAgencyExcelList.get(20).toString());
							agencyExcel.setLsre_relation3("4");
							if( rowAgencyExcelList.get(22) != null ){
								if( !rowAgencyExcelList.get(22).toString().equals("") ){
									agencyExcel.setMsaw_persen3(Double.valueOf(rowAgencyExcelList.get(22).toString()));
								}
							}
							
														
							agencyExcel.setMsaw_first4(rowAgencyExcelList.get(23).toString());
							agencyExcel.setLsre_relation4("4");
							if( rowAgencyExcelList.get(25) != null ){
								if( !rowAgencyExcelList.get(25).toString().equals("") ){
									agencyExcel.setMsaw_persen4(Double.valueOf(rowAgencyExcelList.get(25).toString()));
								}
							}
							
							
							
							agenList.add(agencyExcel);
						}
					}						
					//successMessageList = new ArrayList<Map>();
					//successMessageList = regFreePaDmtm(tmSalesList, tanggalAwal);
				   request.setAttribute("success","true");
				   request.setAttribute("jenis_pas", jenis_pas);
				   
					request.setAttribute("jenis_upload_produk", jenis_upload_produk);
				}
			}
		}
		
		
		if(transfer3!= null){			
			Cmdeditbac excelList = new Cmdeditbac();
		//	Cmdeditbac excelList = (Cmdeditbac)cmd;
			successMessageList = new ArrayList<Map>();
			
			Pemegang pemegang = new Pemegang();
			Tertanggung tertanggung = new Tertanggung();
			Rekening_client rekCLient = new Rekening_client();
			Datausulan datausulan = new Datausulan();	
			Agen agen = new Agen();
			Personal personal = new Personal();
			PembayarPremi pembayarPremi = new PembayarPremi();
			AddressBilling addressBilling = new AddressBilling();
			Benefeciary benefeciary = new Benefeciary();
			Powersave powersave = new Powersave();
						
			for(Pas agencyExcel : agenList){
			//for(int n = 0 ; n < agenList.size() ; n++){
			//	n=n-1;
			//Pas agencyExcel = agenList.get(n);
			
			//String mcl_idPp= getMclId(1, fmYearSpaj, elionsManager, bacManager );
			//pemegang.setMcl_id(mcl_idPp);
			pemegang.setMcl_first(agencyExcel.getMsp_full_name());//nama lengkap pemegang			
			pemegang.setMspe_date_birth(null);//tanggal lahir pemegang	
			pemegang.setMspe_date_birth(agencyExcel.getMsp_date_of_birth());
			pemegang.setMspe_mother(agencyExcel.getMspe_mother());
			pemegang.setMspe_place_birth("JAKARTA");//kota lahir pemegang	
			pemegang.setMcl_company_name("PT Bumi Serpong Damai Tbk");//perusahaan tempat bekerja
			pemegang.setLus_id(Integer.parseInt(user.getLus_id()));
			pemegang.setKota_kantor("JAKARTA");//kota kantor pemegang
			pemegang.setLsre_id(1);
			pemegang.setMcl_jenis(0);
			pemegang.setMcl_blacklist(0);
			pemegang.setLscb_id(0);
			pemegang.setTgl_verification_date(elionsManager.selectSysdate());
			pemegang.setMspo_spaj_date(agencyExcel.getMsp_pas_beg_date());
			pemegang.setPemegang_polis("2");
			pemegang.setNama_si("");			
			pemegang.setNama_anak1("");
			pemegang.setNama_anak2("");
			pemegang.setNama_anak3("");
			pemegang.setAlamat_rumah("-");
			pemegang.setAlamat_kantor("-");
			// PEMEGANG.FLAG_SPAJ = 5 = GIO(TIDAK PERLU KUESIONER DATA KESEHATAN)		
			pemegang.setMspo_flag_spaj(0);
			
			String[] dataSumberDana= { "-" };			
			pemegang.setPendapatanBulan(dataSumberDana);
			pemegang.setTujuanInvestasi(dataSumberDana);
			
			pemegang.setMspo_ao("528150");
			pemegang.setMspo_follow_up(0);
			
			//String mcl_idTtg = getMclId(2,fmYearSpaj,elionsManager, bacManager );
			//tertanggung.setMcl_id(mcl_idTtg);
			tertanggung.setMcl_first(agencyExcel.getMsp_full_name());//nama lengkap tertanggung
			tertanggung.setMspe_mother(agencyExcel.getMspe_mother());//nama ibu kandung tertanggung
			tertanggung.setLside_id(1);
			tertanggung.setMspe_no_identity(agencyExcel.getMsp_identity_no_tt());
			tertanggung.setMspe_date_birth(agencyExcel.getMsp_date_of_birth());//tanggal lahir tertanggung
			tertanggung.setMspe_place_birth(agencyExcel.getMsp_pas_tmp_lhr_tt());
			tertanggung.setAlamat_kantor("-");//alamat kantor tertanggung
			tertanggung.setKota_kantor("-");//kota kantor tertanggung
			tertanggung.setAlamat_rumah("-");//alamat rumah
			tertanggung.setKota_rumah("-");//kota rumah
			tertanggung.setNo_hp("-");//no hp tertanggung
			//tertanggung.setEmail("-");//email tertanggung
			tertanggung.setMste_age(agencyExcel.getMsp_age());
			tertanggung.setMcl_blacklist(0);			
			tertanggung.setMste_no_vacc("");
			tertanggung.setNama_si("");			
			tertanggung.setNama_anak1("");
			tertanggung.setNama_anak2("");
			tertanggung.setNama_anak3("");
			tertanggung.setUsiattg(agencyExcel.getMsp_age());
			pemegang.setMspo_age(tertanggung.getUsiattg());
			pemegang.setUsiapp(tertanggung.getUsiattg());
			
			tertanggung.setPendapatanBulan(dataSumberDana);
			tertanggung.setTujuanInvestasi(dataSumberDana);
						
			
			rekCLient.setMrc_no_ac(agencyExcel.getMsp_no_rekening());
			rekCLient.setLsbp_id(agencyExcel.getLsbp_id());
			rekCLient.setMrc_nama(agencyExcel.getMsp_rek_nama());
			rekCLient.setMrc_kota(agencyExcel.getMsp_rek_kota());
			rekCLient.setMrc_cabang(agencyExcel.getMsp_rek_cabang());
			InvestasiUtama investasiUtama = new InvestasiUtama();
			
			Integer payperiod=new Integer(0);
			Integer insperiod=new Integer(0);
			Double up=new Double(0.);
			//SetDataUsulan
			try{
				Class aClass;				
				n_prod produk = new n_prod();			
				String nama_produk;
				//set begdate
				datausulan.setMspr_beg_date(agencyExcel.getMsp_pas_beg_date());	
				datausulan.setLsbs_id(142); 
				datausulan.setLsdbs_number(11);
				datausulan.setKurs_premi("01");//set kurs
				datausulan.setLku_id("01");
				datausulan.setLscb_id(0);
				datausulan.setLscb_id_rider(0);
				
				datausulan.setMste_flag_cc(0);
				String kode_produk=Integer.toString(datausulan.getLsbs_id());
				nama_produk="produk_asuransi.n_prod_"+kode_produk;
				aClass = Class.forName(nama_produk);
				produk = (n_prod)aClass.newInstance();
				SqlMapClientDaoSupport uwDao;
				//produk.setSqlMap(uwDao.getSqlMapClient());
				produk.ii_pmode=datausulan.getLscb_id();
				produk.ii_bisnis_id=datausulan.getLsbs_id();
				produk.ii_bisnis_no=datausulan.getLsdbs_number();
				produk.is_kurs_id=datausulan.getKurs_premi();				
				produk.of_set_kurs(produk.is_kurs_id);
				produk.ii_age=tertanggung.getMste_age();
				produk.of_set_usia_tt(tertanggung.getMste_age());
				datausulan.setLi_umur_pp(null);	//set umur pemegang
				//produk.of_set_usia_pp(null);
				datausulan.setLi_umur_ttg(tertanggung.getMste_age());//set umur ttg	
				
				//set begdate
				datausulan.setMspr_beg_date(agencyExcel.getMsp_pas_beg_date());	
				tahun = Integer.parseInt(sdfYear.format(datausulan.getMspr_beg_date()));
				bulan = Integer.parseInt(sdfMonth.format(datausulan.getMspr_beg_date()));
				tanggal = Integer.parseInt(sdfDay.format(datausulan.getMspr_beg_date()));	
				produk.of_set_begdate(tahun,bulan,tanggal);
				
				//set end date
				tanggalEd= new Integer(produk.idt_end_date.getTime().getDate());
				bulanEd= new Integer(produk.idt_end_date.getTime().getMonth()+1);
				tahunEd= new Integer(produk.idt_end_date.getTime().getYear()+1900);			
				String tgl_end=Integer.toString(tanggalEd.intValue());
				String bln_end= Integer.toString(bulanEd.intValue());
				String thn_end = Integer.toString(tahunEd.intValue());			
				if ((tgl_end.equalsIgnoreCase("0")==true) || (bln_end.equalsIgnoreCase("0")==true) || (thn_end.equalsIgnoreCase("0")==true))
				{
					datausulan.setMste_end_date(null);
				}else{
					String tanggal_end_date = FormatString.rpad("0",tgl_end,2)+"/"+FormatString.rpad("0",bln_end,2)+"/"+thn_end;				
					datausulan.setMspr_end_date(defaultDateFormat.parse(tanggal_end_date));				
				}
				
				Date end_date = (Date) agencyExcel.getMsp_pas_beg_date().clone();
				end_date.setYear(end_date.getYear()+5);
				end_date.setDate(end_date.getDate()-1);
				agencyExcel.setMsp_pas_end_date(end_date);
								
				
				//end of set end date
				Calendar cal = Calendar.getInstance();
				//set payperiod , insperiod
				cal.setTime(datausulan.getMspr_end_date());
				cal.add(Calendar.MONTH, -1);
				cal.add(Calendar.DATE, 1);
				Date end_pay=cal.getTime();						
				datausulan.setMspr_end_pay(end_pay);
				
				//payperiod=produk.of_get_payperiod();
				insperiod=produk.ii_contract_period;
				datausulan.setMspo_pay_period(payperiod);
				datausulan.setMspo_ins_period(insperiod);
				//end of set payperiod , insperiod
				
				//set premi, up
				Double premi_pokok = 0.;
				Double premi_berkala = 0.;
				Double premi_tunggal = 0.;
				Double total_premi = 0.;
				premi_pokok = Double.parseDouble(agencyExcel.getMsp_premi());
				premi_berkala = Double.parseDouble("0");
				premi_tunggal = Double.parseDouble("0");
			    total_premi = premi_pokok+premi_berkala;
				up = Double.parseDouble(agencyExcel.getMsp_up());
				
				excelList.setCurrentUser(user);
				//Set Rate
		      //  produk.of_hit_premi();
			    Double rate = 0.0;
			    rate = produk.idec_rate;
			    datausulan.setMspr_rate(rate.intValue());	
				
			    datausulan.setMspr_premium(new Double(agencyExcel.getMsp_premi()));
			    datausulan.setMspr_tsi(up);
				datausulan.setTotal_premi_kombinasi(total_premi);
				datausulan.setFlag_worksite(0);
				datausulan.setJenis_pemegang_polis(0);				
				datausulan.setMste_beg_date(agencyExcel.getMsp_pas_beg_date());
				datausulan.setMspo_spaj_date(agencyExcel.getMsp_pas_beg_date());
				datausulan.setIsBungaSimponi(0);				
				datausulan.setIsBonusTahapan(0);
				datausulan.setMste_gmit(0);
				datausulan.setMste_medical(0);
				datausulan.setFlag_account(2);				
				datausulan.setFlag_rider(0);
				datausulan.setMspr_class(0);
				datausulan.setKurs_p("01");
				datausulan.setLku_id("01");
				datausulan.setRate_plan(new Double(0));
				datausulan.setMspr_ins_period(5);
				datausulan.setMspr_discount(new Double(0));
				datausulan.setMste_end_date(agencyExcel.getMsp_pas_end_date());
				datausulan.setFlag_jenis_plan(0);
				datausulan.setFlag_as(0);				
				datausulan.setDaftarplus_mix(new ArrayList()); 
				//flag_bulanan PowerSave
				datausulan.setKode_flag(1);
				datausulan.setFlag_bulanan(0);
				// PEMEGANG.FLAG_SPAJ = 5 = GIO(TIDAK PERLU KUESIONER DATA KESEHATAN)
				datausulan.setMspo_flag_spaj(0);
				datausulan.setMspo_provider(2);
				datausulan.setJmlrider(new Integer(2));
				datausulan.setLscb_id_rider(0);
				
				//RIDER MEDICAL(+) RI/RJ
				//proses Rider !
				// eka.lst_rider lsbs_id=846 and lsr_jenis=
				List<Datarider> dtrd = new ArrayList<Datarider>();
				Datarider dtr=new Datarider();
				dtr.setMspr_beg_date(agencyExcel.getMsp_pas_beg_date());		
				dtr.setMspr_end_date(end_date);	
				dtr.setMspr_end_pay(end_pay);	
				dtr.setLsbs_id(846);
				if(agencyExcel.getMspo_plan_provider().trim().equalsIgnoreCase("RI-300")){
					dtr.setLsdbs_number(31);			
					dtr.setMspr_tsi((double) 300000);
					dtr.setMspr_premium((double) 3757100);
				}else if(agencyExcel.getMspo_plan_provider().trim().equalsIgnoreCase("RI-400")){
					dtr.setLsdbs_number(32);
					dtr.setMspr_tsi((double) 400000);
					dtr.setMspr_premium((double) 5169300);
				}else if(agencyExcel.getMspo_plan_provider().trim().equalsIgnoreCase("RI-500")){
					dtr.setLsdbs_number(33);
					dtr.setMspr_tsi((double) 500000);
					dtr.setMspr_premium((double) 7600500);
				}
				dtr.setMspr_ins_period(5);	
								
				String planRider = "846~X"+String.valueOf(dtr.getLsdbs_number()); 
				dtr.setPlan_rider(planRider);
				dtr.setPlan_rider1("846");
				dtr.setLku_id("01");
				dtrd.add(dtr);
				
				// eka.lst_rider lsbs_id=847 and lsr_jenis=
				dtr=new Datarider();
				dtr.setMspr_beg_date(agencyExcel.getMsp_pas_beg_date());		
				dtr.setMspr_end_date(end_date);	
				dtr.setMspr_end_pay(end_pay);	
				dtr.setLsbs_id(847);
				if(agencyExcel.getMspo_plan_provider().trim().equalsIgnoreCase("RI-300")){
					dtr.setLsdbs_number(121);			
					dtr.setMspr_tsi((double) 45000);
					dtr.setMspr_premium((double) 1309400);
					
				}else if(agencyExcel.getMspo_plan_provider().trim().equalsIgnoreCase("RI-400")){
					dtr.setLsdbs_number(122);
					dtr.setMspr_tsi((double) 46000);
					dtr.setMspr_premium((double) 1309400);
				}else if(agencyExcel.getMspo_plan_provider().trim().equalsIgnoreCase("RI-500")){
					dtr.setLsdbs_number(123);
					dtr.setMspr_tsi((double) 75000);
					dtr.setMspr_premium((double) 2482500);
				}
				dtr.setMspr_ins_period(5);	
								
			    planRider = "847~X"+String.valueOf(dtr.getLsdbs_number()); 
				dtr.setPlan_rider(planRider);
				dtr.setPlan_rider1("847");
				dtr.setLku_id("01");
				dtrd.add(dtr);
				datausulan.setDaftaRider(dtrd);		
				//end proses Rider !
				
						
				
				
				excelList.setPemegang(pemegang);
				excelList.setTertanggung(tertanggung);
				excelList.setDatausulan(datausulan);
				excelList.setPersonal(personal);
				
				excelList.setCurrentUser(user);
				excelList.setInvestasiutama(investasiUtama);
				
				//{STRREGION=00, STRAGENTBRANCH=0941, STRBRANCH=09, STRWILAYAH=41}
				agen.setMsag_id("528150");
				agen.setKode_regional("013900");
				agen.setMcl_first("");
				agen.setLca_id("01");
				agen.setLwk_id("39");
				agen.setLsrg_id("00");
				
				ContactPerson cp =new ContactPerson();
				cp.setNama_lengkap(tertanggung.getMcl_first());

				excelList.setContactPerson(cp);
				
				
				excelList.setAgen(agen);
				
				pembayarPremi.setPendapatanBulan(dataSumberDana);
				pembayarPremi.setPendapatanTahun(dataSumberDana);
				pembayarPremi.setTujuanInvestasi(dataSumberDana);
				pembayarPremi.setLsre_id_premi("40");
				pembayarPremi.setMkl_kerja_other_radio("0");
				
				excelList.setPembayarPremi(pembayarPremi);
			
				addressBilling.setRegion("020023");
				addressBilling.setKota_tgh("JAKARTA");
				addressBilling.setMsap_address("-");
				addressBilling.setFlag_cc(datausulan.getMste_flag_cc());
				
				excelList.getTertanggung().setMste_age(agencyExcel.getMsp_age());
				excelList.setAddressbilling(addressBilling);
				excelList.setRekening_client(rekCLient);
				excelList.setFlag_special_case("0");
						
			    Integer jumlahBenef=0;
			    if(!agencyExcel.getMsaw_first1().toString().equals(""))jumlahBenef+=1;
			    if(!agencyExcel.getMsaw_first2().toString().equals(""))jumlahBenef+=1;
			    if(!agencyExcel.getMsaw_first3().toString().equals(""))jumlahBenef+=1;
			    if(!agencyExcel.getMsaw_first4().toString().equals(""))jumlahBenef+=1;
				ArrayList<Benefeciary> benef = new ArrayList<Benefeciary>();
			    datausulan.setJml_benef(jumlahBenef);			   
			    if(jumlahBenef>0){		
					benef=Common.serializableList(prosesBenefeciary(agencyExcel,1,jumlahBenef));
			    }			   			 
			    excelList.getDatausulan().setDaftabenef(benef);			    
			    
			   excelList.setPowersave(new Powersave());
				
			   powersave.setReg_spaj("");
			   
			   		Integer flag_ps = bacManager.selectFlagPowersave(datausulan.getLsbs_id(),datausulan.getLsdbs_number() , 0);
					Date tgl_mature = new Date();
					powersave.setMps_kode(5);
					powersave.setMps_deposit_date(agencyExcel.getMsp_pas_beg_date());
					 //eka.mst_powersave_ro
					powersave.setMps_roll_over(Integer.parseInt("1"));
					powersave.setMpr_jns_ro(Integer.parseInt("1"));
					powersave.setMps_jangka_inv("12");
					powersave.setMsl_mgi(12);
					tgl_mature = FormatDate.add(powersave.getMps_deposit_date(), Calendar.MONTH, Integer.parseInt(powersave.getMps_jangka_inv()));
					tgl_mature = FormatDate.add(tgl_mature, Calendar.DAY_OF_MONTH, -1);
					
					powersave.setMpr_mature_date(tgl_mature);
					powersave.setMps_end_period(tgl_mature);
					powersave.setMps_batas_date(FormatDate.add(tgl_mature,Calendar.DAY_OF_MONTH,1));
					powersave.setMps_employee(0);
					powersave.setMps_jenis_plan(0); //Jenis Bunga (0 = normal, 1 = spesial)
					
					powersave.setMps_rate(new Double(7.0));
					powersave.setMps_rate_date(agencyExcel.getMsp_pas_beg_date());
					powersave.setMpr_tgl_rate(agencyExcel.getMsp_pas_beg_date());
					powersave.setMps_prm_deposit(new Double(agencyExcel.getMsp_premi()));
					powersave.setMpr_deposit(new Double(agencyExcel.getMsp_premi()));
					powersave.setMsen_endors_no(null);
					
					f_hit_umur umr = new f_hit_umur();
					int hari = umr.hari_powersave(
							powersave.getMps_deposit_date().getYear()+1900, powersave.getMps_deposit_date().getMonth()+1, 
							powersave.getMps_deposit_date().getDate(), tgl_mature.getYear()+1900, tgl_mature.getMonth()+1, tgl_mature.getDate());
					hari += 1;
					
					powersave.setMpr_hari(hari);					
					powersave.setMpr_jns_rumus(1);
					powersave.setMpr_note("");
					powersave.setMsl_spaj_lama("");					
					powersave.setMpr_nett_tax(0);
					
					powersave.setMpr_breakable(0);
					powersave.setLus_id(Integer.parseInt(user.getLus_id()));
					powersave.setFlag_rider(0);
					powersave.setFlag_bulanan(0);
					
					Double bunga = new Double(0);
					bunga = bacManager.hitungBunga(powersave.getFlag_bulanan(), Integer.parseInt(powersave.getMps_jangka_inv()),
							powersave.getMps_prm_deposit(), powersave.getMps_rate(), powersave.getMpr_hari(), flag_ps);
					powersave.setMps_prm_interest(bunga);
					powersave.setMpr_interest(bunga);
					powersave.setMpr_cara_bayar_rider(0);
					
				excelList.setPowersave(powersave);
					
			    
				excelList = bacManager.savingspajnew(excelList, null, "input", user);
				
				String spajSmartSave = excelList.getPemegang().getReg_spaj();
				//BacManager.ProsesAutoAccept()
				Map mapAutoAccept=new HashMap<String, Object>();				
				Pemegang pmg=elionsManager.selectpp(excelList.getPemegang().getReg_spaj());
				Tertanggung ttg =elionsManager.selectttg(excelList.getPemegang().getReg_spaj());
				mapAutoAccept = bacManager.ProsesAutoAccept(spajSmartSave, 1,pmg,ttg,user,request,elionsManager);									
								
				if(excelList.getPemegang().getReg_spaj() == null){		
					Map<String, Object> map = new HashMap<String, Object>();
					 map.put("sts", "FAILED");
					 map.put("msg", "FAILED (TRANSFER TO PAYMENT):: Nama Tertanggung= ("+excelList.getTertanggung().getMcl_first()  +") Proses TRF to PAYMENT Gagal, No.Polis tidak terbentuk!");							
					 map.put("noSpaj", null);
					 successMessageList.add(map);					
				}else{
					Map<String, Object> map = new HashMap<String, Object>();
					//request.setAttribute("successMessage","transfer sukses dengan SPAJ : "+edit.getPemegang().getReg_spaj()+" dan No. Polis : "+edit.getPemegang().getMspo_policy_no());					
					map.put("sts", "SUCCEED");					
					map.put("msg", "Transfer ke payment Sukses:: Nama Tertanggung= ("+ excelList.getTertanggung().getMcl_first() + ") SMARTSAVE & SMiLeMEDICAL(+) SPAJ: "+ spajSmartSave +" " );									
					map.put("noSpaj", excelList.getPemegang().getReg_spaj());
					successMessageList.add(map);					
				}
			    
			}catch (Exception e) {
				e.printStackTrace();
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}
						
			}
			request.setAttribute("successMessage",successMessageList);	
		}
		
		
				
		//Tombol "SEARCH" Viewer Upload 
		if(search != null || refresh != null){
			String cari_upload_produk = request.getParameter("cari_upload_produk");
			String tgl_upload = request.getParameter("tgl_upload");
			
			
			if(cari_upload_produk.equals("4")){
				cari_upload_produk = "PAS SYARIAH";
			}else if(cari_upload_produk.equals("5")){
				cari_upload_produk = "PAS_FREE";
			}else if(cari_upload_produk.equals("6")){
				cari_upload_produk = "SMART ACCIDENT CARE";
			}else if(cari_upload_produk.equals("10")){
				cari_upload_produk = "NISSAN PA";
			}else if(cari_upload_produk.equals("11")){
				cari_upload_produk = "NISSAN DBD";
			}
			
			List<Pas> pas = new ArrayList<Pas>();
			List<TmSales> pasFree = new ArrayList<TmSales>();
			
			 String noPage = "0";
	         String noRow = "10";
	         String totalPage = "0";
	         if(request.getParameter("cPage") == null || request.getParameter("cPage") == "" ){
             	noPage = "1";
             }else{
             	noPage = request.getParameter("cPage");
             	if(noPage == null || noPage == "" || noPage.equals("")){
             		noPage = "1";
             	}
             }
	         
	         Map param = new HashMap();
	         param.put("tgl_upload", tgl_upload);
	         param.put("cari_upload_produk", cari_upload_produk);	
	         param.put("noRow", noRow);	        	
	         
	         if(cari_upload_produk.equals("PAS SYARIAH")){
	        	 totalPage = bacManager.selectTotalAllUploadPasList(param);
	         }else if(cari_upload_produk.equals("PAS_FREE")){
	        	 totalPage = bacManager.selectTotalAllUploadPasFreeList(param);
	         }else if(cari_upload_produk.equals("SMART ACCIDENT CARE") || cari_upload_produk.equals("NISSAN PA") || cari_upload_produk.equals("NISSAN DBD")){
	        	 totalPage = bacManager.selectTotalAllUploadPasList(param);
	         }
             // HANDLE PAGE LESS OR PAGE OVER
             if(Integer.parseInt(noPage) < 1){
             	noPage = "1";
             }
             
             if(Integer.parseInt(noPage) > Integer.parseInt(totalPage)){
             	noPage = totalPage;
             }	
			
			Map params = new HashMap();
			params.put("tgl_upload", tgl_upload);
			params.put("cari_upload_produk", cari_upload_produk);
			params.put("noRow", noRow);
			params.put("noPage", noPage);
			
			if(cari_upload_produk.equals("PAS SYARIAH")){
				pas 	= bacManager.selectAllUploadPasList(params);
				request.setAttribute("uploadList", pas);
			}else if(cari_upload_produk.equals("PAS_FREE")){
				pasFree = bacManager.selectAllUploadPasFreeList(params);				
				request.setAttribute("uploadList2", pasFree);
			}else if(cari_upload_produk.equals("SMART ACCIDENT CARE") || cari_upload_produk.equals("NISSAN PA") || cari_upload_produk.equals("NISSAN DBD")){
				pas 	= bacManager.selectAllUploadPasList(params);
				request.setAttribute("uploadList", pas);
			}
			
			int currPage = Integer.parseInt(noPage); 
			request.setAttribute("currPage", noPage);
        	request.setAttribute("firstPage", 1 + "");
        	request.setAttribute("lastPage", totalPage);        	
        	request.setAttribute("nextPage", (currPage + 1) + "");
        	request.setAttribute("previousPage", (currPage - 1) + "");        
				
			if(cari_upload_produk.equals("PAS SYARIAH")){
				request.setAttribute("cari_upload_produk","4");
			}else if(cari_upload_produk.equals("PAS_FREE")){
				request.setAttribute("cari_upload_produk","5");
			}else if(cari_upload_produk.equals("SMART ACCIDENT CARE")){
				request.setAttribute("cari_upload_produk","6");
			}else if(cari_upload_produk.equals("NISSAN PA")){
				request.setAttribute("cari_upload_produk","10");
			}else if(cari_upload_produk.equals("NISSAN DBD")){
				request.setAttribute("cari_upload_produk","11");
			}
		}				
		
		request.setAttribute("successMessage",successMessageList);		
		return new ModelAndView("uw/pa_partner_upload", cmd);
    }
		

	/**
	 * copy file
	 * @param srFile
	 * @param dtFile
	 * Filename : FileUtil.java
	 * Create By : Bertho Rafitya Iwasurya
	 * Date Created : Jun 1, 2010 3:07:59 PM
	 */
	public static void copyfile(String srFile, String dtFile){
		InputStream in = null;
		OutputStream out = null;
	    try{
	      File f1 = new File(srFile);
	      File f2 = new File(dtFile);
	      in = new FileInputStream(f1);
	      
	      //For Append the file.
//	      OutputStream out = new FileOutputStream(f2,true);

	      //For Overwrite the file.
	      out = new FileOutputStream(f2);

	      byte[] buf = new byte[1024];
	      int len;
	      while ((len = in.read(buf)) > 0){
	        out.write(buf, 0, len);
	      }
	      in.close();
	      out.close();
	      logger.info("File copied.");
	    }
	    catch(FileNotFoundException ex){
	      logger.info(ex.getMessage() + " in the specified directory.");
	    }
	    catch(IOException e){
	      logger.error("ERROR :", e);      
	    }finally {
	    	try {
	    		if(in != null) {
	    			in.close();
	    		}
	    		if(out != null) {
	    			out.close();
	    		}
	    	}catch (Exception e) {
				logger.error("ERROR :", e);
			}
	    }
	  }
	
	public static boolean deleteFile(String destDir, String fileName, HttpServletResponse response) throws FileNotFoundException,
		IOException {
	File file = new File(destDir + "/" + fileName);
	if (file.exists()) return file.delete();
	return false;
	}
		
	
	/**
	 * UNTUK REGISTRASI PRODUK FREE PA AJS DMTM
	 * FORMAT :
	 * PAKOMPAS#nomor KTP Pembeli (tanpa spasi, tanpa koma, tanpa titik)#Nama Peserta (sesuai KTP)#tanggal lahir (ddmmyyyy)#Jenis Kelamin#Email
	 * PABISNIS#nomor KTP Pembeli (tanpa spasi, tanpa koma, tanpa titik)#Nama Peserta (sesuai KTP)#tanggal lahir (ddmmyyyy)#Jenis Kelamin#Email
	 * Contoh:
	 * PAKOMPAS#09117758917811#Albert Eistain#17/03/1990#L#albert@xx.com
	 * PABISNIS#09117758917811#Albert Eistain#17/03/1990#L#albert@xx.com
	 * @param con
	 * @param msg
	 * @param idnext
	 * @return result
	 * 0= tidak di proses; 20=STATUS APRPOVE (PAS OK); 21 = STATUS APPROVE (PAS REJECT);
	 * @throws TimeoutException
	 * @throws GatewayException
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 * @throws ParseException 
	 * @deprecated Pindah ke BacManager.processRegFreePaDmtm
	 */
	public List<Map> regFreePaDmtm(List<TmSales> tmSaless, String tanggalAwal)
	{
		List<Map> successMessageList = new ArrayList<Map>();
		Integer result=0;
		String err="";
		List<TmSales> tmSalesListPDF = new ArrayList<TmSales>();
		String txtReply="test masuk";
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat df2 = new SimpleDateFormat("ddMMyyyy");
		
		TmSales tmSales = new TmSales();
		String application_id = "PAS_FREE";
////		
		for(TmSales pasx : tmSaless){
			try{
				PreparedStatement cmd=null;
				ResultSet rs=null;
				
				String name	=	pasx.getHolder_name();
				Integer jnskelamin	=	pasx.getSex();
				String sex	 = jnskelamin.toString();
				String identity	=	pasx.getCard_no();
														
				Date tglLahir	=	pasx.getBod_holder();				
				String dobStr	 = 	df.format(tglLahir);
				try{
					Date date = df.parse(dobStr);
				}
				catch (Exception e) {
					logger.error("ERROR :", e);						
				}										
				String email=pasx.getEmail();
				String nohp = pasx.getMobile_no();
				
				//validasi param
				//validasi length maximal
				boolean isBig=false;
				if(identity.length()>50){
					isBig=true;
				}else if(name.length()>150){
					isBig=true;
				}else if(sex.length()>1){
					isBig=true;
				}
				
				if(!isBig){
					String jenis_pas = "PAS-FREE";
					if("060".equals(pasx.getProduct_code())) {
					    jenis_pas = "FREE-PA-BANK-DKI";
					} else if("061".equals(pasx.getProduct_code())) {
					    jenis_pas = "FREE-DBD-BANK-DKI";
					}
				    
				    //format tanggal
					Date dob=null;
					try {
						// dob=new SimpleDateFormat("ddMMyyyy").parse(dobStr);
						// Date begdate=new SimpleDateFormat("dd/MM/yyyy").parse(tanggalAwal);							 
						//Date enddate=FormatDate.add(FormatDate.add(begdate, Calendar.MONTH, 12),Calendar.DATE,-1);
						Date begdate = new SimpleDateFormat("dd/MM/yyyy").parse("20/12/2014");	
						Date enddate = new SimpleDateFormat("dd/MM/yyyy").parse("19/12/2015");
						
						// Set begdate & enddate produk bank dki
						if("060,061".indexOf(pasx.getProduct_code()) > -1) {
//						    begdate = bacManager.selectSysdate();
						    begdate = new SimpleDateFormat("dd/MM/yyyy").parse(tanggalAwal);
						    Calendar temp = Calendar.getInstance();
						    temp.setTime(begdate);
						    temp.add(Calendar.MONTH, 6);// 6 bln
                            temp.add(Calendar.DATE, -1);// -1 hari
						    
						    enddate = temp.getTime();
						}
						 
						Integer umurPp = 0;								
						Integer umurBlnTt = 0;								
						f_hit_umur umr = new f_hit_umur();
								
						SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
						SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
						SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
						Date sysdate = elionsManager.selectSysdate();
						int tahun2 = Integer.parseInt(sdfYear.format(sysdate));
						int bulan2 = Integer.parseInt(sdfMonth.format(sysdate));
						int tanggal2 = Integer.parseInt(sdfDay.format(sysdate));
								
						if(pasx.getBod_holder() != null){		
							int tahun1 = Integer.parseInt(sdfYear.format(pasx.getBod_holder()));
							int bulan1 = Integer.parseInt(sdfMonth.format(pasx.getBod_holder()));
							int tanggal1 = Integer.parseInt(sdfDay.format(pasx.getBod_holder()));										
							umurPp=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
						}					
						
						if(umurPp>=1 && umurPp<=65){
								String campaign = "PARISKAINCDAY";
								
								if("060".equals(pasx.getProduct_code())) {
								    campaign = "FREEPABANKDKI";
								} else if("061".equals(pasx.getProduct_code())) {
								    campaign = "FREEDBDBANKDKI";
								}
								
								tmSales = new TmSales();
											
								tmSales.setTgl_sales(sysdate);
								tmSales.setCampaign(campaign);
								tmSales.setCard_type("00");
								tmSales.setCard_no("1234567890123456");
								tmSales.setCust_no(identity);
								tmSales.setHolder_name(name);
								tmSales.setSex(jnskelamin);
								tmSales.setBod_holder(pasx.getBod_holder());
								tmSales.setAge(umurPp);
								tmSales.setMobile_no(pasx.getMobile_no());
//								tmSales.setProduct_code("056");
								
								if(!Common.isEmpty(pasx.getProduct_code())) {
								    tmSales.setProduct_code(pasx.getProduct_code());
								} else {
								    tmSales.setProduct_code("056");
								}
								
								tmSales.setPremium(new BigDecimal(0));
//								tmSales.setSum_insured(new BigDecimal(50000000));
								
								if(Common.isEmpty(pasx.getSum_insured())) {
								    tmSales.setSum_insured(new BigDecimal(50000000));
								}
								
								tmSales.setBeg_date(begdate);
								tmSales.setEnd_date(enddate);
								tmSales.setBill_mode("2");
								tmSales.setBill_freq(0);
								tmSales.setCall_date(null);
								tmSales.setTgl_input(sysdate);
								tmSales.setFlag_cek(1);
								tmSales.setPosisi(99);
								tmSales.setStatus(10);
								tmSales.setEmail(email);
								tmSales.setNo_sertifikat(sertifikatTmSales(tmSales));
								
								if("060".equals(tmSales.getProduct_code())) {
								    application_id = "FREEPA_BANK_DKI";
								} else if("061".equals(tmSales.getProduct_code())) {
								    application_id = "FREEDBD_BANK_DKI";
								}
								
								tmSales.setApplication_id(application_id);
								
								if(!Common.isEmpty(pasx.getNo_akun())) tmSales.setNo_akun(pasx.getNo_akun());
                                if(!Common.isEmpty(pasx.getTipe_akun())) tmSales.setTipe_akun(pasx.getTipe_akun());
                                if(!Common.isEmpty(pasx.getId_customer())) tmSales.setId_customer(pasx.getId_customer());
                                if(!Common.isEmpty(pasx.getTgl_buka_akun())) tmSales.setTgl_buka_akun(pasx.getTgl_buka_akun());
                                if(!Common.isEmpty(pasx.getSts_nikah())) tmSales.setSts_nikah(pasx.getSts_nikah());
                                if(!Common.isEmpty(pasx.getKd_cabang())) tmSales.setKd_cabang(pasx.getKd_cabang());
                                if(!Common.isEmpty(pasx.getPekerjaan())) tmSales.setPekerjaan(pasx.getPekerjaan());
                                if(!Common.isEmpty(pasx.getTempat_lahir())) tmSales.setTempat_lahir(pasx.getTempat_lahir());
                                if(!Common.isEmpty(pasx.getAddress1())) tmSales.setAddress1(pasx.getAddress1());
                                if(!Common.isEmpty(pasx.getAddress2())) tmSales.setAddress2(pasx.getAddress2());
                                if(!Common.isEmpty(pasx.getHome_phone())) tmSales.setHome_phone(pasx.getHome_phone());
                                if(!Common.isEmpty(pasx.getWork_phone())) tmSales.setWork_phone(pasx.getWork_phone());
																						
								result=20;//berhasil												
								//proses utama
								//insertTmSales(tmSales, con);
								bacManager.insertTmSales(tmSales);	
								tmSalesListPDF.add(tmSales);
											/*
												//String s_urlid = selectSeqUrlSecureId(con);
												String s_urlid =  bacManager.selectSeqUrlSecureId();
												
												//insertMstUrlSecure(con, s_urlid, "regFreePaDmtm", identity, "campaign="+campaign+"~cust="+identity+"~mobile="+originator+"~sertifikat="+tmSales.getNo_sertifikat());
												String id_encrypt_1 = elionsManager.selectEncryptDecrypt(s_urlid, "e");
												String id_encrypt_2 = elionsManager.selectEncryptDecrypt("regFreePaDmtm", "e");
												bacManager.insertMstUrlSecure(s_urlid, "regFreePaDmtm", identity, "campaign="+campaign+"~cust="+identity+"~mobile="+tmSales.getMobile_no()+"~sertifikat="+tmSales.getNo_sertifikat(), id_encrypt_1, id_encrypt_2);
												
												//List<Map> lsSecure=db.querySelect("select ID_ENCRYPT_1, ID_ENCRYPT_2 from eka.mst_url_secure where ID_SECURE_1='"+s_urlid+"'");
												//String id_encrypt_1 = (String) lsSecure.get(0).get("ID_ENCRYPT_1");
												//String id_encrypt_2 = (String) lsSecure.get(0).get("ID_ENCRYPT_2");
																								
//												getServer().emailin_nasabah(true, getMainProperty("email.admin"), tmSales.getEmail().split(";"), null, new String[]{"andy@sinarmasmsiglife.co.id"}, "Komfirmasi Aktivasi Asuransi Kecelakaan Diri dari Sinarmas MSIG Life", "Dear Nasabah, \nSelamat! Anda mendapatkan Asuransi Kecelakaan Diri senilai Rp 50juta dari Sinarmas MSIG Life. \nAktivasi paling lambat 25 Okt 2014. Salam SMiLe \n\n"+"http://ews.sinarmasmsiglife.co.id/freepa/"+id_encrypt_1+"/"+id_encrypt_2, null);
																								
												EmailPool.send("SMiLe SMS Service", 0, 0, 0, 0, 
									    				null, 0, 0, sysdate, null, 
									    				true,
									    				"info@sinarmasmsiglife.co.id",									    				
									    				new String[]{email}, 
									    				null, new String[]{"andy@sinarmasmsiglife.co.id","adrian_n@sinarmasmsiglife.co.id"}, 
									    				"Aktivasi Polis SMiLe Personal Accident", 
									    				"Nasabah Terhormat,<br><br>"+
																"Anda telah terdaftar sebagai pemegang polis asuransi Kecelakaan Diri (SMiLe Personal Accident) senilai Rp. 50 juta dari Sinarmas MSIG Life.<br>"+
																"Segera lindungi diri Anda melalui manfaat asuransi yang dibutuhkan dengan melakukan aktivasi polis.<br>"+
																"Untuk mengaktifkannya, klik link <a href='http://ews.sinarmasmsiglife.co.id/api/activate/freepa/"+id_encrypt_1+"/"+id_encrypt_2+"' >disini</a>  !! <br><br>"+
																"Terima kasih.<br><br>"+
																"Salam Hangat,<br>"+
																"Sinarmas MSIG Life" 
									    				, null, null);*/												
											//==========
									 }	
					} catch (Exception e) {				// TODO Auto-generated catch block
    					err = "error";
    					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();					
    					Map<String, Object> map = new HashMap<String, Object>();
    					map.put("sts", "FAILED");
    					map.put("msg", "FAILED (CETAK POLIS " + jenis_pas + ") : Proses Cetak Polis Di-Batalkan, karena ada Error!!  Mohon cek kembali Validitas Data!");								
    					successMessageList.add(map);				
    					
    					logger.error("ERROR :", e);
    					StringWriter sw = new StringWriter();
    					PrintWriter pw = new PrintWriter(sw);
    					e.printStackTrace(pw);
    					String error=sw.toString();
    					try {
    						sw.close();
    						pw.close();
    					} catch (IOException e1) {
    						logger.error("ERROR :", e1);
    					}
        				//	getServer().emailin(true, getMainProperty("email.admin"), getMainProperty("email.send_error_to").split(";"), null, new String[]{"andy@sinarmasmsiglife.co.id"}, "SMSServer PROSES SMS FREE PA Error", "Dear Admin, \n Terjadi kesalahan pada aplikasi saat melakukan proses registrasi. \nBerikut detailnya : \n\nNO HP : "+originator+"\nISI SMS : "+txt+"\nError : \n"+error, null);
        				//	pasSMS.setMsp_kode_sts_sms("09");
        	           //	pasSMS.setMsp_desc_sts_sms("WRONG FORMAT");
    				}		
				}		
    //		OutboundMessage msgOut=new OutboundMessage(originator,txtReply);
    //		getService().sendMessage(msgOut);
    //		pasSMS.setId_ref(idnext);
    //		pasSMS.setMsp_text(txt);
    //		pasSMS.setDist("01");
    //		pasSMS.setMsp_mobile(originator);
    //		pasSMS.setMsp_message_date(msg.getDate());
    //		pasSMS.setMsp_receive_date(new Date());
    //		pasSMS.setLspd_id(1);
    //		pasSMS.setLssp_id(1);
    //		pasSMS.setJenis_pas(jenis);
    		} catch (Exception e) {			// TODO Auto-generated catch block
    			err = "error";
    			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
    			Map<String, Object> map = new HashMap<String, Object>();
    			map.put("sts", "FAILED");
    			map.put("msg", "FAILED (CETAK POLIS " + jenis_pas + ") : Proses Cetak Polis Di-Batalkan, karena ada Error!!  Mohon cek kembali Validitas Data!");								
    			successMessageList.add(map);
    			
    			logger.error("ERROR :", e);
    			StringWriter sw = new StringWriter();
    			PrintWriter pw = new PrintWriter(sw);
    			e.printStackTrace(pw);
    			String error=sw.toString();
    			try {
    				sw.close();
    				pw.close();
    			} catch (IOException e1) {
    				logger.error("ERROR :", e1);
    			}
    //			insertSMSOut(originator, "error : "+error,idnext, db,msg.getGatewayId());
    //			getServer().emailin(true, getMainProperty("email.admin"), getMainProperty("email.send_error_to").split(";"), null, new String[]{"andy@sinarmasmsiglife.co.id"}, "SMSServer PROSES SMS FREE PA Error", "Dear Admin, \n Terjadi kesalahan pada aplikasi saat melakukan proses registrasi. \nBerikut detailnya : \n\nNO HP : "+originator+"\nISI SMS : "+txt+"\nError : \n"+error, null);
    //			txtReply=getMainProperty("sms.freepa.reply.reg.wrong_format");
    			result=29;
    //			pasSMS.setMsp_kode_sts_sms("09");
    //			pasSMS.setMsp_desc_sts_sms("WRONG FORMAT");
    		}		
		//db.comit();	
    	}
		
    	if("".equals(err)){
    		for(TmSales pasx : tmSalesListPDF){	
    			String no_sertifikat = pasx.getNo_sertifikat();
    	    	String[] no_sertifikat_arr = no_sertifikat.split("-");
    	    	String no_polis_induk = no_sertifikat_arr[0] + "-" + no_sertifikat_arr[1] + "-" + no_sertifikat_arr[2];
    	    	String nama_pp = "CMC Pasar Keuangan Rakyat OJK 2014";
    	    	String nama_tt = pasx.getHolder_name();
    	    	Date tgl_lahir = pasx.getBod_holder();
    	    	Date sysdate = elionsManager.selectSysdate();
    	    	String email = pasx.getEmail();
    	    	Date beg_date = pasx.getBeg_date();
    	    	Date end_date = pasx.getEnd_date();	    	
    	    
    	    	try {
    					if("060".equals(pasx.getProduct_code())) {
    					    nama_pp = "Bank DKI";
    					    String upText = "Rp. 25.000.000,- (dua puluh lima juta rupiah)";
    					    String insperText = FormatDate.toIndonesian(pasx.getBeg_date()) + " s/d " + FormatDate.toIndonesian(pasx.getEnd_date());
    					    ITextPdf.generateSertifikatFreePaDmtm(no_sertifikat, no_polis_induk, nama_pp, nama_tt, tgl_lahir, sysdate, pasx.getProduct_code(), upText, insperText);
    					} else if("061".equals(pasx.getProduct_code())) {
                            nama_pp = "Bank DKI";
    					    String upText = "Rp. 1.000.000,- (satu juta rupiah)";
    					    String insperText = FormatDate.toIndonesian(pasx.getBeg_date()) + " s/d " + FormatDate.toIndonesian(pasx.getEnd_date());
    					    ITextPdf.generateSertifikatFreeDbdDmtm(no_sertifikat, no_polis_induk, nama_pp, nama_tt, tgl_lahir, sysdate, pasx.getProduct_code(), upText, insperText);
    					} else {
    					    ITextPdf.generateSertifikatFreePaDmtm(no_sertifikat, no_polis_induk, nama_pp, nama_tt, tgl_lahir, sysdate);
    					}
    					
    					List<File> attachments = new ArrayList<File>();
    					String polisPath = "\\\\ebserver\\pdfind\\Polis";
//                        String polisPath = "\\\\ebserver\\pdfind\\Polis_Testing";
                        String docPath = "\\free_pa\\"+no_sertifikat+".pdf";
                        
                        if("060".equals(pasx.getProduct_code())) {
                            docPath = "\\free_pa\\060\\"+no_sertifikat+".pdf";
                        } else if ("061".equals(pasx.getProduct_code())) {
                            docPath = "\\free_dbd\\061\\"+no_sertifikat+".pdf";
                        }
                        
    	        		File file = new File(polisPath + docPath);
    	        		attachments.add( file );
    	        		
    	        		String emailSubject = "Polis SMiLe Personal Accident";
    	        		String emailMessage = "Nasabah Terhormat,<br><br>"+
                                "Terima kasih telah melakukan aktivasi polis dan memberikan kepercayaan Anda kepada kami melalui produk perlindungan kecelakaan diri SMiLe Personal Accident<br>"+
                                "Terlampir adalah sertifikat polis sebagai panduan Anda dalam memahami ketentuan produk secara ringkas.<br><br>"+
                                "Terima kasih.<br><br>"+
                                "Salam Hangat,<br>"+
                                "Sinarmas MSIG Life";
    	        		
    	        		if("060".equals(pasx.getProduct_code())) {
    	        		    emailMessage = "Nasabah Terhormat,<br><br>"+
                                    "Selamat, Anda telah terdaftar sebagai pemegang polis asuransi Kecelakaan Diri (SMiLe Personal Accident) dari Sinarmas MSIG Life.<br>"+
                                    "Terlampir adalah sertifikat polis sebagai panduan Anda dalam memahami ketentuan produk secara ringkas.<br><br>"+
                                    "Terima kasih.<br><br>"+
                                    "Salam Hangat,<br>"+
                                    "Sinarmas MSIG Life";
    	        		} else if("061".equals(pasx.getProduct_code())) {
    	        		    emailSubject = "Polis SMiLe Demam Berdarah";
    	        		    emailMessage = "Nasabah Terhormat,<br><br>"+
                                    "Selamat, Anda telah terdaftar sebagai pemegang polis asuransi Demam Berdarah dari Sinarmas MSIG Life.<br>"+
                                    "Terlampir adalah sertifikat polis sebagai panduan Anda dalam memahami ketentuan produk secara ringkas.<br><br>"+
                                    "Terima kasih.<br><br>"+
                                    "Salam Hangat,<br>"+
                                    "Sinarmas MSIG Life";
    	        		}
    	        		
    	        		String[] emailCc = new String[]{"ningrum@sinarmasmsiglife.co.id"};
    	        		String[] emailBcc = new String[]{"andy@sinarmasmsiglife.co.id;adrian_n@sinarmasmsiglife.co.id"};
    	        		
    	        		if("060,061".indexOf(pasx.getProduct_code()) > -1) {
    	        		    emailCc = new String[]{"ningrum@sinarmasmsiglife.co.id", "dwidharmap@bankdki.co.id", "dwidharmaprasetyo@gmail.com", "gpd@bankdki.co.id", "fiqadki@yahoo.com", "lylianty@sinarmasmsiglife.co.id", "yantisumirkan@sinarmasmsiglife.co.id"};
    	        		    emailBcc = new String[] {"daru@sinarmasmsiglife.co.id"};
    	        		}
    	        		
    	    			EmailPool.send(bacManager.selectSeqEmailId(),"SMiLe E-Lions", 0, 0, 0, 0, 
    							null, 0, 0, sysdate, null, true, "info@sinarmasmsiglife.co.id",												
    							new String[]{email}, 
//    							new String[]{"ningrum@sinarmasmsiglife.co.id"},
    							emailCc,
//    							new String[]{"andy@sinarmasmsiglife.co.id;adrian_n@sinarmasmsiglife.co.id"}, 
    							emailBcc,
    							emailSubject, 
    							emailMessage,
    							attachments,11);
    					
    			} catch (IOException e) {
    					// TODO Auto-generated catch block
    					logger.error("ERROR :", e);
    					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
    					Map<String, Object> map = new HashMap<String, Object>();
    					map.put("sts", "FAILED");
    					map.put("msg", "FAILED (CETAK POLIS " + jenis_pas + ") : Proses Cetak Polis Di-Batalkan, karena ada Error!!  Mohon cek kembali Validitas Data!");							
    					successMessageList.add(map);			
    			} catch (DocumentException e) {
    					// TODO Auto-generated catch block
    					logger.error("ERROR :", e);
    					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
    					Map<String, Object> map = new HashMap<String, Object>();
    					map.put("sts", "FAILED");
    					map.put("msg", "FAILED (CETAK POLIS " + jenis_pas + ") : Proses Cetak Polis Di-Batalkan, karena ada Error!!  Mohon cek kembali Validitas Data!");								
    					successMessageList.add(map);
    			}
    	    	Map<String, Object> map = new HashMap<String, Object>();
    			map.put("sts", "SUCCEED");
    			map.put("msg", "Sukses Cetak Polis " + jenis_pas + " dengan No.Sertifikat: "+pasx.getNo_sertifikat()+" ,Nama TTG: "+pasx.getHolder_name());							
    			successMessageList.add(map);
    		}
    			 
    	}else{
    		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("sts", "FAILED");
    		map.put("msg", "FAILED (CETAK POLIS " + jenis_pas + ") : Proses Cetak Polis Di-Batalkan, karena ada Error!!  Mohon cek kembali Validitas Data!");								
    		successMessageList.add(map);
    	}
    	
    	return successMessageList;
	}	
	
	public String sertifikatTmSales( TmSales tmSales){
		String id="";
		try{//select * from eka.lst_tm_product where product_code=#value#
		List<Map> lspin =  bacManager.selectLspin(tmSales.getProduct_code());
		//	List<Map> lspin=db.querySelect("select NOMOR, NOMOR2, NO_POLIS from eka.lst_tm_product@EB where product_code='"+tmSales.getProduct_code()+"'");
			Map tm=lspin.get(0);
			BigDecimal nomor=(BigDecimal) tm.get("NOMOR");
			BigDecimal nomor2=(BigDecimal) tm.get("NOMOR2");
			String no_polis=(String) tm.get("NO_POLIS");
				
			nomor=nomor.add(new BigDecimal(1));
			id=no_polis+"-"+FormatString.rpad("0", nomor.toString(), 6);
//				Map param=new HashMap<String, Object>();
//				param.put("nomor", nomor);
//				param.put("nomor2", nomor2);
//				param.put("product_code", tmms.getProduct_code());				
				// updateTmProduct(nomor, nomor2, tmSales.getProduct_code(), con);
				
			bacManager.updateTmProduct(nomor, nomor2, tmSales.getProduct_code());
		}catch (Exception e) {
//				 TODO: handle exception
			logger.error("ERROR :", e);
		}
		return id;
	}
	
	
	private String getMclId(Integer flag, Integer year, ElionsManager elionsManager , BacManager bacManager) {
		
		String mcl="";
		try {
		Long intIDCounter =	(Long) elionsManager.select_counter(194, "01");
		BigDecimal intIDCounter2=new BigDecimal(Long.parseLong(fmYearSpaj.toString().concat("000000"))+intIDCounter.longValue());
		if(flag==1)mcl ="PP"+intIDCounter2;
		if(flag==2)mcl ="TT"+intIDCounter2;
		if(flag==3)mcl ="PY"+intIDCounter2;		
		bacManager.update_counter(new Long(intIDCounter.longValue()+1).toString(), 194, "01");
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return mcl;
	}
	
	
	private List<Benefeciary> prosesBenefeciary(Pas spajExcelList, int i, Integer jumlahBenef) throws ParseException {			
			List<Benefeciary> benef2 = new ArrayList<Benefeciary>();
			
			SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
			f_replace konteks = new f_replace();
			for(int j=1;j<=jumlahBenef;j++){
				String msaw_first = null ,lsre_id = null, msaw_sex = null,msaw_persen = null;
				Date bod = null ;
				Benefeciary bf = new Benefeciary();
				Integer nobf = 0;
								
				switch (j) {
				case 1:
					msaw_first=spajExcelList.getMsaw_first1().toString();
					msaw_persen=spajExcelList.getMsaw_persen1().toString();
					lsre_id= spajExcelList.getLsre_relation1().toString();
					nobf=1;
					break;

				case 2:
					msaw_first=spajExcelList.getMsaw_first2().toString();
					msaw_persen=spajExcelList.getMsaw_persen2().toString();
					lsre_id= spajExcelList.getLsre_relation2().toString();
					nobf=2;
					break;
				case 3:
					msaw_first=spajExcelList.getMsaw_first3().toString();
					msaw_persen=spajExcelList.getMsaw_persen3().toString();
					lsre_id= spajExcelList.getLsre_relation3().toString();
					nobf=3;
					break;
					
				case 4:
					msaw_first=spajExcelList.getMsaw_first4().toString();
					msaw_persen=spajExcelList.getMsaw_persen4().toString();
					lsre_id= spajExcelList.getLsre_relation4().toString();
					nobf=4;
					break;
				}
				
				if (msaw_first==null){
					msaw_first="";
				}

				if (lsre_id==null){
					lsre_id="1";
				}
				
				if (msaw_persen.trim().length()==0){
					msaw_persen="0";
				}else{
					msaw_persen=konteks.f_replace_persen(msaw_persen);
					boolean cekk1 = f_validasi.f_validasi_numerik1(msaw_persen);
					if (cekk1 == false){
						msaw_persen="0";
					}
				}
				
				bf.setMsaw_first(msaw_first);
				//bf.setMsaw_birth(bod);
				bf.setLsre_id(Integer.parseInt(lsre_id));
				bf.setMsaw_persen(Double.parseDouble(msaw_persen));
				//bf.setMsaw_sex(Integer.parseInt(msaw_sex));
				bf.setMsaw_number(nobf);
				bf.setMste_insured_no(1);
				benef2.add(bf);
			}			
			return benef2;
	}
	
}