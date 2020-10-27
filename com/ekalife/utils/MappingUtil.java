package com.ekalife.utils;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: MappingUtil
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 9, 2008 10:39:03 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.List;

public class MappingUtil
{
    protected final Log logger = LogFactory.getLog( getClass() );

    public MappingUtil()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* MappingUtil constructor is called ..." );
    }


    /**
     *
     * @param dropDownList
     * @param key
     * @return label
     */
    public static String getLabel( List<DropDown> dropDownList, Object key )
    {
        String result = "";
        if( dropDownList != null && key != null )
        {
            int size = dropDownList.size();
            DropDown dropDown;
            boolean valueHasBeenFound = false;
            int idx = 0;
            while( !valueHasBeenFound && idx < size )
            {
                dropDown = dropDownList.get( idx );
                if( dropDown != null && dropDown.getKey().equals( key.toString() ) )
                {
                    valueHasBeenFound = true;
                    result = dropDown.getValue();
                }
                idx++;
            }
        }
        return result;
    }
}
