package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.List;

public class Aspirasi implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mns_kd_nasabah;                 
	private Integer msas_no;                       
	private String msas_aspirasi;
	private List<Aspirasi> listAspirasi;
	
	public List<Aspirasi> getListAspirasi() {
		return listAspirasi;
	}
	public void setListAspirasi(List<Aspirasi> listAspirasi) {
		this.listAspirasi = listAspirasi;
	}
	public String getMns_kd_nasabah() {
		return mns_kd_nasabah;
	}
	public void setMns_kd_nasabah(String mns_kd_nasabah) {
		this.mns_kd_nasabah = mns_kd_nasabah;
	}
	public String getMsas_aspirasi() {
		return msas_aspirasi;
	}
	public void setMsas_aspirasi(String msas_aspirasi) {
		this.msas_aspirasi = msas_aspirasi;
	}
	public Integer getMsas_no() {
		return msas_no;
	}
	public void setMsas_no(Integer msas_no) {
		this.msas_no = msas_no;
	} 
	
}