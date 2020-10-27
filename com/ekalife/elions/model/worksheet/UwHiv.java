package com.ekalife.elions.model.worksheet;

import java.io.Serializable;

public class UwHiv implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nv_anti_hiv_anti_hiv;
	
	private String reg_spaj;
	private Integer insured_no;
	private Integer urutanHiv;
	private String tglMcuHiv;
	private String tmpMcuHiv;
	private String antiHiv;
	
	public UwHiv() {
		setNv_anti_hiv_anti_hiv("Non Reaktif");
	}

	public String getNv_anti_hiv_anti_hiv() { return nv_anti_hiv_anti_hiv; }
	public void setNv_anti_hiv_anti_hiv(String nv_anti_hiv_anti_hiv) { this.nv_anti_hiv_anti_hiv = nv_anti_hiv_anti_hiv; }

	public Integer getInsured_no() { return insured_no; }
	public void setInsured_no(Integer insured_no) { this.insured_no = insured_no; }

	public String getReg_spaj() { return reg_spaj; }
	public void setReg_spaj(String reg_spaj) { this.reg_spaj = reg_spaj; }

	public String getAntiHiv() { return antiHiv; }
	public void setAntiHiv(String antiHiv) { this.antiHiv = antiHiv; }

	public String getTglMcuHiv() { return tglMcuHiv; }
	public void setTglMcuHiv(String tglMcuHiv) { this.tglMcuHiv = tglMcuHiv; }

	public String getTmpMcuHiv() { return tmpMcuHiv; }
	public void setTmpMcuHiv(String tmpMcuHiv) { this.tmpMcuHiv = tmpMcuHiv; }

	public Integer getUrutanHiv() { return urutanHiv; }
	public void setUrutanHiv(Integer urutanHiv) { this.urutanHiv = urutanHiv; }
	
	
	
}
