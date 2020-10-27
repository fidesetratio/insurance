package com.ekalife.elions.model;

import java.util.Date;

public class WelcomeCallStatus {
	
	private String reg_spaj;
	
	private Integer lsbs_id;
	
	private Integer lsdbs_number;
	
	
	private  Integer status_insert;
	
	private String status_message;
	
	private Date created_date;

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

	public Integer getStatus_insert() {
		return status_insert;
	}

	public void setStatus_insert(Integer status_insert) {
		this.status_insert = status_insert;
	}

	public String getStatus_message() {
		return status_message;
	}

	public void setStatus_message(String status_message) {
		this.status_message = status_message;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

}
