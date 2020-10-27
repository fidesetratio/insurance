package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundRedemptHelper
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Nov 21, 2008 10:39:23 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.model.refund.RefundForm;
import com.ekalife.elions.model.refund.RefundPreviewInstruksiRedemptForm;
import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.elions.web.refund.vo.CheckSpajParamsVO;
import com.ekalife.elions.web.refund.vo.RedemptParamsVO;
import com.ekalife.elions.web.refund.vo.RedemptVO;
import com.ekalife.elions.web.refund.vo.RefundDocumentVO;

public class RefundRedemptHelper extends RefundHelperParent
{
    protected final Log logger = LogFactory.getLog( getClass() );

    public RefundRedemptHelper()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundRedemptHelper constructor is called ..." );
    }

    private RefundBusiness refundBusiness;

    public void setRefundBusiness( RefundBusiness refundBusiness )
    {
        this.refundBusiness = refundBusiness;
    }

    private RedemptParamsVO prepareRedemptParamsVO( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper.prepareRedemptParamsVO" );

        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        RefundPreviewInstruksiRedemptForm redemptForm = ( ( RefundForm ) command ).getRedemptForm();
        RedemptParamsVO paramsVO = new RedemptParamsVO();
        paramsVO.setSpajNo( editForm.getSpaj() );
        paramsVO.setAlasan( genAlasan( editForm ) );
        paramsVO.setAlasanForLabel( genAlasanForLabel( editForm ) );
        if( editForm.getPosisiNo() == null )
        {
        	paramsVO.setNamaUnderwriter( editForm.getCurrentUser() );
        	redemptForm.setNamaUnderwriter( editForm.getCurrentUser() );
        }
        else
        {
        	paramsVO.setNamaUnderwriter( editForm.getUpdateWhoFullName() );
        	redemptForm.setNamaUnderwriter( editForm.getUpdateWhoFullName() );
        }
        paramsVO.setPenarikanUlinkVOList( editForm.getPenarikanUlinkVOList() );
        return paramsVO;
    }

    
    public HashMap<String, Object> genParamsRedemptPdf( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper.genParamsRedemptPdf" );
        RedemptParamsVO paramsVO = prepareRedemptParamsVO( command );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        // ini adalah bagian inti dari memproses report
        
        RedemptVO redemptVO = refundBusiness.retrieveRedempt( paramsVO );
        CheckSpajParamsVO checkSpajInDb = refundBusiness.selectCheckSpajInDb( paramsVO.getSpajNo() );
        HashMap<String, Object> params = new HashMap<String, Object>();
        if( redemptVO.getPrevLspdId() != null)
        {
        	editForm.setPrevLspdId( redemptVO.getPrevLspdId() );
        }
        editForm.setInvest( redemptVO.getInvest() );
        // default for report
        params.put( "format", "pdf" );
        params.put( "logoPath", "com/ekalife/elions/reports/refund/images/logo_ajs.gif" );
        params.put( "note", redemptVO.getNote() );
        params.put( "UWName", redemptVO.getNamaUnderwriter() );
        params.put( "dataSource", redemptVO.getDetailList() ); 
        params.put( "ttdDrIngrid", "com/ekalife/elions/reports/refund/images/inge.gif" );
        params.put( "bankName", redemptVO.getBankName() );
        params.put( "bankAccount", redemptVO.getBankAccount() );
//        if( checkSpajInDb != null && !"".equals( checkSpajInDb ) && checkSpajInDb.getNoSurat() != null && !"".equals( checkSpajInDb.getNoSurat() ) )// jika no_surat sudah ada di DB
//        {
//        	 params.put( "noSurat", checkSpajInDb.getNoSurat() );
//        }
//        else if( checkSpajInDb == null || "".equals( checkSpajInDb ))// jika no_surat blm ada di DB
//        {
//        	 String noSurat = refundBusiness.retrieveNoSurat( paramsVO.getSpajNo(), editForm.getTindakanCd() );
//        	 params.put( "noSurat", noSurat );
//        }
//        else
//        {
//        	 params.put( "noSurat", "" );
//        }
        return params;
    }

    public void generateDocumentRekapBatal( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper.generateDocumentRekapBatal" );
        RefundDocumentVO refundDocumentVO = new RefundDocumentVO();
        refundDocumentVO.setDownloadUrlSession( "refund/download_refund_document.htm" );
        refundDocumentVO.setJasperFile( "com/ekalife/elions/reports/refund/lamp_1_rekap_batal_refund.jasper" );
        refundDocumentVO.setParams( genParamsRedemptPdf( command ) );
        RefundForm refundForm = ( RefundForm ) command;
        refundForm.setRefundDocumentVO( refundDocumentVO );
    }
    
    public void generateDocumentRedemptExcel( HttpServletRequest request, Object command, Connection connection )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper.generateDocumentRedemptExcel" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        RefundPreviewInstruksiRedemptForm redemptForm = ( ( RefundForm ) command ).getRedemptForm();
        redemptForm.setDownloadFlag( "newPage" );
