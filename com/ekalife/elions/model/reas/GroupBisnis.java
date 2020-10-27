package com.ekalife.elions.model.reas;

import java.io.Serializable;

public class GroupBisnis implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer lsgb_id;
	private String lsgb_name;
	private Double[][]nilai;
	public Integer getLsgb_id() {
		return lsgb_id;
	}
	public void setLsgb_id(Integer lsgb_id) {
		this.lsgb_id = lsgb_id;
	}
	public String getLsgb_name() {
		return lsgb_name;
	}
	public void setLsgb_name(String lsgb_name) {
		this.lsgb_name = lsgb_name;
	}
	public Double[][] getNilai() {
		return nilai;
	}
	public void setNilai(Double[][] nilai) {
		this.nilai = nilai;
	}
	
	public Double[][] newGroupBisnis(){
		Double [][]nilai=new Double[3][6];
		for(int i=0;i<3;i++){
			for(int j=0;j<6;j++){
				nilai[i][j]=new Double(0);
			}
		}
		return nilai;
	}
}
