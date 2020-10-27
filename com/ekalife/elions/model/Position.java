package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Position implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;   
    private Date msps_date;   
    private Integer lspd_id;   
    private Integer lssp_id;   
    private Integer lssa_id;   
    private String lus_id;   
    private String msps_desc;
    private String lus_login_name;
     
 	private String mspo_policy_no;                 
	private Integer mspo_provider;                 
	private String lspd_position;                  
	private String lssp_status;         
	private String status_accept;
	private Integer sub_id;
	
	public Integer getSub_id() {
		return sub_id;
	}
	public void setSub_id(Integer sub_id) {
		this.sub_id = sub_id;
	}
	public String getStatus_accept() {
		return status_accept;
	}
	public void setStatus_accept(String status_accept) {
		this.status_accept = status_accept;
	}
	public String getLus_login_name() {
		return lus_login_name;
	}
	public void setLus_login_name(String lus_login_name) {
		this.lus_login_name = lus_login_name;
	}
	public Integer getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}
	public Integer getLssa_id() {
		return lssa_id;
	}
	public void setLssa_id(Integer lssa_id) {
		this.lssa_id = lssa_id;
	}
	public Integer getLssp_id() {
		return lssp_id;
	}
	public void setLssp_id(Integer lssp_id) {
		this.lssp_id = lssp_id;
	}
	public String getLus_id() {
		return lus_id;
	}
	public void setLus_id(String lus_id) {
		this.lus_id = lus_id;
	}
	public Date getMsps_date() {
		return msps_date;
	}
	public void setMsps_date(Date msps_date) {
		this.msps_date = msps_date;
	}
	public String getMsps_desc() {
		return msps_desc;
	}
	public void setMsps_desc(String msps_desc) {
		this.msps_desc = msps_desc;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getLspd_position() {
		return lspd_position;
	}
	public void setLspd_position(String lspd_position) {
		this.lspd_position = lspd_position;
	}
	public String getLssp_status() {
		return lssp_status;
	}
	public void setLssp_status(String lssp_status) {
		this.lssp_status = lssp_status;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public Integer getMspo_provider() {
		return mspo_provider;
	}
	public void setMspo_provider(Integer mspo_provider) {
		this.mspo_provider = mspo_provider;
	}  
}
