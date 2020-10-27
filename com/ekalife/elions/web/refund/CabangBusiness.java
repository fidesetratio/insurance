package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: CabangBusiness
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Jul 16, 2009 1:51:14 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.web.refund.vo.*;
import com.ekalife.utils.FormatString;
import id.co.sinarmaslife.std.model.vo.DropDown;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CabangBusiness
{
    protected final Log logger = LogFactory.getLog(getClass());

    private ElionsManager elionsManager;
    private RefundCommonBusiness commonBusiness;

    private List<DropDown> alasanBatalCabangList;

    public CabangBusiness()
    {
    }

    public CabangBusiness(ElionsManager elionsManager)
    {
        this.elionsManager = elionsManager;
    }

    public ElionsManager getElionsManager() {
        return elionsManager;
    }

    public void setElionsManager(ElionsManager elionsManager) {
        this.elionsManager = elionsManager;
    }

    public RefundCommonBusiness getCommonBusiness() {
        return commonBusiness;
    }

    public void setCommonBusiness(RefundCommonBusiness commonBusiness) {
        this.commonBusiness = commonBusiness;
    }

    public PolicyInfoVO genPolicyInfoVOBySpaj( String spajNo )
    {
        PolicyInfoVO policyInfoVO = elionsManager.selectPolicyInfoBySpaj( spajNo );
        if( policyInfoVO != null )
        {
        	String kurs = "02".equals( policyInfoVO.getLkuId() )? "$" : "Rp.";
            policyInfoVO.setSpajNo( FormatString.nomorSPAJ( policyInfoVO.getSpajNo() ) );
            policyInfoVO.setPolicyNo( FormatString.nomorPolis( policyInfoVO.getPolicyNo() ) );
            policyInfoVO.setPremiDisplay( kurs + new DecimalFormat("#,##0.00").format( policyInfoVO.getPremi().abs() ) );
            policyInfoVO.setUangPertanggunganDisplay( kurs + new DecimalFormat("#,##0.00").format( policyInfoVO.getUangPertanggungan().abs())  );
        }
        return policyInfoVO;
    }

    public List<DropDown> getAlasanBatalCabangList()
    {
        if( this.alasanBatalCabangList == null )
        {
            alasanBatalCabangList = new ArrayList<DropDown>();
            alasanBatalCabangList.add( new DropDown( "", "" ) );
            alasanBatalCabangList.add( new DropDown( RefundConst.ALASAN_SALAH_INPUT.toString(), "Salah Input" ) );
            alasanBatalCabangList.add( new DropDown( RefundConst.ALASAN_TIDAK_BAYAR_PREMI.toString(), "Tidak Bayar Premi" ) );
            alasanBatalCabangList.add( new DropDown( RefundConst.ALASAN_DOKUMEN_TIDAK_DILENGKAPI.toString(), "Dokumen Tidak Dilengkapi" ) );
            alasanBatalCabangList.add( new DropDown( RefundConst.ALASAN_LAIN.toString(), "Lain-lain" ) );
        }
        return alasanBatalCabangList;
    }

    public boolean isUnitLink( String spajNo )
    {
        return commonBusiness.isUnitLink( spajNo );
    }

    public String getTimeStr( Date time )
    {
        DateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
		return dateFormat.format( time );
    }

    public Map<String, Object> deleteThenInsertRefund( MstRefundParamsVO paramsVO, String actionMessage )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundBusiness.deleteThenInsertRefund" );

        Map<String, Object> result = new HashMap<String, Object>();
        String pageMessage;
        RefundDbVO refundDbVO = new RefundDbVO();
        refundDbVO.setAlasanCd( paramsVO.getAlasanCd() );
        refundDbVO.setAlasanLain( paramsVO.getAlasanLain() );
        refundDbVO.setCreateWhen( paramsVO.getCreateWhen() );
        refundDbVO.setCreateWho( paramsVO.getCreateWho() );
        refundDbVO.setKliCabangBank( paramsVO.getKliCabangBank() );
        refundDbVO.setKliKotaBank( paramsVO.getKliKotaBank() );
        refundDbVO.setKliNama( paramsVO.getKliNama() );
        refundDbVO.setKliNamaBank( paramsVO.getKliNamaBank() );
        refundDbVO.setKliNorek( paramsVO.getKliNorek() );
        refundDbVO.setNoVoucher( "novoc" );
        refundDbVO.setPosisiNo( paramsVO.getPosisi() );
        refundDbVO.setPremiRefund( paramsVO.getTotalPremiDikembalikan() );
        refundDbVO.setSpajBaruNo( paramsVO.getSpajBaruNo() );
        refundDbVO.setSpajNo( paramsVO.getSpajNo() );
        refundDbVO.setTindakanCd( paramsVO.getTindakanCd() );
        refundDbVO.setUlinkFlag( paramsVO.getUlinkFlag() );
        refundDbVO.setUpdateWhen( paramsVO.getUpdateWhen() );
        refundDbVO.setUpdateWho( paramsVO.getUpdateWho() );
        refundDbVO.setPayment( paramsVO.getPayment() );
        refundDbVO.setPaymentDate( paramsVO.getPaymentDate() );
        refundDbVO.setHasUnitFlag( paramsVO.getHasUnitFlag() );
        refundDbVO.setPrevLspdId( paramsVO.getPrevLspdId() );
        refundDbVO.setPrevLspdId( paramsVO.getPrevLspdId() );
        refundDbVO.setCancelWhen( paramsVO.getCancelWhen() );
        refundDbVO.setCancelWho( paramsVO.getCancelWho() );
        refundDbVO.setFlagUserCabang( 1 );

        List<SetoranPremiDbVO> setoranPremiBySpaj = elionsManager.selectSetoranPremiBySpaj( paramsVO.getSpajNo() );
        if( setoranPremiBySpaj != null && setoranPremiBySpaj.size() > 0 )
        {
            String noSurat = commonBusiness.retrieveNoSurat( paramsVO.getSpajNo(), paramsVO.getTindakanCd() );
            refundDbVO.setNoSurat( noSurat );
        }
        else if( setoranPremiBySpaj == null || setoranPremiBySpaj.size() == 0 )
        {
            refundDbVO.setNoSurat( null );
        }
        refundDbVO.setDetailList( new ArrayList<RefundDetDbVO>() );

        Boolean succeed;
        try
        {
            elionsManager.deleteThenInsertRefund( refundDbVO );
            pageMessage = actionMessage + " sukses pada ";
            succeed = true;
        }
        catch( Exception e )
        {
            logger.error("ERROR :", e);
            pageMessage = actionMessage + " gagal pada ";
            succeed = false;
        }
        pageMessage = pageMessage.concat( getTimeStr( paramsVO.getUpdateWhen() ) );
        result.put( "pageMessage", pageMessage );
        result.put( "succeed", succeed );

        return result;
    }

    public String getNowStr()
    {
        Date now = new Date();
        DateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
		return dateFormat.format( now );
    }

    public HashMap<String, Object> batalkanSpaj( BatalParamsVO paramsVO )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* CabangBusiness.batalkanSpaj" );

        HashMap<String, Object> result = new HashMap<String, Object>();
        String pageMessage;

        Boolean succeed;
        try
        {
            elionsManager.batalkanSpaj( paramsVO.getSpajNo(), paramsVO.getAlasan(), paramsVO.getCurrentUser(), paramsVO.getPosisiNo(), paramsVO.getCancelWho(), paramsVO.getCancelWhen() );
            pageMessage = "Pembatalan SPAJ sukses pada ".concat( getNowStr() );
            succeed = true;
        }
        catch( Exception e )
        {
            logger.error("ERROR :", e);
            pageMessage = "Pembatalan SPAJ gagal pada ".concat( getNowStr() );
            succeed = false;
        }
        result.put( "pageMessage", pageMessage );
        result.put( "succeed", succeed );

        return result;
    }
}
