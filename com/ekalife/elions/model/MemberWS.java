package com.ekalife.elions.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ibatis.common.resources.Resources;

public class MemberWS implements Serializable{
	
	protected final Log logger = LogFactory.getLog( getClass() );
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4335646620112960697L;
	private String member_id;                      
	private String mspe_no_identity;               
	private String mobile;                         
	private String telp;   
	private String area_telp;
	private String no_telp;
	private String email;                          
	private String cc; 
	private String cc_1;
	private String cc_2;
	private String cc_3;
	private String cc_4;
	private Integer cc_type;                       
	private String name_on_cc;                     
	private String cc_exp;  
	private String cc_exp_mm;
	private String cc_exp_yyyy;
	private String first_name;  
	private String mother_maiden_name;             
	private Integer gender;                        
	private Date bod;                              
	private Integer flag_new_member;               
	private Integer lside_id;                      
	private Integer trxid;
	private String status_trx;                     
	private String status_trx_txt;                 
	private Integer update_member;                 
	private Integer hit_count_new;                 
	private Integer hit_count_update;              
	private Integer kode_program;                  
	private Integer lus_id;                        
	private Integer cc_mode;                       
	private String status_trx_update;              
	private String status_trx_txt_update;          
	private Date create_date;    
	private int flag_default;
	private int flag_cc_new;
	private byte[] cc_byte; 
	private byte[] name_on_cc_byte; 
	private byte[] cc_exp_byte;
	private byte[] mother_maiden_name_byte;
	private Integer flag_update_or_new; //update=1 new =0
	private Integer flag_update_or_new_CC; //update=1 new =0
	private Integer id_einsurance;//id key ke mysql
	
	//REFERENCE MODEL
	private List<BillingWS> daftarBill;
	private List<CCDataWS> daftarCard;
	private BillingWS billWS;
	private CCDataWS cardWS;
	
	private String statusTransaksi;
	private Double fee_topup;
	private Double disc;
	private Double fee_regular;
	private Integer errMessage;
	private Integer konfirmasi;
	
	//tambahan
	private String j_captcha_id;
	private String j_captcha_response;
	
	private Double grandTotal;
	
	
	public Double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

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

	public MemberWS(){
		Properties p=null;
		try {
			 p = Resources.getResourceAsProperties("policy.properties");
		} catch (IOException e) {
			
			logger.error("ERROR :", e);
		}
		daftarBill=new ArrayList<BillingWS>();
		daftarCard=new ArrayList<CCDataWS>();
		cardWS=new CCDataWS();
		billWS=new BillingWS();
		this.fee_topup=new Double(p.getProperty("nilai.fee_topup"));
		this.fee_regular=new Double(p.getProperty("nilai.fee"));
		this.disc=new Double(p.getProperty("nilai.disc"));
	}
	
	public Double getDisc() {
		return disc;
	}
	public void setDisc(Double disc) {
		this.disc = disc;
	}
	public Double getFee_topup() {
		return fee_topup;
	}
	public void setFee_topup(Double fee_topup) {
		this.fee_topup = fee_topup;
	}
	public String getStatusTransaksi() {
		return statusTransaksi;
	}
	public void setStatusTransaksi(String statusTransaksi) {
		this.statusTransaksi = statusTransaksi;
	}
	public Integer getFlag_update_or_new() {
		return flag_update_or_new;
	}
	public void setFlag_update_or_new(Integer flag_update_or_new) {
		this.flag_update_or_new = flag_update_or_new;
	}
	public byte[] getCc_byte() {
		return cc_byte;
	}
	public void setCc_byte(byte[] cc_byte) {
		this.cc_byte = cc_byte;
	}
	public byte[] getCc_exp_byte() {
		return cc_exp_byte;
	}
	public void setCc_exp_byte(byte[] cc_exp_byte) {
		this.cc_exp_byte = cc_exp_byte;
	}
	public byte[] getMother_maiden_name_byte() {
		return mother_maiden_name_byte;
	}
	public void setMother_maiden_name_byte(byte[] mother_maiden_name_byte) {
		this.mother_maiden_name_byte = mother_maiden_name_byte;
	}
	public byte[] getName_on_cc_byte() {
		return name_on_cc_byte;
	}
	public void setName_on_cc_byte(byte[] name_on_cc_byte) {
		this.name_on_cc_byte = name_on_cc_byte;
	}
	public Date getBod() {
		return bod;
	}
	public void setBod(Date bod) {
		this.bod = bod;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
		if(cc!=null){
			this.cc_1=cc.substring(0,4);
			this.cc_2=cc.substring(4,8);
			this.cc_3=cc.substring(8,12);
			this.cc_4=cc.substring(12,16);
		}else{
			this.cc_1=null;
			this.cc_2=null;
			this.cc_3=null;
			this.cc_4=null;
		}
	}
	
