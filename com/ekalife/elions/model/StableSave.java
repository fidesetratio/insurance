package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Stable Save (Premi Bulanan / Cicilan)
 * @author Yusuf
 *
 */
public class StableSave implements Serializable {
	private static final long serialVersionUID = -8566405722545557508L;
	public String reg_spaj;
	public Integer mss_kode;
	public Date mss_bdate;
	public Date mss_edate;
	public Date mss_ro_date;
	public Date mss_paid_date;
	public Integer mss_ro;
	public Integer mss_mgi;
	public Double mss_rate;
	public Double mss_premi;
	public Double mss_bunga;
	public Double mss_tax;
	public Double mss_surr_charge;
	public Integer mss_hari;
	public Integer mss_jn_rumus;
	public Integer mss_tahun_ke;
	public Integer mss_premi_ke;
	public String mss_note;
	public Integer mss_bulanan; //1 = premi bulanan
	public Integer mss_bayar_bunga;
	public Integer mss_flag_rate;
	public Date mss_rate_date;
	public Date mss_input_date;
	public Date mss_trans_date;
	public Date mss_confirm_date;
	public Date mss_prod_date;
	public Integer mss_print;
	public Date mss_print_date;
	public Integer mss_flag_comm;
	public Integer mss_cash;
	public String mspin_no_pinjaman;
	public Integer mss_aktif;
	public Integer lus_id;
	public Integer mss_rider_cb;
	public Double mss_rider_total;
	
	public Integer mss_no;                        
	public Integer mss_tu_ke;                     
	public Integer mss_flag_cb;                   
	public Integer mss_surat_pdf;                 
	public Integer mss_proses_bsm;                
	public Integer flag_bulanan; //1 = manfaat bulanan
	public String msen_endors_no;
	public Date mss_letter_date;
	public String mss_spaj_lama;
	
