package com.ekalife.elions.model;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 * model untuk virtual account mst_va dan mst_det_va
 * */

public class VirtualAccount implements Serializable {

	private static final long serialVersionUID = 1L;
                    
	private String msv_id;
	private Date create_date;
	private Integer user_create;
	private Integer jenis_va;
	private Integer jenis_syariah;
	private Integer jenis_link;
	private String msv_amount_req;
	private String start_no_va_req;
	private String end_no_va_req;
	private String start_no_va_cetak;
	private String end_no_va_cetak;
	private String lsbp_id;

	private String no_va;
	private String spaj_temp;
	private String spaj;
	private Integer flag_active;
	private Date input_date;
	private Integer user_input;
	private String jenis_spaj;
	private String j_bank;
	private List<DropDown> jBank;
	
	
	public String getJenis_spaj() {
		return jenis_spaj;
	}
	public void setJenis_spaj(String jenis_spaj) {
		this.jenis_spaj = jenis_spaj;
	}
	private Date tgl;
	
	public Date getTgl() {
		return tgl;
	}
	public void setTgl(Date tgl) {
		this.tgl = tgl;
	}
	public String getMsv_id() {
		return msv_id;
	}
	public void setMsv_id(String msv_id) {
		this.msv_id = msv_id;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Integer getUser_create() {
		return user_create;
	}
	public void setUser_create(Integer user_create) {
		this.user_create = user_create;
	}
	public Integer getJenis_va() {
		return jenis_va;
	}
	public void setJenis_va(Integer jenis_va) {
		this.jenis_va = jenis_va;
	}
	public Integer getJenis_syariah() {
		return jenis_syariah;
	}
	public void setJenis_syariah(Integer jenis_syariah) {
		this.jenis_syariah = jenis_syariah;
	}
	public Integer getJenis_link() {
		return jenis_link;
	}
	public void setJenis_link(Integer jenis_link) {
		this.jenis_link = jenis_link;
	}
	public String getMsv_amount_req() {
		return msv_amount_req;
	}
	public void setMsv_amount_req(String msv_amount_req) {
		this.msv_amount_req = msv_amount_req;
	}
	public String getStart_no_va_req() {
		return start_no_va_req;
	}
	public void setStart_no_va_req(String start_no_va_req) {
		this.start_no_va_req = start_no_va_req;
	}
	public String getEnd_no_va_req() {
		return end_no_va_req;
	}
	public void setEnd_no_va_req(String end_no_va_req) {
		this.end_no_va_req = end_no_va_req;
	}
	public String getStart_no_va_cetak() {
		return start_no_va_cetak;
	}
	public void setStart_no_va_cetak(String start_no_va_cetak) {
		this.start_no_va_cetak = start_no_va_cetak;
	}
	public String getEnd_no_va_cetak() {
		return end_no_va_cetak;
	}
	public void setEnd_no_va_cetak(String end_no_va_cetak) {
		this.end_no_va_cetak = end_no_va_cetak;
	}
	public String getNo_va() {
		return no_va;
	}
	public void setNo_va(String no_va) {
		this.no_va = no_va;
	}
	public String getSpaj_temp() {
		return spaj_temp;
	}
	public void setSpaj_temp(String spaj_temp) {
		this.spaj_temp = spaj_temp;
	}
	public String getSpaj() {
		return spaj;
	}
	public void setSpaj(String spaj) {
		this.spaj = spaj;
	}
	public Integer getFlag_active() {
		return flag_active;
	}
	public void setFlag_active(Integer flag_active) {
		this.flag_active = flag_active;
	}
	public Date getInput_date() {
		return input_date;
	}
	public void setInput_date(Date input_date) {
		this.input_date = input_date;
	}
	public Integer getUser_input() {
		return user_input;
	}
	public void setUser_input(Integer user_input) {
		this.user_input = user_input;
	}
	public List<DropDown> getjBank() {
		return jBank;
	}
	public void setjBank(List<DropDown> jBank) {
		this.jBank = jBank;
	}
	public String getJ_bank() {
		return j_bank;
	}
	public void setJ_bank(String j_bank) {
		this.j_bank = j_bank;
	}
	public String getLsbp_id() {
		return lsbp_id;
	}
	public void setLsbp_id(String lsbp_id) {
		this.lsbp_id = lsbp_id;
	}

}
