package com.ekalife.elions.web.uw;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.DepositPremium;
import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.Transfer;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentFormController;

/**@author Ferry Harlim
 *<p> Class merupakan Simple Form Controller, dimana berfungsi untuk mengontrol 
 * proses transfer polis dari posisi di Underwriting (2) ke Payment(4) atau Print Polis(6).</p>
 * 
 */
public class TransferFormController extends ParentFormController {
	
	DecimalFormat fmt = new DecimalFormat ("000");
	/**Fungsi : Untuk membuka data polis dan melakukan pengecekan awal dari suatu polis jika ingin diproses,
	 * 			seperti pengecekan posisi polis, dimana polis pada proses ini harus berada di Underwritting(2)
	 * @param HttpServletRequest request
	 * @return com.ekalife.elions.web.model.Transfer
	 * */
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Transfer transfer=new Transfer();
		Policy policy=new Policy();
		transfer.setInsuredNo(new Integer(1));
		transfer.setSpaj(request.getParameter("spaj"));
		transfer.setInfo(new Integer(0));
		transfer.setLiPosisi(0);
		transfer.setLspdId(new Integer(2));
		transfer.setLstbId(new Integer(1));
		transfer.setBlock(new Integer(4));
		//cek posisi
		Map mPosisi=elionsManager.selectF_check_posisi(transfer.getSpaj());
		Integer lspdIdTemp=(Integer)mPosisi.get("LSPD_ID");
		String lspdPosTemp=(String)mPosisi.get("LSPD_POSITION");
		if(lspdIdTemp.intValue()!=2){
			transfer.setInfo(new Integer(1));
			transfer.setLsposDoc(lspdPosTemp);
			//posisi document ada di lspdPosTemp
		}
		//
		Map mStatus=elionsManager.selectWfGetStatus(transfer.getSpaj(),transfer.getInsuredNo());
		transfer.setLiAksep((Integer)mStatus.get("LSSA_ID"));
		transfer.setLiReas((Integer)mStatus.get("MSTE_REAS"));
		if (transfer.getLiAksep()==null) 
			transfer.setLiAksep(new Integer(1));
		transfer.setLiBackup((Integer)elionsManager.selectMstInsuredMsteBackup(transfer.getSpaj(),transfer.getInsuredNo()));

		//
		if(transfer.getLiAksep().intValue()==2){
			transfer.setInfo(new Integer(2));
			//status spaj decline , Transfer Status Decline ke Policy Canceled ?
		}
		//dw1 //pemegang
		policy=elionsManager.selectDw1Underwriting(transfer.getSpaj(),transfer.getLspdId(),transfer.getLstbId());
		if(policy!=null){
			transfer.setMspoPolicyHolder(policy.getMspo_policy_holder());
			transfer.setNoPolis(policy.getMspo_policy_no());
			transfer.setInsPeriod(policy.getMspo_ins_period());
			transfer.setPayPeriod(policy.getMspo_pay_period());
			transfer.setLkuId(policy.getLku_id());
			transfer.setUmurPp(policy.getMspo_age());
			transfer.setLcaId(policy.getLca_id());
		//

			//cek standard
			if(policy.getMste_standard().intValue()==1){
			Integer liCount=elionsManager.selectCountMstProductInsuredCekStandard(transfer.getSpaj());
				if(liCount.intValue()==0){
					//Polis ini non-standard, Extra Premi Belum Ada !!!~n~nYakin Lanjutkan ?'
					transfer.setInfo(new Integer(3));
				}
			}
			transfer.setPolicy(policy);
		}
		//
		List lsProdInsured=elionsManager.selectMstProductInsured(transfer.getSpaj(),new Integer(1),new Integer(1));
		String lsdbs_number = uwManager.selectLsdbsNumber(transfer.getSpaj());
		if(!lsProdInsured.isEmpty()){
			Product prodIns=(Product)lsProdInsured.get(0);
			transfer.setLsbsId(prodIns.getLsbs_id());
		}
		//
		transfer.setLiLama(elionsManager.selectCountMstCancel(transfer.getSpaj()));
		transfer.setLsDp(elionsManager.selectMstDepositPremium(transfer.getSpaj(),null));
		//
		if(transfer.getLsbsId().intValue()==157 || (transfer.getLsbsId().intValue()==196)&&lsdbs_number.equals("2")){//jika product endowment langsung transfer ke print polis 14-06-06
			transfer.setLiPosisi(6);
			transfer.setTo("Print Polis ?");
		}else{
//			if(transfer.getLsDp().isEmpty() && transfer.getLiLama().intValue() == 0){
//				transfer.setLiPosisi(10);
//				transfer.setTo("Print Speciment");
//			}else
			if (transfer.getLiLama().intValue() > 0){
				transfer.setLiPosisi(6);
				transfer.setTo("Print Polis ?");
				transfer.setLiLama(new Integer(1));
			}else{
				transfer.setLiPosisi(4);
				transfer.setTo("Pembayaran ?");
			}
		}	
		
