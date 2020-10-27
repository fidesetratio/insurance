package com.ekalife.elions.process;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.validation.BindException;

import com.ekalife.elions.model.Premi;
import com.ekalife.elions.model.RekEkalife;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.RomanNumber;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.parent.ParentDao;

public class Sequence extends ParentDao {

	//untuk insert EKA.MST_DATA_BMI
	public String sequenceDataBmi() {
		String sekuens = this.uwDao.selectGetCounter2(91, "01");

		String tahun = sekuens.substring(0, 4);
		String urut  = sekuens.substring(4);

		Date now = commonDao.selectSysdate();
		String tahunNow = (new SimpleDateFormat("yyyy")).format(now);
		String bulanNow = (new SimpleDateFormat("MM")).format(now);
		
		if(!tahun.equals(tahunNow)) {
			urut = "00000"; //urut direset hanya bila tahunnya beda
		}

		urut = FormatString.rpad("0", String.valueOf(Integer.parseInt(urut) + 1), 5);
		
		this.uwDao.updateCounter(tahunNow+urut, 91, "01");
		
		String hasil = tahunNow + bulanNow + urut;
		
		return hasil;
	}
	
	public String sequenceDuplikatPolis(String reg_spaj) {
		String sekuens = this.uwDao.selectGetCounter2(90, "01");
		
		String tahun = sekuens.substring(0, 4);
		String bulan = sekuens.substring(4, 6);
		String urut  = sekuens.substring(6);

		Date now = commonDao.selectSysdate();
		String tahunNow = (new SimpleDateFormat("yyyy")).format(now);
		String bulanNow = (new SimpleDateFormat("MM")).format(now);
		
		if(!(tahun.equals(tahunNow) && bulan.equals(bulanNow))) {
			if(!tahun.equals(tahunNow)) urut = "00000"; //urut direset hanya bila tahunnya beda
			tahun = tahunNow;
			bulan = bulanNow;
		}

		urut = FormatString.rpad("0", String.valueOf(Integer.parseInt(urut) + 1), 5);
		
		this.uwDao.updateCounter(tahun+bulan+urut, 90, "01");
		
		//String hasil = urut + "/" + bulan + "/" + tahun;
		//YUSUF (14 JUL 09) REQUEST MBA ASRI VIA HELPDESK #14188 (No. surat/bulan(huruf romawi)/1(duplikat pertama)dst/tahun)
		int ke = commonDao.selectJumlahDuplikatPolis(reg_spaj) + 1;
		RomanNumber bulanRomawi = new RomanNumber(Integer.parseInt(bulan));
		String hasil = urut + "/" + bulanRomawi.toRomanValue() + "/" + ke + "/" + tahun;
		
		return hasil;
	}
	
	public String sequenceNoVoucher(String mtb_gl_no, String rek_id, String nomor){
		RekEkalife rekEka = new RekEkalife();
		int lsrek_id = Integer.parseInt(rek_id);
		rekEka.setLsrek_id(lsrek_id);
		String kas = this.accountingDao.selectJenisKas(nomor);
		String ar_no_voucher = "";
		String ls_simbol = "";
		
		//START
		rekEka.setLsrek_ekalife(this.accountingDao.selectVoucher(lsrek_id));
		int Lsrek_no_cr = rekEka.getLsrek_ekalife().get(0).getLsrek_no_cr();
		int Lsrek_no_cd = rekEka.getLsrek_ekalife().get(0).getLsrek_no_cd();
		ls_simbol = rekEka.getLsrek_ekalife().get(0).getLsrek_symbol();
		Integer lsrek_no_run = 0;
		String ls_flag = null;
		if(kas.equals("M")){
//			if(rekEka.getLsrek_no_cr() == null){
//				rekEka.setLsrek_no_cr(0);
//			}
//			Lsrek_no_cr += 1;
//			lsrek_no_run = Lsrek_no_cr;
//			ls_flag = "R";
			ar_no_voucher = this.accountingDao.selectNoVoucherCr(ls_simbol);
		}else if(kas.equals("K")){
//			if(rekEka.getLsrek_ekalife().get(0).getLsrek_no_cd() == null){
//				rekEka.getLsrek_ekalife().get(0).setLsrek_no_cd(0);
//			}
//			Lsrek_no_cd += 1;
//			lsrek_no_run = Lsrek_no_cd;
//			ls_flag= "D";
			ar_no_voucher = this.accountingDao.selectNoVoucherCd(ls_simbol);			
		}
		
//		if(kas.equals("K")){
//			this.accountingDao.updateRekEkalifeCD(Lsrek_no_cd, rek_id);
//		}else {
//			this.accountingDao.updateRekEkalifeCR(Lsrek_no_cr, rek_id);
//		}
		String lsrek_symbol = rekEka.getLsrek_ekalife().get(0).getLsrek_symbol();
		if(lsrek_symbol.contains(" ")){
			lsrek_symbol = lsrek_symbol.trim();
		}
		
		String hasil=FormatString.rpad("", lsrek_symbol, 4)+FormatString.rpad("0",Integer.toString(lsrek_no_run) , 5) + ls_flag;
		return hasil;
	}
	
