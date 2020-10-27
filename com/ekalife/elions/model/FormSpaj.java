package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Model object untuk tabel EKA.MST_FORM dan relasinya,
 * digunakan di modul sistem kontrol spaj 
 * (package com.ekalife.elions.web.bas)
 * 
 * @author Yusuf Sutarko
 * @since Feb 23, 2007 (9:22:04 AM)
 */
public class FormSpaj implements Serializable {
                  
	/**
	 * 
	 */
	private static final long serialVersionUID = -2484710230715669184L;
	private String msf_id;
	private Integer mss_jenis; //0=saldo spaj cabang, 1=saldo spaj per agen, 2=saldo kertas polis
	private String lca_id;
	private String lwk_id;
	private String lsrg_id;
	private Integer lsjs_id;
	private Integer msab_id;
	private Integer msf_amount;
	private Integer msf_amount_req;
	private String start_no_blanko;
	private String end_no_blanko;
	private String no_blanko_req;
	private Integer posisi; //0 / null = belum di approve, 1 = approve, 2 = reject, 3 = cancel, 4 = sent, 5 = received
	private String pos;
	private Integer lus_id;
	//private Integer flag; //1=cabang 2=agen
	private Integer status_permintaan; //0 = permanen, 1 = titipan
	private String bmi_id;
	private Integer trav_ins_type;
	
	private String lsjs_desc;
	private String lsjs_prefix;
	private String status_form;
	private String warna;
	private String lca_nama;
	private String mcl_first;
	
	private Agen agen;

	private Date msfh_dt;
	private String msfh_desc;
	private Integer selisih;
	private String msag_id;
	private String nama_agen;
	private String bukti_pembayaran;
	
	//untuk brosur
	private String busdev; 
	private Integer jn_brosur;

	public Integer getJn_brosur() {
		return jn_brosur;
	}
	public void setJn_brosur(Integer jn_brosur) {
		this.jn_brosur = jn_brosur;
	}
	public String getMsag_id() {
		return msag_id;
	}
	public void setMsag_id(String msag_id) {
		this.msag_id = msag_id;
	}
	public String getNo_blanko_req() {
		return no_blanko_req;
	}
	public void setNo_blanko_req(String no_blanko_req) {
		this.no_blanko_req = no_blanko_req;
	}
	public String getMsfh_desc() {
		return msfh_desc;
	}
	public void setMsfh_desc(String msfh_desc) {
		this.msfh_desc = msfh_desc;
	}
	public Date getMsfh_dt() {
		return msfh_dt;
	}
	public void setMsfh_dt(Date msfh_dt) {
		this.msfh_dt = msfh_dt;
	}
	public Integer getSelisih() {
		return selisih;
	}
	public void setSelisih(Integer selisih) {
		this.selisih = selisih;
	}
	public Agen getAgen() {
		return agen;
	}
	public void setAgen(Agen agen) {
		this.agen = agen;
	}
	public Integer getStatus_permintaan() {
		return status_permintaan;
	}
	public void setStatus_permintaan(Integer status_permintaan) {
		this.status_permintaan = status_permintaan;
	}
	public String getLca_nama() {
		return lca_nama;
	}
	public void setLca_nama(String lca_nama) {
		this.lca_nama = lca_nama;
	}
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
	public String getLsjs_desc() {
		return lsjs_desc;
	}
	public void setLsjs_desc(String lsjs_desc) {
		this.lsjs_desc = lsjs_desc;
	}
	public Integer getLsjs_id() {
		return lsjs_id;
	}
	public void setLsjs_id(Integer lsjs_id) {
		this.lsjs_id = lsjs_id;
	}
	public String getLsjs_prefix() {
		return lsjs_prefix;
	}
	public void setLsjs_prefix(String lsjs_prefix) {
		this.lsjs_prefix = lsjs_prefix;
	}
	public Integer getMsab_id() {
		return msab_id;
	}
	public void setMsab_id(Integer msab_id) {
		this.msab_id = msab_id;
	}
	public Integer getMsf_amount() {
		return msf_amount;
	}
	public void setMsf_amount(Integer msf_amount) {
		this.msf_amount = msf_amount;
	}
	public Integer getMsf_amount_req() {
		return msf_amount_req;
	}
	public void setMsf_amount_req(Integer msf_amount_req) {
		this.msf_amount_req = msf_amount_req;
	}
	public String getMsf_id() {
		return msf_id;
	}
	public void setMsf_id(String msf_id) {
		this.msf_id = msf_id;
	}
	public Integer getMss_jenis() {
		return mss_jenis;
	}
	public void setMss_jenis(Integer mss_jenis) {
		this.mss_jenis = mss_jenis;
	}
	public Integer getPosisi() {
		return posisi;
	}
	public void setPosisi(Integer posisi) {
		this.posisi = posisi;
	}
	public String getStatus_form() {
		return status_form;
	}
	public void setStatus_form(String status_form) {
		this.status_form = status_form;
	}
	public String getWarna() {
		return warna;
	}
	public void setWarna(String warna) {
		this.warna = warna;
	}
	public String getEnd_no_blanko() {
		return end_no_blanko;
	}
	public void setEnd_no_blanko(String end_no_blanko) {
		this.end_no_blanko = end_no_blanko;
	}
	public String getStart_no_blanko() {
		return start_no_blanko;
	}
	public void setStart_no_blanko(String start_no_blanko) {
		this.start_no_blanko = start_no_blanko;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
//	public Integer getFlag() {
//		return flag;
//	}
//	public void setFlag(Integer flag) {
//		this.flag = flag;
//	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getLsrg_id() {
		return lsrg_id;
	}
	public void setLsrg_id(String lsrg_id) {
		this.lsrg_id = lsrg_id;
	}
	public String getLwk_id() {
		return lwk_id;
	}
	public void setLwk_id(String lwk_id) {
		this.lwk_id = lwk_id;
	}
	public String getNama_agen() {
		return nama_agen;
	}
	public void setNama_agen(String nama_agen) {
		this.nama_agen = nama_agen;
	}
	public String getBmi_id() {
		return bmi_id;
	}
	public void setBmi_id(String bmi_id) {
		this.bmi_id = bmi_id;
	}
	public Integer getTrav_ins_type() {
		return trav_ins_type;
	}
	public void setTrav_ins_type(Integer trav_ins_type) {
		this.trav_ins_type = trav_ins_type;
	}
	public String getBukti_pembayaran() {
		return bukti_pembayaran;
	}
	public void setBukti_pembayaran(String buktiPembayaran) {
		bukti_pembayaran = buktiPembayaran;
	}
	public String getBusdev() {
		return busdev;
	}
	public void setBusdev(String busdev) {
		this.busdev = busdev;
	}
}