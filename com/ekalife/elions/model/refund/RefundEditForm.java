package com.ekalife.elions.model.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundEditForm
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 29, 2008 3:08:46 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.ekalife.elions.web.common.DownloadFormInterface;
import com.ekalife.elions.web.refund.vo.AlasanVO;
import com.ekalife.elions.web.refund.vo.LampiranListVO;
import com.ekalife.elions.web.refund.vo.PenarikanUlinkVO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.PreviewEditParamsVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;
import com.ekalife.elions.web.refund.vo.SetoranVO;
import com.ekalife.utils.Common;

public class RefundEditForm implements DownloadFormInterface, Serializable
{

	private static final long serialVersionUID = 6050730710281806854L;

	private String downloadFlag;

	private String spaj;
	private String spajBaru;
	private String PPBaru;
	private String TTBaru;
	private String produkBaru;
	private String alasan;
//    String lampiran;
	private Integer alasanCd;
	private Integer tindakanCd;
	private Integer checkSpajInDetRefund;
	private String invest;
	
	private BigDecimal biayaAdmin;
	private BigDecimal biayaMedis;
	private BigDecimal biayaLain;
	private String biayaLainDescr;
	private String currentUser;
    
	private ArrayList< PenarikanUlinkVO > penarikanUlinkVOList;
//    List< LampiranListVO > lampiranList;
	private ArrayList< LampiranListVO > lampiranAddList;
	private ArrayList< SetoranVO > setoranPokokAndTopUp;
    private ArrayList < SetoranPremiDbVO > setoranPokokAndTopUpChanged;
    private ArrayList < SetoranPremiDbVO > setoranPokokAndTopUpForSave;

    private String atasNama;
    private String norek;
    private String namaBank;
    private String cabangBank;
    private String kotaBank;
    private String statusUnit;
    private String uLinkOrNot;
    private ArrayList<HashMap> checkBoxLampiran;

    private String alasanDisplay;
    private String spajBaruDisplay;

    private String biayaAdminDisplay;
    private String biayaMedisDisplay;
    private String biayaLainDisplay;
    private String atasNamaDisplay;
    private String norekDisplay;
    private String namaBankDisplay;
    private String cabangBankDisplay;
    private String kotaBankDisplay;
    private String penarikanUlinkVOListDisplay;
    private String totalPremiDikembalikanDisplay;
    private String buttonBatalkanSpajDisplay;
    private String buttonNextDisplay;
    private String addLampiranDisplay;
    private String buttonSaveDraftDisplay;
    private String buttonPreviewSuratBatalDisplay;

    private ArrayList<DropDown> alasanList;
    private ArrayList<DropDown> tindakanList;
    private ArrayList<DropDown> lkuList;
    
    private ArrayList <AlasanVO> alasanVOList;
    
    private String addLampiranDisabled;
    private String buttonPreviewSuratBatalIsDisabled;
    private String spajIsDisabled;
    private String alasanListIsDisabled;
    private String alasanIsDisabled;
    private String tindakanListIsDisabled;
    private String spajBaruIsDisabled;
    private String biayaAdminIsDisabled;
    private String biayaMedisIsDisabled;
    private String biayaLainIsDisabled;
    private String atasNamaIsDisabled;
    private String norekIsDisabled;
    private String namaBankIsDisabled;
    private String cabangBankIsDisabled;
    private String kotaBankIsDisabled;
    private String biayaIsDisabled;
    private String buttonSendPhysicalDocIsDisabled;
    private String helpSaveSpajToCompare;
    private String tempSpaj;
    private String helpCompareSpaj;
    private Integer helpFlagCompareSpaj;
    private String spajHelpForAddLamp;
    private String spajNewHelpAddLamp;
    private String addLampiran;

    private PolicyInfoVO policyInfoVO;

    private Date cancelWhen;
    private Date createWhen;
    private Date updateWhen;
    private Integer posisiNo;
    private BigDecimal cancelWho;
    private BigDecimal createWho;
    private BigDecimal updateWho;
    private String updateWhoFullName;
    private BigDecimal biayaAdministrasi;
    private BigDecimal premiDikembalikan;
    private String penarikanULinkDescr;
    private BigDecimal penarikanULinkJumlah;
    private BigDecimal mtuUnit;

