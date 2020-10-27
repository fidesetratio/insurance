package com.ekalife.elions.model.worksheet;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class UwWorkSheet implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer msw_lus_id;
	private String reg_spaj;
	private Integer insured_no;
	private String successMsg;
	private String birthDate;
	private Integer umur;
	private String namaPeserta;
	private String sex;
	private String mcl_id;
	private Integer msw_medis;
	private Integer msw_sex;
	private String submitMode;
	private Integer modeSim;
	private List<SortedMap> listSimultan;
	private List<SortedMap> listSimultanProc; 
	private Integer pptt;
	private Integer okSave;
	private Integer okPrint;
	private Integer okTransfer1;
	private Integer okTransfer2;
	private Integer okReverse1;
	private Integer okReverse2;
	private Integer position; //0: staff, 1: uw-1, 2: uw-2
	private Integer isViewer;
	private List<String> listWarning;
	
	private Integer nonMedisTb;
	private Integer nonMedisBb;
	private Double nonMedisBmi;
	private String nonMedisBmiKelainan;
	private Map emBmi;
	private Integer nonMedisOccupation;
	private String nonMedisOccDesc;
	private String nonMedisHabits;
	private String nonMedisHabitsKelainan;
	private Integer nonMedisHobby;
	private String nonMedisHobbyDesc;
	private String nonMedisKelainan; 
	private String nonMedisKelainanKelainan;
	private Map emOcc;
	private Map emHobby;
	private List<UwQuestionnaire> listQuest;
	
	private List<UwDecision> listUwDec;
	private Integer totalPenyakitUwDesc;
	
	private Integer sumMCU;
	private String jenisMedis;
	private List<UwLpk> listLpk;
	private List<UwUrin> listUrin;
	private List<UwAda> listAda;
	private List<UwHiv> listHiv;
	private List<UwTumor> listTumor;
	private List<UwAbdomen> listAbdomen;
	private List<UwDadaPa> listDadaPa;
	private List<UwEkg> listEkg;
	private List<UwTreadmill> listTreadmill;
	private List<UwMedisLain> listMedLain;
	
	private Integer flag_fs;
	private String fs_date;
	private Integer fs_copy_rek_bank;
	private Integer fs_copy_npwp;
	private Integer fs_spt_pribadi;
	private Integer fs_copy_neraca_persh;
	private Integer fs_lain;
	private String fs_lain_desc;
	
	public UwWorkSheet() {
		setListSimultan(new ArrayList<SortedMap>());
		setListSimultanProc(new ArrayList<SortedMap>());
		setListQuest(new ArrayList<UwQuestionnaire>());
		setListUwDec(new ArrayList<UwDecision>());
		setMsw_medis(0);
		setModeSim(0);		
		setSumMCU(0);
		setListLpk(new ArrayList<UwLpk>());
		setListUrin(new ArrayList<UwUrin>());
		setListAda(new ArrayList<UwAda>());
		setListHiv(new ArrayList<UwHiv>());
		setListTumor(new ArrayList<UwTumor>());
		setListAbdomen(new ArrayList<UwAbdomen>());
		setListDadaPa(new ArrayList<UwDadaPa>());
		setListEkg(new ArrayList<UwEkg>());
		setListTreadmill(new ArrayList<UwTreadmill>());
		setListMedLain(new ArrayList<UwMedisLain>());
		setFlag_fs(0);
		setOkSave(1);
		setOkPrint(0);
		setPosition(0);
		setListWarning(new ArrayList<String>());
	}
	
	public Integer getMsw_sex() { return msw_sex; }
	public void setMsw_sex(Integer mswSex) { msw_sex = mswSex; }

	public Integer getMsw_lus_id() { return msw_lus_id; }
	public void setMsw_lus_id(Integer msw_lus_id) { this.msw_lus_id = msw_lus_id; }

	public String getReg_spaj() { return reg_spaj; } 
	public void setReg_spaj(String reg_spaj) { this.reg_spaj = reg_spaj; }
	
	public Integer getInsured_no() { return insured_no; }
	public void setInsured_no(Integer insured_no) { this.insured_no = insured_no; }
	
	public String getBirthDate() { return birthDate; }
	public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

	public Integer getUmur() { return umur; }
	public void setUmur(Integer umur) { this.umur = umur; }
	
	public String getSex() { return sex; }
	public void setSex(String sex) { this.sex = sex; }

	public String getNamaPeserta() { return namaPeserta; }
	public void setNamaPeserta(String namaPeserta) { this.namaPeserta = namaPeserta; }

	public String getMcl_id() { return mcl_id; }
	public void setMcl_id(String mcl_id) { this.mcl_id = mcl_id; }

	public String getSubmitMode() { return submitMode; }
	public void setSubmitMode(String submitMode) { this.submitMode = submitMode; }
	
	public Integer getMsw_medis() { return msw_medis; }
	public void setMsw_medis(Integer msw_medis) { this.msw_medis = msw_medis; }

	public Integer getModeSim() { return modeSim; }
	public void setModeSim(Integer modeSim) { this.modeSim = modeSim; }
	
	public List<SortedMap> getListSimultan() { return listSimultan; }
	public void setListSimultan(List<SortedMap> listSimultan) { this.listSimultan = listSimultan; }
	
	public List<SortedMap> getListSimultanProc() { return listSimultanProc; }
	public void setListSimultanProc(List<SortedMap> listSimultanProc) { this.listSimultanProc = listSimultanProc; }

	public Integer getPptt() { return pptt; }
	public void setPptt(Integer pptt) { this.pptt = pptt; }
	
	public Integer getOkSave() { return okSave; }
	public void setOkSave(Integer okSave) { this.okSave = okSave; }

	public Integer getOkPrint() { return okPrint; }
	public void setOkPrint(Integer okPrint) { this.okPrint = okPrint; }

	public Integer getPosition() { return position; }
	public void setPosition(Integer position) { this.position = position; }
	
	public Integer getIsViewer() { return isViewer; }
	public void setIsViewer(Integer isViewer) { this.isViewer = isViewer; }

	public List<String> getListWarning() { return listWarning; }
	public void setListWarning(List<String> listWarning) { this.listWarning = listWarning; }

	public Integer getOkReverse1() { return okReverse1; }
	public void setOkReverse1(Integer okReverse1) { this.okReverse1 = okReverse1; }

	public Integer getOkReverse2() { return okReverse2; }
	public void setOkReverse2(Integer okReverse2) { this.okReverse2 = okReverse2; }

	public Integer getOkTransfer1() { return okTransfer1; }
	public void setOkTransfer1(Integer okTransfer1) { this.okTransfer1 = okTransfer1; }

	public Integer getOkTransfer2() { return okTransfer2; }
	public void setOkTransfer2(Integer okTransfer2) { this.okTransfer2 = okTransfer2; }

	public Integer getNonMedisBb() { return nonMedisBb; }
	public void setNonMedisBb(Integer nonMedisBb) { this.nonMedisBb = nonMedisBb; }
	
	public Double getNonMedisBmi() { return nonMedisBmi; }
	public void setNonMedisBmi(Double nonMedisBmi) { this.nonMedisBmi = nonMedisBmi; }
	
	public Integer getNonMedisTb() { return nonMedisTb; }
	public void setNonMedisTb(Integer nonMedisTb) { this.nonMedisTb = nonMedisTb; }
	
	public String getNonMedisBmiKelainan() { return nonMedisBmiKelainan; }
	public void setNonMedisBmiKelainan(String nonMedisBmiKelainan) { this.nonMedisBmiKelainan = nonMedisBmiKelainan; }
	
	public String getNonMedisKelainanKelainan() { return nonMedisKelainanKelainan; }
	public void setNonMedisKelainanKelainan(String nonMedisKelainanKelainan) { this.nonMedisKelainanKelainan = nonMedisKelainanKelainan; }

	public Map getEmBmi() { return emBmi; }
	public void setEmBmi(Map emBmi) { this.emBmi = emBmi; }
	
	public String getNonMedisHabits() { return nonMedisHabits; }
	public void setNonMedisHabits(String nonMedisHabits) { this.nonMedisHabits = nonMedisHabits; }

	public String getNonMedisHabitsKelainan() { return nonMedisHabitsKelainan; }
	public void setNonMedisHabitsKelainan(String nonMedisHabitsKelainan) { this.nonMedisHabitsKelainan = nonMedisHabitsKelainan; }

	public Integer getNonMedisHobby() { return nonMedisHobby; }
	public void setNonMedisHobby(Integer nonMedisHobby) { this.nonMedisHobby = nonMedisHobby; }
	
	public String getNonMedisHobbyDesc() { return nonMedisHobbyDesc; }
	public void setNonMedisHobbyDesc(String nonMedisHobbyDesc) { this.nonMedisHobbyDesc = nonMedisHobbyDesc; }

	public String getNonMedisKelainan() { return nonMedisKelainan; }
	public void setNonMedisKelainan(String nonMedisKelainan) { this.nonMedisKelainan = nonMedisKelainan; }
	
	public Integer getNonMedisOccupation() { return nonMedisOccupation; }
	public void setNonMedisOccupation(Integer nonMedisOccupation) { this.nonMedisOccupation = nonMedisOccupation; }
	
	public String getNonMedisOccDesc() { return nonMedisOccDesc; }
	public void setNonMedisOccDesc(String nonMedisOccDesc) { this.nonMedisOccDesc = nonMedisOccDesc; }

	public Map getEmHobby() { return emHobby; }
	public void setEmHobby(Map emHobby) { this.emHobby = emHobby; }
	
	public Map getEmOcc() { return emOcc; }
	public void setEmOcc(Map emOcc) { this.emOcc = emOcc; }
	
	public List<UwQuestionnaire> getListQuest() { return listQuest; }
	public void setListQuest(List<UwQuestionnaire> listQuest) { this.listQuest = listQuest; }

	public List<UwDecision> getListUwDec() { return listUwDec; }
	public void setListUwDec(List<UwDecision> listUwDec) { this.listUwDec = listUwDec; }

	public Integer getTotalPenyakitUwDesc() { return totalPenyakitUwDesc; }
	public void setTotalPenyakitUwDesc(Integer totalPenyakitUwDesc) { this.totalPenyakitUwDesc = totalPenyakitUwDesc; }

	public String getJenisMedis() { return jenisMedis; }
	public void setJenisMedis(String jenisMedis) { this.jenisMedis = jenisMedis; }

	public Integer getSumMCU() { return sumMCU; }
	public void setSumMCU(Integer sumMCU) { this.sumMCU = sumMCU; }

	public List<UwLpk> getListLpk() { return listLpk; }
	public void setListLpk(List<UwLpk> listLpk) { this.listLpk = listLpk; }

	public List<UwUrin> getListUrin() { return listUrin; }
	public void setListUrin(List<UwUrin> listUrin) { this.listUrin = listUrin; }

	public List<UwAda> getListAda() { return listAda; }
	public void setListAda(List<UwAda> listAda) { this.listAda = listAda; }

	public List<UwHiv> getListHiv() { return listHiv; }
	public void setListHiv(List<UwHiv> listHiv) { this.listHiv = listHiv; }

	public List<UwTumor> getListTumor() { return listTumor; }
	public void setListTumor(List<UwTumor> listTumor) { this.listTumor = listTumor; }

	public List<UwAbdomen> getListAbdomen() { return listAbdomen; }
	public void setListAbdomen(List<UwAbdomen> listAbdomen) { this.listAbdomen = listAbdomen; }

	public List<UwDadaPa> getListDadaPa() { return listDadaPa; }
	public void setListDadaPa(List<UwDadaPa> listDadaPa) { this.listDadaPa = listDadaPa; }

	public List<UwEkg> getListEkg() { return listEkg; }
	public void setListEkg(List<UwEkg> listEkg) { this.listEkg = listEkg; }

	public List<UwTreadmill> getListTreadmill() { return listTreadmill; }
	public void setListTreadmill(List<UwTreadmill> listTreadmill) { this.listTreadmill = listTreadmill; }

	public List<UwMedisLain> getListMedLain() { return listMedLain; }
	public void setListMedLain(List<UwMedisLain> listMedLain) { this.listMedLain = listMedLain; }

	public Integer getFlag_fs() { return flag_fs; }
	public void setFlag_fs(Integer flag_fs) { this.flag_fs = flag_fs; }

	public Integer getFs_copy_neraca_persh() { return fs_copy_neraca_persh; }
	public void setFs_copy_neraca_persh(Integer fs_copy_neraca_persh) { this.fs_copy_neraca_persh = fs_copy_neraca_persh; }

	public Integer getFs_copy_npwp() { return fs_copy_npwp; }
	public void setFs_copy_npwp(Integer fs_copy_npwp) { this.fs_copy_npwp = fs_copy_npwp; }

	public Integer getFs_copy_rek_bank() { return fs_copy_rek_bank; }
	public void setFs_copy_rek_bank(Integer fs_copy_rek_bank) { this.fs_copy_rek_bank = fs_copy_rek_bank; }

	public Integer getFs_lain() { return fs_lain; }
	public void setFs_lain(Integer fs_lain) { this.fs_lain = fs_lain; }

	public String getFs_lain_desc() { return fs_lain_desc; } 
	public void setFs_lain_desc(String fs_lain_desc) { this.fs_lain_desc = fs_lain_desc; }

	public Integer getFs_spt_pribadi() { return fs_spt_pribadi; }
	public void setFs_spt_pribadi(Integer fs_spt_pribadi) { this.fs_spt_pribadi = fs_spt_pribadi; }

	public String getFs_date() { return fs_date; }
	public void setFs_date(String fs_date) { this.fs_date = fs_date; }

	public String getSuccessMsg() { return successMsg; }
	public void setSuccessMsg(String successMsg) { this.successMsg = successMsg; }
	
}
  