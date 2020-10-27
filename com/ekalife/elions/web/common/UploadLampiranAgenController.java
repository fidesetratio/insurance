package com.ekalife.elions.web.common;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentController;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;


public class UploadLampiranAgenController extends ParentController {
	
	public ModelAndView handleRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map cmd = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List<String> err = new ArrayList<String>();
		SimpleDateFormat formatDate =new SimpleDateFormat("dd/MM/yyyy");
		String jenisupload = ServletRequestUtils.getStringParameter(request, "jenisupload", "");
		String judul=ServletRequestUtils.getStringParameter(request, "judul", "LAMPIRAN PERJANJIAN KEAGENAN");
		String flag=ServletRequestUtils.getStringParameter(request, "flag", "");
		Date sysdate = elionsManager.selectSysdateSimple();
		SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
		String tgl = format.format(sysdate);
		
		if(flag.equals("1")){
			Integer count = elionsManager.selectCountMstHistoryUpload(jenisupload.toUpperCase());
			count = count + 1;
			if(jenisupload.equals("")){
				count =null;
			}
			
			String coding = "";
			String oldcoding = "";
			String jabatan = "";
			if(count!=null){
				String counting = FormatString.rpad("0", count.toString(), 3);
				Integer oldcount = count - 1;
				String oldcounting = FormatString.rpad("0", oldcount.toString(), 3);
				
				if(jenisupload.equals("Lampiran AD Inkubator(AGENCY)")){
					coding = "PK-AJS/AS-ADI/VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-ADI/VER."+oldcounting+"/"+tgl;
					jabatan = "AGENCY DIRECTOR";
					if(oldcount==0){
						oldcoding="-";
					}
					
				}else if(jenisupload.equals("Lampiran AD Kantor Mandiri(AGENCY)")){
					coding = "PK-AJS/AS-ADK/VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-ADK/VER."+oldcounting+"/"+tgl;
					jabatan = "AGENCY DIRECTOR";
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("Lampiran AM(AGENCY)")){
					coding = "PK-AJS/AS-AAM /VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-AAM /VER."+oldcounting+"/"+tgl;
					jabatan = "AGENCY MANAGER";
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("Lampiran SM(AGENCY)")){
					coding = "PK-AJS/AS-ASM /VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-ASM /VER."+oldcounting+"/"+tgl;
					jabatan = "SALES MANAGER";
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("Lampiran SE(AGENCY)")){
					coding = "PK-AJS/AS-ASE/VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-ASE/VER."+oldcounting+"/"+tgl;
					jabatan = "SALES EXECUTIVE";
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("Lampiran RD(HYBRID)")){
					coding = "PK-AJS/AS-HRD/VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-HRD/VER."+oldcounting+"/"+tgl;
					jabatan = "REGIONAL DIRECTOR";
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("Lampiran RM(HYBRID)")){
					coding = "PK-AJS/AS-HRM /VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-HRM /VER."+oldcounting+"/"+tgl;
					jabatan = "REGIONAL MANAGER";
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("Lampiran DM(HYBRID)")){
					coding = "PK-AJS/AS-HDM /VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-HDM /VER."+oldcounting+"/"+tgl;
					jabatan = "DISTRICT MANAGER";
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("Lampiran BM(HYBRID)")){
					coding = "PK-AJS/AS-HBM /VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-HBM /VER."+oldcounting+"/"+tgl;
					jabatan = "BRANCH MANAGER";
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("Lampiran SM(HYBRID)")){
					coding = "PK-AJS/AS-HSM /VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-HSM /VER."+oldcounting+"/"+tgl;
					jabatan = "SALES MANAGER";
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("Lampiran FC(HYBRID)")){
					coding = "PK-AJS/AS-HFC/VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-HFC/VER."+oldcounting+"/"+tgl;
					jabatan = "FINANCIAL CONSULTANT";
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("Lampiran RM(REGIONAL)")){
					coding = "PK-AJS/AS-RRM /VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-RRM /VER."+oldcounting+"/"+tgl;
					jabatan = "REGIONAL MANAGER";
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("Lampiran SBM(REGIONAL)")){
					coding = "PK-AJS/AS-RSB/VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-RSB/VER."+oldcounting+"/"+tgl;
					jabatan = "SENIOR BRANCH MANAGER";
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("Lampiran BM(REGIONAL)")){
					coding = "PK-AJS/AS-RBM /VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-RBM /VER."+oldcounting+"/"+tgl;
					jabatan = "BRANCH MANAGER";
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("Lampiran UM(REGIONAL)")){
					coding = "PK-AJS/AS-RUM /VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-RUM /VER."+oldcounting+"/"+tgl;
					jabatan = "UNIT MANAGER";
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("Lampiran ME(REGIONAL)")){
					coding = "PK-AJS/AS-RME/VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-RME/VER."+oldcounting+"/"+tgl;
					jabatan = "MARKETING EXECUTIVE";
					if(oldcount==0){
						oldcoding="-";
					}
				}if(jenisupload.equals("Lampiran BRIDGE AD")){
					coding = "PK-AJS/AS-BAD/VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-BAD/VER."+oldcounting+"/"+tgl;
					jabatan = "AGENCY DIRECTOR";
					if(oldcount==0){
						oldcoding="-";
					}
					
				}else if(jenisupload.equals("Lampiran BRIDGE AM")){
					coding = "PK-AJS/AS-BAM /VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-BAM /VER."+oldcounting+"/"+tgl;
					jabatan = "AGENCY MANAGER";
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("Lampiran BRIDGE SM")){
					coding = "PK-AJS/AS-BSM /VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-BSM /VER."+oldcounting+"/"+tgl;
					jabatan = "SALES MANAGER";
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("Lampiran BRIDGE SE")){
					coding = "PK-AJS/AS-BSE/VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-BSE/VER."+oldcounting+"/"+tgl;
					jabatan = "SALES EXECUTIVE";
					if(oldcount==0){
						oldcoding="-";
					}
				}if(jenisupload.equals("Lampiran AD Inkubator(NEW AGENCY)")){
					coding = "PK-AJS/AS-NADI/VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-NADI/VER."+oldcounting+"/"+tgl;
					jabatan = "AGENCY DIRECTOR";
					if(oldcount==0){
						oldcoding="-";
					}
					
				}else if(jenisupload.equals("Lampiran AD Kantor Mandiri(NEW AGENCY)")){
					coding = "PK-AJS/AS-NADK/VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-NADK/VER."+oldcounting+"/"+tgl;
					jabatan = "AGENCY DIRECTOR";
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("Lampiran AM(NEW AGENCY)")){
					coding = "PK-AJS/AS-NAAM /VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-NAAM /VER."+oldcounting+"/"+tgl;
					jabatan = "AGENCY MANAGER";
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("Lampiran SM(NEW AGENCY)")){
					coding = "PK-AJS/AS-NASM /VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-NASM /VER."+oldcounting+"/"+tgl;
					jabatan = "SALES MANAGER";
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("Lampiran SE(NEW AGENCY)")){
					coding = "PK-AJS/AS-NASE/VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/AS-NASE/VER."+oldcounting+"/"+tgl;
					jabatan = "SALES EXECUTIVE";
					if(oldcount==0){
						oldcoding="-";
					}
				}
			}
			
			cmd.put("jabatan", jabatan);
			cmd.put("kode", coding);
			cmd.put("revisi", count);
			cmd.put("oldkode", oldcoding);
			
		}
		