	public String getCc_exp() {
		return cc_exp;
	}
	public void setCc_exp(String cc_exp) {
		this.cc_exp = cc_exp;
	}
	public Integer getCc_mode() {
		return cc_mode;
	}
	public void setCc_mode(Integer cc_mode) {
		this.cc_mode = cc_mode;
	}
	public Integer getCc_type() {
		return cc_type;
	}
	public void setCc_type(Integer cc_type) {
		this.cc_type = cc_type;
	}
	
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public int getFlag_cc_new() {
		return flag_cc_new;
	}
	public void setFlag_cc_new(int flag_cc_new) {
		this.flag_cc_new = flag_cc_new;
	}
	public int getFlag_default() {
		return flag_default;
	}
	public void setFlag_default(int flag_default) {
		this.flag_default = flag_default;
	}
	public Integer getFlag_new_member() {
		return flag_new_member;
	}
	public void setFlag_new_member(Integer flag_new_member) {
		this.flag_new_member = flag_new_member;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public Integer getHit_count_new() {
		return hit_count_new;
	}
	public void setHit_count_new(Integer hit_count_new) {
		this.hit_count_new = hit_count_new;
	}
	public Integer getHit_count_update() {
		return hit_count_update;
	}
	public void setHit_count_update(Integer hit_count_update) {
		this.hit_count_update = hit_count_update;
	}
	public Integer getKode_program() {
		return kode_program;
	}
	public void setKode_program(Integer kode_program) {
		this.kode_program = kode_program;
	}
	public Integer getLside_id() {
		return lside_id;
	}
	public void setLside_id(Integer lside_id) {
		this.lside_id = lside_id;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMother_maiden_name() {
		return mother_maiden_name;
	}
	public void setMother_maiden_name(String mother_maiden_name) {
		this.mother_maiden_name = mother_maiden_name;
	}
	public String getMspe_no_identity() {
		return mspe_no_identity;
	}
	public void setMspe_no_identity(String mspe_no_identity) {
		this.mspe_no_identity = mspe_no_identity;
	}
	public String getName_on_cc() {
		return name_on_cc;
	}
	public void setName_on_cc(String name_on_cc) {
		this.name_on_cc = name_on_cc;
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
	public String getStatus_trx_txt_update() {
		return status_trx_txt_update;
	}
	public void setStatus_trx_txt_update(String status_trx_txt_update) {
		this.status_trx_txt_update = status_trx_txt_update;
	}
	public String getStatus_trx_update() {
		return status_trx_update;
	}
	public void setStatus_trx_update(String status_trx_update) {
		this.status_trx_update = status_trx_update;
	}
	public String getTelp() {
		return telp;
	}
	public void setTelp(String telp) {
		this.telp = telp;
			if(telp!=null){
			String [] telpARR=telp.split("-");
			if(telpARR.length==2){
				this.area_telp=telpARR[0];
				this.no_telp=telpARR[1];
			}else{
				this.area_telp="";
				this.no_telp= telp;
			}
		}
	}
	public Integer getTrxid() {
		return trxid;
	}
	public void setTrxid(Integer trxid) {
		this.trxid = trxid;
	}
	public Integer getUpdate_member() {
		return update_member;
	}
	public void setUpdate_member(Integer update_member) {
		this.update_member = update_member;
	}
	public Integer getFlag_update_or_new_CC() {
		return flag_update_or_new_CC;
	}
	public void setFlag_update_or_new_CC(Integer flag_update_or_new_CC) {
		this.flag_update_or_new_CC = flag_update_or_new_CC;
	}
	public BillingWS getBillWS() {
		return billWS;
	}
	public void setBillWS(BillingWS billWS) {
		this.billWS = billWS;
	}
	public CCDataWS getCardWS() {
		return cardWS;
	}
	public void setCardWS(CCDataWS cardWS) {
		this.cardWS = cardWS;
	}
	public List<BillingWS> getDaftarBill() {
		return daftarBill;
	}
	public void setDaftarBill(List<BillingWS> daftarBill) {
		this.daftarBill = daftarBill;
	}

	public Integer getErrMessage() {
		return errMessage;
	}
	
	/**
	 * NOTE : 1=echo test error; 2. bill in proses
	 * @author Berto
	 * @since Oct 14, 2008 11:48:35 AM
	 * @param errMessage
	 */
	public void setErrMessage(Integer errMessage) {
		this.errMessage = errMessage;
	}

	public String getArea_telp() {
		return area_telp;
	}

	public void setArea_telp(String area_telp) {
		this.area_telp = area_telp;
	}

	public String getNo_telp() {
		return no_telp;
	}

	public void setNo_telp(String no_telp) {
		this.no_telp = no_telp;
	}

	public List<CCDataWS> getDaftarCard() {
		return daftarCard;
	}

	public void setDaftarCard(List<CCDataWS> daftarCard) {
		this.daftarCard = daftarCard;
	}

	public String getCc_1() {
		return cc_1;
	}

	public void setCc_1(String cc_1) {
		this.cc_1 = cc_1;
	}

	public String getCc_2() {
		return cc_2;
	}

	public void setCc_2(String cc_2) {
		this.cc_2 = cc_2;
	}

	public String getCc_3() {
		return cc_3;
	}

	public void setCc_3(String cc_3) {
		this.cc_3 = cc_3;
	}

	public String getCc_4() {
		return cc_4;
	}

	public void setCc_4(String cc_4) {
		this.cc_4 = cc_4;
	}

	public String getCc_exp_mm() {
		return cc_exp_mm;
	}

	public void setCc_exp_mm(String cc_exp_mm) {
		this.cc_exp_mm = cc_exp_mm;
	}

	public String getCc_exp_yyyy() {
		return cc_exp_yyyy;
	}

	public void setCc_exp_yyyy(String cc_exp_yyyy) {
		this.cc_exp_yyyy = cc_exp_yyyy;
	}

	public Double getFee_regular() {
		return fee_regular;
	}

	public void setFee_regular(Double fee_regular) {
		this.fee_regular = fee_regular;
	}

	public Integer getId_einsurance() {
		return id_einsurance;
	}

	public void setId_einsurance(Integer id_einsurance) {
		this.id_einsurance = id_einsurance;
	}
	 
	


	
	
}
