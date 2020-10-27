package com.ekalife.elions.model.cross_selling;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Tabel MST_POLICY_CS
 * 
 * @author Yusuf
 * @since Jul 21, 2008 (9:13:12 AM)
 */
public class PolicyCs implements Serializable{

	private static final long serialVersionUID = 1L;

	//tabel utama
	public String reg_spaj;                       
	public Integer lspd_id;                       
	public Integer lscb_id;                       
	public Integer lus_id;                        
	public Integer lsbs_id;                       
	public Integer lsdbs_number;   
	public String lsbs_lsdbs;
	public String lku_id;                         
	public String mscs_policy_no;                 
	public Integer mscs_ins_period;               
	public Integer mscs_pay_period;               
	public Double mscs_premium;                   
	public Double mscs_tsi;                       
	public String mscs_holder;                    
	public Integer mscs_age;                      
	public Date mscs_birth_date;                  
	public Date mscs_spaj_date;                   
	public Date mscs_input_date;                  
	public Date mscs_ttp_date;                    
	public Date mscs_pay_date;                    
	public Date mscs_beg_date;                    
	public Date mscs_end_date;
	public Date mscs_tgl_terima_asm;
	public Integer mscs_pay_count;
	public Integer mscs_policy_type;
	
	//tabel utama - komisi
	public Double mscs_comm;
	public Double mscs_comm_tax;
	public Double mscs_comm_kurs;
	public Integer mscs_comm_paid;
	public Date mscs_comm_pay_date;
	//tabel utama - produksi
	public Date mscs_prod_date;                   
	public Double mscs_prod_kurs;                
	public Double mscs_upp_eva;                   
	public Double mscs_upp_kontes;                
	public Double mscs_upp_lain;       
	//tabel utama, cabang dkk
	public String lca_id;
	public String lwk_id;
	public String lsrg_id;
	
	//tabel references
	public String lspd_position;
	public String lscb_pay_mode;
	public String lus_login_name;
	public String lsbs_name;
	public String lsdbs_name;
	public String lku_symbol;
	
	//flag buat konfirmasi pembayaran
	public Integer selected;
	//flag buat konfirmasi apakah preminya mau dihitung by system or manual
	public Integer auto;
	
	//childs
	public List<AgentCs> daftarAgent;
	public List<PositionCs> historiPosisi;
	
	public Integer getMscs_policy_type() {
		return mscs_policy_type;
	}
	public void setMscs_policy_type(Integer mscsPolicyType) {
		mscs_policy_type = mscsPolicyType;
	}
	
