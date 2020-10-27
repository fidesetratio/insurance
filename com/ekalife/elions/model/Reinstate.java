package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Reinstate implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
    private String name;
	private String noSpaj;
	private String poPolicyNo;
	private Date poSpajDate;
	private Integer poInsPeriod;
	private Integer poProsesBill;
	private Date poNextBill;
	private Integer poInstallment;
	private Integer reinsLspdId;
	private Integer poLspdId;
	private String poLcaId;
	private String poLwkId;
	private String poLsrgId;
	private Integer poPayPeriod;
	private String poLkuId;
	private Integer poLscbId;
	private Integer poLsspId;
	private Integer insFlagCc;
	private String reinsNo;
	private List daftarSpaj;
	private Integer disabledReas;
	private String noSpajCompare;
	private Integer flag; 
	private Integer bulan;
	private Integer lspdId;
	
	
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public List getDaftarSpaj() {
		return daftarSpaj;
	}
	public void setDaftarSpaj(List daftarSpaj) {
		this.daftarSpaj = daftarSpaj;
	}
	public Integer getDisabledReas() {
		return disabledReas;
	}
	public void setDisabledReas(Integer disabledReas) {
		this.disabledReas = disabledReas;
	}
	public Integer getInsFlagCc() {
		return insFlagCc;
	}
	public void setInsFlagCc(Integer insFlagCc) {
		this.insFlagCc = insFlagCc;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNoSpaj() {
		return noSpaj;
	}
	public void setNoSpaj(String noSpaj) {
		this.noSpaj = noSpaj;
	}
	public Integer getPoInsPeriod() {
		return poInsPeriod;
	}
	public void setPoInsPeriod(Integer poInsPeriod) {
		this.poInsPeriod = poInsPeriod;
	}
	public Integer getPoInstallment() {
		return poInstallment;
	}
	public void setPoInstallment(Integer poInstallment) {
		this.poInstallment = poInstallment;
	}
	public String getPoLcaId() {
		return poLcaId;
	}
	public void setPoLcaId(String poLcaId) {
		this.poLcaId = poLcaId;
	}
	public String getPoLkuId() {
		return poLkuId;
	}
	public void setPoLkuId(String poLkuId) {
		this.poLkuId = poLkuId;
	}
	public Integer getPoLscbId() {
		return poLscbId;
	}
	public void setPoLscbId(Integer poLscbId) {
		this.poLscbId = poLscbId;
	}
	public Integer getPoLspdId() {
		return poLspdId;
	}
	public void setPoLspdId(Integer poLspdId) {
		this.poLspdId = poLspdId;
	}
	public String getPoLsrgId() {
		return poLsrgId;
	}
	public void setPoLsrgId(String poLsrgId) {
		this.poLsrgId = poLsrgId;
	}
	public Integer getPoLsspId() {
		return poLsspId;
	}
	public void setPoLsspId(Integer poLsspId) {
		this.poLsspId = poLsspId;
	}
	public String getPoLwkId() {
		return poLwkId;
	}
	public void setPoLwkId(String poLwkId) {
		this.poLwkId = poLwkId;
	}
	public Date getPoNextBill() {
		return poNextBill;
	}
	public void setPoNextBill(Date poNextBill) {
		this.poNextBill = poNextBill;
	}
	public Integer getPoPayPeriod() {
		return poPayPeriod;
	}
	public void setPoPayPeriod(Integer poPayPeriod) {
		this.poPayPeriod = poPayPeriod;
	}
	public String getPoPolicyNo() {
		return poPolicyNo;
	}
	public void setPoPolicyNo(String poPolicyNo) {
		this.poPolicyNo = poPolicyNo;
	}
	public Integer getPoProsesBill() {
		return poProsesBill;
	}
	public void setPoProsesBill(Integer poProsesBill) {
		this.poProsesBill = poProsesBill;
	}
	public Date getPoSpajDate() {
		return poSpajDate;
	}
	public void setPoSpajDate(Date poSpajDate) {
		this.poSpajDate = poSpajDate;
	}
	public Integer getReinsLspdId() {
		return reinsLspdId;
	}
	public void setReinsLspdId(Integer reinsLspdId) {
		this.reinsLspdId = reinsLspdId;
	}
	public String getReinsNo() {
		return reinsNo;
	}
	public void setReinsNo(String reinsNo) {
		this.reinsNo = reinsNo;
	}
	public String getNoSpajCompare() {
		return noSpajCompare;
	}
	public void setNoSpajCompare(String noSpajCompare) {
		this.noSpajCompare = noSpajCompare;
	}
	public void setBulan(Integer bulan) {
		this.bulan = bulan;
	}
	public Integer getLspdId() {
		return lspdId;
	}
	public void setLspdId(Integer lspdId) {
		this.lspdId = lspdId;
	}
	public Integer getBulan() {
		return bulan;
	}
	
	
	
	
}
