package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable {

	private static final long serialVersionUID = 1L;
	
	 private String reg_spaj;   
	 private Integer mste_insured_no;   
	 private Integer lsbs_id;
	 private Integer lsdbs_number;   
     private String lku_id;   
     private String lku_symbol;
     private Double mspr_tsi;   
     private Double mspr_tsi_pa_a;   
     private Double mspr_tsi_pa_b;   
     private Double mspr_tsi_pa_c;   
     private Double mspr_tsi_pa_d;   
     private Double mspr_tsi_pa_m;   
     private Integer mspr_class;   
     private Integer mspr_unit;   
     private Integer mspr_persen;   
     private Double mspr_premium;   
     private Double mspr_discount;   
     private String mspr_ref_no;   
     private Integer mspr_active;   
     private String prod_id;   
     private Double mspr_rate;   
     private Double disc;   
     private Double mspr_extra;   
     private Integer mspr_ins_period;   
     private Date mspr_beg_date;   
     private Date mspr_end_date;   
     private Date mspr_end_pay;   
     private Integer mspr_tt ;
     private Double total;
     private Integer tambah;
     private Integer selectIndex;
     private String lsdbs_name;
     private Integer lsbs_id_old;
     private Integer length;
     private Integer mspr_flag_jenis;
       
     private Double mspr_bunga_kpr;                 
     private Integer mspr_ins_bulan;                
     private Double mspr_sisa_limit;                
     private Integer mspr_wait_periode;             
     private Double mspr_ujroh;                     
     private Double mspr_tabarru;                                    
     private Double mspr_pinjm_awal;       
     private Double sar;
     private String no_polis;

     public String getNo_polis() {
		return no_polis;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}

	public Double getSar() {
		return sar;
	}

	public void setSar(Double sar) {
		this.sar = sar;
	}

	public Product() {
 	}
      
     public Product(String reg_spaj, int mste_insured_no, int lsbs_id, int lsdbs_number) {
    	 this.reg_spaj = reg_spaj;
    	 this.mste_insured_no = mste_insured_no;
    	 this.lsbs_id = lsbs_id;
    	 this.lsdbs_number = lsdbs_number;
 	}
      
	public Double getMspr_bunga_kpr() {
		return mspr_bunga_kpr;
	}
	public void setMspr_bunga_kpr(Double mspr_bunga_kpr) {
		this.mspr_bunga_kpr = mspr_bunga_kpr;
	}
	public Integer getMspr_ins_bulan() {
		return mspr_ins_bulan;
	}
	public void setMspr_ins_bulan(Integer mspr_ins_bulan) {
		this.mspr_ins_bulan = mspr_ins_bulan;
	}
	public Double getMspr_pinjm_awal() {
		return mspr_pinjm_awal;
	}
	public void setMspr_pinjm_awal(Double mspr_pinjm_awal) {
		this.mspr_pinjm_awal = mspr_pinjm_awal;
	}
	public Double getMspr_sisa_limit() {
		return mspr_sisa_limit;
	}
	public void setMspr_sisa_limit(Double mspr_sisa_limit) {
		this.mspr_sisa_limit = mspr_sisa_limit;
	}
	public Double getMspr_tabarru() {
		return mspr_tabarru;
	}
	public void setMspr_tabarru(Double mspr_tabarru) {
		this.mspr_tabarru = mspr_tabarru;
	}
	public Double getMspr_ujroh() {
		return mspr_ujroh;
	}
	public void setMspr_ujroh(Double mspr_ujroh) {
		this.mspr_ujroh = mspr_ujroh;
	}
	public Integer getMspr_wait_periode() {
		return mspr_wait_periode;
	}
	public void setMspr_wait_periode(Integer mspr_wait_periode) {
		this.mspr_wait_periode = mspr_wait_periode;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Integer getLsbs_id_old() {
		return lsbs_id_old;
	}
	public void setLsbs_id_old(Integer lsbs_id_old) {
		this.lsbs_id_old = lsbs_id_old;
	}
	public Integer getSelectIndex() {
		return selectIndex;
	}
	public void setSelectIndex(Integer selectIndex) {
		this.selectIndex = selectIndex;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Double getDisc() {
		return disc;
	}
	public void setDisc(Double disc) {
		this.disc = disc;
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
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	public Integer getMspr_active() {
		return mspr_active;
	}
	public void setMspr_active(Integer mspr_active) {
		this.mspr_active = mspr_active;
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
	public Double getMspr_discount() {
		return mspr_discount;
	}
	public void setMspr_discount(Double mspr_discount) {
		this.mspr_discount = mspr_discount;
	}
	public Date getMspr_end_date() {
		return mspr_end_date;
	}
	public void setMspr_end_date(Date mspr_end_date) {
		this.mspr_end_date = mspr_end_date;
	}
	public Date getMspr_end_pay() {
		return mspr_end_pay;
	}
	public void setMspr_end_pay(Date mspr_end_pay) {
		this.mspr_end_pay = mspr_end_pay;
	}
	public Double getMspr_extra() {
		return mspr_extra;
	}
	public void setMspr_extra(Double mspr_extra) {
		this.mspr_extra = mspr_extra;
	}
	public Integer getMspr_ins_period() {
		return mspr_ins_period;
	}
	public void setMspr_ins_period(Integer mspr_ins_period) {
		this.mspr_ins_period = mspr_ins_period;
	}
	public Integer getMspr_persen() {
		return mspr_persen;
	}
	public void setMspr_persen(Integer mspr_persen) {
		this.mspr_persen = mspr_persen;
	}
	public Double getMspr_premium() {
		return mspr_premium;
	}
	public void setMspr_premium(Double mspr_premium) {
		this.mspr_premium = mspr_premium;
	}
	public Double getMspr_rate() {
		return mspr_rate;
	}
	public void setMspr_rate(Double mspr_rate) {
		this.mspr_rate = mspr_rate;
	}
	public String getMspr_ref_no() {
		return mspr_ref_no;
	}
	public void setMspr_ref_no(String mspr_ref_no) {
		this.mspr_ref_no = mspr_ref_no;
	}
	public Double getMspr_tsi() {
		return mspr_tsi;
	}
	public void setMspr_tsi(Double mspr_tsi) {
		this.mspr_tsi = mspr_tsi;
	}
	public Double getMspr_tsi_pa_a() {
		return mspr_tsi_pa_a;
	}
	public void setMspr_tsi_pa_a(Double mspr_tsi_pa_a) {
		this.mspr_tsi_pa_a = mspr_tsi_pa_a;
	}
	public Double getMspr_tsi_pa_b() {
		return mspr_tsi_pa_b;
	}
	public void setMspr_tsi_pa_b(Double mspr_tsi_pa_b) {
		this.mspr_tsi_pa_b = mspr_tsi_pa_b;
	}
	public Double getMspr_tsi_pa_c() {
		return mspr_tsi_pa_c;
	}
	public void setMspr_tsi_pa_c(Double mspr_tsi_pa_c) {
		this.mspr_tsi_pa_c = mspr_tsi_pa_c;
	}
	public Double getMspr_tsi_pa_d() {
		return mspr_tsi_pa_d;
	}
	public void setMspr_tsi_pa_d(Double mspr_tsi_pa_d) {
		this.mspr_tsi_pa_d = mspr_tsi_pa_d;
	}
	public Double getMspr_tsi_pa_m() {
		return mspr_tsi_pa_m;
	}
	public void setMspr_tsi_pa_m(Double mspr_tsi_pa_m) {
		this.mspr_tsi_pa_m = mspr_tsi_pa_m;
	}
	public Integer getMspr_tt() {
		return mspr_tt;
	}
	public void setMspr_tt(Integer mspr_tt) {
		this.mspr_tt = mspr_tt;
	}
	public Integer getMspr_unit() {
		return mspr_unit;
	}
	public void setMspr_unit(Integer mspr_unit) {
		this.mspr_unit = mspr_unit;
	}
	public Integer getMste_insured_no() {
		return mste_insured_no;
	}
	public void setMste_insured_no(Integer mste_insured_no) {
		this.mste_insured_no = mste_insured_no;
	}
	public String getProd_id() {
		return prod_id;
	}
	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getTambah() {
		return tambah;
	}
	public void setTambah(Integer tambah) {
		this.tambah = tambah;
	}
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}

	public String getLku_symbol() {
		return lku_symbol;
	}

	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
	
	 public Integer getMspr_flag_jenis() {
			return mspr_flag_jenis;
		}

	public void setMspr_flag_jenis(Integer mspr_flag_jenis) {
		this.mspr_flag_jenis = mspr_flag_jenis;
	}
}
