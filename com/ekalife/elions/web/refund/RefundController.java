package com.ekalife.elions.web.refund;
/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundController
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Sep 23, 2008 2:38:23 PM
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
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.User;
import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.model.refund.RefundForm;
import com.ekalife.elions.model.refund.RefundLookupForm;
import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.PreviewEditParamsVO;
import com.ekalife.utils.CurrencyFormatEditor;
import com.ekalife.utils.LazyConverter;
import com.ekalife.utils.parent.ParentWizardController;

public class RefundController extends ParentWizardController
{
    public static final int REFUND_LOOKUP_JSP = 0;
    public static final int REFUND_EDIT_JSP = 1;
    public static final int STD_DOWNLOAD_JSP = 2;
    public static final int PREVIEW_LAMP_1_JSP = 3;
    public static final int PREVIEW_LAMP_3_JSP = 4;
    public static final int PREVIEW_REDEMPT_JSP = 5;
    public static final int SIGN_IN_JSP = 6;
    public static final int PREVIEW_EDIT_JSP = 7;
    public static final int REFUND_REKAP_LOOKUP_JSP = 8;
    public static final int PREVIEW_EDIT_SETORAN = 9;
    public static final int REFUND_LOOKUP_AKSEPTASI_JSP = 10;
    
    protected String pages[];
    private RefundBusiness refundBusiness;
    private RefundHelper refundHelper;
    protected CurrencyFormatEditor currencyEditor;
    protected RefundValidator refundValidator;
    
    /*
	protected Connection connection = null;

	protected Connection getConnection() {
		if(this.connection==null) {
			try {
				this.connection = this.elionsManager.getUwDao().getDataSource().getConnection();
			} catch (SQLException e) {
				logger.error("ERROR :", e);
			}
		}
		return this.connection;
	}
	*/

    public void setRefundBusiness( RefundBusiness refundBusiness )
    {
        this.refundBusiness = refundBusiness;
    }
    
    public void setRefundHelper( RefundHelper refundHelper )
    {
        this.refundHelper = refundHelper;
    }

    public void setCurrencyEditor( CurrencyFormatEditor currencyEditor )
    {
        this.currencyEditor = currencyEditor;
    }

    public void setRefundValidator( RefundValidator refundValidator )
    {
        this.refundValidator = refundValidator;
    }

    @Override
    protected Object formBackingObject( HttpServletRequest request ) throws Exception
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundController.formBackingObject" );
        this.pages = getPages();

        setAllowDirtyBack( false );

        return refundHelper.initializeForm( request );
    }

    @Override
    protected void initBinder( HttpServletRequest request, ServletRequestDataBinder binder ) throws Exception
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundController.initBinder" );
        binder.registerCustomEditor( Date.class, null, dateEditor );
        binder.registerCustomEditor( Integer.class, null, integerEditor );
        binder.registerCustomEditor( BigDecimal.class, null, currencyEditor );
    }

    @Override
    protected void onBind( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundController.onBind" );
        String theEvent = request.getParameter( "theEvent" );
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* theEvent = " + theEvent );

        RefundForm refundForm = ( RefundForm ) command;
        refundForm.setTheEvent( theEvent );

        int currentPage = getCurrentPage( request );
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* currentPage = " + currentPage );
    }

    protected boolean suppressValidation( HttpServletRequest request, Object command, BindException errors )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundController.suppressValidation" );

        String theEvent = request.getParameter( "theEvent" );
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* theEvent = " + theEvent );

        Boolean result = false;
        Integer currentPage = getCurrentPage( request );
        Integer targetPage = getTargetPage( request, currentPage );
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* currentPage = " + currentPage );
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* targetPage = " + targetPage );

        if(logger.isDebugEnabled())logger.debug( "*-*-*-* result suppressValidation = " + result );
        return result;
    }

    @Override
    protected void onBindAndValidate( HttpServletRequest request, Object command, BindException errors, int currentPage ) throws Exception
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundController.onBindAndValidate" );
        Integer nextPage = getTargetPage( request, currentPage );
        String theEvent = ( ( RefundForm ) command ).getTheEvent();
