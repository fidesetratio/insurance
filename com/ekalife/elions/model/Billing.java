package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Billing implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private String mspo_policy_no;
	private String reg_spaj;
	private Integer msbi_tahun_ke;
	private Integer msbi_premi_ke;
	private String msbi_bill_no;
	private Date msbi_beg_date;
	private Date msbi_end_date;
	private Date msbi_due_date;
	private Date msbi_aktif_date;
	private String lku_id;
	private Double msbi_policy_cost;
	private Double msbi_hcr_policy_cost;
	private Double msbi_ttl_card_cost;
	private Double msbi_stamp;
	private Date msbi_input_date;
	private Double msbi_remain;
	private Integer msbi_paid;
	private Integer msbi_active;
	private Integer msbi_print;
	private Integer msbi_add_bill;
	private Integer msbi_flag_sisa;
	private Integer lus_id;
	private Integer lspd_id;
	private Date msbi_paid_date;
	private Date msbi_tgl_batal;
	private String msbi_hcr_no;
	private String lca_id;
	private String lwk_id;
	private String lsrg_id;
	private Date msbi_print_date;
	private Integer msbi_gross;
	private Integer msbi_flag_recur;
	private Integer msbi_sms;
	private Integer msbi_flag_topup;
	private Integer msbi_persen_paid;
	private String lku_symbol;
	private Integer msbi_flag_kuitansi;
	
	private Double biaya;
	private Double bayar;
	private Double sisa;
	private Integer pilih;
	
	public Integer getMsbi_flag_kuitansi() {
		return msbi_flag_kuitansi;
	}
	public void setMsbi_flag_kuitansi(Integer msbi_flag_kuitansi) {
		this.msbi_flag_kuitansi = msbi_flag_kuitansi;
	}
	
	public Integer getPilih() {
		return pilih;
	}
	public void setPilih(Integer pilih) {
		this.pilih = pilih;
	}
	public Double getBayar() {
		return bayar;
	}
	public void setBayar(Double bayar) {
		this.bayar = bayar;
	}
	public Double getBiaya() {
		return biaya;
	}
	public void setBiaya(Double biaya) {
		this.biaya = biaya;
	}
	public Double getSisa() {
		return sisa;
	}
	public void setSisa(Double sisa) {
		this.sisa = sisa;
	}
	public String getLku_symbol() {
		return lku_symbol;
	}
	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Integer getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}
	public String getLsrg_id() {
		return lsrg_id;
	}
	public void setLsrg_id(String lsrg_id) {
		this.lsrg_id = lsrg_id;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getLwk_id() {
		return lwk_id;
	}
	public void setLwk_id(String lwk_id) {
		this.lwk_id = lwk_id;
	}
	public Integer getMsbi_active() {
		return msbi_active;
	}
	public void setMsbi_active(Integer msbi_active) {
		this.msbi_active = msbi_active;
	}
	public Integer getMsbi_add_bill() {
		return msbi_add_bill;
	}
	public void setMsbi_add_bill(Integer msbi_add_bill) {
		this.msbi_add_bill = msbi_add_bill;
	}
	public Date getMsbi_aktif_date() {
		return msbi_aktif_date;
	}
	public void setMsbi_aktif_date(Date msbi_aktif_date) {
		this.msbi_aktif_date = msbi_aktif_date;
	}
	public Date getMsbi_beg_date() {
		return msbi_beg_date;
	}
	public void setMsbi_beg_date(Date msbi_beg_date) {
		this.msbi_beg_date = msbi_beg_date;
	}
	public String getMsbi_bill_no() {
		return msbi_bill_no;
	}
	public void setMsbi_bill_no(String msbi_bill_no) {
		this.msbi_bill_no = msbi_bill_no;
	}
	public Date getMsbi_due_date() {
		return msbi_due_date;
	}
	public void setMsbi_due_date(Date msbi_due_date) {
		this.msbi_due_date = msbi_due_date;
	}
	public Date getMsbi_end_date() {
		return msbi_end_date;
	}
	public void setMsbi_end_date(Date msbi_end_date) {
		this.msbi_end_date = msbi_end_date;
	}
	public Integer getMsbi_flag_recur() {
		return msbi_flag_recur;
	}
	public void setMsbi_flag_recur(Integer msbi_flag_recur) {
		this.msbi_flag_recur = msbi_flag_recur;
	}
	public Integer getMsbi_flag_sisa() {
		return msbi_flag_sisa;
	}
	public void setMsbi_flag_sisa(Integer msbi_flag_sisa) {
		this.msbi_flag_sisa = msbi_flag_sisa;
	}
	public Integer getMsbi_flag_topup() {
		return msbi_flag_topup;
	}
	public void setMsbi_flag_topup(Integer msbi_flag_topup) {
		this.msbi_flag_topup = msbi_flag_topup;
	}
	public Integer getMsbi_gross() {
		return msbi_gross;
	}
	public void setMsbi_gross(Integer msbi_gross) {
		this.msbi_gross = msbi_gross;
	}
	public String getMsbi_hcr_no() {
		return msbi_hcr_no;
	}
	public void setMsbi_hcr_no(String msbi_hcr_no) {
		this.msbi_hcr_no = msbi_hcr_no;
	}
	public Double getMsbi_hcr_policy_cost() {
		return msbi_hcr_policy_cost;
	}
	public void setMsbi_hcr_policy_cost(Double msbi_hcr_policy_cost) {
		this.msbi_hcr_policy_cost = msbi_hcr_policy_cost;
	}
	public Date getMsbi_input_date() {
		return msbi_input_date;
	}
	public void setMsbi_input_date(Date msbi_input_date) {
		this.msbi_input_date = msbi_input_date;
	}
	public Integer getMsbi_paid() {
		return msbi_paid;
	}
	public void setMsbi_paid(Integer msbi_paid) {
		this.msbi_paid = msbi_paid;
	}
	public Date getMsbi_paid_date() {
		return msbi_paid_date;
	}
	public void setMsbi_paid_date(Date msbi_paid_date) {
		this.msbi_paid_date = msbi_paid_date;
	}
	public Integer getMsbi_persen_paid() {
		return msbi_persen_paid;
	}
	public void setMsbi_persen_paid(Integer msbi_persen_paid) {
		this.msbi_persen_paid = msbi_persen_paid;
	}
	public Double getMsbi_policy_cost() {
		return msbi_policy_cost;
	}
	public void setMsbi_policy_cost(Double msbi_policy_cost) {
		this.msbi_policy_cost = msbi_policy_cost;
	}
	public Integer getMsbi_premi_ke() {
		return msbi_premi_ke;
	}
	public void setMsbi_premi_ke(Integer msbi_premi_ke) {
		this.msbi_premi_ke = msbi_premi_ke;
	}
	public Integer getMsbi_print() {
		return msbi_print;
	}
	public void setMsbi_print(Integer msbi_print) {
		this.msbi_print = msbi_print;
	}
	public Date getMsbi_print_date() {
		return msbi_print_date;
	}
	public void setMsbi_print_date(Date msbi_print_date) {
		this.msbi_print_date = msbi_print_date;
	}
	public Double getMsbi_remain() {
		return msbi_remain;
	}
	public void setMsbi_remain(Double msbi_remain) {
		this.msbi_remain = msbi_remain;
	}
	public Integer getMsbi_sms() {
		return msbi_sms;
	}
	public void setMsbi_sms(Integer msbi_sms) {
		this.msbi_sms = msbi_sms;
	}
	public Double getMsbi_stamp() {
		return msbi_stamp;
	}
	public void setMsbi_stamp(Double msbi_stamp) {
		this.msbi_stamp = msbi_stamp;
	}
	public Integer getMsbi_tahun_ke() {
		return msbi_tahun_ke;
	}
	public void setMsbi_tahun_ke(Integer msbi_tahun_ke) {
		this.msbi_tahun_ke = msbi_tahun_ke;
	}
	public Date getMsbi_tgl_batal() {
		return msbi_tgl_batal;
	}
	public void setMsbi_tgl_batal(Date msbi_tgl_batal) {
		this.msbi_tgl_batal = msbi_tgl_batal;
	}
	public Double getMsbi_ttl_card_cost() {
		return msbi_ttl_card_cost;
	}
	public void setMsbi_ttl_card_cost(Double msbi_ttl_card_cost) {
		this.msbi_ttl_card_cost = msbi_ttl_card_cost;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	
	
}
