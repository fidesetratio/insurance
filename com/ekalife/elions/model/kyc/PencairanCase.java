/**
 * @author  : Andhika
 * @created : July 02, 2012 5:40:06 PM
 */
package com.ekalife.elions.model.kyc;

import java.io.Serializable;
import java.util.Date;

public class PencairanCase implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;                       
	private String mspo_policy_no_format;          
	private String pemegang;  
	private String tertanggung;                    
	private Date mste_beg_date;
	private Integer lsbs_id;
	private String lsdbs_name;                     
	private String lku_symbol;                     
	private Double premi_pokok;                   
	private String frek;                           
	private String mpn_job_desc;                   
	private String mkl_kerja;                      
	private String mkl_industri;                   
	private String mkl_penghasilan;                
	private String mkl_pendanaan;                  
	private String mkl_tujuan;
	private Integer flagKyc;
	private String kycResult;
	private Date mssur_tgl_input;
	private Integer row;
	private Integer jns_transaksi;
	private String lus_full_name; 
	private Integer mste_flag_uw;
	private String mspo_policy_holder;
	private Date mste_kyc_date; 
	private String jabatan;
	private String status_spaj;
	private Double mpc_cair;
	private Date tgl_cair;
	private Double nominal_cair;
	
	public String getStatus_spaj() {
		return status_spaj;
	}
	public void setStatus_spaj(String statusSpaj) {
		status_spaj = statusSpaj;
	}
	public String getJabatan() {
		return jabatan;
	}
	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
	}
	public Integer getMste_flag_uw() {
		return mste_flag_uw;
	}
	public void setMste_flag_uw(Integer mste_flag_uw) {
		this.mste_flag_uw = mste_flag_uw;
	}
	public String getLus_full_name() {
		return lus_full_name;
	}
	public void setLus_full_name(String lus_full_name) {
		this.lus_full_name = lus_full_name;
	}
	public Integer getRow() {
		return row;
	}
	public void setRow(Integer row) {
		this.row = row;
	}
	public Integer getFlagKyc() {
		return flagKyc;
	}
	public void setFlagKyc(Integer flagKyc) {
		this.flagKyc = flagKyc;
	}
	public String getKycResult() {
		return kycResult;
	}
	public void setKycResult(String kycResult) {
		this.kycResult = kycResult;
	}
	public String getFrek() {
		return frek;
	}
	public void setFrek(String frek) {
		this.frek = frek;
	}
	public String getLku_symbol() {
		return lku_symbol;
	}
	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
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
	public String getMspo_policy_no_format() {
		return mspo_policy_no_format;
	}
	public void setMspo_policy_no_format(String mspo_policy_no_format) {
		this.mspo_policy_no_format = mspo_policy_no_format;
	}
	public Date getMste_beg_date() {
		return mste_beg_date;
	}
	public void setMste_beg_date(Date mste_beg_date) {
		this.mste_beg_date = mste_beg_date;
	}
	public String getPemegang() {
		return pemegang;
	}
	public void setPemegang(String pemegang) {
		this.pemegang = pemegang;
	}
	public Double getPremi_pokok() {
		return premi_pokok;
	}
	public void setPremi_pokok(Double premi_pokok) {
		this.premi_pokok = premi_pokok;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getTertanggung() {
		return tertanggung;
	}
	public void setTertanggung(String tertanggung) {
		this.tertanggung = tertanggung;
	}
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public Integer getJns_transaksi() {
		return jns_transaksi;
	}
	public void setJns_transaksi(Integer jns_transaksi) {
		this.jns_transaksi = jns_transaksi;
	}
	public Date getMssur_tgl_input() {
		return mssur_tgl_input;
	}
	public void setMssur_tgl_input(Date mssur_tgl_input) {
		this.mssur_tgl_input = mssur_tgl_input;
	}
	public String getMspo_policy_holder() {
		return mspo_policy_holder;
	}
	public void setMspo_policy_holder(String mspo_policy_holder) {
		this.mspo_policy_holder = mspo_policy_holder;
	}
	public Date getMste_kyc_date() {
		return mste_kyc_date;
	}
	public void setMste_kyc_date(Date mste_kyc_date) {
		this.mste_kyc_date = mste_kyc_date;
	}
	public Double getMpc_cair() {
		return mpc_cair;
	}
	public void setMpc_cair(Double mpc_cair) {
		this.mpc_cair = mpc_cair;
	}
	public Date getTgl_cair() {
		return tgl_cair;
	}
	public void setTgl_cair(Date tgl_cair) {
		this.tgl_cair = tgl_cair;
	}
	public Double getNominal_cair() {
		return nominal_cair;
	}
	public void setNominal_cair(Double nominal_cair) {
		this.nominal_cair = nominal_cair;
	}
}
