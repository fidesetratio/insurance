package com.ekalife.elions.model;

import java.io.Serializable;

public class AlasanTolak implements Serializable {

	private static final long serialVersionUID = 1L;
	
private Integer id;                            
private Integer jenis;                         
private String keterangan;

public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public Integer getJenis() {
	return jenis;
}
public void setJenis(Integer jenis) {
	this.jenis = jenis;
}
public String getKeterangan() {
	return keterangan;
}
public void setKeterangan(String keterangan) {
	this.keterangan = keterangan;
}

}