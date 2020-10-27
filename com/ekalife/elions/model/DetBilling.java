package com.ekalife.elions.model;

import java.io.Serializable;

public class DetBilling implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;
	private Integer msbi_tahun_ke;
	private Integer msbi_premi_ke;
	private Integer msdb_det_ke;
	private Integer lsbs_id;
	private Integer lsdbs_number;
	private Double msdb_premium;
	private Double msdb_discount;
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
	public Integer getMsbi_premi_ke() {
		return msbi_premi_ke;
	}
	public void setMsbi_premi_ke(Integer msbi_premi_ke) {
		this.msbi_premi_ke = msbi_premi_ke;
	}
	public Integer getMsbi_tahun_ke() {
		return msbi_tahun_ke;
	}
	public void setMsbi_tahun_ke(Integer msbi_tahun_ke) {
		this.msbi_tahun_ke = msbi_tahun_ke;
	}
	public Integer getMsdb_det_ke() {
		return msdb_det_ke;
	}
	public void setMsdb_det_ke(Integer msdb_det_ke) {
		this.msdb_det_ke = msdb_det_ke;
	}
	public Double getMsdb_discount() {
		return msdb_discount;
	}
	public void setMsdb_discount(Double msdb_discount) {
		this.msdb_discount = msdb_discount;
	}
	public Double getMsdb_premium() {
		return msdb_premium;
	}
	public void setMsdb_premium(Double msdb_premium) {
		this.msdb_premium = msdb_premium;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
}
