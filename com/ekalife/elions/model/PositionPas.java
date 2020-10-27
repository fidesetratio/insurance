package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class PositionPas implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_id;   
    private Date msps_date;   
    private String msps_desc;   
    private String lus_login_name;
	public String getReg_id() {
		return reg_id;
	}
	public void setReg_id(String regId) {
		reg_id = regId;
	}
	public Date getMsps_date() {
		return msps_date;
	}
	public void setMsps_date(Date mspsDate) {
		msps_date = mspsDate;
	}
	public String getMsps_desc() {
		return msps_desc;
	}
	public void setMsps_desc(String mspsDesc) {
		msps_desc = mspsDesc;
	}
	public String getLus_login_name() {
		return lus_login_name;
	}
	public void setLus_login_name(String lusLoginName) {
		lus_login_name = lusLoginName;
	}
    
    
}
