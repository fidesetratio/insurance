package com.ekalife.elions.model;

import java.io.Serializable;

public class SumberKyc implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;
	private Integer no_urut;
	private Integer kyc_id;
	private Integer kyc_pp;
	private String kyc_desc1;
	private String kyc_desc;
	private String kyc_desc_x;
	private String kyc_desc2;
	private String kyc_desc2_x;

	public SumberKyc() {
	}
	
	public SumberKyc(String reg_spaj, int no_urut, int kyc_pp) {
		this.reg_spaj = reg_spaj;
		this.no_urut = no_urut;
		this.kyc_pp = kyc_pp;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public Integer getNo_urut() {
		return no_urut;
	}

	public void setNo_urut(Integer no_urut) {
		this.no_urut = no_urut;
	}

	public Integer getKyc_id() {
		return kyc_id;
	}

	public void setKyc_id(Integer kyc_id) {
		this.kyc_id = kyc_id;
	}

	public Integer getKyc_pp() {
		return kyc_pp;
	}

	public void setKyc_pp(Integer kyc_pp) {
		this.kyc_pp = kyc_pp;
	}

	public String getKyc_desc1() {
		return kyc_desc1;
	}

	public void setKyc_desc1(String kyc_desc1) {
		this.kyc_desc1 = kyc_desc1;
	}
	
	public String getKyc_desc() {
		return kyc_desc;
	}

	public void setKyc_desc(String kyc_desc) {
		this.kyc_desc = kyc_desc;
	}

	public String getKyc_desc_x() {
		return kyc_desc_x;
	}

	public void setKyc_desc_x(String kyc_desc_x) {
		this.kyc_desc_x = kyc_desc_x;
	}

	public String getKyc_desc2() {
		return kyc_desc2;
	}

	public void setKyc_desc2(String kyc_desc2) {
		this.kyc_desc2 = kyc_desc2;
	}

	public String getKyc_desc2_x() {
		return kyc_desc2_x;
	}

	public void setKyc_desc2_x(String kyc_desc2_x) {
		this.kyc_desc2_x = kyc_desc2_x;
	}
	
}