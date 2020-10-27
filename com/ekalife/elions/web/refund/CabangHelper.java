package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: CabangHelper
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Jul 16, 2009 1:51:03 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import com.ekalife.elions.model.User;
import com.ekalife.elions.model.refund.CabangEditForm;
import com.ekalife.elions.model.refund.CabangForm;
import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.model.refund.RefundForm;
import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.elions.web.refund.vo.MstRefundParamsVO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.BatalParamsVO;
import com.ekalife.elions.web.refund.vo.PenarikanUlinkVO;
import com.ekalife.utils.MappingUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CabangHelper
{
    protected final Log logger = LogFactory.getLog( getClass() );

    public CabangHelper()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* CabangHelper constructor is called ..." );
    }

    private CabangBusiness cabangBusiness;

    public void setCabangBusiness( CabangBusiness cabangBusiness )
    {
        this.cabangBusiness = cabangBusiness;
    }

    public CabangForm initializeForm( HttpServletRequest request  )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* CabangHelper.initializeForm" );
        CabangForm cabangForm = new CabangForm();
        CabangEditForm editForm = new CabangEditForm();
        cabangForm.setEditForm( editForm );

        return cabangForm;
    }

    public Map<String, String> addAdditionalDataMap( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* CabangHelper.addAdditionalDataMap" );

        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        PolicyInfoVO policyInfoVO = cabangBusiness.genPolicyInfoVOBySpaj( editForm.getSpaj() );
        String currency;

        if( policyInfoVO == null )
        {
            currency = "Rp.";
        }
        else
        {
            currency = policyInfoVO.getLkuId().equals( "01" )? "Rp." : "US$";
        }

        Map<String, String> refMap = new HashMap<String, String>();
        refMap.put( "currency", currency );
        return refMap;
    }

    public void initializeEditForm( Object command )
    {
        CabangEditForm editForm = ( ( CabangForm ) command ).getEditForm();

        editForm.setAlasanList( cabangBusiness.getAlasanBatalCabangList() );

        if( editForm.getAlasanCd() != null && editForm.getAlasanCd().equals( RefundConst.ALASAN_LAIN ) )
        {
            editForm.setAlasanDisplay( CommonConst.DISPLAY_TRUE );
        }
        else
        {
            editForm.setAlasanDisplay( CommonConst.DISPLAY_FALSE );
        }

        // mengeset nama pp, tt dan produk tiap kali halaman refresh
        editForm.setPolicyInfoVO( cabangBusiness.genPolicyInfoVOBySpaj( editForm.getSpaj() ) );
    }


    public boolean onPressButtonBatalkanSpajTindakanTidakAda( HttpServletRequest request, Object command, Properties props )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* CabangHelper.onPressButtonBatalkanSpajTindakanTidakAda" );

        CabangEditForm editForm = ( ( CabangForm ) command ).getEditForm();
        MstRefundParamsVO paramsVO = new MstRefundParamsVO();
        paramsVO.setSpajNo( editForm.getSpaj() );
        paramsVO.setAlasanCd( editForm.getAlasanCd() );
        paramsVO.setAlasanLain( genAlasan( editForm ) );
        paramsVO.setAlasanLainForLabel( genAlasanForLabel( editForm ) );
        paramsVO.setTindakanCd( RefundConst.TINDAKAN_TIDAK_ADA );
        paramsVO.setSetoranPremiDbVOList( null );
        paramsVO.setBiayaUlinkDbVOList( null );
        paramsVO.setHasUnitFlag( null );
        paramsVO.setPrevLspdId( null );

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute( "currentUser" );
        Integer ulinkFlag = cabangBusiness.isUnitLink( paramsVO.getSpajNo() )? 1 : 0;
        paramsVO.setUlinkFlag( ulinkFlag );

        paramsVO.setCreateWhen( new Date() );
        paramsVO.setUpdateWhen( new Date() );
        paramsVO.setCreateWho( new BigDecimal( currentUser.getLus_id() ) );
        paramsVO.setUpdateWho( new BigDecimal( currentUser.getLus_id() ) );
        paramsVO.setCancelWhen( new Date() );
        paramsVO.setCancelWho( new BigDecimal( currentUser.getLus_id() ) );

        // karena posisi penting, selalu ditaruh sebagai parameter yg terakhir
        paramsVO.setPosisi( RefundConst.POSISI_TIDAK_ADA_CANCEL );

        boolean saveIsSucced = false;
        String actionMessage = "Pembatalan SPAJ";
        Map<String, Object> resultMap = cabangBusiness.deleteThenInsertRefund( paramsVO, actionMessage );

        String saveDraftMessage = ( String ) resultMap.get( "pageMessage" );
        Boolean saveDraftSucceed = ( Boolean ) resultMap.get( "succeed" );
        if( saveDraftSucceed )
        {
            saveIsSucced = batalkanSpaj( request, command );
        }
        else
        {
            request.setAttribute( "pageMessage", saveDraftMessage );
        }


        return saveIsSucced;
    }


    protected String genAlasan( CabangEditForm editForm )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* CabangHelper.genAlasan" );
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

    protected String genAlasanForLabel( CabangEditForm editForm )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* CabangHelper.genAlasanForLabel" );
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

    /**
     * Important note: Semua pembatalan dari user cabang akan melalui pintu ini (funtion ini)
     *
     * @param request: request
     * @param command: command
     * @return true:  jika pembatalan sukses
     *         false: jika pembatalan gagal
     */
    public boolean batalkanSpaj( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.batalkanSpaj" );

        CabangEditForm editForm = ( ( CabangForm ) command ).getEditForm();

        HttpSession session = request.getSession();
        User currentUser = ( User ) session.getAttribute( "currentUser" );

        BatalParamsVO paramsVO = new BatalParamsVO();
        paramsVO.setAlasan( genAlasan( editForm ) );
        paramsVO.setAlasanForLabel(genAlasanForLabel(editForm));
        paramsVO.setCurrentUser( currentUser );
        paramsVO.setSpajNo( editForm.getSpaj() );
      	paramsVO.setCancelWhen( editForm.getCancelWhen() == null? new Date() : editForm.getCancelWhen() );
      	paramsVO.setCancelWho( editForm.getCancelWho() == null? new BigDecimal( currentUser.getLus_id() ) : editForm.getCancelWho() );


        paramsVO.setPosisiNo( RefundConst.POSISI_TIDAK_ADA_CANCEL );

        Boolean succeed;
        HashMap<String, Object> resultMap = cabangBusiness.batalkanSpaj( paramsVO );
        request.setAttribute( "pageMessage", resultMap.get( "pageMessage" ) );

        succeed = ( Boolean ) resultMap.get( "succeed" );

        return succeed;

    }

}