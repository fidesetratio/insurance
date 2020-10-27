package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Class buat input topup
 * 
 * @author Yusuf
 * @since Jan 19, 2009 (8:31:12 AM)
 */
public class InputTopup implements Serializable {

	private static final long serialVersionUID = 2579770708787566275L;
	public Integer flag_bulanan;
	public Integer msl_proses_bsm;
	public Integer flag_special;
	
	public String lku_id;
	public String mspo_policy_no;
	public String reg_spaj;
	public Date mste_beg_date;
	public Date mste_end_date;
	public Double mspr_tsi;
	public Double mspr_premium;
	public String lsdbs_name;
	public Integer lssp_id;
	public Integer lsbs_id;
	public Integer lsdbs_number;
	public String mcl_first;
	public Integer lscb_id;
	public Integer mspo_pay_period;
	public Integer mste_age;
	public Date mspo_next_bill;
	public Integer lssa_id;
	public String lku_symbol;
	public String no_trx;
	public String no_trx10;
	public String trx1;
	public String trx2;
	public String status_otorisasi;

	// w_entry_trans_stable_link > d_entry_trans_stable_link_det

	public String lji_invest;
	public String msl_desc;
	public Integer msl_no;
	public Date msl_trans_date;
	public Integer msl_tu_ke;
	public Double msl_premi;
	public String no_reg;
	public Double msl_rate;
	public Integer flag_rate;
	public Integer msl_mgi;
	public Double msl_bunga;
	public Integer msl_hari;
	public Date msl_bdate;
	public Date msl_edate;
	public Date msl_tgl_nab;
	public Integer msl_ro;
	public Double msl_nab;
	public Double msl_bp_rate;
	public Double msl_unit;
	public Integer tarik_bunga;
	public Date msl_input_date;
	public Integer posisi;
	
	//tambahan
	public Integer flag_new;
	public String lji_id;
	public Integer simpan_msl_no;
	public Integer simpan_msl_tu_ke;
	public String simpan_lji_id;
	public String simpan_mode;
	
