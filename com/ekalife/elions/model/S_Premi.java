package com.ekalife.elions.model;

import java.io.Serializable;


public class S_Premi implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Double premi_dasar;
	private Double premi_pa;
	private Double premi_hcr;
	private Double premi_disc;
	private Double materai;
	private String accno;
	private Long no_cr;
	private Long no_cd;
	private String voucher;
	private Double kurs_harian;
	private Double kurs_bulanan;
	private Double saldo_akhir;
	private Integer rek_id;
	private String nama_pemegang;
	private String no_polis;
	private String k_region;
	private Integer premike;
	private Integer tahunke;
	private String no_pre;
	private String depo_pre;
	private String no_spaj;
	private Double biaya_polis;
	private boolean mri;
	private Integer gross;
	private String old_polis;
	private String flag_materai;
	private boolean ekaproteksi;
	private String[] project; 
	private String[] budget; 
	private Long bisnis_id;
	private String jum_pre[];
	private Double premi_ext;
	private Double premi_ar;
	private Double bunga_tunggak;
	private Double tahapan;
	private Double tot_premi;
	private String no_jm;

	public String getNo_jm() {
		return no_jm;
	}
	public void setNo_jm(String no_jm) {
		this.no_jm = no_jm;
	}
	public String getAccno() {
		return accno;
	}
	public void setAccno(String accno) {
		this.accno = accno;
	}
	public Double getBiaya_polis() {
		return biaya_polis;
	}
	public void setBiaya_polis(Double biaya_polis) {
		this.biaya_polis = biaya_polis;
	}
	public Long getBisnis_id() {
		return bisnis_id;
	}
	public void setBisnis_id(Long bisnis_id) {
		this.bisnis_id = bisnis_id;
	}
	public String[] getBudget() {
		return budget;
	}
	public void setBudget(String[] budget) {
		this.budget = budget;
	}
	public Double getBunga_tunggak() {
		return bunga_tunggak;
	}
	public void setBunga_tunggak(Double bunga_tunggak) {
		this.bunga_tunggak = bunga_tunggak;
	}
	public String getDepo_pre() {
		return depo_pre;
	}
	public void setDepo_pre(String depo_pre) {
		this.depo_pre = depo_pre;
	}
	public boolean isEkaproteksi() {
		return ekaproteksi;
	}
	public void setEkaproteksi(boolean ekaproteksi) {
		this.ekaproteksi = ekaproteksi;
	}
	public String getFlag_materai() {
		return flag_materai;
	}
	public void setFlag_materai(String flag_materai) {
		this.flag_materai = flag_materai;
	}
	public Integer getGross() {
		return gross;
	}
	public void setGross(Integer gross) {
		this.gross = gross;
	}
	public String[] getJum_pre() {
		return jum_pre;
	}
	public void setJum_pre(String[] jum_pre) {
		this.jum_pre = jum_pre;
	}
	public String getK_region() {
		return k_region;
	}
	public void setK_region(String k_region) {
		this.k_region = k_region;
	}
	public Double getKurs_bulanan() {
		return kurs_bulanan;
	}
	public void setKurs_bulanan(Double kurs_bulanan) {
		this.kurs_bulanan = kurs_bulanan;
	}
	public Double getKurs_harian() {
		return kurs_harian;
	}
	public void setKurs_harian(Double kurs_harian) {
		this.kurs_harian = kurs_harian;
	}
	public Double getMaterai() {
		return materai;
	}
	public void setMaterai(Double materai) {
		this.materai = materai;
	}
	public boolean isMri() {
		return mri;
	}
	public void setMri(boolean mri) {
		this.mri = mri;
	}
	public String getNama_pemegang() {
		return nama_pemegang;
	}
	public void setNama_pemegang(String nama_pemegang) {
		this.nama_pemegang = nama_pemegang;
	}
	public Long getNo_cd() {
		return no_cd;
	}
	public void setNo_cd(Long no_cd) {
		this.no_cd = no_cd;
	}
	public Long getNo_cr() {
		return no_cr;
	}
	public void setNo_cr(Long no_cr) {
		this.no_cr = no_cr;
	}
	public String getNo_polis() {
		return no_polis;
	}
	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}
	public String getNo_pre() {
		return no_pre;
	}
	public void setNo_pre(String no_pre) {
		this.no_pre = no_pre;
	}
	public String getNo_spaj() {
		return no_spaj;
	}
	public void setNo_spaj(String no_spaj) {
		this.no_spaj = no_spaj;
	}
	public String getOld_polis() {
		return old_polis;
	}
	public void setOld_polis(String old_polis) {
		this.old_polis = old_polis;
	}
	public Double getPremi_ar() {
		return premi_ar;
	}
	public void setPremi_ar(Double premi_ar) {
		this.premi_ar = premi_ar;
	}
	public Double getPremi_dasar() {
		return premi_dasar;
	}
	public void setPremi_dasar(Double premi_dasar) {
		this.premi_dasar = premi_dasar;
	}
	public Double getPremi_disc() {
		return premi_disc;
	}
	public void setPremi_disc(Double premi_disc) {
		this.premi_disc = premi_disc;
	}
	public Double getPremi_ext() {
		return premi_ext;
	}
	public void setPremi_ext(Double premi_ext) {
		this.premi_ext = premi_ext;
	}
	public Double getPremi_hcr() {
		return premi_hcr;
	}
	public void setPremi_hcr(Double premi_hcr) {
		this.premi_hcr = premi_hcr;
	}
	public Double getPremi_pa() {
		return premi_pa;
	}
	public void setPremi_pa(Double premi_pa) {
		this.premi_pa = premi_pa;
	}
	public Integer getPremike() {
		return premike;
	}
	public void setPremike(Integer premike) {
		this.premike = premike;
	}
	public String[] getProject() {
		return project;
	}
	public void setProject(String[] project) {
		this.project = project;
	}
	public Integer getRek_id() {
		return rek_id;
	}
	public void setRek_id(Integer rek_id) {
		this.rek_id = rek_id;
	}
	public Double getSaldo_akhir() {
		return saldo_akhir;
	}
	public void setSaldo_akhir(Double saldo_akhir) {
		this.saldo_akhir = saldo_akhir;
	}
	public Double getTahapan() {
		return tahapan;
	}
	public void setTahapan(Double tahapan) {
		this.tahapan = tahapan;
	}
	public Integer getTahunke() {
		return tahunke;
	}
	public void setTahunke(Integer tahunke) {
		this.tahunke = tahunke;
	}
	public Double getTot_premi() {
		return tot_premi;
	}
	public void setTot_premi(Double tot_premi) {
		this.tot_premi = tot_premi;
	}
	public String getVoucher() {
		return voucher;
	}
	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}

}
