package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class VoidPayment implements Serializable {

	private static final long serialVersionUID = 1L;
	
private String mvp_payment_id;                 
private String reg_spaj;                       
private String mspo_policy_no;                 
private String lku_id;                         
private Integer msdp_number;                   
private Date mvp_pay_date;                     
private Double mvp_payment;                    
private Date input_date_uw;                    
private Integer lus_id;                        
private String mvp_no_acc;                     
private String mvp_no_pre;                     
private String mvp_no_voucher;                 
private Integer flag;                          
private Date input_date_acc;
private Integer jn_revisi;

public Integer getJn_revisi() {
	return jn_revisi;
}
public void setJn_revisi(Integer jn_revisi) {
	this.jn_revisi = jn_revisi;
}
public Integer getFlag() {
	return flag;
}
public void setFlag(Integer flag) {
	this.flag = flag;
}
public Date getInput_date_acc() {
	return input_date_acc;
}
public void setInput_date_acc(Date input_date_acc) {
	this.input_date_acc = input_date_acc;
}
public Date getInput_date_uw() {
	return input_date_uw;
}
public void setInput_date_uw(Date input_date_uw) {
	this.input_date_uw = input_date_uw;
}
public String getLku_id() {
	return lku_id;
}
public void setLku_id(String lku_id) {
	this.lku_id = lku_id;
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
public String getMspo_policy_no() {
	return mspo_policy_no;
}
public void setMspo_policy_no(String mspo_policy_no) {
	this.mspo_policy_no = mspo_policy_no;
}
public String getMvp_no_acc() {
	return mvp_no_acc;
}
public void setMvp_no_acc(String mvp_no_acc) {
	this.mvp_no_acc = mvp_no_acc;
}
public String getMvp_no_pre() {
	return mvp_no_pre;
}
public void setMvp_no_pre(String mvp_no_pre) {
	this.mvp_no_pre = mvp_no_pre;
}
public String getMvp_no_voucher() {
	return mvp_no_voucher;
}
public void setMvp_no_voucher(String mvp_no_voucher) {
	this.mvp_no_voucher = mvp_no_voucher;
}
public Date getMvp_pay_date() {
	return mvp_pay_date;
}
public void setMvp_pay_date(Date mvp_pay_date) {
	this.mvp_pay_date = mvp_pay_date;
}
public Double getMvp_payment() {
	return mvp_payment;
}
public void setMvp_payment(Double mvp_payment) {
	this.mvp_payment = mvp_payment;
}
public String getMvp_payment_id() {
	return mvp_payment_id;
}
public void setMvp_payment_id(String mvp_payment_id) {
	this.mvp_payment_id = mvp_payment_id;
}
public String getReg_spaj() {
	return reg_spaj;
}
public void setReg_spaj(String reg_spaj) {
	this.reg_spaj = reg_spaj;
} 

}