package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Policy implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;

	private Integer lstb_id;

	private String lca_id;

	private String lwk_id;

	private String lsrg_id;

	private String mpi_mou;

	private Integer lssp_id;

	private Integer lspd_id;

	private String mspo_policy_holder;

	private Integer lsre_id;

	private Integer mspo_age;

	private String lku_id;

	private Integer lscb_id;

	private Integer lus_id;

	private String mspo_policy_no;

	private Date mspo_spaj_date;

	private Integer mspo_ins_period;

	private Integer mspo_pay_period;

	private Integer mspo_proses_bill;

	private Date mspo_next_bill;

	private Integer mspo_installment;

	private Integer mspo_flat;

	private Integer mspo_ref_bii;

	private Integer mspo_jn_coas;

	private Date mspo_date_print;

	private Date mspo_date_spec;

	private Date mspo_date_ttp;

	private Integer mspo_pribadi;

	private String mspo_ao;

	private String mspo_cutomer;

	private String mspo_no_blanko;

	private String mspo_no_pol_asm;

	private Double mspo_under_table;

	private Integer mspo_ins_bulan;

	private String mspo_beasiswa;

	private Integer mspo_lama_beasiswa;

	private Integer mspo_cara_bayar;

	private Integer mspo_umur_beasiswa;

	private String mspo_oldpol_eb;

	private Integer mspo_follow_up;

	private Integer mspo_komisi_bii;

	private Date mspo_input_date;

	private Date mspo_beg_date;

	private Date mspo_end_date;

	private Integer mspo_provider;

	private Integer mspo_preexisting;

	private Double mspo_extend;

	private String mspo_pass_eb;

	private Double mspo_biaya_transfer;

	private Integer mspo_as_charges;

	private String mspo_plan_provider;
	
	private Integer mspo_self_ins;

	private Integer mspo_aso;

	private Integer lstp_id;

	private String mspo_note;

	private String mspo_no_kerjasama;

	private String mspo_nasabah_acc;

	private String mspo_nasabah_dcif;

	private String mspo_policy_no_format;

	private Integer mspo_syariah;

	private Integer mspo_cash_back;

	private Date mste_tgl_recur;

	private Integer mste_flag_recur;

	private Integer mste_standard;

	private String mspo_customer;

	private Integer mspo_cara_beasiswa;

	private Integer mspo_syahriah;

	private String mspo_cash_mkt;

	private String mspo_cash_entry;

	private String mspo_cash_verifikasi;

	private String mspo_cash_dpp;

	private Integer mspo_cash_wil;

	private Double mspo_cash_rate_1;

	private Double mspo_cash_rate_2;
	
	private Date mste_kyc_date;
	
	private String msag_id;
	private Integer cek;

	private String mcl_first;
	
	private String lsdbs_name;
	
	private String tertanggung;
	
	private String mspr_premium;
	
	private String mspr_tsi;
	
	public String getMspr_tsi(){
		return mspr_tsi;
	}
	public void setMspr_tsi(String mspr_tsi){
		this.mspr_tsi = mspr_tsi;
	}
	
	public String getMspr_premium(){
		return mspr_premium;
	}
	public void setMspr_premium(String mspr_premium){
		this.mspr_premium = mspr_premium;
	}
	
	public String getTertanggung(){
		return tertanggung;
	}
	public void setTertanggung(String tertanggung){
		this.tertanggung = tertanggung;
	}
	
	public String getLsdbs_name(){
		return lsdbs_name;
	}
	public void setLsdbs_name (String lsdbs_name){
		this.lsdbs_name = lsdbs_name;
	}
	
	public String getMcl_first(){
		return mcl_first;
	}
	public void setMcl_first(String mcl_first){
		this.mcl_first = mcl_first;
	}
	
	public Integer getCek() {
		return cek;
	}

	public void setCek(Integer cek) {
		this.cek = cek;
	}

	public String getMsag_id() {
		return msag_id;
	}

	public void setMsag_id(String msag_id) {
		this.msag_id = msag_id;
	}

	public Integer getMspo_cara_beasiswa() {
		return mspo_cara_beasiswa;
	}

	public void setMspo_cara_beasiswa(Integer mspo_cara_beasiswa) {
		this.mspo_cara_beasiswa = mspo_cara_beasiswa;
	}

	public String getMspo_cash_dpp() {
		return mspo_cash_dpp;
	}

	public void setMspo_cash_dpp(String mspo_cash_dpp) {
		this.mspo_cash_dpp = mspo_cash_dpp;
	}

	public String getMspo_cash_entry() {
		return mspo_cash_entry;
	}

	public void setMspo_cash_entry(String mspo_cash_entry) {
		this.mspo_cash_entry = mspo_cash_entry;
	}

	public String getMspo_cash_mkt() {
		return mspo_cash_mkt;
	}

	public void setMspo_cash_mkt(String mspo_cash_mkt) {
		this.mspo_cash_mkt = mspo_cash_mkt;
	}

	public Double getMspo_cash_rate_1() {
		return mspo_cash_rate_1;
	}

	public void setMspo_cash_rate_1(Double mspo_cash_rate_1) {
		this.mspo_cash_rate_1 = mspo_cash_rate_1;
	}

	public Double getMspo_cash_rate_2() {
		return mspo_cash_rate_2;
	}

	public void setMspo_cash_rate_2(Double mspo_cash_rate_2) {
		this.mspo_cash_rate_2 = mspo_cash_rate_2;
	}

	public String getMspo_cash_verifikasi() {
		return mspo_cash_verifikasi;
	}

	public void setMspo_cash_verifikasi(String mspo_cash_verifikasi) {
		this.mspo_cash_verifikasi = mspo_cash_verifikasi;
	}

	public Integer getMspo_cash_wil() {
		return mspo_cash_wil;
	}

	public void setMspo_cash_wil(Integer mspo_cash_wil) {
		this.mspo_cash_wil = mspo_cash_wil;
	}

	public String getMspo_customer() {
		return mspo_customer;
	}

	public void setMspo_customer(String mspo_customer) {
		this.mspo_customer = mspo_customer;
	}

	public Integer getMspo_syahriah() {
		return mspo_syahriah;
	}

	public void setMspo_syahriah(Integer mspo_syahriah) {
		this.mspo_syahriah = mspo_syahriah;
	}

	public Policy() {

	}

	public Policy(String reg_spaj, Integer lstb_id, Integer lssp_id,
			Integer lspd_id, Integer lus_id) {
		this.reg_spaj = reg_spaj;
		this.lstb_id = lstb_id;
		this.lssp_id = lssp_id;
		this.lspd_id = lspd_id;
		this.lus_id = lus_id;
	}

	public String getLku_id() {
		return lku_id;
	}

	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}

	public Integer getLscb_id() {
		return lscb_id;
	}

	public void setLscb_id(Integer lscb_id) {
		this.lscb_id = lscb_id;
	}

	public Integer getLstp_id() {
		return lstp_id;
	}

	public void setLstp_id(Integer lstp_id) {
		this.lstp_id = lstp_id;
	}

	public Integer getLus_id() {
		return lus_id;
	}

	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}

	public String getMspo_ao() {
		return mspo_ao;
	}

	public void setMspo_ao(String mspo_ao) {
		this.mspo_ao = mspo_ao;
	}

	public Integer getMspo_as_charges() {
		return mspo_as_charges;
	}

	public void setMspo_as_charges(Integer mspo_as_charges) {
		this.mspo_as_charges = mspo_as_charges;
	}

	public Integer getMspo_aso() {
		return mspo_aso;
	}

	public void setMspo_aso(Integer mspo_aso) {
		this.mspo_aso = mspo_aso;
	}

	public String getMspo_beasiswa() {
		return mspo_beasiswa;
	}

	public void setMspo_beasiswa(String mspo_beasiswa) {
		this.mspo_beasiswa = mspo_beasiswa;
	}

	public Date getMspo_beg_date() {
		return mspo_beg_date;
	}

	public void setMspo_beg_date(Date mspo_beg_date) {
		this.mspo_beg_date = mspo_beg_date;
	}

	public Double getMspo_biaya_transfer() {
		return mspo_biaya_transfer;
	}

	public void setMspo_biaya_transfer(Double mspo_biaya_transfer) {
		this.mspo_biaya_transfer = mspo_biaya_transfer;
	}

	public Integer getMspo_cara_bayar() {
		return mspo_cara_bayar;
	}

	public void setMspo_cara_bayar(Integer mspo_cara_bayar) {
		this.mspo_cara_bayar = mspo_cara_bayar;
	}

	public Integer getMspo_cash_back() {
		return mspo_cash_back;
	}

	public void setMspo_cash_back(Integer mspo_cash_back) {
		this.mspo_cash_back = mspo_cash_back;
	}

	public String getMspo_cutomer() {
		return mspo_cutomer;
	}

	public void setMspo_cutomer(String mspo_cutomer) {
		this.mspo_cutomer = mspo_cutomer;
	}

	public Date getMspo_date_print() {
		return mspo_date_print;
	}

	public void setMspo_date_print(Date mspo_date_print) {
		this.mspo_date_print = mspo_date_print;
	}

	public Date getMspo_date_spec() {
		return mspo_date_spec;
	}

	public void setMspo_date_spec(Date mspo_date_spec) {
		this.mspo_date_spec = mspo_date_spec;
	}

	public Date getMspo_date_ttp() {
		return mspo_date_ttp;
	}

	public void setMspo_date_ttp(Date mspo_date_ttp) {
		this.mspo_date_ttp = mspo_date_ttp;
	}

	public Date getMspo_end_date() {
		return mspo_end_date;
	}

	public void setMspo_end_date(Date mspo_end_date) {
		this.mspo_end_date = mspo_end_date;
	}

	public Double getMspo_extend() {
		return mspo_extend;
	}

	public void setMspo_extend(Double mspo_extend) {
		this.mspo_extend = mspo_extend;
	}

	public Integer getMspo_flat() {
		return mspo_flat;
	}

	public void setMspo_flat(Integer mspo_flat) {
		this.mspo_flat = mspo_flat;
	}

	public Integer getMspo_follow_up() {
		return mspo_follow_up;
	}

	public void setMspo_follow_up(Integer mspo_follow_up) {
		this.mspo_follow_up = mspo_follow_up;
	}

	public Date getMspo_input_date() {
		return mspo_input_date;
	}

	public void setMspo_input_date(Date mspo_input_date) {
		this.mspo_input_date = mspo_input_date;
	}

	public Integer getMspo_ins_bulan() {
		return mspo_ins_bulan;
	}

	public void setMspo_ins_bulan(Integer mspo_ins_bulan) {
		this.mspo_ins_bulan = mspo_ins_bulan;
	}

	public Integer getMspo_ins_period() {
		return mspo_ins_period;
	}

	public void setMspo_ins_period(Integer mspo_ins_period) {
		this.mspo_ins_period = mspo_ins_period;
	}

	public Integer getMspo_installment() {
		return mspo_installment;
	}

	public void setMspo_installment(Integer mspo_installment) {
		this.mspo_installment = mspo_installment;
	}

	public Integer getMspo_jn_coas() {
		return mspo_jn_coas;
	}

	public void setMspo_jn_coas(Integer mspo_jn_coas) {
		this.mspo_jn_coas = mspo_jn_coas;
	}

	public Integer getMspo_komisi_bii() {
		return mspo_komisi_bii;
	}

	public void setMspo_komisi_bii(Integer mspo_komisi_bii) {
		this.mspo_komisi_bii = mspo_komisi_bii;
	}

	public Integer getMspo_lama_beasiswa() {
		return mspo_lama_beasiswa;
	}

	public void setMspo_lama_beasiswa(Integer mspo_lama_beasiswa) {
		this.mspo_lama_beasiswa = mspo_lama_beasiswa;
	}

	public String getMspo_nasabah_acc() {
		return mspo_nasabah_acc;
	}

	public void setMspo_nasabah_acc(String mspo_nasabah_acc) {
		this.mspo_nasabah_acc = mspo_nasabah_acc;
	}

	public String getMspo_nasabah_dcif() {
		return mspo_nasabah_dcif;
	}

	public void setMspo_nasabah_dcif(String mspo_nasabah_dcif) {
		this.mspo_nasabah_dcif = mspo_nasabah_dcif;
	}

	public Date getMspo_next_bill() {
		return mspo_next_bill;
	}

	public void setMspo_next_bill(Date mspo_next_bill) {
		this.mspo_next_bill = mspo_next_bill;
	}

	public String getMspo_no_blanko() {
		return mspo_no_blanko;
	}

	public void setMspo_no_blanko(String mspo_no_blanko) {
		this.mspo_no_blanko = mspo_no_blanko;
	}

	public String getMspo_no_kerjasama() {
		return mspo_no_kerjasama;
	}

	public void setMspo_no_kerjasama(String mspo_no_kerjasama) {
		this.mspo_no_kerjasama = mspo_no_kerjasama;
	}

	public String getMspo_no_pol_asm() {
		return mspo_no_pol_asm;
	}

	public void setMspo_no_pol_asm(String mspo_no_pol_asm) {
		this.mspo_no_pol_asm = mspo_no_pol_asm;
	}

	public String getMspo_note() {
		return mspo_note;
	}

	public void setMspo_note(String mspo_note) {
		this.mspo_note = mspo_note;
	}

	public String getMspo_oldpol_eb() {
		return mspo_oldpol_eb;
	}

	public void setMspo_oldpol_eb(String mspo_oldpol_eb) {
		this.mspo_oldpol_eb = mspo_oldpol_eb;
	}

	public String getMspo_pass_eb() {
		return mspo_pass_eb;
	}

	public void setMspo_pass_eb(String mspo_pass_eb) {
		this.mspo_pass_eb = mspo_pass_eb;
	}

	public Integer getMspo_pay_period() {
		return mspo_pay_period;
	}

	public void setMspo_pay_period(Integer mspo_pay_period) {
		this.mspo_pay_period = mspo_pay_period;
	}

	public String getMspo_plan_provider() {
		return mspo_plan_provider;
	}

	public void setMspo_plan_provider(String mspo_plan_provider) {
		this.mspo_plan_provider = mspo_plan_provider;
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

	public Integer getMspo_preexisting() {
		return mspo_preexisting;
	}

	public void setMspo_preexisting(Integer mspo_preexisting) {
		this.mspo_preexisting = mspo_preexisting;
	}

	public Integer getMspo_pribadi() {
		return mspo_pribadi;
	}

	public void setMspo_pribadi(Integer mspo_pribadi) {
		this.mspo_pribadi = mspo_pribadi;
	}

	public Integer getMspo_proses_bill() {
		return mspo_proses_bill;
	}

	public void setMspo_proses_bill(Integer mspo_proses_bill) {
		this.mspo_proses_bill = mspo_proses_bill;
	}

	public Integer getMspo_provider() {
		return mspo_provider;
	}

	public void setMspo_provider(Integer mspo_provider) {
		this.mspo_provider = mspo_provider;
	}

	public Integer getMspo_ref_bii() {
		return mspo_ref_bii;
	}

	public void setMspo_ref_bii(Integer mspo_ref_bii) {
		this.mspo_ref_bii = mspo_ref_bii;
	}

	public Date getMspo_spaj_date() {
		return mspo_spaj_date;
	}

	public void setMspo_spaj_date(Date mspo_spaj_date) {
		this.mspo_spaj_date = mspo_spaj_date;
	}

	public Integer getMspo_syariah() {
		return mspo_syariah;
	}

	public void setMspo_syariah(Integer mspo_syariah) {
		this.mspo_syariah = mspo_syariah;
	}

	public Integer getMspo_umur_beasiswa() {
		return mspo_umur_beasiswa;
	}

	public void setMspo_umur_beasiswa(Integer mspo_umur_beasiswa) {
		this.mspo_umur_beasiswa = mspo_umur_beasiswa;
	}

	public Double getMspo_under_table() {
		return mspo_under_table;
	}

	public void setMspo_under_table(Double mspo_under_table) {
		this.mspo_under_table = mspo_under_table;
	}

	public Integer getMste_flag_recur() {
		return mste_flag_recur;
	}

	public void setMste_flag_recur(Integer mste_flag_recur) {
		this.mste_flag_recur = mste_flag_recur;
	}

	public Integer getMste_standard() {
		return mste_standard;
	}

	public void setMste_standard(Integer mste_standard) {
		this.mste_standard = mste_standard;
	}

	public Date getMste_tgl_recur() {
		return mste_tgl_recur;
	}

	public void setMste_tgl_recur(Date mste_tgl_recur) {
		this.mste_tgl_recur = mste_tgl_recur;
	}

	public String getLca_id() {
		return lca_id;
	}

	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}

	public Integer getLspd_id() {
		return lspd_id;
	}

	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}

	public Integer getLsre_id() {
		return lsre_id;
	}

	public void setLsre_id(Integer lsre_id) {
		this.lsre_id = lsre_id;
	}

	public String getLsrg_id() {
		return lsrg_id;
	}

	public void setLsrg_id(String lsrg_id) {
		this.lsrg_id = lsrg_id;
	}

	public Integer getLssp_id() {
		return lssp_id;
	}

	public void setLssp_id(Integer lssp_id) {
		this.lssp_id = lssp_id;
	}

	public Integer getLstb_id() {
		return lstb_id;
	}

	public void setLstb_id(Integer lstb_id) {
		this.lstb_id = lstb_id;
	}

	public String getLwk_id() {
		return lwk_id;
	}

	public void setLwk_id(String lwk_id) {
		this.lwk_id = lwk_id;
	}

	public String getMpi_mou() {
		return mpi_mou;
	}

	public void setMpi_mou(String mpi_mou) {
		this.mpi_mou = mpi_mou;
	}

	public Integer getMspo_age() {
		return mspo_age;
	}

	public void setMspo_age(Integer mspo_age) {
		this.mspo_age = mspo_age;
	}

	public String getMspo_policy_holder() {
		return mspo_policy_holder;
	}

	public void setMspo_policy_holder(String mspo_policy_holder) {
		this.mspo_policy_holder = mspo_policy_holder;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public Date getMste_kyc_date() {
		return mste_kyc_date;
	}

	public void setMste_kyc_date(Date mste_kyc_date) {
		this.mste_kyc_date = mste_kyc_date;
	}

	public Integer getMspo_self_ins() {
		return mspo_self_ins;
	}

	public void setMspo_self_ins(Integer mspo_self_ins) {
		this.mspo_self_ins = mspo_self_ins;
	}
}
