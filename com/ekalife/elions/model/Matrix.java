package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.List;

public class Matrix implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mns_kd_nasabah;                 
	private Integer lsbs_id;                       
	private Integer msma_fund;                     
	private Integer msma_studi_c;                  
	private Integer msma_studi_b;                  
	private Integer msma_studi_a;                  
	private Integer msma_pensiun_c;                
	private Integer msma_pensiun_b;                
	private Integer msma_pensiun_a;                
	private Integer msma_proteksi_c;               
	private Integer msma_proteksi_b;               
	private Integer msma_proteksi_a;               
	private Integer msma_saving_c;                 
	private Integer msma_saving_b;                 
	private Integer msma_saving_a;
	private String lsbs_name;
	private List<Matrix> listMatrix;
	
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
	public Integer getMsma_fund() {
		return msma_fund;
	}
	public void setMsma_fund(Integer msma_fund) {
		this.msma_fund = msma_fund;
	}
	public Integer getMsma_pensiun_a() {
		return msma_pensiun_a;
	}
	public void setMsma_pensiun_a(Integer msma_pensiun_a) {
		this.msma_pensiun_a = msma_pensiun_a;
	}
	public Integer getMsma_pensiun_b() {
		return msma_pensiun_b;
	}
	public void setMsma_pensiun_b(Integer msma_pensiun_b) {
		this.msma_pensiun_b = msma_pensiun_b;
	}
	public Integer getMsma_pensiun_c() {
		return msma_pensiun_c;
	}
	public void setMsma_pensiun_c(Integer msma_pensiun_c) {
		this.msma_pensiun_c = msma_pensiun_c;
	}
	public Integer getMsma_proteksi_a() {
		return msma_proteksi_a;
	}
	public void setMsma_proteksi_a(Integer msma_proteksi_a) {
		this.msma_proteksi_a = msma_proteksi_a;
	}
	public Integer getMsma_proteksi_b() {
		return msma_proteksi_b;
	}
	public void setMsma_proteksi_b(Integer msma_proteksi_b) {
		this.msma_proteksi_b = msma_proteksi_b;
	}
	public Integer getMsma_proteksi_c() {
		return msma_proteksi_c;
	}
	public void setMsma_proteksi_c(Integer msma_proteksi_c) {
		this.msma_proteksi_c = msma_proteksi_c;
	}
	public Integer getMsma_saving_a() {
		return msma_saving_a;
	}
	public void setMsma_saving_a(Integer msma_saving_a) {
		this.msma_saving_a = msma_saving_a;
	}
	public Integer getMsma_saving_b() {
		return msma_saving_b;
	}
	public void setMsma_saving_b(Integer msma_saving_b) {
		this.msma_saving_b = msma_saving_b;
	}
	public Integer getMsma_saving_c() {
		return msma_saving_c;
	}
	public void setMsma_saving_c(Integer msma_saving_c) {
		this.msma_saving_c = msma_saving_c;
	}
	public Integer getMsma_studi_a() {
		return msma_studi_a;
	}
	public void setMsma_studi_a(Integer msma_studi_a) {
		this.msma_studi_a = msma_studi_a;
	}
	public Integer getMsma_studi_b() {
		return msma_studi_b;
	}
	public void setMsma_studi_b(Integer msma_studi_b) {
		this.msma_studi_b = msma_studi_b;
	}
	public Integer getMsma_studi_c() {
		return msma_studi_c;
	}
	public void setMsma_studi_c(Integer msma_studi_c) {
		this.msma_studi_c = msma_studi_c;
	}
	public List<Matrix> getListMatrix() {
		return listMatrix;
	}
	public void setListMatrix(List<Matrix> listMatrix) {
		this.listMatrix = listMatrix;
	}
	public String getLsbs_name() {
		return lsbs_name;
	}
	public void setLsbs_name(String lsbs_name) {
		this.lsbs_name = lsbs_name;
	}
	
	
}