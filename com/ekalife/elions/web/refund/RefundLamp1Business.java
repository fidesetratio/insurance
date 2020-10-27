package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundLamp1Business
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

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.model.refund.RefundLookupForm;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.elions.web.refund.vo.BiayaUlinkDbVO;
import com.ekalife.elions.web.refund.vo.Lamp1ParamsVO;
import com.ekalife.elions.web.refund.vo.Lamp1VO;
import com.ekalife.elions.web.refund.vo.PenarikanUlinkDbVO;
import com.ekalife.elions.web.refund.vo.PenarikanUlinkVO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.PreviewEditParamsVO;
import com.ekalife.elions.web.refund.vo.RefundDetDbVO;
import com.ekalife.elions.web.refund.vo.RincianPolisVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;
import com.ekalife.utils.AngkaTerbilang;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.LazyConverter;

public class RefundLamp1Business implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected final Log logger = LogFactory.getLog( getClass() );

    private ElionsManager elionsManager;

    public void setElionsManager( ElionsManager elionsManager )
    {
        this.elionsManager = elionsManager;
    }

    public RefundLamp1Business()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Business constructor is called ..." );
    }

    public RefundLamp1Business( ElionsManager elionsManager )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Business constructor is called ..." );
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
    
    public boolean isUnitLink( String spajNo )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Business.isUnitLink" );
        if(spajNo!=null)
        {
        	spajNo = spajNo.trim();
        }
        PolicyInfoVO policyInfoVO = elionsManager.selectPolicyInfoBySpaj( spajNo );
        boolean result = false;
        if( policyInfoVO != null && policyInfoVO.getLsgbId() != null )
        {
            result = policyInfoVO.getLsgbId().equals( RefundConst.UNIT_LINK );
        }
        return result;
    }

    private String genStatement( String spajNo, String polisNo, String alasan )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Business.genStatement" );

        return "Pembayaran premi untuk No. SPAJ:" + StringUtil.nomorSPAJ( spajNo ) +
                "/ No.Polis:" + polisNo +
                " diatas dengan rincian sebagai berikut mohon dikembalikan karena: " + alasan;
    }

    private RincianPolisVO genRincianPolis( String spajNo, String spajLkuId, BigDecimal biayaAdmin, BigDecimal biayaMedis, BigDecimal biayaLain, String biayaLainDescr, ArrayList<PenarikanUlinkVO> penarikanUlinkVOList, 
    		ArrayList<PreviewEditParamsVO> tempDescrDanJumlah,
    		BigDecimal totalPremiDikembalikan, String uLinkOrNot, ArrayList<PenarikanUlinkVO> penarikanULink, String statusUnit, Integer posisi, RefundEditForm editForm, Integer checkSpaj)
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Business.genRincianPolis" );

        RincianPolisVO result = new RincianPolisVO();
        ArrayList<HashMap<String, String>> setoranMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> penarikanUlinkMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> biayaUlinkMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> biayaStandarMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> summaryMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> allDescrAndJumlah = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> allDataFromDbDetRefund = new ArrayList<HashMap<String, String>>();
        
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

        BigDecimal totalSetor = BigDecimal.ZERO;
      
        
        if( editForm.getSpaj() != null && isUnitLink( editForm.getSpaj() ) && editForm.getHasUnitFlag() != null && editForm.getHasUnitFlag() == 1 )
        {
	        if(tempDescrDanJumlah!=null)
	        {
	        	for(int i = 0 ; i < tempDescrDanJumlah.size() ; i ++)
	        	{
	        		if(!"yes".equals(tempDescrDanJumlah.get(i).getBiayaStandarOrNot()))
	        		{
		        		String kurs = "02".equals( tempDescrDanJumlah.get(i).getLkuId() )? "$" : "Rp.";
		        		String tglSetor = null;
		        		String titipanKe = null;
		        		if(tempDescrDanJumlah.get(i).getTglSetor()!=null)
		        		{
		        			tglSetor = DateUtil.toIndonesian( tempDescrDanJumlah.get(i).getTglSetor() );
		        		}
		        		if(tempDescrDanJumlah.get(i).getTitipanKe()!=null)
		        		{
		        			titipanKe = tempDescrDanJumlah.get(i).getTitipanKe().toString();
		        		}
		        		map = new HashMap<String, String>();
		        		map.put( "setoranOrNot", tempDescrDanJumlah.get(i).getSetoranOrNot() );
		                map.put( "descr", tempDescrDanJumlah.get(i).getDescr() );
		                map.put( "lkuId", tempDescrDanJumlah.get(i).getLkuId() );
		        		map.put( "ljbId", tempDescrDanJumlah.get(i).getLjbId() );
		        		map.put( "biayaStandarOrNot", tempDescrDanJumlah.get(i).getBiayaStandarOrNot() );
		                map.put( "tglSetor", tglSetor );
		                map.put( "titipanKe", titipanKe );
		                map.put( "noPre", tempDescrDanJumlah.get(i).getNoPre() );
		                map.put( "noVoucher", tempDescrDanJumlah.get(i).getNoVoucher() );
		                if(tempDescrDanJumlah.get(i).getJumlahDebet()!=null && !BigDecimal.ZERO.equals(tempDescrDanJumlah.get(i).getJumlahDebet()))
		                {
		                	map.put( "jumlah", ": " + stdCurrency(  kurs, tempDescrDanJumlah.get(i).getJumlahDebet() ) );
		                	map.put( "jumlahStr",  tempDescrDanJumlah.get(i).getJumlahDebet() .toString() );
		                }
		                else if(tempDescrDanJumlah.get(i).getJumlahKredit()!=null && !BigDecimal.ZERO.equals(tempDescrDanJumlah.get(i).getJumlahKredit()) )
		                {
		                	map.put( "jumlah", ": " + stdCurrency(  kurs, tempDescrDanJumlah.get(i).getJumlahKredit() ) );
		                	map.put( "jumlahStr",tempDescrDanJumlah.get(i).getJumlahKredit().toString() );
		                }
		                else if(tempDescrDanJumlah.get(i).getJumlahPremi()!=null && !BigDecimal.ZERO.equals(tempDescrDanJumlah.get(i).getJumlahPremi()) )
		                {
		                	map.put( "jumlah", ": " + stdCurrency(  kurs, tempDescrDanJumlah.get(i).getJumlahPremi() ) );
		                	map.put( "jumlahStr",tempDescrDanJumlah.get(i).getJumlahPremi().toString()  );
		                }
		                else 
		                {
		                	map.put( "jumlah", ": " + stdCurrency(  kurs, new BigDecimal("0") ) );
		                	map.put( "jumlahStr", "0" );
		                }
		                allDescrAndJumlah.add(map);
		                allDescrAndJumlah.addAll(genSpace());
	        		}
	        	result.setAllDescrAndJumlah(allDescrAndJumlah);
	        	}
	        }
	        if(penarikanULink!=null && penarikanULink.size() >0)
	        {
	        	map = new HashMap<String, String>();
	        	map = Common.serializableMap((HashMap)genPenarikanULink(penarikanULink, spajCur));
	//        	if(posisi!=null && posisi >= 2)
	//        	{
		        	allDescrAndJumlah.add(map);
		        	allDescrAndJumlah.addAll(genSpace());
	//        	}
	        }
	        if(biayaAdmin!= null && !BigDecimal.ZERO.equals(biayaAdmin))
	        {
	        	map = new HashMap<String, String>();
	        	map.put("descr","Biaya administrasi pembatalan polis");
	            map.put("biayaStandarOrNot", "yes");
	        	map.put("jumlah",  ": " + "(" + stdCurrency( spajCur, biayaAdmin ) + ")" );
	        	allDescrAndJumlah.add(map);
	        	allDescrAndJumlah.addAll(genSpace());
	        }
	        if(biayaMedis!=null && !BigDecimal.ZERO.equals(biayaMedis))
	        {
	        	map = new HashMap<String, String>();
	            map.put( "descr", "Biaya medis" );
	            map.put("biayaStandarOrNot", "yes");
	            map.put( "jumlah", ": " + "("  + stdCurrency( spajCur, biayaMedis ) + ")" );
	         	allDescrAndJumlah.add(map);
	            allDescrAndJumlah.addAll(genSpace());
	        }
	        if(biayaLain!=null && !BigDecimal.ZERO.equals(biayaLain))
	        {
	            map = new HashMap<String, String>();
	            map.put( "descr", biayaLainDescr );
	            map.put("biayaStandarOrNot", "yes");
	            map.put( "jumlah", ": " + "("  + stdCurrency( spajCur, biayaLain ) + ")");
	            allDescrAndJumlah.add(map);
	            allDescrAndJumlah.addAll(genSpace());
	        }
	        if(totalPremiDikembalikan!=null && !BigDecimal.ZERO.equals(totalPremiDikembalikan))
	        {
	           	map = new HashMap<String, String>();
	            map.put( "descr", "Premi dikembalikan" );
	            map.put("biayaStandarOrNot", "yes");
	            map.put( "jumlah", ": " + stdCurrency( spajCur, totalPremiDikembalikan ) );
	         	allDescrAndJumlah.add(map);
	        }
	        if(checkSpaj > 0 && tempDescrDanJumlah ==null || checkSpaj > 0 && tempDescrDanJumlah.size() == 0)// jika spaj sdh di simpan di DB(mst_det_refund)
	        {
	        	HashMap<String, Object> params = new HashMap<String, Object>();
	        	params.put("regSpaj", spajNo);
	        	ArrayList<RefundDetDbVO> selectRefundDetList = Common.serializableList(elionsManager.selectRefundDetList(params));
	        	for(int i = 0 ; i < selectRefundDetList.size() ; i ++)
	        	{
	        		if(!"WITHDRAW".equals(selectRefundDetList.get(i).getTipe().toString()) && !"Biaya Administrasi Pembatalan Polis".equals(selectRefundDetList.get(i).getDeskripsi()))
	        		{
	        			String jumlahStr = "";
	            		String ljbId = "";
	            		String tipe = "";
	            		String unit = "";
	            		String lkuId = RefundConst.KURS_RUPIAH.equals(selectRefundDetList.get(i).getLkuId()) ? "Rp." : "$";
	            		if(selectRefundDetList.get(i).getJumlah()!=null){jumlahStr = selectRefundDetList.get(i).getJumlah().toString();}
	            		else {jumlahStr = "0";}
	            		if(selectRefundDetList.get(i).getLjbId()!=null){ljbId = selectRefundDetList.get(i).getLjbId().toString();}
	            		else{ljbId = null;}
	            		if(selectRefundDetList.get(i).getTipe()!= null){tipe = selectRefundDetList.get(i).getTipe().toString();}
	            		else {tipe = "0";}
	            		if(selectRefundDetList.get(i).getUnit()!=null){unit = selectRefundDetList.get(i).getUnit().toString();}
	            		else{unit = "0";}
	            		map = new HashMap<String, String>();
	            		map.put( "descr", selectRefundDetList.get(i).getDeskripsi() );
	            		map.put( "itemNo", selectRefundDetList.get(i).getItemNo().toString() );
	            		map.put( "jumlah", ": " + stdCurrency( lkuId, new BigDecimal(jumlahStr) ) );
	            		map.put( "jumlahStr", jumlahStr  );
	            		map.put( "ljbId", ljbId );            		 
	            		map.put( "ljiId", selectRefundDetList.get(i).getLjiId() );
	            		map.put( "tipe", tipe );
	            		map.put( "unit", unit );
	            		map.put( "lkuId", selectRefundDetList.get(i).getLkuId() );
	           			if("PREMI".equals(selectRefundDetList.get(i).getTipe().toString()))
	        			{
	           				map.put( "setoranOrNot", "yes" );
	        			}
	            		allDataFromDbDetRefund.add(map);
	        		}
	        	}
	            if(penarikanULink!=null && penarikanULink.size() >0)
	            {
	            	map = new HashMap<String, String>();
	            	map = genPenarikanULink(penarikanULink, spajCur);
	            	
	//               	if(posisi!= null &&posisi >= 2)
	//            	{
		            	allDataFromDbDetRefund.add(map);
		            	allDataFromDbDetRefund.addAll(genSpace());
	//            	}
	            }
	            if(biayaAdmin!= null && !BigDecimal.ZERO.equals(biayaAdmin))
	            {
	            	map = new HashMap<String, String>();
	            	map.put("descr","Biaya administrasi pembatalan polis");
	            	map.put("biayaStandarOrNot", "yes");
	            	map.put("jumlah",  ": " + "("  + stdCurrency( spajCur, biayaAdmin ) + ")");
	            	allDataFromDbDetRefund.add(map);
	            	allDataFromDbDetRefund.addAll(genSpace());
	            }
	            if(biayaMedis!=null && !BigDecimal.ZERO.equals(biayaMedis))
	            {
	            	map = new HashMap<String, String>();
	                map.put( "descr", "Biaya medis" );
	                map.put("biayaStandarOrNot", "yes");
	                map.put( "jumlah", ": " + "("  + stdCurrency( spajCur, biayaMedis ) + ")");
	                allDataFromDbDetRefund.add(map);
	                allDataFromDbDetRefund.addAll(genSpace());
	            }
	            if(biayaLain!=null && !BigDecimal.ZERO.equals(biayaLain))
	            {
	                map = new HashMap<String, String>();
	                map.put( "descr", biayaLainDescr );
	                map.put("biayaStandarOrNot", "yes");
	                map.put( "jumlah", ": " + "("  + stdCurrency( spajCur, biayaLain ) + ")");
	                allDataFromDbDetRefund.add(map);
	                allDataFromDbDetRefund.addAll(genSpace());
	            }
	            if(totalPremiDikembalikan!=null && !BigDecimal.ZERO.equals(totalPremiDikembalikan))
	            {
	               	map = new HashMap<String, String>();
	                map.put( "descr", "Premi dikembalikan" );
	                map.put("biayaStandarOrNot", "yes");
	                map.put( "jumlah", ": " + stdCurrency( spajCur, totalPremiDikembalikan ) );
	                allDataFromDbDetRefund.add(map);
	            }
	        	result.setAllDescrAndJumlah(allDataFromDbDetRefund);
	        }
	//        else if( checkSpaj > 0 && tempDescrDanJumlah.size() == 0)// jika spaj sdh di simpan di DB(mst_det_refund)
	//        {
	//        	result.setAllDescrAndJumlah(allDescrAndJumlah);
	//        }
	        else if(checkSpaj == 0) // jika spaj blm di simpan di DB(mst_det_refund)
	        {
		        for( SetoranPremiDbVO vo : setoranPremiDbVOList )
		        {
		            String kurs = null;
		            if( vo.getKurs() != null && !"".equals( vo.getKurs() ) )
		            {
		            	kurs = vo.getKurs().toString();
		            }	
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
		                adjustedJumlah = rowJumlah.divide( new BigDecimal( "10000" ), MathContext.DECIMAL64  );
		            }
		            else
		            {
		                adjustedJumlah = rowJumlah;
		            }
		            //end todo
		            String titipanKe = null;
		            titipan = vo.getTitipanKe() != null? " (titipan premi)" : "";
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

		            if(vo.getTitipanKe() == null)
		            {
		            	titipanKe = null;
		            }
		            else
		            {
		            	titipanKe = vo.getTitipanKe().toString();
		            }
		            tglStr = DateUtil.toIndonesian( vo.getTglSetor() );
		            SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
		            String tgl = df.format(vo.getTglSetor());
		            map = new HashMap<String, String>();
		            if("Link".equals(editForm.getULinkOrNot()) && "sudah".equals(editForm.getStatusUnit()) && editForm.getPosisiNo() ==null || "Link".equals(editForm.getULinkOrNot()) && "sudah".equals(editForm.getStatusUnit()) && editForm.getPosisiNo() < 2) // jika link dan status unit nya sudah
		            {
		            	
		            }
		            else 
		            {
		              map.put( "descr", "Premi disetor tgl. " + tglStr + titipan + originalCur + "( NoPre:" + noPre + "/NoVouc:" + noVoucher+ " )" );
		              map.put( "jumlah", ": " + stdCurrency( spajCur, adjustedJumlah ) );
		              map.put( "jumlahStr",  adjustedJumlah.toString()  );
		              map.put( "lkuId", rowLkuId );
		              map.put( "titipanKe", titipanKe );
		              map.put( "tglSetor", tglStr );
		              map.put( "tanggalFormat", tgl );
		              map.put( "setoranOrNot", "yes" );
		              map.put( "noPre", noPre );
		              map.put( "noVoucher", noVoucher );
		              map.put( "kurs", kurs );
		            }
		            
		            map.put( "tanggal", tglStr );
		            setoranMapList.add( map );
		
		            totalSetor = totalSetor.add( rowJumlah );
		        }
		
		//        jika unit link
		        if( penarikanUlinkVOList != null && penarikanUlinkVOList.size() > 0 )
		        {
		            BigDecimal totalPenarikanUlink = BigDecimal.ZERO;
		            for( PenarikanUlinkVO vo : penarikanUlinkVOList )
		            {
		                rowDescr = "Penarikan " + vo.getJumlahUnit() + " unit " + vo.getLjiInvest();
		                BigDecimal voJumlah = vo.getJumlah();
		                String jumlahPenarikan = "";
		                if(voJumlah == null)
		                {
		                	jumlahPenarikan = "0";
		                }
		                else if(voJumlah!=null)
		                {
		                	jumlahPenarikan = 	voJumlah.toString();
		                }
		                map = new HashMap<String, String>();
		                map.put( "descr", rowDescr );
		                map.put( "lkuId", spajLkuId );
		                map.put( "biayaStandarOrNot","yes" );
		                map.put( "jumlah", ": " + stdCurrency( "Rp.", vo.getJumlah() ) );
		                map.put( "jumlahStr",  jumlahPenarikan  );
		                penarikanUlinkMapList.add( map );
		
		                BigDecimal addition = vo.getJumlah() == null? BigDecimal.ZERO : vo.getJumlah();
		                totalPenarikanUlink = totalPenarikanUlink.add( addition );
		            }
		
		            ArrayList<BiayaUlinkDbVO> biayaUlinkDbVOList = Common.serializableList(elionsManager.selectBiayaUlink( spajNo ));
		
		            BigDecimal totalBiayaUlink = BigDecimal.ZERO;
		            for( BiayaUlinkDbVO vo : biayaUlinkDbVOList )
		            {
		                rowJumlah = vo.getAmount();
		                rowDescr = vo.getDescr();
		                String jumlahBiayaULink = "";
		                if(vo.getAmount() == null)
		                {
		                	jumlahBiayaULink = "0";
		                }
		                else if(vo.getAmount()!=null)
		                {
		                	jumlahBiayaULink = 	vo.getAmount().toString();
		                }
		                map = new HashMap<String, String>();
		                map.put( "descr", rowDescr );
		                map.put( "lkuId", spajLkuId );
		                map.put( "ljbId", vo.getLjbId().toString() );
		                map.put( "jumlah", ": " + stdCurrency( "Rp.", rowJumlah ) );
		                map.put( "jumlahStr",  jumlahBiayaULink  );
		                biayaUlinkMapList.add( map );
		
		                totalBiayaUlink = totalBiayaUlink.add( rowJumlah );
		            }
		
		            premiDikembalikan = totalPenarikanUlink.add( totalBiayaUlink );
		        }
		        else
		        {
		            premiDikembalikan = totalSetor;
		        }
		
		        if( biayaAdmin != null && !BigDecimal.ZERO.equals( biayaAdmin ) )
		        {
		            map = new HashMap<String, String>();
		            String jumlahBiayaAdmin = "";
		            if(biayaAdmin == null)
		            {
		            	jumlahBiayaAdmin = "0";
		            }
		            else if(biayaAdmin!=null)
		            {
		            	jumlahBiayaAdmin = 	biayaAdmin.toString();
		            }
		            map.put( "descr", "Biaya administrasi pembatalan polis" );
		            map.put( "lkuId", spajLkuId );
		            map.put("biayaStandarOrNot", "yes");
		            map.put( "jumlah", ": " + "("  + stdCurrency( spajCur, biayaAdmin ) + ")");
		            map.put( "jumlahStr",  jumlahBiayaAdmin  );
		            biayaStandarMapList.add( map );
		
		            premiDikembalikan = premiDikembalikan.subtract( biayaAdmin );
		        }
		
		        if( biayaMedis != null && !BigDecimal.ZERO.equals( biayaMedis ) )
		        {
		            String jumlahBiayaMedis = "";
		            if(biayaMedis == null)
		            {
		            	jumlahBiayaMedis = "0";
		            }
		            else if(biayaMedis!=null)
		            {
		            	jumlahBiayaMedis = 	biayaMedis.toString();
		            }
		            map = new HashMap<String, String>();
		            map.put( "descr", "Biaya medis" );
		            map.put( "lkuId", spajLkuId );
		            map.put( "biayaStandarOrNot", "yes" );
		            map.put( "jumlah", ": " + "("  + stdCurrency( spajCur, biayaMedis ) + ")");
		            map.put( "jumlahStr",  jumlahBiayaMedis  );
		            biayaStandarMapList.add( map );
		
		            premiDikembalikan = premiDikembalikan.subtract( biayaMedis );
		        }
		
		        if( biayaLain != null && !BigDecimal.ZERO.equals( biayaLain ) )
		        {
		            String jumlahBiayaLainStr = "";
		            if(biayaLain == null)
		            {
		            	jumlahBiayaLainStr = "0";
		            }
		            else if(biayaLain!=null)
		            {
		            	jumlahBiayaLainStr = 	biayaLain.toString();
		            }
		            map = new HashMap<String, String>();
		            map.put( "descr", biayaLainDescr );
		            map.put( "lkuId", spajLkuId );
		            map.put( "biayaStandarOrNot", "yes" );
		            map.put( "jumlah", ": " + "("  + stdCurrency( spajCur, biayaLain ) + ")");
		            map.put( "jumlahStr",  jumlahBiayaLainStr  );
		            biayaStandarMapList.add( map );
		
		            premiDikembalikan = premiDikembalikan.subtract( biayaLain );
		        }
		        String jumlahPremiDikembalikan = "";
		        if(premiDikembalikan == null)
		        {
		        	jumlahPremiDikembalikan = "0";
		        }
		        else if(premiDikembalikan!=null)
		        {
		        	jumlahPremiDikembalikan = 	premiDikembalikan.toString();
		        }
		        map = new HashMap<String, String>();
		        map.put( "descr", "Premi dikembalikan" );
		        map.put( "biayaStandarOrNot", "yes" );
		        map.put( "lkuId", spajLkuId );
		        map.put( "jumlah", ": " + stdCurrency( spajCur, premiDikembalikan ) );
		        map.put( "jumlahStr",  jumlahPremiDikembalikan  );
		        summaryMapList.add( map );
		
		        result.setSetoranMapList( setoranMapList );
		        result.setPenarikanUlinkMapList( penarikanUlinkMapList );
		        result.setBiayaUlinkMapList( biayaUlinkMapList );
		        result.setBiayaStandarList( biayaStandarMapList );
		        result.setSummaryMapList( summaryMapList );
		        result.setTotalPremiDikembalikan( premiDikembalikan );
		        result.setAllDescrAndJumlah(allDescrAndJumlah);
	        }
        }
        else 
        {
              for( SetoranPremiDbVO vo : setoranPremiDbVOList )
              {
  	            String kurs = null;
  	            if( vo.getKurs() != null && !"".equals( vo.getKurs() ) )
  	            {
  	            	kurs = vo.getKurs().toString();
  	            }
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
                      adjustedJumlah = rowJumlah.divide( new BigDecimal( "10000" ), MathContext.DECIMAL64  );
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
                  if( noPre == null || "".equals(noPre) )
                  {
                  	noPre = "-";
                  }
                  if( noVoucher == null || "".equals(noVoucher) )
                  {
                  	noVoucher = "-";
                  }
                  map = new HashMap<String, String>();
                  map.put( "descr", "Premi disetor tgl. " + tglStr + titipan + originalCur + "( NoPre:" + noPre + "/NoVouc:" + noVoucher+ " )" );
                  map.put( "jumlah", ": " + stdCurrency( spajCur, adjustedJumlah ) );
                  map.put( "tanggal", tglStr );
                  map.put( "kurs", kurs );
                  setoranMapList.add( map );

                  totalSetor = totalSetor.add( rowJumlah );
              }

//              jika unit link
              if( penarikanUlinkVOList != null && penarikanUlinkVOList.size() > 0 )
              {
                  BigDecimal totalPenarikanUlink = BigDecimal.ZERO;
                  for( PenarikanUlinkVO vo : penarikanUlinkVOList )
                  {
                      rowDescr = "Penarikan " + vo.getJumlahUnit() + " unit " + vo.getLjiInvest();

                      map = new HashMap<String, String>();
                      map.put( "descr", rowDescr );
                      map.put( "jumlah", ": " + stdCurrency( "Rp.", vo.getJumlah() ) );
                      penarikanUlinkMapList.add( map );

                      BigDecimal addition = vo.getJumlah() == null? BigDecimal.ZERO : vo.getJumlah();
                      totalPenarikanUlink = totalPenarikanUlink.add( addition );
                  }

                  ArrayList<BiayaUlinkDbVO> biayaUlinkDbVOList = Common.serializableList(elionsManager.selectBiayaUlink( spajNo ));

                  BigDecimal totalBiayaUlink = BigDecimal.ZERO;
                  for( BiayaUlinkDbVO vo : biayaUlinkDbVOList )
                  {
                      rowJumlah = vo.getAmount();
                      rowDescr = vo.getDescr();

                      map = new HashMap<String, String>();
                      map.put( "descr", rowDescr );
                      map.put( "jumlah", ": " + stdCurrency( "Rp.", rowJumlah ) );
                      biayaUlinkMapList.add( map );

                      totalBiayaUlink = totalBiayaUlink.add( rowJumlah );
                  }

                  premiDikembalikan = totalPenarikanUlink.add( totalBiayaUlink );
              }
              else
              {
                  premiDikembalikan = totalSetor;
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

        }
        return result;
    }

    private HashMap genPenarikanULink( ArrayList<PenarikanUlinkVO> penarikanUlinkVOList, String spajCur )
    {
    	RincianPolisVO result = new RincianPolisVO();
    	  String rowDescr;
    	  ArrayList<HashMap<String, String>> penarikanUlinkMapList = new ArrayList<HashMap<String, String>>();
    	  HashMap<String, String> map;
    	  map = new HashMap<String, String>();
        if( penarikanUlinkVOList != null && penarikanUlinkVOList.size() > 0 )
        {
            for( PenarikanUlinkVO vo : penarikanUlinkVOList )
            {
                rowDescr = "Penarikan " + vo.getJumlahUnit() + " unit " + vo.getLjiInvest();
                map.put( "descr", rowDescr );
                BigDecimal voJumlah = vo.getJumlah();
                String voJumlahStr = "";
                if(voJumlah == null)
                {
                	voJumlahStr = "0";
                }
                else if(voJumlah!=null)
                {
                	voJumlahStr = voJumlah.toString();
                }
                map.put( "jumlah",": " + stdCurrency( spajCur, new BigDecimal(voJumlahStr) ) );
                map.put( "biayaStandarOrNot","yes" );
                penarikanUlinkMapList.add( map );
            }
        }
    	return map;
    }
    
    //TODO
    
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
    
    private RincianPolisVO genRincianPolisPreviewEdit( String spajNo, String spajLkuId, BigDecimal biayaAdmin, BigDecimal biayaMedis, BigDecimal biayaLain, String biayaLainDescr, ArrayList<PenarikanUlinkVO> penarikanUlinkVOList, Integer checkSpaj )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Business.genRincianPolis" );

        RincianPolisVO result = new RincianPolisVO();

        ArrayList<HashMap<String, String>> setoranMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> penarikanUlinkMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> biayaUlinkMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> biayaStandarMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> summaryMapList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> AllDataFromDbDetRefund = new ArrayList<HashMap<String, String>>();
        

        HashMap<String, String> map;
        BigDecimal premiDikembalikan;

        String titipan;
        String tglStr;
        String rowDescr;
        String rowLkuId;
        BigDecimal rowKurs;
        BigDecimal rowJumlah;
        BigDecimal adjustedJumlah;
        String biayaStandarOrNot = null;
        String standartOrNot;
        if(checkSpaj > 0)// jika spaj sdh di simpan di DB(mst_det_refund)
        {
        	HashMap<String, Object> params = new HashMap<String, Object>();
        	params.put("regSpaj", spajNo);
        	ArrayList<RefundDetDbVO> selectRefundDetList = Common.serializableList(elionsManager.selectRefundDetList(params));
        	for(int i = 0 ; i < selectRefundDetList.size() ; i ++)
        	{
        		if(!"WITHDRAW".equals(selectRefundDetList.get(i).getTipe().toString()) && !"Biaya Administrasi Pembatalan Polis".equals(selectRefundDetList.get(i).getDeskripsi()))
        		{
        			String jumlahStr = "";
            		String ljbId = "";
            		String tipe = "";
            		String unit = "";
            		if(selectRefundDetList.get(i).getJumlah()!=null){jumlahStr = selectRefundDetList.get(i).getJumlah().toString();}
            		else {jumlahStr = "0";}
            		if(selectRefundDetList.get(i).getLjbId()!=null){ljbId = selectRefundDetList.get(i).getLjbId().toString();}
            		else{ljbId = null;}
            		if(selectRefundDetList.get(i).getTipe()!= null){tipe = selectRefundDetList.get(i).getTipe().toString();}
            		else {tipe = "0";}
            		if(selectRefundDetList.get(i).getUnit()!=null){unit = selectRefundDetList.get(i).getUnit().toString();}
            		else{unit = "0";}
            		map = new HashMap<String, String>();
            		map.put( "descr", selectRefundDetList.get(i).getDeskripsi() );
            		map.put( "itemNo", selectRefundDetList.get(i).getItemNo().toString() );
            		map.put( "jumlah", jumlahStr );
            		map.put( "ljbId", ljbId );
            		map.put( "ljiId", selectRefundDetList.get(i).getLjiId() );
            		map.put( "tipe", tipe );
            		map.put( "unit", unit );
            		map.put( "lkuId", selectRefundDetList.get(i).getLkuId() );
         			if("PREMI".equals(selectRefundDetList.get(i).getTipe().toString()))
        			{
        				map.put( "setoranOrNot", "yes" );
        			}
            		AllDataFromDbDetRefund.add(map);
        		}
        	}
        	result.setAllDescrAndJumlah(AllDataFromDbDetRefund);
        }
        else if(checkSpaj == 0) // jika spaj blm di simpan di DB(mst_det_refund)
        {
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
                    adjustedJumlah = rowJumlah.divide( new BigDecimal( "10000" ), MathContext.DECIMAL64 );
                }
                else
                {
                    adjustedJumlah = rowJumlah;
                }
                //end todo
                String titipanKe = null;
                if( vo.getTitipanKe() == null )
                {
                	titipanKe = null;
                }
                else
                {
                	titipanKe = vo.getTitipanKe().toString();
                }
                titipan = vo.getTitipanKe() != null? " (titipan premi)" : "";
                tglStr = DateUtil.toIndonesian( vo.getTglSetor() );
                SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
                String tgl = df.format(vo.getTglSetor());
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
                map = new HashMap<String, String>();
                map.put( "descr", "Premi disetor tgl. " + tglStr + titipan + originalCur + "( NoPre:" + noPre + "/NoVouc:" + noVoucher+ " )" );
                map.put( "jumlah",  adjustedJumlah.toString()  );
                map.put( "tanggal", tgl );
                map.put( "lkuId", rowLkuId );
                map.put( "setoranOrNot", "yes" );
                map.put( "titipanKe", titipanKe );
                map.put( "tglSetor", tglStr );
	            map.put( "noPre", noPre );
	            map.put( "noVoucher", noVoucher );
	            map.put( "kurs", kurs );
                setoranMapList.add( map );

                totalSetor = totalSetor.add( rowJumlah );
            }
            
