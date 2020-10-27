package com.ekalife.elions.web.refund.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.ekalife.utils.Common;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: Lamp3ParamsVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 23, 2008 11:42:03 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

public class GbrA3ParamsVO implements Serializable
{
	private static final long serialVersionUID = -2124629272312159190L;
	String spajNo;
    String spajBaruNo;
    String alasan;
    String alasanForLabel;
    String biayaLainDescr;

    BigDecimal biayaAdmin;
    BigDecimal biayaMedis;
    BigDecimal biayaLain;
    
    ArrayList<PenarikanUlinkVO> penarikanUlinkList;
    
    public ArrayList<PenarikanUlinkVO> getPenarikanUlinkList() {
		return penarikanUlinkList;
	}

	public void setPenarikanUlinkList(ArrayList<PenarikanUlinkVO> penarikanUlinkList) {
		this.penarikanUlinkList =  Common.serializableList(penarikanUlinkList);
	}

	public BigDecimal getBiayaAdmin() {
		return biayaAdmin;
	}

	public void setBiayaAdmin(BigDecimal biayaAdmin) {
		this.biayaAdmin = biayaAdmin;
	}

	public String getAlasanForLabel() {
		return alasanForLabel;
	}

	public void setAlasanForLabel(String alasanForLabel) {
		this.alasanForLabel = alasanForLabel;
	}

	public String getSpajNo()
    {
        return spajNo;
    }

    public void setSpajNo( String spajNo )
    {
        this.spajNo = spajNo;
    }

    public String getSpajBaruNo()
    {
        return spajBaruNo;
    }

    public void setSpajBaruNo( String spajBaruNo )
    {
        this.spajBaruNo = spajBaruNo;
    }

    public String getAlasan()
    {
        return alasan;
    }

    public void setAlasan( String alasan )
    {
        this.alasan = alasan;
    }

	public BigDecimal getBiayaLain() {
		return biayaLain;
	}

	public void setBiayaLain(BigDecimal biayaLain) {
		this.biayaLain = biayaLain;
	}

	public BigDecimal getBiayaMedis() {
		return biayaMedis;
	}

	public void setBiayaMedis(BigDecimal biayaMedis) {
		this.biayaMedis = biayaMedis;
	}

	public String getBiayaLainDescr() {
		return biayaLainDescr;
	}

	public void setBiayaLainDescr(String biayaLainDescr) {
		this.biayaLainDescr = biayaLainDescr;
	}
}
