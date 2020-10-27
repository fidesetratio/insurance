package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class CariBlacklist implements Serializable {

	private static final long serialVersionUID = -3260973454932103502L;
	private BlackList blacklist;
	private String lbl_id;
	private String mcl_id;
	private String mcl_first;
	private int mcl_blacklist;
	private Date mspe_date_birth;
	private String mspe_date_birth2;
	private String mspe_no_identity;
	private String mspo_policy_no;
	private String reg_spaj;
	private String telpon_rumah;
	
	public BlackList getBlacklist() {
		return blacklist;
	}
	
	public void setBlacklist(BlackList blacklist) {
		this.blacklist = blacklist;
	}

	public String getMcl_first() {
		return mcl_first;
	}

	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}

	public String getMcl_id() {
		return mcl_id;
	}

	public void setMcl_id(String mcl_id) {
		this.mcl_id = mcl_id;
	}

	public Date getMspe_date_birth() {
		return mspe_date_birth;
	}

	public void setMspe_date_birth(Date mspe_date_birth) {
		this.mspe_date_birth = mspe_date_birth;
	}

	public String getMspe_no_identity() {
		return mspe_no_identity;
	}

	public void setMspe_no_identity(String mspe_no_identity) {
		this.mspe_no_identity = mspe_no_identity;
	}

	public String getMspo_policy_no() {
		return mspo_policy_no;
	}

	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public String getMspe_date_birth2() {
		return mspe_date_birth2;
	}

	public void setMspe_date_birth2(String mspe_date_birth2) {
		this.mspe_date_birth2 = mspe_date_birth2;
	}

	public int getMcl_blacklist() {
		return mcl_blacklist;
	}

	public void setMcl_blacklist(int mcl_blacklist) {
		this.mcl_blacklist = mcl_blacklist;
	}

	public String getLbl_id() {
		return lbl_id;
	}

	public void setLbl_id(String lbl_id) {
		this.lbl_id = lbl_id;
	}

	public String getTelpon_rumah() {
		return telpon_rumah;
	}

	public void setTelpon_rumah(String telpon_rumah) {
		this.telpon_rumah = telpon_rumah;
	}

}
