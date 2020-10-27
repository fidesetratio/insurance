package com.ekalife.elions.web.refund.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ekalife.utils.Common;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RedemptVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Nov 21, 2008 11:31:39 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

public class RedemptVO implements Serializable{
	private static final long serialVersionUID = -1886049394642709969L;
	String alasan;
    String note;
    String namaUnderwriter;
    ArrayList<HashMap<String, Object>> detailList;
    Integer prevLspdId;
    String bankName;
    String bankAccount;
    String invest;

    public String getInvest() {
		return invest;
	}

	public void setInvest(String invest) {
		this.invest = invest;
	}

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

    public String getNamaUnderwriter() {
		return namaUnderwriter;
	}

	public void setNamaUnderwriter(String namaUnderwriter) {
		this.namaUnderwriter = namaUnderwriter;
	}

	public String getNote()
    {
        return note;
    }

    public void setNote( String note )
    {
        this.note = note;
    }

    public ArrayList<HashMap<String, Object>> getDetailList()
    {
        return detailList;
    }

    public void setDetailList( ArrayList<HashMap<String, Object>> detailList )
    {
        this.detailList = Common.serializableList(detailList);
    }

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

}
