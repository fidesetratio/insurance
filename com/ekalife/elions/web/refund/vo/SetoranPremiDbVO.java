package com.ekalife.elions.web.refund.vo;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: SetoranPremiDbVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 9, 2008 4:47:38 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

public class SetoranPremiDbVO implements Serializable
{
	private static final long serialVersionUID = 3567566895985452587L;
//    msdp_number, mspa_date_book, mspa_payment, lku_id from eka.mst_payment

    private Integer titipanKe;
    private Date tglSetor;
    private BigDecimal jumlah;
    private String lkuId;
    private BigDecimal kurs;
    private String descr;
    private String noPre;
    private String noVoucher;

    public SetoranPremiDbVO()
    {
    	
    }
    
    public SetoranPremiDbVO(Integer titipanKe, Date tglSetor, BigDecimal jumlah, 
    		String lkuId, BigDecimal kurs, String descr, String noPre,  String noVoucher)
    {
    	super();
    	this.titipanKe = titipanKe;
    	this.tglSetor = tglSetor;
    	this.jumlah = jumlah;
    	this.lkuId = lkuId;
    	this.kurs = kurs;
    	this.descr = descr;
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

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Integer getTitipanKe()
    {
        return titipanKe;
    }

    public void setTitipanKe( Integer titipanKe )
    {
        this.titipanKe = titipanKe;
    }

    public Date getTglSetor()
    {
        return tglSetor;
    }

    public void setTglSetor( Date tglSetor )
    {
        this.tglSetor = tglSetor;
    }

    public BigDecimal getJumlah()
    {
        return jumlah;
    }

    public void setJumlah( BigDecimal jumlah )
    {
        this.jumlah = jumlah;
    }

    public String getLkuId()
    {
        return lkuId;
    }

    public void setLkuId( String lkuId )
    {
        this.lkuId = lkuId;
    }

    public BigDecimal getKurs()
    {
        return kurs;
    }

    public void setKurs( BigDecimal kurs )
    {
        this.kurs = kurs;
    }
}
