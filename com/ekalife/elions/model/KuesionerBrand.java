package com.ekalife.elions.model;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class KuesionerBrand implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String lus_id;
	private Integer mkb_cr_msig_1;        
	private Integer mkb_cr_msig_2;
	private Integer mkb_cr_msig_3;
	private Integer mkb_cr_msig_4;
	private Integer mkb_cr_msig_5;
	
	private Integer mkb_cr_aset_1;      
	private Integer mkb_cr_aset_2;  
	private Integer mkb_cr_aset_3;  
	private Integer mkb_cr_aset_4;  
	private Integer mkb_cr_aset_5;  
	
	private Integer mkb_cr_pelanggan_1;   
	private Integer mkb_cr_pelanggan_2; 
	private Integer mkb_cr_pelanggan_3; 
	private Integer mkb_cr_pelanggan_4; 
	private Integer mkb_cr_pelanggan_5; 
	
	private Integer mkb_cr_prima_1;       
	private Integer mkb_cr_prima_2;  
	private Integer mkb_cr_prima_3;  
	private Integer mkb_cr_prima_4;  
	private Integer mkb_cr_prima_5;  
	
	private Integer mkb_pr_terbaik_1;     
	private Integer mkb_pr_terbaik_2;     
	private Integer mkb_pr_terbaik_3;     
	private Integer mkb_pr_terbaik_4;     
	private Integer mkb_pr_terbaik_5;     
	
	private Integer mkb_pr_fleksibilias_1;
	private Integer mkb_pr_fleksibilias_2;
	private Integer mkb_pr_fleksibilias_3;
	private Integer mkb_pr_fleksibilias_4;
	private Integer mkb_pr_fleksibilias_5;
	
	private Integer mkb_pr_asuransi_1;    
	private Integer mkb_pr_asuransi_2;    
	private Integer mkb_pr_asuransi_3;    
	private Integer mkb_pr_asuransi_4;    
	private Integer mkb_pr_asuransi_5;    
	
	private Integer mkb_pr_produk_1;      
	private Integer mkb_pr_produk_2;   
	private Integer mkb_pr_produk_3;   
	private Integer mkb_pr_produk_4;   
	private Integer mkb_pr_produk_5;   
	
	private Integer mkb_im_internasional_1;
	private Integer mkb_im_internasional_2;
	private Integer mkb_im_internasional_3;
	private Integer mkb_im_internasional_4;
	private Integer mkb_im_internasional_5;
	
	private Integer mkb_im_positif_1;    
	private Integer mkb_im_positif_2;
	private Integer mkb_im_positif_3;
	private Integer mkb_im_positif_4;
	private Integer mkb_im_positif_5;
	
	private Integer mkb_im_moderen_1;     
	private Integer mkb_im_moderen_2; 
	private Integer mkb_im_moderen_3; 
	private Integer mkb_im_moderen_4; 
	private Integer mkb_im_moderen_5; 
	
	private Integer mkb_im_komitmen_1;    
	private Integer mkb_im_komitmen_2;    
	private Integer mkb_im_komitmen_3;    
	private Integer mkb_im_komitmen_4;    
	private Integer mkb_im_komitmen_5;    
	
	private Integer mkb_em_waspada_1;     
	private Integer mkb_em_waspada_2;     
	private Integer mkb_em_waspada_3;     
	private Integer mkb_em_waspada_4;     
	private Integer mkb_em_waspada_5;     
	
	private Integer mkb_em_kenyamanan_1;  
	private Integer mkb_em_kenyamanan_2;  
	private Integer mkb_em_kenyamanan_3;  
	private Integer mkb_em_kenyamanan_4;  
	private Integer mkb_em_kenyamanan_5;  
	
	private Integer mkb_em_pengertian_1;  
	private Integer mkb_em_pengertian_2;  
	private Integer mkb_em_pengertian_3;  
	private Integer mkb_em_pengertian_4;  
	private Integer mkb_em_pengertian_5;  
	
	private Integer mkb_em_ketulusan_1; 
	private Integer mkb_em_ketulusan_2; 
	private Integer mkb_em_ketulusan_3; 
	private Integer mkb_em_ketulusan_4; 
	private Integer mkb_em_ketulusan_5; 
	
	private Date mkb_input_date;
	
	private List<DropDown> corporateList;
	private List<DropDown> emotionalList;
	private List<DropDown> productList;
	private List<DropDown> imageList;
	public String getLus_id() {
		return lus_id;
	}
	public void setLus_id(String lus_id) {
		this.lus_id = lus_id;
	}
	public Integer getMkb_cr_msig_1() {
		return mkb_cr_msig_1;
	}
	public void setMkb_cr_msig_1(Integer mkb_cr_msig_1) {
		this.mkb_cr_msig_1 = mkb_cr_msig_1;
	}
	public Integer getMkb_cr_msig_2() {
		return mkb_cr_msig_2;
	}
	public void setMkb_cr_msig_2(Integer mkb_cr_msig_2) {
		this.mkb_cr_msig_2 = mkb_cr_msig_2;
	}
	public Integer getMkb_cr_msig_3() {
		return mkb_cr_msig_3;
	}
	public void setMkb_cr_msig_3(Integer mkb_cr_msig_3) {
		this.mkb_cr_msig_3 = mkb_cr_msig_3;
	}
	public Integer getMkb_cr_msig_4() {
		return mkb_cr_msig_4;
	}
	public void setMkb_cr_msig_4(Integer mkb_cr_msig_4) {
		this.mkb_cr_msig_4 = mkb_cr_msig_4;
	}
	public Integer getMkb_cr_msig_5() {
		return mkb_cr_msig_5;
	}
	public void setMkb_cr_msig_5(Integer mkb_cr_msig_5) {
		this.mkb_cr_msig_5 = mkb_cr_msig_5;
	}
	public Integer getMkb_cr_aset_1() {
		return mkb_cr_aset_1;
	}
	public void setMkb_cr_aset_1(Integer mkb_cr_aset_1) {
		this.mkb_cr_aset_1 = mkb_cr_aset_1;
	}
	public Integer getMkb_cr_aset_2() {
		return mkb_cr_aset_2;
	}
	public void setMkb_cr_aset_2(Integer mkb_cr_aset_2) {
		this.mkb_cr_aset_2 = mkb_cr_aset_2;
	}
	public Integer getMkb_cr_aset_3() {
		return mkb_cr_aset_3;
	}
	public void setMkb_cr_aset_3(Integer mkb_cr_aset_3) {
		this.mkb_cr_aset_3 = mkb_cr_aset_3;
	}
	public Integer getMkb_cr_aset_4() {
		return mkb_cr_aset_4;
	}
	public void setMkb_cr_aset_4(Integer mkb_cr_aset_4) {
		this.mkb_cr_aset_4 = mkb_cr_aset_4;
	}
	public Integer getMkb_cr_aset_5() {
		return mkb_cr_aset_5;
	}
	public void setMkb_cr_aset_5(Integer mkb_cr_aset_5) {
		this.mkb_cr_aset_5 = mkb_cr_aset_5;
	}
	public Integer getMkb_cr_pelanggan_1() {
		return mkb_cr_pelanggan_1;
	}
	public void setMkb_cr_pelanggan_1(Integer mkb_cr_pelanggan_1) {
		this.mkb_cr_pelanggan_1 = mkb_cr_pelanggan_1;
	}
	public Integer getMkb_cr_pelanggan_2() {
		return mkb_cr_pelanggan_2;
	}
	public void setMkb_cr_pelanggan_2(Integer mkb_cr_pelanggan_2) {
		this.mkb_cr_pelanggan_2 = mkb_cr_pelanggan_2;
	}
	public Integer getMkb_cr_pelanggan_3() {
		return mkb_cr_pelanggan_3;
	}
	public void setMkb_cr_pelanggan_3(Integer mkb_cr_pelanggan_3) {
		this.mkb_cr_pelanggan_3 = mkb_cr_pelanggan_3;
	}
	public Integer getMkb_cr_pelanggan_4() {
		return mkb_cr_pelanggan_4;
	}
	public void setMkb_cr_pelanggan_4(Integer mkb_cr_pelanggan_4) {
		this.mkb_cr_pelanggan_4 = mkb_cr_pelanggan_4;
	}
	public Integer getMkb_cr_pelanggan_5() {
		return mkb_cr_pelanggan_5;
	}
	public void setMkb_cr_pelanggan_5(Integer mkb_cr_pelanggan_5) {
		this.mkb_cr_pelanggan_5 = mkb_cr_pelanggan_5;
	}
	public Integer getMkb_cr_prima_1() {
		return mkb_cr_prima_1;
	}
	public void setMkb_cr_prima_1(Integer mkb_cr_prima_1) {
		this.mkb_cr_prima_1 = mkb_cr_prima_1;
	}
	public Integer getMkb_cr_prima_2() {
		return mkb_cr_prima_2;
	}
	public void setMkb_cr_prima_2(Integer mkb_cr_prima_2) {
		this.mkb_cr_prima_2 = mkb_cr_prima_2;
	}
	public Integer getMkb_cr_prima_3() {
		return mkb_cr_prima_3;
	}
	public void setMkb_cr_prima_3(Integer mkb_cr_prima_3) {
		this.mkb_cr_prima_3 = mkb_cr_prima_3;
	}
	public Integer getMkb_cr_prima_4() {
		return mkb_cr_prima_4;
	}
	public void setMkb_cr_prima_4(Integer mkb_cr_prima_4) {
		this.mkb_cr_prima_4 = mkb_cr_prima_4;
	}
	public Integer getMkb_cr_prima_5() {
		return mkb_cr_prima_5;
	}
	public void setMkb_cr_prima_5(Integer mkb_cr_prima_5) {
		this.mkb_cr_prima_5 = mkb_cr_prima_5;
	}
	public Integer getMkb_pr_terbaik_1() {
		return mkb_pr_terbaik_1;
	}
	public void setMkb_pr_terbaik_1(Integer mkb_pr_terbaik_1) {
		this.mkb_pr_terbaik_1 = mkb_pr_terbaik_1;
	}
	public Integer getMkb_pr_terbaik_2() {
		return mkb_pr_terbaik_2;
	}
	public void setMkb_pr_terbaik_2(Integer mkb_pr_terbaik_2) {
		this.mkb_pr_terbaik_2 = mkb_pr_terbaik_2;
	}
	public Integer getMkb_pr_terbaik_3() {
		return mkb_pr_terbaik_3;
	}
	public void setMkb_pr_terbaik_3(Integer mkb_pr_terbaik_3) {
		this.mkb_pr_terbaik_3 = mkb_pr_terbaik_3;
	}
	public Integer getMkb_pr_terbaik_4() {
		return mkb_pr_terbaik_4;
	}
	public void setMkb_pr_terbaik_4(Integer mkb_pr_terbaik_4) {
		this.mkb_pr_terbaik_4 = mkb_pr_terbaik_4;
	}
	public Integer getMkb_pr_terbaik_5() {
		return mkb_pr_terbaik_5;
	}
	public void setMkb_pr_terbaik_5(Integer mkb_pr_terbaik_5) {
		this.mkb_pr_terbaik_5 = mkb_pr_terbaik_5;
	}
	public Integer getMkb_pr_fleksibilias_1() {
		return mkb_pr_fleksibilias_1;
	}
	public void setMkb_pr_fleksibilias_1(Integer mkb_pr_fleksibilias_1) {
		this.mkb_pr_fleksibilias_1 = mkb_pr_fleksibilias_1;
	}
	public Integer getMkb_pr_fleksibilias_2() {
		return mkb_pr_fleksibilias_2;
	}
	public void setMkb_pr_fleksibilias_2(Integer mkb_pr_fleksibilias_2) {
		this.mkb_pr_fleksibilias_2 = mkb_pr_fleksibilias_2;
	}
	public Integer getMkb_pr_fleksibilias_3() {
		return mkb_pr_fleksibilias_3;
	}
	public void setMkb_pr_fleksibilias_3(Integer mkb_pr_fleksibilias_3) {
		this.mkb_pr_fleksibilias_3 = mkb_pr_fleksibilias_3;
	}
	public Integer getMkb_pr_fleksibilias_4() {
		return mkb_pr_fleksibilias_4;
	}
	public void setMkb_pr_fleksibilias_4(Integer mkb_pr_fleksibilias_4) {
		this.mkb_pr_fleksibilias_4 = mkb_pr_fleksibilias_4;
	}
	public Integer getMkb_pr_fleksibilias_5() {
		return mkb_pr_fleksibilias_5;
	}
	public void setMkb_pr_fleksibilias_5(Integer mkb_pr_fleksibilias_5) {
		this.mkb_pr_fleksibilias_5 = mkb_pr_fleksibilias_5;
	}
	public Integer getMkb_pr_asuransi_1() {
		return mkb_pr_asuransi_1;
	}
	public void setMkb_pr_asuransi_1(Integer mkb_pr_asuransi_1) {
		this.mkb_pr_asuransi_1 = mkb_pr_asuransi_1;
	}
	public Integer getMkb_pr_asuransi_2() {
		return mkb_pr_asuransi_2;
	}
	public void setMkb_pr_asuransi_2(Integer mkb_pr_asuransi_2) {
		this.mkb_pr_asuransi_2 = mkb_pr_asuransi_2;
	}
	public Integer getMkb_pr_asuransi_3() {
		return mkb_pr_asuransi_3;
	}
	public void setMkb_pr_asuransi_3(Integer mkb_pr_asuransi_3) {
		this.mkb_pr_asuransi_3 = mkb_pr_asuransi_3;
	}
	public Integer getMkb_pr_asuransi_4() {
		return mkb_pr_asuransi_4;
	}
	public void setMkb_pr_asuransi_4(Integer mkb_pr_asuransi_4) {
		this.mkb_pr_asuransi_4 = mkb_pr_asuransi_4;
	}
	public Integer getMkb_pr_asuransi_5() {
		return mkb_pr_asuransi_5;
	}
	public void setMkb_pr_asuransi_5(Integer mkb_pr_asuransi_5) {
		this.mkb_pr_asuransi_5 = mkb_pr_asuransi_5;
	}
	public Integer getMkb_pr_produk_1() {
		return mkb_pr_produk_1;
	}
	public void setMkb_pr_produk_1(Integer mkb_pr_produk_1) {
		this.mkb_pr_produk_1 = mkb_pr_produk_1;
	}
	public Integer getMkb_pr_produk_2() {
		return mkb_pr_produk_2;
	}
	public void setMkb_pr_produk_2(Integer mkb_pr_produk_2) {
		this.mkb_pr_produk_2 = mkb_pr_produk_2;
	}
	public Integer getMkb_pr_produk_3() {
		return mkb_pr_produk_3;
	}
	public void setMkb_pr_produk_3(Integer mkb_pr_produk_3) {
		this.mkb_pr_produk_3 = mkb_pr_produk_3;
	}
	public Integer getMkb_pr_produk_4() {
		return mkb_pr_produk_4;
	}
	public void setMkb_pr_produk_4(Integer mkb_pr_produk_4) {
		this.mkb_pr_produk_4 = mkb_pr_produk_4;
	}
	public Integer getMkb_pr_produk_5() {
		return mkb_pr_produk_5;
	}
	public void setMkb_pr_produk_5(Integer mkb_pr_produk_5) {
		this.mkb_pr_produk_5 = mkb_pr_produk_5;
	}
	public Integer getMkb_im_internasional_1() {
		return mkb_im_internasional_1;
	}
	public void setMkb_im_internasional_1(Integer mkb_im_internasional_1) {
		this.mkb_im_internasional_1 = mkb_im_internasional_1;
	}
	public Integer getMkb_im_internasional_2() {
		return mkb_im_internasional_2;
	}
	public void setMkb_im_internasional_2(Integer mkb_im_internasional_2) {
		this.mkb_im_internasional_2 = mkb_im_internasional_2;
	}
	public Integer getMkb_im_internasional_3() {
		return mkb_im_internasional_3;
	}
	public void setMkb_im_internasional_3(Integer mkb_im_internasional_3) {
		this.mkb_im_internasional_3 = mkb_im_internasional_3;
	}
	public Integer getMkb_im_internasional_4() {
		return mkb_im_internasional_4;
	}
	public void setMkb_im_internasional_4(Integer mkb_im_internasional_4) {
		this.mkb_im_internasional_4 = mkb_im_internasional_4;
	}
	public Integer getMkb_im_internasional_5() {
		return mkb_im_internasional_5;
	}
	public void setMkb_im_internasional_5(Integer mkb_im_internasional_5) {
		this.mkb_im_internasional_5 = mkb_im_internasional_5;
	}
	public Integer getMkb_im_positif_1() {
		return mkb_im_positif_1;
	}
	public void setMkb_im_positif_1(Integer mkb_im_positif_1) {
		this.mkb_im_positif_1 = mkb_im_positif_1;
	}
	public Integer getMkb_im_positif_2() {
		return mkb_im_positif_2;
	}
	public void setMkb_im_positif_2(Integer mkb_im_positif_2) {
		this.mkb_im_positif_2 = mkb_im_positif_2;
	}
	public Integer getMkb_im_positif_3() {
		return mkb_im_positif_3;
	}
	public void setMkb_im_positif_3(Integer mkb_im_positif_3) {
		this.mkb_im_positif_3 = mkb_im_positif_3;
	}
	public Integer getMkb_im_positif_4() {
		return mkb_im_positif_4;
	}
	public void setMkb_im_positif_4(Integer mkb_im_positif_4) {
		this.mkb_im_positif_4 = mkb_im_positif_4;
	}
	public Integer getMkb_im_positif_5() {
		return mkb_im_positif_5;
	}
	public void setMkb_im_positif_5(Integer mkb_im_positif_5) {
		this.mkb_im_positif_5 = mkb_im_positif_5;
	}
	public Integer getMkb_im_moderen_1() {
		return mkb_im_moderen_1;
	}
	public void setMkb_im_moderen_1(Integer mkb_im_moderen_1) {
		this.mkb_im_moderen_1 = mkb_im_moderen_1;
	}
	public Integer getMkb_im_moderen_2() {
		return mkb_im_moderen_2;
	}
	public void setMkb_im_moderen_2(Integer mkb_im_moderen_2) {
		this.mkb_im_moderen_2 = mkb_im_moderen_2;
	}
	public Integer getMkb_im_moderen_3() {
		return mkb_im_moderen_3;
	}
	public void setMkb_im_moderen_3(Integer mkb_im_moderen_3) {
		this.mkb_im_moderen_3 = mkb_im_moderen_3;
	}
	public Integer getMkb_im_moderen_4() {
		return mkb_im_moderen_4;
	}
	public void setMkb_im_moderen_4(Integer mkb_im_moderen_4) {
		this.mkb_im_moderen_4 = mkb_im_moderen_4;
	}
	public Integer getMkb_im_moderen_5() {
		return mkb_im_moderen_5;
	}
	public void setMkb_im_moderen_5(Integer mkb_im_moderen_5) {
		this.mkb_im_moderen_5 = mkb_im_moderen_5;
	}
	public Integer getMkb_im_komitmen_1() {
		return mkb_im_komitmen_1;
	}
	public void setMkb_im_komitmen_1(Integer mkb_im_komitmen_1) {
		this.mkb_im_komitmen_1 = mkb_im_komitmen_1;
	}
	public Integer getMkb_im_komitmen_2() {
		return mkb_im_komitmen_2;
	}
	public void setMkb_im_komitmen_2(Integer mkb_im_komitmen_2) {
		this.mkb_im_komitmen_2 = mkb_im_komitmen_2;
	}
	public Integer getMkb_im_komitmen_3() {
		return mkb_im_komitmen_3;
	}
	public void setMkb_im_komitmen_3(Integer mkb_im_komitmen_3) {
		this.mkb_im_komitmen_3 = mkb_im_komitmen_3;
	}
	public Integer getMkb_im_komitmen_4() {
		return mkb_im_komitmen_4;
	}
	public void setMkb_im_komitmen_4(Integer mkb_im_komitmen_4) {
		this.mkb_im_komitmen_4 = mkb_im_komitmen_4;
	}
	public Integer getMkb_im_komitmen_5() {
		return mkb_im_komitmen_5;
	}
	public void setMkb_im_komitmen_5(Integer mkb_im_komitmen_5) {
		this.mkb_im_komitmen_5 = mkb_im_komitmen_5;
	}
	public Integer getMkb_em_waspada_1() {
		return mkb_em_waspada_1;
	}
	public void setMkb_em_waspada_1(Integer mkb_em_waspada_1) {
		this.mkb_em_waspada_1 = mkb_em_waspada_1;
	}
	public Integer getMkb_em_waspada_2() {
		return mkb_em_waspada_2;
	}
	public void setMkb_em_waspada_2(Integer mkb_em_waspada_2) {
		this.mkb_em_waspada_2 = mkb_em_waspada_2;
	}
	public Integer getMkb_em_waspada_3() {
		return mkb_em_waspada_3;
	}
	public void setMkb_em_waspada_3(Integer mkb_em_waspada_3) {
		this.mkb_em_waspada_3 = mkb_em_waspada_3;
	}
	public Integer getMkb_em_waspada_4() {
		return mkb_em_waspada_4;
	}
	public void setMkb_em_waspada_4(Integer mkb_em_waspada_4) {
		this.mkb_em_waspada_4 = mkb_em_waspada_4;
	}
	public Integer getMkb_em_waspada_5() {
		return mkb_em_waspada_5;
	}
	public void setMkb_em_waspada_5(Integer mkb_em_waspada_5) {
		this.mkb_em_waspada_5 = mkb_em_waspada_5;
	}
	public Integer getMkb_em_kenyamanan_1() {
		return mkb_em_kenyamanan_1;
	}
	public void setMkb_em_kenyamanan_1(Integer mkb_em_kenyamanan_1) {
		this.mkb_em_kenyamanan_1 = mkb_em_kenyamanan_1;
	}
	public Integer getMkb_em_kenyamanan_2() {
		return mkb_em_kenyamanan_2;
	}
	public void setMkb_em_kenyamanan_2(Integer mkb_em_kenyamanan_2) {
		this.mkb_em_kenyamanan_2 = mkb_em_kenyamanan_2;
	}
	public Integer getMkb_em_kenyamanan_3() {
		return mkb_em_kenyamanan_3;
	}
	public void setMkb_em_kenyamanan_3(Integer mkb_em_kenyamanan_3) {
		this.mkb_em_kenyamanan_3 = mkb_em_kenyamanan_3;
	}
	public Integer getMkb_em_kenyamanan_4() {
		return mkb_em_kenyamanan_4;
	}
	public void setMkb_em_kenyamanan_4(Integer mkb_em_kenyamanan_4) {
		this.mkb_em_kenyamanan_4 = mkb_em_kenyamanan_4;
	}
	public Integer getMkb_em_kenyamanan_5() {
		return mkb_em_kenyamanan_5;
	}
	public void setMkb_em_kenyamanan_5(Integer mkb_em_kenyamanan_5) {
		this.mkb_em_kenyamanan_5 = mkb_em_kenyamanan_5;
	}
	public Integer getMkb_em_pengertian_1() {
		return mkb_em_pengertian_1;
	}
	public void setMkb_em_pengertian_1(Integer mkb_em_pengertian_1) {
		this.mkb_em_pengertian_1 = mkb_em_pengertian_1;
	}
	public Integer getMkb_em_pengertian_2() {
		return mkb_em_pengertian_2;
	}
	public void setMkb_em_pengertian_2(Integer mkb_em_pengertian_2) {
		this.mkb_em_pengertian_2 = mkb_em_pengertian_2;
	}
	public Integer getMkb_em_pengertian_3() {
		return mkb_em_pengertian_3;
	}
	public void setMkb_em_pengertian_3(Integer mkb_em_pengertian_3) {
		this.mkb_em_pengertian_3 = mkb_em_pengertian_3;
	}
	public Integer getMkb_em_pengertian_4() {
		return mkb_em_pengertian_4;
	}
	public void setMkb_em_pengertian_4(Integer mkb_em_pengertian_4) {
		this.mkb_em_pengertian_4 = mkb_em_pengertian_4;
	}
	public Integer getMkb_em_pengertian_5() {
		return mkb_em_pengertian_5;
	}
	public void setMkb_em_pengertian_5(Integer mkb_em_pengertian_5) {
		this.mkb_em_pengertian_5 = mkb_em_pengertian_5;
	}
	public Integer getMkb_em_ketulusan_1() {
		return mkb_em_ketulusan_1;
	}
	public void setMkb_em_ketulusan_1(Integer mkb_em_ketulusan_1) {
		this.mkb_em_ketulusan_1 = mkb_em_ketulusan_1;
	}
	public Integer getMkb_em_ketulusan_2() {
		return mkb_em_ketulusan_2;
	}
	public void setMkb_em_ketulusan_2(Integer mkb_em_ketulusan_2) {
		this.mkb_em_ketulusan_2 = mkb_em_ketulusan_2;
	}
	public Integer getMkb_em_ketulusan_3() {
		return mkb_em_ketulusan_3;
	}
	public void setMkb_em_ketulusan_3(Integer mkb_em_ketulusan_3) {
		this.mkb_em_ketulusan_3 = mkb_em_ketulusan_3;
	}
	public Integer getMkb_em_ketulusan_4() {
		return mkb_em_ketulusan_4;
	}
	public void setMkb_em_ketulusan_4(Integer mkb_em_ketulusan_4) {
		this.mkb_em_ketulusan_4 = mkb_em_ketulusan_4;
	}
	public Integer getMkb_em_ketulusan_5() {
		return mkb_em_ketulusan_5;
	}
	public void setMkb_em_ketulusan_5(Integer mkb_em_ketulusan_5) {
		this.mkb_em_ketulusan_5 = mkb_em_ketulusan_5;
	}
	public Date getMkb_input_date() {
		return mkb_input_date;
	}
	public void setMkb_input_date(Date mkb_input_date) {
		this.mkb_input_date = mkb_input_date;
	}
	public List<DropDown> getCorporateList() {
		return corporateList;
	}
	public void setCorporateList(List<DropDown> corporateList) {
		this.corporateList = corporateList;
	}
	public List<DropDown> getEmotionalList() {
		return emotionalList;
	}
	public void setEmotionalList(List<DropDown> emotionalList) {
		this.emotionalList = emotionalList;
	}
	public List<DropDown> getProductList() {
		return productList;
	}
	public void setProductList(List<DropDown> productList) {
		this.productList = productList;
	}
	public List<DropDown> getImageList() {
		return imageList;
	}
	public void setImageList(List<DropDown> imageList) {
		this.imageList = imageList;
	}
	
	
	
}
