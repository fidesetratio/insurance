package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * EKA.MST_SLINK_BAYAR
 * 
 * @author yusuf
 * @since Jul 28, 2009 (8:21:45 AM)
 */
public class SlinkBayar implements Serializable {

	private static final long serialVersionUID = 1L;

	public String reg_spaj;                       
	public Integer msl_no;                        
	public String lji_id;                         
	public String mslb_bayar_id;                  
	public String mslb_desc;                      
	public Integer mslb_tu_ke;                    
	public Integer lus_id;                        
	public String lku_id;                         
	public Date mslb_input_date;                  
	public Date mslb_paid_date;                   
	public Date mslb_due_date;                    
	public Date mslb_print_date;                  
	public Date mslb_beg_period;                  
	public Date mslb_end_period;                  
	public String mslb_rekening;                  
	public Double mslb_up;                        
	public Double mslb_premi;                     
	public Double mslb_pinalti;                   
	public Double mslb_rate;                      
	public Double mslb_bunga;                     
	public Double mslb_bp;                        
	public Double mslb_tarik;                     
	public Double mslb_jum_bayar;                 
	public Date mslb_tgl_nab;                     
	public Double mslb_nab;                       
	public Double mslb_unit;                      
	public Double mslb_tambah;                    
	public Integer mslb_hari;                     
	public String mslb_notes;                     
	public Integer mslb_flag_bayar;               
	public Integer mslb_filing;                   
	public Date mslb_filing_date;                 
	public Double mslb_bp_pt;                     
	public Integer lsbp_id;                       
	public String mrc_cabang;                     
	public String mrc_atas_nama;                  
	public String mrc_no_ac;                      
	public Double mslb_kurang;                    
	public Integer flag_bulanan;                  
	public Integer flag_proses;
	public Integer mslb_jml_hari;
	
