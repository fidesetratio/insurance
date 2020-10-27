package com.ekalife.elions.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Lap_Ttp implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal SELISIH;                    
	private String CABANG;   
	
	private String PP;
	private String MSPO_POLICY_NO;  
	private String MCL_FIRST;  
	private String MSPS_DESC;  
	private String SS;  
	private String HH;  
	private String DAYS; 
	private BigDecimal HARI;


	private String MI;  
	private BigDecimal MENIT;
	private BigDecimal DETIK;
	private BigDecimal JAM;
	

	
	private String LCA_NAMA;
	private String LSBS_ID;
	private String PRODUK;
	private String LUS_LOGIN_NAME;
	private String REG_SPAJ;
	private String SELISIH_SSL;
	
	private BigDecimal SELISIH_HARI;
	private BigDecimal SELISIH_JAM;
	private BigDecimal SELISIH_MENIT;
	private BigDecimal SELISIH_DETIK;

	private Date MSPO_TERIMA_TTP;
	private Date SYSDATE;
	private Date MSPO_DATE_PRINT;
	private Date MSPS_DATE;
	private Date MSPO_DATE_TTP;
	private Date TERIMATTP;
	private Date INPUTTTP;
	
	private Date KIRIMPOLIS;

	public BigDecimal getHARI() {
		return HARI;
	}

	public BigDecimal getSELISIH_HARI() {
		return SELISIH_HARI;
	}

	public void setSELISIH_HARI(BigDecimal sELISIHHARI) {
		SELISIH_HARI = sELISIHHARI;
	}

	public BigDecimal getSELISIH_JAM() {
		return SELISIH_JAM;
	}

	public void setSELISIH_JAM(BigDecimal sELISIHJAM) {
		SELISIH_JAM = sELISIHJAM;
	}

	public BigDecimal getSELISIH_MENIT() {
		return SELISIH_MENIT;
	}

	public void setSELISIH_MENIT(BigDecimal sELISIHMENIT) {
		SELISIH_MENIT = sELISIHMENIT;
	}

	public BigDecimal getSELISIH_DETIK() {
		return SELISIH_DETIK;
	}

	public void setSELISIH_DETIK(BigDecimal sELISIHDETIK) {
		SELISIH_DETIK = sELISIHDETIK;
	}

	public void setHARI(BigDecimal hARI) {
		HARI = hARI;
	}

	public BigDecimal getMENIT() {
		return MENIT;
	}

	public void setMENIT(BigDecimal mENIT) {
		MENIT = mENIT;
	}

	public BigDecimal getDETIK() {
		return DETIK;
	}

	public void setDETIK(BigDecimal dETIK) {
		DETIK = dETIK;
	}

	public BigDecimal getJAM() {
		return JAM;
	}

	public void setJAM(BigDecimal jAM) {
		JAM = jAM;
	}

	public BigDecimal getSELISIH() {
		return SELISIH;
	}

	public void setSELISIH(BigDecimal sELISIH) {
		SELISIH = sELISIH;
	}

	public String getCABANG() {
		return CABANG;
	}

	public void setCABANG(String cABANG) {
		CABANG = cABANG;
	}

	public String getPP() {
		return PP;
	}

	public void setPP(String pP) {
		PP = pP;
	}

	public String getMSPO_POLICY_NO() {
		return MSPO_POLICY_NO;
	}

	public void setMSPO_POLICY_NO(String mSPOPOLICYNO) {
		MSPO_POLICY_NO = mSPOPOLICYNO;
	}

	public String getMCL_FIRST() {
		return MCL_FIRST;
	}

	public void setMCL_FIRST(String mCLFIRST) {
		MCL_FIRST = mCLFIRST;
	}

	public String getMSPS_DESC() {
		return MSPS_DESC;
	}

	public void setMSPS_DESC(String mSPSDESC) {
		MSPS_DESC = mSPSDESC;
	}

	public String getSS() {
		return SS;
	}

	public void setSS(String sS) {
		SS = sS;
	}

	public String getHH() {
		return HH;
	}

	public void setHH(String hH) {
		HH = hH;
	}

	public String getDAYS() {
		return DAYS;
	}

	public void setDAYS(String dAYS) {
		DAYS = dAYS;
	}

	public String getMI() {
		return MI;
	}

	public void setMI(String mI) {
		MI = mI;
	}

	public String getLCA_NAMA() {
		return LCA_NAMA;
	}

	public void setLCA_NAMA(String lCANAMA) {
		LCA_NAMA = lCANAMA;
	}

	public String getLSBS_ID() {
		return LSBS_ID;
	}

	public void setLSBS_ID(String lSBSID) {
		LSBS_ID = lSBSID;
	}

	public String getPRODUK() {
		return PRODUK;
	}

	public void setPRODUK(String pRODUK) {
		PRODUK = pRODUK;
	}

	public String getLUS_LOGIN_NAME() {
		return LUS_LOGIN_NAME;
	}

	public void setLUS_LOGIN_NAME(String lUSLOGINNAME) {
		LUS_LOGIN_NAME = lUSLOGINNAME;
	}

	public String getREG_SPAJ() {
		return REG_SPAJ;
	}

	public void setREG_SPAJ(String rEGSPAJ) {
		REG_SPAJ = rEGSPAJ;
	}

	public String getSELISIH_SSL() {
		return SELISIH_SSL;
	}

	public void setSELISIH_SSL(String sELISIHSSL) {
		SELISIH_SSL = sELISIHSSL;
	}

	public Date getMSPO_TERIMA_TTP() {
		return MSPO_TERIMA_TTP;
	}

	public void setMSPO_TERIMA_TTP(Date mSPOTERIMATTP) {
		MSPO_TERIMA_TTP = mSPOTERIMATTP;
	}

	public Date getSYSDATE() {
		return SYSDATE;
	}

	public void setSYSDATE(Date sYSDATE) {
		SYSDATE = sYSDATE;
	}

	public Date getMSPO_DATE_PRINT() {
		return MSPO_DATE_PRINT;
	}

	public void setMSPO_DATE_PRINT(Date mSPODATEPRINT) {
		MSPO_DATE_PRINT = mSPODATEPRINT;
	}

	public Date getMSPS_DATE() {
		return MSPS_DATE;
	}

	public void setMSPS_DATE(Date mSPSDATE) {
		MSPS_DATE = mSPSDATE;
	}

	public Date getMSPO_DATE_TTP() {
		return MSPO_DATE_TTP;
	}

	public void setMSPO_DATE_TTP(Date mSPODATETTP) {
		MSPO_DATE_TTP = mSPODATETTP;
	}

	public Date getTERIMATTP() {
		return TERIMATTP;
	}

	public void setTERIMATTP(Date tERIMATTP) {
		TERIMATTP = tERIMATTP;
	}

	public Date getINPUTTTP() {
		return INPUTTTP;
	}

	public void setINPUTTTP(Date iNPUTTTP) {
		INPUTTTP = iNPUTTTP;
	}

	public Date getKIRIMPOLIS() {
		return KIRIMPOLIS;
	}

	public void setKIRIMPOLIS(Date kIRIMPOLIS) {
		KIRIMPOLIS = kIRIMPOLIS;
	}
	

}
