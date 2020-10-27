/**********************************************************************
 * Program History
 *
 * Project Name      	: Inventory and Costing System
 * Client Name       	: Toyota Motor Manufacturing Indonesia 
 * Program Id         	: ExportExcelDbUtilInt.java
 * Program Name   		: ExportExcelDbUtilInt
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


package com.ekalife.utils.easypoi.interfaces;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ExportExcelDbUtilInt
{
    public Connection getConnection()
            throws SQLException;
    public void closeConnection()
            throws SQLException;
    public ResultSet executeQuery( String query, Object params )
            throws SQLException;
    public List executeQueryToList( String query, Object params )
    	throws SQLException;
    public String getCurrentDate( String format )
		throws SQLException;
}
