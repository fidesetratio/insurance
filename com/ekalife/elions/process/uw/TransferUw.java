/**TransferUw.java
 * @author  : Ferry Harlim
 * @created : Nov 12, 2007 1:59:09 PM
 */
package com.ekalife.elions.process.uw;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;

import com.ekalife.elions.model.DepositPremium;
import com.ekalife.elions.model.Insured;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.Premi;
import com.ekalife.elions.model.Transfer;
import com.ekalife.elions.model.User;
import com.ekalife.utils.ExternalDatabase;
import com.ekalife.utils.parent.ParentDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TransferUw extends ParentDao {
	
	/**@Fungsi: Untuk melakukan proses transfer ke Payment atau printpolis dimana pada method ini
	 * 			di atur proses transfer berdasarkan kriteria produk dan sesuai dengan prosedur dari
	 * 			produk itu masing-masing, khusus produk worksite (149) maka ada di insert ke database
	 * 			dplk8i dengan tabel DPLK.DPLKWORKSITE	
	 *@param 	Transfer transfer,BindException err]
	 *			Integer FlagNew (0=old, 1=New) proses pembayaran baru atao lama.
	 *@returns 	int-proses 
	 *  		proses=1=berhasil transfer ke pembayaran
	 *			proses=2=berhasil transfer ke print polis
	 *			proses=3=transfer ke print polis dan tikdak melakukan proses billing
	 *@author 	Ferry Harlim
	 * @throws ParseException 
	 * */
	protected final Log logger = LogFactory.getLog( getClass() );
	
	public Map prosesTransferPembayaran(Transfer transfer,Integer flagNew,BindException err,HttpServletRequest request)throws DataAccessException, ParseException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String spaj,lusId,lkuId;
		Integer insuredNo,umurTt,liReas,medical,insPeriod;
		Integer lsbsId,lsdbsNumber,lstbId,lspdId,lssaId,liLama;
		Date begdate,endDate;
		List lsDp;
		int liPosisi;
		Policy policy;
		User currentUser;
		spaj=transfer.getSpaj();
		insuredNo=transfer.getInsuredNo();
		lusId=transfer.getCurrentUser().getLus_id();
		lsbsId=transfer.getLsbsId();
		lsdbsNumber=transfer.getLsdbsNumber();
		lstbId=transfer.getLstbId();
		lspdId=transfer.getLspdId();
		lssaId=transfer.getLiAksep();
		currentUser=transfer.getCurrentUser();
		liLama=transfer.getLiLama();
		lkuId=transfer.getLkuId();
		umurTt=transfer.getUmurTt();
		liReas=transfer.getLiReas();
		begdate=transfer.getBegDate();
		endDate=transfer.getEndDate();
		medical=transfer.getMedical();
		insPeriod=transfer.getInsPeriod();
		lsDp=transfer.getLsDp();
		liPosisi=transfer.getLiPosisi();
		policy=transfer.getPolicy();
		Map mPemegang =uwDao.selectPemegang(spaj);
		String namaPemegang=(String)mPemegang.get("MCL_FIRST");
		Map allResult = new HashMap();
		String pesanTambahan = "";
		
		int proses=0;
		
		boolean lbLangsung=false;
		//wf_set_posisi
		if (lsbsId==196 && lsdbsNumber==2){
			liPosisi=6;
			lspdId=6;
			pesanTambahan="Proses Transfer Print Polis Berhasil ";
			uwDao.updateMstInsured(spaj,new Integer(liPosisi),lssaId,insuredNo, null);
			uwDao.insertMstPositionSpaj(lusId,"TRANSFER DARI U/W KE PRINT POLIS", spaj, 0);
			uwDao.updateMstPolicyPosition(spaj,new Integer(liPosisi),lstbId);
			uwDao.prosesSnows(spaj, currentUser.getLus_id(), 211, 212);
		}else{
			uwDao.updateMstInsured(spaj,new Integer(liPosisi),lssaId,insuredNo, null);
			uwDao.insertMstPositionSpaj(lusId,"TRANSFER DARI U/W KE PAYMENT", spaj, 0);
			uwDao.updateMstPolicyPosition(spaj,new Integer(liPosisi),lstbId);
			uwDao.prosesSnows(spaj, currentUser.getLus_id(), 211, 212);
		}
		//wf_save_reins()
		boolean prosesReins;
		if(flagNew==1){//new
			prosesReins=uwDao.wf_save_reinsNew(spaj,insuredNo,umurTt,liReas,lkuId,begdate,endDate,medical,lusId,insPeriod,err);
		}else{//old
			prosesReins=uwDao.wf_save_reins(spaj,insuredNo,umurTt,liReas,lkuId,begdate,endDate,medical,lusId,insPeriod,err);
		}
		if(prosesReins){
			//endowment insert billing sementara (belum bayar dan selanjutna langsung transfer ke print polis
			if(liLama==0){
				if(uwDao.wf_ins_bill(spaj,insuredNo,new Integer(1),lsbsId,lsdbsNumber,lspdId,lstbId,lsDp,lusId,policy,err)){
					//updateMstPolicyStatus(spaj,new Integer(4),lstbId);
					Boolean flagPre = true;
					Boolean health = true;
//					String result = "";
					lsDp = uwDao.selectMstDepositPremium(transfer.getSpaj(), null);
					if( lsbsId==157 || (lsbsId==196 && lsdbsNumber==2) ){
						proses = 3;
						
					}else{
						proses=1;
						if(lsDp.size()>0){
							if(products.unitLink(lsbsId.toString())) {//produk unitlink, termasuk stable link
								//kalo stable link, tetep dijurnal secara unit link
								if(!uwDao.getJurnalBacUlink(spaj,lusId,lsbsId,namaPemegang,err,request)){
									flagPre = false;
									pesanTambahan = "Gagal Jurnal Ulink.";
									health = false;
									TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
									proses = 0;
								}
//								err.reject("","Jurnal Ulink.");
							}else{//bukan unit link
								if(!uwDao.getJurnalBacIndividu(spaj,lusId,lsbsId,namaPemegang,err,request)){
									flagPre = false;
									pesanTambahan = "Gagal Jurnal Individu.";
									health = false;
									TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
									proses = 0;
								}
								pesanTambahan = "Jurnal Individu";
							}
						}else{
							flagPre = false;
						}
					}
					lsDp = uwDao.selectMstDepositPremium(transfer.getSpaj(),null);//dijalankan ulang untuk meretrieve no Pre dan voucher yang sudah diset setelah jurnal.
					String nomorPre[] = new String[lsDp.size()+1];
					String pre = "";
					for(int i=0;i<lsDp.size();i++){
						DepositPremium depPre = (DepositPremium)lsDp.get(i);
						nomorPre[i] = depPre.getMsdp_no_pre();
						if(lsDp.size()>0){
							pre = nomorPre[i]+", "+pre;
						}else{
							pre = nomorPre[i];
						}
					}
					
					if(flagPre){
						pesanTambahan = "No Pre =" + pre;
					}
						
					Integer liPaid, liTopup;
					liPaid = uwDao.selectMstBillingMsbiPaid(spaj,new Integer(1),new Integer(1));
					if(liPaid!=null){
						if(liPaid==1){
							lbLangsung = true;
							if(products.stableLink(lsbsId.toString())) {
								//MANTA - Cek apakah ada topupnya atau tidak, jika tidak ada topup langsung ke Print polis
								Powersave dt = (Powersave) this.bacDao.select_slink_topup(spaj);
								if(dt!=null){
									lbLangsung = false; //harus bayar topup dulu untuk stable link
								}
								
							//ulink
							}else if(products.unitLink(lsbsId.toString())) {
								liTopup = uwDao.selectMstUlinkMuPeriodicTu(spaj,new Integer(1));
								if(liTopup!=null){
									if(liTopup>=1) lbLangsung = false;
								}
							}
							if(lbLangsung){
								//Tampilan Untuk langsung transfer ke print polis
								proses = 2;
							}
						}
					}
				}else{
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				}
				
			}else if(liLama==1 && liPosisi==6){
				proses=3;
				//Yusuf (04/01/2010) - Untuk semua update status polis, dipindah saat insert production, agar tidak membingungkan
				//Yusuf (20/08/2010) - Enable lagi, karena ini update status untuk produk endorse
				uwDao.updateMstPolicyLsspdId(spaj,new Integer(1));
			}
		}else{
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		
		//YUSUF (21/09/2006) - REQUEST TANWER, UNTUK INSERT DANA DPLK KE TABEL DPLK.DPLKWORKSITE
		if(lsbsId==149){//HORISON
			Map map = uwDao.selectPersentaseDplk(spaj);
			Double persen = (Double) map.get("mste_pct_dplk");
			
			if(persen==0) { //kalo dia 0 atau null, error
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				proses=5;
			}else {
				Double dplk = (Double) map.get("biaya_dplk");
				Date tgl_rk = (Date) uwDao.selectMaxRkDateFromTitipanPremi(spaj);
				ExternalDatabase update = new ExternalDatabase("dplk8i");
				update.doUpdate(
						"INSERT INTO dplk.dplkworksite (regspaj, tglrk, iuranke, nopolis, netfund, posdoc, flagt, tgltrans) "+
						"VALUES (?, ?, ?, ?, ?, ?, ?, sysdate)", 
						new Object[] {
								spaj, tgl_rk,
								1, policy.getMspo_policy_no(),
								dplk, 1, 0});
			}
		}
		
		allResult.put("proses", proses);
		allResult.put("pesanTambahan", pesanTambahan);
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return allResult;	
	}

	public void  prosesinsertLossMstReinsProduct(List lsSpaj,User currentUser,BindException err) throws DataAccessException, ParseException{
		for(int i=0;i<lsSpaj.size();i++){	
			String spaj=(String)lsSpaj.get(i);
			Policy policy=uwDao.selectDw1Underwriting(spaj,null,1);
//			Integer liReas=1 ;// 0=Non reas , 1=treaty , 2=fakultatif
			Integer liReas=uwDao.selectFlagReas(spaj) ;
//			data usulan asuransi
			Map mDataUsulan=uwDao.selectDataUsulan(spaj);
			String lkuId=(String)mDataUsulan.get("LKU_ID");
			Date begDate=(Date)mDataUsulan.get("MSTE_BEG_DATE");
			Date endDate=(Date)mDataUsulan.get("MSTE_END_DATE");
			Integer medical=(Integer)mDataUsulan.get("MSTE_MEDICAL");
			//tertanggung
			Map mTertanggung=uwDao.selectTertanggung(spaj);
			Integer insuredNo=((Integer)mTertanggung.get("MSTE_INSURED_NO"));
			Integer umurTt=(Integer)mTertanggung.get("MSTE_AGE");
			uwDao.wf_save_reinsNew(spaj,insuredNo,umurTt,liReas,lkuId,begDate,endDate,medical,currentUser.getLus_id(),policy.getMspo_ins_period(),err);
		}	
		logger.info(lsSpaj.size());
		
	}
	
	public Map prosesTransferPembayaranNewFlow(Transfer transfer,Integer flagNew,BindException err,HttpServletRequest request, Integer flow, Integer flag)throws DataAccessException, ParseException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String spaj,lusId,lkuId, lsCab, lsKursId, lsPayId, lsWakil, lsRegion, lsKursPolis;
		Integer insuredNo,umurTt,liReas,medical,insPeriod,error = 0;
		Integer lsbsId,lsdbsNumber,lstbId,lspdId,lssaId,liLama;
		Double ldecKurs = new Double(1);
		Date begdate,endDate,ldt_tgl_book;
		Date ldt_tgl_debet = null;
		List lsDp;
		int liPosisi;
		Policy policy;
		User currentUser;
		spaj=transfer.getSpaj();
		insuredNo=transfer.getInsuredNo();
		lusId=transfer.getCurrentUser().getLus_id();
		lsbsId=transfer.getLsbsId();
		lsdbsNumber=transfer.getLsdbsNumber();
		lstbId=transfer.getLstbId();
		lspdId=transfer.getLspdId();
		lssaId=transfer.getLiAksep();
		currentUser=transfer.getCurrentUser();
		liLama=transfer.getLiLama();
		lkuId=transfer.getLkuId();
		umurTt=transfer.getUmurTt();
		liReas=transfer.getLiReas();
		begdate=transfer.getBegDate();
		endDate=transfer.getEndDate();
		medical=transfer.getMedical();
		insPeriod=transfer.getInsPeriod();
		lsDp=transfer.getLsDp();
		liPosisi=transfer.getLiPosisi();
		policy=transfer.getPolicy();
		Map mPemegang =uwDao.selectPemegang(spaj);
		Map mTertanggung =uwDao.selectTertanggung(spaj);
		Insured ins = uwDao.selectMstInsuredAll(spaj);
		String namaPemegang=(String)mPemegang.get("MCL_FIRST");
		Map allResult = new HashMap();
		String pesanTambahan = "";
		String pesanErorr = "";

		int proses=0;
		if(flow==1)// NewBusiness
		{
			boolean prosesReins;
			if(flagNew==1){//new
				prosesReins=uwDao.wf_save_reinsNew(spaj,insuredNo,umurTt,liReas,lkuId,begdate,endDate,medical,lusId,insPeriod,err);
			}else{//old
				prosesReins=uwDao.wf_save_reins(spaj,insuredNo,umurTt,liReas,lkuId,begdate,endDate,medical,lusId,insPeriod,err);
			}
		}else if (flow==2)//collection
		{
			boolean lbLangsung=false;
			if(liLama==0){
				if(uwDao.wf_ins_bill(spaj,insuredNo,new Integer(1),lsbsId,lsdbsNumber,lspdId,lstbId,lsDp,lusId,policy,err)){
					//updateMstPolicyStatus(spaj,new Integer(4),lstbId);
					Boolean flagPre = true;
					Boolean health = true;
					//					String result = "";
					lsDp = uwDao.selectMstDepositPremium(transfer.getSpaj(), null);
					if( lsbsId==157 || (lsbsId==196 && lsdbsNumber==2) ){
						proses = 3;

					}else{
						proses=1;
						if(lsDp.size()>0){
							if(products.unitLink(lsbsId.toString())) {//produk unitlink, termasuk stable link
								//kalo stable link, tetep dijurnal secara unit link
								if(!uwDao.getJurnalBacUlink(spaj,lusId,lsbsId,namaPemegang,err,request)){
									flagPre = false;
									pesanErorr = "Gagal Jurnal Ulink.";
									health = false;
									TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
									proses = 0;
								}
								//								err.reject("","Jurnal Ulink.");
							}else{//bukan unit link
								if(!uwDao.getJurnalBacIndividu(spaj,lusId,lsbsId,namaPemegang,err,request)){
									flagPre = false;
									pesanErorr = "Gagal Jurnal Individu.";
									health = false;
									TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
									proses = 0;
								}
								//pesanTambahan = "Jurnal Individu";
							}
						}else{
							flagPre = false;
						}
					}
					lsDp = uwDao.selectMstDepositPremium(transfer.getSpaj(),null);//dijalankan ulang untuk meretrieve no Pre dan voucher yang sudah diset setelah jurnal.
					String nomorPre[] = new String[lsDp.size()+1];
					String pre = "";
					for(int i=0;i<lsDp.size();i++){
						DepositPremium depPre = (DepositPremium)lsDp.get(i);
						nomorPre[i] = depPre.getMsdp_no_pre();
						if(lsDp.size()>0){
							pre = nomorPre[i]+", "+pre;
						}else{
							pre = nomorPre[i];
						}
					}

					if(flagPre){
						pesanTambahan = "No Pre =" + pre;
					}

					Integer liPaid, liTopup;
					liPaid = uwDao.selectMstBillingMsbiPaid(spaj,new Integer(1),new Integer(1));
					if(liPaid!=null){
						if(liPaid==1){
							lbLangsung = true;
							if(products.stableLink(lsbsId.toString())) {
								//MANTA - Cek apakah ada topupnya atau tidak, jika tidak ada topup langsung ke Print polis
								Powersave dt = (Powersave) this.bacDao.select_slink_topup(spaj);
								if(dt!=null){
									lbLangsung = false; //harus bayar topup dulu untuk stable link
								}

								//ulink
							}else if(products.unitLink(lsbsId.toString())) {
								liTopup = uwDao.selectMstUlinkMuPeriodicTu(spaj,new Integer(1));
								if(liTopup!=null){
									if(liTopup>=1) lbLangsung = false;
								}
							}
							if(lbLangsung){
								//Tampilan Untuk langsung transfer ke print polis
								proses = 2;
							}
						}
					}
				}else{
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				}
				
				//cek Billing yang diinput Collection, Ada kelebihan atau kekurangan 
				List billInfo = this.uwDao.selectBillingInfoForTransfer(spaj, 1, 1);
				Map mapBill = (HashMap) billInfo.get(0);
				Integer error_bill = 0;
				//cek ada selisih kurs/ kelebihan bayar
				Integer paid=(Integer)mapBill.get("MSBI_PAID");
				if(paid==0){
					if(Double.parseDouble(mapBill.get("MSBI_REMAIN").toString())>0){//masih kurang bayar
						pesanErorr="<br>- Ada Selisih Kurs( Kurang Bayar) Sebesar  " + Double.parseDouble(mapBill.get("MSBI_REMAIN").toString());
					}else if(Double.parseDouble(mapBill.get("MSBI_REMAIN").toString())<0){//kelebihan bayar
						pesanErorr="<br>- Ada Selisih Kurs( Lebih Bayar) Sebesar  "+ Double.parseDouble(mapBill.get("MSBI_REMAIN").toString());
					}
				}
				//cek Untuk TOP-UP
				Integer countBilling = uwDao.selectCountMstBillingNB(spaj);
				if(countBilling>1){
					for(int i=2;i<=countBilling;i++){
						List billInfoTopUp = this.uwDao.selectBillingInfoForTransfer(spaj, 1, i);
						Map mapTopup = (HashMap) billInfoTopUp.get(0);
						Integer paidTopUP=(Integer)mapTopup.get("MSBI_PAID");
						if(paidTopUP==0){
							if(Double.parseDouble(mapTopup.get("MSBI_REMAIN").toString())>0){//masih kurang bayar
								pesanErorr="<br>- Ada kekurangan pembayaran TopUp sebesar "+mapTopup.get("LKU_SYMBOL")+mapTopup.get("MSBI_REMAIN");
							}else if(Double.parseDouble(mapTopup.get("MSBI_REMAIN").toString())<0){//kelebihan bayar
								pesanErorr="<br>- Ada kelebihan pembayaran TopUp sebesar "+mapTopup.get("LKU_SYMBOL")+mapTopup.get("MSBI_REMAIN");
							}
						}
					}
				}
			}else if(liLama==1 && liPosisi==6){
				proses=3;
				//Yusuf (04/01/2010) - Untuk semua update status polis, dipindah saat insert production, agar tidak membingungkan
				//Yusuf (20/08/2010) - Enable lagi, karena ini update status untuk produk endorse
				uwDao.updateMstPolicyLsspdId(spaj,new Integer(1));
			}
			//YUSUF (21/09/2006) - REQUEST TANWER, UNTUK INSERT DANA DPLK KE TABEL DPLK.DPLKWORKSITE
			if(lsbsId==149){//HORISON
				Map map = uwDao.selectPersentaseDplk(spaj);
				Double persen = (Double) map.get("mste_pct_dplk");

				if(persen==0) { //kalo dia 0 atau null, error
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					proses=5;
				}else {
					Double dplk = (Double) map.get("biaya_dplk");
					Date tgl_rk = (Date) uwDao.selectMaxRkDateFromTitipanPremi(spaj);
					ExternalDatabase update = new ExternalDatabase("dplk8i");
					update.doUpdate(
							"INSERT INTO dplk.dplkworksite (regspaj, tglrk, iuranke, nopolis, netfund, posdoc, flagt, tgltrans) "+
									"VALUES (?, ?, ?, ?, ?, ?, ?, sysdate)", 
									new Object[] {
									spaj, tgl_rk,
									1, policy.getMspo_policy_no(),
									dplk, 1, 0});
				}
			}
		}else if (flow==3)// Flow Jika salah 1 Pending
		{
			if(flag==1){
				Integer flag_speedy = ins.getFlag_speedy();
				if(lspdId==27){//dari posisi SPEEDY - NB&COLL belum approve salah satu
					if(flag_speedy==1)
					{	
						uwDao.updateMstInsured(spaj,new Integer(251),lssaId,insuredNo, null);
						uwDao.insertMstPositionSpaj(lusId,"TRANSFER KE WAITING PROSES NB", spaj, 1);
						uwDao.updateMstPolicyPosition(spaj,new Integer(251),lstbId);
						uwDao.prosesSnows(spaj, currentUser.getLus_id(), 251, 212);
						pesanTambahan="Transfer Ke Waiting Proses NB ";
					}
				}else if(lspdId==2 && lssaId==5){//dari posisi UNCLEAN & sudah Accept- NB&COLL belum approve salah satu
					uwDao.updateMstInsured(spaj,new Integer(251),lssaId,insuredNo, null);
					uwDao.insertMstPositionSpaj(lusId,"TRANSFER KE WAITING PROSES NB", spaj, 1);
					uwDao.updateMstPolicyPosition(spaj,new Integer(251),lstbId);
					uwDao.prosesSnows(spaj, currentUser.getLus_id(), 251, 212);
					pesanTambahan="Transfer Ke Waiting Proses NB ";
				}
			}else{
				pesanTambahan="Collection Done (* Jika Terdapat Infomarsi Tambahan, Proses Collection Belum Done)";
			}
		}

		allResult.put("proses", proses);
		allResult.put("pesanTambahan", pesanTambahan);
		allResult.put("pesanErorr", pesanErorr);
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return allResult;	
	}
	
}
