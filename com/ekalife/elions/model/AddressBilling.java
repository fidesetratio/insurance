package com.ekalife.elions.model;

import java.io.Serializable;

public class AddressBilling implements Serializable {

	private static final long serialVersionUID = 1L;
	
                   
	private Integer lskc_id;                       
	private Integer lskl_id;                       
                  
	private String lca_id;                         
	private String lwk_id;                         
	private String lsrg_id;                        
	
	private Integer lti_id;                        
	private String msap_contact;                   
	private String msap_address;                   
	private Integer lska_id;                       
	private String msap_zip_code;                  
	private String msap_area_code1;                
	private String msap_phone1;                    
	private String msap_area_code2;                
	private String msap_phone2;                    
	private String msap_area_code3;                
	private String msap_phone3;                    
	private String msap_area_code_fax1;            
	private String msap_fax1;                      
	private String msap_area_code_fax2;            
	private String msap_fax2;                      
	private Integer flag_cc;                       
	private String e_mail;                         
	private String no_hp;                          
	private String no_hp2;                         
	private String reg_spaj;                       
	private String kota_tgh;                           
	private String kota_tagih;                     
	private String region;
	private String tagih;
	private String region_name;
	private String lsrg_nama;
	private String lwk_nama;
	private String lca_nama;
	private Integer kode_kabupaten;
	private String kota;
	private Integer lsne_id;
	
	public String getKota() {
		return kota;
	}
	public void setKota(String kota) {
		this.kota = kota;
	}
	public String getRegion_name() {
		return region_name;
	}
	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
	public Integer getLskc_id() {
		return lskc_id;
	}
	public void setLskc_id(Integer lskc_id) {
		this.lskc_id = lskc_id;
	}
	public Integer getLskl_id() {
		return lskl_id;
	}
	public void setLskl_id(Integer lskl_id) {
		this.lskl_id = lskl_id;
	}
	public String getLsrg_id() {
		return lsrg_id;
	}
	public void setLsrg_id(String lsrg_id) {
		this.lsrg_id = lsrg_id;
	}
	public String getLwk_id() {
		return lwk_id;
	}
	public void setLwk_id(String lwk_id) {
		this.lwk_id = lwk_id;
	}
	public Integer getKode_kabupaten() {
		return kode_kabupaten;
	}
	public void setKode_kabupaten(Integer kode_kabupaten) {
		this.kode_kabupaten = kode_kabupaten;
	}
	public String getLsrg_nama() {
		return lsrg_nama;
	}
	public void setLsrg_nama(String lsrg_nama) {
		this.lsrg_nama = lsrg_nama;
	}
	public String getE_mail() {
		return e_mail;
	}
	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}
	public Integer getFlag_cc() {
		return flag_cc;
	}
	public void setFlag_cc(Integer flag_cc) {
		this.flag_cc = flag_cc;
	}
	public String getKota_tagih() {
		return kota_tagih;
	}
	public void setKota_tagih(String kota_tagih) {
		this.kota_tagih = kota_tagih;
	}
	public String getKota_tgh() {
		return kota_tgh;
	}
	public void setKota_tgh(String kota_tgh) {
		this.kota_tgh = kota_tgh;
	}
	public Integer getLska_id() {
		return lska_id;
	}
	public void setLska_id(Integer lska_id) {
		this.lska_id = lska_id;
	}
	public Integer getLti_id() {
		return lti_id;
	}
	public void setLti_id(Integer lti_id) {
		this.lti_id = lti_id;
	}
	public String getMsap_address() {
		return msap_address;
	}
	public void setMsap_address(String msap_address) {
		this.msap_address = msap_address;
	}
	public String getMsap_area_code_fax1() {
		return msap_area_code_fax1;
	}
	public void setMsap_area_code_fax1(String msap_area_code_fax1) {
		this.msap_area_code_fax1 = msap_area_code_fax1;
	}
	public String getMsap_area_code_fax2() {
		return msap_area_code_fax2;
	}
	public void setMsap_area_code_fax2(String msap_area_code_fax2) {
		this.msap_area_code_fax2 = msap_area_code_fax2;
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
	public String getMsap_contact() {
		return msap_contact;
	}
	public void setMsap_contact(String msap_contact) {
		this.msap_contact = msap_contact;
	}
	public String getMsap_fax1() {
		return msap_fax1;
	}
	public void setMsap_fax1(String msap_fax1) {
		this.msap_fax1 = msap_fax1;
	}
	public String getMsap_fax2() {
		return msap_fax2;
	}
	public void setMsap_fax2(String msap_fax2) {
		this.msap_fax2 = msap_fax2;
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
	public String getMsap_zip_code() {
		return msap_zip_code;
	}
	public void setMsap_zip_code(String msap_zip_code) {
		this.msap_zip_code = msap_zip_code;
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
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getTagih() {
		return tagih;
	}
	public void setTagih(String tagih) {
		this.tagih = tagih;
	}
	public String getLca_nama() {
		return lca_nama;
	}
	public void setLca_nama(String lca_nama) {
		this.lca_nama = lca_nama;
	}
	public String getLwk_nama() {
		return lwk_nama;
	}
	public void setLwk_nama(String lwk_nama) {
		this.lwk_nama = lwk_nama;
	}
	public Integer getLsne_id() {
		return lsne_id;
	}
	public void setLsne_id(Integer lsne_id) {
		this.lsne_id = lsne_id;
	}

}
