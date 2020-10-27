package com.ekalife.elions.model.reinstate;

import java.io.Serializable;
import java.util.Date;

public class Reinstatement implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mspo_policy_no_format;          
	private String lsdbs_name;                     
	private String lku_symbol;                     
	private Double mspr_tsi;                       
	private Double mspr_premium;                   
	private String nama_pp;                        
	private String ttl_pp;                         
	private Integer mspo_age;                      
	private String nama_tt;                        
	private String ttl_tt;                         
	private Integer mste_age;                      
	private Integer mspo_ins_period;               
	private Integer mspo_pay_period;      
	private Date mspo_end_date;
	private Date msbi_aktif_date;
	private Integer msbi_tahun_ke;
	private Integer lama_lapse;
	private Date tgl_lapse;                    
	private Integer lssp_id;                       
	private Integer no;                            
	private String rider_name;
	private String put_uw_old;
	private String put_uw_new;
	private String mste_tgl_terima_LB;
	private String mste_tgl_kirim_LB;
	private String put_uw_kep;
	
	public String getMste_tgl_kirim_LB() {
		return mste_tgl_kirim_LB;
	}
	public void setMste_tgl_kirim_LB(String mste_tgl_kirim_LB) {
		this.mste_tgl_kirim_LB = mste_tgl_kirim_LB;
	}
	public String getMste_tgl_terima_LB() {
		return mste_tgl_terima_LB;
	}
	public void setMste_tgl_terima_LB(String mste_tgl_terima_LB) {
		this.mste_tgl_terima_LB = mste_tgl_terima_LB;
	}
	
	public String getPut_uw_new() {
		return put_uw_new;
	}
	public void setPut_uw_new(String put_uw_new) {
		this.put_uw_new = put_uw_new;
	}
	public String getPut_uw_old() {
		return put_uw_old;
	}
	public void setPut_uw_old(String put_uw_old) {
		this.put_uw_old = put_uw_old;
	}
	public String getLku_symbol() {
		return lku_symbol;
	}
	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}
	public Integer getLssp_id() {
		return lssp_id;
	}
	public void setLssp_id(Integer lssp_id) {
		this.lssp_id = lssp_id;
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
	public String getMspo_policy_no_format() {
		return mspo_policy_no_format;
	}
	public void setMspo_policy_no_format(String mspo_policy_no_format) {
		this.mspo_policy_no_format = mspo_policy_no_format;
	}
	public Double getMspr_premium() {
		return mspr_premium;
	}
	public void setMspr_premium(Double mspr_premium) {
		this.mspr_premium = mspr_premium;
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
	public String getNama_pp() {
		return nama_pp;
	}
	public void setNama_pp(String nama_pp) {
		this.nama_pp = nama_pp;
	}
	public String getNama_tt() {
		return nama_tt;
	}
	public void setNama_tt(String nama_tt) {
		this.nama_tt = nama_tt;
	}
	public Integer getNo() {
		return no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}
	public String getRider_name() {
		return rider_name;
	}
	public void setRider_name(String rider_name) {
		this.rider_name = rider_name;
	}
	public String getTtl_pp() {
		return ttl_pp;
	}
	public void setTtl_pp(String ttl_pp) {
		this.ttl_pp = ttl_pp;
	}
	public String getTtl_tt() {
		return ttl_tt;
	}
	public void setTtl_tt(String ttl_tt) {
		this.ttl_tt = ttl_tt;
	}
	public Integer getMspo_pay_period() {
		return mspo_pay_period;
	}
	public void setMspo_pay_period(Integer mspo_pay_period) {
		this.mspo_pay_period = mspo_pay_period;
	}
	public Integer getMsbi_tahun_ke() {
		return msbi_tahun_ke;
	}
	public void setMsbi_tahun_ke(Integer msbi_tahun_ke) {
		this.msbi_tahun_ke = msbi_tahun_ke;
	}
	public Date getTgl_lapse() {
		return tgl_lapse;
	}
	public void setTgl_lapse(Date tgl_lapse) {
		this.tgl_lapse = tgl_lapse;
	}
	public Integer getLama_lapse() {
		return lama_lapse;
	}
	public void setLama_lapse(Integer lama_lapse) {
		this.lama_lapse = lama_lapse;
	}
	public Date getMspo_end_date() {
		return mspo_end_date;
	}
	public void setMspo_end_date(Date mspo_end_date) {
		this.mspo_end_date = mspo_end_date;
	}
	public Date getMsbi_aktif_date() {
		return msbi_aktif_date;
	}
	public void setMsbi_aktif_date(Date msbi_aktif_date) {
		this.msbi_aktif_date = msbi_aktif_date;
	}
	public String getPut_uw_kep() {
		return put_uw_kep;
	}
	public void setPut_uw_kep(String put_uw_kep) {
		this.put_uw_kep = put_uw_kep;
	}  
}
