package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class ListSpajTtp implements Serializable {

	private static final long serialVersionUID = 7455788614246338042L;
	private String reg_spaj;                       
	private String ket_ttp;
	private String userttp;
	private String tgl_ttp;
	private String msag_id;
	private String mcl_first;
	
	
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public String getMsag_id() {
		return msag_id;
	}
	public void setMsag_id(String msag_id) {
		this.msag_id = msag_id;
	}
	public String getKet_ttp() {
		return ket_ttp;
	}
	public void setKet_ttp(String ket_ttp) {
		this.ket_ttp = ket_ttp;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public String getTgl_ttp() {
		return tgl_ttp;
	}
	public void setTgl_ttp(String tgl_ttp) {
		this.tgl_ttp = tgl_ttp;
	}
	
	public String getUserttp() {
		return userttp;
	}
	public void setUserttp(String userttp) {
		this.userttp = userttp;
	}              

}
