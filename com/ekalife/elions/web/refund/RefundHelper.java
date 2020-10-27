package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Proposal
 * Function Id         	: 
 * Program Name   		: RefundHelper
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Sep 23, 2008 2:42:19 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import id.co.sinarmaslife.std.util.StringUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.User;
import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.model.refund.RefundForm;
import com.ekalife.elions.model.refund.RefundLookupForm;
import com.ekalife.elions.model.refund.RefundPreviewInstruksiRedemptForm;
import com.ekalife.elions.model.refund.RefundPreviewLamp1Form;
import com.ekalife.elions.model.refund.RefundPreviewLamp3Form;
import com.ekalife.elions.model.refund.RefundSignInForm;
import com.ekalife.elions.model.sms.Smsserver_out;
import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.elions.web.refund.product.RefundProductPowerSave;
import com.ekalife.elions.web.refund.vo.BatalParamsVO;
import com.ekalife.elions.web.refund.vo.BiayaUlinkDbVO;
import com.ekalife.elions.web.refund.vo.CheckSpajParamsVO;
import com.ekalife.elions.web.refund.vo.LampiranListVO;
import com.ekalife.elions.web.refund.vo.MstBatalParamsVO;
import com.ekalife.elions.web.refund.vo.MstRefundParamsVO;
import com.ekalife.elions.web.refund.vo.PenarikanUlinkVO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.RefundDbVO;
import com.ekalife.elions.web.refund.vo.RefundDetDbVO;
import com.ekalife.elions.web.refund.vo.RefundDocumentVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.LazyConverter;
import com.ekalife.utils.MappingUtil;

public class RefundHelper extends RefundHelperParent
{
    protected final Log logger = LogFactory.getLog( getClass() );

