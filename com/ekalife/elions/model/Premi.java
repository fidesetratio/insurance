/**
 * 
 */
package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Premi implements Serializable {

	private static final long serialVersionUID = 7571743059725313240L;
	private Integer lsdbs_number;
	private String mspa_payment_id;
	private String lku_id;
	private String lscb_id;
	private Integer mspo_pay_period;
	private Date mste_beg_date;
	private Integer mste_flag_cc;
	private String ls_pre;
	private String ls_no_voucher;
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
	private String project[];
	private String budget[];
	private Long bisnis_id;
	private String jum_pre[];
	private Double premi_ext;
	private Double premi_ar;
	private Double bunga_tunggak;
	private Double tahapan;
	private Long msdp_number;
	private Integer lspd_id;
	private String no_jm;
	private String no_jm_sa;
	
	public String getNo_jm_sa() {
		return no_jm_sa;
	}
	public void setNo_jm_sa(String no_jm_sa) {
		this.no_jm_sa = no_jm_sa;
	}
	public String getNo_jm() {
		return no_jm;
	}
	public void setNo_jm(String no_jm) {
		this.no_jm = no_jm;
	}
	public Long getMsdp_number() {
		return msdp_number;
	}
	public void setMsdp_number(Long msdp_number) {
		this.msdp_number = msdp_number;
	}
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
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
		if(bunga_tunggak==null) return new Double(0);
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
		if(jum_pre==null) return new String[0];
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
		if(premi_hcr==null) return new Double(0);
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
	public String getVoucher() {
		return voucher;
	}
	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public String getLscb_id() {
		return lscb_id;
	}
	public void setLscb_id(String lscb_id) {
		this.lscb_id = lscb_id;
	}
	public Integer getMspo_pay_period() {
		return mspo_pay_period;
	}
	public void setMspo_pay_period(Integer mspo_pay_period) {
		this.mspo_pay_period = mspo_pay_period;
	}
	public Date getMste_beg_date() {
		return mste_beg_date;
	}
	public void setMste_beg_date(Date mste_beg_date) {
		this.mste_beg_date = mste_beg_date;
	}
	public Integer getMste_flag_cc() {
		return mste_flag_cc;
	}
	public void setMste_flag_cc(Integer mste_flag_cc) {
		this.mste_flag_cc = mste_flag_cc;
	}
	public String getLs_no_voucher() {
		return ls_no_voucher;
	}
	public void setLs_no_voucher(String ls_no_voucher) {
		this.ls_no_voucher = ls_no_voucher;
	}
	public String getLs_pre() {
		return ls_pre;
	}
	public void setLs_pre(String ls_pre) {
		this.ls_pre = ls_pre;
	}
	public String getMspa_payment_id() {
		return mspa_payment_id;
	}
	public void setMspa_payment_id(String mspa_payment_id) {
		this.mspa_payment_id = mspa_payment_id;
	}
	public Integer getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}	
}
