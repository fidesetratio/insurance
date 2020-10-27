package com.ekalife.elions.model.worksheet;

import java.io.Serializable;

public class UwDadaPa implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private String reg_spaj;
	private Integer insured_no;
	private Integer urutanDadaPA;
	private String tglMcuDadaPA;
	private String tmpMcuDadaPA;
	private Integer flagDadaPa;
	private String dada_pa_kelainan;
	private String dada_pa_lic_id;
	private String dada_pa_lic_desc;
	
	public UwDadaPa() {
		setFlagDadaPa(0);
	}
	
	public Integer getInsured_no() { return insured_no; }
	public void setInsured_no(Integer insured_no) { this.insured_no = insured_no; }
	
	public String getReg_spaj() { return reg_spaj; }
	public void setReg_spaj(String reg_spaj) { this.reg_spaj = reg_spaj; }
	
	public String getDada_pa_kelainan() { return dada_pa_kelainan; }
	public void setDada_pa_kelainan(String dada_pa_kelainan) { this.dada_pa_kelainan = dada_pa_kelainan; }
	
	public String getDada_pa_lic_desc() { return dada_pa_lic_desc; }
	public void setDada_pa_lic_desc(String dada_pa_lic_desc) { this.dada_pa_lic_desc = dada_pa_lic_desc; }
	
	public String getDada_pa_lic_id() { return dada_pa_lic_id; }
	public void setDada_pa_lic_id(String dada_pa_lic_id) { this.dada_pa_lic_id = dada_pa_lic_id; }
	
	public Integer getFlagDadaPa() { return flagDadaPa; }
	public void setFlagDadaPa(Integer flagDadaPa) { this.flagDadaPa = flagDadaPa; }
	
	public String getTglMcuDadaPA() { return tglMcuDadaPA; }
	public void setTglMcuDadaPA(String tglMcuDadaPA) { this.tglMcuDadaPA = tglMcuDadaPA; }
	
	public String getTmpMcuDadaPA() { return tmpMcuDadaPA; }
	public void setTmpMcuDadaPA(String tmpMcuDadaPA) { this.tmpMcuDadaPA = tmpMcuDadaPA; }
	
	public Integer getUrutanDadaPA() { return urutanDadaPA; }
	public void setUrutanDadaPA(Integer urutanDadaPA) { this.urutanDadaPA = urutanDadaPA; }
	
	
}
