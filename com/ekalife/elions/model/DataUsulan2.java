package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class DataUsulan2 implements Serializable {

	/**
	 * 
	 */
		private static final long serialVersionUID = 1L;
		private String lsdbs_name;
		private Integer lsdbs_number;
		private Integer lsbs_id;
		private Integer lsdbs_lama_bayar;
		private String msap_address;
		private String msap_zip_code;
		private String msap_area_code1;
		private String msap_phone1;
		private String msap_area_code2;
		private String msap_phone2;
		private String msap_area_code3;
		private String msap_phone3;
		private String msap_area_code_fax1;
		private String msap_fax1;
		private String msap_area_code_fax2;
		private String msap_fax2;
		private String no_hp; 
		private String kota;
		private String no_hp2;
		private Integer mste_flag_cc;
		private Date tgl_recur;
		private Double mspr_tsi;
		private Double mspr_premium;
		private Integer mspr_class;
		private Integer mste_medical;
		private Date mspr_beg_date;
		private Date mspr_end_date;
		private String lku_id;
		private Integer lscb_id;
		private Integer mspo_pay_period;
		private Integer mspo_ins_period;
		
		public String getKota() {
			return kota;
		}
		public void setKota(String kota) {
			this.kota = kota;
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
		public Integer getLscb_id() {
			return lscb_id;
		}
		public void setLscb_id(Integer lscb_id) {
			this.lscb_id = lscb_id;
		}
		public Integer getLsdbs_lama_bayar() {
			return lsdbs_lama_bayar;
		}
		public void setLsdbs_lama_bayar(Integer lsdbs_lama_bayar) {
			this.lsdbs_lama_bayar = lsdbs_lama_bayar;
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
		public String getMsap_address() {
			return msap_address;
		}
		public void setMsap_address(String msap_address) {
			this.msap_address = msap_address;
		}
		public String getMsap_area_code_fax1() {
			return msap_area_code_fax1;
		}
		public void setMsap_area_code_fax1(String msap_area_code_fax1) {
			this.msap_area_code_fax1 = msap_area_code_fax1;
		}
		public String getMsap_area_code_fax2() {
			return msap_area_code_fax2;
		}
		public void setMsap_area_code_fax2(String msap_area_code_fax2) {
			this.msap_area_code_fax2 = msap_area_code_fax2;
		}
		public String getMsap_area_code1() {
			return msap_area_code1;
		}
		public void setMsap_area_code1(String msap_area_code1) {
			this.msap_area_code1 = msap_area_code1;
		}
		public String getMsap_area_code2() {
			return msap_area_code2;
		}
		public void setMsap_area_code2(String msap_area_code2) {
			this.msap_area_code2 = msap_area_code2;
		}
		public String getMsap_area_code3() {
			return msap_area_code3;
		}
		public void setMsap_area_code3(String msap_area_code3) {
			this.msap_area_code3 = msap_area_code3;
		}
		public String getMsap_fax1() {
			return msap_fax1;
		}
		public void setMsap_fax1(String msap_fax1) {
			this.msap_fax1 = msap_fax1;
		}
		public String getMsap_fax2() {
			return msap_fax2;
		}
		public void setMsap_fax2(String msap_fax2) {
			this.msap_fax2 = msap_fax2;
		}
		public String getMsap_phone1() {
			return msap_phone1;
		}
		public void setMsap_phone1(String msap_phone1) {
			this.msap_phone1 = msap_phone1;
		}
		public String getMsap_phone2() {
			return msap_phone2;
		}
		public void setMsap_phone2(String msap_phone2) {
			this.msap_phone2 = msap_phone2;
		}
		public String getMsap_phone3() {
			return msap_phone3;
		}
		public void setMsap_phone3(String msap_phone3) {
			this.msap_phone3 = msap_phone3;
		}
		public String getMsap_zip_code() {
			return msap_zip_code;
		}
		public void setMsap_zip_code(String msap_zip_code) {
			this.msap_zip_code = msap_zip_code;
		}
		public Integer getMspo_ins_period() {
			return mspo_ins_period;
		}
		public void setMspo_ins_period(Integer mspo_ins_period) {
			this.mspo_ins_period = mspo_ins_period;
		}
		public Integer getMspo_pay_period() {
			return mspo_pay_period;
		}
		public void setMspo_pay_period(Integer mspo_pay_period) {
			this.mspo_pay_period = mspo_pay_period;
		}
		public Date getMspr_beg_date() {
			return mspr_beg_date;
		}
		public void setMspr_beg_date(Date mspr_beg_date) {
			this.mspr_beg_date = mspr_beg_date;
		}
		public Integer getMspr_class() {
			return mspr_class;
		}
		public void setMspr_class(Integer mspr_class) {
			this.mspr_class = mspr_class;
		}
		public Date getMspr_end_date() {
			return mspr_end_date;
		}
		public void setMspr_end_date(Date mspr_end_date) {
			this.mspr_end_date = mspr_end_date;
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
		public Integer getMste_flag_cc() {
			return mste_flag_cc;
		}
		public void setMste_flag_cc(Integer mste_flag_cc) {
			this.mste_flag_cc = mste_flag_cc;
		}
		public Integer getMste_medical() {
			return mste_medical;
		}
		public void setMste_medical(Integer mste_medical) {
			this.mste_medical = mste_medical;
		}
		public String getNo_hp() {
			return no_hp;
		}
		public void setNo_hp(String no_hp) {
			this.no_hp = no_hp;
		}
		public String getNo_hp2() {
			return no_hp2;
		}
		public void setNo_hp2(String no_hp2) {
			this.no_hp2 = no_hp2;
		}
		public Date getTgl_recur() {
			return tgl_recur;
		}
		public void setTgl_recur(Date tgl_recur) {
			this.tgl_recur = tgl_recur;
		}
		
		
		
		
}