		if(request.getParameter("download") !=null){
			String uploadid = ServletRequestUtils.getStringParameter(request, "uploadid", "");
			
			//bagian ini agar data listnya tetap tampil
			String jenis = ServletRequestUtils.getStringParameter(request, "jenis", "");
			String level = null;
			Date tglawal = null, tglakhir = null;
//			if(jenis.equals("PERJANJIAN")){
//				level = ServletRequestUtils.getStringParameter(request, "level", "");
//				cmd.put("level", level);
//			}
//			else if(jenis.equals("STATUS")){
//				level = ServletRequestUtils.getStringParameter(request, "level", "");
//				cmd.put("level", level);
//			}
//			else if(jenis.equals("TANGGAL")){
//				tglawal = formatDate.parse(request.getParameter("tglawal"));
//				tglakhir = formatDate.parse(request.getParameter("tglakhir"));
//				cmd.put("tglawal", tglawal);
//				cmd.put("tglakhir", tglakhir);
//			}
			List list = elionsManager.selectListMstHistoryUpload(jenis, level, tglawal, tglakhir, judul);
			cmd.put("list", list);
			cmd.put("jenis", jenis);
			
			//bagian ini untuk menampilkan file yang dipilih saat klik button
			String path = elionsManager.selectPathMstHistoryUpload(uploadid);
			String filename = elionsManager.selectFilenameMstHistoryUpload(uploadid);
			File l_file = new File(path);
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try{
				
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Attachment;filename="+filename+".pdf");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				
				in = new FileInputStream(l_file);
			    ouputStream = response.getOutputStream();
			    
			    IOUtils.copy(in, ouputStream);
			}catch (Exception e) {
				logger.error("ERROR :", e);
			}finally {
				try {
					if(in != null) {
						in.close();
					}
					if(ouputStream != null) {
						ouputStream.close();
					}
				}catch (Exception e) {
					logger.error("ERROR :", e);
				}
			}
			
			return null;
		}
		
