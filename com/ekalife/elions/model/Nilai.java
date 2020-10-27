package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Nilai implements Serializable {

	private static final long serialVersionUID = -3880941045184394763L;

	private String reg_spaj;
	private String lsbs_id;
	private String lku_id;
	private Integer jenis;
	private Integer tahun;
	private Double nilai;
	private Integer lus_id;
	private Date create_date;
	private Double persen;
	
	private Double tsi;
	private Double mspr_premium;
	private Double nilai_tunai;
	private Double tahapan;
	private Double bonus;
	private Double saving;
	private Double deviden;
	private Double maturity;
	
	/**
		flag_proses :
		0 = di-insert pada saat print-polis
		1 = diproses dari data lama
		
		flag_fix :
		flag u/ meunjukkan nilai tsb sudah fix, atau sesuai pasar pada saat jt tempo,
		(misalnya multi-invest thn ke-8 : 800%, tapi tdk fix)
		0 = fix
		1 = tdk fix
	 */
	private Integer flag_proses;
	private Integer flag_fix;

	public Double getPersen() {
		return persen;
	}

	public void setPersen(Double persen) {
		this.persen = persen;
	}

	public Integer getFlag_fix() {
		return flag_fix;
	}

	public void setFlag_fix(Integer flag_fix) {
		this.flag_fix = flag_fix;
	}

	public Integer getFlag_proses() {
		return flag_proses;
	}

	public void setFlag_proses(Integer flag_proses) {
		this.flag_proses = flag_proses;
	}

	public Double getTsi() {
		return tsi;
	}

	public void setTsi(Double tsi) {
		this.tsi = tsi;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Double getBonus() {
		return bonus;
	}

	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}

	public Double getDeviden() {
		return deviden;
	}

	public void setDeviden(Double deviden) {
		this.deviden = deviden;
	}

	public Double getMaturity() {
		return maturity;
	}

	public void setMaturity(Double maturity) {
		this.maturity = maturity;
	}

	public Double getNilai_tunai() {
		return nilai_tunai;
	}

	public void setNilai_tunai(Double nilai_tunai) {
		this.nilai_tunai = nilai_tunai;
	}

	public Double getSaving() {
		return saving;
	}

	public void setSaving(Double saving) {
		this.saving = saving;
	}

	public Double getTahapan() {
		return tahapan;
	}

	public void setTahapan(Double tahapan) {
		this.tahapan = tahapan;
	}

	public String getLku_id() {
		return lku_id;
	}

	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}

	public String getLsbs_id() {
		return lsbs_id;
	}

	public void setLsbs_id(String lsbs_id) {
		this.lsbs_id = lsbs_id;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Integer getJenis() {
		return jenis;
	}

	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}

	public Integer getLus_id() {
		return lus_id;
	}

	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}

	public Double getNilai() {
		return nilai;
	}

	public void setNilai(Double nilai) {
		this.nilai = nilai;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public Integer getTahun() {
		return tahun;
	}

	public void setTahun(Integer tahun) {
		this.tahun = tahun;
	}

	public Double getMspr_premium() {
		return mspr_premium;
	}

	public void setMspr_premium(Double mspr_premium) {
		this.mspr_premium = mspr_premium;
	}

}
