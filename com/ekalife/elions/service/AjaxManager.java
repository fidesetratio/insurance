package com.ekalife.elions.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

import produk_asuransi.n_prod;

import com.ekalife.elions.dao.BacDao;
import com.ekalife.elions.dao.BasDao;
import com.ekalife.elions.dao.CommonDao;
import com.ekalife.elions.dao.EndorsDao;
import com.ekalife.elions.dao.FinanceDao;
import com.ekalife.elions.dao.SnowsDao;
import com.ekalife.elions.dao.UwDao;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Hadiah;
import com.ekalife.elions.model.Linkdetail;
import com.ekalife.elions.model.MedQuest;
import com.ekalife.elions.model.MstInboxHist;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.User;
import com.ekalife.utils.CheckSum;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.Products;
import com.ekalife.utils.f_hit_umur;

public class AjaxManager {
	
	protected final Log logger = LogFactory.getLog( getClass() );
	
	private UwDao uwDao;
	private CommonDao commonDao;
	private BacDao bacDao;
	private FinanceDao financeDao;
	private BasDao basDao;
	private Products products;
	private EndorsDao endorsDao;
	private SnowsDao snowsDao;
	
	private DateFormat defaultDateFormat;

	
	public SnowsDao getSnowsDao() {
		return snowsDao;
	}

	public void setSnowsDao(SnowsDao snowsDao) {
		this.snowsDao = snowsDao;
	}

	public void setDefaultDateFormat(DateFormat defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
	}

	public void setFinanceDao(FinanceDao financeDao) {
		this.financeDao = financeDao;
	}

	public BacDao getBacDao() {
		return bacDao;
	}

	public void setBacDao(BacDao bacDao) {
		this.bacDao = bacDao;
	}

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public void setUwDao(UwDao uwDao) {
		this.uwDao = uwDao;
	}
	public void setBasDao(BasDao basDao) {
		this.basDao = basDao;
	}
	public void setProducts(Products products) {
		this.products = products;
	}

//	public Map selectLeadNasabah(String lead) throws DataAccessException{
//		return this.uwDao.selectLeadNasabah(lead);
//	}

	public String ping(String ip) {
		String result = "";
		try {
			Process p = Runtime.getRuntime().exec("ping -n 1 " + ip);
			int status = p.waitFor();
			result = (ip + " is " + (status == 0 ? "alive" : "dead"));
		} catch (InterruptedException e) {
			logger.error("ERROR :", e);
		} catch (IOException e) {
			logger.error("ERROR :", e);
		}
		return result;
	}

	public int selectIsInputanBank(String spaj) {
		return bacDao.selectIsInputanBank(spaj);
	}

	//hitung enddate berdasarkan beg date dan ins period
	public String populateEndDateCrossSelling(String beg_date, int ins_period) throws DataAccessException{
		try {
			Date d = defaultDateFormat.parse(beg_date);
			Date d2 = FormatDate.add(d, Calendar.YEAR, ins_period);
			d2 = FormatDate.add(d2, Calendar.DATE, -1);
			return defaultDateFormat.format(d2);
		} catch (ParseException e) {
			logger.error("ERROR :", e);
		}
		return null; 
	}
	
	public Map populateAgentCrossSelling(String msag_id) throws DataAccessException{
		Map result = bacDao.selectagenpenutup(msag_id);
		if(result == null) {
			result = new HashMap();
			result.put("errMessage", "Agen ini tidak terdaftar sebagai agen aktif dan berlisensi. Harap hubungi bagian Agency Support.");
		}
		return result;
	}
	
	public Map selectMstPolicyCs(String nama_pp, String birth_date) throws DataAccessException, ParseException{
		Map result = bacDao.selectMstPolicyCs(nama_pp, birth_date);
		return result;
	}
	
	public int flagWarningAutodebet(String lsbs) {
		String nama_produk="produk_asuransi.n_prod_"+FormatString.rpad("0", lsbs.toString(), 2);
		try {
			Class aClass = Class.forName( nama_produk );
			n_prod produk = (n_prod) aClass.newInstance();
			return produk.flag_warning_autodebet;
		} catch (ClassNotFoundException e) {
			logger.error("ERROR :", e);
		} catch (InstantiationException e) {
			logger.error("ERROR :", e);
		} catch (IllegalAccessException e) {
			logger.error("ERROR :", e);
		}
		
		return 0;
	}
	
	public Map populateFieldsCrossSelling(String lsbs_lsdbs) throws DataAccessException{
		Map results = new HashMap();
		Integer lsbs = Integer.valueOf(lsbs_lsdbs.substring(0,3));
		Integer lsdbs = Integer.valueOf(lsbs_lsdbs.substring(3));
		String nama_produk="produk_asuransi.n_prod_"+FormatString.rpad("0", lsbs.toString(), 2);
		
		try {
			Class aClass = Class.forName( nama_produk );
			n_prod produk = (n_prod) aClass.newInstance();
			produk.of_set_bisnis_no(lsdbs);
			
			//ins period, pay period, flag_uppremi
			results.put("mspo_ins_period", produk.ii_contract_period);
			results.put("mspo_pay_period", produk.ii_lbayar);
			results.put("flag_uppremi", produk.flag_uppremi);
			
			//lst_pay_mode
			List<HashMap> lst_pay_mode = new ArrayList<HashMap>();
			for(int i=1; i<produk.li_pmode.length; i++) {
				HashMap m = new HashMap();
				m.put("LSCB_ID", produk.li_pmode[i]);
				m.put("LSCB_PAY_MODE", bacDao.select_detilcrbayar(produk.li_pmode[i]));
				lst_pay_mode.add(m);
			}
			results.put("lst_pay_mode", lst_pay_mode);
			
			//lst_kurs
			List<HashMap> lst_kurs = new ArrayList<HashMap>();
			for(int i=0; i<produk.is_forex.length; i++) {
				HashMap m = new HashMap();
				m.put("LKU_ID", produk.is_forex[i]);
				m.put("LKU_SYMBOL", bacDao.select_detilkurs(produk.is_forex[i]));
				lst_kurs.add(m);
			}
			results.put("lst_kurs", lst_kurs);
			
		} catch (ClassNotFoundException e) {
			logger.error("ERROR :", e);
		} catch (InstantiationException e) {
			logger.error("ERROR :", e);
		} catch (IllegalAccessException e) {
			logger.error("ERROR :", e);
		}
		
		return results;
	}
	
	public List selectUserList(String userName) throws DataAccessException{
		return this.commonDao.selectUserList(userName);
	}
	
	public List selectUserList2(String userName) throws DataAccessException{
		return this.commonDao.selectUserList2(userName);
	}
	
	public List selectWilayah(String like) throws DataAccessException {
		return this.uwDao.selectWilayah(like);
	}
	
	public List select_wilayah(String query)throws DataAccessException {
		return this.bacDao.select_wilayah(query);
	}	

	public List listIcd(String query) throws DataAccessException {
		return this.uwDao.select_Icd(query);
	}
	
	public List listRegion(String query) {
		return this.uwDao.selectRegions(query);		
	}
	
	public List listRegion2(String query) {
		return this.commonDao.selectRegions2(query);		
	}
	
	public List select_bank1(String query) {
		return this.bacDao.select_bank1(query);
	}
	
	public List select_simas_card(String query) {
		return this.bacDao.select_simas_card(query);
	}
	
	public List selectLstStatusAcceptSub(String query) {
		//Integer lssa_id = Integer.parseInt(query);
		return this.bacDao.selectLstStatusAcceptSub(query);
	}
	
	public List select_bank2(String query) {
		return this.bacDao.select_bank2(query);
	}
	
	public List select_bank3(String query) {
		return this.bacDao.select_bank3(query);
	}
	
	public List select_bankcredit(String query) {
		return this.bacDao.select_bankcredit(query);
	}
	
	public List select_sumberBisnis(String query){
		return this.bacDao.select_sumberBisnis(query);
	}
	
	public List select_reff(String query) {
		return this.bacDao.select_reff(query);
	}
	
	public List select_bii1(String query) throws DataAccessException {
		return this.bacDao.select_bii1(query);
	}	
	
	public List select_cic(String query) throws DataAccessException {
		return this.bacDao.select_cic(query);
	}	
	
	public List select_reff_cic (String query) throws DataAccessException {
		return this.bacDao.select_reff_cic(query);
	}	
	
	public List select_reff_shinta(String query,Integer kode_flag, Boolean show_ajspusat) throws DataAccessException {
		return this.bacDao.select_reff_shinta(query,kode_flag, show_ajspusat);
	}
	
	public List select_reff_shintacekBc(String query,Integer kode_flag, Boolean show_ajspusat) throws DataAccessException {
		return this.bacDao.select_reff_shintacekBC(query,kode_flag, show_ajspusat);
	}
	
	public List select_reff_shintacekBc2(String query,Integer kode_flag, Boolean show_ajspusat) throws DataAccessException {
		return this.bacDao.select_reff_shintacekBC2(query,kode_flag, show_ajspusat);
	}
	
	public List select_reff_shintabyagentcode(String query,Integer kode_flag , Boolean show_ajspusat) throws DataAccessException {
		return this.bacDao.select_reff_shintabyagentcode(query,kode_flag, show_ajspusat);
	}
	
	public List select_reff_shintabyagentcode2(String query,Integer kode_flag, Boolean show_ajspusat) throws DataAccessException {
		return this.bacDao.sselect_reff_shintabyagentcode2(query,kode_flag,show_ajspusat);
	}
	
	public List select_reff_shinta2(String query,Integer kode_flag, Boolean show_ajspusat) throws DataAccessException {
		return this.bacDao.select_reff_shinta2(query,kode_flag, show_ajspusat);
	}
	
	public Map select_referrer(String query) {
		return this.bacDao.select_referrer(query);
	}
	
	public Map select_bii(String query)  {
		return this.bacDao.select_bii(query);
	}	
	
	public List select_company(String query) {
		return this.bacDao.select_company(query);
	}		
	
	public List select_company_ekal(String query) {
		return this.bacDao.select_company_ekal(query);
	}
	
	public Map select_namacompany(String query)  {
		return this.bacDao.select_namacompany(query);
	}		
	
	public Integer selectJumlahNAB(int pos, String startDate, String endDate) {
		return this.uwDao.selectJumlahNAB(pos, startDate, endDate);
	}
	
	public Integer select_panj_rek2(String query) throws DataAccessException {
		return this.bacDao.select_panj_rek2(query);
	}
	
	public Integer select_panj_rek1(String query) throws DataAccessException {
		return this.bacDao.select_panj_rek1(query);
	}
	
	public List listBankPusat(){
		return this.uwDao.selectBankPusat();
	}
	
	public List listCaraBayar(String tipe){
		return this.uwDao.selectPayType0(tipe);
	}
	
	public List listEmployeesInDepartment(String lde_id) {
		return this.commonDao.selectEmployeesInDepartment(lde_id);
	}

	public List selectDaftarSPAJUnitLink(int lspd) {
		return this.uwDao.selectDaftarSPAJUnitLink(lspd);
	}
	
	public List selectListNoEndors(int lstb_id,int lspd_id,String reg_spaj) {
		return this.endorsDao.selectListNoEndors(lstb_id, lspd_id,reg_spaj);
	}
	
	public List selectnilaikurs(String kurs,String tgl_kurs) {
		return this.bacDao.selectnilaikurs(kurs,tgl_kurs);
	}	
	
	public List select_detilprodukutama(Integer kode)
	{
		return this.bacDao.select_detilprodukutama(kode);
	}
	
	public List select_detilprodukutamamall(Integer kode)
	{
		return this.bacDao.select_detilprodukutamamall(kode);
	}
	
	public List select_detilprodukutama_banksinarmas(Integer kode)
	{
		return this.bacDao.select_detilprodukutama_banksinarmas(kode);
	}
	
	public List select_detilprodukutamaadminmall(Integer kode)
	{
		return this.bacDao.select_detilprodukutamaadminmall(kode);
	}
	
	public List select_detilprodukutama_sekuritas(Integer kode)
	{
		return this.bacDao.select_detilprodukutama_sekuritas(kode);
	}
	
	public List select_detilprodukutama_platinumbii(Integer kode)
	{
		return this.bacDao.select_detilprodukutama_platinumbii(kode);
	}
	
	public String select_detilkurs(String kurs)
	{
		return this.bacDao.select_detilkurs(kurs);
	}
	
	public String select_detilcrbayar(Integer crbyr)
	{
		return this.bacDao.select_detilcrbayar(crbyr);
	}
	
	public Map pesanTandaTerimaPolis(String spaj) {
		return this.uwDao.selectInfoAgen(spaj);
	}

	public List selectBankEkaLife(String dig4){
		return this.uwDao.selectBankEkaLife(dig4);
	}
	
