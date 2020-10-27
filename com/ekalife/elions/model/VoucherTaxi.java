package com.ekalife.elions.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Model Voucher Taxi Marketing
 * @author : Daru
 * @since : Dec 18, 2013
 */
public class VoucherTaxi {

	private String msvt_no;
	private String msvt_msag_id;
	private String msvt_user_name;
	private String msvt_user_dept;
	private String msvt_from;
	private String msvt_to;
	private String msvt_cost_formatted;
	private BigDecimal msvt_cost;
	private Date msvt_release_date;
	private Date msvt_return_date;
	private String lus_id;
	private Date msvt_insert_date;
	private String lus_id_edit;
	private Date msvt_edit_date;
	private Integer msvt_status_flag;
	private String msvt_status_desc;
	
	public String getMsvt_no() {
		return msvt_no;
	}
	public void setMsvt_no(String msvt_no) {
		this.msvt_no = msvt_no;
	}
	public String getMsvt_msag_id() {
		return msvt_msag_id;
	}
	public void setMsvt_msag_id(String msvt_msag_id) {
		this.msvt_msag_id = msvt_msag_id;
	}
	public String getMsvt_user_name() {
		return msvt_user_name;
	}
	public void setMsvt_user_name(String msvt_user_name) {
		this.msvt_user_name = msvt_user_name;
	}
	public String getMsvt_user_dept() {
		return msvt_user_dept;
	}
	public void setMsvt_user_dept(String msvt_user_dept) {
		this.msvt_user_dept = msvt_user_dept;
	}
	public String getMsvt_from() {
		return msvt_from;
	}
	public void setMsvt_from(String msvt_from) {
		this.msvt_from = msvt_from;
	}
	public String getMsvt_to() {
		return msvt_to;
	}
	public void setMsvt_to(String msvt_to) {
		this.msvt_to = msvt_to;
	}
	public String getMsvt_cost_formatted() {
		return msvt_cost_formatted;
	}
	public void setMsvt_cost_formatted(String msvt_cost_formatted) {
		this.msvt_cost_formatted = msvt_cost_formatted;
		try {
			Long cost = (Long) NumberFormat.getInstance().parse(msvt_cost_formatted);
			this.msvt_cost = BigDecimal.valueOf(cost);
		} catch(ParseException pe) {
			// Do nothing
		} catch(NumberFormatException nfe) {
			// Do nothing
		}
	}
	public BigDecimal getMsvt_cost() {
		return msvt_cost;
	}
	public void setMsvt_cost(BigDecimal msvt_cost) {
		this.msvt_cost = msvt_cost;
		try {
			this.msvt_cost_formatted = NumberFormat.getInstance().format(msvt_cost);
		} catch(IllegalArgumentException iae) {
			// Do nothing
		}
	}
	public Date getMsvt_release_date() {
		return msvt_release_date;
	}
	public void setMsvt_release_date(Date msvt_release_date) {
		this.msvt_release_date = msvt_release_date;
	}
	public Date getMsvt_return_date() {
		return msvt_return_date;
	}
	public void setMsvt_return_date(Date msvt_return_date) {
		this.msvt_return_date = msvt_return_date;
	}
	public String getLus_id() {
		return lus_id;
	}
	public void setLus_id(String lus_id) {
		this.lus_id = lus_id;
	}
	public Date getMsvt_insert_date() {
		return msvt_insert_date;
	}
	public void setMsvt_insert_date(Date msvt_insert_date) {
		this.msvt_insert_date = msvt_insert_date;
	}
	public String getLus_id_edit() {
		return lus_id_edit;
	}
	public void setLus_id_edit(String lus_id_edit) {
		this.lus_id_edit = lus_id_edit;
	}
	public Date getMsvt_edit_date() {
		return msvt_edit_date;
	}
	public void setMsvt_edit_date(Date msvt_edit_date) {
		this.msvt_edit_date = msvt_edit_date;
	}
	public Integer getMsvt_status_flag() {
		return msvt_status_flag;
	}
	public void setMsvt_status_flag(Integer msvt_status_flag) {
		this.msvt_status_flag = msvt_status_flag;
	}
	public String getMsvt_status_desc() {
		return msvt_status_desc;
	}
	public void setMsvt_status_desc(String msvt_status_desc) {
		this.msvt_status_desc = msvt_status_desc;
	}
	
}
