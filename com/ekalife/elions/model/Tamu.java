package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Tamu implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String kd_tamu;
	private String nama_tamu;
	private Date tgl_lahir;
	private String kode_area_telp;
	private String no_telp;
	private String no_hp;
	private String msag_id;
	private String lcb_no;
	private String email;
	private String nama_penutup;
	
	public String getKd_tamu() {
		return kd_tamu;
	}


	public void setKd_tamu(String kdTamu) {
		kd_tamu = kdTamu;
	}


	public String getNama_tamu() {
		return nama_tamu;
	}


	public void setNama_tamu(String namaTamu) {
		nama_tamu = namaTamu;
	}


	public Date getTgl_lahir() {
		return tgl_lahir;
	}


	public void setTgl_lahir(Date tglLahir) {
		tgl_lahir = tglLahir;
	}


	public String getKode_area_telp() {
		return kode_area_telp;
	}


	public void setKode_area_telp(String kodeAreaTelp) {
		kode_area_telp = kodeAreaTelp;
	}


	public String getNo_telp() {
		return no_telp;
	}


	public void setNo_telp(String noTelp) {
		no_telp = noTelp;
	}


	public String getNo_hp() {
		return no_hp;
	}


	public void setNo_hp(String noHp) {
		no_hp = noHp;
	}


	public String getMsag_id() {
		return msag_id;
	}


	public void setMsag_id(String msagId) {
		msag_id = msagId;
	}


	public String getLcb_no() {
		return lcb_no;
	}


	public void setLcb_no(String lcbNo) {
		lcb_no = lcbNo;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getNama_penutup() {
		return nama_penutup;
	}


	public void setNama_penutup(String namaPenutup) {
		nama_penutup = namaPenutup;
	}


	public String getMall_nama_penutup() {
		return mall_nama_penutup;
	}


	public void setMall_nama_penutup(String mallNamaPenutup) {
		mall_nama_penutup = mallNamaPenutup;
	}


	private String mall_nama_penutup;
}