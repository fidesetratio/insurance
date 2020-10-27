package com.ekalife.elions.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Maturity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//Dari Tabel Utama mst_powersave_ro
	private String reg_spaj;                       
	private Integer mps_kode;                      
	private Date mps_deposit_date;                 
	private Date mpr_mature_date;                  
	private Integer mpr_jangka_invest;             
	private Double mpr_rate;                       
	private Double mpr_deposit;                    
	private Double mpr_interest;                   
	private Double mpr_tax;                        
	private Double mpr_insurance;                  
	private Double mpr_debet;                      
	private Integer mpr_print;                     
	private Date mpr_print_date;                   
	private String mpr_print_info;                 
	private String mpr_note;                       
	private Date mpr_input_date;                   
	private Integer lus_id;                        
	private Integer mpr_flag_comm;                 
	private Integer mpr_bayar_prm;                 
	private Integer mpr_nett_tax;                  
	private Date mpr_tgl_rate;                     
	private Integer mpr_jns_ro;                    
	private Date mpr_tgl_confirm;                  
	private Integer mpr_hari;                      
	private Integer mpr_jns_rumus;                 
	private Integer mpr_hari_lebih;                
	private Double mpr_rate_lebih;                 
	private Date mpr_tgl_prod;                     
	private Integer mpr_breakable;                 
	private Integer msbi_tahun_ke;                 
	private Integer msbi_premi_ke; 
	
	//Dari tabel tambahan
	private String pemegang;
	private String mcl_first;
	private String tertanggung;
	private String mspo_policy_no_format;
	private String wilayah;
	private String cabang_induk;
	private String nama_cabang;
	private String lku_id;
	private String lku_symbol;
	private String mspo_nasabah_dcif;
	private String lus_login_name;
	private String lsbs_name;
	private String lsdbs_name;
	private String hasil_mgi;
	private BigDecimal mspr_premium;
	private BigDecimal mspr_tsi;
	private Integer mspo_ins_period;
	private Date mspo_date_print;
	private Date mspr_beg_date;
	private Date mste_tgl_aksep;
	private Date mssur_tgl_klaim;
	private Date mspin_tgl_keluar;
	private Date tgl_cair;
	private Date tgl_penempatan;
	private Date tgl_jatuhtempo;
	
	public String getCabang_induk() {
		return cabang_induk;
	}
	public void setCabang_induk(String cabang_induk) {
		this.cabang_induk = cabang_induk;
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
	public String getLsbs_name() {
		return lsbs_name;
	}
	public void setLsbs_name(String lsbs_name) {
		this.lsbs_name = lsbs_name;
	}
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getLus_login_name() {
		return lus_login_name;
	}
	public void setLus_login_name(String lus_login_name) {
		this.lus_login_name = lus_login_name;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public Integer getMpr_bayar_prm() {
		return mpr_bayar_prm;
	}
	public void setMpr_bayar_prm(Integer mpr_bayar_prm) {
		this.mpr_bayar_prm = mpr_bayar_prm;
	}
	public Integer getMpr_breakable() {
		return mpr_breakable;
	}
	public void setMpr_breakable(Integer mpr_breakable) {
		this.mpr_breakable = mpr_breakable;
	}
	public Double getMpr_debet() {
		return mpr_debet;
	}
	public void setMpr_debet(Double mpr_debet) {
		this.mpr_debet = mpr_debet;
	}
	public Double getMpr_deposit() {
		return mpr_deposit;
	}
	public void setMpr_deposit(Double mpr_deposit) {
		this.mpr_deposit = mpr_deposit;
	}
	public Integer getMpr_flag_comm() {
		return mpr_flag_comm;
	}
	public void setMpr_flag_comm(Integer mpr_flag_comm) {
		this.mpr_flag_comm = mpr_flag_comm;
	}
	public Integer getMpr_hari() {
		return mpr_hari;
	}
	public void setMpr_hari(Integer mpr_hari) {
		this.mpr_hari = mpr_hari;
	}
	public Integer getMpr_hari_lebih() {
		return mpr_hari_lebih;
	}
	public void setMpr_hari_lebih(Integer mpr_hari_lebih) {
		this.mpr_hari_lebih = mpr_hari_lebih;
	}
	public Date getMpr_input_date() {
		return mpr_input_date;
	}
	public void setMpr_input_date(Date mpr_input_date) {
		this.mpr_input_date = mpr_input_date;
	}
	public Double getMpr_insurance() {
		return mpr_insurance;
	}
	public void setMpr_insurance(Double mpr_insurance) {
		this.mpr_insurance = mpr_insurance;
	}
	public Double getMpr_interest() {
		return mpr_interest;
	}
	public void setMpr_interest(Double mpr_interest) {
		this.mpr_interest = mpr_interest;
	}
	public Integer getMpr_jangka_invest() {
		return mpr_jangka_invest;
	}
	public void setMpr_jangka_invest(Integer mpr_jangka_invest) {
		this.mpr_jangka_invest = mpr_jangka_invest;
	}
	public Integer getMpr_jns_ro() {
		return mpr_jns_ro;
	}
	public void setMpr_jns_ro(Integer mpr_jns_ro) {
		this.mpr_jns_ro = mpr_jns_ro;
	}
	public Integer getMpr_jns_rumus() {
		return mpr_jns_rumus;
	}
	public void setMpr_jns_rumus(Integer mpr_jns_rumus) {
		this.mpr_jns_rumus = mpr_jns_rumus;
	}
	public Date getMpr_mature_date() {
		return mpr_mature_date;
	}
	public void setMpr_mature_date(Date mpr_mature_date) {
		this.mpr_mature_date = mpr_mature_date;
	}
	public Integer getMpr_nett_tax() {
		return mpr_nett_tax;
	}
	public void setMpr_nett_tax(Integer mpr_nett_tax) {
		this.mpr_nett_tax = mpr_nett_tax;
	}
	public String getMpr_note() {
		return mpr_note;
	}
	public void setMpr_note(String mpr_note) {
		this.mpr_note = mpr_note;
	}
	public Integer getMpr_print() {
		return mpr_print;
	}
	public void setMpr_print(Integer mpr_print) {
		this.mpr_print = mpr_print;
	}
	public Date getMpr_print_date() {
		return mpr_print_date;
	}
	public void setMpr_print_date(Date mpr_print_date) {
		this.mpr_print_date = mpr_print_date;
	}
	public String getMpr_print_info() {
		return mpr_print_info;
	}
	public void setMpr_print_info(String mpr_print_info) {
		this.mpr_print_info = mpr_print_info;
	}
	public Double getMpr_rate() {
		return mpr_rate;
	}
	public void setMpr_rate(Double mpr_rate) {
		this.mpr_rate = mpr_rate;
	}
	public Double getMpr_rate_lebih() {
		return mpr_rate_lebih;
	}
	public void setMpr_rate_lebih(Double mpr_rate_lebih) {
		this.mpr_rate_lebih = mpr_rate_lebih;
	}
	public Double getMpr_tax() {
		return mpr_tax;
	}
	public void setMpr_tax(Double mpr_tax) {
		this.mpr_tax = mpr_tax;
	}
	public Date getMpr_tgl_confirm() {
		return mpr_tgl_confirm;
	}
	public void setMpr_tgl_confirm(Date mpr_tgl_confirm) {
		this.mpr_tgl_confirm = mpr_tgl_confirm;
	}
	public Date getMpr_tgl_prod() {
		return mpr_tgl_prod;
	}
	public void setMpr_tgl_prod(Date mpr_tgl_prod) {
		this.mpr_tgl_prod = mpr_tgl_prod;
	}
	public Date getMpr_tgl_rate() {
		return mpr_tgl_rate;
	}
	public void setMpr_tgl_rate(Date mpr_tgl_rate) {
		this.mpr_tgl_rate = mpr_tgl_rate;
	}
	public Date getMps_deposit_date() {
		return mps_deposit_date;
	}
	public void setMps_deposit_date(Date mps_deposit_date) {
		this.mps_deposit_date = mps_deposit_date;
	}
	public Integer getMps_kode() {
		return mps_kode;
	}
	public void setMps_kode(Integer mps_kode) {
		this.mps_kode = mps_kode;
	}
	public Integer getMsbi_premi_ke() {
		return msbi_premi_ke;
	}
	public void setMsbi_premi_ke(Integer msbi_premi_ke) {
		this.msbi_premi_ke = msbi_premi_ke;
	}
	public Integer getMsbi_tahun_ke() {
		return msbi_tahun_ke;
	}
	public void setMsbi_tahun_ke(Integer msbi_tahun_ke) {
		this.msbi_tahun_ke = msbi_tahun_ke;
	}
	public Date getMspin_tgl_keluar() {
		return mspin_tgl_keluar;
	}
	public void setMspin_tgl_keluar(Date mspin_tgl_keluar) {
		this.mspin_tgl_keluar = mspin_tgl_keluar;
	}
	public Date getMspo_date_print() {
		return mspo_date_print;
	}
	public void setMspo_date_print(Date mspo_date_print) {
		this.mspo_date_print = mspo_date_print;
	}
	public Integer getMspo_ins_period() {
		return mspo_ins_period;
	}
	public void setMspo_ins_period(Integer mspo_ins_period) {
		this.mspo_ins_period = mspo_ins_period;
	}
	public String getMspo_nasabah_dcif() {
		return mspo_nasabah_dcif;
	}
	public void setMspo_nasabah_dcif(String mspo_nasabah_dcif) {
		this.mspo_nasabah_dcif = mspo_nasabah_dcif;
	}
	public String getMspo_policy_no_format() {
		return mspo_policy_no_format;
	}
	public void setMspo_policy_no_format(String mspo_policy_no_format) {
		this.mspo_policy_no_format = mspo_policy_no_format;
	}
	public Date getMspr_beg_date() {
		return mspr_beg_date;
	}
	public void setMspr_beg_date(Date mspr_beg_date) {
		this.mspr_beg_date = mspr_beg_date;
	}
	public BigDecimal getMspr_premium() {
		return mspr_premium;
	}
	public void setMspr_premium(BigDecimal mspr_premium) {
		this.mspr_premium = mspr_premium;
	}
	public BigDecimal getMspr_tsi() {
		return mspr_tsi;
	}
	public void setMspr_tsi(BigDecimal mspr_tsi) {
		this.mspr_tsi = mspr_tsi;
	}
	public Date getMssur_tgl_klaim() {
		return mssur_tgl_klaim;
	}
	public void setMssur_tgl_klaim(Date mssur_tgl_klaim) {
		this.mssur_tgl_klaim = mssur_tgl_klaim;
	}
	public Date getMste_tgl_aksep() {
		return mste_tgl_aksep;
	}
	public void setMste_tgl_aksep(Date mste_tgl_aksep) {
		this.mste_tgl_aksep = mste_tgl_aksep;
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
	public Date getTgl_cair() {
		return tgl_cair;
	}
	public void setTgl_cair(Date tgl_cair) {
		this.tgl_cair = tgl_cair;
	}
	public String getWilayah() {
		return wilayah;
	}
	public void setWilayah(String wilayah) {
		this.wilayah = wilayah;
	}
	public String getHasil_mgi() {
		return hasil_mgi;
	}
	public void setHasil_mgi(String hasil_mgi) {
		this.hasil_mgi = hasil_mgi;
	}
	public Date getTgl_jatuhtempo() {
		return tgl_jatuhtempo;
	}
	public void setTgl_jatuhtempo(Date tgl_jatuhtempo) {
		this.tgl_jatuhtempo = tgl_jatuhtempo;
	}
	public Date getTgl_penempatan() {
		return tgl_penempatan;
	}
	public void setTgl_penempatan(Date tgl_penempatan) {
		this.tgl_penempatan = tgl_penempatan;
	}
	
	
	
}