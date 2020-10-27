package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TmmsBill implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 3593520840534641439L;

	private String id;                             
	private Integer premi_ke;                      
	private Integer tahun_ke;                      
	private Date beg_date;                         
	private Date end_date;                         
	private Double jumlah_premi;                   
	private Double jumlah_disc;                    
	private Integer flag_paid;                     
	private Integer posisi;                        
	private Date tgl_bayar;                        
	private Date tgl_prod;                         
	private Date tgl_input;                        
	private String no_kwitansi;                    
	private Double jumlah_komisi;                  
	private String bill_mode;                      
	private Double jumlah_bayar;                   
	private Date tgl_input_bayar;                  
	private Integer user_input;                    
	private Date tgl_rk2;                          
	private Double jumlah_bayar2;                  
	private Date tgl_rk3;  
	private Double jumlah_bayar3;                  
	private Integer flag_db;                       
	private Date tgl_paid;                         
	private Integer flag_recur;                    
	private String flag_db_note;                   
	private Integer flag_aktif;                    
	private Date tgl_bsb;                          
	private Date tgl_refund;                       
	private Date tgl_input_refund;                 
	private Integer user_refund;                   
	private Integer flag_refund;                   
	private Integer flag_biaya_ref1;               
	private String ket_biaya_ref1;                 
	private Double biaya_ref1;                     
	private Integer flag_biaya_ref2;               
	private String ket_biaya_ref2;                 
	private Double biaya_ref2;                     
	private Date tgl_bayar_refund;                 
	private Integer user_bayar_refund;             
	private Date tgl_input_bayar_refund;
	
	
	private String lku_id;
	private List<TmmsDBill> tmmsDBill;
	
	public TmmsBill(){
		tmmsDBill= new ArrayList<TmmsDBill>();
		tmmsDBill.add(new TmmsDBill());
		tmmsDBill.add(new TmmsDBill());
	}
	
	public TmmsBill(String product_code){
		tmmsDBill= new ArrayList<TmmsDBill>();
		tmmsDBill.add(new TmmsDBill());
		if(product_code!=null){
			if (product_code.equals("019")) {
				tmmsDBill.add(new TmmsDBill());		
			}
		}
	}
	
	public Date getBeg_date() {
		return beg_date;
	}
	public void setBeg_date(Date beg_date) {
		this.beg_date = beg_date;
	}
	public Double getBiaya_ref1() {
		return biaya_ref1;
	}
	public void setBiaya_ref1(Double biaya_ref1) {
		this.biaya_ref1 = biaya_ref1;
	}
	public Double getBiaya_ref2() {
		return biaya_ref2;
	}
	public void setBiaya_ref2(Double biaya_ref2) {
		this.biaya_ref2 = biaya_ref2;
	}
	public String getBill_mode() {
		return bill_mode;
	}
	public void setBill_mode(String bill_mode) {
		this.bill_mode = bill_mode;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public Integer getFlag_aktif() {
		return flag_aktif;
	}
	public void setFlag_aktif(Integer flag_aktif) {
		this.flag_aktif = flag_aktif;
	}
	public Integer getFlag_biaya_ref1() {
		return flag_biaya_ref1;
	}
	public void setFlag_biaya_ref1(Integer flag_biaya_ref1) {
		this.flag_biaya_ref1 = flag_biaya_ref1;
	}
	public Integer getFlag_biaya_ref2() {
		return flag_biaya_ref2;
	}
	public void setFlag_biaya_ref2(Integer flag_biaya_ref2) {
		this.flag_biaya_ref2 = flag_biaya_ref2;
	}
	public Integer getFlag_db() {
		return flag_db;
	}
	public void setFlag_db(Integer flag_db) {
		this.flag_db = flag_db;
	}
	public String getFlag_db_note() {
		return flag_db_note;
	}
	public void setFlag_db_note(String flag_db_note) {
		this.flag_db_note = flag_db_note;
	}
	public Integer getFlag_paid() {
		return flag_paid;
	}
	public void setFlag_paid(Integer flag_paid) {
		this.flag_paid = flag_paid;
	}
	public Integer getFlag_recur() {
		return flag_recur;
	}
	public void setFlag_recur(Integer flag_recur) {
		this.flag_recur = flag_recur;
	}
	public Integer getFlag_refund() {
		return flag_refund;
	}
	public void setFlag_refund(Integer flag_refund) {
		this.flag_refund = flag_refund;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Double getJumlah_bayar() {
		return jumlah_bayar;
	}
	public void setJumlah_bayar(Double jumlah_bayar) {
		this.jumlah_bayar = jumlah_bayar;
	}
	public Double getJumlah_bayar2() {
		return jumlah_bayar2;
	}
	public void setJumlah_bayar2(Double jumlah_bayar2) {
		this.jumlah_bayar2 = jumlah_bayar2;
	}
	public Double getJumlah_bayar3() {
		return jumlah_bayar3;
	}
	public void setJumlah_bayar3(Double jumlah_bayar3) {
		this.jumlah_bayar3 = jumlah_bayar3;
	}
	public Double getJumlah_disc() {
		return jumlah_disc;
	}
	public void setJumlah_disc(Double jumlah_disc) {
		this.jumlah_disc = jumlah_disc;
	}
	public Double getJumlah_komisi() {
		return jumlah_komisi;
	}
	public void setJumlah_komisi(Double jumlah_komisi) {
		this.jumlah_komisi = jumlah_komisi;
	}
	public Double getJumlah_premi() {
		return jumlah_premi;
	}
	public void setJumlah_premi(Double jumlah_premi) {
		this.jumlah_premi = jumlah_premi;
	}
	public String getKet_biaya_ref1() {
		return ket_biaya_ref1;
	}
	public void setKet_biaya_ref1(String ket_biaya_ref1) {
		this.ket_biaya_ref1 = ket_biaya_ref1;
	}
	public String getKet_biaya_ref2() {
		return ket_biaya_ref2;
	}
	public void setKet_biaya_ref2(String ket_biaya_ref2) {
		this.ket_biaya_ref2 = ket_biaya_ref2;
	}
	public String getNo_kwitansi() {
		return no_kwitansi;
	}
	public void setNo_kwitansi(String no_kwitansi) {
		this.no_kwitansi = no_kwitansi;
	}
	public Integer getPosisi() {
		return posisi;
	}
	public void setPosisi(Integer posisi) {
		this.posisi = posisi;
	}
	public Integer getPremi_ke() {
		return premi_ke;
	}
	public void setPremi_ke(Integer premi_ke) {
		this.premi_ke = premi_ke;
	}
	public Integer getTahun_ke() {
		return tahun_ke;
	}
	public void setTahun_ke(Integer tahun_ke) {
		this.tahun_ke = tahun_ke;
	}
	public Date getTgl_bayar() {
		return tgl_bayar;
	}
	public void setTgl_bayar(Date tgl_bayar) {
		this.tgl_bayar = tgl_bayar;
	}
	public Date getTgl_bayar_refund() {
		return tgl_bayar_refund;
	}
	public void setTgl_bayar_refund(Date tgl_bayar_refund) {
		this.tgl_bayar_refund = tgl_bayar_refund;
	}
	public Date getTgl_bsb() {
		return tgl_bsb;
	}
	public void setTgl_bsb(Date tgl_bsb) {
		this.tgl_bsb = tgl_bsb;
	}
	public Date getTgl_input() {
		return tgl_input;
	}
	public void setTgl_input(Date tgl_input) {
		this.tgl_input = tgl_input;
	}
	public Date getTgl_input_bayar() {
		return tgl_input_bayar;
	}
	public void setTgl_input_bayar(Date tgl_input_bayar) {
		this.tgl_input_bayar = tgl_input_bayar;
	}
	public Date getTgl_input_bayar_refund() {
		return tgl_input_bayar_refund;
	}
	public void setTgl_input_bayar_refund(Date tgl_input_bayar_refund) {
		this.tgl_input_bayar_refund = tgl_input_bayar_refund;
	}
	public Date getTgl_input_refund() {
		return tgl_input_refund;
	}
	public void setTgl_input_refund(Date tgl_input_refund) {
		this.tgl_input_refund = tgl_input_refund;
	}
	public Date getTgl_paid() {
		return tgl_paid;
	}
	public void setTgl_paid(Date tgl_paid) {
		this.tgl_paid = tgl_paid;
	}
	public Date getTgl_prod() {
		return tgl_prod;
	}
	public void setTgl_prod(Date tgl_prod) {
		this.tgl_prod = tgl_prod;
	}
	public Date getTgl_refund() {
		return tgl_refund;
	}
	public void setTgl_refund(Date tgl_refund) {
		this.tgl_refund = tgl_refund;
	}
	public Date getTgl_rk2() {
		return tgl_rk2;
	}
	public void setTgl_rk2(Date tgl_rk2) {
		this.tgl_rk2 = tgl_rk2;
	}
	public Date getTgl_rk3() {
		return tgl_rk3;
	}
	public void setTgl_rk3(Date tgl_rk3) {
		this.tgl_rk3 = tgl_rk3;
	}
	public Integer getUser_bayar_refund() {
		return user_bayar_refund;
	}
	public void setUser_bayar_refund(Integer user_bayar_refund) {
		this.user_bayar_refund = user_bayar_refund;
	}
	public Integer getUser_input() {
		return user_input;
	}
	public void setUser_input(Integer user_input) {
		this.user_input = user_input;
	}
	public Integer getUser_refund() {
		return user_refund;
	}
	public void setUser_refund(Integer user_refund) {
		this.user_refund = user_refund;
	}
	public List<TmmsDBill> getTmmsDBill() {
		return tmmsDBill;
	}
	public void setTmmsDBill(List<TmmsDBill> tmmsDBill) {
		this.tmmsDBill = tmmsDBill;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}           

	
}
