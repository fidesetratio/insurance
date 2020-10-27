package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * LST_SCAN
 * 
 * @author Yusuf
 * @since Aug 5, 2011 (8:12:14 AM)
 *
 */
public class Scan implements Serializable {

	private static final long serialVersionUID = 1L;

	public Integer id;
	public String dept;
	public String ket;
	public String nmfile;
	public Date tgl;
	public Integer wajib;
	
	public Integer kd_scan;
	public String lus_id;
	public String lde_id;
	public String no_indek;
	public Date tgl_input;
	public String files_ad;
	public String tipe_file;
	public Integer jml_page;
	public Integer flag_aktif;
	public Integer jenis;
	
	public Integer lc_id;
	public Integer ljj_id;
	
	
	public Integer getKd_scan() {
		return kd_scan;
	}
	public void setKd_scan(Integer kd_scan) {
		this.kd_scan = kd_scan;
	}
	public String getLus_id() {
		return lus_id;
	}
	public void setLus_id(String lus_id) {
		this.lus_id = lus_id;
	}
	public String getLde_id() {
		return lde_id;
	}
	public void setLde_id(String lde_id) {
		this.lde_id = lde_id;
	}
	public String getNo_indek() {
		return no_indek;
	}
	public void setNo_indek(String no_indek) {
		this.no_indek = no_indek;
	}
	public Date getTgl_input() {
		return tgl_input;
	}
	public void setTgl_input(Date tgl_input) {
		this.tgl_input = tgl_input;
	}
	public String getFiles_ad() {
		return files_ad;
	}
	public void setFiles_ad(String files_ad) {
		this.files_ad = files_ad;
	}
	public String getTipe_file() {
		return tipe_file;
	}
	public void setTipe_file(String tipe_file) {
		this.tipe_file = tipe_file;
	}
	public Integer getJml_page() {
		return jml_page;
	}
	public void setJml_page(Integer jml_page) {
		this.jml_page = jml_page;
	}
	public Integer getFlag_aktif() {
		return flag_aktif;
	}
	public void setFlag_aktif(Integer flag_aktif) {
		this.flag_aktif = flag_aktif;
	}
	public Integer getJenis() {
		return jenis;
	}
	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getKet() {
		return ket;
	}
	public void setKet(String ket) {
		this.ket = ket;
	}
	public String getNmfile() {
		return nmfile;
	}
	public void setNmfile(String nmfile) {
		this.nmfile = nmfile;
	}
	public Date getTgl() {
		return tgl;
	}
	public void setTgl(Date tgl) {
		this.tgl = tgl;
	}
	public Integer getWajib() {
		return wajib;
	}
	public void setWajib(Integer wajib) {
		this.wajib = wajib;
	}
	public Integer getLc_id() {
		return lc_id;
	}
	public void setLc_id(Integer lc_id) {
		this.lc_id = lc_id;
	}
	public Integer getLjj_id() {
		return ljj_id;
	}
	public void setLjj_id(Integer ljj_id) {
		this.ljj_id = ljj_id;
	}
	
}
