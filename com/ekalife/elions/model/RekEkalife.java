package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.List;

public class RekEkalife implements Serializable {

	private static final long serialVersionUID = 1L;
	
private Integer lsrek_id;                      
private String lca_id;                         
private String lsrek_gl_no;                    
private Integer lbn_id;                        
private String lre_acc_no;                     
private String lku_id;                         
private String lsrek_symbol;                   
private Integer lsrek_no_cr;                   
private Integer lsrek_no_cd;                   
private Double lsrek_saldo;                    
private Double lsrek_saldo_akhir;              
private String lsrek_kodasi;                   
private String lsrek_gl_no_old;                
private Integer lsrek_aktif;                   
private Integer lsrek_produksi;
private List<RekEkalife> lsrek_ekalife;

public List<RekEkalife> getLsrek_ekalife() {
	return lsrek_ekalife;
}
public void setLsrek_ekalife(List<RekEkalife> lsrek_ekalife) {
	this.lsrek_ekalife = lsrek_ekalife;
}
public Integer getLbn_id() {
	return lbn_id;
}
public void setLbn_id(Integer lbn_id) {
	this.lbn_id = lbn_id;
}
public String getLca_id() {
	return lca_id;
}
public void setLca_id(String lca_id) {
	this.lca_id = lca_id;
}
public String getLku_id() {
	return lku_id;
}
public void setLku_id(String lku_id) {
	this.lku_id = lku_id;
}
public String getLre_acc_no() {
	return lre_acc_no;
}
public void setLre_acc_no(String lre_acc_no) {
	this.lre_acc_no = lre_acc_no;
}
public Integer getLsrek_aktif() {
	return lsrek_aktif;
}
public void setLsrek_aktif(Integer lsrek_aktif) {
	this.lsrek_aktif = lsrek_aktif;
}
public String getLsrek_gl_no() {
	return lsrek_gl_no;
}
public void setLsrek_gl_no(String lsrek_gl_no) {
	this.lsrek_gl_no = lsrek_gl_no;
}
public String getLsrek_gl_no_old() {
	return lsrek_gl_no_old;
}
public void setLsrek_gl_no_old(String lsrek_gl_no_old) {
	this.lsrek_gl_no_old = lsrek_gl_no_old;
}
public Integer getLsrek_id() {
	return lsrek_id;
}
public void setLsrek_id(Integer lsrek_id) {
	this.lsrek_id = lsrek_id;
}
public String getLsrek_kodasi() {
	return lsrek_kodasi;
}
public void setLsrek_kodasi(String lsrek_kodasi) {
	this.lsrek_kodasi = lsrek_kodasi;
}
public Integer getLsrek_no_cd() {
	return lsrek_no_cd;
}
public void setLsrek_no_cd(Integer lsrek_no_cd) {
	this.lsrek_no_cd = lsrek_no_cd;
}
public Integer getLsrek_no_cr() {
	return lsrek_no_cr;
}
public void setLsrek_no_cr(Integer lsrek_no_cr) {
	this.lsrek_no_cr = lsrek_no_cr;
}
public Integer getLsrek_produksi() {
	return lsrek_produksi;
}
public void setLsrek_produksi(Integer lsrek_produksi) {
	this.lsrek_produksi = lsrek_produksi;
}
public Double getLsrek_saldo() {
	return lsrek_saldo;
}
public void setLsrek_saldo(Double lsrek_saldo) {
	this.lsrek_saldo = lsrek_saldo;
}
public Double getLsrek_saldo_akhir() {
	return lsrek_saldo_akhir;
}
public void setLsrek_saldo_akhir(Double lsrek_saldo_akhir) {
	this.lsrek_saldo_akhir = lsrek_saldo_akhir;
}
public String getLsrek_symbol() {
	return lsrek_symbol;
}
public void setLsrek_symbol(String lsrek_symbol) {
	this.lsrek_symbol = lsrek_symbol;
}


}