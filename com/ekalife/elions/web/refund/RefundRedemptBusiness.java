package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundRedemptBusiness
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Nov 21, 2008 11:32:07 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import id.co.sinarmaslife.std.util.StringUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.comparator.InvertibleComparator;

import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.web.refund.vo.PenarikanUlinkVO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.RedemptParamsVO;
import com.ekalife.elions.web.refund.vo.RedemptVO;

public class RefundRedemptBusiness
{
    protected final Log logger = LogFactory.getLog( getClass() );

    private ElionsManager elionsManager;

    public void setElionsManager( ElionsManager elionsManager )
    {
        this.elionsManager = elionsManager;
    }

    public RefundRedemptBusiness()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundRedemptBusiness constructor is called ..." );
    }

    public RefundRedemptBusiness( ElionsManager elionsManager )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundRedemptBusiness constructor is called ..." );
        setElionsManager( elionsManager );
    }

    
    public RedemptVO retrieveRedempt( RedemptParamsVO paramsVO )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Business.retrieveRedempt" );
        RedemptVO result = new RedemptVO();
        
        String spajNo = paramsVO.getSpajNo();
        PolicyInfoVO policyInfoVO = elionsManager.selectPolicyInfoBySpaj( spajNo );
        String polisNo ;
        String bankName = "";
        String bankAccount = "";
        String invest = "";

        if( policyInfoVO.getPolicyNo() == null || policyInfoVO.getPolicyNo().trim().equals( "" ) )
        {
            polisNo = "(nomor belum ada)";
        }
        else
        {
            polisNo = StringUtil.nomorPolis( policyInfoVO.getPolicyNo() );
        }
        
       
        result.setAlasan( paramsVO.getAlasan() );
        result.setPrevLspdId( policyInfoVO.getLspdId() );
        String note =
                "Penarikan dilakukan karena " + paramsVO.getAlasanForLabel() + ". " +
                "Hasil penarikan polis tsb diatas, ditransfer ke rek. PT Asuransi Jiwa Sinarmas MSIG Tbk. : ";
        result.setNote( note );

        ArrayList<HashMap<String, Object>> detailList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;

        for( int i = 0; i < paramsVO.getPenarikanUlinkVOList().size(); i++ )
        {
            PenarikanUlinkVO vo = paramsVO.getPenarikanUlinkVOList().get( i );
            if( vo.getLjiInvest() != null && vo.getLjiInvest().toLowerCase().contains( "excellink" ))
            {
            	invest = "excellink";
            }
            else if( vo.getLjiInvest() != null && vo.getLjiInvest().toLowerCase().contains( "ekalink" ))
            {
            	invest = "ekalink";
            }
            map = new HashMap<String, Object>();
            map.put( "ljiInvest", vo.getLjiInvest() );
            map.put( "no", Integer.toString( i + 1 ) );
            map.put( "policyNo", polisNo );
            map.put( "description", "Penarikan" );
            map.put( "unit", vo.getJumlahUnit().toString() );
            detailList.add( map );
        }      
        
        if( RefundConst.KURS_DOLLAR.equals( policyInfoVO.getLkuId() ) )
        {
        	bankName = "Bank Niaga cab. Mangga Dua Mall";
        	bankAccount = "A/C. 022-02-12690-005";
        }
        else if( RefundConst.KURS_RUPIAH.equals( policyInfoVO.getLkuId() ) )
        {
        	if( policyInfoVO.getLsbsLineBus() != null && policyInfoVO.getLsbsLineBus() == 3) // ( policyInfoVO.getLsbsLineBus() == 3 ) ==> adalah syariah
        	{
        		bankName = "Bank Niaga Syariah cab. Sudirman";
        		bankAccount = "A/C. 520-01-00112-001";
        	}
        	else // selain syariah
        	{
        		if ( invest != null && "excellink".equals( invest ) )
        		{
            		bankName = "Bank Niaga Mangga Dua Mall";
            		bankAccount = "A/C.220111748002";
        		}
        		else if ( invest != null && "ekalink".equals( invest ) )
        		{
            		bankName = "BII cab. Wisma Eka Jiwa";
            		bankAccount = "A/C. 2001-501193";
        		}

        	}
        }
    	result.setBankName( bankName );
    	result.setBankAccount( bankAccount );
        result.setNamaUnderwriter( paramsVO.getNamaUnderwriter() );
        result.setDetailList( detailList );
        result.setInvest( invest );
        return result;
    }

    public String genRedemptFileName( String spajNo )
    {
        String result;

        DateFormat dateFormat = new SimpleDateFormat( "yyyyMMdd_HHmmss" );
		result =  "surat_redempt_" + spajNo + "_" + dateFormat.format( new Date() ) + ".pdf";
        return result;
    }
}