	public List selectBankEkaLife2(String dig5){
		return this.uwDao.selectBankEkaLife2(dig5);
	}

	public List selectPayType0(String tipe){
		return this.uwDao.selectPayType0(tipe);
	}

	
	public String searchXml(String filePath, String colKey, String colName, String value) {
		return Common.searchXml(filePath, colKey, colName, value);
	}
	
	public Integer select_cektglkirimpolis (String spaj) {
		Map temp = uwDao.select_cektglkirimpolis(spaj);
		Integer flag_confirm_ok = 0;
		if(!temp.isEmpty()){
			Date mste_tgl_terima_admedika = (Date)temp.get("ADME");
			Date mspo_date_print = (Date)temp.get("MSPO_DATE_PRINT");
			BigDecimal flag_provider = (BigDecimal)temp.get("PROV");
			if(!Common.isEmpty(mspo_date_print) && flag_provider.intValue()==2 && Common.isEmpty(mste_tgl_terima_admedika)){
				flag_confirm_ok = 1;
			}
		}
		return flag_confirm_ok;
	}
	
	public List getPrePostErrorMessages(String spaj, int posisi) {
		List pesan = new ArrayList();
		Map m;

		try {
			n_prod produk = (n_prod) Class.forName("produk_asuransi.n_prod_"+this.uwDao.selectBusinessId(spaj)).newInstance();
			produk.setSqlMap(this.getBacDao().getSqlMapClient());

			if(posisi == 4) { //menu payment
				Integer topupBerkala;
				if(!produk.isInvestasi) {
					m = new HashMap();
					m.put("kode", "00");
					m.put("pesan", "bukan produk unit-link");
					pesan.add(m);
				}
				
				m = new HashMap();
				if(this.uwDao.validationAlreadyPaid(spaj, 1, 1).intValue()>0){
					m.put("kode", "01");
					m.put("pesan", "payment: lunas");
				}else {
					m.put("kode", "02");
					m.put("pesan", "payment: belum lunas");
				}
				pesan.add(m);
				m = new HashMap();
				if((topupBerkala=this.uwDao.validationTopup(spaj))!=null){ //apakah ada topup berkala?
					if(topupBerkala.intValue()==1){
						if(this.uwDao.validationAlreadyPaid(spaj, 1 ,2).intValue()==0){//belom lunas topupnya
							m.put("kode", "03");
							m.put("pesan", "payment top-up: belum lunas (tidak bisa transfer)");
						}else {
							m.put("kode", "04");
							m.put("pesan", "payment top-up: lunas");						
						}
						pesan.add(m);
					}
				}
			}else if(posisi == 6) { //menu print polis
				Map map =this.uwDao.validationPrintPolis(spaj); 
				if(map.get("ORI")!=null) {
					m = new HashMap();
					m.put("kode", "05");
					m.put("pesan", "polis: sudah pernah dicetak");
					pesan.add(m);
				}
				if(produk.isInvestasi && this.uwDao.validationNAB(spaj).intValue()>0) {
					m = new HashMap();
					m.put("kode", "06");
					m.put("pesan", "NAB belum diproses");
					pesan.add(m);
				}
			}else if(posisi == -1) { //menu viewer
				if(!produk.isInvestasi) {
					m = new HashMap();
					m.put("kode", "07");
					m.put("pesan", "Bukan produk Unit-Link (Investasi)");
					pesan.add(m);					
				}
			}else if(posisi == 8) { //menu komisi finance
				if(!this.financeDao.validationBillingPosition(spaj, 8)) {
					m = new HashMap();
					m.put("kode", "08");
					m.put("pesan", "Posisi Billing Polis Salah.");
					pesan.add(m);					
				}
			}
		}catch(Exception e) {
			logger.error("ERROR :", e);
		}
		return pesan;
	}
	
	public Map cekPin(String pin)
	{
		CheckSum checkSum = new CheckSum();
		String produk="";
		if(pin.length()>10) produk="x";
		else produk = uwDao.selectCekPin(pin, 0);
		
		if(produk == null)produk = "x";
		
		if(!"x".equals(produk)){
			produk = produk.substring(1);
		}
		
		Map m =null;
		m = new HashMap();
		m.put("produk", produk);	
		
		return m;
	}
	
	public Map cekKartuPas(String no)
	{
		CheckSum checkSum = new CheckSum();
		String produk="";
		//produk = uwDao.selectCekKartuPas(no, 0);
		produk = uwDao.selectCekNoKartu(no, "PAS", 0);
		
		// Cek kartu baru (1 kartu bisa utk semua paket)
		if(produk == null) {
		    produk = uwDao.selectCekNoKartu(no, "CARD", 0);
		}
		
		if(produk == null)produk = "x";
		
		if(!"x".equals(produk)){
			produk = produk.substring(1);
		}
		
		Map m =null;
		m = new HashMap();
		m.put("produk", produk);	
		
		return m;
	}
	
	public Map cekPremiHcp(String no, String tgl_lahir)
	{
		CheckSum checkSum = new CheckSum();
		Double premi=new Double(0);
		
		Integer umur = 0;
		try{
			f_hit_umur umr = new f_hit_umur();
			
			SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
			SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
			SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
			Date sysdate = commonDao.selectSysdate();
			int tahun2 = Integer.parseInt(sdfYear.format(sysdate));
			int bulan2 = Integer.parseInt(sdfMonth.format(sysdate));
			int tanggal2 = Integer.parseInt(sdfDay.format(sysdate));
			
			if(tgl_lahir != null){
				int tanggal1 = Integer.parseInt(tgl_lahir.substring(0, 2));
				int bulan1 = Integer.parseInt(tgl_lahir.substring(3, 5));
				int tahun1 = Integer.parseInt(tgl_lahir.substring(6));
				umur=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
			}
		}catch(Exception e){
			umur = 0;
		}
		
		premi = uwDao.selectCekPremiHcp(no, umur);
		
		if(premi == null)new Double(0);
		
		Map m =null;
		m = new HashMap();
		m.put("premi", premi);	
		
		return m;
	}
	
	public Map qcOkControl(String spaj, String lus_id, String lde_id, String info)
	{
		Map m =null;
		if(!lde_id.equals("11")){
			m = new HashMap();
			m.put("peringatan", "Hanya User U/W yang bisa melakukan proses ini!");
			return m;
		}else{
			try{
				uwDao.insertMstPositionSpaj(lus_id, info, spaj, 0);
			}catch(Exception e){
				
			}
		}
		
		return m;
	}
	
	public Map approved(String spaj, Integer flag_approve, String lus_id, String flag, String info)
	{	// Kedepannya, penambahan flag digunakan sesuai kebutuhan.
		/*
		 * Flag 1 : Collection Proses Approved
		 * Flag 2 : New Business/UW Proses Approved
		 */
		Date nowDate = commonDao.selectSysdate();
		Map m =null;
		try{
			if(flag.equals("1")){
				uwDao.updateFlagAprove(spaj,flag_approve,"flag_approve_collection");
				uwDao.saveMstTransHistory(spaj, "tgl_approve_collection", nowDate, null, null);
				uwDao.insertMstPositionSpaj(lus_id, info, spaj, 0);
				m.put("peringatan", info);}
			else {
				uwDao.updateFlagAprove(spaj,flag_approve,"flag_approve_uw");
				uwDao.saveMstTransHistory(spaj, "tgl_approve_uw", nowDate, null, null);
				uwDao.insertMstPositionSpaj(lus_id, info, spaj, 0);
				m.put("peringatan", info);
			}
		}catch(Exception e){
				
		}
		return m;
	}
	
	public Map transferInbox(String spaj, String lus_id)
	{
		List <Map> inbox = null, inbox_uw = null, inbox_uw_new_prod = null;
		HashMap m = null;
		String mi_id = null;
		BigDecimal lspd_id = null;
		Date nowDate = commonDao.selectSysdate();
		try{
			inbox = uwDao.selectMstInbox(spaj, null);
			inbox_uw = uwDao.selectMstInbox(spaj, "1");
			inbox_uw_new_prod = uwDao.selectMstInbox(spaj, "3");
			if(inbox.size()>0){				
				
				if(!inbox_uw.isEmpty()){
					m = (HashMap) inbox_uw.get(0);
					mi_id =(String) m.get("MI_ID");
					lspd_id = (BigDecimal) m.get("LSPD_ID");
				}
				if(!inbox_uw_new_prod.isEmpty()){
					m = (HashMap) inbox_uw_new_prod.get(0);
					mi_id =(String) m.get("MI_ID");
					lspd_id = (BigDecimal) m.get("LSPD_ID");	
				}
				
				uwDao.updateMstInboxLspdId(mi_id, 201, lspd_id.intValue(), 2, null, null, null, null, 0);
				MstInboxHist mstInboxHist = new MstInboxHist(mi_id, lspd_id.intValue(), 201, null, null, null, Integer.parseInt(lus_id), nowDate, null,0,0);
				uwDao.insertMstInboxHist(mstInboxHist);
				uwDao.insertMstPositionSpaj(lus_id, "Transfer Dokumen dari U/W ke IMAGING", spaj, 0);
				m.put("peringatan", "Proses Transfer Dokumen ke Imaging berhasil dilakukan.");					
			
			}
	
		}catch(Exception e){
			m.put("peringatan", "Proses Transfer Dokumen ke Imaging tidak berhasil dilakukan.");
		}
		return m;
	}
	
	public Map listagen(String kodeagen, String lca_id,String spaj)
	{
		String nama_penutup="";
		String kode_regional="";
		String nama_regional="";
		String cbg = lca_id;
		Map m =null;
		m = new HashMap();
		if (kodeagen.equalsIgnoreCase("000000"))
		{
			if (cbg.equalsIgnoreCase("01") || cbg.equalsIgnoreCase("07") || cbg.equalsIgnoreCase("09"))
			{
				kode_regional=cbg.concat("0100");
			}else{
				kode_regional=cbg.concat("0000");
			}
			Map data = (HashMap) this.bacDao.selectregional(kode_regional);
			if (data!=null)
			{		
				nama_regional = (String)data.get("LSRG_NAMA");
			}	
			m.put("nama_penutup","");
			m.put("kode_regional",kode_regional);
			m.put("nama_regional",nama_regional);	
		}else{
			Integer jumlah_cancel;
			if (!spaj.equalsIgnoreCase(""))
			{
				jumlah_cancel = (Integer)this.bacDao.count_mst_cancel(spaj);
			}else{
				jumlah_cancel = new Integer(0);
			}
			Map data;
			if (jumlah_cancel.intValue() == 0)
			{
				data = (HashMap) this.bacDao.selectagenpenutup(kodeagen);
			}else{
				data = (HashMap) this.bacDao.selectagenpenutup_endors(kodeagen);
			}
			if (data!=null)
			{		
				nama_penutup=(String)data.get("NAMA");
				kode_regional=(String)data.get("REGIONID");
				nama_regional = (String)data.get("REGION");
			}
			m.put("nama_penutup",nama_penutup);
			m.put("kode_regional",kode_regional);
			m.put("nama_regional",nama_regional);			
		}
		return m;
	}
	
	public Map agen_rekruter_detil(String kodeagen, Integer kodepil)
	{
		String nama="";
		String acc="";
		BigDecimal namabank=null;
		String namabank2="";
		String jabatan="";
		Map m =null;
		m = new HashMap();
		if (kodepil == null)
		{
			kodepil=new Integer(0);
		}
		Map data =(HashMap) this.bacDao.selectagenrekrut(Integer.toString(kodepil), kodeagen);
		if (data != null)
		{
			nama = (String) data.get("NAMA");
			acc = (String) data.get("ACC");
			namabank = (BigDecimal) data.get("NAMABANK");
			namabank2 = (String) data.get("NAMABANK2");
			jabatan = (String) data.get("JABATAN");
		}
		m.put("nama", nama);
		m.put("id", kodeagen);
		m.put("acc", acc);
		m.put("namabank", namabank);
		m.put("namabank2", namabank2);
		m.put("jabatan", jabatan);

		return m;
	}
	
	public Map agen_rekruter_UM(String kodeagen){
		String nama="";
		Map m =null;
			m = new HashMap();
		Map data =(HashMap) this.bacDao.selectagenrekrutStruktur(kodeagen);
		if (data != null){nama = (String) data.get("NAMA");}
			m.put("nama", nama);
		return m;
	}
	public Map agen_rekruter_BM(String kodeagen){
		String nama="";
		Map m =null;
			m = new HashMap();
		Map data =(HashMap) this.bacDao.selectagenrekrutStruktur(kodeagen);
		if (data != null){nama = (String) data.get("NAMA");}
			m.put("nama", nama);
		return m;
	}
	public Map agen_rekruter_SBM(String kodeagen){
		String nama="";
		Map m =null;
			m = new HashMap();
		Map data =(HashMap) this.bacDao.selectagenrekrutStruktur(kodeagen);
		if (data != null){nama = (String) data.get("NAMA");}
			m.put("nama", nama);
		return m;
	}
	public Map agen_rekruter_RM(String kodeagen){
		String nama="";
		Map m =null;
			m = new HashMap();
		Map data =(HashMap) this.bacDao.selectagenrekrutStruktur(kodeagen);
		if (data != null){nama = (String) data.get("NAMA");}
			m.put("nama", nama);
		return m;
	}

