package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Model Class untuk EKA.MST_CHECKLIST dan EKA.LST_CHECKLIST
 * @author Yusuf
 * @since Sep 25, 2008 (8:43:22 AM)
 */
public class Checklist implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -581270496001586999L;
	//EKA.LST_CHECKLIST
	public Integer lc_id;
	public Integer lc_parent_id;
	public Integer lc_urut;
	public String lc_nama;
	public String lc_desc;
	public Integer lc_lus_id;
	public Date lc_create_dt;
	public Integer lc_active;

	//EKA.MST_CHECKLIST
	public String reg_spaj;
	public Integer mc_no;
	public String mc_desc;
	public Integer lus_id_adm;
	public Integer lus_id_bancass;
	public Integer lus_id_uw;
	public Integer lus_id_print;
	public Integer lus_id_filling;
	public Date tgl_adm;
	public Date tgl_bancass;
	public Date tgl_uw;
	public Date tgl_print;
	public Date tgl_filling;
	public Integer flag_adm;
	public Integer flag_bancass;
	public Integer flag_uw;
	public Integer flag_print;
	public Integer flag_filling;
	
	//tambahan
	public Integer level;
	public String user_adm;
	public String user_bancass;
	public String user_uw;
	public String user_print;
	public String user_filling;
	
	public Integer getLc_id() {
		return lc_id;
	}
	public void setLc_id(Integer lc_id) {
		this.lc_id = lc_id;
	}
	public Integer getLc_parent_id() {
		return lc_parent_id;
	}
	public void setLc_parent_id(Integer lc_parent_id) {
		this.lc_parent_id = lc_parent_id;
	}
	public Integer getLc_urut() {
		return lc_urut;
	}
	public void setLc_urut(Integer lc_urut) {
		this.lc_urut = lc_urut;
	}
	public String getLc_nama() {
		return lc_nama;
	}
	public void setLc_nama(String lc_nama) {
		this.lc_nama = lc_nama;
	}
	public String getLc_desc() {
		return lc_desc;
	}
	public void setLc_desc(String lc_desc) {
		this.lc_desc = lc_desc;
	}
	public Integer getLc_lus_id() {
		return lc_lus_id;
	}
	public void setLc_lus_id(Integer lc_lus_id) {
		this.lc_lus_id = lc_lus_id;
	}
	public Date getLc_create_dt() {
		return lc_create_dt;
	}
	public void setLc_create_dt(Date lc_create_dt) {
		this.lc_create_dt = lc_create_dt;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getMc_no() {
		return mc_no;
	}
	public void setMc_no(Integer mc_no) {
		this.mc_no = mc_no;
	}
	public String getMc_desc() {
		return mc_desc;
	}
	public void setMc_desc(String mc_desc) {
		this.mc_desc = mc_desc;
	}
	public Integer getLus_id_adm() {
		return lus_id_adm;
	}
	public void setLus_id_adm(Integer lus_id_adm) {
		this.lus_id_adm = lus_id_adm;
	}
	public Integer getLus_id_bancass() {
		return lus_id_bancass;
	}
	public void setLus_id_bancass(Integer lus_id_bancass) {
		this.lus_id_bancass = lus_id_bancass;
	}
	public Integer getLus_id_uw() {
		return lus_id_uw;
	}
	public void setLus_id_uw(Integer lus_id_uw) {
		this.lus_id_uw = lus_id_uw;
	}
	public Integer getLus_id_print() {
		return lus_id_print;
	}
	public void setLus_id_print(Integer lus_id_print) {
		this.lus_id_print = lus_id_print;
	}
	public Integer getLus_id_filling() {
		return lus_id_filling;
	}
	public void setLus_id_filling(Integer lus_id_filling) {
		this.lus_id_filling = lus_id_filling;
	}
	public Date getTgl_adm() {
		return tgl_adm;
	}
	public void setTgl_adm(Date tgl_adm) {
		this.tgl_adm = tgl_adm;
	}
	public Date getTgl_bancass() {
		return tgl_bancass;
	}
	public void setTgl_bancass(Date tgl_bancass) {
		this.tgl_bancass = tgl_bancass;
	}
	public Date getTgl_uw() {
		return tgl_uw;
	}
	public void setTgl_uw(Date tgl_uw) {
		this.tgl_uw = tgl_uw;
	}
	public Date getTgl_print() {
		return tgl_print;
	}
	public void setTgl_print(Date tgl_print) {
		this.tgl_print = tgl_print;
	}
	public Date getTgl_filling() {
		return tgl_filling;
	}
	public void setTgl_filling(Date tgl_filling) {
		this.tgl_filling = tgl_filling;
	}
	public Integer getFlag_adm() {
		return flag_adm;
	}
	public void setFlag_adm(Integer flag_adm) {
		this.flag_adm = flag_adm;
	}
	public Integer getFlag_bancass() {
		return flag_bancass;
	}
	public void setFlag_bancass(Integer flag_bancass) {
		this.flag_bancass = flag_bancass;
	}
	public Integer getFlag_uw() {
		return flag_uw;
	}
	public void setFlag_uw(Integer flag_uw) {
		this.flag_uw = flag_uw;
	}
	public Integer getFlag_print() {
		return flag_print;
	}
	public void setFlag_print(Integer flag_print) {
		this.flag_print = flag_print;
	}
	public Integer getFlag_filling() {
		return flag_filling;
	}
	public void setFlag_filling(Integer flag_filling) {
		this.flag_filling = flag_filling;
	}
	public Integer getLc_active() {
		return lc_active;
	}
	public void setLc_active(Integer lc_active) {
		this.lc_active = lc_active;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getUser_adm() {
		return user_adm;
	}
	public void setUser_adm(String user_adm) {
		this.user_adm = user_adm;
	}
	public String getUser_bancass() {
		return user_bancass;
	}
	public void setUser_bancass(String user_bancass) {
		this.user_bancass = user_bancass;
	}
	public String getUser_uw() {
		return user_uw;
	}
	public void setUser_uw(String user_uw) {
		this.user_uw = user_uw;
	}
	public String getUser_print() {
		return user_print;
	}
	public void setUser_print(String user_print) {
		this.user_print = user_print;
	}
	public String getUser_filling() {
		return user_filling;
	}
	public void setUser_filling(String user_filling) {
		this.user_filling = user_filling;
	}

}