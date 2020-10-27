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

public class Cplan implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2120981781545177444L;
	

	private String no_id;		
	private String msp_id;	
	private Integer jenis_cp;		
	private String no_rek;	
	private String kanwill_id;	
	private Integer lsbp_id;
	private String full_name;		
	private String lku_id;			
	private Integer set_bulanan;		
	private Integer set_cplan;
	private Long total_premi;
	private Long lump_sum;	
	private Integer mcp_insper;	
	private String mcp_sex;		
	private Integer mcp_umur;		
	private Date mcp_tgl_lahir;	
	private Date mcp_beg_date;	
	private Date mcp_end_date;
	private Integer lssp_id;		
	private Integer mcp_sts_aksep;	
	private Integer mcp_standard;		
	private Integer mcp_medis;	
	private Date mcp_tgl_input;	
	private Date mcp_tgl_aksep;	
	private Date mcp_next_bill;
	private String mcp_ket_aksep;		
	private Integer lspd_id;	
	private Integer mcp_flag_sim;
	private Integer mcp_flag_reas;
	private Integer lc_id;	
	private Integer mcp_flag_bill;	
	private Date mcp_tgl_batal;
	private String mcp_alasan;		
	private Date tgl_death;		
	private Integer bayar_claim;		
	private Integer flag_bayar_thp;	
	private Integer mulai_bayar;	
	private Date tgl_next_thp;		
	private String kd_sales;		
	private String kd_cs;	
	private Integer mcp_tb;		
	private Integer mcp_bb;		
	private String address1;	
	private String address2;		
	private String address3;		
	private String city;		
	private String postal_code;
	private String telepon;	
	private String work_phone;	
	private String mobile_no;		
	private String email;		
	private String discount;	
	private String no_tterima;		
	private Integer flag_print;
	private Date tgl_print;	
	private Date mcp_tgl_lahir_pp;		
	private String status_pp;	
	private String tmp_lahir;	
	private String work_name;		
	private String work_address1;		
	private String work_address2;		
	private String work_city;	
	private String work_postal_code;	
	private String fax;	
	private String work_fax;		
	private String nama_rek;	
	private String bank_city;	
	private String mcp_gelar;		
	private Integer lsag_id;	
	private Integer lsed_id;		
	private Integer lside_id;	
	private String no_identitas;		
	private Integer kewarganegaraan;	
	private Integer work_salary;
	private String work_desc;	
	private String koresp_address1;	
	private String koresp_address2;		
	private String koresp_city;		
	private String koresp_postal_code;	
	private String koresp_phone;	
	private String koresp_email;	
	private String koresp_hp;	
	private String nama_tabungan;
	private String work_email;		
	private String work_hp;	
	private String msag_id;	
	private String no_ringkasan_polis;		
	private Integer lscb_id;	
	private String koresp_address3;	
	private Integer bill_mode;		
	private Integer cc_cvv;	
	private Date cc_exp;	
	private Integer flag_free_pa;	
	private String kd_wil;	
	private String reason;	
	private Integer flag_koresp_sama;		
	private String no_sertifikat;		
	private String no_sertifikat_pa;			
	private Date tgl_kirim_polis;	
	private Date beg_date_pa;	
	private Date end_date_pa;	
	private Date tgl_print_pa;		
	private String ip_ttp_email;	
	private Date tgl_ttp;
	private String reg_spaj;		
	
	//=========================
	
	private Integer status;

	public String getNo_id() {
		return no_id;
	}

	public void setNo_id(String noId) {
		no_id = noId;
	}

	public String getMsp_id() {
		return msp_id;
	}

	public void setMsp_id(String mspId) {
		msp_id = mspId;
	}

	public Integer getJenis_cp() {
		return jenis_cp;
	}

	public void setJenis_cp(Integer jenisCp) {
		jenis_cp = jenisCp;
	}

	public String getNo_rek() {
		return no_rek;
	}

	public void setNo_rek(String noRek) {
		no_rek = noRek;
	}

	public String getKanwill_id() {
		return kanwill_id;
	}

	public void setKanwill_id(String kanwillId) {
		kanwill_id = kanwillId;
	}

	public Integer getLsbp_id() {
		return lsbp_id;
	}

	public void setLsbp_id(Integer lsbpId) {
		lsbp_id = lsbpId;
	}

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String fullName) {
		full_name = fullName;
	}

	public String getLku_id() {
		return lku_id;
	}

	public void setLku_id(String lkuId) {
		lku_id = lkuId;
	}

	public Integer getSet_bulanan() {
		return set_bulanan;
	}

	public void setSet_bulanan(Integer setBulanan) {
		set_bulanan = setBulanan;
	}

	public Integer getSet_cplan() {
		return set_cplan;
	}

	public void setSet_cplan(Integer setCplan) {
		set_cplan = setCplan;
	}

	public Long getTotal_premi() {
		return total_premi;
	}

	public void setTotal_premi(Long totalPremi) {
		total_premi = totalPremi;
	}

	public Long getLump_sum() {
		return lump_sum;
	}

	public void setLump_sum(Long lumpSum) {
		lump_sum = lumpSum;
	}

	public Integer getMcp_insper() {
		return mcp_insper;
	}

	public void setMcp_insper(Integer mcpInsper) {
		mcp_insper = mcpInsper;
	}

	public String getMcp_sex() {
		return mcp_sex;
	}

	public void setMcp_sex(String mcpSex) {
		mcp_sex = mcpSex;
	}

	public Integer getMcp_umur() {
		return mcp_umur;
	}

	public void setMcp_umur(Integer mcpUmur) {
		mcp_umur = mcpUmur;
	}

	public Date getMcp_tgl_lahir() {
		return mcp_tgl_lahir;
	}

	public void setMcp_tgl_lahir(Date mcpTglLahir) {
		mcp_tgl_lahir = mcpTglLahir;
	}

	public Date getMcp_beg_date() {
		return mcp_beg_date;
	}

	public void setMcp_beg_date(Date mcpBegDate) {
		mcp_beg_date = mcpBegDate;
	}

	public Date getMcp_end_date() {
		return mcp_end_date;
	}

	public void setMcp_end_date(Date mcpEndDate) {
		mcp_end_date = mcpEndDate;
	}

	public Integer getLssp_id() {
		return lssp_id;
	}

	public void setLssp_id(Integer lsspId) {
		lssp_id = lsspId;
	}

	public Integer getMcp_sts_aksep() {
		return mcp_sts_aksep;
	}

	public void setMcp_sts_aksep(Integer mcpStsAksep) {
		mcp_sts_aksep = mcpStsAksep;
	}

	public Integer getMcp_standard() {
		return mcp_standard;
	}

	public void setMcp_standard(Integer mcpStandard) {
		mcp_standard = mcpStandard;
	}

	public Integer getMcp_medis() {
		return mcp_medis;
	}

	public void setMcp_medis(Integer mcpMedis) {
		mcp_medis = mcpMedis;
	}

	public Date getMcp_tgl_input() {
		return mcp_tgl_input;
	}

	public void setMcp_tgl_input(Date mcpTglInput) {
		mcp_tgl_input = mcpTglInput;
	}

	public Date getMcp_tgl_aksep() {
		return mcp_tgl_aksep;
	}

	public void setMcp_tgl_aksep(Date mcpTglAksep) {
		mcp_tgl_aksep = mcpTglAksep;
	}

	public Date getMcp_next_bill() {
		return mcp_next_bill;
	}

	public void setMcp_next_bill(Date mcpNextBill) {
		mcp_next_bill = mcpNextBill;
	}

	public String getMcp_ket_aksep() {
		return mcp_ket_aksep;
	}

	public void setMcp_ket_aksep(String mcpKetAksep) {
		mcp_ket_aksep = mcpKetAksep;
	}

	public Integer getLspd_id() {
		return lspd_id;
	}

	public void setLspd_id(Integer lspdId) {
		lspd_id = lspdId;
	}

	public Integer getMcp_flag_sim() {
		return mcp_flag_sim;
	}

	public void setMcp_flag_sim(Integer mcpFlagSim) {
		mcp_flag_sim = mcpFlagSim;
	}

	public Integer getMcp_flag_reas() {
		return mcp_flag_reas;
	}

	public void setMcp_flag_reas(Integer mcpFlagReas) {
		mcp_flag_reas = mcpFlagReas;
	}

	public Integer getLc_id() {
		return lc_id;
	}

	public void setLc_id(Integer lcId) {
		lc_id = lcId;
	}

	public Integer getMcp_flag_bill() {
		return mcp_flag_bill;
	}

	public void setMcp_flag_bill(Integer mcpFlagBill) {
		mcp_flag_bill = mcpFlagBill;
	}

	public Date getMcp_tgl_batal() {
		return mcp_tgl_batal;
	}

	public void setMcp_tgl_batal(Date mcpTglBatal) {
		mcp_tgl_batal = mcpTglBatal;
	}

	public String getMcp_alasan() {
		return mcp_alasan;
	}

	public void setMcp_alasan(String mcpAlasan) {
		mcp_alasan = mcpAlasan;
	}

	public Date getTgl_death() {
		return tgl_death;
	}

	public void setTgl_death(Date tglDeath) {
		tgl_death = tglDeath;
	}

	public Integer getBayar_claim() {
		return bayar_claim;
	}

	public void setBayar_claim(Integer bayarClaim) {
		bayar_claim = bayarClaim;
	}

	public Integer getFlag_bayar_thp() {
		return flag_bayar_thp;
	}

	public void setFlag_bayar_thp(Integer flagBayarThp) {
		flag_bayar_thp = flagBayarThp;
	}

	public Integer getMulai_bayar() {
		return mulai_bayar;
	}

	public void setMulai_bayar(Integer mulaiBayar) {
		mulai_bayar = mulaiBayar;
	}

	public Date getTgl_next_thp() {
		return tgl_next_thp;
	}

	public void setTgl_next_thp(Date tglNextThp) {
		tgl_next_thp = tglNextThp;
	}

	public String getKd_sales() {
		return kd_sales;
	}

	public void setKd_sales(String kdSales) {
		kd_sales = kdSales;
	}

	public String getKd_cs() {
		return kd_cs;
	}

	public void setKd_cs(String kdCs) {
		kd_cs = kdCs;
	}

	public Integer getMcp_tb() {
		return mcp_tb;
	}

	public void setMcp_tb(Integer mcpTb) {
		mcp_tb = mcpTb;
	}

	public Integer getMcp_bb() {
		return mcp_bb;
	}

	public void setMcp_bb(Integer mcpBb) {
		mcp_bb = mcpBb;
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

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
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

	public String getTelepon() {
		return telepon;
	}

	public void setTelepon(String telepon) {
		this.telepon = telepon;
	}

	public String getWork_phone() {
		return work_phone;
	}

	public void setWork_phone(String workPhone) {
		work_phone = workPhone;
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

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getNo_tterima() {
		return no_tterima;
	}

	public void setNo_tterima(String noTterima) {
		no_tterima = noTterima;
	}

	public Integer getFlag_print() {
		return flag_print;
	}

	public void setFlag_print(Integer flagPrint) {
		flag_print = flagPrint;
	}

	public Date getTgl_print() {
		return tgl_print;
	}

	public void setTgl_print(Date tglPrint) {
		tgl_print = tglPrint;
	}

	public Date getMcp_tgl_lahir_pp() {
		return mcp_tgl_lahir_pp;
	}

	public void setMcp_tgl_lahir_pp(Date mcpTglLahirPp) {
		mcp_tgl_lahir_pp = mcpTglLahirPp;
	}

	public String getStatus_pp() {
		return status_pp;
	}

	public void setStatus_pp(String statusPp) {
		status_pp = statusPp;
	}

	public String getTmp_lahir() {
		return tmp_lahir;
	}

	public void setTmp_lahir(String tmpLahir) {
		tmp_lahir = tmpLahir;
	}

	public String getWork_name() {
		return work_name;
	}

	public void setWork_name(String workName) {
		work_name = workName;
	}

	public String getWork_address1() {
		return work_address1;
	}

	public void setWork_address1(String workAddress1) {
		work_address1 = workAddress1;
	}

	public String getWork_address2() {
		return work_address2;
	}

	public void setWork_address2(String workAddress2) {
		work_address2 = workAddress2;
	}

	public String getWork_city() {
		return work_city;
	}

	public void setWork_city(String workCity) {
		work_city = workCity;
	}

	public String getWork_postal_code() {
		return work_postal_code;
	}

	public void setWork_postal_code(String workPostalCode) {
		work_postal_code = workPostalCode;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getWork_fax() {
		return work_fax;
	}

	public void setWork_fax(String workFax) {
		work_fax = workFax;
	}

	public String getNama_rek() {
		return nama_rek;
	}

	public void setNama_rek(String namaRek) {
		nama_rek = namaRek;
	}

	public String getBank_city() {
		return bank_city;
	}

	public void setBank_city(String bankCity) {
		bank_city = bankCity;
	}

	public String getMcp_gelar() {
		return mcp_gelar;
	}

	public void setMcp_gelar(String mcpGelar) {
		mcp_gelar = mcpGelar;
	}

	public Integer getLsag_id() {
		return lsag_id;
	}

	public void setLsag_id(Integer lsagId) {
		lsag_id = lsagId;
	}

	public Integer getLsed_id() {
		return lsed_id;
	}

	public void setLsed_id(Integer lsedId) {
		lsed_id = lsedId;
	}

	public Integer getLside_id() {
		return lside_id;
	}

	public void setLside_id(Integer lsideId) {
		lside_id = lsideId;
	}

	public String getNo_identitas() {
		return no_identitas;
	}

	public void setNo_identitas(String noIdentitas) {
		no_identitas = noIdentitas;
	}

	public Integer getKewarganegaraan() {
		return kewarganegaraan;
	}

	public void setKewarganegaraan(Integer kewarganegaraan) {
		this.kewarganegaraan = kewarganegaraan;
	}

	public Integer getWork_salary() {
		return work_salary;
	}

	public void setWork_salary(Integer workSalary) {
		work_salary = workSalary;
	}

	public String getWork_desc() {
		return work_desc;
	}

	public void setWork_desc(String workDesc) {
		work_desc = workDesc;
	}

	public String getKoresp_address1() {
		return koresp_address1;
	}

	public void setKoresp_address1(String korespAddress1) {
		koresp_address1 = korespAddress1;
	}

	public String getKoresp_address2() {
		return koresp_address2;
	}

	public void setKoresp_address2(String korespAddress2) {
		koresp_address2 = korespAddress2;
	}

	public String getKoresp_city() {
		return koresp_city;
	}

	public void setKoresp_city(String korespCity) {
		koresp_city = korespCity;
	}

	public String getKoresp_postal_code() {
		return koresp_postal_code;
	}

	public void setKoresp_postal_code(String korespPostalCode) {
		koresp_postal_code = korespPostalCode;
	}

	public String getKoresp_phone() {
		return koresp_phone;
	}

	public void setKoresp_phone(String korespPhone) {
		koresp_phone = korespPhone;
	}

	public String getKoresp_email() {
		return koresp_email;
	}

	public void setKoresp_email(String korespEmail) {
		koresp_email = korespEmail;
	}

	public String getKoresp_hp() {
		return koresp_hp;
	}

	public void setKoresp_hp(String korespHp) {
		koresp_hp = korespHp;
	}

	public String getNama_tabungan() {
		return nama_tabungan;
	}

	public void setNama_tabungan(String namaTabungan) {
		nama_tabungan = namaTabungan;
	}

	public String getWork_email() {
		return work_email;
	}

	public void setWork_email(String workEmail) {
		work_email = workEmail;
	}

	public String getWork_hp() {
		return work_hp;
	}

	public void setWork_hp(String workHp) {
		work_hp = workHp;
	}

	public String getMsag_id() {
		return msag_id;
	}

	public void setMsag_id(String msagId) {
		msag_id = msagId;
	}

	public String getNo_ringkasan_polis() {
		return no_ringkasan_polis;
	}

	public void setNo_ringkasan_polis(String noRingkasanPolis) {
		no_ringkasan_polis = noRingkasanPolis;
	}

	public Integer getLscb_id() {
		return lscb_id;
	}

	public void setLscb_id(Integer lscbId) {
		lscb_id = lscbId;
	}

	public String getKoresp_address3() {
		return koresp_address3;
	}

	public void setKoresp_address3(String korespAddress3) {
		koresp_address3 = korespAddress3;
	}

	public Integer getBill_mode() {
		return bill_mode;
	}

	public void setBill_mode(Integer billMode) {
		bill_mode = billMode;
	}

	public Integer getCc_cvv() {
		return cc_cvv;
	}

	public void setCc_cvv(Integer ccCvv) {
		cc_cvv = ccCvv;
	}

	public Date getCc_exp() {
		return cc_exp;
	}

	public void setCc_exp(Date ccExp) {
		cc_exp = ccExp;
	}

	public Integer getFlag_free_pa() {
		return flag_free_pa;
	}

	public void setFlag_free_pa(Integer flagFreePa) {
		flag_free_pa = flagFreePa;
	}

	public String getKd_wil() {
		return kd_wil;
	}

	public void setKd_wil(String kdWil) {
		kd_wil = kdWil;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getFlag_koresp_sama() {
		return flag_koresp_sama;
	}

	public void setFlag_koresp_sama(Integer flagKorespSama) {
		flag_koresp_sama = flagKorespSama;
	}

	public String getNo_sertifikat() {
		return no_sertifikat;
	}

	public void setNo_sertifikat(String noSertifikat) {
		no_sertifikat = noSertifikat;
	}

	public String getNo_sertifikat_pa() {
		return no_sertifikat_pa;
	}

	public void setNo_sertifikat_pa(String noSertifikatPa) {
		no_sertifikat_pa = noSertifikatPa;
	}

	public Date getTgl_kirim_polis() {
		return tgl_kirim_polis;
	}

	public void setTgl_kirim_polis(Date tglKirimPolis) {
		tgl_kirim_polis = tglKirimPolis;
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

	public String getIp_ttp_email() {
		return ip_ttp_email;
	}

	public void setIp_ttp_email(String ipTtpEmail) {
		ip_ttp_email = ipTtpEmail;
	}

	public Date getTgl_ttp() {
		return tgl_ttp;
	}

	public void setTgl_ttp(Date tglTtp) {
		tgl_ttp = tglTtp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public void setReg_spaj(String regSpaj) {
		reg_spaj = regSpaj;
	}	
	
	
	

}
