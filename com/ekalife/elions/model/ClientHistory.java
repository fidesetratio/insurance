/**
 * @author  : Ferry Harlim
 * @created : Aug 13, 2007 1:54:11 PM
 */
package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class ClientHistory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mspo_policy_no;                 
	private Integer msch_no_urut;                  
	private Integer lssh_id;
	private String lssh_name;
	private String msch_no_seri;                   
	private String msch_nama_pp;                   
	private String msch_nama_tt;                   
	private String msch_nama_produk;               
	private String msch_cabang;                    
	private String msch_alamat;                    
	private String msch_kota;                      
	private String  msch_zip_code;                 
	private Date msch_tgl_kirim;                   
	private Date msch_tgl_terima;                  
	private String msch_penerima;                  
	private Integer lus_id;
	private Date msch_tgl_upload;
	private Date msch_bas_tgl_terima;
	private Date msch_bas_tgl_input;               
	private Date msch_tgl_follow_up;               
	private String msch_job_ord;                   

	private String msch_history;
	private Integer msch_status_input;
	
	private Integer retour;                        
	private Integer kirim;                         
	private String msap_area_code1;                
	private String msap_phone1;                    
	private String msap_area_code2;                
	private String msap_phone2;                    
	private String msap_area_code3;                
	private String msap_phone3;                    
	private String no_hp;                          
	private String no_hp2;         
	
	
	public Integer getMsch_status_input() {
		return msch_status_input;
	}
	public void setMsch_status_input(Integer msch_status_input) {
		this.msch_status_input = msch_status_input;
	}
	public String getMsch_history() {
		return msch_history;
	}
	public void setMsch_history(String msch_history) {
		this.msch_history = msch_history;
	}
	
	public Date getMsch_bas_tgl_terima() {
		return msch_bas_tgl_terima;
	}
	public void setMsch_bas_tgl_terima(Date msch_bas_tgl_terima) {
		this.msch_bas_tgl_terima = msch_bas_tgl_terima;
	}
	public Date getMsch_tgl_upload() {
		return msch_tgl_upload;
	}
	public void setMsch_tgl_upload(Date msch_tgl_upload) {
		this.msch_tgl_upload = msch_tgl_upload;
	}
	public Integer getLssh_id() {
		return lssh_id;
	}
	public void setLssh_id(Integer lssh_id) {
		this.lssh_id = lssh_id;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getMsch_alamat() {
		return msch_alamat;
	}
	public void setMsch_alamat(String msch_alamat) {
		this.msch_alamat = msch_alamat;
	}
	public String getMsch_cabang() {
		return msch_cabang;
	}
	public void setMsch_cabang(String msch_cabang) {
		this.msch_cabang = msch_cabang;
	}
	
	public String getMsch_kota() {
		return msch_kota;
	}
	public void setMsch_kota(String msch_kota) {
		this.msch_kota = msch_kota;
	}
	public String getMsch_nama_pp() {
		return msch_nama_pp;
	}
	public void setMsch_nama_pp(String msch_nama_pp) {
		this.msch_nama_pp = msch_nama_pp;
	}
	public String getMsch_nama_produk() {
		return msch_nama_produk;
	}
	public void setMsch_nama_produk(String msch_nama_produk) {
		this.msch_nama_produk = msch_nama_produk;
	}
	public String getMsch_nama_tt() {
		return msch_nama_tt;
	}
	public void setMsch_nama_tt(String msch_nama_tt) {
		this.msch_nama_tt = msch_nama_tt;
	}
	public String getMsch_no_seri() {
		return msch_no_seri;
	}
	public void setMsch_no_seri(String msch_no_seri) {
		this.msch_no_seri = msch_no_seri;
	}
	public Integer getMsch_no_urut() {
		return msch_no_urut;
	}
	public void setMsch_no_urut(Integer msch_no_urut) {
		this.msch_no_urut = msch_no_urut;
	}
	public String getMsch_penerima() {
		return msch_penerima;
	}
	public void setMsch_penerima(String msch_penerima) {
		this.msch_penerima = msch_penerima;
	}
	public Date getMsch_tgl_kirim() {
		return msch_tgl_kirim;
	}
	public void setMsch_tgl_kirim(Date msch_tgl_kirim) {
		this.msch_tgl_kirim = msch_tgl_kirim;
	}
	public Date getMsch_tgl_terima() {
		return msch_tgl_terima;
	}
	public void setMsch_tgl_terima(Date msch_tgl_terima) {
		this.msch_tgl_terima = msch_tgl_terima;
	}
	
	public String getMsch_zip_code() {
		return msch_zip_code;
	}
	public void setMsch_zip_code(String msch_zip_code) {
		this.msch_zip_code = msch_zip_code;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public Integer getKirim() {
		return kirim;
	}
	public void setKirim(Integer kirim) {
		this.kirim = kirim;
	}
	public String getMsap_area_code1() {
		return msap_area_code1;
	}
	public void setMsap_area_code1(String msap_area_code1) {
		this.msap_area_code1 = msap_area_code1;
	}
	public String getMsap_area_code2() {
		return msap_area_code2;
	}
	public void setMsap_area_code2(String msap_area_code2) {
		this.msap_area_code2 = msap_area_code2;
	}
	public String getMsap_area_code3() {
		return msap_area_code3;
	}
	public void setMsap_area_code3(String msap_area_code3) {
		this.msap_area_code3 = msap_area_code3;
	}
	public String getMsap_phone1() {
		return msap_phone1;
	}
	public void setMsap_phone1(String msap_phone1) {
		this.msap_phone1 = msap_phone1;
	}
	public String getMsap_phone2() {
		return msap_phone2;
	}
	public void setMsap_phone2(String msap_phone2) {
		this.msap_phone2 = msap_phone2;
	}
	public String getMsap_phone3() {
		return msap_phone3;
	}
	public void setMsap_phone3(String msap_phone3) {
		this.msap_phone3 = msap_phone3;
	}
	public String getNo_hp() {
		return no_hp;
	}
	public void setNo_hp(String no_hp) {
		this.no_hp = no_hp;
	}
	public String getNo_hp2() {
		return no_hp2;
	}
	public void setNo_hp2(String no_hp2) {
		this.no_hp2 = no_hp2;
	}
	public Integer getRetour() {
		return retour;
	}
	public void setRetour(Integer retour) {
		this.retour = retour;
	}
	public String getLssh_name() {
		return lssh_name;
	}
	public void setLssh_name(String lssh_name) {
		this.lssh_name = lssh_name;
	}
	public Date getMsch_bas_tgl_input() {
		return msch_bas_tgl_input;
	}
	public void setMsch_bas_tgl_input(Date msch_bas_tgl_input) {
		this.msch_bas_tgl_input = msch_bas_tgl_input;
	}
	public String getMsch_job_ord() {
		return msch_job_ord;
	}
	public void setMsch_job_ord(String msch_job_ord) {
		this.msch_job_ord = msch_job_ord;
	}
	public Date getMsch_tgl_follow_up() {
		return msch_tgl_follow_up;
	}
	public void setMsch_tgl_follow_up(Date msch_tgl_follow_up) {
		this.msch_tgl_follow_up = msch_tgl_follow_up;
	}     
}