    private Date sendPhysicalDocDate;
    private Date paymentDate;
    private BigDecimal payment;
    private String paymentDisplay;
    private String paymentIsDisabled;
    private String sendPhysicalDocIsDisabled;
    private String sendPhysicalDocDateDisplay;

    private Integer hasUnitFlag;
    private String hasUnitDisplay;
    private String hasUnitListIsDisabled;
    private ArrayList <PreviewEditParamsVO> tempDescrDanJumlah;
//    List <PreviewEditParamsVO> tempDescrDanJumlahNew;
    private Integer flagHelp;
    private Integer prevLspdId;
    private Integer setoranPokokAndTopUpFlag;
    private Integer currentPage;
    
	private Integer merchantCd;
	private BigDecimal biayaMerchant;
    private String biayaMerchantDisplay;
	private ArrayList<DropDown> merchantList;
    private String merchantListIsDisabled;

	public ArrayList<SetoranPremiDbVO> getSetoranPokokAndTopUpChanged() {
		return setoranPokokAndTopUpChanged;
	}

	public void setSetoranPokokAndTopUpChanged(
			ArrayList<SetoranPremiDbVO> setoranPokokAndTopUpChanged) {
		this.setoranPokokAndTopUpChanged = Common.serializableList(setoranPokokAndTopUpChanged);
	}

	public Integer getSetoranPokokAndTopUpFlag() {
		return setoranPokokAndTopUpFlag;
	}

	public void setSetoranPokokAndTopUpFlag(Integer setoranPokokAndTopUpFlag) {
		this.setoranPokokAndTopUpFlag = setoranPokokAndTopUpFlag;
	}

	public Integer getPrevLspdId() {
		return prevLspdId;
	}

	public void setPrevLspdId(Integer prevLspdId) {
		this.prevLspdId = prevLspdId;
	}

	public Integer getFlagHelp() {
		return flagHelp;
	}

	public void setFlagHelp(Integer flagHelp) {
		this.flagHelp = flagHelp;
	}

	public String getDownloadFlag()
    {
        return downloadFlag;
    }

    public void setDownloadFlag( String downloadFlag )
    {
        this.downloadFlag = downloadFlag;
    }

    public String getSpaj()
    {
        return spaj;
    }

    public void setSpaj( String spaj )
    {
        this.spaj = spaj;
    }

    public String getSpajBaru()
    {
        return spajBaru;
    }

    public void setSpajBaru( String spajBaru )
    {
        this.spajBaru = spajBaru;
    }

    public String getAlasan()
    {
        return alasan;
    }

    public void setAlasan( String alasan )
    {
        this.alasan = alasan;
    }

    public Integer getAlasanCd()
    {
        return alasanCd;
    }

    public void setAlasanCd( Integer alasanCd )
    {
        this.alasanCd = alasanCd;
    }

    public Integer getTindakanCd()
    {
        return tindakanCd;
    }

    public void setTindakanCd( Integer tindakanCd )
    {
        this.tindakanCd = tindakanCd;
    }

    public BigDecimal getBiayaAdmin()
    {
        return biayaAdmin;
    }

    public void setBiayaAdmin( BigDecimal biayaAdmin )
    {
        this.biayaAdmin = biayaAdmin;
    }

    public BigDecimal getBiayaMedis()
    {
        return biayaMedis;
    }

    public void setBiayaMedis( BigDecimal biayaMedis )
    {
        this.biayaMedis = biayaMedis;
    }

    public BigDecimal getBiayaLain()
    {
        return biayaLain;
    }

    public void setBiayaLain( BigDecimal biayaLain )
    {
        this.biayaLain = biayaLain;
    }

    public String getBiayaLainDescr()
    {
        return biayaLainDescr;
    }

    public void setBiayaLainDescr( String biayaLainDescr )
    {
        this.biayaLainDescr = biayaLainDescr;
    }

    public ArrayList<PenarikanUlinkVO> getPenarikanUlinkVOList()
    {
        return penarikanUlinkVOList;
    }

    public void setPenarikanUlinkVOList( ArrayList<PenarikanUlinkVO> penarikanUlinkVOList )
    {
        this.penarikanUlinkVOList = Common.serializableList(penarikanUlinkVOList);
    }

    public String getAtasNama()
    {
        return atasNama;
    }

    public void setAtasNama( String atasNama )
    {
        this.atasNama = atasNama;
    }

    public String getNorek()
    {
        return norek;
    }