    public RefundHelper()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper constructor is called ..." );
    }

    private RefundBusiness refundBusiness;
    private RefundLookupHelper lookupHelper;
    private RefundEditHelper editHelper;
    private RefundLamp1Helper lamp1Helper;
    private RefundGbrA1Helper gbrA1Helper;
    private RefundGbrA2Helper gbrA2Helper;
    private RefundGbrA3Helper gbrA3Helper;
    private RefundGbrA4Helper gbrA4Helper;
    private RefundGbrB1Helper gbrB1Helper;
    private RefundGbrB2Helper gbrB2Helper;
    private RefundGbrB3Helper gbrB3Helper;
    private RefundLamp3Helper lamp3Helper;
    private RefundRedemptHelper redemptHelper;

    public void setRefundBusiness( RefundBusiness refundBusiness )
    {
        this.refundBusiness = refundBusiness;
    }

    public void setLookupHelper( RefundLookupHelper lookupHelper )
    {
        this.lookupHelper = lookupHelper;
    }

    public void setGbrA1Helper( RefundGbrA1Helper gbrA1Helper ) {
		this.gbrA1Helper = gbrA1Helper;
	}

	public void setGbrA4Helper(RefundGbrA4Helper gbrA4Helper) {
		this.gbrA4Helper = gbrA4Helper;
	}

	public void setGbrA2Helper(RefundGbrA2Helper gbrA2Helper) {
		this.gbrA2Helper = gbrA2Helper;
	}

	public void setGbrA3Helper(RefundGbrA3Helper gbrA3Helper) {
		this.gbrA3Helper = gbrA3Helper;
	}

	public void setGbrB1Helper(RefundGbrB1Helper gbrB1Helper) {
		this.gbrB1Helper = gbrB1Helper;
	}
	
	public void setGbrB2Helper(RefundGbrB2Helper gbrB2Helper) {
		this.gbrB2Helper = gbrB2Helper;
	}

	public void setGbrB3Helper(RefundGbrB3Helper gbrB3Helper) {
		this.gbrB3Helper = gbrB3Helper;
	}

	public void setEditHelper( RefundEditHelper editHelper )
    {
        this.editHelper = editHelper;
    }

    public void setLamp1Helper( RefundLamp1Helper lamp1Helper )
    {
        this.lamp1Helper = lamp1Helper;
    }

    public void setLamp3Helper( RefundLamp3Helper lamp3Helper )
    {
        this.lamp3Helper = lamp3Helper;
    }

    public void setRedemptHelper( RefundRedemptHelper redemptHelper )
    {
        this.redemptHelper = redemptHelper;
    }

    public RefundForm initializeForm( HttpServletRequest request  )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.initializeForm" );
        RefundForm refundForm = new RefundForm();    

        RefundLookupForm lookupForm = new RefundLookupForm();
        RefundPreviewLamp1Form lamp1Form = new RefundPreviewLamp1Form();
        RefundPreviewLamp3Form lamp3Form = new RefundPreviewLamp3Form();
        RefundPreviewInstruksiRedemptForm redemptForm = new RefundPreviewInstruksiRedemptForm();
        RefundSignInForm signInForm = new RefundSignInForm();

        lookupForm.setNoOfRowsPerPageList( refundBusiness.generateNoOfRowsPerPageList() );
        lookupForm.setTindakanFilterList( RefundLookupList.getTindakanList() );
        lookupForm.setCurrentPage( 1 );
        lookupForm.setNoOfRowsPerPage( 100 );
        lookupForm.setGotoPage( 1 );

        Date now = new Date();
        lookupForm.setUpdateFromFilter( FormatDate.getFirstDayOfMonth( now ) );
        lookupForm.setUpdateToFilter( FormatDate.getDateInFirstSecond( now ) );

        refundForm.setEditForm( editHelper.intializeEditForm() );
        refundForm.setLookupForm( lookupForm );
        refundForm.setLamp1Form( lamp1Form );
        refundForm.setLamp3Form( lamp3Form );
        refundForm.setRedemptForm( redemptForm );
        refundForm.setSignInForm( signInForm );
        
        return refundForm;
    }
    
    public void setSumMtuUnitTransUlink( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.setSumMtuUnitTransUlink" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        refundBusiness.setSumMtuUnit( editForm );
    }


    public HashMap<String, String> addAdditionalDataMap( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.addAdditionalDataMap" );

        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        PolicyInfoVO policyInfoVO = refundBusiness.genPolicyInfoVOBySpaj( editForm.getSpaj() );
        String currency;

        if( policyInfoVO == null )
        {
            currency = "Rp.";
        }
        else
        {
            currency = policyInfoVO.getLkuId().equals( "01" )? "Rp." : "US$";
        }

        HashMap<String, String> refMap = new HashMap<String, String>();
        refMap.put( "currency", currency );
        return refMap;
    }
    
    public void generateDocumentPreviewEditSetoran( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.generateDocumentPreviewEditSetoran" );
        gbrA2Helper.generateDocumentPreviewEditSetoran( command );        
    }
    
    public void generateSetoranTransform( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.generateDocumentPreviewEditSetoran" );
        gbrA2Helper.generateSetoranTransform( command );        
    }

    public void setCurrentPageForRedemptBackButton( RefundEditForm editForm, Integer currentPage )
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.setCurrentPageForRedemptBackButton" );
    	editForm.setCurrentPage( currentPage );
    }   
    
    public void generateDocumentGbrA1A2B1( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.generateDocumentRedempt" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        setCurrentPageForRedemptBackButton( editForm, RefundConst.PREVIEW_LAMP_1_JSP );
        if( editForm.getAlasanCd() != null && editForm.getTindakanCd() != null && RefundConst.TINDAKAN_REFUND_PREMI.equals( editForm.getTindakanCd() ) 
        		&& editForm.getHasUnitFlag() != null && editForm.getHasUnitFlag() == 1) // unit link dah sudah fund
        {
        	gbrA1Helper.generateDocumentGbrA1( command );
        }
        else if( editForm.getAlasanCd() != null && editForm.getTindakanCd() != null && RefundConst.TINDAKAN_REFUND_PREMI.equals( editForm.getTindakanCd() ) 
        		&& editForm.getHasUnitFlag() !=  null && editForm.getHasUnitFlag() == 0 ) // unit link dan blm fund
        {
        	generateSetoranTransform( command );
        	gbrA2Helper.generateDocumentGbrA2( command );
        }
        else if( editForm.getAlasanCd() != null && editForm.getTindakanCd() != null && RefundConst.TINDAKAN_REFUND_PREMI.equals( editForm.getTindakanCd() ) )
        {
        	gbrB1Helper.generateDocumentGbrB1( command );
        }
    }

    public void generateDocumentGbrA1EditDetail( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.generateDocumentGbrA1EditDetail" );
        gbrA1Helper.generateDocumentGbrA1EditDetail( command );
    }
    
    public void generateCountPremi( Object command, String theEvent )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper.generateDocumentRedempt" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
        refundBusiness.retrieveCountPremi( editForm, lookupForm, theEvent );
    }
    
    public void updateTglKirimDokFisik( Object command, String lusId )
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper.updateTglKirimDokFisik" );
    	RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
    	Date nowDate = refundBusiness.selectNowDate();
    	refundBusiness.updateTglKirimDokFisik( editForm.getSpaj(), editForm.getSendPhysicalDocDate(), "Tgl Kirim Dokumen Fisik", lusId, nowDate );
    }
    
    public void generateAllDescrAndJumlahForPreviewEdit( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.generateAllDescrAndJumlahForPreviewEdit" );
        gbrA1Helper.generateAllDescrAndJumlahForPreviewEdit( command );
    }
    
    public void generateDocumentTindakanTidakAda( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.generateDocumentGbr3AndGbr4" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        if( editForm.getSpaj() != null && isUnitLink( editForm.getSpaj().trim() ) && editForm.getAlasanCd() != null && editForm.getTindakanCd() != null 
        		&& RefundConst.TINDAKAN_TIDAK_ADA.equals( editForm.getTindakanCd() )	)
        {
        	gbrA4Helper.generateDocumentGbrA4( command );
        }
        else if( editForm.getSpaj() != null && isUnitLink( editForm.getSpaj().trim() ) == false && editForm.getAlasanCd() != null && editForm.getTindakanCd() != null 
        		&& RefundConst.TINDAKAN_TIDAK_ADA.equals( editForm.getTindakanCd() )	)
        {
        	gbrB3Helper.generateDocumentGbrB3( command );
        }
    }
    
    public void generateDocumentGbrA3A4B2B3( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.generateDocumentGbr3AndGbr4" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        setCurrentPageForRedemptBackButton( editForm, RefundConst.PREVIEW_LAMP_3_JSP );
        if( editForm.getSpaj() != null && isUnitLink( editForm.getSpaj().trim() ) && editForm.getAlasanCd() != null && editForm.getTindakanCd() != null && RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() ) // jika link dan tindakan ganti tertanggung 
        		|| editForm.getSpaj() != null && isUnitLink( editForm.getSpaj().trim() ) && editForm.getAlasanCd() != null && editForm.getTindakanCd() != null && RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() )	) // jika link dan tindakan ganti plan
        {
        	gbrA3Helper.generateDocumentGbrA3( command );
        }
        else if( editForm.getSpaj() != null && isUnitLink( editForm.getSpaj().trim())== false && editForm.getAlasanCd() != null && editForm.getTindakanCd() != null && RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() )  // jika bkn link dan tindakan ganti tertanggung 
        		|| editForm.getSpaj() != null && isUnitLink( editForm.getSpaj().trim())== false && editForm.getAlasanCd() != null && editForm.getTindakanCd() != null && RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() ))// jika bkn link dan tindakan ganti plan
        {
        	gbrB2Helper.generateDocumentGbrB2( command );
        }
        else if( editForm.getSpaj() != null && isUnitLink( editForm.getSpaj().trim() ) && editForm.getAlasanCd() != null && editForm.getTindakanCd() != null 
        		&& RefundConst.TINDAKAN_TIDAK_ADA.equals( editForm.getTindakanCd() )	)
        {
        	gbrA4Helper.generateDocumentGbrA4( command );
        }
        else if( editForm.getSpaj() != null && isUnitLink( editForm.getSpaj().trim() ) == false && editForm.getAlasanCd() != null && editForm.getTindakanCd() != null 
        		&& RefundConst.TINDAKAN_TIDAK_ADA.equals( editForm.getTindakanCd() )	)
        {
        	gbrB3Helper.generateDocumentGbrB3( command );
        }
    }
    
    public void generateDocumentRefundEdit( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.generateDocumentRefundEdit" );
        editHelper.generateDocumentRefundEdit( command );
    }
    
    public void generateDocumentRedempt( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.generateDocumentRedempt" );
        RefundPreviewInstruksiRedemptForm redemptForm = ( ( RefundForm ) command ).getRedemptForm();
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        redemptForm.setCurrentPage( editForm.getCurrentPage() );
        redemptHelper.generateDocumentRedempt( command );
    }
    
    public void initializeEditFormGeneral( HttpServletRequest request, Object command, String theEvent )
    {
        editHelper.initializeEditFormGeneral( request, command, theEvent );
    }
    
  	public void initializeForm( HttpServletRequest request, Object command )
  	{
  		if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Helper.initializeLamp1Form" );

  		RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
  		RefundPreviewLamp1Form lamp1Form = ( ( RefundForm ) command ).getLamp1Form();
  		String display = refundBusiness.isUnitLink( editForm.getSpaj() )? CommonConst.DISPLAY_TRUE : CommonConst.DISPLAY_FALSE;
        
  		User user = (User) request.getSession().getAttribute("currentUser");  
        
  		if( refundBusiness.isUnitLink( editForm.getSpaj() ) && editForm.getPenarikanUlinkVOList().size() > 0 ){
  			display = CommonConst.DISPLAY_TRUE;
  		}else{
  			display = CommonConst.DISPLAY_FALSE;      
  		}
        
  		lamp1Form.setButtonPreviewRedemptDisplay( display );

  		String disabled = refundBusiness.selectRefundByCd( editForm.getSpaj() ) == null?
                                CommonConst.DISABLED_TRUE : CommonConst.DISABLED_FALSE;
        
  		//lamp1Form.setButtonBatalkanSpajIsDisabled( disabled );
  		lamp1Form.setButtonAccBatalSpajIsDisabled( disabled );

  		//MANTA - Khusus Spesial User Untuk Akseptasi Proses Refund SPAJ Dewi(1614), Titis(1161), dan Anta(3123)
  		if(RefundConst.USER_AKSEP_UW.indexOf(user.getLus_id().trim())>-1 && RefundConst.TINDAKAN_REFUND_PREMI.equals(editForm.getTindakanCd()) &&
  				( (refundBusiness.isUnitLink(editForm.getSpaj()) && RefundConst.POSISI_REFUND_ULINK_ACCEPTATION.equals(editForm.getPosisiNo())) ||
  				(!refundBusiness.isUnitLink(editForm.getSpaj()) && RefundConst.POSISI_REFUND_NON_ULINK_ACCEPTATION.equals(editForm.getPosisiNo())) )){
  			lamp1Form.setButtonBatalkanSpajIsDisabled( CommonConst.DISABLED_FALSE );
  		}else{
  			lamp1Form.setButtonBatalkanSpajIsDisabled( CommonConst.DISABLED_TRUE );
  		}
        
  		if( editForm.getPosisiNo() == null || editForm.getPosisiNo().equals( RefundConst.POSISI_DRAFT ) ){
  			lamp1Form.setButtonSaveDraftIsDisabled( CommonConst.DISABLED_FALSE );
  			lamp1Form.setButtonViewAttachmentIsDisabled( CommonConst.DISABLED_TRUE );
  		}else{
  			lamp1Form.setButtonSaveDraftIsDisabled( CommonConst.DISABLED_TRUE );
  			lamp1Form.setButtonViewAttachmentIsDisabled( CommonConst.DISABLED_FALSE );
  		}

  		if( refundBusiness.isUnitLink( editForm.getSpaj() ) ){
  			// todo
//  			Boolean tempPosisi = false;
//  			Boolean tempPosisiAndUnit = false;

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
  			if(RefundConst.TINDAKAN_GANTI_PLAN.equals(editForm.getTindakanCd()) || RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals(editForm.getTindakanCd())){
  				if( editForm.getPosisiNo()!=null && editForm.getPosisiNo()>=RefundConst.POSISI_REFUND_ULINK_REDEMPT_SENT ){
  					tempPosisi = false;
  				}else{
  					tempPosisi = true;
  				}
  				tempPosisiAndUnit = false;
  				
  			}else{
  				if( RefundConst.POSISI_REFUND_ULINK_REDEMPT_SENT.equals( editForm.getPosisiNo() ) ){
  					tempPosisi = true;
  				}else{
  					tempPosisi = false;
  				}
  				if( editForm.getPosisiNo() != null && editForm.getPenarikanUlinkVOList().size() == 0 &&
  						((editForm.getPosisiNo() < RefundConst.POSISI_REFUND_ULINK_CANCEL) && !editForm.getPosisiNo().equals(RefundConst.POSISI_REFUND_ULINK_ACCEPTATION))){
  					tempPosisiAndUnit = true;
  				}else{
  					tempPosisiAndUnit = false;
  				}
  			}
  			if( tempPosisi || editForm.getPenarikanUlinkVOList().size() == 0 && editForm.getPosisiNo() == null || tempPosisiAndUnit ){
  				//lamp1Form.setButtonBatalkanSpajIsDisabled( CommonConst.DISABLED_FALSE );
            	lamp1Form.setButtonAccBatalSpajIsDisabled( CommonConst.DISABLED_TRUE );
  				for(int i=0; i<editForm.getPenarikanUlinkVOList().size(); i++){
                    PenarikanUlinkVO vo = editForm.getPenarikanUlinkVOList().get(i);
                    if(vo.getJumlah() != null){
                    	lamp1Form.setButtonAccBatalSpajIsDisabled( CommonConst.DISABLED_FALSE );
                    }
                }
  			}else{
  				//lamp1Form.setButtonBatalkanSpajIsDisabled( CommonConst.DISABLED_TRUE );
  				lamp1Form.setButtonAccBatalSpajIsDisabled( CommonConst.DISABLED_TRUE );
  			}
*/
  			if( RefundConst.POSISI_REFUND_ULINK_CANCEL.equals( editForm.getPosisiNo() ) ){
  				lamp1Form.setButtonUpdatePaymentIsDisabled( CommonConst.DISABLED_FALSE );
  			}else{
  				lamp1Form.setButtonUpdatePaymentIsDisabled( CommonConst.DISABLED_TRUE );
  			}
  			
  		}else{
  			if( RefundConst.POSISI_DRAFT.equals( editForm.getPosisiNo() ) || editForm.getPosisiNo() == null ){
  				//lamp1Form.setButtonBatalkanSpajIsDisabled( CommonConst.DISABLED_FALSE );
  				lamp1Form.setButtonAccBatalSpajIsDisabled( CommonConst.DISABLED_FALSE );
  			}else{
  				//lamp1Form.setButtonBatalkanSpajIsDisabled( CommonConst.DISABLED_TRUE );
  				lamp1Form.setButtonAccBatalSpajIsDisabled( CommonConst.DISABLED_TRUE );
  			}

  			if( RefundConst.POSISI_REFUND_NON_ULINK_CANCEL.equals( editForm.getPosisiNo() ) ){
  				lamp1Form.setButtonUpdatePaymentIsDisabled( CommonConst.DISABLED_FALSE );
  			}else{
  				lamp1Form.setButtonUpdatePaymentIsDisabled( CommonConst.DISABLED_TRUE );
  			}
  		}
  	}
  	
    public void initializeLamp3Form( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.initializeLamp3Form" );

        User user = (User) request.getSession().getAttribute("currentUser");  
        
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        RefundPreviewLamp3Form lamp3Form = ( ( RefundForm ) command ).getLamp3Form();
        RefundPreviewInstruksiRedemptForm redemptForm = ( ( RefundForm ) command ).getRedemptForm();
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

        redemptForm.setButtonKirimIsDisabled( disabledKirimRedempt );
        lamp3Form.setButtonPreviewRedemptDisplay( displayRedempt );
        lamp3Form.setButtonAccBatalSpajIsDisabled( disabledAcceptButton );
        lamp3Form.setButtonBatalkanSpajIsDisabled( disabledCancelButton );

        if( editForm.getPosisiNo() == null || editForm.getPosisiNo().equals( RefundConst.POSISI_DRAFT ) ){
            lamp3Form.setButtonSaveDraftIsDisabled( CommonConst.DISABLED_FALSE );
            lamp3Form.setButtonViewAttachmentIsDisabled( CommonConst.DISABLED_TRUE );
        }else{
            lamp3Form.setButtonSaveDraftIsDisabled( CommonConst.DISABLED_TRUE );
            lamp3Form.setButtonViewAttachmentIsDisabled( CommonConst.DISABLED_FALSE );
        }
    }

    public void initializeRedempt( HttpServletRequest request, Object command )
    {
        redemptHelper.initializeRedempt( request, command );
    }

    public void search( HttpServletRequest request, Object command, Integer page )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.search" );
        lookupHelper.search( request, command, page );
    }

    public void setTotalOfPages( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.setTotalOfPages" );
        lookupHelper.setTotalOfPages( command );
    }

    public void setlkuList( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.setTotalOfPages" );
        lookupHelper.setlkuList( command );
    }
    
    public void refreshListPreviewEdit( String theEvent, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.refreshListPreviewEdit" );
        lookupHelper.refreshListPreviewEdit( theEvent, command );
    }
    
    public void onPressLinkFirst( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.onPressLinkFirst" );
        lookupHelper.onPressLinkFirst( command );
    }

    public void onPressLinkPrev( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.onPressLinkPrev" );
        lookupHelper.onPressLinkPrev( command );
    }

    public void onPressLinkNext( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.onPressLinkNext" );
        lookupHelper.onPressLinkNext( command );
    }

    public void onPressLinkLast( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.onPressLinkLast" );
        lookupHelper.onPressLinkLast( command );
    }
    
    public void onPressMoveToMstDetRefundLamp( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.onPressMoveToMstDetRefundLamp" );
        lookupHelper.onPressMoveToMstDetRefundLamp( command );
    }

    public boolean isUnitLink( String spaj )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.isUnitLink" );
        return lookupHelper.isUnitLink( spaj );
    }
    
    
    public void onPressLinkAdd( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressLinkAdd" );
        editHelper.onPressLinkAdd( request, command );
    }
    
    public void onChangeAlasanCd( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onChangeAlasanCd" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        editHelper.onChangeAlasanCd( editForm  );
    }
    
    public void fillTindakanListAndAlasanList( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.fillTindakanListAndAlasanList" );
//        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
//        String selectedRowSpaj = lookupForm.getSelectedRowCd();
        editHelper.fillTindakanListAndAlasanList( request, command );
    }

    public void onPressImageEdit( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressImageEdit" );
        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
        String selectedRowSpaj = lookupForm.getSelectedRowCd();
        editHelper.onPressImageEdit( request, command, selectedRowSpaj );
    }

    public void clearListDescrAndJumlah( Object command )
    {
    	 if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.clearListDescrAndJumlah" );
    	  RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
    	  editHelper.clearListDescrAndJumlah( editForm );
    }
    
    public void setBiayaAdmin( RefundEditForm editForm )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.setBiayaAdmin" );
        editHelper.setBiayaAdmin( editForm );
    }
    
    public void refreshPenarikanList( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.refreshPenarikanList" );
        editHelper.refreshPenarikanList( request, command );
    }

    public boolean onPressButtonUpdatePayment( HttpServletRequest request, Object command, Properties props  )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressButtonUpdatePayment" );
        boolean updatePaymentIsSuccess = updatePayment( request, command );

        if( updatePaymentIsSuccess )
        {
            RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
            sendEmailAfterUpdatePayment( request, props, editForm.getSpaj() );
        }

        return updatePaymentIsSuccess;
    }

    public void onPressButtonKirimRedemption( HttpServletRequest request, Object command, Properties props )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressButtonKirimRedemption" );
        HttpSession session = request.getSession();
        User currentUser = ( User ) session.getAttribute( "currentUser" );
        RefundForm refundForm = ( RefundForm ) command;
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        String validationMessage = "";
        
        //MANTA
        validationMessage = refundBusiness.prosesRedeemUnitForRefund(currentUser.getLus_id(), editForm.getSpaj());
        if(validationMessage.equals("")){
            if( saveDraftTindakanRefund( request, command, "Pembatalan SPAJ", RefundConst.POSISI_DRAFT ) )
            {
                if( redemptHelper.kirimRedemption( request, command ) )
                {
                    editHelper.onPressImageEdit( request, command, editForm.getSpaj() );
                    redemptHelper.generateDocumentRedempt( command );
                    sendEmailAfterKirimRedempt( request, refundForm.getRefundDocumentVO(), refundBusiness.genRedemptFileName( editForm.getSpaj() ), props, editForm.getSpaj() );
                }
            }
        }else{
            request.setAttribute("errMessageFailed", validationMessage);
        }
    }

    public void onPressButtonPrintRedemptionExcel( HttpServletRequest request, Object command, Properties props, Connection connection )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressButtonPrintRedemptionExcel" );
        redemptHelper.generateDocumentRedemptExcel( request, command, connection );
    }
    
    public boolean onPressButtonSaveDraftTindakanTidakAda( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.saveDraftTindakanTidakAda" );
        return editHelper.saveDraftTindakanTidakAda( request, command, "Save Draft" );
    }

    public void onPressButtonSaveDraftTindakanRefund( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressButtonSaveDraftTindakanRefund" );
        saveDraftTindakanRefund( request, command, "Save draft", RefundConst.POSISI_DRAFT );
    }

    public boolean onPressButtonSaveDraftTindakanGantiTertanggung( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressButtonSaveDraftTindakanGantiTertanggung" );
        return onPressButtonSaveDraftTindakanGantiTertanggung( request, command, "Save Draft", RefundConst.POSISI_DRAFT );
    }
    
    public boolean onPressButtonSaveDraftTindakanGantiTertanggung( HttpServletRequest request, Object command, String actionMessage, Integer posisiCd )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressButtonSaveDraftTindakanGantiTertanggung" );

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
        paramsVO.setPenarikanUlinkVOList( editForm.getPenarikanUlinkVOList() );
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
//        if( "onPressButtonBatalkanSpaj".equals( theEvent ) )
//        {
//        	paramsVO.setCancelWhen( editForm.getCancelWhen() == null? new Date() : editForm.getCancelWhen() );
//        	paramsVO.setCancelWho( editForm.getCancelWho() == null? new BigDecimal( currentUser.getLus_id() ) : editForm.getCancelWho() );
//        }
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

    public void onPressButtonBatalkanSpajTindakanTidakAda( String kategoriUW, HttpServletRequest request, Object command, Properties props )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressButtonBatalkanSpajTindakanTidakAda" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        RefundProductSpecInterface productSpecific = generateProduct( editForm );
        BatalParamsVO paramsVO = new BatalParamsVO();
        paramsVO.setAlasan( genAlasan( editForm ) );
        paramsVO.setAlasanForLabel(genAlasanForLabel(editForm));
        HttpSession session = request.getSession();
        User currentUser = ( User ) session.getAttribute( "currentUser" );
        paramsVO.setCurrentUser( currentUser );
        paramsVO.setSpajNo( editForm.getSpaj() );
      	paramsVO.setCancelWhen( editForm.getCancelWhen() == null? new Date() : editForm.getCancelWhen() );
      	paramsVO.setCancelWho( editForm.getCancelWho() == null? new BigDecimal( currentUser.getLus_id() ) : editForm.getCancelWho() );

