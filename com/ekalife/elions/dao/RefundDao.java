package com.ekalife.elions.dao;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundDao
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 7, 2008 11:08:35 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.elions.web.refund.vo.BiayaUlinkDbVO;
import com.ekalife.elions.web.refund.vo.CheckSpajParamsVO;
import com.ekalife.elions.web.refund.vo.InfoBatalVO;
import com.ekalife.elions.web.refund.vo.LampiranListVO;
import com.ekalife.elions.web.refund.vo.MstBatalParamsVO;
import com.ekalife.elions.web.refund.vo.MstPtcTmVO;
import com.ekalife.elions.web.refund.vo.PenarikanUlinkDbVO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.RefundDbVO;
import com.ekalife.elions.web.refund.vo.RefundDetDbVO;
import com.ekalife.elions.web.refund.vo.RefundViewVO;
import com.ekalife.elions.web.refund.vo.RekapInfoVO;
import com.ekalife.elions.web.refund.vo.SetoranPokokDanTopUpVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;
import com.ekalife.elions.web.refund.vo.SpajInfoVO;
import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentDao;


@SuppressWarnings( "unchecked" )
public class RefundDao extends ParentDao
{
    protected final Log logger = LogFactory.getLog( getClass() );

    protected void initDao() throws Exception
    {
		this.statementNameSpace = "elions.refund.";
	}

    public String selectKategoriUW(String spaj){
    	return (String) querySingle("selectKategoriUW", spaj);
    }
    
    public String generateNoBatalRefund( String lcaId )
    {
        return this.sequence.sequenceMst_batal( 24, lcaId );
    }

    public Double selectNewNoSuratRefund()
    {
        return ( Double ) querySingle( "selectNewNoSuratRefund", null );
    }

    public BigInteger selectMaxNoSuratRefund()
    {
        return ( BigInteger ) querySingle( "selectMaxNoSuratRefund", null );
    }

    public PolicyInfoVO selectPolicyInfoBySpaj( String spajNo )
    {
        return ( PolicyInfoVO ) querySingle( "selectPolicyInfoBySpaj", spajNo );
    }
    
    public List<PolicyInfoVO> selectPolicyInfoBySpajList( String[] spajList )
    {
    	Map params = new HashMap();
    	params.put("spajList", spajList);
        return ( List<PolicyInfoVO> ) query( "selectPolicyInfoBySpajList", params );
    }
    
    public List < RekapInfoVO > selectInfoForRekap( Map<String, Object> params )
    {
        return ( List< RekapInfoVO > ) query( "selectInfoForRekap", params );
    }
    
    public List < RekapInfoVO > selectInfoForRekapKeAccFinance( Map<String, Object> params )
    {
        return ( List< RekapInfoVO > ) query( "selectInfoForRekapKeAccFinance", params );
    }
    
    public List<HashMap> selectNoPre( String reg_spaj )
    {
        return ( List<HashMap> ) query( "selectNoPre", reg_spaj );
    }
    
    public MstPtcTmVO selectMstPtcTm( String no_pre )
    {
        return ( MstPtcTmVO ) querySingle( "selectMstPtcTm", no_pre );
    }
    
    public Integer selectPositionMstTbank( String no_pre )
    {
        return ( Integer ) querySingle( "selectPositionMstTbank", no_pre );
    }
    
    public Integer selectPositionMstPtcTm( String no_jm )
    {
        return ( Integer ) querySingle( "selectPositionMstPtcTm", no_jm );
    }
    
    public InfoBatalVO selectInfoBatalBySpaj( String spajNo )
    {
        return ( InfoBatalVO ) querySingle( "selectInfoBatalBySpaj", spajNo );
    }

    public SpajInfoVO selectSpajInfoBySpaj( String spajNo )
    {
        return ( SpajInfoVO ) querySingle( "selectSpajInfoBySpaj", spajNo );
    }

    public int updateNoSuratRefund( Double newCounterCd )
    {
        return update( "updateNoSuratRefund", newCounterCd );
    }
    
    public void updatePositionMstTBank( Map<String, Object> params )
    {
        update( "updatePositionMstTBank", params );
    }
    
    public void updatePositionMstPtcTm( Map<String, Object> params )
    {
        update( "updatePositionMstPtcTm", params );
    }
    
