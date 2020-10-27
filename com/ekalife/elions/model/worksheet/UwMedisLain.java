package com.ekalife.elions.model.worksheet;

import java.io.Serializable;

public class UwMedisLain implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private String reg_spaj;
	private Integer insured_no;
	private Integer urutanMedisLain;
	private String tglMcuMedisLain;
	private String tmpMcuMedisLain;
	private String ketMedisLain;
	
	public Integer getInsured_no() { return insured_no; }
	public void setInsured_no(Integer insured_no) { this.insured_no = insured_no; }
	
	public String getReg_spaj() { return reg_spaj; }
	public void setReg_spaj(String reg_spaj) { this.reg_spaj = reg_spaj; }
	
	public String getKetMedisLain() { return ketMedisLain; }
	public void setKetMedisLain(String ketMedisLain) { this.ketMedisLain = ketMedisLain; }
	
	public String getTglMcuMedisLain() { return tglMcuMedisLain; }
	public void setTglMcuMedisLain(String tglMcuMedisLain) { this.tglMcuMedisLain = tglMcuMedisLain; }
	
	public String getTmpMcuMedisLain() { return tmpMcuMedisLain; }
	public void setTmpMcuMedisLain(String tmpMcuMedisLain) { this.tmpMcuMedisLain = tmpMcuMedisLain; }
	
	public Integer getUrutanMedisLain() { return urutanMedisLain; }
	public void setUrutanMedisLain(Integer urutanMedisLain) { this.urutanMedisLain = urutanMedisLain; }
	
	
}
