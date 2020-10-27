package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class DaftarSpajOtorisasi implements Serializable {

	private static final long serialVersionUID = -7778817355831962683L;
	private String reg_spaj;                       
	private Date mspo_input_date;                     
	private String produk;                       
	private Date mste_beg_date;                 
	private Date mste_end_date;                  
	private String nama_cabang;                     
	private String penutup;
	private String pemegang;
	private String tertanggung;
	private String mspo_policy_no;
	private String lsrg_nama;
	private String lsdbs_name;
	
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}
	public String getLsrg_nama() {
		return lsrg_nama;
	}
	public void setLsrg_nama(String lsrg_nama) {
		this.lsrg_nama = lsrg_nama;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public Date getMspo_input_date() {
		return mspo_input_date;
	}
	public void setMspo_input_date(Date mspo_input_date) {
		this.mspo_input_date = mspo_input_date;
	}
	public Date getMste_beg_date() {
		return mste_beg_date;
	}
	public void setMste_beg_date(Date mste_beg_date) {
		this.mste_beg_date = mste_beg_date;
	}
	public Date getMste_end_date() {
		return mste_end_date;
	}
	public void setMste_end_date(Date mste_end_date) {
		this.mste_end_date = mste_end_date;
	}
	public String getNama_cabang() {
		return nama_cabang;
	}
	public void setNama_cabang(String nama_cabang) {
		this.nama_cabang = nama_cabang;
	}
	public String getPemegang() {
		return pemegang;
	}
	public void setPemegang(String pemegang) {
		this.pemegang = pemegang;
	}
	public String getPenutup() {
		return penutup;
	}
	public void setPenutup(String penutup) {
		this.penutup = penutup;
	}
	public String getProduk() {
		return produk;
	}
	public void setProduk(String produk) {
		this.produk = produk;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getTertanggung() {
		return tertanggung;
	}
	public void setTertanggung(String tertanggung) {
		this.tertanggung = tertanggung;
	}
	
	

	
}
