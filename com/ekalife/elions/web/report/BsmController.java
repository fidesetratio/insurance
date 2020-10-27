package com.ekalife.elions.web.report;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.jasper.Report;
import com.ekalife.utils.parent.ParentJasperReportingController;
import com.google.gson.Gson;
import com.ibatis.common.resources.Resources;
import com.lowagie.text.pdf.PdfWriter;

import id.co.sinarmaslife.std.model.vo.DropDown;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
/**
 * Report2 yang digunakan di modul BSM (Bank Sinarmas)
 *
 * @author Samuel Baktiar
 * @since Mar 01, 2010 (11:30:00 AM)
 */
public class BsmController extends ParentJasperReportingController
{
	protected final Log logger = LogFactory.getLog( getClass() );
    /**
     * Report Demografi Bank Sinarmas, Req Cynthia / Himmia via Email
     * http://yusuf7/E-Lions/report/bsm.htm?window=demografi
     * Yusuf - 3 Okt 2011
     */
    public ModelAndView demografi(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");

    	//bila tampilkan report
    	if(request.getParameter("showReport") != null){

    		Map m = request.getParameterMap();
    		
    		//String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		//String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		List data = uwManager.selectReportDemografiBSM(m,currentUser.getJn_bank());
    		
    		if(data.size() > 0){ //bila ada data
	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bsm.demografi") + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		//params.put("bdate", bdate);
	    		//params.put("edate", edate);
	    		params.put("user", currentUser.getName());
	    		params.put("jn_bank", currentUser.getJn_bank());

	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));

	    		//Yusuf (Okt 2011) enable ini kalau mau generate file dulu ke server, baru ditampilkan ke usernya
	    		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	    		Date now = elionsManager.selectSysdateSimple();
	    		String destDir = props.getProperty("pdf.dir.report") + "\\bsm_demografi";
	    		String fileName = "demografi" + df.format(now); 
		    	if(request.getParameter("showXLS") != null){
		    		fileName += ".xls";
		    		response.setContentType("application/vnd.ms-excel");
		    		JExcelApiExporter exporter = new JExcelApiExporter();
		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, destDir + "\\" + fileName);
		            exporter.exportReport();
		    	}else if(request.getParameter("showPDF") != null){
		    		fileName += ".pdf";
		    		JasperExportManager.exportReportToPdfFile(jasperPrint, destDir + "\\" + fileName);
		    	}
		    	FileUtils.downloadFile("inline", destDir, fileName, response);

