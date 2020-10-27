package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundEditHelper
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Nov 20, 2008 11:29:48 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import com.ekalife.elions.model.User;
import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.model.refund.RefundForm;
import com.ekalife.elions.model.refund.RefundPreviewInstruksiRedemptForm;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.elions.web.refund.vo.*;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.LazyConverter;
import com.ekalife.utils.MappingUtil;
import com.ekalife.utils.StringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import id.co.sinarmaslife.std.model.vo.DropDown;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefundEditHelper extends RefundHelperParent  
{
    protected final Log logger = LogFactory.getLog( getClass() );

    public RefundEditHelper()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundEditHelper constructor is called ..." );
    }
    
    private RefundBusiness refundBusiness;

    public void setRefundBusiness( RefundBusiness refundBusiness )
    {
        this.refundBusiness = refundBusiness;
    }

    public RefundEditForm intializeEditForm()
    {
        // todo
        ArrayList<AlasanVO> alasanVOList = Common.serializableList(refundBusiness.initializeAlasan());
        RefundEditForm editForm = new RefundEditForm();
        editForm.setAlasanList( RefundLookupList.getAlasanList( alasanVOList ) );
        editForm.setTindakanList( RefundLookupList.getTindakanList() );
        editForm.setAlasanVOList( alasanVOList );
        return editForm;
    }

    public PolicyInfoVO genPolicyInfoVOBySpaj( String spajNo )
    {
        PolicyInfoVO policyInfoVO = refundBusiness.selectPolicyInfoBySpaj( spajNo );
        if( policyInfoVO != null )
        {
            policyInfoVO.setSpajNo( FormatString.nomorSPAJ( policyInfoVO.getSpajNo() ) );
            policyInfoVO.setPolicyNo( FormatString.nomorPolis( policyInfoVO.getPolicyNo() ) );
        }
        return policyInfoVO;
    }
    
    public void setBiayaAdmin( RefundEditForm editForm )
    {
    	BigDecimal biayaAdmin = BigDecimal.ZERO;
    	
    	PolicyInfoVO policyInfoVO = genPolicyInfoVOBySpaj( editForm.getSpaj() );
    	
    	CheckSpajParamsVO checkSpajInDb = refundBusiness.selectCheckSpajInDb( editForm.getSpaj() );
    	Date checkMspoDatePrint = refundBusiness.selectMspoDatePrint( editForm.getSpaj() );
    	if( checkSpajInDb == null || "".equals( checkSpajInDb ) )
    	{
	    	if( editForm.getPosisiNo()==null || editForm.getPosisiNo() == 0 )
			{
	    		if( checkMspoDatePrint != null )
	    		{
		    		if( policyInfoVO.getLkuId() != null && RefundConst.KURS_RUPIAH.equals( policyInfoVO.getLkuId() ) )
		    		{
		    			biayaAdmin = new BigDecimal( "50000" );
		    		}
		    		else if(  policyInfoVO.getLkuId() != null && RefundConst.KURS_DOLLAR.equals(policyInfoVO.getLkuId() ) )
		    		{
		    			biayaAdmin = new BigDecimal( "5" );
		    		}
		    		
		    		if ( policyInfoVO != null )
		    		{
		    			if( policyInfoVO.getPolicyNo() == null || policyInfoVO.getPolicyNo().trim().equals( "" )
				        		|| policyInfoVO.getPolicyNo() != null && !policyInfoVO.getPolicyNo().trim().equals( "" ) && policyInfoVO.getLspdId() != null && policyInfoVO.getLspdId() < 6
				        		|| policyInfoVO.getPolicyNo() != null && !policyInfoVO.getPolicyNo().trim().equals( "" ) && policyInfoVO.getLspdId() != null && policyInfoVO.getLspdId() == 95 && policyInfoVO.getPrevLspdId() != null && policyInfoVO.getPrevLspdId() < 6
					      )
				        {
		    				biayaAdmin = new BigDecimal( "0" );
				            editForm.setBiayaAdminIsDisabled( CommonConst.DISABLED_TRUE );
				            editForm.setMerchantListIsDisabled( CommonConst.DISABLED_TRUE );
				        }	
		    		}
	    		}
		        editForm.setBiayaAdmin( biayaAdmin );
			}
    	}
    }
    public void initializeEditFormGeneral( HttpServletRequest request, Object command, String theEvent )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundEditHelper.initializeEditFormGeneral" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();

        String lca_id = "";
        if(!StringUtil.isEmpty(editForm.getSpaj())) lca_id = editForm.getSpaj().substring(0, 2);
        
        editForm.setHasUnitDisplay( CommonConst.DISPLAY_FALSE );
        editForm.setPenarikanUlinkVOListDisplay( CommonConst.DISPLAY_FALSE );
        editForm.setAddLampiranDisplay( CommonConst.DISPLAY_FALSE );
        if( RefundConst.TINDAKAN_REFUND_PREMI.equals( editForm.getTindakanCd() ) )
        {
            editForm.setSpajBaruDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setBiayaAdminDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setBiayaMedisDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setBiayaLainDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setAtasNamaDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setNorekDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setNamaBankDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setCabangBankDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setKotaBankDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setButtonBatalkanSpajDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setButtonSaveDraftDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setButtonPreviewSuratBatalDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setButtonNextDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setAddLampiranDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setSendPhysicalDocDateDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setPaymentDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setBiayaMerchantDisplay( CommonConst.DISPLAY_TRUE );
//            if(lca_id.equals("40")){
//                editForm.setBiayaMerchantDisplay( CommonConst.DISPLAY_TRUE );
//            }else{
//            	editForm.setBiayaMerchantDisplay( CommonConst.DISPLAY_FALSE );
//            }
            if( refundBusiness.isUnitLink( editForm.getSpaj() ) )
            {
                editForm.setHasUnitDisplay( CommonConst.DISPLAY_TRUE );
                if( editForm.getHasUnitFlag() != null && editForm.getHasUnitFlag().equals( 1 ) || refundBusiness.getHasFundAllocation(editForm.getSpaj()) )
                {
                    editForm.setPenarikanUlinkVOListDisplay( CommonConst.DISPLAY_TRUE );
                    editForm.setStatusUnit("sudah");
                    editForm.setHasUnitFlag(1);
                }
                else if(editForm.getHasUnitFlag() != null && editForm.getHasUnitFlag().equals( 0 ) || !refundBusiness.getHasFundAllocation(editForm.getSpaj()))
                {
                	editForm.setStatusUnit("belum");
                	editForm.setHasUnitFlag(0);
                }
            }
            else
            {
            	
            }
            
        }
        else if( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() ) )
        {
        	editForm.setHasUnitDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setSpajBaruDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setBiayaAdminDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setBiayaMedisDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setBiayaLainDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setAtasNamaDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setNorekDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setNamaBankDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setCabangBankDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setKotaBankDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setButtonBatalkanSpajDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setButtonSaveDraftDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setButtonPreviewSuratBatalDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setButtonNextDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setAddLampiranDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setSendPhysicalDocDateDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setPaymentDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setBiayaMerchantDisplay( CommonConst.DISPLAY_FALSE );
            if( refundBusiness.isUnitLink( editForm.getSpaj() ) )
            {
                if( editForm.getHasUnitFlag() != null && editForm.getHasUnitFlag().equals( 1 ) || refundBusiness.getHasFundAllocation(editForm.getSpaj()) )
                {
                	editForm.setHasUnitDisplay( CommonConst.DISPLAY_TRUE );
                    editForm.setPenarikanUlinkVOListDisplay( CommonConst.DISPLAY_TRUE );
                    editForm.setHasUnitFlag(1);
                }
            }
        }
        else if( RefundConst.TINDAKAN_TIDAK_ADA.equals( editForm.getTindakanCd() ) )
        {
            editForm.setSpajBaruDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setBiayaAdminDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setBiayaMedisDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setBiayaLainDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setAtasNamaDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setNorekDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setNamaBankDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setCabangBankDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setKotaBankDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setButtonBatalkanSpajDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setButtonSaveDraftDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setButtonPreviewSuratBatalDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setButtonNextDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setAddLampiranDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setSendPhysicalDocDateDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setPaymentDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setBiayaMerchantDisplay( CommonConst.DISPLAY_FALSE );
        }
        else if( RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() ) )
        {
        	editForm.setHasUnitDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setSpajBaruDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setBiayaAdminDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setBiayaMedisDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setBiayaLainDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setAtasNamaDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setNorekDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setNamaBankDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setCabangBankDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setKotaBankDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setButtonBatalkanSpajDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setButtonSaveDraftDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setButtonPreviewSuratBatalDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setButtonNextDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setAddLampiranDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setSendPhysicalDocDateDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setPaymentDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setBiayaMerchantDisplay( CommonConst.DISPLAY_FALSE );
            if( refundBusiness.isUnitLink( editForm.getSpaj() ) )
            {
                if( editForm.getHasUnitFlag() != null && editForm.getHasUnitFlag().equals( 1 ) || refundBusiness.getHasFundAllocation(editForm.getSpaj()) )
                {
                    editForm.setPenarikanUlinkVOListDisplay( CommonConst.DISPLAY_TRUE );
                    editForm.setHasUnitDisplay( CommonConst.DISPLAY_TRUE );
                    editForm.setHasUnitFlag(1);
                }
            }
        }
        else
        {
            editForm.setSpajBaruDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setBiayaAdminDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setBiayaMedisDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setBiayaLainDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setAtasNamaDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setNorekDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setNamaBankDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setCabangBankDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setKotaBankDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setButtonBatalkanSpajDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setButtonSaveDraftDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setButtonPreviewSuratBatalDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setButtonNextDisplay( CommonConst.DISPLAY_TRUE );
            editForm.setAddLampiranDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setSendPhysicalDocDateDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setPaymentDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setBiayaMerchantDisplay( CommonConst.DISPLAY_FALSE );
        }

        if( editForm.getAlasanCd() != null && editForm.getAlasanCd().equals( RefundConst.ALASAN_LAIN ) )
        {
            editForm.setAlasanDisplay( CommonConst.DISPLAY_TRUE );
        }
        else
        {
            editForm.setAlasanDisplay( CommonConst.DISPLAY_FALSE );
        }

        // mengeset nama pp, tt dan produk tiap kali halaman refresh
        editForm.setPolicyInfoVO( refundBusiness.genPolicyInfoVOBySpaj( editForm.getSpaj() ) );

        if( editForm.getPosisiNo() == null )
        {
            enableAllEditForm( editForm, true );
            editForm.setBiayaAdminIsDisabled( CommonConst.DISABLED_FALSE );
        }
        else if( editForm.getPosisiNo().equals( RefundConst.POSISI_DRAFT ) )
        {
            enableAllEditForm( editForm, true );
            editForm.setSpajIsDisabled( CommonConst.DISABLED_TRUE );
            editForm.setBiayaAdminIsDisabled( CommonConst.DISABLED_FALSE );
        }
        else
        {
            enableAllEditForm( editForm, false );
            editForm.setButtonBatalkanSpajDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setButtonSaveDraftDisplay( CommonConst.DISPLAY_FALSE );
            editForm.setBiayaAdminIsDisabled( CommonConst.DISABLED_TRUE );
        }
        Date checkTglKirimDokFisik = refundBusiness.selectTglKirimDokFisik( editForm.getSpaj() );
        
        if( checkTglKirimDokFisik != null && !"".equals( checkTglKirimDokFisik ))
        {
        	editForm.setSendPhysicalDocIsDisabled( CommonConst.DISABLED_TRUE );
        	editForm.setButtonSendPhysicalDocIsDisabled( CommonConst.DISABLED_TRUE );
        	editForm.setSendPhysicalDocDate( checkTglKirimDokFisik );
        }
        else
        {
        	editForm.setSendPhysicalDocDate( checkTglKirimDokFisik );
        	if(RefundConst.TINDAKAN_REFUND_PREMI.equals( editForm.getTindakanCd() ) )
        	{
        		if( editForm.getPenarikanUlinkVOList() != null && editForm.getPenarikanUlinkVOList().size() > 0 
        				&& editForm.getPosisiNo() != null && editForm.getPosisiNo() >= 3)
        		{
        			editForm.setSendPhysicalDocIsDisabled( CommonConst.DISABLED_FALSE );
        			editForm.setButtonSendPhysicalDocIsDisabled( CommonConst.DISABLED_FALSE );
        		}
        		else if( editForm.getPenarikanUlinkVOList() != null && editForm.getPenarikanUlinkVOList().size() > 0 && editForm.getPosisiNo() == null 
        				|| editForm.getPenarikanUlinkVOList() != null && editForm.getPenarikanUlinkVOList().size() > 0 && editForm.getPosisiNo() != null && editForm.getPosisiNo() <= 2 )
        		{
        			editForm.setSendPhysicalDocIsDisabled( CommonConst.DISABLED_TRUE );
        			editForm.setButtonSendPhysicalDocIsDisabled( CommonConst.DISABLED_TRUE );
        		}
        		else if( editForm.getPenarikanUlinkVOList() == null && editForm.getPosisiNo() == null 
        				|| editForm.getPenarikanUlinkVOList() == null && editForm.getPosisiNo() != null && editForm.getPosisiNo() <= 1 
        				|| editForm.getPenarikanUlinkVOList().size() == 0 && editForm.getPosisiNo() == null 
        				|| editForm.getPenarikanUlinkVOList().size() == 0 && editForm.getPosisiNo() != null && editForm.getPosisiNo() <= 1 )
        		{
        			editForm.setSendPhysicalDocIsDisabled( CommonConst.DISABLED_TRUE );
        			editForm.setButtonSendPhysicalDocIsDisabled( CommonConst.DISABLED_TRUE );
        		}    
        		else if( editForm.getPenarikanUlinkVOList() == null && editForm.getPosisiNo() != null && editForm.getPosisiNo() >= 2 
        				|| editForm.getPenarikanUlinkVOList().size() == 0 && editForm.getPosisiNo() != null && editForm.getPosisiNo() >= 2 )
        		{
        			editForm.setSendPhysicalDocIsDisabled( CommonConst.DISABLED_FALSE );
        			editForm.setButtonSendPhysicalDocIsDisabled( CommonConst.DISABLED_FALSE );
        		} 
        	}
        	else
        	{
        		if( editForm.getPosisiNo() != null && editForm.getPosisiNo() >= 2 )
        		{
        			editForm.setSendPhysicalDocIsDisabled( CommonConst.DISABLED_FALSE );
        			editForm.setButtonSendPhysicalDocIsDisabled( CommonConst.DISABLED_FALSE );
        		}
        		else
        		{
        			editForm.setSendPhysicalDocIsDisabled( CommonConst.DISABLED_TRUE );
        			editForm.setButtonSendPhysicalDocIsDisabled( CommonConst.DISABLED_TRUE );
        		}
        	}
        }
        if( RefundConst.TINDAKAN_REFUND_PREMI.equals( editForm.getTindakanCd() ) )
        {
            if( refundBusiness.isUnitLink( editForm.getSpaj() ) )
            {
//                if( RefundConst.POSISI_REFUND_ULINK_REDEMPT_SENT.equals( editForm.getPosisiNo() ) )
//                {
//                    editForm.setBiayaIsDisabled( CommonConst.DISABLED_FALSE );
//                }
//                else
//                {
            	editForm.setBiayaIsDisabled( CommonConst.DISABLED_TRUE );
//                }
                if( RefundConst.POSISI_REFUND_ULINK_CANCEL.equals( editForm.getPosisiNo() )
                		&& editForm.getTindakanCd().toString().equals(RefundConst.TINDAKAN_REFUND_PREMI.toString() ))                
                {
                    editForm.setPaymentIsDisabled( CommonConst.DISABLED_FALSE );
                }
                else
                {
                    editForm.setPaymentIsDisabled( CommonConst.DISABLED_TRUE );
                }
            }
            else
            {
                if( RefundConst.POSISI_REFUND_NON_ULINK_CANCEL.equals( editForm.getPosisiNo() ) )
                {
                    editForm.setPaymentIsDisabled( CommonConst.DISABLED_FALSE );
                }
                else
                {
                    editForm.setPaymentIsDisabled( CommonConst.DISABLED_TRUE );
                }
            }
            
            if(editForm.getHasUnitFlag()!=null)
            {
            	String statusUnit = "";
	            if(editForm.getHasUnitFlag() == 0) // belum membeli unit
	            {
	            	statusUnit = "belum";
	            }
	            else if(editForm.getHasUnitFlag() == 1) // sudah membeli unit
	            {
	            	statusUnit = "sudah";
	            }
	            editForm.setStatusUnit(statusUnit);
            }
            editForm.setHasUnitListIsDisabled( CommonConst.DISABLED_TRUE );
        }
        else if( RefundConst.TINDAKAN_TIDAK_ADA.equals( editForm.getTindakanCd() ) )
        {
            if( editForm.getPosisiNo() == null || editForm.getPosisiNo() < 2)
            {
            	editForm.setButtonPreviewSuratBatalIsDisabled( CommonConst.DISABLED_TRUE );
            }
            else
            {
            	List<SetoranPremiDbVO> setoranPremiBySpaj = refundBusiness.selectSetoranPremiBySpaj( editForm.getSpaj() );
            	if( setoranPremiBySpaj != null && setoranPremiBySpaj.size() > 0 )
            	{
            		editForm.setButtonPreviewSuratBatalIsDisabled( CommonConst.DISABLED_FALSE );
            	}
            	else if( setoranPremiBySpaj == null || setoranPremiBySpaj.size() == 0)
            	{
            		editForm.setButtonPreviewSuratBatalIsDisabled( CommonConst.DISABLED_TRUE );
            	}
            }
        }
        else if( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() ) )
        {
            if( refundBusiness.isUnitLink( editForm.getSpaj() ) )
            {
//                if( editForm.getHasUnitFlag() != null && RefundConst.UNIT_FLAG.equals( editForm.getHasUnitFlag() ) 
//                	 && RefundConst.POSISI_GANTI_PLAN_ULINK_REDEMPT_SEND.equals( editForm.getPosisiNo() ) )
//                {
//                    editForm.setBiayaIsDisabled( CommonConst.DISABLED_FALSE );
//                }
//                else
//                {
                    editForm.setBiayaIsDisabled( CommonConst.DISABLED_TRUE );
//                }
            }
        	editForm.setHasUnitListIsDisabled( CommonConst.DISABLED_TRUE );
        }
        else if( RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() ) )
        {
            if( refundBusiness.isUnitLink( editForm.getSpaj() ) )
            {
//                if( editForm.getHasUnitFlag() != null && RefundConst.UNIT_FLAG.equals( editForm.getHasUnitFlag() ) 
//                	 && RefundConst.POSISI_GANTI_PLAN_ULINK_REDEMPT_SEND.equals( editForm.getPosisiNo() ) )
//                {
//                    editForm.setBiayaIsDisabled( CommonConst.DISABLED_FALSE );
//                }
//                else
//                {
                    editForm.setBiayaIsDisabled( CommonConst.DISABLED_TRUE );
//                }
            }
        	editForm.setHasUnitListIsDisabled( CommonConst.DISABLED_TRUE );
        }

        // set biaya admin
