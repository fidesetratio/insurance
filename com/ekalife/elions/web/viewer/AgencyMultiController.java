package com.ekalife.elions.web.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsXlsView;

import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentMultiController;

/**
 * @author Yusuf
 * @since Jan 11, 2006
 */
public class AgencyMultiController extends ParentMultiController{

	public ModelAndView agen(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String msag_id = request.getParameter("msag_id");
		Map map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		List agen = new ArrayList();
		Agen detil = new Agen();
		String kode_leader =msag_id;
		Integer flag_bm = new Integer(0);
		Integer flag_sbm = new Integer(0);
		Integer flag_agent = new Integer(0);
		do{
			msag_id =kode_leader;
			detil = (Agen) this.elionsManager.selectdetilagen(msag_id);
			if (detil != null)
			{
				flag_sbm = detil.getMsag_sbm();
				flag_bm = detil.getMsag_flag_bm();
				flag_agent = detil.getLsle_id();
				
				if ((flag_agent.intValue() == 1 )||(flag_agent.intValue() == 2 ))
				{
					if (flag_agent.intValue() == 1)
					{
						if (flag_sbm == null)
						{
							flag_sbm = new Integer(0);
						}
						if (flag_sbm.intValue() == 1)
						{
							detil.setLsle_name("SENIOR BRANCH MANAGER");
						}
					}else{
						if (flag_bm == null)
						{
							flag_bm= new Integer(0);
						}
							if (flag_bm.intValue() == 1)
							{
								detil.setLsle_name("BRANCH MANAGER");
							}
					}
				}
				agen.add(detil);
				kode_leader = detil.getMst_leader();
				if (kode_leader ==null)
				{
					kode_leader ="";
				}
			}else{
				kode_leader = "";
			}
		}while (!kode_leader.equalsIgnoreCase(""));
		map.put("ketagen", agen);
		
		return new ModelAndView("uw/viewer/level_agen", "cmd", map);
	}
	
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
        User currentUser = (User) request.getSession().getAttribute("currentUser");
 		return new ModelAndView("uw/viewer/agency", "cmd", map);
	}
	
	public ModelAndView generateExcelRegBp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map params = new HashMap();
		AbstractJasperReportsView jasperViewer = new JasperReportsXlsView();
        Map<JRExporterParameter, Object> exporterParam = new HashMap<JRExporterParameter, Object>();
        exporterParam.put(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        exporterParam.put(JRXlsExporterParameter.SHEET_NAMES, new String[]{"Report Reg BP"});
        exporterParam.put(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.FALSE);
        exporterParam.put(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
        exporterParam.put(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
        exporterParam.put(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporterParam.put(JRXlsExporterParameter.CHARACTER_ENCODING, "UTF-8");
        jasperViewer.setExporterParameters( exporterParam );
        String reportPath = props.getProperty("report.uw.report_generate_regbponline") + ".jasper";
        jasperViewer.setUrl( "/WEB-INF/classes/" + reportPath );
        
        Object dataSource;
		dataSource = elionsManager.getUwDao().getDataSource();
		jasperViewer.setJdbcDataSource( (DataSource) dataSource);
		params.put("dataSource", dataSource);
   
        //jasperViewer.setReportDataKey( "dataSource" );

        jasperViewer.setApplicationContext( WebApplicationContextUtils.getWebApplicationContext( request.getSession().getServletContext() ) );
        return new ModelAndView( jasperViewer, params );
	}
	
    public ModelAndView report_pas_sms_partner(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Map params = new HashMap();
		AbstractJasperReportsView jasperViewer = new JasperReportsXlsView();
        Map<JRExporterParameter, Object> exporterParam = new HashMap<JRExporterParameter, Object>();
        exporterParam.put(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        exporterParam.put(JRXlsExporterParameter.SHEET_NAMES, new String[]{"Report Pas SMS BP"});
        exporterParam.put(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporterParam.put(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
        exporterParam.put(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
        exporterParam.put(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
        exporterParam.put(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporterParam.put(JRXlsExporterParameter.CHARACTER_ENCODING, "UTF-8");
        jasperViewer.setExporterParameters( exporterParam );
        String reportPath = props.getProperty("report.uw.report_pas_sms_bp") + ".jasper";
        jasperViewer.setUrl( "/WEB-INF/classes/" + reportPath );
        
        Object dataSource;
		dataSource = elionsManager.getUwDao().getDataSource();
		jasperViewer.setJdbcDataSource( (DataSource) dataSource);
		params.put("dataSource", dataSource);
   
        //jasperViewer.setReportDataKey( "dataSource" );

        jasperViewer.setApplicationContext( WebApplicationContextUtils.getWebApplicationContext( request.getSession().getServletContext() ) );
        return new ModelAndView( jasperViewer, params );
    }

    public ModelAndView report_pas_sms_uw(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Map params = new HashMap();
    	AbstractJasperReportsView jasperViewer = new JasperReportsXlsView();
    	Map<JRExporterParameter, Object> exporterParam = new HashMap<JRExporterParameter, Object>();
        exporterParam.put(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
        exporterParam.put(JRXlsExporterParameter.SHEET_NAMES, new String[]{"Report Pas SMS UW"});
        exporterParam.put(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporterParam.put(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
        exporterParam.put(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.FALSE);
        exporterParam.put(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
        exporterParam.put(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporterParam.put(JRXlsExporterParameter.CHARACTER_ENCODING, "UTF-8");
        jasperViewer.setExporterParameters( exporterParam );
        String reportPath = props.getProperty("report.uw.report_pas_sms_uw") + ".jasper";
        jasperViewer.setUrl( "/WEB-INF/classes/" + reportPath );
        
        Object dataSource;
		dataSource = elionsManager.getUwDao().getDataSource();
		jasperViewer.setJdbcDataSource( (DataSource) dataSource);
		params.put("dataSource", dataSource);
		//jasperViewer.setReportDataKey( "dataSource" );

        jasperViewer.setApplicationContext( WebApplicationContextUtils.getWebApplicationContext( request.getSession().getServletContext() ) );
    	
    	return new ModelAndView( jasperViewer, params);
    }
}