    public void setNorek( String norek )
    {
        this.norek = norek;
    }

    public String getNamaBank()
    {
        return namaBank;
    }

    public void setNamaBank( String namaBank )
    {
        this.namaBank = namaBank;
    }

    public String getCabangBank()
    {
        return cabangBank;
    }

    public void setCabangBank( String cabangBank )
    {
        this.cabangBank = cabangBank;
    }

    public String getKotaBank()
    {
        return kotaBank;
    }

    public void setKotaBank( String kotaBank )
    {
        this.kotaBank = kotaBank;
    }

    public String getAlasanDisplay()
    {
        return alasanDisplay;
    }

    public void setAlasanDisplay( String alasanDisplay )
    {
        this.alasanDisplay = alasanDisplay;
    }

    public String getSpajBaruDisplay()
    {
        return spajBaruDisplay;
    }

    public void setSpajBaruDisplay( String spajBaruDisplay )
    {
        this.spajBaruDisplay = spajBaruDisplay;
    }

    public String getBiayaAdminDisplay()
    {
        return biayaAdminDisplay;
    }

    public void setBiayaAdminDisplay( String biayaAdminDisplay )
    {
        this.biayaAdminDisplay = biayaAdminDisplay;
    }

    public String getBiayaMedisDisplay()
    {
        return biayaMedisDisplay;
    }

    public void setBiayaMedisDisplay( String biayaMedisDisplay )
    {
        this.biayaMedisDisplay = biayaMedisDisplay;
    }

    public String getBiayaLainDisplay()
    {
        return biayaLainDisplay;
    }

    public void setBiayaLainDisplay( String biayaLainDisplay )
    {
        this.biayaLainDisplay = biayaLainDisplay;
    }

    public String getAtasNamaDisplay()
    {
        return atasNamaDisplay;
    }

    public void setAtasNamaDisplay( String atasNamaDisplay )
    {
        this.atasNamaDisplay = atasNamaDisplay;
    }

    public String getNorekDisplay()
    {
        return norekDisplay;
    }

    public void setNorekDisplay( String norekDisplay )
    {
        this.norekDisplay = norekDisplay;
    }

    public String getNamaBankDisplay()
    {
        return namaBankDisplay;
    }

    public void setNamaBankDisplay( String namaBankDisplay )
    {
        this.namaBankDisplay = namaBankDisplay;
    }

    public String getCabangBankDisplay()
    {
        return cabangBankDisplay;
    }

    public void setCabangBankDisplay( String cabangBankDisplay )
    {
        this.cabangBankDisplay = cabangBankDisplay;
    }

    public String getKotaBankDisplay()
    {
        return kotaBankDisplay;
    }

    public void setKotaBankDisplay( String kotaBankDisplay )
    {
        this.kotaBankDisplay = kotaBankDisplay;
    }

    public String getPenarikanUlinkVOListDisplay()
    {
        return penarikanUlinkVOListDisplay;
    }

    public void setPenarikanUlinkVOListDisplay( String penarikanUlinkVOListDisplay )
    {
        this.penarikanUlinkVOListDisplay = penarikanUlinkVOListDisplay;
    }

    public String getTotalPremiDikembalikanDisplay()
    {
        return totalPremiDikembalikanDisplay;
    }

    public void setTotalPremiDikembalikanDisplay( String totalPremiDikembalikanDisplay )
    {
        this.totalPremiDikembalikanDisplay = totalPremiDikembalikanDisplay;
    }

    public String getButtonBatalkanSpajDisplay()
    {
        return buttonBatalkanSpajDisplay;
    }

    public void setButtonBatalkanSpajDisplay( String buttonBatalkanSpajDisplay )
    {
        this.buttonBatalkanSpajDisplay = buttonBatalkanSpajDisplay;
    }

    public String getButtonNextDisplay()
    {
        return buttonNextDisplay;
    }

    public void setButtonNextDisplay( String buttonNextDisplay )
    {
        this.buttonNextDisplay = buttonNextDisplay;
    }

    public String getButtonSaveDraftDisplay()
    {
        return buttonSaveDraftDisplay;
    }

    public void setButtonSaveDraftDisplay( String buttonSaveDraftDisplay )
    {
        this.buttonSaveDraftDisplay = buttonSaveDraftDisplay;
    }