		String rdm = ServletRequestUtils.getStringParameter(request, "spaj");
		String rdblock = uwManager.selectRedeemBlock(rdm);
		if (rdblock != null){		
			transfer.setBlock(1);
		}
		
		return transfer;
	}
	/**
	 * Fungsi : Untuk melakukan pengecekan dari kriteria-kriteria/syarat dari suatu polis,
	 * 			Apakah polis tersebut telah memenuhi seluruh kriteria untuk dapat di transfer
	 * 			dan dilanjutkan ke proses selanjutnya.Jika ada Suatu kriteria maka dapat di sisipan suatu kondisi
	 * 			pada method ini.
	 * @param HttpServletRequest request, Object cmd, BindException err
	 * */
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Integer flagNew=ServletRequestUtils.getIntParameter(request, "flagNew",0);
		
		int proses=Integer.parseInt(request.getParameter("proses"));
		//1=decline 2=transfer ke payment 
		Transfer transfer=(Transfer)cmd;
		String kode;
		String kode2=" 112, 126, 133";
		int pos=0,pos2=0;
		transfer.setInfo(new Integer(100));
		//
		//data usulan asuransi
		Map mDataUsulan=uwManager.selectDataUsulan(transfer.getSpaj());
		transfer.setPModeId((Integer)mDataUsulan.get("LSCB_ID"));
		transfer.setBegDate((Date)mDataUsulan.get("MSTE_BEG_DATE"));
		transfer.setEndDate((Date)mDataUsulan.get("MSTE_END_DATE"));
		transfer.setMedical((Integer)mDataUsulan.get("MSTE_MEDICAL"));
		transfer.setLsbsId((Integer)mDataUsulan.get("LSBS_ID"));
		transfer.setLsdbsNumber((Integer)mDataUsulan.get("LSDBS_NUMBER"));
		transfer.setBisnisId(fmt.format(transfer.getLsbsId().intValue()));
		transfer.setPremium((Double)mDataUsulan.get("MSPR_PREMIUM"));
		transfer.setTsi((Double)mDataUsulan.get("MSPR_TSI"));
		//tertanggung
		Map mTertanggung=elionsManager.selectTertanggung(transfer.getSpaj());
		transfer.setInsuredNo((Integer)mTertanggung.get("MSTE_INSURED_NO"));
		transfer.setMsteInsured((String)mTertanggung.get("MCL_ID"));
		transfer.setMsagId((String)mTertanggung.get("MSAG_ID"));
		transfer.setUmurTt((Integer)mTertanggung.get("MSTE_AGE"));
		transfer.setCurrentUser((User) request.getSession().getAttribute("currentUser"));
		Date tglTerimaSpaj=(Date)mTertanggung.get("MSTE_TGL_TERIMA_SPAJ");
		//yusuf 5/03/2007
		//jika endors tidak cek tanggal
		if(uwManager.selectAllEndorsements(transfer.getSpaj()).isEmpty()){
			//kolom tanda terima spaj dan tanggal spaj doc tidak boleh kosong request Yusuf 20/12/2006 (email)
			Date tglSpajDoc=(Date)mTertanggung.get("MSTE_TGL_SPAJ_DOC");
//			if(!transfer.getLcaId().equals("58")){
				if(tglSpajDoc==null)err.reject("","Tanggal kelengkapan Dokumen Spaj masih Kosong.Silakan Update terlebih dahulu untuk tanggal ini.");
				if(tglTerimaSpaj==null) err.reject("","Tanggal Terima Spaj masih Kosong. Silakan Update terlebih dahulu tanggal terima spaj ini.");
//			}else{
//				if(tglSpajDoc==null)
//					err.reject("","Tanggal Terima Spaj atau Tanggal Dokumen Spaj masih Kosong.Update dahulu tanggal terima spaj ini");
//			}
			
		}
		
		if(transfer.getLcaId().equals("58") && tglTerimaSpaj==null){
			err.reject("","Khusus Mall Assurance, Silakan mengisi Tanggal Terima SPAJ terlebih dahulu sebelum melakukan transfer ke proses selanjutnya.");
		}
		
		//cek fund alocation untuk product link
		if(!products.stableLink(transfer.getBisnisId()) && products.unitLink(transfer.getBisnisId())){
			if(elionsManager.selectCountMstTransUlink(transfer.getSpaj(),null).intValue()==0)
					err.reject("","Ini Polis Excellink. HarusFUND ALLOCATION dulu");
		}	
		//cek medical
		if(transfer.getMedical().intValue()==1){
			Integer countMedical=elionsManager.selectCountMstMedical(transfer.getSpaj(),transfer.getInsuredNo());
			if(countMedical==null || countMedical.intValue()==0){
				err.reject("","Medical Belum diisi .");
			}
		}
		//pa=800
		Integer Pa=elionsManager.selectMstProductInsuredPa(transfer.getSpaj(),transfer.getInsuredNo(),new Integer(800));
		if(Pa.intValue()==1){
			Integer liClass=elionsManager.selectMstProductInsuredMsprClass(transfer.getSpaj(),transfer.getInsuredNo(),new Integer(800));
			if(liClass==null || liClass.intValue()==0){
				err.reject("","Class PA Belum diisi .");
			}
		}
		//
		//
		kode=props.getProperty("product.plan_asystem");
		pos=0;
		pos=kode.indexOf(transfer.getBisnisId());
		if(pos>=0){
			//untuk produk ekalink bisa as dan regional
			if("37,46,52,60,73".indexOf(transfer.getLcaId()) == -1){
//			if(! transfer.getLcaId().equals("37") && ! transfer.getLcaId().equals("46") && ! transfer.getLcaId().equals("60")){
				String kd=props.getProperty("product.plan_asystemandregional");
				int p=kd.indexOf(transfer.getBisnisId());
				if(p==-1) err.reject("","Kode Plan Yang Anda Pilih Hanya untuk Agency System ");
			}
		}
		//
		if(proses==1){//proses decline
			elionsManager.prosesDecline(transfer.getSpaj(),transfer.getMsteInsured(),transfer.getInsuredNo(),transfer.getCurrentUser().getLus_id(),new Integer(0),new Integer(15),new Integer(2),transfer.getLstbId(),"TRANSFER DARI U/W KE PAYMENT");
			err.reject("Proses Decline Berhasil");
		
		}else if(proses==2){
			
			if(transfer.getMsteInsured().substring(0,2).equals("XX") || transfer.getMsteInsured().substring(0,2).equals("WW") ){
				if(transfer.getLsbsId()!=161)//klo bukan produk ASM cek simultan
					err.reject("","Proses Simultan Belum dilakukan ");
			}else if(transfer.getLiReas()==null || transfer.getLiBackup()==null || transfer.getLiBackup().intValue()==0){
				err.reject("","Spaj Belum Proses Reas");
			}else if(transfer.getLiAksep()==null){
				err.reject("","SPAJ Belum di Aksep ");
			}else if(transfer.getLiReas().intValue()==0 && transfer.getLiAksep().intValue()<5){
				err.reject("","Status SPAJ Belum Accepted !!! (Non Reas)");
			}
			else if(( transfer.getLiReas().intValue() == 1 ) && ( transfer.getLiAksep().intValue()< 5 )) {
				err.reject("","Status SPAJ Belum Accepted !!! (Treaty)");
			}else if(( transfer.getLiReas().intValue()== 2 ) && ( transfer.getLiAksep().intValue() < 5 )){
				err.reject("","Status SPAJ Belum Accepted !!! (Facultative)");
			}else if(( transfer.getLiReas().intValue()== 2 ) &&( transfer.getLiBackup().intValue()==0 )){
				err.reject("","Status SPAJ Belum Full Backup !!! (Facultative)");
			//Bonie 01042002 - no polis tidak muncul setelah diakseptasi	
			}else if(transfer.getNoPolis()==null){
				err.reject("","Belum ada No Polis");
			}
			//
			//cek product link
			kode2=" 112, 126, 133";
			pos2=kode2.indexOf(transfer.getBisnisId());
			
			if(products.stableLink(transfer.getLsbsId().toString())) {
				List<Map> slink = elionsManager.selectValidasiStableLink(transfer.getSpaj());
				if(transfer.getLsdbsNumber()==11){
					slink = uwManager.selectValidasiNewStableLink(transfer.getSpaj());
				}
				if(slink.isEmpty()) {
					err.reject("", "Ada kesalahan pada data Stable Link. Harap hubungi IT");
				}else {
					for(Map m : slink) {
						int validasi = ((BigDecimal) m.get("V")).intValue();
						if(validasi == 0) {
							err.reject("", "Ada kesalahan pada data Stable Link. Harap hubungi IT");
							break;
						}
					}
				}
			}else if(products.unitLink(transfer.getBisnisId())){
				Double ldecPersen;
				ldecPersen=elionsManager.selectSumPersenMstDetUlink(transfer.getSpaj());
				if(ldecPersen==null || ldecPersen.intValue()!=100){
					err.reject("","Produk Unit-Linked !!!~nAlokasi Investasi Belum Lengkap ");
				}
				//
				ldecPersen=elionsManager.selectCountMstBiayaUlink(transfer.getSpaj());
				if(ldecPersen==null ||ldecPersen.intValue()==0){
					err.reject("","Produk Unit-Linked !!! Biaya Alokasi Investasi Belum Lengkap !!!");
				}
				//
				if(transfer.getLiAksep().intValue()!=5 && transfer.getLiAksep().intValue()!=10){
					err.reject("","Produk Unit-Linked !!!~nPolis Belum di-Aksep !!!");
				}
				//
			}else if(pos2>0){
				Double ldecPersen=elionsManager.selectMstPolicyUnderTable(transfer.getSpaj());
				if(ldecPersen==null){
					err.reject("","Bonus Tahapan Belum Di-isi !!! Silahkan Edit SPAJ ini");
				}
			}
			//
			//check_premi
			if(! wf_check_premi(transfer) &&(transfer.getLcaId().equals("58")&& transfer.getBisnisId().equals("196")) ){
				err.reject("","Tidak Bisa transfer ada kesalahan premi.Hubungi E.D.P ");
			}
	//		//cek standard
	//		String standard=request.getParameter("standard");
	//		if(standard.equals("0")){
	//		Integer liCount=elionsManager.selectCountMstProductInsuredCekStandard(spaj);
	//			if(liCount.intValue()==0){
	//				//li_count = Messagebox('Info', 'Polis ini non-standard, Extra Premi Belum Ada !!!~n~nYakin Lanjutkan ?', Question!, Yesno!, 2)
	//				info=14;
	//				err.reject("","Polis Non Standard");
	//			}
	//		}
			//
			transfer.setLiLama(elionsManager.selectCountMstCancel(transfer.getSpaj()));
			
			//Yusuf - 14/12/2007 - flag yang menandakan polis inputan bank
			//Deddy - 18/07/2012 - konfirm dgn team bancass, bahwa untuk titipan premi sudah bisa langsung diisi di U/W setelah SNOWS berjalan.Jadi validasi titipan premi dibuka untuk case bancass dan plan produk powersave & stable link
			int isInputanBank = elionsManager.selectIsInputanBank(transfer.getSpaj());
			List lds_dp=new ArrayList();
			//validasi untuk produk endowment (tidak harus input titipan premi 14-06-06)
			//untuk inputan bank juga tidak harus input titipan premi (Yusuf - 14/12/2007)
			Account_recur account_recur = elionsManager.select_account_recur(transfer.getSpaj());
			if("40,58".indexOf(transfer.getLcaId())<0 && Integer.parseInt(transfer.getBisnisId())!=157 
					&& (Integer.parseInt(transfer.getBisnisId())!=171 || transfer.getLsdbsNumber() !=2) && (Integer.parseInt(transfer.getBisnisId())!=182 
					|| transfer.getLsdbsNumber() !=7) && (Integer.parseInt(transfer.getBisnisId()) !=182 || transfer.getLsdbsNumber() !=8) 
					&& (Integer.parseInt(transfer.getBisnisId()) !=182 || transfer.getLsdbsNumber() !=9) 
					&& !products.powerSave(transfer.getBisnisId().toString()) 
					&& !products.stableLink(transfer.getBisnisId().toString()) && isInputanBank!=2 && isInputanBank!=3
					){
				lds_dp=elionsManager.selectMstDepositPremium(transfer.getSpaj(),null);
				if(lds_dp.size()==0){
					if(account_recur.getFlag_autodebet_nb()!=null){
						if(account_recur.getFlag_autodebet_nb()!=1){
							err.reject("","Jumlah Titipan Premi Belum Di Input");
						}
					}
				}
			}
			
			if(cekTransferBackToUw(transfer.getSpaj())==false){
				if(account_recur.getFlag_autodebet_nb()!=null){
					if(account_recur.getFlag_autodebet_nb()!=1){
						err.reject("","Polis Ini sudah pernah ditransfer dari U/W. Tidak bisa Di transfer 2x");
					}
				}
			}
			
			/*validasi untuk produk multi-invest
			**Rk s/d 28/02/03 boleh pakai 74, rk >= 01/03/03 pakai 76; berlaku s/d 31/03/03; mulai 01/04/03 harus 76
			**tgl 1oct - 31 des05 pakai 135 & 136
			**If li_bisnis = 74 or li_bisnis = 76 Then*/
			kode="";
			pos=0;
			kode=props.getProperty("product.plan_minvest");
			pos=kode.indexOf(transfer.getBisnisId());
			if (pos>= 0 ){
				Date ldt_rk;
				Calendar calAwal=new GregorianCalendar(2005,10-1,01);
				Calendar calAkhir=new GregorianCalendar(2007,1-1,31);

				for(int i=0;i<lds_dp.size();i++){
					DepositPremium depPre=(DepositPremium) lds_dp.get(i);
					ldt_rk = depPre.getMsdp_date_book();
					if(Integer.parseInt(transfer.getBisnisId())== 96 || Integer.parseInt(transfer.getBisnisId())== 99){
						if(FormatDate.isDateBetween(ldt_rk,calAwal.getTime(),calAkhir.getTime())){
							err.reject("","Tgl RK SPAJ ini : "+defaultDateFormatStripes.format(ldt_rk)+" . Khusus periode 1 Oktober 2005 sampai dengan 31 Januari 2007");
							err.reject("","Harus Pakai Multi-Invest III (new) 135/136 ");

						}
					}else if( Integer.parseInt(transfer.getBisnisId())== 135 || Integer.parseInt(transfer.getBisnisId())== 136 ){
						if(! (FormatDate.isDateBetween(ldt_rk,calAwal.getTime(),calAkhir.getTime())) ){ 
							err.reject("","Tgl RK Spaj ini "+defaultDateFormatStripes.format(ldt_rk)+" Di luar Periode 1 Oktober 2005 sampai dengan 31 Januari 2007");
							err.reject("","Harus Pakai Multi-Invest III 96/99 ");	
						}
					}
				
				}	
			}
			
			//validasi produk maxi Deposit (103, 114, 137) 2/1/2008 (ucup)
			lds_dp=elionsManager.selectMstDepositPremium(transfer.getSpaj(),1);
			if( (!lds_dp.isEmpty())){
				Calendar calBatas=new GregorianCalendar(2007,12-1,31);
				String productMaxiDeposit=props.getProperty("product.maxi_deposit");
				Integer idx=productMaxiDeposit.indexOf(transfer.getBisnisId());
				DepositPremium depPre=(DepositPremium)lds_dp.get(0);
				if(transfer.getBisnisId().equals("137") || transfer.getBisnisId().equals("114")){
					if(transfer.getBisnisId().equals("137")){
						if(depPre.getMsdp_date_book().after(calBatas.getTime()) && idx>0 && ("3,4,7,8,9".indexOf(transfer.getLsdbsNumber().toString()) <0 ) ){
							err.reject("","Produk Maxi Deposit Rp dapat di proses oleh bagian Undw adalah SPAJ yang diterima oleh AJS paling lambat tgl 31 Desember 2007.");
						}
					}else if(transfer.getBisnisId().equals("114")){
						if(depPre.getMsdp_date_book().after(calBatas.getTime()) && idx>0 && transfer.getLsdbsNumber()<2){ // sebelum direvisi, nilai lsdbsNumber<3
							err.reject("","Produk Maxi Deposit Rp dapat di proses oleh bagian Undw adalah SPAJ yang diterima oleh AJS paling lambat tgl 31 Desember 2007.");
						}
					}
				}else {
					if(depPre.getMsdp_date_book().after(calBatas.getTime()) && idx>0){
						err.reject("","Produk Maxi Deposit Rp dapat di proses oleh bagian Undw adalah SPAJ yang diterima oleh AJS paling lambat tgl 31 Desember 2007.");
					}
				}
			}
			
			transfer.setLsDp(elionsManager.selectMstDepositPremium(transfer.getSpaj(),null));

			//Yusuf - 25-09-08 - Bila bukan inputan bank, harus ada validasi checklist
			//if(isInputanBank < 0) {
			if(!elionsManager.selectValidasiCheckListBySpaj(transfer.getSpaj()))
				err.reject("", "Harap Input CHECKLIST DOKUMEN POLIS Terlebih Dahulu!");
			//}
			
			//Yusuf - 14 Okt 08 - Bila save bayar lnik, harus input dulu sblink sampe transfer
			int flag_cc = elionsManager.selectValidasiTransferPbp(transfer.getSpaj());
			if(flag_cc == 4) {
				if(elionsManager.selectJumlahTransferPbp(transfer.getSpaj()) != 4) {
					err.reject("", "Harap lakukan proses input/transfer SB LINK terlebih dahulu!");
				}
			}
			
			//Yusuf - 14/12/2007 - Transfer U/W ke step berikutnya gak boleh kalo Titipan premi belum ada khusus polis inputan bank
			int pes=0;
			String kodok=props.getProperty("product.planRefBii");
			pes=kodok.indexOf(transfer.getBisnisId());
			if(pes>=0 && (isInputanBank==2 || isInputanBank==3)){
				Map reg = elionsManager.selectRegion(transfer.getSpaj());
				String region = (String) reg.get("LSRG_NAMA");
				
				//kuseno 0916 bisa dua2nya 22/03/2007
				if(region.equals("0916")){
					Map mapNasabah=uwManager.selectLeadNasabahFromSpaj(transfer.getSpaj());
					Map mReffBii=elionsManager.selectReferrerBii(transfer.getSpaj());
					if(mapNasabah==null ||mapNasabah==null)
						err.reject("","Harap Input Reff BII..");
				}else{
					//Yusuf (25/09/2006) -> Kalo referral dari BII (bukan yang lain), maka : 
					//cek nya di MST_POLICY.MSPO_PLAN_PROVIDER di JOIN dengan MST_NASABAH 
					if("0914,0917,0919,0920,0923,0924,0925,0926".indexOf(region) > -1) {
						//validasi carau baru (MST_POLICY.MSPO_PLAN_PROVIDER -> MST_NASABAH)
						Map mapNasabah=uwManager.selectLeadNasabahFromSpaj(transfer.getSpaj());
						if(mapNasabah==null)
							err.reject("","Harap Input Referral (EKA.MST_NASABAH Kosong)");
						
					}else {
						//validasi lama
						Map mReffBii=elionsManager.selectReferrerBii(transfer.getSpaj());
						if(mReffBii==null){
							err.reject("","Harap Input Referral terlebih dahulu");
						}
					}
				}
				
				//yusuf - 13/06/2008 - simas prima, gak bisa ditransfer ke TTP kalo masih akseptasi khusus
//				Integer count=elionsManager.selectCountProductSimasPrimaAkseptasiKhusus(transfer.getSpaj(), 1,10,2);
//				if(count>0){
//					err.reject("","Produk Simas Prima Masih Terakseptasi Khusus. Tidak Bisa di transfer");
//				}
			}
			
		}
		
	}
	
	/**
	 * 	Fungsi : untuk Mengecek Apakah SPAJ ini sudah pernah di transfer dari BAC ke UW
	 **/
