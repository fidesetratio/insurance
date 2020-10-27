package com.ekalife.elions.web.refund.vo;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: MstRefundParamsVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Nov 11, 2008 9:11:32 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import com.ekalife.utils.Common;
import com.ekalife.utils.parent.HistoryDb;

public class MstRefundParamsVO extends HistoryDb implements Serializable
{
	private static final long serialVersionUID = 5840259327468823517L;
	String spajNo;
    Integer tindakanCd;
    Integer alasanCd;
    String alasanLain;
    String alasanLainForLabel;
    String spajBaruNo;
    String kliNama;
    String kliNorek;
    String kliNamaBank;
    String kliCabangBank;
    String kliKotaBank;
    BigDecimal totalPremiDikembalikan;
    BigDecimal biayaAdmin;
    BigDecimal biayaMedis;
    BigDecimal biayaLain;
    BigDecimal biayaMerchant;
	String biayaLainDescr;
    
    Integer posisi;
    Integer ulinkFlag;
    Integer prevLspdId;

    BigDecimal payment;
    Date paymentDate;
    Integer hasUnitFlag;
    
    ArrayList<SetoranPremiDbVO> setoranPremiDbVOList;
    ArrayList<PenarikanUlinkVO> penarikanUlinkVOList;
    ArrayList<BiayaUlinkDbVO> biayaUlinkDbVOList;
    ArrayList<LampiranListVO> addLampiranList;

    public ArrayList<LampiranListVO> getAddLampiranList() {
		return addLampiranList;
	}

	public void setAddLampiranList(ArrayList<LampiranListVO> addLampiranList) {
		this.addLampiranList = Common.serializableList(addLampiranList);
	}

	public String getSpajNo()
    {
        return spajNo;
    }

    public void setSpajNo( String spajNo )
    {
        this.spajNo = spajNo;
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

    public String getAlasanLain()
    {
        return alasanLain;
    }

    public void setAlasanLain( String alasanLain )
    {
        this.alasanLain = alasanLain;
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

    public BigDecimal getTotalPremiDikembalikan()
    {
        return totalPremiDikembalikan;
    }

    public void setTotalPremiDikembalikan( BigDecimal totalPremiDikembalikan )
    {
        this.totalPremiDikembalikan = totalPremiDikembalikan;
    }

    public BigDecimal getBiayaAdmin()
    {
        return biayaAdmin;
    }

    public void setBiayaAdmin( BigDecimal biayaAdmin )
    {
        this.biayaAdmin = biayaAdmin;
    }

    public BigDecimal getBiayaMedis()
    {
        return biayaMedis;
    }

    public void setBiayaMedis( BigDecimal biayaMedis )
    {
        this.biayaMedis = biayaMedis;
    }

    public BigDecimal getBiayaLain()
    {
        return biayaLain;
    }

    public void setBiayaLain( BigDecimal biayaLain )
    {
        this.biayaLain = biayaLain;
    }

    public String getBiayaLainDescr()
    {
        return biayaLainDescr;
    }

    public void setBiayaLainDescr( String biayaLainDescr )
    {
        this.biayaLainDescr = biayaLainDescr;
    }

    public ArrayList<PenarikanUlinkVO> getPenarikanUlinkVOList()
    {
        return penarikanUlinkVOList;
    }

    public void setPenarikanUlinkVOList( ArrayList<PenarikanUlinkVO> penarikanUlinkVOList )
    {
        this.penarikanUlinkVOList = Common.serializableList(penarikanUlinkVOList);
    }

    public Integer getPosisi()
    {
        return posisi;
    }

    public void setPosisi( Integer posisi )
    {
        this.posisi = posisi;
    }

    public Integer getUlinkFlag()
    {
        return ulinkFlag;
    }

    public void setUlinkFlag( Integer ulinkFlag )
    {
        this.ulinkFlag = ulinkFlag;
    }

    public Date getPaymentDate()
    {
        return paymentDate;
    }

    public void setPaymentDate( Date paymentDate )
    {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getPayment()
    {
        return payment;
    }

    public void setPayment( BigDecimal payment )
    {
        this.payment = payment;
    }

    public Integer getHasUnitFlag() {
        return hasUnitFlag;
    }

    public void setHasUnitFlag(Integer hasUnitFlag) {
        this.hasUnitFlag = hasUnitFlag;
    }

	public ArrayList<BiayaUlinkDbVO> getBiayaUlinkDbVOList() {
		return biayaUlinkDbVOList;
	}

	public void setBiayaUlinkDbVOList(ArrayList<BiayaUlinkDbVO> biayaUlinkDbVOList) {
		this.biayaUlinkDbVOList = Common.serializableList(biayaUlinkDbVOList);
	}

	public ArrayList<SetoranPremiDbVO> getSetoranPremiDbVOList() {
		return setoranPremiDbVOList;
	}

	public void setSetoranPremiDbVOList(ArrayList<SetoranPremiDbVO> setoranPremiDbVOList) {
		this.setoranPremiDbVOList = Common.serializableList(setoranPremiDbVOList);
	}

	public String getAlasanLainForLabel() {
		return alasanLainForLabel;
	}

	public void setAlasanLainForLabel(String alasanLainForLabel) {
		this.alasanLainForLabel = alasanLainForLabel;
	}

	public Integer getPrevLspdId() {
		return prevLspdId;
	}

	public void setPrevLspdId(Integer prevLspdId) {
		this.prevLspdId = prevLspdId;
	}
	
    public BigDecimal getBiayaMerchant() {
		return biayaMerchant;
	}

	public void setBiayaMerchant(BigDecimal biayaMerchant) {
		this.biayaMerchant = biayaMerchant;
	}
    
}
