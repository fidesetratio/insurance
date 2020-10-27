package com.ekalife.elions.model.kyc;

import java.io.Serializable;

public class Hrc implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer lshc_id;
	private String lshc_desc;
	private Integer brs;
	private Integer cek;
	public Integer getCek() {
		return cek;
	}
	public void setCek(Integer cek) {
		this.cek = cek;
	}
	public String getLshc_desc() {
		return lshc_desc;
	}
	public void setLshc_desc(String lshc_desc) {
		this.lshc_desc = lshc_desc;
	}
	public Integer getLshc_id() {
		return lshc_id;
	}
	public void setLshc_id(Integer lshc_id) {
		this.lshc_id = lshc_id;
	}
	public Integer getBrs() {
		return brs;
	}
	public void setBrs(Integer brs) {
		this.brs = brs;
	}
	
}
