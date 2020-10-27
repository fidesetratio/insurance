/**
 * @author  : Ferry Harlim
 * @created : Dec 27, 2006 
 */
package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Bfa implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String kd_region;                      
	private String kd_koord;                       
	private String kd_area;                        
	private String msag_id;                        
	private String kd_cabang;                      
	private String nama_bfa;                       
	private Integer level_id;                      
	private String leader;                         
	private Integer status_aktif;                  
	private Date tgl_aktif;                        
	private String nama_user;                      
	private String pass;                           
	private Integer act_pass;                      
	private String kode;                           
	private Integer no_batch;                      
	private Integer flag_jab;
	private String nama_cabang;
	public Integer getAct_pass() {
		return act_pass;
	}
	public void setAct_pass(Integer act_pass) {
		this.act_pass = act_pass;
	}
	public Integer getFlag_jab() {
		return flag_jab;
	}
	public void setFlag_jab(Integer flag_jab) {
		this.flag_jab = flag_jab;
	}
	public String getKd_area() {
		return kd_area;
	}
	public void setKd_area(String kd_area) {
		this.kd_area = kd_area;
	}
	public String getKd_cabang() {
		return kd_cabang;
	}
	public void setKd_cabang(String kd_cabang) {
		this.kd_cabang = kd_cabang;
	}
	public String getKd_koord() {
		return kd_koord;
	}
	public void setKd_koord(String kd_koord) {
		this.kd_koord = kd_koord;
	}
	public String getKd_region() {
		return kd_region;
	}
	public void setKd_region(String kd_region) {
		this.kd_region = kd_region;
	}
	public String getKode() {
		return kode;
	}
	public void setKode(String kode) {
		this.kode = kode;
	}
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	public Integer getLevel_id() {
		return level_id;
	}
	public void setLevel_id(Integer level_id) {
		this.level_id = level_id;
	}
	public String getMsag_id() {
		return msag_id;
	}
	public void setMsag_id(String msag_id) {
		this.msag_id = msag_id;
	}
	public String getNama_bfa() {
		return nama_bfa;
	}
	public void setNama_bfa(String nama_bfa) {
		this.nama_bfa = nama_bfa;
	}
	public String getNama_user() {
		return nama_user;
	}
	public void setNama_user(String nama_user) {
		this.nama_user = nama_user;
	}
	public Integer getNo_batch() {
		return no_batch;
	}
	public void setNo_batch(Integer no_batch) {
		this.no_batch = no_batch;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public Integer getStatus_aktif() {
		return status_aktif;
	}
	public void setStatus_aktif(Integer status_aktif) {
		this.status_aktif = status_aktif;
	}
	public Date getTgl_aktif() {
		return tgl_aktif;
	}
	public void setTgl_aktif(Date tgl_aktif) {
		this.tgl_aktif = tgl_aktif;
	}
	public String getNama_cabang() {
		return nama_cabang;
	}
	public void setNama_cabang(String nama_cabang) {
		this.nama_cabang = nama_cabang;
	} 
}
