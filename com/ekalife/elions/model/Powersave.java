package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Powersave implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Integer msl_proses_bsm;
	public Integer mps_kode;                      
	public Date mps_deposit_date;                 
	public String reg_spaj;                       
	public Date mps_batas_date;                   
	public Integer mps_roll_over;                 
	public String mps_jangka_inv;                
	public Date mps_rate_date;                    
	public Double mps_rate;                       
	public Double mps_prm_deposit;                
	public Double mps_prm_interest;               
	public Double mps_prm_insurance;              
	public Double mps_prm_debet;                  
	public Double mps_tax;                        
	public Date mps_input_date;                   
	public Integer lus_id;                        
	public Integer mps_employee;
	public String mpr_note;                       
	public Integer mps_jenis_plan;                
	public Integer mpr_nett_tax;
	public Date mpr_mature_date;
	public String mpr_jangka_invest;
	public Double mpr_deposit;
	public Double mpr_interest;
	public Double mpr_rate;
	public Date mpr_tgl_rate;                     
	public Integer mpr_jns_ro;
	public Integer mpr_hari;                      
	public Integer mpr_jns_rumus; 
	public Integer flag_powersave;
	public Date mps_end_period;
	public Integer mpr_cara_bayar_rider;
	public Integer flag_rider; //0->non rider, 1->rider
	public Double mpr_rider_total;
    
	public Integer msl_no;                        
	public String lji_id;                         
	public Integer msl_kode;                      
	public Integer msl_ro;                        
	public Integer msl_posisi;                    
	public Date msl_bdate;                        
	public Date msl_edate;                        
	public Double msl_premi;                      
	public Double msl_rate;                       
	public Integer msl_mgi;                       
	public Double msl_bunga;                      
	public Double msl_bp_rate;                    
	public Date msl_tgl_nab_bp;                   
	public Double msl_nab_bp;                     
	public Double msl_bp;                         
	public Double msl_up;                         
	public Integer msl_hari;                      
	public Date msl_next_date;                    
	public Integer msl_bayar_bunga;               
	public Integer msl_bayar_bp;
	public String no_reg;
	public String msl_desc;
	
	public Double msl_tax;                        
	public Date msl_tgl_nab;                      
	public Double msl_nab;                        
	public Double msl_unit;                       
	public Double msl_bp_pt;                      
	public Date msl_input_date;                   
	public Date msl_proses_date;                  
	public Date msl_confirm_date;                 
	public Date msl_prod_date;                    
	public Integer flag_rate;                     
	public String msl_note;     
	public Integer msl_tahun_ke;
	public Integer msl_premi_ke;
	public Date msl_letter_date;
	public Date msl_paid_date;
	
	public Date begdate_topup;
	public Date enddate_topup;
	public Date enddate_topup1;
	
	public Integer tarik_bunga;                   
	public Double msl_saldo_unit;                 
	public Integer msl_aktif;                     
	public Double msl_tarik_pertama;
	public Integer msl_tu_ke;
	public Integer msl_flag_up;
	
	public Double fee_based_income;
	public Integer mpr_breakable;
	
	public Double msl_nilai_polis;                
	public Date msl_trans_date;                   
	public Integer msl_flag_print;                
	public Date msl_print_date;                   
	public Integer msl_jn_rumus;                  
	public Integer msl_flag_comm;                 
	public Integer msl_print;                     
	public String no_reg_ref;                     
	public Integer msl_bayar_tarik;        
	public Integer flag_bulanan; //flag stable link bulanan (0 no, 1 yes)
	public String no_trx;
	public String msen_endors_no;
	
	//Yusuf (31 Aug 2009) - Bagian dari proses Konversi Psave ke Slink
	public String msl_spaj_lama;

	//variabel2 tambahan untuk menghitung untuk premi TU stable link
	public Date msl_tgl_nab_tu;                      
	public Double msl_nab_tu;                        
	public Double msl_unit_tu;                       	
	public Double mps_prm_interest_tu;               
	
	//tambahan untuk tampilan saja
	public String mspo_policy_no;
	public String mspo_policy_no_format;
	public String mcl_first;
	public String lsdbs_name;
	public String lus_login_name;
	public Integer lsbs_id;
	public Integer lsdbs_number;
	public Date mste_beg_date;
	public Integer msbi_tahun_ke;
	public Integer msbi_premi_ke;
	public Double mspr_premium;
	
	
	//TAMBAHAN UNTUK LISTING FILE2 PDF ROLLOVER / TOPUP
	public String fileName;
	public String fileDirectory;
	public String fileCreated;
	
	public Integer msl_surat_pdf;
	public Integer mpr_surat_pdf;

	public Double mpr_tax;                        
	public Double mpr_insurance;                  
	public Double mpr_debet;                      
	public Integer mpr_print;                     
	public Date mpr_print_date;                   
	public String mpr_print_info;                 
	public Date mpr_input_date;                   
	public Integer mpr_flag_comm;                 
	public Integer mpr_bayar_prm;                 
	public Date mpr_tgl_confirm;                  
	public Integer mpr_hari_lebih;                
	public Double mpr_rate_lebih;                 
	public Date mpr_tgl_prod;                     
	public Integer mpr_flag_cb;   
	public Integer msl_employee;
	
	//MANTA (14/5/2013) - Req Deddy P untuk insert ke MST_SLINK
	public Date msl_next_rep;

	public Integer getMsl_employee() {
		return msl_employee;
	}

	public void setMsl_employee(Integer mslEmployee) {
		msl_employee = mslEmployee;
	}

	public Date begdateTerbaru;
	
	public Integer flag_special;
	
	public Integer getFlag_special() {
		return flag_special;
	}

	public void setFlag_special(Integer flag_special) {
		this.flag_special = flag_special;
	}

	public Date getBegdateTerbaru() {
		return begdateTerbaru;
	}

	public void setBegdateTerbaru(Date begdateTerbaru) {
		this.begdateTerbaru = begdateTerbaru;
	}

	public Integer getMpr_bayar_prm() {
		return mpr_bayar_prm;
	}

	public void setMpr_bayar_prm(Integer mpr_bayar_prm) {
		this.mpr_bayar_prm = mpr_bayar_prm;
	}

	public Double getMpr_debet() {
		return mpr_debet;
	}

	public void setMpr_debet(Double mpr_debet) {
		this.mpr_debet = mpr_debet;
	}

	public Integer getMpr_flag_cb() {
		return mpr_flag_cb;
	}

	public void setMpr_flag_cb(Integer mpr_flag_cb) {
		this.mpr_flag_cb = mpr_flag_cb;
	}

	public Integer getMpr_flag_comm() {
		return mpr_flag_comm;
	}

	public void setMpr_flag_comm(Integer mpr_flag_comm) {
		this.mpr_flag_comm = mpr_flag_comm;
	}

	public Integer getMpr_hari_lebih() {
		return mpr_hari_lebih;
	}

	public void setMpr_hari_lebih(Integer mpr_hari_lebih) {
		this.mpr_hari_lebih = mpr_hari_lebih;
	}

	public Date getMpr_input_date() {
		return mpr_input_date;
	}

	public void setMpr_input_date(Date mpr_input_date) {
		this.mpr_input_date = mpr_input_date;
	}

	public Double getMpr_insurance() {
		return mpr_insurance;
	}

	public void setMpr_insurance(Double mpr_insurance) {
		this.mpr_insurance = mpr_insurance;
	}

	public Integer getMpr_print() {
		return mpr_print;
	}

	public void setMpr_print(Integer mpr_print) {
		this.mpr_print = mpr_print;
	}

	public Date getMpr_print_date() {
		return mpr_print_date;
	}

	public void setMpr_print_date(Date mpr_print_date) {
		this.mpr_print_date = mpr_print_date;
	}

	public String getMpr_print_info() {
		return mpr_print_info;
	}

	public void setMpr_print_info(String mpr_print_info) {
		this.mpr_print_info = mpr_print_info;
	}

	public Double getMpr_rate_lebih() {
		return mpr_rate_lebih;
	}

	public void setMpr_rate_lebih(Double mpr_rate_lebih) {
		this.mpr_rate_lebih = mpr_rate_lebih;
	}

	public Double getMpr_tax() {
		return mpr_tax;
	}

	public void setMpr_tax(Double mpr_tax) {
		this.mpr_tax = mpr_tax;
	}

	public Date getMpr_tgl_confirm() {
		return mpr_tgl_confirm;
	}

	public void setMpr_tgl_confirm(Date mpr_tgl_confirm) {
		this.mpr_tgl_confirm = mpr_tgl_confirm;
	}

	public Date getMpr_tgl_prod() {
		return mpr_tgl_prod;
	}

	public void setMpr_tgl_prod(Date mpr_tgl_prod) {
		this.mpr_tgl_prod = mpr_tgl_prod;
	}

	//default constructor
	public Powersave() {
		flag_bulanan = 0;
	}
	
	public String getNo_trx() {
		return no_trx;
	}

	public void setNo_trx(String no_trx) {
		this.no_trx = no_trx;
	}

	public Integer getFlag_bulanan() {
		return flag_bulanan;
	}
	public void setFlag_bulanan(Integer flag_bulanan) {
		this.flag_bulanan = flag_bulanan;
	}
	public String getFileCreated() {
		return fileCreated;
	}
	public void setFileCreated(String fileCreated) {
		this.fileCreated = fileCreated;
	}
	public String getFileDirectory() {
		return fileDirectory;
	}
	public void setFileDirectory(String fileDirectory) {
		this.fileDirectory = fileDirectory;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Date getMste_beg_date() {
		return mste_beg_date;
	}
	public void setMste_beg_date(Date mste_beg_date) {
		this.mste_beg_date = mste_beg_date;
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
	public Date getMsl_letter_date() {
		return msl_letter_date;
	}
	public String getMspo_policy_no_format() {
		return mspo_policy_no_format;
	}
	public void setMspo_policy_no_format(String mspo_policy_no_format) {
		this.mspo_policy_no_format = mspo_policy_no_format;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}
	public String getLus_login_name() {
		return lus_login_name;
	}
	public void setLus_login_name(String lus_login_name) {
		this.lus_login_name = lus_login_name;
	}
	public void setMsl_letter_date(Date msl_letter_date) {
		this.msl_letter_date = msl_letter_date;
	}
	public Double getMps_prm_interest_tu() {
		return mps_prm_interest_tu;
	}
	public void setMps_prm_interest_tu(Double mps_prm_interest_tu) {
		this.mps_prm_interest_tu = mps_prm_interest_tu;
	}
	public Date getMsl_tgl_nab_tu() {
		return msl_tgl_nab_tu;
	}
	public Date getMsl_paid_date() {
		return msl_paid_date;
	}
	public void setMsl_paid_date(Date msl_paid_date) {
		this.msl_paid_date = msl_paid_date;
	}
	public void setMsl_tgl_nab_tu(Date msl_tgl_nab_tu) {
		this.msl_tgl_nab_tu = msl_tgl_nab_tu;
	}
	public Double getMsl_nab_tu() {
		return msl_nab_tu;
	}
	public void setMsl_nab_tu(Double msl_nab_tu) {
		this.msl_nab_tu = msl_nab_tu;
	}
	public Double getMsl_unit_tu() {
		return msl_unit_tu;
	}
	public void setMsl_unit_tu(Double msl_unit_tu) {
		this.msl_unit_tu = msl_unit_tu;
	}
	public Double getMsl_nilai_polis() {
		return msl_nilai_polis;
	}
	public void setMsl_nilai_polis(Double msl_nilai_polis) {
		this.msl_nilai_polis = msl_nilai_polis;
	}
	public Date getMsl_trans_date() {
		return msl_trans_date;
	}
	public void setMsl_trans_date(Date msl_trans_date) {
		this.msl_trans_date = msl_trans_date;
	}
	public Integer getMsl_flag_print() {
		return msl_flag_print;
	}
	public void setMsl_flag_print(Integer msl_flag_print) {
		this.msl_flag_print = msl_flag_print;
	}
	public Date getMsl_print_date() {
		return msl_print_date;
	}
	public void setMsl_print_date(Date msl_print_date) {
		this.msl_print_date = msl_print_date;
	}
	public Integer getMsl_jn_rumus() {
		return msl_jn_rumus;
	}
	public void setMsl_jn_rumus(Integer msl_jn_rumus) {
		this.msl_jn_rumus = msl_jn_rumus;
	}
	public Integer getMsl_flag_comm() {
		return msl_flag_comm;
	}
	public void setMsl_flag_comm(Integer msl_flag_comm) {
		this.msl_flag_comm = msl_flag_comm;
	}
	public Integer getMsl_print() {
		return msl_print;
	}
	public void setMsl_print(Integer msl_print) {
		this.msl_print = msl_print;
	}
	public String getNo_reg_ref() {
		return no_reg_ref;
	}
	public void setNo_reg_ref(String no_reg_ref) {
		this.no_reg_ref = no_reg_ref;
	}
	public Integer getMsl_bayar_tarik() {
		return msl_bayar_tarik;
	}
	public void setMsl_bayar_tarik(Integer msl_bayar_tarik) {
		this.msl_bayar_tarik = msl_bayar_tarik;
	}
	public String getNo_reg() {
		return no_reg;
	}
	public void setNo_reg(String no_reg) {
		this.no_reg = no_reg;
	}
	public String getMsl_desc() {
		return msl_desc;
	}
	public void setMsl_desc(String msl_desc) {
		this.msl_desc = msl_desc;
	}
	public Double getFee_based_income() {
		return fee_based_income;
	}
	public void setFee_based_income(Double fee_based_income) {
		this.fee_based_income = fee_based_income;
	}
	public Integer getMsl_aktif() {
		return msl_aktif;
	}
	public void setMsl_aktif(Integer msl_aktif) {
		this.msl_aktif = msl_aktif;
	}
	public Double getMsl_saldo_unit() {
		return msl_saldo_unit;
	}
	public void setMsl_saldo_unit(Double msl_saldo_unit) {
		this.msl_saldo_unit = msl_saldo_unit;
	}
	public Double getMsl_tarik_pertama() {
		return msl_tarik_pertama;
	}
	public void setMsl_tarik_pertama(Double msl_tarik_pertama) {
		this.msl_tarik_pertama = msl_tarik_pertama;
	}
	public Integer getTarik_bunga() {
		return tarik_bunga;
	}
	public void setTarik_bunga(Integer tarik_bunga) {
		this.tarik_bunga = tarik_bunga;
	}
	public Date getEnddate_topup() {
		return enddate_topup;
	}
	public void setEnddate_topup(Date enddate_topup) {
		this.enddate_topup = enddate_topup;
	}
	public Date getBegdate_topup() {
		return begdate_topup;
	}
	public void setBegdate_topup(Date begdate_topup) {
		this.begdate_topup = begdate_topup;
	}
	public Integer getMsl_premi_ke() {
		return msl_premi_ke;
	}
	public void setMsl_premi_ke(Integer msl_premi_ke) {
		this.msl_premi_ke = msl_premi_ke;
	}
	public Integer getMsl_tahun_ke() {
		return msl_tahun_ke;
	}
	public void setMsl_tahun_ke(Integer msl_tahun_ke) {
		this.msl_tahun_ke = msl_tahun_ke;
	}
	public Integer getFlag_rate() {
		return flag_rate;
	}
	public void setFlag_rate(Integer flag_rate) {
		this.flag_rate = flag_rate;
	}
	public Double getMsl_bp_pt() {
		return msl_bp_pt;
	}
	public void setMsl_bp_pt(Double msl_bp_pt) {
		this.msl_bp_pt = msl_bp_pt;
	}
	public Date getMsl_confirm_date() {
		return msl_confirm_date;
	}
	public void setMsl_confirm_date(Date msl_confirm_date) {
		this.msl_confirm_date = msl_confirm_date;
	}
	public Date getMsl_input_date() {
		return msl_input_date;
	}
	public void setMsl_input_date(Date msl_input_date) {
		this.msl_input_date = msl_input_date;
	}
	public String getMsl_note() {
		return msl_note;
	}
	public void setMsl_note(String msl_note) {
		this.msl_note = msl_note;
	}
	public Date getMsl_prod_date() {
		return msl_prod_date;
	}
	public void setMsl_prod_date(Date msl_prod_date) {
		this.msl_prod_date = msl_prod_date;
	}
	public Date getMsl_proses_date() {
		return msl_proses_date;
	}
	public void setMsl_proses_date(Date msl_proses_date) {
		this.msl_proses_date = msl_proses_date;
	}
	public Double getMsl_tax() {
		return msl_tax;
	}
	public void setMsl_tax(Double msl_tax) {
		this.msl_tax = msl_tax;
	}
	public Double getMsl_unit() {
		return msl_unit;
	}
	public void setMsl_unit(Double msl_unit) {
		this.msl_unit = msl_unit;
	}
	public Integer getFlag_powersave() {
		return flag_powersave;
	}
	public void setFlag_powersave(Integer flag_powersave) {
		this.flag_powersave = flag_powersave;
	}
	public Integer getMpr_hari() {
		return mpr_hari;
	}
	public void setMpr_hari(Integer mpr_hari) {
		this.mpr_hari = mpr_hari;
	}
	public Integer getMpr_jns_rumus() {
		return mpr_jns_rumus;
	}
	public void setMpr_jns_rumus(Integer mpr_jns_rumus) {
		this.mpr_jns_rumus = mpr_jns_rumus;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public Double getMpr_deposit() {
		return mpr_deposit;
	}
	public void setMpr_deposit(Double mpr_deposit) {
		this.mpr_deposit = mpr_deposit;
	}
	public Double getMpr_interest() {
		return mpr_interest;
	}
	public void setMpr_interest(Double mpr_interest) {
		this.mpr_interest = mpr_interest;
	}
	public String getMpr_jangka_invest() {
		return mpr_jangka_invest;
	}
	public void setMpr_jangka_invest(String mpr_jangka_invest) {
		this.mpr_jangka_invest = mpr_jangka_invest;
	}
	public Integer getMpr_jns_ro() {
		return mpr_jns_ro;
	}
	public void setMpr_jns_ro(Integer mpr_jns_ro) {
		this.mpr_jns_ro = mpr_jns_ro;
	}
	public Date getMpr_mature_date() {
		return mpr_mature_date;
	}
	public void setMpr_mature_date(Date mpr_mature_date) {
		this.mpr_mature_date = mpr_mature_date;
	}
	public Integer getMpr_nett_tax() {
		return mpr_nett_tax;
	}
	public void setMpr_nett_tax(Integer mpr_nett_tax) {
		this.mpr_nett_tax = mpr_nett_tax;
	}
	public String getMpr_note() {
		return mpr_note;
	}
	public void setMpr_note(String mpr_note) {
		this.mpr_note = mpr_note;
	}
	public Double getMpr_rate() {
		return mpr_rate;
	}
	public void setMpr_rate(Double mpr_rate) {
		this.mpr_rate = mpr_rate;
	}
	public Date getMpr_tgl_rate() {
		return mpr_tgl_rate;
	}
	public void setMpr_tgl_rate(Date mpr_tgl_rate) {
		this.mpr_tgl_rate = mpr_tgl_rate;
	}
	public Date getMps_batas_date() {
		return mps_batas_date;
	}
	public void setMps_batas_date(Date mps_batas_date) {
		this.mps_batas_date = mps_batas_date;
	}
	public Date getMps_deposit_date() {
		return mps_deposit_date;
	}
	public void setMps_deposit_date(Date mps_deposit_date) {
		this.mps_deposit_date = mps_deposit_date;
	}
	public Integer getMps_employee() {
		return mps_employee;
	}
	public void setMps_employee(Integer mps_employee) {
		this.mps_employee = mps_employee;
	}
	public Date getMps_input_date() {
		return mps_input_date;
	}
	public void setMps_input_date(Date mps_input_date) {
		this.mps_input_date = mps_input_date;
	}
	public String getMps_jangka_inv() {
		return mps_jangka_inv;
	}
	public void setMps_jangka_inv(String mps_jangka_inv) {
		this.mps_jangka_inv = mps_jangka_inv;
	}
	public Integer getMps_jenis_plan() {
		return mps_jenis_plan;
	}
	public void setMps_jenis_plan(Integer mps_jenis_plan) {
		this.mps_jenis_plan = mps_jenis_plan;
	}
	public Integer getMps_kode() {
		return mps_kode;
	}
	public void setMps_kode(Integer mps_kode) {
		this.mps_kode = mps_kode;
	}
	public Double getMps_prm_debet() {
		return mps_prm_debet;
	}
	public void setMps_prm_debet(Double mps_prm_debet) {
		this.mps_prm_debet = mps_prm_debet;
	}
	public Double getMps_prm_deposit() {
		return mps_prm_deposit;
	}
	public void setMps_prm_deposit(Double mps_prm_deposit) {
		this.mps_prm_deposit = mps_prm_deposit;
	}
	public Double getMps_prm_insurance() {
		return mps_prm_insurance;
	}
	public void setMps_prm_insurance(Double mps_prm_insurance) {
		this.mps_prm_insurance = mps_prm_insurance;
	}
	public Double getMps_prm_interest() {
		return mps_prm_interest;
	}
	public void setMps_prm_interest(Double mps_prm_interest) {
		this.mps_prm_interest = mps_prm_interest;
	}
	public Double getMps_rate() {
		return mps_rate;
	}
	public void setMps_rate(Double mps_rate) {
		this.mps_rate = mps_rate;
	}
	public Date getMps_rate_date() {
		return mps_rate_date;
	}
	public void setMps_rate_date(Date mps_rate_date) {
		this.mps_rate_date = mps_rate_date;
	}
	public Integer getMps_roll_over() {
		return mps_roll_over;
	}
	public void setMps_roll_over(Integer mps_roll_over) {
		this.mps_roll_over = mps_roll_over;
	}
	public Double getMps_tax() {
		return mps_tax;
	}
	public void setMps_tax(Double mps_tax) {
		this.mps_tax = mps_tax;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getLji_id() {
		return lji_id;
	}
	public void setLji_id(String lji_id) {
		this.lji_id = lji_id;
	}
	public Integer getMsl_bayar_bp() {
		return msl_bayar_bp;
	}
	public void setMsl_bayar_bp(Integer msl_bayar_bp) {
		this.msl_bayar_bp = msl_bayar_bp;
	}
	public Integer getMsl_bayar_bunga() {
		return msl_bayar_bunga;
	}
	public void setMsl_bayar_bunga(Integer msl_bayar_bunga) {
		this.msl_bayar_bunga = msl_bayar_bunga;
	}
	public Date getMsl_bdate() {
		return msl_bdate;
	}
	public void setMsl_bdate(Date msl_bdate) {
		this.msl_bdate = msl_bdate;
	}
	public Double getMsl_bp() {
		return msl_bp;
	}
	public void setMsl_bp(Double msl_bp) {
		this.msl_bp = msl_bp;
	}
	public Double getMsl_bp_rate() {
		return msl_bp_rate;
	}
	public void setMsl_bp_rate(Double msl_bp_rate) {
		this.msl_bp_rate = msl_bp_rate;
	}
	public Double getMsl_bunga() {
		return msl_bunga;
	}
	public void setMsl_bunga(Double msl_bunga) {
		this.msl_bunga = msl_bunga;
	}
	public Date getMsl_edate() {
		return msl_edate;
	}
	public void setMsl_edate(Date msl_edate) {
		this.msl_edate = msl_edate;
	}
	public Integer getMsl_hari() {
		return msl_hari;
	}
	public void setMsl_hari(Integer msl_hari) {
		this.msl_hari = msl_hari;
	}
	public Integer getMsl_kode() {
		return msl_kode;
	}
	public void setMsl_kode(Integer msl_kode) {
		this.msl_kode = msl_kode;
	}
	public Integer getMsl_mgi() {
		return msl_mgi;
	}
	public void setMsl_mgi(Integer msl_mgi) {
		this.msl_mgi = msl_mgi;
	}
	public Double getMsl_nab() {
		return msl_nab;
	}
	public void setMsl_nab(Double msl_nab) {
		this.msl_nab = msl_nab;
	}
	public Double getMsl_nab_bp() {
		return msl_nab_bp;
	}
	public void setMsl_nab_bp(Double msl_nab_bp) {
		this.msl_nab_bp = msl_nab_bp;
	}
	public Date getMsl_next_date() {
		return msl_next_date;
	}
	public void setMsl_next_date(Date msl_next_date) {
		this.msl_next_date = msl_next_date;
	}
	public Integer getMsl_no() {
		return msl_no;
	}
	public void setMsl_no(Integer msl_no) {
		this.msl_no = msl_no;
	}
	public Integer getMsl_posisi() {
		return msl_posisi;
	}
	public void setMsl_posisi(Integer msl_posisi) {
		this.msl_posisi = msl_posisi;
	}
	public Double getMsl_premi() {
		return msl_premi;
	}
	public void setMsl_premi(Double msl_premi) {
		this.msl_premi = msl_premi;
	}
	public Double getMsl_rate() {
		return msl_rate;
	}
	public void setMsl_rate(Double msl_rate) {
		this.msl_rate = msl_rate;
	}
	public Integer getMsl_ro() {
		return msl_ro;
	}
	public void setMsl_ro(Integer msl_ro) {
		this.msl_ro = msl_ro;
	}
	public Date getMsl_tgl_nab() {
		return msl_tgl_nab;
	}
	public void setMsl_tgl_nab(Date msl_tgl_nab) {
		this.msl_tgl_nab = msl_tgl_nab;
	}
	public Date getMsl_tgl_nab_bp() {
		return msl_tgl_nab_bp;
	}
	public void setMsl_tgl_nab_bp(Date msl_tgl_nab_bp) {
		this.msl_tgl_nab_bp = msl_tgl_nab_bp;
	}
	public Double getMsl_up() {
		return msl_up;
	}
	public void setMsl_up(Double msl_up) {
		this.msl_up = msl_up;
	}
	public Date getEnddate_topup1() {
		return enddate_topup1;
	}
	public void setEnddate_topup1(Date enddate_topup1) {
		this.enddate_topup1 = enddate_topup1;
	}
	public Integer getMsl_flag_up() {
		return msl_flag_up;
	}
	public void setMsl_flag_up(Integer msl_flag_up) {
		this.msl_flag_up = msl_flag_up;
	}
	public Integer getMsl_tu_ke() {
		return msl_tu_ke;
	}
	public void setMsl_tu_ke(Integer msl_tu_ke) {
		this.msl_tu_ke = msl_tu_ke;
	}
	public Date getMps_end_period() {
		return mps_end_period;
	}
	public void setMps_end_period(Date mps_end_period) {
		this.mps_end_period = mps_end_period;
	}
	public Integer getMpr_breakable() {
		return mpr_breakable;
	}
	public void setMpr_breakable(Integer mpr_breakable) {
		this.mpr_breakable = mpr_breakable;
	}
	public Integer getMpr_cara_bayar_rider() {
		return mpr_cara_bayar_rider;
	}
	public void setMpr_cara_bayar_rider(Integer mpr_cara_bayar_rider) {
		this.mpr_cara_bayar_rider = mpr_cara_bayar_rider;
	}
	public Double getMpr_rider_total() {
		return mpr_rider_total;
	}
	public void setMpr_rider_total(Double mpr_rider_total) {
		this.mpr_rider_total = mpr_rider_total;
	}
	public Integer getFlag_rider() {
		return flag_rider;
	}
	public void setFlag_rider(Integer flag_rider) {
		this.flag_rider = flag_rider;
	}
	public Integer getMpr_surat_pdf() {
		return mpr_surat_pdf;
	}
	public void setMpr_surat_pdf(Integer mpr_surat_pdf) {
		this.mpr_surat_pdf = mpr_surat_pdf;
	}
	public Integer getMsl_surat_pdf() {
		return msl_surat_pdf;
	}
	public void setMsl_surat_pdf(Integer msl_surat_pdf) {
		this.msl_surat_pdf = msl_surat_pdf;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public Integer getMsbi_premi_ke() {
		return msbi_premi_ke;
	}
	public void setMsbi_premi_ke(Integer msbi_premi_ke) {
		this.msbi_premi_ke = msbi_premi_ke;
	}
	public Integer getMsbi_tahun_ke() {
		return msbi_tahun_ke;
	}
	public void setMsbi_tahun_ke(Integer msbi_tahun_ke) {
		this.msbi_tahun_ke = msbi_tahun_ke;
	}

	public String getMsl_spaj_lama() {
		return msl_spaj_lama;
	}

	public void setMsl_spaj_lama(String msl_spaj_lama) {
		this.msl_spaj_lama = msl_spaj_lama;
	}

	public Integer getMsl_proses_bsm() {
		return msl_proses_bsm;
	}

	public void setMsl_proses_bsm(Integer msl_proses_bsm) {
		this.msl_proses_bsm = msl_proses_bsm;
	}

	public String getMsen_endors_no() {
		return msen_endors_no;
	}

	public void setMsen_endors_no(String msen_endors_no) {
		this.msen_endors_no = msen_endors_no;
	} 
	
	public Double getMspr_premium() {
		return mspr_premium;
	}
	
	public void setMspr_premium(Double mspr_premium) {
		this.mspr_premium = mspr_premium;
	}
	
	public Date getMsl_next_rep() {
		return msl_next_rep;
	}

	public void setMsl_next_rep(Date msl_next_rep) {
		this.msl_next_rep = msl_next_rep;
	}
}
