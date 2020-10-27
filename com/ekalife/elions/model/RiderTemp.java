package com.ekalife.elions.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RiderTemp implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private BigDecimal msprTsi;
	private BigDecimal bisnisId;
	private BigDecimal bisnisNo;
	private Date begDate;
	private BigDecimal umur;
	private BigDecimal umurp;
	private BigDecimal lamaBayar;
	private BigDecimal caraBayar;
	private String kursId;
	private BigDecimal premi;
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
	public BigDecimal getBisnisNo() {
		return bisnisNo;
	}
	public void setBisnisNo(BigDecimal bisnisNo) {
		this.bisnisNo = bisnisNo;
	}
	public BigDecimal getCaraBayar() {
		return caraBayar;
	}
	public void setCaraBayar(BigDecimal caraBayar) {
		this.caraBayar = caraBayar;
	}
	public String getKursId() {
		return kursId;
	}
	public void setKursId(String kursId) {
		this.kursId = kursId;
	}
	public BigDecimal getLamaBayar() {
		return lamaBayar;
	}
	public void setLamaBayar(BigDecimal lamaBayar) {
		this.lamaBayar = lamaBayar;
	}
	public BigDecimal getMsprTsi() {
		return msprTsi;
	}
	public void setMsprTsi(BigDecimal msprTsi) {
		this.msprTsi = msprTsi;
	}
	public BigDecimal getPremi() {
		return premi;
	}
	public void setPremi(BigDecimal premi) {
		this.premi = premi;
	}
	public BigDecimal getUmur() {
		return umur;
	}
	public void setUmur(BigDecimal umur) {
		this.umur = umur;
	}
	public BigDecimal getUmurp() {
		return umurp;
	}
	public void setUmurp(BigDecimal umurp) {
		this.umurp = umurp;
	}
	
	
	
}
