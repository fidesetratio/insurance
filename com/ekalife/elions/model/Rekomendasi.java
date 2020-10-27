package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.List;

public class Rekomendasi implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mns_kd_nasabah;                 
	private Integer msrek_no;                      
	private Integer lsbs_id;                       
	private String msrek_alasan;                   
	private Integer msrek_prod_id;
	private List<Rekomendasi> listRekomendasi;
	
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public String getMns_kd_nasabah() {
		return mns_kd_nasabah;
	}
	public void setMns_kd_nasabah(String mns_kd_nasabah) {
		this.mns_kd_nasabah = mns_kd_nasabah;
	}
	public String getMsrek_alasan() {
		return msrek_alasan;
	}
	public void setMsrek_alasan(String msrek_alasan) {
		this.msrek_alasan = msrek_alasan;
	}
	public Integer getMsrek_no() {
		return msrek_no;
	}
	public void setMsrek_no(Integer msrek_no) {
		this.msrek_no = msrek_no;
	}
	public Integer getMsrek_prod_id() {
		return msrek_prod_id;
	}
	public void setMsrek_prod_id(Integer msrek_prod_id) {
		this.msrek_prod_id = msrek_prod_id;
	}
	public List<Rekomendasi> getListRekomendasi() {
		return listRekomendasi;
	}
	public void setListRekomendasi(List<Rekomendasi> listRekomendasi) {
		this.listRekomendasi = listRekomendasi;
	}   
	
	
}