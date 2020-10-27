package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Rekening_auto_debet implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private String reg_spaj; 
	private String lsbp_id;
	private String bank;
	private String mar_holder;
	private Integer lus_id;
	private String mspo_nasabah_acc;
	private String mar_acc_no;
	private String tglvalid; 
	private Date tgldebet;
	private String mrc_no_ac; 
	private String mrc_kurs;
	private String lku_id;
	//private Date mar_expired;
	private String mar_expired;
	//private Date mste_tgl_recur;
	private String mste_tgl_recur;
	
	private Integer mar_active; 
	private String [] mrc_no_ac_split;
	
	private int typeRek;
	private Integer mar_jenis;
	private String lbn_id;
	private String mrc_no_ac_lama;
	private String keterangan;
	private Integer flag_jn_tabungan;
	private String lsbp_nama;
	private Integer flag_autodebet_nb;
	
	public Rekening_auto_debet() {
		mrc_no_ac_split=new String[21];
	}
	
	public String[] getMrc_no_ac_split() {
		return mrc_no_ac_split;
	}

	public void setMrc_no_ac_split(String[] mrc_no_ac_split) {
		this.mrc_no_ac_split = mrc_no_ac_split;
	}
 
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	
	public String getMar_acc_no() {
		return mar_acc_no;
	}
	public void setMar_acc_no(String mar_acc_no) {
		this.mar_acc_no = mar_acc_no;
	}
	public String getMar_holder() {
		return mar_holder;
	}
	public void setMar_holder(String mar_holder) {
		this.mar_holder = mar_holder;
	}
	
	public String getMspo_nasabah_acc() {
		return mspo_nasabah_acc;
	}

	public void setMspo_nasabah_acc(String mspo_nasabah_acc) {
		this.mspo_nasabah_acc = mspo_nasabah_acc;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Date getTgldebet() {
		return tgldebet;
	}
	public void setTgldebet(Date tgldebet) {
		this.tgldebet = tgldebet;
	}
	
	public String getTglvalid() {
		return tglvalid;
	}

	public void setTglvalid(String tglvalid) {
		this.tglvalid = tglvalid;
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
	public String getMrc_no_ac() {
		return mrc_no_ac;
	}
	public void setMrc_no_ac(String mrc_no_ac) {
		this.mrc_no_ac = mrc_no_ac;
	}

	public String getLku_id() {
		return lku_id;
	}

	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}

	public String getMrc_kurs() {
		return mrc_kurs;
	}

	public void setMrc_kurs(String mrc_kurs) {
		this.mrc_kurs = mrc_kurs;
	}

	public Integer getMar_active() {
		return mar_active;
	}

	public void setMar_active(Integer mar_active) {
		this.mar_active = mar_active;
	}

	/*public Date getMar_expired() {
		return mar_expired;
	}

	public void setMar_expired(Date mar_expired) {
		this.mar_expired = mar_expired;
	}*/
	
	public String getMar_expired() {
		return mar_expired;
	}

	public void setMar_expired(String mar_expired) {
		this.mar_expired = mar_expired;
	}	
	
	/*public Date getMste_tgl_recur() {
		return mste_tgl_recur;
	}

	public void setMste_tgl_recur(Date mste_tgl_recur) {
		this.mste_tgl_recur = mste_tgl_recur;
	}*/ 	

	public int getTypeRek() {
		return typeRek;
	}

	public void setTypeRek(int typeRek) {
		this.typeRek = typeRek;
	}

	public Integer getMar_jenis() {
		return mar_jenis;
	}

	public void setMar_jenis(Integer mar_jenis) {
		this.mar_jenis = mar_jenis;
	}

	public String getLbn_id() {
		return lbn_id;
	}

	public void setLbn_id(String lbn_id) {
		this.lbn_id = lbn_id;
	}

	public String getMrc_no_ac_lama() {
		return mrc_no_ac_lama;
	}

	public void setMrc_no_ac_lama(String mrc_no_ac_lama) {
		this.mrc_no_ac_lama = mrc_no_ac_lama;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public String getMste_tgl_recur() {
		return mste_tgl_recur;
	}

	public void setMste_tgl_recur(String mste_tgl_recur) {
		this.mste_tgl_recur = mste_tgl_recur;
	}
	
	public Integer getFlag_jn_tabungan() {
		return flag_jn_tabungan;
	}

	public void setFlag_jn_tabungan(Integer flag_jn_tabungan) {
		this.flag_jn_tabungan = flag_jn_tabungan;
	}

	public String getLsbp_nama() {
		return lsbp_nama;
	}

	public void setLsbp_nama(String lsbp_nama) {
		this.lsbp_nama = lsbp_nama;
	}

	public Integer getFlag_autodebet_nb() {
		return flag_autodebet_nb;
	}

	public void setFlag_autodebet_nb(Integer flag_autodebet_nb) {
		this.flag_autodebet_nb = flag_autodebet_nb;
	}

	
}
