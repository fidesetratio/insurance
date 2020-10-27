package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Kesehatan implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;                       
	private Integer msnm_sehat;                    
	private Integer msnm_berat;                    
	private Integer msnm_tinggi;                   
	private Integer msnm_berubah;                  
	private Integer msnm_turun_naik;               
	private Integer msnm_banyak;                   
	private String msnm_alasan_berat;              
	private Integer msnm_perawatan;                
	private Integer msnm_operasi;                  
	private Integer msnm_laboratorium;             
	private Integer msnm_demam;                    
	private Integer msnm_tumor;                    
	private Integer msnm_pendarahan;               
	private Integer msnm_pingsan;                  
	private Integer msnm_asma;                     
	private Integer msnm_jantung;                  
	private Integer msnm_diabetes;                 
	private Integer msnm_darah_tinggi;             
	private Integer msnm_kencing_darah;            
	private Integer msnm_gondok;                   
	private Integer msnm_parkinson;                
	private Integer msnm_lambung;                  
	private Integer msnm_rhematik;                 
	private Integer msnm_kelamin;                  
	private Integer msnm_cacat;                    
	private String msnm_alasan_cacat;              
	private Integer msnm_suntikan;                 
	private Integer msnm_tranfusi;                 
	private Integer msnm_tolak_donor;              
	private String msnm_alasan_tolak;              
	private Integer msnm_menstruasi;               
	private Integer msnm_hamil;                    
	private Integer msnm_bulan;                    
	private Integer msnm_melahirkan;               
	private Integer msnm_banyak_lahir;             
	private Date msnm_tahun_akhir;                 
	private Integer msnm_keguguran;                
	private Integer msnm_suami_ins;                
	private String msnm_desc_ins;                  
	private String msnm_alasan_sehat;              
	private String msnm_hobi;                      
	private Integer msnm_kelainan;                 
	private Integer msnm_gila;                     
	private Integer msnm_lain;  
	private Integer msdok_number;                  
	private String msdok_penyakit;                 
	private Date msdok_date;                       
	private Integer msdok_lama;                    
	private String msdok_dokter;
	private String keadaan;
	private String msnm_desc_sakit; 
	private String msnm_keterangan;
	
	public String getMsnm_keterangan() {
		return msnm_keterangan;
	}
	public void setMsnm_keterangan(String msnm_keterangan) {
		this.msnm_keterangan = msnm_keterangan;
	}
	public String getMsnm_desc_sakit() {
		return msnm_desc_sakit;
	}
	public void setMsnm_desc_sakit(String msnm_desc_sakit) {
		this.msnm_desc_sakit = msnm_desc_sakit;
	}
	public String getKeadaan() {
		return keadaan;
	}
	public void setKeadaan(String keadaan) {
		this.keadaan = keadaan;
	}
	public Date getMsdok_date() {
		return msdok_date;
	}
	public void setMsdok_date(Date msdok_date) {
		this.msdok_date = msdok_date;
	}
	public String getMsdok_dokter() {
		return msdok_dokter;
	}
	public void setMsdok_dokter(String msdok_dokter) {
		this.msdok_dokter = msdok_dokter;
	}
	public Integer getMsdok_lama() {
		return msdok_lama;
	}
	public void setMsdok_lama(Integer msdok_lama) {
		this.msdok_lama = msdok_lama;
	}
	public Integer getMsdok_number() {
		return msdok_number;
	}
	public void setMsdok_number(Integer msdok_number) {
		this.msdok_number = msdok_number;
	}
	public String getMsdok_penyakit() {
		return msdok_penyakit;
	}
	public void setMsdok_penyakit(String msdok_penyakit) {
		this.msdok_penyakit = msdok_penyakit;
	}
	public String getMsnm_alasan_berat() {
		return msnm_alasan_berat;
	}
	public void setMsnm_alasan_berat(String msnm_alasan_berat) {
		this.msnm_alasan_berat = msnm_alasan_berat;
	}
	public String getMsnm_alasan_cacat() {
		return msnm_alasan_cacat;
	}
	public void setMsnm_alasan_cacat(String msnm_alasan_cacat) {
		this.msnm_alasan_cacat = msnm_alasan_cacat;
	}
	public String getMsnm_alasan_sehat() {
		return msnm_alasan_sehat;
	}
	public void setMsnm_alasan_sehat(String msnm_alasan_sehat) {
		this.msnm_alasan_sehat = msnm_alasan_sehat;
	}
	public String getMsnm_alasan_tolak() {
		return msnm_alasan_tolak;
	}
	public void setMsnm_alasan_tolak(String msnm_alasan_tolak) {
		this.msnm_alasan_tolak = msnm_alasan_tolak;
	}
	public Integer getMsnm_asma() {
		return msnm_asma;
	}
	public void setMsnm_asma(Integer msnm_asma) {
		this.msnm_asma = msnm_asma;
	}
	public Integer getMsnm_banyak() {
		return msnm_banyak;
	}
	public void setMsnm_banyak(Integer msnm_banyak) {
		this.msnm_banyak = msnm_banyak;
	}
	public Integer getMsnm_banyak_lahir() {
		return msnm_banyak_lahir;
	}
	public void setMsnm_banyak_lahir(Integer msnm_banyak_lahir) {
		this.msnm_banyak_lahir = msnm_banyak_lahir;
	}
	public Integer getMsnm_berat() {
		return msnm_berat;
	}
	public void setMsnm_berat(Integer msnm_berat) {
		this.msnm_berat = msnm_berat;
	}
	public Integer getMsnm_berubah() {
		return msnm_berubah;
	}
	public void setMsnm_berubah(Integer msnm_berubah) {
		this.msnm_berubah = msnm_berubah;
	}
	public Integer getMsnm_bulan() {
		return msnm_bulan;
	}
	public void setMsnm_bulan(Integer msnm_bulan) {
		this.msnm_bulan = msnm_bulan;
	}
	public Integer getMsnm_cacat() {
		return msnm_cacat;
	}
	public void setMsnm_cacat(Integer msnm_cacat) {
		this.msnm_cacat = msnm_cacat;
	}
	public Integer getMsnm_darah_tinggi() {
		return msnm_darah_tinggi;
	}
	public void setMsnm_darah_tinggi(Integer msnm_darah_tinggi) {
		this.msnm_darah_tinggi = msnm_darah_tinggi;
	}
	public Integer getMsnm_demam() {
		return msnm_demam;
	}
	public void setMsnm_demam(Integer msnm_demam) {
		this.msnm_demam = msnm_demam;
	}
	public String getMsnm_desc_ins() {
		return msnm_desc_ins;
	}
	public void setMsnm_desc_ins(String msnm_desc_ins) {
		this.msnm_desc_ins = msnm_desc_ins;
	}
	public Integer getMsnm_diabetes() {
		return msnm_diabetes;
	}
	public void setMsnm_diabetes(Integer msnm_diabetes) {
		this.msnm_diabetes = msnm_diabetes;
	}
	public Integer getMsnm_gila() {
		return msnm_gila;
	}
	public void setMsnm_gila(Integer msnm_gila) {
		this.msnm_gila = msnm_gila;
	}
	public Integer getMsnm_gondok() {
		return msnm_gondok;
	}
	public void setMsnm_gondok(Integer msnm_gondok) {
		this.msnm_gondok = msnm_gondok;
	}
	public Integer getMsnm_hamil() {
		return msnm_hamil;
	}
	public void setMsnm_hamil(Integer msnm_hamil) {
		this.msnm_hamil = msnm_hamil;
	}
	public String getMsnm_hobi() {
		return msnm_hobi;
	}
	public void setMsnm_hobi(String msnm_hobi) {
		this.msnm_hobi = msnm_hobi;
	}
	public Integer getMsnm_jantung() {
		return msnm_jantung;
	}
	public void setMsnm_jantung(Integer msnm_jantung) {
		this.msnm_jantung = msnm_jantung;
	}
	public Integer getMsnm_keguguran() {
		return msnm_keguguran;
	}
	public void setMsnm_keguguran(Integer msnm_keguguran) {
		this.msnm_keguguran = msnm_keguguran;
	}
	public Integer getMsnm_kelainan() {
		return msnm_kelainan;
	}
	public void setMsnm_kelainan(Integer msnm_kelainan) {
		this.msnm_kelainan = msnm_kelainan;
	}
	public Integer getMsnm_kelamin() {
		return msnm_kelamin;
	}
	public void setMsnm_kelamin(Integer msnm_kelamin) {
		this.msnm_kelamin = msnm_kelamin;
	}
	public Integer getMsnm_kencing_darah() {
		return msnm_kencing_darah;
	}
	public void setMsnm_kencing_darah(Integer msnm_kencing_darah) {
		this.msnm_kencing_darah = msnm_kencing_darah;
	}
	public Integer getMsnm_laboratorium() {
		return msnm_laboratorium;
	}
	public void setMsnm_laboratorium(Integer msnm_laboratorium) {
		this.msnm_laboratorium = msnm_laboratorium;
	}
	public Integer getMsnm_lain() {
		return msnm_lain;
	}
	public void setMsnm_lain(Integer msnm_lain) {
		this.msnm_lain = msnm_lain;
	}
	public Integer getMsnm_lambung() {
		return msnm_lambung;
	}
	public void setMsnm_lambung(Integer msnm_lambung) {
		this.msnm_lambung = msnm_lambung;
	}
	public Integer getMsnm_melahirkan() {
		return msnm_melahirkan;
	}
	public void setMsnm_melahirkan(Integer msnm_melahirkan) {
		this.msnm_melahirkan = msnm_melahirkan;
	}
	public Integer getMsnm_menstruasi() {
		return msnm_menstruasi;
	}
	public void setMsnm_menstruasi(Integer msnm_menstruasi) {
		this.msnm_menstruasi = msnm_menstruasi;
	}
	public Integer getMsnm_operasi() {
		return msnm_operasi;
	}
	public void setMsnm_operasi(Integer msnm_operasi) {
		this.msnm_operasi = msnm_operasi;
	}
	public Integer getMsnm_parkinson() {
		return msnm_parkinson;
	}
	public void setMsnm_parkinson(Integer msnm_parkinson) {
		this.msnm_parkinson = msnm_parkinson;
	}
	public Integer getMsnm_pendarahan() {
		return msnm_pendarahan;
	}
	public void setMsnm_pendarahan(Integer msnm_pendarahan) {
		this.msnm_pendarahan = msnm_pendarahan;
	}
	public Integer getMsnm_perawatan() {
		return msnm_perawatan;
	}
	public void setMsnm_perawatan(Integer msnm_perawatan) {
		this.msnm_perawatan = msnm_perawatan;
	}
	public Integer getMsnm_pingsan() {
		return msnm_pingsan;
	}
	public void setMsnm_pingsan(Integer msnm_pingsan) {
		this.msnm_pingsan = msnm_pingsan;
	}
	public Integer getMsnm_rhematik() {
		return msnm_rhematik;
	}
	public void setMsnm_rhematik(Integer msnm_rhematik) {
		this.msnm_rhematik = msnm_rhematik;
	}
	public Integer getMsnm_sehat() {
		return msnm_sehat;
	}
	public void setMsnm_sehat(Integer msnm_sehat) {
		this.msnm_sehat = msnm_sehat;
	}
	public Integer getMsnm_suami_ins() {
		return msnm_suami_ins;
	}
	public void setMsnm_suami_ins(Integer msnm_suami_ins) {
		this.msnm_suami_ins = msnm_suami_ins;
	}
	public Integer getMsnm_suntikan() {
		return msnm_suntikan;
	}
	public void setMsnm_suntikan(Integer msnm_suntikan) {
		this.msnm_suntikan = msnm_suntikan;
	}
	public Date getMsnm_tahun_akhir() {
		return msnm_tahun_akhir;
	}
	public void setMsnm_tahun_akhir(Date msnm_tahun_akhir) {
		this.msnm_tahun_akhir = msnm_tahun_akhir;
	}
	public Integer getMsnm_tinggi() {
		return msnm_tinggi;
	}
	public void setMsnm_tinggi(Integer msnm_tinggi) {
		this.msnm_tinggi = msnm_tinggi;
	}
	public Integer getMsnm_tolak_donor() {
		return msnm_tolak_donor;
	}
	public void setMsnm_tolak_donor(Integer msnm_tolak_donor) {
		this.msnm_tolak_donor = msnm_tolak_donor;
	}
	public Integer getMsnm_tranfusi() {
		return msnm_tranfusi;
	}
	public void setMsnm_tranfusi(Integer msnm_tranfusi) {
		this.msnm_tranfusi = msnm_tranfusi;
	}
	public Integer getMsnm_tumor() {
		return msnm_tumor;
	}
	public void setMsnm_tumor(Integer msnm_tumor) {
		this.msnm_tumor = msnm_tumor;
	}
	public Integer getMsnm_turun_naik() {
		return msnm_turun_naik;
	}
	public void setMsnm_turun_naik(Integer msnm_turun_naik) {
		this.msnm_turun_naik = msnm_turun_naik;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}                   

}
