package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Keluarga implements Serializable {


	private static final long serialVersionUID = 7858149439644200972L;
	private String reg_spaj;
	private String nama;
	private Integer lsre_id;
	private Date tanggal_lahir;
	private Integer no;
	private Integer insured;
	private String lsre_relation;
	
	public String getLsre_relation() {
		return lsre_relation;
	}
	public void setLsre_relation(String lsre_relation) {
		this.lsre_relation = lsre_relation;
	}
	public Integer getInsured() {
		return insured;
	}
	public void setInsured(Integer insured) {
		this.insured = insured;
	}
	public Integer getLsre_id() {
		return lsre_id;
	}
	public void setLsre_id(Integer lsre_id) {
		this.lsre_id = lsre_id;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public Integer getNo() {
		return no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Date getTanggal_lahir() {
		return tanggal_lahir;
	}
	public void setTanggal_lahir(Date tanggal_lahir) {
		this.tanggal_lahir = tanggal_lahir;
	}
	
}