package com.ekalife.elions.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Rider implements Serializable {

	private static final long serialVersionUID = 1L;
	
private String noSpaj;
private BigDecimal bisnisId;
private BigDecimal bisnisIdOld;
private BigDecimal bisnisNumber;
private BigDecimal insuredNo;
private String bisnisName;
private String kursId;
private String prodId;
private BigDecimal msprTsi;
private BigDecimal msprTsiA;
private BigDecimal msprTsiB;
private BigDecimal msprTsiC;
private BigDecimal msprTsiD;
private BigDecimal msprTsiM;
private BigDecimal msprClass;
private BigDecimal msprUnit;
private BigDecimal msprRate;
private BigDecimal msprPersen;
private BigDecimal msprPremium;
private BigDecimal msprDiscount;
private String msprRefNo;
private BigDecimal msprActive;
private BigDecimal msprExtra;
private BigDecimal msprInsPeriod;
private BigDecimal msprBungaKpr;
private BigDecimal insBulan;
private BigDecimal  msprSisaLimit;
private BigDecimal msprWaitPeriod;
private BigDecimal msprUjroh;
private BigDecimal tabarru;
private Date msprEndPay;
private Date endDate;
private Date begDate;
private int sex;
private int umur;
private int umurP;
private int medis;
private int lamaBayar;
private int caraBayar;
private BigDecimal premi;
private int flag;
private int error;
private BigDecimal msprTt;
private Double discount;

public Double getDiscount() {
	return discount;
}
public void setDiscount(Double discount) {
	this.discount = discount;
}
public BigDecimal getMsprTt() {
	return msprTt;
}
public void setMsprTt(BigDecimal msprTt) {
	this.msprTt = msprTt;
}
public Date getBegDate() {
	return begDate;
}
public void setBegDate(Date begDate) {
	this.begDate = begDate;
}
public BigDecimal getBisnisId() {
	return bisnisId;
}
public void setBisnisId(BigDecimal bisnisId) {
	this.bisnisId = bisnisId;
}
public BigDecimal getBisnisNumber() {
	return bisnisNumber;
}
public void setBisnisNumber(BigDecimal bisnisNumber) {
	this.bisnisNumber = bisnisNumber;
}
public int getCaraBayar() {
	return caraBayar;
}
public void setCaraBayar(int caraBayar) {
	this.caraBayar = caraBayar;
}
public Date getEndDate() {
	return endDate;
}
public void setEndDate(Date endDate) {
	this.endDate = endDate;
}
public int getError() {
	return error;
}
public void setError(int error) {
	this.error = error;
}
public int getFlag() {
	return flag;
}
public void setFlag(int flag) {
	this.flag = flag;
}
public BigDecimal getInsBulan() {
	return insBulan;
}
public void setInsBulan(BigDecimal insBulan) {
	this.insBulan = insBulan;
}
public BigDecimal getInsuredNo() {
	return insuredNo;
}
public void setInsuredNo(BigDecimal insuredNo) {
	this.insuredNo = insuredNo;
}
public String getKursId() {
	return kursId;
}
public void setKursId(String kursId) {
	this.kursId = kursId;
}
public int getLamaBayar() {
	return lamaBayar;
}
public void setLamaBayar(int lamaBayar) {
	this.lamaBayar = lamaBayar;
}
public int getMedis() {
	return medis;
}
public void setMedis(int medis) {
	this.medis = medis;
}
public BigDecimal getMsprActive() {
	return msprActive;
}
public void setMsprActive(BigDecimal msprActive) {
	this.msprActive = msprActive;
}
public BigDecimal getMsprBungaKpr() {
	return msprBungaKpr;
}
public void setMsprBungaKpr(BigDecimal msprBungaKpr) {
	this.msprBungaKpr = msprBungaKpr;
}
public BigDecimal getMsprClass() {
	return msprClass;
}
public void setMsprClass(BigDecimal msprClass) {
	this.msprClass = msprClass;
}
public BigDecimal getMsprDiscount() {
	return msprDiscount;
}
public void setMsprDiscount(BigDecimal msprDiscount) {
	this.msprDiscount = msprDiscount;
}
public Date getMsprEndPay() {
	return msprEndPay;
}
public void setMsprEndPay(Date msprEndPay) {
	this.msprEndPay = msprEndPay;
}
public BigDecimal getMsprExtra() {
	return msprExtra;
}
public void setMsprExtra(BigDecimal msprExtra) {
	this.msprExtra = msprExtra;
}
public BigDecimal getMsprInsPeriod() {
	return msprInsPeriod;
}
public void setMsprInsPeriod(BigDecimal msprInsPeriod) {
	this.msprInsPeriod = msprInsPeriod;
}
public BigDecimal getMsprPersen() {
	return msprPersen;
}
public void setMsprPersen(BigDecimal msprPersen) {
	this.msprPersen = msprPersen;
}
public BigDecimal getMsprPremium() {
	return msprPremium;
}
public void setMsprPremium(BigDecimal msprPremium) {
	this.msprPremium = msprPremium;
}
public BigDecimal getMsprRate() {
	return msprRate;
}
public void setMsprRate(BigDecimal msprRate) {
	this.msprRate = msprRate;
}
public String getMsprRefNo() {
	return msprRefNo;
}
public void setMsprRefNo(String msprRefNo) {
	this.msprRefNo = msprRefNo;
}
public BigDecimal getMsprSisaLimit() {
	return msprSisaLimit;
}
public void setMsprSisaLimit(BigDecimal msprSisaLimit) {
	this.msprSisaLimit = msprSisaLimit;
}
public BigDecimal getMsprTsi() {
	return msprTsi;
}
public void setMsprTsi(BigDecimal msprTsi) {
	this.msprTsi = msprTsi;
}
public BigDecimal getMsprTsiA() {
	return msprTsiA;
}
public void setMsprTsiA(BigDecimal msprTsiA) {
	this.msprTsiA = msprTsiA;
}
public BigDecimal getMsprTsiB() {
	return msprTsiB;
}
public void setMsprTsiB(BigDecimal msprTsiB) {
	this.msprTsiB = msprTsiB;
}
public BigDecimal getMsprTsiC() {
	return msprTsiC;
}
public void setMsprTsiC(BigDecimal msprTsiC) {
	this.msprTsiC = msprTsiC;
}
public BigDecimal getMsprTsiD() {
	return msprTsiD;
}
public void setMsprTsiD(BigDecimal msprTsiD) {
	this.msprTsiD = msprTsiD;
}
public BigDecimal getMsprTsiM() {
	return msprTsiM;
}
public void setMsprTsiM(BigDecimal msprTsiM) {
	this.msprTsiM = msprTsiM;
}
public BigDecimal getMsprUjroh() {
	return msprUjroh;
}
public void setMsprUjroh(BigDecimal msprUjroh) {
	this.msprUjroh = msprUjroh;
}
public BigDecimal getMsprUnit() {
	return msprUnit;
}
public void setMsprUnit(BigDecimal msprUnit) {
	this.msprUnit = msprUnit;
}
public BigDecimal getMsprWaitPeriod() {
	return msprWaitPeriod;
}
public void setMsprWaitPeriod(BigDecimal msprWaitPeriod) {
	this.msprWaitPeriod = msprWaitPeriod;
}
public String getNoSpaj() {
	return noSpaj;
}
public void setNoSpaj(String noSpaj) {
	this.noSpaj = noSpaj;
}
public BigDecimal getPremi() {
	return premi;
}
public void setPremi(BigDecimal premi) {
	this.premi = premi;
}
public String getProdId() {
	return prodId;
}
public void setProdId(String prodId) {
	this.prodId = prodId;
}
public int getSex() {
	return sex;
}
public void setSex(int sex) {
	this.sex = sex;
}
public BigDecimal getTabarru() {
	return tabarru;
}
public void setTabarru(BigDecimal tabarru) {
	this.tabarru = tabarru;
}
public int getUmur() {
	return umur;
}
public void setUmur(int umur) {
	this.umur = umur;
}
public int getUmurP() {
	return umurP;
}
public void setUmurP(int umurP) {
	this.umurP = umurP;
}
public String getBisnisName() {
	return bisnisName;
}
public void setBisnisName(String bisnisName) {
	this.bisnisName = bisnisName;
}
public BigDecimal getBisnisIdOld() {
	return bisnisIdOld;
}
public void setBisnisIdOld(BigDecimal bisnisIdOld) {
	this.bisnisIdOld = bisnisIdOld;
}}
