package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class KursDanJumlah implements Serializable {


	private static final long serialVersionUID = -3933250842075564068L;
	private String lku_id;                       
	private Double  nominal;
	
	
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Double getNominal() {
		return nominal;
	}
	public void setNominal(Double nominal) {
		this.nominal = nominal;
	}     
	
	
}
