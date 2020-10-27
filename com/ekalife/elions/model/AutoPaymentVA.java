package com.ekalife.elions.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class AutoPaymentVA implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;
	private String lku_id;
	private String no_va;
	private String nama_pp;
	private String nama_produk;
	private String no_trx;
	private String norek_ajs;
	private String tgl_trx;
	private String jenis_trx;
	private String no_pre;
	private Integer check;
	private Integer lspd_id;
	private Integer lsbs_id;
	private Integer lsdbs_number;
	private Integer lsgb_id;
	private Integer lsjb_id;
	private Integer lsbp_id;
	private Integer nilai_kurs;
	private Integer flag_merchant;
	private Integer flag_tunggal_gabungan;
	private BigDecimal premi_pokok;
	private BigDecimal premi_topup_berkala;
	private BigDecimal premi_topup_tunggal;
	private BigDecimal total_premi;
	private BigDecimal nilai_trx;
	private BigDecimal nilai_trx_terpakai;
	
	//Tambahan untuk Autopayment BSM
	private String cif;
	private String nama_rek;
	private String no_rek;
	
	//Tambahan untuk Autopayment Bancassurance
	private String no_khusus;
	
	
	public String getReg_spaj() {
		return reg_spaj;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public String getLku_id() {
		return lku_id;
	}

	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}

	public String getNo_va() {
		return no_va;
	}

	public void setNo_va(String no_va) {
		this.no_va = no_va;
	}
	
	public String getNama_pp() {
		return nama_pp;
	}

	public void setNama_pp(String nama_pp) {
		this.nama_pp = nama_pp;
	}

	public String getNama_produk() {
		return nama_produk;
	}

	public void setNama_produk(String nama_produk) {
		this.nama_produk = nama_produk;
	}

	public String getNo_trx() {
		return no_trx;
	}

	public void setNo_trx(String no_trx) {
		this.no_trx = no_trx;
	}

	public String getNorek_ajs() {
		return norek_ajs;
	}

	public void setNorek_ajs(String norek_ajs) {
		this.norek_ajs = norek_ajs;
	}

	public String getTgl_trx() {
		return tgl_trx;
	}

	public void setTgl_trx(String tgl_trx) {
		this.tgl_trx = tgl_trx;
	}

	public String getJenis_trx() {
		return jenis_trx;
	}

	public void setJenis_trx(String jenis_trx) {
		this.jenis_trx = jenis_trx;
	}

	public String getNo_pre() {
		return no_pre;
	}

	public void setNo_pre(String no_pre) {
		this.no_pre = no_pre;
	}

	public Integer getCheck() {
		return check;
	}

	public void setCheck(Integer check) {
		this.check = check;
	}

	public Integer getLspd_id() {
		return lspd_id;
	}

	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}

	public Integer getLsbs_id() {
		return lsbs_id;
	}

	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}

	public Integer getLsdbs_number() {
		return lsdbs_number;
	}

	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}

	public Integer getLsgb_id() {
		return lsgb_id;
	}

	public void setLsgb_id(Integer lsgb_id) {
		this.lsgb_id = lsgb_id;
	}

	public Integer getLsjb_id() {
		return lsjb_id;
	}

	public void setLsjb_id(Integer lsjb_id) {
		this.lsjb_id = lsjb_id;
	}
	
	public Integer getLsbp_id() {
		return lsbp_id;
	}

	public void setLsbp_id(Integer lsbp_id) {
		this.lsbp_id = lsbp_id;
	}

	public Integer getNilai_kurs() {
		return nilai_kurs;
	}

	public void setNilai_kurs(Integer nilai_kurs) {
		this.nilai_kurs = nilai_kurs;
	}

	public Integer getFlag_merchant() {
		return flag_merchant;
	}

	public void setFlag_merchant(Integer flag_merchant) {
		this.flag_merchant = flag_merchant;
	}

	public Integer getFlag_tunggal_gabungan() {
		return flag_tunggal_gabungan;
	}

	public void setFlag_tunggal_gabungan(Integer flag_tunggal_gabungan) {
		this.flag_tunggal_gabungan = flag_tunggal_gabungan;
	}

	public BigDecimal getPremi_pokok() {
		return premi_pokok;
	}

	public void setPremi_pokok(BigDecimal premi_pokok) {
		this.premi_pokok = premi_pokok;
	}

	public BigDecimal getPremi_topup_berkala() {
		return premi_topup_berkala;
	}

	public void setPremi_topup_berkala(BigDecimal premi_topup_berkala) {
		this.premi_topup_berkala = premi_topup_berkala;
	}

	public BigDecimal getPremi_topup_tunggal() {
		return premi_topup_tunggal;
	}

	public void setPremi_topup_tunggal(BigDecimal premi_topup_tunggal) {
		this.premi_topup_tunggal = premi_topup_tunggal;
	}
	
	public BigDecimal getTotal_premi() {
		return total_premi;
	}

	public void setTotal_premi(BigDecimal total_premi) {
		this.total_premi = total_premi;
	}

	public BigDecimal getNilai_trx() {
		return nilai_trx;
	}

	public void setNilai_trx(BigDecimal nilai_trx) {
		this.nilai_trx = nilai_trx;
	}

	public BigDecimal getNilai_trx_terpakai() {
		return nilai_trx_terpakai;
	}

	public void setNilai_trx_terpakai(BigDecimal nilai_trx_terpakai) {
		this.nilai_trx_terpakai = nilai_trx_terpakai;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getNama_rek() {
		return nama_rek;
	}

	public void setNama_rek(String nama_rek) {
		this.nama_rek = nama_rek;
	}

	public String getNo_rek() {
		return no_rek;
	}

	public void setNo_rek(String no_rek) {
		this.no_rek = no_rek;
	}

	public String getNo_khusus() {
		return no_khusus;
	}

	public void setNo_khusus(String no_khusus) {
		this.no_khusus = no_khusus;
	}
}
