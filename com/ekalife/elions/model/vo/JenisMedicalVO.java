package com.ekalife.elions.model.vo;

import java.io.Serializable;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: JenisMedicalVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Mar 13, 2008 11:01:52 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

public class JenisMedicalVO implements Serializable {

	private static final long serialVersionUID = 1L;

    Integer cd;
    String descr;

    public Integer getCd()
    {
        return cd;
    }

    public void setCd( Integer cd )
    {
        this.cd = cd;
    }

    public String getDescr()
    {
        return descr;
    }

    public void setDescr( String descr )
    {
        this.descr = descr;
    }
}
