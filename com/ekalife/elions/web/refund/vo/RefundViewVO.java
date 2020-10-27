package com.ekalife.elions.web.refund.vo;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundViewVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 30, 2008 2:13:17 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RefundViewVO implements Serializable
{
	private static final long serialVersionUID = 4427354893752423233L;
	private String spaj;
    private String lastUpdate;
    private String deskripsi;
    private String label;
    private String tindakan;
    private Integer posisiCd;
    private Integer tindakanCd;
    private String suratBatalExist;
    private String suratRedemptExist;
    private String updateWho;
    private String newSpaj;
    private String noPolis;
    private String spajLabel;
    private String polisLabel;
	private Date tglKirimDokFisik;
    private String tglKirimDokFisikLabel;
    private BigDecimal cancelWho;
    private String cancelFullName;
    private String alasan;
    private Integer hasUnitFlag;
    private Integer ulinkFlag;
    private String aksesHapusDraft;

	public Integer getUlinkFlag() {
		return ulinkFlag;
	}

	public void setUlinkFlag(Integer ulinkFlag) {
		this.ulinkFlag = ulinkFlag;
	}

	public Integer getHasUnitFlag() {
		return hasUnitFlag;
	}

	public void setHasUnitFlag(Integer hasUnitFlag) {
		this.hasUnitFlag = hasUnitFlag;
	}

	public String getAlasan() {
		return alasan;
	}

	public void setAlasan(String alasan) {
		this.alasan = alasan;
	}

	public String getCancelFullName() {
		return cancelFullName;
	}

	public void setCancelFullName(String cancelFullName) {
		this.cancelFullName = cancelFullName;
	}

	public BigDecimal getCancelWho() {
		return cancelWho;
	}

	public void setCancelWho(BigDecimal cancelWho) {
		this.cancelWho = cancelWho;
	}

	public String getTglKirimDokFisikLabel() {
		return tglKirimDokFisikLabel;
	}

	public void setTglKirimDokFisikLabel(String tglKirimDokFisikLabel) {
		this.tglKirimDokFisikLabel = tglKirimDokFisikLabel;
	}

	public String getPolisLabel() {
		return polisLabel;
	}

	public void setPolisLabel(String polisLabel) {
		this.polisLabel = polisLabel;
	}

	public String getSpajLabel() {
		return spajLabel;
	}

	public void setSpajLabel(String spajLabel) {
		this.spajLabel = spajLabel;
	}

	public String getNoPolis() {
		return noPolis;
	}

	public void setNoPolis(String noPolis) {
		this.noPolis = noPolis;
	}

	public String getSpaj()
    {
        return spaj;
    }

    public void setSpaj( String spaj )
    {
        this.spaj = spaj;
    }

    public String getLastUpdate()
    {
        return lastUpdate;
    }

    public void setLastUpdate( String lastUpdate )
    {
        this.lastUpdate = lastUpdate;
    }

    public String getDeskripsi()
    {
        return deskripsi;
    }

    public void setDeskripsi( String deskripsi )
    {
        this.deskripsi = deskripsi;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel( String label )
    {
        this.label = label;
    }

    public String getTindakan()
    {
        return tindakan;
    }

    public void setTindakan( String tindakan )
    {
        this.tindakan = tindakan;
    }

    public Integer getPosisiCd()
    {
        return posisiCd;
    }

    public void setPosisiCd( Integer posisiCd )
    {
        this.posisiCd = posisiCd;
    }

    public Integer getTindakanCd()
    {
        return tindakanCd;
    }

    public void setTindakanCd( Integer tindakanCd )
    {
        this.tindakanCd = tindakanCd;
    }

    public String getSuratBatalExist()
    {
        return suratBatalExist;
    }

    public void setSuratBatalExist( String suratBatalExist )
    {
        this.suratBatalExist = suratBatalExist;
    }

    public String getSuratRedemptExist()
    {
        return suratRedemptExist;
    }

    public void setSuratRedemptExist( String suratRedemptExist )
    {
        this.suratRedemptExist = suratRedemptExist;
    }

    public String getUpdateWho()
    {
        return updateWho;
    }

    public void setUpdateWho( String updateWho )
    {
        this.updateWho = updateWho;
    }

	public String getNewSpaj() {
		return newSpaj;
	}

	public void setNewSpaj(String newSpaj) {
		this.newSpaj = newSpaj;
	}

	public Date getTglKirimDokFisik() {
		return tglKirimDokFisik;
	}

	public void setTglKirimDokFisik(Date tglKirimDokFisik) {
		this.tglKirimDokFisik = tglKirimDokFisik;
	}

    public String getAksesHapusDraft() {
		return aksesHapusDraft;
	}

	public void setAksesHapusDraft(String aksesHapusDraft) {
		this.aksesHapusDraft = aksesHapusDraft;
	}
}
