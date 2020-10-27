package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class TransUlink implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer mu_ke;                         
	private Integer mtu_ke;                        
	private Date mtu_tgl_trans;                    
	private Integer lt_id;                         
	private String lji_id;                         
	private String mtu_desc;                       
	private Double mtu_jumlah;                     
	private Date mtu_tgl_nab;                      
	private Double mtu_nab;                        
	private Double mtu_unit;                       
	private String mtu_dk;                         
	private Double mtu_saldo_unit;                 
	private Date mtu_tgl_input;                    
	private Integer lus_id;                        
	private Integer mtu_flag_print;                
	private Integer mtu_tahun_ke;                  
	private Integer mtu_premi_ke;                  
	private Integer lspd_id;                       
	private Date mtu_tgl_proses;                   
	private Integer mtu_flag_biaya;                
	private Integer mtu_flag_lock;                 
	private Double mtu_saldo_unit_pp;              
	private Double mtu_saldo_unit_tu;              
	private String reg_spaj;
	private String lji_invest;
	public String getLji_id() {
		return lji_id;
	}
	public void setLji_id(String lji_id) {
		this.lji_id = lji_id;
	}
	public Integer getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}
	public Integer getLt_id() {
		return lt_id;
	}
	public void setLt_id(Integer lt_id) {
		this.lt_id = lt_id;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getMtu_desc() {
		return mtu_desc;
	}
	public void setMtu_desc(String mtu_desc) {
		this.mtu_desc = mtu_desc;
	}
	public String getMtu_dk() {
		return mtu_dk;
	}
	public void setMtu_dk(String mtu_dk) {
		this.mtu_dk = mtu_dk;
	}
	public Integer getMtu_flag_biaya() {
		return mtu_flag_biaya;
	}
	public void setMtu_flag_biaya(Integer mtu_flag_biaya) {
		this.mtu_flag_biaya = mtu_flag_biaya;
	}
	public Integer getMtu_flag_lock() {
		return mtu_flag_lock;
	}
	public void setMtu_flag_lock(Integer mtu_flag_lock) {
		this.mtu_flag_lock = mtu_flag_lock;
	}
	public Integer getMtu_flag_print() {
		return mtu_flag_print;
	}
	public void setMtu_flag_print(Integer mtu_flag_print) {
		this.mtu_flag_print = mtu_flag_print;
	}
	public Double getMtu_jumlah() {
		return mtu_jumlah;
	}
	public void setMtu_jumlah(Double mtu_jumlah) {
		this.mtu_jumlah = mtu_jumlah;
	}
	public Integer getMtu_ke() {
		return mtu_ke;
	}
	public void setMtu_ke(Integer mtu_ke) {
		this.mtu_ke = mtu_ke;
	}
	public Double getMtu_nab() {
		return mtu_nab;
	}
	public void setMtu_nab(Double mtu_nab) {
		this.mtu_nab = mtu_nab;
	}
	public Integer getMtu_premi_ke() {
		return mtu_premi_ke;
	}
	public void setMtu_premi_ke(Integer mtu_premi_ke) {
		this.mtu_premi_ke = mtu_premi_ke;
	}
	public Double getMtu_saldo_unit() {
		return mtu_saldo_unit;
	}
	public void setMtu_saldo_unit(Double mtu_saldo_unit) {
		this.mtu_saldo_unit = mtu_saldo_unit;
	}
	public Double getMtu_saldo_unit_pp() {
		return mtu_saldo_unit_pp;
	}
	public void setMtu_saldo_unit_pp(Double mtu_saldo_unit_pp) {
		this.mtu_saldo_unit_pp = mtu_saldo_unit_pp;
	}
	public Double getMtu_saldo_unit_tu() {
		return mtu_saldo_unit_tu;
	}
	public void setMtu_saldo_unit_tu(Double mtu_saldo_unit_tu) {
		this.mtu_saldo_unit_tu = mtu_saldo_unit_tu;
	}
	public Integer getMtu_tahun_ke() {
		return mtu_tahun_ke;
	}
	public void setMtu_tahun_ke(Integer mtu_tahun_ke) {
		this.mtu_tahun_ke = mtu_tahun_ke;
	}
	public Date getMtu_tgl_input() {
		return mtu_tgl_input;
	}
	public void setMtu_tgl_input(Date mtu_tgl_input) {
		this.mtu_tgl_input = mtu_tgl_input;
	}
	public Date getMtu_tgl_nab() {
		return mtu_tgl_nab;
	}
	public void setMtu_tgl_nab(Date mtu_tgl_nab) {
		this.mtu_tgl_nab = mtu_tgl_nab;
	}
	public Date getMtu_tgl_proses() {
		return mtu_tgl_proses;
	}
	public void setMtu_tgl_proses(Date mtu_tgl_proses) {
		this.mtu_tgl_proses = mtu_tgl_proses;
	}
	public Date getMtu_tgl_trans() {
		return mtu_tgl_trans;
	}
	public void setMtu_tgl_trans(Date mtu_tgl_trans) {
		this.mtu_tgl_trans = mtu_tgl_trans;
	}
	public Double getMtu_unit() {
		return mtu_unit;
	}
	public void setMtu_unit(Double mtu_unit) {
		this.mtu_unit = mtu_unit;
	}
	public Integer getMu_ke() {
		return mu_ke;
	}
	public void setMu_ke(Integer mu_ke) {
		this.mu_ke = mu_ke;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getLji_invest() {
		return lji_invest;
	}
	public void setLji_invest(String lji_invest) {
		this.lji_invest = lji_invest;
	} 

}
