package com.ekalife.elions.model.saveseries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupByPlan implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private String nm_plan;
	
	
	private List<Result> lsGroupByPlan;
	
	private List<GroupByKet> lsGroupByKet;
	
	private List<GroupByKurs> lsGroupByKurs;
	
	

	public GroupByPlan(){	
		lsGroupByKet=new ArrayList<GroupByKet>();
		lsGroupByPlan=new ArrayList<Result>();
		lsGroupByKurs=new ArrayList<GroupByKurs>();
	}



	public List<GroupByKet> getLsGroupByKet() {
		return lsGroupByKet;
	}



	public void setLsGroupByKet(List<GroupByKet> lsGroupByKet) {
		this.lsGroupByKet = lsGroupByKet;
	}



	public List<Result> getLsGroupByPlan() {
		return lsGroupByPlan;
	}



	public void setLsGroupByPlan(List<Result> lsGroupByPlan) {
		this.lsGroupByPlan = lsGroupByPlan;
	}



	public String getNm_plan() {
		return nm_plan;
	}



	public void setNm_plan(String nm_plan) {
		this.nm_plan = nm_plan;
	}



	public List<GroupByKurs> getLsGroupByKurs() {
		return lsGroupByKurs;
	}



	public void setLsGroupByKurs(List<GroupByKurs> lsGroupByKurs) {
		this.lsGroupByKurs = lsGroupByKurs;
	}

	
}

