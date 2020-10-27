package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundCommonBusiness
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Nov 24, 2008 12:00:56 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import id.co.sinarmaslife.std.spring.util.Email;
import id.co.sinarmaslife.std.util.DateUtil;
import id.co.sinarmaslife.std.util.StringUtil;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;

import com.ekalife.elions.model.User;
import com.ekalife.elions.model.refund.RefundEditForm;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.web.refund.vo.BiayaUlinkDbVO;
import com.ekalife.elions.web.refund.vo.CheckSpajParamsVO;
import com.ekalife.elions.web.refund.vo.MstRefundParamsVO;
import com.ekalife.elions.web.refund.vo.PenarikanUlinkVO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.RefundDbVO;
import com.ekalife.elions.web.refund.vo.RefundDetDbVO;
import com.ekalife.elions.web.refund.vo.RefundDocumentVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.jasper.JasperUtils;
import com.lowagie.text.pdf.PdfWriter;

public class RefundCommonBusiness
{
    protected final Log logger = LogFactory.getLog( getClass() );

    private ElionsManager elionsManager;
    private BacManager bacManager;

	private Email email;

    public void setElionsManager( ElionsManager elionsManager )
    {
        this.elionsManager = elionsManager;
    }

    public BacManager getBacManager() {
		return bacManager;
	}

	public void setBacManager(BacManager bacManager) {
		this.bacManager = bacManager;
	}
    
    public void setEmail( Email email )
    {
        this.email = email;
    }

    public RefundCommonBusiness()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundCommonBusiness constructor is called ..." );
    }

    public RefundCommonBusiness( ElionsManager elionsManager )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundCommonBusiness constructor is called ..." );
        setElionsManager( elionsManager );
    }

    public String getTimeStr( Date time )
    {
        DateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
		return dateFormat.format( time );
    }
    
    public boolean isUnitLink( String spajNo )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundEditBusiness.isUnitLink" );
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

    public void updatePosisiAndCancelRefund( String spajNo, Integer posisiCd, BigDecimal cancelWho, Date cancelWhen )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundCommonBusiness.updatePosisiAndCancelRefund" );
        elionsManager.updatePosisiAndCancelRefund( spajNo, posisiCd, cancelWho, cancelWhen );
    }

    public void updateTglKirimDokFisik( String spajNo, Date tglKirimDokFisik, String updateWho, Date updateWhen )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundCommonBusiness.updateTglKirimDokFisik" );
        elionsManager.updateTglKirimDokFisik( spajNo, tglKirimDokFisik, updateWho, updateWhen );
        elionsManager.insertMstPositionSpaj(updateWho, "BERKAS SPAJ BATAL TELAH DIKIRIM KE ACCOUNTING/FINANCE", spajNo, 0);
    }
    // return message if succeed or not
    
