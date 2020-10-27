package com.ekalife.elions.process;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;

import com.ekalife.elions.model.Commission;
import com.ekalife.elions.model.Deduct;
import com.ekalife.elions.model.MedQuest;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Premi;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.SpajBill;
import com.ekalife.elions.model.Ttp;
import com.ekalife.elions.model.Ulink;
import com.ekalife.elions.model.User;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.parent.ParentDao;

import produk_asuransi.n_prod;

public class TransferPolis extends ParentDao{
	protected final Log logger = LogFactory.getLog( getClass() );
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);		
	
	public void prosesKomisiROAgentPromosiKetinggalanJanuari2010(String spaj, User currentUser) throws Exception{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		Date sysdate = commonDao.selectSysdateTruncated(0);
		Date sysdatePayDate = commonDao.selectSysdateTruncated(1);//set untuk hari besoknya
		BindException errors = new BindException(new HashMap(), "cmd");
		Date tgl_bayar = defaultDateFormat.parse("9/2/2010");
		List<String> daftarAgen = new ArrayList<String>();
		//
		daftarAgen.add("800114");
		daftarAgen.add("800168");
		String insert1="INERT KE SPAJ";
		for(String msag : daftarAgen){
			List<Map> listkomisi = editDataDao.selectCommissionWSLanjutanYgTertinggalper2010(msag);
			for(int i=0;i<listkomisi.size();i++){
				Map m = listkomisi.get(i);
				String reg_spaj 		= (String) m.get("REG_SPAJ");
				String no_polis 		= (String) m.get("MSPO_POLICY_NO"); 
				Integer mspro_prod_ke	= new Integer(m.get("MSPRO_PROD_KE").toString());
				Integer msbi_tahun_ke	= new Integer(m.get("MSBI_TAHUN_KE").toString());
				Integer msbi_premi_ke	= new Integer(m.get("MSBI_PREMI_KE").toString());
				Integer flag_msco_paid_last = uwDao.selectLastMscoPaid(reg_spaj, msbi_tahun_ke, msbi_premi_ke);
			
				if(uwDao.selectcountProdLastAgent(reg_spaj, msbi_tahun_ke, msbi_premi_ke, msag)==0){
					Commission commission = new Commission();
					String msco_id = komisi.sequenceMst_commission(11);
					commission.setReg_spaj(reg_spaj);
					commission.setMsco_id(msco_id);
					commission.setCo_id(msco_id);
					commission.setMsbi_premi_ke(msbi_premi_ke);
					commission.setMsbi_tahun_ke(msbi_tahun_ke);
					commission.setMsag_id(msag);
					commission.setLev_kom(4);					
					commission.setMsco_date(sysdate);
					
					commission.setMsco_active(1);
					commission.setMsco_paid(0);
					commission.setMsco_jurnal(0);
					commission.setLus_id(currentUser.getLus_id());
					commission.setMsco_nilai_kurs(0.0);
					commission.setMsco_flag(1);
					commission.setAgent_id(msag);
					
					if(flag_msco_paid_last==1){
						Double comm = uwDao.selectMscoCommLastAgent(reg_spaj, msbi_tahun_ke, msbi_premi_ke-1, msag);
						commission.setKomisi(comm);
						commission.setKomisi(FormatNumber.rounding(commission.getKomisi() , false, 25));
						commission.setMsco_pay_date(sysdatePayDate);
						if(comm!=null){
							commission.setTax(komisi.f_load_tax(comm, sysdate, msag));
							commission.setTax(FormatNumber.rounding(commission.getTax() , true, 25));
						}
						commission.setMsco_pay_date(tgl_bayar);
					}else if(flag_msco_paid_last==0){
						Double comm = uwDao.selectMscoCommLastAgent(reg_spaj, msbi_tahun_ke, msbi_premi_ke-1, msag);
						commission.setKomisi(comm);
						commission.setKomisi(FormatNumber.rounding(commission.getKomisi() , false, 25));
						if(comm!=null){
							commission.setTax(komisi.f_load_tax(comm, sysdate, msag));
						}
					}
					// insertnya harus ditambahkan msco_pay_date ya!!!!!
					this.uwDao.insertMst_commission(commission, sysdate);
					insert1+="\nspaj :"+commission.getReg_spaj()+" agen id: "+commission.getAgent_id()+" premi_ke ="+commission.getMsbi_premi_ke();
					
					Map infoBonus 			= uwDao.selectBonusAgen(msag);
					String mcl_first 		= (String) infoBonus.get("MCL_FIRST");
					String msag_tabungan 	= (String) infoBonus.get("MSAG_TABUNGAN"); 
					String lbn_id 			= infoBonus.get("LBN_ID").toString();
					
					Commission uploadNon 	= new Commission();
					uploadNon.lbn_id 		= Integer.valueOf(lbn_id);
					uploadNon.account_no 	= msag_tabungan;
					uploadNon.msco_id 		= commission.msco_id; 
					uploadNon.tgl_kom 		= sysdate;
					uploadNon.tgl_paid 		= tgl_bayar;
					uploadNon.agent_id 		= msag;
					uploadNon.amount 		= commission.getKomisi() - commission.getTax();
					uploadNon.sts_aktif 	= 1;
					editDataDao.insertMstUploadNon(uploadNon);
				}
			}
		}
		logger.info(insert1);
	}
	
	public void testPerhitunganPajakNov2009(){
		try {
			// List<String> daftarAgen = editDataDao.selectDataAgenUntukTesting();
			List<String> daftarAgen = new ArrayList<String>();
			daftarAgen.add("021050");
			
			Date now = commonDao.selectSysdateTruncated(0);
			for(String msag : daftarAgen){
				double kom = komisi.f_load_tax(10000000, now, msag);
				//logger.info(msag + " = " + twoDecimalNumberFormat.format(kom));
			}
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
	}
	
	public void prosesBonusStableLinkYangKetinggalan() throws Exception{
		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		List<Commission> daftarSpaj = editDataDao.selectDataBonusStableLinkYangKetinggalan();
		
		Date sysdate = commonDao.selectSysdate();
		Date tanggalKomisi = defaultDateFormat.parse("12/10/2009");
		Date bulanKomisi = defaultDateFormat.parse("01/10/2009");
		
		for(int i=0; i<daftarSpaj.size(); i++){
			
			// bila cuman mau proses 1 data : if(i > 0) break;
			
			Commission c = daftarSpaj.get(i);
			c.setLsbs_linebus(bacDao.selectLineBusLstBisnis(c.getBisnis_id().toString()));
			
			double totalPremi 	= uwDao.selectTotalPremiStableLink(c.reg_spaj);
			String lku_id 		= uwDao.selectMstProductInsuredKurs(c.reg_spaj, 1); 
			int mgi 			= uwDao.selectMasaGaransiInvestasi(c.reg_spaj, c.msbi_tahun_ke, c.msbi_premi_ke);
			double persen 		= 0.;
			
			if((lku_id.equals("01") && totalPremi >= 100000000) || (lku_id.equals("02") && totalPremi >= 10000)) {
				if(mgi == 1) {
					persen = 0.033;
				}else if(mgi == 3) {
					persen = 0.1;
				}else if(mgi == 6) {
					persen = 0.2;
				}else if(mgi == 12) {
					persen = 0.4;
				}else if(mgi == 24) {
					persen = 0.55;
				}else if(mgi == 36) {
					persen = 0.7;
				}else {
					throw new RuntimeException("Harap cek perhitungan bonus penjualan Stable Link, mgi = " + mgi);
				}
			}

			if(persen > 0){
				Map infoBonus 			= uwDao.selectBonusAgen(c.msag_id);
				double bonus 			= FormatNumber.rounding(persen/100 * c.premi * c.msco_nilai_kurs, false, 25);
				String mcl_first 		= (String) infoBonus.get("MCL_FIRST");
				String msag_tabungan 	= (String) infoBonus.get("MSAG_TABUNGAN"); 
				String lbn_id 			= infoBonus.get("LBN_ID").toString();
	
				//perhitungan pajak progresif dari TOTAL komisi dan bonus
				Double temp 		= c.msco_comm + bonus;
				Double bonus_tax 	= komisi.f_load_tax(temp, sysdate, c.msag_id);
				bonus_tax 			-= c.msco_tax;
				bonus_tax 			= FormatNumber.rounding(bonus_tax, false, 25);
				
				if(bonus < 1){
					logger.info("ompong?");
				}
				
				//1. kalau belum bayar, insert biasa ke comm bonus
				if(c.msco_paid.intValue() == 0){
					if(bonus > 0){
						this.uwDao.insertMst_comm_bonus(
								c.reg_spaj, c.msbi_tahun_ke, c.msbi_premi_ke, 1, c.msag_id, 
								mcl_first, msag_tabungan, 
								lbn_id, c.msco_id, bonus, bonus_tax, c.msco_nilai_kurs, "0",
								tanggalKomisi, c.lsbs_linebus);
					}
				//2. kalau sudah bayar
				}else{
					//karena sudah bayar, insert ke mst_commission, create 1 msco_id baru dgn level = 5
					c.msco_id 		= komisi.sequenceMst_commission(11); 
					c.co_id 		= c.msco_id;
					c.lev_comm 		= 5; c.lev_kom = 5;
					c.msco_comm 	= bonus; c.komisi = bonus;
					c.msco_tax 		= bonus_tax; c.tax = bonus_tax;
					c.nilai_kurs	= c.msco_nilai_kurs;
					c.agent_id		= c.msag_id;
					c.msco_date 	= tanggalKomisi;
					c.msco_pay_date = null;
					c.msco_paid 	= 0;
					c.msco_no_pre 	= null;
					c.msco_jurnal 	= null;
					c.lus_id 		= "0";
					c.msco_no 		= null;
					this.uwDao.insertMst_commission(c, tanggalKomisi);
					//step berikut, insert upload non
					Commission uploadNon 	= new Commission();
					uploadNon.lbn_id 		= Integer.valueOf(lbn_id);
					uploadNon.account_no 	= msag_tabungan;
					uploadNon.msco_id 		= c.msco_id; 
					uploadNon.tgl_kom 		= tanggalKomisi;
					uploadNon.tgl_paid 		= tanggalKomisi;
					uploadNon.agent_id 		= c.msag_id;
					uploadNon.amount 		= bonus - bonus_tax;
					uploadNon.sts_aktif 	= 1;
					editDataDao.insertMstUploadNon(uploadNon);
					//step berikut, insert agent tax pada bulan bersangkutan
					editDataDao.updateMstAgentTax(bonus, bonus_tax, bulanKomisi, c.msag_id);
				}

				//break;
			}
		}
	}
	
	/**
	 * @deprecated 
	 */
	public void testSequenceVoucherPremiIndividu(String reg_spaj, User currentUser) throws Exception{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		Premi premi = this.uwDao.selectPremiTertanggung(reg_spaj);
		int topupBerkala=this.uwDao.validationTopup(reg_spaj);
		BindException errors = new BindException(new HashMap(), "cmd");
		premi = this.jurnal.jurnalProduksiUnitLink(reg_spaj, premi, errors, currentUser, topupBerkala);
	}
	
	public void testJurnalProduksiIndividu(String reg_spaj, User currentUser) throws Exception {
		Premi premi = this.uwDao.selectPremiTertanggung(reg_spaj);
		BindException errors = new BindException(new HashMap(), "cmd");
		this.jurnal.jurnalProduksiIndividu(reg_spaj, premi, errors, currentUser);
	}
	
	public void testJurnalProduksiIndividuKetinggalanNoPre(String reg_spaj, User currentUser) throws Exception {
		Premi premi = this.uwDao.selectPremiTertanggung(reg_spaj);
		BindException errors = new BindException(new HashMap(), "cmd");
		this.jurnal.jurnalProduksiIndividuKetinggalanNoPre(reg_spaj, premi, errors, currentUser);
	}

	public void testJurnalProduksiUnitLink(String reg_spaj, User currentUser) throws Exception {
		Premi premi = this.uwDao.selectPremiTertanggung(reg_spaj);
		BindException errors = new BindException(new HashMap(), "cmd");
		String businessId = FormatString.rpad("0", this.uwDao.selectBusinessId(reg_spaj), 2);
		
		int topup;
		if(products.stableLink(businessId)) {
			topup=this.uwDao.validationStableLink(reg_spaj);
		}else {
			topup=this.uwDao.validationTopup(reg_spaj);
		}
		
		this.jurnal.jurnalProduksiUnitLink(reg_spaj, premi, errors, currentUser, topup);
	}
	
	public void testHitungReward(String reg_spaj, User currentUser) throws Exception{
		boolean flagTopup2007 = false;
		List billInfo = this.uwDao.selectBillingInfoForTransfer(reg_spaj, 1, 1);
		BindException errors = new BindException(new HashMap(), "cmd");
		
		String businessId = FormatString.rpad("0", this.uwDao.selectBusinessId(reg_spaj), 2);
		n_prod produk = (n_prod) Class.forName("produk_asuransi.n_prod_"+businessId).newInstance();
		businessId = FormatString.rpad("0", businessId, 3);
		
		SpajBill spajBill = new SpajBill();
		int li_persen_bayar = 0;
		Date ldt_next_bill; Date ldt_end_bill;
		
		Premi premi = this.uwDao.selectPremiTertanggung(reg_spaj);
		if(premi==null || premi.getNama_pemegang()==null){
			errors.reject("payment.namaPemegangKosong");
			throw new Exception(errors);
		}
				
		/** 2. Proses Komisi BancAss, refferall BII **/
		//(Yusuf 03/01/2008) - Tambah Amanah Link untuk diikutsertakan perhitungan komisi ref
		if(errors.getErrorCount()==0){
			String lca_id = uwDao.selectCabangFromSpaj(reg_spaj);
			if((lca_id.equals("09"))){
		/** 3. Proses Komisi Individu **/
			}else {
				//FLAG RECUR DI-DISABLE //YUSUF 20060329
//						Integer flag_cc = (Integer) ((HashMap)billInfo.get(0)).get("MSTE_FLAG_CC"); 
//						if(flag_cc==null) flag_cc = new Integer(0);
//						
//						if(flag_cc==0){
				if(logger.isDebugEnabled())logger.debug("~ PROSES KOMISI INDIVIDU untuk SPAJ " + reg_spaj);
				flagTopup2007 = this.komisi.prosesRewardIndividu(reg_spaj, currentUser, errors);
				if(errors.getErrorCount()>0){
					errors.reject("payment.commNewLgFailed");
					throw new Exception(errors);
				}
//						}
			}
		}else {
			errors.reject("payment.productionFailed");
			throw new Exception(errors);
		}

		int topupBerkala;
		if(products.stableLink(businessId)) {
			topupBerkala=this.uwDao.validationStableLink(reg_spaj);
		}else {
			topupBerkala=this.uwDao.validationTopup(reg_spaj);
		}
		
		/** 4. Proses Produksi Top Up**/
		//insert komisi topup excellink
		if(products.unitLink(businessId)){
			List<Integer> daftarMuKe = uwDao.selectMuKe(reg_spaj);
			
			if(topupBerkala>=1){
				
		/** 5. Proses Komisi Top Up Individu, komisi refferall BII yang topup **/
				//Excellink karyawan (113,138,139) tidak ada komisi
				if("113, 138, 139".indexOf(businessId)<0){
					if(logger.isDebugEnabled())logger.debug("~ PROSES KOMISI TOPUP INDIVIDU untuk SPAJ " + reg_spaj);
					if(flagTopup2007) {
						for(Integer d : daftarMuKe) {
							if(!this.komisi.prosesRewardTopUpIndividu2007(reg_spaj, errors, currentUser, d)){
								errors.reject("payment.komisiTuExcellinkFailed");
								throw new Exception(errors);
							}
						}
					}else {
						for(Integer d : daftarMuKe) {
							if(!this.komisi.prosesRewardTopUpIndividu(reg_spaj, errors, currentUser, d)){
								errors.reject("payment.komisiTuExcellinkFailed");
								throw new Exception(errors);
							}
						}
					}
				}
			}
		}
		
		////////////////////////////////////////////////////
		if(errors.getErrorCount()>0) {
			throw new Exception(errors);
		}

	}

	public void testHitungKomisi(String reg_spaj, User currentUser) throws Exception{
		boolean flagTopup2007 = false;
		List billInfo = this.uwDao.selectBillingInfoForTransfer(reg_spaj, 1, 1);
		BindException errors = new BindException(new HashMap(), "cmd");
		
		this.uwDao.deleteDataKomisi(reg_spaj);

		String businessId = FormatString.rpad("0", this.uwDao.selectBusinessId(reg_spaj), 2);
		n_prod produk = (n_prod) Class.forName("produk_asuransi.n_prod_"+businessId).newInstance();
		businessId = FormatString.rpad("0", businessId, 3);
		
		SpajBill spajBill = new SpajBill();
		int li_persen_bayar = 0;
		Date ldt_next_bill; Date ldt_end_bill;
		
		Premi premi = this.uwDao.selectPremiTertanggung(reg_spaj);
		if(premi==null || premi.getNama_pemegang()==null){
			errors.reject("payment.namaPemegangKosong");
			throw new Exception(errors);
		}
				
		/** 2. Proses Komisi BancAss, refferall BII **/
		//(Yusuf 03/01/2008) - Tambah Amanah Link untuk diikutsertakan perhitungan komisi ref
		if(errors.getErrorCount()==0){
			String lca_id = uwDao.selectCabangFromSpaj(reg_spaj);
			if(((lca_id.equals("09") && ("079, 080, 081, 091, 092, 111, 120, 121, 127, 128, 129, 150, 151, 155, 134, 165, 166".indexOf(businessId)>-1
					|| ("142".equals(businessId) && premi.getLsdbs_number()>=2 && premi.getLsdbs_number() != 8) //bukan DM/TM
					|| ("158".equals(businessId) && premi.getLsdbs_number()>=5)
					|| ("164".equals(businessId) && premi.getLsdbs_number()==2)
					|| ("175".equals(businessId) && premi.getLsdbs_number()== 2)
					|| ("186".equals(businessId) && premi.getLsdbs_number()==3))) 
					|| (!lca_id.equals("58") && uwDao.selectCountMstReffBii(reg_spaj)>0)) 
					&& (!lca_id.equals("73")) ){
				if("120, 121, 127, 128, 129, 150, 151, 155, 134, 166".indexOf(businessId)<0){
					if(logger.isDebugEnabled())logger.debug("~ PROSES KOMISI BANCASS untuk SPAJ " + reg_spaj);
					this.komisi.prosesKomisiBancAss(reg_spaj, currentUser, errors);
					if(errors.getErrorCount()>0){
						errors.reject("payment.commBancassFailed");
						throw new Exception(errors);
					}
				}
				
				//if("150, 151".indexOf(businessId)==-1){
					spajBill.setNo_spaj(reg_spaj);
					spajBill.setTahun_ke(1);
					spajBill.setPremi_ke(1);
					spajBill.setFlag_topup(0);
	
					if(logger.isDebugEnabled())logger.debug("~ PROSES HITUNG KOMISI REFF BII untuk SPAJ " + reg_spaj);
					errors = this.komisi.hitungKomisiReffBII(spajBill, errors, currentUser); 
					if(errors.getErrorCount()>0){
						errors.reject("payment.commReffBIIFailed");
						throw new Exception(errors);
					}				
				//}
		/** 3. Proses Komisi Individu **/
			}else {
				//FLAG RECUR DI-DISABLE //YUSUF 20060329
//						Integer flag_cc = (Integer) ((HashMap)billInfo.get(0)).get("MSTE_FLAG_CC"); 
//						if(flag_cc==null) flag_cc = new Integer(0);
//						
//						if(flag_cc==0){
				if(logger.isDebugEnabled())logger.debug("~ PROSES KOMISI INDIVIDU untuk SPAJ " + reg_spaj);
				flagTopup2007 = this.komisi.prosesKomisiIndividu(reg_spaj, currentUser, errors);
				if(errors.getErrorCount()>0){
					errors.reject("payment.commNewLgFailed");
					throw new Exception(errors);
				}
//						}
			}
		}else {
			errors.reject("payment.productionFailed");
			throw new Exception(errors);
		}

		int topupBerkala;
		if(products.stableLink(businessId)) {
			topupBerkala=this.uwDao.validationStableLink(reg_spaj);
		}else {
			topupBerkala=this.uwDao.validationTopup(reg_spaj);
		}
		
		/** 4. Proses Produksi Top Up**/
		//insert komisi topup excellink
		if(products.unitLink(businessId)){
			List<Integer> daftarMuKe = uwDao.selectMuKe(reg_spaj);
			
			if(topupBerkala>=1){
				
		/** 5. Proses Komisi Top Up Individu, komisi refferall BII yang topup **/
				//Excellink karyawan (113,138,139) tidak ada komisi
				if("113, 138, 139".indexOf(businessId)<0){
					if(logger.isDebugEnabled())logger.debug("~ PROSES KOMISI TOPUP INDIVIDU untuk SPAJ " + reg_spaj);
					if(flagTopup2007) {
						for(Integer d : daftarMuKe) {
							if(!this.komisi.prosesKomisiTopUpIndividu2007(reg_spaj, errors, currentUser, d)){
								errors.reject("payment.komisiTuExcellinkFailed");
								throw new Exception(errors);
							}
						}
					}else {
						for(Integer d : daftarMuKe) {
							if(!this.komisi.prosesKomisiTopUpIndividu(reg_spaj, errors, currentUser, d)){
								errors.reject("payment.komisiTuExcellinkFailed");
								throw new Exception(errors);
							}
						}
					}
				}
				
				//(Yusuf 03/01/2008) tambah amanah link 166 u/ ikut serta perhitungan kom ref
				if("120, 127, 128, 129, 134, 166".indexOf(businessId)>-1){
					for(Integer d : daftarMuKe) {
						spajBill.setNo_spaj(reg_spaj);
						spajBill.setTahun_ke(1);
						spajBill.setPremi_ke(d);
						spajBill.setFlag_topup(1);
//						 unit link bancass ada komisi tu u/ BII (RG) 26/09/2005
						if(logger.isDebugEnabled())logger.debug("~ PROSES HITUNG KOMISI REFF BII TOPUP untuk SPAJ " + reg_spaj);
						errors = this.komisi.hitungKomisiReffBII(spajBill, errors, currentUser);
						if(errors.getErrorCount()>0){
							errors.reject("payment.commReffBIIFailed2");
							throw new Exception(errors);
						}									
					}
				}
			}
		}
		
		////////////////////////////////////////////////////
		if(errors.getErrorCount()>0) {
			throw new Exception(errors);
		}

	}
	
	public void testHitungKomisiTopUpOnly(String reg_spaj, User currentUser) throws Exception{
		boolean flagTopup2007 = true;
		List billInfo = this.uwDao.selectBillingInfoForTransfer(reg_spaj, 1, 1);
		BindException errors = new BindException(new HashMap(), "cmd");
		
//		this.uwDao.deleteDataKomisi(reg_spaj);
		Map pribadi = this.uwDao.selectInfoPribadi(reg_spaj);
		String ls_region = (String) pribadi.get("REGION");
		String lca_id = ls_region.substring(0,2);
		String prodDate = (String) pribadi.get("PROD_DATE");
		
		if(ls_region.startsWith("0914")) {
			flagTopup2007 = false;
		}else if("37,46,52".indexOf(lca_id)>-1){ //Agency) { //37 agency, 46 arthamas, 52 Agency Arthamas (Deddy 22 Feb 2012)
			flagTopup2007 = false;
		//Yusuf (8/7/08) Proses Komisi Cross-Selling
		}else if(lca_id.equals("55")) {
			flagTopup2007 = true; //true menandakan > 2007
		}else if(lca_id.equals("58")){
			flagTopup2007 = true;
		}else if(lca_id.equals("60")) {
			flagTopup2007 = false; //true menandakan > 2007
		}else if(lca_id.equals("62")){
//			prosesKomisiMNC(spaj,currentUser,errors,1);
			flagTopup2007 = false;
		}else if(lca_id.equals("68")){//akan diupdate memakai flag, dan lca_id tetap seperti agency existing
			flagTopup2007 = false;
		}else if(Integer.parseInt(prodDate) > 2006 && !ls_region.startsWith("42")) {
			//}else if(Integer.parseInt(prodDate) > 1980 || Integer.parseInt(prodDate) == 0) {
				flagTopup2007 = true; //true menandakan > 2007
		}
		

		String businessId = FormatString.rpad("0", this.uwDao.selectBusinessId(reg_spaj), 2);
		n_prod produk = (n_prod) Class.forName("produk_asuransi.n_prod_"+businessId).newInstance();
		businessId = FormatString.rpad("0", businessId, 3);
		
		SpajBill spajBill = new SpajBill();
		int li_persen_bayar = 0;
		Date ldt_next_bill; Date ldt_end_bill;
		
		Premi premi = this.uwDao.selectPremiTertanggung(reg_spaj);
		if(premi==null || premi.getNama_pemegang()==null){
			errors.reject("payment.namaPemegangKosong");
			throw new Exception(errors);
		}
				
		int topupBerkala;
		if(products.stableLink(businessId)) {
			topupBerkala=this.uwDao.validationStableLink(reg_spaj);
		}else {
			topupBerkala=this.uwDao.validationTopup(reg_spaj);
		}
		
		/** 4. Proses Produksi Top Up**/
		//insert komisi topup excellink
		if(products.unitLink(businessId)){
			List<Integer> daftarMuKe = uwDao.selectMuKe(reg_spaj);
			
			if(topupBerkala>=1){
				
		/** 5. Proses Komisi Top Up Individu, komisi refferall BII yang topup **/
				//Excellink karyawan (113,138,139) tidak ada komisi
				if("113, 138, 139".indexOf(businessId)<0){
					if(logger.isDebugEnabled())logger.debug("~ PROSES KOMISI TOPUP INDIVIDU untuk SPAJ " + reg_spaj);
					if(flagTopup2007) {
						for(Integer d : daftarMuKe) {
							if(!this.komisi.prosesKomisiTopUpIndividu2007(reg_spaj, errors, currentUser, d)){
								errors.reject("payment.komisiTuExcellinkFailed");
								throw new Exception(errors);
							}
						}
					}else {
						for(Integer d : daftarMuKe) {
							if(!this.komisi.prosesKomisiTopUpIndividu(reg_spaj, errors, currentUser, d)){
								errors.reject("payment.komisiTuExcellinkFailed");
								throw new Exception(errors);
							}
						}
					}
				}
			}
		}
		if(errors.getErrorCount()>0) {
			throw new Exception(errors);
		}

	}
		
	//public methods
	public Map transferKomisiToFilling(String spaj, int tahun, int premi, String lus_id, String palembang) throws DataAccessException{
		Map info = this.financeDao.selectInfoTransferFilling(spaj, tahun, premi);
		String region = (String) info.get("REGION");
		if(region.equals("0914")) return wf_transfer_fa(info, spaj, tahun, premi, lus_id); 
		else return wf_transfer(info, spaj, tahun, premi, lus_id, palembang);
	}

	public String transferBanyakPrintPolisKeFilling(String spaj, User currentUser) throws DataAccessException{
		if(uwDao.validationPositionSPAJ(spaj) != 6) {
			return "POSISI POLIS BUKAN DI 6!";
		}
		
		int li_lspd = 99;
		String ls_ket = "FILLING";
		
		uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "TRANSFER KE "+ls_ket, spaj, 0);
		
		this.uwDao.updateMst_billing(spaj, null, null, null, new Integer(1), new Integer(1), new Integer(li_lspd), null, null);
		
		String businessId = this.uwDao.selectBusinessId(spaj);
		if(products.unitLink(businessId)) {
			int li_ke = 0;
			Integer li_topup = this.uwDao.validationTopup(spaj);
			if(li_topup!=null)if(li_topup>=1) {
				li_ke=2;
				this.uwDao.updateMst_billing(spaj, null, null, null, 1, 2, li_lspd, null, null);
			}
			if(this.uwDao.validationTopup3(spaj) != null) {
				this.uwDao.updateMst_billing(spaj, null, null, null, 1, 3, li_lspd, null, null);
			}
			this.uwDao.updateMstUlinkTransferTTP(spaj, li_ke);
			this.uwDao.updateMstTransUlinkTransferTTP(spaj, li_ke);
			
		}

		this.uwDao.updatePosisiMst_policy(spaj, new Integer(li_lspd));
		this.uwDao.updatePosisiMst_insured(spaj, li_lspd);
		
		return "SUKSES!";
	}
	
	public String transferPrintPolisToFillingAtauTandaTerimaPolis(String spaj, User currentUser, int isInputanBank) throws DataAccessException {
		
		if(uwDao.validationPositionSPAJ(spaj) != 6) {
			return null;
		}
		
		int li_lspd, li_lspd_billing;
		String ls_ket;
		Integer li_jum = this.uwDao.selectSpajCancel(spaj);
		String businessId = this.uwDao.selectBusinessId(spaj);
		Integer businessNumber = this.uwDao.selectBusinessNumber(spaj);
		
		//tambahan validasi untuk produk simas prima jikalau sudah di akseptasi (khusus/tidak) langsung ke tanda terima/filling
		//15/02/2008 -- Yusuf
		//jn bank =2 bahwa polis tersebut produk simas prima
		//lssa_id =10 akseptasi khusus
		
		//20 feb 09 - yusuf - tidak hanya produk simas prima, melainkan seluruh produk save dan stable link
		
		Integer akseptasiNormal = uwDao.selectCountProductSimasPrimaAkseptasiKhusus(spaj, 1,5, isInputanBank);
		Integer akseptasiKhusus = uwDao.selectCountProductSimasPrimaAkseptasiKhusus(spaj, 1,10, isInputanBank);
		String lcaid = uwDao.selectCabangFromSpaj(spaj);
		String lwk_id = uwDao.selectWakilFromSpaj(spaj);
		Product detailProduk = akseptasiDao.selectMstProductInsuredDetail(spaj, Integer.parseInt(businessId), businessNumber);
		List<MedQuest> mq = uwDao.selectMedQuest(spaj,null);
		Integer clear_case = 1;
		for(int i=0;i<mq.size();i++){
			if(mq.get(i).getMsadm_clear_case()==0){
				clear_case = 0;
			}
		}//clear case disini apabila 1 maka transfer ke U/W, apabila 0 transfer ke tanda terima(berarti sudah dihitung oleh U/W untuk extra premi, dll)
		// PROJECT: POWERSAVE & STABLE LINK
		//18/9/2012 - Anta (Penambahan kondisi untuk PowerSave Syariah prod 175 sub 2
		if((((isInputanBank == 3 && akseptasiNormal.intValue() == 0 && akseptasiKhusus.intValue() == 0) || (lcaid.equals("58") && currentUser.getLca_id().equals("58") && detailProduk.getMspr_tsi()<= new Double(200000000) && clear_case==1)) && !products.productBsmFlowStandardIndividu(Integer.parseInt(businessId), businessNumber)) || products.productMallLikeBSM(Integer.parseInt(businessId), businessNumber) || ((businessId.equals("175") && businessNumber.intValue() == 2) && akseptasiNormal.intValue() == 0 && akseptasiKhusus.intValue() == 0)) {
		//if((products.powerSave(businessId) || products.stableLink(businessId)) && akseptasiNormal.intValue() == 0 && akseptasiKhusus.intValue() == 0) {
			li_lspd = 2;
			li_lspd_billing = li_lspd;
			ls_ket = "UNDERWRITING";
		}else if(isInputanBank == 2 || (businessId.equals("190") && businessNumber == 9) || (businessId.equals("200") && businessNumber == 7) ) { //endowment 20 dan trerm securitas, smile link ultimate masuk sini
			li_lspd = 99;
			li_lspd_billing = li_lspd;
			ls_ket = "FILLING";
		}else if(businessId.equals("157") ) { //endowment 20 dan trerm securitas
			li_lspd = 4;
			li_lspd_billing = li_lspd;
			ls_ket = "PAYMENT";
			//this.uwDao.wf_ins_bill(spaj,1,1,lsbsId,lsdbsNumber,li_lspd,1,lsDp,lusId,policy,err)
		}else if(li_jum>0) {
			li_lspd = 99;
			li_lspd_billing = li_lspd;
			ls_ket = "FILLING - ENDORSE";
		//Deddy (9 Okt 2015) : Req TRI HANDINI, untuk THE GREAT mengikuti kondisi lama, tidak proses komisi langsung apabila posisi di tanda terima polis. 
//		}else if(uwDao.cekSpajTransferToFilling(spaj) > 0){ // Deddy (21 nop 2014) : efektif 1 nov 2014, tutupan THE GREAT apabila ditransfer, update ke posisi polis Tanda Terima. posisi billing ke proses Komisi.
//			li_lspd = 7;
//			li_lspd_billing = 8;
//			ls_ket = "TANDA TERIMA";
		}else if (businessId.equals("200") && businessNumber == 6){
			li_lspd = 7;
			li_lspd_billing = li_lspd;
			ls_ket = "TANDA TERIMA";
		}else if (lcaid.equals("73") && lwk_id.equals("00") ){ //ERBE DIRECT
			li_lspd = 7;
			li_lspd_billing = li_lspd;
			ls_ket = "TANDA TERIMA";
		}else{
			//Deddy(21/11/2014) - Mulai saat ini, tidak ada transfer ke posisi checking TTP. Hanya ada transfer ke Filling(Apabila komisi tidak ada atau komisi ada namun sudah paid) atau Tanda Terima.
			List<Map> listKomisi = uwDao.selectMstCommissionNewBusiness(spaj, 1, 1);
			if(listKomisi.size()!=0){
				Map komisiperPremi = listKomisi.get(0);
				BigDecimal msco_paid= (BigDecimal) komisiperPremi.get("MSCO_PAID");
				if(msco_paid.intValue()==1){
					li_lspd = 99;
					li_lspd_billing = li_lspd;
					ls_ket = "FILLING";
				}else{
					li_lspd = 7;
					li_lspd_billing = li_lspd;
					ls_ket = "TANDA TERIMA";
				}
			}else{
				li_lspd = 99;
				li_lspd_billing = li_lspd;
				ls_ket = "FILLING - NO COMMISSION.";
			}
		}
		
//		else if( (( (businessId.equals("175") && businessNumber.intValue() == 2) && akseptasiNormal.intValue() >= 1) || 
//					products.productBsmFlowStandardIndividu(Integer.parseInt(businessId), businessNumber) || 
//					((isInputanBank == 2 || isInputanBank == 3) && akseptasiNormal.intValue() >= 1) && !products.productBsmFlowStandardIndividu(Integer.parseInt(businessId), businessNumber) ) || 
//					(businessId.equals("196")&&businessNumber==2)){
//			li_lspd = 99;
//			li_lspd_billing = li_lspd;
//			ls_ket = "FILLING";
//		}else {
//			li_lspd = 7;
//			li_lspd_billing = li_lspd;
//			ls_ket = "TANDA TERIMA";
//		}
			
		boolean isMuamalat = products.muamalat(spaj);
		
		//insert position spaj, khusus muamalat, tidak diinsert disini, karena akan dilakukan 3x transfer yaitu : 
		//dari PAYMENT -> PRINT POLIS -> TANDA TERIMA -> AGENT COMMISSION
		//sehingga insert position spaj nya langsung di langkah terakhir aja, yaitu saat trans dari TANDA TERIMA ke AGENT COMMISSION
		if(!isMuamalat && !products.isLangsungTransferKeKomisi(spaj)) {
			Boolean ok = false;
			do{
				try{
					uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "TRANSFER KE "+ls_ket, spaj, 0);
					ok=true;
				}catch(Exception e){};
			}while (!ok);
		}
		
		if (lcaid.equals("73") && lwk_id.equals("01") ){ //ERBE PACKAGE
			//ERBE PAckage do nothing pada saat update billing, karena blum tentu pembayaran sudah di bayar, proses ini dilakukan oleh sistem lanjutan
		}else{
			this.uwDao.updateMst_billing(spaj, null, null, null, 1, 1, li_lspd_billing, null, null);
		}		

		if(products.stableLink(businessId)) { //stable
			Integer li_topup = this.uwDao.validationStableLink(spaj);
			if(li_topup != null) if(li_topup==2) {
				this.uwDao.updateMst_billing(spaj, null, null, null, 1, 2, li_lspd_billing, null, null);
			}
			
		}else if(products.unitLink(businessId) && !(lcaid.equals("73") && lwk_id.equals("01"))) { //ERBE PACKAGE Tidak Update Billing, di update oleh system EAS
			int li_ke = 0;
			Integer li_topup = this.uwDao.validationTopup(spaj);
			if(li_topup!=null)if(li_topup>=1) {
				li_ke=2;
				this.uwDao.updateMst_billing(spaj, null, null, null, 1, 2, li_lspd_billing, null, null);
			}
			if(this.uwDao.validationTopup3(spaj) != null) {
				this.uwDao.updateMst_billing(spaj, null, null, null, 1, 3, li_lspd_billing, null, null);
			}
			
			if((lcaid.equals("58") && currentUser.getLde_id().equals("11")) || !lcaid.equals("58")){
				this.uwDao.updateMstUlinkTransferTTP(spaj, li_ke);
				this.uwDao.updateMstTransUlinkTransferTTP(spaj, li_ke);
			}
			
		}else if((businessId.equals("196")&&businessNumber==2)){
			this.uwDao.updateMst_billing(spaj, null, null, null, 1, 1, 56, null, null);
		}

		this.uwDao.updatePosisiMst_policy(spaj, new Integer(li_lspd));
		this.uwDao.updatePosisiMst_insured(spaj, li_lspd);
		
		if(uwDao.cekSpajTransferToFilling(spaj) > 0){
			this.uwDao.updateMstInsuredKomisi(spaj, 1, 1,1, currentUser.getLus_id(), false);
		}
		
		return ls_ket;
	}
	
	/*
	public void transferTandaTerimaPolisToKomisiOrFilling(String spaj, String lus_id, Date tanggal) throws DataAccessException {
		int li_pos = 8;
		String ket = " Print Commission ?";
		if ( this.uwDao.selectCekKomisi(spaj) ==0 ) {
			li_pos = 99;
			ket = " Filling ?";
		}
		ket = "TRANSFER KE " + ket;
		int li_ke = 0;
		this.uwDao.updatePosisiMst_insured(spaj, li_pos);
		this.uwDao.insertMst_position_spaj(spaj, lus_id, li_pos, 1, ket);
		if("077, 084, 087, 097, 100, 101, 113, 115, 116, 117, 118, 119, 120, 121, 122, 127, 128, 129, 138, 139, 107, 152, 153".indexOf(
				FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 3))>-1) {
			Integer li_topup = this.uwDao.validationTopup(spaj);
			if(li_topup != null) if(li_topup >= 1) li_ke=2;
		}
		this.uwDao.updateBillingTtp(li_pos, spaj, li_ke2,l);
		this.uwDao.updateMst_policy(spaj, null, new Double(li_pos), null, tanggal);
	}
			*/
	public String transferTandaTerimaPolisToKomisiOrFillingNew(Ttp ttp, User currentUser) throws DataAccessException {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		int li_pos = 0;
		String ket = null;
		String kd_agen=ttp.getMsagId();
		String kd_agenStr= kd_agen.substring(1,1);
		String lca_id = uwDao.selectCabangFromSpaj(ttp.getSpaj());
		//int polisPribadi =uwDao.selectPolicy_pribadi(kd_agen);//Ryan, cek polis pribadi
		if (kd_agenStr==null||kd_agenStr.equals(""))
			kd_agenStr="0";
		
		//Deddy(21/11/2014) - Mulai saat ini, tidak ada transfer ke posisi checking TTP. Hanya ada transfer ke Filling(Apabila komisi tidak ada atau komisi ada namun sudah paid) atau Proses Commission.
		List<Map> listKomisi = uwDao.selectMstCommissionNewBusiness(ttp.getSpaj(), 1, 1);
		if(listKomisi.size()!=0){
			Map komisiperPremi = listKomisi.get(0);
			BigDecimal msco_paid= (BigDecimal) komisiperPremi.get("MSCO_PAID");
			if(msco_paid.intValue()==1){
				li_pos = 99;
				ket = "TRANSFER KE FILLING (Validasi TTD (ok))";
				prosesTransferKomisiOrFilling(ttp.getSpaj(), li_pos, currentUser.getLus_id(), ttp.getTgl(), ket);
			}else{
				li_pos = 8;
				ket = "TRANSFER KE PRINT COMMISSION (FINANCE) (Validasi TTD (ok)).";
				prosesTransferKomisiOrFilling(ttp.getSpaj(), li_pos, currentUser.getLus_id(), ttp.getTgl(), ket);
				Integer totalNewBusiness = uwDao.selectCountMstBillingNB(ttp.getSpaj());
				for(int i=0; i<totalNewBusiness;i++){
					listKomisi = uwDao.selectMstCommissionNewBusiness(ttp.getSpaj(), 1, i+1);
					for(int j=0;j<listKomisi.size();j++){
						komisiperPremi = listKomisi.get(j);
						BigDecimal msco_active= (BigDecimal) komisiperPremi.get("MSCO_ACTIVE");
						msco_paid= (BigDecimal) komisiperPremi.get("MSCO_PAID");
						if(msco_active.intValue()==0 && msco_paid.intValue()==0){
							uwDao.updateMscoAktifKomisiNewBusiness(ttp.getSpaj(), 1, j+1);
						}
					}
				}
			}
		}else{
			li_pos = 99;
			ket = "TRANSFER KE FILLING - NO COMMISSION. (Validasi TTD (ok))";
			prosesTransferKomisiOrFilling(ttp.getSpaj(), li_pos, currentUser.getLus_id(), ttp.getTgl(), ket);
		}
		
		
//		if(ttp.getProses()==1){//filling
//			li_pos = 99;
//			ket = "TRANSFER KE FILLING.";
//			prosesTransferKomisiOrFilling(ttp.getSpaj(), li_pos, currentUser.getLus_id(), ttp.getTgl(), ket);
//		}else if(ttp.getProses()==2 || ttp.getProses()==3 || 
//				ttp.getProses()==4 || ttp.getProses()==5){//finance (print commission)
//			li_pos = 8;
//			ket = "TRANSFER KE PRINT COMMISSION (FINANCE).";
//			prosesTransferKomisiOrFilling(ttp.getSpaj(), li_pos, currentUser.getLus_id(), ttp.getTgl(), ket);
//			Integer totalNewBusiness = uwDao.selectCountMstBillingNB(ttp.getSpaj());
//			for(int i=0; i<totalNewBusiness;i++){
//				List<Map> listKomisi = uwDao.selectMstCommissionNewBusiness(ttp.getSpaj(), 1, i+1);
//				for(int j=0;j<listKomisi.size();j++){
//					Map komisiperPremi = listKomisi.get(j);
//					BigDecimal msco_active= (BigDecimal) komisiperPremi.get("MSCO_ACTIVE");
//					BigDecimal msco_paid= (BigDecimal) komisiperPremi.get("MSCO_PAID");
//					if(msco_active.intValue()==0 && msco_paid.intValue()==0){
//						uwDao.updateMscoAktifKomisiNewBusiness(ttp.getSpaj(), 1, j+1);
//					}
//				}
//			}
//		}else if(ttp.getProses()==6){//agent commission
//
//			//override paksa untuk cabang2 dibawah, tetep ditrans ke komisi
//			int lca = Integer.parseInt(lca_id);
//			if(lca == 37 || lca == 9 || lca == 42 || lca == 46 || lca == 52) {
//				li_pos = 8;
//				ket = "TRANSFER KE PRINT COMMISSION (FINANCE).";
//				prosesTransferKomisiOrFilling(ttp.getSpaj(), li_pos, currentUser.getLus_id(), ttp.getTgl(), ket);
//				return ket;
//			}
//			
//			li_pos = 9;
//			ket = "TRANSFER KE PROSES CHECKING TTP (DEDUCT).";
//			this.uwDao.updatePosisiMst_insured(ttp.getSpaj(), li_pos);
//			uwDao.insertMstPositionSpaj(currentUser.getLus_id(), ket, ttp.getSpaj(), 0);
//			this.uwDao.updateBillingTtp(li_pos, ttp.getSpaj());
//			Policy policy=this.uwDao.selectDw1Underwriting(ttp.getSpaj(),7,1);
//			if(policy.getMspo_date_ttp()==null){
//				this.uwDao.updateMst_policy(ttp.getSpaj(), new Double(li_pos), null, ttp.getTgl());
//				this.uwDao.saveMstTransHistory(ttp.getSpaj(), "tgl_ttp", ttp.getTgl(), null, null);
//			}else{
//				this.uwDao.updateMst_policy(ttp.getSpaj(), new Double(li_pos), null, null);
//			}
//			//email 1
			int lca = Integer.parseInt(lca_id);
			/*if(lca != 37 && lca != 52 && lca != 42){
					String pesan="KOMISI BELUM DAPAT DIBAYAR KARENA ADA DEDUCT (A).";
					List lsSpaj=new ArrayList();
					lsSpaj.add(ttp.getSpaj());
					emailCheckingTtp(lsSpaj, pesan, currentUser,1,"ADA DEDUCT");
			}*/
//			
//		}else if(ttp.getProses()==7){//ttp dalam bentuk fax
//			li_pos=7;
//			ket="TERIMA TTP DALAM BENTUK FAX.";
//			uwDao.insertMstPositionSpaj(currentUser.getLus_id(), ket + " (Update Tgl Komisi)", ttp.getSpaj(), 0);
//			this.uwDao.updateMstInsuredKomisi(ttp.getSpaj(),1,2,null, currentUser.getLus_id(), false);
//			Policy policy=this.uwDao.selectDw1Underwriting(ttp.getSpaj(),7,1);
//			if(policy.getMspo_date_ttp()==null)
//				this.uwDao.updateMst_policy(ttp.getSpaj(), null, null, ttp.getTgl());
//		}else if((ttp.getProses()==8)||(ttp.getProses()==0)){//transfer ttp langsung ke agency
//			li_pos = 9;
//			int lspd_id=0;
//			if (ttp.getProses()==8){
//				ket = "LANGSUNG TRANSFER KE PROSES CHECKING TTP (DEDUCT).";
//				this.uwDao.updatePosisiMst_insured(ttp.getSpaj(), li_pos);
//				uwDao.insertMstPositionSpaj(currentUser.getLus_id(), ket, ttp.getSpaj(), 0);
//				lspd_id=7;
//			} 
//			if (ttp.getProses()==0){
//				ket = "TRANSFER KE PROSES CHECKING TTP";
//				this.uwDao.updatePosisiMst_insured(ttp.getSpaj(), li_pos);
//				uwDao.insertMstPositionSpaj(currentUser.getLus_id(), ket, ttp.getSpaj(), 0);
//				lspd_id=9;
//			} 
//			Policy policy=this.uwDao.selectDw1Underwriting(ttp.getSpaj(),lspd_id,1);
//			if(policy == null)
//				this.uwDao.updateMst_policy(ttp.getSpaj(), new Double(li_pos), null, ttp.getTgl());
//			else if(policy.getMspo_date_ttp()==null)
//				this.uwDao.updateMst_policy(ttp.getSpaj(), new Double(li_pos), null, ttp.getTgl());
//			else
//				this.uwDao.updateMst_policy(ttp.getSpaj(), new Double(li_pos), null, null);
//			
//			if (kd_agenStr.equals("8")){
//					logger.info("Biarin lewat aja");
//			}else{
//				int lca = Integer.parseInt(lca_id);
//			//email 1
				/*if(lca !=37 && lca != 52){
					String pesan="Komisi belum dapat dibayar karena ada deduct (B).";
					List lsSpaj=new ArrayList();
					lsSpaj.add(ttp.getSpaj());
					emailCheckingTtp(lsSpaj, pesan, currentUser,1,"Ada deduct");
				}*/
//			}
//		}else if(ttp.getProses()==9){//lisensi expired
//			
//			//override paksa untuk cabang2 dibawah, tetep ditrans ke komisi
//			int lca = Integer.parseInt(lca_id);
//			if(lca == 37 || lca == 9 || lca == 42 || lca == 46 || lca == 52) {
//				li_pos = 8;
//				ket = "TRANSFER KE PRINT COMMISSION (FINANCE).";
//				prosesTransferKomisiOrFilling(ttp.getSpaj(), li_pos, currentUser.getLus_id(), ttp.getTgl(), ket);
//				return ket;
//			}
//						
//			li_pos = 9;
//			ket = "TRANSFER KE PROSES CHECKING TTP (DEDUCT).";
//			this.uwDao.updatePosisiMst_insured(ttp.getSpaj(), li_pos);
//			uwDao.insertMstPositionSpaj(currentUser.getLus_id(), ket, ttp.getSpaj(), 0);
//			Policy policy=this.uwDao.selectDw1Underwriting(ttp.getSpaj(),7,1);
//			if(policy.getMspo_date_ttp()==null)
//				this.uwDao.updateMst_policy(ttp.getSpaj(), new Double(li_pos), null, ttp.getTgl());
//			else
//				this.uwDao.updateMst_policy(ttp.getSpaj(), new Double(li_pos), null, null);
//			
//			//email 1
//			String pesan="KOMISI BELUM DAPAT DIBAYAR KARENA AGEN BELUM BERLISENSI / LISENSI TELAH EXPIRED.";
//			List lsSpaj=new ArrayList();
//			lsSpaj.add(ttp.getSpaj());
//			emailCheckingTtp(lsSpaj, pesan, currentUser,1,"AGEN BELUM BERLISENSI / LISENSI TELAH EXPIRED.");
//		}
		
		String errorsertifikat = uwDao.cekSetifikatAgen(ttp.getSpaj());
		String errorKdRek = uwDao.cekKdBankRekAgen2(ttp.getSpaj());
		String errorRekening = uwDao.cekRekAgen2(ttp.getSpaj());
		String emailCabang = uwDao.selectEmailCabangFromSpaj(ttp.getSpaj());
		Map infoAgen = uwDao.selectEmailAgen(ttp.getSpaj());
		String emailAgen = (String) infoAgen.get("MSPE_EMAIL");
		String alamat = "";
		String cc = "";
		String bcc="";
		
		//validasi email agen
		if(emailAgen!=null) {
			if(!emailAgen.trim().equals("")) {
				if(emailAgen.toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
					cc =emailAgen;
					
				}
			}
		}
		//validasi email cabang
		if(emailCabang!=null) {
			if(!emailCabang.trim().equals("")) {
				if(emailCabang.toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
					alamat= emailCabang;
					
				}
			}
		}
		
		if((!errorRekening.equals(""))||(!errorKdRek.equals(""))||(!errorsertifikat.equals(""))){
			String kesiapa=null;
			if( (lca_id.equals("08")) || (lca_id.equals("42"))|| (lca_id.equals("62")) || (lca_id.equals("67")) ){
				kesiapa=props.getProperty("agensysTTp.emails");
			}else{
				kesiapa=props.getProperty("agensysTTp1.emails");
			}
			
			li_pos=9; //ke Finance

			if (emailAgen!=null||emailCabang!=null){
				alamat= alamat + ";" +cc;
			}	
			
			if(errorRekening.equals("")){
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, new Date(), null,
	        			false, props.getProperty("admin.ajsjava"), emailAgen.split(";"), alamat.split(";"), null, 
//				email.send(false, props.getProperty("admin.ajsjava"), props.getProperty("admin.dian").split(";"), alamat.split(";"), null, 
	        		"[E-Lions] Rekening Agen "+errorRekening+" Kosong", 
					"Harap cek rekening agen: \n\n"  +errorRekening+" karena rekeningnya masih kosong, sehingga komisinya tidak dapat diproses. Terima kasih."+
					"\n\nNo SPAJ  "  + ttp.getSpaj() +
					"\n\nINFO: Pesan ini dikirim secara otomatis melalui sistem E-LIONS.", null, ttp.getSpaj());
//				uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "TRANSFER KE PROSES CHECKING TTP (REK BKN BSM/REK KOSONG)", ttp.getSpaj(), 0);
			}else if  (errorKdRek.equals("")) {
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, new Date(), null,
		        		false, props.getProperty("admin.ajsjava"), kesiapa.split(";")/*props.getProperty("agensysTTp.emails").split(";")*/, alamat.split(";"),  null, 
//				email.send(false, props.getProperty("admin.ajsjava"), props.getProperty("admin.dian").split(";"),  alamat.split(";"), null, 
		        		"[E-Lions] Rekening Agen "+errorKdRek+" Bukan Rekening Bank Sinarmas", 
						"Harap cek rekening agen: \n\n"  +errorKdRek+" karena rekeningnya bukan rekening Bank Sinar Mas, sehingga komisinya tidak dapat diproses. Terima kasih."+
						"\n\nNo SPAJ  "  + ttp.getSpaj() +
						"\n\nINFO: Pesan ini dikirim secara otomatis melalui sistem E-LIONS.", null, ttp.getSpaj());
//				uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "TRANSFER KE PROSES CHECKING TTP (REK BKN BSM/REK KOSONG)", ttp.getSpaj(), 0);
			}else if  (errorsertifikat.equals("")) {
//		        EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
//		    				null, 0, 0, new Date(), null,
//		    				false, props.getProperty("admin.ajsjava"), kesiapa.split(";") /*props.getProperty("agensysTTp.emails").split(";")*/, alamat.split(";"),  null, 
//		        		"[E-Lions] Sertifikat Agen "+errorsertifikat+" Belum berlisensi", 
//						"Harap cek Sertifikat agen: \n\n"  +errorsertifikat+" karena sertifikat tidak berlisensi, sehingga komisinya tidak dapat diproses. Terima kasih."+
//						"\n\nNo SPAJ  "  + ttp.getSpaj() +
//						"\n\nINFO: Pesan ini dikirim secara otomatis melalui sistem E-LIONS.", null, ttp.getSpaj());
//				uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "TRANSFER KE PROSES CHECKING TTP (SERTIFIKAT TDK BERLISENSI)", ttp.getSpaj(), 0);				
			}
		}
		
	return ket;
	}
	
	public String transferTandaTerimaPolisToKomisiOrFilling30Hari(User currentUser,BindException err) throws Exception{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		//tambahan dr ingrid 18012008 mst_policy.mspo_jenis_terbit=1 (soft copy polis)
		List daftarSPAJ = this.uwDao.selectDaftarSPAJ("7", 1,null,1);
		Date sysDate = this.commonDao.selectSysdate();
		String hasil="";
		int hitungFilling=0;
		int hitungFinance=0;
		int hitungRekKosong=0;
		List lsAgency=new ArrayList();
		
		String cabangWorkSite="08,42,62,67";
		
		for(int i=0; i<daftarSPAJ.size(); i++) {
			Map map = (HashMap) daftarSPAJ.get(i);
			String spaj = (String) map.get("REG_SPAJ");
			String ket=null;
			Date tgl_print = this.uwDao.selectPrintDate(spaj);
			String cabang=(String)map.get("LCA_ID");
			Integer a=cabangWorkSite.indexOf((String)map.get("LCA_ID"));
			if(tgl_print!=null && a<0 ){
				Date ldt_tgl_ttp=FormatDate.add(tgl_print, Calendar.MONTH, 1);
				if(FormatDate.dateDifference(tgl_print, sysDate, false) >= 30) { //tanggal print lebih dari 30 hari yg lalu
					Integer pos=null;
					Ttp ttp=new Ttp();
					ttp.setSpaj(spaj);
					ttp.setTgl(ldt_tgl_ttp);
					//
					Map tertanggung= this.uwDao.selectTertanggung(spaj);
					ttp.setLcaId((String)tertanggung.get("LCA_ID"));
					ttp.setFlagKomisi((Integer)tertanggung.get("MSTE_FLAG_KOMISI"));
					ttp.setMsagId(this.uwDao.selectAgenFromSpaj(spaj));
					ttp.setProses(prosesCheckingTtp(ttp,err));
		
					String errorRekening = uwDao.cekRekAgen2(spaj);
					
					String[] to = new String[]{"wenny@sinarmasmsiglife.co.id;hisar@sinarmasmsiglife.co.id;"};
					
					if(errorRekening.equals("")) { //rekening kosong
//						TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
						//Deddy - Sementara ditutup dulu sampai sudah fix.
//						EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
//								null, 0, 0, new Date(), null,
//			        			false, props.getProperty("admin.ajsjava"), to , null, null, 
//								"[Sheduler E-Lions] Rekening Agen "+errorRekening+" Kosong",
//								"Harap cek rekening agen: \n\n"+errorRekening+ " karena rekeningnya masih kosong, sehingga komisinya tidak dapat diproses. Terima kasih."+
//								"\n\nNo SPAJ: "+spaj+""+
//								"\n\nINFO: Pesan ini dikirim secara otomatis melalui sistem E-LIONS.", null);
						
						hitungRekKosong++;
					}else if(ttp.getProses()==1){//filling
						pos = 99;
						ket = "TRANSFER TANDA TERIMA > 30 HARI, KE FILLING.";
						transfer30HariFiananceNFilling(spaj, currentUser.getLus_id(), pos, ket, ttp.getTgl());
						hitungFilling++;
					}else if(ttp.getProses()==2 || ttp.getProses()==3 || 
							ttp.getProses()==4 || ttp.getProses()==5){//finance (print commission)
						pos = 8;
						ket = "TRANSFER TANDA TERIMA > 30 HARI, KE PRINT COMMISSION (FINANCE).";
						transfer30HariFiananceNFilling(spaj, currentUser.getLus_id(), pos, ket, ttp.getTgl());
						Integer totalNewBusiness = uwDao.selectCountMstBillingNB(ttp.getSpaj());
						for(int k=0; k<totalNewBusiness;k++){
							List<Map> listKomisi = uwDao.selectMstCommissionNewBusiness(ttp.getSpaj(), 1, k+1);
							for(int j=0;j<listKomisi.size();j++){
								Map komisiperPremi = listKomisi.get(j);
								BigDecimal msco_active= (BigDecimal) komisiperPremi.get("MSCO_ACTIVE");
								BigDecimal msco_paid= (BigDecimal) komisiperPremi.get("MSCO_PAID");
								if(msco_active.intValue()==0 && msco_paid.intValue()==0){
									uwDao.updateMscoAktifKomisiNewBusiness(ttp.getSpaj(), 1, k+1);
								}
							}
						}
						hitungFinance++;
					}else if(ttp.getProses()==6){//agent commission
						pos= 9;
						ket = "TRANSFER TANDA TERIMA > 30 HARI, KE PROSES CHECKING TTP (AGENCY).";
						this.uwDao.updatePosisiMst_insured(ttp.getSpaj(), pos);
						uwDao.insertMstPositionSpaj(currentUser.getLus_id(), ket, ttp.getSpaj(), 0);
						Policy policy=this.uwDao.selectDw1Underwriting(ttp.getSpaj(),7,1);
						if(policy.getMspo_date_ttp()==null){
							this.uwDao.updateMst_policy(ttp.getSpaj(), new Double(pos), null, ttp.getTgl());
							this.uwDao.saveMstTransHistory(ttp.getSpaj(), "tgl_ttp", ttp.getTgl(), null, null);
						}else{
							this.uwDao.updateMst_policy(ttp.getSpaj(), new Double(pos), null, null);
						}
						lsAgency.add(ttp.getSpaj());
					}
					
					
				}
			}
		}
		if(hitungFilling>0){
			hasil+="\n*. (Transfer ke Filling) adalah  "+hitungFilling;
		}
		if(hitungFinance>0){
			hasil+="\n*. (Transfer ke Finance) adalah  "+hitungFinance;
		}
		if(hitungRekKosong>0) {
			hasil+="\n*. (Rekening Agen Kosong) adalah  "+hitungRekKosong;
		}
		//email daftar polis yang ditransfer ke AS
		if(lsAgency.size()>0){
			String pesan="DAFTAR KOMISI BELUM DAPAT DIBAYAR KARENA BELUM PUNYA POLIS (C).";
			emailCheckingTtp(lsAgency, pesan, currentUser,1,"AGEN BELUM PUNYA POLIS (C)");
			hasil+="\n*. (Transfer ke Agency Support )adalah  "+lsAgency.size();
		}
		hasil="Jumlah SPAJ yang diproses 30 hari "+hasil;
		return hasil;
	}
	
	public void transfer30HariFiananceNFilling(String spaj, String lusId, Integer pos, String ket, Date tglTtp){

		this.uwDao.updatePosisiMst_insured(spaj, pos);
		this.uwDao.updateMstInsuredKomisi(spaj, 1, 1,1, lusId, false);
		uwDao.insertMstPositionSpaj(lusId, ket+ " (Update Tgl Komisi)", spaj, 0);
		//tambahin filter msbi_paid=1 di update mst_billling FR:YUSUF 22102007
		this.uwDao.updateBillingTtp(pos, spaj);
		Policy policy=this.uwDao.selectDw1Underwriting(spaj,7,1);
		/*if(policy.getMspo_date_ttp()==null)
			this.uwDao.updateMst_policy(spaj, null, new Double(8), null, tglTtp);
		else*/
		this.uwDao.updateMst_policy(spaj, new Double(8), null, null);
	}
	
	/**Fungsi: 	Untuk melakukan proses checking ttp yang sudah punya polis dan 
	 * 			komisi > 500, serta cabang worksite seperti (42,09,37).
	 * 			Jikalau hasil prosesnya transfer ke agency support maka akan di email
	 * 			secara automatis oleh sistem, ke agency.
	 * @param	Ttp ttp
	 * @return Integer
	 */
	public Integer prosesCheckingTtp(Ttp ttp,BindException err)throws Exception {
	//	TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		Integer proses=0;
		Integer countComm=this.uwDao.selectCekKomisi(ttp.getSpaj());
//		countComm=1;
		if ( countComm ==0 ) {//gak ada komisi
			ttp.setInfo("Tidak Punya Komisi, Transfer Ke Filling?");
			proses=1;
		}else{//ada komisi
			List tmp = this.uwDao.selectAgenCekKomisi(ttp.getMsagId());
			Date ldt_birth = new Date();
			String ls_nama = "";
			String msagId = "";
			Double ldec_kom = new Double(0);
			int li_polis = 0 ;
			if( tmp.size() > 0 ) {
				ldt_birth = (Date) ((Map) tmp.get(tmp.size()-1)).get("MSPE_DATE_BIRTH");
				ls_nama = (String) ((Map) tmp.get(tmp.size()-1)).get("MCL_FIRST");
				msagId= (String) ((Map) tmp.get(tmp.size()-1)).get("MSAG_ID");
				ldec_kom = (Double) ((Map) tmp.get(tmp.size()-1)).get("KOMISI");
				if(ldt_birth==null){
					err.reject("","Tanggal Lahir Agen "+ls_nama+"("+msagId+") TIdak Ada Silahkan DIlengkapi");
					return 0;
				}	
				int spasi=ls_nama.indexOf(' ');
				int titik=ls_nama.indexOf('.');
				int koma=ls_nama.indexOf(',');
				int pjg=ls_nama.length();
				if(spasi>0)
					ls_nama=ls_nama.substring(0,spasi);
				else if(titik>0)
					ls_nama=ls_nama.substring(0,titik);
				else if(koma>0)
					ls_nama=ls_nama.substring(0,koma);
				
				li_polis = this.uwDao.selectJumlahPolisAgen(ls_nama, FormatDate.toString(ldt_birth));
			}
			
			//bancass, worksite, agency, dan hybrid, langsung transfer ke finance
			//String cabang="37,09,42";
			String cabang="09, 37, 42, 46, 52";
			
			//request mba wesni - 23 oct 08, sudah tidak perlu diblokir
			//String agenWorksiteYangDiBlokir = "800218, 800129, 800167, 800233, 800242";
			String agenWorksiteYangDiBlokir = "";
			
			String lca_id = uwDao.selectCabangFromSpaj(ttp.getSpaj());
			String nmCabang= uwDao.selectNmCabangFromSpaj(ttp.getSpaj());
			
			int a = cabang.indexOf(lca_id);
			int b = agenWorksiteYangDiBlokir.indexOf(msagId);
			
//			if(a >= 0 && b == -1){// yang lama
			if(a >= 0 ){
				ttp.setInfo("Cabang BANCASSURANCE/AGENCY/HYBRID/WORKSITE. Langsung Transfer ke Finance?");
				proses=5;
			}else{
//				ldec_kom=new Double(1000000);
				if(ttp.getFlagKomisi()==1 ){//proses transfer balikan dari Agency
					ttp.setInfo("Ada Komisi, Anda Ingin Langsung Transfer ke Finance?");
					proses=2;
				}else if(ttp.getFlagKomisi()==2){//
					ttp.setInfo("Ada Komisi, TTP Asli telah Ada dan Anda Ingin Langsung Transfer ke Finance?");
					proses=3;
				}else if(b > -1) {
					ttp.setInfo("Agen Worksite ini komisinya harus di-deduct \\nAnda Ingin Transfer Polis ke Agent Commission(Agency Support)?");
					proses=6;
				}else if(ldec_kom.doubleValue()<500000 || li_polis>0){
					ttp.setInfo("Agen sudah punya polis atau komisi < 500 rb,\\nYakin Transfer ke Finance?");
					proses=4;
					String emailAdd1= uwDao.selectEmailCabangTtp(ttp.getSpaj());
					String emailAdd= (emailAdd1);
					
//					String[] to=emailAdd.split(";");
					
					if (emailAdd1== null||emailAdd1.equals("")){
//						email.send(false, props.getProperty("admin.ajsjava"), props.getProperty("agensys.emails").split(";"), null, null, 
//								"[E-Lions] Agen "+ls_nama+ "Kode Agen" +msagId+ " Belum memiliki Polis Pribadi",
//								"Harap agen: \n\n" + "Nama Agen: "+ls_nama+ "\n\n" + "Kode Agen :" +msagId+  "\n\n" +"Cabang : " +nmCabang+ "\n\n" + "No. SPAJ:" +ttp.getSpaj()+
//								"\n\n" +"Jika sudah punya polis pribadi konfirmasi ke Agency Support No polisnya. Terima kasih."+
//								"\n\nINFO: Pesan ini dikirim secara otomatis melalui sistem E-LIONS.", null);
					}else{
//						email.send(false, props.getProperty("admin.ajsjava"), emailAdd.split(";"), null, null, 
//								"[E-Lions] Agen "+ls_nama+ "Kode Agen" +msagId+ " Belum memiliki Polis Pribadi",
//								"Harap agen: \n\n" + "Nama Agen: "+ls_nama+ "\n\n" + "Kode Agen :" +msagId+  "\n\n" +"Cabang : " +nmCabang+ "\n\n" + "No. SPAJ:" +ttp.getSpaj()+
//								"\n\n" +"Jika sudah punya polis pribadi konfirmasi ke Agency Support No polisnya. Terima kasih."+
//								"\n\nINFO: Pesan ini dikirim secara otomatis melalui sistem E-LIONS.", null);
					}


				}else if(uwDao.selectCekAgenExpired(msagId, new Date())==0) {
					ttp.setInfo("Agen belum berlisensi atau sertifikasi expired \\nAnda Ingin Transfer Polis ke Agent Commission(Agency Support)?");
					proses = 9;
				}else{//transfer ke Proses Checking TTP{
					ttp.setInfo("Agen tidak punya polis atau komisi > 500 rb \\nAnda Ingin Transfer Polis ke Agent Commission(Agency Support)?");
					proses=6;
				}
			}
		
		}
	
		return proses;
	}

	/**Fungsi:	Untuk memproses transfer komisi atau transfer ke filling dari Tanda Terima Polis
	 * @param 	String spaj,Integer li_pos,String lus_id,Date tanggal,String ket
	 * @author	Ferry Harlim
	 */
	public void prosesTransferKomisiOrFilling(String spaj,Integer li_pos,String lus_id,Date tanggal,String ket){
		int li_ke2 = 0,li_ke3=0;
		this.uwDao.updatePosisiMst_insured(spaj, li_pos);
		this.uwDao.updateMstInsuredKomisi(spaj, 1, 1,1, lus_id, false);
		String lsbs = uwDao.selectBusinessId(spaj);
		String lca_id = checklistDao.selectLcaIdBySpaj(spaj);
		String lsdbs = uwDao.selectLsdbsNumber(spaj);
		
		if("187,203".indexOf(lsbs.toString())>-1){
			Boolean ok = false;
			do{
				try{
					if(lsdbs.equals("5")){
						uwDao.insertMstPositionSpaj(lus_id, ket, spaj, 0);
					}else{
						uwDao.insertMstPositionSpaj(lus_id, ket + " (Update Tgl Komisi)", spaj, 0);
					}
					
					ok=true;
				}catch(Exception e){};
			}while (!ok);
			//uwDao.insertMstPositionSpaj(lus_id, ket + " (Update Tgl Komisi)", spaj, 1000);
		}else{
			Boolean ok = false;
			do{
				try{
					if(lca_id.equals("55")){
						uwDao.insertMstPositionSpaj(lus_id, ket, spaj, 0);
					}else{
						uwDao.insertMstPositionSpaj(lus_id, ket + " (Update Tgl Komisi)", spaj, 0);
					}
					ok=true;
				}catch(Exception e){};
			}while (!ok);
		}
		this.uwDao.updateBillingTtp(li_pos, spaj);
		if(products.stableLink(lsbs)) this.uwDao.updateBillingTtpStableLink(li_pos, spaj);
		else this.uwDao.updateBillingTtp(li_pos, spaj);
		
		//
		Policy policy=this.uwDao.selectDw1Underwriting(spaj,null,1);
		if(policy.getMspo_date_ttp()==null){
			this.uwDao.updateMst_policy(spaj, new Double(li_pos), null, tanggal);
			this.uwDao.saveMstTransHistory(spaj, "tgl_ttp", tanggal, null, null);
		}else{
			this.uwDao.updateMst_policy(spaj, new Double(li_pos), null, null);
		}
	}
	/**Fungsi:	Untuk memproses transfer komisi ke Agent commission (lspd_id=8)
	 * @param 	String spaj,Integer li_pos,String lus_id,Date tanggal,String ket
	 * @author	Ferry Harlim
	 */
	/*
	public void prosesTransferKomisi(String spaj,Integer li_pos,String lus_id,Date tanggal,String ket){
		int li_ke2 = 0,li_ke3=0;
		this.uwDao.updatePosisiMst_insured(spaj, li_pos);
		this.uwDao.insertMst_position_spaj(spaj, lus_id, li_pos, 1, ket);
		if("077, 084, 087, 097, 100, 101, 113, 115, 116, 117, 118, 119, 120, 121, 122, 127, 128, 129, 138, 139, 107, 152, 153, 159, 160".indexOf(
				FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 3))>-1) {
			Integer li_topup = this.uwDao.validationTopup(spaj);
			if(li_topup != null) if(li_topup >= 1) li_ke2=2;
			
			//premi ke 3 19/04/2007 regina
			if(uwDao.validationTopup3(spaj)!=null)
				li_ke3=3;
		}
		this.uwDao.updateBillingTtp(li_pos, spaj, li_ke2,li_ke3);
		this.uwDao.updateMst_policy(spaj, null, new Double(li_pos), null, null);
	}	*/
	//private methods
	private void wf_trans_reward(String spaj, int tahun, int premi) throws DataAccessException {
		int flagUpload = 0;
		int jumlahReward = this.financeDao.selectJumlahReward(spaj, tahun, premi);
		if(jumlahReward > 0) {
			int kodeBank = this.financeDao.selectBankReward(spaj, tahun, premi);
			if(kodeBank == 45) flagUpload = 1;
			this.financeDao.updateRewards(flagUpload, spaj, tahun, premi);
		}
	}

	private Map wf_transfer(Map info, String spaj, int tahun, int premi, String lus_id, String palembang) throws DataAccessException {
		Map result = new HashMap();

		String lca_id = (String) info.get("LCA_ID");

		//Khusus Palembang
		String msco_id = (String) info.get("MSCO_ID");
		if(palembang.equals("ya")) this.financeDao.insertDeduct(msco_id, new Integer(4), new Date(), new Double(20000), new Double(0), "Potongan Biaya Polis", lus_id);
		
		//Jika komisi new business
		int mspo_lspd_id = ((Integer) info.get("MSPO_LSPD_ID"));
		if(mspo_lspd_id==8) {
			this.uwDao.updatePosisiMst_insured(spaj, 99);
			uwDao.insertMstPositionSpaj(lus_id, "TRANSFER KE FILLING", spaj, 0);
			mspo_lspd_id = 99;
		}
		
		//Looping sebanyak komisi per billing (max sejumlah 4.. RM/AM/UM/EM)
		List daftarKomisi = this.financeDao.selectKomisiAgen(spaj, tahun, premi);
		for(int i=0; i<daftarKomisi.size(); i++) {
			Commission komisi = (Commission) daftarKomisi.get(i);
			
			//Jika RM New Bisnis (level 1) & bukan Agency system & non-sbm
			if(komisi.getLsle_id()==1 && "37,52".indexOf(lca_id)==-1 && komisi.getMsag_sbm()!=1) {
				//jika premi pertama, atau beg_date > 01/01/2000 tidak dapat
				if(komisi.getMsbi_premi_ke()==1) continue;
				Calendar cal = Calendar.getInstance(); cal.set(2000, 1, 1);
				if(FormatDate.dateDifference((Date) info.get("MSTE_BEG_DATE"), cal.getTime(), false)<0) continue;
			}
			
			//UPDATE PAY DATE
			this.financeDao.updatePayDate(lus_id, komisi.getCo_id());
			
			//bancass, skip
			if(komisi.getLca_id().equals("09")) continue;
				
			//Cari apakah ada potongan dplk, sekaligus hitung jumlah komisi - deduct2nya (untuk insert ke upload)
			List daftarDeduct = komisi.getDeduct();
			double dplk = 0;
			double jmlKomisi = komisi.getTotal().doubleValue();
			for(int j=0; j<daftarDeduct.size(); j++) {
				Map temp = (HashMap) daftarDeduct.get(j);
				if(((Integer) temp.get("LSJD_ID")) == 5) dplk = ((Double) temp.get("MSDD_DEDUCT")).doubleValue();
				jmlKomisi -= ((Double) temp.get("MSDD_DEDUCT")).doubleValue();
			}			

			String accountAgent = StringUtils.delete(komisi.getMsag_tabungan(), ".");
			int kodeBank = this.financeDao.validasiRekeningBank(komisi.getMsag_id());

			//flag upload ke lippo (sampai saat ini belum digunakan)
			String flagLippo = props.getProperty("komisi.uploadLippo", "0");
			
			//Apakah agen punya rek di bii (kode 45)
			if(kodeBank == 45) {
				if(accountAgent.length() != 10) {
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					result.put("error", "Maaf, tetapi ada kesalahan nomor tabungan BII pada Agent "+komisi.getMsag_id());
					return result;
				}
				this.financeDao.deleteUpload(komisi.getCo_id());
				this.financeDao.insertUpload(komisi.getCo_id(), "000000", komisi.getMsag_id(), accountAgent, jmlKomisi);
				
				if(dplk>0) {
					String sekuens = this.uwDao.selectGetCounter(37, "01");
					this.uwDao.updateCounter(sekuens, 37, "01");
					double ldec_dplk_pt = komisi.getLpc_dplk_p().doubleValue();
					ldec_dplk_pt = FormatNumber.rounding(new Double(komisi.getTotal().doubleValue() * ldec_dplk_pt / 100), false, 25).doubleValue();
					this.financeDao.insertDplk(sekuens, komisi.getCo_id(), spaj, komisi.getMsag_id(), komisi.getMcl_first(), komisi.getLsle_id(), dplk, ldec_dplk_pt);
				}
				
			//Apabila flag upload ke lippo aktif
			}else if(flagLippo.equals("1")) {
				//Apakah agen punya rek di lippo (kode 52)
				if(kodeBank == 52) {
					if(accountAgent.length() != 11) {
						TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
						result.put("error", "Maaf, tetapi ada kesalahan nomor tabungan Lippo pada Agent "+komisi.getMsag_id());
					}
					this.financeDao.deleteUploadLippo(komisi.getCo_id());
					this.financeDao.insertUploadLippo(komisi.getCo_id(), komisi.getMsag_id(), accountAgent, jmlKomisi);
				//Apabila rek bank lain
				}else {
					this.financeDao.deleteUploadNon(komisi.getCo_id());
					this.financeDao.insertUploadNon(komisi.getCo_id(), komisi.getMsag_id(), jmlKomisi);
				}
				//
			//Apabila rek bank lain
			}else {
				this.financeDao.deleteUploadNon(komisi.getCo_id());
				this.financeDao.insertUploadNon(komisi.getCo_id(), komisi.getMsag_id(), jmlKomisi);
			}
			//
		}

		//Update posisi billing
		this.uwDao.updateMst_billing(spaj, null, null, null, new Integer(tahun), new Integer(premi), new Integer(99), null, null);

		wf_trans_reward(spaj, tahun, premi);
		
		Integer lsbs_id = (Integer) info.get("LSBS_ID");
		
		if("77, 84, 87, 97, 100, 101, 102, 115, 116, 117, 118, 119, 120, 122, 127, 128, 129, 107, 152, 153,217,218".indexOf(lsbs_id.toString())>-1) {
			int flagTopup = ((Integer) info.get("FLAG_TOPUP"));
			if(flagTopup!=1) {
				flagTopup = this.financeDao.validationTopupBilling(spaj);
				if(flagTopup>0) result.put("sukses", "Billing berhasil di-transfer ke Filling. Harap perhatikan bahwa polis ini mempunyai komisi Top-Up");
			}
		}
		if(result.get("sukses")==null) result.put("sukses", "Billing berhasil di-transfer ke Filling.");
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return result;
	}
	
	private Map wf_transfer_fa(Map info, String spaj, int tahun, int premi, String lus_id) throws DataAccessException {
		Map result = new HashMap();
		
		String lca_id = (String) info.get("LCA_ID");
		
		//Jika komisi new business
		int mspo_lspd_id = ((Integer) info.get("MSPO_LSPD_ID"));
		if(mspo_lspd_id==8) {
			this.uwDao.updatePosisiMst_insured(spaj, 99);
			uwDao.insertMstPositionSpaj(lus_id, "TRANSFER KE FILLING", spaj, 0);
			mspo_lspd_id = 99;
		}
		
		//Looping sebanyak komisi per billing (max sejumlah 4.. RM/AM/UM/EM)
		List daftarKomisi = this.financeDao.selectKomisiAgen(spaj, tahun, premi);
		for(int i=0; i<daftarKomisi.size(); i++) {
			Commission komisi = (Commission) daftarKomisi.get(i);
			
			//proses hanya fa, leader & supervisor tidak
			if(komisi.getLev_kom()!=4) continue;

			//Jika RM New Bisnis (level 1) & non-sbm
			if(komisi.getLsle_id()==1 && komisi.getMsag_sbm()!=1) {
				//jika premi pertama, atau beg_date > 01/01/2000 tidak dapat
				if(komisi.getMsbi_premi_ke()==1) continue;
				Calendar cal = Calendar.getInstance(); cal.set(2000, 1, 1);
				if(FormatDate.dateDifference((Date) info.get("MSTE_BEG_DATE"), cal.getTime(), false)<0) continue;
			}
			
			//UPDATE PAY DATE
			this.financeDao.updatePayDate(lus_id, komisi.getCo_id());
			
			//bancass, skip
			if(komisi.getLca_id().equals("09")) continue;
				
			//hitung jumlah komisi - deduct2nya (untuk insert ke upload)
			List daftarDeduct = komisi.getDeduct();
			double jmlKomisi = komisi.getTotal().doubleValue();
			for(int j=0; j<daftarDeduct.size(); j++) {
				Map temp = (HashMap) daftarDeduct.get(j);
				jmlKomisi -= ((Double) temp.get("MSDD_DEDUCT")).doubleValue();
			}			

			String accountAgent = StringUtils.delete(komisi.getMsag_tabungan(), ".");
			int kodeBank = this.financeDao.validasiRekeningBank(komisi.getMsag_id());

			//Apakah agen punya rek di bii (kode 45)
			if(kodeBank == 45) {
				if(accountAgent.length() != 10) {
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					result.put("error", "Maaf, tetapi ada kesalahan nomor tabungan BII pada Agent "+komisi.getMsag_id());
					return result;
				}
				this.financeDao.deleteUploadFa(komisi.getCo_id());
				this.financeDao.insertUploadFa(komisi.getCo_id(), "000000", komisi.getMsag_id(), accountAgent, jmlKomisi);
			//Apabila rek bank lain
			}else {
				this.financeDao.deleteUploadNon(komisi.getCo_id());
				this.financeDao.insertUploadNon(komisi.getCo_id(), komisi.getMsag_id(), jmlKomisi);
			}
			//
		}

		//Update posisi billing
		int li_flag = ((Integer) info.get("MSTE_FLAG_CC"));
		int li_posisi = 99;
		if( li_flag == 1 || li_flag == 2 ) li_posisi = 37;
		
		this.uwDao.updateMst_billing(spaj, null, null, null, new Integer(tahun), new Integer(premi), new Integer(li_posisi), null, null);

		wf_trans_reward(spaj, tahun, premi);
		
		Integer lsbs_id = (Integer) info.get("LSBS_ID");
		
		if("77, 84, 87, 97, 100, 101, 102, 115, 116, 117, 118, 119, 120, 122, 127, 128, 129, 107, 152, 153,217,218".indexOf(lsbs_id.toString())>-1) {
			int flagTopup = ((Integer) info.get("FLAG_TOPUP"));
			if(flagTopup!=1) {
				flagTopup = this.financeDao.validationTopupBilling(spaj);
				if(flagTopup>0) result.put("sukses", "Billing berhasil di-transfer ke Filling. Harap perhatikan bahwa polis ini mempunyai komisi Top-Up");
			}
		}
		
		if(result.get("sukses")==null) result.put("sukses", "Billing berhasil di-transfer ke Filling.");
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return result;
	}

	/*
	public static void main(String[] args) {
		TransferPolis t = new TransferPolis();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
		t.defaultDateFormatReversed = df2;
		try {
			logger.info(t.hitungCekEndBill(df.parse("11/06/2008"), df.parse("12/06/2007"), 1, null));
		} catch (ParseException e) {
			logger.error("ERROR :", e);
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
	}*/
	
	/*
	 * ***********************************
	 * ***********************************
	 * ***********************************
	 * ***********************************
	 */
	public Date hitungCekEndBill(Date adt_next, Date adt_beg_date, int ai_month, BindException errors) throws Exception{
		if(logger.isDebugEnabled())logger.debug("PROSES: hitungCekEndBill");
		Date ldt_next = FormatDate.add(adt_next, Calendar.DATE, 1); //ldt_next + 1
		Date ld = defaultDateFormatReversed.parse(defaultDateFormatReversed.format(ldt_next)); //trunc date
		Date ldt_tmp;
		int li_count = Integer.valueOf(defaultDateFormatReversed.format(adt_beg_date).substring(6,8)); //day of month
		int adt_next_day = Integer.valueOf(defaultDateFormatReversed.format(ldt_next).substring(6,8)); //day of month

		//kalo day nya beda
		if(adt_next_day != li_count){
			do{
				if(ai_month == 12){
					ldt_tmp = defaultDateFormatReversed.parse(
							defaultDateFormatReversed.format(ld).substring(0,4) + 
							defaultDateFormatReversed.format(adt_beg_date).substring(4,6) + 
							FormatString.rpad("0", Integer.toString(li_count),2));
				}else{
					ldt_tmp = defaultDateFormatReversed.parse(
							defaultDateFormatReversed.format(ld).substring(0,4) + 
							defaultDateFormatReversed.format(adt_next).substring(4,6) + 
							FormatString.rpad("0", Integer.toString(li_count),2));
				}
				li_count--;
				if(li_count < 1) li_count = 31;
			}while(defaultDateFormatReversed.format(ldt_tmp).equals("19000101"));
			ldt_next = ldt_tmp;
		}
		ldt_tmp = FormatDate.add(adt_beg_date, Calendar.MONTH, ai_month); //begdate + pay period

		int month_ldt_tmp = Integer.valueOf(defaultDateFormatReversed.format(ldt_tmp).substring(6,8)); //day of month
		int day_ldt_tmp = Integer.valueOf(defaultDateFormatReversed.format(ldt_tmp).substring(4,6)); //day of month
		if(month_ldt_tmp == 2 && day_ldt_tmp == 29) {
			ldt_tmp = FormatDate.add(ldt_tmp, Calendar.DATE, -1); //ldt_tmp - 1
		}
		
		if(ldt_next.compareTo(ldt_tmp) >= 0) { //If ldt_next >= ldt_tmp
			ldt_next = null;
		}
		
		return ldt_next;
	}

	/**
	 * @author Yusuf
	 * @deprecated
	 * Udah gak dipake!
	 */
	private boolean hitungPrivasi(String spaj, int tahun, int premi, User currentUser) throws Exception{
			if(logger.isDebugEnabled())logger.debug("PROSES: hitungPrivasi");
	
			int li_pmode = this.uwDao.selectCbFromSpaj(spaj);
			double ldec_premi = this.uwDao.selectPremiAndRider(spaj, "premi").doubleValue();
			double ldec_rider = this.uwDao.selectPremiAndRider(spaj, "rider").doubleValue();
			double ldec_persen=0;
			double ldec_persen_dep=0;
			int li_insert=0;
			double ldec_jlh_premi=0;
			double ldec_saldo_depo;
			double ldec_depo;
			if(ldec_rider==-1)ldec_rider=0;
			int li_ke = this.uwDao.selectPrivasiKe(spaj);
			li_ke++;
			this.uwDao.insertMst_privasi(spaj, li_ke, ldec_premi, ldec_rider, tahun, premi, currentUser.getLus_id());
	//		Mulai th. ke-2 masuk premi terbagi 2 : premi resiko dan premi deposito
			if(li_pmode==0){
				ldec_persen = 30;
				ldec_persen_dep = 70;
				li_insert = 2;
			}else{
				if(tahun==1){
					ldec_persen = 100;
					ldec_persen_dep = 0; 
					li_insert = 1;
				}else if(tahun>=2 && tahun<11){
					ldec_persen = 40;
					ldec_persen_dep = 60;
					li_insert = 2;
				}else if(tahun>=11 && tahun<=20){
					ldec_persen = 0 ;
					ldec_persen_dep = 100;
					li_insert = 2;
				}
			}
			
			for(int i=0; i<li_insert; i++){
				ldec_jlh_premi = (ldec_persen* ldec_premi)/100;
				
				if( i == 2 ){ 
					ldec_persen = ldec_persen_dep;
					ldec_jlh_premi = (ldec_persen_dep * ldec_premi)/100;
				}
				
				this.uwDao.insertMst_det_privasi(spaj, li_ke, i+1, ldec_persen, ldec_jlh_premi);
			}
			
			if(li_insert>1){
				ldec_saldo_depo = this.uwDao.selectSaldoDeposit(spaj);
				ldec_depo = ldec_persen_dep * ldec_premi / 100;	
				ldec_saldo_depo = ldec_saldo_depo + ldec_depo;
				this.uwDao.insertMst_det_deposito(spaj, li_ke, li_insert, ldec_depo, ldec_persen_dep, ldec_saldo_depo);
			}
			
			return true;
			
		}

	public boolean transferToPrintPolis(String reg_spaj, BindException errors, int bulanRK, List billInfo, User currentUser, String li_retur, String li_returTopUp,String from, ElionsManager elionsManager, UwManager uwManager) throws Exception{
		boolean flagTopup2007 = false;
			try{
				
				if(logger.isDebugEnabled())logger.debug("~ TRANSFER KE PRINT POLIS DARI " + from + " untuk SPAJ " + reg_spaj);
				
				String businessId = FormatString.rpad("0", this.uwDao.selectBusinessId(reg_spaj), 2);
				n_prod produk = (n_prod) Class.forName("produk_asuransi.n_prod_"+businessId).newInstance();
				businessId = FormatString.rpad("0", businessId, 3);
				String businessNumber = uwDao.selectLsdbsNumber(reg_spaj);
				
				if(businessId.equals("134") && businessNumber.equals("6")){
					produk.isProductBancass=false;
				}
				
				SpajBill spajBill = new SpajBill();
				int li_persen_bayar = 0;
				Date ldt_next_bill; Date ldt_end_bill;
				
				Premi premi = this.uwDao.selectPremiTertanggung(reg_spaj);
				
				boolean isMuamalat = products.muamalat(Integer.parseInt(businessId), premi.getLsdbs_number().intValue());
				boolean isBethany = products.bethany(Integer.parseInt(businessId), premi.getLsdbs_number().intValue());
				
				if(premi==null || premi.getNama_pemegang()==null){
					errors.reject("payment.namaPemegangKosong");
					throw new Exception(errors);
				}
				
				if(businessId.equals("217") && businessNumber.equals("2")){
					//Do Nothing - tidak ada pengupdatan billing, karena belum ada payment
					//ERBE Package, Payment pertama sesudah produksi di sistem Lanjutan
				}else{					
						this.uwDao.updateMst_billing(reg_spaj, new Integer(1), Integer.valueOf(li_retur), null, 
								new Integer(1), new Integer(1), null, null, new Integer(li_persen_bayar));
						
						Integer countBilling = uwDao.selectCountMstBillingNB(reg_spaj);
						if(countBilling>1){
							for(int i=2;i<=countBilling;i++){
								this.uwDao.updateMst_billing(reg_spaj, new Integer(1), Integer.valueOf(li_returTopUp), null, 
										new Integer(1), new Integer(i), null, null, new Integer(li_persen_bayar));
							}
						}
				}
				
				/** 1. Proses Produksi **/
				if(logger.isDebugEnabled())logger.debug("~ PROSES PRODUKSI untuk SPAJ " + reg_spaj);
				errors = this.produksi.prosesProduksi(reg_spaj, errors, bulanRK, billInfo, currentUser, businessId, produk, elionsManager, uwManager);
				
				String lca_id = uwDao.selectCabangFromSpaj(reg_spaj);
				String lwk_id=uwDao.selectWakilFromSpaj(reg_spaj);
				/** 2. Proses Komisi BancAss, refferall BII **/
				//(Yusuf 03/01/2008) tambah amanah link 166
				if(errors.getErrorCount()==0){
					if((uwManager.selectAgenFromSpaj(reg_spaj).equals("022902") && ("170".equals(businessId)) && premi.getLsdbs_number()==1) || (lca_id.equals("40") && !lwk_id.equals("02"))) {
						//Req Pak Him : Untuk kode agent 022902 dan produk Iklas tidak mendapatkan komisi
					}else{
						if(bacDao.count_mst_cancel(reg_spaj)<=0){//bila ada di cancel, tidak perlu diinsert komisinya.
							if(((lca_id.equals("09") && ("079, 080, 081, 091, 092, 111, 120, 121, 127, 128, 129, 150, 151, 155, 134, 165, 166".indexOf(businessId)>-1
									|| ("142".equals(businessId) && premi.getLsdbs_number()>=2 && premi.getLsdbs_number() != 8) //bukan DM/TM
									|| ("158".equals(businessId) && premi.getLsdbs_number()>=5)
									|| ("164".equals(businessId) && premi.getLsdbs_number()==2)
									|| ("175".equals(businessId) && premi.getLsdbs_number() == 2)
									|| ("164".equals(businessId) && premi.getLsdbs_number()==5)
									|| ("186".equals(businessId) && premi.getLsdbs_number()==3))) 
									|| (!lca_id.equals("58") && uwDao.selectCountMstReffBii(reg_spaj)>0))
									&& (!lca_id.equals("73")) ){
								if("120, 121, 127, 128, 129, 150, 151, 155, 134, 166".indexOf(businessId)<0){
									if(logger.isDebugEnabled())logger.debug("~ PROSES KOMISI BANCASS untuk SPAJ " + reg_spaj);
									this.komisi.prosesKomisiBancAss(reg_spaj, currentUser, errors);
									if(errors.getErrorCount()>0){
										errors.reject("payment.commBancassFailed");
										throw new Exception(errors);
									}
								}
								
									spajBill.setNo_spaj(reg_spaj);
									spajBill.setTahun_ke(1);
									spajBill.setPremi_ke(1);
									spajBill.setFlag_topup(0);
					
									if(logger.isDebugEnabled())logger.debug("~ PROSES HITUNG KOMISI REFF BII untuk SPAJ " + reg_spaj);
									errors = this.komisi.hitungKomisiReffBII(spajBill, errors, currentUser); 
									if(errors.getErrorCount()>0){
										errors.reject("payment.commReffBIIFailed");
										throw new Exception(errors);
									}				
						/** 3. Proses Komisi Individu **/
							}else {								
									//lca_id 63 untuk ACD (Agency Career Development) //Anta
									if("58, 63, 65".indexOf(lca_id)==-1){								
										if(logger.isDebugEnabled())logger.debug("~ PROSES KOMISI INDIVIDU untuk SPAJ " + reg_spaj);
										if(lca_id.equals("40") && lwk_id.equals("02")){										
												//flagTopup2007 = this.komisi.prosesKomisiArco(reg_spaj, currentUser, errors,1);
										}else{
												flagTopup2007 = this.komisi.prosesKomisiIndividu(reg_spaj, currentUser, errors);
										}
										if(errors.getErrorCount()>0){
											errors.reject("payment.commNewLgFailed");
											throw new Exception(errors);
										}
								
									}							
							}
						}
					}
					
				}else {
					errors.reject("payment.productionFailed");
					throw new Exception(errors);
				}
				
				int topup;
				if(products.stableLink(businessId)) {
					topup=this.uwDao.validationStableLink(reg_spaj);
				}else {
					topup=this.uwDao.validationTopup(reg_spaj);
				}
				
				/** 4. Proses Produksi Top Up**/
				//insert komisi topup excellink
				if(products.unitLink(businessId) && !businessId.equals("191")){
					List<Integer> daftarMuKe = uwDao.selectMuKe(reg_spaj);
					
					if(topup>=1){
						
						//	produksi top-up per Jan '05 (hm 28/02/05)
						if(logger.isDebugEnabled())logger.debug("~ PROSES PRODUKSI TOPUP untuk SPAJ " + reg_spaj);
						for(Integer d : daftarMuKe) {
							errors = this.produksi.prosesProduksiTopUp(reg_spaj, errors, bulanRK, currentUser, businessId, d, elionsManager, uwManager);
							if(errors.getErrorCount()>0){
								errors.reject("payment.productionTuFailed");
								throw new Exception(errors);
							}
						}
						
				/** 5. Proses Komisi Top Up Individu, komisi refferall BII yang topup **/
						//Excellink karyawan (113,138,139) tidak ada komisi
						
						if(bacDao.count_mst_cancel(reg_spaj)<=0){//bila ada di cancel, tidak perlu diinsert komisinya.
							//lca_id 63 untuk ACD (Agency Career Development) //Anta
							if("40, 58, 63, 65".indexOf(lca_id)==-1){
								if("113, 138, 139, 191".indexOf(businessId)<0){
									if(logger.isDebugEnabled())logger.debug("~ PROSES KOMISI TOPUP INDIVIDU untuk SPAJ " + reg_spaj);
									 if ("190, 200".indexOf(businessId)>0&&premi.getLsdbs_number()<3){
										 
									 }else{
										if(flagTopup2007) {
											for(Integer d : daftarMuKe) {
												if(!this.komisi.prosesKomisiTopUpIndividu2007(reg_spaj, errors, currentUser, d)){
													errors.reject("payment.komisiTuExcellinkFailed");
													throw new Exception(errors);
												}
											}
										}else {
											for(Integer d : daftarMuKe) {
												if(!this.komisi.prosesKomisiTopUpIndividu(reg_spaj, errors, currentUser, d)){
													errors.reject("payment.komisiTuExcellinkFailed");
													throw new Exception(errors);
												}
											}
										}
									 }	
								}
								
								//(Yusuf 03/01/2008) tamnbah amanah link
								if("120, 121, 127, 128, 129, 134, 165, 166".indexOf(businessId)>-1){
									spajBill.setNo_spaj(reg_spaj);
									spajBill.setTahun_ke(1);
									spajBill.setPremi_ke(2);
									spajBill.setFlag_topup(1);
				//					 unit link bancass ada komisi tu u/ BII (RG) 26/09/2005
									if(logger.isDebugEnabled())logger.debug("~ PROSES HITUNG KOMISI REFF BII TOPUP untuk SPAJ " + reg_spaj);
									if(businessId.equals("134") && businessNumber.equals("6")){
										
									}else{
									errors = this.komisi.hitungKomisiReffBII(spajBill, errors, currentUser);
									}
									if(errors.getErrorCount()>0){
										errors.reject("payment.commReffBIIFailed2");
										throw new Exception(errors);
									}									
								}
							}
						}
					}
				}
				
				/** 6. Proses Jurnal Produksi **/	
				if(lca_id.equals("73") && lwk_id.equals("01")){
					if(logger.isDebugEnabled())logger.debug("~ PROSES JURNAL PRODUKSI UNITLINK ERBE untuk SPAJ " + reg_spaj);
					premi = this.jurnal.jurnalProduksiUnitLinkERBEPackage(reg_spaj, premi, errors, currentUser, topup);					
				}else if(products.unitLink(businessId)){
					if(logger.isDebugEnabled())logger.debug("~ PROSES JURNAL PRODUKSI UNITLINK untuk SPAJ " + reg_spaj);
					premi = this.jurnal.jurnalProduksiUnitLink(reg_spaj, premi, errors, currentUser, topup);
				}else{
					if(logger.isDebugEnabled())logger.debug("~ PROSES JURNAL PRODUKSI INDIVIDU untuk SPAJ " + reg_spaj);
					premi = this.jurnal.jurnalProduksiIndividu(reg_spaj, premi, errors, currentUser);
				}
				
				if(premi==null){
					errors.reject("payment.journalFailed");
					throw new Exception(errors);
				}
				
				/** 6.a Proses Komisi ERBE PP dan TB beserta generate Jurnal Komisi - khusus produk ERBE Package**/
				if(lca_id.equals("73") && lwk_id.equals("01")){
					if(logger.isDebugEnabled())logger.debug("~ PROSES JURNAL KOMISI UNITLINK ERBE PACKAGE untuk SPAJ " + reg_spaj);
					//untuk ERBE Package akan memproses Jurnal Komisi juga
					int status_jurn_kom = 0;
					
					//Proses Komisi ERBE (Input ke MST_Invoice untuk premi pokok dan berkala)	: prose package tanya kebagian Komisi -Otbie				
					Integer li_row = this.bacDao.selectBillingCount(reg_spaj, 1);
					Integer hasil=null;				
					for(int premke= 1; premke<= li_row; premke++){
						hasil = bacDao.prosesKomisiErbePackageSystem(reg_spaj, 1, premke,0);			
					}
					if(hasil==0){
						errors.reject("Mohon Maaf, terjadi kesalahan pada saat proses kompensasi topup berkala, Silakan hubungi IT.");
						throw new Exception(errors);
					}										
//					
					//jika null harus cek pada saat pemanggilan kelas "prosesKomisiErbePackageSystem" total billing = total yg keinsert di mst_invoice
					List daftarKomisi = financeDao.selectKomisiAgenErbePackage(reg_spaj,1,li_row);
					if(daftarKomisi.size() == 0){
						errors.reject("komisi.journalFailedKomisiVal");
						throw new Exception(errors);
					}else{
						Date ldt_max_rk = this.commonDao.selectSysdateTruncated(0);
						
						//Genretae Jurnal ERBE Komisi Pokok
						List daftarKomisiPokok = financeDao.selectKomisiAgenErbePackage(reg_spaj,1,1);
						
						if(daftarKomisiPokok.size() != 0){
							status_jurn_kom = jurnal.jurnalMemorialPremiUlinkKomisiERBEPackagePremiPokok(premi,ldt_max_rk,daftarKomisiPokok, errors, currentUser);
						}else{
							status_jurn_kom = 0;
						}
						
						//Genretae Jurnal ERBE Komisi Pokok 
						List daftarKomisitopup = financeDao.selectKomisiAgenErbePackage(reg_spaj,1,2);
						
						if(daftarKomisitopup.size() != 0){
							status_jurn_kom = jurnal.jurnalMemorialPremiUlinkKomisiERBEPackagePremiTopup(premi,ldt_max_rk,daftarKomisitopup, errors, currentUser);
						}else{
							status_jurn_kom = 0;
						}
						
						if(status_jurn_kom==0){
							errors.reject("komisi.journalFailedKomisi");
							throw new Exception(errors);
						}
					}			
							
				}
				//DISABLED - YUSUF - 20060328
//				if(businessId.equals("80") || businessId.equals("92")){
//					//privasi
//					if(!hitungPrivasi(reg_spaj, 1, 1, currentUser)){
//						errors.reject("payment.gagalPrivasi");
//						throw new Exception(errors);
//					}
//				}
				
				if("074,076,096,099".indexOf(businessId)>-1)li_persen_bayar=100;

				//Yusuf - 14/12/2007 - Apabila inputan bank -> dipakai untuk disable validasi reff BII dan titipan premi
				int isInputanBank = elionsManager.selectIsInputanBank(reg_spaj);

				/** 7.a. Proses Update Posisi Polis - Khusus produk ENDOWMENT, PRINT POLIS dulu baru PAYMENT **/
				if(businessId.equals("157")) {
					Integer li_jum = this.uwDao.selectSpajCancel(reg_spaj);
					int li_lspd;
					String ls_ket;
					if(li_jum>0) {
						li_lspd = 99;
						ls_ket = "FILLING - ENDORSE";
					}else {
						li_lspd = 7;
						ls_ket = "TANDA TERIMA";
					}
					this.uwDao.updateMst_billing(reg_spaj, new Integer(1), Integer.valueOf(li_retur), null, 
							new Integer(1), new Integer(1), new Integer(li_lspd), null, new Integer(li_persen_bayar));					
					this.uwDao.updatePosisiMst_insured(reg_spaj, li_lspd);
					this.uwDao.updatePosisiMst_policy(reg_spaj, new Integer(li_lspd));

					//Kalau bukan cara bayar sekaligus, ada proses billing
					int li_proses=0;
					if(!premi.getLscb_id().equals("0")){
						ldt_end_bill = (Date) ((HashMap)billInfo.get(0)).get("MSBI_END_DATE");
						ldt_next_bill = hitungCekEndBill(ldt_end_bill, premi.getMste_beg_date(), premi.getMspo_pay_period() * 12, errors);
						if(ldt_next_bill!=null)li_proses=1;
					}
					
					this.uwDao.updateMst_policy(reg_spaj, new Double(li_lspd), new Integer(li_proses), null);
					uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "TRANSFER KE " + ls_ket, reg_spaj, 0);
					
				/** 7.b. Proses Update Posisi Polis - Khusus produk inputan bank sinarmas, flownya :
				 * - apabila akseptasi biasa, dari payment longkap ke Tanda terima Polis, soalnya print polisnya udah di awal setelah BAC 
				 * - apabila akseptasi khusus, dari payment tetap ke print polis, ditahan disana
				 * - mulai 20 feb 09, untuk semua powersave dan stable link 
				 * **/				
				// PROJECT: POWERSAVE & STABLE LINK
				}else if((isInputanBank==2 || isInputanBank==3 || (businessId.equals("175") && premi.getLsdbs_number()==2) || (businessId.equals("073") && premi.getLsdbs_number()==14))
							&&  !(businessId.equals("217") && premi.getLsdbs_number()==2)  ) {
				//}else if(products.powerSave(businessId) || products.stableLink(businessId)){
				
					Integer count=elionsManager.selectCountProductSimasPrimaAkseptasiKhusus(reg_spaj, 1,10, isInputanBank);					
					int li_lspd;
					String ls_ket;
					if(businessId.equals("073") && premi.getLsdbs_number()==14){//MANTA (13/01/2014) - Request Andy
						li_lspd = 99;
						ls_ket = "FILLING";
					}else if(isInputanBank==2 || (businessId.equals(217) && premi.getLsdbs_number()==2) ){ //MANTA - 01/06/2015 dan produk ERBE Pakage - Ridhaal 1/8/2018
						li_lspd = 6;
						ls_ket = "PRINT POLIS";
						uwDao.prosesSnows(reg_spaj, currentUser.getLus_id(), 211, 212);
					}else if(products.productBsmFlowStandardIndividu(Integer.parseInt(businessId), Integer.parseInt(businessNumber)) ||  (businessId.equals("142")) && currentUser.getCab_bank().equals("") ){
						li_lspd = 6;
						ls_ket = "PRINT POLIS";
						uwDao.prosesSnows(reg_spaj, currentUser.getLus_id(), 211, 212);
					}else{
						if(count>0){ //AKSEPTASI KHUSUS
							li_lspd = 6;
							ls_ket = "PRINT POLIS";
						}else { //AKSEPTASI BIASA
							li_lspd = 7;
							ls_ket = "TANDA TERIMA";
						}
					}
					
					uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "TRANSFER KE " + ls_ket, reg_spaj, 0);
					
					this.uwDao.updateMst_billing(reg_spaj, new Integer(1), Integer.valueOf(li_retur), null, 
							new Integer(1), new Integer(1), null, null, new Integer(li_persen_bayar));
					this.uwDao.updatePosisiMst_insured(reg_spaj, li_lspd);
					this.uwDao.updatePosisiMst_policy(reg_spaj, li_lspd);

					//Kalau bukan cara bayar sekaligus, ada proses billing
					int li_proses=0;
					if(!premi.getLscb_id().equals("0")){
						ldt_end_bill = (Date) ((HashMap)billInfo.get(0)).get("MSBI_END_DATE");
						ldt_next_bill = hitungCekEndBill(ldt_end_bill, premi.getMste_beg_date(), premi.getMspo_pay_period() * 12, errors);
						if(ldt_next_bill!=null)li_proses=1;
					}
					
					this.uwDao.updateMst_policy(reg_spaj, (double) li_lspd, new Integer(li_proses), null);
					
					if(ls_ket.equals("PRINT POLIS")) uwDao.saveMstTransHistory(reg_spaj, "tgl_transfer_uw_print", null, null, null);
					
					if(products.productBsmFlowStandardIndividu(Integer.parseInt(businessId), Integer.parseInt(businessNumber))){
						String emailAdmin = bacDao.selectEmailAdminInputter(reg_spaj);
						if(!Common.isEmpty(emailAdmin)){
							email.send(false, props.getProperty("admin.ajsjava"), new String[] {emailAdmin}, new String[]{"ryan@sinarmasmsiglife.co.id"}, null, 
					        		"[E-Lions] VALID PRINT POLIS UNTUK NO POLIS "+FormatString.nomorPolis(uwDao.selectNoPolisFromSpaj(reg_spaj)), 
									"Proses telah selesai dilakukan. Silakan Admin BSM untuk melanjutkan proses Print polis. Terima kasih."+
									"\n\nINFO: Pesan ini dikirim secara otomatis melalui sistem E-LIONS.", null);
						}
					}
					
				/** 7.c. Proses Update Posisi Polis - Selain ENDOWMENT, POWERSAVE, dan STABLE LINK, PAYMENT dulu baru PRINT POLIS **/
				}else {
					
					if(businessId.equals("217") && businessNumber.equals("2")){
						//dont update billing karena blom ada payment
					}else{	
					this.uwDao.updateMst_billing(reg_spaj, new Integer(1), Integer.valueOf(li_retur), null, 
							new Integer(1), new Integer(1), null, null, new Integer(li_persen_bayar));
					}
					this.uwDao.updatePosisiMst_insured(reg_spaj, 6);
					this.uwDao.updatePosisiMst_policy(reg_spaj, new Integer(6));

					//Kalau bukan cara bayar sekaligus, ada proses billing
					int li_proses=0;
					if(!premi.getLscb_id().equals("0")){
						ldt_end_bill = (Date) ((HashMap)billInfo.get(0)).get("MSBI_END_DATE");
						ldt_next_bill = hitungCekEndBill(ldt_end_bill, premi.getMste_beg_date(), premi.getMspo_pay_period() * 12, errors);
						if(ldt_next_bill!=null)li_proses=1;
					}
					
					//update LSSP_ID = 1 (INFORCE)
					this.uwDao.updateMst_policy(reg_spaj, new Double(6), new Integer(li_proses), null);
							
					//insert position spaj, khusus muamalat, tidak diinsert disini, karena akan dilakukan 3x transfer yaitu : 
					//dari PAYMENT -> PRINT POLIS -> TANDA TERIMA -> AGENT COMMISSION
					//sehingga insert position spaj nya langsung di langkah terakhir aja, yaitu saat trans dari TANDA TERIMA ke AGENT COMMISSION
					if(isMuamalat) {
						if(businessId.equals("153")){
							uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "TRANSFER KE PRINT POLIS", reg_spaj, 0);
							uwDao.saveMstTransHistory(reg_spaj, "tgl_transfer_uw_print", null, null, null);
							//Proses snows
							uwDao.prosesSnows(reg_spaj, currentUser.getLus_id(), 211, 212);
							uwDao.prosesSnows(reg_spaj, currentUser.getLus_id(), null, 201);
						}
					}else if(from.equals("payment")) {
						Boolean ok = false;
							do{
								try{
									uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "TRANSFER KE PRINT POLIS", reg_spaj, 0);
									uwDao.saveMstTransHistory(reg_spaj, "tgl_transfer_uw_print", null, null, null);
									ok=true;
									//Proses snows
									uwDao.prosesSnows(reg_spaj, currentUser.getLus_id(), 211, 212);
									uwDao.prosesSnows(reg_spaj, currentUser.getLus_id(), null, 201);
								}catch(Exception e){};
							}while (!ok);
					}else if(from.equals("underwriting")) {
						uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "LANGSUNG PRINT POLIS", reg_spaj, 0);
					}else { 
						throw new Exception("Fungsi ini hanya bisa digunakan dari menu Payment / Underwriting");
					}
				}
				
				/** 8. Proses Nilai2 tunai/tahapan/bonus/saving/devident/maturity, simpan ke EKA.MST_NILAI **/

				nilaiTunai.proses(reg_spaj, currentUser.getLus_id(), "simpan", 0, 0);

				/** 9. Proses Cek dan generate Simas Card **/
				List daftarSebelumnya = uwDao.selectSimasCard(reg_spaj); //daftar simas card sebelumnya yang masih aktif
				
				String polis = uwDao.selectNoPolisFromSpaj(reg_spaj);
				polis = FormatString.nomorPolis(polis);
				
				int jumlahPolis = uwDao.selectJumlahPolis(reg_spaj).size(); //ambil jumlah polis yang dia punya
				double totalPremi = uwDao.selectTotalPremi(reg_spaj); //total semua premi nya
				
				String notes = "";
				int jenis = 0; //apabila pemegang new business
				int flag_aktif = 1;
				
				List isAgen = uwDao.selectIsSimasCardClientAnAgent(reg_spaj); 
				if(isAgen.size()>0){ //apabila pemegang seorang agent
					jenis = 5; 
					String msag_id = (String) ((Map) isAgen.get(0)).get("MSAG_ID");
					
					if(uwDao.selectIsAgentCertified(msag_id)==0){ //apabila agen TIDAK certified
						notes = "[AGEN BELUM CERTIFIED] ";
						flag_aktif = 0;
					}else{ //apabila agen certified
						notes = "[AGEN] ";
					}
				}
					
				if(!isAgen.isEmpty()){
					if(daftarSebelumnya.isEmpty()) { //kalo gak ada simas card sebelumnya
						if(StringUtil.isEmpty(polis)){
							errors.reject("payment.noSimascard");
							throw new Exception(errors);
						}else{
							//tidak print -> flag_print=4, print -> flag_print=0
							if((("187,203,209".indexOf(businessId) > -1) && isMuamalat && isBethany)){
								if(uwDao.selectJenisTerbitPolis(reg_spaj)==1){//apabila softcopy, kasih notes aja
									uwDao.insertSimasCard(jenis, reg_spaj, polis, currentUser.getLus_id(), 
										jumlahPolis, totalPremi, 0, notes + "POLIS SOFTCOPY", flag_aktif);  
								}else{ 
									uwDao.insertSimasCard(jenis, reg_spaj, polis, currentUser.getLus_id(), 
										jumlahPolis, totalPremi, 0, "POLIS HARDCOPY", flag_aktif); //belum print
								}
							}
						}
					}
				}
				
				// *Andhika - Khusus VIP Card Syariah
