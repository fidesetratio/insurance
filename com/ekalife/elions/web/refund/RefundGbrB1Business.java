package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundGbrB1Business
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
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.elions.web.refund.vo.GbrA2VO;
import com.ekalife.elions.web.refund.vo.GbrA4VO;
import com.ekalife.elions.web.refund.vo.GbrB1ParamsVO;
import com.ekalife.elions.web.refund.vo.GbrB1VO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.RincianPolisVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;
import com.ekalife.utils.AngkaTerbilang;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatString;

public class RefundGbrB1Business
{
    protected final Log logger = LogFactory.getLog( getClass() );

    private ElionsManager elionsManager;

    public void setElionsManager( ElionsManager elionsManager )
    {
        this.elionsManager = elionsManager;
    }

    public RefundGbrB1Business()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrB1Business constructor is called ..." );
    }

    public RefundGbrB1Business( ElionsManager elionsManager )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrB1Business constructor is called ..." );
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
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrB1Business.genStatement" );

        return "Pembayaran premi untuk No. SPAJ:" + StringUtil.nomorSPAJ( spajNo ) +
                "/ No.Polis:" + polisNo +
                " diatas dengan rincian sebagai berikut mohon dikembalikan/ dibatalkan karena: " + alasan;
    }

    private RincianPolisVO genRincianPolis( String spajNo, String spajLkuId, BigDecimal biayaAdmin, BigDecimal biayaMedis, BigDecimal biayaLain, String biayaLainDescr, BigDecimal biayaMerchant )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrB1Business.genRincianPolis" );

        RincianPolisVO result = new RincianPolisVO();
        ArrayList<HashMap<String, String>> setoranMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> penarikanUlinkMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> biayaUlinkMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> biayaStandarMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> summaryMapList = new ArrayList<HashMap<String, String>>();
        
        HashMap<String, String> map;
        BigDecimal premiDikembalikan;
        String spajCur = "02".equals( spajLkuId )? "$" : "Rp.";

        String titipan;
        String tglStr;
        String rowDescr;
        String rowLkuId;
        BigDecimal rowKurs;
        BigDecimal rowJumlah;
        BigDecimal adjustedJumlah;
        ArrayList<SetoranPremiDbVO> setoranPremiDbVOList = Common.serializableList(elionsManager.selectSetoranPremiBySpaj( spajNo ));
        String allNoVoucher = null;
        BigDecimal totalSetor = BigDecimal.ZERO;
        
        for( SetoranPremiDbVO vo : setoranPremiDbVOList ) {
        	// jika rupiah, abaikan nilai kurs di data base (karena ada yg null dan nol)
    		if( "02".equals( vo.getLkuId() ) )
    		{
    			rowKurs = vo.getKurs();
    			rowLkuId = "02";
    		}
            else
            {
            	rowKurs = BigDecimal.ONE;
            	rowLkuId = "01";
            }

              //todo
            String originalCur = "";
            rowJumlah = vo.getJumlah();
            if( !rowLkuId.equals( spajLkuId ) )
            {
            	String cur = "02".equals( rowLkuId )? "$" : "Rp.";
            	originalCur = stdCurrency( cur, rowJumlah );
            	adjustedJumlah = rowJumlah.divide( rowKurs, MathContext.DECIMAL64 );
            }
            else
            {
            	adjustedJumlah = rowJumlah;
            }
              //end todo

            titipan = vo.getTitipanKe() != null? " (titipan premi)" : "";
            tglStr = DateUtil.toIndonesian( vo.getTglSetor() );
            String noPre = vo.getNoPre();
            String noVoucher = vo.getNoVoucher();
            SimpleDateFormat formatDate = new SimpleDateFormat("/MM/yy");
			
            if( noPre == null || "".equals(noPre) ){noPre = "-";}
			  
            if( noVoucher == null || "".equals(noVoucher) ){ noVoucher = "-"; }
            else{ noVoucher = noVoucher + formatDate.format( vo.getTglSetor() ); }
            
            map = new HashMap<String, String>();
            map.put( "descr", "Premi disetor tgl. " + tglStr + titipan + originalCur );
            map.put( "jumlah", ": " + stdCurrency( spajCur, adjustedJumlah ) );
            map.put( "tanggal", tglStr );
            setoranMapList.add( map );
			
            totalSetor = totalSetor.add( rowJumlah );
            if( allNoVoucher == null )
            {
                allNoVoucher = noVoucher;
            }
            else
            {
            	allNoVoucher = allNoVoucher + "," + noVoucher;
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
	
        result.setTotal_setor( premiDikembalikan );
        result.setSetoranMapList( setoranMapList );
        result.setPenarikanUlinkMapList( penarikanUlinkMapList );
        result.setBiayaUlinkMapList( biayaUlinkMapList );
        result.setBiayaStandarList( biayaStandarMapList );
        result.setSummaryMapList( summaryMapList );
        result.setTotalPremiDikembalikan( premiDikembalikan );
        result.setNoVoucher( allNoVoucher );
        
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
    
    
    public GbrB1VO retrieveGbrB1( GbrB1ParamsVO paramsVO, RefundEditForm editForm )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrB1Business.retrieveGbrB1" );

        GbrB1VO gbrB1VO = new GbrB1VO();

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
        gbrB1VO.setNoUrutMemo( "-" );
        gbrB1VO.setHal( hal );
        gbrB1VO.setTanggal( DateUtil.toIndonesian( elionsManager.selectDate() ) );
        
        gbrB1VO.setSpajNo( StringUtil.nomorSPAJ( spajNo ) );
        gbrB1VO.setPolisNo( polisNo );
        gbrB1VO.setProduk( StringUtil.camelHumpAndTrim( policyInfoVO.getNamaProduk() ) );
        gbrB1VO.setPemegangPolis( StringUtil.camelHumpAndTrim( policyInfoVO.getNamaPp() ) );
        gbrB1VO.setTertanggung( StringUtil.camelHumpAndTrim( policyInfoVO.getNamaTt() ) );
        gbrB1VO.setPrevLspdId( policyInfoVO.getLspdId() );
        gbrB1VO.setEndDate( policyInfoVO.getEndDate() );
        gbrB1VO.setBegDate( policyInfoVO.getBegDate() );
        gbrB1VO.setPembatal( policyInfoVO.getLus_full_name() );
        gbrB1VO.setLca_id( policyInfoVO.getLca_id() );
        
        gbrB1VO.setStatement( genStatement( spajNo, polisNo, paramsVO.getAlasanForLabel() ) );
        
        RincianPolisVO rincianPolisVO = genRincianPolis(
                spajNo,
                policyInfoVO.getLkuId(),
                paramsVO.getBiayaAdmin(),
                paramsVO.getBiayaMedis(),
                paramsVO.getBiayaLain(),
                paramsVO.getBiayaLainDescr(),
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

        gbrB1VO.setRincianPolisList( rincianList );

        gbrB1VO.setJumlahTerbilang( "(" + AngkaTerbilang.indonesian(
               rincianPolisVO.getTotalPremiDikembalikan().toString(), policyInfoVO.getLkuId() ).toLowerCase() + ")" );
        
        gbrB1VO.setRincianPolisList( rincianList );       

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
            	gbrB1VO.setAddLampiranList( tempLampiran );       
            }
    	
        gbrB1VO.setAtasNama( StringUtil.camelHumpAndTrim( paramsVO.getAtasNama() ) );        
        gbrB1VO.setRekeningNo( paramsVO.getNorek() );
        gbrB1VO.setBankName( paramsVO.getNamaBank() );        
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
        gbrB1VO.setCabang( cabangBank );
        gbrB1VO.setKota( kotaBank );
        setSignerAndTtd( rincianPolisVO.getTotal_setor(), gbrB1VO, policyInfoVO.getLkuId() );
        gbrB1VO.setTotalPremiDikembalikan( rincianPolisVO.getTotalPremiDikembalikan() );
        gbrB1VO.setNoVoucher( rincianPolisVO.getNoVoucher() );
        return gbrB1VO;
    }
    
    private void setSignerAndTtd( BigDecimal premiDikembalikan, GbrB1VO gbrB1VO, String lkuId ){
    	BigDecimal compareValue = BigDecimal.ZERO;
    	if( "02".equals(lkuId)){// dollar
    		compareValue = new BigDecimal("5000");
    	}else{// rupiah
    		compareValue = new BigDecimal("50000000");
    	}
    	if( premiDikembalikan != null ){
	    	if( premiDikembalikan.compareTo(compareValue) == 1){
				gbrB1VO.setSigner( "dr. Sisti Karsinah" );
				gbrB1VO.setTtd("com/ekalife/elions/reports/refund/images/drsisti.gif");
	    	}else{
	    		if("09,40,42,58,62,65".indexOf(gbrB1VO.getLca_id())<=-1){
	    			gbrB1VO.setSigner( "dr. Sisti Karsinah" );
	    			gbrB1VO.setTtd("com/ekalife/elions/reports/refund/images/drsisti.gif");
	    		}else{
	    			gbrB1VO.setSigner( "V. Inge" );
	    			gbrB1VO.setTtd("com/ekalife/elions/reports/refund/images/inge.gif");
	    		}
	    	}
    	}else{
			gbrB1VO.setSigner( "dr. Sisti Karsinah" );
			gbrB1VO.setTtd("com/ekalife/elions/reports/refund/images/drsisti.gif");
    	}
    }
    
}
