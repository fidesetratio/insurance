package com.ekalife.elions.web.refund.vo;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundDbVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 7, 2008 11:51:00 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import com.ekalife.utils.Common;
import com.ekalife.utils.parent.HistoryDb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class RefundDbVO extends HistoryDb implements Serializable
{
	private static final long serialVersionUID = -4684125835292065731L;
	String spajNo;
    String noSurat;
    String lampiran;
    Integer posisiNo;
    Integer tindakanCd;
    Integer alasanCd;
    Integer ulinkFlag;
    String noVoucher;
    BigDecimal premiRefund;
    String spajBaruNo;
    String kliNama;
    String kliNorek;
    String kliNamaBank;
    String kliCabangBank;
    String kliKotaBank;
    String alasanLain;
    BigDecimal payment;
    Date paymentDate;
    Integer hasUnitFlag;
    String updateFullName;
    Integer prevLspdId;
    Integer flagUserCabang;
    ArrayList<LampiranListVO> addLampiranList;
    
    ArrayList<RefundDetDbVO> detailList;

    public String getSpajNo()
    {
        return spajNo;
    }

    public void setSpajNo( String spajNo )
    {
        this.spajNo = spajNo;
    }

    public String getNoSurat()
    {
        return noSurat;
    }

    public void setNoSurat( String noSurat )
    {
        this.noSurat = noSurat;
    }

    public Integer getPosisiNo()
    {
        return posisiNo;
    }

    public void setPosisiNo( Integer posisiNo )
    {
        this.posisiNo = posisiNo;
    }

    public Integer getTindakanCd()
    {
        return tindakanCd;
    }

    public void setTindakanCd( Integer tindakanCd )
    {
        this.tindakanCd = tindakanCd;
    }

    public Integer getAlasanCd()
    {
        return alasanCd;
    }

    public void setAlasanCd( Integer alasanCd )
    {
        this.alasanCd = alasanCd;
    }

    public Integer getUlinkFlag()
    {
        return ulinkFlag;
    }

    public void setUlinkFlag( Integer ulinkFlag )
    {
        this.ulinkFlag = ulinkFlag;
    }

    public String getNoVoucher()
    {
        return noVoucher;
    }

    public void setNoVoucher( String noVoucher )
    {
        this.noVoucher = noVoucher;
    }

    public BigDecimal getPremiRefund()
    {
        return premiRefund;
    }

    public void setPremiRefund( BigDecimal premiRefund )
    {
        this.premiRefund = premiRefund;
    }

    public String getSpajBaruNo()
    {
        return spajBaruNo;
    }

    public void setSpajBaruNo( String spajBaruNo )
    {
        this.spajBaruNo = spajBaruNo;
    }

    public String getKliNama()
    {
        return kliNama;
    }

    public void setKliNama( String kliNama )
    {
        this.kliNama = kliNama;
    }

    public String getKliNorek()
    {
        return kliNorek;
    }

    public void setKliNorek( String kliNorek )
    {
        this.kliNorek = kliNorek;
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

    public ArrayList<RefundDetDbVO> getDetailList()
    {
        return detailList;
    }

    public void setDetailList( ArrayList<RefundDetDbVO> detailList )
    {
        this.detailList = Common.serializableList(detailList);
    }

    public String getAlasanLain()
    {
        return alasanLain;
    }

    public void setAlasanLain( String alasanLain )
    {
        this.alasanLain = alasanLain;
    }

    public BigDecimal getPayment()
    {
        return payment;
    }

    public void setPayment( BigDecimal payment )
    {
        this.payment = payment;
    }

    public Date getPaymentDate()
    {
        return paymentDate;
    }

    public void setPaymentDate( Date paymentDate )
    {
        this.paymentDate = paymentDate;
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

	public String getUpdateFullName() {
		return updateFullName;
	}

	public void setUpdateFullName(String updateFullName) {
		this.updateFullName = updateFullName;
	}

	public Integer getPrevLspdId() {
		return prevLspdId;
	}

	public void setPrevLspdId(Integer prevLspdId) {
		this.prevLspdId = prevLspdId;
	}

	public Integer getFlagUserCabang() {
		return flagUserCabang;
	}

	public void setFlagUserCabang(Integer flagUserCabang) {
		this.flagUserCabang = flagUserCabang;
	}

	public ArrayList<LampiranListVO> getAddLampiranList() {
		return addLampiranList;
	}

	public void setAddLampiranList(ArrayList<LampiranListVO> addLampiranList) {
		this.addLampiranList = Common.serializableList(addLampiranList);
	}
}
