package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class ReinsProd implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;
	private Integer mste_insured_no;
	private Integer	  lsbs_id ;
	private Integer	  lsdbs_number;
	private Integer	  msrpr_number ;
	private String	  lku_id ;
	private Double  msrpr_simultan ;
	private Double	  msrpr_tsi;
	private Double	  msrpr_resiko_awal;
	private Double	  msrpr_retensi ;
	private Double	  msrpr_tsi_reas;
	private Integer	  msrpr_pa_class ;
	private String	  msrpr_pa_risk ;
	private Double	  msrpr_extra   ;
	private Date	  msrpr_beg_date;
	private Date	  msrpr_end_date ;
	private Integer	  msrpr_pemegang ;
	private Integer	  msrpr_flag_premi;
	private Double	  msrpr_sar;
	private Double	  msrpr_premium ;
	private Double	  msrpr_comm ;
	private Integer	  msrpr_status_reas;
	private Integer	  msrpr_active   ;
	private Integer	  msrpr_convert;
	private Double	  msrpr_extra_mort;
	private Double	  msrpr_extra_hamil;
	private Double	  mrspr_extra_pa;
	private Double	  msrpr_extra_pa ;
	private Double	  msrpr_extra_risk;
	private Double	  msrpr_commision;
	private Double	  msrpr_prm_ext_mort;
	private Double	  msrpr_comm_ext_mort;
	private Double	  msrpr_prm_ext_risk;
	private Double	  msrpr_comm_ext_risk;
	private Integer	  msrpr_contract_year;
	private Integer	  msre_recapture ;
	private Date	  msre_tgl_recap;
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
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	public Double getMrspr_extra_pa() {
		return mrspr_extra_pa;
	}
	public void setMrspr_extra_pa(Double mrspr_extra_pa) {
		this.mrspr_extra_pa = mrspr_extra_pa;
	}
	public Integer getMsre_recapture() {
		return msre_recapture;
	}
	public void setMsre_recapture(Integer msre_recapture) {
		this.msre_recapture = msre_recapture;
	}
	public Date getMsre_tgl_recap() {
		return msre_tgl_recap;
	}
	public void setMsre_tgl_recap(Date msre_tgl_recap) {
		this.msre_tgl_recap = msre_tgl_recap;
	}
	public Integer getMsrpr_active() {
		return msrpr_active;
	}
	public void setMsrpr_active(Integer msrpr_active) {
		this.msrpr_active = msrpr_active;
	}
	public Date getMsrpr_beg_date() {
		return msrpr_beg_date;
	}
	public void setMsrpr_beg_date(Date msrpr_beg_date) {
		this.msrpr_beg_date = msrpr_beg_date;
	}
	public Double getMsrpr_comm() {
		return msrpr_comm;
	}
	public void setMsrpr_comm(Double msrpr_comm) {
		this.msrpr_comm = msrpr_comm;
	}
	public Double getMsrpr_comm_ext_mort() {
		return msrpr_comm_ext_mort;
	}
	public void setMsrpr_comm_ext_mort(Double msrpr_comm_ext_mort) {
		this.msrpr_comm_ext_mort = msrpr_comm_ext_mort;
	}
	public Double getMsrpr_comm_ext_risk() {
		return msrpr_comm_ext_risk;
	}
	public void setMsrpr_comm_ext_risk(Double msrpr_comm_ext_risk) {
		this.msrpr_comm_ext_risk = msrpr_comm_ext_risk;
	}
	public Double getMsrpr_commision() {
		return msrpr_commision;
	}
	public void setMsrpr_commision(Double msrpr_commision) {
		this.msrpr_commision = msrpr_commision;
	}
	public Integer getMsrpr_contract_year() {
		return msrpr_contract_year;
	}
	public void setMsrpr_contract_year(Integer msrpr_contract_year) {
		this.msrpr_contract_year = msrpr_contract_year;
	}
	public Integer getMsrpr_convert() {
		return msrpr_convert;
	}
	public void setMsrpr_convert(Integer msrpr_convert) {
		this.msrpr_convert = msrpr_convert;
	}
	public Date getMsrpr_end_date() {
		return msrpr_end_date;
	}
	public void setMsrpr_end_date(Date msrpr_end_date) {
		this.msrpr_end_date = msrpr_end_date;
	}
	public Double getMsrpr_extra() {
		return msrpr_extra;
	}
	public void setMsrpr_extra(Double msrpr_extra) {
		this.msrpr_extra = msrpr_extra;
	}
	public Double getMsrpr_extra_hamil() {
		return msrpr_extra_hamil;
	}
	public void setMsrpr_extra_hamil(Double msrpr_extra_hamil) {
		this.msrpr_extra_hamil = msrpr_extra_hamil;
	}
	public Double getMsrpr_extra_mort() {
		return msrpr_extra_mort;
	}
	public void setMsrpr_extra_mort(Double msrpr_extra_mort) {
		this.msrpr_extra_mort = msrpr_extra_mort;
	}
	public Double getMsrpr_extra_pa() {
		return msrpr_extra_pa;
	}
	public void setMsrpr_extra_pa(Double msrpr_extra_pa) {
		this.msrpr_extra_pa = msrpr_extra_pa;
	}
	public Double getMsrpr_extra_risk() {
		return msrpr_extra_risk;
	}
	public void setMsrpr_extra_risk(Double msrpr_extra_risk) {
		this.msrpr_extra_risk = msrpr_extra_risk;
	}
	public Integer getMsrpr_flag_premi() {
		return msrpr_flag_premi;
	}
	public void setMsrpr_flag_premi(Integer msrpr_flag_premi) {
		this.msrpr_flag_premi = msrpr_flag_premi;
	}
	public Integer getMsrpr_number() {
		return msrpr_number;
	}
	public void setMsrpr_number(Integer msrpr_number) {
		this.msrpr_number = msrpr_number;
	}
	public Integer getMsrpr_pa_class() {
		return msrpr_pa_class;
	}
	public void setMsrpr_pa_class(Integer msrpr_pa_class) {
		this.msrpr_pa_class = msrpr_pa_class;
	}
	public String getMsrpr_pa_risk() {
		return msrpr_pa_risk;
	}
	public void setMsrpr_pa_risk(String msrpr_pa_risk) {
		this.msrpr_pa_risk = msrpr_pa_risk;
	}
	public Integer getMsrpr_pemegang() {
		return msrpr_pemegang;
	}
	public void setMsrpr_pemegang(Integer msrpr_pemegang) {
		this.msrpr_pemegang = msrpr_pemegang;
	}
	public Double getMsrpr_premium() {
		return msrpr_premium;
	}
	public void setMsrpr_premium(Double msrpr_premium) {
		this.msrpr_premium = msrpr_premium;
	}
	public Double getMsrpr_prm_ext_mort() {
		return msrpr_prm_ext_mort;
	}
	public void setMsrpr_prm_ext_mort(Double msrpr_prm_ext_mort) {
		this.msrpr_prm_ext_mort = msrpr_prm_ext_mort;
	}
	public Double getMsrpr_prm_ext_risk() {
		return msrpr_prm_ext_risk;
	}
	public void setMsrpr_prm_ext_risk(Double msrpr_prm_ext_risk) {
		this.msrpr_prm_ext_risk = msrpr_prm_ext_risk;
	}
	public Double getMsrpr_resiko_awal() {
		return msrpr_resiko_awal;
	}
	public void setMsrpr_resiko_awal(Double msrpr_resiko_awal) {
		this.msrpr_resiko_awal = msrpr_resiko_awal;
	}
	public Double getMsrpr_retensi() {
		return msrpr_retensi;
	}
	public void setMsrpr_retensi(Double msrpr_retensi) {
		this.msrpr_retensi = msrpr_retensi;
	}
	public Double getMsrpr_sar() {
		return msrpr_sar;
	}
	public void setMsrpr_sar(Double msrpr_sar) {
		this.msrpr_sar = msrpr_sar;
	}
	public Double getMsrpr_simultan() {
		return msrpr_simultan;
	}
	public void setMsrpr_simultan(Double msrpr_simultan) {
		this.msrpr_simultan = msrpr_simultan;
	}
	public Integer getMsrpr_status_reas() {
		return msrpr_status_reas;
	}
	public void setMsrpr_status_reas(Integer msrpr_status_reas) {
		this.msrpr_status_reas = msrpr_status_reas;
	}
	public Double getMsrpr_tsi() {
		return msrpr_tsi;
	}
	public void setMsrpr_tsi(Double msrpr_tsi) {
		this.msrpr_tsi = msrpr_tsi;
	}
	public Double getMsrpr_tsi_reas() {
		return msrpr_tsi_reas;
	}
	public void setMsrpr_tsi_reas(Double msrpr_tsi_reas) {
		this.msrpr_tsi_reas = msrpr_tsi_reas;
	}
	public Integer getMste_insured_no() {
		return mste_insured_no;
	}
	public void setMste_insured_no(Integer mste_insured_no) {
		this.mste_insured_no = mste_insured_no;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
}
