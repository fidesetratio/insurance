package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Model Class untuk MST_DREK
 * 
 * @author Yusuf
 * @since Nov 5, 2008 (1:32:20 PM)
 */
public class Drek implements Serializable {
	

	private static final long serialVersionUID = -3555408838731449933L;

	private String mspo_no_blanko;
	
	private String nama_cab;
	private String no_trx;
	private Integer lsbp_id;
	private Date tgl_trx;
	private String jenis;
	private String lku_id;
	private Double nilai_trx;
	private String no_spaj;
	private Date tgl_input;
	private Integer flag_proses;
	private Integer flag_update;
	private Date tgl_proses;
	private Integer user_input;
	private Integer user_proses;
	private String ket;
	private String kode_cab;
	private String lsbp_nama;
	private String lku_symbol;
	private String nama_input;
	private String nama_proses;
	private Integer flag_recheck;
	private String ket_update;

	private String bank_pusat_ajs;
	private String bank_ajs;
	private String norek_ajs;
	
	private Integer flag_tunggal_gabungan;
	private String flag_tunggal;
	private String flag_gabungan;
	
	private Double jumlah;
	private String radioBoxGabunganDisabled;


	public String getRadioBoxGabunganDisabled() {
		return radioBoxGabunganDisabled;
	}
	public void setRadioBoxGabunganDisabled(String radioBoxGabunganDisabled) {
		this.radioBoxGabunganDisabled = radioBoxGabunganDisabled;
	}
	public Double getJumlah() {
		return jumlah;
	}
	public void setJumlah(Double jumlah) {
		this.jumlah = jumlah;
	}
	public String getFlag_gabungan() {
		return flag_gabungan;
	}
	public void setFlag_gabungan(String flag_gabungan) {
		this.flag_gabungan = flag_gabungan;
	}
	public String getFlag_tunggal() {
		return flag_tunggal;
	}
	public void setFlag_tunggal(String flag_tunggal) {
		this.flag_tunggal = flag_tunggal;
	}
	public Integer getFlag_tunggal_gabungan() {
		return flag_tunggal_gabungan;
	}
	public void setFlag_tunggal_gabungan(Integer flag_tunggal_gabungan) {
		this.flag_tunggal_gabungan = flag_tunggal_gabungan;
	}
	public Integer getFlag_recheck() {
		return flag_recheck;
	}
	public void setFlag_recheck(Integer flag_recheck) {
		this.flag_recheck = flag_recheck;
	}
	public String getKet_update() {
		return ket_update;
	}
	public void setKet_update(String ket_update) {
		this.ket_update = ket_update;
	}
	public String getBank_pusat_ajs() {
		return bank_pusat_ajs;
	}
	public void setBank_pusat_ajs(String bank_pusat_ajs) {
		this.bank_pusat_ajs = bank_pusat_ajs;
	}
	public String getBank_ajs() {
		return bank_ajs;
	}
	public void setBank_ajs(String bank_ajs) {
		this.bank_ajs = bank_ajs;
	}
	public String getLsbp_nama() {
		return lsbp_nama;
	}
	public String getNama_cab() {
		return nama_cab;
	}
	public void setNama_cab(String nama_cab) {
		this.nama_cab = nama_cab;
	}
	public void setLsbp_nama(String lsbp_nama) {
		this.lsbp_nama = lsbp_nama;
	}
	public String getLku_symbol() {
		return lku_symbol;
	}
	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
	public String getNama_input() {
		return nama_input;
	}
	public void setNama_input(String nama_input) {
		this.nama_input = nama_input;
	}
	public String getNama_proses() {
		return nama_proses;
	}
	public void setNama_proses(String nama_proses) {
		this.nama_proses = nama_proses;
	}
	public String getNo_trx() {
		return no_trx;
	}
	public void setNo_trx(String no_trx) {
		this.no_trx = no_trx;
	}
	public Integer getLsbp_id() {
		return lsbp_id;
	}
	public void setLsbp_id(Integer lsbp_id) {
		this.lsbp_id = lsbp_id;
	}
	public Date getTgl_trx() {
		return tgl_trx;
	}
	public void setTgl_trx(Date tgl_trx) {
		this.tgl_trx = tgl_trx;
	}
	public String getJenis() {
		return jenis;
	}
	public void setJenis(String jenis) {
		this.jenis = jenis;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Double getNilai_trx() {
		return nilai_trx;
	}
	public void setNilai_trx(Double nilai_trx) {
		this.nilai_trx = nilai_trx;
	}
	public String getNorek_ajs() {
		return norek_ajs;
	}
	public void setNorek_ajs(String norek_ajs) {
		this.norek_ajs = norek_ajs;
	}
	public String getNo_spaj() {
		return no_spaj;
	}
	public void setNo_spaj(String no_spaj) {
		this.no_spaj = no_spaj;
	}
	public Date getTgl_input() {
		return tgl_input;
	}
	public void setTgl_input(Date tgl_input) {
		this.tgl_input = tgl_input;
	}
	public Integer getFlag_proses() {
		return flag_proses;
	}
	public void setFlag_proses(Integer flag_proses) {
		this.flag_proses = flag_proses;
	}
	public Integer getFlag_update() {
		return flag_update;
	}
	public void setFlag_update(Integer flag_update) {
		this.flag_update = flag_update;
	}
	public Date getTgl_proses() {
		return tgl_proses;
	}
	public void setTgl_proses(Date tgl_proses) {
		this.tgl_proses = tgl_proses;
	}
	public Integer getUser_input() {
		return user_input;
	}
	public void setUser_input(Integer user_input) {
		this.user_input = user_input;
	}
	public Integer getUser_proses() {
		return user_proses;
	}
	public void setUser_proses(Integer user_proses) {
		this.user_proses = user_proses;
	}
	public String getKet() {
		return ket;
	}
	public void setKet(String ket) {
		this.ket = ket;
	}
	public String getKode_cab() {
		return kode_cab;
	}
	public void setKode_cab(String kode_cab) {
		this.kode_cab = kode_cab;
	}
	public String getMspo_no_blanko() {
		return mspo_no_blanko;
	}
	public void setMspo_no_blanko(String mspo_no_blanko) {
		this.mspo_no_blanko = mspo_no_blanko;
	}

}
