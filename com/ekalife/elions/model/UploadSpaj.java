package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.List;

public class UploadSpaj implements Serializable {


	private static final long serialVersionUID = 1578380240909721352L;
	Pemegang pemegang;
	AddressNew address;
	Policy polis;
	Insured insured;
	AddressBilling addressBilling;
	Product product;
	DepositPremium ttp;
	List<Benefeciary> daftarBenefeciary;
	
	public DepositPremium getTtp() {
		return ttp;
	}
	public void setTtp(DepositPremium ttp) {
		this.ttp = ttp;
	}
	public AddressNew getAddress() {
		return address;
	}
	public void setAddress(AddressNew address) {
		this.address = address;
	}
	public AddressBilling getAddressBilling() {
		return addressBilling;
	}
	public void setAddressBilling(AddressBilling addressBilling) {
		this.addressBilling = addressBilling;
	}
	public List<Benefeciary> getDaftarBenefeciary() {
		return daftarBenefeciary;
	}
	public void setDaftarBenefeciary(List<Benefeciary> daftarBenefeciary) {
		this.daftarBenefeciary = daftarBenefeciary;
	}
	public Insured getInsured() {
		return insured;
	}
	public void setInsured(Insured insured) {
		this.insured = insured;
	}
	public Pemegang getPemegang() {
		return pemegang;
	}
	public void setPemegang(Pemegang pemegang) {
		this.pemegang = pemegang;
	}
	public Policy getPolis() {
		return polis;
	}
	public void setPolis(Policy polis) {
		this.polis = polis;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

}
