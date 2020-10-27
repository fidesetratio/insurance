package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Rekening_client implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private String lsbp_id;   
	private Integer lus_id;
	private String lsbp_nama;
	private String mrc_cabang;                     
	private String mrc_nama;                       
	private String mrc_no_ac;                      
	private Integer mrc_jenis;                     
	private Integer mrc_jn_nasabah;                
	private String reg_spaj;
	private String mrc_kota;
	private String mrc_kurs;
	private Date tgl_change;
	private String keterangan;
	private String mrc_no_ac_lama;
	private Integer mrc_pp; 
	private String lsbp_va;

	private String no_account;
	
	private Integer mrc_kuasa;                     
	private Date tgl_surat;                        
	private String kuasa;                          
	private Date tgl;                              
	private Integer id;                            
	private String notes;
	private int typeRek;
	
	//Tambahan dari Bertho split mrc_no_ac
	 private String [] mrc_no_ac_split; 
	
	public Rekening_client() {
		setMrc_kuasa(new Integer(0));
		mrc_no_ac_split=new String[21];
	}
	
	public String[] getMrc_no_ac_split() {
		return mrc_no_ac_split;
	}

	public void setMrc_no_ac_split(String[] mrc_no_ac_split) {
		this.mrc_no_ac_split = mrc_no_ac_split;
	}

	public String getMrc_no_ac_lama() {
		return mrc_no_ac_lama;
	}

	public void setMrc_no_ac_lama(String mrc_no_ac_lama) {
		this.mrc_no_ac_lama = mrc_no_ac_lama;
	}

	public String getNo_account() {
		return no_account;
	}

	public void setNo_account(String no_account) {
		this.no_account = no_account;
	}
	
	public Integer getMrc_kuasa() {
		return mrc_kuasa;
	}
	public void setMrc_kuasa(Integer mrc_kuasa) {
		this.mrc_kuasa = mrc_kuasa;
	}
	public Date getTgl_surat() {
		return tgl_surat;
	}
	public void setTgl_surat(Date tgl_surat) {
		this.tgl_surat = tgl_surat;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public Date getTgl_change() {
		return tgl_change;
	}
	public void setTgl_change(Date tgl_change) {
		this.tgl_change = tgl_change;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getLsbp_id() {
		return lsbp_id;
	}
	public void setLsbp_id(String lsbp_id) {
		this.lsbp_id = lsbp_id;
	}
	public String getLsbp_nama() {
		return lsbp_nama;
	}
	public void setLsbp_nama(String lsbp_nama) {
		this.lsbp_nama = lsbp_nama;
	}
	public String getMrc_cabang() {
		return mrc_cabang;
	}
	public void setMrc_cabang(String mrc_cabang) {
		this.mrc_cabang = mrc_cabang;
	}
	public Integer getMrc_jenis() {
		return mrc_jenis;
	}
	public void setMrc_jenis(Integer mrc_jenis) {
		this.mrc_jenis = mrc_jenis;
	}
	public Integer getMrc_jn_nasabah() {
		return mrc_jn_nasabah;
	}
	public void setMrc_jn_nasabah(Integer mrc_jn_nasabah) {
		this.mrc_jn_nasabah = mrc_jn_nasabah;
	}
	public String getMrc_kota() {
		return mrc_kota;
	}
	public void setMrc_kota(String mrc_kota) {
		this.mrc_kota = mrc_kota;
	}
	public String getMrc_kurs() {
		return mrc_kurs;
	}
	public void setMrc_kurs(String mrc_kurs) {
		this.mrc_kurs = mrc_kurs;
	}
	public String getMrc_nama() {
		return mrc_nama;
	}
	public void setMrc_nama(String mrc_nama) {
		this.mrc_nama = mrc_nama;
	}
	public String getMrc_no_ac() {
		return mrc_no_ac;
	}
	public void setMrc_no_ac(String mrc_no_ac) {
		this.mrc_no_ac = mrc_no_ac;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getKuasa() {
		return kuasa;
	}
	public void setKuasa(String kuasa) {
		this.kuasa = kuasa;
	}


	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Date getTgl() {
		return tgl;
	}
	public void setTgl(Date tgl) {
		this.tgl = tgl;
	}

	public int getTypeRek() {
		return typeRek;
	}

	public void setTypeRek(int typeRek) {
		this.typeRek = typeRek;
	}
	
	public Integer getMrc_pp() {
		return mrc_pp;
	}

	public void setMrc_pp(Integer mrc_pp) {
		this.mrc_pp = mrc_pp;
	}

	public String getLsbp_va() {
		return lsbp_va;
	}

	public void setLsbp_va(String lsbp_va) {
		this.lsbp_va = lsbp_va;
	}

}
