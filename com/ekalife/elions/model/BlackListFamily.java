package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class BlackListFamily implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer lblf_id;
	private Integer lbl_id;
	private Integer lsre_id;
	private String lblf_nama;
	private Date lblf_tgl_lahir;
	private String lblf_nama_si;
	private Date lblf_tgllhr_si;
	private String lblf_tgllhr_sis;
	private String lblf_nama_anak1;
	private Date lblf_tgllhr_anak1;
	private String lblf_tgllhr_anak1s;
	private String lblf_nama_anak2;
	private Date lblf_tgllhr_anak2;
	private String lblf_tgllhr_anak2s;
	private String lblf_nama_anak3;
	private Date lblf_tgllhr_anak3;
	private String lblf_tgllhr_anak3s;
	private Integer lus_id;
	private String lus_login_name;
	private Date lblf_tgl_input;
	
	public BlackListFamily(){
	}
	
	public BlackListFamily(Integer lblf_id, Integer lbl_id, Integer lsre_id, String lblf_nama, Date lblf_tgl_lahir, Integer lus_id, Date lblf_tgl_input) {
		super();
		this.lblf_id = lblf_id;
		this.lbl_id = lbl_id;
		this.lsre_id = lsre_id;
		this.lblf_nama = lblf_nama;
		this.lblf_tgl_lahir = lblf_tgl_lahir;
		this.lus_id = lus_id;
		this.lblf_tgl_input = lblf_tgl_input;
	}
	public Date getLblf_tgl_input() {
		return lblf_tgl_input;
	}
	public void setLblf_tgl_input(Date lblf_tgl_input) {
		this.lblf_tgl_input = lblf_tgl_input;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getLus_login_name() {
		return lus_login_name;
	}
	public void setLus_login_name(String lus_login_name) {
		this.lus_login_name = lus_login_name;
	}
	public Integer getLblf_id() {
		return lblf_id;
	}
	public void setLblf_id(Integer lblf_id) {
		this.lblf_id = lblf_id;
	}
	public String getLblf_nama_si() {
		return lblf_nama_si;
	}
	public void setLblf_nama_si(String lblf_nama_si) {
		this.lblf_nama_si = lblf_nama_si;
	}
	public Date getLblf_tgllhr_si() {
		return lblf_tgllhr_si;
	}
	public void setLblf_tgllhr_si(Date lblf_tgllhr_si) {
		this.lblf_tgllhr_si = lblf_tgllhr_si;
	}
	public Integer getLsre_id() {
		return lsre_id;
	}
	public void setLsre_id(Integer lsre_id) {
		this.lsre_id = lsre_id;
	}
	public Integer getLbl_id() {
		return lbl_id;
	}
	public void setLbl_id(Integer lbl_id) {
		this.lbl_id = lbl_id;
	}
	public String getLblf_nama_anak1() {
		return lblf_nama_anak1;
	}
	public void setLblf_nama_anak1(String lblf_nama_anak1) {
		this.lblf_nama_anak1 = lblf_nama_anak1;
	}
	public String getLblf_nama_anak2() {
		return lblf_nama_anak2;
	}
	public void setLblf_nama_anak2(String lblf_nama_anak2) {
		this.lblf_nama_anak2 = lblf_nama_anak2;
	}
	public String getLblf_nama_anak3() {
		return lblf_nama_anak3;
	}
	public void setLblf_nama_anak3(String lblf_nama_anak3) {
		this.lblf_nama_anak3 = lblf_nama_anak3;
	}
	public Date getLblf_tgllhr_anak1() {
		return lblf_tgllhr_anak1;
	}
	public void setLblf_tgllhr_anak1(Date lblf_tgllhr_anak1) {
		this.lblf_tgllhr_anak1 = lblf_tgllhr_anak1;
	}
	public Date getLblf_tgllhr_anak2() {
		return lblf_tgllhr_anak2;
	}
	public void setLblf_tgllhr_anak2(Date lblf_tgllhr_anak2) {
		this.lblf_tgllhr_anak2 = lblf_tgllhr_anak2;
	}
	public Date getLblf_tgllhr_anak3() {
		return lblf_tgllhr_anak3;
	}
	public void setLblf_tgllhr_anak3(Date lblf_tgllhr_anak3) {
		this.lblf_tgllhr_anak3 = lblf_tgllhr_anak3;
	}
	public String getLblf_nama() {
		return lblf_nama;
	}
	public void setLblf_nama(String lblf_nama) {
		this.lblf_nama = lblf_nama;
	}
	public Date getLblf_tgl_lahir() {
		return lblf_tgl_lahir;
	}
	public void setLblf_tgl_lahir(Date lblf_tgl_lahir) {
		this.lblf_tgl_lahir = lblf_tgl_lahir;
	}

	public String getLblf_tgllhr_anak1s() {
		return lblf_tgllhr_anak1s;
	}

	public void setLblf_tgllhr_anak1s(String lblf_tgllhr_anak1s) {
		this.lblf_tgllhr_anak1s = lblf_tgllhr_anak1s;
	}

	public String getLblf_tgllhr_anak2s() {
		return lblf_tgllhr_anak2s;
	}

	public void setLblf_tgllhr_anak2s(String lblf_tgllhr_anak2s) {
		this.lblf_tgllhr_anak2s = lblf_tgllhr_anak2s;
	}

	public String getLblf_tgllhr_anak3s() {
		return lblf_tgllhr_anak3s;
	}

	public void setLblf_tgllhr_anak3s(String lblf_tgllhr_anak3s) {
		this.lblf_tgllhr_anak3s = lblf_tgllhr_anak3s;
	}

	public String getLblf_tgllhr_sis() {
		return lblf_tgllhr_sis;
	}

	public void setLblf_tgllhr_sis(String lblf_tgllhr_sis) {
		this.lblf_tgllhr_sis = lblf_tgllhr_sis;
	}
	
}
