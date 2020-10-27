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

import com.ekalife.elions.model.User;
import com.ekalife.elions.web.refund.RefundProductSpecInterface;

public class CheckSpajParamsVO implements Serializable
{
	private static final long serialVersionUID = 4409172520211834264L;
	String noSpaj;
    String noSurat;
    
    
	public String getNoSpaj() {
		return noSpaj;
	}
	public void setNoSpaj(String noSpaj) {
		this.noSpaj = noSpaj;
	}
	public String getNoSurat() {
		return noSurat;
	}
	public void setNoSurat(String noSurat) {
		this.noSurat = noSurat;
	}

  
}
