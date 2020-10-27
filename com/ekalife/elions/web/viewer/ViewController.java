package com.ekalife.elions.web.viewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Aktivitas;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.User;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.jasper.Report;
import com.ekalife.utils.parent.ParentMultiController;
import com.google.gson.Gson;
import com.ibatis.common.resources.Resources;

import id.co.sinarmaslife.std.model.vo.DropDown;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

public class ViewController  extends ParentMultiController{

	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {

		User currentUser = (User)request.getSession().getAttribute("currentUser");
		String lusID = currentUser.getLus_id();
    	int json = ServletRequestUtils.getIntParameter(request, "json", 0);
    	
//    	String user_bas_admin = "690, 3041, 3179, 113, 5, 55, 133, 2664, 1128, 252";
    	String user_bas_admin = "5, 55, 133, 500, 3177, 3179, 113, 516, 67, 2306, 2141, 2475";
    	List datauser=new ArrayList();
    	

    	//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		
    		String lca = ServletRequestUtils.getStringParameter(request, "cabang");
    		String lwk = ServletRequestUtils.getStringParameter(request, "wakil");
    		String lsrg = ServletRequestUtils.getStringParameter(request, "region");
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		String jn_report = ServletRequestUtils.getStringParameter(request, "jn_report");
    		String lus_id=currentUser.getLus_id();
    		List data=new ArrayList();
    		    		
    		if(jn_report.equals("0")){// summary input by user
//    			Integer all = 0;	
//    			if(user_bas_admin.indexOf(currentUser.getLus_id()) != -1){
//    				all = 1;
//    			}
    			String[] idbas = request.getParameterValues("usbas");
    			String idbas2= idbas[0];
    			
    			if(!idbas2.equals("ALL")){		
	    			if (idbas.length == 1){
	    				idbas2 = idbas[0];
	    			}else{
	        			for (int a=0; a<idbas.length; a++){
	        				idbas2 = idbas2+", "+idbas[a];
	        			}
	    			}
    			}
    			
    			String[] prods = request.getParameterValues("prodbas");
    			String prods2= "'"+prods[0]+"'";
    			
    			if(!prods2.equals("'ALL'")){		
	    			if (prods.length == 1){
	    				prods2 = "'"+prods[0]+"'";
	    			}else{
	        			for (int a=0; a<prods.length; a++){
	        				prods2 = prods2+", '"+prods[a]+"'";
	        			}
	    			}
    			}
    			
//    			data=bacManager.selectreportSummaryInputUser(idbas2, bdate, edate, prods2, "1"); //1=preview
    			data=bacManager.selectreportSummaryInputUser(idbas2, bdate, edate, prods2, "0");
    			
//    			if(all==1){
//    				String user_id = ServletRequestUtils.getStringParameter(request, "seluserBas", "ALL");;
//    				data=uwManager.selectreportSummaryInputUser(user_id, bdate, edate);
//    			}else{
//    				data=uwManager.selectreportSummaryInputUser(lus_id, bdate, edate);
//    			}
    			
    			if(data.size() > 0){ //bila ada data
    	    		
    	    		ServletOutputStream sos2 = response.getOutputStream();
    	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.summary_input_user") + ".jasper");
    	    		
    	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

    	    		Map<String, Object> params = new HashMap<String, Object>();
    	    		params.put("tanggal1", bdate);
    	    		params.put("tanggal2", edate);
    	    		params.put("id", lus_id);

//    	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));

    		    	if(request.getParameter("showXLS") != null){
    		    			Report report;
    		    			report = new Report("Report Summary Input",props.getProperty("report.bas.summary_input_user"),Report.XLS, null);
    		    			
    		    			String id="1 = 1";
    		    			if(!idbas2.equals("ALL")){	
    		    				id = "a.lus_id in ("+idbas2+")";
    		    			}

    		    			String produk = "1=1";
    		            	if(!prods2.equals("'ALL'")){	
    		            		produk = "a.lca_id in (select lca_id from eka.lst_cabang where jalurdis in (select id_dist from EKA.LST_JALUR_DIST where grup_report in ("+prods2+")))";
    		            	}
    		            	
//    		    			Map<String, Comparable> params = new HashMap<String, Comparable>();
    	    	    		params.put("tanggal1", bdate);
    	    	    		params.put("tanggal2", edate);
    	    	    		params.put("id", id);
    	    	    		params.put("produk", produk);
    	    	    		
//    		    			String dest =  "C:\\EkaWeb\\";
    		    			String dest =  "\\\\ebserver\\pdfind\\Report\\temp\\";
    		    			
    		    			
    		    			String nama_file = "Report_Summary_input_"+lusID+".xls";
    		    			
    		    			Connection conn = null;
    		    			
    		    			try{
    		    				conn = this.elionsManager.getUwDao().getDataSource().getConnection();
    		    				JasperUtils.exportReportToXls(report.getReportPath()+".jasper", dest, nama_file, params, conn);
    		    			}finally {
    							this.elionsManager.getUwDao().closeConn(conn);
    						}
    		    			
    		    			File file = new File(dest+"\\"+nama_file);
    		    			
    		    			if (file.exists()){
    		    				
//    		    				com.ekalife.utils.FileUtils.downloadFile("inline", dest, nama_file, response);
    		    				
    		    				FileInputStream in = null;
    		    				ServletOutputStream ouputStream = null;
    		    				try{
    		    					
    		    					response.setHeader("Content-Disposition","inline; filename=" + nama_file);
        		    				response.setHeader("Expires", "0");
        		    				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        		    				response.setHeader("Pragma", "public");
        		    				response.setContentType("application/vnd.ms-excel");
    		    					
        		    				in = new FileInputStream(file);
        		    			    ouputStream = response.getOutputStream();
        		    			    
        		    			    IOUtils.copy(in, ouputStream);
    		    				}finally {
    		                        try {
    		                            if(in != null) {
    		                                 in.close();
    		                            }
    		                            if(ouputStream != null) {
    		                                   ouputStream.flush();
    		                                   ouputStream.close();
    		                            }
    		                            if(file != null) {
    		                            	FileUtils.forceDelete(file);
    		                            }
	    		                      }catch (Exception e) {
	    		                            logger.error("ERROR :", e);
	    		                      }
    		    				}
    		    				
    		    			}
    		    		
    		    		
    		    	}else if(request.getParameter("showPDF") != null){
//    		    		data=bacManager.selectreportSummaryInputUser(idbas2, bdate, edate, prods2, "0");
    		    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));
    		    		JasperExportManager.exportReportToPdfStream(jasperPrint, sos2);
    		    	}
    	    		sos2.close();
        		 }else{ //bila tidak ada data
        			ServletOutputStream sos = response.getOutputStream();
        			sos.println("<script>alert('Tidak ada data');window.close();</script>");
        			sos.close();
        		}
    			
    		}
    		
    		if(jn_report.equals("1")){//summary input by cabang
    			data.clear();
    			data = uwManager.selectreportSummaryInput(lca, lwk, lsrg, bdate, edate, null);
    		
    			if(data.size() > 0){ //bila ada data
    		
    	    		ServletOutputStream sos = response.getOutputStream();
    	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.summary_input") + ".jasper");
    	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

    	    		Map<String, Object> params = new HashMap<String, Object>();
    	    		params.put("tanggal1", bdate);
    	    		params.put("tanggal2", edate);
    	    		params.put("cabang", ServletRequestUtils.getStringParameter(request, "cabang2", "ALL"));
    	    		params.put("wakil", ServletRequestUtils.getStringParameter(request, "wakil2", "ALL"));
    	    		params.put("region", ServletRequestUtils.getStringParameter(request, "region2", "ALL"));

    	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));

    		    	if(request.getParameter("showXLS") != null){
    		    		response.setContentType("application/vnd.ms-excel");
    		            JRXlsExporter exporter = new JRXlsExporter();
    		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
    		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
    		            exporter.exportReport();
    		    	}else if(request.getParameter("showPDF") != null){
    		    		JasperExportManager.exportReportToPdfStream(jasperPrint, sos);
    		    	}
    	    		sos.close();
        		 }else{ //bila tidak ada data
        			ServletOutputStream sos = response.getOutputStream();
        			sos.println("<script>alert('Tidak ada data');window.close();</script>");
        			sos.close();
        		}
    		}
    		
    		if(jn_report.equals("2")){//summary input all
    			data.clear();
    			data = bacManager.selectreportSummaryInputTotal(null, bdate, edate);
    		
    		if(data.size() > 0){ //bila ada data
    		
    	    		ServletOutputStream sos = response.getOutputStream();
    	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.summary_input_total") + ".jasper");
    	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

    	    		Map<String, Object> params = new HashMap<String, Object>();
    	    		params.put("tanggal1", bdate);
    	    		params.put("tanggal2", edate);
    	    		params.put("cabang", ServletRequestUtils.getStringParameter(request, "cabang2", "ALL"));
    	    		params.put("wakil", ServletRequestUtils.getStringParameter(request, "wakil2", "ALL"));
    	    		params.put("region", ServletRequestUtils.getStringParameter(request, "region2", "ALL"));

    	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));

    		    	if(request.getParameter("showXLS") != null){
    		    		response.setContentType("application/vnd.ms-excel");
    		            JRXlsExporter exporter = new JRXlsExporter();
    		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
    		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
    		            exporter.exportReport();
    		    	}else if(request.getParameter("showPDF") != null){
    		    		JasperExportManager.exportReportToPdfStream(jasperPrint, sos);
    		    	}
    	    		sos.close();
        		 }else{ //bila tidak ada data
        			ServletOutputStream sos = response.getOutputStream();
        			sos.println("<script>alert('Tidak ada data');window.close();</script>");
        			sos.close();
        		}
    		}
    		
    		
		
    	   	
    	//halaman depan
    	}else if(json == 0){
//    		datauser=bacManager.selectreportSummaryInputUserBas();
        	Map<String, Object> m = new HashMap<String, Object>();

//        	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
//	    	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
	    	
        	m.put("bdate", defaultDateFormat.format(new Date()));
	    	m.put("edate", defaultDateFormat.format(new Date()));
	    	
	    	if("12".equals(currentUser.getLde_id())){
		    	m.put("listCabang", elionsManager.selectDropDown(
					"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lca_id", "eka.utils.cabang(c.lca_id)", "", "2", 
					"a.lus_id = '3177' and a.lus_id = b.lus_id and b.lar_id = c.lar_id and c.lca_id not in (09) group by c.lca_id"));
		    	m.put("listWakil", elionsManager.selectDropDown(
					"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lwk_id", "eka.utils.kanwil_nama(c.lca_id, c.lwk_id)", "", "2", 
					"a.lus_id = '3177' and a.lus_id = b.lus_id and b.lar_id = c.lar_id and c.lca_id not in (09) group by c.lca_id, c.lwk_id"));
		    	m.put("listRegion", elionsManager.selectDropDown(
					"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lsrg_id", "c.lsrg_nama", "", "2", 
					"a.lus_id = '3177' and a.lus_id = b.lus_id and b.lar_id = c.lar_id and c.lca_id not in (09) group by c.lsrg_id, c.lsrg_nama"));
	    	}else{
	    		m.put("listCabang", elionsManager.selectDropDown(
						"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lca_id", "eka.utils.cabang(c.lca_id)", "", "2", 
						"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id and c.lca_id not in (09) group by c.lca_id"));
			    	m.put("listWakil", elionsManager.selectDropDown(
						"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lwk_id", "eka.utils.kanwil_nama(c.lca_id, c.lwk_id)", "", "2", 
						"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id and c.lca_id not in (09) group by c.lca_id, c.lwk_id"));
			    	m.put("listRegion", elionsManager.selectDropDown(
						"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lsrg_id", "c.lsrg_nama", "", "2", 
						"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id and c.lca_id not in (09) group by c.lsrg_id, c.lsrg_nama"));

	    	}
	    	List<DropDown> userBas = null;
	    	
	    	if("29".equals(currentUser.getLde_id()) || "12".equals(currentUser.getLde_id())){
	    		datauser = uwManager.selectUserBasSummaryInputNew(1);
	    	}else{
	    		datauser = uwManager.selectUserBasSummaryInputNew(0);
	    	}
	    	m.put("userBas", userBas);
	    	
	    	//untuk admin bas
	    	Integer mode = 0;
	    	if(user_bas_admin.indexOf(currentUser.getLus_id()) > -1){
				mode = 1;
			}
	    	
	    	m.put("mode", mode);
	    	m.put("datauser", datauser);
	    	m.put("user_lus_id", currentUser.getLus_id());
	    	m.put("user_name", currentUser.getLus_full_name());
	    	return new ModelAndView("uw/viewer/summary_input", m);
	    	
    	//tarik data wakil dari data cabang (ajax)
    	}else if(json == 1){ 
    		String lca = ServletRequestUtils.getStringParameter(request, "lca", "ALL");
    		List<DropDown> result = elionsManager.selectDropDown(
    			"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lwk_id", "eka.utils.kanwil_nama(c.lca_id, c.lwk_id)", "", "2", 
    			"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id " +(lca.equals("ALL") ?  "" : " and c.lca_id = '" +lca+ "' ")+ " group by c.lca_id, c.lwk_id");
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();

       	//tarik data region dari data cabang dan region (ajax)
    	}else if(json == 2){
    		String lca = ServletRequestUtils.getStringParameter(request, "lca", "ALL");
    		String lwk = ServletRequestUtils.getStringParameter(request, "lwk", "ALL");
    		List<DropDown> result = elionsManager.selectDropDown(
    			"eka.lst_user a, eka.lst_user_admin b, eka.lst_region c", "c.lsrg_id", "c.lsrg_nama", "", "2", 
    			"a.lus_id = '" +currentUser.getLus_id()+ "' and a.lus_id = b.lus_id and b.lar_id = c.lar_id " +(lca.equals("ALL") ? " " : " and c.lca_id = '" +lca+ "' ")+(lwk.equals("ALL") ? " " : " and c.lwk_id = '" +lwk+ "' ")+ " group by c.lsrg_id, c.lsrg_nama");
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();
    	}
    	
		return null;
	}
	

	/*
	protected Connection connection = null;
	protected Connection getConnection() {
		if(this.connection==null) {
			try {
				this.connection = this.elionsManager.getUwDao().getDataSource().getConnection();
			} catch (SQLException e) {
				logger.error("ERROR :", e);
			}
		}
		return this.connection;
	}
	*/

}
