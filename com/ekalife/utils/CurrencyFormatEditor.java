package com.ekalife.utils;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: CurrencyFormatEditor
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 13, 2008 2:44:51 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import org.springframework.util.StringUtils;
import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyFormatEditor extends PropertyEditorSupport
{
    public String currencyFormat;

    public CurrencyFormatEditor( String decimalPattern )
    {
        this.currencyFormat = decimalPattern;
    }

    public void setAsText( String text ) throws IllegalArgumentException
    {
        if( !StringUtils.hasText( text ) )
        {
            setValue( null );
        }
        else
        {
        	String numeric = "([^-|^0-9])";
            Pattern p = Pattern.compile( numeric);
            Matcher m = p.matcher( text );
            String value = m.replaceAll( "" );
            BigDecimal amount = ( new BigDecimal( value ) ).divide( new BigDecimal( "100" ), MathContext.DECIMAL64 );
            setValue( amount );
        }
    }

    public String getAsText()
    {
        String result = "";
        Object value = getValue();
        if( ( value != null ) && ( value instanceof BigDecimal ) )
        {
            BigDecimal amount = ( BigDecimal ) value;
            amount = amount.setScale( 2, RoundingMode.DOWN );
            DecimalFormat decimalFormat = new DecimalFormat( currencyFormat );
            result = decimalFormat.format( amount );
        }

        return result;
    }

}