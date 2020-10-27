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

import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;

import com.ekalife.elions.model.DrekDet;
import com.ekalife.elions.model.Payment;
import com.ekalife.elions.model.Premi;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.parent.ParentDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * (Yusuf) as of 22 jun 2012 -> semua yang berbau2 jurnal, ptc tm, ptc jm, dbank, tbank, bvoucher, semua pakai kurs bulanan, bukan kurs harian (request Tri akunting)
 * 
 * @author Yusuf
 * @since 2007
 *
 */
public class Jurnal extends ParentDao {
	protected final static Log logger = LogFactory.getLog( Jurnal.class );

	public static void main(String[] args) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date a = df.parse("01/05/2008");
			Date b = df.parse("30/04/2008");
			//ini berarti a > b
			logger.info(FormatDate.dateDifference(b, a, false)>0);
		} catch (ParseException e) {
			logger.error("ERROR :", e);
		} 
	}

	/**
	 * Yusuf (15/05/2012) - Req Akunting (Jumoro, Charles, Derry) via Rudy via email - PP syariah dibagi TABARRU (802) vs UJROH (801)
	 * Fungsi ini berguna untuk menarik persentase tabarru vs ujroh (total = 100%)
	 * 
	 * @param reg_spaj
	 * @return
	 */
	private Double getPremiSyariah(String reg_spaj, Integer tahun_ke, Integer premi_ke, Integer flag){
		//flag : 1 = tabaru, 2 = ujrah, 3 = wakalah, 4 = mudarobah
		Double premi = financeDao.selectGetPremiSyariah(reg_spaj, tahun_ke, premi_ke, flag);
		return premi;
	}
	
	private Double getTabarru(String reg_spaj) {
		int lsbs = Integer.valueOf(uwDao.selectBusinessId(reg_spaj));
		int lsdbs = uwDao.selectBusinessNumber(reg_spaj);
		//int lscb = uwDao.selectCaraBayarFromSpaj(reg_spaj);
		
		//I. CARA BARU, QUERY KE TABEL (30 Juli 2012 - Req by Rudy)
		//1. query dulu dengan LSDBS sesuai produknya, apakah ada?
		Map hasil = financeDao.selectLstPersenSyariah(lsbs, lsdbs);
		//2. kalau tidak ada, coba query ulang lagi dengan lsdbs = 0, yg artinya untuk semua lsbs tertentu rate nya sama
		if(hasil == null) hasil = financeDao.selectLstPersenSyariah(lsbs, 0);
		//3. kalau tidak ada, error
		if(hasil == null) {
			return null;
		}else {
			return ((BigDecimal) hasil.get("TABARRU")).doubleValue();
		}
		
		//II. CARA LAMA, HARDCODE
		/*
		Double tabarru = null;
		
		//1. Medivest Syariah, Excellink 80 Plus Syariah, Ekalink 80 Plus Syariah, Mabrur, 
		//3. Excellink 80 Syariah, Ekalink 80 Syariah
		//4. Ekalink Family Syariah
		//5. Amanah Link, Berkah Link
		//10. Excellent Link Syariah ( kode produknya belum ada???)
		//11. Eka Bridge Link Syariah ( kode produknya belum ada???)
		//13. Eka Sejahtera Syariah ( kode produknya belum ada???)
		if(lsbs == 199 || lsbs == 153 || lsbs == 152 || lsbs == 160 || lsbs == 166){ 
			tabarru = 0.8; //Tabarru 80%, Ujroh 20%
			
		//2. Fast Excellink Syariah
		}else if(lsbs == 107){
			if(lsdbs == 1) { //Sekaligus = Tabarru 70%, Ujroh 30%
				tabarru = 0.7; 
			}else {
				tabarru = 0.6; //Reguler = Tabarru 60%, Ujroh 40%
			}
			
		//6. Ikhlas
		}else if(lsbs == 170){
			tabarru = 0.5; //Tabarru 50%, Ujroh 50%
			
		//7. Sakinah
		}else if(lsbs == 171){
			tabarru = 0.3145; //Tabarru 31.45%, Ujroh 68.55%
		
		//8. PAS
		}else if(lsbs == 187){
			if(lsdbs == 1){ //Perdana = Tabarru 45%, Ujroh 55%
				tabarru = 0.45;
			}else if(lsdbs == 2){ //Single = Tabarru 45%, Ujroh 55%
				tabarru = 0.45;
			}else if(lsdbs == 3){ //Ceria = Tabarru 56.5%, Ujroh 43.5%
				tabarru = 0.565;
			}else if(lsdbs == 4){ //Ideal = Tabarru 60.5%, Ujroh 39.5%
				tabarru = 0.605;
			}
		
		//9. PA Syariah
		//16. Eka Sehat Syariah
		}else if(lsbs == 198 || lsbs == 189){
			tabarru = 0.45; //Tabarru 45%, Ujroh 55%
			
		//15. Multi Invest Syariah
		}else if(lsbs == 182){
			tabarru = 0.95; //Tabarru 95%, Ujroh 5%
		//18 .Powersave Syariah 175	
		}else if (lsbs == 175){
		  tabarru = 0.0; //100 % premi di investasikan , Tabarru 0% dan Ujroh 0%
		}
		
		//12. Progressive Save Syariah ( kode produknya belum ada???)
		
		return tabarru;
		*/
	}

	/**
	 * Yusuf (30/04/2008) - mulai 1 may 2008 tarik tanggal jurnal untuk disimpan ke tbank dan ptc_tm
	 * dibuat fungsi ini, karena finance sekarang closing h+1, bukan seperti uw yang closing h+7
	 * 
	 * Yusuf (10/02/2011) - mulai hari ini, fungsi ini diganti menggunakan prosedur GET_TGL_PROD di oracle
	 * di passing parameter tgl RK, dan di hardcode '09' req Himmia/Iwen
	 * 
	 * @return
	 */
	public Date getTanggalJurnal(Date tglRK, String lca_id, Integer jenis) throws RuntimeException{
		Date prodDate = commonDao.selectTanggalProduksiUntukProsesProduksi(tglRK, lca_id, jenis);
		if(prodDate == null){
			throw new RuntimeException("Mohon dicek GET_TGL_PROD return null!");
		}
		return prodDate;
		
		/*Date ldt_prod_date;
		Date ldt_tgl_input = commonDao.selectSysdateTruncated(0);
		
		//Production Date Finance
		Date ldt_close = uwDao.selectMst_default(21);
		
		ldt_prod_date = ldt_tgl_input;
		//apabila tgl_input > tgl_close
		if(FormatDate.dateDifference(ldt_close, ldt_tgl_input, false)>0) {
			//hm 8/2/08, jika tgl rk <= tgl rk yg diperkenankan, maka masuk produksi berjalan
			//Closing Date Finance
			Date ldt_rk_terakhir = uwDao.selectMst_default(20);
			//apabila tgl_input <= tgl_rk_terakhir 
			if(FormatDate.dateDifference(ldt_rk_terakhir, ldt_tgl_input, false)<=0) {
				ldt_prod_date = ldt_close;
			}
		}
		return ldt_prod_date;*/
	}
	
	public Premi jurnal_pre_premi_individu(Double ldec_jumlah, Date ldt_tglrk, Premi premi, Integer li_cr_bayar, BindException errors, User currentUser) throws Exception{
		if(logger.isDebugEnabled())logger.debug("PROSES: jurnal_pre_premi_individu");
	
		String profitCenter = "001";
		
		//Yusuf - 29/02/12 - Req Gesti Akunting - untuk produk syariah, profit center untuk sisi Debet diambil dari lst_rek_ekalife, untuk sisi kredit 801, bukan 850 lagi
		//Yusuf (10/4/2012) req Derry Laksana via helpdesk #24526: Tolong diperbaiki harusnya profit center dmtm bukan 001 tapi 040
		boolean isSyariah = false;
		Map detbisnis = (Map) uwDao.selectDetailBisnis(premi.getNo_spaj()).get(0);
		String lsbs_id = (String) detbisnis.get("BISNIS");
		String lsdbs_number = (String) detbisnis.get("DETBISNIS");
		if(products.syariah(lsbs_id, lsdbs_number)) { //syariah
			profitCenter  = "801";
			isSyariah = true;
		}else if(premi.getNo_spaj().startsWith("40")){
				if(premi.getPremike()==1) {//dmtm, tes, untuk premi pertama (41111,41112) masuk ke 001 profit center
				profitCenter = "001";
				}else{
				profitCenter = "040";
				}
		}
		
		String ls_no_pre=null; String ls_kas = "M"; String ls_bayar; String ls_ket[] = new String[4]; String ls_thn; String ls_banding[] = new String[2]; 
		int li_run = 0; Integer li_pos = new Integer(2); int ll_arr;
		Date ldt_prod; Date ldt_msdef_date; Date ldt_date_input; Date ldt_voucher;
		Double ldec_saldo=null; Double ldec_kosong = new Double(0);
		Double ldec_bayar;
		
		ldt_prod = null;
		
//		ls_no_pre = this.sequence.sequenceNo_pre(32);
		ls_no_pre = this.uwDao.selectGetPacGl("nopre");
		String lca_id = uwDao.selectCabangFromSpaj(premi.getNo_spaj());
		Integer jenis = 1;
		
		/** full autosales project start **/
		String flagAutoSalesCek = bacDao.selectFullAutoSalesFromMstPolicy(premi.getNo_spaj());
		if (flagAutoSalesCek != null && flagAutoSalesCek.equals("4")){
			jenis = 4;
		}
		/** full autosales project end **/
		
		if((lsbs_id.equals("190") && lsdbs_number.equals("009")) || (lsbs_id.equals("200") && lsdbs_number.equals("007"))) jenis = 3;
		//Yusuf (30/04/2008)
		ldt_prod = getTanggalJurnal(ldt_tglrk, lca_id, jenis);

//		if(premi.getPremike()==1) {
//			ldt_prod = hitungTanggalProduksiBaru(ldt_tglrk);
//			if(FormatDate.dateDifference(ldt_prod, ldt_tglrk, false)>0) {
//				ldt_prod = ldt_tglrk;
//			}
//		}else {
//			ldt_prod = this.uwDao.selectProductionDate(premi.getNo_spaj(), premi.getTahunke(), premi.getPremike());
//			if(ldt_prod==null) {
//				ldt_msdef_date = this.uwDao.selectMst_default(1);
//				//ambil tgl rk terakhir
//				ldt_tglrk = this.uwDao.selectMaxRkDate(premi.getNo_spaj(), premi.getTahunke(), premi.getPremike(), "individu");
//				ldt_date_input = this.commonDao.selectSysdateTruncated(0);
//				ldt_prod = ldt_date_input;
//				if(FormatDate.dateDifference(ldt_tglrk, ldt_msdef_date, false)>=0) {
//					if(FormatDate.dateDifference(ldt_msdef_date, ldt_date_input, false)>=0) {
//						ldt_prod = ldt_msdef_date;
//					}
//				}
//			}
//		}
		
		DateFormat df2 = new SimpleDateFormat("yyyy");
		ls_thn = df2.format(ldt_prod);
		premi.setVoucher(premi.getVoucher().trim());
		ldt_voucher = this.uwDao.selectTanggalJurnal(premi.getVoucher(), ls_thn);
		if(ldt_voucher!=null) {
			errors.reject("", "No Voucher " + premi.getVoucher() + " kembar. Mohon diberitahukan ke ITwebandmobile@sinarmasmsiglife.co.id.");
			return null;
		}
		
		ll_arr = premi.getJum_pre().length;
		if(ll_arr==0) {
			premi.setJum_pre(new String[] {ls_no_pre});
		}else {
			premi.setJum_pre(StringUtils.addStringToArray(premi.getJum_pre(), ls_no_pre));
		}

		premi.setDepo_pre(ls_no_pre);
		if(premi.getNo_pre()==null) premi.setNo_pre("");
		else if(premi.getNo_pre().trim().equals("") || premi.getNo_pre().trim().equals(","))premi.setNo_pre(ls_no_pre);
		else premi.setNo_pre(premi.getNo_pre()+", " + ls_no_pre);
		
		ldec_saldo = new Double(ldec_jumlah.doubleValue() + premi.getSaldo_akhir().doubleValue());
	
		this.uwDao.insertMst_tbank(ls_no_pre, li_pos, ldt_prod, ldt_tglrk, premi, ls_kas, ldec_jumlah, currentUser.getLus_id());
	
		if(premi.getKurs_bulanan().doubleValue()>1){
			ldec_bayar = new Double(ldec_jumlah.doubleValue() / premi.getKurs_bulanan().doubleValue());
			NumberFormat f = new DecimalFormat("#,##0.00;(#,##0.00)");
			ls_bayar = "$ " + f.format(ldec_bayar);
		}else{
			ls_bayar = "";
		}
	
		DateFormat df3 = new SimpleDateFormat("yyyyMM");
		
		Map sudor = basDao.selectPolisLamaSurrenderEndorsement(premi.getNo_spaj());
		String polisLama = "";
		if(sudor != null) polisLama = " Rf " + (String) sudor.get("MSPO_POLICY_NO");
		
		if(premi.getPremike()==1) {
			if(li_cr_bayar==17 || li_cr_bayar==99){
				ls_ket[0] = "KOMISI AGEN " + premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ldt_tglrk);
				ls_ket[1] = "TITIPAN PREMI NO. SPAJ " + premi.getNo_spaj();
				ls_ket[2] = "KA " + premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ldt_tglrk);
				ls_ket[3] = "TP " + premi.getNo_spaj().trim() + " " + defaultDateFormatStripes.format(ldt_tglrk);
				premi.setProject(new String[] {"001", "001"});
				premi.setBudget(new String[] {"400203", "400201"});
			}else {
				ls_banding[0] = df3.format(ldt_tglrk);
				ls_banding[1] = df3.format(ldt_prod);
				ls_ket[0] = "BANK " + premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ldt_tglrk) + "  " + ls_bayar + polisLama;
				ls_ket[1] = "TITIPAN PREMI NO. SPAJ " + premi.getNo_spaj().trim() + " " + ls_bayar;
				ls_ket[2] = "BK " + premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ldt_tglrk) + "  " + ls_bayar;
				ls_ket[3] = "TP " + premi.getNo_spaj().trim() + " " + defaultDateFormatStripes.format(ldt_tglrk) + "  " + ls_bayar;
				ls_ket[0] = ls_ket[0] + " BOOKING";
				ls_ket[2] = ls_ket[2] + " BOOKING";
				
				String profitCenter2 = premi.getAccno().substring(0,3);
				
				if(premi.getKurs_bulanan().doubleValue()>1) {
					premi.setProject(new String[]{profitCenter2, profitCenter});
					premi.setBudget(new String[]{premi.getAccno().substring(3), "41112"});
				}else {
					premi.setProject(new String[]{profitCenter2, profitCenter});
					premi.setBudget(new String[]{premi.getAccno().substring(3), "41111"});
				}
			}
		}else {
			ls_ket[0] = "BANK " + premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ldt_tglrk) + "  " + ls_bayar + polisLama;
			ls_ket[1] = "TITIPAN PREMI SUCC NO. POLIS " + premi.getNo_polis().trim() + " " + ls_bayar;
			ls_ket[2] = "BK " + premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ldt_tglrk);
			ls_ket[3] = "TP " + premi.getNo_polis().trim() + " " + defaultDateFormatStripes.format(ldt_tglrk) + "  " + ls_bayar;

			String profitCenter2 = premi.getAccno().substring(0,3);
			
			if(premi.getKurs_bulanan().doubleValue()>1) {
				premi.setProject(new String[]{profitCenter2, profitCenter});
				premi.setBudget(new String[]{premi.getAccno().substring(3), "41122"});
			}else {
				premi.setProject(new String[]{profitCenter2, profitCenter});
				premi.setBudget(new String[]{premi.getAccno().substring(3), "41121"});
			}
		}
	
		li_run++;
		ls_kas = "M";
//		logger.info(premi.getNo_spaj().trim());
//		logger.info(premi.getNo_spaj().trim().replaceAll(".", ""));
//		logger.info(premi.getNo_spaj().replaceAll(".", "").trim());
		this.uwDao.insertMst_dbank(ls_no_pre, li_run, ls_ket[0], ls_kas, ldec_jumlah, null, premi.getNo_spaj().trim());
		this.uwDao.insertMst_bvoucher(ls_no_pre, li_run, ls_ket[2], ldec_jumlah, ldec_kosong, premi.getProject()[0], premi.getBudget()[0], premi.getNo_spaj().trim());
		
		li_run++;
		ls_kas = "B";
		this.uwDao.insertMst_dbank(ls_no_pre, li_run, ls_ket[1], ls_kas, ldec_jumlah, new Integer(1), premi.getNo_spaj().trim());
		this.uwDao.insertMst_bvoucher(ls_no_pre, li_run, ls_ket[3], ldec_kosong, ldec_jumlah, premi.getProject()[1], premi.getBudget()[1], premi.getNo_spaj().trim());
	
		premi.setSaldo_akhir(ldec_saldo);
		
		return premi;
				
	}
	
	public Premi jurnal_pre_individu_jm(Double ldec_jumlah, Date ldt_tglrk, Premi premi, Integer li_cr_bayar, String no_pre, BindException errors, User currentUser) throws Exception{
		if(logger.isDebugEnabled())logger.debug("PROSES: jurnal_pre_individu_jm");
		NumberFormat f = new DecimalFormat("#,##0.00;(#,##0.00)");
		
		String profitCenter = "001";
		
		//Yusuf - 29/02/12 - Req Gesti Akunting - untuk produk syariah, profit center untuk sisi Debet diambil dari lst_rek_ekalife, untuk sisi kredit 801, bukan 850 lagi
		//Yusuf (10/4/2012) req Derry Laksana via helpdesk #24526: Tolong diperbaiki harusnya profit center dmtm bukan 001 tapi 040
		Map detbisnis = (Map) uwDao.selectDetailBisnis(premi.getNo_spaj()).get(0);
		String lsbs_id = (String) detbisnis.get("BISNIS");
		String lsdbs_number = (String) detbisnis.get("DETBISNIS");
		if(products.syariah(lsbs_id, lsdbs_number)) { //syariah
			profitCenter = "801";
		}
		
		String ls_no_pre=null; String ls_kas = "M"; String ls_bayar; String ls_ket[] = new String[4]; String ls_thn; String ls_banding[] = new String[2]; 
		Integer li_run = 0; Integer li_pos = new Integer(2); Integer ll_arr;
		Date ldt_prod; Date ldt_msdef_date; Date ldt_date_input; Date ldt_voucher;
		Double ldec_saldo=null; Double ldec_kosong = new Double(0);
		Double ldec_bayar;
		
		ldt_prod = null;
		
		String lca_id = uwDao.selectCabangFromSpaj(premi.getNo_spaj());
		Integer jenis = 1;
		
		/** full autosales project start **/
		String flagAutoSalesCek = bacDao.selectFullAutoSalesFromMstPolicy(premi.getNo_spaj());
		if (flagAutoSalesCek != null && flagAutoSalesCek.equals("4")){
			jenis = 4;
		}
		/** full autosales project end **/
		
		if((lsbs_id.equals("190") && lsdbs_number.equals("009")) || (lsbs_id.equals("200") && lsdbs_number.equals("007"))) jenis = 3;
		//Yusuf (30/04/2008)
		ldt_prod = getTanggalJurnal(ldt_tglrk, lca_id, jenis);

		DateFormat df2 = new SimpleDateFormat("yyyy");
		ls_thn = df2.format(ldt_prod);
		premi.setVoucher(premi.getVoucher().trim());
		ldt_voucher = this.uwDao.selectTanggalJurnal(premi.getVoucher(), ls_thn);
		if(ldt_voucher!=null) {
			errors.reject("", "No Voucher " + premi.getVoucher() + " kembar. Mohon diberitahukan ke ITwebandmobile@sinarmasmsiglife.co.id.");
			return null;
		}
		
		ll_arr = premi.getJum_pre().length;
		if(ll_arr==0) {
			premi.setJum_pre(new String[] {no_pre});
		}else {
			premi.setJum_pre(StringUtils.addStringToArray(premi.getJum_pre(), no_pre));
		}

		premi.setDepo_pre(no_pre);
		if(premi.getNo_pre()==null) premi.setNo_pre("");
		else if(premi.getNo_pre().trim().equals("") || premi.getNo_pre().trim().equals(","))premi.setNo_pre(no_pre);
		else premi.setNo_pre(premi.getNo_pre()+", " + no_pre);
		
		ldec_saldo = new Double(ldec_jumlah.doubleValue() + premi.getSaldo_akhir().doubleValue());
	
		if(premi.getKurs_bulanan().doubleValue()>1){
			ldec_bayar = new Double(ldec_jumlah.doubleValue()/premi.getKurs_bulanan().doubleValue());
			ls_bayar = " US $ " + f.format(ldec_bayar) + " " + premi.getKurs_bulanan().doubleValue() + " ";
		}else{
			ldec_bayar = ldec_jumlah;
			ls_bayar = " ";
		}

		List<DrekDet> listdrekdet = uwDao.selectMstDrekDet(null, premi.getNo_spaj(), premi.getMspa_payment_id(), null);
		String no_trx = listdrekdet.get(0).getNo_trx();
		
		ArrayList mstbvoucher = Common.serializableList(refundDao.selectMstBvoucher(no_pre, 2));
		HashMap bvoucher = (HashMap) mstbvoucher.get(0);
		String profitCenter2 = (String) bvoucher.get("PROJECT_NO");
		
		DateFormat df3 = new SimpleDateFormat("yyyyMM");
		
		Map sudor = basDao.selectPolisLamaSurrenderEndorsement(premi.getNo_spaj());
		String polisLama = "";
		if(sudor != null) polisLama = " Rf " + (String) sudor.get("MSPO_POLICY_NO");
		
		String kdBudget = "";
		if(premi.getKurs_bulanan().doubleValue()>1) {
			kdBudget = "48215";
		}else {
			kdBudget = "48214";
		}
		
		if(premi.getPremike()==1) {
			if(li_cr_bayar==17 || li_cr_bayar==99){
				ls_ket[0] = "KOMISI AGEN " + premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ldt_tglrk);
				ls_ket[1] = "TITIPAN PREMI NO. SPAJ " + premi.getNo_spaj();
				ls_ket[2] = "KA " + premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ldt_tglrk);
				ls_ket[3] = "TP " + premi.getNo_spaj().trim() + " " + defaultDateFormatStripes.format(ldt_tglrk);
				premi.setProject(new String[] {"001", "001"});
				premi.setBudget(new String[] {"400203", "400201"});
				kdBudget = "400203";
			}else {
				ls_banding[0] = df3.format(ldt_tglrk);
				ls_banding[1] = df3.format(ldt_prod);
				ls_ket[0] = "TITIPAN TIDAK DIKETAHUI " + defaultDateFormatStripes.format(ldt_tglrk) + ls_bayar + no_trx + " " + premi.getVoucher() + " " + polisLama;
				ls_ket[1] = "TITIPAN PREMI NO. SPAJ " + premi.getNo_spaj().trim();
				ls_ket[2] = "BK " + premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ldt_tglrk) + "  " + ls_bayar;
				ls_ket[3] = "TP " + premi.getNo_spaj().trim() + " " + defaultDateFormatStripes.format(ldt_tglrk) + "  " + ls_bayar;

//				String profitCenter2 = premi.getAccno().substring(0,3);
				
				if(premi.getKurs_bulanan().doubleValue()>1) {
					premi.setProject(new String[]{profitCenter2, profitCenter});
					premi.setBudget(new String[]{premi.getAccno().substring(3), "41112"});
				}else {
					premi.setProject(new String[]{profitCenter2, profitCenter});
					premi.setBudget(new String[]{premi.getAccno().substring(3), "41111"});
				}
			}
		}else {
			ls_ket[0] = "TITIPAN TIDAK DIKETAHUI " + defaultDateFormatStripes.format(ldt_tglrk) + ls_bayar + no_trx + " " + premi.getVoucher() + " " + polisLama;
			ls_ket[1] = "TITIPAN PREMI SUCC NO. POLIS " + premi.getNo_spaj().trim();
			ls_ket[2] = "BK " + premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ldt_tglrk);
			ls_ket[3] = "TP " + premi.getNo_polis().trim() + " " + defaultDateFormatStripes.format(ldt_tglrk) + "  " + ls_bayar;

