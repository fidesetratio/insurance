package com.ekalife.elions.web.refund.vo;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: BiayaUlinkDbVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 21, 2008 2:05:54 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.io.Serializable;
import java.math.BigDecimal;

public class BiayaUlinkDbVO implements Serializable
{
	private static final long serialVersionUID = 5783917119232712964L;
	private String descr;
    private BigDecimal amount;
    private Integer ljbId;
    private String lkuId;
    
    public BiayaUlinkDbVO()
    {
    	
    }
    
    public BiayaUlinkDbVO( String descr, BigDecimal amount, Integer ljbId, String lkuId)
    {
    	this.descr = descr;
    	this.amount = amount;
    	this.ljbId =ljbId;
    	this.lkuId = lkuId;
    }
    
    public String getLkuId() {
		return lkuId;
	}

	public void setLkuId(String lkuId) {
		this.lkuId = lkuId;
	}

	public String getDescr()
    {
        return descr;
    }

    public void setDescr( String descr )
    {
        this.descr = descr;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount( BigDecimal amount )
    {
        this.amount = amount;
    }

    public Integer getLjbId()
    {
        return ljbId;
    }

    public void setLjbId( Integer ljbId )
    {
        this.ljbId = ljbId;
    }
}