		if(request.getParameter("viewbutton") !=null){
			String jenis = ServletRequestUtils.getStringParameter(request, "jenis", "");
			String level = null;
			Date tglawal = null, tglakhir = null;
			if(jenis.equals("PERJANJIAN")){
				level = ServletRequestUtils.getStringParameter(request, "level", "");
				cmd.put("level", level);
			}
			else if(jenis.equals("STATUS")){
				level = ServletRequestUtils.getStringParameter(request, "level", "");
				cmd.put("level", level);
			}
			else if(jenis.equals("TANGGAL")){
				tglawal = formatDate.parse(request.getParameter("tglawal"));
				tglakhir = formatDate.parse(request.getParameter("tglakhir"));
				cmd.put("tglawal", tglawal);
				cmd.put("tglakhir", tglakhir);
			}
			List list = elionsManager.selectListMstHistoryUpload(jenis, level, tglawal, tglakhir, judul);
			cmd.put("list", list);
			cmd.put("jenis", jenis);
			
//			 if(list.size()!=0){
//				 for(int i=0; i<list.size();i++){
//					 Map temp = (HashMap) list.get(i);
//					 String temp_filename = (String) temp.get("TEMP_FILENAME");
//					 Date tgl_revisi = (Date) temp.get("REVISI_DATE");
//					 cmd.put("temp_filename", temp_filename);
//					 cmd.put("tgl_revisi", tgl_revisi);
//				 }
//			 }
			
		}
		