//			String profitCenter2 = premi.getAccno().substring(0,3);
			
			if(premi.getKurs_bulanan().doubleValue()>1) {
				premi.setProject(new String[]{profitCenter2, profitCenter});
				premi.setBudget(new String[]{premi.getAccno().substring(3), "41122"});
			}else {
				premi.setProject(new String[]{profitCenter2, profitCenter});
				premi.setBudget(new String[]{premi.getAccno().substring(3), "41121"});
			}
		}
		
		if(StringUtil.isEmpty(premi.getNo_jm_sa())){
			String s_nojmsa = this.uwDao.selectGetPacGl("nojm");
			uwDao.insertMst_ptc_tm(s_nojmsa, 1, ldt_prod, no_pre, currentUser.getLus_id());
			uwDao.insertMst_ptc_jm(s_nojmsa, 1, ls_ket[0], ldec_jumlah, premi.getProject()[0], kdBudget, "D", premi.getNo_spaj());
			uwDao.insertMst_ptc_jm(s_nojmsa, 2, ls_ket[1], ldec_jumlah, premi.getProject()[1], premi.getBudget()[1], "C", premi.getNo_spaj());
			premi.setNo_jm_sa(s_nojmsa);
		}
		premi.setNo_pre(no_pre);
		premi.setSaldo_akhir(ldec_saldo);
		
		return premi;
				
	}
	
	public Premi jurnal_pre_ulink_jm(Double ldec_jumlah, Date ldt_tglrk, Premi premi, Integer li_cr_bayar, String no_pre, BindException errors, User currentUser) throws Exception {
		if(logger.isDebugEnabled())logger.debug("PROSES: jurnal_pre_ulink_jm");
		NumberFormat f = new DecimalFormat("#,##0.00;(#,##0.00)");
		
		String ls_no_pre; String ls_kas = "M"; String ls_bayar; String ls_ket[] = new String[4]; 
		Integer li_run = 0; Integer li_pos = new Integer(2);
		Date ldt_prod;
		Double ldec_saldo; Double ldec_kosong = new Double(0);
		Double ldec_bayar;
		
		//Yusuf - 11/06/08 - untuk produk unit link syariah, profit center nya beda
		//Yusuf - 30/03/10 - untuk product stable link efektif 1 apr 2010, profit center nya beda
		//Yusuf - 29/02/12 - Req Gesti Akunting - untuk produk unit link syariah, profit center untuk sisi Debet diambil dari lst_rek_ekalife, untuk sisi kredit 801, bukan 850 lagi
		String lsbs = premi.getBisnis_id().toString();
		String lsdbs = premi.getLsdbs_number().toString();
		String profitCenterUlink = "501";
		if(products.unitLinkSyariah(lsbs) || products.muamalat(premi.getNo_spaj())) {
			profitCenterUlink = "801";
		}else if(products.stableLink(lsbs)){
			profitCenterUlink = "701";
		}
		
		String lca_id = uwDao.selectCabangFromSpaj(premi.getNo_spaj());
		Integer jenis = 1;
		
		/** full autosales project start **/
		String flagAutoSalesCek = bacDao.selectFullAutoSalesFromMstPolicy(premi.getNo_spaj());
		if (flagAutoSalesCek != null && flagAutoSalesCek.equals("4")){
			jenis = 4;
		}
		/** full autosales project end **/
		
		if((lsbs.equals("190") && lsdbs.equals("9"))|| (lsbs.equals("200") && lsdbs.equals("7"))) jenis = 3;
		ldt_prod = getTanggalJurnal(ldt_tglrk, lca_id, jenis);
		
		ldec_saldo = new Double(ldec_jumlah.doubleValue() + premi.getSaldo_akhir().doubleValue());
		
		if(premi.getKurs_bulanan().doubleValue()>1){
			ldec_bayar = new Double(ldec_jumlah.doubleValue()/premi.getKurs_bulanan().doubleValue());
			ls_bayar = " US $ " + f.format(ldec_bayar) + " " + premi.getKurs_bulanan().doubleValue() + " ";
		}else{
			ldec_bayar = ldec_jumlah;
			ls_bayar = " ";
		}

		List<DrekDet> listdrekdet = uwDao.selectMstDrekDet(null, premi.getNo_spaj(), premi.getMspa_payment_id(), null);
		String no_trx = listdrekdet.get(0).getNo_trx();
		
		Map sudor = basDao.selectPolisLamaSurrenderEndorsement(premi.getNo_spaj());
		String polisLama = "";
		if(sudor != null) polisLama = " Rf " + (String) sudor.get("MSPO_POLICY_NO");

		if(premi.getPremike()==1) {
			ls_ket[0] = "TITIPAN TIDAK DIKETAHUI " + defaultDateFormatStripes.format(ldt_tglrk) + ls_bayar + no_trx + " " + premi.getVoucher() + " " + polisLama;
			ls_ket[1] = "TITIPAN PREMI NO. SPAJ " + premi.getNo_spaj().trim() + " " + ls_bayar;
			ls_ket[2] = "BK " + premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ldt_tglrk) + "  " + ls_bayar;
			ls_ket[3] = "TP " + premi.getNo_spaj().trim() + " " + defaultDateFormatStripes.format(ldt_tglrk) + "  " + ls_bayar;
		}else {
			ls_ket[0] = "TITIPAN TIDAK DIKETAHUI " + defaultDateFormatStripes.format(ldt_tglrk) + ls_bayar + no_trx + " " + premi.getVoucher() + " " + polisLama;
			ls_ket[1] = "TITIPAN PREMI SUCC NO. POLIS " + premi.getNo_polis().trim() + " " + ls_bayar;
			ls_ket[2] = "BK " + premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ldt_tglrk) + "  " + ls_bayar;
			ls_ket[3] = "TP " + premi.getNo_spaj().trim() + " " + defaultDateFormatStripes.format(ldt_tglrk) + "  " + ls_bayar;
		}

		ArrayList mstbvoucher = Common.serializableList(refundDao.selectMstBvoucher(no_pre, 2));
		HashMap bvoucher = (HashMap) mstbvoucher.get(0);
		String profitCenter2 = (String) bvoucher.get("PROJECT_NO");
//		String profitCenter2 = premi.getAccno().substring(0,3);
		String kdBudget = "";
		
		if(premi.getKurs_bulanan().doubleValue()>1) {
			kdBudget = "48215";
			premi.setProject(new String[]{profitCenter2, profitCenterUlink});
			premi.setBudget(new String[]{premi.getAccno().substring(3), "41112"});
		}else {
			kdBudget = "48214";
			premi.setProject(new String[]{profitCenter2, profitCenterUlink});
			premi.setBudget(new String[]{premi.getAccno().substring(3), "41111"});
		}
		
		if(StringUtil.isEmpty(premi.getNo_jm_sa())){
			String s_nojmsa = this.uwDao.selectGetPacGl("nojm");
			uwDao.insertMst_ptc_tm(s_nojmsa, 1, ldt_prod, no_pre, currentUser.getLus_id());
			uwDao.insertMst_ptc_jm(s_nojmsa, 1, ls_ket[0], ldec_jumlah, premi.getProject()[0], kdBudget, "D", premi.getNo_spaj());
			uwDao.insertMst_ptc_jm(s_nojmsa, 2, ls_ket[1], ldec_jumlah, premi.getProject()[1], premi.getBudget()[1], "C", premi.getNo_spaj());
			premi.setNo_jm_sa(s_nojmsa);
		}
		
		premi.setNo_pre(no_pre);
		premi.setSaldo_akhir(ldec_saldo);
	
		return premi;
	}

	public Premi jurnal_pre_ulink(Double ldec_jumlah, Date ldt_tglrk, Premi premi, Integer li_cr_bayar, BindException errors, User currentUser) throws Exception {
		if(logger.isDebugEnabled())logger.debug("PROSES: jurnal_pre_ulink");
	
		//Yusuf - 11/06/08 - untuk produk unit link syariah, profit center nya beda
		//Yusuf - 30/03/10 - untuk product stable link efektif 1 apr 2010, profit center nya beda
		//Yusuf - 29/02/12 - Req Gesti Akunting - untuk produk unit link syariah, profit center untuk sisi Debet diambil dari lst_rek_ekalife, untuk sisi kredit 801, bukan 850 lagi
		String lsbs = premi.getBisnis_id().toString();
		String lsdbs = premi.getLsdbs_number().toString();
		String profitCenterUlink = "501";
		if(products.unitLinkSyariah(lsbs) || products.muamalat(premi.getNo_spaj())) {
			profitCenterUlink = "801";
		}else if(products.stableLink(lsbs)){
			profitCenterUlink = "701";
		}
		
		String ls_no_pre; String ls_kas = "M"; String ls_bayar; String ls_ket[] = new String[4]; 
		int li_run = 0; Integer li_pos = new Integer(2);
		Date ldt_prod;
		Double ldec_saldo; Double ldec_kosong = new Double(0);
		Double ldec_bayar;
		
		String lca_id = uwDao.selectCabangFromSpaj(premi.getNo_spaj());
		Integer jenis = 1;
		
		/** full autosales project start **/
		String flagAutoSalesCek = bacDao.selectFullAutoSalesFromMstPolicy(premi.getNo_spaj());
		if (flagAutoSalesCek != null && flagAutoSalesCek.equals("4")){
			jenis = 4;
		}
		/** full autosales project end **/
		
		if((lsbs.equals("190") && lsdbs.equals("9"))|| (lsbs.equals("200") && lsdbs.equals("7"))) jenis = 3;
		//Yusuf (30/04/2008)
		ldt_prod = getTanggalJurnal(ldt_tglrk, lca_id, jenis);
		//ldt_prod = hitungTanggalProduksiBaru(ldt_tglrk);
		
//		ls_no_pre = this.sequence.sequenceNo_pre(32);
		ls_no_pre = this.uwDao.selectGetPacGl("nopre");
		ldec_saldo = new Double(ldec_jumlah.doubleValue() + premi.getSaldo_akhir().doubleValue());
		
		this.uwDao.insertMst_tbank(ls_no_pre, li_pos, ldt_prod, ldt_tglrk, premi, ls_kas, ldec_jumlah, currentUser.getLus_id());
		
		if(premi.getKurs_bulanan().doubleValue()>1){
			ldec_bayar = new Double(ldec_jumlah.doubleValue() / premi.getKurs_bulanan().doubleValue());
			NumberFormat f = new DecimalFormat("#,##0.00;(#,##0.00)");
			ls_bayar = "$ " + f.format(ldec_bayar);
		}else{
			ls_bayar = "";
		}

		Map sudor = basDao.selectPolisLamaSurrenderEndorsement(premi.getNo_spaj());
		String polisLama = "";
		if(sudor != null) polisLama = " Rf " + (String) sudor.get("MSPO_POLICY_NO");

		if(premi.getPremike()==1) {
			ls_ket[0] = "BANK " + premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ldt_tglrk) + "  " + ls_bayar + polisLama;
			ls_ket[1] = "TITIPAN PREMI NO. SPAJ " + premi.getNo_spaj().trim() + " " + ls_bayar;
			ls_ket[2] = "BK " + premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ldt_tglrk) + "  " + ls_bayar;
			ls_ket[3] = "TP " + premi.getNo_spaj().trim() + " " + defaultDateFormatStripes.format(ldt_tglrk) + "  " + ls_bayar;
			ls_ket[0] = ls_ket[0] + " BOOKING";
			ls_ket[2] = ls_ket[2] + " BOOKING";
		}else {
			ls_ket[0] = "BANK " + premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ldt_tglrk) + "  " + ls_bayar + polisLama;
			ls_ket[1] = "TITIPAN PREMI SUCC NO. POLIS " + premi.getNo_polis().trim() + " " + ls_bayar;
			ls_ket[2] = "BK " + premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ldt_tglrk) + "  " + ls_bayar;
			ls_ket[3] = "TP " + premi.getNo_spaj().trim() + " " + defaultDateFormatStripes.format(ldt_tglrk) + "  " + ls_bayar;
		}

		//Yusuf (09/12/2011) - Untuk sisi Bank (Debet), project_no nya jangan diutak atik! Ikutin dari tabel lst_rek_ekalife saja
		String profitCenter2 = premi.getAccno().substring(0,3);
		//Map detbisnis = (Map) uwDao.selectDetailBisnis(premi.getNo_spaj()).get(0);
		//String lsbs_id = (String) detbisnis.get("BISNIS");
		//String lsdbs_number = (String) detbisnis.get("DETBISNIS");
		//if(products.syariah(lsbs_id, lsdbs_number)) {
		//	profitCenter2 = "801";
		//}
		
		if(premi.getKurs_bulanan().doubleValue()>1) {
			premi.setProject(new String[]{profitCenter2, profitCenterUlink});
			premi.setBudget(new String[]{premi.getAccno().substring(3), "41112"});
		}else {
			premi.setProject(new String[]{profitCenter2, profitCenterUlink});
			premi.setBudget(new String[]{premi.getAccno().substring(3), "41111"});
		}
		
		li_run++;
		ls_kas = "M";
