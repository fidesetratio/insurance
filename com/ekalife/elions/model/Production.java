package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Production implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;
	private Integer mspro_prod_ke;
	private Integer msbi_tahun_ke;
	private Integer msbi_premi_ke;
	private Date mspro_beg_date;   
	private Date mspro_end_date;
	
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}   
	public Date getMspro_beg_date() {
		return mspro_beg_date;
	}
	public void setMspro_beg_date(Date mspro_beg_date) {
		this.mspro_beg_date = mspro_beg_date;
	}
	public Date getMspro_end_date() {
		return mspro_end_date;
	}
	public void setMspro_end_date(Date mspro_end_date) {
		this.mspro_end_date = mspro_end_date;
	}
	public Integer getMspro_prod_ke() {
		return mspro_prod_ke;
	}
	public void setMspro_prod_ke(Integer mspro_prod_ke) {
		this.mspro_prod_ke = mspro_prod_ke;
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
}
