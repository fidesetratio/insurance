package com.ekalife.elions.web.bas;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Followup;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.elions.process.Sequence;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentMultiController;
import com.google.gson.Gson;
import com.ibatis.common.resources.Resources;

/**
 * Form Inputan Followup Premi Lanjutan oleh BAS (untuk billing2 outstanding yg belum di FU oleh cabang)
 * Helpdesk #20963
 * 
 * @author Yusuf
 * @since Aug 19, 2011 (8:45:02 AM)
 *
 */
public class FollowUpPremiLanjutanMultiController extends ParentMultiController{
       
       protected Sequence sequence;

       public Sequence getSequence() {
              return sequence;
       }

       public void setSequence(Sequence sequence) {
              this.sequence = sequence;
       }

       /**
        * Method khusus untuk download file attachment
        *  
        * @author Yusuf
        * @since 19 Sep 2011
        */
       public ModelAndView download(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
              String fu = ServletRequestUtils.getRequiredStringParameter(request, "fu");
              String reg_spaj = fu.substring(0, fu.indexOf("~"));
              String tahun_ke = fu.substring(fu.indexOf("~")+1, fu.indexOf("`"));
              String premi_ke = fu.substring(fu.indexOf("`")+1);

              File file = new File(uwManager.selectAttachmentFollowupBilling(tahun_ke, premi_ke, reg_spaj));
              
              if(file.exists()){
                     ServletOutputStream out = null;
                     InputStream in = null;
                     try {
                           out = response.getOutputStream();
                           in = new FileInputStream(file);
                           String mimeType = Common.getMimeType(file);
                           byte[] bytes = new byte[1024];
                           int bytesRead;

                           response.setContentType(mimeType);
                           response.setHeader( "Content-Disposition", "attachment;filename=" + file.getName());

                           while ((bytesRead = in.read(bytes)) != -1) {
                               out.write(bytes, 0, bytesRead);
                           }
                     } catch (IOException e) {
                           logger.error("ERROR :", e);
                     } finally{
                           in.close();
                           out.close();
                     }
              }

              return null;
       }
       
