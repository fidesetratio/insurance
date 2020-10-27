package com.ekalife.elions.model.worksheet;

import java.io.Serializable;

public class UwRiwPenyakit implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;
	private Integer insured_no;
	private Integer urutanLpk;
	private String rp_desc;
	private Integer rp_type;
	private Integer rp_urutan;

	public UwRiwPenyakit(Integer type) {
		setRp_type(type);
	}
	
	public Integer getInsured_no() { return insured_no; }
	public void setInsured_no(Integer insured_no) { this.insured_no = insured_no; }

	public String getReg_spaj() { return reg_spaj; }
	public void setReg_spaj(String reg_spaj) { this.reg_spaj = reg_spaj; }

	public Integer getUrutanLpk() { return urutanLpk; }
	public void setUrutanLpk(Integer urutanLpk) { this.urutanLpk = urutanLpk; }

	public String getRp_desc() { return rp_desc; }
	public void setRp_desc(String rp_desc) { this.rp_desc = rp_desc; }
	
	public Integer getRp_type() { return rp_type; } 
	public void setRp_type(Integer rp_type) { this.rp_type = rp_type; }

	public Integer getRp_urutan() { return rp_urutan; }
	public void setRp_urutan(Integer rp_urutan) { this.rp_urutan = rp_urutan; }
	
}
