package com.ekalife.elions.model.saveseries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Result implements Serializable{
	/**
	 * Field type : long
	 * Create By : Bertho Rafitya Iwasurya
	 * Date Created : 4:38:05 PM
	 */
	private static final long serialVersionUID = 7283851671984826829L;
	private String lsbp_nama;
	private String kurs;
	private String nm_plan;
	private String ket;
	private Double agency;
	private Double bancass;
	private Double wsite;
	private Double tm;
	private Double total;
	private Date periode;
	
	private List<GroupByKurs> lsgroupbykurs;
	
	private List<GroupByPlan> lsgroupbyPlan;
	
	public Result(){
		lsgroupbykurs=new ArrayList<GroupByKurs>();
		lsgroupbyPlan= new ArrayList<GroupByPlan>();
	}

	public Double getAgency() {
		return agency;
	}

	public void setAgency(Double agency) {
		this.agency = agency;
	}

	public Double getBancass() {
		return bancass;
	}

	public void setBancass(Double bancass) {
		this.bancass = bancass;
	}

	public String getKet() {
		return ket;
	}

	public void setKet(String ket) {
		this.ket = ket;
	}

	public String getKurs() {
		return kurs;
	}

	public void setKurs(String kurs) {
		this.kurs = kurs;
	}

	public List<GroupByKurs> getLsgroupbykurs() {
		return lsgroupbykurs;
	}

	public void setLsgroupbykurs(List<GroupByKurs> lsgroupbykurs) {
		this.lsgroupbykurs = lsgroupbykurs;
	}

	public String getNm_plan() {
		return nm_plan;
	}

	public void setNm_plan(String nm_plan) {
		this.nm_plan = nm_plan;
	}

	public Date getPeriode() {
		return periode;
	}

	public void setPeriode(Date periode) {
		this.periode = periode;
	}

	public Double getTm() {
		return tm;
	}

	public void setTm(Double tm) {
		this.tm = tm;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getWsite() {
		return wsite;
	}

	public void setWsite(Double wsite) {
		this.wsite = wsite;
	}

	public String getLsbp_nama() {
		return lsbp_nama;
	}

	public void setLsbp_nama(String lsbp_nama) {
		this.lsbp_nama = lsbp_nama;
	}

	public List<GroupByPlan> getLsgroupbyPlan() {
		return lsgroupbyPlan;
	}

	public void setLsgroupbyPlan(List<GroupByPlan> lsgroupbyPlan) {
		this.lsgroupbyPlan = lsgroupbyPlan;
	}
}