	/**
	 * sequence NOMOR PRE (MSCO_NUMBER = 32)
	 * @return (2 digit tahun + 2 digit bulan + 5 digit sequence)
	 */
	public String sequenceNoPre() {
		String sekuens = this.uwDao.selectGetCounter(32, "01");
		DateFormat df = new SimpleDateFormat("yyMM");
		String yymm = df.format(this.commonDao.selectSysdateTruncated(0));
		this.uwDao.updateCounter(sekuens, 32, "01");
		String hasil = yymm + FormatString.rpad("0", sekuens, 5);
		
		return hasil;
	}
	
	/**
	 * sequence EKA.MST_FORM (MSCO_NUMBER = 73)
	 * @return (2 digit cabang + 4 digit tahun + 4 digit sequence)
	 */
	public String sequenceMst_form(int mss_jenis, String cabang) {
		String sekuens = this.uwDao.selectGetCounter(73, cabang);
		DateFormat df = new SimpleDateFormat("yy");
		String year = df.format(this.commonDao.selectSysdateTruncated(0));
		this.uwDao.updateCounter(sekuens, 73, cabang);

		String hasil = cabang + (mss_jenis==0?"C":"A") + year + FormatString.rpad("0", sekuens, 5);
		
		if(logger.isDebugEnabled())logger.debug("SEQUENCE: untuk EKA.MST_FORM terakhir adalah: " + hasil);
		
		return hasil;
		
	}
	
	/**
	 * 
	 * MST_COUNTER NO 112
	 * Format : 
	 * YYYY-MM-XXXXX (4 digit tahun, 2 digit bulan, 5 digit sequence)
	 * 
	 * @author Yusuf
	 * @since 5 Jul 2010
	 * @return
	 */
	public String sequenceNoRegPencairan(Date cair) {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String result 		= "";
		int sekuens 		= Integer.valueOf(this.uwDao.selectGetCounter2(112, "01"));
		String yyyymm 		= this.uwDao.selectGetCounterMonthYear(112, "01");
		DateFormat dfyyyymm	= new SimpleDateFormat ("yyyyMM");
		String cairDate		= dfyyyymm.format(cair);
		
		Date sysdate = commonDao.selectSysdate();
		
		if(Integer.parseInt(dfyyyymm.format(sysdate)) > Integer.parseInt(yyyymm)){
			sekuens = 1;
			this.uwDao.updateCounter(String.valueOf(sekuens), 112, "01");
			this.uwDao.updateCounterMonthYear(cairDate, 112, "01");
		}else{
			sekuens++;
			this.uwDao.updateCounter(String.valueOf(sekuens), 112, "01");
		}
		
		result = yyyymm + FormatString.rpad("0", String.valueOf(sekuens), 5);
		
		return result;
	}
	
	public String sequenceKdScan() {
//		int sekuens = Integer.valueOf(this.uwDao.selectGetCounter2(134, "01"));
//		DateFormat dfYY = new SimpleDateFormat("yy");
//		Date now = this.commonDao.selectSysdateTruncated(0);
//		String valueCounter = FormatString.rpad("0", String.valueOf(sekuens), 6);
//		String result = dfYY.format(now) + valueCounter ;
//		
//		if(uwDao.selectCountKdScan(dfYY.format(now))==0){
//			sekuens=1;
//		}else{
//			sekuens++;
//		}
//		this.uwDao.updateCounter(String.valueOf(sekuens), 134, "01");
		String result = uwDao.selectSeqScanId();
		
		return result;
		
	}
	
