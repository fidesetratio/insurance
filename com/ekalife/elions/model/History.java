package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class History implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Date tanggal;                          
	private String jenis;                          
	private Integer status_polis;                  
	private Integer lus_id;                        
	private String keterangan;
	public String getJenis() {
		return jenis;
	}
	public void setJenis(String jenis) {
		this.jenis = jenis;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public Integer getStatus_polis() {
		return status_polis;
	}
	public void setStatus_polis(Integer status_polis) {
		this.status_polis = status_polis;
	}
	public Date getTanggal() {
		return tanggal;
	}
	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	} 
}
