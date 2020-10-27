package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Edit implements Serializable{

	private static final long serialVersionUID = 5293012506445279319L;
	private Date msur_tgl_batas_paid;
	private Date msur_tanggal_acc;
    private Double msur_total_unbayar;
    private Double msur_total_bunga_unbayar;
    private Integer msur_accept;
    private String lku_id;
    private Double msur_total;
    private Integer msur_kondisi_polis;
    private String msur_accept_note;
    private String msur_kondisi_note;
    private String nomor;
    private String reg_spaj;
    private String msrt_reinstate_no;
    
	public String getMsrt_reinstate_no() {
		return msrt_reinstate_no;
	}
	public void setMsrt_reinstate_no(String msrt_reinstate_no) {
		this.msrt_reinstate_no = msrt_reinstate_no;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getMsur_kondisi_note() {
		return msur_kondisi_note;
	}
	public void setMsur_kondisi_note(String msur_kondisi_note) {
		this.msur_kondisi_note = msur_kondisi_note;
	}
	public String getMsur_accept_note() {
		return msur_accept_note;
	}
	public void setMsur_accept_note(String msur_accept_note) {
		this.msur_accept_note = msur_accept_note;
	}
	public Integer getMsur_kondisi_polis() {
		return msur_kondisi_polis;
	}
	public void setMsur_kondisi_polis(Integer msur_kondisi_polis) {
		this.msur_kondisi_polis = msur_kondisi_polis;
	}
	public Double getMsur_total() {
		return msur_total;
	}
	public void setMsur_total(Double msur_total) {
		this.msur_total = msur_total;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Integer getMsur_accept() {
		return msur_accept;
	}
	public void setMsur_accept(Integer msur_accept) {
		this.msur_accept = msur_accept;
	}
	public Date getMsur_tgl_batas_paid() {
		return msur_tgl_batas_paid;
	}
	public void setMsur_tgl_batas_paid(Date msur_tgl_batas_paid) {
		this.msur_tgl_batas_paid = msur_tgl_batas_paid;
	}
	public Double getMsur_total_bunga_unbayar() {
		return msur_total_bunga_unbayar;
	}
	public void setMsur_total_bunga_unbayar(Double msur_total_bunga_unbayar) {
		this.msur_total_bunga_unbayar = msur_total_bunga_unbayar;
	}
	public Double getMsur_total_unbayar() {
		return msur_total_unbayar;
	}
	public void setMsur_total_unbayar(Double msur_total_unbayar) {
		this.msur_total_unbayar = msur_total_unbayar;
	}
	public Date getMsur_tanggal_acc() {
		return msur_tanggal_acc;
	}
	public void setMsur_tanggal_acc(Date msur_tanggal_acc) {
		this.msur_tanggal_acc = msur_tanggal_acc;
	}
	public String getNomor() {
		return nomor;
	}
	public void setNomor(String nomor) {
		this.nomor = nomor;
	}
	
}
