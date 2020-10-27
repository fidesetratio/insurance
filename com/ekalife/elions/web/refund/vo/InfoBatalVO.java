package com.ekalife.elions.web.refund.vo;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: InfoBatalVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Nov 11, 2008 10:51:13 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.io.Serializable;

public class InfoBatalVO implements Serializable
{
	private static final long serialVersionUID = 7564339345166746145L;
	String regSpaj;

    public String getRegSpaj()
    {
        return regSpaj;
    }

    public void setRegSpaj( String regSpaj )
    {
        this.regSpaj = regSpaj;
    }
}
