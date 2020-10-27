package com.ekalife.elions.model.btpp;

import java.io.Serializable;
import java.util.Date;

public class Tahapan implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer mstah_tahapan_ke;              
	private String mstah_no_tahapan;               
	private Integer mstah_tahun_ke;                
	private Date mstah_beg_active;                 
	private Date mstah_billing_date;               
	private Double mstah_jumlah;                   
	private Date mstah_jt_tempo;                   
	private Date mstah_tgl_konfirmasi;             
	private Integer mstah_print;                   
	private String reg_spaj;                       
	private Integer lspd_id;                       
	private Integer lus_id;                        
	private Double mstah_persen;                   
	private String mstah_no_pre;                   
	private Date mstah_tgl_proses;                 
	private Integer flag_susulan;                  
	private Date mstah_tgl_aju;                    
	private Date mstah_tgl_trans;                  
	private Date mstah_tgl_print;                  
	private Integer pct_bonus;
	private String lku_id;
	private String lku_symbol;
	public Integer getFlag_susulan() {
		return flag_susulan;
	}
	public void setFlag_susulan(Integer flag_susulan) {
		this.flag_susulan = flag_susulan;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public String getLku_symbol() {
		return lku_symbol;
	}
	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
	public Integer getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public Date getMstah_beg_active() {
		return mstah_beg_active;
	}
	public void setMstah_beg_active(Date mstah_beg_active) {
		this.mstah_beg_active = mstah_beg_active;
	}
	public Date getMstah_billing_date() {
		return mstah_billing_date;
	}
	public void setMstah_billing_date(Date mstah_billing_date) {
		this.mstah_billing_date = mstah_billing_date;
	}
	public Date getMstah_jt_tempo() {
		return mstah_jt_tempo;
	}
	public void setMstah_jt_tempo(Date mstah_jt_tempo) {
		this.mstah_jt_tempo = mstah_jt_tempo;
	}
	public Double getMstah_jumlah() {
		return mstah_jumlah;
	}
	public void setMstah_jumlah(Double mstah_jumlah) {
		this.mstah_jumlah = mstah_jumlah;
	}
	public String getMstah_no_pre() {
		return mstah_no_pre;
	}
	public void setMstah_no_pre(String mstah_no_pre) {
		this.mstah_no_pre = mstah_no_pre;
	}
	public String getMstah_no_tahapan() {
		return mstah_no_tahapan;
	}
	public void setMstah_no_tahapan(String mstah_no_tahapan) {
		this.mstah_no_tahapan = mstah_no_tahapan;
	}
	public Double getMstah_persen() {
		return mstah_persen;
	}
	public void setMstah_persen(Double mstah_persen) {
		this.mstah_persen = mstah_persen;
	}
	public Integer getMstah_print() {
		return mstah_print;
	}
	public void setMstah_print(Integer mstah_print) {
		this.mstah_print = mstah_print;
	}
	public Integer getMstah_tahapan_ke() {
		return mstah_tahapan_ke;
	}
	public void setMstah_tahapan_ke(Integer mstah_tahapan_ke) {
		this.mstah_tahapan_ke = mstah_tahapan_ke;
	}
	public Integer getMstah_tahun_ke() {
		return mstah_tahun_ke;
	}
	public void setMstah_tahun_ke(Integer mstah_tahun_ke) {
		this.mstah_tahun_ke = mstah_tahun_ke;
	}
	public Date getMstah_tgl_aju() {
		return mstah_tgl_aju;
	}
	public void setMstah_tgl_aju(Date mstah_tgl_aju) {
		this.mstah_tgl_aju = mstah_tgl_aju;
	}
	public Date getMstah_tgl_konfirmasi() {
		return mstah_tgl_konfirmasi;
	}
	public void setMstah_tgl_konfirmasi(Date mstah_tgl_konfirmasi) {
		this.mstah_tgl_konfirmasi = mstah_tgl_konfirmasi;
	}
	public Date getMstah_tgl_print() {
		return mstah_tgl_print;
	}
	public void setMstah_tgl_print(Date mstah_tgl_print) {
		this.mstah_tgl_print = mstah_tgl_print;
	}
	public Date getMstah_tgl_proses() {
		return mstah_tgl_proses;
	}
	public void setMstah_tgl_proses(Date mstah_tgl_proses) {
		this.mstah_tgl_proses = mstah_tgl_proses;
	}
	public Date getMstah_tgl_trans() {
		return mstah_tgl_trans;
	}
	public void setMstah_tgl_trans(Date mstah_tgl_trans) {
		this.mstah_tgl_trans = mstah_tgl_trans;
	}
	public Integer getPct_bonus() {
		return pct_bonus;
	}
	public void setPct_bonus(Integer pct_bonus) {
		this.pct_bonus = pct_bonus;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	
     
}
