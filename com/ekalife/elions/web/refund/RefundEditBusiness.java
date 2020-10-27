package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundEditBusiness
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Nov 14, 2008 1:57:06 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.util.DateUtil;
import id.co.sinarmaslife.std.util.StringUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.elions.web.refund.vo.AlasanVO;
import com.ekalife.elions.web.refund.vo.PenarikanUlinkVO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.PreviewEditParamsVO;
import com.ekalife.elions.web.refund.vo.RefundDbVO;
import com.ekalife.elions.web.refund.vo.RefundDetDbVO;
import com.ekalife.elions.web.refund.vo.RefundEditParamsVO;
import com.ekalife.elions.web.refund.vo.RefundEditVO;
import com.ekalife.elions.web.refund.vo.RincianPolisVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.LazyConverter;

public class RefundEditBusiness
{
    protected final Log logger = LogFactory.getLog( getClass() );

    private ElionsManager elionsManager;

    public void setElionsManager( ElionsManager elionsManager )
    {
        this.elionsManager = elionsManager;
    }

    public RefundEditBusiness()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundEditBusiness constructor is called ..." );
    }

    public RefundEditBusiness( ElionsManager elionsManager )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundEditBusiness constructor is called ..." );
        setElionsManager( elionsManager );
    }

//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_HASIL_UW_SUBSTANDARD.toString(), "Hasil UW:substandar" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_HASIL_UW_DITOLAK.toString(), "Hasil UW:ditolak" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_MENOLAK_MEDIS.toString(), "Menolak Medis" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_GANTI_PLAN.toString(), "Ganti plan" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_DOUBLE_INPUT.toString(), "Double input" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_KELEBIHAN_SETORAN_PREMI_PERTAMA.toString(), "Kelebihan setoran premi pertama" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_SALAH_INPUT.toString(), "Salah input" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_GANTI_TERTANGGUNG.toString(), "Ganti Tertanggung" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_TAMBAH_ATAU_GANTI_RIDER.toString(), "Tambah/ganti Rider" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_LAIN.toString(), "Lain-lain" ) );

    public ArrayList<DropDown> getAlasanAllList()
    {
    	ArrayList<DropDown> alasanBatalList = null;
        if( alasanBatalList == null )
        {
            alasanBatalList = new ArrayList<DropDown>();
            alasanBatalList.add( new DropDown( "", "" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_HASIL_UW_SUBSTANDARD.toString(), "Hasil UW:substandar" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_HASIL_UW_DITOLAK.toString(), "Hasil UW:ditolak" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_HASIL_UW_POSTPONED.toString(), "Hasil UW:postponed" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_MENOLAK_MEDIS.toString(), "Menolak Medis" ) );
            alasanBatalList.add( new DropDown( RefundConst.ALASAN_ATAS_PERMINTAAN_NASABAH_KRN_TIDAK_SETUJU_DENGAN_ISI_POLIS.toString(), "Atas Permintaan Nasabah" ) );
            alasanBatalList.add( new DropDown( RefundConst.ALASAN_UNDERWRITING_DECISION.toString(), "Underwriting Decision" ) );
            alasanBatalList.add( new DropDown( RefundConst.ALASAN_DOUBLE_INPUT.toString(), "Double input" ) );
            alasanBatalList.add( new DropDown( RefundConst.ALASAN_SPAJ_KADARLUASA.toString(), "SPAJ Kadaluarsa > 3 Bulan" ) );
            alasanBatalList.add( new DropDown( RefundConst.ALASAN_NOT_PROCEED_WITH.toString(), "Not Proceed With" ) );
            alasanBatalList.add( new DropDown( RefundConst.ALASAN_WITHDRAWAL.toString(), "Withdrawal" ) );
            alasanBatalList.add( new DropDown( RefundConst.ALASAN_CANCEL_ON_FREE_LOOK_PERIOD.toString(), "Cancel On Free Look Period" ) );
            alasanBatalList.add( new DropDown( RefundConst.ALASAN_DOKUMEN_TIDAK_DILENGKAPI.toString(), "Pendingan Tidak Dilengkapi" ) );
            alasanBatalList.add( new DropDown( RefundConst.ALASAN_GAGAL_DEBET.toString(), "Gagal Debet > 45 Hari" ) );
            alasanBatalList.add( new DropDown( RefundConst.ALASAN_LAIN.toString(), "Lain-lain" ) );
        }
        return alasanBatalList;
    }

    public ArrayList<DropDown> getTindakanAllList()
    {
    	ArrayList<DropDown> tindakanList = null;
        if( tindakanList == null )
        {
            tindakanList = new ArrayList<DropDown>();
            tindakanList.add( new DropDown( "", "" ) );
            tindakanList.add( new DropDown( RefundConst.TINDAKAN_TIDAK_ADA.toString(), "Tidak ada" ) );
            tindakanList.add( new DropDown( RefundConst.TINDAKAN_REFUND_PREMI.toString(), "Refund Premi" ) );
            tindakanList.add( new DropDown( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.toString(), "Ganti Tertanggung" ) );
            tindakanList.add( new DropDown( RefundConst.TINDAKAN_GANTI_PLAN.toString(), "Ganti Plan" ) );
        }
        return tindakanList;
    }
    
    public ArrayList<AlasanVO> initializeAlasan()
    {
        ArrayList<AlasanVO> alasanVOList = new ArrayList<AlasanVO>();
        ArrayList < DropDown > listTindakan = new ArrayList<DropDown>();
        AlasanVO alasanVO;
/*
        alasanVO = new AlasanVO();
        listTindakan = new ArrayList<DropDown>();
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_REFUND_PREMI.toString(), "REFUND PREMI" ) );
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.toString(), "GANTI TERTANGGUNG" ) );
        alasanVO.setAlasanCd( RefundConst.ALASAN_HASIL_UW_SUBSTANDARD );
        alasanVO.setAlasanLabel( "Hasil UW:substandar" );
        alasanVO.setTindakanList( listTindakan );
        alasanVOList.add( alasanVO );
        
        alasanVO = new AlasanVO();
        listTindakan = new ArrayList<DropDown>();
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_REFUND_PREMI.toString(), "REFUND PREMI" ) );
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.toString(), "GANTI TERTANGGUNG" ) );
        alasanVO.setAlasanCd( RefundConst.ALASAN_HASIL_UW_DITOLAK );
        alasanVO.setAlasanLabel( "Hasil UW:ditolak" );
        alasanVO.setTindakanList( listTindakan );
        alasanVOList.add( alasanVO );

        alasanVO = new AlasanVO();
        listTindakan = new ArrayList<DropDown>();
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_REFUND_PREMI.toString(), "REFUND PREMI" ) );
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.toString(), "GANTI TERTANGGUNG" ) );
        alasanVO.setAlasanCd( RefundConst.ALASAN_HASIL_UW_POSTPONED );
        alasanVO.setAlasanLabel( "Hasil UW:postponed" );
        alasanVO.setTindakanList( listTindakan );
        alasanVOList.add( alasanVO );
*/        
        alasanVO = new AlasanVO();
        listTindakan = new ArrayList<DropDown>();
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_REFUND_PREMI.toString(), "REFUND PREMI" ) );
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.toString(), "GANTI TERTANGGUNG" ) );
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_GANTI_PLAN.toString(), "GANTI PLAN" ) );
        alasanVO.setAlasanCd( RefundConst.ALASAN_UNDERWRITING_DECISION );
        alasanVO.setAlasanLabel( "Underwriting Decision" );
        alasanVO.setTindakanList( listTindakan );
        alasanVOList.add( alasanVO );
