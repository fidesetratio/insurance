/**********************************************************************
 * Program History
 *
 * Project Name      	: Inventory and Costing System
 * Client Name       	: Toyota Motor Manufacturing Indonesia 
 * Program Id         	: ExportExcelConfigUtil.java
 * Program Name   		: ExportExcelConfigUtil
 * Description         	: 
 * Environment      	: Java  1.4.2
 * Author               : Samuel Baktiar
 * Version              : 
 * Creation Date    	: Apr 3, 2007 1:00:00 PM
 *
 * Update history   Re-fix date      Person in charge      Description
 *
 * Copyright(C) 2007-TOYOTA Motor Manufacturing Indonesia. All Rights Reserved.
 ***********************************************************************/


package com.ekalife.utils.easypoi.specific;


import java.io.File;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.utils.easypoi.interfaces.ExportExcelConfigUtilInt;
import com.ekalife.utils.easypoi.interfaces.ExportExcelDbUtilInt;

public class ExportExcelConfigUtil implements ExportExcelConfigUtilInt
{
	protected final Log logger = LogFactory.getLog( getClass() );
	
	private static ExportExcelDbUtilInt dbUtil;
	
	public ExportExcelConfigUtil( ExportExcelDbUtilInt dbUtil )
	{
		setDbUtil( dbUtil );
	}
	
	public String getTemplateDirectoryPath()
		throws SQLException
    {
		return "/WEB-INF/classes/com/ekalife/elions/reports/refund/excel";
    }
	
	
	public String getTimeStamp()
		throws SQLException
	{
		String DATE_FORMAT = "yyyyMMdd_HHmmss";
		return getDbUtil().getCurrentDate( DATE_FORMAT );
	}
	
	public boolean isExistDirectory( String directoryPath )
	{
        logger.info( "*-*-*-* directoryPath = " + directoryPath );
        boolean result;
        File dir = new File( directoryPath );
        if( dir.isDirectory() )
        {
        	result = true;
        }
        else
        {
            result = false;
        }
        return result;
	}
	
	public boolean isExistFile( String filePath )
	{
		boolean result;
        File file = new File( filePath );
        if( file.isFile() )
        {
        	result = true;
        }
        else
        {
            result = false;
        }
        return result;
	}
	
	
	/**
	 * @return Returns the dbUtil.
	 */
	public ExportExcelDbUtilInt getDbUtil() {
		return dbUtil;
	}
	/**
	 * @param dbUtil The dbUtil to set.
	 */
	public void setDbUtil( ExportExcelDbUtilInt dbUtil) {
		ExportExcelConfigUtil.dbUtil = dbUtil;
	}
}
