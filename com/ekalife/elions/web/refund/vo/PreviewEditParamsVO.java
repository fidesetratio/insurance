package com.ekalife.elions.web.refund.vo;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: Lamp1ParamsVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 8, 2008 4:13:51 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PreviewEditParamsVO implements Serializable
{
	private static final long serialVersionUID = -9028381235307622099L;
	private String descr;
	private String setoranOrNot;
	private String lkuId;
	private String ljbId;
	private String disabled;
	private String biayaStandarOrNot;
	private String noPre;
	private String noVoucher;
    
	private BigDecimal kurs;
	private BigDecimal jumlahPremi;
	private BigDecimal jumlahDebet;
    private BigDecimal jumlahKredit;
    
    private Integer titipanKe;
    
    private Date tglSetor;
    
    
    
    public PreviewEditParamsVO( String descr, BigDecimal jumlahPremi, 
    		BigDecimal jumlahDebet, BigDecimal jumlahKredit, String setoranOrNot, String lkuId,
    	    Integer titipanKe, Date tglSetor, String ljbId, String disabled, String biayaStandarOrNot, String noPre, String noVoucher, BigDecimal kurs)
    {
    	super();
        this.descr = descr;
        this.jumlahPremi = jumlahPremi;
        this.jumlahDebet = jumlahDebet;
        this.jumlahKredit = jumlahKredit;
        this.setoranOrNot = setoranOrNot;
        this.lkuId = lkuId;
        this.titipanKe = titipanKe;
        this.tglSetor = tglSetor;
        this.ljbId = ljbId;
        this.disabled = disabled;
        this.biayaStandarOrNot = biayaStandarOrNot;
        this.noPre = noPre;
        this.noVoucher = noVoucher;
        this.kurs = kurs;
    }

	public String getBiayaStandarOrNot() {
		return biayaStandarOrNot;
	}

	public void setBiayaStandarOrNot(String biayaStandarOrNot) {
		this.biayaStandarOrNot = biayaStandarOrNot;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public String getLjbId() {
		return ljbId;
	}

	public void setLjbId(String ljbId) {
		this.ljbId = ljbId;
	}

	public String getLkuId() {
		return lkuId;
	}

	public void setLkuId(String lkuId) {
		this.lkuId = lkuId;
	}

	public Date getTglSetor() {
		return tglSetor;
	}

	public void setTglSetor(Date tglSetor) {
		this.tglSetor = tglSetor;
	}

	public Integer getTitipanKe() {
		return titipanKe;
	}

	public void setTitipanKe(Integer titipanKe) {
		this.titipanKe = titipanKe;
	}

	public String getSetoranOrNot() {
		return setoranOrNot;
	}

	public void setSetoranOrNot(String setoranOrNot) {
		this.setoranOrNot = setoranOrNot;
	}

	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}

	public BigDecimal getJumlahPremi() {
		return jumlahPremi;
	}

	public void setJumlahPremi(BigDecimal jumlahPremi) {
		this.jumlahPremi = jumlahPremi;
	}

	public BigDecimal getJumlahDebet() {
		return jumlahDebet;
	}

	public void setJumlahDebet(BigDecimal jumlahDebet) {
		this.jumlahDebet = jumlahDebet;
	}

	public BigDecimal getJumlahKredit() {
		return jumlahKredit;
	}

	public void setJumlahKredit(BigDecimal jumlahKredit) {
		this.jumlahKredit = jumlahKredit;
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

	public BigDecimal getKurs() {
		return kurs;
	}

	public void setKurs(BigDecimal kurs) {
		this.kurs = kurs;
	}

   
}
