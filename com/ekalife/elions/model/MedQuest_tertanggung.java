package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MedQuest_tertanggung implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reg_spaj;    
	private Integer mste_insured_no; 	
	private Integer msadd_life_ins;                
	private String msadd_life_ins_desc;            
	private Integer msadd_smoke;                   
	private Integer nsadd_many_cig;                
	private Integer msadd_drink_beer;
	private String msadd_drink_beer_desc;
	private Integer msadd_type_beer;               
	private Integer msadd_time_drink;              
	private Integer msadd_many_drink;              
	private Integer msadd_long_drink;            
	private Integer msadd_drugs;                   
	private String msadd_type_drugs;               
	private Integer msadd_many_drugs;              
	private String msadd_reason_drugs;             
	private String msadd_hobby;                    
	private Integer msadd_flight;                  
	private String msadd_desc_flight;              
	private Integer msadd_dead;                    
	private Integer msadd_ill;                     
	private Integer msadm_berat;                   
	private Integer msadm_tinggi;                  
	private Integer msadm_sehat;                   
	private String msadm_sehat_desc;               
	private Integer msadm_operasi;                 
	private String msadm_operasi_desc;             
	private Integer msadm_berat_berubah;           
	private String msadm_berubah_desc;             
	private Integer msadm_penyakit;                
	private String msadm_penyakit_desc;            
	private Integer msadm_medis;                   
	private String msadm_medis_desc;
	private Integer msadm_pregnant;                   
	private String msadm_pregnant_desc;
	private Integer msadm_medis_alt;
	private String msadm_medis_alt_desc;
	private Integer msadm_abortion;
	private String msadm_abortion_desc;
	private Integer msadm_usg;
	private Integer msadm_family_sick;
	private String msadm_family_sick_desc;
	private Integer msadm_clear_case;
	private Integer msadm_sehat_std;
	private Integer msadm_penyakit_std;
	private Integer msadm_usg_mcu;
	private Integer msadm_usg_mcu_std;
	private Double msadm_bmi;
	private Integer msadm_pregnant_time;
	private Map msadm_em;
	private Double msadm_em_life;
	private Integer sex;

	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Double getMsadm_em_life() {
		return msadm_em_life;
	}
	public void setMsadm_em_life(Double msadmEmLife) {
		msadm_em_life = msadmEmLife;
	}
	public Map getMsadm_em() {
		return msadm_em;
	}
	public void setMsadm_em(Map msadmEm) {
		msadm_em = msadmEm;
	}
	public Integer getMsadm_pregnant_time() {
		return msadm_pregnant_time;
	}
	public void setMsadm_pregnant_time(Integer msadmPregnantTime) {
		msadm_pregnant_time = msadmPregnantTime;
	}
	public Double getMsadm_bmi() {
		return msadm_bmi;
	}
	public void setMsadm_bmi(Double msadmBmi) {
		msadm_bmi = msadmBmi;
	}
	public Integer getMsadm_sehat_std() {
		return msadm_sehat_std;
	}
	public void setMsadm_sehat_std(Integer msadmSehatStd) {
		msadm_sehat_std = msadmSehatStd;
	}
	public Integer getMsadm_penyakit_std() {
		return msadm_penyakit_std;
	}
	public void setMsadm_penyakit_std(Integer msadmPenyakitStd) {
		msadm_penyakit_std = msadmPenyakitStd;
	}
	public Integer getMsadm_usg_mcu() {
		return msadm_usg_mcu;
	}
	public void setMsadm_usg_mcu(Integer msadmUsgMcu) {
		msadm_usg_mcu = msadmUsgMcu;
	}
	public Integer getMsadm_usg_mcu_std() {
		return msadm_usg_mcu_std;
	}
	public void setMsadm_usg_mcu_std(Integer msadmUsgMcuStd) {
		msadm_usg_mcu_std = msadmUsgMcuStd;
	}
	private List<Map> health_checklist;
	
	public List<Map> getHealth_checklist() {
		return health_checklist;
	}
	public void setHealth_checklist(List<Map> healthChecklist) {
		health_checklist = healthChecklist;
	}
	public Integer getMsadm_clear_case() {
		return msadm_clear_case;
	}
	public void setMsadm_clear_case(Integer msadmClearCase) {
		msadm_clear_case = msadmClearCase;
	}
	public Integer getMsadm_family_sick() {
		return msadm_family_sick;
	}
	public void setMsadm_family_sick(Integer msadmFamilySick) {
		msadm_family_sick = msadmFamilySick;
	}
	public String getMsadm_family_sick_desc() {
		return msadm_family_sick_desc;
	}
	public void setMsadm_family_sick_desc(String msadmFamilySickDesc) {
		msadm_family_sick_desc = msadmFamilySickDesc;
	}
	public Integer getMsadm_medis_alt() {
		return msadm_medis_alt;
	}
	public void setMsadm_medis_alt(Integer msadmMedisAlt) {
		msadm_medis_alt = msadmMedisAlt;
	}
	public String getMsadm_medis_alt_desc() {
		return msadm_medis_alt_desc;
	}
	public void setMsadm_medis_alt_desc(String msadmMedisAltDesc) {
		msadm_medis_alt_desc = msadmMedisAltDesc;
	}
	public Integer getMsadm_abortion() {
		return msadm_abortion;
	}
	public void setMsadm_abortion(Integer msadmAbortion) {
		msadm_abortion = msadmAbortion;
	}
	public String getMsadm_abortion_desc() {
		return msadm_abortion_desc;
	}
	public void setMsadm_abortion_desc(String msadmAbortionDesc) {
		msadm_abortion_desc = msadmAbortionDesc;
	}
	public Integer getMsadm_usg() {
		return msadm_usg;
	}
	public void setMsadm_usg(Integer msadmUsg) {
		msadm_usg = msadmUsg;
	}
	public String getMsadm_usg_desc() {
		return msadm_usg_desc;
	}
	public void setMsadm_usg_desc(String msadmUsgDesc) {
		msadm_usg_desc = msadmUsgDesc;
	}
	private String msadm_usg_desc;
	private Integer lsre_id;
	
	public Integer getLsre_id() {
		return lsre_id;
	}
	public void setLsre_id(Integer lsreId) {
		lsre_id = lsreId;
	}
	public Integer getMsadm_pregnant() {
		return msadm_pregnant;
	}
	public void setMsadm_pregnant(Integer msadmPregnant) {
		msadm_pregnant = msadmPregnant;
	}
	public String getMsadm_pregnant_desc() {
		return msadm_pregnant_desc;
	}
	public void setMsadm_pregnant_desc(String msadmPregnantDesc) {
		msadm_pregnant_desc = msadmPregnantDesc;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String regSpaj) {
		reg_spaj = regSpaj;
	}
	public Integer getMste_insured_no() {
		return mste_insured_no;
	}
	public void setMste_insured_no(Integer msteInsuredNo) {
		mste_insured_no = msteInsuredNo;
	}
	public Integer getMsadd_life_ins() {
		return msadd_life_ins;
	}
	public void setMsadd_life_ins(Integer msaddLifeIns) {
		msadd_life_ins = msaddLifeIns;
	}
	public String getMsadd_life_ins_desc() {
		return msadd_life_ins_desc;
	}
	public void setMsadd_life_ins_desc(String msaddLifeInsDesc) {
		msadd_life_ins_desc = msaddLifeInsDesc;
	}
	public Integer getMsadd_smoke() {
		return msadd_smoke;
	}
	public void setMsadd_smoke(Integer msaddSmoke) {
		msadd_smoke = msaddSmoke;
	}
	public Integer getNsadd_many_cig() {
		return nsadd_many_cig;
	}
	public void setNsadd_many_cig(Integer nsaddManyCig) {
		nsadd_many_cig = nsaddManyCig;
	}
	public Integer getMsadd_drink_beer() {
		return msadd_drink_beer;
	}
	public void setMsadd_drink_beer(Integer msaddDrinkBeer) {
		msadd_drink_beer = msaddDrinkBeer;
	}
	public Integer getMsadd_type_beer() {
		return msadd_type_beer;
	}
	public void setMsadd_type_beer(Integer msaddTypeBeer) {
		msadd_type_beer = msaddTypeBeer;
	}
	public Integer getMsadd_time_drink() {
		return msadd_time_drink;
	}
	public void setMsadd_time_drink(Integer msaddTimeDrink) {
		msadd_time_drink = msaddTimeDrink;
	}
	public Integer getMsadd_many_drink() {
		return msadd_many_drink;
	}
	public void setMsadd_many_drink(Integer msaddManyDrink) {
		msadd_many_drink = msaddManyDrink;
	}
	public Integer getMsadd_long_drink() {
		return msadd_long_drink;
	}
	public void setMsadd_long_drink(Integer msaddLongDrink) {
		msadd_long_drink = msaddLongDrink;
	}
	public Integer getMsadd_drugs() {
		return msadd_drugs;
	}
	public void setMsadd_drugs(Integer msaddDrugs) {
		msadd_drugs = msaddDrugs;
	}
	public String getMsadd_type_drugs() {
		return msadd_type_drugs;
	}
	public void setMsadd_type_drugs(String msaddTypeDrugs) {
		msadd_type_drugs = msaddTypeDrugs;
	}
	public Integer getMsadd_many_drugs() {
		return msadd_many_drugs;
	}
	public void setMsadd_many_drugs(Integer msaddManyDrugs) {
		msadd_many_drugs = msaddManyDrugs;
	}
	public String getMsadd_reason_drugs() {
		return msadd_reason_drugs;
	}
	public void setMsadd_reason_drugs(String msaddReasonDrugs) {
		msadd_reason_drugs = msaddReasonDrugs;
	}
	public String getMsadd_hobby() {
		return msadd_hobby;
	}
	public void setMsadd_hobby(String msaddHobby) {
		msadd_hobby = msaddHobby;
	}
	public Integer getMsadd_flight() {
		return msadd_flight;
	}
	public void setMsadd_flight(Integer msaddFlight) {
		msadd_flight = msaddFlight;
	}
	public String getMsadd_desc_flight() {
		return msadd_desc_flight;
	}
	public void setMsadd_desc_flight(String msaddDescFlight) {
		msadd_desc_flight = msaddDescFlight;
	}
	public Integer getMsadd_dead() {
		return msadd_dead;
	}
	public void setMsadd_dead(Integer msaddDead) {
		msadd_dead = msaddDead;
	}
	public Integer getMsadd_ill() {
		return msadd_ill;
	}
	public void setMsadd_ill(Integer msaddIll) {
		msadd_ill = msaddIll;
	}
	public Integer getMsadm_berat() {
		return msadm_berat;
	}
	public void setMsadm_berat(Integer msadmBerat) {
		msadm_berat = msadmBerat;
	}
	public Integer getMsadm_tinggi() {
		return msadm_tinggi;
	}
	public void setMsadm_tinggi(Integer msadmTinggi) {
		msadm_tinggi = msadmTinggi;
	}
	public Integer getMsadm_sehat() {
		return msadm_sehat;
	}
	public void setMsadm_sehat(Integer msadmSehat) {
		msadm_sehat = msadmSehat;
	}
	public String getMsadm_sehat_desc() {
		return msadm_sehat_desc;
	}
	public void setMsadm_sehat_desc(String msadmSehatDesc) {
		msadm_sehat_desc = msadmSehatDesc;
	}
	public Integer getMsadm_operasi() {
		return msadm_operasi;
	}
	public void setMsadm_operasi(Integer msadmOperasi) {
		msadm_operasi = msadmOperasi;
	}
	public String getMsadm_operasi_desc() {
		return msadm_operasi_desc;
	}
	public void setMsadm_operasi_desc(String msadmOperasiDesc) {
		msadm_operasi_desc = msadmOperasiDesc;
	}
	public Integer getMsadm_berat_berubah() {
		return msadm_berat_berubah;
	}
	public void setMsadm_berat_berubah(Integer msadmBeratBerubah) {
		msadm_berat_berubah = msadmBeratBerubah;
	}
	public String getMsadm_berubah_desc() {
		return msadm_berubah_desc;
	}
	public void setMsadm_berubah_desc(String msadmBerubahDesc) {
		msadm_berubah_desc = msadmBerubahDesc;
	}
	public Integer getMsadm_penyakit() {
		return msadm_penyakit;
	}
	public void setMsadm_penyakit(Integer msadmPenyakit) {
		msadm_penyakit = msadmPenyakit;
	}
	public String getMsadm_penyakit_desc() {
		return msadm_penyakit_desc;
	}
	public void setMsadm_penyakit_desc(String msadmPenyakitDesc) {
		msadm_penyakit_desc = msadmPenyakitDesc;
	}
	public Integer getMsadm_medis() {
		return msadm_medis;
	}
	public void setMsadm_medis(Integer msadmMedis) {
		msadm_medis = msadmMedis;
	}
	public String getMsadm_medis_desc() {
		return msadm_medis_desc;
	}
	public void setMsadm_medis_desc(String msadmMedisDesc) {
		msadm_medis_desc = msadmMedisDesc;
	}
	public String getMsadd_drink_beer_desc() {
		return msadd_drink_beer_desc;
	}
	public void setMsadd_drink_beer_desc(String msaddDrinkBeerDesc) {
		msadd_drink_beer_desc = msaddDrinkBeerDesc;
	}
  	
}
