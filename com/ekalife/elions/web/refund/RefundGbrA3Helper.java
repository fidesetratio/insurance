package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundGbrA3Helper
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
import com.ekalife.elions.web.refund.vo.GbrA3ParamsVO;
import com.ekalife.elions.web.refund.vo.GbrA3VO;
import com.ekalife.elions.web.refund.vo.PenarikanUlinkVO;
import com.ekalife.elions.web.refund.vo.RefundDocumentVO;
import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.model.refund.RefundForm;
import com.ekalife.utils.FormatDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class RefundGbrA3Helper extends RefundHelperParent
{
    protected final Log logger = LogFactory.getLog( getClass() );

    public RefundGbrA3Helper()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA3Helper constructor is called ..." );
    }

    private RefundBusiness refundBusiness;

    public void setRefundBusiness( RefundBusiness refundBusiness )
    {
        this.refundBusiness = refundBusiness;
    }

    private GbrA3ParamsVO prepareGbrA3ParamsVO( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA3Helper.prepareGbrA3ParamsVO" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        GbrA3ParamsVO paramsVO = new GbrA3ParamsVO();
        paramsVO.setSpajNo( editForm.getSpaj().trim() );
        paramsVO.setSpajBaruNo( editForm.getSpajBaru().trim() );
        paramsVO.setAlasan( genAlasan( editForm ) );
        paramsVO.setAlasanForLabel(genAlasanForLabel(editForm));
        
        paramsVO.setPenarikanUlinkList( editForm.getPenarikanUlinkVOList() );
        paramsVO.setBiayaMedis( editForm.getBiayaMedis() == null? BigDecimal.ZERO : editForm.getBiayaMedis() );
        paramsVO.setBiayaLain( editForm.getBiayaLain() == null? BigDecimal.ZERO : editForm.getBiayaLain() );
        paramsVO.setBiayaLainDescr( editForm.getBiayaLainDescr() );
        paramsVO.setBiayaAdmin( editForm.getBiayaAdmin() == null? BigDecimal.ZERO : editForm.getBiayaAdmin() );
        
        return paramsVO;
    }

    public HashMap<String, Object> genParamsTindakanGbrA3Pdf( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA3Helper.genParamsTindakanGbrA3Pdf" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        GbrA3ParamsVO paramsVO = prepareGbrA3ParamsVO( command );

        // ini adalah bagian inti dari memproses report
        GbrA3VO gbrA3VO = refundBusiness.retrieveGbrA3( paramsVO, editForm );
        CheckSpajParamsVO checkSpajInDb = refundBusiness.selectCheckSpajInDb( gbrA3VO.getSpajNo() );
        HashMap<String, Object> params = new HashMap<String, Object>();
        String statementAvailableOrNot = null;
        String gantiTertanggungOrGantiPlan = null;
        String keterangan = null;
        String statementLamp1AvailableOrNot = null;
        String statementLamp2AvailableOrNot = null;
        String productName = null;
        ArrayList<PenarikanUlinkVO> penarikanUlink =  refundBusiness.selectPenarikanUlink( editForm.getSpaj() );       
        if( penarikanUlink != null && penarikanUlink.size() > 0 )
        {
        	productName = gbrA3VO.getProductName() + "(sudah Fund)";
        }
        else
        {
        	productName = gbrA3VO.getProductName() + "(belum Fund)";
        }
        if( gbrA3VO.getPrevLspdId() != null )
        {
        	editForm.setPrevLspdId( gbrA3VO.getPrevLspdId() );
        }
        if(gbrA3VO.getTempDescrDanJumlah()!=null && gbrA3VO.getTempDescrDanJumlah().size() > 0)
        {
        	editForm.setTempDescrDanJumlah(gbrA3VO.getTempDescrDanJumlah());
        }
        if(gbrA3VO.getStatementLamp1() != null )
        {
        	statementLamp1AvailableOrNot = "available";
        }
        if(gbrA3VO.getStatementLamp2() != null)
        {
        	statementLamp2AvailableOrNot = "available";
        }
        if( gbrA3VO.getStatementLamp1() == null && gbrA3VO.getStatementLamp2() == null && editForm.getLampiranAddList() == null ||
        		gbrA3VO.getStatementLamp1() == null && gbrA3VO.getStatementLamp2() == null && editForm.getLampiranAddList() != null && editForm.getLampiranAddList().size() == 0 )
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
        	keterangan = "Pembatalan dan Ganti Tertanggung";
        }
        else if(RefundConst.TINDAKAN_GANTI_PLAN.toString().equals( editForm.getTindakanCd().toString() ) )
        {
        	gantiTertanggungOrGantiPlan = "gantiPlan";
        	keterangan = "Pembatalan dan Ganti Plan";
        }
