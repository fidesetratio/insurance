package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Agen implements Serializable {

	private static final long serialVersionUID = 1L;
                    
	private String mcl_id;                         
	private String mst_leader;                                         
	private Integer msag_jenis;                    
	private Date msag_beg_date;                    
	private Date msag_end_date;                    
	private Date msag_active_date;                 
	private Date msag_input_date;                  
	private Date msag_prod_1;                      
	private Integer msag_active;                   
	private Double msag_tax;                       
	private Integer lus_id;                        
	private Integer msag_komisi;                   
	private String msag_tabungan;                  
	private Integer lbn_id;                        
	private String rekruter;                       
	private Integer msag_flag_bm;                  
	private Integer msag_sbm;                      
	private Integer msag_coa;                                       
	private String msag_rm;                                    
	private String msag_sertifikat_no;             
	private String regbank;                        
	private Integer msag_aakm;                     
	private Integer msag_fad;                      
	private Integer jperson;                       
	private Integer msag_bay;                      
	private String lsle_name;
	private String msag_id;                        
	private String mcl_first;                      
	private String lca_id;                         
	private String lwk_id;                         
	private String lsrg_id;                        
	private String lsrg_nama;                      
	private Integer msag_sertifikat;               
	private Date msag_berlaku;                     
	private Integer msag_ulink;                    
	private String reg_spaj;
	private String kode_regional;
	private Integer lev_comm;
	private Integer lstb_id;
	private Integer lsle_id;
	private Integer flag_sbm;
	private Integer lsla_id;
	private String tingkat;
	private Integer msab_id;
	private String msab_nama;

	private Integer msag_sp;
	private String lside_name;
	private String mspe_no_identity;
	private String lwk_nama;
	private String lca_nama;
	private String leader; 
	private String mspe_email;
	private String leader_email;
	

	private Integer no_urut;
	private Integer msrk_level;
	private Integer msag_asnew;

	//untuk mallinsurance
	private String lrb_id; 
	//
	
	//untuk referensi
	private String id_ref;
	private String name_ref;
	private String pesan_ref;
	private Integer jenis_ref;
	private Integer lvl_fcd;
	
	private  Integer edit_agen;
	private String kriteria_kesalahan;
	
	public Integer getLvl_fcd() {
		return lvl_fcd;
	}
	public void setLvl_fcd(Integer lvl_fcd) {
		this.lvl_fcd = lvl_fcd;
	}
	public String getId_ref() {
		return id_ref;
	}
	public void setId_ref(String id_ref) {
		this.id_ref = id_ref;
	}
	public String getName_ref() {
		return name_ref;
	}
	public void setName_ref(String name_ref) {
		this.name_ref = name_ref;
	}
	public String getPesan_ref() {
		return pesan_ref;
	}
	public void setPesan_ref(String pesan_ref) {
		this.pesan_ref = pesan_ref;
	}
	public Integer getJenis_ref() {
		return jenis_ref;
	}
	public void setJenis_ref(Integer jenis_ref) {
		this.jenis_ref = jenis_ref;
	}
	//end referensi
	
	private List<HashMap> LsStatus;
	public List<HashMap> getLsStatus() {
		return LsStatus;
	}
	public void setLsStatus(List<HashMap> lsStatus) {
		LsStatus = lsStatus;
	}
	public List setStatus(){
		List<Map<String, String>> list=new ArrayList<Map<String, String>>();
		Map<String, String> map=new HashMap<String, String>();
		map.put("KEY", "0");
		map.put("VALUE", "OK");
		list.add(map);
		map=new HashMap<String, String>();
		map.put("KEY", "1");
		map.put("VALUE", "SP-1");
		list.add(map);
		map=new HashMap<String, String>();
		map.put("KEY", "2");
		map.put("VALUE", "SP-2");
		list.add(map);
		map=new HashMap<String, String>();
		map.put("KEY", "3");
		map.put("VALUE", "SP-3");
		list.add(map);
		return list;
		
	}
	public Integer getMsag_sp() {
		return msag_sp;
	}
	public void setMsag_sp(Integer msag_sp) {
		this.msag_sp = msag_sp;
	}
	public Integer getFlag_sbm() {
		return flag_sbm;
	}
	public void setFlag_sbm(Integer flag_sbm) {
		this.flag_sbm = flag_sbm;
	}
	public Integer getJperson() {
		return jperson;
	}
	public void setJperson(Integer jperson) {
		this.jperson = jperson;
	}
	public String getKode_regional() {
		return kode_regional;
	}
	public void setKode_regional(String kode_regional) {
		this.kode_regional = kode_regional;
	}
	public Integer getLbn_id() {
		return lbn_id;
	}
	public void setLbn_id(Integer lbn_id) {
		this.lbn_id = lbn_id;
	}
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
	public Integer getLev_comm() {
		return lev_comm;
	}
	public void setLev_comm(Integer lev_comm) {
		this.lev_comm = lev_comm;
	}
	public Integer getLsla_id() {
		return lsla_id;
	}
	public void setLsla_id(Integer lsla_id) {
		this.lsla_id = lsla_id;
	}
	public Integer getLsle_id() {
		return lsle_id;
	}
	public void setLsle_id(Integer lsle_id) {
		this.lsle_id = lsle_id;
	}
	public String getLsle_name() {
		return lsle_name;
	}
	public void setLsle_name(String lsle_name) {
		this.lsle_name = lsle_name;
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
	public Integer getLstb_id() {
		return lstb_id;
	}
	public void setLstb_id(Integer lstb_id) {
		this.lstb_id = lstb_id;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getLwk_id() {
		return lwk_id;
	}
	public void setLwk_id(String lwk_id) {
		this.lwk_id = lwk_id;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public String getMcl_id() {
		return mcl_id;
	}
	public void setMcl_id(String mcl_id) {
		this.mcl_id = mcl_id;
	}
	public Integer getMsab_id() {
		return msab_id;
	}
	public void setMsab_id(Integer msab_id) {
		this.msab_id = msab_id;
	}
	public String getMsab_nama() {
		return msab_nama;
	}
	public void setMsab_nama(String msab_nama) {
		this.msab_nama = msab_nama;
	}
	public Integer getMsag_aakm() {
		return msag_aakm;
	}
	public void setMsag_aakm(Integer msag_aakm) {
		this.msag_aakm = msag_aakm;
	}
	public Integer getMsag_active() {
		return msag_active;
	}
	public void setMsag_active(Integer msag_active) {
		this.msag_active = msag_active;
	}
	public Date getMsag_active_date() {
		return msag_active_date;
	}
	public void setMsag_active_date(Date msag_active_date) {
		this.msag_active_date = msag_active_date;
	}
	public Integer getMsag_bay() {
		return msag_bay;
	}
	public void setMsag_bay(Integer msag_bay) {
		this.msag_bay = msag_bay;
	}
	public Date getMsag_beg_date() {
		return msag_beg_date;
	}
	public void setMsag_beg_date(Date msag_beg_date) {
		this.msag_beg_date = msag_beg_date;
	}
	public Date getMsag_berlaku() {
		return msag_berlaku;
	}
	public void setMsag_berlaku(Date msag_berlaku) {
		this.msag_berlaku = msag_berlaku;
	}
	public Integer getMsag_coa() {
		return msag_coa;
	}
	public void setMsag_coa(Integer msag_coa) {
		this.msag_coa = msag_coa;
	}
	public Date getMsag_end_date() {
		return msag_end_date;
	}
	public void setMsag_end_date(Date msag_end_date) {
		this.msag_end_date = msag_end_date;
	}
	public Integer getMsag_fad() {
		return msag_fad;
	}
	public void setMsag_fad(Integer msag_fad) {
		this.msag_fad = msag_fad;
	}
	public Integer getMsag_flag_bm() {
		return msag_flag_bm;
	}
	public void setMsag_flag_bm(Integer msag_flag_bm) {
		this.msag_flag_bm = msag_flag_bm;
	}
	public String getMsag_id() {
		return msag_id;
	}
	public void setMsag_id(String msag_id) {
		this.msag_id = msag_id;
	}
	public Date getMsag_input_date() {
		return msag_input_date;
	}
	public void setMsag_input_date(Date msag_input_date) {
		this.msag_input_date = msag_input_date;
	}
	public Integer getMsag_jenis() {
		return msag_jenis;
	}
	public void setMsag_jenis(Integer msag_jenis) {
		this.msag_jenis = msag_jenis;
	}
	public Integer getMsag_komisi() {
		return msag_komisi;
	}
	public void setMsag_komisi(Integer msag_komisi) {
		this.msag_komisi = msag_komisi;
	}
	public Date getMsag_prod_1() {
		return msag_prod_1;
	}
	public void setMsag_prod_1(Date msag_prod_1) {
		this.msag_prod_1 = msag_prod_1;
	}
	public String getMsag_rm() {
		return msag_rm;
	}
	public void setMsag_rm(String msag_rm) {
		this.msag_rm = msag_rm;
	}
	public Integer getMsag_sbm() {
		return msag_sbm;
	}
	public void setMsag_sbm(Integer msag_sbm) {
		this.msag_sbm = msag_sbm;
	}
	public Integer getMsag_sertifikat() {
		return msag_sertifikat;
	}
	public void setMsag_sertifikat(Integer msag_sertifikat) {
		this.msag_sertifikat = msag_sertifikat;
	}
	public String getMsag_sertifikat_no() {
		return msag_sertifikat_no;
	}
	public void setMsag_sertifikat_no(String msag_sertifikat_no) {
		this.msag_sertifikat_no = msag_sertifikat_no;
	}
	public String getMsag_tabungan() {
		return msag_tabungan;
	}
	public void setMsag_tabungan(String msag_tabungan) {
		this.msag_tabungan = msag_tabungan;
	}
	public Double getMsag_tax() {
		return msag_tax;
	}
	public void setMsag_tax(Double msag_tax) {
		this.msag_tax = msag_tax;
	}
	public Integer getMsag_ulink() {
		return msag_ulink;
	}
	public void setMsag_ulink(Integer msag_ulink) {
		this.msag_ulink = msag_ulink;
	}
	public String getMst_leader() {
		return mst_leader;
	}
	public void setMst_leader(String mst_leader) {
		this.mst_leader = mst_leader;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getRegbank() {
		return regbank;
	}
	public void setRegbank(String regbank) {
		this.regbank = regbank;
	}
	public String getRekruter() {
		return rekruter;
	}
	public void setRekruter(String rekruter) {
		this.rekruter = rekruter;
	}
	public String getTingkat() {
		return tingkat;
	}
	public void setTingkat(String tingkat) {
		this.tingkat = tingkat;
	}
	public String getMspe_no_identity() {
		return mspe_no_identity;
	}
	public void setMspe_no_identity(String mspe_no_identity) {
		this.mspe_no_identity = mspe_no_identity;
	}
	public String getLside_name() {
		return lside_name;
	}
	public void setLside_name(String lside_name) {
		this.lside_name = lside_name;
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
	public String getLrb_id() {
		return lrb_id;
	}
	public void setLrb_id(String lrbId) {
		lrb_id = lrbId;
	}
	
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}

	public String getMspe_email() {
		return mspe_email;
	}
	public void setMspe_email(String mspe_email) {
		this.mspe_email = mspe_email;
	}

	public String getLeader_email() {
		return leader_email;
	}
	public void setLeader_email(String leader_email) {
		this.leader_email = leader_email;
	}
	public Integer getNo_urut() {
		return no_urut;
	}
	public void setNo_urut(Integer no_urut) {
		this.no_urut = no_urut;
	}
	public Integer getMsrk_level() {
		return msrk_level;
	}
	public void setMsrk_level(Integer msrk_level) {
		this.msrk_level = msrk_level;
	}
	public Integer getMsag_asnew() {
		return msag_asnew;
	}
	public void setMsag_asnew(Integer msag_asnew) {
		this.msag_asnew = msag_asnew;
	}
	public String getKriteria_kesalahan() {
		return kriteria_kesalahan;
	}
	public void setKriteria_kesalahan(String kriteria_kesalahan) {
		this.kriteria_kesalahan = kriteria_kesalahan;
	}
	public Integer getEdit_agen() {
		return edit_agen;
	}
	public void setEdit_agen(Integer edit_agen) {
		this.edit_agen = edit_agen;
	}

}