//        editForm.setBiayaAdminIsDisabled( CommonConst.DISABLED_TRUE );
        CheckSpajParamsVO spajAlreadyCancelMstRefund = refundBusiness.selectSpajAlreadyCancelMstRefund( editForm.getSpaj() );
        
        if( spajAlreadyCancelMstRefund != null && !"".equals( spajAlreadyCancelMstRefund  ) )
        {
	       	 editForm.setNorekIsDisabled( CommonConst.DISABLED_TRUE);
	    	 editForm.setAtasNamaIsDisabled( CommonConst.DISABLED_TRUE );
	         editForm.setNamaBankIsDisabled( CommonConst.DISABLED_TRUE );
	         editForm.setCabangBankIsDisabled( CommonConst.DISABLED_TRUE );
	         editForm.setKotaBankIsDisabled( CommonConst.DISABLED_TRUE );
        }
        else
        {
      	  editForm.setNorekIsDisabled( CommonConst.DISABLED_FALSE);
    	  editForm.setAtasNamaIsDisabled( CommonConst.DISABLED_FALSE );
          editForm.setNamaBankIsDisabled( CommonConst.DISABLED_FALSE );
          editForm.setCabangBankIsDisabled( CommonConst.DISABLED_FALSE );
          editForm.setKotaBankIsDisabled( CommonConst.DISABLED_FALSE );
        }

        if( editForm.getPosisiNo() == null || editForm.getPosisiNo() <= RefundConst.POSISI_DRAFT )
        {
        	/*
        	BigDecimal biayaAdmin = new BigDecimal( "50000" );
        	if(editForm.getPolicyInfoVO() !=null)
        	{
	        	if(RefundConst.KURS_RUPIAH.equals(editForm.getPolicyInfoVO().getLkuId()))
	        	{
	        		 biayaAdmin = new BigDecimal( "50000" );
	        	}
	        	else if(RefundConst.KURS_DOLLAR.equals(editForm.getPolicyInfoVO().getLkuId()))
	        	{
	        		 biayaAdmin = new BigDecimal( "5" );
	        	}
        	}
        	*/
            PolicyInfoVO policyInfoVO = editForm.getPolicyInfoVO();
            /*
            if( policyInfoVO != null )
            {

                if( policyInfoVO.getPolicyNo() == null || policyInfoVO.getPolicyNo().trim().equals( "" ) )
                {
                    biayaAdmin = new BigDecimal( "0" );
                }
                // Jika stable link biayanya 100 rebu
//                else if( editForm.getPolicyInfoVO().getLsbsId().equals( 164 ) )
//                {
//                    biayaAdmin = new BigDecimal( "100000" );
//                }
            }

            editForm.setBiayaAdmin( biayaAdmin );
*/
            if( "onChangeTindakanCd".equals( theEvent ) || "onBlurSpaj".equals( theEvent ) || "onPressImageEdit".equals( theEvent ) )
            {
            	if(editForm.getHelpCompareSpaj()!=null)
            	{
	            	if(!editForm.getHelpCompareSpaj().trim().equals(editForm.getSpaj().trim()))
	            	{
		                editForm.setHelpFlagCompareSpaj(null);
	            	}
            	}
            }
            if( editForm.getHelpFlagCompareSpaj() == null )
            {
            	editForm.setHelpCompareSpaj(editForm.getSpaj());
	            if( policyInfoVO != null )
	            {
	                editForm.setAtasNama( policyInfoVO.getKliAtasNama() );
	                editForm.setNorek( policyInfoVO.getKliNoRek() );
	                editForm.setNamaBank( policyInfoVO.getKliNamaBank());
	                editForm.setCabangBank( policyInfoVO.getKliCabangBank() );
	                editForm.setKotaBank( policyInfoVO.getKliKotaBank() );
	                editForm.setHelpFlagCompareSpaj(1);
	            }
            }
        }
    }
    

    public void refreshPenarikanList( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundEditHelper.refreshPenarikanList" );
        
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        if(editForm.getTempSpaj()!=null && !"".equals(editForm.getTempSpaj()))
	    {
	        if(!editForm.getTempSpaj().trim().equals(editForm.getSpaj().trim()))
	        {
	        	editForm.setFlagHelp(null);
	        	editForm.setSetoranPokokAndTopUpFlag( null );
	        }
	    }
        ArrayList<PenarikanUlinkVO> selectPenarikanUlink = Common.serializableList(refundBusiness.selectPenarikanUlinkLamp1( editForm.getSpaj() ));
        editForm.setPenarikanUlinkVOList( selectPenarikanUlink );
        if( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() ) || RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() ) )
        {
        	if( refundBusiness.isUnitLink( editForm.getSpaj() ) )
        	{
	        	RefundDbVO refundDb = refundBusiness.selectRefundByCd( editForm.getSpaj() );
	        	if( refundDb != null && !"".equals( refundDb ) )
	        	{
//	        		if( refundDb.getHasUnitFlag() != null && RefundConst.UNIT_FLAG.equals( refundDb.getHasUnitFlag() ) )
//	        		{
//	        			editForm.setHasUnitFlag( RefundConst.UNIT_FLAG );
//	        		}
//	        		else if( refundDb.getHasUnitFlag() != null && RefundConst.NON_UNIT_FLAG.equals( refundDb.getHasUnitFlag() ) )
//	        		{
//	        			editForm.setHasUnitFlag( RefundConst.NON_UNIT_FLAG );
//	        		}
//	        		else if( refundDb.getHasUnitFlag() == null )
//	        		{
//	        			editForm.setHasUnitFlag( null );
//	        		}
	        	}
	        	else
	        	{
	            	if( selectPenarikanUlink != null && selectPenarikanUlink.size() > 0 )
	            	{
	            		BigDecimal tempSumUnit = BigDecimal.ZERO;
	            		for( PenarikanUlinkVO vo : selectPenarikanUlink)
	            		{
	            			tempSumUnit = tempSumUnit.add( vo.getJumlahUnit() );
	            		}
	            		
	            		if( tempSumUnit != null && tempSumUnit.intValue() > 0 ) // jika jumlah unit nya lbh besar dari 0
	            		{
	            			editForm.setHasUnitFlag( RefundConst.UNIT_FLAG );
	            		}
	            		else  // jika jumlah unit null / 0
	            		{
	            			editForm.setHasUnitFlag( RefundConst.NON_UNIT_FLAG );
	            		}
	            	}
	            	else
	            	{
	            		editForm.setHasUnitFlag( RefundConst.NON_UNIT_FLAG );
	            	}
	        	}
        	}
        }
        
