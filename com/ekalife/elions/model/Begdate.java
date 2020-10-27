package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ekalife.utils.Common;

public class Begdate implements Serializable{
	
	private static final long serialVersionUID = 8238795333728661197L;
	
	public String pesan;
	public String reg_spaj;
	public String pp_bdate;
	public String tt_bdate;
	public String prod_bdate;
	public String ket_bdate;
	public String old_bdate;
	public String new_bdate;
	public String no_helpdesk;

	public Policy policy;
	public Insured insured;
	public ArrayList<Product> lsprodins;
	public ArrayList<Production> lsprod;
	public ArrayList<Billing> lsbilling;
	public Date msp_pas_beg_date;
	public Date msp_pas_end_date;
	public ArrayList<Ulink> lsulink;
	public ArrayList<TransUlink> lstransulink;
	public ArrayList<DetUlink> lsdetulink;
	public ArrayList<BiayaUlink> lsbiayaulink;
	public ArrayList<UlinkBill> lsulinkbill;
	
	public String getPesan() {
		return pesan;
	}
	public void setPesan(String pesan) {
		this.pesan = pesan;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getPp_bdate() {
		return pp_bdate;
	}
	public void setPp_bdate(String pp_bdate) {
		this.pp_bdate = pp_bdate;
	}
	public String getTt_bdate() {
		return tt_bdate;
	}
	public void setTt_bdate(String tt_bdate) {
		this.tt_bdate = tt_bdate;
	}
	public String getProd_bdate() {
		return prod_bdate;
	}
	public void setProd_bdate(String prod_bdate) {
		this.prod_bdate = prod_bdate;
	}
	public String getKet_bdate() {
		return ket_bdate;
	}
	public void setKet_bdate(String ket_bdate) {
		this.ket_bdate = ket_bdate;
	}
	public String getOld_bdate() {
		return old_bdate;
	}
	public void setOld_bdate(String old_bdate) {
		this.old_bdate = old_bdate;
	}
	public String getNew_bdate() {
		return new_bdate;
	}
	public void setNew_bdate(String new_bdate) {
		this.new_bdate = new_bdate;
	}
	public String getNo_helpdesk() {
		return no_helpdesk;
	}
	public void setNo_helpdesk(String no_helpdesk) {
		this.no_helpdesk = no_helpdesk;
	}

	
	public Policy getPolicy() {
		return policy;
	}
	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
	public Insured getInsured() {
		return insured;
	}
	public void setInsured(Insured insured) {
		this.insured = insured;
	}
	public ArrayList<Product> getLsprodins() {
		return lsprodins;
	}
	public void setLsprodins(ArrayList<Product> lsprodins) {
		this.lsprodins = lsprodins;
	}
	public ArrayList<Production> getLsprod() {
		return lsprod;
	}
	public void setLsprod(ArrayList<Production> lsprod) {
		this.lsprod = lsprod;
	}
	public ArrayList<Billing> getLsbilling() {
		return lsbilling;
	}
	public void setLsbilling(ArrayList<Billing> lsbilling) {
		this.lsbilling = lsbilling;
	}
	public Date getMsp_pas_beg_date() {
		return msp_pas_beg_date;
	}
	public void setMsp_pas_beg_date(Date msp_pas_beg_date) {
		this.msp_pas_beg_date = msp_pas_beg_date;
	}
	public Date getMsp_pas_end_date() {
		return msp_pas_end_date;
	}
	public void setMsp_pas_end_date(Date msp_pas_end_date) {
		this.msp_pas_end_date = msp_pas_end_date;
	}
	public ArrayList<Ulink> getLsulink() {
		return lsulink;
	}
	public void setLsulink(ArrayList<Ulink> lsulink) {
		this.lsulink = lsulink;
	}
	public ArrayList<TransUlink> getLstransulink() {
		return lstransulink;
	}
	public void setLstransulink(ArrayList<TransUlink> lstransulink) {
		this.lstransulink = lstransulink;
	}
	public ArrayList<DetUlink> getLsdetulink() {
		return lsdetulink;
	}
	public void setLsdetulink(ArrayList<DetUlink> lsdetulink) {
		this.lsdetulink = lsdetulink;
	}
	public ArrayList<BiayaUlink> getLsbiayaulink() {
		return lsbiayaulink;
	}
	public void setLsbiayaulink(ArrayList<BiayaUlink> lsbiayaulink) {
		this.lsbiayaulink = lsbiayaulink;
	}
	public ArrayList<UlinkBill> getLsulinkbill() {
		return lsulinkbill;
	}
	public void setLsulinkbill(ArrayList<UlinkBill> lsulinkbill) {
		this.lsulinkbill = lsulinkbill;
	}
}
