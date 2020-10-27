package com.ekalife.elions.web.uw;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.ExcelRead;
import com.ekalife.utils.parent.ParentController;
import com.google.gson.Gson;
import com.ibatis.common.resources.Resources;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.util.FileUtil;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

public class DataUploadFormController extends ParentController {
	
	@SuppressWarnings("unchecked")
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String, Object> cmd = new HashMap<String, Object>();
		String tabs = "1";
		String pesanKemenangan = "";
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
		String dist = ServletRequestUtils.getStringParameter(request, "dist", null);
		String company = ServletRequestUtils.getStringParameter(request, "company", null);			 
		Integer cmp_id = null;
		String submit = request.getParameter("upload");
		List<Map> pesanList = new ArrayList<Map>();
		List dbpolis = new ArrayList();
			
		//untuk tab1
		List<DropDown> jenisDist = new ArrayList<DropDown>();
		List<DropDown> listCompany = new ArrayList<DropDown>();
		jenisDist.add(new DropDown("00","------PILIH DISTRIBUSI---------"));				
		jenisDist.add(new DropDown("04","ARCO"));
		jenisDist.add(new DropDown("05","SMD"));
		jenisDist.add(new DropDown("06","DMTM"));
		jenisDist.add(new DropDown("07","SSS"));
		jenisDist.add(new DropDown("08","SIP"));
		jenisDist.add(new DropDown("10","REDBERRY"));
		jenisDist.add(new DropDown("11","PSJS"));
		jenisDist.add(new DropDown("12","EKAWAKTU"));
		jenisDist.add(new DropDown("13","PRIORITAS"));
		jenisDist.add(new DropDown("14","ACCIDENT CASH"));
		jenisDist.add(new DropDown("15","SAPTA"));
		jenisDist.add(new DropDown("16","DENA"));
		jenisDist.add(new DropDown("17","SIO"));
		jenisDist.add(new DropDown("19","SIO+"));
		jenisDist.add(new DropDown("18","GIO"));
		jenisDist.add(new DropDown("17","GIO(Q)"));//SIO(GIO)//SIO(guaranteed)
		if(json==1){
			if(!lca.isEmpty() && !lca.equals("04")){
				listCompany = bacManager.selectCompanyUpload(lca);
				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				Gson gson = new Gson();
				out.print(gson.toJson(listCompany));
				out.close();
			}
			return null;
	    }

		//untuk tab2
		List<DropDown> jenisDist1 = new ArrayList<DropDown>();
		List<DropDown> jnsReport = new ArrayList<DropDown>();
		List<DropDown> listCompany1 = new ArrayList<DropDown>();
		jenisDist1.add(new DropDown("00","------PILIH DISTRIBUSI---------"));		
		//jenisDist1.add(new DropDown("02","PERUSAHAAN PRODUK INDIVIDU"));				 
		jenisDist1.add(new DropDown("04","ARCO"));
		jenisDist1.add(new DropDown("05","SMD"));
		jenisDist1.add(new DropDown("06","DMTM"));
		jenisDist1.add(new DropDown("07","SSS"));
		jenisDist1.add(new DropDown("08","SIP"));
		jenisDist1.add(new DropDown("09","BSIM"));
		jenisDist1.add(new DropDown("10","REDBERRY"));
		jenisDist1.add(new DropDown("11","PSJS"));
		jenisDist1.add(new DropDown("13","PRIORITAS"));
		jenisDist1.add(new DropDown("15","SAPTA"));
		jenisDist1.add(new DropDown("16","DENA"));
		jenisDist1.add(new DropDown("19","MARZ")); //cmp_id=14
		jenisDist1.add(new DropDown("20","VALDO")); // cmp_id=15
		jenisDist1.add(new DropDown("21","GOS")); //cmp_id=16
		jenisDist1.add(new DropDown("23","VASCO")); //cmp_id=20
		jenisDist1.add(new DropDown("24","NISSAN")); //cmp_id=21
		jenisDist1.add(new DropDown("25","SYNERGYS")); //cmp_id=22 
		jenisDist1.add(new DropDown("26","SSI")); //cmp_id=23 
		jenisDist1.add(new DropDown("27","APK")); //cmp_id=24  AUSINDO PRATAMA KARYA
		jenisDist1.add(new DropDown("28","ABH")); //cmp_id=25  PT ABHIPRAYA
		jenisDist1.add(new DropDown("29","KAY")); //cmp_id=26  PT KAYZAN 
		