       /**
        * Method khusus untuk Save To Excell
        *  
        * @author Ryan
        * @since 30 Aug 2016
        */
       public ModelAndView savetoexcell(HttpServletRequest request, HttpServletResponse response) throws Exception {
              User currentUser = (User) request.getSession().getAttribute("currentUser");
              String tipe = ServletRequestUtils.getStringParameter(request, "t", "");         	 
         	  String krirep = ServletRequestUtils.getRequiredStringParameter(request, "krirep");
         	  
              //List<Followup> result=new ArrayList<Followup>();
              ArrayList result = new ArrayList();
              ArrayList resultEFUPL =  new ArrayList();
              ArrayList resultSummary =  new ArrayList();
              
              if(tipe.equals("report")){
                     result = Common.serializableList(bacManager.selectReportFollowupBilling2(
                                  ServletRequestUtils.getRequiredStringParameter(request, "begdate"), 
                                  ServletRequestUtils.getRequiredStringParameter(request, "enddate"), 
                                  null, ServletRequestUtils.getRequiredStringParameter(request, "stfu3"), 
                                  ServletRequestUtils.getRequiredStringParameter(request, "cabang"), 
                                  ServletRequestUtils.getRequiredStringParameter(request, "admin"),
                                  ServletRequestUtils.getRequiredStringParameter(request, "kat"),
                                  ServletRequestUtils.getRequiredStringParameter(request, "jn_tgl")));

              }else if(tipe.equals("report_peruser")){
            	  
		            	 String rep = ServletRequestUtils.getRequiredStringParameter(request, "rep");	
		            	 
		             	 if (rep.equals("2")){
		             		 
		             		 result = Common.serializableList(bacManager.selectReportFollowupBillingPerUser2(
		                              ServletRequestUtils.getRequiredStringParameter(request, "begdate"), 
		                              ServletRequestUtils.getRequiredStringParameter(request, "enddate"), 
		                              currentUser.getLus_id(), ServletRequestUtils.getRequiredStringParameter(request, "stfu3"), 
		                              "ALL", //cabang pusat saja
		                              ServletRequestUtils.getRequiredStringParameter(request, "admin"),
		                              ServletRequestUtils.getRequiredStringParameter(request, "kat"),
		                              rep, 
		                              ServletRequestUtils.getRequiredStringParameter(request, "jn_tgl")));
		
		             	 }else{
		            	  	
		                     result = Common.serializableList(bacManager.selectReportFollowupBillingPerUser2(
		                           ServletRequestUtils.getRequiredStringParameter(request, "begdate"), 
		                           ServletRequestUtils.getRequiredStringParameter(request, "enddate"), 
		                           currentUser.getLus_id(), ServletRequestUtils.getRequiredStringParameter(request, "stfu3"), 
		                           ServletRequestUtils.getRequiredStringParameter(request, "cabang"), 
		                           ServletRequestUtils.getRequiredStringParameter(request, "admin"),
		                           ServletRequestUtils.getRequiredStringParameter(request, "kat"),
		                           ServletRequestUtils.getRequiredStringParameter(request, "rep"),
		                           ServletRequestUtils.getRequiredStringParameter(request, "jn_tgl")));
		             	 }

              }else if(tipe.equals("reportFUPL")){
            	  
            	  String aging = request.getParameter("aging");
                  Integer vaging = (aging == null ? null : Integer.valueOf(aging));
                  String jn = ServletRequestUtils.getStringParameter(request, "jn","0");
                  String no_polis = request.getParameter("no_polis");
                  String spaj = "";
                  if(no_polis==null){
                        spaj = null;
                  }else{
                        spaj = elionsManager.selectSpajFromPolis(no_polis);
                  }
                               
                  //String jen = ServletRequestUtils.getRequiredStringParameter(request, "jenis");
                  
                  resultEFUPL = Common.serializableList(bacManager.selectFollowupBilling2(
                               ServletRequestUtils.getRequiredStringParameter(request, "jenis"),
                               vaging,
                               request.getParameter("begdate"), 
                                request.getParameter("enddate"), 
                               currentUser.getLus_id(), 
                               ServletRequestUtils.getRequiredStringParameter(request, "stfu"), null, null, jn, spaj));
//                  resultEFUPL = Common.serializableList(bacManager.selectReportFollowupBillingPerUser2(
//                          "30/08/2016", 
//                          "29/09/2016", 
//                          "2475", "2", 
//                          "ALL", 
//                          "ALL",
//                          "ALL",
//                          "0","0"));


             }else if(tipe.equals("report_summary")){ 
	            	 		             
		             String rep = ServletRequestUtils.getRequiredStringParameter(request, "rep");
         	 
		         	 if (rep.equals("2")){
		         		 
		         		 resultSummary = Common.serializableList(bacManager.selectReportFollowupBillingPerUserCount(
		                          ServletRequestUtils.getRequiredStringParameter(request, "begdate"), 
		                          ServletRequestUtils.getRequiredStringParameter(request, "enddate"), 
		                          currentUser.getLus_id(), ServletRequestUtils.getRequiredStringParameter(request, "stfu3"), 
		                          "ALL", //cabang pusat saja
		                          ServletRequestUtils.getRequiredStringParameter(request, "admin"),
		                          ServletRequestUtils.getRequiredStringParameter(request, "kat"),
		                          rep, 
		                          ServletRequestUtils.getRequiredStringParameter(request, "jn_tgl")));
		
		         	 }else{
		        	  	
		         		resultSummary = Common.serializableList(bacManager.selectReportFollowupBillingPerUserCount(
		                       ServletRequestUtils.getRequiredStringParameter(request, "begdate"), 
		                       ServletRequestUtils.getRequiredStringParameter(request, "enddate"), 
		                       currentUser.getLus_id(), ServletRequestUtils.getRequiredStringParameter(request, "stfu3"), 
		                       ServletRequestUtils.getRequiredStringParameter(request, "cabang"), 
		                       ServletRequestUtils.getRequiredStringParameter(request, "admin"),
		                       ServletRequestUtils.getRequiredStringParameter(request, "kat"),
		                       ServletRequestUtils.getRequiredStringParameter(request, "rep"),
		                       ServletRequestUtils.getRequiredStringParameter(request, "jn_tgl")));
		         	 }
         	 }
              
              if(result.size() > 0){ //bila ada data
                     ServletOutputStream sos = response.getOutputStream();
                     File sourceFile = null;
                     
                     if(krirep.equals("0")){ //Report FU dengan keterangan FU                   
                    	 sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.reportFollowupPremiLanj") + ".jasper");
                     }else{ //Report FU tanpa keterangan FU
                    	 sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.reportFollowupPremiLanj2") + ".jasper");
                     }
                     JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
                     Map<String, Object> params = new HashMap<String, Object>();
                     params.put("bdate", ServletRequestUtils.getRequiredStringParameter(request, "begdate"));
                     params.put("edate", ServletRequestUtils.getRequiredStringParameter(request, "enddate"));
                     JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(result));
                     response.setContentType("application/vnd.ms-excel");
                     JRXlsExporter exporter = new JRXlsExporter();
                     exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                     exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
                     exporter.exportReport();
                     if(sos!=null){
                           sos.flush();
                           sos.close();
                     }
              }else if(resultEFUPL.size() > 0){ //bila ada data Excel FUPL
            	  ServletOutputStream sos = response.getOutputStream();
                  File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.reportFollowupPremiLanjUtama") + ".jasper");
                  JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
                  Map<String, Object> params = new HashMap<String, Object>();
                  params.put("bdate", ServletRequestUtils.getRequiredStringParameter(request, "begdate"));
                  params.put("edate", ServletRequestUtils.getRequiredStringParameter(request, "enddate"));
                  JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(resultEFUPL));
                  response.setContentType("application/vnd.ms-excel");
                  JRXlsExporter exporter = new JRXlsExporter();
                  exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                  exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
                  exporter.exportReport();
                  if(sos!=null){
                        sos.flush();
                        sos.close();
                  }
              }else if(resultSummary.size() > 0){ //bila ada data Excel Summary 
            	  ServletOutputStream sos = response.getOutputStream();
                  File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.reportFollowupPremiLanjSummary") + ".jasper");
                  JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
                  Map<String, Object> params = new HashMap<String, Object>();
                 
                  String jn_tgl = ServletRequestUtils.getRequiredStringParameter(request, "jn_tgl");
 	             
		             if (jn_tgl.contains("0")){
		            	 jn_tgl = "Tagihan";
		             }else{
		            	 jn_tgl = "Follow Up";
		             }
		             
		          params.put("bdate", ServletRequestUtils.getRequiredStringParameter(request, "begdate"));
	              params.put("edate", ServletRequestUtils.getRequiredStringParameter(request, "enddate"));
	              params.put("jn_tgl", jn_tgl);   
                  JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(resultSummary));
                  response.setContentType("application/vnd.ms-excel");
                  JRXlsExporter exporter = new JRXlsExporter();
                  exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                  exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
                  exporter.exportReport();
                  if(sos!=null){
                        sos.flush();
                        sos.close();
                  }
              }else{ //bila tidak ada data
                     ServletOutputStream sos = response.getOutputStream();
                     sos.println("<script>alert('Tidak ada data');window.close();</script>");
                     sos.close();
              }
              
              return null;
       }
       
       /**
        * Method khusus untuk return data dalam bentuk JSON (untuk ajax)
        * 
        * 2012-06-22 (Canpri) Penambahan kondisi untuk menarik data auto debet
        * 
        * @author Yusuf
        * @since 19 Aug 2011
        * 
        * 
        */
       public ModelAndView json(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
              User currentUser = (User) request.getSession().getAttribute("currentUser");
              String tipe = ServletRequestUtils.getStringParameter(request, "t", "");
              
              Object result = null;
              
              if(tipe.equals("followup")){
                     String aging = request.getParameter("aging");
                     Integer vaging = (aging == null ? null : Integer.valueOf(aging));
                     String jn = ServletRequestUtils.getStringParameter(request, "jn","0");
                     String no_polis = request.getParameter("no_polis");
                     String spaj = "";
                     if(no_polis==null){
                           spaj = null;
                     }else{
                           spaj = elionsManager.selectSpajFromPolis(no_polis);
                     }
                                  
                     //String jen = ServletRequestUtils.getRequiredStringParameter(request, "jenis");
                     String lus_id = currentUser.getLus_id();
                     if (lus_id.equals("3041") || lus_id.equals("2475") || lus_id.equals("4180") ||  lus_id.equals("690")  ||  lus_id.equals("787") ||  lus_id.equals("4852") ||  lus_id.equals("6579")){
                    	 lus_id = null;
                     }
                     
                     result = uwManager.selectFollowupBilling(
                                  ServletRequestUtils.getRequiredStringParameter(request, "jenis"),
                                  vaging,
                                  request.getParameter("begdate"), 
                                   request.getParameter("enddate"), 
                                  lus_id, 
                                  ServletRequestUtils.getRequiredStringParameter(request, "stfu"), null, null, jn, spaj);
                     /*
                     if(request.getParameter("aging") != null){ //berdasarkan aging
                           result = uwManager.selectFollowupBilling(
                                         ServletRequestUtils.getRequiredIntParameter(request, "aging"), 
                                         currentUser.getLus_id(), ServletRequestUtils.getRequiredStringParameter(request, "stfu"));
                     }else{ //berdasarkan tgl tagihan
                           result = uwManager.selectFollowupBilling(
                                         ServletRequestUtils.getRequiredStringParameter(request, "begdate"), 
                                         ServletRequestUtils.getRequiredStringParameter(request, "enddate"), 
                                         currentUser.getLus_id(), ServletRequestUtils.getRequiredStringParameter(request, "stfu"), null, null);
                     }
                     */
                     
              }else if(tipe.equals("detail")){
                     String fu = ServletRequestUtils.getRequiredStringParameter(request, "fu");
                     String reg_spaj = fu.substring(0, fu.indexOf("~"));
                     String tahun_ke = fu.substring(fu.indexOf("~")+1, fu.indexOf("`"));
                     String premi_ke = fu.substring(fu.indexOf("`")+1);
                     
                     result = uwManager.selectDetailPolisFollowupBilling(tahun_ke, premi_ke, reg_spaj);
                     
              }else if(tipe.equals("payment")){
                     String fu = ServletRequestUtils.getRequiredStringParameter(request, "fu");
                     String reg_spaj = fu.substring(0, fu.indexOf("~"));
                     
                     result = uwManager.selectDetailPaymentFollowupBilling(reg_spaj);

              }else if(tipe.equals("hist")){
                     String fu = ServletRequestUtils.getRequiredStringParameter(request, "fu");
                     String reg_spaj = fu.substring(0, fu.indexOf("~"));
                     String tahun_ke = fu.substring(fu.indexOf("~")+1, fu.indexOf("`"));
                     String premi_ke = fu.substring(fu.indexOf("`")+1);
                     
                     result = uwManager.selectHistoryFollowupBilling(tahun_ke, premi_ke, reg_spaj);

              }else if(tipe.equals("admin")){
                     String lca = ServletRequestUtils.getRequiredStringParameter(request, "lca");
                     
                     if(lca.equals("ALL")) {
                           result = elionsManager.selectDropDown("EKA.LST_USER a, EKA.LST_CABANG b", "LUS_ID", "LUS_LOGIN_NAME", "LCA_NAMA", "LCA_NAMA, LUS_LOGIN_NAME", 
                                         "a.lca_id = b.lca_id and a.lus_active = 1 and a.lus_bas = 1");
                     }else{
                           result = elionsManager.selectDropDown("EKA.LST_USER a, EKA.LST_CABANG b", "LUS_ID", "LUS_LOGIN_NAME", "LCA_NAMA", "LCA_NAMA, LUS_LOGIN_NAME", 
                                         "a.lca_id = b.lca_id and a.lca_id = '"+lca+"' and a.lus_active = 1 and a.lus_bas = 1");
                     }
                     
              }else if(tipe.equals("kontrol")){
                     String aging = request.getParameter("aging");
                     Integer vaging = (aging == null ? null : Integer.valueOf(aging));
                     String jn = ServletRequestUtils.getStringParameter(request, "jn","0");
                     
                     result = uwManager.selectFollowupBilling(
                                  "all", vaging,
                                  ServletRequestUtils.getRequiredStringParameter(request, "begdate"), 
                                  ServletRequestUtils.getRequiredStringParameter(request, "enddate"), 
                                  null, ServletRequestUtils.getRequiredStringParameter(request, "stfu2"), 
                                  ServletRequestUtils.getRequiredStringParameter(request, "cabang"), 
                                  ServletRequestUtils.getRequiredStringParameter(request, "admin"),jn, null);
                     
              }else if(tipe.equals("report")){
                     result = uwManager.selectReportFollowupBilling(
                                  ServletRequestUtils.getRequiredStringParameter(request, "begdate"), 
                                  ServletRequestUtils.getRequiredStringParameter(request, "enddate"), 
                                  null, ServletRequestUtils.getRequiredStringParameter(request, "stfu3"), 
                                  ServletRequestUtils.getRequiredStringParameter(request, "cabang"), 
                                  ServletRequestUtils.getRequiredStringParameter(request, "admin"),
                                  ServletRequestUtils.getRequiredStringParameter(request, "kat"),
                                  ServletRequestUtils.getRequiredStringParameter(request, "jn_tgl"));
                     
              }else if(tipe.equals("report_peruser")){
            	  
            	 String rep = ServletRequestUtils.getRequiredStringParameter(request, "rep");
            	             	 
            	 if (rep.equals("2")){
            		 
            		 result = uwManager.selectReportFollowupBillingPerUser(
                             ServletRequestUtils.getRequiredStringParameter(request, "begdate"), 
                             ServletRequestUtils.getRequiredStringParameter(request, "enddate"), 
                             currentUser.getLus_id(), ServletRequestUtils.getRequiredStringParameter(request, "stfu3"), 
                             "ALL", //cabang pusat saja
                             ServletRequestUtils.getRequiredStringParameter(request, "admin"),
                             ServletRequestUtils.getRequiredStringParameter(request, "kat"),
                             rep, 
                             ServletRequestUtils.getRequiredStringParameter(request, "jn_tgl"));

            	 }else{
            		 result = uwManager.selectReportFollowupBillingPerUser(
                             ServletRequestUtils.getRequiredStringParameter(request, "begdate"), 
                             ServletRequestUtils.getRequiredStringParameter(request, "enddate"), 
                             currentUser.getLus_id(), ServletRequestUtils.getRequiredStringParameter(request, "stfu3"), 
                             ServletRequestUtils.getRequiredStringParameter(request, "cabang"), 
                             ServletRequestUtils.getRequiredStringParameter(request, "admin"),
                             ServletRequestUtils.getRequiredStringParameter(request, "kat"),
                             ServletRequestUtils.getRequiredStringParameter(request, "rep"),
                             ServletRequestUtils.getRequiredStringParameter(request, "jn_tgl"));
            	 }
            	   
              }else if(tipe.equals("kat")){
                     String kat = ServletRequestUtils.getRequiredStringParameter(request, "kat");
                     String fu = ServletRequestUtils.getRequiredStringParameter(request, "fu");
                     String reg_spaj = fu.substring(0, fu.indexOf("~"));
                     
                     //req Ibu June (//lsfu_id in (17,32,6,18,7,30) di tambahkan email ke channel)
                     if(kat.equals("17")|| kat.equals("32") || kat.equals("6")||kat.equals("18")||kat.equals("7") || kat.equals("30") )
                     {
                    	 result = bacManager.selectDropDownFU(reg_spaj,kat);  
                     }else{
                    	 result = elionsManager.selectDropDown("EKA.LST_FOLLOWUP", "LSFU_EMAIL", "LSFU_STATUS", "", "1", 
                                 "lsfu_id = '" + kat + "'");     
                     }
                                      
              }else if(tipe.equals("tahapan")){
                     String fu = ServletRequestUtils.getRequiredStringParameter(request, "fu");
                     String reg_spaj = fu.substring(0, fu.indexOf("~"));
                     
                     result = elionsManager.selectDropDown("EKA.MST_TAHAPAN", "MSTAH_NO_TAHAPAN", "REG_SPAJ", "", "1", 
                                  "reg_spaj = '" + reg_spaj + "' and mstah_tgl_konfirmasi is null");
              
              }else if(tipe.equals("autodebet")){
                     String fu = ServletRequestUtils.getRequiredStringParameter(request, "fu");
                     String reg_spaj = fu.substring(0, fu.indexOf("~"));
                     
                     result = uwManager.selectAutoDebetFollowupBilling(reg_spaj);
              }else if(tipe.equals("billing")){
                     String fu = ServletRequestUtils.getRequiredStringParameter(request, "fu");
                     String reg_spaj = fu.substring(0, fu.indexOf("~"));
                     
                     result = uwManager.selectViewBillingFollowup(reg_spaj);
              }else if(tipe.equals("viewtahapan")){
                     String fu = ServletRequestUtils.getRequiredStringParameter(request, "fu");
                     String reg_spaj = fu.substring(0, fu.indexOf("~"));
                     
                     result = uwManager.selectViewTahapanFollowup(reg_spaj);
              }else if(tipe.equals("viewsimpanan")){
                     String fu = ServletRequestUtils.getRequiredStringParameter(request, "fu");
                     String reg_spaj = fu.substring(0, fu.indexOf("~"));
                     
                     result = uwManager.selectViewSimpananFollowup(reg_spaj);
              }else if(tipe.equals("viewcallsummary")){
                     String fu = ServletRequestUtils.getRequiredStringParameter(request, "fu");
                     String reg_spaj = fu.substring(0, fu.indexOf("~"));
                     
                     result = uwManager.selectViewCallSummary(reg_spaj);
              }else if(tipe.equals("detailpowersave")){
                     String fu = ServletRequestUtils.getRequiredStringParameter(request, "fu");
                     String reg_spaj = fu.substring(0, fu.indexOf("~"));
                     
                     result = bacManager.selectDetailPowerSaveFU(reg_spaj);
              }else if(tipe.equals("detailstablelink")){
                     String fu = ServletRequestUtils.getRequiredStringParameter(request, "fu");
                     String reg_spaj = fu.substring(0, fu.indexOf("~"));
                     
                     result = bacManager.selectDetailStableLinkFU(reg_spaj);
              }
              
              response.setContentType("application/json");
              PrintWriter out = response.getWriter();
              Gson gson = new Gson();
              out.print(gson.toJson(result));
              out.close();
              
              return null;
       }
       
       /**
        * Main method
        * http://yusufxp/E-Lions/bas/fu_premi.htm?window=main
        * 
        * @author Yusuf
        * @since 19 Aug 2011
        */    
       public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
              User currentUser = (User) request.getSession().getAttribute("currentUser");
       
              //
              String begdate = ServletRequestUtils.getStringParameter(request, "begdate", defaultDateFormat.format(elionsManager.selectSysdate(0)));
              String enddate = ServletRequestUtils.getStringParameter(request, "enddate", defaultDateFormat.format(elionsManager.selectSysdate(30)));
              
              //Reference Data
              //0 = Outstanding (Sudah Followup), 1 = Closed, 2 = All, 3 = Belum Followup

              //semua, termasuk yg belum di fu (outstanding), yg sudah di fu (followup) dan yang sudah selesai (closed)
              List<DropDown> listStatus = new ArrayList<DropDown>();
              listStatus.add(new DropDown("2", "All")); 
              listStatus.add(new DropDown("3", "Belum Followup")); 
              listStatus.add(new DropDown("0", "Outstanding (Sudah Followup)"));
              listStatus.add(new DropDown("1", "Closed"));

              //untuk halaman report, cukup status followup dan closed saja
              List<DropDown> listStatus2 = new ArrayList<DropDown>(); 
              listStatus2.add(new DropDown("2", "All"));
              listStatus2.add(new DropDown("0", "Outstanding (Sudah Followup)"));
              listStatus2.add(new DropDown("1", "Closed"));

              //untuk halaman report, Kriteria Report
              List<DropDown> listKriteriaRep = new ArrayList<DropDown>(); 
              listKriteriaRep.add(new DropDown("0", "Report FU dengan keterangan FU"));
              listKriteriaRep.add(new DropDown("1", "Report FU tanpa keterangan FU"));
              
              //jenis  : gagaldebet, jatuhtempo, all
              List<DropDown> listJenis = new ArrayList<DropDown>();
              listJenis.add(new DropDown("all", "All"));
              listJenis.add(new DropDown("jatuhtempo", "Jatuh Tempo")); 
              listJenis.add(new DropDown("gagaldebet", "Gagal Debet"));               
              listJenis.add(new DropDown("list_aging", "List Aging")); 
              listJenis.add(new DropDown("gagaldebet_efc", "Gagal Debet (EFC)")); 
              listJenis.add(new DropDown("visa_camp", "Visa Camp"));
              listJenis.add(new DropDown("powersave", "Power Save")); 
              listJenis.add( new DropDown("stablelink", "Stable Link")); 

              //List<DropDown> listKat = elionsManager.selectDropDown("EKA.LST_FOLLOWUP", "LSFU_ID", "LSFU_DESC", "", "LSFU_DESC", "LSFU_ID <> 99");
              List<DropDown> listKat = elionsManager.selectDropDown("EKA.LST_FOLLOWUP", "LSFU_ID", "LSFU_DESC", "", "LSFU_DESC", "LSFU_ID not in (99,5,20,21,24,26,16,22)");//permintaan June beberapa tidak ditampilkan
              List<DropDown> listKat2 = elionsManager.selectDropDown("EKA.LST_FOLLOWUP", "LSFU_ID", "LSFU_DESC", "", "LSFU_DESC", "");

              List<DropDown> listCab = new ArrayList<DropDown>();
              listCab.add(new DropDown("ALL", "ALL"));
              listCab.addAll(elionsManager.selectDropDown("EKA.LST_CABANG", "LCA_ID", "LCA_NAMA", "", "LCA_NAMA", currentUser.getLus_bas().intValue() == 1 ? "lca_id = '" + currentUser.getLca_id() + "'" : ""));
              
              
              //Model Object
              Map<String, Object> cmd = new HashMap<String, Object>();
              cmd.put("begdate", begdate);
              cmd.put("enddate", enddate);
              cmd.put("begdate2", begdate);
              cmd.put("enddate2", enddate);
              cmd.put("listStatus", listStatus);
              cmd.put("listStatus2", listStatus2);
              cmd.put("listJenis", listJenis);
              cmd.put("listKat", listKat);
              cmd.put("listKat2", listKat2);
              cmd.put("listCab", listCab);
              cmd.put("listKriteriaRep", listKriteriaRep);
              
              
              int mode = 0;
              
              //User menekan tombol submit di halaman INPUT
              int submitType = ServletRequestUtils.getIntParameter(request, "submitType", 0);
              if(submitType != 0){
                     DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
                     String fu = ServletRequestUtils.getRequiredStringParameter(request, "spajThnPremi");
                     String reg_spaj = fu.substring(0, fu.indexOf("~"));
                     String tahun_ke = fu.substring(fu.indexOf("~")+1, fu.indexOf("`"));
                     String premi_ke = fu.substring(fu.indexOf("`")+1);
                     int kategori = ServletRequestUtils.getRequiredIntParameter(request, "kategori");
                     int lsfustatus = ServletRequestUtils.getRequiredIntParameter(request, "lsfustatus");
                     String keterangan = ServletRequestUtils.getStringParameter(request, "keterangan","");
                     String polis = uwManager.selectNoPolisFromSpaj(reg_spaj);
                     String emailto = ServletRequestUtils.getStringParameter(request, "emailto", "");
                     String pemegang = ServletRequestUtils.getStringParameter(request, "pemegang", "");
                     String remindDate = ServletRequestUtils.getStringParameter(request, "remindDate", "");
                     String waktu = ServletRequestUtils.getStringParameter(request, "waktu", "00:00:00");
                     
                     keterangan = keterangan + " (waktu  "+waktu+")";
                     
                     
                     Followup followup = new Followup(reg_spaj, polis, Integer.parseInt(tahun_ke), Integer.parseInt(premi_ke), 
                                  kategori, currentUser.getLus_id(), keterangan, lsfustatus);
                     followup.setPemegang(pemegang);
                     followup.setTgl_proses(defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
                     
                     
                     //bind file upload
                     Upload upload = new Upload();
                     ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
                     binder.bind(request);

                     String result = "";
                     
                     if(!remindDate.equals("")){
                           followup.setReminder_date(defaultDateFormat.parse(remindDate));
                     }else{
                           followup.setReminder_date(null);
                     }
                     
                     if((kategori==5 || kategori==11) && remindDate.equals("")){//Jika kategori akan melakukan pembayaran
                            result = "Silakan Diinput reminder Date terlebih dahulu.";
                     }
                     
                     if(submitType == 1){ //Save only
                           //DISABLED by Request Pak Edi Sartana 13 Sep 2011 (semua SAVE pasti EMAIL)
                           //boolean sukses = uwManager.insertFollowupBilling(followup, currentUser.getEmail(), upload.getFile1(), false);
                           //if(sukses) result = "Data berhasil disimpan.";
                     }else if(submitType == 2){ //Save + Email
                           //permintaan legal, sebelumnya untuk email yang dikirim ke audit diganti dikirim ke legal dan cc ke audit 07/10/2013
                           boolean sukses = uwManager.insertFollowupBilling(followup, currentUser, upload.getFile1(), true, emailto);
                           //boolean sukses = true;

                           //cek ada topup atau tidak
                           /*Integer topup_premi_ke = uwManager.selectFollowupPremiKeTopup(followup);
                           if(topup_premi_ke==null)topup_premi_ke=0;
                           if(topup_premi_ke>0){
                                  Followup fol = new Followup();
                                  fol = followup;
                                  fol.setMsbi_premi_ke(topup_premi_ke);
                                  boolean sukses2 = uwManager.insertFollowupBilling(fol, currentUser, upload.getFile1(), true, emailto);
                           }*/
                           
                           if(sukses) {
                                  result = "Data berhasil disimpan dan diemail ke bagian terkait (" + emailto + ").";
                           }else{
                                  result = "Data TIDAK berhasil disimpan. Harap hubungi IT";
                           }
                     }else if(submitType == 3){
                           //masih belum setting email tujuan
                           String mail = ServletRequestUtils.getStringParameter(request, "e_mail", "");
                           String tagihan_premi = ServletRequestUtils.getStringParameter(request, "jumlah", "");
                           String tgl_tagihan = ServletRequestUtils.getStringParameter(request, "tgl_tagihan", "");
                           String produk = ServletRequestUtils.getStringParameter(request, "nm_produk", "");
                           String telp = "";
                           List<String> tp = new ArrayList();
                           
                           String jt_tempo = tgl_tagihan.substring(0, tgl_tagihan.indexOf(" "));
                           
                           for(int i=0;i<4;i++){
                                  int j = i+1;
                                  String a = ServletRequestUtils.getStringParameter(request, "telp"+j, "");
                                  if(a.equals("") || a.equals(" ")){
                                  }else{
                                         //tp = new String[]{a};
                                         tp.add(a);
                                  }
                           }
                           
                           if(!tp.isEmpty()){
                                  for(int i=0;i<tp.size();i++){
                                         if(i==tp.size()-1){
                                                telp += tp.get(i);
                                         }else{
                                                telp += tp.get(i)+", ";
                                         }
                                  }
                           }
                           
                           String from = "cs@sinarmasmsiglife.co.id";
                           StringBuffer pesan = new StringBuffer();
                           String[] to = new String[]{mail};
                           String[] cc = new String[]{};
                           String[] bcc = new String[]{"TeamLeaderCS-FL@sinarmasmsiglife.co.id"};
                           String subject = "Billing Info PT. Asuransi Jiwa SinarmasMSIG";
                           
                           String tgl_sent = defaultDateFormat.format(elionsManager.selectSysdate());
                           
                           
                           //pesan
                           pesan.append("Nasabah yang terhormat Bpk/Ibu "+pemegang+"  ,<br><br>");
                           pesan.append("Kami telah mengubungi Bpk/Ibu ke nomor "+telp+" pada tanggal "+tgl_sent+" tetapi tidak berhasil kami hubungi.<br>");
                           pesan.append("Berikut kami informasikan tagihan premi jatuh tempo dengan data sebagai berikut :<br><br>");
                           pesan.append("Nomor Polis\t\t : "+FormatString.nomorPolis(polis)+"<br>");
                           pesan.append("Produk\t\t\t : "+produk+"<br>");
                           pesan.append("Total Tagihan Premi\t : "+tagihan_premi+"<br>");
                           pesan.append("Periode Pembayaran\t : "+tgl_tagihan+"<br>");
                           pesan.append("Tanggal Jatuh Tempo\t : "+jt_tempo+"<br><br>");
                           pesan.append("Apabila Bpk/Ibu sudah melakukan pembayaran premi atau apabila ada perubahan nomor telepon atau ada hal-hal yang perlu ditanyakan mohon dapat melakukan reply email ke ");
                           pesan.append("<a href='mailto:cs@sinarmasmsiglife.co.id'>cs@sinarmasmsiglife.co.id</a>, atau menghubungi  Customer Service kami, di telepon 021-50609999, 021-26508300, atau Layanan Bebas Pulsa 0-800-1401217");
                           pesan.append("<br><br>Terima kasih");
                           String message = pesan.toString();
                           
                           try{
                                  EmailPool.send("Followup billing E-Lions", 1, 1, 0, 0, null, 0, 0, elionsManager.selectSysdate(), null, true, from, to, null, bcc, subject, message, null, null);
                                  result = "E-mail berhasil dikirim ke nasabah!";
                           }catch(Exception e){
                                  e.getLocalizedMessage();
                                  result = "Error!E-mail gagal dikirim!";
                           }
                     }
                     
                     //
                     mode = 1;
                     if(mode==1){
                           String begdate2 = ServletRequestUtils.getStringParameter(request, "begdate2");
                           String enddate2 = ServletRequestUtils.getStringParameter(request, "enddate2");
                           String begdate3 = ServletRequestUtils.getStringParameter(request, "begdate3");
                           String enddate3 = ServletRequestUtils.getStringParameter(request, "enddate3");
                           String pilih2 = ServletRequestUtils.getStringParameter(request, "pilih2");
                           String aging2 = ServletRequestUtils.getStringParameter(request, "aging2","");
                           String status2 = ServletRequestUtils.getStringParameter(request, "status2");
                           String jenis2 = ServletRequestUtils.getStringParameter(request, "jenis2");
                           String followup2 = ServletRequestUtils.getStringParameter(request, "followup2");
                           String followup3 = ServletRequestUtils.getStringParameter(request, "followup3");
                           
                           //followup3 = followup3.substring(followup3.indexOf(" "), followup3.length());
                           
                           cmd.put("mode", mode);
                           cmd.put("pesan", result);
                           cmd.put("begdate", begdate2);
                           cmd.put("enddate", enddate2);
                           cmd.put("begdate2", begdate3);
                           cmd.put("enddate2", enddate3);
                           cmd.put("pilih2", pilih2);
                           cmd.put("aging2", aging2);
                           cmd.put("status2", status2);
                           cmd.put("jenis2", jenis2);
                           cmd.put("followup2", followup2);
                           cmd.put("followup3", followup3);
                     }
                     //end
                     
                     /*PrintWriter out = response.getWriter();
                     out.println(result);
                     out.close();*/
                     /*return null;*/
              }
              
              //User menekan tombol submit di halaman KONTROL
              int submitType2 = ServletRequestUtils.getIntParameter(request, "submitType2", 0);
              if(submitType2 != 0){
                     String cabang        = ServletRequestUtils.getRequiredStringParameter(request, "cabang");
                     String admin = ServletRequestUtils.getRequiredStringParameter(request, "admin");
                     String[] polis       = ServletRequestUtils.getStringParameters(request, "polis");
                     String[] pp          = ServletRequestUtils.getStringParameters(request, "pp");
                     String[] spaj        = ServletRequestUtils.getStringParameters(request, "spaj");
                     int[] thnke   = ServletRequestUtils.getIntParameters(request, "thnke");
                     int[] premike        = ServletRequestUtils.getIntParameters(request, "premike");
                     String[] ket = ServletRequestUtils.getStringParameters(request, "ket");

                     String result = "Data TIDAK berhasil disimpan. Harap hubungi IT";

                     if(submitType2 == 1){ //Save
                           boolean sukses = uwManager.insertKontrolFollowupBilling(currentUser, cabang, admin, polis, pp, spaj, thnke, premike, ket);
                           if(sukses) result = "Data berhasil disimpan.";
                     }
                     
                     PrintWriter out = response.getWriter();
                     out.println(result);
                     out.close();
                     return null;
              }             
              
              //for akses report
              Integer rep = 0; //0 = user cabang ; 1 = user yang bisa melihat secara keseluruhan/ admin ; 2 = user2 tertentu di BAS
              if("5,55,690".indexOf(currentUser.getLus_id())>-1){
                     rep = 1;
              }else if(currentUser.getLus_id().contains("2475") || currentUser.getLus_id().contains("4180") || currentUser.getLus_id().contains("690") || currentUser.getLus_id().contains("3041") || currentUser.getLus_id().contains("4852") || currentUser.getLus_id().contains("6579")){
                  rep = 2;
                  String lus_id_list = "(2475,4180,690,3041,4852,6579)"; //list user Summary Follow Up Premi Lanjutan dan list ini juga di set di xml elions.bas.selectReportFollowupBillingPerUser dan elions.bas.selectReportFollowupBillingPerUser2 dengan <isEqual property="rep" compareValue="2">
                  
                  List<DropDown> listAdmin = new ArrayList<DropDown>();
                  listAdmin.add(new DropDown("ALL", "ALL"));
                  listAdmin.addAll(elionsManager.selectDropDown("EKA.LST_USER", "LUS_ID", "LUS_FULL_NAME", "", "LUS_FULL_NAME", currentUser.getLus_bas().intValue() == 1 ? "LUS_ID in " + lus_id_list + "" : ""));
                  
                  cmd.put("listAdmin", listAdmin);
              }
              cmd.put("rep", rep);
              
              return new ModelAndView("bas/fupremi/main", cmd);
       }
       
       public ModelAndView summary(HttpServletRequest request, HttpServletResponse response) throws Exception {
           
    	   List result = new ArrayList();
    	   Map<String, Object> cmd = new HashMap<String, Object>();
    	   Integer total = 0;
    	   User currentUser = (User) request.getSession().getAttribute("currentUser");
           String kriteria_tgl = ServletRequestUtils.getRequiredStringParameter(request, "jn_tgl");
           String begdate = ServletRequestUtils.getStringParameter(request, "begdate", defaultDateFormat.format(elionsManager.selectSysdate(0)));
           String enddate = ServletRequestUtils.getStringParameter(request, "enddate", defaultDateFormat.format(elionsManager.selectSysdate(30)));
           String admin = ServletRequestUtils.getRequiredStringParameter(request, "admin");
           
           
           if (kriteria_tgl.contains("0")){
        	   kriteria_tgl = "Tagihan";
           }else{
        	   kriteria_tgl = "Follow Up";
           }
           
           String rep = ServletRequestUtils.getRequiredStringParameter(request, "rep");
       	 
       	 if (rep.equals("2")){
       		 
       		 result = Common.serializableList(bacManager.selectReportFollowupBillingPerUserCount(
                        ServletRequestUtils.getRequiredStringParameter(request, "begdate"), 
                        ServletRequestUtils.getRequiredStringParameter(request, "enddate"), 
                        currentUser.getLus_id(), ServletRequestUtils.getRequiredStringParameter(request, "stfu3"), 
                        "ALL", //cabang pusat saja
                        ServletRequestUtils.getRequiredStringParameter(request, "admin"),
                        ServletRequestUtils.getRequiredStringParameter(request, "kat"),
                        rep, 
                        ServletRequestUtils.getRequiredStringParameter(request, "jn_tgl")));

       	 }else{
      	  	
               result = Common.serializableList(bacManager.selectReportFollowupBillingPerUserCount(
                     ServletRequestUtils.getRequiredStringParameter(request, "begdate"), 
                     ServletRequestUtils.getRequiredStringParameter(request, "enddate"), 
                     currentUser.getLus_id(), ServletRequestUtils.getRequiredStringParameter(request, "stfu3"), 
                     ServletRequestUtils.getRequiredStringParameter(request, "cabang"), 
                     ServletRequestUtils.getRequiredStringParameter(request, "admin"),
                     ServletRequestUtils.getRequiredStringParameter(request, "kat"),
                     ServletRequestUtils.getRequiredStringParameter(request, "rep"),
                     ServletRequestUtils.getRequiredStringParameter(request, "jn_tgl")));
       	 }
         
       	if(!result.isEmpty()){
       		
       		for(int i=0; i<result.size(); i++){
       			HashMap m = (HashMap) result.get(i);
       			Integer subtotal = ((BigDecimal) m.get("TOTAL_SPAJ")).intValue();				
				total = total + subtotal;
       		}
       		
       		cmd.put("total", total);
       		cmd.put("listcountuserfu", result);
       	}else{ //bila tidak ada data
                ServletOutputStream sos = response.getOutputStream();
                sos.println("<script>alert('Tidak ada data');window.close();</script>");
                sos.close();
         }
      
           cmd.put("kriteria", kriteria_tgl);
           cmd.put("begdate", begdate);
           cmd.put("enddate", enddate);
           cmd.put("stfu3", ServletRequestUtils.getRequiredStringParameter(request, "stfu3"));
           cmd.put("cabang","ALL");
           cmd.put("admin", admin);
           cmd.put("kat", ServletRequestUtils.getRequiredStringParameter(request, "kat"));
           cmd.put("rep", rep);
           cmd.put("jn_tgl", ServletRequestUtils.getRequiredStringParameter(request, "jn_tgl"));
           return new ModelAndView("bas/fupremi/summary", cmd);
    }
}
