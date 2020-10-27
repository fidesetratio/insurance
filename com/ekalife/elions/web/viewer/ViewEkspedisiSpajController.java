package com.ekalife.elions.web.viewer;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Proposal
 * Function Id         	: 
 * Program Name   		: ViewEkspedisiSpajController
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Jun 29, 2007 3:54:52 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentMultiController;
import com.lowagie.text.pdf.PdfWriter;

public class ViewEkspedisiSpajController  extends ParentMultiController
{

    public ModelAndView main( HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        Map< String, Object > paramsMap = new HashMap< String, Object >();
        String tanggalAwal = request.getParameter( "tanggalAwal" );
        String tanggalAkhir = request.getParameter( "tanggalAkhir" );
        List resultList = null;
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        if(request.getParameter("search") != null) {
            if( tanggalAwal != null && tanggalAkhir != null ) resultList = elionsManager.selectEkpedisiSpaj( tanggalAwal, tanggalAkhir, null );
        } else if(request.getParameter("print") != null) {
            String[] daftarSpaj = request.getParameterValues("checkFlag");
            
            if( daftarSpaj.length > 0 )
            {
        
            	AbstractJasperReportsView jasperViewer = new JasperReportsPdfView();
        		Map params = new HashMap();
        		params.put("format", "pdf");
        		
        		Map<JRExporterParameter, Object> exp = new HashMap<JRExporterParameter, Object>(); 
    			exp.put(JRPdfExporterParameter.IS_ENCRYPTED, Boolean.TRUE);
    			exp.put(JRPdfExporterParameter.IS_128_BIT_KEY, Boolean.TRUE);
    			exp.put(JRPdfExporterParameter.PERMISSIONS, new Integer(PdfWriter.AllowPrinting));
    			
    			jasperViewer.setExporterParameters(exp);
    			jasperViewer.setUrl("/WEB-INF/classes/"+props.getProperty("report.ekspedisi_spaj")+".jasper");

    			jasperViewer.setReportDataKey("dataSource");
            	params.put("dataSource", elionsManager.selectEkpedisiSpaj(tanggalAwal, tanggalAkhir, daftarSpaj));
    			params.put("username", currentUser.getName());
    			params.put("tanggalAwal", tanggalAwal);
    			params.put("tanggalAkhir", tanggalAkhir);
            	
    			jasperViewer.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()));
    			
    			//update flag
    			elionsManager.updateFlagEkspedisiSpaj(daftarSpaj);
    			
    			return new ModelAndView(jasperViewer, params);
            }
        } else {
        	
        }
        
        paramsMap.put("username", currentUser.getName());
        paramsMap.put( "tanggalAwal", tanggalAwal );
        paramsMap.put( "tanggalAkhir", tanggalAkhir );
        paramsMap.put( "resultList", resultList );

        return new ModelAndView("bas/viewer/viewEkspedisiSpaj", paramsMap );
    }

}