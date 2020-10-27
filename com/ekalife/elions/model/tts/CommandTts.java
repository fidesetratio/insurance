package com.ekalife.elions.model.tts;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CommandTts implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List lsPolis;
	private List lsPembayaran;
	private List lsPil;
	private Tahapan tahapan;
	private String kd_cabang;
	private String nama;//semetara untuk inputTtsFormTts (nanti klo dah ok di buang aja) 
	private String nama_pembayar;
	private String nama_cabang;
	private String nama_admin;
	private String keterangan;
	private String mstNoBatal;
	private String mstNoNew;
	private Date tglRk;
	private Date tglSetor;
	private String lkuId;
	private String lsjbId;
	private Integer size;
	private Integer size2;
	private Integer proses;
	private String mst_no;
	private String s_tgl_rk;
	private String s_tgl_setor;
//	private Double GtJumlahRp;
	private Double GtBayarRp;
//	private Double GtBungaRp;
//	private Double GtJumlahDlr;
	private Double GtBayarDlr;
	private Double GtTahapan;
//	private Double GtBungaDlr;
	private Double GtJlhHari;
	private String flag;
	private Integer info; 
	private String desc;
	private String alasanBatal;
	private Integer editTglSetor;
	private String noTelp;
	private String limit;
	
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public Tahapan getTahapan() {
		return tahapan;
	}
	public void setTahapan(Tahapan tahapan) {
		this.tahapan = tahapan;
	}
	public String getAlasanBatal() {
		return alasanBatal;
	}
	public void setAlasanBatal(String alasanBatal) {
		this.alasanBatal = alasanBatal;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Integer getEditTglSetor() {
		return editTglSetor;
	}
	public void setEditTglSetor(Integer editTglSetor) {
		this.editTglSetor = editTglSetor;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Double getGtBayarDlr() {
		return GtBayarDlr;
	}
	public void setGtBayarDlr(Double gtBayarDlr) {
		GtBayarDlr = gtBayarDlr;
	}
	public Double getGtBayarRp() {
		return GtBayarRp;
	}
	public void setGtBayarRp(Double gtBayarRp) {
		GtBayarRp = gtBayarRp;
	}
	public Double getGtJlhHari() {
		return GtJlhHari;
	}
	public void setGtJlhHari(Double gtJlhHari) {
		GtJlhHari = gtJlhHari;
	}
	public Integer getInfo() {
		return info;
	}
	public void setInfo(Integer info) {
		this.info = info;
	}
	public String getKd_cabang() {
		return kd_cabang;
	}
	public void setKd_cabang(String kd_cabang) {
		this.kd_cabang = kd_cabang;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public String getLkuId() {
		return lkuId;
	}
	public void setLkuId(String lkuId) {
		this.lkuId = lkuId;
	}
	public String getLsjbId() {
		return lsjbId;
	}
	public void setLsjbId(String lsjbId) {
		this.lsjbId = lsjbId;
	}
	public List getLsPembayaran() {
		return lsPembayaran;
	}
	public void setLsPembayaran(List lsPembayaran) {
		this.lsPembayaran = lsPembayaran;
	}
	public List getLsPil() {
		return lsPil;
	}
	public void setLsPil(List lsPil) {
		this.lsPil = lsPil;
	}
	public List getLsPolis() {
		return lsPolis;
	}
	public void setLsPolis(List lsPolis) {
		this.lsPolis = lsPolis;
	}
	public String getMst_no() {
		return mst_no;
	}
	public void setMst_no(String mst_no) {
		this.mst_no = mst_no;
	}
	public String getMstNoBatal() {
		return mstNoBatal;
	}
	public void setMstNoBatal(String mstNoBatal) {
		this.mstNoBatal = mstNoBatal;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getNama_admin() {
		return nama_admin;
	}
	public void setNama_admin(String nama_admin) {
		this.nama_admin = nama_admin;
	}
	public String getNama_cabang() {
		return nama_cabang;
	}
	public void setNama_cabang(String nama_cabang) {
		this.nama_cabang = nama_cabang;
	}
	public String getNama_pembayar() {
		return nama_pembayar;
	}
	public void setNama_pembayar(String nama_pembayar) {
		this.nama_pembayar = nama_pembayar;
	}
	public Integer getProses() {
		return proses;
	}
	public void setProses(Integer proses) {
		this.proses = proses;
	}
	public String getS_tgl_rk() {
		return s_tgl_rk;
	}
	public void setS_tgl_rk(String s_tgl_rk) {
		this.s_tgl_rk = s_tgl_rk;
	}
	public String getS_tgl_setor() {
		return s_tgl_setor;
	}
	public void setS_tgl_setor(String s_tgl_setor) {
		this.s_tgl_setor = s_tgl_setor;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Integer getSize2() {
		return size2;
	}
	public void setSize2(Integer size2) {
		this.size2 = size2;
	}
	public Date getTglRk() {
		return tglRk;
	}
	public void setTglRk(Date tglRk) {
		this.tglRk = tglRk;
	}
	public Date getTglSetor() {
		return tglSetor;
	}
	public void setTglSetor(Date tglSetor) {
		this.tglSetor = tglSetor;
	}
	public Double getGtTahapan() {
		return GtTahapan;
	}
	public void setGtTahapan(Double gtTahapan) {
		GtTahapan = gtTahapan;
	}
	public String getMstNoNew() {
		return mstNoNew;
	}
	public void setMstNoNew(String mstNoNew) {
		this.mstNoNew = mstNoNew;
	}
	public String getNoTelp() {
		return noTelp;
	}
	public void setNoTelp(String noTelp) {
		this.noTelp = noTelp;
	}

	
}
