package com.ekalife.elions.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.apache.poi.hssf.record.formula.MemErrPtg;

import com.ibatis.common.resources.Resources;

public class NSIAPAYDET implements Serializable{
	
	protected final Log logger = LogFactory.getLog( getClass() );

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3481213434890793156L;
	
	private Integer id;                            
	private String transidmerchant;                
	private Integer kode_program;                  
	private String reg_spaj;                       
	private String mspo_policy_no;                 
	private Integer msbi_premi_ke;                 
	private Integer msbi_tahun_ke;                 
	private String lku_id;                         
	private Double mspa_nilai_kurs;                
	private Integer lsjb_id;                       
	private String lca_id;                         
	private String mspa_payment_id;                
	private Integer lscb_id;                       
	private Integer bill_accept;                   
	private Date bill_accept_date;                 
	private String ip_address;                     
	private Double jumlah_diterima;                
	private Double total_premi;                    
	private Double besar_denda;                    
	private Integer hari_denda;                    
	private Double fee_ajs;                        
	private Double fee_nsia;                       
	private Double disc;                           
	private Integer flag_ulink;                    
	private Date msbi_beg_date;                    
	private Date msbi_end_date;                    
	private Double msbi_stamp;                     
	private Double tahapan;                        
	private Integer lsbs_id;
	private Double jumlah_bayar; 
	private String product_code; 
	private String product_name;
	private Integer lus_id;
	private String holder_name;
	private Date bod_holder; 
	private String email;
	private Integer sex;
	private String master_transid;
	private String member_id;
	private String invoice_no;
	private String response_code;
	private String status;
	private Date start_date;
	private Date end_date;
	private Integer date_execute;
	private String month_execute;
	
	
//	ADDITIONAL
	
	private MemberWS memberWS;
	
	private Date msbi_due_date;
	
	
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
	private String namaPP;
	private Double fee_topup;
	private Double fee_regular;
	
	private Integer konfirmasi;
	private String j_captcha_id;
	private String j_captcha_response;
	
	public String getJ_captcha_id() {
		return j_captcha_id;
	}

	public void setJ_captcha_id(String j_captcha_id) {
		this.j_captcha_id = j_captcha_id;
	}

	public String getJ_captcha_response() {
		return j_captcha_response;
	}

	public void setJ_captcha_response(String j_captcha_response) {
		this.j_captcha_response = j_captcha_response;
	}

	public Integer getKonfirmasi() {
		return konfirmasi;
	}

	public void setKonfirmasi(Integer konfirmasi) {
		this.konfirmasi = konfirmasi;
	}

