package com.ekalife.elions.model.worksheet;

import java.io.Serializable;

public class UwTreadmill implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;
	private Integer insured_no;
	private Integer urutanTreadmill;
	private String tglMcuTreadmill;
	private String tmpMcuTreadmill;
	
	private Integer duration_men;
	private Integer duration_det;
	private String mets;
	private String reason_of_termination;
	private String max_st_depresi;
	private String resting_bp;
	private String max_bp;
	private String max_heart_rate;
	private String interpretasi;
	private String treadmill_lic_id;
	private String treadmill_lic_desc;
	
	public Integer getInsured_no() { return insured_no; }
	public void setInsured_no(Integer insured_no) { this.insured_no = insured_no; }
	
	public String getReg_spaj() { return reg_spaj; }
	public void setReg_spaj(String reg_spaj) { this.reg_spaj = reg_spaj; }
	
	public Integer getDuration_det() { return duration_det; }
	public void setDuration_det(Integer duration_det) { this.duration_det = duration_det; }
	
	public Integer getDuration_men() { return duration_men; }
	public void setDuration_men(Integer duration_men) { this.duration_men = duration_men; }
	
	public String getInterpretasi() { return interpretasi; }
	public void setInterpretasi(String interpretasi) { this.interpretasi = interpretasi; }
	
	public String getMax_bp() { return max_bp; }
	public void setMax_bp(String max_bp) { this.max_bp = max_bp; }
	
	public String getMax_heart_rate() {
		return max_heart_rate;
	}
	public void setMax_heart_rate(String max_heart_rate) {
		this.max_heart_rate = max_heart_rate;
	}
	public String getMax_st_depresi() {
		return max_st_depresi;
	}
	public void setMax_st_depresi(String max_st_depresi) {
		this.max_st_depresi = max_st_depresi;
	}
	public String getMets() {
		return mets;
	}
	public void setMets(String mets) {
		this.mets = mets;
	}
	public String getReason_of_termination() {
		return reason_of_termination;
	}
	public void setReason_of_termination(String reason_of_termination) {
		this.reason_of_termination = reason_of_termination;
	}
	public String getResting_bp() { return resting_bp; }
	public void setResting_bp(String resting_bp) { this.resting_bp = resting_bp; }
	
	public String getTglMcuTreadmill() { return tglMcuTreadmill; }
	public void setTglMcuTreadmill(String tglMcuTreadmill) { this.tglMcuTreadmill = tglMcuTreadmill; }
	
	public String getTmpMcuTreadmill() { return tmpMcuTreadmill; }
	public void setTmpMcuTreadmill(String tmpMcuTreadmill) { this.tmpMcuTreadmill = tmpMcuTreadmill; }

	public String getTreadmill_lic_desc() { return treadmill_lic_desc; }
	public void setTreadmill_lic_desc(String treadmill_lic_desc) { this.treadmill_lic_desc = treadmill_lic_desc; }

	public String getTreadmill_lic_id() { return treadmill_lic_id; }
	public void setTreadmill_lic_id(String treadmill_lic_id) { this.treadmill_lic_id = treadmill_lic_id; }

	public Integer getUrutanTreadmill() { return urutanTreadmill; }
	public void setUrutanTreadmill(Integer urutanTreadmill) { this.urutanTreadmill = urutanTreadmill; }
	
	
}
