package com.ekalife.elions.model.worksheet;

import java.io.Serializable;

public class UwEkg implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;
	private Integer insured_no;
	private Integer urutanEkg;
	private String tglMcuEkg;
	private String tmpMcuEkg;
	private Integer flagEkg;
	private String ekg_kelainan;
	private String ekg_lic_id;
	private String ekg_lic_desc;
	
	public UwEkg() {
		setFlagEkg(0);
	}
	
	public Integer getInsured_no() { return insured_no; }
	public void setInsured_no(Integer insured_no) { this.insured_no = insured_no; }
	
	public String getReg_spaj() { return reg_spaj; }
	public void setReg_spaj(String reg_spaj) { this.reg_spaj = reg_spaj; }
	
	public String getEkg_kelainan() { return ekg_kelainan; }
	public void setEkg_kelainan(String ekg_kelainan) { this.ekg_kelainan = ekg_kelainan; }
	
	public String getEkg_lic_desc() { return ekg_lic_desc; }
	public void setEkg_lic_desc(String ekg_lic_desc) { this.ekg_lic_desc = ekg_lic_desc; }
	
	public String getEkg_lic_id() { return ekg_lic_id; }
	public void setEkg_lic_id(String ekg_lic_id) { this.ekg_lic_id = ekg_lic_id; }
	
	public Integer getFlagEkg() { return flagEkg; }
	public void setFlagEkg(Integer flagEkg) { this.flagEkg = flagEkg; }
	
	public String getTglMcuEkg() { return tglMcuEkg; }
	public void setTglMcuEkg(String tglMcuEkg) { this.tglMcuEkg = tglMcuEkg; }
	
	public String getTmpMcuEkg() { return tmpMcuEkg; }
	public void setTmpMcuEkg(String tmpMcuEkg) { this.tmpMcuEkg = tmpMcuEkg; }
	
	public Integer getUrutanEkg() { return urutanEkg; }
	public void setUrutanEkg(Integer urutanEkg) { this.urutanEkg = urutanEkg; }
	
	
}
