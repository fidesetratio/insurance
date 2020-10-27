package com.ekalife.elions.model;

import java.io.Serializable;


public class IncomeCalc implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mns_kd_nasabah;                 
	private Double annual_income;                  
	private Double pct_interest1;                  
	private Double capital;                        
	private Double pct_interest2;                  
	private Integer period_year;
	
	public Double getAnnual_income() {
		return annual_income;
	}
	public void setAnnual_income(Double annual_income) {
		this.annual_income = annual_income;
	}
	public Double getCapital() {
		return capital;
	}
	public void setCapital(Double capital) {
		this.capital = capital;
	}
	public String getMns_kd_nasabah() {
		return mns_kd_nasabah;
	}
	public void setMns_kd_nasabah(String mns_kd_nasabah) {
		this.mns_kd_nasabah = mns_kd_nasabah;
	}
	public Double getPct_interest1() {
		return pct_interest1;
	}
	public void setPct_interest1(Double pct_interest1) {
		this.pct_interest1 = pct_interest1;
	}
	public Double getPct_interest2() {
		return pct_interest2;
	}
	public void setPct_interest2(Double pct_interest2) {
		this.pct_interest2 = pct_interest2;
	}
	public Integer getPeriod_year() {
		return period_year;
	}
	public void setPeriod_year(Integer period_year) {
		this.period_year = period_year;
	} 
	
	
	
}