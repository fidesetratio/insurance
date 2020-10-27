package com.ekalife.elions.web.refund.vo;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: GbrA1ParamsVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : Fadly
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

import com.ekalife.utils.Common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GbrA2ParamsVO implements Serializable
{
	private static final long serialVersionUID = -8831661551233742881L;
	String spajNo;
    String alasan;
    String alasanForLabel;
    Integer posisi;

    String atasNama;
    String norek;
    String namaBank;
    String cabangBank;
    String kotaBank;

    BigDecimal biayaAdmin;
    BigDecimal biayaMedis;
    BigDecimal biayaLain;
    BigDecimal biayaMerchant;

    String biayaLainDescr;
    ArrayList < PenarikanUlinkVO > penarikanUlinkList;
    ArrayList < PreviewEditParamsVO > tempDesrcDanJumlah;
    ArrayList < SetoranPremiDbVO > setoranPokokAndTopUpList;

    public ArrayList<PreviewEditParamsVO> getTempDesrcDanJumlah() {
		return tempDesrcDanJumlah;
	}

	public void setTempDesrcDanJumlah(ArrayList<PreviewEditParamsVO> tempDesrcDanJumlah) {
		this.tempDesrcDanJumlah = Common.serializableList(tempDesrcDanJumlah);
	}

	public String getSpajNo()
    {
        return spajNo;
    }

    public void setSpajNo( String spajNo )
    {
        this.spajNo = spajNo;
    }

    public String getAlasan()
    {
        return alasan;
    }

    public void setAlasan( String alasan )
    {
        this.alasan = alasan;
    }

    public String getAtasNama()
    {
        return atasNama;
    }

    public void setAtasNama( String atasNama )
    {
        this.atasNama = atasNama;
    }

    public String getNorek()
    {
        return norek;
    }

    public void setNorek( String norek )
    {
        this.norek = norek;
    }

    public String getNamaBank()
    {
        return namaBank;
    }

    public void setNamaBank( String namaBank )
    {
        this.namaBank = namaBank;
    }

    public String getCabangBank()
    {
        return cabangBank;
    }

    public void setCabangBank( String cabangBank )
    {
        this.cabangBank = cabangBank;
    }

    public String getKotaBank()
    {
        return kotaBank;
    }

    public void setKotaBank( String kotaBank )
    {
        this.kotaBank = kotaBank;
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

    public ArrayList<PenarikanUlinkVO> getPenarikanUlinkList()
    {
        return penarikanUlinkList;
    }

    public void setPenarikanUlinkList( ArrayList<PenarikanUlinkVO> penarikanUlinkList )
    {
        this.penarikanUlinkList = Common.serializableList(penarikanUlinkList);
    }

	public String getAlasanForLabel() {
		return alasanForLabel;
	}

	public void setAlasanForLabel(String alasanForLabel) {
		this.alasanForLabel = alasanForLabel;
	}

	public Integer getPosisi() {
		return posisi;
	}

	public void setPosisi(Integer posisi) {
		this.posisi = posisi;
	}

	public ArrayList<SetoranPremiDbVO> getSetoranPokokAndTopUpList() {
		return setoranPokokAndTopUpList;
	}

	public void setSetoranPokokAndTopUpList(ArrayList<SetoranPremiDbVO> setoranPokokAndTopUpList) {
		this.setoranPokokAndTopUpList = Common.serializableList(setoranPokokAndTopUpList);
	}
	
    public BigDecimal getBiayaMerchant() {
		return biayaMerchant;
	}

	public void setBiayaMerchant(BigDecimal biayaMerchant) {
		this.biayaMerchant = biayaMerchant;
	}
}
