package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class SummaryStatus implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Date mste_input_date;
	private String nama_agen;
	private String reg_spaj;
	private String mspo_policy_no;
	private String lsdbs_name;
	private String nama_tt;
	private String nama_pp;
	private String lku_id;
	private Double mspr_tsi;
	private Integer lssa_id;
	private String status_accept;
	private Integer lspd_id;
	private String lspd_position;
	private Integer lus_id;
	private String msps_desc;
	private String ket_aksep;
	private Integer lca_id;
	private String lca_nama;
	public String getKet_aksep() {
		return ket_aksep;
	}
	public void setKet_aksep(String ket_aksep) {
		this.ket_aksep = ket_aksep;
	}
	public Integer getLca_id() {
		return lca_id;
	}
	public void setLca_id(Integer lca_id) {
		this.lca_id = lca_id;
	}
	public String getLca_nama() {
		return lca_nama;
	}
	public void setLca_nama(String lca_nama) {
		this.lca_nama = lca_nama;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}
	public Integer getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}
	public String getLspd_position() {
		return lspd_position;
	}
	public void setLspd_position(String lspd_position) {
		this.lspd_position = lspd_position;
	}
	public Integer getLssa_id() {
		return lssa_id;
	}
	public void setLssa_id(Integer lssa_id) {
		this.lssa_id = lssa_id;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public Double getMspr_tsi() {
		return mspr_tsi;
	}
	public void setMspr_tsi(Double mspr_tsi) {
		this.mspr_tsi = mspr_tsi;
	}
	public String getMsps_desc() {
		return msps_desc;
	}
	public void setMsps_desc(String msps_desc) {
		this.msps_desc = msps_desc;
	}
	public Date getMste_input_date() {
		return mste_input_date;
	}
	public void setMste_input_date(Date mste_input_date) {
		this.mste_input_date = mste_input_date;
	}
	public String getNama_agen() {
		return nama_agen;
	}
	public void setNama_agen(String nama_agen) {
		this.nama_agen = nama_agen;
	}
	public String getNama_pp() {
		return nama_pp;
	}
	public void setNama_pp(String nama_pp) {
		this.nama_pp = nama_pp;
	}
	public String getNama_tt() {
		return nama_tt;
	}
	public void setNama_tt(String nama_tt) {
		this.nama_tt = nama_tt;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getStatus_accept() {
		return status_accept;
	}
	public void setStatus_accept(String status_accept) {
		this.status_accept = status_accept;
	}
	
}
