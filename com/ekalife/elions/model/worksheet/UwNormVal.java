package com.ekalife.elions.model.worksheet;

import java.io.Serializable;

public class UwNormVal implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * normal value berdasarkan lab Pramita.
	 */
	
	private String lpk_deyut_nadi;
	
	private String urin_warna;
	private String urin_kejernihan;
	private String urin_berat_jenis;
	private String urin_ph;
	private String urin_protein;
	private String urin_glukosa;
	private String urin_keton;
	private String urin_bilirubin;
	private String urin_urobilinogen;
	private String urin_nitrit;
	private String urin_darah_samar;
	private String urin_lekosit_esterase;
	private String urin_eritrosit;
	private String urin_lekosit;
	private String urin_epitel;
	private String urin_silinder;
	private String urin_kristal;
	private String urin_bakteri;
	
	private String darah_rutin_hemoglobin;
	private String darah_rutin_leukosit;
	private String darah_rutin_trombosit;
	private String darah_rutin_eritrosit;
	private String darah_rutin_hematokrit;
	private String darah_rutin_led;
	private String darah_rutin_mcv;
	private String darah_rutin_mch;
	private String darah_rutin_mchc;
	private String darah_rutin_rdw;
	private String darah_rutin_eosinofil;
	private String darah_rutin_basofil;
	private String darah_rutin_neutrofil_batang;
	private String darah_rutin_neutrofil_segmen;
	private String darah_rutin_limfosit;
	private String darah_rutin_monosit;
	
	private String gula_darah_glukosa_darah_puasa;
	private String gula_darah_glukosa_hba1c;
	
	private String lemak_darah_total_cholesterol;
	private String lemak_darah_hdl_cholesterol;
	private String lemak_darah_ldl_cholesterol;
	private String lemak_darah_ratio_total_chol_hdl;
	private String lemak_darah_ratio_ldl_hdl;
	private String lemak_darah_trigliserida;
	
	private String fungsi_hati_sgot_ast;
	private String fungsi_hati_sgpt_alt;
	private String fungsi_hati_gamma_gt_ggtp;
	private String fungsi_hati_alkali_fosfatase;
	private String fungsi_hati_bilirubin_direk;
	private String fungsi_hati_bilirubin_indirek;
	private String fungsi_hati_bilirubin_total;
	private String fungsi_hati_albumin;
	private String fungsi_hati_globulin;
	private String fungsi_hati_total_protein;
	
	private String hepatitis_hbsag;
	private String hepatitis_hbeag;
	
	private String fungsi_ginjal_ureum;
	private String fungsi_ginjal_creatinin;
	private String fungsi_ginjal_asam_urat;
	
	private String anti_hiv_anti_hiv;
	
	private String tumor_marker_cea;
	private String tumor_marker_afp;
	private String tumor_marker_psa;
	private String tumor_marker_ca_15_3;
	
	public UwNormVal() {
		setLpk_deyut_nadi("60 - 100");
		
		setUrin_warna("kuning");
		setUrin_kejernihan("jernih");
		setUrin_berat_jenis("1,010 - 1,030"); 
		setUrin_ph("5,0 - 8,0");
		setUrin_protein("negatif (<=15 mg/dl)");
		setUrin_glukosa("negatif (< 50 mg/dl)");
		setUrin_keton("negatif (<= 15 mg/dl )");
		setUrin_bilirubin("negatif ( < 0.5 mg/dl)");
		setUrin_urobilinogen("0,1 - 1,0");
		setUrin_nitrit("negatif");
		setUrin_darah_samar("negatif");
		setUrin_lekosit_esterase("negatif");
		setUrin_eritrosit("0 - 1 /LPB");
		setUrin_lekosit("0 - 5 /LPB");
		setUrin_epitel("< 10 / LPB");
		setUrin_silinder("negatif");
		setUrin_kristal("negatif");
		setUrin_bakteri("negatif");
		
		setDarah_rutin_hemoglobin("13,0 - 18,0 g/dl");
		setDarah_rutin_leukosit("4.000 - 11.000 /µL");
		setDarah_rutin_trombosit("150.000 - 400.000/µL");
		setDarah_rutin_eritrosit("4,5 - 5,9  10<sup>6/</sup>µL");
		setDarah_rutin_hematokrit("40 - 50 %");
		setDarah_rutin_led("0 - 10  mm/jam");
		setDarah_rutin_mcv("82 - 92 fL");
		setDarah_rutin_mch("27,5 - 33,2  pg");
		setDarah_rutin_mchc("32 - 37  g/dl");
		setDarah_rutin_rdw("11,5 - 14,5 %");
		setDarah_rutin_eosinofil("1 - 3 %");
		setDarah_rutin_basofil("0 - 1 %");
		setDarah_rutin_neutrofil_batang("2 - 6 %");
		setDarah_rutin_neutrofil_segmen("50 - 70 %");
		setDarah_rutin_limfosit("20 - 40 %");
		setDarah_rutin_monosit("2 - 8 %");
		
		setGula_darah_glukosa_darah_puasa("70 - 110 mg/dl");
		setGula_darah_glukosa_hba1c("< 6,5 %");
		
		setLemak_darah_total_cholesterol("<  200 mg/dl");
		setLemak_darah_hdl_cholesterol("40 - 60 mg/dl");
		setLemak_darah_ldl_cholesterol("<  100 mg/dl");
		setLemak_darah_ratio_total_chol_hdl("< 5");
		setLemak_darah_ratio_ldl_hdl("< 3");
		setLemak_darah_trigliserida("< 150 mg/dl");
		
		setFungsi_hati_sgot_ast("10 - 35 U/L");
		setFungsi_hati_sgpt_alt("9 - 43 U/L");
		setFungsi_hati_gamma_gt_ggtp("11 - 50 U/L");
		setFungsi_hati_alkali_fosfatase("20 - 140 U/L");
		setFungsi_hati_bilirubin_direk("< 0,30 mg/dl");
		setFungsi_hati_bilirubin_indirek("< 0,80 mg/dl");
		setFungsi_hati_bilirubin_total("< 1,00 mg/dl");
		setFungsi_hati_albumin("3,5 - 5,3 g/dl");
		setFungsi_hati_globulin("1,9 - 3,5 g/dl");
		setFungsi_hati_total_protein("6,6 - 8,7 g/dl");
		
		setHepatitis_hbsag("non reaktif indeks <1,0");
		setHepatitis_hbeag("non reaktif indeks <1,0");
		
		setFungsi_ginjal_ureum("14,9 - 38,5 mg/dl");
		setFungsi_ginjal_creatinin("0,7 - 1,3 mg/dl");
		setFungsi_ginjal_asam_urat("2,6 - 7,2 mg/dl");
		
		setAnti_hiv_anti_hiv("Non Reaktif");
		
		setTumor_marker_cea("< =2.5 ng/ml");
		setTumor_marker_afp("<= 7,0 ng/dl");
		setTumor_marker_psa("<=  4 ng/ml");
		setTumor_marker_ca_15_3("< 49 U/ml");
	}

	public String getAnti_hiv_anti_hiv() { return anti_hiv_anti_hiv; }
	public void setAnti_hiv_anti_hiv(String anti_hiv_anti_hiv) { this.anti_hiv_anti_hiv = anti_hiv_anti_hiv; }
	 
	public String getDarah_rutin_basofil() { return darah_rutin_basofil; }
	public void setDarah_rutin_basofil(String darah_rutin_basofil) { this.darah_rutin_basofil = darah_rutin_basofil; }
	
	public String getDarah_rutin_eosinofil() { return darah_rutin_eosinofil; }
	public void setDarah_rutin_eosinofil(String darah_rutin_eosinofil) { this.darah_rutin_eosinofil = darah_rutin_eosinofil; }
	
	public String getDarah_rutin_eritrosit() { return darah_rutin_eritrosit; }
	public void setDarah_rutin_eritrosit(String darah_rutin_eritrosit) { this.darah_rutin_eritrosit = darah_rutin_eritrosit; }
	
	public String getDarah_rutin_hematokrit() { return darah_rutin_hematokrit; }
	public void setDarah_rutin_hematokrit(String darah_rutin_hematokrit) { this.darah_rutin_hematokrit = darah_rutin_hematokrit; }
	
	public String getDarah_rutin_hemoglobin() { return darah_rutin_hemoglobin; }
	public void setDarah_rutin_hemoglobin(String darah_rutin_hemoglobin) { this.darah_rutin_hemoglobin = darah_rutin_hemoglobin; }
	
	public String getDarah_rutin_led() { return darah_rutin_led; }
	public void setDarah_rutin_led(String darah_rutin_led) { this.darah_rutin_led = darah_rutin_led; }
	
	public String getDarah_rutin_leukosit() { return darah_rutin_leukosit; }
	public void setDarah_rutin_leukosit(String darah_rutin_leukosit) { this.darah_rutin_leukosit = darah_rutin_leukosit; }
	
	public String getDarah_rutin_limfosit() { return darah_rutin_limfosit; }
	public void setDarah_rutin_limfosit(String darah_rutin_limfosit) { this.darah_rutin_limfosit = darah_rutin_limfosit; }
	
	public String getDarah_rutin_mch() { return darah_rutin_mch; }
	public void setDarah_rutin_mch(String darah_rutin_mch) { this.darah_rutin_mch = darah_rutin_mch; }
	
	public String getDarah_rutin_mchc() { return darah_rutin_mchc; } 
	public void setDarah_rutin_mchc(String darah_rutin_mchc) { this.darah_rutin_mchc = darah_rutin_mchc; }
	
	public String getDarah_rutin_mcv() { return darah_rutin_mcv; }
	public void setDarah_rutin_mcv(String darah_rutin_mcv) { this.darah_rutin_mcv = darah_rutin_mcv; }
	
	public String getDarah_rutin_monosit() { return darah_rutin_monosit; }
	public void setDarah_rutin_monosit(String darah_rutin_monosit) { this.darah_rutin_monosit = darah_rutin_monosit; }
	
	public String getDarah_rutin_neutrofil_batang() { return darah_rutin_neutrofil_batang; }
	public void setDarah_rutin_neutrofil_batang(String darah_rutin_neutrofil_batang) { this.darah_rutin_neutrofil_batang = darah_rutin_neutrofil_batang; }
	
	public String getDarah_rutin_neutrofil_segmen() { return darah_rutin_neutrofil_segmen; }
	public void setDarah_rutin_neutrofil_segmen(String darah_rutin_neutrofil_segmen) { this.darah_rutin_neutrofil_segmen = darah_rutin_neutrofil_segmen; }
	
	public String getDarah_rutin_rdw() { return darah_rutin_rdw; }
	public void setDarah_rutin_rdw(String darah_rutin_rdw) { this.darah_rutin_rdw = darah_rutin_rdw; }
	
	public String getDarah_rutin_trombosit() { return darah_rutin_trombosit; }
	public void setDarah_rutin_trombosit(String darah_rutin_trombosit) { this.darah_rutin_trombosit = darah_rutin_trombosit; }
	
	public String getFungsi_ginjal_asam_urat() { return fungsi_ginjal_asam_urat; }
	public void setFungsi_ginjal_asam_urat(String fungsi_ginjal_asam_urat) { this.fungsi_ginjal_asam_urat = fungsi_ginjal_asam_urat; }
	
	public String getFungsi_ginjal_creatinin() { return fungsi_ginjal_creatinin; }
	public void setFungsi_ginjal_creatinin(String fungsi_ginjal_creatinin) { this.fungsi_ginjal_creatinin = fungsi_ginjal_creatinin; }
	
	public String getFungsi_ginjal_ureum() { return fungsi_ginjal_ureum; }
	public void setFungsi_ginjal_ureum(String fungsi_ginjal_ureum) { this.fungsi_ginjal_ureum = fungsi_ginjal_ureum; }
	
	public String getFungsi_hati_albumin() { return fungsi_hati_albumin; }
	public void setFungsi_hati_albumin(String fungsi_hati_albumin) { this.fungsi_hati_albumin = fungsi_hati_albumin; }
	
	public String getFungsi_hati_alkali_fosfatase() { return fungsi_hati_alkali_fosfatase; }
	public void setFungsi_hati_alkali_fosfatase(String fungsi_hati_alkali_fosfatase) { this.fungsi_hati_alkali_fosfatase = fungsi_hati_alkali_fosfatase; }
	
	public String getFungsi_hati_bilirubin_direk() { return fungsi_hati_bilirubin_direk; }
	public void setFungsi_hati_bilirubin_direk(String fungsi_hati_bilirubin_direk) { this.fungsi_hati_bilirubin_direk = fungsi_hati_bilirubin_direk; }
	
	public String getFungsi_hati_bilirubin_indirek() { return fungsi_hati_bilirubin_indirek; }
	public void setFungsi_hati_bilirubin_indirek(String fungsi_hati_bilirubin_indirek) { this.fungsi_hati_bilirubin_indirek = fungsi_hati_bilirubin_indirek; }
	
	public String getFungsi_hati_bilirubin_total() { return fungsi_hati_bilirubin_total; }
	public void setFungsi_hati_bilirubin_total(String fungsi_hati_bilirubin_total) { this.fungsi_hati_bilirubin_total = fungsi_hati_bilirubin_total; }
	
	public String getFungsi_hati_gamma_gt_ggtp() { return fungsi_hati_gamma_gt_ggtp; }
	public void setFungsi_hati_gamma_gt_ggtp(String fungsi_hati_gamma_gt_ggtp) { this.fungsi_hati_gamma_gt_ggtp = fungsi_hati_gamma_gt_ggtp; }
	
	public String getFungsi_hati_globulin() { return fungsi_hati_globulin; }
	public void setFungsi_hati_globulin(String fungsi_hati_globulin) { this.fungsi_hati_globulin = fungsi_hati_globulin; }
	
	public String getFungsi_hati_sgot_ast() { return fungsi_hati_sgot_ast; }
	public void setFungsi_hati_sgot_ast(String fungsi_hati_sgot_ast) { this.fungsi_hati_sgot_ast = fungsi_hati_sgot_ast; }
	
	public String getFungsi_hati_sgpt_alt() { return fungsi_hati_sgpt_alt; }
	public void setFungsi_hati_sgpt_alt(String fungsi_hati_sgpt_alt) { this.fungsi_hati_sgpt_alt = fungsi_hati_sgpt_alt; }
	
	public String getFungsi_hati_total_protein() { return fungsi_hati_total_protein; }
	public void setFungsi_hati_total_protein(String fungsi_hati_total_protein) { this.fungsi_hati_total_protein = fungsi_hati_total_protein; }
	
	public String getGula_darah_glukosa_darah_puasa() { return gula_darah_glukosa_darah_puasa; }
	public void setGula_darah_glukosa_darah_puasa(String gula_darah_glukosa_darah_puasa) { this.gula_darah_glukosa_darah_puasa = gula_darah_glukosa_darah_puasa; }
	
	public String getGula_darah_glukosa_hba1c() { return gula_darah_glukosa_hba1c; }
	public void setGula_darah_glukosa_hba1c(String gula_darah_glukosa_hba1c) { this.gula_darah_glukosa_hba1c = gula_darah_glukosa_hba1c; }
	
	public String getHepatitis_hbeag() { return hepatitis_hbeag; }
	public void setHepatitis_hbeag(String hepatitis_hbeag) { this.hepatitis_hbeag = hepatitis_hbeag; }
	
	public String getHepatitis_hbsag() { return hepatitis_hbsag; }
	public void setHepatitis_hbsag(String hepatitis_hbsag) { this.hepatitis_hbsag = hepatitis_hbsag; }
	
	public String getLemak_darah_hdl_cholesterol() { return lemak_darah_hdl_cholesterol; }
	public void setLemak_darah_hdl_cholesterol(String lemak_darah_hdl_cholesterol) { this.lemak_darah_hdl_cholesterol = lemak_darah_hdl_cholesterol; }
	
	public String getLemak_darah_ldl_cholesterol() { return lemak_darah_ldl_cholesterol; }
	public void setLemak_darah_ldl_cholesterol(String lemak_darah_ldl_cholesterol) { this.lemak_darah_ldl_cholesterol = lemak_darah_ldl_cholesterol; }
	
	public String getLemak_darah_ratio_ldl_hdl() { return lemak_darah_ratio_ldl_hdl; }
	public void setLemak_darah_ratio_ldl_hdl(String lemak_darah_ratio_ldl_hdl) { this.lemak_darah_ratio_ldl_hdl = lemak_darah_ratio_ldl_hdl; }
	
	public String getLemak_darah_ratio_total_chol_hdl() { return lemak_darah_ratio_total_chol_hdl; }
	public void setLemak_darah_ratio_total_chol_hdl(String lemak_darah_ratio_total_chol_hdl) { this.lemak_darah_ratio_total_chol_hdl = lemak_darah_ratio_total_chol_hdl; }
	 
	public String getLemak_darah_total_cholesterol() { return lemak_darah_total_cholesterol; }
	public void setLemak_darah_total_cholesterol(String lemak_darah_total_cholesterol) { this.lemak_darah_total_cholesterol = lemak_darah_total_cholesterol; }
	
	public String getLemak_darah_trigliserida() { return lemak_darah_trigliserida; }
	public void setLemak_darah_trigliserida(String lemak_darah_trigliserida) { this.lemak_darah_trigliserida = lemak_darah_trigliserida; }
	
	public String getLpk_deyut_nadi() { return lpk_deyut_nadi; }
	public void setLpk_deyut_nadi(String lpk_deyut_nadi) { this.lpk_deyut_nadi = lpk_deyut_nadi; }
	
	public String getTumor_marker_afp() { return tumor_marker_afp; }
	public void setTumor_marker_afp(String tumor_marker_afp) { this.tumor_marker_afp = tumor_marker_afp; }
	
	public String getTumor_marker_ca_15_3() { return tumor_marker_ca_15_3; }
	public void setTumor_marker_ca_15_3(String tumor_marker_ca_15_3) { this.tumor_marker_ca_15_3 = tumor_marker_ca_15_3; }
	
	public String getTumor_marker_cea() { return tumor_marker_cea; }
	public void setTumor_marker_cea(String tumor_marker_cea) { this.tumor_marker_cea = tumor_marker_cea; }
	
	public String getTumor_marker_psa() { return tumor_marker_psa; }
	public void setTumor_marker_psa(String tumor_marker_psa) { this.tumor_marker_psa = tumor_marker_psa; }
	
	public String getUrin_bakteri() { return urin_bakteri; }
	public void setUrin_bakteri(String urin_bakteri) { this.urin_bakteri = urin_bakteri; }
	
	public String getUrin_berat_jenis() { return urin_berat_jenis; }
	public void setUrin_berat_jenis(String urin_berat_jenis) { this.urin_berat_jenis = urin_berat_jenis; }
	
	public String getUrin_bilirubin() { return urin_bilirubin; }
	public void setUrin_bilirubin(String urin_bilirubin) { this.urin_bilirubin = urin_bilirubin; }
	
	public String getUrin_darah_samar() { return urin_darah_samar; }
	public void setUrin_darah_samar(String urin_darah_samar) { this.urin_darah_samar = urin_darah_samar; }
	
	public String getUrin_epitel() { return urin_epitel; }
	public void setUrin_epitel(String urin_epitel) { this.urin_epitel = urin_epitel; }
	
	public String getUrin_eritrosit() { return urin_eritrosit; }
	public void setUrin_eritrosit(String urin_eritrosit) { this.urin_eritrosit = urin_eritrosit; }
	
	public String getUrin_glukosa() { return urin_glukosa; }
	public void setUrin_glukosa(String urin_glukosa) { this.urin_glukosa = urin_glukosa; }
	
	public String getUrin_kejernihan() { return urin_kejernihan; }
	public void setUrin_kejernihan(String urin_kejernihan) { this.urin_kejernihan = urin_kejernihan; }
	
	public String getUrin_keton() { return urin_keton; }
	public void setUrin_keton(String urin_keton) { this.urin_keton = urin_keton; }
	
	public String getUrin_kristal() { return urin_kristal; }
	public void setUrin_kristal(String urin_kristal) { this.urin_kristal = urin_kristal; }
	
	public String getUrin_lekosit() { return urin_lekosit; }
	public void setUrin_lekosit(String urin_lekosit) { this.urin_lekosit = urin_lekosit; }
	
	public String getUrin_lekosit_esterase() { return urin_lekosit_esterase; }
	public void setUrin_lekosit_esterase(String urin_lekosit_esterase) { this.urin_lekosit_esterase = urin_lekosit_esterase; }
	
	public String getUrin_nitrit() { return urin_nitrit; }
	public void setUrin_nitrit(String urin_nitrit) { this.urin_nitrit = urin_nitrit; }
	
	public String getUrin_ph() { return urin_ph; }
	public void setUrin_ph(String urin_ph) { this.urin_ph = urin_ph; }
	
	public String getUrin_protein() { return urin_protein; }
	public void setUrin_protein(String urin_protein) { this.urin_protein = urin_protein; }
	
	public String getUrin_silinder() { return urin_silinder; }
	public void setUrin_silinder(String urin_silinder) { this.urin_silinder = urin_silinder; }
	
	public String getUrin_urobilinogen() { return urin_urobilinogen; }
	public void setUrin_urobilinogen(String urin_urobilinogen) { this.urin_urobilinogen = urin_urobilinogen; }
	
	public String getUrin_warna() { return urin_warna; } 
	public void setUrin_warna(String urin_warna) { this.urin_warna = urin_warna; }
	
}
