package com.ekalife.elions.model.saveseries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupByKet implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String ket;
	
	private List<Result> lsGroupByKet;
	
//	private List<GroupByKet> lsGroupByKet;
	
	public GroupByKet(){
		lsGroupByKet=new ArrayList<Result>();
	}

	public String getKet() {
		return ket;
	}

	public void setKet(String ket) {
		this.ket = ket;
	}

	public List<Result> getLsGroupByKet() {
		return lsGroupByKet;
	}

	public void setLsGroupByKet(List<Result> lsGroupByKet) {
		this.lsGroupByKet = lsGroupByKet;
	}
	
}

