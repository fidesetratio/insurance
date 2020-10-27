package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Class untuk perhitungan yang berhubungan dengan UPP
 * 
 * @author Yusuf
 * @since Jul 7, 2008 (1:40:53 PM)
 */
public class Upp implements Serializable {

	//variabel2 dasar
	public Integer lstb_id;
	public Integer lsgb_id;           
	public Integer lscb_id;
	public Integer lsbs_id;                       
	public Integer lsdbs_number;                  
	public Integer mspro_jn_prod;                 
	public Integer mspro_prod_ke;
	public Date mspro_prod_date;
	public Integer msbi_tahun_ke;                 
	public String reg_spaj;                       
	public Integer msbi_premi_ke;
	public Double premi_pokok;
	public Double premi_rider;
	public Double mspro_nilai_kurs;
	public Integer lsbs_jenis; 
	public String lca_id;

	//variabel2 hasil perhitungan UPP
	public Double upp_eva;
	public Double upp_kontes;
	public Double upp_lain;
	public Double upp_bonus;
	public Double com_eva;

	//variabel2 untuk perhitungan koefisien UPP
	public Integer flag_topup;
	public Integer mgi;
	public Integer lspc_type;
	
	public Double coef;
	public Double mod_hybrid;
	public Double mod_con;
	public Double mod_con_hs;
	public Double mod_link;
	public Double mod_con_rider;
	public Double mod_con_rider_hs;
	public Double comm_eva;
	public Double mod_lain;
	public Double mod_lain_hs;
	public Double mod_lain_rider;
	public Double mod_lain_rider_hs;
	public Double bonus_coef;
	public Double bonus_comm;
	
	public Double getBonus_coef() {
		return bonus_coef;
	}
	public void setBonus_coef(Double bonus_coef) {
		this.bonus_coef = bonus_coef;
	}
	public Double getBonus_comm() {
		return bonus_comm;
	}
	public void setBonus_comm(Double bonus_comm) {
		this.bonus_comm = bonus_comm;
	}
	public Double getCoef() {
		return coef;
	}
	public void setCoef(Double coef) {
		this.coef = coef;
	}
	public Double getCom_eva() {
		return com_eva;
	}
	public void setCom_eva(Double com_eva) {
		this.com_eva = com_eva;
	}
	public Double getComm_eva() {
		return comm_eva;
	}
	public void setComm_eva(Double comm_eva) {
		this.comm_eva = comm_eva;
	}
	public Integer getFlag_topup() {
		return flag_topup;
	}
	public void setFlag_topup(Integer flag_topup) {
		this.flag_topup = flag_topup;
	}
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public Integer getLsbs_jenis() {
		return lsbs_jenis;
	}
	public void setLsbs_jenis(Integer lsbs_jenis) {
		this.lsbs_jenis = lsbs_jenis;
	}
	public Integer getLscb_id() {
		return lscb_id;
	}
	public void setLscb_id(Integer lscb_id) {
		this.lscb_id = lscb_id;
	}
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	public Integer getLsgb_id() {
		return lsgb_id;
	}
	public void setLsgb_id(Integer lsgb_id) {
		this.lsgb_id = lsgb_id;
	}
	public Integer getLspc_type() {
		return lspc_type;
	}
	public void setLspc_type(Integer lspc_type) {
		this.lspc_type = lspc_type;
	}
	public Integer getLstb_id() {
		return lstb_id;
	}
	public void setLstb_id(Integer lstb_id) {
		this.lstb_id = lstb_id;
	}
	public Integer getMgi() {
		return mgi;
	}
	public void setMgi(Integer mgi) {
		this.mgi = mgi;
	}
	public Double getMod_con() {
		return mod_con;
	}
	public void setMod_con(Double mod_con) {
		this.mod_con = mod_con;
	}
	public Double getMod_con_hs() {
		return mod_con_hs;
	}
	public void setMod_con_hs(Double mod_con_hs) {
		this.mod_con_hs = mod_con_hs;
	}
	public Double getMod_con_rider() {
		return mod_con_rider;
	}
	public void setMod_con_rider(Double mod_con_rider) {
		this.mod_con_rider = mod_con_rider;
	}
	public Double getMod_con_rider_hs() {
		return mod_con_rider_hs;
	}
	public void setMod_con_rider_hs(Double mod_con_rider_hs) {
		this.mod_con_rider_hs = mod_con_rider_hs;
	}
	public Double getMod_hybrid() {
		return mod_hybrid;
	}
	public void setMod_hybrid(Double mod_hybrid) {
		this.mod_hybrid = mod_hybrid;
	}
	public Double getMod_lain() {
		return mod_lain;
	}
	public void setMod_lain(Double mod_lain) {
		this.mod_lain = mod_lain;
	}
	public Double getMod_lain_hs() {
		return mod_lain_hs;
	}
	public void setMod_lain_hs(Double mod_lain_hs) {
		this.mod_lain_hs = mod_lain_hs;
	}
	public Double getMod_lain_rider() {
		return mod_lain_rider;
	}
	public void setMod_lain_rider(Double mod_lain_rider) {
		this.mod_lain_rider = mod_lain_rider;
	}
	public Double getMod_lain_rider_hs() {
		return mod_lain_rider_hs;
	}
	public void setMod_lain_rider_hs(Double mod_lain_rider_hs) {
		this.mod_lain_rider_hs = mod_lain_rider_hs;
	}
	public Double getMod_link() {
		return mod_link;
	}
	public void setMod_link(Double mod_link) {
		this.mod_link = mod_link;
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
	public Integer getMspro_jn_prod() {
		return mspro_jn_prod;
	}
	public void setMspro_jn_prod(Integer mspro_jn_prod) {
		this.mspro_jn_prod = mspro_jn_prod;
	}
	public Double getMspro_nilai_kurs() {
		return mspro_nilai_kurs;
	}
	public void setMspro_nilai_kurs(Double mspro_nilai_kurs) {
		this.mspro_nilai_kurs = mspro_nilai_kurs;
	}
	public Date getMspro_prod_date() {
		return mspro_prod_date;
	}
	public void setMspro_prod_date(Date mspro_prod_date) {
		this.mspro_prod_date = mspro_prod_date;
	}
	public Integer getMspro_prod_ke() {
		return mspro_prod_ke;
	}
	public void setMspro_prod_ke(Integer mspro_prod_ke) {
		this.mspro_prod_ke = mspro_prod_ke;
	}
	public Double getPremi_pokok() {
		return premi_pokok;
	}
	public void setPremi_pokok(Double premi_pokok) {
		this.premi_pokok = premi_pokok;
	}
	public Double getPremi_rider() {
		return premi_rider;
	}
	public void setPremi_rider(Double premi_rider) {
		this.premi_rider = premi_rider;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Double getUpp_bonus() {
		return upp_bonus;
	}
	public void setUpp_bonus(Double upp_bonus) {
		this.upp_bonus = upp_bonus;
	}
	public Double getUpp_eva() {
		return upp_eva;
	}
	public void setUpp_eva(Double upp_eva) {
		this.upp_eva = upp_eva;
	}
	public Double getUpp_kontes() {
		return upp_kontes;
	}
	public void setUpp_kontes(Double upp_kontes) {
		this.upp_kontes = upp_kontes;
	}
	public Double getUpp_lain() {
		return upp_lain;
	}
	public void setUpp_lain(Double upp_lain) {
		this.upp_lain = upp_lain;
	}
	
}