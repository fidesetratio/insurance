package com.ekalife.elions.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
public class Pas implements Serializable {
	
	protected final Log logger = LogFactory.getLog( getClass() );
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2032926849885830566L;
	private String msp_id;
	private String msp_fire_id;
	private String msp_kode_sts_sms;
	private String msp_desc_sts_sms;
	private String msp_mobile;
	private String msp_mobile2;
	private String msp_text;
	private String msp_pas_phone_number;
	private Date msp_message_date;
	private Date msp_receive_date;
	private String msp_full_name;
	private String jenis_pas;
	
	private String mcl_id_pp;
	private String mcl_id_tt;
	
	private String msp_pas_nama_pp;
	private String msp_pas_tmp_lhr_pp;
	private String msp_pas_email;
	private Integer lsre_id;
	private Integer msp_sex_pp;
	private Integer msp_sex_tt;
	private Integer msp_warga;
	private String msp_warga_else;
	private Integer msp_status_nikah;
	private String msp_agama;
	private Integer lsag_id;
	private Integer msp_pendidikan;
	private String msp_pendidikan_else;
	private String msp_occupation;
	private String msp_occupation_else;
	private String msp_company_name;
	private Integer msp_company_usaha;
	private Integer lju_id;
	private String msp_company_jabatan;
	private String msp_company_address;
	private String msp_company_postal_code;
	private String msp_company_area_code;
	private String msp_company_phone_number;
	private Integer msp_cek_ktp;
	private Integer msp_cek_kk;
	private Integer msp_cek_npwp;
	private Integer msp_cek_bukti_bayar;
	private Integer msp_cek_srt_keterangan;
	private Integer msp_cek_rekening;
	private Integer msp_cek_ktp_uw;
	private Integer msp_cek_kk_uw;
	private Integer msp_cek_npwp_uw;
	private Integer msp_cek_bukti_bayar_uw;
	private Integer msp_cek_srt_keterangan_uw;
	private Integer msp_cek_rekening_uw;
	private Integer msp_exist_bp;
	private Integer msp_still_bp;
	private Integer msp_partner_exist_bp;
	private Integer msp_partner_still_bp;
	private String msp_exist_bp_name;
	private String msp_area_code_rumah;
	private Integer lside_id;
	private String msp_pas_tmp_lhr_tt;
	private Date msp_pas_dob_pp;
	private String msp_pas_dob_pp2;
	private Integer flag_val_send;
	private Integer msp_cek_akte_kelahiran;
	private Integer msp_cek_akte_kelahiran_uw;
	private Integer msp_cek_tandatangan;
	private Integer msp_cek_tandatangan_uw;
	
	private String branch_admin_name;
	
	private String msp_identity_no;
	private String msp_identity_no_tt;
	private Date msp_date_of_birth;
	private String msp_date_of_birth2;
	private String msp_address_1;
	private String msp_address_2;
	private String msp_city;
	private String msp_postal_code;
	private String dist;
	private String pin;
	private String no_kartu;
	private Integer msp_age;
	private Integer msp_fire_export_flag;
	private Date msp_fire_export_date;
	private Integer lspd_id;
	private Integer lssa_id;
	private Integer lssp_id;
	private Integer msp_flag_pas;
	private Date msp_pas_accept_date;
	private Date msp_pas_beg_date;
	private String msp_pas_beg_date2;
	private Date msp_pas_end_date;
	private String msp_pas_end_date2;
	private Date msp_admin_date;
	private Date msp_fire_accept_date;
	private Date msp_fire_beg_date;
	private Date msp_fire_end_date;
	private String msp_fire_policy_no;
	private String msp_fire_code_name;
	private String msp_fire_insured_city;
	private String msp_fire_insured_phone_number;
	private String msp_fire_name;
	private String msp_fire_identity;
	private String msp_fire_occupation;
	private String msp_fire_occupation2;
	private String msp_fire_type_business;
	private String msp_fire_type_business2;
	private String msp_fire_source_fund;
	private String msp_fire_source_fund2;
	private String msp_fire_address_1;
	private String msp_fire_addr_code;
	private String msp_fire_phone_number;
	private String msp_fire_email;
	private String msp_fire_okupasi;
	private String msp_fire_okupasi_else;
	private String msp_fire_postal_code;
	private String msp_fire_mobile;
	private String msp_fire_insured_addr_code;
	private String msp_fire_insured_addr;
	private String msp_fire_insured_postal_code;
	private String msp_fire_insured_addr_no;
	private String msp_fire_fail_desc;
	private Integer msp_fire_insured_addr_envir;
	private String msp_fire_ins_addr_envir_else;
	private Date  msp_fire_date_of_birth ;
	private Integer msp_fire_jenis;
	private Integer lscb_id;
	private Integer msp_flag_aksep;
	private Integer msp_flag_cc;
	private Integer produk;
	private String product_code;
	
	private String nama_produk;
	private String premi;
	private String msp_premi;
	private String msp_up;
	private String mspo_plan_provider;
	
	//agent=============
	private String msag_id;
	private Integer lrb_id;
	private String msag_id_pp;
	private String kode_ao;
	private Integer pribadi;
	private String lca_id;
	private String lsrg_nama;
	private String mcl_first;
	private String nama_agent;
	//===================

	private String msp_fire_date_of_birth2; 
	private Integer reff_bii_flag;
	
	private String msp_no_rekening;
	private String msp_no_rekening_autodebet;
	private String cabang;
	private Date msp_tgl_debet;
	private String msp_tgl_debet2;
	private Date msp_tgl_valid;
	private String msp_tgl_valid2;
	
	private Date msp_fire_create_date;
	private Date msp_pas_create_date;
	private String msp_pas_create_date2;
	
	private String lsbp_id;
	private String lsbp_id_autodebet;
	private String lsbp_nama;
	private String lsbp_nama_autodebet;
	private String msp_rek_nama;
	private String msp_rek_nama_autodebet;
	private String msp_rek_cabang;
	private String msp_rek_kota;
	private Integer lus_id;
	private Integer lus_id_uw_pas;
	private Integer lus_id_uw_fire;
	private Integer lus_id_uw_batal;
	private String mspo_policy_no;
	private String lus_login_name;
	private String reg_spaj;
	private String status_polis;
	//===========================
	
	private Integer status;
	private Integer msp_status;
	private String msp_ket_status;
	private Date msp_tgl_status;
	private String msp_tgl_status2;
	
	private String emailAgen;
	private String emailAdmin;
	
	private String emailTo;
	private String emailCc;
	
	private Integer dirAgenBp;
	private String msp_ket_batal;
	private Date msp_tgl_batal;
	private Date msp_fire_fail_date;
	private Date msp_tgl_transfer;
	
	private String msp_company_email;
	private String msp_company_fax;
	private String msp_company_ijin_usaha;
	
