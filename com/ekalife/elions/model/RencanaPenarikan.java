package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class RencanaPenarikan implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;
	private Integer jenis;
	private Date tgl_penarikan;
	private Double jumlah;
	private String keterangan;
	private Integer proses;
	private Integer lus_id;
	private Date tgl_input;
	
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getJenis() {
		return jenis;
	}
	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}
	public Date getTgl_penarikan() {
		return tgl_penarikan;
	}
	public void setTgl_penarikan(Date tgl_penarikan) {
		this.tgl_penarikan = tgl_penarikan;
	}
	public Double getJumlah() {
		return jumlah;
	}
	public void setJumlah(Double jumlah) {
		this.jumlah = jumlah;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public Integer getProses() {
		return proses;
	}
	public void setProses(Integer proses) {
		this.proses = proses;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public Date getTgl_input() {
		return tgl_input;
	}
	public void setTgl_input(Date tgl_input) {
		this.tgl_input = tgl_input;
	}
}
