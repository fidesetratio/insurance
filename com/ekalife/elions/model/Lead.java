/**
 * @author  : Ferry Harlim
 * @created : Dec 27, 2006 
 */
package com.ekalife.elions.model;

import java.io.Serializable;

public class Lead implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer ljl_sumber;                    
	private Integer level_id;                      
	private String ljl_ket;                        
	private Integer ljl_jenis;
	public Integer getLevel_id() {
		return level_id;
	}
	public void setLevel_id(Integer level_id) {
		this.level_id = level_id;
	}
	public Integer getLjl_jenis() {
		return ljl_jenis;
	}
	public void setLjl_jenis(Integer ljl_jenis) {
		this.ljl_jenis = ljl_jenis;
	}
	public String getLjl_ket() {
		return ljl_ket;
	}
	public void setLjl_ket(String ljl_ket) {
		this.ljl_ket = ljl_ket;
	}
	public Integer getLjl_sumber() {
		return ljl_sumber;
	}
	public void setLjl_sumber(Integer ljl_sumber) {
		this.ljl_sumber = ljl_sumber;
	}   
}