		if(request.getParameter("uploadbutton")!=null){
			String rev = cmd.get("revisi").toString();
			Integer revisi= Integer.parseInt(rev);
			String keterangan=ServletRequestUtils.getStringParameter(request, "keterangan", null);
			String kode=cmd.get("kode").toString();
			String oldkode=cmd.get("oldkode").toString();
			String jabatan=cmd.get("jabatan").toString();
			String ketentuan=ServletRequestUtils.getStringParameter(request, "ketentuan", "");
			
			//bagian ini untuk generate upload_id
			DateFormat df = new SimpleDateFormat("yyyyMM");
			Date tanggalrevisi = formatDate.parse(request.getParameter("tanggalrevisi"));
			sysdate = elionsManager.selectSysdateSimple();
			tgl = formatDate.format(sysdate);
			String upload_id = df.format(sysdate);
			upload_id = upload_id+"000";
			
			Upload upload = new Upload();
			ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
			binder.bind(request);
			
			String tDest=props.getProperty("upload.agency");
			File destDir = new File(tDest);
			if(!destDir.exists()) destDir.mkdir();
			
			if(!judul.equals("") && !jabatan.equals("") && !ketentuan.equals("") && revisi!=0 && !keterangan.equals("") && !kode.equals("") && !oldkode.equals("")){
				if(upload.getFile1().isEmpty()==false){
					//String filename=upload.getFile1().getOriginalFilename();
					//int row = filename.indexOf('.');
//					String name=filename.substring(0, row).toUpperCase();
					String filename = jenisupload;
					String name=filename.toUpperCase();
					String pdf = name+"("+revisi+")";
					String dest=tDest+"\\"+name+"("+revisi+").pdf";
					File outputFile = new File(dest);
					FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
				    
//					kondisi ini untuk mengganti status yang lama menjadi tidak aktif
				    List history = elionsManager.selectMstHistoryUpload();
				    if(history.size()!=0){
				    	for(int i=0;i<history.size();i++){
				    		Map temp = (HashMap) history.get(i);
				    		String temp_file = (String) temp.get("TEMP_FILENAME");
				    		if(temp_file==null)temp_file="";
				    		if(temp_file.equals(name)){
				    			uwManager.updateMstHistoryUpload(temp_file, "TIDAK AKTIF");
				    		}
				    	}
				    }
				    
				    //Bagian ini untuk memasukkan SK baru ke mst_history_upload
				    Integer id = Integer.parseInt(upload_id)+history.size()+1;
				    uwManager.insertLampiranMstHistoryUpload( id.toString(), kode, oldkode, pdf, name, tanggalrevisi, judul, "AKTIF", revisi,Integer.parseInt(currentUser.getLus_id()), keterangan, dest, jabatan, ketentuan, sysdate);
					
				    //bagian ini untuk memasukkan itext berupa coding perjanjian di footer pdf
					try{
						 
//					 	adding some metadata
						HashMap moreInfo = new HashMap();
						moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
						moreInfo.put("Title", "Kontrak Marketing");
						moreInfo.put("Subject", "Lampiran Perjanjian Keagenan");
					    
//						 adding content to each page
						PdfContentByte over;
						BaseFont arial_narrow = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALN.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
						BaseFont arial_narrow_bold = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALNB.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
						BaseFont arial = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
						BaseFont arial_bold = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALBD.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
						
						
					    PdfReader reader=null ;
					    String lampiran = "Lampiran";
					    reader = new PdfReader(upload.getFile1().getInputStream());
					    PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(dest));
						stamp.setMoreInfo(moreInfo);
						over = stamp.getOverContent(1);
						over.beginText();
						over.setFontAndSize(arial_narrow,14);
						over.setColorFill(Color.black);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT, lampiran.toUpperCase(), 275, 730, 0);
						//logger.info(kode.substring(j, j+1).toUpperCase());
						over.endText();
						
						int totalpage = reader.getNumberOfPages();
						for(int i=0;i<totalpage;i++){
							//testing untuk for ini
							int kodelength = kode.length();
							for(int j=0;j<kode.length();j++){
								//ini untuk menentukan baris terakhir dan ke 4 terakhir size fontnya beda
								int x = 80 + (j*4);
								if(j ==kode.length()-1){
									over = stamp.getOverContent(i+1);
									over.beginText();
									over.setFontAndSize(arial_narrow,9);
									over.setColorFill(Color.gray);
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, kode.substring(j, j+1).toUpperCase(), x, 35, 0);
									//logger.info(kode.substring(j, j+1).toUpperCase());
									over.endText();
								}else if(j == kode.length()-4){
									over = stamp.getOverContent(i+1);
									over.beginText();
									over.setFontAndSize(arial_narrow, 9);
									over.setColorFill(Color.gray);
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, kode.substring(j, j+1).toUpperCase(), x, 35, 0);
									//logger.info(kode.substring(j, j+1).toUpperCase());
									over.endText();
								}else {
									over = stamp.getOverContent(i+1);
									over.beginText();
									over.setFontAndSize(arial_narrow,10);
									over.setColorFill(Color.gray);
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, kode.substring(j, j+1).toUpperCase(), x, 35, 0);
									//logger.info(kode.substring(j, j+1).toUpperCase());
									over.endText();
								}
								
							}
							
						}
						stamp.close();
						reader.close();
					}catch (Exception e) {
						logger.error("ERROR :", e);
					}
				    
				    
				    cmd.put("pesan", "Data Berhasil diupload");
				    
				    String[] to = {"tri.handini@sinarmasmsiglife.co.id","yulikusuma@sinarmasmsiglife.co.id","Primita@sinarmasmsiglife.co.id"};
				    String[] cc = {"Deddy@sinarmasmsiglife.co.id","anna_yulia@sinarmasmsiglife.co.id","Yune@sinarmasmsiglife.co.id","Hendra@sinarmasmsiglife.co.id"};
				    
				    //untuk data asli