	public Integer getMslb_jml_hari() {
		return mslb_jml_hari;
	}
	public void setMslb_jml_hari(Integer mslb_jml_hari) {
		this.mslb_jml_hari = mslb_jml_hari;
	}
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
	public String getLji_id() {
		return lji_id;
	}
	public void setLji_id(String lji_id) {
		this.lji_id = lji_id;
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
	public Integer getMsl_no() {
		return msl_no;
	}
	public void setMsl_no(Integer msl_no) {
		this.msl_no = msl_no;
	}
	public String getMslb_bayar_id() {
		return mslb_bayar_id;
	}
	public void setMslb_bayar_id(String mslb_bayar_id) {
		this.mslb_bayar_id = mslb_bayar_id;
	}
	public Date getMslb_beg_period() {
		return mslb_beg_period;
	}
	public void setMslb_beg_period(Date mslb_beg_period) {
		this.mslb_beg_period = mslb_beg_period;
	}
	public Double getMslb_bp() {
		return mslb_bp;
	}
	public void setMslb_bp(Double mslb_bp) {
		this.mslb_bp = mslb_bp;
	}
	public Double getMslb_bp_pt() {
		return mslb_bp_pt;
	}
	public void setMslb_bp_pt(Double mslb_bp_pt) {
		this.mslb_bp_pt = mslb_bp_pt;
	}
	public Double getMslb_bunga() {
		return mslb_bunga;
	}
	public void setMslb_bunga(Double mslb_bunga) {
		this.mslb_bunga = mslb_bunga;
	}
	public String getMslb_desc() {
		return mslb_desc;
	}
	public void setMslb_desc(String mslb_desc) {
		this.mslb_desc = mslb_desc;
	}
	public Date getMslb_due_date() {
		return mslb_due_date;
	}
	public void setMslb_due_date(Date mslb_due_date) {
		this.mslb_due_date = mslb_due_date;
	}
	public Date getMslb_end_period() {
		return mslb_end_period;
	}
	public void setMslb_end_period(Date mslb_end_period) {
		this.mslb_end_period = mslb_end_period;
	}
	public Integer getMslb_filing() {
		return mslb_filing;
	}
	public void setMslb_filing(Integer mslb_filing) {
		this.mslb_filing = mslb_filing;
	}
	public Date getMslb_filing_date() {
		return mslb_filing_date;
	}
	public void setMslb_filing_date(Date mslb_filing_date) {
		this.mslb_filing_date = mslb_filing_date;
	}
	public Integer getMslb_flag_bayar() {
		return mslb_flag_bayar;
	}
	public void setMslb_flag_bayar(Integer mslb_flag_bayar) {
		this.mslb_flag_bayar = mslb_flag_bayar;
	}
	public Integer getMslb_hari() {
		return mslb_hari;
	}
	public void setMslb_hari(Integer mslb_hari) {
		this.mslb_hari = mslb_hari;
	}
	public Date getMslb_input_date() {
		return mslb_input_date;
	}
	public void setMslb_input_date(Date mslb_input_date) {
		this.mslb_input_date = mslb_input_date;
	}
	public Double getMslb_jum_bayar() {
		return mslb_jum_bayar;
	}
	public void setMslb_jum_bayar(Double mslb_jum_bayar) {
		this.mslb_jum_bayar = mslb_jum_bayar;
	}
	public Double getMslb_kurang() {
		return mslb_kurang;
	}
	public void setMslb_kurang(Double mslb_kurang) {
		this.mslb_kurang = mslb_kurang;
	}
	public Double getMslb_nab() {
		return mslb_nab;
	}
	public void setMslb_nab(Double mslb_nab) {
		this.mslb_nab = mslb_nab;
	}
	public String getMslb_notes() {
		return mslb_notes;
	}
	public void setMslb_notes(String mslb_notes) {
		this.mslb_notes = mslb_notes;
	}
	public Date getMslb_paid_date() {
		return mslb_paid_date;
	}
	public void setMslb_paid_date(Date mslb_paid_date) {
		this.mslb_paid_date = mslb_paid_date;
	}
	public Double getMslb_pinalti() {
		return mslb_pinalti;
	}
	public void setMslb_pinalti(Double mslb_pinalti) {
		this.mslb_pinalti = mslb_pinalti;
	}
	public Double getMslb_premi() {
		return mslb_premi;
	}
	public void setMslb_premi(Double mslb_premi) {
		this.mslb_premi = mslb_premi;
	}
	public Date getMslb_print_date() {
		return mslb_print_date;
	}
	public void setMslb_print_date(Date mslb_print_date) {
		this.mslb_print_date = mslb_print_date;
	}
	public Double getMslb_rate() {
		return mslb_rate;
	}
	public void setMslb_rate(Double mslb_rate) {
		this.mslb_rate = mslb_rate;
	}
	public String getMslb_rekening() {
		return mslb_rekening;
	}
	public void setMslb_rekening(String mslb_rekening) {
		this.mslb_rekening = mslb_rekening;
	}
	public Double getMslb_tambah() {
		return mslb_tambah;
	}
	public void setMslb_tambah(Double mslb_tambah) {
		this.mslb_tambah = mslb_tambah;
	}
	public Double getMslb_tarik() {
		return mslb_tarik;
	}
	public void setMslb_tarik(Double mslb_tarik) {
		this.mslb_tarik = mslb_tarik;
	}
	public Date getMslb_tgl_nab() {
		return mslb_tgl_nab;
	}
	public void setMslb_tgl_nab(Date mslb_tgl_nab) {
		this.mslb_tgl_nab = mslb_tgl_nab;
	}
	public Integer getMslb_tu_ke() {
		return mslb_tu_ke;
	}
	public void setMslb_tu_ke(Integer mslb_tu_ke) {
		this.mslb_tu_ke = mslb_tu_ke;
	}
	public Double getMslb_unit() {
		return mslb_unit;
	}
	public void setMslb_unit(Double mslb_unit) {
		this.mslb_unit = mslb_unit;
	}
	public Double getMslb_up() {
		return mslb_up;
	}
	public void setMslb_up(Double mslb_up) {
		this.mslb_up = mslb_up;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	
}