	/**
		Rudy : Cup, untuk no_reg di mst_slink ada salah format, waktu itu sih kayanya emang gw ga kasih tau lu, 
		harusnya : 000010608
		00001 = angka yg ada di mst_counter number 83
		06      = Bulan
		08      = Tahun
	 * @author Yusuf
	 * @since Jun 23, 2008 (8:22:11 AM)
	 * @return
	 */
	public String sequenceNoRegStableLink() {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String sekuens = this.uwDao.selectGetCounter2(83, "01");
		String bulanSekuens = sekuens.substring(sekuens.length()-2);
		DateFormat dfMM = new SimpleDateFormat("MM");
		DateFormat dfYY = new SimpleDateFormat("yy");
		Date now = this.commonDao.selectSysdateTruncated(0);
		String bulan = dfMM.format(now); 
		int urut = 1;
		if(bulan.equals(bulanSekuens)) {
			urut = Integer.parseInt(sekuens.substring(0, sekuens.length()-2));
			urut += 1;
		}
		sekuens = FormatString.rpad("0", String.valueOf(urut), 5) + bulan;
		this.uwDao.updateCounter(sekuens, 83, "01");
		
		String result = sekuens + dfYY.format(now);
		
		this.uwDao.insertTestStable(result, new Date(), Integer.valueOf(sekuens), 9);
			
		return result;
//		String sekuens = this.uwDao.selectGetCounter(83, "01");
//		DateFormat df = new SimpleDateFormat("MMyy");
//		String mmyy = df.format(this.commonDao.selectSysdateTruncated(0));
//		this.uwDao.updateCounter(sekuens, 83, "01");
//		String hasil = FormatString.rpad("0", sekuens, 5) + mmyy;
//		return hasil;
	}
	
	public String sequenceNoRegPsaveBaru() {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String sekuens = this.uwDao.selectGetCounter2(128, "01");
		String bulanSekuens = sekuens.substring(sekuens.length()-2);
		DateFormat dfMM = new SimpleDateFormat("MM");
		DateFormat dfYY = new SimpleDateFormat("yy");
		Date now = this.commonDao.selectSysdateTruncated(0);
		String bulan = dfMM.format(now); 
		int urut = 1;
		if(bulan.equals(bulanSekuens)) {
			urut = Integer.parseInt(sekuens.substring(0, sekuens.length()-2));
			urut += 1;
		}
		sekuens = FormatString.rpad("0", String.valueOf(urut), 5) + bulan;
		this.uwDao.updateCounter(sekuens, 128, "01");
		
		String result = sekuens + dfYY.format(now);
		
		this.uwDao.insertTestStable(result, new Date(), Integer.valueOf(sekuens), 9);
			
		return result;
//		String sekuens = this.uwDao.selectGetCounter(83, "01");
//		DateFormat df = new SimpleDateFormat("MMyy");
//		String mmyy = df.format(this.commonDao.selectSysdateTruncated(0));
//		this.uwDao.updateCounter(sekuens, 83, "01");
//		String hasil = FormatString.rpad("0", sekuens, 5) + mmyy;
//		return hasil;
	}

	/**
	 * ini untuk MST_SLINK_BAYAR
		kalo yg 83 isinya contoh "000200709", 5 digit counter, 4 digit bulan tahun -> 
		kalo yg 84 isinya contoh "00020STL072009 ", 5 digit counter, 3 digit STL, 6 digit bulan tahun
	 * @author yusuf
	 * @since Jul 28, 2009 (9:59:08 AM)
	 * @return
	 */
	public String sequenceNoRegSlinkBayar() {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String sekuens = this.uwDao.selectGetCounter2(84, "01");
		DateFormat dfMM = new SimpleDateFormat("MM");
		DateFormat dfYYYY = new SimpleDateFormat("yyyy");
		Date now = this.commonDao.selectSysdateTruncated(0);

		int urut = Integer.parseInt(sekuens);
		urut += 1;
		sekuens = String.valueOf(urut);
		this.uwDao.updateCounter(sekuens, 84, "01");
		
		String result = FormatString.rpad("0", sekuens, 5) + "STL" + dfMM.format(now) + dfYYYY.format(now);
		
		return result;
	}

	/**
	 * ini untuk MST_SSAVE_BAYAR
		kalo yg 83 isinya contoh "000200709", 5 digit counter, 4 digit bulan tahun -> 
		kalo yg 84 isinya contoh "00020SSV072009 ", 5 digit counter, 3 digit STL, 6 digit bulan tahun
	 * @author yusuf
	 * @since 20 Aug, 2009 
	 * @return
	 */
	public String sequenceNoRegSsaveBayar() {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String sekuens = this.uwDao.selectGetCounter2(108, "01");
		DateFormat dfMM = new SimpleDateFormat("MM");
		DateFormat dfYYYY = new SimpleDateFormat("yyyy");
		Date now = this.commonDao.selectSysdateTruncated(0);

		int urut = Integer.parseInt(sekuens);
		urut += 1;
		sekuens = String.valueOf(urut);
		this.uwDao.updateCounter(sekuens, 108, "01");
		
		String result = FormatString.rpad("0", sekuens, 5) + "SSV" + dfMM.format(now) + dfYYYY.format(now);
		
		return result;
	}