//		logger.info(premi.getNo_spaj().trim());
//		logger.info(premi.getNo_spaj().trim().replaceAll(".", ""));
//		logger.info(premi.getNo_spaj().replaceAll(".", "").trim());
		this.uwDao.insertMst_dbank(ls_no_pre, li_run, ls_ket[0], ls_kas, ldec_jumlah, null,  premi.getNo_spaj().trim());
		this.uwDao.insertMst_bvoucher(ls_no_pre, li_run, ls_ket[2], ldec_jumlah, ldec_kosong, premi.getProject()[0], premi.getBudget()[0], premi.getNo_spaj().trim());
		
		li_run++;
		ls_kas = "B";
		this.uwDao.insertMst_dbank(ls_no_pre, li_run, ls_ket[1], ls_kas, ldec_jumlah, new Integer(1),  premi.getNo_spaj().trim());
		this.uwDao.insertMst_bvoucher(ls_no_pre, li_run, ls_ket[3], ldec_kosong, ldec_jumlah, premi.getProject()[1], premi.getBudget()[1], premi.getNo_spaj().trim());
		
		premi.setNo_pre(ls_no_pre);
		premi.setSaldo_akhir(ldec_saldo);
		
		return premi;
	}

	public Premi jurnalMemorialPremiIndividu(
			Premi premi, double[] ar_ldec_bayar, String ar_ls_no_voucher, Date ar_max_rk, Double ar_topup, BindException errors, User currentUser, 
			List ar_ttp, List ar_ttp_kurs, List ar_thp, List ar_thp_kurs, List ar_thp_polis, List ar_ttp_voucher) throws Exception{
		if(logger.isDebugEnabled())logger.debug("PROSES: jurnalMemorialPremiIndividu");
		Double ldec_thp;
		Date ldec_beg_date_bill = null;
		String[] ls_accno = new String[2]; String ls_flag; 
		String[] ls_ket = new String[20]; 
		String ls_region; 
		int li_run=0; int z; Integer li_pos = new Integer(1); Integer li_cbid;
		String ll_no_jm; Long li_bisnis; String ls_grp; 
		Date ldt_prod = null; 
		Double ldec_tot_premi; Double ldec_tot_bayar=new Double(0); Double ldec_selisih; Double ldec_total;
		String ls_polis;
		NumberFormat f = new DecimalFormat("#,##0.00;(#,##0.00)");
		HashMap mapRegion=Common.serializableMap(uwDao.selectRegion(premi.getNo_spaj()));
		ls_region = premi.getK_region();
		ldt_prod = null;
		if(premi.getNo_polis().length()==17) ls_polis = premi.getNo_polis().trim();
		else ls_polis = ls_region + "." + premi.getNo_polis().trim();
		
		if(ls_region.equals("29"))ls_region="21";
		
		Map detbisnis = (Map) uwDao.selectDetailBisnis(premi.getNo_spaj()).get(0);
		String lsbs_id = (String) detbisnis.get("BISNIS");
		String lsdbs_number = (String) detbisnis.get("DETBISNIS");
		
		//Yusuf - 11/03/09 - untuk produk muamalat, profit center nya beda
		boolean isSyariah = false;
		if(products.syariah(lsbs_id, lsdbs_number)) {
			isSyariah = true;
		}
		
		Map tmp = this.uwDao.selectProductionDateAndKurs(premi.getNo_spaj(), premi.getTahunke(), premi.getPremike());
		if(tmp==null) {
			errors.reject("payment.noProductionDate");
			return null;
		}
		String lca_id = (String) mapRegion.get("LCA_ID");
		Integer jenis = 1;
		
		/** full autosales project start **/
		String flagAutoSalesCek = bacDao.selectFullAutoSalesFromMstPolicy(premi.getNo_spaj());
		if (flagAutoSalesCek != null && flagAutoSalesCek.equals("4")){
			jenis = 4;
		}
		/** full autosales project end **/
		
		if((lsbs_id.equals("190") && lsdbs_number.equals("009")) || (lsbs_id.equals("200") && lsdbs_number.equals("007"))) jenis = 3;
//		String lwk_id = (String)mapRegion.get("LWK_ID");
		//Yusuf (30/04/2008)
		ldt_prod = getTanggalJurnal(ar_max_rk, lca_id, jenis);
		//ldt_prod = (Date) tmp.get("MSPRO_PROD_DATE");
		
//		ArrayList listComForJm=new ArrayList();
//		ArrayList listComBonusForJm=new ArrayList();
//		ArrayList listComRewardForJm=new ArrayList();
//		if(lca_id.equals("40") && lwk_id.equals("02")){
//			listComForJm=uwDao.selectKomisiForPtcjm(premi.getNo_spaj(),"1");//1 untuk komisi disetahunkan(Arco dkk)
//		}else if(lca_id.equals("09")){
//			listComForJm=uwDao.selectKomisiForPtcjm(premi.getNo_spaj(),"2");//2 bancass
//		}else if(lca_id.equals("60")){
//			listComForJm=uwDao.selectKomisiForPtcjm(premi.getNo_spaj(),"3");//3 bridge dan semua produk yg komisinya di insert mst_diskon_perusahaan (kecuali Arco dkk)
//		}else{
//			listComForJm=uwDao.selectKomisiForPtcjm(premi.getNo_spaj(),"4");//4 komisi yg ada di insert ke mst_commission
//		}
//		
//		listComBonusForJm  =  uwDao.selectCommBonusForPtcjm(premi.getNo_spaj(), "0");
//		listComRewardForJm =  uwDao.selectCommRewardForPtcjm(premi.getNo_spaj(), "0");
//		
		premi.setKurs_harian(new Double(((Integer) tmp.get("MSPRO_NILAI_KURS")).doubleValue()));
	
		if(ldt_prod==null) {
			errors.reject("payment.noProductionDate");
			return null;
		}
		
		if(premi.getKurs_harian()==null) premi.setKurs_harian(new Double (1));
		if(premi.getKurs_bulanan()==null) premi.setKurs_bulanan(new Double(1));
		
		//ldec_tot_premi = new Double(((premi.getPremi_dasar().doubleValue()+premi.getPremi_pa().doubleValue()-premi.getPremi_disc().doubleValue())*premi.getKurs_harian().doubleValue())+premi.getPremi_hcr().doubleValue());
		ldec_tot_premi = new Double(((premi.getPremi_dasar().doubleValue()+premi.getPremi_pa().doubleValue()-premi.getPremi_disc().doubleValue())*premi.getKurs_bulanan().doubleValue())+premi.getPremi_hcr().doubleValue());
		
		li_bisnis = premi.getBisnis_id();
		
		ls_grp = this.uwDao.selectBusinessGroup(li_bisnis);
		if(ls_grp==null) {
			errors.reject("payment.businessGroupFailed");
			return null;
		}else {
			ls_grp = ls_grp.trim();
		}
		
		//Yusuf - 3 Jan 2012 - Req Rudy H, untuk case powersave, LST_GROUP_BISNIS.LSBS_GROUP_ACC bukan termasuk ENDOWNMENT COMBINED/DWIGUNA (2), melainkan 4 (powersave)
		if(products.powerSave(String.valueOf(li_bisnis))) {
			ls_grp = "4";
		}
		//Ryan, Stablesave diubah ke 4, pake validasi diatas terlewat, untuk mastiin aja.
		if(products.stableSave2(String.valueOf(li_bisnis))) {
			ls_grp = "4";
		}
	
		if(premi.getOld_polis()==null) premi.setOld_polis(premi.getNo_polis());
		else if(premi.getOld_polis().equals("")) premi.setOld_polis(premi.getNo_polis());
		
		Map m = this.uwDao.selectBiayaMaterai2(premi.getNo_spaj(), premi.getTahunke(), premi.getPremike());

		if(premi.getPremike()==1) 
			premi.setMaterai(new Double(0));
		else {
			ldec_beg_date_bill = (Date) m.get("MSBI_BEG_DATE");
			premi.setMaterai((Double)m.get("MSBI_STAMP"));
			//premi.setMaterai(new Double(premi.getMaterai().doubleValue()*premi.getKurs_harian().doubleValue()));
			premi.setMaterai(new Double(premi.getMaterai().doubleValue()*premi.getKurs_bulanan().doubleValue()));
		}
	
		for(z=0; z<ar_ldec_bayar.length; z++) {
			ldec_tot_bayar = new Double(ldec_tot_bayar.doubleValue() + ar_ldec_bayar[z]);
		}
		
		//premi.setBiaya_polis(new Double(premi.getBiaya_polis().doubleValue() * premi.getKurs_harian().doubleValue()));
		//premi.setBunga_tunggak(new Double(premi.getBunga_tunggak().doubleValue() * premi.getKurs_harian().doubleValue()));
		premi.setBiaya_polis(new Double(premi.getBiaya_polis().doubleValue() * premi.getKurs_bulanan().doubleValue()));
		premi.setBunga_tunggak(new Double(premi.getBunga_tunggak().doubleValue() * premi.getKurs_bulanan().doubleValue()));
		Double biaya_dplk = new Double("0");
		if(lsbs_id.equals("149")) biaya_dplk = new Double("10000");
		ldec_selisih = new Double((ldec_tot_bayar.doubleValue()-(ar_ldec_bayar[10]*2))-(ldec_tot_premi.doubleValue()+premi.getMaterai().doubleValue()+premi.getBiaya_polis().doubleValue()+premi.getBunga_tunggak().doubleValue()+biaya_dplk.doubleValue()));
		
		if(ldec_selisih.doubleValue()>0) ls_flag="C";
		else {
			ldec_selisih = new Double(ldec_selisih.doubleValue() * -1);
			ls_flag="D";
		}
		//ll_no_jm = this.sequence.sequenceMst_ptc_tm(34);
		ll_no_jm = this.uwDao.selectGetPacGl("nojm");
		if(ll_no_jm==null){
			errors.reject("Sequence PTCTM gagal dilakukan.");
			return null;
		}
		//Deddy (11/05/2015) - Req Gesti Akunting(helpdesk 70088) - ditambahkan keterangan no_spaj pada saat proses jurnal titipan premi pertama (Cth Format : TP_No Spaj_No Polis_No Voucher_Tgl RK).
		//INSERT TMEMO
		String pre = premi.getLs_pre()!=null?!premi.getLs_pre().trim().equals("")?premi.getLs_pre():premi.getNo_pre():premi.getNo_pre();
		this.uwDao.insertMst_ptc_tm(ll_no_jm, li_pos, ldt_prod, pre, currentUser.getLus_id());
	
		//INSERT JMEMO1 - Titipan Premi / Payment
		if(ar_ldec_bayar[0]!=0) {
			for(int li_ttp=0; li_ttp<ar_ttp.size(); li_ttp++) {
				li_run++;
				ls_ket[0] = "TP " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ttp_voucher.get(li_ttp) + " " + defaultDateFormatStripes.format(ar_max_rk);
				if(premi.getPremike()==1) {
					if(((Double)ar_ttp_kurs.get(li_ttp)).doubleValue()>1) {
						ls_accno[0] = (isSyariah ? "801" : "001");
						ls_accno[1] = "41112";
					}else {
						ls_accno[0] = (isSyariah ? "801" : "001");
						ls_accno[1] = "41111";
					}
				}else {
					if(((Double)ar_ttp_kurs.get(li_ttp)).doubleValue()>1) {
						ls_accno[0] = (isSyariah ? "801" : "001");
						ls_accno[1] = "41122";
					}else {
						ls_accno[0] = (isSyariah ? "801" : "001");
						ls_accno[1] = "41121";
					}
				}
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[0], (Double) ar_ttp.get(li_ttp), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
			}
		}
		
		//INSERT JMEMO AR
		if(premi.getPremi_ar().doubleValue()!=0) {
			li_run++;
			ls_ket[0] = "AR " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher + " " + defaultDateFormatStripes.format(ar_max_rk);
			ls_accno[0] = (isSyariah ? "801" : "001");
			ls_accno[1] = "021002";
			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[0], premi.getPremi_ar(), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
		}
		//INSERT JMEMO2 - Nilai Tunai
		if(ar_ldec_bayar[1]!=0) {
			li_run++;
			ls_ket[1]   = "NT " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getOld_polis() + " " + premi.getNama_pemegang() + " " + ar_ls_no_voucher + " " + premi.getOld_polis();
			if(premi.getKurs_bulanan().doubleValue()>1) {
				ls_accno[0] = (isSyariah ? "801" : "001");
				ls_accno[1] = "41152";
			}else {
				ls_accno[0] = (isSyariah ? "801" : "001");
				ls_accno[1] = "41151";
			}
			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[1], new Double(ar_ldec_bayar[1]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
		}
		//INSERT JMEMO3 - Tahapan
		if(ar_ldec_bayar[2]!=0) {
			int li_jn;
			for(int li_thp = 0; li_thp< ar_thp.size(); li_thp++) {
				premi.setOld_polis(premi.getOld_polis().trim());
				li_run++;
				//ldec_thp = this.uwDao.selectJumlahBayarTahapan(premi.getNo_spaj(), ldec_beg_date_bill);
				//Yusuf (12 Nov 2009), tarik data gunakan polis lamanya, jgn gunakan no barunya
				ldec_thp = this.uwDao.selectJumlahBayarTahapanUsingOldPolicy(premi.getOld_polis().replace(".", "").split(","));
				if(ldec_thp == null){
					ldec_thp = 0.0;
				}
				if(ldec_tot_premi.doubleValue()<=(ldec_thp.doubleValue() * ((Double) ar_thp_kurs.get(li_thp)).doubleValue())) {
					if(((Double) ar_thp_kurs.get(li_thp)).doubleValue()>1) {
						ls_accno[0] = (isSyariah ? "801" : "001");
						ls_accno[1] = "41152";
						ls_ket[2] = "TH " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + ar_thp_polis.get(li_thp) 
						//dikosongin jumlah tahapan di EKA.MST_PTC_JM untuk new business (YUSUF - 14/06/2006)
						//+ " $" + f.format(premi.getTahapan().doubleValue())  
						+ " " + premi.getNama_pemegang() + " " + ar_ls_no_voucher;
					}else {
						ls_accno[0] = (isSyariah ? "801" : "001");
						ls_accno[1] = "41151";
						ls_ket[2] = "TH " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + ar_thp_polis.get(li_thp) + premi.getNama_pemegang() + " " + ar_ls_no_voucher;
					}
				}else {
					if(((Double) ar_thp_kurs.get(li_thp)).doubleValue()>1) {
						ls_accno[0] = (isSyariah ? "801" : "001");
						ls_accno[1] = "337142";
						ls_ket[2] = "TH " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + ar_thp_polis.get(li_thp) 
						//dikosongin jumlah tahapan di EKA.MST_PTC_JM untuk new business (YUSUF - 14/06/2006)
						//+ " $" + f.format(premi.getTahapan().doubleValue())
						+ " " + premi.getNama_pemegang() + " " + ar_ls_no_voucher;
					}else {
						ls_accno[0] = (isSyariah ? "801" : "001");
						ls_accno[1] = "337141";
						ls_ket[2] = "TH " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + ar_thp_polis.get(li_thp) + premi.getNama_pemegang() + " " + ar_ls_no_voucher;
					}
				}
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[2], (Double) ar_thp.get(li_thp), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());				
			}
		}
		//INSERT JMEMO4
		if(ar_ldec_bayar[3]!=0) {
			li_run++;
			ls_ket[3]   = "DP " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getOld_polis() + " " + premi.getNama_pemegang()  + " " + ar_ls_no_voucher + " " + premi.getOld_polis();
			if(premi.getKurs_bulanan().doubleValue()>1) {
				ls_accno[0] = (isSyariah ? "801" : "001");
				ls_accno[1] = "337122";
			}else {
				ls_accno[0] = (isSyariah ? "801" : "001");
				ls_accno[1] = "337121";
			}
			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[3], new Double(ar_ldec_bayar[3]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
		}
		//INSERT JMEMO5
		if(ar_ldec_bayar[4]!=0) {
			li_run++;
			ls_ket[4]   = "PJ " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getOld_polis().trim() + " " + ar_ls_no_voucher  + " " + premi.getOld_polis().trim();
			if(premi.getKurs_bulanan().doubleValue()>1) {
				ls_accno[0] = (isSyariah ? "801" : "001");
				ls_accno[1] = "118122";
			}else {
				ls_accno[0] = (isSyariah ? "801" : "001");
				ls_accno[1] = "118121";
			}
			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[4], new Double(ar_ldec_bayar[4]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
		}
		//INSERT JMEMO7
		if(ar_ldec_bayar[6]!=0) {
			li_run++;
			ls_ket[6]   = "BB " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
			if(premi.getKurs_bulanan().doubleValue()>1) {
				ls_accno[0] = (isSyariah ? "801" : "001");
				ls_accno[1] = "41162";
			}else {
				ls_accno[0] = (isSyariah ? "801" : "001");
				ls_accno[1] = "41161";
			}
			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[6], new Double(ar_ldec_bayar[6]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
		}
		//INSERT JMEMO8
		if(ar_ldec_bayar[7]!=0) {
			li_run++;
			ls_ket[7]   = "KA " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
			if(premi.getKurs_bulanan().doubleValue()>1) {
				ls_accno[0] = (isSyariah ? "801" : "001");
				ls_accno[1] = "41112";
			}else {
				ls_accno[0] = (isSyariah ? "801" : "001");
				ls_accno[1] = "41111";
			}
			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[7], new Double(ar_ldec_bayar[7]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
		}
		//INSERT JMEMO10
		if(ar_ldec_bayar[9]!=0) {
			li_run++;
			if(premi.getKurs_bulanan().doubleValue()>1) {
				ls_accno[0] = (isSyariah ? "801" : "001");
				ls_accno[1] = "41152";
			}else {
				ls_accno[0] = (isSyariah ? "801" : "001");
				ls_accno[1] = "41151";
			}
			ls_ket[9]  = "AK " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getOld_polis().trim() + " " + ar_ls_no_voucher; 
			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[9], new Double(ar_ldec_bayar[9]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
		}
		//INSERT JMEMO12
		if(ar_ldec_bayar[10]!=0) {
			li_run++;
			ls_ket[10]   = "PG " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
			if(premi.getKurs_bulanan().doubleValue()>1) {
				ls_accno[0] = (isSyariah ? "801" : "001");
				ls_accno[1] = "41172";
			}else {
				ls_accno[0] = (isSyariah ? "801" : "001");
				ls_accno[1] = "41171";
			}
			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[11], new Double(ar_ldec_bayar[11]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
		}
		//BUDGET PROMOSI
		if(ar_ldec_bayar[11]!=0) {
			li_run++;
			ls_ket[11]   = "CAMPAIGN FREE POLIS " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim();
			ls_accno[0] = "040";
			ls_accno[1] = "811999";
			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[11], new Double(ar_ldec_bayar[11]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
		}
		
		if(premi.getPremi_dasar().doubleValue()!=0) {
			//INSERT JMEMO11 - Premi Pokok / Premi Lanjutan
			if(premi.getPremike()==1) {
				//
				ls_accno[0] = (isSyariah && !(lsbs_id.equals("175") && lsdbs_number.equals("002"))? "801" :(lsbs_id.equals("175") && lsdbs_number.equals("002"))?"851": ("0" + ls_region));
				int lscb = uwDao.selectCaraBayarFromSpaj(premi.getNo_spaj());
				int jenis_acc_premi =3;
				if(lscb==0) jenis_acc_premi = 1;
				ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), jenis_acc_premi);//"61111" + ls_grp.trim();
				ls_ket[10] = "PP " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
			}else {
				if(premi.getTahunke()==1) {
					ls_accno[0] = (isSyariah ? "801" : ("0" + ls_region));
					ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 4);//"61112" + ls_grp.trim();
				}else {
					ls_accno[0] = (isSyariah ? "801" : ("0" + ls_region));
					ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 5);//"6121" + ls_grp.trim();
				}
				ls_ket[10] = "PL " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
			}
			
			//HORISON, PP-NYA DIBAGI BERDASARKAN PERSENTASE DPLK
			if(lsbs_id.equals("149") && premi.getPremike()==1) { 
				Map persentaseDPLK = uwDao.selectPersentaseDplk(premi.getNo_spaj());
				Double premi_pdpt = (Double) persentaseDPLK.get("premi");
				Double premi_dplk = (Double) persentaseDPLK.get("biaya_dplk");
				if(premi_pdpt==null || premi_dplk==null) {
					errors.reject("payment.persentaseDPLK");
					return null;
				}
				//1. INSERT JMEMO 11.a. PENDAPATAN PREMI = TOTAL PREMI * ((100 - PERSENTASE DPLK) / 100)
				li_run++;
				//ldec_total = new Double(premi_pdpt * premi.getKurs_harian());
				ldec_total = new Double(premi_pdpt * premi.getKurs_bulanan());
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[10], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
				//2. INSERT JMEMO 11.b. UTANG KEPADA DPLK = (TOTAL PREMI * PERSENTASE DPLK / 100) + Rp. 10 ribu biaya kepesertaan
				ls_accno[0] = (isSyariah ? "801" : ("0" + ls_region));
				ls_accno[1] = "484";
				ls_ket[10] = "BD " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
				li_run++;
				//ldec_total = new Double((premi_dplk * premi.getKurs_harian()));
				ldec_total = new Double((premi_dplk * premi.getKurs_bulanan()));
				//logger.info(ldec_total);
				ldec_total += 10000;
				//logger.info(ldec_total);
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[10], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
		
			//Yusuf (15/05/12) - Req Akunting (Jumoro, Charles, Derry) via Rudy via email - PP syariah dibagi TABARRU vs UJROH
			}else if(isSyariah){
				Double d_total = new Double(0);
				Double d_total_bayar = new Double(0);
				Double d_selisih = new Double(0);
				for(int index=1;index<=4;index++){//Req Rudy By Email.
//					looping index disini disamakan dengan flag. Flag : 1 = tabaru, 2 = ujrah, 3 = wakalah, 4 = mudarobah
//					801 : ujroh
//					802 : tabaru
//					850 : wakalah
//					851 : mudarobah
					if(index==1){
						ls_accno[0] ="802";
					}else if(index==2){
						ls_accno[0] ="801";
					}else if(index==3){
						ls_accno[0] ="850";
					}else if(index==4){
						ls_accno[0] ="851";
					}
					ldec_total = getPremiSyariah(premi.getNo_spaj(), 1, premi.getPremike(), index);
					ldec_total = FormatNumber.round2(ldec_total, 2);
					d_total = d_total.doubleValue() + ldec_total.doubleValue();
					if(ldec_total > 0){
						li_run++;
						this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[10], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
					}
				}
				
				for(int i=0; i<ar_ttp.size(); i++) {
					d_total_bayar = d_total_bayar.doubleValue() + (Double) ar_ttp.get(i);
				}
				
				if(ls_flag.equals("D")){
					d_selisih = d_total.doubleValue() - d_total_bayar.doubleValue();
				}else{
					d_selisih = d_total_bayar.doubleValue() - d_total.doubleValue();
				}
				ldec_selisih = FormatNumber.round2(d_selisih, 2);
				
//				//tabarru + ujroh = 100%
//				Double persenTabarru = getTabarru(premi.getNo_spaj());
//				if(persenTabarru == null) {
//					errors.reject("payment.persenTabarru");
//					return null;
//				}
//				
//				//1. INSERT TABARRU (802)//rubah
//				li_run++;
//				ls_accno[0] = "802";
//					if((lsbs_id.equals("175") && (lsdbs_number.equals("001")||lsdbs_number.equals("002"))))//ryan, test 175, jurnal baru
//						{
//						ls_accno[0] = "851";
//						}
//				//ldec_total = new Double( persenTabarru.doubleValue() * premi.getPremi_dasar().doubleValue() * premi.getKurs_harian().doubleValue());
//				ldec_total = new Double( persenTabarru.doubleValue() * premi.getPremi_dasar().doubleValue() * premi.getKurs_bulanan().doubleValue());
//				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[10], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
//
//				//2. INSERT UJROH (801)
//				li_run++;
//				ls_accno[0] = "801";
//					if((lsbs_id.equals("175") && (lsdbs_number.equals("001")||lsdbs_number.equals("002"))))
//					{
//						ls_accno[0] = "851";
//					}
//				//ldec_total = new Double( (1 - persenTabarru.doubleValue()) * premi.getPremi_dasar().doubleValue() * premi.getKurs_harian().doubleValue());
//				ldec_total = new Double( (1 - persenTabarru.doubleValue()) * premi.getPremi_dasar().doubleValue() * premi.getKurs_bulanan().doubleValue());
//				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[10], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
//				
			}else {
				li_run++;
				//ldec_total = new Double(premi.getPremi_dasar().doubleValue() * premi.getKurs_harian().doubleValue());
				ldec_total = new Double(premi.getPremi_dasar().doubleValue() * premi.getKurs_bulanan().doubleValue());
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[10], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
			}
			
			//INSERT JMEMO12
			if(premi.getPremi_hcr().doubleValue()!=0) {
				li_run++;
				if(premi.getPremike()==1) {
					ls_accno[0] = (isSyariah ? "801" : ("0" + ls_region));
					ls_accno[1] = "600306";
				}else {
					if(premi.getTahunke()==1) {
						ls_accno[0] = (isSyariah ? "801" : ("0" + ls_region));
						ls_accno[1] = "611125";
					}else {
						ls_accno[0] = (isSyariah ? "801" : ("0" + ls_region));
						ls_accno[1] = "61215";						
					}
				}
				ls_ket[11] = "HC " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
				ldec_total = premi.getPremi_hcr();
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[11], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
			}
			
			//INSERT JMEMO13
			if(premi.getPremi_pa().doubleValue()!=0) {
				li_run++;
				if(premi.getPremike()==1) {
					ls_accno[0] = (isSyariah ? "801" : ("0" + ls_region));
					ls_accno[1] = "611116";
				}else {
					if(premi.getTahunke()==1) {
						ls_accno[0] = (isSyariah ? "801" : ("0" + ls_region));
						ls_accno[1] = "611126";
					}else {
						ls_accno[0] = (isSyariah ? "801" : ("0" + ls_region));
						ls_accno[1] = "61216";						
					}
				}
				ls_ket[12] = "PA " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis() + " " + ar_ls_no_voucher;
				//ldec_total = new Double(premi.getPremi_pa().doubleValue() * premi.getKurs_harian().doubleValue());
				ldec_total = new Double(premi.getPremi_pa().doubleValue() * premi.getKurs_bulanan().doubleValue());
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[12], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
			}
			
			//INSERT JMEMO14
			if(premi.getBunga_tunggak().doubleValue()!=0) {
				li_run++;
				ls_accno[0] = (isSyariah ? "801" : ("0" + ls_region));
				ls_accno[1] = "694";
				ls_ket[17] = "BT " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
				ldec_total = premi.getBunga_tunggak();
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[17], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
			}
	
			//INSERT JMEMO14
			if(premi.getPremi_disc()!=0) {
				li_run++;
				if(premi.getPremike()==1) {
					ls_accno[0] = (isSyariah ? "801" : ("0" + ls_region));
					ls_accno[1] = "62111";
				}else {
					if(premi.getTahunke()==1) {
						ls_accno[0] = (isSyariah ? "801" : ("0" + ls_region));
						ls_accno[1] = "62112";
					}else {
						ls_accno[0] = (isSyariah ? "801" : ("0" + ls_region));
						ls_accno[1] = "6221";						
					}
				}
				ls_ket[13] = "DC " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
				//ldec_total = new Double(premi.getPremi_disc().doubleValue() * premi.getKurs_harian().doubleValue());
				ldec_total = new Double(premi.getPremi_disc().doubleValue() * premi.getKurs_bulanan().doubleValue());
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[13], ldec_total, ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
			}
			
			//INSERT JMEMO15 - Biaya Polis
			if(premi.getBiaya_polis().doubleValue()!=0) {
				li_run++;
				ls_ket[14] = "BP " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
				ls_accno[0] = (isSyariah ? "801" : ("0" + ls_region));
				ls_accno[1] = "693";
				ldec_total = premi.getBiaya_polis();
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[14], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
			}
			
			//INSERT JMEMO16
			if(premi.getMaterai().doubleValue()!=0) {
				li_run++;
				ls_accno[0] = (isSyariah ? "801" : "001");
				ls_accno[1] = "835316";
				ls_ket[15] = "MT " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis() + " " + ar_ls_no_voucher;
				ldec_total = premi.getMaterai();
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[15], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
			}
			
			//INSERT JMEMO17 - Selisih Kurs
			if(ldec_selisih.doubleValue() !=0) {
				
				//UNTUK HORISON, MASUK KE SELISIH PEMBAYARAN
				if(lsbs_id.equals("149") && premi.getPremike()==1) { 
					li_run++;
					ls_accno[0] = (isSyariah ? "801" : ("0" + premi.getK_region()));
					ls_accno[1] = "8927";
					ls_ket[16] = "SP " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
					ldec_total = ldec_selisih;
					this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[16], ldec_total, ls_accno[0], ls_accno[1], ls_flag, premi.getNo_spaj());

				//Yusuf - 26/04/2012 - Email dari Derry Laksana Accounting (13 Apr 2012) UNTUK PAS BPD, 187-6, 
				//Rp. 49.000,- MASUK KE JURNAL DISCOUNT (621116 - Pot Premi Ind PP Kecelakaan)
				} else if(lsbs_id.equals("187") && Integer.valueOf(lsdbs_number) == 6 && premi.getPremike()==1) {
					
					if(ldec_selisih.doubleValue() != 49000){
						errors.reject("payment.errorJurnalDiscountPasBPD");
						return null;
					}
					
					li_run++;
					ls_accno[0] = (isSyariah ? "801" : ("0" + premi.getK_region()));
					ls_accno[1] = "621116";
					ls_ket[16] = "Pot Premi Ind PP Kecelakaan " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
					ldec_total = ldec_selisih;
					this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[16], ldec_total, ls_accno[0], ls_accno[1], ls_flag, premi.getNo_spaj());

				//UNTUK PRODUK LAIN, MASUK KE SELISIH KURS
				}else {
					
					//Yusuf (11/7/08) Bila selisih dalam rupiah masuk Selisih Pembayaran, bila dollar masuk Selisih Kurs
					String akun = "8923"; //selisih kurs
					String ket = "SK ";
					if(premi.getLku_id().equals("01")) {
						akun = "8927";
						ket = "SP ";
					}
					
					li_run++;
					if(ls_flag.equals("C")) {
						ls_accno[0] = (isSyariah ? "801" : ("0" + premi.getK_region()));
						ls_accno[1] = "8914";
						
						if(lsbs_id.equals("208") && (Integer.valueOf(lsdbs_number) >= 45 && Integer.valueOf(lsdbs_number) <= 48) ){
							ls_accno[1] = "8927";
						};
						
						/* [141424] NANA - Smile Hospital care dan smile medical care */
						if((lsbs_id.equals("183") && 
						   (Integer.valueOf(lsdbs_number) >= 135 && Integer.valueOf(lsdbs_number) <= 149)) ||
						   (lsbs_id.equals("195") && 
						   (Integer.valueOf(lsdbs_number) >= 85 && Integer.valueOf(lsdbs_number) <= 96)) ){
								ls_accno[1] = "8927";
						}
						
						if(products.muamalat(premi.getNo_spaj())) {
							ls_accno[1] = "41131"; //Muamalat Indonesia
							ket = "MI ";
						}
						
					}else {
						ls_accno[0] = (isSyariah ? "801" : ("0" + premi.getK_region()));
						ls_accno[1] = akun;
					}
					ls_ket[16] = ket + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
					ldec_total = ldec_selisih;
					this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[16], ldec_total, ls_accno[0], ls_accno[1], ls_flag, premi.getNo_spaj());
				}
			}
			
			//lufi - proses insert nilai komisi pada jurnal premi pertama
//			if(!listComForJm.isEmpty()){
//				li_run = prosesJurnaPencatatanKomisiPertama(listComForJm,premi, lca_id, lwk_id,li_run,isSyariah,false,ll_no_jm);				
//			}
//			
//			if(!listComBonusForJm.isEmpty()){
//				li_run = prosesJurnalPencatatanBonusKomisi(listComBonusForJm,premi, lca_id, lwk_id,li_run,isSyariah,false,ll_no_jm);
//			}
//			
//			if(!listComRewardForJm.isEmpty()){
//				li_run = prosesJurnalPencatatanReward(listComRewardForJm,premi, lca_id, lwk_id,li_run,isSyariah,false,ll_no_jm);
//			}		
		}else {
			errors.reject("premi.premiDasarKosong");
			return null;
		}
		
		//MANTA
		String[] nopre = pre.split(",");
		Payment payment = new Payment();
		payment.setReg_spaj(premi.getNo_spaj());
		payment.setMspa_no_jm(ll_no_jm);
		for(int i=0; i<nopre.length; i++){
			payment.setMspa_no_pre(nopre[i].trim());
			this.editDataDao.updateMstPaymentbySpajAndPre(payment);
		}
		this.editDataDao.updateMstPaymentNonBank(premi.getNo_spaj(), ll_no_jm);
//		this.uwDao.updateNoPreMstDrekDet(null, payment.getReg_spaj(), premi.getMspa_payment_id(), null, null);
		return premi;
		
	}
	
	public Premi jurnalMemorialPremiIndividuKetinggalanNoPre(
			Premi premi, double[] ar_ldec_bayar, String ar_ls_no_voucher, Date ar_max_rk, Double ar_topup, BindException errors, User currentUser, 
			List ar_ttp, List ar_ttp_kurs, List ar_thp, List ar_thp_kurs, List ar_thp_polis, List ar_ttp_voucher) throws Exception{
		if(logger.isDebugEnabled())logger.debug("PROSES: jurnalMemorialPremiIndividu");
		Double ldec_thp;
		Date ldec_beg_date_bill = null;
		String[] ls_accno = new String[2]; String ls_flag; 
		String[] ls_ket = new String[20]; 
		String ls_region; 
		int li_run=0; int z; Integer li_pos = new Integer(1); Integer li_cbid;
		String ll_no_jm; Long li_bisnis; String ls_grp; 
		Date ldt_prod = null; 
		Double ldec_tot_premi; Double ldec_tot_bayar=new Double(0); Double ldec_selisih; Double ldec_total;
		String ls_polis;
		NumberFormat f = new DecimalFormat("#,##0.00;(#,##0.00)");
//		ArrayList listComForJm=new ArrayList();
		ls_region = premi.getK_region();
		ldt_prod = null;
		if(premi.getNo_polis().length()==17) ls_polis = premi.getNo_polis().trim();
		else ls_polis = ls_region + "." + premi.getNo_polis().trim();
		
		if(ls_region.equals("29"))ls_region="21";
		
		Map detbisnis = (Map) uwDao.selectDetailBisnis(premi.getNo_spaj()).get(0);
		String lsbs_id = (String) detbisnis.get("BISNIS");
		String lsdbs_number = (String) detbisnis.get("DETBISNIS");
		
		//Yusuf - 11/03/09 - untuk produk muamalat, profit center nya beda
		boolean isSyariah = false;
		if(products.syariah(lsbs_id, lsdbs_number)) {
			isSyariah = true;
		}
		
		Map tmp = this.uwDao.selectProductionDateAndKurs(premi.getNo_spaj(), premi.getTahunke(), premi.getPremike());
		if(tmp==null) {
			errors.reject("payment.noProductionDate");
			return null;
		}
		String lca_id = uwDao.selectCabangFromSpaj(premi.getNo_spaj());
		Integer jenis = 1;
		
		/** full autosales project start **/
		String flagAutoSalesCek = bacDao.selectFullAutoSalesFromMstPolicy(premi.getNo_spaj());
		if (flagAutoSalesCek != null && flagAutoSalesCek.equals("4")){
			jenis = 4;
		}
		/** full autosales project end **/
		
		if((lsbs_id.equals("190") && lsdbs_number.equals("009"))|| (lsbs_id.equals("200") && lsdbs_number.equals("007"))) jenis = 3;
		//Yusuf (30/04/2008)
		ldt_prod = getTanggalJurnal(ar_max_rk, lca_id, jenis);
		//ldt_prod = (Date) tmp.get("MSPRO_PROD_DATE");
		
		premi.setKurs_harian(new Double(((Integer) tmp.get("MSPRO_NILAI_KURS")).doubleValue()));
	
		if(ldt_prod==null) {
			errors.reject("payment.noProductionDate");
			return null;
		}
		
		if(premi.getKurs_harian()==null) premi.setKurs_harian(new Double (1));
		if(premi.getKurs_bulanan()==null) premi.setKurs_bulanan(new Double(1));
		
		//ldec_tot_premi = new Double(((premi.getPremi_dasar().doubleValue()+premi.getPremi_pa().doubleValue()-premi.getPremi_disc().doubleValue())*premi.getKurs_harian().doubleValue())+premi.getPremi_hcr().doubleValue());
		ldec_tot_premi = new Double(((premi.getPremi_dasar().doubleValue()+premi.getPremi_pa().doubleValue()-premi.getPremi_disc().doubleValue())*premi.getKurs_bulanan().doubleValue())+premi.getPremi_hcr().doubleValue());
		
		li_bisnis = premi.getBisnis_id();
		
		ls_grp = this.uwDao.selectBusinessGroup(li_bisnis);
		if(ls_grp==null) {
			errors.reject("payment.businessGroupFailed");
			return null;
		}else {
			ls_grp = ls_grp.trim();
		}
		
		//Yusuf - 3 Jan 2012 - Req Rudy H, untuk case powersave, LST_GROUP_BISNIS.LSBS_GROUP_ACC bukan termasuk ENDOWNMENT COMBINED/DWIGUNA (2), melainkan 4 (powersave)
		if(products.powerSave(String.valueOf(li_bisnis))) {
			ls_grp = "4";
		}
		//Ryan, Stablesave diubah ke 4, pake validasi diatas terlewat, untuk mastiin aja.
		if(products.stableSave2(String.valueOf(li_bisnis))) {
			ls_grp = "4";
		}
	
		if(premi.getOld_polis()==null) premi.setOld_polis(premi.getNo_polis());
		else if(premi.getOld_polis().equals("")) premi.setOld_polis(premi.getNo_polis());
		
		Map m = this.uwDao.selectBiayaMaterai2(premi.getNo_spaj(), premi.getTahunke(), premi.getPremike());

		if(premi.getPremike()==1) 
			premi.setMaterai(new Double(0));
		else {
			ldec_beg_date_bill = (Date) m.get("MSBI_BEG_DATE");
			premi.setMaterai((Double)m.get("MSBI_STAMP"));
			//premi.setMaterai(new Double(premi.getMaterai().doubleValue()*premi.getKurs_harian().doubleValue()));
			premi.setMaterai(new Double(premi.getMaterai().doubleValue()*premi.getKurs_bulanan().doubleValue()));
		}
	
		for(z=0; z<ar_ldec_bayar.length; z++) {
			ldec_tot_bayar = new Double(ldec_tot_bayar.doubleValue() + ar_ldec_bayar[z]);
		}
		
		//premi.setBiaya_polis(new Double(premi.getBiaya_polis().doubleValue() * premi.getKurs_harian().doubleValue()));
		//premi.setBunga_tunggak(new Double(premi.getBunga_tunggak().doubleValue() * premi.getKurs_harian().doubleValue()));
		premi.setBiaya_polis(new Double(premi.getBiaya_polis().doubleValue() * premi.getKurs_bulanan().doubleValue()));
		premi.setBunga_tunggak(new Double(premi.getBunga_tunggak().doubleValue() * premi.getKurs_bulanan().doubleValue()));
		Double biaya_dplk = new Double("0");
		if(lsbs_id.equals("149")) biaya_dplk = new Double("10000");
		ldec_selisih = new Double((ldec_tot_bayar.doubleValue()-(ar_ldec_bayar[10]*2))-(ldec_tot_premi.doubleValue()+premi.getMaterai().doubleValue()+premi.getBiaya_polis().doubleValue()+premi.getBunga_tunggak().doubleValue()+biaya_dplk.doubleValue()));
		
		if(ldec_selisih.doubleValue()>0) ls_flag="C";
		else {
			ldec_selisih = new Double(ldec_selisih.doubleValue() * -1);
			ls_flag="D";
		}
		//ll_no_jm = this.sequence.sequenceMst_ptc_tm(34);
		ll_no_jm = uwDao.selectNoJmBySpajNB(premi.getNo_spaj());
		if(ll_no_jm==null){
			errors.reject("Sequence PTCTM gagal dilakukan.");
			return null;
		}
		
		//INSERT TMEMO
		String pre = premi.getLs_pre()!=null?!premi.getLs_pre().trim().equals("")?premi.getLs_pre():premi.getNo_pre():premi.getNo_pre();
		//this.uwDao.insertMst_ptc_tm(ll_no_jm, li_pos, ldt_prod, pre, currentUser.getLus_id());
		
		HashMap map = new HashMap();
		map.put("no_jm", ll_no_jm);
		map.put("no_pre", pre);
		uwDao.updateMstPtcTm(map);
	
		return premi;
		
	}

	//(premi, ldec_bayar, ls_voucher, ldt_max_rk, ldec_topup, errors, currentUser, ld_ttp, ld_ttp_kurs, ls_voucher_ttp);
	public Premi jurnalMemorialPremiUlink(Premi premi, double[] ar_ldec_bayar, String ar_ls_no_voucher, Date ar_max_rk, Double ar_topup, BindException errors, User currentUser,
		List ar_ttp, List ar_ttp_kurs, List ar_ttp_voucher, List ld_ttp_flagTp) throws Exception{
		if(logger.isDebugEnabled())logger.debug("PROSES: jurnalMemorialPremiUlink");
		Date ldec_beg_date_bill ;
		String[] ls_accno = new String[2]; String ls_flag; 
		String[] ls_ket = new String[20]; 
		String ls_region; Integer li_pmode;
		int li_run=0; int z; Integer li_pos = new Integer(1); Integer li_cbid;
		String ll_no_jm; Long li_bisnis; String ls_grp; 
		Date ldt_prod; 
		Double ldec_tot_premi; Double ldec_tot_bayar=new Double(0); Double ldec_selisih; Double ldec_total; 
		
		//Yusuf (29/12/2012) - Variable untuk menampung total biaya yang dialokasikan ke premi pokok dan total biaya yg dialokasikan ke premi topup
		Double totalBiayaPP; Double totalBiayaTU;Double d_totalBiayaTUSingle;
		HashMap mapRegion=Common.serializableMap(uwDao.selectRegion(premi.getNo_spaj()));
		Map detbisnis = (Map) uwDao.selectDetailBisnis(premi.getNo_spaj()).get(0);
		String lsbs_id = (String) detbisnis.get("BISNIS");
		String lsdbs_number = (String) detbisnis.get("DETBISNIS");

		Date ldt_now = this.commonDao.selectSysdate();
		String ls_pre = premi.getNo_pre().trim();
		ls_region = premi.getK_region();
		ldt_prod = null;
	
		if(ls_region.equals("29"))ls_region="21";
		String lca_id = (String)mapRegion.get("LCA_ID");
//		String lwk_id = (String)mapRegion.get("LWK_ID");
		Integer jenis = 1;
		
		/** full autosales project start **/
		String flagAutoSalesCek = bacDao.selectFullAutoSalesFromMstPolicy(premi.getNo_spaj());
		if (flagAutoSalesCek != null && flagAutoSalesCek.equals("4")){
			jenis = 4;
		}
		/** full autosales project end **/
		
		if((lsbs_id.equals("190") && lsdbs_number.equals("009")) || (lsbs_id.equals("200") && lsdbs_number.equals("007"))) jenis = 3;
		
		//Yusuf (30/04/2008)
		ldt_prod = getTanggalJurnal(ar_max_rk, lca_id, jenis);
		//ldt_prod = this.uwDao.selectProductionDate(premi.getNo_spaj(), premi.getTahunke(), premi.getPremike());
		
//		ArrayList listComForJm=new ArrayList();
//		ArrayList listComBonusForJm=new ArrayList();
//		ArrayList listComRewardForJm=new ArrayList();
//		if(lca_id.equals("40") && lwk_id.equals("02")){
//			listComForJm=uwDao.selectKomisiForPtcjm(premi.getNo_spaj(),"1");//1 untuk komisi disetahunkan(Arco dkk)
//		}else if(lca_id.equals("09")){
//			listComForJm=uwDao.selectKomisiForPtcjm(premi.getNo_spaj(),"2");//2 bancass
//		}else if(lca_id.equals("60")){
//			listComForJm=uwDao.selectKomisiForPtcjm(premi.getNo_spaj(),"3");//3 bridge dan semua produk yg komisinya di insert mst_diskon_perusahaan (kecuali Arco dkk)
//		}else{
//			listComForJm=uwDao.selectKomisiForPtcjm(premi.getNo_spaj(),"4");//4 komisi yg ada di insert ke mst_commission
//		}
//		
//		listComBonusForJm  =  uwDao.selectCommBonusForPtcjm(premi.getNo_spaj(), "0");
//		listComRewardForJm =  uwDao.selectCommRewardForPtcjm(premi.getNo_spaj(), "0");
//		
		
		if(ldt_prod==null) {
			errors.reject("payment.noProductionDate");
			return null;
		}
		
		li_pmode = this.uwDao.selectPayMode(premi.getNo_spaj());
		
		if(premi.getKurs_harian()==null) premi.setKurs_harian(new Double (1));
		if(premi.getKurs_bulanan()==null) premi.setKurs_bulanan(new Double(1));
		//ldec_tot_premi = new Double(((premi.getPremi_dasar().doubleValue()+premi.getPremi_pa().doubleValue()-premi.getPremi_disc().doubleValue())*premi.getKurs_harian().doubleValue())+premi.getPremi_hcr().doubleValue());
		ldec_tot_premi = new Double(((premi.getPremi_dasar().doubleValue()+premi.getPremi_pa().doubleValue()-premi.getPremi_disc().doubleValue())*premi.getKurs_bulanan().doubleValue())+premi.getPremi_hcr().doubleValue());
		li_bisnis = premi.getBisnis_id();
		
		//Yusuf - 11/06/08 - untuk produk unit link syariah, profit center nya beda
		String lsbs = li_bisnis.toString();
		boolean isSyariah = false;
		if(products.unitLinkSyariah(lsbs) || products.muamalat(premi.getNo_spaj())) {
			isSyariah = true;
		}

		//Yusuf - 30/03/10 - untuk produk stable link, efektif 1 april 2010, profit center nya beda
		boolean isStableLink = false;
		if(products.stableLink(lsbs_id)){
			isStableLink = true;
		}

		ls_grp = this.uwDao.selectBusinessGroup(li_bisnis);
		if(ls_grp==null) ls_grp="";
	
		if(premi.getOld_polis()==null)premi.setOld_polis("");
		
		if(premi.getPremike()==1)premi.setMaterai(new Double(0));
		else {
			premi.setMaterai(this.uwDao.selectBiayaMaterai(premi.getNo_spaj(), premi.getTahunke(), premi.getPremike()));
			//premi.setMaterai(new Double(premi.getMaterai().doubleValue() * premi.getKurs_harian().doubleValue()));
			premi.setMaterai(new Double(premi.getMaterai().doubleValue() * premi.getKurs_bulanan().doubleValue()));
		}
		
		for(z=0; z<ar_ldec_bayar.length; z++) {
			ldec_tot_bayar = new Double(ldec_tot_bayar.doubleValue() + ar_ldec_bayar[z]);
		}
	
		if(premi.getPremi_ar().doubleValue()!=0) {
			ldec_tot_bayar = new Double(ldec_tot_bayar.doubleValue() + premi.getPremi_ar().doubleValue());
		}
	
		//premi.setBiaya_polis(new Double(premi.getBiaya_polis().doubleValue() * premi.getKurs_harian().doubleValue()));
		premi.setBiaya_polis(new Double(premi.getBiaya_polis().doubleValue() * premi.getKurs_bulanan().doubleValue()));

		//if(products.stableLink(lsbs)) ar_topup = (double) 20000000; 

		ldec_selisih = new Double(ldec_tot_bayar.doubleValue() - (ldec_tot_premi.doubleValue()+premi.getMaterai().doubleValue()+premi.getBiaya_polis().doubleValue()+ar_topup.doubleValue()));

		//biar gak ke-insert yg 8927 (yusuf - 11 feb 09)
		/*if(products.stableLink(lsbs)){
			ldec_selisih = 0.;
		}*/
		
		if(ldec_selisih.doubleValue()>0) ls_flag="C";
		else {
			ldec_selisih = new Double(ldec_selisih.doubleValue() * -1);
			ls_flag="D";
		}
		//ll_no_jm = this.sequence.sequenceMst_ptc_tm(34);
		ll_no_jm = this.uwDao.selectGetPacGl("nojm");
		if(ll_no_jm==null){
			errors.reject("Sequence PTCTM gagal dilakukan.");
			return null;
		}
		
		//INSERT TMEMO;
		this.uwDao.insertMst_ptc_tm(ll_no_jm, li_pos, ldt_prod, ls_pre, currentUser.getLus_id());
	
		//INSERT JMEMO1 (Titipan Premi = TP, untuk seluruh premi NB, bisa lebih dari 1 row)
		//Yusuf - 29/02/12 - Req Gesti Akunting - untuk produk unit link syariah, profit center untuk sisi kredit 801, bukan 850 lagi
		//Deddy (11/05/2015) - Req Gesti Akunting(helpdesk 70088) - ditambahkan keterangan no_spaj pada saat proses jurnal titipan premi pertama (Cth Format : TP_No Spaj_No Polis_No Voucher_Tgl RK).
		if(ar_ldec_bayar[0]!=0) {
			for(int li_ttp=0; li_ttp<ar_ttp.size(); li_ttp++) {
				li_run++;
				ls_ket[0] = "TP " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ttp_voucher.get(li_ttp) + " " + defaultDateFormatStripes.format(ar_max_rk);
				if(premi.getPremike()==1) {
					if(((Double)ar_ttp_kurs.get(li_ttp)).doubleValue()>1) {
						ls_accno[0] = (isSyariah ? "801" : isStableLink ? "701" : "501");
						ls_accno[1] = "41112";
					}else {
						ls_accno[0] = (isSyariah ? "801" : isStableLink ? "701" : "501");
						ls_accno[1] = "41111";
					}
				}else {
					if(((Double)ar_ttp_kurs.get(li_ttp)).doubleValue()>1) {
						ls_accno[0] = (isSyariah ? "801" : isStableLink ? "701" : "501");
						ls_accno[1] = "41122";
					}else {
						ls_accno[0] = (isSyariah ? "801" : isStableLink ? "701" : "501");
						ls_accno[1] = "41121";
					}
				}
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[0], (Double) ar_ttp.get(li_ttp), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
			}
		}

		//INSERT JMEMO AR (tidak ada di NB)
		if(premi.getPremi_ar().doubleValue()!=0) {
			li_run++;
			ls_ket[0] = "AR " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " "  + premi.getNo_polis().trim() + " " + ar_ls_no_voucher + " " + defaultDateFormatStripes.format(ar_max_rk);
			ls_accno[0] = (isSyariah ? "850" : isStableLink ? "701" : "501");
			ls_accno[1] = "021002";
			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[0], premi.getPremi_ar(), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
		}
		//INSERT JMEMO2 (Nilai Tunai, tidak ada di NB)
		if(ar_ldec_bayar[1]!=0) {
			li_run++;
			ls_ket[1]   = "NT " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher  + " " + premi.getOld_polis().trim();
			if(premi.getKurs_bulanan().doubleValue()>1) {
				ls_accno[0] = (isSyariah ? "850" : isStableLink ? "701" : "501");
				ls_accno[1] = "41152";
			}else {
				ls_accno[0]= (isSyariah ? "850" : isStableLink ? "701" : "501");
				ls_accno[1]= "41151";
			}
			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[1], new Double(ar_ldec_bayar[1]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
		}
		//INSERT JMEMO3 (Tahapan, tidak ada di NB)
		if(ar_ldec_bayar[2]!=0) {
			li_run++;
			ls_ket[2]   = "TH " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + ls_region + premi.getNo_polis().trim() + " " + ar_ls_no_voucher  + " " + premi.getOld_polis().trim();
			if(premi.getKurs_bulanan().doubleValue()>1) {
				ls_accno[0] = (isSyariah ? "850" : isStableLink ? "701" : "501");
				ls_accno[1] = "41152";
			}else {
				ls_accno[0]= (isSyariah ? "850" : isStableLink ? "701" : "501");
				ls_accno[1]="41151";
			}
			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[2], new Double(ar_ldec_bayar[2]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
		}
		//INSERT JMEMO4 (Death Claim, tidak ada di NB)
		if(ar_ldec_bayar[3]!=0) {
			li_run++;
			ls_ket[3]   = "DP " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher  + " " + premi.getOld_polis().trim();
			if(premi.getKurs_bulanan().doubleValue()>1) {
				ls_accno[0] = (isSyariah ? "850" : isStableLink ? "701" : "501");
				ls_accno[1] = "337122";
			}else {
				ls_accno[0]=(isSyariah ? "850" : isStableLink ? "701" : "501");
				ls_accno[1]="337121";
			}
			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[3], new Double(ar_ldec_bayar[3]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
		}
		//INSERT JMEMO5 (tidak ada di NB)
		if(ar_ldec_bayar[4]!=0) {
			li_run++;
			ls_ket[4]   = "PJ " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher  + " " + premi.getOld_polis().trim();
			if(premi.getKurs_bulanan().doubleValue()>1) {
				ls_accno[0] = (isSyariah ? "850" : isStableLink ? "701" : "501");
				ls_accno[1] = "118122";
			}else {
				ls_accno[0]=(isSyariah ? "850" : isStableLink ? "701" : "501");
				ls_accno[1]="118121";
			}
			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[4], new Double(ar_ldec_bayar[4]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
		}
		//INSERT JMEMO7 (tidak ada di NB)
		if(ar_ldec_bayar[6]!=0) {
			li_run++;
			ls_ket[6]   = "BB " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
			if(premi.getKurs_bulanan().doubleValue()>1) {
				ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
				ls_accno[1] = "41162";
			}else {
				ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
				ls_accno[1] = "41161";
			}
			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[6], new Double(ar_ldec_bayar[6]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
		}
		//INSERT JMEMO8 (tidak ada di NB)
		if(ar_ldec_bayar[7]!=0) {
			li_run++;
			ls_ket[7]   = "KA " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
			if(premi.getKurs_bulanan().doubleValue()>1) {
				ls_accno[0] = (isSyariah ? "850" : isStableLink ? "701" : "501");
				ls_accno[1] = "41112";
			}else {
				ls_accno[0]=(isSyariah ? "850" : isStableLink ? "701" : "501");
				ls_accno[1]="41111";
			}
			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[7], new Double(ar_ldec_bayar[7]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
		}
		//INSERT JMEMO8 (tidak ada di NB)
		if(ar_ldec_bayar[9]!=0) {
			li_run++;
			ls_ket[9]   = "AK " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
			if(premi.getKurs_bulanan().doubleValue()>1) {
				ls_accno[0] = (isSyariah ? "850" : isStableLink ? "701" : "501");
				ls_accno[1] = "41152";
			}else {
				ls_accno[0]=(isSyariah ? "850" : isStableLink ? "701" : "501");
				ls_accno[1]="41151";
			}
			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[9], new Double(ar_ldec_bayar[9]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
		}
		//INSERT JMEMO8 (tidak ada di NB)
		if(ar_ldec_bayar[10]!=0) {
			li_run++;
			ls_ket[10]   = "PG " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
			if(premi.getKurs_bulanan().doubleValue()>1) {
				ls_accno[0] = (isSyariah ? "850" : isStableLink ? "701" : "501");
				ls_accno[1] = "41172";
			}else {
				ls_accno[0]=(isSyariah ? "850" : isStableLink ? "701" : "501");
				ls_accno[1]="41171";
			}
			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[10], new Double(ar_ldec_bayar[10]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
		}
		//BUDGET PROMOSI
		if(ar_ldec_bayar[11]!=0) {
			li_run++;
			ls_ket[11]   = "CAMPAIGN FREE POLIS " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim();
			ls_accno[0] = "040";
			ls_accno[1] = "811999";
			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[11], new Double(ar_ldec_bayar[11]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
		}
		
		if(premi.getPremi_dasar().doubleValue()!=0) {
			if(premi.getPremike()==1) {
				//INSERT JMEMO11 (Premi Pokok)
				if(li_pmode==0) {
					ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
					if(products.stableLink(lsbs_id)) {
						ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 1);//"611117";
					}else {
						ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 1);//"611118";
					}
					
				}else {
					ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
					ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 3);//"611119";		
				}
				ls_ket[10]="PP " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
			}else {
				//INSERT JMEMO11 (Premi Lanjutan, tidak ada di NB)
				if(premi.getTahunke()==1) {
					ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
					//Yusuf (19/05/2010) - Req Tony Johan by Email : 611129 rubah ke 6111171
					if(products.stableLink(lsbs)){
						ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 4);//"6111171"; 
					}else{
						ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 4);//"611129";
					}
				}else {
					ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
					ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 5);//"61219";		
				}
				ls_ket[10]="PL " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
			}
			
			//Yusuf (29/02/2012) - Req Gesti Akunting - Jurnal PP dan TU syariah, tidak disimpan bulat2 angkanya, tapi dikurang2i dulu dengan biaya2.
			totalBiayaPP = 0.;
			totalBiayaTU = 0.;
			d_totalBiayaTUSingle=0.;
			if(isSyariah){
				List<Map> daftarBiaya = this.uwDao.selectMemorialUnitLink(premi.getNo_spaj());
				if(!daftarBiaya.isEmpty()) {
					//looping dulu, total semua biaya untuk PP dan biaya untuk TU
					for(Map biaya : daftarBiaya) {
						String flag_jenis = (String) biaya.get("FLAG_JENIS");
						if( flag_jenis.trim().equalsIgnoreCase("Biaya Asuransi") 	|| flag_jenis.trim().equalsIgnoreCase("Biaya Akuisisi") ||
							flag_jenis.trim().equalsIgnoreCase("Biaya Adm")){ //flag_jenis.trim().equalsIgnoreCase("Biaya Extra") perlu gak?
							totalBiayaPP = totalBiayaPP.doubleValue() + ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue();
						}else if(flag_jenis.trim().equalsIgnoreCase("Biaya TopUp")){
							if(((BigDecimal)biaya.get("LT_ID")).intValue()==5){
								totalBiayaTU = (totalBiayaTU.doubleValue() + ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan());
							}else if(((BigDecimal)biaya.get("LT_ID")).intValue()==2){
								d_totalBiayaTUSingle=(d_totalBiayaTUSingle.doubleValue()+((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan());
							}
						}
					}
					//pengecekan tambahan apabila biaya PP > premi PP, maka harus dialokasikan ke Top Up
					//Double premiPP = new Double (premi.getPremi_dasar().doubleValue() * premi.getKurs_harian().doubleValue()); //total PP
					Double premiPP = new Double (premi.getPremi_dasar().doubleValue() * premi.getKurs_bulanan().doubleValue()); //total PP
					if(premiPP.doubleValue() <= totalBiayaPP.doubleValue()){ //bila premiPP <= biayaPP
						ldec_total = 0.;
						totalBiayaPP = totalBiayaPP.doubleValue() - premiPP.doubleValue();//total biaya2(rider2,adm,asuransi,akuisisi)
					}else{
						ldec_total = premiPP.doubleValue() - totalBiayaPP.doubleValue();
						totalBiayaPP=0.;
					}
					
					//insert PP hanya bila > 0 (tidak habis dipotong biaya)
					if(ldec_total.doubleValue() > 0){
						li_run++;
						this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[10], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
					}
				}
			}else{
				li_run++;
				//ldec_total = new Double (premi.getPremi_dasar().doubleValue() * premi.getKurs_harian().doubleValue());
				ldec_total = new Double (premi.getPremi_dasar().doubleValue() * premi.getKurs_bulanan().doubleValue());
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[10], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
			}
			
			//INSERT JMEMO18 (Topup = TU)
			//Yusuf (29/12/2012) - Untuk Syariah, Jurnal Topup tidak disimpan bulat2, tapi dikurangi biaya TU
			//Lufi(23/06/2014) - Req Derry Accounting Top up Berkala Dan Single Dibedakan
			if(ar_topup.doubleValue()>0) {
				Integer i_flagTb=0,i_flagTs=0;
				Double ar_tu_awal=0.;
				
				for(int li_topup=0; li_topup<ar_ttp.size(); li_topup++) {
					
					int i_flag_tu=(Integer) ld_ttp_flagTp.get(li_topup);
					Double ar_tu=(Double)ar_ttp.get(li_topup);	
					ar_tu_awal=ar_tu.doubleValue();
					
					if(i_flag_tu>=1){
						ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
						li_run++;
						if(i_flag_tu==1){
							if(isSyariah && i_flagTs<=0) ar_tu = ar_tu.doubleValue() - d_totalBiayaTUSingle.doubleValue();
							ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 2);
							if(ar_tu.doubleValue()<0){
								ar_tu=0.;
								d_totalBiayaTUSingle=d_totalBiayaTUSingle.doubleValue()-ar_tu_awal.doubleValue();
							}else{
								i_flagTs=1;
							}
							
							ls_ket[17] = "Top Up Single " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ttp_voucher.get(li_topup)+ " " + defaultDateFormatStripes.format(ar_max_rk);
						}else if(i_flag_tu==2 ){
							if(isSyariah  && i_flagTb<=0) ar_tu = ar_tu.doubleValue() - totalBiayaTU.doubleValue();
							ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 6);
							if(ar_tu.doubleValue()<0){
								ar_tu=0.;
								totalBiayaTU=totalBiayaTU.doubleValue()-ar_tu_awal.doubleValue();
							}else{
								i_flagTb=1;
							}
							
							ls_ket[17] = "Top Up Berkala " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ttp_voucher.get(li_topup)+ " " + defaultDateFormatStripes.format(ar_max_rk);
						}
						if(isSyariah){
							
							/**
							 *Lufi(05/08/2015)
							 *Jika Syariah Top up dikurangi oleh jumlah total biaya yg belum tercover oleh Premi pokok 
							 * 							 * 
							 **/
							if(totalBiayaPP>0){//bila total biaya-biaya(asuransi,rider2,adm,akuisisi)>0
								if(ar_tu<totalBiayaPP){							
									totalBiayaPP=totalBiayaPP.doubleValue() - ar_tu.doubleValue();
									ar_tu=0.;
								}else{
									ar_tu=ar_tu.doubleValue() - totalBiayaPP.doubleValue();
									totalBiayaPP=0.;
								}
							}
						}
						this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[17], ar_tu, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());						
					}					
				}			
			}
			
			//INSERT JMEMO13 (tidak ada di NB)
			if(premi.getPremi_pa().doubleValue()!=0) {
				li_run++;
				if(premi.getTahunke()==1) {
					ls_accno[0] = "0" + premi.getK_region(); // : (isSyariah ? "801" : ("0"+premi.getK_region()));
					ls_accno[1] = "611126";
				}else {
					ls_accno[0] = "0" + premi.getK_region();
					ls_accno[1] = "61216";
				}
				ls_ket[12] = "PA " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis() + " " + ar_ls_no_voucher;
				//ldec_total = new Double(premi.getPremi_pa().doubleValue() * premi.getKurs_harian().doubleValue());
				ldec_total = new Double(premi.getPremi_pa().doubleValue() * premi.getKurs_bulanan().doubleValue());
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[12], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
			}
			
			//INSERT JMEMO14 (tidak ada di NB)
			if(premi.getPremi_disc()!=0) {
				li_run++;
				if(premi.getPremike()==1) {
					ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
					ls_accno[1] = "62111";
				}else {
					if(premi.getTahunke()==1) {
						ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+premi.getK_region()) : ("5"+premi.getK_region()));
						ls_accno[1] = "62112";
					}else {
						ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+premi.getK_region()) : ("5"+premi.getK_region()));
						ls_accno[1] = "6221";
					}
				}
				ls_ket[13] = "DC " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
				//ldec_total = new Double(premi.getPremi_disc().doubleValue() * premi.getKurs_harian().doubleValue());
				ldec_total = new Double(premi.getPremi_disc().doubleValue() * premi.getKurs_bulanan().doubleValue());
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[13], ldec_total, ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
			}
			
			//INSERT JMEMO15 (tidak ada di NB)
			if(premi.getBiaya_polis().doubleValue()!=0) {
				li_run++;
				ls_ket[14] = "BP " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
				ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
				ls_accno[1] = "693";
				ldec_total = premi.getBiaya_polis();
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[14], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
			}
			//INSERT JMEMO16 (tidak ada di NB)
			if(premi.getMaterai().doubleValue()!=0) {
				li_run++;
				ls_accno[0] = "001";
				ls_accno[1] = "835316";
				ls_ket[15] = "MT " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis() + " " + ar_ls_no_voucher;
				ldec_total = premi.getMaterai();
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[15], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
			}
			
			if(ldec_selisih.doubleValue() !=0) {
				//Yusuf (11/7/08) Bila selisih dalam rupiah masuk Selisih Pembayaran, bila dollar masuk Selisih Kurs
				
				String akun = "8923"; //selisih kurs
				String ket = "SK ";
				if(premi.getLku_id().equals("01")) {
					akun = "8927";
					ket = "SP ";
				}
				
				li_run++;
				if(ls_flag.equals("C")) {
					ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
					ls_accno[1] = "8914";
					
					if(lsbs_id.equals("208") && (Integer.valueOf(lsdbs_number) >= 45 && Integer.valueOf(lsdbs_number) <= 48) ){
						ls_accno[1] = "8927";
					}
					
					/* [141424] NANA - Smile Hospital care dan smile medical care */
					if((lsbs_id.equals("183") && 
					   (Integer.valueOf(lsdbs_number) >= 135 && Integer.valueOf(lsdbs_number) <= 149)) ||
					   (lsbs_id.equals("195") && 
					   (Integer.valueOf(lsdbs_number) >= 85 && Integer.valueOf(lsdbs_number) <= 96)) ){
							ls_accno[1] = "8927";
					}
					
					if(products.muamalat(premi.getNo_spaj())) {
						ls_accno[1] = "41131"; //Muamalat Indonesia
						ket = "MI ";
					}
					
				}else {
					ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
					ls_accno[1] = akun;
				}					
				ls_ket[16] = ket + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
				ldec_total = ldec_selisih;
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[16], ldec_total, ls_accno[0], ls_accno[1], ls_flag, premi.getNo_spaj());
			}
			
			//lufi-3/7/2014 untuk biaya top up dipisah jadi single dan berkala jika berkala 611191 single 6111081
			if(Integer.valueOf(this.uwDao.selectBulanProduksi(premi.getNo_spaj())) >= 200803) {
				List<Map> daftarBiaya = this.uwDao.selectMemorialUnitLink(premi.getNo_spaj());
				if(!daftarBiaya.isEmpty()) {
					
					//Yusuf - 29 Feb 2012 - as of march 2012, jurnal syariah tidak disimpan angkanya bulat2, tapi dikurangi dengan biaya2
					if(isSyariah){
					
						//insert sisi KREDIT (bukan sisi debet, seperti konvensional)
						for(Map biaya : daftarBiaya) {
							String flag_jenis = (String) biaya.get("FLAG_JENIS");
							Integer ljb_id = ((BigDecimal)biaya.get("LJB_ID")).intValue();
							
							//Pendapatan Akuisisi
							if(flag_jenis.trim().equalsIgnoreCase("Biaya Akuisisi")) {
								li_run++;
								ls_accno[0] = "801";
								ls_accno[1] = "611119";
								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Akuisisi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
							
							//Pendapatan Asuransi dan Rider
							}else if(flag_jenis.trim().equalsIgnoreCase("Biaya Asuransi")) {
								//Req Rudy : UNTUK BIAYA ASURANSI SYARIAH DAN RIDER DIPECAH JADI 2.(BIAYA ASURANSI JUGA DIPECAH JADI 2 YAKNI TABBARU DAN UJRAH)
								ls_accno[0] = "802";
								ls_accno[1] = "611119";
								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
								if(ljb_id==0){//di select ljb_id di set 0 untuk sum biaya rider.
									li_run++;
									this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Asuransi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
								}else{
									for(int i=0;i<2;i++){//looping 2 kali untuk set ujrah dan tabarru
										li_run++;
										Double persenTabarru = getTabarru(premi.getNo_spaj());
										Double persenUjrah = 1-persenTabarru;
										if(i==0){//jika ujrah(i=0) ls_accno[0]= "801"
											ls_accno[0] = "801";
											ldec_total = (((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue() * persenUjrah)*premi.getKurs_bulanan();
										}else if(i==1){//jika tabarru(i=1) ls_accno[0]= "802"
											ls_accno[0] = "802";
											ldec_total = (((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue() * persenTabarru)*premi.getKurs_bulanan();
										}
										this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Asuransi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
									}
								}
							//Pendapatan Akuisisi (Top Up)
							} else if(flag_jenis.trim().equalsIgnoreCase("Biaya TopUp")) {
								li_run++;
								ls_accno[0] = "801";
								if(((BigDecimal)biaya.get("LT_ID")).intValue()==5){
									ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 6);
								}else if(((BigDecimal)biaya.get("LT_ID")).intValue()==2){
									ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 2);
								}
								
								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By TopUp " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
								
							//Pendapatan Administrasi
							} else if(flag_jenis.trim().equalsIgnoreCase("Biaya Adm")) {
								li_run++;
								ls_accno[0] = "801";
								ls_accno[1] = "611119";
								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Administrasi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
							}
							
							//Pendapatan Ekstra (perlu dicatat gak?)
						}
						
					//Untuk produk ulink non syariah, biasa saja
					}else{
						//PERTAMA, insert dulu yang sisi debet
						for(Map biaya : daftarBiaya) {
							String flag_jenis = (String) biaya.get("FLAG_JENIS");
							
							//Biaya Akuisisi
							if(flag_jenis.trim().equalsIgnoreCase("Biaya Akuisisi")) {
								li_run++;
								ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+premi.getK_region()) : ("5"+premi.getK_region()));
								ls_accno[1] = "692";
								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Akuisisi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
							
							//Biaya Asuransi dan Biaya Rider
							}else if(flag_jenis.trim().equalsIgnoreCase("Biaya Asuransi")) {
								li_run++;
								ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+premi.getK_region()) : ("5"+premi.getK_region()));
								ls_accno[1] = "696";
								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Asuransi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
								
							//Biaya Akuisisi (Top Up)
							} else if(flag_jenis.trim().equalsIgnoreCase("Biaya TopUp")) {
								li_run++;
								ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+premi.getK_region()) : ("5"+premi.getK_region()));
								ls_accno[1] = "697";
								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By TopUp " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
								
							//Biaya Administrasi
							} else if(flag_jenis.trim().equalsIgnoreCase("Biaya Adm")) {
								li_run++;
								ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+premi.getK_region()) : ("5"+premi.getK_region()));
								ls_accno[1] = "698";
								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Administrasi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
							}
							
							//Biaya Ekstra (perlu dicatat gak?)
							
							//
						}
						
						//KEDUA, insert yang sisi credit, sama persis seperti yg sisi debet, beda di 3 digit account depannya, sama di kode C atau D nya

						for(Map biaya : daftarBiaya) {
							String flag_jenis = (String) biaya.get("FLAG_JENIS");
							
							//Pendapatan Akuisisi
							if(flag_jenis.trim().equalsIgnoreCase("Biaya Akuisisi")) {
								li_run++;
								ls_accno[0] = (isSyariah ? "801" : ("0"+premi.getK_region()));
								ls_accno[1] = "692";
								ldec_total = (((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue())*premi.getKurs_bulanan();
								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Akuisisi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
							
							//Pendapatan Asuransi dan Rider
							}else if(flag_jenis.trim().equalsIgnoreCase("Biaya Asuransi")) {
								li_run++;
								ls_accno[0] = (isSyariah ? "801" : ("0"+premi.getK_region()));
								ls_accno[1] = "696";
								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Asuransi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
								
							//Pendapatan Akuisisi (Top Up)
							} else if(flag_jenis.trim().equalsIgnoreCase("Biaya TopUp")) {
								li_run++;
								ls_accno[0] = (isSyariah ? "801" : ("0"+premi.getK_region()));
								ls_accno[1] = "697";
								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By TopUp " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
								
							//Pendapatan Administrasi
							} else if(flag_jenis.trim().equalsIgnoreCase("Biaya Adm")) {
								li_run++;
								ls_accno[0] = (isSyariah ? "801" : ("0"+premi.getK_region()));
								ls_accno[1] = "698";
								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Administrasi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
							}
							
							//Pendapatan Ekstra (perlu dicatat gak?)
		
							//
						}
						//
					}
									
				}
			}
		}
		
		//lufi - proses insert nilai komisi pada jurnal premi pertama
