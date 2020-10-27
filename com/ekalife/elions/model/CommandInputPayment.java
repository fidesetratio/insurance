package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.List;

/**
 * Command Class untuk input payment successive
 * 
 * @author yusuf
 * @since Aug 10, 2009 (10:13:06 AM)
 */
public class CommandInputPayment implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String reg_spaj;
	public String mspo_policy_no;
	public Integer msbi_tahun_ke;
	public Integer msbi_premi_ke;
	public String mspa_payment_id;
	public String mode;
	public String bill_lku_id;
	
	public List<Billing> daftarBilling;
	public PowersaveCair dataPolis;
	public TopUp payment;
	public List<Payment> daftarPayment;
	
	public String getBill_lku_id() {
		return bill_lku_id;
	}
	public void setBill_lku_id(String bill_lku_id) {
		this.bill_lku_id = bill_lku_id;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getMspa_payment_id() {
		return mspa_payment_id;
	}
	public void setMspa_payment_id(String mspa_payment_id) {
		this.mspa_payment_id = mspa_payment_id;
	}
	public List<Payment> getDaftarPayment() {
		return daftarPayment;
	}
	public void setDaftarPayment(List<Payment> daftarPayment) {
		this.daftarPayment = daftarPayment;
	}
	public TopUp getPayment() {
		return payment;
	}
	public void setPayment(TopUp payment) {
		this.payment = payment;
	}
	public PowersaveCair getDataPolis() {
		return dataPolis;
	}
	public void setDataPolis(PowersaveCair dataPolis) {
		this.dataPolis = dataPolis;
	}
	public List<Billing> getDaftarBilling() {
		return daftarBilling;
	}
	public void setDaftarBilling(List<Billing> daftarBilling) {
		this.daftarBilling = daftarBilling;
	}
	public Integer getMsbi_premi_ke() {
		return msbi_premi_ke;
	}
	public void setMsbi_premi_ke(Integer msbi_premi_ke) {
		this.msbi_premi_ke = msbi_premi_ke;
	}
	public Integer getMsbi_tahun_ke() {
		return msbi_tahun_ke;
	}
	public void setMsbi_tahun_ke(Integer msbi_tahun_ke) {
		this.msbi_tahun_ke = msbi_tahun_ke;
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
}