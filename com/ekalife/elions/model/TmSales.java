package com.ekalife.elions.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class TmSales implements Serializable{
	
	/**
	 * 
	 * @author : Andy
	 * @since : Oct 16, 2014 (2:40:36 PM)
	 */
	private static final long serialVersionUID = 8676313410882531692L;
	
	private Date tgl_sales;
	private String campaign;
	private String no_sertifikat;
	private String card_type;
	private String card_no;
	private String cust_no;
	private String holder_name;
	private Integer sex;
	private Date bod_holder;
	private String bod_holder2;
	private Integer age;
	private String mobile_no;
	private String product_code;
	private BigDecimal premium;
	private BigDecimal sum_insured;
	private Date beg_date;
	private String beg_date2;
	private Date end_date;
	private String end_date2;
	private String bill_mode;
	private Integer bill_freq;
	private Date call_date;
	private Date tgl_input;
	private Integer flag_cek;
	private Integer posisi;
	private Integer status;
	private String email;
	private String application_id;
	private String address1;
	private String address2;
	private String home_phone;
	private String work_phone;
	private Integer sts_nikah;
	private String pekerjaan;
	private String tempat_lahir;
	// Field2 khusus bank DKI
	private String no_akun;
	private String tipe_akun;
	private String id_customer;
	private Date tgl_buka_akun;
	private String kd_cabang;
	// =========================
	
	
	public Date getTgl_sales() {
		return tgl_sales;
	}
	public void setTgl_sales(Date tgl_sales) {
		this.tgl_sales = tgl_sales;
	}
	public String getCampaign() {
		return campaign;
	}
	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}
	public String getNo_sertifikat() {
		return no_sertifikat;
	}
	public void setNo_sertifikat(String no_sertifikat) {
		this.no_sertifikat = no_sertifikat;
	}
	public String getCard_type() {
		return card_type;
	}
	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	public String getCust_no() {
		return cust_no;
	}
	public void setCust_no(String cust_no) {
		this.cust_no = cust_no;
	}
	public String getHolder_name() {
		return holder_name;
	}
	public void setHolder_name(String holder_name) {
		this.holder_name = holder_name;
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
	public void setBod_holder(Date bod_holder) {
		this.bod_holder = bod_holder;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getMobile_no() {
		return mobile_no;
	}
	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public BigDecimal getPremium() {
		return premium;
	}
	public void setPremium(BigDecimal premium) {
		this.premium = premium;
	}
	public BigDecimal getSum_insured() {
		return sum_insured;
	}
	public void setSum_insured(BigDecimal sum_insured) {
		this.sum_insured = sum_insured;
	}
	public Date getBeg_date() {
		return beg_date;
	}
	public void setBeg_date(Date beg_date) {
		this.beg_date = beg_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public String getBill_mode() {
		return bill_mode;
	}
	public void setBill_mode(String bill_mode) {
		this.bill_mode = bill_mode;
	}
	public Integer getBill_freq() {
		return bill_freq;
	}
	public void setBill_freq(Integer bill_freq) {
		this.bill_freq = bill_freq;
	}
	public Date getCall_date() {
		return call_date;
	}
	public void setCall_date(Date call_date) {
		this.call_date = call_date;
	}
	public Date getTgl_input() {
		return tgl_input;
	}
	public void setTgl_input(Date tgl_input) {
		this.tgl_input = tgl_input;
	}
	public Integer getFlag_cek() {
		return flag_cek;
	}
	public void setFlag_cek(Integer flag_cek) {
		this.flag_cek = flag_cek;
	}
	public Integer getPosisi() {
		return posisi;
	}
	public void setPosisi(Integer posisi) {
		this.posisi = posisi;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getApplication_id() {
		return application_id;
	}
	public void setApplication_id(String application_id) {
		this.application_id = application_id;
	}
	public String getBod_holder2() {
		return bod_holder2;
	}
	public void setBod_holder2(String bod_holder2) {
		this.bod_holder2 = bod_holder2;
	}
	public String getBeg_date2() {
		return beg_date2;
	}
	public void setBeg_date2(String beg_date2) {
		this.beg_date2 = beg_date2;
	}
	public String getEnd_date2() {
		return end_date2;
	}
	public void setEnd_date2(String end_date2) {
		this.end_date2 = end_date2;
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
    
    public String getHome_phone() {
        return home_phone;
    }
    
    public void setHome_phone(String home_phone) {
        this.home_phone = home_phone;
    }
    
    public String getWork_phone() {
        return work_phone;
    }
    
    public void setWork_phone(String work_phone) {
        this.work_phone = work_phone;
    }
    public Integer getSts_nikah() {
        return sts_nikah;
    }
    
    public void setSts_nikah(Integer sts_nikah) {
        this.sts_nikah = sts_nikah;
    }
    
    public String getPekerjaan() {
        return pekerjaan;
    }
    
    public void setPekerjaan(String pekerjaan) {
        this.pekerjaan = pekerjaan;
    }
    
    public String getTempat_lahir() {
        return tempat_lahir;
    }
    
    public void setTempat_lahir(String tempat_lahir) {
        this.tempat_lahir = tempat_lahir;
    }
    
    public String getNo_akun() {
        return no_akun;
    }
    
    public void setNo_akun(String no_akun) {
        this.no_akun = no_akun;
    }
    
    public String getTipe_akun() {
        return tipe_akun;
    }
    
    public void setTipe_akun(String tipe_akun) {
        this.tipe_akun = tipe_akun;
    }
    
    public String getId_customer() {
        return id_customer;
    }
    
    public void setId_customer(String id_customer) {
        this.id_customer = id_customer;
    }
    
    public Date getTgl_buka_akun() {
        return tgl_buka_akun;
    }
    
    public void setTgl_buka_akun(Date tgl_buka_akun) {
        this.tgl_buka_akun = tgl_buka_akun;
    }
    
    public String getKd_cabang() {
        return kd_cabang;
    }
    
    public void setKd_cabang(String kd_cabang) {
        this.kd_cabang = kd_cabang;
    }
	
	
}
