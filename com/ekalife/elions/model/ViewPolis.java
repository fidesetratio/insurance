package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ekalife.utils.Common;

public class ViewPolis implements Serializable {
	
	private static final long serialVersionUID = 548789218403408071L;
	
	private Pemegang pemegang;
	private User user;
	private ContactPerson contactPerson;
	private Personal personal;
	private Tertanggung tertanggung;
	private Datausulan dataUsulan;
	private HashMap docPos;
	private String policyNo;
	private String insured;
	private HashMap agen;
	private HashMap referal;
	private ArrayList billing;
	private ArrayList unitLink;
	private ArrayList stableLink;
	private ArrayList detailUnitLink;
	private ArrayList biayaUnitLink;
	private Powersave powerSave;
	private String bukanUnitLink;
	private String proses;
	private String blangko;
	private String tempBlangko;
	private String tempTglSpaj;
	private String tglSpaj;
	private AddressBilling addressbilling;
	private Integer editFlag;
	private Integer flagEditAll; //edit tanpa batasan posisi.
	private Integer info;
	private String suc;
	private ArrayList daftarPremi;
	private ReffBii referral;
	private String virtual_account;
	private Rekening_client rekening;
	private Rekening_auto_debet rekening_autodebet;
	private ArrayList bang;
	private ArrayList select_kurs;
	private String msgInfo;
	private ArrayList detailDTH; //Anta
	private PembayarPremi ppremi;
	private long currentTimeMillis;
	private String receiver_polis;
	private String tgl_receiver;
	
