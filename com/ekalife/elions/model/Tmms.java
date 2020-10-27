package com.ekalife.elions.model;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tmms implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2120981781545177444L;
	
	private Date tgl_awal;                         
	private String kd_agent;                       
	private Integer flag_bonus;                    
	private Date tgl_bonus;                        
	private Integer bank_pusat;    //                
	private String kode_cc;                        
	private String tm_id;                          
	private String product_code;                   
	private String mst_id; 
	private String mst_id_encrypt;
	private Date tgl_surat;                        
	private Integer bank;   
	private Integer usia;
	private String bank_cabang;                    
	private String bank_kota;                      
	private String no_rekening;                    
	private Integer flag_endors;                   
	private String cover_letter;                   
	private Double lump_sum;                       
	private Integer flag_pa;                       
	private String no_sertifikat_pa;               
	private Date beg_date_pa;                      
	private Date end_date_pa; 
	private Date tgl_print_pa;                     
	private Date tgl_upload;                       
	private Date tgl_kirim_polis;                  
	private String bod_tempat;                     
	private String no_identitas;                   
	private String bank_atasnama;                  
	private String application_id;                 
	private String address_kores1;                 
	private String address_kores2;                 
	private String city_kores;                     
	private String postcode_kores;                 
	private String ip_ttp_email;                   
	private String id; 
	private String id_encrypt;
	private Integer flag_proses_polis;
	private Date tgl_proses_polis;
	
	private String bank_id;                        
	private String campaign;                       
	private String cust_no;                        
	private String no_sertifikat;                  
	private String holder_name;                    
	private Integer sex;                           
	private Date bod_holder;
	private String bod_holder_dd;
	private String bod_holder_mm;
	private String bod_holder_yyyy;
	private Integer status;  //status marital, 0 =single, 1: married
	private Integer bill_freq;  // freq bayar                    
	private String bill_mode; // transfer, autodebet, cc ,etc                     
	private String card_no; //bisa diinput nomor rekening                       
	private String expired;
	private String expired_mm;
	private String expired_yy;
	private Date call_date;                        
	private String tm_code;                        
	private Integer flag_cek;                      
	private Integer posisi;                        
	private Integer status_polis;                  
	private Date beg_date;                         
	private Date end_date;                         
	private Integer flag_edit;                     
	private Integer flag_next_bill;                
	private Date tgl_next_bill;                    
	private Date tgl_input;                        
	private Date tgl_export;                       
	private Date tgl_print;                        
	private Date tgl_ttp;                          
	private String address1;                       
	private String address2;                       
	private String city;      
	private String postal_code;                    
	private String home_area;                      
	private String home_phone;                     
	private String work_are;                       
	private String work_phone;                     
	private String fax_area;                       
	private String fax_no;                         
	private String fax_warea;                      
	private String fax_wno;                        
	private String mobile_no;                      
	private String email;                          
	private String card_type;
	private Integer lside_id;
	private Integer flag_refund_via;
	private Integer flag_disc;
	private String virtual_account;
	private Integer flag_db ;
	private Double tm_premi;
	
	private String mgs_nik;  
	private String ip;
	private Integer flag_koresp_sama;
	private String mspo_plan_provider;
	
	public Date getTgl_awal() {
		return tgl_awal;
	}
	public void setTgl_awal(Date tglAwal) {
		tgl_awal = tglAwal;
	}
	public String getKd_agent() {
		return kd_agent;
	}
	public void setKd_agent(String kdAgent) {
		kd_agent = kdAgent;
	}
	public Integer getFlag_bonus() {
		return flag_bonus;
	}
	public void setFlag_bonus(Integer flagBonus) {
		flag_bonus = flagBonus;
	}
	public Date getTgl_bonus() {
		return tgl_bonus;
	}
	public void setTgl_bonus(Date tglBonus) {
		tgl_bonus = tglBonus;
	}
	public Integer getBank_pusat() {
		return bank_pusat;
	}
	public void setBank_pusat(Integer bankPusat) {
		bank_pusat = bankPusat;
	}
	public String getKode_cc() {
		return kode_cc;
	}
	public void setKode_cc(String kodeCc) {
		kode_cc = kodeCc;
	}
	public String getTm_id() {
		return tm_id;
	}
	public void setTm_id(String tmId) {
		tm_id = tmId;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String productCode) {
		product_code = productCode;
	}
	public String getMst_id() {
		return mst_id;
	}
	public void setMst_id(String mstId) {
		mst_id = mstId;
	}
	public String getMst_id_encrypt() {
		return mst_id_encrypt;
	}
	public void setMst_id_encrypt(String mstIdEncrypt) {
		mst_id_encrypt = mstIdEncrypt;
	}
	public Date getTgl_surat() {
		return tgl_surat;
	}
	public void setTgl_surat(Date tglSurat) {
		tgl_surat = tglSurat;
	}
	public Integer getBank() {
		return bank;
	}
	public void setBank(Integer bank) {
		this.bank = bank;
	}
	public String getBank_cabang() {
		return bank_cabang;
	}
	public void setBank_cabang(String bankCabang) {
		bank_cabang = bankCabang;
	}
	public String getBank_kota() {
		return bank_kota;
	}
	public void setBank_kota(String bankKota) {
		bank_kota = bankKota;
	}
	public String getNo_rekening() {
		return no_rekening;
	}
	public void setNo_rekening(String noRekening) {
		no_rekening = noRekening;
	}
	public Integer getFlag_endors() {
		return flag_endors;
	}
	public void setFlag_endors(Integer flagEndors) {
		flag_endors = flagEndors;
	}
	public String getCover_letter() {
		return cover_letter;
	}
	public void setCover_letter(String coverLetter) {
		cover_letter = coverLetter;
	}
	public Double getLump_sum() {
		return lump_sum;
	}
	public void setLump_sum(Double lumpSum) {
		lump_sum = lumpSum;
	}
	public Integer getFlag_pa() {
		return flag_pa;
	}
	public void setFlag_pa(Integer flagPa) {
		flag_pa = flagPa;
	}
	public String getNo_sertifikat_pa() {
		return no_sertifikat_pa;
	}
	public void setNo_sertifikat_pa(String noSertifikatPa) {
		no_sertifikat_pa = noSertifikatPa;
	}
	public Date getBeg_date_pa() {
		return beg_date_pa;
	}
	public void setBeg_date_pa(Date begDatePa) {
		beg_date_pa = begDatePa;
	}
	public Date getEnd_date_pa() {
		return end_date_pa;
	}
	public void setEnd_date_pa(Date endDatePa) {
		end_date_pa = endDatePa;
	}
	public Date getTgl_print_pa() {
		return tgl_print_pa;
	}
	public void setTgl_print_pa(Date tglPrintPa) {
		tgl_print_pa = tglPrintPa;
	}
	public Date getTgl_upload() {
		return tgl_upload;
	}
	public void setTgl_upload(Date tglUpload) {
		tgl_upload = tglUpload;
	}
	public Date getTgl_kirim_polis() {
		return tgl_kirim_polis;
	}
	public void setTgl_kirim_polis(Date tglKirimPolis) {
		tgl_kirim_polis = tglKirimPolis;
	}
	public String getBod_tempat() {
		return bod_tempat;
	}
	public void setBod_tempat(String bodTempat) {
		bod_tempat = bodTempat;
	}
	public String getNo_identitas() {
		return no_identitas;
	}
	public void setNo_identitas(String noIdentitas) {
		no_identitas = noIdentitas;
	}
	public String getBank_atasnama() {
		return bank_atasnama;
	}
	public void setBank_atasnama(String bankAtasnama) {
		bank_atasnama = bankAtasnama;
	}
	public String getApplication_id() {
		return application_id;
	}
	public void setApplication_id(String applicationId) {
		application_id = applicationId;
	}
	public String getAddress_kores1() {
		return address_kores1;
	}
	public void setAddress_kores1(String addressKores1) {
		address_kores1 = addressKores1;
	}
	public String getAddress_kores2() {
		return address_kores2;
	}
	public void setAddress_kores2(String addressKores2) {
		address_kores2 = addressKores2;
	}
	public String getCity_kores() {
		return city_kores;
	}
	public void setCity_kores(String cityKores) {
		city_kores = cityKores;
	}
	public String getPostcode_kores() {
		return postcode_kores;
	}
	public void setPostcode_kores(String postcodeKores) {
		postcode_kores = postcodeKores;
	}
	public String getIp_ttp_email() {
		return ip_ttp_email;
	}
	public void setIp_ttp_email(String ipTtpEmail) {
		ip_ttp_email = ipTtpEmail;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId_encrypt() {
		return id_encrypt;
	}
	public void setId_encrypt(String idEncrypt) {
		id_encrypt = idEncrypt;
	}
	public Integer getFlag_proses_polis() {
		return flag_proses_polis;
	}
	public void setFlag_proses_polis(Integer flagProsesPolis) {
		flag_proses_polis = flagProsesPolis;
	}
	public Date getTgl_proses_polis() {
		return tgl_proses_polis;
	}
	public void setTgl_proses_polis(Date tglProsesPolis) {
		tgl_proses_polis = tglProsesPolis;
	}
	public String getBank_id() {
		return bank_id;
	}
	public void setBank_id(String bankId) {
		bank_id = bankId;
	}
	public String getCampaign() {
		return campaign;
	}
	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}
	public String getCust_no() {
		return cust_no;
	}
	public void setCust_no(String custNo) {
		cust_no = custNo;
	}
	public String getNo_sertifikat() {
		return no_sertifikat;
	}
	public void setNo_sertifikat(String noSertifikat) {
		no_sertifikat = noSertifikat;
	}
	public String getHolder_name() {
		return holder_name;
	}
	public void setHolder_name(String holderName) {
		holder_name = holderName;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Date getBod_holder() {
		return bod_holder;
	}
	public void setBod_holder(Date bodHolder) {
		bod_holder = bodHolder;
	}
	public String getBod_holder_dd() {
		return bod_holder_dd;
	}
	public void setBod_holder_dd(String bodHolderDd) {
		bod_holder_dd = bodHolderDd;
	}
	public String getBod_holder_mm() {
		return bod_holder_mm;
	}
	public void setBod_holder_mm(String bodHolderMm) {
		bod_holder_mm = bodHolderMm;
	}
	public String getBod_holder_yyyy() {
		return bod_holder_yyyy;
	}
	public void setBod_holder_yyyy(String bodHolderYyyy) {
		bod_holder_yyyy = bodHolderYyyy;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getBill_freq() {
		return bill_freq;
	}
	public void setBill_freq(Integer billFreq) {
		bill_freq = billFreq;
	}
	public String getBill_mode() {
		return bill_mode;
	}
	public void setBill_mode(String billMode) {
		bill_mode = billMode;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String cardNo) {
		card_no = cardNo;
	}
	public String getExpired() {
		return expired;
	}
	public void setExpired(String expired) {
		this.expired = expired;
	}
	public String getExpired_mm() {
		return expired_mm;
	}
	public void setExpired_mm(String expiredMm) {
		expired_mm = expiredMm;
	}
	public String getExpired_yy() {
		return expired_yy;
	}
	public void setExpired_yy(String expiredYy) {
		expired_yy = expiredYy;
	}
	public Date getCall_date() {
		return call_date;
	}
	public void setCall_date(Date callDate) {
		call_date = callDate;
	}
	public String getTm_code() {
		return tm_code;
	}
	public void setTm_code(String tmCode) {
		tm_code = tmCode;
	}
	public Integer getFlag_cek() {
		return flag_cek;
	}
	public void setFlag_cek(Integer flagCek) {
		flag_cek = flagCek;
	}
	public Integer getPosisi() {
		return posisi;
	}
	public void setPosisi(Integer posisi) {
		this.posisi = posisi;
	}
	public Integer getStatus_polis() {
		return status_polis;
	}
	public void setStatus_polis(Integer statusPolis) {
		status_polis = statusPolis;
	}
	public Date getBeg_date() {
		return beg_date;
	}
	public void setBeg_date(Date begDate) {
		beg_date = begDate;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date endDate) {
		end_date = endDate;
	}
	public Integer getFlag_edit() {
		return flag_edit;
	}
	public void setFlag_edit(Integer flagEdit) {
		flag_edit = flagEdit;
	}
	public Integer getFlag_next_bill() {
		return flag_next_bill;
	}
	public void setFlag_next_bill(Integer flagNextBill) {
		flag_next_bill = flagNextBill;
	}
	public Date getTgl_next_bill() {
		return tgl_next_bill;
	}
	public void setTgl_next_bill(Date tglNextBill) {
		tgl_next_bill = tglNextBill;
	}
	public Date getTgl_input() {
		return tgl_input;
	}
	public void setTgl_input(Date tglInput) {
		tgl_input = tglInput;
	}
	public Date getTgl_export() {
		return tgl_export;
	}
	public void setTgl_export(Date tglExport) {
		tgl_export = tglExport;
	}
	public Date getTgl_print() {
		return tgl_print;
	}
	public void setTgl_print(Date tglPrint) {
		tgl_print = tglPrint;
	}
	public Date getTgl_ttp() {
		return tgl_ttp;
	}
	public void setTgl_ttp(Date tglTtp) {
		tgl_ttp = tglTtp;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostal_code() {
		return postal_code;
	}
	public void setPostal_code(String postalCode) {
		postal_code = postalCode;
	}
	public String getHome_area() {
		return home_area;
	}
	public void setHome_area(String homeArea) {
		home_area = homeArea;
	}
	public String getHome_phone() {
		return home_phone;
	}
	public void setHome_phone(String homePhone) {
		home_phone = homePhone;
	}
	public String getWork_are() {
		return work_are;
	}
	public void setWork_are(String workAre) {
		work_are = workAre;
	}
	public String getWork_phone() {
		return work_phone;
	}
	public void setWork_phone(String workPhone) {
		work_phone = workPhone;
	}
	public String getFax_area() {
		return fax_area;
	}
	public void setFax_area(String faxArea) {
		fax_area = faxArea;
	}
	public String getFax_no() {
		return fax_no;
	}
	public void setFax_no(String faxNo) {
		fax_no = faxNo;
	}
	public String getFax_warea() {
		return fax_warea;
	}
	public void setFax_warea(String faxWarea) {
		fax_warea = faxWarea;
	}
	public String getFax_wno() {
		return fax_wno;
	}
	public void setFax_wno(String faxWno) {
		fax_wno = faxWno;
	}
	public String getMobile_no() {
		return mobile_no;
	}
	public void setMobile_no(String mobileNo) {
		mobile_no = mobileNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCard_type() {
		return card_type;
	}
	public void setCard_type(String cardType) {
		card_type = cardType;
	}
	public Integer getLside_id() {
		return lside_id;
	}
	public void setLside_id(Integer lsideId) {
		lside_id = lsideId;
	}
	public Integer getFlag_refund_via() {
		return flag_refund_via;
	}
	public void setFlag_refund_via(Integer flagRefundVia) {
		flag_refund_via = flagRefundVia;
	}
	public Integer getFlag_disc() {
		return flag_disc;
	}
	public void setFlag_disc(Integer flagDisc) {
		flag_disc = flagDisc;
	}
	public String getVirtual_account() {
		return virtual_account;
	}
	public void setVirtual_account(String virtualAccount) {
		virtual_account = virtualAccount;
	}
	public Integer getFlag_db() {
		return flag_db;
	}
	public void setFlag_db(Integer flagDb) {
		flag_db = flagDb;
	}
	public Double getTm_premi() {
		return tm_premi;
	}
	public void setTm_premi(Double tmPremi) {
		tm_premi = tmPremi;
	}
	public String getMgs_nik() {
		return mgs_nik;
	}
	public void setMgs_nik(String mgsNik) {
		mgs_nik = mgsNik;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getFlag_koresp_sama() {
		return flag_koresp_sama;
	}
	public void setFlag_koresp_sama(Integer flagKorespSama) {
		flag_koresp_sama = flagKorespSama;
	}
	public String getMspo_plan_provider() {
		return mspo_plan_provider;
	}
	public void setMspo_plan_provider(String mspoPlanProvider) {
		mspo_plan_provider = mspoPlanProvider;
	}
	public Integer getUsia() {
		return usia;
	}
	public void setUsia(Integer usia) {
		this.usia = usia;
	}
	
	

}
