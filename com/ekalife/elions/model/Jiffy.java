package com.ekalife.elions.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Jiffy implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mns_kd_nasabah;                 
	private String mjf_perusahaan;                 
	private Integer mjf_lama_kerja;                
	private Integer mjf_lama_kerja_bln;            
	private String mjf_jn_pekerjaan;               
	private String mjf_alamat_ktr;                 
	private String mjf_kota;                       
	private String mjf_kd_pos_ktr;                 
	private Integer mjf_renc_karir;                
	private String mjf_ket_renc_karir;             
	private String mjf_renc_realisasi;             
	private String mjf_ket_pekerjaan;              
	private String mjf_hobby;                      
	private String mjf_alasan_hobby;               
	private String mjf_frek_hobby;                 
	private Integer mjf_partner_hobby;             
	private String mjf_ket_partner;                
	private String mjf_ket_hobby;                  
	private Integer mjf_menikah_yt;                
	private String mjf_menikah_ket;                
	private Integer mjf_anak_yt;                   
	private String mjf_anak_ket;                   
	private Integer mjf_cucu_yt;                   
	private String mjf_cucu_ket;                   
	private String mjf_ket_family;                 
	private String mjf_frek_ktm_teman;             
	private Integer mjf_ingin_sering_yt;           
	private String mjf_ingin_sering_ket;           
	private Integer mjf_real_pensiun_yt;           
	private String mjf_real_pensiun_ket;           
	private String mjf_ket_friend;                 
	private BigDecimal mjf_besar_biaya;               
	private BigDecimal mjf_kebutuhan;                 
	private BigDecimal mjf_biaya_pendidikan;          
	private BigDecimal mjf_biaya_pensiun;             
	private BigDecimal mjf_biaya_pleasure;            
	private String mjf_ket_you;                    
	private Integer mjf_sikap_resiko;              
	private BigDecimal mjf_budget;                    
	private Integer mjf_tt_nasabah;                
	private Date mjf_tgl_tt;                       
	private Integer mjf_fek_pert;                  
	private Integer mjf_ok_saran;                  
	private Integer mjf_beli_produk;               
	private Double mjf_up;                         
	private Integer mjf_frek_review;
	
	public String getMjf_alamat_ktr() {
		return mjf_alamat_ktr;
	}
	public void setMjf_alamat_ktr(String mjf_alamat_ktr) {
		this.mjf_alamat_ktr = mjf_alamat_ktr;
	}
	public String getMjf_alasan_hobby() {
		return mjf_alasan_hobby;
	}
	public void setMjf_alasan_hobby(String mjf_alasan_hobby) {
		this.mjf_alasan_hobby = mjf_alasan_hobby;
	}
	public String getMjf_anak_ket() {
		return mjf_anak_ket;
	}
	public void setMjf_anak_ket(String mjf_anak_ket) {
		this.mjf_anak_ket = mjf_anak_ket;
	}
	public Integer getMjf_anak_yt() {
		return mjf_anak_yt;
	}
	public void setMjf_anak_yt(Integer mjf_anak_yt) {
		this.mjf_anak_yt = mjf_anak_yt;
	}
	public Integer getMjf_beli_produk() {
		return mjf_beli_produk;
	}
	public void setMjf_beli_produk(Integer mjf_beli_produk) {
		this.mjf_beli_produk = mjf_beli_produk;
	}
	public BigDecimal getMjf_besar_biaya() {
		return mjf_besar_biaya;
	}
	public void setMjf_besar_biaya(BigDecimal mjf_besar_biaya) {
		this.mjf_besar_biaya = mjf_besar_biaya;
	}
	public BigDecimal getMjf_biaya_pendidikan() {
		return mjf_biaya_pendidikan;
	}
	public void setMjf_biaya_pendidikan(BigDecimal mjf_biaya_pendidikan) {
		this.mjf_biaya_pendidikan = mjf_biaya_pendidikan;
	}
	public BigDecimal getMjf_biaya_pensiun() {
		return mjf_biaya_pensiun;
	}
	public void setMjf_biaya_pensiun(BigDecimal mjf_biaya_pensiun) {
		this.mjf_biaya_pensiun = mjf_biaya_pensiun;
	}
	public BigDecimal getMjf_biaya_pleasure() {
		return mjf_biaya_pleasure;
	}
	public void setMjf_biaya_pleasure(BigDecimal mjf_biaya_pleasure) {
		this.mjf_biaya_pleasure = mjf_biaya_pleasure;
	}
	public BigDecimal getMjf_budget() {
		return mjf_budget;
	}
	public void setMjf_budget(BigDecimal mjf_budget) {
		this.mjf_budget = mjf_budget;
	}
	public String getMjf_cucu_ket() {
		return mjf_cucu_ket;
	}
	public void setMjf_cucu_ket(String mjf_cucu_ket) {
		this.mjf_cucu_ket = mjf_cucu_ket;
	}
	public Integer getMjf_cucu_yt() {
		return mjf_cucu_yt;
	}
	public void setMjf_cucu_yt(Integer mjf_cucu_yt) {
		this.mjf_cucu_yt = mjf_cucu_yt;
	}
	public Integer getMjf_fek_pert() {
		return mjf_fek_pert;
	}
	public void setMjf_fek_pert(Integer mjf_fek_pert) {
		this.mjf_fek_pert = mjf_fek_pert;
	}
	public String getMjf_frek_hobby() {
		return mjf_frek_hobby;
	}
	public void setMjf_frek_hobby(String mjf_frek_hobby) {
		this.mjf_frek_hobby = mjf_frek_hobby;
	}
	public String getMjf_frek_ktm_teman() {
		return mjf_frek_ktm_teman;
	}
	public void setMjf_frek_ktm_teman(String mjf_frek_ktm_teman) {
		this.mjf_frek_ktm_teman = mjf_frek_ktm_teman;
	}
	public Integer getMjf_frek_review() {
		return mjf_frek_review;
	}
	public void setMjf_frek_review(Integer mjf_frek_review) {
		this.mjf_frek_review = mjf_frek_review;
	}
	public String getMjf_hobby() {
		return mjf_hobby;
	}
	public void setMjf_hobby(String mjf_hobby) {
		this.mjf_hobby = mjf_hobby;
	}
	public String getMjf_ingin_sering_ket() {
		return mjf_ingin_sering_ket;
	}
	public void setMjf_ingin_sering_ket(String mjf_ingin_sering_ket) {
		this.mjf_ingin_sering_ket = mjf_ingin_sering_ket;
	}
	public Integer getMjf_ingin_sering_yt() {
		return mjf_ingin_sering_yt;
	}
	public void setMjf_ingin_sering_yt(Integer mjf_ingin_sering_yt) {
		this.mjf_ingin_sering_yt = mjf_ingin_sering_yt;
	}
	public String getMjf_jn_pekerjaan() {
		return mjf_jn_pekerjaan;
	}
	public void setMjf_jn_pekerjaan(String mjf_jn_pekerjaan) {
		this.mjf_jn_pekerjaan = mjf_jn_pekerjaan;
	}
	public String getMjf_kd_pos_ktr() {
		return mjf_kd_pos_ktr;
	}
	public void setMjf_kd_pos_ktr(String mjf_kd_pos_ktr) {
		this.mjf_kd_pos_ktr = mjf_kd_pos_ktr;
	}
	public BigDecimal getMjf_kebutuhan() {
		return mjf_kebutuhan;
	}
	public void setMjf_kebutuhan(BigDecimal mjf_kebutuhan) {
		this.mjf_kebutuhan = mjf_kebutuhan;
	}
	public String getMjf_ket_family() {
		return mjf_ket_family;
	}
	public void setMjf_ket_family(String mjf_ket_family) {
		this.mjf_ket_family = mjf_ket_family;
	}
	public String getMjf_ket_friend() {
		return mjf_ket_friend;
	}
	public void setMjf_ket_friend(String mjf_ket_friend) {
		this.mjf_ket_friend = mjf_ket_friend;
	}
	public String getMjf_ket_hobby() {
		return mjf_ket_hobby;
	}
	public void setMjf_ket_hobby(String mjf_ket_hobby) {
		this.mjf_ket_hobby = mjf_ket_hobby;
	}
	public String getMjf_ket_partner() {
		return mjf_ket_partner;
	}
	public void setMjf_ket_partner(String mjf_ket_partner) {
		this.mjf_ket_partner = mjf_ket_partner;
	}
	public String getMjf_ket_pekerjaan() {
		return mjf_ket_pekerjaan;
	}
	public void setMjf_ket_pekerjaan(String mjf_ket_pekerjaan) {
		this.mjf_ket_pekerjaan = mjf_ket_pekerjaan;
	}
	public String getMjf_ket_renc_karir() {
		return mjf_ket_renc_karir;
	}
	public void setMjf_ket_renc_karir(String mjf_ket_renc_karir) {
		this.mjf_ket_renc_karir = mjf_ket_renc_karir;
	}
	public String getMjf_ket_you() {
		return mjf_ket_you;
	}
	public void setMjf_ket_you(String mjf_ket_you) {
		this.mjf_ket_you = mjf_ket_you;
	}
	public String getMjf_kota() {
		return mjf_kota;
	}
	public void setMjf_kota(String mjf_kota) {
		this.mjf_kota = mjf_kota;
	}
	public Integer getMjf_lama_kerja() {
		return mjf_lama_kerja;
	}
	public void setMjf_lama_kerja(Integer mjf_lama_kerja) {
		this.mjf_lama_kerja = mjf_lama_kerja;
	}
	public Integer getMjf_lama_kerja_bln() {
		return mjf_lama_kerja_bln;
	}
	public void setMjf_lama_kerja_bln(Integer mjf_lama_kerja_bln) {
		this.mjf_lama_kerja_bln = mjf_lama_kerja_bln;
	}
	public String getMjf_menikah_ket() {
		return mjf_menikah_ket;
	}
	public void setMjf_menikah_ket(String mjf_menikah_ket) {
		this.mjf_menikah_ket = mjf_menikah_ket;
	}
	public Integer getMjf_menikah_yt() {
		return mjf_menikah_yt;
	}
	public void setMjf_menikah_yt(Integer mjf_menikah_yt) {
		this.mjf_menikah_yt = mjf_menikah_yt;
	}
	public Integer getMjf_ok_saran() {
		return mjf_ok_saran;
	}
	public void setMjf_ok_saran(Integer mjf_ok_saran) {
		this.mjf_ok_saran = mjf_ok_saran;
	}
	public Integer getMjf_partner_hobby() {
		return mjf_partner_hobby;
	}
	public void setMjf_partner_hobby(Integer mjf_partner_hobby) {
		this.mjf_partner_hobby = mjf_partner_hobby;
	}
	public String getMjf_perusahaan() {
		return mjf_perusahaan;
	}
	public void setMjf_perusahaan(String mjf_perusahaan) {
		this.mjf_perusahaan = mjf_perusahaan;
	}
	public String getMjf_real_pensiun_ket() {
		return mjf_real_pensiun_ket;
	}
	public void setMjf_real_pensiun_ket(String mjf_real_pensiun_ket) {
		this.mjf_real_pensiun_ket = mjf_real_pensiun_ket;
	}
	public Integer getMjf_real_pensiun_yt() {
		return mjf_real_pensiun_yt;
	}
	public void setMjf_real_pensiun_yt(Integer mjf_real_pensiun_yt) {
		this.mjf_real_pensiun_yt = mjf_real_pensiun_yt;
	}
	public Integer getMjf_renc_karir() {
		return mjf_renc_karir;
	}
	public void setMjf_renc_karir(Integer mjf_renc_karir) {
		this.mjf_renc_karir = mjf_renc_karir;
	}
	public String getMjf_renc_realisasi() {
		return mjf_renc_realisasi;
	}
	public void setMjf_renc_realisasi(String mjf_renc_realisasi) {
		this.mjf_renc_realisasi = mjf_renc_realisasi;
	}
	public Integer getMjf_sikap_resiko() {
		return mjf_sikap_resiko;
	}
	public void setMjf_sikap_resiko(Integer mjf_sikap_resiko) {
		this.mjf_sikap_resiko = mjf_sikap_resiko;
	}
	public Date getMjf_tgl_tt() {
		return mjf_tgl_tt;
	}
	public void setMjf_tgl_tt(Date mjf_tgl_tt) {
		this.mjf_tgl_tt = mjf_tgl_tt;
	}
	public Integer getMjf_tt_nasabah() {
		return mjf_tt_nasabah;
	}
	public void setMjf_tt_nasabah(Integer mjf_tt_nasabah) {
		this.mjf_tt_nasabah = mjf_tt_nasabah;
	}
	public Double getMjf_up() {
		return mjf_up;
	}
	public void setMjf_up(Double mjf_up) {
		this.mjf_up = mjf_up;
	}
	public String getMns_kd_nasabah() {
		return mns_kd_nasabah;
	}
	public void setMns_kd_nasabah(String mns_kd_nasabah) {
		this.mns_kd_nasabah = mns_kd_nasabah;
	}
	
	
}