//        boolean validationPassed;
//        String validationMessage;
//        // jika tidak ada special case pada produk maka dianggap validasi berhasil
//        if( productSpecific == null )
//        {
//            validationPassed = true;
//            validationMessage = "";
//        }
//        else
//        {
//            Map validationResultMap = productSpecific.validationBeforeCancel( paramsVO );
//            validationPassed = ( Boolean ) validationResultMap.get( "validationPassed" );
//            validationMessage = ( String ) validationResultMap.get( "validationMessage" );
//        }
//        if( validationPassed )
//        {
	        if( editHelper.saveDraftTindakanTidakAda( request, command, "Pembatalan SPAJ" ) )
	        {
	            // jika pembatalan sukses, kirim email
	            if( batalkanSpaj( request, command ) )
	            {
	                RefundForm refundForm = ( RefundForm ) command;
	                sendEmailAfterCancel( kategoriUW, request, refundForm.getRefundDocumentVO(), refundBusiness.genLamp3TanpaTindakan( editForm.getSpaj() ), props, editForm.getSpaj(), "TINDAKAN_TIDAK_ADA" );
	            }
	        }
//        }
//        request.setAttribute("errMessageFailed", validationMessage);
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

        paramsVO.setBiayaMerchant( editForm.getBiayaMerchant() );
        paramsVO.setBiayaAdmin( editForm.getBiayaAdmin() );
        paramsVO.setBiayaMedis( editForm.getBiayaMedis() );
        paramsVO.setBiayaLain( editForm.getBiayaLain() );
        paramsVO.setBiayaLainDescr( editForm.getBiayaLainDescr() );
        paramsVO.setAddLampiranList( editForm.getLampiranAddList() );
        
        paramsVO.setPenarikanUlinkVOList( editForm.getPenarikanUlinkVOList() );
        
        // TODO: isi list2 yang 
        ArrayList<SetoranPremiDbVO> setoranPremiDbVOList = new ArrayList<SetoranPremiDbVO>();
        ArrayList<BiayaUlinkDbVO> biayaUlinkDbVOList = new ArrayList<BiayaUlinkDbVO>();        
        if( refundBusiness.isUnitLink(editForm.getSpaj()) && editForm.getPenarikanUlinkVOList() != null && "sudah".equals(editForm.getStatusUnit() ) )
        {
        	if( editForm.getTempDescrDanJumlah()!=null && editForm.getTempDescrDanJumlah().size() > 0 )
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
    	        			//ADD
    	        			setoranPremiDbVOList.add( new SetoranPremiDbVO( editForm.getTempDescrDanJumlah().get(i).getTitipanKe(),editForm.getTempDescrDanJumlah().get(i).getTglSetor(),editForm.getTempDescrDanJumlah().get(i).getJumlahPremi(),editForm.getTempDescrDanJumlah().get(i).getLkuId(), editForm.getTempDescrDanJumlah().get(i).getKurs(), editForm.getTempDescrDanJumlah().get(i).getDescr(), editForm.getTempDescrDanJumlah().get(i).getNoPre(), editForm.getTempDescrDanJumlah().get(i).getNoVoucher() ) );
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
        	if( editForm.getHasUnitFlag() != null && editForm.getHasUnitFlag() == 0 ) // link blm fund
        	{
        		setoranPremiDbVOList = editForm.getSetoranPokokAndTopUpForSave();       		
        	}
        	else
        	{
        		setoranPremiDbVOList = refundBusiness.selectSetoranPremiBySpaj(editForm.getSpaj());	
        	}
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
//        if( "onPressButtonBatalkanSpaj".equals( theEvent ) )
//        {
//        	paramsVO.setCancelWhen( editForm.getCancelWhen() == null? new Date() : editForm.getCancelWhen() );
//        	paramsVO.setCancelWho( editForm.getCancelWho() == null? new BigDecimal( currentUser.getLus_id() ) : editForm.getCancelWho() );
//        }
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
    
    public void onPressButtonAccBatalTindakanRefund( String kategoriUW, HttpServletRequest request, Object command, Properties props )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressButtonAccBatalTindakanRefund" );
        
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        
        Boolean save = false;
        Integer posisi;
        if( refundBusiness.isUnitLink( editForm.getSpaj() ) )
        {
        	if(RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() ) ||
        			RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() )){
        		posisi = RefundConst.POSISI_GANTI_TERTANGGUNG_ULINK_ACCEPTATION;
        	}else{
            	posisi = RefundConst.POSISI_REFUND_ULINK_ACCEPTATION;
        	}
        }
        else 
        {
        	if(RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() ) ||
            		RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() )){
        		posisi = RefundConst.POSISI_GANTI_TERTANGGUNG_NON_ULINK_ACCEPTATION;
        	}else{
            	posisi = RefundConst.POSISI_REFUND_NON_ULINK_ACCEPTATION;
        	}
        }
        
        BatalParamsVO paramsVO = new BatalParamsVO();
        paramsVO.setAlasan( genAlasan( editForm ) );
        paramsVO.setAlasanForLabel(genAlasanForLabel(editForm));
        HttpSession session = request.getSession();
        User currentUser = ( User ) session.getAttribute( "currentUser" );
        paramsVO.setCurrentUser( currentUser );
        paramsVO.setSpajNo( editForm.getSpaj() );
//        paramsVO.setCancelWhen( editForm.getCancelWhen() == null? new Date() : editForm.getCancelWhen() );
//        paramsVO.setCancelWho( editForm.getCancelWho() == null? new BigDecimal( currentUser.getLus_id() ) : editForm.getCancelWho() );
        
    	if(RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() ) ||
        		RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() )){
    		save = onPressButtonSaveDraftTindakanGantiTertanggung( request, command, "Permintaan Aksep Pembatalan", posisi );
    	}else{
        	save = saveDraftTindakanRefund( request, command, "Permintaan Aksep Pembatalan", posisi );
    	}
    	
        if(save) sendEmailPermintaanAccBatal( request, props, editForm );

    }

    public void onPressButtonBatalkanSpajTindakanRefund( String kategoriUW, HttpServletRequest request, Object command, Properties props )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressButtonBatalkanSpajTindakanRefund" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
