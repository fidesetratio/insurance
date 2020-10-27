package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ekalife.utils.Common;

public class Datausulan implements Serializable {


	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;
	private String no_polis;
	private Integer mste_insured_no;               
	private Integer mspr_unit;                     
	private Integer mspr_class;                    
	private Double mspr_tsi;                       
	private Double mspr_premium;                   
	private Integer mspr_ins_period;               
	private Date mspr_end_pay;                     
	private Integer mspr_tt;                       
	private Integer lsbs_id;                       
	private String lsdbs_name;
	private String lsdbs_depkeu;
	private Integer mspo_pay_period;               
	private Integer mste_medical;                  
	private String medis;                          
	private Date mste_end_date;                    
	private Date mste_beg_date;                    
	private Integer mste_flag_cc;                  
	private Integer lscb_id;                       
	private String lscb_pay_mode;                  
	private String lku_id;   
	private String kurs_p;
	private String lku_symbol;                     
	private String plan;  
	private String kurs_premi;   
	private Integer lsdbs_number;
	private Integer jmlrider;
	private Integer jumlah_seluruh_rider;
	private Integer jmlrider_include;
	private Integer li_umur_ttg;
	private Integer li_umur_pp;
	private Integer kode_flag;
	private Integer flag_as;
	private Integer flag_bao;
	private Double rate_plan;
	private Integer mn;
	private Double total_premi_rider;
	private Double total_premi_sementara;
	private Integer flag_account;
	private ArrayList daftaRider;
	private ArrayList daftabenef;
	private ArrayList daftaExtra;
	private Integer jml_benef;
	private Integer isProductBancass;
	private Integer isInvestasi;
	private Integer isBungaSimponi;
	private Integer isBonusTahapan;
	private Integer flag_rider;
	public Integer flag_jenis_plan;
	public Integer flag_platinumlink;
	public Integer indeks_validasi;
	public Integer tipeproduk;
	private Integer li_trans_tunggal;
	private Integer li_trans_berkala;
	private Double jmlh_top;
	private Integer flag_worksite;
	String kodeproduk;
	private Integer mspo_installment;
	private Integer flag_endowment;
	private Integer flag_bulanan;
	private Double mspr_discount;
	
	private Integer lstb_id;                       
	private String lca_id;                         
	private String lwk_id;                         
	private String lsrg_id;                        
	private String mpi_mou;                        
	private Integer lssp_id;                       
	private Integer lspd_id;                       
	private String mspo_policy_holder;             
	private Integer lsre_id;                       
	private Integer mspo_age;                                            
	private Integer lus_id;                        
	private String mspo_policy_no;                 
	private Date mspo_spaj_date;                   
	private Integer mspo_ins_period;                             
	private Integer mspo_proses_bill;              
	private Date mspo_next_bill;                                
	private Integer mspo_flat;                     
	private Integer mspo_ref_bii;                  
	private Integer mspo_jn_coas;                  
	private Date mspo_date_print;                  
	private Date mspo_date_spec;                   
	private Date mspo_date_ttp;                    
	private Integer mspo_pribadi;                  
	private String mspo_ao;                        
	private String mspo_customer;                  
	private String mspo_no_blanko;                 
	private String mspo_no_pol_asm;                
	private Double mspo_under_table;               
	private Integer mspo_ins_bulan;                                     
	private String mspo_beasiswa;                  
	private Integer mspo_lama_beasiswa;            
	private Integer mspo_cara_beasiswa;            
	private Integer mspo_umur_beasiswa;            
	private String mspo_oldpol_eb;                 
	private Integer mspo_follow_up;                
	private Integer mspo_komisi_bii;               
	private Date mspo_input_date;                  
	private Date mspo_beg_date;                    
	private Date mspo_end_date;                    
	private Integer mspo_provider;                 
	private Integer mspo_preexisting;              
	private Double mspo_extend;                    
	private String mspo_pass_eb;                   
	private Double mspo_biaya_transfer;            
	private Integer mspo_as_charges;               
	private String mspo_plan_provider;   
	private Integer mspo_self_ins;
	private Integer mspo_aso;                      
	private Integer lstp_id;                       
	private String mspo_note;                      
	private String mspo_no_kerjasama;              
	private String mspo_nasabah_acc;               
	private String mspo_nasabah_dcif;              
	private String mspo_policy_no_format;          
	private Integer mspo_syahriah;                 
	private Integer mspo_cash_back;                
	private String mspo_cash_mkt;                  
	private String mspo_cash_entry;                
	private String mspo_cash_verifikasi;           
	private String mspo_cash_dpp;                  
	private Integer mspo_cash_wil;                 
	private Double mspo_cash_rate_1;               
	private Double mspo_cash_rate_2;
	private Integer lssa_id;
	private Integer flag_ekalink;
	private Integer flag_worksite_ekalife ;
	private Integer flag_horison;
	
	private String keterangan_fund;
	private Integer mste_flag_el;
	private Integer mste_flag_investasi;
	private Double total_premi_kombinasi;
	private Integer cara_premi;
	private Integer flag_include;
	private String plan_rider;
	private Date mspr_beg_date;
	private Date mspr_end_date;
	private Integer mspr_rate;