	public String sequenceSertifikatSimasPrima() {
		String sekuens = this.uwDao.selectGetCounter(80, "01");
		this.uwDao.updateCounter(sekuens, 80, "01");
		String hasil = FormatString.rpad("0", sekuens, 8);
		return hasil;
	}
	
	public String sequenceSertifikatSimasSekuritas() {
		String sekuens = this.uwDao.selectGetCounter(102, "01");
		this.uwDao.updateCounter(sekuens, 102, "01");
		String hasil = FormatString.rpad("0", sekuens, 8);
		return hasil;
	}
	
	public String sequenceKuitansi() {
		String sekuens = this.uwDao.selectGetCounter(122,"01");
		this.uwDao.updateCounter(sekuens, 122, "01");
		String hasil = FormatString.rpad("0", sekuens, 8);
		return hasil;
	}
	
	/**
	 * sequence EKA.MST_BATAL
	 * @return (2 digit cabang + 4 digit tahun + "B" + 3 digit terakhir dari sequence)
	 */
	public String sequenceMst_batal(int aplikasi, String cabang) {
		String sekuens = this.uwDao.selectGetCounter(aplikasi, cabang);
		DateFormat df = new SimpleDateFormat("yyyy");
		String yearNow = df.format(this.commonDao.selectSysdateTruncated(0));
		String year = yearNow;
		if (sekuens.equals("1")) {
			year = yearNow;
		} else if (sekuens.length() == 9) {
			year = sekuens.substring(0, 4);
			sekuens = sekuens.substring(sekuens.length() - 3, sekuens.length());
			if (year != yearNow) {
				year = yearNow;
				sekuens = "1";
			}
		}
		sekuens = String.valueOf((Long.parseLong(year) * 10000)
				+ Long.parseLong(sekuens));
		this.uwDao.updateCounter(sekuens, aplikasi, cabang);

		String hasil = cabang + year + 'B'
				+ sekuens.substring(sekuens.length() - 3, sekuens.length());
		
		if(logger.isDebugEnabled())logger.debug("SEQUENCE: untuk EKA.MST_BATAL terakhir adalah: " + hasil);
		
		return hasil;
	}	

	/**
	 * sequence EKA.MST_PAYMENT
	 * @return (2 digit kode cabang + 10 digit sequence)
	 */
	public String sequenceMst_payment(int aplikasi, String cabang) {
		//(12 May 2014) Deddy - Pakai Seq langsung dari oracle.
//		String sekuens = this.uwDao.selectGetCounter(aplikasi, cabang);
//		this.uwDao.updateCounter(sekuens, aplikasi, cabang);
//		String hasil = cabang + FormatString.rpad("0", sekuens, 10);
//		if(logger.isDebugEnabled())logger.debug("SEQUENCE: untuk EKA.MST_PAYMENT terakhir adalah: " + hasil);
		String hasil = uwDao.selectSeqPaymentId();
		return hasil;
	}
	
	/**
	 * sequence untuk BLANKO SPAJ OTOMATIS
	 * @author Yusuf Sutarko
	 * @since Sep 18, 2007 (5:28:14 PM)
	 * @param aplikasi
	 * @param cabang
	 * @return
	 */
	public String sequenceSpajElektronik(int aplikasi, String cabang) {
		String sekuens = this.uwDao.selectGetCounter(aplikasi, cabang);
		this.uwDao.updateCounter(sekuens, aplikasi, cabang);
		String hasil = "E" + FormatString.rpad("0", sekuens, 6);
		return hasil;
	}

