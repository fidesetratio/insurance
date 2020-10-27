package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundValidator
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 13, 2008 4:05:11 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;

import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.model.refund.RefundForm;
import com.ekalife.elions.model.refund.RefundSignInForm;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.elions.web.refund.vo.InfoBatalVO;
import com.ekalife.elions.web.refund.vo.PenarikanUlinkVO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;

public class RefundValidator
{
    protected final Log logger = LogFactory.getLog( getClass() );

    ElionsManager elionsManager;
    BacManager bacManager;
    Object command;
    Errors errors;
    RefundBusiness refundBusiness;

    public RefundValidator( ElionsManager elionsManager, BacManager bacManager, Object command, Errors errors )
    {
        this.elionsManager = elionsManager;
        this.bacManager = bacManager;
        this.command = command;
        this.errors = errors;
        this.refundBusiness = new RefundBusiness( elionsManager );
    }

    public void validateCommonInput()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundValidator.validateCommonInput" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();

        if( isEmpty( editForm.getSpaj() ) )
        {
            errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.emptyForm", null, "error.refund.emptyForm" );
        }
        else
        {
            PolicyInfoVO policyInfoVO = elionsManager.selectPolicyInfoBySpaj( editForm.getSpaj() );
            if( policyInfoVO == null )
            {
                errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.spajNotExist", null, "error.refund.spajNotExist" );
            }
            else
            {
                // refresh jika nomor spaj mengandung huruf (supaya hurufnya dihilangkan )
                editForm.setSpaj( policyInfoVO.getSpajNo() );

                if( CommonConst.DISABLED_FALSE.equals( editForm.getSpajIsDisabled() )
                        && elionsManager.selectRefundByCd( policyInfoVO.getSpajNo() ) != null )
                {
                    errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.spajDraftExist", null, "error.refund.spajDraftExist" );
                
                }else if(policyInfoVO.getLssp_id() == 5 || policyInfoVO.getLssp_id() == 7){
                	errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.spajCCV", null, "error.refund.spajCCV" );
                	
                }else{
                    if( editForm.getPosisiNo() == null || editForm.getPosisiNo().equals( RefundConst.POSISI_DRAFT ) )
                    {
                        if( CommonConst.DISABLED_FALSE.equals( editForm.getSpajIsDisabled() ) )
                        {
                            InfoBatalVO infoBatalVO = elionsManager.selectInfoBatalBySpaj( policyInfoVO.getSpajNo() );
                            if( infoBatalVO != null )
                            {
                                errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.spajHasBeenCanceled", null, "error.refund.spajHasBeenCanceled" );
                            }
                        }
                    }
                    else
                    {
                        if( editForm.getAlasanCd() == null )
                        {
                            errors.rejectValue( RefundEditFormConst.ALASAN_CD, "error.refund.emptyForm", null, "error.refund.emptyForm" );
                        }
                        else if( editForm.getAlasanCd().equals( RefundConst.ALASAN_LAIN ) )
                        {
                            if( isEmpty( editForm.getAlasan() ) )
                            {
                                errors.rejectValue( RefundEditFormConst.ALASAN, "error.refund.emptyForm", null, "error.refund.emptyForm" );
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isEmpty( String str )
    {
        return str == null || str.trim().equals( "" );
    }

    private void validateBiaya2( RefundEditForm editForm )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundValidator.validateBiaya2" );
        // validasi biaya2
        if( editForm.getBiayaLain() != null && isEmpty( editForm.getBiayaLainDescr() ) )
        {
            errors.rejectValue( RefundEditFormConst.BIAYA_LAIN_DESCR, "error.refund.emptyForm", null, "error.refund.emptyForm" );
        }
    }

    private void validateSeputarNorek( RefundEditForm editForm )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundValidator.validateSeputarNorek" );
        // validasi seputar nomor rekening
        if( isEmpty( editForm.getAtasNama() ) )
        {
            errors.rejectValue( RefundEditFormConst.ATAS_NAMA, "error.refund.emptyForm", null, "error.refund.emptyForm" );
        }
        if( isEmpty( editForm.getNorek() ) )
        {
            errors.rejectValue( RefundEditFormConst.NOREK, "error.refund.emptyForm", null, "error.refund.emptyForm" );
        }
        if( isEmpty( editForm.getNamaBank() ) )
        {
            errors.rejectValue( RefundEditFormConst.NAMA_BANK, "error.refund.emptyForm", null, "error.refund.emptyForm" );
        }
        if( isEmpty( editForm.getCabangBank() ) )
        {
            errors.rejectValue( RefundEditFormConst.CABANG_BANK, "error.refund.emptyForm", null, "error.refund.emptyForm" );
        }
        if( isEmpty( editForm.getKotaBank() ) )
        {
            errors.rejectValue( RefundEditFormConst.KOTA_BANK, "error.refund.emptyForm", null, "error.refund.emptyForm" );
        }
    }

    public void validateAfterClickCancelGbrA3()
    {
    	RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
    	
//        if( RefundConst.UNIT_FLAG.equals( editForm.getHasUnitFlag() ) 
//                && editForm.getPenarikanUlinkVOList() != null
//                && editForm.getPenarikanUlinkVOList().size() > 0
//                && refundBusiness.isUnitLink( editForm.getSpaj() )
//                && editForm.getPosisiNo() != null && editForm.getPosisiNo().equals( RefundConst.POSISI_GANTI_TERTANGGUNG_ULINK_REDEMPT_SEND ) )
//        {
//            for( int i = 0; i < editForm.getPenarikanUlinkVOList().size(); i++ )
//            {
//                PenarikanUlinkVO vo = editForm.getPenarikanUlinkVOList().get( i );
//                if( vo.getJumlah() == null )
//                {
//                    errors.rejectValue( "editForm.penarikanUlinkVOList[" + i + "].jumlah", "error.refund.emptyForm", null, "error.refund.emptyForm" );
//                }
//                else
//                {
//                    if(logger.isDebugEnabled())logger.debug( "*-*-*-* vo.getJumlah() = " + vo.getJumlah() );
//                }
//            }
//        }
    	
    }
    
    public void validateAfterClickNext()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundValidator.validateAfterClickNext" );

        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        
        if( editForm.getSetoranPokokAndTopUp() != null && editForm.getSetoranPokokAndTopUp().size() > 0 )
        {
        	for( int i = 0 ; i < editForm.getSetoranPokokAndTopUp().size() ; i ++ )
        	{
        		if( editForm.getSetoranPokokAndTopUp().get( i ).getJumlahPokok() == null && editForm.getSetoranPokokAndTopUp().get( i ).getJumlahTopUp() == null || 
        				editForm.getSetoranPokokAndTopUp().get( i ).getJumlahPokok() != null && editForm.getSetoranPokokAndTopUp().get( i ).getJumlahTopUp() == null && BigDecimal.ZERO.equals( editForm.getSetoranPokokAndTopUp().get( i ).getJumlahPokok() ) ||
        				editForm.getSetoranPokokAndTopUp().get( i ).getJumlahPokok() == null && editForm.getSetoranPokokAndTopUp().get( i ).getJumlahTopUp() != null && BigDecimal.ZERO.equals( editForm.getSetoranPokokAndTopUp().get( i ).getJumlahTopUp() ) ||
        				editForm.getSetoranPokokAndTopUp().get( i ).getJumlahPokok() != null && editForm.getSetoranPokokAndTopUp().get( i ).getJumlahTopUp() != null && BigDecimal.ZERO.equals( editForm.getSetoranPokokAndTopUp().get( i ).getJumlahPokok().add( editForm.getSetoranPokokAndTopUp().get( i ).getJumlahTopUp() ) )
        				)
        		{
        			errors.rejectValue( "editForm.setoranPokokAndTopUp[" + i + "].jumlahTopUp", "error.refund.sumPokokAndTopUp", null, "error.refund.sumPokokAndTopUp" );
        		}
//        		if( editForm.getSetoranPokokAndTopUp().get( i ).getJumlahPokok() == null || BigDecimal.ZERO.equals( editForm.getSetoranPokokAndTopUp().get( i ).getJumlahPokok() ) )
//        		{
//        			errors.rejectValue( "editForm.setoranPokokAndTopUp[" + i + "].jumlahPokok", "error.refund.emptyForm", null, "error.refund.emptyForm" );
//        		}
//        		if( editForm.getSetoranPokokAndTopUp().get( i ).getJumlahTopUp() == null || BigDecimal.ZERO.equals( editForm.getSetoranPokokAndTopUp().get( i ).getJumlahTopUp() ) )
//        		{
//        			errors.rejectValue( "editForm.setoranPokokAndTopUp[" + i + "].jumlahTopUp", "error.refund.emptyForm", null, "error.refund.emptyForm" );
//        		}
        	}
        }
    }
    
    public void validateBeforePreview()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundValidator.validateBeforePreview" );

        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
    	Date msteTglKirimPolis = elionsManager.selectMsteTglKirimPolis( editForm.getSpaj() );
    	Date tglBatasPemahamanPolis = elionsManager.selectSysdate1("dd", true, -21);
    	Long nKirimPolis = new Long(0);
    	Long nBatasPemahamanPolis = tglBatasPemahamanPolis.getTime();
    	if( msteTglKirimPolis != null ){
    		nKirimPolis = msteTglKirimPolis.getTime();
    	}
    	Integer i_posisi = editForm.getPosisiNo();
    	if(i_posisi == null) i_posisi = 0;
    		
        if( !"disabled".equals(editForm.getAlasanIsDisabled()) && editForm.getTindakanCd() != null && editForm.getSpaj() != null && !"".equals( editForm.getSpaj() ) )
        {
        	List selectFilterRefundSpaj = elionsManager.selectFilterRefundSpaj(null, "0", editForm.getSpaj(), "EQ", null, null, null, null);

            // Comment baris dibawah utk SPECIAL CASE REFUND
//            if( (selectFilterRefundSpaj == null || (selectFilterRefundSpaj != null && selectFilterRefundSpaj.size() == 0)) && (nKirimPolis != 0 && (nKirimPolis < nBatasPemahamanPolis)) )
//        	{
//        		String documentPositionSpaj = elionsManager.selectLstDocumentPosition( editForm.getSpaj().trim() );
//        		errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.spajTidakBisaDigunakan", new Object[]{documentPositionSpaj}, "error.refund.emptyForm" );
//        	}
        }
//        if( editForm.getTindakanCd() != null && editForm.getSpajBaru() != null && !"".equals(editForm.getSpajBaru() ) )
//        {
//        	String[] regSpaj = editForm.getSpajBaru().split(",");
//        	String spajList = null;
//        	for(int i = 0 ; i < regSpaj.length ; i ++ )
//        	{
//        		String spaj = regSpaj[i].trim();
//        		List selectFilterRefundSpaj = elionsManager.selectFilterRefundSpaj(null, "0", spaj, "EQ", null, null, null, "isNewSpaj");	
//            	if( selectFilterRefundSpaj == null || selectFilterRefundSpaj != null && selectFilterRefundSpaj.size() == 0 )
//            	{
//            		if( spajList == null )
//            		{
//            			spajList = spaj;
//            		}
//            		else
//            		{
//            			spajList = spajList +","+ spaj;
//            		}
//            	}
//        	}
//    		if( spajList != null && !"".equals( spajList ) )
//    		{
//    			errors.rejectValue( RefundEditFormConst.SPAJ_BARU, "error.refund.spajBaruTidakBisaDigunakan", new Object[]{spajList}, "error.refund.emptyForm" );
//    		}
//        }
        if( editForm.getSpaj() != null && !"".equals( editForm.getSpaj() ) && editForm.getTindakanCd() != null && !RefundConst.TINDAKAN_TIDAK_ADA.equals( editForm.getTindakanCd() ) )
        {
        	PolicyInfoVO PolicyInfo = elionsManager.selectPolicyInfoBySpaj( editForm.getSpaj() );
        	if( msteTglKirimPolis != null )
        	{
        		if( editForm.getBiayaAdmin() == null || editForm.getBiayaAdmin() != null && BigDecimal.ZERO.equals( editForm.getBiayaAdmin() ) )
        		{
        			// Comment baris dibawah utk SPECIAL CASE REFUND
        			// MANTA - Khusus ARCO tidak terkena biaya Administrasi
//        			if( !PolicyInfo.getLca_id().toString().equals("40") ){
//        				errors.rejectValue( RefundEditFormConst.BIAYA_ADMIN, "error.refund.emptyBiayaAdmin", null, "error.refund.emptyBiayaAdmin" );
//        			}
        		}
        	}
        }
        
        if( editForm.getTindakanCd() == null )
        {
            errors.rejectValue( RefundEditFormConst.TINDAKAN_CD, "error.refund.emptyForm", null, "error.refund.emptyForm" );
        }
        else if( editForm.getTindakanCd().equals( RefundConst.TINDAKAN_REFUND_PREMI ) )
        {
            validateBiaya2( editForm );
            validateSeputarNorek( editForm );
            if( editForm.getBiayaIsDisabled().equals( CommonConst.DISABLED_TRUE )
                    && editForm.getPenarikanUlinkVOList() != null
                    && editForm.getPenarikanUlinkVOList().size() > 0
                    && refundBusiness.isUnitLink( editForm.getSpaj() )
                    && i_posisi.equals( RefundConst.POSISI_REFUND_ULINK_REDEMPT_SENT )
                    )
            {
                for( int i = 0; i < editForm.getPenarikanUlinkVOList().size(); i++ )
                {
                    PenarikanUlinkVO vo = editForm.getPenarikanUlinkVOList().get( i );
                    if( vo.getJumlah() == null )
                    {
                        errors.rejectValue( "editForm.penarikanUlinkVOList[" + i + "].jumlah", "error.refund.emptyForm", null, "error.refund.emptyForm" );
                    }
                    else
                    {
                        if(logger.isDebugEnabled())logger.debug( "*-*-*-* vo.getJumlah() = " + vo.getJumlah() );
                    }
                }
            }

//            if( CommonConst.DISABLED_FALSE.equals( editForm.getPaymentIsDisabled() ) )
//            {
//                if( editForm.getPayment() == null )
//                {
//                    errors.rejectValue( RefundEditFormConst.PAYMENT, "error.refund.emptyForm", null, "error.refund.emptyForm" );
//                }
//                if( editForm.getPaymentDate() == null )
//                {
//                    errors.rejectValue( RefundEditFormConst.PAYMENT_DATE, "error.refund.emptyForm", null, "error.refund.emptyForm" );
//                }
//            }

            //todo

            if( refundBusiness.isUnitLink( editForm.getSpaj() ) )
            {
                if( editForm.getHasUnitFlag() == null )
                {
                    errors.rejectValue( RefundEditFormConst.HAS_UNIT_FLAG, "error.refund.emptyForm", null, "error.refund.emptyForm" );
                }
                else if( editForm.getHasUnitFlag().equals( 1 ) )
                {
                    if( editForm.getPenarikanUlinkVOList() == null || editForm.getPenarikanUlinkVOList().size() == 0 )
                    {
                        errors.rejectValue( RefundEditFormConst.HAS_UNIT_FLAG, "error.refund.dataUnitNotFound", null, "error.refund.emptyForm" );
                    }
                }
                else if( editForm.getHasUnitFlag().equals( 0 ) )
                {
                    if( editForm.getPenarikanUlinkVOList() != null && editForm.getPenarikanUlinkVOList().size() > 0 )
                    {
                        errors.rejectValue( RefundEditFormConst.HAS_UNIT_FLAG, "error.refund.dataUnitShouldExist", null, "error.refund.emptyForm" );
                    }
                }
            }

            Integer posisiDokumen = elionsManager.selectPosisiDocumentBySpaj( editForm.getSpaj() );
            if( editForm.getPosisiNo() == null || editForm.getPosisiNo() != null && editForm.getPosisiNo() == 0 )
            {
                if( posisiDokumen == null )
                {
                    errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.posisiDokumenKosong", null, "error.refund.posisiDokumenKosong" );
                }
                else
                {
                    if( refundBusiness.isUnitLink( editForm.getSpaj() )
                            && editForm.getPosisiNo() != null
                            && editForm.getPosisiNo().compareTo( RefundConst.POSISI_REFUND_ULINK_CANCEL ) >= 0 )
                    {
                        // do no validator
                    }
                    else
                    {
                    	// do validator
                        // Comment baris dibawah utk SPECIAL CASE REFUND
//                        if( (posisiDokumen != 1 && posisiDokumen != 2 && posisiDokumen != 4 && posisiDokumen != 6 && posisiDokumen != 7) &&
//                        	(nKirimPolis != 0 && (nKirimPolis < nBatasPemahamanPolis)))
//                        {
//                            errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.posisiNotInNewBusiness", null, "error.refund.posisiNotInNewBusiness" );
//                        }
                    }
                }
            }
            
            //MANTA - Bila komisi sudah dibayarkan maka SPAJ tidak dapat dibatalkan
            Integer bayarKomisi = bacManager.selectCekBayarKomisi(editForm.getSpaj());
            if(bayarKomisi > 0){
            	errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.komisiSudahDibayar", null, "error.refund.komisiSudahDibayar" );
            }
     
        }
        else if( editForm.getTindakanCd().equals( RefundConst.TINDAKAN_GANTI_TERTANGGUNG ) )
        {
            validateBiaya2( editForm );

            PolicyInfoVO newPolicyInfoVO = elionsManager.selectPolicyInfoBySpaj( editForm.getSpajBaru() );
            PolicyInfoVO oldPolicyInfoVO = elionsManager.selectPolicyInfoBySpaj( editForm.getSpaj() );

            if( isEmpty( editForm.getSpajBaru()  ) )
            {
                errors.rejectValue( RefundEditFormConst.SPAJ_BARU, "error.refund.emptyForm", null, "error.refund.emptyForm" );
            }
            else if( editForm.getSpajBaru().trim().equals( editForm.getSpaj().trim() ) )
            {
                errors.rejectValue( RefundEditFormConst.SPAJ_BARU, "error.refund.spajCannotTheSame", null, "error.refund.spajCannotTheSame" );
            }
            else if( newPolicyInfoVO == null )
            {
                errors.rejectValue( RefundEditFormConst.SPAJ_BARU, "error.refund.newSpajNotEnlisted", null, "error.refund.newSpajNotEnlisted" );
            }
//            else if( !newPolicyInfoVO.getNamaPp().equals( oldPolicyInfoVO.getNamaPp() ) )
//            {
//                errors.rejectValue( RefundEditFormConst.SPAJ_BARU, "error.refund.policyHolderDifferent", null, "error.refund.policyHolderDifferent" );
//            }
//            else if( !newPolicyInfoVO.getNamaProduk().equals( oldPolicyInfoVO.getNamaProduk() ) )
//            {
//                errors.rejectValue( RefundEditFormConst.SPAJ_BARU, "error.refund.newProductDifferent", null, "error.refund.newProductDifferent" );
//            }
            
            if( editForm.getBiayaIsDisabled().equals( CommonConst.DISABLED_FALSE )
                    && editForm.getPenarikanUlinkVOList() != null
                    && editForm.getPenarikanUlinkVOList().size() > 0
                    && refundBusiness.isUnitLink( editForm.getSpaj() )
                    && editForm.getPosisiNo() != null && editForm.getPosisiNo().equals( RefundConst.POSISI_GANTI_TERTANGGUNG_ULINK_REDEMPT_SEND ) )
            {
                for( int i = 0; i < editForm.getPenarikanUlinkVOList().size(); i++ )
                {
                    PenarikanUlinkVO vo = editForm.getPenarikanUlinkVOList().get( i );
                    if( vo.getJumlah() == null )
                    {
                        errors.rejectValue( "editForm.penarikanUlinkVOList[" + i + "].jumlah", "error.refund.emptyForm", null, "error.refund.emptyForm" );
                    }
                    else
                    {
                        if(logger.isDebugEnabled())logger.debug( "*-*-*-* vo.getJumlah() = " + vo.getJumlah() );
                    }
                }
            }

        }
        else if( editForm.getTindakanCd().equals( RefundConst.TINDAKAN_GANTI_PLAN ) )
        {
            validateBiaya2( editForm );

            PolicyInfoVO newPolicyInfoVO = elionsManager.selectPolicyInfoBySpaj( editForm.getSpajBaru() );
//            PolicyInfoVO oldPolicyInfoVO = elionsManager.selectPolicyInfoBySpaj( editForm.getSpaj() );

            if( isEmpty( editForm.getSpajBaru()  ) )
            {
                errors.rejectValue( RefundEditFormConst.SPAJ_BARU, "error.refund.emptyForm", null, "error.refund.emptyForm" );
            }
            else if( editForm.getSpajBaru().trim().equals( editForm.getSpaj().trim() ) )
            {
                errors.rejectValue( RefundEditFormConst.SPAJ_BARU, "error.refund.spajCannotTheSame", null, "error.refund.spajCannotTheSame" );
            }
            else if( newPolicyInfoVO == null )
            {
                errors.rejectValue( RefundEditFormConst.SPAJ_BARU, "error.refund.newSpajNotEnlisted", null, "error.refund.newSpajNotEnlisted" );
            }

//            else if( !newPolicyInfoVO.getNamaPp().equals( oldPolicyInfoVO.getNamaPp() ) )
//            {
//                errors.rejectValue( RefundEditFormConst.SPAJ_BARU, "error.refund.policyHolderDifferent", null, "error.refund.policyHolderDifferent" );
//            }
            
            if( editForm.getBiayaIsDisabled().equals( CommonConst.DISABLED_FALSE )
                    && editForm.getPenarikanUlinkVOList() != null
                    && editForm.getPenarikanUlinkVOList().size() > 0
                    && refundBusiness.isUnitLink( editForm.getSpaj() )
                    && editForm.getPosisiNo() != null && editForm.getPosisiNo().equals( RefundConst.POSISI_GANTI_TERTANGGUNG_ULINK_REDEMPT_SEND ) )
            {
                for( int i = 0; i < editForm.getPenarikanUlinkVOList().size(); i++ )
                {
                    PenarikanUlinkVO vo = editForm.getPenarikanUlinkVOList().get( i );
                    if( vo.getJumlah() == null )
                    {
                        errors.rejectValue( "editForm.penarikanUlinkVOList[" + i + "].jumlah", "error.refund.emptyForm", null, "error.refund.emptyForm" );
                    }
                    else
                    {
                        if(logger.isDebugEnabled())logger.debug( "*-*-*-* vo.getJumlah() = " + vo.getJumlah() );
                    }
                }
            }
        }
        else if( editForm.getTindakanCd().equals( RefundConst.TINDAKAN_TIDAK_ADA ) )
        {
//            do nothing
        }
    }

    public void validateSignIn()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundValidator.validateSignIn" );

        RefundSignInForm signInForm = ((RefundForm) command).getSignInForm();

        if( signInForm.getPassword() == null ) signInForm.setPassword( "" );
        String password = signInForm.getPassword().trim().toUpperCase();

		if(password.equals( "" ) ){
            errors.rejectValue( RefundSignInFormConst.PASSWORD, "error.refund.passwordEmpty", null, "error.refund.passwordEmpty" );
        
		}else{
			if( this.elionsManager.validationVerify( 6 ).get("PASSWORD").toString().trim().equals( password ) ){
                signInForm.setSignIn( "true" );
                
            }else{
                errors.rejectValue( RefundSignInFormConst.PASSWORD, "error.refund.passwordWrong", null, "error.refund.passwordWrong" );
			}
		}
    }
    
    public void validateTglKirimDokFisik()
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundValidator.validateTglKirimDokFisik" );
    	RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
    	if( editForm.getSendPhysicalDocDate() == null || "".equals( editForm.getSendPhysicalDocDate() ) )
    	{
    		errors.rejectValue( RefundEditFormConst.TGL_KIRIM_DOK_FISIK, "error.refund.emptyForm", null, "error.refund.emptyForm" );
    	}
    }
    
