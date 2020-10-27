package com.ekalife.elions.web.refund.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ekalife.utils.Common;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RedemptParamsVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Nov 21, 2008 11:25:32 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

public class RedemptParamsVO implements Serializable
{
	private static final long serialVersionUID = -396211055720881133L;
	String spajNo;
    String alasan;
    String alasanForLabel;
    String namaUnderwriter;
    ArrayList<PenarikanUlinkVO> penarikanUlinkVOList;
    Integer prevLspdId;

    public Integer getPrevLspdId() {
		return prevLspdId;
	}

	public void setPrevLspdId(Integer prevLspdId) {
		this.prevLspdId = prevLspdId;
	}

	public String getAlasan()
    {
        return alasan;
    }

    public void setAlasan( String alasan )
    {
        this.alasan = alasan;
    }

    public ArrayList<PenarikanUlinkVO> getPenarikanUlinkVOList()
    {
        return penarikanUlinkVOList;
    }

    public void setPenarikanUlinkVOList( ArrayList<PenarikanUlinkVO> penarikanUlinkVOList )
    {
        this.penarikanUlinkVOList = Common.serializableList(penarikanUlinkVOList);
    }

    public String getSpajNo()
    {
        return spajNo;
    }

    public void setSpajNo( String spajNo )
    {
        this.spajNo = spajNo;
    }

	public String getAlasanForLabel() {
		return alasanForLabel;
	}

	public void setAlasanForLabel(String alasanForLabel) {
		this.alasanForLabel = alasanForLabel;
	}

	public String getNamaUnderwriter() {
		return namaUnderwriter;
	}

	public void setNamaUnderwriter(String namaUnderwriter) {
		this.namaUnderwriter = namaUnderwriter;
	}
}