    public void insertMstRefund( RefundDbVO refundDbVO )
    {
		insert( "insertMstRefund", refundDbVO );
	}
    
    public void insertMstDetRefundLamp( RefundDbVO refundDbVO )
    {
    	if( refundDbVO.getAddLampiranList() != null && refundDbVO.getAddLampiranList().size() > 0 )
    	{
    		for(int i = 0 ; i < refundDbVO.getAddLampiranList().size() ; i ++ )
    		{
    			String checked = null;
    	    	Map params = new HashMap();
    	    	params.put("regSpaj", refundDbVO.getSpajNo() );
    	    	params.put("lampiran", refundDbVO.getAddLampiranList().get(i).getLampiranLabel() );
    	    	if(CommonConst.CHECKED_TRUE.equals( refundDbVO.getAddLampiranList().get(i).getCheckBox() ) )
    	    	{
    	    		checked = "1";
    	    	}
    	    	else if( refundDbVO.getAddLampiranList().get(i).getCheckBox() == CommonConst.CHECKED_FALSE )
    	    	{
    	    		checked = "0";
    	    	}
    	    	params.put("checked", checked );
    	    	params.put("noUrut", i+1);
    			insert( "insertMstDetRefundLamp", params );
    		}
    	}
	}
    
    public void insertMstTbankJurnalBalik( String no_pre_new, String lus_id, String no_pre, Double jumlah )
    {
    	Map params = new HashMap();
    	params.put("no_pre_new", no_pre_new );
    	params.put("lus_id", lus_id );
    	params.put("no_pre", no_pre );
    	params.put("jumlah", jumlah );
        insert( "insertMstTbankJurnalBalik", params );
	}
    
    public void insertMstDbankJurnalBalik( String spaj, String no_pre_new, String no_pre, String voucherall, Integer jumlah, Integer fee )
    {
    	Map params = new HashMap();
    	params.put("spaj", spaj );
    	params.put("no_pre_new", no_pre_new );
    	params.put("no_pre", no_pre );
    	params.put("no_voucher", voucherall );
    	params.put("jumlah", jumlah );
    	params.put("fee", fee );
        insert( "insertMstDbankJurnalBalik", params );
	}
    
    public void insertMstPtcTmJurnalBalik(String no_pre_new, String no_jm_new, String no_jm, String lus_id)
    {
    	Map params = new HashMap();
    	params.put("no_pre_new", no_pre_new);
    	params.put("no_jm_new", no_jm_new);
    	params.put("no_jm", no_jm);
    	params.put("lus_id", lus_id);
        insert( "insertMstPtcTmJurnalBalik", params );
	}
    
    public void insertMstPtcJmJurnalBalik(String no_jm_new, String no_jm, Integer flagjm)
    {
    	Map params = new HashMap();
    	params.put("no_jm_new", no_jm_new);
    	params.put("no_jm", no_jm);
    	params.put("flagjm", flagjm);
        insert("insertMstPtcJmJurnalBalik", params);
	}
    
    public void insertMstPtcJmJurnalBalikSA(String reg_spaj, String no_pre, String no_jm_sa, String no_jm_sa_new, Double jumlah)
    {
    	Map params = new HashMap();
    	params.put("reg_spaj", reg_spaj);
    	params.put("no_pre", no_pre);
    	params.put("no_jm_sa", no_jm_sa);
    	params.put("no_jm_sa_new", no_jm_sa_new);
    	params.put("jumlah", jumlah);
        insert("insertMstPtcJmJurnalBalikSA", params);
	}
    
    public void insertMstDbankJurnalBalikSA(String reg_spaj, String no_pre, String no_pre_new, String no_jm_sa, String ket, Double jumlah, Double fee)
    {
    	Map params = new HashMap();
    	params.put("reg_spaj", reg_spaj);
    	params.put("no_pre", no_pre);
    	params.put("no_pre_new", no_pre_new);
    	params.put("no_jm_sa", no_jm_sa);
    	params.put("ket", ket);
    	params.put("jumlah", jumlah);
    	if(fee == 0.0){
    		fee = null;
    	}
    	params.put("fee", fee);
        insert("insertMstDbankJurnalBalikSA", params);
	}
    
