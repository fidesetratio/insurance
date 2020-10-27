package com.ekalife.elions.model;

import java.io.Serializable;

public class BVoucher implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String no_pre;

	public Integer no_jurnal;

	public String keterangan;

	public Double debet;

	public Double kredit;

	public String project_no;

	public String budget_no;

	public Integer lus_id_del;

	public Integer type_trx;

	public String key_jurnal;

	public String getBudget_no() {
		return budget_no;
	}

	public void setBudget_no(String budget_no) {
		this.budget_no = budget_no;
	}

	public Double getDebet() {
		return debet;
	}

	public void setDebet(Double debet) {
		this.debet = debet;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public String getKey_jurnal() {
		return key_jurnal;
	}

	public void setKey_jurnal(String key_jurnal) {
		this.key_jurnal = key_jurnal;
	}

	public Double getKredit() {
		return kredit;
	}

	public void setKredit(Double kredit) {
		this.kredit = kredit;
	}

	public Integer getLus_id_del() {
		return lus_id_del;
	}

	public void setLus_id_del(Integer lus_id_del) {
		this.lus_id_del = lus_id_del;
	}

	public Integer getNo_jurnal() {
		return no_jurnal;
	}

	public void setNo_jurnal(Integer no_jurnal) {
		this.no_jurnal = no_jurnal;
	}

	public String getNo_pre() {
		return no_pre;
	}

	public void setNo_pre(String no_pre) {
		this.no_pre = no_pre;
	}

	public String getProject_no() {
		return project_no;
	}

	public void setProject_no(String project_no) {
		this.project_no = project_no;
	}

	public Integer getType_trx() {
		return type_trx;
	}

	public void setType_trx(Integer type_trx) {
		this.type_trx = type_trx;
	}

}