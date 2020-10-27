package com.ekalife.elions.model;

import java.io.Serializable;

public class HslReas implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;                       
	private Integer mste_insured_no;               
	private Integer mpa_number;                    
	private Integer lsrei_id;                      
	private String msdhr_keputusan;                
	private String msdhr_alasan;                   
	private String lus_full_name;
	private Integer msdhr_lus_id;                  
	private String msdhr_input_date;
	private Integer cek;
	private Integer msdhr_em; 
	private Integer msdhr_expired_day;
	private String msdhr_expired_date;
	private String msdhr_catatan;
	
	public Integer getCek() {
		return cek;
	}
	public void setCek(Integer cek) {
		this.cek = cek;
	}
	public Integer getLsrei_id() {
		return lsrei_id;
	}
	public void setLsrei_id(Integer lsrei_id) {
		this.lsrei_id = lsrei_id;
	}
	public Integer getMpa_number() {
		return mpa_number;
	}
	public void setMpa_number(Integer mpa_number) {
		this.mpa_number = mpa_number;
	}
	public String getMsdhr_alasan() {
		return msdhr_alasan;
	}
	public void setMsdhr_alasan(String msdhr_alasan) {
		this.msdhr_alasan = msdhr_alasan;
	}
	public String getMsdhr_keputusan() {
		return msdhr_keputusan;
	}
	public void setMsdhr_keputusan(String msdhr_keputusan) {
		this.msdhr_keputusan = msdhr_keputusan;
	}
	public String getMsdhr_input_date() {
		return msdhr_input_date;
	}
	public void setMsdhr_input_date(String msdhr_input_date) {
		this.msdhr_input_date = msdhr_input_date;
	}
	public Integer getMsdhr_lus_id() {
		return msdhr_lus_id;
	}
	public void setMsdhr_lus_id(Integer msdhr_lus_id) {
		this.msdhr_lus_id = msdhr_lus_id;
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
	public String getLus_full_name() {
		return lus_full_name;
	}
	public void setLus_full_name(String lus_full_name) {
		this.lus_full_name = lus_full_name;
	}
	public Integer getMsdhr_em() {
		return msdhr_em;
	}
	public void setMsdhr_em(Integer msdhr_em) {
		this.msdhr_em = msdhr_em;
	}
	public Integer getMsdhr_expired_day() {
		return msdhr_expired_day;
	}
	public void setMsdhr_expired_day(Integer msdhr_expired_day) {
		this.msdhr_expired_day = msdhr_expired_day;
	}
	public String getMsdhr_catatan() {
		return msdhr_catatan;
	}
	public void setMsdhr_catatan(String msdhr_catatan) {
		this.msdhr_catatan = msdhr_catatan;
	}
	public String getMsdhr_expired_date() {
		return msdhr_expired_date;
	}
	public void setMsdhr_expired_date(String msdhr_expired_date) {
		this.msdhr_expired_date = msdhr_expired_date;
	}
	
}
