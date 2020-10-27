package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ekalife.utils.Common;

public class Personal implements Serializable {

	private static final long serialVersionUID = -2317405244351681902L;
	private String mcl_id;
	private Integer mcl_jenis;
	private Integer lju_id;                        
	private String mpt_usaha_desc;                 
	private Integer mpt_term;                      
	private String mpt_npwp;   
	private Date tgl_npwp;      
	private String mpt_contact;                    
	private Integer mcl_blacklist;     
	private Integer selisih;
	private String mcl_gelar;                      
	private String mcl_first;                      
	private String lca_id;                         
	private String lwk_id;    
	private String color;
	private String dian;
	private String lsrg_id;                        
	private String lsrg_nama;                      
	private String region_id;
	private Integer lbn_id;
	private Integer lsbp_id;
	private String s_tgl_print_inv;
	private String s_tgl_print_kw;
	private Integer flag_ws;
	private String lus_admin_ws;
	
	//tambahan spaj baru
	private String mpt_no_anggaran;
	private String mpt_notaris;
	private Date mpt_tgl_anggaran;


	private String mpt_siup;  
	private Date tgl_siup;  
	private String mkl_aset;
	private ArrayList lsContactPerson;
	
	private AddressNew address;
	
	private ArrayList status;
	
	//=== Added by Daru @since 18 Mar 2014
	private Integer flag_gws;
	//===
	private Integer flag_group;

	public String getLus_admin_ws() {
		return lus_admin_ws;
	}

	public void setLus_admin_ws(String lusAdminWs) {
		lus_admin_ws = lusAdminWs;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public ArrayList getLsContactPerson() {
		return lsContactPerson;
	}

	public void setLsContactPerson(List lsContactPerson) {
		this.lsContactPerson = Common.serializableList(lsContactPerson);
	}

	public String getLca_id() {
		return lca_id;
	}

	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}

	public Integer getLju_id() {
		return lju_id;
	}

	public void setLju_id(Integer lju_id) {
		this.lju_id = lju_id;
	}

	public String getLsrg_id() {
		return lsrg_id;
	}

	public void setLsrg_id(String lsrg_id) {
		this.lsrg_id = lsrg_id;
	}

	public String getLsrg_nama() {
		return lsrg_nama;
	}

	public void setLsrg_nama(String lsrg_nama) {
		this.lsrg_nama = lsrg_nama;
	}

	public String getLwk_id() {
		return lwk_id;
	}

	public void setLwk_id(String lwk_id) {
		this.lwk_id = lwk_id;
	}

	public Integer getMcl_blacklist() {
		return mcl_blacklist;
	}

	public void setMcl_blacklist(Integer mcl_blacklist) {
		this.mcl_blacklist = mcl_blacklist;
	}

	public String getMcl_first() {
		return mcl_first;
	}

	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}

	public String getMcl_gelar() {
		return mcl_gelar;
	}

	public void setMcl_gelar(String mcl_gelar) {
		this.mcl_gelar = mcl_gelar;
	}

	public String getMpt_contact() {
		return mpt_contact;
	}

	public void setMpt_contact(String mpt_contact) {
		this.mpt_contact = mpt_contact;
	}

	public String getMpt_npwp() {
		return mpt_npwp;
	}

	public void setMpt_npwp(String mpt_npwp) {
		this.mpt_npwp = mpt_npwp;
	}

	public Integer getMpt_term() {
		return mpt_term;
	}

	public void setMpt_term(Integer mpt_term) {
		this.mpt_term = mpt_term;
	}

	public String getMpt_usaha_desc() {
		return mpt_usaha_desc;
	}

	public void setMpt_usaha_desc(String mpt_usaha_desc) {
		this.mpt_usaha_desc = mpt_usaha_desc;
	}

	public ArrayList getStatus() {
		return status;
	}

	public void setStatus(List status) {
		this.status = Common.serializableList(status);
	}

	public String getMcl_id() {
		return mcl_id;
	}

	public void setMcl_id(String mcl_id) {
		this.mcl_id = mcl_id;
	}

	public Integer getMcl_jenis() {
		return mcl_jenis;
	}

	public void setMcl_jenis(Integer mcl_jenis) {
		this.mcl_jenis = mcl_jenis;
	}

	public String getRegion_id() {
		return region_id;
	}

	public void setRegion_id(String region_id) {
		this.region_id = region_id;
	}

	public Integer getLbn_id() {
		return lbn_id;
	}

	public void setLbn_id(Integer lbn_id) {
		this.lbn_id = lbn_id;
	}

	public Integer getLsbp_id() {
		return lsbp_id;
	}

	public void setLsbp_id(Integer lsbp_id) {
		this.lsbp_id = lsbp_id;
	}

	public AddressNew getAddress() {
		return address;
	}

	public void setAddress(AddressNew address) {
		this.address = address;
	}

	public String getS_tgl_print_inv() {
		return s_tgl_print_inv;
	}

	public void setS_tgl_print_inv(String s_tgl_print_inv) {
		this.s_tgl_print_inv = s_tgl_print_inv;
	}

	public String getS_tgl_print_kw() {
		return s_tgl_print_kw;
	}

	public void setS_tgl_print_kw(String s_tgl_print_kw) {
		this.s_tgl_print_kw = s_tgl_print_kw;
	}

	public Integer getFlag_ws() {
		return flag_ws;
	}

	public void setFlag_ws(Integer flag_ws) {
		this.flag_ws = flag_ws;
	}

	public String getMkl_aset() {
		return mkl_aset;
	}

	public void setMkl_aset(String mkl_aset) {
		this.mkl_aset = mkl_aset;
	}

	public String getMpt_siup() {
		return mpt_siup;
	}

	public void setMpt_siup(String mpt_siup) {
		this.mpt_siup = mpt_siup;
	}

	public Date getTgl_npwp() {
		return tgl_npwp;
	}

	public void setTgl_npwp(Date tgl_npwp) {
		this.tgl_npwp = tgl_npwp;
	}

	public Date getTgl_siup() {
		return tgl_siup;
	}

	public void setTgl_siup(Date tgl_siup) {
		this.tgl_siup = tgl_siup;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getDian() {
		return dian;
	}

	public void setDian(String dian) {
		this.dian = dian;
	}

	public Integer getSelisih() {
		return selisih;
	}

	public void setSelisih(Integer selisih) {
		this.selisih = selisih;
	}

	public Integer getFlag_gws() {
		return flag_gws;
	}

	public void setFlag_gws(Integer flag_gws) {
		this.flag_gws = flag_gws;
	}
	
	public String getMpt_no_anggaran() {
		return mpt_no_anggaran;
	}

	public void setMpt_no_anggaran(String mpt_no_anggaran) {
		this.mpt_no_anggaran = mpt_no_anggaran;
	}

	public String getMpt_notaris() {
		return mpt_notaris;
	}

	public void setMpt_notaris(String mpt_notaris) {
		this.mpt_notaris = mpt_notaris;
	}

	public Date getMpt_tgl_anggaran() {
		return mpt_tgl_anggaran;
	}

	public void setMpt_tgl_anggaran(Date mpt_tgl_anggaran) {
		this.mpt_tgl_anggaran = mpt_tgl_anggaran;
	}

	public Integer getFlag_group() {
		return flag_group;
	}

	public void setFlag_group(Integer flag_group) {
		this.flag_group = flag_group;
	}

	
}
