package com.ekalife.elions.model.reas;

import java.io.Serializable;
import java.util.Date;

public class DataRiderLink implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mspo_policy_no;
	private String nama_pemegang;
	private String nama_tertanggung;
	private Integer sex_holder;
	private Integer sex_insured;
	private Date birth_date_hld;
	private Date birth_date_ins;
	private Integer age_hld;
	private Integer age_ins;
	private Integer lscb_id;
	//rider
	private Double tsi;
	private Double retensi;
	private Double sar;
	private Double reas;
	
	////
	private Integer lsbs_id;                       
	private Integer mspr_ins_period;
	private Integer mspo_pay_period;
	private Integer mste_insured_no;
	private Integer mste_medical;
	private Integer lsdbs_number;                  
	private String lsdbs_name;                     
	private Date mspr_beg_date;                    
	private Date mspr_end_date;                    
	private String reg_spaj;                       
	private Integer pemegang;                      
	private String lku_id;                         
	private Integer nil_kurs;                      
	private Integer mste_reas;                     
	private String sts_aktif;                      
	private Double simultan_tr_rd;                 
	private Double tsi_tr_rd;                      
	private Double sar_tr_rd;                      
	private Double retensi_tr_rd;                  
	private Double reas_tr_rd;                     
	private Double simultan_life;                  
	private Double tsi_life;                       
	private Double sar_life;                       
	private Double retensi_life;                   
	private Double reas_life;                      
	private Double simultan_ssp;                   
	private Double tsi_ssp;                        
	private Double sar_ssp;                        
	private Double retensi_ssp;                    
	private Double reas_ssp;                       
	private Double simultan_pa_in;                 
	private Double tsi_pa_in;                      
	private Double sar_pa_in;                      
	private Double retensi_pa_in;                  
	private Double reas_pa_in;                     
	private Double simultan_pa_rd;                 
	private Double tsi_pa_rd;                      
	private Double sar_pa_rd;                      
	private Double retensi_pa_rd;                  
	private Double reas_pa_rd;                     
	private Double simultan_pk_in;                 
	private Double tsi_pk_in;                      
	private Double sar_pk_in;                      
	private Double retensi_pk_in;                  
	private Double reas_pk_in;                     
	private Double simultan_pk_rd;                 
	private Double tsi_pk_rd;                      
	private Double sar_pk_rd;                      
	private Double retensi_pk_rd;                  
	private Double reas_pk_rd;                     
	private Double simultan_ssh;                   
	private Double tsi_ssh;                        
	private Double sar_ssh;                        
	private Double retensi_ssh;                    
	private Double reas_ssh;                       
	private Double extra_mortality;                
	private Double simultan_cash;                  
	private Double tsi_cash;                       
	private Double sar_cash;                       
	private Double retensi_cash;                   
	private Double reas_cash;                      
	private Double simultan_tpd;                   
	private Double tsi_tpd;                        
	private Double sar_tpd;                        
	private Double retensi_tpd;                    
	private Double reas_tpd;                       
	private Integer tipe;                          
	private Double simultan_wpd;                   
	private Double tsi_wpd;                        
	private Double sar_wpd;                        
	private Double retensi_wpd;                    
	private Double reas_wpd;                       
	private String tipe_reas;
	public Integer getAge_hld() {
		return age_hld;
	}
	public void setAge_hld(Integer age_hld) {
		this.age_hld = age_hld;
	}
	public Integer getAge_ins() {
		return age_ins;
	}
	public void setAge_ins(Integer age_ins) {
		this.age_ins = age_ins;
	}
	public Date getBirth_date_hld() {
		return birth_date_hld;
	}
	public void setBirth_date_hld(Date birth_date_hld) {
		this.birth_date_hld = birth_date_hld;
	}
	public Date getBirth_date_ins() {
		return birth_date_ins;
	}
	public void setBirth_date_ins(Date birth_date_ins) {
		this.birth_date_ins = birth_date_ins;
	}
	public Double getExtra_mortality() {
		return extra_mortality;
	}
	public void setExtra_mortality(Double extra_mortality) {
		this.extra_mortality = extra_mortality;
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
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	public Integer getMspo_pay_period() {
		return mspo_pay_period;
	}
	public void setMspo_pay_period(Integer mspo_pay_period) {
		this.mspo_pay_period = mspo_pay_period;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public Date getMspr_beg_date() {
		return mspr_beg_date;
	}
	public void setMspr_beg_date(Date mspr_beg_date) {
		this.mspr_beg_date = mspr_beg_date;
	}
	public Date getMspr_end_date() {
		return mspr_end_date;
	}
	public void setMspr_end_date(Date mspr_end_date) {
		this.mspr_end_date = mspr_end_date;
	}
	public Integer getMspr_ins_period() {
		return mspr_ins_period;
	}
	public void setMspr_ins_period(Integer mspr_ins_period) {
		this.mspr_ins_period = mspr_ins_period;
	}
	public Integer getMste_medical() {
		return mste_medical;
	}
	public void setMste_medical(Integer mste_medical) {
		this.mste_medical = mste_medical;
	}
	public Integer getMste_reas() {
		return mste_reas;
	}
	public void setMste_reas(Integer mste_reas) {
		this.mste_reas = mste_reas;
	}
	public String getNama_pemegang() {
		return nama_pemegang;
	}
	public void setNama_pemegang(String nama_pemegang) {
		this.nama_pemegang = nama_pemegang;
	}
	public String getNama_tertanggung() {
		return nama_tertanggung;
	}
	public void setNama_tertanggung(String nama_tertanggung) {
		this.nama_tertanggung = nama_tertanggung;
	}
	public Integer getNil_kurs() {
		return nil_kurs;
	}
	public void setNil_kurs(Integer nil_kurs) {
		this.nil_kurs = nil_kurs;
	}
	public Integer getPemegang() {
		return pemegang;
	}
	public void setPemegang(Integer pemegang) {
		this.pemegang = pemegang;
	}
	public Double getReas_cash() {
		return reas_cash;
	}
	public void setReas_cash(Double reas_cash) {
		this.reas_cash = reas_cash;
	}
	public Double getReas_life() {
		return reas_life;
	}
	public void setReas_life(Double reas_life) {
		this.reas_life = reas_life;
	}
	public Double getReas_pa_in() {
		return reas_pa_in;
	}
	public void setReas_pa_in(Double reas_pa_in) {
		this.reas_pa_in = reas_pa_in;
	}
	public Double getReas_pa_rd() {
		return reas_pa_rd;
	}
	public void setReas_pa_rd(Double reas_pa_rd) {
		this.reas_pa_rd = reas_pa_rd;
	}
	public Double getReas_pk_in() {
		return reas_pk_in;
	}
	public void setReas_pk_in(Double reas_pk_in) {
		this.reas_pk_in = reas_pk_in;
	}
	public Double getReas_pk_rd() {
		return reas_pk_rd;
	}
	public void setReas_pk_rd(Double reas_pk_rd) {
		this.reas_pk_rd = reas_pk_rd;
	}
	public Double getReas_ssh() {
		return reas_ssh;
	}
	public void setReas_ssh(Double reas_ssh) {
		this.reas_ssh = reas_ssh;
	}
	public Double getReas_ssp() {
		return reas_ssp;
	}
	public void setReas_ssp(Double reas_ssp) {
		this.reas_ssp = reas_ssp;
	}
	public Double getReas_tpd() {
		return reas_tpd;
	}
	public void setReas_tpd(Double reas_tpd) {
		this.reas_tpd = reas_tpd;
	}
	public Double getReas_tr_rd() {
		return reas_tr_rd;
	}
	public void setReas_tr_rd(Double reas_tr_rd) {
		this.reas_tr_rd = reas_tr_rd;
	}
	public Double getReas_wpd() {
		return reas_wpd;
	}
	public void setReas_wpd(Double reas_wpd) {
		this.reas_wpd = reas_wpd;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Double getRetensi_cash() {
		return retensi_cash;
	}
	public void setRetensi_cash(Double retensi_cash) {
		this.retensi_cash = retensi_cash;
	}
	public Double getRetensi_life() {
		return retensi_life;
	}
	public void setRetensi_life(Double retensi_life) {
		this.retensi_life = retensi_life;
	}
	public Double getRetensi_pa_in() {
		return retensi_pa_in;
	}
	public void setRetensi_pa_in(Double retensi_pa_in) {
		this.retensi_pa_in = retensi_pa_in;
	}
	public Double getRetensi_pa_rd() {
		return retensi_pa_rd;
	}
	public void setRetensi_pa_rd(Double retensi_pa_rd) {
		this.retensi_pa_rd = retensi_pa_rd;
	}
	public Double getRetensi_pk_in() {
		return retensi_pk_in;
	}
	public void setRetensi_pk_in(Double retensi_pk_in) {
		this.retensi_pk_in = retensi_pk_in;
	}
	public Double getRetensi_pk_rd() {
		return retensi_pk_rd;
	}
	public void setRetensi_pk_rd(Double retensi_pk_rd) {
		this.retensi_pk_rd = retensi_pk_rd;
	}
	public Double getRetensi_ssh() {
		return retensi_ssh;
	}
	public void setRetensi_ssh(Double retensi_ssh) {
		this.retensi_ssh = retensi_ssh;
	}
	public Double getRetensi_ssp() {
		return retensi_ssp;
	}
	public void setRetensi_ssp(Double retensi_ssp) {
		this.retensi_ssp = retensi_ssp;
	}
	public Double getRetensi_tpd() {
		return retensi_tpd;
	}
	public void setRetensi_tpd(Double retensi_tpd) {
		this.retensi_tpd = retensi_tpd;
	}
	public Double getRetensi_tr_rd() {
		return retensi_tr_rd;
	}
	public void setRetensi_tr_rd(Double retensi_tr_rd) {
		this.retensi_tr_rd = retensi_tr_rd;
	}
	public Double getRetensi_wpd() {
		return retensi_wpd;
	}
	public void setRetensi_wpd(Double retensi_wpd) {
		this.retensi_wpd = retensi_wpd;
	}
	public Double getSar_cash() {
		return sar_cash;
	}
	public void setSar_cash(Double sar_cash) {
		this.sar_cash = sar_cash;
	}
	public Double getSar_life() {
		return sar_life;
	}
	public void setSar_life(Double sar_life) {
		this.sar_life = sar_life;
	}
	public Double getSar_pa_in() {
		return sar_pa_in;
	}
	public void setSar_pa_in(Double sar_pa_in) {
		this.sar_pa_in = sar_pa_in;
	}
	public Double getSar_pa_rd() {
		return sar_pa_rd;
	}
	public void setSar_pa_rd(Double sar_pa_rd) {
		this.sar_pa_rd = sar_pa_rd;
	}
	public Double getSar_pk_in() {
		return sar_pk_in;
	}
	public void setSar_pk_in(Double sar_pk_in) {
		this.sar_pk_in = sar_pk_in;
	}
	public Double getSar_pk_rd() {
		return sar_pk_rd;
	}
	public void setSar_pk_rd(Double sar_pk_rd) {
		this.sar_pk_rd = sar_pk_rd;
	}
	public Double getSar_ssh() {
		return sar_ssh;
	}
	public void setSar_ssh(Double sar_ssh) {
		this.sar_ssh = sar_ssh;
	}
	public Double getSar_ssp() {
		return sar_ssp;
	}
	public void setSar_ssp(Double sar_ssp) {
		this.sar_ssp = sar_ssp;
	}
	public Double getSar_tpd() {
		return sar_tpd;
	}
	public void setSar_tpd(Double sar_tpd) {
		this.sar_tpd = sar_tpd;
	}
	public Double getSar_tr_rd() {
		return sar_tr_rd;
	}
	public void setSar_tr_rd(Double sar_tr_rd) {
		this.sar_tr_rd = sar_tr_rd;
	}
	public Double getSar_wpd() {
		return sar_wpd;
	}
	public void setSar_wpd(Double sar_wpd) {
		this.sar_wpd = sar_wpd;
	}
	public Integer getSex_holder() {
		return sex_holder;
	}
	public void setSex_holder(Integer sex_holder) {
		this.sex_holder = sex_holder;
	}
	public Integer getSex_insured() {
		return sex_insured;
	}
	public void setSex_insured(Integer sex_insured) {
		this.sex_insured = sex_insured;
	}
	public Double getSimultan_cash() {
		return simultan_cash;
	}
	public void setSimultan_cash(Double simultan_cash) {
		this.simultan_cash = simultan_cash;
	}
	public Double getSimultan_life() {
		return simultan_life;
	}
	public void setSimultan_life(Double simultan_life) {
		this.simultan_life = simultan_life;
	}
	public Double getSimultan_pa_in() {
		return simultan_pa_in;
	}
	public void setSimultan_pa_in(Double simultan_pa_in) {
		this.simultan_pa_in = simultan_pa_in;
	}
	public Double getSimultan_pa_rd() {
		return simultan_pa_rd;
	}
	public void setSimultan_pa_rd(Double simultan_pa_rd) {
		this.simultan_pa_rd = simultan_pa_rd;
	}
	public Double getSimultan_pk_in() {
		return simultan_pk_in;
	}
	public void setSimultan_pk_in(Double simultan_pk_in) {
		this.simultan_pk_in = simultan_pk_in;
	}
	public Double getSimultan_pk_rd() {
		return simultan_pk_rd;
	}
	public void setSimultan_pk_rd(Double simultan_pk_rd) {
		this.simultan_pk_rd = simultan_pk_rd;
	}
	public Double getSimultan_ssh() {
		return simultan_ssh;
	}
	public void setSimultan_ssh(Double simultan_ssh) {
		this.simultan_ssh = simultan_ssh;
	}
	public Double getSimultan_ssp() {
		return simultan_ssp;
	}
	public void setSimultan_ssp(Double simultan_ssp) {
		this.simultan_ssp = simultan_ssp;
	}
	public Double getSimultan_tpd() {
		return simultan_tpd;
	}
	public void setSimultan_tpd(Double simultan_tpd) {
		this.simultan_tpd = simultan_tpd;
	}
	public Double getSimultan_tr_rd() {
		return simultan_tr_rd;
	}
	public void setSimultan_tr_rd(Double simultan_tr_rd) {
		this.simultan_tr_rd = simultan_tr_rd;
	}
	public Double getSimultan_wpd() {
		return simultan_wpd;
	}
	public void setSimultan_wpd(Double simultan_wpd) {
		this.simultan_wpd = simultan_wpd;
	}
	public String getSts_aktif() {
		return sts_aktif;
	}
	public void setSts_aktif(String sts_aktif) {
		this.sts_aktif = sts_aktif;
	}
	public Integer getTipe() {
		return tipe;
	}
	public void setTipe(Integer tipe) {
		this.tipe = tipe;
	}
	public String getTipe_reas() {
		return tipe_reas;
	}
	public void setTipe_reas(String tipe_reas) {
		this.tipe_reas = tipe_reas;
	}
	public Double getTsi_cash() {
		return tsi_cash;
	}
	public void setTsi_cash(Double tsi_cash) {
		this.tsi_cash = tsi_cash;
	}
	public Double getTsi_life() {
		return tsi_life;
	}
	public void setTsi_life(Double tsi_life) {
		this.tsi_life = tsi_life;
	}
	public Double getTsi_pa_in() {
		return tsi_pa_in;
	}
	public void setTsi_pa_in(Double tsi_pa_in) {
		this.tsi_pa_in = tsi_pa_in;
	}
	public Double getTsi_pa_rd() {
		return tsi_pa_rd;
	}
	public void setTsi_pa_rd(Double tsi_pa_rd) {
		this.tsi_pa_rd = tsi_pa_rd;
	}
	public Double getTsi_pk_in() {
		return tsi_pk_in;
	}
	public void setTsi_pk_in(Double tsi_pk_in) {
		this.tsi_pk_in = tsi_pk_in;
	}
	public Double getTsi_pk_rd() {
		return tsi_pk_rd;
	}
	public void setTsi_pk_rd(Double tsi_pk_rd) {
		this.tsi_pk_rd = tsi_pk_rd;
	}
	public Double getTsi_ssh() {
		return tsi_ssh;
	}
	public void setTsi_ssh(Double tsi_ssh) {
		this.tsi_ssh = tsi_ssh;
	}
	public Double getTsi_ssp() {
		return tsi_ssp;
	}
	public void setTsi_ssp(Double tsi_ssp) {
		this.tsi_ssp = tsi_ssp;
	}
	public Double getTsi_tpd() {
		return tsi_tpd;
	}
	public void setTsi_tpd(Double tsi_tpd) {
		this.tsi_tpd = tsi_tpd;
	}
	public Double getTsi_tr_rd() {
		return tsi_tr_rd;
	}
	public void setTsi_tr_rd(Double tsi_tr_rd) {
		this.tsi_tr_rd = tsi_tr_rd;
	}
	public Double getTsi_wpd() {
		return tsi_wpd;
	}
	public void setTsi_wpd(Double tsi_wpd) {
		this.tsi_wpd = tsi_wpd;
	}
	public Integer getLscb_id() {
		return lscb_id;
	}
	public void setLscb_id(Integer lscb_id) {
		this.lscb_id = lscb_id;
	}
	public Double getReas() {
		return reas;
	}
	public void setReas(Double reas) {
		this.reas = reas;
	}
	public Double getRetensi() {
		return retensi;
	}
	public void setRetensi(Double retensi) {
		this.retensi = retensi;
	}
	public Double getSar() {
		return sar;
	}
	public void setSar(Double sar) {
		this.sar = sar;
	}
	public Double getTsi() {
		return tsi;
	}
	public void setTsi(Double tsi) {
		this.tsi = tsi;
	}
	public Integer getMste_insured_no() {
		return mste_insured_no;
	}
	public void setMste_insured_no(Integer mste_insured_no) {
		this.mste_insured_no = mste_insured_no;
	}
	
}
