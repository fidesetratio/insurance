package com.ekalife.elions.model.saveseries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupByKurs implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String kurs;
	
	private Result result;
	private List<Result> lsGroupByKurs;
	
	private List<GroupByPlan> lsGroupByPlan;
	
	private List<GroupByKet> lsGroupByKet;

	public GroupByKurs(){
		lsGroupByKurs=new ArrayList<Result>();
		lsGroupByPlan=new ArrayList<GroupByPlan>();
		lsGroupByKet=new  ArrayList<GroupByKet>();
	}

	public String getKurs() {
		return kurs;
	}

	public void setKurs(String kurs) {
		this.kurs = kurs;
	}

	public List<Result> getLsGroupByKurs() {
		return lsGroupByKurs;
	}

	public void setLsGroupByKurs(List<Result> lsGroupByKurs) {
		this.lsGroupByKurs = lsGroupByKurs;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public List<GroupByPlan> getLsGroupByPlan() {
		return lsGroupByPlan;
	}

	public void setLsGroupByPlan(List<GroupByPlan> lsGroupByPlan) {
		this.lsGroupByPlan = lsGroupByPlan;
	}

	public List<GroupByKet> getLsGroupByKet() {
		return lsGroupByKet;
	}

	public void setLsGroupByKet(List<GroupByKet> lsGroupByKet) {
		this.lsGroupByKet = lsGroupByKet;
	}

	
	
}

