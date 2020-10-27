/**
 * 
 */
package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Yusuf
 *
 */
public class Commission implements Serializable {

	private static final long serialVersionUID = -324433606842579755L;

	public Date tgl_paid;                         
	public Double amount;                         
	public Integer lbn_id;                        
	public String account_no;                     
	
	public String lca_id;
	public Integer msag_sbm;
	public String msag_id;
	public Integer msbi_tahun_ke;
	public Integer msbi_premi_ke;
	public Integer msco_flag;
	public String lus_id;
	public String reg_spaj;
	public String co_id;
	public Integer cb_asli;
	public Integer cb_id;
	public Integer bisnis_no;
	public Double tax;
	public Integer flag_sbm;
	public Integer ins_period;
	public Integer lev_kom;
	public String lev_nama;
	public Integer th_kom;
	public Double komisi;
	public Double bonus;
	public Date tgl_kom;
	public Integer bisnis_id;
	public String agent_id;
	public String region;
	public String bancass;
	public String kurs_id;
	public Double nilai_kurs;
	public Double premi;
	public Integer flag_komisi;
	public Integer lsco_jenis;
	public Integer lsco_year;
	public Double msco_comm;
	public Double msco_tax;
	public Double total;
	public String mcl_gelar;
	public String mcl_first;
	public String msag_tabungan;
	public Double msco_nilai_kurs;
	public Integer msbi_flag_topup;
	public Integer lsle_id;
	public Integer lsla_id;
	public Integer reff_bii;
	public Integer sts_aktif;
	public Double lpc_dplk_p;
	public Double msco_comm_kud;
	public Double msco_tax_kud;
	public String mcl_id_kud;
	public String periode_kud;
	public Integer jenis_kud;
	public Integer flag_bayar_kud;
	public Date create_date_kud;
	
	public Double pkhusus;
	public Double pkasih;
	public Double pakm;

	public Boolean flag_mess;
	
	public List deduct;
	public Date msbi_beg_date;
	public Date msbi_end_date;

	public String msco_id;                        
	public Integer lev_comm;                      
	public Date msco_date;                        
	public Integer msco_active;                   
	public Date msco_pay_date;                    
	public Integer msco_paid;                     
	public String msco_no_pre;                    
	public Integer msco_jurnal;                   
	public String msco_no;
	public Integer lsbs_linebus;
	
//	public Boolean flag_ae;
//	public Integer skenario;
	
	public Integer getLsbs_linebus() {
		return lsbs_linebus;
	}
	public void setLsbs_linebus(Integer lsbsLineBus) {
		lsbs_linebus = lsbsLineBus;
	}
	public Double getPkhusus() {
		return pkhusus;
	}
	public void setPkhusus(Double pkhusus) {
		this.pkhusus = pkhusus;
	}
	public Double getPkasih() {
		return pkasih;
	}
	public void setPkasih(Double pkasih) {
		this.pkasih = pkasih;
	}
	public Double getPakm() {
		return pakm;
	}
	public void setPakm(Double pakm) {
		this.pakm = pakm;
	}

