package com.ekalife.elions.web.refund.vo;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: PenarikanUlinkVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 22, 2008 9:55:19 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.io.Serializable;
import java.math.BigDecimal;

public class PenarikanUlinkVO implements Serializable
{
	private static final long serialVersionUID = -8148228092274806933L;
	private String ljiInvest;
    private String ljiId;
    private BigDecimal jumlahUnit;
    private BigDecimal jumlah;
    private String deskripsiPenarikan;
    
    public String getLjiInvest()
    {
        return ljiInvest;
    }

    public void setLjiInvest( String ljiInvest )
    {
        this.ljiInvest = ljiInvest;
    }

    public String getLjiId()
    {
        return ljiId;
    }

    public void setLjiId( String ljiId )
    {
        this.ljiId = ljiId;
    }

    public BigDecimal getJumlahUnit()
    {
        return jumlahUnit;
    }

    public void setJumlahUnit( BigDecimal jumlahUnit )
    {
        this.jumlahUnit = jumlahUnit;
    }

    public BigDecimal getJumlah()
    {
        return jumlah;
    }

    public void setJumlah( BigDecimal jumlah )
    {
        this.jumlah = jumlah;
    }

    public String getDeskripsiPenarikan()
    {
        return deskripsiPenarikan;
    }

    public void setDeskripsiPenarikan( String deskripsiPenarikan )
    {
        this.deskripsiPenarikan = deskripsiPenarikan;
    }
}