	public String getMss_spaj_lama() {
		return mss_spaj_lama;
	}
	public void setMss_spaj_lama(String mss_spaj_lama) {
		this.mss_spaj_lama = mss_spaj_lama;
	}
	public Date getMss_letter_date() {
		return mss_letter_date;
	}
	public void setMss_letter_date(Date mss_letter_date) {
		this.mss_letter_date = mss_letter_date;
	}
	public String getMsen_endors_no() {
		return msen_endors_no;
	}
	public void setMsen_endors_no(String msen_endors_no) {
		this.msen_endors_no = msen_endors_no;
	}
	public Integer getFlag_bulanan() {
		return flag_bulanan;
	}
	public void setFlag_bulanan(Integer flag_bulanan) {
		this.flag_bulanan = flag_bulanan;
	}
	public Integer getMss_flag_cb() {
		return mss_flag_cb;
	}
	public void setMss_flag_cb(Integer mss_flag_cb) {
		this.mss_flag_cb = mss_flag_cb;
	}
	public Integer getMss_no() {
		return mss_no;
	}
	public void setMss_no(Integer mss_no) {
		this.mss_no = mss_no;
	}
	public Integer getMss_proses_bsm() {
		return mss_proses_bsm;
	}
	public void setMss_proses_bsm(Integer mss_proses_bsm) {
		this.mss_proses_bsm = mss_proses_bsm;
	}
	public Integer getMss_surat_pdf() {
		return mss_surat_pdf;
	}
	public void setMss_surat_pdf(Integer mss_surat_pdf) {
		this.mss_surat_pdf = mss_surat_pdf;
	}
	public Integer getMss_tu_ke() {
		return mss_tu_ke;
	}
	public void setMss_tu_ke(Integer mss_tu_ke) {
		this.mss_tu_ke = mss_tu_ke;
	}
	public Integer getMss_rider_cb() {
		return mss_rider_cb;
	}
	public void setMss_rider_cb(Integer mss_rider_cb) {
		this.mss_rider_cb = mss_rider_cb;
	}
	public Double getMss_rider_total() {
		return mss_rider_total;
	}
	public void setMss_rider_total(Double mss_rider_total) {
		this.mss_rider_total = mss_rider_total;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getMspin_no_pinjaman() {
		return mspin_no_pinjaman;
	}
	public void setMspin_no_pinjaman(String mspin_no_pinjaman) {
		this.mspin_no_pinjaman = mspin_no_pinjaman;
	}
	public Integer getMss_aktif() {
		return mss_aktif;
	}
	public void setMss_aktif(Integer mss_aktif) {
		this.mss_aktif = mss_aktif;
	}
	public Integer getMss_bayar_bunga() {
		return mss_bayar_bunga;
	}
	public void setMss_bayar_bunga(Integer mss_bayar_bunga) {
		this.mss_bayar_bunga = mss_bayar_bunga;
	}
	public Date getMss_bdate() {
		return mss_bdate;
	}
	public void setMss_bdate(Date mss_bdate) {
		this.mss_bdate = mss_bdate;
	}
	public Integer getMss_bulanan() {
		return mss_bulanan;
	}
	public void setMss_bulanan(Integer mss_bulanan) {
		this.mss_bulanan = mss_bulanan;
	}
	public Double getMss_bunga() {
		return mss_bunga;
	}
	public void setMss_bunga(Double mss_bunga) {
		this.mss_bunga = mss_bunga;
	}
	public Integer getMss_cash() {
		return mss_cash;
	}
	public void setMss_cash(Integer mss_cash) {
		this.mss_cash = mss_cash;
	}
	public Date getMss_confirm_date() {
		return mss_confirm_date;
	}
	public void setMss_confirm_date(Date mss_confirm_date) {
		this.mss_confirm_date = mss_confirm_date;
	}
	public Date getMss_edate() {
		return mss_edate;
	}
	public void setMss_edate(Date mss_edate) {
		this.mss_edate = mss_edate;
	}
	public Integer getMss_flag_comm() {
		return mss_flag_comm;
	}
	public void setMss_flag_comm(Integer mss_flag_comm) {
		this.mss_flag_comm = mss_flag_comm;
	}
	public Integer getMss_flag_rate() {
		return mss_flag_rate;
	}
	public void setMss_flag_rate(Integer mss_flag_rate) {
		this.mss_flag_rate = mss_flag_rate;
	}
	public Integer getMss_hari() {
		return mss_hari;
	}
	public void setMss_hari(Integer mss_hari) {
		this.mss_hari = mss_hari;
	}
	public Date getMss_input_date() {
		return mss_input_date;
	}
	public void setMss_input_date(Date mss_input_date) {
		this.mss_input_date = mss_input_date;
	}
	public Integer getMss_jn_rumus() {
		return mss_jn_rumus;
	}
	public void setMss_jn_rumus(Integer mss_jn_rumus) {
		this.mss_jn_rumus = mss_jn_rumus;
	}
	public Integer getMss_kode() {
		return mss_kode;
	}
	public void setMss_kode(Integer mss_kode) {
		this.mss_kode = mss_kode;
	}
	public Integer getMss_mgi() {
		return mss_mgi;
	}
	public void setMss_mgi(Integer mss_mgi) {
		this.mss_mgi = mss_mgi;
	}
	public String getMss_note() {
		return mss_note;
	}
	public void setMss_note(String mss_note) {
		this.mss_note = mss_note;
	}
	public Date getMss_paid_date() {
		return mss_paid_date;
	}
	public void setMss_paid_date(Date mss_paid_date) {
		this.mss_paid_date = mss_paid_date;
	}
	public Double getMss_premi() {
		return mss_premi;
	}
	public void setMss_premi(Double mss_premi) {
		this.mss_premi = mss_premi;
	}
	public Integer getMss_premi_ke() {
		return mss_premi_ke;
	}
	public void setMss_premi_ke(Integer mss_premi_ke) {
		this.mss_premi_ke = mss_premi_ke;
	}
	public Integer getMss_print() {
		return mss_print;
	}
	public void setMss_print(Integer mss_print) {
		this.mss_print = mss_print;
	}
	public Date getMss_print_date() {
		return mss_print_date;
	}
	public void setMss_print_date(Date mss_print_date) {
		this.mss_print_date = mss_print_date;
	}
	public Date getMss_prod_date() {
		return mss_prod_date;
	}
	public void setMss_prod_date(Date mss_prod_date) {
		this.mss_prod_date = mss_prod_date;
	}
	public Double getMss_rate() {
		return mss_rate;
	}
	public void setMss_rate(Double mss_rate) {
		this.mss_rate = mss_rate;
	}
	public Date getMss_rate_date() {
		return mss_rate_date;
	}
	public void setMss_rate_date(Date mss_rate_date) {
		this.mss_rate_date = mss_rate_date;
	}
	public Integer getMss_ro() {
		return mss_ro;
	}
	public void setMss_ro(Integer mss_ro) {
		this.mss_ro = mss_ro;
	}
	public Date getMss_ro_date() {
		return mss_ro_date;
	}
	public void setMss_ro_date(Date mss_ro_date) {
		this.mss_ro_date = mss_ro_date;
	}
	public Double getMss_surr_charge() {
		return mss_surr_charge;
	}
	public void setMss_surr_charge(Double mss_surr_charge) {
		this.mss_surr_charge = mss_surr_charge;
	}
	public Integer getMss_tahun_ke() {
		return mss_tahun_ke;
	}
	public void setMss_tahun_ke(Integer mss_tahun_ke) {
		this.mss_tahun_ke = mss_tahun_ke;
	}
	public Double getMss_tax() {
		return mss_tax;
	}
	public void setMss_tax(Double mss_tax) {
		this.mss_tax = mss_tax;
	}
	public Date getMss_trans_date() {
		return mss_trans_date;
	}
	public void setMss_trans_date(Date mss_trans_date) {
		this.mss_trans_date = mss_trans_date;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

}