	public List fungsi_ganti_cabang(Integer cabang){
		List list = new ArrayList();
		Map m = new HashMap();
		
		try{                            
					if(cabang.intValue()==1)
					{
						String indexCabang = "37,46,40,42,52,55,58,60";
						list = this.bacDao.selectLstAgenRegional_kues(indexCabang);
					}else if(cabang.intValue()==2)
					{
						String indexCabang = "37,52,60,67"; //permintaan Wenny 60 dan 67 di masukkan ke Agency
						list = this.bacDao.selectLstAgenAgency_kues(indexCabang);
					}else if(cabang.intValue()==3)
					{
						String lshd_id = "01";
						list = this.bacDao.selectLstHybridAJS_kues(lshd_id);
						
					}else if(cabang.intValue()==7)//Sebelumnya 4
					{
						String indexCabang = "42";
						list = this.bacDao.selectLstAgenWorksite_kues(indexCabang);
					}else{
							String indexCabang = "";
							list = this.bacDao.selectLstAgen_kues(indexCabang);
					}
		}
		
		catch (Exception e)
		{
			 logger.error("ERROR :", e);
		} 
		return list;
	}
	
	public List fungsi_ganti_posisi(Integer dist){
		List list = new ArrayList();
		Map m = new HashMap();
		try{
			if(dist == 0) dist = 2;
			list = this.bacDao.selectLstLevelDist(dist);
		}
		catch (Exception e){
			 logger.error("ERROR :", e);
		} 
		return list;
	}
	
	public Map listagenao(String kodeagen)
	{
		String nama_ao="";
		Map m =null;
		m = new HashMap();
		Map data = (HashMap) this.bacDao.selectagenao(kodeagen);
		if (data!=null)
		{		
			nama_ao=(String)data.get("nama");
		}
		m.put("nama_ao",nama_ao);
		return m;
	}
	
	public Map listagenpas(String kodeagen)
	{
		String nama_penutup="";
		String nama_regional="";
		Map m =null;
		m = new HashMap();
		Map data = (HashMap) this.bacDao.selectagenpenutup(kodeagen);
//		if (data!=null)
//		{		
//			nama_ao=(String)data.get("nama");
//		}
		if (data!=null)
		{		
			nama_penutup=(String)data.get("NAMA");
			nama_regional = (String)data.get("REGION");
		}
		m.put("nama_penutup",nama_penutup);
		m.put("nama_regional",nama_regional);	
		return m;
	}
	
	public Map listagenleader(String kodeagen)
	{
		String nama_leader="";
		Map m =null;
		m = new HashMap();
		nama_leader = this.bacDao.selectagenleader(kodeagen);
		m.put("nama_leader",nama_leader);
		return m;
	}
	
	public List cekmuamalat() {
		return this.bacDao.cekmuamalat();
	}
	
	public Integer mallnonESPAJ(String reg_spaj){
		Integer flag_non_espaj= 0;
		Map map=this.uwDao.selectDataUsulan(reg_spaj);
		Integer lsbsId=(Integer)map.get("LSBS_ID");
		Integer lsdbsNumber=(Integer)map.get("LSDBS_NUMBER");
		if(products.productMallLikeBSM(lsbsId, lsdbsNumber)){
			flag_non_espaj=1;
		}
		return flag_non_espaj;
	}
	
	public Map listrider(String kode_bisnis) {
		
		Map m=null;
		m = new HashMap();
		String kode_produk="";
		String number_produk="";
		Integer letak=new Integer(kode_bisnis.indexOf("X"));
		kode_produk=kode_bisnis.substring(0,letak.intValue()-1);
		number_produk=kode_bisnis.substring(letak.intValue()+1,kode_bisnis.length());	

		String nama_produk="";
		if (kode_produk.trim().length()==1)
		{
			nama_produk="produk_asuransi.n_prod_0"+kode_produk;	
		}else{
			nama_produk="produk_asuransi.n_prod_"+kode_produk;	
		}
		try{
			Class aClass = Class.forName( nama_produk );
			n_prod produk = (n_prod)aClass.newInstance();
			produk.setSqlMap(this.getBacDao().getSqlMapClient());
			
			produk.riderInclude(Integer.parseInt(number_produk));

			if (produk.indeks_rider_include>1)
			{
				produk.riderInclude(Integer.parseInt(number_produk));
				Integer jmlh_rider=new Integer(produk.indeks_rider_include);
				Integer[] kode_rider;
				Integer[] nomor_rider;
				String[] kode_plan;
				
				kode_rider = new Integer[jmlh_rider.intValue()+1];
				nomor_rider = new Integer[jmlh_rider.intValue()+1];
				kode_plan = new String[jmlh_rider.intValue()+1];
				
				for (int i =0 ; i< jmlh_rider.intValue();i++)
				{
					kode_rider[i] = new Integer(produk.rider_include[i]);
					produk.cek_rider_include(Integer.parseInt(number_produk),kode_rider[i].intValue(),0,0,0,0,0);
					nomor_rider[i] = new Integer(produk.nomor_rider_include);
					kode_plan[i] = Integer.toString(kode_rider[i].intValue())+"~X"+Integer.toString(nomor_rider[i].intValue());
				}
				m.put("jmlh_rider",jmlh_rider);
				m.put("kode_rider",kode_rider);
				m.put("nomor_rider",nomor_rider);
				m.put("kode_plan",kode_plan);
			}else{
				m.put("jmlh_rider","0");
			}
				
		}
		catch (Exception e)
		{
			 logger.error("ERROR :", e);
		} 

		return m;
			
	}
	
	public Map listcara_bayar(String kode_bisnis, String tanggal, String tanggal_pp, String tanggal_ttg,Integer ins_period2) {
		
		Map m=null;
		String kode_produk="";
		String number_produk="";
		Integer letak=new Integer(kode_bisnis.indexOf("X"));
		kode_produk=kode_bisnis.substring(0,letak.intValue()-1);
		number_produk=kode_bisnis.substring(letak.intValue()+1,kode_bisnis.length());
		String nama_produk="";
		Date tanggal_beg_date=null;
		Date tanggal_lahir_ttg=null;
		Date tanggal_lahir_pp = null;

		try {
			tanggal_beg_date = defaultDateFormat.parse(tanggal);
			tanggal_lahir_pp = defaultDateFormat.parse(tanggal_pp);
			tanggal_lahir_ttg = defaultDateFormat.parse(tanggal_ttg);
		} catch (ParseException e1) {
			logger.error("ERROR :", e1);
		}
		
		if (kode_produk.trim().length()==1)
		{
			nama_produk="produk_asuransi.n_prod_0"+kode_produk;	
		}else{
			nama_produk="produk_asuransi.n_prod_"+kode_produk;	
		}
		try{
			if(!kode_produk.equals("0")){
			Class aClass = Class.forName( nama_produk );
			n_prod produk = (n_prod)aClass.newInstance();
			produk.setSqlMap(this.getBacDao().getSqlMapClient());
			produk.of_set_bisnis_no(Integer.parseInt(number_produk));
			//kurs
			Integer jmlh_kurs =new Integer(produk.indeks_is_forex);
			if((kode_produk.equalsIgnoreCase("224") && number_produk.equalsIgnoreCase("3")) || //helpdesk [133899], produk B Smile Protection Syariah 224-3 cuma ada idr
				(kode_produk.equalsIgnoreCase("134") && number_produk.equalsIgnoreCase("13"))) //helpdesk [142003] produk baru Smart Platinum Link RPUL BEL (134-13)
				jmlh_kurs = 1;
			m = new HashMap();
			String[] id_kurs;
			String[] nama_kurs;
			id_kurs= new String[jmlh_kurs.intValue()];
			nama_kurs = new String[jmlh_kurs.intValue()];
			for (int i=0 ; i<jmlh_kurs.intValue();i++)
			{
				id_kurs[i]=produk.is_forex[i];
				nama_kurs[i]=select_detilkurs(id_kurs[i]);
			}
			m.put("kurs",id_kurs);
			m.put("nama_kurs",nama_kurs);
			
			Integer flag_platinum = new Integer(produk.flag_platinumlink);
			m.put("flag_platinum",flag_platinum);
			
			Integer flag_cuti_premi = new Integer(produk.flag_cuti_premi);
			m.put("flag_cuti_premi", flag_cuti_premi);
			
			Integer flag_class = new Integer(produk.flag_class);
			//cara bayar
		
			produk.of_set_bisnis_no(Integer.parseInt(number_produk));
			Integer jmlh_cr_byr = new Integer(produk.indeks_li_pmode);
	
			Integer[] cr_byr;
			String[] nama_crbyr;
			cr_byr = new Integer[jmlh_cr_byr.intValue()];
			nama_crbyr = new String[jmlh_cr_byr.intValue()];

			for (int i=1 ; i<jmlh_cr_byr.intValue();i++)
			{
				cr_byr[i]=new Integer(produk.li_pmode[i]);
				nama_crbyr[i] = select_detilcrbayar(cr_byr[i]);
			}
			m.put("cr_byr",cr_byr);
			m.put("nama_crbyr",nama_crbyr);
			m.put("flag_class",flag_class);
			
			Integer[] kd_packet;
			String[] nama_packet;
			Integer total_packet = bacDao.select_listPaket(kode_produk,number_produk).size();
			kd_packet = new Integer[total_packet];
			nama_packet = new String[total_packet];
			
			for (int i=0 ; i<total_packet;i++)
			{
				Map mapPacket = bacDao.select_listPaket(kode_produk,number_produk).get(i);
				kd_packet[i]=((BigDecimal) mapPacket.get("FLAG_PACKET")).intValue();
				nama_packet[i] = (String) mapPacket.get("NAMA_PACKET");
			}
			m.put("kd_packet",kd_packet);
			m.put("nama_packet",nama_packet);
			
			//umur
			Integer tanggal1= tanggal_beg_date.getDate();
			Integer bulan1= tanggal_beg_date.getMonth() + 1;
			Integer tahun1 = tanggal_beg_date.getYear() + 1900;	
			
			Integer tanggal2=tanggal_lahir_ttg.getDate();
			Integer bulan2=tanggal_lahir_ttg.getMonth() + 1 ;
			Integer tahun2=tanggal_lahir_ttg.getYear() + 1900;

			Integer tanggal3=tanggal_lahir_ttg.getDate();
			Integer bulan3=tanggal_lahir_ttg.getMonth() + 1;
			Integer tahun3=tanggal_lahir_ttg.getYear() + 1900;		
						
			//hit umur ttg, pp 
			f_hit_umur umr= new f_hit_umur();
			Integer li_umur_ttg = new Integer(umr.umur(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1));
			Integer li_umur_pp = new Integer(umr.umur(tahun3,bulan3,tanggal3,tahun1,bulan1,tanggal1));
			
			if(li_umur_ttg==0 && "208".equals(kode_produk)){
				int hari1=umr.hari1(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);
				if (hari1 >= 183){
					li_umur_ttg=1;
				}
			}
			
			produk.of_set_usia_tt(li_umur_ttg.intValue());
			produk.of_set_usia_pp(li_umur_pp.intValue());
			produk.of_set_age();
			int age=produk.ii_age;
			m.put("umurttg",li_umur_ttg);
			m.put("umurpp",li_umur_pp);
			Integer ins_period=0;
			if( kode_produk.equals("177") || kode_produk.equals("207") || kode_produk.equals("208") || kode_produk.equals("212") || kode_produk.equals("223") || (kode_produk.equals("186") && !number_produk.equals("3"))){
				ins_period= ins_period2;
			}else{
				ins_period= new Integer(produk.of_get_conperiod(Integer.parseInt(number_produk)));
			}
			 
			if (kode_produk.equalsIgnoreCase("63") || kode_produk.equalsIgnoreCase("173"))
			{
				ins_period = new Integer(25);
			}
			
			//20180905-Chandra  Konversi Simas prima (stand alone) masa asuransi dari 4 tahun menjadi 1 tahun
			if(kode_produk.equals("142") && ( Integer.parseInt(number_produk) == 2 ||  Integer.parseInt(number_produk) == 1) ){
				m.put("lama_tanggung", 1);
			}else{
				//default
				m.put("lama_tanggung",Integer.toString(ins_period.intValue()));
			}

			/*produk.of_set_begdate(tahun1, bulan1, tanggal1);
			int tanggal_end =produk.idt_end_date.getTime().getDate();
			int bulan_end =produk.idt_end_date.getTime().getMonth()+1;
			int tahun_end =produk.idt_end_date.getTime().getYear()+1900;
			
			m.put("tanggal_end",new Integer(tanggal_end));
			m.put("bulan_end",new Integer(bulan_end));
			m.put("tahun_end",new Integer(tahun_end));*/
				
			//lama_bayar
			int pay_period=0;
			if(kode_produk.equals("177") ||  kode_produk.equals("207") || kode_produk.equals("212") || kode_produk.equals("223") || (kode_produk.equals("186") && !number_produk.equals("3"))){
				pay_period=ins_period2;
			}else{
				if((kode_produk.equals("219") && (Integer.parseInt(number_produk) >= 5 && Integer.parseInt(number_produk) <= 8)) ) { //helpdesk [138638] produk baru SPP Syariah (219-5~8)
					pay_period = 5;
				} else {
					pay_period= produk.of_get_payperiod();
				}
			}
			
			//helpdesk [142003] produk baru Smart Platinum Link RPUL BEL (134-13)
			if(kode_produk.equals("134") && Integer.parseInt(number_produk) == 13){
				pay_period = ins_period;
			}
			 
			m.put("lama_bayar",new Integer(pay_period));
			
			int jumlah_rider = 	produk.indeks_rider_list;
			Integer[] rider;
			rider = new Integer[jumlah_rider];
			for (int i = 0 ; i <jumlah_rider; i++)
			{
				rider[i] = new Integer(produk.ii_rider[i]);
			}
			m.put("rider",rider);
			}
		}
		catch (Exception e)
		{
			 logger.error("ERROR :", e);
		} 
		return m;
	}