    public void insertMstPembayaran(Integer trans_id, String no_pre, String lku_id, String nama_bank, String mrc_atas_nama, String mrc_cabang, String mrc_kota, String mrc_no_ac, Integer nominal, String lus_id)
    {
    	Integer lsbp_id = (Integer) querySingle("selectKodeBankPusat", nama_bank);
    	
    	Map params = new HashMap();
    	params.put("trans_id", trans_id);
    	params.put("no_pre", no_pre);
    	params.put("lku_id", lku_id);
    	params.put("lsbp_id", lsbp_id);
    	params.put("mrc_atas_nama", mrc_atas_nama);
    	params.put("mrc_cabang", mrc_cabang);
    	params.put("mrc_kota", mrc_kota);
    	params.put("mrc_no_ac", mrc_no_ac);
    	params.put("nominal", nominal);
    	params.put("lus_id", lus_id);
        insert("insertMstPembayaran", params);
	}
    
    public void insertMstDetRefundLampiran( String spaj, String lampiran, String checkBox, Integer noUrut )
    {
    	String checked = null;
    	Map params = new HashMap();
    	params.put("regSpaj", spaj );
    	params.put("lampiran", lampiran );
    	if(CommonConst.CHECKED_TRUE.equals( checkBox ) )
    	{
    		checked = "1";
    	}
    	else if( checkBox == CommonConst.CHECKED_FALSE )
    	{
    	 	checked = "0";
    	}
    	params.put("checked", checked );
    	params.put("noUrut", noUrut );
    	insert( "insertMstDetRefundLamp", params );
	}

    public void insertMstDetRefund( RefundDetDbVO refundDetDbVO )
    {
        insert( "insertMstDetRefund", refundDetDbVO );
	}

    public List<SetoranPremiDbVO> selectSetoranPremiBySpaj( String spajNo )
    {
    	//MANTA
    	HashMap params = new HashMap();
    	Integer paid = 1;
    	Integer flag_cc = uwDao.selectValidasiTransferPbp(spajNo);
//    	if(flag_cc == 1 && spajNo.substring(0, 2).equals("40")){
//    		paid = 0;
//    	}
    	params.put("reg_spaj", spajNo );
    	params.put("paid", paid );
    	List<HashMap> noPreListTmp = selectNoPreTemp(spajNo);
    	List<HashMap> billingrefund = null;
    	List<SetoranPremiDbVO> setoran = null;
    	
    	if(noPreListTmp != null && noPreListTmp.size()>0){
    		setoran = query( "selectSetoranPremiBySpajTmp", spajNo );
    	}else{
    		billingrefund = query( "selectBillingRefund", params );
    		setoran = query( "selectSetoranPremiBySpaj", spajNo );
    	}

    	if(billingrefund != null && billingrefund.size() > 0){
    		for(int i=0;i<billingrefund.size();i++){
    			Integer flag_remain = 0;
    			String bill_kurs = billingrefund.get(i).get("LKU_ID").toString();
    			Date bill_date = (Date) billingrefund.get(i).get("MSBI_PAID_DATE");
    			BigDecimal bill_remain = (BigDecimal) billingrefund.get(i).get("MSBI_REMAIN");
    			if(bill_remain.intValue() < 0 && paid == 1){
    				bill_remain = bill_remain.negate();
    			}
    			//Menambahkan biaya kartu kredit yang ditanggung AJS, sebagai uang yang dikembalikan ke Nasabah
    			for(int j=0;j<setoran.size();j++){
    				if((setoran.get(j).getTglSetor().equals(bill_date) && setoran.get(j).getLkuId().toString().equals(bill_kurs) && flag_remain == 0) || paid == 0){
//    					BigDecimal total = setoran.get(j).getJumlah().add(bill_remain);
//    					setoran.get(j).setJumlah(total);
//    					flag_remain = 1;
    				}
    			}
    		}
    	}
        return setoran;
    }
    
    public List<SetoranPremiDbVO> selectPenarikanUlinkSortedByMsdpNumber( String spajNo )
    {
        return ( List<SetoranPremiDbVO> ) query( "selectPenarikanUlinkSortedByMsdpNumber", spajNo );
    }
    
    public List<PenarikanUlinkDbVO> selectPenarikanUlink( String spajNo )
    {
        return ( List<PenarikanUlinkDbVO> ) query( "selectPenarikanUlink", spajNo );
    }

