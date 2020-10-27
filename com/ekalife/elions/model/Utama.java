package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Utama implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String spaj;
	private Integer insuredNo;
	private List lsDaftarSpaj;
	private List lsBisnisId;
	private List lsProduk;
	private Integer standard;
 	private Integer tombol;
 	private String proses;
 	private Double totalPremi;
 	private Double totalTot;
 	private List lsStatAksep;
 	private Integer umurPp;
 	private Integer umurTt;
 	private Integer payPeriod;
 	private Integer insPeriod;
 	private String lkuId;
 	private Integer lspdId;
 	private Integer lstbId;
 	private Date begDate;
 	private Integer medical;
 	private Integer lsbsId;
 	private Integer lscbId;
 	private Double tsi;
 	private Double premium;
 	private String bisnisId;
 	private String plan;
 	private Integer msteStandard;
 	private int jumAwal;
	private List lsIcd;
	private Integer sex_tt;
	
	public int getJumAwal() {
		return jumAwal;
	}
	public void setJumAwal(int jumAwal) {
		this.jumAwal = jumAwal;
	}
	public Integer getMsteStandard() {
		return msteStandard;
	}
	public void setMsteStandard(Integer msteStandard) {
		this.msteStandard = msteStandard;
	}
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	public String getBisnisId() {
		return bisnisId;
	}
	public void setBisnisId(String bisnisId) {
		this.bisnisId = bisnisId;
	}
	public Integer getLsbsId() {
		return lsbsId;
	}
	public void setLsbsId(Integer lsbsId) {
		this.lsbsId = lsbsId;
	}
	public Double getPremium() {
		return premium;
	}
	public void setPremium(Double premium) {
		this.premium = premium;
	}
	public Double getTsi() {
		return tsi;
	}
	public void setTsi(Double tsi) {
		this.tsi = tsi;
	}
	public Date getBegDate() {
		return begDate;
	}
	public void setBegDate(Date begDate) {
		this.begDate = begDate;
	}
	public Integer getMedical() {
		return medical;
	}
	public void setMedical(Integer medical) {
		this.medical = medical;
	}
	public Integer getLspdId() {
		return lspdId;
	}
	public void setLspdId(Integer lspdId) {
		this.lspdId = lspdId;
	}
	public Integer getLstbId() {
		return lstbId;
	}
	public void setLstbId(Integer lstbId) {
		this.lstbId = lstbId;
	}
	public Integer getInsPeriod() {
		return insPeriod;
	}
	public void setInsPeriod(Integer insPeriod) {
		this.insPeriod = insPeriod;
	}
	public String getLkuId() {
		return lkuId;
	}
	public void setLkuId(String lkuId) {
		this.lkuId = lkuId;
	}
	public Integer getPayPeriod() {
		return payPeriod;
	}
	public void setPayPeriod(Integer payPeriod) {
		this.payPeriod = payPeriod;
	}
	public Integer getUmurPp() {
		return umurPp;
	}
	public void setUmurPp(Integer umurPp) {
		this.umurPp = umurPp;
	}
	public Integer getUmurTt() {
		return umurTt;
	}
	public void setUmurTt(Integer umurTt) {
		this.umurTt = umurTt;
	}
	public List getLsStatAksep() {
		return lsStatAksep;
	}
	public void setLsStatAksep(List lsStatAksep) {
		this.lsStatAksep = lsStatAksep;
	}
	public List getLsBisnisId() {
		return lsBisnisId;
	}
	public void setLsBisnisId(List lsBisnisId) {
		this.lsBisnisId = lsBisnisId;
	}
	public List getLsDaftarSpaj() {
		return lsDaftarSpaj;
	}
	public void setLsDaftarSpaj(List lsDaftarSpaj) {
		this.lsDaftarSpaj = lsDaftarSpaj;
	}
	public List getLsProduk() {
		return lsProduk;
	}
	public void setLsProduk(List lsProduk) {
		this.lsProduk = lsProduk;
	}
	public String getProses() {
		return proses;
	}
	public void setProses(String proses) {
		this.proses = proses;
	}
	public Integer getStandard() {
		return standard;
	}
	public void setStandard(Integer standard) {
		this.standard = standard;
	}
	public Integer getTombol() {
		return tombol;
	}
	public void setTombol(Integer tombol) {
		this.tombol = tombol;
	}
	public Double getTotalPremi() {
		return totalPremi;
	}
	public void setTotalPremi(Double totalPremi) {
		this.totalPremi = totalPremi;
	}
	public Double getTotalTot() {
		return totalTot;
	}
	public void setTotalTot(Double totalTot) {
		this.totalTot = totalTot;
	}
	public Integer getInsuredNo() {
		return insuredNo;
	}
	public void setInsuredNo(Integer insuredNo) {
		this.insuredNo = insuredNo;
	}
	public String getSpaj() {
		return spaj;
	}
	public void setSpaj(String spaj) {
		this.spaj = spaj;
	}
	public Integer getLscbId() {
		return lscbId;
	}
	public void setLscbId(Integer lscbId) {
		this.lscbId = lscbId;
	}
	public List getLsIcd() {
		return lsIcd;
	}
	public void setLsIcd(List lsIcd) {
		this.lsIcd = lsIcd;
	}
	public Integer getSex_tt() {
		return sex_tt;
	}
	public void setSex_tt(Integer sex_tt) {
		this.sex_tt = sex_tt;
	}
 	
/*	public Integer getTombol() {
		return tombol;
	}

	public void setTombol(Integer tombol) {
		this.tombol = tombol;
	}*/

	
}
