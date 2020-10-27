package com.ekalife.elions.model.worksheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UwLpk implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//private UwNormVal nv;
	
	private String nv_lpk_deyut_nadi;
	
	private String reg_spaj;
	private Integer insured_no;
	private Integer urutanLpk;
	private String tglMcuLpk;
	private String tmpMcuLpk;
	private Integer lpkTb;
	private Integer lpkBb;
	private Double lpkBmi;
	private String lpkBmiKelainan;
	private Map emBmiLpk;
	private Integer lpkSystolic;
	private Integer lpkDiastolic;
	private String emBloodPresure;
	private Integer lpkDeyutNadi;
	private Integer lpkRPD;
	private List<UwRiwPenyakit> listRpd;
	private Integer lpkRPS;
	private List<UwRiwPenyakit> listRps;
	private Integer lpkRPK;
	private List<UwRiwPenyakit> listRpk;
	private Integer lpkKelainan;
	private List<UwRiwPenyakit> listKelainan;
	
	public UwLpk() {
		setNv_lpk_deyut_nadi("60 - 100");
		
		setLpkRPD(0);
		setListRpd(new ArrayList<UwRiwPenyakit>());
		getListRpd().add(new UwRiwPenyakit(1));
		
		setLpkRPS(0);
		setListRps(new ArrayList<UwRiwPenyakit>());
		getListRps().add(new UwRiwPenyakit(2));
		
		setLpkRPK(0);
		setListRpk(new ArrayList<UwRiwPenyakit>());
		getListRpk().add(new UwRiwPenyakit(3));
		
		setLpkKelainan(0);
		setListKelainan(new ArrayList<UwRiwPenyakit>());
		getListKelainan().add(new UwRiwPenyakit(4));
	}
	
	public String getNv_lpk_deyut_nadi() { return nv_lpk_deyut_nadi; }
	public void setNv_lpk_deyut_nadi(String nv_lpk_deyut_nadi) { this.nv_lpk_deyut_nadi = nv_lpk_deyut_nadi; }
	
	public Integer getInsured_no() { return insured_no; }
	public void setInsured_no(Integer insured_no) { this.insured_no = insured_no; }

	public String getReg_spaj() { return reg_spaj; }
	public void setReg_spaj(String reg_spaj) { this.reg_spaj = reg_spaj; }

	public Integer getUrutanLpk() { return urutanLpk; }
	public void setUrutanLpk(Integer urutanLpk) { this.urutanLpk = urutanLpk; }

	public String getEmBloodPresure() { return emBloodPresure; }
	public void setEmBloodPresure(String emBloodPresure) { this.emBloodPresure = emBloodPresure; }
	
	public Map getEmBmiLpk() { return emBmiLpk; }
	public void setEmBmiLpk(Map emBmiLpk) { this.emBmiLpk = emBmiLpk; }
	
	public List<UwRiwPenyakit> getListKelainan() { return listKelainan; }
	public void setListKelainan(List<UwRiwPenyakit> listKelainan) { this.listKelainan = listKelainan; }
	
	public List<UwRiwPenyakit> getListRpd() { return listRpd; }
	public void setListRpd(List<UwRiwPenyakit> listRpd) { this.listRpd = listRpd; }
	
	public List<UwRiwPenyakit> getListRpk() { return listRpk; }
	public void setListRpk(List<UwRiwPenyakit> listRpk) { this.listRpk = listRpk; }
	
	public List<UwRiwPenyakit> getListRps() { return listRps; }
	public void setListRps(List<UwRiwPenyakit> listRps) { this.listRps = listRps; }
	
	public Integer getLpkBb() { return lpkBb; }
	public void setLpkBb(Integer lpkBb) { this.lpkBb = lpkBb; }
	
	public Double getLpkBmi() { return lpkBmi; }
	public void setLpkBmi(Double lpkBmi) { this.lpkBmi = lpkBmi; }
	
	public String getLpkBmiKelainan() { return lpkBmiKelainan; }
	public void setLpkBmiKelainan(String lpkBmiKelainan) { this.lpkBmiKelainan = lpkBmiKelainan; }

	public Integer getLpkDeyutNadi() { return lpkDeyutNadi; }
	public void setLpkDeyutNadi(Integer lpkDeyutNadi) { this.lpkDeyutNadi = lpkDeyutNadi; } 
	
	public Integer getLpkDiastolic() { return lpkDiastolic; }
	public void setLpkDiastolic(Integer lpkDiastolic) { this.lpkDiastolic = lpkDiastolic; }
	
	public Integer getLpkKelainan() { return lpkKelainan; }
	public void setLpkKelainan(Integer lpkKelainan) { this.lpkKelainan = lpkKelainan; }
	
	public Integer getLpkRPD() { return lpkRPD; }
	public void setLpkRPD(Integer lpkRPD) { this.lpkRPD = lpkRPD; }
	
	public Integer getLpkRPK() { return lpkRPK; }
	public void setLpkRPK(Integer lpkRPK) { this.lpkRPK = lpkRPK; }
	
	public Integer getLpkRPS() { return lpkRPS; }
	public void setLpkRPS(Integer lpkRPS) { this.lpkRPS = lpkRPS; }
	
	public Integer getLpkSystolic() { return lpkSystolic; }
	public void setLpkSystolic(Integer lpkSystolic) { this.lpkSystolic = lpkSystolic; }
	
	public Integer getLpkTb() { return lpkTb; } 
	public void setLpkTb(Integer lpkTb) { this.lpkTb = lpkTb; }
	
	public String getTglMcuLpk() { return tglMcuLpk; }
	public void setTglMcuLpk(String tglMcuLpk) { this.tglMcuLpk = tglMcuLpk; }
	
	public String getTmpMcuLpk() { return tmpMcuLpk; }
	public void setTmpMcuLpk(String tmpMcuLpk) { this.tmpMcuLpk = tmpMcuLpk; }
	
	
}
 