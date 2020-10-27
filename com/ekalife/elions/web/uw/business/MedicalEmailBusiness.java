package com.ekalife.elions.web.uw.business;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: MedicalEmailBusiness
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Mar 14, 2008 10:30:05 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import com.ekalife.elions.model.vo.PertimbanganMedicalVO;
import com.ekalife.elions.model.vo.JenisMedicalVO;
import com.ekalife.elions.model.vo.MedicalCheckupVO;
import com.ekalife.elions.model.MedicalEmailForm;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.utils.FormatDate;

import java.util.*;

public class MedicalEmailBusiness
{
    public MedicalEmailBusiness()
    {
    }

    public List<PertimbanganMedicalVO> genPertimbanganMedicalVOList()
    {
        List<PertimbanganMedicalVO> result = new ArrayList<PertimbanganMedicalVO>();
        PertimbanganMedicalVO vo;

        vo = new PertimbanganMedicalVO( 1, "Riwayat Kesehatan" );
        result.add( vo );

        vo = new PertimbanganMedicalVO( 2, "Riwayat Kesehatan Keluarga" );
        result.add( vo );

        vo = new PertimbanganMedicalVO( 3, "Hasil Pemeriksaan Kesehatan" );
        result.add( vo );

        vo = new PertimbanganMedicalVO( 4, "Peraturan Perusahaan" );
        result.add( vo );

        return result;
    }

    public String genScriptCheckMedicalCheckupAccordingToJenisMedis( MedicalEmailForm cmd, ElionsManager elionsManager )
    {
        StringBuffer result = new StringBuffer( "\n" );
        List<Integer> lsmcIdList;
        Integer jnsMedisCd;
        List<JenisMedicalVO> jenisMedicalVOList = cmd.getJenisMedicalVOList();
        List<MedicalCheckupVO> medicalCheckupVOList = cmd.getMedicalCheckupVOList();
        int foundIndex = -1;

        for( int i = 0; i < medicalCheckupVOList.size(); i++ )
        {
            result = result.append( "checkForm( 'medicalCheckupVOList[" ).append( i ).append( "].flag', false );\n" ); 
        }

        for( JenisMedicalVO vo : jenisMedicalVOList )
        {
            jnsMedisCd = vo.getCd();

            result = result.append( "if( jnsMedisCd == " ).append( jnsMedisCd ).append( " )\n" );
            result = result.append( "{\n" );
            lsmcIdList = elionsManager.selectMedicalCheckupListByJenisMedis( jnsMedisCd );
            for( Integer lsmcId : lsmcIdList )
            {
                for( int i = 0; i < medicalCheckupVOList.size(); i++ )
                {
                    MedicalCheckupVO medicalCheckupVO = medicalCheckupVOList.get( i );
                    if( medicalCheckupVO.getCd().equals( lsmcId ) )
                    {
                        foundIndex = i;
                        break;
                    }
                }
                result = result.append( "    checkForm( 'medicalCheckupVOList[" ).append( foundIndex ).append( "].flag', isChecked );\n" );
            }
            result = result.append( "}\n" );
            result = result.append( "\n" );
        }
        return result.toString();
    }

    public List<Map<String, String>> genTableDetail( MedicalEmailForm medicalEmailForm )
    {
        List<PertimbanganMedicalVO> pertimbanganMedicalVOList = medicalEmailForm.getPertimbanganMedicalVOList();
        List<Map<String, String>> tableDetail = new ArrayList<Map<String, String>>();
        Map<String, String> rowMap;
        String[] pertimbanganFlag = new String[ pertimbanganMedicalVOList.size() ];
        String[] pertimbangan = {
                "Riwayat Kesehatan",
                "Riwayat Kesehatan Keluarga",
                "Hasil Pemeriksaan Kesehatan",
                "Peraturan Perusahaan",
        };

        for( int i = 0; i < pertimbanganMedicalVOList.size(); i++ )
        {
            PertimbanganMedicalVO vo = pertimbanganMedicalVOList.get( i );
            pertimbanganFlag[ i ] = genChecked( vo.getFlag() );
        }

        for( int i = 0; i < medicalEmailForm.getMedicalCheckupVOList().size(); i++ )
        {
            MedicalCheckupVO vo = medicalEmailForm.getMedicalCheckupVOList().get( i );
            rowMap = new HashMap<String, String>();
            rowMap.put( "jenisFlag", genChecked( vo.getFlag() ) );
            rowMap.put( "jenis", vo.getDescr() );

            if( i >= 0 && i < pertimbanganMedicalVOList.size() )
            {
                rowMap.put( "pertimbanganFlag", pertimbanganFlag[ i ] );
                rowMap.put( "pertimbangan", pertimbangan[ i ] );
            }

            tableDetail.add( rowMap );
        }

        return tableDetail;
    }

    private String genChecked( String isCheckedFlag )
    {
        boolean isChecked = CommonConst.CHECKED_TRUE.equals( isCheckedFlag );
        return isChecked? "V" : "";
    }

    public Map<String, Object> getDownloadParams( MedicalEmailForm form )
    {
    	String tempPrefix;
        String tempSuffix;

        tempPrefix = form.getPolicyHolderPrefix().trim().equals( "" )? "" : form.getPolicyHolderPrefix().trim() + " ";
        tempSuffix = form.getPolicyHolderSuffix().trim().equals( "" )? "" : ", " + form.getPolicyHolderSuffix().trim();
        String policyHolderFullName =
        		tempPrefix +
                form.getPolicyHolderName().trim() +
                tempSuffix;

        tempPrefix = form.getInsuredPrefix().trim().equals( "" )? "" : form.getInsuredPrefix().trim() + " ";
        tempSuffix = form.getInsuredSuffix().trim().equals( "" )? "" : ", " + form.getInsuredSuffix().trim();
        String insuredFullName =
        		tempPrefix +
                form.getInsuredName().trim() +
                tempSuffix;

        String preface = ( "" ).concat(
        "Terima kasih atas pengajuan asuransi yang ditujukan kepada PT. ASURANSI JIWA Sinarmas. " ).concat(
        "Setelah dikaji dengan seksama berdasarkan ketentuan yang berlaku, dengan ini diberitahukan " ).concat(
        "bahwa atas permintaan tersebut masih diperlukan adanya persyaratan dan atau pemeriksaan medis dari " ).concat(
        insuredFullName ).concat(
        " sebagai berikut (hanya yang diberi tanda \"V\")." );

        Map<String, Object> result = new HashMap<String, Object>();

        String placeAndDate = "Jakarta, " + FormatDate.toIndonesian( new Date() );
        result.put( "placeAndDate", placeAndDate );
        result.put( "letterNo", form.getLetterNo() );
        result.put( "policyHolderName", policyHolderFullName );
        result.put( "subject", "SPAJ no. " + form.getBeautifiedSpaj() + " a/n " + insuredFullName );
        result.put( "preface", preface );

        return result;
    }
}
