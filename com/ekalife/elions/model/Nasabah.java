/**
 * @author  : Ferry Harlim
 * @created : Dec 22, 2006 
 */
package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Nasabah implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String kd_rm;                          
	private String mns_nama_fa;                    
	private String kd_region;                      
	private String kd_koord;                       
	private String kd_area;                        
	private String kd_cabang;                      
	private String referrer_id;                    
	private String referrer_fa;                    
	private String kode;                           
	private String mns_reff_cab;                   
	private String mns_rek_bii;                    
	private Integer mns_usia;                      
	private Date mns_tglinput;                     
	private Integer mns_tt_nasabah;                
	private Date mns_tgl_tt;                       
	private Integer lsbs_id;                       
	private Integer mns_ok_saran;                  
	private Double mns_up;                         
	private Integer mns_frek_review;               
	private Integer lspd_id;                       
	private String mns_kd_nasabah;                 
	private String mns_no_ref;                     
	private String mns_nama;                       
	private Integer mns_sex;                       
	private String mns_dcif;                       
	private Integer mns_tipe_nasabah;              
	private String mns_area_kantor;                
	private String mns_tlpkantor;                  
	private String mns_area_rumah;                 
	private String mns_tlprumah;                   
	private String mns_area_hp;                    
	private String mns_hp;                         
	private Date mns_tgl_app;                      
	private String s_mns_tgl_app;
	private String mns_waktu_hub;                  
	private Integer ljl_jenis;                     
	private Integer mns_layanan;                   
	private Integer mns_st_jelas;                  
	private String mns_nama_lead;                  
	private String msag_id;                        
	private String kd_asm;                         
	private String kd_sm;                          
	private String kd_leader;
	private String nama_bfa;
	private String cabang_detail;
	private String bfa_detail;
	private String namaLead;
	private Integer flag;
	private Integer platinum;
	private String leader_ref1;
	private String leader_ref2;
	private String leader_ref3;
	private Integer jn_lead;
	private String jn_nasabah;
	private String nm_jenis;
	private String lus_id;
	
	public String getLus_id() {
		return lus_id;
	}
	public void setLus_id(String lus_id) {
		this.lus_id = lus_id;
	}
	public String getNm_jenis() {
		return nm_jenis;
	}
	public void setNm_jenis(String nm_jenis) {
		this.nm_jenis = nm_jenis;
	}
	public String getJn_nasabah() {
		return jn_nasabah;
	}
	public void setJn_nasabah(String jn_nasabah) {
		this.jn_nasabah = jn_nasabah;
	}
	public String getLeader_ref1() {
		return leader_ref1;
	}
	public void setLeader_ref1(String leader_ref1) {
		this.leader_ref1 = leader_ref1;
	}
	public String getLeader_ref2() {
		return leader_ref2;
	}
	public void setLeader_ref2(String leader_ref2) {
		this.leader_ref2 = leader_ref2;
	}
	public String getLeader_ref3() {
		return leader_ref3;
	}
	public void setLeader_ref3(String leader_ref3) {
		this.leader_ref3 = leader_ref3;
	}
	public Integer getPlatinum() {
		return platinum;
	}
	public void setPlatinum(Integer platinum) {
		this.platinum = platinum;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getNamaLead() {
		return namaLead;
	}
	public void setNamaLead(String namaLead) {
		this.namaLead = namaLead;
	}
	public String getBfa_detail() {
		return bfa_detail;
	}
	public void setBfa_detail(String bfa_detail) {
		this.bfa_detail = bfa_detail;
	}
	public String getCabang_detail() {
		return cabang_detail;
	}
	public void setCabang_detail(String cabang_detail) {
		this.cabang_detail = cabang_detail;
	}
	public String getKd_area() {
		return kd_area;
	}
	public void setKd_area(String kd_area) {
		this.kd_area = kd_area;
	}
	public String getKd_asm() {
		return kd_asm;
	}
	public void setKd_asm(String kd_asm) {
		this.kd_asm = kd_asm;
	}
	public String getKd_cabang() {
		return kd_cabang;
	}
	public void setKd_cabang(String kd_cabang) {
		this.kd_cabang = kd_cabang;
	}
	public String getKd_koord() {
		return kd_koord;
	}
	public void setKd_koord(String kd_koord) {
		this.kd_koord = kd_koord;
	}
	public String getKd_leader() {
		return kd_leader;
	}
	public void setKd_leader(String kd_leader) {
		this.kd_leader = kd_leader;
	}
	public String getKd_region() {
		return kd_region;
	}
	public void setKd_region(String kd_region) {
		this.kd_region = kd_region;
	}
	public String getKd_rm() {
		return kd_rm;
	}
	public void setKd_rm(String kd_rm) {
		this.kd_rm = kd_rm;	
	}
	public String getKd_sm() {
		return kd_sm;
	}
	public void setKd_sm(String kd_sm) {
		this.kd_sm = kd_sm;
	}
	public String getKode() {
		return kode;
	}
	public void setKode(String kode) {
		this.kode = kode;
	}
	public Integer getLjl_jenis() {
		return ljl_jenis;
	}
	public void setLjl_jenis(Integer ljl_jenis) {
		this.ljl_jenis = ljl_jenis;
	}
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public Integer getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}
	public String getMns_area_hp() {
		return mns_area_hp;
	}
	public void setMns_area_hp(String mns_area_hp) {
		this.mns_area_hp = mns_area_hp;
	}
	public String getMns_area_kantor() {
		return mns_area_kantor;
	}
	public void setMns_area_kantor(String mns_area_kantor) {
		this.mns_area_kantor = mns_area_kantor;
	}
	public String getMns_area_rumah() {
		return mns_area_rumah;
	}
	public void setMns_area_rumah(String mns_area_rumah) {
		this.mns_area_rumah = mns_area_rumah;
	}
	public String getMns_dcif() {
		return mns_dcif;
	}
	public void setMns_dcif(String mns_dcif) {
		this.mns_dcif = mns_dcif;
	}
	public Integer getMns_frek_review() {
		return mns_frek_review;
	}
	public void setMns_frek_review(Integer mns_frek_review) {
		this.mns_frek_review = mns_frek_review;
	}
	public String getMns_hp() {
		return mns_hp;
	}
	public void setMns_hp(String mns_hp) {
		this.mns_hp = mns_hp;
	}
	public String getMns_kd_nasabah() {
		return mns_kd_nasabah;
	}
	public void setMns_kd_nasabah(String mns_kd_nasabah) {
		this.mns_kd_nasabah = mns_kd_nasabah;
	}
	public Integer getMns_layanan() {
		return mns_layanan;
	}
	public void setMns_layanan(Integer mns_layanan) {
		this.mns_layanan = mns_layanan;
	}
	public String getMns_nama() {
		return mns_nama;
	}
	public void setMns_nama(String mns_nama) {
		this.mns_nama = mns_nama;
	}
	public String getMns_nama_fa() {
		return mns_nama_fa;
	}
	public void setMns_nama_fa(String mns_nama_fa) {
		this.mns_nama_fa = mns_nama_fa;
	}
	public String getMns_nama_lead() {
		return mns_nama_lead;
	}
	public void setMns_nama_lead(String mns_nama_lead) {
		this.mns_nama_lead = mns_nama_lead;
	}
	public String getMns_no_ref() {
		return mns_no_ref;
	}
	public void setMns_no_ref(String mns_no_ref) {
		this.mns_no_ref = mns_no_ref;
	}
	public Integer getMns_ok_saran() {
		return mns_ok_saran;
	}
	public void setMns_ok_saran(Integer mns_ok_saran) {
		this.mns_ok_saran = mns_ok_saran;
	}
	public String getMns_reff_cab() {
		return mns_reff_cab;
	}
	public void setMns_reff_cab(String mns_reff_cab) {
		this.mns_reff_cab = mns_reff_cab;
	}
	public String getMns_rek_bii() {
		return mns_rek_bii;
	}
	public void setMns_rek_bii(String mns_rek_bii) {
		this.mns_rek_bii = mns_rek_bii;
	}
	public Integer getMns_sex() {
		return mns_sex;
	}
	public void setMns_sex(Integer mns_sex) {
		this.mns_sex = mns_sex;
	}
	public Integer getMns_st_jelas() {
		return mns_st_jelas;
	}
	public void setMns_st_jelas(Integer mns_st_jelas) {
		this.mns_st_jelas = mns_st_jelas;
	}
	public Date getMns_tgl_app() {
		return mns_tgl_app;
	}
	public void setMns_tgl_app(Date mns_tgl_app) {
		this.mns_tgl_app = mns_tgl_app;
	}
	public Date getMns_tgl_tt() {
		return mns_tgl_tt;
	}
	public void setMns_tgl_tt(Date mns_tgl_tt) {
		this.mns_tgl_tt = mns_tgl_tt;
	}
	public Date getMns_tglinput() {
		return mns_tglinput;
	}
	public void setMns_tglinput(Date mns_tglinput) {
		this.mns_tglinput = mns_tglinput;
	}
	public Integer getMns_tipe_nasabah() {
		return mns_tipe_nasabah;
	}
	public void setMns_tipe_nasabah(Integer mns_tipe_nasabah) {
		this.mns_tipe_nasabah = mns_tipe_nasabah;
	}
	public String getMns_tlpkantor() {
		return mns_tlpkantor;
	}
	public void setMns_tlpkantor(String mns_tlpkantor) {
		this.mns_tlpkantor = mns_tlpkantor;
	}
	public String getMns_tlprumah() {
		return mns_tlprumah;
	}
	public void setMns_tlprumah(String mns_tlprumah) {
		this.mns_tlprumah = mns_tlprumah;
	}
	public Integer getMns_tt_nasabah() {
		return mns_tt_nasabah;
	}
	public void setMns_tt_nasabah(Integer mns_tt_nasabah) {
		this.mns_tt_nasabah = mns_tt_nasabah;
	}
	public Double getMns_up() {
		return mns_up;
	}
	public void setMns_up(Double mns_up) {
		this.mns_up = mns_up;
	}
	public Integer getMns_usia() {
		return mns_usia;
	}
	public void setMns_usia(Integer mns_usia) {
		this.mns_usia = mns_usia;
	}
	public String getMns_waktu_hub() {
		return mns_waktu_hub;
	}
	public void setMns_waktu_hub(String mns_waktu_hub) {
		this.mns_waktu_hub = mns_waktu_hub;
	}
	public String getMsag_id() {
		return msag_id;
	}
	public void setMsag_id(String msag_id) {
		this.msag_id = msag_id;
	}
	public String getReferrer_fa() {
		return referrer_fa;
	}
	public void setReferrer_fa(String referrer_fa) {
		this.referrer_fa = referrer_fa;
	}
	public String getReferrer_id() {
		return referrer_id;
	}
	public void setReferrer_id(String referrer_id) {
		this.referrer_id = referrer_id;
	}
	public String getNama_bfa() {
		return nama_bfa;
	}
	public void setNama_bfa(String nama_bfa) {
		this.nama_bfa = nama_bfa;
	}
	public String getS_mns_tgl_app() {
		return s_mns_tgl_app;
	}
	public void setS_mns_tgl_app(String s_mns_tgl_app) {
		this.s_mns_tgl_app = s_mns_tgl_app;
	}
	public Integer getJn_lead() {
		return jn_lead;
	}
	public void setJn_lead(Integer jn_lead) {
		this.jn_lead = jn_lead;
	}
 
}
