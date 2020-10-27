package com.ekalife.elions.model.cross_selling;

import java.io.Serializable;
import java.util.Date;

/**
 * Model Class untuk LST_FAT_HISTORY (database HRD)
 * 
 * @author Yusuf
 * @since Apr 27, 2011 (5:53:30 PM)
 *
 */
public class FatHistory implements Serializable{

	private static final long serialVersionUID = -7443061718802725396L;
	
	public String fatid;
	public Integer posisi;
	public Integer lus_id;
	public String nik_input;
	public Date tanggal;
	public String keterangan;
	
	public FatHistory() {}

	public FatHistory(String fatid, Integer posisi, Integer lus_id, String nik_input, Date tanggal, String keterangan) {
		this.fatid = fatid;
		this.posisi = posisi;
		this.lus_id = lus_id;
		this.nik_input = nik_input;
		this.tanggal = tanggal;
		this.keterangan = keterangan;
	}

	public String getFatid() {
		return fatid;
	}
	public void setFatid(String fatid) {
		this.fatid = fatid;
	}
	public Integer getPosisi() {
		return posisi;
	}
	public void setPosisi(Integer posisi) {
		this.posisi = posisi;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getNik_input() {
		return nik_input;
	}
	public void setNik_input(String nik_input) {
		this.nik_input = nik_input;
	}
	public Date getTanggal() {
		return tanggal;
	}
	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	
}