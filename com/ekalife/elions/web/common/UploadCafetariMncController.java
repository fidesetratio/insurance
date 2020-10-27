/**
 * 
 */
package com.ekalife.elions.web.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.ContactPerson;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.ExcelRead;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.MergePDF;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.parent.ParentController;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

/**
 * Upload XLS Data Pre_SPAJ (Surat Permintaan Asuransi Jiwa) Individu Worksite MNC 
 * - Proses PDFStamper untuk Remarks template file PDF di \\EBSERVER\pdfind\Template\CAFETARI_MNC
 * - Proses MergePDF.java dengan concatPDFs untuk menggabungkan file-file hasil PDFStamper 
 * - Output di \\ebserver\pdfind\PRE_SPAJ\CAFETARI_MNC\
 * 
 * @author Adrian Nathaniel
 * @since Mei 10, 2013 (10:25:31 AM)
 */
public class UploadCafetariMncController extends ParentController{
	//private static String mergeDirectory;


	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> cmd = new HashMap<String, Object>();
		String mergeDirectory = "";
		String err = "";
		String err1="";
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		cmd.put("user_id", lus_id);
	
		//===========
		
		String submit = request.getParameter("upload");
		List<Map> successMessageList = new ArrayList<Map>();
		List<Map> successMessageList2 = new ArrayList<Map>();
		List<String> pdfStamper = new ArrayList<String>();
		Date sysdate=null;
		
