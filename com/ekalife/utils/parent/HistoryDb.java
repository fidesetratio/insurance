package com.ekalife.utils.parent;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: HistoryDb
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Nov 11, 2008 8:24:47 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.math.BigDecimal;
import java.util.Date;

public class HistoryDb
{
    Date createWhen;
    BigDecimal createWho;
    Date updateWhen;
    BigDecimal updateWho;
    Date cancelWhen;
    BigDecimal cancelWho;

    public BigDecimal getCreateWho()
    {
        return createWho;
    }

    public void setCreateWho( BigDecimal createWho )
    {
        this.createWho = createWho;
    }

    public BigDecimal getUpdateWho()
    {
        return updateWho;
    }

    public void setUpdateWho( BigDecimal updateWho )
    {
        this.updateWho = updateWho;
    }

    public Date getCreateWhen()
    {
        return createWhen;
    }

    public void setCreateWhen( Date createWhen )
    {
        this.createWhen = createWhen;
    }

    public Date getUpdateWhen()
    {
        return updateWhen;
    }

    public void setUpdateWhen( Date updateWhen )
    {
        this.updateWhen = updateWhen;
    }

	public Date getCancelWhen() {
		return cancelWhen;
	}

	public void setCancelWhen(Date cancelWhen) {
		this.cancelWhen = cancelWhen;
	}

	public BigDecimal getCancelWho() {
		return cancelWho;
	}

	public void setCancelWho(BigDecimal cancelWho) {
		this.cancelWho = cancelWho;
	}
}
