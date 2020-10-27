package com.ekalife.elions.web.refund.vo;

import java.util.Date;


/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: MstPtcTmVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : fadly
 * Version              : 1.0
 * Creation Date    	: March 9, 2012 4:20:04 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

public class MstPtcTmVO
{
	String no_jm;
    Integer mtm_position;
    Date tgl_jurnal;
    String no_pre;
    String mtm_print;
    Integer user_input;
    Date tgl_input;
    Date tgl_trans;
    
    
	public String getNo_jm() {
		return no_jm;
	}
	public void setNo_jm(String no_jm) {
		this.no_jm = no_jm;
	}
	public Integer getMtm_position() {
		return mtm_position;
	}
	public void setMtm_position(Integer mtm_position) {
		this.mtm_position = mtm_position;
	}
	public Date getTgl_jurnal() {
		return tgl_jurnal;
	}
	public void setTgl_jurnal(Date tgl_jurnal) {
		this.tgl_jurnal = tgl_jurnal;
	}
	public String getNo_pre() {
		return no_pre;
	}
	public void setNo_pre(String no_pre) {
		this.no_pre = no_pre;
	}
	public String getMtm_print() {
		return mtm_print;
	}
	public void setMtm_print(String mtm_print) {
		this.mtm_print = mtm_print;
	}
	public Integer getUser_input() {
		return user_input;
	}
	public void setUser_input(Integer user_input) {
		this.user_input = user_input;
	}
	public Date getTgl_input() {
		return tgl_input;
	}
	public void setTgl_input(Date tgl_input) {
		this.tgl_input = tgl_input;
	}
	public Date getTgl_trans() {
		return tgl_trans;
	}
	public void setTgl_trans(Date tgl_trans) {
		this.tgl_trans = tgl_trans;
	}
    

}
