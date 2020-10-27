package com.ekalife.elions.model.worksheet;

import java.io.Serializable;

public class UwAda implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nv_darah_rutin_hemoglobin;
	private String nv_darah_rutin_leukosit;
	private String nv_darah_rutin_trombosit;
	private String nv_darah_rutin_eritrosit;
	private String nv_darah_rutin_hematokrit;
	private String nv_darah_rutin_led;
	private String nv_darah_rutin_mcv;
	private String nv_darah_rutin_mch;
	private String nv_darah_rutin_mchc;
	private String nv_darah_rutin_rdw;
	private String nv_darah_rutin_eosinofil;
	private String nv_darah_rutin_basofil;
	private String nv_darah_rutin_neutrofil_batang;
	private String nv_darah_rutin_neutrofil_segmen;
	private String nv_darah_rutin_limfosit;
	private String nv_darah_rutin_monosit;
	
	private String nv_gula_darah_glukosa_darah_puasa;
	private String nv_gula_darah_glukosa_pp;
	private String nv_gula_darah_glukosa_hba1c;
	private String nv_lemak_darah_total_cholesterol;
	private String nv_lemak_darah_hdl_cholesterol;
	private String nv_lemak_darah_ldl_cholesterol;
	private String nv_lemak_darah_ratio_total_chol_hdl;
	private String nv_lemak_darah_ratio_ldl_hdl;
	private String nv_lemak_darah_trigliserida;
	private String nv_fungsi_hati_sgot_ast;
	private String nv_fungsi_hati_sgpt_alt;
	private String nv_fungsi_hati_gamma_gt_ggtp;
	private String nv_fungsi_hati_alkali_fosfatase;
	private String nv_fungsi_hati_bilirubin_direk;
	private String nv_fungsi_hati_bilirubin_indirek;
	private String nv_fungsi_hati_bilirubin_total;
	private String nv_fungsi_hati_albumin;
	private String nv_fungsi_hati_globulin;
	private String nv_fungsi_hati_total_protein;
	private String nv_hepatitis_hbsag;
	private String nv_hepatitis_hbeag;
	private String nv_fungsi_ginjal_ureum;
	private String nv_fungsi_ginjal_creatinin;
	private String nv_fungsi_ginjal_asam_urat;
	
	private String reg_spaj;
	private Integer insured_no;
	private Integer flagAda;
	private Integer urutanAda;
	private String tglMcuAda;
	private String tmpMcuAda;
	
	private Integer flagHemoglobin;
	private String hemoglobin;
	private String hemoglobin_kelainan;
	private String hemoglobin_lic_id;
	private String hemoglobin_lic_desc; 
	private Integer flagLeukosit;
	private String leukosit;
	private String leukosit_kelainan;
	private String leukosit_lic_id;
	private String leukosit_lic_desc; 
	private Integer flagEosinofil;
	private String eosinofil;
	private String eosinofil_kelainan;
	private String eosinofil_lic_id;
	private String eosinofil_lic_desc; 	
	private Integer flagBasofil;
	private String basofil;
	private String basofil_kelainan;
	private String basofil_lic_id;
	private String basofil_lic_desc;	
	private Integer flagNeutrofil_batang;
	private String neutrofil_batang;
	private String neutrofil_batang_kelainan;
	private String neutrofil_batang_lic_id;
	private String neutrofil_batang_lic_desc;	
	private Integer flagNeutrofil_segmen;
	private String neutrofil_segmen;
	private String neutrofil_segmen_kelainan;
	private String neutrofil_segmen_lic_id;
	private String neutrofil_segmen_lic_desc;	
	private Integer flagLimfosit;
	private String limfosit;
	private String limfosit_kelainan;
	private String limfosit_lic_id;
	private String limfosit_lic_desc;
	private Integer flagMonosit;
	private String monosit;
	private String monosit_kelainan;
	private String monosit_lic_id;
	private String monosit_lic_desc;	
	private Integer flagTrombosit;
	private String trombosit;
	private String trombosit_kelainan;
	private String trombosit_lic_id;
	private String trombosit_lic_desc;	
	private Integer flagEritrosit;
	private String eritrosit;
	private String eritrosit_kelainan;
	private String eritrosit_lic_id;
	private String eritrosit_lic_desc;	
	private Integer flagHematokrit;
	private String hematokrit;
	private String hematokrit_kelainan;
	private String hematokrit_lic_id;
	private String hematokrit_lic_desc;		
	private Integer flagLed;
	private String led;
	private String led_kelainan;
	private String led_lic_id;
	private String led_lic_desc;	
	private Integer flagMcv; 
	private String mcv;
	private String mcv_kelainan;
	private String mcv_lic_id;
	private String mcv_lic_desc;	
	private Integer flagMch;
	private String mch;
	private String mch_kelainan;
	private String mch_lic_id;
	private String mch_lic_desc;
	private Integer flagMchc;	
	private String mchc;
	private String mchc_kelainan;
	private String mchc_lic_id;
	private String mchc_lic_desc;	
	private Integer flagRdw;
	private String rdw;
	private String rdw_kelainan;
	private String rdw_lic_id;
	private String rdw_lic_desc;		
	
	private Integer flagGulaDarahPuasa;
	private String gula_darah_puasa;
	private String gula_darah_puasa_kelainan;
	private String gula_darah_puasa_lic_id;
	private String gula_darah_puasa_lic_desc;	
	private Integer flagGulaDarahPp;
	private String gula_darah_pp;
	private String gula_darah_pp_kelainan;
	private String gula_darah_pp_lic_id;
	private String gula_darah_pp_lic_desc;	
	private Integer flagHb1c;
	private String hba1c;
	private String hba1c_kelainan;
	private String hba1c_lic_id;
	private String hba1c_lic_desc;
	private Integer satuanChol; // 1: mg/dl 2: mmol/l
	private Double total_cholesterol;
	private Double hdl_cholesterol;
	private Double ldl_cholesterol;
	private String ratio_cholesterol;
	private Double chol_hdl;
	private Double ldl_hdl;
	private String trigiliserida;
	private Integer flagSgot;
	private String sgot;
	private String sgot_kelainan;
	private String sgot_lic_id;
	private String sgot_lic_desc;	
	private Integer flagSgpt;
	private String sgpt;
	private String sgpt_kelainan;
	private String sgpt_lic_id;
	private String sgpt_lic_desc;
	private Integer flagGgt;
	private String ggt;
	private String ggt_kelainan;
	private String ggt_lic_id;
	private String ggt_lic_desc;
	private Integer flagFosfatase_alkali;
	private String fosfatase_alkali;
	private String fosfatase_alkali_kelainan;
	private String fosfatase_alkali_lic_id;
	private String fosfatase_alkali_lic_desc;	
	private Integer flagBilirubin_direk;
	private String bilirubin_direk;
	private String bilirubin_direk_kelainan;
	private String bilirubin_direk_lic_id;
	private String bilirubin_direk_lic_desc;	
	private Integer flagBilirubin_indirek;
	private String bilirubin_indirek;
	private String bilirubin_indirek_kelainan;
	private String bilirubin_indirek_lic_id;
	private String bilirubin_indirek_lic_desc;	
	private Integer flagBilirubin_total;
	private String bilirubin_total;
	private String bilirubin_total_kelainan;
	private String bilirubin_total_lic_id;
	private String bilirubin_total_lic_desc;	
	private Integer flagAlbumin;
	private String albumin;
	private String albumin_kelainan;
	private String albumin_lic_id;
	private String albumin_lic_desc;	
	private Integer flagGlobulin;
	private String globulin;
	private String globulin_kelainan;
	private String globulin_lic_id;
	private String globulin_lic_desc;	
	private Integer flagTotal_protein;
	private String total_protein;
	private String total_protein_kelainan;
	private String total_protein_lic_id;
	private String total_protein_lic_desc;
	private Integer flagHbs_ag;
	private String hbs_ag;
	private String hbs_ag_kelainan;
	private String hbs_ag_lic_id;
	private String hbs_ag_lic_desc;
	private Integer flagHbe_ag;
	private String hbe_ag;
	private String hbe_ag_kelainan;
	private String hbe_ag_lic_id;
	private String hbe_ag_lic_desc;
	private Integer flagCreatinin;
	private String creatinin;
	private String creatinin_kelainan;
	private String creatinin_lic_id;
	private String creatinin_lic_desc;
	private Integer flagUreum;
	private String ureum;
	private String ureum_kelainan;
	private String ureum_lic_id;
	private String ureum_lic_desc;
	private Integer flagAsam_urat;
	private String asam_urat;
	private String asam_urat_kelainan;
	private String asam_urat_lic_id;
	private String asam_urat_lic_desc;
	
	public UwAda() {
		setFlagAda(0); // 0 : ADA 1 : ADAL
		
		setNv_darah_rutin_hemoglobin("13,0 - 18,0 g/dl");
		setNv_darah_rutin_leukosit("4.000 - 11.000 /µL");
		setNv_darah_rutin_trombosit("150.000 - 400.000 /µL");
		setNv_darah_rutin_eritrosit("4,5 - 5,9 10 6/µL");
		setNv_darah_rutin_hematokrit("40 - 50 %");
		setNv_darah_rutin_led("0 - 10 mm/jam");
		setNv_darah_rutin_mcv("82 - 92 fL");
		setNv_darah_rutin_mch("27,5 - 33,2 pg");
		setNv_darah_rutin_mchc("32 - 37 g/dl");
		setNv_darah_rutin_rdw("11,5 - 14,5 %");
		setNv_darah_rutin_eosinofil("1 - 3 %");
		setNv_darah_rutin_basofil("0 - 1 %");
		setNv_darah_rutin_neutrofil_batang("2 - 6 %");
		setNv_darah_rutin_neutrofil_segmen("50 - 70 %");
		setNv_darah_rutin_limfosit("20 - 40 %");
		setNv_darah_rutin_monosit("2 - 8 %");
		
		setNv_gula_darah_glukosa_darah_puasa("70 - 110 mg/dl");
		setNv_gula_darah_glukosa_pp("< 140 mg/dl");
		setNv_gula_darah_glukosa_hba1c("< 6,5 %");
		setNv_lemak_darah_total_cholesterol("<  200 mg/dl");
		setNv_lemak_darah_hdl_cholesterol("40 - 60 mg/dl");
		setNv_lemak_darah_ldl_cholesterol("<  100 mg/dl");
		setNv_lemak_darah_ratio_total_chol_hdl("< 5");
		setNv_lemak_darah_ratio_ldl_hdl("< 3");
		setNv_lemak_darah_trigliserida("< 150 mg/dl");
		
		setNv_fungsi_hati_sgot_ast("10 - 35 U/L");
		setNv_fungsi_hati_sgpt_alt("9 - 43 U/L");
		setNv_fungsi_hati_gamma_gt_ggtp("11 - 50 U/L");
		setNv_fungsi_hati_alkali_fosfatase("20 - 140 U/L");
		setNv_fungsi_hati_bilirubin_direk("< 0,30 mg/dl");
		setNv_fungsi_hati_bilirubin_indirek("< 0,80 mg/dl");
		setNv_fungsi_hati_bilirubin_total("< 1,00 mg/dl");
		setNv_fungsi_hati_albumin("3,5 - 5,3 g/dl");
		setNv_fungsi_hati_globulin("1,9 - 3,5 g/dl");
		setNv_fungsi_hati_total_protein("6,6 - 8,7 g/dl");
		
		setNv_hepatitis_hbsag("non reaktif, <1,0");
		setNv_hepatitis_hbeag("non reaktif, <1,0");
		setNv_fungsi_ginjal_ureum("14,9 - 38,5");
		setNv_fungsi_ginjal_creatinin("0,7 - 1,3 mg/dl");
		setNv_fungsi_ginjal_asam_urat("2,6 - 7,2 mg/dl");
		
		setFlagHemoglobin(0);
		setFlagLeukosit(0);
		setFlagEosinofil(0);
		setFlagBasofil(0);
		setFlagNeutrofil_batang(0);
		setFlagNeutrofil_segmen(0);
		setFlagLimfosit(0);
		setFlagMonosit(0);
		setFlagTrombosit(0);
		setFlagEritrosit(0);
		setFlagHematokrit(0);
		setFlagLed(0);
		setFlagMcv(0);
		setFlagMch(0);
		setFlagMchc(0);
		setFlagRdw(0);
		setFlagSgot(0);
		setFlagSgpt(0);
		setFlagGgt(0);
		setFlagFosfatase_alkali(0);
		setFlagBilirubin_direk(0);
		setFlagBilirubin_indirek(0);
		setFlagBilirubin_total(0);
		setFlagAlbumin(0);
		setFlagGlobulin(0);
		setFlagTotal_protein(0);
		setFlagHbs_ag(0);
		setFlagHbe_ag(0);
		setFlagCreatinin(0);
		setFlagUreum(0);
		setFlagAsam_urat(0);
		
		setFlagGulaDarahPuasa(0);
		setFlagGulaDarahPp(0);
		setFlagHb1c(0);
	}
	
	public String getNv_darah_rutin_basofil() { return nv_darah_rutin_basofil; }
	public void setNv_darah_rutin_basofil(String nv_darah_rutin_basofil) { this.nv_darah_rutin_basofil = nv_darah_rutin_basofil; }
	
	public String getNv_darah_rutin_eosinofil() { return nv_darah_rutin_eosinofil; }
	public void setNv_darah_rutin_eosinofil(String nv_darah_rutin_eosinofil) { this.nv_darah_rutin_eosinofil = nv_darah_rutin_eosinofil; }
	
	public String getNv_darah_rutin_eritrosit() { return nv_darah_rutin_eritrosit; }
	public void setNv_darah_rutin_eritrosit(String nv_darah_rutin_eritrosit) { this.nv_darah_rutin_eritrosit = nv_darah_rutin_eritrosit; }
	
	public String getNv_darah_rutin_hematokrit() { return nv_darah_rutin_hematokrit; }
	public void setNv_darah_rutin_hematokrit(String nv_darah_rutin_hematokrit) { this.nv_darah_rutin_hematokrit = nv_darah_rutin_hematokrit; }
	
	public String getNv_darah_rutin_hemoglobin() { return nv_darah_rutin_hemoglobin; }
	public void setNv_darah_rutin_hemoglobin(String nv_darah_rutin_hemoglobin) { this.nv_darah_rutin_hemoglobin = nv_darah_rutin_hemoglobin; }
	
	public String getNv_darah_rutin_led() { return nv_darah_rutin_led; }
	public void setNv_darah_rutin_led(String nv_darah_rutin_led) { this.nv_darah_rutin_led = nv_darah_rutin_led; }
	
	public String getNv_darah_rutin_leukosit() { return nv_darah_rutin_leukosit; }
	public void setNv_darah_rutin_leukosit(String nv_darah_rutin_leukosit) { this.nv_darah_rutin_leukosit = nv_darah_rutin_leukosit; }
	
	public String getNv_darah_rutin_limfosit() { return nv_darah_rutin_limfosit; }
	public void setNv_darah_rutin_limfosit(String nv_darah_rutin_limfosit) { this.nv_darah_rutin_limfosit = nv_darah_rutin_limfosit; }
	
	public String getNv_darah_rutin_mch() { return nv_darah_rutin_mch; }
	public void setNv_darah_rutin_mch(String nv_darah_rutin_mch) { this.nv_darah_rutin_mch = nv_darah_rutin_mch; }
	
	public String getNv_darah_rutin_mchc() { return nv_darah_rutin_mchc; }
	public void setNv_darah_rutin_mchc(String nv_darah_rutin_mchc) { this.nv_darah_rutin_mchc = nv_darah_rutin_mchc; }
	
	public String getNv_darah_rutin_mcv() { return nv_darah_rutin_mcv; }
	public void setNv_darah_rutin_mcv(String nv_darah_rutin_mcv) { this.nv_darah_rutin_mcv = nv_darah_rutin_mcv; }
	
	public String getNv_darah_rutin_monosit() { return nv_darah_rutin_monosit; }
	public void setNv_darah_rutin_monosit(String nv_darah_rutin_monosit) { this.nv_darah_rutin_monosit = nv_darah_rutin_monosit; }
	
	public String getNv_darah_rutin_neutrofil_batang() { return nv_darah_rutin_neutrofil_batang; }
	public void setNv_darah_rutin_neutrofil_batang(String nv_darah_rutin_neutrofil_batang) { this.nv_darah_rutin_neutrofil_batang = nv_darah_rutin_neutrofil_batang; }
	
	public String getNv_darah_rutin_neutrofil_segmen() { return nv_darah_rutin_neutrofil_segmen; }
	public void setNv_darah_rutin_neutrofil_segmen(String nv_darah_rutin_neutrofil_segmen) { this.nv_darah_rutin_neutrofil_segmen = nv_darah_rutin_neutrofil_segmen; }
	
	public String getNv_darah_rutin_rdw() { return nv_darah_rutin_rdw; }
	public void setNv_darah_rutin_rdw(String nv_darah_rutin_rdw) { this.nv_darah_rutin_rdw = nv_darah_rutin_rdw; }
	
	public String getNv_darah_rutin_trombosit() { return nv_darah_rutin_trombosit; }
	public void setNv_darah_rutin_trombosit(String nv_darah_rutin_trombosit) { this.nv_darah_rutin_trombosit = nv_darah_rutin_trombosit; }
	
	public String getNv_fungsi_ginjal_asam_urat() { return nv_fungsi_ginjal_asam_urat; }
	public void setNv_fungsi_ginjal_asam_urat(String nv_fungsi_ginjal_asam_urat) { this.nv_fungsi_ginjal_asam_urat = nv_fungsi_ginjal_asam_urat; }

	public String getNv_fungsi_ginjal_creatinin() { return nv_fungsi_ginjal_creatinin; }
	public void setNv_fungsi_ginjal_creatinin(String nv_fungsi_ginjal_creatinin) { this.nv_fungsi_ginjal_creatinin = nv_fungsi_ginjal_creatinin; }

	public String getNv_fungsi_ginjal_ureum() { return nv_fungsi_ginjal_ureum; }
	public void setNv_fungsi_ginjal_ureum(String nv_fungsi_ginjal_ureum) { this.nv_fungsi_ginjal_ureum = nv_fungsi_ginjal_ureum; }

	public String getNv_fungsi_hati_albumin() { return nv_fungsi_hati_albumin; }
	public void setNv_fungsi_hati_albumin(String nv_fungsi_hati_albumin) { this.nv_fungsi_hati_albumin = nv_fungsi_hati_albumin; }

	public String getNv_fungsi_hati_alkali_fosfatase() { return nv_fungsi_hati_alkali_fosfatase; }
	public void setNv_fungsi_hati_alkali_fosfatase(String nv_fungsi_hati_alkali_fosfatase) { this.nv_fungsi_hati_alkali_fosfatase = nv_fungsi_hati_alkali_fosfatase; }

	public String getNv_fungsi_hati_bilirubin_direk() { return nv_fungsi_hati_bilirubin_direk; }
	public void setNv_fungsi_hati_bilirubin_direk(String nv_fungsi_hati_bilirubin_direk) { this.nv_fungsi_hati_bilirubin_direk = nv_fungsi_hati_bilirubin_direk; }

	public String getNv_fungsi_hati_bilirubin_indirek() { return nv_fungsi_hati_bilirubin_indirek; }
	public void setNv_fungsi_hati_bilirubin_indirek(String nv_fungsi_hati_bilirubin_indirek) { this.nv_fungsi_hati_bilirubin_indirek = nv_fungsi_hati_bilirubin_indirek; }

	public String getNv_fungsi_hati_bilirubin_total() { return nv_fungsi_hati_bilirubin_total; }
	public void setNv_fungsi_hati_bilirubin_total(String nv_fungsi_hati_bilirubin_total) { this.nv_fungsi_hati_bilirubin_total = nv_fungsi_hati_bilirubin_total; }

	public String getNv_fungsi_hati_gamma_gt_ggtp() { return nv_fungsi_hati_gamma_gt_ggtp; }
	public void setNv_fungsi_hati_gamma_gt_ggtp(String nv_fungsi_hati_gamma_gt_ggtp) { this.nv_fungsi_hati_gamma_gt_ggtp = nv_fungsi_hati_gamma_gt_ggtp; }

	public String getNv_fungsi_hati_globulin() { return nv_fungsi_hati_globulin; }
	public void setNv_fungsi_hati_globulin(String nv_fungsi_hati_globulin) { this.nv_fungsi_hati_globulin = nv_fungsi_hati_globulin; }

	public String getNv_fungsi_hati_sgot_ast() { return nv_fungsi_hati_sgot_ast; }
	public void setNv_fungsi_hati_sgot_ast(String nv_fungsi_hati_sgot_ast) { this.nv_fungsi_hati_sgot_ast = nv_fungsi_hati_sgot_ast; }

	public String getNv_fungsi_hati_sgpt_alt() { return nv_fungsi_hati_sgpt_alt; }
	public void setNv_fungsi_hati_sgpt_alt(String nv_fungsi_hati_sgpt_alt) { this.nv_fungsi_hati_sgpt_alt = nv_fungsi_hati_sgpt_alt; }

	public String getNv_fungsi_hati_total_protein() { return nv_fungsi_hati_total_protein; }
	public void setNv_fungsi_hati_total_protein(String nv_fungsi_hati_total_protein) { this.nv_fungsi_hati_total_protein = nv_fungsi_hati_total_protein; }

	public String getNv_gula_darah_glukosa_darah_puasa() { return nv_gula_darah_glukosa_darah_puasa; }
	public void setNv_gula_darah_glukosa_darah_puasa(String nv_gula_darah_glukosa_darah_puasa) { this.nv_gula_darah_glukosa_darah_puasa = nv_gula_darah_glukosa_darah_puasa; }

	public String getNv_gula_darah_glukosa_pp() { return nv_gula_darah_glukosa_pp; }
	public void setNv_gula_darah_glukosa_pp(String nv_gula_darah_glukosa_pp) { this.nv_gula_darah_glukosa_pp = nv_gula_darah_glukosa_pp; }

	public String getNv_gula_darah_glukosa_hba1c() { return nv_gula_darah_glukosa_hba1c; }
	public void setNv_gula_darah_glukosa_hba1c(String nv_gula_darah_glukosa_hba1c) { this.nv_gula_darah_glukosa_hba1c = nv_gula_darah_glukosa_hba1c; }

	public String getNv_hepatitis_hbeag() { return nv_hepatitis_hbeag; }
	public void setNv_hepatitis_hbeag(String nv_hepatitis_hbeag) { this.nv_hepatitis_hbeag = nv_hepatitis_hbeag; }

	public String getNv_hepatitis_hbsag() { return nv_hepatitis_hbsag; }
	public void setNv_hepatitis_hbsag(String nv_hepatitis_hbsag) { this.nv_hepatitis_hbsag = nv_hepatitis_hbsag; }

	public String getNv_lemak_darah_hdl_cholesterol() { return nv_lemak_darah_hdl_cholesterol; }
	public void setNv_lemak_darah_hdl_cholesterol(String nv_lemak_darah_hdl_cholesterol) { this.nv_lemak_darah_hdl_cholesterol = nv_lemak_darah_hdl_cholesterol; }

	public String getNv_lemak_darah_ldl_cholesterol() { return nv_lemak_darah_ldl_cholesterol; }
	public void setNv_lemak_darah_ldl_cholesterol(String nv_lemak_darah_ldl_cholesterol) { this.nv_lemak_darah_ldl_cholesterol = nv_lemak_darah_ldl_cholesterol; }

	public String getNv_lemak_darah_ratio_total_chol_hdl() { return nv_lemak_darah_ratio_total_chol_hdl; }
	public void setNv_lemak_darah_ratio_total_chol_hdl(String nv_lemak_darah_ratio_total_chol_hdl) { this.nv_lemak_darah_ratio_total_chol_hdl = nv_lemak_darah_ratio_total_chol_hdl; }

	public String getNv_lemak_darah_total_cholesterol() { return nv_lemak_darah_total_cholesterol; }
 	public void setNv_lemak_darah_total_cholesterol(String nv_lemak_darah_total_cholesterol) { this.nv_lemak_darah_total_cholesterol = nv_lemak_darah_total_cholesterol; }

	public String getNv_lemak_darah_trigliserida() { return nv_lemak_darah_trigliserida; }
	public void setNv_lemak_darah_trigliserida(String nv_lemak_darah_trigliserida) { this.nv_lemak_darah_trigliserida = nv_lemak_darah_trigliserida; }

	public Double getChol_hdl() { return chol_hdl; }
	public void setChol_hdl(Double chol_hdl) { this.chol_hdl = chol_hdl; }

	public String getNv_lemak_darah_ratio_ldl_hdl() { return nv_lemak_darah_ratio_ldl_hdl; }
	public void setNv_lemak_darah_ratio_ldl_hdl(String nv_lemak_darah_ratio_ldl_hdl) { this.nv_lemak_darah_ratio_ldl_hdl = nv_lemak_darah_ratio_ldl_hdl; }
	

	public Integer getFlagAda() { return flagAda; }
	public void setFlagAda(Integer flagAda) { this.flagAda = flagAda; }

	public String getBasofil() { return basofil; }
	public void setBasofil(String basofil) { this.basofil = basofil; }
	
	public String getBasofil_kelainan() { return basofil_kelainan; }
	public void setBasofil_kelainan(String basofil_kelainan) { this.basofil_kelainan = basofil_kelainan; }
	
	public String getBasofil_lic_desc() { return basofil_lic_desc; }
	public void setBasofil_lic_desc(String basofil_lic_desc) { this.basofil_lic_desc = basofil_lic_desc; }
	
	public String getBasofil_lic_id() { return basofil_lic_id; }
	public void setBasofil_lic_id(String basofil_lic_id) { this.basofil_lic_id = basofil_lic_id; }
	
	public String getEosinofil() { return eosinofil; }
	public void setEosinofil(String eosinofil) { this.eosinofil = eosinofil; }
	
	public String getEosinofil_kelainan() { return eosinofil_kelainan; }
	public void setEosinofil_kelainan(String eosinofil_kelainan) { this.eosinofil_kelainan = eosinofil_kelainan; }
	
	public String getEosinofil_lic_desc() { return eosinofil_lic_desc; }
	public void setEosinofil_lic_desc(String eosinofil_lic_desc) { this.eosinofil_lic_desc = eosinofil_lic_desc; }
	
	public String getEosinofil_lic_id() { return eosinofil_lic_id; }
	public void setEosinofil_lic_id(String eosinofil_lic_id) { this.eosinofil_lic_id = eosinofil_lic_id; }
	
	public String getEritrosit() { return eritrosit; }
	public void setEritrosit(String eritrosit) { this.eritrosit = eritrosit; }
	
	public String getEritrosit_kelainan() { return eritrosit_kelainan; }
	public void setEritrosit_kelainan(String eritrosit_kelainan) { this.eritrosit_kelainan = eritrosit_kelainan; }
	
	public String getEritrosit_lic_desc() { return eritrosit_lic_desc; }
	public void setEritrosit_lic_desc(String eritrosit_lic_desc) { this.eritrosit_lic_desc = eritrosit_lic_desc; }
	
	public String getEritrosit_lic_id() { return eritrosit_lic_id; }
	public void setEritrosit_lic_id(String eritrosit_lic_id) { this.eritrosit_lic_id = eritrosit_lic_id; }
	
	public Integer getFlagBasofil() { return flagBasofil; }
	public void setFlagBasofil(Integer flagBasofil) { this.flagBasofil = flagBasofil; }
	
	public Integer getFlagEosinofil() { return flagEosinofil; }
	public void setFlagEosinofil(Integer flagEosinofil) { this.flagEosinofil = flagEosinofil; }
	
	public Integer getFlagEritrosit() { return flagEritrosit; }
	public void setFlagEritrosit(Integer flagEritrosit) { this.flagEritrosit = flagEritrosit; }
	
	public Integer getFlagHemoglobin() { return flagHemoglobin; }
	public void setFlagHemoglobin(Integer flagHemoglobin) { this.flagHemoglobin = flagHemoglobin; }
	
	public Integer getFlagHematokrit() { return flagHematokrit; }
	public void setFlagHematokrit(Integer flagHematokrit) { this.flagHematokrit = flagHematokrit; }

	public Integer getFlagLed() { return flagLed; }
	public void setFlagLed(Integer flagLed) { this.flagLed = flagLed; }
	
	public Integer getFlagLeukosit() { return flagLeukosit; }
	public void setFlagLeukosit(Integer flagLeukosit) { this.flagLeukosit = flagLeukosit; }
	
	public Integer getFlagLimfosit() { return flagLimfosit; }
	public void setFlagLimfosit(Integer flagLimfosit) { this.flagLimfosit = flagLimfosit; }
	
	public Integer getFlagMch() { return flagMch; }
	public void setFlagMch(Integer flagMch) { this.flagMch = flagMch; }
	
	public Integer getFlagMchc() { return flagMchc; }
	public void setFlagMchc(Integer flagMchc) { this.flagMchc = flagMchc; }
	
	public Integer getFlagMcv() { return flagMcv; }
	public void setFlagMcv(Integer flagMcv) { this.flagMcv = flagMcv; }
	
	public Integer getFlagMonosit() { return flagMonosit; }
	public void setFlagMonosit(Integer flagMonosit) { this.flagMonosit = flagMonosit; }
	
	public Integer getFlagNeutrofil_batang() { return flagNeutrofil_batang; }
	public void setFlagNeutrofil_batang(Integer flagNeutrofil_batang) { this.flagNeutrofil_batang = flagNeutrofil_batang; }
	
	public Integer getFlagNeutrofil_segmen() { return flagNeutrofil_segmen; }
	public void setFlagNeutrofil_segmen(Integer flagNeutrofil_segmen) { this.flagNeutrofil_segmen = flagNeutrofil_segmen; }
	
	public Integer getFlagRdw() { return flagRdw; }
	public void setFlagRdw(Integer flagRdw) { this.flagRdw = flagRdw; }
	
	public Integer getFlagTrombosit() { return flagTrombosit; }
	public void setFlagTrombosit(Integer flagTrombosit) { this.flagTrombosit = flagTrombosit; }
	
	public String getHemoglobin() { return hemoglobin; }
	public void setHemoglobin(String hemoglobin) { this.hemoglobin = hemoglobin; }
	
	public String getHemoglobin_kelainan() { return hemoglobin_kelainan; }
	public void setHemoglobin_kelainan(String hemoglobin_kelainan) { this.hemoglobin_kelainan = hemoglobin_kelainan; }
	
	public String getHemoglobin_lic_desc() { return hemoglobin_lic_desc; }
	public void setHemoglobin_lic_desc(String hemoglobin_lic_desc) { this.hemoglobin_lic_desc = hemoglobin_lic_desc; }
	
	public String getHemoglobin_lic_id() { return hemoglobin_lic_id; }
	public void setHemoglobin_lic_id(String hemoglobin_lic_id) { this.hemoglobin_lic_id = hemoglobin_lic_id; }
	
	public String getHematokrit() { return hematokrit; }
	public void setHematokrit(String hematokrit) { this.hematokrit = hematokrit; }

	public String getHematokrit_kelainan() { return hematokrit_kelainan; }
	public void setHematokrit_kelainan(String hematokrit_kelainan) { this.hematokrit_kelainan = hematokrit_kelainan; }

	public String getHematokrit_lic_desc() { return hematokrit_lic_desc; }
	public void setHematokrit_lic_desc(String hematokrit_lic_desc) { this.hematokrit_lic_desc = hematokrit_lic_desc; }

	public String getHematokrit_lic_id() { return hematokrit_lic_id; }
	public void setHematokrit_lic_id(String hematokrit_lic_id) { this.hematokrit_lic_id = hematokrit_lic_id; }

	public String getHemotokrit_lic_id() { return hematokrit_lic_id; }
	public void setHemotokrit_lic_id(String hemotokrit_lic_id) { this.hematokrit_lic_id = hematokrit_lic_id; }
	
	public String getLed() { return led; }
	public void setLed(String led) { this.led = led; }
	
	public String getLed_kelainan() { return led_kelainan; }
	public void setLed_kelainan(String led_kelainan) { this.led_kelainan = led_kelainan; }
	
	public String getLed_lic_desc() { return led_lic_desc; }
	public void setLed_lic_desc(String led_lic_desc) { this.led_lic_desc = led_lic_desc; }
	
	public String getLed_lic_id() { return led_lic_id; }
	public void setLed_lic_id(String led_lic_id) { this.led_lic_id = led_lic_id; }
	
	public String getLeukosit() { return leukosit; }
	public void setLeukosit(String leukosit) { this.leukosit = leukosit; }
	
	public String getLeukosit_kelainan() { return leukosit_kelainan; }
	public void setLeukosit_kelainan(String leukosit_kelainan) { this.leukosit_kelainan = leukosit_kelainan; }
	
	public String getLeukosit_lic_desc() { return leukosit_lic_desc; }
	public void setLeukosit_lic_desc(String leukosit_lic_desc) { this.leukosit_lic_desc = leukosit_lic_desc; }
	
	public String getLeukosit_lic_id() { return leukosit_lic_id; }
	public void setLeukosit_lic_id(String leukosit_lic_id) { this.leukosit_lic_id = leukosit_lic_id; }
	
	public String getLimfosit() { return limfosit; }
	public void setLimfosit(String limfosit) { this.limfosit = limfosit; }
	
	public String getLimfosit_kelainan() { return limfosit_kelainan; }
	public void setLimfosit_kelainan(String limfosit_kelainan) { this.limfosit_kelainan = limfosit_kelainan; }

	public String getLimfosit_lic_desc() { return limfosit_lic_desc; }
	public void setLimfosit_lic_desc(String limfosit_lic_desc) { this.limfosit_lic_desc = limfosit_lic_desc; }

	public String getLimfosit_lic_id() { return limfosit_lic_id; }
	public void setLimfosit_lic_id(String limfosit_lic_id) { this.limfosit_lic_id = limfosit_lic_id; }

	public String getMch() { return mch; }
	public void setMch(String mch) { this.mch = mch; }
	
	public String getMch_kelainan() { return mch_kelainan; }
	public void setMch_kelainan(String mch_kelainan) { this.mch_kelainan = mch_kelainan; }
	
	public String getMch_lic_desc() { return mch_lic_desc; }
	public void setMch_lic_desc(String mch_lic_desc) { this.mch_lic_desc = mch_lic_desc; }
	
	public String getMch_lic_id() { return mch_lic_id; }
	public void setMch_lic_id(String mch_lic_id) { this.mch_lic_id = mch_lic_id; }
	
	public String getMchc() { return mchc; }
	public void setMchc(String mchc) { this.mchc = mchc; }
	
	public String getMchc_kelainan() { return mchc_kelainan; }
	public void setMchc_kelainan(String mchc_kelainan) { this.mchc_kelainan = mchc_kelainan; }
	
	public String getMchc_lic_desc() { return mchc_lic_desc; }
	public void setMchc_lic_desc(String mchc_lic_desc) { this.mchc_lic_desc = mchc_lic_desc; }
	
	public String getMchc_lic_id() { return mchc_lic_id; }
	public void setMchc_lic_id(String mchc_lic_id) { this.mchc_lic_id = mchc_lic_id; }
	
	public String getMcv() { return mcv; }
	public void setMcv(String mcv) { this.mcv = mcv; }
	
	public String getMcv_kelainan() { return mcv_kelainan; }
	public void setMcv_kelainan(String mcv_kelainan) { this.mcv_kelainan = mcv_kelainan; }
	
	public String getMcv_lic_desc() { return mcv_lic_desc; }
	public void setMcv_lic_desc(String mcv_lic_desc) { this.mcv_lic_desc = mcv_lic_desc; }
	
	public String getMcv_lic_id() { return mcv_lic_id; }
	public void setMcv_lic_id(String mcv_lic_id) { this.mcv_lic_id = mcv_lic_id; }
	
	public String getMonosit() { return monosit; }
	public void setMonosit(String monosit) { this.monosit = monosit; }
	
	public String getMonosit_kelainan() { return monosit_kelainan; }
	public void setMonosit_kelainan(String monosit_kelainan) { this.monosit_kelainan = monosit_kelainan; }

	public String getMonosit_lic_desc() { return monosit_lic_desc; }
	public void setMonosit_lic_desc(String monosit_lic_desc) { this.monosit_lic_desc = monosit_lic_desc; }

	public String getMonosit_lic_id() { return monosit_lic_id; }
	public void setMonosit_lic_id(String monosit_lic_id) { this.monosit_lic_id = monosit_lic_id; }

	public String getNeutrofil_batang() { return neutrofil_batang; }
	public void setNeutrofil_batang(String neutrofil_batang) { this.neutrofil_batang = neutrofil_batang; }
	
	public String getNeutrofil_batang_kelainan() { return neutrofil_batang_kelainan; }
	public void setNeutrofil_batang_kelainan(String neutrofil_batang_kelainan) { this.neutrofil_batang_kelainan = neutrofil_batang_kelainan; }
	
	public String getNeutrofil_batang_lic_desc() { return neutrofil_batang_lic_desc; }
	public void setNeutrofil_batang_lic_desc(String neutrofil_batang_lic_desc) { this.neutrofil_batang_lic_desc = neutrofil_batang_lic_desc; }
	
	public String getNeutrofil_batang_lic_id() { return neutrofil_batang_lic_id; }
	public void setNeutrofil_batang_lic_id(String neutrofil_batang_lic_id) { this.neutrofil_batang_lic_id = neutrofil_batang_lic_id; }
	
	public String getNeutrofil_segmen() { return neutrofil_segmen; }
	public void setNeutrofil_segmen(String neutrofil_segmen) { this.neutrofil_segmen = neutrofil_segmen; }
	
	public String getNeutrofil_segmen_kelainan() { return neutrofil_segmen_kelainan; }
	public void setNeutrofil_segmen_kelainan(String neutrofil_segmen_kelainan) { this.neutrofil_segmen_kelainan = neutrofil_segmen_kelainan; }
	
	public String getNeutrofil_segmen_lic_desc() { return neutrofil_segmen_lic_desc; }
	public void setNeutrofil_segmen_lic_desc(String neutrofil_segmen_lic_desc) { this.neutrofil_segmen_lic_desc = neutrofil_segmen_lic_desc; }
	
	public String getNeutrofil_segmen_lic_id() { return neutrofil_segmen_lic_id; }
	public void setNeutrofil_segmen_lic_id(String neutrofil_segmen_lic_id) { this.neutrofil_segmen_lic_id = neutrofil_segmen_lic_id; }
	
	public String getRdw() { return rdw; }
	public void setRdw(String rdw) { this.rdw = rdw; } 
	
	public String getRdw_kelainan() { return rdw_kelainan; }
	public void setRdw_kelainan(String rdw_kelainan) { this.rdw_kelainan = rdw_kelainan; }
	
	public String getRdw_lic_desc() { return rdw_lic_desc; }
	public void setRdw_lic_desc(String rdw_lic_desc) { this.rdw_lic_desc = rdw_lic_desc; }
	
	public String getRdw_lic_id() { return rdw_lic_id; }
	public void setRdw_lic_id(String rdw_lic_id) { this.rdw_lic_id = rdw_lic_id; }
	
	public String getTglMcuAda() { return tglMcuAda; }
	public void setTglMcuAda(String tglMcuAda) { this.tglMcuAda = tglMcuAda; }
	
	public String getTmpMcuAda() { return tmpMcuAda; }
	public void setTmpMcuAda(String tmpMcuAda) { this.tmpMcuAda = tmpMcuAda; }
	
	public String getTrombosit() { return trombosit; }
	public void setTrombosit(String trombosit) { this.trombosit = trombosit; }
	
	public String getTrombosit_kelainan() { return trombosit_kelainan; }
	public void setTrombosit_kelainan(String trombosit_kelainan) { this.trombosit_kelainan = trombosit_kelainan; } 
	
	public String getTrombosit_lic_desc() { return trombosit_lic_desc; }
	public void setTrombosit_lic_desc(String trombosit_lic_desc) { this.trombosit_lic_desc = trombosit_lic_desc; }

	public String getTrombosit_lic_id() { return trombosit_lic_id; }
	public void setTrombosit_lic_id(String trombosit_lic_id) { this.trombosit_lic_id = trombosit_lic_id; }

	public Integer getUrutanAda() { return urutanAda; }
	public void setUrutanAda(Integer urutanAda) { this.urutanAda = urutanAda; }

	
	public Integer getFlagGulaDarahPuasa() { return flagGulaDarahPuasa; }
	public void setFlagGulaDarahPuasa(Integer flagGulaDarahPuasa) { this.flagGulaDarahPuasa = flagGulaDarahPuasa; }

	public Integer getFlagHb1c() { return flagHb1c; }
	public void setFlagHb1c(Integer flagHb1c) { this.flagHb1c = flagHb1c; }

	public String getGula_darah_puasa() { return gula_darah_puasa; }
	public void setGula_darah_puasa(String gula_darah_puasa) { this.gula_darah_puasa = gula_darah_puasa; }

	public String getGula_darah_puasa_kelainan() { return gula_darah_puasa_kelainan; }
	public void setGula_darah_puasa_kelainan(String gula_darah_puasa_kelainan) { this.gula_darah_puasa_kelainan = gula_darah_puasa_kelainan; }

	public String getGula_darah_puasa_lic_desc() { return gula_darah_puasa_lic_desc; }
	public void setGula_darah_puasa_lic_desc(String gula_darah_puasa_lic_desc) { this.gula_darah_puasa_lic_desc = gula_darah_puasa_lic_desc; }

	public String getGula_darah_puasa_lic_id() { return gula_darah_puasa_lic_id; }
	public void setGula_darah_puasa_lic_id(String gula_darah_puasa_lic_id) { this.gula_darah_puasa_lic_id = gula_darah_puasa_lic_id; }

 	public Integer getFlagGulaDarahPp() { return flagGulaDarahPp; }
	public void setFlagGulaDarahPp(Integer flagGulaDarahPp) { this.flagGulaDarahPp = flagGulaDarahPp; }

	public String getGula_darah_pp() { return gula_darah_pp; }
	public void setGula_darah_pp(String gula_darah_pp) { this.gula_darah_pp = gula_darah_pp; }

	public String getGula_darah_pp_kelainan() { return gula_darah_pp_kelainan; }
	public void setGula_darah_pp_kelainan(String gula_darah_pp_kelainan) { this.gula_darah_pp_kelainan = gula_darah_pp_kelainan; }

	public String getGula_darah_pp_lic_desc() { return gula_darah_pp_lic_desc; }
	public void setGula_darah_pp_lic_desc(String gula_darah_pp_lic_desc) { this.gula_darah_pp_lic_desc = gula_darah_pp_lic_desc; }

	public String getGula_darah_pp_lic_id() {
		return gula_darah_pp_lic_id;
	}

	public void setGula_darah_pp_lic_id(String gula_darah_pp_lic_id) {
		this.gula_darah_pp_lic_id = gula_darah_pp_lic_id;
	}

	public String getHba1c() { return hba1c; }
	public void setHba1c(String hba1c) { this.hba1c = hba1c; }

 	public String getHba1c_kelainan() { return hba1c_kelainan; }
	public void setHba1c_kelainan(String hba1c_kelainan) { this.hba1c_kelainan = hba1c_kelainan; }

	public String getHba1c_lic_desc() { return hba1c_lic_desc; }
	public void setHba1c_lic_desc(String hba1c_lic_desc) { this.hba1c_lic_desc = hba1c_lic_desc; }

	public String getHba1c_lic_id() { return hba1c_lic_id; }
	public void setHba1c_lic_id(String hba1c_lic_id) { this.hba1c_lic_id = hba1c_lic_id; }

	public Double getHdl_cholesterol() { return hdl_cholesterol; }
	public void setHdl_cholesterol(Double hdl_cholesterol) { this.hdl_cholesterol = hdl_cholesterol; }

	public Double getLdl_cholesterol() { return ldl_cholesterol; }
	public void setLdl_cholesterol(Double ldl_cholesterol) { this.ldl_cholesterol = ldl_cholesterol; }

	public Double getLdl_hdl() { return ldl_hdl; }
	public void setLdl_hdl(Double ldl_hdl) { this.ldl_hdl = ldl_hdl; }

	public String getRatio_cholesterol() { return ratio_cholesterol; }
	public void setRatio_cholesterol(String ratio_cholesterol) { this.ratio_cholesterol = ratio_cholesterol; }

	public Integer getSatuanChol() { return satuanChol; }
	public void setSatuanChol(Integer satuanChol) { this.satuanChol = satuanChol; }

	public Double getTotal_cholesterol() { return total_cholesterol; }
	public void setTotal_cholesterol(Double total_cholesterol) { this.total_cholesterol = total_cholesterol; }
	
	public String getTrigiliserida() { return trigiliserida; }
	public void setTrigiliserida(String trigiliserida) { this.trigiliserida = trigiliserida; }

	public String getAlbumin() { return albumin; }
	public void setAlbumin(String albumin) { this.albumin = albumin; }

	public String getAlbumin_kelainan() { return albumin_kelainan; }
	public void setAlbumin_kelainan(String albumin_kelainan) { this.albumin_kelainan = albumin_kelainan; }

	public String getAlbumin_lic_desc() { return albumin_lic_desc; }
	public void setAlbumin_lic_desc(String albumin_lic_desc) { this.albumin_lic_desc = albumin_lic_desc; }

	public String getAlbumin_lic_id() { return albumin_lic_id; }
	public void setAlbumin_lic_id(String albumin_lic_id) { this.albumin_lic_id = albumin_lic_id; }

	public String getAsam_urat() { return asam_urat; }
	public void setAsam_urat(String asam_urat) { this.asam_urat = asam_urat; }

	public String getAsam_urat_kelainan() { return asam_urat_kelainan; }
	public void setAsam_urat_kelainan(String asam_urat_kelainan) { this.asam_urat_kelainan = asam_urat_kelainan; }

	public String getAsam_urat_lic_desc() { return asam_urat_lic_desc; }
	public void setAsam_urat_lic_desc(String asam_urat_lic_desc) { this.asam_urat_lic_desc = asam_urat_lic_desc; }

	public String getAsam_urat_lic_id() { return asam_urat_lic_id; }
	public void setAsam_urat_lic_id(String asam_urat_lic_id) { this.asam_urat_lic_id = asam_urat_lic_id; }

	public String getBilirubin_direk() { return bilirubin_direk; }
	public void setBilirubin_direk(String bilirubin_direk) { this.bilirubin_direk = bilirubin_direk; }

	public String getBilirubin_direk_kelainan() { return bilirubin_direk_kelainan; }
	public void setBilirubin_direk_kelainan(String bilirubin_direk_kelainan) { this.bilirubin_direk_kelainan = bilirubin_direk_kelainan; }

	public String getBilirubin_direk_lic_desc() { return bilirubin_direk_lic_desc; }
	public void setBilirubin_direk_lic_desc(String bilirubin_direk_lic_desc) { this.bilirubin_direk_lic_desc = bilirubin_direk_lic_desc; }

	public String getBilirubin_direk_lic_id() { return bilirubin_direk_lic_id; }
	public void setBilirubin_direk_lic_id(String bilirubin_direk_lic_id) { this.bilirubin_direk_lic_id = bilirubin_direk_lic_id; }

	public String getBilirubin_indirek() { return bilirubin_indirek; }
	public void setBilirubin_indirek(String bilirubin_indirek) { this.bilirubin_indirek = bilirubin_indirek; }

	public String getBilirubin_indirek_kelainan() { return bilirubin_indirek_kelainan; }
	public void setBilirubin_indirek_kelainan(String bilirubin_indirek_kelainan) { this.bilirubin_indirek_kelainan = bilirubin_indirek_kelainan; }

	public String getBilirubin_indirek_lic_desc() { return bilirubin_indirek_lic_desc; }
	public void setBilirubin_indirek_lic_desc(String bilirubin_indirek_lic_desc) { this.bilirubin_indirek_lic_desc = bilirubin_indirek_lic_desc; }

	public String getBilirubin_indirek_lic_id() { return bilirubin_indirek_lic_id; }
	public void setBilirubin_indirek_lic_id(String bilirubin_indirek_lic_id) { this.bilirubin_indirek_lic_id = bilirubin_indirek_lic_id; }

	public String getBilirubin_total() { return bilirubin_total; }
	public void setBilirubin_total(String bilirubin_total) { this.bilirubin_total = bilirubin_total; }

	public String getBilirubin_total_kelainan() { return bilirubin_total_kelainan; }
	public void setBilirubin_total_kelainan(String bilirubin_total_kelainan) { this.bilirubin_total_kelainan = bilirubin_total_kelainan; }

	public String getBilirubin_total_lic_desc() { return bilirubin_total_lic_desc; }
	public void setBilirubin_total_lic_desc(String bilirubin_total_lic_desc) { this.bilirubin_total_lic_desc = bilirubin_total_lic_desc; }

	public String getBilirubin_total_lic_id() { return bilirubin_total_lic_id; }
	public void setBilirubin_total_lic_id(String bilirubin_total_lic_id) { this.bilirubin_total_lic_id = bilirubin_total_lic_id; }

	public String getCreatinin() { return creatinin; }
	public void setCreatinin(String creatinin) { this.creatinin = creatinin; }

	public String getCreatinin_kelainan() { return creatinin_kelainan; }
	public void setCreatinin_kelainan(String creatinin_kelainan) { this.creatinin_kelainan = creatinin_kelainan; }

 	public String getCreatinin_lic_desc() { return creatinin_lic_desc; }
	public void setCreatinin_lic_desc(String creatinin_lic_desc) { this.creatinin_lic_desc = creatinin_lic_desc; }

	public String getCreatinin_lic_id() { return creatinin_lic_id; }
	public void setCreatinin_lic_id(String creatinin_lic_id) { this.creatinin_lic_id = creatinin_lic_id; }

	public Integer getFlagAlbumin() { return flagAlbumin; }
	public void setFlagAlbumin(Integer flagAlbumin) { this.flagAlbumin = flagAlbumin; }

	public Integer getFlagAsam_urat() { return flagAsam_urat; }
	public void setFlagAsam_urat(Integer flagAsam_urat) { this.flagAsam_urat = flagAsam_urat; }

	public Integer getFlagBilirubin_direk() { return flagBilirubin_direk; }
	public void setFlagBilirubin_direk(Integer flagBilirubin_direk) { this.flagBilirubin_direk = flagBilirubin_direk; }

 	public Integer getFlagBilirubin_indirek() { return flagBilirubin_indirek; }
	public void setFlagBilirubin_indirek(Integer flagBilirubin_indirek) { this.flagBilirubin_indirek = flagBilirubin_indirek; }

	public Integer getFlagBilirubin_total() { return flagBilirubin_total; }
	public void setFlagBilirubin_total(Integer flagBilirubin_total) { this.flagBilirubin_total = flagBilirubin_total; }

	public Integer getFlagCreatinin() { return flagCreatinin; }
	public void setFlagCreatinin(Integer flagCreatinin) { this.flagCreatinin = flagCreatinin; }

	public Integer getFlagFosfatase_alkali() { return flagFosfatase_alkali; }
	public void setFlagFosfatase_alkali(Integer flagFosfatase_alkali) { this.flagFosfatase_alkali = flagFosfatase_alkali; }

	public Integer getFlagGgt() { return flagGgt; }
	public void setFlagGgt(Integer flagGgt) { this.flagGgt = flagGgt; }

	public Integer getFlagGlobulin() { return flagGlobulin; }
	public void setFlagGlobulin(Integer flagGlobulin) { this.flagGlobulin = flagGlobulin; }

	public Integer getFlagHbe_ag() { return flagHbe_ag; }
	public void setFlagHbe_ag(Integer flagHbe_ag) { this.flagHbe_ag = flagHbe_ag; }

	public Integer getFlagHbs_ag() { return flagHbs_ag; }
	public void setFlagHbs_ag(Integer flagHbs_ag) { this.flagHbs_ag = flagHbs_ag; }

 	public Integer getFlagSgot() { return flagSgot; }
	public void setFlagSgot(Integer flagSgot) { this.flagSgot = flagSgot; }

	public Integer getFlagSgpt() { return flagSgpt; }
	public void setFlagSgpt(Integer flagSgpt) { this.flagSgpt = flagSgpt; }

	public Integer getFlagTotal_protein() { return flagTotal_protein; }
	public void setFlagTotal_protein(Integer flagTotal_protein) { this.flagTotal_protein = flagTotal_protein; }

 	public Integer getFlagUreum() { return flagUreum; }
	public void setFlagUreum(Integer flagUreum) { this.flagUreum = flagUreum; }

	public String getFosfatase_alkali() { return fosfatase_alkali; }
	public void setFosfatase_alkali(String fosfatase_alkali) { this.fosfatase_alkali = fosfatase_alkali; }

	public String getFosfatase_alkali_kelainan() { return fosfatase_alkali_kelainan; }
	public void setFosfatase_alkali_kelainan(String fosfatase_alkali_kelainan) { this.fosfatase_alkali_kelainan = fosfatase_alkali_kelainan; }

	public String getFosfatase_alkali_lic_desc() { return fosfatase_alkali_lic_desc; }
	public void setFosfatase_alkali_lic_desc(String fosfatase_alkali_lic_desc) { this.fosfatase_alkali_lic_desc = fosfatase_alkali_lic_desc; }

	public String getFosfatase_alkali_lic_id() { return fosfatase_alkali_lic_id; }
	public void setFosfatase_alkali_lic_id(String fosfatase_alkali_lic_id) { this.fosfatase_alkali_lic_id = fosfatase_alkali_lic_id; }

	public String getGgt() { return ggt;}
	public void setGgt(String ggt) { this.ggt = ggt; }

	public String getGgt_kelainan() { return ggt_kelainan; }
	public void setGgt_kelainan(String ggt_kelainan) { this.ggt_kelainan = ggt_kelainan; }

	public String getGgt_lic_desc() { return ggt_lic_desc; }
	public void setGgt_lic_desc(String ggt_lic_desc) { this.ggt_lic_desc = ggt_lic_desc; }

	public String getGgt_lic_id() { return ggt_lic_id; }
	public void setGgt_lic_id(String ggt_lic_id) { this.ggt_lic_id = ggt_lic_id; }

	public String getGlobulin() { return globulin; }
	public void setGlobulin(String globulin) { this.globulin = globulin; }

	public String getGlobulin_kelainan() { return globulin_kelainan; }
	public void setGlobulin_kelainan(String globulin_kelainan) { this.globulin_kelainan = globulin_kelainan; }

	public String getGlobulin_lic_desc() { return globulin_lic_desc; }
	public void setGlobulin_lic_desc(String globulin_lic_desc) { this.globulin_lic_desc = globulin_lic_desc; }

 	public String getGlobulin_lic_id() { return globulin_lic_id; }
	public void setGlobulin_lic_id(String globulin_lic_id) { this.globulin_lic_id = globulin_lic_id; }

	public String getHbe_ag() { return hbe_ag; }
	public void setHbe_ag(String hbe_ag) { this.hbe_ag = hbe_ag; }

	public String getHbe_ag_kelainan() { return hbe_ag_kelainan; }
	public void setHbe_ag_kelainan(String hbe_ag_kelainan) { this.hbe_ag_kelainan = hbe_ag_kelainan; }

	public String getHbe_ag_lic_desc() { return hbe_ag_lic_desc; }
	public void setHbe_ag_lic_desc(String hbe_ag_lic_desc) { this.hbe_ag_lic_desc = hbe_ag_lic_desc; }

	public String getHbe_ag_lic_id() { return hbe_ag_lic_id; }
	public void setHbe_ag_lic_id(String hbe_ag_lic_id) { this.hbe_ag_lic_id = hbe_ag_lic_id; }

	public String getHbs_ag() { return hbs_ag; }
	public void setHbs_ag(String hbs_ag) { this.hbs_ag = hbs_ag; }

 	public String getHbs_ag_kelainan() { return hbs_ag_kelainan; }
	public void setHbs_ag_kelainan(String hbs_ag_kelainan) { this.hbs_ag_kelainan = hbs_ag_kelainan; }

	public String getHbs_ag_lic_desc() { return hbs_ag_lic_desc; }
	public void setHbs_ag_lic_desc(String hbs_ag_lic_desc) { this.hbs_ag_lic_desc = hbs_ag_lic_desc; }

	public String getHbs_ag_lic_id() { return hbs_ag_lic_id; }
	public void setHbs_ag_lic_id(String hbs_ag_lic_id) { this.hbs_ag_lic_id = hbs_ag_lic_id; }

	public String getSgot() { return sgot; }
	public void setSgot(String sgot) { this.sgot = sgot; }

	public String getSgot_kelainan() { return sgot_kelainan; }
	public void setSgot_kelainan(String sgot_kelainan) { this.sgot_kelainan = sgot_kelainan; }

	public String getSgot_lic_desc() { return sgot_lic_desc; }
	public void setSgot_lic_desc(String sgot_lic_desc) { this.sgot_lic_desc = sgot_lic_desc; }

	public String getSgot_lic_id() { return sgot_lic_id; }
	public void setSgot_lic_id(String sgot_lic_id) { this.sgot_lic_id = sgot_lic_id; }

	public String getSgpt() { return sgpt; }
	public void setSgpt(String sgpt) { this.sgpt = sgpt; }

	public String getSgpt_kelainan() { return sgpt_kelainan; }
	public void setSgpt_kelainan(String sgpt_kelainan) { this.sgpt_kelainan = sgpt_kelainan; }

	public String getSgpt_lic_desc() { return sgpt_lic_desc; }
	public void setSgpt_lic_desc(String sgpt_lic_desc) { this.sgpt_lic_desc = sgpt_lic_desc; }

	public String getSgpt_lic_id() { return sgpt_lic_id; }
	public void setSgpt_lic_id(String sgpt_lic_id) { this.sgpt_lic_id = sgpt_lic_id; }

	public String getTotal_protein() { return total_protein; }
	public void setTotal_protein(String total_protein) { this.total_protein = total_protein; }

 	public String getTotal_protein_kelainan() { return total_protein_kelainan; }
	public void setTotal_protein_kelainan(String total_protein_kelainan) { this.total_protein_kelainan = total_protein_kelainan; }

	public String getTotal_protein_lic_desc() { return total_protein_lic_desc; }
	public void setTotal_protein_lic_desc(String total_protein_lic_desc) { this.total_protein_lic_desc = total_protein_lic_desc; }

	public String getTotal_protein_lic_id() { return total_protein_lic_id; }
	public void setTotal_protein_lic_id(String total_protein_lic_id) { this.total_protein_lic_id = total_protein_lic_id; }

	public String getUreum() { return ureum; }
	public void setUreum(String ureum) { this.ureum = ureum; }

	public String getUreum_kelainan() { return ureum_kelainan; }
	public void setUreum_kelainan(String ureum_kelainan) { this.ureum_kelainan = ureum_kelainan; }

	public String getUreum_lic_desc() { return ureum_lic_desc; }
	public void setUreum_lic_desc(String ureum_lic_desc) { this.ureum_lic_desc = ureum_lic_desc; }

	public String getUreum_lic_id() { return ureum_lic_id; }
	public void setUreum_lic_id(String ureum_lic_id) { this.ureum_lic_id = ureum_lic_id; }

	public Integer getInsured_no() { return insured_no; }
	public void setInsured_no(Integer insured_no) { this.insured_no = insured_no; }

	public String getReg_spaj() { return reg_spaj; }
	public void setReg_spaj(String reg_spaj) { this.reg_spaj = reg_spaj; } 

	
}
       