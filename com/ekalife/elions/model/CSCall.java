package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class CSCall implements Serializable {

	private static final long serialVersionUID = -6595584214488101635L;
	private Date mscsf_tgl_back;                   
	private String reg_spaj;                       
	private Integer mscsf_dial_ke;                 
	private String flag_inout;                     
	private Date mscsf_beg_tgl_dial;               
	private Date mscsf_end_tgl_dial;               
	private Integer lus_id;                        
	private String mscsf_ket;
	
	public String getFlag_inout() {
		return flag_inout;
	}
	public void setFlag_inout(String flag_inout) {
		this.flag_inout = flag_inout;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public Date getMscsf_beg_tgl_dial() {
		return mscsf_beg_tgl_dial;
	}
	public void setMscsf_beg_tgl_dial(Date mscsf_beg_tgl_dial) {
		this.mscsf_beg_tgl_dial = mscsf_beg_tgl_dial;
	}
	public Integer getMscsf_dial_ke() {
		return mscsf_dial_ke;
	}
	public void setMscsf_dial_ke(Integer mscsf_dial_ke) {
		this.mscsf_dial_ke = mscsf_dial_ke;
	}
	public Date getMscsf_end_tgl_dial() {
		return mscsf_end_tgl_dial;
	}
	public void setMscsf_end_tgl_dial(Date mscsf_end_tgl_dial) {
		this.mscsf_end_tgl_dial = mscsf_end_tgl_dial;
	}
	public String getMscsf_ket() {
		return mscsf_ket;
	}
	public void setMscsf_ket(String mscsf_ket) {
		this.mscsf_ket = mscsf_ket;
	}
	public Date getMscsf_tgl_back() {
		return mscsf_tgl_back;
	}
	public void setMscsf_tgl_back(Date mscsf_tgl_back) {
		this.mscsf_tgl_back = mscsf_tgl_back;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}                      

}
