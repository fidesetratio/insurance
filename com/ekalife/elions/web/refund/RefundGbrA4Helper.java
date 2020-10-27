package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundGbrA4Helper
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.web.refund.vo.CheckSpajParamsVO;
import com.ekalife.elions.web.refund.vo.GbrA4ParamsVO;
import com.ekalife.elions.web.refund.vo.GbrA4VO;
import com.ekalife.elions.web.refund.vo.RefundDocumentVO;
import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.model.refund.RefundForm;
import com.ekalife.utils.FormatDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class RefundGbrA4Helper extends RefundHelperParent
{
    protected final Log logger = LogFactory.getLog( getClass() );

    public RefundGbrA4Helper()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA4Helper constructor is called ..." );
    }

    private RefundBusiness refundBusiness;

    public void setRefundBusiness( RefundBusiness refundBusiness )
    {
        this.refundBusiness = refundBusiness;
    }

    private GbrA4ParamsVO prepareGbrA4ParamsVO( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA4Helper.prepareGbrA4ParamsVO" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        GbrA4ParamsVO paramsVO = new GbrA4ParamsVO();
        paramsVO.setSpajNo( editForm.getSpaj().trim() );
        paramsVO.setAlasan( genAlasan( editForm ) );
        paramsVO.setAlasanForLabel( genAlasanForLabel( editForm ) );
        return paramsVO;
    }

    public HashMap<String, Object> genParamsTindakanGbrA4Pdf( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA4Helper.genParamsTindakanGbrA4Pdf" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        GbrA4ParamsVO paramsVO = prepareGbrA4ParamsVO( command );
        
        // ini adalah bagian inti dari memproses report
        GbrA4VO gbrA4VO = refundBusiness.retrieveGbrA4( paramsVO, editForm );
        CheckSpajParamsVO checkSpajInDb = refundBusiness.selectCheckSpajInDb( gbrA4VO.getSpajNo() );
        HashMap<String, Object> params = new HashMap<String, Object>();
        String statementAvailableOrNot = null;
        String statementLamp1AvailableOrNot = null;
        String statementLamp2AvailableOrNot = null;
        if( gbrA4VO.getPrevLspdId() != null )
        {
        	editForm.setPrevLspdId( gbrA4VO.getPrevLspdId() );
        }
        if(gbrA4VO.getTempDescrDanJumlah()!=null && gbrA4VO.getTempDescrDanJumlah().size() > 0)
        {
        	editForm.setTempDescrDanJumlah(gbrA4VO.getTempDescrDanJumlah());
        }
        if(gbrA4VO.getStatementLamp1() != null )
        {
        	statementLamp1AvailableOrNot = "available";
        }
        if(gbrA4VO.getStatementLamp2() != null)
        {
        	statementLamp2AvailableOrNot = "available";
        }
        if(gbrA4VO.getStatementLamp1() == null && gbrA4VO.getStatementLamp2() == null && editForm.getLampiranAddList() == null || 
        	 gbrA4VO.getStatementLamp1() == null && gbrA4VO.getStatementLamp2() == null && editForm.getLampiranAddList() != null && editForm.getLampiranAddList().size() == 0 )
        {
        	statementAvailableOrNot = null;
        }
        else
        {
        	statementAvailableOrNot = "available";
        }
//        
        // default for report
        params.put( "format", "pdf" );
        params.put( "logoPath", "com/ekalife/elions/reports/refund/images/logo_ajs.gif" );
        params.put( "statementLamp1AvailableOrNot", statementLamp1AvailableOrNot );
        params.put( "statementLamp2AvailableOrNot", statementLamp2AvailableOrNot );
        params.put( "hal", gbrA4VO.getHal() );
        params.put( "insuredName", gbrA4VO.getInsuredName() );
        params.put( "newInsuredName", gbrA4VO.getNewInsuredName() );
        params.put( "newPolicyHolderName", gbrA4VO.getNewPolicyHolderName() );
        params.put( "newSpajNo", gbrA4VO.getNewSpajNo() );
        params.put( "newProductName", gbrA4VO.getNewProductName() );
        params.put( "noUrutMemo", gbrA4VO.getNoUrutMemo() );
        params.put( "policyHolderName", gbrA4VO.getPolicyHolderName() );
        params.put( "policyNo", gbrA4VO.getPolicyNo() );
        params.put( "productName", gbrA4VO.getProductName() );
        params.put( "signer", gbrA4VO.getSigner() );
        params.put( "spajNo", gbrA4VO.getSpajNo() );
        params.put( "statement", gbrA4VO.getStatement() );
        if( gbrA4VO.getStatementLamp1() != null && "".equals( gbrA4VO.getStatementLamp1() ) )
        {
        	gbrA4VO.setStatementLamp1(null);
        }
        if( gbrA4VO.getStatementLamp2() != null && "".equals( gbrA4VO.getStatementLamp2() ) )
        {
        	gbrA4VO.setStatementLamp2(null);
        }
        params.put( "statementLamp1", gbrA4VO.getStatementLamp1() );
        params.put( "statementLamp2", gbrA4VO.getStatementLamp2() );
        ArrayList<HashMap<String, String>> tempAddLampiranList= gbrA4VO.getAddLampiranList();
        if( tempAddLampiranList != null && tempAddLampiranList.size() == 0 )
        {
        	tempAddLampiranList = null;
        }
        params.put( "dsAddLampiranRefund", tempAddLampiranList );
        params.put( "pembatal", gbrA4VO.getPembatal() );
        params.put( "tanggal", gbrA4VO.getTanggal() );
        params.put( "noVoucher", gbrA4VO.getNoVoucher() );
        params.put( "statementAvailableOrNot", statementAvailableOrNot );
        params.put( "efektifPolis", FormatDate.toIndonesian( gbrA4VO.getBegDate() )+ " - " + FormatDate.toIndonesian( gbrA4VO.getEndDate() ) );
//        params.put( "ttdDrIngrid", "com/ekalife/elions/reports/refund/images/ingrid.gif" );
        params.put( "ttdDrIngrid", gbrA4VO.getTtd() );
        if( checkSpajInDb != null && !"".equals( checkSpajInDb ) && checkSpajInDb.getNoSurat() != null && !"".equals( checkSpajInDb.getNoSurat() ) )// jika no_surat sudah ada di DB
        {
        	 params.put( "noSurat", checkSpajInDb.getNoSurat() );
        }
        else if( checkSpajInDb == null || "".equals( checkSpajInDb ))// jika no_surat blm ada di DB
        {
        	 String noSurat = refundBusiness.retrieveNoSurat( gbrA4VO.getSpajNo(), editForm.getTindakanCd() );
        	 params.put( "noSurat", noSurat );
        }
        else
        {
        	 params.put( "noSurat", "" );
        }
        //detail
        params.put( "dataSource", gbrA4VO.getRincianPolisList() );
        
        return params;
    }

    public void generateDocumentGbrA4( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA4Helper.generateDocumentGbrA4" );
        RefundDocumentVO refundDocumentVO = new RefundDocumentVO();
        refundDocumentVO.setDownloadUrlSession( "refund/download_refund_document.htm" );
        refundDocumentVO.setJasperFile( "com/ekalife/elions/reports/refund/gbr_a_4.jasper" );
        refundDocumentVO.setParams( genParamsTindakanGbrA4Pdf( command ) );
        RefundForm refundForm = ( RefundForm ) command;
        refundForm.setRefundDocumentVO( refundDocumentVO );
    }

}
