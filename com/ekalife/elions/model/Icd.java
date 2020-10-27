package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Icd implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;                       
	private Integer mpa_number;                    
	private String lic_id;    
	private String lic_desc;
	private Integer lic_type;                      
	private Integer active;  	
	private Integer msdi_lus_id;
	private String lus_full_name;
	private Date msdi_input_date;
	private Integer mste_insured_no; 
	private Integer cek;
	private String lic_category;
	
	public String getLic_id() {
		return lic_id;
	}
	public void setLic_id(String lic_id) {
		this.lic_id = lic_id;
	}
	public Integer getMpa_number() {
		return mpa_number;
	}
	public void setMpa_number(Integer mpa_number) {
		this.mpa_number = mpa_number;
	}
	public Date getMsdi_input_date() {
		return msdi_input_date;
	}
	public void setMsdi_input_date(Date msdi_input_date) {
		this.msdi_input_date = msdi_input_date;
	}
	public Integer getMsdi_lus_id() {
		return msdi_lus_id;
	}
	public void setMsdi_lus_id(Integer msdi_lus_id) {
		this.msdi_lus_id = msdi_lus_id;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getCek() {
		return cek;
	}
	public void setCek(Integer cek) {
		this.cek = cek;
	}
	public String getLus_full_name() {
		return lus_full_name;
	}
	public void setLus_full_name(String lus_full_name) {
		this.lus_full_name = lus_full_name;
	}
	public String getLic_desc() {
		return lic_desc;
	}
	public void setLic_desc(String lic_desc) {
		this.lic_desc = lic_desc;
	}
	public Integer getActive() {
		return active;
	}
	public void setActive(Integer active) {
		this.active = active;
	}
	public Integer getLic_type() {
		return lic_type;
	}
	public void setLic_type(Integer lic_type) {
		this.lic_type = lic_type;
	}
	public Integer getMste_insured_no() {
		return mste_insured_no;
	}
	public void setMste_insured_no(Integer mste_insured_no) {
		this.mste_insured_no = mste_insured_no;
	}
	public String getLic_category() {
		return lic_category;
	}
	public void setLic_category(String lic_category) {
		this.lic_category = lic_category;
	}
	
}