	private String kombinasi;
	private HashMap worksite;
	private HashMap psn;
	private String status_submit;
	private String lde_id;
	private Integer flag_gutri;
	private Integer mste_flag_guthrie;
	private Integer flag_simas;
	private Integer jml_peserta;
	private ArrayList daftapeserta;	
	private ArrayList daftariderhcp;
	private ArrayList daftariderall;
	private ArrayList daftahcp;
	private Integer specta_save;
	private Integer jenis_pemegang_polis;
	private Integer flag_hcp;
	private Integer flag_rider_hcp;
	private Integer cek_flag_hcp;
	private Integer flag_ktp;
	private String spaj_ktp;
	private String keterangan;
	private Integer polis_dobel;
	private Integer flag_uppremi;
	private Double mspr_extra;
	private String kopiSPAJ;
	private Integer flag_paymode_rider;
	private Integer lscb_id_rider;
	private Integer mssur_se;
	private Integer alertEkaSehat;
	private Integer mspo_flag_sph;
	private Integer mspo_flag_spaj;
	private Integer persenUp;
	private Integer jmlDaftarPesertax;
	private Integer pesertax;
	private ArrayList daftarplus;
	private ArrayList daftarplus_mix;
	private ArrayList daftarplus_mix_new;
	private ArrayList daftarplusplus;
	private ArrayList daftarPesertaNew;
	private Integer flag_compare;
	private Integer flag_compare_baru;
	private Integer jmlriderplus;
	private Integer flag_compare2;
	private Integer jmlJenisRider;
	private Integer jRSpouse;
	private Integer jRChild1;
	private Integer jRChild2;
	private Integer jRChild3;
	private Integer jRChild4;
	private Integer jRChild5;
	private Integer cekBasic;
	private Integer cekCompare;
	private Integer flag_jenis_peserta;
	private Integer mp_main;
	private String akhir_polis1;
	private String akhir_bayar1;
	private Integer tahun_polis;
	private Integer tahun_bayar;
	private Integer flag_simpol;
	private Double premi_additional_unit;
	private Integer lsbs_id_plus;
	private Integer tgl_tertanggung_I;
	private Integer masa_tanggung_I;
	private Integer mspo_flag_new;
	private Integer flag_paket;
	private String spaj_paket;
	private Integer msen_aksep_uw;
	private Integer flagspecial;
	private Boolean flagIssue;
	private Integer mste_gmit;
	private String nama_paket;	
	private ArrayList daftaRiderAddOnTtg;
	private ArrayList daftaRiderAddOnTtg1;
	private ArrayList daftaRiderMedicalPlusTtg1;
	private ArrayList daftaRiderAddOnTtg2;
	private ArrayList daftaRiderMedicalPlusTtg2;
	private ArrayList daftaRiderAddOnTtg3;
	private ArrayList daftaRiderMedicalPlusTtg3;
	private ArrayList daftaRiderAddOnTtg4;
	private ArrayList daftaRiderMedicalPlusTtg4;
	private ArrayList daftaRiderAddOnTtg5;
	private ArrayList daftaRiderMedicalPlusTtg5;
	private ArrayList daftaRiderAddOnTtg6;
	private ArrayList daftaRiderMedicalPlusTtg6;
	private Integer daftaRiderMedicalPlus;
	private Integer daftaRiderAddOn;
	private Integer flag_riderAddon;
	private Integer tgl_tertanggung_I_RI;
	private Integer edit_dataUsulan;
	private String kriteria_kesalahan;
	private String pilih_batch; // tambah pilihan nonbatch untuk simas primelink (rider save). helpdesk [128634/128295] //Chandra
	private String campaign_id = "0~X0"; // helpdesk [137730] tambahin pilihan campaign
	private String campaign_name; // helpdesk [137730] tambahin pilihan campaign
	private String campradio = "0"; // helpdesk [137730] tambahin pilihan campaign
	private String covid_flag = "0"; // flag covid for urgent measurement patar timotius
	private String flag_vip = "0"; // flag vip nana
	private Double premiTunggal; // untuk simpol isi premitunggal
	private String full_autosales = "1"; // project full autosales
	private String enable_full_autosales = "1"; // project full autosales
	
	private String covid_name;
	public String getCovid_name() {
		return covid_name;
	}

	public void setCovid_name(String covid_name) {
		this.covid_name = covid_name;
	}


	public String getCovid_flag() {
		return covid_flag;
	}

	public void setCovid_flag(String covid_flag) {
		this.covid_flag = covid_flag;
	}

	public Boolean getFlagIssue() {
		return flagIssue;
	}

	public void setFlagIssue(Boolean flagIssue) {
		this.flagIssue = flagIssue;
	}

	public String getSpaj_paket() {
		return spaj_paket;
	}

	public void setSpaj_paket(String spaj_paket) {
		this.spaj_paket = spaj_paket;
	}

	public Integer getFlag_paket() {
		return flag_paket;
	}

	public void setFlag_paket(Integer flag_paket) {
		this.flag_paket = flag_paket;
	}

	public Integer getMspo_flag_new() {
		return mspo_flag_new;
	}

	public void setMspo_flag_new(Integer mspo_flag_new) {
		this.mspo_flag_new = mspo_flag_new;
	}

	public Double getPremi_additional_unit() {
		return premi_additional_unit;
	}

	public void setPremi_additional_unit(Double premi_additional_unit) {
		this.premi_additional_unit = premi_additional_unit;
	}
	
	public Integer getFlag_simpol() {
		return flag_simpol;
	}

	public void setFlag_simpol(Integer flag_simpol) {
		this.flag_simpol = flag_simpol;
	}

	public Integer getMspo_flag_sph() {
		return mspo_flag_sph;
	}

	public void setMspo_flag_sph(Integer mspoFlagSph) {
		mspo_flag_sph = mspoFlagSph;
	}

	public boolean isPsave;
	
	public boolean isPlatinumSave;
	
	public boolean isSlink;
	
	public boolean isSlink() {
		return isSlink;
	}

	public void setSlink(boolean isSlink) {
		this.isSlink = isSlink;
	}

	private Integer convert;


	public Integer getMspr_rate() {
		return mspr_rate;
	}

	public void setMspr_rate(Integer msprRate) {
		mspr_rate = msprRate;
	}	
	
	public Date getMspr_beg_date() {
		return mspr_beg_date;
	}

	public void setMspr_beg_date(Date msprBegDate) {
		mspr_beg_date = msprBegDate;
	}

	public Date getMspr_end_date() {
		return mspr_end_date;
	}

	public void setMspr_end_date(Date msprEndDate) {
		mspr_end_date = msprEndDate;
	}
	
	public String getPlan_rider() {
		return plan_rider;
	}

	public void setPlan_rider(String planRider) {
		plan_rider = planRider;
	}

	public Integer getFlag_include() {
		return flag_include;
	}

	public void setFlag_include(Integer flagInclude) {
		flag_include = flagInclude;
	}
	
	public boolean isPsave() {
		return isPsave;
	}

	public void setPsave(boolean isPsave) {
		this.isPsave = isPsave;
	}

	public Double getMspr_extra() {
		return mspr_extra;
	}

	public void setMspr_extra(Double mspr_extra) {
		this.mspr_extra = mspr_extra;
	}

	public Integer getPolis_dobel() {
		return polis_dobel;
	}