	public List list_rider(String kode_bisnis, String detil_bisnis) {
		List b= new ArrayList();
		Map m=null;

		String nama_produk="";
		
		if (kode_bisnis.trim().length()==1)
		{
			nama_produk="produk_asuransi.n_prod_0"+kode_bisnis;	
		}else{
			nama_produk="produk_asuransi.n_prod_"+kode_bisnis;	
		}
		
		try{
			Class aClass = Class.forName( nama_produk );
			n_prod produk = (n_prod)aClass.newInstance();
			produk.setSqlMap(this.getBacDao().getSqlMapClient());
			int letak = detil_bisnis.indexOf("X");
			String lsdbs_number = detil_bisnis.substring(letak+1,detil_bisnis.length());
			String lsdbs_number183 = detil_bisnis.toString();
			m = new HashMap();
	
			int jumlah_rider = 	produk.indeks_rider_list;
			Integer rider;
			if(Integer.parseInt(kode_bisnis)==191)
				//if(products.cerdas(kode_bisnis.toString()))
			{
				for (int i = 0 ; i <jumlah_rider; i++)
				{
					rider = new Integer(produk.ii_rider[i]);
					int lsdbs_from;
					int lsdbs_to;
					if(rider.intValue()==811){
						b.addAll(this.bacDao.select_spesifik_produk_rider(rider, 1, 10));
					}else if(rider.intValue()==813){
						b.addAll(this.bacDao.select_spesifik_produk_rider(rider, 8, 8)); // *CI new
						//					}else if(rider.intValue()==814){
						//						b.addAll(this.bacDao.select_spesifik_produk_rider(rider, 4, 5));
					}else if(rider.intValue()==818){
						b.addAll(this.bacDao.select_spesifik_produk_rider(rider, 2, 2));
					}else if(rider.intValue()==810){
						b.addAll(this.bacDao.select_spesifik_produk_rider(rider, 3, 3));
					}else if(rider.intValue()==823 || rider.intValue()==825){
						b.addAll(this.bacDao.select_spesifik_produk_rider(rider, 1, 105));
					}else{
						b.addAll(this.bacDao.select_detilproduk_rider(rider));
					}
				}	
			}else if(products.powerSave(kode_bisnis.toString())){
				for (int i = 0 ; i <jumlah_rider; i++)
				{
					rider = new Integer(produk.ii_rider[i]);
					int lsdbs_from;
					int lsdbs_to;
					if(rider.intValue()==813){
						if(products.bsm(kode_bisnis.toString(), lsdbs_number.toString())){
							b.addAll(this.bacDao.select_spesifik_produk_rider_bsm(rider, 7, 7));
						}else{
							b.addAll(this.bacDao.select_spesifik_produk_rider(rider, 4, 5));
						}
					}else if(rider.intValue()==818){
						if(products.bsm(kode_bisnis.toString(), lsdbs_number.toString())){
							b.addAll(this.bacDao.select_spesifik_produk_rider_bsm(rider, 6, 6));
						}else{
							b.addAll(this.bacDao.select_spesifik_produk_rider(rider, 3, 4));	
						}
					}else if(rider.intValue()==819){
						b.addAll(this.bacDao.select_spesifik_produk_rider(rider, 281, 380));
					}else if(rider.intValue()==823 || rider.intValue()==825){
						b.addAll(this.bacDao.select_spesifik_produk_rider_bsm(rider, 1, 105));
					}else if(rider.intValue()==811){
						if(products.bsm(kode_bisnis.toString(), lsdbs_number.toString())){
						}else{
							b.addAll(this.bacDao.select_detilproduk_rider(rider));
						}
					}else{
						b.addAll(this.bacDao.select_detilproduk_rider(rider));
					}

				}
			}else if(products.bsm(kode_bisnis.toString(), lsdbs_number.toString())){
				for (int i = 0 ; i <jumlah_rider; i++)
				{
					rider = new Integer(produk.ii_rider[i]);
					int lsdbs_from;
					int lsdbs_to;
					if(rider.intValue()==810){
						if (kode_bisnis.equals("120")){
							b.addAll(this.bacDao.select_spesifik_produk_rider_bsm(rider, 1 ,3));
						}else{
							b.addAll(this.bacDao.select_detilproduk_rider(rider));
						}
					}else if(rider.intValue()==813){
						if (kode_bisnis.equals("120")){
							b.addAll(this.bacDao.select_spesifik_produk_rider_bsm(rider, 8, 8));
						}else{
							b.addAll(this.bacDao.select_spesifik_produk_rider_bsm(rider, 7, 7));
						}
					}else if(rider.intValue()==815){
						b.addAll(this.bacDao.select_detilproduk_rider(rider));//(Deddy)- Sementara dibuka dulu khusus BSM, tunggu instruksi dari Aktuari/Auditya apabila rate untuk 828 sudah ada untuk semua jenis payor, 815 dan 817 akan ditutup.
					}else if(rider.intValue()==816){
						if (kode_bisnis.equals("120")){
							b.addAll(this.bacDao.select_spesifik_produk_rider_bsm(rider, 2 ,3));
						}else{
							b.addAll(this.bacDao.select_detilproduk_rider(rider));
						}
					}else if(rider.intValue()==818){
						if (kode_bisnis.equals("120")){
							b.addAll(this.bacDao.select_spesifik_produk_rider_bsm(rider, 2, 2));
						}else{
							b.addAll(this.bacDao.select_spesifik_produk_rider_bsm(rider, 6, 6));
						}
					}else if(rider.intValue()==819){
						if (kode_bisnis.equals("120")){
							b.addAll(this.bacDao.select_spesifik_produk_rider_bsm(rider, 141, 270));
						}else{
							b.addAll(this.bacDao.select_spesifik_produk_rider_bsm(rider, 431, 530));
						}						
					}else if(rider.intValue()==823 || rider.intValue()==825){
						if (kode_bisnis.equals("120")){
							b.addAll(this.bacDao.select_detilproduk_rider(rider));
						}else{
							b.addAll(this.bacDao.select_spesifik_produk_rider_bsm(rider, 1, 15));
						}				
						
					}else{
						b.addAll(this.bacDao.select_detilproduk_rider(rider));
					}
				}
			}else if(Integer.parseInt(kode_bisnis)==214)
				//Produk Provestara : Ridhaal (5/20/2016)
			{
				String lsdbs_name_sub = ""; //produk.lsdbs_name_sub;
				
				if (lsdbs_number.equals("1")){
					lsdbs_name_sub = "GOLD";
				}else {
					lsdbs_name_sub = "PLATINUM";
				}
				
				for (int i = 0 ; i <jumlah_rider; i++)
				{
					rider = new Integer(produk.ii_rider[i]);
					b.addAll(this.bacDao.select_spesifik_produk_rider_namesub(rider, lsdbs_name_sub));
					
				}
			}else if(Integer.parseInt(lsdbs_number) > 45 && Integer.parseInt(lsdbs_number) < 61 && kode_bisnis.equals("183")){
					for (int i = 0 ; i <jumlah_rider; i++){
						rider = new Integer(produk.ii_rider[i]);
						if(rider.intValue()==823){
							if(lsdbs_number.equals("46")){
								b.addAll(this.bacDao.select_spesifik_produk_rider_183(rider, "PLAN A"));
							}else if(lsdbs_number.equals("47")){
								b.addAll(this.bacDao.select_spesifik_produk_rider_183(rider, "PLAN B"));
							}else if(lsdbs_number.equals("48")){
								b.addAll(this.bacDao.select_spesifik_produk_rider_183(rider, "PLAN C"));
							}else if(lsdbs_number.equals("49")){
								b.addAll(this.bacDao.select_spesifik_produk_rider_183(rider, "PLAN D"));
							}else if(lsdbs_number.equals("50")){
								b.addAll(this.bacDao.select_spesifik_produk_rider_183(rider, "PLAN E"));
							}else if(lsdbs_number.equals("51")){
								b.addAll(this.bacDao.select_spesifik_produk_rider_183(rider, "PLAN F"));
							}else if(lsdbs_number.equals("52")){
								b.addAll(this.bacDao.select_spesifik_produk_rider_183(rider, "PLAN G"));
							}else if(lsdbs_number.equals("53")){
								b.addAll(this.bacDao.select_spesifik_produk_rider_183(rider, "PLAN H"));
							}else if(lsdbs_number.equals("54")){
								b.addAll(this.bacDao.select_spesifik_produk_rider_183(rider, "PLAN I"));
							}else if(lsdbs_number.equals("55")){
								b.addAll(this.bacDao.select_spesifik_produk_rider_183(rider, "PLAN J"));
							}else if(lsdbs_number.equals("56")){
								b.addAll(this.bacDao.select_spesifik_produk_rider_183(rider, "PLAN K"));
							}else if(lsdbs_number.equals("57")){
								b.addAll(this.bacDao.select_spesifik_produk_rider_183(rider, "PLAN L"));
							}else if(lsdbs_number.equals("58")){
								b.addAll(this.bacDao.select_spesifik_produk_rider_183(rider, "PLAN M"));
							}else if(lsdbs_number.equals("59")){
								b.addAll(this.bacDao.select_spesifik_produk_rider_183(rider, "PLAN N"));	
							}else if(lsdbs_number.equals("60")){
								b.addAll(this.bacDao.select_spesifik_produk_rider_183(rider, "PLAN O"));
							}
						}
					}	
			}else if(kode_bisnis.equals("134") && lsdbs_number.equals("6")){
				for (int i = 2 ; i <=3; i++){
					rider = new Integer(produk.ii_rider[i]);
					b.addAll(this.bacDao.select_detilproduk_rider(rider));
				}
			}else if(kode_bisnis.equals("208")){
				if(lsdbs_number.equals("1")||lsdbs_number.equals("2")||lsdbs_number.equals("3")||lsdbs_number.equals("4")){
					for (int i = 0 ; i <=2; i++){
						rider = new Integer(produk.ii_rider[i]);
						b.addAll(this.bacDao.select_detilproduk_rider(rider));
					}	
				}else if (!lsdbs_number.equals("21") && !lsdbs_number.equals("22") && !lsdbs_number.equals("23") && !lsdbs_number.equals("24")) { // Smart PLan Protection tidak ada Smile Baby
					rider = new Integer(produk.ii_rider[3]);
					b.addAll(this.bacDao.select_detilproduk_rider(rider));
				}
			}else {
				if(Integer.parseInt(kode_bisnis)==121){
					Integer kode_number = Integer.parseInt(lsdbs_number);
					if(kode_number>=3){
						jumlah_rider = 0;
					}
				}
				//				if(Integer.parseInt(kode_bisnis)==182){
				//					Integer kode_number = Integer.parseInt(lsdbs_number);
				//					if(kode_number>=7){
				//						jumlah_rider = 0;
				//					}
				//				}
				for (int i = 0 ; i <jumlah_rider; i++)
				{
					rider = new Integer(produk.ii_rider[i]);
					// Andhika(09 July 2012) - Kode 815-(4,5,6) non aktif 
					if(rider.intValue()==815){
						b.addAll(this.bacDao.select_spesifik_produk_rider_bsm(rider, 4, 6));
					}
					else{
						b.addAll(this.bacDao.select_detilproduk_rider(rider));
					}
				}
			}
			
			//Deddy (18 Juni 2012) - Khusus rider 815, hanya diaktifkan yang SPOUSE 60 TPD/DEATH (Untuk semua plan)
			/*if(products.bsm(kode_bisnis.toString(), lsdbs_number.toString())){
				for (int i = 0 ; i <jumlah_rider; i++)
				{
					rider = new Integer(produk.ii_rider[i]);
					int lsdbs_from;
					int lsdbs_to;
					if(rider.intValue()==813){
						b.addAll(this.bacDao.select_spesifik_produk_rider_bsm(rider, 7, 7));
					}else if(rider.intValue()==815){
						b.addAll(this.bacDao.select_detilproduk_rider(rider));//(Deddy)- Sementara dibuka dulu khusus BSM, tunggu instruksi dari Aktuari/Auditya apabila rate untuk 828 sudah ada untuk semua jenis payor, 815 dan 817 akan ditutup.
					}else if(rider.intValue()==818){
						b.addAll(this.bacDao.select_spesifik_produk_rider_bsm(rider, 6, 6));
					}else if(rider.intValue()==819){
						b.addAll(this.bacDao.select_spesifik_produk_rider_bsm(rider, 431, 530));
					}else if(rider.intValue()==823 || rider.intValue()==825){
						b.addAll(this.bacDao.select_spesifik_produk_rider_bsm(rider, 1, 15));
					}else{
						b.addAll(this.bacDao.select_detilproduk_rider(rider));
					}
				}
			}*/
			
		}
		catch (Exception e)
		{
			 logger.error("ERROR :", e);
		} 
		return b;
	}
	