	/**
	 *	sequence nomor voucher untuk transfer input_pre
	 */
	public String sequenceNo_Voucher_Pre(String mtb_gl_no){
		
		String hasil="";
		return hasil;
	}
	
	
	/**
	 *	sequence nomor voucher 
	 */
	public Premi sequenceVoucherPremiIndividu(Premi premi, String ls_no_pre, String ls_no_voucher, Integer li_cr_bayar, BindException errors) throws Exception{
		Long ll_cr = null;
		String ls_simbol = "";
		boolean lselect = false;
		Map tmp;
		premi.setSaldo_akhir(new Double(0));
		if(li_cr_bayar!=17 && li_cr_bayar!=99){
			tmp = this.uwDao.selectCounterRekEkalife(premi.getRek_id());
			if(tmp == null){
				errors.reject("payment.generateCounterRekEkalifeFailed");
				throw new Exception(errors);
			}else{
				ls_simbol = (String) tmp.get("LSREK_SYMBOL");
				ll_cr = new Long(((BigDecimal) tmp.get("LSREK_NO_CR")).longValue());
				premi.setAccno((String) tmp.get("LSREK_GL_NO"));
				lselect = true;
			}
		}else{
			premi.setAccno("01400203");
		}
		
		if(lselect){
			if(ll_cr == null)ll_cr = new Long(0);
			ll_cr = this.accountingDao.selectNewCrNo("");
			//premi.setNo_cr(new Long(ll_cr.longValue()+1));
			premi.setNo_cr(ll_cr);
			if(ls_simbol==null) ls_simbol = "";
			if(!StringUtil.isEmpty(ls_no_pre)){
				premi.setVoucher(refundDao.selectNoVoucher(ls_no_pre));
			}else{
				premi.setVoucher(ls_simbol.trim() + FormatString.rpad("0", premi.getNo_cr().toString(), 5) + "R");
			}
			
			if(StringUtil.isEmpty(ls_no_voucher.trim())){
				premi.setLs_no_voucher(premi.getVoucher());
			}else{
				premi.setLs_no_voucher(ls_no_voucher + ", " + premi.getVoucher());
			}
		}
	
		//this.uwDao.updateLst_rek_ekalife(premi);
		
		if(logger.isDebugEnabled())logger.debug("SEQUENCE: untuk LST_REK_NOCR terakhir adalah: " + premi.getNo_cr() + " dan voucher terakhir adalah: " + premi.getVoucher());
		
		return premi;
	}

	/**
	 *	sequence EKA.MST_PTC_TM 
	 */
	public String sequenceMst_ptc_tm(int aplikasi) {
		String sekuens = this.uwDao.selectGetCounter(aplikasi, "01");
		if(Integer.parseInt(sekuens)<0){
			return null;
		}else{
			this.uwDao.updateCounter(sekuens, aplikasi, "01");
			DateFormat df = new SimpleDateFormat("MMyy");
			String hasil = FormatString.rpad("0", sekuens, 5)+df.format(this.commonDao.selectSysdateTruncated(0));
			if(logger.isDebugEnabled())logger.debug("SEQUENCE: untuk EKA.MST_PTC_TM (jurnal memo) terakhir adalah: " + hasil);
			return hasil;
		}
		
	}

	/**
	 *	sequence nomor pre 
	 */
	public String sequenceNo_pre(int aplikasi) {
		String sekuens = this.uwDao.selectGetCounter(aplikasi, "01");
		this.uwDao.updateCounter(sekuens, aplikasi, "01");
		DateFormat df = new SimpleDateFormat("yyMM");
		
		String hasil = df.format(this.commonDao.selectSysdateTruncated(0)) + FormatString.rpad("0", sekuens, 5);
		
		if(logger.isDebugEnabled())logger.debug("SEQUENCE: untuk NO_PRE terakhir adalah: " + hasil);
	
		return hasil;
	}
	
	/**
	 * sequence EKA.MST_CLIENT_NEW
	 * @return (2 digit cabang, 10 digit sequence)
	 */
	public String sequenceMst_client_new(String cabang) {
	/*	String sekuens = uwDao.selectGetCounter(3, cabang);
		uwDao.updateCounter(sekuens, 3, cabang);

		String hasil = cabang + FormatString.rpad("0", sekuens, 10);
		
		if(logger.isDebugEnabled())logger.debug("SEQUENCE: untuk MST_CLIENT_NEW terakhir adalah: " + hasil);
	*/
		String hasil = bacDao.selectSequenceClientID();	
		return hasil;
	}
		
	/**
	 * sequence EKA.MST_POLICY (reg_spaj)
	 * @return (2 digit cabang agen, 4 digit tahun, 5 digit sequence) (khusus spaj, tahunnya kesimpen di mst_counter)
	 */
	public String sequenceMst_policy(Map regionalAgen) {
		String cabang = (String) regionalAgen.get("STRBRANCH");
		String sekuens = uwDao.selectGetCounter(1, cabang);
		uwDao.updateCounter(sekuens, 1, cabang);

		String hasil = cabang + FormatString.rpad("0", sekuens, 5);
		
		if(logger.isDebugEnabled())logger.debug("SEQUENCE: untuk MST_CLIENT_NEW terakhir adalah: " + hasil);
	
		return hasil;
	}
	
