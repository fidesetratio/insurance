package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ekalife.utils.Common;

public class InvestasiUtama implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;                       
	private Integer mu_ke;                         
	private Date mu_tgl_nab;                       
	private Date mu_tgl_surat;                     
	private Double mu_jlh_premi;                   
	private Integer mu_periodic_tu;                
	private Double mu_jlh_tu;                      
	private String no_register;                    
	private Integer lt_id;                         
	private Date mu_tgl_trans;                     
	private Double mu_jlh_unit;                    
	private Integer mu_bulan_surat;                
	private Integer mu_switch_cnt;                 
	private Date mu_last_switch;                   
	private Date mu_last_wdraw;                    
	private Integer mu_wdraw_cnt;                  
	private Integer mu_lspd_id;                    
	private Date mu_tgl_input;                     
	private Integer mu_aktif;                      
	private Integer mu_tahun_ke;                   
	private Integer mu_premi_ke;                   
	private Integer mu_flag_print;                 
	private Integer mu_lus_id;
	private ArrayList daftarinvestasi;
	private ArrayList daftarbiaya;
	private Double total_investasi;
	private Integer jmlh_invest;
	private Double total_premi;
	private Double jml_premium;
	private Integer jmlh_biaya;
	private Double total_biaya_premi;
	private Integer total_persen;
	private Double total_premi_sementara;
	private Integer jml_top_up ;
	private DetilTopUp daftartopup;
	private Integer edit_investasi;
	private String kriteria_kesalahan;

	public DetilTopUp getDaftartopup() {
		return daftartopup;
	}
	public void setDaftartopup(DetilTopUp daftartopup) {
		this.daftartopup = daftartopup;
	}
	public ArrayList getDaftarbiaya() {
		return daftarbiaya;
	}
	public void setDaftarbiaya(List daftarbiaya) {
		this.daftarbiaya = Common.serializableList(daftarbiaya);
	}
	public ArrayList getDaftarinvestasi() {
		return daftarinvestasi;
	}
	public void setDaftarinvestasi(List daftarinvestasi) {
		this.daftarinvestasi = Common.serializableList(daftarinvestasi);
	}
	public Double getJml_premium() {
		return jml_premium;
	}
	public void setJml_premium(Double jml_premium) {
		this.jml_premium = jml_premium;
	}
	public Integer getJml_top_up() {
		return jml_top_up;
	}
	public void setJml_top_up(Integer jml_top_up) {
		this.jml_top_up = jml_top_up;
	}
	public Integer getJmlh_biaya() {
		return jmlh_biaya;
	}
	public void setJmlh_biaya(Integer jmlh_biaya) {
		this.jmlh_biaya = jmlh_biaya;
	}
	public Integer getJmlh_invest() {
		return jmlh_invest;
	}
	public void setJmlh_invest(Integer jmlh_invest) {
		this.jmlh_invest = jmlh_invest;
	}
	public Integer getLt_id() {
		return lt_id;
	}
	public void setLt_id(Integer lt_id) {
		this.lt_id = lt_id;
	}
	public Integer getMu_aktif() {
		return mu_aktif;
	}
	public void setMu_aktif(Integer mu_aktif) {
		this.mu_aktif = mu_aktif;
	}
	public Integer getMu_bulan_surat() {
		return mu_bulan_surat;
	}
	public void setMu_bulan_surat(Integer mu_bulan_surat) {
		this.mu_bulan_surat = mu_bulan_surat;
	}
	public Integer getMu_flag_print() {
		return mu_flag_print;
	}
	public void setMu_flag_print(Integer mu_flag_print) {
		this.mu_flag_print = mu_flag_print;
	}
	public Double getMu_jlh_premi() {
		return mu_jlh_premi;
	}
	public void setMu_jlh_premi(Double mu_jlh_premi) {
		this.mu_jlh_premi = mu_jlh_premi;
	}
	public Double getMu_jlh_tu() {
		return mu_jlh_tu;
	}
	public void setMu_jlh_tu(Double mu_jlh_tu) {
		this.mu_jlh_tu = mu_jlh_tu;
	}
	public Double getMu_jlh_unit() {
		return mu_jlh_unit;
	}
	public void setMu_jlh_unit(Double mu_jlh_unit) {
		this.mu_jlh_unit = mu_jlh_unit;
	}
	public Integer getMu_ke() {
		return mu_ke;
	}
	public void setMu_ke(Integer mu_ke) {
		this.mu_ke = mu_ke;
	}
	public Date getMu_last_switch() {
		return mu_last_switch;
	}
	public void setMu_last_switch(Date mu_last_switch) {
		this.mu_last_switch = mu_last_switch;
	}
	public Date getMu_last_wdraw() {
		return mu_last_wdraw;
	}
	public void setMu_last_wdraw(Date mu_last_wdraw) {
		this.mu_last_wdraw = mu_last_wdraw;
	}
	public Integer getMu_lspd_id() {
		return mu_lspd_id;
	}
	public void setMu_lspd_id(Integer mu_lspd_id) {
		this.mu_lspd_id = mu_lspd_id;
	}
	public Integer getMu_lus_id() {
		return mu_lus_id;
	}
	public void setMu_lus_id(Integer mu_lus_id) {
		this.mu_lus_id = mu_lus_id;
	}
	public Integer getMu_periodic_tu() {
		return mu_periodic_tu;
	}
	public void setMu_periodic_tu(Integer mu_periodic_tu) {
		this.mu_periodic_tu = mu_periodic_tu;
	}
	public Integer getMu_premi_ke() {
		return mu_premi_ke;
	}
	public void setMu_premi_ke(Integer mu_premi_ke) {
		this.mu_premi_ke = mu_premi_ke;
	}
	public Integer getMu_switch_cnt() {
		return mu_switch_cnt;
	}
	public void setMu_switch_cnt(Integer mu_switch_cnt) {
		this.mu_switch_cnt = mu_switch_cnt;
	}
	public Integer getMu_tahun_ke() {
		return mu_tahun_ke;
	}
	public void setMu_tahun_ke(Integer mu_tahun_ke) {
		this.mu_tahun_ke = mu_tahun_ke;
	}
	public Date getMu_tgl_input() {
		return mu_tgl_input;
	}
	public void setMu_tgl_input(Date mu_tgl_input) {
		this.mu_tgl_input = mu_tgl_input;
	}
	public Date getMu_tgl_nab() {
		return mu_tgl_nab;
	}
	public void setMu_tgl_nab(Date mu_tgl_nab) {
		this.mu_tgl_nab = mu_tgl_nab;
	}
	public Date getMu_tgl_surat() {
		return mu_tgl_surat;
	}
	public void setMu_tgl_surat(Date mu_tgl_surat) {
		this.mu_tgl_surat = mu_tgl_surat;
	}
	public Date getMu_tgl_trans() {
		return mu_tgl_trans;
	}
	public void setMu_tgl_trans(Date mu_tgl_trans) {
		this.mu_tgl_trans = mu_tgl_trans;
	}
	public Integer getMu_wdraw_cnt() {
		return mu_wdraw_cnt;
	}
	public void setMu_wdraw_cnt(Integer mu_wdraw_cnt) {
		this.mu_wdraw_cnt = mu_wdraw_cnt;
	}
	public String getNo_register() {
		return no_register;
	}
	public void setNo_register(String no_register) {
		this.no_register = no_register;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Double getTotal_biaya_premi() {
		return total_biaya_premi;
	}
	public void setTotal_biaya_premi(Double total_biaya_premi) {
		this.total_biaya_premi = total_biaya_premi;
	}
	public Double getTotal_investasi() {
		return total_investasi;
	}
	public void setTotal_investasi(Double total_investasi) {
		this.total_investasi = total_investasi;
	}
	public Integer getTotal_persen() {
		return total_persen;
	}
	public void setTotal_persen(Integer total_persen) {
		this.total_persen = total_persen;
	}
	public Double getTotal_premi() {
		return total_premi;
	}
	public void setTotal_premi(Double total_premi) {
		this.total_premi = total_premi;
	}
	public Double getTotal_premi_sementara() {
		return total_premi_sementara;
	}
	public void setTotal_premi_sementara(Double total_premi_sementara) {
		this.total_premi_sementara = total_premi_sementara;
	}
	public Integer getEdit_investasi() {
		return edit_investasi;
	}
	public void setEdit_investasi(Integer edit_investasi) {
		this.edit_investasi = edit_investasi;
	}
	public String getKriteria_kesalahan() {
		return kriteria_kesalahan;
	}
	public void setKriteria_kesalahan(String kriteria_kesalahan) {
		this.kriteria_kesalahan = kriteria_kesalahan;
	}

}
