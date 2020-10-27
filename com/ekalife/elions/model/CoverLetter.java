package com.ekalife.elions.model;

import java.io.Serializable;

public class CoverLetter implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer cek;
	private String reg_spaj;
	private String no_polis;
	private String pemegangpolis;
	private String tertanggung;
	private String produk;
	private String admininput;
	private Integer cabanginput;
	private String simascard;
	private String admedika;
	private String no_resi;
	private String tgl_printpolis;
	private String keterangan;
	private String transhistory;
	
	public Integer getCek() {
		return cek;
	}
	public void setCek(Integer cek) {
		this.cek = cek;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getNo_polis() {
		return no_polis;
	}
	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}
	public String getPemegangpolis() {
		return pemegangpolis;
	}
	public void setPemegangpolis(String pemegangpolis) {
		this.pemegangpolis = pemegangpolis;
	}
	public String getTertanggung() {
		return tertanggung;
	}
	public void setTertanggung(String tertanggung) {
		this.tertanggung = tertanggung;
	}
	public String getProduk() {
		return produk;
	}
	public void setProduk(String produk) {
		this.produk = produk;
	}
	public String getAdmininput() {
		return admininput;
	}
	public void setAdmininput(String admininput) {
		this.admininput = admininput;
	}
	public Integer getCabanginput() {
		return cabanginput;
	}
	public void setCabanginput(Integer cabanginput) {
		this.cabanginput = cabanginput;
	}
	public String getSimascard() {
		return simascard;
	}
	public void setSimascard(String simascard) {
		this.simascard = simascard;
	}
	public String getAdmedika() {
		return admedika;
	}
	public void setAdmedika(String admedika) {
		this.admedika = admedika;
	}
	public String getNo_resi() {
		return no_resi;
	}
	public void setNo_resi(String no_resi) {
		this.no_resi = no_resi;
	}
	public String getTgl_printpolis() {
		return tgl_printpolis;
	}
	public void setTgl_printpolis(String tgl_printpolis) {
		this.tgl_printpolis = tgl_printpolis;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public String getTranshistory() {
		return transhistory;
	}
	public void setTranshistory(String transhistory) {
		this.transhistory = transhistory;
	}
}
