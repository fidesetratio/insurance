package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ekalife.utils.Common;

public class Comment implements Serializable {

private static final long serialVersionUID = 1L;
	
	private String comm_id;
	private String comm_mofs_id;
	private String comm_spaj;
	private String comm_type;
	private String comm_comment;
	private String comm_tgl_input;
	private Integer comm_mofr_id;
	private Integer comm_add_num;
	
	public Integer getComm_add_num() {
		return comm_add_num;
	}
	public void setComm_add_num(Integer comm_add_num) {
		this.comm_add_num = comm_add_num;
	}
	public Integer getComm_mofr_id() {
		return comm_mofr_id;
	}
	public void setComm_mofr_id(Integer comm_mofr_id) {
		this.comm_mofr_id = comm_mofr_id;
	}
	public String getComm_id() {
		return comm_id;
	}
	public void setComm_id(String comm_id) {
		this.comm_id = comm_id;
	}
	public String getComm_mofs_id() {
		return comm_mofs_id;
	}
	public void setComm_mofs_id(String comm_mofs_id) {
		this.comm_mofs_id = comm_mofs_id;
	}
	public String getComm_spaj() {
		return comm_spaj;
	}
	public void setComm_spaj(String comm_spaj) {
		this.comm_spaj = comm_spaj;
	}
	public String getComm_type() {
		return comm_type;
	}
	public void setComm_type(String comm_type) {
		this.comm_type = comm_type;
	}
	public String getComm_comment() {
		return comm_comment;
	}
	public void setComm_comment(String comm_comment) {
		this.comm_comment = comm_comment;
	}
	public String getComm_tgl_input() {
		return comm_tgl_input;
	}
	public void setComm_tgl_input(String comm_tgl_input) {
		this.comm_tgl_input = comm_tgl_input;
	}
	
}