//        if(editForm.getTempDescrDanJumlahNew()!=null &&  editForm.getTempDescrDanJumlahNew().size() > 0)
//        {
//        	editForm.getTempDescrDanJumlahNew().clear();
//        	editForm.setTempDescrDanJumlahNew(null);
//        }
    }
    
    public void clearListDescrAndJumlah( RefundEditForm editForm )
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundEditHelper.clearListDescrAndJumlah" );
    	if(editForm.getLampiranAddList() != null && editForm.getLampiranAddList().size() > 0)
    	{
    		editForm.getLampiranAddList().clear();
    		editForm.setLampiranAddList(null);
    		editForm.setAddLampiran(null);
    	}
    	if(editForm.getTempDescrDanJumlah()!=null && editForm.getTempDescrDanJumlah().size() >0)
    	{
    		editForm.getTempDescrDanJumlah().clear();
    		editForm.setTempDescrDanJumlah(null);
    	}
       	if(editForm.getPenarikanUlinkVOList()!=null && editForm.getPenarikanUlinkVOList().size() >0)
    	{
    		editForm.getPenarikanUlinkVOList().clear();
    		editForm.setPenarikanUlinkVOList(null);
    	}
       	if( editForm.getULinkOrNot()!=null && !"".equals(editForm.getULinkOrNot()))
       	{
       		editForm.setULinkOrNot(null);
       	}
       	if( editForm.getStatusUnit() != null && !"".equals(editForm.getStatusUnit() ) )
       	{
       		editForm.setStatusUnit(null);
       	}
    }
    
    
    public void onChangeAlasanCd( RefundEditForm editForm )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundEditHelper.onChangeAlasanCd" );
        if( editForm.getAlasanVOList() != null && !"".equals( editForm.getAlasanVOList() ) )
        {
        	ArrayList < DropDown > tempTindakan = new ArrayList<DropDown>();
        	tempTindakan.add( new DropDown ("","") );
        	for (int i = 0 ; i < editForm.getAlasanVOList().size() ; i ++)
        	{
        		if( editForm.getAlasanCd() != null && editForm.getAlasanCd().equals( editForm.getAlasanVOList().get( i ).getAlasanCd() ) )
        		{
        			for (int j = 0 ; j < editForm.getAlasanVOList().get( i ).getTindakanList().size() ; j ++ )
        			{
        				tempTindakan.add( new DropDown ( editForm.getAlasanVOList().get( i ).getTindakanList().get( j ).getKey(),  editForm.getAlasanVOList().get( i ).getTindakanList().get( j ).getValue()) );        				
        			}
        		}
        	}
        	editForm.setTindakanList( tempTindakan );     
        }
    }
    
    
    public void fillTindakanListAndAlasanList( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundEditHelper.fillTindakanListAndAlasanList" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        if(  editForm.getTindakanListIsDisabled() == CommonConst.DISABLED_TRUE  && editForm.getAlasanListIsDisabled() == CommonConst.DISABLED_TRUE )
        {
        	editForm.setAlasanList( refundBusiness.getAlasanAllList() );
        	editForm.setTindakanList( refundBusiness.getTindakanAllList() );
        }
        editForm.setMerchantList( refundBusiness.getMerchantAllList() );
        
    }
    
    public void onPressImageEdit( HttpServletRequest request, Object command, String selectedRowSpaj )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundEditHelper.onPressImageEdit" );

        request.setAttribute( "pageSubTitle",  " --> Edit".toString());

        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        
        RefundDbVO refundDbVO = refundBusiness.selectRefundByCd( selectedRowSpaj );
        String createWhoFullName = refundBusiness.selectLusFullName(refundDbVO.getCreateWho());
        String updateWhoFullName = refundBusiness.selectLusFullName(refundDbVO.getUpdateWho());
        if( refundDbVO != null && refundDbVO.getPosisiNo() == null || refundDbVO != null && refundDbVO.getPosisiNo() == 0 )
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
        //MANTA (21/11/2013)
        if(editForm.getSpajBaru() != null && !"".equals(editForm.getSpajBaru())){
	    	PolicyInfoVO policyInfoVONew = refundBusiness.selectPolicyInfoBySpaj(editForm.getSpajBaru().toString());
	    	editForm.setPPBaru(policyInfoVONew.getNamaPp());
	    	editForm.setTTBaru(policyInfoVONew.getNamaTt());
	    	editForm.setProdukBaru(policyInfoVONew.getNamaProduk());
        }
    	
        editForm.setTindakanCd( refundDbVO.getTindakanCd() );
        editForm.setHasUnitFlag( refundDbVO.getHasUnitFlag() );
        
        // biaya2
        HashMap biayaMap = refundBusiness.retrieveBiaya2( refundDbVO.getSpajNo() );
        BigDecimal biayaMerchant = ( BigDecimal ) biayaMap.get( "biayaMerchant" );
        BigDecimal biayaAdmin = ( BigDecimal ) biayaMap.get( "biayaAdmin" );
        BigDecimal biayaMedis = ( BigDecimal ) biayaMap.get( "biayaMedis" );
        BigDecimal biayaLain = ( BigDecimal ) biayaMap.get( "biayaLain" );
        String biayaLainDescr = ( String ) biayaMap.get( "biayaLainDescr" );
        
        editForm.setBiayaMerchant( biayaMerchant );
        editForm.setBiayaAdmin( biayaAdmin );
        editForm.setBiayaMedis( biayaMedis );
        editForm.setBiayaLain( biayaLain );
        editForm.setBiayaLainDescr( biayaLainDescr );
        editForm.setPenarikanUlinkVOList( refundBusiness.selectPenarikanUlinkVOListBySpaj( editForm.getSpaj().trim() ) );

        BigDecimal totalPayment = new BigDecimal(0);
        BigDecimal percentMerchant = new BigDecimal(0);
        ArrayList<SetoranPremiDbVO> setoranPremiBySpaj = refundBusiness.selectSetoranPremiBySpaj( refundDbVO.getSpajNo() );
        if(!setoranPremiBySpaj.isEmpty()){
        	for(SetoranPremiDbVO vo : setoranPremiBySpaj){
        		totalPayment = totalPayment.add(vo.getJumlah());
        	}
        }
        if(biayaMerchant != null && biayaMerchant != new BigDecimal(0)){
        	percentMerchant = biayaMerchant.divide((totalPayment.add(biayaMerchant)), 3, RoundingMode.HALF_UP);
        	if(percentMerchant.doubleValue() == new Double(0.018)){
        		editForm.setMerchantCd(1);
        	}else if(percentMerchant.doubleValue() == new Double(0.009)){
        		editForm.setMerchantCd(2);
        	}else if(percentMerchant.doubleValue() == new Double(0.025)){
        		editForm.setMerchantCd(3);
        	}else{
        		editForm.setMerchantCd(0);
        	}
        }
        
        ArrayList<LampiranListVO> lampiranTambahan = refundBusiness.selectMstDetRefundLamp( refundDbVO.getSpajNo() );
        if( lampiranTambahan != null && lampiranTambahan.size()>0 )
        {
        	editForm.setLampiranAddList( lampiranTambahan );
        }
        
        // kalau belum di bikin surat redempt, maka ga bisa isi jumlah redemptionnya
        String biayaStatus;
