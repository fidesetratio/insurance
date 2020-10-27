package com.ekalife.elions.model;

import java.io.Serializable;

public class CashFlow implements Serializable {

	private static final long serialVersionUID = 1L;
	
private Integer kode_cash_flow;                
private String keterangan;
public String getKeterangan() {
	return keterangan;
}
public void setKeterangan(String keterangan) {
	this.keterangan = keterangan;
}
public Integer getKode_cash_flow() {
	return kode_cash_flow;
}
public void setKode_cash_flow(Integer kode_cash_flow) {
	this.kode_cash_flow = kode_cash_flow;
}

}