//		if(!listComForJm.isEmpty()){
//			li_run = prosesJurnaPencatatanKomisiPertama(listComForJm,premi, lca_id, lwk_id,li_run,isSyariah,isStableLink,ll_no_jm);				
//		}
//		
//		if(!listComBonusForJm.isEmpty()){
//			li_run = prosesJurnalPencatatanBonusKomisi(listComBonusForJm,premi, lca_id, lwk_id,li_run,isSyariah,isStableLink,ll_no_jm);
//		}
//		
//		if(!listComRewardForJm.isEmpty()){
//			li_run = prosesJurnalPencatatanReward(listComRewardForJm,premi, lca_id, lwk_id,li_run,isSyariah,isStableLink,ll_no_jm);
//		}
		
		//MANTA
		String[] nopre = ls_pre.split(",");
		Payment payment = new Payment();
		payment.setReg_spaj(premi.getNo_spaj());
		payment.setMspa_no_jm(ll_no_jm);
		for(int i=0; i<nopre.length; i++){
			payment.setMspa_no_pre(nopre[i].trim());
			this.editDataDao.updateMstPaymentbySpajAndPre(payment);
		}
		this.editDataDao.updateMstPaymentNonBank(premi.getNo_spaj(), ll_no_jm);
//		this.uwDao.updateNoPreMstDrekDet(null, payment.getReg_spaj(), premi.getMspa_payment_id(), null, ll_no_jm);
		return premi;
	}

	public Premi jurnalProduksiIndividu(String spaj, Premi premi, BindException errors, User currentUser) throws Exception{
		if(logger.isDebugEnabled())logger.debug("PROSES: jurnalProduksiIndividu");
		
		if(spaj==null){
			errors.reject("payment.noSPAJ");
			return null;						
		}
		
		List lds_mst_prod_ins; List payment;
		int i;
		String ls_kurs_polis; String ls_id; String ls_kurs_bayar; String ls_voucher=""; String ls_no_voucher=""; String ls_no_pre=null; String ls_vch; 
		int li_row, li_thp=0;Integer li_jurnal; Integer li_cr_bayar; 
		Date ldt_tglrk; Date ldt_max_rk; Date ldt_prod;
		Double ldec_jumlah; Double ldec_topup=null;
		double ldec_bayar[] = {0,0,0,0,0,0,0,0,0,0,0,0};
		boolean lJurnal = false;
		
		List ld_ttp = new ArrayList();
		List ld_ttp_kurs = new ArrayList();
		List ls_voucher_ttp = new ArrayList();
		
		List ld_thp = new ArrayList();
		List ld_thp_kurs = new ArrayList();
		List ls_polis_thp = new ArrayList();		
		
		//double ld_ttp[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		//double ld_ttp_kurs[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		//String ls_voucher_ttp[] = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
		//double ld_thp[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		//double ld_thp_kurs[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		//String ls_polis_thp[] = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
		
		premi.setTahunke(new Integer(1));
		premi.setPremike(new Integer(1));
		premi.setPremi_ar(new Double(0));
		
		premi.setNo_pre(null);
		premi.setMri(false);
		premi.setNo_spaj(spaj);
		
		Map tmp = this.uwDao.selectCabangDanKursFromSpaj(spaj);
		
		if(tmp==null){
			errors.reject("payment.select.mst_policy.error");
			return null;			
		}else{
			premi.setK_region((String) tmp.get("LCA_ID"));
			ls_kurs_polis = (String) tmp.get("LKU_ID");
			try {ldt_prod = this.uwDao.selectProductionDate(spaj, premi.getTahunke(), premi.getPremike());} catch(Exception e) {
				errors.reject("payment.noProductionDate");
				return null;
			}
			premi.setNo_polis(this.uwDao.selectNoPolisFromSpaj(spaj));
			premi.setNo_polis(FormatString.nomorPolis(premi.getNo_polis()));
			
			if(premi.getNo_polis()==null){
				errors.reject("payment.noPolis");
				return null;			
			}
			lds_mst_prod_ins = this.uwDao.selectProductInsured(spaj, 1);
			if(lds_mst_prod_ins.isEmpty()){
				errors.reject("payment.noProductInsured");
				return null;			
			}
		}
		
		premi.setPremi_pa(new Double(0));
		premi.setPremi_dasar(new Double(0));
		premi.setPremi_disc(new Double(0));
		
		for(i=0; i<lds_mst_prod_ins.size(); i++){
			Map temp = (HashMap) lds_mst_prod_ins.get(i);
			if(((Integer) temp.get("LSBS_ID")).toString().equals("800")){
				premi.setPremi_pa(new Double(premi.getPremi_pa().doubleValue()+new Double(temp.get("MSPR_PREMIUM").toString()).doubleValue())); //premi pa
			}else{
				premi.setPremi_dasar(new Double(premi.getPremi_dasar().doubleValue()+new Double(temp.get("MSPR_PREMIUM").toString()).doubleValue())); //semua premi kecuali pa
			}
			premi.setPremi_disc(new Double(premi.getPremi_disc().doubleValue()+new Double(temp.get("MSPR_DISCOUNT").toString()).doubleValue())); //diskon premi
		}
		
		String param = "individu";
		String lsbs = uwDao.selectBusinessId(spaj);
		if(products.stableLink(lsbs)) {
			param = "ulink";
		}
		
		try{
			li_row = this.uwDao.selectTagPaymentCount(spaj, 1, 1, param);
		}catch(Exception e) {
			errors.reject("payment.noTagPayment");
			return null;						
		}
		
		premi.setBiaya_polis(this.uwDao.selectBiayaPolis(spaj, 1, 1));
		if(premi.getBiaya_polis()==null){
			errors.reject("payment.noBilling");
			return null;									
		}
		
		ldt_max_rk = this.uwDao.selectMaxRkDate(spaj, 1, 1, param);
		if(ldt_max_rk==null){
			errors.reject("payment.rkFailed");
			return null;
		}
		
		payment = this.uwDao.selectDetailPayment(spaj, 1, 1, param);
		if(payment.isEmpty()){
			errors.reject("payment.noTagPayment");
			return null;
		}
		
		for(i=0; i<li_row; i++){
			Map m = (HashMap) payment.get(i);
			ls_vch = (String) m.get("MSPA_NO_VOUCHER");
			li_jurnal = (Integer) m.get("MSPA_JURNAL");
			ldec_jumlah = (Double) m.get("MSTP_VALUE");
			ldt_tglrk = (Date) m.get("MSPA_DATE_BOOK");
			ls_kurs_bayar = (String) m.get("LKU_ID");
			li_cr_bayar = (Integer) m.get("LSJB_ID");
			premi.setRek_id((Integer) m.get("LSREK_ID"));
			ls_no_pre = (String) m.get("MSPA_NO_PRE");
			if(ls_no_pre==null)ls_no_pre="";
			ls_id = (String) m.get("MSPA_PAYMENT_ID");
			premi.setMspa_payment_id(ls_id);
			String ls_old_polis = (String) m.get("MSPA_OLD_POLICY");
			premi.setNo_jm_sa((String) m.get("MSPA_NO_JM_SA"));
			
			if(li_jurnal==null)li_jurnal = new Integer(0);
			if(ls_vch==null) ls_vch="";
			if(li_jurnal==0) {
				if(ls_vch.trim().equals("") || ls_vch.trim().equals(",")) ls_vch = "";
				else if(ls_voucher.indexOf(ls_vch)<0) ls_voucher = ls_voucher + ", " + ls_vch;
			} else ls_voucher = ls_vch;

			if(premi.getNo_pre()==null) premi.setNo_pre(ls_no_pre);
			else if(premi.getNo_pre().trim().equals("") || premi.getNo_pre().trim().equals(",")) premi.setNo_pre(ls_no_pre);
			else if(!ls_no_pre.trim().equals(""))premi.setNo_pre(premi.getNo_pre()+", "+ls_no_pre);
			
			if(ls_old_polis==null) premi.setOld_polis("");
			else {
				ls_old_polis = FormatString.nomorPolis(ls_old_polis);
				if(premi.getOld_polis()==null) premi.setOld_polis(ls_old_polis);
				else if(premi.getOld_polis().trim().equals("")) premi.setOld_polis(ls_old_polis);
				else if(!premi.getOld_polis().contains(ls_old_polis)) premi.setOld_polis(premi.getOld_polis()+", "+ls_old_polis);
			}
			
			if(ls_kurs_bayar.equals("02")){
				premi.setKurs_bulanan(this.uwDao.selectMonthlyKurs("02", defaultDateFormat.format(ldt_tglrk).substring(6), defaultDateFormat.format(ldt_tglrk).substring(3,5)));
				if(premi.getKurs_bulanan()==null){
					errors.reject("payment.noMonthlyCurrency", 
							new Object[]{defaultDateFormat.format(ldt_tglrk).substring(6), defaultDateFormat.format(ldt_tglrk).substring(3,5)}, "Kurs tahun {0} bulan {1} tidak ada, harap hubungi CSF");
				}else if(premi.getKurs_bulanan().doubleValue()==0){
					errors.reject("payment.noMonthlyCurrency", 
							new Object[]{defaultDateFormat.format(ldt_tglrk).substring(6), defaultDateFormat.format(ldt_tglrk).substring(3,5)}, "Kurs tahun {0} bulan {1} tidak ada, harap hubungi CSF");
				}
			}else{
				premi.setKurs_bulanan(new Double(1));
			}
			
			if(ls_kurs_polis.equals("02")){
				Map a = this.uwDao.selectTodayKurs("02", ldt_tglrk);
				if(a==null){
					errors.reject("payment.noDailyCurrency");
					return null;
				}else{
					premi.setKurs_harian((Double) a.get("LKH_CURRENCY"));
				}
			}else{
				premi.setKurs_harian(new Double(1));
			}
			
			//Cek eka.mst_drek_det
			List<DrekDet> mstDrekDetBasedSpaj = uwDao.selectMstDrekDet(null, spaj, ls_id, null);
			if(mstDrekDetBasedSpaj.isEmpty() && premi.getRek_id()!=0){
				errors.reject("payment.noIbank");
				return null;
			}
			
			if((li_cr_bayar>=1 && li_cr_bayar<=9) || li_cr_bayar==18 || li_cr_bayar==20 || li_cr_bayar ==29){ //cash
				
				//Jurnal = gunakan kurs bulanan
				ldec_jumlah = new Double(ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue());
				
				ldec_bayar[0] += ldec_jumlah.doubleValue();
				if(li_cr_bayar==20) ldec_bayar[10] = ldec_jumlah.doubleValue();
				if(li_jurnal.doubleValue()!=1){
					premi = this.sequence.sequenceVoucherPremiIndividu(premi, ls_no_pre, ls_no_voucher, li_cr_bayar, errors); 
					if(premi!=null){
						ls_no_voucher = premi.getVoucher();
						if(ls_no_voucher==null) ls_no_voucher="";
						if(!ls_no_voucher.trim().equals("")) if(ls_voucher.indexOf(ls_no_voucher)<0) ls_voucher +=", "+ls_no_voucher;
						//Deddy (17 Feb 2015) - Jurnal TTD : Apabila no_pre dari mst_drek exists, pakai jurnal baru (Jurnal TTD).Apabila tidak ada, pakai jurnal bank yg lama.
						if(!StringUtil.isEmpty(ls_no_pre)){	
							premi = jurnal_pre_individu_jm(ldec_jumlah, ldt_tglrk, premi, li_cr_bayar, ls_no_pre, errors, currentUser);
							if(StringUtil.isEmpty(premi.getProject()[0])){
								errors.reject("payment.jurnal_pre_ulink.failed");
								return null;
							}
						}else{
							premi = jurnal_pre_premi_individu(ldec_jumlah, ldt_tglrk, premi, li_cr_bayar, errors, currentUser);
						}
						
						if(premi!=null){
							String ls_pre = premi.getNo_pre();
							if(ls_pre!=null) {
								if(premi.getNo_pre().length()>10) ls_pre = premi.getNo_pre().substring(premi.getNo_pre().length()-10);
							}else {
								errors.reject("payment.jurnal_pre_ulink.failed");
								return null;
							}
							//this.uwDao.updateLst_rek_ekalife(premi);
							this.uwDao.updateMst_paymentJurnal(premi, ls_pre, ls_id, null, premi.getNo_jm_sa());
							this.uwDao.updateNoPreMstDrekDet(null, spaj, ls_id, ls_pre, premi.getNo_jm_sa());
							ld_ttp.add(ldec_jumlah);
							ld_ttp_kurs.add(premi.getKurs_bulanan());
							ls_voucher_ttp.add(premi.getVoucher());							
						}else{
							errors.reject("payment.jurnal_pre_ulink.failed");
							return null;
						}
					}else{
						errors.reject("payment.voucherPremiIndividu.failed");
						return null;
					}
				}else {
					ld_ttp.add(ldec_jumlah);
					ld_ttp_kurs.add(premi.getKurs_bulanan());
					ls_voucher_ttp.add(ls_vch);							
				}
			}else if(li_cr_bayar==10){ //nilai tunai
				ldec_bayar[1]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==11){ //tahapan

				//Yusuf (9 Nov 2009) - Atas request Annalisa (email oleh Himmia), validasi ini dinonaktifkan
//				int cek = uwDao.selectCekSpajBayar(spaj);
//				if(cek==0) {
//					errors.reject("payment.reg_spaj_bayar");
//					return null;
//				}
				
				ldec_bayar[2]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
				li_thp ++;
				//dikosongin jumlah tahapan di EKA.MST_PTC_JM untuk new business (YUSUF - 14/06/2006)
				//premi.setTahapan(ldec_jumlah);
				ld_thp.add(new Double(ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue()));
				ld_thp_kurs.add(premi.getKurs_bulanan());
				ls_polis_thp.add(ls_old_polis);
			}else if(li_cr_bayar==12){ //deposito
				ldec_bayar[3]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==13){ //pinjaman
				ldec_bayar[4]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==14){ //bunga tahapan
				ldec_bayar[5]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==15){ //bank bebas premi
				ldec_bayar[6]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==17){ //titipan premi agen
				ldec_bayar[7]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==19){ //maturity
				ldec_bayar[9]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==21){ //bunga tunggakan (non bank)
				ldec_bayar[10]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==99){ //komisi agen
				ldec_bayar[8]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==31){ //budget promosi
				ldec_bayar[11]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}
		}
		
		if(ldt_max_rk==null){
			errors.reject("payment.noRkDate");
			return null;
		}
		
		return jurnalMemorialPremiIndividu(premi, ldec_bayar, ls_voucher, ldt_max_rk, ldec_topup, errors, currentUser, ld_ttp, ld_ttp_kurs, ld_thp, ld_thp_kurs, ls_polis_thp, ls_voucher_ttp);  //ingat i nya beda 1 !!!!!!!!!!!!!!!!!!!!
	}
	
	public Premi jurnalProduksiIndividuKetinggalanNoPre(String spaj, Premi premi, BindException errors, User currentUser) throws Exception{
		if(logger.isDebugEnabled())logger.debug("PROSES: jurnalProduksiIndividu");
		
		if(spaj==null){
			errors.reject("payment.noSPAJ");
			return null;						
		}
		
		List lds_mst_prod_ins; List payment;
		int i;
		String ls_kurs_polis; String ls_id; String ls_kurs_bayar; String ls_voucher=""; String ls_no_voucher=""; String ls_no_pre=null; String ls_vch; 
		int li_row, li_thp=0;Integer li_jurnal; Integer li_cr_bayar; 
		Date ldt_tglrk; Date ldt_max_rk; Date ldt_prod;
		Double ldec_jumlah; Double ldec_topup=null;
		double ldec_bayar[] = {0,0,0,0,0,0,0,0,0,0,0,0};
		boolean lJurnal = false;
		
		List ld_ttp = new ArrayList();
		List ld_ttp_kurs = new ArrayList();
		List ls_voucher_ttp = new ArrayList();
		
		List ld_thp = new ArrayList();
		List ld_thp_kurs = new ArrayList();
		List ls_polis_thp = new ArrayList();		
		
		//double ld_ttp[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		//double ld_ttp_kurs[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		//String ls_voucher_ttp[] = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
		//double ld_thp[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		//double ld_thp_kurs[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		//String ls_polis_thp[] = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
		
		premi.setTahunke(new Integer(1));
		premi.setPremike(new Integer(1));
		premi.setPremi_ar(new Double(0));
		
		premi.setNo_pre(null);
		premi.setMri(false);
		premi.setNo_spaj(spaj);
		
		Map tmp = this.uwDao.selectCabangDanKursFromSpaj(spaj);
		
		if(tmp==null){
			errors.reject("payment.select.mst_policy.error");
			return null;			
		}else{
			premi.setK_region((String) tmp.get("LCA_ID"));
			ls_kurs_polis = (String) tmp.get("LKU_ID");
			try {ldt_prod = this.uwDao.selectProductionDate(spaj, premi.getTahunke(), premi.getPremike());} catch(Exception e) {
				errors.reject("payment.noProductionDate");
				return null;
			}
			premi.setNo_polis(this.uwDao.selectNoPolisFromSpaj(spaj));
			premi.setNo_polis(FormatString.nomorPolis(premi.getNo_polis()));
			
			if(premi.getNo_polis()==null){
				errors.reject("payment.noPolis");
				return null;			
			}
			lds_mst_prod_ins = this.uwDao.selectProductInsured(spaj, 1);
			if(lds_mst_prod_ins.isEmpty()){
				errors.reject("payment.noProductInsured");
				return null;			
			}
		}
		
		premi.setPremi_pa(new Double(0));
		premi.setPremi_dasar(new Double(0));
		premi.setPremi_disc(new Double(0));
		
		for(i=0; i<lds_mst_prod_ins.size(); i++){
			Map temp = (HashMap) lds_mst_prod_ins.get(i);
			if(((Integer) temp.get("LSBS_ID")).toString().equals("800")){
				premi.setPremi_pa(new Double(premi.getPremi_pa().doubleValue()+new Double(temp.get("MSPR_PREMIUM").toString()).doubleValue())); //premi pa
			}else{
				premi.setPremi_dasar(new Double(premi.getPremi_dasar().doubleValue()+new Double(temp.get("MSPR_PREMIUM").toString()).doubleValue())); //semua premi kecuali pa
			}
			premi.setPremi_disc(new Double(premi.getPremi_disc().doubleValue()+new Double(temp.get("MSPR_DISCOUNT").toString()).doubleValue())); //diskon premi
		}
		
		String param = "individu";
		String lsbs = uwDao.selectBusinessId(spaj);
		if(products.stableLink(lsbs)) {
			param = "ulink";
		}
//		}else if(products.unitLinkNew(lsbs)){
//			param = "ulink";
//		}
		
		try{
			li_row = this.uwDao.selectTagPaymentCount(spaj, 1, 1, param);
		}catch(Exception e) {
			errors.reject("payment.noTagPayment");
			return null;						
		}
		
		premi.setBiaya_polis(this.uwDao.selectBiayaPolis(spaj, 1, 1));
		if(premi.getBiaya_polis()==null){
			errors.reject("payment.noBilling");
			return null;									
		}
		
		ldt_max_rk = this.uwDao.selectMaxRkDate(spaj, 1, 1, param);
		if(ldt_max_rk==null){
			errors.reject("payment.rkFailed");
			return null;
		}
		
		payment = this.uwDao.selectDetailPayment(spaj, 1, 1, param);
		if(payment.isEmpty()){
			errors.reject("payment.noTagPayment");
			return null;
		}
		
		for(i=0; i<li_row; i++){
			Map m = (HashMap) payment.get(i);
			ls_vch = (String) m.get("MSPA_NO_VOUCHER");
			li_jurnal = (Integer) m.get("MSPA_JURNAL");
			ldec_jumlah = (Double) m.get("MSTP_VALUE");
			ldt_tglrk = (Date) m.get("MSPA_DATE_BOOK");
			ls_kurs_bayar = (String) m.get("LKU_ID");
			li_cr_bayar = (Integer) m.get("LSJB_ID");
			premi.setRek_id((Integer) m.get("LSREK_ID"));
			ls_no_pre = (String) m.get("MSPA_NO_PRE");
			if(ls_no_pre==null)ls_no_pre="";
			ls_id = (String) m.get("MSPA_PAYMENT_ID");
			String ls_old_polis = (String) m.get("MSPA_OLD_POLICY");
			
			if(li_jurnal==null)li_jurnal = new Integer(0);
			if(ls_vch==null) ls_vch="";
			if(li_jurnal==0) {
				if(ls_vch.trim().equals("") || ls_vch.trim().equals(",")) ls_vch = "";
				else if(ls_voucher.indexOf(ls_vch)<0) ls_voucher = ls_voucher + ", " + ls_vch;
			} else ls_voucher = ls_vch;

			if(premi.getNo_pre()==null) premi.setNo_pre(ls_no_pre);
			else if(premi.getNo_pre().trim().equals("") || premi.getNo_pre().trim().equals(",")) premi.setNo_pre(ls_no_pre);
			else if(!ls_no_pre.trim().equals(""))premi.setNo_pre(premi.getNo_pre()+", "+ls_no_pre);
			
			if(ls_old_polis==null) premi.setOld_polis("");
			else {
				ls_old_polis = FormatString.nomorPolis(ls_old_polis);
				if(premi.getOld_polis()==null) premi.setOld_polis(ls_old_polis);
				else if(premi.getOld_polis().trim().equals("")) premi.setOld_polis(ls_old_polis);
				else if(!premi.getOld_polis().contains(ls_old_polis)) premi.setOld_polis(premi.getOld_polis()+", "+ls_old_polis);
			}
			
			if(ls_kurs_bayar.equals("02")){
				premi.setKurs_bulanan(this.uwDao.selectMonthlyKurs("02", defaultDateFormat.format(ldt_tglrk).substring(6), defaultDateFormat.format(ldt_tglrk).substring(3,5)));
				if(premi.getKurs_bulanan()==null){
					errors.reject("payment.noMonthlyCurrency", 
							new Object[]{defaultDateFormat.format(ldt_tglrk).substring(6), defaultDateFormat.format(ldt_tglrk).substring(3,5)}, "Kurs tahun {0} bulan {1} tidak ada, harap hubungi CSF");
				}else if(premi.getKurs_bulanan().doubleValue()==0){
					errors.reject("payment.noMonthlyCurrency", 
							new Object[]{defaultDateFormat.format(ldt_tglrk).substring(6), defaultDateFormat.format(ldt_tglrk).substring(3,5)}, "Kurs tahun {0} bulan {1} tidak ada, harap hubungi CSF");
				}
			}else{
				premi.setKurs_bulanan(new Double(1));
			}
			
			if(ls_kurs_polis.equals("02")){
				Map a = this.uwDao.selectTodayKurs("02", ldt_tglrk);
				if(a==null){
					errors.reject("payment.noDailyCurrency");
					return null;
				}else{
					premi.setKurs_harian((Double) a.get("LKH_CURRENCY"));
				}
			}else{
				premi.setKurs_harian(new Double(1));
			}
			
			if((li_cr_bayar>=1 && li_cr_bayar<=9) || li_cr_bayar==18 || li_cr_bayar==20 || li_cr_bayar ==29){ //cash
				
				//Jurnal = gunakan kurs bulanan
				ldec_jumlah = new Double(ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue());
				
				ldec_bayar[0] += ldec_jumlah.doubleValue();
				if(li_cr_bayar==20) ldec_bayar[10] = ldec_jumlah.doubleValue();
				if(li_jurnal.doubleValue()!=1){
					premi = this.sequence.sequenceVoucherPremiIndividu(premi, ls_no_pre, ls_no_voucher, li_cr_bayar, errors); 
					if(premi!=null){
						ls_no_voucher = premi.getVoucher();
						if(ls_no_voucher==null) ls_no_voucher="";
						if(!ls_no_voucher.trim().equals("")) if(ls_voucher.indexOf(ls_no_voucher)<0) ls_voucher +=", "+ls_no_voucher;
						premi = jurnal_pre_premi_individu(ldec_jumlah, ldt_tglrk, premi, li_cr_bayar, errors, currentUser);
						if(premi!=null){
							String ls_pre = premi.getNo_pre();
							if(ls_pre!=null) {
								if(premi.getNo_pre().length()>10) ls_pre = premi.getNo_pre().substring(premi.getNo_pre().length()-10);
							}else {
								errors.reject("payment.jurnal_pre_ulink.failed");
								return null;
							}
							//this.uwDao.updateLst_rek_ekalife(premi);
							this.uwDao.updateMst_paymentJurnal(premi, ls_pre, ls_id, null, null);
							this.uwDao.updateNoPreMstDrekDet(null, spaj, ls_id, ls_pre,null);
							ld_ttp.add(ldec_jumlah);
							ld_ttp_kurs.add(premi.getKurs_bulanan());
							ls_voucher_ttp.add(premi.getVoucher());							
						}else{
							errors.reject("payment.jurnal_pre_ulink.failed");
							return null;
						}
					}else{
						errors.reject("payment.voucherPremiIndividu.failed");
						return null;
					}
				}else {
					ld_ttp.add(ldec_jumlah);
					ld_ttp_kurs.add(premi.getKurs_bulanan());
					ls_voucher_ttp.add(ls_vch);							
				}
			}else if(li_cr_bayar==10){ //nilai tunai
				ldec_bayar[1]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==11){ //tahapan

				//Yusuf (9 Nov 2009) - Atas request Annalisa (email oleh Himmia), validasi ini dinonaktifkan
//				int cek = uwDao.selectCekSpajBayar(spaj);
//				if(cek==0) {
//					errors.reject("payment.reg_spaj_bayar");
//					return null;
//				}
				
				ldec_bayar[2]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
				li_thp ++;
				//dikosongin jumlah tahapan di EKA.MST_PTC_JM untuk new business (YUSUF - 14/06/2006)
				//premi.setTahapan(ldec_jumlah);
				ld_thp.add(new Double(ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue()));
				ld_thp_kurs.add(premi.getKurs_bulanan());
				ls_polis_thp.add(ls_old_polis);
			}else if(li_cr_bayar==12){ //deposito
				ldec_bayar[3]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==13){ //pinjaman
				ldec_bayar[4]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==14){ //bunga tahapan
				ldec_bayar[5]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==15){ //bank bebas premi
				ldec_bayar[6]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==17){ //titipan premi agen
				ldec_bayar[7]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==19){ //titipan premi agen
				ldec_bayar[9]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==21){ //bunga tunggakan (non bank)
				ldec_bayar[10]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==99){ //komisi agen
				ldec_bayar[8]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}
		}
		
		if(ldt_max_rk==null){
			errors.reject("payment.noRkDate");
			return null;
		}
		
		return jurnalMemorialPremiIndividuKetinggalanNoPre(premi, ldec_bayar, ls_voucher, ldt_max_rk, ldec_topup, errors, currentUser, ld_ttp, ld_ttp_kurs, ld_thp, ld_thp_kurs, ls_polis_thp, ls_voucher_ttp);  //ingat i nya beda 1 !!!!!!!!!!!!!!!!!!!!
	}
	

	public Premi jurnalProduksiUnitLink(String spaj, Premi premi, BindException errors, User currentUser, int ar_topup) throws Exception{
		if(logger.isDebugEnabled())logger.debug("PROSES: jurnalProduksiUnitLink");
	
		if(spaj==null){
			errors.reject("payment.noSPAJ");
			return null;						
		}
		
		List lds_mst_prod_ins; List payment;
		int i;
		String ls_kurs_polis; String ls_id; String ls_kurs_bayar; String ls_voucher=""; String ls_no_voucher=""; String ls_no_pre=null; String ls_vch; 
		int li_row; Integer li_jurnal; Integer li_cr_bayar;
		Date ldt_tglrk; Date ldt_max_rk;
		Double ldec_jumlah; Double ldec_topup= new Double(0);
		double ldec_bayar[] = {0,0,0,0,0,0,0,0,0,0,0,0}; 
		
		List ld_ttp = new ArrayList();
		List ld_ttp_kurs = new ArrayList();
		List ls_voucher_ttp = new ArrayList();
		List ld_ttp_flagTp = new ArrayList();
		
		premi.setTahunke(new Integer(1));
		premi.setPremike(new Integer(1));
		premi.setPremi_ar(new Double(0));
		
		premi.setNo_pre(null);
		premi.setMri(false);
		premi.setNo_spaj(spaj);
		
		Map tmp = this.uwDao.selectCabangDanKursFromSpaj(spaj);
		Date ldt_prod = this.uwDao.selectProductionDate(spaj, premi.getTahunke(), premi.getPremike());
		
		if(tmp==null){
			errors.reject("payment.select.mst_policy.error");
			return null;			
		}else{
			premi.setK_region((String) tmp.get("LCA_ID"));
			ls_kurs_polis = (String) tmp.get("LKU_ID");
			premi.setNo_polis(this.uwDao.selectNoPolisFromSpaj(spaj));
			premi.setNo_polis(FormatString.nomorPolis(premi.getNo_polis()));
			if(premi.getNo_polis()==null){
				errors.reject("payment.noPolis");
				return null;			
			}
			lds_mst_prod_ins = this.uwDao.selectProductInsured(spaj, 1);
			if(lds_mst_prod_ins.isEmpty()){
				errors.reject("payment.noProductInsured");
				return null;
			}
		}
		
		premi.setPremi_pa(new Double(0));
		premi.setPremi_dasar(new Double(0));
		premi.setPremi_disc(new Double(0));
		
		for(i=0; i<lds_mst_prod_ins.size(); i++){
			Map temp = (HashMap) lds_mst_prod_ins.get(i);
			if(((Integer) temp.get("LSBS_ID")).toString().equals("800")){
				premi.setPremi_pa(new Double(premi.getPremi_pa().doubleValue()+new Double(temp.get("MSPR_PREMIUM").toString()).doubleValue())); //premi pa
			}else{
				premi.setPremi_dasar(new Double(premi.getPremi_dasar().doubleValue()+new Double(temp.get("MSPR_PREMIUM").toString()).doubleValue())); //semua premi kecuali pa
			}
			premi.setPremi_disc(new Double(premi.getPremi_disc().doubleValue()+new Double(temp.get("MSPR_DISCOUNT").toString()).doubleValue()));
		}
		
		//select jumlah pembayaran dari tag payment
		li_row = this.uwDao.selectTagPaymentCount(spaj, 1, 2, "ulink");
		if(products.stableLink(uwDao.selectBusinessId(spaj))){
			li_row = this.uwDao.selectTagPaymentCountStableLink(spaj);
		}
		if(li_row==0){
			errors.reject("payment.noTagPayment");
			return null;						
		}
		
		premi.setBiaya_polis(this.uwDao.selectBiayaPolis(spaj, 1, 1));
		if(premi.getBiaya_polis()==null){
			errors.reject("payment.noBilling");
			return null;									
		}
		
		ldt_max_rk = this.uwDao.selectMaxRkDate(spaj, 1, 2, "ulink");
		if(ldt_max_rk==null){
			errors.reject("payment.rkFailed");
			return null;
		}
		
		//select detail pembayaran masing2 (jumlahnya harus sama dengan selectTagPaymentCount diatas)
		payment = this.uwDao.selectDetailPayment(spaj, 1, 2, "ulink");
		if(payment.isEmpty()){
			errors.reject("payment.noTagPayment");
			return null;			
		}
		
		//untuk setiap row pembayaran, lakukan hal dibawah
		for(i=0; i<li_row; i++){
			Map m = (HashMap) payment.get(i);
			ls_vch = (String) m.get("MSPA_NO_VOUCHER");
			if(ls_vch==null) ls_vch="";
			li_jurnal = (Integer) m.get("MSPA_JURNAL");
			ldec_jumlah = (Double) m.get("MSTP_VALUE");
			ldt_tglrk = (Date) m.get("MSPA_DATE_BOOK");
			ls_kurs_bayar = (String) m.get("LKU_ID");
			li_cr_bayar = (Integer) m.get("LSJB_ID");
			premi.setRek_id((Integer) m.get("LSREK_ID"));
			ls_no_pre = (String) m.get("MSPA_NO_PRE");
			if(ls_no_pre == null) ls_no_pre="";
			ls_id = (String) m.get("MSPA_PAYMENT_ID");
			premi.setMspa_payment_id(ls_id);
			premi.setOld_polis((String) m.get("MSPA_OLD_POLICY"));
			Integer msbi_flag_topup = (Integer) m.get("MSBI_FLAG_TOPUP");
			premi.setNo_jm_sa((String) m.get("MSPA_NO_JM_SA"));

			if(li_jurnal==null)li_jurnal = new Integer(0);
			if(ls_vch==null) ls_vch="";
			if(li_jurnal==0) {
				if(ls_vch.trim().equals("") || ls_vch.trim().equals(",")) ls_vch = "";
				else if(ls_voucher.indexOf(ls_vch)<0) ls_voucher = ls_voucher + "," + ls_vch;
			} else ls_voucher = ls_vch;
			
			if(premi.getNo_pre()==null) premi.setNo_pre(ls_no_pre);
			else if(!ls_no_pre.trim().equals(""))premi.setNo_pre(premi.getNo_pre()+", "+ls_no_pre);
			if(ls_kurs_bayar.equals("02")){
				premi.setKurs_bulanan(this.uwDao.selectMonthlyKurs("02", defaultDateFormat.format(ldt_tglrk).substring(6), defaultDateFormat.format(ldt_tglrk).substring(3,5)));
				if(premi.getKurs_bulanan()==null){
					errors.reject("payment.noMonthlyCurrency", 
							new Object[]{defaultDateFormat.format(ldt_tglrk).substring(6), defaultDateFormat.format(ldt_tglrk).substring(3,5)}, "Kurs tahun {0} bulan {1} tidak ada, harap hubungi CSF");
				}else if(premi.getKurs_bulanan().doubleValue()==0){
					errors.reject("payment.noMonthlyCurrency", 
							new Object[]{defaultDateFormat.format(ldt_tglrk).substring(6), defaultDateFormat.format(ldt_tglrk).substring(3,5)}, "Kurs tahun {0} bulan {1} tidak ada, harap hubungi CSF");
				}
			}else{
				premi.setKurs_bulanan(new Double(1));
			}
			
			if(ls_kurs_polis.equals("02")){
				Map a = this.uwDao.selectTodayKurs("02", ldt_tglrk);
				if(a==null){
					errors.reject("payment.noDailyCurrency");
					return null;
				}else{
					premi.setKurs_harian((Double) a.get("LKH_CURRENCY"));
				}
			}else{
				premi.setKurs_harian(new Double(1));
			}
			
			//Cek eka.mst_drek_det
			List<DrekDet> mstDrekDetBasedSpaj = uwDao.selectMstDrekDet(null, spaj, ls_id, null);
			if(mstDrekDetBasedSpaj.isEmpty() && premi.getRek_id()!=0){
				errors.reject("payment.noIbank");
				return null;
			}
			
			if((li_cr_bayar>=1 && li_cr_bayar<=9) || li_cr_bayar==18 || li_cr_bayar==29){ //cash
				
				//Yusuf - 21 Jul 09 - Request Mario via email
				//TU, gunakan kurs harian (untuk yg di JM, untuk yg di pre awal tetap kurs bulanan)
				//Yusuf - 2 Mar 12 - Request Tri Lestari via email
				//TU, gunakan kurs bulanan juga
				if(msbi_flag_topup >= 1) {
					//ldec_topup = new Double(ldec_jumlah.doubleValue() * premi.getKurs_harian());
					ldec_topup = new Double( ldec_topup + ( ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue() ) );
				}

				ldec_jumlah = new Double(ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue());
				
				ldec_bayar[0]+= ldec_jumlah.doubleValue();
				if(li_jurnal.doubleValue()!=1){
					premi = this.sequence.sequenceVoucherPremiIndividu(premi, ls_no_pre, ls_no_voucher, li_cr_bayar, errors);
					if(premi!=null){
						ls_no_voucher = premi.getVoucher();
						if(ls_no_voucher==null) ls_no_voucher="";
						if(!ls_no_voucher.trim().equals("")) if(ls_voucher.indexOf(ls_no_voucher)<0) ls_voucher +=","+ls_no_voucher;
						//Deddy (17 Feb 2015) - Jurnal TTD : Apabila no_pre dari mst_drek exists, pakai jurnal baru (Jurnal TTD).Apabila tidak ada, pakai jurnal bank yg lama.
						if(!StringUtil.isEmpty(ls_no_pre)){	
							premi = jurnal_pre_ulink_jm(ldec_jumlah, ldt_tglrk, premi, li_cr_bayar, ls_no_pre, errors, currentUser);
							if(StringUtil.isEmpty(premi.getProject()[0])){
								errors.reject("payment.jurnal_pre_ulink.failed");
								return null;
							}
						}else{
							premi = jurnal_pre_ulink(ldec_jumlah, ldt_tglrk, premi, li_cr_bayar, errors, currentUser);
						}
						
						if(premi!=null){
							String ls_pre = premi.getNo_pre().substring(premi.getNo_pre().length()-9);
							//this.uwDao.updateLst_rek_ekalife(premi);
							this.uwDao.updateMst_paymentJurnal(premi, ls_pre, ls_id, null, premi.getNo_jm_sa());
							this.uwDao.updateNoPreMstDrekDet(null, spaj, ls_id, ls_pre, premi.getNo_jm_sa());
							ld_ttp.add(ldec_jumlah);
							ld_ttp_kurs.add(premi.getKurs_bulanan());
							ls_voucher_ttp.add(premi.getVoucher());
							ld_ttp_flagTp.add(msbi_flag_topup);
						}else{
							errors.reject("payment.jurnal_pre_ulink.failed");
							return null;
						}
					}else{
						errors.reject("payment.voucherPremiIndividu.failed");
						return null;
					}
				}else {
					ld_ttp.add(ldec_jumlah);
					ld_ttp_kurs.add(premi.getKurs_bulanan());
					ls_voucher_ttp.add(ls_vch);	
					ld_ttp_flagTp.add(msbi_flag_topup);
				}
			}else if(li_cr_bayar==10){ //nilai tunai
				if(msbi_flag_topup >= 1) {
					ldec_topup = new Double( ldec_topup + ( ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue() ) );
				}
				ld_ttp.add(ldec_jumlah);
				ld_ttp_kurs.add(premi.getKurs_bulanan());
				ls_voucher_ttp.add(ls_vch);	
				ld_ttp_flagTp.add(msbi_flag_topup);
				ldec_bayar[1]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==11){ //tahapan
				ldec_bayar[2]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==12){ //deposito
				ldec_bayar[3]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==13){ //pinjaman
				ldec_bayar[4]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==14){ //bunga tahapan
				ldec_bayar[5]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==15){ //bank bebas premi
				ldec_bayar[6]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==17){ //titipan premi agen
				ldec_bayar[7]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==19){ //maturity
				ldec_bayar[9]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==22){ //potongan gaji
				ldec_bayar[10]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==99){ //komisi agen
				ldec_bayar[8]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}else if(li_cr_bayar==31){ //budget promosi
				ldec_bayar[11]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
			}
		}
		
		if(ldt_max_rk==null){
			errors.reject("payment.noRkDate");
			return null;
		}
		
		return jurnalMemorialPremiUlink(premi, ldec_bayar, ls_voucher, ldt_max_rk, ldec_topup, errors, currentUser, ld_ttp, ld_ttp_kurs, ls_voucher_ttp,ld_ttp_flagTp);  //ingat i nya beda 1 !!!!!!!!!!!!!!!!!!!!
		
	}

	private Date hitungTanggalProduksiBaru(Date tglRk) throws Exception{
		if(logger.isDebugEnabled())logger.debug("PROSES: hitungTanggalProduksiBaru");
	
		Date ldt_prod_for; Date ldt_tgl_input; Date ldt_close;
		ldt_prod_for = this.uwDao.selectMst_default(1);
		ldt_tgl_input = this.commonDao.selectSysdateTruncated(0);
		if(FormatDate.dateDifference(tglRk, ldt_prod_for, false)>0){
			if(FormatDate.dateDifference(ldt_tgl_input, ldt_prod_for, false)<0){
				ldt_close = this.uwDao.selectMst_default(2);
				if(FormatDate.dateDifference(ldt_tgl_input, ldt_close, false)<0){
					ldt_prod_for=null;
				}
				return ldt_prod_for;
			}
		}else{
			return ldt_prod_for;
		}
		
		ldt_prod_for = ldt_tgl_input;
		
		return ldt_prod_for;
	}
	
