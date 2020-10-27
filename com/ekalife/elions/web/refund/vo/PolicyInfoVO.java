package com.ekalife.elions.web.refund.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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

public class PolicyInfoVO implements Serializable
{
	private static final long serialVersionUID = 5459018241838906070L;
	private String spajNo;
	private String policyNo;
	private String namaProduk;
	private String namaPp;
	private String namaTt;
	private String lkuId;
	private Integer lsgbId;   // dipakai utk mengetahui, bila = 17 maka itu adalah produk unit link
	private String posisi;
	private String fu;
	private Integer lsbsId;
	private Integer lspdId;
	private Integer lsbsLineBus;
	private Integer prevLspdId;
	private String blankoNo;
	private String namaAgent;
	private String kodeAgent;
	private String lus_full_name;
	private String lca_id;
	private Integer lssp_id;

	private String kliAtasNama;
	private String kliNoRek;
	private String kliNamaBank;
	private String kliCabangBank;
	private String kliKotaBank;

	private Date begDate;
	private Date endDate;
    
	private BigDecimal premi;
	private BigDecimal uangPertanggungan;
    
	private String premiDisplay;
	private String uangPertanggunganDisplay;
    
	private Integer flagUserCabang;
    
    //Digunakan Untuk Proses Temp Premi Refund
    public String pesan;
    public String jenisProses;
    public String no_trx;
    public String potongan;
    public String keterangan;
    public Integer tindakan;
	public BigDecimal premitmp;
	public BigDecimal premirefundtmp;
	public Double nilaiKurs;
    public Date tgl_rk;
    public String idCentrix;
	public ArrayList<RefundCentrix> listCentrix;
	public Integer premi_ke;
    

	public String getPesan() {
		return pesan;
	}
	
	public void setPesan(String pesan) {
		this.pesan = pesan;
	}

	public String getJenisProses() {
		return jenisProses;
	}

	public void setJenisProses(String jenisProses) {
		this.jenisProses = jenisProses;
	}

    public String getLca_id() {
		return lca_id;
	}
	
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
    
    public String getLus_full_name() {
		return lus_full_name;
	}

	public void setLus_full_name(String lus_full_name) {
		this.lus_full_name = lus_full_name;
	}

	public Integer getFlagUserCabang() {
		return flagUserCabang;
	}

	public void setFlagUserCabang(Integer flagUserCabang) {
		this.flagUserCabang = flagUserCabang;
	}

	public BigDecimal getPremi() {
		return premi;
	}

	public void setPremi(BigDecimal premi) {
		this.premi = premi;
	}

	public BigDecimal getUangPertanggungan() {
		return uangPertanggungan;
	}

	public void setUangPertanggungan(BigDecimal uangPertanggungan) {
		this.uangPertanggungan = uangPertanggungan;
	}

    public BigDecimal getPremitmp() {
		return premitmp;
	}

	public void setPremitmp(BigDecimal premitmp) {
		this.premitmp = premitmp;
	}
    
    public BigDecimal getPremirefundtmp() {
		return premirefundtmp;
	}

	public void setPremirefundtmp(BigDecimal premirefundtmp) {
		this.premirefundtmp = premirefundtmp;
	}

	public Double getNilaiKurs() {
		return nilaiKurs;
	}

	public void setNilaiKurs(Double nilaiKurs) {
		this.nilaiKurs = nilaiKurs;
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

    public String getNamaTt()
    {
        return namaTt;
    }

    public void setNamaTt( String namaTt )
    {
        this.namaTt = namaTt;
    }

    public String getLkuId()
    {
        return lkuId;
    }

    public void setLkuId( String lkuId )
    {
        this.lkuId = lkuId;
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

    public Integer getLsbsId()
    {
        return lsbsId;
    }

    public void setLsbsId( Integer lsbsId )
    {
        this.lsbsId = lsbsId;
    }

    public String getKliAtasNama()
    {
        return kliAtasNama;
    }

    public void setKliAtasNama( String kliAtasNama )
    {
        this.kliAtasNama = kliAtasNama;
    }

    public String getKliNoRek()
    {
        return kliNoRek;
    }

    public void setKliNoRek( String kliNoRek )
    {
        this.kliNoRek = kliNoRek;
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

	public Integer getLspdId() {
		return lspdId;
	}

	public void setLspdId(Integer lspdId) {
		this.lspdId = lspdId;
	}

	public Integer getLsbsLineBus() {
		return lsbsLineBus;
	}

	public void setLsbsLineBus(Integer lsbsLineBus) {
		this.lsbsLineBus = lsbsLineBus;
	}

	public Integer getPrevLspdId() {
		return prevLspdId;
	}

	public void setPrevLspdId(Integer prevLspdId) {
		this.prevLspdId = prevLspdId;
	}

	public String getBlankoNo() {
		return blankoNo;
	}

	public void setBlankoNo(String blankoNo) {
		this.blankoNo = blankoNo;
	}

	public String getKodeAgent() {
		return kodeAgent;
	}

	public void setKodeAgent(String kodeAgent) {
		this.kodeAgent = kodeAgent;
	}

	public String getNamaAgent() {
		return namaAgent;
	}

	public void setNamaAgent(String namaAgent) {
		this.namaAgent = namaAgent;
	}

	public String getPremiDisplay() {
		return premiDisplay;
	}

	public void setPremiDisplay(String premiDisplay) {
		this.premiDisplay = premiDisplay;
	}

	public String getUangPertanggunganDisplay() {
		return uangPertanggunganDisplay;
	}

	public void setUangPertanggunganDisplay(String uangPertanggunganDisplay) {
		this.uangPertanggunganDisplay = uangPertanggunganDisplay;
	}
	
    public String getNo_trx() {
		return no_trx;
	}

	public void setNo_trx(String no_trx) {
		this.no_trx = no_trx;
	}
	
	public String getPotongan() {
		return potongan;
	}

	public void setPotongan(String potongan) {
		this.potongan = potongan;
	}
	
	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public Integer getTindakan() {
		return tindakan;
	}

	public void setTindakan(Integer tindakan) {
		this.tindakan = tindakan;
	}

	public Date getTgl_rk() {
		return tgl_rk;
	}

	public void setTgl_rk(Date tgl_rk) {
		this.tgl_rk = tgl_rk;
	}
	
    public String getIdCentrix() {
		return idCentrix;
	}

	public void setIdCentrix(String idCentrix) {
		this.idCentrix = idCentrix;
	}

	public ArrayList<RefundCentrix> getListCentrix() {
		return listCentrix;
	}

	public void setListCentrix(ArrayList<RefundCentrix> listCentrix) {
		this.listCentrix = listCentrix;
	}
	
	public Integer getPremi_ke() {
		return premi_ke;
	}

	public void setPremi_ke(Integer premi_ke) {
		this.premi_ke = premi_ke;
	}

	public Integer getLssp_id() {
		return lssp_id;
	}

	public void setLssp_id(Integer lssp_id) {
		this.lssp_id = lssp_id;
	}

}
