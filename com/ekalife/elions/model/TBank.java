package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class TBank implements Serializable {

	private static final long serialVersionUID = 1L;
	

	public String no_pre;

	public Integer position;

	public Date tgl_input;

	public Date tgl_jurnal;

	public Date tgl_rk;

	public String no_voucher;

	public String kas;

	public Double jumlah;

	public Double saldo_kas;

	public String print_voucher;

	public String lus_id;

	public String mtb_gl_no;

	public Integer lus_id_trans;

	public Integer lus_id_void;

	public Date tgl_trans_pending;

	public Integer print_pending;

	public String lus_id_add;

	public Integer mtb_manual;

	public Integer titipan_gantung;

	public String no_pre_old;

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

	public String getLus_id() {
		return lus_id;
	}

	public void setLus_id(String lus_id) {
		this.lus_id = lus_id;
	}

	public String getLus_id_add() {
		return lus_id_add;
	}

	public void setLus_id_add(String lus_id_add) {
		this.lus_id_add = lus_id_add;
	}

	public Integer getLus_id_trans() {
		return lus_id_trans;
	}

	public void setLus_id_trans(Integer lus_id_trans) {
		this.lus_id_trans = lus_id_trans;
	}

	public Integer getLus_id_void() {
		return lus_id_void;
	}

	public void setLus_id_void(Integer lus_id_void) {
		this.lus_id_void = lus_id_void;
	}

	public String getMtb_gl_no() {
		return mtb_gl_no;
	}

	public void setMtb_gl_no(String mtb_gl_no) {
		this.mtb_gl_no = mtb_gl_no;
	}

	public Integer getMtb_manual() {
		return mtb_manual;
	}

	public void setMtb_manual(Integer mtb_manual) {
		this.mtb_manual = mtb_manual;
	}

	public String getNo_pre() {
		return no_pre;
	}

	public void setNo_pre(String no_pre) {
		this.no_pre = no_pre;
	}

	public String getNo_pre_old() {
		return no_pre_old;
	}

	public void setNo_pre_old(String no_pre_old) {
		this.no_pre_old = no_pre_old;
	}

	public String getNo_voucher() {
		return no_voucher;
	}

	public void setNo_voucher(String no_voucher) {
		this.no_voucher = no_voucher;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Integer getPrint_pending() {
		return print_pending;
	}

	public void setPrint_pending(Integer print_pending) {
		this.print_pending = print_pending;
	}

	public String getPrint_voucher() {
		return print_voucher;
	}

	public void setPrint_voucher(String print_voucher) {
		this.print_voucher = print_voucher;
	}

	public Double getSaldo_kas() {
		return saldo_kas;
	}

	public void setSaldo_kas(Double saldo_kas) {
		this.saldo_kas = saldo_kas;
	}

	public Date getTgl_input() {
		return tgl_input;
	}

	public void setTgl_input(Date tgl_input) {
		this.tgl_input = tgl_input;
	}

	public Date getTgl_jurnal() {
		return tgl_jurnal;
	}

	public void setTgl_jurnal(Date tgl_jurnal) {
		this.tgl_jurnal = tgl_jurnal;
	}

	public Date getTgl_rk() {
		return tgl_rk;
	}

	public void setTgl_rk(Date tgl_rk) {
		this.tgl_rk = tgl_rk;
	}

	public Date getTgl_trans_pending() {
		return tgl_trans_pending;
	}

	public void setTgl_trans_pending(Date tgl_trans_pending) {
		this.tgl_trans_pending = tgl_trans_pending;
	}

	public Integer getTitipan_gantung() {
		return titipan_gantung;
	}

	public void setTitipan_gantung(Integer titipan_gantung) {
		this.titipan_gantung = titipan_gantung;
	}

}