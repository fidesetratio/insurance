package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.List;

/**
 * Model Untuk Edit Data Form Controller (new business)
 * 
 * @author Yusuf
 * @since Sep 26, 2008 (2:08:03 PM)
 */
public class CommandEditData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4276759869477159639L;
	//
	public String reg_spaj;
	public String lus_id;
	
	//Edit Data Pembayaran (Titipan Premi + Payment)
	public List<DepositPremium > listTitipanPremi;
	public List<Payment> listPayment;
	
	//
	public List<DepositPremium> getListTitipanPremi() {
		return listTitipanPremi;
	}
	public void setListTitipanPremi(List<DepositPremium> listTitipanPremi) {
		this.listTitipanPremi = listTitipanPremi;
	}
	public List<Payment> getListPayment() {
		return listPayment;
	}
	public void setListPayment(List<Payment> listPayment) {
		this.listPayment = listPayment;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getLus_id() {
		return lus_id;
	}
	public void setLus_id(String lus_id) {
		this.lus_id = lus_id;
	}
	
}