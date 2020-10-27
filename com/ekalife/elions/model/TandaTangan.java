package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class TandaTangan implements Serializable {

	private static final long serialVersionUID = 32180049324508889L;
	
	public String mstt_id;
	public String mstt_nama;
	public String mcl_id;
	public String reg_spaj;
	public byte[] mstt_image;
	public String mstt_ext;
	public Integer create_id;
	public Date create_dt;
	public Integer modify_id;
	public Date modify_dt;
	
	public String getMstt_id() {
		return mstt_id;
	}
	public void setMstt_id(String mstt_id) {
		this.mstt_id = mstt_id;
	}
	public String getMstt_nama() {
		return mstt_nama;
	}
	public void setMstt_nama(String mstt_nama) {
		this.mstt_nama = mstt_nama;
	}
	public String getMcl_id() {
		return mcl_id;
	}
	public void setMcl_id(String mcl_id) {
		this.mcl_id = mcl_id;
	}
	public Date getCreate_dt() {
		return create_dt;
	}
	public void setCreate_dt(Date create_dt) {
		this.create_dt = create_dt;
	}
	public Integer getCreate_id() {
		return create_id;
	}
	public void setCreate_id(Integer create_id) {
		this.create_id = create_id;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Date getModify_dt() {
		return modify_dt;
	}
	public void setModify_dt(Date modify_dt) {
		this.modify_dt = modify_dt;
	}
	public Integer getModify_id() {
		return modify_id;
	}
	public void setModify_id(Integer modify_id) {
		this.modify_id = modify_id;
	}
	public String getMstt_ext() {
		return mstt_ext;
	}
	public void setMstt_ext(String mstt_ext) {
		this.mstt_ext = mstt_ext;
	}
	public byte[] getMstt_image() {
		return mstt_image;
	}
	public void setMstt_image(byte[] mstt_image) {
		this.mstt_image = mstt_image;
	}
	
}