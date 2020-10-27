package com.ekalife.elions.model;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DBank implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String no_pre;

	public Integer no_jurnal;

	public Integer kode_cash_flow;

	public String keterangan;

	public String kas;

	public Double jumlah;

	public String giro;

	public String key_jurnal;

	public Integer type_trx;

	public Date tgl_input;

	public TBank tbank;

	public User user;

	public Integer flag;

	public String value;

	public String lus_login_name;

	public String lus_id;

	public Double debet;

	public Double kredit;

	public Double balance;

	public List<DropDown> listKas;

	public List<DBank> listDBank;

	public List<DBank> getListDBank() {
		return listDBank;
	}

	public void setListDBank(List<DBank> listDBank) {
		this.listDBank = listDBank;
	}

	public TBank getTbank() {
		return tbank;
	}

	public void setTbank(TBank tbank) {
		this.tbank = tbank;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getGiro() {
		return giro;
	}

	public void setGiro(String giro) {
		this.giro = giro;
	}

	public Double getJumlah() {
		return jumlah;
	}

	public void setJumlah(Double jumlah) {
		this.jumlah = jumlah;
	}

	public String getKas() {
		return kas;
	}

	public void setKas(String kas) {
		this.kas = kas;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public String getKey_jurnal() {
		return key_jurnal;
	}

	public void setKey_jurnal(String key_jurnal) {
		this.key_jurnal = key_jurnal;
	}

	public Integer getKode_cash_flow() {
		return kode_cash_flow;
	}

	public void setKode_cash_flow(Integer kode_cash_flow) {
		this.kode_cash_flow = kode_cash_flow;
	}

	public Integer getNo_jurnal() {
		return no_jurnal;
	}

	public void setNo_jurnal(Integer no_jurnal) {
		this.no_jurnal = no_jurnal;
	}

	public String getNo_pre() {
		return no_pre;
	}

	public void setNo_pre(String no_pre) {
		this.no_pre = no_pre;
	}

	public Integer getType_trx() {
		return type_trx;
	}

	public void setType_trx(Integer type_trx) {
		this.type_trx = type_trx;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public DBank(String kas, String value) {
		this.kas = kas;
		this.value = value;
	}

	public DBank() {
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public void setListKas(List<DropDown> listKas) {
		this.listKas = listKas;
	}

	public List<DropDown> getListKas() {
		return listKas;
	}

	public String getLus_login_name() {
		return lus_login_name;
	}

	public void setLus_login_name(String lus_login_name) {
		this.lus_login_name = lus_login_name;
	}

	public Date getTgl_input() {
		return tgl_input;
	}

	public void setTgl_input(Date tgl_input) {
		this.tgl_input = tgl_input;
	}

	public String getLus_id() {
		return lus_id;
	}

	public void setLus_id(String lus_id) {
		this.lus_id = lus_id;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getDebet() {
		return debet;
	}

	public void setDebet(Double debet) {
		this.debet = debet;
	}

	public Double getKredit() {
		return kredit;
	}

	public void setKredit(Double kredit) {
		this.kredit = kredit;
	}

}