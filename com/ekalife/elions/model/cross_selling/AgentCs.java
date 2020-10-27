package com.ekalife.elions.model.cross_selling;

import java.io.Serializable;

/**
 * Tabel MST_AGENT_CS
 * 
 * @author Yusuf
 * @since Jul 21, 2008 (9:13:20 AM)
 */
public class AgentCs implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//tabel utama
	public String reg_spaj;                       
	public String msag_id;                        
	public Integer lsle_id;                       
	public Integer lev_comm;                      
	public Integer flag_sbm;
	
	//tabel reference
	public String mcl_first;
	public String lca_id;
	public String lca_nama;
	public String lwk_id;
	public String lwk_nama;
	public String lsrg_id;
	public String lsrg_nama;
	
	public String getLca_nama() {
		return lca_nama;
	}
	public void setLca_nama(String lca_nama) {
		this.lca_nama = lca_nama;
	}
	public String getLwk_id() {
		return lwk_id;
	}
	public void setLwk_id(String lwk_id) {
		this.lwk_id = lwk_id;
	}
	public String getLwk_nama() {
		return lwk_nama;
	}
	public void setLwk_nama(String lwk_nama) {
		this.lwk_nama = lwk_nama;
	}
	public String getLsrg_id() {
		return lsrg_id;
	}
	public void setLsrg_id(String lsrg_id) {
		this.lsrg_id = lsrg_id;
	}
	public String getLsrg_nama() {
		return lsrg_nama;
	}
	public void setLsrg_nama(String lsrg_nama) {
		this.lsrg_nama = lsrg_nama;
	}
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getMsag_id() {
		return msag_id;
	}
	public void setMsag_id(String msag_id) {
		this.msag_id = msag_id;
	}
	public Integer getLsle_id() {
		return lsle_id;
	}
	public void setLsle_id(Integer lsle_id) {
		this.lsle_id = lsle_id;
	}
	public Integer getLev_comm() {
		return lev_comm;
	}
	public void setLev_comm(Integer lev_comm) {
		this.lev_comm = lev_comm;
	}
	public Integer getFlag_sbm() {
		return flag_sbm;
	}
	public void setFlag_sbm(Integer flag_sbm) {
		this.flag_sbm = flag_sbm;
	}                      

}
