package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class DTH implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String rownum;
	private Date tgl_penarikan;
	private Integer jumlah;
	private String keterangan;
	
	
	public String getRownum() {
		return rownum;
	}
	public void setRownum(String rownum) {
		this.rownum = rownum;
	}
	public Date getTgl_penarikan() {
		return tgl_penarikan;
	}
	public void setTgl_penarikan(Date tgl_penarikan) {
		this.tgl_penarikan = tgl_penarikan;
	}
	public Integer getJumlah() {
		return jumlah;
	}
	public void setJumlah(Integer jumlah) {
		this.jumlah = jumlah;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	
}
