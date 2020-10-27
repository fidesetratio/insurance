package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundLookupHelper
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Nov 20, 2008 11:15:21 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.util.StringUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.User;
import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.model.refund.RefundForm;
import com.ekalife.elions.model.refund.RefundLookupForm;
import com.ekalife.elions.model.refund.RefundSignInForm;
import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.elions.web.refund.vo.LampiranListVO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.RefundDbVO;
import com.ekalife.elions.web.refund.vo.RefundDocumentVO;
import com.ekalife.elions.web.refund.vo.RefundViewVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;
import com.ekalife.utils.FormatDate;

public class RefundLookupHelper extends RefundHelperParent
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
    
    public RefundLookupHelper()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper constructor is called ..." );
    }

    private RefundBusiness refundBusiness;

    public void setRefundBusiness( RefundBusiness refundBusiness )
    {
        this.refundBusiness = refundBusiness;
    }

    private void gotoPage( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.gotoPage" );
        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
        int gotoPage = lookupForm.getGotoPage();
        if( gotoPage > lookupForm.getTotalOfPages() ) gotoPage = lookupForm.getTotalOfPages();
        if( gotoPage < 1 ) gotoPage = 1;
        lookupForm.setGotoPage( gotoPage );
        lookupForm.setCurrentPage( gotoPage );
    }

    public void onPressLinkFirst( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.onPressLinkFirst" );
        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
        lookupForm.setCurrentPage( 1 );
        lookupForm.setGotoPage( 1 );
    }

    public void onPressLinkPrev( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.onPressLinkPrev" );
        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
        int prevPage = lookupForm.getCurrentPage() - 1;
        if( prevPage < 1 ) prevPage = 1;
        lookupForm.setCurrentPage( prevPage );
        lookupForm.setGotoPage( prevPage );
    }

    public void onPressLinkNext( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.onPressLinkNext" );
        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
        int nextPage = lookupForm.getCurrentPage() + 1;
        if( nextPage > lookupForm.getTotalOfPages() ) nextPage = lookupForm.getTotalOfPages();
        lookupForm.setCurrentPage( nextPage );
        lookupForm.setGotoPage( nextPage );
    }

    public void onPressLinkLast( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.onPressLinkLast" );
        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
        lookupForm.setCurrentPage( lookupForm.getTotalOfPages() );
        lookupForm.setGotoPage( lookupForm.getTotalOfPages() );
    }
    
    public void onPressMoveToMstDetRefundLamp( Object command )
    {
    	//TODO fadly
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.onPressLinkLast" );
        ArrayList<RefundDbVO> mstRefund = refundBusiness.selectMstRefund(null);
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        if( mstRefund != null && mstRefund.size()>0 )
        {
        	for( int i = 0 ; i < mstRefund.size() ; i ++ )
        	{
        		if( mstRefund.get(i).getTindakanCd() != null && mstRefund.get(i).getTindakanCd() == 1 ) // TINDAKAN TIDAK ADA
        		{
        			ArrayList <LampiranListVO> lampiranList = new ArrayList<LampiranListVO>();
        			PolicyInfoVO policyInfoVO = refundBusiness.getInformationForLampiran( mstRefund.get(i).getSpajNo() );
        			String polis = null;
        			if( policyInfoVO != null )
        			{
	        			if( policyInfoVO.getPolicyNo() == null )
	        			{
	        				polis = "-";
	        			}
	        			else
	        			{
	        				polis = policyInfoVO.getPolicyNo();
	        			}
        			
				    lampiranList.add(new LampiranListVO("1","No. SPAJ: " + StringUtil.nomorSPAJ( mstRefund.get(i).getSpajNo() ) + "/ " 
			                + "No. Polis: " + StringUtil.nomorPolis( polis ) + " atas nama Pemegang Polis/Tertanggung: "
			                + policyInfoVO.getNamaPp() + " / " + policyInfoVO.getNamaTt(), CommonConst.DISABLED_FALSE));
				    Integer maxNoUrut = refundBusiness.selectMaxNoUrutMstDetRefLamp( mstRefund.get(i).getSpajNo() );
				    if( maxNoUrut == null ){ maxNoUrut = 0; }
				    refundBusiness.insertMstDetRefundLampiran( mstRefund.get(i).getSpajNo(), lampiranList.get(0).getLampiranLabel(), CommonConst.CHECKED_TRUE, maxNoUrut + 1);
        			}
        		}
        		else if( mstRefund.get(i).getTindakanCd() != null && mstRefund.get(i).getTindakanCd() == 2 )// TINDAKAN REDUND PREMI
        		{
        			ArrayList <LampiranListVO> lampiranList = new ArrayList<LampiranListVO>();
        			PolicyInfoVO policyInfoVO = refundBusiness.getInformationForLampiran( mstRefund.get(i).getSpajNo() );
        			if( policyInfoVO != null )
        			{
	        			ArrayList<SetoranPremiDbVO> setoranPremiDbVOList = refundBusiness.selectPenarikanUlinkSortedByMsdpNumber( mstRefund.get(i).getSpajNo() );
				        lampiranList.add(new LampiranListVO("1","Surat Permohonan Pembatalan", CommonConst.DISABLED_FALSE));
				        if( setoranPremiDbVOList != null && setoranPremiDbVOList.size() > 0 )
				        {
				        	lampiranList.add(new LampiranListVO("2","bukti setoran tanggal " + formatDate.format( setoranPremiDbVOList.get(0).getTglSetor()) + " dengan nominal sebesar " + stdCurrency(policyInfoVO.getLkuId(), setoranPremiDbVOList.get(0).getJumlah()), CommonConst.DISABLED_FALSE ));
				        	for( int j = 1 ; j <  setoranPremiDbVOList.size() ; j ++  )
				        	{
				        		lampiranList.add(new LampiranListVO( (j + 9) + "","bukti setoran tanggal " +formatDate.format( setoranPremiDbVOList.get(j).getTglSetor()) + " dengan nominal sebesar " + stdCurrency(policyInfoVO.getLkuId(), setoranPremiDbVOList.get(j).getJumlah()), CommonConst.DISABLED_FALSE ));
				        	}
				        }
				        lampiranList.add(new LampiranListVO("3","Polis Asli dengan nomor polis " + policyInfoVO.getPolicyNo() + " atas nama Pemegang Polis/Tertanggung:" + StringUtil.camelHumpAndTrim( policyInfoVO.getNamaPp() ) + " / " + StringUtil.camelHumpAndTrim( policyInfoVO.getNamaTt() ), CommonConst.DISABLED_FALSE));
			        	String[] tempLampiram = null;
			        	Integer countTempLampiran = 0;
			        	if(mstRefund.get(i).getLampiran()!=null)
			        	{
			        		tempLampiram = mstRefund.get(i).getLampiran().split(",");
			        		countTempLampiran = tempLampiram.length;
			        	}
			        	int helpCount = 0;
			        	if( countTempLampiran > 0 )
			        	{
				        	for(int j = 0 ; j < lampiranList.size() ; j ++)
				        	{
					        	if(tempLampiram[helpCount].equals(lampiranList.get(j).getLampiranNo()))
					        	{
					        		lampiranList.get(j).setCheckBox( CommonConst.CHECKED_TRUE );
					        		helpCount = helpCount + 1;
					        	}
					        	if(helpCount == countTempLampiran )
					        	{
					        		j = lampiranList.size();
					        	}
				        	}
			        	}
			        	Integer maxNoUrut = refundBusiness.selectMaxNoUrutMstDetRefLamp( mstRefund.get(i).getSpajNo() );
			        	if( maxNoUrut == null )
			        	{
			        		maxNoUrut = 0;
			        	}
			        	for(int j = 0 ; j < lampiranList.size() ; j ++)
			        	{
			        		refundBusiness.insertMstDetRefundLampiran( mstRefund.get(i).getSpajNo(), lampiranList.get(j).getLampiranLabel(), lampiranList.get(j).getCheckBox(), maxNoUrut + (j +1) );	
			        	}
        			}
		        	
        		}
        		else if( mstRefund.get(i).getTindakanCd() != null && mstRefund.get(i).getTindakanCd() == 3 // TINDAKAN GANTI TERTANGGUNG
        				 || mstRefund.get(i).getTindakanCd() != null && mstRefund.get(i).getTindakanCd() == 4 ) // TINDAKAN GANTI PLAN
        		{
        			ArrayList <LampiranListVO> lampiranList = new ArrayList<LampiranListVO>();
			        PolicyInfoVO oldPolicyInfoVO =  refundBusiness.getInformationForLampiran( mstRefund.get(i).getSpajNo() );
			        PolicyInfoVO newPolicyInfoVO = refundBusiness.getNewInformationForLampiran( mstRefund.get(i).getSpajBaruNo() );
			        if( oldPolicyInfoVO != null && newPolicyInfoVO != null )
			        {
					    lampiranList.add(new LampiranListVO("1","No. SPAJ: " + StringUtil.nomorSPAJ( oldPolicyInfoVO.getSpajNo() ) + "/ " 
				                + "No. Polis: " + StringUtil.nomorPolis( oldPolicyInfoVO.getPolicyNo() ) + " atas nama Pemegang Polis/Tertanggung: "
				                + oldPolicyInfoVO.getNamaPp() + " / " + oldPolicyInfoVO.getNamaTt(), CommonConst.DISABLED_FALSE));
					    lampiranList.add(new LampiranListVO("2","SPAJ: " + StringUtil.nomorSPAJ( newPolicyInfoVO.getSpajNo() ) + " Pemegang Polis/Tertanggung: "
		                + newPolicyInfoVO.getNamaPp() + " / " + newPolicyInfoVO.getNamaTt(), CommonConst.DISABLED_FALSE ));
			        	String[] tempLampiram = null;
			        	Integer countTempLampiran = 0;
			        	if(mstRefund.get(i).getLampiran()!=null)
			        	{
			        		tempLampiram = mstRefund.get(i).getLampiran().split(",");
			        		countTempLampiran = tempLampiram.length;
			        	}
			        	int helpCount = 0;
			        	if( countTempLampiran > 0 )
			        	{
				        	for(int j = 0 ; j < lampiranList.size() ; j ++)
				        	{
					        	if(tempLampiram[helpCount].equals(lampiranList.get(j).getLampiranNo()))
					        	{
					        		lampiranList.get(j).setCheckBox( CommonConst.CHECKED_TRUE );
					        		helpCount = helpCount + 1;
					        	}
					        	if(helpCount == countTempLampiran )
					        	{
					        		j = lampiranList.size();
					        	}
				        	}
			        	}
			        	Integer maxNoUrut = refundBusiness.selectMaxNoUrutMstDetRefLamp( mstRefund.get(i).getSpajNo() );
			        	if( maxNoUrut == null )
			        	{
			        		maxNoUrut = 0;
			        	}
			        	for(int j = 0 ; j < lampiranList.size() ; j ++)
			        	{
			        		refundBusiness.insertMstDetRefundLampiran( mstRefund.get(i).getSpajNo(), lampiranList.get(j).getLampiranLabel(), lampiranList.get(j).getCheckBox(), maxNoUrut + (j +1) );	
			        	}
			        }
        		}
        	}
        }
    }
    
    public boolean isUnitLink( String spaj )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.isUnitLink" );
        boolean result = refundBusiness.isUnitLink(spaj);
        return result;
    }
    
    
    public void refreshListPreviewEdit( String theEvent, Object command )
    {      
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.refreshListPreviewEdit" );
	    RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
	    if(editForm.getTempSpaj()!=null && !"".equals(editForm.getTempSpaj()))
	    {
	    	if(!editForm.getSpaj().trim().equals(editForm.getTempSpaj().trim()))
	    	{
	    		editForm.setFlagHelp(null);
	    		editForm.setSetoranPokokAndTopUpFlag( null );
	    	}
	    }
    }
    
    
    public void setlkuList( Object command )
    {     
	    if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.setlkuList" );
	    RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
	    ArrayList<DropDown> lkuList = new ArrayList<DropDown>();
	    lkuList.add(new DropDown("01","Rp"));
	    lkuList.add(new DropDown("02","$"));
	    editForm.setLkuList(lkuList);
    }
    public void setTotalOfPages( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.setTotalOfPages" );
        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();

        lookupForm.setUpdateFromFilter( FormatDate.getDateInFirstSecond( lookupForm.getUpdateFromFilter() ) );
        lookupForm.setUpdateToFilter( FormatDate.getDateInLastSecond( lookupForm.getUpdateToFilter() ) );

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put( "noOfRowsPerPage", lookupForm.getNoOfRowsPerPage() );
        params.put( "tindakanCd", lookupForm.getTindakanFilterCd() );
        params.put( "posisi", lookupForm.getPosisiFilter() );
        params.put( "regSpaj", lookupForm.getSpajFilter() );
        params.put( "updateFrom", lookupForm.getUpdateFromFilter() );
        params.put( "updateTo", lookupForm.getUpdateToFilter() );
        params.put( "lastUpdateBy", lookupForm.getLastUpdateFilter() );

        lookupForm.setTotalOfPages( refundBusiness.selectRefundTotalOfPages( params ) );
    }
    
    private ArrayList<HashMap<String, String>> cloneList( ArrayList<HashMap<String, String>> list )
    {
    	ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        for( HashMap<String, String> map : list )
        {
            result.add( map );
        }
        return result;
    }
    
    
    private HashMap<String, Object> genParamsRekapKeAccFinance( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.genParamsRekapBatal" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();
        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
        // ini adalah bagian inti dari memproses report
    	Date nowDate = refundBusiness.selectNowDate();
    	Date tglBatalAwal = lookupForm.getTglKirimDokFisikFrom();
    	Date tglBatalAkhir = lookupForm.getTglKirimDokFisikTo();
    	
    	ArrayList<HashMap<String, String>>  rekapInfoVO = refundBusiness.rekapKeAccFinanceInfoVO( editForm, tglBatalAwal, tglBatalAkhir );
    	ArrayList<HashMap< String, String > > detailSource = new ArrayList<HashMap< String,String > >();
        String awalTglKirim = null;
        String akhirTglKirim = null;
        HashMap< String, Object > params = new HashMap< String, Object >();
        SimpleDateFormat tgl = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        if( tglBatalAwal != null && !"".equals( tglBatalAwal ) )
        {
        	awalTglKirim = tgl.format( tglBatalAwal );
        }
        else
        {
        	awalTglKirim = null;
        }
        if( tglBatalAkhir != null && !"".equals( tglBatalAkhir ) )
        {
        	akhirTglKirim = tgl.format( tglBatalAkhir );
        }
        else
        {
        	akhirTglKirim = null;
        }
        String tglCetakLaporan = tgl.format( nowDate );
        String jamCetakLaporan = jam.format( nowDate );
        // default for report
        params.put( "awalTglKirim", awalTglKirim );
        params.put( "akhirTglKirim", akhirTglKirim );
        params.put( "tglCetakLaporan", tglCetakLaporan );
        params.put( "jamCetakLaporan", "(jam" + jamCetakLaporan + ")" );
        params.put( "format", "pdf" );
        params.put( "logoPath", "com/ekalife/elions/reports/refund/images/logo_ajs.gif" );
        
        //detail
        detailSource = cloneList( rekapInfoVO );
        params.put( "dataSource", detailSource );
    	return params;
    }
    
    private HashMap<String, Object> genParamsRekapBatal( Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.genParamsRekapBatal" );
        RefundEditForm editForm = ( ( RefundForm ) command ).getEditForm();

        // ini adalah bagian inti dari memproses report
    	Date nowDate = refundBusiness.selectNowDate();
    	Date tglBatalAwal = new Date( nowDate.getYear(), nowDate.getMonth(), nowDate.getDate() - 1, 13, 00,00 );
    	Date tglBatalAkhir = new Date( nowDate.getYear(), nowDate.getMonth(), nowDate.getDate() , 13, 00,00 );
    	
    	ArrayList<HashMap<String, String>>  rekapInfoVO = refundBusiness.rekapInfoVO( editForm, tglBatalAwal, tglBatalAkhir );
    	ArrayList<HashMap< String, String > > detailSource = new ArrayList < HashMap< String,String > >();
        
        HashMap< String, Object > params = new HashMap< String, Object >();
        SimpleDateFormat tgl = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String awalTglKirim = tgl.format( tglBatalAwal );
        String awalJamKirim = jam.format( tglBatalAwal );
        String akhirTglKirim = tgl.format( tglBatalAkhir );
        String akhirJamKirim = jam.format( tglBatalAkhir );
        // default for report
        params.put( "awalTglKirim", awalTglKirim );
        params.put( "awalJamKirim", "(jam" + awalJamKirim + ")" );
        params.put( "akhirTglKirim", akhirTglKirim );
        params.put( "akhirJamKirim", "(jam" + akhirJamKirim + ")" );
        params.put( "tglCetakLaporan", akhirTglKirim );
        params.put( "jamCetakLaporan", "(jam" + akhirJamKirim + ")" );
        params.put( "format", "pdf" );
        params.put( "logoPath", "com/ekalife/elions/reports/refund/images/logo_ajs.gif" );
        
        //detail
        detailSource = cloneList( rekapInfoVO );
        params.put( "dataSource", detailSource );
    	return params;
    }
    
    
    public void generateDocumentRekapKeAccFinance( Object command)
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.generateDocumentRekapBatal" );
    	RefundForm refundForm = ( RefundForm ) command;
        RefundDocumentVO refundDocumentVO = new RefundDocumentVO();
        refundDocumentVO.setDownloadUrlSession( "refund/download_refund_document.htm" );
        refundDocumentVO.setJasperFile( "com/ekalife/elions/reports/refund/lamp_1_rekap_batal_to_acc_finance_refund.jasper" );
        refundDocumentVO.setParams( genParamsRekapKeAccFinance( command ) );
        refundForm.setRefundDocumentVO( refundDocumentVO );
    }
    
    public void generateDocumentRekapBatal( Object command)
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.generateDocumentRekapBatal" );
    	RefundForm refundForm = ( RefundForm ) command;
        RefundDocumentVO refundDocumentVO = new RefundDocumentVO();
        refundDocumentVO.setDownloadUrlSession( "refund/download_refund_document.htm" );
        refundDocumentVO.setJasperFile( "com/ekalife/elions/reports/refund/lamp_1_rekap_batal_refund.jasper" );	
        refundDocumentVO.setParams( genParamsRekapBatal( command ) );
        refundForm.setRefundDocumentVO( refundDocumentVO );
    }
    
    public void search( HttpServletRequest request, Object command, Integer page )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLookupHelper.search" );
        RefundLookupForm lookupForm = ( ( RefundForm ) command ).getLookupForm();
        RefundSignInForm signInForm = ( ( RefundForm ) command ).getSignInForm();
        gotoPage( command );
        User user = (User) request.getSession().getAttribute("currentUser");   

        lookupForm.setUpdateFromFilter( FormatDate.getDateInFirstSecond( lookupForm.getUpdateFromFilter() ) );
        lookupForm.setUpdateToFilter( FormatDate.getDateInLastSecond( lookupForm.getUpdateToFilter() ) );

        String spajFilter = lookupForm.getSpajFilter();
        String polisFilter = lookupForm.getPolisFilter();
        if( spajFilter != null ) { spajFilter = spajFilter.replace( ".", "" ).trim(); }
        if( polisFilter != null) { polisFilter = polisFilter.replace( ".", "" ).trim(); }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if( page == REFUND_REKAP_LOOKUP_JSP )
        {
        	 params.put( "page", "rekapLookUp" );
        	 params.put( "tglAwalKirim", lookupForm.getTglKirimDokFisikFrom() );
        	 params.put( "tglAkhirKirim", lookupForm.getTglKirimDokFisikTo() );
             params.put( "currentPage", lookupForm.getCurrentPage() );
             params.put( "noOfRowsPerPage", lookupForm.getNoOfRowsPerPage() );
        }
        else if( page == REFUND_LOOKUP_AKSEPTASI_JSP )
        {
            params.put( "currentPage", lookupForm.getCurrentPage() );
            params.put( "noOfRowsPerPage", lookupForm.getNoOfRowsPerPage() );
            params.put( "tindakanCd", "2" );
            params.put( "posisi", "AKSEPTASI" );
            params.put( "regSpaj", spajFilter );
            params.put( "noPolis", polisFilter );
            params.put( "updateFrom", lookupForm.getUpdateFromFilter() );
            params.put( "updateTo", lookupForm.getUpdateToFilter() );
            params.put( "lastUpdateBy", lookupForm.getLastUpdateFilter() );
        }
        else
        {
            params.put( "currentPage", lookupForm.getCurrentPage() );
            params.put( "noOfRowsPerPage", lookupForm.getNoOfRowsPerPage() );
            params.put( "tindakanCd", lookupForm.getTindakanFilterCd() );
            params.put( "posisi", lookupForm.getPosisiFilter() );
            params.put( "regSpaj", spajFilter );
            params.put( "noPolis", polisFilter );
            params.put( "updateFrom", lookupForm.getUpdateFromFilter() );
            params.put( "updateTo", lookupForm.getUpdateToFilter() );
            params.put( "lastUpdateBy", lookupForm.getLastUpdateFilter() );
        }
        params.put( "", lookupForm.getLastUpdateFilter() );

        ArrayList<RefundViewVO> resultViewList = refundBusiness.selectRefundList( params );
        request.setAttribute( "resultList", resultViewList );

        if( resultViewList == null || resultViewList.size() == 0 )
        {
            request.setAttribute( "pageMessage", "No record(s) found" );
        }

        // initialize access right
        if( "true".equals( signInForm.getSignIn() ) )
        {
            lookupForm.setLinkAddDisplay( CommonConst.DISPLAY_TRUE );
            lookupForm.setLinkSignInDisplay( CommonConst.DISPLAY_FALSE );
            //Jika login nya adalah samuel(793), fadly(1407), atau anta(3123)
            if( "793".equals( user.getLus_id().trim() ) || "1407".equals( user.getLus_id().trim() ) || "3123".equals( user.getLus_id().trim() ))
            {
            	lookupForm.setBatalSpajDisplay( CommonConst.DISPLAY_TRUE );
            }
            else
            {
            	lookupForm.setBatalSpajDisplay( CommonConst.DISPLAY_FALSE );
            }
            
            //MANTA - Khusus Spesial User Untuk Akseptasi Proses Refund SPAJ
            //Dewi(1614), Titis(1161), dan Anta(3123)
            if(RefundConst.USER_AKSEP_UW.indexOf(user.getLus_id().trim())>-1){
            	lookupForm.setAcceptBatalDisplay( CommonConst.DISPLAY_TRUE );
            	signInForm.setSuperuser( "true" );
            }else{
            	lookupForm.setAcceptBatalDisplay( CommonConst.DISPLAY_FALSE );
            }
        }
        else
        {
            lookupForm.setLinkAddDisplay( CommonConst.DISPLAY_FALSE );
            lookupForm.setLinkSignInDisplay( CommonConst.DISPLAY_TRUE );
        	lookupForm.setBatalSpajDisplay( CommonConst.DISPLAY_FALSE );
        	lookupForm.setAcceptBatalDisplay( CommonConst.DISPLAY_FALSE );
        }

        // arrange how to display prev next first last link
        int currentPage = lookupForm.getCurrentPage();
        int totalOfPages = lookupForm.getTotalOfPages();

        if( currentPage == 1 )
        {
            lookupForm.setLinkFirstDisplay( CommonConst.DISPLAY_FALSE );
            lookupForm.setLinkPrevDisplay( CommonConst.DISPLAY_FALSE );
        }
        else
        {
            lookupForm.setLinkFirstDisplay( CommonConst.DISPLAY_TRUE );
            lookupForm.setLinkPrevDisplay( CommonConst.DISPLAY_TRUE );
        }

        if( currentPage == totalOfPages )
        {
            lookupForm.setLinkLastDisplay( CommonConst.DISPLAY_FALSE );
            lookupForm.setLinkNextDisplay( CommonConst.DISPLAY_FALSE );
        }
        else
        {
            lookupForm.setLinkLastDisplay( CommonConst.DISPLAY_TRUE );
            lookupForm.setLinkNextDisplay( CommonConst.DISPLAY_TRUE );
        }
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
    
}