		jnsReport.add(new DropDown("00","TOTAL DATA YANG BELUM DI INPUT"));					
		jnsReport.add(new DropDown("01","TOTAL DATA YANG SUDAH DI INPUT"));
		jnsReport.add(new DropDown("02","ALL"));
		if(json1==1){
			if(!lca1.isEmpty() && !lca1.equals("04")){
				listCompany1 = bacManager.selectCompanyUpload(lca);
				response.setContentType("application/json");
		    	PrintWriter out = response.getWriter();
		    	Gson gson = new Gson();
		    	out.print(gson.toJson(listCompany));
		    	out.close();
			}
			return null;
		}
		
		//untuk tab3
		List<DropDown> jenisDist2 = new ArrayList<DropDown>();
		List<DropDown> listCompany2 = new ArrayList<DropDown>();
		jenisDist2.add(new DropDown("00","------PILIH DISTRIBUSI---------"));		
		//jenisDist1.add(new DropDown("02","PERUSAHAAN PRODUK INDIVIDU"));				 
		jenisDist2.add(new DropDown("04","ARCO"));
		jenisDist2.add(new DropDown("05","SMD"));
		jenisDist2.add(new DropDown("06","DMTM"));
		jenisDist2.add(new DropDown("07","SSS"));
		jenisDist2.add(new DropDown("08","SIP"));
		jenisDist2.add(new DropDown("09","BSIM"));
		jenisDist2.add(new DropDown("10","REDBERRY"));
		jenisDist2.add(new DropDown("11","PSJS"));
		jenisDist2.add(new DropDown("13","PRIORITAS"));
		jenisDist2.add(new DropDown("15","SAPTA"));
		jenisDist2.add(new DropDown("16","DENA"));
		jenisDist2.add(new DropDown("19","MARZ")); //cmp_id=14
		jenisDist2.add(new DropDown("20","VALDO")); //cmp_id=15
		jenisDist2.add(new DropDown("21","GOS")); // cmp_id=16
		jenisDist2.add(new DropDown("23","VASCO")); //cmp_id=20
		jenisDist2.add(new DropDown("24","NISSAN")); //cmp_id=21
		jenisDist2.add(new DropDown("25","SYNERGYS")); //cmp_id=22 
		jenisDist2.add(new DropDown("26","SSI")); //cmp_id=23 
		jenisDist2.add(new DropDown("27","APK")); //cmp_id=24  AUSINDO PRATAMA KARYA
		jenisDist2.add(new DropDown("28","ABH")); //cmp_id=25  PT ABHIPRAYA
		jenisDist2.add(new DropDown("29","KAY")); //cmp_id=26  PT KAYZAN 
		if(json1==1){
			if(!lca1.isEmpty() && !lca1.equals("04")){
				listCompany2 = bacManager.selectCompanyUpload(lca);
				response.setContentType("application/json");
		    	PrintWriter out = response.getWriter();
		    	Gson gson = new Gson();
		    	out.print(gson.toJson(listCompany));
		    	out.close();
			}
			return null;
		}
				 
		//jika user menekan tombol upload
		if(submit != null){
		    Upload upload = new Upload();
			String file_fp = request.getParameter("file_fp");
			upload.setDaftarFile(new ArrayList<MultipartFile>(10));
			ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
			binder.bind(request);
			MultipartFile mf = upload.getFile1();
			String fileName = mf.getOriginalFilename();
			String directory = props.getProperty("pdf.dir.export");//("temp.dir.fileupload");
			
			ExcelRead excelRead = new ExcelRead();
			ArrayList<List> SpajExcelList = new ArrayList<List>();
			String dest = directory + "/" + fileName ;
			File outputFile = new File(dest);
		    FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
		    SpajExcelList = Common.serializableList(excelRead.readExcelFileNew(directory + "\\", fileName));
		    pesanList = Common.serializableList(bacManager.terimaDataXls(SpajExcelList,user,company,dist));
		    FileUtil.deleteFile(directory, fileName, response);//Request Pak Him File Uploadnya di delete aja
			    
			if(pesanList.isEmpty()){
				Map<String,String> map2= new HashMap<String, String>();
				map2.put("sts", "SUCCESS");
				map2.put("msg", "Upload Berhasil");
				pesanList.add(map2);	
			}			
		}	
				 
