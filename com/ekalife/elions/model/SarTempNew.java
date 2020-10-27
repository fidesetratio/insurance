package com.ekalife.elions.model;

import java.io.Serializable;

public class SarTempNew implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;                       
	private Integer cnt;                           
	private String no_polis;                       
	private Integer bisnis_id;                     
	private Integer bisnis_no;                     
	private String kurs_id;                        
	private Double sar;                            
	private Integer sts_polis;                     
	private Integer medis;                         
	private String sts_aktif;                      
	private Double simultan;                       
	private Double tsi;                            
	private Double retensi;                        
	private Double reas;                           
	private Double extra;                          
	private Integer lsgb_id;                       
	private String id_sar;
	public Integer getBisnis_id() {
		return bisnis_id;
	}
	public void setBisnis_id(Integer bisnis_id) {
		this.bisnis_id = bisnis_id;
	}
	public Integer getBisnis_no() {
		return bisnis_no;
	}
	public void setBisnis_no(Integer bisnis_no) {
		this.bisnis_no = bisnis_no;
	}
	public Integer getCnt() {
		return cnt;
	}
	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}
	public Double getExtra() {
		return extra;
	}
	public void setExtra(Double extra) {
		this.extra = extra;
	}
	public String getId_sar() {
		return id_sar;
	}
	public void setId_sar(String id_sar) {
		this.id_sar = id_sar;
	}
	public String getKurs_id() {
		return kurs_id;
	}
	public void setKurs_id(String kurs_id) {
		this.kurs_id = kurs_id;
	}
	public Integer getLsgb_id() {
		return lsgb_id;
	}
	public void setLsgb_id(Integer lsgb_id) {
		this.lsgb_id = lsgb_id;
	}
	public Integer getMedis() {
		return medis;
	}
	public void setMedis(Integer medis) {
		this.medis = medis;
	}
	public String getNo_polis() {
		return no_polis;
	}
	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}
	public Double getReas() {
		return reas;
	}
	public void setReas(Double reas) {
		this.reas = reas;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Double getRetensi() {
		return retensi;
	}
	public void setRetensi(Double retensi) {
		this.retensi = retensi;
	}
	public Double getSar() {
		return sar;
	}
	public void setSar(Double sar) {
		this.sar = sar;
	}
	public Double getSimultan() {
		return simultan;
	}
	public void setSimultan(Double simultan) {
		this.simultan = simultan;
	}
	public String getSts_aktif() {
		return sts_aktif;
	}
	public void setSts_aktif(String sts_aktif) {
		this.sts_aktif = sts_aktif;
	}
	public Integer getSts_polis() {
		return sts_polis;
	}
	public void setSts_polis(Integer sts_polis) {
		this.sts_polis = sts_polis;
	}
	public Double getTsi() {
		return tsi;
	}
	public void setTsi(Double tsi) {
		this.tsi = tsi;
	}   
}
