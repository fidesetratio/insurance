package com.ekalife.elions.model;

import java.io.Serializable;

public class AddReffBii implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String namaReff;
	private String kode;
	private String nama_cabang;
	private String npk;
	private String kodeAgent;
	private Integer lisensi;
	private Integer lrb_id;
	private Integer flag_aktif;
	private Integer jenis;
	
	
	public Integer getFlag_aktif() {
		return flag_aktif;
	}
	public void setFlag_aktif(Integer flag_aktif) {
		this.flag_aktif = flag_aktif;
	}
	public Integer getJenis() {
		return jenis;
	}
	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}
	public Integer getLrb_id() {
		return lrb_id;
	}
	public void setLrb_id(Integer lrb_id) {
		this.lrb_id = lrb_id;
	}
	public String getKode() {
		return kode;
	}
	public void setKode(String kode) {
		this.kode = kode;
	}
	public String getKodeAgent() {
		return kodeAgent;
	}
	public void setKodeAgent(String kodeAgent) {
		this.kodeAgent = kodeAgent;
	}
	public Integer getLisensi() {
		return lisensi;
	}
	public void setLisensi(Integer lisensi) {
		this.lisensi = lisensi;
	}
	public String getNama_cabang() {
		return nama_cabang;
	}
	public void setNama_cabang(String nama_cabang) {
		this.nama_cabang = nama_cabang;
	}
	public String getNamaReff() {
		return namaReff;
	}
	public void setNamaReff(String namaReff) {
		this.namaReff = namaReff;
	}
	public String getNpk() {
		return npk;
	}
	public void setNpk(String npk) {
		this.npk = npk;
	}
	
	
}