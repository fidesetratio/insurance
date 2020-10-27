package com.ekalife.elions.model;

import java.io.Serializable;


public class CekValidPrintPolis implements Serializable {

	private static final long serialVersionUID = 1L;
	
	String reg_spaj;
	String cek_polis_utama;
	String cek_manfaat;
	String cek_alo_dana;
	String ttp;
	String sppp;
	String ssu;
	String kp;
	public String getKp() {
		return kp;
	}
	public void setKp(String kp) {
		this.kp = kp;
	}
	public String getSppp() {
		return sppp;
	}
	public void setSppp(String sppp) {
		this.sppp = sppp;
	}
	public String getSsu() {
		return ssu;
	}
	public void setSsu(String ssu) {
		this.ssu = ssu;
	}
	public String getTtp() {
		return ttp;
	}
	public void setTtp(String ttp) {
		this.ttp = ttp;
	}
	public String getCek_alo_dana() {
		return cek_alo_dana;
	}
	public void setCek_alo_dana(String cek_alo_dana) {
		this.cek_alo_dana = cek_alo_dana;
	}
	public String getCek_manfaat() {
		return cek_manfaat;
	}
	public void setCek_manfaat(String cek_manfaat) {
		this.cek_manfaat = cek_manfaat;
	}
	public String getCek_polis_utama() {
		return cek_polis_utama;
	}
	public void setCek_polis_utama(String cek_polis_utama) {
		this.cek_polis_utama = cek_polis_utama;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	
	
}