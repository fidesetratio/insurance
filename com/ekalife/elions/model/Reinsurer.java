package com.ekalife.elions.model;

import java.io.Serializable;

public class Reinsurer implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer lsrei_id;                      
	private String lsre_nama;                      
	private String lsre_alamat;                    
	private String lsre_telpon;                    
	private String lsre_fax;                       
	private String lsre_contact;                   
	private String lsre_account_gl;                
	private String lsre_email;
	private Integer type_reinsurer;
	
	public Integer getLsrei_id() {
		return lsrei_id;
	}
	public void setLsrei_id(Integer lsreiId) {
		lsrei_id = lsreiId;
	}
	public String getLsre_nama() {
		return lsre_nama;
	}
	public void setLsre_nama(String lsreNama) {
		lsre_nama = lsreNama;
	}
	public String getLsre_alamat() {
		return lsre_alamat;
	}
	public void setLsre_alamat(String lsreAlamat) {
		lsre_alamat = lsreAlamat;
	}
	public String getLsre_telpon() {
		return lsre_telpon;
	}
	public void setLsre_telpon(String lsreTelpon) {
		lsre_telpon = lsreTelpon;
	}
	public String getLsre_fax() {
		return lsre_fax;
	}
	public void setLsre_fax(String lsreFax) {
		lsre_fax = lsreFax;
	}
	public String getLsre_contact() {
		return lsre_contact;
	}
	public void setLsre_contact(String lsreContact) {
		lsre_contact = lsreContact;
	}
	public String getLsre_account_gl() {
		return lsre_account_gl;
	}
	public void setLsre_account_gl(String lsreAccountGl) {
		lsre_account_gl = lsreAccountGl;
	}
	public String getLsre_email() {
		return lsre_email;
	}
	public void setLsre_email(String lsreEmail) {
		lsre_email = lsreEmail;
	}
	public Integer getType_reinsurer() {
		return type_reinsurer;
	}
	public void setType_reinsurer(Integer typeReinsurer) {
		type_reinsurer = typeReinsurer;
	}  
	
}
