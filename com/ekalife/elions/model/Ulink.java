package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Ulink implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;
	private Integer mu_ke;
	private String no_register;
	private String lspd_position;
	private Integer lt_id;
	private Date mu_tgl_trans;
	private Date mu_tgl_nab;
	private Double mu_jlh_premi;
	private Double mu_jlh_unit;
	private Date mu_tgl_surat;
	private Integer mu_bulan_surat;
	private Integer mu_periodic_tu;
	private Double mu_jlh_tu;
	private Integer mu_switch_cnt;
	private Date mu_last_switch;
	private Integer mu_wdraw_cnt;
	private Date mu_last_wdraw;
	private Integer mu_lspd_id;
	private Date mu_tgl_input;
	private Integer mu_aktif;
	private Integer mu_tahun_ke;
	private Integer mu_premi_ke;
	private Integer mu_flag_print;
	private Integer mu_lus_id;
	private Double total;
	
	public Ulink() {}
	
	public String getLspd_position() {
		return lspd_position;
	}
	public void setLspd_position(String lspd_position) {
		this.lspd_position = lspd_position;
	}
	public Integer getLt_id() {
		return lt_id;
	}
	public void setLt_id(Integer lt_id) {
		this.lt_id = lt_id;
	}
	public Integer getMu_aktif() {
		return mu_aktif;
	}
	public void setMu_aktif(Integer mu_aktif) {
		this.mu_aktif = mu_aktif;
	}
	public Integer getMu_bulan_surat() {
		return mu_bulan_surat;
	}
	public void setMu_bulan_surat(Integer mu_bulan_surat) {
		this.mu_bulan_surat = mu_bulan_surat;
	}
	public Integer getMu_flag_print() {
		return mu_flag_print;
	}
	public void setMu_flag_print(Integer mu_flag_print) {
		this.mu_flag_print = mu_flag_print;
	}
	public Double getMu_jlh_premi() {
		return mu_jlh_premi;
	}
	public void setMu_jlh_premi(Double mu_jlh_premi) {
		this.mu_jlh_premi = mu_jlh_premi;
	}
	public Double getMu_jlh_tu() {
		return mu_jlh_tu;
	}
	public void setMu_jlh_tu(Double mu_jlh_tu) {
		this.mu_jlh_tu = mu_jlh_tu;
	}
	public Double getMu_jlh_unit() {
		return mu_jlh_unit;
	}
	public void setMu_jlh_unit(Double mu_jlh_unit) {
		this.mu_jlh_unit = mu_jlh_unit;
	}
	public Integer getMu_ke() {
		return mu_ke;
	}
	public void setMu_ke(Integer mu_ke) {
		this.mu_ke = mu_ke;
	}
	public Date getMu_last_switch() {
		return mu_last_switch;
	}
	public void setMu_last_switch(Date mu_last_switch) {
		this.mu_last_switch = mu_last_switch;
	}
	public Date getMu_last_wdraw() {
		return mu_last_wdraw;
	}
	public void setMu_last_wdraw(Date mu_last_wdraw) {
		this.mu_last_wdraw = mu_last_wdraw;
	}
	public Integer getMu_lspd_id() {
		return mu_lspd_id;
	}
	public void setMu_lspd_id(Integer mu_lspd_id) {
		this.mu_lspd_id = mu_lspd_id;
	}
	public Integer getMu_lus_id() {
		return mu_lus_id;
	}
	public void setMu_lus_id(Integer mu_lus_id) {
		this.mu_lus_id = mu_lus_id;
	}
	public Integer getMu_periodic_tu() {
		return mu_periodic_tu;
	}
	public void setMu_periodic_tu(Integer mu_periodic_tu) {
		this.mu_periodic_tu = mu_periodic_tu;
	}
	public Integer getMu_premi_ke() {
		return mu_premi_ke;
	}
	public void setMu_premi_ke(Integer mu_premi_ke) {
		this.mu_premi_ke = mu_premi_ke;
	}
	public Integer getMu_switch_cnt() {
		return mu_switch_cnt;
	}
	public void setMu_switch_cnt(Integer mu_switch_cnt) {
		this.mu_switch_cnt = mu_switch_cnt;
	}
	public Integer getMu_tahun_ke() {
		return mu_tahun_ke;
	}
	public void setMu_tahun_ke(Integer mu_tahun_ke) {
		this.mu_tahun_ke = mu_tahun_ke;
	}
	public Date getMu_tgl_input() {
		return mu_tgl_input;
	}
	public void setMu_tgl_input(Date mu_tgl_input) {
		this.mu_tgl_input = mu_tgl_input;
	}
	public Date getMu_tgl_nab() {
		return mu_tgl_nab;
	}
	public void setMu_tgl_nab(Date mu_tgl_nab) {
		this.mu_tgl_nab = mu_tgl_nab;
	}
	public Date getMu_tgl_surat() {
		return mu_tgl_surat;
	}
	public void setMu_tgl_surat(Date mu_tgl_surat) {
		this.mu_tgl_surat = mu_tgl_surat;
	}
	public Date getMu_tgl_trans() {
		return mu_tgl_trans;
	}
	public void setMu_tgl_trans(Date mu_tgl_trans) {
		this.mu_tgl_trans = mu_tgl_trans;
	}
	public Integer getMu_wdraw_cnt() {
		return mu_wdraw_cnt;
	}
	public void setMu_wdraw_cnt(Integer mu_wdraw_cnt) {
		this.mu_wdraw_cnt = mu_wdraw_cnt;
	}
	public String getNo_register() {
		return no_register;
	}
	public void setNo_register(String no_register) {
		this.no_register = no_register;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	
	
	
}
