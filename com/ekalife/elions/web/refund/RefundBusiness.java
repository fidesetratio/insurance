package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundBusiness
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Sep 23, 2008 2:42:26 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.util.StringUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.User;
import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.model.refund.RefundLookupForm;
import com.ekalife.elions.model.sms.Smsserver_out;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.elions.web.refund.vo.AlasanVO;
import com.ekalife.elions.web.refund.vo.BatalParamsVO;
import com.ekalife.elions.web.refund.vo.BiayaUlinkDbVO;
import com.ekalife.elions.web.refund.vo.CheckSpajParamsVO;
import com.ekalife.elions.web.refund.vo.GbrA1ParamsVO;
import com.ekalife.elions.web.refund.vo.GbrA1VO;
import com.ekalife.elions.web.refund.vo.GbrA2ParamsVO;
import com.ekalife.elions.web.refund.vo.GbrA2VO;
import com.ekalife.elions.web.refund.vo.GbrA3ParamsVO;
import com.ekalife.elions.web.refund.vo.GbrA3VO;
import com.ekalife.elions.web.refund.vo.GbrA4ParamsVO;
import com.ekalife.elions.web.refund.vo.GbrA4VO;
import com.ekalife.elions.web.refund.vo.GbrB1ParamsVO;
import com.ekalife.elions.web.refund.vo.GbrB1VO;
import com.ekalife.elions.web.refund.vo.GbrB2ParamsVO;
import com.ekalife.elions.web.refund.vo.GbrB2VO;
import com.ekalife.elions.web.refund.vo.GbrB3ParamsVO;
import com.ekalife.elions.web.refund.vo.GbrB3VO;
import com.ekalife.elions.web.refund.vo.Lamp1ParamsVO;
import com.ekalife.elions.web.refund.vo.Lamp1VO;
import com.ekalife.elions.web.refund.vo.Lamp3ParamsVO;
import com.ekalife.elions.web.refund.vo.Lamp3VO;
import com.ekalife.elions.web.refund.vo.LampiranListVO;
import com.ekalife.elions.web.refund.vo.MstBatalParamsVO;
import com.ekalife.elions.web.refund.vo.MstRefundParamsVO;
import com.ekalife.elions.web.refund.vo.PenarikanUlinkDbVO;
import com.ekalife.elions.web.refund.vo.PenarikanUlinkVO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.RedemptParamsVO;
import com.ekalife.elions.web.refund.vo.RedemptVO;
import com.ekalife.elions.web.refund.vo.RefundDbVO;
import com.ekalife.elions.web.refund.vo.RefundDetDbVO;
import com.ekalife.elions.web.refund.vo.RefundEditParamsVO;
import com.ekalife.elions.web.refund.vo.RefundEditVO;
import com.ekalife.elions.web.refund.vo.RefundViewVO;
import com.ekalife.elions.web.refund.vo.SetoranPokokDanTopUpVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;
import com.ekalife.elions.web.refund.vo.SetoranVO;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatString;

// this is business logic tier

public class RefundBusiness implements Serializable
{
    

	/**
	 *@author Deddy
	 *@since Mar 31, 2015
	 *@description TODO 
	 */
	private static final long serialVersionUID = -6370065015911318829L;

	protected final Log logger = LogFactory.getLog( getClass() );

    private ElionsManager elionsManager;
    private BacManager bacManager;
    private RefundLamp1Business lamp1Business;
    private RefundGbrA1Business gbrA1Business;
    private RefundGbrA2Business gbrA2Business;
    private RefundGbrA3Business gbrA3Business;
    private RefundGbrA4Business gbrA4Business;
    private RefundGbrB1Business gbrB1Business;
    private RefundGbrB2Business gbrB2Business;
    private RefundGbrB3Business gbrB3Business;
    private RefundLamp3Business lamp3Business;
    private RefundLookupBusiness lookupBusiness;
    private RefundEditBusiness editBusiness;
    private RefundRedemptBusiness redemptBusiness;
    private RefundCommonBusiness commonBusiness;

    public BacManager getBacManager() {
		return bacManager;
	}

	public void setBacManager(BacManager bacManager) {
		this.bacManager = bacManager;
	}
    
    public void setElionsManager( ElionsManager elionsManager )
    {
        this.elionsManager = elionsManager;
    }

	public ElionsManager getElionsManager()
    {
        return elionsManager;
    }

    public void setLamp1Business( RefundLamp1Business lamp1Business )
    {
        this.lamp1Business = lamp1Business;
    }

    public void setGbrA1Business(RefundGbrA1Business gbrA1Business) {
		this.gbrA1Business = gbrA1Business;
	}

	public void setGbrA2Business(RefundGbrA2Business gbrA2Business) {
		this.gbrA2Business = gbrA2Business;
	}

	public void setGbrA3Business(RefundGbrA3Business gbrA3Business) {
		this.gbrA3Business = gbrA3Business;
	}

	public void setGbrA4Business(RefundGbrA4Business gbrA4Business) {
		this.gbrA4Business = gbrA4Business;
	}

	public void setGbrB1Business(RefundGbrB1Business gbrB1Business) {
		this.gbrB1Business = gbrB1Business;
	}

