package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Insured implements Serializable {

	private static final long serialVersionUID = 6185413738350911046L;

	private Integer mste_insured_no;

	private String mste_insured;

	private Integer mste_age;

	private Integer mste_medical;

	private Integer mste_standard;

	private Integer lspd_id;

	private Integer lus_id;

	private Double mste_card_cost;

	private Date mste_beg_date;

	private Date mste_end_date;

	private Date mste_input_date;

	private Integer mste_backup;

	private Integer lssa_id;

	private Integer mste_reas;

	private Integer mste_active;

	private Integer mste_dewasa;

	private Integer mste_flag_cc;

	private Date mste_tgl_recur;

	private Integer mste_flag_recur;

	private String mste_no_reg;

	private Date mste_tgl_batal;

	private Integer mste_pre_exiting;

	private Date mste_tgl_aksep;

	private Date mste_tgl_spaj;

	private String no_invoice;

	private Integer mste_flag_ubah;

	private Double mste_pct_dplk;

	private String reg_spaj;
	
	private Integer flag_speedy;

	private Integer jns_medis;
	private Integer flag_approve_uw;
	private Integer flag_approve_collection;

	public Integer getFlag_approve_uw() {
		return flag_approve_uw;
	}

	public void setFlag_approve_uw(Integer flag_approve_uw) {
		this.flag_approve_uw = flag_approve_uw;
	}

	public Integer getFlag_approve_collection() {
		return flag_approve_collection;
	}

	public void setFlag_approve_collection(Integer flag_approve_collection) {
		this.flag_approve_collection = flag_approve_collection;
	}

	public Insured() {
	}
	
	public Insured(String reg_spaj, Integer mste_insured_no, Integer lspd_id, String mste_insured, Integer lus_id, Date mste_input_date) {
		this.reg_spaj = reg_spaj;
		this.mste_insured_no = mste_insured_no;
		this.lspd_id = lspd_id;
		this.mste_insured = mste_insured;
		this.lus_id = lus_id;
		this.mste_input_date = mste_input_date;
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

	public Integer getLus_id() {
		return lus_id;
	}

	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}

	public Integer getMste_active() {
		return mste_active;
	}

	public void setMste_active(Integer mste_active) {
		this.mste_active = mste_active;
	}

	public Integer getMste_age() {
		return mste_age;
	}

	public void setMste_age(Integer mste_age) {
		this.mste_age = mste_age;
	}

	public Integer getMste_backup() {
		return mste_backup;
	}

	public void setMste_backup(Integer mste_backup) {
		this.mste_backup = mste_backup;
	}

	public Date getMste_beg_date() {
		return mste_beg_date;
	}

	public void setMste_beg_date(Date mste_beg_date) {
		this.mste_beg_date = mste_beg_date;
	}

	public Double getMste_card_cost() {
		return mste_card_cost;
	}

	public void setMste_card_cost(Double mste_card_cost) {
		this.mste_card_cost = mste_card_cost;
	}

	public Integer getMste_dewasa() {
		return mste_dewasa;
	}

	public void setMste_dewasa(Integer mste_dewasa) {
		this.mste_dewasa = mste_dewasa;
	}

	public Date getMste_end_date() {
		return mste_end_date;
	}

	public void setMste_end_date(Date mste_end_date) {
		this.mste_end_date = mste_end_date;
	}

	public Integer getMste_flag_cc() {
		return mste_flag_cc;
	}

	public void setMste_flag_cc(Integer mste_flag_cc) {
		this.mste_flag_cc = mste_flag_cc;
	}

	public Integer getMste_flag_recur() {
		return mste_flag_recur;
	}

	public void setMste_flag_recur(Integer mste_flag_recur) {
		this.mste_flag_recur = mste_flag_recur;
	}

	public Integer getMste_flag_ubah() {
		return mste_flag_ubah;
	}

	public void setMste_flag_ubah(Integer mste_flag_ubah) {
		this.mste_flag_ubah = mste_flag_ubah;
	}

	public Date getMste_input_date() {
		return mste_input_date;
	}

	public void setMste_input_date(Date mste_input_date) {
		this.mste_input_date = mste_input_date;
	}

	public String getMste_insured() {
		return mste_insured;
	}

	public void setMste_insured(String mste_insured) {
		this.mste_insured = mste_insured;
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

	public String getMste_no_reg() {
		return mste_no_reg;
	}

	public void setMste_no_reg(String mste_no_reg) {
		this.mste_no_reg = mste_no_reg;
	}

	public Double getMste_pct_dplk() {
		return mste_pct_dplk;
	}

	public void setMste_pct_dplk(Double mste_pct_dplk) {
		this.mste_pct_dplk = mste_pct_dplk;
	}

	public Integer getMste_pre_exiting() {
		return mste_pre_exiting;
	}

	public void setMste_pre_exiting(Integer mste_pre_exiting) {
		this.mste_pre_exiting = mste_pre_exiting;
	}

	public Integer getMste_reas() {
		return mste_reas;
	}

	public void setMste_reas(Integer mste_reas) {
		this.mste_reas = mste_reas;
	}

	public Integer getMste_standard() {
		return mste_standard;
	}

	public void setMste_standard(Integer mste_standard) {
		this.mste_standard = mste_standard;
	}

	public Date getMste_tgl_aksep() {
		return mste_tgl_aksep;
	}

	public void setMste_tgl_aksep(Date mste_tgl_aksep) {
		this.mste_tgl_aksep = mste_tgl_aksep;
	}

	public Date getMste_tgl_batal() {
		return mste_tgl_batal;
	}

	public void setMste_tgl_batal(Date mste_tgl_batal) {
		this.mste_tgl_batal = mste_tgl_batal;
	}

	public Date getMste_tgl_recur() {
		return mste_tgl_recur;
	}

	public void setMste_tgl_recur(Date mste_tgl_recur) {
		this.mste_tgl_recur = mste_tgl_recur;
	}

	public Date getMste_tgl_spaj() {
		return mste_tgl_spaj;
	}

	public void setMste_tgl_spaj(Date mste_tgl_spaj) {
		this.mste_tgl_spaj = mste_tgl_spaj;
	}

	public String getNo_invoice() {
		return no_invoice;
	}

	public void setNo_invoice(String no_invoice) {
		this.no_invoice = no_invoice;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	
	public Integer getFlag_speedy() {
		return flag_speedy;
	}

	public void setFlag_speedy(Integer flag_speedy) {
		this.flag_speedy = flag_speedy;
	}

	public Integer getJns_medis() {
		return jns_medis;
	}

	public void setJns_medis(Integer jns_medis) {
		this.jns_medis = jns_medis;
	}


}
