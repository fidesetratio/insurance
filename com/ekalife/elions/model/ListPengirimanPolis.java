package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class ListPengirimanPolis implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -229218133176451082L;
	private String reg_spaj;                       
	private Integer lsbs_id; 
	private String lca_id;
	private Integer lsdbs_number;                  
	private String lsdbs_name;  
	private String mspo_policy_no;
	private String mspo_policy_no_format;          
	private String mcl_first;                      
	private Date mste_beg_date;                    
	private Date mste_tgl_kirim_polis;
	private Date msps_date;
	private Date mspo_date_ttp;                    
	private Date mste_tgl_komisi;                  
	private String softcopy;                       
	private String kirim_hh;                       
	private String kirim_mm;                       
	private String terima_hh;                      
	private String terima_mm;
	private Date mste_tgl_kirim_lb;
	private Date mste_tgl_terima_lb;
	private Date mste_tgl_aksep;
	private Integer lsgb_id;
	private Integer lsbs_jenis;
	private String status_accept;
	private Integer lssa_id;
	private String keterangan;
	
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public Integer getLssa_id() {
		return lssa_id;
	}
	public void setLssa_id(Integer lssa_id) {
		this.lssa_id = lssa_id;
	}
	public String getStatus_accept() {
		return status_accept;
	}
	public void setStatus_accept(String status_accept) {
		this.status_accept = status_accept;
	}
	public Integer getLsbs_jenis() {
		return lsbs_jenis;
	}
	public void setLsbs_jenis(Integer lsbs_jenis) {
		this.lsbs_jenis = lsbs_jenis;
	}
	public Integer getLsgb_id() {
		return lsgb_id;
	}
	public void setLsgb_id(Integer lsgb_id) {
		this.lsgb_id = lsgb_id;
	}
	public Date getMste_tgl_aksep() {
		return mste_tgl_aksep;
	}
	public void setMste_tgl_aksep(Date mste_tgl_aksep) {
		this.mste_tgl_aksep = mste_tgl_aksep;
	}
	public Date getMste_tgl_kirim_lb() {
		return mste_tgl_kirim_lb;
	}
	public void setMste_tgl_kirim_lb(Date mste_tgl_kirim_lb) {
		this.mste_tgl_kirim_lb = mste_tgl_kirim_lb;
	}
	public Date getMste_tgl_terima_lb() {
		return mste_tgl_terima_lb;
	}
	public void setMste_tgl_terima_lb(Date mste_tgl_terima_lb) {
		this.mste_tgl_terima_lb = mste_tgl_terima_lb;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}
	public String getMspo_policy_no_format() {
		return mspo_policy_no_format;
	}
	public void setMspo_policy_no_format(String mspo_policy_no_format) {
		this.mspo_policy_no_format = mspo_policy_no_format;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public Date getMste_beg_date() {
		return mste_beg_date;
	}
	public void setMste_beg_date(Date mste_beg_date) {
		this.mste_beg_date = mste_beg_date;
	}
	public Date getMste_tgl_kirim_polis() {
		return mste_tgl_kirim_polis;
	}
	public void setMste_tgl_kirim_polis(Date mste_tgl_kirim_polis) {
		this.mste_tgl_kirim_polis = mste_tgl_kirim_polis;
	}
	public Date getMspo_date_ttp() {
		return mspo_date_ttp;
	}
	public void setMspo_date_ttp(Date mspo_date_ttp) {
		this.mspo_date_ttp = mspo_date_ttp;
	}
	public Date getMste_tgl_komisi() {
		return mste_tgl_komisi;
	}
	public void setMste_tgl_komisi(Date mste_tgl_komisi) {
		this.mste_tgl_komisi = mste_tgl_komisi;
	}
	public String getSoftcopy() {
		return softcopy;
	}
	public void setSoftcopy(String softcopy) {
		this.softcopy = softcopy;
	}
	public String getKirim_hh() {
		return kirim_hh;
	}
	public void setKirim_hh(String kirim_hh) {
		this.kirim_hh = kirim_hh;
	}
	public String getKirim_mm() {
		return kirim_mm;
	}
	public void setKirim_mm(String kirim_mm) {
		this.kirim_mm = kirim_mm;
	}
	public String getTerima_hh() {
		return terima_hh;
	}
	public void setTerima_hh(String terima_hh) {
		this.terima_hh = terima_hh;
	}
	public String getTerima_mm() {
		return terima_mm;
	}
	public void setTerima_mm(String terima_mm) {
		this.terima_mm = terima_mm;
	}
	public Date getMsps_date() {
		return msps_date;
	}
	public void setMsps_date(Date msps_date) {
		this.msps_date = msps_date;
	}
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}                      

}
