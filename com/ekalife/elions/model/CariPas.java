package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class CariPas implements Serializable {

	private static final long serialVersionUID = -3260973454932103502L;
	private Pas pas;
	private String msp_id;
	private String msp_full_name;
	private String msp_identity_no;
	private Date msp_date_of_birth;
	private String msp_date_of_birth2;
	private String msp_address_1;
	private String msp_city;
	private String msp_postal_code;
	private String reg_spaj;
	private String msp_fire_id;
	
	public Pas getPas() {
		return pas;
	}
	public void setPas(Pas pas) {
		this.pas = pas;
	}
	public String getMsp_id() {
		return msp_id;
	}
	public void setMsp_id(String mspId) {
		msp_id = mspId;
	}
	public String getMsp_full_name() {
		return msp_full_name;
	}
	public void setMsp_full_name(String mspFullName) {
		msp_full_name = mspFullName;
	}
	public String getMsp_identity_no() {
		return msp_identity_no;
	}
	public void setMsp_identity_no(String mspIdentityNo) {
		msp_identity_no = mspIdentityNo;
	}
	public Date getMsp_date_of_birth() {
		return msp_date_of_birth;
	}
	public void setMsp_date_of_birth(Date mspDateOfBirth) {
		msp_date_of_birth = mspDateOfBirth;
	}
	public String getMsp_date_of_birth2() {
		return msp_date_of_birth2;
	}
	public void setMsp_date_of_birth2(String mspDateOfBirth2) {
		msp_date_of_birth2 = mspDateOfBirth2;
	}
	public String getMsp_address_1() {
		return msp_address_1;
	}
	public void setMsp_address_1(String mspAddress_1) {
		msp_address_1 = mspAddress_1;
	}
	public String getMsp_city() {
		return msp_city;
	}
	public void setMsp_city(String mspCity) {
		msp_city = mspCity;
	}
	public String getMsp_postal_code() {
		return msp_postal_code;
	}
	public void setMsp_postal_code(String mspPostalCode) {
		msp_postal_code = mspPostalCode;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String regSpaj) {
		reg_spaj = regSpaj;
	}
	public String getMsp_fire_id() {
		return msp_fire_id;
	}
	public void setMsp_fire_id(String mspFireId) {
		msp_fire_id = mspFireId;
	}
	
}