	private String msp_kecamatan;
	private String msp_propinsi;
	private String msp_pas_npwp;
	private String msp_rt;
	private String msp_rw;
	
	private Integer jenis_dbd;
	private Integer jenis_bp;
	
	private String no_sertifikat;
	private String jenisQuery;
	
	//untuk Referensi
	private String id_ref;
	private String pesan;
	private String nama_ref;
	private Integer jenis_ref;
	
	private String input_type;
	private String application_id;
	private String tm_id;
	private String spv_id;
	private Integer flag_kpr; //tambah kolom baru. helpdesk[132502] //chandra
	
	private Integer panjang_no_rek;
	
	//Referral bsm
	private Integer reff_id;
	private String nama_reff;
	private String lcb_no;
	private String lcb_reff;
	// No VA
	private String no_va;
	// Flag kartu baru
	private Integer new_card;
	
	private String cf_job_code;
	private String cf_customer_id;
	private String cf_campaign_code;
	
	private String cif;
	
	//Beneficiary
	private String msaw_first1; 
	private String lsre_relation1;
	private Double msaw_persen1;  
	private String msaw_first2; 
	private String lsre_relation2;
	private Double msaw_persen2;  
	private String msaw_first3; 
	private String lsre_relation3;
	private Double msaw_persen3;
	private String msaw_first4; 
	private String lsre_relation4;
	private Double msaw_persen4;
	private String mspe_mother; 
	
	public String getCf_job_code() {
		return cf_job_code;
	}
	public void setCf_job_code(String cf_job_code) {
		this.cf_job_code = cf_job_code;
	}
	public String getCf_customer_id() {
		return cf_customer_id;
	}
	public void setCf_customer_id(String cf_customer_id) {
		this.cf_customer_id = cf_customer_id;
	}
	public String getCf_campaign_code() {
		return cf_campaign_code;
	}
	public void setCf_campaign_code(String cf_campaign_code) {
		this.cf_campaign_code = cf_campaign_code;
	}
	public String getInput_type() {
		return input_type;
	}
	public void setInput_type(String input_type) {
		this.input_type = input_type;
	}
	public String getId_ref() {
		return id_ref;
	}
	public void setId_ref(String id_ref) {
		this.id_ref = id_ref;
	}
	public String getNama_ref() {
		return nama_ref;
	}
	public void setNama_ref(String nama_ref) {
		this.nama_ref = nama_ref;
	}
	public Integer getJenis_ref() {
		return jenis_ref;
	}
	public void setJenis_ref(Integer jenis_ref) {
		this.jenis_ref = jenis_ref;
	}
	public String getPesan() {
		return pesan;
	}
	public void setPesan(String pesan) {
		this.pesan = pesan;
	}
	public String getJenisQuery() {
		return jenisQuery;
	}
	public void setJenisQuery(String jenisQuery) {
		this.jenisQuery = jenisQuery;
	}
	public Integer getJenis_dbd() {
		return jenis_dbd;
	}
	public void setJenis_dbd(Integer jenis_dbd) {
		this.jenis_dbd = jenis_dbd;
	}
	public String getEmailAgen() {
		return emailAgen;
	}
	public void setEmailAgen(String emailAgen) {
		this.emailAgen = emailAgen;
	}
	public String getEmailAdmin() {
		return emailAdmin;
	}
	public void setEmailAdmin(String emailAdmin) {
		this.emailAdmin = emailAdmin;
	}
	public String getStatus_polis() {
		return status_polis;
	}
	public void setStatus_polis(String statusPolis) {
		status_polis = statusPolis;
	}
	public Date getMsp_fire_create_date() {
		return msp_fire_create_date;
	}
	public void setMsp_fire_create_date(Date mspFireCreateDate) {
		msp_fire_create_date = mspFireCreateDate;
	}
	public Date getMsp_pas_create_date() {
		return msp_pas_create_date;
	}
	public void setMsp_pas_create_date(Date mspPasCreateDate) {
		msp_pas_create_date = mspPasCreateDate;
	}
	
