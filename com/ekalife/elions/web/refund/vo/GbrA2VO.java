package com.ekalife.elions.web.refund.vo;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: Lamp1VO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 8, 2008 3:09:06 PM
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ekalife.utils.Common;

public class GbrA2VO implements Serializable
{
	private static final long serialVersionUID = 8517386402499500973L;
	String noUrutMemo;
    String hal;
    String tanggal;
    String spajNo;
    String polisNo;
    String produk;
    String pemegangPolis;
    String tertanggung;
    String statement;
    String jumlahTerbilang;
    String atasNama;
    String rekeningNo;
    String bankName;
    String cabang;
    String kota;
    String signer;
    String noVoucher;
    String ttd;
    Integer posisi;
    String pembatal;
    String lca_id;
    ArrayList<HashMap<String, String>> rincianPolisList;
    ArrayList<HashMap<String, String>> lampiranList;
    ArrayList<HashMap<String, String>> addLampiranList;
    ArrayList <PreviewEditParamsVO> tempDescrDanJumlah;
    ArrayList < SetoranPremiDbVO > setoranPokokAndTopUp;
    
    BigDecimal totalPremiDikembalikan;
    BigDecimal biayaAdministrasi;
    BigDecimal premiDikembalikan;
    
	Integer prevLspdId;

    Date endDate;
    Date begDate;
    
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

	public Integer getPrevLspdId() {
		return prevLspdId;
	}

	public void setPrevLspdId(Integer prevLspdId) {
		this.prevLspdId = prevLspdId;
	}

	public BigDecimal getPremiDikembalikan() {
		return premiDikembalikan;
	}

	public void setPremiDikembalikan(BigDecimal premiDikembalikan) {
		this.premiDikembalikan = premiDikembalikan;
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

    public String getPolisNo()
    {
        return polisNo;
    }

    public void setPolisNo( String polisNo )
    {
        this.polisNo = polisNo;
    }

    public String getProduk()
    {
        return produk;
    }

    public void setProduk( String produk )
    {
        this.produk = produk;
    }

    public String getPemegangPolis()
    {
        return pemegangPolis;
    }

    public void setPemegangPolis( String pemegangPolis )
    {
        this.pemegangPolis = pemegangPolis;
    }

    public String getTertanggung()
    {
        return tertanggung;
    }

    public void setTertanggung( String tertanggung )
    {
        this.tertanggung = tertanggung;
    }

    public String getStatement()
    {
        return statement;
    }

    public void setStatement( String statement )
    {
        this.statement = statement;
    }

    public String getJumlahTerbilang()
    {
        return jumlahTerbilang;
    }

    public void setJumlahTerbilang( String jumlahTerbilang )
    {
        this.jumlahTerbilang = jumlahTerbilang;
    }

    public String getAtasNama()
    {
        return atasNama;
    }

    public void setAtasNama( String atasNama )
    {
        this.atasNama = atasNama;
    }

    public String getRekeningNo()
    {
        return rekeningNo;
    }

    public void setRekeningNo( String rekeningNo )
    {
        this.rekeningNo = rekeningNo;
    }

    public String getBankName()
    {
        return bankName;
    }

    public void setBankName( String bankName )
    {
        this.bankName = bankName;
    }

    public String getCabang()
    {
        return cabang;
    }

    public void setCabang( String cabang )
    {
        this.cabang = cabang;
    }

    public String getKota()
    {
        return kota;
    }

    public void setKota( String kota )
    {
        this.kota = kota;
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

    public ArrayList<HashMap<String, String>> getLampiranList()
    {
        return lampiranList;
    }

    public void setLampiranList( ArrayList<HashMap<String, String>> lampiranList )
    {
        this.lampiranList = Common.serializableList(lampiranList);
    }

    public BigDecimal getTotalPremiDikembalikan()
    {
        return totalPremiDikembalikan;
    }

    public void setTotalPremiDikembalikan( BigDecimal totalPremiDikembalikan )
    {
        this.totalPremiDikembalikan = totalPremiDikembalikan;
    }

	public ArrayList<PreviewEditParamsVO> getTempDescrDanJumlah() {
		return tempDescrDanJumlah;
	}

	public void setTempDescrDanJumlah(ArrayList<PreviewEditParamsVO> tempDescrDanJumlah) {
		this.tempDescrDanJumlah = Common.serializableList(tempDescrDanJumlah);
	}

	public BigDecimal getBiayaAdministrasi() {
		return biayaAdministrasi;
	}

	public void setBiayaAdministrasi(BigDecimal biayaAdministrasi) {
		this.biayaAdministrasi = biayaAdministrasi;
	}

	public String getNoVoucher() {
		return noVoucher;
	}

	public void setNoVoucher(String noVoucher) {
		this.noVoucher = noVoucher;
	}

	public Integer getPosisi() {
		return posisi;
	}

	public void setPosisi(Integer posisi) {
		this.posisi = posisi;
	}

	public ArrayList<SetoranPremiDbVO> getSetoranPokokAndTopUp() {
		return setoranPokokAndTopUp;
	}

	public void setSetoranPokokAndTopUp(ArrayList<SetoranPremiDbVO> setoranPokokAndTopUp) {
		this.setoranPokokAndTopUp = Common.serializableList(setoranPokokAndTopUp);
	}

	public ArrayList<HashMap<String, String>> getAddLampiranList() {
		return addLampiranList;
	}

	public void setAddLampiranList(ArrayList<HashMap<String, String>> addLampiranList) {
		this.addLampiranList = Common.serializableList(addLampiranList);
	}


}