    public ArrayList<DropDown> getAlasanList()
    {
        return alasanList;
    }

    public void setAlasanList( ArrayList<DropDown> alasanList )
    {
        this.alasanList = Common.serializableList(alasanList);
    }

    public ArrayList<DropDown> getTindakanList()
    {
        return tindakanList;
    }

    public void setTindakanList( ArrayList<DropDown> tindakanList )
    {
        this.tindakanList = Common.serializableList(tindakanList);
    }

    public String getSpajIsDisabled()
    {
        return spajIsDisabled;
    }

    public void setSpajIsDisabled( String spajIsDisabled )
    {
        this.spajIsDisabled = spajIsDisabled;
    }

    public String getAlasanListIsDisabled()
    {
        return alasanListIsDisabled;
    }

    public void setAlasanListIsDisabled( String alasanListIsDisabled )
    {
        this.alasanListIsDisabled = alasanListIsDisabled;
    }

    public String getAlasanIsDisabled()
    {
        return alasanIsDisabled;
    }

    public void setAlasanIsDisabled( String alasanIsDisabled )
    {
        this.alasanIsDisabled = alasanIsDisabled;
    }

    public String getTindakanListIsDisabled()
    {
        return tindakanListIsDisabled;
    }

    public void setTindakanListIsDisabled( String tindakanListIsDisabled )
    {
        this.tindakanListIsDisabled = tindakanListIsDisabled;
    }

    public String getSpajBaruIsDisabled()
    {
        return spajBaruIsDisabled;
    }

    public void setSpajBaruIsDisabled( String spajBaruIsDisabled )
    {
        this.spajBaruIsDisabled = spajBaruIsDisabled;
    }

    public String getBiayaAdminIsDisabled()
    {
        return biayaAdminIsDisabled;
    }

    public void setBiayaAdminIsDisabled( String biayaAdminIsDisabled )
    {
        this.biayaAdminIsDisabled = biayaAdminIsDisabled;
    }

    public String getBiayaMedisIsDisabled()
    {
        return biayaMedisIsDisabled;
    }

    public void setBiayaMedisIsDisabled( String biayaMedisIsDisabled )
    {
        this.biayaMedisIsDisabled = biayaMedisIsDisabled;
    }

    public String getBiayaLainIsDisabled()
    {
        return biayaLainIsDisabled;
    }

    public void setBiayaLainIsDisabled( String biayaLainIsDisabled )
    {
        this.biayaLainIsDisabled = biayaLainIsDisabled;
    }

    public String getAtasNamaIsDisabled()
    {
        return atasNamaIsDisabled;
    }

    public void setAtasNamaIsDisabled( String atasNamaIsDisabled )
    {
        this.atasNamaIsDisabled = atasNamaIsDisabled;
    }

    public String getNorekIsDisabled()
    {
        return norekIsDisabled;
    }

    public void setNorekIsDisabled( String norekIsDisabled )
    {
        this.norekIsDisabled = norekIsDisabled;
    }

    public String getNamaBankIsDisabled()
    {
        return namaBankIsDisabled;
    }

    public void setNamaBankIsDisabled( String namaBankIsDisabled )
    {
        this.namaBankIsDisabled = namaBankIsDisabled;
    }

    public String getCabangBankIsDisabled()
    {
        return cabangBankIsDisabled;
    }

    public void setCabangBankIsDisabled( String cabangBankIsDisabled )
    {
        this.cabangBankIsDisabled = cabangBankIsDisabled;
    }

    public String getKotaBankIsDisabled()
    {
        return kotaBankIsDisabled;
    }

    public void setKotaBankIsDisabled( String kotaBankIsDisabled )
    {
        this.kotaBankIsDisabled = kotaBankIsDisabled;
    }

    public String getBiayaIsDisabled()
    {
        return biayaIsDisabled;
    }

    public void setBiayaIsDisabled( String biayaIsDisabled )
    {
        this.biayaIsDisabled = biayaIsDisabled;
    }

    public PolicyInfoVO getPolicyInfoVO()
    {
        return policyInfoVO;
    }

    public void setPolicyInfoVO( PolicyInfoVO policyInfoVO )
    {
        this.policyInfoVO = policyInfoVO;
    }

    public Integer getPosisiNo()
    {
        return posisiNo;
    }

    public void setPosisiNo( Integer posisiNo )
    {
        this.posisiNo = posisiNo;
    }

