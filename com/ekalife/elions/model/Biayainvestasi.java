package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Biayainvestasi implements Serializable {

	private static final long serialVersionUID = 1L;
	

	private Integer mu_ke;                         
	private Integer ljb_id;                        
	private Double mbu_jumlah;                     
	private Double mbu_persen;                     
	private String ljb_biaya;                      
	private String reg_spaj;
	private Date mbu_end_pay ;
	private String flag_jenis;
	public Date getMbu_end_pay() {
		return mbu_end_pay;
	}
	public void setMbu_end_pay(Date mbu_end_pay) {
		this.mbu_end_pay = mbu_end_pay;
	}
	public String getLjb_biaya() {
		return ljb_biaya;
	}
	public void setLjb_biaya(String ljb_biaya) {
		this.ljb_biaya = ljb_biaya;
	}
	public Integer getLjb_id() {
		return ljb_id;
	}
	public void setLjb_id(Integer ljb_id) {
		this.ljb_id = ljb_id;
	}
	public Double getMbu_jumlah() {
		return mbu_jumlah;
	}
	public void setMbu_jumlah(Double mbu_jumlah) {
		this.mbu_jumlah = mbu_jumlah;
	}
	public Double getMbu_persen() {
		return mbu_persen;
	}
	public void setMbu_persen(Double mbu_persen) {
		this.mbu_persen = mbu_persen;
	}
	public Integer getMu_ke() {
		return mu_ke;
	}
	public void setMu_ke(Integer mu_ke) {
		this.mu_ke = mu_ke;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getFlag_jenis() {
		return flag_jenis;
	}
	public void setFlag_jenis(String flag_jenis) {
		this.flag_jenis = flag_jenis;
	} 
	
}
