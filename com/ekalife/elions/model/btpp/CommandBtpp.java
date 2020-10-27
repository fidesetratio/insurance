package com.ekalife.elions.model.btpp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.ekalife.elions.model.Agen;

public class CommandBtpp implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Btpp btpp;
	private List lsBtpp;
	private List lsPolis;
	private List lsAgent;
	private List lsPembayaran;
	private List lsPil;
	private Tahapan tahapan;
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
	private String limit;
	private Double GtTahapan;
//	private Double GtBungaDlr;
	private Double GtJlhHari;
	private String flag;
	private Integer info; 
	private String desc;
	private String alasanBatal;
	private String query;
	private String editTglSetor;
	private Agen agen;
	private Date prde_byr_awal;
	private Date prde_byr_akhr;
	private String sprde_byr_awal;
	private String sprde_byr_akhr;
	private Integer mst_flag_batal;


	public Integer getMst_flag_batal() {
		return mst_flag_batal;
	}
	public void setMst_flag_batal(Integer mst_flag_batal) {
		this.mst_flag_batal = mst_flag_batal;
	}
	public Agen getAgen() {
		return agen;
	}
	public void setAgen(Agen agen) {
		this.agen = agen;
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


	public String getEditTglSetor() {
		return editTglSetor;
	}
	public void setEditTglSetor(String editTglSetor) {
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
	public Double getGtTahapan() {
		return GtTahapan;
	}
	public void setGtTahapan(Double gtTahapan) {
		GtTahapan = gtTahapan;
	}
	public Integer getInfo() {
		return info;
	}
	public void setInfo(Integer info) {
		this.info = info;
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
	public String getMstNoNew() {
		return mstNoNew;
	}
	public void setMstNoNew(String mstNoNew) {
		this.mstNoNew = mstNoNew;
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
	public Tahapan getTahapan() {
		return tahapan;
	}
	public void setTahapan(Tahapan tahapan) {
		this.tahapan = tahapan;
	}
//	public Integer getKd_agen() {
//		return kd_agen;
//	}
//	public void setKd_agen(Integer kd_agen) {
//		this.kd_agen = kd_agen;
//	}
//	public String getNm_penutup() {
//		return nm_penutup;
//	}
//	public void setNm_penutup(String nm_penutup) {
//		this.nm_penutup = nm_penutup;
//	}
//	public String getTot_byr() {
//		return tot_byr;
//	}
//	public void setTot_byr(String tot_byr) {
//		this.tot_byr = tot_byr;
//	}
	public Btpp getBtpp() {
		return btpp;
	}
	public void setBtpp(Btpp btpp) {
		this.btpp = btpp;
	}
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public List getLsBtpp() {
		return lsBtpp;
	}
	public void setLsBtpp(List lsBtpp) {
		this.lsBtpp = lsBtpp;
	}
	public List getLsAgent() {
		return lsAgent;
	}
	public void setLsAgent(List lsAgent) {
		this.lsAgent = lsAgent;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getSprde_byr_akhr() {
		return sprde_byr_akhr;
	}
	public void setSprde_byr_akhr(String sprde_byr_akhr) {
		this.sprde_byr_akhr = sprde_byr_akhr;
	}
	public String getSprde_byr_awal() {
		return sprde_byr_awal;
	}
	public void setSprde_byr_awal(String sprde_byr_awal) {
		this.sprde_byr_awal = sprde_byr_awal;
	}
	public void setPrde_byr_akhr(Date prde_byr_akhr) {
		this.prde_byr_akhr = prde_byr_akhr;
	}
	public void setPrde_byr_awal(Date prde_byr_awal) {
		this.prde_byr_awal = prde_byr_awal;
	}
	public Date getPrde_byr_akhr() {
		return prde_byr_akhr;
	}
	public Date getPrde_byr_awal() {
		return prde_byr_awal;
	}


}
	
	
