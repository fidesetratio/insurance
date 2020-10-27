package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Model object untuk tabel EKA.MST_SPAJ dan relasinya,
 * digunakan di modul sistem kontrol spaj 
 * (package com.ekalife.elions.web.bas)
 * 
 * @author Yusuf Sutarko
 * @since Feb 23, 2007 (9:22:04 AM)
 */
public class Spaj implements Serializable {

	private static final long serialVersionUID = -2936496692289775621L;
	private Integer mss_jenis;                     
	private String lca_id;                         
	private Integer lsjs_id;                       
	private Integer msab_id;                       
	private Integer lus_id;
	private Integer mss_amount;                    
	private Integer modify_lus_id;                 
	private Date modify_dt;
	private Integer damage;
	private String lsjs_desc;
	private String lsjs_prefix;
	private String lus_login_name;
	private String lca_nama;
	
	//untuk brosur
	private Integer jn_brosur;
	
	public Integer getJn_brosur() {
		return jn_brosur;
	}

	public void setJn_brosur(Integer jn_brosur) {
		this.jn_brosur = jn_brosur;
	}

	private List<SpajDet> daftarSpajDet;
	
	public void newSpaj(Integer mss_jenis, String lca_id, Integer lsjs_id, Integer msab_id, Integer mss_amount, Integer modify_lus_id,Integer lus_id) {
		this.mss_jenis = mss_jenis;
		this.lca_id = lca_id;
		this.lsjs_id = lsjs_id;
		this.msab_id = msab_id;
		this.mss_amount = mss_amount;
		this.modify_lus_id = modify_lus_id;
		this.lus_id=lus_id;
	}
	

	public List<SpajDet> getDaftarSpajDet() {
		return daftarSpajDet;
	}

	public void setDaftarSpajDet(List<SpajDet> daftarSpajDet) {
		this.daftarSpajDet = daftarSpajDet;
	}

	public String getLsjs_desc() {
		return lsjs_desc;
	}

	public void setLsjs_desc(String lsjs_desc) {
		this.lsjs_desc = lsjs_desc;
	}

	public String getLsjs_prefix() {
		return lsjs_prefix;
	}

	public void setLsjs_prefix(String lsjs_prefix) {
		this.lsjs_prefix = lsjs_prefix;
	}

	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
	public Integer getLsjs_id() {
		return lsjs_id;
	}
	public void setLsjs_id(Integer lsjs_id) {
		this.lsjs_id = lsjs_id;
	}
	public Date getModify_dt() {
		return modify_dt;
	}
	public void setModify_dt(Date modify_dt) {
		this.modify_dt = modify_dt;
	}
	public Integer getModify_lus_id() {
		return modify_lus_id;
	}
	public void setModify_lus_id(Integer modify_lus_id) {
		this.modify_lus_id = modify_lus_id;
	}
	public Integer getMsab_id() {
		return msab_id;
	}
	public void setMsab_id(Integer msab_id) {
		this.msab_id = msab_id;
	}
	public Integer getMss_amount() {
		return mss_amount;
	}
	public void setMss_amount(Integer mss_amount) {
		this.mss_amount = mss_amount;
	}
	public Integer getMss_jenis() {
		return mss_jenis;
	}
	public void setMss_jenis(Integer mss_jenis) {
		this.mss_jenis = mss_jenis;
	}

	public Integer getDamage() {
		return damage;
	}

	public void setDamage(Integer damage) {
		this.damage = damage;
	}

	public String getLca_nama() {
		return lca_nama;
	}

	public void setLca_nama(String lca_nama) {
		this.lca_nama = lca_nama;
	}

	public String getLus_login_name() {
		return lus_login_name;
	}

	public void setLus_login_name(String lus_login_name) {
		this.lus_login_name = lus_login_name;
	}

	public Integer getLus_id() {
		return lus_id;
	}

	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}

	
}