package com.ekalife.elions.model;

import java.io.Serializable;

public class CabangBii implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String lcb_no;
	public String nama_cabang;

	public String head_no;
	public String cabang_induk;

	public String wil_no;
	public String wilayah;
	
	public String alamat;
	public String kota;
	public String nama_head;
	public String nama_wakil;
	public String no_telp;
	public String no_fax;
	public Integer level_id;
	public Integer jenis;
	public String kode;
	public String rek_head;
	public String kode_alias;
	public String team_name;
	public String msag_id_ao;
	public String lcb_group;
	public String email_cab;
	public Integer flag_aktif;
	public String email_head;
	public String mnemonic;
	
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	public String getCabang_induk() {
		return cabang_induk;
	}
	public void setCabang_induk(String cabang_induk) {
		this.cabang_induk = cabang_induk;
	}
	public String getEmail_cab() {
		return email_cab;
	}
	public void setEmail_cab(String email_cab) {
		this.email_cab = email_cab;
	}
	public String getEmail_head() {
		return email_head;
	}
	public void setEmail_head(String email_head) {
		this.email_head = email_head;
	}
	public Integer getFlag_aktif() {
		return flag_aktif;
	}
	public void setFlag_aktif(Integer flag_aktif) {
		this.flag_aktif = flag_aktif;
	}
	public String getHead_no() {
		return head_no;
	}
	public void setHead_no(String head_no) {
		this.head_no = head_no;
	}
	public Integer getJenis() {
		return jenis;
	}
	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}
	public String getKode() {
		return kode;
	}
	public void setKode(String kode) {
		this.kode = kode;
	}
	public String getKode_alias() {
		return kode_alias;
	}
	public void setKode_alias(String kode_alias) {
		this.kode_alias = kode_alias;
	}
	public String getKota() {
		return kota;
	}
	public void setKota(String kota) {
		this.kota = kota;
	}
	public String getLcb_group() {
		return lcb_group;
	}
	public void setLcb_group(String lcb_group) {
		this.lcb_group = lcb_group;
	}
	public String getLcb_no() {
		return lcb_no;
	}
	public void setLcb_no(String lcb_no) {
		this.lcb_no = lcb_no;
	}
	public Integer getLevel_id() {
		return level_id;
	}
	public void setLevel_id(Integer level_id) {
		this.level_id = level_id;
	}
	public String getMnemonic() {
		return mnemonic;
	}
	public void setMnemonic(String mnemonic) {
		this.mnemonic = mnemonic;
	}
	public String getMsag_id_ao() {
		return msag_id_ao;
	}
	public void setMsag_id_ao(String msag_id_ao) {
		this.msag_id_ao = msag_id_ao;
	}
	public String getNama_cabang() {
		return nama_cabang;
	}
	public void setNama_cabang(String nama_cabang) {
		this.nama_cabang = nama_cabang;
	}
	public String getNama_head() {
		return nama_head;
	}
	public void setNama_head(String nama_head) {
		this.nama_head = nama_head;
	}
	public String getNama_wakil() {
		return nama_wakil;
	}
	public void setNama_wakil(String nama_wakil) {
		this.nama_wakil = nama_wakil;
	}
	public String getNo_fax() {
		return no_fax;
	}
	public void setNo_fax(String no_fax) {
		this.no_fax = no_fax;
	}
	public String getNo_telp() {
		return no_telp;
	}
	public void setNo_telp(String no_telp) {
		this.no_telp = no_telp;
	}
	public String getRek_head() {
		return rek_head;
	}
	public void setRek_head(String rek_head) {
		this.rek_head = rek_head;
	}
	public String getTeam_name() {
		return team_name;
	}
	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}
	public String getWil_no() {
		return wil_no;
	}
	public void setWil_no(String wil_no) {
		this.wil_no = wil_no;
	}
	public String getWilayah() {
		return wilayah;
	}
	public void setWilayah(String wilayah) {
		this.wilayah = wilayah;
	}

}
