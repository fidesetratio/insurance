package com.ekalife.utils;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Bancass
 * Function Id         	: 
 * Program Name   		: DbUtil
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Nov 8, 2007 4:23:25 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.Date;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DbUtil
{
	protected static final Log logger = LogFactory.getLog( DbUtil.class );

    public static java.sql.Date convertToSqlDate( Date dtDate )
    {
        return dtDate == null ? null : new java.sql.Date( dtDate.getTime() );
    }

    public static void assignParams( PreparedStatement ps, Object oParams) throws SQLException
    {
        if (oParams == null) {
            return;
        } else {
            if (oParams instanceof Object[]) {
                Object[] arrParam = (Object[]) oParams;

                for (int i = 0; i < arrParam.length; i++) {
                    if (arrParam[i] instanceof java.util.Date) {
                        ps.setDate(i + 1, convertToSqlDate((java.util.Date) arrParam[i]));
                    } else if (arrParam[i] != null) {
                        if (arrParam[i] instanceof java.lang.Long) {
                            ps.setLong(i + 1, ((Long) arrParam[i]).longValue());
                        } else if (arrParam[i] instanceof java.lang.Double) {
                            ps.setDouble(i + 1, ((Double) arrParam[i]).doubleValue());
                        } else if (arrParam[i] instanceof java.lang.String) {
                            ps.setString(i + 1, ((String) arrParam[i]));
                        } else if (arrParam[i] instanceof java.lang.Integer) {
                            ps.setInt(i + 1, ((Integer) arrParam[i]).intValue());
                        } else if(arrParam[i] instanceof BigDecimal ){
                        	ps.setBigDecimal(i + 1, ((BigDecimal) arrParam[i]));

                        }

                    } else if (arrParam[i] == null) {

                        ps.setString(i + 1, (String) arrParam[i]);

                    } else {
                        ps.setObject(i + 1, arrParam[i]);
                    }
                }
            } else {
                //if user sent only one paramater, the ADMPARMSC will not be converted to uppercase.
                //This is done because in TMSD2 application there is no table with one and only one field.
                ps.setObject(1, oParams);
            }
        }
    }

    public static ResultSet executeQueryToResultSet( Connection con,String stQuery, Object oParams) throws SQLException {
        ResultSet rs;
        PreparedStatement ps = con.prepareStatement(stQuery);
        assignParams( ps, oParams );
        rs = ps.executeQuery();
        return rs;
    }

    public static ArrayList executeQuery(Connection con, String stQuery, Object oParams) throws SQLException {
        ArrayList alResult = new ArrayList();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
		        ps = con.prepareStatement(stQuery);
		        DbUtil.assignParams(ps, oParams);
		        rs = ps.executeQuery();
		        int intNumColumn = rs.getMetaData().getColumnCount();
		        if (intNumColumn == 1) {
		            while (rs.next()) {
		                alResult.add(rs.getObject(1));
		            }
		        } else {
		            while (rs.next()) {
		                Object[] obRow = new Object[intNumColumn];
		                for (int i = 1; i <= intNumColumn; i++) {
		                    obRow[i - 1] = rs.getObject(i);
		                }
		                alResult.add(obRow);
		            }
		        }
        }finally{
        	try {
				if(ps != null) {
					ps.close();
				}
				if(rs != null) {
					rs.close();
				}
			}catch (Exception e) {
				logger.error("ERROR :", e);
			}
        }
        return alResult;
    }
}
