package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class DetBlackList implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer ldbl_number;
	private Integer lbl_id;
	private Date ldbl_tgl_kejadian;
	private Date ldbl_tgl_kejadian_to;
	private String ldbl_tgl_kejadian2;
	private String ldbl_tgl_kejadian_to2;
	private String ldbl_diagnosa;
	private Integer lus_id;
	
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getLdbl_diagnosa() {
		return ldbl_diagnosa;
	}
	public void setLdbl_diagnosa(String ldbl_diagnosa) {
		this.ldbl_diagnosa = ldbl_diagnosa;
	}
	public Integer getLdbl_number() {
		return ldbl_number;
	}
	public void setLdbl_number(Integer ldbl_number) {
		this.ldbl_number = ldbl_number;
	}
	public Date getLdbl_tgl_kejadian() {
		return ldbl_tgl_kejadian;
	}
	public void setLdbl_tgl_kejadian(Date ldbl_tgl_kejadian) {
		this.ldbl_tgl_kejadian = ldbl_tgl_kejadian;
	}
	public Date getLdbl_tgl_kejadian_to() {
		return ldbl_tgl_kejadian_to;
	}
	public void setLdbl_tgl_kejadian_to(Date ldbl_tgl_kejadian_to) {
		this.ldbl_tgl_kejadian_to = ldbl_tgl_kejadian_to;
	}
	public String getLdbl_tgl_kejadian_to2() {
		return ldbl_tgl_kejadian_to2;
	}
	public void setLdbl_tgl_kejadian_to2(String ldbl_tgl_kejadian_to2) {
		this.ldbl_tgl_kejadian_to2 = ldbl_tgl_kejadian_to2;
	}
	public String getLdbl_tgl_kejadian2() {
		return ldbl_tgl_kejadian2;
	}
	public void setLdbl_tgl_kejadian2(String ldbl_tgl_kejadian2) {
		this.ldbl_tgl_kejadian2 = ldbl_tgl_kejadian2;
	}
	public Integer getLbl_id() {
		return lbl_id;
	}
	public void setLbl_id(Integer lbl_id) {
		this.lbl_id = lbl_id;
	}
	
}
