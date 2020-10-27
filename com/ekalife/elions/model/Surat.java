package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * HRD.HRD_SURAT@ABSEN
 * 
 * @author yusuf
 * @since Sep 8, 2009 (9:43:12 AM)
 */
public class Surat implements Serializable {
	

	private static final long serialVersionUID = 9042340358427329301L;
	public Integer surat_id;
	public String no_surat;
	public String jdl_surat;
	public String grant_user;
	public String ljb_id;
	public Integer jns_surat;
	public String nm_file;
	public Date tgl_upload;
	public Date tgl_edit;
	public Integer is_active;
	public Integer is_agency;
	public Integer regional_flag;
	public Integer akm_flag;
	
	public Integer getIs_agency() {
		return is_agency;
	}
	public void setIs_agency(Integer is_agency) {
		this.is_agency = is_agency;
	}
	public Integer getIs_active() {
		return is_active;
	}
	public void setIs_active(Integer is_active) {
		this.is_active = is_active;
	}
	public String getJdl_surat() {
		return jdl_surat;
	}
	public void setJdl_surat(String jdl_surat) {
		this.jdl_surat = jdl_surat;
	}
	public Integer getJns_surat() {
		return jns_surat;
	}
	public void setJns_surat(Integer jns_surat) {
		this.jns_surat = jns_surat;
	}
	public String getNm_file() {
		return nm_file;
	}
	public void setNm_file(String nm_file) {
		this.nm_file = nm_file;
	}
	public String getNo_surat() {
		return no_surat;
	}
	public void setNo_surat(String no_surat) {
		this.no_surat = no_surat;
	}
	public Integer getSurat_id() {
		return surat_id;
	}
	public void setSurat_id(Integer surat_id) {
		this.surat_id = surat_id;
	}
	public Date getTgl_edit() {
		return tgl_edit;
	}
	public void setTgl_edit(Date tgl_edit) {
		this.tgl_edit = tgl_edit;
	}
	public Date getTgl_upload() {
		return tgl_upload;
	}
	public void setTgl_upload(Date tgl_upload) {
		this.tgl_upload = tgl_upload;
	}
	public Integer getAkm_flag() {
		return akm_flag;
	}
	public void setAkm_flag(Integer akm_flag) {
		this.akm_flag = akm_flag;
	}
	public Integer getRegional_flag() {
		return regional_flag;
	}
	public void setRegional_flag(Integer regional_flag) {
		this.regional_flag = regional_flag;
	}
	public String getGrant_user() {
		return grant_user;
	}
	public void setGrant_user(String grantUser) {
		grant_user = grantUser;
	}
	public String getLjb_id() {
		return ljb_id;
	}
	public void setLjb_id(String ljbId) {
		ljb_id = ljbId;
	}
}