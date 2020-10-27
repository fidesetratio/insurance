package com.ekalife.elions.model.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: CabangEditForm
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Jul 16, 2009 2:14:06 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ekalife.elions.web.refund.vo.AlasanVO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;

public class CabangEditForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
    String spaj;
    String alasan;
    Integer alasanCd;
    String currentUser;

    String alasanDisplay;
    String buttonBatalkanSpajDisplay;

    List<DropDown> alasanList;
    List <AlasanVO> alasanVOList;

    String spajIsDisabled;
    String alasanListIsDisabled;
    String alasanIsDisabled;
    PolicyInfoVO policyInfoVO;

    Date cancelWhen;
    Date createWhen;
    Date updateWhen;
    BigDecimal cancelWho;
    BigDecimal createWho;
    BigDecimal updateWho;
    String updateWhoFullName;


    public String getSpaj() {
        return spaj;
    }

    public void setSpaj(String spaj) {
        this.spaj = spaj;
    }

    public String getAlasan() {
        return alasan;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
    }

    public Integer getAlasanCd() {
        return alasanCd;
    }

    public void setAlasanCd(Integer alasanCd) {
        this.alasanCd = alasanCd;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getAlasanDisplay() {
        return alasanDisplay;
    }

    public void setAlasanDisplay(String alasanDisplay) {
        this.alasanDisplay = alasanDisplay;
    }

    public String getButtonBatalkanSpajDisplay() {
        return buttonBatalkanSpajDisplay;
    }

    public void setButtonBatalkanSpajDisplay(String buttonBatalkanSpajDisplay) {
        this.buttonBatalkanSpajDisplay = buttonBatalkanSpajDisplay;
    }

    public List<DropDown> getAlasanList() {
        return alasanList;
    }

    public void setAlasanList(List<DropDown> alasanList) {
        this.alasanList = alasanList;
    }

    public List<AlasanVO> getAlasanVOList() {
        return alasanVOList;
    }

    public void setAlasanVOList(List<AlasanVO> alasanVOList) {
        this.alasanVOList = alasanVOList;
    }

    public String getSpajIsDisabled() {
        return spajIsDisabled;
    }

    public void setSpajIsDisabled(String spajIsDisabled) {
        this.spajIsDisabled = spajIsDisabled;
    }

    public String getAlasanListIsDisabled() {
        return alasanListIsDisabled;
    }

    public void setAlasanListIsDisabled(String alasanListIsDisabled) {
        this.alasanListIsDisabled = alasanListIsDisabled;
    }

    public String getAlasanIsDisabled() {
        return alasanIsDisabled;
    }

    public void setAlasanIsDisabled(String alasanIsDisabled) {
        this.alasanIsDisabled = alasanIsDisabled;
    }

    public PolicyInfoVO getPolicyInfoVO() {
        return policyInfoVO;
    }

    public void setPolicyInfoVO(PolicyInfoVO policyInfoVO) {
        this.policyInfoVO = policyInfoVO;
    }

    public Date getCancelWhen() {
        return cancelWhen;
    }

    public void setCancelWhen(Date cancelWhen) {
        this.cancelWhen = cancelWhen;
    }

    public Date getCreateWhen() {
        return createWhen;
    }

    public void setCreateWhen(Date createWhen) {
        this.createWhen = createWhen;
    }

    public Date getUpdateWhen() {
        return updateWhen;
    }

    public void setUpdateWhen(Date updateWhen) {
        this.updateWhen = updateWhen;
    }

    public BigDecimal getCancelWho() {
        return cancelWho;
    }

    public void setCancelWho(BigDecimal cancelWho) {
        this.cancelWho = cancelWho;
    }

    public BigDecimal getCreateWho() {
        return createWho;
    }

    public void setCreateWho(BigDecimal createWho) {
        this.createWho = createWho;
    }

    public BigDecimal getUpdateWho() {
        return updateWho;
    }

    public void setUpdateWho(BigDecimal updateWho) {
        this.updateWho = updateWho;
    }

    public String getUpdateWhoFullName() {
        return updateWhoFullName;
    }

    public void setUpdateWhoFullName(String updateWhoFullName) {
        this.updateWhoFullName = updateWhoFullName;
    }
}
