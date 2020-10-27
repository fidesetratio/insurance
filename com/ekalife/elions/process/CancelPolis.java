package com.ekalife.elions.process;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.ekalife.elions.model.DepositPremium;
import com.ekalife.elions.model.DrekDet;
import com.ekalife.elions.model.Payment;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Premi;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.refund.RefundConst;
import com.ekalife.elions.web.refund.vo.MstPtcTmVO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.RefundDbVO;
import com.ekalife.elions.web.refund.vo.RefundDetDbVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.parent.ParentDao;

public class CancelPolis extends ParentDao {
	
	public void cancelPolisFromPayment(String spaj, String alasan, User currentUser) throws DataAccessException {
		uwDao.insertMst_batal(spaj, this.sequence.sequenceMst_batal(24, uwDao.selectCabangFromSpaj(spaj)), alasan, currentUser.getLus_id());// mst_counter
		uwDao.updateMst_billing_cancel(spaj, 95);
		uwDao.updateMst_reins_cancel(spaj);
		uwDao.updateMst_policy(spaj, new Double(95), null, null);
		uwDao.updateMstPolicyLsspdId(spaj, 9);
		uwDao.updateAktifSimasCard(spaj, 0);
		
	}

	public void cancelPolisFromUw(String spaj, String ls_alasan, String lus_id) throws DataAccessException {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		Integer li_reas_polis;
		Integer li_count=new Integer(0);
		li_reas_polis = (Integer) uwDao.selectCountMstReins(spaj);
		
		if(li_reas_polis > 0){
			uwDao.updateMstReins(spaj);
		}
		
		uwDao.updateMstPolicyCancelPolis(spaj);
		li_count = (Integer) uwDao.selectCountMstSampleUw(spaj);
		if(li_count > 0){
			uwDao.updateMstSampleuw(spaj);
		}
		
		String ls_lca,ls_batal;
		ls_lca = (String) uwDao.selectCabangFromSpaj(spaj);
		ls_batal = this.sequence.sequenceMst_batal(24, ls_lca);
		
		uwDao.insertMst_batal(spaj,ls_batal,ls_alasan,lus_id);
		uwDao.updateAktifSimasCard(spaj, 0);
		uwDao.insertMstPositionSpaj("0", ls_alasan, spaj, 0);
		
	}

