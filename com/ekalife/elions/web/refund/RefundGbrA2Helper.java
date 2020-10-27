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

import id.co.sinarmaslife.std.util.StringUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.model.refund.RefundForm;
import com.ekalife.elions.web.refund.vo.CheckSpajParamsVO;
import com.ekalife.elions.web.refund.vo.GbrA2ParamsVO;
import com.ekalife.elions.web.refund.vo.GbrA2VO;
import com.ekalife.elions.web.refund.vo.RefundDocumentVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;
import com.ekalife.elions.web.refund.vo.SetoranVO;
import com.ekalife.utils.FormatDate;

public class RefundGbrA2Helper extends RefundHelperParent
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
    
    public RefundGbrA2Helper()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA2Helper constructor is called ..." );
    }

    private RefundBusiness refundBusiness;

    public void setRefundBusiness( RefundBusiness refundBusiness )
    {
        this.refundBusiness = refundBusiness;
    }

    private GbrA2ParamsVO prepareGbrA2ParamsVO( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA2Helper.prepareGbrA2ParamsVO" );

        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();

        GbrA2ParamsVO paramsVO = new GbrA2ParamsVO();
        paramsVO.setSpajNo( editForm.getSpaj().trim() );
        paramsVO.setAlasan( genAlasan( editForm ) );
        paramsVO.setAlasanForLabel(genAlasanForLabel(editForm));
        paramsVO.setPosisi( editForm.getPosisiNo() );

        paramsVO.setAtasNama( editForm.getAtasNama() );
        paramsVO.setNorek( editForm.getNorek() );
        paramsVO.setNamaBank( editForm.getNamaBank() );
        paramsVO.setCabangBank( editForm.getCabangBank() );
        paramsVO.setKotaBank( editForm.getKotaBank() );
        paramsVO.setPenarikanUlinkList( editForm.getPenarikanUlinkVOList() );
        paramsVO.setSetoranPokokAndTopUpList( editForm.getSetoranPokokAndTopUpChanged() );

        paramsVO.setBiayaMerchant( editForm.getBiayaMerchant() == null? BigDecimal.ZERO : editForm.getBiayaMerchant() );
        paramsVO.setBiayaAdmin( editForm.getBiayaAdmin() == null? BigDecimal.ZERO : editForm.getBiayaAdmin() );
        paramsVO.setBiayaMedis( editForm.getBiayaMedis() == null? BigDecimal.ZERO : editForm.getBiayaMedis() );
        paramsVO.setBiayaLain( editForm.getBiayaLain() == null? BigDecimal.ZERO : editForm.getBiayaLain() );
        paramsVO.setBiayaLainDescr( editForm.getBiayaLainDescr() );

        return paramsVO;
    }
    
    private HashMap<String, Object> genParamsTindakanRefundPremiPdf( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA2Helper.genParamsRedemptPdf" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        GbrA2ParamsVO paramsVO = prepareGbrA2ParamsVO( command );

        // ini adalah bagian inti dari memproses report
        GbrA2VO gbrA2VO = refundBusiness.retrieveGbrA2( paramsVO,  editForm );
        
        editForm.setSetoranPokokAndTopUpForSave( gbrA2VO.getSetoranPokokAndTopUp() );
        CheckSpajParamsVO checkSpajInDb = refundBusiness.selectCheckSpajInDb( gbrA2VO.getSpajNo() );
        HashMap<String, Object> params = new HashMap<String, Object>();

        // default for report
        params.put( "format", "pdf" );
        params.put( "logoPath", "com/ekalife/elions/reports/refund/images/logo_ajs.gif" );

        params.put( "noUrutMemo", gbrA2VO.getNoUrutMemo() );
        params.put( "hal", gbrA2VO.getHal() );
        params.put( "tanggal", gbrA2VO.getTanggal() );
        params.put( "spajNo", gbrA2VO.getSpajNo() );
        params.put( "polisNo", gbrA2VO.getPolisNo() );
        params.put( "produk", gbrA2VO.getProduk() );
        params.put( "pemegangPolis", gbrA2VO.getPemegangPolis() );
        params.put( "tertanggung", gbrA2VO.getTertanggung() );
        params.put( "statement", gbrA2VO.getStatement() );
        params.put( "noVoucher", gbrA2VO.getNoVoucher() );
        params.put( "efektifPolis", FormatDate.toIndonesian( gbrA2VO.getBegDate() )+ " - " + FormatDate.toIndonesian( gbrA2VO.getEndDate() ) );
//        params.put( "ttdDrIngrid", "com/ekalife/elions/reports/refund/images/ingrid.gif" );
        params.put( "ttdDrIngrid", gbrA2VO.getTtd() );
        if( checkSpajInDb != null && !"".equals( checkSpajInDb ) && checkSpajInDb.getNoSurat() != null && !"".equals( checkSpajInDb.getNoSurat() ) ) // jika no_surat sudah ada di DB
        {
        	 params.put( "noSurat", checkSpajInDb.getNoSurat() );
        }
        else if( checkSpajInDb == null || "".equals( checkSpajInDb ) ) // jika no_surat blm ada di DB
        {
        	 String noSurat = refundBusiness.retrieveNoSurat( gbrA2VO.getSpajNo(), editForm.getTindakanCd() );
        	 params.put( "noSurat", noSurat );
        }
        else
        {
        	 params.put( "noSurat", "" );
        }
        //detail
        ArrayList<HashMap<String, String>> detailSource = new ArrayList<HashMap<String,String>>();
        for(int i = 0 ; i <gbrA2VO.getRincianPolisList().size() ; i ++ )
        {
        	if("".equals(gbrA2VO.getRincianPolisList().get(i).get("descr")) && "".equals(gbrA2VO.getRincianPolisList().get(i).get("jumlah")))
        	{
        		
        	}
        	else
        	{
        		detailSource.add(gbrA2VO.getRincianPolisList().get(i));
        	}
        }
        params.put( "dataSource", detailSource );
        params.put( "jumlahTerbilang", gbrA2VO.getJumlahTerbilang() );
        if(gbrA2VO.getTempDescrDanJumlah()!=null)
        {
        	editForm.setTempDescrDanJumlah(gbrA2VO.getTempDescrDanJumlah());        	
        	params.put( "dataList", editForm.getTempDescrDanJumlah() );
        }       
        if(gbrA2VO.getPremiDikembalikan()!=null)
        {
        	editForm.setPremiDikembalikan(gbrA2VO.getPremiDikembalikan());
        	params.put( "premiDikembalikan", gbrA2VO.getPremiDikembalikan() );
        } 
        params.put( "atasNama", gbrA2VO.getAtasNama() );
        params.put( "rekeningNo", gbrA2VO.getRekeningNo() );
        params.put( "bankName", gbrA2VO.getBankName() );
        params.put( "cabang", gbrA2VO.getCabang() );
        params.put( "kota", gbrA2VO.getKota() );
        params.put( "signer", gbrA2VO.getSigner() );

        ArrayList<HashMap<String, String>> tempLampiranList = gbrA2VO.getLampiranList();
        ArrayList<HashMap<String, String>> tempAddLampiranList= gbrA2VO.getAddLampiranList();
        if( tempLampiranList != null && tempLampiranList.size() == 0 )
        {
        	tempLampiranList = null;
        }
        if( tempAddLampiranList != null && tempAddLampiranList.size() == 0 )
        {
        	tempAddLampiranList = null;
        }
        params.put( "dsAddLampiranRefund", tempAddLampiranList );
        params.put( "pembatal", gbrA2VO.getPembatal() );

        params.put( "totalPremiDikembalikan", gbrA2VO.getTotalPremiDikembalikan() );

        return params;
    }
    //TODO
    public void generateDocumentPreviewEditSetoran( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA2Helper.generateDocumentPreviewEditSetoran" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        ArrayList < SetoranVO > setoranPremi = refundBusiness.setSetoranPremi( editForm.getSpaj(), editForm.getPosisiNo() );
        editForm.setSetoranPokokAndTopUp( setoranPremi );
    }
    
    public void generateSetoranTransform( Object command ) // mengganti tipe data setoranVO menjadi SetoranPremiDbVO
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA2Helper.generateSetoranTransform" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        ArrayList < SetoranPremiDbVO > setSetoranPokokAndTopUp = refundBusiness.setSetoranPokokAndTopUp( editForm.getSetoranPokokAndTopUp() );
        editForm.setSetoranPokokAndTopUpChanged( setSetoranPokokAndTopUp );
    }
    
    public void generateDocumentGbrA2( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA2Helper.generateDocumentGbrA1" );
        RefundDocumentVO refundDocumentVO = new RefundDocumentVO();
        refundDocumentVO.setDownloadUrlSession( "refund/download_refund_document.htm" );
        refundDocumentVO.setJasperFile( "com/ekalife/elions/reports/refund/gbr_a_2.jasper" );
        refundDocumentVO.setParams( genParamsTindakanRefundPremiPdf( command ) );
        RefundForm refundForm = ( RefundForm ) command;
        refundForm.setRefundDocumentVO( refundDocumentVO );
    }
    
}