//	private int prosesJurnaPencatatanKomisiPertama(ArrayList listComForJm, Premi premi,String lca_id, 
//			String lwk_id, int li_run, boolean isSyariah, boolean isStableLink, String ll_no_jm) {
//		String namaAgen = new String(); 
//		String noAgen = new String();
//		String[] ls_accno = new String[2];
//		Double ldec_total=0.;
//		HashMap mapComm = new HashMap();
//		int lscb = uwDao.selectCaraBayarFromSpaj(premi.getNo_spaj());
//		int jenis_acc_komisi =3;
//		if(lscb==0 && !lca_id.equals("09")) {
//			jenis_acc_komisi = 1;
//		}else if(lscb!=0 && !lca_id.equals("09")){
//			jenis_acc_komisi = 3;
//		}else if(lca_id.equals("09")){
//			jenis_acc_komisi = 9;
//		}
//		if(lca_id.equals("40") && lwk_id.equals("02")){//khusus komisi disetahunkan
//			for(int i=0; i<listComForJm.size(); i++){
//					mapComm= (HashMap)listComForJm.get(i);
					
//					//Komisi Setahun
//					//insert sisi debet Komisi Setahun
//					li_run++;
//					ls_accno[0] = (isSyariah ? "801" : ("0"+premi.getK_region()));
//					ls_accno[1] = bacDao.selectGetAccKomisi(premi.getNo_spaj(),0, jenis_acc_komisi);//komisi premi pertama tahun pertama untuk perorangan
//					namaAgen = (String)mapComm.get("NAMA_AGENT");
//					noAgen = (String)mapComm.get("MSAG_ID");
//					ldec_total = ((BigDecimal) mapComm.get("KOMISI_DPP")).doubleValue()*premi.getKurs_bulanan();
//					this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "KOMISI " + premi.getNo_polis()+" "+premi.getNama_pemegang()+" "+noAgen+" "+namaAgen, ldec_total, ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());	
//					
//					//insert sisi kredit Komisi Setahun
//					li_run++;
//					ls_accno[1] = "431";
//					this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "UTANG KOMISI  " + premi.getNo_polis()+" "+premi.getNama_pemegang()+" "+noAgen+""+namaAgen, ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
//					
//					//Reklas Komisi
//					//insert sisi debet reklas komisi
//					li_run++;
//					ls_accno[1] = "1717";
//					this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "KOMISI DIBAYAR DIMUKA" + premi.getNo_polis()+" "+premi.getNama_pemegang()+" "+noAgen+" "+namaAgen, ldec_total, ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
//					
					//insert sisi kredit reklas komisi
