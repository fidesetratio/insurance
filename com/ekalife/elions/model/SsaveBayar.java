package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * MST_SSAVE_BAYAR
 * 
 * @author yusuf
 * @since Aug 20, 2009 (3:01:32 PM)
 */
public class SsaveBayar implements Serializable {


	private static final long serialVersionUID = 3598352916574520580L;
	public String reg_spaj;
	public Integer mss_no;
	public String mssb_bayar_id;
	public Date mss_bdate;
	public Date mss_edate;
	public Date mssb_paid_date;
	public Date mssb_print_date;
	public String lku_id;
	public String mssb_rekening;
	public Double mssb_premi;
	public Double mssb_bunga;
	public Double mssb_rate;
	public Double mssb_tambah;
	public Integer mssb_hari;
	public Double mssb_kurang;
	public Double mssb_jum_bayar;
	public Integer mssb_jml_hari;
	public String mssb_notes;
	public Integer lsbp_id;
	public String mrc_cabang;
	public String mrc_atas_nama;
	public String mrc_no_ac;
	public Integer flag_bulanan;
	public Integer flag_proses;
	public Integer mssb_filing;
	public Date mssb_filing_date;
	public Date mssb_input_date;
	public Integer lus_id;

	public Integer getFlag_bulanan() {
		return flag_bulanan;
	}

	public void setFlag_bulanan(Integer flag_bulanan) {
		this.flag_bulanan = flag_bulanan;
	}

	public Integer getFlag_proses() {
		return flag_proses;
	}

	public void setFlag_proses(Integer flag_proses) {
		this.flag_proses = flag_proses;
	}

	public String getLku_id() {
		return lku_id;
	}

	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}

	public Integer getLsbp_id() {
		return lsbp_id;
	}

	public void setLsbp_id(Integer lsbp_id) {
		this.lsbp_id = lsbp_id;
	}

	public Integer getLus_id() {
		return lus_id;
	}

	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}

	public String getMrc_atas_nama() {
		return mrc_atas_nama;
	}

	public void setMrc_atas_nama(String mrc_atas_nama) {
		this.mrc_atas_nama = mrc_atas_nama;
	}

	public String getMrc_cabang() {
		return mrc_cabang;
	}

	public void setMrc_cabang(String mrc_cabang) {
		this.mrc_cabang = mrc_cabang;
	}

	public String getMrc_no_ac() {
		return mrc_no_ac;
	}

	public void setMrc_no_ac(String mrc_no_ac) {
		this.mrc_no_ac = mrc_no_ac;
	}

	public Date getMss_bdate() {
		return mss_bdate;
	}

	public void setMss_bdate(Date mss_bdate) {
		this.mss_bdate = mss_bdate;
	}

	public Date getMss_edate() {
		return mss_edate;
	}

	public void setMss_edate(Date mss_edate) {
		this.mss_edate = mss_edate;
	}

	public Integer getMss_no() {
		return mss_no;
	}

	public void setMss_no(Integer mss_no) {
		this.mss_no = mss_no;
	}

	public String getMssb_bayar_id() {
		return mssb_bayar_id;
	}

	public void setMssb_bayar_id(String mssb_bayar_id) {
		this.mssb_bayar_id = mssb_bayar_id;
	}

	public Double getMssb_bunga() {
		return mssb_bunga;
	}

	public void setMssb_bunga(Double mssb_bunga) {
		this.mssb_bunga = mssb_bunga;
	}

	public Integer getMssb_filing() {
		return mssb_filing;
	}

	public void setMssb_filing(Integer mssb_filing) {
		this.mssb_filing = mssb_filing;
	}

	public Date getMssb_filing_date() {
		return mssb_filing_date;
	}

	public void setMssb_filing_date(Date mssb_filing_date) {
		this.mssb_filing_date = mssb_filing_date;
	}

	public Integer getMssb_hari() {
		return mssb_hari;
	}

	public void setMssb_hari(Integer mssb_hari) {
		this.mssb_hari = mssb_hari;
	}

	public Date getMssb_input_date() {
		return mssb_input_date;
	}

	public void setMssb_input_date(Date mssb_input_date) {
		this.mssb_input_date = mssb_input_date;
	}

	public Integer getMssb_jml_hari() {
		return mssb_jml_hari;
	}

	public void setMssb_jml_hari(Integer mssb_jml_hari) {
		this.mssb_jml_hari = mssb_jml_hari;
	}

	public Double getMssb_jum_bayar() {
		return mssb_jum_bayar;
	}

	public void setMssb_jum_bayar(Double mssb_jum_bayar) {
		this.mssb_jum_bayar = mssb_jum_bayar;
	}

	public Double getMssb_kurang() {
		return mssb_kurang;
	}

	public void setMssb_kurang(Double mssb_kurang) {
		this.mssb_kurang = mssb_kurang;
	}

	public String getMssb_notes() {
		return mssb_notes;
	}

	public void setMssb_notes(String mssb_notes) {
		this.mssb_notes = mssb_notes;
	}

	public Date getMssb_paid_date() {
		return mssb_paid_date;
	}

	public void setMssb_paid_date(Date mssb_paid_date) {
		this.mssb_paid_date = mssb_paid_date;
	}

	public Double getMssb_premi() {
		return mssb_premi;
	}

	public void setMssb_premi(Double mssb_premi) {
		this.mssb_premi = mssb_premi;
	}

	public Date getMssb_print_date() {
		return mssb_print_date;
	}

	public void setMssb_print_date(Date mssb_print_date) {
		this.mssb_print_date = mssb_print_date;
	}

	public Double getMssb_rate() {
		return mssb_rate;
	}

	public void setMssb_rate(Double mssb_rate) {
		this.mssb_rate = mssb_rate;
	}

	public String getMssb_rekening() {
		return mssb_rekening;
	}

	public void setMssb_rekening(String mssb_rekening) {
		this.mssb_rekening = mssb_rekening;
	}

	public Double getMssb_tambah() {
		return mssb_tambah;
	}

	public void setMssb_tambah(Double mssb_tambah) {
		this.mssb_tambah = mssb_tambah;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

}
