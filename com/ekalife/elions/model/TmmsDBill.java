package com.ekalife.elions.model;

import java.io.Serializable;

public class TmmsDBill implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7127968854583126039L;
	
	private String id;                             
	private Integer urut;                          
	private Integer premi_ke;                      
	private Integer tahun_ke;                      
	private String product_code;                   
	private Integer plan;                          
	private Double rate_premi;                     
	private Double premi;                          
	private Double diskon;                         
	private Double komisi;                         
	private Integer usia;
	
	public Double getDiskon() {
		return diskon;
	}
	public void setDiskon(Double diskon) {
		this.diskon = diskon;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Double getKomisi() {
		return komisi;
	}
	public void setKomisi(Double komisi) {
		this.komisi = komisi;
	}
	public Integer getPlan() {
		return plan;
	}
	public void setPlan(Integer plan) {
		this.plan = plan;
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
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public Double getRate_premi() {
		return rate_premi;
	}
	public void setRate_premi(Double rate_premi) {
		this.rate_premi = rate_premi;
	}
	public Integer getTahun_ke() {
		return tahun_ke;
	}
	public void setTahun_ke(Integer tahun_ke) {
		this.tahun_ke = tahun_ke;
	}
	public Integer getUrut() {
		return urut;
	}
	public void setUrut(Integer urut) {
		this.urut = urut;
	}
	public Integer getUsia() {
		return usia;
	}
	public void setUsia(Integer usia) {
		this.usia = usia;
	}                          

	
}
