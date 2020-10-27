package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class CCDataWS implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2045096654774737419L;
	private String member_id;                      
	private String cc;
	private String cc_1;
	private String cc_2;
	private String cc_3;
	private String cc_4;
	private String name_on_cc;                     
	private String cc_exp;
	private String cc_exp_mm;
	private String cc_exp_yyyy;
	private Integer flag_active;                   
	private Integer cc_type;                       
	private Integer flag_new;                      
	private Integer cc_mode;                   
	private Integer hit_count;                     
	private String status_trx;                     
	private Integer flag_default;                  
	private Date create_date; 
	private byte[] cc_byte; 
	private byte[] name_on_cc_byte; 
	private byte[] cc_exp_byte;
	private Integer flag_update;
	private Integer hit_count_update;
	private String status_trx_update;
	
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
		if(cc!=null){
			this.cc_1=cc.substring(0,4);
			this.cc_2=cc.substring(4,8);
			this.cc_3=cc.substring(8,12);
			this.cc_4=cc.substring(12,16);
		}else{
			this.cc_1=null;
			this.cc_2=null;
			this.cc_3=null;
			this.cc_4=null;
		}
	}
	public byte[] getCc_byte() {
		return cc_byte;
	}
	public void setCc_byte(byte[] cc_byte) {
		this.cc_byte = cc_byte;
	}
	public String getCc_exp() {
		return cc_exp;
	}
	public void setCc_exp(String cc_exp) {
		this.cc_exp = cc_exp;
	}
	public byte[] getCc_exp_byte() {
		return cc_exp_byte;
	}
	public void setCc_exp_byte(byte[] cc_exp_byte) {
		this.cc_exp_byte = cc_exp_byte;
	}
	public Integer getCc_mode() {
		return cc_mode;
	}
	public void setCc_mode(Integer cc_mode) {
		this.cc_mode = cc_mode;
	}
	public Integer getCc_type() {
		return cc_type;
	}
	public void setCc_type(Integer cc_type) {
		this.cc_type = cc_type;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Integer getFlag_active() {
		return flag_active;
	}
	public void setFlag_active(Integer flag_active) {
		this.flag_active = flag_active;
	}
	public Integer getFlag_default() {
		return flag_default;
	}
	public void setFlag_default(Integer flag_default) {
		this.flag_default = flag_default;
	}
	public Integer getFlag_new() {
		return flag_new;
	}
	public void setFlag_new(Integer flag_new) {
		this.flag_new = flag_new;
	}
	public Integer getHit_count() {
		return hit_count;
	}
	public void setHit_count(Integer hit_count) {
		this.hit_count = hit_count;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getName_on_cc() {
		return name_on_cc;
	}
	public void setName_on_cc(String name_on_cc) {
		this.name_on_cc = name_on_cc;
	}
	public byte[] getName_on_cc_byte() {
		return name_on_cc_byte;
	}
	public void setName_on_cc_byte(byte[] name_on_cc_byte) {
		this.name_on_cc_byte = name_on_cc_byte;
	}
	public String getStatus_trx() {
		return status_trx;
	}
	public void setStatus_trx(String status_trx) {
		this.status_trx = status_trx;
	}
	public Integer getFlag_update() {
		return flag_update;
	}
	public void setFlag_update(Integer flag_update) {
		this.flag_update = flag_update;
	}
	public Integer getHit_count_update() {
		return hit_count_update;
	}
	public void setHit_count_update(Integer hit_count_update) {
		this.hit_count_update = hit_count_update;
	}
	public String getStatus_trx_update() {
		return status_trx_update;
	}
	public void setStatus_trx_update(String status_trx_update) {
		this.status_trx_update = status_trx_update;
	}
	public String getCc_1() {
		return cc_1;
	}
	public void setCc_1(String cc_1) {
		this.cc_1 = cc_1;
	}
	public String getCc_2() {
		return cc_2;
	}
	public void setCc_2(String cc_2) {
		this.cc_2 = cc_2;
	}
	public String getCc_3() {
		return cc_3;
	}
	public void setCc_3(String cc_3) {
		this.cc_3 = cc_3;
	}
	public String getCc_4() {
		return cc_4;
	}
	public void setCc_4(String cc_4) {
		this.cc_4 = cc_4;
	}
	public String getCc_exp_mm() {
		return cc_exp_mm;
	}
	public void setCc_exp_mm(String cc_exp_mm) {
		this.cc_exp_mm = cc_exp_mm;
	}
	public String getCc_exp_yyyy() {
		return cc_exp_yyyy;
	}
	public void setCc_exp_yyyy(String cc_exp_yyyy) {
		this.cc_exp_yyyy = cc_exp_yyyy;
	}
		

}
