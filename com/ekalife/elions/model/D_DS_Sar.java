package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class D_DS_Sar implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer lssp_id;
	private Integer lspd_id;
	private Integer mspo_age;
	private String lku_id;
	private Integer lscb_id;
	private String mspo_policy_no;
	private Integer mspo_ins_period;
	private Integer mspo_pay_period;
	private Integer lsbs_id;
	private Integer lsdbs_number;
	private Double mspr_tsi;
	private Double mspr_tsi_pa_a;
	private Integer mspr_unit;
	private Double mspr_rate;
	private Double mspr_extra;
	private String reg_spaj;
	private String reg_spaj_ref;
	private Integer mste_insured_no;
	private Integer mssm_pemegang;
	private Integer mste_medical;
	private Date mste_beg_date;
	private Integer lssa_id;
	private Integer lstb_id;
	private Integer lstr_id;
	private Double lstr_quota_reas;
	private Integer lsdbs_reas_client;
	private Integer mspo_ins_bulan;
	private Double mspr_bunga_kpr;
	private Integer mspo_flat;
	private Integer lsgb_id;  
	private Integer lsgr_id;//1=life;	2=pa; 3=health; 4=rider; 5=extra
	private Integer lssr_id; //1=surplus; 2=quota share; 3=quota share , surplus
	private Double retensi_life;
	private Double retensi_tr_rd;
	private Double retensi_ssp;
	private Double retensi_ssh;
	public Double getRetensi_life() {
		return retensi_life;
	}
	public void setRetensi_life(Double retensi_life) {
		this.retensi_life = retensi_life;
	}
	public Double getRetensi_ssh() {
		return retensi_ssh;
	}
	public void setRetensi_ssh(Double retensi_ssh) {
		this.retensi_ssh = retensi_ssh;
	}
	public Double getRetensi_ssp() {
		return retensi_ssp;
	}
	public void setRetensi_ssp(Double retensi_ssp) {
		this.retensi_ssp = retensi_ssp;
	}
	public Double getRetensi_tr_rd() {
		return retensi_tr_rd;
	}
	public void setRetensi_tr_rd(Double retensi_tr_rd) {
		this.retensi_tr_rd = retensi_tr_rd;
	}
	public Integer getLsgb_id() {
		return lsgb_id;
	}
	public void setLsgb_id(Integer lsgb_id) {
		this.lsgb_id = lsgb_id;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public Integer getLscb_id() {
		return lscb_id;
	}
	public void setLscb_id(Integer lscb_id) {
		this.lscb_id = lscb_id;
	}
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	public Integer getLsdbs_reas_client() {
		return lsdbs_reas_client;
	}
	public void setLsdbs_reas_client(Integer lsdbs_reas_client) {
		this.lsdbs_reas_client = lsdbs_reas_client;
	}
	public Integer getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}
	public Integer getLssa_id() {
		return lssa_id;
	}
	public void setLssa_id(Integer lssa_id) {
		this.lssa_id = lssa_id;
	}
	public Integer getLssp_id() {
		return lssp_id;
	}
	public void setLssp_id(Integer lssp_id) {
		this.lssp_id = lssp_id;
	}
	public Integer getLstb_id() {
		return lstb_id;
	}
	public void setLstb_id(Integer lstb_id) {
		this.lstb_id = lstb_id;
	}
	public Integer getLstr_id() {
		return lstr_id;
	}
	public void setLstr_id(Integer lstr_id) {
		this.lstr_id = lstr_id;
	}
	public Double getLstr_quota_reas() {
		return lstr_quota_reas;
	}
	public void setLstr_quota_reas(Double lstr_quota_reas) {
		this.lstr_quota_reas = lstr_quota_reas;
	}
	public Integer getMspo_age() {
		return mspo_age;
	}
	public void setMspo_age(Integer mspo_age) {
		this.mspo_age = mspo_age;
	}
	public Integer getMspo_flat() {
		return mspo_flat;
	}
	public void setMspo_flat(Integer mspo_flat) {
		this.mspo_flat = mspo_flat;
	}
	public Integer getMspo_ins_bulan() {
		return mspo_ins_bulan;
	}
	public void setMspo_ins_bulan(Integer mspo_ins_bulan) {
		this.mspo_ins_bulan = mspo_ins_bulan;
	}
	public Integer getMspo_ins_period() {
		return mspo_ins_period;
	}
	public void setMspo_ins_period(Integer mspo_ins_period) {
		this.mspo_ins_period = mspo_ins_period;
	}
	public Integer getMspo_pay_period() {
		return mspo_pay_period;
	}
	public void setMspo_pay_period(Integer mspo_pay_period) {
		this.mspo_pay_period = mspo_pay_period;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public Double getMspr_bunga_kpr() {
		return mspr_bunga_kpr;
	}
	public void setMspr_bunga_kpr(Double mspr_bunga_kpr) {
		this.mspr_bunga_kpr = mspr_bunga_kpr;
	}
	public Double getMspr_tsi() {
		return mspr_tsi;
	}
	public void setMspr_tsi(Double mspr_tsi) {
		this.mspr_tsi = mspr_tsi;
	}
	public Double getMspr_tsi_pa_a() {
		return mspr_tsi_pa_a;
	}
	public void setMspr_tsi_pa_a(Double mspr_tsi_pa_a) {
		this.mspr_tsi_pa_a = mspr_tsi_pa_a;
	}
	public Integer getMspr_unit() {
		return mspr_unit;
	}
	public void setMspr_unit(Integer mspr_unit) {
		this.mspr_unit = mspr_unit;
	}
	public Integer getMssm_pemegang() {
		return mssm_pemegang;
	}
	public void setMssm_pemegang(Integer mssm_pemegang) {
		this.mssm_pemegang = mssm_pemegang;
	}
	public Date getMste_beg_date() {
		return mste_beg_date;
	}
	public void setMste_beg_date(Date mste_beg_date) {
		this.mste_beg_date = mste_beg_date;
	}
	public Integer getMste_insured_no() {
		return mste_insured_no;
	}
	public void setMste_insured_no(Integer mste_insured_no) {
		this.mste_insured_no = mste_insured_no;
	}
	public Integer getMste_medical() {
		return mste_medical;
	}
	public void setMste_medical(Integer mste_medical) {
		this.mste_medical = mste_medical;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Double getMspr_extra() {
		return mspr_extra;
	}
	public void setMspr_extra(Double mspr_extra) {
		this.mspr_extra = mspr_extra;
	}
	public Double getMspr_rate() {
		return mspr_rate;
	}
	public void setMspr_rate(Double mspr_rate) {
		this.mspr_rate = mspr_rate;
	}
	public String getReg_spaj_ref() {
		return reg_spaj_ref;
	}
	public void setReg_spaj_ref(String reg_spaj_ref) {
		this.reg_spaj_ref = reg_spaj_ref;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public Integer getLsgr_id() {
		return lsgr_id;
	}
	public void setLsgr_id(Integer lsgr_id) {
		this.lsgr_id = lsgr_id;
	}
	public Integer getLssr_id() {
		return lssr_id;
	}
	public void setLssr_id(Integer lssr_id) {
		this.lssr_id = lssr_id;
	}
	
	
	
}
