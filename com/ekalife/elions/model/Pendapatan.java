package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.List;

public class Pendapatan implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mns_kd_nasabah;                 
	private Integer lsp_id;                        
	private Integer lsp_in_out;                    
	private Integer mpp_value;                     
	private String mpp_value_alasan; 
	private List<Pendapatan> listPendapatan;
	private String lsp_ket;
	
	public String getLsp_ket() {
		return lsp_ket;
	}
	public void setLsp_ket(String lsp_ket) {
		this.lsp_ket = lsp_ket;
	}
	public List<Pendapatan> getListPendapatan() {
		return listPendapatan;
	}
	public void setListPendapatan(List<Pendapatan> listPendapatan) {
		this.listPendapatan = listPendapatan;
	}
	public Integer getLsp_id() {
		return lsp_id;
	}
	public void setLsp_id(Integer lsp_id) {
		this.lsp_id = lsp_id;
	}
	public Integer getLsp_in_out() {
		return lsp_in_out;
	}
	public void setLsp_in_out(Integer lsp_in_out) {
		this.lsp_in_out = lsp_in_out;
	}
	public String getMns_kd_nasabah() {
		return mns_kd_nasabah;
	}
	public void setMns_kd_nasabah(String mns_kd_nasabah) {
		this.mns_kd_nasabah = mns_kd_nasabah;
	}
	public Integer getMpp_value() {
		return mpp_value;
	}
	public void setMpp_value(Integer mpp_value) {
		this.mpp_value = mpp_value;
	}
	public String getMpp_value_alasan() {
		return mpp_value_alasan;
	}
	public void setMpp_value_alasan(String mpp_value_alasan) {
		this.mpp_value_alasan = mpp_value_alasan;
	}
	
}