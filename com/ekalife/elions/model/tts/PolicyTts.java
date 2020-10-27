package com.ekalife.elions.model.tts;

import java.io.Serializable;
import java.util.Date;

public class PolicyTts implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer no_urut;
	private Integer tahun_ke;
	private Integer premi_ke;
	private String mst_no;
	private String mst_no_polis;
	private String mst_no_polis_formated;
	private String lku_id_tahapan;
	private String lku_symbol_tahapan;
	private Integer mst_periode;
	private String mst_kurs;
	private Double mst_jumlah;
	private Double mst_jumlah_byr_rp;
	private Double mst_jumlah_byr_dlr;
	private Double mstah_jumlah;
	private Date beg_date;
	private Date end_date;
	private Date due_date;
	private String s_beg_date;
	private String s_end_date;
	private String s_due_date;
	private String lku_symbol;
	private Double mst_premium;
	private Double lsbun_bunga;
	private Double mst_discount;
	private Integer mst_jum_hari;
	private String pil;
	private Integer size;
	private String status;
	private String mcl_first;
	private Double msbi_stamp;
	private String lku_id;
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Double getMsbi_stamp() {
		return msbi_stamp;
	}
	public void setMsbi_stamp(Double msbi_stamp) {
		this.msbi_stamp = msbi_stamp;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public Date getBeg_date() {
		return beg_date;
	}
	public void setBeg_date(Date beg_date) {
		this.beg_date = beg_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public String getLku_symbol() {
		return lku_symbol;
	}
	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
	public Double getLsbun_bunga() {
		return lsbun_bunga;
	}
	public void setLsbun_bunga(Double lsbun_bunga) {
		this.lsbun_bunga = lsbun_bunga;
	}
	public Double getMst_discount() {
		return mst_discount;
	}
	public void setMst_discount(Double mst_discount) {
		this.mst_discount = mst_discount;
	}
	public Integer getMst_jum_hari() {
		return mst_jum_hari;
	}
	public void setMst_jum_hari(Integer mst_jum_hari) {
		this.mst_jum_hari = mst_jum_hari;
	}
	public Double getMst_jumlah() {
		return mst_jumlah;
	}
	public void setMst_jumlah(Double mst_jumlah) {
		this.mst_jumlah = mst_jumlah;
	}
	public Double getMst_jumlah_byr_dlr() {
		return mst_jumlah_byr_dlr;
	}
	public void setMst_jumlah_byr_dlr(Double mst_jumlah_byr_dlr) {
		this.mst_jumlah_byr_dlr = mst_jumlah_byr_dlr;
	}
	public Double getMst_jumlah_byr_rp() {
		return mst_jumlah_byr_rp;
	}
	public void setMst_jumlah_byr_rp(Double mst_jumlah_byr_rp) {
		this.mst_jumlah_byr_rp = mst_jumlah_byr_rp;
	}
	public String getMst_kurs() {
		return mst_kurs;
	}
	public void setMst_kurs(String mst_kurs) {
		this.mst_kurs = mst_kurs;
	}
	public String getMst_no() {
		return mst_no;
	}
	public void setMst_no(String mst_no) {
		this.mst_no = mst_no;
	}
	public String getMst_no_polis() {
		return mst_no_polis;
	}
	public void setMst_no_polis(String mst_no_polis) {
		this.mst_no_polis = mst_no_polis;
	}
	public String getMst_no_polis_formated() {
		return mst_no_polis_formated;
	}
	public void setMst_no_polis_formated(String mst_no_polis_formated) {
		this.mst_no_polis_formated = mst_no_polis_formated;
	}
	public Integer getMst_periode() {
		return mst_periode;
	}
	public void setMst_periode(Integer mst_periode) {
		this.mst_periode = mst_periode;
	}
	public Double getMst_premium() {
		return mst_premium;
	}
	public void setMst_premium(Double mst_premium) {
		this.mst_premium = mst_premium;
	}
	public Integer getNo_urut() {
		return no_urut;
	}
	public void setNo_urut(Integer no_urut) {
		this.no_urut = no_urut;
	}
	public String getPil() {
		return pil;
	}
	public void setPil(String pil) {
		this.pil = pil;
	}
	public Integer getPremi_ke() {
		return premi_ke;
	}
	public void setPremi_ke(Integer premi_ke) {
		this.premi_ke = premi_ke;
	}
	public String getS_beg_date() {
		return s_beg_date;
	}
	public void setS_beg_date(String s_beg_date) {
		this.s_beg_date = s_beg_date;
	}
	public String getS_end_date() {
		return s_end_date;
	}
	public void setS_end_date(String s_end_date) {
		this.s_end_date = s_end_date;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getTahun_ke() {
		return tahun_ke;
	}
	public void setTahun_ke(Integer tahun_ke) {
		this.tahun_ke = tahun_ke;
	}
	public Double getMstah_jumlah() {
		return mstah_jumlah;
	}
	public void setMstah_jumlah(Double mstah_jumlah) {
		this.mstah_jumlah = mstah_jumlah;
	}
	public String getLku_id_tahapan() {
		return lku_id_tahapan;
	}
	public void setLku_id_tahapan(String lku_id_tahapan) {
		this.lku_id_tahapan = lku_id_tahapan;
	}
	public String getLku_symbol_tahapan() {
		return lku_symbol_tahapan;
	}
	public void setLku_symbol_tahapan(String lku_symbol_tahapan) {
		this.lku_symbol_tahapan = lku_symbol_tahapan;
	}
	public Date getDue_date() {
		return due_date;
	}
	public void setDue_date(Date due_date) {
		this.due_date = due_date;
	}
	public String getS_due_date() {
		return s_due_date;
	}
	public void setS_due_date(String s_due_date) {
		this.s_due_date = s_due_date;
	}
	
}
