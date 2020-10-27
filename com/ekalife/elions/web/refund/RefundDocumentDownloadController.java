package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundDocumentDownloadController
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Sep 29, 2008 11:43:59 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import com.ekalife.elions.web.refund.vo.RefundDocumentVO;
import com.lowagie.text.pdf.PdfWriter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RefundDocumentDownloadController implements Controller
{
    protected final Log logger = LogFactory.getLog( getClass() );
    
    public ModelAndView handleRequest( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundDocumentDownloadController.handleRequest" );

        RefundDocumentVO refundDocumentVO = ( RefundDocumentVO ) request.getSession().getAttribute( "documentTobeDownload" );

        AbstractJasperReportsView jasperViewer = new JasperReportsPdfView();

        Map<JRExporterParameter, Object> exporterParam = new HashMap<JRExporterParameter, Object>();
        exporterParam.put( JRPdfExporterParameter.IS_ENCRYPTED, Boolean.TRUE );
        exporterParam.put( JRPdfExporterParameter.IS_128_BIT_KEY, Boolean.TRUE );
        exporterParam.put( JRPdfExporterParameter.PERMISSIONS, PdfWriter.AllowPrinting );

        jasperViewer.setExporterParameters( exporterParam );
        jasperViewer.setUrl( "/WEB-INF/classes/" + refundDocumentVO.getJasperFile() );
        jasperViewer.setReportDataKey( "dataSource" );
        jasperViewer.setApplicationContext( WebApplicationContextUtils.getWebApplicationContext( request.getSession().getServletContext() ) );

        Map<String, Object> params = refundDocumentVO.getParams();

        return new ModelAndView( jasperViewer, params );
    }

}