    public BigDecimal getCreateWho()
    {
        return createWho;
    }

    public void setCreateWho( BigDecimal createWho )
    {
        this.createWho = createWho;
    }

    public BigDecimal getUpdateWho()
    {
        return updateWho;
    }

    public void setUpdateWho( BigDecimal updateWho )
    {
        this.updateWho = updateWho;
    }

    public Date getCreateWhen()
    {
        return createWhen;
    }

    public void setCreateWhen( Date createWhen )
    {
        this.createWhen = createWhen;
    }

    public Date getUpdateWhen()
    {
        return updateWhen;
    }

    public void setUpdateWhen( Date updateWhen )
    {
        this.updateWhen = updateWhen;
    }

    public Date getPaymentDate()
    {
        return paymentDate;
    }

    public void setPaymentDate( Date paymentDate )
    {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getPayment()
    {
        return payment;
    }

    public void setPayment( BigDecimal payment )
    {
        this.payment = payment;
    }

    public String getPaymentDisplay()
    {
        return paymentDisplay;
    }

    public void setPaymentDisplay( String paymentDisplay )
    {
        this.paymentDisplay = paymentDisplay;
    }


    public String getPaymentIsDisabled()
    {
        return paymentIsDisabled;
    }

    public void setPaymentIsDisabled( String paymentIsDisabled )
    {
        this.paymentIsDisabled = paymentIsDisabled;
    }

    public String getHasUnitDisplay() {
        return hasUnitDisplay;
    }

    public void setHasUnitDisplay(String hasUnitDisplay) {
        this.hasUnitDisplay = hasUnitDisplay;
    }

    public String getHasUnitListIsDisabled() {
        return hasUnitListIsDisabled;
    }

    public void setHasUnitListIsDisabled(String hasUnitListIsDisabled) {
        this.hasUnitListIsDisabled = hasUnitListIsDisabled;
    }

    public Integer getHasUnitFlag() {
        return hasUnitFlag;
    }

    public void setHasUnitFlag(Integer hasUnitFlag) {
        this.hasUnitFlag = hasUnitFlag;
    }

	public String getStatusUnit() {
		return statusUnit;
	}

	public void setStatusUnit(String statusUnit) {
		this.statusUnit = statusUnit;
	}

	public ArrayList<PreviewEditParamsVO> getTempDescrDanJumlah() {
		return tempDescrDanJumlah;
	}

	public void setTempDescrDanJumlah(ArrayList<PreviewEditParamsVO> tempDescrDanJumlah) {
		this.tempDescrDanJumlah = Common.serializableList(tempDescrDanJumlah);
	}

//	public List<PreviewEditParamsVO> getTempDescrDanJumlahNew() {
//		return tempDescrDanJumlahNew;
//	}
//
//	public void setTempDescrDanJumlahNew(
//			List<PreviewEditParamsVO> tempDescrDanJumlahNew) {
//		this.tempDescrDanJumlahNew = tempDescrDanJumlahNew;
//	}

	public BigDecimal getBiayaAdministrasi() {
		return biayaAdministrasi;
	}

	public void setBiayaAdministrasi(BigDecimal biayaAdministrasi) {
		this.biayaAdministrasi = biayaAdministrasi;
	}

	public BigDecimal getPremiDikembalikan() {
		return premiDikembalikan;
	}

	public void setPremiDikembalikan(BigDecimal premiDikembalikan) {
		this.premiDikembalikan = premiDikembalikan;
	}

	public String getULinkOrNot() {
		return uLinkOrNot;
	}

	public void setULinkOrNot(String linkOrNot) {
		uLinkOrNot = linkOrNot;
	}

	public String getPenarikanULinkDescr() {
		return penarikanULinkDescr;
	}

	public void setPenarikanULinkDescr(String penarikanULinkDescr) {
		this.penarikanULinkDescr = penarikanULinkDescr;
	}

	public BigDecimal getPenarikanULinkJumlah() {
		return penarikanULinkJumlah;
	}

	public void setPenarikanULinkJumlah(BigDecimal penarikanULinkJumlah) {
		this.penarikanULinkJumlah = penarikanULinkJumlah;
	}

	public ArrayList<DropDown> getLkuList() {
		return lkuList;
	}

	public void setLkuList(ArrayList<DropDown> lkuList) {
		this.lkuList = Common.serializableList(lkuList);
	}

	public String getTempSpaj() {
		return tempSpaj;
	}

	public void setTempSpaj(String tempSpaj) {
		this.tempSpaj = tempSpaj;
	}

	public Integer getCheckSpajInDetRefund() {
		return checkSpajInDetRefund;
	}

	public void setCheckSpajInDetRefund(Integer checkSpajInDetRefund) {
		this.checkSpajInDetRefund = checkSpajInDetRefund;
	}

	public String getHelpCompareSpaj() {
		return helpCompareSpaj;
	}

	public void setHelpCompareSpaj(String helpCompareSpaj) {
		this.helpCompareSpaj = helpCompareSpaj;
	}

	public Integer getHelpFlagCompareSpaj() {
		return helpFlagCompareSpaj;
	}

	public void setHelpFlagCompareSpaj(Integer helpFlagCompareSpaj) {
		this.helpFlagCompareSpaj = helpFlagCompareSpaj;
	}

//	public List<LampiranListVO> getLampiranList() {
//		return lampiranList;
//	}
//
//	public void setLampiranList(List<LampiranListVO> lampiranList) {
//		this.lampiranList = lampiranList;
//	}

	public ArrayList getCheckBoxLampiran() {
		return checkBoxLampiran;
	}

	public void setCheckBoxLampiran(ArrayList checkBoxLampiran) {
		this.checkBoxLampiran = Common.serializableList(checkBoxLampiran);
	}

	public String getButtonPreviewSuratBatalDisplay() {
		return buttonPreviewSuratBatalDisplay;
	}

	public void setButtonPreviewSuratBatalDisplay(
			String buttonPreviewSuratBatalDisplay) {
		this.buttonPreviewSuratBatalDisplay = buttonPreviewSuratBatalDisplay;
	}

//	public String getLampiran() {
//		return lampiran;
//	}
//
//	public void setLampiran(String lampiran) {
//		this.lampiran = lampiran;
//	}

	public String getButtonPreviewSuratBatalIsDisabled() {
		return buttonPreviewSuratBatalIsDisabled;
	}

	public void setButtonPreviewSuratBatalIsDisabled(
			String buttonPreviewSuratBatalIsDisabled) {
		this.buttonPreviewSuratBatalIsDisabled = buttonPreviewSuratBatalIsDisabled;
	}

	public String getUpdateWhoFullName() {
		return updateWhoFullName;
	}

	public void setUpdateWhoFullName(String updateWhoFullName) {
		this.updateWhoFullName = updateWhoFullName;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public Date getSendPhysicalDocDate() {
		return sendPhysicalDocDate;
	}

	public void setSendPhysicalDocDate(Date sendPhysicalDocDate) {
		this.sendPhysicalDocDate = sendPhysicalDocDate;
	}

	public String getSendPhysicalDocIsDisabled() {
		return sendPhysicalDocIsDisabled;
	}

	public void setSendPhysicalDocIsDisabled(String sendPhysicalDocIsDisabled) {
		this.sendPhysicalDocIsDisabled = sendPhysicalDocIsDisabled;
	}

	public String getSendPhysicalDocDateDisplay() {
		return sendPhysicalDocDateDisplay;
	}

	public void setSendPhysicalDocDateDisplay(String sendPhysicalDocDateDisplay) {
		this.sendPhysicalDocDateDisplay = sendPhysicalDocDateDisplay;
	}

	public String getButtonSendPhysicalDocIsDisabled() {
		return buttonSendPhysicalDocIsDisabled;
	}

	public void setButtonSendPhysicalDocIsDisabled(
			String buttonSendPhysicalDocIsDisabled) {
		this.buttonSendPhysicalDocIsDisabled = buttonSendPhysicalDocIsDisabled;
	}

	public String getHelpSaveSpajToCompare() {
		return helpSaveSpajToCompare;
	}

	public void setHelpSaveSpajToCompare(String helpSaveSpajToCompare) {
		this.helpSaveSpajToCompare = helpSaveSpajToCompare;
	}

	public Date getCancelWhen() {
		return cancelWhen;
	}

	public void setCancelWhen(Date cancelWhen) {
		this.cancelWhen = cancelWhen;
	}

	public BigDecimal getCancelWho() {
		return cancelWho;
	}

	public void setCancelWho(BigDecimal cancelWho) {
		this.cancelWho = cancelWho;
	}

	public ArrayList<AlasanVO> getAlasanVOList() {
		return alasanVOList;
	}

	public void setAlasanVOList(ArrayList<AlasanVO> alasanVOList) {
		this.alasanVOList = Common.serializableList(alasanVOList);
	}

	public BigDecimal getMtuUnit() {
		return mtuUnit;
	}

	public void setMtuUnit(BigDecimal mtuUnit) {
		this.mtuUnit = mtuUnit;
	}

	public ArrayList<SetoranVO> getSetoranPokokAndTopUp() {
		return setoranPokokAndTopUp;
	}

	public void setSetoranPokokAndTopUp(ArrayList<SetoranVO> setoranPokokAndTopUp) {
		this.setoranPokokAndTopUp = Common.serializableList(setoranPokokAndTopUp);
	}

	public ArrayList<SetoranPremiDbVO> getSetoranPokokAndTopUpForSave() {
		return setoranPokokAndTopUpForSave;
	}

	public void setSetoranPokokAndTopUpForSave(
			ArrayList<SetoranPremiDbVO> setoranPokokAndTopUpForSave) {
		this.setoranPokokAndTopUpForSave = Common.serializableList(setoranPokokAndTopUpForSave);
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public String getInvest() {
		return invest;
	}

	public void setInvest(String invest) {
		this.invest = invest;
	}

	public ArrayList<LampiranListVO> getLampiranAddList() {
		return lampiranAddList;
	}

	public void setLampiranAddList(ArrayList<LampiranListVO> lampiranAddList) {
		this.lampiranAddList = Common.serializableList(lampiranAddList);
	}

	public String getAddLampiran() {
		return addLampiran;
	}

	public void setAddLampiran(String addLampiran) {
		this.addLampiran = addLampiran;
	}

	public String getAddLampiranDisplay() {
		return addLampiranDisplay;
	}

	public void setAddLampiranDisplay(String addLampiranDisplay) {
		this.addLampiranDisplay = addLampiranDisplay;
	}

	public String getSpajHelpForAddLamp() {
		return spajHelpForAddLamp;
	}

	public void setSpajHelpForAddLamp(String spajHelpForAddLamp) {
		this.spajHelpForAddLamp = spajHelpForAddLamp;
	}

	public String getAddLampiranDisabled() {
		return addLampiranDisabled;
	}

	public void setAddLampiranDisabled(String addLampiranDisabled) {
		this.addLampiranDisabled = addLampiranDisabled;
	}

	public String getSpajNewHelpAddLamp() {
		return spajNewHelpAddLamp;
	}

	public void setSpajNewHelpAddLamp(String spajNewHelpAddLamp) {
		this.spajNewHelpAddLamp = spajNewHelpAddLamp;
	}
	
    public String getPPBaru() {
		return PPBaru;
	}
    
	public void setPPBaru(String pPBaru) {
		PPBaru = pPBaru;
	}

	public String getTTBaru() {
		return TTBaru;
	}
	
	public void setTTBaru(String tTBaru) {
		TTBaru = tTBaru;
	}

	public String getProdukBaru() {
		return produkBaru;
	}
	
	public void setProdukBaru(String produkBaru) {
		this.produkBaru = produkBaru;
	}
	
    public Integer getMerchantCd() {
		return merchantCd;
	}
    
	public void setMerchantCd(Integer merchantCd) {
		this.merchantCd = merchantCd;
	}
	
	public BigDecimal getBiayaMerchant() {
		return biayaMerchant;
	}
	
	public void setBiayaMerchant(BigDecimal biayaMerchant) {
		this.biayaMerchant = biayaMerchant;
	}
	
	public String getBiayaMerchantDisplay() {
		return biayaMerchantDisplay;
	}

	public void setBiayaMerchantDisplay(String biayaMerchantDisplay) {
		this.biayaMerchantDisplay = biayaMerchantDisplay;
	}

	public ArrayList<DropDown> getMerchantList() {
		return merchantList;
	}

	public void setMerchantList(ArrayList<DropDown> merchantList) {
		this.merchantList = merchantList;
	}

	public String getMerchantListIsDisabled() {
		return merchantListIsDisabled;
	}

	public void setMerchantListIsDisabled(String merchantListIsDisabled) {
		this.merchantListIsDisabled = merchantListIsDisabled;
	}
}
