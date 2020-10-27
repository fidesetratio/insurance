package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class ProductInsEnd implements Serializable{
	/**
	 * Field type : long
	 * Create By : Bertho Rafitya Iwasurya
	 * Date Created : 9:43:39 AM
	 */
	private static final long serialVersionUID = -2449299029902772378L;
	private String msen_endors_no;                 
	private String reg_spaj;                       
	private Integer mste_insured_no;               
	private Integer lsbs_id;                       
	private Integer lsdbs_number;                  
	private String lku_id;                         
	private Date mspie_beg_date;                   
	private Date mspie_end_date;                   
	private Double mspie_tsi;                      
	private Double mspie_tsi_a;                    
	private Double mspie_tsi_b;                    
	private Double mspie_tsi_c;                    
	private Double mspie_tsi_d;                    
	private Double mspie_tsi_m;                    
	private Integer mspie_class;                   
	private Integer mspie_unit;                    
	private Double mspie_rate;                     
	private Integer mspie_persen;                  
	private Double mspie_premium;                  
	private Double mspie_discount;                 
	private Double mspie_extra; 
	private Integer mspie_ins_period;              
	private Integer lus_id;                        
	private Integer lscb_id;                       
	private Integer lst_lsbs_id;                   
	private Integer lst_lsdbs_number;              
	private Double mspie_tsi_old;                  
	private Double mspie_premium_old;              
	private Double mspie_disc_old;                 
	private Integer lsbs_id_old;                   
	private Integer lsdbs_num_old;                 
	private Double mspie_rate_old;                 
	private Integer lscb_id_old;                   
	private Double mspie_premium_prod;             
	private Double mspie_discount_prod;
	
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
	public Integer getLsbs_id_old() {
		return lsbs_id_old;
	}
	public void setLsbs_id_old(Integer lsbs_id_old) {
		this.lsbs_id_old = lsbs_id_old;
	}
	public Integer getLscb_id() {
		return lscb_id;
	}
	public void setLscb_id(Integer lscb_id) {
		this.lscb_id = lscb_id;
	}
	public Integer getLscb_id_old() {
		return lscb_id_old;
	}
	public void setLscb_id_old(Integer lscb_id_old) {
		this.lscb_id_old = lscb_id_old;
	}
	public Integer getLsdbs_num_old() {
		return lsdbs_num_old;
	}
	public void setLsdbs_num_old(Integer lsdbs_num_old) {
		this.lsdbs_num_old = lsdbs_num_old;
	}
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	public Integer getLst_lsbs_id() {
		return lst_lsbs_id;
	}
	public void setLst_lsbs_id(Integer lst_lsbs_id) {
		this.lst_lsbs_id = lst_lsbs_id;
	}
	public Integer getLst_lsdbs_number() {
		return lst_lsdbs_number;
	}
	public void setLst_lsdbs_number(Integer lst_lsdbs_number) {
		this.lst_lsdbs_number = lst_lsdbs_number;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getMsen_endors_no() {
		return msen_endors_no;
	}
	public void setMsen_endors_no(String msen_endors_no) {
		this.msen_endors_no = msen_endors_no;
	}
	public Date getMspie_beg_date() {
		return mspie_beg_date;
	}
	public void setMspie_beg_date(Date mspie_beg_date) {
		this.mspie_beg_date = mspie_beg_date;
	}
	public Integer getMspie_class() {
		return mspie_class;
	}
	public void setMspie_class(Integer mspie_class) {
		this.mspie_class = mspie_class;
	}
	public Double getMspie_disc_old() {
		return mspie_disc_old;
	}
	public void setMspie_disc_old(Double mspie_disc_old) {
		this.mspie_disc_old = mspie_disc_old;
	}
	public Double getMspie_discount() {
		return mspie_discount;
	}
	public void setMspie_discount(Double mspie_discount) {
		this.mspie_discount = mspie_discount;
	}
	public Double getMspie_discount_prod() {
		return mspie_discount_prod;
	}
	public void setMspie_discount_prod(Double mspie_discount_prod) {
		this.mspie_discount_prod = mspie_discount_prod;
	}
	public Date getMspie_end_date() {
		return mspie_end_date;
	}
	public void setMspie_end_date(Date mspie_end_date) {
		this.mspie_end_date = mspie_end_date;
	}
	public Double getMspie_extra() {
		return mspie_extra;
	}
	public void setMspie_extra(Double mspie_extra) {
		this.mspie_extra = mspie_extra;
	}
	public Integer getMspie_ins_period() {
		return mspie_ins_period;
	}
	public void setMspie_ins_period(Integer mspie_ins_period) {
		this.mspie_ins_period = mspie_ins_period;
	}
	public Integer getMspie_persen() {
		return mspie_persen;
	}
	public void setMspie_persen(Integer mspie_persen) {
		this.mspie_persen = mspie_persen;
	}
	public Double getMspie_premium() {
		return mspie_premium;
	}
	public void setMspie_premium(Double mspie_premium) {
		this.mspie_premium = mspie_premium;
	}
	public Double getMspie_premium_old() {
		return mspie_premium_old;
	}
	public void setMspie_premium_old(Double mspie_premium_old) {
		this.mspie_premium_old = mspie_premium_old;
	}
	public Double getMspie_premium_prod() {
		return mspie_premium_prod;
	}
	public void setMspie_premium_prod(Double mspie_premium_prod) {
		this.mspie_premium_prod = mspie_premium_prod;
	}
	public Double getMspie_rate() {
		return mspie_rate;
	}
	public void setMspie_rate(Double mspie_rate) {
		this.mspie_rate = mspie_rate;
	}
	public Double getMspie_rate_old() {
		return mspie_rate_old;
	}
	public void setMspie_rate_old(Double mspie_rate_old) {
		this.mspie_rate_old = mspie_rate_old;
	}
	public Double getMspie_tsi() {
		return mspie_tsi;
	}
	public void setMspie_tsi(Double mspie_tsi) {
		this.mspie_tsi = mspie_tsi;
	}
	public Double getMspie_tsi_a() {
		return mspie_tsi_a;
	}
	public void setMspie_tsi_a(Double mspie_tsi_a) {
		this.mspie_tsi_a = mspie_tsi_a;
	}
	public Double getMspie_tsi_b() {
		return mspie_tsi_b;
	}
	public void setMspie_tsi_b(Double mspie_tsi_b) {
		this.mspie_tsi_b = mspie_tsi_b;
	}
	public Double getMspie_tsi_c() {
		return mspie_tsi_c;
	}
	public void setMspie_tsi_c(Double mspie_tsi_c) {
		this.mspie_tsi_c = mspie_tsi_c;
	}
	public Double getMspie_tsi_d() {
		return mspie_tsi_d;
	}
	public void setMspie_tsi_d(Double mspie_tsi_d) {
		this.mspie_tsi_d = mspie_tsi_d;
	}
	public Double getMspie_tsi_m() {
		return mspie_tsi_m;
	}
	public void setMspie_tsi_m(Double mspie_tsi_m) {
		this.mspie_tsi_m = mspie_tsi_m;
	}
	public Double getMspie_tsi_old() {
		return mspie_tsi_old;
	}
	public void setMspie_tsi_old(Double mspie_tsi_old) {
		this.mspie_tsi_old = mspie_tsi_old;
	}
	public Integer getMspie_unit() {
		return mspie_unit;
	}
	public void setMspie_unit(Integer mspie_unit) {
		this.mspie_unit = mspie_unit;
	}
	public Integer getMste_insured_no() {
		return mste_insured_no;
	}
	public void setMste_insured_no(Integer mste_insured_no) {
		this.mste_insured_no = mste_insured_no;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}            



}
