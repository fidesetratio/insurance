package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Reksadana implements Serializable {
	
	//inv_reksadana
	private String ire_reksa_name;
	private String ire_reksa_no;                   
	private Integer mrd_trans_id;                  
	private Integer lus_id;                        
	private Integer ird_reksa_trans;               
	private Date ird_trans_date;                   
	private Date ird_process;                      
	private Double ird_nav;                        
	private Double ird_value;                      
	private Double ird_unit;                       
	private Double ird_last_30d;                   
	private Double ird_last_oney;                  
	private Double ird_last_oneyr;
	
	//inv_reksa_transaksi
	private Integer irt_rtrans_id;                 
	private String lku_id;                         
	private Integer irt_rtrans_jn;                 
	private Date irt_input_date;                   
	private Date irt_trans_date;                   
	private Date irt_effective_date;               
	private Double irt_cost;                       
	private Double irt_amount;                     
	private Double irt_nav;                        
	private Double irt_subscribe_unit;             
	private Double irt_subs_redem_fee;             
	private String irt_note;                       
	private Double irt_total_cost;                 
	private Double irt_average_cost;
	private Double irt_gain_loss;   
	
	//tambahan
	private Double ird_nab_up; 
	private Double ird_last_1d;                   
	private Double ird_last_7d;                  
	private Double ird_last_3yr;
	
	
	public Reksadana() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Reksadana(String ireReksaName, String ireReksaNo,
			Date irdTransDate, Double irdNabUp, Double irdNav,
			Double irdLast_30d, Double irdLastOney,
			Double irdLastOneyr, Double irdLast_1d,
			Double irdLast_7d, Double irdLast_3yr) {
		super();
		ire_reksa_name = ireReksaName;
		ire_reksa_no = ireReksaNo;
		ird_trans_date = irdTransDate;
		ird_nab_up = irdNabUp;
		ird_nav = irdNav;
		ird_last_30d = irdLast_30d;
		ird_last_oney = irdLastOney;
		ird_last_oneyr = irdLastOneyr;
		ird_last_1d = irdLast_1d;
		ird_last_7d = irdLast_7d;
		ird_last_3yr = irdLast_3yr;
	}
	public Double getIrd_nab_up() {
		return ird_nab_up;
	}
	public void setIrd_nab_up(Double irdNabUp) {
		ird_nab_up = irdNabUp;
	}
	public Double getIrd_last_1d() {
		return ird_last_1d;
	}
	public void setIrd_last_1d(Double irdLast_1d) {
		ird_last_1d = irdLast_1d;
	}
	public Double getIrd_last_7d() {
		return ird_last_7d;
	}
	public void setIrd_last_7d(Double irdLast_7d) {
		ird_last_7d = irdLast_7d;
	}
	public Double getIrd_last_3yr() {
		return ird_last_3yr;
	}
	public void setIrd_last_3yr(Double irdLast_3yr) {
		ird_last_3yr = irdLast_3yr;
	}
	public Double getIrt_amount() {
		return irt_amount;
	}
	public void setIrt_amount(Double irt_amount) {
		this.irt_amount = irt_amount;
	}
	public Double getIrt_average_cost() {
		return irt_average_cost;
	}
	public void setIrt_average_cost(Double irt_average_cost) {
		this.irt_average_cost = irt_average_cost;
	}
	public Double getIrt_cost() {
		return irt_cost;
	}
	public void setIrt_cost(Double irt_cost) {
		this.irt_cost = irt_cost;
	}
	public Date getIrt_effective_date() {
		return irt_effective_date;
	}
	public void setIrt_effective_date(Date irt_effective_date) {
		this.irt_effective_date = irt_effective_date;
	}
	public Date getIrt_input_date() {
		return irt_input_date;
	}
	public void setIrt_input_date(Date irt_input_date) {
		this.irt_input_date = irt_input_date;
	}
	public Double getIrt_nav() {
		return irt_nav;
	}
	public void setIrt_nav(Double irt_nav) {
		this.irt_nav = irt_nav;
	}
	public String getIrt_note() {
		return irt_note;
	}
	public void setIrt_note(String irt_note) {
		this.irt_note = irt_note;
	}
	public Integer getIrt_rtrans_id() {
		return irt_rtrans_id;
	}
	public void setIrt_rtrans_id(Integer irt_rtrans_id) {
		this.irt_rtrans_id = irt_rtrans_id;
	}
	public Integer getIrt_rtrans_jn() {
		return irt_rtrans_jn;
	}
	public void setIrt_rtrans_jn(Integer irt_rtrans_jn) {
		this.irt_rtrans_jn = irt_rtrans_jn;
	}
	public Double getIrt_subs_redem_fee() {
		return irt_subs_redem_fee;
	}
	public void setIrt_subs_redem_fee(Double irt_subs_redem_fee) {
		this.irt_subs_redem_fee = irt_subs_redem_fee;
	}
	public Double getIrt_subscribe_unit() {
		return irt_subscribe_unit;
	}
	public void setIrt_subscribe_unit(Double irt_subscribe_unit) {
		this.irt_subscribe_unit = irt_subscribe_unit;
	}
	public Double getIrt_total_cost() {
		return irt_total_cost;
	}
	public void setIrt_total_cost(Double irt_total_cost) {
		this.irt_total_cost = irt_total_cost;
	}
	public Date getIrt_trans_date() {
		return irt_trans_date;
	}
	public void setIrt_trans_date(Date irt_trans_date) {
		this.irt_trans_date = irt_trans_date;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Double getIrd_last_30d() {
		return ird_last_30d;
	}
	public void setIrd_last_30d(Double ird_last_30d) {
		this.ird_last_30d = ird_last_30d;
	}
	public Double getIrd_last_oney() {
		return ird_last_oney;
	}
	public void setIrd_last_oney(Double ird_last_oney) {
		this.ird_last_oney = ird_last_oney;
	}
	public Double getIrd_last_oneyr() {
		return ird_last_oneyr;
	}
	public void setIrd_last_oneyr(Double ird_last_oneyr) {
		this.ird_last_oneyr = ird_last_oneyr;
	}
	public Double getIrd_nav() {
		return ird_nav;
	}
	public void setIrd_nav(Double ird_nav) {
		this.ird_nav = ird_nav;
	}
	public Date getIrd_process() {
		return ird_process;
	}
	public void setIrd_process(Date ird_process) {
		this.ird_process = ird_process;
	}
	public Integer getIrd_reksa_trans() {
		return ird_reksa_trans;
	}
	public void setIrd_reksa_trans(Integer ird_reksa_trans) {
		this.ird_reksa_trans = ird_reksa_trans;
	}
	public Date getIrd_trans_date() {
		return ird_trans_date;
	}
	public void setIrd_trans_date(Date ird_trans_date) {
		this.ird_trans_date = ird_trans_date;
	}
	public Double getIrd_unit() {
		return ird_unit;
	}
	public void setIrd_unit(Double ird_unit) {
		this.ird_unit = ird_unit;
	}
	public Double getIrd_value() {
		return ird_value;
	}
	public void setIrd_value(Double ird_value) {
		this.ird_value = ird_value;
	}
	public String getIre_reksa_name() {
		return ire_reksa_name;
	}
	public void setIre_reksa_name(String ire_reksa_name) {
		this.ire_reksa_name = ire_reksa_name;
	}
	public String getIre_reksa_no() {
		return ire_reksa_no;
	}
	public void setIre_reksa_no(String ire_reksa_no) {
		this.ire_reksa_no = ire_reksa_no;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public Integer getMrd_trans_id() {
		return mrd_trans_id;
	}
	public void setMrd_trans_id(Integer mrd_trans_id) {
		this.mrd_trans_id = mrd_trans_id;
	}
	public Double getIrt_gain_loss() {
		return irt_gain_loss;
	}
	public void setIrt_gain_loss(Double irt_gain_loss) {
		this.irt_gain_loss = irt_gain_loss;
	}                 
	
}