	/**
	 * sequence untuk counter no endors di mst_endors
	 * @param cabang
	 * @return
	 * Filename : Sequence.java
	 * Create By : Bertho Rafitya Iwasurya
	 * Date Created : Aug 6, 2009 9:28:47 AM
	 */
	public String sequenceNo_Endorse(String cabang){ //GAK PAKE COUNTER LAGI, PAKAI SEQUENCE ORACLE
//		String ld_no;
//		Long ll_th_now, ll_ctl, ll_th;
//		String ls_noend=null;
//		DateFormat df = new SimpleDateFormat("yyyy");
//		
//		ll_th_now =  new Long(df.format(this.commonDao.selectSysdateTruncated(0)));
//		ld_no = uwDao.selectGetCounter2(5, cabang);
//		if(ld_no==null)ld_no="0";
//		
//		if(ld_no.equals("0")){
//			ll_th=ll_th_now;
//			ll_ctl=1l;
//		}else if(ld_no.length()==9){
//			ll_th=new Long(ld_no.substring(0, 4));
//			ll_ctl=new Long(ld_no.substring(ld_no.length()-5, ld_no.length()));
//			ll_ctl++;
//			
//			if(ll_th.longValue()!=ll_th_now.longValue()){
//				ll_th=ll_th_now;
//				ll_ctl=1l;
//			}
//		}else{
//			return ls_noend;
//		}
//		
//		ld_no=""+((ll_th*100000)+ll_ctl);
//		
//		uwDao.updateCounter(ld_no, 5, cabang);
//		
//		ls_noend=cabang+ld_no.substring(0,4)+"E"+ld_no.substring(ld_no.length()-5,ld_no.length());
//
//		return ls_noend;
		return uwDao.selectSeqNoEndors();
	}
	//dipakai buat endors bancass,
	public String sequenceNo_Endorse2(String cabang){
//		String ld_no;
//		Long ll_th_now, ll_ctl, ll_th;
//		String ls_noend=null;
//		String ww ="WW";
//		DateFormat df = new SimpleDateFormat("yyyy");
//		Date sysdate = commonDao.selectSysdate();
//		Integer month=sysdate.getMonth()+1;
//		ll_th_now =  new Long(df.format(this.commonDao.selectSysdateTruncated(0)));
//		ld_no = uwDao.selectGetCounter2(5, ww);
//		if(ld_no==null)ld_no="0";
//		
//		if(ld_no.equals("0")){
//			ll_th=ll_th_now;
//			ll_ctl=1l;
//		}else if(ld_no.length()==9){
//			ll_th=new Long(ld_no.substring(0, 4));
//			ll_ctl=new Long(ld_no.substring(ld_no.length()-5, ld_no.length()));
//			ll_ctl++;
//			
//			if(ll_th.longValue()!=ll_th_now.longValue()){
//				ll_th=ll_th_now;
//				ll_ctl=1l;
//			}
//		}else{
//			return ls_noend;
//		}
//		
//		ld_no=""+((ll_th*100000)+ll_ctl);
//		
//		uwDao.updateCounter(ld_no, 5, ww);
//		//ubah no endors, untuk format bulan 2 angka (10,11,12)
//		ls_noend=month+ld_no.substring(0,4)+"E"+ld_no.substring(ld_no.length()-5,ld_no.length());
//
//		return ls_noend;
		return uwDao.selectSeqNoEndors();
	}

	public String sequenceNo_Endorse3(String cabang){
		String ld_no;
		Long ll_th_now, ll_ctl, ll_th;
		String ls_noend=null;
		String ww ="01";
		DateFormat df = new SimpleDateFormat("yy");
		Date sysdate = commonDao.selectSysdate();
		Integer month=sysdate.getMonth()+1;
		ll_th_now =  new Long(df.format(this.commonDao.selectSysdateTruncated(0)));
		ld_no = uwDao.selectGetCounter2(138, ww);
		if(ld_no==null)ld_no="0";
		
		if(ld_no.equals("0")){
			ll_th=ll_th_now;
			ll_ctl=1l;
		}
			ll_th=new Long(ld_no.substring(0, 4));
			ll_ctl=new Long(ld_no.substring(ld_no.length()-5, ld_no.length()));
			ll_ctl++;
			
			if(ll_th.longValue()!=ll_th_now.longValue()){
				ll_th=ll_th_now;
				//ll_ctl=1l;
			}
		
		ld_no=""+((ll_th*1000000)+ll_ctl);
		
		uwDao.updateCounter(ld_no, 138, ww);
		
		ls_noend="EA"+ll_th_now+ld_no.substring(ld_no.length()-6,ld_no.length());

		return ls_noend;
	}
	
