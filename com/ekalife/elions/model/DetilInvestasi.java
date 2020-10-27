package com.ekalife.elions.model;

import java.io.Serializable;


public class DetilInvestasi implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer mu_ke1;                        
	private String lji_id1;                        
	private Integer mdu_persen1;                    
	private Double mdu_jumlah1;                    
	private String lji_invest1;                    
	private String reg_spaj1;
	private Double mdu_jumlah_top;
	private Double mdu_jumlah_top_tunggal;
	
	
	public Double getMdu_jumlah_top_tunggal() {
		return mdu_jumlah_top_tunggal;
	}
	public void setMdu_jumlah_top_tunggal(Double mdu_jumlah_top_tunggal) {
		this.mdu_jumlah_top_tunggal = mdu_jumlah_top_tunggal;
	}
	public Double getMdu_jumlah_top() {
		return mdu_jumlah_top;
	}
	public void setMdu_jumlah_top(Double mdu_jumlah_top) {
		this.mdu_jumlah_top = mdu_jumlah_top;
	}
	public String getLji_id1() {
		return lji_id1;
	}
	public void setLji_id1(String lji_id1) {
		this.lji_id1 = lji_id1;
	}
	public String getLji_invest1() {
		return lji_invest1;
	}
	public void setLji_invest1(String lji_invest1) {
		this.lji_invest1 = lji_invest1;
	}
	public Double getMdu_jumlah1() {
		return mdu_jumlah1;
	}
	public void setMdu_jumlah1(Double mdu_jumlah1) {
		this.mdu_jumlah1 = mdu_jumlah1;
	}
	public Integer getMdu_persen1() {
		return mdu_persen1;
	}
	public void setMdu_persen1(Integer mdu_persen1) {
		this.mdu_persen1 = mdu_persen1;
	}
	public Integer getMu_ke1() {
		return mu_ke1;
	}
	public void setMu_ke1(Integer mu_ke1) {
		this.mu_ke1 = mu_ke1;
	}
	public String getReg_spaj1() {
		return reg_spaj1;
	}
	public void setReg_spaj1(String reg_spaj1) {
		this.reg_spaj1 = reg_spaj1;
	}
}
