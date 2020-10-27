package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CmdPromo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	private String pesan;
	private ArrayList<Promo> listSpaj;
	private Integer  jum_spaj_kw7_all;
	private Integer  jum_spaj_kw7_approve;
	private Integer  jum_spaj_kw7_reject;
	private Integer  jum_spaj_kw7_waiting;
	private Integer  jum_spaj_kw11_all;
	private Integer  jum_spaj_kw11_approve;
	private Integer  jum_spaj_kw11_reject;
	private Integer  jum_spaj_kw11_waiting;
		
	public ArrayList<Promo> getListSpaj() {
		return listSpaj;
	}
	public void setListSpaj(ArrayList<Promo> listSpaj) {
		this.listSpaj = listSpaj;
	}
	public String getPesan() {
		return pesan;
	}
	public void setPesan(String pesan) {
		this.pesan = pesan;
	}
	public Integer getJum_spaj_kw7_all() {
		return jum_spaj_kw7_all;
	}
	public void setJum_spaj_kw7_all(Integer jum_spaj_kw7_all) {
		this.jum_spaj_kw7_all = jum_spaj_kw7_all;
	}
	public Integer getJum_spaj_kw7_approve() {
		return jum_spaj_kw7_approve;
	}
	public void setJum_spaj_kw7_approve(Integer jum_spaj_kw7_approve) {
		this.jum_spaj_kw7_approve = jum_spaj_kw7_approve;
	}
	public Integer getJum_spaj_kw7_reject() {
		return jum_spaj_kw7_reject;
	}
	public void setJum_spaj_kw7_reject(Integer jum_spaj_kw7_reject) {
		this.jum_spaj_kw7_reject = jum_spaj_kw7_reject;
	}
	public Integer getJum_spaj_kw7_waiting() {
		return jum_spaj_kw7_waiting;
	}
	public void setJum_spaj_kw7_waiting(Integer jum_spaj_kw7_waiting) {
		this.jum_spaj_kw7_waiting = jum_spaj_kw7_waiting;
	}
	public Integer getJum_spaj_kw11_all() {
		return jum_spaj_kw11_all;
	}
	public void setJum_spaj_kw11_all(Integer jum_spaj_kw11_all) {
		this.jum_spaj_kw11_all = jum_spaj_kw11_all;
	}
	public Integer getJum_spaj_kw11_approve() {
		return jum_spaj_kw11_approve;
	}
	public void setJum_spaj_kw11_approve(Integer jum_spaj_kw11_approve) {
		this.jum_spaj_kw11_approve = jum_spaj_kw11_approve;
	}
	public Integer getJum_spaj_kw11_reject() {
		return jum_spaj_kw11_reject;
	}
	public void setJum_spaj_kw11_reject(Integer jum_spaj_kw11_reject) {
		this.jum_spaj_kw11_reject = jum_spaj_kw11_reject;
	}
	public Integer getJum_spaj_kw11_waiting() {
		return jum_spaj_kw11_waiting;
	}
	public void setJum_spaj_kw11_waiting(Integer jum_spaj_kw11_waiting) {
		this.jum_spaj_kw11_waiting = jum_spaj_kw11_waiting;
	}
}