//  List<SetoranPremiDbVO> setoranPremiDbVOList = elionsManager.selectSetoranPremiBySpaj( paramsVO.getSpajNo() );
    public List<SetoranPremiDbVO> setoranPremiDbVOListForGantiTertanggungAndPlan( String spaj )
    {
    	if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundBusiness.setoranPremiDbVOList" );
    	List<SetoranPremiDbVO> result = elionsManager.selectSetoranPremiBySpaj( spaj );
    	return result;
    }
    
    public String retrieveNoSurat( String noSpaj, Integer tindakan )
    {
	    Date tanggal = elionsManager.selectNowDate();
	    SimpleDateFormat tahun = new SimpleDateFormat("yy");
	    SimpleDateFormat bulan = new SimpleDateFormat("MM");
	    String noSuratYear = tahun.format(tanggal);
	    String noSuratMonth = bulan.format(tanggal);
	    String noSuratDate = noSuratMonth + "/" + noSuratYear;
	    
	    CheckSpajParamsVO checkSpajInDb = elionsManager.selectCheckSpajInDb( noSpaj ); 
	    Integer generateNoSuratMax = elionsManager.selectNoSuratList( noSuratDate );
	    String noSurat = null;
	    String generateNoSurat = null; 
//	    String tindakanString = null;
	    if( checkSpajInDb == null 
	    		|| "".equals( checkSpajInDb ) 
	    		|| checkSpajInDb != null && !"".equals( checkSpajInDb ) && checkSpajInDb.getNoSpaj() == null 
	    		|| checkSpajInDb != null && !"".equals( checkSpajInDb ) && "".equals( checkSpajInDb.getNoSpaj() ) ) // jika data blm ada dlm DB
	    {
	        if( generateNoSuratMax == null )
	        { 
	        	generateNoSurat = "0"; 
	        }
	        else 
	        {
	        	generateNoSurat = ( generateNoSuratMax + 1 ) + "";
	        }
	        int getLengthGenerateNoSurat = generateNoSurat.length();
	        if( getLengthGenerateNoSurat == 1 ) { generateNoSurat = "00" + generateNoSurat; }
	        else if( getLengthGenerateNoSurat == 2 ) { generateNoSurat = "0" + generateNoSurat; }
	        else if( getLengthGenerateNoSurat == 3 ) { }
	        
//	        if( RefundConst.TINDAKAN_REFUND_PREMI.equals( tindakan ) ) { tindakanString = "RP"; }
//	        else if( RefundConst.TINDAKAN_GANTI_TERTANGGUNG.equals( tindakan ) ) { tindakanString = "GT"; }
//	        else if( RefundConst.TINDAKAN_GANTI_PLAN.equals( tindakan ) ) { tindakanString = "GP"; }
	        
	  	    SimpleDateFormat simpleDateFormatNoSurat = new SimpleDateFormat("MM/yy");    	   
	 	    noSurat = ( generateNoSurat + "/IUW-BTL/" + simpleDateFormatNoSurat.format( tanggal ) ).trim();
	    }
	    else if( checkSpajInDb != null && !"".equals( checkSpajInDb ) &&  checkSpajInDb.getNoSurat() != null && !"".equals( checkSpajInDb.getNoSurat() ) )
	    {
	    	noSurat = checkSpajInDb.getNoSurat();
	    }
	    else
	    {
	    	noSurat = "";
	    }
	    
	    return noSurat;
    }
    
    public HashMap<String, Object> deleteThenInsertRefund( MstRefundParamsVO paramsVO, String actionMessage, RefundEditForm editForm, String theEvent, Integer hasUnitFlag, Integer tindakanCd )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundBusiness.deleteThenInsertRefund" );

        HashMap<String, Object> result = new HashMap<String, Object>();
        String pageMessage;
        RefundDbVO refundDbVO = new RefundDbVO();
        refundDbVO.setAlasanCd( paramsVO.getAlasanCd() );
        refundDbVO.setAlasanLain( paramsVO.getAlasanLain() );
        refundDbVO.setCreateWhen( paramsVO.getCreateWhen() );
        refundDbVO.setCreateWho( paramsVO.getCreateWho() );
        refundDbVO.setKliCabangBank( paramsVO.getKliCabangBank() );
        refundDbVO.setKliKotaBank( paramsVO.getKliKotaBank() );
        refundDbVO.setKliNama( paramsVO.getKliNama() );
        refundDbVO.setKliNamaBank( paramsVO.getKliNamaBank() );
        refundDbVO.setKliNorek( paramsVO.getKliNorek() );
        refundDbVO.setNoVoucher( "novoc" );
        refundDbVO.setPosisiNo( paramsVO.getPosisi() );
        refundDbVO.setPremiRefund( paramsVO.getTotalPremiDikembalikan() );
        refundDbVO.setSpajBaruNo( paramsVO.getSpajBaruNo() );
        refundDbVO.setSpajNo( paramsVO.getSpajNo() );
        refundDbVO.setTindakanCd( paramsVO.getTindakanCd() );
        refundDbVO.setUlinkFlag( paramsVO.getUlinkFlag() );
        refundDbVO.setUpdateWhen( paramsVO.getUpdateWhen() );
        refundDbVO.setUpdateWho( paramsVO.getUpdateWho() );
        refundDbVO.setPayment( paramsVO.getPayment() );
        refundDbVO.setPaymentDate( paramsVO.getPaymentDate() );
        refundDbVO.setHasUnitFlag( paramsVO.getHasUnitFlag() );
        refundDbVO.setPrevLspdId( paramsVO.getPrevLspdId() );
        refundDbVO.setPrevLspdId( paramsVO.getPrevLspdId() );
        refundDbVO.setCancelWhen( paramsVO.getCancelWhen() );
        refundDbVO.setCancelWho( paramsVO.getCancelWho() );
        refundDbVO.setAddLampiranList( paramsVO.getAddLampiranList() );
        
        if( RefundConst.TINDAKAN_TIDAK_ADA.equals( paramsVO.getTindakanCd() ) )
        {
        	ArrayList<SetoranPremiDbVO> setoranPremiBySpaj = Common.serializableList(elionsManager.selectSetoranPremiBySpaj( paramsVO.getSpajNo() ));
        	if( setoranPremiBySpaj != null && setoranPremiBySpaj.size() > 0 )
        	{
                String noSurat = retrieveNoSurat( paramsVO.getSpajNo(), paramsVO.getTindakanCd() );
                refundDbVO.setNoSurat( noSurat );
        	}        	
        	else if( setoranPremiBySpaj == null || setoranPremiBySpaj.size() == 0 )
        	{
        		refundDbVO.setNoSurat( null );
        	}
        }
        else
        {
            String noSurat = retrieveNoSurat( paramsVO.getSpajNo(), paramsVO.getTindakanCd() );
            refundDbVO.setNoSurat( noSurat );
        }
        
        String tempLampiran = null;
