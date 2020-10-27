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
import java.util.Date;

public class MstBatalParamsVO implements Serializable
{
	private static final long serialVersionUID = 7283062815531993012L;
	String regSpaj;
    String msbNoBatal;
    Date msbTglBatal;
    String msbAlasan;
    Integer lusId;
    
    
	public Integer getLusId() {
		return lusId;
	}
	public void setLusId(Integer lusId) {
		this.lusId = lusId;
	}
	public String getMsbAlasan() {
		return msbAlasan;
	}
	public void setMsbAlasan(String msbAlasan) {
		this.msbAlasan = msbAlasan;
	}
	public String getMsbNoBatal() {
		return msbNoBatal;
	}
	public void setMsbNoBatal(String msbNoBatal) {
		this.msbNoBatal = msbNoBatal;
	}
	public Date getMsbTglBatal() {
		return msbTglBatal;
	}
	public void setMsbTglBatal(Date msbTglBatal) {
		this.msbTglBatal = msbTglBatal;
	}
	public String getRegSpaj() {
		return regSpaj;
	}
	public void setRegSpaj(String regSpaj) {
		this.regSpaj = regSpaj;
	}
    

  
}
