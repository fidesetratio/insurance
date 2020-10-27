package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class BiayaUlink implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;
	private Integer mu_ke;
	private Integer ljb_id;
	private String ljb_biaya;
	private Double mbu_jumlah;
	private Double mbu_persen;
	private Integer lsdbs_number;
	private Integer lsbs_id;
	private Date mbu_end_pay;
	
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	public Integer getLjb_id() {
		return ljb_id;
	}
	public void setLjb_id(Integer ljb_id) {
		this.ljb_id = ljb_id;
	}
	public Double getMbu_jumlah() {
		return mbu_jumlah;
	}
	public void setMbu_jumlah(Double mbu_jumlah) {
		this.mbu_jumlah = mbu_jumlah;
	}
	public Double getMbu_persen() {
		return mbu_persen;
	}
	public void setMbu_persen(Double mbu_persen) {
		this.mbu_persen = mbu_persen;
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
	public String getLjb_biaya() {
		return ljb_biaya;
	}
	public void setLjb_biaya(String ljb_biaya) {
		this.ljb_biaya = ljb_biaya;
	}
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public Date getMbu_end_pay() {
		return mbu_end_pay;
	}
	public void setMbu_end_pay(Date mbu_end_pay) {
		this.mbu_end_pay = mbu_end_pay;
	}
}
