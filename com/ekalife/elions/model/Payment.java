package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Payment implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private boolean checked;
	
	private String mspa_payment_id;
	private String lku_id;
	private Integer lsjb_id;
	private Integer client_bank;
	private String reg_spaj;
	private Integer msdp_number;
	private String mspa_no_rek;
	private Date mspa_pay_date;
	private Date mspa_due_date;
	private Date mspa_date_book;
	private Double mspa_payment;
	private Date mspa_input_date;
	private String mspa_old_policy;
	private String mspa_desc;
	private Integer lus_id;
	private Integer mspa_active;
	private Integer lsrek_id;
	private String mspa_no_pre;
	private Integer mspa_jurnal = new Integer(0);
	private Double mspa_nilai_kurs;
	private String mspa_no_voucher;
	private Date mspa_tgl_jurnal;
	private String mspa_no_jm;
	private String mspa_no_jm_sa;
	private Integer mspa_flag_merchant;

	private List<Payment> lsPayment;
	private String lku_symbol;
	private String lre_acc_no;
	private String lbn_nama;
	
	private String lus_login_name;
	
	private Integer ke;                            
	private Double mstp_value;                     
	private String tipe;                           
	private String lsjb_type;                      
	private String lsbp_nama;                      
	private String lku_rek;                        	
	private Integer pilih;
	
	public Integer getPilih() {
		return pilih;
	}
	public void setPilih(Integer pilih) {
		this.pilih = pilih;
	}
	
	public Integer getKe() {
		return ke;
	}
	public void setKe(Integer ke) {
		this.ke = ke;
	}
	public String getLku_rek() {
		return lku_rek;
	}
	public void setLku_rek(String lku_rek) {
		this.lku_rek = lku_rek;
	}
	public String getLsbp_nama() {
		return lsbp_nama;
	}
	public void setLsbp_nama(String lsbp_nama) {
		this.lsbp_nama = lsbp_nama;
	}
	public String getLsjb_type() {
		return lsjb_type;
	}
	public void setLsjb_type(String lsjb_type) {
		this.lsjb_type = lsjb_type;
	}
	public Double getMstp_value() {
		return mstp_value;
	}
	public void setMstp_value(Double mstp_value) {
		this.mstp_value = mstp_value;
	}
	public String getTipe() {
		return tipe;
	}
	public void setTipe(String tipe) {
		this.tipe = tipe;
	}
	public String getLus_login_name() {
		return lus_login_name;
	}
	public void setLus_login_name(String lus_login_name) {
		this.lus_login_name = lus_login_name;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
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
	public Integer getMsdp_number() {
		return msdp_number;
	}
	public void setMsdp_number(Integer msdp_number) {
		this.msdp_number = msdp_number;
	}
	public Integer getMspa_active() {
		return mspa_active;
	}
	public void setMspa_active(Integer mspa_active) {
		this.mspa_active = mspa_active;
	}
	public Date getMspa_date_book() {
		return mspa_date_book;
	}
	public void setMspa_date_book(Date mspa_date_book) {
		this.mspa_date_book = mspa_date_book;
	}
	public String getMspa_desc() {
		return mspa_desc;
	}
	public void setMspa_desc(String mspa_desc) {
		this.mspa_desc = mspa_desc;
	}
	public Date getMspa_due_date() {
		return mspa_due_date;
	}
	public void setMspa_due_date(Date mspa_due_date) {
		this.mspa_due_date = mspa_due_date;
	}
	public Date getMspa_input_date() {
		return mspa_input_date;
	}
	public void setMspa_input_date(Date mspa_input_date) {
		this.mspa_input_date = mspa_input_date;
	}
	public Integer getMspa_jurnal() {
		return mspa_jurnal;
	}
	public void setMspa_jurnal(Integer mspa_jurnal) {
		this.mspa_jurnal = mspa_jurnal;
	}
	public Double getMspa_nilai_kurs() {
		return mspa_nilai_kurs;
	}
	public void setMspa_nilai_kurs(Double mspa_nilai_kurs) {
		this.mspa_nilai_kurs = mspa_nilai_kurs;
	}
	public String getMspa_no_pre() {
		return mspa_no_pre;
	}
	public void setMspa_no_pre(String mspa_no_pre) {
		this.mspa_no_pre = mspa_no_pre;
	}
	public String getMspa_no_rek() {
		return mspa_no_rek;
	}
	public void setMspa_no_rek(String mspa_no_rek) {
		this.mspa_no_rek = mspa_no_rek;
	}
	public String getMspa_no_voucher() {
		return mspa_no_voucher;
	}
	public void setMspa_no_voucher(String mspa_no_voucher) {
		this.mspa_no_voucher = mspa_no_voucher;
	}
	public String getMspa_old_policy() {
		return mspa_old_policy;
	}
	public void setMspa_old_policy(String mspa_old_policy) {
		this.mspa_old_policy = mspa_old_policy;
	}
	public Date getMspa_pay_date() {
		return mspa_pay_date;
	}
	public void setMspa_pay_date(Date mspa_pay_date) {
		this.mspa_pay_date = mspa_pay_date;
	}
	public Double getMspa_payment() {
		return mspa_payment;
	}
	public void setMspa_payment(Double mspa_payment) {
		this.mspa_payment = mspa_payment;
	}
	public String getMspa_payment_id() {
		return mspa_payment_id;
	}
	public void setMspa_payment_id(String mspa_payment_id) {
		this.mspa_payment_id = mspa_payment_id;
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
	public List<Payment> getLsPayment() {
		return lsPayment;
	}
	public void setLsPayment(List<Payment> lsPayment) {
		this.lsPayment = lsPayment;
	}
	public String getLre_acc_no() {
		return lre_acc_no;
	}
	public void setLre_acc_no(String lre_acc_no) {
		this.lre_acc_no = lre_acc_no;
	}
	public String getLbn_nama() {
		return lbn_nama;
	}
	public void setLbn_nama(String lbn_nama) {
		this.lbn_nama = lbn_nama;
	}
	public Date getMspa_tgl_jurnal() {
		return mspa_tgl_jurnal;
	}
	public void setMspa_tgl_jurnal(Date mspa_tgl_jurnal) {
		this.mspa_tgl_jurnal = mspa_tgl_jurnal;
	}
	public String getMspa_no_jm() {
		return mspa_no_jm;
	}
	public void setMspa_no_jm(String mspa_no_jm) {
		this.mspa_no_jm = mspa_no_jm;
	}
	public String getMspa_no_jm_sa() {
		return mspa_no_jm_sa;
	}
	public void setMspa_no_jm_sa(String mspa_no_jm_sa) {
		this.mspa_no_jm_sa = mspa_no_jm_sa;
	}
	public Integer getMspa_flag_merchant() {
		return mspa_flag_merchant;
	}
	public void setMspa_flag_merchant(Integer mspa_flag_merchant) {
		this.mspa_flag_merchant = mspa_flag_merchant;
	}
	
}
