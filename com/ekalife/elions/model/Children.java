package com.ekalife.elions.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class Children implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mns_kd_nasabah;                 
	private Integer mch_id;                        
	private Integer mch_jn_relasi;                 
	private String mch_nama;                       
	private Date mch_birth_date;                   
	private Integer mch_usia;                      
	private String mch_pekerjaan;                  
	private String mch_renc_studi;                 
	private String mch_tempat_studi;               
	private String mch_waktu_realisasi;            
	private BigDecimal mch_biaya;                     
	private String mch_renc_skrg;                  
	private String mch_pengaturan;
	private List<Children> listChildren;
	
	public List<Children> getListChildren() {
		return listChildren;
	}
	public void setListChildren(List<Children> listChildren) {
		this.listChildren = listChildren;
	}
	public BigDecimal getMch_biaya() {
		return mch_biaya;
	}
	public void setMch_biaya(BigDecimal mch_biaya) {
		this.mch_biaya = mch_biaya;
	}
	public Date getMch_birth_date() {
		return mch_birth_date;
	}
	public void setMch_birth_date(Date mch_birth_date) {
		this.mch_birth_date = mch_birth_date;
	}
	public Integer getMch_id() {
		return mch_id;
	}
	public void setMch_id(Integer mch_id) {
		this.mch_id = mch_id;
	}
	public Integer getMch_jn_relasi() {
		return mch_jn_relasi;
	}
	public void setMch_jn_relasi(Integer mch_jn_relasi) {
		this.mch_jn_relasi = mch_jn_relasi;
	}
	public String getMch_nama() {
		return mch_nama;
	}
	public void setMch_nama(String mch_nama) {
		this.mch_nama = mch_nama;
	}
	public String getMch_pekerjaan() {
		return mch_pekerjaan;
	}
	public void setMch_pekerjaan(String mch_pekerjaan) {
		this.mch_pekerjaan = mch_pekerjaan;
	}
	public String getMch_pengaturan() {
		return mch_pengaturan;
	}
	public void setMch_pengaturan(String mch_pengaturan) {
		this.mch_pengaturan = mch_pengaturan;
	}
	public String getMch_renc_skrg() {
		return mch_renc_skrg;
	}
	public void setMch_renc_skrg(String mch_renc_skrg) {
		this.mch_renc_skrg = mch_renc_skrg;
	}
	public String getMch_renc_studi() {
		return mch_renc_studi;
	}
	public void setMch_renc_studi(String mch_renc_studi) {
		this.mch_renc_studi = mch_renc_studi;
	}
	public String getMch_tempat_studi() {
		return mch_tempat_studi;
	}
	public void setMch_tempat_studi(String mch_tempat_studi) {
		this.mch_tempat_studi = mch_tempat_studi;
	}
	public Integer getMch_usia() {
		return mch_usia;
	}
	public void setMch_usia(Integer mch_usia) {
		this.mch_usia = mch_usia;
	}
	public String getMch_waktu_realisasi() {
		return mch_waktu_realisasi;
	}
	public void setMch_waktu_realisasi(String mch_waktu_realisasi) {
		this.mch_waktu_realisasi = mch_waktu_realisasi;
	}
	public String getMns_kd_nasabah() {
		return mns_kd_nasabah;
	}
	public void setMns_kd_nasabah(String mns_kd_nasabah) {
		this.mns_kd_nasabah = mns_kd_nasabah;
	}  
	
}