//        if(editForm.getCheckSpajInDetRefund() > 0) // jika sdh di save di mst_det_refund
//        {
//        	
//        }
//        else if(editForm.getCheckSpajInDetRefund() == 0) // jika blm di save di mst_det_refund
//        {
//            for(int i = 0 ; i < editForm.getLampiranAddList().size() ; i ++)
//            {
//            	if(CommonConst.CHECKED_TRUE.equals(editForm.getLampiranAddList().get(i).getCheckBox()))
//            	{
//            		if(tempLampiran == null)
//            		{
//            			tempLampiran = editForm.getLampiranAddList().get(i).getLampiranNo() + "";
//            		}
//            		else 
//            		{
//            			tempLampiran = tempLampiran + "," + editForm.getLampiranAddList().get(i).getLampiranNo(); 
//            		}
//            	}
//            }
//        }
//        refundDbVO.setLampiran(tempLampiran);
        
        ArrayList<RefundDetDbVO> detailList = new ArrayList<RefundDetDbVO>();
        RefundDetDbVO refundDetDbVO;
        
        String titipan;
        String tglStr;
        BigDecimal rowKurs;
        BigDecimal rowJumlah = BigDecimal.ZERO;
        //List<SetoranPremiDbVO> setoranPremiDbVOList = elionsManager.selectSetoranPremiBySpaj( paramsVO.getSpajNo() );
        
        Integer biayaListIdx = 0;

        // setoran premi

        String spaj = paramsVO.getSpajNo();
        String lkuIdMstPolicy = elionsManager.selectLkuIdFromMstPolicy(spaj).get("LKU_ID").toString();
        
        ArrayList<SetoranPremiDbVO> setoranPremiDbVOList = paramsVO.getSetoranPremiDbVOList();
        for( SetoranPremiDbVO vo : setoranPremiDbVOList )
        {
            biayaListIdx = biayaListIdx + 1;
            // jika rupiah, abaikan nilai kurs di data base (karena ada yg null dan nol)
            BigDecimal jumlah = vo.getJumlah();
            if( jumlah == null )
            {
            	jumlah = BigDecimal.ZERO;
            }
            try{
	            rowKurs = "02".equals( vo.getLkuId() )? vo.getKurs() : BigDecimal.ONE;
	            if( vo.getLkuId() != null && !lkuIdMstPolicy.equals( vo.getLkuId() ) )
	            {
	            	if( vo.getLkuId().equals( RefundConst.KURS_RUPIAH ) && lkuIdMstPolicy.equals( RefundConst.KURS_DOLLAR ) )
	            	{
	            		 rowJumlah = jumlah.divide( rowKurs );
	            	}
	            	else if( vo.getLkuId().equals( RefundConst.KURS_DOLLAR ) && lkuIdMstPolicy.equals( RefundConst.KURS_RUPIAH ) )
	            	{
	            		 rowJumlah = jumlah.multiply( rowKurs );
	            	}
	            }
	            else if( vo.getLkuId() != null && lkuIdMstPolicy.equals( vo.getLkuId() ) )
	            {
	            	rowJumlah = jumlah;
	            }
	            else if( vo.getLkuId() == null )
	            {
	            	 rowJumlah = jumlah.multiply( rowKurs );
	            }
            }
            catch( Exception e )
            {
                logger.error("ERROR :", e);
            }
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
            refundDetDbVO = new RefundDetDbVO();
            if( tindakanCd == 2 && hasUnitFlag != null && hasUnitFlag == 1 ) // jika tindakan refund premi, link dan sudah fund
            {
                if( editForm.getCheckSpajInDetRefund() > 0 ) // jika sdh di save di mst_det_refund
                {
                	refundDetDbVO.setDeskripsi( vo.getDescr() );
                }
                else if( editForm.getCheckSpajInDetRefund() == 0 ) // jika blm di save di mst_det_refund
                {
                	refundDetDbVO.setDeskripsi( "Premi disetor tgl. " + tglStr + titipan );
                }
                refundDetDbVO.setTipe( RefundDetDbVO.Tipe.PREMI );
            }
            else if( tindakanCd == 2 && hasUnitFlag != null && hasUnitFlag == 0 )// jika tindakan refund premi, link dan blm fund
            {
            	refundDetDbVO.setDeskripsi( vo.getDescr() );
            	if( vo.getDescr().toLowerCase().contains( "premi top up" ) )
            	{
            		refundDetDbVO.setTipe( RefundDetDbVO.Tipe.TOPUP );
            	}
            	else if( vo.getDescr().toLowerCase().contains( "premi pokok" ) )
            	{
            		refundDetDbVO.setTipe( RefundDetDbVO.Tipe.PREMI );
            	}
            }
            else
            {
            	refundDetDbVO.setDeskripsi( "Premi disetor tgl. " + tglStr + titipan );
            	refundDetDbVO.setTipe( RefundDetDbVO.Tipe.PREMI );
            }
            refundDetDbVO.setItemNo( biayaListIdx );
            refundDetDbVO.setJumlah( rowJumlah );
            refundDetDbVO.setLjbId( null );
            refundDetDbVO.setSpajNo( paramsVO.getSpajNo() );
            refundDetDbVO.setUnit( null );
            refundDetDbVO.setLjiId( null );
            refundDetDbVO.setLkuId(vo.getLkuId());
            detailList.add( refundDetDbVO );
        }

        // Penarikan
        if( paramsVO.getPenarikanUlinkVOList() != null )
        {
            for( PenarikanUlinkVO vo : paramsVO.getPenarikanUlinkVOList() )
            {
                biayaListIdx = biayaListIdx + 1;
                refundDetDbVO = new RefundDetDbVO();
                refundDetDbVO.setDeskripsi(
                        "Penarikan " + vo.getJumlahUnit() + " unit " + vo.getLjiInvest() );
                refundDetDbVO.setItemNo( biayaListIdx );
                refundDetDbVO.setJumlah( vo.getJumlah() );
                refundDetDbVO.setLjbId( null );
                refundDetDbVO.setSpajNo( paramsVO.getSpajNo() );
                refundDetDbVO.setTipe( RefundDetDbVO.Tipe.WITHDRAW );
                refundDetDbVO.setUnit( vo.getJumlahUnit() );
                refundDetDbVO.setLjiId( vo.getLjiId() );    
                detailList.add( refundDetDbVO );
            }
        }

        // biaya lain2
        ArrayList<BiayaUlinkDbVO> biayaUlinkDbVOList;

