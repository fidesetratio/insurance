package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.List;

public class RelasiNasabah implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mns_kd_nasabah;                 
	private Integer mrn_no_relasi;                 
	private String mrn_nama;                       
	private String mrn_alamat;                     
	private String mrn_contact_no;                 
	private String mrn_ket_relasi;                 
	private String mrn_alasan;                     
	private Integer mrn_ref_to_bii;                
	private String referrer_id;
	private List<RelasiNasabah> listRelasiNasabah;
	
	public String getMns_kd_nasabah() {
		return mns_kd_nasabah;
	}
	public void setMns_kd_nasabah(String mns_kd_nasabah) {
		this.mns_kd_nasabah = mns_kd_nasabah;
	}
	public String getMrn_alamat() {
		return mrn_alamat;
	}
	public void setMrn_alamat(String mrn_alamat) {
		this.mrn_alamat = mrn_alamat;
	}
	public String getMrn_alasan() {
		return mrn_alasan;
	}
	public void setMrn_alasan(String mrn_alasan) {
		this.mrn_alasan = mrn_alasan;
	}
	public String getMrn_contact_no() {
		return mrn_contact_no;
	}
	public void setMrn_contact_no(String mrn_contact_no) {
		this.mrn_contact_no = mrn_contact_no;
	}
	public String getMrn_ket_relasi() {
		return mrn_ket_relasi;
	}
	public void setMrn_ket_relasi(String mrn_ket_relasi) {
		this.mrn_ket_relasi = mrn_ket_relasi;
	}
	public String getMrn_nama() {
		return mrn_nama;
	}
	public void setMrn_nama(String mrn_nama) {
		this.mrn_nama = mrn_nama;
	}
	public Integer getMrn_no_relasi() {
		return mrn_no_relasi;
	}
	public void setMrn_no_relasi(Integer mrn_no_relasi) {
		this.mrn_no_relasi = mrn_no_relasi;
	}
	public Integer getMrn_ref_to_bii() {
		return mrn_ref_to_bii;
	}
	public void setMrn_ref_to_bii(Integer mrn_ref_to_bii) {
		this.mrn_ref_to_bii = mrn_ref_to_bii;
	}
	public String getReferrer_id() {
		return referrer_id;
	}
	public void setReferrer_id(String referrer_id) {
		this.referrer_id = referrer_id;
	}
	public List<RelasiNasabah> getListRelasiNasabah() {
		return listRelasiNasabah;
	}
	public void setListRelasiNasabah(List<RelasiNasabah> listRelasiNasabah) {
		this.listRelasiNasabah = listRelasiNasabah;
	}
	
	
}