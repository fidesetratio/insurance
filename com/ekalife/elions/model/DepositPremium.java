package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class DepositPremium implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private boolean checked;
	
	private String reg_spaj;
	private Integer msdp_number;
	private Integer msdp_jtp=new Integer(0);
	private String msdp_flag;
	private Integer lsjb_id;
	private Integer client_bank;
	private String msdp_no_rek;
	private Date msdp_pay_date;
	private Date msdp_due_date;
	private Date msdp_date_book;
	private String lku_id;
	private String lku_symbol;
	private Double msdp_payment;
	private Double msdp_selisih_kurs;
	private Date msdp_input_date;
	private String msdp_old_policy;
	private String msdp_desc;
	private Integer lus_id;
	private Integer msdp_active=new Integer(1);
	private Integer lsrek_id;
	private String msdp_no_pre;
	private Integer msdp_jurnal;
	private String msdp_no_voucher;
	private String no_kttp;
	private String lus_login_name;
	private Integer msdp_flag_topup;
	private Integer msdp_flag_merchant;
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getLus_login_name() {
		return lus_login_name;
	}
	public void setLus_login_name(String lus_login_name) {
		this.lus_login_name = lus_login_name;
	}
	public Integer getClient_bank() {
		return client_bank;
	}
	public void setClient_bank(Integer client_bank) {
		this.client_bank = client_bank;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Integer getLsjb_id() {
		return lsjb_id;
	}
	public void setLsjb_id(Integer lsjb_id) {
		this.lsjb_id = lsjb_id;
	}
	public Integer getLsrek_id() {
		return lsrek_id;
	}
	public void setLsrek_id(Integer lsrek_id) {
		this.lsrek_id = lsrek_id;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public Integer getMsdp_active() {
		return msdp_active;
	}
	public void setMsdp_active(Integer msdp_active) {
		this.msdp_active = msdp_active;
	}
	public Date getMsdp_date_book() {
		return msdp_date_book;
	}
	public void setMsdp_date_book(Date msdp_date_book) {
		this.msdp_date_book = msdp_date_book;
	}
	public String getMsdp_desc() {
		return msdp_desc;
	}
	public void setMsdp_desc(String msdp_desc) {
		this.msdp_desc = msdp_desc;
	}
	public Date getMsdp_due_date() {
		return msdp_due_date;
	}
	public void setMsdp_due_date(Date msdp_due_date) {
		this.msdp_due_date = msdp_due_date;
	}
	public String getMsdp_flag() {
		return msdp_flag;
	}
	public void setMsdp_flag(String msdp_flag) {
		this.msdp_flag = msdp_flag;
	}
	public Date getMsdp_input_date() {
		return msdp_input_date;
	}
	public void setMsdp_input_date(Date msdp_input_date) {
		this.msdp_input_date = msdp_input_date;
	}
	public Integer getMsdp_jtp() {
		return msdp_jtp;
	}
	public void setMsdp_jtp(Integer msdp_jtp) {
		this.msdp_jtp = msdp_jtp;
	}
	public Integer getMsdp_jurnal() {
		return msdp_jurnal;
	}
	public void setMsdp_jurnal(Integer msdp_jurnal) {
		this.msdp_jurnal = msdp_jurnal;
	}
	public String getMsdp_no_pre() {
		return msdp_no_pre;
	}
	public void setMsdp_no_pre(String msdp_no_pre) {
		this.msdp_no_pre = msdp_no_pre;
	}
	public String getMsdp_no_rek() {
		return msdp_no_rek;
	}
	public void setMsdp_no_rek(String msdp_no_rek) {
		this.msdp_no_rek = msdp_no_rek;
	}
	public String getMsdp_no_voucher() {
		return msdp_no_voucher;
	}
	public void setMsdp_no_voucher(String msdp_no_voucher) {
		this.msdp_no_voucher = msdp_no_voucher;
	}
	public Integer getMsdp_number() {
		return msdp_number;
	}
	public void setMsdp_number(Integer msdp_number) {
		this.msdp_number = msdp_number;
	}
	public String getMsdp_old_policy() {
		return msdp_old_policy;
	}
	public void setMsdp_old_policy(String msdp_old_policy) {
		this.msdp_old_policy = msdp_old_policy;
	}
	public Date getMsdp_pay_date() {
		return msdp_pay_date;
	}
	public void setMsdp_pay_date(Date msdp_pay_date) {
		this.msdp_pay_date = msdp_pay_date;
	}
	public Double getMsdp_payment() {
		return msdp_payment;
	}
	public void setMsdp_payment(Double msdp_payment) {
		this.msdp_payment = msdp_payment;
	}
	public Double getMsdp_selisih_kurs() {
		return msdp_selisih_kurs;
	}
	public void setMsdp_selisih_kurs(Double msdp_selisih_kurs) {
		this.msdp_selisih_kurs = msdp_selisih_kurs;
	}
	public String getNo_kttp() {
		return no_kttp;
	}
	public void setNo_kttp(String no_kttp) {
		this.no_kttp = no_kttp;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getLku_symbol() {
		return lku_symbol;
	}
	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
	public Integer getMsdp_flag_topup() {
		return msdp_flag_topup;
	}
	public void setMsdp_flag_topup(Integer msdp_flag_topup) {
		this.msdp_flag_topup = msdp_flag_topup;
	}
	public Integer getMsdp_flag_merchant() {
		return msdp_flag_merchant;
	}
	public void setMsdp_flag_merchant(Integer msdp_flag_merchant) {
		this.msdp_flag_merchant = msdp_flag_merchant;
	}
}
