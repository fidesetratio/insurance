package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.List;

public class CommandPowersaveCair implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2172955661380132336L;
	public String reg_spaj;
	public Integer lsbs_id;
	public PowersaveCair powersaveCair;
	public List<PowersaveCair> daftarPremi;
	public List< DaftarSpajOtorisasi> daftarSpaj;
	public List<PowersaveCair> daftarPremiSusulan;
	public String valid1;
	public String valid2;
	public Integer flag_update_sph;
	public String kategori_company;
	
	public String getKategori_company() {
		return kategori_company;
	}
	public void setKategori_company(String kategori_company) {
		this.kategori_company = kategori_company;
	}
	public Integer getFlag_update_sph() {
		return flag_update_sph;
	}
	public void setFlag_update_sph(Integer flag_update_sph) {
		this.flag_update_sph = flag_update_sph;
	}
	public List<PowersaveCair> getDaftarPremi() {
		return daftarPremi;
	}
	public void setDaftarPremi(List<PowersaveCair> daftarPremi) {
		this.daftarPremi = daftarPremi;
	}
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public String getValid1() {
		return valid1;
	}
	public void setValid1(String valid1) {
		this.valid1 = valid1;
	}
	public String getValid2() {
		return valid2;
	}
	public void setValid2(String valid2) {
		this.valid2 = valid2;
	}
	public PowersaveCair getPowersaveCair() {
		return powersaveCair;
	}
	public void setPowersaveCair(PowersaveCair powersaveCair) {
		this.powersaveCair = powersaveCair;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public List<PowersaveCair> getDaftarPremiSusulan() {
		return daftarPremiSusulan;
	}
	public void setDaftarPremiSusulan(List<PowersaveCair> daftarPremiSusulan) {
		this.daftarPremiSusulan = daftarPremiSusulan;
	}
	public List<DaftarSpajOtorisasi> getDaftarSpaj() {
		return daftarSpaj;
	}
	public void setDaftarSpaj(List<DaftarSpajOtorisasi> daftarSpaj) {
		this.daftarSpaj = daftarSpaj;
	}                       	
	
}