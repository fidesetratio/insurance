package com.ekalife.elions.web.refund.vo;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: AlasanVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: May 29, 2009 10:14:24 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.Serializable;
import java.util.ArrayList;

import com.ekalife.utils.Common;

public class AlasanVO implements Serializable
{
	private static final long serialVersionUID = -7343710159182220885L;
	private Integer alasanCd;
    private String alasanLabel;
    private ArrayList <DropDown> tindakanList;

    public Integer getAlasanCd() {
        return alasanCd;
    }

    public void setAlasanCd(Integer alasanCd) {
        this.alasanCd = alasanCd;
    }

    public String getAlasanLabel() {
        return alasanLabel;
    }

    public void setAlasanLabel(String alasanLabel) {
        this.alasanLabel = alasanLabel;
    }

	public ArrayList<DropDown> getTindakanList() {
		return tindakanList;
	}

	public void setTindakanList(ArrayList<DropDown> tindakanList) {
		this.tindakanList = Common.serializableList(tindakanList);
	}


}
