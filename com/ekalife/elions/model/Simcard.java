package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Simas Card
 * MST_SIMCARD
 * 
 * @author Yusuf
 * @since Sep 11, 2008 (1:38:58 PM)
 */
public class Simcard implements Serializable {

	private static final long serialVersionUID = 1L;
		
	private String notes;
	private Date tgl_naktif;
	private Date tgl_naktif_end;
	private Date tgl_naktif_endkartu;
	private Date tgl_naktif_endsurat;
	
	private Integer msc_jenis;                     
	private String no_kartu;                       
	private String nama;                           
	private Date tgl_akhir;                        
	private String mcl_id;                         
	private String reg_spaj;                       
	private Date tgl_lahir;                        
	private String alamat;                         
	private String kota;                           
	private String kode_pos;                       
	private Date tgl_proses;                       
	private Integer flag_print;                    
	private Date tgl_print;                        
	private Integer flag_aktif;                    
	private Integer flag_roll;                     
	private Integer lus_id;                        
	private Integer polis;                         
	private Double total_premi;                    
	private Integer kartu_ke;                      
	private Date tgl_input;                        
	private Integer flag_nb;                       
	private String lca_id;                         
	private String lca_cabang;
	private String mrc_no_kartu;
	
	public String getMrc_no_kartu() {
		return mrc_no_kartu;
	}
	public void setMrc_no_kartu(String mrcNoKartu) {
		mrc_no_kartu = mrcNoKartu;
	}
	private String lus_login_name;
	private String cabang_bank;
	
	private String[] no_kartu_split;
	
	public String[] getNo_kartu_split() {
		return no_kartu_split;
	}
	public void setNo_kartu_split(String[] noKartuSplit) {
		no_kartu_split = noKartuSplit;
	}
	public String getCabang_bank() {
		return cabang_bank;
	}
	public void setCabang_bank(String cabang_bank) {
		this.cabang_bank = cabang_bank;
	}
	public String getLus_login_name() {
		return lus_login_name;
	}
	public void setLus_login_name(String lus_login_name) {
		this.lus_login_name = lus_login_name;
	}
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	public Integer getFlag_aktif() {
		return flag_aktif;
	}
	public void setFlag_aktif(Integer flag_aktif) {
		this.flag_aktif = flag_aktif;
	}
	public Integer getFlag_nb() {
		return flag_nb;
	}
	public void setFlag_nb(Integer flag_nb) {
		this.flag_nb = flag_nb;
	}
	public Integer getFlag_print() {
		return flag_print;
	}
	public void setFlag_print(Integer flag_print) {
		this.flag_print = flag_print;
	}
	public Integer getFlag_roll() {
		return flag_roll;
	}
	public void setFlag_roll(Integer flag_roll) {
		this.flag_roll = flag_roll;
	}
	public Integer getKartu_ke() {
		return kartu_ke;
	}
	public void setKartu_ke(Integer kartu_ke) {
		this.kartu_ke = kartu_ke;
	}
	public String getKode_pos() {
		return kode_pos;
	}
	public void setKode_pos(String kode_pos) {
		this.kode_pos = kode_pos;
	}
	public String getKota() {
		return kota;
	}
	public void setKota(String kota) {
		this.kota = kota;
	}
	public String getLca_cabang() {
		return lca_cabang;
	}
	public void setLca_cabang(String lca_cabang) {
		this.lca_cabang = lca_cabang;
	}
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
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
	public Integer getMsc_jenis() {
		return msc_jenis;
	}
	public void setMsc_jenis(Integer msc_jenis) {
		this.msc_jenis = msc_jenis;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getNo_kartu() {
		return no_kartu;
	}
	public void setNo_kartu(String no_kartu) {
		this.no_kartu = no_kartu;
	}
	public Integer getPolis() {
		return polis;
	}
	public void setPolis(Integer polis) {
		this.polis = polis;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Date getTgl_akhir() {
		return tgl_akhir;
	}
	public void setTgl_akhir(Date tgl_akhir) {
		this.tgl_akhir = tgl_akhir;
	}
	public Date getTgl_input() {
		return tgl_input;
	}
	public void setTgl_input(Date tgl_input) {
		this.tgl_input = tgl_input;
	}
	public Date getTgl_lahir() {
		return tgl_lahir;
	}
	public void setTgl_lahir(Date tgl_lahir) {
		this.tgl_lahir = tgl_lahir;
	}
	public Date getTgl_print() {
		return tgl_print;
	}
	public void setTgl_print(Date tgl_print) {
		this.tgl_print = tgl_print;
	}
	public Date getTgl_proses() {
		return tgl_proses;
	}
	public void setTgl_proses(Date tgl_proses) {
		this.tgl_proses = tgl_proses;
	}
	public Double getTotal_premi() {
		return total_premi;
	}
	public void setTotal_premi(Double total_premi) {
		this.total_premi = total_premi;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Date getTgl_naktif() {
		return tgl_naktif;
	}
	public void setTgl_naktif(Date tgl_naktif) {
		this.tgl_naktif = tgl_naktif;
	}
	public Date getTgl_naktif_end() {
		return tgl_naktif_end;
	}
	public void setTgl_naktif_end(Date tgl_naktif_end) {
		this.tgl_naktif_end = tgl_naktif_end;
	}
	public Date getTgl_naktif_endkartu() {
		return tgl_naktif_endkartu;
	}
	public void setTgl_naktif_endkartu(Date tgl_naktif_endkartu) {
		this.tgl_naktif_endkartu = tgl_naktif_endkartu;
	}
	public Date getTgl_naktif_endsurat() {
		return tgl_naktif_endsurat;
	}
	public void setTgl_naktif_endsurat(Date tgl_naktif_endsurat) {
		this.tgl_naktif_endsurat = tgl_naktif_endsurat;
	}
}
