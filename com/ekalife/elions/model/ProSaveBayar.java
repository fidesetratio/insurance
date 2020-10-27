package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class ProSaveBayar implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mpb_bayar_id;                   
	private Integer mpb_jenis;                     
	private String reg_spaj;                       
	private Integer mpt_quarter;                   
	private Integer mpt_day;                       
	private Integer lus_id;                        
	private String lku_id;                         
	private Date mpb_input_date;                   
	private Date mpb_paid_date;                    
	private Date mpb_due_date;                     
	private String mpb_rekening;                   
	private Double mpb_up;                         
	private Double mpb_prm_deposit;                
	private Double mpb_bunga;                      
	private Double mpb_jum_bayar;                  
	private Date mpb_print_date;                   
	private Date mpb_beg_period;                   
	private Date mpb_end_period;                   
	private Double mpb_rate;                       
	private Double mpb_tambah;                     
	private Integer mpb_hari;                      
	private String mpb_notes;                      
	private Integer mpb_filing;                    
	private Date mpb_filing_date;                  
	private Integer mpb_flag_bs;
	private String nama_cabang;
	private String lsdbs_name;
	private String pemegang;
	private String tertanggung;
	private String status;
	private String lus_login_name;
	private Integer flag_check;
	private String mspo_policy_no;
	private String lku_symbol;
	private String mspo_policy_no_format;
	private String lsbp_nama;
		
	public String getLsbp_nama() {
		return lsbp_nama;
	}
	public void setLsbp_nama(String lsbp_nama) {
		this.lsbp_nama = lsbp_nama;
	}
	public String getLku_symbol() {
		return lku_symbol;
	}
	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public String getMspo_policy_no_format() {
		return mspo_policy_no_format;
	}
	public void setMspo_policy_no_format(String mspo_policy_no_format) {
		this.mspo_policy_no_format = mspo_policy_no_format;
	}
	public Integer getFlag_check() {
		return flag_check;
	}
	public void setFlag_check(Integer flag_check) {
		this.flag_check = flag_check;
	}
	public String getLus_login_name() {
		return lus_login_name;
	}
	public void setLus_login_name(String lus_login_name) {
		this.lus_login_name = lus_login_name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getMpb_bayar_id() {
		return mpb_bayar_id;
	}
	public void setMpb_bayar_id(String mpb_bayar_id) {
		this.mpb_bayar_id = mpb_bayar_id;
	}
	public Date getMpb_beg_period() {
		return mpb_beg_period;
	}
	public void setMpb_beg_period(Date mpb_beg_period) {
		this.mpb_beg_period = mpb_beg_period;
	}
	public Double getMpb_bunga() {
		return mpb_bunga;
	}
	public void setMpb_bunga(Double mpb_bunga) {
		this.mpb_bunga = mpb_bunga;
	}
	public Date getMpb_due_date() {
		return mpb_due_date;
	}
	public void setMpb_due_date(Date mpb_due_date) {
		this.mpb_due_date = mpb_due_date;
	}
	public Date getMpb_end_period() {
		return mpb_end_period;
	}
	public void setMpb_end_period(Date mpb_end_period) {
		this.mpb_end_period = mpb_end_period;
	}
	public Integer getMpb_filing() {
		return mpb_filing;
	}
	public void setMpb_filing(Integer mpb_filing) {
		this.mpb_filing = mpb_filing;
	}
	public Date getMpb_filing_date() {
		return mpb_filing_date;
	}
	public void setMpb_filing_date(Date mpb_filing_date) {
		this.mpb_filing_date = mpb_filing_date;
	}
	public Integer getMpb_flag_bs() {
		return mpb_flag_bs;
	}
	public void setMpb_flag_bs(Integer mpb_flag_bs) {
		this.mpb_flag_bs = mpb_flag_bs;
	}
	public Integer getMpb_hari() {
		return mpb_hari;
	}
	public void setMpb_hari(Integer mpb_hari) {
		this.mpb_hari = mpb_hari;
	}
	public Date getMpb_input_date() {
		return mpb_input_date;
	}
	public void setMpb_input_date(Date mpb_input_date) {
		this.mpb_input_date = mpb_input_date;
	}
	public Integer getMpb_jenis() {
		return mpb_jenis;
	}
	public void setMpb_jenis(Integer mpb_jenis) {
		this.mpb_jenis = mpb_jenis;
	}
	public Double getMpb_jum_bayar() {
		return mpb_jum_bayar;
	}
	public void setMpb_jum_bayar(Double mpb_jum_bayar) {
		this.mpb_jum_bayar = mpb_jum_bayar;
	}
	public String getMpb_notes() {
		return mpb_notes;
	}
	public void setMpb_notes(String mpb_notes) {
		this.mpb_notes = mpb_notes;
	}
	public Date getMpb_paid_date() {
		return mpb_paid_date;
	}
	public void setMpb_paid_date(Date mpb_paid_date) {
		this.mpb_paid_date = mpb_paid_date;
	}
	public Date getMpb_print_date() {
		return mpb_print_date;
	}
	public void setMpb_print_date(Date mpb_print_date) {
		this.mpb_print_date = mpb_print_date;
	}
	public Double getMpb_prm_deposit() {
		return mpb_prm_deposit;
	}
	public void setMpb_prm_deposit(Double mpb_prm_deposit) {
		this.mpb_prm_deposit = mpb_prm_deposit;
	}
	public Double getMpb_rate() {
		return mpb_rate;
	}
	public void setMpb_rate(Double mpb_rate) {
		this.mpb_rate = mpb_rate;
	}
	public String getMpb_rekening() {
		return mpb_rekening;
	}
	public void setMpb_rekening(String mpb_rekening) {
		this.mpb_rekening = mpb_rekening;
	}
	public Double getMpb_tambah() {
		return mpb_tambah;
	}
	public void setMpb_tambah(Double mpb_tambah) {
		this.mpb_tambah = mpb_tambah;
	}
	public Double getMpb_up() {
		return mpb_up;
	}
	public void setMpb_up(Double mpb_up) {
		this.mpb_up = mpb_up;
	}
	public Integer getMpt_day() {
		return mpt_day;
	}
	public void setMpt_day(Integer mpt_day) {
		this.mpt_day = mpt_day;
	}
	public Integer getMpt_quarter() {
		return mpt_quarter;
	}
	public void setMpt_quarter(Integer mpt_quarter) {
		this.mpt_quarter = mpt_quarter;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}
	public String getNama_cabang() {
		return nama_cabang;
	}
	public void setNama_cabang(String nama_cabang) {
		this.nama_cabang = nama_cabang;
	}
	public String getPemegang() {
		return pemegang;
	}
	public void setPemegang(String pemegang) {
		this.pemegang = pemegang;
	}
	public String getTertanggung() {
		return tertanggung;
	}
	public void setTertanggung(String tertanggung) {
		this.tertanggung = tertanggung;
	}
	
	
}


