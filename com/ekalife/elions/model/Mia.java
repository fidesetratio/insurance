package com.ekalife.elions.model;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Mia implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer isNew; //0: new, 1:exist 
	private String region;
	private String nama_recruiter;
	private Integer flagExist; //0:new, 1:exist
	private Integer status;
	
	private String mia_agensys_id;                 
	private Integer lus_id;                        
	private String msag_id;                        
	private String mia_recruiter;                  
	private Integer mia_level_recruit;             
	private String mcl_id;                         
	private Integer lbn_id;                        
	private String lca_id;                         
	private String lwk_id;                         
	private String lsrg_id;                        
	private String mia_no_kontrak;                 
	private String mia_ktp;                        
	private String mia_nama;                       
	private String mia_birth_place;                
	private String mia_birth_date;                   
	private String mia_input_date;                   
	private String mia_alamat;                     
	private String mia_awal_kontrak;                 
	private String mia_akhir_kontrak;                
	private String mia_tgl_aktif;                    
	private Integer mia_aktif;
	private Integer mia_level;                     
	private String mia_no_rek;
	private String mia_tgl_berkas;
	
	private String pendidikan;
	private String status_nikah;
	
	public String getMia_tgl_berkas() {
		return mia_tgl_berkas;
	}

	public void setMia_tgl_berkas(String mia_tgl_berkas) {
		this.mia_tgl_berkas = mia_tgl_berkas;
	}
	private List<DropDown> lsLevel;
	
	public Mia() {
		setIsNew(0);
		setLsLevel(new ArrayList<DropDown>());
		getLsLevel().add(new DropDown());
		setFlagExist(0);
		
	}
	
	public String getMia_agensys_id() {
		return mia_agensys_id;
	}
	public void setMia_agensys_id(String miaAgensysId) {
		mia_agensys_id = miaAgensysId;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lusId) {
		lus_id = lusId;
	}
	public String getMsag_id() {
		return msag_id;
	}
	public void setMsag_id(String msagId) {
		msag_id = msagId;
	}
	public String getMia_recruiter() {
		return mia_recruiter;
	}
	public void setMia_recruiter(String miaRecruiter) {
		mia_recruiter = miaRecruiter;
	}
	public Integer getMia_level_recruit() {
		return mia_level_recruit;
	}
	public void setMia_level_recruit(Integer miaLevelRecruit) {
		mia_level_recruit = miaLevelRecruit;
	}
	public String getMcl_id() {
		return mcl_id;
	}
	public void setMcl_id(String mclId) {
		mcl_id = mclId;
	}
	public Integer getLbn_id() {
		return lbn_id;
	}
	public void setLbn_id(Integer lbnId) {
		lbn_id = lbnId;
	}
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lcaId) {
		lca_id = lcaId;
	}
	public String getLwk_id() {
		return lwk_id;
	}
	public void setLwk_id(String lwkId) {
		lwk_id = lwkId;
	}
	public String getLsrg_id() {
		return lsrg_id;
	}
	public void setLsrg_id(String lsrgId) {
		lsrg_id = lsrgId;
	}
	public String getMia_no_kontrak() {
		return mia_no_kontrak;
	}
	public void setMia_no_kontrak(String miaNoKontrak) {
		mia_no_kontrak = miaNoKontrak;
	}
	public String getMia_ktp() {
		return mia_ktp;
	}
	public void setMia_ktp(String miaKtp) {
		mia_ktp = miaKtp;
	}
	public String getMia_nama() {
		return mia_nama;
	}
	public void setMia_nama(String miaNama) {
		mia_nama = miaNama;
	}
	public String getMia_birth_place() {
		return mia_birth_place;
	}
	public void setMia_birth_place(String miaBirthPlace) {
		mia_birth_place = miaBirthPlace;
	}
	public String getMia_birth_date() {
		return mia_birth_date;
	}
	public void setMia_birth_date(String miaBirthDate) {
		mia_birth_date = miaBirthDate;
	}
	public String getMia_input_date() {
		return mia_input_date;
	}
	public void setMia_input_date(String miaInputDate) {
		mia_input_date = miaInputDate;
	}
	public String getMia_alamat() {
		return mia_alamat;
	}
	public void setMia_alamat(String miaAlamat) {
		mia_alamat = miaAlamat;
	}
	public String getMia_awal_kontrak() {
		return mia_awal_kontrak;
	}
	public void setMia_awal_kontrak(String miaAwalKontrak) {
		mia_awal_kontrak = miaAwalKontrak;
	}
	public String getMia_akhir_kontrak() {
		return mia_akhir_kontrak;
	}
	public void setMia_akhir_kontrak(String miaAkhirKontrak) {
		mia_akhir_kontrak = miaAkhirKontrak;
	}
	public String getMia_tgl_aktif() {
		return mia_tgl_aktif;
	}
	public void setMia_tgl_aktif(String miaTglAktif) {
		mia_tgl_aktif = miaTglAktif;
	}
	public Integer getMia_aktif() {
		return mia_aktif;
	}
	public void setMia_aktif(Integer miaAktif) {
		mia_aktif = miaAktif;
	}
	public Integer getMia_level() {
		return mia_level;
	}
	public void setMia_level(Integer miaLevel) {
		mia_level = miaLevel;
	}
	public String getMia_no_rek() {
		return mia_no_rek;
	}
	public void setMia_no_rek(String miaNoRek) {
		mia_no_rek = miaNoRek;
	}
	public Integer getIsNew() {
		return isNew;
	}
	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public List<DropDown> getLsLevel() {
		return lsLevel;
	}
	public void setLsLevel(List<DropDown> lsLevel) {
		this.lsLevel = lsLevel;
	}
	public String getNama_recruiter() {
		return nama_recruiter;
	}
	public void setNama_recruiter(String namaRecruiter) {
		nama_recruiter = namaRecruiter;
	}
	public Integer getFlagExist() {
		return flagExist;
	}
	public void setFlagExist(Integer flagExist) {
		this.flagExist = flagExist;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}   
	public String getPendidikan() {
		return pendidikan;
	}

	public void setPendidikan(String pendidikan) {
		this.pendidikan = pendidikan;
	}

	public String getStatus_nikah() {
		return status_nikah;
	}

	public void setStatus_nikah(String status_nikah) {
		this.status_nikah = status_nikah;
	}
	
}
