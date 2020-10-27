package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Model Class untuk MST_DREK
 * 
 * @author Yusuf
 * @since Nov 5, 2008 (1:32:20 PM)
 */
public class DrekDet implements Serializable {

	private static final long serialVersionUID = -1373214076090162023L;
	private Integer nomor;
	private String no_trx;	
	private Integer tahun_ke;
	private Integer premi_ke;
	private Double jumlah;
	private Integer no_urut;
	private String descr;
	private String jumlahForDisplay;
	private String no_spaj;
	private String payment_id;
	private String no_pre;
	private Date tgl_trx;
	
	public Integer getNomor() {
		return nomor;
	}
	public void setNomor(Integer nomor) {
		this.nomor = nomor;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public Double getJumlah() {
		return jumlah;
	}
	public void setJumlah(Double jumlah) {
		this.jumlah = jumlah;
	}
	public String getNo_trx() {
		return no_trx;
	}
	public void setNo_trx(String no_trx) {
		this.no_trx = no_trx;
	}
	public Integer getNo_urut() {
		return no_urut;
	}
	public void setNo_urut(Integer no_urut) {
		this.no_urut = no_urut;
	}
	public Integer getPremi_ke() {
		return premi_ke;
	}
	public void setPremi_ke(Integer premi_ke) {
		this.premi_ke = premi_ke;
	}
	public Integer getTahun_ke() {
		return tahun_ke;
	}
	public void setTahun_ke(Integer tahun_ke) {
		this.tahun_ke = tahun_ke;
	}
	public String getJumlahForDisplay() {
		return jumlahForDisplay;
	}
	public void setJumlahForDisplay(String jumlahForDisplay) {
		this.jumlahForDisplay = jumlahForDisplay;
	}
	public String getNo_spaj() {
		return no_spaj;
	}
	public void setNo_spaj(String no_spaj) {
		this.no_spaj = no_spaj;
	}
	public String getPayment_id() {
		return payment_id;
	}
	public void setPayment_id(String payment_id) {
		this.payment_id = payment_id;
	}
	public String getNo_pre() {
		return no_pre;
	}
	public void setNo_pre(String no_pre) {
		this.no_pre = no_pre;
	}
	public Date getTgl_trx() {
		return tgl_trx;
	}
	public void setTgl_trx(Date tgl_trx) {
		this.tgl_trx = tgl_trx;
	}
	
}
