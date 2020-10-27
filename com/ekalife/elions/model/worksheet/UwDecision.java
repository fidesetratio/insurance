package com.ekalife.elions.model.worksheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

public class UwDecision implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;
	private Integer insured_no;
	private Integer mwd_urut;
	private String penyakit;
	private String prod_utama_persen;
	private String prod_utama_permil;
	private List<UwDecisionRider> rider;
	private String lic_id;
	private String lic_desc;
	private Integer urutan_penyakit;
	private Integer urutan_decision;
	private String catatan;
	private String lus_id;
	private String input_date;
	private Integer lsbs_id;
	private Integer lsdbs_number;
	
	
	public UwDecision(String date) {
		setInput_date(date);
		setProd_utama_persen("0");
		setProd_utama_permil("0");
		setCatatan("");	
		setRider(new ArrayList<UwDecisionRider>());
	}
	
	public void setRiderContent(List<SortedMap> x) {
		for(int a=0;a<x.size();a++) {
			SortedMap data = x.get(a);
			if(((Integer) data.get("O") < 300 && (Integer) data.get("lstb_id") == 1) || 
			   (a == 0 && (Integer) data.get("lstb_id") == 2)) {
				setLsbs_id((Integer) data.get("O"));
				setLsdbs_number((Integer) data.get("U"));
				continue;
			}
			UwDecisionRider udr = new UwDecisionRider();
			udr.setReg_spaj(data.get("M").toString());
			udr.setMwdr_urut(a+1);
			udr.setLsbs_id((Integer) data.get("O"));
			udr.setLsdbs_number((Integer) data.get("U"));
			udr.setRate_persen("0");
			udr.setRate_permil("0");
			getRider().add(udr);
		}
	}
	
	public String getReg_spaj() { return reg_spaj; }
	public void setReg_spaj(String reg_spaj) { this.reg_spaj = reg_spaj; }

	public Integer getInsured_no() { return insured_no; }
	public void setInsured_no(Integer insured_no) { this.insured_no = insured_no; }

	public Integer getMwd_urut() { return mwd_urut; }
	public void setMwd_urut(Integer mwd_urut) { this.mwd_urut = mwd_urut; }

	public String getCatatan() { return catatan; }
	public void setCatatan(String catatan) { this.catatan = catatan; }
	
	public String getLic_id() { return lic_id; }
	public void setLic_id(String lic_id) { this.lic_id = lic_id; }
	
	public String getLic_desc() { return lic_desc; }
	public void setLic_desc(String lic_desc) { this.lic_desc = lic_desc; }
	
	public String getLus_id() { return lus_id; }
	public void setLus_id(String lus_id) { this.lus_id = lus_id; }
	
	public String getPenyakit() { return penyakit; }
	public void setPenyakit(String penyakit) { this.penyakit = penyakit; }
	
	public String getProd_utama_permil() { return prod_utama_permil; }
	public void setProd_utama_permil(String prod_utama_permil) { this.prod_utama_permil = prod_utama_permil; }
	
	public String getProd_utama_persen() { return prod_utama_persen; }
	public void setProd_utama_persen(String prod_utama_persen) { this.prod_utama_persen = prod_utama_persen; }
	
	public List<UwDecisionRider> getRider() { return rider; }
	public void setRider(List<UwDecisionRider> rider) { this.rider = rider; }

	public Integer getUrutan_decision() { return urutan_decision; }
	public void setUrutan_decision(Integer urutan_decision) { this.urutan_decision = urutan_decision; }
	
	public Integer getUrutan_penyakit() { return urutan_penyakit; }
	public void setUrutan_penyakit(Integer urutan_penyakit) { this.urutan_penyakit = urutan_penyakit; }
	
	public String getInput_date() { return input_date; }
	public void setInput_date(String input_date) { this.input_date = input_date; }

	public Integer getLsbs_id() { return lsbs_id; }
	public void setLsbs_id(Integer lsbs_id) { this.lsbs_id = lsbs_id; }

	public Integer getLsdbs_number() { return lsdbs_number; }
	public void setLsdbs_number(Integer lsdbs_number) { this.lsdbs_number = lsdbs_number; }
	
}
 