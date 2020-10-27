package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Aktivitas implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mns_kd_nasabah;                 
	private Integer pert_ke;                       
	private Integer kd_aktivitas;                  
	private Date tgl_pert;                         
	private String keterangan;                     
	private Integer status;                        
	private Integer approval;                      
	private Date tgl_transfer; 
	private List<Aktivitas> listAktivitas;
	
	public Integer getApproval() {
		return approval;
	}
	public void setApproval(Integer approval) {
		this.approval = approval;
	}
	public Integer getKd_aktivitas() {
		return kd_aktivitas;
	}
	public void setKd_aktivitas(Integer kd_aktivitas) {
		this.kd_aktivitas = kd_aktivitas;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public List<Aktivitas> getListAktivitas() {
		return listAktivitas;
	}
	public void setListAktivitas(List<Aktivitas> listAktivitas) {
		this.listAktivitas = listAktivitas;
	}
	public String getMns_kd_nasabah() {
		return mns_kd_nasabah;
	}
	public void setMns_kd_nasabah(String mns_kd_nasabah) {
		this.mns_kd_nasabah = mns_kd_nasabah;
	}
	public Integer getPert_ke() {
		return pert_ke;
	}
	public void setPert_ke(Integer pert_ke) {
		this.pert_ke = pert_ke;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getTgl_pert() {
		return tgl_pert;
	}
	public void setTgl_pert(Date tgl_pert) {
		this.tgl_pert = tgl_pert;
	}
	public Date getTgl_transfer() {
		return tgl_transfer;
	}
	public void setTgl_transfer(Date tgl_transfer) {
		this.tgl_transfer = tgl_transfer;
	}
	
	
}