//					li_run++;
//					ls_accno[1] = bacDao.selectGetAccKomisi(premi.getNo_spaj(), 0,jenis_acc_komisi);
//					this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "KOMISI " + premi.getNo_polis()+" "+premi.getNama_pemegang()+" "+noAgen+" "+namaAgen, ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
//					
					//Komisi Actual
					//insert sisi debet Komisi Actual
//					li_run++;
//					ls_accno[1] = bacDao.selectGetAccKomisi(premi.getNo_spaj(),0, jenis_acc_komisi);
//					ldec_total = ((BigDecimal) mapComm.get("KOMISI_ACTUAL")).doubleValue()*premi.getKurs_bulanan();
//					this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "KOMISI " + premi.getNo_polis()+" "+premi.getNama_pemegang()+" "+noAgen+" "+namaAgen, ldec_total, ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
//					
//					//insert sisi kredit Komisi Actual
//					li_run++;
//					ls_accno[1] = "1717";
//					ldec_total = ((BigDecimal) mapComm.get("KOMISI_ACTUAL")).doubleValue()*premi.getKurs_bulanan();
//					this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "KOMISI " + premi.getNo_polis()+" "+premi.getNama_pemegang()+" "+noAgen+" "+namaAgen, ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
//			}
//		}else{				
//			for(int i=0; i<listComForJm.size(); i++){
//				mapComm= (HashMap)listComForJm.get(i);
//				
//				//insert sisi debet
//				li_run++;
//				ls_accno[0] = (isSyariah ? "801" : ("0"+premi.getK_region()));
//				ls_accno[1] = bacDao.selectGetAccKomisi(premi.getNo_spaj(),0, jenis_acc_komisi);
//				namaAgen = (String)mapComm.get("NAMA_AGENT");
//				noAgen = (String)mapComm.get("MSAG_ID");
//				ldec_total = ((BigDecimal) mapComm.get("KOMISI_DPP")).doubleValue()*premi.getKurs_bulanan();
//				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "KOMISI " + premi.getNo_polis()+" "+premi.getNama_pemegang()+" "+noAgen+" "+namaAgen, ldec_total, ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());				
//				
//				//insert sisi kredit
//				li_run++;
//				ls_accno[1] = "431";
//				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "UTANG KOMISI " + premi.getNo_polis()+" "+premi.getNama_pemegang()+" "+noAgen+" "+namaAgen, ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
//			}
//			
//		}			
//		return li_run;
//	}
	
//	private int prosesJurnalPencatatanBonusKomisi(ArrayList listComBonusForJm, Premi premi, String lca_id, String lwk_id, int li_run,
//			boolean isSyariah, boolean isStableLink, String ll_no_jm) {
//		
//		
//		String namaAgen = new String(); 
//		String noAgen = new String();
//		String[] ls_accno = new String[2];
//		Double ldec_total=0.;
//		HashMap mapComm = new HashMap();
//		for(int i=0; i<listComBonusForJm.size(); i++){
//			li_run++;
//			mapComm= (HashMap)listComBonusForJm.get(i);
//			ls_accno[0] = (isSyariah ? "801" : ("0"+premi.getK_region()));
//			ls_accno[1] = bacDao.selectGetAccKomisi(premi.getNo_spaj(),0, 12);
//			namaAgen = (String)mapComm.get("NAMA_AGENT");
//			noAgen = (String)mapComm.get("KODE_AGENT");
//			ldec_total = ((BigDecimal) mapComm.get("BONUS")).doubleValue()*premi.getKurs_bulanan();
//			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "BONUS KOMISI " + premi.getNo_polis()+" "+premi.getNama_pemegang()+" "+noAgen+" "+namaAgen, ldec_total, ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
//			
//			li_run++;
//			ls_accno[1] = "431";
//			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "UTANG KOMISI " + premi.getNo_polis()+" "+premi.getNama_pemegang()+" "+noAgen+" "+namaAgen, ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
//		
//		
////		}
//		return li_run;
//	}
//	
//	private int prosesJurnalPencatatanReward(ArrayList listComRewardForJm,Premi premi, String lca_id, String lwk_id, int li_run,
//			boolean isSyariah, boolean isStableLink, String ll_no_jm) {
//		
//		
//		String namaAgen = new String(); 
//		String noAgen = new String();
//		String[] ls_accno = new String[2];
//		Double ldec_total=0.;
//		HashMap mapComm = new HashMap();
		
