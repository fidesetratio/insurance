/**********************************************************************
 * Program History
 *
 * Project Name      	: Inventory and Costing System
 * Client Name       	: Toyota Motor Manufacturing Indonesia 
 * Program Id         	: ExportExcelQueryBuilder.java
 * Program Name   		: ExportExcelQueryBuilder
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


package com.ekalife.utils.easypoi.createexcel;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.utils.easypoi.vo.ParameterizedQueryVO;

/**
 * User: Samuel
 * Date: Mar 28, 2007
 * Time: 4:36:27 PM
 */
public class ExportExcelQueryBuilder
{
	protected static final Log logger = LogFactory.getLog( ExportExcelQueryBuilder.class );
	
    public static ParameterizedQueryVO constructQuery( StringBuffer selectClause, StringBuffer whereClause, StringBuffer orderClause, TreeMap filterParamsMap )
    {
    	logger.debug( "_________ExportExcelQueryBuilder.constructQuery " );
        ParameterizedQueryVO result = new ParameterizedQueryVO();

        StringBuffer query = new StringBuffer( "" );
        List paramsList = new ArrayList();
        String key;
        String value;
        
        Iterator paramsIterator = filterParamsMap.keySet().iterator();
        while( paramsIterator.hasNext() )
        {
            key = ( ( String ) paramsIterator.next() );
            value = ( String ) filterParamsMap.get( key );
            key = key.toUpperCase();
            logger.debug( " ________key = " + key );
            logger.debug( " ______value = " + value );
            
            
            if( value != null && !value.trim().equals( "" ) )
            {
            	// TODO: type of operator, you can add your code here
            	if( key.indexOf( "AND " ) != -1 )
            	{
            		key = key.replaceAll( "AND ", "" );
            		whereClause.append( " and ");
            	} 
            	else if( key.indexOf( "OR " ) != -1 )
            	{
            		key = key.replaceAll( "OR ", "" );
            		whereClause.append( " or ");
            	} 
            	else
            	{
            		// if it's not mentioned, the default is operator AND
            		whereClause.append( " and ");
            	} 	
            	
//              TODO: type of operator with parameter, you can add your code here
            	if( key.indexOf( "LIKE " ) != -1 )
            	{
            		key = key.replaceAll( "LIKE ", "" );
            		whereClause.append( " upper( " );
            		whereClause.append( key );
            		whereClause.append( " ) " );
            		whereClause.append( " like upper( ? )" );
            		value = "%" + value + "%";
            	} 
            	else if( key.indexOf( "GREATER_DATE " ) != -1 )
            	{
            		key = key.replaceAll( "GREATER_DATE ", "" );
            		whereClause.append( key );
            		whereClause.append( " >= to_date(?,'dd.mm.yyyy') " );
            	} 
            	else if( key.indexOf( "LESS_DATE " ) != -1 ) 
            	{
            		key = key.replaceAll( "LESS_DATE ", "" );
            		whereClause.append( key );
            		whereClause.append( " <= to_date(?,'dd.mm.yyyy') " );
            	} 
            	else if( key.indexOf( "LESS_THAN " ) != -1 ) 
            	{
            		key = key.replaceAll( "LESS_THAN ", "" );
            		whereClause.append( key );
            		whereClause.append( " < ? " );
            	} 
            	else if( key.indexOf( "GREATER_THAN " ) != -1 ) 
            	{
            		key = key.replaceAll( "GREATER_THAN ", "" );
            		whereClause.append( key );
            		whereClause.append( " > ? " );
            	} 
            	else if( key.indexOf( "LESS_EQUAL_THAN " ) != -1 ) 
            	{
            		key = key.replaceAll( "LESS_EQUAL_THAN ", "" );
            		whereClause.append( key );
            		whereClause.append( " <= ? " );
            	} 
            	else if( key.indexOf( "GREATER_EQUAL_THAN " ) != -1 ) 
            	{
            		key = key.replaceAll( "GREATER_EQUAL_THAN ", "" );
            		whereClause.append( key );
            		whereClause.append( " >= ? " );
            	} 
            	else
            	{
            		whereClause.append( key );
            		whereClause.append( " = ? ");
            	}
            	logger.debug( " ________where clause query = " + whereClause );
                paramsList.add( value );
            }
        }

        query.append( selectClause ).append( whereClause ).append( orderClause );
        
        result.setQuery( query.toString() );
        result.setParams( paramsList );

        return result;
    }
}
