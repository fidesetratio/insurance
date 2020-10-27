package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Hcp implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Double mspr_tsi;                       
	private Double mspr_premium;                   
	private Integer mspr_ins_period;               
	private Date mspr_beg_date;                    
	private Date mspr_end_date;                    
	private Date mspr_end_pay;                     
	private Integer mspr_class;                    
	private Integer mspr_unit;                     
	private String lsdbs_name;                     
	private Double mspr_extra;                     
	private Integer lsdbs_number;                  
	private Integer lsbs_id;                       
	private Integer mspr_tt;                       
	private Double mspr_rate;                      
	private Integer mste_insured_no;               
	private String lku_id;                         
	private Integer mspr_persen;                   
	private Double mspr_tsi_pa_a;                  
	private Double mspr_tsi_pa_b;                  
	private Double mspr_tsi_pa_c;                  
	private Double mspr_tsi_pa_d;                  
	private Double mspr_tsi_pa_m;                  
	private String plan_rider;                     
	private String reg_spaj;                       
	private Integer no_urut;                       
	private Integer lsre_id;                       
	private String nama;                           
	private Date tgl_lahir;                        
	private Integer kelamin;                       
	private Integer umur;                          
	private Double premi;
	private Double discount;
	private Integer lspc_no;
	private Integer flag_jenis_peserta;
	private String pengecualian;
	private Date tanggal_lahir;
	
	public String getPengecualian() {
		return pengecualian;
	}
	public void setPengecualian(String pengecualian) {
		this.pengecualian = pengecualian;
	}
	public Integer getLspc_no() {
		return lspc_no;
	}
	public void setLspc_no(Integer lspc_no) {
		this.lspc_no = lspc_no;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Integer getKelamin() {
		return kelamin;
	}
	public void setKelamin(Integer kelamin) {
		this.kelamin = kelamin;
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
	public Integer getLsre_id() {
		return lsre_id;
	}
	public void setLsre_id(Integer lsre_id) {
		this.lsre_id = lsre_id;
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
	public Date getMspr_end_pay() {
		return mspr_end_pay;
	}
	public void setMspr_end_pay(Date mspr_end_pay) {
		this.mspr_end_pay = mspr_end_pay;
	}
	public Double getMspr_extra() {
		return mspr_extra;
	}
	public void setMspr_extra(Double mspr_extra) {
		this.mspr_extra = mspr_extra;
	}
	public Integer getMspr_ins_period() {
		return mspr_ins_period;
	}
	public void setMspr_ins_period(Integer mspr_ins_period) {
		this.mspr_ins_period = mspr_ins_period;
	}
	public Integer getMspr_persen() {
		return mspr_persen;
	}
	public void setMspr_persen(Integer mspr_persen) {
		this.mspr_persen = mspr_persen;
	}
	public Double getMspr_premium() {
		return mspr_premium;
	}
	public void setMspr_premium(Double mspr_premium) {
		this.mspr_premium = mspr_premium;
	}
	public Double getMspr_rate() {
		return mspr_rate;
	}
	public void setMspr_rate(Double mspr_rate) {
		this.mspr_rate = mspr_rate;
	}
	public Double getMspr_tsi() {
		return mspr_tsi;
	}
	public void setMspr_tsi(Double mspr_tsi) {
		this.mspr_tsi = mspr_tsi;
	}
	public Double getMspr_tsi_pa_a() {
		return mspr_tsi_pa_a;
	}
	public void setMspr_tsi_pa_a(Double mspr_tsi_pa_a) {
		this.mspr_tsi_pa_a = mspr_tsi_pa_a;
	}
	public Double getMspr_tsi_pa_b() {
		return mspr_tsi_pa_b;
	}
	public void setMspr_tsi_pa_b(Double mspr_tsi_pa_b) {
		this.mspr_tsi_pa_b = mspr_tsi_pa_b;
	}
	public Double getMspr_tsi_pa_c() {
		return mspr_tsi_pa_c;
	}
	public void setMspr_tsi_pa_c(Double mspr_tsi_pa_c) {
		this.mspr_tsi_pa_c = mspr_tsi_pa_c;
	}
	public Double getMspr_tsi_pa_d() {
		return mspr_tsi_pa_d;
	}
	public void setMspr_tsi_pa_d(Double mspr_tsi_pa_d) {
		this.mspr_tsi_pa_d = mspr_tsi_pa_d;
	}
	public Double getMspr_tsi_pa_m() {
		return mspr_tsi_pa_m;
	}
	public void setMspr_tsi_pa_m(Double mspr_tsi_pa_m) {
		this.mspr_tsi_pa_m = mspr_tsi_pa_m;
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
	public Integer getMste_insured_no() {
		return mste_insured_no;
	}
	public void setMste_insured_no(Integer mste_insured_no) {
		this.mste_insured_no = mste_insured_no;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public Integer getNo_urut() {
		return no_urut;
	}
	public void setNo_urut(Integer no_urut) {
		this.no_urut = no_urut;
	}
	public String getPlan_rider() {
		return plan_rider;
	}
	public void setPlan_rider(String plan_rider) {
		this.plan_rider = plan_rider;
	}
	public Double getPremi() {
		return premi;
	}
	public void setPremi(Double premi) {
		this.premi = premi;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Date getTgl_lahir() {
		return tgl_lahir;
	}
	public void setTgl_lahir(Date tgl_lahir) {
		this.tgl_lahir = tgl_lahir;
	}
	public Integer getUmur() {
		return umur;
	}
	public void setUmur(Integer umur) {
		this.umur = umur;
	}
	public Integer getFlag_jenis_peserta(){
		return flag_jenis_peserta;
	}
	public void setFlag_jenis_peserta(Integer flag_jenis_peserta) {
		this.flag_jenis_peserta = flag_jenis_peserta;
	}
	
	public Date getTanggal_lahir(){
		return tanggal_lahir;
	}
	public void setTanggal_lahir(Date tanggal_lahir){
		this.tanggal_lahir = tanggal_lahir;
	}
	
}
