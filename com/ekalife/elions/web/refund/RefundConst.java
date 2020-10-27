package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundConst
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Sep 24, 2008 11:21:51 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/


public class RefundConst
{
    public static Integer REFUND_LOOKUP_JSP = 0;
    public static Integer REFUND_EDIT_JSP = 1;
    public static Integer STD_DOWNLOAD_JSP = 2;
    public static Integer PREVIEW_LAMP_1_JSP = 3;
    public static Integer PREVIEW_LAMP_3_JSP = 4;
    public static Integer PREVIEW_REDEMPT_JSP = 5;
    public static Integer SIGN_IN_JSP = 6;
    public static Integer PREVIEW_EDIT_JSP = 7;
    public static Integer REFUND_REKAP_LOOKUP_JSP = 8;
    public static Integer PREVIEW_EDIT_SETORAN = 9;
    public static Integer REFUND_LOOKUP_AKSEPTASI_JSP = 10;
    
	// semua alasan di bwh ini tdr boleh diganti atau dihapus krn sudah tercatat di database
    public static Integer ALASAN_HASIL_UW_SUBSTANDARD = 1;
    public static Integer ALASAN_HASIL_UW_DITOLAK = 2;
    public static Integer ALASAN_MENOLAK_MEDIS = 3;
    public static Integer ALASAN_GANTI_PLAN = 4;
    public static Integer ALASAN_DOUBLE_INPUT = 5;
    public static Integer ALASAN_KELEBIHAN_SETORAN_PREMI_PERTAMA = 6;
    public static Integer ALASAN_SALAH_INPUT = 7;
    public static Integer ALASAN_GANTI_TERTANGGUNG = 8;
    public static Integer ALASAN_TAMBAH_ATAU_GANTI_RIDER = 9;
    public static Integer ALASAN_ATAS_PERMINTAAN_NASABAH_KRN_TIDAK_SETUJU_DENGAN_ISI_POLIS = 10; 
    public static Integer ALASAN_SPAJ_KADARLUASA = 11;
    public static Integer ALASAN_TIDAK_BAYAR_PREMI = 12;
    public static Integer ALASAN_DOKUMEN_TIDAK_DILENGKAPI = 13;
    public static Integer ALASAN_HASIL_UW_POSTPONED = 14;
    public static Integer ALASAN_UNDERWRITING_DECISION = 15;
    public static Integer ALASAN_NOT_PROCEED_WITH = 16;
    public static Integer ALASAN_WITHDRAWAL = 17;
    public static Integer ALASAN_CANCEL_ON_FREE_LOOK_PERIOD = 18;
    public static Integer ALASAN_GAGAL_DEBET = 19;
    
    //lain2
    public static Integer ALASAN_LAIN = 99;


    public static Integer TINDAKAN_TIDAK_ADA = 1;
    public static Integer TINDAKAN_REFUND_PREMI = 2;
    public static Integer TINDAKAN_GANTI_TERTANGGUNG = 3;
    public static Integer TINDAKAN_GANTI_PLAN = 4;

    public static Integer POSISI_DRAFT = 1;
    
    public static Integer POSISI_TIDAK_ADA_CANCEL = 2;
    
    public static Integer POSISI_REFUND_ULINK_REDEMPT_SENT = 2;
    public static Integer POSISI_REFUND_ULINK_CANCEL = 3;
    public static Integer POSISI_REFUND_ULINK_PAID = 4;
    public static Integer POSISI_REFUND_ULINK_ACCEPTATION = 5;

    public static Integer POSISI_REFUND_NON_ULINK_CANCEL = 2;
    public static Integer POSISI_REFUND_NON_ULINK_PAID = 3;
    public static Integer POSISI_REFUND_NON_ULINK_ACCEPTATION = 4;

    public static Integer POSISI_GANTI_TERTANGGUNG_ULINK_REDEMPT_SEND = 2;
    public static Integer POSISI_GANTI_TERTANGGUNG_ULINK_CANCEL = 3;
    public static Integer POSISI_GANTI_TERTANGGUNG_ULINK_ACCEPTATION = 4;
    
    public static Integer POSISI_GANTI_TERTANGGUNG_NON_ULINK_CANCEL = 2;
    public static Integer POSISI_GANTI_TERTANGGUNG_NON_ULINK_ACCEPTATION = 3;

    public static Integer POSISI_GANTI_PLAN_ULINK_REDEMPT_SEND = 2;
    public static Integer POSISI_GANTI_PLAN_ULINK_CANCEL = 3;
    public static Integer POSISI_GANTI_PLAN_ULINK_ACCEPTATION = 4;
    
    public static Integer POSISI_GANTI_PLAN_NON_ULINK_CANCEL = 2;
    public static Integer POSISI_GANTI_PLAN_NON_ULINK_ACCEPTATION = 3;

    public static Integer LJB_BIAYA_ADMINISTRASI_PEMBATALAN_POLIS = 356;
    public static Integer LJB_BIAYA_MEDIS = 357;
    public static Integer LJB_BIAYA_LAIN = 358;
    
    public static Integer TIPE_PREMI = 1;
    public static Integer TIPE_BIAYA = 2;
    public static Integer TIPE_TOPUP = 3;
    public static Integer TIPE_WITHDRAW = 4;
    public static Integer TIPE_MERCHANTFEE = 5;

    public static Integer UNIT_LINK = 17;
    
    public static Integer UNIT_FLAG = 1;
    public static Integer NON_UNIT_FLAG = 0;
    
    public static String KURS_RUPIAH = "01";
    public static String KURS_DOLLAR = "02";
    
    public static Integer MERCHANT_BNI = 1;
    public static Integer MERCHANT_BCA = 2;
    public static Integer MERCHANT_VISAMASTER = 3;
    
	//MANTA - User Yang Melakukan Akseptasi Pada Proses Refund SPAJ
    //Dewi(1614), Titis(1161), dan Anta(3123)
    public static String USER_AKSEP_UW = "1614,1161,3123,1569";
    public static String USER_AKSEP_COLECTION = "27,62,3752,4867,4990,3123";

    public static final String PAGE_TITLE_SESSION = "pageTitle";
}
