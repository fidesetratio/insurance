package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ekalife.utils.Common;

public class MstTransUlink implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer mtu_ke;
	private Integer mu_ke;
	private String reg_spaj;

	public Integer getMtu_ke() {
		return mtu_ke;
	}
	public void setMtu_ke(Integer mtu_ke) {
		this.mtu_ke = mtu_ke;
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
	
	}
