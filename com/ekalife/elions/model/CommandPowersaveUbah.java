package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CommandPowersaveUbah implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -685145854953778848L;
	public String reg_spaj;
	public String mspo_policy_no;
	public String mcl_first;
	
	//table mst_powersave_proses
	public Integer mps_roll_over;
	public Integer mps_jangka_inv;
	public Double mps_rate;
	public Date mps_batas_date;
	public Integer mps_jenis_plan;
	
	public PowersaveProses powersaveProses;
	
	public PowersaveUbah powersaveUbah;
	
	public List<PowersaveUbah> listPowersaveUbah;
	
	public Integer flagPinjaman;
	
	public Integer flagEditMGI;
	public Integer flagEditRO;
	public Integer flagEditRATE;
	public Integer flagStopRO;
	
	public Long selisih;
	
	public String lsbs_id;
	
	public String valid1;
	public String valid2;
	
	public String messageError;
	
	public String getMessageError() {
		return messageError;
	}


	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}


	public CommandPowersaveUbah(){
		powersaveUbah=new PowersaveUbah();
		flagEditMGI=0;
		flagEditRATE=0;
		flagEditRO=0;
		flagStopRO=0;
	}
	
	
	public PowersaveProses getPowersaveProses() {
		return powersaveProses;
	}

	public void setPowersaveProses(PowersaveProses powersaveProses) {
		this.powersaveProses = powersaveProses;
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

	public Integer getFlagPinjaman() {
		return flagPinjaman;
	}


	public void setFlagPinjaman(Integer flagPinjaman) {
		this.flagPinjaman = flagPinjaman;
	}


	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public Date getMps_batas_date() {
		return mps_batas_date;
	}
	public void setMps_batas_date(Date mps_batas_date) {
		this.mps_batas_date = mps_batas_date;
	}
	public Integer getMps_jangka_inv() {
		return mps_jangka_inv;
	}
	public void setMps_jangka_inv(Integer mps_jangka_inv) {
		this.mps_jangka_inv = mps_jangka_inv;
	}
	public Integer getMps_jenis_plan() {
		return mps_jenis_plan;
	}
	public void setMps_jenis_plan(Integer mps_jenis_plan) {
		this.mps_jenis_plan = mps_jenis_plan;
	}
	public Double getMps_rate() {
		return mps_rate;
	}
	public void setMps_rate(Double mps_rate) {
		this.mps_rate = mps_rate;
	}
	public Integer getMps_roll_over() {
		return mps_roll_over;
	}
	public void setMps_roll_over(Integer mps_roll_over) {
		this.mps_roll_over = mps_roll_over;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}


	public String getLsbs_id() {
		return lsbs_id;
	}


	public void setLsbs_id(String lsbs_id) {
		this.lsbs_id = lsbs_id;
	}


	public Integer getFlagEditMGI() {
		return flagEditMGI;
	}


	public void setFlagEditMGI(Integer flagEditMGI) {
		this.flagEditMGI = flagEditMGI;
	}


	public Integer getFlagEditRATE() {
		return flagEditRATE;
	}


	public void setFlagEditRATE(Integer flagEditRATE) {
		this.flagEditRATE = flagEditRATE;
	}


	public Integer getFlagEditRO() {
		return flagEditRO;
	}


	public void setFlagEditRO(Integer flagEditRO) {
		this.flagEditRO = flagEditRO;
	}


	public Integer getFlagStopRO() {
		return flagStopRO;
	}


	public void setFlagStopRO(Integer flagStopRO) {
		this.flagStopRO = flagStopRO;
	}


	public PowersaveUbah getPowersaveUbah() {
		return powersaveUbah;
	}


	public void setPowersaveUbah(PowersaveUbah powersaveUbah) {
		this.powersaveUbah = powersaveUbah;
	}


	public Long getSelisih() {
		return selisih;
	}


	public void setSelisih(Long selisih) {
		this.selisih = selisih;
	}


	public List<PowersaveUbah> getListPowersaveUbah() {
		return listPowersaveUbah;
	}


	public void setListPowersaveUbah(List<PowersaveUbah> listPowersaveUbah) {
		this.listPowersaveUbah = listPowersaveUbah;
	}


	              	
	
}