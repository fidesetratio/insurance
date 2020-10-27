package com.ekalife.elions.model.tts;

import java.io.Serializable;
import java.util.Date;

public class BillingDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mspo_policy_no;
	private String reg_spaj; //gak perlu
	private Date msbi_beg_date;
	private Date msbi_end_date;
	private String s_msbi_beg_date; //gak perlu
	private String s_msbi_end_date; //gak perlu
	private String lku_id;
	private String lku_symbol;
	private Double msbi_remain;
	private Integer msbi_premi_ke;
	private Double msbi_jumlah;
	private Double msbi_discount;
	private Integer size;
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Date getMsbi_beg_date() {
		return msbi_beg_date;
	}
	public void setMsbi_beg_date(Date msbi_beg_date) {
		this.msbi_beg_date = msbi_beg_date;
	}
	public Date getMsbi_end_date() {
		return msbi_end_date;
	}
	public void setMsbi_end_date(Date msbi_end_date) {
		this.msbi_end_date = msbi_end_date;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getS_msbi_beg_date() {
		return s_msbi_beg_date;
	}
	public void setS_msbi_beg_date(String s_msbi_beg_date) {
		this.s_msbi_beg_date = s_msbi_beg_date;
	}
	public String getS_msbi_end_date() {
		return s_msbi_end_date;
	}
	public void setS_msbi_end_date(String s_msbi_end_date) {
		this.s_msbi_end_date = s_msbi_end_date;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Double getMsbi_remain() {
		return msbi_remain;
	}
	public void setMsbi_remain(Double msbi_remain) {
		this.msbi_remain = msbi_remain;
	}
	public String getLku_symbol() {
		return lku_symbol;
	}
	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
	public Integer getMsbi_premi_ke() {
		return msbi_premi_ke;
	}
	public void setMsbi_premi_ke(Integer msbi_premi_ke) {
		this.msbi_premi_ke = msbi_premi_ke;
	}
	public Double getMsbi_discount() {
		return msbi_discount;
	}
	public void setMsbi_discount(Double msbi_discount) {
		this.msbi_discount = msbi_discount;
	}
	public Double getMsbi_jumlah() {
		return msbi_jumlah;
	}
	public void setMsbi_jumlah(Double msbi_jumlah) {
		this.msbi_jumlah = msbi_jumlah;
	}
}