//        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
//        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        refundValidator = new RefundValidator( elionsManager, bacManager, command, errors );
        if( nextPage >= currentPage ) //validasi hanya kalo ke halaman yg sama ato berikutnya, bukan ke halaman sebelumnya
        {
            if( currentPage == REFUND_LOOKUP_JSP )
            {
                if( "onPressButtonNext".equals( theEvent ) )
                {
                    // do nothing
                }
            }
            else if( currentPage == REFUND_EDIT_JSP )
            {
                if( "onPressButtonNext".equals( theEvent ) )
                {
                    refundValidator.validateBeforePreview();
                    refundValidator.validateCommonInput();
                    //MANTA
                    refundValidator.validateSetoranPremi();
                }
                else if( "onPressButtonSaveDraft".equals( theEvent ) )
                {
                    refundValidator.validateBeforePreview();
                    refundValidator.validateCommonInput();
                    refundValidator.validateSpajForTindakanTidakAda();
                }
                else if( "onPressButtonBatalkanSpaj".equals( theEvent ) )
                {
                    refundValidator.validateBeforePreview();
                    refundValidator.validateCommonInput();
                    refundValidator.validateSpajForTindakanTidakAda();
                }
            }
        }
        if( currentPage == SIGN_IN_JSP )
        {
            if( "onPressButtonSignIn".equals( theEvent ) )
            {
                refundValidator.validateSignIn();
            }
            else if( "onPressButtonCancel".equals( theEvent ) )
            {
                // nothing
            }
        }
        else if( currentPage == PREVIEW_EDIT_SETORAN )
        {
            if( "onPressButtonNext".equals( theEvent ) )
            {
            	refundValidator.validateAfterClickNext();
            }
        }
        else if( currentPage == PREVIEW_LAMP_3_JSP )
        {
            if( "onPressButtonBatalkanSpaj".equals( theEvent ) )
            {
            	refundValidator.validateAfterClickCancelGbrA3();
            }
        }
        else if( currentPage == REFUND_LOOKUP_AKSEPTASI_JSP )
        {
        	//MANTA
        }
    }

    @Override
    protected HashMap referenceData( HttpServletRequest request, Object command, Errors errors, int page ) throws Exception
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundController.referenceData" );
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* page = " + page );
        
        refundValidator = new RefundValidator( elionsManager, bacManager, command, errors );
        String theEvent = request.getParameter( "theEvent" );
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* theEvent = " + theEvent );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        HashMap<String, String> result = new HashMap<String, String>();
        User user = (User) request.getSession().getAttribute("currentUser");   
        editForm.setCurrentUser(user.getName());
        refundHelper.setlkuList(command);
        refundHelper.refreshListPreviewEdit( theEvent, command );
       
        if(editForm.getSpaj()!=null)
        {
	        refundHelper.generateCountPremi( command, theEvent );
        }
        if( "onPressImageEdit".equals( theEvent ) )
        {
        	refundHelper.clearListDescrAndJumlah( command );
        }
        else if( "onPressImageDelete".equals( theEvent ) )
        {
        	refundHelper.clearListDescrAndJumlah( command );
        }
        else if( "onPressImageSuratRedempt".equals( theEvent ) )
        {
        	refundHelper.clearListDescrAndJumlah( command );
        }
        else if( "onPressImageSuratBatal".equals( theEvent ) )
        {
        	refundHelper.clearListDescrAndJumlah( command );
        }
        
        if( page == REFUND_LOOKUP_JSP )
        {
            refundHelper.setTotalOfPages( command );
            if( "onPressLinkGotoPage".equals( theEvent ) )
            {
                // do nothing
            }
            else if( "onPressLinkFirst".equals( theEvent ) )
            {
                refundHelper.onPressLinkFirst( command );
            }
            else if( "onPressLinkPrev".equals( theEvent ) )
            {
                refundHelper.onPressLinkPrev( command );
            }
            else if( "onPressLinkNext".equals( theEvent ) )
            {
                refundHelper.onPressLinkNext( command );
            }
            else if( "onPressLinkLast".equals( theEvent ) )
            {
                refundHelper.onPressLinkLast( command );
            }
            else if( "onPressMoveToMstDetRefundLamp".equals( theEvent ) )
            {
            	refundHelper.onPressMoveToMstDetRefundLamp( command );
            }
            else if( "onPressAcceptBatalSpaj".equals( theEvent ) )
            {
            	refundHelper.search( request, command, page );
            }
            refundHelper.search( request, command, page );
        }
        else if( page == SIGN_IN_JSP )
        {
            // nothing
        }
        else if( page == REFUND_EDIT_JSP )
        {
        	if( "onBlurNewSpaj".equals( theEvent ) )
        	{
            	if( editForm.getTindakanCd()!=null && editForm.getTindakanCd() == 4 // GANTI PLAN
		    			|| editForm.getTindakanCd() != null && editForm.getTindakanCd() == 3 )//GANTI TERTANGGUNG
		    	{
            		if( editForm.getSpajBaru() != null && !"".equals( editForm.getSpajBaru() ) )
            		{
            			editForm.setSpajBaru(editForm.getSpajBaru().replace(".", ""));// menghilangkan titik pada spaj baru
            			editForm.setSpajBaru(editForm.getSpajBaru().replace(" ", ""));// menghilangkan spasi pada spaj baru
                    	
                    	//MANTA (21/11/2013)
                    	PolicyInfoVO policyInfoVONew = elionsManager.selectPolicyInfoBySpaj(editForm.getSpajBaru().toString());
                    	editForm.setPPBaru(policyInfoVONew.getNamaPp());
                    	editForm.setTTBaru(policyInfoVONew.getNamaTt());
                    	editForm.setProdukBaru(policyInfoVONew.getNamaProduk());
            		}else{
            			editForm.setPPBaru("");
            			editForm.setTTBaru("");
            			editForm.setProdukBaru("");
            		}
		    	}
        	}
        	String indexDelete = request.getParameter("indexDelete");
        	if( indexDelete != null && !"".equals( indexDelete ) )
        	{
        		refundHelper.onPressDeleteLamp( request, command, indexDelete );
        	}
            if( "onPressLinkAdd".equals( theEvent ) )
            {
                refundHelper.onPressLinkAdd( request, command );
            }
            else if( "onPressImageEdit".equals( theEvent ) )
            {
            	editForm.setSetoranPokokAndTopUpFlag( null );
            	editForm.setFlagHelp( null );
                refundHelper.onPressImageEdit( request, command );
            }
            else if( "onChangeAlasanCd".equals( theEvent ) )
            {
            	 refundHelper.onChangeAlasanCd( request, command );
            }
            else if( "onChangeTindakanCd".equals( theEvent ) || "onBlurSpaj".equals( theEvent ) )
            {
            	if( "onBlurSpaj".equals( theEvent ) )
            	{
            		refundHelper.setBiayaAdmin( editForm );
            	}
                refundHelper.refreshPenarikanList( request, command );  
                if(editForm.getTempSpaj()!=null && !"".equals(editForm.getTempSpaj()))
        	    {
	                if(!editForm.getTempSpaj().trim().equals(editForm.getSpaj().trim()))
	                {
	                	  editForm.setFlagHelp( null );
	                	  editForm.setSetoranPokokAndTopUpFlag( null );
	                }
        	    }
            }
            else if( "onChangeMerchantCd".equals( theEvent ) )
            {
            	 refundHelper.onChangeMerchantCd( request, command );
            }
            else if("onPressButtonNext".equals(theEvent))
            {
            	
            }
        	else if("onPressButtonSendPhysicalDoc".equals(theEvent))
        	{
        		refundValidator.validateTglKirimDokFisik();
        		refundHelper.updateTglKirimDokFisik( command, user.getLus_id() );
        	}
        	else if("onPressButtonAddLampiran".equals(theEvent))
        	{
        		refundHelper.onPressAddLampiran(request, command);
        	}
            refundHelper.initializeEditFormGeneral( request, command, theEvent );
            refundHelper.fillTindakanListAndAlasanList( request, command );
            if( !"onPressLinkAdd".equals( theEvent ) )
            {
                if( editForm.getTindakanCd()!=null && RefundConst.TINDAKAN_TIDAK_ADA.equals(editForm.getTindakanCd())) 
    	        {
    	        	refundHelper.generateDocumentTindakanTidakAda( command );
    	        }
            }
            result = refundHelper.addAdditionalDataMap( command );
            refundHelper.setSumMtuUnitTransUlink( command );
        }
        // mengisi data untuk preview yang nantinya juga sbg data utk download
        else if( page == PREVIEW_LAMP_1_JSP )
        {
            refundHelper.initializeForm( request, command );
            refundHelper.statusULinkOrNot( request, command, theEvent );
            refundHelper.generateCountPremi( command, theEvent );
            refundHelper.setlampiranList( command, theEvent, page );
            refundHelper.generateDocumentGbrA1A2B1( command );
        }
        else if( page == PREVIEW_LAMP_3_JSP )
        {
            refundHelper.initializeLamp3Form( request, command );
            refundHelper.statusULinkOrNot( request, command, theEvent );
            refundHelper.generateCountPremi( command, theEvent );
            refundHelper.setlampiranList( command, theEvent, page );        
            refundHelper.generateDocumentGbrA3A4B2B3( command );
        }
        else if( page == PREVIEW_REDEMPT_JSP )
        {
            refundHelper.initializeRedempt( request, command );
            refundHelper.setlampiranList( command, theEvent, page);     
            refundHelper.generateDocumentRedempt( command );
        }
        else if( page == PREVIEW_EDIT_JSP)
        {
        	if(editForm.getFlagHelp() == null) // flag utk membantu mencegah masuk ke generateDocumentPreviewEdit setiap refresh
        	{
        		refundHelper.generateDocumentGbrA1EditDetail( command );
        		editForm.setFlagHelp( 1 );
        		editForm.setTempSpaj( editForm.getSpaj() );
        	}
        	if("onPressButtonDelete".equals(theEvent))
        	{
        		String helpIndex = request.getParameter("helpIndex");
        		if(editForm.getTempDescrDanJumlah() != null && editForm.getTempDescrDanJumlah().size() > 0)
        		{
        			editForm.getTempDescrDanJumlah().remove(LazyConverter.toInt(helpIndex));
        		}
        	}
        	else if("onPressButtonAddNewRow".equals(theEvent))
        	{
        		if(editForm.getTempDescrDanJumlah() == null)
        		{
        			ArrayList <PreviewEditParamsVO> temp = new ArrayList<PreviewEditParamsVO>();
        			temp.add( 0, new PreviewEditParamsVO(  "", new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), null, null, null, null, null, CommonConst.DISABLED_FALSE,null, null, null, null ) );
        			editForm.setTempDescrDanJumlah( temp );
        		}
        		else
        		{
        			editForm.getTempDescrDanJumlah().add( new PreviewEditParamsVO( "", new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), null, null, null, null,null, CommonConst.DISABLED_FALSE, null, null, null, null ) );
        		}
        	}
        	else if("onPressButtonCount".equals(theEvent))
        	{
//        		refundHelper.generateCountPremi( command );
        	}
        	
        	refundHelper.generateAllDescrAndJumlahForPreviewEdit( command );
        }
        else if( page == REFUND_REKAP_LOOKUP_JSP )
        {
        	 if("onPressLinkSearch".equals( theEvent ))
        	 {
        		 refundHelper.search( request, command, page );
        	 }
        }
        else if( page == PREVIEW_EDIT_SETORAN )
        {
        	if( editForm.getSetoranPokokAndTopUpFlag() == null )
        	{
        		refundHelper.initializeForm( request, command );
        		refundHelper.generateDocumentPreviewEditSetoran( command );
        		editForm.setSetoranPokokAndTopUpFlag( 1 );
        		editForm.setTempSpaj(editForm.getSpaj());
        	}
        }
        //MANTA
        else if( page == REFUND_LOOKUP_AKSEPTASI_JSP )
        {
        	if("onPressLinkSearch".equals( theEvent ))
        	{
        		refundHelper.search( request, command, page );
        	}
        	refundHelper.search( request, command, page );
        }
        
        if( !"onPressImageSuratBatal".equals( theEvent ) )
        {
        	refundHelper.setlampiranList( command, theEvent, page);     
        }
        
        return result;
    }

    @Override
    protected ModelAndView processFinish( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundController.processFinish" );
        return new ModelAndView( new RedirectView( request.getContextPath() + "/common/menu.htm?frame=main" ) );
    }

    protected void postProcessPage( HttpServletRequest request, Object command, Errors errors, int page ) throws Exception
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundController.postProcessPage" );
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* page = " + page );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
//        RefundForm refundForm = ( RefundForm ) command;
//        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
//        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
//        RefundPreviewLamp1Form lamp1Form = ( ( RefundForm ) command ).getLamp1Form();
//        RefundPreviewLamp3Form lamp3Form = ( ( RefundForm ) command ).getLamp3Form();
//        RefundPreviewInstruksiRedemptForm redemptForm = ( ( RefundForm ) command ).getRedemptForm();

        User user = (User) request.getSession().getAttribute("currentUser");   
        editForm.setCurrentUser(user.getName());
        String theEvent = request.getParameter( "theEvent" );
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* theEvent = " + theEvent );

        // lakukan tindakan berikut, entah form ada error atau tidak pada validasi
        if( "onPressImageEdit".equals( theEvent ) )
        {
        	refundHelper.clearListDescrAndJumlah( command );
        }
        else if( "onPressImageDelete".equals( theEvent ) )
        {
        	refundHelper.clearListDescrAndJumlah( command );
        }
        else if( "onPressImageSuratRedempt".equals( theEvent ) )
        {
        	refundHelper.clearListDescrAndJumlah( command );
        }
        else if( "onPressImageSuratBatal".equals( theEvent ) )
        {
        	refundHelper.clearListDescrAndJumlah( command );
        }
        if( page == REFUND_LOOKUP_JSP )
        {
            if( "onPressImageSuratBatal".equals( theEvent ) )
            {
            	refundHelper.statusULinkOrNot( request, command, theEvent );
                if(lookupForm.getSelectedRowCd()!=null)
                {
        	        refundHelper.generateCountPremi( command, theEvent );
                }
                refundHelper.setlampiranList( command, theEvent, page );  
                refundHelper.onPressImageSuratBatal( request, command );
            }
            else if( "onPressImageSuratRedempt".equals( theEvent ) )
            {
                refundHelper.onPressImageSuratRedempt( request, command );
            }
            else if( "onPressBatalSpaj".equals( theEvent ) )
            {
            	String[] daftarSpajBatal = new String[]{       // masukan no spaj yg ingin dibatalkan
            	};
                refundHelper.onPressLinkBatalOtomatis( request, daftarSpajBatal, command );
            }
            else if( "onPressImageDelete".equals( theEvent ) )
            {
            	refundHelper.onPressImageDelete( request, command );
            }
        }
        else if( page == REFUND_EDIT_JSP )
        {
        	if( "onPressButtonPreviewSuratBatal".equals( theEvent ) )
        	{
        		refundHelper.onPressButtonViewAttachmentLampTanpaTindakan( request, command );
        	}
        }
        else if( page == PREVIEW_LAMP_1_JSP )
        {
            if( "onPressButtonSaveDraft".equals( theEvent ) )
            {
                refundHelper.onPressButtonSaveDraftTindakanRefund( request, command );
            }
        }
        else if( page == PREVIEW_LAMP_3_JSP )
        {
            if( "onPressButtonSaveDraft".equals( theEvent ) )
            {
                refundHelper.onPressButtonSaveDraftTindakanGantiTertanggung( request, command );
            }
        }
        else if( page == REFUND_REKAP_LOOKUP_JSP )
        {
            if( "onPressLinkSuratRekapKeAccFinance".equals( theEvent ) )
            {
            	refundHelper.onPressLinkSuratRekapKeAccFinance( request, command, props );
            }
        }
        // hanya lakukan jika form telah melewati validasi
        if( errors.getErrorCount() == 0 && errors.getGlobalErrorCount() == 0 )
        {
            if( page == REFUND_LOOKUP_JSP )
            {
                if( "onPressButtonViewAttachment".equals( theEvent ) )
                {
                    refundHelper.onPressButtonViewAttachmentLamp3( request, command );
                }
            }
            else if( page == REFUND_EDIT_JSP )
            {
                if( "onPressButtonSaveDraft".equals( theEvent ) )
                {
                    refundHelper.onPressButtonSaveDraftTindakanTidakAda( request, command );
                }
                else if( "onPressButtonBatalkanSpaj".equals( theEvent ) )
                {
                	// TODO: step 1
                	String kategoriUW = uwManager.selectKategoriUW(editForm.getSpaj());
                    refundHelper.onPressButtonBatalkanSpajTindakanTidakAda( kategoriUW, request, command, props );
                }
                else if( "onPressButtonPreviewSuratBatal".equals( theEvent ) )
                {
                	 refundHelper.onPressButtonViewAttachmentLampTanpaTindakan( request, command );
                }
            }
            else if( page == PREVIEW_LAMP_1_JSP )
            {
                if( "onPressButtonViewAttachment".equals( theEvent ) )
                {
                    refundHelper.onPressButtonViewAttachmentLamp1( request, command );
                }else if( "onPressAccBatalSpaj".equals( theEvent ) )
                {
                	String kategoriUW = uwManager.selectKategoriUW(editForm.getSpaj());
                    refundHelper.onPressButtonAccBatalTindakanRefund( kategoriUW, request, command, props );
                }
                else if( "onPressButtonBatalkanSpaj".equals( theEvent ) )
                {
                	String kategoriUW = uwManager.selectKategoriUW(editForm.getSpaj());
                    refundHelper.onPressButtonBatalkanSpajTindakanRefund( kategoriUW, request, command, props );
                }
                else if( "onPressButtonUpdatePayment".equals( theEvent ) )
                {
                    refundHelper.onPressButtonUpdatePayment( request, command, props );
                }
            }
            else if( page == PREVIEW_LAMP_3_JSP )
            {
                if( "onPressButtonViewAttachment".equals( theEvent ) )
                {
                    refundHelper.onPressButtonViewAttachmentLamp3( request, command );
                }
                else if( "onPressAccBatalSpaj".equals( theEvent ) )
                {
                	String kategoriUW = uwManager.selectKategoriUW(editForm.getSpaj());
                    refundHelper.onPressButtonAccBatalTindakanRefund( kategoriUW, request, command, props );
                }
                else if( "onPressButtonBatalkanSpaj".equals( theEvent ) )
                {
                	String kategoriUW = uwManager.selectKategoriUW(editForm.getSpaj());                	
                    refundHelper.onPressButtonBatalkanSpajGbrA3GbrB2( kategoriUW, request, command, props, editForm.getTindakanCd() );
                }
            }
            else if( page == PREVIEW_REDEMPT_JSP )
            {
                if( "onPressButtonViewAttachment".equals( theEvent ) )
                {
                    refundHelper.onPressButtonViewAttachmentRedempt( request, command );
                }
                else if( "onPressButtonKirim".equals( theEvent ) )
                {
                    refundHelper.onPressButtonKirimRedemption( request, command, props );
                }
                else if( "onPressButtonViewAttachmentExcel".equals( theEvent ) )
                {
                	refundHelper.onPressButtonPrintRedemptionExcel( request, command, props, this.elionsManager.getUwDao().getDataSource().getConnection() );
                }
            }
            
        }
    }
}