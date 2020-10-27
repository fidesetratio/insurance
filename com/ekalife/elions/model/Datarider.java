package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Datarider implements Serializable {

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
	private Integer lsdbs_number;                  
	private Integer lsbs_id;                       
	private Integer mspr_tt;     
	private Integer mspr_tt1;
	private Double mspr_rate;                      
	private Integer mste_insured_no;               
	private String lku_id;                         
	private Integer mspr_persen;                   
	private Double mspr_tsi_pa_a;                  
	private Double mspr_tsi_pa_b;                  
	private Double mspr_tsi_pa_c;                  
	private Double mspr_tsi_pa_d;                  
	private Double mspr_tsi_pa_m;                  
	private Double mspr_extra;                  
	private String plan_rider;
	private String plan_rider1;
	private Integer flag_include;
	
	private Double mspr_tsi_include;                       
	private Double mspr_premium_include;                   
	private Integer mspr_ins_period_include;               
	private Date mspr_beg_date_include;                    
	private Date mspr_end_date_include;                    
	private Date mspr_end_pay_include;                     
	private Integer mspr_class_include;                    
	private Integer mspr_unit_include;                     
	private String lsdbs_name_include;                     
	private Integer lsdbs_number_include;                  
	private Integer lsbs_id_include;                       
	private Integer mspr_tt_include;                       
	private Double mspr_rate_include;                      
	private Integer mste_insured_no_include;               
	private String lku_id_include;                         
	private Integer mspr_persen_include;                   
	private Double mspr_tsi_pa_a_include;                  
	private Double mspr_tsi_pa_b_include;                  
	private Double mspr_tsi_pa_c_include;                  
	private Double mspr_tsi_pa_d_include;                  
	private Double mspr_tsi_pa_m_include;                  
	private String plan_rider_include;	
	private Double discount;
	private Double mrs_premi_tahunan;
	private Boolean premi_rider_single;
	private Integer persenUp;
	private Integer persenUpx;
	private String spouseName;
	private String childName;
	private Integer jenis;
	private Integer jenis_eka;
	private Integer jenis_hcp;
	private Integer countbasic;
	private Integer countSpouse;
	private Integer countChild1;
	private Integer countChild2;
	private Integer countChild3;
	private Integer countChild4;
	private Integer countChild5;
	private String namaRiderNya;
	private Integer flag_jenis_up;
	private Integer flag_jenis_Pesertamedplus;
	private Integer flag_jenis_Paketmedplus;
	
	public Boolean getPremi_rider_single() {
		return premi_rider_single;
	}

	public void setPremi_rider_single(Boolean premi_rider_single) {
		this.premi_rider_single = premi_rider_single;
	}

	public Double getMrs_premi_tahunan() {
		return mrs_premi_tahunan;
	}

	public void setMrs_premi_tahunan(Double mrs_premi_tahunan) {
		this.mrs_premi_tahunan = mrs_premi_tahunan;
	}

	public Datarider() {
	}
	
	public Datarider(Integer lsbs_id, Integer lsdbs_number, Integer mspr_class, Integer persenUp, Integer mspr_ins_period, Double mspr_premium, Double mspr_rate, Double mspr_tsi, Integer mspr_tt, Integer mspr_unit, String plan_rider) {
		this.lsbs_id = lsbs_id;
		this.lsdbs_number = lsdbs_number;
		this.mspr_class = mspr_class;
		this.persenUp = persenUp;
		this.mspr_ins_period = mspr_ins_period;
		this.mspr_premium = mspr_premium;
		this.mspr_rate = mspr_rate;
		this.mspr_tsi = mspr_tsi;
		this.mspr_tt = mspr_tt;
		this.mspr_unit = mspr_unit;
		this.plan_rider = plan_rider;
	}
	
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
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
	public String getPlan_rider() {
		return plan_rider;
	}
	public void setPlan_rider(String plan_rider) {
		this.plan_rider = plan_rider;
	}
	public String getLku_id_include() {
		return lku_id_include;
	}
	public void setLku_id_include(String lku_id_include) {
		this.lku_id_include = lku_id_include;
	}
	public Integer getLsbs_id_include() {
		return lsbs_id_include;
	}
	public void setLsbs_id_include(Integer lsbs_id_include) {
		this.lsbs_id_include = lsbs_id_include;
	}
	public String getLsdbs_name_include() {
		return lsdbs_name_include;
	}
	public void setLsdbs_name_include(String lsdbs_name_include) {
		this.lsdbs_name_include = lsdbs_name_include;
	}
	public Integer getLsdbs_number_include() {
		return lsdbs_number_include;
	}
	public void setLsdbs_number_include(Integer lsdbs_number_include) {
		this.lsdbs_number_include = lsdbs_number_include;
	}
	public Date getMspr_beg_date_include() {
		return mspr_beg_date_include;
	}
	public void setMspr_beg_date_include(Date mspr_beg_date_include) {
		this.mspr_beg_date_include = mspr_beg_date_include;
	}
	public Integer getMspr_class_include() {
		return mspr_class_include;
	}
	public void setMspr_class_include(Integer mspr_class_include) {
		this.mspr_class_include = mspr_class_include;
	}
	public Date getMspr_end_date_include() {
		return mspr_end_date_include;
	}
	public void setMspr_end_date_include(Date mspr_end_date_include) {
		this.mspr_end_date_include = mspr_end_date_include;
	}
	public Date getMspr_end_pay_include() {
		return mspr_end_pay_include;
	}
	public void setMspr_end_pay_include(Date mspr_end_pay_include) {
		this.mspr_end_pay_include = mspr_end_pay_include;
	}
	public Integer getMspr_ins_period_include() {
		return mspr_ins_period_include;
	}
	public void setMspr_ins_period_include(Integer mspr_ins_period_include) {
		this.mspr_ins_period_include = mspr_ins_period_include;
	}
	public Integer getMspr_persen_include() {
		return mspr_persen_include;
	}
	public void setMspr_persen_include(Integer mspr_persen_include) {
		this.mspr_persen_include = mspr_persen_include;
	}
	public Double getMspr_premium_include() {
		return mspr_premium_include;
	}
	public void setMspr_premium_include(Double mspr_premium_include) {
		this.mspr_premium_include = mspr_premium_include;
	}
	public Double getMspr_rate_include() {
		return mspr_rate_include;
	}
	public void setMspr_rate_include(Double mspr_rate_include) {
		this.mspr_rate_include = mspr_rate_include;
	}
	public Double getMspr_tsi_include() {
		return mspr_tsi_include;
	}
	public void setMspr_tsi_include(Double mspr_tsi_include) {
		this.mspr_tsi_include = mspr_tsi_include;
	}
	public Double getMspr_tsi_pa_a_include() {
		return mspr_tsi_pa_a_include;
	}
	public void setMspr_tsi_pa_a_include(Double mspr_tsi_pa_a_include) {
		this.mspr_tsi_pa_a_include = mspr_tsi_pa_a_include;
	}
	public Double getMspr_tsi_pa_b_include() {
		return mspr_tsi_pa_b_include;
	}
	public void setMspr_tsi_pa_b_include(Double mspr_tsi_pa_b_include) {
		this.mspr_tsi_pa_b_include = mspr_tsi_pa_b_include;
	}
	public Double getMspr_tsi_pa_c_include() {
		return mspr_tsi_pa_c_include;
	}
	public void setMspr_tsi_pa_c_include(Double mspr_tsi_pa_c_include) {
		this.mspr_tsi_pa_c_include = mspr_tsi_pa_c_include;
	}
	public Double getMspr_tsi_pa_d_include() {
		return mspr_tsi_pa_d_include;
	}
	public void setMspr_tsi_pa_d_include(Double mspr_tsi_pa_d_include) {
		this.mspr_tsi_pa_d_include = mspr_tsi_pa_d_include;
	}
	public Double getMspr_tsi_pa_m_include() {
		return mspr_tsi_pa_m_include;
	}
	public void setMspr_tsi_pa_m_include(Double mspr_tsi_pa_m_include) {
		this.mspr_tsi_pa_m_include = mspr_tsi_pa_m_include;
	}
	public Integer getMspr_tt_include() {
		return mspr_tt_include;
	}
	public void setMspr_tt_include(Integer mspr_tt_include) {
		this.mspr_tt_include = mspr_tt_include;
	}
	public Integer getMspr_unit_include() {
		return mspr_unit_include;
	}
	public void setMspr_unit_include(Integer mspr_unit_include) {
		this.mspr_unit_include = mspr_unit_include;
	}
	public Integer getMste_insured_no_include() {
		return mste_insured_no_include;
	}
	public void setMste_insured_no_include(Integer mste_insured_no_include) {
		this.mste_insured_no_include = mste_insured_no_include;
	}
	public String getPlan_rider_include() {
		return plan_rider_include;
	}
	public void setPlan_rider_include(String plan_rider_include) {
		this.plan_rider_include = plan_rider_include;
	}
	public Integer getFlag_include() {
		return flag_include;
	}
	public void setFlag_include(Integer flag_include) {
		this.flag_include = flag_include;
	}
	public Integer getMspr_tt1() {
		return mspr_tt1;
	}
	public void setMspr_tt1(Integer mspr_tt1) {
		this.mspr_tt1 = mspr_tt1;
	}
	public String getPlan_rider1() {
		return plan_rider1;
	}
	public void setPlan_rider1(String plan_rider1) {
		this.plan_rider1 = plan_rider1;
	}
	public Double getMspr_extra() {
		return mspr_extra;
	}
	public void setMspr_extra(Double mspr_extra) {
		this.mspr_extra = mspr_extra;
	}   
	public Integer getPersenUp(){
		return persenUp;
	}
	public void setPersenUp(Integer persenUp){
		this.persenUp = persenUp;
	}
	public Integer getPersenUpx(){
		return persenUpx;
	}
	public void setPersenUpx(Integer persenUpx){
		this.persenUpx = persenUpx;
	}
	
	public String getSpouseName(){
		return spouseName;
	}
	public void setSpouseName(String spouseName){
		this.spouseName = spouseName;
	}
	
	public String getChildName(){
		return childName;
	}
	public void setChildName(String childName){
		this.childName = childName;
	}
	
	public String getNamaRiderNya(){
		return namaRiderNya;
	}
	public void setNamaRiderNya(String namaRiderNya){
		this.namaRiderNya = namaRiderNya;
	}
	
	public Integer getJenis(){
		return jenis;
	}
	public void setJenis(Integer jenis){
		this.jenis = jenis;
	}
	
	public Integer getJenis_eka(){
		return jenis_eka;
	}
	public void setJenis_eka(Integer jenis_eka){
		this.jenis_eka = jenis_eka;
	}
	
	public Integer getJenis_hcp(){
		return jenis_hcp;
	}
	public void setJenis_hcp(Integer jenis_hcp){
		this.jenis_hcp = jenis_hcp;
	}
	
	public Integer getCountbasic(){
		return countbasic;
	}
	public void setCountbasic(Integer countbasic){
		this.countbasic = countbasic;
	}
	public Integer getCountSpouse(){
		return countSpouse;
	}
	public void setCountSpouse(Integer countSpouse){
		this.countSpouse = countSpouse;
	}
	public Integer getCountChild1(){
		return countChild1;
	}
	public void setCountChild1(Integer countChild1){
		this.countChild1 = countChild1;
	}
	public Integer getCountChild2(){
		return countChild2;
	}
	public void setCountChild2(Integer countChild2){
		this.countChild2 = countChild2;
	}
	public Integer getCountChild3(){
		return countChild3;
	}
	public void setCountChild3(Integer countChild3){
		this.countChild3 = countChild3;
	}
	public Integer getCountChild4(){
		return countChild4;
	}
	public void setCountChild4(Integer countChild4){
		this.countChild4 = countChild4;
	}
	public Integer getCountChild5(){
		return countChild5;
	}
	public void setCountChild5(Integer countChild5){
		this.countChild5 = countChild5;
	}

	public Integer getFlag_jenis_up() {
		return flag_jenis_up;
	}

	public void setFlag_jenis_up(Integer flag_jenis_up) {
		this.flag_jenis_up = flag_jenis_up;
	}

	public Integer getFlag_jenis_Pesertamedplus() {
		return flag_jenis_Pesertamedplus;
	}

	public void setFlag_jenis_Pesertamedplus(Integer flag_jenis_Pesertamedplus) {
		this.flag_jenis_Pesertamedplus = flag_jenis_Pesertamedplus;
	}

	public Integer getFlag_jenis_Paketmedplus() {
		return flag_jenis_Paketmedplus;
	}

	public void setFlag_jenis_Paketmedplus(Integer flag_jenis_Paketmedplus) {
		this.flag_jenis_Paketmedplus = flag_jenis_Paketmedplus;
	}

	
	
}