//        if( refundDbVO.getPosisiNo() < 2 )
        biayaStatus = CommonConst.DISABLED_TRUE;
//        else
//            biayaStatus = CommonConst.DISABLED_FALSE;

        editForm.setBiayaIsDisabled( biayaStatus );

        editForm.setPayment( refundDbVO.getPayment() );
        editForm.setPaymentDate( refundDbVO.getPaymentDate() );
        editForm.setCreateWhen( refundDbVO.getCreateWhen() );
        editForm.setUpdateWhen( refundDbVO.getUpdateWhen() );
        editForm.setCancelWho( refundDbVO.getCancelWho() );
        editForm.setCancelWhen( refundDbVO.getCancelWhen() );
        editForm.setCreateWho( refundDbVO.getCreateWho() );
        editForm.setUpdateWho( refundDbVO.getUpdateWho() );
    }

    public void onPressLinkAdd( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundEditHelper.onPressLinkAdd" );

        request.setAttribute( "pageSubTitle", " --> Add New".toString() );
        RefundEditForm editForm = intializeEditForm();
        enableAllEditForm( editForm, true );
        editForm.setBiayaIsDisabled( CommonConst.DISABLED_TRUE );

        RefundForm refundForm = ( RefundForm ) command;
        refundForm.setEditForm( editForm );
    }

    public boolean saveDraftTindakanTidakAda( HttpServletRequest request, Object command, String actionMessage )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundEditHelper.saveDraftTindakanTidakAda" );

        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        MstRefundParamsVO paramsVO = new MstRefundParamsVO();
        String theEvent = ( ( RefundForm ) command ).getTheEvent();
        paramsVO.setSpajNo( editForm.getSpaj() );
        paramsVO.setAlasanCd( editForm.getAlasanCd() );
        paramsVO.setAlasanLain( genAlasan( editForm ) );
        paramsVO.setAlasanLainForLabel( genAlasanForLabel( editForm ) );
        paramsVO.setTindakanCd( editForm.getTindakanCd() );
        paramsVO.setAddLampiranList( editForm.getLampiranAddList() );

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
        	setoranPremiDbVOList = Common.serializableList(refundBusiness.selectSetoranPremiBySpaj(editForm.getSpaj()));
        	biayaUlinkDbVOList = refundBusiness.selectBiayaUlink(editForm.getSpaj());
        }
        paramsVO.setSetoranPremiDbVOList( setoranPremiDbVOList );
        paramsVO.setBiayaUlinkDbVOList( biayaUlinkDbVOList );
        paramsVO.setHasUnitFlag( editForm.getHasUnitFlag() );
        paramsVO.setPrevLspdId( editForm.getPrevLspdId() );
        
        HttpSession session = request.getSession();
        User currentUser = ( User ) session.getAttribute( "currentUser" );

        Integer ulinkFlag = refundBusiness.isUnitLink( paramsVO.getSpajNo() )? 1 : 0;
        Integer spajAlreadyInDetRefundOrNot = refundBusiness.selectCheckSpaj( editForm.getSpaj() );
        editForm.setCheckSpajInDetRefund( spajAlreadyInDetRefundOrNot ) ;
        paramsVO.setUlinkFlag( ulinkFlag );

        paramsVO.setCreateWhen( editForm.getCreateWhen() == null? new Date() : editForm.getCreateWhen() );
        paramsVO.setUpdateWhen( new Date() );
        paramsVO.setCreateWho( editForm.getCreateWho() == null? new BigDecimal( currentUser.getLus_id() ) : editForm.getCreateWho() );
        paramsVO.setUpdateWho( new BigDecimal( currentUser.getLus_id() ) );
