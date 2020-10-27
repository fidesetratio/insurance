package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Currency implements Serializable {
	
	private static final long serialVersionUID = -214053381137521715L;
	private String lku_id;
	private Date lkh_date;
	private Integer lkh_currency;
	private Integer lkh_kurs_jual;
	private Integer lkh_kurs_beli;
	private String flag_insert;
	
	public String getFlag_insert() {
		return flag_insert;
	}

	public void setFlag_insert(String flag_insert) {
		this.flag_insert = flag_insert;
	}

	public Integer getLkh_currency() {
		return lkh_currency;
	}

	public void setLkh_currency(Integer lkh_currency) {
		this.lkh_currency = lkh_currency;
	}

	public Date getLkh_date() {
		return lkh_date;
	}

	public void setLkh_date(Date lkh_date) {
		this.lkh_date = lkh_date;
	}

	public Integer getLkh_kurs_beli() {
		return lkh_kurs_beli;
	}

	public void setLkh_kurs_beli(Integer lkh_kurs_beli) {
		this.lkh_kurs_beli = lkh_kurs_beli;
	}

	public Integer getLkh_kurs_jual() {
		return lkh_kurs_jual;
	}

	public void setLkh_kurs_jual(Integer lkh_kurs_jual) {
		this.lkh_kurs_jual = lkh_kurs_jual;
	}

	public String getLku_id() {
		return lku_id;
	}

	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	
}