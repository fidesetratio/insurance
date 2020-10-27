package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundLamp1Helper
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

import com.ekalife.elions.model.User;
import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.model.refund.RefundForm;
import com.ekalife.elions.model.refund.RefundLookupForm;
import com.ekalife.elions.model.refund.RefundPreviewLamp1Form;
import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.elions.web.refund.vo.BiayaUlinkDbVO;
import com.ekalife.elions.web.refund.vo.CheckSpajParamsVO;
import com.ekalife.elions.web.refund.vo.Lamp1ParamsVO;
import com.ekalife.elions.web.refund.vo.Lamp1VO;
import com.ekalife.elions.web.refund.vo.LampiranListVO;
import com.ekalife.elions.web.refund.vo.MstRefundParamsVO;
import com.ekalife.elions.web.refund.vo.PenarikanUlinkVO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.PreviewEditParamsVO;
import com.ekalife.elions.web.refund.vo.RefundDbVO;
import com.ekalife.elions.web.refund.vo.RefundDetDbVO;
import com.ekalife.elions.web.refund.vo.RefundDocumentVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.LazyConverter;
import com.ekalife.utils.MappingUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import id.co.sinarmaslife.std.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class RefundLamp1Helper extends RefundHelperParent
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
    
    public RefundLamp1Helper()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper constructor is called ..." );
    }

    private RefundBusiness refundBusiness;

    public void setRefundBusiness( RefundBusiness refundBusiness )
    {
        this.refundBusiness = refundBusiness;
    }

    

    private Lamp1ParamsVO prepareLamp1ParamsVO( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper.prepareLamp1ParamsVO" );

        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();

        Lamp1ParamsVO paramsVO = new Lamp1ParamsVO();
        paramsVO.setSpajNo( editForm.getSpaj().trim() );
        paramsVO.setAlasan( genAlasan( editForm ) );
        paramsVO.setAlasanForLabel(genAlasanForLabel(editForm));

        paramsVO.setAtasNama( editForm.getAtasNama() );
        paramsVO.setNorek( editForm.getNorek() );
        paramsVO.setNamaBank( editForm.getNamaBank() );
        paramsVO.setCabangBank( editForm.getCabangBank() );
        paramsVO.setKotaBank( editForm.getKotaBank() );
        paramsVO.setPenarikanUlinkList( editForm.getPenarikanUlinkVOList() );

        paramsVO.setBiayaAdmin( editForm.getBiayaAdmin() == null? BigDecimal.ZERO : editForm.getBiayaAdmin() );
        paramsVO.setBiayaMedis( editForm.getBiayaMedis() == null? BigDecimal.ZERO : editForm.getBiayaMedis() );
        paramsVO.setBiayaLain( editForm.getBiayaLain() == null? BigDecimal.ZERO : editForm.getBiayaLain() );
        paramsVO.setBiayaLainDescr( editForm.getBiayaLainDescr() );

        return paramsVO;
    }

    private HashMap<String, Object> genParamsTindakanRefundPremiPdf( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper.genParamsRedemptPdf" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        Lamp1ParamsVO paramsVO = prepareLamp1ParamsVO( command );

        // ini adalah bagian inti dari memproses report
        Lamp1VO lamp1VO = refundBusiness.retrieveLamp1( paramsVO,  editForm );
        CheckSpajParamsVO checkSpajInDb = refundBusiness.selectCheckSpajInDb( lamp1VO.getSpajNo() );
        HashMap<String, Object> params = new HashMap<String, Object>();

        // default for report
        params.put( "format", "pdf" );
        params.put( "logoPath", "com/ekalife/elions/reports/refund/images/logo_ajs.gif" );

        params.put( "noUrutMemo", lamp1VO.getNoUrutMemo() );
        params.put( "hal", lamp1VO.getHal() );
        params.put( "tanggal", lamp1VO.getTanggal() );
        params.put( "spajNo", lamp1VO.getSpajNo() );
        params.put( "polisNo", lamp1VO.getPolisNo() );
        params.put( "produk", lamp1VO.getProduk() );
        params.put( "pemegangPolis", lamp1VO.getPemegangPolis() );
        params.put( "tertanggung", lamp1VO.getTertanggung() );
        params.put( "statement", lamp1VO.getStatement() );
        params.put( "efektifPolis", FormatDate.toIndonesian( lamp1VO.getBegDate() )+ " - " + FormatDate.toIndonesian( lamp1VO.getEndDate() ) );
        params.put( "ttdDrIngrid", "com/ekalife/elions/reports/refund/images/inge.gif" );
        if( checkSpajInDb != null && !"".equals( checkSpajInDb ) && checkSpajInDb.getNoSurat() != null && !"".equals( checkSpajInDb.getNoSurat() ) ) // jika no_surat sudah ada di DB
        {
        	 params.put( "noSurat", checkSpajInDb.getNoSurat() );
        }
        else if( checkSpajInDb == null || "".equals( checkSpajInDb ) ) // jika no_surat blm ada di DB
        {
        	 String noSurat = refundBusiness.retrieveNoSurat( lamp1VO.getSpajNo(), editForm.getTindakanCd() );
        	 params.put( "noSurat", noSurat );
        }
        else
        {
        	 params.put( "noSurat", "" );
        }
        //detail
        ArrayList<HashMap<String, String>> detailSource = new ArrayList<HashMap<String,String>>();
        for(int i = 0 ; i <lamp1VO.getRincianPolisList().size() ; i ++ )
        {
        	if("".equals(lamp1VO.getRincianPolisList().get(i).get("descr")) && "".equals(lamp1VO.getRincianPolisList().get(i).get("jumlah")))
        	{
        		
        	}
        	else
        	{
        		detailSource.add(lamp1VO.getRincianPolisList().get(i));
        	}
        }
        params.put( "dataSource", detailSource );
        params.put( "jumlahTerbilang", lamp1VO.getJumlahTerbilang() );
        if(lamp1VO.getTempDescrDanJumlah()!=null)
        {
        	editForm.setTempDescrDanJumlah(lamp1VO.getTempDescrDanJumlah());        	
        	params.put( "dataList", editForm.getTempDescrDanJumlah() );
        }       
        if(lamp1VO.getPremiDikembalikan()!=null)
        {
        	editForm.setPremiDikembalikan(lamp1VO.getPremiDikembalikan());
        	params.put( "premiDikembalikan", lamp1VO.getPremiDikembalikan() );
        } 
        params.put( "atasNama", lamp1VO.getAtasNama() );
        params.put( "rekeningNo", lamp1VO.getRekeningNo() );
        params.put( "bankName", lamp1VO.getBankName() );
        params.put( "cabang", lamp1VO.getCabang() );
        params.put( "kota", lamp1VO.getKota() );
        params.put( "signer", lamp1VO.getSigner() );

        params.put( "totalPremiDikembalikan", lamp1VO.getTotalPremiDikembalikan() );

        return params;
    }
    
    

    private HashMap<String, Object> genParamsPreviewRefundPremiPdf( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper.genParamsRedemptPdf" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        Lamp1ParamsVO paramsVO = prepareLamp1ParamsVO( command );
        // ini adalah bagian inti dari memproses report
        Lamp1VO lamp1VO = refundBusiness.retrievePreviewEdit( paramsVO, editForm );

        HashMap<String, Object> params = new HashMap<String, Object>();

        // default for report
        params.put( "format", "pdf" );
        params.put( "logoPath", "com/ekalife/elions/reports/refund/images/logo_ajs.gif" );

        params.put( "noUrutMemo", lamp1VO.getNoUrutMemo() );
        params.put( "hal", lamp1VO.getHal() );
        params.put( "tanggal", lamp1VO.getTanggal() );
        params.put( "spajNo", lamp1VO.getSpajNo() );
        params.put( "polisNo", lamp1VO.getPolisNo() );
        params.put( "produk", lamp1VO.getProduk() );
        params.put( "pemegangPolis", lamp1VO.getPemegangPolis() );
        params.put( "tertanggung", lamp1VO.getTertanggung() );
        params.put( "statement", lamp1VO.getStatement() );
        params.put( "ttdDrIngrid", "com/ekalife/elions/reports/refund/images/inge.gif" );
        //detail
        params.put( "dataSource", lamp1VO.getRincianPolisList() );
        params.put( "jumlahTerbilang", lamp1VO.getJumlahTerbilang() );
        if( lamp1VO.getTempDescrDanJumlah()!=null )
        {
        	editForm.setTempDescrDanJumlah(lamp1VO.getTempDescrDanJumlah());
        	params.put( "dataList", editForm.getTempDescrDanJumlah() );
        }       
        if( lamp1VO.getPremiDikembalikan()!=null )
        {
        	editForm.setPremiDikembalikan(lamp1VO.getPremiDikembalikan());
        	params.put( "premiDikembalikan", lamp1VO.getPremiDikembalikan() );
        } 
        if( lamp1VO.getPrevLspdId() != null )
        {
        	editForm.setPrevLspdId( lamp1VO.getPrevLspdId() );
        }
        params.put( "atasNama", lamp1VO.getAtasNama() );
        params.put( "rekeningNo", lamp1VO.getRekeningNo() );
        params.put( "bankName", lamp1VO.getBankName() );
        params.put( "cabang", lamp1VO.getCabang() );
        params.put( "kota", lamp1VO.getKota() );
        params.put( "signer", lamp1VO.getSigner() );

        params.put( "dsLampiranRefund", lamp1VO.getLampiranList() );
        params.put( "totalPremiDikembalikan", lamp1VO.getTotalPremiDikembalikan() );

        return params;
    }
    
    
    public void generateDocumentLamp1( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper.generateDocumentRedempt" );
        RefundDocumentVO refundDocumentVO = new RefundDocumentVO();
        refundDocumentVO.setDownloadUrlSession( "refund/download_refund_document.htm" );
        refundDocumentVO.setJasperFile( "com/ekalife/elions/reports/refund/lamp_1_dan_2_surat_refund.jasper" );
        refundDocumentVO.setParams( genParamsTindakanRefundPremiPdf( command ) );
        RefundForm refundForm = ( RefundForm ) command;
        refundForm.setRefundDocumentVO( refundDocumentVO );
    }

    public void generateDocumentPreviewEdit( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper.generateDocumentRedempt" );
        RefundDocumentVO refundDocumentVO = new RefundDocumentVO();
        refundDocumentVO.setDownloadUrlSession( "refund/download_refund_document.htm" );
        refundDocumentVO.setJasperFile( "com/ekalife/elions/reports/refund/lamp_1_dan_2_surat_refund.jasper" );
        refundDocumentVO.setParams( genParamsPreviewRefundPremiPdf( command ) );
        RefundForm refundForm = ( RefundForm ) command;
        refundForm.setRefundDocumentVO( refundDocumentVO );
    }
    
    public void updateTglKirimDokFisik( Object command )
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper.updateTglKirimDokFisik" );
    	RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
