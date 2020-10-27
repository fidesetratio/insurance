package com.ekalife.elions.web.finance;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.util.FileUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.filefilter.WildcardFilter;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.dao.FinanceDao;
import com.ekalife.elions.model.FactSheetNab;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentFormController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author : Daru, randy
 * @since : Dec 17, 2012
 */
public class UploadFactSheetNabController extends ParentFormController {
	protected final Log logger = LogFactory.getLog( getClass() );

	private UwManager uwManager;
	private BacManager bacManager;
	private Properties props;
	
	public void setUwManager(UwManager uwManager) {this.uwManager = uwManager;}
	public void setBacManager(BacManager bacManager) {this.bacManager = bacManager;}
	public void setProps(Properties props) {this.props = props;}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		FactSheetNab factSheet = (FactSheetNab) command;
		
		Map map = new HashMap();
		List lstNab = uwManager.selectListNab();
		map.put("lstNab",lstNab);
		
		return map;
		
	}
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
		FactSheetNab factSheet = (FactSheetNab) command;
		String email = request.getParameter("email");	
		if(email == null){
			if(factSheet.getFile() == null || factSheet.getFile().getSize() == 0){
				errors.rejectValue("file", "", "Harap masukkan file yang mau di upload.");
			}else if(factSheet.getFile().getSize() > 5242880){
				errors.rejectValue("file", "", "Ukuran file terlalu besar.");
			}else if(!factSheet.getFile().getContentType().equals("application/pdf")){
				errors.rejectValue("file", "", "Tipe file yang diperbolehkan hanya PDF.");
			}
			
			if(factSheet.getBulan().equals("")){
				errors.rejectValue("bulan", "", "Harap pilih BULAN terlebih dahulu.");
			}
			
			if(factSheet.getTahun().equals("")){
				errors.rejectValue("tahun", "", "Harap pilih TAHUN terlebih dahulu.");
			}
		}
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		FactSheetNab factSheet = (FactSheetNab) command;
		ModelAndView view = new ModelAndView("finance/upload_factsheet_nab");
		view.addObject("lstNab", uwManager.selectListNab());
				
		String upload = request.getParameter("upload");
		String email = request.getParameter("email");	
		String message = "";
		String messageValue = "";
				
		if(upload != null){
			String tahun = ServletRequestUtils.getStringParameter(request, "tahun","");
			String bulan = ServletRequestUtils.getStringParameter(request, "bulan","");
			
			MultipartFile file = factSheet.getFile();
			factSheet.setJenis(uwManager.selectJenisInvestNab(factSheet.getId()));
			
			try{
				if(!tahun.equals("") && !bulan.equals("")) {
					if(file.getSize() > 0){
						String directory_epolicy = props.getProperty("pdf.dir.factsheet_nab");
						String directory_bulanan = directory_epolicy+"\\"+tahun+"\\"+bulan;
//						String directory_epolicy = "C:\\EkaWeb\\PDF\\factsheet_nab";
//						String directory_bulanan = directory_epolicy +"\\"+tahun+"\\"+bulan;
						
						File dir = new File(directory_bulanan);
						
						if(!dir.exists()) {
							dir.mkdirs();
				        }
						
						if(factSheet.getId().toString().contains("DPLK")){
							factSheet.setJenis(factSheet.getId().toString());
						}
						
						String fileName = factSheet.getJenis() + ".pdf";
						File pdf_epolicy = new File(directory_epolicy + "\\" + fileName);
						File pdf_bulanan = new File(directory_bulanan + "\\" + fileName);

						if(pdf_epolicy.exists()){
							pdf_epolicy.delete();
						}
						
						if(pdf_bulanan.exists()){
							pdf_bulanan.delete();
						}
						
						file.transferTo(pdf_epolicy);
						FileUtils.copy(pdf_epolicy, pdf_bulanan);
						
						
						message 	= "successMessage";
						messageValue = "File berhasil di upload - " + factSheet.getJenis() + " - " + bulan + " " + tahun;	
					}
				}else{
					message 	= "errorMessage";
					messageValue = "Upload file gagal. TAHUN atau BULLAN belum dipilih.";	
				}
				
			}catch(Exception e){
				logger.error("ERROR :", e);
			//	return view.addObject("errorMessage", "Upload file gagal.");
				message		 = "errorMessage";
				messageValue = "Upload file gagal.";
			}
		}
		
		if(email != null){
			String directory = props.getProperty("pdf.dir.factsheet_nab");
			String bulan = new SimpleDateFormat("MM").format(new Date());
			//String bulan = "05";
			String periode = null;
			
			 Date periodex = new Date();
		     DateFormat df3 = new SimpleDateFormat("MMMM yyyy");
			 periode = df3.format(periodex);
			
			ArrayList<String> pdfWildcards = new ArrayList<String>();
            pdfWildcards.add("*.pdf");
			pdfWildcards.add("*.PDF");
			FileFilter pdfFilter = new WildcardFilter(pdfWildcards);
			
			File destDir = new File(directory);
			List<File> daftar = new ArrayList<File>();
			if(destDir.exists()) {
				
				List<File> children = new ArrayList<File>(Arrays.asList(destDir.listFiles(pdfFilter)));
				List<File> children2 = new ArrayList<File>();
			
	            SimpleDateFormat sdf = new SimpleDateFormat("MM");	    			       	            	            
				
				for(int i=0; i<children.size(); i++) {
					File f = children.get(i);
					Date mmYTD = new Date();
					mmYTD.setTime(f.lastModified());
					String bln = sdf.format(mmYTD);
					
					if( bulan.equals(bln)){
						children2.add(f);						
					}
				}
				
				if(children2.size()>0){
					ZipOutputStream out = null;
					try {
				        // out put file 
				        out = new ZipOutputStream(new FileOutputStream(directory+"//"+"Fund Fact Sheet as of "+periode+".zip"));
		
				        for(int i=0; i<children2.size(); i++) {
				        	FileInputStream in = null;
				        	try {
						        in = new FileInputStream(children2.get(i).toString());
						        // name the file inside the zip  file 
						        out.putNextEntry(new ZipEntry(children2.get(i).getName())); 
				
						        // buffer size
						        byte[] b = new byte[1024];
						        int count;
				
						        while ((count = in.read(b)) > 0) {
						            logger.info(b);
						            out.write(b, 0, count);			            
						        }
				        	}finally {
				        		try {
				        			if(in != null) {
				        				in.close();
				        			}
				        		}catch (Exception e) {
				        			logger.error("ERROR :", e);
								}
				        	}
				        } 
					}finally {
						try {
							if(out != null) {
								out.close();
							}
						}catch (Exception e) {
							logger.error("ERROR :", e);
						}
					}
		       		        
		        HashMap em = bacManager.selectMstConfig(6, "uploadFacSheetNab", "Investment Planning And Management");
		        String[] to = null;
		        to = em.get("NAME")!=null?em.get("NAME").toString().split(";"):null;
		        String[] cc = em.get("NAME2")!=null?em.get("NAME2").toString().split(";"):null;
		        String[] bcc = em.get("NAME3")!=null?em.get("NAME3").toString().split(";"):null;
		        
		        Date tanggal = elionsManager.selectSysdateSimple();
		        String from = "anthony@sinarmasmsiglife.co.id";
		    		      		        
		        String subject = "Fund Fact Sheet and Market Review PDF files as of "+ periode;
		        
		        String emMessage="Dear All, "+"\n\n Please find the attached Fund Fact Sheet and Market Review PDF files as of "+ periode+
		        		"\n\n Thank You,"+"\n\n Best Regards,"+"\n\n Anthony Gunawan"+"\n Investment Department"+
		        		"\n PT Asuransi Jiwa Sinarmas MSIG Tbk."+"\n Sinarmas MSIG Tower, Lt.6"+"\n Jl. Jend. Sudirman Kav.21"+"\n Jakarta 12920 - Indonesia"+
		        		"\n telp: 021-50597777  ext: 5112"+"\n email: anthony@sinarmasmsiglife.co.id";
		        
		        List<File> attachments = new ArrayList<File>();
		        		File file = new File(directory+"\\"+"Fund Fact Sheet as of "+periode+".zip");
		        		attachments.add( file );		        				        	
		        		
		    	EmailPool.send("INVESTMENT", 0, 0, 0, 0, 
						null, 0, 0, tanggal, null, 
						false, from, to, cc, bcc, subject, emMessage, attachments, null);
		    			    	
		    	file.delete();		    	
		    	message 	= "successMessage";
				messageValue = "Email berhasil dikirim.";
		    	
			}else{
				message		 = "errorMessage";
				messageValue = "Email gagal dikirim."+" Fund Fact Sheet periode "+periode+" tidak ada";
			}
	            
			}
			
		}
		
		//return view.addObject("successMessage", "File berhasil di upload.");
		return view.addObject(message, messageValue);
	}
	

	
}
