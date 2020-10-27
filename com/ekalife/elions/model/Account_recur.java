package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Account_recur implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer mar_number;                    
	private String lbn_id;   
	private String lbn_nama;
	private Integer mar_jenis;                     
	private String mar_acc_no;                     
	private String mar_holder;                     
	private Date mar_expired;                      
	private Integer mar_active;                    
	private String reg_spaj;
	private Integer lus_id;
	private Date mar_tgl;
	private String lbn_id2; 
	private String nama;
	private Integer flag_jn_tabungan;
	private Integer flag_autodebet_nb;
	private Integer flag_set_auto; 
	private Date tgl_debet; 
	
	public Integer getFlag_autodebet_nb() {
		return flag_autodebet_nb;
	}

	public void setFlag_autodebet_nb(Integer flag_autodebet_nb) {
		this.flag_autodebet_nb = flag_autodebet_nb;
	}

	//Tambahan dari Berho untuk spkit mar_acc_no
	private String [] mar_acc_no_split; 
	
	public Account_recur(){
		
		mar_acc_no_split=new String[21];
	}
	
	public String[] getMar_acc_no_split() {
		return mar_acc_no_split;
	}
	public void setMar_acc_no_split(String[] mar_acc_no_split) {
		this.mar_acc_no_split = mar_acc_no_split;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getLbn_id() {
		return lbn_id;
	}
	public void setLbn_id(String lbn_id) {
		this.lbn_id = lbn_id;
	}
	public String getMar_acc_no() {
		return mar_acc_no;
	}
	public void setMar_acc_no(String mar_acc_no) {
		this.mar_acc_no = mar_acc_no;
	}
	public Integer getMar_active() {
		return mar_active;
	}
	public void setMar_active(Integer mar_active) {
		this.mar_active = mar_active;
	}
	public Date getMar_expired() {
		return mar_expired;
	}
	public void setMar_expired(Date mar_expired) {
		this.mar_expired = mar_expired;
	}
	public String getMar_holder() {
		return mar_holder;
	}
	public void setMar_holder(String mar_holder) {
		this.mar_holder = mar_holder;
	}
	public Integer getMar_jenis() {
		return mar_jenis;
	}
	public void setMar_jenis(Integer mar_jenis) {
		this.mar_jenis = mar_jenis;
	}
	public Integer getMar_number() {
		return mar_number;
	}
	public void setMar_number(Integer mar_number) {
		this.mar_number = mar_number;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getLbn_nama() {
		return lbn_nama;
	}
	public void setLbn_nama(String lbn_nama) {
		this.lbn_nama = lbn_nama;
	}
	public Date getMar_tgl() {
		return mar_tgl;
	}
	public void setMar_tgl(Date mar_tgl) {
		this.mar_tgl = mar_tgl;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getLbn_id2() {
		return lbn_id2;
	}

	public void setLbn_id2(String lbn_id2) {
		this.lbn_id2 = lbn_id2;
	}
	
	public Integer getFlag_jn_tabungan() {
		return flag_jn_tabungan;
	}

	public void setFlag_jn_tabungan(Integer flag_jn_tabungan) {
		this.flag_jn_tabungan = flag_jn_tabungan;
	}
	
	public Integer getFlag_set_auto() {
		return flag_set_auto;
	}

	public void setFlag_set_auto(Integer flag_set_auto) {
		this.flag_set_auto = flag_set_auto;
	}

	public Date getTgl_debet() {
		return tgl_debet;
	}

	public void setTgl_debet(Date tgl_debet) {
		this.tgl_debet = tgl_debet;
	}


}
