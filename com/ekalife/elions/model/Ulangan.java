package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Ulangan implements Serializable{

	private static final long serialVersionUID = 6938930140451063535L;
	
	private String reg_spaj;                       
	private Date tanggal;                          
	private String jenis;                          
	private Integer status_polis;                  
	private Integer lus_id;                        
	private String keterangan;                  
	private String id_ticket;
	
	private String lus_full_name;
	private String lssp_status;
	private Integer flagAdd;
	
	public Integer getFlagAdd() {
		return flagAdd;
	}
	public void setFlagAdd(Integer flagAdd) {
		this.flagAdd = flagAdd;
	}
	public String getLssp_status() {
		return lssp_status;
	}
	public void setLssp_status(String lssp_status) {
		this.lssp_status = lssp_status;
	}
	public String getLus_full_name() {
		return lus_full_name;
	}
	public void setLus_full_name(String lus_full_name) {
		this.lus_full_name = lus_full_name;
	}
	public String getJenis() {
		return jenis;
	}
	public void setJenis(String jenis) {
		this.jenis = jenis;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getStatus_polis() {
		return status_polis;
	}
	public void setStatus_polis(Integer status_polis) {
		this.status_polis = status_polis;
	}
	public Date getTanggal() {
		return tanggal;
	}
	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}
	public String getId_ticket() {
		return id_ticket;
	}
	public void setId_ticket(String id_ticket) {
		this.id_ticket = id_ticket;
	}
}
