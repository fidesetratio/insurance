package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundGbrB2Helper
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
import com.ekalife.elions.web.refund.vo.GbrB2ParamsVO;
import com.ekalife.elions.web.refund.vo.GbrB2VO;
import com.ekalife.elions.web.refund.vo.RefundDocumentVO;
import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.model.refund.RefundForm;
import com.ekalife.utils.FormatDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class RefundGbrB2Helper extends RefundHelperParent
{
    protected final Log logger = LogFactory.getLog( getClass() );

    public RefundGbrB2Helper()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrB2Helper constructor is called ..." );
    }

    private RefundBusiness refundBusiness;

    public void setRefundBusiness( RefundBusiness refundBusiness )
    {
        this.refundBusiness = refundBusiness;
    }

    private GbrB2ParamsVO prepareGbrB2ParamsVO( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrB2Helper.prepareGbrA3ParamsVO" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        GbrB2ParamsVO paramsVO = new GbrB2ParamsVO();
        paramsVO.setSpajNo( editForm.getSpaj().trim() );
        paramsVO.setSpajBaruNo( editForm.getSpajBaru().trim() );
        paramsVO.setAlasan( genAlasan( editForm ) );
        paramsVO.setAlasanForLabel(genAlasanForLabel(editForm));
 
        return paramsVO;
    }

    public HashMap<String, Object> genParamsTindakanGbrB2Pdf( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrB2Helper.genParamsTindakanGbrB2Pdf" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        GbrB2ParamsVO paramsVO = prepareGbrB2ParamsVO( command );
        
        // ini adalah bagian inti dari memproses report
        GbrB2VO gbrB2VO = refundBusiness.retrieveGbrB2( paramsVO, editForm );
        CheckSpajParamsVO checkSpajInDb = refundBusiness.selectCheckSpajInDb( gbrB2VO.getSpajNo() );
        HashMap<String, Object> params = new HashMap<String, Object>();
        String statementAvailableOrNot = null;
        String gantiTertanggungOrGantiPlan = null;
        String keterangan = null;
        String statementLamp1AvailableOrNot = null;
        String statementLamp2AvailableOrNot = null;
        if( gbrB2VO.getPrevLspdId() != null )
        {
        	editForm.setPrevLspdId( gbrB2VO.getPrevLspdId() );
        }
        if(gbrB2VO.getTempDescrDanJumlah()!=null && gbrB2VO.getTempDescrDanJumlah().size() > 0)
        {
        	editForm.setTempDescrDanJumlah( gbrB2VO.getTempDescrDanJumlah() );
        }
        if(gbrB2VO.getStatementLamp1() != null )
        {
        	statementLamp1AvailableOrNot = "available";
        }
        if(gbrB2VO.getStatementLamp2() != null)
        {
        	statementLamp2AvailableOrNot = "available";
        }
        if( gbrB2VO.getStatementLamp1() == null && gbrB2VO.getStatementLamp2() == null && editForm.getLampiranAddList() == null ||
        		gbrB2VO.getStatementLamp1() == null && gbrB2VO.getStatementLamp2() == null && editForm.getLampiranAddList() != null && editForm.getLampiranAddList().size() == 0)
        {
        	statementAvailableOrNot = null;
        }
        else
        {
        	statementAvailableOrNot = "available";
        }
        if(RefundConst.TINDAKAN_GANTI_TERTANGGUNG.toString().equals( editForm.getTindakanCd().toString() ) )
        {
        	gantiTertanggungOrGantiPlan = "gantiTertanggung";
        	keterangan = "Pembatalan Polis dan Ganti Tertanggung";
        }
        else if(RefundConst.TINDAKAN_GANTI_PLAN.toString().equals( editForm.getTindakanCd().toString() ) )
        {
        	gantiTertanggungOrGantiPlan = "gantiPlan";
        	keterangan = "Pembatalan Polis dan Ganti Plan";
        }
//        
        // default for report
        params.put( "format", "pdf" );
        params.put( "logoPath", "com/ekalife/elions/reports/refund/images/logo_ajs.gif" );

        params.put( "statementLamp1AvailableOrNot", statementLamp1AvailableOrNot );
        params.put( "statementLamp2AvailableOrNot", statementLamp2AvailableOrNot );
        params.put( "hal", gbrB2VO.getHal() );
        params.put( "insuredName", gbrB2VO.getInsuredName() );
        params.put( "newInsuredName", gbrB2VO.getNewInsuredName() );
        params.put( "newPolicyHolderName", gbrB2VO.getNewPolicyHolderName() );
        params.put( "newSpajNo", gbrB2VO.getNewSpajNo() );
        params.put( "newProductName", gbrB2VO.getNewProductName() );
        params.put( "noUrutMemo", gbrB2VO.getNoUrutMemo() );
        params.put( "policyHolderName", gbrB2VO.getPolicyHolderName() );
        params.put( "policyNo", gbrB2VO.getPolicyNo() );
        params.put( "productName", gbrB2VO.getProductName() );
        params.put( "signer", gbrB2VO.getSigner() );
        params.put( "spajNo", gbrB2VO.getSpajNo() );
        params.put( "statement", gbrB2VO.getStatement() );
        params.put("policyHolderAndInsuredName", gbrB2VO.getPolicyHolderAndInsuredName() );
        if( gbrB2VO.getStatementLamp1() != null && "".equals( gbrB2VO.getStatementLamp1() ) )
        {
        	gbrB2VO.setStatementLamp1(null);
        }
        if( gbrB2VO.getStatementLamp2() != null && "".equals( gbrB2VO.getStatementLamp2() ) )
        {
        	gbrB2VO.setStatementLamp2(null);
        }
        params.put( "statementLamp1", gbrB2VO.getStatementLamp1() );
        params.put( "statementLamp2", gbrB2VO.getStatementLamp2() );
        ArrayList<HashMap<String, String>> tempAddLampiranList= gbrB2VO.getAddLampiranList();
        if( tempAddLampiranList != null && tempAddLampiranList.size() == 0 )
        {
        	tempAddLampiranList = null;
        }
        params.put( "dsAddLampiranRefund", tempAddLampiranList );
        params.put( "pembatal", gbrB2VO.getPembatal() );
        params.put( "tanggal", gbrB2VO.getTanggal() );
        params.put( "noVoucher", gbrB2VO.getNoVoucher() );
        params.put( "statementAvailableOrNot", statementAvailableOrNot );
        params.put( "efektifPolis", FormatDate.toIndonesian( gbrB2VO.getBegDate() )+ " - " + FormatDate.toIndonesian( gbrB2VO.getEndDate() ) );
//        params.put( "ttdDrIngrid", "com/ekalife/elions/reports/refund/images/ingrid.gif" );
        params.put( "ttdDrIngrid", gbrB2VO.getTtd() );
        if( checkSpajInDb != null && !"".equals( checkSpajInDb ) && checkSpajInDb.getNoSurat() != null && !"".equals( checkSpajInDb.getNoSurat() ) )// jika no_surat sudah ada di DB
        {
        	 params.put( "noSurat", checkSpajInDb.getNoSurat() );
        }
        else if( checkSpajInDb == null || "".equals( checkSpajInDb ))// jika no_surat blm ada di DB
        {
        	 String noSurat = refundBusiness.retrieveNoSurat( gbrB2VO.getSpajNo(), editForm.getTindakanCd() );
        	 params.put( "noSurat", noSurat );
        }
        else
        {
        	 params.put( "noSurat", "" );
        }
        //detail
        params.put( "dataSource", gbrB2VO.getRincianPolisList() );
        params.put( "gantiTertanggungOrGantiPlan", gantiTertanggungOrGantiPlan );
        params.put( "keterangan", keterangan );
        
        return params;
    }

    public void generateDocumentGbrB2( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrB2Helper.generateDocumentGbrB2" );
        RefundDocumentVO refundDocumentVO = new RefundDocumentVO();
        refundDocumentVO.setDownloadUrlSession( "refund/download_refund_document.htm" );
        refundDocumentVO.setJasperFile( "com/ekalife/elions/reports/refund/gbr_b_2.jasper" );	
        refundDocumentVO.setParams( genParamsTindakanGbrB2Pdf( command ) );
        RefundForm refundForm = ( RefundForm ) command;
        refundForm.setRefundDocumentVO( refundDocumentVO );
    }

}
