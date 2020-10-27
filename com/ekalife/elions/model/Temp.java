/**
 * @author  : Ferry Harlim
 * @created : Jan 26, 2007 
 */
package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Temp implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;                       
	private Integer mste_age;                      
	private Integer mste_medical;                  
	private Integer lsbs_id;
	private Integer lsbs_id1;
	private Integer lsbs_id2;  
	private Integer lsdbs_number;                  
	private String lku_id;                         
	private Date mspr_beg_date;                    
	private Double mspr_tsi;
	//
	private String mcl_first_t;
	private String mspo_policy_no;
	private String lsdbs_name;
	private Integer number;
	private Double retensi;
	private Double reas;
	private Integer no;
	//
	private String mspo_policy_holder;             
	private String mcl_first_p;
	private Integer mspe_sex_p;                      
	private Date mspe_date_birth_p;                  
	private Integer mspe_sex_t;                      
	private Date mspe_date_birth_t;                  
	private Integer mspo_age;                      
	private Date mspr_end_date;                    
	private Integer mspo_ins_period;               
	private Integer mspo_pay_period;               
	private Integer mspr_class;                    
	private Integer mste_reas;
	private String lscb_pay_mode;

	public String getLscb_pay_mode() {
		return lscb_pay_mode;
	}
	public void setLscb_pay_mode(String lscb_pay_mode) {
		this.lscb_pay_mode = lscb_pay_mode;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public Integer getLsbs_id1() {
		return lsbs_id1;
	}
	public void setLsbs_id1(Integer lsbs_id1) {
		this.lsbs_id1 = lsbs_id1;
	}
	public Integer getLsbs_id2() {
		return lsbs_id2;
	}
	public void setLsbs_id2(Integer lsbs_id2) {
		this.lsbs_id2 = lsbs_id2;
	}
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	public Date getMspe_date_birth_p() {
		return mspe_date_birth_p;
	}
	public void setMspe_date_birth_p(Date mspe_date_birth_p) {
		this.mspe_date_birth_p = mspe_date_birth_p;
	}
	public Date getMspe_date_birth_t() {
		return mspe_date_birth_t;
	}
	public void setMspe_date_birth_t(Date mspe_date_birth_t) {
		this.mspe_date_birth_t = mspe_date_birth_t;
	}
	public Integer getMspe_sex_p() {
		return mspe_sex_p;
	}
	public void setMspe_sex_p(Integer mspe_sex_p) {
		this.mspe_sex_p = mspe_sex_p;
	}
	public Integer getMspe_sex_t() {
		return mspe_sex_t;
	}
	public void setMspe_sex_t(Integer mspe_sex_t) {
		this.mspe_sex_t = mspe_sex_t;
	}
	public Integer getMspo_age() {
		return mspo_age;
	}
	public void setMspo_age(Integer mspo_age) {
		this.mspo_age = mspo_age;
	}
	public Integer getMspo_ins_period() {
		return mspo_ins_period;
	}
	public void setMspo_ins_period(Integer mspo_ins_period) {
		this.mspo_ins_period = mspo_ins_period;
	}
	public Integer getMspo_pay_period() {
		return mspo_pay_period;
	}
	public void setMspo_pay_period(Integer mspo_pay_period) {
		this.mspo_pay_period = mspo_pay_period;
	}
	public String getMspo_policy_holder() {
		return mspo_policy_holder;
	}
	public void setMspo_policy_holder(String mspo_policy_holder) {
		this.mspo_policy_holder = mspo_policy_holder;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public Date getMspr_beg_date() {
		return mspr_beg_date;
	}
	public void setMspr_beg_date(Date mspr_beg_date) {
		this.mspr_beg_date = mspr_beg_date;
	}
	public Integer getMspr_class() {
		return mspr_class;
	}
	public void setMspr_class(Integer mspr_class) {
		this.mspr_class = mspr_class;
	}
	public Date getMspr_end_date() {
		return mspr_end_date;
	}
	public void setMspr_end_date(Date mspr_end_date) {
		this.mspr_end_date = mspr_end_date;
	}
	public Double getMspr_tsi() {
		return mspr_tsi;
	}
	public void setMspr_tsi(Double mspr_tsi) {
		this.mspr_tsi = mspr_tsi;
	}
	public Integer getMste_age() {
		return mste_age;
	}
	public void setMste_age(Integer mste_age) {
		this.mste_age = mste_age;
	}
	public Integer getMste_medical() {
		return mste_medical;
	}
	public void setMste_medical(Integer mste_medical) {
		this.mste_medical = mste_medical;
	}
	public Integer getMste_reas() {
		return mste_reas;
	}
	public void setMste_reas(Integer mste_reas) {
		this.mste_reas = mste_reas;
	}
	public Integer getNo() {
		return no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Double getReas() {
		return reas;
	}
	public void setReas(Double reas) {
		this.reas = reas;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Double getRetensi() {
		return retensi;
	}
	public void setRetensi(Double retensi) {
		this.retensi = retensi;
	}
	public String getMcl_first_p() {
		return mcl_first_p;
	}
	public void setMcl_first_p(String mcl_first_p) {
		this.mcl_first_p = mcl_first_p;
	}
	public String getMcl_first_t() {
		return mcl_first_t;
	}
	public void setMcl_first_t(String mcl_first_t) {
		this.mcl_first_t = mcl_first_t;
	}        
	
	
}
