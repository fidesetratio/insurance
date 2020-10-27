package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Class untuk MST_QUESTIONNAIRE dan LST_QUESTIONNAIRE - table2 kuesioner
 * 
 * Jenis 
 * 1 : BII
 * 
 * @author Yusuf
 * @since Feb 11, 2010 (9:06:46 AM)
 */
public class Questionnaire implements Serializable {

	private static final long serialVersionUID = 1L;

	//MST_QUESTIONNAIRE
	public String reg_spaj;                       
	public Integer lsqu_id;                       
	public Integer msqu_jawab;   
	public String msqu_desc;                      
	public Date create_dt;                        
	public Integer lus_id;

	//LST_QUESTIONNAIRE
	public Integer lsqu_jenis;                    
	public String lsqu_desc;                      
	public Integer lsqu_active;

	public String getLsqu_desc() {return lsqu_desc;}
	public void setLsqu_desc(String lsqu_desc) {this.lsqu_desc = lsqu_desc;}
	public Integer getLsqu_jenis() {return lsqu_jenis;}
	public void setLsqu_jenis(Integer lsqu_jenis) {this.lsqu_jenis = lsqu_jenis;}
	public Date getCreate_dt() {return create_dt;}
	public void setCreate_dt(Date create_dt) {this.create_dt = create_dt;}
	public Integer getLsqu_id() {return lsqu_id;}
	public void setLsqu_id(Integer lsqu_id) {this.lsqu_id = lsqu_id;}
	public Integer getLus_id() {return lus_id;}
	public void setLus_id(Integer lus_id) {this.lus_id = lus_id;}
	public String getMsqu_desc() {return msqu_desc;}
	public void setMsqu_desc(String msqu_desc) {this.msqu_desc = msqu_desc;}
	public Integer getMsqu_jawab() {return msqu_jawab;}
	public void setMsqu_jawab(Integer msqu_jawab) {this.msqu_jawab = msqu_jawab;}
	public String getReg_spaj() {return reg_spaj;}
	public void setReg_spaj(String reg_spaj) {this.reg_spaj = reg_spaj;}
	public Integer getLsqu_active() {return lsqu_active;}
	public void setLsqu_active(Integer lsqu_active) {this.lsqu_active = lsqu_active;}                        
	
}