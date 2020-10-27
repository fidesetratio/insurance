package com.ekalife.elions.model;

import java.io.Serializable;

/**
 * 
 * @author alfian_h
 *
 * Model Calon Karyawan
 */
public class CalonKaryawan implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ktp;
	private String nama;
	private Integer jk;
	private String alamat;
	private String tempat_lahir;
	private String tanggal_lahir;
	private Integer blacklist;
	private Integer perusahaan_lama;
	private Integer status_join;
	private String dokumen;
	private String lca_id;
	

	
	public CalonKaryawan() {
	}
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
	public String getDokumen() {
		return dokumen;
	}
	public void setDokumen(String dokumen) {
		this.dokumen = dokumen;
	}
	public String getKtp() {
		return ktp;
	}
	public void setKtp(String ktp) {
		this.ktp = ktp;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public Integer getJk() {
		return jk;
	}
	public void setJk(Integer jk) {
		this.jk = jk;
	}
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	public String getTempat_lahir() {
		return tempat_lahir;
	}
	public void setTempat_lahir(String tempat_lahir) {
		this.tempat_lahir = tempat_lahir;
	}
	public String getTanggal_lahir() {
		return tanggal_lahir;
	}
	public void setTanggal_lahir(String tanggal_lahir) {
		this.tanggal_lahir = tanggal_lahir;
	}
	public Integer getBlacklist() {
		return blacklist;
	}
	public void setBlacklist(Integer blacklist) {
		this.blacklist = blacklist;
	}
	public Integer getPerusahaan_lama() {
		return perusahaan_lama;
	}
	public void setPerusahaan_lama(Integer perusahaan_lama) {
		this.perusahaan_lama = perusahaan_lama;
	}
	public Integer getStatus_join() {
		return status_join;
	}
	public void setStatus_join(Integer status_join) {
		this.status_join = status_join;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
