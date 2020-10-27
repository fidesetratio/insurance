package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class CariSpaj implements Serializable {

	private static final long serialVersionUID = -3260973454932103502L;
	private CariSpaj cariSpaj;
	private String reg_spaj;
	private String mspo_policy_no;
	private String pp;
	private String tt;
	private String lsrg_nama;
	private String lspd_position;
	private String distribusi;
	private String lsdbs_name;
	private String mspo_policy_no_format;
	private Date mspo_date_print;
	private String lus_id;
	private String lssp_status;
	private String perusahaan;
	private String nik;
	
	public String getLssp_status() {
		return lssp_status;
	}
	public void setLssp_status(String lssp_status) {
		this.lssp_status = lssp_status;
	}
	// added by Andy
	private int mcl_blacklist;
	//===================
	
	public int getMcl_blacklist() {
		return mcl_blacklist;
	}
	public void setMcl_blacklist(int mcl_blacklist) {
		this.mcl_blacklist = mcl_blacklist;
	}
	public String getLus_id() {
		return lus_id;
	}
	public void setLus_id(String lus_id) {
		this.lus_id = lus_id;
	}
	public Date getMspo_date_print() {
		return mspo_date_print;
	}
	public void setMspo_date_print(Date mspo_date_print) {
		this.mspo_date_print = mspo_date_print;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getMspo_policy_no_format() {
		return mspo_policy_no_format;
	}
	public void setMspo_policy_no_format(String mspo_policy_no_format) {
		this.mspo_policy_no_format = mspo_policy_no_format;
	}
	public String getDistribusi() {
		return distribusi;
	}
	public void setDistribusi(String distribusi) {
		this.distribusi = distribusi;
	}
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}
	public String getLspd_position() {
		return lspd_position;
	}
	public void setLspd_position(String lspd_position) {
		this.lspd_position = lspd_position;
	}
	public String getLsrg_nama() {
		return lsrg_nama;
	}
	public void setLsrg_nama(String lsrg_nama) {
		this.lsrg_nama = lsrg_nama;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public String getPp() {
		return pp;
	}
	public void setPp(String pp) {
		this.pp = pp;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getTt() {
		return tt;
	}
	public void setTt(String tt) {
		this.tt = tt;
	}
	public CariSpaj getCariSpaj() {
		return cariSpaj;
	}
	public void setCariSpaj(CariSpaj cariSpaj) {
		this.cariSpaj = cariSpaj;
	}
	public String getPerusahaan() {
		return perusahaan;
	}
	public void setPerusahaan(String perusahaan) {
		this.perusahaan = perusahaan;
	}
	public String getNik() {
		return nik;
	}
	public void setNik(String nik) {
		this.nik = nik;
	}
}
