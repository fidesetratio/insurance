package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundGbrB3Helper
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Nov 20, 2008 11:30:06 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.model.refund.RefundForm;
import com.ekalife.elions.web.refund.vo.CheckSpajParamsVO;
import com.ekalife.elions.web.refund.vo.GbrB3ParamsVO;
import com.ekalife.elions.web.refund.vo.GbrB3VO;
import com.ekalife.elions.web.refund.vo.RefundDocumentVO;
import com.ekalife.utils.FormatDate;

public class RefundGbrB3Helper extends RefundHelperParent
{
    protected final Log logger = LogFactory.getLog( getClass() );

    public RefundGbrB3Helper()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrB3Helper constructor is called ..." );
    }

    private RefundBusiness refundBusiness;

    public void setRefundBusiness( RefundBusiness refundBusiness )
    {
        this.refundBusiness = refundBusiness;
    }

    private GbrB3ParamsVO prepareGbrB3ParamsVO( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrB3Helper.prepareGbrB3ParamsVO" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        GbrB3ParamsVO paramsVO = new GbrB3ParamsVO();
        paramsVO.setSpajNo( editForm.getSpaj().trim() );
        paramsVO.setAlasan( genAlasan( editForm ) );
        paramsVO.setAlasanForLabel( genAlasanForLabel( editForm ) );
        return paramsVO;
    }

    public HashMap<String, Object> genParamsTindakanGbrB3Pdf( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrB3Helper.genParamsTindakanGbrB3Pdf" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        
        GbrB3ParamsVO paramsVO = prepareGbrB3ParamsVO( command );
        
        // ini adalah bagian inti dari memproses report
        GbrB3VO gbrB3VO = refundBusiness.retrieveGbrB3( paramsVO, editForm );
        CheckSpajParamsVO checkSpajInDb = refundBusiness.selectCheckSpajInDb( gbrB3VO.getSpajNo() );
        HashMap<String, Object> params = new HashMap<String, Object>();
        String statementAvailableOrNot = null;
        String keterangan = null;
        String statementLamp1AvailableOrNot = null;
        String statementLamp2AvailableOrNot = null;
        if( gbrB3VO.getPrevLspdId() != null )
        {
        	editForm.setPrevLspdId( gbrB3VO.getPrevLspdId() );
        }
        if(gbrB3VO.getTempDescrDanJumlah()!=null && gbrB3VO.getTempDescrDanJumlah().size() > 0)
        {
        	editForm.setTempDescrDanJumlah(gbrB3VO.getTempDescrDanJumlah());
        }
        if(gbrB3VO.getStatementLamp1() != null )
        {
        	statementLamp1AvailableOrNot = "available";
        }
        if(gbrB3VO.getStatementLamp2() != null)
        {
        	statementLamp2AvailableOrNot = "available";
        }
        if(gbrB3VO.getStatementLamp1() == null && gbrB3VO.getStatementLamp2() == null && editForm.getLampiranAddList() == null ||
        		gbrB3VO.getStatementLamp1() == null && gbrB3VO.getStatementLamp2() == null && editForm.getLampiranAddList() != null && editForm.getLampiranAddList().size() == 0)
        {
        	statementAvailableOrNot = null;
        }
        else
        {
        	statementAvailableOrNot = "available";
        }
        if(RefundConst.TINDAKAN_GANTI_TERTANGGUNG.toString().equals( editForm.getTindakanCd().toString() ) )
        {
        	keterangan = "Pembatalan dan Ganti Tertanggung";
        }
        else if(RefundConst.TINDAKAN_GANTI_PLAN.toString().equals( editForm.getTindakanCd().toString() ) )
        {
        	keterangan = "Pembatalan dan Ganti Plan";
        }
//        
        // default for report
        params.put( "format", "pdf" );
        params.put( "logoPath", "com/ekalife/elions/reports/refund/images/logo_ajs.gif" );
        params.put( "statementLamp1AvailableOrNot", statementLamp1AvailableOrNot );
        params.put( "statementLamp2AvailableOrNot", statementLamp2AvailableOrNot );
        params.put( "hal", gbrB3VO.getHal() );
        params.put( "insuredName", gbrB3VO.getInsuredName() );
        params.put( "newInsuredName", gbrB3VO.getNewInsuredName() );
        params.put( "newPolicyHolderName", gbrB3VO.getNewPolicyHolderName() );
        params.put( "newSpajNo", gbrB3VO.getNewSpajNo() );
        params.put( "newProductName", gbrB3VO.getNewProductName() );
        params.put( "noUrutMemo", gbrB3VO.getNoUrutMemo() );
        params.put( "policyHolderName", gbrB3VO.getPolicyHolderName() );
        params.put( "policyNo", gbrB3VO.getPolicyNo() );
        params.put( "productName", gbrB3VO.getProductName() );
        params.put( "signer", gbrB3VO.getSigner() );
        params.put( "spajNo", gbrB3VO.getSpajNo() );
        params.put( "statement", gbrB3VO.getStatement() );
        if( gbrB3VO.getStatementLamp1() != null && "".equals( gbrB3VO.getStatementLamp1() ) )
        {
        	gbrB3VO.setStatementLamp1(null);
        }
        if( gbrB3VO.getStatementLamp2() != null && "".equals( gbrB3VO.getStatementLamp2() ) )
        {
        	gbrB3VO.setStatementLamp2(null);
        }
        params.put( "statementLamp1", gbrB3VO.getStatementLamp1() );
        params.put( "statementLamp2", gbrB3VO.getStatementLamp2() );
        ArrayList<HashMap<String, String>> tempAddLampiranList= gbrB3VO.getAddLampiranList();
        if( tempAddLampiranList != null && tempAddLampiranList.size() == 0 )
        {
        	tempAddLampiranList = null;
        }
        params.put( "dsAddLampiranRefund", tempAddLampiranList );
        params.put( "tanggal", gbrB3VO.getTanggal() );
        params.put( "pembatal", gbrB3VO.getPembatal() );
        params.put( "noVoucher", gbrB3VO.getNoVoucher() );
        params.put( "keterangan", keterangan );
        params.put( "statementAvailableOrNot", statementAvailableOrNot );
        params.put( "efektifPolis", FormatDate.toIndonesian( gbrB3VO.getBegDate() )+ " - " + FormatDate.toIndonesian( gbrB3VO.getEndDate() ) );
//        params.put( "ttdDrIngrid", "com/ekalife/elions/reports/refund/images/ingrid.gif" );
        params.put( "ttdDrIngrid", gbrB3VO.getTtd() );
        if( checkSpajInDb != null && !"".equals( checkSpajInDb ) && checkSpajInDb.getNoSurat() != null && !"".equals( checkSpajInDb.getNoSurat() ) )// jika no_surat sudah ada di DB
        {
        	 params.put( "noSurat", checkSpajInDb.getNoSurat() );
        }
        else if( checkSpajInDb == null || "".equals( checkSpajInDb ))// jika no_surat blm ada di DB
        {
        	 String noSurat = refundBusiness.retrieveNoSurat( gbrB3VO.getSpajNo(), editForm.getTindakanCd() );
        	 params.put( "noSurat", noSurat );
        }
        else
        {
        	 params.put( "noSurat", "" );
        }
        //detail
        params.put( "dataSource", gbrB3VO.getRincianPolisList() );
        
        return params;
    }

    public void generateDocumentGbrB3( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrB3Helper.generateDocumentGbrB3" );
        RefundDocumentVO refundDocumentVO = new RefundDocumentVO();
        refundDocumentVO.setDownloadUrlSession( "refund/download_refund_document.htm" );
        refundDocumentVO.setJasperFile( "com/ekalife/elions/reports/refund/gbr_b_3.jasper" );
        refundDocumentVO.setParams( genParamsTindakanGbrB3Pdf( command ) );
        RefundForm refundForm = ( RefundForm ) command;
        refundForm.setRefundDocumentVO( refundDocumentVO );
    }

}