/*        
        alasanVO = new AlasanVO();
        listTindakan = new ArrayList<DropDown>();
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_REFUND_PREMI.toString(), "REFUND PREMI" ) );
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.toString(), "GANTI TERTANGGUNG" ) );
        alasanVO.setAlasanCd( RefundConst.ALASAN_MENOLAK_MEDIS );
        alasanVO.setAlasanLabel( "Menolak Medis" );
        alasanVO.setTindakanList( listTindakan );
        alasanVOList.add( alasanVO );
*/        
        alasanVO = new AlasanVO();
        listTindakan = new ArrayList<DropDown>();
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_REFUND_PREMI.toString(), "REFUND PREMI" ) );
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_GANTI_PLAN.toString(), "GANTI PLAN" ) );
        alasanVO.setAlasanCd( RefundConst.ALASAN_ATAS_PERMINTAAN_NASABAH_KRN_TIDAK_SETUJU_DENGAN_ISI_POLIS );
        alasanVO.setAlasanLabel( "Atas Permintaan Nasabah" );
        alasanVO.setTindakanList( listTindakan );
        alasanVOList.add( alasanVO );
        
        alasanVO = new AlasanVO();
        listTindakan = new ArrayList<DropDown>();
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_TIDAK_ADA.toString(), "TIDAK ADA" ) );
        alasanVO.setAlasanCd( RefundConst.ALASAN_DOUBLE_INPUT );
        alasanVO.setAlasanLabel( "Double Input" );
        alasanVO.setTindakanList( listTindakan );
        alasanVOList.add( alasanVO );
     
        alasanVO = new AlasanVO();
        listTindakan = new ArrayList<DropDown>();
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_REFUND_PREMI.toString(), "REFUND PREMI" ) );
        alasanVO.setAlasanCd( RefundConst.ALASAN_SPAJ_KADARLUASA );
        alasanVO.setAlasanLabel( "SPAJ Kadaluarsa > 3 Bulan" );
        alasanVO.setTindakanList( listTindakan );
        alasanVOList.add( alasanVO );
        
        alasanVO = new AlasanVO();
        listTindakan = new ArrayList<DropDown>();
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_REFUND_PREMI.toString(), "REFUND PREMI" ) );
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.toString(), "GANTI TERTANGGUNG" ) );
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_GANTI_PLAN.toString(), "GANTI PLAN" ) );
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_TIDAK_ADA.toString(), "TIDAK ADA" ) );
        alasanVO.setAlasanCd( RefundConst.ALASAN_NOT_PROCEED_WITH );
        alasanVO.setAlasanLabel( "Not Proceed With" );
        alasanVO.setTindakanList( listTindakan );
        alasanVOList.add( alasanVO );
        
        alasanVO = new AlasanVO();
        listTindakan = new ArrayList<DropDown>();
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_REFUND_PREMI.toString(), "REFUND PREMI" ) );
        alasanVO.setAlasanCd( RefundConst.ALASAN_WITHDRAWAL );
        alasanVO.setAlasanLabel( "Withdrawal" );
        alasanVO.setTindakanList( listTindakan );
        alasanVOList.add( alasanVO );
        
        alasanVO = new AlasanVO();
        listTindakan = new ArrayList<DropDown>();
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_REFUND_PREMI.toString(), "REFUND PREMI" ) );
        alasanVO.setAlasanCd( RefundConst.ALASAN_CANCEL_ON_FREE_LOOK_PERIOD );
        alasanVO.setAlasanLabel( "Cancel On Free Look Period" );
        alasanVO.setTindakanList( listTindakan );
        alasanVOList.add( alasanVO );
        
        alasanVO = new AlasanVO();
        listTindakan = new ArrayList<DropDown>();
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_REFUND_PREMI.toString(), "REFUND PREMI" ) );
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_TIDAK_ADA.toString(), "TIDAK ADA" ) );
        alasanVO.setAlasanCd( RefundConst.ALASAN_DOKUMEN_TIDAK_DILENGKAPI );
        alasanVO.setAlasanLabel( "Pendingan Tidak Dilengkapi" );
        alasanVO.setTindakanList( listTindakan );
        alasanVOList.add( alasanVO );
        
        alasanVO = new AlasanVO();
        listTindakan = new ArrayList<DropDown>();
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_TIDAK_ADA.toString(), "TIDAK ADA" ) );
        alasanVO.setAlasanCd( RefundConst.ALASAN_GAGAL_DEBET );
        alasanVO.setAlasanLabel( "Gagal Debet > 45 Hari" );
        alasanVO.setTindakanList( listTindakan );
        alasanVOList.add( alasanVO );
        
        alasanVO = new AlasanVO();
        listTindakan = new ArrayList<DropDown>();
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_REFUND_PREMI.toString(), "REFUND PREMI" ) );
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.toString(), "GANTI TERTANGGUNG" ) );
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_GANTI_PLAN.toString(), "GANTI PLAN" ) );
        listTindakan.add( new DropDown ( RefundConst.TINDAKAN_TIDAK_ADA.toString(), "TIDAK ADA" ) );
        alasanVO.setAlasanCd( RefundConst.ALASAN_LAIN );
        alasanVO.setAlasanLabel( "Lain - lain" );
        alasanVO.setTindakanList( listTindakan );
        alasanVOList.add( alasanVO );
        
        return alasanVOList;
    }

    public ArrayList<RefundDetDbVO> selectRefundDetList( HashMap<String, Object> params )
    {
        return Common.serializableList(elionsManager.selectRefundDetList( params ));
    }

    public ArrayList<PenarikanUlinkVO> selectPenarikanUlinkVOListBySpaj( String regSpaj )
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

    public HashMap<String, Object> retrieveBiaya2( String regSpaj )
    {
        HashMap<String, Object> result = new HashMap<String, Object>();
        HashMap<String, Object> params;
        ArrayList<RefundDetDbVO> detailList;
        RefundDetDbVO refundDetDbVO;

        // mendapatkan biaya merchant
        params = new HashMap<String, Object>();
        params.put( "regSpaj", regSpaj );
        params.put( "tipeNo", RefundDetDbVO.Tipe.MERCHANTFEE.tipe() );
        params.put( "ljbId", null );
        detailList = Common.serializableList(elionsManager.selectRefundDetList( params ));
        if( detailList != null && detailList.size() > 0 )
        {
            refundDetDbVO = detailList.get( 0 );
            result.put( "biayaMerchant", refundDetDbVO.getJumlah() );
        }
        
        // mendapatkan biaya admin
        params = new HashMap<String, Object>();
        params.put( "regSpaj", regSpaj );
        params.put( "tipeNo", RefundDetDbVO.Tipe.BIAYA.tipe() );
        params.put( "ljbId", RefundConst.LJB_BIAYA_ADMINISTRASI_PEMBATALAN_POLIS );
        detailList = Common.serializableList(elionsManager.selectRefundDetList( params ));
        if( detailList != null && detailList.size() > 0 )
        {
            refundDetDbVO = detailList.get( 0 );
            result.put( "biayaAdmin", refundDetDbVO.getJumlah() );
        }

        // mendapatkan biaya medis
        params = new HashMap<String, Object>();
        params.put( "regSpaj", regSpaj );
        params.put( "tipeNo", RefundDetDbVO.Tipe.BIAYA.tipe() );
        params.put( "ljbId", RefundConst.LJB_BIAYA_MEDIS );
        detailList = Common.serializableList(elionsManager.selectRefundDetList( params ));
        if( detailList != null && detailList.size() > 0 )
        {
            refundDetDbVO = detailList.get( 0 );
            result.put( "biayaMedis", refundDetDbVO.getJumlah() );
        }

        // mendapatkan biaya lain2
        params = new HashMap<String, Object>();
        params.put( "regSpaj", regSpaj );
        params.put( "tipeNo", RefundDetDbVO.Tipe.BIAYA.tipe() );
        params.put( "ljbId", RefundConst.LJB_BIAYA_LAIN );
        detailList = Common.serializableList(elionsManager.selectRefundDetList( params ));
        if( detailList != null && detailList.size() > 0 )
        {
            refundDetDbVO = detailList.get( 0 );
            result.put( "biayaLain", refundDetDbVO.getJumlah() );
            result.put( "biayaLainDescr", refundDetDbVO.getDeskripsi() );
        }

        return result;
    }
    
    private String genStatementTindakanTidakAda( String spajNo, String polisNo, String alasan )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp3Business.genStatement" );

        return "Pembayaran premi untuk No. SPAJ:" + StringUtil.nomorSPAJ( spajNo ) +
                "/ No.Polis:" + polisNo +
                " diatas dengan rincian sebagai berikut mohon dibatalkan karena: " + alasan;
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
            // jika rupiah, abaikan nilai kurs di data base (karena ada yg null dan nol)
            SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
            String tgl = df.format(vo.getTglSetor());
            String jumlahStr = null;
            String kursString = null;
            if( vo.getKurs() != null && BigDecimal.ZERO.equals( vo.getKurs() ) )
            {
            	kursString = vo.getKurs().toString();
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
            map.put( "kurs", kursString );
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
    
    
    public RefundEditVO retrieveRefundEdit( RefundEditParamsVO paramsVO, RefundEditForm editForm)
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundLamp3Business.retrieveLamp3" );

        RefundEditVO refundEditVO = new RefundEditVO();

        String spajNo = paramsVO.getSpajNo();

        PolicyInfoVO oldPolicyInfoVO = elionsManager.selectPolicyInfoBySpaj( spajNo );

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
            polisNo = " / " + StringUtil.nomorPolis( oldPolicyInfoVO.getPolicyNo() );
        }

        String spaj_polis = FormatString.nomorSPAJ( spajNo ) + polisNo;

        String hal = "Pembatalan Polis : SPAJ " + spaj_polis;

        refundEditVO.setNoUrutMemo( "to be generated" );
        refundEditVO.setHal( hal );
        refundEditVO.setTanggal( DateUtil.toIndonesian( elionsManager.selectDate() ) );

        refundEditVO.setSpajNo( StringUtil.nomorSPAJ( spajNo ) );
        refundEditVO.setPolicyNo( polisNo );
        refundEditVO.setProductName( StringUtil.camelHumpAndTrim( oldPolicyInfoVO.getNamaProduk() ) );
        refundEditVO.setPolicyHolderName( StringUtil.camelHumpAndTrim( oldPolicyInfoVO.getNamaPp() ) );
        refundEditVO.setInsuredName( StringUtil.camelHumpAndTrim( oldPolicyInfoVO.getNamaTt() ) );
        refundEditVO.setPrevLspdId( oldPolicyInfoVO.getLspdId() );
        refundEditVO.setBegDate( oldPolicyInfoVO.getBegDate() );
        refundEditVO.setEndDate( oldPolicyInfoVO.getEndDate() );
        
        refundEditVO.setStatement( genStatementTindakanTidakAda( spajNo, polisNo, paramsVO.getAlasanForLabel() ) );
        
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
			 String jumlahStr = null;
			 Integer titipanKe = null;
			 Date tanggal = null;
			 String kurs = allDescrAndJumlah.get(i).get("kurs");
			 BigDecimal kursBigDecimal = BigDecimal.ZERO;
			 if( kurs != null && !"".equals( kurs ) )
			 {
				 kursBigDecimal = new BigDecimal(kurs);
			 }
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
        refundEditVO.setTempDescrDanJumlah(temp);
        refundEditVO.setRincianPolisList( rincianList );