    public List<BiayaUlinkDbVO> selectBiayaUlink( String spajNo )
    {
        return ( List<BiayaUlinkDbVO> ) query( "selectBiayaUlink", spajNo );
    }

    public List<RefundViewVO> selectRefundList( Map<String, Object> params )
    {
        return ( List<RefundViewVO> ) query( "selectRefundList", params );
	}

    public List<RefundDetDbVO> selectRefundDetList( Map<String, Object> params )
    {
        return ( List<RefundDetDbVO> ) query( "selectRefundDetList", params );
    }
    
    public String selectJenisInvestByLjiId( String ljiId )
    {
        return ( String ) querySingle( "selectJenisInvestByLjiId", ljiId );
    }

    public Integer selectRefundTotalOfPages( Map<String, Object> params )
    {
        return ( Integer ) querySingle( "selectRefundTotalOfPages", params );
    }

    public RefundDbVO selectRefundByCd( String regSpaj )
    {
        return ( RefundDbVO ) querySingle( "selectRefundByCd", regSpaj );
    }

    public List<RefundDbVO> selectMstRefund( String regSpaj )
    {
        return ( List<RefundDbVO> ) query( "selectMstRefund", regSpaj );
    }
    
    public Date selectNowDate()
    {
        return ( Date ) querySingle( "selectNowDate", null );
    }
    
    public Date selectTglKirimDokFisik( String regSpaj )
    {
        return ( Date ) querySingle( "selectTglKirimDokFisik", regSpaj );
    }
    
    public Integer selectNoSuratList( String noSuratDate )
    {
        return ( Integer ) querySingle( "selectNoSuratList", noSuratDate );
    }
    
    public Date selectMspoDatePrint( String regSpaj )
    {
        return ( Date ) querySingle( "selectMspoDatePrint", regSpaj );
    }
    
    public List <SetoranPokokDanTopUpVO> selectSetoranPremiPokokDanTopUp( String regSpaj )
    {
        return ( List< SetoranPokokDanTopUpVO > ) query( "selectSetoranPremiPokokDanTopUp", regSpaj );
    }
    
    public Integer selectMaxNoUrutMstDetRefLamp( String regSpaj )
    {
        return ( Integer ) querySingle( "selectMaxNoUrutMstDetRefLamp", regSpaj );
    }
    
    public List <LampiranListVO> selectMstDetRefundLamp( String regSpaj )
    {
        return ( List< LampiranListVO > ) query( "selectMstDetRefundLamp", regSpaj );
    }
    
    public MstBatalParamsVO selectSpajCancelMstBatal( String regSpaj )
    {
        return ( MstBatalParamsVO ) querySingle( "selectSpajCancelMstBatal", regSpaj );
    }
    
    public CheckSpajParamsVO selectSpajAlreadyCancelMstRefund( String regSpaj )
    {
        return ( CheckSpajParamsVO ) querySingle( "selectSpajAlreadyCancelMstRefund", regSpaj );
    }
    
    public CheckSpajParamsVO selectCheckSpajInDb( String regSpaj )
    {
        return ( CheckSpajParamsVO ) querySingle( "selectCheckSpajInDb", regSpaj );
    }
    
    public void insertRefund( RefundDbVO refundDbVO )
    {
		insert( "deleteThenInsertRefund", refundDbVO );
	}

    public void deleteMstDetRefundBySpaj( String regSpaj )
    {
        delete( "deleteMstDetRefundBySpaj", regSpaj );
    }

    public void deleteMstRefundBySpaj( String regSpaj )
    {
        delete( "deleteMstRefundBySpaj", regSpaj );
    }
    
    public void deleteMstDetRefundLamp( String regSpaj )
    {
        delete( "deleteMstDetRefundLamp", regSpaj );
    }
    
    public void deleteMstDrekDet( String regSpaj, String no_trx, String no_ke )
    {
    	Map map = new HashMap();
    	map.put("regSpaj", regSpaj);
    	map.put("no_trx", no_trx);
    	map.put("no_ke", no_ke);
        delete( "deleteMstDrekDet", map );
    }
    
