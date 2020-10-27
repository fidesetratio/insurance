package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataNasabah implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7129681909894770418L;
	private List<String> listPolisLain1;
	private List<String> listPolisLain2;
	private List<String> listPolisLain3;
	private Policy polis;
	private Pemegang2 pemegang;
	private Tertanggung tertanggung;
	private AddressBilling addressBilling;
	private Datausulan datausulan;
	private Agen agen;
	private List<String> listPolis1;
	private List<String> listPolis2;
	private Integer jumlah_sisa_polis;
	String no_polis;
	private String proses;
	private String msch_bas_tgl_terima;
	private List lsPolicy;
	private String spajAwal;
	private Integer cekUpdate;
	private String alasan;
	private List<String> alert;
	private Integer lssh_id;
	



	
	public Integer getLssh_id() {
		return lssh_id;
	}

	public void setLssh_id(Integer lssh_id) {
		this.lssh_id = lssh_id;
	}

	public List<String> getAlert() {
		return alert;
	}

	public void setAlert(List<String> alert) {
		this.alert = alert;
	}

	public String getAlasan() {
		return alasan;
	}

	public void setAlasan(String alasan) {
		this.alasan = alasan;
	}

	public Integer getCekUpdate() {
		return cekUpdate;
	}

	public void setCekUpdate(Integer cekUpdate) {
		this.cekUpdate = cekUpdate;
	}

	public List getLsPolicy() {
		return lsPolicy;
	}

	public void setLsPolicy(List lsPolicy) {
		this.lsPolicy = lsPolicy;
	}

	public String getProses() {
		return proses;
	}

	public void setProses(String proses) {
		this.proses = proses;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}

	public List<String> getListPolis1() {
		return listPolis1;
	}

	public void setListPolis1(List<String> listPolis1) {
		this.listPolis1 = listPolis1;
	}

	public List<String> getListPolis2() {
		return listPolis2;
	}

	public void setListPolis2(List<String> listPolis2) {
		this.listPolis2 = listPolis2;
	}

	public Agen getAgen() {
		return agen;
	}

	public void setAgen(Agen agen) {
		this.agen = agen;
	}

	public Datausulan getDatausulan() {
		return datausulan;
	}

	public void setDatausulan(Datausulan datausulan) {
		this.datausulan = datausulan;
	}

	public DataNasabah() {
		listPolisLain1 = new ArrayList<String>();
		listPolisLain1.add("");
		listPolisLain1.add("");
		listPolisLain1.add("");
		listPolisLain1.add("");
		listPolisLain1.add("");
		listPolisLain2 = new ArrayList<String>();
		listPolisLain2.add("");
		listPolisLain2.add("");
		listPolisLain2.add("");
		listPolisLain2.add("");
		listPolisLain2.add("");
		listPolisLain3 = new ArrayList<String>();
		listPolisLain3.add("");
		listPolisLain3.add("");
		listPolisLain3.add("");
		listPolisLain3.add("");
		listPolisLain3.add("");

		polis = new Policy();
		pemegang = new Pemegang2();
		tertanggung = new Tertanggung();
		addressBilling = new AddressBilling();
	}

	public AddressBilling getAddressBilling() {
		return addressBilling;
	}

	public void setAddressBilling(AddressBilling addressBilling) {
		this.addressBilling = addressBilling;
	}

	public Policy getPolis() {
		return polis;
	}

	public void setPolis(Policy polis) {
		this.polis = polis;
	}

	public Tertanggung getTertanggung() {
		return tertanggung;
	}

	public void setTertanggung(Tertanggung tertanggung) {
		this.tertanggung = tertanggung;
	}

	public List<String> getListPolisLain1() {
		return listPolisLain1;
	}

	public void setListPolisLain1(List<String> listPolisLain1) {
		this.listPolisLain1 = listPolisLain1;
	}

	public List<String> getListPolisLain2() {
		return listPolisLain2;
	}

	public void setListPolisLain2(List<String> listPolisLain2) {
		this.listPolisLain2 = listPolisLain2;
	}

	public List<String> getListPolisLain3() {
		return listPolisLain3;
	}

	public void setListPolisLain3(List<String> listPolisLain3) {
		this.listPolisLain3 = listPolisLain3;
	}

	public Integer getJumlah_sisa_polis() {
		return jumlah_sisa_polis;
	}

	public void setJumlah_sisa_polis(Integer jumlah_sisa_polis) {
		this.jumlah_sisa_polis = jumlah_sisa_polis;
	}

	public Pemegang2 getPemegang() {
		return pemegang;
	}

	public void setPemegang(Pemegang2 pemegang) {
		this.pemegang = pemegang;
	}

	public String getMsch_bas_tgl_terima() {
		return msch_bas_tgl_terima;
	}

	public void setMsch_bas_tgl_terima(String msch_bas_tgl_terima) {
		this.msch_bas_tgl_terima = msch_bas_tgl_terima;
	}

	public String getSpajAwal() {
		return spajAwal;
	}

	public void setSpajAwal(String spajAwal) {
		this.spajAwal = spajAwal;
	}



}
