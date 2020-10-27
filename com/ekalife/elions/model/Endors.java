package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Endors implements Serializable{
	
	/**
	 * Field type : long
	 * Create By : Bertho Rafitya Iwasurya
	 * Date Created : 9:32:40 AM
	 */
	private static final long serialVersionUID = -4759596306736814409L;
	private String msen_endors_no;                 
	private String reg_spaj;                       
	private Integer msen_internal;                 
	private String msen_alasan;                    
	private Date msen_input_date;                  
	private Double msen_endors_cost;               
	private Date msen_active_date;                 
	private String msen_surat;                     
	private Integer msen_print;                    
	private Integer lspd_id;                       
	private Integer msen_tahun_ke;                 
	private Integer msen_premi_ke;                 
	private Integer msen_prod_ke;                  
	private Integer lus_id;                        
	private Integer msen_ke;                       
	private Date msen_tgl_trans;                   
	private Integer msen_auto_rider;
	private Integer flag_ps;
	private Integer msen_proses_bsm;
	private Integer msen_flag_uw;
	private Integer msen_aksep_uw;
	
	private ArrayList<DetEndors> ls_detEndors;
	
	public Endors(){
		ls_detEndors=new ArrayList<DetEndors>();
	}
	
	public Integer getMsen_proses_bsm() {
		return msen_proses_bsm;
	}
	public void setMsen_proses_bsm(Integer msen_proses_bsm) {
		this.msen_proses_bsm = msen_proses_bsm;
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
	public Date getMsen_active_date() {
		return msen_active_date;
	}
	public void setMsen_active_date(Date msen_active_date) {
		this.msen_active_date = msen_active_date;
	}
	public String getMsen_alasan() {
		return msen_alasan;
	}
	public void setMsen_alasan(String msen_alasan) {
		this.msen_alasan = msen_alasan;
	}
	public Integer getMsen_auto_rider() {
		return msen_auto_rider;
	}
	public void setMsen_auto_rider(Integer msen_auto_rider) {
		this.msen_auto_rider = msen_auto_rider;
	}
	public Double getMsen_endors_cost() {
		return msen_endors_cost;
	}
	public void setMsen_endors_cost(Double msen_endors_cost) {
		this.msen_endors_cost = msen_endors_cost;
	}
	public String getMsen_endors_no() {
		return msen_endors_no;
	}
	public void setMsen_endors_no(String msen_endors_no) {
		this.msen_endors_no = msen_endors_no;
	}
	public Date getMsen_input_date() {
		return msen_input_date;
	}
	public void setMsen_input_date(Date msen_input_date) {
		this.msen_input_date = msen_input_date;
	}
	public Integer getMsen_internal() {
		return msen_internal;
	}
	public void setMsen_internal(Integer msen_internal) {
		this.msen_internal = msen_internal;
	}
	public Integer getMsen_ke() {
		return msen_ke;
	}
	public void setMsen_ke(Integer msen_ke) {
		this.msen_ke = msen_ke;
	}
	public Integer getMsen_premi_ke() {
		return msen_premi_ke;
	}
	public void setMsen_premi_ke(Integer msen_premi_ke) {
		this.msen_premi_ke = msen_premi_ke;
	}
	public Integer getMsen_print() {
		return msen_print;
	}
	public void setMsen_print(Integer msen_print) {
		this.msen_print = msen_print;
	}
	public Integer getMsen_prod_ke() {
		return msen_prod_ke;
	}
	public void setMsen_prod_ke(Integer msen_prod_ke) {
		this.msen_prod_ke = msen_prod_ke;
	}
	public String getMsen_surat() {
		return msen_surat;
	}
	public void setMsen_surat(String msen_surat) {
		this.msen_surat = msen_surat;
	}
	public Integer getMsen_tahun_ke() {
		return msen_tahun_ke;
	}
	public void setMsen_tahun_ke(Integer msen_tahun_ke) {
		this.msen_tahun_ke = msen_tahun_ke;
	}
	public Date getMsen_tgl_trans() {
		return msen_tgl_trans;
	}
	public void setMsen_tgl_trans(Date msen_tgl_trans) {
		this.msen_tgl_trans = msen_tgl_trans;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getFlag_ps() {
		return flag_ps;
	}
	public void setFlag_ps(Integer flag_ps) {
		this.flag_ps = flag_ps;
	}
	public Integer getMsen_flag_uw() {
		return msen_flag_uw;
	}
	public void setMsen_flag_uw(Integer msen_flag_uw) {
		this.msen_flag_uw = msen_flag_uw;
	}               
	public Integer getMsen_aksep_uw() {
		return msen_aksep_uw;
	}
	public void setMsen_aksep_uw(Integer msen_aksep_uw) {
		this.msen_aksep_uw = msen_aksep_uw;
	}
}