    public void updatePosisiAndCancelRefund( Map<String, Object> params )
    {
        update( "updatePosisiAndCancelRefund", params );
    }
    
    public void updateTglKirimDokFisik( Map<String, Object> params )
    {
        update( "updateTglKirimDokFisik", params );
    }
    
    public void updateWhoAndWhenMstRefund( Map<String, Object> params )
    {
        update( "updateWhoAndWhenMstRefund", params );
    }
    
    public void updateMstPowerSaveRo( Map<String, Object> params )
    {
        update( "updateMstPowerSaveRo", params );
    }
    
    public void updateMstSLink( Map<String, Object> params )
    {
        update( "updateMstSLink", params );
    }
    
    public void updateMstSSave( Map<String, Object> params )
    {
        update( "updateMstSSave", params );
    }
    
    public void updateMstPSave( Map<String, Object> params )
    {
        update( "updateMstPSave", params );
    }
    
    public void updateMstDrekBySpaj( String spaj )
    {
        update( "updateMstDrekBySpaj", spaj );
    }
    
    public String selectLstDocumentPosition( String regSpaj )
    {
        return ( String ) querySingle( "selectLstDocumentPosition", regSpaj );     
    }
    
    public Date selectMsteTglKirimPolis( String regSpaj )
    {
        return ( Date ) querySingle( "selectMsteTglKirimPolis", regSpaj );     
    }
    
    public List selectFilterRefundSpaj(String posisi, String tipe, String kata, String pilter,String lssaId,String lsspId, String tgl_lahir, String isNewSpaj) {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";

		Map params = new HashMap();
//		if(lssaId==null){
//			params.put("posisi", posisi);
//		}else{
//			params.put("posisi", "in ("+posisi+")");
//		}
		params.put("tipe", tipe);
		params.put("kata", kata);
		params.put("lssaId",lssaId);
		params.put("lssp_id",lsspId);
		params.put("tgl_lahir", tgl_lahir);
		params.put("isNewSpaj", isNewSpaj);
		return query("selectFilterRefundSpaj", params);
	}

    public Integer selectPosisiDocumentBySpaj( String spajNo )
    {
        return ( Integer ) querySingle( "selectPosisiDocumentBySpaj", spajNo );     
    }
    
    public String selectLusFullName( BigDecimal lusId )
    {
        return ( String ) querySingle( "selectLusFullName", lusId );     
    }
    
    public Integer selectPowerSaveInterestPaid( String spajNo )
    {
        return ( Integer ) querySingle( "selectPowerSaveInterestPaid", spajNo );
    }

    public Integer selectPowerSaveRollOver( String spajNo )
    {
        return ( Integer ) querySingle( "selectPowerSaveRollOver", spajNo );
    }
    
    public Integer selectCheckSpaj( String spajNo )
    {
        return ( (Integer) querySingle( "selectCheckSpaj", spajNo ) );
    }
    
    public String selectMtuUnitTransUlink( String regSpaj )
    {
        return ( (String) querySingle( "selectMtuUnitTransUlink", regSpaj ) );
    }
    
    public Map selectLkuIdFromMstPolicy( String regSpaj )
    {
        return ( (Map) querySingle( "selectLkuIdFromMstPolicy", regSpaj ) );
    }
    
    
    public BigDecimal selectAksesPembatalanCabang( String regSpaj, BigDecimal lusId )
    {
        Map params = new HashMap();
        params.put( "regSpaj", regSpaj );
        params.put( "lusId", lusId );
        return ( (BigDecimal) querySingle( "selectAksesPembatalanCabang", params ) );
    }

	public List selectDocBatal(String spaj) {
		return query("selectDocBatal",spaj);
	}
	
    public ArrayList<HashMap> selectSumRefAksep(String lku_id)
    {
        return ( ArrayList<HashMap> ) query("selectSumRefAksep", lku_id);
    }
    
    public ArrayList<HashMap> selectDetRefAksep(String lku_id)
    {
        return ( ArrayList<HashMap> ) query("selectDetRefAksep", lku_id);
    }
    
    public ArrayList<HashMap> selectDetRefPegaAksep(String lku_id)
    {
        return ( ArrayList<HashMap> ) query("selectDetRefPegaAksep", lku_id);
    }
    
