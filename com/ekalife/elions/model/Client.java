package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Client implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer lti_id;
	private String mcl_id_new;
	private String mcl_id;
	private Integer mcl_jenis;
	private String mcl_first;
	private Integer mcl_blacklist;
	private Date mcl_tgl_input;
	private Integer lus_id;
	private Integer mcl_data_salah;
	private String mcl_gelar;
	private Integer lside_id;
	private String mspe_no_identity;
	private Integer lsed_id;
	private String lgj_id;
	private String ljb_id;
	private String mpn_job_desc;
	private String mspe_mother;
	private Integer mspe_sex;
	private Date mspe_date_birth;
	private String mspe_date_birth2;
	private String mspe_place_birth;
	private Integer lsag_id;
	private String mspe_sts_mrt;
	private Integer lsne_id;
	private Integer mspe_lama_kerja;
	private String mspe_email;
	private String mkl_tujuan;
	private String mkl_penghasilan;
	private String mkl_pendanaan;
	private String mkl_kerja;
	private String mkl_industri;
	private String mkl_smbr_penghasilan;
	private String mcl_agama;
	private Integer mkl_red_flag;
	private Date mspe_no_identity_expired;
	
	public String getMcl_agama() {
		return mcl_agama;
	}
	public void setMcl_agama(String mcl_agama) {
		this.mcl_agama = mcl_agama;
	}
	public String getLgj_id() {
		return lgj_id;
	}
	public void setLgj_id(String lgj_id) {
		this.lgj_id = lgj_id;
	}
	public String getLjb_id() {
		return ljb_id;
	}
	public void setLjb_id(String ljb_id) {
		this.ljb_id = ljb_id;
	}
	public Integer getLsag_id() {
		return lsag_id;
	}
	public void setLsag_id(Integer lsag_id) {
		this.lsag_id = lsag_id;
	}
	public Integer getLsed_id() {
		return lsed_id;
	}
	public void setLsed_id(Integer lsed_id) {
		this.lsed_id = lsed_id;
	}
	public Integer getLside_id() {
		return lside_id;
	}
	public void setLside_id(Integer lside_id) {
		this.lside_id = lside_id;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public Integer getMcl_blacklist() {
		return mcl_blacklist;
	}
	public void setMcl_blacklist(Integer mcl_blacklist) {
		this.mcl_blacklist = mcl_blacklist;
	}
	public Integer getMcl_data_salah() {
		return mcl_data_salah;
	}
	public void setMcl_data_salah(Integer mcl_data_salah) {
		this.mcl_data_salah = mcl_data_salah;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public String getMcl_gelar() {
		return mcl_gelar;
	}
	public void setMcl_gelar(String mcl_gelar) {
		this.mcl_gelar = mcl_gelar;
	}
	public String getMcl_id() {
		return mcl_id;
	}
	public void setMcl_id(String mcl_id) {
		this.mcl_id = mcl_id;
	}
	public Integer getMcl_jenis() {
		return mcl_jenis;
	}
	public void setMcl_jenis(Integer mcl_jenis) {
		this.mcl_jenis = mcl_jenis;
	}
	public Date getMcl_tgl_input() {
		return mcl_tgl_input;
	}
	public void setMcl_tgl_input(Date mcl_tgl_input) {
		this.mcl_tgl_input = mcl_tgl_input;
	}
	public String getMkl_industri() {
		return mkl_industri;
	}
	public void setMkl_industri(String mkl_industri) {
		this.mkl_industri = mkl_industri;
	}
	public String getMkl_kerja() {
		return mkl_kerja;
	}
	public void setMkl_kerja(String mkl_kerja) {
		this.mkl_kerja = mkl_kerja;
	}
	public String getMkl_pendanaan() {
		return mkl_pendanaan;
	}
	public void setMkl_pendanaan(String mkl_pendanaan) {
		this.mkl_pendanaan = mkl_pendanaan;
	}
	public String getMkl_penghasilan() {
		return mkl_penghasilan;
	}
	public void setMkl_penghasilan(String mkl_penghasilan) {
		this.mkl_penghasilan = mkl_penghasilan;
	}
	public String getMkl_tujuan() {
		return mkl_tujuan;
	}
	public void setMkl_tujuan(String mkl_tujuan) {
		this.mkl_tujuan = mkl_tujuan;
	}
	public String getMpn_job_desc() {
		return mpn_job_desc;
	}
	public void setMpn_job_desc(String mpn_job_desc) {
		this.mpn_job_desc = mpn_job_desc;
	}
	public Date getMspe_date_birth() {
		return mspe_date_birth;
	}
	public void setMspe_date_birth(Date mspe_date_birth) {
		this.mspe_date_birth = mspe_date_birth;
	}
	public String getMspe_email() {
		return mspe_email;
	}
	public void setMspe_email(String mspe_email) {
		this.mspe_email = mspe_email;
	}
	public Integer getMspe_lama_kerja() {
		return mspe_lama_kerja;
	}
	public void setMspe_lama_kerja(Integer mspe_lama_kerja) {
		this.mspe_lama_kerja = mspe_lama_kerja;
	}
	public String getMspe_mother() {
		return mspe_mother;
	}
	public void setMspe_mother(String mspe_mother) {
		this.mspe_mother = mspe_mother;
	}
	public String getMspe_no_identity() {
		return mspe_no_identity;
	}
	public void setMspe_no_identity(String mspe_no_identity) {
		this.mspe_no_identity = mspe_no_identity;
	}
	public Integer getMspe_sex() {
		return mspe_sex;
	}
	public void setMspe_sex(Integer mspe_sex) {
		this.mspe_sex = mspe_sex;
	}
	public String getMspe_sts_mrt() {
		return mspe_sts_mrt;
	}
	public void setMspe_sts_mrt(String mspe_sts_mrt) {
		this.mspe_sts_mrt = mspe_sts_mrt;
	}
	public String getMcl_id_new() {
		return mcl_id_new;
	}
	public void setMcl_id_new(String mcl_id_new) {
		this.mcl_id_new = mcl_id_new;
	}
	public String getMspe_place_birth() {
		return mspe_place_birth;
	}
	public void setMspe_place_birth(String mspe_place_birth) {
		this.mspe_place_birth = mspe_place_birth;
	}
	public Integer getLsne_id() {
		return lsne_id;
	}
	public void setLsne_id(Integer lsne_id) {
		this.lsne_id = lsne_id;
	}

	public String getMkl_smbr_penghasilan() {
		return mkl_smbr_penghasilan;
	}
	public void setMkl_smbr_penghasilan(String mkl_smbr_penghasilan) {
		this.mkl_smbr_penghasilan = mkl_smbr_penghasilan;
	}
	public Integer getLti_id() {
		return lti_id;
	}
	public void setLti_id(Integer lti_id) {
		this.lti_id = lti_id;
	}
	public String getMspe_date_birth2() {
		return mspe_date_birth2;
	}
	public void setMspe_date_birth2(String mspe_date_birth2) {
		this.mspe_date_birth2 = mspe_date_birth2;
	}
	
	public Integer getMkl_red_flag() {
		return mkl_red_flag;
	}
	public void setMkl_red_flag(Integer mkl_red_flag) {
		this.mkl_red_flag = mkl_red_flag;
	}
	public Date getMspe_no_identity_expired() {
		return mspe_no_identity_expired;
	}
	public void setMspe_no_identity_expired(Date mspe_no_identity_expired) {
		this.mspe_no_identity_expired = mspe_no_identity_expired;
	}
}