		//jika user menekan tombol show report
		if(request.getParameter("showReport") != null){
			tabs = "2";
			String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
	    	String edate = ServletRequestUtils.getStringParameter(request, "edate");
	    	String dist1 = ServletRequestUtils.getStringParameter(request, "dist1", null);
			String company1 = ServletRequestUtils.getStringParameter(request, "company1", null);
			String jenisReport = ServletRequestUtils.getStringParameter(request, "tipe", null);
		
			List data = new ArrayList();
			if(dist1.equals("04")){
				cmp_id=2;
			}else if(dist1.equals("05")){
				cmp_id=3;
			}else if(dist1.equals("06")){
				cmp_id=5;
			}else if(dist1.equals("07")){
				cmp_id=6;
			}else if(dist1.equals("08")){
				cmp_id=7;
			}else if(dist1.equals("09")){
				cmp_id=8;
			}else if(dist1.equals("10")){
				cmp_id=9;
		 	}else if(dist1.equals("11")){
		 		cmp_id=10;
		 	}else if(dist1.equals("11")){
		 		cmp_id=10;
		 	}else if(dist1.equals("13")){
		 		cmp_id=11;
		 	}else if(dist1.equals("15")){
		 		cmp_id=12;
		 	}else if(dist1.equals("16")){
		 		cmp_id=13;
		 	}else if(dist1.equals("18")){
		 		cmp_id=18;
		 	}else if(dist1.equals("19")){
				cmp_id=14;//PT. MARZ
			}else if(dist1.equals("20")){
				cmp_id=15;// PT. VALDO
			}else if(dist1.equals("21")){
				cmp_id=16;// PT. GOS
			}else if(dist1.equals("22")){
				cmp_id=19; //SMiLe Link 99
			}else if(dist1.equals("23")){
				cmp_id=20;// PT. VASCO
			}else if(dist1.equals("24")){
				cmp_id=21;// PT. NISSAN
			}else if(dist1.equals("25")){
				cmp_id=22;// PT. SYNERGYS
			}else if(dist1.equals("26")){
				cmp_id=23;// PT. SSI (SEVEN STARS INDONESIA)
			}else if(dist1.equals("27")){
				cmp_id=24;// PT. APK (PT AUSINDO PRATAMA KARYA)
			}else if(dist1.equals("28")){
				cmp_id=25;// PT ABHIPRAYA
			}else if(dist1.equals("29")){
				cmp_id=26;// PT KAYZAN
			}
			stpolis = jenisReport.substring(1);
			data = bacManager.selectdataTempAll(bdate,edate,cmp_id,company1,stpolis);
			if(data.size() > 0){ //bila ada data
				ServletOutputStream sos2 = response.getOutputStream();
				File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bac.data_upload") + ".jasper");
				JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
			    Map<String, Object> params = new HashMap<String, Object>();
			    params.put("tanggal1", bdate);
			    params.put("tanggal2", edate);
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
    			sos.flush();
    			sos.close();
    		}
			return null;
		}
				 
