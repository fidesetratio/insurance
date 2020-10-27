package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class FormHist implements Serializable {

	private static final long serialVersionUID = 7050701971741846124L;
	private String msf_id;
	private Integer msf_urut;
	private Integer posisi;
	private Integer msfh_lus_id;
	private Date msfh_dt;
	private String msfh_desc;
	private String lus_login_name;
	private String status_form;
	
	public String getStatus_form() {
		return status_form;
	}
	public void setStatus_form(String status_form) {
		this.status_form = status_form;
	}
	public String getLus_login_name() {
		return lus_login_name;
	}
	public void setLus_login_name(String lus_login_name) {
		this.lus_login_name = lus_login_name;
	}
	public String getMsf_id() {
		return msf_id;
	}
	public void setMsf_id(String msf_id) {
		this.msf_id = msf_id;
	}
	public Integer getMsf_urut() {
		return msf_urut;
	}
	public void setMsf_urut(Integer msf_urut) {
		this.msf_urut = msf_urut;
	}
	public String getMsfh_desc() {
		return msfh_desc;
	}
	public void setMsfh_desc(String msfh_desc) {
		this.msfh_desc = msfh_desc;
	}
	public Date getMsfh_dt() {
		return msfh_dt;
	}
	public void setMsfh_dt(Date msfh_dt) {
		this.msfh_dt = msfh_dt;
	}
	public Integer getMsfh_lus_id() {
		return msfh_lus_id;
	}
	public void setMsfh_lus_id(Integer msfh_lus_id) {
		this.msfh_lus_id = msfh_lus_id;
	}
	public Integer getPosisi() {
		return posisi;
	}
	public void setPosisi(Integer posisi) {
		this.posisi = posisi;
	}
	
}