//        if( "onPressButtonBatalkanSpaj".equals( theEvent ) || "onPressBatalSpaj".equals( theEvent ) )
//        {
//        	paramsVO.setCancelWhen( editForm.getCancelWhen() == null? new Date() : editForm.getCancelWhen() );
//        	paramsVO.setCancelWho( editForm.getCancelWho() == null? new BigDecimal( currentUser.getLus_id() ) : editForm.getCancelWho() );
//        }
        // karena posisi penting, selalu ditaruh sebagai parameter yg terakhir
        paramsVO.setPosisi( RefundConst.POSISI_DRAFT );
        
        HashMap<String, Object> resultMap = refundBusiness.deleteThenInsertRefund( paramsVO, actionMessage, editForm, theEvent, editForm.getHasUnitFlag(), editForm.getTindakanCd() );
        request.setAttribute( "pageMessage", resultMap.get( "pageMessage" ) );

        Boolean result = ( Boolean ) resultMap.get( "succeed" );
        if( result )
        {
            editForm.setPosisiNo( paramsVO.getPosisi() );
        }
        return result;
    }

    public void generateDocumentRefundEdit( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundEditHelper.generateDocumentRefundEdit" );
        RefundDocumentVO refundDocumentVO = new RefundDocumentVO();
        refundDocumentVO.setDownloadUrlSession( "refund/download_refund_document.htm" );      
        refundDocumentVO.setJasperFile( "com/ekalife/elions/reports/refund/lamp3_surat_batal_tanpa_tindakan.jasper" );
        refundDocumentVO.setParams( genParamsTindakanTidakAdaPdf( command ) );
        RefundForm refundForm = ( RefundForm ) command;
        refundForm.setRefundDocumentVO( refundDocumentVO );
    }
    
    private RefundEditParamsVO prepareRefundEditParamsVO( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundEditHelper.prepareRefundEditParamsVO" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        RefundEditParamsVO paramsVO = new RefundEditParamsVO();
        String spaj =  null;
        if( editForm.getSpaj() != null)
        {
        	spaj = editForm.getSpaj().trim();
        }
        paramsVO.setSpajNo( spaj );
        paramsVO.setAlasan( genAlasan( editForm ) );
        paramsVO.setAlasanForLabel(genAlasanForLabel(editForm));
        
        return paramsVO;
    }
    
    
    public HashMap<String, Object> genParamsTindakanTidakAdaPdf( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundEditHelper.genParamsTindakanTidakAdaPdf" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        RefundEditParamsVO paramsVO = prepareRefundEditParamsVO( command );
        
        // ini adalah bagian inti dari memproses report
        RefundEditVO refundEditVO = refundBusiness.retrieveRefundEdit( paramsVO, editForm );
        CheckSpajParamsVO checkSpajInDb = refundBusiness.selectCheckSpajInDb( refundEditVO.getSpajNo() );
        HashMap<String, Object> params = new HashMap<String, Object>();
        String statementAvailableOrNot = null;
        String statementLamp1AvailableOrNot = null;
        String statementLamp2AvailableOrNot = null;
        if(refundEditVO.getStatementLamp1() != null )
        {
        	statementLamp1AvailableOrNot = "available";
        }
        if(refundEditVO.getStatementLamp1() == null && refundEditVO.getStatementLamp2() == null && editForm.getLampiranAddList() == null ||
        		refundEditVO.getStatementLamp1() == null && refundEditVO.getStatementLamp2() == null && editForm.getLampiranAddList()!= null && editForm.getLampiranAddList().size() == 0 )
        {
        	statementAvailableOrNot = null;
        }
        else
        {
        	statementAvailableOrNot = "available";
        }
//        
        // default for report
        params.put( "format", "pdf" );
        params.put( "logoPath", "com/ekalife/elions/reports/refund/images/logo_ajs.gif" );

        params.put( "statementLamp1AvailableOrNot", statementLamp1AvailableOrNot );
        params.put( "statementLamp2AvailableOrNot", statementLamp2AvailableOrNot );
        params.put( "hal", refundEditVO.getHal() );
        params.put( "insuredName", refundEditVO.getInsuredName() );
        params.put( "noUrutMemo", refundEditVO.getNoUrutMemo() );
        params.put( "policyHolderName", refundEditVO.getPolicyHolderName() );
        params.put( "policyNo", refundEditVO.getPolicyNo() );
        params.put( "productName", refundEditVO.getProductName() );
        params.put( "signer", refundEditVO.getSigner() );
        params.put( "spajNo", refundEditVO.getSpajNo() );
        params.put( "statement", refundEditVO.getStatement() );
        params.put( "statementLamp1", refundEditVO.getStatementLamp1() );
        params.put( "statementLamp2", refundEditVO.getStatementLamp2() );
        params.put( "tanggal", refundEditVO.getTanggal() );
        params.put( "statementAvailableOrNot", statementAvailableOrNot );
        params.put( "efektifPolis", FormatDate.toIndonesian( refundEditVO.getBegDate() )+ " - " + FormatDate.toIndonesian( refundEditVO.getEndDate() ) );
        params.put( "dataSource", refundEditVO.getRincianPolisList() );
        
//        if( checkSpajInDb != null && !"".equals( checkSpajInDb ) && checkSpajInDb.getNoSurat() != null && !"".equals( checkSpajInDb.getNoSurat() ) )// jika no_surat sudah ada di DB
//        {
//        	 params.put( "noSurat", checkSpajInDb.getNoSurat() );
//        }
//        else if( checkSpajInDb == null || "".equals( checkSpajInDb )) // jika no_surat blm ada di DB
//        {
//        	 String noSurat = refundBusiness.retrieveNoSurat( refundEditVO.getSpajNo(), editForm.getTindakanCd() );
//        	 params.put( "noSurat", noSurat );
//        }
//        else
//        {
//        	 params.put( "noSurat", "" );
//        }
        
        return params;
    }
    
    public void onChangeMerchantCd( RefundEditForm editForm )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundEditHelper.onChangeMerchantCd" );
        ArrayList<SetoranPremiDbVO> setoranPremiBySpaj = refundBusiness.selectSetoranPremiBySpaj( editForm.getSpaj() );
    	
        BigDecimal jmlbayar = new BigDecimal(0);
        BigDecimal percent = new BigDecimal(0);
        BigDecimal percent2 = new BigDecimal(1);
        if(!setoranPremiBySpaj.isEmpty()){
    		for(SetoranPremiDbVO vo : setoranPremiBySpaj){
    			jmlbayar = jmlbayar.add(vo.getJumlah());
    		}
    		
    		if(editForm.getMerchantCd() == 1){//BNI
            	percent = new BigDecimal(0.018);
            	percent2 = new BigDecimal(0.982);
            }else if(editForm.getMerchantCd() == 2){//BCA
            	percent = new BigDecimal(0.009);
            	percent2 = new BigDecimal(0.991);
            }else if(editForm.getMerchantCd() == 3){//VisaMaster
            	percent = new BigDecimal(0.025);
            	percent2 = new BigDecimal(0.975);
            }
    	}
		editForm.setBiayaMerchant( (jmlbayar.multiply(percent)).divide(percent2, 0, RoundingMode.HALF_UP) );
    }
}