	public Map tanggalenddate(String tanggal_beg, Integer lama_bayar,String kode_bisnis,String tanggal_pp, String tanggal_ttg) 
	{

		Date tanggal_beg_date=null;
		Date tanggal_end_date =null;
		String tgl_end_date=null;
		Date tanggal_lahir_ttg=null;
		Date tanggal_lahir_pp = null;

		try {
			tanggal_beg_date = defaultDateFormat.parse(tanggal_beg);
			tanggal_lahir_pp = defaultDateFormat.parse(tanggal_pp);
			tanggal_lahir_ttg = defaultDateFormat.parse(tanggal_ttg);
		} catch (ParseException e1) {
			logger.error("ERROR :", e1);
		}		
				
		Map m=null;
		if (tanggal_beg_date != null)
		{
			String kode_produk="";
			String number_produk="";
			Integer letak=new Integer(kode_bisnis.indexOf("X"));
			kode_produk=kode_bisnis.substring(0,letak.intValue()-1);
			number_produk=kode_bisnis.substring(letak.intValue()+1,kode_bisnis.length());
			String nama_produk="";

			//umur
			int tanggal1= tanggal_beg_date.getDate();
			int bulan1= tanggal_beg_date.getMonth() + 1;
			int tahun1 = tanggal_beg_date.getYear() + 1900;	
			int tanggal;
			int bulan;
			int tahun;
			
			if (kode_produk.trim().length()==1)
			{
				nama_produk="produk_asuransi.n_prod_0"+kode_produk;	
			}else{
				nama_produk="produk_asuransi.n_prod_"+kode_produk;	
			}
			try{
				m = new HashMap();
				Class aClass = Class.forName( nama_produk );
				n_prod produk = (n_prod)aClass.newInstance();			
				produk.setSqlMap(this.getBacDao().getSqlMapClient());
				produk.of_set_bisnis_no(Integer.parseInt(number_produk));
				int tanggal2=tanggal_lahir_ttg.getDate();
				int bulan2=tanggal_lahir_ttg.getMonth() + 1 ;
				int tahun2=tanggal_lahir_ttg.getYear() + 1900;

				int tanggal3=tanggal_lahir_ttg.getDate();
				int bulan3=tanggal_lahir_ttg.getMonth() + 1;
				int tahun3=tanggal_lahir_ttg.getYear() + 1900;		
							
				//hit umur ttg, pp 
				f_hit_umur umr= new f_hit_umur();
				Integer li_umur_ttg = new Integer(umr.umur(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1));
				Integer li_umur_pp = new Integer(umr.umur(tahun3,bulan3,tanggal3,tahun1,bulan1,tanggal1));
				
				if(li_umur_ttg==0 && "208".equals(kode_produk)){
					int hari1=umr.hari1(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);
					if (hari1 >= 183){
						li_umur_ttg=1;
					}
				}
				
				produk.of_set_usia_tt(li_umur_ttg.intValue());
				produk.of_set_usia_pp(li_umur_pp.intValue());
				produk.of_set_age();
				Integer ins_period=0;
				if(kode_produk.equals("177") || kode_produk.equals("207") || kode_produk.equals("208") || kode_produk.equals("212") || kode_produk.equals("223") || kode_produk.equals("186")){
					ins_period = lama_bayar;
					produk.ii_contract_period = ins_period;
				}else{
					ins_period = new Integer(produk.of_get_conperiod(Integer.parseInt(number_produk)));
				}
				
				produk.of_set_begdate(tahun1,bulan1,tanggal1);
				tanggal= (produk.idt_end_date.getTime().getDate());
				bulan= (produk.idt_end_date.getTime().getMonth()+1);
				tahun= (produk.idt_end_date.getTime().getYear()+1900);	
				String tgl_end=Integer.toString(tanggal);
				String bln_end = Integer.toString(bulan);
				String thn_end = Integer.toString(tahun);
				if ((tgl_end.equalsIgnoreCase("0")==true) || (bln_end.equalsIgnoreCase("0")==true) || (thn_end.equalsIgnoreCase("0")==true))
				{
					tgl_end_date=null;
					tanggal_end_date =null;
				}else{
					tgl_end_date = FormatString.rpad("0",tgl_end,2)+"/"+FormatString.rpad("0",bln_end,2)+"/"+thn_end;
					tanggal_end_date = defaultDateFormat.parse(tgl_end_date);
				}
				m.put("tanggal_end_date",tanggal_end_date);
				m.put("tgl_end_date",tgl_end_date);
			}
			catch (Exception e)
			{
				 logger.error("ERROR :", e);
			} 	
		}	

		return m;
	}
	
	public Map listagenba(String kodeao) 
	{
		String nama_ao="";
		Map m=null;
		m = new HashMap();
		Map data = (HashMap) this.bacDao.selectagenao(kodeao);
		if (data!=null)
		{		
			nama_ao=(String)data.get("nama");
		}
		m.put("nama_ao",nama_ao);
		return m;
	}
	
	public Map listnamapp(String nopol) 
	{
		String nama_pemegang="";
		Map m = new HashMap();
		Map n = new HashMap();
	    m=(HashMap) this.bacDao.selectnamapp(nopol);	
		if (m!=null)
		{		
			nama_pemegang = (String) m.get("MCL_FIRST");
		}
		n.put("nama_pemegang",nama_pemegang);
		return n;
	}
	
	public Map listcollect(String kode , String nama)
	{
		Map m=null;
		m = new HashMap();
		if (nama.equalsIgnoreCase("referral"))
		{
			Map data = (HashMap) this.bacDao.select_referrer(kode);
			if (data!=null)
			{
				m.putAll(data);
			}
		}else if (nama.equalsIgnoreCase("bii"))
			{
				Map data = (HashMap) this.bacDao.select_bii(kode);
				if (data!=null)
				{
					m.putAll(data);
				}
			}else if (nama.equalsIgnoreCase("region"))
				{
					Map data = (HashMap) this.bacDao.selectregion(kode);
					if (data!=null)
					{
						m.putAll(data);
					}
					Integer data1 = (Integer) this.bacDao.selectmax_peserta(kode);
					if (data1 == null)
					{
						data1 = new Integer(0);
					}
					m.put("jml_peserta",data1);
				
				}
		Integer data2 = (Integer) this.uwDao.selectFlagQuestionare(kode);
		Integer data3 = (Integer) this.uwDao.selectCallDate(kode);
		Integer syariah = (Integer) this.uwDao.selectSyariah(kode);
		String flag_spaj = this.bacDao.selectMspoFlagSpaj(kode);
		Integer flag_produk = this.uwDao.selectProdukNonKuesioner(kode);
		
		m.put("quest", data2);
		m.put("calldate", data3);
		m.put("syariah", syariah);
		m.put("flag_spaj",flag_spaj);
		m.put("flag_produk",flag_produk);
		return m;
	}
	
	public String selectMstInsuredMsteFlagKomisi (String spaj){
		String info="";
		Integer flag=this.uwDao.selectMstInsuredMsteFlagKomisi(spaj);
		if(flag==null)
			flag=0;
		if(flag==0){
			info="Belum Input TTP (Transfer TTP)";
		}else if(flag==1){
			info="TTP balikan dari agency support.";
		}else if(flag==2){
			info="TTP Telah di input dalam bentuk fax.";
		}
		return info;
	}
	/**
	 * Dian natalia
	 * u/ pengecekan rekening tabungan agen
	 * @param spaj
	 * @return
	 */
	
	public String cekRekAgen(String spaj){
		return uwDao.cekRekAgen2(spaj);
	}

	/**
	 * Dian natalia
	 * u/cari alamat emai cabang
	 * @param lca_id
	 * @return
	
	
	public List emailCab(String lca_id){
		return uwDao.selectEmailCabang(lca_id);
	}
	 */
	public String selectKodeBisnis(String spaj){
		String asm=null;
		Map map=this.uwDao.selectDataUsulan(spaj);
		Integer lsbsId=(Integer)map.get("LSBS_ID");
		if(lsbsId.intValue()==161)
			asm="<script>formpost.btn_simultan.disabled=true</script>";
		return asm;
	}
	
	public String tambahTahunEndDate(String sBegDate,String sEndDate,int flag){
		String output;
		if(flag==1){
			int dd,mm,yy;
			dd=Integer.parseInt(sBegDate.substring(0,2));
			mm=Integer.parseInt(sBegDate.substring(3,5));
			yy=Integer.parseInt(sBegDate.substring(6,10));
			
			Calendar calAwal=new GregorianCalendar(yy,mm-1,dd);
			Date begDate=calAwal.getTime();
			Date endDate=FormatDate.add(begDate, Calendar.YEAR, 1);
		}
		output="<script>inputDate('endDate', '"+sEndDate+"', false);</script>";
		return output;
	}
	
	public Map hitung_umur (String tanggal_lahir)
	{
		Map m = new HashMap();
		Integer umur = new Integer(0);
		if (tanggal_lahir != null)
		{
			Integer tanggal = Integer.parseInt(tanggal_lahir.substring(0,2));
			Integer bulan =Integer.parseInt(tanggal_lahir.substring(3,5));
			Integer tahun = Integer.parseInt(tanggal_lahir.substring(6,10));
			Calendar tgl_sekarang = Calendar.getInstance(); 
			Integer li_curr_day   = tgl_sekarang.get(Calendar.DAY_OF_MONTH);
			Integer li_curr_month = tgl_sekarang.get(Calendar.MONTH)+1;
			Integer li_curr_year  = tgl_sekarang.get(Calendar.YEAR);
			f_hit_umur umr= new f_hit_umur();
			umur=  umr.umur(tahun,bulan,tanggal,li_curr_year,li_curr_month,li_curr_day);
		}
		m.put("umur",umur);
		return m;
	}
	
	public Map lsSelectAgent(String kodeagen)throws DataAccessException{
		String nm_penutup="";
		List list=this.basDao.lsSelectAgent(kodeagen);
		Map data = (HashMap)list.get(0);
		return data;
	}
	
	public Integer CountMAKesehatan(String reg_spaj) throws DataAccessException{
		List<MedQuest> mq = uwDao.selectMedQuest(reg_spaj,null);
		return mq.size();
	}
	