//				    email.send(
				    EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0,
				    		null, 0, 0, new Date(), null,
				    		true, props.getProperty("admin.ajsjava"), 
				    		to, cc, 
				    		null, " LAMPIRAN PERJANJIAN KEAGENAN", "Kepada Yth.<br/> <b>Pihak Terkait</b> <br/>Di Tempat <br/> <br/>Dengan Hormat,<br/><br/>Bersama ini Kami sampaikan kepada Pihak Terkait " +
				    		"bahwa kami telah mengupload Lampiran Perjanjian Keagenan yang terbaru, pertanggal "+tgl+".<br/>Dengan telah diuploadnya Lampiran Perjanjian Keagenan yang dimaksud, maka Lampiran Perjanjian Keagenan yang sebelumnya dinyatakan tidak berlaku lagi." +
				    		" <br/><br/>Demikian kami sampaikan, terima kasih.<br/><br/>Hormat Kami,<br/><b>Agency Support Dept</b> <br/><b>(021)6257808 ext.8612/8202<b/>", null, null);
				    
				    //untuk testing
//				    email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
//				    		new String[] {"Deddy@sinarmasmsiglife.co.id"}, new String[]{"Deddy@sinarmasmsiglife.co.id"}, 
//				    		null, " LAMPIRAN PERJANJIAN KEAGENAN", "Kepada Yth.<br/> <b>Pihak Terkait</b> <br/>Di Tempat <br/> <br/>Dengan Hormat,<br/><br/>Bersama ini Kami sampaikan kepada Pihak Terkait " +
//				    		"bahwa kami telah mengupload Lampiran Perjanjian Keagenan yang terbaru mengenai "+jenisupload+", pertanggal "+tgl+".<br/>Dengan telah diuploadnya Lampiran Perjanjian Keagenan yang dimaksud, maka Lampiran Perjanjian Keagenan yang sebelumnya dinyatakan tidak berlaku lagi." +
//				    		" <br/><br/>Demikian kami sampaikan, terima kasih.<br/><br/>Hormat Kami,<br/><b>Agency Support Dept</b> <br/><b>(021)6257808 ext.8612/8202<b/>", null);
				}
			}
			else{
				err.add("Semua keterangan harus diisi.");
				cmd.put("errors", err);
			}
			
			
			cmd.put("oldkode", oldkode);
			cmd.put("kode", kode);
			cmd.put("keterangan", keterangan);
			cmd.put("judul", judul);
			cmd.put("revisi", revisi+1);
			cmd.put("jabatan", jabatan);
			cmd.put("ketentuan", ketentuan);
			
		}
		cmd.put("jenisupload", jenisupload);
		
		return new ModelAndView("common/uploadlampiranagen", cmd);
	}
	
}