		//jika user menekan tombol show polis
		if(request.getParameter("showPolicy") != null){
			tabs = "3";
			String bdate = ServletRequestUtils.getStringParameter(request, "bdate1");
			String edate = ServletRequestUtils.getStringParameter(request, "edate1");
			String dist1 = ServletRequestUtils.getStringParameter(request, "dist2",null);
			String company1 = ServletRequestUtils.getStringParameter(request, "company2",null);				 
			stpolis = ServletRequestUtils.getStringParameter(request, "st_polis");
			if(dist1.equals("04")){
				cmp_id=2;
			}else if(dist1.equals("05")){
				cmp_id=3;
			}else if(dist1.equals("06")){
				cmp_id=5;
			}else if(dist1.equals("07")){
				cmp_id=6;
			}else if(dist1.equals("08")){
				cmp_id=7;
			}else if(dist1.equals("09")){
				cmp_id=8;
			}else if(dist1.equals("10")){
				cmp_id=9;
			}else if(dist1.equals("11")){
				cmp_id=10;
			}else if(dist1.equals("13")){
				cmp_id=11;
			}else if(dist1.equals("15")){
				cmp_id=12;
			}else if(dist1.equals("16")){
				cmp_id=13;
			}else if(dist1.equals("18")){
				cmp_id=18;
			}else if(dist1.equals("19")){
				cmp_id=14; //PT. MARZ
			}else if(dist1.equals("20")){
				cmp_id=15; // PT. VALDO
			}else if(dist1.equals("21")){
				cmp_id=16; // PT. GOS
			}else if(dist1.equals("22")){
				cmp_id=19;
			}else if(dist1.equals("23")){
				cmp_id=20;// PT. VASCO
			}else if(dist1.equals("24")){
				cmp_id=21;// PT. NISSAN
			}else if(dist1.equals("25")){
				cmp_id=22;// PT. SYNERGYS
			}else if(dist1.equals("26")){
				cmp_id=23;// PT. SSI (SEVEN STARS INDONESIA)
			}else if(dist1.equals("27")){
				cmp_id=24;// PT. APK (PT AUSINDO PRATAMA KARYA)
			}else if(dist1.equals("28")){
				cmp_id=25;// PT ABHIPRAYA
			}else if(dist1.equals("29")){
				cmp_id=26;// PT KAYZAN
			}
			cmd.put("bdate", bdate);
			cmd.put("edate", edate);
			cmd.put("companya", company1);
			cmd.put("jenis", dist1);
			cmd.put("stpolis", stpolis);
					
			if(request.getParameter("InputDel") != null){
				String check[] = request.getParameterValues("chbox");
				if(request.getParameter("delete") != null){
					if(check != null){
						for(int i=0;i<check.length;i++){										
							String data = check[i];
							String notemp = data.substring(0, data.indexOf("~"));
							pesanList=bacManager.deleteDatatemp(notemp);
						}
						if(pesanList.isEmpty()){
//								String pesan="upload berhasil";
							Map<String,String> map2= new HashMap<String, String>();
							map2.put("sts2", "SUCCESS");
							map2.put("msg2", "Delete Berhasil");
							pesanList.add(map2);	
						}
					}
				}
				if(request.getParameter("input") != null){
					int countSpaj=0;
					int countGagal=0;
					if(check != null){
						String pesanInput="";
						for(int i=0;i<check.length;i++){										
							String data = check[i];
							String temp_id = data.substring(0, data.indexOf("~"));								 
							pesanInput = bacManager.inputDataTemp(temp_id,user);
							if(pesanInput.isEmpty()){
								countSpaj++;
							}else{
								countGagal++;
								Map<String,String> map2= new HashMap<String, String>();
								map2.put("sts2", "FAILED");
								map2.put("msg2", temp_id+" "+"GAGAL INPUT"+" "+"("+pesanInput+")");											
								pesanList.add(map2);	
							}
							pesanKemenangan = " "+countSpaj+" "+"berhasil input, "+countGagal +" gagal input";
						}
					}
				}
				if(request.getParameter("transfer") != null){
					for(int i=0;i<check.length;i++){
						String data = check[i];
						String spaj = data.substring(data.indexOf("~")+1, data.length());
						HashMap<String, Object> mapAutoAccept = new HashMap<String, Object>();
						Pemegang pmg = elionsManager.selectpp(spaj);
						Tertanggung ttg = elionsManager.selectttg(spaj);
						Map<String, String> map2 = new HashMap<String, String>();
						mapAutoAccept = Common.serializableMap(bacManager.ProsesAutoAccept(spaj, 1, pmg, ttg, user, request,elionsManager));
						map2.put("sts2", "FAILED");
					    map2.put("msg2", mapAutoAccept.get("success").toString());
					    pesanList.add(map2);
					}
				}
			}
			dbpolis = bacManager.selectdataTempAll(bdate,edate,cmp_id,company1,stpolis);
		}
		
		cmd.put("showTab", tabs);
		cmd.put("pesan", pesanList);
		cmd.put("listdist", jenisDist);
		cmd.put("listcompany", listCompany);
		cmd.put("listdist1", jenisDist1);
		cmd.put("listcompany1", listCompany1);
		cmd.put("listdist2", jenisDist2);
		cmd.put("listcompany2", listCompany2);
		cmd.put("dbpolis", dbpolis);
		cmd.put("stpolis", stpolis);
		cmd.put("listreport", jnsReport);
		cmd.put("dbpolis", dbpolis);
		cmd.put("pk", pesanKemenangan);
		return new ModelAndView("uw/excel_upload", cmd);
	}
}
	
