package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundGbrA3Business
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 23, 2008 3:26:18 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.elions.web.refund.vo.*;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.LazyConverter;

import id.co.sinarmaslife.std.util.StringUtil;
import id.co.sinarmaslife.std.util.DateUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class RefundGbrA3Business
{
    protected final Log logger = LogFactory.getLog( getClass() );

    private ElionsManager elionsManager;

    public void setElionsManager( ElionsManager elionsManager )
    {
        this.elionsManager = elionsManager;
    }

    public RefundGbrA3Business()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA3Business constructor is called ..." );
    }

    public RefundGbrA3Business( ElionsManager elionsManager )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA3Business constructor is called ..." );
        setElionsManager( elionsManager );
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

    private String genStatement( String spajNo, String polisNo, String alasan, Integer tindakan )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA3Business.genStatement" );
        String statement = null;
        if( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( tindakan ) )
        {
        	statement = "Pembayaran premi untuk No. SPAJ:" + StringUtil.nomorSPAJ( spajNo ) +
            "/ No.Polis:" + polisNo +
            " diatas dengan rincian sebagai berikut mohon dibatalkan karena: " + alasan;
        }
        else if( RefundConst.TINDAKAN_GANTI_PLAN.equals( tindakan ) )
        {
        	statement = "Pembayaran premi untuk No. SPAJ:" + StringUtil.nomorSPAJ( spajNo ) +
            "/ No.Polis:" + polisNo +
            " diatas dengan rincian sebagai berikut mohon DIBATALKAN karena: " + alasan;
        }
        return statement;
    }

    private RincianPolisVO genRincianPolis( String spajNo, String spajLkuId, BigDecimal biayaAdmin, 
    		ArrayList<PenarikanUlinkVO> penarikanUlinkVOList, BigDecimal biayaMedis, BigDecimal biayaLain, String biayaLainDescr )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA3Business.genRincianPolis" );

        RincianPolisVO result = new RincianPolisVO();

        ArrayList<HashMap<String, String>> setoranMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> allDescrAndJumlah = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> penarikanUlinkMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> biayaStandarMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> summaryMapList = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map;
        String spajCur = "02".equals( spajLkuId )? "$" : "Rp.";
        String allNoVoucher = null;
        String titipan;
        String tglStr;
        BigDecimal rowKurs;
        BigDecimal rowJumlah;
        String rowLkuId;
        ArrayList<SetoranPremiDbVO> setoranPremiDbVOList = Common.serializableList(elionsManager.selectSetoranPremiBySpaj( spajNo ));

        BigDecimal totalSetor = BigDecimal.ZERO;
        for( SetoranPremiDbVO vo : setoranPremiDbVOList )
        {
            // jika rupiah, abaikan nilai kurs di data base (karena ada yg null dan nol)
            SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
            String tgl = df.format(vo.getTglSetor());
            String jumlahStr = null;
            String kurs = null;
            if( vo.getKurs() != null && !"".equals( vo.getKurs() ) )
            {
            	kurs = vo.getKurs().toString();
            }
            
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
            rowKurs = "02".equals( vo.getLkuId() )? vo.getKurs() : BigDecimal.ONE;
            String titipanKe = null;
            if(vo.getTitipanKe() == null)
            {
            	titipanKe = null;
            }
            else
            {
            	titipanKe = vo.getTitipanKe().toString();
            }
            rowJumlah = vo.getJumlah();
            titipan = vo.getTitipanKe() != null? " (titipan premi)" : "";
            tglStr = DateUtil.toIndonesian( vo.getTglSetor() );
            String noPre = vo.getNoPre();
            String noVoucher = vo.getNoVoucher();
            SimpleDateFormat formatDate = new SimpleDateFormat("/MM/yy");
            if( noPre == null || "".equals(noPre) ){noPre = "-";}
           
            if( noVoucher == null || "".equals(noVoucher) ){noVoucher = "-";}
            else{noVoucher = noVoucher + formatDate.format( vo.getTglSetor() );}
            
            if( rowJumlah ==null){jumlahStr = null;}
            else{jumlahStr = rowJumlah.toString();}
            
            map = new HashMap<String, String>();
            map.put( "jumlahStr",  jumlahStr );
            map.put( "lkuId", rowLkuId );
            map.put( "titipanKe", titipanKe );
            map.put( "tglSetor", tglStr );
            map.put( "tanggalFormat", tgl );
            map.put( "descr", "Premi disetor tgl. " + tglStr + titipan );
            map.put( "jumlah", ": " + stdCurrency( spajCur, rowJumlah ) );
            map.put( "tanggal", tglStr );
            map.put( "kurs", kurs );
            setoranMapList.add( map );
            allDescrAndJumlah.add( map );
            if( allNoVoucher == null )
            {
            	allNoVoucher = noVoucher;
            }
            else
            {
            	allNoVoucher = allNoVoucher + "," + noVoucher;
            }

            totalSetor = totalSetor.add( rowJumlah );
        }
        