	public String getLsbp_nama_autodebet() {
		return lsbp_nama_autodebet;
	}
	public void setLsbp_nama_autodebet(String lsbpNamaAutodebet) {
		lsbp_nama_autodebet = lsbpNamaAutodebet;
	}
	public String getMsp_no_rekening_autodebet() {
		return msp_no_rekening_autodebet;
	}
	public void setMsp_no_rekening_autodebet(String mspNoRekeningAutodebet) {
		msp_no_rekening_autodebet = mspNoRekeningAutodebet;
	}
	public Date getMsp_tgl_debet() {
		return msp_tgl_debet;
	}
	public void setMsp_tgl_debet(Date mspTglDebet) {
		this.msp_tgl_debet = mspTglDebet;
		if(mspTglDebet != null){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				msp_tgl_debet2 = sdf.format(mspTglDebet);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			} 
		}
	}
	public String getMsp_tgl_debet2() {
		return msp_tgl_debet2;
	}
	public void setMsp_tgl_debet2(String mspTglDebet2) {
		msp_tgl_debet2 = mspTglDebet2;
	}
	public Date getMsp_tgl_valid() {
		return msp_tgl_valid;
	}
	public void setMsp_tgl_valid(Date mspTglValid) {
		this.msp_tgl_valid = mspTglValid;
		if(mspTglValid != null){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				msp_tgl_valid2 = sdf.format(mspTglValid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			} 
		}
	}
	public String getMsp_tgl_valid2() {
		return msp_tgl_valid2;
	}
	public void setMsp_tgl_valid2(String mspTglValid2) {
		msp_tgl_valid2 = mspTglValid2;
	}
	public String getLsbp_id_autodebet() {
		return lsbp_id_autodebet;
	}
	public void setLsbp_id_autodebet(String lsbpIdAutodebet) {
		lsbp_id_autodebet = lsbpIdAutodebet;
	}
	public String getMsp_rek_nama_autodebet() {
		return msp_rek_nama_autodebet;
	}
	public void setMsp_rek_nama_autodebet(String mspRekNamaAutodebet) {
		msp_rek_nama_autodebet = mspRekNamaAutodebet;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getMsp_fire_export_date() {
		return msp_fire_export_date;
	}
	public void setMsp_fire_export_date(Date mspFireExportDate) {
		msp_fire_export_date = mspFireExportDate;
	}
	public String getMsp_fire_id() {
		return msp_fire_id;
	}
	public void setMsp_fire_id(String mspFireId) {
		msp_fire_id = mspFireId;
	}
	public String getMsp_fire_addr_code() {
		return msp_fire_addr_code;
	}
	public void setMsp_fire_addr_code(String mspFireAddrCode) {
		msp_fire_addr_code = mspFireAddrCode;
	}
	public String getDist() {
		return dist;
	}
	public void setDist(String dist) {
		this.dist = dist;
	}
	public Integer getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}
	public Integer getLssa_id() {
		return lssa_id;
	}
	public void setLssa_id(Integer lssa_id) {
		this.lssa_id = lssa_id;
	}
	public Integer getLssp_id() {
		return lssp_id;
	}
	public void setLssp_id(Integer lssp_id) {
		this.lssp_id = lssp_id;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public Integer getLus_id_uw_pas() {
		return lus_id_uw_pas;
	}
	public void setLus_id_uw_pas(Integer lusIdUwPas) {
		lus_id_uw_pas = lusIdUwPas;
	}
	public Integer getLus_id_uw_fire() {
		return lus_id_uw_fire;
	}
	public void setLus_id_uw_fire(Integer lusIdUwFire) {
		lus_id_uw_fire = lusIdUwFire;
	}
	public String getLus_login_name() {
		return lus_login_name;
	}
	public void setLus_login_name(String lus_login_name) {
		this.lus_login_name = lus_login_name;
	}
	public String getMsp_address_1() {
		return msp_address_1;
	}
	public void setMsp_address_1(String msp_address_1) {
		this.msp_address_1 = msp_address_1;
	}
	public String getMsp_address_2() {
		return msp_address_2;
	}
	public void setMsp_address_2(String msp_address_2) {
		this.msp_address_2 = msp_address_2;
	}
	public String getMsp_city() {
		return msp_city;
	}
	public void setMsp_city(String msp_city) {
		this.msp_city = msp_city;
	}
	public Date getMsp_date_of_birth() {
		return msp_date_of_birth;
	}
	public void setMsp_date_of_birth(Date msp_date_of_birth) {
		this.msp_date_of_birth = msp_date_of_birth;
		if(msp_date_of_birth != null){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				msp_date_of_birth2 = sdf.format(msp_date_of_birth);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			} 
		}
	}
	public String getMsp_desc_sts_sms() {
		return msp_desc_sts_sms;
	}
	public void setMsp_desc_sts_sms(String msp_desc_sts_sms) {
		this.msp_desc_sts_sms = msp_desc_sts_sms;
	}
	public Date getMsp_fire_accept_date() {
		return msp_fire_accept_date;
	}
	public void setMsp_fire_accept_date(Date msp_fire_accept_date) {
		this.msp_fire_accept_date = msp_fire_accept_date;
	}
	public Date getMsp_fire_beg_date() {
		return msp_fire_beg_date;
	}
	public void setMsp_fire_beg_date(Date msp_fire_beg_date) {
		this.msp_fire_beg_date = msp_fire_beg_date;
	}
	public String getMsp_fire_code_name() {
		return msp_fire_code_name;
	}
	public void setMsp_fire_code_name(String msp_fire_code_name) {
		this.msp_fire_code_name = msp_fire_code_name;
	}
	public String getMsp_fire_email() {
		return msp_fire_email;
	}
	public void setMsp_fire_email(String msp_fire_email) {
		this.msp_fire_email = msp_fire_email;
	}
	public Date getMsp_fire_end_date() {
		return msp_fire_end_date;
	}
	public void setMsp_fire_end_date(Date msp_fire_end_date) {
		this.msp_fire_end_date = msp_fire_end_date;
	}
	public String getMsp_fire_insured_addr() {
		return msp_fire_insured_addr;
	}
	public void setMsp_fire_insured_addr(String msp_fire_insured_addr) {
		this.msp_fire_insured_addr = msp_fire_insured_addr;
	}
	public String getMsp_fire_insured_addr_code() {
		return msp_fire_insured_addr_code;
	}
	public void setMsp_fire_insured_addr_code(String msp_fire_insured_addr_code) {
		this.msp_fire_insured_addr_code = msp_fire_insured_addr_code;
	}
	public Integer getMsp_fire_insured_addr_envir() {
		return msp_fire_insured_addr_envir;
	}
	public void setMsp_fire_insured_addr_envir(Integer msp_fire_insured_addr_envir) {
		this.msp_fire_insured_addr_envir = msp_fire_insured_addr_envir;
	}
	public String getMsp_fire_insured_addr_no() {
		return msp_fire_insured_addr_no;
	}
	public void setMsp_fire_insured_addr_no(String msp_fire_insured_addr_no) {
		this.msp_fire_insured_addr_no = msp_fire_insured_addr_no;
	}
	public String getMsp_fire_insured_postal_code() {
		return msp_fire_insured_postal_code;
	}
	public void setMsp_fire_insured_postal_code(String msp_fire_insured_postal_code) {
		this.msp_fire_insured_postal_code = msp_fire_insured_postal_code;
	}
	public String getMsp_fire_occupation() {
		return msp_fire_occupation;
	}
	public void setMsp_fire_occupation(String msp_fire_occupation) {
		this.msp_fire_occupation = msp_fire_occupation;
		if(msp_fire_occupation != null){
			msp_fire_occupation2 = msp_fire_occupation;
		}
	}
	public String getMsp_fire_okupasi() {
		return msp_fire_okupasi;
	}
	public void setMsp_fire_okupasi(String msp_fire_okupasi) {
		this.msp_fire_okupasi = msp_fire_okupasi;
	}
	public String getMsp_fire_phone_number() {
		return msp_fire_phone_number;
	}
	public void setMsp_fire_phone_number(String msp_fire_phone_number) {
		this.msp_fire_phone_number = msp_fire_phone_number;
	}
	public String getMsp_fire_policy_no() {
		return msp_fire_policy_no;
	}
	public void setMsp_fire_policy_no(String msp_fire_policy_no) {
		this.msp_fire_policy_no = msp_fire_policy_no;
	}
	public String getMsp_fire_source_fund() {
		return msp_fire_source_fund;
	}
	public void setMsp_fire_source_fund(String msp_fire_source_fund) {
		this.msp_fire_source_fund = msp_fire_source_fund;
		if(msp_fire_source_fund != null){
			msp_fire_source_fund2 = msp_fire_source_fund;
		}
	}
	public String getMsp_fire_type_business() {
		return msp_fire_type_business;
	}
	public void setMsp_fire_type_business(String msp_fire_type_business) {
		this.msp_fire_type_business = msp_fire_type_business;
		if(msp_fire_type_business!= null){
			msp_fire_type_business2 = msp_fire_type_business;
		}
	}
	public String getMsp_full_name() {
		return msp_full_name;
	}
	public void setMsp_full_name(String msp_full_name) {
		this.msp_full_name = msp_full_name;
	}
	public String getMsp_id() {
		return msp_id;
	}
	public void setMsp_id(String msp_id) {
		this.msp_id = msp_id;
	}
	public String getMsp_identity_no() {
		return msp_identity_no;
	}
	public void setMsp_identity_no(String msp_identity_no) {
		this.msp_identity_no = msp_identity_no;
	}
	public String getMsp_kode_sts_sms() {
		return msp_kode_sts_sms;
	}
	public void setMsp_kode_sts_sms(String msp_kode_sts_sms) {
		this.msp_kode_sts_sms = msp_kode_sts_sms;
	}
	public Date getMsp_message_date() {
		return msp_message_date;
	}
	public void setMsp_message_date(Date msp_message_date) {
		this.msp_message_date = msp_message_date;
	}
	public String getMsp_mobile() {
		return msp_mobile;
	}
	public void setMsp_mobile(String msp_mobile) {
		this.msp_mobile = msp_mobile;
	}
	public Date getMsp_pas_accept_date() {
		return msp_pas_accept_date;
	}
	public void setMsp_pas_accept_date(Date msp_pas_accept_date) {
		this.msp_pas_accept_date = msp_pas_accept_date;
	}
	public Date getMsp_pas_beg_date() {
		return msp_pas_beg_date;
	}
	public void setMsp_pas_beg_date(Date msp_pas_beg_date) {
		this.msp_pas_beg_date = msp_pas_beg_date;
	}
	public Date getMsp_pas_end_date() {
		return msp_pas_end_date;
	}
	public void setMsp_pas_end_date(Date msp_pas_end_date) {
		this.msp_pas_end_date = msp_pas_end_date;
	}
	public String getMsp_postal_code() {
		return msp_postal_code;
	}
	public void setMsp_postal_code(String msp_postal_code) {
		this.msp_postal_code = msp_postal_code;
	}
	public Date getMsp_receive_date() {
		return msp_receive_date;
	}
	public void setMsp_receive_date(Date msp_receive_date) {
		this.msp_receive_date = msp_receive_date;
	}
	public String getMsp_text() {
		return msp_text;
	}
	public void setMsp_text(String msp_text) {
		this.msp_text = msp_text;
	}
	public String getNo_kartu() {
		return no_kartu;
	}
	public void setNo_kartu(String no_kartu) {
		this.no_kartu = no_kartu;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getMsp_fire_address_1() {
		return msp_fire_address_1;
	}
	public void setMsp_fire_address_1(String msp_fire_address_1) {
		this.msp_fire_address_1 = msp_fire_address_1;
	}
	public Date getMsp_fire_date_of_birth() {
		return msp_fire_date_of_birth;
	}
	public void setMsp_fire_date_of_birth(Date msp_fire_date_of_birth) {
		this.msp_fire_date_of_birth = msp_fire_date_of_birth;
		if(msp_fire_date_of_birth != null){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				msp_fire_date_of_birth2 = sdf.format(msp_fire_date_of_birth);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			} 
		}
	}
	public String getMsp_fire_identity() {
		return msp_fire_identity;
	}
	public void setMsp_fire_identity(String msp_fire_identity) {
		this.msp_fire_identity = msp_fire_identity;
	}
	public String getMsp_fire_mobile() {
		return msp_fire_mobile;
	}
	public void setMsp_fire_mobile(String msp_fire_mobile) {
		this.msp_fire_mobile = msp_fire_mobile;
	}
	public String getMsp_fire_name() {
		return msp_fire_name;
	}
	public void setMsp_fire_name(String msp_fire_name) {
		this.msp_fire_name = msp_fire_name;
	}
	public String getMsp_fire_postal_code() {
		return msp_fire_postal_code;
	}
	public void setMsp_fire_postal_code(String msp_fire_postal_code) {
		this.msp_fire_postal_code = msp_fire_postal_code;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public String getMsag_id() {
		return msag_id;
	}
	public void setMsag_id(String msag_id) {
		this.msag_id = msag_id;
	}
	public String getMsp_no_rekening() {
		return msp_no_rekening;
	}
	public void setMsp_no_rekening(String msp_no_rekening) {
		this.msp_no_rekening = msp_no_rekening;
	}
	public String getMsp_fire_date_of_birth2() {
		return msp_fire_date_of_birth2;
	}
	public void setMsp_fire_date_of_birth2(String msp_fire_date_of_birth2) {
		this.msp_fire_date_of_birth2 = msp_fire_date_of_birth2;
		if(!"".equals(msp_fire_date_of_birth2)){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				msp_fire_date_of_birth = sdf.parse(msp_fire_date_of_birth2);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			} 
		}
	}
	public Integer getLscb_id() {
		return lscb_id;
	}
	public void setLscb_id(Integer lscbId) {
		lscb_id = lscbId;
	}
	public Integer getMsp_age() {
		return msp_age;
	}
	public void setMsp_age(Integer mspAge) {
		msp_age = mspAge;
	}
	
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lcaId) {
		lca_id = lcaId;
	}
	public String getLsrg_nama() {
		return lsrg_nama;
	}
	public void setLsrg_nama(String lsrgNama) {
		lsrg_nama = lsrgNama;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mclFirst) {
		mcl_first = mclFirst;
	}
	public String getLsbp_id() {
		return lsbp_id;
	}
	public void setLsbp_id(String lsbpId) {
		lsbp_id = lsbpId;
	}
	public String getMsp_rek_nama() {
		return msp_rek_nama;
	}
	public void setMsp_rek_nama(String mspRekNama) {
		msp_rek_nama = mspRekNama;
	}
	public String getMsp_rek_cabang() {
		return msp_rek_cabang;
	}
	public void setMsp_rek_cabang(String mspRekCabang) {
		msp_rek_cabang = mspRekCabang;
	}
	public String getMsp_rek_kota() {
		return msp_rek_kota;
	}
	public void setMsp_rek_kota(String mspRekKota) {
		msp_rek_kota = mspRekKota;
	}
	public String getLsbp_nama() {
		return lsbp_nama;
	}
	public void setLsbp_nama(String lsbpNama) {
		lsbp_nama = lsbpNama;
	}
	public String getMsp_fire_insured_city() {
		return msp_fire_insured_city;
	}
	public void setMsp_fire_insured_city(String mspFireInsuredCity) {
		msp_fire_insured_city = mspFireInsuredCity;
	}
	public String getMsp_fire_insured_phone_number() {
		return msp_fire_insured_phone_number;
	}
	public void setMsp_fire_insured_phone_number(String mspFireInsuredPhoneNumber) {
		msp_fire_insured_phone_number = mspFireInsuredPhoneNumber;
	}
	public String getMsp_pas_phone_number() {
		return msp_pas_phone_number;
	}
	public void setMsp_pas_phone_number(String mspPasPhoneNumber) {
		msp_pas_phone_number = mspPasPhoneNumber;
	}
	public String getMsp_fire_okupasi_else() {
		return msp_fire_okupasi_else;
	}
	public void setMsp_fire_okupasi_else(String mspFireOkupasiElse) {
		msp_fire_okupasi_else = mspFireOkupasiElse;
	}
	public String getMsp_fire_ins_addr_envir_else() {
		return msp_fire_ins_addr_envir_else;
	}
	public void setMsp_fire_ins_addr_envir_else(String mspFireInsAddrEnvirElse) {
		msp_fire_ins_addr_envir_else = mspFireInsAddrEnvirElse;
	}
	public String getMsp_date_of_birth2() {
		return msp_date_of_birth2;
	}
	public void setMsp_date_of_birth2(String mspDateOfBirth2) {
		msp_date_of_birth2 = mspDateOfBirth2;
		if(!"".equals(msp_date_of_birth2)){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				msp_date_of_birth = sdf.parse(msp_date_of_birth2);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			} 
		}
	}

	public Integer getMsp_flag_aksep() {
		return msp_flag_aksep;
	}
	public void setMsp_flag_aksep(Integer mspFlagAksep) {
		msp_flag_aksep = mspFlagAksep;
	}
	public Integer getMsp_fire_export_flag() {
		return msp_fire_export_flag;
	}
	public void setMsp_fire_export_flag(Integer mspFireExportFlag) {
		msp_fire_export_flag = mspFireExportFlag;
	}
	public Integer getMsp_flag_cc() {
		return msp_flag_cc;
	}
	public void setMsp_flag_cc(Integer mspFlagCc) {
		msp_flag_cc = mspFlagCc;
	}
	
	public String getMsp_pas_nama_pp() {
		return msp_pas_nama_pp;
	}
	public void setMsp_pas_nama_pp(String mspPasNamaPp) {
		msp_pas_nama_pp = mspPasNamaPp;
	}
	public String getMsp_pas_tmp_lhr_pp() {
		return msp_pas_tmp_lhr_pp;
	}
	public void setMsp_pas_tmp_lhr_pp(String mspPasTmpLhrPp) {
		msp_pas_tmp_lhr_pp = mspPasTmpLhrPp;
	}
	public String getMsp_pas_email() {
		return msp_pas_email;
	}
	public void setMsp_pas_email(String mspPasEmail) {
		msp_pas_email = mspPasEmail;
	}
	public Integer getLsre_id() {
		return lsre_id;
	}
	public void setLsre_id(Integer lsreId) {
		lsre_id = lsreId;
	}
	public String getMsp_pas_tmp_lhr_tt() {
		return msp_pas_tmp_lhr_tt;
	}
	public void setMsp_pas_tmp_lhr_tt(String mspPasTmpLhrTt) {
		msp_pas_tmp_lhr_tt = mspPasTmpLhrTt;
	}
	public Date getMsp_pas_dob_pp() {
		return msp_pas_dob_pp;
	}
	public void setMsp_pas_dob_pp(Date mspPasDobPp) {
		this.msp_pas_dob_pp = mspPasDobPp;
		if(msp_pas_dob_pp != null){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				msp_pas_dob_pp2 = sdf.format(msp_pas_dob_pp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			} 
		}
	}
	public String getMsp_pas_dob_pp2() {
		return msp_pas_dob_pp2;
	}
	public void setMsp_pas_dob_pp2(String mspPasDobPp2) {
		this.msp_pas_dob_pp2 = mspPasDobPp2;
		if(!"".equals(msp_pas_dob_pp2)){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				msp_pas_dob_pp = sdf.parse(msp_pas_dob_pp2);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			} 
		}
	}
	public Integer getProduk() {
		return produk;
	}
	public void setProduk(Integer produk) {
		this.produk = produk;
	}
	public String getMsp_fire_occupation2() {
		return msp_fire_occupation2;
	}
	public void setMsp_fire_occupation2(String mspFireOccupation2) {
		msp_fire_occupation2 = mspFireOccupation2;
	}
	public String getMsp_fire_type_business2() {
		return msp_fire_type_business2;
	}
	public void setMsp_fire_type_business2(String mspFireTypeBusiness2) {
		msp_fire_type_business2 = mspFireTypeBusiness2;
	}
	public String getMsp_fire_source_fund2() {
		return msp_fire_source_fund2;
	}
	public void setMsp_fire_source_fund2(String mspFireSourceFund2) {
		msp_fire_source_fund2 = mspFireSourceFund2;
	}
	public Integer getMsp_flag_pas() {
		return msp_flag_pas;
	}
	public void setMsp_flag_pas(Integer mspFlagPas) {
		msp_flag_pas = mspFlagPas;
	}
	public String getMsp_fire_fail_desc() {
		return msp_fire_fail_desc;
	}
	public void setMsp_fire_fail_desc(String mspFireFailDesc) {
		msp_fire_fail_desc = mspFireFailDesc;
	}
	public String getNama_produk() {
		return nama_produk;
	}
	public void setNama_produk(String namaProduk) {
		nama_produk = namaProduk;
	}
	public String getPremi() {
		return premi;
	}
	public void setPremi(String premi) {
		this.premi = premi;
	}
	public String getNama_agent() {
		return nama_agent;
	}
	public void setNama_agent(String namaAgent) {
		nama_agent = namaAgent;
	}
	public String getCabang() {
		return cabang;
	}
	public void setCabang(String cabang) {
		this.cabang = cabang;
	}
	public Integer getMsp_sex_pp() {
		return msp_sex_pp;
	}
	public void setMsp_sex_pp(Integer mspSexPp) {
		msp_sex_pp = mspSexPp;
	}
	public Integer getMsp_status() {
		return msp_status;
	}
	public void setMsp_status(Integer mspStatus) {
		msp_status = mspStatus;
	}
	public String getMsp_ket_status() {
		return msp_ket_status;
	}
	public void setMsp_ket_status(String mspKetStatus) {
		msp_ket_status = mspKetStatus;
	}
	public Date getMsp_tgl_status() {
		return msp_tgl_status;
	}
	public void setMsp_tgl_status(Date mspTglStatus) {
		msp_tgl_status = mspTglStatus;
		if(msp_tgl_status != null){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				msp_tgl_status2 = sdf.format(msp_tgl_status);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			} 
		}
	}
	public String getMsp_tgl_status2() {
		return msp_tgl_status2;
	}
	public void setMsp_tgl_status2(String mspTglStatus2) {
		msp_tgl_status2 = mspTglStatus2;
		if(msp_tgl_status2 != null){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				msp_tgl_status = sdf.parse(msp_tgl_status2);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			} 
		}
	}
	public String getKode_ao() {
		return kode_ao;
	}
	public void setKode_ao(String kodeAo) {
		kode_ao = kodeAo;
	}
	public Integer getPribadi() {
		return pribadi;
	}
	public void setPribadi(Integer pribadi) {
		this.pribadi = pribadi;
	}
	public String getMsag_id_pp() {
		return msag_id_pp;
	}
	public void setMsag_id_pp(String msagIdPp) {
		msag_id_pp = msagIdPp;
	}
	public String getBranch_admin_name() {
		return branch_admin_name;
	}
	public void setBranch_admin_name(String branchAdminName) {
		branch_admin_name = branchAdminName;
	}
	public Integer getMsp_exist_bp() {
		return msp_exist_bp;
	}
	public void setMsp_exist_bp(Integer mspExistBp) {
		msp_exist_bp = mspExistBp;
	}
	public Integer getMsp_warga() {
		return msp_warga;
	}
	public void setMsp_warga(Integer mspWarga) {
		msp_warga = mspWarga;
	}
	public String getMsp_warga_else() {
		return msp_warga_else;
	}
	public void setMsp_warga_else(String mspWargaElse) {
		msp_warga_else = mspWargaElse;
	}
	public Integer getMsp_status_nikah() {
		return msp_status_nikah;
	}
	public void setMsp_status_nikah(Integer mspStatusNikah) {
		msp_status_nikah = mspStatusNikah;
	}
	public Integer getMsp_pendidikan() {
		return msp_pendidikan;
	}
	public void setMsp_pendidikan(Integer mspPendidikan) {
		msp_pendidikan = mspPendidikan;
	}
	public String getMsp_pendidikan_else() {
		return msp_pendidikan_else;
	}
	public void setMsp_pendidikan_else(String mspPendidikanElse) {
		msp_pendidikan_else = mspPendidikanElse;
	}
	public String getMsp_occupation() {
		return msp_occupation;
	}
	public void setMsp_occupation(String mspOccupation) {
		msp_occupation = mspOccupation;
	}
	public String getMsp_occupation_else() {
		return msp_occupation_else;
	}
	public void setMsp_occupation_else(String mspOccupationElse) {
		msp_occupation_else = mspOccupationElse;
	}
	public String getMsp_company_name() {
		return msp_company_name;
	}
	public void setMsp_company_name(String mspCompanyName) {
		msp_company_name = mspCompanyName;
	}
	public Integer getMsp_company_usaha() {
		return msp_company_usaha;
	}
	public void setMsp_company_usaha(Integer mspCompanyUsaha) {
		msp_company_usaha = mspCompanyUsaha;
	}
	public String getMsp_company_jabatan() {
		return msp_company_jabatan;
	}
	public void setMsp_company_jabatan(String mspCompanyJabatan) {
		msp_company_jabatan = mspCompanyJabatan;
	}
	public String getMsp_company_address() {
		return msp_company_address;
	}
	public void setMsp_company_address(String mspCompanyAddress) {
		msp_company_address = mspCompanyAddress;
	}
	public String getMsp_company_postal_code() {
		return msp_company_postal_code;
	}
	public void setMsp_company_postal_code(String mspCompanyPostalCode) {
		msp_company_postal_code = mspCompanyPostalCode;
	}
	public String getMsp_company_phone_number() {
		return msp_company_phone_number;
	}
	public void setMsp_company_phone_number(String mspCompanyPhoneNumber) {
		msp_company_phone_number = mspCompanyPhoneNumber;
	}
	public Integer getMsp_cek_ktp() {
		return msp_cek_ktp;
	}
	public void setMsp_cek_ktp(Integer mspCekKtp) {
		msp_cek_ktp = mspCekKtp;
	}
	public Integer getMsp_cek_kk() {
		return msp_cek_kk;
	}
	public void setMsp_cek_kk(Integer mspCekKk) {
		msp_cek_kk = mspCekKk;
	}
	public Integer getMsp_cek_npwp() {
		return msp_cek_npwp;
	}
	public void setMsp_cek_npwp(Integer mspCekNpwp) {
		msp_cek_npwp = mspCekNpwp;
	}
	public Integer getMsp_cek_ktp_uw() {
		return msp_cek_ktp_uw;
	}
	public void setMsp_cek_ktp_uw(Integer mspCekKtpUw) {
		msp_cek_ktp_uw = mspCekKtpUw;
	}
	public Integer getMsp_cek_kk_uw() {
		return msp_cek_kk_uw;
	}
	public void setMsp_cek_kk_uw(Integer mspCekKkUw) {
		msp_cek_kk_uw = mspCekKkUw;
	}
	public Integer getMsp_cek_npwp_uw() {
		return msp_cek_npwp_uw;
	}
	public void setMsp_cek_npwp_uw(Integer mspCekNpwpUw) {
		msp_cek_npwp_uw = mspCekNpwpUw;
	}
	public Integer getMsp_cek_bukti_bayar_uw() {
		return msp_cek_bukti_bayar_uw;
	}
	public void setMsp_cek_bukti_bayar_uw(Integer mspCekBuktiBayarUw) {
		msp_cek_bukti_bayar_uw = mspCekBuktiBayarUw;
	}
	
	public Integer getMsp_cek_srt_keterangan() {
		return msp_cek_srt_keterangan;
	}
	public void setMsp_cek_srt_keterangan(Integer msp_cek_srt_keterangan) {
		this.msp_cek_srt_keterangan = msp_cek_srt_keterangan;
	}
	public Integer getMsp_cek_srt_keterangan_uw() {
		return msp_cek_srt_keterangan_uw;
	}
	public void setMsp_cek_srt_keterangan_uw(Integer msp_cek_srt_keterangan_uw) {
		this.msp_cek_srt_keterangan_uw = msp_cek_srt_keterangan_uw;
	}
	public Integer getMsp_cek_bukti_bayar() {
		return msp_cek_bukti_bayar;
	}
	public void setMsp_cek_bukti_bayar(Integer mspCekBuktiBayar) {
		msp_cek_bukti_bayar = mspCekBuktiBayar;
	}
	public String getMsp_agama() {
		return msp_agama;
	}
	public void setMsp_agama(String mspAgama) {
		msp_agama = mspAgama;
	}
	public Integer getLsag_id() {
		return lsag_id;
	}
	public void setLsag_id(Integer lsagId) {
		lsag_id = lsagId;
	}
	public String getMsp_exist_bp_name() {
		return msp_exist_bp_name;
	}
	public void setMsp_exist_bp_name(String mspExistBpName) {
		msp_exist_bp_name = mspExistBpName;
	}
	public String getMsp_area_code_rumah() {
		return msp_area_code_rumah;
	}
	public void setMsp_area_code_rumah(String mspAreaCodeRumah) {
		msp_area_code_rumah = mspAreaCodeRumah;
	}
	public Integer getLside_id() {
		return lside_id;
	}
	public void setLside_id(Integer lsideId) {
		lside_id = lsideId;
	}
	public String getMsp_company_area_code() {
		return msp_company_area_code;
	}
	public void setMsp_company_area_code(String mspCompanyAreaCode) {
		msp_company_area_code = mspCompanyAreaCode;
	}
	public String getEmailTo() {
		return emailTo;
	}
	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}
	public String getEmailCc() {
		return emailCc;
	}
	public void setEmailCc(String emailCc) {
		this.emailCc = emailCc;
	}
	public Integer getLrb_id() {
		return lrb_id;
	}
	public void setLrb_id(Integer lrbId) {
		lrb_id = lrbId;
	}
	public Integer getDirAgenBp() {
		return dirAgenBp;
	}
	public void setDirAgenBp(Integer dirAgenBp) {
		this.dirAgenBp = dirAgenBp;
	}
	public String getMsp_mobile2() {
		return msp_mobile2;
	}
	public void setMsp_mobile2(String mspMobile2) {
		msp_mobile2 = mspMobile2;
	}
	public String getMsp_ket_batal() {
		return msp_ket_batal;
	}
	public void setMsp_ket_batal(String mspKetBatal) {
		msp_ket_batal = mspKetBatal;
	}
	public Date getMsp_tgl_batal() {
		return msp_tgl_batal;
	}
	public void setMsp_tgl_batal(Date mspTglBatal) {
		msp_tgl_batal = mspTglBatal;
	}
	public Date getMsp_tgl_transfer() {
		return msp_tgl_transfer;
	}
	public void setMsp_tgl_transfer(Date mspTglTransfer) {
		msp_tgl_transfer = mspTglTransfer;
	}
	public Integer getLus_id_uw_batal() {
		return lus_id_uw_batal;
	}
	public void setLus_id_uw_batal(Integer lusIdUwBatal) {
		lus_id_uw_batal = lusIdUwBatal;
	}
	public Date getMsp_fire_fail_date() {
		return msp_fire_fail_date;
	}
	public void setMsp_fire_fail_date(Date mspFireFailDate) {
		msp_fire_fail_date = mspFireFailDate;
	}
	public Date getMsp_admin_date() {
		return msp_admin_date;
	}
	public void setMsp_admin_date(Date mspAdminDate) {
		msp_admin_date = mspAdminDate;
	}
	public String getMspo_plan_provider() {
		return mspo_plan_provider;
	}
	public void setMspo_plan_provider(String mspoPlanProvider) {
		mspo_plan_provider = mspoPlanProvider;
	}
	public String getJenis_pas() {
		return jenis_pas;
	}
	public void setJenis_pas(String jenisPas) {
		jenis_pas = jenisPas;
	}
	public String getMsp_up() {
		return msp_up;
	}
	public void setMsp_up(String mspUp) {
		msp_up = mspUp;
	}
	public String getNo_sertifikat() {
		return no_sertifikat;
	}
	public void setNo_sertifikat(String noSertifikat) {
		no_sertifikat = noSertifikat;
	}
	public Integer getReff_bii_flag() {
		return reff_bii_flag;
	}
	public void setReff_bii_flag(Integer reffBiiFlag) {
		reff_bii_flag = reffBiiFlag;
	}
	public String getMsp_identity_no_tt() {
		return msp_identity_no_tt;
	}
	public void setMsp_identity_no_tt(String mspIdentityNoTt) {
		msp_identity_no_tt = mspIdentityNoTt;
	}
	public Integer getFlag_val_send() {
		return flag_val_send;
	}
	public void setFlag_val_send(Integer flagValSend) {
		flag_val_send = flagValSend;
	}
	public String getMsp_premi() {
		return msp_premi;
	}
	public void setMsp_premi(String mspPremi) {
		msp_premi = mspPremi;
	}
	public Integer getMsp_cek_rekening() {
		return msp_cek_rekening;
	}
	public void setMsp_cek_rekening(Integer mspCekRekening) {
		msp_cek_rekening = mspCekRekening;
	}
	public Integer getMsp_cek_rekening_uw() {
		return msp_cek_rekening_uw;
	}
	public void setMsp_cek_rekening_uw(Integer mspCekRekeningUw) {
		msp_cek_rekening_uw = mspCekRekeningUw;
	}
	public Integer getJenis_bp() {
		return jenis_bp;
	}
	public void setJenis_bp(Integer jenis_bp) {
		this.jenis_bp = jenis_bp;
	}
	public Integer getMsp_cek_akte_kelahiran() {
		return msp_cek_akte_kelahiran;
	}
	public void setMsp_cek_akte_kelahiran(Integer msp_cek_akte_kelahiran) {
		this.msp_cek_akte_kelahiran = msp_cek_akte_kelahiran;
	}
	public Integer getMsp_cek_akte_kelahiran_uw() {
		return msp_cek_akte_kelahiran_uw;
	}
	public void setMsp_cek_akte_kelahiran_uw(Integer msp_cek_akte_kelahiran_uw) {
		this.msp_cek_akte_kelahiran_uw = msp_cek_akte_kelahiran_uw;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getMcl_id_pp() {
		return mcl_id_pp;
	}
	public void setMcl_id_pp(String mcl_id_pp) {
		this.mcl_id_pp = mcl_id_pp;
	}
	public String getMcl_id_tt() {
		return mcl_id_tt;
	}
	public void setMcl_id_tt(String mcl_id_tt) {
		this.mcl_id_tt = mcl_id_tt;
	}
	public String getMsp_company_email() {
		return msp_company_email;
	}
	public void setMsp_company_email(String msp_company_email) {
		this.msp_company_email = msp_company_email;
	}
	public String getMsp_company_fax() {
		return msp_company_fax;
	}
	public void setMsp_company_fax(String msp_company_fax) {
		this.msp_company_fax = msp_company_fax;
	}
	public String getMsp_company_ijin_usaha() {
		return msp_company_ijin_usaha;
	}
	public void setMsp_company_ijin_usaha(String msp_company_ijin_usaha) {
		this.msp_company_ijin_usaha = msp_company_ijin_usaha;
	}
	public String getMsp_kecamatan() {
		return msp_kecamatan;
	}
	public void setMsp_kecamatan(String msp_kecamatan) {
		this.msp_kecamatan = msp_kecamatan;
	}
	public String getMsp_propinsi() {
		return msp_propinsi;
	}
	public void setMsp_propinsi(String msp_propinsi) {
		this.msp_propinsi = msp_propinsi;
	}
	public String getMsp_pas_npwp() {
		return msp_pas_npwp;
	}
	public void setMsp_pas_npwp(String msp_pas_npwp) {
		this.msp_pas_npwp = msp_pas_npwp;
	}
	public String getMsp_rt() {
		return msp_rt;
	}
	public void setMsp_rt(String msp_rt) {
		this.msp_rt = msp_rt;
	}
	public String getMsp_rw() {
		return msp_rw;
	}
	public void setMsp_rw(String msp_rw) {
		this.msp_rw = msp_rw;
	}
	public Integer getMsp_still_bp() {
		return msp_still_bp;
	}
	public void setMsp_still_bp(Integer msp_still_bp) {
		this.msp_still_bp = msp_still_bp;
	}
	public Integer getMsp_partner_exist_bp() {
		return msp_partner_exist_bp;
	}
	public void setMsp_partner_exist_bp(Integer msp_partner_exist_bp) {
		this.msp_partner_exist_bp = msp_partner_exist_bp;
	}
	public Integer getMsp_partner_still_bp() {
		return msp_partner_still_bp;
	}
	public void setMsp_partner_still_bp(Integer msp_partner_still_bp) {
		this.msp_partner_still_bp = msp_partner_still_bp;
	}
	public Integer getLju_id() {
		return lju_id;
	}
	public void setLju_id(Integer lju_id) {
		this.lju_id = lju_id;
	}
	public Integer getMsp_sex_tt() {
		return msp_sex_tt;
	}
	public void setMsp_sex_tt(Integer msp_sex_tt) {
		this.msp_sex_tt = msp_sex_tt;
	}
	public Integer getMsp_fire_jenis() {
		return msp_fire_jenis;
	}
	public void setMsp_fire_jenis(Integer msp_fire_jenis) {
		this.msp_fire_jenis = msp_fire_jenis;
	}
	public Integer getMsp_cek_tandatangan() {
		return msp_cek_tandatangan;
	}
	public void setMsp_cek_tandatangan(Integer msp_cek_tandatangan) {
		this.msp_cek_tandatangan = msp_cek_tandatangan;
	}
	public Integer getMsp_cek_tandatangan_uw() {
		return msp_cek_tandatangan_uw;
	}
	public void setMsp_cek_tandatangan_uw(Integer msp_cek_tandatangan_uw) {
		this.msp_cek_tandatangan_uw = msp_cek_tandatangan_uw;
	}
	public String getApplication_id() {
		return application_id;
	}
	public void setApplication_id(String application_id) {
		this.application_id = application_id;
	}
	public Integer getReff_id() {
		return reff_id;
	}
	public void setReff_id(Integer reff_id) {
		this.reff_id = reff_id;
	}
	public String getNama_reff() {
		return nama_reff;
	}
	public void setNama_reff(String nama_reff) {
		this.nama_reff = nama_reff;
	}
	public String getLcb_no() {
		return lcb_no;
	}
	public void setLcb_no(String lcb_no) {
		this.lcb_no = lcb_no;
	}
	public String getLcb_reff() {
		return lcb_reff;
	}
	public void setLcb_reff(String lcb_reff) {
		this.lcb_reff = lcb_reff;
	}
	public String getNo_va() {
		return no_va;
	}
	public void setNo_va(String no_va) {
		this.no_va = no_va;
	}
	public Integer getPanjang_no_rek() {
		return panjang_no_rek;
	}
	public void setPanjang_no_rek(Integer panjang_no_rek) {
		this.panjang_no_rek = panjang_no_rek;
	}
	public String getMsp_pas_beg_date2() {
		return msp_pas_beg_date2;
	}
	public void setMsp_pas_beg_date2(String msp_pas_beg_date2) {
		this.msp_pas_beg_date2 = msp_pas_beg_date2;
	}
	public String getMsp_pas_end_date2() {
		return msp_pas_end_date2;
	}
	public void setMsp_pas_end_date2(String msp_pas_end_date2) {
		this.msp_pas_end_date2 = msp_pas_end_date2;
	}
	public String getMsp_pas_create_date2() {
		return msp_pas_create_date2;
	}
	public void setMsp_pas_create_date2(String msp_pas_create_date2) {
		this.msp_pas_create_date2 = msp_pas_create_date2;
	}
    public Integer getNew_card() {
        return new_card;
    }
    public void setNew_card(Integer new_card) {
        this.new_card = new_card;
    }
	public String getTm_id() {
		return tm_id;
	}
	public void setTm_id(String tm_id) {
		this.tm_id = tm_id;
	}
	public String getSpv_id() {
		return spv_id;
	}
	public void setSpv_id(String spv_id) {
		this.spv_id = spv_id;
	}
	public Integer getFlag_kpr() {
		return flag_kpr;
	}
	public void setFlag_kpr(Integer flag_kpr) {
		this.flag_kpr = flag_kpr;
	}
	public String getMsaw_first1() {
		return msaw_first1;
	}
	public void setMsaw_first1(String msaw_first1) {
		this.msaw_first1 = msaw_first1;
	}
	public String getLsre_relation1() {
		return lsre_relation1;
	}
	public void setLsre_relation1(String lsre_relation1) {
		this.lsre_relation1 = lsre_relation1;
	}
	public Double getMsaw_persen1() {
		return msaw_persen1;
	}
	public void setMsaw_persen1(Double msaw_persen1) {
		this.msaw_persen1 = msaw_persen1;
	}
	public String getMsaw_first2() {
		return msaw_first2;
	}
	public void setMsaw_first2(String msaw_first2) {
		this.msaw_first2 = msaw_first2;
	}
	public String getLsre_relation2() {
		return lsre_relation2;
	}
	public void setLsre_relation2(String lsre_relation2) {
		this.lsre_relation2 = lsre_relation2;
	}
	public Double getMsaw_persen2() {
		return msaw_persen2;
	}
	public void setMsaw_persen2(Double msaw_persen2) {
		this.msaw_persen2 = msaw_persen2;
	}
	public String getMsaw_first3() {
		return msaw_first3;
	}
	public void setMsaw_first3(String msaw_first3) {
		this.msaw_first3 = msaw_first3;
	}
	public String getLsre_relation3() {
		return lsre_relation3;
	}
	public void setLsre_relation3(String lsre_relation3) {
		this.lsre_relation3 = lsre_relation3;
	}
	public Double getMsaw_persen3() {
		return msaw_persen3;
	}
	public void setMsaw_persen3(Double msaw_persen3) {
		this.msaw_persen3 = msaw_persen3;
	}
	public String getMsaw_first4() {
		return msaw_first4;
	}
	public void setMsaw_first4(String msaw_first4) {
		this.msaw_first4 = msaw_first4;
	}
	public String getLsre_relation4() {
		return lsre_relation4;
	}
	public void setLsre_relation4(String lsre_relation4) {
		this.lsre_relation4 = lsre_relation4;
	}
	public Double getMsaw_persen4() {
		return msaw_persen4;
	}
	public void setMsaw_persen4(Double msaw_persen4) {
		this.msaw_persen4 = msaw_persen4;
	}
	public String getMspe_mother() {
		return mspe_mother;
	}
	public void setMspe_mother(String mspe_mother) {
		this.mspe_mother = mspe_mother;
	}
	public String getCif() {
		return cif;
	}
	public void setCif(String cif) {
		this.cif = cif;
	}
	
	
	
}