//    	refundBusiness.updateTglKirimDokFisik( editForm.getSpaj(), editForm.getSendPhysicalDocDate(), "Tgl Kirim Dokumen Fisik");
    }
    
    public void generateCountPremi( Object command, String theEvent )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper.generateDocumentRedempt" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
        refundBusiness.retrieveCountPremi( editForm, lookupForm, theEvent );
    }
    
    public void generateAllDescrAndJumlahForPreviewEdit( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper.generateAllDescrAndJumlahForPreviewEdit" );
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
    
    public void initializeLamp1Form( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper.initializeLamp1Form" );

        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        RefundPreviewLamp1Form lamp1Form = ( ( RefundForm ) command ).getLamp1Form();
        User user = (User) request.getSession().getAttribute("currentUser");   
        
        String display = refundBusiness.isUnitLink( editForm.getSpaj() )? CommonConst.DISPLAY_TRUE : CommonConst.DISPLAY_FALSE;
        
        if( refundBusiness.isUnitLink( editForm.getSpaj() ) && editForm.getPenarikanUlinkVOList().size() > 0 )
        {
        	display = CommonConst.DISPLAY_TRUE;
        }
        else
        {
        	display = CommonConst.DISPLAY_FALSE;      
        }
        
        lamp1Form.setButtonPreviewRedemptDisplay( display );

        String disabled = refundBusiness.selectRefundByCd( editForm.getSpaj() ) == null?
                                CommonConst.DISABLED_TRUE : CommonConst.DISABLED_FALSE;
        
        //lamp1Form.setButtonBatalkanSpajIsDisabled( disabled );
        lamp1Form.setButtonAccBatalSpajIsDisabled( disabled );
        
        //MANTA - Khusus Spesial User Untuk Akseptasi Proses Refund SPAJ Timmy(795), Dewi(1614), Anta(3123)
        if(RefundConst.USER_AKSEP_UW.indexOf(user.getLus_id().trim())>-1 && RefundConst.TINDAKAN_REFUND_PREMI.equals(editForm.getTindakanCd()) &&
      	 ( (refundBusiness.isUnitLink(editForm.getSpaj()) && RefundConst.POSISI_REFUND_ULINK_ACCEPTATION.equals(editForm.getPosisiNo())) ||
      	 (!refundBusiness.isUnitLink(editForm.getSpaj()) && RefundConst.POSISI_REFUND_NON_ULINK_ACCEPTATION.equals(editForm.getPosisiNo())) ))
        {
        	lamp1Form.setButtonBatalkanSpajIsDisabled( CommonConst.DISABLED_FALSE );
        }else{
        	lamp1Form.setButtonBatalkanSpajIsDisabled( CommonConst.DISABLED_TRUE );
        }

        if( editForm.getPosisiNo() == null || editForm.getPosisiNo().equals( RefundConst.POSISI_DRAFT ) )
        {
            lamp1Form.setButtonSaveDraftIsDisabled( CommonConst.DISABLED_FALSE );
            lamp1Form.setButtonViewAttachmentIsDisabled( CommonConst.DISABLED_TRUE );
        }
        else
        {
            lamp1Form.setButtonSaveDraftIsDisabled( CommonConst.DISABLED_TRUE );
            lamp1Form.setButtonViewAttachmentIsDisabled( CommonConst.DISABLED_FALSE );
        }

        if( refundBusiness.isUnitLink( editForm.getSpaj() ) )
        {
        	// todo
//        	Boolean tempPosisi = false;
//        	Boolean tempPosisiAndUnit = false;
			if( editForm.getPenarikanUlinkVOList().size()>0 ){
				if( RefundConst.POSISI_REFUND_ULINK_REDEMPT_SENT.equals( editForm.getPosisiNo() ) ){
                	lamp1Form.setButtonAccBatalSpajIsDisabled( CommonConst.DISABLED_TRUE );
	  				for(int i=0; i<editForm.getPenarikanUlinkVOList().size(); i++){
	                    PenarikanUlinkVO vo = editForm.getPenarikanUlinkVOList().get(i);
	                    if(vo.getJumlah()!=null){
	                    	lamp1Form.setButtonAccBatalSpajIsDisabled( CommonConst.DISABLED_FALSE );
	                    }
	                }
				}else{
                	lamp1Form.setButtonAccBatalSpajIsDisabled( CommonConst.DISABLED_TRUE );
				}
			}else{
				if( editForm.getPosisiNo()==null || RefundConst.POSISI_DRAFT.equals(editForm.getPosisiNo()) ){
	  				lamp1Form.setButtonAccBatalSpajIsDisabled( CommonConst.DISABLED_FALSE );
	  			}else{
	  				lamp1Form.setButtonAccBatalSpajIsDisabled( CommonConst.DISABLED_TRUE );
	  			}
			}
/*        	
        	if(RefundConst.TINDAKAN_GANTI_PLAN.equals(editForm.getTindakanCd()) || RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals(editForm.getTindakanCd()))
        	{
        		if( editForm.getPosisiNo()!=null && editForm.getPosisiNo() >= RefundConst.POSISI_REFUND_ULINK_REDEMPT_SENT )
        		{
        			tempPosisi = false;
        		}
        		else
        		{
        			tempPosisi = true;
        		}
        		tempPosisiAndUnit = false;
        	}
        	else
        	{
        		if( RefundConst.POSISI_REFUND_ULINK_REDEMPT_SENT.equals( editForm.getPosisiNo() ) )
        		{
        			tempPosisi = true;
        		}
        		else
        		{
        			tempPosisi = false;
        		}
	      		if( editForm.getPosisiNo() != null && editForm.getPenarikanUlinkVOList().size() == 0 &&
	      			((editForm.getPosisiNo() < RefundConst.POSISI_REFUND_ULINK_CANCEL) && !editForm.getPosisiNo().equals(RefundConst.POSISI_REFUND_ULINK_ACCEPTATION)))
        		{
        			tempPosisiAndUnit = true;
        		}
        		else
        		{
        			tempPosisiAndUnit = false;
        		}
        	}
            if( tempPosisi || editForm.getPenarikanUlinkVOList().size() == 0 && editForm.getPosisiNo() == null || tempPosisiAndUnit )
            {
                //lamp1Form.setButtonBatalkanSpajIsDisabled( CommonConst.DISABLED_FALSE );
            	lamp1Form.setButtonAccBatalSpajIsDisabled( CommonConst.DISABLED_TRUE );
  				for(int i=0; i<editForm.getPenarikanUlinkVOList().size(); i++){
                    PenarikanUlinkVO vo = editForm.getPenarikanUlinkVOList().get(i);
                    if(vo.getJumlah() != null){
                    	lamp1Form.setButtonAccBatalSpajIsDisabled( CommonConst.DISABLED_FALSE );
                    }
                }
            }
            else
            {
                //lamp1Form.setButtonBatalkanSpajIsDisabled( CommonConst.DISABLED_TRUE );
            	lamp1Form.setButtonAccBatalSpajIsDisabled( CommonConst.DISABLED_TRUE );
            }
*/
            if( RefundConst.POSISI_REFUND_ULINK_CANCEL.equals( editForm.getPosisiNo() ) )
            {
                lamp1Form.setButtonUpdatePaymentIsDisabled( CommonConst.DISABLED_FALSE );
            }
            else
            {
                lamp1Form.setButtonUpdatePaymentIsDisabled( CommonConst.DISABLED_TRUE );
            }
        }
        else
        {
            if( RefundConst.POSISI_DRAFT.equals( editForm.getPosisiNo() ) || editForm.getPosisiNo() == null )
            {
                //lamp1Form.setButtonBatalkanSpajIsDisabled( CommonConst.DISABLED_FALSE );
            	lamp1Form.setButtonAccBatalSpajIsDisabled( CommonConst.DISABLED_FALSE );
            }
            else
            {
                //lamp1Form.setButtonBatalkanSpajIsDisabled( CommonConst.DISABLED_TRUE );
            	lamp1Form.setButtonAccBatalSpajIsDisabled( CommonConst.DISABLED_TRUE );
            }

            if( RefundConst.POSISI_REFUND_NON_ULINK_CANCEL.equals( editForm.getPosisiNo() ) )
            {
                lamp1Form.setButtonUpdatePaymentIsDisabled( CommonConst.DISABLED_FALSE );
            }
            else
            {
                lamp1Form.setButtonUpdatePaymentIsDisabled( CommonConst.DISABLED_TRUE );
            }
        }
    }
    
    
    public boolean saveDraftTindakanRefund( HttpServletRequest request, Object command, String actionMessage, Integer posisiCd )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper.saveDraftTindakanRefund" );

        String theEvent = ( ( RefundForm ) command ).getTheEvent();
        
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        RefundForm refundForm = ( RefundForm ) command;

        MstRefundParamsVO paramsVO = new MstRefundParamsVO();
        ArrayList<RefundDetDbVO> detailList = new ArrayList<RefundDetDbVO>();
        RefundDetDbVO refundDetDbVO;
        
        paramsVO.setSpajNo( editForm.getSpaj() );
        paramsVO.setAlasanCd( editForm.getAlasanCd() );
        paramsVO.setAlasanLain( genAlasan( editForm ) );
        paramsVO.setAlasanLainForLabel( genAlasanForLabel( editForm ) );
        paramsVO.setKliCabangBank( editForm.getCabangBank() );
        paramsVO.setKliKotaBank( editForm.getKotaBank() );
        paramsVO.setKliNama( editForm.getAtasNama() );
        paramsVO.setKliNamaBank( editForm.getNamaBank() );
        paramsVO.setKliNorek( editForm.getNorek() );
        paramsVO.setSpajBaruNo( editForm.getSpajBaru() );
        paramsVO.setTindakanCd( editForm.getTindakanCd() );
        BigDecimal totalPremiDikembalikan = ( BigDecimal ) refundForm.getRefundDocumentVO().getParams().get( "totalPremiDikembalikan" );
        if( totalPremiDikembalikan != null )
        {
        	paramsVO.setTotalPremiDikembalikan( totalPremiDikembalikan );	
        }
        else
        {
        	paramsVO.setTotalPremiDikembalikan( editForm.getPremiDikembalikan() );	
        }
        paramsVO.setPrevLspdId( editForm.getPrevLspdId() );
        paramsVO.setAddLampiranList( editForm.getLampiranAddList() ); 

        paramsVO.setBiayaAdmin( editForm.getBiayaAdmin() );
        paramsVO.setBiayaMedis( editForm.getBiayaMedis() );
        paramsVO.setBiayaLain( editForm.getBiayaLain() );
        paramsVO.setBiayaLainDescr( editForm.getBiayaLainDescr() );
        
        
        paramsVO.setPenarikanUlinkVOList( editForm.getPenarikanUlinkVOList() );
        
        // TODO: isi list2 yang 
        ArrayList<SetoranPremiDbVO> setoranPremiDbVOList = new ArrayList<SetoranPremiDbVO>();
        ArrayList<BiayaUlinkDbVO> biayaUlinkDbVOList = new ArrayList<BiayaUlinkDbVO>();        
        
        if( refundBusiness.isUnitLink(editForm.getSpaj()) && editForm.getPenarikanUlinkVOList() != null && "sudah".equals(editForm.getStatusUnit()))
        {
        	if(editForm.getTempDescrDanJumlah()!=null && editForm.getTempDescrDanJumlah().size() > 0)
            {
            	for( int i = 0 ; i < editForm.getTempDescrDanJumlah().size() ; i++ )
            	{
            		if(editForm.getTempDescrDanJumlah().get(i).getBiayaStandarOrNot() == null)
            		{
    	    			String ljbIdStr = editForm.getTempDescrDanJumlah().get(i).getLjbId();
    	    			Integer ljbId = null;
    	    			if( ljbIdStr == null || "".equals(ljbIdStr))
    	    			{
    	    				ljbId = null;
    	    			}
    	    			else 
    	    			{
    	    				ljbId = LazyConverter.toInt(ljbIdStr);
    	    			}
    	        		if("yes".equals(editForm.getTempDescrDanJumlah().get(i).getSetoranOrNot()))
    	        		{
    	        			setoranPremiDbVOList.add( new SetoranPremiDbVO( editForm.getTempDescrDanJumlah().get(i).getTitipanKe(),editForm.getTempDescrDanJumlah().get(i).getTglSetor(),editForm.getTempDescrDanJumlah().get(i).getJumlahPremi(),editForm.getTempDescrDanJumlah().get(i).getLkuId(), null, editForm.getTempDescrDanJumlah().get(i).getDescr(), editForm.getTempDescrDanJumlah().get(i).getNoPre(), editForm.getTempDescrDanJumlah().get(i).getNoVoucher() ) );
    	        		}
    	        		else
    	        		{
    	        			BigDecimal tempJumlah = BigDecimal.ZERO;
    	        			if(editForm.getTempDescrDanJumlah().get(i).getJumlahDebet() !=null && !BigDecimal.ZERO.equals(editForm.getTempDescrDanJumlah().get(i).getJumlahDebet()))
    	        			{
    	        				tempJumlah = editForm.getTempDescrDanJumlah().get(i).getJumlahDebet();
    	        			}
    	        			else if(editForm.getTempDescrDanJumlah().get(i).getJumlahKredit() !=null && !BigDecimal.ZERO.equals(editForm.getTempDescrDanJumlah().get(i).getJumlahKredit()))
    	        			{
    	        				tempJumlah = editForm.getTempDescrDanJumlah().get(i).getJumlahKredit();
    	        			}
    	        			biayaUlinkDbVOList.add( new BiayaUlinkDbVO( editForm.getTempDescrDanJumlah().get(i).getDescr(), tempJumlah, ljbId, editForm.getTempDescrDanJumlah().get(i).getLkuId() ) );
    	        		}
            		}
            	}
            }
        }
        else
        {
        	setoranPremiDbVOList = Common.serializableList(refundBusiness.selectSetoranPremiBySpaj(editForm.getSpaj()));
        	biayaUlinkDbVOList = refundBusiness.selectBiayaUlink(editForm.getSpaj());
        }
        
        paramsVO.setSetoranPremiDbVOList( setoranPremiDbVOList );
        paramsVO.setBiayaUlinkDbVOList( biayaUlinkDbVOList );
        paramsVO.setHasUnitFlag( editForm.getHasUnitFlag() );

        HttpSession session = request.getSession();
        User currentUser = ( User ) session.getAttribute( "currentUser" );

        Integer ulinkFlag = refundBusiness.isUnitLink( paramsVO.getSpajNo() )? 1 : 0;
        Integer spajAlreadyInDetRefundOrNot = refundBusiness.selectCheckSpaj(editForm.getSpaj());
        editForm.setCheckSpajInDetRefund(spajAlreadyInDetRefundOrNot) ;
        paramsVO.setUlinkFlag( ulinkFlag );

        paramsVO.setCreateWho( editForm.getCreateWho() == null? new BigDecimal( currentUser.getLus_id() ) : editForm.getCreateWho() );
        paramsVO.setUpdateWho( new BigDecimal( currentUser.getLus_id() ) );
        paramsVO.setCreateWhen( editForm.getCreateWhen() == null? new Date() : editForm.getCreateWhen() );
        paramsVO.setUpdateWhen( new Date() );
        //if( "onPressButtonBatalkanSpaj".equals( theEvent ) )
        if( "onPressAccBatalSpaj".equals( theEvent ) )
        {
        	paramsVO.setCancelWhen( editForm.getCancelWhen() == null? new Date() : editForm.getCancelWhen() );
        	paramsVO.setCancelWho( editForm.getCancelWho() == null? new BigDecimal( currentUser.getLus_id() ) : editForm.getCancelWho() );
        }
        paramsVO.setPosisi( posisiCd );

        HashMap<String, Object> resultMap = refundBusiness.deleteThenInsertRefund( paramsVO, actionMessage, editForm, theEvent, editForm.getHasUnitFlag(), editForm.getTindakanCd() );
        request.setAttribute( "pageMessage", resultMap.get( "pageMessage" ) );

        Boolean result = ( Boolean ) resultMap.get( "succeed" );
        if( result )
        {
            editForm.setPosisiNo( paramsVO.getPosisi() );
        }
        
        return result;
    }

    public boolean updatePayment( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper.updatePayment" );

        String theEvent = ( ( RefundForm ) command ).getTheEvent();
        
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        RefundForm refundForm = ( RefundForm ) command;

        MstRefundParamsVO paramsVO = new MstRefundParamsVO();

        paramsVO.setSpajNo( editForm.getSpaj() );
        paramsVO.setAlasanCd( editForm.getAlasanCd() );
        paramsVO.setAlasanLain( genAlasan( editForm ) );
        paramsVO.setAlasanLainForLabel( genAlasanForLabel( editForm ) );
        paramsVO.setKliCabangBank( editForm.getCabangBank() );
        paramsVO.setKliKotaBank( editForm.getKotaBank() );
        paramsVO.setKliNama( editForm.getAtasNama() );
        paramsVO.setKliNamaBank( editForm.getNamaBank() );
        paramsVO.setKliNorek( editForm.getNorek() );
        paramsVO.setSpajBaruNo( editForm.getSpajBaru() );
        paramsVO.setTindakanCd( editForm.getTindakanCd() );
        BigDecimal totalPremiDikembalikan = ( BigDecimal ) refundForm.getRefundDocumentVO().getParams().get( "totalPremiDikembalikan" );
        if( totalPremiDikembalikan != null )
        {
        	paramsVO.setTotalPremiDikembalikan( totalPremiDikembalikan );	
        }
        else
        {
        	paramsVO.setTotalPremiDikembalikan( editForm.getPremiDikembalikan() );	
        }
        paramsVO.setAddLampiranList( editForm.getLampiranAddList() );

        paramsVO.setBiayaAdmin( editForm.getBiayaAdmin() );
        paramsVO.setBiayaMedis( editForm.getBiayaMedis() );
        paramsVO.setBiayaLain( editForm.getBiayaLain() );
        paramsVO.setBiayaLainDescr( editForm.getBiayaLainDescr() );

        paramsVO.setPenarikanUlinkVOList( editForm.getPenarikanUlinkVOList() );
        // TODO: isi list2 yang 
        ArrayList<SetoranPremiDbVO> setoranPremiDbVOList = new ArrayList<SetoranPremiDbVO>();
        ArrayList<BiayaUlinkDbVO> biayaUlinkDbVOList = new ArrayList<BiayaUlinkDbVO>();        
        if( refundBusiness.isUnitLink(editForm.getSpaj()) && editForm.getPenarikanUlinkVOList() != null && "sudah".equals(editForm.getStatusUnit()))
        {
        	if(editForm.getTempDescrDanJumlah()!=null && editForm.getTempDescrDanJumlah().size() > 0)
	        {
	        	for( int i = 0 ; i < editForm.getTempDescrDanJumlah().size() ; i++ )
	        	{
	    			String ljbIdStr = editForm.getTempDescrDanJumlah().get(i).getLjbId();
	    			Integer ljbId = null;
	    			if( ljbIdStr == null || "".equals(ljbIdStr))
	    			{
	    				ljbId = null;
	    			}
	    			else 
	    			{
	    				ljbId = LazyConverter.toInt(ljbIdStr);
	    			}
	        		if("yes".equals(editForm.getTempDescrDanJumlah().get(i).getSetoranOrNot()))
	        		{
	        			setoranPremiDbVOList.add( new SetoranPremiDbVO( editForm.getTempDescrDanJumlah().get(i).getTitipanKe(),editForm.getTempDescrDanJumlah().get(i).getTglSetor(),editForm.getTempDescrDanJumlah().get(i).getJumlahPremi(),editForm.getTempDescrDanJumlah().get(i).getLkuId(), null, editForm.getTempDescrDanJumlah().get(i).getDescr(), editForm.getTempDescrDanJumlah().get(i).getNoPre(), editForm.getTempDescrDanJumlah().get(i).getNoVoucher() ) );
	        		}
	        		else
	        		{
	        			BigDecimal tempJumlah = BigDecimal.ZERO;
	        			if(editForm.getTempDescrDanJumlah().get(i).getJumlahDebet() !=null && !BigDecimal.ZERO.equals(editForm.getTempDescrDanJumlah().get(i).getJumlahDebet()))
	        			{
	        				tempJumlah = editForm.getTempDescrDanJumlah().get(i).getJumlahDebet();
	        			}
	        			else if(editForm.getTempDescrDanJumlah().get(i).getJumlahKredit() !=null && !BigDecimal.ZERO.equals(editForm.getTempDescrDanJumlah().get(i).getJumlahKredit()))
	        			{
	        				tempJumlah = editForm.getTempDescrDanJumlah().get(i).getJumlahKredit();
	        			}
	        			biayaUlinkDbVOList.add( new BiayaUlinkDbVO( editForm.getTempDescrDanJumlah().get(i).getDescr(), tempJumlah, ljbId, editForm.getTempDescrDanJumlah().get(i).getLkuId() ) );
	        		}
	        	}
	        }
        }
        else
        {
        	setoranPremiDbVOList = refundBusiness.selectSetoranPremiBySpaj(editForm.getSpaj());
        	biayaUlinkDbVOList = refundBusiness.selectBiayaUlink(editForm.getSpaj());
	        
        }
        paramsVO.setSetoranPremiDbVOList(setoranPremiDbVOList);
        paramsVO.setBiayaUlinkDbVOList(biayaUlinkDbVOList);
        paramsVO.setHasUnitFlag( editForm.getHasUnitFlag() );
        paramsVO.setPrevLspdId( editForm.getPrevLspdId() );
        
        HttpSession session = request.getSession();
        User currentUser = ( User ) session.getAttribute( "currentUser" );

        boolean isUlink = refundBusiness.isUnitLink( paramsVO.getSpajNo() );
        Integer spajAlreadyInDetRefundOrNot = refundBusiness.selectCheckSpaj(editForm.getSpaj());
        editForm.setCheckSpajInDetRefund(spajAlreadyInDetRefundOrNot) ;
        Integer ulinkFlag = isUlink? 1 : 0;
        Integer posisiCd = isUlink? RefundConst.POSISI_REFUND_ULINK_PAID : RefundConst.POSISI_REFUND_NON_ULINK_PAID;
        paramsVO.setUlinkFlag( ulinkFlag );

        paramsVO.setCreateWho( editForm.getCreateWho() == null? new BigDecimal( currentUser.getLus_id() ) : editForm.getCreateWho() );
        paramsVO.setUpdateWho( new BigDecimal( currentUser.getLus_id() ) );
        paramsVO.setCreateWhen( editForm.getCreateWhen() == null? new Date() : editForm.getCreateWhen() );
        paramsVO.setUpdateWhen( new Date() );
        paramsVO.setPayment( editForm.getPayment() );
        paramsVO.setPaymentDate( editForm.getPaymentDate() );
        paramsVO.setCancelWhen( editForm.getCancelWhen() == null? new Date() : editForm.getCancelWhen() );
        paramsVO.setCancelWho( editForm.getCancelWho() == null? new BigDecimal( currentUser.getLus_id() ) : editForm.getCancelWho() );
        paramsVO.setPosisi( posisiCd );

        Boolean result;
        if( editForm.getPayment() == null || editForm.getPaymentDate() == null )
        {
            request.setAttribute( "pageMessage", "Mohon melengkapi data pembayaran di halaman sebelumnya" );
            result = false;
        }
        else
        {
            Map<String, Object> resultMap = refundBusiness.deleteThenInsertRefund( paramsVO, "Update payment", editForm, theEvent, editForm.getHasUnitFlag(), editForm.getTindakanCd() );
            request.setAttribute( "pageMessage", resultMap.get( "pageMessage" ) );

            result = ( Boolean ) resultMap.get( "succeed" );
            if( result )
            {
                editForm.setPosisiNo( paramsVO.getPosisi() );
            }
        }

        return result;
    }
    
    public void statusULinkOrNot( HttpServletRequest request, Object command, String theEvent )
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundEditHelper.refreshPenarikanList" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
        String spaj = null;
        Integer hasUnitFlag = null;
	    if("onPressImageSuratBatal".equals(theEvent))
	    {
	    	spaj = lookupForm.getSelectedRowCd();
	    }
	    else 
	    {
	    	spaj = editForm.getSpaj();
	    }
        RefundDbVO refundDbVO = refundBusiness.selectRefundByCd( spaj );
        
        if( refundBusiness.isUnitLink( editForm.getSpaj() ) )
        {
            editForm.setHasUnitDisplay( CommonConst.DISPLAY_TRUE );
    	    if("onPressImageSuratBatal".equals(theEvent))
    	    {
    	    	hasUnitFlag = refundDbVO.getHasUnitFlag();
    	    }
    	    else 
    	    {
    	    	hasUnitFlag = editForm.getHasUnitFlag();
    	    }
            if( hasUnitFlag != null && hasUnitFlag.equals( 1 ) || refundBusiness.getHasFundAllocation(editForm.getSpaj()) )
            {
                editForm.setPenarikanUlinkVOListDisplay( CommonConst.DISPLAY_TRUE );
                editForm.setStatusUnit("sudah");
            }
            else if(hasUnitFlag != null && hasUnitFlag.equals( 0 ) || !refundBusiness.getHasFundAllocation(editForm.getSpaj()) )
            {
            	editForm.setStatusUnit("belum");
            }
            editForm.setULinkOrNot("Link");
        }
        else
        {
        	editForm.setULinkOrNot("not Link");
        }
    }
}