//        request.getSession().setAttribute( "command", command );
        String alasan = editForm.getAlasan();
        alasan = alasan.replace(" ", "_");
        String downloadUrl = "refund/redempt_excel.htm?spaj=" + editForm.getSpaj()+"&alasan="+ alasan+"&invest="+ editForm.getInvest();
        request.getSession().setAttribute( "downloadUrlSession", downloadUrl );
    }
    
    public void generateDocumentRedempt( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper.generateDocumentRedempt" );
        RefundDocumentVO refundDocumentVO = new RefundDocumentVO();
        refundDocumentVO.setDownloadUrlSession( "refund/download_refund_document.htm" );
        refundDocumentVO.setJasperFile( "com/ekalife/elions/reports/refund/lamp_4_redempt.jasper" );
        refundDocumentVO.setParams( genParamsRedemptPdf( command ) );
        RefundForm refundForm = ( RefundForm ) command;
        refundForm.setRefundDocumentVO( refundDocumentVO );
    }

    public boolean kirimRedemption( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundRedemptHelper.kirimRedemption" );

        Boolean result;

        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        HashMap<String, Object> resultMap = refundBusiness.updatePosisiAndCancelRefund( editForm.getSpaj(), RefundConst.POSISI_REFUND_ULINK_REDEMPT_SENT, "Kirim Redempt" );

        request.setAttribute( "pageMessage", resultMap.get( "pageMessage" ) );

        result = ( Boolean ) resultMap.get( "succeed" );
        if( result )
        {
            editForm.setPosisiNo( RefundConst.POSISI_REFUND_ULINK_REDEMPT_SENT );
        }
        return result;
        
    }

     public void initializeRedempt( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundRedemptHelper.initializeRedempt" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        RefundPreviewInstruksiRedemptForm redemptForm = ( ( RefundForm ) command ).getRedemptForm();
        
        if( RefundConst.TINDAKAN_REFUND_PREMI.equals( editForm.getTindakanCd() ) )
        {
        	redemptForm.setButtonViewAttachmentExcelDisplay( CommonConst.DISPLAY_TRUE );
            if( editForm.getPosisiNo() == null || editForm.getPosisiNo().equals( RefundConst.POSISI_DRAFT ) )
            {
            	redemptForm.setButtonViewAttachmentExcelIsDisabled( CommonConst.DISABLED_TRUE );
                redemptForm.setButtonViewAttachmentIsDisabled( CommonConst.DISABLED_TRUE );
                redemptForm.setButtonKirimIsDisabled( CommonConst.DISABLED_FALSE );
            }
            else
            {
            	redemptForm.setButtonViewAttachmentExcelIsDisabled( CommonConst.DISABLED_FALSE );
                redemptForm.setButtonViewAttachmentIsDisabled( CommonConst.DISABLED_FALSE );
                redemptForm.setButtonKirimIsDisabled( CommonConst.DISABLED_TRUE );
            }        	
        }
        else if( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() ) || RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() ) )
        {
        	redemptForm.setButtonViewAttachmentExcelDisplay( CommonConst.DISPLAY_FALSE );
        	redemptForm.setButtonViewAttachmentExcelIsDisabled( CommonConst.DISABLED_TRUE );
            if( editForm.getPosisiNo() == null || editForm.getPosisiNo().equals( RefundConst.POSISI_DRAFT ) )
            {
                redemptForm.setButtonViewAttachmentIsDisabled( CommonConst.DISABLED_TRUE );
                redemptForm.setButtonKirimIsDisabled( CommonConst.DISABLED_FALSE );
            }
            else
            {
                redemptForm.setButtonViewAttachmentIsDisabled( CommonConst.DISABLED_FALSE );
                redemptForm.setButtonKirimIsDisabled( CommonConst.DISABLED_TRUE );
            }    
        }
    }

}