	    		//Yusuf (Okt 2011) enable ini kalau mau langsung di stream dan ditampilkan ke browser user
//	    		ServletOutputStream sos = response.getOutputStream();
//		    	if(request.getParameter("showXLS") != null){
//		    		response.setContentType("application/vnd.ms-excel");
//		    		JExcelApiExporter exporter = new JExcelApiExporter();
//		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
//		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
//		            exporter.exportReport();
//		    	}else if(request.getParameter("showPDF") != null){
//		    		JasperExportManager.exportReportToPdfStream(jasperPrint, sos);
//		    	}
//	    		sos.close();
	    		
    		}else{ //bila tidak ada data
    			ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Tidak ada data');window.close();</script>");
    			sos.close();
    		}
    	
    	//halaman depan
    	}else{
        	Map<String, Object> m = new HashMap<String, Object>();

        	List<DropDown> listUmur = new ArrayList<DropDown>();
        	listUmur.add(new DropDown("17 and 26", "17-26 Tahun"));
        	listUmur.add(new DropDown("27 and 36", "27-36 Tahun"));
        	listUmur.add(new DropDown("37 and 46", "37-46 Tahun"));
        	listUmur.add(new DropDown("47 and 56", "47-56 Tahun"));
        	listUmur.add(new DropDown("57 and 66", "57-66 Tahun"));
        	listUmur.add(new DropDown("67 and 99", "> 66 Tahun"));
        	
        	List<DropDown> listSex = new ArrayList<DropDown>();
        	listSex.add(new DropDown("1", "Laki-laki"));
        	listSex.add(new DropDown("0", "Perempuan"));

        	List<DropDown> listMarital = new ArrayList<DropDown>();
        	listMarital.add(new DropDown("1", "Belum Menikah"));
        	listMarital.add(new DropDown("2", "Menikah"));
        	listMarital.add(new DropDown("3", "Janda"));
        	listMarital.add(new DropDown("4", "Duda"));

        	List<DropDown> listAgama = new ArrayList<DropDown>();
        	listAgama.add(new DropDown("1", "Islam"));
        	listAgama.add(new DropDown("2", "Protestan"));
        	listAgama.add(new DropDown("3", "Katolik"));
        	listAgama.add(new DropDown("4", "Buddha"));
        	listAgama.add(new DropDown("5", "Hindu"));
        	listAgama.add(new DropDown("6", "Lain-lain"));

        	//List<DropDown> listKerja = new ArrayList<DropDown>();
        	//listKerja.add(new DropDown("", ""));

        	List<DropDown> listPenghasilan = new ArrayList<DropDown>();
        	listPenghasilan.add(new DropDown("<= RP. 10 JUTA", "<= RP. 10 JUTA"));
        	listPenghasilan.add(new DropDown("> RP. 10 JUTA - RP. 50 JUTA", "> RP. 10 JUTA - RP. 50 JUTA"));
        	listPenghasilan.add(new DropDown("> RP. 50 JUTA - RP. 100 JUTA", "> RP. 50 JUTA - RP. 100 JUTA"));
        	listPenghasilan.add(new DropDown("> RP. 100 JUTA - RP. 300 JUTA", "> RP. 100 JUTA - RP. 300 JUTA"));
        	listPenghasilan.add(new DropDown("> RP. 300 JUTA - RP. 500 JUTA", "> RP. 300 JUTA - RP. 500 JUTA"));
        	listPenghasilan.add(new DropDown("> RP. 500 JUTA", "> RP. 500 JUTA"));
        	
        	List<DropDown> listWilayah = elionsManager.selectDropDown("eka.lst_cabang_bii", "lcb_no", "nama_cabang", "", "kode", "lcb_no = wil_no and jenis = 2 and flag_aktif = 1");
        	if(currentUser.getJn_bank()==3){
        		listWilayah = null;
        	}
        	
        	List<DropDown> listOutstanding = new ArrayList<DropDown>();
        	listOutstanding.add(new DropDown("0 and 100000000", "<= Rp. 100 juta"));
        	listOutstanding.add(new DropDown("100000001 and 500000000", "Rp. 100 juta - Rp. 500 juta"));
        	listOutstanding.add(new DropDown("500000001 and 1000000000", "Rp. 500 juta - Rp. 1 milyar"));
        	listOutstanding.add(new DropDown("1000000001 and 2000000000", "Rp. 1 milyar - Rp. 2 milyar"));
        	listOutstanding.add(new DropDown("2000000001 and 1000000000000", "> Rp. 2 milyar"));

        	List<DropDown> listMGI = new ArrayList<DropDown>();
        	listMGI.add(new DropDown("1", "1 Bulan"));
        	listMGI.add(new DropDown("3", "3 Bulan"));
        	listMGI.add(new DropDown("6", "6 Bulan"));
        	listMGI.add(new DropDown("12", "12 Bulan"));
        	listMGI.add(new DropDown("24", "24 Bulan"));
        	listMGI.add(new DropDown("36", "36 Bulan"));
        	listMGI.add(new DropDown("0", "Sekaligus"));

        	/* LIHAT com.ekalife.utils.Products.bsm
			select (lsbs_id || '-' || lsdbs_number), ('[' || lsbs_id || '-' || lsdbs_number || '] ' || lsdbs_name)
			from eka.lst_det_bisnis 
			where (lsbs_id || '-' || lsdbs_number) in ('142-2', '158-6', '164-2',
			'120-10','120-11','120-12','163-6','163-7','163-8','163-9','163-10',
			'175-2','182-7','182-8','182-9','186-3','187-1','188-2') 
			and lsdbs_aktif = 1
			order by lsbs_id, lsdbs_number
        	 */
        	
        	List<DropDown> listProduct = elionsManager.selectDropDown("eka.lst_det_bisnis", "(lsbs_id || '-' || lsdbs_number)", "('[' || lsbs_id || '-' || lsdbs_number || '] ' || lsdbs_name)", "", "lsbs_id, lsdbs_number", "lsdbs_aktif = 1 and (lsbs_id || '-' || lsdbs_number) in ('142-2', '158-6', '164-2', '175-2','182-7','182-8','182-9','120-22','120-23','120-24')");
        	if(currentUser.getJn_bank()==3){
        		listProduct = elionsManager.selectDropDown("eka.lst_det_bisnis", "(lsbs_id || '-' || lsdbs_number)", "('[' || lsbs_id || '-' || lsdbs_number || '] ' || lsdbs_name)", "", "lsbs_id, lsdbs_number", "lsdbs_aktif = 1 and (lsbs_id || '-' || lsdbs_number) in ('142-9', '158-14', '164-8')");
        	}
        	List<DropDown> listKurs = new ArrayList<DropDown>();
        	listKurs.add(new DropDown("01", "IDR"));
        	listKurs.add(new DropDown("02", "USD"));
        	
        	m.put("listUmur", listUmur); 
        	m.put("listSex", listSex);
        	m.put("listMarital", listMarital);
        	m.put("listAgama", listAgama);
        	//m.put("listKerja", listKerja);
        	m.put("listPenghasilan", listPenghasilan);
        	m.put("listWilayah", listWilayah);
        	m.put("listOutstanding", listOutstanding);
        	m.put("listMGI", listMGI);
        	m.put("listProduct", listProduct);
        	m.put("listKurs", listKurs);
        	
        	String today = defaultDateFormat.format(elionsManager.selectSysdate());
        	m.put("today", today);
        	
        	m.put("jn_bank", currentUser.getJn_bank());
        	String judul = "Report Demografi Bank Sinarmas";
        	if(currentUser.getJn_bank()==3){
        		judul= "Report Demografi Sekuritas";
        	}	
        	m.put("judul", judul);

	    	return new ModelAndView("report/bsm_demografi", m);
    	}
    	
		return null;
    }	
	
    /**
     * Report Produksi BSM Berdasarkan Tanggal Efektif
     *
     * @param request: no desc
     * @param response: no desc
     * @return ModelAndView
     * @throws Exception : no desc
     * @since Mar 1, 2010 (11:30:00 AM)
     */
    public ModelAndView bsm_produksi_tgl_efektif( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "BsmController.bsm_produksi_tgl_efektif" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();

        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

        if( currentUser.getCab_bank().trim().equals( "SSS" ) )
        {
            reportPathList.add( new DropDown(
                    props.getProperty( "report.bsm.prod_efektif_prima_all" ),
                    "Produksi Simas Prima Berdasarkan Tanggal Efektif Semua Cabang"
            ) );

            reportPathList.add( new DropDown(
                    props.getProperty( "report.bsm.prod_efektif_stabil_link_all" ),
                    "Produksi Simas Stabil Link Berdasarkan Tanggal Efektif Semua Cabang"
            ) );
        }
        else
        {
        	if(currentUser.getLus_id().equals("847")){//IBT SBY_SIMASPRIMA
        		reportPathList.add( new DropDown(
                    props.getProperty( "report.bsm.prod_efektif_prima_cbg_ibt" ),
                    "Produksi Simas Prima Berdasarkan Tanggal Efektif Per Cabang"
        		) );
        		
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.prod_efektif_stabil_link_ibt" ),
                        "Produksi Simas Stabil Link Berdasarkan Tanggal Efektif Per Cabang"
                ) );
        	}else if(currentUser.getLus_id().equals("583")){//makasar TIEN
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.prod_efektif_prima_mksr" ),
                        "Produksi Simas Prima Berdasarkan Tanggal Efektif Per Cabang"
            		) );
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.prod_efektif_stabil_link_makasar" ),
                        "Produksi Simas Stabil Link Berdasarkan Tanggal Efektif Per Cabang"
                ) );
        	}else if(currentUser.getLus_id().equals("1053")){//BALI YULIS
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.prod_efektif_prima_bali" ),
                        "Produksi Simas Prima Berdasarkan Tanggal Efektif Per Cabang"
            		) );
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.prod_efektif_stabil_link_bali" ),
                        "Produksi Simas Stabil Link Berdasarkan Tanggal Efektif Per Cabang"
                ) );
        	}
        	else if(currentUser.getLus_id().equals("1916")){//MANADO BLM ADA USER
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.prod_efektif_prima_manado" ),
                        "Produksi Simas Prima Berdasarkan Tanggal Efektif Per Cabang"
            		) );
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.prod_efektif_stabil_link_manado" ),
                        "Produksi Simas Stabil Link Berdasarkan Tanggal Efektif Per Cabang"
                ) );
        	}
        	else{
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.prod_efektif_prima_cbg" ),
                        "Produksi Simas Prima Berdasarkan Tanggal Efektif Per Cabang"
                ) );

                reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.prod_efektif_stabil_link_cbg" ),
                        "Produksi Simas Stabil Link Berdasarkan Tanggal Efektif Per Cabang"
                ) );
        	}
            
        }

        report = new Report( "Report Produksi Berdasarkan Tanggal Efektif BSM", reportPathList, Report.PDF, null );

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        report.addParamDefault( "username", "username", 0, currentUser.getLus_full_name(), false );
        report.addParamDefault( "lus_id", "lus_id", 200, currentUser.getLus_id(), false );

        if( currentUser.getCab_bank().trim().equals( "SSS" ) )
        {
        }
        else
        {
            report.addParamDefault( "lcb_no", "lcb_no", 200, currentUser.getCab_bank(), false );
        }

        report.addParamDate( "Tanggal Efektif", "tanggal", true, new Date[]{ sysDate, sysDate }, true );

        List<Map<String, String>> mataUangList = new ArrayList<Map<String, String>>();
		Map<String, String> tmp = new HashMap<String, String>();
		tmp.put("KEY", "01");
		tmp.put("VALUE", "Rupiah");
		mataUangList.add(tmp);
		tmp = new HashMap<String, String>();
		tmp.put("KEY", "02");
		tmp.put("VALUE", "US Dollar");
		mataUangList.add(tmp);
		report.addParamSelectWithoutAll("Mata Uang", "lku_id", mataUangList, "01", true);

        return prepareReport( request, response, report );
    }
    
    public ModelAndView internal_ajs( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "BsmController.internal_ajs" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();
        
       

        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

        reportPathList.add( new DropDown(
              props.getProperty( "report.bsm.ajs_cair_all" ),
              "Laporan Pencarian"
	    ) );
	
	    reportPathList.add( new DropDown(
	              props.getProperty( "report.bsm.ajs_prod_akseptasi_all" ),
	              "Laporan Produksi"
	    ) );

	    reportPathList.add( new DropDown(
	              props.getProperty( "report.bsm.ajs_outstanding_all" ),
	              "Laporan Outstanding"
	    ) );
      
//        reportPathList.add( new DropDown(
//                props.getProperty( "report.bsm.ajs_prod_efektif_prima_all" ),
//                "Produksi Simas Prima Berdasarkan Tanggal Efektif Semua Cabang"
//        ) );
//
//        reportPathList.add( new DropDown(
//                props.getProperty( "report.bsm.ajs_prod_efektif_stabil_link_all" ),
//                "Produksi Simas Stabil Link Berdasarkan Tanggal Efektif Semua Cabang"
//        ) );
//        
//        reportPathList.add( new DropDown(
//                props.getProperty( "report.bsm.ajs_prod_akseptasi_prima_all" ),
//                "Produksi Simas Prima Berdasarkan Tanggal Akseptasi Semua Cabang"
//        ) );
//
//        reportPathList.add( new DropDown(
//                props.getProperty( "report.bsm.ajs_prod_akseptasi_stabil_link_all" ),
//                "Produksi Simas Stabil Link Berdasarkan Tanggal Akseptasi Semua Cabang"
//        ) );
//        
//        reportPathList.add( new DropDown(
//                props.getProperty( "report.bsm.ajs_cair_prima_all" ),
//                "Pencairan Simas Prima Berdasarkan Tanggal Aktual Semua Cabang"
//        ) );
//
//        reportPathList.add( new DropDown(
//                props.getProperty( "report.bsm.ajs_cair_stabil_link_all" ),
//                "Pencairan Simas Stabil Link Berdasarkan Tanggal Aktual Semua Cabang"
//        ) );
//        
//        reportPathList.add( new DropDown(
//                props.getProperty( "report.bsm.ajs_outstanding_all" ),
//                "Report Summary Outstanding Semua Produk Untuk Semua Cabang"
//        ) );
       

        report = new Report( "Report Produksi", reportPathList, Report.PDF, null );
        
        List<Map<String, String>> team = new ArrayList<Map<String, String>>();
		Map<String, String> tmp1 = new HashMap<String, String>();
		tmp1.put("KEY", "Team Jan Rosadi");
		tmp1.put("VALUE", "Jakarta");
		team.add(tmp1);
		tmp1 = new HashMap<String, String>();
		tmp1.put("KEY", "Team Dewi");
		tmp1.put("VALUE", "Surabaya");
		team.add(tmp1);
        report.addParamSelectWithoutAll("team", "team", team, "1", true);

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        report.addParamDefault( "username", "username", 0, currentUser.getLus_full_name(), false );
        report.addParamDefault( "lus_id", "lus_id", 200, currentUser.getLus_id(), false );

        if( currentUser.getCab_bank().trim().equals( "SSS" ) )
        {
        }
        else
        {
            report.addParamDefault( "lcb_no", "lcb_no", 200, currentUser.getCab_bank(), false );
        }

        report.addParamDate( "Tanggal", "tanggal", true, new Date[]{ sysDate, sysDate }, true );

        return prepareReport( request, response, report );
    }

    /**
     * Report Produksi BSM Berdasarkan Tanggal Akseptasi
     *
     * @param request: no desc
     * @param response: no desc
     * @return ModelAndView
     * @throws Exception: no desc
     * @since Mar 1, 2010 (11:30:00 AM)
     */
    public ModelAndView bsm_produksi_tgl_akseptasi( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "BsmController.bsm_produksi_tgl_akseptasi" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();

        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

        if( currentUser.getCab_bank().trim().equals( "SSS" ) )
        {
            reportPathList.add( new DropDown(
                    props.getProperty( "report.bsm.prod_akseptasi_prima_all" ),
                    "Produksi Simas Prima Berdasarkan Tanggal Akseptasi Semua Cabang"
            ) );

            reportPathList.add( new DropDown(
                    props.getProperty( "report.bsm.prod_akseptasi_stabil_link_all" ),
                    "Produksi Simas Stabil Link Berdasarkan Tanggal Akseptasi Semua Cabang"
            ) );
        }
        else
        {
        	if(currentUser.getLus_id().equals("847")){//IBT SBY_SIMASPRIMA
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.prod_akseptasi_prima_ibt" ),
                        "Produksi Simas Prima Berdasarkan Tanggal Akseptasi Per Cabang"
                ) );

                reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.prod_akseptasi_stabil_link_ibt" ),
                        "Produksi Simas Stabil Link Berdasarkan Tanggal Akseptasi Per Cabang"
                ) );
        	}else if(currentUser.getLus_id().equals("583")){//makasar TIEN
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.prod_akseptasi_prima_makasar" ),
                        "Produksi Simas Prima Berdasarkan Tanggal Akseptasi Per Cabang"
                ) );

                reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.prod_akseptasi_stabil_link_makasar" ),
                        "Produksi Simas Stabil Link Berdasarkan Tanggal Akseptasi Per Cabang"
                ) );
        	}else if(currentUser.getLus_id().equals("1053")){//BALI YULIS
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.prod_akseptasi_prima_bali" ),
                        "Produksi Simas Prima Berdasarkan Tanggal Akseptasi Per Cabang"
                ) );

                reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.prod_akseptasi_stabil_link_bali" ),
                        "Produksi Simas Stabil Link Berdasarkan Tanggal Akseptasi Per Cabang"
                ) );
        	}
        	else if(currentUser.getLus_id().equals("1916")){//MANADO BLM ADA USER
        		reportPathList.add( new DropDown(
            		props.getProperty( "report.bsm.prod_akseptasi_prima_manado" ),
            		"Produksi Simas Prima Berdasarkan Tanggal Akseptasi Per Cabang"
   			 ) );

    			reportPathList.add( new DropDown(
           		 props.getProperty( "report.bsm.prod_akseptasi_stabil_link_manado" ),
            		"Produksi Simas Stabil Link Berdasarkan Tanggal Akseptasi Per Cabang"
    			) );
        	}
        	else{reportPathList.add( new DropDown(
                    props.getProperty( "report.bsm.prod_akseptasi_prima_cbg" ),
                    "Produksi Simas Prima Berdasarkan Tanggal Akseptasi Per Cabang"
            ) );

            reportPathList.add( new DropDown(
                    props.getProperty( "report.bsm.prod_akseptasi_stabil_link_cbg" ),
                    "Produksi Simas Stabil Link Berdasarkan Tanggal Akseptasi Per Cabang"
            ) );}
        }

        report = new Report( "Report Produksi Berdasarkan Tanggal Akseptasi BSM", reportPathList, Report.PDF, null );

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        report.addParamDefault( "username", "username", 0, currentUser.getLus_full_name(), false );
        report.addParamDefault( "lus_id", "lus_id", 200, currentUser.getLus_id(), false );

        if( currentUser.getCab_bank().trim().equals( "SSS" ) )
        {
        }
        else
        {
            report.addParamDefault( "lcb_no", "lcb_no", 200, currentUser.getCab_bank(), false );
        }

        report.addParamDate( "Tanggal Akseptasi ", "tanggal", true, new Date[]{ sysDate, sysDate }, true );

        List<Map<String, String>> mataUangList = new ArrayList<Map<String, String>>();
		Map<String, String> tmp;

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "01");
		tmp.put("VALUE", "Rupiah");
		mataUangList.add(tmp);

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "02");
		tmp.put("VALUE", "US Dollar");
        mataUangList.add(tmp);

        report.addParamSelectWithoutAll("Mata Uang", "lku_id", mataUangList, "01", true);

        return prepareReport( request, response, report );
    }

    /**
     * Report Produksi BSM Berdasarkan Tanggal Akseptasi, by Referal
     *
     * @param request: no desc
     * @param response: no desc
     * @return ModelAndView
     * @throws Exception: no desc
     * @since Apr 7, 2010 (17:00:00 AM)
     */
    public ModelAndView bsm_produksi_by_referral_all( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "BsmController.prod_akseptasi_by_referral_all" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();

        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

        if( currentUser.getCab_bank().trim().equals( "SSS" ) )
        {
            reportPathList.add( new DropDown(
                    props.getProperty( "report.bsm.prod_akseptasi_by_referral_all" ),
                    "Produksi Bank Sinarmas Berdasarkan Tanggal Akseptasi By Referral"
            ) );
        }

        report = new Report( "Report Produksi Berdasarkan Tanggal Akseptasi BSM By Referral", reportPathList, Report.PDF, null );

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        report.addParamDefault( "username", "username", 0, currentUser.getLus_full_name(), false );
        report.addParamDefault( "lus_id", "lus_id", 200, currentUser.getLus_id(), false );

        if( currentUser.getCab_bank().trim().equals( "SSS" ) )
        {
        }
        else
        {
            report.addParamDefault( "lcb_no", "lcb_no", 200, currentUser.getCab_bank(), false );
        }

        report.addParamDate( "Tanggal Akseptasi", "tanggal", true, new Date[]{ sysDate, sysDate }, true );

        List<Map<String, String>> mataUangList = new ArrayList<Map<String, String>>();
		Map<String, String> tmp;

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "01");
		tmp.put("VALUE", "Rupiah");
		mataUangList.add(tmp);

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "02");
		tmp.put("VALUE", "US Dollar");
        mataUangList.add(tmp);

        report.addParamSelectWithoutAll("Mata Uang", "lku_id", mataUangList, "01", true);

        return prepareReport( request, response, report );
    }    
    
    /**
     * Report Produksi BSM Berdasarkan Tanggal Akseptasi
     *
     * @param request: no desc
     * @param response: no desc
     * @return ModelAndView
     * @throws Exception: no desc
     * @since Mar 1, 2010 (11:30:00 AM)
     */
    public ModelAndView bsm_delta_harian( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "BsmController.bsm_delta_harian" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();

        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

        if( currentUser.getCab_bank().trim().equals( "SSS" ) )
        {
            reportPathList.add( new DropDown(
                    props.getProperty( "report.bsm.delta_harian_prima_all" ),
                    "Report Summary (Delta Harian) Simas Prima Untuk Semua Cabang"
            ) );
            reportPathList.add( new DropDown(
                    props.getProperty( "report.bsm.delta_harian_stabil_link_all" ),
                    "Report Summary (Delta Harian) Simas Stabil Link Untuk Semua Cabang"
            ) );
        }
        else
        {
            reportPathList.add( new DropDown(
                    props.getProperty( "report.bsm.delta_harian_prima_cbg" ),
                    "Report Summary (Delta Harian) Simas Prima Per Cabang"
            ) );
            reportPathList.add( new DropDown(
                    props.getProperty( "report.bsm.delta_harian_stabil_link_cbg" ),
                    "Report Summary (Delta Harian) Simas Stabil Link Per Cabang"
            ) );
        }

        report = new Report( "Report Summary (Delta Harian) BSM", reportPathList, Report.PDF, null );

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        report.addParamDefault( "username", "username", 0, currentUser.getLus_full_name(), false );
        report.addParamDefault( "lus_id", "lus_id", 200, currentUser.getLus_id(), false );

        if( currentUser.getCab_bank().trim().equals( "SSS" ) )
        {
        }
        else
        {
            report.addParamDefault( "lcb_no", "lcb_no", 200, currentUser.getCab_bank(), false );
        }

        report.addParamDate( "Tanggal Efektif", "tanggal", true, new Date[]{ sysDate, sysDate }, true );

        List<Map<String, String>> mataUangList = new ArrayList<Map<String, String>>();
		Map<String, String> tmp;

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "01");
		tmp.put("VALUE", "Rupiah");
		mataUangList.add(tmp);

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "02");
		tmp.put("VALUE", "US Dollar");
		mataUangList.add(tmp);

        report.addParamSelectWithoutAll("Mata Uang", "lku_id", mataUangList, "01", true);

        return prepareReport( request, response, report );
    }

    /**
     * Report Produksi BSM Berdasarkan Tanggal Akseptasi
     *
     * @param request: no desc
     * @param response: no desc
     * @return ModelAndView
     * @throws Exception: no desc
     * @since Mar 1, 2010 (11:30:00 AM)
     */
    public ModelAndView bsm_pencairan( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "BsmController.bsm_pencairan" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();

        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

        if( currentUser.getCab_bank().trim().equals( "SSS" ) )
        {
            reportPathList.add( new DropDown(
                    props.getProperty( "report.bsm.cair_prima_all" ),
                    "Pencairan Simas Prima Berdasarkan Tanggal Aktual Semua Cabang"
            ) );

            reportPathList.add( new DropDown(
                    props.getProperty( "report.bsm.cair_stabil_link_all" ),
                    "Pencairan Simas Stabil Link Berdasarkan Tanggal Aktual Semua Cabang"
            ) );
        }
        else
        {
            
            if(currentUser.getLus_id().equals("847")){//IBT SBY_SIMASPRIMA
            	reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.cair_prima_ibt" ),
                             "Pencairan Simas Prima Berdasarkan Tanggal Aktual Per Cabang"
                     ) );
            	reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.cair_stabil_link_ibt" ),
                        "Pencairan Simas Stabil Link Berdasarkan Tanggal Aktual Per Cabang"
                ) );
        	}else if(currentUser.getLus_id().equals("583")){//makasar TIEN
        		reportPathList.add( new DropDown(
        	               props.getProperty( "report.bsm.cair_prima_makasar" ),
        	                    "Pencairan Simas Prima Berdasarkan Tanggal Aktual Per Cabang"
        	            ) );
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.cair_stabil_link_makasar" ),
                        "Pencairan Simas Stabil Link Berdasarkan Tanggal Aktual Per Cabang"
                ) );
        	}else if(currentUser.getLus_id().equals("1053")){//BALI YULIS
        		reportPathList.add( new DropDown(
        	               props.getProperty( "report.bsm.cair_prima_bali" ),
        	                    "Pencairan Simas Prima Berdasarkan Tanggal Aktual Per Cabang"
        	            ) );
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.cair_stabil_link_bali" ),
                        "Pencairan Simas Stabil Link Berdasarkan Tanggal Aktual Per Cabang"
                ) );
        	}
        	else if(currentUser.getLus_id().equals("1916")){//MANADO BLM ADA USER
        		reportPathList.add( new DropDown(
        	               props.getProperty( "report.bsm.cair_prima_manado" ),
        	                    "Pencairan Simas Prima Berdasarkan Tanggal Aktual Per Cabang"
        	            ) );
            reportPathList.add( new DropDown(
                    props.getProperty( "report.bsm.cair_stabil_link_manado" ),
                    "Pencairan Simas Stabil Link Berdasarkan Tanggal Aktual Per Cabang"
            ) );
        	}
        	else{
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.cair_prima_cbg" ),
                             "Pencairan Simas Prima Berdasarkan Tanggal Aktual Per Cabang"
                     ) );
            	reportPathList.add( new DropDown(
                        props.getProperty( "report.bsm.cair_stabil_link_cbg" ),
                        "Pencairan Simas Stabil Link Berdasarkan Tanggal Aktual Per Cabang"
                ) );
        	}
            
        }

        report = new Report( "Report Pencairan BSM Berdasarkan Tanggal Aktual", reportPathList, Report.PDF, null );

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        report.addParamDefault( "username", "username", 0, currentUser.getLus_full_name(), false );
        report.addParamDefault( "lus_id", "lus_id", 200, currentUser.getLus_id(), false );

        if( currentUser.getCab_bank().trim().equals( "SSS" ) )
        {
        }
        else
        {
            report.addParamDefault( "lcb_no", "lcb_no", 200, currentUser.getCab_bank(), false );
        }

        report.addParamDate( "Tanggal Aktual Bayar", "tanggal", true, new Date[]{ sysDate, sysDate }, true );

        List<Map<String, String>> mataUangList = new ArrayList<Map<String, String>>();
		Map<String, String> tmp;

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "01");
		tmp.put("VALUE", "Rupiah");
		mataUangList.add(tmp);

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "02");
		tmp.put("VALUE", "US Dollar");
        mataUangList.add(tmp);

        report.addParamSelectWithoutAll("Mata Uang", "lku_id", mataUangList, "01", true);

        return prepareReport( request, response, report );
    }

    /**
     * Report Rollover BSM
     *
     * @param request: no desc
     * @param response: no desc
     * @return ModelAndView
     * @throws Exception: no desc
     * @since Mar 25, 2010 (11:30:00 AM)
     */
    public ModelAndView bsm_rollover( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "BsmController.bsm_rollover" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();

        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

        if( currentUser.getCab_bank().trim().equals( "SSS" ) )
        {
            reportPathList.add( new DropDown(
                    props.getProperty( "report.bac.view_rollover_all" ),
                    "Laporan Rollover Simas Prima Per Tgl Produksi Semua Cabang"
            ) );
            reportPathList.add( new DropDown(
                    props.getProperty( "report.bac.view_stabil_rollover_all" ),
                    "Laporan Rollover Simas Stabil Link Per Tgl Produksi Semua Cabang"
            ) );
        }
        else
        {
        	if(currentUser.getLus_id().equals("847")){//ibt
        		reportPathList.add( new DropDown(
                    props.getProperty( "report.bac.view_rollover_ibt" ),
                    "Laporan Rollover Simas Prima Per Tgl Produksi Per Cabang"
        		) );
        		reportPathList.add( new DropDown(
                    props.getProperty( "report.bac.view_stabil_rollover_ibt" ),
                    "Laporan Rollover Simas Stabil Link Per Tgl Produksi Per Cabang"
        		) );
        	}else if(currentUser.getLus_id().equals("583")){//makasar
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bac.view_rollover_makasar" ),
                        "Laporan Rollover Simas Prima Per Tgl Produksi Per Cabang"
            		) );
            		reportPathList.add( new DropDown(
                        props.getProperty( "report.bac.view_stabil_rollover_makasar" ),
                        "Laporan Rollover Simas Stabil Link Per Tgl Produksi Per Cabang"
            		) );
        	}else if(currentUser.getLus_id().equals("1053")){//bali
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bac.view_rollover_bali" ),
                        "Laporan Rollover Simas Prima Per Tgl Produksi Per Cabang"
            		) );
            		reportPathList.add( new DropDown(
                        props.getProperty( "report.bac.view_stabil_rollover_bali" ),
                        "Laporan Rollover Simas Stabil Link Per Tgl Produksi Per Cabang"
            		) );
        	}
        	else if(currentUser.getLus_id().equals("1916")){//madod blm ada user
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bac.view_rollover_manado" ),
                        "Laporan Rollover Simas Prima Per Tgl Produksi Per Cabang"
            		) );
            		reportPathList.add( new DropDown(
                        props.getProperty( "report.bac.view_stabil_rollover_manado" ),
                        "Laporan Rollover Simas Stabil Link Per Tgl Produksi Per Cabang"
            		) );
        	}
        	else{reportPathList.add( new DropDown(
                    props.getProperty( "report.bac.view_rollover" ),
                    "Laporan Rollover Simas Prima Per Tgl Produksi Per Cabang"
        		) );
        		reportPathList.add( new DropDown(
                    props.getProperty( "report.bac.view_stabil_rollover" ),
                    "Laporan Rollover Simas Stabil Link Per Tgl Produksi Per Cabang"
        		) );}
        }

        report = new Report( "Report Roll Over BSM", reportPathList, Report.PDF, null );

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        String jn_bank = Integer.toString(currentUser.getJn_bank());
		report.addParamDefault("username", "username", 200, currentUser.getLus_full_name(), false);
		report.addParamDefault("cab_bank", "cab_bank", 200, currentUser.getCab_bank(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDefault("jn_bank", "jn_bank", 200, jn_bank, false);
		report.addParamDate("Tanggal Produksi", "tanggal", true, new Date[] {sysDate, sysDate}, true);

        return prepareReport( request, response, report );
    }

    /**
     * Report Jatuh Tempo BSM
     *
     * @param request: no desc
     * @param response: no desc
     * @return ModelAndView
     * @throws Exception: no desc
     * @since Mar 25, 2010 (11:30:00 AM)
     */
    public ModelAndView bsm_jatuh_tempo( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "BsmController.bsm_jatuh_tempo" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();

        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

        if( currentUser.getCab_bank().trim().equals( "SSS" ) )
        {
            reportPathList.add( new DropDown(
                    props.getProperty( "report.bac.view_jatuh_tempo_all" ),
                    "Jatuh Tempo Simas Prima Semua Cabang"
            ) );

            reportPathList.add( new DropDown(
                    props.getProperty( "report.bac.view_stabil_jatuh_tempo_all" ),
                    "Jatuh Tempo Simas Stabil Link Semua Cabang"
            ) );
        }
        else
        {
        	if(currentUser.getLus_id().equals("847")){//IBT
        		reportPathList.add( new DropDown(
                    props.getProperty( "report.bac.view_jatuh_tempo_ibt" ),
                    "Jatuh Tempo Simas Prima Per Cabang"
        		) );

        		reportPathList.add( new DropDown(
                    props.getProperty( "report.bac.view_stabil_jatuh_tempo_ibt" ),
                    "Jatuh Tempo Simas Stabil Link Per Cabang"
        		) );
        	}else if(currentUser.getLus_id().equals("583")){//MAKASAR
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bac.view_jatuh_tempo_makasar" ),
                        "Jatuh Tempo Simas Prima Per Cabang"
            		) );

            		reportPathList.add( new DropDown(
                        props.getProperty( "report.bac.view_stabil_jatuh_tempo_makasar" ),
                        "Jatuh Tempo Simas Stabil Link Per Cabang"
            		) );
            }else if(currentUser.getLus_id().equals("1053")){//BALI
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bac.view_jatuh_tempo_bali" ),
                        "Jatuh Tempo Simas Prima Per Cabang"
            		) );

            		reportPathList.add( new DropDown(
                        props.getProperty( "report.bac.view_stabil_jatuh_tempo_bali" ),
                        "Jatuh Tempo Simas Stabil Link Per Cabang"
            		) );
            }
            else if(currentUser.getLus_id().equals("1916")){//MANADO
        		reportPathList.add( new DropDown(
                        props.getProperty( "report.bac.view_jatuh_tempo_manado" ),
                        "Jatuh Tempo Simas Prima Per Cabang"
            		) );

            		reportPathList.add( new DropDown(
                        props.getProperty( "report.bac.view_stabil_jatuh_tempo_manado" ),
                        "Jatuh Tempo Simas Stabil Link Per Cabang"
            		) );
            }
            else{
            	reportPathList.add( new DropDown(
                    props.getProperty( "report.bac.view_jatuh_tempo" ),
                    "Jatuh Tempo Simas Prima Per Cabang"
        		) );

        		reportPathList.add( new DropDown(
                    props.getProperty( "report.bac.view_stabil_jatuh_tempo" ),
                    "Jatuh Tempo Simas Stabil Link Per Cabang"
        		) );}
        }

        report = new Report( "Report Jatuh Tempo BSM", reportPathList, Report.PDF, null );

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        String jn_bank = Integer.toString(currentUser.getJn_bank());
		report.addParamDefault("username", "username", 200, currentUser.getLus_full_name(), false);
		report.addParamDefault("cab_bank", "cab_bank", 200, currentUser.getCab_bank(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDefault("jn_bank", "jn_bank", 200, jn_bank, false);
		report.addParamDate("Tanggal  Jatuh Tempo", "tanggal", true, new Date[] {sysDate, sysDate}, true);

        return prepareReport( request, response, report );
    }
    
    /**
     * @author Deddy
     * @since Sept 26,2016
     * Req : Gideon, Ada Perubahan tampilan dari report sebelumnya (Method simas_power_link_bsm).
     */
    public ModelAndView production_bsim_in_branch ( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
    	logger.info( "BsmController.production_bsim_in_branch" );
    	Report report = new Report("Laporan Produksi Bancassurance Bank Sinarmas", props.getProperty("report.bsm.prod_bsim_in_branch"), Report.PDF, null);
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();
        String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal","");
    	String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir","");
        String namaJudul = "Laporan Produksi Bancassurance Bank Sinarmas";
        
        List<Map> jenis_report = new ArrayList<Map>();
		Map tmp = new HashMap();
		tmp.put("KEY", "New Business");
		tmp.put("VALUE", "New Business");
		jenis_report.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "Renewal");
		tmp.put("VALUE", "Renewal");
		jenis_report.add(tmp);
		tmp = new HashMap();
		tmp.put("KEY", "Top-Up");
		tmp.put("VALUE", "Top-Up");
		jenis_report.add(tmp);
		report.addParamSelectWithoutAll("Jenis", "jenis", jenis_report, "New Business", true);
		
		List<Map> type_bisnis = new ArrayList<Map>();
		Map tmp1 = new HashMap();
		tmp1.put("KEY", "2");
		tmp1.put("VALUE", "Konvensional");
		type_bisnis.add(tmp1);
		tmp1 = new HashMap();
		tmp1.put("KEY", "16");
		tmp1.put("VALUE", "Syariah");
		type_bisnis.add(tmp1);
		report.addParamSelect("Type Bisnis", "type", type_bisnis, "All", true);
		
		List<Map> product = new ArrayList<Map>();
		Map tmp2 = new HashMap();
		tmp2.put("KEY", "120");
		tmp2.put("VALUE", "Simas Power Link");
		product.add(tmp2);
		tmp2 = new HashMap();
		tmp2.put("KEY", "202");
		tmp2.put("VALUE", "Simas Power Link Syariah");
		product.add(tmp2);
		tmp2 = new HashMap();
		tmp2.put("KEY", "213");
		tmp2.put("VALUE", "Simas Magna Link");
		product.add(tmp2);
		tmp2 = new HashMap();
		tmp2.put("KEY", "216");
		tmp2.put("VALUE", "Simas Magna Link Syariah");
		product.add(tmp2);
		tmp2 = new HashMap();
		tmp2.put("KEY", "134");
		tmp2.put("VALUE", "Simas Prime Link");
		product.add(tmp2);
		tmp2 = new HashMap();
		tmp2.put("KEY", "215");
		tmp2.put("VALUE", "Simas Prime Link Syariah");
		product.add(tmp2);
		tmp2 = new HashMap();
		tmp2.put("KEY", "208");
		tmp2.put("VALUE", "Simas Kid");
		product.add(tmp2);
		tmp2 = new HashMap();
		tmp2.put("KEY", "219");
		tmp2.put("VALUE", "Simas Kid Insurance Syariah");
		product.add(tmp2);
		tmp2 = new HashMap();
		tmp2.put("KEY", "73");
		tmp2.put("VALUE", "Personal Accident");
		product.add(tmp2);
		tmp2 = new HashMap();
		tmp2.put("KEY", "183");
		tmp2.put("VALUE", "Smile Medical");
		product.add(tmp2);
		tmp2 = new HashMap();
		tmp2.put("KEY", "189");
		tmp2.put("VALUE", "Smile Medical Syariah");
		product.add(tmp2);
		tmp2 = new HashMap();
		tmp2.put("KEY", "142");
		tmp2.put("VALUE", "Simas Prima");
		product.add(tmp2);
		tmp2 = new HashMap();
		tmp2.put("KEY", "158");
		tmp2.put("VALUE", "Simas Prima Manfaat Bulanan");
		product.add(tmp2);
		tmp2 = new HashMap();
		tmp2.put("KEY", "175");
		tmp2.put("VALUE", "Power Save Syariah");
		product.add(tmp2);
		tmp2 = new HashMap();
		tmp2.put("KEY", "226");
		tmp2.put("VALUE", "Simas Legacy Plan");
		product.add(tmp2);
		
		report.addParamSelect("Product", "product", product, "All", true);
		
//		SIMPOL, MAGNA, PRIME, SIMAS KID, PA, SMILE MEDICAL, POWER SAVE, SIMAS PRIMA Conventional dan Syariah
//		120, 202, 213, 216, 134, 215, 208, 219, 73, 183, 189, 142, 158, 175
		
		
		report.addParamDefault("username", "username", 200, currentUser.getLus_full_name().toUpperCase(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDate("Tanggal Produksi", "tanggal", true, new Date[] {sysDate, sysDate}, true);
		report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
		report.setReportQueryMethod("selectProductionBSIMInBranch");
		
		
		return prepareReport( request, response, report );
    }
    
 
    /**
     * @author Andhika
     * @since Mei 15, 2013 (11:30:00 AM)
     * Update Nov 29, 2013 : namaJudul req : Hisar
     */    
    public ModelAndView simas_power_link( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "BsmController.simas_power_link" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();
        
        List<DropDown> reportPathList = new ArrayList<DropDown>();
        
        	reportPathList.add( new DropDown(
                props.getProperty( "report.bsm.simas_power_link_new_business" ),
                "New Business"
        	) );

        	reportPathList.add( new DropDown(
                props.getProperty( "report.bsm.simas_power_link_renewal" ),
                "Renewal"
        	) );
        	
        	reportPathList.add( new DropDown(
                props.getProperty( "report.bsm.simas_power_link_top_up" ),
                "Top Up"
            ) );
        	
        	reportPathList.add( new DropDown(
                    props.getProperty( "report.bsm.simas_power_link_all_wil" ),
                    "All - Wilayah"
                ) );
        	
        String namaprod = "";	
        String lsdbsnumber = "";
        String jn_bank = Integer.toString(currentUser.getJn_bank());
        String namaJudul = "";
        
        if(jn_bank.equalsIgnoreCase("3") || currentUser.getLus_id().equals("692") || 
        		 currentUser.getLus_id().equals("3760") || currentUser.getLus_id().equals("76") ||
        		 currentUser.getLus_id().equals("39")){
        	namaprod = "SMiLe Link Satu";
        	lsdbsnumber = "19,20,21";
        	namaJudul = "Report SMiLe LINK SATU";
        }else if(jn_bank.equalsIgnoreCase("2") || jn_bank.equalsIgnoreCase("-1")){
        	namaprod = "Simas Power Link";
        	lsdbsnumber = "10,11,12,22,23,24";
        	namaJudul = "Report SIMAS POWER LINK";
        }
       
    	report = new Report( namaJudul, reportPathList, Report.PDF, null );
    	
		report.addParamDefault("username", "username", 200, currentUser.getLus_full_name(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDefault("jn_bank", "jn_bank", 200, jn_bank, false);
		report.addParamDefault("namaprod", "namaprod", 200, namaprod, false);
		report.addParamDefault("lsdbsnumber", "lsdbsnumber", 200, lsdbsnumber, false);
    	report.addParamDate("Tanggal Produksi", "tanggal", true, new Date[] {sysDate, sysDate}, true);
    	return prepareReport( request, response, report );
    }
    
    /**
     * @author Lufi
     * @since September 12, 2014 (18:41:00 PM)
     * Update Sep 12, 2014 : namaJudul req : Jelita
     */    
    public ModelAndView simas_power_link_bsm( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
       
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();
        String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal","");
    	String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir","");
    	
        List<DropDown> reportPathList = new ArrayList<DropDown>();
        
        reportPathList.add( new DropDown(
                props.getProperty( "report.bsm.simas_power_link_nb_bsm" ),
                "New Business"
        	) );

        	reportPathList.add( new DropDown(
                props.getProperty( "report.bsm.simas_power_link_rn_bsm" ),
                "Renewal"
        	) );
        	
        	reportPathList.add( new DropDown(
                props.getProperty( "report.bsm.simas_power_link_tu_bsm" ),
                "Top Up"
            ) );
        	
        String namaprod = "";	
        String lsdbsnumber = "";        
        String namaJudul = "";      
        namaprod = "Simas Power Link";
        lsdbsnumber = "10,11,12,22,23,24";
        namaJudul = "Report SIMAS POWER LINK";
      
    	report = new Report( namaJudul, reportPathList, Report.PDF, null );
    	
		report.addParamDefault("username", "username", 200, currentUser.getLus_full_name(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDefault("jn_bank", "jn_bank", 200, "2", false);
		report.addParamDefault("namaprod", "namaprod", 200, namaprod, false);
		report.addParamDefault("lsdbsnumber", "lsdbsnumber", 200, lsdbsnumber, false);
    	report.addParamDate("Tanggal Produksi", "tanggal", true, new Date[] {sysDate, sysDate}, true);
    	report.addParamDefault("tanggalAwal", "tanggalAwal", 0,tanggalAwal,  false);
		report.addParamDefault("tanggalAkhir", "tanggalAkhir", 0,tanggalAkhir,  false);
    	return prepareReport( request, response, report );
    }
    
    
    /**
     * @author Randy
     * @since 2 Mei 2016
     */    
    
    public ModelAndView laporan_pa_danasejahtera( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {		    	
        logger.info( "BsmController.laporan_pa_danasejahtera" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();
        String extend ="";
        List<DropDown> reportPathList = new ArrayList<DropDown>();
        	
        reportPathList.add( new DropDown(
        		props.getProperty( "report.bsm.laporan_pa" ),
        		"LAPORAN PA"
        		) );

        reportPathList.add( new DropDown(
        		props.getProperty( "report.bsm.laporan_dana_sejahtera" ),
        		"LAPORAN DANA SEJAHTERA"
        		) );
        	 
        report = new Report( "Laporan PA & Dana Sejahtera", reportPathList, Report.PDF, null );

        String jn_bank = Integer.toString(currentUser.getJn_bank());
		report.addParamDefault("username", "username", 200, currentUser.getLus_full_name(), false);
		report.addParamDefault("cab_bank", "cab_bank", 200, currentUser.getCab_bank(), false);
		report.addParamDefault("statement", "statement", 200, extend, false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDefault("jn_bank", "jn_bank", 200, jn_bank, false);
		report.addParamDate("Tanggal  Produksi", "tanggal", true, new Date[] {sysDate, sysDate}, true);

        return prepareReport( request, response, report );
    }
    
    /**
     * Report Summary Outstanding
     *
     * @param request: no desc
     * @param response: no desc
     * @return ModelAndView
     * @throws Exception: no desc
     * @since Mar 1, 2010 (11:30:00 AM)
     */
    public ModelAndView bsm_outstanding( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "BsmController.bsm_outstanding" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();

        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

        if( currentUser.getCab_bank().trim().equals( "SSS" ) )
        {
            reportPathList.add( new DropDown(
                    props.getProperty( "report.bsm.outstanding_all" ),
                    "Report Summary Outstanding Semua Produk Untuk Semua Cabang"
            ) );
        }
        else
        {
            reportPathList.add( new DropDown(
                     props.getProperty( "report.bsm.outstanding_cbg" ),
                    "Report Summary Outstanding Semua Produk Per Cabang"
            ) );
        }

        report = new Report( "Report Summary Outstanding BSM", reportPathList, Report.PDF, null );

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        report.addParamDefault( "username", "username", 0, currentUser.getLus_full_name(), false );
        report.addParamDefault( "lus_id", "lus_id", 200, currentUser.getLus_id(), false );

        if( currentUser.getCab_bank().trim().equals( "SSS" ) )
        {
        }
        else
        {
            report.addParamDefault( "lcb_no", "lcb_no", 200, currentUser.getCab_bank(), false );
        }

        report.addParamDate( "Tanggal", "tanggal", true, new Date[]{ sysDate, sysDate }, true );

        List<Map<String, String>> mataUangList = new ArrayList<Map<String, String>>();
		Map<String, String> tmp;

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "01");
		tmp.put("VALUE", "Rupiah");
		mataUangList.add(tmp);

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "02");
		tmp.put("VALUE", "US Dollar");
		mataUangList.add(tmp);

        report.addParamSelectWithoutAll("Mata Uang", "lku_id", mataUangList, "01", true);

        return prepareReport( request, response, report );
    }
    
    /**
     * Monitoring BSM
     * Ryan
     * @param request
     * @param response
     * @return ModelAndView
     * @throws Exception: no desc
     * @since Mar 01, 2013 (11:30:00 AM)
     */
    
    public ModelAndView monitoring_bsm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param=new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String json = ServletRequestUtils.getStringParameter(request, "json","0");
		String getPath="";
		
		if(json.equals("0")){
			String judul = "Report Monitoring Bank Sinarmas";
			List<Map<String, String>> listAgenNew=new ArrayList();
			List listCabangNew=new ArrayList();
			String wil_no=ServletRequestUtils.getStringParameter(request, "wil_no",currentUser.getLca_id());
			Agen paramAgen=new Agen();
			paramAgen.setMsag_active(1);
			paramAgen.setLstb_id(1);
			List listAgen=new ArrayList();
			listAgen=elionsManager.selectAllAgentFromBranch(paramAgen);
			List<DropDown> listWilayah = elionsManager.selectDropDown("eka.lst_cabang_bii", "lcb_no", "nama_cabang", "", "nama_cabang", "lcb_no = wil_no and jenis = 2 and flag_aktif = 1");
			List<DropDown> listcabang = elionsManager.selectDropDown("eka.lst_cabang_bii", "lcb_no", "nama_cabang", "", "kode", "wil_no = '"+wil_no.toString()+"' and jenis = 2 and flag_aktif = 1");
			List<DropDown> listProduct = elionsManager.selectDropDown("eka.lst_det_bisnis", "(lsbs_id || '-' || lsdbs_number)", "('[' || lsbs_id || '-' || lsdbs_number || '] ' || lsdbs_name)", "", "lsbs_id, lsdbs_number", "lsdbs_aktif = 1 and (lsbs_id || '-' || lsdbs_number) in ('142-2', '164-2', '164-11')");
			
			List<DropDown> listkategori = new ArrayList<DropDown>();
	/*		listkategori.add(new DropDown("1", "New Business"));
			listkategori.add(new DropDown("2", "Top-Up"));
			listkategori.add(new DropDown("3", "Outstanding"));
			listkategori.add(new DropDown("4", "Demografi"));
			listkategori.add(new DropDown("5", "Roll Over"));*/
			
			param.put("listcabang", listcabang);
			param.put("listProduct", listProduct);
			param.put("listCabang", listCabangNew);
			param.put("listkategori",listkategori);
			param.put("listWilayah",listWilayah);
			param.put("judul",judul);
			
			if(request.getParameter("showReport") != null){
				String lcb_no=ServletRequestUtils.getStringParameter(request, "lcb_no","");
		 		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
	    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
	    		String lsbs_id = ServletRequestUtils.getStringParameter(request, "lsbs_id");
	    		//pembagian 
		    		String kategori0=ServletRequestUtils.getStringParameter(request, "kategori0");
		    		String kategori1=ServletRequestUtils.getStringParameter(request, "kategori1");
		    		String kategori2=ServletRequestUtils.getStringParameter(request, "kategori2");
		    		String kategori3=ServletRequestUtils.getStringParameter(request, "kategori3");
		    		String kategori4=ServletRequestUtils.getStringParameter(request, "kategori4");
		    		
	    		String extend ="";
	    		String extend2 ="";
	    		
	    		String lsbsId =lsbs_id.substring(0, 3);
	    		String lsdbsNumber =lsbs_id.substring(4,lsbs_id.length());
				String dir = props.getProperty("pdf.dir.report");
				Date dateBesok=elionsManager.selectSysdate1("dd", false, 0);
				String nama_produk =uwManager.selectLstDetBisnisNamaProduk(Integer.parseInt(lsbsId), Integer.parseInt(lsdbsNumber));
				String cabang =uwManager.selectNamaCabang(lcb_no);
				String wilayah =uwManager.selectNamaCabang(wil_no);
						if (lcb_no.equals("ALL")){
							cabang="ALL";
							//wilayah="ALL";
						}
					
					//pembagian kategori
						if (lsbs_id.equals("164-11")){
							extend ="AND exists (SELECT 1 FROM eka.mst_slink WHERE msl_new in (1,2) AND reg_spaj = a.reg_spaj AND msl_tahun_ke = a.msbi_tahun_ke AND msl_premi_ke = a.msbi_premi_ke)";
							getPath=props.getProperty("report.polis.new_main_slink")+".jasper";
						}
						if (lsbs_id.equals("164-2")){
							extend ="AND exists (SELECT 1 FROM eka.mst_slink WHERE msl_new = 2 AND reg_spaj = a.reg_spaj AND msl_tahun_ke = a.msbi_tahun_ke AND msl_premi_ke = a.msbi_premi_ke)";
							getPath=props.getProperty("report.polis.new_main_slinklama")+".jasper";
						}
						if (lsbs_id.equals("142-2")){
							extend ="AND exists (SELECT 1 FROM eka.mst_powersave_ro WHERE mps_kode = 5 AND reg_spaj = a.reg_spaj AND msbi_tahun_ke = a.msbi_tahun_ke AND msbi_premi_ke = a.msbi_premi_ke)";
							extend2="AND exists (SELECT 1 FROM eka.mst_powersave_ro where mps_kode in (2,3) AND reg_spaj = a.reg_spaj AND msbi_tahun_ke = a.msbi_tahun_ke AND msbi_premi_ke = a.msbi_premi_ke)";
							getPath=props.getProperty("report.polis.new_main_ssprima")+".jasper";
						}
						if (lsbs_id.equals("120-10") || lsbs_id.equals("120-11")|| lsbs_id.equals("120-12")){
							extend ="AND exists (SELECT 1 FROM eka.mst_ulink WHERE mu_ke = 1 AND reg_spaj = a.reg_spaj AND mu_tahun_ke = a.msbi_tahun_ke AND mu_premi_ke = a.msbi_premi_ke)";
							extend2="AND exists (SELECT 1 FROM eka.mst_ulink where lt_id in (2,5) AND reg_spaj = a.reg_spaj AND mu_tahun_ke = a.msbi_tahun_ke AND mu_premi_ke = a.msbi_premi_ke)";
							getPath=props.getProperty("report.polis.new_main_simpol")+".jasper";
						/*	
							extend ="AND exists (SELECT 1 FROM eka.mst_powersave_ro WHERE mps_kode = 5 AND reg_spaj = a.reg_spaj AND msbi_tahun_ke = a.msbi_tahun_ke AND msbi_premi_ke = a.msbi_premi_ke)";
							extend2="AND exists (SELECT 1 FROM eka.mst_powersave_ro where mps_kode in (2,3) AND reg_spaj = a.reg_spaj AND msbi_tahun_ke = a.msbi_tahun_ke AND msbi_premi_ke = a.msbi_premi_ke)";
							getPath=props.getProperty("report.polis.new_main_ssprima")+".jasper";*/
							
						}
							String destDir = props.getProperty("pdf.dir.report");
							Map params = new HashMap();
							params.put("cab_bank", lcb_no.trim());
							params.put("lsbsId", lsbsId);
							params.put("lsdbsNumber", lsdbsNumber);
							params.put("tgl_a", bdate);
							params.put("tgl_b", edate);
							params.put("statement1", extend);
							params.put("statement2", extend2);
							params.put("nama_produk", nama_produk);
							params.put("cabang", cabang);
							params.put("wilayah", wilayah);
							params.put("kategori0", kategori0);
							params.put("kategori1", kategori1);
							params.put("kategori2", kategori2);
							params.put("kategori3", kategori3);
							params.put("kategori4", kategori4);
							
							Connection conn = null;
							try {
								//conn = this.getDataSource().getConnection();
								conn = this.getUwManager().getUwDao().getDataSource().getConnection();
								if(request.getParameter("showPDF") != null){
									dir =dir+"\\"+"Monitoring"+"\\"+"PDF";
									destDir=destDir+"\\"+"Monitoring"+"\\"+"PDF";
											JasperUtils.exportReportToPdf(
													getPath, 
													dir, 
													nama_produk+" ("+cabang+" )"+".pdf", 
													params, 
													conn,
													PdfWriter.AllowPrinting, null, null);
											}else if(request.getParameter("showXLS") != null){
												dir =dir+"\\"+"Monitoring"+"\\"+"XLS"+"\\";
												destDir=destDir+"\\"+"Monitoring"+"\\"+"XLS";
												JasperUtils.exportReportToXls(
														getPath, 
														dir, 
														nama_produk+" ("+cabang+" )"+".xls", 
														params, 
														conn);
											}
							} catch (Exception e) {
								logger.error("ERROR :", e);
							}finally{
								closeConnection(conn);
							}
									
							
							//ServletOutputStream sos = response.getOutputStream();
							File sourceFile = Resources.getResourceAsFile(getPath);
							JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
					
							JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(listAgen));
							
							DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				    		Date now = elionsManager.selectSysdateSimple();
				    		//String destDir = props.getProperty("pdf.dir.report");
				    		String fileName = nama_produk+" ("+cabang+" )"; 
					    	if(request.getParameter("showXLS") != null){
					    		fileName += ".xls";
					    		response.setContentType("application/vnd.ms-excel");
					    	/*	JExcelApiExporter exporter = new JExcelApiExporter();
					            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					            exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, destDir + "\\" + fileName);
					            exporter.exportReport();*/
					    	}else if(request.getParameter("showPDF") != null){
					    		fileName += ".pdf";
					    		//JasperExportManager.exportReportToPdfFile(jasperPrint, destDir + "\\" + fileName);
					    	}
					    	FileUtils.downloadFile("inline", destDir, fileName, response);
				}
				return new ModelAndView("report/monitoring_bsm", param);
			}else{
				String lca = ServletRequestUtils.getStringParameter(request, "wil_no", "ALL");
	    		
				List<DropDown> result = elionsManager.selectDropDown("eka.lst_cabang_bii", "lcb_no", "nama_cabang", "", "kode", "wil_no = '"+lca.toString()+"' and jenis = 2 and flag_aktif = 1 and head_no <>'"+lca.toString()+"'");
	    		
	    		response.setContentType("application/json");
	    		PrintWriter out = response.getWriter();
	    		Gson gson = new Gson();
	    		out.print(gson.toJson(result));
	    		out.close();
			}
			return null;//return new ModelAndView("report/monitoring_bsm", param);
	}
    
    /**
     * @author Andhika
     * @since Mei 15, 2013 (11:30:00 AM)
     */    
    public ModelAndView danamas_stable_link( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "BsmController.danamas_stable_link" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();
        
        List<DropDown> reportPathList = new ArrayList<DropDown>();
        
        	reportPathList.add( new DropDown(
                props.getProperty( "report.bsm.danamas_prima" ),
                "Danamas Prima"
        	) );

        	reportPathList.add( new DropDown(
                props.getProperty( "report.bsm.stable_link" ),
                "Stable Link"
        	) );
        	
        String namaprod = "";	
        String lsdbsnumber = "";
        String jn_bank = Integer.toString(currentUser.getJn_bank());
        
    	report = new Report( "Report", reportPathList, Report.PDF, null );
    	
		report.addParamDefault("username", "username", 200, currentUser.getLus_full_name(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDefault("jn_bank", "jn_bank", 200, jn_bank, false);
		report.addParamDefault("namaprod", "namaprod", 200, namaprod, false);
		report.addParamDefault("lsdbsnumber", "lsdbsnumber", 200, lsdbsnumber, false);
    	report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
    	return prepareReport( request, response, report );
    }  
    
    public ModelAndView danasejahtera_bsim(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Map<String, Object> cmd = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");		
		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
		String edate = ServletRequestUtils.getStringParameter(request, "edate");
		String jn_report = ServletRequestUtils.getStringParameter(request, "jenisReport");		
		String report = ServletRequestUtils.getStringParameter(request, "showReport");		
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");	
		List<DropDown> jenisReport = new ArrayList<DropDown>();			
		jenisReport.add(new DropDown("0","SUMMARY INPUT DANA SEJAHTERA"));
		jenisReport.add(new DropDown("1","REPORT FURTHER"));
		jenisReport.add(new DropDown("2","SUMMARY INFO"));
		cmd.put("bdate", dt.format(elionsManager.selectSysdate()));	
		cmd.put("edate", dt.format(elionsManager.selectSysdate()));
		List data=new ArrayList();
		File sourceFile;
		if(bdate!=null && edate!=null){			
			if(jn_report.equals("0")){
				data.clear();
				data=bacManager.selectReportSummaryInputDanaSejahtera(bdate, edate);
				sourceFile = Resources.getResourceAsFile(props.getProperty("report_input_ds_dmtm") + ".jasper");
			}else if(jn_report.equals("1")){
				data.clear();
				data=bacManager.selectReportFurtherDanaSejahteraBSM( bdate, edate);
				sourceFile = Resources.getResourceAsFile(props.getProperty("report_further_ds_bsim") + ".jasper");
			}else{
				data.clear();
				data=bacManager.selectReportSummaryInfoDanaSejahteraBSM( bdate, edate);
				sourceFile = Resources.getResourceAsFile(props.getProperty("report_summary_info_ds_bsm") + ".jasper");
			}
			if(data.size() > 0){ //bila ada data
	    		
	    		ServletOutputStream sos2 = response.getOutputStream();	    		
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("tanggal1", bdate);
	    		params.put("tanggal2", edate);
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
	    		return null;
    		 }else{ //bila tidak ada data
    			 cmd.put("jenisReport", jenisReport);
    			 cmd.put("pesan", "Tidak Ada Data");
    			return new ModelAndView("report/danasejahtera_bsm",cmd);
    		}		
	    }		
		cmd.put("jenisReport", jenisReport);	
		return new ModelAndView("report/danasejahtera_bsm", cmd);
	}
    
    /**
     * @author Andhika
     * @since sep 1, 2013 (11:30:00 AM)
     */    
    public ModelAndView smile_dana_sejahtera( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "BsmController.smile_dana_sejahtera" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();
        
        List<DropDown> reportPathList = new ArrayList<DropDown>();
        
        	reportPathList.add( new DropDown(
                props.getProperty( "report.bsm.smile_dana_sejahtera" ),
                "Production"
        	) );

        String jn_bank = Integer.toString(currentUser.getJn_bank());
        
    	report = new Report( "Report Produksi SMiLe Dana Sejahtera", reportPathList, Report.PDF, null );
    	
		report.addParamDefault("username", "username", 200, currentUser.getLus_full_name(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDefault("jn_bank", "jn_bank", 200, jn_bank, false);
    	report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);
    	return prepareReport( request, response, report );
    }
    
    /**
     * Report Jatuh Tempo Billing *Ryan*
     *
     * @param request: no desc
     * @param response: no desc
     * @return ModelAndView
     * @throws Exception: no desc
     * @since Mar 15, 2016 (11:30:00 AM)
     */
    public ModelAndView bsm_jatuh_tempo_billing( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "BsmController.bsm_jatuh_tempo" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();
        String extend ="";
        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

        if( currentUser.getCab_bank().trim().equals( "SSS" )  || "39,834".indexOf(currentUser.getLus_id())>-1)
        {
        	extend ="";
        	 reportPathList.add( new DropDown(
                     props.getProperty( "report.bac.view_jatuh_billing" ),
                     " Jatuh tempo billing Kanwill"
             ) );
        }else{
        	extend ="and trim(c.lcb_no) =trim('"+currentUser.getCab_bank()+"')";
        	 reportPathList.add( new DropDown(
                     props.getProperty( "report.bac.view_jatuh_billing" ),
                     " Jatuh tempo billing Per Cabang"
             ) );
        }

        report = new Report( "Report Jatuh Tempo Billing BSM", reportPathList, Report.PDF, null );

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        String jn_bank = Integer.toString(currentUser.getJn_bank());
		report.addParamDefault("username", "username", 200, currentUser.getLus_full_name(), false);
		report.addParamDefault("cab_bank", "cab_bank", 200, currentUser.getCab_bank(), false);
		report.addParamDefault("statement", "statement", 200, extend, false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDefault("jn_bank", "jn_bank", 200, jn_bank, false);
		report.addParamDate("Tanggal  Jatuh Tempo", "tanggal", true, new Date[] {sysDate, sysDate}, true);

        return prepareReport( request, response, report );
    }
    
    /**
     * MANTA
     */
    public ModelAndView reportSimpolJatuhTempo( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        Report report;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysdate = elionsManager.selectSysdate();

    	report = new Report( "Report Simpol Jatuh Tempo", props.getProperty("report.simpol_jatuhtempo"), Report.XLS, null );
    	
    	report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysdate, sysdate}, true);
    	report.addParamDefault("tanggalPrint", "tanggalPrint", 0, df.format(sysdate), false);
    	report.addParamDefault("lcb_no", "lcb_no", 200, currentUser.getCab_bank(), false);
    	return prepareReport( request, response, report );
    }
    
   /* *//**
     * Report Produksi UntuK Bancass
     * Ryan
     * @param request
     * @param response
     * @return ModelAndView
     * @throws Exception: no desc
     * @since Sept 25, 2014 (11:30:00 AM)
     *//*
    
    public ModelAndView monitoring_produksi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param=new HashMap();
		String judul = "Report Produksi & Case Size";
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String json = ServletRequestUtils.getStringParameter(request, "json","0");
		String getPath="";
		List<DropDown> listkategori = new ArrayList<DropDown>();
				listkategori.add(new DropDown("1", "PRODUKSI TOTAL PER KANWIL"));
				listkategori.add(new DropDown("2", "CASE SIZE TOTAL PER KANWIL"));
				listkategori.add(new DropDown("3", "PRODUKSI REGULER PER KANWIL"));
				listkategori.add(new DropDown("4", "CASE SIZE REGULER PER KANWIL"));
				listkategori.add(new DropDown("5", "PRODUKSI SINGLE PREMI PER KANWIL"));
				listkategori.add(new DropDown("6", "CASE SIZE SINGLE PRE PER KANWIL"));
		
				if(request.getParameter("showReport") != null){
					String kode=ServletRequestUtils.getStringParameter(request, "kode","");
		    		String extend ="";
		    		String extend2 ="";
		    		
					String dir = props.getProperty("pdf.dir.report");
						
						//pembagian kategori
							if (kode.equals("164-11")){
								extend ="AND exists (SELECT 1 FROM eka.mst_slink WHERE msl_new in (1,2) AND reg_spaj = a.reg_spaj AND msl_tahun_ke = a.msbi_tahun_ke AND msl_premi_ke = a.msbi_premi_ke)";
								getPath=props.getProperty("report.polis.new_main_slink")+".jasper";
							}
							if (kode.equals("164-2")){
								extend ="AND exists (SELECT 1 FROM eka.mst_slink WHERE msl_new = 2 AND reg_spaj = a.reg_spaj AND msl_tahun_ke = a.msbi_tahun_ke AND msl_premi_ke = a.msbi_premi_ke)";
								getPath=props.getProperty("report.polis.new_main_slinklama")+".jasper";
							}
							if (kode.equals("142-2")){
								extend ="AND exists (SELECT 1 FROM eka.mst_powersave_ro WHERE mps_kode = 5 AND reg_spaj = a.reg_spaj AND msbi_tahun_ke = a.msbi_tahun_ke AND msbi_premi_ke = a.msbi_premi_ke)";
								extend2="AND exists (SELECT 1 FROM eka.mst_powersave_ro where mps_kode in (2,3) AND reg_spaj = a.reg_spaj AND msbi_tahun_ke = a.msbi_tahun_ke AND msbi_premi_ke = a.msbi_premi_ke)";
								getPath=props.getProperty("report.polis.new_main_ssprima")+".jasper";
							}
							if (kode.equals("120-10")){
								extend ="AND exists (SELECT 1 FROM eka.mst_ulink WHERE mu_ke = 1 AND reg_spaj = a.reg_spaj AND mu_tahun_ke = a.msbi_tahun_ke AND mu_premi_ke = a.msbi_premi_ke)";
								extend2="AND exists (SELECT 1 FROM eka.mst_ulink where lt_id in (2,5) AND reg_spaj = a.reg_spaj AND mu_tahun_ke = a.msbi_tahun_ke AND mu_premi_ke = a.msbi_premi_ke)";
								getPath=props.getProperty("report.polis.new_main_simpol")+".jasper";
								
								extend ="AND exists (SELECT 1 FROM eka.mst_powersave_ro WHERE mps_kode = 5 AND reg_spaj = a.reg_spaj AND msbi_tahun_ke = a.msbi_tahun_ke AND msbi_premi_ke = a.msbi_premi_ke)";
								extend2="AND exists (SELECT 1 FROM eka.mst_powersave_ro where mps_kode in (2,3) AND reg_spaj = a.reg_spaj AND msbi_tahun_ke = a.msbi_tahun_ke AND msbi_premi_ke = a.msbi_premi_ke)";
								getPath=props.getProperty("report.polis.new_main_ssprima")+".jasper";
								
							}
								String destDir = props.getProperty("pdf.dir.report");
								Map params = new HashMap();
								params.put("statement1", extend);
								
								if(request.getParameter("showPDF") != null){
										dir =dir+"\\"+"Monitoring"+"\\"+"PDF";
										destDir=destDir+"\\"+"Monitoring"+"\\"+"PDF";
												JasperUtils.exportReportToPdf(
														getPath, 
														dir, 
														"test"+".pdf", 
														params, 
														this.connection = this.getDataSource().getConnection(),
														PdfWriter.AllowPrinting, null, null);
												}else if(request.getParameter("showXLS") != null){
													dir =dir+"\\"+"Monitoring"+"\\"+"XLS"+"\\";
													destDir=destDir+"\\"+"Monitoring"+"\\"+"XLS";
													JasperUtils.exportReportToXls(
															getPath, 
															dir, 
															 "test"+".xls", 
															params, 
															this.connection = this.getDataSource().getConnection());
												}
										
								
								//ServletOutputStream sos = response.getOutputStream();
								File sourceFile = Resources.getResourceAsFile(getPath);
								JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
						
								JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(null));
								
								DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
					    		Date now = elionsManager.selectSysdateSimple();
					    		String fileName ="test"; 
						    	if(request.getParameter("showXLS") != null){
						    		fileName += ".xls";
						    		response.setContentType("application/vnd.ms-excel");
						    	}else if(request.getParameter("showPDF") != null){
						    		fileName += ".pdf";
						    	}
						    	FileUtils.downloadFile("inline", destDir, fileName, response);
					}
				
		String today = defaultDateFormat.format(elionsManager.selectSysdate());
	    param.put("today", today);
		param.put("judul",judul);
		param.put("listkategori",listkategori);
		return new ModelAndView("report/monitoring_produksi", param);
	}*/
}