package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Absen implements Serializable {

	private static final long serialVersionUID = 2267377398484570605L;
	public String msag_id;
	public Date tanggal;
	public Date jam_masuk;
	public Date jam_keluar;
	
	 
	public String getMsag_id() {
		return msag_id;
	}
	public void setMsag_id(String msagId) {
		msag_id = msagId;
	}
	public Date getTanggal() {
		return tanggal;
	}
	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}
	public Date getJam_masuk() {
		return jam_masuk;
	}
	public void setJam_masuk(Date jamMasuk) {
		jam_masuk = jamMasuk;
	}
	public Date getJam_keluar() {
		return jam_keluar;
	}
	public void setJam_keluar(Date jamKeluar) {
		jam_keluar = jamKeluar;
	}
	
}