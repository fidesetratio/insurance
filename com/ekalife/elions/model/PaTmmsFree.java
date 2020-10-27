package com.ekalife.elions.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaTmmsFree implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3073015286219060131L;
	
	private Tmms tmms;
	private TmmsDet tmmsDet;
	private TmmsBill tmmsBill;
	private TmmsDBill tmmsDBill;
	
	public Tmms getTmms() {
		return tmms;
	}
	public void setTmms(Tmms tmms) {
		this.tmms = tmms;
	}
	public TmmsDet getTmmsDet() {
		return tmmsDet;
	}
	public void setTmmsDet(TmmsDet tmmsDet) {
		this.tmmsDet = tmmsDet;
	}
	public TmmsBill getTmmsBill() {
		return tmmsBill;
	}
	public void setTmmsBill(TmmsBill tmmsBill) {
		this.tmmsBill = tmmsBill;
	}
	public TmmsDBill getTmmsDBill() {
		return tmmsDBill;
	}
	public void setTmmsDBill(TmmsDBill tmmsDBill) {
		this.tmmsDBill = tmmsDBill;
	}
	
}
