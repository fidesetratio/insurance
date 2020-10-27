package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.File;
import java.io.PrintWriter;
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

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.utils.ExcelRead;
import com.ekalife.utils.PrintPolisPerjanjianAgent;
import com.ekalife.utils.parent.ParentController;
import com.google.gson.Gson;
import com.ibatis.common.resources.Resources;

public class UploadFeeRiderUw extends ParentController {
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
				 Map<String, Object> cmd = new HashMap<String, Object>();
				 String tabs="1";
				 String err = "";				
				 User user = (User) request.getSession().getAttribute("currentUser");
				 Integer lus_id = Integer.parseInt(user.getLus_id());
				 int json = ServletRequestUtils.getIntParameter(request, "json", 0);
				 int json1 = ServletRequestUtils.getIntParameter(request, "json1", 0);
				 String lca = ServletRequestUtils.getStringParameter(request, "lca", "0");
				 String lca1 = ServletRequestUtils.getStringParameter(request, "lca1", "0");
				 String stpolis = ServletRequestUtils.getStringParameter(request, "st_polis");
				 cmd.put("user_id", lus_id);
				 cmd.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
			     cmd.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
				 String dist=ServletRequestUtils.getStringParameter(request, "dist",null);
				 String company=ServletRequestUtils.getStringParameter(request, "company",null);			 
				 Integer cmp_id = null;
				 String submit = request.getParameter("upload");
				 List<Map> pesanList = new ArrayList<Map>();			 
				 List dbpolis=new ArrayList();
				 Map data_valid = (HashMap) this.elionsManager.select_validbank(2998);
		
			 	 //jika user menekan tombol upload
				 if(submit != null){
					Upload upload = new Upload();
					String file_fp = request.getParameter("file_fp");
					upload.setDaftarFile(new ArrayList<MultipartFile>(10));
					ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
					binder.bind(request);
					MultipartFile mf = upload.getFile1();
					
					String fileName = mf.getOriginalFilename();
						if(!mf.getContentType().contains("ms-excel")){
							Map<String,String> map2= new HashMap<String, String>();
							map2.put("sts", "ERROR ");
							map2.put("msg", "File Yg Di upload Mesti Excell");
							pesanList.add(map2);
						}else{
							String directory = props.getProperty("pdf.dir.export");//("temp.dir.fileupload");
							Map<String,String> dataUpload = new HashMap<String, String>();
							ExcelRead excelRead = new ExcelRead();
							List<List> SpajExcelList = new ArrayList<List>();
							String dest=directory + "/" + fileName ;
							File outputFile = new File(dest);
						    FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
						    SpajExcelList = excelRead.readExcelFile(directory + "\\", fileName);
						    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						    String noKloter =bacManager.noKloterUpload("");
						    for(int i = 1 ; i < SpajExcelList.size() ; i++){
						    	String tgl_di_excel= SpajExcelList.get(i).get(0).toString();
						    	String no_polis = SpajExcelList.get(i).get(1).toString();
						    	Date tgl_proses=df.parse(tgl_di_excel);
						    	String lsdbs_name =SpajExcelList.get(i).get(2).toString();
						    	String jml_excel=SpajExcelList.get(i).get(5).toString().substring(0,SpajExcelList.get(i).get(5).toString().lastIndexOf("."));
						    	Integer jml =Integer.parseInt(jml_excel);
						    	String jml_tax=SpajExcelList.get(i).get(6).toString();
						    	Double tax =Double.parseDouble(jml_tax);
						    	String status =SpajExcelList.get(i).get(3).toString();
						    	
						    	bacManager.insertFeeRider(tgl_proses, no_polis, lsdbs_name, jml, status, noKloter,tax);
						    }
						    Map<String,String> map2= new HashMap<String, String>();
					    	map2.put("msg", "Upload Berhasil");
							pesanList.add(map2);
						}
							
				   }	
				//jika user menekan tombol show report
				 if(request.getParameter("showReport") != null){
					 tabs="2";
					
					 List data=new ArrayList();
					 data=bacManager.selectDataUploadAdmedika("","",cmp_id,"",stpolis);
					 if(data.size() > 0){ //bila ada data
		    	    		
		    	    		ServletOutputStream sos2 = response.getOutputStream();
		    	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bac.data_upload") + ".jasper");
		    	    		
		    	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

		    	    		Map<String, Object> params = new HashMap<String, Object>();
		    	    		params.put("id", lus_id);
		    	    		


		    	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));

		    		    	if(request.getParameter("showXLS") != null){
		    		    		response.setContentType("application/vnd.ms-excel");
		    		            JRXlsExporter exporter = new JRXlsExporter();
		    		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		    		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos2);
		    		            exporter.exportReport();
		    		    	}else if(request.getParameter("showPDF") != null){
		    		    		JasperExportManager.exportReportToPdfStream(jasperPrint, sos2);
		    		    	}
		    	    		sos2.close();
		        		 }else{ //bila tidak ada data
		        			ServletOutputStream sos = response.getOutputStream();
		        			sos.println("<script>alert('Tidak ada data');window.close();</script>");
		        			sos.close();
		        		}
					 return null;
							 
				 }
				 
				//jika user menekan tombol show polis
				 if(request.getParameter("showPolicy") != null){					 
					 tabs="tab-3";
					 stpolis="1";
					 dbpolis=bacManager.selectDataUploadAdmedika("","",cmp_id,"",stpolis);
				 }
				 
				 if(request.getParameter("Transfer") != null){		
					 String kloternya="";
					 String check[] = request.getParameterValues("chbox");
						 if(check != null){
							 for(int i=0;i<check.length;i++){		
									String data = check[i];
									 kloternya = ServletRequestUtils.getStringParameter(request, "kloternya");
									String spaj =elionsManager.selectGetSpaj(data);
									bacManager.prosesTransferAdmedika(spaj, kloternya,data);
							 }
							 bacManager.prosesTransferAdmedika2(null, kloternya,null);
								  Map<String,String> map2= new HashMap<String, String>();
							    	map2.put("msg", "Proses Transfer Berhasil");
									pesanList.add(map2);
									cmd.put("pesanList", pesanList);
						}
				 }
				
				 cmd.put("dbpolis", dbpolis);
				 cmd.put("stpolis",stpolis);
				 cmd.put("pesanList", pesanList);
				 return new ModelAndView("uw/upload_feerider", cmd);
	 }	 


}
