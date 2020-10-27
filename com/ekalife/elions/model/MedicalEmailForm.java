package com.ekalife.elions.model;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Bancass
 * Function Id         	: 
 * Program Name   		: MedicalEmailForm
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Mar 13, 2008 10:18:28 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.io.Serializable;
import java.util.List;

import com.ekalife.elions.model.vo.JenisMedicalVO;
import com.ekalife.elions.model.vo.MedicalCheckupVO;
import com.ekalife.elions.model.vo.PertimbanganMedicalVO;

public class MedicalEmailForm implements Serializable
{
	private static final long serialVersionUID = 8048280907936734609L;
	private String spaj;
    private List<JenisMedicalVO> jenisMedicalVOList;
    private Integer jenisMedisCd;
    private List<MedicalCheckupVO> medicalCheckupVOList;
    private List<PertimbanganMedicalVO> pertimbanganMedicalVOList;
    private String generalErrMsg;
    private String letterNo;
    private String policyHolderName;
    private String insuredName;
    private String beautifiedSpaj;
    private String generalMsg;
    private String policyHolderPrefix;
    private String policyHolderSuffix;
    private String insuredPrefix;
    private String insuredSuffix;

    public Integer getJenisMedisCd()
    {
        return jenisMedisCd;
    }

    public void setJenisMedisCd( Integer jenisMedisCd )
    {
        this.jenisMedisCd = jenisMedisCd;
    }

    public String getSpaj()
    {
        return spaj;
    }

    public void setSpaj( String spaj )
    {
        this.spaj = spaj;
    }

    public List<JenisMedicalVO> getJenisMedicalVOList()
    {
        return jenisMedicalVOList;
    }

    public void setJenisMedicalVOList( List<JenisMedicalVO> jenisMedicalVOList )
    {
        this.jenisMedicalVOList = jenisMedicalVOList;
    }

    public List<MedicalCheckupVO> getMedicalCheckupVOList()
    {
        return medicalCheckupVOList;
    }

    public void setMedicalCheckupVOList( List<MedicalCheckupVO> medicalCheckupVOList )
    {
        this.medicalCheckupVOList = medicalCheckupVOList;
    }

    public List<PertimbanganMedicalVO> getPertimbanganMedicalVOList()
    {
        return pertimbanganMedicalVOList;
    }

    public void setPertimbanganMedicalVOList( List<PertimbanganMedicalVO> pertimbanganMedicalVOList )
    {
        this.pertimbanganMedicalVOList = pertimbanganMedicalVOList;
    }

    public String getGeneralErrMsg()
    {
        return generalErrMsg;
    }

    public void setGeneralErrMsg( String generalErrMsg )
    {
        this.generalErrMsg = generalErrMsg;
    }

    public String getLetterNo()
    {
        return letterNo;
    }

    public void setLetterNo( String letterNo )
    {
        this.letterNo = letterNo;
    }

    public String getPolicyHolderName()
    {
        return policyHolderName;
    }

    public void setPolicyHolderName( String policyHolderName )
    {
        this.policyHolderName = policyHolderName;
    }

    public String getInsuredName()
    {
        return insuredName;
    }

    public void setInsuredName( String insuredName )
    {
        this.insuredName = insuredName;
    }

    public String getBeautifiedSpaj()
    {
        return beautifiedSpaj;
    }

    public void setBeautifiedSpaj( String beautifiedSpaj )
    {
        this.beautifiedSpaj = beautifiedSpaj;
    }

    public String getGeneralMsg()
    {
        return generalMsg;
    }

    public void setGeneralMsg( String generalMsg )
    {
        this.generalMsg = generalMsg;
    }

    public String getPolicyHolderPrefix()
    {
        return policyHolderPrefix;
    }

    public void setPolicyHolderPrefix( String policyHolderPrefix )
    {
        this.policyHolderPrefix = policyHolderPrefix;
    }

    public String getPolicyHolderSuffix()
    {
        return policyHolderSuffix;
    }

    public void setPolicyHolderSuffix( String policyHolderSuffix )
    {
        this.policyHolderSuffix = policyHolderSuffix;
    }

    public String getInsuredPrefix()
    {
        return insuredPrefix;
    }

    public void setInsuredPrefix( String insuredPrefix )
    {
        this.insuredPrefix = insuredPrefix;
    }

    public String getInsuredSuffix()
    {
        return insuredSuffix;
    }

    public void setInsuredSuffix( String insuredSuffix )
    {
        this.insuredSuffix = insuredSuffix;
    }
}
