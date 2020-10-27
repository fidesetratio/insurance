package com.ekalife.elions.model;

import java.io.Serializable;


public class Cmdeditbac implements Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = -714925359146764786L;
private Pemegang pemegang;
private ContactPerson contactPerson;
private Personal personal;
private Tertanggung tertanggung;
private Datausulan datausulan;
private Agen agen;
private AddressBilling addressbilling;
private InvestasiUtama investasiutama;
private Biayainvestasi biayainvestasi;
private Rekening_client rekening_client;
private Account_recur account_recur;
private Powersave powersave;
private Employee employee;
private History history;
private TransUlink transulink;
private User currentUser;
private String pesan;
private String no_pb;
private Integer flag_gmanual_spaj;
private Integer refreshObject;//0 not refresh Object, 1 refresh Object
private String pathFileTemp;
private String fileConfirm;
private MedQuest medQuest;
private MedQuest_tertanggung medQuest_tertanggung;
private MedQuest_tambah medQuest_tambah;
private MedQuest_tambah2 medQuest_tambah2;
private MedQuest_tambah3 medQuest_tambah3;
private MedQuest_tambah4 medQuest_tambah4;
private MedQuest_tambah5 medQuest_tambah5;
private SumberKyc sumberKyc;
private Datarider datarider;
private Boolean newPlan;
private Broker broker;
private Endors endors;
private String no_temp;
private PembayarPremi pembayarPremi;
private String flag_special_case;


