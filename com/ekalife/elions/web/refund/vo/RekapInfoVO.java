package com.ekalife.elions.web.refund.vo;

import java.io.Serializable;
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

public class RekapInfoVO implements Serializable
{
	private static final long serialVersionUID = -8244510515746733616L;
	Integer tindakanCd;
    String spajNo;
    String policyNo;
    String namaProduk;
    String namaPp;
    Integer lsgbId;   // dipakai utk mengetahui, bila = 17 maka itu adalah produk unit link
    String posisi;
    String fu;
    Integer lsbsId;
    Integer lsdbsNumber;
    String lsbsName;
    String alasan;
    Date tglBatal;
    Integer alasanCd;
    String userUw;
    String voucher;
    String noPre;
    Date tglKirimDokFisik;
    
    public RekapInfoVO ()
    {
    	
    }
    public RekapInfoVO ( String spajNo, String policyNo, String namaProduk, 
    		String namaPp, String posisi, String alasan, String userUw, String voucher, String noPre )
    {
    	this.spajNo = spajNo;
    	this.policyNo = policyNo;
    	this.namaProduk = namaProduk;
    	this.namaPp = namaPp;
    	this.alasan = alasan;
    	this.userUw = userUw;
    	this.voucher = voucher;
    	this.noPre = noPre;
    }
    
    public String getNoPre() {
		return noPre;
	}
	public void setNoPre(String noPre) {
		this.noPre = noPre;
	}
	public Date getTglKirimDokFisik() {
		return tglKirimDokFisik;
	}
	public void setTglKirimDokFisik(Date tglKirimDokFisik) {
		this.tglKirimDokFisik = tglKirimDokFisik;
	}
	public String getVoucher() {
		return voucher;
	}
	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}
	public String getAlasan() {
		return alasan;
	}

	public void setAlasan(String alasan) {
		this.alasan = alasan;
	}

	public Integer getAlasanCd() {
		return alasanCd;
	}

	public void setAlasanCd(Integer alasanCd) {
		this.alasanCd = alasanCd;
	}

	public Integer getLsbsId() {
		return lsbsId;
	}

	public void setLsbsId(Integer lsbsId) {
		this.lsbsId = lsbsId;
	}

	public String getLsbsName() {
		return lsbsName;
	}

	public void setLsbsName(String lsbsName) {
		this.lsbsName = lsbsName;
	}

	public Integer getLsdbsNumber() {
		return lsdbsNumber;
	}

	public void setLsdbsNumber(Integer lsdbsNumber) {
		this.lsdbsNumber = lsdbsNumber;
	}

	public Date getTglBatal() {
		return tglBatal;
	}

	public void setTglBatal(Date tglBatal) {
		this.tglBatal = tglBatal;
	}

	public String getUserUw() {
		return userUw;
	}

	public void setUserUw(String userUw) {
		this.userUw = userUw;
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

    public String getNamaProduk()
    {
        return namaProduk;
    }

    public void setNamaProduk( String namaProduk )
    {
        this.namaProduk = namaProduk;
    }

    public String getNamaPp()
    {
        return namaPp;
    }

    public void setNamaPp( String namaPp )
    {
        this.namaPp = namaPp;
    }

    public Integer getLsgbId()
    {
        return lsgbId;
    }

    public void setLsgbId( Integer lsgbId )
    {
        this.lsgbId = lsgbId;
    }

    public String getPosisi()
    {
        return posisi;
    }

    public void setPosisi( String posisi )
    {
        this.posisi = posisi;
    }

    public String getFu()
    {
        return fu;
    }

    public void setFu( String fu )
    {
        this.fu = fu;
    }
	public Integer getTindakanCd() {
		return tindakanCd;
	}
	public void setTindakanCd(Integer tindakanCd) {
		this.tindakanCd = tindakanCd;
	}

}
