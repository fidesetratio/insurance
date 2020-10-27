package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class DetUlink implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;
	private Integer mu_ke;
	private String lji_id;
	private Double mdu_persen;
	private Double mdu_jumlah;
	private Double mdu_unit;
	private Double mdu_saldo_unit;
	private Double mdu_saldo_unit_pp;
	private Double mdu_saldo_unit_tu;
	private Double mdu_persen_tu;
	private Date mdu_last_trans;
	private Integer mdu_flag_min;
	private Integer mdu_aktif;
	private String lji_invest;
	
	public String getLji_invest() {
		return lji_invest;
	}
	public void setLji_invest(String lji_invest) {
		this.lji_invest = lji_invest;
	}
	public String getLji_id() {
		return lji_id;
	}
	public void setLji_id(String lji_id) {
		this.lji_id = lji_id;
	}
	public Integer getMdu_aktif() {
		return mdu_aktif;
	}
	public void setMdu_aktif(Integer mdu_aktif) {
		this.mdu_aktif = mdu_aktif;
	}
	public Integer getMdu_flag_min() {
		return mdu_flag_min;
	}
	public void setMdu_flag_min(Integer mdu_flag_min) {
		this.mdu_flag_min = mdu_flag_min;
	}
	public Double getMdu_jumlah() {
		return mdu_jumlah;
	}
	public void setMdu_jumlah(Double mdu_jumlah) {
		this.mdu_jumlah = mdu_jumlah;
	}
	public Date getMdu_last_trans() {
		return mdu_last_trans;
	}
	public void setMdu_last_trans(Date mdu_last_trans) {
		this.mdu_last_trans = mdu_last_trans;
	}
	public Double getMdu_persen() {
		return mdu_persen;
	}
	public void setMdu_persen(Double mdu_persen) {
		this.mdu_persen = mdu_persen;
	}
	public Double getMdu_persen_tu() {
		return mdu_persen_tu;
	}
	public void setMdu_persen_tu(Double mdu_persen_tu) {
		this.mdu_persen_tu = mdu_persen_tu;
	}
	public Double getMdu_saldo_unit() {
		return mdu_saldo_unit;
	}
	public void setMdu_saldo_unit(Double mdu_saldo_unit) {
		this.mdu_saldo_unit = mdu_saldo_unit;
	}
	public Double getMdu_unit() {
		return mdu_unit;
	}
	public void setMdu_unit(Double mdu_unit) {
		this.mdu_unit = mdu_unit;
	}
	public Integer getMu_ke() {
		return mu_ke;
	}
	public void setMu_ke(Integer mu_ke) {
		this.mu_ke = mu_ke;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Double getMdu_saldo_unit_pp() {
		return mdu_saldo_unit_pp;
	}
	public void setMdu_saldo_unit_pp(Double mdu_saldo_unit_pp) {
		this.mdu_saldo_unit_pp = mdu_saldo_unit_pp;
	}
	public Double getMdu_saldo_unit_tu() {
		return mdu_saldo_unit_tu;
	}
	public void setMdu_saldo_unit_tu(Double mdu_saldo_unit_tu) {
		this.mdu_saldo_unit_tu = mdu_saldo_unit_tu;
	}
	
}
