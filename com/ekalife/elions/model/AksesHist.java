package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class AksesHist implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 459471122540256007L;
	private Integer msah_jenis;
	private Integer lus_id;
	private String msah_spaj;
	private Date msah_date;
	private String msah_uri;
	private String msah_ip;
	private Integer jenis_id;

	public String getMsah_ip() {
		return msah_ip;
	}

	public void setMsah_ip(String msah_ip) {
		this.msah_ip = msah_ip;
	}

	public Integer getMsah_jenis() {
		return msah_jenis;
	}

	public void setMsah_jenis(Integer msah_jenis) {
		this.msah_jenis = msah_jenis;
	}

	public Integer getLus_id() {
		return lus_id;
	}

	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}

	public String getMsah_spaj() {
		return msah_spaj;
	}

	public void setMsah_spaj(String msah_spaj) {
		this.msah_spaj = msah_spaj;
	}

	public Date getMsah_date() {
		return msah_date;
	}

	public void setMsah_date(Date msah_date) {
		this.msah_date = msah_date;
	}

	public String getMsah_uri() {
		return msah_uri;
	}

	public void setMsah_uri(String msah_uri) {
		this.msah_uri = msah_uri;
	}

	public Integer getJenis_id() {
		return jenis_id;
	}

	public void setJenis_id(Integer jenis_id) {
		this.jenis_id = jenis_id;
	}
	
}
