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
import com.ekalife.elions.web.refund.vo.GbrA1ParamsVO;
import com.ekalife.elions.web.refund.vo.GbrA1VO;
import com.ekalife.elions.web.refund.vo.RefundDocumentVO;
import com.ekalife.utils.FormatDate;

public class RefundGbrA1Helper extends RefundHelperParent
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
    
    public RefundGbrA1Helper()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA1Helper constructor is called ..." );
    }

    private RefundBusiness refundBusiness;

    public void setRefundBusiness( RefundBusiness refundBusiness )
    {
        this.refundBusiness = refundBusiness;
    }

    private GbrA1ParamsVO prepareGbrA1ParamsVO( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA1Helper.prepareGbrA1ParamsVO" );

        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();

        GbrA1ParamsVO paramsVO = new GbrA1ParamsVO();
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
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA1Helper.genParamsRedemptPdf" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        GbrA1ParamsVO paramsVO = prepareGbrA1ParamsVO( command );

        // ini adalah bagian inti dari memproses report
        GbrA1VO gbrA1VO = refundBusiness.retrieveGbrA1( paramsVO,  editForm );
        CheckSpajParamsVO checkSpajInDb = refundBusiness.selectCheckSpajInDb( gbrA1VO.getSpajNo() );
        HashMap<String, Object> params = new HashMap<String, Object>();
        // default for report
        params.put( "format", "pdf" );
        params.put( "logoPath", "com/ekalife/elions/reports/refund/images/logo_ajs.gif" );
        params.put( "noUrutMemo", gbrA1VO.getNoUrutMemo() );
        params.put( "hal", gbrA1VO.getHal() );
        params.put( "tanggal", gbrA1VO.getTanggal() );
        params.put( "spajNo", gbrA1VO.getSpajNo() );
        params.put( "polisNo", gbrA1VO.getPolisNo() );
        params.put( "produk", gbrA1VO.getProduk() );
        params.put( "pemegangPolis", gbrA1VO.getPemegangPolis() );
        params.put( "tertanggung", gbrA1VO.getTertanggung() );
        params.put( "statement", gbrA1VO.getStatement() );
        params.put( "noVoucher", gbrA1VO.getNoVoucher() );
        params.put( "efektifPolis", FormatDate.toIndonesian( gbrA1VO.getBegDate() )+ " - " + FormatDate.toIndonesian( gbrA1VO.getEndDate() ) );
//        params.put( "ttdDrIngrid", "com/ekalife/elions/reports/refund/images/ingrid.gif" );
        params.put( "ttdDrIngrid", gbrA1VO.getTtd() );
        if( checkSpajInDb != null && !"".equals( checkSpajInDb ) && checkSpajInDb.getNoSurat() != null && !"".equals( checkSpajInDb.getNoSurat() ) ) // jika no_surat sudah ada di DB
        {
        	 params.put( "noSurat", checkSpajInDb.getNoSurat() );
        }
        else if( checkSpajInDb == null || "".equals( checkSpajInDb ) ) // jika no_surat blm ada di DB
        {
        	 String noSurat = refundBusiness.retrieveNoSurat( gbrA1VO.getSpajNo(), editForm.getTindakanCd() );
        	 params.put( "noSurat", noSurat );
        }
        else
        {
        	 params.put( "noSurat", "" );
        }
        //detail
        ArrayList<HashMap<String, String>> detailSource = new ArrayList<HashMap<String,String>>();
        for(int i = 0 ; i <gbrA1VO.getRincianPolisList().size() ; i ++ )
        {
        	if("".equals(gbrA1VO.getRincianPolisList().get(i).get("descr")) && "".equals(gbrA1VO.getRincianPolisList().get(i).get("jumlah")))
        	{
        		
        	}
        	else
        	{
        		detailSource.add(gbrA1VO.getRincianPolisList().get(i));
        	}
        }
        params.put( "dataSource", detailSource );
        params.put( "jumlahTerbilang", gbrA1VO.getJumlahTerbilang() );
        if(gbrA1VO.getTempDescrDanJumlah()!=null)
        {
        	editForm.setTempDescrDanJumlah(gbrA1VO.getTempDescrDanJumlah());        	
        	params.put( "dataList", editForm.getTempDescrDanJumlah() );
        }       
        if(gbrA1VO.getPremiDikembalikan()!=null)
        {
        	editForm.setPremiDikembalikan(gbrA1VO.getPremiDikembalikan());
        	params.put( "premiDikembalikan", gbrA1VO.getPremiDikembalikan() );
        } 
        params.put( "atasNama", gbrA1VO.getAtasNama() );
        params.put( "rekeningNo", gbrA1VO.getRekeningNo() );
        params.put( "bankName", gbrA1VO.getBankName() );
        params.put( "cabang", gbrA1VO.getCabang() );
        params.put( "kota", gbrA1VO.getKota() );
        params.put( "signer", gbrA1VO.getSigner() );

        ArrayList<HashMap<String, String>> tempLampiranList = gbrA1VO.getLampiranList();
        ArrayList<HashMap<String, String>> tempAddLampiranList= gbrA1VO.getAddLampiranList();
        if( tempLampiranList != null && tempLampiranList.size() == 0 )
        {
        	tempLampiranList = null;
        }
        if( tempAddLampiranList != null && tempAddLampiranList.size() == 0 )
        {
        	tempAddLampiranList = null;
        }
        params.put( "dsAddLampiranRefund", tempAddLampiranList );
        params.put( "pembatal", gbrA1VO.getPembatal() );

        params.put( "totalPremiDikembalikan", gbrA1VO.getTotalPremiDikembalikan() );

        return params;
    }

    private HashMap<String, Object> genParamsRefundGbrA1EditDetailPdf( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* genParamsRefundGbrA1DetailPdf.genParamsRedemptPdf" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        GbrA1ParamsVO paramsVO = prepareGbrA1ParamsVO( command );
        // ini adalah bagian inti dari memproses report
        GbrA1VO gbrA1VO = refundBusiness.retrieveGbrA1EditDetail( paramsVO, editForm );

        HashMap<String, Object> params = new HashMap<String, Object>();

        // default for report
        params.put( "format", "pdf" );
        params.put( "logoPath", "com/ekalife/elions/reports/refund/images/logo_ajs.gif" );

        params.put( "noUrutMemo", gbrA1VO.getNoUrutMemo() );
        params.put( "hal", gbrA1VO.getHal() );
        params.put( "tanggal", gbrA1VO.getTanggal() );
        params.put( "spajNo", gbrA1VO.getSpajNo() );
        params.put( "polisNo", gbrA1VO.getPolisNo() );
        params.put( "produk", gbrA1VO.getProduk() );
        params.put( "pemegangPolis", gbrA1VO.getPemegangPolis() );
        params.put( "tertanggung", gbrA1VO.getTertanggung() );
        params.put( "statement", gbrA1VO.getStatement() );
        params.put( "noVoucher", gbrA1VO.getNoVoucher() );
//        params.put( "ttdDrIngrid", "com/ekalife/elions/reports/refund/images/ingrid.gif" );
        params.put( "ttdDrIngrid", gbrA1VO.getTtd() );
        //detail
        params.put( "dataSource", gbrA1VO.getRincianPolisList() );
        params.put( "jumlahTerbilang", gbrA1VO.getJumlahTerbilang() );
        if( gbrA1VO.getTempDescrDanJumlah()!=null )
        {
        	editForm.setTempDescrDanJumlah(gbrA1VO.getTempDescrDanJumlah());
        	params.put( "dataList", editForm.getTempDescrDanJumlah() );
        }       
        if( gbrA1VO.getPremiDikembalikan()!=null )
        {
        	editForm.setPremiDikembalikan(gbrA1VO.getPremiDikembalikan());
        	params.put( "premiDikembalikan", gbrA1VO.getPremiDikembalikan() );
        } 
        if( gbrA1VO.getPrevLspdId() != null )
        {
        	editForm.setPrevLspdId( gbrA1VO.getPrevLspdId() );
        }
        params.put( "atasNama", gbrA1VO.getAtasNama() );
        params.put( "rekeningNo", gbrA1VO.getRekeningNo() );
        params.put( "bankName", gbrA1VO.getBankName() );
        params.put( "cabang", gbrA1VO.getCabang() );
        params.put( "kota", gbrA1VO.getKota() );
        params.put( "signer", gbrA1VO.getSigner() );

        params.put( "dsLampiranRefund", gbrA1VO.getLampiranList() );
        params.put( "totalPremiDikembalikan", gbrA1VO.getTotalPremiDikembalikan() );

        return params;
    }
    
    
    public void generateDocumentGbrA1( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA1Helper.generateDocumentGbrA1" );
        RefundDocumentVO refundDocumentVO = new RefundDocumentVO();
        refundDocumentVO.setDownloadUrlSession( "refund/download_refund_document.htm" );
        refundDocumentVO.setJasperFile( "com/ekalife/elions/reports/refund/gbr_a_1.jasper" );
        refundDocumentVO.setParams( genParamsTindakanRefundPremiPdf( command ) );
        RefundForm refundForm = ( RefundForm ) command;
        refundForm.setRefundDocumentVO( refundDocumentVO );
    }

    public void generateDocumentGbrA1EditDetail( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA1Helper.generateDocumentGbrA1EditDetail" );
        RefundDocumentVO refundDocumentVO = new RefundDocumentVO();
        refundDocumentVO.setDownloadUrlSession( "refund/download_refund_document.htm" );
        refundDocumentVO.setJasperFile( "com/ekalife/elions/reports/refund/gbr_a_1.jasper" );
        refundDocumentVO.setParams( genParamsRefundGbrA1EditDetailPdf( command ) );
        RefundForm refundForm = ( RefundForm ) command;
        refundForm.setRefundDocumentVO( refundDocumentVO );
    }
    
    public void generateAllDescrAndJumlahForPreviewEdit( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA1Helper.generateAllDescrAndJumlahForPreviewEdit" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        if(editForm.getTempDescrDanJumlah()!=null)
        {
        	for(int i = 0 ; i < editForm.getTempDescrDanJumlah().size() ; i++)
        	{
        		if("yes".equals(editForm.getTempDescrDanJumlah().get(i).getBiayaStandarOrNot()))
        		{
        			editForm.getTempDescrDanJumlah().remove(i);
        			i = i -1;
        		}     
        	}
        }
    }
}
