package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Model class untuk detail dari mst_spaj_det (child dari mst_spaj), digunakan untuk informasi per nomor blanko
 * 
 * @author Yusuf Sutarko
 * @since Mar 31, 2007 (2:51:50 PM)
 */
public class SpajDet implements Serializable {

	private static final long serialVersionUID = -5037917664233141121L;
	private String lsjs_prefix;                    
	private String lsjs_desc;                      
	private String lsp_jenis; 
	
	private String msf_id;
	private String msf_id_asli;
	private Integer mss_jenis;
	private String lca_id;
	private Integer lsjs_id;
	private Integer msab_id;
	private Integer lsp_id;
	private String no_blanko;
	private String mssd_desc;
	private Integer mssd_lus_id;
	private Date mssd_dt;
	private String reg_spaj;
	private String mspo_policy_no;
	private Integer check;
	private String msf_id_bef;
	private Integer flag_bef;
	
	public Integer getFlag_bef() {
		return flag_bef;
	}
	public void setFlag_bef(Integer flag_bef) {
		this.flag_bef = flag_bef;
	}
	public String getMsf_id_bef() {
		return msf_id_bef;
	}
	public void setMsf_id_bef(String msf_id_bef) {
		this.msf_id_bef = msf_id_bef;
	}
	public Integer getCheck() {
		return check;
	}
	public void setCheck(Integer check) {
		this.check = check;
	}

	public SpajDet(String msf_id, String msf_id_asli, String msf_id_bef,Integer mss_jenis, String lca_id, Integer lsjs_id, Integer msab_id, Integer lsp_id, String no_blanko, String mssd_desc, Integer mssd_lus_id, Integer flag_bef) {
		this.msf_id = msf_id;
		this.msf_id_asli = msf_id_asli;
		this.mss_jenis = mss_jenis;
		this.lca_id = lca_id;
		this.lsjs_id = lsjs_id;
		this.msab_id = msab_id;
		this.lsp_id = lsp_id; 
		this.no_blanko = no_blanko;
		this.mssd_desc = mssd_desc;
		this.mssd_lus_id = mssd_lus_id;
		this.msf_id_bef=msf_id_bef;
		this.flag_bef=flag_bef;
	}	
	
	public SpajDet() {	}
	
	public String getMsf_id_asli() {
		return msf_id_asli;
	}
	public void setMsf_id_asli(String msf_id_asli) {
		this.msf_id_asli = msf_id_asli;
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
	public String getLsp_jenis() {
		return lsp_jenis;
	}
	public void setLsp_jenis(String lsp_jenis) {
		this.lsp_jenis = lsp_jenis;
	}
	public String getMsf_id() {
		return msf_id;
	}
	public void setMsf_id(String msf_id) {
		this.msf_id = msf_id;
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
	public Integer getLsp_id() {
		return lsp_id;
	}
	public void setLsp_id(Integer lsp_id) {
		this.lsp_id = lsp_id;
	}
	public Integer getMsab_id() {
		return msab_id;
	}
	public void setMsab_id(Integer msab_id) {
		this.msab_id = msab_id;
	}
	public Integer getMss_jenis() {
		return mss_jenis;
	}
	public void setMss_jenis(Integer mss_jenis) {
		this.mss_jenis = mss_jenis;
	}
	public String getMssd_desc() {
		return mssd_desc;
	}
	public void setMssd_desc(String mssd_desc) {
		this.mssd_desc = mssd_desc;
	}
	public Date getMssd_dt() {
		return mssd_dt;
	}
	public void setMssd_dt(Date mssd_dt) {
		this.mssd_dt = mssd_dt;
	}
	public Integer getMssd_lus_id() {
		return mssd_lus_id;
	}
	public void setMssd_lus_id(Integer mssd_lus_id) {
		this.mssd_lus_id = mssd_lus_id;
	}
	public String getNo_blanko() {
		return no_blanko;
	}
	public void setNo_blanko(String no_blanko) {
		this.no_blanko = no_blanko;
	}
	
}