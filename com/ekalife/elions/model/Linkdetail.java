package com.ekalife.elions.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Linkdetail implements Serializable {

	private static final long serialVersionUID = 1L;
	

	private String descr;                    
	private Integer tahunKe;   
	private Integer premiKe;
	private BigDecimal premi;
	private String tahunKeAndPremiKe;
	
	public String getTahunKeAndPremiKe() {
		return tahunKeAndPremiKe;
	}
	public void setTahunKeAndPremiKe(String tahunKeAndPremiKe) {
		this.tahunKeAndPremiKe = tahunKeAndPremiKe;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public BigDecimal getPremi() {
		return premi;
	}
	public void setPremi(BigDecimal premi) {
		this.premi = premi;
	}
	public Integer getPremiKe() {
		return premiKe;
	}
	public void setPremiKe(Integer premiKe) {
		this.premiKe = premiKe;
	}
	public Integer getTahunKe() {
		return tahunKe;
	}
	public void setTahunKe(Integer tahunKe) {
		this.tahunKe = tahunKe;
	}                     

}
