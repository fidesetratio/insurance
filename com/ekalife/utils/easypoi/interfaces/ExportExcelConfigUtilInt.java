/**********************************************************************
 * Program History
 *
 * Project Name      	: Inventory and Costing System
 * Client Name       	: Toyota Motor Manufacturing Indonesia 
 * Program Id         	: ExportExcelConfigUtilInt.java
 * Program Name   		: ExportExcelConfigUtilInt
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

import java.sql.SQLException;

/**
 * @author Samuel
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ExportExcelConfigUtilInt
{
	public String getTemplateDirectoryPath()
		throws SQLException;
	public String getTimeStamp()
		throws SQLException;
}
