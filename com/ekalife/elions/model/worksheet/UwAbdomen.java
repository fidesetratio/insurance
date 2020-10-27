package com.ekalife.elions.model.worksheet;

import java.io.Serializable;

public class UwAbdomen implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer urutanAbdomen;
	private String tglMcuAbdomen;
	private String tmpMcuAbdomen;

	private String reg_spaj;
	private Integer insured_no;
	private Integer flagAbdomen;
	private String abdomen_kelainan;
	private String abdomen_lic_id;
	private String abdomen_lic_desc;
	
	public UwAbdomen() {
		setFlagAbdomen(0);
	}
	
	public Integer getInsured_no() { return insured_no; }
	public void setInsured_no(Integer insured_no) { this.insured_no = insured_no; }
	
	public String getReg_spaj() { return reg_spaj; }
	public void setReg_spaj(String reg_spaj) { this.reg_spaj = reg_spaj; }
	
	public String getAbdomen_kelainan() { return abdomen_kelainan; }
	public void setAbdomen_kelainan(String abdomen_kelainan) { this.abdomen_kelainan = abdomen_kelainan; }
	
	public String getAbdomen_lic_desc() { return abdomen_lic_desc; }
	public void setAbdomen_lic_desc(String abdomen_lic_desc) { this.abdomen_lic_desc = abdomen_lic_desc; }
	
	public String getAbdomen_lic_id() { return abdomen_lic_id; }
	public void setAbdomen_lic_id(String abdomen_lic_id) { this.abdomen_lic_id = abdomen_lic_id; }
	
	public Integer getFlagAbdomen() { return flagAbdomen; }
	public void setFlagAbdomen(Integer flagAbdomen) { this.flagAbdomen = flagAbdomen; }
	
	public String getTglMcuAbdomen() { return tglMcuAbdomen; }
	public void setTglMcuAbdomen(String tglMcuAbdomen) { this.tglMcuAbdomen = tglMcuAbdomen; }
	
	public String getTmpMcuAbdomen() { return tmpMcuAbdomen; }
	public void setTmpMcuAbdomen(String tmpMcuAbdomen) { this.tmpMcuAbdomen = tmpMcuAbdomen; }
	
	public Integer getUrutanAbdomen() { return urutanAbdomen; }
	public void setUrutanAbdomen(Integer urutanAbdomen) { this.urutanAbdomen = urutanAbdomen; }
	
	
}
