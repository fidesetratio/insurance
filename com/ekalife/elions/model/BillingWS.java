package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class BillingWS implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5894528583316676584L;
	
	private String bill_no;                        
	private String member_id;                      
	private String reg_spaj;                       
	private Integer msbi_premi_ke;                 
	private Double jumlah_bayar;                  
	private Date bill_date;                        
	private String card_assign;                    
	private Integer bill_status;                   
	private String mspo_policy_no;                 
	private Date process_date;                     
	private String status_trx;                     
	private String status_trx_txt;                 
	private Integer flag_send_bill;                
	private Integer flag_send_email;               
	private Integer msbi_tahun_ke;                 
	private String lku_id;                         
	private Double mspa_nilai_kurs;               
	private Integer lsjb_id;                       
	private String lca_id;                         
	private String mspa_payment_id;                
	private Integer hit_count;
	private Integer lscb_id;                       
	private Integer trxid;                         
	private Integer kode_program;                  
	private Integer bill_accept;                   
	private Date bill_accept_date;
	private byte[] card_assign_byte;
	private String ip_address;
	private Double jumlah_diterima;
	private String master_bill_no; 
	private String atm_kurs;
	private Integer msbi_flag_topup;
	
	private Double total_premi;
	private Double besar_denda;
	private Integer hari_denda;
	private Double fee_ajs;
	private Double fee_xlink;
	private Double disc;
	private Integer flag_ulink;
	private Date msbi_beg_date;
	private Date msbi_end_date;
	private Date msbi_due_date;
	private Double msbi_stamp;
	private Double tahapan;
	private Integer lsbs_id;
	
	private Date mcp_next_bill;
	private Integer mcp_insper;
	private String mspo_policy_holder; 
	
	//temp
	private Double totPremi;
	private Double jumDisc;
	private Double subTotal;
	private Double tagihanPremi;
	private Double totalTagihAsli;
	private Double subTotalRP;
	private Double kursToday;
	private String kursCheck;
	private Double jumFee;
	private Double grandTotal;
	private String lku_symbol;
	private Double total_tagih;
	
	public Double getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}
	public Double getJumDisc() {
		return jumDisc;
	}
	public void setJumDisc(Double jumDisc) {
		this.jumDisc = jumDisc;
	}
	public Double getJumFee() {
		return jumFee;
	}
	public void setJumFee(Double jumFee) {
		this.jumFee = jumFee;
	}
	public String getKursCheck() {
		return kursCheck;
	}
	public void setKursCheck(String kursCheck) {
		this.kursCheck = kursCheck;
	}
	public Double getKursToday() {
		return kursToday;
	}
	public void setKursToday(Double kursToday) {
		this.kursToday = kursToday;
	}
	public Double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}
	public Double getSubTotalRP() {
		return subTotalRP;
	}
	public void setSubTotalRP(Double subTotalRP) {
		this.subTotalRP = subTotalRP;
	}
	public Double getTagihanPremi() {
		return tagihanPremi;
	}
	public void setTagihanPremi(Double tagihanPremi) {
		this.tagihanPremi = tagihanPremi;
	}
	public Double getTotalTagihAsli() {
		return totalTagihAsli;
	}
	public void setTotalTagihAsli(Double totalTagihAsli) {
		this.totalTagihAsli = totalTagihAsli;
	}
	public Double getTotPremi() {
		return totPremi;
	}
	public void setTotPremi(Double totPremi) {
		this.totPremi = totPremi;
	}
	public Date getMcp_next_bill() {
		return mcp_next_bill;
	}
	public void setMcp_next_bill(Date mcp_next_bill) {
		this.mcp_next_bill = mcp_next_bill;
	}
	public Double getJumlah_diterima() {
		return jumlah_diterima;
	}
	public void setJumlah_diterima(Double jumlah_diterima) {
		this.jumlah_diterima = jumlah_diterima;
	}
	public String getIp_address() {
		return ip_address;
	}
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
	public byte[] getCard_assign_byte() {
		return card_assign_byte;
	}
	public void setCard_assign_byte(byte[] card_assign_byte) {
		this.card_assign_byte = card_assign_byte;
	}
	public Integer getBill_accept() {
		return bill_accept;
	}
	public void setBill_accept(Integer bill_accept) {
		this.bill_accept = bill_accept;
	}
	public Date getBill_accept_date() {
		return bill_accept_date;
	}
	public void setBill_accept_date(Date bill_accept_date) {
		this.bill_accept_date = bill_accept_date;
	}
	public Date getBill_date() {
		return bill_date;
	}
	public void setBill_date(Date bill_date) {
		this.bill_date = bill_date;
	}
	public String getBill_no() {
		return bill_no;
	}
	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}
	public Integer getBill_status() {
		return bill_status;
	}
	public void setBill_status(Integer bill_status) {
		this.bill_status = bill_status;
	}
	public String getCard_assign() {
		return card_assign;
	}
	public void setCard_assign(String card_assign) {
		this.card_assign = card_assign;
	}
	public Integer getFlag_send_bill() {
		return flag_send_bill;
	}
	public void setFlag_send_bill(Integer flag_send_bill) {
		this.flag_send_bill = flag_send_bill;
	}
	public Integer getFlag_send_email() {
		return flag_send_email;
	}
	public void setFlag_send_email(Integer flag_send_email) {
		this.flag_send_email = flag_send_email;
	}
	public Integer getHit_count() {
		return hit_count;
	}
	public void setHit_count(Integer hit_count) {
		this.hit_count = hit_count;
	}
	public Double getJumlah_bayar() {
		return jumlah_bayar;
	}
	public void setJumlah_bayar(Double jumlah_bayar) {
		this.jumlah_bayar = jumlah_bayar;
	}
	public Integer getKode_program() {
		return kode_program;
	}
	public void setKode_program(Integer kode_program) {
		this.kode_program = kode_program;
	}
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Integer getLscb_id() {
		return lscb_id;
	}
	public void setLscb_id(Integer lscb_id) {
		this.lscb_id = lscb_id;
	}
	public Integer getLsjb_id() {
		return lsjb_id;
	}
	public void setLsjb_id(Integer lsjb_id) {
		this.lsjb_id = lsjb_id;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
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
	public Double getMspa_nilai_kurs() {
		return mspa_nilai_kurs;
	}
	public void setMspa_nilai_kurs(Double mspa_nilai_kurs) {
		this.mspa_nilai_kurs = mspa_nilai_kurs;
	}
	public String getMspa_payment_id() {
		return mspa_payment_id;
	}
	public void setMspa_payment_id(String mspa_payment_id) {
		this.mspa_payment_id = mspa_payment_id;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public Date getProcess_date() {
		return process_date;
	}
	public void setProcess_date(Date process_date) {
		this.process_date = process_date;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getStatus_trx() {
		return status_trx;
	}
	public void setStatus_trx(String status_trx) {
		this.status_trx = status_trx;
	}
	public String getStatus_trx_txt() {
		return status_trx_txt;
	}
	public void setStatus_trx_txt(String status_trx_txt) {
		this.status_trx_txt = status_trx_txt;
	}
	public Integer getTrxid() {
		return trxid;
	}
	public void setTrxid(Integer trxid) {
		this.trxid = trxid;
	}
	public Double getDisc() {
		return disc;
	}
	public void setDisc(Double disc) {
		this.disc = disc;
	}
	public Double getFee_ajs() {
		return fee_ajs;
	}
	public void setFee_ajs(Double fee_ajs) {
		this.fee_ajs = fee_ajs;
	}
	public Double getFee_xlink() {
		return fee_xlink;
	}
	public void setFee_xlink(Double fee_xlink) {
		this.fee_xlink = fee_xlink;
	}
	public Integer getFlag_ulink() {
		return flag_ulink;
	}
	public void setFlag_ulink(Integer flag_ulink) {
		this.flag_ulink = flag_ulink;
	}
	public Double getTotal_premi() {
		return total_premi;
	}
	public void setTotal_premi(Double total_premi) {
		this.total_premi = total_premi;
	}
	public Double getBesar_denda() {
		return besar_denda;
	}
	public void setBesar_denda(Double besar_denda) {
		this.besar_denda = besar_denda;
	}
	public Integer getHari_denda() {
		return hari_denda;
	}
	public void setHari_denda(Integer hari_denda) {
		this.hari_denda = hari_denda;
	}
	public Date getMsbi_beg_date() {
		return msbi_beg_date;
	}
	public void setMsbi_beg_date(Date msbi_beg_date) {
		this.msbi_beg_date = msbi_beg_date;
	}
	public Date getMsbi_end_date() {
		return msbi_end_date;
	}
	public void setMsbi_end_date(Date msbi_end_date) {
		this.msbi_end_date = msbi_end_date;
	}
	public Double getMsbi_stamp() {
		return msbi_stamp;
	}
	public void setMsbi_stamp(Double msbi_stamp) {
		this.msbi_stamp = msbi_stamp;
	}
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public Double getTahapan() {
		return tahapan;
	}
	public void setTahapan(Double tahapan) {
		this.tahapan = tahapan;
	}
	public Date getMsbi_due_date() {
		return msbi_due_date;
	}
	public void setMsbi_due_date(Date msbi_due_date) {
		this.msbi_due_date = msbi_due_date;
	}
	public Integer getMcp_insper() {
		return mcp_insper;
	}
	public void setMcp_insper(Integer mcp_insper) {
		this.mcp_insper = mcp_insper;
	}
	public String getMspo_policy_holder() {
		return mspo_policy_holder;
	}
	public void setMspo_policy_holder(String mspo_policy_holder) {
		this.mspo_policy_holder = mspo_policy_holder;
	}
	public String getLku_symbol() {
		return lku_symbol;
	}
	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
	public Double getTotal_tagih() {
		return total_tagih;
	}
	public void setTotal_tagih(Double total_tagih) {
		this.total_tagih = total_tagih;
	}
	public String getMaster_bill_no() {
		return master_bill_no;
	}
	public void setMaster_bill_no(String master_bill_no) {
		this.master_bill_no = master_bill_no;
	}
	public String getAtm_kurs() {
		return atm_kurs;
	}
	public void setAtm_kurs(String atm_kurs) {
		this.atm_kurs = atm_kurs;
	}
	public Integer getMsbi_flag_topup() {
		return msbi_flag_topup;
	}
	public void setMsbi_flag_topup(Integer msbi_flag_topup) {
		this.msbi_flag_topup = msbi_flag_topup;
	}
	
//	private String bill_no;
//	private String member_id; 
//	private String reg_spaj; 
//	private int msbi_premi_ke; 
//	private Double jumlah_bayar;
//	private Date bill_date; 
//	private String card_assign;
//	private String mspo_policy_no;
//	private Date process_date;
//	private String status_trx; 
//	private String status_trx_txt;
//	private int flag_send_bill;
//	private int flag_send_email;
//	private int msbi_tahun_ke;
//	private String lku_id;
//	private Double mspa_nilai_kurs;
//	private String lca_id;
//	private int lsjb_id;
//	private String mspa_payment_id; 
//	private int hit_count;
//	private int lscb_id;
//	private int trxid;
//	
//	public int getTrxid() {
//		return trxid;
//	}
//	public void setTrxid(int trxid) {
//		this.trxid = trxid;
//	}
//	public int getLscb_id() {
//		return lscb_id;
//	}
//	public void setLscb_id(int lscb_id) {
//		this.lscb_id = lscb_id;
//	}
//	public String getLca_id() {
//		return lca_id;
//	}
//	public void setLca_id(String lca_id) {
//		this.lca_id = lca_id;
//	}
//	public int getLsjb_id() {
//		return lsjb_id;
//	}
//	public void setLsjb_id(int lsjb_id) {
//		this.lsjb_id = lsjb_id;
//	}
//	public String getMspo_policy_no() {
//		return mspo_policy_no;
//	}
//	public void setMspo_policy_no(String mspo_policy_no) {
//		this.mspo_policy_no = mspo_policy_no;
//	}
//	public Date getBill_date() {
//		return bill_date;
//	}
//	public void setBill_date(Date bill_date) {
//		this.bill_date = bill_date;
//	}
//	public String getBill_no() {
//		return bill_no;
//	}
//	public void setBill_no(String bill_no) {
//		this.bill_no = bill_no;
//	}
//	public String getCard_assign() {
//		return card_assign;
//	}
//	public void setCard_assign(String card_assign) {
//		this.card_assign = card_assign;
//	}
//	public Double getJumlah_bayar() {
//		return jumlah_bayar;
//	}
//	public void setJumlah_bayar(Double jumlah_bayar) {
//		this.jumlah_bayar = jumlah_bayar;
//	}
//	public String getMember_id() {
//		return member_id;
//	}
//	public void setMember_id(String member_id) {
//		this.member_id = member_id;
//	}
//	public int getMsbi_premi_ke() {
//		return msbi_premi_ke;
//	}
//	public void setMsbi_premi_ke(int msbi_premi_ke) {
//		this.msbi_premi_ke = msbi_premi_ke;
//	}
//	public String getReg_spaj() {
//		return reg_spaj;
//	}
//	public void setReg_spaj(String reg_spaj) {
//		this.reg_spaj = reg_spaj;
//	}
//	public Date getProcess_date() {
//		return process_date;
//	}
//	public void setProcess_date(Date process_date) {
//		this.process_date = process_date;
//	}
//	public String getStatus_trx() {
//		return status_trx;
//	}
//	public void setStatus_trx(String status_trx) {
//		this.status_trx = status_trx;
//	}
//	public String getStatus_trx_txt() {
//		return status_trx_txt;
//	}
//	public void setStatus_trx_txt(String status_trx_txt) {
//		this.status_trx_txt = status_trx_txt;
//	}
//	public int getFlag_send_bill() {
//		return flag_send_bill;
//	}
//	public void setFlag_send_bill(int flag_send_bill) {
//		this.flag_send_bill = flag_send_bill;
//	}
//	public int getFlag_send_email() {
//		return flag_send_email;
//	}
//	public void setFlag_send_email(int flag_send_email) {
//		this.flag_send_email = flag_send_email;
//	}
//	public String getLku_id() {
//		return lku_id;
//	}
//	public void setLku_id(String lku_id) {
//		this.lku_id = lku_id;
//	}
//	public int getMsbi_tahun_ke() {
//		return msbi_tahun_ke;
//	}
//	public void setMsbi_tahun_ke(int msbi_tahun_ke) {
//		this.msbi_tahun_ke = msbi_tahun_ke;
//	}
//	public Double getMspa_nilai_kurs() {
//		return mspa_nilai_kurs;
//	}
//	public void setMspa_nilai_kurs(Double mspa_nilai_kurs) {
//		this.mspa_nilai_kurs = mspa_nilai_kurs;
//	}
//	public int getHit_count() {
//		return hit_count;
//	}
//	public void setHit_count(int hit_count) {
//		this.hit_count = hit_count;
//	}
//	public String getMspa_payment_id() {
//		return mspa_payment_id;
//	}
//	public void setMspa_payment_id(String mspa_payment_id) {
//		this.mspa_payment_id = mspa_payment_id;
//	}
	
	
}
