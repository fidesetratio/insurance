package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Broker implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;
	private String lsb_id;
	private String lsb_nama;                                         
	private String lsb_alamat;
	private String lbn_id;
	private String lbn_nama;
	private String no_account;
	private Integer msbi_tahun_ke;
	private Integer msbi_premi_ke;
	private Integer lspd_id;
	private Integer lus_id;
	private Date msco_input_date;
	private boolean flagbroker;
	
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	
	public String getLsb_id() {
		return lsb_id;
	}
	public void setLsb_id(String lsb_id) {
		this.lsb_id = lsb_id;
	}
	
	public String getLsb_nama() {
		return lsb_nama;
	}
	public void setLsb_nama(String lsb_nama) {
		this.lsb_nama = lsb_nama;
	}
	
	public String getLsb_alamat() {
		return lsb_alamat;
	}
	public void setLsb_alamat(String lsb_alamat) {
		this.lsb_alamat = lsb_alamat;
	}
	
	public String getLbn_id() {
		return lbn_id;
	}
	public void setLbn_id(String lbn_id) {
		this.lbn_id = lbn_id;
	}
	
	public String getLbn_nama() {
		return lbn_nama;
	}
	public void setLbn_nama(String lbn_nama) {
		this.lbn_nama = lbn_nama;
	}
	
	public String getNo_account() {
		return no_account;
	}
	public void setNo_account(String no_account) {
		this.no_account = no_account;
	}
	
	public Integer getMsbi_tahun_ke() {
		return msbi_tahun_ke;
	}
	public void setMsbi_tahun_ke(Integer msbi_tahun_ke) {
		this.msbi_tahun_ke = msbi_tahun_ke;
	}
	
	public Integer getMsbi_premi_ke() {
		return msbi_premi_ke;
	}
	public void setMsbi_premi_ke(Integer msbi_premi_ke) {
		this.msbi_premi_ke = msbi_premi_ke;
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
	
	public Date getMsco_input_date() {
		return msco_input_date;
	}
	public void setMsco_input_date(Date msco_input_date) {
		this.msco_input_date = msco_input_date;
	}
	
	public boolean isFlagbroker() {
		return flagbroker;
	}
	public void setFlagbroker(boolean flagbroker) {
		this.flagbroker = flagbroker;
	}
}
