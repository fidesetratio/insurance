package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundLookupList
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Sep 24, 2008 10:58:43 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import id.co.sinarmaslife.std.model.vo.DropDown;
import com.ekalife.elions.web.refund.vo.AlasanVO;

// angka id yg ada dibawah tidak boleh diubah2
// hanya boleh ditambahkan saja

public class RefundLookupList implements Serializable
{
	private static final long serialVersionUID = -4032014392627742262L;
	private static ArrayList<DropDown> alasanBatalList;
    private static ArrayList<DropDown> tindakanList;
    private static ArrayList<DropDown> lkuList;

    // singleton
    public static ArrayList<DropDown> getAlasanList( ArrayList<AlasanVO> alasanVOList )
    {
        if( alasanBatalList == null )
        {
            alasanBatalList = new ArrayList<DropDown>();
            alasanBatalList.add( new DropDown( "", "" ) );

            for( AlasanVO alasanVO : alasanVOList )
            {
                alasanBatalList.add( new DropDown( alasanVO.getAlasanCd().toString(), alasanVO.getAlasanLabel() ) );
            }
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_HASIL_UW_SUBSTANDARD.toString(), "Hasil UW:substandar" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_HASIL_UW_DITOLAK.toString(), "Hasil UW:ditolak" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_MENOLAK_MEDIS.toString(), "Menolak Medis" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_GANTI_PLAN.toString(), "Ganti plan" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_DOUBLE_INPUT.toString(), "Double input" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_KELEBIHAN_SETORAN_PREMI_PRETAMA.toString(), "Kelebihan setoran premi pertama" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_SALAH_INPUT.toString(), "Salah input" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_GANTI_TERTANGGUNG.toString(), "Ganti Tertanggung" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_TAMBAH_ATAU_GANTI_RIDER.toString(), "Tambah/ganti Rider" ) );
//            alasanBatalList.add( new DropDown( RefundConst.ALASAN_LAIN.toString(), "Lain-lain" ) );
        }
        return alasanBatalList;
    }

    // singleton
    public static ArrayList<DropDown> getTindakanList()
    {
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
}
