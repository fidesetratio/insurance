package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer no_urut;                       
	private String nik;                            
	private String nama;                           
	private String cabang;                         
	private String reg_spaj;                       
	private String dept;                           
	private String plan;                           
	private Double potongan;                       
	private Date tgl_proses;                       
	private Integer status;                        
	private String keterangan;
	public String getCabang() {
		return cabang;
	}
	public void setCabang(String cabang) {
		this.cabang = cabang;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getNik() {
		return nik;
	}
	public void setNik(String nik) {
		this.nik = nik;
	}
	public Integer getNo_urut() {
		return no_urut;
	}
	public void setNo_urut(Integer no_urut) {
		this.no_urut = no_urut;
	}
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	public Double getPotongan() {
		return potongan;
	}
	public void setPotongan(Double potongan) {
		this.potongan = potongan;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getTgl_proses() {
		return tgl_proses;
	}
	public void setTgl_proses(Date tgl_proses) {
		this.tgl_proses = tgl_proses;
	} 
}