	public String ValidateESpaj(String reg_spaj,String lus_id) throws DataAccessException{
		String pesan = "";
		//validasi untuk pengisian questionare
		List<MedQuest> mq = uwDao.selectMedQuest(reg_spaj,null);
		if(mq.size()<=0){
			pesan ="Silakan menginput questionare medical kesehatan terlebih dahulu";
		}
		
		Pemegang dataPemegang = bacDao.selectpp(reg_spaj);
		
		//Ada dua validasi untuk compare dgn data di Proposal yg diinput User.
		//1. Perbedaan UP di proposal dengan UP di input SPAJ
		//2. Perbedaan di Rider yg diinput antara proposal dengan input SPAJ.
		if(lus_id.equals("2326")){
			Datausulan planUtamaProposal = bacDao.selectMstProposal(dataPemegang.getMspo_plan_provider());
			Datausulan planUtamaBac = bacDao.selectDataUsulanutama(reg_spaj);
			if(planUtamaProposal.getMspr_tsi().compareTo(planUtamaBac.getMspr_tsi()) !=0){
				pesan = "UP yang dimasukkan di proposal sebesar "+planUtamaProposal.getLku_symbol() +FormatNumber.convertToTwoDigit(new BigDecimal(planUtamaProposal.getMspr_tsi()) )+"sedangkan UP di input SPAJ sebesar "+planUtamaBac.getLku_symbol() +FormatNumber.convertToTwoDigit(new BigDecimal(planUtamaBac.getMspr_tsi()) )+".Silakan proses Ulang Proposal terlebih dahulu apabila terjadi perubahan UP.";
			}
			
			List<Datausulan> planRiderProposal =  bacDao.selectMstProposalRider(dataPemegang.getMspo_plan_provider());
			List<Datarider> planRiderBac =  planUtamaBac.getDaftaRider();
			if(planRiderProposal.size()!= planRiderBac.size()){
				pesan="Terdapat perbedaan Jumlah rider antara inputan di Proposal sebanyak "+planRiderProposal.size()+" rider, sedangkan di inputan SPAJ sebanyak "+planRiderBac.size()+"Silahkan dicrosscek lagi sebelum Print E-SPAJ";
			}else{
				if(planRiderProposal.size()>0 && planRiderBac.size()>0){
					String tampungRiderProposal = "";
					String tampungRiderBac = "";
					for(int i=0;i<planRiderProposal.size();i++){
						if(i==planRiderProposal.size()-1){
							tampungRiderProposal+=planRiderProposal.get(i).getPlan_rider();
						}else{
							tampungRiderProposal+=planRiderProposal.get(i).getPlan_rider()+",";
						}
					}
					for(int j=0;j<planRiderBac.size();j++){
						Datarider riderBac = (Datarider)planRiderBac.get(j);
						if(j==planRiderBac.size()-1){
							tampungRiderBac+=riderBac.getPlan_rider();
						}else{
							tampungRiderBac+=riderBac.getPlan_rider()+",";
						}
					}
					
					//Comparing dari sisi inputan rider proposal
					for(int countBac=0; countBac<planRiderBac.size();countBac++){
						Datarider riderBac = (Datarider)planRiderBac.get(countBac);
						if(tampungRiderProposal.indexOf(riderBac.getPlan_rider()) <0){//jika rider inputan SPAJ tidak ada di List Rider Proposal
							List tampungBac = bacDao.select_spesifik_produk_rider(riderBac.getLsbs_id(), riderBac.getLsdbs_number(), riderBac.getLsdbs_number());
							Map map = (Map) tampungBac.get(0);
							String nama_rider = (String)map.get("lsdbs_name");
							pesan="Rider "+nama_rider+" yang diinput di SPAJ tidak ada di inputan Proposal,Silakan dicek & disamakan antara proposal dan SPAJ.";
						}
					}
					
					//Comparing dari sisi inputan rider bac
					for(int countProposal=0; countProposal<planRiderProposal.size();countProposal++){
						Datausulan riderProposal = (Datausulan)planRiderProposal.get(countProposal);
						if(tampungRiderBac.indexOf(riderProposal.getPlan_rider()) <0){//jika rider inputan SPAJ tidak ada di List Rider Input SPAJ
							List tampungBac = bacDao.select_spesifik_produk_rider(riderProposal.getLsbs_id(), riderProposal.getLsdbs_number(), riderProposal.getLsdbs_number());
							Map map = (Map) tampungBac.get(0);
							String nama_rider = (String)map.get("lsdbs_name");
							pesan="Rider "+nama_rider+" yang diinput di Proposal tidak ada di inputan SPAJ,Silakan dicek & disamakan antara proposal dan SPAJ.";
						}
					}
				}
			}
		}
		return pesan;
	}
	
	public Integer CountPeserta(String reg_spaj) throws DataAccessException{
		Integer jumlahPeserta = bacDao.selectJumlahPeserta(reg_spaj);
		return jumlahPeserta;
	}
	
	/**Fungsi : Untuk mengupdate ID simultan yang berbeda tetapi polis itu simultan.
	 * 			
	 * 
	 * @param id_sim
	 * @param cek_tbl
	 * @param sts  1= update simultan id pemegang
	 * 			   2= update simultan id tertanggung
	 * 			   3= update simultan id pemegang dan tertanggung
	 * @return
	 * @throws DataAccessException
	 */
	public List prosesUpdateSimultan(String id_sim,String cek_tbl,String userName)throws DataAccessException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		List list=new ArrayList();
		Map m = new HashMap();
		String idSimultan[]=id_sim.split(";");
		List lsIdUpdate=new ArrayList();
		String cekTbl[]=cek_tbl.split(";");
		Integer count[]=new Integer[idSimultan.length];
		Integer nilaiMax=0,idx=0;
		String filterId="";
		//looping untuk cari jumlah id simultan di tabel eka.mst_simultaneous 
		for(int i=0;i<idSimultan.length;i++){
			count[i]=uwDao.selectCountIdSimultan(idSimultan[i]);
			Map map=new HashMap();
			map.put("ID_SIMULTAN",idSimultan[i]);
			map.put("LS_DATA", uwDao.selectMstSimultaneousRegSpaj(idSimultan[i]));
			lsIdUpdate.add(map);
		}
		
		nilaiMax=Collections.max(Arrays.asList(count));
		//looping untuk cari index max
		for(int i=0;i<idSimultan.length;i++){
			if(count[i]==nilaiMax){//index yang akan menjadi simultan
				idx=i;
				//m.put("IDX", idx);
			}else{
				filterId+=idSimultan[i]+",";
			}
			m=new HashMap();
			m.put("ID_SIMULTAN_OLD", idSimultan[i]);
			m.put("CEK_TBL", cekTbl[i]);
			//m.put("ID_SIMULTAN_NEW", idSimultan[idx]);
			list.add(m);
			
		}
		//lopping untuk isi id_simultan new
		for(int i=0;i<list.size();i++){
			Map map=(HashMap)list.get(i);
			map.put("ID_SIMULTAN_NEW", idSimultan[idx]);
			list.set(i, map);
		}
		
		filterId=filterId.substring(0,filterId.length()-1);
		uwDao.updateMstSimultaneousIdSimultan(idSimultan[idx], filterId);
		uwDao.updateMstSimultaneousIdSimultanSendEmail(lsIdUpdate,userName);
		
		return list;
	}

	public void setEndorsDao(EndorsDao endorsDao) {
		this.endorsDao = endorsDao;
	}
	
