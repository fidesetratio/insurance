package com.ekalife.elions.web.rekruitment;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
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

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentController;
import com.google.gson.Gson;
import com.ibatis.common.resources.Resources;

public class PrintSuratTsrController extends ParentController  {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		 Map<String, Object> cmd = new HashMap<String, Object>();
		 String tabs="1";
		 String err = "";	
		 String pesanKemenangan="";
		 String namaKar, alamat,no_ktp,nama_atasan,jabatan,jabatan_atasan;
		 Integer lde_id;
		 Date awal,akhir;		 
		 User user = (User) request.getSession().getAttribute("currentUser");
		 Integer lus_id = Integer.parseInt(user.getLus_id());
		 int json = ServletRequestUtils.getIntParameter(request, "json", 0);
		 String nama = ServletRequestUtils.getStringParameter(request, "nama", null);
		 cmd.put("user_id", lus_id);
		 cmd.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
	     cmd.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
		 ArrayList<DropDown> jenisDist = new ArrayList<DropDown>();
		 ArrayList<DropDown> daftarNama = new ArrayList<DropDown>();
		 jenisDist.add(new DropDown("00","------DAFTAR NAMA---------"));
		 cmd.put("daftarNama", jenisDist);
		 cmd.put("user", user);
		 cmd.put("recheck", "1");	
			
		 if(json==1 && nama!=null){
			
			 	 daftarNama=Common.serializableList(ajaxManager.selectDaftarNamaPegawai(nama));
				 response.setContentType("application/json");
		    	 PrintWriter out = response.getWriter();
		    	 Gson gson = new Gson();
		    	 out.print(gson.toJson(daftarNama));
		    	 out.close();
			 
		 return null;
	    }else if(json==2 && nama!=null){
	    	 daftarNama=Common.serializableList(ajaxManager.selectDaftarDetaiNamaPegawai(nama));
			 response.setContentType("application/json");
	    	 PrintWriter out = response.getWriter();
	    	 Gson gson = new Gson();
	    	 out.print(gson.toJson(daftarNama));
	    	 out.close();
		 
	    	 return null;
	    }
		 
		 //untuk tab 2
		 List<DropDown> jenisDist2 = new ArrayList<DropDown>();
		 jenisDist2.add(new DropDown("49","DMTM"));
		 jenisDist2.add(new DropDown("63","MALLASSURANCE"));
		 jenisDist2.add(new DropDown("7B","BANCASSURANCE"));
		 

//		 }
		 
		 ArrayList data=new ArrayList();
	    	
		 if(request.getParameter("showReport")!=null){
			tabs="2";
			String bdate = ServletRequestUtils.getStringParameter(request, "bdate1");
	    	String edate = ServletRequestUtils.getStringParameter(request, "edate1");
	    	String dist1=ServletRequestUtils.getStringParameter(request, "dist1",null);
	    	data=Common.serializableList(bacManager.selectDataTsr(bdate,edate,dist1,"1",null));
	    	if(data.size()>0){
	    		
	    		ServletOutputStream sos2 = response.getOutputStream();
	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.surat.tsr") + ".jasper");
	    		
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("tanggal1", bdate);
	    		params.put("tanggal2", edate);
	    		params.put("dist", dist1);
	    		
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
		 
		 if(request.getParameter("showPolicy")!=null){
			tabs="3";
			String bdate = ServletRequestUtils.getStringParameter(request, "bdate2");
	    	String edate = ServletRequestUtils.getStringParameter(request, "edate2");
	    	String dist2=ServletRequestUtils.getStringParameter(request, "dist2",null);
	    	data=Common.serializableList(bacManager.selectDataTsr(bdate,edate,dist2,"1",null));
		 }
		
		 cmd.put("showTab", tabs);
		 cmd.put("listdist1", jenisDist2);
		 cmd.put("listdist2", jenisDist2);
		 cmd.put("dpolis", data);
		 
		 return new ModelAndView("rekruitment/inputtsr", cmd);
	}

}
