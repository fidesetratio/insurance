package com.ekalife.elions.web.refund.vo;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: BatalParamsVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Nov 25, 2008 8:17:01 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.ekalife.elions.model.User;
import com.ekalife.elions.web.refund.RefundProductSpecInterface;

public class BatalParamsVO implements Serializable
{
	private static final long serialVersionUID = -6767871806612486557L;
	String spajNo;
    String alasan;
    String alasanForLabel;
    User currentUser;
    Integer posisiNo;
    Date cancelWhen;
    BigDecimal cancelWho;

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

	public String getSpajNo()
    {
        return spajNo;
    }

    public void setSpajNo( String spajNo )
    {
        this.spajNo = spajNo;
    }

    public String getAlasan()
    {
        return alasan;
    }

    public void setAlasan( String alasan )
    {
        this.alasan = alasan;
    }

    public User getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser( User currentUser )
    {
        this.currentUser = currentUser;
    }

    public Integer getPosisiNo()
    {
        return posisiNo;
    }

    public void setPosisiNo( Integer posisiNo )
    {
        this.posisiNo = posisiNo;
    }

	public String getAlasanForLabel() {
		return alasanForLabel;
	}

	public void setAlasanForLabel(String alasanForLabel) {
		this.alasanForLabel = alasanForLabel;
	}
}