		if(submit != null){
			Upload upload = new Upload();
						
			String file_fp = request.getParameter("file_fp");
			upload.setDaftarFile(new ArrayList<MultipartFile>(10));
			ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
			binder.bind(request);
			MultipartFile mf = upload.getFile1();
			String fileName = mf.getOriginalFilename();
			String directory = props.getProperty("temp.dir.fileupload"); //file_fp.replaceAll(fileName, "");
									
			
			if(upload.getFile1() == null) {
				Map<String,String> map = new HashMap<String, String>();
				map.put("sts", "FAILED");
				map.put("msg", "Silahkan masukan file dengan tipe EXCEL (.xls)");
				successMessageList.add(map);
			}else if(upload.getFile1().isEmpty() || upload.getFile1().getOriginalFilename().toLowerCase().indexOf(".xls")==-1) {
				Map<String,String> map = new HashMap<String, String>();
				map.put("sts", "FAILED");
				map.put("msg", "Silahkan masukan file dengan tipe EXCEL (.xls)");	
				successMessageList.add(map);
			}else{				
			ExcelRead excelRead = new ExcelRead();
			ContactPerson agencyExcel = null;
			List<List> agencyExcelList = new ArrayList<List>();
			List<ContactPerson> CP = new ArrayList<ContactPerson>();
			
			try{
				String dest=directory + "/" + fileName ;
				File outputFile = new File(dest);
			    FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
				agencyExcelList = excelRead.readExcelFile(directory + "\\", fileName);
			}catch (Exception e) {
				Map<String,String> map = new HashMap<String, String>();
				map.put("sts", "FAILED");
				map.put("msg", "FAILED:: akses file EXCEL (.xls) gagal");
				successMessageList.add(map);
			}
			
			int jmlh_header = agencyExcelList.get(0).size();
			if(jmlh_header<10){
				Map<String,String> map = new HashMap<String, String>();
				err = "error";
				map.put("sts", "FAILED");
				map.put("msg", "FAILED:: Format Jumlah Kolom file EXCEL (.xls) tidak sesuai!");
				successMessageList.add(map);
			}else{
			  for(int i = 1 ; i < agencyExcelList.size() ; i++){
				Map<String,String> map = new HashMap<String, String>();			
				if(agencyExcelList.get(i) != null){				
				if(agencyExcelList.get(i).size()==10){		
					try{
						err1="";
						agencyExcel = new ContactPerson();
						SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						
						Calendar cal = Calendar.getInstance();
						
						if(agencyExcelList.get(i).get(0).toString().trim() != null && !agencyExcelList.get(i).get(0).toString().trim().equals("") ){
							try{
								agencyExcel.setNo_urut(i);
							}
							catch (Exception e) {							
								logger.error("ERROR :", e);
							}
						}
						
						agencyExcel.setNama_kantor(agencyExcelList.get(i).get(1).toString().trim());
						agencyExcel.setNama_lengkap(agencyExcelList.get(i).get(2).toString().trim());
						agencyExcel.setNo_identity(agencyExcelList.get(i).get(3).toString().trim());
						
						//ljb_note digunakan untuk inputan= Warga Negara
						String WN =  agencyExcelList.get(i).get(4).toString().trim();	
						if(WN.equalsIgnoreCase("Indonesia") || WN.equalsIgnoreCase("Indo") || WN.equalsIgnoreCase("WNI") )
						{   
							agencyExcel.setLjb_note("Indonesia");
						}else{
							agencyExcel.setLjb_note(WN);		
						}
						
						//Setting TanggalLahir_PP & Hitung Usia_PP												
						try{							
							Date date_birth = df.parse(agencyExcelList.get(i).get(5).toString());					
							agencyExcel.setDate_birth(date_birth);
						}
						catch (Exception e) {
							agencyExcel.setDate_birth(null);
							logger.error("ERROR :", e);
						}		
						agencyExcel.setMste_age(null);
						if(agencyExcel.getDate_birth()!= null){
							try{
								f_hit_umur umr = new f_hit_umur();
							
								SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
								SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
								SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
								Date sysdate1 = elionsManager.selectSysdate();
								int tahun2 = Integer.parseInt(sdfYear.format(sysdate1));
								int bulan2 = Integer.parseInt(sdfMonth.format(sysdate1));
								int tanggal2 = Integer.parseInt(sdfDay.format(sysdate1));
																			
								int tahun1 = Integer.parseInt(sdfYear.format(agencyExcel.getDate_birth()));
								int bulan1 = Integer.parseInt(sdfMonth.format(agencyExcel.getDate_birth()));
								int tanggal1 = Integer.parseInt(sdfDay.format(agencyExcel.getDate_birth()));
								
								int umurPp=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);							
						
								agencyExcel.setMste_age(umurPp);
							}
							catch(Exception e){	
								agencyExcel.setMste_age(null);
								logger.error("ERROR :", e);
							}
						}
						
						agencyExcel.setPlace_birth(agencyExcelList.get(i).get(6).toString());
						
						String JK = agencyExcelList.get(i).get(7).toString().trim();				
						if(JK.equalsIgnoreCase("Pria") || JK.equalsIgnoreCase("Laki-laki") || JK.equalsIgnoreCase("Lakilaki"))
						{   agencyExcel.setMste_sex(1);
						} 
						if(JK.equalsIgnoreCase("Wanita") || JK.equalsIgnoreCase("Perempuan") )
						{   agencyExcel.setMste_sex(0);
						} 
						
						String stsMarried = agencyExcelList.get(i).get(8).toString().trim();
						if(!Common.isEmpty(stsMarried)){
							if(stsMarried.equalsIgnoreCase("Menikah") || stsMarried.equalsIgnoreCase("Kawin")){
								agencyExcel.setSts_mrt("Menikah");
							}
							else if(stsMarried.equalsIgnoreCase("Belum Menikah") || stsMarried.equalsIgnoreCase("Tidak Kawin") || stsMarried.equalsIgnoreCase("Tidak Menikah")){
								agencyExcel.setSts_mrt("Belum Menikah");
							}
							else agencyExcel.setSts_mrt("Janda/Duda");
						}
						agencyExcel.setAgama(agencyExcelList.get(i).get(9).toString().trim());
												
						CP.add(agencyExcel);						
						//================
						//validasi 						
						//err = "";
												
						if( agencyExcel.getNo_urut()== null) {
							err = "error";
							err1= "error";
							map.put("no", "** KOSONG **");
						}else{
							map.put("no", String.valueOf(agencyExcel.getNo_urut()));
						}
						if(agencyExcel.getNama_kantor().equals("") || agencyExcel.getNama_kantor()== null) {
							err = "error";
							err1= "error";
							map.put("nama_kantor", "** KOSONG **");
						}else{
							map.put("nama_kantor", agencyExcelList.get(i).get(1).toString().trim());
						}
						if(agencyExcel.getNama_lengkap().equals("") || agencyExcel.getNama_lengkap()== null) {
							err = "error";
							err1= "error";
							map.put("nama", "** KOSONG **");
						}else{
							map.put("nama", agencyExcelList.get(i).get(2).toString().trim());
						}																	
						if(agencyExcel.getNo_identity().equals("") || agencyExcel.getNo_identity()== null) {
							err = "error";
							err1= "error";
							map.put("nik", "** KOSONG **");
						}else{
							map.put("nik", agencyExcelList.get(i).get(3).toString().trim());
						}						
						if(agencyExcel.getLjb_note().equals("") || agencyExcel.getLjb_note()== null) {
							err = "error";
							err1= "error";
							map.put("warga_negara", "** KOSONG **");
						}else{
							map.put("warga_negara", agencyExcel.getLjb_note());
						}						
						if(agencyExcel.getDate_birth()== null) {
							err = "error";
							err1= "error";
							map.put("tgl_lahir", "** KOSONG **");
						}else{
							map.put("tgl_lahir",  df.format(agencyExcel.getDate_birth()));
						}						
						if(agencyExcel.getPlace_birth().equals("") || agencyExcel.getPlace_birth()== null) {
							err = "error";
							err1= "error";
							map.put("tempat_lahir", "** KOSONG **");
						}else{
							map.put("tempat_lahir", agencyExcelList.get(i).get(6).toString().trim());
						}						
						if(Common.isEmpty(agencyExcel.getMste_sex())){
							err = "error";
							err1= "error";
							map.put("sex", "** KOSONG **");
						}else{
							if(agencyExcel.getMste_sex()==1)map.put("sex", "Laki-Laki");
							else map.put("sex", "Perempuan");							
						}						
						if(agencyExcel.getSts_mrt().equals("") || agencyExcel.getSts_mrt()== null) {
							err = "error";
							err1= "error";
							map.put("sts_mrt", "** KOSONG **");
						}else{
							map.put("sts_mrt", agencyExcel.getSts_mrt());
						}						
						if(agencyExcel.getAgama().equals("") || agencyExcel.getAgama()== null) {
							err = "error";
							err1= "error";
							map.put("agama", "** KOSONG **");
						}else{
							map.put("agama", agencyExcel.getAgama());
						}						
						if(err1.equals("error"))map.put("ket", "** FORMAT KOLOM TIDAK SESUAI **");
						else map.put("ket", "**   OK   **");
						
						successMessageList2.add(map);					
					}catch (Exception e) {
						logger.error("ERROR :", e);
						err = "error";
						map.put("no", agencyExcelList.get(i).get(0).toString());
						map.put("nama_kantor", agencyExcelList.get(i).get(1).toString());
						map.put("nama", agencyExcelList.get(i).get(2).toString());		
						map.put("nik", agencyExcelList.get(i).get(3).toString());	
						map.put("warga_negara", agencyExcelList.get(i).get(4).toString());
						map.put("tgl_lahir", agencyExcelList.get(i).get(5).toString());
						map.put("tempat_lahir", agencyExcelList.get(i).get(6).toString());
						map.put("sex", agencyExcelList.get(i).get(7).toString());
						map.put("sts_mrt", agencyExcelList.get(i).get(8).toString());	
						map.put("agama", agencyExcelList.get(i).get(9).toString());		
						map.put("ket", "** ERROR (TRY): FORMAT KOLOM TIDAK SESUAI **");
						successMessageList2.add(map); }
				}else{
					err = "error";
					map.put("no", "** KOSONG **");				
					map.put("nama_kantor", "** KOSONG **");
					map.put("nama", "** KOSONG **");		
					map.put("nik", "** KOSONG **");	
					map.put("warga_negara", "** KOSONG **");
					map.put("tgl_lahir","** KOSONG **");
					map.put("tempat_lahir", "** KOSONG **");
					map.put("sex", "** KOSONG **");
					map.put("sts_mrt", "** KOSONG **");	
					map.put("agama", "** KOSONG **");		
					map.put("ket", "** ERROR: FORMAT KOLOM TIDAK SESUAI! **");
					successMessageList2.add(map); }	
				}else{
				err = "error";
				map.put("no", "** KOSONG **");
				map.put("nama_kantor", "** KOSONG **");
				map.put("nama", "** KOSONG **");		
				map.put("nik", "** KOSONG **");	
				map.put("warga_negara", "** KOSONG **");
				map.put("tgl_lahir","** KOSONG **");
				map.put("tempat_lahir", "** KOSONG **");
				map.put("sex", "** KOSONG **");
				map.put("sts_mrt", "** KOSONG **");	
				map.put("agama", "** KOSONG **");		
				map.put("ket", "** KOSONG **");
				successMessageList2.add(map); }
			}
			
			// Processing PDFStamper
			if( err.equals("") && CP.size()>0){			
				 sysdate = elionsManager.selectSysdateSimple();					
				 pdfStamper =processPdfStamper(CP,sysdate);
			}
		}
			if(!err.equals("")){
				Map<String,String> map = new HashMap<String, String>();
				map.put("sts", "FAILED");
				map.put("msg", "FAILED:: ERROR!!, Mohon file upload disesuaikan Format!");
				successMessageList.add(map);
			}else if(agencyExcelList.size()>1 && err.equals("") ){
				String destDirectory = props.getProperty("pdf.dir.export.cafmnc");
				SimpleDateFormat ym = new SimpleDateFormat("yyyyMM");
				String nameDir = ym.format(sysdate);
				mergeDirectory = destDirectory+"\\"+nameDir;				
				List<Map> listMap = new ArrayList<Map>();	
				
				for(int i = 0 ; i < pdfStamper.size() ; i++){
					Map<String,String> param = new HashMap<String, String>();				
					param.put("pdfStamper",pdfStamper.get(i));
					listMap.add(param);					
				}
				request.setAttribute("successMessage3",listMap);	
				
				Map<String,String> map = new HashMap<String, String>();
				map.put("sts", "SUCCEDD");
				map.put("msg", "File PDF disimpan di:  "+mergeDirectory);
				successMessageList.add(map);
			}
		}		
		}	
				
		if(request.getParameter("dl_cafmnc") != null){
			String fileName = "";
			String fileDir = props.getProperty("download.folder.template");
			fileName = "template_cafmnc.xls";
			FileUtils.downloadFile("attachment;", fileDir, fileName, response);
		}		
		if(request.getParameter("dl_pdf") != null){
			String fileName = "";
			String fileDir = mergeDirectory;
			fileName = request.getParameter("file_fp");
			FileUtils.downloadFile("attachment;", fileDir, fileName, response);
		}		
		
		request.setAttribute("successMessage",successMessageList);	
		request.setAttribute("successMessage2",successMessageList2);	
		return new ModelAndView("common/upload_cafetari_mnc", cmd);
	}

	
	public List<String> processPdfStamper(List<ContactPerson> cp, Date uploadTime) throws IOException, DocumentException{
		
	List<String> pdfStamper = new ArrayList<String>();
	SimpleDateFormat dd = new SimpleDateFormat("ddMMyyyy");
	SimpleDateFormat dw = new SimpleDateFormat("HHmmss");
	SimpleDateFormat ym = new SimpleDateFormat("yyyyMM");
	
	String destDirectory = props.getProperty("pdf.dir.export.cafmnc");
	//Cek Direktori Tujuan (Buat Dir jika tidak ada)
	String nameDir = ym.format(uploadTime);
	String mergeDirectory = destDirectory+"\\"+nameDir;
	destDirectory = destDirectory+"\\"+"SINGLE_SPAJ"+"\\"+nameDir;	
	String no_urut =null;
	
	File destDir = new File(destDirectory);
    if(!destDir.exists()) {	
    	destDir.mkdirs();	
    }
	
    File mergeDir= new File(mergeDirectory);
    if(!mergeDir.exists()) {
    	mergeDir.mkdirs();
    }
    
    List<String> pdfa = new ArrayList<String>();
    List<String> pdfb = new ArrayList<String>();
    List<String> pdfc = new ArrayList<String>();
    List<String> pdfd = new ArrayList<String>();
    List<String> pdfe = new ArrayList<String>();
    List<String> pdff = new ArrayList<String>();
    List<String> pdfg = new ArrayList<String>();
    List<String> pdfh = new ArrayList<String>();
    List<String> pdfi = new ArrayList<String>();
    List<String> pdfj = new ArrayList<String>();
    List<String> pdfk = new ArrayList<String>();
    List<String> pdfl = new ArrayList<String>();
    List<String> pdfm = new ArrayList<String>();
    List<String> pdfn = new ArrayList<String>();
    List<String> pdfo = new ArrayList<String>();
    List<String> pdfp = new ArrayList<String>();
    List<String> pdfq = new ArrayList<String>();
    List<String> pdfr = new ArrayList<String>();
    List<String> pdfs = new ArrayList<String>();
    List<String> pdft = new ArrayList<String>();
    List<String> pdfu = new ArrayList<String>();
    List<String> pdfv = new ArrayList<String>();
    List<String> pdfw = new ArrayList<String>();
    List<String> pdfx = new ArrayList<String>();
    List<String> pdfy = new ArrayList<String>();
    List<String> pdfz = new ArrayList<String>(); 
        
	for(int i = 0 ; i < cp.size() ; i++){
		ContactPerson CP = new ContactPerson();
		CP = cp.get(i);
		
		no_urut = hitCounter(i+1);
		
		String nama_kantor = CP.getNama_kantor();
		String nama_pp = CP.getNama_lengkap().toUpperCase().trim();
		String nik  = CP.getNo_identity();
		String warga_negara = CP.getLjb_note();
		Date tgl_lahir = CP.getDate_birth();
		int usia = CP.getMste_age();
		String tempat_lahir  = CP.getPlace_birth();
		int sex = CP.getMste_sex();
		String agama = CP.getAgama();
		String sts_mrt = CP.getSts_mrt();
		
		SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
		SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
		SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
		String day = sdfDay.format(tgl_lahir);
		String month = sdfMonth.format(tgl_lahir);
		String year = sdfYear.format(tgl_lahir);
		
		// Set pre nama_file, misalnya: A_ddMMyyyy_hhmmss_no_urut.pdf
		String nm_pre_file = nama_pp.substring(0, 1).trim();
		
		String cafMNC_singleSPAJ = nm_pre_file+"_"+dd.format(uploadTime)+"_"+dw.format(uploadTime)+"_"+no_urut+".pdf";
				
		String pdfPRE_SPAJ = "SPAJCafetari(MNC)okrev1.pdf";
		
		String pathTemplate = props.getProperty("pdf.template");
		String pathTemplateCafMnc = pathTemplate+"\\"+"CAFETARI_MNC";
	
		String outputName = destDirectory+"\\"+cafMNC_singleSPAJ;
		if(nm_pre_file.equalsIgnoreCase("A"))
		{
			pdfa.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("B"))
		{
			pdfb.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("C"))
		{
			pdfc.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("D"))
		{
			pdfd.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("E"))
		{
			pdfe.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("F"))
		{
			pdff.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("G"))
		{
			pdfg.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("H"))
		{
			pdfh.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("I"))
		{
			pdfi.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("J"))
		{
			pdfj.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("K"))
		{
			pdfk.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("L"))
		{
			pdfl.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("M"))
		{
			pdfm.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("N"))
		{
			pdfn.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("O"))
		{
			pdfo.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("P"))
		{
			pdfp.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("Q"))
		{
			pdfq.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("R"))
		{
			pdfr.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("S"))
		{
			pdfs.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("T"))
		{
			pdft.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("U"))
		{
			pdfu.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("V"))
		{
			pdfv.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("W"))
		{
			pdfw.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("X"))
		{
			pdfx.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("Y"))
		{
			pdfy.add(outputName);
		}
		else if(nm_pre_file.equalsIgnoreCase("Z"))
		{
			pdfz.add(outputName);
		}
		
		PdfReader reader = null;
		PdfStamper stamp = null;
		try{	
			reader = new PdfReader(pathTemplateCafMnc+"\\"+pdfPRE_SPAJ);
					outputName = destDirectory+"\\"+cafMNC_singleSPAJ;
			File file = null;
			file = new File(outputName);
			stamp = new PdfStamper(reader,new FileOutputStream(file));
			PdfContentByte cb = stamp.getOverContent(1);
				cb = stamp.getOverContent(1);
				cb.beginText();
				BaseFont bf = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALN.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			
				cb.setFontAndSize(bf, 11);
			   	int row_header =0;
			   	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, nama_kantor, 92, 724, 0);
			   	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, nama_pp, 110, 668, 0);
			   	cb.showTextAligned(PdfContentByte.ALIGN_CENTER, nik, 134, 593, 0);
			   	
			   	if(warga_negara.equalsIgnoreCase("Indonesia")){
			   		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "X", 108, 573, 0);
			   	}
			   	else {
			   		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "X", 177, 573, 0);
			   		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, warga_negara, 203, 574, 0);
			   	}
			   	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, day, 123, 556, 0);
			 	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, month, 159, 556, 0);
			 	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, year, 196, 556, 0);
				cb.showTextAligned(PdfContentByte.ALIGN_LEFT, String.valueOf(usia), 256, 556, 0);
				cb.showTextAligned(PdfContentByte.ALIGN_LEFT, tempat_lahir, 111, 538, 0);
				
				if(sex==1){
					cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "X", 390, 671, 0);
				}else{
					cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "X", 433, 671, 0);
				}
				
				if(sts_mrt.equalsIgnoreCase("Menikah")){
					cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "X", 390, 656, 0);
				}else if(sts_mrt.equalsIgnoreCase("Belum Menikah")){
					cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "X", 433, 656, 0);
				}else if(sts_mrt.equalsIgnoreCase("Janda/Duda")){
					cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "X", 509, 656, 0);
				}
				cb.showTextAligned(PdfContentByte.ALIGN_LEFT, agama, 390, 642, 0);
				
			   	cb.endText();
			   
		}catch(Exception e){
			logger.error("ERROR :", e);
		}finally{
			 stamp.close();
			 reader.close();
		}
	}
		
	if(!pdfa.isEmpty())
	{
		String pdfHasilA = mergePDF(pdfa, mergeDirectory);
		if(pdfHasilA!=null){
			pdfStamper.add(pdfHasilA);
		}	
	}
	if(!pdfb.isEmpty())
	{
		String pdfHasilB = mergePDF(pdfb, mergeDirectory);
		if(pdfHasilB!=null){
			pdfStamper.add(pdfHasilB);
		}
	}
	if(!pdfc.isEmpty())
	{
		String pdfHasilC = mergePDF(pdfc,  mergeDirectory);
		if(pdfHasilC!=null){
			pdfStamper.add(pdfHasilC);
		}
	}
	if(!pdfd.isEmpty())
	{
		String pdfHasilD = mergePDF(pdfd, mergeDirectory);
		if(pdfHasilD!=null){
			pdfStamper.add(pdfHasilD);
		}
	}
	if(!pdfe.isEmpty())
	{
		String pdfHasilE = mergePDF(pdfe, mergeDirectory);
		if(pdfHasilE!=null){
			pdfStamper.add(pdfHasilE);
		}
	}
	if(!pdff.isEmpty())
	{
		String pdfHasilF = mergePDF(pdff, mergeDirectory);
		if(pdfHasilF!=null){
			pdfStamper.add(pdfHasilF);
		}
	}
	if(!pdfg.isEmpty())
	{
		String pdfHasilG = mergePDF(pdfg, mergeDirectory);
		if(pdfHasilG!=null){
			pdfStamper.add(pdfHasilG);
		}
	}
	if(!pdfh.isEmpty())
	{
		String pdfHasilH = mergePDF(pdfh, mergeDirectory);
		if(pdfHasilH!=null){
			pdfStamper.add(pdfHasilH);
		}
	}
	if(!pdfi.isEmpty())
	{
		String pdfHasilI = mergePDF(pdfi,mergeDirectory);
		if(pdfHasilI!=null){
			pdfStamper.add(pdfHasilI);
		}
	}
	if(!pdfj.isEmpty())
	{
		String pdfHasilJ = mergePDF(pdfj, mergeDirectory);
		if(pdfHasilJ!=null){
			pdfStamper.add(pdfHasilJ);
		}
	}
	if(!pdfk.isEmpty())
	{
		String pdfHasilK = mergePDF(pdfk, mergeDirectory);
		if(pdfHasilK!=null){
			pdfStamper.add(pdfHasilK);
		}
	}
	if(!pdfl.isEmpty())
	{
		String pdfHasilL = mergePDF(pdfl, mergeDirectory);
		if(pdfHasilL!=null){
			pdfStamper.add(pdfHasilL);
		}
	}
	if(!pdfm.isEmpty())
	{
		String pdfHasilM = mergePDF(pdfm, mergeDirectory);
		if(pdfHasilM!=null){
			pdfStamper.add(pdfHasilM);
		}
	}
	if(!pdfn.isEmpty())
	{
		String pdfHasilN = mergePDF(pdfn, mergeDirectory);
		if(pdfHasilN!=null){
			pdfStamper.add(pdfHasilN);
		}
	}
	if(!pdfo.isEmpty())
	{
		String pdfHasilO = mergePDF(pdfo, mergeDirectory);
		if(pdfHasilO!=null){
			pdfStamper.add(pdfHasilO);
		}
	}
	if(!pdfp.isEmpty())
	{
		String pdfHasilP = mergePDF(pdfp, mergeDirectory);
		if(pdfHasilP!=null){
			pdfStamper.add(pdfHasilP);
		}
	}
	if(!pdfq.isEmpty())
	{
		String pdfHasilQ = mergePDF(pdfq, mergeDirectory);
		if(pdfHasilQ!=null){
			pdfStamper.add(pdfHasilQ);
		}
	}
	if(!pdfr.isEmpty())
	{
		String pdfHasilR = mergePDF(pdfr, mergeDirectory);
		if(pdfHasilR!=null){
			pdfStamper.add(pdfHasilR);
		}
	}
	if(!pdfs.isEmpty())
	{
		String pdfHasilS = mergePDF(pdfs, mergeDirectory);
		if(pdfHasilS!=null){
			pdfStamper.add(pdfHasilS);
		}
	}
	if(!pdft.isEmpty())
	{
		String pdfHasilT = mergePDF(pdft, mergeDirectory);
		if(pdfHasilT!=null){
			pdfStamper.add(pdfHasilT);
		}
	}
	if(!pdfu.isEmpty())
	{
		String pdfHasilU = mergePDF(pdfu, mergeDirectory);
		if(pdfHasilU!=null){
			pdfStamper.add(pdfHasilU);
		}
	}
	if(!pdfv.isEmpty())
	{
		String pdfHasilV = mergePDF(pdfv, mergeDirectory);
		if(pdfHasilV!=null){
			pdfStamper.add(pdfHasilV);
		}
	}
	if(!pdfw.isEmpty())
	{
		String pdfHasilW = mergePDF(pdfw, mergeDirectory);
		if(pdfHasilW!=null){
			pdfStamper.add(pdfHasilW);
		}
	}
	if(!pdfx.isEmpty())
	{
		String pdfHasilX= mergePDF(pdfx, mergeDirectory);
		if(pdfHasilX!=null){
			pdfStamper.add(pdfHasilX);
		}
	}
	if(!pdfy.isEmpty())
	{
		String pdfHasilY = mergePDF(pdfy, mergeDirectory);
		if(pdfHasilY!=null){
			pdfStamper.add(pdfHasilY);
		}
	}
	if(!pdfz.isEmpty())
	{
		String pdfHasilZ =mergePDF(pdfz, mergeDirectory);
		if(pdfHasilZ!=null){
			pdfStamper.add(pdfHasilZ);
		}
	}
	
	return pdfStamper;
	}
	
	public String mergePDF(List<String> pdf, String mergeDirectory){
		boolean suksesMerge=false;
		String merge="";		
		try{
			merge = pdf.get(0).trim();
			merge = merge.substring(59);
			merge=merge.trim();
			merge = merge.substring(0, 17)+merge.substring(22);
			merge=merge.trim();
			OutputStream output = new FileOutputStream(mergeDirectory+"\\"+merge);
			// ** Processing ConcatPDF **
			suksesMerge = MergePDF.concatPDFs(pdf, output, false);
			output.flush();
			output.close();
			}
			catch(Exception e){
				suksesMerge=false;
				logger.error("ERROR :", e);
			}
		
		if(suksesMerge){
			return merge; }
		else{
			return null;}
	}
	
	
	public String hitCounter(int i){
	  	String no_urut = String.valueOf(i);
	  	int len= no_urut.length();
		String hitCounter;
		
	  	if(len==1){
	  		hitCounter = "000"+no_urut;
	  	}else
	  	if(len==2){
		  	hitCounter = "00"+no_urut;
		}else
		if(len==3){
			hitCounter = "0"+no_urut;
		}else{
			hitCounter = no_urut;
		}					
	return hitCounter;
	}
	
}