/**	public List prosesUpdateSimultan(String id_sim,String cek_tbl,Integer sts)throws DataAccessException{
		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		List list=new ArrayList();
		Map m = new HashMap();
		String idSimultan[]=id_sim.split(";");
		String cekTbl[]=cek_tbl.split(";");
		Integer count[]=new Integer[idSimultan.length];
		Integer nilaiMax=0,idx=0;
		String filterId="";
		//looping untuk cari jumlah id simultan di tabel eka.mst_simultaneous 
		for(int i=0;i<idSimultan.length;i++){
			count[i]=uwDao.selectCountIdSimultan(idSimultan[i]);
		}
		
		nilaiMax=Collections.max(Arrays.asList(count));
		//looping untuk cari index max
		for(int i=0;i<idSimultan.length;i++){
			if(count[i]==nilaiMax){//index yang akan menjadi simultan
				idx=i;
			}else{
				filterId+=idSimultan[i]+",";
				m=new HashMap();
				m.put("IDX", i);
				m.put("ID_SIMULTAN_OLD", idSimultan[i]);
				m.put("CEK_TBL", cekTbl[i]);
				list.add(m);
			}
			
		
			
		}
		//lopping untuk isi id_simultan new
		for(int i=0;i<list.size();i++){
			Map map=(HashMap)list.get(i);
			map.put("ID_SIMULTAN_NEW", idSimultan[idx]);
			list.set(i, map);
		}
		
		filterId=filterId.substring(0,filterId.length()-1);
		uwDao.updateMstSimultaneousIdSimultan(idSimultan[idx], filterId);
		
		return list;
	}*/

	public List<Hadiah> selectDataPolisUntukInputHadiah(String spajPolis) {
		
		return basDao.selectDataPolisUntukInputHadiah(spajPolis);
	}
	
	public List<Hadiah> selectDataPolisUntukInputHadiah2(String spajPolis) {
		return basDao.selectDataPolisUntukInputHadiah2(spajPolis);
	}
	
	public List<Hadiah> selectDataPolisUntukInputHadiah3(String spajPolis) {
		return basDao.selectDataPolisUntukInputHadiah3(spajPolis);
	}

	public List<Map> selectPosisiProsesHadiah(String begdate, String enddate, int lspd_id, String jenis, String program_hadiah){
	    return basDao.selectPosisiProsesHadiah(begdate, enddate, lspd_id, jenis, program_hadiah);
	}
	
	public List<Map> selectInfoMstHadiah(String spaj){
		return basDao.selectInfoMstHadiah(spaj);
	}
	
	public List<Map> selectBlanko(String no_blanko){
		return basDao.selectBlanko(no_blanko);
	}
	
	public String updateProses(Hadiah hadiah, User currentUser) {
		return basDao.updateProses(hadiah, currentUser);
	}

	public String cancelProses(Hadiah hadiah, User currentUser) {
		return basDao.cancelProses(hadiah, currentUser);
	}

	public String prosesHadiahMemo2(Hadiah hadiah, User currentUser) {
		return basDao.prosesHadiahMemo2(hadiah, currentUser);
	}

	public String prosesHadiahMemo3(Hadiah hadiah, User currentUser) {
		return basDao.prosesHadiahMemo3(hadiah, currentUser);
	}

	public List<Map> viewDetailHadiah(String spaj, Integer mh, String program_hadiah) {
		return basDao.viewDetailHadiah(spaj, mh, program_hadiah);
	}
	
	public List<Hadiah> selectHadiahCek(String spajPolis) {
		
		return basDao.selectHadiahCek(spajPolis);
	}

	public Integer selectPositionHadiah(String spaj) {
		return basDao.selectPositionHadiah(spaj);
	}
	
	public List<Map> selectMasterSupplier(String v) {
		return basDao.selectMasterSupplier(v);
	}
	
	public List<Map<String, Object>> select_detilprodukutamacfl(Integer kode)
	{
		return this.bacDao.select_detilprodukutamacfl(kode);
	}
	
	public List<Map<String, Object>> select_detilprodukutamaonline(Integer kode)
	{
		return this.bacDao.select_detilprodukutamaonline(kode);
	}
	
	public List<Map<String, Object>> select_detilprodukutama_bsim(Integer kode)
	{
		return this.bacDao.select_detilprodukutama_bsim(kode);
	}
	
	public Map selectKodeAgenByHolder(String mscs_holder) throws DataAccessException{
		Map result = bacDao.selectKodeAgenByHolder(mscs_holder);
		if(result == null) {
			result = new HashMap();
			result.put("errMessage", "Kode Agen tidak ada untuk nama ini");
		}
		return result;
	}
	
	public Map selectKodeAgenBySpaj(String reg_spaj) throws DataAccessException{
		Map result = bacDao.selectKodeAgenBySpaj(reg_spaj);
		if(result == null) {
			result = new HashMap();
			result.put("errMessage", "Kode Agen tidak ada untuk nomor SPAJ ini");
		}
		return result;
	}
	//Ryan - SPT OK , validasi ke nasabah, sebelum proses upload SPT (req Timmy )
	public Map posisiSpt(String spaj, String lus_id, String lde_id)
	{
		Map m =null;
			try{
				uwDao.insertMstPositionSpaj(lus_id, "Validasi SPT", spaj, 0);
			}catch(Exception e){
				
			}
		
		return m;
	}
		
	public Map Id_Ref(String id_ref, String nama_p, String tgl_lahir_p, String nama_t, String tgl_lahir_t)
	{
		String name_ref = "";
		Integer jenis_ref = null;
		Map m =null;
		m = new HashMap();
		Map data = (HashMap) this.bacDao.selectNameSellerRef(id_ref.toUpperCase(), nama_p, tgl_lahir_p, nama_t, tgl_lahir_t);
		
		if (data!=null)
		{		
			name_ref=(String)data.get("NAME_REF");
			jenis_ref= ((BigDecimal)data.get("JENIS_REF")).intValue();
			
			m.put("id_ref", id_ref);
			m.put("name_ref",name_ref);
			m.put("jenis_ref",jenis_ref);
			m.put("pesan", null);
		}else{
			m.put("id_ref", "");
			m.put("name_ref","");
			m.put("jenis_ref","2");
			m.put("pesan", "ID Referral salah");
		}
		return m;
	}
	
	/**
	 * Function untuk update Email ke mst_client_new untuk halaman Tambang Emas (Referensi)
	 * @author Canpri Setiawan
	 * @since 29 Aug 2012
	 * @param nama, tgl_lahir, email
	 * @return email
	 */
	public Map editEmailRef(String email, String nama, String tgl_lahir)
	{
		Map m = new HashMap();
		Integer upt = bacDao.updateEmailRef(email, nama, tgl_lahir, "1");
		Integer upt2 = bacDao.updateEmailRef(email, nama, tgl_lahir, "2");
		
		if (upt>0){		
			m.put("email",email);
			m.put("pesan", "Update email berhasil");
		}else{
			m.put("email",null);
			m.put("pesan", "Update email gagal");
		}
		return m;
	}
	
	/**
	 * Function untuk select nama agen dan cabang agen
	 * @author Canpri Setiawan
	 * @since 30 Aug 2012
	 * @param kode agen
	 * @return email
	 */
	public Map getAgen(String kd_agen)
	{
		Map m = new HashMap();
		
		List agen = bacDao.selectAgenRef(kd_agen);
		
		m = (HashMap) agen.get(0);
		
		return m;
	}
	
	/**
	 * Function untuk select info referral untuk redeem poin
	 * @author Canpri Setiawan
	 * @since 26 Sep 2012
	 * @param kode referral
	 * @return List
	 */
	public Map getInfoRef(String kd_ref)
	{
		Map map = new HashMap();
		List ref = bacDao.getInfoRef(kd_ref);
		
		if (ref.isEmpty()){
			map.put("kd_ref","");
			map.put("nama_ref","");
			map.put("alamat","");
			map.put("kota","");
			map.put("kd_pos","");
			map.put("area_telp","");
			map.put("telp","");
			map.put("poin","");
			map.put("pesan", "Tidak ada data");
		}else{
			Map m = (HashMap)ref.get(0);
			map.put("kd_ref",(String)m.get("ID_SELLER"));
			map.put("nama_ref",(String)m.get("NAMA"));
			map.put("alamat",(String)m.get("ALAMAT"));
			map.put("kota",(String)m.get("KOTA"));
			map.put("kd_pos",(String)m.get("KD_POS"));
			map.put("area_telp",(String)m.get("AREA_TELP"));
			map.put("telp",(String)m.get("NO_TELP"));
			map.put("poin",((BigDecimal)m.get("POIN")).longValue());
			map.put("pesan", null);
		}
		
		return map;
	}
	
	public Map nik (String nik){
		Map map=new HashMap();
		List dataNik=commonDao.selectNik(nik);				
		if (dataNik.isEmpty()){					
			map.put("nik2", "s");				
			map.put("nama", "");
			map.put("dept", "");	
		}else{
			Map mapDataNik=(HashMap)dataNik.get(0);
			String nik2=(String)mapDataNik.get("NIK");
			String nama=(String)mapDataNik.get("MCL_FIRST");
			String dept=(String)mapDataNik.get("LDE_DEPT");
			map.put("nik2", nik2);				
			map.put("nama", nama);
			map.put("dept", dept);	
			
		}
		
		
		return map;
	}
	
	public Integer ibankTunggalOrGabungan(String no_trx, String no_spaj){
		Integer flagTunggalGabungan=0;
		Integer count_drek_det = uwDao.selectCountMstDrekDet(no_trx);
		if(count_drek_det ==0){ 
			flagTunggalGabungan=0;
		}else{
			flagTunggalGabungan=1;
		}
		return flagTunggalGabungan;
	}
	
	/**
	 * Function untuk edit hadiah dari program hadiah stable save
	 * @author Canpri Setiawan
	 * @since 30 Aug 2012
	 * @param reg_spaj, lh_id
	 * @return pesan
	 */
	public Map updateHadiahStableSave(String reg_spaj, String lh_id)
	{
		Map m = new HashMap();
		
		Integer update = basDao.updateHadiahStableSave(reg_spaj, lh_id);
		
		if(update>0){
			m.put("pesan", "Hadiah telah diupdate, Harap refresh halaman untuk perubahan!");
		}else{
			m.put("pesan", "Error");
		}
		
		return m;
	}
	
	public Map validasiAgenPasBPDbd(String kodeagen)
	{
		String nama_penutup="";
		String noktp="";
		Date tgllahir=null;
		Map m =null;
		m = new HashMap();
		Map data = (HashMap) this.bacDao.validasiAgenPasBPDbd(kodeagen);
//		if (data!=null)
//		{		
//			nama_ao=(String)data.get("nama");
//		}
		if (data!=null)
		{		
			nama_penutup=(String)data.get("NAMA");
			noktp = (String)data.get("NOKTP");
			tgllahir = (Date) data.get("TGLLAHIR");
		}
		m.put("nama_penutup",nama_penutup);
		m.put("noktp",noktp);	
		m.put("tgllahir", tgllahir);
		return m;
	}
	
	public List struktur_agent(String msag_id){
		List result = bacDao.struktur_agent(msag_id);
	
		return result;
	}
	
	//select hadiah powersave hadiah
	public List select_hadiah_ps(Double mspr_premium) {
		List result = uwDao.select_hadiah_ps(mspr_premium);
		
		return result;
	}
	
	public List select_hadiah_ps_spesial(Double mspr_premium) {
		List result = uwDao.select_hadiah_ps_spesial(mspr_premium);
		
		return result;
	}
	
	public Map listbroker(String kodebroker)
	{
		String nama_broker="";
		Map m =null;
		m = new HashMap();

		Map data;
		data = (HashMap) this.bacDao.selectbroker(kodebroker);
		if (data!=null)
		{		
			nama_broker = (String) data.get("NAMA");
		}
		
		m.put("nama_broker",nama_broker);		
		return m;
	}
	
	public HashMap listdataRkCc(String spaj) 
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		Date tgl_rk = null;
		String no_trx = "";
		String norek_ajs = "";
		String kurs = "";
		BigDecimal flag_tg = null;
		BigDecimal jml_bayar = null;
		BigDecimal nilai_kurs = null;
		
		HashMap x = new HashMap();
		HashMap y = new HashMap();
	    x = Common.serializableMap(this.uwDao.selectDataRKCC(spaj));
	    
		if (x != null)
		{
			tgl_rk = (Date) x.get("TGL_RK");
			no_trx = (String) x.get("NO_TRX");
			norek_ajs = (String) x.get("REK_BANK");
			kurs = (String) x.get("KURS");
			flag_tg = (BigDecimal) x.get("FLAG_TUNGGAL_GABUNGAN");
			jml_bayar = (BigDecimal) x.get("NETT_BAYAR");
			nilai_kurs = (BigDecimal) x.get("NILAI_KURS");
		}
		
		y.put("tgl_rk",df.format(tgl_rk));
		y.put("no_trx",no_trx);
		y.put("norek_ajs",norek_ajs);
		y.put("flag_tg",flag_tg);
		y.put("kurs",kurs);
		y.put("jml_bayar",jml_bayar);
		y.put("nilai_kurs",nilai_kurs);
		return y;
	}

	public List selectDaftarNamaPegawai(String nama) {
		// TODO Auto-generated method stub
		return commonDao.selectDaftarNamaPegawai(nama);
	}

	public List selectDaftarDetaiNamaPegawai(String nama) {
		return commonDao.selectDaftarDetaiNamaPegawai(nama);
	}
	
	public HashMap listdataVirtualAccount(String spaj) 
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		Date tgl_rk = null;
		String no_trx = "";
		String norek_ajs = "";
		String kurs = "";
		BigDecimal flag_tg = null;
		BigDecimal jml_bayar = null;
		BigDecimal nilai_kurs = null;
		BigDecimal premi_sisa = null;
		
		HashMap x = new HashMap();
		HashMap y = new HashMap();
	    x = Common.serializableMap(this.uwDao.selectDataRKVA(spaj));
	    
		if (x != null)
		{
			tgl_rk = (Date) x.get("TGL_TRX");
			no_trx = (String) x.get("NO_TRX");
			norek_ajs = (String) x.get("NOREK_AJS");
			kurs = (String) x.get("LKU_ID");
			flag_tg = (BigDecimal) x.get("FLAG_TUNGGAL_GABUNGAN");
			jml_bayar = (BigDecimal) x.get("MSPR_PREMIUM");
			nilai_kurs = (BigDecimal) x.get("NILAI_KURS");
			premi_sisa =  (BigDecimal) x.get("JUMLAH");
		}
		
		y.put("tgl_rk",df.format(tgl_rk));
		y.put("no_trx",no_trx);
		y.put("norek_ajs",norek_ajs);
		y.put("flag_tg",flag_tg);
		y.put("kurs",kurs);
		y.put("jml_bayar",jml_bayar);
		y.put("nilai_kurs",nilai_kurs);
		y.put("premi_sisa",premi_sisa);
		return y;
	}
	
	public List selectDataPolisNpw(String spaj) {		
		return this.uwDao.selectDataPolisNpw(spaj);
	}
	
	public Integer selectCountCabangBsmByKota(String kota) throws Exception {
		return this.commonDao.selectCountCabangBsmByKota(kota);
	}
	
	public HashMap getPremiPokokTopup(String spaj, String param){
		List<Linkdetail> result = new ArrayList<Linkdetail>();
		Integer isUlink = uwDao.isUlinkBasedSpajNo(spaj);
		Integer isSlink = uwDao.isSlinkBasedSpajNo(spaj);
		HashMap map = new HashMap();
		Integer premi = null;
		Integer flagtopup = null;
		
		if(isUlink != null && isUlink > 0){
			result = uwDao.uLinkDescrAndDetail(spaj);
			
		}else if(isSlink != null && isSlink > 0){
			result = uwDao.sLinkDescrAndDetail(spaj);
			
		}else{
			result = null;
		}
		
		for(int i=0; i<result.size(); i++){
			if(param.equals(result.get(i).getTahunKeAndPremiKe()))
			{
				premi = result.get(i).getPremi().intValue();
				
				if(result.get(i).getDescr().equals("Top-Up Tunggal")){
					flagtopup = 1;
				}else if(result.get(i).getDescr().equals("Top-Up Berkala")){
					flagtopup = 2;
				}
			}
		}
		
		map.put("premi", premi);
		map.put("flagtopup", flagtopup);
		
		return map;
	}
	
	public User listSpv(String cabang_id){
		User m = new User();
		m.setDaftarSpv1(bacDao.selectDaftarUserUA(cabang_id, 2, null));
		return m;
	}
	
	//SISTEM UPLOAD SSU -- RANDY
	public Map listProdSSU(Integer lsbsId, Integer lsdbsNumber)
	{
		Map m =null;
		m = new HashMap();
		String nama_file = productFileSSU(lsbsId.toString(), lsdbsNumber.toString());
		String nama_prod = uwDao.selectLstDetBisnisNamaProduk(lsbsId, lsdbsNumber);
		m.put("nama_prod",nama_prod);
		return m;
	}

	public List selectLsbsSsu() {
		return this.basDao.selectLsbsSsu();
	}
	
	public List selectLsdbsSsu(Integer lsbsId) {
		return basDao.selectLsdbsSsu(lsbsId);
	}
	
	public List selectLsbsSrc() {
		return this.basDao.selectLsbsSrc();
	}
	
	public List selectLsdbsSrc(Integer lsbsId) {
		return basDao.selectLsdbsSrc(lsbsId);
	}
	
	public List selectLsdbsSrc2(String nama) {
		return basDao.selectLsdbsSrc2(nama);
	}
	
	public String selectNamaProduk(String lsbs, String lsdbs){
		return basDao.selectNamaProduk(lsbs, lsdbs);
	}
	
    public static String productFileSSU(String bisnis, String det) {
		Date juli2007 = (new GregorianCalendar(2007,6,1)).getTime(); // 1 januari 2007
		Date  a16Sep = (new GregorianCalendar(2007,8,16)).getTime(); //setelah tanggal 16 september 2007
		Date sep2009 = (new GregorianCalendar(2009,9,1)).getTime(); // untuk simas stabil link (164-2) ada tiga macam SSU : sebelum dan sesudah tgl 1 september 2009, after 1 oct
		Date oct2009 = (new GregorianCalendar(2009,10,1)).getTime(); // untuk simas stabil link (164-2) ada tiga macam SSU : sebelum dan sesudah tgl 1 september 2009, after 1 oct
		String result = "";
		
		//SPECIAL CASES
		if("052, 085".indexOf(bisnis) > -1) { //EKA PROTEKSI
//			if(!pp.getMcl_id().trim().equals(tt.getMcl_id().trim())) {
			if(1+1==2) {
//				return result = (bisnis+"-"+det+"-BEDA PPTTG.pdf"); 
				return result = "TIDAK BISA DIUPLOAD";
			}else {
//				return result = (bisnis+"-"+det+".pdf");
				return result = "TIDAK BISA DIUPLOAD";
			}
		}else if(bisnis.equals("096")){ //MULTI INVEST
			if( det.equals("001")|| det.equals("002")|| det.equals("003")){ //MULTI INVEST MANFAAT LAMA
				return result = (bisnis+"-"+det+".pdf");
			}else if( det.equals("007")|| det.equals("008")|| det.equals("009")){ //MULTI INVEST MANFAAT LAMA
				return result = (bisnis+"-"+det+".pdf");
			}else { //MULTI INVEST MANFAAT BARU
				return result = (bisnis+"-All.pdf");
			}
		}else if(bisnis.equals("099")){ //MULTI INVEST (AS)
			if( det.equals("001")|| det.equals("002")|| det.equals("003")){ //MULTI INVEST MANFAAT LAMA
				return result = (bisnis+"-"+det+".pdf");
			}else { //MULTI INVEST MANFAAT BARU
				return result = (bisnis+"-All.pdf");
			}
		}else if("138".equals(bisnis)) { //EKALINK 80+ KARYAWAN
//			if(elionsManager.selectIsKaryawanEkalife(spaj)>0) {
//			if(1+1==2) {
//				return result = (bisnis+"-"+det+"-KARYAWAN.pdf"); 
//			}else {
//				return result = (bisnis+"-"+det+".pdf");
//			}
			return result = "TIDAK BISA DIUPLOAD";
		}else if("141".equals(bisnis)) { //EDUVEST
//			if(elionsManager.selectIsKaryawanEkalife(spaj)>0) {
//			if(1+1==2) {
//				return result = (bisnis+"-"+det+"-KARYAWAN.pdf"); 
//			}else {
//				return result = (bisnis+"-"+det+".pdf");
//			}
			return result = "TIDAK BISA DIUPLOAD";
		}else if("142".equals(bisnis)) { //KELUARGA PRODUK POWERSAVE
//			if(det.equals("006")) { //POWERSAVE UOB (PRIVILEGE)
//				return result = (bisnis+"-"+det+".pdf");
////			}else if(det.equals("008")){
////				return result = (bisnis+"-"+det+"-SOFTCOPY.pdf");
////			}else if(begdate.before(juli2007)) {
//			}else if(1+1==2) {
//				return result = (bisnis+"-"+det+"-old.pdf");
//			}else { 
//				return result = (bisnis+"-"+det+"-new.pdf");
//			}
			return result = "TIDAK BISA DIUPLOAD";
//		}else if("143".equals(bisnis)) { //KELUARGA PRODUK POWERSAVE
//			if(begdate.before(juli2007)) {
//				return result = (bisnis+"-"+det+"-old.pdf");
//			}else {
//				return result = (bisnis+"-"+det+"-new.pdf");
//			}
		}else if("144".equals(bisnis)) { //KELUARGA PRODUK POWERSAVE
//			if(begdate.before(juli2007)) {
//			if(1+1==2) {
//				return result = (bisnis+"-"+det+"-old.pdf");
//			}else {
//				return result = (bisnis+"-"+det+"-new.pdf");
//			}
			return result = "TIDAK BISA DIUPLOAD";
		}else if("158".equals(bisnis)) { //KELUARGA POWERSAVE MANFAAT BULANAN + STABLE SAVE MANFAAT BULANAN YG LAMA
//			if(begdate.before(juli2007)) {
//			if(1+1==2) {
//				return result = (bisnis+"-"+det+"-old.pdf");
//			}else {
//				return result = (bisnis+"-"+det+"-new.pdf");
//			}
			return result = "TIDAK BISA DIUPLOAD";
		}else if(bisnis.equals("162")){ //EKALINK 88
//			if( det.equals("003")|| det.equals("004")) {
////				if(begdate.before(a16Sep)) {
//				if(1+1==2) {
//					return result = (bisnis+"-"+det+"-old.pdf");
//				}else {
//					return result = (bisnis+"-"+det+"-new.pdf");
//				}
//			}else{
//				return result = (bisnis+"-"+det+".pdf");
//			}
			return result = "TIDAK BISA DIUPLOAD";
		}else if("164".equals(bisnis)){ //SIMAS STABIL LINK
//			if(det.equals("002")){
////				if(begdate.before(sep2009)){
//				if(1+1==2) {
//					return result = (bisnis+"-"+det+"-KHUSUS_BSM_BEFORE1SEP.pdf");
////				}else if(begdate.before(oct2009)){
//				}else if(1+1==2) {
//					return result = (bisnis+"-"+det+"-KHUSUS_BSM_AFTER1SEP.pdf");
//				}else{
//					return result = (bisnis+"-"+det+".pdf");
//				}
//			}else {
////				if(flag_special==1){
//				if(1+1==2) {
//					return result = (bisnis+"-"+det+"-SPECIAL.pdf");
//				}else{
//					return result = (bisnis+"-"+det+".pdf");
//				}
//			}
			return result = "TIDAK BISA DIUPLOAD";
		}else if("175".equals(bisnis) && det.equals("002")) { //PRODUK POWERSAVE SYARIAH BSM
				return result = (bisnis+"-"+det+"-KHUSUS_BSM.pdf");
		}else if("178".equals(bisnis)){ //smart medicare dmtm dan mallins
//			if(cabang.equals("58")){
//				return result = (bisnis+"-All-MALLASSURANCE.pdf");
//			}else if(cabang.equals("40")){
//				return result = (bisnis+"-All-DMTM.pdf");
//			}else{
//				return result = (bisnis+"-All-WORKSITE.pdf");
//			}
			return result = "TIDAK BISA DIUPLOAD";
		}else if("208".equals(bisnis)){ // *SMART KID			
			if (Integer.parseInt(det) >=13 && Integer.parseInt(det) <=16){
				return result = (bisnis+"-VIP"+".pdf");//VIP EDU PLAN
			}else if(Integer.parseInt(det) >=17 && Integer.parseInt(det) <=20){
				return result = (bisnis+"-All-BJB.pdf");//SMile Kid
			}else if(Integer.parseInt(det) >=5 && Integer.parseInt(det) <=8){
				return result = (bisnis+"-All-BANCASS.pdf");//SIMAS Kid
			}else if(Integer.parseInt(det) >=9 && Integer.parseInt(det) <=12){			
				return result = (bisnis+"-All-AGENCY.pdf");//SMile Kid
			}else{
//				return result = (bisnis+"-All-DMTM.pdf");//SMART KID
			}
		}else if("181".equals(bisnis)){
//			if(det.equals("001")){
//				return result = (bisnis+"-case10.pdf");
//			}else if(det.equals("002") || det.equals("005")){
////				if(pp.getLsre_id()==1){
////					return result = (bisnis+"-case8.pdf");
////				}else if(pp.getLsre_id()!=1 && tt.getMste_age()<17){
////					return result = (bisnis+"-case9.pdf");
////				}else{
////					logger.info(dir+"/"+bisnis+"-case10.pdf");
////					return result = (bisnis+"-case10.pdf");
////				}
//			}else if(det.equals("003") || det.equals("006")){
////				if(pp.getLsre_id()==1){
////					return result = (bisnis+"-case8.pdf");
////				}else if(pp.getLsre_id()!=1 && tt.getMste_age()<17){
////					return result = (bisnis+"-case9.pdf");
////				}else{
////					return result = (bisnis+"-case10.pdf");
////				}
//			}
			return result = "TIDAK BISA DIUPLOAD";
		}else if("129".equals(bisnis) && (det.equals("005") || det.equals("006") || det.equals("009") || det.equals("010") || det.equals("011") || det.equals("012")) ){
//			if(pack.equals("12")){
//				return result = (bisnis+"-"+det+"-"+m.get("FLAGPACKET")+".pdf"); 
//			}else if(pack.equals("13")){
//				return result = (bisnis+"-"+det+"-"+m.get("FLAGPACKET")+".pdf");
//			}else if(pack.equals("14")){
//				return result = (bisnis+"-"+det+"-"+m.get("FLAGPACKET")+".pdf");
//			}else if(pack.equals("15")){
//				return result = (bisnis+"-"+det+"-"+m.get("FLAGPACKET")+".pdf");
//			}else if(pack.equals("16")){
//				return result = (bisnis+"-"+det+"-"+m.get("FLAGPACKET")+".pdf");
//			}else if(pack.equals("17")){
//				return result = (bisnis+"-"+det+"-"+m.get("FLAGPACKET")+".pdf");
//			}
			return result = "TIDAK BISA DIUPLOAD";
		}else if("201".equals(bisnis)){
			return result = (bisnis+"-All.pdf");
		}else if("219".equals(bisnis)){
			return result = (bisnis+"-All.pdf");
		}else if("120".equals(bisnis)){
			return result = "TIDAK BISA DIUPLOAD";
		}
		
		//yg 1 format untuk semua detail bisnis
		if ("143, 155, 168, 178, 179, 183, 189, 193, 195,204".contains(bisnis)){
			//Andhika - req:Ningrum SMiLe Medical PT ARCO MITRA SEJATI 
//			if("183,189,204".indexOf(bisnis)>-1 && cabang.equals("40")){
			if("183,189,204".indexOf(bisnis)>-1){
				//SSU BTN
				if ("183".equals(bisnis) && (Integer.parseInt(det)>=76 && Integer.parseInt(det)<=90)){
					return result = (bisnis+"SMC"+".pdf");
				}else if("183".equals(bisnis) && (Integer.parseInt(det)>=16 && Integer.parseInt(det)<=60)){
					return result = (bisnis+"-TM.pdf");
				}else if("183".equals(bisnis) && (Integer.parseInt(det)>=106 && Integer.parseInt(det)<=120)){
					return result = (bisnis+"-DKI.pdf"); // SMile MEdical Care DKI
				}
//				else if("204".equals(bisnis) && (Integer.parseInt(det)>=37 && Integer.parseInt(det)<=48)){
//					return  result = (bisnis+"-MARZ.pdf"); // MARZ
//				}else if("189".equals(bisnis) && (Integer.parseInt(det)>=33 && Integer.parseInt(det)<=47)){
//					return  result = (bisnis+"-MARZ.pdf"); // MARZ
//				}
				else{
					return result = "TIDAK BISA DIUPLOAD";
				}
			}else{
				if ("195".equals(bisnis) && (Integer.parseInt(det)>=13 && Integer.parseInt(det)<=24)){
					return result = (bisnis+"SHC"+".pdf");
				}else if ( ("195".equals(bisnis) && (Integer.parseInt(det)>=49 && Integer.parseInt(det)<=60)) ||
						("183".equals(bisnis) && (Integer.parseInt(det)>=91 && Integer.parseInt(det)<=105)) ){
					return result = (bisnis+"-VIP"+".pdf");
				}else if ("195".equals(bisnis) && (Integer.parseInt(det)>=61 && Integer.parseInt(det)<=72)){
					return result = (bisnis+"-HCD"+".pdf"); // Smile Hospital Care Bank DKI
				}else if ("195".equals(bisnis) && (Integer.parseInt(det)>=37 && Integer.parseInt(det)<=48)){
					return result = (bisnis+"-SHP"+".pdf"); // SMILE HOSPITAL PLUS
				}else{
					return result = "TIDAK BISA DIUPLOAD";
				}
			}
		}
//		if(user!=null){
//			if ("120".equals(bisnis) && user.getLus_bas()==2 && "22,23,24".indexOf(bisnis) >=0){
//				return result = (bisnis+" SIMPOL"+".pdf");
//			}
//		}	
		//selain itu, format default
		return result = (bisnis+"-"+det+".pdf");
	}
	
	public Integer eSpajGadget(String reg_spaj){
		Integer flag_gadget = 0;
		ArrayList spajtemp = bacDao.selectMstSpajTemp(reg_spaj, 4);
		if(spajtemp.size()>0) flag_gadget = 1;
		return flag_gadget;
	}
	
	public String cekKodeCabang(String kode) throws Exception {
		Map map = new HashMap();	
		String namaCabang = "";
		namaCabang = bacDao.cekKodeCabang(kode);
		
		if(namaCabang == null){
			namaCabang = "Kode Cabang Tidak Terdaftar";
		}
		return  namaCabang;
	}
	
	//helpdesk [137730] tambahin pilihan campaign
	public List selectMstCampaignProduct(String kode_bisnis) {	
		String kode_produk = "";
		String number_produk = "";
		Integer letak = new Integer(kode_bisnis.indexOf("X"));
		kode_produk = kode_bisnis.substring(0, letak.intValue() - 1);
		number_produk = kode_bisnis.substring(letak.intValue() + 1, kode_bisnis.length());
		
		return bacDao.selectMstCampaignProduct(kode_produk, number_produk);
	}
}