/**
 * 
 */
package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Yusuf
 *
 */
public class TopUp implements Serializable{

	private static final long serialVersionUID = 6305173031683492554L;
	
	public Integer tahun_ke;
	public Integer premi_ke;
	public String judul;
	public List initialErrors;
	public String bill_lku_id;
	
	public String lca_polis;
	public String reg_spaj;
	public String mspa_payment_id;
	public Date mspa_pay_date;
	
	public String lsrek_id;
	public Date mspa_date_book;
	public String mspa_no_pre;
	public String mspa_no_voucher;
	public Date pay_date;
	public String no_kttp;
	public String lus_id;
	public Integer mspa_active;
	public String lus_login_name;
	public Integer lsjb_id;
	public String mspa_old_policy;
	public String lku_id;
	public Double mspa_nilai_kurs;
	public Double mspa_nilai_kurs_dollar;
	public Double mspa_payment;
	public String mspa_desc;
	public String client_bank;
	public String mspa_no_rek;
	public Date mspa_due_date;
	public List listPaymentCount;
	public String kode;
	public Date mste_beg_date;
	public String no_trx;
	public String tipe;
	public String jenis;
	public String actionTypeForDrekDet;
	public Integer tanggalRkDisabled;
	public String mcl_first;
	private String no_pre;
	private Integer mspa_flag_merchant;
	
	public String getNo_pre() {
		return no_pre;
	}
	public void setNo_pre(String no_pre) {
		this.no_pre = no_pre;
	}
	public Integer getTanggalRkDisabled() {
		return tanggalRkDisabled;
	}
	public void setTanggalRkDisabled(Integer tanggalRkDisabled) {
		this.tanggalRkDisabled = tanggalRkDisabled;
	}
	public String getActionTypeForDrekDet() {
		return actionTypeForDrekDet;
	}
	public void setActionTypeForDrekDet(String actionTypeForDrekDet) {
		this.actionTypeForDrekDet = actionTypeForDrekDet;
	}
	public String getTipe() {
		return tipe;
	}
	public void setTipe(String tipe) {
		this.tipe = tipe;
	}
	public String getNo_trx() {
		return no_trx;
	}
	public void setNo_trx(String no_trx) {
		this.no_trx = no_trx;
	}
	public Date getMste_beg_date() {
		return mste_beg_date;
	}
	public void setMste_beg_date(Date mste_beg_date) {
		this.mste_beg_date = mste_beg_date;
	}
	public Double getMspa_nilai_kurs_dollar() {
		return mspa_nilai_kurs_dollar;
	}
	public void setMspa_nilai_kurs_dollar(Double mspa_nilai_kurs_dollar) {
		this.mspa_nilai_kurs_dollar = mspa_nilai_kurs_dollar;
	}
	public String getKode() {
		return kode;
	}
	public void setKode(String kode) {
		this.kode = kode;
	}
	public List getListPaymentCount() {
		return listPaymentCount;
	}
	public void setListPaymentCount(List listPaymentCount) {
		this.listPaymentCount = listPaymentCount;
	}
	public String getLus_login_name() {
		return lus_login_name;
	}
	public void setLus_login_name(String lus_login_name) {
		this.lus_login_name = lus_login_name;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Double getMspa_payment() {
		return mspa_payment;
	}
	public void setMspa_payment(Double mspa_payment) {
		this.mspa_payment = mspa_payment;
	}
	public String getLus_id() {
		return lus_id;
	}
	public void setLus_id(String lus_id) {
		this.lus_id = lus_id;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getClient_bank() {
		return client_bank;
	}
	public void setClient_bank(String client_bank) {
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
	public String getLsrek_id() {
		return lsrek_id;
	}
	public void setLsrek_id(String lsrek_id) {
		this.lsrek_id = lsrek_id;
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
	public String getNo_kttp() {
		return no_kttp;
	}
	public void setNo_kttp(String no_kttp) {
		this.no_kttp = no_kttp;
	}
	public Date getPay_date() {
		return pay_date;
	}
	public void setPay_date(Date pay_date) {
		this.pay_date = pay_date;
	}
	public String getMspa_payment_id() {
		return mspa_payment_id;
	}
	public void setMspa_payment_id(String mspa_payment_id) {
		this.mspa_payment_id = mspa_payment_id;
	}
	public Integer getMspa_active() {
		return mspa_active;
	}
	public void setMspa_active(Integer mspa_active) {
		this.mspa_active = mspa_active;
	}
	public String getLca_polis() {
		return lca_polis;
	}
	public void setLca_polis(String lca_polis) {
		this.lca_polis = lca_polis;
	}
	public void setJudul(String judul) {
		this.judul = judul;
	}
	public String getJudul() {
		return judul;
	}
	public Integer getPremi_ke() {
		return premi_ke;
	}
	public void setPremi_ke(Integer premi_ke) {
		this.premi_ke = premi_ke;
	}
	public Integer getTahun_ke() {
		return tahun_ke;
	}
	public void setTahun_ke(Integer tahun_ke) {
		this.tahun_ke = tahun_ke;
	}
	public List getInitialErrors() {
		return initialErrors;
	}
	public void setInitialErrors(List initialErrors) {
		this.initialErrors = initialErrors;
	}
	public String getBill_lku_id() {
		return bill_lku_id;
	}
	public void setBill_lku_id(String bill_lku_id) {
		this.bill_lku_id = bill_lku_id;
	}
	public Date getMspa_pay_date() {
		return mspa_pay_date;
	}
	public void setMspa_pay_date(Date mspa_pay_date) {
		this.mspa_pay_date = mspa_pay_date;
	}
	public String getJenis() {
		return jenis;
	}
	public void setJenis(String jenis) {
		this.jenis = jenis;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public Integer getMspa_flag_merchant() {
		return mspa_flag_merchant;
	}
	public void setMspa_flag_merchant(Integer mspa_flag_merchant) {
		this.mspa_flag_merchant = mspa_flag_merchant;
	}
}