    public void validateSpajForTindakanTidakAda()
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundValidator.validateTglKirimDokFisik" );
    	RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
    	List<SetoranPremiDbVO> setoran = elionsManager.selectSetoranPremiBySpaj( editForm.getSpaj() );
    	if( editForm.getSpaj() != null && refundBusiness.isUnitLink( editForm.getSpaj() ) )
    	{
	    	if( editForm.getPenarikanUlinkVOList() != null && editForm.getPenarikanUlinkVOList().size() > 0 )
	    	{
	    		errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.alreadyHasUnit", null, "error.refund.alreadyHasUnit" );
	    	}
	    	else if( setoran != null && setoran.size() > 0)
	    	{
	    		errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.alreadyPaid", null, "error.refund.alreadyPaid" );
	    	}
    	}
    	else
    	{
    		if( setoran != null && setoran.size() > 0)
    		{
    			errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.alreadyPaid", null, "error.refund.alreadyPaid" );
    		}
    	}
    }
    
    public void validateSetoranPremi()
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundValidator.validateSetoranPremi" );
    	RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
    	List<SetoranPremiDbVO> setoran = elionsManager.selectSetoranPremiBySpaj( editForm.getSpaj() );
    	if( editForm.getTindakanCd().equals( RefundConst.TINDAKAN_REFUND_PREMI ) && (setoran == null || setoran.size() == 0) )
    	{
    		errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.premiTidakAda", null, "error.refund.premiTidakAda" );
    	}
    }
}