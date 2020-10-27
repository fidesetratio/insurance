package com.ekalife.elions.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DanamasPrima implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2032926849885830566L;
	private String reg_spaj;
	private String mspo_policy_no;
	private String pemegang;
	private String kurs;
	private Double uang_masuk;
	private Double pokok;
	private Double bunga;
	private Double total;
	private Date tgl_rk;
	private Date tgl_bayar;
	private String no_voucher;
	private String no_reg;
	private String no_pre;
	private Date tgl_aksep;
	private Double premi;
	private String no_jm;
	
	
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
	public String getPemegang() {
		return pemegang;
	}
	public void setPemegang(String pemegang) {
		this.pemegang = pemegang;
	}
	public String getKurs() {
		return kurs;
	}
	public void setKurs(String kurs) {
		this.kurs = kurs;
	}
	public Double getUang_masuk() {
		return uang_masuk;
	}
	public void setUang_masuk(Double uangMasuk) {
		uang_masuk = uangMasuk;
	}
	public Double getPokok() {
		return pokok;
	}
	public void setPokok(Double pokok) {
		this.pokok = pokok;
	}
	public Double getBunga() {
		return bunga;
	}
	public void setBunga(Double bunga) {
		this.bunga = bunga;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Date getTgl_rk() {
		return tgl_rk;
	}
	public void setTgl_rk(Date tglRk) {
		tgl_rk = tglRk;
	}
	public Date getTgl_bayar() {
		return tgl_bayar;
	}
	public void setTgl_bayar(Date tglBayar) {
		tgl_bayar = tglBayar;
	}
	public String getNo_voucher() {
		return no_voucher;
	}
	public void setNo_voucher(String noVoucher) {
		no_voucher = noVoucher;
	}
	public String getNo_reg() {
		return no_reg;
	}
	public void setNo_reg(String noReg) {
		no_reg = noReg;
	}
	public String getNo_pre() {
		return no_pre;
	}
	public void setNo_pre(String noPre) {
		no_pre = noPre;
	}
	public Date getTgl_aksep() {
		return tgl_aksep;
	}
	public void setTgl_aksep(Date tglAksep) {
		tgl_aksep = tglAksep;
	}
	public Double getPremi() {
		return premi;
	}
	public void setPremi(Double premi) {
		this.premi = premi;
	}
	public String getNo_jm() {
		return no_jm;
	}
	public void setNo_jm(String noJm) {
		no_jm = noJm;
	}
	
}
