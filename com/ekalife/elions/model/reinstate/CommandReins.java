/**CommandReins.java
 * @author  : Ferry Harlim
 * @created : Feb 5, 2008 10:57:06 AM
 */
package com.ekalife.elions.model.reinstate;

import java.io.Serializable;
import java.util.List;


public class CommandReins implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;
	private Integer day;
	private Integer month;
	private Integer year;
	private String tglBayar;
	
	
	private List<Reinstatement> lsReinstatement;

	public List<Reinstatement> getLsReinstatement() {
		return lsReinstatement;
	}
	public void setLsReinstatement(List<Reinstatement> lsReinstatement) {
		this.lsReinstatement = lsReinstatement;
	}
	
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getYear() {
		return year;
	}
	public String getTglBayar() {
		return tglBayar;
	}
	public void setTglBayar(String tglBayar) {
		this.tglBayar = tglBayar;
	}
	
	
	
}
