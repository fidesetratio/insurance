package com.ekalife.elions.model;

import java.io.Serializable;

public class Agentba implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nama;                           
	private String mst_leader;                     
	private Integer lstb_id;                       
	private Integer lsle_id;                       
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getLsle_id() {
		return lsle_id;
	}
	public void setLsle_id(Integer lsle_id) {
		this.lsle_id = lsle_id;
	}
	public Integer getLstb_id() {
		return lstb_id;
	}
	public void setLstb_id(Integer lstb_id) {
		this.lstb_id = lstb_id;
	}
	public String getMst_leader() {
		return mst_leader;
	}
	public void setMst_leader(String mst_leader) {
		this.mst_leader = mst_leader;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}   

}