	/**
	 * Blanko E-SPAJ SYARIAH
	 * @return String sequence
	 *
	 * @author MANTA
	 * @since 9/7/2013
	 */
	public String sequenceNo_Endorse4(String cabang){
		String ld_no;
		Long ll_th_now, ll_ctl, ll_th;
		String ls_noend=null;
		String ww ="01";
		DateFormat df = new SimpleDateFormat("yy");
		Date sysdate = commonDao.selectSysdate();
		Integer month=sysdate.getMonth()+1;
		ll_th_now =  new Long(df.format(this.commonDao.selectSysdateTruncated(0)));
		ld_no = uwDao.selectGetCounter2(156, ww);
		if(ld_no==null)ld_no="0";
		
		if(ld_no.equals("0")){
			ll_th=ll_th_now;
			ll_ctl=1l;
		}
			ll_th=new Long(ld_no.substring(0, 4));
			ll_ctl=new Long(ld_no.substring(ld_no.length()-5, ld_no.length()));
			ll_ctl++;
			
			if(ll_th.longValue()!=ll_th_now.longValue()){
				ll_th=ll_th_now;
				//ll_ctl=1l;
			}
		
		ld_no=""+((ll_th*1000000)+ll_ctl);
		
		uwDao.updateCounter(ld_no, 156, ww);
		
		ls_noend="ES"+ll_th_now+ld_no.substring(ld_no.length()-6,ld_no.length());
		
		return ls_noend;
	}
	
	/**
	 * sequence untuk no blanko spaj ASM
	 * @return String sequence
	 *
	 * @author yusup_a
	 * @since Aug 31, 2010 (10:53:21 AM)
	 */
	public String sequenceNoBlankoAsm() {
		String sequence = uwDao.selectGetCounter(114, "XX");
		uwDao.updateCounter(sequence, 114, "XX");
		
		return "ASM" + FormatString.rpad("0", sequence, 6);
	}
	
	/**
	 * sequence untuk me id di mst_email
	 * @return String sequence
	 * 
	 * @author Deddy
	 * @since Sep 6,2012 (09:01:10 AM)
	 */
	
	public String sequenceMeIdEmail() {
//		String sequence = uwDao.selectGetCounter(144, "01");
//		DateFormat df = new SimpleDateFormat("yyyyMMdd");
//		Date sysdate = commonDao.selectSysdate();
//		Long ll_th_now =  new Long(df.format(this.commonDao.selectSysdateTruncated(0)));
//		String urut = FormatString.rpad("0", String.valueOf(Integer.parseInt(sequence)), 6);
//		String ls_noend= ll_th_now+urut;
//		uwDao.updateCounter(String.valueOf(Integer.parseInt(sequence)), 144, "01");
		String ls_noend = uwDao.selectSeqEmailId();
		return ls_noend;
	}
	
	public String sequenceNoKloter(String cabang){
		String ld_no;
		Long ll_th_now, ll_ctl, ll_th;
		String ls_noend=null;
		String ww ="01";
		DateFormat df = new SimpleDateFormat("yy");
		Date sysdate = commonDao.selectSysdate();
		Integer month=sysdate.getMonth()+1;
		ll_th_now =  new Long(df.format(this.commonDao.selectSysdateTruncated(0)));
		ld_no = uwDao.selectGetCounter2(165, ww);
		if(ld_no==null)ld_no="0";
		
		if(ld_no.equals("0")){
			ll_th=ll_th_now;
			ll_ctl=1l;
		}
			ll_th=new Long(ld_no.substring(0, 4));
			ll_ctl=new Long(ld_no.substring(ld_no.length()-4, ld_no.length()));
			ll_ctl++;
			
			if(ll_th.longValue()!=ll_th_now.longValue()){
				ll_th=ll_th_now;
				//ll_ctl=1l;
			}
		
		ld_no=""+((ll_th*1000000)+ll_ctl);
		
		uwDao.updateCounter(ld_no, 165, ww);
		
		ls_noend="UPLOAD"+ll_th_now+ld_no.substring(ld_no.length()-6,ld_no.length());

		return ls_noend;
	}

