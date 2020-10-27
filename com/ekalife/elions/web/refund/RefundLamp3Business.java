package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundLamp3Business
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

import id.co.sinarmaslife.std.util.DateUtil;
import id.co.sinarmaslife.std.util.StringUtil;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.elions.web.refund.vo.Lamp3ParamsVO;
import com.ekalife.elions.web.refund.vo.Lamp3VO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.PreviewEditParamsVO;
import com.ekalife.elions.web.refund.vo.RincianPolisVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.LazyConverter;

public class RefundLamp3Business
{
    protected final Log logger = LogFactory.getLog( getClass() );

    private ElionsManager elionsManager;

    public void setElionsManager( ElionsManager elionsManager )
    {
        this.elionsManager = elionsManager;
    }

    public RefundLamp3Business()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp3Business constructor is called ..." );
    }

    public RefundLamp3Business( ElionsManager elionsManager )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp3Business constructor is called ..." );
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
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp3Business.genStatement" );
        String statement = null;
        if( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( tindakan ) )
        {
        	statement = "Pembayaran premi untuk No. SPAJ:" + StringUtil.nomorSPAJ( spajNo ) +
            "/ No.Polis:" + polisNo +
            " diatas dengan rincian sebagai berikut mohon dikembalikan karena: " + alasan;
        }
        else if( RefundConst.TINDAKAN_GANTI_PLAN.equals( tindakan ) )
        {
        	statement = "Pembayaran premi untuk No. SPAJ:" + StringUtil.nomorSPAJ( spajNo ) +
            "/ No.Polis:" + polisNo +
            " diatas dengan rincian sebagai berikut mohon DIBATALKAN karena: " + alasan;
        }
        return statement;
    }
    
    private String genStatementTindakanTidakAda( String spajNo, String polisNo, String alasan )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp3Business.genStatement" );

        return "Pembayaran premi untuk No. SPAJ:" + StringUtil.nomorSPAJ( spajNo ) +
                "/ No.Polis:" + polisNo +
                " diatas dengan rincian sebagai berikut mohon dibatalkan karena: " + alasan;
    }

    private RincianPolisVO genRincianPolis( String spajNo, String spajLkuId )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp3Business.genRincianPolis" );

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
            rowJumlah = vo.getJumlah().multiply( rowKurs );
            titipan = vo.getTitipanKe() != null? " (titipan premi)" : "";
            tglStr = DateUtil.toIndonesian( vo.getTglSetor() );
            String noPre = vo.getNoPre();
            String noVoucher = vo.getNoVoucher();
            if( noPre == null || "".equals(noPre) )
            {
            	noPre = "-";
            }
            if( noVoucher == null || "".equals(noVoucher) )
            {
            	noVoucher = "-";
            }
            if( rowJumlah ==null)
            {
            	jumlahStr = null;
            }
            else
            {
            	jumlahStr = rowJumlah.toString();
            }
            
            map = new HashMap<String, String>();
            map.put( "jumlahStr",  jumlahStr );
            map.put( "lkuId", rowLkuId );
            map.put( "titipanKe", titipanKe );
            map.put( "tglSetor", tglStr );
            map.put( "tanggalFormat", tgl );
            map.put( "descr", "Premi disetor tgl. " + tglStr + titipan + "( NoPre:" + noPre + "/NoVouc:" + noVoucher+ " )" );
            map.put( "jumlah", ": " + stdCurrency( spajCur, rowJumlah ) );
            map.put( "tanggal", tglStr );
            map.put( "kurs", kurs );
            setoranMapList.add( map );
            allDescrAndJumlah.add( map );

            totalSetor = totalSetor.add( rowJumlah );
        }


        map = new HashMap<String, String>();
        map.put( "descr", "TOTAL SETOR" );
        map.put( "jumlah", ": " + stdCurrency( spajCur, totalSetor ) );
        setoranMapList.add( map );

        result.setSetoranMapList( setoranMapList );
        result.setAllDescrAndJumlah(allDescrAndJumlah);
        
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

    
    
   
    
    public Lamp3VO retrieveLamp3( Lamp3ParamsVO paramsVO, RefundEditForm editForm)
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp3Business.retrieveLamp3" );

        Lamp3VO lamp3VO = new Lamp3VO();

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

        lamp3VO.setNoUrutMemo( "to be generated" );
        lamp3VO.setHal( hal );
        lamp3VO.setTanggal( DateUtil.toIndonesian( elionsManager.selectDate() ) );

        lamp3VO.setSpajNo( StringUtil.nomorSPAJ( spajNo ) );
        lamp3VO.setPolicyNo( polisNo );
        lamp3VO.setProductName( StringUtil.camelHumpAndTrim( oldPolicyInfoVO.getNamaProduk() ) );
        lamp3VO.setPolicyHolderName( StringUtil.camelHumpAndTrim( oldPolicyInfoVO.getNamaPp() ) );
        lamp3VO.setInsuredName( StringUtil.camelHumpAndTrim( oldPolicyInfoVO.getNamaTt() ) );
        lamp3VO.setPrevLspdId( oldPolicyInfoVO.getLspdId() );
        lamp3VO.setBegDate( oldPolicyInfoVO.getBegDate() );
        lamp3VO.setEndDate( oldPolicyInfoVO.getEndDate() );
        
        if( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( editForm.getTindakanCd() ) || RefundConst.TINDAKAN_GANTI_PLAN.equals( editForm.getTindakanCd() ) )
        {
            lamp3VO.setNewSpajNo( StringUtil.nomorSPAJ( newSpajNo ) );
            lamp3VO.setNewPolicyHolderName( StringUtil.camelHumpAndTrim( newPolicyInfoVO.getNamaPp() ) );
            lamp3VO.setNewInsuredName( StringUtil.camelHumpAndTrim( newPolicyInfoVO.getNamaTt() ) );
            lamp3VO.setStatement( genStatement( spajNo, polisNo, paramsVO.getAlasanForLabel(), editForm.getTindakanCd() ) );
            lamp3VO.setNewProductName( StringUtil.camelHumpAndTrim( newPolicyInfoVO.getNamaProduk() ) );
        }
        else if( RefundConst.TINDAKAN_TIDAK_ADA.equals( editForm.getTindakanCd() ) )
        {
        	 lamp3VO.setStatement( genStatementTindakanTidakAda( spajNo, polisNo, paramsVO.getAlasanForLabel() ) );
        }
        
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
        lamp3VO.setTempDescrDanJumlah(temp);
        lamp3VO.setRincianPolisList( rincianList );
//        lamp3VO.setSigner( "V. Inge" );
        lamp3VO.setSigner("Suryanto Lim");

        return lamp3VO;
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
}