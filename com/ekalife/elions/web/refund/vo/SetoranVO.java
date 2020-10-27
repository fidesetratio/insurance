package com.ekalife.elions.web.refund.vo;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: SetoranVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : fadly
 * Version              : 1.0
 * Creation Date    	: May 29, 2009 10:14:24 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SetoranVO implements Serializable{
	
	private static final long serialVersionUID = 5173429615370804369L;
	private String descrPokok;
    private String descrTopUp;
    private BigDecimal jumlahPokok;
    private BigDecimal jumlahTopUp;
    private Date tanggalSetoran;
    private String isDisabled;
    private String lkuId;
    private Integer titipanKe;
    private BigDecimal kurs;
    private String noPre;
    private String noVoucher;

	public SetoranVO( Date tanggalSetoran, String descrPokok, String descrTopUp, BigDecimal jumlahPokok,
    		String lkuId, Integer titipanKe, BigDecimal kurs, String noPre, String noVoucher )
    {
    	super();
    	this.tanggalSetoran = tanggalSetoran;
    	this.descrPokok = descrPokok;
    	this.descrTopUp = descrTopUp;
    	this.jumlahPokok = jumlahPokok;
    	this.lkuId = lkuId;
    	this.titipanKe = titipanKe;
    	this.kurs = kurs;
    	this.noPre = noPre;
    	this.noVoucher = noVoucher;
    }
    
    
	public String getNoPre() {
		return noPre;
	}


	public void setNoPre(String noPre) {
		this.noPre = noPre;
	}


	public String getNoVoucher() {
		return noVoucher;
	}


	public void setNoVoucher(String noVoucher) {
		this.noVoucher = noVoucher;
	}


	public String getLkuId() {
		return lkuId;
	}


	public void setLkuId(String lkuId) {
		this.lkuId = lkuId;
	}

	public String getIsDisabled() {
		return isDisabled;
	}
	public void setIsDisabled(String isDisabled) {
		this.isDisabled = isDisabled;
	}
	public String getDescrPokok() {
		return descrPokok;
	}
	public void setDescrPokok(String descrPokok) {
		this.descrPokok = descrPokok;
	}
	public String getDescrTopUp() {
		return descrTopUp;
	}
	public void setDescrTopUp(String descrTopUp) {
		this.descrTopUp = descrTopUp;
	}
	public BigDecimal getJumlahPokok() {
		return jumlahPokok;
	}
	public void setJumlahPokok(BigDecimal jumlahPokok) {
		this.jumlahPokok = jumlahPokok;
	}
	public BigDecimal getJumlahTopUp() {
		return jumlahTopUp;
	}
	public void setJumlahTopUp(BigDecimal jumlahTopUp) {
		this.jumlahTopUp = jumlahTopUp;
	}

	public Date getTanggalSetoran() {
		return tanggalSetoran;
	}
	public void setTanggalSetoran(Date tanggalSetoran) {
		this.tanggalSetoran = tanggalSetoran;
	}


	public BigDecimal getKurs() {
		return kurs;
	}


	public void setKurs(BigDecimal kurs) {
		this.kurs = kurs;
	}


	public Integer getTitipanKe() {
		return titipanKe;
	}


	public void setTitipanKe(Integer titipanKe) {
		this.titipanKe = titipanKe;
	}


}