	public void setPolis_dobel(Integer polis_dobel) {
		this.polis_dobel = polis_dobel;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public String getSpaj_ktp() {
		return spaj_ktp;
	}

	public void setSpaj_ktp(String spaj_ktp) {
		this.spaj_ktp = spaj_ktp;
	}

	public Integer getFlag_ktp() {
		return flag_ktp;
	}

	public void setFlag_ktp(Integer flag_ktp) {
		this.flag_ktp = flag_ktp;
	}

	public Integer getCek_flag_hcp() {
		return cek_flag_hcp;
	}

	public void setCek_flag_hcp(Integer cek_flag_hcp) {
		this.cek_flag_hcp = cek_flag_hcp;
	}

	public Integer getFlag_hcp() {
		return flag_hcp;
	}

	public void setFlag_hcp(Integer flag_hcp) {
		this.flag_hcp = flag_hcp;
	}

	public Integer getSpecta_save() {
		return specta_save;
	}

	public void setSpecta_save(Integer specta_save) {
		this.specta_save = specta_save;
	}

	public Integer getFlag_simas() {
		return flag_simas;
	}

	public void setFlag_simas(Integer flag_simas) {
		this.flag_simas = flag_simas;
	}

	public Integer getMste_flag_guthrie() {
		return mste_flag_guthrie;
	}

	public void setMste_flag_guthrie(Integer mste_flag_guthrie) {
		this.mste_flag_guthrie = mste_flag_guthrie;
	}

	public Integer getFlag_gutri() {
		return flag_gutri;
	}

	public void setFlag_gutri(Integer flag_gutri) {
		this.flag_gutri = flag_gutri;
	}

	public String getLde_id() {
		return lde_id;
	}

	public void setLde_id(String lde_id) {
		this.lde_id = lde_id;
	}

	public String getStatus_submit() {
		return status_submit;
	}

	public void setStatus_submit(String status_submit) {
		this.status_submit = status_submit;
	}

	public String getKombinasi() {
		return kombinasi;
	}

	public void setKombinasi(String kombinasi) {
		this.kombinasi = kombinasi;
	}

	public Integer getMste_flag_investasi() {
		return mste_flag_investasi;
	}

	public void setMste_flag_investasi(Integer mste_flag_investasi) {
		this.mste_flag_investasi = mste_flag_investasi;
	}

	public Integer getMste_flag_el() {
		return mste_flag_el;
	}

	public void setMste_flag_el(Integer mste_flag_el) {
		this.mste_flag_el = mste_flag_el;
	}

	public Datausulan() {
		this.isPsave = false;
	}
	
	public Datausulan(Date mste_beg_date) {
		this.mste_beg_date = mste_beg_date;
	}
	
	public String getKeterangan_fund() {
		return keterangan_fund;
	}
	public void setKeterangan_fund(String keterangan_fund) {
		this.keterangan_fund = keterangan_fund;
	}
	public Integer getFlag_bulanan() {
		return flag_bulanan;
	}
	public void setFlag_bulanan(Integer flag_bulanan) {
		this.flag_bulanan = flag_bulanan;
	}
	public Integer getFlag_endowment() {
		return flag_endowment;
	}
	public void setFlag_endowment(Integer flag_endowment) {
		this.flag_endowment = flag_endowment;
	}
	public Integer getMspo_installment() {
		return mspo_installment;
	}
	public void setMspo_installment(Integer mspo_installment) {
		this.mspo_installment = mspo_installment;
	}
	public String getKodeproduk() {
		return kodeproduk;
	}
	public void setKodeproduk(String kodeproduk) {
		this.kodeproduk = kodeproduk;
	}
	public Double getJmlh_top() {
		return jmlh_top;
	}
	public void setJmlh_top(Double jmlh_top) {
		this.jmlh_top = jmlh_top;
	}

	public Integer getLi_trans_tunggal() {
		return li_trans_tunggal;
	}

	public void setLi_trans_tunggal(Integer li_trans_tunggal) {
		this.li_trans_tunggal = li_trans_tunggal;
	}

	public ArrayList getDaftabenef() {
		return daftabenef;
	}
	public void setDaftabenef(List daftabenef) {
		this.daftabenef = Common.serializableList(daftabenef);
	}
	public ArrayList getDaftaExtra() {
		return daftaExtra;
	}

	public void setDaftaExtra(ArrayList daftaExtra) {
		this.daftaExtra = Common.serializableList(daftaExtra);
	}

	public ArrayList getDaftaRider() {
		return daftaRider;
	}
	public void setDaftaRider(List daftaRider) {
		this.daftaRider = Common.serializableList(daftaRider);
	}
	public Integer getFlag_account() {
		return flag_account;
	}
	public void setFlag_account(Integer flag_account) {
		this.flag_account = flag_account;
	}
	public Integer getFlag_as() {
		return flag_as;
	}
	public void setFlag_as(Integer flag_as) {
		this.flag_as = flag_as;
	}
	public Integer getFlag_bao() {
		return flag_bao;
	}
	public void setFlag_bao(Integer flag_bao) {
		this.flag_bao = flag_bao;
	}
	public Integer getFlag_jenis_plan() {
		return flag_jenis_plan;
	}
	public void setFlag_jenis_plan(Integer flag_jenis_plan) {
		this.flag_jenis_plan = flag_jenis_plan;
	}
	public Integer getFlag_rider() {
		return flag_rider;
	}
	public void setFlag_rider(Integer flag_rider) {
		this.flag_rider = flag_rider;
	}
	public Integer getIndeks_validasi() {
		return indeks_validasi;
	}
	public void setIndeks_validasi(Integer indeks_validasi) {
		this.indeks_validasi = indeks_validasi;
	}
	public Integer getIsBonusTahapan() {
		return isBonusTahapan;
	}
	public void setIsBonusTahapan(Integer isBonusTahapan) {
		this.isBonusTahapan = isBonusTahapan;
	}
	public Integer getIsBungaSimponi() {
		return isBungaSimponi;
	}
	public void setIsBungaSimponi(Integer isBungaSimponi) {
		this.isBungaSimponi = isBungaSimponi;
	}
	public Integer getIsInvestasi() {
		return isInvestasi;
	}
	public void setIsInvestasi(Integer isInvestasi) {
		this.isInvestasi = isInvestasi;
	}
	public Integer getIsProductBancass() {
		return isProductBancass;
	}
	public void setIsProductBancass(Integer isProductBancass) {
		this.isProductBancass = isProductBancass;
	}
	public Integer getJml_benef() {
		return jml_benef;
	}
	public void setJml_benef(Integer jml_benef) {
		this.jml_benef = jml_benef;
	}
	public Integer getJmlrider() {
		return jmlrider;
	}
	public void setJmlrider(Integer jmlrider) {
		this.jmlrider = jmlrider;
	}
	public Integer getJmlrider_include() {
		return jmlrider_include;
	}
	public void setJmlrider_include(Integer jmlrider_include) {
		this.jmlrider_include = jmlrider_include;
	}
	public Integer getJumlah_seluruh_rider() {
		return jumlah_seluruh_rider;
	}
	public void setJumlah_seluruh_rider(Integer jumlah_seluruh_rider) {
		this.jumlah_seluruh_rider = jumlah_seluruh_rider;
	}
	public Integer getKode_flag() {
		return kode_flag;
	}
	public void setKode_flag(Integer kode_flag) {
		this.kode_flag = kode_flag;
	}
	public String getKurs_p() {
		return kurs_p;
	}
	public void setKurs_p(String kurs_p) {
		this.kurs_p = kurs_p;
	}
	public String getKurs_premi() {
		return kurs_premi;
	}
	public void setKurs_premi(String kurs_premi) {
		this.kurs_premi = kurs_premi;
	}
	public Integer getLi_umur_pp() {
		return li_umur_pp;
	}
	public void setLi_umur_pp(Integer li_umur_pp) {
		this.li_umur_pp = li_umur_pp;
	}
	public Integer getLi_umur_ttg() {
		return li_umur_ttg;
	}
	public void setLi_umur_ttg(Integer li_umur_ttg) {
		this.li_umur_ttg = li_umur_ttg;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public String getLku_symbol() {
		return lku_symbol;
	}
	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public Integer getLscb_id() {
		return lscb_id;
	}
	public void setLscb_id(Integer lscb_id) {
		this.lscb_id = lscb_id;
	}
	public String getLscb_pay_mode() {
		return lscb_pay_mode;
	}
	public void setLscb_pay_mode(String lscb_pay_mode) {
		this.lscb_pay_mode = lscb_pay_mode;
	}
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}
	public String getLsdbs_depkeu() {
		return lsdbs_depkeu;
	}
	public void setLsdbs_depkeu(String lsdbs_depkeu) {
		this.lsdbs_depkeu = lsdbs_depkeu;
	}
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	public String getMedis() {
		return medis;
	}
	public void setMedis(String medis) {
		this.medis = medis;
	}
	public Integer getMn() {
		return mn;
	}
	public void setMn(Integer mn) {
		this.mn = mn;
	}
	public Integer getMspo_pay_period() {
		return mspo_pay_period;
	}
	public void setMspo_pay_period(Integer mspo_pay_period) {
		this.mspo_pay_period = mspo_pay_period;
	}
	public Integer getMspr_class() {
		return mspr_class;
	}
	public void setMspr_class(Integer mspr_class) {
		this.mspr_class = mspr_class;
	}
	public Date getMspr_end_pay() {
		return mspr_end_pay;
	}
	public void setMspr_end_pay(Date mspr_end_pay) {
		this.mspr_end_pay = mspr_end_pay;
	}
	public Integer getMspr_ins_period() {
		return mspr_ins_period;
	}
	public void setMspr_ins_period(Integer mspr_ins_period) {
		this.mspr_ins_period = mspr_ins_period;
	}
	public Double getMspr_premium() {
		return mspr_premium;
	}
	public void setMspr_premium(Double mspr_premium) {
		this.mspr_premium = mspr_premium;
	}
	public Double getMspr_tsi() {
		return mspr_tsi;
	}
	public void setMspr_tsi(Double mspr_tsi) {
		this.mspr_tsi = mspr_tsi;
	}
	public Integer getMspr_tt() {
		return mspr_tt;
	}
	public void setMspr_tt(Integer mspr_tt) {
		this.mspr_tt = mspr_tt;
	}
	public Integer getMspr_unit() {
		return mspr_unit;
	}
	public void setMspr_unit(Integer mspr_unit) {
		this.mspr_unit = mspr_unit;
	}
	public Date getMste_beg_date() {
		return mste_beg_date;
	}
	public void setMste_beg_date(Date mste_beg_date) {
		this.mste_beg_date = mste_beg_date;
	}
	public Date getMste_end_date() {
		return mste_end_date;
	}
	public void setMste_end_date(Date mste_end_date) {
		this.mste_end_date = mste_end_date;
	}
	public Integer getMste_flag_cc() {
		return mste_flag_cc;
	}
	public void setMste_flag_cc(Integer mste_flag_cc) {
		this.mste_flag_cc = mste_flag_cc;
	}
	public Integer getMste_insured_no() {
		return mste_insured_no;
	}
	public void setMste_insured_no(Integer mste_insured_no) {
		this.mste_insured_no = mste_insured_no;
	}
	public Integer getMste_medical() {
		return mste_medical;
	}
	public void setMste_medical(Integer mste_medical) {
		this.mste_medical = mste_medical;
	}
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	public Double getRate_plan() {
		return rate_plan;
	}
	public void setRate_plan(Double rate_plan) {
		this.rate_plan = rate_plan;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getTipeproduk() {
		return tipeproduk;
	}
	public void setTipeproduk(Integer tipeproduk) {
		this.tipeproduk = tipeproduk;
	}
	public Double getTotal_premi_rider() {
		return total_premi_rider;
	}
	public void setTotal_premi_rider(Double total_premi_rider) {
		this.total_premi_rider = total_premi_rider;
	}
	public Double getTotal_premi_sementara() {
		return total_premi_sementara;
	}
	public void setTotal_premi_sementara(Double total_premi_sementara) {
		this.total_premi_sementara = total_premi_sementara;
	}
	public Integer getFlag_worksite() {
		return flag_worksite;
	}
	public void setFlag_worksite(Integer flag_worksite) {
		this.flag_worksite = flag_worksite;
	}
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
	public Integer getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}
	public Integer getLsre_id() {
		return lsre_id;
	}
	public void setLsre_id(Integer lsre_id) {
		this.lsre_id = lsre_id;
	}
	public String getLsrg_id() {
		return lsrg_id;
	}
	public void setLsrg_id(String lsrg_id) {
		this.lsrg_id = lsrg_id;
	}
	public Integer getLssp_id() {
		return lssp_id;
	}
	public void setLssp_id(Integer lssp_id) {
		this.lssp_id = lssp_id;
	}
	public Integer getLstb_id() {
		return lstb_id;
	}
	public void setLstb_id(Integer lstb_id) {
		this.lstb_id = lstb_id;
	}
	public Integer getLstp_id() {
		return lstp_id;
	}
	public void setLstp_id(Integer lstp_id) {
		this.lstp_id = lstp_id;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getLwk_id() {
		return lwk_id;
	}
	public void setLwk_id(String lwk_id) {
		this.lwk_id = lwk_id;
	}
	public String getMpi_mou() {
		return mpi_mou;
	}
	public void setMpi_mou(String mpi_mou) {
		this.mpi_mou = mpi_mou;
	}
	public Integer getMspo_age() {
		return mspo_age;
	}
	public void setMspo_age(Integer mspo_age) {
		this.mspo_age = mspo_age;
	}
	public String getMspo_ao() {
		return mspo_ao;
	}
	public void setMspo_ao(String mspo_ao) {
		this.mspo_ao = mspo_ao;
	}
	public Integer getMspo_as_charges() {
		return mspo_as_charges;
	}
	public void setMspo_as_charges(Integer mspo_as_charges) {
		this.mspo_as_charges = mspo_as_charges;
	}
	public Integer getMspo_aso() {
		return mspo_aso;
	}
	public void setMspo_aso(Integer mspo_aso) {
		this.mspo_aso = mspo_aso;
	}
	public String getMspo_beasiswa() {
		return mspo_beasiswa;
	}
	public void setMspo_beasiswa(String mspo_beasiswa) {
		this.mspo_beasiswa = mspo_beasiswa;
	}
	public Date getMspo_beg_date() {
		return mspo_beg_date;
	}
	public void setMspo_beg_date(Date mspo_beg_date) {
		this.mspo_beg_date = mspo_beg_date;
	}
	public Double getMspo_biaya_transfer() {
		return mspo_biaya_transfer;
	}
	public void setMspo_biaya_transfer(Double mspo_biaya_transfer) {
		this.mspo_biaya_transfer = mspo_biaya_transfer;
	}
	public Integer getMspo_cara_beasiswa() {
		return mspo_cara_beasiswa;
	}
	public void setMspo_cara_beasiswa(Integer mspo_cara_beasiswa) {
		this.mspo_cara_beasiswa = mspo_cara_beasiswa;
	}
	public Integer getMspo_cash_back() {
		return mspo_cash_back;
	}
	public void setMspo_cash_back(Integer mspo_cash_back) {
		this.mspo_cash_back = mspo_cash_back;
	}
	public String getMspo_cash_dpp() {
		return mspo_cash_dpp;
	}
	public void setMspo_cash_dpp(String mspo_cash_dpp) {
		this.mspo_cash_dpp = mspo_cash_dpp;
	}
	public String getMspo_cash_entry() {
		return mspo_cash_entry;
	}
	public void setMspo_cash_entry(String mspo_cash_entry) {
		this.mspo_cash_entry = mspo_cash_entry;
	}
	public String getMspo_cash_mkt() {
		return mspo_cash_mkt;
	}
	public void setMspo_cash_mkt(String mspo_cash_mkt) {
		this.mspo_cash_mkt = mspo_cash_mkt;
	}
	public Double getMspo_cash_rate_1() {
		return mspo_cash_rate_1;
	}
	public void setMspo_cash_rate_1(Double mspo_cash_rate_1) {
		this.mspo_cash_rate_1 = mspo_cash_rate_1;
	}
	public Double getMspo_cash_rate_2() {
		return mspo_cash_rate_2;
	}
	public void setMspo_cash_rate_2(Double mspo_cash_rate_2) {
		this.mspo_cash_rate_2 = mspo_cash_rate_2;
	}
	public String getMspo_cash_verifikasi() {
		return mspo_cash_verifikasi;
	}
	public void setMspo_cash_verifikasi(String mspo_cash_verifikasi) {
		this.mspo_cash_verifikasi = mspo_cash_verifikasi;
	}
	public Integer getMspo_cash_wil() {
		return mspo_cash_wil;
	}
	public void setMspo_cash_wil(Integer mspo_cash_wil) {
		this.mspo_cash_wil = mspo_cash_wil;
	}
	public String getMspo_customer() {
		return mspo_customer;
	}
	public void setMspo_customer(String mspo_customer) {
		this.mspo_customer = mspo_customer;
	}
	public Date getMspo_date_print() {
		return mspo_date_print;
	}
	public void setMspo_date_print(Date mspo_date_print) {
		this.mspo_date_print = mspo_date_print;
	}
	public Date getMspo_date_spec() {
		return mspo_date_spec;
	}
	public void setMspo_date_spec(Date mspo_date_spec) {
		this.mspo_date_spec = mspo_date_spec;
	}
	public Date getMspo_date_ttp() {
		return mspo_date_ttp;
	}
	public void setMspo_date_ttp(Date mspo_date_ttp) {
		this.mspo_date_ttp = mspo_date_ttp;
	}
	public Date getMspo_end_date() {
		return mspo_end_date;
	}
	public void setMspo_end_date(Date mspo_end_date) {
		this.mspo_end_date = mspo_end_date;
	}
	public Double getMspo_extend() {
		return mspo_extend;
	}
	public void setMspo_extend(Double mspo_extend) {
		this.mspo_extend = mspo_extend;
	}
	public Integer getMspo_flat() {
		return mspo_flat;
	}
	public void setMspo_flat(Integer mspo_flat) {
		this.mspo_flat = mspo_flat;
	}
	public Integer getMspo_follow_up() {
		return mspo_follow_up;
	}
	public void setMspo_follow_up(Integer mspo_follow_up) {
		this.mspo_follow_up = mspo_follow_up;
	}
	public Date getMspo_input_date() {
		return mspo_input_date;
	}
	public void setMspo_input_date(Date mspo_input_date) {
		this.mspo_input_date = mspo_input_date;
	}
	public Integer getMspo_ins_bulan() {
		return mspo_ins_bulan;
	}
	public void setMspo_ins_bulan(Integer mspo_ins_bulan) {
		this.mspo_ins_bulan = mspo_ins_bulan;
	}
	public Integer getMspo_ins_period() {
		return mspo_ins_period;
	}
	public void setMspo_ins_period(Integer mspo_ins_period) {
		this.mspo_ins_period = mspo_ins_period;
	}
	public Integer getMspo_jn_coas() {
		return mspo_jn_coas;
	}
	public void setMspo_jn_coas(Integer mspo_jn_coas) {
		this.mspo_jn_coas = mspo_jn_coas;
	}
	public Integer getMspo_komisi_bii() {
		return mspo_komisi_bii;
	}
	public void setMspo_komisi_bii(Integer mspo_komisi_bii) {
		this.mspo_komisi_bii = mspo_komisi_bii;
	}
	public Integer getMspo_lama_beasiswa() {
		return mspo_lama_beasiswa;
	}
	public void setMspo_lama_beasiswa(Integer mspo_lama_beasiswa) {
		this.mspo_lama_beasiswa = mspo_lama_beasiswa;
	}
	public String getMspo_nasabah_acc() {
		return mspo_nasabah_acc;
	}
	public void setMspo_nasabah_acc(String mspo_nasabah_acc) {
		this.mspo_nasabah_acc = mspo_nasabah_acc;
	}
	public String getMspo_nasabah_dcif() {
		return mspo_nasabah_dcif;
	}
	public void setMspo_nasabah_dcif(String mspo_nasabah_dcif) {
		this.mspo_nasabah_dcif = mspo_nasabah_dcif;
	}
	public Date getMspo_next_bill() {
		return mspo_next_bill;
	}
	public void setMspo_next_bill(Date mspo_next_bill) {
		this.mspo_next_bill = mspo_next_bill;
	}
	public String getMspo_no_blanko() {
		return mspo_no_blanko;
	}
	public void setMspo_no_blanko(String mspo_no_blanko) {
		this.mspo_no_blanko = mspo_no_blanko;
	}
	public String getMspo_no_kerjasama() {
		return mspo_no_kerjasama;
	}
	public void setMspo_no_kerjasama(String mspo_no_kerjasama) {
		this.mspo_no_kerjasama = mspo_no_kerjasama;
	}
	public String getMspo_no_pol_asm() {
		return mspo_no_pol_asm;
	}
	public void setMspo_no_pol_asm(String mspo_no_pol_asm) {
		this.mspo_no_pol_asm = mspo_no_pol_asm;
	}
	public String getMspo_note() {
		return mspo_note;
	}
	public void setMspo_note(String mspo_note) {
		this.mspo_note = mspo_note;
	}
	public String getMspo_oldpol_eb() {
		return mspo_oldpol_eb;
	}
	public void setMspo_oldpol_eb(String mspo_oldpol_eb) {
		this.mspo_oldpol_eb = mspo_oldpol_eb;
	}
	public String getMspo_pass_eb() {
		return mspo_pass_eb;
	}
	public void setMspo_pass_eb(String mspo_pass_eb) {
		this.mspo_pass_eb = mspo_pass_eb;
	}
	public String getMspo_plan_provider() {
		return mspo_plan_provider;
	}
	public void setMspo_plan_provider(String mspo_plan_provider) {
		this.mspo_plan_provider = mspo_plan_provider;
	}
	public String getMspo_policy_holder() {
		return mspo_policy_holder;
	}
	public void setMspo_policy_holder(String mspo_policy_holder) {
		this.mspo_policy_holder = mspo_policy_holder;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public String getMspo_policy_no_format() {
		return mspo_policy_no_format;
	}
	public void setMspo_policy_no_format(String mspo_policy_no_format) {
		this.mspo_policy_no_format = mspo_policy_no_format;
	}
	public Integer getMspo_preexisting() {
		return mspo_preexisting;
	}
	public void setMspo_preexisting(Integer mspo_preexisting) {
		this.mspo_preexisting = mspo_preexisting;
	}
	public Integer getMspo_pribadi() {
		return mspo_pribadi;
	}
	public void setMspo_pribadi(Integer mspo_pribadi) {
		this.mspo_pribadi = mspo_pribadi;
	}
	public Integer getMspo_proses_bill() {
		return mspo_proses_bill;
	}
	public void setMspo_proses_bill(Integer mspo_proses_bill) {
		this.mspo_proses_bill = mspo_proses_bill;
	}
	public Integer getMspo_provider() {
		return mspo_provider;
	}
	public void setMspo_provider(Integer mspo_provider) {
		this.mspo_provider = mspo_provider;
	}
	public Integer getMspo_ref_bii() {
		return mspo_ref_bii;
	}
	public void setMspo_ref_bii(Integer mspo_ref_bii) {
		this.mspo_ref_bii = mspo_ref_bii;
	}
	public Date getMspo_spaj_date() {
		return mspo_spaj_date;
	}
	public void setMspo_spaj_date(Date mspo_spaj_date) {
		this.mspo_spaj_date = mspo_spaj_date;
	}
	public Integer getMspo_syahriah() {
		return mspo_syahriah;
	}
	public void setMspo_syahriah(Integer mspo_syahriah) {
		this.mspo_syahriah = mspo_syahriah;
	}
	public Integer getMspo_umur_beasiswa() {
		return mspo_umur_beasiswa;
	}
	public void setMspo_umur_beasiswa(Integer mspo_umur_beasiswa) {
		this.mspo_umur_beasiswa = mspo_umur_beasiswa;
	}
	public Double getMspo_under_table() {
		return mspo_under_table;
	}
	public void setMspo_under_table(Double mspo_under_table) {
		this.mspo_under_table = mspo_under_table;
	}
	public Integer getLssa_id() {
		return lssa_id;
	}
	public void setLssa_id(Integer lssa_id) {
		this.lssa_id = lssa_id;
	}
	public Double getMspr_discount() {
		return mspr_discount;
	}
	public void setMspr_discount(Double mspr_discount) {
		this.mspr_discount = mspr_discount;
	}
	public Integer getFlag_ekalink() {
		return flag_ekalink;
	}
	public void setFlag_ekalink(Integer flag_ekalink) {
		this.flag_ekalink = flag_ekalink;
	}
	public Integer getFlag_worksite_ekalife() {
		return flag_worksite_ekalife;
	}
	public void setFlag_worksite_ekalife(Integer flag_worksite_ekalife) {
		this.flag_worksite_ekalife = flag_worksite_ekalife;
	}
	public Integer getFlag_horison() {
		return flag_horison;
	}
	public void setFlag_horison(Integer flag_horison) {
		this.flag_horison = flag_horison;
	}

	public Double getTotal_premi_kombinasi() {
		return total_premi_kombinasi;
	}

	public void setTotal_premi_kombinasi(Double total_premi_kombinasi) {
		this.total_premi_kombinasi = total_premi_kombinasi;
	}

	public Integer getCara_premi() {
		return cara_premi;
	}

	public void setCara_premi(Integer cara_premi) {
		this.cara_premi = cara_premi;
	}

	public HashMap getWorksite() {
		return worksite;
	}

	public void setWorksite(Map worksite) {
		this.worksite = Common.serializableMap(worksite);
	}

	public Integer getFlag_platinumlink() {
		return flag_platinumlink;
	}

	public void setFlag_platinumlink(Integer flag_platinumlink) {
		this.flag_platinumlink = flag_platinumlink;
	}

	public ArrayList getDaftapeserta() {
		return daftapeserta;
	}

	public void setDaftapeserta(List daftapeserta) {
		this.daftapeserta = Common.serializableList(daftapeserta);
	}

	public Integer getJml_peserta() {
		return jml_peserta;
	}

	public void setJml_peserta(Integer jml_peserta) {
		this.jml_peserta = jml_peserta;
	}

	public Integer getLi_trans_berkala() {
		return li_trans_berkala;
	}

	public void setLi_trans_berkala(Integer li_trans_berkala) {
		this.li_trans_berkala = li_trans_berkala;
	}

	public ArrayList getDaftahcp() {
		return daftahcp;
	}

	public void setDaftahcp(List daftahcp) {
		this.daftahcp = Common.serializableList(daftahcp);
	}

	public ArrayList getDaftariderhcp() {
		return daftariderhcp;
	}

	public void setDaftariderhcp(List daftariderhcp) {
		this.daftariderhcp = Common.serializableList(daftariderhcp);
	}

	public ArrayList getDaftariderall() {
		return daftariderall;
	}

	public void setDaftariderall(List daftariderall) {
		this.daftariderall = Common.serializableList(daftariderall);
	}

	public Integer getFlag_rider_hcp() {
		return flag_rider_hcp;
	}

	public void setFlag_rider_hcp(Integer flag_rider_hcp) {
		this.flag_rider_hcp = flag_rider_hcp;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}

	public Integer getFlag_uppremi() {
		return flag_uppremi;
	}

	public void setFlag_uppremi(Integer flag_uppremi) {
		this.flag_uppremi = flag_uppremi;
	}

	public Integer getMspo_self_ins() {
		return mspo_self_ins;
	}

	public void setMspo_self_ins(Integer mspo_self_ins) {
		this.mspo_self_ins = mspo_self_ins;
	}

	public String getKopiSPAJ() {
		return kopiSPAJ;
	}

	public void setKopiSPAJ(String kopiSPAJ) {
		this.kopiSPAJ = kopiSPAJ;
	}

	public void setConvert(Integer convert) {
		this.convert = convert;
	}

	public Integer getConvert() {
		return convert;
	}

	public Integer getFlag_paymode_rider() {
		return flag_paymode_rider;
	}

	public void setFlag_paymode_rider(Integer flag_paymode_rider) {
		this.flag_paymode_rider = flag_paymode_rider;
	}

	public Integer getLscb_id_rider() {
		return lscb_id_rider;
	}

	public void setLscb_id_rider(Integer lscb_id_rider) {
		this.lscb_id_rider = lscb_id_rider;
	}

	public Integer getJenis_pemegang_polis() {
		return jenis_pemegang_polis;
	}

	public void setJenis_pemegang_polis(Integer jenis_pemegang_polis) {
		this.jenis_pemegang_polis = jenis_pemegang_polis;
	}

	public boolean isPlatinumSave() {
		return isPlatinumSave;
	}

	public void setPlatinumSave(boolean isPlatinumSave) {
		this.isPlatinumSave = isPlatinumSave;
	}

	public Integer getMssur_se() {
		return mssur_se;
	}

	public void setMssur_se(Integer mssur_se) {
		this.mssur_se = mssur_se;
	}

	public Integer getAlertEkaSehat() {
		return alertEkaSehat;
	}

	public void setAlertEkaSehat(Integer alertEkaSehat) {
		this.alertEkaSehat = alertEkaSehat;
	}

	public Integer getPersenUp() {
		return persenUp;
	}

	public void setPersenUp(Integer persenUp) {
		this.persenUp = persenUp;
	}
	
	public Integer getJmlDaftarPesertax(){
		return jmlDaftarPesertax;
	}
	public void setJmlDaftarpesertax(Integer jmlDaftarPesertax){
		this.jmlDaftarPesertax = jmlDaftarPesertax;
	}
	
	public Integer getPesertax(){
		return pesertax;
	}
	public void setPesertax(Integer pesertax){
		this.pesertax = pesertax;
	}
	
	public ArrayList getDaftarplus(){
		return daftarplus;
	}
	public void setDaftarplus(List daftarplus){
		this.daftarplus = Common.serializableList(daftarplus);
	}
	
	public ArrayList getDaftarplus_mix(){
		return daftarplus_mix;
	}
	public void setDaftarplus_mix(List daftarplus_mix){
		this.daftarplus_mix = Common.serializableList(daftarplus_mix);
	}
	
	public ArrayList getDaftarPesertaNew(){
		return daftarPesertaNew;
	}
	public void setDaftarPesertaNew(List daftarPesertaNew){
		this.daftarPesertaNew = Common.serializableList(daftarPesertaNew);
	}	
	
	public ArrayList getDaftarplus_mix_new(){
		return daftarplus_mix_new;
	}
	public void setDaftarplus_mix_new(List daftarplus_mix_new){
		this.daftarplus_mix_new = Common.serializableList(daftarplus_mix_new);
	}
		
	public ArrayList getDaftarplusplus(){
		return daftarplusplus;
	}
	public void setDaftarplusplus(List daftarplusplus){
		this.daftarplusplus = Common.serializableList(daftarplusplus);
	}
	
	public Integer getFlag_compare_baru(){
		return flag_compare_baru;
	}
	public void setFlag_compare_baru(Integer flag_compare_baru){
		this.flag_compare_baru = flag_compare_baru;
	}
	
	public Integer getFlag_compare(){
		return flag_compare;
	}
	public void setFlag_compare(Integer flag_compare){
		this.flag_compare = flag_compare;
	}
	
	public Integer getFlag_compare2(){
		return flag_compare2;
	}
	public void setFlag_compare2(Integer flag_compare2){
		this.flag_compare2 = flag_compare2;
	}	
	
	public Integer getJmlriderplus(){
		return jmlriderplus;
	}
	public void setJmlriderplus(Integer jmlriderplus){
		this.jmlriderplus = jmlriderplus;
	}
	public Integer getJmlJenisRider(){
		return jmlJenisRider;
	}
	public void setJmlJenisRider(Integer jmlJenisRider){
		this.jmlJenisRider = jmlJenisRider;
	}
	public Integer getJRSpouse(){
		return jRSpouse;
	}
	public void setJRSpouse(Integer jRSpouse){
		this.jRSpouse = jRSpouse;
	}
	public Integer getJRChild1(){
		return jRChild1;
	}
	public void setJRChild1(Integer jRChild1){
		this.jRChild1 = jRChild1;
	}
	public Integer getJRChild2(){
		return jRChild2;
	}
	public void setJRChild2(Integer jRChild2){
		this.jRChild2 = jRChild2;
	}
	public Integer getJRChild3(){
		return jRChild3;
	}
	public void setJRChild3(Integer jRChild3){
		this.jRChild3 = jRChild3;
	}
	public Integer getJRChild4(){
		return jRChild4;
	}
	public void setJRChild4(Integer jRChild4){
		this.jRChild4 = jRChild4;
	}
	public Integer getJRChild5(){
		return jRChild5;
	}
	public void setJRChild5(Integer jRChild5){
		this.jRChild5 = jRChild5;
	}
	public Integer getCekBasic(){
		return cekBasic;
	}
	public void setCekBasic(Integer cekBasic){
		this.cekBasic = cekBasic;
	}
	public Integer getCekCompare(){
		return cekCompare;
	}
	public void setCekCompare(Integer cekCompare){
		this.cekCompare = cekCompare;
	}
	public Integer getFlag_jenis_peserta(){
		return flag_jenis_peserta;
	}
	public void setFlag_jenis_peserta(Integer flag_jenis_peserta){
		this.flag_jenis_peserta = flag_jenis_peserta;
	}
	public Integer getMp_main(){
		return mp_main;
	}
	public void setMp_main(Integer mp_main){
		this.mp_main = mp_main;
	}
	public String getAkhir_polis1() {
		return akhir_polis1;
	}
	public void setAkhir_polis1(String akhir_polis1) {
		this.akhir_polis1 = akhir_polis1;
	}
	public String getAkhir_bayar1() {
		return akhir_bayar1;
	}
	public void setAkhir_bayar1(String akhir_bayar1) {
		this.akhir_bayar1 = akhir_bayar1;
	}
	
	public Integer getTahun_polis(){
		return tahun_polis;
	}
	public void setTahun_polis(Integer tahun_polis){
		this.tahun_polis = tahun_polis;
	}
	public Integer getTahun_bayar(){
		return tahun_bayar;
	}
	public void setTahun_bayar(Integer tahun_bayar){
		this.tahun_bayar = tahun_bayar;
	}
	public Integer getLsbs_id_plus() {
		return lsbs_id_plus;
	}
	public void setLsbs_id_plus(Integer lsbs_id_plus) {
		this.lsbs_id_plus = lsbs_id_plus;
	}
	public Integer getTgl_tertanggung_I() {
		return tgl_tertanggung_I;
	}
	public void setTgl_tertanggung_I(Integer tgl_tertanggung_I) {
		this.tgl_tertanggung_I = tgl_tertanggung_I;
	}
	public Integer getMasa_tanggung_I() {
		return masa_tanggung_I;
	}
	public void setMasa_tanggung_I(Integer masa_tanggung_I) {
		this.masa_tanggung_I = masa_tanggung_I;
	}
	public Integer getMsen_aksep_uw() {
		return msen_aksep_uw;
	}
	public void setMsen_aksep_uw(Integer msen_aksep_uw) {
		this.msen_aksep_uw = msen_aksep_uw;
	}
	public Integer getFlagspecial() {
		return flagspecial;
	}
	public void setFlagspecial(Integer flagspecial) {
		this.flagspecial = flagspecial;
	}	
	public Integer getMste_gmit() {
		return mste_gmit;
	}
	public void setMste_gmit(Integer mste_gmit) {
		this.mste_gmit = mste_gmit;
	}	
	
	public String getNama_paket() {
		return nama_paket;
	}

	public void setNama_paket(String nama_paket) {
		this.nama_paket = nama_paket;
	}	

	public ArrayList getDaftaRiderAddOnTtg() {
		return daftaRiderAddOnTtg;
	}

	public void setDaftaRiderAddOnTtg(ArrayList daftaRiderAddOnTtg) {
		this.daftaRiderAddOnTtg = Common.serializableList(daftaRiderAddOnTtg);
	}

	public ArrayList getDaftaRiderAddOnTtg1() {
		return daftaRiderAddOnTtg1;
	}

	public void setDaftaRiderAddOnTtg1(ArrayList daftaRiderAddOnTtg1) {
		this.daftaRiderAddOnTtg1 = Common.serializableList(daftaRiderAddOnTtg1);
	}

	public ArrayList getDaftaRiderMedicalPlusTtg1() {
		return daftaRiderMedicalPlusTtg1;
	}

	public void setDaftaRiderMedicalPlusTtg1(ArrayList daftaRiderMedicalPlusTtg1) {
		this.daftaRiderMedicalPlusTtg1 = Common.serializableList(daftaRiderMedicalPlusTtg1);
	}

	public ArrayList getDaftaRiderAddOnTtg2() {
		return daftaRiderAddOnTtg2;
	}

	public void setDaftaRiderAddOnTtg2(ArrayList daftaRiderAddOnTtg2) {
		this.daftaRiderAddOnTtg2 = Common.serializableList(daftaRiderAddOnTtg2);
	}

	public ArrayList getDaftaRiderMedicalPlusTtg2() {
		return daftaRiderMedicalPlusTtg2;
	}

	public void setDaftaRiderMedicalPlusTtg2(ArrayList daftaRiderMedicalPlusTtg2) {
		this.daftaRiderMedicalPlusTtg2 = Common.serializableList(daftaRiderMedicalPlusTtg2);
	}

	public ArrayList getDaftaRiderAddOnTtg3() {
		return daftaRiderAddOnTtg3;
	}

	public void setDaftaRiderAddOnTtg3(ArrayList daftaRiderAddOnTtg3) {
		this.daftaRiderAddOnTtg3 = Common.serializableList(daftaRiderAddOnTtg3);
	}

	public ArrayList getDaftaRiderMedicalPlusTtg3() {
		return daftaRiderMedicalPlusTtg3;
	}

	public void setDaftaRiderMedicalPlusTtg3(ArrayList daftaRiderMedicalPlusTtg3) {
		this.daftaRiderMedicalPlusTtg3 = Common.serializableList(daftaRiderMedicalPlusTtg3);
	}

	public ArrayList getDaftaRiderAddOnTtg4() {
		return daftaRiderAddOnTtg4;
	}

	public void setDaftaRiderAddOnTtg4(ArrayList daftaRiderAddOnTtg4) {
		this.daftaRiderAddOnTtg4 = Common.serializableList(daftaRiderAddOnTtg4);
	}

	public ArrayList getDaftaRiderMedicalPlusTtg4() {
		return daftaRiderMedicalPlusTtg4;
	}

	public void setDaftaRiderMedicalPlusTtg4(ArrayList daftaRiderMedicalPlusTtg4) {
		this.daftaRiderMedicalPlusTtg4 = Common.serializableList(daftaRiderMedicalPlusTtg4);
	}

	public ArrayList getDaftaRiderAddOnTtg5() {
		return daftaRiderAddOnTtg5;
	}

	public void setDaftaRiderAddOnTtg5(ArrayList daftaRiderAddOnTtg5) {
		this.daftaRiderAddOnTtg5 = Common.serializableList(daftaRiderAddOnTtg5);
	}

	public ArrayList getDaftaRiderMedicalPlusTtg5() {
		return daftaRiderMedicalPlusTtg5;
	}

	public void setDaftaRiderMedicalPlusTtg5(ArrayList daftaRiderMedicalPlusTtg5) {
		this.daftaRiderMedicalPlusTtg5 = Common.serializableList(daftaRiderMedicalPlusTtg5);
	}

	public ArrayList getDaftaRiderAddOnTtg6() {
		return daftaRiderAddOnTtg6;
	}

	public void setDaftaRiderAddOnTtg6(ArrayList daftaRiderAddOnTtg6) {
		this.daftaRiderAddOnTtg6 = Common.serializableList(daftaRiderAddOnTtg6);
	}

	public ArrayList getDaftaRiderMedicalPlusTtg6() {
		return daftaRiderMedicalPlusTtg6;
	}

	public void setDaftaRiderMedicalPlusTtg6(ArrayList daftaRiderMedicalPlusTtg6) {
		this.daftaRiderMedicalPlusTtg6 = Common.serializableList(daftaRiderMedicalPlusTtg6);
	}

	public Integer getDaftaRiderMedicalPlus() {
		return daftaRiderMedicalPlus;
	}

	public void setDaftaRiderMedicalPlus(Integer daftaRiderMedicalPlus) {
		this.daftaRiderMedicalPlus = daftaRiderMedicalPlus;
	}

	public Integer getDaftaRiderAddOn() {
		return daftaRiderAddOn;
	}

	public void setDaftaRiderAddOn(Integer daftaRiderAddOn) {
		this.daftaRiderAddOn = daftaRiderAddOn;
	}

	public Integer getFlag_riderAddon() {
		return flag_riderAddon;
	}

	public void setFlag_riderAddon(Integer flag_riderAddon) {
		this.flag_riderAddon = flag_riderAddon;
	}

	public Integer getTgl_tertanggung_I_RI() {
		return tgl_tertanggung_I_RI;
	}

	public void setTgl_tertanggung_I_RI(Integer tgl_tertanggung_I_RI) {
		this.tgl_tertanggung_I_RI = tgl_tertanggung_I_RI;
	}

	public Integer getEdit_dataUsulan() {
		return edit_dataUsulan;
	}

	public void setEdit_dataUsulan(Integer edit_dataUsulan) {
		this.edit_dataUsulan = edit_dataUsulan;
	}

	public String getKriteria_kesalahan() {
		return kriteria_kesalahan;
	}

	public void setKriteria_kesalahan(String kriteria_kesalahan) {
		this.kriteria_kesalahan = kriteria_kesalahan;
	}
	
	public HashMap getPsn() {
		return psn;
	}

	public void setPsn(Map psn) {
		this.psn = Common.serializableMap(psn);
	}

	public Integer getMspo_flag_spaj() {
		return mspo_flag_spaj;
	}

	public void setMspo_flag_spaj(Integer mspo_flag_spaj) {
		this.mspo_flag_spaj = mspo_flag_spaj;
	}
	
	// tambah pilihan nonbatch untuk simas primelink (rider save). helpdesk [128634/128295] //Chandra
	public String getPilih_batch() {
		return pilih_batch;
	}

	public void setPilih_batch(String pilih_batch) {
		this.pilih_batch = pilih_batch;
	}
	
	// helpdesk [137730] tambahin pilihan campaign
	public String getCampaign_id() {
		return campaign_id;
	}
	public void setCampaign_id(String campaign_id) {
		this.campaign_id = campaign_id;
	}
	public String getCampaign_name() {
		return campaign_name;
	}
	public void setCampaign_name(String campaign_name) {
		this.campaign_name = campaign_name;
	}
	public String getCampradio() {
		return campradio;
	}
	public void setCampradio(String campradio) {
		this.campradio = campradio;
	}
	public String getFlag_vip() {
		return flag_vip;
	}

	public void setFlag_vip(String flag_vip) {
		this.flag_vip = flag_vip;
	}

	public Double getPremiTunggal() {
		return premiTunggal;
	}

	public void setPremiTunggal(Double premiTunggal) {
		this.premiTunggal = premiTunggal;
	}

	public String getFull_autosales() {
		return full_autosales;
	}

	public void setFull_autosales(String full_autosales) {
		this.full_autosales = full_autosales;
	}

	public String getEnable_full_autosales() {
		return enable_full_autosales;
	}

	public void setEnable_full_autosales(String enable_full_autosales) {
		this.enable_full_autosales = enable_full_autosales;
	}
	
}
