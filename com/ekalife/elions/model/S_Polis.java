package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class S_Polis implements Serializable {

	private static final long serialVersionUID = 1L;
	
private String no_spaj;
private Integer insured_no;
private Integer type_bisnis;
private Integer[] pos_doc;
private String no_polis;
private Date beg_date;
private Date end_date;
private String[] agent_id;
private Integer[] agent_tb;
private Integer[] agent_lev;
private Integer[] agent_levcom;
private Integer flag;
private String client_id;
private String policy_holder;
private Integer sex;
private Integer umur;
private Integer umur_beasiswa;
private Integer umur_p;
private Integer medis;
private Integer bisnis_id;
private Integer bisnis_no;
private String kurs_id;
private Integer lama_tanggung;
private Integer lama_bayar;
private Integer cara_bayar;
private Integer tahun_ke;
private Double[] up_now;
private Double[] sar_now;
private Double[] up_life;
private Double[] up_pa;
private Double[] up_pk;
private Double[] up_ssh;
private Double[] up_simp;
private Double[] uw_limit;
private Double extra;
private Double[] em_max;
private Double[] sum_reas;
private String bill_no;
private Integer kelas;
private Double premi;
private Integer[] agent_sbm;
private Integer[] agent_bm;
public Integer[] getAgent_bm() {
	return agent_bm;
}
public void setAgent_bm(Integer[] agent_bm) {
	this.agent_bm = agent_bm;
}
public String[] getAgent_id() {
	return agent_id;
}
public void setAgent_id(String[] agent_id) {
	this.agent_id = agent_id;
}
public Integer[] getAgent_lev() {
	return agent_lev;
}
public void setAgent_lev(Integer[] agent_lev) {
	this.agent_lev = agent_lev;
}
public Integer[] getAgent_levcom() {
	return agent_levcom;
}
public void setAgent_levcom(Integer[] agent_levcom) {
	this.agent_levcom = agent_levcom;
}
public Integer[] getAgent_sbm() {
	return agent_sbm;
}
public void setAgent_sbm(Integer[] agent_sbm) {
	this.agent_sbm = agent_sbm;
}
public Integer[] getAgent_tb() {
	return agent_tb;
}
public void setAgent_tb(Integer[] agent_tb) {
	this.agent_tb = agent_tb;
}
public Date getBeg_date() {
	return beg_date;
}
public void setBeg_date(Date beg_date) {
	this.beg_date = beg_date;
}
public String getBill_no() {
	return bill_no;
}
public void setBill_no(String bill_no) {
	this.bill_no = bill_no;
}
public Integer getBisnis_id() {
	return bisnis_id;
}
public void setBisnis_id(Integer bisnis_id) {
	this.bisnis_id = bisnis_id;
}
public Integer getBisnis_no() {
	return bisnis_no;
}
public void setBisnis_no(Integer bisnis_no) {
	this.bisnis_no = bisnis_no;
}
public Integer getCara_bayar() {
	return cara_bayar;
}
public void setCara_bayar(Integer cara_bayar) {
	this.cara_bayar = cara_bayar;
}
public String getClient_id() {
	return client_id;
}
public void setClient_id(String client_id) {
	this.client_id = client_id;
}
public Double[] getEm_max() {
	return em_max;
}
public void setEm_max(Double[] em_max) {
	this.em_max = em_max;
}
public Date getEnd_date() {
	return end_date;
}
public void setEnd_date(Date end_date) {
	this.end_date = end_date;
}
public Double getExtra() {
	return extra;
}
public void setExtra(Double extra) {
	this.extra = extra;
}
public Integer getFlag() {
	return flag;
}
public void setFlag(Integer flag) {
	this.flag = flag;
}
public Integer getInsured_no() {
	return insured_no;
}
public void setInsured_no(Integer insured_no) {
	this.insured_no = insured_no;
}
public Integer getKelas() {
	return kelas;
}
public void setKelas(Integer kelas) {
	this.kelas = kelas;
}
public String getKurs_id() {
	return kurs_id;
}
public void setKurs_id(String kurs_id) {
	this.kurs_id = kurs_id;
}
public Integer getLama_bayar() {
	return lama_bayar;
}
public void setLama_bayar(Integer lama_bayar) {
	this.lama_bayar = lama_bayar;
}
public Integer getLama_tanggung() {
	return lama_tanggung;
}
public void setLama_tanggung(Integer lama_tanggung) {
	this.lama_tanggung = lama_tanggung;
}
public Integer getMedis() {
	return medis;
}
public void setMedis(Integer medis) {
	this.medis = medis;
}
public String getNo_polis() {
	return no_polis;
}
public void setNo_polis(String no_polis) {
	this.no_polis = no_polis;
}
public String getNo_spaj() {
	return no_spaj;
}
public void setNo_spaj(String no_spaj) {
	this.no_spaj = no_spaj;
}
public String getPolicy_holder() {
	return policy_holder;
}
public void setPolicy_holder(String policy_holder) {
	this.policy_holder = policy_holder;
}
public Integer[] getPos_doc() {
	return pos_doc;
}
public void setPos_doc(Integer[] pos_doc) {
	this.pos_doc = pos_doc;
}
public Double getPremi() {
	return premi;
}
public void setPremi(Double premi) {
	this.premi = premi;
}
public Double[] getSar_now() {
	return sar_now;
}
public void setSar_now(Double[] sar_now) {
	this.sar_now = sar_now;
}
public Integer getSex() {
	return sex;
}
public void setSex(Integer sex) {
	this.sex = sex;
}
public Double[] getSum_reas() {
	return sum_reas;
}
public void setSum_reas(Double[] sum_reas) {
	this.sum_reas = sum_reas;
}
public Integer getTahun_ke() {
	return tahun_ke;
}
public void setTahun_ke(Integer tahun_ke) {
	this.tahun_ke = tahun_ke;
}
public Integer getType_bisnis() {
	return type_bisnis;
}
public void setType_bisnis(Integer type_bisnis) {
	this.type_bisnis = type_bisnis;
}
public Integer getUmur() {
	return umur;
}
public void setUmur(Integer umur) {
	this.umur = umur;
}
public Integer getUmur_beasiswa() {
	return umur_beasiswa;
}
public void setUmur_beasiswa(Integer umur_beasiswa) {
	this.umur_beasiswa = umur_beasiswa;
}
public Integer getUmur_p() {
	return umur_p;
}
public void setUmur_p(Integer umur_p) {
	this.umur_p = umur_p;
}
public Double[] getUp_life() {
	return up_life;
}
public void setUp_life(Double[] up_life) {
	this.up_life = up_life;
}
public Double[] getUp_now() {
	return up_now;
}
public void setUp_now(Double[] up_now) {
	this.up_now = up_now;
}
public Double[] getUp_pa() {
	return up_pa;
}
public void setUp_pa(Double[] up_pa) {
	this.up_pa = up_pa;
}
public Double[] getUp_pk() {
	return up_pk;
}
public void setUp_pk(Double[] up_pk) {
	this.up_pk = up_pk;
}
public Double[] getUp_simp() {
	return up_simp;
}
public void setUp_simp(Double[] up_simp) {
	this.up_simp = up_simp;
}
public Double[] getUp_ssh() {
	return up_ssh;
}
public void setUp_ssh(Double[] up_ssh) {
	this.up_ssh = up_ssh;
}
public Double[] getUw_limit() {
	return uw_limit;
}
public void setUw_limit(Double[] uw_limit) {
	this.uw_limit = uw_limit;
}




}
