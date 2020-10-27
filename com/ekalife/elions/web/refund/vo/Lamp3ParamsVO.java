package com.ekalife.elions.web.refund.vo;

import java.io.Serializable;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: Lamp3ParamsVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 23, 2008 11:42:03 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

public class Lamp3ParamsVO implements Serializable
{
	private static final long serialVersionUID = -5851125076689753611L;
	String spajNo;
    String spajBaruNo;
    String alasan;
    String alasanForLabel;

    public String getAlasanForLabel() {
		return alasanForLabel;
	}

	public void setAlasanForLabel(String alasanForLabel) {
		this.alasanForLabel = alasanForLabel;
	}

	public String getSpajNo()
    {
        return spajNo;
    }

    public void setSpajNo( String spajNo )
    {
        this.spajNo = spajNo;
    }

    public String getSpajBaruNo()
    {
        return spajBaruNo;
    }

    public void setSpajBaruNo( String spajBaruNo )
    {
        this.spajBaruNo = spajBaruNo;
    }

    public String getAlasan()
    {
        return alasan;
    }

    public void setAlasan( String alasan )
    {
        this.alasan = alasan;
    }
}
