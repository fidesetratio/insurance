package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * MODEL CLASS UNTUK EKA.MST_DATA_BMI data untuk produk2 bank muamalat
 * 
 * @author Yusuf
 * @since Nov 19, 2008 (8:00:28 PM)
 */
public class Bmi implements Serializable {

	//UNTUK DATA EKA.MST_DATA_BMI
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2145471789872216469L;
	public String mdb_id;
	public Integer jenis;
	public Integer lsbp_id;
	public String norek;
	public String no_seri;
	public String nama;
	public Date tgl_lahir;
	public Date tgl_debet;
	public Double premi;
	public Integer flag_debet;
	public String reg_spaj;
	public Integer tahun_ke;
	public Integer premi_ke;
	public Integer posisi;
	public Date tgl_input;
	public Integer user_input;
	public Date tgl_proses;
	public Integer user_proses;
	
	//UNTUK GENERATE TEXT FILE HARIAN
	
	//private String no_seri;                        
	public String kd_bank;                        
	public String no_kartu;                       
	//private String nama;                           
	public String sex;                            
	public String mspe_place_birth;               
	public String tglahir;                        
	public Integer agama;                         
	public String alamat;                         
	public String atagih;                         
	public String telp_rmh;                       
	public String telp_ktr;                       
	public String no_hp;                          
	public String ktp;                            
	public String mspe_no_identity;               
	public String mspe_mother;                    
	public Integer sumber;                        
	public Integer ptambah;                       
	public Integer kerja;                         
	public Integer ptetap;                        
	public Integer ptdk;                          
	public String agen;                           
	public String rekagen;                        
	public String bdate;                          
	public String edate;                          
	public String akhir;                          
	//public Double premi;                          
	
	public String getMdb_id() {
		return mdb_id;
	}
	public void setMdb_id(String mdb_id) {
		this.mdb_id = mdb_id;
	}
	public Integer getJenis() {
		return jenis;
	}
	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}
	public Integer getLsbp_id() {
		return lsbp_id;
	}
	public void setLsbp_id(Integer lsbp_id) {
		this.lsbp_id = lsbp_id;
	}
	public String getNorek() {
		return norek;
	}
	public void setNorek(String norek) {
		this.norek = norek;
	}
	public String getNo_seri() {
		return no_seri;
	}
	public void setNo_seri(String no_seri) {
		this.no_seri = no_seri;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public Date getTgl_lahir() {
		return tgl_lahir;
	}
	public void setTgl_lahir(Date tgl_lahir) {
		this.tgl_lahir = tgl_lahir;
	}
	public Date getTgl_debet() {
		return tgl_debet;
	}
	public void setTgl_debet(Date tgl_debet) {
		this.tgl_debet = tgl_debet;
	}
	public Double getPremi() {
		return premi;
	}
	public void setPremi(Double premi) {
		this.premi = premi;
	}
	public Integer getFlag_debet() {
		return flag_debet;
	}
	public void setFlag_debet(Integer flag_debet) {
		this.flag_debet = flag_debet;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getTahun_ke() {
		return tahun_ke;
	}
	public void setTahun_ke(Integer tahun_ke) {
		this.tahun_ke = tahun_ke;
	}
	public Integer getPremi_ke() {
		return premi_ke;
	}
	public void setPremi_ke(Integer premi_ke) {
		this.premi_ke = premi_ke;
	}
	public Integer getPosisi() {
		return posisi;
	}
	public void setPosisi(Integer posisi) {
		this.posisi = posisi;
	}
	public Date getTgl_input() {
		return tgl_input;
	}
	public void setTgl_input(Date tgl_input) {
		this.tgl_input = tgl_input;
	}
	public Integer getUser_input() {
		return user_input;
	}
	public void setUser_input(Integer user_input) {
		this.user_input = user_input;
	}
	public Date getTgl_proses() {
		return tgl_proses;
	}
	public void setTgl_proses(Date tgl_proses) {
		this.tgl_proses = tgl_proses;
	}
	public Integer getUser_proses() {
		return user_proses;
	}
	public void setUser_proses(Integer user_proses) {
		this.user_proses = user_proses;
	}
	public String getKd_bank() {
		return kd_bank;
	}
	public void setKd_bank(String kd_bank) {
		this.kd_bank = kd_bank;
	}
	public String getNo_kartu() {
		return no_kartu;
	}
	public void setNo_kartu(String no_kartu) {
		this.no_kartu = no_kartu;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getMspe_place_birth() {
		return mspe_place_birth;
	}
	public void setMspe_place_birth(String mspe_place_birth) {
		this.mspe_place_birth = mspe_place_birth;
	}
	public String getTglahir() {
		return tglahir;
	}
	public void setTglahir(String tglahir) {
		this.tglahir = tglahir;
	}
	public Integer getAgama() {
		return agama;
	}
	public void setAgama(Integer agama) {
		this.agama = agama;
	}
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	public String getAtagih() {
		return atagih;
	}
	public void setAtagih(String atagih) {
		this.atagih = atagih;
	}
	public String getTelp_rmh() {
		return telp_rmh;
	}
	public void setTelp_rmh(String telp_rmh) {
		this.telp_rmh = telp_rmh;
	}
	public String getTelp_ktr() {
		return telp_ktr;
	}
	public void setTelp_ktr(String telp_ktr) {
		this.telp_ktr = telp_ktr;
	}
	public String getNo_hp() {
		return no_hp;
	}
	public void setNo_hp(String no_hp) {
		this.no_hp = no_hp;
	}
	public String getKtp() {
		return ktp;
	}
	public void setKtp(String ktp) {
		this.ktp = ktp;
	}
	public String getMspe_no_identity() {
		return mspe_no_identity;
	}
	public void setMspe_no_identity(String mspe_no_identity) {
		this.mspe_no_identity = mspe_no_identity;
	}
	public String getMspe_mother() {
		return mspe_mother;
	}
	public void setMspe_mother(String mspe_mother) {
		this.mspe_mother = mspe_mother;
	}
	public Integer getSumber() {
		return sumber;
	}
	public void setSumber(Integer sumber) {
		this.sumber = sumber;
	}
	public Integer getPtambah() {
		return ptambah;
	}
	public void setPtambah(Integer ptambah) {
		this.ptambah = ptambah;
	}
	public Integer getKerja() {
		return kerja;
	}
	public void setKerja(Integer kerja) {
		this.kerja = kerja;
	}
	public Integer getPtetap() {
		return ptetap;
	}
	public void setPtetap(Integer ptetap) {
		this.ptetap = ptetap;
	}
	public Integer getPtdk() {
		return ptdk;
	}
	public void setPtdk(Integer ptdk) {
		this.ptdk = ptdk;
	}
	public String getAgen() {
		return agen;
	}
	public void setAgen(String agen) {
		this.agen = agen;
	}
	public String getRekagen() {
		return rekagen;
	}
	public void setRekagen(String rekagen) {
		this.rekagen = rekagen;
	}
	public String getBdate() {
		return bdate;
	}
	public void setBdate(String bdate) {
		this.bdate = bdate;
	}
	public String getEdate() {
		return edate;
	}
	public void setEdate(String edate) {
		this.edate = edate;
	}
	public String getAkhir() {
		return akhir;
	}
	public void setAkhir(String akhir) {
		this.akhir = akhir;
	}
}
