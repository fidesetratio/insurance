package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Bancass
 * Function Id         	: 
 * Program Name   		: Cebc01030202DownloadController
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Nov 15, 2007 3:46:37 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentController;

public class RefundRedemptExcelDownloadController extends ParentController 
{
	protected final Log logger = LogFactory.getLog( getClass() );
	
    public ModelAndView handleRequest( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException, SQLException
    {
        logger.info( "*-*-*-* RefundRedemptExcelDownloadController.handleRequest" );
        Map<String, Object> params = new HashMap<String, Object>();
        RefundRedemptExcelView view = new RefundRedemptExcelView( elionsManager, this.getElionsManager().getUwDao().getDataSource().getConnection() );

        return new ModelAndView( view, params );
    }
    
}