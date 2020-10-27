package com.ekalife.elions.model.worksheet;

import java.io.Serializable;

public class UwDecisionRider implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;
	private Integer insured_no;
	private Integer mwd_urut;
	private Integer mwdr_urut;
	private Integer lsbs_id;
	private Integer lsdbs_number;
	private Integer urutan_penyakit;
	private Integer urutan_decision;
	private String rate_persen;
	private String rate_permil;
	
	public Integer getInsured_no() { return insured_no; }
	public void setInsured_no(Integer insured_no) { this.insured_no = insured_no; }
	
	public Integer getLsbs_id() { return lsbs_id; }
	public void setLsbs_id(Integer lsbs_id) { this.lsbs_id = lsbs_id; }
	
	public Integer getLsdbs_number() { return lsdbs_number; }
	public void setLsdbs_number(Integer lsdbs_number) { this.lsdbs_number = lsdbs_number; }
	
	public String getRate_permil() { return rate_permil; }
	public void setRate_permil(String rate_permil) { this.rate_permil = rate_permil; }
	
	public String getRate_persen() { return rate_persen; }
	public void setRate_persen(String rate_persen) { this.rate_persen = rate_persen; }
	
	public String getReg_spaj() { return reg_spaj; }
	public void setReg_spaj(String reg_spaj) { this.reg_spaj = reg_spaj; }
	
	public Integer getMwd_urut() { return mwd_urut; }
	public void setMwd_urut(Integer mwd_urut) { this.mwd_urut = mwd_urut; }
	
	public Integer getMwdr_urut() { return mwdr_urut; }
	public void setMwdr_urut(Integer mwdr_urut) { this.mwdr_urut = mwdr_urut; }
	
	public Integer getUrutan_decision() { return urutan_decision; }
	public void setUrutan_decision(Integer urutan_decision) { this.urutan_decision = urutan_decision; }
	
	public Integer getUrutan_penyakit() { return urutan_penyakit; }
	public void setUrutan_penyakit(Integer urutan_penyakit) { this.urutan_penyakit = urutan_penyakit; }
	
}