	public void cancelPolisFromTandaTerimaPolis(String spaj, String alasan, User currentUser) throws DataAccessException {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		DateFormat datefm = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat datestr = new SimpleDateFormat("dd-MM-yyyy");
		NumberFormat nf = new DecimalFormat("#.00;(#,##0.00)");
		Date nowDate = commonDao.selectSysdateTruncated(0);
		RefundDbVO refundDbVO = refundDao.selectRefundByCd(spaj);
		Integer i_lspdid = 95;
		if(refundDbVO.getAlasanCd() == RefundConst.ALASAN_NOT_PROCEED_WITH){
			i_lspdid = 999;
		}
		
		Integer lsgb_id = reinstateDao.selectIsPolisUnitlink(spaj);
		String param = "individu";
		if(lsgb_id == 17) param = "ulink";
		Date tglrk = uwDao.selectMaxRkDate(spaj, 1, 1, param);
		String lca_id = uwDao.selectCabangFromSpaj(spaj);
		String lsbs = uwDao.selectLsbsId(spaj);
		String lsdbs = uwDao.selectLsdbsNumber(spaj);
		Integer jenis = 1;
		if((lsbs.equals("190") && lsdbs.equals("9"))||(lsbs.equals("200") && lsdbs.equals("7")))  jenis = 3;
		Date tglprod = commonDao.selectTanggalProduksiUntukProsesProduksi(tglrk, lca_id, jenis);
		uwDao.insertProductionFromCancel(tglprod, spaj);
		uwDao.insertDetProductionFromCancel(tglprod, spaj);
		
		uwDao.insertMst_batal(spaj, this.sequence.sequenceMst_batal(24, uwDao.selectCabangFromSpaj(spaj)), alasan, currentUser.getLus_id());
		uwDao.updateMst_billing_cancel(spaj, i_lspdid);
		uwDao.updateMst_reins_cancel(spaj);
		uwDao.updateMst_policy(spaj, new Double(i_lspdid), null, null);
		uwDao.updateMstPolicyLsspdId(spaj, 8);
		uwDao.updateMstInsuredBtl(spaj, i_lspdid, 12);
		uwDao.updateAktifSimasCard(spaj, 0);
		uwDao.updateMstTransUlinkCancelPolis(spaj, i_lspdid);
		uwDao.updateMstDetUlinkCancelPolis(spaj);
		if(products.unitLink(uwDao.selectBusinessId(spaj)))
			uwDao.updateMst_ulink_cancel(spaj, i_lspdid);
		uwDao.insertMstPositionSpaj(currentUser.getLus_id(), alasan, spaj, 0);
		
		/**
		 * MANTA (10-03-2015)
		 * Khusus untuk tindakan REFUND PREMI
		 * Cek field NO_PRE di EKA.MST_DREK
		 * Jika tidak null/kosong, maka masuk ke penjurnalan model baru (tidak generate jurnal refund di EKA.MST_TBANK, EKA.MST_DBANK)
		 * Jika null/kosong, maka tetap pada model penjurnalan sebelumnya
		 */
		if(refundDbVO.getTindakanCd() == 2){
			
			Double premirefund = refundDao.selectPremiRefund(spaj);
			if(premirefund == null) premirefund = 0.0;
			
			Boolean flag_jurnalbaru = false;
			List<DrekDet> mstDrekDetBasedSpaj = uwDao.selectMstDrekDet(null, spaj, null, null);
			List listmstdrek = uwDao.selectMstDrekBasedNoTrx(mstDrekDetBasedSpaj.get(0).getNo_trx());
			HashMap mstdrek = (HashMap) listmstdrek.get(0);
			if(!StringUtil.isEmpty((String) mstdrek.get("NO_PRE"))){
				if(!StringUtil.isEmpty(mstDrekDetBasedSpaj.get(0).getNo_pre())){
					if(mstDrekDetBasedSpaj.get(0).getNo_pre().equals(mstdrek.get("NO_PRE").toString())){
						flag_jurnalbaru = true;
					}
				}
			}
			
	        //Mendapatkan biaya merchant
			RefundDetDbVO refundDetDbVO = new RefundDetDbVO();
	        HashMap<String, Object> map = new HashMap<String, Object>();
	        map.put( "regSpaj", spaj );
	        map.put( "tipeNo", RefundDetDbVO.Tipe.MERCHANTFEE.tipe() );
	        map.put( "ljbId", null );
	        ArrayList<RefundDetDbVO> detailList = Common.serializableList(refundDao.selectRefundDetList( map ));
	        if(!detailList.isEmpty()) refundDetDbVO = detailList.get(0);
	        
			if(!flag_jurnalbaru){
				/*
				 * MANTA
				 * Penjurnalan Lama
				 */
				ArrayList<HashMap> noPreList = Common.serializableList(refundDao.selectNoPre(spaj));
				ArrayList<HashMap> noPreListTmp = Common.serializableList(refundDao.selectNoPreTemp(spaj));
				List<SetoranPremiDbVO> setoranPremiDb = refundDao.selectSetoranPremiBySpaj(spaj);
				if(noPreList == null || noPreList.size() == 0){
//					refundDao.deleteMstDrekDet(spaj, null, null);
//					refundDao.updateMstDrekBySpaj(spaj);
				}
				
				//Tambahan untuk SPAJ yg sudah input titipan premi atau temp premi refund
				if(noPreList.size() == 0 && premirefund != 0.0 && refundDbVO.getTindakanCd() == 2){
					
					if(noPreListTmp.size() > 0){ //Sudah input temp premi refund
						noPreList = Common.serializableList(refundDao.selectNoPreTemp(spaj));
						
					}else{ //Sudah input titipan premi tapi belum ada no pre
						PolicyInfoVO dataRefund = new PolicyInfoVO();
						dataRefund = refundDao.selectPolicyInfoBySpaj(spaj);
						dataRefund.setPremitmp(new BigDecimal(premirefund));
						
						Double kursBeli = new Double(1);
						if(dataRefund.getLkuId().equals("02")){
							try {
								kursBeli = uwDao.selectGetKursJb(datefm.parse(datefm.format(setoranPremiDb.get(0).getTglSetor())),"B");
							} catch (ParseException e) {
								logger.error(e);
							}
						}
						
						//Fungsi untuk generate no pre produksi
						
						dataRefund.setNo_trx(mstDrekDetBasedSpaj.get(0).getNo_trx());
						prosesTempPremiRefund2(dataRefund, kursBeli, currentUser.getLus_id());
						noPreList = Common.serializableList(refundDao.selectNoPreTemp(spaj));
					}
				}
				
				if(noPreList != null && noPreList.size() > 0){
					String no_pre_new = this.uwDao.selectGetPacGl("nopre");
					String no_jm_new = "";
					String voucherall = "";
					Integer flagjm = 0;
					Integer flagtm = 0;
					
					for(int i=0; i<noPreList.size(); i++){
						Boolean lb_pre = true;
						Boolean lb_jm = true;
						String no_pre = noPreList.get(i).get("MSPA_NO_PRE").toString();
						String no_voucher = refundDao.selectNoVoucher(no_pre);
						String no_jm = null;
						MstPtcTmVO selectMstPtcTm = refundDao.selectMstPtcTm(no_pre);
//						Integer position = refundDao.selectPositionMstTbank(no_pre);
//						
//						if(position.equals(1) || position.equals(2)){
//							Map<String, Object> paramsMstTbank = new HashMap<String, Object>();
//							paramsMstTbank.put("no_pre", no_pre);
//							paramsMstTbank.put("lus_id", currentUser.getLus_id());
//							refundDao.updatePositionMstTBank(paramsMstTbank);
//							lb_pre = false;
//						}
						if(selectMstPtcTm != null ){
							no_jm = selectMstPtcTm.getNo_jm();
							if(no_jm != null && !"".equals(no_jm)){
								Integer mtm_position = refundDao.selectPositionMstPtcTm(no_jm);
								if( mtm_position.equals(1) ){
									Map<String, Object> paramsMstPtcTm = new HashMap<String, Object>();
									paramsMstPtcTm.put("no_jm", no_jm);
									refundDao.updatePositionMstPtcTm(paramsMstPtcTm);
									lb_jm = false;
								}
							}
						}
						
						if(lb_pre == true ){
							if(no_pre != null && !"".equals(no_pre)){
								if(i == 0){
									refundDao.insertMstTbankJurnalBalik(no_pre_new, "0", no_pre, premirefund);
								}
								voucherall = voucherall + no_voucher + " ";
								if(i == noPreList.size()-1){
									refundDao.insertMstDbankJurnalBalik(spaj, no_pre_new, no_pre, voucherall, premirefund.intValue(), null);
								}
							}
						}
						
						if(lb_jm == true){
							if(no_jm != null && !"".equals(no_jm)){
								if(flagtm == 0){
									flagtm = 1;
									no_jm_new = this.uwDao.selectGetPacGl("nojm");
									refundDao.insertMstPtcTmJurnalBalik(no_pre_new, no_jm_new, no_jm, "0");
								}
								refundDao.insertMstPtcJmJurnalBalik(no_jm_new, no_jm, flagjm);
								flagjm = flagjm + 3;
							}
						}
					}
					
					refundDao.updateMstTempPremiRefund(spaj, no_pre_new, null, null);
					
					//MANTA - Insert ke Tabel MST_PEMBAYARAN
					Map lkuid = refundDao.selectLkuIdFromMstPolicy(spaj);
					
					if(refundDbVO.getTindakanCd() == 2){
						Map temp = uwDao.selectMstCounter(154,"01");
						Double hasil = (Double) temp.get("MSCO_VALUE");
						Integer counter = new Integer(hasil.intValue());
						refundDao.insertMstPembayaran(counter, no_pre_new, lkuid.get("LKU_ID").toString(), refundDbVO.getKliNamaBank(), refundDbVO.getKliNama(), refundDbVO.getKliCabangBank(), refundDbVO.getKliKotaBank(), refundDbVO.getKliNorek(), premirefund.intValue(), "0");
						counter = counter + 1;
						bacDao.update_counter(counter.toString(), 154, "01");
					}
					
			        Map<String, Object> params = new HashMap<String, Object>();
			        params.put("reg_spaj", spaj);
			        params.put("no_pre", no_pre_new);
			        params.put("no_jm", no_jm_new);
			        params.put("no_jm_sa", "");
			        refundDao.updateMstRefund(params);
				}

				Map<String, Object> params2 = new HashMap<String, Object>();
				params2.put("reg_spaj", spaj);
				params2.put("flag_active", 2);
				uwDao.updateKartuPasBySpaj(params2);
			
			}else{
				/*
				 * MANTA
				 * Penjurnalan Baru
				 * 03/08/2015
				 */
				
				String s_nopre = "", s_nopre_new = "", s_nojm = "", s_nojmsa = "", s_nojmsa_all = "",
					   s_nojm_refund = "", s_novoc = "", s_novoc_all = "", s_budget = "", s_lkuid = "",
					   s_year = "", s_month = "", s_year2 = "", s_month2 = "", s_ketkurs = " ",
					   s_nojmsa_first = "", s_nopre_first = "";
				String ls_ket[] = new String[4];
				Integer i_flagjm = 0;
				Integer i_flagtm = 0;
				Double d_kursbulanan = new Double(1), d_kursbulanan2 = new Double(1);
				Double d_jmlbayar = new Double(0), d_jmlrefund = new Double(0), d_merchant_fee = new Double(0);
				
				List<Payment> ls_payment = uwDao.selectMstPaymentAll(spaj);
				ArrayList ls_deposit = Common.serializableList(uwDao.selectMstDepositPremium(spaj, null));
				
				if(ls_payment.size()>0){
					for(int i=0; i<ls_payment.size(); i++){
						Payment payment = ls_payment.get(i);
						s_lkuid = payment.getLku_id();
						
						DrekDet drekdet = new DrekDet();
						for(int j=0; j<mstDrekDetBasedSpaj.size(); j++){
							if(mstDrekDetBasedSpaj.get(j).getPayment_id().equals(payment.getMspa_payment_id())){
								drekdet = mstDrekDetBasedSpaj.get(j);
							}
						}
						s_nopre = payment.getMspa_no_pre();
						s_novoc = refundDao.selectNoVoucher(s_nopre);
						s_nojm = payment.getMspa_no_jm();
						s_nojmsa = payment.getMspa_no_jm_sa();
						
						if(s_lkuid.equals("01")){
							s_budget = "41111";
						}else if(s_lkuid.equals("02")){
							s_budget = "41112";
							s_year = datestr.format(drekdet.getTgl_trx()).substring(6,datestr.format(drekdet.getTgl_trx()).length());
							s_month = datestr.format(drekdet.getTgl_trx()).substring(3,5);
							d_kursbulanan = uwDao.selectLstMonthlyKursLmkNilai(s_year, s_month, payment.getLku_id());
							s_ketkurs = " US $ " + nf.format(payment.getMspa_payment()) + " " + d_kursbulanan + " ";
						}
						d_jmlbayar = payment.getMspa_payment() * d_kursbulanan;
						
						ls_ket[0] = "TITIPAN TIDAK DIKETAHUI " + datestr.format(drekdet.getTgl_trx()) + s_ketkurs + drekdet.getNo_trx() + " " + s_novoc;
						ls_ket[1] = "TITIPAN PREMI NO. SPAJ " + spaj.trim();
						
						//Jika Jurnal Suspend Account tidak ada,
						//Maka buat Jurnal Suspend Account terlebih dahulu
						if(StringUtil.isEmpty(s_nojmsa)){
							ArrayList mstbvoucher = Common.serializableList(refundDao.selectMstBvoucher(s_nopre, 2));
							HashMap bvoucher = (HashMap) mstbvoucher.get(0);
							
							s_nojmsa = this.uwDao.selectGetPacGl("nojm");
							uwDao.insertMst_ptc_tm(s_nojmsa, 1, nowDate, s_nopre, "0");
							uwDao.insertMst_ptc_jm(s_nojmsa, 1, ls_ket[0], d_jmlbayar, (String) bvoucher.get("PROJECT_NO"), (String) bvoucher.get("BUDGET_NO"), "D", spaj);
							uwDao.insertMst_ptc_jm(s_nojmsa, 2, ls_ket[1], d_jmlbayar, (String) bvoucher.get("PROJECT_NO"), s_budget, "C", spaj);
							Premi premi = new Premi();
							premi.setVoucher(s_novoc);
							uwDao.updateMst_paymentJurnal(premi, s_nopre, payment.getMspa_payment_id(), s_nojm, s_nojmsa);
							uwDao.updateNoPreMstDrekDet(null, spaj, payment.getMspa_payment_id(), s_nopre, s_nojmsa);
						}
						
						//Jika Jurnal Produksi ada,
						//Maka buat jurnal baliknya
						if(!StringUtil.isEmpty(s_nojm)){
							if(i_flagtm == 0){
								i_flagtm = 1;
								s_nojm_refund = this.uwDao.selectGetPacGl("nojm");
								refundDao.insertMstPtcTmJurnalBalik(null, s_nojm_refund, s_nojm, "0");
								refundDao.insertMstPtcJmJurnalBalik(s_nojm_refund, s_nojm, i_flagjm);
							}
						}
						
						if(i==0){
							s_nojmsa_first = s_nojmsa;
							s_nopre_first = s_nopre;
						}
						if(!s_nojmsa_all.contains(s_nojmsa)) s_nojmsa_all = s_nojmsa_all + s_nojmsa + " ";
						if(!s_novoc_all.contains(s_novoc)) s_novoc_all = s_novoc_all + s_novoc + " ";
					}
					
				}else if(ls_deposit.size()>0){
					for(int i=0; i<ls_deposit.size(); i++){
						Date d_tglrk = (Date) mstdrek.get("TGL_TRX");
						s_lkuid = (String) mstdrek.get("LKU_ID");
						DepositPremium deposit = (DepositPremium) ls_deposit.get(i);
						Double jmlbayar = deposit.getMsdp_payment().doubleValue();
						
						DrekDet drekdet = new DrekDet();
						for(int j=0; j<mstDrekDetBasedSpaj.size(); j++){
							if(deposit.getMsdp_desc().contains(mstDrekDetBasedSpaj.get(j).getNo_trx()) && 
							   mstDrekDetBasedSpaj.get(j).getJumlah().equals(jmlbayar)){
								drekdet = mstDrekDetBasedSpaj.get(j);
							}
						}
						
						s_nopre = deposit.getMsdp_no_pre();
						s_novoc = refundDao.selectNoVoucher(s_nopre);
						
						//Buat Jurnal Suspend Account terlebih dahulu
						Pemegang pemegang = bacDao.selectpp(spaj);
						ArrayList mstbvoucher = Common.serializableList(refundDao.selectMstBvoucher(s_nopre, 2));
						HashMap bvoucher = (HashMap) mstbvoucher.get(0);
						if(s_lkuid.equals("01")){
							s_budget = "41111";
						}else if(s_lkuid.equals("02")){
							s_budget = "41112";
							s_year = datestr.format(drekdet.getTgl_trx()).substring(6,datestr.format(drekdet.getTgl_trx()).length());
							s_month = datestr.format(drekdet.getTgl_trx()).substring(3,5);
							d_kursbulanan = uwDao.selectLstMonthlyKursLmkNilai(s_year, s_month, deposit.getLku_id());
							s_ketkurs = " US $ " + nf.format(deposit.getMsdp_payment()) + " " + d_kursbulanan + " ";
						}
						d_jmlbayar = deposit.getMsdp_payment() * d_kursbulanan;
						
						ls_ket[0] = "TITIPAN TIDAK DIKETAHUI " + datestr.format(drekdet.getTgl_trx()) + s_ketkurs + drekdet.getNo_trx() + " " + s_novoc;
						ls_ket[1] = "TITIPAN PREMI NO. SPAJ " + spaj.trim();
						
						//Generate Jurnal Suspend Account
						s_nojmsa = this.uwDao.selectGetPacGl("nojm");
						uwDao.insertMst_ptc_tm(s_nojmsa, 1, nowDate, s_nopre, "0");
						uwDao.insertMst_ptc_jm(s_nojmsa, 1, ls_ket[0], d_jmlbayar, (String) bvoucher.get("PROJECT_NO"), (String) bvoucher.get("BUDGET_NO"), "D", spaj);
						uwDao.insertMst_ptc_jm(s_nojmsa, 2, ls_ket[1], d_jmlbayar, (String) bvoucher.get("PROJECT_NO"), s_budget, "C", spaj);

						uwDao.updateNoPreMstDrekDetFromDepositPremium(spaj, null, s_nopre, s_nojmsa, new Long(deposit.getMsdp_number()));
						
						if(i==0){
							s_nojmsa_first = s_nojmsa;
							s_nopre_first = s_nopre;
						}
						if(!s_novoc_all.contains(s_novoc)) s_novoc_all = s_novoc_all + s_novoc + " ";
					}
				}
				
				if(s_lkuid.equals("02")){
					s_year2 = datestr.format(nowDate).substring(6,datestr.format(nowDate).length());
					s_month2 = datestr.format(nowDate).substring(3,5);
					d_kursbulanan2 = uwDao.selectLstMonthlyKursLmkNilai(s_year2, s_month2, s_lkuid);
				}
				d_jmlrefund = premirefund * d_kursbulanan2;
				if(refundDetDbVO.getJumlah() != null) d_merchant_fee = refundDetDbVO.getJumlah().doubleValue() * d_kursbulanan;
				
				//Jurnal Batal Suspend Account
				ls_ket[2] = "REFUND PREMI " + refundDbVO.getKliNama() + " " + spaj.trim() + " " + s_novoc_all;
				s_nopre_new = this.uwDao.selectGetPacGl("nopre");
				refundDao.insertMstTbankJurnalBalik(s_nopre_new, "0", s_nopre_first, d_jmlrefund);
				refundDao.insertMstDbankJurnalBalikSA(spaj, s_nopre_first, s_nopre_new, s_nojmsa_first, ls_ket[2], d_jmlrefund, d_merchant_fee);
				
		        Map<String, Object> params = new HashMap<String, Object>();
		        params.put("reg_spaj", spaj);
		        params.put("no_pre", s_nopre_new);
		        params.put("no_jm", s_nojm_refund);
		        params.put("no_jm_sa", s_nojmsa);
		        refundDao.updateMstRefund(params);
			}
		
		//Tindakan Ganti Tertanggung dan Ganti Plan
		}else if(refundDbVO.getTindakanCd() == 3 || refundDbVO.getTindakanCd() == 4){
			List<Payment> ls_payment = uwDao.selectMstPaymentAll(spaj);
			String s_nojmsa = "", s_nojmsa_new = "", s_nojm = "", s_nojm_new = "";
			Boolean flag_jbjm = true;
			
			if(ls_payment.size()>0){
				for(int i=0; i<ls_payment.size(); i++){
					Payment payment = ls_payment.get(i);
					s_nojmsa = payment.getMspa_no_jm_sa();
					s_nojm = payment.getMspa_no_jm();
					String s_nopre = payment.getMspa_no_pre();
					
					if(!StringUtil.isEmpty(s_nojmsa)){
						s_nojmsa_new = this.uwDao.selectGetPacGl("nojm");
						refundDao.insertMstPtcTmJurnalBalik(s_nopre, s_nojmsa_new, s_nojmsa, "0");
						refundDao.insertMstPtcJmSaJurnalBalik(s_nojmsa_new, s_nojmsa);

						//MANTA (01/05/2016) - Buat drekdet pembatal
						refundDao.insertMstDrekDetBatal(spaj, payment.getMspa_payment_id(), s_nojmsa_new);
					}
					
					if(!StringUtil.isEmpty(s_nojm) && flag_jbjm){
						s_nojm_new = this.uwDao.selectGetPacGl("nojm");
						refundDao.insertMstPtcTmJurnalBalik(s_nopre, s_nojm_new, s_nojm, "0");
						refundDao.insertMstPtcJmJurnalBalik(s_nojm_new, s_nojm, 0);
						flag_jbjm = false;
					}
				}
			}else{
				refundDao.deleteMstDrekDet(spaj, null, null);
				refundDao.updateMstDrekBySpaj(spaj);
			}

			//MANTA - Memindahkan No VA dari SPAJ lama ke SPAJ baru
			HashMap dataVaBaru = bacDao.select_det_va_spaj(refundDbVO.getSpajBaruNo());
			HashMap dataVaLama = bacDao.select_det_va_spaj(spaj);
			if(dataVaLama != null){
				if(dataVaBaru != null){
					refundDao.updateNoVaBatalPolis("", (String) dataVaBaru.get("NO_VA"));
					refundDao.updateMsteNoVacc(spaj, (String) dataVaBaru.get("NO_VA"));
				}else{
					refundDao.updateMsteNoVacc(spaj, "");
				}
				refundDao.updateNoVaBatalPolis(refundDbVO.getSpajBaruNo(), (String) dataVaLama.get("NO_VA"));
				refundDao.updateMsteNoVacc(refundDbVO.getSpajBaruNo(), (String) dataVaLama.get("NO_VA"));
			}
			
	        Map<String, Object> params = new HashMap<String, Object>();
	        params.put("reg_spaj", spaj);
	        params.put("no_pre", null);
	        params.put("no_jm", s_nojm_new);
	        params.put("no_jm_sa", s_nojmsa_new);
	        refundDao.updateMstRefund(params);
			
		}else{
			ArrayList<HashMap> noJmSaList = Common.serializableList(refundDao.selectNoJmSa(spaj));
			if(noJmSaList.size()==0){
				refundDao.deleteMstDrekDet(spaj, null, null);
				refundDao.updateMstDrekBySpaj(spaj);
			}
		}
	}
	