//        if ()unit link uda dibeli then bakalPosisi = RefundConst.POSISI_REFUND_ULINK_REDEMPT_SENT
//        else if belum then bakalPosisi = RefundConst.POSISI_DRAFT
        Integer posisi = RefundConst.POSISI_REFUND_ULINK_REDEMPT_SENT;
        if( refundBusiness.isUnitLink( editForm.getSpaj() ) )
        {
        	if("belum".equals(editForm.getStatusUnit()))
        	{
        		posisi =  RefundConst.POSISI_DRAFT;
        	}
        	else 
        	{
        		posisi = RefundConst.POSISI_REFUND_ULINK_REDEMPT_SENT;
        	}
        }
        else 
        {
        	posisi = RefundConst.POSISI_REFUND_NON_ULINK_CANCEL;
        }
        RefundProductSpecInterface productSpecific = generateProduct( editForm );
        BatalParamsVO paramsVO = new BatalParamsVO();
        paramsVO.setAlasan( genAlasan( editForm ) );
        paramsVO.setAlasanForLabel(genAlasanForLabel(editForm));
        HttpSession session = request.getSession();
        User currentUser = ( User ) session.getAttribute( "currentUser" );
        paramsVO.setCurrentUser( currentUser );
        paramsVO.setSpajNo( editForm.getSpaj() );
      	paramsVO.setCancelWhen( editForm.getCancelWhen() == null? new Date() : editForm.getCancelWhen() );
      	paramsVO.setCancelWho( editForm.getCancelWho() == null? new BigDecimal( currentUser.getLus_id() ) : editForm.getCancelWho() );

        boolean validationPassed;
        String validationMessage;
        // jika tidak ada special case pada produk maka dianggap validasi berhasil
        if( productSpecific == null )
        {
            validationPassed = true;
            validationMessage = "";
        }
        else
        {
            HashMap validationResultMap = productSpecific.validationBeforeCancel( paramsVO );
            validationPassed = ( Boolean ) validationResultMap.get( "validationPassed" );
            validationMessage = ( String ) validationResultMap.get( "validationMessage" );
        }
        if( validationPassed )
        {
	        if( saveDraftTindakanRefund( request, command, "Pembatalan SPAJ", posisi ) )
	        {
	            // jika pembatalan sukses, kirim email
	            if( batalkanSpaj( request, command ) )
	            {
	                RefundForm refundForm = ( RefundForm ) command;
	                
	                if( editForm.getHasUnitFlag() != null && editForm.getHasUnitFlag() == 1 ) // sudah fund(link)
	                {
	                	gbrA1Helper.generateDocumentGbrA1( command );	
	                }
	                else if( editForm.getHasUnitFlag() != null && editForm.getHasUnitFlag() == 0 ) // blm fund(link)
	                {
	                	gbrA2Helper.generateDocumentGbrA2( command );
	                }
	                else if( editForm.getHasUnitFlag() == null && isUnitLink( editForm.getSpaj() ) == false ) // bkn link
	                {
	                    gbrB1Helper.generateDocumentGbrB1( command );
	                }
	                
	                // TODO: step 2
	                sendEmailAfterCancel( kategoriUW, request, refundForm.getRefundDocumentVO(), refundBusiness.genLamp1FileName( editForm.getSpaj() ), props, editForm.getSpaj(), "TINDAKAN_REFUND_PREMI" );
	            }
	        }
        }
        request.setAttribute("errMessageFailed", validationMessage);
    }

    public void onPressButtonBatalkanSpajGbrA3GbrB2( String kategoriUW, HttpServletRequest request, Object command, Properties props, Integer tindakan  )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressButtonBatalkanSpajTindakanGantiTertanggung" );
        
        RefundForm refundForm = ( RefundForm ) command;
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
 
        String tindakanLabel;
        if( RefundConst.TINDAKAN_GANTI_PLAN.equals( tindakan ) )
        	{ tindakanLabel = "TINDAKAN_GANTI_PLAN"; }
        else
        	{ tindakanLabel = "TINDAKAN_GANTI_TERTANGGUNG"; }
        
        Integer posisiNo;
        
        if( refundBusiness.isUnitLink( editForm.getSpaj() ) ) //ulink
        {
        	if( editForm.getHasUnitFlag() != null && RefundConst.UNIT_FLAG.equals( editForm.getHasUnitFlag() ) )//sudah fund
        	{
        		posisiNo = RefundConst.POSISI_GANTI_PLAN_ULINK_CANCEL;
        	}
        	else//blm fund
        	{
        		posisiNo = RefundConst.POSISI_GANTI_TERTANGGUNG_NON_ULINK_CANCEL;
        	}
        }
        else//non link
        {
        	posisiNo = RefundConst.POSISI_GANTI_PLAN_NON_ULINK_CANCEL;
        }
        
        RefundProductSpecInterface productSpecific = generateProduct( editForm );
        BatalParamsVO paramsVO = new BatalParamsVO();
        paramsVO.setAlasan( genAlasan( editForm ) );
        paramsVO.setAlasanForLabel(genAlasanForLabel(editForm));
        HttpSession session = request.getSession();
        User currentUser = ( User ) session.getAttribute( "currentUser" );
        paramsVO.setCurrentUser( currentUser );
        paramsVO.setSpajNo( editForm.getSpaj() );
      	paramsVO.setCancelWhen( editForm.getCancelWhen() == null? new Date() : editForm.getCancelWhen() );
      	paramsVO.setCancelWho( editForm.getCancelWho() == null? new BigDecimal( currentUser.getLus_id() ) : editForm.getCancelWho() );

        boolean validationPassed;
        String validationMessage;
        // jika tidak ada special case pada produk maka dianggap validasi berhasil
        if( productSpecific == null )
        {
            validationPassed = true;
            validationMessage = "";
        }
        else
        {
            HashMap validationResultMap = productSpecific.validationBeforeCancel( paramsVO );
            validationPassed = ( Boolean ) validationResultMap.get( "validationPassed" );
            validationMessage = ( String ) validationResultMap.get( "validationMessage" );
        }
        if( validationPassed )
        {
        	if( onPressButtonSaveDraftTindakanGantiTertanggung( request, command, "Pembatalan SPAJ", posisiNo ) )
            {
                // jika pembatalan sukses, kirim email
                if( batalkanSpaj( request, command ) )
                {
                    if( editForm.getSpaj() != null && isUnitLink( editForm.getSpaj().trim() ) && editForm.getAlasanCd() != null && editForm.getTindakanCd() != null && RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() ) // jika link dan tindakan ganti tertanggung 
                    		|| editForm.getSpaj() != null && isUnitLink( editForm.getSpaj().trim() ) && editForm.getAlasanCd() != null && editForm.getTindakanCd() != null && RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() )	) // jika link dan tindakan ganti plan
                    {
                    	gbrA3Helper.generateDocumentGbrA3( command );
                    }
                    else if( editForm.getSpaj() != null && isUnitLink( editForm.getSpaj().trim())== false && editForm.getAlasanCd() != null && editForm.getTindakanCd() != null && RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() )  // jika bkn link dan tindakan ganti tertanggung 
                    		|| editForm.getSpaj() != null && isUnitLink( editForm.getSpaj().trim())== false && editForm.getAlasanCd() != null && editForm.getTindakanCd() != null && RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() ))// jika bkn link dan tindakan ganti plan
                    {
                    	gbrB2Helper.generateDocumentGbrB2( command );
                    }
                    sendEmailAfterCancel( kategoriUW, request, refundForm.getRefundDocumentVO(), refundBusiness.genLamp3FileName( editForm.getSpaj(), tindakanLabel ), props, editForm.getSpaj(), tindakanLabel );
                    sendEmailAfterCancelKeBC( request, props, editForm );
                }
            }
        }
        else
        {
        	
        }
            
        request.setAttribute("errMessageFailed", validationMessage);
    }


    /**
     * Important note: Semua pembatalan akan melalui pintu ini (funtion ini)
     *
     * @param request: request
     * @param command: command
     * @return true:  jika pembatalan sukses
     *         false: jika pembatalan gagal
     */
    public boolean batalkanSpaj( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.batalkanSpaj" );

        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();

        BatalParamsVO paramsVO = new BatalParamsVO();
        paramsVO.setAlasan( genAlasan( editForm ) );
        paramsVO.setAlasanForLabel(genAlasanForLabel(editForm));
        HttpSession session = request.getSession();
        User currentUser = ( User ) session.getAttribute( "currentUser" );
        paramsVO.setCurrentUser( currentUser );
        paramsVO.setSpajNo( editForm.getSpaj() );
      	paramsVO.setCancelWhen( editForm.getCancelWhen() == null? new Date() : editForm.getCancelWhen() );
      	paramsVO.setCancelWho( editForm.getCancelWho() == null? new BigDecimal( currentUser.getLus_id() ) : editForm.getCancelWho() );

        Integer posisiNo;
        if( RefundConst.TINDAKAN_TIDAK_ADA.equals( editForm.getTindakanCd() ) )
        {
            posisiNo = RefundConst.POSISI_TIDAK_ADA_CANCEL;
        }
        else if( RefundConst.TINDAKAN_REFUND_PREMI.equals( editForm.getTindakanCd() ) )
        {
            if( refundBusiness.isUnitLink( editForm.getSpaj() ) )
                posisiNo = RefundConst.POSISI_REFUND_ULINK_CANCEL;
            else
                posisiNo = RefundConst.POSISI_REFUND_NON_ULINK_CANCEL;
        }
//        TODO special
        else if( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() ) )
        {
        	if( refundBusiness.isUnitLink( editForm.getSpaj() ) )
        	{
        		if( editForm.getHasUnitFlag() != null && RefundConst.UNIT_FLAG.equals( editForm.getHasUnitFlag() ) )
        		{
        			posisiNo = RefundConst.POSISI_GANTI_TERTANGGUNG_ULINK_CANCEL;
        		}
        		else
        		{
        			posisiNo = RefundConst.POSISI_GANTI_TERTANGGUNG_NON_ULINK_CANCEL;
        		}
        	}
        	else
        	{
        		posisiNo = RefundConst.POSISI_GANTI_TERTANGGUNG_NON_ULINK_CANCEL;
        	}
//            posisiNo = RefundConst.POSISI_GANTI_TERTANGGUNG_CANCEL;
        }
        else if( RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() ) )
        {
        	if( refundBusiness.isUnitLink( editForm.getSpaj() ) )
        	{
        		if( editForm.getHasUnitFlag() != null && RefundConst.UNIT_FLAG.equals( editForm.getHasUnitFlag() ) )
        		{
        			posisiNo = RefundConst.POSISI_GANTI_TERTANGGUNG_ULINK_CANCEL;
        		}
        		else
        		{
        			posisiNo = RefundConst.POSISI_GANTI_TERTANGGUNG_NON_ULINK_CANCEL;
        		}
        	}
        	else
        	{
        		posisiNo = RefundConst.POSISI_GANTI_TERTANGGUNG_NON_ULINK_CANCEL;
        	}
//            posisiNo = RefundConst.POSISI_GANTI_PLAN_CANCEL;
        }
        else
        {
            throw new RuntimeException( "posisi refund tidak bisa didaftarkan" );
        }

        paramsVO.setPosisiNo( posisiNo );
        boolean someRowPenarikanIsEmpty = false;
        if( editForm.getPenarikanUlinkVOList() != null
                    && editForm.getPenarikanUlinkVOList().size() > 0
                    && refundBusiness.isUnitLink( editForm.getSpaj() )
                    && paramsVO.getPosisiNo().equals( 3 )
                    &&  RefundConst.TINDAKAN_REFUND_PREMI.equals( editForm.getTindakanCd() )
                    )
        {
            for( int i = 0; i < editForm.getPenarikanUlinkVOList().size(); i++ )
            {
                PenarikanUlinkVO vo = editForm.getPenarikanUlinkVOList().get( i );
                if( vo.getJumlah() == null )
                {
                    someRowPenarikanIsEmpty = true;
                    break;
                }
            }
        }

        Boolean succeed;
        if( someRowPenarikanIsEmpty )
        {
            request.setAttribute( "pageMessage", "Mohon melengkapi jumlah penarikan di halaman sebelumnya".toString() );
            succeed = false;
        }
        else
        {
            RefundProductSpecInterface product = generateProduct( editForm );
            HashMap<String, Object> resultMap = refundBusiness.batalkanSpaj( paramsVO, product );
            request.setAttribute( "pageMessage", resultMap.get( "pageMessage" ) );

            succeed = ( Boolean ) resultMap.get( "succeed" );
            if( succeed )
            {
                editForm.setPosisiNo( paramsVO.getPosisiNo() );
                //Cek jika tindakan refund, sms ke nasabah (#77603) - Daru 19/10/2015
                if(RefundConst.TINDAKAN_REFUND_PREMI.equals(editForm.getTindakanCd())) {
                	try {
                		String alasan = editForm.getAlasan();
                    	HashMap pemegang = Common.serializableMap(refundBusiness.getDataPemegang(editForm.getSpaj()));
                    	Smsserver_out sms_out = new Smsserver_out();
                    	sms_out.setType("O");
                    	sms_out.setRecipient((String) pemegang.get("NO_HP"));
                    	sms_out.setText("Nasabah Yth, Pengajuan asuransi No." + editForm.getSpaj() + " kami batalkan karena " + 
                    			alasan + ", dana direfund 5 hari kerja, info hub CS: 021 26508300");
                    	sms_out.setLjs_id(37);
                    	sms_out.setPriority(100);
                    	sms_out.setLus_id(0);
                    	sms_out.setReg_spaj(editForm.getSpaj());
                    	sms_out.setMspo_policy_no((String) pemegang.get("MSPO_POLICY_NO"));
                    	
                    	refundBusiness.sendSmsRefund(sms_out, true);
                	} catch (Exception e) {
//						logger.error("ERROR :", e);
					}
                }
            }
        }
        return succeed;
    }

    public void onPressImageSuratBatal( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressImageSuratBatal" );

        RefundForm refundForm = ( RefundForm ) command;
        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
        editHelper.onPressImageEdit( request, command, lookupForm.getSelectedRowCd() );

        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        if( RefundConst.TINDAKAN_REFUND_PREMI.equals( editForm.getTindakanCd() ) && editForm.getHasUnitFlag() != null && editForm.getHasUnitFlag() == 1 )
        {
            gbrA1Helper.generateDocumentGbrA1( command );
        }
        else if( RefundConst.TINDAKAN_REFUND_PREMI.equals( editForm.getTindakanCd() ) && editForm.getHasUnitFlag() != null && editForm.getHasUnitFlag() == 0 )
        {
            gbrA2Helper.generateDocumentGbrA2( command );
        }
        else if( RefundConst.TINDAKAN_REFUND_PREMI.equals( editForm.getTindakanCd() ) && editForm.getHasUnitFlag() == null )
        {
            gbrB1Helper.generateDocumentGbrB1( command );
        }
        else if( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() ) && editForm.getSpaj() != null && isUnitLink( editForm.getSpaj() )  
        	|| RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() ) && editForm.getSpaj() != null && isUnitLink( editForm.getSpaj() ) )
        {
            gbrA3Helper.generateDocumentGbrA3( command );
        }
        else if( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() ) && editForm.getSpaj() != null && isUnitLink( editForm.getSpaj() ) == false
            	|| RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() )  && editForm.getSpaj() != null && isUnitLink( editForm.getSpaj() ) == false)
        {
        	gbrB2Helper.generateDocumentGbrB2( command );
        }
        else if( RefundConst.TINDAKAN_TIDAK_ADA.equals( editForm.getTindakanCd() ) && editForm.getSpaj() != null && isUnitLink( editForm.getSpaj() ) ) 
        {
        	gbrA4Helper.generateDocumentGbrA4( command );
        }
        else if( RefundConst.TINDAKAN_TIDAK_ADA.equals( editForm.getTindakanCd() ) && editForm.getSpaj() != null && isUnitLink( editForm.getSpaj() ) == false ) 
        {
        	gbrB3Helper.generateDocumentGbrB3( command );
        }

        doDownload( request, lookupForm, refundForm.getRefundDocumentVO() );
    }
    
    public void onPressLinkSuratRekap( HttpServletRequest request, Object command )
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressLinkSuratRekap" );
    	RefundForm refundForm = ( RefundForm ) command;
        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
        lookupHelper.generateDocumentRekapBatal( command );
        doDownload( request, lookupForm, refundForm.getRefundDocumentVO() );
    }
    
    public void onPressLinkSuratRekapKeAccFinance( HttpServletRequest request, Object command, Properties props )
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressLinkSuratRekap" );
    	User user = (User) request.getSession().getAttribute("currentUser");   
    	RefundForm refundForm = ( RefundForm ) command;
        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
        lookupHelper.generateDocumentRekapKeAccFinance( command );
        doDownload( request, lookupForm, refundForm.getRefundDocumentVO() );
        sendEmailCreateRekap(request, refundForm.getRefundDocumentVO(), refundBusiness.genCreatRekapFileName( user.getName(), lookupForm.getTglKirimDokFisikFrom(), lookupForm.getTglKirimDokFisikTo() ), props, null, null, lookupForm);
    }
    
    public void onPressImageSuratRedempt( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressImageSuratRedempt" );

        RefundForm refundForm = ( RefundForm ) command;
        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
        editHelper.onPressImageEdit( request, command, lookupForm.getSelectedRowCd() );
        redemptHelper.generateDocumentRedempt( command );

        doDownload( request, lookupForm, refundForm.getRefundDocumentVO() );
    }

    public void onPressButtonViewAttachmentLamp1( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressButtonViewAttachmentLamp1" );

        RefundForm refundForm = ( RefundForm ) command;
        RefundPreviewLamp1Form lamp1Form = ( ( RefundForm ) command ).getLamp1Form();
        doDownload( request, lamp1Form, refundForm.getRefundDocumentVO() );
    }

    public void onPressButtonViewAttachmentLamp3( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressButtonViewAttachmentLamp3" );

        RefundForm refundForm = ( RefundForm ) command;
        RefundPreviewLamp3Form lamp3Form = ( ( RefundForm ) command ).getLamp3Form();
        doDownload( request, lamp3Form, refundForm.getRefundDocumentVO() );
    }

    public void onPressButtonViewAttachmentRedempt( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressButtonViewAttachmentRedempt" );

        RefundForm refundForm = ( RefundForm ) command;
        RefundPreviewInstruksiRedemptForm redemptForm = ( ( RefundForm ) command ).getRedemptForm();
        doDownload( request, redemptForm, refundForm.getRefundDocumentVO() );
    }

    private String sendEmailCreateRekap( HttpServletRequest request, RefundDocumentVO documentVO, String fileName, Properties props, String spajNo, String tindakan,  RefundLookupForm lookupForm )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.sendEmailCreateRekap" );
               
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        String dirName = props.getProperty("upload.dir.refund");
        String sender = props.getProperty("admin.ajsjava");
        
        String tempEmailTujuan = props.getProperty( "email_create_rekap.email_tujuan" );      // email tujuan
        String[] emailTujuan = tempEmailTujuan.split(";");
       
        String subject = "[INFO] Telah dibuat surat rekap pembatalan dr tanggal " + FormatDate.toIndonesian( lookupForm.getTglKirimDokFisikFrom() ) +" s/d "+FormatDate.toIndonesian( lookupForm.getTglKirimDokFisikTo() );

        String content =
                "Informasi : Telah dibuat surat rekap pembatalan dr tanggal "
                + FormatDate.toIndonesian( lookupForm.getTglKirimDokFisikFrom() ) +" s/d "+ FormatDate.toIndonesian( lookupForm.getTglKirimDokFisikTo() ) 
                +". User :"+ currentUser.getName();
        
        return refundBusiness.getCommonBusiness().sendEmail( currentUser, documentVO, dirName, fileName, sender, emailTujuan, null, null, subject, content, false );
    }
    
    private String sendEmailAfterCancel( String kategoriUW, HttpServletRequest request, RefundDocumentVO documentVO, String fileName, Properties props, String spajNo, String tindakan )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.sendEmailAfterCancel" );
        
        String tindakanLabel = null;
        if( "TINDAKAN_GANTI_PLAN".equals( tindakan ) )
        {
        	tindakanLabel = "Tindakan Ganti Plan";
        }
        else if( "TINDAKAN_GANTI_TERTANGGUNG".equals( tindakan ) )
        {
        	tindakanLabel = "Tindakan Ganti Tertanggung";
        }
        else if( "TINDAKAN_REFUND_PREMI".equals( tindakan ) )
        {
        	tindakanLabel = "Tindakan Refund Premi";
        }
        else if( "TINDAKAN_TIDAK_ADA".equals( tindakan ) )
        {
        	tindakanLabel = "Tindakan Tidak Ada";
        }
        
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        String dirName = props.getProperty("upload.dir.refund");
        String sender = props.getProperty("admin.ajsjava");
        
        String tempEmailTujuan;      // email tujuan
        String tempCc; // cc
        String tempBcc;  // bcc
        String content;

        String spajPolis = refundBusiness.getCommonBusiness().spajNoAndPolicyNo( spajNo );

        String subject = "[INFO] Telah dilakukan pembatalan SPAJ/POLIS no " + spajPolis;
        
        if( "TINDAKAN_TIDAK_ADA".equals( tindakan ) ){
        	tempEmailTujuan = props.getProperty( "email_after_cancel_tindakan_tidak_ada.email_tujuan" );      // email tujuan
            tempCc = props.getProperty( "email_after_cancel_tindakan_tidak_ada.cc" ); // cc
            tempBcc = props.getProperty( "email_after_cancel_tindakan_tidak_ada.bcc" );  // bcc
            content =     
            	"Informasi : Telah dilakukan pembatalan SPAJ/POLIS no " + spajPolis + ". \n\n"+
	            "Kategori Tindakan : " + tindakanLabel +". Terima kasih. "
	            +"User Input :"+ currentUser.getName();
            
        	ArrayList<SetoranPremiDbVO> setoranPremiBySpaj = refundBusiness.selectSetoranPremiBySpaj( spajNo );
        	if( setoranPremiBySpaj == null || setoranPremiBySpaj.size() == 0 ){
        		fileName = null; // Diset null agar tidak ada file attachment
        	}
        	
        }else{
	        tempEmailTujuan = props.getProperty( "email_after_cancel.email_tujuan" );      // email tujuan
	        tempCc = props.getProperty( "email_after_cancel.cc" ); // cc
	        tempBcc = props.getProperty( "email_after_cancel.bcc" );  // bcc
	        content =
	            "Informasi : Telah dilakukan pembatalan SPAJ/POLIS no " + spajPolis + ". \n\n"+
	            "Harap dilakukan pengecekan terhadap data terkait pembayaran seperti PRE, VOUCHER, dan JURNAL MEMORIAL. "+"Kategori Tindakan : " + tindakanLabel +". Terima kasih. "
	            +"User Input :"+ currentUser.getName();
        }

        //Yusuf (19/09/2011) Request Inge via Email
        if("DMTM".equals(kategoriUW)){
            tempCc = props.getProperty("uw.dmtm");
        }else if("BANCASS1".equals(kategoriUW)){
        	tempCc = props.getProperty("uw.bancass1");
        }else if("BANCASS2".equals(kategoriUW)){
        	tempCc = props.getProperty("uw.bancass2");
        }else if("AGENCYWORKSITE".equals(kategoriUW)){
        	tempCc = props.getProperty("uw.agencyworksite");
        }
        
        String[] emailTujuan = tempEmailTujuan.split(";");
        String[] cc = tempCc.split(";");
        String[] bcc = tempBcc.split(";");

        return refundBusiness.getCommonBusiness().sendEmail( currentUser, documentVO, dirName, fileName, sender, emailTujuan, cc, bcc, subject, content, false );
    }

    private String sendEmailAfterKirimRedempt( HttpServletRequest request, RefundDocumentVO documentVO, String fileName, Properties props, String spajNo )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.sendEmailAfterKirimRedempt" );

        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        String dirName = props.getProperty("upload.dir.refund");
        String sender = props.getProperty("admin.ajsjava");

        String tempEmailTujuan = props.getProperty("email_kirim_redempt.email_tujuan");      
        String[] emailTujuan = tempEmailTujuan.split(";");// email tujuan

        String tempCc = props.getProperty( "email_kirim_redempt.cc" ); 
        String[] cc = tempCc.split(";"); // cc

        String tempBcc = props.getProperty( "email_kirim_redempt.bcc" );  
        String[] bcc = tempBcc.split(";"); // bcc
        
        refundBusiness.getCommonBusiness().insertMstPositionSpaj(currentUser.getLus_id(), "(PROSES PEMBATALAN POLIS) PERMINTAAN REDEEM UNIT", spajNo, 0);
        
        String spajPolis = refundBusiness.getCommonBusiness().spajNoAndPolicyNo( spajNo );

        String subject = "[INFO] Telah dilakukan pengiriman surat redempt untuk SPAJ/POLIS no " + spajPolis;

        String content = "Informasi : Telah dilakukan pengiriman surat redempt untuk SPAJ/POLIS no " + spajPolis + ". \n\n"+
                         "Harap dilakukan pengecekan terhadap data terkait. Terima kasih. "+ "User Input :" + currentUser.getName();

        return refundBusiness.getCommonBusiness().sendEmail( currentUser, documentVO, dirName, fileName, sender, emailTujuan, cc, bcc, subject, content, false );
    }

    private String sendEmailAfterUpdatePayment( HttpServletRequest request, Properties props, String spajNo )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.sendEmailAfterUpdatePayment" );

        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        String sender = props.getProperty("admin.ajsjava");

        String tempEmailTujuan = props.getProperty( "email_update_payment.email_tujuan" );      // email tujuan
        String[] emailTujuan = tempEmailTujuan.split(";");

        String tempCc = props.getProperty( "email_update_payment.cc" ); // cc
        String[] cc = tempCc.split(";");

        String tempBcc = props.getProperty( "email_update_payment.bcc" );  // bcc
        String[] bcc = tempBcc.split(";");
        
        String spajPolis = refundBusiness.getCommonBusiness().spajNoAndPolicyNo( spajNo );

        String subject = "[INFO] Telah dilakukan pembayaran refund untuk SPAJ/POLIS no " + spajPolis;

        String content = "Informasi : Telah dilakukan pembayaran refund untuk SPAJ/POLIS no " + spajPolis + ". \n\n"+
                         "Harap dilakukan pengecekan terhadap data terkait. Terima kasih.";
        
        return refundBusiness.getCommonBusiness().sendEmail( currentUser, null, null, null, sender, emailTujuan, cc, bcc, subject, content, false );
    }

    private String sendEmailPermintaanAccBatal( HttpServletRequest request, Properties props, RefundEditForm editForm )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.sendEmailPermintaanAccBatal" );

        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        PolicyInfoVO policyInfoVO = refundBusiness.selectPolicyInfoBySpaj( editForm.getSpaj() );
        
        String spajPolis = refundBusiness.getCommonBusiness().spajNoAndPolicyNo( editForm.getSpaj() );
        String sender = props.getProperty("admin.ajsjava");
        String subject = "[INFO] Permohonan Akseptasi Pembatalan Polis untuk SPAJ/POLIS No " + spajPolis +
        				 " atas nama " + policyInfoVO.getNamaPp();
        String tempTo = "", tempCc = "", tempBcc = "", content = "", sub_section = "";
        Boolean flag_email = false;
        
    	if(RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() ) || RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() )){
    		flag_email = true;
    		sub_section = "TINDAKAN_GANTI_PLAN_TERTANGGUNG";
    		
    		content = "Mohon bantuannya untuk proses pembatalan SPAJ dengan No. " + editForm.getSpaj() + " atas nama " + policyInfoVO.getNamaPp() + 
    				  " dengan tindakan ganti plan / ganti tertanggung.\n" +
    				  "Ibank akan digunakan kembali oleh SPAJ No. " + editForm.getSpajBaru() + " atas nama " + editForm.getPPBaru();
    		
    	}else{
    		sub_section = "TINDAKAN_REFUND";
    		
    		content = "Informasi : Telah dilakukan permintaan batal & refund untuk SPAJ/POLIS no " + spajPolis + ". \n\n"+
            		  "Harap dilakukan pengecekan terhadap data terkait. Terima kasih.";
    	}
        
		HashMap mapEmail = refundBusiness.getCommonBusiness().selectMstConfig(6, "sendEmailPermintaanAccBatal", sub_section);
		String emailto = mapEmail.get("NAME")!=null? mapEmail.get("NAME").toString():"";
		String emailcc = mapEmail.get("NAME2")!=null? mapEmail.get("NAME2").toString():"";
		String emailbcc = mapEmail.get("NAME3")!=null? mapEmail.get("NAME3").toString():"";

        tempTo = emailto;
        tempCc = currentUser.getEmail() + ";" + emailcc;
        tempBcc = emailbcc;
		
    	String[] to = tempTo.split(";");
        String[] cc = tempCc.split(";");
    	String[] bcc = tempBcc.split(";");
        
        refundBusiness.getCommonBusiness().insertMstPositionSpaj(currentUser.getLus_id(), "(PROSES PEMBATALAN POLIS) PERMINTAAN AKSEPTASI", editForm.getSpaj(), 0);
        
        return refundBusiness.getCommonBusiness().sendEmail( currentUser, null, null, null, sender, to, cc, bcc, subject, content, flag_email );
    }
    
    private String sendEmailAfterCancelKeBC( HttpServletRequest request, Properties props, RefundEditForm editForm )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.sendEmailPermintaanAccBatal" );

        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        PolicyInfoVO policyInfoVO = refundBusiness.selectPolicyInfoBySpaj( editForm.getSpaj() );
        
        String sender = props.getProperty("admin.ajsjava");
        String subject = "[INFO] Pemberitahuan Penggantian SPAJ No. " + editForm.getSpaj() +
        				 " atas nama " + policyInfoVO.getNamaPp();
        String tempTo = "", tempCc = "", tempBcc = "", content = "";
        Boolean flag_email = false;
        
    	content = "Dengan ini kami sampaikan informasi terkait proses pembatalan SPAJ dengan No. " + editForm.getSpaj() +
    			  " atas nama " + policyInfoVO.getNamaPp() + ".\n" +
				  "SPAJ telah diinput ulang dengan No. " + editForm.getSpajBaru() + " atas nama " + editForm.getPPBaru() + ".";
        
		HashMap mapEmail = refundBusiness.getCommonBusiness().selectEmailBCAM2(editForm.getSpaj());
		
		if(mapEmail!=null){
			String emailto = mapEmail.get("EMAIL_BC")!=null? mapEmail.get("EMAIL_BC").toString():"";
			String emailcc = mapEmail.get("EMAIL_AM")!=null? mapEmail.get("EMAIL_AM").toString():"";
			String emailbcc = "";
	
	        tempTo = emailto;
	        tempCc = currentUser.getEmail() + ";" + emailcc;
	        tempBcc = emailbcc;
			
	    	String[] to = tempTo.split(";");
	        String[] cc = tempCc.split(";");
	    	String[] bcc = tempBcc.split(";");
	    	
	        return refundBusiness.getCommonBusiness().sendEmail( currentUser, null, null, null, sender, to, cc, bcc, subject, content, flag_email );
		}else{
			return null;
		}
	}
    
    /**
     * Function ini untuk proses special (spt validasi sebelum pembatalan) pada product
     * @param editForm: editForm
     * @return RefundProductSpecInterface
     */
    private RefundProductSpecInterface generateProduct( RefundEditForm editForm )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.generateProduct" );

        RefundProductSpecInterface product = null;

        PolicyInfoVO policyInfoVO = editForm.getPolicyInfoVO();
        if( policyInfoVO != null )
        {
            int lsbsId = editForm.getPolicyInfoVO().getLsbsId() == null? 0 : editForm.getPolicyInfoVO().getLsbsId();

            switch( lsbsId )
            {
                case 86:
                case 94:
                case 123:    
                case 124:
                case 142:
                case 143:
                case 144:
                case 158:
                case 175:
                case 176:
                    product = new RefundProductPowerSave( refundBusiness.getElionsManager() ); break;
            }
        }

        return product;
    }
    
    public void onPressButtonViewAttachmentLampTanpaTindakan( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressButtonViewAttachmentLamp3" );

        RefundForm refundForm = ( RefundForm ) command;
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        doDownload( request, editForm, refundForm.getRefundDocumentVO() );
    }
    
    public void fillBlankForm( Object command, String selectedRowSpaj )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.fillBlankForm" );

        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        
        RefundDbVO refundDbVO = refundBusiness.selectRefundByCd( selectedRowSpaj );
        String createWhoFullName = refundBusiness.selectLusFullName(refundDbVO.getCreateWho());
        String updateWhoFullName = refundBusiness.selectLusFullName(refundDbVO.getUpdateWho());
        String cancelWhoFullName = refundBusiness.selectLusFullName(refundDbVO.getCancelWho());
        if( editForm.getPosisiNo() == null || editForm.getPosisiNo() == 0 )
        {
        	editForm.setUpdateWhoFullName( editForm.getCurrentUser() );
        }
        else
        {
            if(updateWhoFullName!=null && !"".equals(updateWhoFullName.trim()))
            {
            	editForm.setUpdateWhoFullName(updateWhoFullName);
            }
            else
            {
            	editForm.setUpdateWhoFullName(createWhoFullName);
            }
        }

        editForm.setPosisiNo( refundDbVO.getPosisiNo() );

        editForm.setSpaj( refundDbVO.getSpajNo() );
        editForm.setAlasanCd( refundDbVO.getAlasanCd() );

        String alasan = editForm.getAlasanCd().equals( RefundConst.ALASAN_LAIN )?
                            refundDbVO.getAlasanLain() : MappingUtil.getLabel( editForm.getAlasanList(), editForm.getAlasanCd() );
        editForm.setAlasan( alasan );
        editForm.setAtasNama( refundDbVO.getKliNama() );
        editForm.setCabangBank( refundDbVO.getKliCabangBank() );
        editForm.setKotaBank( refundDbVO.getKliKotaBank() );
        editForm.setNamaBank( refundDbVO.getKliNamaBank() );
        editForm.setNorek( refundDbVO.getKliNorek() );
        editForm.setSpajBaru( refundDbVO.getSpajBaruNo() );
        editForm.setTindakanCd( refundDbVO.getTindakanCd() );
        editForm.setHasUnitFlag( refundDbVO.getHasUnitFlag() );

        // biaya2
        HashMap biayaMap = refundBusiness.retrieveBiaya2( refundDbVO.getSpajNo() );
        BigDecimal biayaAdmin = ( BigDecimal ) biayaMap.get( "biayaAdmin" );
        BigDecimal biayaMedis = ( BigDecimal ) biayaMap.get( "biayaMedis" );
        BigDecimal biayaLain = ( BigDecimal ) biayaMap.get( "biayaLain" );
        String biayaLainDescr = ( String ) biayaMap.get( "biayaLainDescr" );

        editForm.setBiayaAdmin( biayaAdmin );
        editForm.setBiayaMedis( biayaMedis );
        editForm.setBiayaLain( biayaLain );
        editForm.setBiayaLainDescr( biayaLainDescr );
        editForm.setPenarikanUlinkVOList( refundBusiness.selectPenarikanUlinkVOListBySpaj( editForm.getSpaj().trim() ) );

        // kalau belum di bikin surat redempt, maka ga bisa isi jumlah redemptionnya
        String biayaStatus;
        if( refundDbVO.getPosisiNo() < 2 )
        	biayaStatus = CommonConst.DISABLED_TRUE;
        else
            biayaStatus = CommonConst.DISABLED_FALSE;

        editForm.setBiayaIsDisabled( biayaStatus );

        editForm.setPayment( refundDbVO.getPayment() );
        editForm.setPaymentDate( refundDbVO.getPaymentDate() );
        editForm.setCreateWhen( refundDbVO.getCreateWhen() );
        editForm.setUpdateWhen( refundDbVO.getUpdateWhen() );
        editForm.setCreateWho( refundDbVO.getCreateWho() );
        editForm.setUpdateWho( refundDbVO.getUpdateWho() );
        editForm.setCancelWho( refundDbVO.getCancelWho() );
        editForm.setCancelWhen( refundDbVO.getCancelWhen() );
    }
    
    private String stdCurrency( String lkuId, BigDecimal value )
    {
        String result;
        if(RefundConst.KURS_RUPIAH.equals(lkuId))
        {
        	lkuId = "Rp.";
        }
        else if(RefundConst.KURS_DOLLAR.equals(lkuId))
        {
        	lkuId = "$";
        }
        if (value == null){
			result = "-";
		}else{
			result = (lkuId != null ? lkuId : "") + new DecimalFormat("#,##0.00").format( value.abs() );
		}

        if( result != null && value != null && value.compareTo( BigDecimal.ZERO ) < 0 )
        {
            result = "( ".concat( result ).concat( " )" );
        }
        return result;
    }
    
	public void setlampiranList( Object command, String theEvent, int page )
	{
  		if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.setlampiranList" );
  		RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
  		RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
	    ArrayList <LampiranListVO> lampiranList = new ArrayList<LampiranListVO>();
	   
	    String lkuId = "";
	    String spaj = null;
	    String newSpaj = null;
	    String tindakan = null;
	    SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
	    if( "onChangeTindakanCd".equals( theEvent ) ) { editForm.setSpajHelpForAddLamp(null); editForm.setSpajNewHelpAddLamp(null);}
	    String spajHelp = editForm.getSpajHelpForAddLamp();
	    String spajNewHelp = editForm.getSpajNewHelpAddLamp();
	    if( "onPressImageSuratBatal".equals( theEvent ) || "onPressImageSuratRedempt".equals( theEvent ) )
	    {
	    	spaj = lookupForm.getSelectedRowCd();
	    	newSpaj = lookupForm.getSelectedRowNewSpaj();
	    	tindakan = lookupForm.getSelectedRowTindakan();
	    	fillBlankForm(command, spaj);
	    }
	    else
	    {
	    	spaj = editForm.getSpaj();
	    	newSpaj = editForm.getSpajBaru();
	    	if(editForm.getTindakanCd()!=null)
	    	{
	    		tindakan = editForm.getTindakanCd().toString();
	    	}
	    }
	    
	    if("onPressImageEdit".equals(theEvent) )
	    {
		    if( editForm.getLampiranAddList() != null && editForm.getLampiranAddList().size() > 0)
			{
		    	if( editForm.getLampiranAddList()!=null && editForm.getLampiranAddList().size() > 0 )
		    	{
			          editForm.getLampiranAddList().clear();
			    	  editForm.setLampiranAddList(null);
		    	}
		    	editForm.setPremiDikembalikan(null);
			}
	    }
	    if( spajHelp == null) { spajHelp = ""; }
	    if( spaj !=null && !"".equals(spaj) && CommonConst.DISABLED_TRUE.equals( editForm.getAddLampiranDisabled() ) )
	    {
		    RefundDbVO selectMstRefund = refundBusiness.selectRefundByCd(spaj);
	        ArrayList<LampiranListVO> lampiranTambahan = refundBusiness.selectMstDetRefundLamp( spaj );
	        if( lampiranTambahan != null && lampiranTambahan.size()>0 )
	        {
	        	editForm.setLampiranAddList( lampiranTambahan );
	        }
		    //TODO
		    if( selectMstRefund!=null ) // jika sdh di save di mst_det_refund
	        {
	        	//TODO special
	        	if( RefundConst.TINDAKAN_GANTI_PLAN.equals(editForm.getTindakanCd())
	        	   || RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals(editForm.getTindakanCd())
	        	   || editForm.getPosisiNo()!=null && RefundConst.TINDAKAN_TIDAK_ADA.equals(editForm.getTindakanCd()) && editForm.getPosisiNo() >= RefundConst.POSISI_TIDAK_ADA_CANCEL
	        	   || RefundConst.TINDAKAN_REFUND_PREMI.equals(editForm.getTindakanCd()))
	        			
	        	{
	        		if(RefundConst.TINDAKAN_REFUND_PREMI.equals(editForm.getTindakanCd()))
	        		{
	        			if(refundBusiness.isUnitLink(spaj))
	        			{
	        				if(editForm.getPosisiNo()!=null && editForm.getPosisiNo() >= RefundConst.POSISI_REFUND_ULINK_REDEMPT_SENT)
	        				{
	                		  	if( editForm.getLampiranAddList() != null && editForm.getLampiranAddList().size()>0 )
	                		  	{
	                		  		for( int i = 0 ; i < editForm.getLampiranAddList().size() ; i ++ )
	                		  		{
	                		  			editForm.getLampiranAddList().get(i).setIsDisabled( CommonConst.DISABLED_TRUE );
	                		  		}
	                		  	}
	        				}
	        			}
	        			else
	        			{
	        				if(editForm.getPosisiNo()!=null && editForm.getPosisiNo() >= RefundConst.POSISI_REFUND_NON_ULINK_CANCEL)
	        				{
	                		  	if( editForm.getLampiranAddList() != null && editForm.getLampiranAddList().size()>0 )
	                		  	{
	                		  		for( int i = 0 ; i < editForm.getLampiranAddList().size() ; i ++ )
	                		  		{
	                		  			editForm.getLampiranAddList().get(i).setIsDisabled( CommonConst.DISABLED_TRUE );
	                		  		}
	                		  	}
	        				}
	        			}
	        		}
	        		else if( RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() ) || RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() ))
	        		{
	        			if( refundBusiness.isUnitLink( spaj ) ) // ulink
	        			{
		        			if( editForm.getHasUnitFlag() != null && RefundConst.UNIT_FLAG.equals( editForm.getHasUnitFlag() ) )// sudah fund
		        			{
		        				if( editForm.getPosisiNo() != null && editForm.getPosisiNo() >= RefundConst.POSISI_GANTI_TERTANGGUNG_ULINK_CANCEL )
		        				{
			        				if(editForm.getPosisiNo()!=null && editForm.getPosisiNo() >= RefundConst.POSISI_REFUND_NON_ULINK_CANCEL)
			        				{
			                		  	if( editForm.getLampiranAddList() != null && editForm.getLampiranAddList().size()>0 )
			                		  	{
			                		  		for( int i = 0 ; i < editForm.getLampiranAddList().size() ; i ++ )
			                		  		{
			                		  			editForm.getLampiranAddList().get(i).setIsDisabled( CommonConst.DISABLED_TRUE );
			                		  		}
			                		  	}
			        				}
		        				}
		        			}
		        			else// blm fund
		        			{
		        				if( editForm.getPosisiNo() != null && editForm.getPosisiNo() >= RefundConst.POSISI_GANTI_TERTANGGUNG_NON_ULINK_CANCEL )
		        				{
			        				if(editForm.getPosisiNo()!=null && editForm.getPosisiNo() >= RefundConst.POSISI_REFUND_NON_ULINK_CANCEL)
			        				{
			                		  	if( editForm.getLampiranAddList() != null && editForm.getLampiranAddList().size()>0 )
			                		  	{
			                		  		for( int i = 0 ; i < editForm.getLampiranAddList().size() ; i ++ )
			                		  		{
			                		  			editForm.getLampiranAddList().get(i).setIsDisabled( CommonConst.DISABLED_TRUE );
			                		  		}
			                		  	}
			        				}
		        				}
		        			}
	        			}
	        			else// non link
	        			{
	        				if( editForm.getPosisiNo() != null && editForm.getPosisiNo() >= RefundConst.POSISI_GANTI_TERTANGGUNG_NON_ULINK_CANCEL )
	        				{
		        				if(editForm.getPosisiNo()!=null && editForm.getPosisiNo() >= RefundConst.POSISI_REFUND_NON_ULINK_CANCEL)
		        				{
		                		  	if( editForm.getLampiranAddList() != null && editForm.getLampiranAddList().size()>0 )
		                		  	{
		                		  		for( int i = 0 ; i < editForm.getLampiranAddList().size() ; i ++ )
		                		  		{
		                		  			editForm.getLampiranAddList().get(i).setIsDisabled( CommonConst.DISABLED_TRUE );
		                		  		}
		                		  	}
		        				}
	        				}
	        			}
	        		}
	        		else if( RefundConst.TINDAKAN_TIDAK_ADA.equals( editForm.getTindakanCd() ) )
	        		{
            		  	if( editForm.getLampiranAddList() != null && editForm.getLampiranAddList().size()>0 )
            		  	{
            		  		for( int i = 0 ; i < editForm.getLampiranAddList().size() ; i ++ )
            		  		{
            		  			editForm.getLampiranAddList().get(i).setIsDisabled( CommonConst.DISABLED_TRUE );
            		  		}
            		  	}
	        		}
	        	}
	        }
		    else
		    {
     		  	if( editForm.getLampiranAddList() != null && editForm.getLampiranAddList().size()>0 )
    		  	{
    		  		for( int i = 0 ; i < editForm.getLampiranAddList().size() ; i ++ )
    		  		{
    		  			editForm.getLampiranAddList().get(i).setIsDisabled( CommonConst.DISABLED_FALSE );
    		  		}
    		  	}
		    }
	    }
	    else if( spaj !=null && !"".equals(spaj) && tindakan != null && !"".equals(tindakan) && LazyConverter.toInt( tindakan ) != RefundConst.TINDAKAN_GANTI_PLAN 
	    		&& LazyConverter.toInt( tindakan ) != RefundConst.TINDAKAN_GANTI_TERTANGGUNG && !spajHelp.equals(spaj) || 
	    	spaj !=null && !"".equals(spaj) && tindakan != null && !"".equals(tindakan) && LazyConverter.toInt( tindakan ) == RefundConst.TINDAKAN_GANTI_PLAN
	    		&& !spajHelp.equals(spaj) ||
		    spaj !=null && !"".equals(spaj) && tindakan != null && !"".equals(tindakan) && LazyConverter.toInt( tindakan ) == RefundConst.TINDAKAN_GANTI_PLAN 
	    		&& !newSpaj.equals(spajNewHelp)||	    		
		    spaj !=null && !"".equals(spaj) && tindakan != null && !"".equals(tindakan) && LazyConverter.toInt( tindakan ) == RefundConst.TINDAKAN_GANTI_TERTANGGUNG 
	    		&& !spajHelp.equals(spaj) ||
			spaj !=null && !"".equals(spaj) && tindakan != null && !"".equals(tindakan) && LazyConverter.toInt( tindakan ) == RefundConst.TINDAKAN_GANTI_TERTANGGUNG 
	    		&& !newSpaj.equals(spajNewHelp) )
	    {
	    	RefundDbVO selectMstRefundBySpaj = refundBusiness.selectRefundByCd( spaj );
	    	CheckSpajParamsVO selectMstRefundCancel = refundBusiness.selectSpajAlreadyCancelMstRefund( spaj ) ;
	    	if( selectMstRefundBySpaj != null && "".equals( selectMstRefundBySpaj ) ){ selectMstRefundBySpaj = null; }
	    	if( selectMstRefundCancel != null && "".equals( selectMstRefundCancel ) ) { selectMstRefundCancel = null;}
	    	
		    	ArrayList<SetoranPremiDbVO> setoranPremiDbVOList = refundBusiness.selectPenarikanUlinkSortedByMsdpNumber( spaj );
		    	String polisNo = "";
		    	if(RefundConst.TINDAKAN_REFUND_PREMI.toString().equals(tindakan))
		    	{
		    		PolicyInfoVO policyInfoVO = refundBusiness.getInformationForLampiran(spaj);
		    		if(policyInfoVO != null)    
		    		{
		    			if( policyInfoVO.getPolicyNo() == null || policyInfoVO.getPolicyNo().trim().equals( "" ) )
		    			{
		    				if( policyInfoVO.getNamaPp() != null && !policyInfoVO.getNamaPp().trim().equals( "" ) )
		    				{
		    					polisNo = "- ";
		    				}
		    				else
		    				{
		    					polisNo = "";
		    				}
		    			}
		    			else
		    			{
		    				polisNo = StringUtil.nomorPolis( policyInfoVO.getPolicyNo() );
					        }
		    			lampiranList.add(new LampiranListVO("1","Surat Permohonan Pembatalan", CommonConst.DISABLED_FALSE));
		    			if( setoranPremiDbVOList != null && setoranPremiDbVOList.size() > 0 )
		    			{
		    				lampiranList.add(new LampiranListVO("2","bukti setoran tanggal " + formatDate.format( setoranPremiDbVOList.get(0).getTglSetor()) + " dengan nominal sebesar " + stdCurrency(lkuId, setoranPremiDbVOList.get(0).getJumlah()), CommonConst.DISABLED_FALSE ));
		    				for( int i = 1 ; i <  setoranPremiDbVOList.size() ; i ++  )
		    				{
		    					lampiranList.add(new LampiranListVO( (i + 9) + "","bukti setoran tanggal " +formatDate.format( setoranPremiDbVOList.get(i).getTglSetor()) + " dengan nominal sebesar " + stdCurrency(lkuId, setoranPremiDbVOList.get(i).getJumlah()), CommonConst.DISABLED_FALSE ));
		    				}
		    			}
		    			lampiranList.add(new LampiranListVO("3","Polis Asli dengan nomor polis " + polisNo + " atas nama Pemegang Polis/Tertanggung:" + StringUtil.camelHumpAndTrim( policyInfoVO.getNamaPp() ) + " / " + StringUtil.camelHumpAndTrim( policyInfoVO.getNamaTt() ), CommonConst.DISABLED_FALSE));
		    		}
		    	}
		    	else if(RefundConst.TINDAKAN_GANTI_PLAN.toString().equals(tindakan) 
		    			|| RefundConst.TINDAKAN_GANTI_TERTANGGUNG.toString().equals(tindakan) )
		    	{
		    		if( newSpaj != null && !"".equals(newSpaj) )
		    		{
		    			PolicyInfoVO oldPolicyInfoVO =  refundBusiness.getInformationForLampiran( spaj );
//		    			PolicyInfoVO newPolicyInfoVO = refundBusiness.getNewInformationForLampiran( newSpaj );
		    			newSpaj = newSpaj.replace(" ", "");
		    			String[] spajList = newSpaj.split(",");
		    			ArrayList<PolicyInfoVO> newPolicyInfoVOList = refundBusiness.selectPolicyInfoBySpajList(spajList);
		    			if( oldPolicyInfoVO.getPolicyNo() == null || oldPolicyInfoVO.getPolicyNo().trim().equals( "" ) )
		    			{
		    				if( oldPolicyInfoVO.getNamaPp() != null && !oldPolicyInfoVO.getNamaPp().trim().equals( "" ) )
		    				{
		    					polisNo = "- ";
		    				}
		    				else
		    				{
		    					polisNo = "";
		    				}
		    			}
		    			else
		    			{
		    				polisNo = StringUtil.nomorPolis( oldPolicyInfoVO.getPolicyNo() );
		    			}
		    			if( oldPolicyInfoVO != null && newPolicyInfoVOList != null && newPolicyInfoVOList.size() > 0 )
		    			{
		    				lampiranList.add(new LampiranListVO("1","No. SPAJ: " + StringUtil.nomorSPAJ( spaj ) + "/ " 
		    						+ "No. Polis: " + StringUtil.nomorPolis( polisNo ) + " atas nama Pemegang Polis/Tertanggung: "
		    						+ oldPolicyInfoVO.getNamaPp() + " / " + oldPolicyInfoVO.getNamaTt(), CommonConst.DISABLED_FALSE));
		    				for( int i = 0 ; i < newPolicyInfoVOList.size() ; i ++ )
		    				{
			    				lampiranList.add(new LampiranListVO((i+2)+"","SPAJ: " + StringUtil.nomorSPAJ( newSpaj ) + " Pemegang Polis/Tertanggung: "
			    						+ newPolicyInfoVOList.get(i).getNamaPp() + " / " + newPolicyInfoVOList.get(i).getNamaTt(), CommonConst.DISABLED_FALSE ));
		    				}
		    			}
		    		}
		    	}
		    	else if( RefundConst.TINDAKAN_TIDAK_ADA.toString().equals(tindakan) )
		    	{
		    		PolicyInfoVO oldPolicyInfoVO =  refundBusiness.getInformationForLampiran( spaj );
		    		if( oldPolicyInfoVO.getPolicyNo() == null || oldPolicyInfoVO.getPolicyNo().trim().equals( "" ) )
		    		{
		    			if( oldPolicyInfoVO.getNamaPp() != null && !oldPolicyInfoVO.getNamaPp().trim().equals( "" ) )
		    			{
		    				polisNo = "- ";
		    			}
		    			else
		    			{
		    				polisNo = "";
		    			}
		    		}
		    		else
		    		{
		    			polisNo = StringUtil.nomorPolis( oldPolicyInfoVO.getPolicyNo() );
		    		}
		    		if( oldPolicyInfoVO != null )
		    		{
		    			lampiranList.add(new LampiranListVO("1","No. SPAJ: " + StringUtil.nomorSPAJ( spaj ) + "/ " 
		    					+ "No. Polis: " + StringUtil.nomorPolis( polisNo ) + " atas nama Pemegang Polis/Tertanggung: "
		    					+ oldPolicyInfoVO.getNamaPp() + " / " + oldPolicyInfoVO.getNamaTt(), CommonConst.DISABLED_FALSE));
		    		}
		    	}
		    	editForm.setLampiranAddList(lampiranList);
		    	editForm.setSpajHelpForAddLamp(spaj);
		    	if( tindakan != null && !"".equals( tindakan ) && LazyConverter.toInt( tindakan ) == RefundConst.TINDAKAN_GANTI_PLAN|| 
		    			tindakan != null && !"".equals( tindakan ) && LazyConverter.toInt( tindakan ) == RefundConst.TINDAKAN_GANTI_TERTANGGUNG )
		    	{
		    		editForm.setSpajNewHelpAddLamp( editForm.getSpajBaru() );
		    	}
	    }
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
		if( totalPremiDikembalikan != null ){
			paramsVO.setTotalPremiDikembalikan( totalPremiDikembalikan );	
		}else{
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
		if( refundBusiness.isUnitLink(editForm.getSpaj()) && editForm.getPenarikanUlinkVOList() != null && "sudah".equals(editForm.getStatusUnit()) ){
			if(editForm.getTempDescrDanJumlah()!=null && editForm.getTempDescrDanJumlah().size() > 0){
				for( int i = 0 ; i < editForm.getTempDescrDanJumlah().size() ; i++ ){
		    		String ljbIdStr = editForm.getTempDescrDanJumlah().get(i).getLjbId();
		    		Integer ljbId = null;
		    		if( ljbIdStr == null || "".equals(ljbIdStr)){
		    			ljbId = null;
		    		}else{
		    			ljbId = LazyConverter.toInt(ljbIdStr);
		    		}
		    		if("yes".equals(editForm.getTempDescrDanJumlah().get(i).getSetoranOrNot())){
		        		setoranPremiDbVOList.add( new SetoranPremiDbVO( editForm.getTempDescrDanJumlah().get(i).getTitipanKe(),editForm.getTempDescrDanJumlah().get(i).getTglSetor(),editForm.getTempDescrDanJumlah().get(i).getJumlahPremi(),editForm.getTempDescrDanJumlah().get(i).getLkuId(), null, editForm.getTempDescrDanJumlah().get(i).getDescr(), editForm.getTempDescrDanJumlah().get(i).getNoPre(), editForm.getTempDescrDanJumlah().get(i).getNoVoucher() ) );
		        	}else{
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
			
		}else{
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
		if( editForm.getPayment() == null || editForm.getPaymentDate() == null ){
			request.setAttribute( "pageMessage", "Mohon melengkapi data pembayaran di halaman sebelumnya".toString() );
			result = false;
		}else{
			HashMap<String, Object> resultMap = refundBusiness.deleteThenInsertRefund( paramsVO, "Update payment", editForm, theEvent, editForm.getHasUnitFlag(), editForm.getTindakanCd() );
			request.setAttribute( "pageMessage", resultMap.get( "pageMessage" ) );
		
			result = ( Boolean ) resultMap.get( "succeed" );
			if( result ){
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
  
	/**
	* 
	* @param request
	* @param daftarSpajBatal
	* @param command
	* @return List<String> spajGagalBatal
	*/
  	public ArrayList<String> onPressLinkBatalOtomatis( HttpServletRequest request, String[] daftarSpajBatal, Object command )
	{
      if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressLinkBatalOtomatis" );
      RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
      String errMessageFailed = null;
      String errMessageAlreadySave = null;
      // UBAH DISINI
      editForm.setAlasanCd( RefundConst.ALASAN_LAIN );
      editForm.setAlasan( "DATA SAMPAH" );
      editForm.setTindakanCd( RefundConst.TINDAKAN_TIDAK_ADA );
      // END UBAH
      ArrayList<String> spajGagalBatal = new ArrayList<String>();
      for( String spajNo : daftarSpajBatal )
      {
          editForm.setSpaj( spajNo );
          CheckSpajParamsVO tempSpaj = refundBusiness.selectSpajAlreadyCancelMstRefund( spajNo );
          if( tempSpaj != null && !"".equals( tempSpaj ) )
          {
        	  if( errMessageAlreadySave == null ) { errMessageAlreadySave = "SPAJ sudah pernah dibatalkan : \\n" + spajNo ; }
        	  else {  errMessageAlreadySave = errMessageAlreadySave + "\\n" + spajNo; }
          }
          else
          {
        	  MstBatalParamsVO checkSpajCancelAtMstBatal = refundBusiness.selectSpajCancelMstBatal( spajNo );
        	  if( checkSpajCancelAtMstBatal != null && !"".equals( checkSpajCancelAtMstBatal ) )
        	  {
            	  if( errMessageAlreadySave == null ) {  errMessageAlreadySave = "SPAJ sudah pernah dibatalkan : \\n" + spajNo ; }
            	  else { errMessageAlreadySave = errMessageAlreadySave + "\\n" + spajNo; }
        	  }
        	  else
        	  {
        		  RefundProductSpecInterface productSpecific = generateProduct( editForm );
        	        BatalParamsVO paramsVO = new BatalParamsVO();
        	        paramsVO.setAlasan( genAlasan( editForm ) );
        	        paramsVO.setAlasanForLabel(genAlasanForLabel(editForm));
        	        HttpSession session = request.getSession();
        	        User currentUser = ( User ) session.getAttribute( "currentUser" );
        	        paramsVO.setCurrentUser( currentUser );
        	        paramsVO.setSpajNo( editForm.getSpaj() );
        	      	paramsVO.setCancelWhen( editForm.getCancelWhen() == null? new Date() : editForm.getCancelWhen() );
        	      	paramsVO.setCancelWho( editForm.getCancelWho() == null? new BigDecimal( currentUser.getLus_id() ) : editForm.getCancelWho() );

        	        boolean validationPassed;

        	        // jika tidak ada special case pada produk maka dianggap validasi berhasil
        	        if( productSpecific == null )
        	        {
        	            validationPassed = true;
        	        }
        	        else
        	        {
        	            HashMap validationResultMap = productSpecific.validationBeforeCancel( paramsVO );
        	            validationPassed = ( Boolean ) validationResultMap.get( "validationPassed" );
        	        }
        	        if( validationPassed )
        	        {
		                  editHelper.saveDraftTindakanTidakAda( request, command, "Pembatalan SPAJ" );
		                  //jika di nomor ini gagalnya batal, tmpung nomornya
		                  if( !batalkanSpaj( request, command ) )
		                  {
		                	  spajGagalBatal.add( spajNo );
		                	  if( errMessageFailed == null ) { errMessageFailed = "SPAJ gagal dibatalkan : \\n" + spajNo ; }
		                	  else { errMessageFailed = errMessageFailed + "\\n" + spajNo; }
		                  }
        	        }
        	  }
          }
      }
      
      request.setAttribute("errMessageAlreadySave", errMessageAlreadySave);
      request.setAttribute("errMessageFailed", errMessageFailed);
      return spajGagalBatal;
	}
  
	public void onPressAddLampiran( HttpServletRequest request, Object command )
	{
		if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressAddLampiran" );
		RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
		ArrayList< LampiranListVO > lampiranList = new ArrayList<LampiranListVO>();
		if( editForm.getAddLampiran() != null && !"".equals(editForm.getAddLampiran() ) ) {
			lampiranList.add(new LampiranListVO(null,editForm.getAddLampiran(),CommonConst.DISABLED_FALSE));
			if( lampiranList != null && lampiranList.size() > 0 ){
				lampiranList.get(0).setCheckBox(CommonConst.CHECKED_TRUE);
			}
			if( editForm.getLampiranAddList() == null ){
				editForm.setLampiranAddList(lampiranList);
			}else{
				editForm.getLampiranAddList().addAll(lampiranList);  
			}
			editForm.setAddLampiran(null);
		}
	}
  
	public void onPressDeleteLamp( HttpServletRequest request, Object command, String indexDelete )
	{
		if(logger.isDebugEnabled()) logger.debug( "*-*-*-* RefundHelper.onPressAddLampiran" );
		RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
		if( editForm.getLampiranAddList() != null && editForm.getLampiranAddList().size() > 0 ){
			editForm.getLampiranAddList().remove(LazyConverter.toInt(indexDelete));
		}
	}
  
	public void onPressImageDelete( HttpServletRequest request, Object command )
	{
		if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onPressImageDelete" );
		User user = (User) request.getSession().getAttribute("currentUser");  
		RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
		String selectedRowSpaj = lookupForm.getSelectedRowCd();
		refundBusiness.onPressImageDelete( selectedRowSpaj, user );
	}
  
	public void onChangeMerchantCd( HttpServletRequest request, Object command )
	{
		if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundHelper.onChangeMerchantCd" );
		RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
		editHelper.onChangeMerchantCd( editForm  );
	}
}