	//tambahan untuk insert ke mst_slink
	public Integer msl_kode;                      
	public Integer msl_posisi;                    
	public Double msl_tax;                        
	public Double msl_saldo_unit;                 
	public Double msl_tarik_pertama;              
	public Date msl_tgl_nab_bp;                   
	public Double msl_nab_bp;                     
	public Double msl_nilai_polis;                
	public Double msl_bp;                         
	public Double msl_bp_pt;                      
	public Double msl_up;                         
	public Date msl_next_date;                    
	public Integer msl_bayar_bunga;               
	public Integer msl_bayar_bp;                  
	public Date msl_proses_date;                  
	public Date msl_confirm_date;                 
	public Date msl_prod_date;                    
	public Integer msl_tahun_ke;                  
	public Integer msl_premi_ke;                  
	public String msl_note;                       
	public Integer lus_id;                        
	public Integer msl_flag_print;                
	public Date msl_print_date;                   
	public Integer msl_aktif;                     
	public Integer msl_flag_up;                   
	public Integer msl_jn_rumus;                  
	public Integer msl_flag_comm;                 
	public Integer msl_print;                     
	public String no_reg_ref;                     
	public Integer msl_bayar_tarik;               
	public Date msl_rate_date;                    
	public Integer msl_new;                       
	public Double msl_surr_charge;                
	public Date msl_letter_date;                  
	public String mspin_no_pinjaman;              
	public Date msl_paid_date;                    
	public Integer msl_cash;     
	public String userotor;
	public String passotor;
	public String msen_endors_no;
	public Integer msl_flag_cb;
	public String valid_bank_1;
	public String valid_bank_2;
	
	
	public String getTrx1() {
		return trx1;
	}
	public void setTrx1(String trx1) {
		this.trx1 = trx1;
	}
	public String getTrx2() {
		return trx2;
	}
	public void setTrx2(String trx2) {
		this.trx2 = trx2;
	}
	public String getValid_bank_1() {
		return valid_bank_1;
	}
	public void setValid_bank_1(String valid_bank_1) {
		this.valid_bank_1 = valid_bank_1;
	}
	public String getValid_bank_2() {
		return valid_bank_2;
	}
	public void setValid_bank_2(String valid_bank_2) {
		this.valid_bank_2 = valid_bank_2;
	}
	public Integer getMsl_flag_cb() {
		return msl_flag_cb;
	}
	public void setMsl_flag_cb(Integer msl_flag_cb) {
		this.msl_flag_cb = msl_flag_cb;
	}
	public String getPassotor() {
		return passotor;
	}
	public void setPassotor(String passotor) {
		this.passotor = passotor;
	}
	public String getUserotor() {
		return userotor;
	}
	public void setUserotor(String userotor) {
		this.userotor = userotor;
	}
	public Integer getSimpan_msl_no() {
		return simpan_msl_no;
	}
	public void setSimpan_msl_no(Integer simpan_msl_no) {
		this.simpan_msl_no = simpan_msl_no;
	}
	public String getSimpan_mode() {
		return simpan_mode;
	}
	public void setSimpan_mode(String simpan_mode) {
		this.simpan_mode = simpan_mode;
	}
	public String getLji_id() {
		return lji_id;
	}
	public void setLji_id(String lji_id) {
		this.lji_id = lji_id;
	}
	public String getSimpan_lji_id() {
		return simpan_lji_id;
	}
	public void setSimpan_lji_id(String simpan_lji_id) {
		this.simpan_lji_id = simpan_lji_id;
	}
	public Integer getFlag_new() {
		return flag_new;
	}
	public Integer getMsl_kode() {
		return msl_kode;
	}
	public void setMsl_kode(Integer msl_kode) {
		this.msl_kode = msl_kode;
	}
	public Integer getMsl_posisi() {
		return msl_posisi;
	}
	public void setMsl_posisi(Integer msl_posisi) {
		this.msl_posisi = msl_posisi;
	}
	public Double getMsl_tax() {
		return msl_tax;
	}
	public void setMsl_tax(Double msl_tax) {
		this.msl_tax = msl_tax;
	}
	public Double getMsl_saldo_unit() {
		return msl_saldo_unit;
	}
	public void setMsl_saldo_unit(Double msl_saldo_unit) {
		this.msl_saldo_unit = msl_saldo_unit;
	}
	public Double getMsl_tarik_pertama() {
		return msl_tarik_pertama;
	}
	public void setMsl_tarik_pertama(Double msl_tarik_pertama) {
		this.msl_tarik_pertama = msl_tarik_pertama;
	}
	public Date getMsl_tgl_nab_bp() {
		return msl_tgl_nab_bp;
	}
	public void setMsl_tgl_nab_bp(Date msl_tgl_nab_bp) {
		this.msl_tgl_nab_bp = msl_tgl_nab_bp;
	}
	public Double getMsl_nab_bp() {
		return msl_nab_bp;
	}
	public void setMsl_nab_bp(Double msl_nab_bp) {
		this.msl_nab_bp = msl_nab_bp;
	}
	public Double getMsl_nilai_polis() {
		return msl_nilai_polis;
	}
	public void setMsl_nilai_polis(Double msl_nilai_polis) {
		this.msl_nilai_polis = msl_nilai_polis;
	}
	public Double getMsl_bp() {
		return msl_bp;
	}
	public void setMsl_bp(Double msl_bp) {
		this.msl_bp = msl_bp;
	}
	public Double getMsl_bp_pt() {
		return msl_bp_pt;
	}
	public void setMsl_bp_pt(Double msl_bp_pt) {
		this.msl_bp_pt = msl_bp_pt;
	}
	public Double getMsl_up() {
		return msl_up;
	}
	public void setMsl_up(Double msl_up) {
		this.msl_up = msl_up;
	}
	public Date getMsl_next_date() {
		return msl_next_date;
	}
	public void setMsl_next_date(Date msl_next_date) {
		this.msl_next_date = msl_next_date;
	}
	public Integer getMsl_bayar_bunga() {
		return msl_bayar_bunga;
	}
	public void setMsl_bayar_bunga(Integer msl_bayar_bunga) {
		this.msl_bayar_bunga = msl_bayar_bunga;
	}
	public Integer getMsl_bayar_bp() {
		return msl_bayar_bp;
	}
	public void setMsl_bayar_bp(Integer msl_bayar_bp) {
		this.msl_bayar_bp = msl_bayar_bp;
	}
	public Date getMsl_proses_date() {
		return msl_proses_date;
	}
	public void setMsl_proses_date(Date msl_proses_date) {
		this.msl_proses_date = msl_proses_date;
	}
	public Date getMsl_confirm_date() {
		return msl_confirm_date;
	}
	public void setMsl_confirm_date(Date msl_confirm_date) {
		this.msl_confirm_date = msl_confirm_date;
	}
	public Date getMsl_prod_date() {
		return msl_prod_date;
	}
	public void setMsl_prod_date(Date msl_prod_date) {
		this.msl_prod_date = msl_prod_date;
	}
	public Integer getMsl_tahun_ke() {
		return msl_tahun_ke;
	}
	public void setMsl_tahun_ke(Integer msl_tahun_ke) {
		this.msl_tahun_ke = msl_tahun_ke;
	}
	public Integer getMsl_premi_ke() {
		return msl_premi_ke;
	}
	public void setMsl_premi_ke(Integer msl_premi_ke) {
		this.msl_premi_ke = msl_premi_ke;
	}
	public String getMsl_note() {
		return msl_note;
	}
	public void setMsl_note(String msl_note) {
		this.msl_note = msl_note;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public Integer getMsl_flag_print() {
		return msl_flag_print;
	}
	public void setMsl_flag_print(Integer msl_flag_print) {
		this.msl_flag_print = msl_flag_print;
	}
	public Date getMsl_print_date() {
		return msl_print_date;
	}
	public void setMsl_print_date(Date msl_print_date) {
		this.msl_print_date = msl_print_date;
	}
	public Integer getMsl_aktif() {
		return msl_aktif;
	}
	public void setMsl_aktif(Integer msl_aktif) {
		this.msl_aktif = msl_aktif;
	}
	public Integer getMsl_flag_up() {
		return msl_flag_up;
	}
	public void setMsl_flag_up(Integer msl_flag_up) {
		this.msl_flag_up = msl_flag_up;
	}
	public Integer getMsl_jn_rumus() {
		return msl_jn_rumus;
	}
	public void setMsl_jn_rumus(Integer msl_jn_rumus) {
		this.msl_jn_rumus = msl_jn_rumus;
	}
	public Integer getMsl_flag_comm() {
		return msl_flag_comm;
	}
	public void setMsl_flag_comm(Integer msl_flag_comm) {
		this.msl_flag_comm = msl_flag_comm;
	}
	public Integer getMsl_print() {
		return msl_print;
	}
	public void setMsl_print(Integer msl_print) {
		this.msl_print = msl_print;
	}
	public String getNo_reg_ref() {
		return no_reg_ref;
	}
	public void setNo_reg_ref(String no_reg_ref) {
		this.no_reg_ref = no_reg_ref;
	}
	public Integer getMsl_bayar_tarik() {
		return msl_bayar_tarik;
	}
	public void setMsl_bayar_tarik(Integer msl_bayar_tarik) {
		this.msl_bayar_tarik = msl_bayar_tarik;
	}
	public Date getMsl_rate_date() {
		return msl_rate_date;
	}
	public void setMsl_rate_date(Date msl_rate_date) {
		this.msl_rate_date = msl_rate_date;
	}
	public Integer getMsl_new() {
		return msl_new;
	}
	public void setMsl_new(Integer msl_new) {
		this.msl_new = msl_new;
	}
	public Double getMsl_surr_charge() {
		return msl_surr_charge;
	}
	public void setMsl_surr_charge(Double msl_surr_charge) {
		this.msl_surr_charge = msl_surr_charge;
	}
	public Date getMsl_letter_date() {
		return msl_letter_date;
	}
	public void setMsl_letter_date(Date msl_letter_date) {
		this.msl_letter_date = msl_letter_date;
	}
	public String getMspin_no_pinjaman() {
		return mspin_no_pinjaman;
	}
	public void setMspin_no_pinjaman(String mspin_no_pinjaman) {
		this.mspin_no_pinjaman = mspin_no_pinjaman;
	}
	public Date getMsl_paid_date() {
		return msl_paid_date;
	}
	public void setMsl_paid_date(Date msl_paid_date) {
		this.msl_paid_date = msl_paid_date;
	}
	public Integer getMsl_cash() {
		return msl_cash;
	}
	public void setMsl_cash(Integer msl_cash) {
		this.msl_cash = msl_cash;
	}
	public void setFlag_new(Integer flag_new) {
		this.flag_new = flag_new;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
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
	public Double getMspr_tsi() {
		return mspr_tsi;
	}
	public void setMspr_tsi(Double mspr_tsi) {
		this.mspr_tsi = mspr_tsi;
	}
	public Double getMspr_premium() {
		return mspr_premium;
	}
	public void setMspr_premium(Double mspr_premium) {
		this.mspr_premium = mspr_premium;
	}
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}
	public Integer getLssp_id() {
		return lssp_id;
	}
	public void setLssp_id(Integer lssp_id) {
		this.lssp_id = lssp_id;
	}
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public Integer getLscb_id() {
		return lscb_id;
	}
	public void setLscb_id(Integer lscb_id) {
		this.lscb_id = lscb_id;
	}
	public Integer getMspo_pay_period() {
		return mspo_pay_period;
	}
	public void setMspo_pay_period(Integer mspo_pay_period) {
		this.mspo_pay_period = mspo_pay_period;
	}
	public Integer getMste_age() {
		return mste_age;
	}
	public void setMste_age(Integer mste_age) {
		this.mste_age = mste_age;
	}
	public Date getMspo_next_bill() {
		return mspo_next_bill;
	}
	public void setMspo_next_bill(Date mspo_next_bill) {
		this.mspo_next_bill = mspo_next_bill;
	}
	public Integer getLssa_id() {
		return lssa_id;
	}
	public void setLssa_id(Integer lssa_id) {
		this.lssa_id = lssa_id;
	}
	public String getLku_symbol() {
		return lku_symbol;
	}
	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
	public String getLji_invest() {
		return lji_invest;
	}
	public void setLji_invest(String lji_invest) {
		this.lji_invest = lji_invest;
	}
	public String getMsl_desc() {
		return msl_desc;
	}
	public void setMsl_desc(String msl_desc) {
		this.msl_desc = msl_desc;
	}
	public Integer getMsl_no() {
		return msl_no;
	}
	public void setMsl_no(Integer msl_no) {
		this.msl_no = msl_no;
	}
	public Date getMsl_trans_date() {
		return msl_trans_date;
	}
	public void setMsl_trans_date(Date msl_trans_date) {
		this.msl_trans_date = msl_trans_date;
	}
	public Integer getMsl_tu_ke() {
		return msl_tu_ke;
	}
	public void setMsl_tu_ke(Integer msl_tu_ke) {
		this.msl_tu_ke = msl_tu_ke;
	}
	public Double getMsl_premi() {
		return msl_premi;
	}
	public void setMsl_premi(Double msl_premi) {
		this.msl_premi = msl_premi;
	}
	public String getNo_reg() {
		return no_reg;
	}
	public void setNo_reg(String no_reg) {
		this.no_reg = no_reg;
	}
	public Double getMsl_rate() {
		return msl_rate;
	}
	public void setMsl_rate(Double msl_rate) {
		this.msl_rate = msl_rate;
	}
	public Integer getFlag_rate() {
		return flag_rate;
	}
	public void setFlag_rate(Integer flag_rate) {
		this.flag_rate = flag_rate;
	}
	public Integer getMsl_mgi() {
		return msl_mgi;
	}
	public void setMsl_mgi(Integer msl_mgi) {
		this.msl_mgi = msl_mgi;
	}
	public Double getMsl_bunga() {
		return msl_bunga;
	}
	public void setMsl_bunga(Double msl_bunga) {
		this.msl_bunga = msl_bunga;
	}
	public Integer getMsl_hari() {
		return msl_hari;
	}
	public void setMsl_hari(Integer msl_hari) {
		this.msl_hari = msl_hari;
	}
	public Date getMsl_bdate() {
		return msl_bdate;
	}
	public void setMsl_bdate(Date msl_bdate) {
		this.msl_bdate = msl_bdate;
	}
	public Date getMsl_edate() {
		return msl_edate;
	}
	public void setMsl_edate(Date msl_edate) {
		this.msl_edate = msl_edate;
	}
	public Date getMsl_tgl_nab() {
		return msl_tgl_nab;
	}
	public void setMsl_tgl_nab(Date msl_tgl_nab) {
		this.msl_tgl_nab = msl_tgl_nab;
	}
	public Integer getMsl_ro() {
		return msl_ro;
	}
	public void setMsl_ro(Integer msl_ro) {
		this.msl_ro = msl_ro;
	}
	public Double getMsl_nab() {
		return msl_nab;
	}
	public void setMsl_nab(Double msl_nab) {
		this.msl_nab = msl_nab;
	}
	public Double getMsl_bp_rate() {
		return msl_bp_rate;
	}
	public void setMsl_bp_rate(Double msl_bp_rate) {
		this.msl_bp_rate = msl_bp_rate;
	}
	public Double getMsl_unit() {
		return msl_unit;
	}
	public void setMsl_unit(Double msl_unit) {
		this.msl_unit = msl_unit;
	}
	public Integer getTarik_bunga() {
		return tarik_bunga;
	}
	public void setTarik_bunga(Integer tarik_bunga) {
		this.tarik_bunga = tarik_bunga;
	}
	public Date getMsl_input_date() {
		return msl_input_date;
	}
	public void setMsl_input_date(Date msl_input_date) {
		this.msl_input_date = msl_input_date;
	}
	public Integer getFlag_bulanan() {
		return flag_bulanan;
	}
	public void setFlag_bulanan(Integer flag_bulanan) {
		this.flag_bulanan = flag_bulanan;
	}
	public String getNo_trx() {
		return no_trx;
	}
	public void setNo_trx(String no_trx) {
		this.no_trx = no_trx;
		if(no_trx!=null){
			no_trx10=no_trx.replaceFirst("FT","");
		}
	}
	public Integer getMsl_proses_bsm() {
		return msl_proses_bsm;
	}
	public void setMsl_proses_bsm(Integer msl_proses_bsm) {
		this.msl_proses_bsm = msl_proses_bsm;
	}
	public Integer getFlag_special() {
		return flag_special;
	}
	public void setFlag_special(Integer flag_special) {
		this.flag_special = flag_special;
	}
	public String getMsen_endors_no() {
		return msen_endors_no;
	}
	public void setMsen_endors_no(String msen_endors_no) {
		this.msen_endors_no = msen_endors_no;
	}
	public Integer getSimpan_msl_tu_ke() {
		return simpan_msl_tu_ke;
	}
	public void setSimpan_msl_tu_ke(Integer simpan_msl_tu_ke) {
		this.simpan_msl_tu_ke = simpan_msl_tu_ke;
	}
	public String getStatus_otorisasi() {
		return status_otorisasi;
	}
	public void setStatus_otorisasi(String status_otorisasi) {
		this.status_otorisasi = status_otorisasi;
	}
	public Integer getPosisi() {
		return posisi;
	}
	public void setPosisi(Integer posisi) {
		this.posisi = posisi;
	}
	public String getNo_trx10() {
		return no_trx10;
	}
	public void setNo_trx10(String no_trx10) {
		this.no_trx10 = no_trx10;
		if(no_trx10!=null){
			no_trx="FT"+no_trx10;
		}
	}
	
}