	public NSIAPAYDET(){
		Properties p=null;
		try {
			 p = Resources.getResourceAsProperties("policy.properties");
		} catch (IOException e) {
			
			logger.error("ERROR :", e);
		}
		
		this.fee_topup=new Double(p.getProperty("nilai.fee_topup"));
		this.fee_regular=new Double(p.getProperty("nilai.fee"));
		
	}
	
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
	public String getLku_symbol() {
		return lku_symbol;
	}
	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
	public Integer getMcp_insper() {
		return mcp_insper;
	}
	public void setMcp_insper(Integer mcp_insper) {
		this.mcp_insper = mcp_insper;
	}
	public Date getMcp_next_bill() {
		return mcp_next_bill;
	}
	public void setMcp_next_bill(Date mcp_next_bill) {
		this.mcp_next_bill = mcp_next_bill;
	}
	public Date getMsbi_due_date() {
		return msbi_due_date;
	}
	public void setMsbi_due_date(Date msbi_due_date) {
		this.msbi_due_date = msbi_due_date;
	}
	public String getMspo_policy_holder() {
		return mspo_policy_holder;
	}
	public void setMspo_policy_holder(String mspo_policy_holder) {
		this.mspo_policy_holder = mspo_policy_holder;
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
	public Double getTotal_tagih() {
		return total_tagih;
	}
	public void setTotal_tagih(Double total_tagih) {
		this.total_tagih = total_tagih;
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
	public Double getBesar_denda() {
		return besar_denda;
	}
	public void setBesar_denda(Double besar_denda) {
		this.besar_denda = besar_denda;
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
	public Double getFee_nsia() {
		return fee_nsia;
	}
	public void setFee_nsia(Double fee_nsia) {
		this.fee_nsia = fee_nsia;
	}
	public Integer getFlag_ulink() {
		return flag_ulink;
	}
	public void setFlag_ulink(Integer flag_ulink) {
		this.flag_ulink = flag_ulink;
	}
	public Integer getHari_denda() {
		return hari_denda;
	}
	public void setHari_denda(Integer hari_denda) {
		this.hari_denda = hari_denda;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIp_address() {
		return ip_address;
	}
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
	public Double getJumlah_diterima() {
		return jumlah_diterima;
	}
	public void setJumlah_diterima(Double jumlah_diterima) {
		this.jumlah_diterima = jumlah_diterima;
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
	public Integer getLsjb_id() {
		return lsjb_id;
	}
	public void setLsjb_id(Integer lsjb_id) {
		this.lsjb_id = lsjb_id;
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
	public Integer getMsbi_premi_ke() {
		return msbi_premi_ke;
	}
	public void setMsbi_premi_ke(Integer msbi_premi_ke) {
		this.msbi_premi_ke = msbi_premi_ke;
	}
	public Double getMsbi_stamp() {
		return msbi_stamp;
	}
	public void setMsbi_stamp(Double msbi_stamp) {
		this.msbi_stamp = msbi_stamp;
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
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Double getTahapan() {
		return tahapan;
	}
	public void setTahapan(Double tahapan) {
		this.tahapan = tahapan;
	}
	public Double getTotal_premi() {
		return total_premi;
	}
	public void setTotal_premi(Double total_premi) {
		this.total_premi = total_premi;
	}
	public String getTransidmerchant() {
		return transidmerchant;
	}
	public void setTransidmerchant(String transidmerchant) {
		this.transidmerchant = transidmerchant;
	}
	public Double getJumlah_bayar() {
		return jumlah_bayar;
	}
	public void setJumlah_bayar(Double jumlah_bayar) {
		this.jumlah_bayar = jumlah_bayar;
	}

	public Double getFee_regular() {
		return fee_regular;
	}

	public void setFee_regular(Double fee_regular) {
		this.fee_regular = fee_regular;
	}

	public Double getFee_topup() {
		return fee_topup;
	}

	public void setFee_topup(Double fee_topup) {
		this.fee_topup = fee_topup;
	}

	public String getNamaPP() {
		return namaPP;
	}

	public void setNamaPP(String namaPP) {
		this.namaPP = namaPP;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public Integer getLus_id() {
		return lus_id;
	}

	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}

	public Date getBod_holder() {
		return bod_holder;
	}

	public void setBod_holder(Date bod_holder) {
		this.bod_holder = bod_holder;
	}

	public String getHolder_name() {
		return holder_name;
	}

	public void setHolder_name(String holder_name) {
		this.holder_name = holder_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getMaster_transid() {
		return master_transid;
	}

	public void setMaster_transid(String master_transid) {
		this.master_transid = master_transid;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public MemberWS getMemberWS() {
		return memberWS;
	}

	public void setMemberWS(MemberWS memberWS) {
		this.memberWS = memberWS;
	}

	public String getInvoice_no() {
		return invoice_no;
	}

	public void setInvoice_no(String invoice_no) {
		this.invoice_no = invoice_no;
	}

	public String getResponse_code() {
		return response_code;
	}

	public void setResponse_code(String response_code) {
		this.response_code = response_code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getDate_execute() {
		return date_execute;
	}

	public void setDate_execute(Integer date_execute) {
		this.date_execute = date_execute;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getMonth_execute() {
		return month_execute;
	}

	public void setMonth_execute(String month_execute) {
		this.month_execute = month_execute;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}   
}
