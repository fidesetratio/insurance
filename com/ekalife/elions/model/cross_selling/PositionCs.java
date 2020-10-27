package com.ekalife.elions.model.cross_selling;

import java.io.Serializable;
import java.util.Date;

/**
 * Tabel MST_POSITION_CS
 * 
 * @author Yusuf
 * @since Jul 21, 2008 (9:13:28 AM)
 */
public class PositionCs implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//tabel utama
	public String reg_spaj;                       
	public Date mspc_date;                        
	public Integer lspd_id;                       
	public Integer lus_id;                        
	public String mspc_desc;
	
	//tabel2 reference
	public String lus_login_name;
	public String lspd_position;
	
	public String getLus_login_name() {
		return lus_login_name;
	}
	public void setLus_login_name(String lus_login_name) {
		this.lus_login_name = lus_login_name;
	}
	public String getLspd_position() {
		return lspd_position;
	}
	public void setLspd_position(String lspd_position) {
		this.lspd_position = lspd_position;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Date getMspc_date() {
		return mspc_date;
	}
	public void setMspc_date(Date mspc_date) {
		this.mspc_date = mspc_date;
	}
	public Integer getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getMspc_desc() {
		return mspc_desc;
	}
	public void setMspc_desc(String mspc_desc) {
		this.mspc_desc = mspc_desc;
	}                      

}
