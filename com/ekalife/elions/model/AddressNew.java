package com.ekalife.elions.model;

import java.io.Serializable;

public class AddressNew implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mcl_id;
	private String mcl_id_new;
	private String alamat_rumah;
	private String kd_pos_rumah;
	private String kota_rumah;
	private String area_code_rumah;
	private String telpon_rumah;
	private String alamat_kantor;
	private String kd_pos_kantor;
	private String kota_kantor;
	private String area_code_kantor;
	private String telpon_kantor;
	private String email;
	private String no_hp;
	private String no_hp2;
	private String no_fax;
	private String area_code_rumah2;
	private String telpon_rumah2;
	private String area_code_kantor2;
	private String telpon_kantor2;
	private String area_code_fax;
	
	public AddressNew() {
	}
	
	public AddressNew(String mcl_id) {
		this.mcl_id = mcl_id;
	}
	
	public String getAlamat_kantor() {
		return alamat_kantor;
	}
	public void setAlamat_kantor(String alamat_kantor) {
		this.alamat_kantor = alamat_kantor;
	}
	public String getAlamat_rumah() {
		return alamat_rumah;
	}
	public void setAlamat_rumah(String alamat_rumah) {
		this.alamat_rumah = alamat_rumah;
	}
	public String getArea_code_fax() {
		return area_code_fax;
	}
	public void setArea_code_fax(String area_code_fax) {
		this.area_code_fax = area_code_fax;
	}
	public String getArea_code_kantor() {
		return area_code_kantor;
	}
	public void setArea_code_kantor(String area_code_kantor) {
		this.area_code_kantor = area_code_kantor;
	}
	public String getArea_code_kantor2() {
		return area_code_kantor2;
	}
	public void setArea_code_kantor2(String area_code_kantor2) {
		this.area_code_kantor2 = area_code_kantor2;
	}
	public String getArea_code_rumah() {
		return area_code_rumah;
	}
	public void setArea_code_rumah(String area_code_rumah) {
		this.area_code_rumah = area_code_rumah;
	}
	public String getArea_code_rumah2() {
		return area_code_rumah2;
	}
	public void setArea_code_rumah2(String area_code_rumah2) {
		this.area_code_rumah2 = area_code_rumah2;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getKd_pos_kantor() {
		return kd_pos_kantor;
	}
	public void setKd_pos_kantor(String kd_pos_kantor) {
		this.kd_pos_kantor = kd_pos_kantor;
	}
	public String getKd_pos_rumah() {
		return kd_pos_rumah;
	}
	public void setKd_pos_rumah(String kd_pos_rumah) {
		this.kd_pos_rumah = kd_pos_rumah;
	}
	public String getKota_kantor() {
		return kota_kantor;
	}
	public void setKota_kantor(String kota_kantor) {
		this.kota_kantor = kota_kantor;
	}
	public String getKota_rumah() {
		return kota_rumah;
	}
	public void setKota_rumah(String kota_rumah) {
		this.kota_rumah = kota_rumah;
	}
	public String getMcl_id() {
		return mcl_id;
	}
	public void setMcl_id(String mcl_id) {
		this.mcl_id = mcl_id;
	}
	public String getNo_fax() {
		return no_fax;
	}
	public void setNo_fax(String no_fax) {
		this.no_fax = no_fax;
	}
	public String getNo_hp() {
		return no_hp;
	}
	public void setNo_hp(String no_hp) {
		this.no_hp = no_hp;
	}
	public String getNo_hp2() {
		return no_hp2;
	}
	public void setNo_hp2(String no_hp2) {
		this.no_hp2 = no_hp2;
	}
	public String getTelpon_kantor() {
		return telpon_kantor;
	}
	public void setTelpon_kantor(String telpon_kantor) {
		this.telpon_kantor = telpon_kantor;
	}
	public String getTelpon_kantor2() {
		return telpon_kantor2;
	}
	public void setTelpon_kantor2(String telpon_kantor2) {
		this.telpon_kantor2 = telpon_kantor2;
	}
	public String getTelpon_rumah() {
		return telpon_rumah;
	}
	public void setTelpon_rumah(String telpon_rumah) {
		this.telpon_rumah = telpon_rumah;
	}
	public String getTelpon_rumah2() {
		return telpon_rumah2;
	}
	public void setTelpon_rumah2(String telpon_rumah2) {
		this.telpon_rumah2 = telpon_rumah2;
	}
	public String getMcl_id_new() {
		return mcl_id_new;
	}
	public void setMcl_id_new(String mcl_id_new) {
		this.mcl_id_new = mcl_id_new;
	}
	
}
