package com.ekalife.elions.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OutstandingBSM implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2032926849885830566L;
	private String reg_spaj;
	private String mspo_policy_no;
	private String produk;
	private String kurs;
	private String pemegang;
	private String polis;
	private Double premi;
	private Double up;
	private String rollover;
	private Integer mgi;
	private Double bunga;
	private Double persen;
	private Date print_polis;
	private Date tgl_mulai;
	private Date tgl_aksep;
	private String agent_penutup;
	private String agent_referral;
	private String rek_cabang;
	private String rek_nama;
	private String rek_no;
	private String transaksi;
	private Date tgl;
	private String lji_invest;
	private Date lnu_tgl;
	private Double total_unit;
	private Double harga_unit;
	private Double nilai_polis;
	private String lji_id;
	
	public Date getTgl() {
		return tgl;
	}
	public void setTgl(Date tgl) {
		this.tgl = tgl;
	}
	public String getLji_invest() {
		return lji_invest;
	}
	public void setLji_invest(String lji_invest) {
		this.lji_invest = lji_invest;
	}
	public Date getLnu_tgl() {
		return lnu_tgl;
	}
	public void setLnu_tgl(Date lnu_tgl) {
		this.lnu_tgl = lnu_tgl;
	}
	public Double getTotal_unit() {
		return total_unit;
	}
	public void setTotal_unit(Double total_unit) {
		this.total_unit = total_unit;
	}
	public Double getHarga_unit() {
		return harga_unit;
	}
	public void setHarga_unit(Double harga_unit) {
		this.harga_unit = harga_unit;
	}
	public Double getNilai_polis() {
		return nilai_polis;
	}
	public void setNilai_polis(Double nilai_polis) {
		this.nilai_polis = nilai_polis;
	}
	public String getLji_id() {
		return lji_id;
	}
	public void setLji_id(String lji_id) {
		this.lji_id = lji_id;
	}
	public String getTransaksi() {
		return transaksi;
	}
	public void setTransaksi(String transaksi) {
		this.transaksi = transaksi;
	}
	public String getProduk() {
		return produk;
	}
	public void setProduk(String produk) {
		this.produk = produk;
	}
	public String getKurs() {
		return kurs;
	}
	public void setKurs(String kurs) {
		this.kurs = kurs;
	}
	public String getPemegang() {
		return pemegang;
	}
	public void setPemegang(String pemegang) {
		this.pemegang = pemegang;
	}
	public String getPolis() {
		return polis;
	}
	public void setPolis(String polis) {
		this.polis = polis;
	}
	public Double getPremi() {
		return premi;
	}
	public void setPremi(Double premi) {
		this.premi = premi;
	}
	public Double getUp() {
		return up;
	}
	public void setUp(Double up) {
		this.up = up;
	}
	public String getRollover() {
		return rollover;
	}
	public void setRollover(String rollover) {
		this.rollover = rollover;
	}
	public Integer getMgi() {
		return mgi;
	}
	public void setMgi(Integer mgi) {
		this.mgi = mgi;
	}
	public Double getBunga() {
		return bunga;
	}
	public void setBunga(Double bunga) {
		this.bunga = bunga;
	}
	public Double getPersen() {
		return persen;
	}
	public void setPersen(Double persen) {
		this.persen = persen;
	}
	public Date getPrint_polis() {
		return print_polis;
	}
	public void setPrint_polis(Date print_polis) {
		this.print_polis = print_polis;
	}
	public Date getTgl_mulai() {
		return tgl_mulai;
	}
	public void setTgl_mulai(Date tgl_mulai) {
		this.tgl_mulai = tgl_mulai;
	}
	public Date getTgl_aksep() {
		return tgl_aksep;
	}
	public void setTgl_aksep(Date tgl_aksep) {
		this.tgl_aksep = tgl_aksep;
	}
	public String getAgent_penutup() {
		return agent_penutup;
	}
	public void setAgent_penutup(String agent_penutup) {
		this.agent_penutup = agent_penutup;
	}
	public String getAgent_referral() {
		return agent_referral;
	}
	public void setAgent_referral(String agent_referral) {
		this.agent_referral = agent_referral;
	}
	public String getRek_cabang() {
		return rek_cabang;
	}
	public void setRek_cabang(String rek_cabang) {
		this.rek_cabang = rek_cabang;
	}
	public String getRek_nama() {
		return rek_nama;
	}
	public void setRek_nama(String rek_nama) {
		this.rek_nama = rek_nama;
	}
	public String getRek_no() {
		return rek_no;
	}
	public void setRek_no(String rek_no) {
		this.rek_no = rek_no;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String regSpaj) {
		reg_spaj = regSpaj;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspoPolicyNo) {
		mspo_policy_no = mspoPolicyNo;
	}
	
}
