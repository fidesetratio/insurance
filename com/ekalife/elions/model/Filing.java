package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Filing implements Serializable {
	
	private static final long serialVersionUID = -1692108994181610866L;
	private String reg_spaj;
	private String mspo_policy_no;
	private String mcl_first;
	private String pemegang_polis;
	private String tertanggung;
	private Date mspo_beg_date;
	private Date mspo_end_date;
	private Date mste_beg_date;
	private Date mste_end_date;
	private Integer lsbs_id;
	private Integer lsdbs_number;
	private Integer lspd_id;
	private String lspd_position;
	private String lku_id;
	private String lku_symbol;
	private String lsdbs_name;
	private Double mspr_tsi;
	private Double mspr_premium;
	private String kd_box;                         
	private String kd_bundle;                      
	private String nama_bundle;                    
	private Date tgl_created;                      
	private String request_by;                     
	private String sts_dokumen;                    
	private String pos_dokumen;                    
	private Date tgl_aktif;                        
	private String user_id;
	private String kd_file;                        
	private String tipe_file;                      
	private String noref_file;                     
	private String insured;                        
	private Date b_date;                           
	private Date e_date;                           
	private Date tgl_destroyed_plan;               
	private Date tgl_destroyed;
	private String flag_tgl_destroyed;
	private String note;
	private String no_index;
	private String success;
	
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getNo_index() {
		return no_index;
	}
	public void setNo_index(String no_index) {
		this.no_index = no_index;
	}
	public Date getB_date() {
		return b_date;
	}
	public void setB_date(Date b_date) {
		this.b_date = b_date;
	}
	public Date getE_date() {
		return e_date;
	}
	public void setE_date(Date e_date) {
		this.e_date = e_date;
	}
	public String getInsured() {
		return insured;
	}
	public void setInsured(String insured) {
		this.insured = insured;
	}
	public String getKd_box() {
		return kd_box;
	}
	public void setKd_box(String kd_box) {
		this.kd_box = kd_box;
	}
	public String getKd_bundle() {
		return kd_bundle;
	}
	public void setKd_bundle(String kd_bundle) {
		this.kd_bundle = kd_bundle;
	}
	public String getKd_file() {
		return kd_file;
	}
	public void setKd_file(String kd_file) {
		this.kd_file = kd_file;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public String getLku_symbol() {
		return lku_symbol;
	}
	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	public Integer getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}
	public String getLspd_position() {
		return lspd_position;
	}
	public void setLspd_position(String lspd_position) {
		this.lspd_position = lspd_position;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public Date getMspo_beg_date() {
		return mspo_beg_date;
	}
	public void setMspo_beg_date(Date mspo_beg_date) {
		this.mspo_beg_date = mspo_beg_date;
	}
	public Date getMspo_end_date() {
		return mspo_end_date;
	}
	public void setMspo_end_date(Date mspo_end_date) {
		this.mspo_end_date = mspo_end_date;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public Double getMspr_premium() {
		return mspr_premium;
	}
	public void setMspr_premium(Double mspr_premium) {
		this.mspr_premium = mspr_premium;
	}
	public Double getMspr_tsi() {
		return mspr_tsi;
	}
	public void setMspr_tsi(Double mspr_tsi) {
		this.mspr_tsi = mspr_tsi;
	}
	public String getNama_bundle() {
		return nama_bundle;
	}
	public void setNama_bundle(String nama_bundle) {
		this.nama_bundle = nama_bundle;
	}
	public String getNoref_file() {
		return noref_file;
	}
	public void setNoref_file(String noref_file) {
		this.noref_file = noref_file;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getPemegang_polis() {
		return pemegang_polis;
	}
	public void setPemegang_polis(String pemegang_polis) {
		this.pemegang_polis = pemegang_polis;
	}
	public String getPos_dokumen() {
		return pos_dokumen;
	}
	public void setPos_dokumen(String pos_dokumen) {
		this.pos_dokumen = pos_dokumen;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getRequest_by() {
		return request_by;
	}
	public void setRequest_by(String request_by) {
		this.request_by = request_by;
	}
	public String getSts_dokumen() {
		return sts_dokumen;
	}
	public void setSts_dokumen(String sts_dokumen) {
		this.sts_dokumen = sts_dokumen;
	}
	public String getTertanggung() {
		return tertanggung;
	}
	public void setTertanggung(String tertanggung) {
		this.tertanggung = tertanggung;
	}
	public Date getTgl_aktif() {
		return tgl_aktif;
	}
	public void setTgl_aktif(Date tgl_aktif) {
		this.tgl_aktif = tgl_aktif;
	}
	public Date getTgl_created() {
		return tgl_created;
	}
	public void setTgl_created(Date tgl_created) {
		this.tgl_created = tgl_created;
	}
	public Date getTgl_destroyed() {
		return tgl_destroyed;
	}
	public void setTgl_destroyed(Date tgl_destroyed) {
		this.tgl_destroyed = tgl_destroyed;
	}
	public Date getTgl_destroyed_plan() {
		return tgl_destroyed_plan;
	}
	public void setTgl_destroyed_plan(Date tgl_destroyed_plan) {
		this.tgl_destroyed_plan = tgl_destroyed_plan;
	}
	public String getTipe_file() {
		return tipe_file;
	}
	public void setTipe_file(String tipe_file) {
		this.tipe_file = tipe_file;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public Date getMste_beg_date() {
		return mste_beg_date;
	}
	public void setMste_beg_date(Date mste_beg_date) {
		this.mste_beg_date = mste_beg_date;
	}
	public Date getMste_end_date() {
		return mste_end_date;
	}
	public void setMste_end_date(Date mste_end_date) {
		this.mste_end_date = mste_end_date;
	}
	public String getFlag_tgl_destroyed() {
		return flag_tgl_destroyed;
	}
	public void setFlag_tgl_destroyed(String flag_tgl_destroyed) {
		this.flag_tgl_destroyed = flag_tgl_destroyed;
	}
	
	
}