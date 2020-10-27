package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ekalife.utils.Common;

public class OfacSertifikat implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String sdn_name;
	private String address;
	private String country;
	private String mofs_status_message;
	private String mofs_type;
	private String mofs_id;
	private String reg_spaj;
	private Integer mofr_id;
	private Integer add_num;
	private Integer type_data;
	private Integer mofs_status;
	
	public Integer getMofs_status() {
		return mofs_status;
	}

	public void setMofs_status(Integer mofs_status) {
		this.mofs_status = mofs_status;
	}

	public Integer getType_data() {
		return type_data;
	}

	public void setType_data(Integer type_data) {
		this.type_data = type_data;
	}

	public Integer getAdd_num() {
		return add_num;
	}

	public void setAdd_num(Integer add_num) {
		this.add_num = add_num;
	}

	public Integer getMofr_id() {
		return mofr_id;
	}

	public void setMofr_id(Integer mofr_id) {
		this.mofr_id = mofr_id;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public String getMofs_type() {
		return mofs_type;
	}

	public void setMofs_type(String mofs_type) {
		this.mofs_type = mofs_type;
	}

	public String getMofs_id() {
		return mofs_id;
	}

	public void setMofs_id(String mofs_id) {
		this.mofs_id = mofs_id;
	}

	public String getMofs_status_message() {
		return mofs_status_message;
	}

	public void setMofs_status_message(String mofs_status_message) {
		this.mofs_status_message = mofs_status_message;
	}

	public String getMofs_threshold() {
		return mofs_threshold;
	}

	private String mofs_threshold;                    

	public String getSdn_name() {
			return sdn_name;
	}
	
	public void setSdn_name(String sdn_name) {
			this.sdn_name = sdn_name;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
			this.country = country;
	}
	
	public String getoMfs_threshold() {
		return mofs_threshold;
	}
	
	public void setMofs_threshold(String mofs_threshold) {
			this.mofs_threshold = mofs_threshold;
	}
}
