package com.ekalife.elions.model;

import java.io.Serializable;

public class Agentrec implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer bisnis_id;
	private String agent_id;
	private Integer level_id;
	private Integer comm_id;
	private Integer sbm;
	private Integer bm;
	private String lca_id;
	private String msag_rm;
	private Integer lvl_fcd;
	
	public Integer getLvl_fcd() {
		return lvl_fcd;
	}
	public void setLvl_fcd(Integer lvl_fcd) {
		this.lvl_fcd = lvl_fcd;
	}
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
	public String getMsag_rm() {
		return msag_rm;
	}
	public void setMsag_rm(String msag_rm) {
		this.msag_rm = msag_rm;
	}
	public Integer getBm() {
		return bm;
	}
	public void setBm(Integer bm) {
		this.bm = bm;
	}
	public String getAgent_id() {
		return agent_id;
	}
	public Integer getSbm() {
		return sbm;
	}
	public void setSbm(Integer sbm) {
		this.sbm = sbm;
	}
	public void setAgent_id(String agent_id) {
		this.agent_id = agent_id;
	}
	public Integer getBisnis_id() {
		return bisnis_id;
	}
	public void setBisnis_id(Integer bisnis_id) {
		this.bisnis_id = bisnis_id;
	}
	public Integer getComm_id() {
		return comm_id;
	}
	public void setComm_id(Integer comm_id) {
		this.comm_id = comm_id;
	}
	public Integer getLevel_id() {
		return level_id;
	}
	public void setLevel_id(Integer level_id) {
		this.level_id = level_id;
	}

}
