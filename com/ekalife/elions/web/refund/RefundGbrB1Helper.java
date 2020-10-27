package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundGbrA1Helper
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Nov 20, 2008 11:29:58 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.model.refund.RefundForm;
import com.ekalife.elions.web.refund.vo.CheckSpajParamsVO;
import com.ekalife.elions.web.refund.vo.GbrB1ParamsVO;
import com.ekalife.elions.web.refund.vo.GbrB1VO;
import com.ekalife.elions.web.refund.vo.RefundDocumentVO;
import com.ekalife.utils.FormatDate;

public class RefundGbrB1Helper extends RefundHelperParent
{
    protected final Log logger = LogFactory.getLog( getClass() );
    
    public static final int REFUND_LOOKUP_JSP = 0;
    public static final int REFUND_EDIT_JSP = 1;
    public static final int STD_DOWNLOAD_JSP = 2;
    public static final int PREVIEW_LAMP_1_JSP = 3;
    public static final int PREVIEW_LAMP_3_JSP = 4;
    public static final int PREVIEW_REDEMPT_JSP = 5;
    public static final int SIGN_IN_JSP = 6;
    public static final int PREVIEW_EDIT_JSP = 7;
    public static final int REFUND_REKAP_LOOKUP_JSP = 8;
    public static final int REFUND_LOOKUP_AKSEPTASI_JSP = 10;
    
