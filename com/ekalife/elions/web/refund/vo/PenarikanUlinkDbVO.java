package com.ekalife.elions.web.refund.vo;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: PenarikanUlinkDbVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 21, 2008 1:42:12 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.io.Serializable;
import java.math.BigDecimal;

public class PenarikanUlinkDbVO implements Serializable
{
	private static final long serialVersionUID = 1346016452920764476L;
	private String ljiId;
    private String ljiInvest;
    private BigDecimal jumlahUnit;

    public String getLjiId()
    {
        return ljiId;
    }

    public void setLjiId( String ljiId )
    {
        this.ljiId = ljiId;
    }

    public String getLjiInvest()
    {
        return ljiInvest;
    }

    public void setLjiInvest( String ljiInvest )
    {
        this.ljiInvest = ljiInvest;
    }

    public BigDecimal getJumlahUnit()
    {
        return jumlahUnit;
    }

    public void setJumlahUnit( BigDecimal jumlahUnit )
    {
        this.jumlahUnit = jumlahUnit;
    }
}
