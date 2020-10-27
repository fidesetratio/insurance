package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Benefeciary implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private String reg_spaj;                       
	private Integer mste_insured_no;               
	private Integer msaw_number;                   
	private String msaw_first;                     
	private String msaw_middle;                    
	private String msaw_last;                      
	private Date msaw_birth;
	private String smsaw_birth;
	private Integer lsre_id;                       
	private Double msaw_persen;                    
	private String lsre_relation;
	private Integer msaw_sex;
	private String msaw_sex_detail;
	private Integer lsne_id;
	private String lsne_negara;
	private String msaw_ket;
	private String nama;
	
	
	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getMsaw_ket() {
		return msaw_ket;
	}

	public void setMsaw_ket(String msaw_ket) {
		this.msaw_ket = msaw_ket;
	}

	public String getLsne_negara() {
		return lsne_negara;
	}

	public void setLsne_negara(String lsne_negara) {
		this.lsne_negara = lsne_negara;
	}

	public Integer getLsne_id() {
		return lsne_id;
	}

	public void setLsne_id(Integer lsne_id) {
		this.lsne_id = lsne_id;
	}

	public String getMsaw_sex_detail() {
		return msaw_sex_detail;
	}

	public void setMsaw_sex_detail(String msaw_sex_detail) {
		this.msaw_sex_detail = msaw_sex_detail;
	}

	public Integer getMsaw_sex() {
		return msaw_sex;
	}

	public void setMsaw_sex(Integer msawSex) {
		msaw_sex = msawSex;
	}

	public Benefeciary() {
	}
	
	public Benefeciary(String reg_spaj, int mste_insured_no, int msaw_number) {
		this.reg_spaj = reg_spaj;
		this.mste_insured_no = mste_insured_no;
		this.msaw_number = msaw_number;
	}
	
	public Integer getLsre_id() {
		return lsre_id;
	}
	public void setLsre_id(Integer lsre_id) {
		this.lsre_id = lsre_id;
	}
	public String getLsre_relation() {
		return lsre_relation;
	}
	public void setLsre_relation(String lsre_relation) {
		this.lsre_relation = lsre_relation;
	}
	public Date getMsaw_birth() {
		return msaw_birth;
	}
	public void setMsaw_birth(Date msaw_birth) {
		this.msaw_birth = msaw_birth;
	}
	public String getMsaw_first() {
		return msaw_first;
	}
	public void setMsaw_first(String msaw_first) {
		this.msaw_first = msaw_first;
	}
	public String getMsaw_last() {
		return msaw_last;
	}
	public void setMsaw_last(String msaw_last) {
		this.msaw_last = msaw_last;
	}
	public String getMsaw_middle() {
		return msaw_middle;
	}
	public void setMsaw_middle(String msaw_middle) {
		this.msaw_middle = msaw_middle;
	}
	public Integer getMsaw_number() {
		return msaw_number;
	}
	public void setMsaw_number(Integer msaw_number) {
		this.msaw_number = msaw_number;
	}
	public Double getMsaw_persen() {
		return msaw_persen;
	}
	public void setMsaw_persen(Double msaw_persen) {
		this.msaw_persen = msaw_persen;
	}
	public Integer getMste_insured_no() {
		return mste_insured_no;
	}
	public void setMste_insured_no(Integer mste_insured_no) {
		this.mste_insured_no = mste_insured_no;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getSmsaw_birth() {
		return smsaw_birth;
	}
	public void setSmsaw_birth(String smsaw_birth) {
		this.smsaw_birth = smsaw_birth;
	}
	
}