//         biayaUlinkDbVOList = elionsManager.selectBiayaUlink( paramsVO.getSpajNo() );
        biayaUlinkDbVOList = paramsVO.getBiayaUlinkDbVOList();
        
        if( biayaUlinkDbVOList != null )
        {
            for( BiayaUlinkDbVO vo : biayaUlinkDbVOList )
            {
                biayaListIdx = biayaListIdx + 1;
                refundDetDbVO = new RefundDetDbVO();
                refundDetDbVO.setDeskripsi( vo.getDescr() );
                refundDetDbVO.setItemNo( biayaListIdx );
                refundDetDbVO.setJumlah( vo.getAmount() );
                refundDetDbVO.setLjbId( vo.getLjbId() );
                refundDetDbVO.setSpajNo( paramsVO.getSpajNo() );
                refundDetDbVO.setTipe( RefundDetDbVO.Tipe.BIAYA );
                refundDetDbVO.setUnit( null );
                refundDetDbVO.setLjiId( null );
                refundDetDbVO.setLkuId(vo.getLkuId());
                detailList.add( refundDetDbVO );
            }
        }

        //MANTA - Merchant Fee
        if( paramsVO.getBiayaMerchant() != null && !BigDecimal.ZERO.equals( paramsVO.getBiayaMerchant() ) )
        {
            biayaListIdx = biayaListIdx + 1;
            refundDetDbVO = new RefundDetDbVO();
            refundDetDbVO.setDeskripsi( "Biaya Merchant" );
            refundDetDbVO.setItemNo( biayaListIdx );
            refundDetDbVO.setJumlah( paramsVO.getBiayaMerchant() );
            refundDetDbVO.setLjbId( null );
            refundDetDbVO.setSpajNo( paramsVO.getSpajNo() );
            refundDetDbVO.setTipe( RefundDetDbVO.Tipe.MERCHANTFEE );
            refundDetDbVO.setUnit( null );
            refundDetDbVO.setLkuId( lkuIdMstPolicy );
            refundDetDbVO.setLjiId( null );
            detailList.add( refundDetDbVO );
        }
        
        // biaya yang dicentang2 di form
        if( paramsVO.getBiayaAdmin() != null && !BigDecimal.ZERO.equals( paramsVO.getBiayaAdmin() ) )
        {
            biayaListIdx = biayaListIdx + 1;
            refundDetDbVO = new RefundDetDbVO();
            refundDetDbVO.setDeskripsi( "Biaya Administrasi Pembatalan Polis" );
            refundDetDbVO.setItemNo( biayaListIdx );
            refundDetDbVO.setJumlah( paramsVO.getBiayaAdmin() );
            refundDetDbVO.setLjbId( RefundConst.LJB_BIAYA_ADMINISTRASI_PEMBATALAN_POLIS );
            refundDetDbVO.setSpajNo( paramsVO.getSpajNo() );
            refundDetDbVO.setTipe( RefundDetDbVO.Tipe.BIAYA );
            refundDetDbVO.setUnit( null );
            refundDetDbVO.setLjiId( null );
            refundDetDbVO.setLkuId("01");
            detailList.add( refundDetDbVO );
        }
        if( paramsVO.getBiayaMedis() != null && !BigDecimal.ZERO.equals( paramsVO.getBiayaMedis() ) )
        {
            biayaListIdx = biayaListIdx + 1;
            refundDetDbVO = new RefundDetDbVO();
            refundDetDbVO.setDeskripsi( "Biaya Medis" );
            refundDetDbVO.setItemNo( biayaListIdx );
            refundDetDbVO.setJumlah( paramsVO.getBiayaMedis() );
            refundDetDbVO.setLjbId( RefundConst.LJB_BIAYA_MEDIS );
            refundDetDbVO.setSpajNo( paramsVO.getSpajNo() );
            refundDetDbVO.setTipe( RefundDetDbVO.Tipe.BIAYA );
            refundDetDbVO.setUnit( null );
            refundDetDbVO.setLkuId("01");
            refundDetDbVO.setLjiId( null );
            detailList.add( refundDetDbVO );
        }
        if( paramsVO.getBiayaLain() != null && !BigDecimal.ZERO.equals( paramsVO.getBiayaLain() ) )
        {
            biayaListIdx = biayaListIdx + 1;
            refundDetDbVO = new RefundDetDbVO();
            refundDetDbVO.setDeskripsi( paramsVO.getBiayaLainDescr() );
            refundDetDbVO.setItemNo( biayaListIdx );
            refundDetDbVO.setJumlah( paramsVO.getBiayaLain() );
            refundDetDbVO.setLjbId( RefundConst.LJB_BIAYA_LAIN );
            refundDetDbVO.setSpajNo( paramsVO.getSpajNo() );
            refundDetDbVO.setTipe( RefundDetDbVO.Tipe.BIAYA );
            refundDetDbVO.setUnit( null );
            refundDetDbVO.setLkuId("01");
            refundDetDbVO.setLjiId( null );
            detailList.add( refundDetDbVO );
        }

        refundDbVO.setDetailList( detailList );

        Boolean succeed;
        try
        {
            elionsManager.deleteThenInsertRefund( refundDbVO );
            pageMessage = actionMessage + " sukses pada ";
            succeed = true;
        }
        catch( Exception e )
        {
            logger.error("ERROR :", e);
            pageMessage = actionMessage + " gagal pada ";
            succeed = false;
        }
        pageMessage = pageMessage.concat( getTimeStr( paramsVO.getUpdateWhen() ) );
        result.put( "pageMessage", pageMessage );
        result.put( "succeed", succeed );

        return result;
    }

    public Connection getConnection() 
    {
        Connection con = null;

        try
        {
            con = elionsManager.getUwDao().getDataSource().getConnection();
        }
        catch( SQLException e )
        {
            logger.error("ERROR :", e);
        }

        return con;
    }

    // Fungsi standard di refund untuk mengirim email
    // Perlu diperhatikan bahwa nama params untuk list harus "dataSource"
    public String sendEmail( User currentUser, RefundDocumentVO documentVO, String dirName, String fileName, String sender,
    		String[] to, String[] cc, String[] bcc, String subject, String content, Boolean flag_email)
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundCommonBusiness.sendEmail" );
        String result = "";

        if( dirName != null && !dirName.trim().equals( "" ) ) dirName = dirName.concat( "\\" );
        
