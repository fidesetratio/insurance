package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DetailPembayaran implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String To;
	private String kode;
	private String lca_polis;
	private String reg_spaj;
	private String mspa_old_policy;
	private Integer bank;
	private String no_kttp;
	private Date tgl_rk;
	private Date tgl_bayar;
	private Integer jenis_ttp;
	private Integer cara_bayar;
	private String ref_polis_no;
	private Double nilai_kurs;
	private String kurs;
	private Double jml_bayar;
	private String keterangan;
	private String client_bank;
	private String no_rek;
	private String tgl_jatuh_tempo;
	private Double total;
	private String lus_login_name;
	private String status;
	private String msdp_no_pre;	
	private String msdp_jurnal;
	private String msdp_no_voucher;
	private String submitType;
	private Integer no_ke;
	private Date tgl_skrg;
	private Integer aktif;
	private Integer mstah_tahapan_ke;
	private Integer lsjta_id;
	private Double msbat_jumlah;
	private String mstah_no_tahapan;
	private Date mstah_tgl_konfirmasi;
	private String lku_id;
	private String mspo_policy_no;
	private Integer ke;
	private Double total_bayar;
	private String spaj_s ;
	private Integer hitung;
	private Integer mste_flag_cc;
	private Integer lsbs_id;
	private Integer hit_err;
	private String statussubmit;
	private Integer flag_w;
	private String acc_no;
	private String no_trx;
	private String lus_id;
	private String tipe;
	private String jenis;
	private Integer no_urut;
	private List<Linkdetail> jenisTransaksiList;
	public String mcl_first;
	private String jenis_kredit;
	private String no_pre;
	private Integer msdp_flag_topup;
	private Integer msdp_flag_merchant;
	
	public String getNo_pre() {
		return no_pre;
	}
	public void setNo_pre(String no_pre) {
		this.no_pre = no_pre;
	}
	public String getJenis_kredit() {
		return jenis_kredit;
	}
	public void setJenis_kredit(String jenis_kredit) {
		this.jenis_kredit = jenis_kredit;
	}
	public Integer getNo_urut() {
		return no_urut;
	}
	public void setNo_urut(Integer no_urut) {
		this.no_urut = no_urut;
	}
	public List<Linkdetail> getJenisTransaksiList() {
		return jenisTransaksiList;
	}
	public void setJenisTransaksiList(List<Linkdetail> jenisTransaksiList) {
		this.jenisTransaksiList = jenisTransaksiList;
	}
	public String getJenis() {
		return jenis;
	}
	public void setJenis(String jenis) {
		this.jenis = jenis;
	}
	public String getTipe() {
		return tipe;
	}
	public void setTipe(String tipe) {
		this.tipe = tipe;
	}
	public String getLus_id() {
		return lus_id;
	}
	public void setLus_id(String lus_id) {
		this.lus_id = lus_id;
	}
	public String getNo_trx() {
		return no_trx;
	}
	public void setNo_trx(String no_trx) {
		this.no_trx = no_trx;
	}
	public String getAcc_no() {
		return acc_no;
	}
	public void setAcc_no(String acc_no) {
		this.acc_no = acc_no;
	}
	public Integer getFlag_w() {
		return flag_w;
	}
	public void setFlag_w(Integer flag_w) {
		this.flag_w = flag_w;
	}
	public String getStatussubmit() {
		return statussubmit;
	}
	public void setStatussubmit(String statussubmit) {
		this.statussubmit = statussubmit;
	}
	public Integer getAktif() {
		return aktif;
	}
	public void setAktif(Integer aktif) {
		this.aktif = aktif;
	}
	public Integer getBank() {
		return bank;
	}
	public void setBank(Integer bank) {
		this.bank = bank;
	}
	public Integer getCara_bayar() {
		return cara_bayar;
	}
	public void setCara_bayar(Integer cara_bayar) {
		this.cara_bayar = cara_bayar;
	}
	public String getClient_bank() {
		return client_bank;
	}
	public void setClient_bank(String client_bank) {
		this.client_bank = client_bank;
	}
	public Integer getHit_err() {
		return hit_err;
	}
	public void setHit_err(Integer hit_err) {
		this.hit_err = hit_err;
	}
	public Integer getHitung() {
		return hitung;
	}
	public void setHitung(Integer hitung) {
		this.hitung = hitung;
	}
	public Integer getJenis_ttp() {
		return jenis_ttp;
	}
	public void setJenis_ttp(Integer jenis_ttp) {
		this.jenis_ttp = jenis_ttp;
	}
	public Double getJml_bayar() {
		return jml_bayar;
	}
	public void setJml_bayar(Double jml_bayar) {
		this.jml_bayar = jml_bayar;
	}
	public Integer getKe() {
		return ke;
	}
	public void setKe(Integer ke) {
		this.ke = ke;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public String getKode() {
		return kode;
	}
	public void setKode(String kode) {
		this.kode = kode;
	}
	public String getKurs() {
		return kurs;
	}
	public void setKurs(String kurs) {
		this.kurs = kurs;
	}
	public String getLca_polis() {
		return lca_polis;
	}
	public void setLca_polis(String lca_polis) {
		this.lca_polis = lca_polis;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public Integer getLsjta_id() {
		return lsjta_id;
	}
	public void setLsjta_id(Integer lsjta_id) {
		this.lsjta_id = lsjta_id;
	}
	public String getLus_login_name() {
		return lus_login_name;
	}
	public void setLus_login_name(String lus_login_name) {
		this.lus_login_name = lus_login_name;
	}
	public Double getMsbat_jumlah() {
		return msbat_jumlah;
	}
	public void setMsbat_jumlah(Double msbat_jumlah) {
		this.msbat_jumlah = msbat_jumlah;
	}
	public String getMsdp_jurnal() {
		return msdp_jurnal;
	}
	public void setMsdp_jurnal(String msdp_jurnal) {
		this.msdp_jurnal = msdp_jurnal;
	}
	public String getMsdp_no_pre() {
		return msdp_no_pre;
	}
	public void setMsdp_no_pre(String msdp_no_pre) {
		this.msdp_no_pre = msdp_no_pre;
	}
	public String getMsdp_no_voucher() {
		return msdp_no_voucher;
	}
	public void setMsdp_no_voucher(String msdp_no_voucher) {
		this.msdp_no_voucher = msdp_no_voucher;
	}
	public String getMspa_old_policy() {
		return mspa_old_policy;
	}
	public void setMspa_old_policy(String mspa_old_policy) {
		this.mspa_old_policy = mspa_old_policy;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public String getMstah_no_tahapan() {
		return mstah_no_tahapan;
	}
	public void setMstah_no_tahapan(String mstah_no_tahapan) {
		this.mstah_no_tahapan = mstah_no_tahapan;
	}
	public Integer getMstah_tahapan_ke() {
		return mstah_tahapan_ke;
	}
	public void setMstah_tahapan_ke(Integer mstah_tahapan_ke) {
		this.mstah_tahapan_ke = mstah_tahapan_ke;
	}
	public Date getMstah_tgl_konfirmasi() {
		return mstah_tgl_konfirmasi;
	}
	public void setMstah_tgl_konfirmasi(Date mstah_tgl_konfirmasi) {
		this.mstah_tgl_konfirmasi = mstah_tgl_konfirmasi;
	}
	public Integer getMste_flag_cc() {
		return mste_flag_cc;
	}
	public void setMste_flag_cc(Integer mste_flag_cc) {
		this.mste_flag_cc = mste_flag_cc;
	}
	public Double getNilai_kurs() {
		return nilai_kurs;
	}
	public void setNilai_kurs(Double nilai_kurs) {
		this.nilai_kurs = nilai_kurs;
	}
	public Integer getNo_ke() {
		return no_ke;
	}
	public void setNo_ke(Integer no_ke) {
		this.no_ke = no_ke;
	}
	public String getNo_kttp() {
		return no_kttp;
	}
	public void setNo_kttp(String no_kttp) {
		this.no_kttp = no_kttp;
	}
	public String getNo_rek() {
		return no_rek;
	}
	public void setNo_rek(String no_rek) {
		this.no_rek = no_rek;
	}
	public String getRef_polis_no() {
		return ref_polis_no;
	}
	public void setRef_polis_no(String ref_polis_no) {
		this.ref_polis_no = ref_polis_no;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getSpaj_s() {
		return spaj_s;
	}
	public void setSpaj_s(String spaj_s) {
		this.spaj_s = spaj_s;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSubmitType() {
		return submitType;
	}
	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}
	public Date getTgl_bayar() {
		return tgl_bayar;
	}
	public void setTgl_bayar(Date tgl_bayar) {
		this.tgl_bayar = tgl_bayar;
	}
	public String getTgl_jatuh_tempo() {
		return tgl_jatuh_tempo;
	}
	public void setTgl_jatuh_tempo(String tgl_jatuh_tempo) {
		this.tgl_jatuh_tempo = tgl_jatuh_tempo;
	}
	public Date getTgl_rk() {
		return tgl_rk;
	}
	public void setTgl_rk(Date tgl_rk) {
		this.tgl_rk = tgl_rk;
	}
	public Date getTgl_skrg() {
		return tgl_skrg;
	}
	public void setTgl_skrg(Date tgl_skrg) {
		this.tgl_skrg = tgl_skrg;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Double getTotal_bayar() {
		return total_bayar;
	}
	public void setTotal_bayar(Double total_bayar) {
		this.total_bayar = total_bayar;
	}
	public String getTo() {
		return To;
	}
	public void setTo(String to) {
		To = to;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public Integer getMsdp_flag_topup() {
		return msdp_flag_topup;
	}
	public void setMsdp_flag_topup(Integer msdp_flag_topup) {
		this.msdp_flag_topup = msdp_flag_topup;
	}
	public Integer getMsdp_flag_merchant() {
		return msdp_flag_merchant;
	}
	public void setMsdp_flag_merchant(Integer msdp_flag_merchant) {
		this.msdp_flag_merchant = msdp_flag_merchant;
	}
}