public Boolean getNewPlan() {
	return newPlan;
}
public void setNewPlan(Boolean newPlan) {
	this.newPlan = newPlan;
}
public MedQuest_tambah4 getMedQuest_tambah4() {
	return medQuest_tambah4;
}
public void setMedQuest_tambah4(MedQuest_tambah4 medQuest_tambah4) {
	this.medQuest_tambah4 = medQuest_tambah4;
}
public MedQuest_tambah5 getMedQuest_tambah5() {
	return medQuest_tambah5;
}
public void setMedQuest_tambah5(MedQuest_tambah5 medQuest_tambah5) {
	this.medQuest_tambah5 = medQuest_tambah5;
}
public MedQuest_tertanggung getMedQuest_tertanggung() {
	return medQuest_tertanggung;
}
public void setMedQuest_tertanggung(MedQuest_tertanggung medQuest_tertanggung) {
	this.medQuest_tertanggung = medQuest_tertanggung;
}
public MedQuest_tambah getMedQuest_tambah() {
	return medQuest_tambah;
}
public void setMedQuest_tambah(MedQuest_tambah medQuest_tambah) {
	this.medQuest_tambah = medQuest_tambah;
}
public MedQuest_tambah2 getMedQuest_tambah2() {
	return medQuest_tambah2;
}
public void setMedQuest_tambah2(MedQuest_tambah2 medQuest_tambah2) {
	this.medQuest_tambah2 = medQuest_tambah2;
}
public MedQuest_tambah3 getMedQuest_tambah3() {
	return medQuest_tambah3;
}
public void setMedQuest_tambah3(MedQuest_tambah3 medQuest_tambah3) {
	this.medQuest_tambah3 = medQuest_tambah3;
}
public MedQuest getMedQuest() {
	return medQuest;
}
public void setMedQuest(MedQuest medQuest) {
	this.medQuest = medQuest;
}
public String getFileConfirm() {
	return fileConfirm;
}
public void setFileConfirm(String fileConfirm) {
	this.fileConfirm = fileConfirm;
}
public String getPathFileTemp() {
	return pathFileTemp;
}
public void setPathFileTemp(String pathFileTemp) {
	this.pathFileTemp = pathFileTemp;
}
public Integer getRefreshObject() {
	return refreshObject;
}
public void setRefreshObject(Integer refreshObject) {
	this.refreshObject = refreshObject;
}
public Integer getFlag_gmanual_spaj() {
	return flag_gmanual_spaj;
}
public void setFlag_gmanual_spaj(Integer flag_gmanual_spaj) {
	this.flag_gmanual_spaj = flag_gmanual_spaj;
}
public String getNo_pb() {
	return no_pb;
}
public void setNo_pb(String no_pb) {
	this.no_pb = no_pb;
}
public String getPesan() {
	return pesan;
}
public void setPesan(String pesan) {
	this.pesan = pesan;
}
public TransUlink getTransulink() {
	return transulink;
}
public void setTransulink(TransUlink transulink) {
	this.transulink = transulink;
}
public History getHistory() {
	return history;
}
public void setHistory(History history) {
	this.history = history;
}
public Account_recur getAccount_recur() {
	return account_recur;
}
public void setAccount_recur(Account_recur account_recur) {
	this.account_recur = account_recur;
}
public AddressBilling getAddressbilling() {
	return addressbilling;
}
public void setAddressbilling(AddressBilling addressbilling) {
	this.addressbilling = addressbilling;
}
public Agen getAgen() {
	return agen;
}
public void setAgen(Agen agen) {
	this.agen = agen;
}
public Biayainvestasi getBiayainvestasi() {
	return biayainvestasi;
}
public void setBiayainvestasi(Biayainvestasi biayainvestasi) {
	this.biayainvestasi = biayainvestasi;
}
public Datausulan getDatausulan() {
	return datausulan;
}
public void setDatausulan(Datausulan datausulan) {
	this.datausulan = datausulan;
}
public Employee getEmployee() {
	return employee;
}
public void setEmployee(Employee employee) {
	this.employee = employee;
}
public InvestasiUtama getInvestasiutama() {
	return investasiutama;
}
public void setInvestasiutama(InvestasiUtama investasiutama) {
	this.investasiutama = investasiutama;
}
public Pemegang getPemegang() {
	return pemegang;
}
public void setPemegang(Pemegang pemegang) {
	this.pemegang = pemegang;
}
public Powersave getPowersave() {
	return powersave;
}
public void setPowersave(Powersave powersave) {
	this.powersave = powersave;
}
public Rekening_client getRekening_client() {
	return rekening_client;
}
public void setRekening_client(Rekening_client rekening_client) {
	this.rekening_client = rekening_client;
}
public Tertanggung getTertanggung() {
	return tertanggung;
}
public void setTertanggung(Tertanggung tertanggung) {
	this.tertanggung = tertanggung;
}
public User getCurrentUser() {
	return currentUser;
}
public void setCurrentUser(User currentUser) {
	this.currentUser = currentUser;
}
public Personal getPersonal() {
	return personal;
}
public void setPersonal(Personal personal) {
	this.personal = personal;
}
public ContactPerson getContactPerson() {
	return contactPerson;
}
public void setContactPerson(ContactPerson contactPerson) {
	this.contactPerson = contactPerson;
}
public Datarider getDatarider(){
	return datarider;
}
public void setDatarider(Datarider datarider){
	this.datarider = datarider;
}
public SumberKyc getSumberKyc(){
	return sumberKyc;
}
public void setSumberKyc(SumberKyc sumberKyc){
	this.sumberKyc = sumberKyc;
}
public Broker getBroker() {
	return broker;
}
public void setBroker(Broker broker) {
	this.broker = broker;
}
public Endors getEndors() {
	return endors;
}
public void setendors(Endors endors) {
	this.endors = endors;
}
public String getNo_temp() {
	return no_temp;
}
public void setNo_temp(String no_temp) {
	this.no_temp = no_temp;
}
public PembayarPremi getPembayarPremi() {
	return pembayarPremi;
}
public void setPembayarPremi(PembayarPremi pembayarPremi) {
	this.pembayarPremi = pembayarPremi;
}
public String getFlag_special_case() {
	return flag_special_case;
}
public void setFlag_special_case(String flag_special_case) {
	this.flag_special_case = flag_special_case;
}
}
