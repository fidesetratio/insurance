package com.ekalife.elions.web.refund.vo;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: Lamp3VO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 23, 2008 2:48:58 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ekalife.utils.Common;

public class GbrA4VO implements Serializable
{
	private static final long serialVersionUID = 5606818058336207130L;
	String noUrutMemo;
    String hal;
    String tanggal;
    String spajNo;
    String policyNo;
    String productName;
    String policyHolderName;
    String insuredName;
    String newSpajNo;
    String newPolicyHolderName;
    String newInsuredName;
    String newProductName;
    String statement;
    String statementLamp1;
    String statementLamp2;
    String noVoucher;
    String ttd;
    String pembatal;
    String signer;
    String lca_id;

    ArrayList<HashMap<String, String>> rincianPolisList;
    ArrayList<PreviewEditParamsVO> tempDescrDanJumlah;
    ArrayList<HashMap<String, String>> addLampiranList;
    
    Date begDate;
    Date endDate;
    
    Integer prevLspdId;
    
    public String getLca_id() {
		return lca_id;
	}

	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}

    public String getPembatal() {
		return pembatal;
	}

	public void setPembatal(String pembatal) {
		this.pembatal = pembatal;
	}

	public String getTtd() {
		return ttd;
	}

	public void setTtd(String ttd) {
		this.ttd = ttd;
	}

	public Integer getPrevLspdId() {
		return prevLspdId;
	}

	public void setPrevLspdId(Integer prevLspdId) {
		this.prevLspdId = prevLspdId;
	}

	public ArrayList<PreviewEditParamsVO> getTempDescrDanJumlah() {
		return tempDescrDanJumlah;
	}

	public void setTempDescrDanJumlah(ArrayList<PreviewEditParamsVO> tempDescrDanJumlah) {
		this.tempDescrDanJumlah = Common.serializableList(tempDescrDanJumlah);
	}

	public String getNoUrutMemo()
    {
        return noUrutMemo;
    }

    public void setNoUrutMemo( String noUrutMemo )
    {
        this.noUrutMemo = noUrutMemo;
    }

    public String getHal()
    {
        return hal;
    }

    public void setHal( String hal )
    {
        this.hal = hal;
    }

    public String getTanggal()
    {
        return tanggal;
    }

    public void setTanggal( String tanggal )
    {
        this.tanggal = tanggal;
    }

    public String getSpajNo()
    {
        return spajNo;
    }

    public void setSpajNo( String spajNo )
    {
        this.spajNo = spajNo;
    }

    public String getPolicyNo()
    {
        return policyNo;
    }

    public void setPolicyNo( String policyNo )
    {
        this.policyNo = policyNo;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName( String productName )
    {
        this.productName = productName;
    }

    public String getInsuredName()
    {
        return insuredName;
    }

    public void setInsuredName( String insuredName )
    {
        this.insuredName = insuredName;
    }

    public String getPolicyHolderName()
    {
        return policyHolderName;
    }

    public void setPolicyHolderName( String policyHolderName )
    {
        this.policyHolderName = policyHolderName;
    }

    public String getNewSpajNo()
    {
        return newSpajNo;
    }

    public void setNewSpajNo( String newSpajNo )
    {
        this.newSpajNo = newSpajNo;
    }

    public String getNewPolicyHolderName()
    {
        return newPolicyHolderName;
    }

    public void setNewPolicyHolderName( String newPolicyHolderName )
    {
        this.newPolicyHolderName = newPolicyHolderName;
    }

    public String getNewInsuredName()
    {
        return newInsuredName;
    }

    public void setNewInsuredName( String newInsuredName )
    {
        this.newInsuredName = newInsuredName;
    }

    public String getStatement()
    {
        return statement;
    }

    public void setStatement( String statement )
    {
        this.statement = statement;
    }

    public String getStatementLamp1()
    {
        return statementLamp1;
    }

    public void setStatementLamp1( String statementLamp1 )
    {
        this.statementLamp1 = statementLamp1;
    }

    public String getStatementLamp2()
    {
        return statementLamp2;
    }

    public void setStatementLamp2( String statementLamp2 )
    {
        this.statementLamp2 = statementLamp2;
    }

    public String getSigner()
    {
        return signer;
    }

    public void setSigner( String signer )
    {
        this.signer = signer;
    }

    public ArrayList<HashMap<String, String>> getRincianPolisList()
    {
        return rincianPolisList;
    }

    public void setRincianPolisList( ArrayList<HashMap<String, String>> rincianPolisList )
    {
        this.rincianPolisList = Common.serializableList(rincianPolisList);
    }

	public Date getBegDate() {
		return begDate;
	}

	public void setBegDate(Date begDate) {
		this.begDate = begDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getNewProductName() {
		return newProductName;
	}

	public void setNewProductName(String newProductName) {
		this.newProductName = newProductName;
	}

	public String getNoVoucher() {
		return noVoucher;
	}

	public void setNoVoucher(String noVoucher) {
		this.noVoucher = noVoucher;
	}

	public ArrayList<HashMap<String, String>> getAddLampiranList() {
		return addLampiranList;
	}

	public void setAddLampiranList(ArrayList<HashMap<String, String>> addLampiranList) {
		this.addLampiranList = Common.serializableList(addLampiranList);
	}
}
