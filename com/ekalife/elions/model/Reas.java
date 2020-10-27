package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Reas implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String spaj;
	private User currentUser;
	private Integer info;
	private Integer insuredNo;
	private String msteInsured;
	private Integer umurTt;
	private Integer umurPp;
	private String noPolis;
	private String mspoPolicyHolder;
	private Integer insPeriod;
	private Integer payPeriod;
	private String lkuId;
	private String lcaId;
	private String lsPosDoc;
	private Integer liAksep;
	private Integer liReas;
	private Integer lspdId;
	private Integer lstbId;
	private Date mste_kyc_date;
	private String simultan_id;
	public Date getMste_kyc_date() {
		return mste_kyc_date;
	}
	public void setMste_kyc_date(Date mste_kyc_date) {
		this.mste_kyc_date = mste_kyc_date;
	}
	public User getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	public Integer getInfo() {
		return info;
	}
	public void setInfo(Integer info) {
		this.info = info;
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
	public Integer getLspdId() {
		return lspdId;
	}
	public void setLspdId(Integer lspdId) {
		this.lspdId = lspdId;
	}
	public String getLsPosDoc() {
		return lsPosDoc;
	}
	public void setLsPosDoc(String lsPosDoc) {
		this.lsPosDoc = lsPosDoc;
	}
	public Integer getLstbId() {
		return lstbId;
	}
	public void setLstbId(Integer lstbId) {
		this.lstbId = lstbId;
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
	public String getSpaj() {
		return spaj;
	}
	public void setSpaj(String spaj) {
		this.spaj = spaj;
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
	public String getSimultan_id() {
		return simultan_id;
	}
	public void setSimultan_id(String simultan_id) {
		this.simultan_id = simultan_id;
	}
	
	
}