	public long getCurrentTimeMillis() {
		return currentTimeMillis;
	}
	public void setCurrentTimeMillis(long currentTimeMillis) {
		this.currentTimeMillis = currentTimeMillis;
	}
	public ArrayList getStableLink() {
		return stableLink;
	}
	public void setStableLink(List stableLink) {
		this.stableLink = Common.serializableList(stableLink);
	}
	public ArrayList getDaftarPremi() {
		return daftarPremi;
	}
	public void setDaftarPremi(List daftarPremi) {
		this.daftarPremi = Common.serializableList(daftarPremi);
	}
	public String getSuc() {
		return suc;
	}
	public void setSuc(String suc) {
		this.suc = suc;
	}
	public Integer getInfo() {
		return info;
	}
	public void setInfo(Integer info) {
		this.info = info;
	}
	public Integer getEditFlag() {
		return editFlag;
	}
	public void setEditFlag(Integer editFlag) {
		this.editFlag = editFlag;
	}
	public AddressBilling getAddressbilling() {
		return addressbilling;
	}
	public void setAddressbilling(AddressBilling addressbilling) {
		this.addressbilling = addressbilling;
	}
	public String getProses() {
		return proses;
	}
	public void setProses(String proses) {
		this.proses = proses;
	}
	public String getBukanUnitLink() {
		return bukanUnitLink;
	}
	public void setBukanUnitLink(String bukanUnitLink) {
		this.bukanUnitLink = bukanUnitLink;
	}
	public HashMap getAgen() {
		return agen;
	}
	public void setAgen(Map agen) {
		this.agen = Common.serializableMap(agen);
	}
	public ArrayList getBiayaUnitLink() {
		return biayaUnitLink;
	}
	public void setBiayaUnitLink(List biayaUnitLink) {
		this.biayaUnitLink = Common.serializableList(biayaUnitLink);
	}
	public ArrayList getBilling() {
		return billing;
	}
	public void setBilling(List billing) {
		this.billing = Common.serializableList(billing);
	}
	public Datausulan getDataUsulan() {
		return dataUsulan;
	}
	public void setDataUsulan(Datausulan dataUsulan) {
		this.dataUsulan = dataUsulan;
	}
	public ArrayList getDetailUnitLink() {
		return detailUnitLink;
	}
	public void setDetailUnitLink(List detailUnitLink) {
		this.detailUnitLink = Common.serializableList(detailUnitLink);
	}
	public HashMap getDocPos() {
		return docPos;
	}
	public void setDocPos(Map docPos) {
		this.docPos = Common.serializableMap(docPos);
	}
	public String getInsured() {
		return insured;
	}
	public void setInsured(String insured) {
		this.insured = insured;
	}
	public Pemegang getPemegang() {
		return pemegang;
	}
	public void setPemegang(Pemegang pemegang) {
		this.pemegang = pemegang;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public Powersave getPowerSave() {
		return powerSave;
	}
	public void setPowerSave(Powersave powerSave) {
		this.powerSave = powerSave;
	}
	public Tertanggung getTertanggung() {
		return tertanggung;
	}
	public void setTertanggung(Tertanggung tertanggung) {
		this.tertanggung = tertanggung;
	}
	public ArrayList getUnitLink() {
		return unitLink;
	}
	public void setUnitLink(List unitLink) {
		this.unitLink = Common.serializableList(unitLink);
	}
	public String getBlangko() {
		return blangko;
	}
	public void setBlangko(String blangko) {
		this.blangko = blangko;
	}
	public String getTglSpaj() {
		return tglSpaj;
	}
	public void setTglSpaj(String tglSpaj) {
		this.tglSpaj = tglSpaj;
	}
	public String getTempBlangko() {
		return tempBlangko;
	}
	public void setTempBlangko(String tempBlangko) {
		this.tempBlangko = tempBlangko;
	}
	public String getTempTglSpaj() {
		return tempTglSpaj;
	}
	public void setTempTglSpaj(String tempTglSpaj) {
		this.tempTglSpaj = tempTglSpaj;
	}
	public Integer getFlagEditAll() {
		return flagEditAll;
	}
	public void setFlagEditAll(Integer flagEditAll) {
		this.flagEditAll = flagEditAll;
	}
	public ReffBii getReferral() {
		return referral;
	}
	public void setReferral(ReffBii referral) {
		this.referral = referral;
	}
	public Rekening_client getRekening() {
		return rekening;
	}
	public void setRekening(Rekening_client rekening) {
		this.rekening = rekening;
	}
	public String getVirtual_account() {
		return virtual_account;
	}
	public void setVirtual_account(String virtual_account) {
		this.virtual_account = virtual_account;
	}
	public ArrayList getBang() {
		return bang;
	}
	public void setBang(List bang) {
		this.bang = Common.serializableList(bang);
	}
	public ArrayList getSelect_kurs() {
		return select_kurs;
	}
	public void setSelect_kurs(List select_kurs) {
		this.select_kurs = Common.serializableList(select_kurs);
	}
	public String getMsgInfo() {
		return msgInfo;
	}
	public void setMsgInfo(String msgInfo) {
		this.msgInfo = msgInfo;
	}
	public ContactPerson getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(ContactPerson contactPerson) {
		this.contactPerson = contactPerson;
	}
	public Personal getPersonal() {
		return personal;
	}
	public void setPersonal(Personal personal) {
		this.personal = personal;
	}

	public HashMap getReferal() {
		return referal;
	}
	public void setReferal(Map referal) {
		this.referal = Common.serializableMap(referal);
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public ArrayList getDetailDTH() {
		return detailDTH;
	}
	public void setDetailDTH(List detailDTH) {
		this.detailDTH = Common.serializableList(detailDTH);
	}
	public Rekening_auto_debet getRekening_autodebet() {
		return rekening_autodebet;
	}
	public void setRekening_autodebet(Rekening_auto_debet rekening_autodebet) {
		this.rekening_autodebet = rekening_autodebet;
	}
	
	public PembayarPremi getPpremi() {
		return ppremi;
	}
	public void setPpremi(PembayarPremi ppremi) {
		this.ppremi = ppremi;
	}
	public String getReceiver_polis() {
		return receiver_polis;
	}
	public void setReceiver_polis(String receiver_polis) {
		this.receiver_polis = receiver_polis;
	}
	public String getTgl_receiver() {
		return tgl_receiver;
	}
	public void setTgl_receiver(String tgl_receiver) {
		this.tgl_receiver = tgl_receiver;
	}
	
}