    public RefundGbrB1Helper()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrB1Helper constructor is called ..." );
    }

    private RefundBusiness refundBusiness;

    public void setRefundBusiness( RefundBusiness refundBusiness )
    {
        this.refundBusiness = refundBusiness;
    }

    private GbrB1ParamsVO prepareGbrB1ParamsVO( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrB1Helper.prepareGbrB1ParamsVO" );

        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();

        GbrB1ParamsVO paramsVO = new GbrB1ParamsVO();
        paramsVO.setSpajNo( editForm.getSpaj().trim() );
        paramsVO.setAlasan( genAlasan( editForm ) );
        paramsVO.setAlasanForLabel(genAlasanForLabel(editForm));

        paramsVO.setAtasNama( editForm.getAtasNama() );
        paramsVO.setNorek( editForm.getNorek() );
        paramsVO.setNamaBank( editForm.getNamaBank() );
        paramsVO.setCabangBank( editForm.getCabangBank() );
        paramsVO.setKotaBank( editForm.getKotaBank() );
        paramsVO.setPenarikanUlinkList( editForm.getPenarikanUlinkVOList() );

        paramsVO.setBiayaMerchant( editForm.getBiayaMerchant() == null? BigDecimal.ZERO : editForm.getBiayaMerchant() );
        paramsVO.setBiayaAdmin( editForm.getBiayaAdmin() == null? BigDecimal.ZERO : editForm.getBiayaAdmin() );
        paramsVO.setBiayaMedis( editForm.getBiayaMedis() == null? BigDecimal.ZERO : editForm.getBiayaMedis() );
        paramsVO.setBiayaLain( editForm.getBiayaLain() == null? BigDecimal.ZERO : editForm.getBiayaLain() );
        paramsVO.setBiayaLainDescr( editForm.getBiayaLainDescr() );

        return paramsVO;
    }

    private HashMap<String, Object> genParamsTindakanRefundPremiPdf( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrB1Helper.genParamsRedemptPdf" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        GbrB1ParamsVO paramsVO = prepareGbrB1ParamsVO( command );

        // ini adalah bagian inti dari memproses report
        GbrB1VO gbrB1VO = refundBusiness.retrieveGbrB1( paramsVO,  editForm );
        CheckSpajParamsVO checkSpajInDb = refundBusiness.selectCheckSpajInDb( gbrB1VO.getSpajNo() );
        HashMap<String, Object> params = new HashMap<String, Object>();

        // default for report
        params.put( "format", "pdf" );
        params.put( "logoPath", "com/ekalife/elions/reports/refund/images/logo_ajs.gif" );

        params.put( "noUrutMemo", gbrB1VO.getNoUrutMemo() );
        params.put( "hal", gbrB1VO.getHal() );
        params.put( "tanggal", gbrB1VO.getTanggal() );
        params.put( "spajNo", gbrB1VO.getSpajNo() );
        params.put( "polisNo", gbrB1VO.getPolisNo() );
        params.put( "produk", gbrB1VO.getProduk() );
        params.put( "pemegangPolis", gbrB1VO.getPemegangPolis() );
        params.put( "tertanggung", gbrB1VO.getTertanggung() );
        params.put( "statement", gbrB1VO.getStatement() );
        params.put( "noVoucher", gbrB1VO.getNoVoucher() );
        params.put( "efektifPolis", FormatDate.toIndonesian( gbrB1VO.getBegDate() )+ " - " + FormatDate.toIndonesian( gbrB1VO.getEndDate() ) );
//        params.put( "ttdDrIngrid", "com/ekalife/elions/reports/refund/images/ingrid.gif" );
        params.put( "ttdDrIngrid", gbrB1VO.getTtd() );
        if( checkSpajInDb != null && !"".equals( checkSpajInDb ) && checkSpajInDb.getNoSurat() != null && !"".equals( checkSpajInDb.getNoSurat() ) ) // jika no_surat sudah ada di DB
        {
        	 params.put( "noSurat", checkSpajInDb.getNoSurat() );
        }
        else if( checkSpajInDb == null || "".equals( checkSpajInDb ) ) // jika no_surat blm ada di DB
        {
        	 String noSurat = refundBusiness.retrieveNoSurat( gbrB1VO.getSpajNo(), editForm.getTindakanCd() );
        	 params.put( "noSurat", noSurat );
        }
        else
        {
        	 params.put( "noSurat", "" );
        }
        //detail
        ArrayList<HashMap<String, String>> detailSource = new ArrayList<HashMap<String,String>>();
        for(int i = 0 ; i <gbrB1VO.getRincianPolisList().size() ; i ++ )
        {
        	if("".equals(gbrB1VO.getRincianPolisList().get(i).get("descr")) && "".equals(gbrB1VO.getRincianPolisList().get(i).get("jumlah")))
        	{
        		
        	}
        	else
        	{
        		detailSource.add(gbrB1VO.getRincianPolisList().get(i));
        	}
        }
        params.put( "dataSource", detailSource );
        params.put( "jumlahTerbilang", gbrB1VO.getJumlahTerbilang() );
        if(gbrB1VO.getTempDescrDanJumlah()!=null)
        {
        	editForm.setTempDescrDanJumlah(gbrB1VO.getTempDescrDanJumlah());        	
        	params.put( "dataList", editForm.getTempDescrDanJumlah() );
        }       
        if(gbrB1VO.getPremiDikembalikan()!=null)
        {
        	editForm.setPremiDikembalikan(gbrB1VO.getPremiDikembalikan());
        	params.put( "premiDikembalikan", gbrB1VO.getPremiDikembalikan() );
        } 
        params.put( "atasNama", gbrB1VO.getAtasNama() );
        params.put( "rekeningNo", gbrB1VO.getRekeningNo() );
        params.put( "bankName", gbrB1VO.getBankName() );
        params.put( "cabang", gbrB1VO.getCabang() );
        params.put( "kota", gbrB1VO.getKota() );
        params.put( "signer", gbrB1VO.getSigner() );

        ArrayList<HashMap<String, String>> tempLampiranList = gbrB1VO.getLampiranList();
        ArrayList<HashMap<String, String>> tempAddLampiranList= gbrB1VO.getAddLampiranList();
        if( tempLampiranList != null && tempLampiranList.size() == 0 )
        {
        	tempLampiranList = null;
        }
        if( tempAddLampiranList != null && tempAddLampiranList.size() == 0 )
        {
        	tempAddLampiranList = null;
        }
        params.put( "dsAddLampiranRefund", tempAddLampiranList );
        params.put( "pembatal", gbrB1VO.getPembatal() );
        params.put( "totalPremiDikembalikan", gbrB1VO.getTotalPremiDikembalikan() );

        return params;
    }
    
    public void generateDocumentGbrB1( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrB1Helper.generateDocumentGbrB1" );
        RefundDocumentVO refundDocumentVO = new RefundDocumentVO();
        refundDocumentVO.setDownloadUrlSession( "refund/download_refund_document.htm" );
        refundDocumentVO.setJasperFile( "com/ekalife/elions/reports/refund/gbr_b_1.jasper" );
        refundDocumentVO.setParams( genParamsTindakanRefundPremiPdf( command ) );
        RefundForm refundForm = ( RefundForm ) command;
        refundForm.setRefundDocumentVO( refundDocumentVO );
    }
    
}
