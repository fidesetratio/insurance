package com.ekalife.elions.model;

import java.io.Serializable;

public class SurplusCalc implements Serializable {

	private static final long serialVersionUID = 1L;
	
private String mns_kd_nasabah;                 
private Double jumlah_butuh;                   
private Integer tahun_butuh;                   
private Double rate_inflasi;                   
private Double jumlah_tersedia;                
private Double pct_invest;                     
private Double tambahan;

public Double getJumlah_butuh() {
	return jumlah_butuh;
}
public void setJumlah_butuh(Double jumlah_butuh) {
	this.jumlah_butuh = jumlah_butuh;
}
public Double getJumlah_tersedia() {
	return jumlah_tersedia;
}
public void setJumlah_tersedia(Double jumlah_tersedia) {
	this.jumlah_tersedia = jumlah_tersedia;
}
public String getMns_kd_nasabah() {
	return mns_kd_nasabah;
}
public void setMns_kd_nasabah(String mns_kd_nasabah) {
	this.mns_kd_nasabah = mns_kd_nasabah;
}
public Double getPct_invest() {
	return pct_invest;
}
public void setPct_invest(Double pct_invest) {
	this.pct_invest = pct_invest;
}
public Double getRate_inflasi() {
	return rate_inflasi;
}
public void setRate_inflasi(Double rate_inflasi) {
	this.rate_inflasi = rate_inflasi;
}
public Integer getTahun_butuh() {
	return tahun_butuh;
}
public void setTahun_butuh(Integer tahun_butuh) {
	this.tahun_butuh = tahun_butuh;
}
public Double getTambahan() {
	return tambahan;
}
public void setTambahan(Double tambahan) {
	this.tambahan = tambahan;
}  


}