//        
        // default for report
        params.put( "format", "pdf" );
        params.put( "logoPath", "com/ekalife/elions/reports/refund/images/logo_ajs.gif" );

        params.put( "statementLamp1AvailableOrNot", statementLamp1AvailableOrNot );
        params.put( "statementLamp2AvailableOrNot", statementLamp2AvailableOrNot );
        params.put( "hal", gbrA3VO.getHal() );
        params.put( "insuredName", gbrA3VO.getInsuredName() );
        params.put( "newInsuredName", gbrA3VO.getNewInsuredName() );
        params.put( "newPolicyHolderName", gbrA3VO.getNewPolicyHolderName() );
        params.put( "newSpajNo", gbrA3VO.getNewSpajNo() );
        params.put( "newProductName", gbrA3VO.getNewProductName() );
        params.put( "noUrutMemo", gbrA3VO.getNoUrutMemo() );
        params.put( "policyHolderName", gbrA3VO.getPolicyHolderName() );
        params.put( "policyNo", gbrA3VO.getPolicyNo() );
        params.put( "productName", productName );
        params.put( "signer", gbrA3VO.getSigner() );
        params.put( "spajNo", gbrA3VO.getSpajNo() );
        params.put( "statement", gbrA3VO.getStatement() );
        params.put("policyHolderAndInsuredName", gbrA3VO.getPolicyHolderAndInsuredName() );
        if( gbrA3VO.getStatementLamp1() != null && "".equals( gbrA3VO.getStatementLamp1() ) )
        {
        	gbrA3VO.setStatementLamp1(null);
        }
        if( gbrA3VO.getStatementLamp2() != null && "".equals( gbrA3VO.getStatementLamp2() ) )
        {
        	gbrA3VO.setStatementLamp2(null);
        }
        params.put( "statementLamp1", gbrA3VO.getStatementLamp1() );
        params.put( "statementLamp2", gbrA3VO.getStatementLamp2() );
        ArrayList<HashMap<String, String>> tempAddLampiranList= gbrA3VO.getAddLampiranList();
        if( tempAddLampiranList != null && tempAddLampiranList.size() == 0 )
        {
        	tempAddLampiranList = null;
        }
        params.put( "dsAddLampiranRefund", tempAddLampiranList );
        params.put( "pembatal", gbrA3VO.getPembatal() );
        params.put( "tanggal", gbrA3VO.getTanggal() );
        params.put( "noVoucher", gbrA3VO.getNoVoucher() );
        params.put( "statementAvailableOrNot", statementAvailableOrNot );
        params.put( "efektifPolis", FormatDate.toIndonesian( gbrA3VO.getBegDate() )+ " - " + FormatDate.toIndonesian( gbrA3VO.getEndDate() ) );
//        params.put( "ttdDrIngrid", "com/ekalife/elions/reports/refund/images/ingrid.gif" );
        params.put( "ttdDrIngrid", gbrA3VO.getTtd() );
        if( checkSpajInDb != null && !"".equals( checkSpajInDb ) && checkSpajInDb.getNoSurat() != null && !"".equals( checkSpajInDb.getNoSurat() ) )// jika no_surat sudah ada di DB
        {
        	 params.put( "noSurat", checkSpajInDb.getNoSurat() );
        }
        else if( checkSpajInDb == null || "".equals( checkSpajInDb ))// jika no_surat blm ada di DB
        {
        	 String noSurat = refundBusiness.retrieveNoSurat( gbrA3VO.getSpajNo(), editForm.getTindakanCd() );
        	 params.put( "noSurat", noSurat );
        }
        else
        {
        	 params.put( "noSurat", "" );
        }
        //detail
        params.put( "dataSource", gbrA3VO.getRincianPolisList() );
        params.put( "gantiTertanggungOrGantiPlan", gantiTertanggungOrGantiPlan );
        params.put( "keterangan", keterangan );
        return params;
    }

    public void generateDocumentGbrA3( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA3Helper.generateDocumentGbrA3" );
        RefundDocumentVO refundDocumentVO = new RefundDocumentVO();
        refundDocumentVO.setDownloadUrlSession( "refund/download_refund_document.htm" );
        refundDocumentVO.setJasperFile( "com/ekalife/elions/reports/refund/gbr_a_3.jasper" );	
        refundDocumentVO.setParams( genParamsTindakanGbrA3Pdf( command ) );
        RefundForm refundForm = ( RefundForm ) command;
        refundForm.setRefundDocumentVO( refundDocumentVO );
    }

}
