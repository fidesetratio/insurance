package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Fakultatif implements Serializable{
	/**
	 * Field type : long
	 * Create By : Bertho Rafitya Iwasurya
	 * Date Created : 11:06:00 AM
	 */
	private static final long serialVersionUID = 6953598876676726390L;

	private String reg_spaj;
	
	//data reasurer
	private Integer reasurader_type;
	private String nama_reasurader;
	private String cp_reasurader;
	private String tlp_reasurader;
	private String fax_reasurader;
	private String email_reasurader;
	private String no_surat;
	
	//data nasabah
	private String nama_pemegang;
	private String nama_tertanggung;
	private Date bod_pp;
	private Date bod_tt;
	private String pekerjaan;
	private Date beg_date;
	private Integer masa_asuransi;
	private Integer medis;
	private Integer age;
	
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Date getBeg_date() {
		return beg_date;
	}
	public void setBeg_date(Date beg_date) {
		this.beg_date = beg_date;
	}
	public Date getBod_pp() {
		return bod_pp;
	}
	public void setBod_pp(Date bod_pp) {
		this.bod_pp = bod_pp;
	}
	public Date getBod_tt() {
		return bod_tt;
	}
	public void setBod_tt(Date bod_tt) {
		this.bod_tt = bod_tt;
	}
	public String getCp_reasurader() {
		return cp_reasurader;
	}
	public void setCp_reasurader(String cp_reasurader) {
		this.cp_reasurader = cp_reasurader;
	}
	public String getEmail_reasurader() {
		return email_reasurader;
	}
	public void setEmail_reasurader(String email_reasurader) {
		this.email_reasurader = email_reasurader;
	}
	public String getFax_reasurader() {
		return fax_reasurader;
	}
	public void setFax_reasurader(String fax_reasurader) {
		this.fax_reasurader = fax_reasurader;
	}
	public Integer getMasa_asuransi() {
		return masa_asuransi;
	}
	public void setMasa_asuransi(Integer masa_asuransi) {
		this.masa_asuransi = masa_asuransi;
	}
	public Integer getMedis() {
		return medis;
	}
	public void setMedis(Integer medis) {
		this.medis = medis;
	}
	public String getNama_pemegang() {
		return nama_pemegang;
	}
	public void setNama_pemegang(String nama_pemegang) {
		this.nama_pemegang = nama_pemegang;
	}
	public String getNama_reasurader() {
		return nama_reasurader;
	}
	public void setNama_reasurader(String nama_reasurader) {
		this.nama_reasurader = nama_reasurader;
	}
	public String getNama_tertanggung() {
		return nama_tertanggung;
	}
	public void setNama_tertanggung(String nama_tertanggung) {
		this.nama_tertanggung = nama_tertanggung;
	}
	public String getNo_surat() {
		return no_surat;
	}
	public void setNo_surat(String no_surat) {
		this.no_surat = no_surat;
	}
	public String getPekerjaan() {
		return pekerjaan;
	}
	public void setPekerjaan(String pekerjaan) {
		this.pekerjaan = pekerjaan;
	}
	public Integer getReasurader_type() {
		return reasurader_type;
	}
	public void setReasurader_type(Integer reasurader_type) {
		this.reasurader_type = reasurader_type;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getTlp_reasurader() {
		return tlp_reasurader;
	}
	public void setTlp_reasurader(String tlp_reasurader) {
		this.tlp_reasurader = tlp_reasurader;
	}
	
	
	
	
	
}
