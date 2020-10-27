package com.ekalife.elions.web.uw;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: MedicalEmailDownloadController
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Mar 25, 2008 4:47:12 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/


import com.lowagie.text.pdf.PdfWriter;
import com.ekalife.elions.model.MedicalEmailForm;
import com.ekalife.elions.web.uw.business.MedicalEmailBusiness;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MedicalEmailDownloadController implements Controller
{
	protected final Log logger = LogFactory.getLog( getClass() );
    private MedicalEmailBusiness business;

    public MedicalEmailDownloadController()
    {
        this.business = new MedicalEmailBusiness();
    }

    public ModelAndView handleRequest( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException
    {
        logger.info( "*-*-*-* Cepr01030202DownloadController.handleRequest" );

        Object command = request.getSession().getAttribute( "command" );
        MedicalEmailForm medicalEmailForm = ( MedicalEmailForm ) command;

        AbstractJasperReportsView jasperViewer = new JasperReportsPdfView();

        Map<JRExporterParameter, Object> exporterParam = new HashMap<JRExporterParameter, Object>();
        exporterParam.put( JRPdfExporterParameter.IS_ENCRYPTED, Boolean.TRUE );
        exporterParam.put( JRPdfExporterParameter.IS_128_BIT_KEY, Boolean.TRUE );
        exporterParam.put( JRPdfExporterParameter.PERMISSIONS, PdfWriter.AllowPrinting );

        jasperViewer.setExporterParameters( exporterParam );
        jasperViewer.setUrl( "/WEB-INF/classes/" + "com/ekalife/elions/reports/uw/medical_email.jasper" );



        Map<String, Object> params;
        params = business.getDownloadParams( medicalEmailForm );

        jasperViewer.setReportDataKey( "dataSource" );
        params.put( "dataSource", business.genTableDetail( medicalEmailForm ) );
        params.put( "format", "pdf" );

        jasperViewer.setApplicationContext( WebApplicationContextUtils.getWebApplicationContext( request.getSession().getServletContext() ) );

        return new ModelAndView( jasperViewer, params );
    }

}
