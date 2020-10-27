package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ekalife.utils.Common;

public class Pemegang2 implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer lti_id;
	private String reg_spaj;
	private String mcl_first;
	private String mcl_id;
	private String mkl_kerja;
	private String mkl_tujuan;
	private String mkl_penghasilan;
	private String mkl_pendanaan;
	private String mkl_industri;
	private String mspe_no_identity;
	private Integer lside_id;
	private String mspe_place_birth;
	private Date mspe_date_birth;
	private Integer mspe_sex;
	private String mspe_sex2;
	private Integer mspe_sts_mrt;
	private Integer mspo_umur_beasiswa;
	private String lgj_note;
	private Integer mspe_lama_kerja;
	private String lside_name;
	private String lsne_note;
	private String mcl_gelar;
	private String mpn_job_desc;
	private String mspe_mother;
	private String alamat_rumah;
	private String kd_pos_rumah;
	private String kota_rumah;
	private String area_code_rumah;
	private String telpon_rumah;
	private String alamat_kantor;
	private String kd_pos_kantor;
	private String kota_kantor;
	private String no_hp;
	private String no_hp2;
	private String email;
	private String area_code_kantor;
	private String telpon_kantor;
	private Integer mspo_umur_bieasiswa;
	private Integer mste_insured_no;
	private Integer mste_standard;
	private Integer mste_age;
	private String lsag_name;
	private String lsed_name;
	private String ljb_note;
	private Integer lsre_id;
	private String lsre_relation;
	private Integer msag_id;
	private String lca_id;
	private String lwk_id;
	private String lsrg_id;
	private Integer lscb_id;
	private String mspo_policy_no;
	private String mspo_policy_holder;
	private Integer mspo_age;
	private String mspo_no_blanko;
	private ArrayList<Keluarga>listKeluarga;
	private String area_code_rumah2;
	private String telpon_rumah2;
	private String area_code_kantor2;
	private String telpon_kantor2;
	private Integer mspo_pay_period;
	
	private String id;
	private Date mcl_tgl_input;
	private String kerjaa;                         
	private String kerjab;                         
	private String tujuana;                        
	private String danaa; 
	private String danaa2;
	private String industria;                      
	private String lgj_id;                         
	private Integer lsne_id;                       
	private String nama_kota;
	private String kota_rumaha;                    
	private String kota_kantora;                   
	private Integer lsag_id;                       
	private Integer lsed_id;                       
	private String ljb_id;                         
	private Date mste_tgl_recur;
	private Double mspo_under_table;
	private Date tgl_mspo_under_table;
	private Integer usiapp;
	private Double bonus_tahapan;
	private String tgl_pp;
	private Integer mspo_pribadi;
	private Integer mspo_follow_up;
	private String nama_ao;
	private String mspo_ao;
	private Integer mspo_ref_bii;
	private Date mspo_spaj_date;
	private String kode_cbg;
	private Integer mspo_komisi_bii;
	private Integer lus_id;
	private String cbg_lus_id;
	private String status;
	private Integer indeks_halaman;
	private String no_fax;
	private String area_code_fax;
	private String mspo_customer;
	private String mspo_customer_nama;
	private Integer dewasa;
	private Integer mcl_jenis;
	private Integer mcl_blacklist;
	private Integer mste_dewasa;
	private Integer lssp_id;
	private String nik;
	private Double mste_pct_dplk;
	private String mcl_data_salah;
	private String mspe_email;  
	private Integer mspo_jenis_terbit;
	private Integer jumlah_cancel;
	private Integer mste_flag_guthrie;
	private Integer cek_blanko; 
	private Integer mste_spaj_asli;
	private String nama_si;
	private Integer hubungan_si;
	private Date tgllhr_si;
	private String nama_anak1;
	private Integer hubungan_anak1;
	private Date tgllhr_anak1;
	private String nama_anak2;
	private Integer hubungan_anak2;
	private Date tgllhr_anak2;
	private String nama_anak3;
	private Integer hubungan_anak3;
	private Date tgllhr_anak3;
	private String lca_nama;
	private String mkl_smbr_penghasilan;
	private String mcl_agama;
	
	public String getMcl_agama() {
		return mcl_agama;
	}
	public void setMcl_agama(String mcl_agama) {
		this.mcl_agama = mcl_agama;
	}
	public String getMkl_smbr_penghasilan() {
		return mkl_smbr_penghasilan;
	}
	public void setMkl_smbr_penghasilan(String mkl_smbr_penghasilan) {
		this.mkl_smbr_penghasilan = mkl_smbr_penghasilan;
	}
	public String getAlamat_kantor() {
		return alamat_kantor;
	}
	public void setAlamat_kantor(String alamat_kantor) {
		this.alamat_kantor = alamat_kantor;
	}
	public String getAlamat_rumah() {
		return alamat_rumah;
	}
	public void setAlamat_rumah(String alamat_rumah) {
		this.alamat_rumah = alamat_rumah;
	}
	public String getArea_code_fax() {
		return area_code_fax;
	}
	public void setArea_code_fax(String area_code_fax) {
		this.area_code_fax = area_code_fax;
	}
	public String getArea_code_kantor() {
		return area_code_kantor;
	}
	public void setArea_code_kantor(String area_code_kantor) {
		this.area_code_kantor = area_code_kantor;
	}
	public String getArea_code_kantor2() {
		return area_code_kantor2;
	}
	public void setArea_code_kantor2(String area_code_kantor2) {
		this.area_code_kantor2 = area_code_kantor2;
	}
	public String getArea_code_rumah() {
		return area_code_rumah;
	}
	public void setArea_code_rumah(String area_code_rumah) {
		this.area_code_rumah = area_code_rumah;
	}
	public Integer getLti_id() {
		return lti_id;
	}
	public void setLti_id(Integer lti_id) {
		this.lti_id = lti_id;
	}	
	public String getArea_code_rumah2() {
		return area_code_rumah2;
	}
	public void setArea_code_rumah2(String area_code_rumah2) {
		this.area_code_rumah2 = area_code_rumah2;
	}
	public Double getBonus_tahapan() {
		return bonus_tahapan;
	}
	public void setBonus_tahapan(Double bonus_tahapan) {
		this.bonus_tahapan = bonus_tahapan;
	}
	public String getCbg_lus_id() {
		return cbg_lus_id;
	}
	public void setCbg_lus_id(String cbg_lus_id) {
		this.cbg_lus_id = cbg_lus_id;
	}
	public Integer getCek_blanko() {
		return cek_blanko;
	}
	public void setCek_blanko(Integer cek_blanko) {
		this.cek_blanko = cek_blanko;
	}
	public String getDanaa() {
		return danaa;
	}
	public void setDanaa(String danaa) {
		this.danaa = danaa;
	}
	public Integer getDewasa() {
		return dewasa;
	}
	public void setDewasa(Integer dewasa) {
		this.dewasa = dewasa;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getHubungan_anak1() {
		return hubungan_anak1;
	}
	public void setHubungan_anak1(Integer hubungan_anak1) {
		this.hubungan_anak1 = hubungan_anak1;
	}
	public Integer getHubungan_anak2() {
		return hubungan_anak2;
	}
	public void setHubungan_anak2(Integer hubungan_anak2) {
		this.hubungan_anak2 = hubungan_anak2;
	}
	public Integer getHubungan_anak3() {
		return hubungan_anak3;
	}
	public void setHubungan_anak3(Integer hubungan_anak3) {
		this.hubungan_anak3 = hubungan_anak3;
	}
	public Integer getHubungan_si() {
		return hubungan_si;
	}
	public void setHubungan_si(Integer hubungan_si) {
		this.hubungan_si = hubungan_si;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getIndeks_halaman() {
		return indeks_halaman;
	}
	public void setIndeks_halaman(Integer indeks_halaman) {
		this.indeks_halaman = indeks_halaman;
	}
	public String getIndustria() {
		return industria;
	}
	public void setIndustria(String industria) {
		this.industria = industria;
	}
	public Integer getJumlah_cancel() {
		return jumlah_cancel;
	}
	public void setJumlah_cancel(Integer jumlah_cancel) {
		this.jumlah_cancel = jumlah_cancel;
	}
	public String getKd_pos_kantor() {
		return kd_pos_kantor;
	}
	public void setKd_pos_kantor(String kd_pos_kantor) {
		this.kd_pos_kantor = kd_pos_kantor;
	}
	public String getKd_pos_rumah() {
		return kd_pos_rumah;
	}
	public void setKd_pos_rumah(String kd_pos_rumah) {
		this.kd_pos_rumah = kd_pos_rumah;
	}
	public String getKerjaa() {
		return kerjaa;
	}
	public void setKerjaa(String kerjaa) {
		this.kerjaa = kerjaa;
	}
	public String getKerjab() {
		return kerjab;
	}
	public void setKerjab(String kerjab) {
		this.kerjab = kerjab;
	}
	public String getKode_cbg() {
		return kode_cbg;
	}
	public void setKode_cbg(String kode_cbg) {
		this.kode_cbg = kode_cbg;
	}
	public String getKota_kantor() {
		return kota_kantor;
	}
	public void setKota_kantor(String kota_kantor) {
		this.kota_kantor = kota_kantor;
	}
	public String getKota_kantora() {
		return kota_kantora;
	}
	public void setKota_kantora(String kota_kantora) {
		this.kota_kantora = kota_kantora;
	}
	public String getKota_rumah() {
		return kota_rumah;
	}
	public void setKota_rumah(String kota_rumah) {
		this.kota_rumah = kota_rumah;
	}
	public String getKota_rumaha() {
		return kota_rumaha;
	}
	public void setKota_rumaha(String kota_rumaha) {
		this.kota_rumaha = kota_rumaha;
	}
	public String getLca_nama() {
		return lca_nama;
	}
	public void setLca_nama(String lca_nama) {
		this.lca_nama = lca_nama;
	}
	public String getLgj_id() {
		return lgj_id;
	}
	public void setLgj_id(String lgj_id) {
		this.lgj_id = lgj_id;
	}
	public String getLgj_note() {
		return lgj_note;
	}
	public void setLgj_note(String lgj_note) {
		this.lgj_note = lgj_note;
	}
	public ArrayList<Keluarga> getListKeluarga() {
		return listKeluarga;
	}
	public void setListKeluarga(List<Keluarga> listKeluarga) {
		this.listKeluarga = Common.serializableList(listKeluarga);
	}
	public String getLjb_id() {
		return ljb_id;
	}
	public void setLjb_id(String ljb_id) {
		this.ljb_id = ljb_id;
	}
	public String getLjb_note() {
		return ljb_note;
	}
	public void setLjb_note(String ljb_note) {
		this.ljb_note = ljb_note;
	}
	public Integer getLsag_id() {
		return lsag_id;
	}
	public void setLsag_id(Integer lsag_id) {
		this.lsag_id = lsag_id;
	}
	public String getLsag_name() {
		return lsag_name;
	}
	public void setLsag_name(String lsag_name) {
		this.lsag_name = lsag_name;
	}
	public Integer getLscb_id() {
		return lscb_id;
	}
	public void setLscb_id(Integer lscb_id) {
		this.lscb_id = lscb_id;
	}
	public Integer getLsed_id() {
		return lsed_id;
	}
	public void setLsed_id(Integer lsed_id) {
		this.lsed_id = lsed_id;
	}
	public String getLsed_name() {
		return lsed_name;
	}
	public void setLsed_name(String lsed_name) {
		this.lsed_name = lsed_name;
	}
	public Integer getLside_id() {
		return lside_id;
	}
	public void setLside_id(Integer lside_id) {
		this.lside_id = lside_id;
	}
	public String getLside_name() {
		return lside_name;
	}
	public void setLside_name(String lside_name) {
		this.lside_name = lside_name;
	}
	public Integer getLsne_id() {
		return lsne_id;
	}
	public void setLsne_id(Integer lsne_id) {
		this.lsne_id = lsne_id;
	}
	public String getLsne_note() {
		return lsne_note;
	}
	public void setLsne_note(String lsne_note) {
		this.lsne_note = lsne_note;
	}
	public Integer getLsre_id() {
		return lsre_id;
	}
	public void setLsre_id(Integer lsre_id) {
		this.lsre_id = lsre_id;
	}
	public String getLsre_relation() {
		return lsre_relation;
	}
	public void setLsre_relation(String lsre_relation) {
		this.lsre_relation = lsre_relation;
	}
	public Integer getLssp_id() {
		return lssp_id;
	}
	public void setLssp_id(Integer lssp_id) {
		this.lssp_id = lssp_id;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
	public String getLwk_id() {
		return lwk_id;
	}
	public void setLwk_id(String lwk_id) {
		this.lwk_id = lwk_id;
	}
	public String getLsrg_id() {
		return lsrg_id;
	}
	public void setLsrg_id(String lsrg_id) {
		this.lsrg_id = lsrg_id;
	}
	public Integer getMcl_blacklist() {
		return mcl_blacklist;
	}
	public void setMcl_blacklist(Integer mcl_blacklist) {
		this.mcl_blacklist = mcl_blacklist;
	}
	public String getMcl_data_salah() {
		return mcl_data_salah;
	}
	public void setMcl_data_salah(String mcl_data_salah) {
		this.mcl_data_salah = mcl_data_salah;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public String getMcl_gelar() {
		return mcl_gelar;
	}
	public void setMcl_gelar(String mcl_gelar) {
		this.mcl_gelar = mcl_gelar;
	}
	public String getMcl_id() {
		return mcl_id;
	}
	public void setMcl_id(String mcl_id) {
		this.mcl_id = mcl_id;
	}
	public Integer getMcl_jenis() {
		return mcl_jenis;
	}
	public void setMcl_jenis(Integer mcl_jenis) {
		this.mcl_jenis = mcl_jenis;
	}
	public Date getMcl_tgl_input() {
		return mcl_tgl_input;
	}
	public void setMcl_tgl_input(Date mcl_tgl_input) {
		this.mcl_tgl_input = mcl_tgl_input;
	}
	public String getMkl_industri() {
		return mkl_industri;
	}
	public void setMkl_industri(String mkl_industri) {
		this.mkl_industri = mkl_industri;
	}
	public String getMkl_kerja() {
		return mkl_kerja;
	}
	public void setMkl_kerja(String mkl_kerja) {
		this.mkl_kerja = mkl_kerja;
	}
	public String getMkl_pendanaan() {
		return mkl_pendanaan;
	}
	public void setMkl_pendanaan(String mkl_pendanaan) {
		this.mkl_pendanaan = mkl_pendanaan;
	}
	public String getMkl_penghasilan() {
		return mkl_penghasilan;
	}
	public void setMkl_penghasilan(String mkl_penghasilan) {
		this.mkl_penghasilan = mkl_penghasilan;
	}
	public String getMkl_tujuan() {
		return mkl_tujuan;
	}
	public void setMkl_tujuan(String mkl_tujuan) {
		this.mkl_tujuan = mkl_tujuan;
	}
	public String getMpn_job_desc() {
		return mpn_job_desc;
	}
	public void setMpn_job_desc(String mpn_job_desc) {
		this.mpn_job_desc = mpn_job_desc;
	}
	public Integer getMsag_id() {
		return msag_id;
	}
	public void setMsag_id(Integer msag_id) {
		this.msag_id = msag_id;
	}
	public Date getMspe_date_birth() {
		return mspe_date_birth;
	}
	public void setMspe_date_birth(Date mspe_date_birth) {
		this.mspe_date_birth = mspe_date_birth;
	}
	public String getMspe_email() {
		return mspe_email;
	}
	public void setMspe_email(String mspe_email) {
		this.mspe_email = mspe_email;
	}
	public Integer getMspe_lama_kerja() {
		return mspe_lama_kerja;
	}
	public void setMspe_lama_kerja(Integer mspe_lama_kerja) {
		this.mspe_lama_kerja = mspe_lama_kerja;
	}
	public String getMspe_mother() {
		return mspe_mother;
	}
	public void setMspe_mother(String mspe_mother) {
		this.mspe_mother = mspe_mother;
	}
	public String getMspe_no_identity() {
		return mspe_no_identity;
	}
	public void setMspe_no_identity(String mspe_no_identity) {
		this.mspe_no_identity = mspe_no_identity;
	}
	public String getMspe_place_birth() {
		return mspe_place_birth;
	}
	public void setMspe_place_birth(String mspe_place_birth) {
		this.mspe_place_birth = mspe_place_birth;
	}
	public Integer getMspe_sex() {
		return mspe_sex;
	}
	public void setMspe_sex(Integer mspe_sex) {
		this.mspe_sex = mspe_sex;
	}
	public String getMspe_sex2() {
		return mspe_sex2;
	}
	public void setMspe_sex2(String mspe_sex2) {
		this.mspe_sex2 = mspe_sex2;
	}
	public Integer getMspe_sts_mrt() {
		return mspe_sts_mrt;
	}
	public void setMspe_sts_mrt(Integer mspe_sts_mrt) {
		this.mspe_sts_mrt = mspe_sts_mrt;
	}
	public Integer getMspo_age() {
		return mspo_age;
	}
	public void setMspo_age(Integer mspo_age) {
		this.mspo_age = mspo_age;
	}
	public String getMspo_ao() {
		return mspo_ao;
	}
	public void setMspo_ao(String mspo_ao) {
		this.mspo_ao = mspo_ao;
	}
	public String getMspo_customer() {
		return mspo_customer;
	}
	public void setMspo_customer(String mspo_customer) {
		this.mspo_customer = mspo_customer;
	}
	public String getMspo_customer_nama() {
		return mspo_customer_nama;
	}
	public void setMspo_customer_nama(String mspo_customer_nama) {
		this.mspo_customer_nama = mspo_customer_nama;
	}
	public Integer getMspo_follow_up() {
		return mspo_follow_up;
	}
	public void setMspo_follow_up(Integer mspo_follow_up) {
		this.mspo_follow_up = mspo_follow_up;
	}
	public Integer getMspo_jenis_terbit() {
		return mspo_jenis_terbit;
	}
	public void setMspo_jenis_terbit(Integer mspo_jenis_terbit) {
		this.mspo_jenis_terbit = mspo_jenis_terbit;
	}
	public Integer getMspo_komisi_bii() {
		return mspo_komisi_bii;
	}
	public void setMspo_komisi_bii(Integer mspo_komisi_bii) {
		this.mspo_komisi_bii = mspo_komisi_bii;
	}
	public String getMspo_no_blanko() {
		return mspo_no_blanko;
	}
	public void setMspo_no_blanko(String mspo_no_blanko) {
		this.mspo_no_blanko = mspo_no_blanko;
	}
	public String getMspo_policy_holder() {
		return mspo_policy_holder;
	}
	public void setMspo_policy_holder(String mspo_policy_holder) {
		this.mspo_policy_holder = mspo_policy_holder;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public Integer getMspo_pribadi() {
		return mspo_pribadi;
	}
	public void setMspo_pribadi(Integer mspo_pribadi) {
		this.mspo_pribadi = mspo_pribadi;
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
	public Integer getMspo_umur_beasiswa() {
		return mspo_umur_beasiswa;
	}
	public void setMspo_umur_beasiswa(Integer mspo_umur_beasiswa) {
		this.mspo_umur_beasiswa = mspo_umur_beasiswa;
	}
	public Integer getMspo_umur_bieasiswa() {
		return mspo_umur_bieasiswa;
	}
	public void setMspo_umur_bieasiswa(Integer mspo_umur_bieasiswa) {
		this.mspo_umur_bieasiswa = mspo_umur_bieasiswa;
	}
	public Double getMspo_under_table() {
		return mspo_under_table;
	}
	public void setMspo_under_table(Double mspo_under_table) {
		this.mspo_under_table = mspo_under_table;
	}
	public Integer getMste_age() {
		return mste_age;
	}
	public void setMste_age(Integer mste_age) {
		this.mste_age = mste_age;
	}
	public Integer getMste_dewasa() {
		return mste_dewasa;
	}
	public void setMste_dewasa(Integer mste_dewasa) {
		this.mste_dewasa = mste_dewasa;
	}
	public Integer getMste_flag_guthrie() {
		return mste_flag_guthrie;
	}
	public void setMste_flag_guthrie(Integer mste_flag_guthrie) {
		this.mste_flag_guthrie = mste_flag_guthrie;
	}
	public Integer getMste_insured_no() {
		return mste_insured_no;
	}
	public void setMste_insured_no(Integer mste_insured_no) {
		this.mste_insured_no = mste_insured_no;
	}
	public Double getMste_pct_dplk() {
		return mste_pct_dplk;
	}
	public void setMste_pct_dplk(Double mste_pct_dplk) {
		this.mste_pct_dplk = mste_pct_dplk;
	}
	public Integer getMste_spaj_asli() {
		return mste_spaj_asli;
	}
	public void setMste_spaj_asli(Integer mste_spaj_asli) {
		this.mste_spaj_asli = mste_spaj_asli;
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
	public String getNama_anak1() {
		return nama_anak1;
	}
	public void setNama_anak1(String nama_anak1) {
		this.nama_anak1 = nama_anak1;
	}
	public String getNama_anak2() {
		return nama_anak2;
	}
	public void setNama_anak2(String nama_anak2) {
		this.nama_anak2 = nama_anak2;
	}
	public String getNama_anak3() {
		return nama_anak3;
	}
	public void setNama_anak3(String nama_anak3) {
		this.nama_anak3 = nama_anak3;
	}
	public String getNama_ao() {
		return nama_ao;
	}
	public void setNama_ao(String nama_ao) {
		this.nama_ao = nama_ao;
	}
	public String getNama_kota() {
		return nama_kota;
	}
	public void setNama_kota(String nama_kota) {
		this.nama_kota = nama_kota;
	}
	public String getNama_si() {
		return nama_si;
	}
	public void setNama_si(String nama_si) {
		this.nama_si = nama_si;
	}
	public String getNik() {
		return nik;
	}
	public void setNik(String nik) {
		this.nik = nik;
	}
	public String getNo_fax() {
		return no_fax;
	}
	public void setNo_fax(String no_fax) {
		this.no_fax = no_fax;
	}
	public String getNo_hp() {
		return no_hp;
	}
	public void setNo_hp(String no_hp) {
		this.no_hp = no_hp;
	}
	public String getNo_hp2() {
		return no_hp2;
	}
	public void setNo_hp2(String no_hp2) {
		this.no_hp2 = no_hp2;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTelpon_kantor() {
		return telpon_kantor;
	}
	public void setTelpon_kantor(String telpon_kantor) {
		this.telpon_kantor = telpon_kantor;
	}
	public String getTelpon_kantor2() {
		return telpon_kantor2;
	}
	public void setTelpon_kantor2(String telpon_kantor2) {
		this.telpon_kantor2 = telpon_kantor2;
	}
	public String getTelpon_rumah() {
		return telpon_rumah;
	}
	public void setTelpon_rumah(String telpon_rumah) {
		this.telpon_rumah = telpon_rumah;
	}
	public String getTelpon_rumah2() {
		return telpon_rumah2;
	}
	public void setTelpon_rumah2(String telpon_rumah2) {
		this.telpon_rumah2 = telpon_rumah2;
	}
	public Date getTgl_mspo_under_table() {
		return tgl_mspo_under_table;
	}
	public void setTgl_mspo_under_table(Date tgl_mspo_under_table) {
		this.tgl_mspo_under_table = tgl_mspo_under_table;
	}
	public String getTgl_pp() {
		return tgl_pp;
	}
	public void setTgl_pp(String tgl_pp) {
		this.tgl_pp = tgl_pp;
	}
	public Date getTgllhr_anak1() {
		return tgllhr_anak1;
	}
	public void setTgllhr_anak1(Date tgllhr_anak1) {
		this.tgllhr_anak1 = tgllhr_anak1;
	}
	public Date getTgllhr_anak2() {
		return tgllhr_anak2;
	}
	public void setTgllhr_anak2(Date tgllhr_anak2) {
		this.tgllhr_anak2 = tgllhr_anak2;
	}
	public Date getTgllhr_anak3() {
		return tgllhr_anak3;
	}
	public void setTgllhr_anak3(Date tgllhr_anak3) {
		this.tgllhr_anak3 = tgllhr_anak3;
	}
	public Date getTgllhr_si() {
		return tgllhr_si;
	}
	public void setTgllhr_si(Date tgllhr_si) {
		this.tgllhr_si = tgllhr_si;
	}
	public String getTujuana() {
		return tujuana;
	}
	public void setTujuana(String tujuana) {
		this.tujuana = tujuana;
	}
	public Integer getUsiapp() {
		return usiapp;
	}
	public void setUsiapp(Integer usiapp) {
		this.usiapp = usiapp;
	}
	public Integer getMspo_pay_period() {
		return mspo_pay_period;
	}
	public void setMspo_pay_period(Integer mspo_pay_period) {
		this.mspo_pay_period = mspo_pay_period;
	}
	public String getDanaa2() {
		return danaa2;
	}
	public void setDanaa2(String danaa2) {
		this.danaa2 = danaa2;
	}
	
	
	
}
