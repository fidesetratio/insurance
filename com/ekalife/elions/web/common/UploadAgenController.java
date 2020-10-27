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
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentController;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class UploadAgenController extends ParentController {
	
	public ModelAndView handleRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map cmd = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List<String> err = new ArrayList<String>();
		SimpleDateFormat formatDate =new SimpleDateFormat("dd/MM/yyyy");
		String jenisupload = ServletRequestUtils.getStringParameter(request, "jenisupload", "");
		String judul=ServletRequestUtils.getStringParameter(request, "judul", "PERJANJIAN KEAGENAN");
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
			if(count!=null){
				String counting = FormatString.rpad("0", count.toString(), 3);
				Integer oldcount = count - 1;
				String oldcounting = FormatString.rpad("0", oldcount.toString(), 3);
				
				if(jenisupload.equals("PERJANJIAN KEAGENAN PT ASURANSI JIWA SINARMAS MSIG Tbk.")){
					coding = "PK-AJS/LGL/VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/LGL/VER."+oldcounting+"/"+tgl;
					if(oldcount==0){
						oldcoding="-";
					}
					
				}else if(jenisupload.equals("PERJANJIAN KEAGENAN PT ARTHAMAS KONSULINDO")){
					coding = "PK-ART/LGL/VER."+counting+"/"+tgl;
					oldcoding= "PK-AJS/LGL/VER."+oldcounting+"/"+tgl;
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("PERJANJIAN KEAGENAN RELATIONSHIP OFFICER(RO)")){
					coding = "PK-RO/LGL/VER."+counting+"/"+tgl;
					oldcoding= "PK-RO/LGL/VER."+oldcounting+"/"+tgl;
					if(oldcount==0){
						oldcoding="-";
					}
				}else if(jenisupload.equals("PERJANJIAN KEAGENAN COORDINATOR RELATIONSHIP OFFICER(CRO)")){
					coding = "PK-CRO/LGL/VER."+counting+"/"+tgl;
					oldcoding= "PK-CRO/LGL/VER."+oldcounting+"/"+tgl;
					if(oldcount==0){
						oldcoding="-";
					}
				}
			}
			
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
			

		}
		
		if(request.getParameter("uploadbutton")!=null){
			String rev = cmd.get("revisi").toString();
			Integer revisi= Integer.parseInt(rev);
			String keterangan=ServletRequestUtils.getStringParameter(request, "keterangan", null);
			String kode=cmd.get("kode").toString();
			String oldkode=cmd.get("oldkode").toString();
			//String file = ServletRequestUtils.getStringParameter(request, "", arg2)
			
			//bagian ini untuk generate upload_id
			DateFormat df = new SimpleDateFormat("yyyyMM");
			Date tanggalrevisi = formatDate.parse(request.getParameter("tanggalrevisi"));
			sysdate = elionsManager.selectSysdateSimple();
			tgl = formatDate.format(sysdate);
			String upload_id = df.format(sysdate);
			upload_id = upload_id+"000";
			
			String tDest=props.getProperty("upload.legal");
			File destDir = new File(tDest);
			if(!destDir.exists()) destDir.mkdir();
			
			Upload upload = new Upload();
			ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
			binder.bind(request);
			
			if(!judul.equals("") && revisi!=0 && !keterangan.equals("") && !kode.equals("") && !oldkode.equals("")){
				if(upload.getFile1().isEmpty()==false){
					String filename = jenisupload;
					String namefile = upload.getFile1().getName();
					String name=filename.toUpperCase();
					String pdf = name+"("+revisi+")";
					String dest=tDest+"\\"+name+"("+revisi+").pdf";
					File outputFile = new File(dest);
					FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
					
					
					//ini basic path dari yusup jr.
					//String sementara = "C:\\EkaWeb\\Upload\\"+name+"("+revisi+").pdf";
					//File outputFile = new File(sementara);
				   
					 List history = elionsManager.selectMstHistoryUpload();
					    if(history.size()!=0){
					    	for(int i=0;i<history.size();i++){
					    		Map temp = (HashMap) history.get(i);
					    		String temp_file = (String) temp.get("TEMP_FILENAME");
					    		if(temp_file.equals(name)){
					    			uwManager.updateMstHistoryUpload(temp_file, "TIDAK AKTIF");
					    		}
					    	}
					    }
					    
					    //Bagian ini untuk memasukkan SK baru ke mst_history_upload
					    Integer id = Integer.parseInt(upload_id) + history.size()+1;
					    uwManager.insertMstHistoryUpload( id.toString(), kode, oldkode, pdf, name, tanggalrevisi, judul, "AKTIF", revisi,Integer.parseInt(currentUser.getLus_id()), keterangan, dest,sysdate);
					
				    //bagian ini untuk memasukkan itext berupa coding perjanjian di footer pdf
					PdfReader reader=null ;
					PdfStamper stamp = null;
					try{
						 
//					 	adding some metadata
						HashMap moreInfo = new HashMap();
						moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
						moreInfo.put("Title", "Kontrak Marketing");
						moreInfo.put("Subject", "Perjanjian Keagenan");
					    
//						 adding content to each page
						PdfContentByte over;
						BaseFont arial_narrow = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALN.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
						BaseFont arial_narrow_bold = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALNB.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
						BaseFont arial = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
						BaseFont arial_bold = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALBD.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
						
					    reader = new PdfReader(upload.getFile1().getInputStream());
					    stamp = new PdfStamper(reader,new FileOutputStream(dest));
					    
					    //yg ini basic path dari yusup jr.
					    //reader = new PdfReader("C:\\EkaWeb\\Upload"+"\\"+name+".pdf");
					    //PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(sementara));
					    
						stamp.setMoreInfo(moreInfo);
						
						
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
									over.endText();
								}else if(j == kode.length()-4){
									over = stamp.getOverContent(i+1);
									over.beginText();
									over.setFontAndSize(arial_narrow, 9);
									over.setColorFill(Color.gray);
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, kode.substring(j, j+1).toUpperCase(), x, 35, 0);
									over.endText();
								}else {
									over = stamp.getOverContent(i+1);
									over.beginText();
									over.setFontAndSize(arial_narrow,10);
									over.setColorFill(Color.gray);
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, kode.substring(j, j+1).toUpperCase(), x, 35, 0);
									over.endText();
								}
								
							}
							
						}
						
						
					}catch (Exception e) {
						logger.error("ERROR :", e);
					}finally{
						stamp.close();
						reader.close();
					}
				    
				    
				    //kondisi ini untuk mengganti status yang lama menjadi tidak aktif
				   
				    cmd.put("pesan", "Data Berhasil diupload");
				    
				    //untuk data asli
				    String[] to = {"tri.handini@sinarmasmsiglife.co.id","yulikusuma@sinarmasmsiglife.co.id","Primita@sinarmasmsiglife.co.id"};
				    String[] cc = {"Deddy@sinarmasmsiglife.co.id","anna_yulia@sinarmasmsiglife.co.id","Yune@sinarmasmsiglife.co.id","Hendra@sinarmasmsiglife.co.id"};
				    email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
				    		to, cc, 
				    		null, "PERJANJIAN KEAGENAN", "Kepada Yth.<br/> <b>Pihak Terkait</b> <br/>Di Tempat <br/> <br/>Dengan Hormat,<br/><br/>Bersama ini Kami sampaikan kepada Pihak Terkait " +
				    		"bahwa kami telah mengupload Perjanjian Keagenan yang terbaru untuk kategori : "+filename+", pertanggal "+tgl+".<br/>Dengan telah diuploadnya perjanjian keagenan yang dimaksud, maka perjanjian keagenan yang sebelumnya dinyatakan tidak berlaku lagi." +
				    		" <br/><br/>Demikian kami sampaikan, terima kasih.<br/><br/>Hormat Kami,<br/><b>Legal & Compliance</b> <br/>(021)6257808 ext.8705/6.", null);
				    
				    //untuk testing
//				    email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
//				    		new String[] {"Deddy@sinarmasmsiglife.co.id"}, new String[]{"Deddy@sinarmasmsiglife.co.id"}, 
//				    		null, "PERJANJIAN KEAGENAN", "Kepada Yth.<br/> <b>Pihak Terkait</b> <br/>Di Tempat <br/> <br/>Dengan Hormat,<br/><br/>Bersama ini Kami sampaikan kepada Pihak Terkait " +
//				    		"bahwa kami telah mengupload Perjanjian Keagenan yang terbaru mengenai "+jenisupload+", pertanggal "+tgl+".<br/>Dengan telah diuploadnya perjanjian keagenan yang dimaksud, maka perjanjian keagenan yang sebelumnya dinyatakan tidak berlaku lagi." +
//				    		" <br/><br/>Demikian kami sampaikan, terima kasih.<br/><br/>Hormat Kami,<br/><b>Legal & Compliance</b> <br/>(021)6257808 ext.8705/6.", null);
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
			
		}
		cmd.put("jenisupload", jenisupload);
		
		return new ModelAndView("common/uploadagen", cmd);
	}
	
}