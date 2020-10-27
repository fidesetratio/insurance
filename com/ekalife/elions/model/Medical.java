package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Medical implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;
	private Integer mste_insured_no;
	private Integer mpa_number;
	private Integer lsmc_id;
	private Integer lsmc_id_old;
	private Integer msdm_status;
	private String msdm_desc;
	private String lsmc_name;
	private Integer cek;
	
	// table bentuk baru... permintaan dr.Irene (Yusup_A)
	private Integer msdm_lus_id;
	private String lus_full_name;
	private Date msdm_input_date;
	
	public Integer getCek() {
		return cek;
	}
	public void setCek(Integer cek) {
		this.cek = cek;
	}
	public String getLsmc_name() {
		return lsmc_name;
	}
	public void setLsmc_name(String lsmc_name) {
		this.lsmc_name = lsmc_name;
	}
	public Integer getLsmc_id() {
		return lsmc_id;
	}
	public void setLsmc_id(Integer lsmc_id) {
		this.lsmc_id = lsmc_id;
	}
	public Integer getMpa_number() {
		return mpa_number;
	}
	public void setMpa_number(Integer mpa_number) {
		this.mpa_number = mpa_number;
	}
	public String getMsdm_desc() {
		return msdm_desc;
	}
	public void setMsdm_desc(String msdm_desc) {
		this.msdm_desc = msdm_desc;
	}
	public Integer getMsdm_status() {
		return msdm_status;
	}
	public void setMsdm_status(Integer msdm_status) {
		this.msdm_status = msdm_status;
	}
	public Integer getMste_insured_no() {
		return mste_insured_no;
	}
	public void setMste_insured_no(Integer mste_insured_no) {
		this.mste_insured_no = mste_insured_no;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getLsmc_id_old() {
		return lsmc_id_old;
	}
	public void setLsmc_id_old(Integer lsmc_id_old) {
		this.lsmc_id_old = lsmc_id_old;
	}
	public Date getMsdm_input_date() {
		return msdm_input_date;
	}
	public void setMsdm_input_date(Date msdm_input_date) {
		this.msdm_input_date = msdm_input_date;
	}
	public Integer getMsdm_lus_id() {
		return msdm_lus_id;
	}
	public void setMsdm_lus_id(Integer msdm_lus_id) {
		this.msdm_lus_id = msdm_lus_id;
	}
	public String getLus_full_name() {
		return lus_full_name;
	}
	public void setLus_full_name(String lus_full_name) {
		this.lus_full_name = lus_full_name;
	}
	
}
