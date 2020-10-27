package com.ekalife.elions.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MstInboxChecklist implements Serializable {
	
	/**Fadly Mathendra
	 * 
	 */
	private static final long serialVersionUID = -2032926849885830566L;
	private String mi_id; 
	private Integer ljj_id; 
	private Integer lc_id; 
	private Integer mi_flag; 
	private String mi_desc;
	private Integer create_id;
	private Date create_date;
	
	public MstInboxChecklist( String mi_id, Integer ljj_id, Integer lc_id, Integer mi_flag, String mi_desc, Integer create_id, Date create_date ){
		this.mi_id = mi_id;
		this.ljj_id = ljj_id;
		this.lc_id = lc_id;
		this.mi_flag = mi_flag;
		this.mi_desc = mi_desc;
		this.create_id = create_id;
		this.create_date = create_date;
	}
	public String getMi_id() {
		return mi_id;
	}
	public void setMi_id(String mi_id) {
		this.mi_id = mi_id;
	}
	public Integer getLjj_id() {
		return ljj_id;
	}
	public void setLjj_id(Integer ljj_id) {
		this.ljj_id = ljj_id;
	}
	public Integer getLc_id() {
		return lc_id;
	}
	public void setLc_id(Integer lc_id) {
		this.lc_id = lc_id;
	}
	public Integer getMi_flag() {
		return mi_flag;
	}
	public void setMi_flag(Integer mi_flag) {
		this.mi_flag = mi_flag;
	}
	public String getMi_desc() {
		return mi_desc;
	}
	public void setMi_desc(String mi_desc) {
		this.mi_desc = mi_desc;
	}
	public Integer getCreate_id() {
		return create_id;
	}
	public void setCreate_id(Integer create_id) {
		this.create_id = create_id;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	
}
