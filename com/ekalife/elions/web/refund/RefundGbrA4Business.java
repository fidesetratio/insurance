package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundGbrA4Business
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

public class RefundGbrA4Business
{
    protected final Log logger = LogFactory.getLog( getClass() );

    private ElionsManager elionsManager;

    public void setElionsManager( ElionsManager elionsManager )
    {
        this.elionsManager = elionsManager;
    }

    public RefundGbrA4Business()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA4Business constructor is called ..." );
    }

    public RefundGbrA4Business( ElionsManager elionsManager )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA4Business constructor is called ..." );
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
    
    private String genStatementTindakanTidakAda( String spajNo, String polisNo, String alasan )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA4Business.genStatement" );

        return "Pembayaran premi untuk No. SPAJ:" + StringUtil.nomorSPAJ( spajNo ) +
                "/ No.Polis:" + polisNo +
                " diatas dengan rincian sebagai berikut mohon dibatalkan karena: " + alasan;
    }

    private RincianPolisVO genRincianPolis( String spajNo, String spajLkuId )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA4Business.genRincianPolis" );

        RincianPolisVO result = new RincianPolisVO();

        ArrayList<HashMap<String, String>> setoranMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> allDescrAndJumlah = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map;
        String spajCur = "02".equals( spajLkuId )? "$" : "Rp.";

        String titipan;
        String tglStr;
        BigDecimal rowKurs;
        BigDecimal rowJumlah;
        String rowLkuId;
        String allNoVoucher = null;
        ArrayList<SetoranPremiDbVO> setoranPremiDbVOList = Common.serializableList(elionsManager.selectSetoranPremiBySpaj( spajNo ));

        BigDecimal totalSetor = BigDecimal.ZERO;
        for( SetoranPremiDbVO vo : setoranPremiDbVOList )
        {
            String kurs = null;
            if( vo.getKurs() != null && !"".equals( vo.getKurs() ) )
            {
            	kurs = vo.getKurs().toString();
            }
            // jika rupiah, abaikan nilai kurs di data base (karena ada yg null dan nol)
            SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
            String tgl = df.format(vo.getTglSetor());
            String jumlahStr = null;
            
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

//        map = new HashMap<String, String>();
//        map.put( "descr", "TOTAL SETOR" );
//        map.put( "jumlah", ": " + stdCurrency( spajCur, totalSetor ) );
//        setoranMapList.add( map );

        result.setTotal_setor( totalSetor );
        result.setSetoranMapList( setoranMapList );
        result.setAllDescrAndJumlah(allDescrAndJumlah);
        result.setNoVoucher( allNoVoucher );
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
    
    public GbrA4VO retrieveGbrA4( GbrA4ParamsVO paramsVO, RefundEditForm editForm)
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundGbrA4Business.retrieveGbrA4" );

        GbrA4VO gbrA4VO = new GbrA4VO();

        String spajNo = paramsVO.getSpajNo();
        String newSpajNo = paramsVO.getSpajBaruNo();

        PolicyInfoVO oldPolicyInfoVO = elionsManager.selectPolicyInfoBySpaj( spajNo );
        PolicyInfoVO newPolicyInfoVO = elionsManager.selectPolicyInfoBySpaj( newSpajNo );
        
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

        gbrA4VO.setNoUrutMemo( "to be generated" );
        gbrA4VO.setHal( hal );
        gbrA4VO.setTanggal( DateUtil.toIndonesian( elionsManager.selectDate() ) );

        gbrA4VO.setSpajNo( StringUtil.nomorSPAJ( spajNo ) );
        gbrA4VO.setPolicyNo( polisNo );
        gbrA4VO.setProductName( StringUtil.camelHumpAndTrim( oldPolicyInfoVO.getNamaProduk() ) );
        gbrA4VO.setPolicyHolderName( StringUtil.camelHumpAndTrim( oldPolicyInfoVO.getNamaPp() ) );
        gbrA4VO.setInsuredName( StringUtil.camelHumpAndTrim( oldPolicyInfoVO.getNamaTt() ) );
        gbrA4VO.setPrevLspdId( oldPolicyInfoVO.getLspdId() );
        gbrA4VO.setBegDate( oldPolicyInfoVO.getBegDate() );
        gbrA4VO.setEndDate( oldPolicyInfoVO.getEndDate() );
        gbrA4VO.setStatement( genStatementTindakanTidakAda( spajNo, polisNo, paramsVO.getAlasanForLabel() ) );
        gbrA4VO.setPembatal( oldPolicyInfoVO.getLus_full_name() );
        gbrA4VO.setLca_id( oldPolicyInfoVO.getLca_id() );
        
        RincianPolisVO rincianPolisVO = genRincianPolis(
                spajNo,
                oldPolicyInfoVO.getLkuId()
        );

        ArrayList<HashMap<String, String>> rincianList = cloneList( rincianPolisVO.getSetoranMapList() );
        ArrayList<HashMap<String, String>> allDescrAndJumlah = cloneList( rincianPolisVO.getAllDescrAndJumlah() );
        
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
        	gbrA4VO.setAddLampiranList( tempLampiran );       
        }
        gbrA4VO.setTempDescrDanJumlah(temp);
        gbrA4VO.setRincianPolisList( rincianList );
        setSignerAndTtd(rincianPolisVO.getTotal_setor(), gbrA4VO, oldPolicyInfoVO.getLkuId());
        gbrA4VO.setNoVoucher( rincianPolisVO.getNoVoucher() );
        return gbrA4VO;
    }
    
    private void setSignerAndTtd( BigDecimal premiDikembalikan, GbrA4VO gbrA4VO, String lkuId ){
    	BigDecimal compareValue = BigDecimal.ZERO;
    	if( "02".equals(lkuId)){// dollar
    		compareValue = new BigDecimal("5000");
    	}else{// rupiah
    		compareValue = new BigDecimal("50000000");
    	}
//    	if( premiDikembalikan != null ){
//	    	if( premiDikembalikan.compareTo(compareValue) == 1){
//				gbrA4VO.setSigner( "dr. Sisti Karsinah" );
//				gbrA4VO.setTtd("com/ekalife/elions/reports/refund/images/drsisti.gif");
//	    	}else{
//	    		if("09,40,42,58,62,65".indexOf(gbrA4VO.getLca_id())<=-1){
//	    			gbrA4VO.setSigner( "dr. Sisti Karsinah" );
//	    			gbrA4VO.setTtd("com/ekalife/elions/reports/refund/images/drsisti.gif");
//	    		}else{
//	    			gbrA4VO.setSigner( "V. Inge" );
//	    			gbrA4VO.setTtd("com/ekalife/elions/reports/refund/images/inge.gif");
//	    		}
//	    	}
//    	}else{
//			gbrA4VO.setSigner( "dr. Sisti Karsinah" );
//			gbrA4VO.setTtd("com/ekalife/elions/reports/refund/images/drsisti.gif");
//    	}
    	gbrA4VO.setSigner( "Suryanto Lim" );
    	gbrA4VO.setTtd("com/ekalife/utils/images/suryanto.jpg");
    }

}