//        if( penarikanUlinkVOList != null && penarikanUlinkVOList.size() > 0 )
//        {
//            BigDecimal totalPenarikanUlink = BigDecimal.ZERO;
//            for( PenarikanUlinkVO vo : penarikanUlinkVOList )
//            {
//                String descr = "Penarikan " + vo.getJumlahUnit() + " unit " + vo.getLjiInvest();
//                BigDecimal voJumlah = vo.getJumlah();
//                String jumlahPenarikan = "";
//                if(voJumlah == null)
//                {
//                	jumlahPenarikan = "0";
//                }
//                else if(voJumlah!=null)
//                {
//                	jumlahPenarikan = 	voJumlah.toString();
//                }
//                map = new HashMap<String, String>();
//                map.put( "descr", descr );
//                map.put( "lkuId", spajLkuId );
//                map.put( "biayaStandarOrNot","yes" );
//                map.put( "jumlah", ": " + stdCurrency( "Rp.", vo.getJumlah() ) );
//                map.put( "jumlahStr",  jumlahPenarikan  );
//                penarikanUlinkMapList.add( map );
//                allDescrAndJumlah.add(map);
//                BigDecimal addition = vo.getJumlah() == null? BigDecimal.ZERO : vo.getJumlah();
//                totalPenarikanUlink = totalPenarikanUlink.add( addition );
//                if( vo.getJumlah() != null && !BigDecimal.ZERO.equals( vo.getJumlah() ) )
//                {
//                	totalSetor = totalSetor.add( vo.getJumlah() );	
//                }
//
//            }
//        }
        if(biayaAdmin!= null && !BigDecimal.ZERO.equals(biayaAdmin))
        {
        	map = new HashMap<String, String>();
        	map.put( "descr","Biaya administrasi pembatalan polis" );
            map.put( "biayaStandarOrNot", "yes" );
        	map.put( "jumlah",  ": " + "(" + stdCurrency( spajCur, biayaAdmin ) + ")" );
        	allDescrAndJumlah.add(map);
        	biayaStandarMapList.add( map );
            totalSetor = totalSetor.subtract( biayaAdmin );
        }
        if( biayaMedis!=null && !BigDecimal.ZERO.equals( biayaMedis ) )
        {
        	map = new HashMap<String, String>();
            map.put( "descr", "Biaya medis" );
            map.put("biayaStandarOrNot", "yes");
            map.put( "jumlah", ": " + "("  + stdCurrency( spajCur, biayaMedis ) + ")");
            allDescrAndJumlah.add(map);
            biayaStandarMapList.add( map );
            totalSetor = totalSetor.subtract( biayaMedis );
        }
        if( biayaLain!=null && !BigDecimal.ZERO.equals( biayaLain ) )
        {
            map = new HashMap<String, String>();
            map.put( "descr", biayaLainDescr );
            map.put("biayaStandarOrNot", "yes");
            map.put( "jumlah", ": " + "("  + stdCurrency( spajCur, biayaLain ) + ")");
            allDescrAndJumlah.add(map);
            biayaStandarMapList.add( map );
            totalSetor = totalSetor.subtract( biayaLain );
        }
        
        map = new HashMap<String, String>();
        map.put( "descr", "Premi dikembalikan" );
        map.put( "jumlah", ": " + stdCurrency( spajCur, totalSetor ) );
        summaryMapList.add( map );
        
        result.setTotal_setor( totalSetor );
        result.setSetoranMapList( setoranMapList );
        result.setAllDescrAndJumlah(allDescrAndJumlah);
        result.setPenarikanUlinkMapList( penarikanUlinkMapList );
        result.setBiayaStandarList( biayaStandarMapList );
        result.setNoVoucher( allNoVoucher );
        result.setSummaryMapList( summaryMapList );
        return result;
    }

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
    
    public GbrA3VO retrieveGbrA3( GbrA3ParamsVO paramsVO, RefundEditForm editForm)
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA3Business.retrieveGbrA3" );

        GbrA3VO gbrA3VO = new GbrA3VO();

        String spajNo = paramsVO.getSpajNo();
        String[] newSpajNo = paramsVO.getSpajBaruNo().split(",");
        String newSpajNoList = null;
        String newPolicyHolderNameList = null;
        String newInsuredName = null;
        String newProductName = null;
        String policyHolderAndInsuredName = null;
        
        PolicyInfoVO oldPolicyInfoVO = elionsManager.selectPolicyInfoBySpaj( spajNo );
        
        if( newSpajNo != null && newSpajNo.length > 0 )
        {
        	for ( int i = 0 ; i < newSpajNo.length ; i ++ )
        	{        		
        		PolicyInfoVO newPolicyInfoVO = elionsManager.selectPolicyInfoBySpaj( newSpajNo[i] );
        		if( newSpajNoList == null )
        		{
        			newSpajNoList = StringUtil.nomorSPAJ(newPolicyInfoVO.getSpajNo());
        			newPolicyHolderNameList = StringUtil.camelHumpAndTrim( newPolicyInfoVO.getNamaPp() );
        			newInsuredName = StringUtil.camelHumpAndTrim( newPolicyInfoVO.getNamaTt() );
        			newProductName = StringUtil.camelHumpAndTrim( newPolicyInfoVO.getNamaProduk() );
        			policyHolderAndInsuredName = newPolicyHolderNameList + " / " + newInsuredName;
        		}
        		else
        		{
        			newSpajNoList = newSpajNoList + ", " + StringUtil.nomorSPAJ(newPolicyInfoVO.getSpajNo());
        			newPolicyHolderNameList = newPolicyHolderNameList + ", " + StringUtil.camelHumpAndTrim( newPolicyInfoVO.getNamaPp() );
        			newInsuredName = newInsuredName + ", " + StringUtil.camelHumpAndTrim( newPolicyInfoVO.getNamaTt() );
        			newProductName = newProductName + ", " + StringUtil.camelHumpAndTrim( newPolicyInfoVO.getNamaProduk() );
        			policyHolderAndInsuredName = policyHolderAndInsuredName + ", \n" + newPolicyHolderNameList + " / " + newInsuredName;
        		}
        	}
        }
        

        String polisNo ;

        if( oldPolicyInfoVO.getPolicyNo() == null || oldPolicyInfoVO.getPolicyNo().trim().equals( "" ) )
        {
            if( oldPolicyInfoVO.getNamaPp() != null && !oldPolicyInfoVO.getNamaPp().trim().equals( "" ) )
            {
//                polisNo = " atas nama " + oldPolicyInfoVO.getNamaPp();
            	polisNo = "- ";
            }
            else
            {
                polisNo = "";
            }
        }
        else
        {
            polisNo =  StringUtil.nomorPolis( oldPolicyInfoVO.getPolicyNo() );
        }

        String spaj_polis = FormatString.nomorSPAJ( spajNo ) + " / " + polisNo;

        String hal = "Pembatalan Polis : SPAJ " + spaj_polis;

        gbrA3VO.setNoUrutMemo( "to be generated" );
        gbrA3VO.setHal( hal );
        gbrA3VO.setTanggal( DateUtil.toIndonesian( elionsManager.selectDate() ) );

        gbrA3VO.setSpajNo( StringUtil.nomorSPAJ( spajNo ) );
        gbrA3VO.setPolicyNo( polisNo );
        gbrA3VO.setProductName( StringUtil.camelHumpAndTrim( oldPolicyInfoVO.getNamaProduk() ) );
        gbrA3VO.setPolicyHolderName( StringUtil.camelHumpAndTrim( oldPolicyInfoVO.getNamaPp() ) );
        gbrA3VO.setInsuredName( StringUtil.camelHumpAndTrim( oldPolicyInfoVO.getNamaTt() ) );
        gbrA3VO.setPrevLspdId( oldPolicyInfoVO.getLspdId() );
        gbrA3VO.setBegDate( oldPolicyInfoVO.getBegDate() );
        gbrA3VO.setEndDate( oldPolicyInfoVO.getEndDate() );
        gbrA3VO.setNewSpajNo( newSpajNoList );
        gbrA3VO.setNewPolicyHolderName( newPolicyHolderNameList );
        gbrA3VO.setNewInsuredName( newInsuredName );
        gbrA3VO.setStatement( genStatement( spajNo, polisNo, paramsVO.getAlasanForLabel(), editForm.getTindakanCd() ) );
        gbrA3VO.setNewProductName( newProductName );
        gbrA3VO.setPolicyHolderAndInsuredName(policyHolderAndInsuredName);
        gbrA3VO.setPembatal( oldPolicyInfoVO.getLus_full_name() );
        gbrA3VO.setLca_id( oldPolicyInfoVO.getLca_id() );
        RincianPolisVO rincianPolisVO = genRincianPolis(
                spajNo,
                oldPolicyInfoVO.getLkuId(),
                paramsVO.getBiayaAdmin(),
                paramsVO.getPenarikanUlinkList(),
                paramsVO.getBiayaMedis(),
                paramsVO.getBiayaLain(),
                paramsVO.getBiayaLainDescr()
        );

        ArrayList<HashMap<String, String>> allDescrAndJumlah = cloneList( rincianPolisVO.getAllDescrAndJumlah() );
        
        ArrayList<HashMap<String, String>> rincianList = new ArrayList<HashMap<String,String>>();
        rincianList = cloneList( rincianPolisVO.getSetoranMapList() );
        rincianList.addAll( cloneList( rincianPolisVO.getPenarikanUlinkMapList() ) );
        rincianList.addAll( cloneList( rincianPolisVO.getBiayaStandarList() ) );
        rincianList.addAll( cloneList( rincianPolisVO.getSummaryMapList() ) );
        
        ArrayList <PreviewEditParamsVO> temp = new ArrayList<PreviewEditParamsVO>();
        for( int i = 0 ; i < allDescrAndJumlah.size() ; i ++ )
    	 {
      		 String lkuId = allDescrAndJumlah.get(i).get("lkuId");
    		 String ljbId = allDescrAndJumlah.get(i).get("ljbId");
			 String titipanKeStr = allDescrAndJumlah.get(i).get("titipanKe");
			 String noPre = allDescrAndJumlah.get(i).get("noPre");
			 String noVoucher = allDescrAndJumlah.get(i).get("noVoucher");
			 String kurs = allDescrAndJumlah.get(i).get("kurs");
			 BigDecimal kursBigDecimal = BigDecimal.ZERO;
			 if( kurs != null && !"".equals( kurs ) )
			 {
				 kursBigDecimal = new BigDecimal( kurs );
			 }
			 String jumlahStr = null;
			 Integer titipanKe = null;
			 Date tanggal = null;
			 if(allDescrAndJumlah.get(i).get("jumlahStr") == null)
			 {
				 jumlahStr = "0";
			 }
			 else
			 {
				 jumlahStr = allDescrAndJumlah.get(i).get("jumlahStr");
			 }
			 if( titipanKeStr == null )
			 {
				 titipanKe = null;
			 }
			 else if( titipanKeStr != null )
			 {
				 titipanKe = LazyConverter.toInt(allDescrAndJumlah.get(i).get("titipanKe"));
			 }
			 String descr = allDescrAndJumlah.get(i).get("descr");
			 BigDecimal jumlahSetoran  = new BigDecimal(jumlahStr);
			 BigDecimal jumlahDebet  = new BigDecimal("0");
			 BigDecimal jumlahKredit  = new BigDecimal("0");
			 String tglStr = allDescrAndJumlah.get(i).get("tanggalFormat");
			 if(tglStr !=null)
			 {
				 tanggal = FormatDate.toDate(tglStr);
			 }
			 String setoranOrNot = "yes";
			 temp.add( new PreviewEditParamsVO( descr,jumlahSetoran,jumlahDebet,jumlahKredit, setoranOrNot,lkuId, titipanKe, tanggal, ljbId, CommonConst.DISABLED_TRUE, null, noPre, noVoucher, kursBigDecimal ) );
    	 }
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
        	gbrA3VO.setAddLampiranList( tempLampiran );       
        }
        gbrA3VO.setTempDescrDanJumlah(temp);
        gbrA3VO.setRincianPolisList( rincianList );
        setSignerAndTtd( rincianPolisVO.getTotal_setor(), gbrA3VO, oldPolicyInfoVO.getLkuId() );
        gbrA3VO.setNoVoucher( rincianPolisVO.getNoVoucher() );
        return gbrA3VO;
    }
    
    private void setSignerAndTtd( BigDecimal premiDikembalikan, GbrA3VO gbrA3VO, String lkuId ){
    	BigDecimal compareValue = BigDecimal.ZERO;
    	if( "02".equals(lkuId)){// dollar
    		compareValue = new BigDecimal("5000");
    	}else{// rupiah
    		compareValue = new BigDecimal("50000000");
    	}
//    	if( premiDikembalikan != null ){
//	    	if( premiDikembalikan.compareTo(compareValue) == 1){
//				gbrA3VO.setSigner( "dr. Sisti Karsinah" );
//				gbrA3VO.setTtd("com/ekalife/elions/reports/refund/images/drsisti.gif");
//	    	}else{
//	    		if("09,42,62,65".indexOf(gbrA3VO.getLca_id())<=-1){
//	    			gbrA3VO.setSigner( "dr. Sisti Karsinah" );
//	    			gbrA3VO.setTtd("com/ekalife/elions/reports/refund/images/drsisti.gif");
//	    		}else{
//	    			gbrA3VO.setSigner( "V. Inge" );
//	    			gbrA3VO.setTtd("com/ekalife/elions/reports/refund/images/inge.gif");
//	    		}
//	    		
//	    	}
//    	}else{
//			gbrA3VO.setSigner( "dr. Sisti Karsinah" );
//			gbrA3VO.setTtd("com/ekalife/elions/reports/refund/images/drsisti.gif");
//    	}
    	gbrA3VO.setSigner( "Suryanto Lim" );
    	gbrA3VO.setTtd("com/ekalife/utils/images/suryanto.jpg");
    }
    
}