//	private boolean cekTransferBackToUw(List lsDp){
//		boolean hsl=true;
//		Integer lsRekId;
//		String msdpNoPre;
//		
//		for(int i=0;i<lsDp.size();i++){
//			
//			DepositPremium deposit=(DepositPremium)lsDp.get(i);
//			lsRekId=deposit.getLsrek_id();
//			msdpNoPre=deposit.getMsdp_no_pre();
//			if( (lsRekId.intValue()==0 && (msdpNoPre==null || msdpNoPre.equals("")) ) || 
//					((lsRekId.intValue()!=0 && (msdpNoPre==null || msdpNoPre.equals("") ) ) ) ){//
//				hsl=true; //proses transfer boleh dilakukan
//			}else{
//				hsl=false;
//				break;
//			}
//		
//		}
//		return hsl;
//	}
	
	/**
	 * @function : Pembaharuan dari fungsi sebelumnya, dimana kondisi saat ini dicek berdasarkan data billing apakah sudah exist atau belum.Apabila sudah exist, berarti sudah pernah dilakukan transfer dr u/w ke payment atau print polis
	 * @author Deddy
	 * @since 19 Jul 2012
	 * @param String reg_spaj
	 * @return boolean
	 */
	private boolean cekTransferBackToUw(String reg_spaj){
		boolean result = true;
		Integer count =uwManager.selectCountMstBillingNB(reg_spaj);
		if(count>0){
			result = false;
		}
		return result;
	}

	/**Fungsi : untuk mengecek apakah Premi dari suatu polis yang di pilih itu tidak kosong
	 * 			
	 * @param com.ekalife.elions.web.uw.model.Transfer
	 * @return boolean 
	 * 
	 * 
	 **/
	private boolean wf_check_premi(Transfer transfer)throws Exception {
		boolean lbRet=true;
		Integer kodeBisnis;
		List lsProductInsured=elionsManager.selectMstProductInsured(transfer.getSpaj(),transfer.getInsuredNo(),new Integer(1));
		for(int i=0;i<lsProductInsured.size();i++){
			Product product=(Product)lsProductInsured.get(i);
			if(product.getMspr_premium()==null || product.getMspr_premium().doubleValue()<=0){
				kodeBisnis=product.getLsbs_id();
				if(kodeBisnis.intValue()>=810 && kodeBisnis.intValue()<=833)
					lbRet=true;
				else if(kodeBisnis.intValue()==900 || kodeBisnis.intValue()==904 || kodeBisnis.intValue()==905)
					lbRet=true;
				else
					return false;
			}	
		}
		return lbRet;
	}
	/**
	 * Fungsi : berfungsi untuk memperbolehkan suatu polis untuk di proses karena telah
	 * 			melewati kriteria atau persyaratan dari polis tersebut sebelum di transfer.
	 * @param HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err
	 * @return Object untuk menampilkan info dari proses transfer ini.
	 * hasil=0 ==> proses transfer gagal
	 * hasil=1 ==> proses Transfer Ke pembayaran Berhasil
	 * hasil=2 ==> payment sudah lunas lanjut transfer ke Print Polis (yusuf pake popup di jsp transfer) 
	 * hasil=3 ==> proses transfer ke print polis tidak melakukan proses insBIll(ada endors).
	 * hasil=5 ==> proses transfer gagal karena mst_insured.mste_pct_dplk gak boleh 0 atau kosong
	 * */
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		int proses=Integer.parseInt(request.getParameter("proses"));
		//flag proses tranfer pembayaran new atau old( 1=new , 0=old);
		Integer flagNew=ServletRequestUtils.getIntParameter(request, "flagNew",0);
		int hasil = 0;
		String pesanTambahan = null;
		Map allResult = new HashMap();
		Transfer transfer=(Transfer)cmd;
		//1=decline 2=transfer ke payment 
		if(proses==1){
			elionsManager.prosesDecline(transfer.getSpaj(),transfer.getMsteInsured(),transfer.getInsuredNo(),transfer.getCurrentUser().getLus_id(),
					new Integer(0),new Integer(15),new Integer(2),transfer.getLstbId(),"TRANSFER DARI U/W KE PAYMENT");
			String spaj = transfer.getSpaj();
			bacManager.prosesSnows(spaj, currentUser.getLus_id(), 211, 212);
			hasil=4;
		}else if(proses==2){
			allResult=elionsManager.prosesTransferPembayaran(transfer,flagNew,err,request);
			pesanTambahan = (String) allResult.get("pesanTambahan");
			hasil = (Integer) allResult.get("proses");
			request.getSession().setAttribute("inbox_flag_transfer_document", 0);
			
			if (transfer.getLsbsId()==196 && transfer.getLsdbsNumber()==2){
				pesanTambahan="Proses Transfer Print Polis Berhasil ";
			}			
			// Hati-hati Digunakan Hanya untuk proses insert mst_reins dan reins_product
			//hasil=elionsManager.prosesTransferPembayaranReasUlang(transfer, err);	
			
			//Yusuf (11/02/2010) - Req Dr Ingrid, ada proses sample UW untuk BII, 
			//dimana start Feb 2010, untuk produk BII harus diberi warning untuk sampling kuesioner
//			pesanTambahan = uwManager.cekRandomSamplingBii(transfer.getSpaj());
		}
		if(pesanTambahan == null) return new ModelAndView("uw/transfer", err.getModel()).addObject("hasil",""+hasil);
		else return new ModelAndView("uw/transfer", err.getModel()).addObject("hasil",""+hasil).addObject("pesanTambahan", pesanTambahan);
	}
		
}