package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Akseptasi implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String spaj;
	private Integer insuredNo;
	private boolean lbUlink;
	private int info1;
	private int info2;
	private User currentUser;
	private String proses;
	private Integer error;
	private Integer pModeId;
	private Date begDate;
	private Integer medical;
	private Integer lsbsId;
	private String bisnisId;
	private Double premium;
	private Double tsi;
	private String msteInsured;
	private String mspoPolicyHolder;
	private String msagId;
	private Integer umurTt;
	private Integer umurPp;
	private String noPolis;
	private Integer payPeriod;
	private String lkuId;
	private String lcaId;
	private Integer liReas;
	private Integer liAksep;
	private Integer subliAksep;
	private String lsposDoc;
	private List lsStatusAksep;
	private int listSize;
	private Integer insPeriod;
	private String tglAwal;
	private String tglAkhir;
	private Integer lspdId;
	private Integer lsspId;
	private Integer lssaId;
	private Integer lstbId;
	private Integer size;
	private Integer lsdbsNumber;
	private String pesan;
	private Integer flag_investasi;
	private String substandart;
	private String block;
	
	public Integer getFlag_investasi() {
		return flag_investasi;
	}
	public void setFlag_investasi(Integer flag_investasi) {
		this.flag_investasi = flag_investasi;
	}
	public String getPesan() {
		return pesan;
	}
	public void setPesan(String pesan) {
		this.pesan = pesan;
	}
	public String getBlock() {
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
	}
	public Integer getLsdbsNumber() {
		return lsdbsNumber;
	}
	public void setLsdbsNumber(Integer lsdbsNumber) {
		this.lsdbsNumber = lsdbsNumber;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Integer getLspdId() {
		return lspdId;
	}
	public void setLspdId(Integer lspdId) {
		this.lspdId = lspdId;
	}
	public Integer getLssaId() {
		return lssaId;
	}
	public void setLssaId(Integer lssaId) {
		this.lssaId = lssaId;
	}
	public Integer getLsspId() {
		return lsspId;
	}
	public void setLsspId(Integer lsspId) {
		this.lsspId = lsspId;
	}
	public Integer getLstbId() {
		return lstbId;
	}
	public void setLstbId(Integer lstbId) {
		this.lstbId = lstbId;
	}
	public Date getBegDate() {
		return begDate;
	}
	public void setBegDate(Date begDate) {
		this.begDate = begDate;
	}
	public String getBisnisId() {
		return bisnisId;
	}
	public void setBisnisId(String bisnisId) {
		this.bisnisId = bisnisId;
	}
	public User getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	public Integer getError() {
		return error;
	}
	public void setError(Integer error) {
		this.error = error;
	}
	public int getInfo1() {
		return info1;
	}
	public void setInfo1(int info1) {
		this.info1 = info1;
	}
	public int getInfo2() {
		return info2;
	}
	public void setInfo2(int info2) {
		this.info2 = info2;
	}
	public Integer getInsPeriod() {
		return insPeriod;
	}
	public void setInsPeriod(Integer insPeriod) {
		this.insPeriod = insPeriod;
	}
	public Integer getInsuredNo() {
		return insuredNo;
	}
	public void setInsuredNo(Integer insuredNo) {
		this.insuredNo = insuredNo;
	}
	public boolean isLbUlink() {
		return lbUlink;
	}
	public void setLbUlink(boolean lbUlink) {
		this.lbUlink = lbUlink;
	}
	public String getLcaId() {
		return lcaId;
	}
	public void setLcaId(String lcaId) {
		this.lcaId = lcaId;
	}
	public Integer getLiAksep() {
		return liAksep;
	}
	public void setLiAksep(Integer liAksep) {
		this.liAksep = liAksep;
	}
	public Integer getLiReas() {
		return liReas;
	}
	public void setLiReas(Integer liReas) {
		this.liReas = liReas;
	}
	public int getListSize() {
		return listSize;
	}
	public void setListSize(int listSize) {
		this.listSize = listSize;
	}
	public String getLkuId() {
		return lkuId;
	}
	public void setLkuId(String lkuId) {
		this.lkuId = lkuId;
	}
	public Integer getLsbsId() {
		return lsbsId;
	}
	public void setLsbsId(Integer lsbsId) {
		this.lsbsId = lsbsId;
	}
	public String getLsposDoc() {
		return lsposDoc;
	}
	public void setLsposDoc(String lsposDoc) {
		this.lsposDoc = lsposDoc;
	}
	public List getLsStatusAksep() {
		return lsStatusAksep;
	}
	public void setLsStatusAksep(List lsStatusAksep) {
		this.lsStatusAksep = lsStatusAksep;
	}
	public Integer getMedical() {
		return medical;
	}
	public void setMedical(Integer medical) {
		this.medical = medical;
	}
	public String getMsagId() {
		return msagId;
	}
	public void setMsagId(String msagId) {
		this.msagId = msagId;
	}
	public String getMspoPolicyHolder() {
		return mspoPolicyHolder;
	}
	public void setMspoPolicyHolder(String mspoPolicyHolder) {
		this.mspoPolicyHolder = mspoPolicyHolder;
	}
	public String getMsteInsured() {
		return msteInsured;
	}
	public void setMsteInsured(String msteInsured) {
		this.msteInsured = msteInsured;
	}
	public String getNoPolis() {
		return noPolis;
	}
	public void setNoPolis(String noPolis) {
		this.noPolis = noPolis;
	}
	public Integer getPayPeriod() {
		return payPeriod;
	}
	public void setPayPeriod(Integer payPeriod) {
		this.payPeriod = payPeriod;
	}
	public Integer getPModeId() {
		return pModeId;
	}
	public void setPModeId(Integer modeId) {
		pModeId = modeId;
	}
	public Double getPremium() {
		return premium;
	}
	public void setPremium(Double premium) {
		this.premium = premium;
	}
	public String getProses() {
		return proses;
	}
	public void setProses(String proses) {
		this.proses = proses;
	}
	public String getSpaj() {
		return spaj;
	}
	public void setSpaj(String spaj) {
		this.spaj = spaj;
	}
	public String getTglAkhir() {
		return tglAkhir;
	}
	public void setTglAkhir(String tglAkhir) {
		this.tglAkhir = tglAkhir;
	}
	public String getTglAwal() {
		return tglAwal;
	}
	public void setTglAwal(String tglAwal) {
		this.tglAwal = tglAwal;
	}
	public Double getTsi() {
		return tsi;
	}
	public void setTsi(Double tsi) {
		this.tsi = tsi;
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
	public Integer getSubliAksep() {
		return subliAksep;
	}
	public void setSubliAksep(Integer subliAksep) {
		this.subliAksep = subliAksep;
	}
	
	public String getSubstandart() {
		return substandart;
	}
	public void setSubstandart(String substandart) {
		this.substandart = substandart;
	}
		
}
