package com.ekalife.elions.model.worksheet;

import java.io.Serializable;

public class UwQuestionnaire implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;
	private Integer insured_no;
	private Integer urut;
	private Integer lw_id;
	private String kesimpulan;
	
	public Integer getInsured_no() { return insured_no; }
	public void setInsured_no(Integer insured_no) { this.insured_no = insured_no; }
	
	public String getKesimpulan() { return kesimpulan; }
	public void setKesimpulan(String kesimpulan) { this.kesimpulan = kesimpulan; }
	
	public Integer getLw_id() { return lw_id; }
	public void setLw_id(Integer lw_id) { this.lw_id = lw_id; }
	
	public String getReg_spaj() { return reg_spaj; }
	public void setReg_spaj(String reg_spaj) { this.reg_spaj = reg_spaj; }
	
	public Integer getUrut() { return urut; }
	public void setUrut(Integer urut) { this.urut = urut; }
	
}
