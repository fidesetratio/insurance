package com.ekalife.elions.model.worksheet;

import java.io.Serializable;

public class UwUrin implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nv_urin_warna;
	private String nv_urin_kejernihan;
	private String nv_urin_berat_jenis;
	private String nv_urin_ph;
	private String nv_urin_protein;
	private String nv_urin_glukosa;
	private String nv_urin_keton;
	private String nv_urin_bilirubin;
	private String nv_urin_urobilinogen;
	private String nv_urin_nitrit;
	private String nv_urin_darah_samar;
	private String nv_urin_lekosit_esterase;
	private String nv_urin_eritrosit;
	private String nv_urin_lekosit;
	private String nv_urin_epitel;
	private String nv_urin_silinder;
	private String nv_urin_kristal;
	private String nv_urin_bakteri;
	
	private String reg_spaj;
	private Integer insured_no;
	private Integer urutanUrin;
	private String tglMcuUrin;
	private String tmpMcuUrin;
	private Integer flagWarna;
	private String warna;
	private String warna_kelainan;
	private String warna_lic_id;
	private String warna_lic_desc; 
	private Integer flagkejernihan;
	private String kejernihan;
	private String kejernihan_kelainan;
	private String kejernihan_lic_id;
	private String kejernihan_lic_desc;
	private Integer flagBj;
	private String bj;
	private String bj_kelainan;
	private String bj_lic_id;
	private String bj_lic_desc;
	private Integer flagPh;
	private String ph;
	private String ph_kelainan;
	private String ph_lic_id;
	private String ph_lic_desc;
	private Integer flagProtein;
	private String protein;
	private String protein_kelainan;
	private String protein_lic_id;
	private String protein_lic_desc;
	private Integer flagGlukosa;
	private String glukosa;
	private String glukosa_kelainan;
	private String glukosa_lic_id;
	private String glukosa_lic_desc;	
	private Integer flagKeton;
	private String keton;
	private String keton_kelainan;
	private String keton_lic_id;
	private String keton_lic_desc;
	private Integer flagBilirubin;
	private String bilirubin;
	private String bilirubin_kelainan;
	private String bilirubin_lic_id;
	private String bilirubin_lic_desc;
	private Integer flagUrobilinogen;
	private String urobilinogen;
	private String urobilinogen_kelainan;
	private String urobilinogen_lic_id;
	private String urobilinogen_lic_desc;
	private Integer flagNitrit;
	private String nitrit;
	private String nitrit_kelainan;
	private String nitrit_lic_id;
	private String nitrit_lic_desc;	
	private Integer flagDarah_samar;
	private String darah_samar;
	private String darah_samar_kelainan;
	private String darah_samar_lic_id;
	private String darah_samar_lic_desc;
	private Integer flagLeukosit_esterase;
	private String lekosit_esterase;
	private String lekosit_esterase_kelainan;
	private String lekosit_esterase_lic_id;
	private String lekosit_esterase_lic_desc;	
	private Integer flagSedimen_eritrosit;
	private String sedimen_eritrosit;
	private String sedimen_eritrosit_kelainan;
	private String sedimen_eritrosit_lic_id;
	private String sedimen_eritrosit_lic_desc;	
	private Integer flagSedimen_leukosit;
	private String sedimen_leukosit;
	private String sedimen_leukosit_kelainan;
	private String sedimen_leukosit_lic_id;
	private String sedimen_leukosit_lic_desc;	
	private Integer flagSedimen_epitel;
	private String sedimen_epitel;
	private String sedimen_epitel_kelainan;
	private String sedimen_epitel_lic_id;
	private String sedimen_epitel_lic_desc;	
	private Integer flagSedimen_silinder;
	private String sedimen_silinder;
	private String sedimen_silinder_kelainan;
	private String sedimen_silinder_lic_id;
	private String sedimen_silinder_lic_desc;
	private Integer flagSedimen_kristal;
	private String sedimen_kristal;
	private String sedimen_kristal_kelainan;
	private String sedimen_kristal_lic_id;
	private String sedimen_kristal_lic_desc;
	private Integer flagBakteri;
	private String bakteri;
	private String bakteri_kelainan;
	private String bakteri_lic_id;
	private String bakteri_lic_desc;
	
	public UwUrin() {
		setNv_urin_warna("kuning");
		setNv_urin_kejernihan("jernih");
		setNv_urin_berat_jenis("1,010 - 1,030"); 
		setNv_urin_ph("5,0 - 8,0");
		setNv_urin_protein("negatif (<=15 mg/dl)");
		setNv_urin_glukosa("negatif (< 50 mg/dl)");
		setNv_urin_keton("negatif (<= 15 mg/dl )");
		setNv_urin_bilirubin("negatif ( < 0.5 mg/dl)");
		setNv_urin_urobilinogen("0,1 - 1,0");
		setNv_urin_nitrit("negatif");
		setNv_urin_darah_samar("negatif");
		setNv_urin_lekosit_esterase("negatif");
		setNv_urin_eritrosit("0 - 1 /LPB");
		setNv_urin_lekosit("0 - 5 /LPB");
		setNv_urin_epitel("< 10 /LPB");
		setNv_urin_silinder("negatif");
		setNv_urin_kristal("negatif");
		setNv_urin_bakteri("negatif");		
		
		setFlagWarna(0);
		setFlagkejernihan(0);
		setFlagBj(0);
		setFlagPh(0);
		setFlagProtein(0);
		setFlagGlukosa(0);
		setFlagKeton(0);
		setFlagBilirubin(0);
		setFlagUrobilinogen(0);
		setFlagNitrit(0);
		setFlagDarah_samar(0);
		setFlagLeukosit_esterase(0);
		setFlagSedimen_eritrosit(0);
		setFlagSedimen_leukosit(0);
		setFlagSedimen_epitel(0);
		setFlagSedimen_silinder(0);
		setFlagSedimen_kristal(0);
		setFlagBakteri(0);
	}
	
	public String getNv_urin_bakteri() { return nv_urin_bakteri; }
	public void setNv_urin_bakteri(String nv_urin_bakteri) { this.nv_urin_bakteri = nv_urin_bakteri; }
	
	public String getNv_urin_berat_jenis() { return nv_urin_berat_jenis; }
	public void setNv_urin_berat_jenis(String nv_urin_berat_jenis) { this.nv_urin_berat_jenis = nv_urin_berat_jenis; }
	 
	public String getNv_urin_bilirubin() { return nv_urin_bilirubin; }
	public void setNv_urin_bilirubin(String nv_urin_bilirubin) { this.nv_urin_bilirubin = nv_urin_bilirubin; }
	
	public String getNv_urin_darah_samar() { return nv_urin_darah_samar; }
	public void setNv_urin_darah_samar(String nv_urin_darah_samar) { this.nv_urin_darah_samar = nv_urin_darah_samar; }
	
	public String getNv_urin_epitel() { return nv_urin_epitel; }
	public void setNv_urin_epitel(String nv_urin_epitel) { this.nv_urin_epitel = nv_urin_epitel; }
	
	public String getNv_urin_eritrosit() { return nv_urin_eritrosit; }
	public void setNv_urin_eritrosit(String nv_urin_eritrosit) { this.nv_urin_eritrosit = nv_urin_eritrosit; }
	
	public String getNv_urin_glukosa() { return nv_urin_glukosa; }
	public void setNv_urin_glukosa(String nv_urin_glukosa) { this.nv_urin_glukosa = nv_urin_glukosa; }
	
	public String getNv_urin_kejernihan() { return nv_urin_kejernihan; }
	public void setNv_urin_kejernihan(String nv_urin_kejernihan) { this.nv_urin_kejernihan = nv_urin_kejernihan; }
	
	public String getNv_urin_keton() { return nv_urin_keton; }
	public void setNv_urin_keton(String nv_urin_keton) { this.nv_urin_keton = nv_urin_keton; }
	
	public String getNv_urin_kristal() { return nv_urin_kristal; }
	public void setNv_urin_kristal(String nv_urin_kristal) { this.nv_urin_kristal = nv_urin_kristal; }
	
	public String getNv_urin_lekosit() { return nv_urin_lekosit; }
	public void setNv_urin_lekosit(String nv_urin_lekosit) { this.nv_urin_lekosit = nv_urin_lekosit; }
	
	public String getNv_urin_lekosit_esterase() { return nv_urin_lekosit_esterase; } 
	public void setNv_urin_lekosit_esterase(String nv_urin_lekosit_esterase) { this.nv_urin_lekosit_esterase = nv_urin_lekosit_esterase; }
	
	public String getNv_urin_nitrit() { return nv_urin_nitrit; }
	public void setNv_urin_nitrit(String nv_urin_nitrit) { this.nv_urin_nitrit = nv_urin_nitrit; }
	
	public String getNv_urin_ph() { return nv_urin_ph; }
	public void setNv_urin_ph(String nv_urin_ph) { this.nv_urin_ph = nv_urin_ph; }
	
	public String getNv_urin_protein() { return nv_urin_protein; }
	public void setNv_urin_protein(String nv_urin_protein) { this.nv_urin_protein = nv_urin_protein; }
	
	public String getNv_urin_silinder() { return nv_urin_silinder; }
	public void setNv_urin_silinder(String nv_urin_silinder) { this.nv_urin_silinder = nv_urin_silinder; }
	
	public String getNv_urin_urobilinogen() { return nv_urin_urobilinogen; }
	public void setNv_urin_urobilinogen(String nv_urin_urobilinogen) { this.nv_urin_urobilinogen = nv_urin_urobilinogen; }
	
	public String getNv_urin_warna() { return nv_urin_warna; }
	public void setNv_urin_warna(String nv_urin_warna) { this.nv_urin_warna = nv_urin_warna; }	
	
	
	public Integer getUrutanUrin() { return urutanUrin; }
	public void setUrutanUrin(Integer urutanUrin) { this.urutanUrin = urutanUrin; }

	public String getBilirubin() { return bilirubin; }
	public void setBilirubin(String bilirubin) { this.bilirubin = bilirubin; }
	
	public Integer getFlagBj() { return flagBj; }
	public void setFlagBj(Integer flagBj) { this.flagBj = flagBj; }

	public String getBj() { return bj; }
	public void setBj(String bj) { this.bj = bj; }
	
	public String getBj_kelainan() { return bj_kelainan; }
	public void setBj_kelainan(String bj_kelainan) { this.bj_kelainan = bj_kelainan; }

	public String getBj_lic_desc() { return bj_lic_desc; }
	public void setBj_lic_desc(String bj_lic_desc) { this.bj_lic_desc = bj_lic_desc; } 
 
	public String getBj_lic_id() { return bj_lic_id; }
	public void setBj_lic_id(String bj_lic_id) { this.bj_lic_id = bj_lic_id; }

	public String getDarah_samar() { return darah_samar; }
	public void setDarah_samar(String darah_samar) { this.darah_samar = darah_samar; }
	
	public String getGlukosa() { return glukosa; }
	public void setGlukosa(String glukosa) { this.glukosa = glukosa; }

	public Integer getFlagkejernihan() { return flagkejernihan; }
	public void setFlagkejernihan(Integer flagkejernihan) { this.flagkejernihan = flagkejernihan; }

	public String getKejernihan() { return kejernihan; }
	public void setKejernihan(String kejernihan) { this.kejernihan = kejernihan; }
	
	public String getKejernihan_kelainan() { return kejernihan_kelainan; }
	public void setKejernihan_kelainan(String kejernihan_kelainan) { this.kejernihan_kelainan = kejernihan_kelainan; } 

	public String getKejernihan_lic_desc() { return kejernihan_lic_desc; }
	public void setKejernihan_lic_desc(String kejernihan_lic_desc) { this.kejernihan_lic_desc = kejernihan_lic_desc; }

	public String getKejernihan_lic_id() { return kejernihan_lic_id; }
	public void setKejernihan_lic_id(String kejernihan_lic_id) { this.kejernihan_lic_id = kejernihan_lic_id; }
	
	public String getKeton() { return keton; }
	public void setKeton(String keton) { this.keton = keton; } 
	
	public String getLekosit_esterase() { return lekosit_esterase; }
	public void setLekosit_esterase(String lekosit_esterase) { this.lekosit_esterase = lekosit_esterase; }
	 
	public String getNitrit() { return nitrit; }
	public void setNitrit(String nitrit) { this.nitrit = nitrit; }
	
	public String getPh() { return ph; }
	public void setPh(String ph) { this.ph = ph; }
	
	public String getProtein() { return protein; }
	public void setProtein(String protein) { this.protein = protein; }
	
	public String getSedimen_epitel() { return sedimen_epitel; }
	public void setSedimen_epitel(String sedimen_epitel) { this.sedimen_epitel = sedimen_epitel; }
	
	public String getSedimen_eritrosit() { return sedimen_eritrosit; }
	public void setSedimen_eritrosit(String sedimen_eritrosit) { this.sedimen_eritrosit = sedimen_eritrosit; }
	
	public String getSedimen_kristal() { return sedimen_kristal; }
	public void setSedimen_kristal(String sedimen_kristal) { this.sedimen_kristal = sedimen_kristal; }
	
	public String getSedimen_leukosit() { return sedimen_leukosit; }
	public void setSedimen_leukosit(String sedimen_leukosit) { this.sedimen_leukosit = sedimen_leukosit; }
	
	public String getSedimen_silinder() { return sedimen_silinder; }
	public void setSedimen_silinder(String sedimen_silinder) { this.sedimen_silinder = sedimen_silinder; }
	
	public String getTglMcuUrin() { return tglMcuUrin; }
	public void setTglMcuUrin(String tglMcuUrin) { this.tglMcuUrin = tglMcuUrin; }
	
	public String getTmpMcuUrin() { return tmpMcuUrin; }
	public void setTmpMcuUrin(String tmpMcuUrin) { this.tmpMcuUrin = tmpMcuUrin; }
	
	public String getUrobilinogen() { return urobilinogen; }
	public void setUrobilinogen(String urobilinogen) { this.urobilinogen = urobilinogen; }
	
	public Integer getFlagWarna() { return flagWarna; }
	public void setFlagWarna(Integer flagWarna) { this.flagWarna = flagWarna; }

	public String getWarna() { return warna; }
	public void setWarna(String warna) { this.warna = warna; }

	public String getWarna_kelainan() { return warna_kelainan; }
	public void setWarna_kelainan(String warna_kelainan) { this.warna_kelainan = warna_kelainan; }

	public String getWarna_lic_desc() { return warna_lic_desc; }
	public void setWarna_lic_desc(String warna_lic_desc) { this.warna_lic_desc = warna_lic_desc; }
 
	public String getWarna_lic_id() { return warna_lic_id; }
	public void setWarna_lic_id(String warna_lic_id) { this.warna_lic_id = warna_lic_id; }

	public String getBilirubin_kelainan() { return bilirubin_kelainan; }
	public void setBilirubin_kelainan(String bilirubin_kelainan) { this.bilirubin_kelainan = bilirubin_kelainan; }

	public String getBilirubin_lic_desc() { return bilirubin_lic_desc; }
	public void setBilirubin_lic_desc(String bilirubin_lic_desc) { this.bilirubin_lic_desc = bilirubin_lic_desc; }

	public String getBilirubin_lic_id() { return bilirubin_lic_id; }
	public void setBilirubin_lic_id(String bilirubin_lic_id) { this.bilirubin_lic_id = bilirubin_lic_id; }

	public String getDarah_samar_kelainan() { return darah_samar_kelainan; }
 	public void setDarah_samar_kelainan(String darah_samar_kelainan) { this.darah_samar_kelainan = darah_samar_kelainan; }

	public String getDarah_samar_lic_desc() { return darah_samar_lic_desc; }
	public void setDarah_samar_lic_desc(String darah_samar_lic_desc) { this.darah_samar_lic_desc = darah_samar_lic_desc; }

	public String getDarah_samar_lic_id() { return darah_samar_lic_id; }
	public void setDarah_samar_lic_id(String darah_samar_lic_id) { this.darah_samar_lic_id = darah_samar_lic_id; }

	public Integer getFlagBilirubin() { return flagBilirubin; }
	public void setFlagBilirubin(Integer flagBilirubin) { this.flagBilirubin = flagBilirubin; }
 
 	public Integer getFlagDarah_samar() { return flagDarah_samar; }
	public void setFlagDarah_samar(Integer flagDarah_samar) { this.flagDarah_samar = flagDarah_samar; }

	public Integer getFlagGlukosa() { return flagGlukosa; }
	public void setFlagGlukosa(Integer flagGlukosa) { this.flagGlukosa = flagGlukosa; }

	public Integer getFlagKeton() { return flagKeton; }
	public void setFlagKeton(Integer flagKeton) { this.flagKeton = flagKeton; }

	public Integer getFlagLeukosit_esterase() { return flagLeukosit_esterase; }
	public void setFlagLeukosit_esterase(Integer flagLeukosit_esterase) { this.flagLeukosit_esterase = flagLeukosit_esterase; }

	public Integer getFlagNitrit() { return flagNitrit; }
	public void setFlagNitrit(Integer flagNitrit) { this.flagNitrit = flagNitrit; }

	public Integer getFlagPh() { return flagPh; }
	public void setFlagPh(Integer flagPh) { this.flagPh = flagPh; }

	public Integer getFlagProtein() { return flagProtein; }
	public void setFlagProtein(Integer flagProtein) { this.flagProtein = flagProtein; }

	public Integer getFlagSedimen_epitel() { return flagSedimen_epitel; }
	public void setFlagSedimen_epitel(Integer flagSedimen_epitel) { this.flagSedimen_epitel = flagSedimen_epitel; }

 	public Integer getFlagSedimen_eritrosit() { return flagSedimen_eritrosit; }
	public void setFlagSedimen_eritrosit(Integer flagSedimen_eritrosit) { this.flagSedimen_eritrosit = flagSedimen_eritrosit; }

 	public Integer getFlagSedimen_kristal() { return flagSedimen_kristal; }
	public void setFlagSedimen_kristal(Integer flagSedimen_kristal) { this.flagSedimen_kristal = flagSedimen_kristal; }

	public Integer getFlagSedimen_leukosit() { return flagSedimen_leukosit; }
	public void setFlagSedimen_leukosit(Integer flagSedimen_leukosit) { this.flagSedimen_leukosit = flagSedimen_leukosit; }

	public Integer getFlagSedimen_silinder() { return flagSedimen_silinder; }
	public void setFlagSedimen_silinder(Integer flagSedimen_silinder) { this.flagSedimen_silinder = flagSedimen_silinder; }

	public Integer getFlagUrobilinogen() { return flagUrobilinogen; }
	public void setFlagUrobilinogen(Integer flagUrobilinogen) { this.flagUrobilinogen = flagUrobilinogen; }

	public String getGlukosa_kelainan() { return glukosa_kelainan; }
	public void setGlukosa_kelainan(String glukosa_kelainan) { this.glukosa_kelainan = glukosa_kelainan; }

	public String getGlukosa_lic_desc() { return glukosa_lic_desc; }
	public void setGlukosa_lic_desc(String glukosa_lic_desc) { this.glukosa_lic_desc = glukosa_lic_desc; }

	public String getGlukosa_lic_id() { return glukosa_lic_id; }
	public void setGlukosa_lic_id(String glukosa_lic_id) { this.glukosa_lic_id = glukosa_lic_id; }

	public String getKeton_kelainan() { return keton_kelainan; }
	public void setKeton_kelainan(String keton_kelainan) { this.keton_kelainan = keton_kelainan; }

	public String getKeton_lic_desc() { return keton_lic_desc; }
	public void setKeton_lic_desc(String keton_lic_desc) { this.keton_lic_desc = keton_lic_desc; }

	public String getKeton_lic_id() { return keton_lic_id; }
	public void setKeton_lic_id(String keton_lic_id) { this.keton_lic_id = keton_lic_id; }

	public String getLekosit_esterase_kelainan() { return lekosit_esterase_kelainan; }
	public void setLekosit_esterase_kelainan(String lekosit_esterase_kelainan) { this.lekosit_esterase_kelainan = lekosit_esterase_kelainan; }

	public String getLekosit_esterase_lic_desc() { return lekosit_esterase_lic_desc; }
	public void setLekosit_esterase_lic_desc(String lekosit_esterase_lic_desc) { this.lekosit_esterase_lic_desc = lekosit_esterase_lic_desc; }

	public String getLekosit_esterase_lic_id() { return lekosit_esterase_lic_id; }
	public void setLekosit_esterase_lic_id(String lekosit_esterase_lic_id) { this.lekosit_esterase_lic_id = lekosit_esterase_lic_id; }

	public String getNitrit_kelainan() { return nitrit_kelainan; }
	public void setNitrit_kelainan(String nitrit_kelainan) { this.nitrit_kelainan = nitrit_kelainan; }

	public String getNitrit_lic_desc() { return nitrit_lic_desc; }
	public void setNitrit_lic_desc(String nitrit_lic_desc) { this.nitrit_lic_desc = nitrit_lic_desc; }

	public String getNitrit_lic_id() { return nitrit_lic_id; }
	public void setNitrit_lic_id(String nitrit_lic_id) { this.nitrit_lic_id = nitrit_lic_id; }

	public String getPh_kelainan() { return ph_kelainan; }
	public void setPh_kelainan(String ph_kelainan) { this.ph_kelainan = ph_kelainan; }

	public String getPh_lic_desc() { return ph_lic_desc; }
	public void setPh_lic_desc(String ph_lic_desc) { this.ph_lic_desc = ph_lic_desc; }

	public String getPh_lic_id() { return ph_lic_id; }
	public void setPh_lic_id(String ph_lic_id) { this.ph_lic_id = ph_lic_id; }

 	public String getProtein_kelainan() { return protein_kelainan; }
	public void setProtein_kelainan(String protein_kelainan) { this.protein_kelainan = protein_kelainan; }

 	public String getProtein_lic_desc() { return protein_lic_desc; }
	public void setProtein_lic_desc(String protein_lic_desc) { this.protein_lic_desc = protein_lic_desc; }

	public String getProtein_lic_id() { return protein_lic_id; }
	public void setProtein_lic_id(String protein_lic_id) { this.protein_lic_id = protein_lic_id; }

	public String getSedimen_epitel_kelainan() { return sedimen_epitel_kelainan; }
	public void setSedimen_epitel_kelainan(String sedimen_epitel_kelainan) { this.sedimen_epitel_kelainan = sedimen_epitel_kelainan; }

 	public String getSedimen_epitel_lic_desc() { return sedimen_epitel_lic_desc; }
	public void setSedimen_epitel_lic_desc(String sedimen_epitel_lic_desc) { this.sedimen_epitel_lic_desc = sedimen_epitel_lic_desc; }

	public String getSedimen_epitel_lic_id() { return sedimen_epitel_lic_id; }
	public void setSedimen_epitel_lic_id(String sedimen_epitel_lic_id) { this.sedimen_epitel_lic_id = sedimen_epitel_lic_id; }

	public String getSedimen_eritrosit_kelainan() { return sedimen_eritrosit_kelainan; }
	public void setSedimen_eritrosit_kelainan(String sedimen_eritrosit_kelainan) { this.sedimen_eritrosit_kelainan = sedimen_eritrosit_kelainan; }

	public String getSedimen_eritrosit_lic_desc() { return sedimen_eritrosit_lic_desc; }
	public void setSedimen_eritrosit_lic_desc(String sedimen_eritrosit_lic_desc) { this.sedimen_eritrosit_lic_desc = sedimen_eritrosit_lic_desc; }

	public String getSedimen_eritrosit_lic_id() { return sedimen_eritrosit_lic_id; }
	public void setSedimen_eritrosit_lic_id(String sedimen_eritrosit_lic_id) { this.sedimen_eritrosit_lic_id = sedimen_eritrosit_lic_id; }

	public String getSedimen_kristal_kelainan() { return sedimen_kristal_kelainan; }
	public void setSedimen_kristal_kelainan(String sedimen_kristal_kelainan) { this.sedimen_kristal_kelainan = sedimen_kristal_kelainan; }

	public String getSedimen_kristal_lic_desc() { return sedimen_kristal_lic_desc; }
	public void setSedimen_kristal_lic_desc(String sedimen_kristal_lic_desc) { this.sedimen_kristal_lic_desc = sedimen_kristal_lic_desc; }

	public String getSedimen_kristal_lic_id() { return sedimen_kristal_lic_id; }
	public void setSedimen_kristal_lic_id(String sedimen_kristal_lic_id) { this.sedimen_kristal_lic_id = sedimen_kristal_lic_id; }

	public String getSedimen_leukosit_kelainan() { return sedimen_leukosit_kelainan; }
	public void setSedimen_leukosit_kelainan(String sedimen_leukosit_kelainan) { this.sedimen_leukosit_kelainan = sedimen_leukosit_kelainan; }

	public String getSedimen_leukosit_lic_desc() { return sedimen_leukosit_lic_desc; }
	public void setSedimen_leukosit_lic_desc(String sedimen_leukosit_lic_desc) { this.sedimen_leukosit_lic_desc = sedimen_leukosit_lic_desc; }

	public String getSedimen_leukosit_lic_id() { return sedimen_leukosit_lic_id; }
	public void setSedimen_leukosit_lic_id(String sedimen_leukosit_lic_id) { this.sedimen_leukosit_lic_id = sedimen_leukosit_lic_id; }

	public String getSedimen_silinder_kelainan() { return sedimen_silinder_kelainan; }
	public void setSedimen_silinder_kelainan(String sedimen_silinder_kelainan) { this.sedimen_silinder_kelainan = sedimen_silinder_kelainan; }

	public String getSedimen_silinder_lic_desc() { return sedimen_silinder_lic_desc; }
	public void setSedimen_silinder_lic_desc(String sedimen_silinder_lic_desc) { this.sedimen_silinder_lic_desc = sedimen_silinder_lic_desc; }

	public String getSedimen_silinder_lic_id() { return sedimen_silinder_lic_id; }
	public void setSedimen_silinder_lic_id(String sedimen_silinder_lic_id) { this.sedimen_silinder_lic_id = sedimen_silinder_lic_id; }

	public String getUrobilinogen_kelainan() { return urobilinogen_kelainan; }
	public void setUrobilinogen_kelainan(String urobilinogen_kelainan) { this.urobilinogen_kelainan = urobilinogen_kelainan; }

	public String getUrobilinogen_lic_desc() { return urobilinogen_lic_desc; }
	public void setUrobilinogen_lic_desc(String urobilinogen_lic_desc) { this.urobilinogen_lic_desc = urobilinogen_lic_desc; }

	public String getUrobilinogen_lic_id() { return urobilinogen_lic_id; }
	public void setUrobilinogen_lic_id(String urobilinogen_lic_id) { this.urobilinogen_lic_id = urobilinogen_lic_id; }

	public Integer getInsured_no() { return insured_no; }
	public void setInsured_no(Integer insured_no) { this.insured_no = insured_no; }

	public String getReg_spaj() { return reg_spaj; }
	public void setReg_spaj(String reg_spaj) { this.reg_spaj = reg_spaj; }

	public Integer getFlagBakteri() { return flagBakteri; }
	public void setFlagBakteri(Integer flagBakteri) { this.flagBakteri = flagBakteri; }

	public String getBakteri() { return bakteri; }
	public void setBakteri(String bakteri) { this.bakteri = bakteri; }

	public String getBakteri_kelainan() { return bakteri_kelainan; }
	public void setBakteri_kelainan(String bakteriKelainan) { bakteri_kelainan = bakteriKelainan; }

	public String getBakteri_lic_id() { return bakteri_lic_id; }
	public void setBakteri_lic_id(String bakteriLicId) { bakteri_lic_id = bakteriLicId; }

	public String getBakteri_lic_desc() { return bakteri_lic_desc; }
	public void setBakteri_lic_desc(String bakteriLicDesc) { bakteri_lic_desc = bakteriLicDesc; }
	
}
 