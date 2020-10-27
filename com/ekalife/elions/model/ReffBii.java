package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ekalife.utils.Common;

public class ReffBii 
implements Serializable {

	private static final long serialVersionUID = 1L;
		//agen penutup / fa
	private String level_id;                      
	private String lrb_id;
	private String nama_reff;                      
	private String no_rek;                         
	private String cab_rek;                        
	private String atas_nama;                      
	private String flag_aktif;                    
	private String aktif;
	private String npk;
	private String lcb_no;
	private String lcb_no_kk;
	private String lcb_penutup;
	private String lrbj_id1;
	
	//referral
	private String level_id2;                      
	private String reff_id;
	private String nama_reff2;                      
	private String no_rek2;                         
	private String cab_rek2;                        
	private String atas_nama2;                      
	private String flag_aktif2;                    
	private String aktif2;
	private String npk2;
	private String lcb_no2;
	private ArrayList lcb_no2_kk;
	private String lcb_penutup2;
	private String lrbj_id1_2;

		//referral non sales
		private String level_id3;                      
		private String reff_id2;
		private String nama_reff3;                      
		private String no_rek3;                         
		private String cab_rek3;                        
		private String atas_nama3;                      
		private String flag_aktif3;                    
		private String aktif3;
		private String npk3;
		private String lcb_no3;
		private ArrayList lcb_no3_kk;
		private String lcb_penutup3;
	
	//lainnya
	private ArrayList daftarreffbii;
	private String reg_spaj;                       
	private String nama_cabang;
	private String head_no;
	private String status;
	private Integer hit_err;
	private String lcc_no;   
	private String lrc_nama;                       
	private String lrc_id;  
	private String lrc_bank;                       
	private String lrc_no_rek;                     
	private String lrc_atas_nama;                  
	private String lrc_aktif;
	private String statussubmit;
	private Integer halaman_aktif;
	private Integer jumlah_row;
	private Integer jumlah_halaman;
	private Integer rn;
	private Integer flag_edit;
	private String kunci;
	private String flag;
	private String agent_code;
	private String agent_code2;
	private String agent_code3;
	private String jn_nasabah;
	
	public String getJn_nasabah() {
		return jn_nasabah;
	}
	public void setJn_nasabah(String jn_nasabah) {
		this.jn_nasabah = jn_nasabah;
	}
	//tambahan flag (andy)
	private String pabsm; //(y/n)
	
	public String getAktif2() {
		return aktif2;
	}
	public void setAktif2(String aktif2) {
		this.aktif2 = aktif2;
	}
	public String getAtas_nama2() {
		return atas_nama2;
	}
	public void setAtas_nama2(String atas_nama2) {
		this.atas_nama2 = atas_nama2;
	}
	public String getCab_rek2() {
		return cab_rek2;
	}
	public void setCab_rek2(String cab_rek2) {
		this.cab_rek2 = cab_rek2;
	}
	public String getFlag_aktif2() {
		return flag_aktif2;
	}
	public void setFlag_aktif2(String flag_aktif2) {
		this.flag_aktif2 = flag_aktif2;
	}
	public String getLcb_no2() {
		return lcb_no2;
	}
	public void setLcb_no2(String lcb_no2) {
		this.lcb_no2 = lcb_no2;
	}
	public String getLevel_id2() {
		return level_id2;
	}
	public void setLevel_id2(String level_id2) {
		this.level_id2 = level_id2;
	}
	public String getReff_id() {
		return reff_id;
	}
	public void setReff_id(String reff_id) {
		this.reff_id = reff_id;
	}
	public String getNama_reff2() {
		return nama_reff2;
	}
	public void setNama_reff2(String nama_reff2) {
		this.nama_reff2 = nama_reff2;
	}
	public String getNo_rek2() {
		return no_rek2;
	}
	public void setNo_rek2(String no_rek2) {
		this.no_rek2 = no_rek2;
	}
	public String getNpk2() {
		return npk2;
	}
	public void setNpk2(String npk2) {
		this.npk2 = npk2;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getKunci() {
		return kunci;
	}
	public void setKunci(String kunci) {
		this.kunci = kunci;
	}
	public Integer getFlag_edit() {
		return flag_edit;
	}
	public void setFlag_edit(Integer flag_edit) {
		this.flag_edit = flag_edit;
	}
	public String getAktif() {
		return aktif;
	}
	public void setAktif(String aktif) {
		this.aktif = aktif;
	}
	public String getAtas_nama() {
		return atas_nama;
	}
	public void setAtas_nama(String atas_nama) {
		this.atas_nama = atas_nama;
	}
	public String getCab_rek() {
		return cab_rek;
	}
	public void setCab_rek(String cab_rek) {
		this.cab_rek = cab_rek;
	}
	public ArrayList getDaftarreffbii() {
		return daftarreffbii;
	}
	public void setDaftarreffbii(List daftarreffbii) {
		this.daftarreffbii = Common.serializableList(daftarreffbii);
	}
	public String getFlag_aktif() {
		return flag_aktif;
	}
	public void setFlag_aktif(String flag_aktif) {
		this.flag_aktif = flag_aktif;
	}
	public Integer getHalaman_aktif() {
		return halaman_aktif;
	}
	public void setHalaman_aktif(Integer halaman_aktif) {
		this.halaman_aktif = halaman_aktif;
	}
	public String getHead_no() {
		return head_no;
	}
	public void setHead_no(String head_no) {
		this.head_no = head_no;
	}
	public Integer getHit_err() {
		return hit_err;
	}
	public void setHit_err(Integer hit_err) {
		this.hit_err = hit_err;
	}
	public Integer getJumlah_row() {
		return jumlah_row;
	}
	public void setJumlah_row(Integer jumlah_row) {
		this.jumlah_row = jumlah_row;
	}
	public String getLcb_no() {
		return lcb_no;
	}
	public void setLcb_no(String lcb_no) {
		this.lcb_no = lcb_no;
	}
	public String getLcc_no() {
		return lcc_no;
	}
	public void setLcc_no(String lcc_no) {
		this.lcc_no = lcc_no;
	}
	public String getLevel_id() {
		return level_id;
	}
	public void setLevel_id(String level_id) {
		this.level_id = level_id;
	}
	public String getLrb_id() {
		return lrb_id;
	}
	public void setLrb_id(String lrb_id) {
		this.lrb_id = lrb_id;
	}
	public String getLrc_aktif() {
		return lrc_aktif;
	}
	public void setLrc_aktif(String lrc_aktif) {
		this.lrc_aktif = lrc_aktif;
	}
	public String getLrc_atas_nama() {
		return lrc_atas_nama;
	}
	public void setLrc_atas_nama(String lrc_atas_nama) {
		this.lrc_atas_nama = lrc_atas_nama;
	}
	public String getLrc_bank() {
		return lrc_bank;
	}
	public void setLrc_bank(String lrc_bank) {
		this.lrc_bank = lrc_bank;
	}
	public String getLrc_id() {
		return lrc_id;
	}
	public void setLrc_id(String lrc_id) {
		this.lrc_id = lrc_id;
	}
	public String getLrc_nama() {
		return lrc_nama;
	}
	public void setLrc_nama(String lrc_nama) {
		this.lrc_nama = lrc_nama;
	}
	public String getLrc_no_rek() {
		return lrc_no_rek;
	}
	public void setLrc_no_rek(String lrc_no_rek) {
		this.lrc_no_rek = lrc_no_rek;
	}
	public String getNama_cabang() {
		return nama_cabang;
	}
	public void setNama_cabang(String nama_cabang) {
		this.nama_cabang = nama_cabang;
	}
	public String getNama_reff() {
		return nama_reff;
	}
	public void setNama_reff(String nama_reff) {
		this.nama_reff = nama_reff;
	}
	public String getNo_rek() {
		return no_rek;
	}
	public void setNo_rek(String no_rek) {
		this.no_rek = no_rek;
	}
	public String getNpk() {
		return npk;
	}
	public void setNpk(String npk) {
		this.npk = npk;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getRn() {
		return rn;
	}
	public void setRn(Integer rn) {
		this.rn = rn;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatussubmit() {
		return statussubmit;
	}
	public void setStatussubmit(String statussubmit) {
		this.statussubmit = statussubmit;
	}
	public Integer getJumlah_halaman() {
		return jumlah_halaman;
	}
	public void setJumlah_halaman(Integer jumlah_halaman) {
		this.jumlah_halaman = jumlah_halaman;
	}
	public String getLcb_penutup() {
		return lcb_penutup;
	}
	public void setLcb_penutup(String lcb_penutup) {
		this.lcb_penutup = lcb_penutup;
	}
	public String getLcb_penutup2() {
		return lcb_penutup2;
	}
	public void setLcb_penutup2(String lcb_penutup2) {
		this.lcb_penutup2 = lcb_penutup2;
	}
	public String getPabsm() {
		return pabsm;
	}
	public void setPabsm(String pabsm) {
		this.pabsm = pabsm;
	}
	
	public String getLcb_no_kk() {
		return lcb_no_kk;
	}
	public void setLcb_no_kk(String lcbNoKk) {
		lcb_no_kk = lcbNoKk;
	}
	public ArrayList getLcb_no2_kk() {
		return lcb_no2_kk;
	}
	public void setLcb_no2_kk(List lcbNo2Kk) {
		lcb_no2_kk = Common.serializableList(lcbNo2Kk);
	}
	public String getAgent_code() {
		return agent_code;
	}
	public String getAgent_code2() {
		return agent_code2;
	}
	public void setAgent_code(String agent_code) {
		this.agent_code = agent_code;
	}
	public void setAgent_code2(String agent_code2) {
		this.agent_code2 = agent_code2;
	}
	public String getLevel_id3() {
		return level_id3;
	}
	public void setLevel_id3(String level_id3) {
		this.level_id3 = level_id3;
	}
	public String getReff_id2() {
		return reff_id2;
	}
	public void setReff_id2(String reff_id2) {
		this.reff_id2 = reff_id2;
	}
	public String getNama_reff3() {
		return nama_reff3;
	}
	public void setNama_reff3(String nama_reff3) {
		this.nama_reff3 = nama_reff3;
	}
	public String getNo_rek3() {
		return no_rek3;
	}
	public void setNo_rek3(String no_rek3) {
		this.no_rek3 = no_rek3;
	}
	public String getCab_rek3() {
		return cab_rek3;
	}
	public void setCab_rek3(String cab_rek3) {
		this.cab_rek3 = cab_rek3;
	}
	public String getAtas_nama3() {
		return atas_nama3;
	}
	public void setAtas_nama3(String atas_nama3) {
		this.atas_nama3 = atas_nama3;
	}
	public String getFlag_aktif3() {
		return flag_aktif3;
	}
	public void setFlag_aktif3(String flag_aktif3) {
		this.flag_aktif3 = flag_aktif3;
	}
	public String getAktif3() {
		return aktif3;
	}
	public void setAktif3(String aktif3) {
		this.aktif3 = aktif3;
	}
	public String getNpk3() {
		return npk3;
	}
	public void setNpk3(String npk3) {
		this.npk3 = npk3;
	}
	public String getLcb_no3() {
		return lcb_no3;
	}
	public void setLcb_no3(String lcb_no3) {
		this.lcb_no3 = lcb_no3;
	}
	public ArrayList getLcb_no3_kk() {
		return lcb_no3_kk;
	}
	public void setLcb_no3_kk(ArrayList lcb_no3_kk) {
		this.lcb_no3_kk = lcb_no3_kk;
	}
	public String getLcb_penutup3() {
		return lcb_penutup3;
	}
	public void setLcb_penutup3(String lcb_penutup3) {
		this.lcb_penutup3 = lcb_penutup3;
	}
	public void setLcb_no2_kk(ArrayList lcb_no2_kk) {
		this.lcb_no2_kk = lcb_no2_kk;
	}
	public void setDaftarreffbii(ArrayList daftarreffbii) {
		this.daftarreffbii = daftarreffbii;
	}
	public String getAgent_code3() {
		return agent_code3;
	}
	public void setAgent_code3(String agent_code3) {
		this.agent_code3 = agent_code3;
	}
	
	public String getLrbj_id1() {
		return lrbj_id1;
	}
	public void setLrbj_id1(String lrbj_id1) {
		this.lrbj_id1 = lrbj_id1;
	}
	public String getLrbj_id1_2() {
		return lrbj_id1_2;
	}
	public void setLrbj_id1_2(String lrbj_id1_2) {
		this.lrbj_id1_2 = lrbj_id1_2;
	}
	
}
