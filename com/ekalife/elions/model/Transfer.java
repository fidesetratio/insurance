package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Transfer implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String spaj;
	private Integer insuredNo;
	private String msteInsured;
	private String mspoPolicyHolder;
	private int liPosisi;
	private Integer info;
	private String lsposDoc;
	private Integer liAksep;
	private Integer liReas;
	private Integer liBackup;
	private Integer lspdId;
	private Integer lstbId;
	private String noPolis;
	private Integer insPeriod;
	private Integer payPeriod;
	private String lkuId;
	private Integer umurPp;
	private Integer umurTt;
	private String lcaId;
	private Integer liLama;
	private List lsDp;
	private String to;
	private Integer pModeId;
	private Date begDate;
	private Date endDate;
	private Integer medical;
	private Integer lsbsId;
	private Integer lsdbsNumber;
	private String bisnisId;
	private Double tsi;
	private Double premium;
	private String msagId;
	private User currentUser;
	private Policy policy;
	private Integer block;
	
	public Policy getPolicy() {
		return policy;
	}
	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
	public User getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	public String getMsagId() {
		return msagId;
	}
	public void setMsagId(String msagId) {
		this.msagId = msagId;
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
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getInfo() {
		return info;
	}
	public void setInfo(Integer info) {
		this.info = info;
	}
	public Integer getBlock() {
		return block;
	}
	public void setBlock(Integer block) {
		this.block = block;
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
	public Integer getLiLama() {
		return liLama;
	}
	public void setLiLama(Integer liLama) {
		this.liLama = liLama;
	}
	public int getLiPosisi() {
		return liPosisi;
	}
	public void setLiPosisi(int liPosisi) {
		this.liPosisi = liPosisi;
	}
	public Integer getLiReas() {
		return liReas;
	}
	public void setLiReas(Integer liReas) {
		this.liReas = liReas;
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
	public List getLsDp() {
		return lsDp;
	}
	public void setLsDp(List lsDp) {
		this.lsDp = lsDp;
	}
	public Integer getLspdId() {
		return lspdId;
	}
	public void setLspdId(Integer lspdId) {
		this.lspdId = lspdId;
	}
	public String getLsposDoc() {
		return lsposDoc;
	}
	public void setLsposDoc(String lsposDoc) {
		this.lsposDoc = lsposDoc;
	}
	public Integer getLstbId() {
		return lstbId;
	}
	public void setLstbId(Integer lstbId) {
		this.lstbId = lstbId;
	}
	public Integer getMedical() {
		return medical;
	}
	public void setMedical(Integer medical) {
		this.medical = medical;
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
	public String getSpaj() {
		return spaj;
	}
	public void setSpaj(String spaj) {
		this.spaj = spaj;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
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
	public Integer getLiBackup() {
		return liBackup;
	}
	public void setLiBackup(Integer liBackup) {
		this.liBackup = liBackup;
	}
	public Integer getLsdbsNumber() {
		return lsdbsNumber;
	}
	public void setLsdbsNumber(Integer lsdbsNumber) {
		this.lsdbsNumber = lsdbsNumber;
	}
	
	
	
}
