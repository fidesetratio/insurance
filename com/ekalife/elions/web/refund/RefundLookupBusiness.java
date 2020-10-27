package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundLookupBusiness
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 30, 2008 4:15:41 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.web.refund.vo.PenarikanUlinkDbVO;
import com.ekalife.elions.web.refund.vo.RefundViewVO;
import com.ekalife.elions.web.refund.vo.RekapInfoVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;

public class RefundLookupBusiness
{
    protected final Log logger = LogFactory.getLog( getClass() );

    private ElionsManager elionsManager;
    private BacManager bacManager;

    public void setBacManager(BacManager bacManager) {
		this.bacManager = bacManager;
	}

	public void setElionsManager( ElionsManager elionsManager )
    {
        this.elionsManager = elionsManager;
    }

    public RefundLookupBusiness()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupBusiness constructor is called ..." );
    }

    public RefundLookupBusiness( ElionsManager elionsManager )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupBusiness constructor is called ..." );
        setElionsManager( elionsManager );
    }

    public Integer selectRefundTotalOfPages( Map<String, Object> params )
    {
        return elionsManager.selectRefundTotalOfPages( params );
    }
    
    public String selectLusFullName( BigDecimal lusId )
    {
        return bacManager.selectLusFullName( lusId );
    }
    
    public ArrayList<HashMap<String, String>>   rekapKeAccFinanceInfoVO( RefundEditForm editForm, Date tglBatalAwal, Date tglBatalAkhir )
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupBusiness.rekapKeAccFinanceInfoVO" );
    	HashMap<String, Object> params = new HashMap<String, Object>();
    	ArrayList<HashMap<String, String>>   result = new ArrayList<HashMap<String,String>>();
    	HashMap<String, String>  map;
    	String noPolis = "";
    	String tglKirimDokFisik = "";

    	params.put( "tglBatalAwal", tglBatalAwal );
    	params.put( "tglBatalAkhir", tglBatalAkhir );
    	
    	ArrayList < RekapInfoVO > tempInfoForRekap = Common.serializableList(elionsManager.selectInfoForRekapKeAccFinance(params));
    	if( tempInfoForRekap != null && tempInfoForRekap.size() >0 )
    	{
    		for(int i = 0 ; i < tempInfoForRekap.size() ; i ++)
    		{
    			if( tempInfoForRekap.get(i).getTglKirimDokFisik() != null && !"".equals( tempInfoForRekap.get(i).getTglKirimDokFisik() ) )
    			{
    				tglKirimDokFisik = FormatDate.toIndonesian( tempInfoForRekap.get(i).getTglKirimDokFisik() );
    			}
    			else 
    			{
    				tglKirimDokFisik = "";
    			}
    			if( tempInfoForRekap.get( i ).getTindakanCd() != null && RefundConst.TINDAKAN_TIDAK_ADA.equals( tempInfoForRekap.get( i ).getTindakanCd() ) )
    			{
    				tglKirimDokFisik = "tidak ada surat";
    			}
    			if( tempInfoForRekap.get(i).getPolicyNo() != null && !"".equals( tempInfoForRekap.get(i).getPolicyNo() ) )
    			{
    				noPolis = tempInfoForRekap.get(i).getPolicyNo();
    			}
    			else 
    			{
    				noPolis = "";
    			}
    			if( tempInfoForRekap.get(i).getAlasan() != null && tempInfoForRekap.get(i).getAlasan().length() > 18 )
    			{
	    			String alasanPembatalanPolisStr = tempInfoForRekap.get(i).getAlasan().toLowerCase().substring( 0, 18 );
	    			if("pembatalan polis :".equals( alasanPembatalanPolisStr ) )
	    			{
	    				tempInfoForRekap.get(i).setAlasan( tempInfoForRekap.get(i).getAlasan().substring( 19, tempInfoForRekap.get(i).getAlasan().length()) );
	    			}
    			}
    			ArrayList<SetoranPremiDbVO> setoranPremiDb = Common.serializableList(elionsManager.selectSetoranPremiBySpaj( tempInfoForRekap.get(i).getSpajNo() ));
    			String noPre = null;
    			String voucher = null;
    			if( setoranPremiDb == null )
    			{
    				noPre = "-";
        			voucher = "-";
    			}
    			else if( setoranPremiDb != null && setoranPremiDb.size() > 0 )
    			{
    				for( int j = 0 ; j < setoranPremiDb.size() ; j ++ )
    				{
    					if( setoranPremiDb.get(j).getNoPre() != null && !"".equals(setoranPremiDb.get(j).getNoPre()))
    					{
    						if( noPre == null)
        					{
        						noPre = setoranPremiDb.get(j).getNoPre();
        					}
        					else
        					{
        						noPre = noPre + "," + setoranPremiDb.get(j).getNoPre();
        					}
    					}
    					if( setoranPremiDb.get(j).getNoVoucher() != null && !"".equals(setoranPremiDb.get(j).getNoVoucher()))
    					{
	    					if( voucher == null )
	    					{
	    						voucher = setoranPremiDb.get(j).getNoVoucher();
	    					}
	    					else
	    					{
	    						voucher = voucher + "," + setoranPremiDb.get(j).getNoVoucher();
	    					}
    					}
    				
    				}
    			}
    			
    			String alasanBatal = tempInfoForRekap.get(i).getAlasan();
    			map = new HashMap<String, String>();
    			map.put( "no", i + 1 + "" );
    			map.put( "noSpaj", tempInfoForRekap.get(i).getSpajNo() );
    			map.put( "noPolis", noPolis );
    			map.put( "namaPemegangPolis", tempInfoForRekap.get(i).getNamaPp() );
    			map.put( "produk", tempInfoForRekap.get(i).getNamaProduk() );
    			map.put( "voucher", voucher );
    			map.put( "noPre", noPre );
    			map.put( "alasanBatal", alasanBatal );
    			map.put( "userUw", tempInfoForRekap.get(i).getUserUw() );
    			map.put( "tglKirim", tglKirimDokFisik );
    			result.add( map );
    		}
    		
    	}
    	
    	return result;
    }
    
    public ArrayList<HashMap<String, String>>   rekapInfoVO( RefundEditForm editForm, Date tglBatalAwal, Date tglBatalAkhir )
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupBusiness.rekapInfoVO" );
    	HashMap<String, Object> params = new HashMap<String, Object>();
    	ArrayList<HashMap<String, String>>   result = new ArrayList<HashMap<String,String>>();
    	HashMap<String, String>  map;
    	String noPolis = "";

    	params.put( "tglBatalAwal", tglBatalAwal );
    	params.put( "tglBatalAkhir", tglBatalAkhir );
    	
    	ArrayList < RekapInfoVO > tempInfoForRekap = Common.serializableList(elionsManager.selectInfoForRekap(params));
    	if( tempInfoForRekap != null && tempInfoForRekap.size() >0 )
    	{
    		for(int i = 0 ; i < tempInfoForRekap.size() ; i ++)
    		{
    			if( tempInfoForRekap.get(i).getPolicyNo() != null && !"".equals( tempInfoForRekap.get(i).getPolicyNo() ) )
    			{
    				noPolis = tempInfoForRekap.get(i).getPolicyNo();
    			}
    			else 
    			{
    				noPolis = "";
    			}
    			if( tempInfoForRekap.get(i).getAlasan() != null && tempInfoForRekap.get(i).getAlasan().length() > 18 )
    			{
	    			String alasanPembatalanPolisStr = tempInfoForRekap.get(i).getAlasan().toLowerCase().substring( 0, 18 );
	    			if("pembatalan polis :".equals( alasanPembatalanPolisStr ) )
	    			{
	    				tempInfoForRekap.get(i).setAlasan( tempInfoForRekap.get(i).getAlasan().substring( 19, tempInfoForRekap.get(i).getAlasan().length()) );
	    			}
    			}
    			ArrayList<SetoranPremiDbVO> setoranPremiDb = Common.serializableList(elionsManager.selectSetoranPremiBySpaj( tempInfoForRekap.get(i).getSpajNo() ));
    			String noPre = null;
    			String voucher = null;
    			if( setoranPremiDb == null )
    			{
    				noPre = "-";
        			voucher = "-";
    			}
    			else if( setoranPremiDb != null && setoranPremiDb.size() > 0 )
    			{
    				for( int j = 0 ; j < setoranPremiDb.size() ; j ++ )
    				{
    					if( setoranPremiDb.get(j).getNoPre() != null && !"".equals(setoranPremiDb.get(j).getNoPre()))
    					{
    						if( noPre == null)
        					{
        						noPre = setoranPremiDb.get(j).getNoPre();
        					}
        					else
        					{
        						noPre = noPre + "," + setoranPremiDb.get(j).getNoPre();
        					}
    					}
    					if( setoranPremiDb.get(j).getNoVoucher() != null && !"".equals(setoranPremiDb.get(j).getNoVoucher()))
    					{
	    					if( voucher == null )
	    					{
	    						voucher = setoranPremiDb.get(j).getNoVoucher();
	    					}
	    					else
	    					{
	    						voucher = voucher + "," + setoranPremiDb.get(j).getNoVoucher();
	    					}
    					}
    				
    				}
    			}
    			
    			String alasanBatal = tempInfoForRekap.get(i).getAlasan();
    			map = new HashMap<String, String>();
    			map.put( "no", i + 1 + "" );
    			map.put( "noSpaj", tempInfoForRekap.get(i).getSpajNo() );
    			map.put( "noPolis", noPolis );
    			map.put( "namaPemegangPolis", tempInfoForRekap.get(i).getNamaPp() );
    			map.put( "produk", tempInfoForRekap.get(i).getNamaProduk() );
    			map.put( "voucher", voucher );
    			map.put( "noPre", noPre );
    			map.put( "alasanBatal", alasanBatal );
    			map.put( "userUw", tempInfoForRekap.get(i).getUserUw() );
    			result.add( map );
    		}
    		
    	}
    	
    	return result;
    }
    
    public ArrayList<RefundViewVO> selectRefundList( HashMap<String, Object> params )
    {
        ArrayList<RefundViewVO> result = Common.serializableList(elionsManager.selectRefundList( params ));
        Integer posisi;
        Integer tindakanCd;
        boolean isUnitLink;
        for( RefundViewVO vo : result )
        {
    		String cancelFullName = selectLusFullName( vo.getCancelWho() );
    		vo.setCancelFullName( cancelFullName ); 
        	vo.setSpajLabel( FormatString.nomorSPAJ( vo.getSpaj() ) );
        	vo.setPolisLabel( FormatString.nomorPolis( vo.getNoPolis() ) );
            RefundCommonBusiness commonBusiness = new RefundCommonBusiness( elionsManager );
            ArrayList<PenarikanUlinkDbVO> dbUnitResult = Common.serializableList(elionsManager.selectPenarikanUlink( vo.getSpaj() ));
            posisi = vo.getPosisiCd();
            isUnitLink = commonBusiness.isUnitLink( vo.getSpaj() );
            tindakanCd = vo.getTindakanCd();
            vo.setTglKirimDokFisikLabel(FormatDate.toIndonesian( vo.getTglKirimDokFisik() ) );
            if( vo.getAlasan() != null && vo.getAlasan().length() >=18 && "pembatalan polis :".equals( vo.getAlasan().substring( 0 , 18 ).toLowerCase() ))
            {
            	vo.setAlasan( vo.getAlasan().substring( 19 , vo.getAlasan().length() ) );
            }
            if( RefundConst.POSISI_DRAFT.equals( posisi ) )
            {
            	vo.setAksesHapusDraft( "true" );
            }
            
            if( isUnitLink ) // unit link
            {
                if( RefundConst.TINDAKAN_TIDAK_ADA.equals( tindakanCd ) )
                {
                    if( posisi > RefundConst.POSISI_DRAFT )
                    {
                    	ArrayList<SetoranPremiDbVO> setoranPremiBySpaj = Common.serializableList(elionsManager.selectSetoranPremiBySpaj( vo.getSpaj() ));
                    	if( setoranPremiBySpaj != null && setoranPremiBySpaj.size() > 0 )
                    	{
                    		vo.setSuratBatalExist( "true" );
                    	}
                    }
                }
                else if( RefundConst.TINDAKAN_REFUND_PREMI.equals( tindakanCd ) )
                {               
                	if(dbUnitResult != null && dbUnitResult.size() > 0 && dbUnitResult.get(0).getJumlahUnit()!=null && !BigDecimal.ZERO.equals(dbUnitResult.get(0).getJumlahUnit()))
                	{
	                    if( posisi > RefundConst.POSISI_DRAFT )
	                    {
	                        vo.setSuratRedemptExist( "true" );
	                    }
	                    if( posisi > RefundConst.POSISI_REFUND_ULINK_REDEMPT_SENT )
	                    {
	                        vo.setSuratBatalExist( "true" );
	                    }
                	}
                	else
                	{
                        if( posisi > RefundConst.POSISI_DRAFT )
	                    {
	                        vo.setSuratRedemptExist( "false" );
	                    }
	                    if( posisi > RefundConst.POSISI_REFUND_ULINK_REDEMPT_SENT )
	                    {
	                        vo.setSuratBatalExist( "true" );
	                    }
                	}
                }
                else if( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( tindakanCd ) || RefundConst.TINDAKAN_GANTI_PLAN.equals( tindakanCd ))
                {
                	if( vo.getHasUnitFlag() != null && RefundConst.UNIT_FLAG.equals( vo.getHasUnitFlag() ) )
                	{
                        if( posisi > RefundConst.POSISI_DRAFT )
                        {
                            vo.setSuratRedemptExist( "true" );
                        }
                        
                        if( RefundConst.POSISI_GANTI_TERTANGGUNG_ULINK_CANCEL.equals( posisi ) )
                        {
                            vo.setSuratBatalExist( "true" );
                        }
                	}
                	else
                	{
                        if( RefundConst.POSISI_GANTI_TERTANGGUNG_NON_ULINK_CANCEL.equals( posisi ) )
                        {
                            vo.setSuratBatalExist( "true" );
                        }
                        
                        if( !RefundConst.POSISI_GANTI_TERTANGGUNG_NON_ULINK_CANCEL.equals( posisi ) )
                        {
                        	vo.setAksesHapusDraft( "true" );
                        }
                	}
                }
            }
            else // not unit link
            {
                if( RefundConst.TINDAKAN_TIDAK_ADA.equals( tindakanCd ) )
                {
                    if( posisi > RefundConst.POSISI_DRAFT )
                    {
                    	ArrayList<SetoranPremiDbVO> setoranPremiBySpaj = Common.serializableList(elionsManager.selectSetoranPremiBySpaj( vo.getSpaj() ));
                    	if( setoranPremiBySpaj != null && setoranPremiBySpaj.size() > 0 )
                    	{
                    		vo.setSuratBatalExist( "true" );
                    	}
                    }
                }
                else if( RefundConst.TINDAKAN_REFUND_PREMI.equals( tindakanCd ) )
                {
                    if( posisi > RefundConst.POSISI_DRAFT )
                    {
                        vo.setSuratBatalExist( "true" );
                    }
                    
                    if( RefundConst.POSISI_REFUND_NON_ULINK_ACCEPTATION.equals( posisi ) )
                    {
                    	vo.setAksesHapusDraft( "true" );
                    }
                }
                else if( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( tindakanCd ) || RefundConst.TINDAKAN_GANTI_PLAN.equals( tindakanCd ))
                {
                    if( posisi > RefundConst.POSISI_DRAFT )
                    {
                        vo.setSuratBatalExist( "true" );
                    }
                    
                    if( !RefundConst.POSISI_GANTI_TERTANGGUNG_NON_ULINK_CANCEL.equals( posisi ) )
                    {
                    	vo.setAksesHapusDraft( "true" );
                    }
                }
            }
        }

        return result;
    }

    public ArrayList<DropDown> generateNoOfRowsPerPageList()
    {
        ArrayList<DropDown> result = new ArrayList<DropDown>();
        DropDown dropDown;

        dropDown = new DropDown( "3", "3" );
        result.add( dropDown );
        dropDown = new DropDown( "10", "10" );
        result.add( dropDown );
        dropDown = new DropDown( "15", "15" );
        result.add( dropDown );
        dropDown = new DropDown( "25", "25" );
        result.add( dropDown );
        dropDown = new DropDown( "50", "50" );
        result.add( dropDown );
        dropDown = new DropDown( "100", "100" );
        result.add( dropDown );
        dropDown = new DropDown( "250", "250" );
        result.add( dropDown );
        dropDown = new DropDown( "500", "500" );
        result.add( dropDown );

        return result;
    }
}