//		for(int i=0; i<listComRewardForJm.size(); i++){
//			li_run++;
//			mapComm= (HashMap)listComRewardForJm.get(i);
//			ls_accno[0] = (isSyariah ? "801" : ("0"+premi.getK_region()));
//			ls_accno[1] = bacDao.selectGetAccKomisi(premi.getNo_spaj(),0, 10);
//			namaAgen = (String)mapComm.get("NAMA_AGENT");
//			noAgen = (String)mapComm.get("KODE_AGENT");
//			ldec_total = ((BigDecimal) mapComm.get("REWARD")).doubleValue()*premi.getKurs_bulanan();
//			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "REWARD KOMISI " + premi.getNo_polis()+" "+premi.getNama_pemegang()+" "+noAgen+" "+namaAgen, ldec_total, ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
//			
//			li_run++;
//			ls_accno[1] = "431";
//			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "UTANG KOMISI " + premi.getNo_polis()+" "+premi.getNama_pemegang()+" "+noAgen+" "+namaAgen, ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
//		
//		
//		}
//		return li_run;
//	}
	
	public Premi jurnalProduksiUnitLinkERBEPackage(String spaj, Premi premi, BindException errors, User currentUser, int ar_topup) throws Exception{

		if(logger.isDebugEnabled())logger.debug("PROSES: jurnalProduksiUnitLinkERBEPackage");
	
		if(spaj==null){
			errors.reject("payment.noSPAJ");
			return null;						
		}
		
		List lds_mst_prod_ins; List billingErbe;
		int i;
		String ls_kurs_polis; 
//		String ls_id; 
		String ls_kurs_bayar;
		String ls_voucher=""; 
		String ls_no_voucher=""; 
//		String ls_no_pre=null; 
//		String ls_vch; 
		int li_row; 
//		Integer li_jurnal; 
		Integer li_cr_bayar;
		Date ldt_tglrk; 
		Date ldt_max_rk;
		Double ldec_jumlah; Double ldec_topup= new Double(0);
		double ldec_bayar[] = {0,0,0,0,0,0,0,0,0,0,0,0}; 
		
		List ld_ttp = new ArrayList();
		List ld_ttp_kurs = new ArrayList();
		List ls_voucher_ttp = new ArrayList();
		List ld_ttp_flagTp = new ArrayList();
		
		premi.setTahunke(new Integer(1));
		premi.setPremike(new Integer(1));
		premi.setPremi_ar(new Double(0));
		
		premi.setNo_pre(null);
		premi.setMri(false);
		premi.setNo_spaj(spaj);
		
		Map tmp = this.uwDao.selectCabangDanKursFromSpaj(spaj);
		Date ldt_prod = this.uwDao.selectProductionDate(spaj, premi.getTahunke(), premi.getPremike());
		
		if(tmp==null){
			errors.reject("payment.select.mst_policy.error");
			return null;			
		}else{
			premi.setK_region((String) tmp.get("LCA_ID"));
			ls_kurs_polis = (String) tmp.get("LKU_ID");
			premi.setNo_polis(this.uwDao.selectNoPolisFromSpaj(spaj));
			premi.setNo_polis(FormatString.nomorPolis(premi.getNo_polis()));
			if(premi.getNo_polis()==null){
				errors.reject("payment.noPolis");
				return null;			
			}
			lds_mst_prod_ins = this.uwDao.selectProductInsured(spaj, 1);
			if(lds_mst_prod_ins.isEmpty()){
				errors.reject("payment.noProductInsured");
				return null;
			}
		}
		
		premi.setPremi_pa(new Double(0));
		premi.setPremi_dasar(new Double(0));
		premi.setPremi_disc(new Double(0));
		
		for(i=0; i<lds_mst_prod_ins.size(); i++){
			Map temp = (HashMap) lds_mst_prod_ins.get(i);
			if(((Integer) temp.get("LSBS_ID")).toString().equals("800")){
				premi.setPremi_pa(new Double(premi.getPremi_pa().doubleValue()+new Double(temp.get("MSPR_PREMIUM").toString()).doubleValue())); //premi pa
			}else{
				premi.setPremi_dasar(new Double(premi.getPremi_dasar().doubleValue()+new Double(temp.get("MSPR_PREMIUM").toString()).doubleValue())); //semua premi kecuali pa
			}
			premi.setPremi_disc(new Double(premi.getPremi_disc().doubleValue()+new Double(temp.get("MSPR_DISCOUNT").toString()).doubleValue()));
		}
		
		//select jumlah Billing 
		li_row = this.bacDao.selectBillingCount(spaj, 1);

		if(li_row==0){
			errors.reject("billing.noBillCount");
			return null;						
		}
		
		premi.setBiaya_polis(this.uwDao.selectBiayaPolis(spaj, 1, 1));
		if(premi.getBiaya_polis()==null){
			errors.reject("payment.noBillbiaya");
			return null;									
		}
		
		
		
		billingErbe = this.bacDao.selectDetailBilling(spaj, 1, 2);
		if(billingErbe.isEmpty()){
			errors.reject("billing.noBill");
			return null;			
		}
		
		//untuk setiap row pembayaran, lakukan hal dibawah
		for(i=0; i<li_row; i++){
			Map m = (HashMap) billingErbe.get(i);
//			ls_vch = (String) m.get("MSPA_NO_VOUCHER");
//			if(ls_vch==null) ls_vch="";
//			li_jurnal = (Integer) m.get("MSPA_JURNAL");
			ldec_jumlah = ((BigDecimal)m.get("MSDB_PREMIUM")).doubleValue();
			ldt_tglrk = (Date) m.get("TGL_PRODUCTION"); //tgl produktion
			ls_kurs_bayar = (String) m.get("LKU_ID");
//			li_cr_bayar = (Integer) m.get("LSJB_ID");
//			premi.setRek_id((Integer) m.get("LSREK_ID"));
//			ls_no_pre = (String) m.get("MSPA_NO_PRE");
//			if(ls_no_pre == null) ls_no_pre="";
//			ls_id = (String) m.get("MSPA_PAYMENT_ID");
//			premi.setMspa_payment_id(ls_id);
//			premi.setOld_polis((String) m.get("MSPA_OLD_POLICY"));
			Integer msbi_flag_topup = ((BigDecimal)m.get("MSBI_FLAG_TOPUP")).intValue(); 
//			premi.setNo_jm_sa((String) m.get("MSPA_NO_JM_SA"));

//			if(li_jurnal==null)li_jurnal = new Integer(0);
//			if(ls_vch==null) ls_vch="";
//			if(li_jurnal==0) {
//				if(ls_vch.trim().equals("") || ls_vch.trim().equals(",")) ls_vch = "";
//				else if(ls_voucher.indexOf(ls_vch)<0) ls_voucher = ls_voucher + "," + ls_vch;
//			} else ls_voucher = ls_vch;
			
//			if(premi.getNo_pre()==null) premi.setNo_pre(ls_no_pre);
//			else if(!ls_no_pre.trim().equals(""))premi.setNo_pre(premi.getNo_pre()+", "+ls_no_pre);
			if(ls_kurs_bayar.equals("02")){
				premi.setKurs_bulanan(this.uwDao.selectMonthlyKurs("02", defaultDateFormat.format(ldt_tglrk).substring(6), defaultDateFormat.format(ldt_tglrk).substring(3,5)));
				if(premi.getKurs_bulanan()==null){
					errors.reject("payment.noMonthlyCurrency", 
							new Object[]{defaultDateFormat.format(ldt_tglrk).substring(6), defaultDateFormat.format(ldt_tglrk).substring(3,5)}, "Kurs tahun {0} bulan {1} tidak ada, harap hubungi CSF");
				}else if(premi.getKurs_bulanan().doubleValue()==0){
					errors.reject("payment.noMonthlyCurrency", 
							new Object[]{defaultDateFormat.format(ldt_tglrk).substring(6), defaultDateFormat.format(ldt_tglrk).substring(3,5)}, "Kurs tahun {0} bulan {1} tidak ada, harap hubungi CSF");
				}
			}else{
				premi.setKurs_bulanan(new Double(1));
			}
			
			if(ls_kurs_polis.equals("02")){
				Map a = this.uwDao.selectTodayKurs("02", ldt_tglrk);
				if(a==null){
					errors.reject("payment.noDailyCurrency");
					return null;
				}else{
					premi.setKurs_harian((Double) a.get("LKH_CURRENCY"));
				}
			}else{
				premi.setKurs_harian(new Double(1));
			}
			
			//Cek eka.mst_drek_det
//			List<DrekDet> mstDrekDetBasedSpaj = uwDao.selectMstDrekDet(null, spaj, ls_id, null);
//			if(mstDrekDetBasedSpaj.isEmpty() && premi.getRek_id()!=0){
//				errors.reject("payment.noIbank");
//				return null;
//			}
			
//			if((li_cr_bayar>=1 && li_cr_bayar<=9) || li_cr_bayar==18 || li_cr_bayar==29){ //cash
				
				//Yusuf - 21 Jul 09 - Request Mario via email
				//TU, gunakan kurs harian (untuk yg di JM, untuk yg di pre awal tetap kurs bulanan)
				//Yusuf - 2 Mar 12 - Request Tri Lestari via email
				//TU, gunakan kurs bulanan juga
				if(msbi_flag_topup >= 1) {
					//ldec_topup = new Double(ldec_jumlah.doubleValue() * premi.getKurs_harian());
					ldec_topup = new Double( ldec_topup + ( ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue() ) );
				}

				ldec_jumlah = new Double(ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue());
				
				ldec_bayar[0]+= ldec_jumlah.doubleValue();
//				if(li_jurnal.doubleValue()!=1){
//					premi = this.sequence.sequenceVoucherPremiIndividu(premi, ls_no_pre, ls_no_voucher, li_cr_bayar, errors);
//					if(premi!=null){
//						ls_no_voucher = premi.getVoucher();
//						if(ls_no_voucher==null) ls_no_voucher="";
//						if(!ls_no_voucher.trim().equals("")) if(ls_voucher.indexOf(ls_no_voucher)<0) ls_voucher +=","+ls_no_voucher;
						//Deddy (17 Feb 2015) - Jurnal TTD : Apabila no_pre dari mst_drek exists, pakai jurnal baru (Jurnal TTD).Apabila tidak ada, pakai jurnal bank yg lama.
//						if(!StringUtil.isEmpty(ls_no_pre)){	
//							premi = jurnal_pre_ulink_jm(ldec_jumlah, ldt_tglrk, premi, li_cr_bayar, ls_no_pre, errors, currentUser);
//							if(StringUtil.isEmpty(premi.getProject()[0])){
//								errors.reject("payment.jurnal_pre_ulink.failed");
//								return null;
//							}
//						}else{
//							premi = jurnal_pre_ulink(ldec_jumlah, ldt_tglrk, premi, li_cr_bayar, errors, currentUser);
//						}
						
//						if(premi!=null){
//							String ls_pre = premi.getNo_pre().substring(premi.getNo_pre().length()-9);
//							this.uwDao.updateLst_rek_ekalife(premi);
//							this.uwDao.updateMst_paymentJurnal(premi, ls_pre, ls_id, null, premi.getNo_jm_sa());
//							this.uwDao.updateNoPreMstDrekDet(null, spaj, ls_id, ls_pre, premi.getNo_jm_sa());
//							ld_ttp.add(ldec_jumlah);
//							ld_ttp_kurs.add(premi.getKurs_bulanan());
//							ls_voucher_ttp.add(premi.getVoucher());
//							ld_ttp_flagTp.add(msbi_flag_topup);
//						}else{
//							errors.reject("payment.jurnal_pre_ulink.failed");
//							return null;
//						}
//					}else{
//						errors.reject("payment.voucherPremiIndividu.failed");
//						return null;
//					}
//				}else {
					ld_ttp.add(ldec_jumlah);
					ld_ttp_kurs.add(premi.getKurs_bulanan());
//					ls_voucher_ttp.add(ls_vch);	
					ld_ttp_flagTp.add(msbi_flag_topup);
//				}
//			}else if(li_cr_bayar==10){ //nilai tunai
//				if(msbi_flag_topup >= 1) {
//					ldec_topup = new Double( ldec_topup + ( ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue() ) );
//				}
//				ld_ttp.add(ldec_jumlah);
//				ld_ttp_kurs.add(premi.getKurs_bulanan());
//				ls_voucher_ttp.add(ls_vch);	
//				ld_ttp_flagTp.add(msbi_flag_topup);
//				ldec_bayar[1]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
//			}
//			else if(li_cr_bayar==11){ //tahapan
//				ldec_bayar[2]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
//			}else if(li_cr_bayar==12){ //deposito
//				ldec_bayar[3]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
//			}else if(li_cr_bayar==13){ //pinjaman
//				ldec_bayar[4]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
//			}else if(li_cr_bayar==14){ //bunga tahapan
//				ldec_bayar[5]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
//			}else if(li_cr_bayar==15){ //bank bebas premi
//				ldec_bayar[6]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
//			}else if(li_cr_bayar==17){ //titipan premi agen
//				ldec_bayar[7]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
//			}else if(li_cr_bayar==19){ //maturity
//				ldec_bayar[9]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
//			}else if(li_cr_bayar==22){ //potongan gaji
//				ldec_bayar[10]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
//			}else if(li_cr_bayar==99){ //komisi agen
//				ldec_bayar[8]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
//			}else if(li_cr_bayar==31){ //budget promosi
//				ldec_bayar[11]+= ldec_jumlah.doubleValue() * premi.getKurs_bulanan().doubleValue();
//			}
		}
		
		
		ldt_max_rk = this.commonDao.selectSysdateTruncated(0);
		
		
		return jurnalMemorialPremiUlinkERBEPackage(premi, ldec_bayar, ls_voucher, ldt_max_rk, ldec_topup, errors, currentUser, ld_ttp, ld_ttp_kurs, ls_voucher_ttp,ld_ttp_flagTp);  //ingat i nya beda 1 !!!!!!!!!!!!!!!!!!!!
	}
	
	//(premi, ldec_bayar, ls_voucher, ldt_max_rk, ldec_topup, errors, currentUser, ld_ttp, ld_ttp_kurs, ls_voucher_ttp);
	public Premi jurnalMemorialPremiUlinkERBEPackage(Premi premi, double[] ar_ldec_bayar, String ar_ls_no_voucher, Date ar_max_rk, Double ar_topup, BindException errors, User currentUser,
		List ar_ttp, List ar_ttp_kurs, List ar_ttp_voucher, List ld_ttp_flagTp) throws Exception{

		if(logger.isDebugEnabled())logger.debug("PROSES: jurnalMemorialPremiUlink");
		Date ldec_beg_date_bill ;
		String[] ls_accno = new String[2]; String ls_flag; 
		String[] ls_ket = new String[20]; 
		String ls_region; 
		//Integer li_pmode;
		int li_run=0; int z; Integer li_pos = new Integer(1); Integer li_cbid;
		String ll_no_jm; Long li_bisnis; String ls_grp; 
		Date ldt_prod; 
		Double ldec_tot_premi; Double ldec_tot_bayar=new Double(0); Double ldec_selisih; Double ldec_total; 
		Double BiayaC = 0.0;Double BiayaD = 0.0;
		
		//Yusuf (29/12/2012) - Variable untuk menampung total biaya yang dialokasikan ke premi pokok dan total biaya yg dialokasikan ke premi topup
		Double totalBiayaPP; Double totalBiayaTU;Double d_totalBiayaTUSingle;
		HashMap mapRegion=Common.serializableMap(uwDao.selectRegion(premi.getNo_spaj()));
		Map detbisnis = (Map) uwDao.selectDetailBisnis(premi.getNo_spaj()).get(0);
		String lsbs_id = (String) detbisnis.get("BISNIS");
		String lsdbs_number = (String) detbisnis.get("DETBISNIS");

		Date ldt_now = this.commonDao.selectSysdate();
		//String ls_pre = premi.getNo_pre().trim();
		String ls_pre = null; //karena blom ada Payment
		ls_region = premi.getK_region();
		ldt_prod = null;
	
		if(ls_region.equals("29"))ls_region="21";
		String lca_id = (String)mapRegion.get("LCA_ID");
//		String lwk_id = (String)mapRegion.get("LWK_ID");
		Integer jenis = 1;
		
		/** full autosales project start **/
		String flagAutoSalesCek = bacDao.selectFullAutoSalesFromMstPolicy(premi.getNo_spaj());
		if (flagAutoSalesCek != null && flagAutoSalesCek.equals("4")){
			jenis = 4;
		}
		/** full autosales project end **/
		
//		if(lsbs_id.equals("190") && lsdbs_number.equals("009")) jenis = 3;
		
		//Yusuf (30/04/2008)
		ldt_prod = getTanggalJurnal(ar_max_rk, lca_id, jenis);
		//ldt_prod = this.uwDao.selectProductionDate(premi.getNo_spaj(), premi.getTahunke(), premi.getPremike());
		

		if(ldt_prod==null) {
			errors.reject("payment.noProductionDate");
			return null;
		}
		
//		li_pmode = this.uwDao.selectPayMode(premi.getNo_spaj());
		
		if(premi.getKurs_harian()==null) premi.setKurs_harian(new Double (1));
		if(premi.getKurs_bulanan()==null) premi.setKurs_bulanan(new Double(1));
		//ldec_tot_premi = new Double(((premi.getPremi_dasar().doubleValue()+premi.getPremi_pa().doubleValue()-premi.getPremi_disc().doubleValue())*premi.getKurs_harian().doubleValue())+premi.getPremi_hcr().doubleValue());
		ldec_tot_premi = new Double(((premi.getPremi_dasar().doubleValue()+premi.getPremi_pa().doubleValue()-premi.getPremi_disc().doubleValue())*premi.getKurs_bulanan().doubleValue())+premi.getPremi_hcr().doubleValue());
		li_bisnis = premi.getBisnis_id();
		
		//Yusuf - 11/06/08 - untuk produk unit link syariah, profit center nya beda
//		String lsbs = li_bisnis.toString();
//		boolean isSyariah = false;
//		if(products.unitLinkSyariah(lsbs) || products.muamalat(premi.getNo_spaj())) {
//			isSyariah = true;
//		}

		//Yusuf - 30/03/10 - untuk produk stable link, efektif 1 april 2010, profit center nya beda
		boolean isStableLink = false;
		if(products.stableLink(lsbs_id)){
			isStableLink = true;
		}

		ls_grp = this.uwDao.selectBusinessGroup(li_bisnis);
		if(ls_grp==null) ls_grp="";
	
//		if(premi.getOld_polis()==null)premi.setOld_polis("");
		
		if(premi.getPremike()==1)premi.setMaterai(new Double(0));
		else {
			premi.setMaterai(this.uwDao.selectBiayaMaterai(premi.getNo_spaj(), premi.getTahunke(), premi.getPremike()));
			//premi.setMaterai(new Double(premi.getMaterai().doubleValue() * premi.getKurs_harian().doubleValue()));
			premi.setMaterai(new Double(premi.getMaterai().doubleValue() * premi.getKurs_bulanan().doubleValue()));
		}
		
//		for(z=0; z<ar_ldec_bayar.length; z++) {
//			ldec_tot_bayar = new Double(ldec_tot_bayar.doubleValue() + ar_ldec_bayar[z]);
//		}
//	
//		if(premi.getPremi_ar().doubleValue()!=0) {
//			ldec_tot_bayar = new Double(ldec_tot_bayar.doubleValue() + premi.getPremi_ar().doubleValue());
//		}
	
		//premi.setBiaya_polis(new Double(premi.getBiaya_polis().doubleValue() * premi.getKurs_harian().doubleValue()));
		premi.setBiaya_polis(new Double(premi.getBiaya_polis().doubleValue() * premi.getKurs_bulanan().doubleValue()));

		//if(products.stableLink(lsbs)) ar_topup = (double) 20000000; 

//		ldec_selisih = new Double(ldec_tot_bayar.doubleValue() - (ldec_tot_premi.doubleValue()+premi.getMaterai().doubleValue()+premi.getBiaya_polis().doubleValue()+ar_topup.doubleValue()));

		//biar gak ke-insert yg 8927 (yusuf - 11 feb 09)
		/*if(products.stableLink(lsbs)){
			ldec_selisih = 0.;
		}*/
		
//		if(ldec_selisih.doubleValue()>0) ls_flag="C";
//		else {
//			ldec_selisih = new Double(ldec_selisih.doubleValue() * -1);
//			ls_flag="D";
//		}
		//ll_no_jm = this.sequence.sequenceMst_ptc_tm(34);
		ll_no_jm = this.uwDao.selectGetPacGl("nojm");
		if(ll_no_jm==null){
			errors.reject("Sequence PTCTM gagal dilakukan.");
			return null;
		}
		
		//INSERT TMEMO;
		this.uwDao.insertMst_ptc_tm(ll_no_jm, li_pos, ldt_prod, ls_pre, currentUser.getLus_id());
		
		//update no jurnal yang tercreate di  mst_billing
		bacDao.updateMstBillingJurnalErbe(premi.getNo_spaj(), ll_no_jm);
	
		//INSERT JMEMO1 (Titipan Premi = TP, untuk seluruh premi NB, bisa lebih dari 1 row)
		//Yusuf - 29/02/12 - Req Gesti Akunting - untuk produk unit link syariah, profit center untuk sisi kredit 801, bukan 850 lagi
		//Deddy (11/05/2015) - Req Gesti Akunting(helpdesk 70088) - ditambahkan keterangan no_spaj pada saat proses jurnal titipan premi pertama (Cth Format : TP_No Spaj_No Polis_No Voucher_Tgl RK).
		if(ar_ldec_bayar[0]!=0) {
			for(int li_ttp=0; li_ttp<ar_ttp.size(); li_ttp++) {
				li_run++;
				int flagTopUp = ((Integer)ld_ttp_flagTp.get(li_ttp)).intValue();
				
				if(flagTopUp > 0){
					ls_ket[0] = "PIUTANG PREMI TU " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim();
				}else{
					ls_ket[0] = "PIUTANG PREMI NB " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim();
					
				}
				
//				if(premi.getPremike()==1) {
//					if(((Double)ar_ttp_kurs.get(li_ttp)).doubleValue()>1) {
//						ls_accno[0] = "501";
//						ls_accno[1] = "131118";
//					}else {
//						ls_accno[0] = "501";
//						ls_accno[1] = "131118";
//					}
//				}else {
//					if(((Double)ar_ttp_kurs.get(li_ttp)).doubleValue()>1) {
//						ls_accno[0] = "501";
//						ls_accno[1] = "1311181";
//					}else {
						ls_accno[0] = "501";
						ls_accno[1] = "131118";
//					}
//				}
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[0], (Double) ar_ttp.get(li_ttp), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
			}
		}

		//INSERT JMEMO AR (tidak ada di NB)
//		if(premi.getPremi_ar().doubleValue()!=0) {
//			li_run++;
//			ls_ket[0] = "AR " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " "  + premi.getNo_polis().trim() + " " + ar_ls_no_voucher + " " + defaultDateFormatStripes.format(ar_max_rk);
//			ls_accno[0] = (isSyariah ? "850" : isStableLink ? "701" : "501");
//			ls_accno[1] = "021002";
//			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[0], premi.getPremi_ar(), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
//		}
		//INSERT JMEMO2 (Nilai Tunai, tidak ada di NB)
//		if(ar_ldec_bayar[1]!=0) {
//			li_run++;
//			ls_ket[1]   = "NT " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher  + " " + premi.getOld_polis().trim();
//			if(premi.getKurs_bulanan().doubleValue()>1) {
//				ls_accno[0] = (isSyariah ? "850" : isStableLink ? "701" : "501");
//				ls_accno[1] = "41152";
//			}else {
//				ls_accno[0]= (isSyariah ? "850" : isStableLink ? "701" : "501");
//				ls_accno[1]= "41151";
//			}
//			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[1], new Double(ar_ldec_bayar[1]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
//		}
		//INSERT JMEMO3 (Tahapan, tidak ada di NB)
//		if(ar_ldec_bayar[2]!=0) {
//			li_run++;
//			ls_ket[2]   = "TH " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + ls_region + premi.getNo_polis().trim() + " " + ar_ls_no_voucher  + " " + premi.getOld_polis().trim();
//			if(premi.getKurs_bulanan().doubleValue()>1) {
//				ls_accno[0] = (isSyariah ? "850" : isStableLink ? "701" : "501");
//				ls_accno[1] = "41152";
//			}else {
//				ls_accno[0]= (isSyariah ? "850" : isStableLink ? "701" : "501");
//				ls_accno[1]="41151";
//			}
//			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[2], new Double(ar_ldec_bayar[2]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
//		}
		//INSERT JMEMO4 (Death Claim, tidak ada di NB)
//		if(ar_ldec_bayar[3]!=0) {
//			li_run++;
//			ls_ket[3]   = "DP " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher  + " " + premi.getOld_polis().trim();
//			if(premi.getKurs_bulanan().doubleValue()>1) {
//				ls_accno[0] = (isSyariah ? "850" : isStableLink ? "701" : "501");
//				ls_accno[1] = "337122";
//			}else {
//				ls_accno[0]=(isSyariah ? "850" : isStableLink ? "701" : "501");
//				ls_accno[1]="337121";
//			}
//			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[3], new Double(ar_ldec_bayar[3]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
//		}
		//INSERT JMEMO5 (tidak ada di NB)
//		if(ar_ldec_bayar[4]!=0) {
//			li_run++;
//			ls_ket[4]   = "PJ " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher  + " " + premi.getOld_polis().trim();
//			if(premi.getKurs_bulanan().doubleValue()>1) {
//				ls_accno[0] = (isSyariah ? "850" : isStableLink ? "701" : "501");
//				ls_accno[1] = "118122";
//			}else {
//				ls_accno[0]=(isSyariah ? "850" : isStableLink ? "701" : "501");
//				ls_accno[1]="118121";
//			}
//			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[4], new Double(ar_ldec_bayar[4]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
//		}
		//INSERT JMEMO7 (tidak ada di NB)
//		if(ar_ldec_bayar[6]!=0) {
//			li_run++;
//			ls_ket[6]   = "BB " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
//			if(premi.getKurs_bulanan().doubleValue()>1) {
//				ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
//				ls_accno[1] = "41162";
//			}else {
//				ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
//				ls_accno[1] = "41161";
//			}
//			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[6], new Double(ar_ldec_bayar[6]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
//		}
		//INSERT JMEMO8 (tidak ada di NB)
//		if(ar_ldec_bayar[7]!=0) {
//			li_run++;
//			ls_ket[7]   = "KA " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
//			if(premi.getKurs_bulanan().doubleValue()>1) {
//				ls_accno[0] = (isSyariah ? "850" : isStableLink ? "701" : "501");
//				ls_accno[1] = "41112";
//			}else {
//				ls_accno[0]=(isSyariah ? "850" : isStableLink ? "701" : "501");
//				ls_accno[1]="41111";
//			}
//			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[7], new Double(ar_ldec_bayar[7]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
//		}
		//INSERT JMEMO8 (tidak ada di NB)
//		if(ar_ldec_bayar[9]!=0) {
//			li_run++;
//			ls_ket[9]   = "AK " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
//			if(premi.getKurs_bulanan().doubleValue()>1) {
//				ls_accno[0] = (isSyariah ? "850" : isStableLink ? "701" : "501");
//				ls_accno[1] = "41152";
//			}else {
//				ls_accno[0]=(isSyariah ? "850" : isStableLink ? "701" : "501");
//				ls_accno[1]="41151";
//			}
//			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[9], new Double(ar_ldec_bayar[9]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
//		}
		//INSERT JMEMO8 (tidak ada di NB)
//		if(ar_ldec_bayar[10]!=0) {
//			li_run++;
//			ls_ket[10]   = "PG " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
//			if(premi.getKurs_bulanan().doubleValue()>1) {
//				ls_accno[0] = (isSyariah ? "850" : isStableLink ? "701" : "501");
//				ls_accno[1] = "41172";
//			}else {
//				ls_accno[0]=(isSyariah ? "850" : isStableLink ? "701" : "501");
//				ls_accno[1]="41171";
//			}
//			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[10], new Double(ar_ldec_bayar[10]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
//		}
//		//BUDGET PROMOSI
//		if(ar_ldec_bayar[11]!=0) {
//			li_run++;
//			ls_ket[11]   = "CAMPAIGN FREE POLIS " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim();
//			ls_accno[0] = "040";
//			ls_accno[1] = "811999";
//			this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[11], new Double(ar_ldec_bayar[11]), ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
//		}
		
		if(premi.getPremi_dasar().doubleValue()!=0) {
			if(premi.getPremike()==1) {
				//INSERT JMEMO11 (Premi Pokok)
//				if(li_pmode==0) {
//					ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
//					if(products.stableLink(lsbs_id)) {
//						ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 1);//"611117";
//					}else {
//						ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 1);//"611118";
//					}
//					
//				}else {
					ls_accno[0] = "529";
					ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 3);//"611119";		
//				}
				ls_ket[10]="PP " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() ;
			}
//			else {
//				//INSERT JMEMO11 (Premi Lanjutan, tidak ada di NB)
//				if(premi.getTahunke()==1) {
//					ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
//					//Yusuf (19/05/2010) - Req Tony Johan by Email : 611129 rubah ke 6111171
//					if(products.stableLink(lsbs)){
//						ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 4);//"6111171"; 
//					}else{
//						ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 4);//"611129";
//					}
//				}else {
//					ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
//					ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 5);//"61219";		
//				}
//				ls_ket[10]="PL " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
//			}
			
			//Yusuf (29/02/2012) - Req Gesti Akunting - Jurnal PP dan TU syariah, tidak disimpan bulat2 angkanya, tapi dikurang2i dulu dengan biaya2.
			totalBiayaPP = 0.;
			totalBiayaTU = 0.;
			d_totalBiayaTUSingle=0.;
//			if(isSyariah){
//				List<Map> daftarBiaya = this.uwDao.selectMemorialUnitLink(premi.getNo_spaj());
//				if(!daftarBiaya.isEmpty()) {
//					//looping dulu, total semua biaya untuk PP dan biaya untuk TU
//					for(Map biaya : daftarBiaya) {
//						String flag_jenis = (String) biaya.get("FLAG_JENIS");
//						if( flag_jenis.trim().equalsIgnoreCase("Biaya Asuransi") 	|| flag_jenis.trim().equalsIgnoreCase("Biaya Akuisisi") ||
//							flag_jenis.trim().equalsIgnoreCase("Biaya Adm")){ //flag_jenis.trim().equalsIgnoreCase("Biaya Extra") perlu gak?
//							totalBiayaPP = totalBiayaPP.doubleValue() + ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue();
//						}else if(flag_jenis.trim().equalsIgnoreCase("Biaya TopUp")){
//							if(((BigDecimal)biaya.get("LT_ID")).intValue()==5){
//								totalBiayaTU = (totalBiayaTU.doubleValue() + ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan());
//							}else if(((BigDecimal)biaya.get("LT_ID")).intValue()==2){
//								d_totalBiayaTUSingle=(d_totalBiayaTUSingle.doubleValue()+((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan());
//							}
//						}
//					}
//					//pengecekan tambahan apabila biaya PP > premi PP, maka harus dialokasikan ke Top Up
//					//Double premiPP = new Double (premi.getPremi_dasar().doubleValue() * premi.getKurs_harian().doubleValue()); //total PP
//					Double premiPP = new Double (premi.getPremi_dasar().doubleValue() * premi.getKurs_bulanan().doubleValue()); //total PP
//					if(premiPP.doubleValue() <= totalBiayaPP.doubleValue()){ //bila premiPP <= biayaPP
//						ldec_total = 0.;
//						totalBiayaPP = totalBiayaPP.doubleValue() - premiPP.doubleValue();//total biaya2(rider2,adm,asuransi,akuisisi)
//					}else{
//						ldec_total = premiPP.doubleValue() - totalBiayaPP.doubleValue();
//						totalBiayaPP=0.;
//					}
//					
//					//insert PP hanya bila > 0 (tidak habis dipotong biaya)
//					if(ldec_total.doubleValue() > 0){
//						li_run++;
//						this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[10], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
//					}
//				}
//			}else{
				li_run++;
				//ldec_total = new Double (premi.getPremi_dasar().doubleValue() * premi.getKurs_harian().doubleValue());
				ldec_total = new Double (premi.getPremi_dasar().doubleValue() * premi.getKurs_bulanan().doubleValue());
				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[10], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
//			}
			
			//INSERT JMEMO18 (Topup = TU)
			//Yusuf (29/12/2012) - Untuk Syariah, Jurnal Topup tidak disimpan bulat2, tapi dikurangi biaya TU
			//Lufi(23/06/2014) - Req Derry Accounting Top up Berkala Dan Single Dibedakan
			if(ar_topup.doubleValue()>0) {
				Integer i_flagTb=0,i_flagTs=0;
				Double ar_tu_awal=0.;
				
				for(int li_topup=0; li_topup<ar_ttp.size(); li_topup++) {
					
					int i_flag_tu=(Integer) ld_ttp_flagTp.get(li_topup);
					Double ar_tu=(Double)ar_ttp.get(li_topup);	
					ar_tu_awal=ar_tu.doubleValue();
					
					if(i_flag_tu>=1){
						ls_accno[0] = "529";
						li_run++;
						if(i_flag_tu==1){
//							if(isSyariah && i_flagTs<=0) ar_tu = ar_tu.doubleValue() - d_totalBiayaTUSingle.doubleValue();
							ls_accno[1] = "6111191";
							if(ar_tu.doubleValue()<0){
								ar_tu=0.;
								d_totalBiayaTUSingle=d_totalBiayaTUSingle.doubleValue()-ar_tu_awal.doubleValue();
							}else{
								i_flagTs=1;
							}
							
							ls_ket[17] = "Top Up Single " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ttp_voucher.get(li_topup)+ " " + defaultDateFormatStripes.format(ar_max_rk);
						}else 
							if(i_flag_tu==2 ){
//								if(isSyariah  && i_flagTb<=0) ar_tu = ar_tu.doubleValue() - totalBiayaTU.doubleValue();
								ls_accno[1] = "6111191";
								if(ar_tu.doubleValue()<0){
									ar_tu=0.;
									totalBiayaTU=totalBiayaTU.doubleValue()-ar_tu_awal.doubleValue();
								}else{
									i_flagTb=1;
							}
							
							ls_ket[17] = "Top Up Berkala " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim();
						}
//						if(isSyariah){
//							
//							/**
//							 *Lufi(05/08/2015)
//							 *Jika Syariah Top up dikurangi oleh jumlah total biaya yg belum tercover oleh Premi pokok 
//							 * 							 * 
//							 **/
//							if(totalBiayaPP>0){//bila total biaya-biaya(asuransi,rider2,adm,akuisisi)>0
//								if(ar_tu<totalBiayaPP){							
//									totalBiayaPP=totalBiayaPP.doubleValue() - ar_tu.doubleValue();
//									ar_tu=0.;
//								}else{
//									ar_tu=ar_tu.doubleValue() - totalBiayaPP.doubleValue();
//									totalBiayaPP=0.;
//								}
//							}
//						}
						this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[17], ar_tu, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());						
					}					
				}			
			}
			