//            jika unit link
            if( penarikanUlinkVOList != null && penarikanUlinkVOList.size() > 0 )
            {
                BigDecimal totalPenarikanUlink = BigDecimal.ZERO;
                for( PenarikanUlinkVO vo : penarikanUlinkVOList )
                {
                	if(!BigDecimal.ZERO.equals(vo.getJumlah()) && vo.getJumlah() !=null)
                	{
                		 rowDescr = "Penarikan " + vo.getJumlahUnit() + " unit " + vo.getLjiInvest();

                         map = new HashMap<String, String>();
                         map.put( "descr", rowDescr );
                         BigDecimal voJumlah = vo.getJumlah();
                         String voJumlahStr = "";
                         if(voJumlah == null)
                         {
                         	voJumlahStr = "0";
                         }
                         else if(voJumlah!=null)
                         {
                         	voJumlahStr = 	voJumlah.toString();
                         }
                         map.put( "lkuId", spajLkuId );
                         map.put( "jumlah",voJumlahStr );
                         map.put( "biayaStandarOrNot","yes" );
                         
                         penarikanUlinkMapList.add( map );

                         BigDecimal addition = vo.getJumlah() == null? BigDecimal.ZERO : vo.getJumlah();
                         totalPenarikanUlink = totalPenarikanUlink.add( addition );
                	}
                }

                ArrayList<BiayaUlinkDbVO> biayaUlinkDbVOList = Common.serializableList(elionsManager.selectBiayaUlink( spajNo ));

                BigDecimal totalBiayaUlink = BigDecimal.ZERO;
                for( BiayaUlinkDbVO vo : biayaUlinkDbVOList )
                {
                    rowJumlah = vo.getAmount();
                    rowDescr = vo.getDescr();
                    String rowJumlahStr = "";
                    if(rowJumlah == null)
                    {
                    	rowJumlahStr = "0";
                    }
                    else
                    {
                    	rowJumlahStr = rowJumlah.toString();
                    }
                    map = new HashMap<String, String>();
                    map.put( "lkuId", spajLkuId );
                    map.put( "ljbId", vo.getLjbId().toString() );
                    map.put( "descr", rowDescr );
                    map.put( "jumlah", rowJumlahStr  );
                    biayaUlinkMapList.add( map );

                    totalBiayaUlink = totalBiayaUlink.add( rowJumlah );
                }

                premiDikembalikan = totalPenarikanUlink.add( totalBiayaUlink );
            }
            else
            {
                premiDikembalikan = totalSetor;
            }

            if( biayaAdmin != null && !BigDecimal.ZERO.equals( biayaAdmin ) )
            {
            	
            	String biayaAdminStr = "";
            	if(biayaAdmin == null)
            	{
            		biayaAdminStr = "";
            	}
            	else
            	{
            		biayaAdminStr = biayaAdmin.toString();
            	}
                map = new HashMap<String, String>();
                map.put( "lkuId", spajLkuId );
                map.put("biayaStandarOrNot", "yes");
                map.put( "descr", "Biaya administrasi pembatalan polis" );
                map.put( "jumlah",  biayaAdminStr );
                biayaStandarMapList.add( map );

                premiDikembalikan = premiDikembalikan.subtract( biayaAdmin );
            }

            if( biayaMedis != null && !BigDecimal.ZERO.equals( biayaMedis ) )
            {
            	String biayaMedisStr = "";
            	if(biayaMedis==null)
            	{
            		biayaMedisStr = "0";
            	}
            	else
            	{
            		biayaMedisStr = biayaMedis.toString();
            	}
                map = new HashMap<String, String>();
                map.put("biayaStandarOrNot", "yes");
                map.put( "lkuId", spajLkuId );
                map.put( "descr", "Biaya medis" );
                map.put( "jumlah", biayaMedisStr  );
                biayaStandarMapList.add( map );

                premiDikembalikan = premiDikembalikan.subtract( biayaMedis );
            }

            if( biayaLain != null && !BigDecimal.ZERO.equals( biayaLain ) )
            {
            	String biayaLainStr = "";
            	if(biayaLain ==null)
            	{
            		biayaLainStr = "0";
            	}
            	else
            	{
            		biayaLainStr = biayaLain.toString();
            	}
                map = new HashMap<String, String>();
                map.put( "lkuId", spajLkuId );
                map.put("biayaStandarOrNot", "yes");
                map.put( "descr", biayaLainDescr );
                map.put( "jumlah", biayaLainStr );
                biayaStandarMapList.add( map );

                premiDikembalikan = premiDikembalikan.subtract( biayaLain );
            }
            String premiDikembalikanStr = "";
            if(premiDikembalikan==null)
            {
            	premiDikembalikanStr = "0";
            }
            else
            {
            	premiDikembalikanStr = premiDikembalikan.toString();
            }
            map = new HashMap<String, String>();
            map.put( "lkuId", spajLkuId );
            map.put("biayaStandarOrNot", "yes");
            map.put( "descr", "Premi dikembalikan" );
            map.put( "jumlah", premiDikembalikanStr );
            summaryMapList.add( map );

            result.setSetoranMapList( setoranMapList );
            result.setPenarikanUlinkMapList( penarikanUlinkMapList );
            result.setBiayaUlinkMapList( biayaUlinkMapList );
            result.setBiayaStandarList( biayaStandarMapList );
            result.setSummaryMapList( summaryMapList );
            result.setTotalPremiDikembalikan( premiDikembalikan );
        }
        

        return result;
    }
    
    private ArrayList<HashMap<String, String>> genLampiranRefundList( ArrayList<HashMap<String, String>> setoranList, String polisNo, String pp, String tt )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Business.genLampiranRefundList" );

        ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        map = new HashMap<String, String>();
        map.put( "dash", "-" );
        map.put( "content", "Surat Permohonan Pembatalan" );
        result.add( map );

        map = new HashMap<String, String>();
        map.put( "dash", "-" );
        map.put( "content", "Polis Asli dengan nomor polis " + polisNo + " atas nama Pemegang Polis/Tertanggung:" );
        result.add( map );

        map = new HashMap<String, String>();
        map.put( "dash", "" );
        map.put( "content", pp + " / " + tt );
        result.add( map );

        // Menambahkan lampiran setoran

        if( setoranList != null && setoranList.size() > 1)
        {
            String tanggal;
            String jumlah;
            for( int i = 0; i < setoranList.size() - 1; i++ )
            {
                HashMap<String, String> setoranMap =  setoranList.get( i );
                tanggal = setoranMap.get( "tanggal" );
                jumlah = setoranMap.get( "jumlah" );
                map = new HashMap<String, String>();
                map.put( "dash", "-" );
                map.put( "content", "bukti setoran tanggal " + tanggal + " dengan nominal sebesar " + jumlah );
                result.add( map );
            }
        }

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
    
    public ArrayList<PenarikanUlinkVO> selectPenarikanUlinkVOListForRetrieveCountPremi( String regSpaj )
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
    
    public RefundEditForm retrieveCountPremi( RefundEditForm editForm, RefundLookupForm lookupForm, String theEvent )
    {
    	BigDecimal debet = new BigDecimal("0");
    	BigDecimal kredit = new BigDecimal("0");    	
    	BigDecimal premiDikembalikan = new BigDecimal("0");
    	String spaj = null;
    	
        if( "onPressImageSuratBatal".equals( theEvent ) || "onPressImageEdit".equals( theEvent ) )
	    {
	    	spaj = lookupForm.getSelectedRowCd();
	    	editForm.setPenarikanUlinkVOList( selectPenarikanUlinkVOListForRetrieveCountPremi( spaj ) );
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
    
    public Lamp1VO retrievePreviewEdit( Lamp1ParamsVO paramsVO, RefundEditForm editForm )
    {
    	 Lamp1VO lamp1VO = new Lamp1VO();
    	 String spajNo = paramsVO.getSpajNo();
         PolicyInfoVO policyInfoVO = elionsManager.selectPolicyInfoBySpaj( spajNo );

         //TODO
         String polisNo ;

         if( policyInfoVO.getPolicyNo() == null || policyInfoVO.getPolicyNo().trim().equals( "" ) )
         {
             if( policyInfoVO.getNamaPp() != null && !policyInfoVO.getNamaPp().trim().equals( "" ) )
             {
//                 polisNo = " atas nama " + policyInfoVO.getNamaPp();
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

         lamp1VO.setNoUrutMemo( "-" );
         lamp1VO.setHal( hal );
         lamp1VO.setTanggal( DateUtil.toIndonesian( elionsManager.selectDate() ) );

         lamp1VO.setSpajNo( StringUtil.nomorSPAJ( spajNo ) );
         lamp1VO.setPolisNo( polisNo );
         lamp1VO.setProduk( StringUtil.camelHumpAndTrim( policyInfoVO.getNamaProduk() ) );
         lamp1VO.setPemegangPolis( StringUtil.camelHumpAndTrim( policyInfoVO.getNamaPp() ) );
         lamp1VO.setTertanggung( StringUtil.camelHumpAndTrim( policyInfoVO.getNamaTt() ) );

         lamp1VO.setStatement( genStatement( spajNo, polisNo, paramsVO.getAlasanForLabel() ) );
         Integer checkSpaj = elionsManager.selectCheckSpaj( spajNo );       
         RincianPolisVO rincianPolisVO = genRincianPolisPreviewEdit(
                 spajNo,
                 policyInfoVO.getLkuId(),
                 paramsVO.getBiayaAdmin(),
                 paramsVO.getBiayaMedis(),
                 paramsVO.getBiayaLain(),
                 paramsVO.getBiayaLainDescr(),
                 Common.serializableList(paramsVO.getPenarikanUlinkList()),
                 checkSpaj
         );
         if( checkSpaj == 0 )
         {
             ArrayList<HashMap<String, String>> rincianList = cloneList( rincianPolisVO.getSetoranMapList() );
//             if(editForm.getPosisiNo()!= null && editForm.getPosisiNo() >= 2)
//             {
            	rincianList.addAll( cloneList( rincianPolisVO.getPenarikanUlinkMapList() ) );
//             }
             rincianList.addAll( cloneList( rincianPolisVO.getBiayaUlinkMapList() ) );
//             rincianList.addAll( cloneList( rincianPolisVO.getBiayaStandarList() ) );
//             rincianList.addAll( cloneList( rincianPolisVO.getSummaryMapList() ) );
             lamp1VO.setRincianPolisList( rincianList );
             ArrayList <PreviewEditParamsVO> temp = new ArrayList<PreviewEditParamsVO>();
             
             if(rincianList.size() > 0)
             {               
            	 for( int i = 0 ; i < rincianList.size() ; i ++ )
            	 {
         			 String kurs = rincianList.get(i).get("kurs");
        			 BigDecimal kursBigDecimal = BigDecimal.ZERO;
        			 if( kurs != null && !"".equals( kurs ) )
        			 {
        				 kursBigDecimal = new BigDecimal( kurs );
        			 }
            		 String biayaStandarOrNot = rincianList.get(i).get("biayaStandarOrNot");
            		 String lkuId = rincianList.get(i).get("lkuId");
            		 String ljbId = rincianList.get(i).get("ljbId");
            		 if( rincianList.get(i).get("tanggal") != null ) // setoran
            		 {
            			 String titipanKeStr = rincianList.get(i).get("titipanKe");
            			 String noPre = rincianList.get(i).get("noPre");
            			 String noVoucher = rincianList.get(i).get("noVoucher");
            			 Integer titipanKe = null;
            			 if( titipanKeStr == null )
            			 {
            				 titipanKe = null;
            			 }
            			 else if( titipanKeStr != null )
            			 {
            				 titipanKe = LazyConverter.toInt(rincianList.get(i).get("titipanKe"));
            			 }
                		 String descr = rincianList.get(i).get("descr");
                		 BigDecimal jumlahSetoran  = new BigDecimal(rincianList.get(i).get("jumlah"));
                		 BigDecimal jumlahDebet  = new BigDecimal("0");
                		 BigDecimal jumlahKredit  = new BigDecimal("0");
                		 String tglStr = rincianList.get(i).get("tanggal");
                		 Date tanggal = FormatDate.toDate(tglStr);
                		 
                		 String setoranOrNot = "yes";
                		 temp.add(new PreviewEditParamsVO(descr,jumlahSetoran,jumlahDebet,jumlahKredit, setoranOrNot,lkuId, titipanKe, tanggal, ljbId, CommonConst.DISABLED_TRUE, biayaStandarOrNot, noPre, noVoucher, kursBigDecimal ));   
            		 }
            		 else // bkn setoran
            		 {
                		 String descr = rincianList.get(i).get("descr");
                		 BigDecimal jumlahSetoran  = new BigDecimal("0");
                		 BigDecimal jumlahDebet  = new BigDecimal(rincianList.get(i).get("jumlah"));
                		 BigDecimal jumlahKredit  = new BigDecimal("0");
                		 String setoranOrNot = "no";
                		 temp.add(new PreviewEditParamsVO(descr,jumlahSetoran,jumlahDebet,jumlahKredit, setoranOrNot,lkuId, null, null, ljbId, CommonConst.DISABLED_TRUE, biayaStandarOrNot, null, null, kursBigDecimal ) );   
            		 }
            	 }
            	 if(rincianPolisVO.getSummaryMapList()!=null)
            	 {
            		 lamp1VO.setPremiDikembalikan(new BigDecimal(rincianPolisVO.getSummaryMapList().get(0).get("jumlah")));
            	 }
            	 lamp1VO.setTempDescrDanJumlah(temp);
             }
             lamp1VO.setJumlahTerbilang( "(" + AngkaTerbilang.indonesian(
                     rincianPolisVO.getTotalPremiDikembalikan().toString(), policyInfoVO.getLkuId() ).toLowerCase() + ")" );
         }
         //TODO
         else if( checkSpaj > 0 )
         {
        	 ArrayList<HashMap<String, String>> rincianList = cloneList( rincianPolisVO.getAllDescrAndJumlah() );
        	 ArrayList <PreviewEditParamsVO> temp = new ArrayList<PreviewEditParamsVO>();
             if(rincianList.size() > 0)
             {
            	 for( int i = 0 ; i < rincianList.size() ; i ++ )
            	 {
         			 String kurs = rincianList.get(i).get("kurs");
        			 BigDecimal kursBigDecimal = BigDecimal.ZERO;
        			 if( kurs != null && !"".equals( kurs ) )
        			 {
        				 kursBigDecimal = new BigDecimal( kurs );
        			 }
            		String biayaStandarOrNot = rincianList.get(i).get("biayaStandarOrNot");
            		String lkuId = rincianList.get(i).get("lkuId");
            		String ljbId = rincianList.get(i).get("ljbId");
            		if("PREMI".equals(rincianList.get(i).get("tipe")))
            		{
            			 String descr = rincianList.get(i).get("descr");
                   		 BigDecimal jumlahSetoran  = new BigDecimal(rincianList.get(i).get("jumlah"));
                		 BigDecimal jumlahDebet  = new BigDecimal("0");
                		 BigDecimal jumlahKredit  = new BigDecimal("0");
                		 String setoranOrNot = "yes";
                		 temp.add(new PreviewEditParamsVO(descr,jumlahSetoran,jumlahDebet,jumlahKredit, setoranOrNot,lkuId, null, null, ljbId, CommonConst.DISABLED_TRUE, biayaStandarOrNot, null, null, kursBigDecimal ) );   
            		}
            		else 
            		{
	               		 String descr = rincianList.get(i).get("descr");
	            		 BigDecimal jumlahSetoran  = new BigDecimal("0");
	            		 BigDecimal jumlahDebet  = new BigDecimal(rincianList.get(i).get("jumlah"));
	            		 BigDecimal jumlahKredit  = new BigDecimal("0");
	            		 String setoranOrNot = "no";
	            		 temp.add(new PreviewEditParamsVO(descr,jumlahSetoran,jumlahDebet,jumlahKredit, setoranOrNot,lkuId, null, null, ljbId, CommonConst.DISABLED_TRUE, biayaStandarOrNot, null, null, kursBigDecimal));
            		}
            	 }
            	 lamp1VO.setTempDescrDanJumlah(temp);
             }
         }
         lamp1VO.setBiayaAdministrasi(paramsVO.getBiayaAdmin());

         lamp1VO.setTotalPremiDikembalikan( rincianPolisVO.getTotalPremiDikembalikan() );
    	 return lamp1VO;
    }
    
    public PolicyInfoVO getInformationForLampiran( String spaj )
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Business.getInformationForLampiran" );
    	PolicyInfoVO policyInfoVO =  elionsManager.selectPolicyInfoBySpaj( spaj );
    	return policyInfoVO;
    }
    
    public PolicyInfoVO getNewInformationForLampiran( String spajBaru )
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Business.getInformationForLampiran" );
    	PolicyInfoVO policyInfoVO =  elionsManager.selectPolicyInfoBySpaj( spajBaru );
    	return policyInfoVO;
    }
    
    public Lamp1VO retrieveLamp1( Lamp1ParamsVO paramsVO, RefundEditForm editForm )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp1Business.retrieveLamp1" );

        Lamp1VO lamp1VO = new Lamp1VO();

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

        lamp1VO.setNoUrutMemo( "-" );
        lamp1VO.setHal( hal );
        lamp1VO.setTanggal( DateUtil.toIndonesian( elionsManager.selectDate() ) );
        
        lamp1VO.setSpajNo( StringUtil.nomorSPAJ( spajNo ) );
        lamp1VO.setPolisNo( polisNo );
        lamp1VO.setProduk( StringUtil.camelHumpAndTrim( policyInfoVO.getNamaProduk() ) );
        lamp1VO.setPemegangPolis( StringUtil.camelHumpAndTrim( policyInfoVO.getNamaPp() ) );
        lamp1VO.setTertanggung( StringUtil.camelHumpAndTrim( policyInfoVO.getNamaTt() ) );
        lamp1VO.setPrevLspdId( policyInfoVO.getLspdId() );
        lamp1VO.setEndDate( policyInfoVO.getEndDate() );
        lamp1VO.setBegDate( policyInfoVO.getBegDate() );

        lamp1VO.setStatement( genStatement( spajNo, polisNo, paramsVO.getAlasanForLabel() ) );
        Integer checkSpaj = elionsManager.selectCheckSpaj( spajNo );
        
        RincianPolisVO rincianPolisVO = genRincianPolis(
                spajNo,
                policyInfoVO.getLkuId(),
                paramsVO.getBiayaAdmin(),
                paramsVO.getBiayaMedis(),
                paramsVO.getBiayaLain(),
                paramsVO.getBiayaLainDescr(),
                paramsVO.getPenarikanUlinkList(),
                editForm.getTempDescrDanJumlah(),         
                editForm.getPremiDikembalikan(),
                editForm.getULinkOrNot(),
                editForm.getPenarikanUlinkVOList(),
                editForm.getStatusUnit(),
                editForm.getPosisiNo(),
                editForm,
                checkSpaj
        );
        
        ArrayList<HashMap<String, String>> rincianList = new ArrayList<HashMap<String,String>>();
        ArrayList<HashMap<String, String>> tempAllDescrAndJumlah = new ArrayList<HashMap<String,String>>();
        if( editForm.getSpaj() != null && isUnitLink( editForm.getSpaj() ) && editForm.getHasUnitFlag() != null && editForm.getHasUnitFlag() == 1 )
        {
	        if(checkSpaj == 0)
	        {
		        if("Link".equals(editForm.getULinkOrNot()) && "sudah".equals(editForm.getStatusUnit()) && editForm.getPosisiNo() ==null || "Link".equals(editForm.getULinkOrNot()) && "sudah".equals(editForm.getStatusUnit()) && editForm.getPosisiNo() < 2)
		        {       
		            rincianList = cloneList( rincianPolisVO.getSetoranMapList() );
		            rincianList.addAll( cloneList( rincianPolisVO.getAllDescrAndJumlah() ) );  
		            lamp1VO.setJumlahTerbilang( "(" + AngkaTerbilang.indonesian(
		                  editForm.getPremiDikembalikan().toString(), policyInfoVO.getLkuId() ).toLowerCase() + ")" );        
		        }
		        else
		        {
		          ArrayList <PreviewEditParamsVO> temp = new ArrayList<PreviewEditParamsVO>();
		          rincianList = cloneList( rincianPolisVO.getSetoranMapList() );
		          rincianList.addAll( genSpace() );
		          rincianList.addAll( cloneList( rincianPolisVO.getPenarikanUlinkMapList() ) );
		          rincianList.addAll( genSpace() );
		          rincianList.addAll( cloneList( rincianPolisVO.getBiayaUlinkMapList() ) );
		          rincianList.addAll( genSpace() );
		          rincianList.addAll( cloneList( rincianPolisVO.getBiayaStandarList() ) );
		          rincianList.addAll( genSpace() );
		          rincianList.addAll( cloneList( rincianPolisVO.getSummaryMapList() ) );
		          
		          tempAllDescrAndJumlah = cloneList( rincianPolisVO.getSetoranMapList() );
		          tempAllDescrAndJumlah.addAll( cloneList( rincianPolisVO.getPenarikanUlinkMapList() ) );
		          tempAllDescrAndJumlah.addAll( cloneList( rincianPolisVO.getBiayaUlinkMapList() ) );
		          tempAllDescrAndJumlah.addAll( cloneList( rincianPolisVO.getSummaryMapList() ) );
		          lamp1VO.setJumlahTerbilang( "(" + AngkaTerbilang.indonesian(
		        		  rincianPolisVO.getTotalPremiDikembalikan().toString(), policyInfoVO.getLkuId() ).toLowerCase() + ")" );    
		          if(tempAllDescrAndJumlah.size() > 0)
		          {
		         	 for( int i = 0 ; i < tempAllDescrAndJumlah.size() ; i ++ )
		         	 {
		           		 String lkuId = tempAllDescrAndJumlah.get(i).get("lkuId");
		        		 String ljbId = tempAllDescrAndJumlah.get(i).get("ljbId");
		     			 String kurs = tempAllDescrAndJumlah.get(i).get("kurs");
		    			 BigDecimal kursBigDecimal = BigDecimal.ZERO;
		    			 if( kurs != null && !"".equals( kurs ) )
		    			 {
		    				 kursBigDecimal = new BigDecimal( kurs );
		    			 }
		        		 String biayaStandarOrNot = tempAllDescrAndJumlah.get(i).get("biayaStandarOrNot");
		        		 if( tempAllDescrAndJumlah.get(i).get("tanggal") != null ) // setoran
		        		 {
		        			 String titipanKeStr = rincianList.get(i).get("titipanKe");
		        			 String noPre = rincianList.get(i).get("noPre");
		        			 String noVoucher = rincianList.get(i).get("noVoucher");
		        			 Integer titipanKe = null;
		        			 if( titipanKeStr == null )
		        			 {
		        				 titipanKe = null;
		        			 }
		        			 else if( titipanKeStr != null )
		        			 {
		        				 titipanKe = LazyConverter.toInt(rincianList.get(i).get("titipanKe"));
		        			 }
		            		 String descr = tempAllDescrAndJumlah.get(i).get("descr");
		            		 BigDecimal jumlahSetoran  = new BigDecimal(tempAllDescrAndJumlah.get(i).get("jumlahStr"));
		            		 BigDecimal jumlahDebet  = new BigDecimal("0");
		            		 BigDecimal jumlahKredit  = new BigDecimal("0");
		            		 String tglStr = tempAllDescrAndJumlah.get(i).get("tanggalFormat");
		            		 Date tanggal = FormatDate.toDate(tglStr);
		            		 String setoranOrNot = "yes";
		            		 temp.add(new PreviewEditParamsVO( descr,jumlahSetoran,jumlahDebet,jumlahKredit, setoranOrNot,lkuId, titipanKe, tanggal, ljbId, CommonConst.DISABLED_TRUE,  biayaStandarOrNot, noPre, noVoucher, kursBigDecimal ) );   
		        		 }
		        		 else // bkn setoran
		        		 {
		            		 String descr = tempAllDescrAndJumlah.get(i).get("descr");
		            		 BigDecimal jumlahSetoran  = new BigDecimal("0");
		            		 BigDecimal jumlahDebet  = new BigDecimal(tempAllDescrAndJumlah.get(i).get("jumlahStr"));
		            		 BigDecimal jumlahKredit  = new BigDecimal("0");
		            		 String setoranOrNot = "no";
		            		 temp.add(new PreviewEditParamsVO( descr,jumlahSetoran,jumlahDebet,jumlahKredit, setoranOrNot,lkuId, null, null, ljbId, CommonConst.DISABLED_TRUE, biayaStandarOrNot, null, null, kursBigDecimal ) );   
		        		 }
		         	 }
		         	 lamp1VO.setTempDescrDanJumlah(temp);
		          }
		        }
	        }
	        else if( checkSpaj > 0  && editForm.getTempDescrDanJumlah() != null)
	        {
	       	 rincianList = cloneList( rincianPolisVO.getAllDescrAndJumlah() );
	       	 ArrayList <PreviewEditParamsVO> temp = new ArrayList<PreviewEditParamsVO>();
	            if(rincianList.size() > 0)
	            {
	           	 for( int i = 0 ; i < rincianList.size() ; i ++ )
	           	 {
	           		String biayaStandarOrNot = rincianList.get(i).get("biayaStandarOrNot");
	    			 String kurs = rincianList.get(i).get("kurs");
	    			 BigDecimal kursBigDecimal = BigDecimal.ZERO;
	    			 if( kurs != null && !"".equals( kurs ) )
	    			 {
	    				 kursBigDecimal = new BigDecimal( kurs );
	    			 }
	           		if(!rincianList.get(i).get("descr").equals("") )
	           		{
	           			if(!"yes".equals(rincianList.get(i).get("biayaStandarOrNot")))
	           			{
		           		String lkuId = rincianList.get(i).get("lkuId");
		           		String ljbId = rincianList.get(i).get("ljbId");
		           		String setoranOrNot = rincianList.get(i).get("setoranOrNot");
		           		String jumlahStr = null;
		           		if(rincianList.get(i).get("jumlahStr")!=null)
		           		{
		           			jumlahStr = rincianList.get(i).get("jumlahStr");
		           		}
		           		else if(rincianList.get(i).get("jumlahStr")==null)
		           		{
		           			jumlahStr = "0";
		           		}
		           		if("yes".equals(setoranOrNot))
		           		{
		           			 String descr = rincianList.get(i).get("descr");
		                     BigDecimal jumlahSetoran  = new BigDecimal(jumlahStr);
		               		 BigDecimal jumlahDebet  = new BigDecimal("0");
		               		 BigDecimal jumlahKredit  = new BigDecimal("0");
		               		 temp.add( new PreviewEditParamsVO( descr,jumlahSetoran,jumlahDebet,jumlahKredit, setoranOrNot,lkuId, null, null, ljbId, CommonConst.DISABLED_TRUE, biayaStandarOrNot, null, null, kursBigDecimal ) );   
		           		}
		           		else 
		           		{
			               	 String descr = rincianList.get(i).get("descr");
			            	 BigDecimal jumlahSetoran  = new BigDecimal("0");
			            	 BigDecimal jumlahDebet  = new BigDecimal(jumlahStr);
			            	 BigDecimal jumlahKredit  = new BigDecimal("0");
			            	 temp.add( new PreviewEditParamsVO( descr,jumlahSetoran,jumlahDebet,jumlahKredit, setoranOrNot,lkuId, null, null, ljbId, CommonConst.DISABLED_TRUE, biayaStandarOrNot, null, null,kursBigDecimal ) );
		           		}
	           			}
	           		}
	           	 }
	           	 lamp1VO.setTempDescrDanJumlah(temp);
	            }
	            String premiDikembalikan = "0";
	            if( editForm.getPremiDikembalikan() != null && !BigDecimal.ZERO.equals( editForm.getPremiDikembalikan()))
	            {
	            	premiDikembalikan = editForm.getPremiDikembalikan().toString();
	            }
	            lamp1VO.setJumlahTerbilang( "(" + AngkaTerbilang.indonesian(
	            		premiDikembalikan , 
		                  policyInfoVO.getLkuId() ) + ")" ); 
	        }
	        else if( checkSpaj > 0 )
	        {
	       	 rincianList = cloneList( rincianPolisVO.getAllDescrAndJumlah() );
	       	 ArrayList <PreviewEditParamsVO> temp = new ArrayList<PreviewEditParamsVO>();
	            if(rincianList.size() > 0)
	            {
	           	 for( int i = 0 ; i < rincianList.size() ; i ++ )
	           	 {
	           		String biayaStandarOrNot = rincianList.get(i).get("biayaStandarOrNot");
	    			 String kurs = rincianList.get(i).get("kurs");
	    			 BigDecimal kursBigDecimal = BigDecimal.ZERO;
	    			 if( kurs != null && !"".equals( kurs ) )
	    			 {
	    				 kursBigDecimal = new BigDecimal( kurs );
	    			 }
	           		if(rincianList.get(i).get("descr")!=null && !rincianList.get(i).get("descr").equals("") )
	           		{
	           			if(!"yes".equals(rincianList.get(i).get("biayaStandarOrNot")))
	           			{
		           		String lkuId = rincianList.get(i).get("lkuId");
		           		String ljbId = rincianList.get(i).get("ljbId");
		           		String jumlahStr = null;
		           		if(rincianList.get(i).get("jumlahStr")!=null)
		           		{
		           			jumlahStr = rincianList.get(i).get("jumlahStr");
		           		}
		           		else if(rincianList.get(i).get("jumlahStr")==null)
		           		{
		           			jumlahStr = "0";
		           		}
		           		if("PREMI".equals(rincianList.get(i).get("tipe")))
		           		{
		           			 String descr = rincianList.get(i).get("descr");
		                     BigDecimal jumlahSetoran  = new BigDecimal(jumlahStr);
		               		 BigDecimal jumlahDebet  = new BigDecimal("0");
		               		 BigDecimal jumlahKredit  = new BigDecimal("0");
		               		 String setoranOrNot = "yes";
		               		 temp.add( new PreviewEditParamsVO( descr,jumlahSetoran,jumlahDebet,jumlahKredit, setoranOrNot,lkuId, null, null, ljbId, CommonConst.DISABLED_TRUE, biayaStandarOrNot, null, null, kursBigDecimal ) );   
		           		}
		           		else 
		           		{
			               	 String descr = rincianList.get(i).get("descr");
			            	 BigDecimal jumlahSetoran  = new BigDecimal("0");
			            	 BigDecimal jumlahDebet  = new BigDecimal(jumlahStr);
			            	 BigDecimal jumlahKredit  = new BigDecimal("0");
			            	 String setoranOrNot = "no";
			            	 temp.add( new PreviewEditParamsVO( descr,jumlahSetoran,jumlahDebet,jumlahKredit, setoranOrNot,lkuId, null, null, ljbId, CommonConst.DISABLED_TRUE, biayaStandarOrNot, null, null, kursBigDecimal ) );
		           		}
	           			}
	           		}
	           	 }
	           	 lamp1VO.setTempDescrDanJumlah(temp);
	            }
	            String premiDikembalikan = "0";
	            if(editForm.getPremiDikembalikan()!=null)
	            {
	            	premiDikembalikan = editForm.getPremiDikembalikan().toString();
	            }
	            lamp1VO.setJumlahTerbilang( "(" + AngkaTerbilang.indonesian(
	            		premiDikembalikan, 
		                  policyInfoVO.getLkuId() ) + ")" ); 
	        }
        }
        else
        {
        	rincianList = cloneList( rincianPolisVO.getSetoranMapList() );
        	rincianList.addAll( genSpace() );
            rincianList.addAll( cloneList( rincianPolisVO.getPenarikanUlinkMapList() ) );
            rincianList.addAll( genSpace() );
            rincianList.addAll( cloneList( rincianPolisVO.getBiayaUlinkMapList() ) );
            rincianList.addAll( genSpace() );
            rincianList.addAll( cloneList( rincianPolisVO.getBiayaStandarList() ) );
            rincianList.addAll( genSpace() );
            rincianList.addAll( cloneList( rincianPolisVO.getSummaryMapList() ) );

            lamp1VO.setRincianPolisList( rincianList );

            lamp1VO.setJumlahTerbilang( "(" + AngkaTerbilang.indonesian(
                            rincianPolisVO.getTotalPremiDikembalikan().toString(), policyInfoVO.getLkuId() ).toLowerCase() + ")" );
        }
        
        lamp1VO.setRincianPolisList( rincianList );       
        
        lamp1VO.setAtasNama( StringUtil.camelHumpAndTrim( paramsVO.getAtasNama() ) );        
        lamp1VO.setRekeningNo( paramsVO.getNorek() );
        lamp1VO.setBankName( paramsVO.getNamaBank() );        
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
        lamp1VO.setCabang( cabangBank );
        lamp1VO.setKota( kotaBank );
        lamp1VO.setSigner( "V. Inge" );
        lamp1VO.setTotalPremiDikembalikan( rincianPolisVO.getTotalPremiDikembalikan() );

        return lamp1VO;
    }
    
    public String genCreatRekapFileName( String user, Date awal, Date akhir )
    {
        String result;

        DateFormat dateFormat = new SimpleDateFormat( "yyyyMMdd_HHmmss" );
		result =  "surat_rekap_pembatalan_" + dateFormat.format( new Date() ) + ".pdf";
        return result;
    }
    
    public String genLamp1FileName( String spajNo )
    {
        String result;

        DateFormat dateFormat = new SimpleDateFormat( "yyyyMMdd_HHmmss" );
		result =  "surat_refund_" + spajNo + "_" + dateFormat.format( new Date() ) + ".pdf";
        return result;
    }
}