    public ArrayList<HashMap> selectUploadRefund(String flag_bank, String lku_id)
    {
        Map params = new HashMap();
        params.put( "flag_bank", flag_bank );
        params.put( "lku_id", lku_id );
        return ( ArrayList<HashMap> ) query("selectUploadRefund", params);
    }
    
    public ArrayList<HashMap> selectSumRefAksepNonSpaj(String lku_id)
    {
        return ( ArrayList<HashMap> ) query("selectSumRefAksepNonSpaj", lku_id);
    }
    
    public ArrayList<HashMap> selectDetRefAksepNonSpaj(String lku_id)
    {
        return ( ArrayList<HashMap> ) query("selectDetRefAksepNonSpaj", lku_id);
    }
    
    public ArrayList<HashMap> selectUploadRefundNonSpaj(String flag_bank, String lku_id)
    {
    	Map params = new HashMap();
        params.put( "flag_bank", flag_bank );
        params.put( "lku_id", lku_id );
        return ( ArrayList<HashMap> ) query("selectUploadRefundNonSpaj", params);
    }
    
    public Double selectPremiRefund(String reg_spaj) {
    	return (Double) querySingle("selectPremiRefund", reg_spaj);
    }
    
    public String selectNoVoucher(String no_pre)
    {
        return (String) querySingle("selectNoVoucher", no_pre);
    }
    
    public Integer selectCekBayarKomisi(String reg_spaj) {
    	return (Integer) querySingle("selectCekBayarKomisi", reg_spaj);
    }
    
    public List selectMstBvoucher(String no_pre, Integer no_jurnal) {
    	Map map = new HashMap();
    	map.put("no_pre", no_pre);
    	map.put("no_jurnal", no_jurnal);
    	return (List) query("selectMstBvoucher", map);
    }
    
    public void insertMstTempPremiRefund(String reg_spaj, Integer status, Integer jenis, String mstp_norek, String mstp_nama,
    		String mstp_bank, String mstp_cabang_bank, String mstp_kota_bank, Double mstp_payment, String lku_id, String nopre_msk, 
    		String novoc_msk, Double mstp_nilai_kurs, String mstp_user_input, String no_trx, String keterangan, Integer tindakan)
    {
       	Map params = new HashMap();
    	params.put("reg_spaj", reg_spaj);
    	params.put("status", status);
    	params.put("jenis", jenis);
    	params.put("mstp_norek", mstp_norek);
    	params.put("mstp_nama", mstp_nama);
    	params.put("mstp_bank", mstp_bank);
    	params.put("mstp_cabang_bank", mstp_cabang_bank);
    	params.put("mstp_kota_bank", mstp_kota_bank);
    	params.put("mstp_payment", mstp_payment);
    	params.put("lku_id", lku_id);
    	params.put("nopre_msk", nopre_msk);
    	params.put("novoc_msk", novoc_msk);
    	params.put("mstp_nilai_kurs", mstp_nilai_kurs);
    	params.put("mstp_user_input", mstp_user_input);
    	params.put("no_trx", no_trx);
    	params.put("keterangan", keterangan);
    	params.put("tindakan", tindakan);
        insert("insertMstTempPremiRefund", params);
	}
    
    public void insertMstTempPremiRefundMod(PolicyInfoVO dataRefund, Integer status, Integer jenis, String mstp_user_input,
    		String nopre_msk, String novoc_msk, String nojmsa)
    {
       	Map params = new HashMap();
    	params.put("reg_spaj", dataRefund.getSpajNo());
    	params.put("status", status);
    	params.put("jenis", jenis);
    	params.put("mstp_norek", dataRefund.getKliNoRek());
    	params.put("mstp_nama", dataRefund.getKliAtasNama());
    	params.put("mstp_bank", dataRefund.getKliNamaBank());
    	params.put("mstp_cabang_bank", dataRefund.getKliCabangBank());
    	params.put("mstp_kota_bank", dataRefund.getKliKotaBank());
    	params.put("mstp_payment", dataRefund.getPremirefundtmp());
    	params.put("lku_id", dataRefund.getLkuId());
    	params.put("nopre_msk", nopre_msk);
    	params.put("novoc_msk", novoc_msk);
    	params.put("mstp_nilai_kurs", dataRefund.getNilaiKurs());
    	params.put("mstp_user_input", mstp_user_input);
    	params.put("no_trx", dataRefund.getNo_trx());
    	params.put("keterangan", dataRefund.getKeterangan());
    	params.put("tindakan", dataRefund.getTindakan());
    	params.put("nojmsa", nojmsa);
    	params.put("tgl_rk", dataRefund.getTgl_rk());
    	params.put("premi_ke", dataRefund.getPremi_ke());
        insert("insertMstTempPremiRefundMod", params);
	}
    
