package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class UlinkBill implements Serializable {
	
	private static final long serialVersionUID = 2561686452021904185L;
	
	private String reg_spaj;
	private Integer tahun;
	private Integer pot_ke;
	private Integer mu_ke;
	private Date next_bill;
	
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getTahun() {
		return tahun;
	}
	public void setTahun(Integer tahun) {
		this.tahun = tahun;
	}
	public Integer getPot_ke() {
		return pot_ke;
	}
	public void setPot_ke(Integer pot_ke) {
		this.pot_ke = pot_ke;
	}
	public Integer getMu_ke() {
		return mu_ke;
	}
	public void setMu_ke(Integer mu_ke) {
		this.mu_ke = mu_ke;
	}
	public Date getNext_bill() {
		return next_bill;
	}
	public void setNext_bill(Date next_bill) {
		this.next_bill = next_bill;
	}
}