//			//INSERT JMEMO13 (tidak ada di NB)
//			if(premi.getPremi_pa().doubleValue()!=0) {
//				li_run++;
//				if(premi.getTahunke()==1) {
//					ls_accno[0] = "0" + premi.getK_region(); // : (isSyariah ? "801" : ("0"+premi.getK_region()));
//					ls_accno[1] = "611126";
//				}else {
//					ls_accno[0] = "0" + premi.getK_region();
//					ls_accno[1] = "61216";
//				}
//				ls_ket[12] = "PA " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis() + " " + ar_ls_no_voucher;
//				//ldec_total = new Double(premi.getPremi_pa().doubleValue() * premi.getKurs_harian().doubleValue());
//				ldec_total = new Double(premi.getPremi_pa().doubleValue() * premi.getKurs_bulanan().doubleValue());
//				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[12], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
//			}
			
			//INSERT JMEMO14 (tidak ada di NB)
//			if(premi.getPremi_disc()!=0) {
//				li_run++;
//				if(premi.getPremike()==1) {
//					ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
//					ls_accno[1] = "62111";
//				}else {
//					if(premi.getTahunke()==1) {
//						ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+premi.getK_region()) : ("5"+premi.getK_region()));
//						ls_accno[1] = "62112";
//					}else {
//						ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+premi.getK_region()) : ("5"+premi.getK_region()));
//						ls_accno[1] = "6221";
//					}
//				}
//				ls_ket[13] = "DC " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
//				//ldec_total = new Double(premi.getPremi_disc().doubleValue() * premi.getKurs_harian().doubleValue());
//				ldec_total = new Double(premi.getPremi_disc().doubleValue() * premi.getKurs_bulanan().doubleValue());
//				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[13], ldec_total, ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
//			}
			
			//INSERT JMEMO15 (tidak ada di NB)
//			if(premi.getBiaya_polis().doubleValue()!=0) {
//				li_run++;
//				ls_ket[14] = "BP " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
//				ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
//				ls_accno[1] = "693";
//				ldec_total = premi.getBiaya_polis();
//				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[14], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
//			}
			//INSERT JMEMO16 (tidak ada di NB)
//			if(premi.getMaterai().doubleValue()!=0) {
//				li_run++;
//				ls_accno[0] = "001";
//				ls_accno[1] = "835316";
//				ls_ket[15] = "MT " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis() + " " + ar_ls_no_voucher;
//				ldec_total = premi.getMaterai();
//				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[15], ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
//			}
			
//			if(ldec_selisih.doubleValue() !=0) {
//				//Yusuf (11/7/08) Bila selisih dalam rupiah masuk Selisih Pembayaran, bila dollar masuk Selisih Kurs
//				
//				String akun = "8923"; //selisih kurs
//				String ket = "SK ";
//				if(premi.getLku_id().equals("01")) {
//					akun = "8927";
//					ket = "SP ";
//				}
//				
//				li_run++;
//				if(ls_flag.equals("C")) {
//					ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
//					ls_accno[1] = "8914";
//					
//					if(products.muamalat(premi.getNo_spaj())) {
//						ls_accno[1] = "41131"; //Muamalat Indonesia
//						ket = "MI ";
//					}
//					
//				}else {
//					ls_accno[0] = (isSyariah ? "850" : isStableLink ? ("7"+ls_region) : ("5"+ls_region));
//					ls_accno[1] = akun;
//				}					
//				ls_ket[16] = ket + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis().trim() + " " + ar_ls_no_voucher;
//				ldec_total = ldec_selisih;
//				this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), ls_ket[16], ldec_total, ls_accno[0], ls_accno[1], ls_flag, premi.getNo_spaj());
//			}
			
			//lufi-3/7/2014 untuk biaya top up dipisah jadi single dan berkala jika berkala 611191 single 6111081
			if(Integer.valueOf(this.uwDao.selectBulanProduksi(premi.getNo_spaj())) >= 200803) {
				List<Map> daftarBiaya = this.uwDao.selectMemorialUnitLink(premi.getNo_spaj());
				if(!daftarBiaya.isEmpty()) {
					
					//Yusuf - 29 Feb 2012 - as of march 2012, jurnal syariah tidak disimpan angkanya bulat2, tapi dikurangi dengan biaya2
//					if(isSyariah){
//					
//						//insert sisi KREDIT (bukan sisi debet, seperti konvensional)
//						for(Map biaya : daftarBiaya) {
//							String flag_jenis = (String) biaya.get("FLAG_JENIS");
//							Integer ljb_id = ((BigDecimal)biaya.get("LJB_ID")).intValue();
//							
//							//Pendapatan Akuisisi
//							if(flag_jenis.trim().equalsIgnoreCase("Biaya Akuisisi")) {
//								li_run++;
//								ls_accno[0] = "801";
//								ls_accno[1] = "611119";
//								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
//								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Akuisisi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
//							
//							//Pendapatan Asuransi dan Rider
//							}else if(flag_jenis.trim().equalsIgnoreCase("Biaya Asuransi")) {
//								//Req Rudy : UNTUK BIAYA ASURANSI SYARIAH DAN RIDER DIPECAH JADI 2.(BIAYA ASURANSI JUGA DIPECAH JADI 2 YAKNI TABBARU DAN UJRAH)
//								ls_accno[0] = "802";
//								ls_accno[1] = "611119";
//								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
//								if(ljb_id==0){//di select ljb_id di set 0 untuk sum biaya rider.
//									li_run++;
//									this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Asuransi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
//								}else{
//									for(int i=0;i<2;i++){//looping 2 kali untuk set ujrah dan tabarru
//										li_run++;
//										Double persenTabarru = getTabarru(premi.getNo_spaj());
//										Double persenUjrah = 1-persenTabarru;
//										if(i==0){//jika ujrah(i=0) ls_accno[0]= "801"
//											ls_accno[0] = "801";
//											ldec_total = (((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue() * persenUjrah)*premi.getKurs_bulanan();
//										}else if(i==1){//jika tabarru(i=1) ls_accno[0]= "802"
//											ls_accno[0] = "802";
//											ldec_total = (((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue() * persenTabarru)*premi.getKurs_bulanan();
//										}
//										this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Asuransi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
//									}
//								}
//							//Pendapatan Akuisisi (Top Up)
//							} else if(flag_jenis.trim().equalsIgnoreCase("Biaya TopUp")) {
//								li_run++;
//								ls_accno[0] = "801";
//								if(((BigDecimal)biaya.get("LT_ID")).intValue()==5){
//									ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 6);
//								}else if(((BigDecimal)biaya.get("LT_ID")).intValue()==2){
//									ls_accno[1] = bacDao.selectGetAccPremi(premi.getNo_spaj(), 2);
//								}
//								
//								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
//								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By TopUp " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
//								
//							//Pendapatan Administrasi
//							} else if(flag_jenis.trim().equalsIgnoreCase("Biaya Adm")) {
//								li_run++;
//								ls_accno[0] = "801";
//								ls_accno[1] = "611119";
//								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
//								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Administrasi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
//							}
//							
//							//Pendapatan Ekstra (perlu dicatat gak?)
//						}
//						
//					//Untuk produk ulink non syariah, biasa saja
//					}else{
						//PERTAMA, insert dulu yang sisi debet
						for(Map biaya : daftarBiaya) {
							String flag_jenis = (String) biaya.get("FLAG_JENIS");
							
							//Biaya Akuisisi
							if(flag_jenis.trim().equalsIgnoreCase("Biaya Akuisisi")) {
								li_run++;
								ls_accno[0] = "529";
								ls_accno[1] = "692";
								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
								BiayaD = BiayaD + ldec_total ;
								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Akuisisi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
																
							//Biaya Asuransi dan Biaya Rider
							}else if(flag_jenis.trim().equalsIgnoreCase("Biaya Asuransi")) {
								li_run++;
								ls_accno[0] = "529";
								ls_accno[1] = "696";
								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
								BiayaD = BiayaD + ldec_total ;
								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Asuransi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
								
							//Biaya Akuisisi (Top Up)
							} else if(flag_jenis.trim().equalsIgnoreCase("Biaya TopUp")) {
								li_run++;
								ls_accno[0] = "529";
								ls_accno[1] = "697";
								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
								BiayaD = BiayaD + ldec_total ;
								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By TopUp " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
								
							//Biaya Administrasi
							} else if(flag_jenis.trim().equalsIgnoreCase("Biaya Adm")) {
								li_run++;
								ls_accno[0] = "529";
								ls_accno[1] = "698";
								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
								BiayaD = BiayaD + ldec_total ;
								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Administrasi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
							}							
						}
								
						//KEDUA, insert yang sisi credit, sama persis seperti yg sisi debet, beda di 3 digit account depannya, sama di kode C atau D nya

						for(Map biaya : daftarBiaya) {
							String flag_jenis = (String) biaya.get("FLAG_JENIS");
							
							//Pendapatan Akuisisi
							if(flag_jenis.trim().equalsIgnoreCase("Biaya Akuisisi")) {
								li_run++;
								ls_accno[0] = "029";
								ls_accno[1] = "692";
								ldec_total = (((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue())*premi.getKurs_bulanan();
								BiayaC = BiayaC + ldec_total ;
								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Akuisisi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
							
							//Pendapatan Asuransi dan Rider
							}else if(flag_jenis.trim().equalsIgnoreCase("Biaya Asuransi")) {
								li_run++;
								ls_accno[0] = "029";
								ls_accno[1] = "696";
								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
								BiayaC = BiayaC + ldec_total ;
								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Asuransi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
								
							//Pendapatan Akuisisi (Top Up)
							} else if(flag_jenis.trim().equalsIgnoreCase("Biaya TopUp")) {
								li_run++;
								ls_accno[0] = "029";
								ls_accno[1] = "697";
								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
								BiayaC = BiayaC + ldec_total ;
								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By TopUp " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
								
							//Pendapatan Administrasi
							} else if(flag_jenis.trim().equalsIgnoreCase("Biaya Adm")) {
								li_run++;
								ls_accno[0] = "029";
								ls_accno[1] = "698";
								ldec_total = ((BigDecimal) biaya.get("MBU_JUMLAH")).doubleValue()*premi.getKurs_bulanan();
								BiayaC = BiayaC + ldec_total ;
								this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By Administrasi " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ldec_total, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
							}
						}
						
						//Input Jumlah seluruh Biaya DEBIT
//						ls_accno[0] = "001";
//						ls_accno[1] = "221";
//						li_run = li_run+1;
//						this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By TopUp " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), BiayaD, ls_accno[0], ls_accno[1], "D", premi.getNo_spaj());
						//	
						
						//Input Jumlah seluruh Biaya CREDIT
//						ls_accno[0] = "501";
//						ls_accno[1] = "221";
//						li_run = li_run+1;
//						this.uwDao.insertMst_ptc_jm(ll_no_jm, new Integer(li_run), "By TopUp " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), BiayaC, ls_accno[0], ls_accno[1], "C", premi.getNo_spaj());
						
						//
//					}
									
				}
			}
		}
		
		
		//lufi - proses insert nilai komisi pada jurnal premi pertama
//		if(!listComForJm.isEmpty()){
//			li_run = prosesJurnaPencatatanKomisiPertama(listComForJm,premi, lca_id, lwk_id,li_run,isSyariah,isStableLink,ll_no_jm);				
//		}
//		
//		if(!listComBonusForJm.isEmpty()){
//			li_run = prosesJurnalPencatatanBonusKomisi(listComBonusForJm,premi, lca_id, lwk_id,li_run,isSyariah,isStableLink,ll_no_jm);
//		}
//		
//		if(!listComRewardForJm.isEmpty()){
//			li_run = prosesJurnalPencatatanReward(listComRewardForJm,premi, lca_id, lwk_id,li_run,isSyariah,isStableLink,ll_no_jm);
//		}
		
		//MANTA
//		String[] nopre = ls_pre.split(",");
//		Payment payment = new Payment();
//		payment.setReg_spaj(premi.getNo_spaj());
//		payment.setMspa_no_jm(ll_no_jm);
//		for(int i=0; i<nopre.length; i++){
//			payment.setMspa_no_pre(nopre[i].trim());
//			this.editDataDao.updateMstPaymentbySpajAndPre(payment);
//		}
//		this.editDataDao.updateMstPaymentNonBank(premi.getNo_spaj(), ll_no_jm);
//		this.uwDao.updateNoPreMstDrekDet(null, payment.getReg_spaj(), premi.getMspa_payment_id(), null, ll_no_jm);
		return premi;
	
	}
	
	//Ridhaal
	public Integer jurnalMemorialPremiUlinkKomisiERBEPackagePremiPokok(Premi premi, Date ldt_prod, List daftarKomisi, BindException errors, User currentUser) {
		int status_jurnal = 0;
		String ll_no_jm;
		Integer li_pos = new Integer(1);
		String ls_pre = null; //karena blom ada Payment
				
		ll_no_jm = this.uwDao.selectGetPacGl("nojm");
		if(ll_no_jm==null){
			errors.reject("Sequence PTCTM gagal dilakukan.");
			return null;
		}
		
		//INSERT TMEMO;
		this.uwDao.insertMst_ptc_tm(ll_no_jm, li_pos, ldt_prod, ls_pre, currentUser.getLus_id());
		
		//Update no jurnal MSINV_NO_JM untuk komisi terutang
		bacDao.updateMstInvoiceJurnalErbe(premi.getNo_spaj(), ll_no_jm , 1);
	
		HashMap mapRegion=Common.serializableMap(uwDao.selectRegion(premi.getNo_spaj()));
		String lca_id = (String)mapRegion.get("LCA_ID");
		String lwk_id = (String)mapRegion.get("LWK_ID");
		String lsrg_id = (String)mapRegion.get("LSRG_ID");
		int pkp = bacDao.select_PKP(lca_id, lwk_id, lsrg_id);
		
		HashMap mapComm= (HashMap)daftarKomisi.get(0);
		Double KomLink = ((BigDecimal) mapComm.get("MSINV_DPP")).doubleValue();
		Double pph = ((BigDecimal) mapComm.get("MSINV_PPH")).doubleValue();
		Double ppn = ((BigDecimal) mapComm.get("MSINV_PPN")).doubleValue();
		Double utangKom = 0.0;
		
		if (pkp == 0){//Tidak ada PPN
			int li_run = 1;
			String ls_accno0 = "029";
			String ls_accno1 = "761117";
			this.uwDao.insertMst_ptc_jm(ll_no_jm, li_run, "KOMISI LINK " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), KomLink, ls_accno0, ls_accno1, "D", premi.getNo_spaj());
		
			li_run++;
			ls_accno0 = "001";
			ls_accno1 = "431";
			utangKom = KomLink -pph;
			this.uwDao.insertMst_ptc_jm(ll_no_jm, li_run, "UTANG KOMISI LINK " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), utangKom, ls_accno0, ls_accno1, "C", premi.getNo_spaj());
		
			li_run++;
			ls_accno0 = "001";
			ls_accno1 = "444";
			this.uwDao.insertMst_ptc_jm(ll_no_jm, li_run, "PPH 23 UTANG KOMISI LINK " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis() + " (ERBE)", pph, ls_accno0, ls_accno1, "C", premi.getNo_spaj());
		
			status_jurnal = 1;
		}else{//ada PPN
			int li_run = 1;
			String ls_accno0 = "029";
			String ls_accno1 = "761117";
			this.uwDao.insertMst_ptc_jm(ll_no_jm, li_run, "KOMISI LINK " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), KomLink, ls_accno0, ls_accno1, "D", premi.getNo_spaj());
		
			li_run++;
			ls_accno0 = "029";
			ls_accno1 = "761117";
			this.uwDao.insertMst_ptc_jm(ll_no_jm, li_run, "PPN KOMISI LINK " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ppn, ls_accno0, ls_accno1, "D", premi.getNo_spaj());
		
			
			li_run++;
			ls_accno0 = "001";
			ls_accno1 = "431";
			utangKom = KomLink + ppn - pph;
			this.uwDao.insertMst_ptc_jm(ll_no_jm, li_run, "UTANG KOMISI LINK " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), utangKom, ls_accno0, ls_accno1, "C", premi.getNo_spaj());
		
			li_run++;
			ls_accno0 = "001";
			ls_accno1 = "444";
			this.uwDao.insertMst_ptc_jm(ll_no_jm, li_run, "PPH 23 UTANG KOMISI LINK " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis() + " (ERBE)", pph, ls_accno0, ls_accno1, "C", premi.getNo_spaj());
		
			status_jurnal = 1;
		}
		
	return status_jurnal;
	}
	
	//Ridhaal
	public Integer jurnalMemorialPremiUlinkKomisiERBEPackagePremiTopup(Premi premi, Date ldt_prod, List daftarKomisi, BindException errors, User currentUser) {
		int status_jurnal = 0;
		String ll_no_jm;
		Integer li_pos = new Integer(1);
		String ls_pre = null; //karena blom ada Payment
				
		ll_no_jm = this.uwDao.selectGetPacGl("nojm");
		if(ll_no_jm==null){
			errors.reject("Sequence PTCTM gagal dilakukan.");
			return null;
		}
		
		//INSERT TMEMO;
		this.uwDao.insertMst_ptc_tm(ll_no_jm, li_pos, ldt_prod, ls_pre, currentUser.getLus_id());
		
		//Update no jurnal MSINV_NO_JM untuk komisi terutang
		bacDao.updateMstInvoiceJurnalErbe(premi.getNo_spaj(), ll_no_jm , 2);
	
		HashMap mapRegion=Common.serializableMap(uwDao.selectRegion(premi.getNo_spaj()));
		String lca_id = (String)mapRegion.get("LCA_ID");
		String lwk_id = (String)mapRegion.get("LWK_ID");
		String lsrg_id = (String)mapRegion.get("LSRG_ID");
		int pkp = bacDao.select_PKP(lca_id, lwk_id, lsrg_id);
		
		HashMap mapComm= (HashMap)daftarKomisi.get(0);
		Double KomLink = ((BigDecimal) mapComm.get("MSINV_DPP")).doubleValue();
		Double pph = ((BigDecimal) mapComm.get("MSINV_PPH")).doubleValue();
		Double ppn = ((BigDecimal) mapComm.get("MSINV_PPN")).doubleValue();
		Double utangKom = 0.0;
		
		if (pkp == 0){//Tidak ada PPN
			int li_run = 1;
			String ls_accno0 = "029";
			String ls_accno1 = "761117";
			this.uwDao.insertMst_ptc_jm(ll_no_jm, li_run, "KOMISI LINK TU " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), KomLink, ls_accno0, ls_accno1, "D", premi.getNo_spaj());
		
			li_run++;
			ls_accno0 = "001";
			ls_accno1 = "431";
			utangKom = KomLink -pph;
			this.uwDao.insertMst_ptc_jm(ll_no_jm, li_run, "UTANG KOMISI LINK TU " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), utangKom, ls_accno0, ls_accno1, "C", premi.getNo_spaj());
		
			li_run++;
			ls_accno0 = "001";
			ls_accno1 = "444";
			this.uwDao.insertMst_ptc_jm(ll_no_jm, li_run, "PPH 23 UTANG KOMISI LINK TU " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis() + " (ERBE)", pph, ls_accno0, ls_accno1, "C", premi.getNo_spaj());
		
			status_jurnal = 1;
		}else{//ada PPN
			int li_run = 1;
			String ls_accno0 = "029";
			String ls_accno1 = "761117";
			this.uwDao.insertMst_ptc_jm(ll_no_jm, li_run, "KOMISI LINK TU " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), KomLink, ls_accno0, ls_accno1, "D", premi.getNo_spaj());
		
			li_run++;
			ls_accno0 = "029";
			ls_accno1 = "761117";
			this.uwDao.insertMst_ptc_jm(ll_no_jm, li_run, "PPN KOMISI LINK TU " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), ppn, ls_accno0, ls_accno1, "D", premi.getNo_spaj());
		
			
			li_run++;
			ls_accno0 = "001";
			ls_accno1 = "431";
			utangKom = KomLink + ppn - pph;
			this.uwDao.insertMst_ptc_jm(ll_no_jm, li_run, "UTANG KOMISI LINK TU " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis(), utangKom, ls_accno0, ls_accno1, "C", premi.getNo_spaj());
		
			li_run++;
			ls_accno0 = "001";
			ls_accno1 = "444";
			this.uwDao.insertMst_ptc_jm(ll_no_jm, li_run, "PPH 23 UTANG KOMISI LINK TU " + FormatString.nomorSPAJ(premi.getNo_spaj().trim())+ " " + premi.getNo_polis() + " (ERBE)", pph, ls_accno0, ls_accno1, "C", premi.getNo_spaj());
		
			status_jurnal = 1;
		}
		
	return status_jurnal;
	}
}