	/**
	 * MANTA
	 * Fungsi untuk membuat No Pre Masuk dan menyimpannya ke table eka.mst_temp_premi_refund
	 */
	public void prosesTempPremiRefund2(PolicyInfoVO dataRefund, Double nilaikurs, String lus_id) {
		DateFormat stripfr = new SimpleDateFormat("dd-MM-yyyy");
		Date now = commonDao.selectSysdateTruncated(0);
		String nopre = "", nojm = "", rk_simbol = "", profitcenter = "", profitcenter2 = "";
		String ls_ket[] = new String[6];
		String ls_acc[] = new String[2];
		String nospaj = dataRefund.getSpajNo();
		Double jmlsetor = dataRefund.getPremitmp().doubleValue();
		Long rk_nocr = null;
		Premi premi = new Premi();
		HashMap<Object, Object> tmp = new HashMap<Object, Object>();
		
		List listmstdrek = uwDao.selectMstDrekBasedNoTrx(dataRefund.getNo_trx());
		HashMap mstdrek = (HashMap) listmstdrek.get(0);
		
		List listrekekalife = uwDao.selectBankEkaLife(mstdrek.get("NOREK_AJS").toString());
		HashMap rekekalife = (HashMap) listrekekalife.get(0);
		Integer lsrek_id = ((BigDecimal) rekekalife.get("LSREK_ID")).intValue();
		
		//Ambil data rekening AJS
		tmp = Common.serializableMap(this.uwDao.selectCounterRekEkalife(lsrek_id));
		premi.setRek_id(lsrek_id);
		if(props.getProperty("product.syariah").equals(dataRefund.getLsbsId())){
			profitcenter = "801";
			if(dataRefund.getNamaProduk().equals("POWER SAVE SYARIAH BSM")){
				ls_acc[0] = "851";
			}else{
				ls_acc[0] = "801";
			}
		}else{
			profitcenter = "001";
			ls_acc[0] = "0" + nospaj.substring(0,2);
		}
		
		//Generate No Voucher
		rk_simbol = (String) tmp.get("LSREK_SYMBOL");
		rk_nocr = new Long(((BigDecimal) tmp.get("LSREK_NO_CR")).longValue());
		if(rk_simbol == null) rk_simbol = "";
		if(rk_nocr == null) rk_nocr = new Long(0);
		
		rk_nocr = this.accountingDao.selectNewCrNo("");		
		//premi.setNo_cr(new Long(rk_nocr.longValue()+1));
		premi.setNo_cr(rk_nocr);		
		
		premi.setAccno((String) tmp.get("LSREK_GL_NO"));
		premi.setVoucher(rk_simbol.trim() + FormatString.rpad("0", premi.getNo_cr().toString(), 5) + "R");
			
		//Generate No Pre
		nopre = this.uwDao.selectGetPacGl("nopre");

		//Generate No GM
//		nojm = this.uwDao.selectGetPacGl("nojm");
			
		//Proses input jurnal
		this.uwDao.insertMst_tbank(nopre, 2, now, (Date) mstdrek.get("TGL_TRX"), premi, "M", jmlsetor, "0");
		
		if(dataRefund.getPolicyNo() == null) dataRefund.setPolicyNo(nospaj);
		
		ls_ket[0] = "BANK " + dataRefund.getNamaPp().trim() + " " + stripfr.format(now) + " BOOKING";
		ls_ket[1] = "TITIPAN PREMI NO. SPAJ " + nospaj.trim();
		ls_ket[2] = "BK " + dataRefund.getNamaPp().trim() + " " + stripfr.format(now);
		ls_ket[3] = "TP " + nospaj.trim() + " " + stripfr.format(now);
		ls_ket[4] = "TP " + dataRefund.getPolicyNo().trim() + " " + premi.getVoucher();
		ls_ket[5] = "PP " + dataRefund.getPolicyNo().trim() + " " + premi.getVoucher();
		
		int lscb = uwDao.selectCaraBayarFromSpaj(nospaj);
		if(lscb == 0){
			ls_acc[1] = bacDao.selectGetAccPremi(nospaj, 1);
		}else{
			ls_acc[1] = bacDao.selectGetAccPremi(nospaj, 3);
		}
		profitcenter2 = premi.getAccno().substring(0,3);
		premi.setProject(new String[]{profitcenter2, profitcenter});
		premi.setBudget(new String[]{premi.getAccno().substring(3), "41111"});
		
		this.refundDao.insertMstTempPremiRefund(nospaj, 0, 1, dataRefund.getKliNoRek(), dataRefund.getKliAtasNama(), dataRefund.getKliNamaBank(),
				dataRefund.getKliCabangBank(), dataRefund.getKliKotaBank(), jmlsetor, dataRefund.getLkuId(), nopre, premi.getVoucher(), nilaikurs, lus_id, null, null, null);
		
		this.uwDao.insertMst_dbank(nopre, 1, ls_ket[0], "M", jmlsetor, null, nospaj.trim());
		this.uwDao.insertMst_dbank(nopre, 2, ls_ket[1], "B", jmlsetor, new Integer(1), nospaj.trim());
		
		this.uwDao.insertMst_bvoucher(nopre, 1, ls_ket[2], jmlsetor, 0.0, premi.getProject()[0], premi.getBudget()[0], nospaj.trim());
		this.uwDao.insertMst_bvoucher(nopre, 2, ls_ket[3], 0.0, jmlsetor, premi.getProject()[1], premi.getBudget()[1], nospaj.trim());
		
//		this.uwDao.insertMst_ptc_tm(nojm, 1, now, nopre, lus_id);
//		this.uwDao.insertMst_ptc_jm(nojm, 1, ls_ket[4], jmlsetor, premi.getProject()[1], premi.getBudget()[1], "D", nospaj);
//		this.uwDao.insertMst_ptc_jm(nojm, 2, ls_ket[5], jmlsetor, ls_acc[0], ls_acc[1], "C", nospaj);
		
		uwDao.updateNoPreMstDrekDetFromDepositPremium(nospaj, null, nopre, null, new Long(1));
		
		//this.uwDao.updateLst_rek_ekalife(premi);

	}
	
}