/**********************************************************************
 * Program History
 *
 * Project Name      	: Inventory and Costing System
 * Client Name       	: Toyota Motor Manufacturing Indonesia 
 * Program Id         	: ExportExcelDbUtil.java
 * Program Name   		: ExportExcelDbUtil
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


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.service.ElionsManager;
import com.ekalife.utils.DbUtil;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.easypoi.interfaces.ExportExcelDbUtilInt;

public class ExportExcelDbUtil implements ExportExcelDbUtilInt
{
    private Connection connection;
    private ElionsManager elionsManager;

    protected final Log logger = LogFactory.getLog( getClass() );
    
    public ExportExcelDbUtil( ElionsManager elionsManager, Connection connection ) throws SQLException
	{
    	this.elionsManager = elionsManager;
        this.connection = connection;
    }

    public Connection getConnection()
    {
        return connection;
    }

    public void closeConnection()
            throws SQLException
    {
    	if( connection != null && !connection.isClosed() )
    	{
    		connection.close();
    		logger.debug( "ExportExcelDbUtil close connection." );
    	}
    }

    public ResultSet executeQuery( String query, Object params )
            throws SQLException
    {
        logger.debug( "ExportExcelDbUtil.executeQuery sam" );


        return DbUtil.executeQueryToResultSet( getConnection(), query, params );
    }

    public List executeQueryToList( String query, Object params )
    	throws SQLException
	{
    	logger.debug( "ExportExcelDbUtil.executeQuery sam" );

    	return DbUtil.executeQuery( getConnection(), query, params );
	}
    
    // TODO
	public String getCurrentDate( String format )
		throws SQLException
	{
        Date nowDate = elionsManager.selectNowDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( format );
        return simpleDateFormat.format( nowDate );
	}

    public String getCurrentDateInLanguage()
		throws SQLException
	{
        Date nowDate = elionsManager.selectNowDate();
        return FormatDate.toIndonesian( nowDate );
	}

    public String getCompleteCurrentDateInLanguage()
		throws SQLException
	{
        SimpleDateFormat time = new SimpleDateFormat( "HH:mm:ss" );
        Date nowDate = elionsManager.selectNowDate();
        return FormatDate.toIndonesian( nowDate ) + ", " + time.format( nowDate );
	}
}
