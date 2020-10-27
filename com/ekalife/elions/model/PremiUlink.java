package com.ekalife.elions.model;

import java.io.Serializable;

public class PremiUlink implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;                       
	private Integer msbi_tahun_ke;                 
	private Integer msbi_premi_ke;                 
	private Integer premi_ke;                      
	private Integer line_ak;                       
	private Integer th_ak;                         
	private Double premi;                          
	private Double total_premi;                    
	private Double persen_ak;
	public Integer getLine_ak() {
		return line_ak;
	}
	public void setLine_ak(Integer line_ak) {
		this.line_ak = line_ak;
	}
	public Integer getMsbi_premi_ke() {
		return msbi_premi_ke;
	}
	public void setMsbi_premi_ke(Integer msbi_premi_ke) {
		this.msbi_premi_ke = msbi_premi_ke;
	}
	public Integer getMsbi_tahun_ke() {
		return msbi_tahun_ke;
	}
	public void setMsbi_tahun_ke(Integer msbi_tahun_ke) {
		this.msbi_tahun_ke = msbi_tahun_ke;
	}
	public Double getPersen_ak() {
		return persen_ak;
	}
	public void setPersen_ak(Double persen_ak) {
		this.persen_ak = persen_ak;
	}
	public Double getPremi() {
		return premi;
	}
	public void setPremi(Double premi) {
		this.premi = premi;
	}
	public Integer getPremi_ke() {
		return premi_ke;
	}
	public void setPremi_ke(Integer premi_ke) {
		this.premi_ke = premi_ke;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getTh_ak() {
		return th_ak;
	}
	public void setTh_ak(Integer th_ak) {
		this.th_ak = th_ak;
	}
	public Double getTotal_premi() {
		return total_premi;
	}
	public void setTotal_premi(Double total_premi) {
		this.total_premi = total_premi;
	}                    
}
