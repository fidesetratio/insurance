/**********************************************************************
 * Program History
 *
 * Project Name      	: Inventory and Costing System
 * Client Name       	: Toyota Motor Manufacturing Indonesia 
 * Program Id         	: ParameterizedQueryVO.java
 * Program Name   		: ParameterizedQueryVO
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



package com.ekalife.utils.easypoi.vo;

import java.util.List;

/**
 * User: Samuel
 * Date: Mar 28, 2007
 * Time: 4:50:12 PM
 */
public class ParameterizedQueryVO
{
    String query;
    List params;

    public String getQuery()
    {
        return query;
    }

    public void setQuery( String query )
    {
        this.query = query;
    }

    public List getParams()
    {
        return params;
    }

    public void setParams( List params )
    {
        this.params = params;
    }
}
