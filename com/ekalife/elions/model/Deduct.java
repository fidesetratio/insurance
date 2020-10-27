/**
 * @author  : Ferry Harlim
 * @created : Mar 15, 2007 
 */
package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Deduct implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String msco_id;                        
	private Integer msdd_number;                   
	private Integer lsjd_id;                       
	private Date msdd_date;                        
	private Double msdd_deduct;                    
	private Double msdd_tax;                       
	private String msdd_desc;                      
	private Integer lus_id;                        
	private Integer lsbs_id;                       
	private Integer lsdbs_number;                  
	private Double pot_upp;                        
	private Double pot_biaya;                      
	private String pot_desc;                       
	private String pot_spaj;
	//
	private String nama_agen;
	private String kode_agen;
	private String cabang;
	private String nama_product;
	private Double total;
	private Double total2;
	private Double pot_extra;
	private String spaj;
	private String new_spaj;
	private String nopolis;
	private Integer flag;
	private List lsDetProduk;
	private String posisi;
	private String jns_lisensi;
	
	public String getJns_lisensi() {
		return jns_lisensi;
	}
	public void setJns_lisensi(String jns_lisensi) {
		this.jns_lisensi = jns_lisensi;
	}
	public String getPosisi() {
		return posisi;
	}
	public void setPosisi(String posisi) {
		this.posisi = posisi;
	}
	public String getNopolis() {
		return nopolis;
	}
	public void setNopolis(String nopolis) {
		this.nopolis = nopolis;
	}
	public String getNew_spaj() {
		return new_spaj;
	}
	public void setNew_spaj(String new_spaj) {
		this.new_spaj = new_spaj;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getNama_product() {
		return nama_product;
	}
	public void setNama_product(String nama_product) {
		this.nama_product = nama_product;
	}
	public Double getPot_extra() {
		return pot_extra;
	}
	public void setPot_extra(Double pot_extra) {
		this.pot_extra = pot_extra;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	public Integer getLsjd_id() {
		return lsjd_id;
	}
	public void setLsjd_id(Integer lsjd_id) {
		this.lsjd_id = lsjd_id;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getMsco_id() {
		return msco_id;
	}
	public void setMsco_id(String msco_id) {
		this.msco_id = msco_id;
	}
	public Date getMsdd_date() {
		return msdd_date;
	}
	public void setMsdd_date(Date msdd_date) {
		this.msdd_date = msdd_date;
	}
	public Double getMsdd_deduct() {
		return msdd_deduct;
	}
	public void setMsdd_deduct(Double msdd_deduct) {
		this.msdd_deduct = msdd_deduct;
	}
	public String getMsdd_desc() {
		return msdd_desc;
	}
	public void setMsdd_desc(String msdd_desc) {
		this.msdd_desc = msdd_desc;
	}
	public Integer getMsdd_number() {
		return msdd_number;
	}
	public void setMsdd_number(Integer msdd_number) {
		this.msdd_number = msdd_number;
	}
	public Double getMsdd_tax() {
		return msdd_tax;
	}
	public void setMsdd_tax(Double msdd_tax) {
		this.msdd_tax = msdd_tax;
	}
	public Double getPot_biaya() {
		return pot_biaya;
	}
	public void setPot_biaya(Double pot_biaya) {
		this.pot_biaya = pot_biaya;
	}
	public String getPot_desc() {
		return pot_desc;
	}
	public void setPot_desc(String pot_desc) {
		this.pot_desc = pot_desc;
	}
	public String getPot_spaj() {
		return pot_spaj;
	}
	public void setPot_spaj(String pot_spaj) {
		this.pot_spaj = pot_spaj;
	}
	public Double getPot_upp() {
		return pot_upp;
	}
	public void setPot_upp(Double pot_upp) {
		this.pot_upp = pot_upp;
	}
	public String getCabang() {
		return cabang;
	}
	public void setCabang(String cabang) {
		this.cabang = cabang;
	}
	public String getKode_agen() {
		return kode_agen;
	}
	public void setKode_agen(String kode_agen) {
		this.kode_agen = kode_agen;
	}
	public String getNama_agen() {
		return nama_agen;
	}
	public void setNama_agen(String nama_agen) {
		this.nama_agen = nama_agen;
	}
	public String getSpaj() {
		return spaj;
	}
	public void setSpaj(String spaj) {
		this.spaj = spaj;
	}
	public List getLsDetProduk() {
		return lsDetProduk;
	}
	public void setLsDetProduk(List lsDetProduk) {
		this.lsDetProduk = lsDetProduk;
	}
	public Double getTotal2() {
		return total2;
	}
	public void setTotal2(Double total2) {
		this.total2 = total2;
	}    
}
