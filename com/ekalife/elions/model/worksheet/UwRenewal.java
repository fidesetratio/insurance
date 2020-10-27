package com.ekalife.elions.model.worksheet;

import java.io.Serializable;
import java.util.List;

import com.ekalife.elions.model.reinstate.Reinstatement;

public class UwRenewal implements Serializable {

	private static final long serialVersionUID = 1L;

	private String reg_spaj;
	private String area1;
	private String area2;
	private String area3;
	private String area4;
	private String area5;
	private String area6;
	private String area7;
	private String crbyr;
	private String prod1;
	private String lsbs;
	private String lsdbs;
	private String jns;
	private List aksep;
	private String lus_id;
	private String lus_full_name;
	private String lus_login;
	private String lus_dept;
	private String nomorsrt;
	private Double premi1;
	private Double premi3;
	private Double premi2;
	

	private List<String> lsRenewal;
	private List<String> lsDataUsulan1;
	private List<String> lsDataUsulan2;
	
	public List<String> getLsRenewal() {
		return lsRenewal;
	}
	public void setLsRenewal(List<String> lsRenewal) {
		this.lsRenewal = lsRenewal;
	}
	
	public List<String> getLsDataUsulan1() {
		return lsDataUsulan1;
	}
	public void setLsDataUsulan1(List<String> lsDataUsulan1) {
		this.lsDataUsulan1 = lsDataUsulan1;
	}
	
	public List<String> getLsDataUsulan2() {
		return lsDataUsulan2;
	}
	public void setLsDataUsulan2(List<String> lsDataUsulan2) {
		this.lsDataUsulan2 = lsDataUsulan2;
	}
	
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getArea1() {
		return area1;
	}
	public void setArea1(String area1) {
		this.area1 = area1;
	}
	public String getArea2() {
		return area2;
	}
	public void setArea2(String area2) {
		this.area2 = area2;
	}
	public String getArea3() {
		return area3;
	}
	public void setArea3(String area3) {
		this.area3 = area3;
	}
	public String getArea4() {
		return area4;
	}
	public void setArea4(String area4) {
		this.area4 = area4;
	}
	public String getArea5() {
		return area5;
	}
	public void setArea5(String area5) {
		this.area5 = area5;
	}
	public List getAksep() {
		return aksep;
	}
	public void setAksep(List aksep) {
		this.aksep = aksep;
	}
	public String getLus_id() {
		return lus_id;
	}
	public void setLus_id(String lus_id) {
		this.lus_id = lus_id;
	}
	public String getLus_full_name() {
		return lus_full_name;
	}
	public void setLus_full_name(String lus_full_name) {
		this.lus_full_name = lus_full_name;
	}
	public String getLus_login() {
		return lus_login;
	}
	public void setLus_login(String lus_login) {
		this.lus_login = lus_login;
	}
	public String getLus_dept() {
		return lus_dept;
	}
	public void setLus_dept(String lus_dept) {
		this.lus_dept = lus_dept;
	}
	public String getProd1() {
		return prod1;
	}
	public void setProd1(String prod1) {
		this.prod1 = prod1;
	}
	public String getJns() {
		return jns;
	}
	public void setJns(String jns) {
		this.jns = jns;
	}
	public String getArea6() {
		return area6;
	}
	public void setArea6(String area6) {
		this.area6 = area6;
	}
	public String getLsdbs() {
		return lsdbs;
	}
	public void setLsdbs(String lsdbs) {
		this.lsdbs = lsdbs;
	}
	public String getLsbs() {
		return lsbs;
	}
	public void setLsbs(String lsbs) {
		this.lsbs = lsbs;
	}
	public Double getPremi1() {
		return premi1;
	}
	public void setPremi1(Double premi1) {
		this.premi1 = premi1;
	}
	public Double getPremi3() {
		return premi3;
	}
	public void setPremi3(Double premi3) {
		this.premi3 = premi3;
	}
	public Double getPremi2() {
		return premi2;
	}
	public void setPremi2(Double premi2) {
		this.premi2 = premi2;
	}
	public String getCrbyr() {
		return crbyr;
	}
	public void setCrbyr(String crbyr) {
		this.crbyr = crbyr;
	}
	public String getArea7() {
		return area7;
	}
	public void setArea7(String area7) {
		this.area7 = area7;
	}
	public String getNomorsrt() {
		return nomorsrt;
	}
	public void setNomorsrt(String nomorsrt) {
		this.nomorsrt = nomorsrt;
	}

}