//		Untuk testing
//        content = "EMAIL INI HANYA MERUPAKAN TESTING, HARAP DIABAIKAN";
//        String[] test_emailTujuan = {"fadly_m@sinarmasmsiglife.co.id"};
//        String[] test_cc = {"antasari@sinarmasmsiglife.co.id"};
//        String[] test_bcc = {"Dewi_Andriyati@sinarmasmsiglife.co.id"};
//        emailTujuan = test_emailTujuan;
//        cc = test_cc;
//        bcc = test_bcc;

        try{
            ArrayList<File> attachments = null;

            //Penambahan attachment
            if( fileName != null && documentVO != null ){
                ArrayList list = (ArrayList ) documentVO.getParams().get( "dataSource" );
                JasperUtils.exportReportToPdf(
                        documentVO.getJasperFile(),
                        dirName,
                        fileName,
                        documentVO.getParams(),
                        list,
                        PdfWriter.AllowPrinting,
                        null,
                        null
                );

                File sourceFile = new File( dirName + "\\" + fileName );
                attachments = new ArrayList<File>();
                attachments.add( sourceFile );
                
                //Permintaan Hayatin - Canpri
                //upload pdf ke ebserver/pdfind/polis/
                if(fileName.contains("surat_refund")){
                	try{
	                	String spaj = fileName.substring(13);
	                	spaj = spaj.substring(0,spaj.indexOf("_"));
	                	
	                	String lca = elionsManager.selectLcaIdBySpaj(spaj);
	                	
	                	String dir = "\\\\ebserver\\pdfind\\Polis\\"+lca+"\\"+spaj+"\\";
	                	String fileName2 = spaj+"BATAL.pdf";
	                	
	                	JasperUtils.exportReportToPdf(
	                            documentVO.getJasperFile(),
	                            dir,
	                            fileName2,
	                            documentVO.getParams(),
	                            list,
	                            PdfWriter.AllowPrinting,
	                            null,
	                            null
	                    );
                	} catch(Exception e){
                		logger.error("ERROR :", e);
                	}
                }
            }

            if(flag_email){
                try{
                    EmailPool.send("E-Lions", 1, 1, 0, 0, 
    						null, 0, 0, elionsManager.selectSysdateSimple(), null, 
    						true,
    						sender, 
    						to, 
    						cc, 
    						bcc, 
    						subject, 
    						content, 
    						attachments, null);
                    
                } catch ( MailException e){
                    logger.error("ERROR :", e);
                }
                
                finally{
                    if(logger.isDebugEnabled())logger.debug( "Error pada pengiriman email" );
                    result = result + "\\nGagal kirim email";
                }
            }

        } catch( Exception e ){
            logger.error("ERROR :", e);
            result = result + "\\nGagal export ke PDF";
        }

        if( result.equals( "" ) ) result = "Email telah dikirim.";

        return result;
    }

    public PolicyInfoVO selectPolicyInfoBySpaj( String spajNo )
    {
        return elionsManager.selectPolicyInfoBySpaj( spajNo );
    }

    public String spajNoAndPolicyNo( String spajNo )
    {
        PolicyInfoVO policyInfoVO = elionsManager.selectPolicyInfoBySpaj( spajNo );
        String polisNo ;

        if( policyInfoVO.getPolicyNo() == null || policyInfoVO.getPolicyNo().trim().equals( "" ) )
        {
            if( policyInfoVO.getNamaPp() != null && !policyInfoVO.getNamaPp().trim().equals( "" ) )
            {
                //polisNo = " atas nama " + policyInfoVO.getNamaPp();
            	 polisNo = "-";
            }
            else
            {
                polisNo = "";
            }
        }
        else
        {
            if( policyInfoVO.getNamaPp() != null && !policyInfoVO.getNamaPp().trim().equals( "" ) )
            {
               // polisNo = " / " + StringUtil.nomorPolis( policyInfoVO.getPolicyNo() ) + " atas nama " + policyInfoVO.getNamaPp();
            	 polisNo = " / " + StringUtil.nomorPolis( policyInfoVO.getPolicyNo() );
            }
            else
            {
            	polisNo = " / " + StringUtil.nomorPolis( policyInfoVO.getPolicyNo() );
            }
//            polisNo = " / " + StringUtil.nomorPolis( policyInfoVO.getPolicyNo() );
        }

        String spaj_polis = FormatString.nomorSPAJ( spajNo ) + polisNo;

        return spaj_polis;
    }

	public void insertMstPositionSpaj(String lus_id, String msps_desc, String reg_spaj, Integer addSecond){
		this.elionsManager.insertMstPositionSpaj(lus_id, msps_desc, reg_spaj, addSecond);
	}
	
	public HashMap selectMstConfig(Integer app_id, String section, String sub_section){
		return this.bacManager.selectMstConfig(app_id, section, sub_section);
	}
	
	public HashMap selectEmailBCAM2(String spaj){
		return this.bacManager.selectEmailBCAM2(spaj);
	}
}
