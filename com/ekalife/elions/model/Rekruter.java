package com.ekalife.elions.model;

import java.io.Serializable;

public class Rekruter implements Serializable {

	private static final long serialVersionUID = -2808139425580459253L;
	private String agent_id;
	private String jenis_rekrut;
	private String nama;
	private Integer sts_aktif;
	private Integer level_id;
	private Integer lbn_id;
	private String no_tab;
	private Integer msag_komisi;
	
	private Integer msag_active;
	private Integer msag_bay;
	private String lcalwk;
	
	public Integer getMsag_active() {
		return msag_active;
	}
	public void setMsag_active(Integer msagActive) {
		msag_active = msagActive;
	}
	public Integer getMsag_bay() {
		return msag_bay;
	}
	public void setMsag_bay(Integer msagBay) {
		msag_bay = msagBay;
	}
	public String getLcalwk() {
		return lcalwk;
	}
	public void setLcalwk(String lcalwk) {
		this.lcalwk = lcalwk;
	}
	public Integer getMsag_komisi() {
		return msag_komisi;
	}
	public void setMsag_komisi(Integer msagKomisi) {
		msag_komisi = msagKomisi;
	}
	public String getJenis_rekrut() {
		return jenis_rekrut;
	}
	public void setJenis_rekrut(String jenis_rekrut) {
		this.jenis_rekrut = jenis_rekrut;
	}
	public String getAgent_id() {
		return agent_id;
	}
	public void setAgent_id(String agent_id) {
		this.agent_id = agent_id;
	}
	public Integer getLbn_id() {
		return lbn_id;
	}
	public void setLbn_id(Integer lbn_id) {
		this.lbn_id = lbn_id;
	}
	public Integer getLevel_id() {
		return level_id;
	}
	public void setLevel_id(Integer level_id) {
		this.level_id = level_id;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getNo_tab() {
		return no_tab;
	}
	public void setNo_tab(String no_tab) {
		this.no_tab = no_tab;
	}
	public Integer getSts_aktif() {
		return sts_aktif;
	}
	public void setSts_aktif(Integer sts_aktif) {
		this.sts_aktif = sts_aktif;
	}
	
}