//				String lca_id = uwDao.selectCabangFromSpaj(reg_spaj); //hapus
				List daftarVipSebelumnya = uwDao.selectVipCard(reg_spaj); // *Daftar Vip Card
				if(daftarVipSebelumnya.isEmpty()) {
					jenis = 9;
					String polisFormat = uwDao.selectNoPolisFormat(reg_spaj);
					List dataPSN = bacDao.selectDataPSN(reg_spaj);
					Integer id_psn = 0 ;
					if(!dataPSN.isEmpty()){
						Map m = (HashMap)dataPSN.get(0);	
						id_psn = ((BigDecimal) m.get("ID_PSN")).intValue();
					}
					Date sysdate = commonDao.selectSysdateTruncated(0);
					Date sysdatePayDate = commonDao.selectSysdateTruncated(1);//set untuk hari besoknya
					Date tgl_bayar = defaultDateFormat.parse("9/2/2010");
					Double mspa_payment = uwDao.selectPaymentMode(reg_spaj);
					String carBayar = premi.getLscb_id();
					Date tgl_naktif = premi.getMste_beg_date();
					ldt_end_bill = (Date) ((HashMap)billInfo.get(0)).get("MSBI_END_DATE");
					Date msc_enddate  = ldt_end_bill;
					/**
					 * @author Andhika
					 * Ganti ketentuan yang berhak dapet vip card syariah
					 */					
					if(StringUtil.isEmpty(polis)){
						errors.reject("payment.noSimascard");
						throw new Exception(errors);
					}else{
						if(products.syariah(businessId,businessNumber) && premi.getLspd_id() != 95){	 // FIX ME	
							if( (carBayar.equalsIgnoreCase("0") && mspa_payment >= 200000000 && id_psn == 0) || // *sekaligus
								(carBayar.equalsIgnoreCase("3") && mspa_payment >= 23000000 && id_psn == 0) || // *Tahunan
								(carBayar.equalsIgnoreCase("6") && mspa_payment >= 2000000 && id_psn == 0)  || // *Bulanan
								(carBayar.equalsIgnoreCase("2") && mspa_payment >= 11500000 && id_psn == 0) || // *Semesteran
								(carBayar.equalsIgnoreCase("1") && mspa_payment >= 5750000 && id_psn == 0))    // *Triwulan
							{
								if(uwDao.selectJenisTerbitPolis(reg_spaj)==1){ //apabila softcopy, kasih notes aja
									uwDao.insertVipCard(jenis, reg_spaj, polis, currentUser.getLus_id(), 
										jumlahPolis, totalPremi, 0, notes + "POLIS SOFTCOPY", flag_aktif, tgl_naktif);  
								}else{
									uwDao.insertVipCard(jenis, reg_spaj, polis, currentUser.getLus_id(), 
										jumlahPolis, totalPremi, 0, "POLIS HARDCOPY", flag_aktif, tgl_naktif); //belum print
								}
							}else if (id_psn.intValue() > 0 ){
								if( (carBayar.equalsIgnoreCase("0") && mspa_payment >= 150000000 ) || // *sekaligus
									(carBayar.equalsIgnoreCase("3") && mspa_payment >= 22000000  )|| // *Tahunan
									(carBayar.equalsIgnoreCase("6") && mspa_payment >= 1000000 )|| // *Bulanan
									(carBayar.equalsIgnoreCase("2") && mspa_payment >= 11000000 )|| // *Semesteran
									(carBayar.equalsIgnoreCase("1") && mspa_payment >= 5500000 ))    // *Triwulan
								{
									if(uwDao.selectJenisTerbitPolis(reg_spaj)==1){ //apabila softcopy, kasih notes aja
										uwDao.insertVipCard(jenis, reg_spaj, polis, currentUser.getLus_id(), 
											jumlahPolis, totalPremi, 0, notes + "POLIS SOFTCOPY", flag_aktif, tgl_naktif);  
									}else{
										uwDao.insertVipCard(jenis, reg_spaj, polis, currentUser.getLus_id(), 
											jumlahPolis, totalPremi, 0, "POLIS HARDCOPY", flag_aktif, tgl_naktif); //belum print
								
									}
								}
							}
						}
					}
				}
				
				/** 10. Proses Update tanggal transfer stable link */
				uwDao.updateTanggalTransferStableLink(reg_spaj);

				/** 11. Proses Khusus Produk BANK MUAMALAT */
				/** (+) KHUSUS PRODUK PAS, disamakan dengan produk muamalat selain mabrur (dr Payment langsung masuk ke AGENT COMMISSION)*/
				
				if(isMuamalat || ("187,203,209".indexOf(businessId) >-1) || ("073".equals(businessId) && "15".equals(businessNumber))) {
					// 11.a. Insert MST_DATA_BMI, untuk diemail reportnya setiap malam ke bank muamalat 
					if(isMuamalat){
						muamalatDao.saveDataBmi(reg_spaj, currentUser);
					}
					
					int lssa_id = elionsManager.selectStatusAksep(reg_spaj);
					
					if(lssa_id == 5){
							
						//update tgl kirim polis, biar muncul di report service level
						uwDao.updateTanggalKirimPolis(reg_spaj);
						uwDao.saveMstTransHistory(reg_spaj, "tgl_kirim_polis", null, null, null);
						
						if(!businessId.equals("153")){
							// 11.b. transfer lagi dari PRINT POLIS ke TANDA TERIMA
							transferPrintPolisToFillingAtauTandaTerimaPolis(reg_spaj, currentUser, 0);
							
							//sebelum transfer ke finance, harus cek rekening agen gak boleh kosong!bila kosong ditransfer ke CHECKING TTP
							String errorRekening = elionsManager.cekRekAgen2(reg_spaj);
							Map pemegang = this.uwDao.selectPemegang(reg_spaj);
							if(errorRekening.equals("")) {
								String lcaId=(String)pemegang.get("LCA_ID");
								String lwkId=(String)pemegang.get("LWK_ID");
								String nama_dist = (String) pemegang.get("NAMA_DIST");
								String [] to = props.getProperty("agensys.emails").split(";");
								if(nama_dist.equals("AGENCY") || nama_dist.equals("REGIONAL")){
									to = props.getProperty("agensys.emails").split(";");
								}else if(nama_dist.equals("MNC") || nama_dist.equals("WORKSITE")){
									to = props.getProperty("worksite.emails").split(";");
								}else if(nama_dist.equals("BANCASS")){
									to = props.getProperty("bancass.emails").split(";");
								}else if(nama_dist.equals("DMTM")){
									to = props.getProperty("dmtm.emails").split(";");
								}
								
								if(!(lcaId.equals("37") && (lwkId.equals("B2") || lwkId.equals("B3") ))){//Rekening Kosong
									Date tanggal = commonDao.selectSysdateTrunc();
									EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
											null, 0, 0, tanggal, null,
						        			false, props.getProperty("admin.ajsjava"), to , currentUser.getEmail().split(";"), null, 
							        		"[E-Lions] Rekening Agen "+errorRekening+" Kosong", 
											"Harap cek rekening agen: \n\n"  +errorRekening+" karena rekeningnya masih kosong, sehingga komisinya tidak dapat diproses. Terima kasih."+
											"\n\nNo SPAJ  "  + reg_spaj +
											"\n\nINFO UNTUK UNDERWRITING: Polis ini ditransfer ke posisi PROSES CHECKING TTP, silahkan transfer ke finance HANYA SETELAH REKENING AGEN DI-UPDATE." +
											"\n\nINFO: Pesan ini dikirim secara otomatis melalui sistem E-LIONS.", null, reg_spaj);	
									String ket = "TRANSFER KE PROSES CHECKING TTP (REKENING AGEN MASIH KOSONG).";
									this.uwDao.updatePosisiMst_insured(reg_spaj, 9);
									this.uwDao.updateBillingTtp(9, reg_spaj);
									Boolean ok = false;
									do{
										try{
											uwDao.insertMstPositionSpaj(currentUser.getLus_id(), ket, reg_spaj, 0);
											ok=true;
										}catch(Exception e){};
									}while (!ok);
									Policy policy=this.uwDao.selectDw1Underwriting(reg_spaj,null,1);
									if(policy.getMspo_date_ttp()==null)
										this.uwDao.updateMst_policy(reg_spaj, new Double(9), null, commonDao.selectSysdate());
									else
										this.uwDao.updateMst_policy(reg_spaj, new Double(9), null, null);
//									Boolean ok = false;
//									do{
//										try{
//											uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "TRANSFER KE TANDA TERIMA(REKENING AGEN MASIH KOSONG)", reg_spaj, 0);
//											ok=true;
//										}catch(Exception e){};
//									}while (!ok);
								}
							}else{
								Integer proses=0;
								Map tertanggung= this.uwDao.selectTertanggung(reg_spaj);
								String lcaId=(String)tertanggung.get("LCA_ID");
								Integer flagKomisi=(Integer)tertanggung.get("MSTE_FLAG_KOMISI");
								String cabang="37,09,42,46,52";
								String msag = uwDao.selectAgenFromSpaj(reg_spaj);
								List tmp = uwDao.selectAgenCekKomisi(msag);
								Date ldt_birth = new Date();
								String ls_nama = "";
								Double ldec_kom = new Double(0);
								String nmCabang= uwDao.selectNmCabangFromSpaj(reg_spaj);
								int li_polis = 0 ;
								if( tmp.size() > 0 ) {
									ldt_birth = (Date) ((Map) tmp.get(tmp.size()-1)).get("MSPE_DATE_BIRTH");
									ls_nama = (String) ((Map) tmp.get(tmp.size()-1)).get("MCL_FIRST");
									ldec_kom = (Double) ((Map) tmp.get(tmp.size()-1)).get("KOMISI");
									li_polis = uwDao.selectJumlahPolisAgen(ls_nama, FormatDate.toString(ldt_birth));
								}
								if(cabang.indexOf(lca_id)>-1){
									proses=5;
								}else{
//									ldec_kom=new Double(600000);
									if(flagKomisi.intValue()==1 ){//proses transfer balikan dari Agency
										proses=2;
									}else if(flagKomisi.intValue()==2){//
										proses=3;
									}else if(ldec_kom.doubleValue()<500000 || li_polis>0){
										//agen sudah punya polis atau komisi <500rb, transfer ke Finance
										proses=4;
									}else{//transfer ke Proses Checking TTP{
										proses=6;
									}
								}
								// 11.c. transfer lagi dari TANDA TERIMA ke AGENT COMMISSION
								if(businessId.equals("187") && "5,6".indexOf(premi.getLsdbs_number().toString())>-1 ){
									prosesTransferKomisiOrFilling(reg_spaj, 99, currentUser.getLus_id(), commonDao.selectSysdate(), "TRANSFER LANGSUNG KE FILLING.");
								}else{
									prosesTransferKomisiOrFilling(reg_spaj, 8, currentUser.getLus_id(), commonDao.selectSysdate(), "TRANSFER LANGSUNG KE KOMISI (FINANCE).");
								}
								//(Deddy 11 Nov 2010 : (REQ YUSUF) UNTUK PROSES SEBELUM MASUK KE KOMISI FINANCE, DICEK apabila proses 6 maka masuk ke lspd_id 9)
								if(proses==6){
									String ket = "TRANSFER KE PROSES CHECKING TTP (DEDUCT).";
									this.uwDao.updatePosisiMst_insured(reg_spaj, 9);
									this.uwDao.updateBillingTtp(9, reg_spaj);
									Boolean ok = false;
									do{
										try{
											uwDao.insertMstPositionSpaj(currentUser.getLus_id(), ket, reg_spaj, 0);
											ok=true;
										}catch(Exception e){};
									}while (!ok);
									Policy policy=this.uwDao.selectDw1Underwriting(reg_spaj,8,1);
									if(policy.getMspo_date_ttp()==null)
										this.uwDao.updateMst_policy(reg_spaj, new Double(9), null, commonDao.selectSysdate());
									else
										this.uwDao.updateMst_policy(reg_spaj, new Double(9), null, null);
									
									//email 1
									/*if("37,52,58".indexOf(lca_id)==-1 ){
										String pesan="KOMISI BELUM DAPAT DIBAYAR KARENA ADA DEDUCT (D).";
										List lsSpaj=new ArrayList();
										lsSpaj.add(reg_spaj);
										emailCheckingTtp(lsSpaj, pesan, currentUser,1,"ADA DEDUCT (D)");
									}*/
								}
							}
						}
						if((("203,209".indexOf(businessId) >-1) || ("187".equals(businessId) && (premi.getLsdbs_number()>=11 && premi.getLsdbs_number()<=14)))
								|| ("073".equals(businessId) && "15".equals(businessNumber))
								|| ("183".equals(businessId) && (premi.getLsdbs_number()>=76 && premi.getLsdbs_number()<=90))
								|| ("195".equals(businessId) && (premi.getLsdbs_number()>=13 && premi.getLsdbs_number()<=24))
								|| ("203,209".indexOf(businessId) >-1 && (premi.getLsdbs_number()>=9 && premi.getLsdbs_number()<=12)
										)
								) {
		
							Map m = uwManager.selectInformasiEmailSoftcopy(reg_spaj);
							String email = "";
							if(!m.isEmpty()){
								email = (String) m.get("MSPE_EMAIL");
							}
							
							//Mark Valentino - Validasi alamat email
							if((email != null) && (!email.isEmpty())){
								boolean isBancass = reg_spaj.startsWith("09");
								boolean isDmtm = reg_spaj.startsWith("40");			
						        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
						        
								if(!Common.isEmpty(email)){
									if ( (!isBancass) && (!isDmtm) && (matcher.find()) ){
										Boolean EmailOtomatis =softcopy.softCopyOtomatis(reg_spaj, currentUser, email);
										if(EmailOtomatis==false){
											errors.reject("payment.softCopyFailed");
											TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
											throw new Exception(errors);
										}									
									}
								}								
							}

						}
						
						
						
					}
				}
				
				/** 12. Proses Update MST_POLICY (mspo_beg_date, mspo_end_date) disamakan dengan MST_BILLING (msbi_beg_date, msbi_end_date) */
				uwDao.updateBegDatePolis(reg_spaj);
				
				/** 13. Yusuf (04/02/2010) - Setelah semua selesai baru update status polis menjadi INFORCE!!! */
				uwDao.updateMstPolicyLsspdId(reg_spaj, 1);
				this.uwDao.batchingSimasPrimelink(reg_spaj); //update batching simas primelink (rider save) //Chandra - 20181012
				//this.uwDao.blacklistChecking(reg_spaj); //helpdesk 127356, cek blacklist dan kirim email. //Chandra - 20181011
				uwDao.prosesSMSAkseptasi(reg_spaj); //helpdesk [133187], tambah kegiatan sms pada saat spaj inforce
				
				/** 14. Insert Ke Mst Visa Camp Untuk Flag CC =1   Ryan*/
				Product detailProduk = akseptasiDao.selectMstProductInsuredDetail(reg_spaj, Integer.parseInt(businessId), Integer.parseInt(businessNumber));
				HashMap datavisa = Common.serializableMap(uwDao.selectMstPaymentVisa(reg_spaj));
				if (datavisa != null){
					Integer flag_med=0;
					Double ann = ((BigDecimal) datavisa.get("ANN")).doubleValue();
					if("183,189,193,201".indexOf(businessId)>-1 ){//SMiLe Medical Stand Alone
						if(ann > 5000000){
							Integer vNominal = 400000;
							flag_med=1;
							uwDao.insertMstVisaCamp(reg_spaj, vNominal, currentUser.getLus_id(), flag_med,1);
						}else if(ann >= 3600000 && ann < 5000000){
							uwDao.insertMstVisaCamp(reg_spaj, 100000, currentUser.getLus_id(), flag_med,1);
						}
					}else{//untuk yang selain SMiLe Medical Stand Alone
						if(ann > 6000000){
							Integer vNominal = 300000;
							List<Map> daftarRider = uwDao.selectDetBillingRider(reg_spaj, 1, 1);
							for(Map rider : daftarRider) {
								if("823,825,828".indexOf(((BigDecimal) rider.get("LSBS_ID")).intValue())>-1 ){
									vNominal=400000;
									flag_med=1;
								}
							}	
							uwDao.insertMstVisaCamp(reg_spaj, vNominal, currentUser.getLus_id(), flag_med,1);
						}else if(ann >= 3600000 && ann < 6000000){
							uwDao.insertMstVisaCamp(reg_spaj, 100000, currentUser.getLus_id(), flag_med,1);
						}
					}
				}
				
				/** 14.1 Insert Ke Mst Visa Camp Untuk Campaign Magna Link   Ryan*/
				ArrayList<Ulink> listUlink = (ArrayList<Ulink>) uwDao.selectMstUlinkAll(reg_spaj);
				if("213,216".indexOf(businessId) >-1){
					Double premipokok = 0.0;
					Double ldec_total;
					for(int i=0; i<listUlink.size(); i++){
						if(listUlink.get(i).getLt_id() == 1){
							premipokok = listUlink.get(i).getMu_jlh_premi();
						}
					}
					if(premipokok.intValue() > 500000 ){
						ldec_total = new Double(premipokok / 500000);
						ldec_total=Math.floor(ldec_total);
						ldec_total = ldec_total * 25000;
						if(ldec_total.intValue()>0){
							uwDao.insertMstVisaCamp(reg_spaj, ldec_total.intValue(), currentUser.getLus_id(), 0,3);
						}
					}
				}
				
				/** 15. Kirim Email Otomatis Surat Penjaminan BTN*/
				List tmp = elionsManager.selectDetailBisnis(reg_spaj);
				String lsbs = (String) ((Map) tmp.get(0)).get("BISNIS");
				String lsdbs_id = (String) ((Map) tmp.get(0)).get("DETBISNIS");
				if((lsbs.equals("195") &&  "016, 017,018,019,020,021,022,023,024".indexOf(lsdbs_id) > 0 ) 
					|| (lsbs.equals("183") &&  "076, 077,078,079,080,081,082,083,084,085,086,087,088,089,090".indexOf(lsdbs_id) > 0 )) 
				{
					
					Map m = uwManager.selectInformasiEmailSoftcopy(reg_spaj);
					String email = (String) m.get("MSPE_EMAIL");
					
					if(!Common.isEmpty(email)){
						Boolean EmailOtomatis =softcopy.softCopyOtomatisBTN(reg_spaj, currentUser, email);
						if(EmailOtomatis==false){
							errors.reject("payment.softCopyFailed");
							TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
							throw new Exception(errors);
						}
					}
					
					
				}
				
				if((lsbs.equals("212") &&  "13".indexOf(lsdbs_id) > 0 ) 
					|| (lsbs.equals("208") &&  "45,46,47,48".indexOf(lsdbs_id) > 0 ) ){
					Map m = uwManager.selectInformasiEmailSoftcopy(reg_spaj);
					String email = (String) m.get("MSPE_EMAIL");
					if(!Common.isEmpty(email)){
						Boolean EmailOtomatis =softcopy.softCopyOtomatisBJB(reg_spaj, currentUser, email);
						if(EmailOtomatis==false){
							errors.reject("payment.softCopyFailed");
							TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
							throw new Exception(errors);
						}
					};
				}
				

				boolean isPaBsimSyariah = "205".equals(businessId) && ("9".equals(businessNumber) || "10".equals(businessNumber) || "11".equals(businessNumber) ||"12".equals(businessNumber));
				if(isPaBsimSyariah){
						Map m = uwManager.selectInformasiEmailSoftcopy(reg_spaj);
						String em = (String) m.get("MSPE_EMAIL");
						
						boolean isBancass = reg_spaj.startsWith("09");
					//	Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(em);
				        if(!Common.isEmpty(em)){
				        		Boolean EmailOtomatis =softcopy.softCopyOtomatisBSIMSyariah(reg_spaj, currentUser, em,elionsManager,elionsManager.bacManager);
								if(EmailOtomatis==false){
									errors.reject("payment.softCopyFailed");
									TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
									throw new Exception(errors);
								}									
							
						};
				}
				
				//MANTA - SMILE LINK ULTIMATE Konven dan Syariah
				if((lsbs.equals("190") && lsdbs_id.equals("009")) || (lsbs.equals("200") && lsdbs_id.equals("007"))) uwDao.insertMstRefferal(reg_spaj);
				
				////////////////////////////////////////////////////
				if(errors.getErrorCount()>0) {
					throw new Exception(errors);
				}
				else {
					if(uwDao.validationPositionSPAJ(reg_spaj) == 6){
						uwDao.prosesofacscreening(reg_spaj, 1, Integer.parseInt(currentUser.getLus_id()));
					}
					return true;
				}

			}catch(Exception e){
				logger.error("ERROR :", e);
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();

				return false;
			}
		}

	public List transferKustodianToNabOrFilling(int lspd, String[] spaj) throws DataAccessException{

		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();		
		
		List daftar = this.uwDao.selectDaftarSPAJUnitLink2(lspd, spaj);
		
		List errors = new ArrayList();
		
		for(int i=0; i<daftar.size(); i++) {
			Map baris = (HashMap) daftar.get(i);
			String ls_spaj = (String) baris.get("REG_SPAJ");
			Integer li_ke = (Integer) baris.get("MU_KE");
			Integer li_print = (Integer) baris.get("MU_FLAG_PRINT");
			Integer li_trans = (Integer) baris.get("LT_ID");
			
			if(lspd == 42) lspd = 43;
			else if(lspd == 46) {
				lspd = 47;
				if(li_trans == 2) {
					errors.add("Untuk Transaksi Top-up tunggal tidak perlu di Transfer.");
					return errors;
				}
			}else if(lspd == 51) lspd = 52;
			else if(lspd == 61) lspd = 62; //print kustodian potongan
			else if(lspd == 48 || lspd == 53 || lspd == 63) lspd = 49; //print bayar potongan langsung -> print surat 6 bulanan
			
			//if(li_print >= 2) {
			this.uwDao.updatePosisiTransUlink(lspd, ls_spaj, li_ke);
			this.uwDao.updatePosisiUlink(lspd, ls_spaj, li_ke);
			//}
		}
		
		return errors;
	}
	
	/**Fungsi:	Untuk melakukan proses transfer balik ke TTP, pada menu Entry=>UW=>Checking proses TTP
	 * 
	 * @deprecated : Yusuf - 17 nov 2008, sekarang, bila sudah ok dari agency, maka tidak transfer balik ke TTP, tapi lnagsung 
	 * transfer ke finance untuk diproses komisi oleh mba murni
	 * 
	 * @param 	String spaj,Integer li_pos,User currentUser,String ket
	 * @return	Integer
	 * @author 	Ferry Harlim
	 */
	public void transferBalikToTtp(String spaj,Integer li_pos,User currentUser,String ket){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		List lsSpaj=new ArrayList();
		lsSpaj.add(spaj);
		this.uwDao.updatePosisiMst_insured(spaj, li_pos);//li_pos=7 Tanda terima
		this.uwDao.updateMstInsuredKomisi(spaj, 1, 1,1, currentUser.getLus_id(), false);
		this.uwDao.updateMst_policy(spaj, new Double(li_pos), null, null);
		this.uwDao.updateBillingTtp(li_pos, spaj);
		uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "Transfer Balik ke Tanda Terima (Update Tgl Komisi)", spaj, 0);
		ket="Komisi dapat dibayar karena "+ket;
		emailCheckingTtp(lsSpaj, ket, currentUser,2,"Komisi Dapat Dibayarkan");
	}
	
	/**Fungsi: 	Untuk mengirimkan email dari hasil proses checking di TTP
	 * @param 	List lsSpaj,String spaj,String ket,User currentUser,Integer flag,String subject
	 * 			flag=1 AGEN BELUM PUNYA POLIS proses 30 hari / TRANSFER KE PROSES CHECKING TTP (AGENCY).
	 * 			flag=2 transferBalikToTtp
	 * 			flag=3 Pemotongan Komisi
	 * @author	Ferry Harlim
	 */
	private void emailCheckingTtp(List lsSpaj,String ket,User currentUser,Integer flag,String subject) {
		String toTemp[] = null,ccTemp[] = null;
		String to=null,cc=null;
		String message="";
		String lca_id="";
		String emailCabang="";
		Date tanggal = commonDao.selectSysdate();
		
		for (int i=0;i<lsSpaj.size();i++){
			Map infoAgen=uwDao.selectInfoAgen2((String)lsSpaj.get(i));
			lca_id = uwDao.selectCabangFromSpaj((String)lsSpaj.get(i));
			String agen=(String)infoAgen.get("MCL_FIRST");
			String kode=(String)infoAgen.get("MSAG_ID");
			String cabang=(String)infoAgen.get("REGION_NAME");
			String nopolis=uwDao.selectNoPolisFromSpaj((String)lsSpaj.get(i));
			emailCabang=(String)infoAgen.get("LAR_EMAIL");;
			message+= i+". "+"\nAgen		: "+agen+
					  "\nKode		: "+kode+
					  "\nCabang	: "+cabang+
					  "\nNopolis	: "+FormatString.nomorPolis(nopolis) +
					  "\n\n";
		}
		message=ket+"\n\n"+message;

		//email
		if(flag==1){
			if(!lca_id.equals("58") && !lca_id.equals("67")){
			to=props.getProperty("ttp.to.1")+";"+emailCabang;
			cc=props.getProperty("ttp.cc.1");}
				else {
				to=props.getProperty("agensys.emailsMall");
				cc=props.getProperty("ttp.cc.mall");
				}
		}else if(flag==2){
			to=props.getProperty("ttp.to.2");
			cc=props.getProperty("ttp.cc.2");
		}else if(flag==3){
			to=props.getProperty("ttp.to.3");
			cc=props.getProperty("ttp.cc.3");
		}
		toTemp=to.split(";");
		ccTemp=cc.split(";");

		try {
//			email.send(false, props.getProperty("admin.ajsjava"), toTemp, ccTemp, null, subject, message, null);
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, tanggal, null,
					false, props.getProperty("admin.ajsjava"), toTemp, ccTemp, null, subject, message, null, null);
		} catch (MailException e) {
			logger.error("ERROR :", e);
		}
		
	}

	public Integer prosesSimpanSlipPemotonganPolis(Deduct deduct,String ubah,User currentUser){
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		if(ubah.equals("0")){//save
			if(this.uwDao.selectMstDeduct(deduct.getMsco_id(), 1)==null){
				this.uwDao.insertMstDeduct(deduct);
			}else{
				this.uwDao.updateMstDeduct(deduct);
			}
			DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");
			String namaProduk=uwDao.selectLstDetBisnisNamaProduk(deduct.getLsbs_id(), deduct.getLsdbs_number());
			String ket="Komisi dapat dibayarkan, namun dilakukan pemotongan komisi sebesar Rp."+df.format(deduct.getPot_biaya()+deduct.getPot_extra()+deduct.getPot_upp())+
			" untuk premi polis produk "+namaProduk;
			List lsSpaj=new ArrayList();
			lsSpaj.add(deduct.getSpaj());
			emailCheckingTtp(lsSpaj, ket, currentUser,3,"Pemotongan Komisi");
			return 1;
		}else if(ubah.equals("2")){//transfer
//			Integer li_pos=7;//ke TTP
			Integer li_pos=8; //ke Finance
			Date sysdate = this.commonDao.selectSysdate();
//			this.uwDao.updatePosisiMst_insured(deduct.getSpaj(), li_pos);//li_pos=7 tanda terima
//			this.uwdao.updatemstinsuredkomisi(deduct.getspaj(), 1, 1,null, currentuser.getlus_id(), false);
//			this.uwdao.updatemst_policy(deduct.getspaj(), null, new double(li_pos), null, null);
////			uwdao.insertmstpositionspaj(currentuser.getlus_id(), "transfer balik ke tanda terima (update tgl komisi)", deduct.getspaj());
//			uwdao.insertmstpositionspaj(currentuser.getlus_id(), "transfer ke finance" + " (Update Tgl Komisi)", deduct.getSpaj());
				
//				PROSES AGENCY KE FINANCE ---dian;
				this.uwDao.updatePosisiMst_insured(deduct.getSpaj(), li_pos);
				this.uwDao.updateMstInsuredKomisi(deduct.getSpaj(), 1, 1,1, currentUser.getLus_id(), false);
				this.uwDao.updateBillingTtp(li_pos, deduct.getSpaj());
				uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "POLIS SUDAH DITRANSFER KE KOMISI FINANCE" + " (Update Tgl Komisi)", deduct.getSpaj(), 0);

				String lsbs = uwDao.selectBusinessId(deduct.getSpaj());
				if(products.stableLink(lsbs)) this.uwDao.updateBillingTtpStableLink(li_pos, deduct.getSpaj());
				else this.uwDao.updateBillingTtp(li_pos, deduct.getSpaj());
				
				//
				Policy policy=this.uwDao.selectDw1Underwriting(deduct.getSpaj(),null,1);
				if(policy.getMspo_date_ttp()==null){
					this.uwDao.updateMst_policy(deduct.getSpaj(), new Double(li_pos), null, sysdate);
					this.uwDao.saveMstTransHistory(deduct.getSpaj(), "tgl_ttp", sysdate, null, null);
				}else{
					this.uwDao.updateMst_policy(deduct.getSpaj(), new Double(li_pos), null, null);
				}
			return 2;
		}else
			return 0;	
	}	
	
	public boolean prosesKomisiErbeAndJurnalKomisiManual(String reg_spaj , BindException errors, User currentUser){
		boolean result = false;
		try {

			if(logger.isDebugEnabled())logger.debug("~ PROSES JURNAL KOMISI UNITLINK ERBE PACKAGE untuk SPAJ " + reg_spaj);
			//untuk ERBE Package akan memproses Jurnal Komisi juga
			int status_jurn_kom = 0;
			Premi premi = this.uwDao.selectPremiTertanggung(reg_spaj);
			premi.setNo_spaj(reg_spaj);
			
			//Proses Komisi ERBE (Input ke MST_Invoice untuk premi pokok dan berkala)	: prose package tanya kebagian Komisi -Otbie				
			Integer li_row = this.bacDao.selectBillingCount(reg_spaj, 1);
			Integer hasil=null;				
			for(int premke= 1; premke<= li_row; premke++){
				hasil = bacDao.prosesKomisiErbePackageSystem(reg_spaj, 1, premke,0);			
			}
			
			//jika null harus cek pada saat pemanggilan kelas "prosesKomisiErbePackageSystem" total billing = total yg keinsert di mst_invoice
			List daftarKomisi = financeDao.selectKomisiAgenErbePackage(reg_spaj,1,li_row);
			if(daftarKomisi.size() == 0){
				result = false;
			}else{
				Date ldt_max_rk = this.commonDao.selectSysdateTruncated(0);
				ldt_max_rk = commonDao.selectTanggalProduksiUntukProsesProduksi(ldt_max_rk, "73", 1);
				
				//Genretae Jurnal ERBE Komisi Pokok
				List daftarKomisiPokok = financeDao.selectKomisiAgenErbePackage(reg_spaj,1,1);
				
				if(daftarKomisiPokok.size() != 0){
					status_jurn_kom = jurnal.jurnalMemorialPremiUlinkKomisiERBEPackagePremiPokok(premi,ldt_max_rk,daftarKomisiPokok, errors, currentUser);
				}else{
					status_jurn_kom = 0;
				}
				
				//Genretae Jurnal ERBE Komisi Pokok 
				List daftarKomisitopup = financeDao.selectKomisiAgenErbePackage(reg_spaj,1,2);
				
				if(daftarKomisitopup.size() != 0){
					status_jurn_kom = jurnal.jurnalMemorialPremiUlinkKomisiERBEPackagePremiTopup(premi,ldt_max_rk,daftarKomisitopup, errors, currentUser);
				}else{
					status_jurn_kom = 0;
				}
				
				if(status_jurn_kom==0){
					result = false;					
				}else{
					result = true;	
				}
			}	
			
			if (result == false){
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();				
			}
		
		}catch (Exception e) {
			logger.error("ERROR :", e);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();			
		}
		
		return result;
	}
	
	/*public boolean transferToPrintPolisTest(String reg_spaj, BindException errors, int bulanRK, List billInfo, User currentUser, String li_retur, String li_returTopUp,String from, ElionsManager elionsManager, UwManager uwManager) throws Exception{
	Map m = uwManager.selectInformasiEmailSoftcopy(reg_spaj);
	String email = (String) m.get("MSPE_EMAIL");

	if(!Common.isEmpty(email)){
		Boolean EmailOtomatis =softcopy.softCopyOtomatisBTN(reg_spaj, currentUser, email);
		if(EmailOtomatis==false){
			errors.reject("payment.softCopyFailed");
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			throw new Exception(errors);
		}
	}
	return false;
}*/
}