    public void updateMstTempPremiRefund(String reg_spaj, String no_pre, String no_jm_sa, String no_jm_sa_new)
    {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("reg_spaj", reg_spaj);
    	params.put("no_pre", no_pre);
    	params.put("no_jm_sa", no_jm_sa);
    	params.put("no_jm_sa_new", no_jm_sa_new);
        update( "updateMstTempPremiRefund", params );
    }
    
	public List<Map> selectMstTempPremiRefund(String reg_spaj, Integer jenis, Integer premi_ke){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("reg_spaj", reg_spaj);
    	params.put("jenis", jenis);
    	params.put("premi_ke", premi_ke);
    	return ( List<Map> ) query("selectMstTempPremiRefund", reg_spaj);
    }
    
    public List<HashMap> selectNoPreTemp( String reg_spaj ){
        return ( List<HashMap> ) query( "selectNoPreTemp", reg_spaj );
    }
    
    public void updateMstRefund(Map<String, Object> params){
        update("updateMstRefund", params);
    }
    
    public List<HashMap> selectNoJmSa(String reg_spaj){
        return ( List<HashMap> ) query("selectNoJmSa", reg_spaj);
    }
    
    public void insertMstPtcJmSaJurnalBalik(String no_jm_new, String no_jm){
    	Map params = new HashMap();
    	params.put("no_jm_new", no_jm_new);
    	params.put("no_jm", no_jm);
        insert("insertMstPtcJmSaJurnalBalik", params);
	}
    
    public void insertMstUlinkRedeem(String reg_spaj, Date tgl_trans){
       	Map params = new HashMap();
    	params.put("reg_spaj", reg_spaj);
    	params.put("tgl_trans", tgl_trans);
        insert("insertMstUlinkRedeem", params);
	}
    
    public void insertMstDetUlinkRedeem(String reg_spaj){
       	Map params = new HashMap();
    	params.put("reg_spaj", reg_spaj);
        insert("insertMstDetUlinkRedeem", params);
	}
    
    public void insertMstTransUlinkRedeem(String reg_spaj, String lus_id, Date tgl_trans){
       	Map params = new HashMap();
    	params.put("reg_spaj", reg_spaj);
    	params.put("lus_id", lus_id);
    	params.put("tgl_trans", tgl_trans);
        insert("insertMstTransUlinkRedeem", params);
	}
    
    public void insertMstDrekDetBatal(String reg_spaj, String payment_id, String no_jm){
       	Map params = new HashMap();
    	params.put("reg_spaj", reg_spaj);
    	params.put("payment_id", payment_id);
    	params.put("no_jm", no_jm);
        insert("insertMstDrekDetBatal", params);
	}
    
    public void updateNoVaBatalPolis(String spaj, String no_va){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("spaj", spaj);
    	params.put("no_va", no_va);
        update("updateNoVaBatalPolis", params);
    }
    
    public void updateMsteNoVacc(String spaj, String no_va){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("spaj", spaj);
    	params.put("no_va", no_va);
        update("updateMsteNoVacc", params);
    }
    
    public Map selectPaymentKe(String reg_spaj, Integer premi_ke){
       	Map params = new HashMap();
    	params.put("reg_spaj", reg_spaj);
    	params.put("premi_ke", premi_ke);
        return (Map) querySingle("selectPaymentKe", params);
    }
    
    public List<Map> selectRefundPremiLanjutan(){
        return (List<Map>) query("selectRefundPremiLanjutan", null);
    }
    
    public Integer CekDataPremiRefund(String reg_spaj, Integer premi_ke) throws DataAccessException {
       	Map params = new HashMap();
    	params.put("reg_spaj", reg_spaj);
    	params.put("premi_ke", premi_ke);
        return  (Integer) querySingle("CekDataPremiRefund", params);
    }
}
