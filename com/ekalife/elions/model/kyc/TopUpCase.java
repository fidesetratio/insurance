/**
 * @author  : Andhika
 * @created : July 02, 2012 1:40:06 PM
 */
package com.ekalife.elions.model.kyc;

import java.io.Serializable;
import java.util.Date;

public class TopUpCase implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Double total_tu;                      
	private String reg_spaj;                       
	private String mspo_policy_no_format;          
	private String pemegang;  
	private String tertanggung;                    
	private Date mste_beg_date;
	private Integer lsbs_id;
	private String lsdbs_name;                     
	private String lku_symbol;                     
	private Double premi_pokok;                   
	private String topup;                          
	private String frek;                           
	private String mpn_job_desc;                   
	private String mkl_kerja;                      
	private String mkl_industri;                   
	private String mkl_penghasilan;                
	private String mkl_pendanaan;                  
	private Double jmlh_tu;
	private Double jumTop_Upx; 
	private String mkl_tujuan;
	private String nama_topup;
	private Integer flagKyc;
	private String kycResult;
	private Date mssur_tgl_input;
	private Integer row;
	private Integer jns_transaksi;
	private String lus_full_name; 
	private Integer mste_flag_uw;
	private Integer flagYUw;
	private Integer flagTUw;
	private Integer flagYUkm;
	private Integer flagTUkm;
	private Integer flagYDirec;
	private Integer flagTDirec;
	private String mspo_policy_holder;
	private Date mste_kyc_date; 
	private String jabatan;
	private String status_spaj;
	private String status_topUp;
	private Integer top_ke;
	
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
	public Double getJmlh_tu() {
		return jmlh_tu;
	}
	public void setJmlh_tu(Double jmlh_tu) {
		this.jmlh_tu = jmlh_tu;
	}
	public Double getJumTop_Upx() {
		return jumTop_Upx;
	}
	public void setJumTop_Upx(Double jumTop_Upx) {
		this.jumTop_Upx = jumTop_Upx;
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
	public String getNama_topup() {
		return nama_topup;
	}
	public void setNama_topup(String nama_topup) {
		this.nama_topup = nama_topup;
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
	public String getTopup() {
		return topup;
	}
	public void setTopup(String topup) {
		this.topup = topup;
	}
	public Double getTotal_tu() {
		return total_tu;
	}
	public void setTotal_tu(Double total_tu) {
		this.total_tu = total_tu;
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
	/**	1=Pemutusan Kontrak (PK)
	 *  2=Pengambilan Nilai Tunai (NT)
	 *  3=Redemption Unit link / Penarikan Unit Link (RD)
	 *  4=new Business (NB)
	 * @param jns_transaksi
	 */
	public void setJns_transaksi(Integer jns_transaksi) {
		this.jns_transaksi = jns_transaksi;
	}
	public Date getMssur_tgl_input() {
		return mssur_tgl_input;
	}
	public void setMssur_tgl_input(Date mssur_tgl_input) {
		this.mssur_tgl_input = mssur_tgl_input;
	}
	public Integer getFlagTDirec() {
		return flagTDirec;
	}
	public void setFlagTDirec(Integer flagTDirec) {
		this.flagTDirec = flagTDirec;
	}
	public Integer getFlagTUkm() {
		return flagTUkm;
	}
	public void setFlagTUkm(Integer flagTUkm) {
		this.flagTUkm = flagTUkm;
	}
	public Integer getFlagTUw() {
		return flagTUw;
	}
	public void setFlagTUw(Integer flagTUw) {
		this.flagTUw = flagTUw;
	}
	public Integer getFlagYDirec() {
		return flagYDirec;
	}
	public void setFlagYDirec(Integer flagYDirec) {
		this.flagYDirec = flagYDirec;
	}
	public Integer getFlagYUkm() {
		return flagYUkm;
	}
	public void setFlagYUkm(Integer flagYUkm) {
		this.flagYUkm = flagYUkm;
	}
	public Integer getFlagYUw() {
		return flagYUw;
	}
	public void setFlagYUw(Integer flagYUw) {
		this.flagYUw = flagYUw;
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
	public String getStatus_topUp() {
		return status_topUp;
	}
	public void setStatus_topUp(String status_topUp) {
		this.status_topUp = status_topUp;
	}
	public Integer getTop_ke() {
		return top_ke;
	}
	public void setTop_ke(Integer top_ke) {
		this.top_ke = top_ke;
	}

}
