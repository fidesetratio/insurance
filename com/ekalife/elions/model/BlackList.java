package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class BlackList implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer lbl_id;
	private String lbl_nama;
	private String lbl_tempat;
	private Date lbl_tgl_lahir;
	private String lbl_tgl_lahir2;
	private String lbl_alamat;
	private String lbl_asuransi;
	private String lbl_flag_alasan;
	private String lbl_alasan;
	private Date lbl_tgl_input;
	private Integer lus_id;
	private String lus_login_name;
	//added by Andy==============
	private String mcl_id;
	private String lbl_nama_alias_1;
	private String lbl_nama_alias_2;
	private String lbl_nama_alias_3;
	private String lbl_nama_alias_4;
	private String lbl_nama_alias_5;
	private String lbl_no_identity;
	private String lbl_no_identity_sim;
	private String lbl_no_identity_paspor;
	private String lbl_no_identity_kk;
	private String lbl_no_identity_akte_lahir;
	private String lbl_no_identity_kims_kitas;
	private String lbl_sumber_info;
	private String lbl_sumber_info2;
	private String lbl_sumber_informasi;
	private Date lbl_tgl_kejadian;
	private Date lbl_tgl_kejadian_to;
	private String lbl_tgl_kejadian2;
	private String lbl_tgl_kejadian_to2;
	private String lbl_diagnosa;
	private String lbl_sts_cust;
	private String reg_spaj;
	private String lbl_nb_process;
	private String telepon;
	private String no_policy;

	//===========================
		
	public String getLus_login_name() {
		return lus_login_name;
	}
	public void setLus_login_name(String lus_login_name) {
		this.lus_login_name = lus_login_name;
	}
	public String getLbl_alamat() {
		return lbl_alamat;
	}
	public void setLbl_alamat(String lbl_alamat) {
		this.lbl_alamat = lbl_alamat;
	}
	public String getLbl_alasan() {
		return lbl_alasan;
	}
	public void setLbl_alasan(String lbl_alasan) {
		this.lbl_alasan = lbl_alasan;
	}
	public String getLbl_asuransi() {
		return lbl_asuransi;
	}
	public void setLbl_asuransi(String lbl_asuransi) {
		this.lbl_asuransi = lbl_asuransi;
	}
	public Integer getLbl_id() {
		return lbl_id;
	}
	public void setLbl_id(Integer lbl_id) {
		this.lbl_id = lbl_id;
	}
	public String getLbl_nama() {
		return lbl_nama;
	}
	public void setLbl_nama(String lbl_nama) {
		this.lbl_nama = lbl_nama;
	}
	public String getLbl_tempat() {
		return lbl_tempat;
	}
	public void setLbl_tempat(String lbl_tempat) {
		this.lbl_tempat = lbl_tempat;
	}
	public Date getLbl_tgl_input() {
		return lbl_tgl_input;
	}
	public void setLbl_tgl_input(Date lbl_tgl_input) {
		this.lbl_tgl_input = lbl_tgl_input;
	}
	public Date getLbl_tgl_lahir() {
		return lbl_tgl_lahir;
	}
	public void setLbl_tgl_lahir(Date lbl_tgl_lahir) {
		this.lbl_tgl_lahir = lbl_tgl_lahir;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getLbl_diagnosa() {
		return lbl_diagnosa;
	}
	public void setLbl_diagnosa(String lbl_diagnosa) {
		this.lbl_diagnosa = lbl_diagnosa;
	}
	public Date getLbl_tgl_kejadian() {
		return lbl_tgl_kejadian;
	}
	public void setLbl_tgl_kejadian(Date lbl_tgl_kejadian) {
		this.lbl_tgl_kejadian = lbl_tgl_kejadian;
	}
	public String getLbl_sumber_info() {
		return lbl_sumber_info;
	}
	public void setLbl_sumber_info(String lbl_sumber_info) {
		this.lbl_sumber_info = lbl_sumber_info;
	}
	public String getLbl_sumber_informasi() {
		return lbl_sumber_informasi;
	}
	public void setLbl_sumber_informasi(String lbl_sumber_informasi) {
		this.lbl_sumber_informasi = lbl_sumber_informasi;
	}
	public String getLbl_sts_cust() {
		return lbl_sts_cust;
	}
	public void setLbl_sts_cust(String lbl_sts_cust) {
		this.lbl_sts_cust = lbl_sts_cust;
	}
	public String getMcl_id() {
		return mcl_id;
	}
	public void setMcl_id(String mcl_id) {
		this.mcl_id = mcl_id;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getLbl_tgl_kejadian2() {
		return lbl_tgl_kejadian2;
	}
	public void setLbl_tgl_kejadian2(String lbl_tgl_kejadian2) {
		this.lbl_tgl_kejadian2 = lbl_tgl_kejadian2;
	}
	public String getLbl_tgl_lahir2() {
		return lbl_tgl_lahir2;
	}
	public void setLbl_tgl_lahir2(String lbl_tgl_lahir2) {
		this.lbl_tgl_lahir2 = lbl_tgl_lahir2;
	}
	public String getLbl_nb_process() {
		return lbl_nb_process;
	}
	public void setLbl_nb_process(String lbl_nb_process) {
		this.lbl_nb_process = lbl_nb_process;
	}
	public Date getLbl_tgl_kejadian_to() {
		return lbl_tgl_kejadian_to;
	}
	public void setLbl_tgl_kejadian_to(Date lbl_tgl_kejadian_to) {
		this.lbl_tgl_kejadian_to = lbl_tgl_kejadian_to;
	}
	public String getLbl_tgl_kejadian_to2() {
		return lbl_tgl_kejadian_to2;
	}
	public void setLbl_tgl_kejadian_to2(String lbl_tgl_kejadian_to2) {
		this.lbl_tgl_kejadian_to2 = lbl_tgl_kejadian_to2;
	}
	public String getLbl_sumber_info2() {
		return lbl_sumber_info2;
	}
	public void setLbl_sumber_info2(String lbl_sumber_info2) {
		this.lbl_sumber_info2 = lbl_sumber_info2;
	}
	public String getLbl_no_identity() {
		return lbl_no_identity;
	}
	public void setLbl_no_identity(String lbl_no_identity) {
		this.lbl_no_identity = lbl_no_identity;
	}
	public String getLbl_nama_alias_1() {
		return lbl_nama_alias_1;
	}
	public void setLbl_nama_alias_1(String lbl_nama_alias_1) {
		this.lbl_nama_alias_1 = lbl_nama_alias_1;
	}
	public String getLbl_nama_alias_2() {
		return lbl_nama_alias_2;
	}
	public void setLbl_nama_alias_2(String lbl_nama_alias_2) {
		this.lbl_nama_alias_2 = lbl_nama_alias_2;
	}
	public String getLbl_nama_alias_3() {
		return lbl_nama_alias_3;
	}
	public void setLbl_nama_alias_3(String lbl_nama_alias_3) {
		this.lbl_nama_alias_3 = lbl_nama_alias_3;
	}
	public String getLbl_nama_alias_4() {
		return lbl_nama_alias_4;
	}
	public void setLbl_nama_alias_4(String lbl_nama_alias_4) {
		this.lbl_nama_alias_4 = lbl_nama_alias_4;
	}
	public String getLbl_nama_alias_5() {
		return lbl_nama_alias_5;
	}
	public void setLbl_nama_alias_5(String lbl_nama_alias_5) {
		this.lbl_nama_alias_5 = lbl_nama_alias_5;
	}
	public String getLbl_no_identity_akte_lahir() {
		return lbl_no_identity_akte_lahir;
	}
	public void setLbl_no_identity_akte_lahir(String lbl_no_identity_akte_lahir) {
		this.lbl_no_identity_akte_lahir = lbl_no_identity_akte_lahir;
	}
	public String getLbl_no_identity_kims_kitas() {
		return lbl_no_identity_kims_kitas;
	}
	public void setLbl_no_identity_kims_kitas(String lbl_no_identity_kims_kitas) {
		this.lbl_no_identity_kims_kitas = lbl_no_identity_kims_kitas;
	}
	public String getLbl_no_identity_paspor() {
		return lbl_no_identity_paspor;
	}
	public void setLbl_no_identity_paspor(String lbl_no_identity_paspor) {
		this.lbl_no_identity_paspor = lbl_no_identity_paspor;
	}
	public String getLbl_no_identity_sim() {
		return lbl_no_identity_sim;
	}
	public void setLbl_no_identity_sim(String lbl_no_identity_sim) {
		this.lbl_no_identity_sim = lbl_no_identity_sim;
	}
	public String getLbl_no_identity_kk() {
		return lbl_no_identity_kk;
	}
	public void setLbl_no_identity_kk(String lbl_no_identity_kk) {
		this.lbl_no_identity_kk = lbl_no_identity_kk;
	}
	public String getTelepon() {
		return telepon;
	}
	public void setTelepon(String telepon) {
		this.telepon = telepon;
	}
	public String getNo_policy() {
		return no_policy;
	}
	public void setNo_policy(String noPolicy) {
		no_policy = noPolicy;
	}
	public String getLbl_flag_alasan() {
		return lbl_flag_alasan;
	}
	public void setLbl_flag_alasan(String lblFlagAlasan) {
		lbl_flag_alasan = lblFlagAlasan;
	}
		

	
	
}
