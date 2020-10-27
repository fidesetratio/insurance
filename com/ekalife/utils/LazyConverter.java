package com.ekalife.utils;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: LazyConverter
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 30, 2008 4:07:34 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;

public class LazyConverter
{
    protected final Log logger = LogFactory.getLog( getClass() );

    public LazyConverter()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* LazyConverter constructor is called ..." );
    }

    /**
     * if null return defaultValueIfNull otherwise convert BigDecimal to double
     * @param value: bigdecimal value
     * @param defaultValueIfNull: double value
     * @return double
     */
    public static Double toDouble( BigDecimal value, Double defaultValueIfNull )
    {
        Double result;
        if( value == null )
        {
            result = defaultValueIfNull;
        }
        else
        {
            result = value.doubleValue();
        }
        return result;
    }

    /**
     * if null return 0 otherwise convert BigDecimal to double
     * @param value: bigdecimal value
     * @return double
     */
    public static double toDouble( BigDecimal value )
    {
        return toDouble( value, 0.0 );
    }

    /**
     * if null return 0 otherwise convert BigDecimal to double
     * @param value: bigdecimal value
     * @return double
     */
    public static Double toDouble( String value )
    {

        return toDouble( new BigDecimal( value ), 0.0 );
    }

    public static Double toDouble( String value, Double defaultValue )
    {
        Double result;
        if( value == null )
        {
            result = defaultValue;
        }
        else
        {
            try
            {
                BigDecimal valueBd = new BigDecimal( value.trim() );
                result = valueBd.doubleValue();
            }
            catch( Exception e )
            {
                result = defaultValue;
            }
        }
        return result;
    }

      /**
     * if null return defaultValueIfNull otherwise convert BigDecimal to int
     * @param value: bigdecimal value
     * @param defaultValueIfNull: int value
     * @return int
     */
    public static int toInt( Integer value, int defaultValueIfNull )
    {
        int result;
        if( value == null )
        {
            result = defaultValueIfNull;
        }
        else
        {
            result = value;
        }
        return result;
    }

        /**
     * if null return 0 otherwise convert BigDecimal to int
     * @param value: bigdecimal value
     * @return int
     */
    public static int toInt( Integer value )
    {
        return toInt( value, 0 );
    }

    /**
     * if null or convertion error return defaultValueIfNull otherwise convert String to int
     * @param value: String value
     * @param defaultValueIfNull: int value
     * @return int
     */
    public static int toInt( String value, int defaultValueIfNull )
    {
        int result;
        if( value == null )
        {
            result = defaultValueIfNull;
        }
        else
        {
            value = value.trim();
            try
            {
                result = Integer.parseInt( value );
            }
            catch( NumberFormatException e )
            {
                result = defaultValueIfNull;
            }
        }
        return result;
    }

    /**
     * if null or convertion error return 0 otherwise convert String to int
     * @param value: String value
     * @return int
     */
    public static int toInt( String value )
    {
        return toInt( value, 0 );
    }

    /**
     * if null or convertion error return 0 otherwise convert String to int
     * @param value: String value
     * @return int
     */
    public static String toString( int value )
    {
        return Integer.toString( value );
    }

}
