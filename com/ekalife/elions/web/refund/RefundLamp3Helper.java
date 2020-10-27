package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundLamp3Helper
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.User;
import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.model.refund.RefundForm;
import com.ekalife.elions.model.refund.RefundPreviewLamp3Form;
import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.elions.web.refund.vo.CheckSpajParamsVO;
import com.ekalife.elions.web.refund.vo.Lamp3ParamsVO;
import com.ekalife.elions.web.refund.vo.Lamp3VO;
import com.ekalife.elions.web.refund.vo.MstRefundParamsVO;
import com.ekalife.elions.web.refund.vo.PenarikanUlinkVO;
import com.ekalife.elions.web.refund.vo.RefundDocumentVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;
import com.ekalife.utils.FormatDate;

public class RefundLamp3Helper extends RefundHelperParent
{
    protected final Log logger = LogFactory.getLog( getClass() );

    public RefundLamp3Helper()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp3Helper constructor is called ..." );
    }

    private RefundBusiness refundBusiness;

    public void setRefundBusiness( RefundBusiness refundBusiness )
    {
        this.refundBusiness = refundBusiness;
    }

    private Lamp3ParamsVO prepareLamp3ParamsVO( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp3Helper.prepareLamp3ParamsVO" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        Lamp3ParamsVO paramsVO = new Lamp3ParamsVO();
        if( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() ) ||  RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() ))
        {
            paramsVO.setSpajNo( editForm.getSpaj().trim() );
            paramsVO.setSpajBaruNo( editForm.getSpajBaru().trim() );
            paramsVO.setAlasan( genAlasan( editForm ) );
            paramsVO.setAlasanForLabel(genAlasanForLabel(editForm));
        }
        else if( RefundConst.TINDAKAN_TIDAK_ADA.equals( editForm.getTindakanCd() ) )
        {
            paramsVO.setSpajNo( editForm.getSpaj().trim() );
            paramsVO.setAlasan( genAlasan( editForm ) );
            paramsVO.setAlasanForLabel(genAlasanForLabel(editForm));
        }
        return paramsVO;
    }

    public HashMap<String, Object> genParamsTindakanGantiTertanggungPdf( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp3Helper.genParamsTindakanGantiTertanggungPdf" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        Lamp3ParamsVO paramsVO = prepareLamp3ParamsVO( command );
        
        // ini adalah bagian inti dari memproses report
        Lamp3VO lamp3VO = refundBusiness.retrieveLamp3( paramsVO, editForm );
        CheckSpajParamsVO checkSpajInDb = refundBusiness.selectCheckSpajInDb( lamp3VO.getSpajNo() );
        HashMap<String, Object> params = new HashMap<String, Object>();
        String statementAvailableOrNot = null;
        String gantiTertanggungOrGantiPlan = null;
        String keterangan = null;
        String statementLamp1AvailableOrNot = null;
        String statementLamp2AvailableOrNot = null;
        if( lamp3VO.getPrevLspdId() != null )
        {
        	editForm.setPrevLspdId( lamp3VO.getPrevLspdId() );
        }
        if(lamp3VO.getTempDescrDanJumlah()!=null && lamp3VO.getTempDescrDanJumlah().size() > 0)
        {
        	editForm.setTempDescrDanJumlah(lamp3VO.getTempDescrDanJumlah());
        }
        if(lamp3VO.getStatementLamp1() != null )
        {
        	statementLamp1AvailableOrNot = "available";
        }
        if(lamp3VO.getStatementLamp2() != null)
        {
        	statementLamp2AvailableOrNot = "available";
        }
        if(lamp3VO.getStatementLamp1() == null && lamp3VO.getStatementLamp2() == null && editForm.getLampiranAddList() == null ||
        		lamp3VO.getStatementLamp1() == null && lamp3VO.getStatementLamp2() == null && editForm.getLampiranAddList() != null && editForm.getLampiranAddList().size() == 0)
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
        params.put( "hal", lamp3VO.getHal() );
        params.put( "insuredName", lamp3VO.getInsuredName() );
        params.put( "newInsuredName", lamp3VO.getNewInsuredName() );
        params.put( "newPolicyHolderName", lamp3VO.getNewPolicyHolderName() );
        params.put( "newSpajNo", lamp3VO.getNewSpajNo() );
        params.put( "newProductName", lamp3VO.getNewProductName() );
        params.put( "noUrutMemo", lamp3VO.getNoUrutMemo() );
        params.put( "policyHolderName", lamp3VO.getPolicyHolderName() );
        params.put( "policyNo", lamp3VO.getPolicyNo() );
        params.put( "productName", lamp3VO.getProductName() );
        params.put( "signer", lamp3VO.getSigner() );
        params.put( "spajNo", lamp3VO.getSpajNo() );
        params.put( "statement", lamp3VO.getStatement() );
        params.put( "statementLamp1", lamp3VO.getStatementLamp1() );
        params.put( "statementLamp2", lamp3VO.getStatementLamp2() );
        params.put( "tanggal", lamp3VO.getTanggal() );
        params.put( "statementAvailableOrNot", statementAvailableOrNot );
        params.put( "efektifPolis", FormatDate.toIndonesian( lamp3VO.getBegDate() )+ " - " + FormatDate.toIndonesian( lamp3VO.getEndDate() ) );
        params.put( "ttdDrIngrid", "com/ekalife/elions/reports/refund/images/inge.gif" );
        if( checkSpajInDb != null && !"".equals( checkSpajInDb ) && checkSpajInDb.getNoSurat() != null && !"".equals( checkSpajInDb.getNoSurat() ) )// jika no_surat sudah ada di DB
        {
        	 params.put( "noSurat", checkSpajInDb.getNoSurat() );
        }
        else if( checkSpajInDb == null || "".equals( checkSpajInDb ))// jika no_surat blm ada di DB
        {
        	 String noSurat = refundBusiness.retrieveNoSurat( lamp3VO.getSpajNo(), editForm.getTindakanCd() );
        	 params.put( "noSurat", noSurat );
        }
        else
        {
        	 params.put( "noSurat", "" );
        }
        //detail
        params.put( "dataSource", lamp3VO.getRincianPolisList() );
        params.put( "gantiTertanggungOrGantiPlan", gantiTertanggungOrGantiPlan );
        params.put( "keterangan", keterangan );
        
        return params;
    }

    public void generateDocumentLamp3( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp3Helper.generateDocumentLamp3" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        RefundDocumentVO refundDocumentVO = new RefundDocumentVO();
        refundDocumentVO.setDownloadUrlSession( "refund/download_refund_document.htm" );
        if( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() ) )
        {
        	refundDocumentVO.setJasperFile( "com/ekalife/elions/reports/refund/lamp3_surat_ganti_tertanggung.jasper" );	
        }
        else if( RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() ))
        {
        	refundDocumentVO.setJasperFile( "com/ekalife/elions/reports/refund/lamp3_surat_ganti_plan.jasper" );	
        }
        else if( RefundConst.TINDAKAN_TIDAK_ADA.equals( editForm.getTindakanCd() ) )
        {
        	refundDocumentVO.setJasperFile( "com/ekalife/elions/reports/refund/lamp3_surat_batal_tanpa_tindakan.jasper" );
        }
        refundDocumentVO.setParams( genParamsTindakanGantiTertanggungPdf( command ) );
        RefundForm refundForm = ( RefundForm ) command;
        refundForm.setRefundDocumentVO( refundDocumentVO );
    }

    public boolean onPressButtonSaveDraftTindakanGantiTertanggung( HttpServletRequest request, Object command, String actionMessage, Integer posisiCd )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp3Helper.onPressButtonSaveDraftTindakanGantiTertanggung" );

        String theEvent = ( ( RefundForm ) command ).getTheEvent();
        
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        MstRefundParamsVO paramsVO = new MstRefundParamsVO();

        paramsVO.setSpajNo( editForm.getSpaj() );
        paramsVO.setAlasanCd( editForm.getAlasanCd() );
        paramsVO.setAlasanLain( genAlasan( editForm ) );
        paramsVO.setAlasanLainForLabel( genAlasanForLabel( editForm ) );
        paramsVO.setTindakanCd( editForm.getTindakanCd() );
        paramsVO.setBiayaAdmin( editForm.getBiayaAdmin() );
        paramsVO.setBiayaLain( editForm.getBiayaLain() );
        paramsVO.setBiayaLainDescr( editForm.getBiayaLainDescr() );
        paramsVO.setBiayaMedis( editForm.getBiayaMedis() );
        paramsVO.setSpajBaruNo( editForm.getSpajBaru() );
        paramsVO.setPrevLspdId( editForm.getPrevLspdId() );
        paramsVO.setAddLampiranList( editForm.getLampiranAddList() );

        // TODO: isi list2 yang 
        ArrayList<SetoranPremiDbVO> setoranPremiDbVOList = refundBusiness.setoranPremiDbVOListForGantiTertanggungAndPlan(editForm.getSpaj());
        
        paramsVO.setSetoranPremiDbVOList( setoranPremiDbVOList );
        paramsVO.setHasUnitFlag( editForm.getHasUnitFlag() );
        
        HttpSession session = request.getSession();
        User currentUser = ( User ) session.getAttribute( "currentUser" );

        Integer ulinkFlag = refundBusiness.isUnitLink( paramsVO.getSpajNo() )? 1 : 0;
        Integer spajAlreadyInDetRefundOrNot = refundBusiness.selectCheckSpaj( editForm.getSpaj() );
        editForm.setCheckSpajInDetRefund( spajAlreadyInDetRefundOrNot );
        paramsVO.setUlinkFlag( ulinkFlag );
        
        paramsVO.setCreateWhen( editForm.getCreateWhen() == null? new Date() : editForm.getCreateWhen() );
        paramsVO.setUpdateWhen( new Date() );
        paramsVO.setCreateWho( editForm.getCreateWho() == null? new BigDecimal( currentUser.getLus_id() ) : editForm.getCreateWho() );
        paramsVO.setUpdateWho( new BigDecimal( currentUser.getLus_id() ) );
        if( "onPressButtonBatalkanSpaj".equals( theEvent ) )
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

    public void initializeLamp3Form( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp3Helper.initializeLamp3Form" );

        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        RefundPreviewLamp3Form lamp3Form = ( ( RefundForm ) command ).getLamp3Form();
        User user = (User) request.getSession().getAttribute("currentUser");   

        String displayRedempt = null;
        String disabledCancelButton = null;
        String disabledKirimRedempt = null;
        String disabledAcceptButton = null;
        
        if( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() ) || RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() ) ){
        	if( refundBusiness.isUnitLink( editForm.getSpaj() ) ){ //Ulink
            	if( editForm.getHasUnitFlag() != null && RefundConst.UNIT_FLAG.equals( editForm.getHasUnitFlag() ) ){ //Sudah fund
            		displayRedempt = CommonConst.DISPLAY_TRUE;
            		if( RefundConst.POSISI_DRAFT.equals( editForm.getPosisiNo() ) || editForm.getPosisiNo() == null ){
            			disabledKirimRedempt = CommonConst.DISABLED_FALSE;
            			disabledAcceptButton = CommonConst.DISABLED_TRUE;
          			}else if( RefundConst.POSISI_GANTI_TERTANGGUNG_ULINK_REDEMPT_SEND.equals( editForm.getPosisiNo() ) ){
          				disabledKirimRedempt = CommonConst.DISABLED_TRUE;
            			disabledAcceptButton = CommonConst.DISABLED_TRUE;
          				for(int i=0; i<editForm.getPenarikanUlinkVOList().size(); i++){
                            PenarikanUlinkVO vo = editForm.getPenarikanUlinkVOList().get(i);
                            if(vo.getJumlah() != null){
                            	disabledAcceptButton = CommonConst.DISABLED_FALSE;
                            }
                        }
          			}else{
          				disabledKirimRedempt = CommonConst.DISABLED_TRUE;
            			disabledAcceptButton = CommonConst.DISABLED_TRUE;
          			}
            		
            	}else{ //Belum fund
            		displayRedempt = CommonConst.DISPLAY_FALSE;     
            		
            		if( RefundConst.POSISI_DRAFT.equals( editForm.getPosisiNo() ) || editForm.getPosisiNo() == null ){
          				disabledAcceptButton = CommonConst.DISABLED_FALSE;
          			}else{
          				disabledAcceptButton =  CommonConst.DISABLED_TRUE;
          			}
            	}
      			
      			if( RefundConst.USER_AKSEP_COLECTION.indexOf(user.getLus_id().trim())>-1 && RefundConst.POSISI_GANTI_TERTANGGUNG_ULINK_ACCEPTATION.equals( editForm.getPosisiNo() ) ){
      				disabledCancelButton = CommonConst.DISABLED_FALSE;
      			}else{
      				disabledCancelButton = CommonConst.DISABLED_TRUE;
      			}
            	
        	}else{ //Non Link
        		displayRedempt = CommonConst.DISPLAY_FALSE; 
        		
      			if( RefundConst.POSISI_DRAFT.equals( editForm.getPosisiNo() ) || editForm.getPosisiNo() == null ){
      				disabledAcceptButton = CommonConst.DISABLED_FALSE;
      			}else{
      				disabledAcceptButton =  CommonConst.DISABLED_TRUE;
      			}
      			
      			if( RefundConst.USER_AKSEP_COLECTION.indexOf(user.getLus_id().trim())>-1 && RefundConst.POSISI_GANTI_TERTANGGUNG_NON_ULINK_ACCEPTATION.equals( editForm.getPosisiNo() ) ){
      				disabledCancelButton = CommonConst.DISABLED_FALSE;
      			}else{
      				disabledCancelButton = CommonConst.DISABLED_TRUE;
      			}
        	}
        }

        lamp3Form.setButtonPreviewRedemptDisplay( displayRedempt );
        lamp3Form.setButtonAccBatalSpajIsDisabled( disabledAcceptButton );
        lamp3Form.setButtonBatalkanSpajIsDisabled( disabledCancelButton );

        if( editForm.getPosisiNo() == null || editForm.getPosisiNo().equals( RefundConst.POSISI_DRAFT ) )
        {
            lamp3Form.setButtonSaveDraftIsDisabled( CommonConst.DISABLED_FALSE );
            lamp3Form.setButtonViewAttachmentIsDisabled( CommonConst.DISABLED_TRUE );
        }
        else
        {
            lamp3Form.setButtonSaveDraftIsDisabled( CommonConst.DISABLED_TRUE );
            lamp3Form.setButtonViewAttachmentIsDisabled( CommonConst.DISABLED_FALSE );
        }

    }
}
