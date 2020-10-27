package com.ekalife.utils.easypoi.interfaces;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Bancass
 * Function Id         	: 
 * Program Name   		: ExpressionInt
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Nov 13, 2007 11:32:05 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.util.Map;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ExpressionInt
{
    public Object express( String dbFieldName, Map<String, Object> map, ResultSet rs )
            throws SQLException;
}
