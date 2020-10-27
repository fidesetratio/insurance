package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundGbrA2Business
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 23, 2008 1:26:45 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import id.co.sinarmaslife.std.util.DateUtil;
import id.co.sinarmaslife.std.util.StringUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.elions.web.refund.vo.GbrA2ParamsVO;
import com.ekalife.elions.web.refund.vo.GbrA2VO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.RefundDetDbVO;
import com.ekalife.elions.web.refund.vo.RincianPolisVO;
import com.ekalife.elions.web.refund.vo.SetoranPokokDanTopUpVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;
import com.ekalife.elions.web.refund.vo.SetoranVO;
import com.ekalife.utils.AngkaTerbilang;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatString;

public class RefundGbrA2Business
{
    protected final Log logger = LogFactory.getLog( getClass() );

    private ElionsManager elionsManager;

    public void setElionsManager( ElionsManager elionsManager )
    {
        this.elionsManager = elionsManager;
    }

    public RefundGbrA2Business()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA2Business constructor is called ..." );
    }

    public RefundGbrA2Business( ElionsManager elionsManager )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA2Business constructor is called ..." );
        setElionsManager( elionsManager );
    }

    private ArrayList<HashMap<String, String>> genSpace()
    {
        ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;
        map = new HashMap<String, String>();
        map.put( "descr", "" );
        map.put( "jumlah", "" );
        result.add( map );
        return result;
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

    private String genStatement( String spajNo, String polisNo, String alasan )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA2Business.genStatement" );

        return "Pembayaran premi untuk No. SPAJ:" + StringUtil.nomorSPAJ( spajNo ) +
                "/ No.Polis:" + polisNo +
                " diatas dengan rincian sebagai berikut mohon dikembalikan/ dibatalkan karena: " + alasan;
    }
    
    public ArrayList< SetoranPremiDbVO > setSetoranPokokAndTopUp ( ArrayList< SetoranVO > setSetoranPremi )
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA2Business.setSetoranPokokAndTopUp" );
    	ArrayList< SetoranPremiDbVO > result = new ArrayList< SetoranPremiDbVO >();
    	if( setSetoranPremi != null && setSetoranPremi.size() > 0 )
    	{
    		for( SetoranVO vo : setSetoranPremi )
    		{
    			result.add( new SetoranPremiDbVO( vo.getTitipanKe(),vo.getTanggalSetoran(), vo.getJumlahPokok(), vo.getLkuId(), vo.getKurs(), vo.getDescrPokok(), vo.getNoPre(), vo.getNoVoucher() ) );
    			result.add( new SetoranPremiDbVO( vo.getTitipanKe(),vo.getTanggalSetoran(), vo.getJumlahTopUp(), vo.getLkuId(), vo.getKurs(), vo.getDescrTopUp(), vo.getNoPre(), vo.getNoVoucher() ) );
    		}
    	}
    	return result;
    }
    
    public ArrayList< SetoranVO > setSetoranPremi ( String spajNo, Integer posisiNo )
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA2Business.setSetoranPremi" );
    	 ArrayList< SetoranVO > result = new ArrayList< SetoranVO >();
    	 
    	 if( posisiNo == null ) // jika blm prh di save
    	 {    		   
    		 PolicyInfoVO policyInfoVO = elionsManager.selectPolicyInfoBySpaj( spajNo );
    		 String titipan;
    		 BigDecimal rowJumlah;
        	 BigDecimal adjustedJumlah = BigDecimal.ZERO;
        	 String spajLkuId = policyInfoVO.getLkuId();
        	 String descrSetoran;
        	 String descrPokok;
        	 String descrTopUp;
        	 BigDecimal jumlah;
        	 String jumlahLabel;
        	 Date tanggal;
        	 String spajCur = "02".equals( spajLkuId )? "$" : "Rp.";
        	 ArrayList< SetoranPremiDbVO > setoranPremiDbVOList = Common.serializableList(elionsManager.selectSetoranPremiBySpaj( spajNo ));
        	 for( SetoranPremiDbVO vo : setoranPremiDbVOList )
        	 {
        		 String originalCur = "";
        		 rowJumlah = vo.getJumlah();
        		 if( !vo.getLkuId().equals( spajLkuId ) )
        		 {
        			 String cur = "02".equals( vo.getLkuId() )? "$" : "Rp.";
        			 originalCur = stdCurrency( cur, rowJumlah );
        		 }
        		 else
        		 {
        			 adjustedJumlah = rowJumlah;
        		 }
        		 titipan = vo.getTitipanKe() != null? " (titipan premi)" : "";
        		 String tglStr = DateUtil.toIndonesian( vo.getTglSetor() );
        		 jumlahLabel = stdCurrency( spajCur, adjustedJumlah );
        		 descrSetoran = "Premi disetor tgl. " + tglStr + titipan + originalCur;
        		 descrPokok = "Premi Pokok disetor pada tgl. " + tglStr  + titipan + originalCur;
        		 descrTopUp = "Premi Top Up tgl. " + tglStr + titipan + originalCur;
        		 jumlah = adjustedJumlah;
        		 tanggal = vo.getTglSetor() ;
        		 result.add( new SetoranVO( tanggal, descrPokok, descrTopUp, jumlah, vo.getLkuId(), vo.getTitipanKe(), vo.getKurs(), vo.getNoPre(), vo.getNoVoucher() ) );
        	 }
    	 }
    	 else if( posisiNo != null && posisiNo <=2 ) // jika posisi adalah draft
    	 {
    		 HashMap<String, Object> params = new HashMap<String, Object>();
    		 params.put("regSpaj", spajNo );
    		 ArrayList< RefundDetDbVO > setoranPremiFromMstDetRefundList = Common.serializableList(elionsManager.selectRefundDetList( params ));
    		 int indexResult = 0;
    		 for( int i = 0 ; i < setoranPremiFromMstDetRefundList.size() ; i++  )
    		 {
    			 if( setoranPremiFromMstDetRefundList.get( i ).getTipeNo() == 1 ) // jika tipe Premi
    			 {
    				 if( setoranPremiFromMstDetRefundList.get( i ).getDeskripsi().toLowerCase().contains( "premi" ) )
    				 {
        				 String lkuId = setoranPremiFromMstDetRefundList.get( i ).getLkuId();
        				 String descr = setoranPremiFromMstDetRefundList.get( i ).getDeskripsi();
        				 BigDecimal jumlah = setoranPremiFromMstDetRefundList.get( i ).getJumlah();
        				 result.add( new SetoranVO( null, descr, null, jumlah, lkuId, null, null, null, null ) );
        				 indexResult = indexResult + 1;
    				 }
    			 }
    			 if( setoranPremiFromMstDetRefundList.get( i ).getTipeNo() == 3 )// jika tipe Top Up
    			 {
    				 if( setoranPremiFromMstDetRefundList.get( i ).getDeskripsi().toLowerCase().contains( "premi" ) )
    				 {
	    				 if( i > 0 ) // jika index sudah melewati 0 ( logic di dalam if ini utk mengambil index yang sama dengan premi pokok, fungsi nya yaitu menyisipkan deskripsi dan jumlah top up pada index yang sama pada premi pokok )
	    				 {
	        				 String descr = setoranPremiFromMstDetRefundList.get( i ).getDeskripsi();
	        				 BigDecimal jumlah = setoranPremiFromMstDetRefundList.get( i ).getJumlah();
	        				 if( result != null && result.size() > 0 ) // jika result sudah ada isi
	        				 {
	            				 result.get( indexResult - 1 ).setDescrTopUp( descr ); // krn index maka indexResult dikurangi 1
	            				 result.get( indexResult - 1 ).setJumlahTopUp( jumlah );// krn index maka indexResult dikurangi 1
	        				 }
	    				 }
    				 }
    			 }
    		 }
    	 }
    	 else
    	 {
    		 
    	 }
    	 return result;
    }

    private RincianPolisVO genRincianPolis( String spajNo, String spajLkuId, BigDecimal biayaAdmin, 
    		BigDecimal biayaMedis, BigDecimal biayaLain, String biayaLainDescr, Integer posisiNo,
    		ArrayList < SetoranPremiDbVO > setoranPokokAndTopUpList, BigDecimal biayaMerchant )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA2Business.genRincianPolis" );

        RincianPolisVO result = new RincianPolisVO();
        ArrayList<HashMap<String, String>> setoranMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> penarikanUlinkMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> biayaUlinkMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> biayaStandarMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> summaryMapList = new ArrayList<HashMap<String, String>>();
        ArrayList< SetoranPremiDbVO > setoranPokokAndTopUp = new ArrayList< SetoranPremiDbVO >();
        
        HashMap<String, String> map;
        BigDecimal premiDikembalikan;
        String spajCur = "02".equals( spajLkuId )? "$" : "Rp.";

        String tglStr;
        String rowLkuId;
        BigDecimal rowJumlah;
        BigDecimal adjustedJumlah;
        ArrayList< SetoranPokokDanTopUpVO > setoranPremiPokokDanTopUp = Common.serializableList(elionsManager.selectSetoranPremiPokokDanTopUp( spajNo ));
        String allNoVoucher = null;
        BigDecimal totalSetor = BigDecimal.ZERO;
        String mtuUnit = elionsManager.selectMtuUnitTransUlink( spajNo );
        
        if ( mtuUnit == null || "".equals( mtuUnit ) || mtuUnit != null && "0".equals( mtuUnit ) ) // jika mtu unit nya kosong atau null
        {
        	if( posisiNo == null || posisiNo != null && posisiNo <= RefundConst.POSISI_DRAFT ) // jika posisi nya msh draft / blm prh di save
        	{
        		if( setoranPokokAndTopUpList != null && setoranPokokAndTopUpList.size() > 0 ) // setoran List di ambil dr tabel mst_payment dan mst_deposit_premium
        		{
        			for( SetoranPremiDbVO vo : setoranPokokAndTopUpList )
                	{
                        tglStr = DateUtil.toIndonesian( vo.getTglSetor() );
                        BigDecimal jumlah = vo.getJumlah();
                        if( jumlah == null )
                        {
                        	jumlah = new BigDecimal("0");
                        }
                        map = new HashMap<String, String>();
                        map.put( "descr", vo.getDescr() );
                        map.put( "jumlah", ": " + stdCurrency( spajCur, vo.getJumlah() ) );
                        map.put( "tanggal", tglStr );
                        setoranMapList.add( map );
                        
                        totalSetor = totalSetor.add( jumlah );
                      }
        			setoranPokokAndTopUp.addAll( setoranPokokAndTopUpList );
        		}
        	}
        	else // jika posisi uda melewati draft
        	{
	        	HashMap<String, Object> params = new HashMap<String, Object>();
	        	params.put( "regSpaj", spajNo );
        		ArrayList<RefundDetDbVO> selectRefundDetList = Common.serializableList(elionsManager.selectRefundDetList( params ));
        		if( selectRefundDetList != null && selectRefundDetList.size() > 0 ) // setoran list diambil dr mst_det_refund
        		{
		        	for( RefundDetDbVO vo : selectRefundDetList )
		        	{
		        		if( vo.getTipeNo() == RefundConst.TIPE_PREMI && vo.getDeskripsi().toLowerCase().contains( "premi" ) )
		        		{
	                        map = new HashMap<String, String>();
	                        map.put( "descr", vo.getDeskripsi() );
	                        map.put( "jumlah", ": " + stdCurrency( spajCur, vo.getJumlah() ) );
	                        setoranMapList.add( map );
	                        totalSetor = totalSetor.add( vo.getJumlah() );
	                        setoranPokokAndTopUp.add( new SetoranPremiDbVO( null, null, vo.getJumlah(), vo.getLkuId(), null, vo.getDeskripsi(), null, null ) );
		        		}
		        		if( vo.getTipeNo() == RefundConst.TIPE_TOPUP && vo.getDeskripsi().toLowerCase().contains( "premi" ) )
		        		{
	                        map = new HashMap<String, String>();
	                        map.put( "descr", vo.getDeskripsi() );
	                        map.put( "jumlah", ": " + stdCurrency( spajCur, vo.getJumlah() ) );
	                        setoranMapList.add( map );
	                        totalSetor = totalSetor.add( vo.getJumlah() );
	                        setoranPokokAndTopUp.add( new SetoranPremiDbVO( null, null, vo.getJumlah(), vo.getLkuId(), null, vo.getDeskripsi(), null, null ) );
		        		}
		        	}
        		}
        	}
        }
        else // jika mtu unit nya ada nilai
        {
        	 String descr = null;
        	 for( SetoranPokokDanTopUpVO vo : setoranPremiPokokDanTopUp ) // setoran list diambil dari mst_billing
        	 {
        		 rowLkuId = vo.getLkuId();
        		 String originalCur = "";
        		 rowJumlah = vo.getMsdbPremium();
        		 if( !rowLkuId.equals( spajLkuId ) )
        		 {
        			 String cur = "02".equals( rowLkuId )? "$" : "Rp.";
        			 originalCur = stdCurrency( cur, rowJumlah );
        			 adjustedJumlah = rowJumlah;
        		 }
        		 else
        		 {
        			 adjustedJumlah = rowJumlah;
        		 }
        		 //end todo
        		 tglStr = DateUtil.toIndonesian( vo.getMuTglTrans() );
        		 if( vo.getLtId() != null && vo.getLtId() == 1 )
        		 {
        			 descr = "Premi Pokok disetor pada tgl. " + tglStr  + originalCur;
        			 map = new HashMap<String, String>();
        			 map.put( "descr", descr );
        			 map.put( "jumlah", ": " + stdCurrency( spajCur, adjustedJumlah ) );
        			 map.put( "tanggal", tglStr );
        			 setoranMapList.add( map );
        		 }
        		 else if( vo.getLtId() != null && vo.getLtId() == 2 || vo.getLtId() != null && vo.getLtId() == 5 )
        		 {
        			 descr = "Premi Top Up tgl. " + tglStr  + originalCur;
        			 map = new HashMap<String, String>();
        			 map.put( "descr", descr );
        			 map.put( "jumlah", ": " + stdCurrency( spajCur, adjustedJumlah ) );
        			 map.put( "tanggal", tglStr );
        			 setoranMapList.add( map );
        		 }
        		 setoranPokokAndTopUp.add( new SetoranPremiDbVO( null, vo.getMuTglTrans(), adjustedJumlah, vo.getLkuId(), null, descr, null, null ) );
        		 totalSetor = totalSetor.add( rowJumlah );
        	 }
        }
        premiDikembalikan = totalSetor;

        if( biayaMerchant != null && !BigDecimal.ZERO.equals( biayaMerchant ) )
        {
        	map = new HashMap<String, String>();
        	map.put( "descr", "Biaya merchant dikembalikan" );
        	map.put( "jumlah", ": " + stdCurrency( spajCur, biayaMerchant ) );
        	biayaStandarMapList.add( map );
	
        	premiDikembalikan = premiDikembalikan.add( biayaMerchant );
        }
        
        if( biayaAdmin != null && !BigDecimal.ZERO.equals( biayaAdmin ) )
        {
        	map = new HashMap<String, String>();
        	map.put( "descr", "Biaya administrasi pembatalan polis" );
        	map.put( "jumlah", ": " + "("  + stdCurrency( spajCur, biayaAdmin ) + ")");
        	biayaStandarMapList.add( map );
        	
        	premiDikembalikan = premiDikembalikan.subtract( biayaAdmin );
        }

        if( biayaMedis != null && !BigDecimal.ZERO.equals( biayaMedis ) )
        {
        	map = new HashMap<String, String>();
        	map.put( "descr", "Biaya medis" );
        	map.put( "jumlah", ": " + "("  + stdCurrency( spajCur, biayaMedis ) + ")");
        	biayaStandarMapList.add( map );
        		
        	premiDikembalikan = premiDikembalikan.subtract( biayaMedis );
        }

        if( biayaLain != null && !BigDecimal.ZERO.equals( biayaLain ) )
        {
        	map = new HashMap<String, String>();
        	map.put( "descr", biayaLainDescr );
        	map.put( "jumlah", ": " + "("  + stdCurrency( spajCur, biayaLain ) + ")");
        	biayaStandarMapList.add( map );
        	
        	premiDikembalikan = premiDikembalikan.subtract( biayaLain );
        }

        map = new HashMap<String, String>();
        map.put( "descr", "Premi dikembalikan" );
        map.put( "jumlah", ": " + stdCurrency( spajCur, premiDikembalikan ) );
        summaryMapList.add( map );
        
        result.setSetoranMapList( setoranMapList );
        result.setPenarikanUlinkMapList( penarikanUlinkMapList );
        result.setBiayaUlinkMapList( biayaUlinkMapList );
        result.setBiayaStandarList( biayaStandarMapList );
        result.setSummaryMapList( summaryMapList );
        result.setTotalPremiDikembalikan( premiDikembalikan );
        result.setNoVoucher( allNoVoucher );
        result.setSetoranPokokAndTopUp( setoranPokokAndTopUp );
        
        return result;
    }

    //TODO
    
    private String stdCurrency( String lkuId, BigDecimal value )
    {
        String result;

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
    
    public GbrA2VO retrieveGbrA2( GbrA2ParamsVO paramsVO, RefundEditForm editForm )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA2Business.retrieveGbrA2" );

        GbrA2VO gbrA2VO = new GbrA2VO();

        String spajNo = paramsVO.getSpajNo();
        PolicyInfoVO policyInfoVO = elionsManager.selectPolicyInfoBySpaj( spajNo );

        String polisNo ;

        if( policyInfoVO.getPolicyNo() == null || policyInfoVO.getPolicyNo().trim().equals( "" ) )
        {
            if( policyInfoVO.getNamaPp() != null && !policyInfoVO.getNamaPp().trim().equals( "" ) )
            {
//                polisNo = " atas nama " + policyInfoVO.getNamaPp();
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

        String spaj_polis = FormatString.nomorSPAJ( spajNo ) + " / " + polisNo;

        String hal = "Pembatalan Polis : SPAJ " + spaj_polis;
        gbrA2VO.setNoUrutMemo( "-" );
        gbrA2VO.setHal( hal );
        gbrA2VO.setTanggal( DateUtil.toIndonesian( elionsManager.selectDate() ) );
        
        gbrA2VO.setSpajNo( StringUtil.nomorSPAJ( spajNo ) );
        gbrA2VO.setPolisNo( polisNo );
        gbrA2VO.setProduk( StringUtil.camelHumpAndTrim( policyInfoVO.getNamaProduk() ) + " (link belum Fund)"  );
        gbrA2VO.setPemegangPolis( StringUtil.camelHumpAndTrim( policyInfoVO.getNamaPp() ) );
        gbrA2VO.setTertanggung( StringUtil.camelHumpAndTrim( policyInfoVO.getNamaTt() ) );
        gbrA2VO.setPrevLspdId( policyInfoVO.getLspdId() );
        gbrA2VO.setEndDate( policyInfoVO.getEndDate() );
        gbrA2VO.setBegDate( policyInfoVO.getBegDate() );
        gbrA2VO.setLca_id( policyInfoVO.getLca_id() );
        gbrA2VO.setStatement( genStatement( spajNo, polisNo, paramsVO.getAlasanForLabel() ) );
        gbrA2VO.setPembatal( policyInfoVO.getLus_full_name() );
        RincianPolisVO rincianPolisVO = genRincianPolis(
                spajNo,
                policyInfoVO.getLkuId(),
                paramsVO.getBiayaAdmin(),
                paramsVO.getBiayaMedis(),
                paramsVO.getBiayaLain(),
                paramsVO.getBiayaLainDescr(),
                paramsVO.getPosisi(),
                paramsVO.getSetoranPokokAndTopUpList(),
                paramsVO.getBiayaMerchant()
        );
        ArrayList<HashMap<String, String>> rincianList = new ArrayList<HashMap<String,String>>();
        rincianList = cloneList( rincianPolisVO.getSetoranMapList() );
        rincianList.addAll( genSpace() );
        rincianList.addAll( cloneList( rincianPolisVO.getPenarikanUlinkMapList() ) );
        rincianList.addAll( genSpace() );
        rincianList.addAll( cloneList( rincianPolisVO.getBiayaUlinkMapList() ) );
        rincianList.addAll( genSpace() );
        rincianList.addAll( cloneList( rincianPolisVO.getBiayaStandarList() ) );
        rincianList.addAll( genSpace() );
        rincianList.addAll( cloneList( rincianPolisVO.getSummaryMapList() ) );

        gbrA2VO.setSetoranPokokAndTopUp( rincianPolisVO.getSetoranPokokAndTopUp() );
        gbrA2VO.setRincianPolisList( rincianList );

        gbrA2VO.setJumlahTerbilang( "(" + AngkaTerbilang.indonesian(
               rincianPolisVO.getTotalPremiDikembalikan().toString(), policyInfoVO.getLkuId() ).toLowerCase() + ")" );
        
        gbrA2VO.setRincianPolisList( rincianList );       
        
        if( editForm.getLampiranAddList() != null && editForm.getLampiranAddList().size() > 0 )
        {
        	ArrayList<HashMap<String, String>> tempLampiran = new ArrayList<HashMap<String,String>>();
        	HashMap<String, String> map;
        	for(int i = 0 ; i < editForm.getLampiranAddList().size() ; i ++)
        	{
	    		if(CommonConst.CHECKED_TRUE.equals(editForm.getLampiranAddList().get(i).getCheckBox()))
	    		{
	    	        map = new HashMap<String, String>();
	    	        map.put( "dashAdd", "-" );
	    	        map.put( "contentAdd", editForm.getLampiranAddList().get(i).getLampiranLabel() );
	    	        tempLampiran.add(map);
	    		}
        	}
        	gbrA2VO.setAddLampiranList( tempLampiran );       
        }
        
        gbrA2VO.setAtasNama( StringUtil.camelHumpAndTrim( paramsVO.getAtasNama() ) );        
        gbrA2VO.setRekeningNo( paramsVO.getNorek() );
        gbrA2VO.setBankName( paramsVO.getNamaBank() );        
        String kotaBank = null;
        String cabangBank = null;
        if(paramsVO.getKotaBank()!=null && !"".equals(paramsVO.getKotaBank()))
        {
        	kotaBank = StringUtil.camelHumpAndTrim( paramsVO.getKotaBank() );
        }
        if(paramsVO.getCabangBank() !=null && !"".equals(paramsVO.getCabangBank() ))
        {
        	cabangBank = StringUtil.camelHumpAndTrim( paramsVO.getCabangBank() );
        }
        gbrA2VO.setCabang( cabangBank );
        gbrA2VO.setKota( kotaBank );
        setSignerAndTtd( rincianPolisVO.getTotalPremiDikembalikan(), gbrA2VO, policyInfoVO.getLkuId() );
        gbrA2VO.setTotalPremiDikembalikan( rincianPolisVO.getTotalPremiDikembalikan() );
        gbrA2VO.setNoVoucher( rincianPolisVO.getNoVoucher() );
        return gbrA2VO;
    }
    
    private void setSignerAndTtd( BigDecimal premiDikembalikan, GbrA2VO gbrA2VO, String lkuId ){
    	BigDecimal compareValue = BigDecimal.ZERO;
    	if( "02".equals(lkuId)){// dollar
    		compareValue = new BigDecimal("5000");
    	}else{// rupiah
    		compareValue = new BigDecimal("50000000");
    	}
    	if( premiDikembalikan != null ){
	    	if( premiDikembalikan.compareTo( compareValue ) == 1){
    			gbrA2VO.setSigner( "dr. Sisti Karsinah" );
				gbrA2VO.setTtd("com/ekalife/elions/reports/refund/images/drsisti.gif");
	    	}else{
	    		if("09,40,42,58,62,65".indexOf(gbrA2VO.getLca_id())<=-1){
	    			gbrA2VO.setSigner( "dr. Sisti Karsinah" );
					gbrA2VO.setTtd("com/ekalife/elions/reports/refund/images/drsisti.gif");
	    		}else{
	    			gbrA2VO.setSigner( "V. Inge" );
					gbrA2VO.setTtd("com/ekalife/elions/reports/refund/images/inge.gif");
	    		}
	    	}
    	}else{
			gbrA2VO.setSigner( "dr. Sisti Karsinah" );
			gbrA2VO.setTtd("com/ekalife/elions/reports/refund/images/drsisti.gif");
    	}
    }
    
}
