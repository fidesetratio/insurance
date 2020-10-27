package com.ekalife.elions.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ProdBank implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mns_kd_nasabah;                 
	private Integer lpb_id;                        
	private Integer mpb_no;                        
	private Integer mpb_status;                    
	private String mpb_nm_bank;                    
	private Integer mpb_tabungan;                  
	private String mpb_nm_tabungan;                
	private BigDecimal mpb_jml_tabungan;              
	private String mpb_ket_nm_bank;                
	private String mpb_note;  
	private List<ProdBank> listProdBank;
	private String lpb_ket;
	
	public List<ProdBank> getListProdBank() {
		return listProdBank;
	}
	public void setListProdBank(List<ProdBank> listProdBank) {
		this.listProdBank = listProdBank;
	}
	public Integer getLpb_id() {
		return lpb_id;
	}
	public void setLpb_id(Integer lpb_id) {
		this.lpb_id = lpb_id;
	}
	public String getLpb_ket() {
		return lpb_ket;
	}
	public void setLpb_ket(String lpb_ket) {
		this.lpb_ket = lpb_ket;
	}
	public String getMns_kd_nasabah() {
		return mns_kd_nasabah;
	}
	public void setMns_kd_nasabah(String mns_kd_nasabah) {
		this.mns_kd_nasabah = mns_kd_nasabah;
	}
	public BigDecimal getMpb_jml_tabungan() {
		return mpb_jml_tabungan;
	}
	public void setMpb_jml_tabungan(BigDecimal mpb_jml_tabungan) {
		this.mpb_jml_tabungan = mpb_jml_tabungan;
	}
	public String getMpb_ket_nm_bank() {
		return mpb_ket_nm_bank;
	}
	public void setMpb_ket_nm_bank(String mpb_ket_nm_bank) {
		this.mpb_ket_nm_bank = mpb_ket_nm_bank;
	}
	public String getMpb_nm_bank() {
		return mpb_nm_bank;
	}
	public void setMpb_nm_bank(String mpb_nm_bank) {
		this.mpb_nm_bank = mpb_nm_bank;
	}
	public String getMpb_nm_tabungan() {
		return mpb_nm_tabungan;
	}
	public void setMpb_nm_tabungan(String mpb_nm_tabungan) {
		this.mpb_nm_tabungan = mpb_nm_tabungan;
	}
	public Integer getMpb_no() {
		return mpb_no;
	}
	public void setMpb_no(Integer mpb_no) {
		this.mpb_no = mpb_no;
	}
	public String getMpb_note() {
		return mpb_note;
	}
	public void setMpb_note(String mpb_note) {
		this.mpb_note = mpb_note;
	}
	public Integer getMpb_status() {
		return mpb_status;
	}
	public void setMpb_status(Integer mpb_status) {
		this.mpb_status = mpb_status;
	}
	public Integer getMpb_tabungan() {
		return mpb_tabungan;
	}
	public void setMpb_tabungan(Integer mpb_tabungan) {
		this.mpb_tabungan = mpb_tabungan;
	}
	
	
	
}