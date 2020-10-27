package com.ekalife.elions.web.refund.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: PolicyInfoVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 7, 2008 4:20:04 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

public class MstRefundInfoVO implements Serializable
{

	private static final long serialVersionUID = 3292090096291078721L;
	Integer alasanCd;
    String alasanLain;
    Date createWhen;
    Integer createWho;
    Integer hasUnitFlag;
    String lampiran;
    String noSurat;
    String noVoucher;
    BigDecimal payment;
    Date paymentDate;
    Integer posisiNo;
    BigDecimal premiRefund;
    Integer prevLspdId;
    String regSpaj;
    String regSpajBaru;
    Date tglKirimDokFisik;
    Integer tindakanCd;
    Integer uLinkFlag;
    Date updateWhen;
    Integer updateWho;

    String kliAtasNama;
    String kliNoRek;
    String kliNamaBank;
    String kliCabangBank;
    String kliKotaBank;

    public String getKliAtasNama()
    {
        return kliAtasNama;
    }

    public void setKliAtasNama( String kliAtasNama )
    {
        this.kliAtasNama = kliAtasNama;
    }

    public String getKliNoRek()
    {
        return kliNoRek;
    }

    public void setKliNoRek( String kliNoRek )
    {
        this.kliNoRek = kliNoRek;
    }

    public String getKliNamaBank()
    {
        return kliNamaBank;
    }

    public void setKliNamaBank( String kliNamaBank )
    {
        this.kliNamaBank = kliNamaBank;
    }

    public String getKliCabangBank()
    {
        return kliCabangBank;
    }

    public void setKliCabangBank( String kliCabangBank )
    {
        this.kliCabangBank = kliCabangBank;
    }

    public String getKliKotaBank()
    {
        return kliKotaBank;
    }

    public void setKliKotaBank( String kliKotaBank )
    {
        this.kliKotaBank = kliKotaBank;
    }

	public Integer getAlasanCd() {
		return alasanCd;
	}

	public void setAlasanCd(Integer alasanCd) {
		this.alasanCd = alasanCd;
	}

	public String getAlasanLain() {
		return alasanLain;
	}

	public void setAlasanLain(String alasanLain) {
		this.alasanLain = alasanLain;
	}

	public Date getCreateWhen() {
		return createWhen;
	}

	public void setCreateWhen(Date createWhen) {
		this.createWhen = createWhen;
	}

	public Integer getCreateWho() {
		return createWho;
	}

	public void setCreateWho(Integer createWho) {
		this.createWho = createWho;
	}

	public Integer getHasUnitFlag() {
		return hasUnitFlag;
	}

	public void setHasUnitFlag(Integer hasUnitFlag) {
		this.hasUnitFlag = hasUnitFlag;
	}

	public String getLampiran() {
		return lampiran;
	}

	public void setLampiran(String lampiran) {
		this.lampiran = lampiran;
	}

	public String getNoSurat() {
		return noSurat;
	}

	public void setNoSurat(String noSurat) {
		this.noSurat = noSurat;
	}

	public String getNoVoucher() {
		return noVoucher;
	}

	public void setNoVoucher(String noVoucher) {
		this.noVoucher = noVoucher;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Integer getPosisiNo() {
		return posisiNo;
	}

	public void setPosisiNo(Integer posisiNo) {
		this.posisiNo = posisiNo;
	}

	public BigDecimal getPremiRefund() {
		return premiRefund;
	}

	public void setPremiRefund(BigDecimal premiRefund) {
		this.premiRefund = premiRefund;
	}

	public Integer getPrevLspdId() {
		return prevLspdId;
	}

	public void setPrevLspdId(Integer prevLspdId) {
		this.prevLspdId = prevLspdId;
	}

	public String getRegSpaj() {
		return regSpaj;
	}

	public void setRegSpaj(String regSpaj) {
		this.regSpaj = regSpaj;
	}

	public String getRegSpajBaru() {
		return regSpajBaru;
	}

	public void setRegSpajBaru(String regSpajBaru) {
		this.regSpajBaru = regSpajBaru;
	}

	public Date getTglKirimDokFisik() {
		return tglKirimDokFisik;
	}

	public void setTglKirimDokFisik(Date tglKirimDokFisik) {
		this.tglKirimDokFisik = tglKirimDokFisik;
	}

	public Integer getTindakanCd() {
		return tindakanCd;
	}

	public void setTindakanCd(Integer tindakanCd) {
		this.tindakanCd = tindakanCd;
	}

	public Integer getULinkFlag() {
		return uLinkFlag;
	}

	public void setULinkFlag(Integer linkFlag) {
		uLinkFlag = linkFlag;
	}

	public Date getUpdateWhen() {
		return updateWhen;
	}

	public void setUpdateWhen(Date updateWhen) {
		this.updateWhen = updateWhen;
	}

	public Integer getUpdateWho() {
		return updateWho;
	}

	public void setUpdateWho(Integer updateWho) {
		this.updateWho = updateWho;
	}

}
