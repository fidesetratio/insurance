package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class ContactPerson implements Serializable {

	private static final long serialVersionUID = -2874480219803318642L;
	private String mcl_id;                 
	private Integer no_urut;
	private Integer flag_ut;
	private String nama_lengkap;                   
	private String telp_kantor;                    
	private String telp_hp;                        
	private String email;                          
	private Integer cara_bayar;                    
	private String rek_no;                         
	private String rek_nama;                       
	private String rek_bank;                       
	private String rek_bank_cabang;                
	private String rek_bank_kota;                  
	private Integer lus_id;                        
	private Date input_date;
	private Integer mste_sex;
	
	private Integer lsre_id;
	private Integer pic_jenis;
	
	private Integer lti_id;
	private String mcl_gelar;
	private String mspe_mother;
	private String ljb_note;                       
	private String ljb_id;  
	private String mpn_job_desc; 
	private Integer lama_kerja;               
	private Integer lside_id;    
	private String no_identity;  
	private Integer lsne_id;   
	private Date date_birth;  
	private Integer mste_age;
	private String place_birth;         
	private String sts_mrt;   
	private String agama;
	private Integer lsag_id;  
	private Integer lsed_id;  
	
	private String alamat_rumah;
	private String kd_pos_rumah;      
	private String kota_rumah;       
	private String area_code_rumah;    
	private String telpon_rumah;      
	private String area_code_rumah2; 
	private String telpon_rumah2;   
	private String no_hp;   
	private String no_hp2;       
	private String alamat_kantor;                  
	private String kd_pos_kantor;   
	private String kota_kantor;   
	private String area_code_kantor;  
	private String telpon_kantor;  
	private String area_code_kantor2;  
	private String telpon_kantor2;   
	private String area_code_fax;
	private String no_fax;
	
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
	private String jenis_badan;
	private String npwp;
	private Integer flag_komisi;
	private String nama_kantor;
	private Date no_identity_expired;
	private String kota_perusahaan;
	
	public String getKota_perusahaan() {
		return kota_perusahaan;
	}
	public void setKota_perusahaan(String kota_perusahaan) {
		this.kota_perusahaan = kota_perusahaan;
	}
	public Integer getFlag_komisi() {
		return flag_komisi;
	}
	public void setFlag_komisi(Integer flag_komisi) {
		this.flag_komisi = flag_komisi;
	}
	public Integer getMste_sex() {
		return mste_sex;
	}
	public void setMste_sex(Integer mste_sex) {
		this.mste_sex = mste_sex;
	}
	public Integer getFlag_ut() {
		return flag_ut;
	}
	public void setFlag_ut(Integer flag_ut) {
		this.flag_ut = flag_ut;
	}
	public Integer getNo_urut() {
		return no_urut;
	}
	public void setNo_urut(Integer no_urut) {
		this.no_urut = no_urut;
	}
	public Integer getCara_bayar() {
		return cara_bayar;
	}
	public void setCara_bayar(Integer cara_bayar) {
		this.cara_bayar = cara_bayar;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getInput_date() {
		return input_date;
	}
	public void setInput_date(Date input_date) {
		this.input_date = input_date;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getMcl_id() {
		return mcl_id;
	}
	public void setMcl_id(String mcl_id) {
		this.mcl_id = mcl_id;
	}
	public String getNama_lengkap() {
		return nama_lengkap;
	}
	public void setNama_lengkap(String nama_lengkap) {
		this.nama_lengkap = nama_lengkap;
	}
	public String getRek_bank() {
		return rek_bank;
	}
	public void setRek_bank(String rek_bank) {
		this.rek_bank = rek_bank;
	}
	public String getRek_bank_cabang() {
		return rek_bank_cabang;
	}
	public void setRek_bank_cabang(String rek_bank_cabang) {
		this.rek_bank_cabang = rek_bank_cabang;
	}
	public String getRek_bank_kota() {
		return rek_bank_kota;
	}
	public void setRek_bank_kota(String rek_bank_kota) {
		this.rek_bank_kota = rek_bank_kota;
	}
	public String getRek_nama() {
		return rek_nama;
	}
	public void setRek_nama(String rek_nama) {
		this.rek_nama = rek_nama;
	}
	public String getRek_no() {
		return rek_no;
	}
	public void setRek_no(String rek_no) {
		this.rek_no = rek_no;
	}
	public String getTelp_hp() {
		return telp_hp;
	}
	public void setTelp_hp(String telp_hp) {
		this.telp_hp = telp_hp;
	}
	public String getTelp_kantor() {
		return telp_kantor;
	}
	public void setTelp_kantor(String telp_kantor) {
		this.telp_kantor = telp_kantor;
	}
	public String getAgama() {
		return agama;
	}
	public void setAgama(String agama) {
		this.agama = agama;
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
	public String getArea_code_rumah2() {
		return area_code_rumah2;
	}
	public void setArea_code_rumah2(String area_code_rumah2) {
		this.area_code_rumah2 = area_code_rumah2;
	}
	public Date getDate_birth() {
		return date_birth;
	}
	public void setDate_birth(Date date_birth) {
		this.date_birth = date_birth;
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
	public String getKota_kantor() {
		return kota_kantor;
	}
	public void setKota_kantor(String kota_kantor) {
		this.kota_kantor = kota_kantor;
	}
	public String getKota_rumah() {
		return kota_rumah;
	}
	public void setKota_rumah(String kota_rumah) {
		this.kota_rumah = kota_rumah;
	}
	public Integer getLama_kerja() {
		return lama_kerja;
	}
	public void setLama_kerja(Integer lama_kerja) {
		this.lama_kerja = lama_kerja;
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
	public Integer getLsed_id() {
		return lsed_id;
	}
	public void setLsed_id(Integer lsed_id) {
		this.lsed_id = lsed_id;
	}
	public Integer getLside_id() {
		return lside_id;
	}
	public void setLside_id(Integer lside_id) {
		this.lside_id = lside_id;
	}
	public Integer getLsne_id() {
		return lsne_id;
	}
	public void setLsne_id(Integer lsne_id) {
		this.lsne_id = lsne_id;
	}
	public Integer getLti_id() {
		return lti_id;
	}
	public void setLti_id(Integer lti_id) {
		this.lti_id = lti_id;
	}
	public String getMcl_gelar() {
		return mcl_gelar;
	}
	public void setMcl_gelar(String mcl_gelar) {
		this.mcl_gelar = mcl_gelar;
	}
	public String getMpn_job_desc() {
		return mpn_job_desc;
	}
	public void setMpn_job_desc(String mpn_job_desc) {
		this.mpn_job_desc = mpn_job_desc;
	}
	public String getMspe_mother() {
		return mspe_mother;
	}
	public void setMspe_mother(String mspe_mother) {
		this.mspe_mother = mspe_mother;
	}
	public Integer getMste_age() {
		return mste_age;
	}
	public void setMste_age(Integer mste_age) {
		this.mste_age = mste_age;
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
	public String getNama_si() {
		return nama_si;
	}
	public void setNama_si(String nama_si) {
		this.nama_si = nama_si;
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
	public String getNo_identity() {
		return no_identity;
	}
	public void setNo_identity(String no_identity) {
		this.no_identity = no_identity;
	}
	public String getPlace_birth() {
		return place_birth;
	}
	public void setPlace_birth(String place_birth) {
		this.place_birth = place_birth;
	}
	public String getSts_mrt() {
		return sts_mrt;
	}
	public void setSts_mrt(String sts_mrt) {
		this.sts_mrt = sts_mrt;
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
	public Integer getLsag_id() {
		return lsag_id;
	}
	public void setLsag_id(Integer lsag_id) {
		this.lsag_id = lsag_id;
	}
	public Integer getPic_jenis() {
		return pic_jenis;
	}
	public void setPic_jenis(Integer pic_jenis) {
		this.pic_jenis = pic_jenis;
	}
	public Integer getLsre_id() {
		return lsre_id;
	}
	public void setLsre_id(Integer lsre_id) {
		this.lsre_id = lsre_id;
	}
	public String getJenis_badan() {
		return jenis_badan;
	}
	public void setJenis_badan(String jenisBadan) {
		jenis_badan = jenisBadan;
	}
	public String getNpwp() {
		return npwp;
	}
	public void setNpwp(String npwp) {
		this.npwp = npwp;
	}
	public String getNama_kantor() {
		return nama_kantor;
	}
	public void setNama_kantor(String nama_kantor) {
		this.nama_kantor = nama_kantor;
	}                       
	public Date getNo_identity_expired() {
		return no_identity_expired;
	}
	public void setNo_identity_expired(Date no_identity_expired) {
		this.no_identity_expired = no_identity_expired;
	}	
}
