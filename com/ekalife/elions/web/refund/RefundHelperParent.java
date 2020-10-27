package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundHelperParent
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Nov 20, 2008 11:44:22 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.elions.web.common.DownloadFormInterface;
import com.ekalife.elions.web.refund.vo.RefundDocumentVO;
import com.ekalife.utils.MappingUtil;

import javax.servlet.http.HttpServletRequest;

public class RefundHelperParent implements Serializable
{
    /**
	 *@author Deddy
	 *@since Mar 31, 2015
	 *@description TODO 
	 */
	private static final long serialVersionUID = 3879442060346591657L;
	protected final Log logger = LogFactory.getLog( getClass() );

    public RefundHelperParent()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelperParent constructor is called ..." );
    }

    protected void enableAllEditForm( RefundEditForm editForm, boolean isEnabled )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.enableAllEditForm" );

        String disabledStatus = isEnabled? CommonConst.DISABLED_FALSE : CommonConst.DISABLED_TRUE;

        editForm.setAddLampiranDisabled( disabledStatus );
        editForm.setAlasanListIsDisabled( disabledStatus );
        editForm.setAtasNamaIsDisabled( disabledStatus );
        editForm.setBiayaLainIsDisabled( disabledStatus );
        editForm.setBiayaMedisIsDisabled( disabledStatus );
        editForm.setCabangBankIsDisabled( disabledStatus );
        editForm.setKotaBankIsDisabled( disabledStatus );
        editForm.setNamaBankIsDisabled( disabledStatus );
        editForm.setNorekIsDisabled( disabledStatus );
        editForm.setSpajIsDisabled( disabledStatus );
        editForm.setSpajBaruIsDisabled( disabledStatus );
        editForm.setTindakanListIsDisabled( disabledStatus );
        editForm.setBiayaIsDisabled( disabledStatus );
        editForm.setAlasanIsDisabled( disabledStatus );
        editForm.setPaymentIsDisabled( disabledStatus );
        editForm.setSendPhysicalDocIsDisabled( disabledStatus );
        editForm.setHasUnitListIsDisabled( disabledStatus );
        editForm.setMerchantListIsDisabled( disabledStatus );
    }

    protected String genAlasan( RefundEditForm editForm )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.genAlasan" );
        String result;

        if( editForm.getAlasanCd().equals( RefundConst.ALASAN_LAIN ) )
        {
            result = editForm.getAlasan();
        }
        else
        {
            result = MappingUtil.getLabel( editForm.getAlasanList(), editForm.getAlasanCd() );
        }

    	if( result != null && !"".equals( result ) && result.length() >= 18 &&  "pembatalan polis :".equals( result.substring( 0, 18).toLowerCase() ) )
    	{
    		result = result.toUpperCase();
    	}
    	else
    	{
    		result = "PEMBATALAN POLIS : " + result.toUpperCase();
    	}
        return result;
    }

    protected String genAlasanForLabel( RefundEditForm editForm )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.genAlasan" );
        String result;

        if( editForm.getAlasanCd().equals( RefundConst.ALASAN_LAIN ) )
        {
            result = editForm.getAlasan();
            if( result != null && result.length() > 18)
            {
            	if( "pembatalan polis :".equals( result.substring( 0, 18).toLowerCase() ) )
            	{
            		result = result.substring( 19, result.length() );
            	}
            }
        }
        else
        {
            result = MappingUtil.getLabel( editForm.getAlasanList(), editForm.getAlasanCd() );
        }

        return result.toUpperCase();
    }
    
    protected void doDownload( HttpServletRequest request, DownloadFormInterface downloadFormInterface, RefundDocumentVO refundDocumentVO )
    {
        downloadFormInterface.setDownloadFlag( "newPage" );
        request.getSession().setAttribute( "documentTobeDownload", refundDocumentVO );
        request.getSession().setAttribute( "downloadUrlSession", refundDocumentVO.getDownloadUrlSession() );
    }
    
}
