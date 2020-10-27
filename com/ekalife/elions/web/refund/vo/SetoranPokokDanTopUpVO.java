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

public class SetoranPokokDanTopUpVO implements Serializable
{
	private static final long serialVersionUID = -6980015090120542251L;
	String regSpaj;
    Integer msbiTahunKe;
    Integer msbiPremiKe;
    BigDecimal msdbPremium;
    String ltTransksi;
    Date muTglTrans;
    Integer ltId;
    String lkuId;
    
	public String getLkuId() {
		return lkuId;
	}
	public void setLkuId(String lkuId) {
		this.lkuId = lkuId;
	}
	public Integer getLtId() {
		return ltId;
	}
	public void setLtId(Integer ltId) {
		this.ltId = ltId;
	}

	public String getLtTransksi() {
		return ltTransksi;
	}
	public void setLtTransksi(String ltTransksi) {
		this.ltTransksi = ltTransksi;
	}
	public Integer getMsbiPremiKe() {
		return msbiPremiKe;
	}
	public void setMsbiPremiKe(Integer msbiPremiKe) {
		this.msbiPremiKe = msbiPremiKe;
	}
	public Integer getMsbiTahunKe() {
		return msbiTahunKe;
	}
	public void setMsbiTahunKe(Integer msbiTahunKe) {
		this.msbiTahunKe = msbiTahunKe;
	}
	public BigDecimal getMsdbPremium() {
		return msdbPremium;
	}
	public void setMsdbPremium(BigDecimal msdbPremium) {
		this.msdbPremium = msdbPremium;
	}
	public Date getMuTglTrans() {
		return muTglTrans;
	}
	public void setMuTglTrans(Date muTglTrans) {
		this.muTglTrans = muTglTrans;
	}
	public String getRegSpaj() {
		return regSpaj;
	}
	public void setRegSpaj(String regSpaj) {
		this.regSpaj = regSpaj;
	}
 
}