	public void setGbrB2Business(RefundGbrB2Business gbrB2Business) {
		this.gbrB2Business = gbrB2Business;
	}

	public void setGbrB3Business(RefundGbrB3Business gbrB3Business) {
		this.gbrB3Business = gbrB3Business;
	}

	public RefundLamp3Business getLamp3Business()
    {
        return lamp3Business;
    }

    public void setLamp3Business( RefundLamp3Business lamp3Business )
    {
        this.lamp3Business = lamp3Business;
    }

    public void setLookupBusiness( RefundLookupBusiness lookupBusiness )
    {
        this.lookupBusiness = lookupBusiness;
    }

    public void setEditBusiness( RefundEditBusiness editBusiness )
    {
        this.editBusiness = editBusiness;
    }

    public void setRedemptBusiness( RefundRedemptBusiness redemptBusiness )
    {
        this.redemptBusiness = redemptBusiness;
    }

    public RefundBusiness()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundBusiness constructor is called ..." );
    }

    public RefundBusiness( ElionsManager elionsManager )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundBusiness constructor is called ..." );
        this.lamp1Business = new RefundLamp1Business( elionsManager );
        this.lamp3Business = new RefundLamp3Business( elionsManager );
        this.lookupBusiness = new RefundLookupBusiness( elionsManager );
        this.editBusiness = new RefundEditBusiness( elionsManager );
        this.redemptBusiness = new RefundRedemptBusiness( elionsManager );
        this.commonBusiness = new RefundCommonBusiness( elionsManager );
    }

    public RefundCommonBusiness getCommonBusiness()
    {
        return commonBusiness;
    }

    public void setCommonBusiness( RefundCommonBusiness commonBusiness )
    {
        this.commonBusiness = commonBusiness;
    }

    public String getNowStr()
    {
        Date now = new Date();
        DateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
		return dateFormat.format( now );
    }

    public PolicyInfoVO genPolicyInfoVOBySpaj( String spajNo )
    {
        PolicyInfoVO policyInfoVO = elionsManager.selectPolicyInfoBySpaj( spajNo );
        if( policyInfoVO != null )
        {
            policyInfoVO.setSpajNo( FormatString.nomorSPAJ( policyInfoVO.getSpajNo() ) );
            policyInfoVO.setPolicyNo( FormatString.nomorPolis( policyInfoVO.getPolicyNo() ) );
        }
        return policyInfoVO;
    }

    public ArrayList<PenarikanUlinkVO> selectPenarikanUlinkLamp1( String spajNo )
    {
        return selectPenarikanUlink( spajNo );
    }

    public Lamp1VO retrievePreviewEdit( Lamp1ParamsVO paramsVO, RefundEditForm editForm )
    {
        return lamp1Business.retrievePreviewEdit( paramsVO, editForm );
    }
    
    public GbrA1VO retrieveGbrA1EditDetail( GbrA1ParamsVO paramsVO, RefundEditForm editForm )
    {
        return gbrA1Business.retrieveGbrA1EditDetail( paramsVO, editForm );
    }
    
    public void setSumMtuUnit( RefundEditForm editForm )
    {
    	String sumMtuUnitStr = elionsManager.selectMtuUnitTransUlink( editForm.getSpaj() );
    	BigDecimal sumMtuiUnit;
    	if( sumMtuUnitStr != null && !"".equals( sumMtuUnitStr ) ){ sumMtuiUnit = new BigDecimal( sumMtuUnitStr ); }
    	else { sumMtuiUnit = BigDecimal.ZERO; }
    	
    	editForm.setMtuUnit( sumMtuiUnit );
    }
    
    public ArrayList<PenarikanUlinkVO> selectPenarikanUlink( String spajNo )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Business.selectPenarikanUlinkLamp1" );

        if(spajNo!=null)
        {
        	spajNo = spajNo.trim();
        }
        ArrayList<PenarikanUlinkVO> result = new ArrayList<PenarikanUlinkVO>();

        PenarikanUlinkVO vo;
        ArrayList<PenarikanUlinkDbVO> dbResult;
        if( spajNo != null )
        {
            dbResult = Common.serializableList(elionsManager.selectPenarikanUlink( spajNo ));
            for( PenarikanUlinkDbVO dbVO : dbResult )
            {
            	if( dbVO.getJumlahUnit() != null && 
            		!dbVO.getJumlahUnit().equals( BigDecimal.ZERO ) )
            	{	
            		vo = new PenarikanUlinkVO();
                	vo.setJumlahUnit( dbVO.getJumlahUnit() );
                	vo.setLjiInvest(  dbVO.getLjiInvest() );
                	vo.setDeskripsiPenarikan( "Penarikan " + StringUtil.formatCurrency( "", dbVO.getJumlahUnit() ) + " unit " + dbVO.getLjiInvest() );
                	vo.setLjiId( dbVO.getLjiId() );
                	result.add( vo );
            	}
            }
        }

        return result;
    }
    
    public HashMap<String, String> getJumlahPremiDikembalikan( RefundEditForm editForm, String spaj, String theEvent )
    {
    	 if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Business.getJumlahPremiDikembalikan" );
    	 HashMap<String, String> map = new HashMap<String, String>();;
    	 if(spaj!=null && !"".equals(spaj))
    	 {
    		 BigDecimal biayaAdmin = BigDecimal.ZERO;
	    	 BigDecimal premiDikembalikan = BigDecimal.ZERO;
	    	 String spajNo = spaj;
	    	 BigDecimal rowJumlah;
	    	 ArrayList<SetoranPremiDbVO> setoranPremiDbVOList = Common.serializableList(elionsManager.selectSetoranPremiBySpaj( spajNo ));
	    	 PolicyInfoVO policyInfoVO = elionsManager.selectPolicyInfoBySpaj( spajNo );
	    	 
	    	 if( policyInfoVO != null )
	    	 {
	    		if(editForm.getBiayaAdmin()==null || BigDecimal.ZERO.equals(editForm.getBiayaAdmin()))
	    		{
	    			/*
			        if(RefundConst.KURS_RUPIAH.equals(policyInfoVO.getLkuId()))
			        {
			        	 biayaAdmin = new BigDecimal( "50000" );
			        }
			        else if(RefundConst.KURS_DOLLAR.equals(policyInfoVO.getLkuId()))
			        {
			        	 biayaAdmin = new BigDecimal( "5" );
			        }
			        */
	                if( policyInfoVO.getPolicyNo() == null || policyInfoVO.getPolicyNo().trim().equals( "" )
	                		|| policyInfoVO.getPolicyNo() != null && !policyInfoVO.getPolicyNo().trim().equals( "" ) && policyInfoVO.getLspdId() != null && policyInfoVO.getLspdId() < 6
	                		|| policyInfoVO.getPolicyNo() != null && !policyInfoVO.getPolicyNo().trim().equals( "" ) && policyInfoVO.getLspdId() != null && policyInfoVO.getLspdId() == 95 && policyInfoVO.getPrevLspdId() != null && policyInfoVO.getPrevLspdId() < 6
	    		      )
	                {
	                    biayaAdmin = new BigDecimal( "0" );
	                    editForm.setBiayaAdminIsDisabled( CommonConst.DISABLED_TRUE );
	                    editForm.setBiayaAdmin( biayaAdmin );
	                }
//	                editForm.setBiayaAdmin(biayaAdmin);
	    		}
		    	 String lkuId = policyInfoVO.getLkuId();
		    	 if( isUnitLink( spajNo ) && editForm.getPenarikanUlinkVOList() != null && editForm.getPenarikanUlinkVOList().size() > 0 && editForm.getHasUnitFlag() != null && editForm.getHasUnitFlag() == 1 && editForm.getFlagHelp() != null &&  editForm.getFlagHelp() == 1)
		    	 {
		    		 premiDikembalikan = editForm.getPremiDikembalikan();
		    	 }
		    	 else
		    	 {
			         BigDecimal totalSetor = BigDecimal.ZERO;
			         for( SetoranPremiDbVO vo : setoranPremiDbVOList )
			         {
			             rowJumlah = vo.getJumlah();
			             totalSetor = totalSetor.add( rowJumlah );
			         }
			    	 ArrayList<PenarikanUlinkVO> penarikanUlinkVOList = Common.serializableList(selectPenarikanUlink( spajNo ));
			    	 if( penarikanUlinkVOList != null && penarikanUlinkVOList.size() > 0 )
			         {
			             BigDecimal totalPenarikanUlink = BigDecimal.ZERO;
			             for( PenarikanUlinkVO vo : penarikanUlinkVOList )
			             {
			             	if(!BigDecimal.ZERO.equals(vo.getJumlah()) && vo.getJumlah() !=null)
			             	{
			                      BigDecimal addition = vo.getJumlah() == null? BigDecimal.ZERO : vo.getJumlah();
			                      totalPenarikanUlink = totalPenarikanUlink.add( addition );
			             	}
			             }
			             ArrayList<BiayaUlinkDbVO> biayaUlinkDbVOList = Common.serializableList(elionsManager.selectBiayaUlink( spajNo ));
			
			             BigDecimal totalBiayaUlink = BigDecimal.ZERO;
			             for( BiayaUlinkDbVO vo : biayaUlinkDbVOList )
			             {
			                 rowJumlah = vo.getAmount();
			                 totalBiayaUlink = totalBiayaUlink.add( rowJumlah );
			             }
			             premiDikembalikan = totalPenarikanUlink.add( totalBiayaUlink );
			         }
			         else
			         {
			             premiDikembalikan = totalSetor;
			         }
			    	 
			         HashMap<String, Object> params = new HashMap<String, Object>();
			         params.put("regSpaj", spajNo);
			    	 ArrayList<RefundDetDbVO> selectRefundDetList = Common.serializableList(elionsManager.selectRefundDetList(params));
			    	 if( selectRefundDetList != null && selectRefundDetList.size() > 0 )
			        	{
			    			Integer flagBiayaAdmin = 0;
			        		BigDecimal biayaAdminFromDb = BigDecimal.ZERO;
			    			for(int i = 0 ; i < selectRefundDetList.size() ; i ++)
			        		{
				     			if( selectRefundDetList.get(i).getDeskripsi() != null && "Biaya Administrasi Pembatalan Polis".toUpperCase().equals(selectRefundDetList.get(i).getDeskripsi().toUpperCase()) )
				    			{
				    				flagBiayaAdmin = flagBiayaAdmin + 1;
				    				biayaAdminFromDb = selectRefundDetList.get(i).getJumlah();
				    			}
				    			else
				    			{
				    				
				    			}
			        		}
			          		if( flagBiayaAdmin != null && flagBiayaAdmin > 0 )
			    			{
			    				editForm.setBiayaAdmin( biayaAdminFromDb );
			    			}
			    			else
			    			{
			    				editForm.setBiayaAdmin( null );
			    			}
			        	}
			         if( editForm.getBiayaAdmin() != null && !BigDecimal.ZERO.equals( editForm.getBiayaAdmin() ) )
			         {
			             premiDikembalikan = premiDikembalikan.subtract( editForm.getBiayaAdmin() );
			         }
			         if( editForm.getBiayaMedis() != null && !BigDecimal.ZERO.equals( editForm.getBiayaMedis() ) )
			         {
			             premiDikembalikan = premiDikembalikan.subtract( editForm.getBiayaMedis() );
			         }
			         if( editForm.getBiayaLain() != null && !BigDecimal.ZERO.equals( editForm.getBiayaLain() ) )
			         {
			             premiDikembalikan = premiDikembalikan.subtract( editForm.getBiayaLain() );
			         }
		    	 }
		         map.put("premiDikembalikan", premiDikembalikan.toString());
		         map.put("lkuId", lkuId);
	    	 }
    	 }
    	 return map;
    }
    
    public RefundEditForm retrieveCountPremi( RefundEditForm editForm, RefundLookupForm lookupForm, String theEvent )
    {
    	BigDecimal debet = new BigDecimal("0");
    	BigDecimal kredit = new BigDecimal("0");    	
    	BigDecimal premiDikembalikan = new BigDecimal("0");
    	String spaj = null;
    	
        if( "onPressImageSuratBatal".equals( theEvent ) || "onPressImageEdit".equals( theEvent ) )
	    {
	    	spaj = lookupForm.getSelectedRowCd();
	    	editForm.setPenarikanUlinkVOList( Common.serializableList((List)selectPenarikanUlinkVOList( spaj )) );
	    }
	    else 
	    {
	    	spaj = editForm.getSpaj();
	    }
//        selectPenarikanUlinkVOListBySpaj
        
        PolicyInfoVO policyInfoVO  = genPolicyInfoVOBySpaj( spaj );
    	if( policyInfoVO !=null)
    	{
    		
    		/*
        	if( policyInfoVO.getLkuId() != null && RefundConst.KURS_RUPIAH.equals(policyInfoVO.getLkuId()))
        	{
        		 editForm.setBiayaAdmin( new BigDecimal( "50000" ));
        	}
        	else if( policyInfoVO.getLkuId() != null && RefundConst.KURS_DOLLAR.equals(policyInfoVO.getLkuId()))
        	{
        		editForm.setBiayaAdmin( new BigDecimal( "5" ));
        	}
        	*/
          	if( policyInfoVO.getPolicyNo() == null || policyInfoVO.getPolicyNo().trim().equals( "" ) 
          			|| policyInfoVO.getPolicyNo() != null && !policyInfoVO.getPolicyNo().trim().equals( "" ) && policyInfoVO.getLspdId() != null && policyInfoVO.getLspdId() < 6
          			|| policyInfoVO.getPolicyNo() != null && !policyInfoVO.getPolicyNo().trim().equals( "" ) && policyInfoVO.getLspdId() != null && policyInfoVO.getLspdId() == 95 && policyInfoVO.getPrevLspdId() != null && policyInfoVO.getPrevLspdId() < 6
          			)
            {
                editForm.setBiayaAdminIsDisabled( CommonConst.DISABLED_TRUE );
                editForm.setBiayaAdmin( new BigDecimal( "0" ) );
                editForm.setMerchantListIsDisabled( CommonConst.DISABLED_TRUE );
            }
    	}
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("regSpaj", spaj);
    	ArrayList<RefundDetDbVO> selectRefundDetList = Common.serializableList(elionsManager.selectRefundDetList(params));   
    	if(editForm.getPenarikanUlinkVOList()!=null && editForm.getPenarikanUlinkVOList().size() > 0)
    	{
    		for(int i = 0 ; i < editForm.getPenarikanUlinkVOList().size() ; i ++)
    		{
    			if(editForm.getPenarikanUlinkVOList().get(i).getJumlah()!=null)
    			{
    				debet = debet.add(editForm.getPenarikanUlinkVOList().get(i).getJumlah());
    			}
    		}
    	}    	
    	
    	if(editForm.getTempDescrDanJumlah()!=null)
    	{
    		if(editForm.getTempDescrDanJumlah().size() > 0 )
    		{
    			for(int i = 0 ; i < editForm.getTempDescrDanJumlah().size() ; i ++)
    			{
    				debet = debet.add(editForm.getTempDescrDanJumlah().get(i).getJumlahDebet());
    				kredit = kredit.add(editForm.getTempDescrDanJumlah().get(i).getJumlahKredit());
    			}
    		}
    		if( selectRefundDetList != null && selectRefundDetList.size() > 0 )
        	{
    			Integer flagBiayaAdmin = 0;
        		BigDecimal biayaAdminFromDb = BigDecimal.ZERO;
    			for(int i = 0 ; i < selectRefundDetList.size() ; i ++)
        		{
	     			if( selectRefundDetList.get(i).getDeskripsi() != null && "Biaya Administrasi Pembatalan Polis".toUpperCase().equals(selectRefundDetList.get(i).getDeskripsi().toUpperCase()) )
	    			{
	    				flagBiayaAdmin = flagBiayaAdmin + 1;
	    				biayaAdminFromDb = selectRefundDetList.get(i).getJumlah();
	    			}
	    			else
	    			{
	    			}
        		}
          		if( flagBiayaAdmin != null && flagBiayaAdmin > 0 )
    			{
    				editForm.setBiayaAdmin( biayaAdminFromDb );
    			}
    			else
    			{
    				editForm.setBiayaAdmin( null );
    			}
        	}
    	}
    	else {
        	if( selectRefundDetList != null && selectRefundDetList.size() > 0 )
        	{
        		Integer flagBiayaAdmin = 0;
        		BigDecimal biayaAdminFromDb = BigDecimal.ZERO;
        		for(int i = 0 ; i < selectRefundDetList.size() ; i ++)
        		{
        			if("BIAYA".equals( selectRefundDetList.get(i).getTipe().toString() ) && !"Biaya Administrasi Pembatalan Polis".equals( selectRefundDetList.get(i).getDeskripsi() ) )
        			{
        				debet = debet.add( selectRefundDetList.get(i).getJumlah() );
        			}
        			if("MERCHANTFEE".equals( selectRefundDetList.get(i).getTipe().toString() ) )
        			{
        				debet = debet.add( selectRefundDetList.get(i).getJumlah() );
        			}   
        			
        			if( selectRefundDetList.get(i).getDeskripsi() != null && "Biaya Administrasi Pembatalan Polis".toUpperCase().equals( selectRefundDetList.get(i).getDeskripsi().toUpperCase() ) )
        			{
        				flagBiayaAdmin = flagBiayaAdmin + 1;
        				biayaAdminFromDb = selectRefundDetList.get(i).getJumlah();
        			}
        			else
        			{
        			}
        		}
        		if( flagBiayaAdmin != null && flagBiayaAdmin > 0 )
    			{
    				editForm.setBiayaAdmin( biayaAdminFromDb );
    			}
    			else
    			{
    				editForm.setBiayaAdmin( null );
    			}
        	}
    	}
    	
    	if(editForm.getBiayaAdmin()!=null)
    	{
    		kredit = kredit.add(editForm.getBiayaAdmin());
    	}
    	if(editForm.getBiayaMedis()!=null)
    	{
    		kredit = kredit.add(editForm.getBiayaMedis());
    	}
    	if(editForm.getBiayaLain()!=null)
    	{
    		kredit = kredit.add(editForm.getBiayaLain());
    	}
    	premiDikembalikan = debet.subtract(kredit);
    	editForm.setPremiDikembalikan(premiDikembalikan);
    
    	return editForm;
    }
    
    public ArrayList<PenarikanUlinkVO> selectPenarikanUlinkVOList( String regSpaj )
    {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put( "regSpaj", regSpaj );
        params.put( "tipeNo", RefundDetDbVO.Tipe.WITHDRAW.tipe() );
        ArrayList<RefundDetDbVO> detailList = Common.serializableList(elionsManager.selectRefundDetList( params ));
        ArrayList<PenarikanUlinkVO> result = new ArrayList<PenarikanUlinkVO>();
        PenarikanUlinkVO penarikanUlinkVO;

        for( RefundDetDbVO refundDetDbVO : detailList )
        {
            penarikanUlinkVO = new PenarikanUlinkVO();
            penarikanUlinkVO.setDeskripsiPenarikan( refundDetDbVO.getDeskripsi() );
            penarikanUlinkVO.setJumlah( refundDetDbVO.getJumlah() );
            penarikanUlinkVO.setJumlahUnit( refundDetDbVO.getUnit() );
            penarikanUlinkVO.setLjiId( refundDetDbVO.getLjiId() );
            penarikanUlinkVO.setLjiInvest( elionsManager.selectJenisInvestByLjiId( refundDetDbVO.getLjiId() ) );
            result.add( penarikanUlinkVO );
        }

        return result;
    }
    
    //TODO
    public HashMap<String, Object> updateTglKirimDokFisik( String spajNo, Date tglKirimDokFisik, String pesan, String updateWho, Date updateWhen )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundBusiness.updatePosisiRefund" );

        HashMap<String, Object> result = new HashMap<String, Object>();
        String pageMessage;

        Boolean succeed;
        try
        {
            commonBusiness.updateTglKirimDokFisik( spajNo, tglKirimDokFisik, updateWho, updateWhen );
            pageMessage = pesan + " sukses pada ";
            succeed = true;
        }
        catch( Exception e )
        {
            logger.error("ERROR :", e);
            pageMessage = pesan + " gagal pada ";
            succeed = false;
        }
        pageMessage = pageMessage.concat( getNowStr() );
        result.put( "pageMessage", pageMessage );
        result.put( "succeed", succeed );

        return result;
    }
  
    
    public ArrayList<HashMap<String, String>>   rekapInfoVO(  RefundEditForm editForm, Date tglBatalAwal, Date tglBatalAkhir )
    {
        return lookupBusiness.rekapInfoVO( editForm, tglBatalAwal, tglBatalAkhir );
    }
    
    public ArrayList<HashMap<String, String>>   rekapKeAccFinanceInfoVO(  RefundEditForm editForm, Date tglBatalAwal, Date tglBatalAkhir )
    {
        return lookupBusiness.rekapKeAccFinanceInfoVO( editForm, tglBatalAwal, tglBatalAkhir );
    }
    
    public Date selectNowDate()
    {
    	return elionsManager.selectNowDate();
    }
    
    public Date selectTglKirimDokFisik( String regSpaj )
    {
    	return elionsManager.selectTglKirimDokFisik( regSpaj );
    }
    
    public Lamp1VO retrieveLamp1( Lamp1ParamsVO paramsVO, RefundEditForm editForm )
    {
        return lamp1Business.retrieveLamp1( paramsVO, editForm );
    }
    
    public GbrA1VO retrieveGbrA1( GbrA1ParamsVO paramsVO, RefundEditForm editForm )
    {
        return gbrA1Business.retrieveGbrA1( paramsVO, editForm );
    }
    
    public GbrB1VO retrieveGbrB1( GbrB1ParamsVO paramsVO, RefundEditForm editForm )
    {
        return gbrB1Business.retrieveGbrB1( paramsVO, editForm );
    }
    
    public ArrayList< SetoranVO > setSetoranPremi ( String spajNo, Integer posisiNo )
    {
        return gbrA2Business.setSetoranPremi ( spajNo, posisiNo );
    }
    
    public ArrayList< SetoranPremiDbVO > setSetoranPokokAndTopUp ( ArrayList< SetoranVO > setSetoranPremi )
    {
        return gbrA2Business.setSetoranPokokAndTopUp ( setSetoranPremi );
    }
    
    public GbrA2VO retrieveGbrA2( GbrA2ParamsVO paramsVO, RefundEditForm editForm )
    {
        return gbrA2Business.retrieveGbrA2( paramsVO, editForm );
    }
    
    public GbrA3VO retrieveGbrA3( GbrA3ParamsVO paramsVO,  RefundEditForm editForm )
    {
        return gbrA3Business.retrieveGbrA3( paramsVO, editForm);
    }
    
    public GbrA4VO retrieveGbrA4( GbrA4ParamsVO paramsVO,  RefundEditForm editForm )
    {
        return gbrA4Business.retrieveGbrA4( paramsVO, editForm);
    }
    
    public GbrB2VO retrieveGbrB2( GbrB2ParamsVO paramsVO,  RefundEditForm editForm )
    {
        return gbrB2Business.retrieveGbrB2( paramsVO, editForm);
    }
    
    public GbrB3VO retrieveGbrB3( GbrB3ParamsVO paramsVO,  RefundEditForm editForm )
    {
        return gbrB3Business.retrieveGbrB3( paramsVO, editForm);
    }
    
    public Lamp3VO retrieveLamp3( Lamp3ParamsVO paramsVO,  RefundEditForm editForm )
    {
        return lamp3Business.retrieveLamp3( paramsVO, editForm);
    }
    
    public ArrayList <SetoranPokokDanTopUpVO> selectSetoranPremiPokokDanTopUp( String regSpaj )
    {
		return Common.serializableList(elionsManager.selectSetoranPremiPokokDanTopUp( regSpaj ));
	}
    
    public ArrayList <LampiranListVO> selectMstDetRefundLamp( String regSpaj )
    {
		return Common.serializableList(elionsManager.selectMstDetRefundLamp( regSpaj ));
	}
    
    public Integer selectMaxNoUrutMstDetRefLamp( String regSpaj )
    {
		return elionsManager.selectMaxNoUrutMstDetRefLamp( regSpaj );
	}
    
    public MstBatalParamsVO selectSpajCancelMstBatal( String regSpaj )
    {
		return elionsManager.selectSpajCancelMstBatal( regSpaj );
	}
    
    public CheckSpajParamsVO selectSpajAlreadyCancelMstRefund( String regSpaj )
    {
		return elionsManager.selectSpajAlreadyCancelMstRefund( regSpaj );
	}
    
    public CheckSpajParamsVO selectCheckSpajInDb( String regSpaj )
    {
		return elionsManager.selectCheckSpajInDb( regSpaj );
	}

    public Date selectMspoDatePrint( String regSpaj )
    {
		return elionsManager.selectMspoDatePrint( regSpaj );
	}
    
    public RefundEditVO retrieveRefundEdit( RefundEditParamsVO paramsVO,  RefundEditForm editForm )
    {
        return editBusiness.retrieveRefundEdit( paramsVO, editForm);
    }
    
    public RedemptVO retrieveRedempt( RedemptParamsVO paramsVO )
    {
        return redemptBusiness.retrieveRedempt( paramsVO );
    }

    public Integer selectRefundTotalOfPages( HashMap<String, Object> params )
    {
        return lookupBusiness.selectRefundTotalOfPages( params );
    }

    public ArrayList<RefundViewVO> selectRefundList( HashMap<String, Object> params )
    {
        return lookupBusiness.selectRefundList( params );
    }

    public ArrayList<PenarikanUlinkVO> selectPenarikanUlinkVOListBySpaj( String regSpaj )
    {
        return editBusiness.selectPenarikanUlinkVOListBySpaj( regSpaj );
    }
    
    public PolicyInfoVO selectPolicyInfoBySpaj( String spajNo )
    {
        return elionsManager.selectPolicyInfoBySpaj( spajNo );    
    }
    
    public ArrayList<PolicyInfoVO> selectPolicyInfoBySpajList( String[] spajList )
    {
        return Common.serializableList(elionsManager.selectPolicyInfoBySpajList( spajList ));    
    }

    public RefundDbVO selectRefundByCd( String regSpaj )
    {
        return editBusiness.selectRefundByCd( regSpaj );
    }
    
    public ArrayList<RefundDbVO> selectMstRefund( String regSpaj )
    {
        return Common.serializableList(editBusiness.selectMstRefund( regSpaj ));
    }
    
    public ArrayList<DropDown> getTindakanAllList( )
    {
        return editBusiness.getTindakanAllList( );
    }
    
    public ArrayList<DropDown> getAlasanAllList( )
    {
        return editBusiness.getAlasanAllList();
    }

    public Integer selectCheckSpaj( String regSpaj )
    {
    	if(regSpaj!=null)
    	{
    		regSpaj = regSpaj.trim();
    	}
        return editBusiness.selectCheckSpaj( regSpaj );
    }
    
    public List<DropDown> generateNoOfRowsPerPageList()
    {
        return lookupBusiness.generateNoOfRowsPerPageList();
    }
    
    public ArrayList<SetoranPremiDbVO> selectPenarikanUlinkSortedByMsdpNumber( String spaj )
    {
        return Common.serializableList(elionsManager.selectPenarikanUlinkSortedByMsdpNumber( spaj ));
    }
    
    public void insertMstDetRefundLampiran( String spaj, String lampiran, String checkBox, Integer noUrut )
    {
        elionsManager.insertMstDetRefundLampiran( spaj, lampiran, checkBox, noUrut );
    }
    
    public ArrayList<SetoranPremiDbVO> selectSetoranPremiBySpaj( String spaj )
    {
        return Common.serializableList(elionsManager.selectSetoranPremiBySpaj( spaj ));
    }
    
    public String selectLusFullName( BigDecimal lusId )
    {
        return bacManager.selectLusFullName( lusId );
    }

    public ArrayList<BiayaUlinkDbVO> selectBiayaUlink( String spaj )
    {
        return Common.serializableList(elionsManager.selectBiayaUlink( spaj ));
    }
    
    public HashMap<String, Object> deleteThenInsertRefund( MstRefundParamsVO paramsVO, String actionMessage,RefundEditForm editForm, String theEvent, Integer hasUnitFlag, Integer tindakanCd )
    {
        return Common.serializableMap(commonBusiness.deleteThenInsertRefund( paramsVO, actionMessage, editForm, theEvent, hasUnitFlag, tindakanCd ));
    }
    
    public ArrayList<SetoranPremiDbVO> setoranPremiDbVOListForGantiTertanggungAndPlan( String spaj )
    {
        return Common.serializableList(commonBusiness.setoranPremiDbVOListForGantiTertanggungAndPlan( spaj ));
    }
    
    public HashMap<String, Object> retrieveBiaya2( String regSpaj )
    {
        return Common.serializableMap(editBusiness.retrieveBiaya2( regSpaj ));
    }
    
    public String retrieveNoSurat( String noSpaj, Integer tindakan )
    {
        return commonBusiness.retrieveNoSurat( noSpaj, tindakan );
    }
    
    public PolicyInfoVO getInformationForLampiran( String spaj )
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundBusiness.getInformationForLampiran" );
    	PolicyInfoVO policyInfoVO =  elionsManager.selectPolicyInfoBySpaj( spaj );
    	return policyInfoVO;
    }
    
    public PolicyInfoVO getNewInformationForLampiran( String spajBaru )
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundBusiness.getInformationForLampiran" );
    	PolicyInfoVO policyInfoVO =  elionsManager.selectPolicyInfoBySpaj( spajBaru );
    	return policyInfoVO;
    }
    
    public boolean isUnitLink( String spajNo )
    {
        return commonBusiness.isUnitLink( spajNo );
    }

    public HashMap<String, Object> updatePosisiAndCancelRefund( String spajNo, Integer posisiCd, String pesan )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundBusiness.updatePosisiAndCancelRefund" );

        HashMap<String, Object> result = new HashMap<String, Object>();
        String pageMessage;

        Boolean succeed;
        try
        {
            commonBusiness.updatePosisiAndCancelRefund( spajNo, posisiCd, null, null );
            pageMessage = pesan + " sukses pada ";
            succeed = true;
        }
        catch( Exception e )
        {
            logger.error("ERROR :", e);
            pageMessage = pesan + " gagal pada ";
            succeed = false;
        }
        pageMessage = pageMessage.concat( getNowStr() );
        result.put( "pageMessage", pageMessage );
        result.put( "succeed", succeed );

        return result;
    }

    public HashMap<String, Object> batalkanSpaj( BatalParamsVO paramsVO, RefundProductSpecInterface productSpecific )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundBusiness.batalkanSpaj" );

        HashMap<String, Object> result = new HashMap<String, Object>();
        String pageMessage;

        Boolean succeed;
        try
        {
            String validationMessage;
            boolean validationPassed;

            // jika tidak ada special case pada produk maka dianggap validasi berhasil
            if( productSpecific == null )
            {
                validationMessage = "";
                validationPassed = true;
            }
            else
            {
                HashMap validationResultMap = Common.serializableMap(productSpecific.validationBeforeCancel( paramsVO ));
                validationMessage = ( String ) validationResultMap.get( "validationMessage" );
                validationPassed = ( Boolean ) validationResultMap.get( "validationPassed" );
            }

            if( validationPassed )
            {
                elionsManager.batalkanSpaj( paramsVO.getSpajNo(), paramsVO.getAlasan(), paramsVO.getCurrentUser(), paramsVO.getPosisiNo(), paramsVO.getCancelWho(), paramsVO.getCancelWhen() );
                pageMessage = "SUCCESS : Pembatalan SPAJ sukses pada ".concat( getNowStr() );
                succeed = true;
            }
            else
            {
            	elionsManager.updatePosisiAndCancelRefund(paramsVO.getSpajNo(), 1, null, null);
                pageMessage = "FAILED : Pembatalan SPAJ gagal pada ".concat( getNowStr() );
                succeed = false;
            }
            
            if( validationMessage != null && !validationMessage.trim().equals( "" ) )
                pageMessage = pageMessage.concat( "\\n" ).concat( validationMessage );

        }
        catch( Exception e )
        {
            logger.error("ERROR :", e);
            elionsManager.updatePosisiAndCancelRefund(paramsVO.getSpajNo(), 1, null, null);
            pageMessage = "FAILED : Pembatalan SPAJ gagal pada ".concat( getNowStr() );
            succeed = false;
        }
        result.put( "pageMessage", pageMessage );
        result.put( "succeed", succeed );

        return result;
    }

    public String genLamp1FileName( String spajNo )
    {
        String result;

        DateFormat dateFormat = new SimpleDateFormat( "yyyyMMdd_HHmmss" );
		result =  "surat_refund_" + spajNo + "_" + dateFormat.format( new Date() ) + ".pdf";
        return result;
    }
    
    public String genCreatRekapFileName( String user, Date awal, Date akhir )
    {
        String result;

        DateFormat dateFormat = new SimpleDateFormat( "yyyyMMdd_HHmmss" );
		result =  "surat_rekap_pembatalan_" + dateFormat.format( new Date() ) + ".pdf";
        return result;
    }
    
    public String genLamp3FileName( String spajNo, String tindakan )
    {
        String result;
        String tindakanLabel = null;
        if( "TINDAKAN_GANTI_PLAN".equals( tindakan ) )
        {
        	tindakanLabel = "surat_ganti_plan_";
        }
        else if( "TINDAKAN_GANTI_TERTANGGUNG".equals( tindakan ) )
        {
        	tindakanLabel = "surat_ganti_tertanggung_";
        }
        DateFormat dateFormat = new SimpleDateFormat( "yyyyMMdd_HHmmss" );
		result =  tindakanLabel + spajNo + "_" + dateFormat.format( new Date() ) + ".pdf";
        return result;
    }
    
    public String genLamp3TanpaTindakan ( String spajNo )
    {
        String result;

        DateFormat dateFormat = new SimpleDateFormat( "yyyyMMdd_HHmmss" );
		result =  "surat_tanpa_tindakan_" + spajNo + "_" + dateFormat.format( new Date() ) + ".pdf";
        return result;
    }
    
    public String genRedemptFileName( String spajNo )
    {
        return redemptBusiness.genRedemptFileName( spajNo );
    }

    public List<AlasanVO> initializeAlasan()
    {
        return editBusiness.initializeAlasan();   
    }
	
	public String prosesRedeemUnitForRefund(String lus_id, String reg_spaj){
		return bacManager.prosesRedeemUnitForRefund(lus_id, reg_spaj);
	}
	
	public Boolean getHasFundAllocation(String reg_spaj){
		Integer fund = elionsManager.selectCountMstTransUlink(reg_spaj, null);
		if(fund != 0){
			return true;
		}else{
			return false;
		}
	}
	
	public Map getDataPemegang(String reg_spaj) {
		return (Map) elionsManager.selectPemegang(reg_spaj);
	}
	
	public void sendSmsRefund(Smsserver_out sms_out, boolean default_param) {
		bacManager.insertSmsServerOutWithGateWay(sms_out, default_param);
	}
	
	public void onPressImageDelete(String selectedRowSpaj, User user){
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundBusiness.onPressImageDelete" );
        RefundDbVO refundDbVO = selectRefundByCd(selectedRowSpaj);
        bacManager.deleteDraftPembatalanPolis(refundDbVO, user);
    }
	
    public ArrayList<DropDown> getMerchantAllList( ){
        return editBusiness.getMerchantAllList( );
    }
}