	public Integer getSelected() {
		return selected;
	}
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
	public String getLwk_id() {
		return lwk_id;
	}
	public void setLwk_id(String lwk_id) {
		this.lwk_id = lwk_id;
	}
	public Date getMscs_tgl_terima_asm() {
		return mscs_tgl_terima_asm;
	}
	public void setMscs_tgl_terima_asm(Date mscs_tgl_terima_asm) {
		this.mscs_tgl_terima_asm = mscs_tgl_terima_asm;
	}
	public String getLsrg_id() {
		return lsrg_id;
	}
	public void setLsrg_id(String lsrg_id) {
		this.lsrg_id = lsrg_id;
	}
	public Date getMscs_prod_date() {
		return mscs_prod_date;
	}
	public void setMscs_prod_date(Date mscs_prod_date) {
		this.mscs_prod_date = mscs_prod_date;
	}
	public Double getMscs_prod_kurs() {
		return mscs_prod_kurs;
	}
	public void setMscs_prod_kurs(Double mscs_prod_kurs) {
		this.mscs_prod_kurs = mscs_prod_kurs;
	}
	public Double getMscs_upp_eva() {
		return mscs_upp_eva;
	}
	public void setMscs_upp_eva(Double mscs_upp_eva) {
		this.mscs_upp_eva = mscs_upp_eva;
	}
	public Double getMscs_upp_kontes() {
		return mscs_upp_kontes;
	}
	public void setMscs_upp_kontes(Double mscs_upp_kontes) {
		this.mscs_upp_kontes = mscs_upp_kontes;
	}
	public Double getMscs_upp_lain() {
		return mscs_upp_lain;
	}
	public void setMscs_upp_lain(Double mscs_upp_lain) {
		this.mscs_upp_lain = mscs_upp_lain;
	}
	public void setSelected(Integer selected) {
		this.selected = selected;
	}
	public Double getMscs_comm() {
		return mscs_comm;
	}
	public void setMscs_comm(Double mscs_comm) {
		this.mscs_comm = mscs_comm;
	}
	public Double getMscs_comm_tax() {
		return mscs_comm_tax;
	}
	public void setMscs_comm_tax(Double mscs_comm_tax) {
		this.mscs_comm_tax = mscs_comm_tax;
	}
	public Double getMscs_comm_kurs() {
		return mscs_comm_kurs;
	}
	public void setMscs_comm_kurs(Double mscs_comm_kurs) {
		this.mscs_comm_kurs = mscs_comm_kurs;
	}
	public Integer getMscs_comm_paid() {
		return mscs_comm_paid;
	}
	public void setMscs_comm_paid(Integer mscs_comm_paid) {
		this.mscs_comm_paid = mscs_comm_paid;
	}
	public Date getMscs_comm_pay_date() {
		return mscs_comm_pay_date;
	}
	public void setMscs_comm_pay_date(Date mscs_comm_pay_date) {
		this.mscs_comm_pay_date = mscs_comm_pay_date;
	}
	public String getLsbs_lsdbs() {
		return lsbs_lsdbs;
	}
	public void setLsbs_lsdbs(String lsbs_lsdbs) {
		this.lsbs_lsdbs = lsbs_lsdbs;
	}
	public List<AgentCs> getDaftarAgent() {
		return daftarAgent;
	}
	public String getLspd_position() {
		return lspd_position;
	}
	public void setLspd_position(String lspd_position) {
		this.lspd_position = lspd_position;
	}
	public String getLscb_pay_mode() {
		return lscb_pay_mode;
	}
	public void setLscb_pay_mode(String lscb_pay_mode) {
		this.lscb_pay_mode = lscb_pay_mode;
	}
	public String getLus_login_name() {
		return lus_login_name;
	}
	public void setLus_login_name(String lus_login_name) {
		this.lus_login_name = lus_login_name;
	}
	public String getLsbs_name() {
		return lsbs_name;
	}
	public void setLsbs_name(String lsbs_name) {
		this.lsbs_name = lsbs_name;
	}
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}
	public String getLku_symbol() {
		return lku_symbol;
	}
	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
	public void setDaftarAgent(List<AgentCs> daftarAgent) {
		this.daftarAgent = daftarAgent;
	}
	public List<PositionCs> getHistoriPosisi() {
		return historiPosisi;
	}
	public void setHistoriPosisi(List<PositionCs> historiPosisi) {
		this.historiPosisi = historiPosisi;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}
	public Integer getLscb_id() {
		return lscb_id;
	}
	public void setLscb_id(Integer lscb_id) {
		this.lscb_id = lscb_id;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
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
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public String getMscs_policy_no() {
		return mscs_policy_no;
	}
	public void setMscs_policy_no(String mscs_policy_no) {
		this.mscs_policy_no = mscs_policy_no;
	}
	public Integer getMscs_ins_period() {
		return mscs_ins_period;
	}
	public void setMscs_ins_period(Integer mscs_ins_period) {
		this.mscs_ins_period = mscs_ins_period;
	}
	public Integer getMscs_pay_period() {
		return mscs_pay_period;
	}
	public void setMscs_pay_period(Integer mscs_pay_period) {
		this.mscs_pay_period = mscs_pay_period;
	}
	public Double getMscs_premium() {
		return mscs_premium;
	}
	public void setMscs_premium(Double mscs_premium) {
		this.mscs_premium = mscs_premium;
	}
	public Double getMscs_tsi() {
		return mscs_tsi;
	}
	public void setMscs_tsi(Double mscs_tsi) {
		this.mscs_tsi = mscs_tsi;
	}
	public String getMscs_holder() {
		return mscs_holder;
	}
	public void setMscs_holder(String mscs_holder) {
		this.mscs_holder = mscs_holder;
	}
	public Integer getMscs_age() {
		return mscs_age;
	}
	public void setMscs_age(Integer mscs_age) {
		this.mscs_age = mscs_age;
	}
	public Date getMscs_birth_date() {
		return mscs_birth_date;
	}
	public void setMscs_birth_date(Date mscs_birth_date) {
		this.mscs_birth_date = mscs_birth_date;
	}
	public Date getMscs_spaj_date() {
		return mscs_spaj_date;
	}
	public void setMscs_spaj_date(Date mscs_spaj_date) {
		this.mscs_spaj_date = mscs_spaj_date;
	}
	public Date getMscs_input_date() {
		return mscs_input_date;
	}
	public void setMscs_input_date(Date mscs_input_date) {
		this.mscs_input_date = mscs_input_date;
	}
	public Date getMscs_ttp_date() {
		return mscs_ttp_date;
	}
	public void setMscs_ttp_date(Date mscs_ttp_date) {
		this.mscs_ttp_date = mscs_ttp_date;
	}
	public Date getMscs_pay_date() {
		return mscs_pay_date;
	}
	public void setMscs_pay_date(Date mscs_pay_date) {
		this.mscs_pay_date = mscs_pay_date;
	}
	public Date getMscs_beg_date() {
		return mscs_beg_date;
	}
	public void setMscs_beg_date(Date mscs_beg_date) {
		this.mscs_beg_date = mscs_beg_date;
	}
	public Date getMscs_end_date() {
		return mscs_end_date;
	}
	public void setMscs_end_date(Date mscs_end_date) {
		this.mscs_end_date = mscs_end_date;
	}
	public Integer getMscs_pay_count() {
		return mscs_pay_count;
	}
	public void setMscs_pay_count(Integer mscs_pay_count) {
		this.mscs_pay_count = mscs_pay_count;
	}
	public Integer getAuto() {
		return auto;
	}
	public void setAuto(Integer auto) {
		this.auto = auto;
	}                    

}