	public void setDeduct(List deduct) {
		this.deduct = deduct;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getAgent_id() {
		return agent_id;
	}
	public void setAgent_id(String agent_id) {
		this.agent_id = agent_id;
	}
	public String getBancass() {
		return bancass;
	}
	public void setBancass(String bancass) {
		this.bancass = bancass;
	}
	public Integer getBisnis_id() {
		return bisnis_id;
	}
	public void setBisnis_id(Integer bisnis_id) {
		this.bisnis_id = bisnis_id;
	}
	public Integer getBisnis_no() {
		return bisnis_no;
	}
	public void setBisnis_no(Integer bisnis_no) {
		this.bisnis_no = bisnis_no;
	}
	public Double getBonus() {
		return bonus;
	}
	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}
	public Integer getCb_asli() {
		return cb_asli;
	}
	public void setCb_asli(Integer cb_asli) {
		this.cb_asli = cb_asli;
	}
	public Integer getCb_id() {
		return cb_id;
	}
	public void setCb_id(Integer cb_id) {
		this.cb_id = cb_id;
	}
	public Integer getFlag_sbm() {
		return flag_sbm;
	}
	public void setFlag_sbm(Integer flag_sbm) {
		this.flag_sbm = flag_sbm;
	}
	public Integer getIns_period() {
		return ins_period;
	}
	public void setIns_period(Integer ins_period) {
		this.ins_period = ins_period;
	}
	public Double getKomisi() {
		return komisi;
	}
	public void setKomisi(Double komisi) {
		this.komisi = komisi;
	}
	public Integer getLev_kom() {
		return lev_kom;
	}
	public void setLev_kom(Integer lev_kom) {
		this.lev_kom = lev_kom;
	}
	public Integer getReff_bii() {
		return reff_bii;
	}
	public void setReff_bii(Integer reff_bii) {
		this.reff_bii = reff_bii;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public Integer getSts_aktif() {
		return sts_aktif;
	}
	public void setSts_aktif(Integer sts_aktif) {
		this.sts_aktif = sts_aktif;
	}
	public Double getTax() {
		return tax;
	}
	public void setTax(Double tax) {
		this.tax = tax;
	}
	public Date getTgl_kom() {
		return tgl_kom;
	}
	public void setTgl_kom(Date tgl_kom) {
		this.tgl_kom = tgl_kom;
	}
	public Integer getTh_kom() {
		return th_kom;
	}
	public void setTh_kom(Integer th_kom) {
		this.th_kom = th_kom;
	}
	public Boolean getFlag_mess() {
		return flag_mess;
	}
	public Boolean isFlag_mess() {
		return flag_mess;
	}
	public void setFlag_mess(Boolean flag_mess) {
		this.flag_mess = flag_mess;
	}
	public String getKurs_id() {
		return kurs_id;
	}
	public void setKurs_id(String kurs_id) {
		this.kurs_id = kurs_id;
	}
	public Double getPremi() {
		return premi;
	}
	public void setPremi(Double premi) {
		this.premi = premi;
	}
	public Integer getFlag_komisi() {
		return flag_komisi;
	}
	public void setFlag_komisi(Integer flag_komisi) {
		this.flag_komisi = flag_komisi;
	}
	public String getCo_id() {
		return co_id;
	}
	public void setCo_id(String co_id) {
		this.co_id = co_id;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getLus_id() {
		return lus_id;
	}
	public void setLus_id(String lus_id) {
		this.lus_id = lus_id;
	}
	public Double getNilai_kurs() {
		return nilai_kurs;
	}
	public void setNilai_kurs(Double nilai_kurs) {
		this.nilai_kurs = nilai_kurs;
	}
	public Integer getMsco_flag() {
		return msco_flag;
	}
	public void setMsco_flag(Integer msco_flag) {
		this.msco_flag = msco_flag;
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
	public Integer getLsco_jenis() {
		return lsco_jenis;
	}
	public void setLsco_jenis(Integer lsco_jenis) {
		this.lsco_jenis = lsco_jenis;
	}
	public void setLev_nama(String lev_nama) {
		this.lev_nama = lev_nama;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public void setMcl_gelar(String mcl_gelar) {
		this.mcl_gelar = mcl_gelar;
	}
	public void setMsag_tabungan(String msag_tabungan) {
		this.msag_tabungan = msag_tabungan;
	}
	public void setMsbi_beg_date(Date msbi_beg_date) {
		this.msbi_beg_date = msbi_beg_date;
	}
	public void setMsbi_end_date(Date msbi_end_date) {
		this.msbi_end_date = msbi_end_date;
	}
	public void setMsco_comm(Double msco_comm) {
		this.msco_comm = msco_comm;
	}
	public void setMsco_tax(Double msco_tax) {
		this.msco_tax = msco_tax;
	}
	public void setMsco_nilai_kurs(Double msco_nilai_kurs) {
		this.msco_nilai_kurs = msco_nilai_kurs;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public void setMsag_id(String msag_id) {
		this.msag_id = msag_id;
	}
	public void setMsbi_flag_topup(Integer msbi_flag_topup) {
		this.msbi_flag_topup = msbi_flag_topup;
	}
	public List getDeduct() {
		return deduct;
	}
	public String getLev_nama() {
		return lev_nama;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public String getMcl_gelar() {
		return mcl_gelar;
	}
	public String getMsag_id() {
		return msag_id;
	}
	public String getMsag_tabungan() {
		return msag_tabungan;
	}
	public Date getMsbi_beg_date() {
		return msbi_beg_date;
	}
	public Date getMsbi_end_date() {
		return msbi_end_date;
	}
	public Integer getMsbi_flag_topup() {
		return msbi_flag_topup;
	}
	public Double getMsco_comm() {
		return msco_comm;
	}
	public Double getMsco_nilai_kurs() {
		return msco_nilai_kurs;
	}
	public Double getMsco_tax() {
		return msco_tax;
	}
	public Double getTotal() {
		return total;
	}
	public Integer getLsle_id() {
		return lsle_id;
	}
	public void setLsle_id(Integer lsle_id) {
		this.lsle_id = lsle_id;
	}
	public Integer getMsag_sbm() {
		return msag_sbm;
	}
	public void setMsag_sbm(Integer msag_sbm) {
		this.msag_sbm = msag_sbm;
	}
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
	public Double getLpc_dplk_p() {
		return lpc_dplk_p;
	}
	public void setLpc_dplk_p(Double lpc_dplk_p) {
		this.lpc_dplk_p = lpc_dplk_p;
	}
	public Integer getLsla_id() {
		return lsla_id;
	}
	public void setLsla_id(Integer lsla_id) {
		this.lsla_id = lsla_id;
	}
	public Integer getLsco_year() {
		return lsco_year;
	}
	public void setLsco_year(Integer lsco_year) {
		this.lsco_year = lsco_year;
	}
	public Integer getLev_comm() {
		return lev_comm;
	}
	public void setLev_comm(Integer lev_comm) {
		this.lev_comm = lev_comm;
	}
	public Integer getMsco_active() {
		return msco_active;
	}
	public void setMsco_active(Integer msco_active) {
		this.msco_active = msco_active;
	}
	public Date getMsco_date() {
		return msco_date;
	}
	public void setMsco_date(Date msco_date) {
		this.msco_date = msco_date;
	}
	public String getMsco_id() {
		return msco_id;
	}
	public void setMsco_id(String msco_id) {
		this.msco_id = msco_id;
	}
	public Integer getMsco_jurnal() {
		return msco_jurnal;
	}
	public void setMsco_jurnal(Integer msco_jurnal) {
		this.msco_jurnal = msco_jurnal;
	}
	public String getMsco_no() {
		return msco_no;
	}
	public void setMsco_no(String msco_no) {
		this.msco_no = msco_no;
	}
	public String getMsco_no_pre() {
		return msco_no_pre;
	}
	public void setMsco_no_pre(String msco_no_pre) {
		this.msco_no_pre = msco_no_pre;
	}
	public Integer getMsco_paid() {
		return msco_paid;
	}
	public void setMsco_paid(Integer msco_paid) {
		this.msco_paid = msco_paid;
	}
	public Date getMsco_pay_date() {
		return msco_pay_date;
	}
	public void setMsco_pay_date(Date msco_pay_date) {
		this.msco_pay_date = msco_pay_date;
	}
	public String getAccount_no() {
		return account_no;
	}
	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getLbn_id() {
		return lbn_id;
	}
	public void setLbn_id(Integer lbn_id) {
		this.lbn_id = lbn_id;
	}
	public Date getTgl_paid() {
		return tgl_paid;
	}
	public void setTgl_paid(Date tgl_paid) {
		this.tgl_paid = tgl_paid;
	}
	public Integer getFlag_bayar_kud() {
		return flag_bayar_kud;
	}
	public void setFlag_bayar_kud(Integer flag_bayar_kud) {
		this.flag_bayar_kud = flag_bayar_kud;
	}
	public Integer getJenis_kud() {
		return jenis_kud;
	}
	public void setJenis_kud(Integer jenis_kud) {
		this.jenis_kud = jenis_kud;
	}
	public String getMcl_id_kud() {
		return mcl_id_kud;
	}
	public void setMcl_id_kud(String mcl_id_kud) {
		this.mcl_id_kud = mcl_id_kud;
	}
	public Double getMsco_comm_kud() {
		return msco_comm_kud;
	}
	public void setMsco_comm_kud(Double msco_comm_kud) {
		this.msco_comm_kud = msco_comm_kud;
	}
	public Double getMsco_tax_kud() {
		return msco_tax_kud;
	}
	public void setMsco_tax_kud(Double msco_tax_kud) {
		this.msco_tax_kud = msco_tax_kud;
	}
	public String getPeriode_kud() {
		return periode_kud;
	}
	public void setPeriode_kud(String periode_kud) {
		this.periode_kud = periode_kud;
	}
	public Date getCreate_date_kud() {
		return create_date_kud;
	}
	public void setCreate_date_kud(Date create_date_kud) {
		this.create_date_kud = create_date_kud;
	}
	
}
