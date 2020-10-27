/**
 * @author  : Ferry Harlim
 * @created : Jan 17, 2007 
 */
package com.ekalife.elions.model;

import java.io.Serializable;

public class ReasTempNew implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;                       
	private Integer mste_insured_no;
	private Integer lsbs_id;                       
	private Integer lsdbs_number;                  
	private String lsdbs_name;
	private String lku_id;                         
	private Integer nil_kurs;                      
	private Integer mste_reas;                     
	private Double simultan;                       
	private Double tsi;                            
	private Double sar;                            
	private Double retensi;                        
	private Double reas;                           
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	public Integer getMste_reas() {
		return mste_reas;
	}
	public void setMste_reas(Integer mste_reas) {
		this.mste_reas = mste_reas;
	}
	public Integer getNil_kurs() {
		return nil_kurs;
	}
	public void setNil_kurs(Integer nil_kurs) {
		this.nil_kurs = nil_kurs;
	}
	public Double getReas() {
		return reas;
	}
	public void setReas(Double reas) {
		this.reas = reas;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Double getRetensi() {
		return retensi;
	}
	public void setRetensi(Double retensi) {
		this.retensi = retensi;
	}
	public Double getSar() {
		return sar;
	}
	public void setSar(Double sar) {
		this.sar = sar;
	}
	public Double getSimultan() {
		return simultan;
	}
	public void setSimultan(Double simultan) {
		this.simultan = simultan;
	}
	public Double getTsi() {
		return tsi;
	}
	public void setTsi(Double tsi) {
		this.tsi = tsi;
	}
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}
	public Integer getMste_insured_no() {
		return mste_insured_no;
	}
	public void setMste_insured_no(Integer mste_insured_no) {
		this.mste_insured_no = mste_insured_no;
	}

}
