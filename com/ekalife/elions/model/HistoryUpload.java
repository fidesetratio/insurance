package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class HistoryUpload implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String upload_id;                      
	private String code_id;                        
	private String old_code_id;                    
	private String filename;                       
	private String temp_filename;                  
	private Date revisi_date;                      
	private String upload_jenis;                   
	private String status;                         
	private Integer revisi;                        
	private Integer lus_id;                        
	private String keterangan;                     
	private String path;
	private String jabatan;
	private String ketentuan;
	private Date upload_date;
	
	public Date getRevisi_date() {
		return revisi_date;
	}
	public void setRevisi_date(Date revisi_date) {
		this.revisi_date = revisi_date;
	}
	public String getJabatan() {
		return jabatan;
	}
	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
	}
	public String getKetentuan() {
		return ketentuan;
	}
	public void setKetentuan(String ketentuan) {
		this.ketentuan = ketentuan;
	}
	public String getCode_id() {
		return code_id;
	}
	public void setCode_id(String code_id) {
		this.code_id = code_id;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
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
	public String getOld_code_id() {
		return old_code_id;
	}
	public void setOld_code_id(String old_code_id) {
		this.old_code_id = old_code_id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getRevisi() {
		return revisi;
	}
	public void setRevisi(Integer revisi) {
		this.revisi = revisi;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTemp_filename() {
		return temp_filename;
	}
	public void setTemp_filename(String temp_filename) {
		this.temp_filename = temp_filename;
	}
	public Date getUpload_date() {
		return upload_date;
	}
	public void setUpload_date(Date upload_date) {
		this.upload_date = upload_date;
	}
	public String getUpload_id() {
		return upload_id;
	}
	public void setUpload_id(String upload_id) {
		this.upload_id = upload_id;
	}
	public String getUpload_jenis() {
		return upload_jenis;
	}
	public void setUpload_jenis(String upload_jenis) {
		this.upload_jenis = upload_jenis;
	} 
	
}