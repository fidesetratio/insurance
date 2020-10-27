package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class PowersaveProses implements Serializable {
	

	private static final long serialVersionUID = -397190645394996123L;
	public String reg_spaj;                       
	public Integer mps_kode;                      
	public Date mps_deposit_date;                 
	public Date mps_batas_date;                   
	public Integer mps_roll_over;                 
	public Integer mps_jangka_inv;                
	public Date mps_rate_date;                    
	public Double mps_rate;                       
	public Double mps_prm_deposit;                
	public Double mps_prm_interest;               
	public Double mps_prm_insurance;              
	public Double mps_prm_debet;                  
	public Double mps_tax;                        
	public Integer mps_employee;                  
	public Date mps_input_date;                   
	public Integer lus_id;                        
	public Integer mps_jenis_plan;                
	public Integer flag_ubah;                     
	public Date mps_end_period;
	
	//Parameter Tambahan
	public String mspo_policy_no;
	public String mcl_first;
	public Integer getFlag_ubah() {
		return flag_ubah;
	}
	public void setFlag_ubah(Integer flag_ubah) {
		this.flag_ubah = flag_ubah;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public Date getMps_batas_date() {
		return mps_batas_date;
	}
	public void setMps_batas_date(Date mps_batas_date) {
		this.mps_batas_date = mps_batas_date;
	}
	public Date getMps_deposit_date() {
		return mps_deposit_date;
	}
	public void setMps_deposit_date(Date mps_deposit_date) {
		this.mps_deposit_date = mps_deposit_date;
	}
	public Integer getMps_employee() {
		return mps_employee;
	}
	public void setMps_employee(Integer mps_employee) {
		this.mps_employee = mps_employee;
	}
	public Date getMps_end_period() {
		return mps_end_period;
	}
	public void setMps_end_period(Date mps_end_period) {
		this.mps_end_period = mps_end_period;
	}
	public Date getMps_input_date() {
		return mps_input_date;
	}
	public void setMps_input_date(Date mps_input_date) {
		this.mps_input_date = mps_input_date;
	}
	public Integer getMps_jangka_inv() {
		return mps_jangka_inv;
	}
	public void setMps_jangka_inv(Integer mps_jangka_inv) {
		this.mps_jangka_inv = mps_jangka_inv;
	}
	public Integer getMps_jenis_plan() {
		return mps_jenis_plan;
	}
	public void setMps_jenis_plan(Integer mps_jenis_plan) {
		this.mps_jenis_plan = mps_jenis_plan;
	}
	public Integer getMps_kode() {
		return mps_kode;
	}
	public void setMps_kode(Integer mps_kode) {
		this.mps_kode = mps_kode;
	}
	public Double getMps_prm_debet() {
		return mps_prm_debet;
	}
	public void setMps_prm_debet(Double mps_prm_debet) {
		this.mps_prm_debet = mps_prm_debet;
	}
	public Double getMps_prm_deposit() {
		return mps_prm_deposit;
	}
	public void setMps_prm_deposit(Double mps_prm_deposit) {
		this.mps_prm_deposit = mps_prm_deposit;
	}
	public Double getMps_prm_insurance() {
		return mps_prm_insurance;
	}
	public void setMps_prm_insurance(Double mps_prm_insurance) {
		this.mps_prm_insurance = mps_prm_insurance;
	}
	public Double getMps_prm_interest() {
		return mps_prm_interest;
	}
	public void setMps_prm_interest(Double mps_prm_interest) {
		this.mps_prm_interest = mps_prm_interest;
	}
	public Double getMps_rate() {
		return mps_rate;
	}
	public void setMps_rate(Double mps_rate) {
		this.mps_rate = mps_rate;
	}
	public Date getMps_rate_date() {
		return mps_rate_date;
	}
	public void setMps_rate_date(Date mps_rate_date) {
		this.mps_rate_date = mps_rate_date;
	}
	public Integer getMps_roll_over() {
		return mps_roll_over;
	}
	public void setMps_roll_over(Integer mps_roll_over) {
		this.mps_roll_over = mps_roll_over;
	}
	public Double getMps_tax() {
		return mps_tax;
	}
	public void setMps_tax(Double mps_tax) {
		this.mps_tax = mps_tax;
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
