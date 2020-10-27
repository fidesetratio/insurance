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

public class LampiranListVO implements Serializable
{
	private static final long serialVersionUID = -8997577130503923295L;
	private String checkBox;
	private String lampiranLabel;
	private String lampiranNo;
	private String isDisabled;
	private String regSpaj;
	private Integer noUrut;
	
	public Integer getNoUrut() {
		return noUrut;
	}

	public void setNoUrut(Integer noUrut) {
		this.noUrut = noUrut;
	}

	public String getRegSpaj() {
		return regSpaj;
	}

	public void setRegSpaj(String regSpaj) {
		this.regSpaj = regSpaj;
	}

	public LampiranListVO()
	{
		
	}
	
	public LampiranListVO(String lampiranNo, String lampiranLabel, String isDisabled)
	{
		this.lampiranNo = lampiranNo;
		this.lampiranLabel = lampiranLabel;
//		this.checkBox = checkBox;
		this.isDisabled = isDisabled;
	}
	
	public String getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(String isDisabled) {
		this.isDisabled = isDisabled;
	}

	public String getLampiranNo() {
		return lampiranNo;
	}

	public void setLampiranNo(String lampiranNo) {
		this.lampiranNo = lampiranNo;
	}

	public String getLampiranLabel() {
		return lampiranLabel;
	}
	
	public void setLampiranLabel(String lampiranLabel) {
		this.lampiranLabel = lampiranLabel;
	}

	public String getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(String checkBox) {
		this.checkBox = checkBox;
	}

    

}