	public String sequenceNoSuratTolakKesehatan(int msco_number, String lca) {
		String sekuens = this.uwDao.selectGetCounter2(msco_number, lca);
		
		String tahun = sekuens.substring(0, 4);
		String bulan = sekuens.substring(4, 6);
		String urut  = sekuens.substring(6);

		Date now = commonDao.selectSysdate();
		String tahunNow = (new SimpleDateFormat("yyyy")).format(now);
		String bulanNow = (new SimpleDateFormat("MM")).format(now);
		
		if(!(tahun.equals(tahunNow) && bulan.equals(bulanNow))) {
			if(!tahun.equals(tahunNow)) urut = "00000"; //urut direset hanya bila tahunnya beda
			tahun = tahunNow;
			bulan = bulanNow;
		}

		urut = FormatString.rpad("0", String.valueOf(Integer.parseInt(urut) + 1), 5);
		
		this.uwDao.updateCounter(tahun+bulan+urut, msco_number, lca);
		
		
		
		String hasil = urut + "/Ind/Rider/" + bulan +"/"+ tahun;
		
		return hasil;
	}
	
	public String sequenceMscoNoCommission(int msco_number, String lca){
		String sekuens = this.uwDao.selectGetCounter2(msco_number, lca);
		Date now = commonDao.selectSysdate();
		String tahunNow = (new SimpleDateFormat("yy")).format(now);
		String bulanNow = (new SimpleDateFormat("MM")).format(now);
		this.uwDao.updateCounter(sekuens, msco_number, lca);
		
		String hasil = "1.3-" + bulanNow + "." + tahunNow + "-" + FormatString.rpad("0", sekuens, 7);;
		
		return hasil;
		
		//select msco_value counter_id from EKA.MST_COUNTER where msco_number = 101 and lca_id = '01'
		//select '1.3-'||to_char(sysdate, 'MM.YY')||'-'|| lpad(counter_id, 7, '0000000')     from dual 
		//update counter +1;
	}
	
	/**
	 * @author MANTA
	 * Untuk No Pembatalan Non SPAJ
	 */
	public String sequenceNoRegRefundNonSPAJ() {
		DateFormat dfYY = new SimpleDateFormat("yyyy");
		Date now = this.commonDao.selectSysdateTruncated(0);
		
		String sekuens = this.uwDao.selectGetCounter2(174, "01");
		String tahunSekuens = sekuens.substring(0,4);
		String tahun = dfYY.format(now); 
		int urut = 1;
		
		if(tahun.equals(tahunSekuens)) {
			urut = Integer.parseInt(sekuens.substring(4));
			urut += 1;
		}
		
		sekuens = tahun + FormatString.rpad("0", String.valueOf(urut), 5);
		
		this.uwDao.updateCounter(sekuens, 174, "01");
		
		String result = "RS" + sekuens;
		
		return result;
	}
	
	public String sequenceNoAntrian(String cabang){
		String ld_no;
		Long ll_th_now, ll_ctl, ll_th;
		String ls_noend=null;
		String ww ="01";
		DateFormat df = new SimpleDateFormat("yy");
		Date sysdate = commonDao.selectSysdate();
		Integer month=sysdate.getMonth()+1;
		ll_th_now =  new Long(df.format(this.commonDao.selectSysdateTruncated(0)));
		ld_no = uwDao.selectGetCounter2(212, ww);
		if(ld_no==null)ld_no="0";
		
		if(ld_no.equals("0")){
			ll_th=ll_th_now;
			ll_ctl=1l;
		}
			ll_th=new Long(ld_no.substring(0, 4));
			ll_ctl=new Long(ld_no.substring(ld_no.length()-4, ld_no.length()));
			ll_ctl++;
			
			if(ll_th.longValue()!=ll_th_now.longValue()){
				ll_th=ll_th_now;
				//ll_ctl=1l;
			}
		
		ld_no=""+((ll_th*1000000)+ll_ctl);
		
		uwDao.updateCounter(ld_no, 212, ww);
		
		ls_noend="BM"+ll_th_now+ld_no.substring(ld_no.length()-6,ld_no.length());

		return ls_noend;
	}
	
	public String sequenceNoSuratPenjaminanProvider() {
		String sekuens = this.uwDao.selectGetCounter2(229, "01");

		String tahun = sekuens.substring(0, 4);
		String urut  = sekuens.substring(4);

		Date now = commonDao.selectSysdate();
		String tahunNow = (new SimpleDateFormat("yyyy")).format(now);
		String bulanNow = (new SimpleDateFormat("MM")).format(now);
		
		if(!tahun.equals(tahunNow)) {
			urut = "0000"; //urut direset hanya bila tahunnya beda
		}

		urut = FormatString.rpad("0", String.valueOf(Integer.parseInt(urut) + 1), 4);
		
		this.uwDao.updateCounter(tahunNow+urut, 229, "01");
		
		String hasil =  urut;
		
		return hasil;
	}
	
	
}
