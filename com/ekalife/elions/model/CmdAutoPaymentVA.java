package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CmdAutoPaymentVA implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	private String pesan;
	private ArrayList<AutoPaymentVA> listSpaj;
	
	
	public String getPesan() {
		return pesan;
	}
	public void setPesan(String pesan) {
		this.pesan = pesan;
	}
	public ArrayList<AutoPaymentVA> getListSpaj() {
		return listSpaj;
	}
	public void setListSpaj(ArrayList<AutoPaymentVA> listSpaj) {
		this.listSpaj = listSpaj;
	}

}