//        refundEditVO.setSigner( "V. Inge" );
        refundEditVO.setSigner( "Suryanto Lim" );

        return refundEditVO;
    }
    
    
    public RefundDbVO selectRefundByCd( String regSpaj )
    {
        return elionsManager.selectRefundByCd( regSpaj );
    }

    public List<RefundDbVO> selectMstRefund( String regSpaj )
    {
        return elionsManager.selectMstRefund( regSpaj );
    }
    
    public Integer selectCheckSpaj (String spajNo)
    {
    	return elionsManager.selectCheckSpaj(spajNo);
    }

    public ArrayList<DropDown> getMerchantAllList()
    {
    	ArrayList<DropDown> merchantList = null;
        if( merchantList == null )
        {
        	merchantList = new ArrayList<DropDown>();
        	merchantList.add( new DropDown( "0", "None CC" ) );
        	merchantList.add( new DropDown( RefundConst.MERCHANT_BNI.toString(), "CC BNI/SINARMAS (1,8 %)" ) );
        	merchantList.add( new DropDown( RefundConst.MERCHANT_BCA.toString(), "CC BCA (0,9 %)" ) );
        	merchantList.add( new DropDown( RefundConst.MERCHANT_VISAMASTER.toString(), "CC Visa Master (2,5 %)" ) );
        }
        return merchantList;
    }
}
