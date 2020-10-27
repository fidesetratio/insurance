package com.ekalife.elions.dao;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.Broker;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.CommandPowersaveUbah;
import com.ekalife.elions.model.ContactPerson;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DepositPremium;
import com.ekalife.elions.model.DetEndors;
import com.ekalife.elions.model.DetailPembayaran;
import com.ekalife.elions.model.DetilTopUp;
import com.ekalife.elions.model.Employee;
import com.ekalife.elions.model.Endors;
import com.ekalife.elions.model.Hcp;
import com.ekalife.elions.model.InputTopup;
import com.ekalife.elions.model.Insured;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.Kesehatan;
import com.ekalife.elions.model.KuesionerPelayananDetails;
import com.ekalife.elions.model.KursDanJumlah;
import com.ekalife.elions.model.Maturity;
import com.ekalife.elions.model.MedQuest;
import com.ekalife.elions.model.MedQuest_tambah;
import com.ekalife.elions.model.MedQuest_tambah2;
import com.ekalife.elions.model.MedQuest_tambah3;
import com.ekalife.elions.model.MedQuest_tambah4;
import com.ekalife.elions.model.MedQuest_tambah5;
import com.ekalife.elions.model.MstOfacScreeningResult;
import com.ekalife.elions.model.MstQuestionAnswer;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.PembayarPremi;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Pemegang2;
import com.ekalife.elions.model.PemegangAndRekeningInfo;
import com.ekalife.elions.model.Personal;
import com.ekalife.elions.model.PesertaPlus_mix;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.PowersaveCair;
import com.ekalife.elions.model.PowersaveProses;
import com.ekalife.elions.model.PowersaveUbah;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.ProductInsEnd;
import com.ekalife.elions.model.Promo;
import com.ekalife.elions.model.PrwSeller;
import com.ekalife.elions.model.Redeem;
import com.ekalife.elions.model.ReffBii;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.RencanaPenarikan;
import com.ekalife.elions.model.S_Premi;
import com.ekalife.elions.model.Scan;
import com.ekalife.elions.model.Simas;
import com.ekalife.elions.model.SlinkBayar;
import com.ekalife.elions.model.SsaveBayar;
import com.ekalife.elions.model.StableSave;
import com.ekalife.elions.model.SumberKyc;
import com.ekalife.elions.model.Tamu;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.TmSales;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.sms.Smsserver_in;
import com.ekalife.elions.model.sms.Smsserver_out_hist;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.parent.ParentDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

@SuppressWarnings("unchecked")
public class BacDao extends ParentDao{
	protected final Log logger = LogFactory.getLog(getClass());
	
	protected void initDao() throws DataAccessException{
		this.statementNameSpace = "elions.bac.";
	}

	public int selectFlagSPH(String spaj) throws DataAccessException{
		return (Integer) querySingle("selectFlagSPH", spaj);
	}
	
	public int selectBPRatePerTransaksiSlink(String reg_spaj, int msl_tu_ke) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("msl_tu_ke", msl_tu_ke);
		return (Integer) querySingle("selectBPRatePerTransaksiSlink", m);
	}
	
	public Integer selectStatusPaidBilling(String reg_spaj, int msbi_premi_ke, int msbi_tahun_ke) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("msbi_tahun_ke", msbi_tahun_ke);
		m.put("msbi_premi_ke", msbi_premi_ke);
		return (Integer) querySingle("selectStatusPaidBilling", m);
	}
	
	public int selectStatusPaidBillingStableLink(String reg_spaj, int tu_ke) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("tu_ke", tu_ke);
		return (Integer) querySingle("selectStatusPaidBillingStableLink", m);
	}
	
	public Date selectBegDateSlinkPertama(String reg_spaj) throws DataAccessException{
		return (Date) querySingle("selectBegDateSlinkPertama", reg_spaj);
	}
	
	public int selectCountPowersaveCair(String spaj) throws DataAccessException{
		return (Integer) querySingle("selectCountPowersaveCair", spaj);
	}
	
	public int selectCountPremiSlink(String spaj) throws DataAccessException{
		return (Integer) querySingle("selectCountPremiSlink", spaj);
	}
	
	public Powersave selectRolloverPowersaveTerakhir(String reg_spaj) throws DataAccessException{
		return (Powersave) querySingle("selectRolloverPowersaveTerakhir", reg_spaj);
	}
	
	public Date selectMprMatureDate(String reg_spaj) throws DataAccessException{
		return (Date) querySingle("selectMprMatureDate", reg_spaj);
	}
	
	public Date selectPowerSaveRoSurrender(String reg_spaj) throws DataAccessException{
		return (Date) querySingle("selectPowerSaveRoSurrender", reg_spaj);
	}
	
	public Integer selectCountPowerSaveSudahPinjaman(String reg_spaj) throws DataAccessException{
		return (Integer) querySingle("selectCountPowerSaveSudahPinjaman", reg_spaj);
	}
	
	public Date selectEndDatePolisEndors(String reg_spaj) throws DataAccessException{
		return (Date) querySingle("selectEndDatePolisEndors", reg_spaj);
	}
	
	public double getBPrate(Date begdate, int mgi, int flag_bulanan) throws ParseException{
		//Yusuf - per 7 Sept 2009, mgi 1,3,6 bp nya 100%
		//Deddy - diubah batas tgl jd 10 sept 2009
		Date sep10 = defaultDateFormat.parse("10/09/2009");
		if(begdate.before(sep10)){
			return 
			flag_bulanan == 1 ? 100 :
				mgi == 1 ? 97.5 : 
				mgi == 3 ? 97.5 : 
				mgi == 6 ? 95 	: 90; //bonus performance
		}else{
			return 
			flag_bulanan > 0 ? 100 :
			//flag_bulanan == 1 ? 100 :
				mgi == 1 ? 100 : 
				mgi == 3 ? 100 : 
				mgi == 6 ? 100 : 90; //bonus performance
		}
	}
	
	public List select_simas_card(String query) throws DataAccessException {
		return query("select_simas_card", query);
	}
	
	public List selectRekAJSMSIG(){
		return query("selectRekAJSMSIG", null);
	}
	
	public Double selectPersenKomisiReffBii(String reg_spaj) throws DataAccessException{
		return (Double) querySingle("selectPersenKomisiReffBii", reg_spaj);
	}
	 
	public Double selectPersenInsentifReffBii(String reg_spaj) throws DataAccessException{
		return (Double) querySingle("selectPersenInsentifReffBii", reg_spaj);
	}
	 
	public Double selectRateKomisiFeeBasedBancassurance(String reg_spaj) throws DataAccessException{
		return (Double) querySingle("selectRateKomisiFeeBasedBancassurance", reg_spaj);
	}
	
	public Double selectTotalKomisiEkaSehat(String reg_spaj) throws DataAccessException{
		return (Double) querySingle("selectTotalKomisiEkaSehat", reg_spaj);
	}
	
	public Integer selectLineBusLstBisnis(String lsbs_id){
		return (Integer) querySingle("selectLineBusLstBisnis", lsbs_id);
	}
	
	public Integer getJumlahEkaSehat(String reg_spaj) throws DataAccessException{
		return(Integer) querySingle("getJumlahEkaSehat", reg_spaj);
	}

	//project Smile Medical Extra (848-1~70) helpdesk [129135] //Chandra
	public Integer selectRiderMedicalExtra(String reg_spaj){
		return(Integer) querySingle("selectRiderMedicalExtra", reg_spaj);
	}
		
	public Integer selectCountEkaSehatAdmedikaNew(String reg_spaj, int flagJenisEkasehat) throws DataAccessException{
		Map p = new HashMap();
		p.put("reg_spaj", reg_spaj);
		p.put("flag", flagJenisEkasehat);		
		return(Integer) querySingle("selectCountEkaSehatAdmedikaNew", p);
	}
	
	public Integer selectCountEkaSehatAdmedikaHCP(String reg_spaj) throws DataAccessException{
		return(Integer) querySingle("selectCountEkaSehatAdmedikaHCP", reg_spaj);
	}
	
	public List<Map> selectSisaPremiStableLink(String reg_spaj) throws DataAccessException{
		return query("selectSisaPremiStableLink", reg_spaj);
	}
	
	public List<Map> selectProduksiKe(String reg_spaj, int prod_ke) throws DataAccessException{
		Map p = new HashMap();
		p.put("reg_spaj", reg_spaj);
		p.put("prod_ke", prod_ke);		
		return query("selectProduksiKe", p);
	}
	
	public List<Powersave> selectStableLinkUntukPerhitunganBP(String reg_spaj, int msl_tu_ke) throws DataAccessException{
		Map p = new HashMap();
		p.put("reg_spaj", reg_spaj);
		p.put("msl_tu_ke", msl_tu_ke);		
		return query("selectStableLinkUntukPerhitunganBP", p);
	}
	
	public HashMap selectDataAgenValidasiWorksite(String msag_id) throws DataAccessException{
		return (HashMap) querySingle("selectDataAgenValidasiWorksite", msag_id);
	}
	
	public Map selectDataVirtualAccount(String reg_spaj) throws DataAccessException {
		return (Map) querySingle("selectDataVirtualAccount", reg_spaj);
	}
	
	public void updateVirtualAccountBySpaj(String reg_spaj) throws DataAccessException{
		//yg diupdate hanya yg :
		//1. bukan USD
		//2. bukan bancass
		//3. bukan syariah
		//4. pembayaran bukan sekaligus
		//5. bukan autodebet
		Map data 		= (Map) querySingle("selectDataVirtualAccount", reg_spaj);
		int lscb_id 	= ((BigDecimal) data.get("LSCB_ID")).intValue();
		int flag_cc 	= ((BigDecimal) data.get("MSTE_FLAG_CC")).intValue();
		String lku_id 	= (String) data.get("LKU_ID");
		String lca_id 	= (String) data.get("LCA_ID");
		String no_virtual = "8006";//default 4 digit depan produk konvensional
		Map tampung = new HashMap();
		tampung.put("reg_spaj", reg_spaj);
		if(lku_id.equals("01") &&
				//!lca_id.equals("09") &&
				lscb_id != 0 
//				&& flag_cc == 0){ request Himmia (2 Dec 2011) - untuk No Virtual Account kondisi autodebet dihilangkan
				){
			if(!products.syariah(uwDao.selectBusinessId(reg_spaj), uwDao.selectBusinessNumber(reg_spaj).toString())){
				tampung.put("no_virtual", no_virtual);
				update("updateVirtualAccountBySpaj", tampung);
			}else{
				no_virtual ="8076";
				tampung.put("no_virtual", no_virtual);
				update("updateVirtualAccountBySpaj", tampung);
			}
		}
	}
	
	public List<Map> selectRateBankSinarmas() throws DataAccessException{
		return query("selectRateBankSinarmas", null);
	}
	
	public Map selectValidasiSebelumEditDate(String reg_spaj) throws DataAccessException{
		return (Map) querySingle("selectValidasiSebelumEditDate", reg_spaj);
	}
	
	public int selectCountRolloverPowersave(String reg_spaj) throws DataAccessException{
		return (Integer) querySingle("selectCountRolloverPowersave", reg_spaj);
	}
	
	/**
	 * Fungsi ini dibuat untuk insert data powersave, sekaligus stable save premi bulanan (cicilan)
	 * 
	 * @param ps
	 * @param jenis
	 * @author Yusuf
	 * @since 27 Mar 09
	 * @throws DataAccessException
	 * @throws ParseException 
	 */
	public void insertPowersave(Powersave ps, String jenis, String lku_id, Rekening_client rek) throws DataAccessException, ParseException{
		String lsbs_id = uwDao.selectBusinessId(ps.getReg_spaj());
		int lsdbs_number = uwDao.selectBusinessNumber(ps.getReg_spaj());
		
		if(jenis.equals("mst_powersave_proses")){
			
			// Yusuf - Bila Stable Save 184 sudah dijalankan, uncomment dibawah
			if(products.stableSavePremiBulanan(lsbs_id) || products.stableSave(Integer.parseInt(lsbs_id), lsdbs_number)){
			//if(products.stableSavePremiBulanan(lsbs_id)){
				//tidak ada table proses untuk STABLE SAVE (hanya 1 : mst_ssave)
			}else{
				insert("insert.mst_powersave_proses", ps);
			}
			
		}else if(jenis.equals("mst_powersave_ro")){
			
			// Yusuf - Bila Stable Save 184 sudah dijalankan, uncomment dibawah
			if(products.stableSavePremiBulanan(lsbs_id) || products.stableSave(Integer.parseInt(lsbs_id), lsdbs_number)){
			//if(products.stableSavePremiBulanan(lsbs_id)){
				
				StableSave ss = new StableSave();
				
				ss.reg_spaj 			= ps.getReg_spaj();
				ss.mss_kode 			= ps.getMps_kode();
				if(ps.msl_spaj_lama!=null){
					ss.mss_bdate 			= ps.getBegdate_topup();
					Integer mssur_se = commonDao.selectCountMstSurrender(ps.msl_spaj_lama);
					if(mssur_se!=null){
						if(mssur_se==2){
							ss.mss_edate 			= FormatDate.add(ps.getEnddate_topup1(),Calendar.DAY_OF_MONTH,-1);
							ss.mss_ro_date 			= FormatDate.add(ps.getEnddate_topup1(), Calendar.DATE, 0);
							ss.mss_paid_date		= commonDao.selectAddWorkdays(ps.getEnddate_topup1(), 0);
						}
					}else{
						ss.mss_edate 			= FormatDate.add(ps.getEnddate_topup(),Calendar.DAY_OF_MONTH,-1);
						ss.mss_ro_date 			= FormatDate.add(ps.getEnddate_topup(), Calendar.DATE, 0);
						ss.mss_paid_date		= commonDao.selectAddWorkdays(ps.getEnddate_topup(), 0);
					}
					
					
				}else{
					ss.mss_bdate 			= ps.getMps_deposit_date();
					ss.mss_edate 			= ps.getMpr_mature_date();
					ss.mss_ro_date 			= FormatDate.add(ps.getMpr_mature_date(), Calendar.DATE, 1);
					ss.mss_paid_date		= commonDao.selectAddWorkdays(ps.getMpr_mature_date(), 1);
				}
				
				ss.mss_ro 				= ps.getMps_roll_over();
				ss.mss_mgi 				= Integer.parseInt(ps.getMps_jangka_inv());
				ss.mss_rate 			= ps.getMps_rate();
				ss.mss_premi 			= ps.getMpr_deposit();
				ss.mss_bunga 			= ps.getMpr_interest();
				ss.mss_tax				= (double) 0;
				ss.mss_surr_charge 		= null;
				ss.msen_endors_no		= ps.getMsen_endors_no();
				if(products.stableSavePremiBulanan(lsbs_id)){//Req Rudi : untuk progressive save dan progressive link, mss_letter_date/msl_letter_date diisi 1 tahun dari begdate dan mss_no/msl_no diisi 1
					ss.mss_letter_date		= FormatDate.add(ps.getMpr_mature_date(), Calendar.YEAR, 1);
				}else ss.mss_letter_date	= null;
				
				
				Date tglmature = ps.getMpr_mature_date();
				f_hit_umur umr = new f_hit_umur();
				int hari = umr.hari_powersave(
						ss.getMss_bdate().getYear()+1900, ss.getMss_bdate().getMonth()+1, 
						ss.getMss_bdate().getDate(), tglmature.getYear()+1900, tglmature.getMonth()+1, tglmature.getDate());
				hari += 1;
				if(ps.msl_spaj_lama!=null){
					hari = umr.hari_powersave(
							ss.getMss_bdate().getYear()+1900, ss.getMss_bdate().getMonth()+1, 
							ss.getMss_bdate().getDate(), ss.mss_edate.getYear()+1900, ss.mss_edate.getMonth()+1, ss.mss_edate.getDate());
					hari += 1;
				}else{
					hari = umr.hari_powersave(
							ss.getMss_bdate().getYear()+1900, ss.getMss_bdate().getMonth()+1, 
							ss.getMss_bdate().getDate(), tglmature.getYear()+1900, tglmature.getMonth()+1, tglmature.getDate());
					hari += 1;
				}
				
				
				ss.mss_hari 			= hari; 
				ss.mss_jn_rumus 		= ps.getMpr_jns_rumus();
				ss.mss_tahun_ke 		= 1;
				ss.mss_premi_ke			= 1;
				ss.mss_note 			= ps.getMpr_note();
				ss.mss_spaj_lama		= ps.getMsl_spaj_lama();
				
				if(products.stableSavePremiBulanan(lsbs_id)){
					ss.mss_bulanan 			= 1; //premi bulanan
				}else if(products.stableSave(Integer.parseInt(lsbs_id), lsdbs_number)){
					ss.mss_bulanan 			= 0; //premi sekaligus
				}
				
				ss.mss_bayar_bunga 		= 0;
				ss.mss_flag_rate 		= ps.getMpr_nett_tax(); //0, 2 -> normal special nett
				ss.mss_rate_date		= ps.getMpr_tgl_rate();
				
				Date sysdate = commonDao.selectSysdate();
				
				ss.mss_input_date 		= sysdate;
				ss.mss_trans_date 		= null;
				ss.mss_confirm_date 	= null;
				ss.mss_prod_date		= null;
				ss.mss_print 			= 0;
				ss.mss_print_date 		= null;
				ss.mss_flag_comm		= 0;
				ss.mss_cash 			= 0;
				ss.mspin_no_pinjaman	= null;
				ss.mss_aktif			= 1;
				ss.lus_id				= ps.getLus_id();
				ss.mss_rider_cb			= null;
				
				//kolom2 baru
				ss.mss_no 				= 1;
				ss.mss_tu_ke 			= 0;
				ss.mss_flag_cb 			= ps.getFlag_rider();
				ss.mss_surat_pdf 		= 0;
				ss.mss_proses_bsm 		= 0;
				ss.flag_bulanan 		= ps.getFlag_bulanan();
				//stable save selain kode produk 184, dilakukan pengecekan untuk flag_bulanan
				if(Integer.parseInt(lsbs_id)!=184){
					if(Integer.parseInt(lsbs_id)==158){
						ss.flag_bulanan 		= 1;
					}else {
						ss.flag_bulanan 		= 0;
					}
				}
				
				//Yusuf 20 Aug 09 - Tambahan Perhitungan bila Stable Save Manfaat Bulanan (FLAG_BULANAN = 1)
				//Contoh : MGI = 12 bulan, maka akan ada 12 row yg diinsert
				if(ss.flag_bulanan.intValue() == 1){

					ss.mss_bunga = 0.; //bunga di nol kan bila manfaat bulanan
					
					//INSERT UNTUK PREMI POKOK
					insert("insert.mst_ssave", ss);
					
					for(int i=0; i<ss.mss_mgi; i++){
						SsaveBayar sb 		= new SsaveBayar();

						sb.reg_spaj 		= ss.reg_spaj;
						sb.mss_no 			= ss.mss_no;
						sb.mssb_bayar_id 	= sequence.sequenceNoRegSsaveBayar();
						
						sb.mss_bdate 		= uwDao.selectFaddMonths(defaultDateFormat.format(ss.mss_bdate), i); //bertambah 1 bulan dari deposit date
						sb.mss_edate 		= FormatDate.add(uwDao.selectFaddMonths(defaultDateFormat.format(ss.mss_bdate), (i+1)), Calendar.DATE, -1);
						sb.mssb_paid_date 	= commonDao.selectAddWorkdays(sb.mss_edate, 1);
						sb.mssb_print_date	= null;
						
						sb.lku_id			= lku_id;
						sb.mssb_premi 		= ss.mss_premi;
						sb.mssb_rate 		= ss.mss_rate;
						hari 				= umr.hari_powersave(sb.mss_bdate.getYear()+1900,sb.mss_bdate.getMonth()+1,sb.mss_bdate.getDate(),sb.mss_edate.getYear()+1900,sb.mss_edate.getMonth()+1,sb.mss_edate.getDate());
						hari 				= hari + 1;
						sb.mssb_jml_hari 	= hari;
						sb.mssb_bunga 		= (new Double(hari)/365) * (sb.mssb_rate/100) * sb.mssb_premi;                     
						
						sb.mssb_tambah		= 0.;
						sb.mssb_hari		= 0;
						sb.mssb_kurang		= 0.;
						sb.mssb_jum_bayar	= sb.mssb_bunga;
						sb.mssb_notes		= null;
						 
						sb.mssb_rekening 	=
							rek.getLsbp_nama() + 
							" CAB. " + rek.getMrc_cabang() + 
							"-" + rek.getMrc_kota() + 
							", A/C." + rek.getMrc_no_ac() + 
							"(" + (lku_id.equals("01") ? "Rp." : "US$") + 
							"), A/N. " + rek.getMrc_nama();
						
						sb.lsbp_id			= Integer.valueOf(rek.getLsbp_id());
						sb.mrc_cabang		= rek.getMrc_cabang();
						sb.mrc_atas_nama	= rek.getMrc_nama();
						sb.mrc_no_ac		= rek.getMrc_no_ac();
						
						sb.flag_bulanan		= ss.flag_bulanan;
						sb.flag_proses		= 0;
						sb.mssb_filing		= 0;
						sb.mssb_filing_date	= null;
						sb.mssb_input_date	= sysdate;
						sb.lus_id 			= ss.lus_id;
						
						insert("insert.mst_ssave_bayar", sb);
					}
				}else{
					insert("insert.mst_ssave", ss);
				}							
				
			}else{
				insert("insert.mst_powersave_ro", ps);
			}
			
		}else{
			throw new RuntimeException("ERROR INSERT POWERSAVE, JENIS : " + jenis);
		}
	}
	
	public StableSave selectMstSsave(String reg_spaj) throws DataAccessException{
		return (StableSave) querySingle("select.mst_ssave", reg_spaj);
	}
	
	public void insertMstSsave(StableSave ss) throws DataAccessException{
		insert("insert.mst_ssave", ss);
	}
	
	public void deleteMstSsave(String reg_spaj) throws DataAccessException{
		delete("delete.mst_ssave", reg_spaj);
	}
	
	
	public List<String> selectCancelBanyakPolis() throws DataAccessException{
		return query("selectCancelBanyakPolis", null);
	}
	
	public Integer selectPosisiUlink(String reg_spaj) throws DataAccessException{
		return (Integer) querySingle("selectPosisiUlink", reg_spaj);
	}
	
	public String selectCabangBiiPolis(String reg_spaj) throws DataAccessException{
		return (String) querySingle("selectCabangBiiPolis", reg_spaj);
	}
	
	public String otorisasiTopUpStabilLink(Integer mslTuKe, String ljiId, String lusId, String regSpaj) throws ParseException{
		String pesan;
		try{
			uwDao.insertMstPositionSpaj(lusId, "OTORISASI INPUT TOPUP KE-"+mslTuKe, regSpaj, 0 );
			pesan = "data berhasil diotorisasi";
		}catch (Exception e) {
			pesan = "data gagal diotorisasi";
		}
		return pesan;
	}
	
	public String saveTopupStableLink(InputTopup trans, List<InputTopup> daftarTopup, User currentUser) throws ParseException{
		
		for(InputTopup tmp : daftarTopup) {
			//cari dulu yg mana yg mau di insert/update/delete
			String lsbs_id = uwDao.selectBusinessId(tmp.reg_spaj);
			String lsdbs_number = uwDao.selectLsdbsNumber(tmp.reg_spaj);
			Pemegang pmg=selectpp(trans.reg_spaj);
			if(Integer.parseInt(lsbs_id)==188){// diset null untuk lji ID krn powersave tidak ada.
				trans.simpan_lji_id="";
				tmp.lji_id="";
			}
			if(trans.reg_spaj.equals(tmp.reg_spaj) && trans.simpan_lji_id.equals(tmp.lji_id) && trans.simpan_msl_no.equals(tmp.msl_no)) {
				
				Date sysdate = commonDao.selectSysdate();
				
				//insert topup baru
				if(trans.simpan_mode.equals("insert")) {
					
					tmp.msl_kode 			= 5;
					tmp.tarik_bunga 		= 0; //gak dipakai (rudy)
					tmp.msl_posisi 			= 45; //45 dulu, gitu ditrans, baru jadi 73
					
					tmp.msl_tarik_pertama 	= (double) 0;
					tmp.msl_tgl_nab_bp		= null;
					tmp.msl_nab_bp			= (double) 0;
					tmp.msl_nilai_polis		= (double) 0;
					tmp.msl_bp				= (double) 0;
					tmp.msl_bp_pt			= (double) 0;
					tmp.msl_up				= trans.mspr_tsi;
					tmp.msl_bayar_bunga		= 0;
					tmp.msl_bayar_bp		= 0;
					tmp.msl_input_date		= sysdate;
					tmp.msl_proses_date		= sysdate;
					tmp.lus_id				= Integer.valueOf(currentUser.getLus_id());
					tmp.msl_aktif			= 1;
					tmp.msl_flag_up			= 0;
					tmp.msl_flag_comm		= 1;
					tmp.msl_print			= 0;
					tmp.msl_bayar_tarik		= 0;
					tmp.msl_surr_charge		= (double) 0;
					tmp.msl_cash			= 0;
					if(Integer.parseInt(lsbs_id)==188){
						tmp.no_reg				= sequence.sequenceNoRegPsaveBaru();
					}else{
						tmp.no_reg				= sequence.sequenceNoRegStableLink();
					}
					
					tmp.msl_jn_rumus 		= 1;
					
					//(Yusuf 16/08/2010) - Request Rudy, untuk SMS, flag proses bsm nya 2 
					if(currentUser.getJn_bank().intValue() == 2){
						tmp.msl_proses_bsm		= 1;
					}else if(currentUser.getJn_bank().intValue() == 3){
						tmp.msl_proses_bsm		= 2;
					}else{
						tmp.msl_proses_bsm		= 0;
					}
					
					tmp.msl_new				= 2; //Yusuf (06/01/10) - Req Rudy, Topup selain new business dan dari E-Lions = 2
					
					tmp.msl_bp_rate = getBPrate(tmp.msl_bdate, tmp.msl_mgi, tmp.flag_bulanan);
					if(Integer.parseInt(lsbs_id)==164 && Integer.parseInt(lsdbs_number)==11){
						tmp.msl_bp_rate = 25.; 
					}
					
					//Yusuf 28 Jul 09 - Tambahan Perhitungan bila Stable Link Manfaat Bulanan (FLAG_BULANAN = 1)
					if(tmp.flag_bulanan.intValue() > 0){
//					if(tmp.flag_bulanan.intValue() == 1){
					
						tmp.msl_bunga = 0.; //bunga di nol kan bila manfaat bulanan
						
						//INSERT UNTUK PREMI TU 
						if(Integer.parseInt(lsbs_id)==188){
							insert("insert.mst_psave_2", tmp);
						}else{
							
							insert("insert.mst_slink_2", tmp);
						}
						
						
						for(int i=0; i<tmp.msl_mgi; i++){
							SlinkBayar sb = new SlinkBayar();
							sb.reg_spaj = tmp.reg_spaj;
							sb.msl_no = tmp.msl_no;                        
							sb.lji_id = tmp.lji_id;                         
							
							//Yusuf (11/09/09) Request Rudy : tidak usah generate disini, diisi angka urut aja
							//sb.mslb_bayar_id = sequence.sequenceNoRegSlinkBayar();                  
							sb.mslb_bayar_id = String.valueOf(i);

							sb.mslb_desc = "Bunga"; //CASE SENSITIVE, karena query rudy banyak yg -> where mslb_desc = 'Bunga'                      
							sb.mslb_tu_ke = tmp.msl_tu_ke;                    
							sb.lus_id = tmp.lus_id;                        
							sb.lku_id = tmp.lku_id;                         
							sb.mslb_print_date = null;       
							
							sb.mslb_beg_period = uwDao.selectFaddMonths(defaultDateFormat.format(tmp.msl_bdate), i); //bertambah 1 bulan dari deposit date
							sb.mslb_end_period = FormatDate.add(uwDao.selectFaddMonths(defaultDateFormat.format(tmp.msl_bdate), (i+1)), Calendar.DATE, -1);
							sb.mslb_paid_date = commonDao.selectAddWorkdays(sb.mslb_end_period, 1);                   
							sb.mslb_due_date = sb.mslb_end_period;                    
							
							Rekening_client rek = this.select_rek_client(tmp.reg_spaj);
							
							sb.mslb_rekening =           
								rek.getLsbp_nama() + 
								" CAB. " + rek.getMrc_cabang() + 
								"-" + rek.getMrc_kota() + 
								", A/C." + rek.getMrc_no_ac() + 
								"(" + (sb.lku_id.equals("01") ? "Rp." : "US$") + 
								"), A/N. " + rek.getMrc_nama();
							
							sb.mslb_up = tmp.msl_up;                        
							sb.mslb_premi = tmp.msl_premi;                     
							sb.mslb_pinalti = 0.;          
							
							sb.mslb_rate = tmp.msl_rate;
							
							f_hit_umur umr =new f_hit_umur();
							int hari = umr.hari_powersave(
									sb.mslb_beg_period.getYear()+1900,sb.mslb_beg_period.getMonth()+1,sb.mslb_beg_period.getDate(),sb.mslb_end_period.getYear()+1900,sb.mslb_end_period.getMonth()+1,sb.mslb_end_period.getDate());
							hari = hari + 1;
							sb.mslb_jml_hari = hari;			
							
							sb.mslb_bunga = (new Double(hari)/365) * (sb.mslb_rate/100) * sb.mslb_premi;                     
							
							sb.mslb_bp = 0.;                        
							sb.mslb_tarik = 0.;                     
							sb.mslb_jum_bayar = sb.mslb_bunga;                 
							sb.mslb_tgl_nab = null;                     
							sb.mslb_nab = 0.;                       
							sb.mslb_unit = 0.;                      
							sb.mslb_tambah = 0.;                    
							sb.mslb_hari = 0;                     
							sb.mslb_notes = null;                     
							sb.mslb_flag_bayar = 0;               
							sb.mslb_filing = 0;                   
							sb.mslb_filing_date = null;                 
							sb.mslb_bp_pt = 0.;                     
							
							sb.lsbp_id = Integer.valueOf(rek.getLsbp_id());                       
							sb.mrc_cabang = rek.getMrc_cabang();                     
							sb.mrc_atas_nama = rek.getMrc_nama();                  
							sb.mrc_no_ac = rek.getMrc_no_ac();  
							
							sb.mslb_kurang = 0.;   
							sb.flag_bulanan = tmp.flag_bulanan;        
							sb.flag_proses = 0;
							
							if(Integer.parseInt(lsbs_id)==188){
								insert("insert.mst_psave_bayar", sb);
							}else{
								
								insert("insert.mst_slink_bayar", sb);
							}
							
						}
					}else{
						//INSERT UNTUK PREMI TU
						//(Deddy)- req Achmad(15/9/2009) : untuk Simas Stabil Link Bukan Manfaat bulanan & Stable Link Bukan Manfaat bulanan pada laporan alokasi dana awal/Top Up dimana untuk MTI 24 dan 36 bulan perhitungannya :
						if(tmp.msl_mgi>12){
							tmp.msl_bunga = tmp.msl_premi * (Math.pow((1+(tmp.msl_rate/100)),(tmp.msl_mgi/12))-1);
							
//							if(tmp.lku_id.equals("01")){
//								tmp.msl_bunga=FormatNumber.round(tmp.msl_bunga, 0);
//							}else if(tmp.lku_id.equals("02")){
								tmp.msl_bunga=FormatNumber.round(tmp.msl_bunga, 2);
//							}
						}
						if(Integer.parseInt(lsbs_id)==188){
							insert("insert.mst_psave_2", tmp);
						}else{
							
							insert("insert.mst_slink_2", tmp);
						}
						//if(tmp.get)
					}								
				
					uwDao.insertMstPositionSpaj(currentUser.getLus_id(), 
							"Input Top Up BSM ke-" + tmp.msl_tu_ke + " dengan No Reg " + tmp.no_reg, tmp.reg_spaj, 0);
					
					if(tmp.getLku_id().equals("01")){
						if(tmp.getMsl_premi()>=100000000){
						updateRedFlag(pmg.getMcl_id(),1);
						uwDao.insertMstPositionSpajRedFlagTU(currentUser.getLus_id(), "Input TOP-UP > Rp. 100 Juta", trans.reg_spaj, 5,"REDFLAG");
							}
					}else if(tmp.getLku_id().equals("02")){
							if(tmp.getMsl_premi()>=10000){
								updateRedFlag(pmg.getMcl_id(),1);
								uwDao.insertMstPositionSpajRedFlagTU(currentUser.getLus_id(), "Input TOP-UP > $ 10000", trans.reg_spaj, 5,"REDFLAG");	
							}
						
					}
					
					//insert to mst_scan
					String lca_id = uwDao.selectCabangFromSpaj(tmp.reg_spaj);
					String dir = props.getProperty("pdf.dir.export") + "\\" + lca_id;
					String dest=dir+"\\"+tmp.reg_spaj+"\\";
					List<DropDown> dokumen = FileUtils.listFilesInDirectoryStartsWith(dest, tmp.reg_spaj+"FTUP"+tmp.msl_tu_ke);
					Integer size = dokumen.size();
					String nilai = StringUtils.leftPad(size.toString(), 3, "0");
					String destOut =dest+tmp.reg_spaj+"FTUP"+tmp.msl_tu_ke+" "+nilai+".pdf";
					 Scan scan = new Scan();
					   
					    scan.setLus_id(currentUser.getLus_id());
					    scan.setLde_id(currentUser.getLde_id());
					    scan.setNo_indek(tmp.reg_spaj);
					    scan.setTgl_input(sysdate);
					    scan.setFiles_ad(destOut);
					    scan.setTipe_file("FTUP");
					    scan.setJml_page(0);
					    scan.setFlag_aktif(1);
					    scan.setJenis(2);//0 : program upload seno, 1 program SNOWS rudy, 2 program E-Lions deddy
					    boolean a=true;
					    while(a) {
					    	try{
						   		scan.setKd_scan(Integer.parseInt(sequence.sequenceKdScan()) );
							    uwDao.insertMstScan(scan);
							    a=false;
						    }catch (Exception e) {
								// TODO: handle exception
							}
					   	}
					
					return "Data Top Up Berhasil Disimpan. Nomor Registrasi Top Up Adalah " + tmp.no_reg;

				//update topup yg sudah ada
				}else if(trans.simpan_mode.equals("update")) {

					tmp.msl_proses_date		= sysdate;
					tmp.lus_id				= Integer.valueOf(currentUser.getLus_id());
					if(Integer.parseInt(lsbs_id)!=188){
						tmp.msl_bp_rate = getBPrate(tmp.msl_bdate, tmp.msl_mgi, tmp.flag_bulanan);//bonus performance
						if(Integer.parseInt(lsbs_id)==164 && Integer.parseInt(lsdbs_number)==11){
							tmp.msl_bp_rate = 25.; 
						}
					}
					
					//delete dulu slink bayar nya
					delete ("delete.mst_slink_bayar", tmp);	
					delete ("delete.mst_psave_bayar", tmp);
					
					//Yusuf 28 Jul 09 - Tambahan Perhitungan bila Stable Link Manfaat Bulanan (FLAG_BULANAN = 1)
					if(tmp.flag_bulanan.intValue() > 0){
//					if(tmp.flag_bulanan.intValue() == 1){
					
						tmp.msl_bunga = 0.; //bunga di nol kan bila manfaat bulanan
						
						//UPDATE UNTUK PREMI TU 
						tmp.msl_new = 2; //Yusuf (06/01/10) - Req Rudy, Topup selain new business dan dari E-Lions = 2
						
						if(Integer.parseInt(lsbs_id)==188){
							update("update.mst_psave", tmp);
						}else{
							
							update("update.mst_slink", tmp);
						}
						
						for(int i=0; i<tmp.msl_mgi; i++){
							SlinkBayar sb = new SlinkBayar();
							sb.reg_spaj = tmp.reg_spaj;
							sb.msl_no = tmp.msl_no;                        
							sb.lji_id = tmp.lji_id;                         

							//Yusuf (11/09/09) Request Rudy : tidak usah generate disini, diisi angka urut aja
							//sb.mslb_bayar_id = sequence.sequenceNoRegSlinkBayar();                  
							sb.mslb_bayar_id = String.valueOf(i);

							sb.mslb_desc = "Bunga"; //CASE SENSITIVE, karena query rudy banyak yg -> where mslb_desc = 'Bunga'                      
							sb.mslb_tu_ke = tmp.msl_tu_ke;                    
							sb.lus_id = tmp.lus_id;                        
							sb.lku_id = tmp.lku_id;                         
							sb.mslb_print_date = null;       
							
							sb.mslb_beg_period = uwDao.selectFaddMonths(defaultDateFormat.format(tmp.msl_bdate), i); //bertambah 1 bulan dari deposit date
							sb.mslb_end_period = FormatDate.add(uwDao.selectFaddMonths(defaultDateFormat.format(tmp.msl_bdate), (i+1)), Calendar.DATE, -1);
							sb.mslb_paid_date = commonDao.selectAddWorkdays(sb.mslb_end_period, 1);                   
							sb.mslb_due_date = sb.mslb_end_period;                    
							
							Rekening_client rek = this.select_rek_client(tmp.reg_spaj);
							
							sb.mslb_rekening =           
								rek.getLsbp_nama() + 
								" CAB. " + rek.getMrc_cabang() + 
								"-" + rek.getMrc_kota() + 
								", A/C." + rek.getMrc_no_ac() + 
								"(" + (sb.lku_id.equals("01") ? "Rp." : "US$") + 
								"), A/N. " + rek.getMrc_nama();
							
							sb.mslb_up = tmp.msl_up;                        
							sb.mslb_premi = tmp.msl_premi;                     
							sb.mslb_pinalti = 0.;          
							
							sb.mslb_rate = tmp.msl_rate;
							
							f_hit_umur umr =new f_hit_umur();
							int hari = umr.hari_powersave(
									sb.mslb_beg_period.getYear()+1900,sb.mslb_beg_period.getMonth()+1,sb.mslb_beg_period.getDate(),sb.mslb_end_period.getYear()+1900,sb.mslb_end_period.getMonth()+1,sb.mslb_end_period.getDate());
							hari = hari + 1;
							sb.mslb_jml_hari = hari;			
							
							sb.mslb_bunga = (new Double(hari)/365) * (sb.mslb_rate/100) * sb.mslb_premi;                     
							
							sb.mslb_bp = 0.;                        
							sb.mslb_tarik = 0.;                     
							sb.mslb_jum_bayar = sb.mslb_bunga;                 
							sb.mslb_tgl_nab = null;                     
							sb.mslb_nab = 0.;                       
							sb.mslb_unit = 0.;                      
							sb.mslb_tambah = 0.;                    
							sb.mslb_hari = 0;                     
							sb.mslb_notes = null;                     
							sb.mslb_flag_bayar = 0;               
							sb.mslb_filing = 0;                   
							sb.mslb_filing_date = null;                 
							sb.mslb_bp_pt = 0.;                     
							
							sb.lsbp_id = Integer.valueOf(rek.getLsbp_id());                       
							sb.mrc_cabang = rek.getMrc_cabang();                     
							sb.mrc_atas_nama = rek.getMrc_nama();                  
							sb.mrc_no_ac = rek.getMrc_no_ac();  
							
							sb.mslb_kurang = 0.;   
							sb.flag_bulanan = tmp.flag_bulanan;        
							sb.flag_proses = 0;
							
							if(Integer.parseInt(lsbs_id)==188){
								insert("insert.mst_psave_bayar", sb);
							}else{
								
								insert("insert.mst_slink_bayar", sb);
							}
						}
					}else{
						//UPDATE UNTUK PREMI TU
						if(Integer.parseInt(lsbs_id)==188){
							update("update.mst_psave", tmp);
						}else{
							
							update("update.mst_slink", tmp);
						}
					}								
					
					uwDao.insertMstPositionSpaj(currentUser.getLus_id(), 
							"Edit Top Up BSM ke-" + tmp.msl_tu_ke + " dengan No Reg " + tmp.no_reg, tmp.reg_spaj, 0);	
					
					if(tmp.getLku_id().equals("01")){
						if(tmp.getMsl_premi()>=100000000){
						updateRedFlag(pmg.getMcl_id(),1);
						uwDao.insertMstPositionSpajRedFlagTU(currentUser.getLus_id(), "Input TOP-UP > Rp. 100 Juta", trans.reg_spaj, 5,"REDFLAG");
							}
					}else if(tmp.getLku_id().equals("02")){
							if(tmp.getMsl_premi()>=10000){
								updateRedFlag(pmg.getMcl_id(),1);
								uwDao.insertMstPositionSpajRedFlagTU(currentUser.getLus_id(), "Input TOP-UP > $ 10000", trans.reg_spaj, 5,"REDFLAG");	
							}
						
					}
					
					return "Data Top Up Dengan Nomor Registrasi " + tmp.no_reg + " Berhasil Diupdate";

				//delete topup yg sudah ada
				}else if(trans.simpan_mode.equals("delete")) {
					
					//1. hapus dulu file PDF yg pernah digenerate
					//contoh : TOP_UP_2_24164200900002_20090416.PDF
					String cabang 		= uwDao.selectCabangFromSpaj(tmp.reg_spaj);
					String directory 	= props.getProperty("pdf.dir.rollover") + "\\StableLink\\" + cabang + "\\" + tmp.reg_spaj;
					if(Integer.parseInt(lsbs_id)==188){
						directory 	= props.getProperty("pdf.dir.rollover") + "\\PowerSave\\" + cabang + "\\" + tmp.reg_spaj;
					}
					DateFormat df 		= new SimpleDateFormat("yyyyMMdd");
					try {
						FileUtils.deleteFile(directory, 
								"TOP_UP_" + tmp.msl_tu_ke + "_" + uwDao.selectPolicyNumberFromSpaj(tmp.reg_spaj) + "_" + df.format(tmp.msl_bdate) + ".PDF");
					} catch (FileNotFoundException e) {
						logger.error("ERROR :", e);
					} catch (IOException e) {
						logger.error("ERROR :", e);
					}
					
					//2. delete dulu slink bayar nya
					//delete dulu slink bayar nya
					delete ("delete.mst_slink_bayar", tmp);						
					delete ("delete.mst_psave_bayar", tmp);
						
					//delete history otorisasi top up
					if(uwDao.selectMstPositionSpajOtorisasi(tmp.reg_spaj, tmp.msl_tu_ke)>0){
						Map param=new HashMap();
						param.put("spaj", tmp.reg_spaj);
						param.put("ket", "LIKE '%OTORISASI INPUT TOPUP KE-"+tmp.msl_tu_ke+"%'");
						delete ("delete.mst_position_spaj_otor_topup", param);
					}
					
					//3. baru delete TU nya
					deleteMst_slinkTopup(tmp);
					deleteMst_psaveTopup(tmp);
					
					if(tmp.getLku_id().equals("01")){
						if(tmp.getMsl_premi()>=100000000){
						updateRedFlag(pmg.getMcl_id(),0);
							}
					}else if(tmp.getLku_id().equals("02")){
							if(tmp.getMsl_premi()>=10000){
								updateRedFlag(pmg.getMcl_id(),0);
							}
						
					}
					
					uwDao.insertMstPositionSpaj(currentUser.getLus_id(), 
							"Hapus Top Up BSM ke-" + tmp.msl_tu_ke + " dengan No Reg " + tmp.no_reg, tmp.reg_spaj, 0);
					
					return "Data Top Up Dengan Nomor Registrasi " + tmp.no_reg + " Berhasil Dihapus";
					
				//transfer topup yg sudah ada
				}else if(trans.simpan_mode.equals("transfer")) {

					InputTopup topup = new InputTopup();
					topup.msl_proses_date	= sysdate;
					topup.lus_id			= Integer.valueOf(currentUser.getLus_id());
					topup.reg_spaj			= tmp.reg_spaj;
					topup.lji_id			= tmp.lji_id;
					topup.msl_no			= tmp.msl_no;
					topup.msl_posisi		= 73;
				
					uwDao.insertMstPositionSpaj(currentUser.getLus_id(), 
							"Transfer Top Up BSM ke-" + tmp.msl_tu_ke + " dengan No Reg " + tmp.no_reg + " ke Finance", tmp.reg_spaj, 0);
					
					
					/**
					 * kasi gratis rider apabila 
					 * - premi > Rp 100 juta / $10000
					 * - 1 rider per polis
					 * MULAI 1 SEPTEMBER 2009
					 * @author Berto
					 * 7 Agustus 2009
					 */
					if(Integer.parseInt(lsbs_id)!=188){
						Boolean freeFluBabiAss=false;
						Double totPremiLama=selectSumPremiSlinkLama(tmp.reg_spaj);
						Double totPremi=selectSumPremiSlink(tmp.reg_spaj);
						if(tmp.lku_id.equals("01")) {
							if(totPremiLama.doubleValue()>=100000000){
								if(totPremi.doubleValue() >= 10000000)freeFluBabiAss=true ;
							}else{
								if(totPremi.doubleValue() >= 100000000)freeFluBabiAss=true ;
							}
						}else if(tmp.lku_id.equals("02"))  {
							if(totPremiLama.doubleValue()>=10000){
								if(totPremi.doubleValue() >= 1000) freeFluBabiAss=true ;
							}else{
								if(totPremi.doubleValue() >= 10000) freeFluBabiAss=true ;
							}
						}	
						String bab1="";
						if(freeFluBabiAss){
							if(selectCountFreeFluBabi(tmp.reg_spaj)==0){
								//hore dapet gratis flue babi, proses endorse ya...
								Date tglNow=tmp.getMsl_bdate();
								
								Date tglStart=null;
								Date tglEnd=null;
								try {
									tglStart = new SimpleDateFormat("dd/MM/yyyy").parse("11/10/2009"); 
									tglEnd = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2010");
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									logger.error("ERROR :", e);
								}						
								
								if(tglNow.after(tglStart) && tglNow.before(tglEnd)){
									freeFlueBabi(tmp.reg_spaj,tmp.getLus_id(),tmp);		
									topup.setMsl_flag_cb(1);
								}
							}
						}
					}
					topup.setMsen_endors_no(tmp.getMsen_endors_no());
					if(Integer.parseInt(lsbs_id)==188){
						update("update.mst_psave", topup);
					}else{
						
						update("update.mst_slink", topup);
					}
					
					return "Data Top Up Dengan Nomor Registrasi " + tmp.no_reg + " Berhasil Ditransfer. Harap masuk ke menu PRINT TOP UP untuk melakukan pencetakan.";

				}
				
			}
		}
				
		return "Data tidak berhasil diproses. Harap konfirmasi dengan dept IT AJS";
	}
	
	/**
	 * UNtuk program gratis flue burung
	 * 1. update lspd_id = 13 mst_policy
	 * 2. isnert ke table mst_endors
	 * 3. insert ke table mst_det_endors
	 * 4. insert seluruh prod yang ada mst_product_insured ke table mst_product_ins_end plus produk rider barunya
	 * @param reg_spaj
	 * Filename : BacDao.java
	 * Create By : Bertho Rafitya Iwasurya
	 * Date Created : Aug 7, 2009 10:23:20 AM
	 */
	public void freeFlueBabi (String reg_spaj,Integer lus_id, InputTopup inputTopup){
		Pemegang2 pmg=basDao.selectpemegangpolis(reg_spaj);
		
		//uwDao.updatePosisiMst_policy(reg_spaj, null, 13);
		Endors endors=new Endors();
		
		endors.setMsen_endors_no(sequence.sequenceNo_Endorse(pmg.getLca_id()));
		endors.setReg_spaj(reg_spaj);
		endors.setMsen_internal(0);
		endors.setMsen_alasan(new SimpleDateFormat("dd-MM-yyyy").format(inputTopup.getMsl_bdate())+"(Accepted by Underwriting)");
		endors.setMsen_active_date(inputTopup.getMsl_bdate());
		endors.setMsen_surat("INSERT OTOMATIS");
		endors.setMsen_print(0);
		endors.setLspd_id(99);
		endors.setLus_id(lus_id);
		endors.setMsen_auto_rider(2);//0 : RUDI	1 : dari dedy 2 : dari bertho --Otomatis Insert u/Rider Swine Flu Stable Link & Stable Save
		endors.setFlag_ps(2);//konvensional = 0 powersave = 1 stable link = 2 stable save = 3
		endors.setMsen_proses_bsm(1);
		endors.setMsen_tgl_trans(commonDao.selectSysdate());
		
		insertMstEndors(endors);
		
		Datausulan du=selectDataUsulanutama(reg_spaj);
		
		DetEndors detEndors=new DetEndors();
		detEndors.setMsen_endors_no(endors.getMsen_endors_no());
		detEndors.setMsenf_number(1);
		detEndors.setLsje_id(49);//PERUBAHAN RIDER lst_jn_endors
		detEndors.setMste_insured_no(1);
		detEndors.setMsde_old1(du.getLscb_pay_mode());
		detEndors.setMsde_old2(du.getLsdbs_name());
		detEndors.setMsde_old3(du.getLku_symbol());
		detEndors.setMsde_old4(FormatString.formatCurrency("", new BigDecimal(du.getMspr_premium())));
		detEndors.setMsde_old5(FormatString.formatCurrency("", new BigDecimal(du.getMspr_tsi())));
//		detEndors.setMsde_old6(msde_old1);
		detEndors.setMsde_new1(du.getLscb_pay_mode());
		detEndors.setMsde_new2(du.getLsdbs_name());
		detEndors.setMsde_new3(du.getLku_symbol());
		detEndors.setMsde_new4(FormatString.formatCurrency("", new BigDecimal(du.getMspr_premium())));
		detEndors.setMsde_new5(FormatString.formatCurrency("", new BigDecimal(du.getMspr_tsi())));
//		detEndors.setMsde_new6(msde_old1);
		
		insertMstDetEndors(detEndors);
		
		// masukin semua produk lama yang dipunyai
		insertMstProductInsEndAllProdLama(endors.getMsen_endors_no(), reg_spaj, lus_id, pmg.getLscb_id(), du.getMspr_tsi(), du.getMspr_premium(), du.getLsbs_id(), du.getLsdbs_number());
		
		//tambahin rider barunya
		ProductInsEnd prod=new ProductInsEnd();
		prod.setMsen_endors_no(endors.getMsen_endors_no());
		prod.setReg_spaj(reg_spaj);
		prod.setMste_insured_no(du.getMste_insured_no());
		prod.setLsbs_id(822);
		prod.setLsdbs_number(1);
		prod.setLku_id(du.getLku_id());
		prod.setMspie_beg_date(inputTopup.getMsl_bdate());
		prod.setMspie_end_date(FormatDate.add(FormatDate.add(prod.getMspie_beg_date(), Calendar.MONTH, 12),Calendar.DATE,-1));
		Double UP=0.0;
		if(du.getLku_id().equals("01")) {
			UP=new Double (20000000);
		}else if(du.getLku_id().equals("02")) {
			UP=new Double (2000);
		}
		prod.setMspie_tsi(UP);
		prod.setMspie_tsi_a(new Double(0));
		prod.setMspie_tsi_b(new Double(0));
		prod.setMspie_tsi_c(new Double(0));
		prod.setMspie_tsi_d(new Double(0));
		prod.setMspie_tsi_m(new Double(0));
		prod.setMspie_class(du.getMspr_class());
		prod.setMspie_unit(du.getMspr_unit());
		prod.setMspie_rate(new Double(0));
		prod.setMspie_persen(0);
		prod.setMspie_premium(new Double(0));
		prod.setMspie_discount(new Double(0));
		prod.setMspie_extra(du.getMspr_extra());
		prod.setMspie_ins_period(1);
		prod.setLus_id(lus_id);
		prod.setLscb_id(pmg.getLscb_id());
		prod.setLst_lsbs_id(null);
		prod.setLst_lsdbs_number(null);
		prod.setMspie_tsi_old(null);
		prod.setMspie_premium_old(null);
		prod.setMspie_disc_old(new Double(0));
		prod.setLsbs_id_old(null);
		prod.setLsdbs_num_old(null);
		prod.setMspie_rate_old(null);
		prod.setLscb_id_old(null);
		prod.setMspie_premium_prod(null);
		prod.setMspie_discount_prod(null);
		
		insertMstProductInsEnd(prod);
		Product product=new Product();
		product.setReg_spaj(reg_spaj);
		product.setMste_insured_no(du.getMste_insured_no());
		product.setLsbs_id(822);
		product.setLsdbs_number(1);
		product.setLku_id(du.getLku_id());
		product.setMspr_beg_date(inputTopup.getMsl_bdate());
		product.setMspr_end_date(prod.getMspie_end_date());
		product.setMspr_tsi(prod.getMspie_tsi());
		product.setMspr_tsi_pa_a(prod.getMspie_tsi_a());
		product.setMspr_tsi_pa_b(prod.getMspie_tsi_b());
		product.setMspr_tsi_pa_c(null);
		product.setMspr_tsi_pa_d(prod.getMspie_tsi_d());
		product.setMspr_tsi_pa_m(new Double(0));
		product.setMspr_class(du.getMspr_class());
		product.setMspr_unit(du.getMspr_unit());
		product.setMspr_rate(new Double(0));
		product.setMspr_persen(0);
		product.setMspr_premium(new Double(0));
		product.setMspr_discount(new Double(0));
		product.setMspr_ref_no(null);
		product.setMspr_active(1);
		product.setMspr_extra(new Double(0));		
		product.setMspr_ins_period(1);
		product.setMspr_bunga_kpr(null);
		product.setMspr_ins_bulan(null);
		product.setMspr_sisa_limit(null);	
		product.setMspr_wait_periode(null);
		product.setMspr_ujroh(null);
		product.setMspr_tabarru(null);
		product.setMspr_end_pay(null);
		product.setMspr_tt(0);
		product.setMspr_tsi_pa_a(null);
		product.setMspr_pinjm_awal(null);
		
		insertMst_product_insured(product);
		
		inputTopup.setMsen_endors_no(endors.getMsen_endors_no());
	}	
	
	public InputTopup selectInputTopupBaruStableLink(String reg_spaj) throws DataAccessException{
		return (InputTopup) querySingle("selectInputTopupBaruStableLink", reg_spaj);
	}
	
	public InputTopup selectInputTopupBaruPowerSave(String reg_spaj) throws DataAccessException{
		return (InputTopup) querySingle("selectInputTopupBaruPowerSave", reg_spaj);
	}
	
	public InputTopup selectEntryTransStableLink(String reg_spaj) throws DataAccessException{
		return (InputTopup) querySingle("selectEntryTransStableLink", reg_spaj);
	}
	
	public List<InputTopup> selectEntryTopupStableLink(String reg_spaj, int msl_posisi) throws DataAccessException{
		Map params = new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("msl_posisi", msl_posisi);
		return query("selectEntryTopupStableLink", params);
	}
	
	public List<InputTopup> selectEntryTopupPowerSave(String reg_spaj, int msl_posisi) throws DataAccessException{
		Map params = new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("msl_posisi", msl_posisi);
		return query("selectEntryTopupPowerSave", params);
	}
	
	public List<InputTopup> selectOtorisasiTopupStableLink(String reg_spaj, int msl_posisi, String[] tempTuKe, String lusId, Integer mslNo) throws DataAccessException{
		Map params = new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("msl_posisi", msl_posisi);
		params.put("tempTuKe", tempTuKe);
		params.put("lusId", lusId);
		params.put("mslNo", mslNo);
		return query("selectOtorisasiTopupStableLink", params);
	}
	
	public String selectBlanko(String reg_spaj) throws DataAccessException{
		return (String) querySingle("selectBlanko", reg_spaj);
	}
	
	public Map selectKonfirmasiTransferBac(String reg_spaj) throws DataAccessException{
		return (Map) querySingle("selectKonfirmasiTransferBac", reg_spaj);
	}
	
	public List<PowersaveCair> selectReportCair(Map m) throws DataAccessException{
		return query("selectReportCair", m);
	}
	
	public List<PowersaveCair> selectReportCairSLinkNew(Map m) throws DataAccessException{
		return query("selectReportCairSLinkNew", m);
	}
	
	public List<Maturity> selectReportMaturity(Map m) throws DataAccessException {
		return query("selectReportMaturity", m);
	}
	
	public List<PowersaveCair> selectDaftarCair(int posisi, int jenis, String cab_bank) throws DataAccessException{
		Map m = new HashMap();
		m.put("posisi", posisi);
		m.put("jenis", jenis);
		m.put("cab_bank", cab_bank);
		return query("selectDaftarCair", m);
	}
	
	public List selectDaftarSpajOtorisasi( String []cabBank, String kata, String tipe, String pilter ) throws DataAccessException{
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
		/*for(int i=0; i<cabBank.length;i++){
			tes.append(cabBank.toString().split(","));
		}*/
		List<String> rese = new ArrayList<String>();
		for(String daftarDeddy : cabBank) {
			daftarDeddy=daftarDeddy.replaceAll(" ", "");
			daftarDeddy.replaceAll(" ", "");
			rese.add(daftarDeddy);
			//params.put("cabBank", rese);
		}
		params.put("cabBank", rese);
		params.put("kata", kata);
		params.put("tipe", tipe);
	   /* List list1;
		List list2 = null;
		for(String s : cabBank) {
			list1=query("selectDaftarSpajOtorisasi", params);
			for(int i =0;i<list1.size();i++){
				list2.add(list1.get(i));
			}
		}
		List <String> rese = new ArrayList <String>();
		rese.addAll(list2);
		params.put("cabBank", rese);*/
		return query("selectDaftarSpajOtorisasi", params);
	}
	
	public List selectDaftarSpajOtorisasiSpajDanaSekuritas( String cabBank, String kata, String tipe, String pilter ) throws DataAccessException{
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
		params.put("cabBank", cabBank);
		params.put("kata", kata);
		params.put("tipe", tipe);
		return query("selectDaftarSpajOtorisasiSpajDanaSekuritas", params);
	}
	

	public List selectSpajOtorisasiDisabled( String spaj ) throws DataAccessException{
		Map param = new HashMap();
		param.put("spaj",spaj);
		return (List) query("selectSpajOtorisasiDisabled", param);
	}
	
	public List selectAccessMenuOtorisasiSpaj( String cabBank, String jn_bank ) throws DataAccessException{
		Map param = new HashMap();
		param.put( "cabBank", cabBank );
		param.put("jn_bank", jn_bank);
		return (List) query("selectAccessMenuOtorisasiSpaj", param);
	}
	
	public List selectMslTuKeMstPositionSpajList( String regSpaj ) throws DataAccessException{
		Map param = new HashMap();
		param.put( "regSpaj", regSpaj );
		return (List) query("selectMslTuKeMstPositionSpajList", param);
	}
	
	public void updateMstPowersaveCairPosisi(PowersaveCair p) throws DataAccessException{
		update("update.mst_powersave_cair.posisi", p);
	}
	
	public void updateRedFlag(String mcl_id, Integer flag) throws DataAccessException{
		Map param = new HashMap();
		param.put( "mcl_id", mcl_id );
		param.put( "flag", flag );
		update("update.updateRedFlag", param);
	}
	
	public void updateMstDatePrintToNull(String reg_spaj) throws DataAccessException{
		Map param = new HashMap();
		param.put( "reg_spaj", reg_spaj );
		
		update("updateMstDatePrintToNull", param);
	}
	
	public void updateTanggalKonfirmasiToNull(String reg_spaj,String tgl_konfirmasi, String tgl_generate) throws DataAccessException{
		Map param = new HashMap();
		param.put( "reg_spaj", reg_spaj );
		param.put( "tgl_konfirmasi", tgl_konfirmasi );
		param.put( "tgl_generate", tgl_generate );
		update("updateTanggalKonfirmasiToNull", param);
	}
	
	public void updateFlagKonfirmasi(String reg_spaj, String tgl_generate,int flag_konfirmasi, int flag_konfirmasi_target) throws DataAccessException{
		Map param = new HashMap();
		param.put( "reg_spaj", reg_spaj );
		param.put( "tgl_generate", tgl_generate );
		param.put( "flag_konfirmasi", flag_konfirmasi );
		param.put( "flag_konfirmasi_target", flag_konfirmasi_target );
		update("updateFlagKonfirmasi", param);
	}
	//IGA UPDATE RDS SCREENING
	public void updateKetGenerate(String reg_spaj, String tgl_generate, String keterangan) throws DataAccessException{
		Map param = new HashMap();
		param.put( "reg_spaj", reg_spaj );
		param.put( "tgl_generate", tgl_generate );
		param.put("keterangan", keterangan);
		update("updateKetGenerate", param);
	}
	
	public void deleteMst_slinkTopup(InputTopup topup) throws DataAccessException{
		delete("delete.mst_slink.topup", topup);
	}
	
	
	
	
	public void deletePositionSpajGenerate(String reg_spaj) throws DataAccessException{
		delete("deletePositionSpajGenerate", reg_spaj);
	}
	
	public void deleteMst_psaveTopup(InputTopup topup) throws DataAccessException{
		delete("delete.mst_psave.topup", topup);
	}
	
	public void deleteMstPowersaveCair(PowersaveCair p) throws DataAccessException{
		insert("delete.mst_powersave_cair", p);
	}
	
	public void insertMstPowersaveCair(PowersaveCair p) throws DataAccessException{
		insert("insert.mst_powersave_cair", p);
	}
	
	public String SelectTotalRiderPowersave(String reg_spaj) throws DataAccessException{
		return (String) querySingle("SelectTotalRiderPowersave", reg_spaj);
	}
	
	public boolean selectValidasiHapusPowersaveCair(String reg_spaj, int mpc_urut, Date mpc_bdate) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("mpc_urut", mpc_urut);
		m.put("mpc_bdate", mpc_bdate);
		int result = (Integer) querySingle("selectValidasiHapusPowersaveCair", m);
		if(result == 0) return true; else return false;
	}
	
	public boolean selectValidasiPowersaveCair(String reg_spaj, int mpc_urut, Date mpc_bdate) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("mpc_urut", mpc_urut);
		m.put("mpc_bdate", mpc_bdate);
		int result = (Integer) querySingle("selectValidasiPowersaveCair", m);
		if(result == 0) return true; else return false;
	}

	public Integer selectNonCashPowersaveCair(String reg_spaj, int mpc_urut, Date mpc_bdate) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("mpc_urut", mpc_urut);
		m.put("mpc_bdate", mpc_bdate);
		return (Integer) querySingle("selectNonCashPowersaveCair", m);
	}
	
	public String selectNoRegPencairan(String reg_spaj, int mpc_urut, Date mpc_bdate) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("mpc_urut", mpc_urut);
		m.put("mpc_bdate", mpc_bdate);
		String result = (String) querySingle("selectNoRegPencairan", m);
		return result;
	}
	
	public String selectPosisiPowersaveCair(String reg_spaj, int mpc_urut, Date mpc_bdate) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("mpc_urut", mpc_urut);
		m.put("mpc_bdate", mpc_bdate);
		Integer result = (Integer) querySingle("selectPosisiPowersaveCair", m);
		if(result == null) return "#ffffff";
		else if(result == 1) return "#ffffcc";
		else if(result == 2) return "#ccffcc";
		else if(result == 3) return "#ffccff";
		else return "";
	}
	
	public PowersaveCair selectRolloverData(String reg_spaj) throws DataAccessException{
		return (PowersaveCair) querySingle("selectRolloverData", reg_spaj);
	}
	
	public List<Map> selectMstPolicyBasedRegSpaj(String reg_spaj) throws DataAccessException{
		return (List<Map>) query("selectMstPolicyBasedRegSpaj", reg_spaj);
	}
	
	public List<PowersaveCair> selectRolloverPowersaveNormal(String reg_spaj) throws DataAccessException{
		return query("selectRolloverPowersaveNormal", reg_spaj);
	}
	
	public List<PowersaveCair> selectRolloverPowersavePeriodeLibur(String reg_spaj) throws DataAccessException{
		return query("selectRolloverPowersavePeriodeLibur", reg_spaj);
	}
	
	public List<PowersaveCair> selectRolloverPowersaveSusulan(String reg_spaj) throws DataAccessException{
		return query("selectRolloverPowersaveSusulan", reg_spaj);
	}
	
	public List<PowersaveCair> selectRolloverStableLinkNormal(String reg_spaj) throws DataAccessException{
		return query("selectRolloverStableLinkNormal", reg_spaj);
	}

	public Integer selectSelisihBatasDatePowerSave(PowersaveCair pc) throws DataAccessException{
		return (Integer) querySingle("selectSelisihBatasDatePowerSave", pc);
	}
	
	public Integer selectSelisihBatasDateStableLink(PowersaveCair pc) throws DataAccessException{
		return (Integer) querySingle("selectSelisihBatasDateStableLink", pc);
	}
	
	public HashMap selectJumlahBayarManfaatBulananTerakhir(String reg_spaj, int msl_no, Date msl_bdate) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("msl_no", msl_no);
		m.put("msl_bdate", msl_bdate);
		HashMap hasil = (HashMap) querySingle("selectJumlahBayarManfaatBulananTerakhir", m); 
		return hasil;
	}
	
	public Map selectPengurangManfaatBulananTerakhir(String reg_spaj, int msl_no, Date msl_edate) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("msl_no", msl_no);
		m.put("msl_edate", msl_edate);
		Map hasil = (Map) querySingle("selectPengurangManfaatBulananTerakhir", m); 
		return hasil;
	}
	
	public void updateProdDateSlink(String reg_spaj, Date prodDate) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("prodDate", prodDate);
		update("updateProdDateSlink", m);
	}
	
	public List<Map> selectGetKontrakList(String status, String fileName) throws DataAccessException {
		Map p = new HashMap();
		p.put("status", status);
		p.put("fileName", fileName);
		return query("selectGetKontrakList",p);
	}
	
	public String selectGetPDF(String status, String fileName) throws DataAccessException {
		String filename2 = null;
		Map p = new HashMap();
		p.put("status", status);
		p.put("fileName", fileName);
		if(fileName.equals("BM(REGIONAL)")){
			filename2 = "SBM(REGIONAL)";
			p.put("fileName2", filename2);
		}
		String a = (String) querySingle("selectGetPDF",p);
		logger.info("a : " + a);
		return a;
	}
	
	public List selectLstAgenAgency(String indexCabangAgency)throws DataAccessException{
//	public List selectLstAgenAgency(String[] a)throws DataAccessException{
		return query("selectLstAgenAgency", indexCabangAgency);
	}
	
	public List selectLstAgen(String lca_id)throws DataAccessException{
		return query("selectLstAgen", lca_id);
	}
	
	public List selectLstAgenAKM(String lca_id)throws DataAccessException{
		return query("selectLstAgenAKM", lca_id);
	}
	
	public List selectLstAgenBP(String lca_id)throws DataAccessException{
		return query("selectLstAgenBP", lca_id);
	}
	
	public List selectLstHybridArthaMas(String lshb_id)throws DataAccessException{
		return query("selectLstHybridArthaMas", lshb_id);
	}
	
	public List selectLstHybridAJS(String lshb_id)throws DataAccessException{
		return query("selectLstHybridAJS", lshb_id);
	}
	
	public String selectNamaLeader(String msag_id)throws DataAccessException{
		return (String) querySingle("selectNamaLeader", msag_id);
	}
	
	public List selectReportSummaryBiasa(Map map) {
		return query("selectReportSummaryBiasa", map);
	}
	
	public List selectReportSimasCardDistribution(Map map){
		return query("selectReportSimasCardDistribution", map);
	}
	
	public List selectLstAgenRegional(String indexCabang)throws DataAccessException{
		Map map = new HashMap();
		map.put("indexCabang", indexCabang);
		return query("selectLstAgenRegional", map);
	}
	
//	public List selectLstAgenRegional(String lca_id1, String lca_id2, String lca_id3, String lca_id4)throws DataAccessException{
//		Map map = new HashMap();
//		map.put("lca_id1", lca_id1);
//		map.put("lca_id2", lca_id2);
//		map.put("lca_id3", lca_id3);
//		map.put("lca_id4", lca_id4);
//		return query("selectLstAgenRegional", map);
//	}
	
	public List selectLstAgencyArthamas(String lwk_flag)throws DataAccessException{
		Map map = new HashMap();
		map.put("lwk_flag", lwk_flag);
		return query("selectLstAgencyArthamas", map);
	}
	
	public Integer selectValidasiPinjamanKonvensional(String reg_spaj) throws DataAccessException{
		return (Integer) querySingle("selectValidasiPinjamanKonvensional", reg_spaj);
	}
	
	public Integer selectValidasiPinjaman(String reg_spaj, int flag_ps) throws DataAccessException{
		Map p = new HashMap();
		p.put("reg_spaj", reg_spaj);
		p.put("flag_ps", flag_ps);
		return (Integer) querySingle("selectValidasiPinjaman", p);
	}
	
	public List selectPunyaStableLink(String lku_id, Date pp_dob, String pp_name, Date tt_dob, String tt_name) throws DataAccessException{
		Map p = new HashMap();
		p.put("lku_id", lku_id);
		p.put("pp_dob", pp_dob);
		p.put("pp_name", pp_name);
		p.put("tt_dob", tt_dob);
		p.put("tt_name", tt_name);
		return query("selectPunyaStableLink", p);
	}
	
	public List selectPunyaStableLinkBAC(String lku_id, Date pp_dob, String pp_name, Date tt_dob, String tt_name, String msag_id, Integer lsbs_id, Integer lsdbs_number) throws DataAccessException{
		Map p = new HashMap();
		p.put("lku_id", lku_id);
		p.put("pp_dob", pp_dob);
		p.put("pp_name", pp_name);
		p.put("tt_dob", tt_dob);
		p.put("tt_name", tt_name);
		p.put("msag_id", msag_id);
		p.put("lsbs_id", lsbs_id);
		p.put("lsdbs_number", lsdbs_number);
		return query("selectPunyaStableLink", p);
	}
	
	public Integer selectSudahSurrender(String reg_spaj) throws DataAccessException{
		return (Integer) querySingle("selectSudahSurrender", reg_spaj);
	}
	
	public String createPass(String reg_spaj) throws DataAccessException{
		return (String) querySingle("selectPass", reg_spaj);
	}
	
	public int selectCountProsaveBayar(String spaj) throws DataAccessException{
		return (Integer) querySingle("selectCountProsaveBayar", spaj);
	}
	
	public Integer selectValidasiEditUnitLink(String spaj) throws DataAccessException{
		return (Integer) querySingle("selectValidasiEditUnitLink", spaj);
	}
	
	public List<Map> selectValidasiStableLink(String spaj) throws DataAccessException{
		return query("selectValidasiStableLink", spaj);
	}
	
	public List<Map> selectValidasiNewStableLink(String spaj) throws DataAccessException{
		return query("selectValidasiNewStableLink", spaj);
	}
	
	public List selectAgentHist(String msag_id) throws DataAccessException{
		return query("selectAgentHist", msag_id);
	}
	
	public List selectFlagBmSbm(String msag_id) throws DataAccessException{
		return query("selectFlagBmSbm", msag_id);
	}
	
	public String saveHistory(String msag_id) throws DataAccessException{
		return (String) querySingle("saveHistory", msag_id);
	}
	
	public List selectKetAgent(String msag_id) {
		return query("selectKetAgent", msag_id);
	}
	
	public String selectVP(String msag_id) {
		
		return (String) querySingle("selectVP", msag_id);
	}
	public void insertHistory(Map content) throws DataAccessException {
		insert("insertHistory", content);
	}
	
	public void updateHistory(Map content) throws DataAccessException {
		update("updateHistory", content);
	}
	
	public List selectCariAgen(String cari, String tgllahir, String lus_id) throws DataAccessException{
		Map params = new HashMap();
		params.put("cari", cari);
		params.put("lus_id", lus_id);
		params.put("tgllahir", tgllahir);
		return query("selectCariAgen", params);
	}
	
	public Map selectInformasiRegionUntukReferral(String spaj) throws DataAccessException{
		return (Map) querySingle("selectInformasiRegionUntukReferral", spaj);
	}
	
	public int selectUsiaTertanggung(String spaj) throws DataAccessException{
		return (Integer) querySingle("selectUsiaTertanggung", spaj);
	}
	
	public Double selectRateRider(String lku, int umurTertanggung, int umurPemegang, int lsbs, int jenis) throws DataAccessException {
		Map params = new HashMap();
		params.put("lku", lku);
		params.put("umurTertanggung", umurTertanggung);
		params.put("umurPemegang", umurPemegang);
		params.put("lsbs", lsbs);
		params.put("jenis", jenis);
		return (Double) getSqlMapClientTemplate().queryForObject("elions.n_prod.selectRateRider", params);
	}
	
	public List selectDaftarPolisOtorisasiUwSimasPrima(Integer dari, Integer sampai) throws DataAccessException{
		Map p = new HashMap();
		p.put("dari", dari);
		p.put("sampai", sampai);
		return query("selectDaftarPolisOtorisasiUwSimasPrima", p);
	}

	public List selectDaftarPolisOtorisasiUwSimasPrimaUW(Integer dari, Integer sampai) throws DataAccessException{
		Map p = new HashMap();
		p.put("dari", dari);
		p.put("sampai", sampai);
		return query("selectDaftarPolisOtorisasiUwSimasPrimaUW", p);
	}
	
	public List selectDaftarPolisOtorisasiBankSinarmas(int lus_id, Integer dari, Integer sampai) throws DataAccessException{
		Map p = new HashMap();
		p.put("lus_id", lus_id);
		p.put("dari", dari);
		p.put("sampai", sampai);
		return query("selectDaftarPolisOtorisasiBankSinarmas", p);
	}
	
	public List selectDaftarPolisValidasiMaterai(String cab_bank, Integer dari, Integer sampai) throws DataAccessException{
		Map p = new HashMap();
		p.put("cab_bank", cab_bank);
		p.put("dari", dari);
		p.put("sampai", sampai);
		return query("selectDaftarPolisValidasiMaterai", p);
	}
	
	public List selectDaftarPolisOtorisasiSekuritas(int lus_id, Integer dari, Integer sampai) throws DataAccessException{
		Map p = new HashMap();
		p.put("lus_id", lus_id);
		p.put("dari", dari);
		p.put("sampai", sampai);
		return query("selectDaftarPolisOtorisasiSekuritas", p);
	}
	
	public int selectIsSupervisorOrPincabBankSinarmas(int lus_id) throws DataAccessException{
		return (Integer) querySingle("selectIsSupervisorOrPincabBankSinarmas", lus_id);
	}
	
	public Map select_validbank(Integer lus_id)
	{
		return (HashMap)querySingle("select_validbank",lus_id);
	}
	
	public int selectcount_rate(Integer id)
	{
		return (Integer) querySingle("selectcount_rate",id);
	}
	
	public int selectCountRateBankSinarmas(int lsbs_id, int lsdbs_number, int flag_bulanan, String begdate, String lku_id, int mgi) throws DataAccessException{
		Map m =  new HashMap();
		m.put("lsbs_id", lsbs_id);
		m.put("lsdbs_number", lsdbs_number);
		m.put("flag_bulanan", flag_bulanan);
		m.put("begdate", begdate);
		m.put("lku_id", lku_id);
		m.put("mgi", mgi);
		return (Integer) querySingle("selectCountRateBankSinarmas", m);
	}
	
	public int selectIsUserYangInputSekuritas(String spaj, Integer lus_id) throws DataAccessException{
		Map p = new HashMap();
		p.put("spaj", spaj);
		p.put("lus_id", lus_id);
		return (Integer) querySingle("selectIsUserYangInputSekuritas", p);
	}
	
	public int selectCountOtorisasiSpaj(String spaj) throws DataAccessException{
		return (Integer) querySingle("selectCountOtorisasiSpaj", spaj);
	}
	
	public int selectIsUserYangInputBank(String spaj, Integer lus_id) throws DataAccessException{
		Map p = new HashMap();
		p.put("spaj", spaj);
		p.put("lus_id", lus_id);
		return (Integer) querySingle("selectIsUserYangInputBank", p);
	}
	
	public int selectIsUserYangInputDanaSekuritas(String spaj, String cabBank) throws DataAccessException{
		Map p = new HashMap();
		p.put("spaj", spaj);
		p.put("cabBank", cabBank);
		return (Integer) querySingle("selectIsUserYangInputDanaSekuritas", p);
	} 
	
	public List<KuesionerPelayananDetails> selectMstKuesionerPelayananByInsertDate(Date begDate, Date endDate) throws DataAccessException{
		Map p = new HashMap();
		p.put("begDate", begDate);
		p.put("endDate", endDate);
		return (List<KuesionerPelayananDetails>) query("selectMstKuesionerPelayananByInsertDate", p);
	} 
	
	public int selectIsUserInputBank(Integer lus_id) throws DataAccessException{
		Integer result = (Integer) querySingle("selectIsUserInputBank", lus_id);
		if(result == null) return -1; 
		else return result;
	}

	//2 untuk bank sinarmas
	public int selectIsInputanBank(String spaj) throws DataAccessException{
//		Integer result = (Integer) querySingle("selectIsInputanBank", spaj);
//		if(result == null) return -1; 
//		else return result;
		Integer result = (Integer) querySingle("selectIsInputanBankByReferral", spaj);
		if(result == null) return -1; 
		else return result;
	}
	
	public Map select_tminus(String lji_id, Date tgl, int minus)
	{
		Map param = new HashMap();
		param.put("id", lji_id);
		param.put("tgl", tgl);
		param.put("minus", minus);
		return (HashMap) querySingle("select_tminus",param);
	}
	
	public List<HashMap> selectSpajFromABM(Date startDate, Date endDate, String area, String produk) {
		Map map = new HashMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("area", area);
		map.put("produk", produk);
		return query("selectSpajFromABM", map);
	}
	
	public List<HashMap> selectSpajFromABM2(Date startDate, Date endDate, String area, String produk) {
		Map map = new HashMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("area", area);
		map.put("produk", produk);
		return query("selectSpajFromABM2", map);
	}
	
	public List<HashMap> selectSpajFromLCB(Date startDate, Date endDate, String lcb, String produk) {
		Map map = new HashMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("lcb", lcb);
		map.put("produk", produk);
		return query("selectSpajFromLCB", map);
	}
	
	public List<HashMap> selectABM(){
		return query("selectABM", null);
	}
	
	public List<HashMap> selectABM2(){
		return query("selectABM2", null);
	}
	
	public List<HashMap> selectCabangMayapada(){
		return query("selectCabangMayapada", null);
	}
	
	public List<HashMap> selectCabangUOB(){
		return query("selectCabangUOB", null);
	}	
	
	public List selectHistoryBlanko(int lus_id) {
		return query("selectHistoryBlanko", lus_id);
	}
	
	public List selectHistoryBlankoASM(String mssd_lus_id) {
		return query("selectHistoryBlankoASM", mssd_lus_id);
	}
	
	public String sequenceSpajElektronik(User currentUser, String nama_spaj) {
		String seq = this.sequence.sequenceSpajElektronik(77, "01");
		Map map = new HashMap();
		map.put("no_blanko", seq);
		map.put("lus_id", currentUser.getLus_id());
		map.put("nama_spaj", nama_spaj);
		insert("insert.lst_ulangan_blanko", map);
		return seq;
	}
	
//	public String sequenceSpajElektronikASM(User currentUser, String nama_spaj) {
//		String seq = this.sequence.sequenceSpajElektronik(77, "01");
//		Map map = new HashMap();
//		map.put("no_blanko", seq);
//		map.put("lus_id", currentUser.getLus_id());
//		map.put("nama_spaj", nama_spaj);
//		insert("bas.insertSpajDet", map);
//		return seq;
//	}
	
	public Date selectTanggalJatuhTempoPowersave(String spaj) {
		Date result = (Date) querySingle("selectTanggalJatuhTempoPowersave", spaj);
		if(result == null) result = new Date();
		return result;
	}
	
	public String blanko_sama(String no_blanko)
	{
		return (String) querySingle("blanko_sama",no_blanko);
	}
	
	public String no_virtual_sama(String no_va)
	{
		return (String) querySingle("no_virtual_sama",no_va);
	}
	
	public int update_no_blanko(String no_blanko, String spaj, Integer lsjs_id)
	{
		Map param = new HashMap();
		param.put("no_blanko", no_blanko);
		param.put("spaj", spaj);
		param.put("lsjs_id", lsjs_id);
		return update("update_no_blanko",param);
	}
	public int update_no_va(String no_va, String no_temp)
	{
		Map param = new HashMap();
		param.put("spaj_temp", no_temp);
		param.put("no_va", no_va);
		return update("update_no_va",param);
	}
	
	public int updateFlagEkspedisiSpaj(String[] daftarSpaj) {
		Map param = new HashMap();
		param.put("daftarSpaj", daftarSpaj);
		return update("updateFlagEkspedisiSpaj", param);
	}
	
	public void insert_mst_peserta(Simas data)
	{
		insert("insert_mst_peserta",data);
	}
	
	public void insertSpajASM(String data)
	{
		insert("insertSpajASM",data);
	}
	public Date selectPrintDatePolis(String spaj) {
		return (Date) querySingle("selectPrintDatePolis", spaj);
	}
	
	public int selectIsKaryawanEkalife(String spaj) {
		return (Integer) querySingle("selectIsKaryawanEkalife", spaj);
	}
	
	public int selectcountblanko(String blanko) {
		return (Integer) querySingle("selectcountblanko", blanko);
	}
	
	public int update_counter(String IDCounter , Integer number , String lca_id)
	{
		Map param = new HashMap();
		param.put("IDCounter",IDCounter);
		param.put("number", number);
		param.put("lca_id",lca_id);
		return update("update_counter",param);
	}
	
	public void update_mst_peserta_lspc_no(String reg_spaj, Integer lspc_no){
		Map param = new HashMap();
		param.put("reg_spaj", reg_spaj);
		param.put("lspc_no", lspc_no);
		update("update_mst_peserta_lspc_no",param);
	}
	
	public void update_mst_peserta1(String reg_spaj)
	{
		update("update_mst_peserta1",reg_spaj);
	}
	
	public long select_counter(Integer number , String lca_id)
	{
		Map param = new HashMap();
		param.put("number",number);
		param.put("lca_id",lca_id);
		return (Long) querySingle("select_counter",param);
	}
	
	public long select_counter_eb(Integer number , String lca_id)
	{
		Map param = new HashMap();
		param.put("number",number);
		param.put("lca_id",lca_id);
		return (Long) querySingle("select_counter_eb",param);
	}
	
	public int selectcountblanko_spaj(String blanko,String spaj) {
		Map param = new HashMap();
		param.put("blanko",blanko);
		param.put("spaj",spaj);
		return (Integer) querySingle("selectcountblanko_spaj", param);
	}
	
	public void insertMst_deposit_premium(DepositPremium ttp) {
		insert("insert.mst_deposit_premium", ttp);
	}
	
	public void insertMst_benefeciary(Benefeciary benefeciary) {
		insert("insert.mst_benef", benefeciary);
	}
	
	public void insertMst_rencana_penarikan(RencanaPenarikan rencanaPenarikan) {
		insert("insert.mst_rencana_penarikan", rencanaPenarikan);
	}

	public void insertMST_KYC(SumberKyc sumberKyc) { 
		insert("insert.mst_kayece", sumberKyc);
	}
	
	public void insert_mst_peserta_plus_mix(PesertaPlus_mix pesertaPlus_mix){
		insert("insert.mst_peserta_plus_mix", pesertaPlus_mix);
	}
	
	public void insert_mst_peserta_plus2(Simas simas){
		insert("insert.mst_peserta_plus2", simas);
	}
	
	public void deleteMST_KYC(String spaj){
		delete("delete.mst_kayece",spaj);
	}

	public void insertMST_KYC2(SumberKyc sumberKyc) {
		insert("insert.mst_kayece2", sumberKyc);
	}
	
	public void deleteMST_KYC2(String spaj) {
		delete("delete.mst_kayece2", spaj);
	}
	
	public void deletePesertax(String spaj) {
		delete("delete.mst_pesertax", spaj);
	}
	
	public void insertMst_product_insured(Product product) {
		insert("insert.mst_product_insured2", product);
	}
	
	public void insertMst_sts_client(String mcl_id) {
		Map param=new HashMap();
		param.put("strInsClientID", mcl_id);	
		insert("insert.mst_sts_client", param);		
	}
	
	public void insertMst_position_no_spaj_pb(String spaj, String lus_id, int lspd,
			Integer lssp, String desc,Integer count) throws DataAccessException {
		Map params = new HashMap();
		params.put("strTmpSPAJ", spaj);
		params.put("lus_id", lus_id);
		params.put("lspd", new Integer(lspd));
		params.put("lssp", lssp);
		params.put("desc", desc);
		params.put("tgl", "SYSDATE+0.0000"+count);

		insert("insert.mst_position_no_spaj_pb", params);
	}
	
	public void insertMst_position_spaj_ttp( String msps_desc, String reg_spaj) throws DataAccessException{
		Map p = new HashMap();
		p.put("msps_desc", msps_desc);
		p.put("reg_spaj", reg_spaj);
		insert("mst_position_no_spaj_ttp", p);
	}

	
	public void insertMst_address_billing(AddressBilling addressBilling) {
		insert("insert.mst_address_billing", addressBilling);
	}
	
	public List selectMst_agent(String msag_id) {
		return query("select.mst_agent2", msag_id);
	}
	
	public Map selectRegionalAgen(String kodeAgen) {
		Map params = new HashMap();
		params.put("kodeagen", kodeAgen);
		return (HashMap) querySingle("select.regionalagen", params);
	}
	
	public void insertMst_insured(Insured insured) {
		insert("insert.mst_insured2", insured);
	}
	
	public void insertMst_policy(Policy policy) {
		insert("insert.mst_policy2", policy);
	}
	
	public void insertMst_client_new(Pemegang pemegang) {
		insert("insert.mst_client_new", pemegang);
	}
	
	public int updateMst_client_new(Pemegang pemegang) {
		return update("update.mst_client_new", pemegang);
	}
	
	public void insert_mst_non_medical(Kesehatan kesehatan)
	{
		insert("insert_mst_non_medical",kesehatan);
	}
	
	public int update_mst_non_medical(Kesehatan kesehatan)
	{
		return update("update_mst_non_medical",kesehatan);
	}
	
	public Integer selectFlagCC(String spaj) {
		return (Integer) querySingle("selectFlagCC", spaj);
	}
	
	public String select_agent_temp(String spaj) {
		return (String) querySingle("select_agent_temp", spaj);
	}
	
	public void savelssaid(String spaj,  Integer status) {
		Map param = new HashMap();
		param.put("kode", status);
		param.put("reg_spaj", spaj);
		update("update_status_polis",param);
	}


	public void savePending(String spaj, String keterangan, Integer status, String lus_id, Integer sub_id) {
		Map param = new HashMap();
		param.put("kode", status);
		param.put("reg_spaj", spaj);
		update("update_status_polis",param);
		
		//uwDao.insertMstPositionSpaj(lus_id, keterangan, spaj);
		uwDao.insertMstPositionSpajWithSubId(lus_id, keterangan, spaj, sub_id);
	}

	//titipan premi
	public List selectnilaikurs(String kurs,String tgl_kurs) throws DataAccessException {
		Map m = new HashMap();
		m.put("kurs", kurs);
		m.put("tgl_kurs", tgl_kurs);
		return query("select.kursjualbeli",m);
	}	
	
	public List selectpremike(String spaj) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		return query("select_jumlah_premike",params);
	}	
	
	public List selectUserTtpPremi(String userOut) throws DataAccessException {
		return query("selectUserTtpPremi",userOut);
	}	

	public List selectTransDobleTU(String spaj,Date msl_bdate, Double msl_premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("msl_bdate", msl_bdate);
		params.put("msl_premi", msl_premi);
		return query("selectTransDobleTU",params);
	}
	
	public Integer select_flag_cc(String spaj)
	{
		return (Integer) querySingle("select.flag_polis",spaj);
	}
	
	public DetailPembayaran premike(String spaj,Integer ke) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("ke", ke);
		return (DetailPembayaran) querySingle("select.premike",params);
	}	
	
	public Map select_rek_ekal(Integer lsrek_id) throws DataAccessException {
		Map params = new HashMap();
		params.put("lsrek_id", lsrek_id);
		return (HashMap) querySingle("select_rek_ekal",params);
	}		
	
	public List tahapanttppremi(String nopolis) throws DataAccessException {
		Map params = new HashMap();
		params.put("nopolis", nopolis);
		return query("select.mst_bayar_tahapan",params);	
	}
		
	public void insertmst_deposit(DetailPembayaran dp) throws DataAccessException {
		insert("insert.mst_deposit", dp);
	}	
	
	public void updatemst_deposit(DetailPembayaran dp) throws DataAccessException {
		update("updatemst_deposit", dp);
	}
	
	public int countmstcontrolpayment(String no_kttp) throws DataAccessException {
		Map params = new HashMap();
		params.put("no_kttp", no_kttp);
		Integer i= (Integer)querySingle("select.countmstcontrolpayment",params);
		return i.intValue();
	}	
	
	public List ceknokttp(String no_kttp) throws DataAccessException {
		Map params = new HashMap();
		params.put("no_kttp", no_kttp);
		return query("select.ceknokttp",params);	
	}	
	
	public List cekflagcc(String spaj) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		return query("select.cekflagcc", params);
	}
	
	
	public void delete_mst_deposit_premium(String spaj,String ke){ 
		Map param = new HashMap();
		param.put("spaj",spaj);
		param.put("ke",ke);
		delete("delete_mst_deposit_premium",param);
	}	
	
	public String select_kurs(String spaj)
	{
		return (String) querySingle("select_kurs",spaj);
	}
	
//	edit bac
	
	public  Integer selectPositionSpaj(String spaj)
	{
		return (Integer) querySingle("selectPositionSpaj",spaj);
	}
	
	public Map selectPositiondok(String spaj)
	{
		return (HashMap) querySingle("selectPositiondok",spaj);
	}

	public List select_tipeproduk()
	{
		return query("select_tipeproduk",null);
	}
	
	public String selectUserName(int lus_id){
		return (String) querySingle("selectUserName", lus_id);
	}

	public String selectPwdSpv(int lus_id){
		return (String) querySingle("selectPwdSpv", lus_id);
	}
	
	public String selectPasswordFromUsername(String userName){
		return (String) querySingle("selectPasswordFromUsername", userName);
	}
	
	public List select_tipeproduk_banksinarmas()
	{
		return query("select_tipeproduk_banksinarmas",null);
	}
	
	public List select_tipeproduk_sekuritas()
	{
		return query("select_tipeproduk_banksinarmas",null);
		//return query("select_tipeproduk_sekuritas",null);
	}
	
	public List<Map<String, Object>> select_tipeprodukonline()
	{
		return query("select_tipeprodukonline",null);
	}
	
	
	
	public List select_tipeproduk_platinumbii()
	{
		return query("select_tipeproduk_platinumbii",null);
	}
	
	public Pemegang selectpp(String spaj) throws DataAccessException {
		return (Pemegang)querySingle("selectpp", spaj);
	}
	
	public List select_sumberKyc(String spaj)
	{
		return query("select_sumberKyc", spaj);
	}
	
	public ContactPerson selectpic(String mcl_id) throws DataAccessException {
		return (ContactPerson)querySingle("selectpic", mcl_id);
	}
	
	public Tertanggung selectttg(String spaj) throws DataAccessException {
		return (Tertanggung) querySingle("selectttg", spaj);
	}

	
	public AddressBilling  selectAddressBilling(String spaj) throws DataAccessException {
		return (AddressBilling ) querySingle("selectAddressBilling", spaj);
	}
	
	public Datausulan selectDataUsulanutama(String spaj) throws DataAccessException {
		return (Datausulan) querySingle("selectDataUsulanutama", spaj);
	}
	
	public void updateTglSmileBaby(String spaj, String hpl){
		Map map = new HashMap();
		map.put("spaj",spaj);
		map.put("hpl",hpl);
		update("updateTglSmileBaby",map);
	}
	
	public String selectTanggalHpl(String spaj){
		return (String) querySingle("selectTanggalHpl", spaj);
	}
	
	public List selectBidangUsaha(int flag) throws DataAccessException {
		Map params = new HashMap();
		params.put("flag", flag);
		return query("selectBidangUsaha", params);
	}
	
	public List selectDataUsulan_rider(String spaj)
	{
		return query("selectDataUsulan_rider", spaj);
	}
	
	public InvestasiUtama selectinvestasiutama(String spaj) throws DataAccessException {
		return (InvestasiUtama) querySingle("selectinvestasiutama", spaj);
	}	
	
	public InvestasiUtama selectinvestasiutamamall(String spaj) throws DataAccessException {
		return (InvestasiUtama) querySingle("selectinvestasiutamamall", spaj);
	}
	
	public Integer selectinvestasiutamakosong(Integer kode_flag) throws DataAccessException {
		return (Integer) querySingle("selectinvestasiutamakosong", kode_flag);
	}		
	
	public Map select_rider_save(String spaj) throws DataAccessException{
		return (HashMap) querySingle("select_rider_save", spaj);
	}
	
	public List selectdetilinvestasikosong(Integer kode_flag)
	{
		return query("selectdetilinvestasikosong", kode_flag);
	}
	
	public List selectdetilinvestasimall(Integer kode_flag)
	{
		return query("selectdetilinvestasimall", kode_flag);
	}
	
	public List selectdetilinvestasi(String spaj)
	{
		return query("selectdetilinvestasi", spaj);
	}
	
	public List selectdetilinvestasi2(String spaj)
	{
		return query("selectdetilinvestasi2", spaj);
	}
	
	public List selectdetilinvestasimallspaj(String spaj)
	{
		return query("selectdetilinvestasimallspaj", spaj);
	}
	
	public List selectdetilinvestasisyariah(String spaj)
	{
		return query("selectdetilinvestasisyariah", spaj);
	}
	
	public Date select_max_deposit_date()
	{
		return (Date) querySingle("select_max_deposit_date",null);
	}

	public List selectdetilinvbiaya(String spaj)
	{
		return query("selectdetilinvbiaya", spaj);
	}	
		
	public Account_recur select_account_recur(String spaj)
	{
		return (Account_recur) querySingle("select_account_recur", spaj);
	}	
	
	public Rekening_client select_rek_client(String spaj)
	{
		return (Rekening_client) querySingle("select_rek_client", spaj);
	}	
	
	public Rekening_client select_rek_client_temp(String spaj)
	{
		return (Rekening_client) querySingle("select_rek_client_temp", spaj);
	}
	
	public Powersave select_powersaver(String spaj){
		return (Powersave) querySingle("select_powersaver", spaj);
	}
	
	public Powersave select_powersaver_baru(String spaj){
		return (Powersave) querySingle("select_powersaver_baru", spaj);
	}
	
	public Powersave select_stablesaver(String spaj){
		return (Powersave) querySingle("select_stablesaver", spaj);
	}	
	
	public Powersave select_slink (String spaj)
	{
		return (Powersave) querySingle("select_slink",spaj);
	}
	
	public List selectDaftar_kyc(String spaj) {
		return query("selectDaftar_kyc", spaj);
	}
	public List selectDaftar_kyc2(String spaj) {
		return query("selectDaftar_kyc2", spaj);
	}
	public List selectDaftar_kyc_pprm(String spaj) {
		return query("selectDaftar_kyc_pprm", spaj);
	}
	
	public Tamu selectMstTamu(String kd_tamu)
	{
		return (Tamu) querySingle("selectMstTamu", kd_tamu);
	}
	
	public Datausulan selectMstProposal(String kd_tamu)
	{
		return (Datausulan) querySingle("selectMstProposal", kd_tamu);
	}
	
	public List<Datausulan> selectMstProposalRider(String kd_tamu)
	{
		return (List<Datausulan>) query("selectMstProposalRider", kd_tamu);
	}
	
	
	public Powersave select_slink_topup (String spaj)
	{
		return (Powersave) querySingle("select_slink_topup",spaj);
	}
	
	public List select_benef(String spaj)
	{
		return query("select_benef", spaj);
	}	
	
	public List select_benef_temp(String spaj)
	{
		return query("select_benef_temp", spaj);
	}
	
	public List select_dth(String spaj)
	{
		return query("select_dth", spaj);
	}
	
	public List select_produkutama() throws DataAccessException {
		return query("select_produkutama",null);
	}
	
	public List select_produkutamamall() throws DataAccessException {
		return query("select_produkutamamall",null);
	}
	
	public List select_tipeprodukmall() throws DataAccessException {
		return query("select_tipeprodukmall",null);
	}
	
	public List select_produkutama_banksinarmas() throws DataAccessException {
		return query("select_produkutama_banksinarmas",null);
	}
	
	public List select_produkutama_banksinarmas_simpol() throws DataAccessException {
		return query("select_produkutama_banksinarmas_simpol",null);
	}
	
	public List select_produkutama_sekuritas() throws DataAccessException {
		return query("select_produkutama_sekuritas",null);
	}
	
	public List select_produkutama_platinumbii() throws DataAccessException {
		return query("select_produkutama_platinumbii",null);
	}
		
	public List select_listregion()
	{
		return query("select_listregion",null);
	}
	
	public List select_detilprodukutama(Integer kode)
	{
		return query("select_detilprodukutama",kode);
	}
	
	public List select_detilprodukutamamall(Integer kode)
	{
		return query("select_detilprodukutamamall",kode);
	}
	
	public List select_detilprodukutama_viewer(Integer kode)
	{
		return query("select_detilprodukutama_viewer",kode);
	}
	
	public List select_detilprodukutama_banksinarmas(Integer kode)
	{
		return query("select_detilprodukutama_banksinarmas",kode);
		/*String kode2 =props.getProperty("product.khusus.bsim");
		logger.info(kode2);
		
			if(props.getProperty("product.khusus.bsim").indexOf(kode.toString())>=0){
				return this.bacDao.select_detilprodukutama_banksinarmas(kode);
			}else{
				return null;
			}
			//return query("select_detilprodukutama_banksinarmas",kode);
*/	}
	
	public List select_kabupaten2(Integer lspr_id) //chandra a - 20180312
	{
		return query("select_kabupaten2",lspr_id);
	}
	
	public List select_kecamatan(Integer lspr_id, Integer lska_id) //chandra a - 20180312
	{
		Map map = new HashMap();
		map.put("lspr_id", lspr_id);
		map.put("lska_id", lska_id);
		return query("select_kecamatan",map);
	}

	public List select_kelurahan(Integer lspr_id, Integer lska_id, Integer lskc_id) //chandra a - 20180312
	{
		Map map = new HashMap();
		map.put("lspr_id", lspr_id);
		map.put("lska_id", lska_id);
		map.put("lskc_id", lskc_id);
		return query("select_kelurahan",map);
	}
	
	public List select_detilprodukutamaadminmall(Integer kode)
	{
		return query("select_detilprodukutamaadminmall",kode);
	}
	
	public List select_detilprodukutama_sekuritas(Integer kode)
	{
		return query("select_detilprodukutama_sekuritas",kode);
	}
	
	public List select_detilprodukutama_platinumbii(Integer kode)
	{
		return query("select_detilprodukutama_platinumbii",kode);
	}
	
	public List select_detilproduk_rider(Integer kode)
	{
		return  query("select_detilproduk_rider",kode);
	}
	
	public List select_spesifik_produk_rider(Integer kode, Integer lsdbs_from, Integer lsdbs_to)
	{
		Map map = new HashMap();
		map.put("kode", kode);
		map.put("lsdbs_from", lsdbs_from);
		map.put("lsdbs_to", lsdbs_to);
		return  query("select_spesifik_produk_rider",map);
	}
	
	public List select_spesifik_produk_rider_183 (Integer kode, String plan)
	{
		Map map = new HashMap();
		map.put("kode", kode);
		map.put("plan", plan);
		return  query("select_spesifik_produk_rider_183",map);
	}
	
	public List select_spesifik_produk_rider_bsm(Integer kode, Integer lsdbs_from, Integer lsdbs_to)
	{
		Map map = new HashMap();
		map.put("kode", kode);
		map.put("lsdbs_from", lsdbs_from);
		map.put("lsdbs_to", lsdbs_to);
		return  query("select_spesifik_produk_rider_bsm",map);
	}
	
	public List selectagenao_list()
	{
		return query("selectagenao_list",null);
	}
	
	public String select_detilkurs(String kurs)
	{
		return (String) querySingle("select_detilkurs",kurs);
	}	
	
	public String select_detilcrbayar(Integer crbyr)
	{
		return (String) querySingle("select_detilcrbayar",crbyr);
	}
	
	public Employee select_detilemployee(String spaj)
	{
		return (Employee) querySingle("select_detilemployee", spaj);
	}	
	
	public Agen select_detilagen(String spaj)
	{
		return (Agen) querySingle("select_detilagen", spaj);
	}	
	
	public Agen select_detilagenWIthLv(String spaj)
	{
		return (Agen) querySingle("select_detilagenWithLv", spaj);
	}	
	
	public Agen select_detilagen2(String msag_id)
	{
		return (Agen) querySingle("select_detilagen2", msag_id);
	}	
	
	public String select_region(String lca_id, String lwk_id, String lsrg_id)
	{
		Map map = new HashMap();
		map.put("lca_id", lca_id);
		map.put("lwk_id", lwk_id);
		map.put("lsrg_id", lsrg_id);			
		return (String) querySingle("select_region", map);
	}	
	
	public Agen select_detilagen_bp(String msag_id)
	{
		return (Agen) querySingle("select_detilagen_bp", msag_id);
	}	
	
	public Agen select_detilagen_bpd(String msag_id)
	{
		return (Agen) querySingle("select_detilagen_bpd", msag_id);
	}	
	
	public List select_detilproduk()
	{
		return query("select_detilproduk",null);
	}		

	public List<String> select_namaproduk(String kode)
	{
		return getSqlMapClientTemplate().queryForList("elions.bac.select_namaproduk",kode);
	}
	
	public Date select_bungasimponi(String kurs, Date tgl_begin_date_polis)  {
		Map param = new HashMap();
		param.put("kurs", kurs);
		param.put("tgl_begin_date_polis",tgl_begin_date_polis );
		return  (Date) querySingle("select.bungasimponi",param);
	}	

	public Double select_bunga_simponi(String kurs,Date tgl_max)  {
		Map param = new HashMap();
		param.put("kurs", kurs);
		param.put("tgl_max", tgl_max);
		return  (Double) querySingle("select.bunga_simponi",param);
	}		
	
	public Map selectjenisbiaya(String kode_rider,String number_rider)  {
		Map param = new HashMap();
		param.put("kode_rider", kode_rider);
		param.put("number_rider", number_rider);
		return (HashMap) querySingle("selectjenisbiaya",param);
	}		
	
	public Integer select_kabupaten(String nama_wilayah)
	{
		return (Integer) querySingle("select_kabupaten",nama_wilayah);
	}
	
	public Map select_groupjob(String param)  {
		return (HashMap) querySingle("select_groupjob",param);
	}
	
	public Map select_jabatan(String param)  {
		return (HashMap) querySingle("select_jabatan",param);
	}	

	public Map selectInformasiPowersaveUntukFeeBased(String spaj) {
		return (HashMap) querySingle("selectInformasiPowersaveUntukFeeBased", spaj);
	}
	
	//flag_powersave juga harus ditambah disini, karena di hardcode
	public Map selectbungaprosave(String kurs,String jenis,Double up, Date tgl_beg_date, Integer flag, int flag_breakable)  {
		Map param = new HashMap();
		param.put("kurs", kurs);
		param.put("jenis", jenis);
		param.put("up", up);
		param.put("tgl_beg_date",tgl_beg_date);
		param.put("flag",flag);
		
		//Yusuf - 22 Sep 08
		//bila breakable, rate yg diambil adalah rate yang bukan bulanan, dan yang terendah (MGI 3 bln)
		if(flag_breakable == 1) {
			if(flag == 3) param.put("flag", 0); //powersave normal
			else if(flag == 4) param.put("flag", 1); //platinum save
			else if(flag == 5) param.put("flag", 2); //simas prima
			else if(flag == 7) param.put("flag", 6); //specta save
			else if(flag == 9) param.put("flag", 8); //smart invest
			else if(flag == 20) param.put("flag", 19); //stable save
			param.put("jenis", "10"); //mgi 3 bln
		}
		
		return (HashMap) querySingle("selectbungaprosave",param);
	}
	
	public Map select_karyawan_ekalife(String spaj) {
		return (Map) querySingle("select.karyawan_ekalife", spaj);
	}
	
	public Map select_nik_karyawan(String nik)  {
		Map param = new HashMap();
		param.put("nik", nik);
		return (HashMap) querySingle("select.nik_karyawan",param);
	}

	public Map selectagenpenutup(String kodeagen)  {
		Map param = new HashMap();
		param.put("kodeagen", kodeagen);
		return (HashMap) querySingle("selectagenpenutup",param);
	}
	
	public Map selectMstPolicyCs(String nama_pp, String birth_date) throws DataAccessException{
		Map map = new HashMap();
		map.put("nama_pp", nama_pp);
		map.put("birth_date", birth_date);
		return (HashMap) querySingle("selectMstPolicyCs", map);
	}
	
	public String selectMsagLeader(String msag_id) {
		return (String) querySingle("selectMsagLeader", msag_id);
	}
	
	public List<Map> selectTingkatanAgent(String msag_id) {
		return query("selectTingkatanAgent", msag_id);
	}
	
	public Map selectagenpenutup_endors(String kodeagen)  {
		Map param = new HashMap();
		param.put("kodeagen", kodeagen);
		return (HashMap) querySingle("selectagenpenutup_endors",param);
	}
	
	public Map selectregional(String kode_regional)  {
		Map param = new HashMap();
		param.put("kode_regional", kode_regional);
		return (HashMap) querySingle("selectregional",param);
	}

	public Map selectagenao(String kode_ao)  {
		return (HashMap) querySingle("selectagenao",kode_ao);
	}
	
	public Map selectnamapp(String nopol)  {
		return (HashMap) querySingle("selectnamapp",nopol);
	}
	
	public String selectagenleader(String kode_leader)  {
		return (String) querySingle("selectagenleader",kode_leader);
	}
	
	public List select_bank1(String query) throws DataAccessException {
		return query("select_bank1", query);
	}
	
	public Integer select_panj_rek1(String query) throws DataAccessException {
		return (Integer) querySingle("select_panj_rek1", query);
	}
	
	public Integer select_panj_rek2(String query) throws DataAccessException {
		return (Integer) querySingle("select_panj_rek2", query);
	}

	public List selectLstStatusAcceptSub(String query) throws DataAccessException {
		return query("selectLstStatusAcceptSub", query);
	}	
	
	public List select_bank2(String query) throws DataAccessException {
		return query("select_bank2", query);
	}	 
	
	public List select_bank3(String query) throws DataAccessException {
		return query("select_bank3", query);
	}	
	
	public List select_bankcredit(String query) throws DataAccessException {
		return query("select_bankcredit", query);
	}
	
	public List cekmuamalat() throws DataAccessException {
		return query("cekmuamalat", null);
	}
	
	public List select_sumberBisnis(String query) throws DataAccessException {
		return query("select_sumberBisnis", query);
	}	 
	
	public List select_wilayah(String query) throws DataAccessException {
		return query("select_wilayah", query);
	}
	
	public List selectIcd(String query) throws DataAccessException {
		return query("selectIcd", query);
	}	
	
	public Map select_wilayah1(String query) {
		return (HashMap) querySingle("select_wilayah1", query);
	}	
	
	public List select_company(String query) throws DataAccessException {
		return query("select_company", query);
	}
	
	public List select_company_ekal(String query) throws DataAccessException {
		return query("select_company_ekal", query);
	}
	
	public List select_namacompany_list_endow(List query) {
		return query("select_namacompany_list_endow", query);
	}	
	
	public Map select_company_endow(String query)
	{
		return (HashMap)querySingle("select_company_endow",query);
	}
	
	public Map select_namacompany(String query)  {
		return (HashMap) querySingle("select_namacompany", query);
	}	
	
	public Map select_bankrekclient(String query)
	{
		return (HashMap) querySingle("select_rekclient", query);
	}
	
	public Map select_namaBisnis(String query)
	{
		return (HashMap) querySingle("select_namaBisnis", query);
	}
	
	public Map select_bankautodebet(String query)
	{
		return (HashMap) querySingle("select_bankautodebet", query);
	}
	
	//reff BII
	public ReffBii selectmst_reff_bii(String spaj)
	{
		return (ReffBii) querySingle("selectmst_reff_bii",spaj);
	}

	public ReffBii selectmst_reff_cic(String spaj)
	{
		return (ReffBii) querySingle("selectmst_reff_cic",spaj);
	}
	
	public void deletemstreff_bii(String spaj) throws DataAccessException {
		delete("deletemstreff_bii",spaj);
	}
	
	public void deletemstreff_cic(String spaj) throws DataAccessException {
		delete("deletemstreff_cic",spaj);
	}
	
	public Map select_cabang_cic(String query)  {
		return (HashMap) querySingle("select_cabang_cic",query);
	}
	
	public Map select_cabang_bii1(String query)  {
		return (HashMap) querySingle("select_cabang_bii",query);
	}

	public List list_cabang_bii()  {
		return query("list_cabang_bii",null);
	}
	/**Fungsi	Untuk menampilkan data region sesuai dengan spaj
	 * @param 	String query (No Spaj)
	 * @return 	Map
	 * @author 	Ferry Harlim
	 */
	public Map selectregion(String query)
	{
		return (HashMap) querySingle("selectregion",query);
	}
	
	public void insertmst_reff_bii(String spaj, String level, String lcb_no , String lrb_id, String reff_id, String lcb_from_lrb) throws DataAccessException {
		Map param = new HashMap();
		param.put("spaj",spaj);
		param.put("level", level);
		param.put("lcb_no", lcb_no);
		param.put("lrb_id", lrb_id);
		param.put("reff_id", reff_id);
		param.put("lcb_from_lrb", lcb_from_lrb);
		insert("insertmst_reff_bii", param);
	}
	
	/*public void insertmst_reff_bii2(String spaj, String level, String lcb_no , String lrb_id, String reff_id, String lcb_from_lrb,String lcb_penutup,String lus_id,String reff_id_nsles,String lcb_nsles) throws DataAccessException {
		Map param = new HashMap();
		param.put("spaj",spaj);
		param.put("level", level);
		param.put("lcb_no", lcb_no);
		param.put("lrb_id", lrb_id);
		param.put("reff_id", reff_id);
		param.put("lcb_from_lrb", lcb_from_lrb);
		param.put("lcb_penutup", lcb_penutup);
		param.put("lcb_nsles", lcb_nsles);
		param.put("reff_id_nsles", reff_id_nsles);
		param.put("lus_id", lus_id);
		insert("insertmst_reff_bii2", param);
	}
	*/
	public void insertmst_reff_bii2(String spaj, String level, String lcb_no , String lrb_id, String reff_id, String lcb_from_lrb,String lcb_penutup,String lus_id,
			String lcb_reff, String jn_nasabah) throws DataAccessException {
		Map param = new HashMap();
		param.put("spaj",spaj);
		param.put("level", level);
		param.put("lcb_no", lcb_no);
		param.put("lrb_id", lrb_id);
		param.put("reff_id", reff_id);
		param.put("lcb_from_lrb", lcb_from_lrb);
		param.put("lcb_penutup", lcb_penutup);
		param.put("lcb_reff", lcb_reff);
		param.put("lus_id", lus_id);
		param.put("jn_nasabah", jn_nasabah);
		insert("insertmst_reff_bii2", param);
	}
	
	public void insertmst_reff_cic(String spaj, String level, String lcc_no , String lrc_id) throws DataAccessException {
		Map param = new HashMap();
		param.put("spaj",spaj);
		param.put("level", level);
		param.put("lcc_no", lcc_no);
		param.put("lrc_id", lrc_id);
		insert("insertmst_reff_cic", param);
	} 
	
	public List select_reff(String query) throws DataAccessException {
		return query("select_reff", query);
	}
	
	public List select_bii1(String query) throws DataAccessException {
		return query("select_bii1", query);
	}	
	
	public List select_cic(String query) throws DataAccessException {
		return query("select_cic", query);
	}		
	
	public List select_reff_cic(String query) throws DataAccessException {
		return query("select_reff_cic", query);
	}	

	public Map select_referrer(String query) {
		return (HashMap) querySingle("select_referrer", query);
	}
	
	public String selectSpajFromPolis(String nopol) {
		return (String) querySingle("selectSpajFromPolis", nopol);
	}
	
	public String selectGetSpaj(String nopol) {
		return (String) querySingle("selectGetSpaj", nopol);
	}
	
	public String selectGetMspoPolicyNo(String nopol) {
		return (String) querySingle("selectGetMspoPolicyNo", nopol);
	}
	
	public Map selectSimasCardExist(String spaj, String produk){
		Map param = new HashMap();
		param.put("spaj", spaj);
		param.put("produk", produk);
		return (HashMap) querySingle("selectSimasCardExist", param);
	}
	
	public Map select_referrer_shinta(String query,Integer kode_flag) {
		Map param = new HashMap();
		param.put("lrb_id", query);
		param.put("kode_flag",kode_flag);
		return (HashMap) querySingle("select_referrer_shinta", param);
	}
	
	public List <Map> selectCabangKK(String lcb_no) throws DataAccessException{
		Map param= new HashMap();
		param.put("lcb_no", lcb_no);
		return query("select_cabang_kk", param);
	}
	
	public List select_reff_shinta(String query,Integer kode_flag, Boolean show_ajspusat) throws DataAccessException {
		Map param = new HashMap();
		param.put("nama",query);
		param.put("kode_flag",kode_flag);
		param.put("show_ajspusat",show_ajspusat);
		return  query("select_reff_shinta",param);
	}	
	
	public List select_reff_shintacekBC(String query,Integer kode_flag, Boolean show_ajspusat) throws DataAccessException {
		Map param = new HashMap();
		param.put("nama",query);
		param.put("kode_flag",kode_flag);
		param.put("show_ajspusat",show_ajspusat);
		return  query("select_reff_shintacekBC",param);
	}
	
	public List select_reff_shintacekBC2(String query,Integer kode_flag, Boolean show_ajspusat) throws DataAccessException {
		Map param = new HashMap();
		param.put("code",query);
		param.put("kode_flag",kode_flag);
		param.put("show_ajspusat",show_ajspusat);
		return  query("select_reff_shintacekBC2",param);
	}
	
	public List select_reff_shinta2(String query,Integer kode_flag, Boolean show_ajspusat) throws DataAccessException {
		Map param = new HashMap();
		param.put("nama",query);
		param.put("kode_flag",kode_flag);
		param.put("show_ajspusat",show_ajspusat);
		return  query("select_reff_shinta2",param);
	}	
	
	public List select_transulink(String query) throws DataAccessException {
		return query("selecttransulink",query);
	}

	
	public Map select_bii(String query)  {
		return (HashMap) querySingle("select_bii", query);
	}
	
	public Map select_cic1(String query)  {
		return (HashMap) querySingle("select_cic1", query);
	}
	
	public Map select_reff_cic1(String query)  {
		return (HashMap) querySingle("select_reff_cic1", query);
	}
	
	public List list_reff(Integer number1,Integer number2, String kunci){
		Map param = new HashMap();
		param.put("number1",number1);
		param.put("number2", number2);
		param.put("kunci", kunci);
		return query("list_reff", param);
	}	
	
	public List list_reff1(Integer number1,Integer number2){
		Map param = new HashMap();
		param.put("number1",number1);
		param.put("number2", number2);
		return query("list_reff1", param);
	}	
	
	public Integer count_reff(String kunci){
		return (Integer) querySingle("count_reff", kunci);
	}		
	
	public Long max_reff_bii(){
		return (Long) querySingle("max_reff_bii", null);
	}
	
	public void insert_lst_reff_bii(String nama_reff,String lcb_no,String no_rek,String atas_nama,String flag_aktif,String npk,String lrb_id,String cab_rek){
		Map param = new HashMap();
		param.put("nama_reff",nama_reff);
		param.put("lcb_no",lcb_no);
		param.put("no_rek",no_rek);
		param.put("atas_nama",atas_nama);
		param.put("flag_aktif",flag_aktif);
		param.put("npk",npk);
		param.put("lrb_id",lrb_id);
		param.put("cab_rek",cab_rek);
		insert("insert_lst_reff_bii",param);
	}
	
	public void update_lst_reff_bii(String nama_reff,String lcb_no,String no_rek,String atas_nama,String flag_aktif,String npk,String lrb_id,String cab_rek)
	{
		Map param = new HashMap();
		param.put("nama_reff",nama_reff);
		param.put("lcb_no",lcb_no);
		param.put("no_rek",no_rek);
		param.put("atas_nama",atas_nama);
		param.put("flag_aktif",flag_aktif);
		param.put("npk",npk);
		param.put("lrb_id",lrb_id);
		param.put("cab_rek",cab_rek);
		update("update_lst_reff_bii",param);
	}

	public void insertMstCompanyId(Personal personal, String lsClientPpOld){
		insert("insertMstCompany", personal);
	}
	
	public void deleteMstCompanyId(Personal personal, String lsClientPpOld){
		delete("deleteMstCompany", lsClientPpOld);
	}
	
	public void insertMstCompanyContactFamily(Map param){
		insert("insert.mst_company_contact_family", param);
	}
	
	public void deleteMstCompanyContactFamily(String lsClientPpOld){
		insert("delete.mst_company_contact_family", lsClientPpOld);
	}
	
	public void insertMstCompanyContactId(ContactPerson contactPerson, String lsClientPpOld){
		insert("insert.mst_clientpic", contactPerson);
	}
	
	public void deleteMstCompanyContactId(ContactPerson contactPerson, String lsClientPpOld){
		delete("delete.mst_clientpic", lsClientPpOld);
	}
	
	public void updateMstCompanyContactAddressId(ContactPerson contactPerson, String lsClientPpOld){
		insert("insert.mst_addresspic", contactPerson);
		delete("delete.mst_addresspic", lsClientPpOld);
	}
	
	public Map selectDataUsulanDetail(String spaj){
		return (HashMap)getSqlMapClientTemplate().queryForObject("elions.bac.selectDataUsulanDetail",spaj);
	}
	
	public List selectAllLstKurs(){
		return getSqlMapClientTemplate().queryForList("elions.bac.selectAllLstKurs",null);
	}
	
	public List selectAllLstPayMode(){
		return getSqlMapClientTemplate().queryForList("elions.bac.selectAllLstPayMode",null);
	}
	/**Fungsi:	Untuk menampilkan jumlah rider excellink new 
	 * @param 	String spaj, int lsbsId1, int lsbsId2
	 * @return	Integer
	 * @author	Ferry Harlim
	 */
	public Integer selectCountRiderExcellinkNew(String spaj,int lsbsId1,int lsbsId2){
		Map param=new HashMap();
		param.put("txtnospaj",spaj);
		param.put("lsbs_id1",new Integer(lsbsId1));
		param.put("lsbs_id2",new Integer(lsbsId2));
		return (Integer)getSqlMapClientTemplate().queryForObject("elions.bac.select.count_rider.mst_product_insured",param);
	
	}
	
	public Double selectNilaiBonusTahapan(String spaj){
		return (Double)getSqlMapClientTemplate().queryForObject("elions.uw.selectNilaiUnderTable",spaj);
	}
	
	public Integer selectMstAccountRecur(String spaj,Integer active){
		Map param2=new HashMap();
		param2.put("txtnospaj",spaj);
		param2.put("mar_active",active);
		return (Integer)getSqlMapClientTemplate().queryForObject("elions.bac.select.mst_account_recur",param2);
	}
	/**Fungsi:	Untuk Menampilkan jumlah Rekening Client atau Nasabah pada tabel EKA.MST_REK_CLIENT
	 * @param 	String spaj
	 * @return	Double
	 * @author 	Ferry Harlim
	 */
	public Double selectCountMstRekClient(String spaj){
		return (Double)getSqlMapClientTemplate().queryForObject("elions.bac.select.mst_rek_client.count",spaj);	  
	}
	/**Fungsi:	Untuk menampilkan data pada kolom EKA.MST_POWERSAVE_PROSES
	 * @param 	String spaj, Integer mpsKode
	 * @param 	String spaj,Integer mpsKode
	 * @return	List
	 * @author	Ferry Harlim
	 */
	public List selectMstPowerSaveProses(String spaj, Integer mpsKode){
		Map param=new HashMap();
		param.put("txtnospaj",spaj);
		param.put("mps_kode",mpsKode);
		return getSqlMapClientTemplate().queryForList("elions.bac.select.mst_powersave_proses",param);
	}
	
	public void insertMstTBank(String ls_no_pre,Integer liPos,Date ldt_prod,Date ar_tglrk,String noVoucher,
			String lsKas,Double jumlah,String lusId,String accNo){
		Map ins_tbank=new HashMap();
		ins_tbank.put("no_pre",ls_no_pre);
		ins_tbank.put("position",liPos);
		ins_tbank.put("tgl_jurnal",ldt_prod);
		ins_tbank.put("tgl_rk",ar_tglrk);
		ins_tbank.put("no_voucher",noVoucher);
		ins_tbank.put("kas",lsKas);
		ins_tbank.put("jumlah",jumlah);
		ins_tbank.put("lus_id",lusId);
		ins_tbank.put("mtb_gl_no",accNo);
		getSqlMapClientTemplate().insert("elions.bac.insert.mst_tbank",ins_tbank);
		
	}
	
	public void insertMstDBank(String ls_no_pre,int li_run,String ket,String lsKas,Double jumlah,Integer kdCashFlow, String spaj){
		Map param=new HashMap();
		param.put("no_pre",ls_no_pre);
		param.put("no_jurnal",new Integer(li_run));
		param.put("keterangan",ket);
		param.put("kode_cash_flow",kdCashFlow);
		param.put("kas",lsKas);
		param.put("jumlah",jumlah);
		param.put("spaj", spaj);
		getSqlMapClientTemplate().insert("elions.bac.insert.mst_dbank",param);
		
	}
	
	public void insertMstBVoucher(String ls_no_pre,int li_run,String ket,Double ar_jumlah,Double ldec_kosong,String projectNo,String budgetNo, String spaj){
		Map param=new HashMap();
		param.put("no_pre",ls_no_pre);
		param.put("no_jurnal",new Integer(li_run));
		param.put("keterangan",ket);
		param.put("debet",ar_jumlah);
		param.put("kredit",ldec_kosong);
		param.put("project_no",projectNo);
		param.put("budget_no",budgetNo);
		param.put("spaj", spaj);
		getSqlMapClientTemplate().insert("elions.bac.insert.mst_bvoucher",param);

	}
	
	public void updateMstDepositPremium(S_Premi stru_premi,Integer msdpJurnal,Long msdpNumber){
		Map param=new HashMap();
		param.put("msdp_jurnal",msdpJurnal);
		param.put("msdp_no_voucher",stru_premi.getVoucher());
		param.put("msdp_no_pre",stru_premi.getDepo_pre());
		param.put("reg_spaj",stru_premi.getNo_spaj());
		param.put("msdp_number",msdpNumber);
		getSqlMapClientTemplate().update("elions.bac.update.mst_deposit_premium",param);
	}
	
	public Date selectMstProductionMsproProdDate(S_Premi stru_premi){
		Map param=new HashMap();
		param.put("reg_spaj",stru_premi.getNo_spaj());
		param.put("msbi_tahun_ke",stru_premi.getTahunke());
		param.put("msbi_premi_ke",stru_premi.getPremike());
		return (Date)getSqlMapClientTemplate().queryForObject("elions.bac.select.mst_production.mspro_prod_date",param);	
	}
	
	public Date selectMstDefaultMsDefDate(Integer param){
		return (Date)getSqlMapClientTemplate().queryForObject("elions.bac.select.mst_default.msdef_date",param);
	}
	
	public Date selectMstPaymentMspaDateBook(S_Premi stru_premi){
		Map param=new HashMap();
		param.put("reg_spaj",stru_premi.getNo_spaj());
		param.put("msbi_tahun_ke",stru_premi.getTahunke());
		param.put("msbi_premi_ke",stru_premi.getPremike());
		return (Date)getSqlMapClientTemplate().queryForObject("elions.bac.select.mst_payment_mspa_date_book",param);
	
	}
	
	public List selectMstTBankTglJurnal(String noVoucher,String lsThn){
		Map param=new HashMap();
		param.put("no_voucher",noVoucher);
		param.put("thn_jurnal",lsThn);
		return query("select.mst_tbank.tgl_jurnal",param);
		
	}
	
	public List selectDaftarRider(String spaj) {
		return query("selectDataUsulan_rider", spaj);
	}
	
	public void insertMstClientNew(Pemegang pemegang){
		insert("insert.mst_clientpp",pemegang);
	}

	public Map selecteditagenpenutup(String spaj)  {
		return (HashMap) querySingle("selecteditagenpenutup",spaj);
	}
	
	public Integer count_mst_cancel(String spaj)
	{
		return (Integer) querySingle("count_mst_cancel", spaj);
	}
	public List selectRider(String spaj){
		return query("selectRider", spaj);
	}
	public List selectInvest(String spaj){
		return query("selectInvest",spaj);
	}
	
	public String addmonthss(Date tgl1, Integer bulan)
	{
		Map param1 = new HashMap();
		param1.put("v_strBeginDate", tgl1);
		param1.put("ai_month",bulan);
		return ((String)querySingle("select.addmonths", param1));
	}
	
	public Integer selectstsgutri ( String spaj)
	{
		return (Integer) querySingle("selectstsgutri",spaj);
	}
	
	public Simas selectmst_peserta (Simas simas)
	{
		return (Simas) query("selectmst_peserta",simas);
	}
	
	public Integer selectmax_peserta(String reg_spaj)
	{
		return (Integer) querySingle("selectmax_peserta",reg_spaj);
	}
	
	public List select_semua_mst_peserta(String reg_spaj)
	{
		return query("select_semua_mst_peserta", reg_spaj);
	}
	
	public List select_all_mst_peserta(String reg_spaj)
	{
		return query("select_all_mst_peserta", reg_spaj);
	}
	
	public Kesehatan selectkesehatan(String reg_spaj)
	{
		return (Kesehatan) querySingle("selectkesehatan", reg_spaj);
	}
	
	public void update_mst_peserta(Simas simas)
	{
		update("update_mst_peserta",simas);
	}
	
	public void delete_mst_peserta(String reg_spaj)
	{
		delete("delete_mst_peserta",reg_spaj);
	}
	
	public void delete_mst_peserta_all(String reg_spaj)
	{
		delete("delete_mst_peserta_all",reg_spaj);
	}	

	public DetilTopUp select_detil_topup(String reg_spaj)
	{
		return (DetilTopUp) querySingle("select_detil_topup",reg_spaj);
	}
	
	public Map selectagenrekrut(String tipe, String kode) 
	{
		Map params = new HashMap();
		params.put("tipe", tipe);
		params.put("kode", kode.toUpperCase().trim());
		return (HashMap) querySingle("selectagenrekrut", params);
	}
	
	public String selectkodegutri(String nama, String tgl)
	{
		Map params = new HashMap();
		params.put("nama", nama);
		params.put("tgl", tgl);
		return (String) querySingle("selectkodegutri",params);
	}
	
	public Double sum_premi ( String spaj )
	{
		return (Double) querySingle("sum_premi",spaj);
	}
	
	public Datarider selectrider_perkode( String spaj, String kode)
	{
		Map param = new HashMap();
		param.put("spaj", spaj);
		param.put("kode", kode);
		return (Datarider) querySingle("selectrider_perkode",param);
	}
	
	public Integer count_simultan(String mcl_id)
	{
		return (Integer) querySingle("count_simultan",mcl_id);
	}
	
	public Integer count_client_simultan(String mcl_id,String mcl_first,String mspe_date_birth)
	{
		Map param = new HashMap();
		param.put("mcl_id",mcl_id);
		param.put("mcl_first", mcl_first);
		param.put("mspe_date_birth",mspe_date_birth);
		return (Integer) querySingle("count_client_simultan",param);
	}
	
	public Integer count_no_blanko(String kode,String number, String no_blanko)
	{
		Map param = new HashMap();
		param.put("kode",kode);
		param.put("number", number);
		param.put("no_blanko", no_blanko);
		return (Integer) querySingle("count_no_blanko",param);
	}
	
	public Integer count_no_blanko_perspaj(String kode,String number, String no_blanko,String spaj)
	{
		Map param = new HashMap();
		param.put("kode",kode);
		param.put("number", number);
		param.put("no_blanko", no_blanko);
		param.put("spaj", spaj);
		return (Integer) querySingle("count_no_blanko_perspaj",param);
	}
	/**Fungsi:	Untuk mengetahui no blanko sudah dipakai ato blom
	 * @return	Integer
	 * @author	Ferry Harlim
	 */
	public Integer cekNoBlanko(String no_blanko)
	{
		return (Integer)querySingle("cekNoBlanko", no_blanko);
	}	

	public List<Hcp> select_hcp(String reg_spaj)
	{
		return query("select_hcp",reg_spaj);
	}
	
	public Hcp select_hcp_perkode (String spaj, Integer kode , Integer number1 , Integer number2 )
	{
		Map param = new HashMap();
		param.put("spaj", spaj);
		param.put("kode", kode);
		param.put("number1",number1);
		param.put("number2", number2);
		return (Hcp) querySingle ("select_hcp_perkode",param);
	}
	
	public Integer cek_data_sama(String reg_spaj , String ktp , String lsbs_id , String lsdbs_number , Double up)
	{
		Map param = new HashMap();
		param.put("reg_spaj", reg_spaj);
		param.put("ktp", ktp);
		param.put("lsbs_id", lsbs_id);
		param.put("lsdbs_number", lsdbs_number);
		param.put("up", up);
		return (Integer)querySingle("cek_data_sama",param);
	}
	
	public Integer cek_polis_dobel(String nama , String tgl , String lsbs_id , String lsdbs_number , Double premi, String site)
	{
		Map param = new HashMap();
		param.put("nama", nama);
		param.put("tgl", tgl);
		param.put("lsbs_id", lsbs_id);
		param.put("lsdbs_number", lsdbs_number);
		param.put("premi", premi);
		param.put("site", site);
		return (Integer)querySingle("cek_polis_dobel",param);
	}
	
	public Integer cek_polis_dobel_tsi(String nama , String tgl , String lsbs_id , String lsdbs_number , Double up)
	{
		Map param = new HashMap();
		param.put("nama", nama);
		param.put("tgl", tgl);
		param.put("lsbs_id", lsbs_id);
		param.put("lsdbs_number", lsdbs_number);
		param.put("up", up);
		return (Integer)querySingle("cek_polis_dobel_tsi",param);
	}
	
	public Integer cek_data_baru_sama( String ktp , String lsbs_id , String lsdbs_number , Double up)
	{
		Map param = new HashMap();
		param.put("ktp", ktp);
		param.put("lsbs_id", lsbs_id);
		param.put("lsdbs_number", lsdbs_number);
		param.put("up", up);
		return (Integer)querySingle("cek_data_baru_sama",param);
	}
	
	public List cek_spaj_sama( String ktp , String lsbs_id , String lsdbs_number , Double up)
	{
		Map param = new HashMap();
		param.put("ktp", ktp);
		param.put("lsbs_id", lsbs_id);
		param.put("lsdbs_number", lsdbs_number);
		param.put("up", up);
		return query("cek_spaj_sama",param);
	}
	
	public List<Map<String, Object>> cek_spaj_sama1( String reg_spaj,String ktp , String lsbs_id , String lsdbs_number , Double up)
	{
		Map param = new HashMap();
		param.put("reg_spaj", reg_spaj);
		param.put("ktp", ktp);
		param.put("lsbs_id", lsbs_id);
		param.put("lsdbs_number", lsdbs_number);
		param.put("up", up);
		return query("cek_spaj_sama1",param);
	}	
	
	public Integer selectCountProductHcp(String spaj){
		return (Integer)querySingle("selectCountProductHcp", spaj);
	}
	public String selectNmProduct(String reg_spaj) throws DataAccessException{
		return (String) querySingle("selectNmProduct", reg_spaj);
	}
	
	public CommandPowersaveUbah selectDataNasabah(String spaj) throws DataAccessException{
		return (CommandPowersaveUbah) querySingle("selectDataNasabah", spaj);
	}
	
	public Double selectRatePowerSave (PowersaveProses powersaveProses)throws DataAccessException{
		return (Double) querySingle("selectRatePowerSave", powersaveProses);
	}
	
	public PowersaveProses selectProsesPowersave(String spaj) throws DataAccessException{
		return (PowersaveProses) querySingle("selectProsesPowersave", spaj);
	}
	
	public Integer selectCekPinjaman(String spaj, Integer jns) throws DataAccessException{
		return (Integer) querySingle("selectCekPinjaman"+jns, spaj);
	}
	
	public HashMap selectProdInsured(String spaj) throws DataAccessException{
		return (HashMap) querySingle("selectProdInsured", spaj);
	}
	
	public List<Map> selectRiderSave(String spaj) throws DataAccessException{
		return query("selectRiderSave", spaj);
	}
	
	public List<Map> selectDataNasabahSlink(String reg_spaj) throws DataAccessException{
		return query("selectDataNasabahSlink", reg_spaj);
	}
	
	public Integer selectCountPwrRO(String spaj, Date depdate)throws DataAccessException{
		Map<String, Object>params=new HashMap<String, Object>();
		params.put("spaj", spaj);
		params.put("depdate", depdate);
		return (Integer) querySingle("selectCountPwrRO", params);
	}
	
	public void updateJnsRO_PwrRo(String spaj, Integer ro, Date depdate)throws DataAccessException{
		Map<String, Object>params=new HashMap<String, Object>();
		params.put("spaj", spaj);
		params.put("depdate", depdate);
		params.put("ro", ro);
		update("updateJnsRO_PwrRo", params);
	}
	
	public void updateJnsRO_PwrPro(String spaj, Integer ro, Date depdate)throws DataAccessException{
		Map<String, Object>params=new HashMap<String, Object>();
		params.put("spaj", spaj);
		params.put("depdate", depdate);
		params.put("ro", ro);
		update("updateJnsRO_PwrPro", params);
	}
	
	public void insertPwrUbah(PowersaveUbah powersaveUbah)throws DataAccessException{	
		insert("insertPwrUbah", powersaveUbah);
	}
	
	public List<PowersaveUbah> selectUbahView(String spaj)throws DataAccessException{
		return query("selectUbahView", spaj);
	}
	
	public Integer selectUbahCount(PowersaveUbah powersaveUbah)throws DataAccessException{
		return (Integer) querySingle("selectUbahCount", powersaveUbah);
	}
	
	public HashMap selectCekBlacklist(String tgl_lahir, String nama)throws DataAccessException{
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("mcl_first", nama);
		params.put("mspe_date_birth", tgl_lahir.substring(8,10)+"/"+tgl_lahir.substring(5,7)+"/"+tgl_lahir.substring(0,4));
		return (HashMap) querySingle("selectCekBlacklist", params);
	}

	public void delete_mst_powersave_ubah(PowersaveUbah powersaveUbah)throws DataAccessException{
		delete("delete_mst_powersave_ubah", powersaveUbah);
	}
	
	public void insertAutodebetNasabah(Account_recur recur) {
		insert("insert.mst_account_recur",recur);
	}
	

	public Integer selectCountFreeFluBabi(String spaj)throws DataAccessException{
		return (Integer) querySingle("selectCountFreeFluBabi", spaj);
	}
	
	public Double selectSumPremiSlink(String spaj)throws DataAccessException{
		return (Double) querySingle("selectSumPremiSlink", spaj);
	}
	
	public Double selectSumPremiSlinkLama(String spaj)throws DataAccessException{
		return (Double) querySingle("selectSumPremiSlinkLama", spaj);
	}
	
	
	
	public void insertMstEndors(Endors endors)throws DataAccessException{
		insert("insertMstEndors", endors);
	}
	
	public void insertMstDetEndors(DetEndors detEndors)throws DataAccessException{
		insert("insertMstDetEndors", detEndors);
	}
	
	public void insertMstProductInsEnd(ProductInsEnd productInsEnd)throws DataAccessException{
		insert("insertMstProductInsEnd", productInsEnd);
	}
	
	public void insertMstProductInsEndAllProdLama(String msen_endors_no,String reg_spaj, Integer lus_id, Integer lscb_id, Double mspie_tsi_old,Double mspie_premium_old, Integer lsbs_id_old, Integer lsdbs_num_old)throws DataAccessException{
		Map<String, Object>params=new HashMap<String, Object>();
		params.put("msen_endors_no", msen_endors_no);
		params.put("reg_spaj", reg_spaj);
		params.put("lus_id", lus_id);
		params.put("lscb_id", lscb_id);
		params.put("mspie_tsi_old", mspie_tsi_old);
		params.put("mspie_premium_old", mspie_premium_old);
		params.put("lsbs_id_old", lsbs_id_old);
		params.put("lsdbs_num_old", lsdbs_num_old);
		insert("insertMstProductInsEndAllProdLama", params);
	}
	
	public void updateMstProductInsured(String spaj, Integer up) throws DataAccessException{
		Map params=new HashMap<String, Object>();
		params.put("spaj", spaj);
		params.put("up", up);
		update("updateMstProductInsured",params);
	}
	
	public void updateMstProductInsEndAllProdLama(String msen_endors_no,String reg_spaj, Integer lus_id, Integer lscb_id, Double mspie_tsi_old,Double mspie_premium_old, Integer lsbs_id_old, Integer lsdbs_num_old)throws DataAccessException{
		Map<String, Object>params=new HashMap<String, Object>();
		params.put("msen_endors_no", msen_endors_no);
		params.put("reg_spaj", reg_spaj);
		params.put("lus_id", lus_id);
		params.put("lscb_id", lscb_id);
		params.put("mspie_tsi_old", mspie_tsi_old);
		params.put("mspie_premium_old", mspie_premium_old);
		params.put("lsbs_id_old", lsbs_id_old);
		params.put("lsdbs_num_old", lsdbs_num_old);
		update("updateMstProductInsEndAllProdLama", params);
	}
	
	public void updateMsen_Endors_NoSlink(String msen_endors_no, String reg_spaj){
		Map params = new HashMap();
		params.put("msen_endors_no", msen_endors_no);
		params.put("reg_spaj", reg_spaj);
		update("updateMsen_Endors_NoSlink", params);
	}
	
	public void updateMsen_Endors_NoSsave(String msen_endors_no, String reg_spaj){
		Map params = new HashMap();
		params.put("msen_endors_no", msen_endors_no);
		params.put("reg_spaj", reg_spaj);
		update("updateMsen_Endors_NoSsave", params);
	}
	

	public HashMap selectPrintBabiRider(String reg_spaj)throws DataAccessException{
		return  (HashMap) querySingle("selectPrintBabiRider", reg_spaj);
	}
	
	public String selectMstEndorsGetEndorsNo(String reg_spaj,Integer lsbs_id, Integer lsdbs_number)throws DataAccessException{
		Map params = new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("lsbs_id", lsbs_id);
		params.put("lsdbs_number", lsdbs_number);
		return (String) querySingle("selectMstEndorsGetEndorsNo", params);
	}
	
	public void updatePrintEndors(String noEndors)throws DataAccessException{
		update("updatePrintEndors", noEndors);
	}
	
	public List<PemegangAndRekeningInfo> selectPemegangAndRekeningInfo(String regSpaj) throws DataAccessException{
		Map params = new HashMap();
		params.put("regSpaj", regSpaj);
		return (List<PemegangAndRekeningInfo>) query("selectPemegangAndRekeningInfo", params);
	}
	
	public KursDanJumlah selectKursAndNominal(String regSpaj) throws DataAccessException{
		return (KursDanJumlah) querySingle("selectKursAndNominal", regSpaj);
	}

	public int selectCekPrintUlang(String spaj, String keterangan) throws DataAccessException{
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("keterangan", keterangan);
		return (Integer) querySingle("selectCekPrintUlang", params);
	}

    public List<Map<String, Object>> selectStrukturAgen(String msag_id ) throws DataAccessException
    {
		return ( List<Map<String, Object>> ) query ( "selectStrukturAgen", msag_id );
	}
    
    public void insertMstBiayaUlink(Product productIns) throws DataAccessException {
    	insert("insertMstBiayaUlink", productIns);
    }
    
	public void insertMstBiayaUlink2(String reg_spaj, Integer mu_ke, Integer ljb_id, Integer mbu_jumlah, Integer mbu_persen, Date ldt_endpay) throws DataAccessException{
		Map map = new HashMap();
		map.put("strTmpSPAJ", reg_spaj);
		map.put("mu_ke", mu_ke);
		map.put("ljb_id", ljb_id);
		map.put("mbu_jumlah", mbu_jumlah);
		map.put("mbu_persen", mbu_persen);
		map.put("ldt_endpay", ldt_endpay);		
		insert("insert.mst_biaya_ulink", map);
	}
    
    public List selectMstPowerSaveProsesBaru(String spaj, Integer mpsKode, Integer mpsNew){
		Map param=new HashMap();
		param.put("txtnospaj",spaj);
		param.put("mps_kode",mpsKode);
		param.put("mps_new",mpsNew);
		return getSqlMapClientTemplate().queryForList("elions.bac.select.mst_powersave_baru",param);
	}
    
	public String selectEmailAdminInputter(String spaj){
		return (String) querySingle("selectEmailAdminInputter", spaj);
	}
	
	public List select_produkutama_admin_mall() throws DataAccessException {
		return query("select_produkutama_admin_mall",null);
	}
	
	public String selectprodukBSM(int lsbs_id, int lsdbs_number){
		Map param = new HashMap();
		param.put("lsbs_id", lsbs_id);
		param.put("lsdbs_number", lsdbs_number);
		return (String) querySingle("selectprodukBSM", param);
	}
	
	public Integer selectJumlahPeserta(String reg_spaj){
		return (Integer) querySingle("selectJumlahPeserta", reg_spaj);
	}
	
	public Double selectSumPremiRiderInMstRiderSave(String reg_spaj){
		return (Double) querySingle("selectSumPremiRiderInMstRiderSave", reg_spaj);
	}
	
	public List selectProdukRider(String lsbs_id) throws DataAccessException {
		return query("selectProdukRider",lsbs_id);
	}
	
	public void updateMstBillingRemain(String spaj, Integer tahun_ke, Integer premi_ke, Double ldecPremi, Integer li_paid)throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("tahun_ke",tahun_ke);
		param.put("premi_ke",premi_ke);
		param.put("ldecPremi", ldecPremi);
		param.put("li_paid", li_paid);
		update("updateMstBillingRemain",param);
	}
	
	public Map selectAgentCodeAO(String kode_ao){
		return (Map) querySingle("selectAgentCodeAO", kode_ao);
	}
	
	public Double selectMrsKurangBayarRiderSave(String reg_spaj , String lsbs_id, String lsdbs_number){
		Map param = new HashMap();
		param.put("reg_spaj", reg_spaj);
		param.put("lsbs_id", lsbs_id);
		param.put("lsdbs_number", lsdbs_number);
		return (Double) querySingle("selectMrsKurangBayarRiderSave", param);
	}
	
	public void updateRiderSavePaidPremi(String reg_spaj , String lsbs_id, String lsdbs_number, Double premi_bayar, Double premi_kurang_bayar){
		Map param=new HashMap();
		param.put("reg_spaj",reg_spaj);
		param.put("lsbs_id",lsbs_id);
		param.put("lsdbs_number",lsdbs_number);
		param.put("premi_kurang_bayar", premi_kurang_bayar);
		param.put("premi_bayar", premi_bayar);
		update("updateRiderSavePaidPremi",param);
	}
	
	public Boolean selectPesertaUtamaOrTambahan(Integer lsbs_id, Integer lsdbs_number){
		Boolean peserta= false;//true Utama, false tambahan
		Map param = new HashMap();
		param.put("lsbs_id", lsbs_id);
		param.put("lsdbs_number", lsdbs_number);
		Integer UtamaOrTambahan = (Integer)querySingle("selectPesertaUtamaOrTambahan", param);
		if(UtamaOrTambahan==1){
			peserta=true;
		}
		return peserta;
	}
	
	public Integer CountTotalAdmissionBridge(Map map) {
		return (Integer) querySingle("CountTotalAdmissionBridge", map);
	}
	
	public List select_produkutamacfl() throws DataAccessException {
		return query("select_produkutamacfl",null);
	}
	
	public List select_detilprodukutamacfl(Integer kode)
	{
		return query("select_detilprodukutamacfl",kode);
	}
	
	public Map selectNamaPlan(Map map){
		return (Map) querySingle("select.nama_plan", map);
	}
	
	public List<Map<String, Object>> select_produkutama_online() throws DataAccessException {
		return query("select_produkutama_online",null);
	}
	
	public List<Map<String, Object>> select_detilprodukutamaonline(Integer kode)
	{
		return query("select_detilprodukutamaonline",kode);
	}
	//lufi (14/09/2012)khusus produk Bank Sinarmas Syariah Berbeda dengan Bank Sinarmas
	public List<Map<String, Object>> select_produkutama_bsim() throws DataAccessException {
		return query("select_produkutama_bsim",null);
	}
	
	public List<Map<String, Object>> select_detilprodukutama_bsim(Integer kode)
	{
		return query("select_detilprodukutama_bsim",kode);
	}
	
	public List<Map<String, Object>> select_tipeprodukbsim()
	{
		return query("select_tipeprodukbsim",null);
	}
	
	public List<Map> select_listPaket(String lsbs_id, String lsdbs_number){
		List<DropDown> listPaket =  new ArrayList<DropDown>();
		Map m = new HashMap();		
		m.put("lsbs_id", lsbs_id);
		m.put("lsdbs_number", lsdbs_number);
		return query("select_listPaket",m);
//		return listPaket;
	}
	
	public Map selectKodeAgenByHolder(String holder)  {
		Map param = new HashMap();
		param.put("mscs_holder", holder.toUpperCase().trim());
		return (HashMap) querySingle("selectKodeAgenByHolder",param);
	}
	
	public Map selectKodeAgenBySpaj(String reg_spaj)  {
		Map param = new HashMap();
		param.put("reg_spaj", reg_spaj);
		return (HashMap) querySingle("selectKodeAgenBySpaj",param);
	}
	
	public Double hitungBunga(Integer flag_bulanan, Integer mgi, Double premi, Double rate, Integer jumlah_hari, Integer flag_powersave){
		Map map = new HashMap();
		map.put("flag_bulanan", flag_bulanan);
		map.put("mgi", mgi);
		map.put("premi", premi);
		map.put("rate", rate);
		map.put("jumlah_hari", jumlah_hari);
		map.put("flag_powersave", flag_powersave);
		return (Double) querySingle("hitungBunga", map);
		
	}
	
	public Integer selectFlagPowersave(int lsbs, int lsdbs, int flag_bulanan) {
		Map params = new HashMap();
		params.put("lsbs", new Integer(lsbs));
		params.put("lsdbs", new Integer(lsdbs));
		params.put("flag_bulanan", flag_bulanan);
		Integer result = (Integer) querySingle("selectFlagPowersave", params); 
		if(result == null) {
			return -1; 
		}else {
			return result;
		}
	}

	public Integer selectCountAkseptasiKhusus(String spaj){
		return (Integer) querySingle("selectCountAkseptasiKhusus", spaj);
	}
	
	public Integer selectLsbpIdFromNoRek(int lsrek_id){
		return(Integer) querySingle("selectLsbpIdFromNoRek", lsrek_id);
	}
	
	public Map selectNameSellerRef(String id_ref, String nama_p, String tgl_lahir_p, String nama_t, String tgl_lahir_t)  throws DataAccessException {
		Map map = new HashMap();
		map.put("id_ref", id_ref);
		map.put("nama_p", nama_p);
		map.put("tgl_lahir_p", tgl_lahir_p);
		map.put("nama_t", nama_t);
		map.put("tgl_lahir_t", tgl_lahir_t);
		return (HashMap) querySingle("selectNameSellerRef",map);
	}

	public Integer selectCekRef(String nama_p, String tgl_lahir_p, String nama_t, String tgl_lahir_t) throws DataAccessException{
		Map map = new HashMap();
		map.put("nama_p", nama_p);
		map.put("tgl_lahir_p", tgl_lahir_p);
		map.put("nama_t", nama_t);
		map.put("tgl_lahir_t", tgl_lahir_t);
		return (Integer) querySingle("selectCekRef",map);
	}

	public String selectIdTrx(String id_ref, String spaj, String jenis) throws DataAccessException{
		Map map = new HashMap();
		map.put("id_ref", id_ref);
		map.put("jenis", jenis);
		map.put("spaj", spaj);
		if(jenis.equals("1")){
			return (String) querySingle("selectIdTrx",map);
		}else if(jenis.equals("2")){
			return (String) querySingle("selectNoTrx",map);
		}else if(jenis.equals("3")){
			return (String) querySingle("selectMaxNoTrx",map);
		}
		return null;
	}

	public void insertPwrTrx(String id_trx, String id_ref, String spaj, String jenis_trx) throws DataAccessException{
		Map map = new HashMap();
		map.put("id_trx", id_trx);
		map.put("id_ref", id_ref);
		map.put("spaj", spaj);
		map.put("jenis_trx", jenis_trx);
		insert("insertPwrTrx", map);
	}

	public void insertPwrDTrx(String id_trx, String item, String reg_spaj, Double mspr_premium, Integer point, String lus_id) throws DataAccessException{
		Map map = new HashMap();
		map.put("id_trx", id_trx);
		map.put("spaj", reg_spaj);
		map.put("premi", mspr_premium);
		map.put("item", item);
		map.put("point", point);
		map.put("lus_id", lus_id);
		insert("insertPwrDTrx", map);
	}

	public String selectTrx(String id_ref) throws DataAccessException{
		return (String) querySingle("selectTrx",id_ref);
	}

	public void updateDTrx(String trx, Double premi) {
		Map map = new HashMap();
		map.put("trx", trx);
		map.put("premi", premi);
		update("updateDTrx",map);
	}

	public void updatePwrTrx(String id_trx, String id_ref, String spaj) throws DataAccessException{
		Map map = new HashMap();
		map.put("id_trx", id_trx);
		map.put("id_ref", id_ref);
		map.put("spaj", spaj);
		update("updatePwrTrx",map);
	}

	public void deletePwrDtrx(String id_trx, String reg_spaj) throws DataAccessException{
		Map map = new HashMap();
		map.put("id_trx", id_trx);
		map.put("spaj", reg_spaj);
		delete("deletePwrDtrx",map);
	}

	public List selectNoUrutDTRX(String reg_spaj) throws DataAccessException{
		return query("selectNoUrutDTRX",reg_spaj);
	}

	public void deletePwrTrx(String id_trx, String jenis) throws DataAccessException{
		Map map = new HashMap();
		map.put("id_trx", id_trx);
		map.put("jenis", jenis);
		delete("deletePwrTrx",map);
	}

	public void updatePwrTrx2(String id_trx, String id_ref) throws DataAccessException{
		Map map = new HashMap();
		map.put("id_trx", id_trx);
		map.put("id_ref", id_ref);
		update("updatePwrTrx2",map);
	}

	//Referensi
	public List<Map> selectDataPolisRef(String spaj, String polis) throws DataAccessException{
		Map params = new HashMap();
		params.put("spaj",spaj);
		params.put("polis",polis);
		return query("selectDataPolisRef", params);
	}

	public List selectAgentByPolis(String policy_no) throws DataAccessException{
		return query("selectAgentByPolis", policy_no);
	}

	public String selectMsag_id(String id) throws DataAccessException{
		return (String) querySingle("selectMsag_id", id);
	}

	public String selectIdSeller(String nama, Date tgl_lahir, String id) throws DataAccessException{
		Map params = new HashMap();
		params.put("nama",nama);
		params.put("tgl_lahir",defaultDateFormat.format(tgl_lahir));
		params.put("id", id);
		return (String) querySingle("selectIdSeller", params);
	}

	public PrwSeller selectDataPs(String id) throws DataAccessException{
		return (PrwSeller) querySingle("selectDataPs", id);
	}

	public Long selectCounterIDSeller() throws DataAccessException{
		return (Long) querySingle("selectCounterIDSeller", null);
	}

	public void UpdateCounterIDSeller(Long counter) throws DataAccessException{
		update("UpdateCounterIDSeller", counter);
		
	}

	public void insertDataPWRSeller(PrwSeller ps) throws DataAccessException{
		insert("insertDataPWRSeller",ps);
	}

	public void updateMsag_idUserExternal(String ids, String id) throws DataAccessException{
		Map params = new HashMap();
		params.put("ids",ids);
		params.put("id",id);
		update("updateMsag_idUserExternal", params);
	}

	public void insertReferensi(String ids, String nama, String tgl_lahir, String no_telp, String kd_agen, String agen, String jenis, String lus_id)throws DataAccessException {
		Map params = new HashMap();
		params.put("ids",ids);
		params.put("nama",nama);
		params.put("tgl_lahir",tgl_lahir);
		params.put("no_telp",no_telp);
		params.put("kd_agen",kd_agen);
		params.put("agen",agen);
		params.put("jenis",jenis);
		params.put("lus_id", lus_id);
		
		insert("insertReferensi",params);		
	}

	public List<Map> selectReference(String id)throws DataAccessException{
		return query("selectReference", id);
	}

	public void updateIdRef(String spaj, String ids) throws DataAccessException{
		Map params = new HashMap();
		params.put("spaj",spaj);
		params.put("ids",ids);
		update("updateIdRef", params);
	}

	public String selectCekRef(String nama, String tgl_lahir, String jenis) throws DataAccessException {
		Map params = new HashMap();
		params.put("nama",nama);
		params.put("tgl_lahir",tgl_lahir);
		params.put("jenis", jenis);
		return (String) querySingle("selectCekRefInput", params);
	}
	
	public Integer selectExistingSimpol(String pp_mcl_first,String t_mcl_first,String pp_mspe_date_birth,String t_mspe_date_birth)
	{
		Map param = new HashMap();
		param.put("pp_mcl_first",pp_mcl_first);
		param.put("t_mcl_first", t_mcl_first);
		param.put("pp_mspe_date_birth",pp_mspe_date_birth);
		param.put("t_mspe_date_birth",t_mspe_date_birth);
		return (Integer) querySingle("selectExistingSimpol",param);
	}

	public void updateFlagDTRX(String spaj) throws DataAccessException {
		update("updateFlagDTRX", spaj);
	}

	public String selectKodeTambangEmas(String kode) {
		return (String) querySingle("selectKodeTambangEmas", kode);
	}

	public void insertKodeTE(String kode, String lus_id) {
		Map params = new HashMap();
		params.put("kode",kode);
		params.put("lus_id",lus_id);
		insert("insertKodeTE", params);
	}

	public void updateKodeTE(String kode, String pemegang, String tgl_lahir, String spaj) {
		Map params = new HashMap();
		params.put("kode",kode);
		params.put("pemegang",pemegang);
		params.put("tgl_lahir",tgl_lahir);
		params.put("spaj", spaj);
		update("updateKodeTE",params);
	}

	public List selectKodeGenerate(String nama, String tgl_lahir) {
		Map params = new HashMap();
		params.put("nama",nama);
		params.put("tgl_lahir",tgl_lahir);
		return query("selectKodeGenerate",params);
	}
	
	public Map selectAgentBySpajnLevelAgent(String spaj, int lsle_id){
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lsle_id", lsle_id);
		return (HashMap) querySingle("selectAgentBySpajnLevelAgent", params);
	}
	
	public void updateFlagSPH(String reg_spaj, Integer mspo_flag_sph){
		Map params = new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("mspo_flag_sph", mspo_flag_sph);
		update("updateFlagSPH", params);
	}
	
	// *kuesioner
	public Map selectagenrekrutStruktur(String kode){
		Map params = new HashMap();
		params.put("kode", kode.toUpperCase().trim());
		return (HashMap) querySingle("selectagenrekrutStruktur", params);
	}
	
	public Integer selectAgentActiveOrNot(String msag_id){
		return (Integer) querySingle("selectAgentActiveOrNot", msag_id);
	}
	
	public List selectLstAgenRegional_kues(String indexCabang)throws DataAccessException{
		Map map = new HashMap();
		map.put("indexCabang", "not in ("+indexCabang+")");
		return query("selectLstAgenRegional_kues", map);
	}
	public List selectLstAgenAgency_kues(String indexCabang)throws DataAccessException{
		Map map = new HashMap();
		map.put("indexCabang", "IN ("+indexCabang+")");
		return query("selectLstAgenAgency_kues", map);
	}
	public List selectLstHybridAJS_kues(String indexCabang)throws DataAccessException{
		Map map = new HashMap();
		map.put("indexCabang", indexCabang);
		return query("selectLstHybridAJS_kues", map);
	}
	public List selectLstAgenWorksite_kues(String indexCabang)throws DataAccessException{
		Map map = new HashMap();
		map.put("indexCabang", indexCabang);
		return query("selectLstAgenWorksite_kues", map);
	}
	public List selectLstAgen_kues(String indexCabang)throws DataAccessException{
		Map map = new HashMap();
		map.put("indexCabang", indexCabang);
		return query("selectLstAgen_kues", map);
	}

	public Integer updateEmailRef(String email, String nama, String tgl_lahir, String jns) {
		Map params = new HashMap();
		params.put("email",email);
		params.put("nama",nama);
		params.put("tgl_lahir",tgl_lahir);
		params.put("jns",jns);
		return update("updateEmailRef",params);
	}

	public List selectAgenRef(String kd_agen) {
		return query("selectAgenRef",kd_agen);
	}
	
	public Integer updatePrwTrx(String reg_spaj, String fire_id) {
		Map params = new HashMap();
		params.put("reg_spaj",reg_spaj);
		params.put("fire_id",fire_id);
		return update("updatePrwTrx",params);
	}

	public List getInfoRef(String kd_ref) {
		return query("getInfoRef",kd_ref);
	}

	public void insertRedeemTE(Redeem redeem) {
		insert("insertRedeemTE",redeem);
	}

	public Long selectPoinRedeem(String kd_ref) {
		return (Long) querySingle("selectPoinRedeem", kd_ref);
	}

	public List selectRedeemTE(String kd, String status, String id_redeem) {
		Map params = new HashMap();
		params.put("kd",kd);
		params.put("status",status);
		params.put("id_redeem",id_redeem);
		return query("selectRedeemTE",params);
	}

	public void updateRedeem(String id_redeem, String status, String lus_id) {
		Map params = new HashMap();
		params.put("id_redeem",id_redeem);
		params.put("status",status);
		params.put("lus_id",lus_id);
		update("updateRedeem",params);
	}
	
	public Map validasiAgenPasBPDbd(String kodeagen)  {
		Map param = new HashMap();
		param.put("kodeagen", kodeagen);
		return (HashMap) querySingle("validasiAgenPasBPDbd",param);
	}

	public List selectKuesionerByNoReg(String no_reg) {
		return query("selectKuesionerByNoReg",no_reg);
	}

	public List struktur_agent(String msag_id) {
		Map params = new HashMap();
		params.put("msag_id",msag_id);
		return query("struktur_agent",params);
	}
	
	public List selectPunyaStableLinkAll(Date pp_dob, String pp_name, Date tt_dob, String tt_name) throws DataAccessException{
		Map p = new HashMap();
		p.put("pp_dob", pp_dob);
		p.put("pp_name", pp_name);
		p.put("tt_dob", tt_dob);
		p.put("tt_name", tt_name);
		return query("selectPunyaStableLinkAll", p);
	}
	
	public List selectPunyaPowStabSaveAll(Date pp_dob, String pp_name, Date tt_dob, String tt_name) throws DataAccessException{
		Map p = new HashMap();
		p.put("pp_dob", pp_dob);
		p.put("pp_name", pp_name);
		p.put("tt_dob", tt_dob);
		p.put("tt_name", tt_name);
		return query("selectPunyaPowStabSaveAll", p);
	}
	
	public Map selectPremiStableLink(String spaj) throws DataAccessException{
		return (Map) querySingle("selectPremiStableLink", spaj);
	}
	
	public Map selectPremiPowStabSave(String spaj) throws DataAccessException{
		return (Map) querySingle("selectPremiPowStabSave", spaj);
	}
	public List selectDataUsulan_endors(String spaj){
		return query("selectDataUsulan_endors", spaj);
	}
	
	public void updateTransDateSlink(String spaj, Date msl_trans_date){
		Map m = new HashMap();
		m.put("spaj", spaj);
		m.put("msl_trans_date", msl_trans_date);
		update("updateTransDateSlink",m);
	}
	
	public Map selectbroker(String kodebroker) {
		Map param = new HashMap();
		param.put("kodebroker", kodebroker);
		return (HashMap) querySingle("selectbroker",param);
	}
	
	public Broker select_detilbroker(String spaj) {
		return (Broker) querySingle("select_detilbroker", spaj);
	}
	
	public void insertMst_comm_broker(Broker broker) {
		insert("insert.mst_comm_broker", broker);
	}
	
	public Endors selectMstEndors(String spaj, String lspd_id, String msen_flag_issue){
		Map param = new HashMap();
		param.put("spaj", spaj);
		param.put("lspd_id",lspd_id);
		param.put("msen_issue_spaj", msen_flag_issue);
		return (Endors) querySingle("selectMstEndors", param);
		
	}

	public List selectDataKontesProklamasi() throws DataAccessException {
		return query("selectDataKontesProklamasi", null);
	}
	
	public List selectDataKontesPhoto() throws DataAccessException {
		return query("selectDataKontesPhoto", null);
	}
	
	public List selectPolisBatalNonProduction(){
		return query("selectPolisBatalNonProduction",null);
	}
	
	public String selectGetAccPremi(String reg_spaj, Integer li_jenis) throws DataAccessException{
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
//		1 premi single, 
//		2 topup single, 
//		3 premi pokok, 
//		4 premi lanjutan tahun pertama, 
//		5 premi lanjutan tahun lanjutan, 
//		6 premi top up
		map.put("li_jenis", li_jenis);
		
		Map detbisnis = (Map) uwDao.selectDetailBisnis(reg_spaj).get(0);
		String lsbs_id = (String) detbisnis.get("BISNIS");
		String account = (String) querySingle("selectGetAccPremi", map);
		if("204".equals(lsbs_id) && "611112".equals(account)) account = "611115";
		return account;
	}
	
	public List<DropDown> selectCompanyUpload(String lca) {
			
			return query("selectCompanyUpload",lca);
	}
	
	public void insertMstSpajTemp(Cmdeditbac spaj, String no_temp, String reff_bank, String agent_bank) {
			Map m=new HashMap();
			m.put("no_temp", no_temp);	
			m.put("pemegang", spaj.getPemegang());	
			m.put("tertanggung", spaj.getTertanggung());
			m.put("account_recur", spaj.getAccount_recur());
			m.put("datausulan", spaj.getDatausulan());
			m.put("reff_bank", reff_bank);
			m.put("agen_bank", agent_bank);
			m.put("pembayar", spaj.getPembayarPremi());
			insert("insert.mstSpajTemp", m);
			
	}

	public void insertAddressBilling(Cmdeditbac spaj,
			String no_temp) {
		
		Map m=new HashMap();
		m.put("no_temp", no_temp);	
		m.put("addr", spaj.getAddressbilling());
		insert("insert.addressBillingTemp", m);
		
	}

	public void insertProductTemp(Cmdeditbac excelList, String no_temp) {
		Map m=new HashMap();
		m.put("no_temp", no_temp);
		m.put("datausulan", excelList.getDatausulan());
		insert("insert.productTemp", m);
	}

	public void insertProductTempRider(Datarider rd, String no_temp) {
		Map m=new HashMap();
		m.put("no_temp", no_temp);
		m.put("rd", rd);
		insert("insert.productTempRider", m);
	}

	public void insertPesertaTemp(PesertaPlus_mix psrt, String no_temp) {
		// TODO Auto-generated method stub
		Map m=new HashMap();
		m.put("no_temp", no_temp);
		m.put("peserta", psrt);
		insert("insert.pesertaTemp", m);
	}
	public Pemegang selectppTemp(String no_temp) throws DataAccessException {
		return (Pemegang)querySingle("selectppTemp", no_temp);
	}
	
	public String selectNoSertifikat(String reg_Spaj){
		return (String) querySingle("selectNoSertifikat", reg_Spaj);
	}

	public String insertMedQuestTemp(MedQuest medQuest,
			MedQuest_tambah medQuest_tambah, MedQuest_tambah2 medQuest_tambah2,MedQuest_tambah3 medQuest_tambah3,
			MedQuest_tambah4 medQuest_tambah4, MedQuest_tambah5 medQuest_tambah5, String no_temp) {
		Map m=new HashMap();
		m.put("no_temp", no_temp);	
		if(medQuest.getMsadm_berat()!=null){			
			m.put("medquest",medQuest);
			insert("insert.medquest_temp",m);
		}
		 if(medQuest_tambah.getMsadm_berat()!=null){			
			m.put("medquest_tambah",medQuest_tambah);
			insert("insert.medquest_tambah_temp",m);
		}
		 if(medQuest_tambah2.getMsadm_berat()!=null){			
			m.put("medquest_tambah2",medQuest_tambah2);
			insert("insert.medquest_tambah2_temp",m);
		}
		 if(medQuest_tambah3.getMsadm_berat()!=null){			
			m.put("medquest_tambah3",medQuest_tambah3);
			insert("insert.medquest_tambah3_temp",m);
		}
		 if(medQuest_tambah4.getMsadm_berat()!=null){			
			m.put("medquest_tambah4",medQuest_tambah4);
			insert("insert.medquest_tambah4_temp",m);
		}
		 if(medQuest_tambah5.getMsadm_berat()!=null){			
			m.put("medquest_tambah5",medQuest_tambah5);
			insert("insert.medquest_tambah5_temp",m);
		}
		return null;

	}

	public AddressBilling selectAddressBillingTemp(String no_temp) {
		// TODO Auto-generated method stub
		return (AddressBilling)querySingle("selectAddressBillingTemp", no_temp);
	}

	public Tertanggung selectttTemp(String no_temp) {
		// TODO Auto-generated method stub
		return (Tertanggung) querySingle("selectttTemp", no_temp);
	}

	public Datausulan selectMstProductTemp(String no_temp)throws DataAccessException {
		// TODO Auto-generated method stub
		return (Datausulan) querySingle("selectMstProductTemp", no_temp);
	}

	public List<Datarider> selectMstProductTempRider(String no_temp) {
		// TODO Auto-generated method stub
		return (List<Datarider>) query("selectMstProductTempRider", no_temp);
	}

	public Account_recur selectAccRecurTemp(String no_temp) {
		// TODO Auto-generated method stub
		return (Account_recur) querySingle("selectAccRecurTemp", no_temp);
	}

	public List<PesertaPlus_mix> selectPesertaTemp(String no_temp) {
		return (List<PesertaPlus_mix>) query("selectPesertaTemp", no_temp);
	}
	
	public InvestasiUtama selectinvTemp(String no_temp) throws DataAccessException {
		return (InvestasiUtama)querySingle("selectinvTemp", no_temp);
	}
    
	public DetilTopUp selectDetilTopupTemp(String no_temp)throws DataAccessException{
		return (DetilTopUp)querySingle("selectDetilTopupTemp", no_temp);
	}
	
	public List selectDetilInvTemp(String no_temp)throws DataAccessException{
		return (List)query("selectDetilInvTemp", no_temp);
	}
	
	public List selectBiayaInvTemp(String no_temp)throws DataAccessException{
		return (List) query("selectBiayaInvTemp", no_temp);
	}
	
	public void updateSpajTemp(String no_temp, String reg_spaj) {
		Map map = new HashMap();
		map.put("no_temp", no_temp);		
		map.put("spaj", reg_spaj);
		update("updateMstSpajTemp",map);
		
	}

	public void updateAddressBillingTemp(String no_temp,String reg_spaj) {
		Map map = new HashMap();
		map.put("no_temp", no_temp);		
		map.put("spaj", reg_spaj);
		update("updateMstAddressBillingTemp",map);
		
	}

	public void updateProductTemp(String no_temp,String reg_spaj) {
		Map map = new HashMap();
		map.put("no_temp", no_temp);		
		map.put("spaj", reg_spaj);
		update("updateMstProductTemp",map);
		
	}

	public void updatePesertaTemp(String no_temp,String reg_spaj) {
		Map map = new HashMap();
		map.put("no_temp", no_temp);		
		map.put("spaj", reg_spaj);
		update("updateMstPesertaTemp",map);
		
	}

	public void copyMedquestTemp(String no_temp,Cmdeditbac edit) {
		
			Map map = new HashMap();
			map.put("no_temp", no_temp);		
			map.put("spaj", edit.getPemegang().getReg_spaj());
			insert("insertMedQuestFromTemp",map);
		
	}

	public Integer selectCountMedquestTemp(String no_temp) {		
		return (Integer) querySingle("selectCountMedquestTemp", no_temp);
	}
	
	public Integer selectCountQuestionaireTemp(String no_temp) {		
		return (Integer) querySingle("selectCountQuestionaireTemp", no_temp);
	}

	public List selectdataTempAll(String bdate, String edate, Integer cmp_id,
			String company1, String stpolis) {
		Map map = new HashMap();
		map.put("bdate", bdate);
		map.put("edate", edate);
		map.put("dist", cmp_id);
		map.put("company", company1);
		map.put("stpolis", stpolis);
		return query("selectdataTempAll", map);
	}

	public String selectLbn_id(String lsbp_id) {
		return (String) querySingle("selectLbn_id", lsbp_id);
	}

	public Map selectDataSpajBundle(String mspo_spaj_bundle, String pp_name, Date dob) {
		Map map = new HashMap();
		pp_name="%" + pp_name.replaceAll(" ", "%").toUpperCase().trim() + "%";
		map.put("pp_name",pp_name);
		map.put("dob", dob);
		map.put("spaj", mspo_spaj_bundle);
		return (HashMap) querySingle("selectDataSpajBundle",map);
	}
	
	public void deleteMstProdIns(String msen_endors_no){
		delete("delete.MstProdIns",msen_endors_no);
	}
	
	public void deleteMstDetEndors(String msen_endors_no){
		delete("delete.MstDetEndors",msen_endors_no);
	}
	public void deleteMstEndors(String msen_endors_no){
		delete("delete.MstEndors",msen_endors_no);
	}

	public void deleteMstAdditionalTemp(String notemp) {
		delete("delete.MstAdditionalTemp",notemp);		
	}

	public void deleteMstaddrBillTemp(String notemp) {
		delete("delete.MstaddrBillTemp",notemp);
	}

	public void deleteMstPesertaTemp(String notemp) {
		delete("delete.MstPesertaTemp",notemp);
	}

	public void deleteMstProductTemp(String notemp) {
		delete("delete.MstProductTemp",notemp);
	}

	public void deleteMstSpajTemp(String notemp) {
		delete("delete.MstSpajTemp",notemp);
	}
	
	public Long selectCounterBlanko(String id) throws DataAccessException{
		return (Long) querySingle("selectCounterBlanko", id);
	}

	public void UpdateCounterBlanko(Long counter, String id) throws DataAccessException{
		Map m=new HashMap();
		m.put("id", id);
		m.put("counter", counter);
		update("UpdateCounterBlanko", m);
	}
	
	public List selectJenisSurat(String cabang) {
		return query("selectJenisSurat", cabang);
	}

	public String insertRequestPrintSurat(User currentUser, String spaj, String polis, String jenis_surat, String surat) throws DataAccessException, MailException, MessagingException {
		Map m=new HashMap();
		m.put("jenis", jenis_surat);
		m.put("user_req", currentUser.getLus_id());
		m.put("reg_spaj", spaj);
		
		insert("insertRequestPrintSurat",m);
		
		//jika ada kirim email
		/*email.send(true, "ajsjava@sinarmasmsiglife.co.id", new String[]{"canpri@sinarmasmsiglife.co.id"}, null, null, 
				"Request Print Surat", 
				"Email in terkirim otomatis" +
				"\n\nRequest Print Surat" +
				"\nPemohon\t : "+currentUser.getLus_full_name()+
				"\nPolis\t : "+polis+
				"\nSurat\t : "+surat, 
				null);*/
		
		/*Date nowDate = commonDao.selectSysdate();
		String me_id = sequence.sequenceMeIdEmail();
		EmailPool.send(me_id,"E-Lions", 1, 1, 0, 0, 
				null, 0, 0, nowDate, null, 
				true, "ajsjava@sinarmasmsiglife.co.id", 
				new String[]{"Budiaji@sinarmasmsiglife.co.id","feri_w@sinarmasmsiglife.co.id"}, 
				new String[]{currentUser.getEmail()}, 
				new String[]{"canpri@sinarmasmsiglife.co.id","andy@sinarmasmsiglife.co.id","deddy@sinarmasmsiglife.co.id"}, 
				"Request Print Surat", 
				"Email in terkirim otomatis" +
				"\n\nRequest Print Surat" +
				"\nPemohon\t : "+currentUser.getLus_full_name()+
				"\nPolis\t : "+polis+
				"\nSurat\t : "+surat, 
				null);*/
		
		return "Permintaan surat untuk no. polis "+polis+" berhasil.";
	}

	public List selectFindPolis(String cari) {
		return query("selectFindPolis", cari);
	}

	public List selectReqPrintSurat(String lus_id) {
		return query("selectReqPrintSurat", lus_id);
	}
	
	public List selectKomisiCrossSelling(String cari) {
		return query("selectKomisiCrossSelling", cari);
	}
	
	public String selectNoBlankoPas(String reg_spaj, String no_kartu){
		Map paramBlanko = new HashMap();
		paramBlanko.put("no_kartu", no_kartu);
		return (String) querySingle("selectNoBlankoPas",paramBlanko);
	}
	
	public Map selectDataMstProposal(String id_proposal){
		return (HashMap) querySingle("selectDataDariProposal",id_proposal);
	}
	
	public Map selectMstDataProposal(String id_proposal){
		return (Map) querySingle("selectMstDataProposal", id_proposal);
	}
	
	public Map selectMstProposalProduct(String id_proposal){
		return (Map) querySingle("selectMstProposalProduct", id_proposal);
	}
	
	public List selectMstProposalProductRider(String id_proposal){
		return query("selectMstProposalProductRider", id_proposal);
	}
	
	public List selectMstProposalProductPeserta(String id_proposal){
		return query("selectMstProposalProductPeserta", id_proposal);
	}
	
	public List selectMstProposalProductTopUp(String id_proposal){
		return query("selectMstProposalProductTopUp", id_proposal);
	}
	
	public List selectAgentRekruter(String msag_id){
		return query("selectAgentRekruter", msag_id);
	}
	
	public void updateDetVa(String no_va,String lus_id, String spaj) {
		Map map = new HashMap();
		map.put("no_va", no_va);		
		map.put("lus_id", lus_id);
		map.put("spaj",spaj);
		update("updateMstDetVa",map);
		
	}
	
	public List selectJenisPSN()
	{
		return query("select_psn",null);
	}
	
	public List selectDataLembagaPsn(String id_psn)
	{
		return query("selectDataLembagaPsn",id_psn);
	}
	
	public List selectDataHargaItem(String id_psn)
	{
		return query("selectDataHargaItem",id_psn);
	}
	
	public List selectDataReligi(String id_psn)
	{
		return query("selectDataReligi",id_psn);
	}
	
	public void insertPSN(String reg_spaj, Integer id_item , Integer id_psn, Integer id_lbg, Date tgl_pot, Integer flag_pot, Integer freq_pot,
			Integer jml_pot,Integer jml_item , Integer jenis_wakaf, Integer lus_id,Date tgl_input, Integer flag_aktif) throws DataAccessException {
		Map param = new HashMap();
		param.put("reg_spaj",reg_spaj);
		param.put("id_item", id_item);
		param.put("id_psn", id_psn);
		param.put("id_lbg", id_lbg);
		param.put("tgl_pot", tgl_pot);
		param.put("flag_pot", flag_pot);
		param.put("freq_pot",freq_pot);
		param.put("jml_pot", jml_pot);
		param.put("jml_item", jml_item);
		param.put("jenis_wakaf", jenis_wakaf);
		param.put("lus_id", lus_id);
		param.put("tgl_input", tgl_input);
		param.put("flag_aktif", flag_aktif);
		insert("insertPSN", param);
	}

	public Agen select_detilagen3(String msag_id) {
		
		return (Agen) querySingle("select_detilagen3", msag_id);
		
	}
	public List<Map> selectReffTm(String no_temp) {		
		Map param= new HashMap();
		param.put("no_temp",no_temp);			
		return  query("selectReffTm", param);
	}

	public void updateReffTm(String reg_spaj, String spv, String no_kerjasama,
			String app,String reff_tm) {
		Map m=new HashMap();
		m.put("reg_Spaj", reg_spaj);
		m.put("spv", spv);
		m.put("tm", no_kerjasama);
		m.put("app", app);
		m.put("reff_tm_id", reff_tm);
		update("updateReffTm", m);
		
	}

	public List select_reff_shintabyagentcode(String query, Integer kode_flag, Boolean show_ajspusat) {
		Map param = new HashMap();
		param.put("code",query);
		param.put("kode_flag",kode_flag);
		param.put("show_ajspusat",show_ajspusat);
		return  query("select_reff_shintabyagentcode",param);
	}

	public List sselect_reff_shintabyagentcode2(String query, Integer kode_flag, Boolean show_ajspusat) {
		Map param = new HashMap();
		param.put("code",query);
		param.put("kode_flag",kode_flag);
		param.put("show_ajspusat",show_ajspusat);
		return  query("select_reff_shintabyagentcode2",param);
	}
	
	public void insertAccount_recur(Cmdeditbac spaj, String no_temp) {
		Map m=new HashMap();
		m.put("no_temp", no_temp);	
		m.put("accRec", spaj.getAccount_recur());
		insert("insert.accountRecurTemp", m);
		
	}

	public void insertBenefeciaryTemp(Benefeciary bf, String no_temp) {
		Map m=new HashMap();
		m.put("no_temp", no_temp);
		m.put("bf", bf);
		insert("insert.benefeciaryTemp", m);
		
	}

	public List<Map> selectReffSinarmas(String reff_bank, Integer filter) {
		Map param= new HashMap();
		param.put("reff_bank",reff_bank);		
		param.put("filter",filter);	
		return  query("selectReffSinarmas", param);
	}

	public void insertReffbiiTemp(ReffBii reffbii, String no_temp) {
		Map m=new HashMap();
		m.put("no_temp", no_temp);	
		m.put("reffbii", reffbii);
		insert("insert.ReffbiiTemp", m);
		
	}

	public void copybenefTemp(String no_temp, Cmdeditbac edit) {			
		Map map = new HashMap();
		map.put("no_temp", no_temp);		
		map.put("spaj", edit.getPemegang().getReg_spaj());
		insert("insertbenefTemp",map);		
	}

	public void copyReffBiiTemp(String no_temp, Cmdeditbac edit) {
		Map map = new HashMap();
		map.put("no_temp", no_temp);		
		map.put("spaj", edit.getPemegang().getReg_spaj());
		insert("insertReffBiiFromTemp",map);		
		
	}
	
	/*public void copyReffBiiTemp2(String no_temp, String spaj) {
		Map map = new HashMap();
		map.put("no_temp", no_temp);		
		map.put("spaj", spaj);
		insert("insertReffBiiFromTemp",map);		
		
	}*/
	
	public Integer selectCountbenefTemp(String no_temp) {
		return (Integer) querySingle("selectCountbenefTemp", no_temp);
	}

	public String selectCountReffBiiTemp(String no_temp) throws DataAccessException {
		return (String) querySingle("selectCountReffBiiTemp", no_temp);
	}

	public List selectMstPositionSpajForStatusBas(String spaj) {
		return query("selectMstPositionSpajForStatusBas",spaj);
	}

	public List<DropDown> selectEmailAutoComplete(String spaj) {
		return query("selectCEmailAutoComplete",spaj);
	}

	public void updateMstDrekCc(String no_kartu, Integer jumlah) {
		Map m=new HashMap();
		m.put("no_kartu", no_kartu);
		m.put("jumlah", jumlah);
		update("updateMstDrekCc", m);
	}
	
	public List selectGadgetisHere()
	{
		return query("selectViewDataGadget",null);
	}
	
	public List selectReportESpaj(String bdate, String edate) {
		Map m=new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		return query("selectReportESpaj", m);
	}
	
	public List selectDataPSN(String reg_spaj)
	{
		return query("selectViewDataPSN",reg_spaj);
	}

	public HashMap select_det_va_spaj(String spaj) throws DataAccessException {
		return (HashMap) querySingle("select_det_va_spaj", spaj);
	}
	
	public void insertQuestionareNew( Integer key_type , String key_id, Integer q_type_id, Integer q_id,Date q_valid_date, Integer op_type, Integer op_grp,
		Integer op_order,Integer an_order , String answer, Date input_date,Integer lus_id) throws DataAccessException {
		Map param = new HashMap();
		param.put("key_type",key_type);
		param.put("key_id", key_id);
		param.put("q_type_id", q_type_id);
		param.put("q_id", q_id);
		param.put("q_valid_date", q_valid_date);
		param.put("op_type", op_type);
		param.put("op_grp", op_grp);
		param.put("op_order",op_order);
		param.put("an_order", an_order);
		param.put("answer", answer);
		param.put("input_date", input_date);
		param.put("lus_id", lus_id);
		insert("insertQuestionareNew", param);
	}
	
	public List selectQuestionareNew(String spaj , Integer type ,  Integer index, Integer index2)
	{
		Map p = new HashMap();
		p.put("spaj", spaj);
		p.put("type", type);
		p.put("index", index);
		p.put("index2", index2);
		return query("selectQuestionareNew",p);
	}
	
	public List selectQuestionareGadget(String spaj , Integer type ,  Integer index, Integer index2)
	{
		Map p = new HashMap();
		p.put("spaj", spaj);
		p.put("type", type);
		p.put("index", index);
		p.put("index2", index2);
		return query("selectQuestionareGadget",p);
	}
	
	public void deleteQuestionareNew(String key_id, String kata) throws DataAccessException {
		Map p = new HashMap();
		p.put("key_id", key_id);
		p.put("kata", kata);
		delete("deleteQuestionareNew",p);
	}
	
	public void updateQuestionareNewPPTT(String mcl_id, String kolom, Double answer)throws DataAccessException{
		Map<String, Object>params=new HashMap<String, Object>();
		params.put("mcl_id", mcl_id);
		params.put("kolom", kolom);
		params.put("answer", answer);
		update("updateQuestionareNewPPTT", params);
	}
	
	public Integer selectCountQuestionareNew(String key_id) {
		return (Integer) querySingle("selectCountQuestionareNew", key_id);
	}
	
	public List<DropDown> selectLstPekerjaan() throws DataAccessException {
		return query("selectLstPekerjaan",null);
	}
	
	public HashMap<String, Object> selectPropertiesPrinter(){
		return (HashMap)querySingle("selectPropertiesPrinter",null);
	}
	
	public String getAllowPrint (String printerName) throws DataAccessException {
		return (String) querySingle("selectAllowPrint",printerName);
	}
	
	public List select_report_upload_scan(Map params) throws DataAccessException {
		
		return query("select_report_upload_scan", params);
	}


	public Integer selectno_virtual_Exist(String no_virtual_acc) {
		return (Integer) querySingle("selectno_virtual_active", no_virtual_acc);
	}
	
	public PembayarPremi selectP_premi(String spaj) throws DataAccessException {
		return (PembayarPremi) querySingle("selectP_premi", spaj);
	}
	
//	public PembayarPremi selectP_premi2(String spaj) throws DataAccessException {
//		return (PembayarPremi) querySingle("selectP_premi2", spaj);
//	}
	
	public Map selectIsPSNSyariah(String spaj){
		return (HashMap) querySingle("selectIsPSNSyariah", spaj);
	}

//	public Double selectPersenKomisiReffBiiNew(String no_spaj,
//			Date ldt_beg_date, Double ldec_premi, int mgi) {
//		Double persenKomisi = 0.;
//		Map m = new HashMap();
//		m.put("no_spaj", no_spaj);
//		m.put("ldt_beg_date", ldt_beg_date);
//		m.put("ldec_premi", ldec_premi);
//		m.put("mgi", mgi);
//		persenKomisi = (Double) querySingle("selectPersenKomisiReffBiiNew", m);
//		return persenKomisi;
//	}
	
//	public String selectGetAccKomisi(String reg_spaj, Integer bank, Integer li_jenis) throws DataAccessException{
//		Map map = new HashMap();
//		map.put("reg_spaj", reg_spaj);
//		1 komisi single perorangan, 
//		2 komisi single bank, 
//		3 komisi premi berkala pertama perorangan, 
//		4 komisi premi berkala pertama bank,
//		9 fee based
//		10 reward, 
//		12 bonus
//		map.put("li_jenis", li_jenis);
//		map.put("li_bank", bank);
//		return (String)  querySingle("selectGetAccKomisi", map);
//	}
	
	public void insertRekening_client_temp(Cmdeditbac edit, String no_temp) {
		Map m=new HashMap();
		m.put("no_temp", no_temp);	
		m.put("rekClient", edit.getRekening_client());
		insert("insert.rekClientTemp", m);
	
	}	

	public void insertMstBiayaUlinkTemp(Map<String, Comparable> m) {
		insert("insert.MstBiayaUlinkTemp", m);		
	}

public void insertMst_ulink_temp(String no_temp,
			InvestasiUtama investasiutama) {
		Map m=new HashMap();
		m.put("no_temp", no_temp);	
		m.put("inv", investasiutama);
		insert("insert.MstUlinkTemp", m);
		
	}

	public void insertDetUlinkTemp(HashMap<String, Comparable> param) {
		
		insert("insert.MstDetUlinkTemp", param);
	}
	
	public void updateDataClient(Map dataClient){
		insert("updateDataClient",dataClient);
	}
	
	public void updateDataAddress(Map dataAddress){
		update("updateDataAddress",dataAddress);
	}
	
	public void updateDataClientBadanHukum(Map dataClient){
		insert("updateDataClientBadanHukum",dataClient);
	}
	
	public void insertPerusahaanPembayarPremi(Map perusahaanPembayarPremi){
		insert("insertPerusahaanPembayarPremi",perusahaanPembayarPremi);
	}
	
	public void insertNewKyc(Map dataKyc){
		insert("insertNewKyc", dataKyc);
	}
	
	public void insertNewClient(Map pembayarPremiClient){
		insert("insertNewClientPayer", pembayarPremiClient);
	}
	
	public void updateMstPolicyPayer(Map dataMstPolicy){
		update("updateMstPolicyPayer", dataMstPolicy);
	}
	
	public void updateMstClient(Map dataClient){
		update("updateMstClient",dataClient);
	}
	
	public void updateAlasanPayer(Map alasanClient){
		update("updateAlasanPayer",alasanClient);
	}
	
	public PembayarPremi selectPembayarPremi(String spaj) throws DataAccessException {
		return (PembayarPremi)querySingle("selectPembayarPremi", spaj);
	}
	
	public Integer selectPemegangPolis(String spaj) throws DataAccessException{
		return (Integer) querySingle("selectPemegangPolis", spaj);
	}
	
	public String selectKeteranganKerja(String idKerja) throws DataAccessException{
		return (String) querySingle("selectKeteranganKerja", idKerja);
	}
	
	public void updateMstPolicyMspoPayer(String spaj,String mspoPayer){
		Map up_policy=new HashMap();
		up_policy.put("ls",mspoPayer);
		up_policy.put("nospaj",spaj);
		update("simultan.update.mst_policy_payer",up_policy);
	}
	
	public String selectMspoPayerOld(String spaj) {
		return (String) querySingle("select_mspo_payer_old", spaj);
	}
	
	public HashMap<String, Object> selectJenisPihakKetiga(String spaj){
		return (HashMap)querySingle("selectJenisPihakKetiga",spaj);
	}
	
	public String selectIdLstPekerjaan(String lsp_name) {
		return (String) querySingle("selectIdLstPekerjaan", lsp_name);}
	
	public List selectSpajPrint() throws DataAccessException {
		return query("selectSpajPrint", null);
	}
	
	public String selectSequenceSimultan(){
		return (String) querySingle("selectSequenceSimultan", null);
	}
	
	public List<Pas> selectAllUploadPasList(Map param){	
		return query("selectAllUploadPasList",param);
	}
	
	public List<TmSales> selectAllUploadPasFreeList(Map param){	
		return query("selectAllUploadPasFreeList",param);
	}
	
	public String selectTotalAllUploadPasList(Map params){
		return (String) querySingle("selectTotalAllUploadPasList",params);
	}
	
	public String selectTotalAllUploadPasFreeList(Map params){
		return (String) querySingle("selectTotalAllUploadPasFreeList",params);
	}
	
	public String selectMspoFlagSpaj(String spaj){
		return (String) querySingle("selectMspoFlagSpaj", spaj);
	}
	
	public void copyQuestionaireTemp(String no_temp, Cmdeditbac edit) {
		Map map = new HashMap();
		map.put("no_temp", no_temp);		
		map.put("spaj", edit.getPemegang().getReg_spaj());
		insert("insertQuestionaireFromTemp",map);		
		
	}
	
	public PembayarPremi selectPemPremiTemp(String no_temp) throws DataAccessException {
		return (PembayarPremi)querySingle("selectPemPremiTemp", no_temp);
	}
	
	public Integer selectCountKeluarga(String no_temp){
		return (Integer) querySingle("selectCountKeluargaTemp", no_temp);
	}
	
	public List selectDaftarInvestasiTemp(String no_temp){
		return query("selectDaftarInvestasiTemp", no_temp);
	}
	
	public List select_semua_mst_peserta2(String reg_spaj){
		return query("select_semua_mst_peserta2", reg_spaj);
	}
	
	public List selectDaftar_extra_premi(String spaj) {
		return query("selectDaftar_extra_premi", spaj);
	}

	public void insertClientNewPemegangTemp(Cmdeditbac excelList, String no_temp) {
		Map map = new HashMap();
		map.put("pemegang",excelList.getPemegang());
		insert("insertClientNewPpTemp",map);	
	}

	public void insertAddressNewPemegangTemp(Cmdeditbac excelList, String no_temp) {
		Map map = new HashMap();
		map.put("pemegang",excelList.getPemegang());
		insert("insertAddrNewPpTemp",map);
	}	
	
	public void insertClientNewTtgTemp(Cmdeditbac excelList, String no_temp) {
		Map map = new HashMap();
		map.put("ttg",excelList.getTertanggung());
		insert("insertClientNewTtgTemp",map);	
	}

	public void insertAddressNewTtgTemp(Cmdeditbac excelList, String no_temp) {
		Map map = new HashMap();
		map.put("ttg",excelList.getTertanggung());
		insert("insertAddrNewTtgTemp",map);
	}

	public void insertClientNewPayerTemp(Cmdeditbac excelList, String no_temp) {
		Map map = new HashMap();
		map.put("payer",excelList.getPembayarPremi());
		insert("insertClientNewPayerTemp",map);		
	}

	public void insertAddressNewPayerTemp(Cmdeditbac excelList, String no_temp) {
		Map map = new HashMap();
		map.put("payer",excelList.getPembayarPremi());
		insert("insertAddrNewPayerTemp",map);		
	}

	public void insertQuestionAnswerTemp(MstQuestionAnswer question,
			String no_temp) {
		Map map = new HashMap();
		map.put("question",question);
		map.put("no_temp",no_temp);
		insert("insertQuestionAnswerTemp",map);		
	}

	public Pemegang selectppTempNew(String no_temp) {
		return (Pemegang)querySingle("selectppTempNewSIO", no_temp);
	}

	public Tertanggung selectttgTempNew(String no_temp) {
		return (Tertanggung)querySingle("selectttgTempNewSIO", no_temp);
	}

	public void insertKycTemp(String no_temp, int noUrut, String kycId,
			String kycPp, String kycDesc) {
		Map kyc = new HashMap();
		kyc.put("no_temp", no_temp);
		kyc.put("no_urut",noUrut);
		kyc.put("kyc_id",kycId);
		kyc.put("kyc_pp",kycPp);
		kyc.put("kyc_desc", kycDesc);
		insert("insertKycTemp",kyc);	
	}

	public Rekening_client selectRekeningNasabahTemp(String no_temp) {		
		return (Rekening_client)querySingle("selectRekeningNasabahTemp", no_temp);
	}
	
	public List<Map> selectDaftarPolisBSMBelumPrint(String lus_id) throws DataAccessException{
		return query("selectDaftarPolisBSMBelumPrint", lus_id);
	}
	
	public Integer count_answer_table(String key_id, Integer question_type_id, Integer question_id, Integer option_order){
		Map map = new HashMap();
		map.put("key_id",key_id);
		map.put("question_type_id",question_type_id);
		map.put("question_id",question_id);
		map.put("option_order",option_order);
		return (Integer) querySingle("count_answer_order", map);
	}

	public void deleteMstRekcLientTemp(String notemp) {
		delete("delete.MstRekcLientTemp",notemp);
	}

	public void deleteMstBiayaUlinkTemp(String notemp) {
		delete("delete.MstBiayaUlinkTemp",notemp);
	}

	public void deleteMstDetUlinkTemp(String notemp) {
		delete("delete.MstDetUlinkTemp",notemp);
	}

	public void deleteMstUlinkTemp(String notemp) {
		delete("delete.MstUlinkTemp",notemp);
	}

	public void deleteMstAddressNewTemp(String notemp) {
		delete("delete.MstAddressNewTemp",notemp);
	}
	
	public void deleteMstAddressNewTempPemegang(String notemp) {
		delete("delete.MstAddressNewTempPemegang",notemp);
	}
    
    public String selectTotalSmsserver_out(String beg_date, String end_date, String reg_spaj,String no_polis,Integer id,Integer lus_id, Map params)throws DataAccessException{
    	HashMap<String, Object> param=new HashMap<String, Object>();
    	param.put("beg_date", beg_date);
    	param.put("end_date", end_date);
    	param.put("reg_spaj", reg_spaj);
    	param.put("no_polis", no_polis);
    	param.put("id",id);
    	param.put("lus_id",lus_id);
    	if (params!=null){
			param.putAll(params);
		}
    	return (String) querySingle("selectTotalSmsserver_out", param);
    }
    
    public List<Smsserver_in> selectSmsserver_out(String beg_date, String end_date, String reg_spaj,String no_polis,Integer id,Integer lus_id, Map params)throws DataAccessException{
    	HashMap<String, Object> param=new HashMap<String, Object>();
    	param.put("beg_date", beg_date);
    	param.put("end_date", end_date);
    	param.put("reg_spaj", reg_spaj);
    	param.put("no_polis", no_polis);
    	param.put("id",id);
    	param.put("lus_id",lus_id);
    	if (params!=null){
			param.putAll(params);
		}    	
    	return query("selectSmsserver_out", param);
    }
    
    public String selectTotalResultSmsserver_out(String beg_date, String end_date, String reg_spaj,String no_polis,Integer id,Integer lus_id, Map params)throws DataAccessException{
    	HashMap<String, Object> param=new HashMap<String, Object>();
    	param.put("beg_date", beg_date);
    	param.put("end_date", end_date);
    	param.put("reg_spaj", reg_spaj);
    	param.put("no_polis", no_polis);
    	param.put("id",id);
    	param.put("lus_id",lus_id);
    	if (params!=null){
			param.putAll(params);
		}
    	return (String) querySingle("selectTotalResultSmsserver_out", param);
    }

	public Integer selectCountGadgetSpaj(String spaj) throws DataAccessException {		
		return (Integer) querySingle("selectCountGadgetSpaj", spaj);
	}
    
    public ArrayList<Smsserver_out_hist> selectSmsserver_out_hist(Integer id) throws DataAccessException {
    	return (ArrayList<Smsserver_out_hist>) query("selectSmsserver_out_hist", id);
    }
    
    public ArrayList<HashMap<String, Object>> selectReportTotalSmsOutBulanan(String month, String year) throws DataAccessException {
    	HashMap<String, Object> params = new HashMap<String, Object>();
    	params.put("month", month);
    	params.put("year", year);
    	
    	return (ArrayList<HashMap<String, Object>>) query("selectReportTotalSmsOutBulanan", params);
    }
	
	public void updateMstInboxHistLpsdId(String mi_id, String mi_desc, Integer lspd_before, Integer lspd_after, Integer flag_kategori){
		Map map = new HashMap();
		map.put("mi_id",mi_id);
		map.put("mi_desc",mi_desc);
		map.put("lspd_before", lspd_before);
		map.put("lspd_after", lspd_after);
		map.put("flag_kategori", flag_kategori);
		update("updateMstInboxHistLpsdId", map);
	}
	
	public List selectReferensiTempSpaj( String spaj){
		return query("selectReferensiTempSpaj",spaj);
	}

	public void deleteMstAddressNewTempTtg(String notemp) {
		delete("delete.MstAddressNewTempTtg",notemp);
	}

	public void deleteMstAddressNewTempPembayarPremi(String notemp) {
		delete("delete.MstAddressNewTempPembayarPremi",notemp);
	}
	
	public void deleteMstClientNewTempPemegang(String notemp) {
		delete("delete.MstClientNewTempPemegang",notemp);
	}
	
	public void deleteMstClientNewTempTtg(String notemp) {
		delete("delete.MstClientNewTempTtg",notemp);
	}

	public void deleteMstClientNewTempPembayarPremi(String notemp) {
		delete("delete.MstClientNewTempPembayarPremi",notemp);
	}

	public void deleteMstBenefTemp(String notemp) {
		delete("delete.MstBenefTemp",notemp);
	}
	
	public void deleteMstAccounRecurTemp(String notemp) {
		delete("delete.MstAccounRecurTemp",notemp);
	}
	
	public void deleteMstQuestionAnswerTemp(String notemp) {
		delete("delete.MstQuestionAnswerTemp",notemp);
	}
	
	public void deleteMstReffBiiTemp(String notemp) {
		delete("delete.MstReffBiiTemp",notemp);
	}
	
	public List selectLstLevelDist(Integer dist) throws DataAccessException{
		Map map = new HashMap();
		map.put("dist", dist);
		return query("selectLstLevelDist", map);
	}
	
	/**
	 * Proses untuk insert penambahan user id baru  (administrast user id)
	 * 
	 * @author Ridhaal
	 * @since Maret 25, 2016 (4:56:17 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public void updateCounterUserId(User user) throws DataAccessException {
		update("updateCounterUserId", user);	
	}

	public void insertNewUserId(User user, User currentUser) throws DataAccessException {
		insert("insertNewUserId", user);	
	}
		
	public void insertNewUserIdAdmin(User user) throws DataAccessException {
		insert("insertNewUserIdAdmin", user);	
	}
	
	public void insertNewUserIdCab(User user) throws DataAccessException {
		insert("insertNewUserIdCab", user);	
	}
	
	public void insertNewUserIdMenu(User user) throws DataAccessException {
		insert("insertNewUserIdMenu", user);	
	}
	
	public String updateUserId(User user) throws DataAccessException{
		 update("updateUserId", user);
		 return "User id berhasil di perbaharui.";	
	}
	
	public void updatePasswordUser(User user) throws DataAccessException{
		 update("updatePasswordUser", user);
	}
	
	public List selectCabangBiiUA(String cab_bank,Integer jn_cab)throws DataAccessException{
		User user = new User();
		user.setCab_bank(cab_bank);
		user.setJn_cab(jn_cab);
		return query("selectCabangBiiUA", user);
	}
	
	public String selectNewLusIDUA() throws DataAccessException{
		return (String) querySingle("selectNewLusIDUA",null);
	}
	
	public String cekLoginName(String user_name) throws DataAccessException{
		return (String) querySingle("cekLoginName",user_name);
	}
	
	public String cekFullName(String full_name) throws DataAccessException{
		return (String) querySingle("cekFullName",full_name);
	}
	
	public String cekSpv(String lus_id, Integer jn_spv) throws DataAccessException{
		User user = new User();
		user.setLus_id(lus_id);
		user.setJn_spv(jn_spv);
		
		if (jn_spv == 1) 
			{return (String) querySingle("cekSpv1",user);}
		else{
			return (String) querySingle("cekSpv2",user);
		}
	}
	
	public List selectDaftarUserUA(String cab_bank, Integer jn_user, String lus_login_name )throws DataAccessException{
		User user = new User();
		user.setCab_bank(cab_bank);
		user.setJn_user(jn_user);
		user.setLus_login_name(lus_login_name);
		return query("selectDaftarUserUA", user);
	}
	
	public List selectDetUserUA(Integer jn_pil,String lus_id, String cab_bank, String lca_id)throws DataAccessException{
		User user = new User();
		user.setCab_bank(cab_bank);
		user.setLus_id(lus_id);
		user.setJn_pil(jn_pil);
		user.setLca_id(lca_id);
		if(lus_id==null)
			return query("selectCabangBiiUA", user);
		else
			return query("selectDetUserUA", user);
	}
	
	public void deleteMstSsave(User user) throws DataAccessException{
		delete("delete.LstMenuUserNew", user);
	}
	
	public List<Map> selectDataSusulan() {		
		return  query("selectDataSusulan", null);
	}
	
	public List select_spesifik_produk_rider_namesub(Integer kode, String lsdbs_name_sub) {
		Map map = new HashMap();
		map.put("kode", kode);
		map.put("lsdbs_name_sub", lsdbs_name_sub);
		return  query("select_spesifik_produk_rider_namesub",map);
	}
	
	public ArrayList selectMstSpajTemp(String spaj, Integer camp_id){
		HashMap map = new HashMap();
		map.put("spaj", spaj);
		map.put("camp_id", camp_id);
		return (ArrayList) query("selectMstSpajTemp", map);
	}
	
	public String selectUsedMstSpajTemp(String no_temp){
		return (String) querySingle("selectUsedMstSpajTemp", no_temp);
	}
	
	public Map selectAgenESPAJSimasPrima(String spaj) {
		return (HashMap) querySingle("selectAgenESPAJSimasPrima", spaj);
	}
	
	public Map selectAgenESPAJgadget(String spaj) {
		return (HashMap) querySingle("selectAgenESPAJgadget", spaj);
	}
	
	public Tertanggung selectttTemp2(String no_temp) {
		// TODO Auto-generated method stub
		return (Tertanggung) querySingle("selectttTemp2", no_temp);
	}
	
	public Double selectSumPremiTemp(String no_temp){
		return (Double) querySingle("selectSumPremiTemp", no_temp);
	}
	
	public Integer selectCountAreaBC(String lrb_id, String lcb_no){
		HashMap map = new HashMap();
		map.put("lrb_id", lrb_id);
		map.put("lcb_no", lcb_no);
		return (Integer) querySingle("selectCountAreaBC", map);
	}
	
	public Integer selectJnBankDetBisnis(Integer lsbs, Integer lsdbs) throws DataAccessException{
		Map map = new HashMap();
		map.put("lsbs", lsbs);
		map.put("lsdbs", lsdbs);
		return (Integer) querySingle("selectJnBankDetBisnis", map);
	}
	
	/*public void updateMstPowersaveCairTglCair(String reg_spaj , Date tgl_cair_aktual) throws DataAccessException{
		HashMap p = new HashMap();
		p.put("reg_spaj", reg_spaj);
		p.put("tgl_cair_aktual", tgl_cair_aktual);
		update("update.mst_powersave_cair.tgl_cair_aktual", p);
	}*/
	
	public void insertUrlSecureFile(Date input_date, String reg_spaj, String note,Integer VendorId, String KeyId, String LongURL, String URLEncrypt, String ShortUrl ) throws DataAccessException{
		Map map = new HashMap();
		map.put("input_date", input_date);
		map.put("reg_spaj", reg_spaj);
		map.put("note", note);
		map.put("VendorId", VendorId);
		map.put("KeyId", KeyId);
		map.put("LongURL", LongURL);	
		map.put("URLEncrypt", URLEncrypt);
		map.put("ShortUrl", ShortUrl);	
		insert("insert.mst_url_secure_file", map);
	}
	
	public List cekUrlSecureFile(String reg_spaj, String note) {
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("note", note);
		return  query("cekUrlSecureFile",map);
	}
	
	public String selectTglLhrPP(String spaj) throws DataAccessException{
		return (String) querySingle("selectTglLhrPP", spaj);
	}
	
	public Date selectTglTransToPrintPolis(String spaj) throws DataAccessException{
		return (Date)querySingle("selectTglTransToPrintPolis",spaj);
	}
	
	public String selectKycDesc(String spaj, Integer no_urut,Integer kyc_id , Integer kyc_pp ) throws DataAccessException{
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("no_urut", no_urut);
		map.put("kyc_id", kyc_id);
		map.put("kyc_pp", kyc_pp);
		return (String) querySingle("selectKycDesc", map);
	}
	
	public List<Map> selectCekSpajPromoUF( String beg_date_promo, String end_date_promo, String spaj , String jn_promo, Integer jn_spaj) throws DataAccessException{
		Map params = new HashMap();
		
		params.put("beg_date_promo",beg_date_promo);
		params.put("end_date_promo",end_date_promo);
		params.put("spaj",spaj);
		params.put("jn_promo",jn_promo);
		params.put("jn_spaj",jn_spaj);
		
		return query("selectCekSpajPromoUF", params);
	}
	
		
	public void insertPromoFreeSpaj(String reg_spaj_primary,Integer row_num, String reg_spaj_free, Integer process_type, Integer program_type) throws DataAccessException {
		Map m=new HashMap();
		m.put("reg_spaj_primary", reg_spaj_primary);
		m.put("row_num", row_num);
		m.put("reg_spaj_free", reg_spaj_free);
		m.put("process_type", process_type);
		m.put("program_type", program_type);
		
		insert("insert.PromoFreeSpaj",m);		
		
	}
	
	public List<Map> selectCekSpajPromo( String spajReff ,String spajFree, String jn_promo) throws DataAccessException{
		Map params = new HashMap();
		
		params.put("spajReff",spajReff);
		params.put("spajFree",spajFree);
		params.put("jn_promo",jn_promo);
		
		return query("selectCekSpajPromo", params);
	}
	
	
	public void updatePromoFreeSpaj(String spajReff ,String spajFree, Integer process_type) throws DataAccessException{
		Map params = new HashMap();		
		params.put("spajReff",spajReff);
		params.put("spajFree",spajFree);
		params.put("process_type",process_type);
		update("update.updatePromoFreeSpaj", params);
	}
	
	public List<Map> selectCountandvalidSpajPromo( String beg_date_promo, String end_date_promo, String wil_no , String jn_promo, Integer jn_cek) throws DataAccessException{
		Map params = new HashMap();
		
		params.put("beg_date_promo",beg_date_promo);
		params.put("end_date_promo",end_date_promo);
		params.put("wil_no",wil_no);
		params.put("jn_promo",jn_promo);
		params.put("jn_cek",jn_cek);
		
		return query("selectCountandvalidSpajPromo", params);
	}
	
	public void updateflagSpecialOffer(String spaj ,Integer flag_special_offer) throws DataAccessException{
		Map params = new HashMap();		
		params.put("spaj",spaj);
		params.put("flag_special_offer",flag_special_offer);
		update("update.updateflagSpecialOffer", params);
	}
	
	public List<Promo> selectListPromoChecklistFreeProd() throws DataAccessException {
		return query("selectListPromoChecklistFreeProd", null);
	}
	
	public Integer selectHitungApe(String reg_spaj) throws DataAccessException{
		return (Integer) querySingle("selectHitungApe", reg_spaj);
	}
	
	public List selectKanwil711(){
		return query("selectKanwil711",null);
	}
	
	public List selectProducFreeP1(){
		return query("selectProducFreeP1",null);
	}
	
	public List selectReportProductFreeSmileM(Map map) {
		return query("selectReportProductFreeSmileM", map);
	}
	
	public List selectProductDMTMDS(){
		return query("selectProductDMTMDS",null);
	}
	
	public List selectReportProductDMTMDS(Map map) {
		return query("selectReportProductDMTMDS", map);
	}
		
	public String selectSequenceClientID(){
		return (String) querySingle("selectSequenceClientID", null);
	}
	
	public List selectDetailKartuPasBSMIB(String no_kartu) throws DataAccessException {
		return query("selectDetailKartuPasBSMIB", no_kartu);
	}
	
	public String selectSeqNoSpaj(String lca_id){
		return (String) querySingle("selectSeqNoSpaj", lca_id);
	}
	
	/**
	 * @author Randy
	 * @param tanggal
	 * @return
	 * Cek tgl, apakah ada pada hari kerja atau tidak.
	 * Jika tidak maka tgl tersebut akan di kurang 1 hari,
	 * sampai tgl tersebut berada pada hari kerja (request Billy)
	 */
    public Date cekHariKerjaMin(Date tanggal){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(tanggal);
		
    	Date dt_tanggal = new Date();
    	String s_tgl = "";
    	Integer i = 0;
    	Integer j = 0;
    	Boolean libur = false;
    	
    	while(i==0){
    		s_tgl = df.format(cal.getTime());
			j = basDao.selectLibur(s_tgl);
			if(j > 0){
				libur = true;
			}else{
				libur = false;
			}
			
    		if((cal.get(Calendar.DAY_OF_WEEK)==1) || (cal.get(Calendar.DAY_OF_WEEK)==7) || libur==true){
				cal.add(Calendar.DATE, -1); 
    		}else{
    			i = 1;
    		}
    	}
    	
    	try{
    		dt_tanggal = df.parse( df.format(cal.getTime()) );
    	}catch (Exception e) {
    		logger.error("ERROR :", e);
		}
    	
    	return dt_tanggal;
    }
	
	public List<Map> selectMst_Crp_Result(String regspaj) throws DataAccessException{
		return query("selectMst_Crp_Result", regspaj);
	}
	
	public String insertMst_Crp_Result(String regspaj , Integer question_id , Integer answer_id ) throws DataAccessException{
		Map m=new HashMap();
		m.put("regspaj", regspaj);
		m.put("question_id", question_id);
		m.put("answer_id", answer_id);
		
		insert("insertMst_Crp_Result",m);
		
	    return "Data Questioner Risk Profile dengan No. SPAJ "+regspaj+" berhasil disimpan.";
	}
	
	public String updateMst_Crp_Result(String regspaj , Integer question_id , Integer answer_id) throws DataAccessException{
		Map m=new HashMap();
		m.put("regspaj", regspaj);
		m.put("question_id", question_id);
		m.put("answer_id", answer_id);
		 
		update("updateMst_Crp_Result", m);
		return "Data Questioner Risk Profile dengan No. SPAJ "+regspaj+" berhasil diperbarui.";	
	}
	
	public void updateMst_policy_CRPResult(String regspaj ,Integer mspocrpscore, String mspocrpresult) throws DataAccessException{
		Map param = new HashMap();
		param.put( "regspaj", regspaj );
		param.put( "mspocrpscore", mspocrpscore );
		param.put( "mspocrpresult", mspocrpresult );
		update("updateMst_policy_CRPResult", param);
	}
	
	public List<Map> selectMst_Crp_Result_ESPAJ(String regspaj) throws DataAccessException{
		return query("selectMst_Crp_Result_ESPAJ", regspaj);
	}
	
	public List selectSnowsDireksi(String jns) throws DataAccessException{
		Map p = new HashMap();
		p.put("jns", jns);		
		return query("selectSnowsDireksi", p);
	}
	
	public void insertMstCrpResultFromMstCrpTemp(String regspaj) throws DataAccessException{
		update("insertMstCrpResultFromMstCrpTemp", regspaj);
	}
		
	public List selectlistAMLCFT_Monitoring(String dariTanggal,String sampaiTanggal){
		Map param=new HashMap();
		param.put("dariTanggal", dariTanggal);
		param.put("sampaiTanggal", sampaiTanggal);
		return query("selectlistAMLCFT_Monitoring", param);
	}

	public String cekKodeCabang(String kode) {
		return (String) querySingle("cekKodeCabang", kode);
	}
	
	public String selectSertifikatTemp(String lca_id, String lwk_id, String lsbs_id){
		Map param=new HashMap();
		param.put("lca_id", lca_id);
		param.put("lwk_id", lwk_id);
		param.put("lsbs_id", lsbs_id);
		return (String) querySingle("selectSertifikatTemp", param);
	}
	
	public void insertMstSpajCrt(HashMap param){
		insert("insertMstSpajCrt",param);
	}
	
	public List  selectPlanSmileMedicalPlus(String spaj, int lsbs_id , String lsdbs_name_plan) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lsbs_id", new Integer(lsbs_id));
		params.put("lsdbs_name_plan", lsdbs_name_plan);
		return query("selectPlanSmileMedicalPlus", params);
	}
	
	
	public List<DropDown> selectDistribusiData() {
		return query("selectDistibusiData", null);
	}
	
	public List<DropDown> selectDistribusi2Data() {
		return query("selectDistibusi2Data", null);
	}
	
	public Map selectUploadSpajTempConf(int lsbs_id, int lsdbs_number){
		Map param=new HashMap();
		param.put("LSBS_ID", lsbs_id);
		param.put("LSDBS_NUMBER", lsdbs_number);
		return (HashMap) querySingle("selectUploadSpajTempConf", param);
	}
	
	public List<DropDown> selectDropDownDaftarPesertaSMP(String spaj) {		
		return query("selectDropDownDaftarPesertaSMP",spaj);
	}
	
	public int selectBillingCount(String spaj, int tahun) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		Integer hasil = (Integer) querySingle("selectBillingCount", params);
		if (hasil == null)
			return 0;
		else
			return hasil;
	}
	
	public List selectDetailBilling(String spaj, int tahun, int premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		return query("selectDetailBilling", params);
	}
	
	public Integer prosesKomisiErbePackageSystem(String reg_spaj, Integer tahun_ke, Integer premi_ke, Integer reproc) throws DataAccessException, SQLException{
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("result", null);
		map.put("tahun_ke", tahun_ke);
		map.put("premi_ke", premi_ke);
		map.put("reproc", reproc);
		Integer hasil = 1;
		List daftarKomisi = this.financeDao.selectKomisiAgenErbePackage(reg_spaj,tahun_ke, premi_ke);
		if(daftarKomisi.size()==0){
			 querySingle("procKomErbePackage", map);
			 daftarKomisi = this.financeDao.selectKomisiAgenErbePackage(reg_spaj,tahun_ke, premi_ke);
		}else{
			hasil=0;
		}

		return hasil;
	}
	
	public Integer select_PKP(String lca_id, String lwk_id, String lsrg_id)
	{
		Map map = new HashMap();
		map.put("lca_id", lca_id);
		map.put("lwk_id", lwk_id);
		map.put("lsrg_id", lsrg_id);			
		return (Integer) querySingle("select_PKP", map);
	}
	
	public Integer selectCekSpajNonPega(String reg_spaj) {
		return (Integer) querySingle("selectCekSpajNonPega", reg_spaj);
	}

	public List selectJumlahContract() //Chandra A - 20180413
	{
		return query("selectJumlahContract", null);
	}
	
	public List selectDataBelumProd() //Chandra A - 20180417
	{
		return query("selectDataBelumProd", null);
	}
	
	public int ultimateJobFixing(String reg_spaj) //Chandra A - 20180418
	{
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		return update("ultimateJobFixing", map);
	}
	
	public int retryProduction(String noid) //Patar Timotius - 20180418
	{
		Map map = new HashMap();
		map.put("noid", noid);
		return update("retryProduction", map);
	}
	
	

	public int retryPayment(String noid) //Patar Timotius - 20180418
	{
		Map map = new HashMap();
		map.put("noid", noid);
		return update("retryPayment", map);
	}
	
	
	
	public List selectDataJsonTemp() //Chandra A - 20180425
	{
		return query("selectDataJsonTemp", null);
	}
	
	public List selectFailSiap2UProd(){
		//Patar Timotius -- 20180910
		return query("selectFailSiap2UProd", null);
	}
	
	
	public List selectFailPayment(){
		//Patar Timotius -- 20180910
		return query("selectFailPayment", null);
	}
	
	
	public List selectDepositPremiumFailed(){
		//Patar Timotius -- 20180910
		return query("selectDepositPremiumFailed", null);
	}
	public List selectDaftarInvestasiTemp2(String no_temp){
		return query("selectDaftarInvestasiTemp2", no_temp);
	}
	
	public void updateMstBillingJurnalErbe(String spaj, String no_jm)throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("no_jm", no_jm);
		update("updateMstBillingJurnalErbe",param);
	}
	
	public void updateMstInvoiceJurnalErbe(String spaj, String no_jm, int premiKe )throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("no_jm", no_jm);
		param.put("premiKe", premiKe);
		update("updateMstInvoiceJurnalErbe",param);
	}
	
	//Mark Valentino 20181121 Kode Penutup
	public HashMap selectMstSpajTempAutoSales(String no_temp){		
		return (HashMap) querySingle("selectMstSpajTempAutoSales", no_temp);
	}
	
	//public String selectKodePenutup(String lca_id, String lwk_id, String lsrg_id, Integer lsbs_id, Integer lsdbs_number){
	public String selectKodePenutup(Integer lsbs_id, Integer lsdbs_number){
		Map param = new HashMap();
//		param.put("lca_id", lca_id);
//		param.put("lwk_id", lwk_id);
//		param.put("lsrg_id", lsrg_id);
		param.put("lsbs_id", lsbs_id);
		param.put("lsdbs_number", lsdbs_number);		
		return (String) querySingle("selectKodePenutup", param);
	}
	
	//helpdesk [133348] email validasi transaksi Direksi/ Dept Head
	public List selectMstPembayaranApproval(String ls_jenis) throws DataAccessException{
		Map p = new HashMap();
		p.put("ls_jenis", ls_jenis);		
		return query("selectMstPembayaranApproval", p);
	}
	
	//helpdesk [133348] email validasi transaksi Direksi/ Dept Head
	public List selectMstBatchPembayaran(String batch_no, int status) throws DataAccessException{
		Map p = new HashMap();
		p.put("batch_no", batch_no);
		p.put("status", status);
		return query("selectMstBatchPembayaran", p);
	}
	
	//helpdesk [133348] email validasi transaksi Direksi/ Dept Head 
	public void updateMstBatchPembayaran(String batch_no, String lus_id, String index, int status, String me_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("batch_no", batch_no);
		m.put("user", lus_id);
		m.put("date", new Date());
		m.put("index", index);
		m.put("status", status);
		m.put("me_id", me_id);
		update("updateMstBatchPembayaran", m);
	}

	//helpdesk []
	public List selectSpajBatchPembayaranByJns(String jns, String batch_no){
		List result = null;
		
		//helpdesk [148610] update body email approval Finance untuk jenis 1 dan 2, query ada di SRS, poinnya jadi sama dengan jenis 3 & 4
		//if(jns.trim().equals("3") || jns.trim().equals("4")){
			result = selectSpajBatchPembayaranJns3(batch_no);
		//} else if (jns.trim().equals("1") || jns.trim().equals("2")){
		//	result = selectSpajBatchPembayaran(batch_no);
		//}
		
		return result;
	}
	
	//helpdesk [133348] email validasi transaksi Direksi/ Dept Head
	private List selectSpajBatchPembayaran(String batch_no) throws DataAccessException{
		Map p = new HashMap();
		p.put("batch_no", batch_no);
		return query("selectSpajBatchPembayaran", p);
	}
	
	//helpdesk []
	private List selectSpajBatchPembayaranJns3(String batch_no) throws DataAccessException{
		Map p = new HashMap();
		p.put("batch_no", batch_no);
		return query("selectSpajBatchPembayaranJns3", p);
	}
	
	//helpdesk [133348] email validasi transaksi Direksi/ Dept Head 
	public void updateMstBatchPembayaranAfterEmail(String batch_no) throws DataAccessException{
		Map m = new HashMap();
		m.put("batch_no", batch_no);
		update("updateMstBatchPembayaranAfterEmail", m);
	}
	
	//helpdesk [133348] email validasi transaksi Direksi/ Dept Head
	public List selectMstBatchPembayaranApprover(String batch_no, String idx_aksep) throws DataAccessException{
		Map p = new HashMap();
		p.put("batch_no", batch_no);
		
		if(idx_aksep.equalsIgnoreCase("1"))
			return query("selectMstBatchPembayaranApprover1", p);
		else
			return query("selectMstBatchPembayaranApprover2", p);
	}
	
	//helpdesk [133348] email validasi transaksi Direksi/ Dept Head 
	public void updateMstPembayaranEmail(String batch_no, String me_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("batch_no", batch_no);
		m.put("me_id", me_id);
		update("updateMstPembayaranEmail", m);
	}
	
	//helpdesk [] 
	public void updateMstPembayaranApi(String batch_no) throws DataAccessException{
		Map m = new HashMap();
		m.put("batch_no", batch_no);
		update("updateMstPembayaranApi", m);
	}
	
	//helpdesk [] 
	public void updateMstPembayaranApiRejected(String batch_no) throws DataAccessException{
		Map m = new HashMap();
		m.put("batch_no", batch_no);
		update("updateMstPembayaranApiRejected", m);
	}

	//helpdesk [] 
	public List selectMstPembayaranApiIsUpdated(String batch_no, int payment_method, int flag_aktif) throws DataAccessException{
		Map p = new HashMap();
		p.put("batch_no", batch_no);
		p.put("payment_method", payment_method);
		p.put("flag_aktif", flag_aktif);
		
		return query("selectMstPembayaranApiIsUpdated", p);
	}
	
	//elions.bac.select_agen_reff_bii
			public String select_agen_reff_bii(String lrb_id){
				return (String) querySingle("select_agen_reff_bii", lrb_id);
			}
			
			
			//elions.bac.select_counter_spaj_temp
			public String select_counter_spaj_temp(){
				return (String) querySingle("select_counter_spaj_temp", null);
			}
			
			//elions.bac.select_det_bsim_syariah
			public Map select_det_bsim_syariah(String reg_spaj){
				return (Map) querySingle("select_det_bsim_syariah", reg_spaj);
			}
			
			
			
			//elions.bac.select_bsim_no_va_syariah
			public String select_bsim_no_va_syariah(){
				return (String) querySingle("select_bsim_no_va_syariah", null);
			}
			
			public Map select_det_prod(String lsbs_id, int lsdbs_number){
				Map m = new HashMap();
				m.put("lsbs_id",lsbs_id);
				m.put("lsdbs_number", lsdbs_number);
				return (Map) querySingle("select_det_prod", m);
			}
			
			
			//elions.bac.select_det_agen_by_mclid
			
			public Map select_det_agen_by_mclid(String mclId){
				return (Map) querySingle("select_det_agen_by_mclid", mclId);
			}
			public Map select_det_reff_by_lrb_id(String mclId){
				return (Map) querySingle("select_det_reff_by_lrb_id", mclId);
			}
			public Map select_by_lbn(String lbn_id){
				return (Map) querySingle("select_by_lbn", lbn_id);
			}
			
			public void updatePas(Pas pas) throws DataAccessException{
				update("updatePas",pas);
			}
	
	//helpdesk [137730] tambahin pilihan campaign
	public List selectMstCampaignProduct(String lsbs_id, String lsdbs_number){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now_date = sdf.format(new Date());
		HashMap m = new HashMap();
		m.put("lsbs_id", lsbs_id);
		m.put("lsdbs_number", lsdbs_number);
		m.put("now_date", now_date);
		return query("selectMstCampaignProduct", m);
	}
	
	
	//Covid flag patar timotius
	public List selectFaceToFaceCategory(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now_date = sdf.format(new Date());
		HashMap m = new HashMap();
		
		return query("selectFaceToFaceCategory", m);
	}
	
	
	
	//helpdesk [143180] autosales tambah campaign_id
	public List selectMstCampaignProductAutoSales(String campaign_id, String lsbs_id, String lsdbs_number){
		HashMap m = new HashMap();
		m.put("campaign_id", campaign_id);
		m.put("lsbs_id", lsbs_id);
		m.put("lsdbs_number", lsdbs_number);
		return query("selectMstCampaignProductAutoSales", m);
	}
	
	//ofaclist
	public List selectOfacList(String nopol, String spaj, String noRow, String noPage, String sertifikat, String possible, String matched){
		HashMap m = new HashMap();
		m.put("nopol",nopol);
		m.put("spaj", spaj);
		m.put("noRow", noRow);
		m.put("noPage", noPage);
		m.put("sertifikat", sertifikat);
		m.put("possible", possible);
		m.put("matched", matched);
		return query("selectOfacList", m);
	}

	public List<MstOfacScreeningResult> selectMstoFacresultScreening(String spaj,String type,String name){
		HashMap m = new HashMap();
		m.put("spaj", spaj);
		m.put("type", type);
		m.put("name", name);
		return query("selectmstofacresultscreening", m);
	}
	//IGA UPDATE RDS SCREENING
	public List selectrdsscreening(String reg_spaj){
		HashMap m = new HashMap();
		m.put("reg_spaj",reg_spaj);
		return query("selectrdsscreening", m);
	}
	//IGA UPDATE RDS SCREENING
	public List selecthistoryrds(String reg_spaj){
		HashMap m = new HashMap();
		m.put("reg_spaj",reg_spaj);
		return query("selecthistoryrds", m);
	}
	// Flag Vip Nana
	public List selectParamMedisDesc(){
	   HashMap m = new HashMap();
	   return query("selectParamDescMedisListConfig", m);
	}
	
	// Beg Date Coi
	public Date selectBegDateCoiFromConfig() throws DataAccessException{
		return (Date) querySingle("selectBegDateCoiFromConfig", null);
	}
	
	// simpol
	public Double selectSumPremiPokokAndBerkalaTemp(String no_temp){
		return (Double) querySingle("selectSumPremiPokokAndBerkalaTemp", no_temp);
	}
	
	// simpol
	public Double selectPremiTunggalTemp(String no_temp){
		return (Double) querySingle("selectPremiTunggalTemp", no_temp);
	}
	
	// Flag Full Autosales Nana
	public List selectParamFullAutoSalesConfig(){
		HashMap m = new HashMap();
		return query("selectParamFullAutoSalesConfig", m);
	}
		
	// Cek flag Full Autosales 
	public String selectFullAutoSalesFromMstPolicy(String reg_spaj){
		return (String) querySingle("selectFullAutoSalesFromMstPolicy", reg_spaj);
	}
	
	//helpdesk [149354] Project SMS Polis Retur
	private List selectForSmsPolisReturn(String no_polis) throws DataAccessException{
		Map p = new HashMap();
		p.put("mspo_policy_no", no_polis);
		return query("selectForSmsPolisReturn", p);
	}
	
	//helpdesk [149354] Project SMS Polis Retur
	public String prosesSMSGlobal(String spaj, String no_polis, String no_hp, String pesan, int jenis, int ljs_id, Boolean is_default){
		String result = "";
		
		try{			
			com.ekalife.elions.model.sms.Smsserver_out sms_out = new com.ekalife.elions.model.sms.Smsserver_out();
			sms_out.setReg_spaj(spaj);
			sms_out.setMspo_policy_no(no_polis);
			sms_out.setJenis(jenis);
			sms_out.setLjs_id(ljs_id);
			sms_out.setRecipient(no_hp);
			sms_out.setText(pesan);
			sms_out.setLus_id(0); 
			
			if(!is_default){
				sms_out.setType("O");
	    		sms_out.setOriginator("NOT API"); //marker normal rute
	    		sms_out.setErrors(0);
	    		sms_out.setEncoding("7");
	    		sms_out.setStatus_report(1);
	    		sms_out.setFlash_sms(0);
	    		sms_out.setSrc_port(-1);
	    		sms_out.setDst_port(-1);
	    		sms_out.setGateway_id(sms_out.getRecipient());
			}

			basDao.insertSmsServerOutWithGateway(sms_out, is_default);
		}catch (Exception ex) {
			result = String.format("\\nPOLIS : [%s], SPAJ : [%s], PHONE : [%s]. ERROR : [%s]", no_polis, spaj, no_hp, ex.getLocalizedMessage());
		}
		
		return result;
	}
	
	//helpdesk [149354] Project SMS Polis Retur
	private String prosesSMSPolisReturByApi(String spaj, String no_polis, String no_hp){
		String result = "", 
				pesan = String.format("Nasabah Yth, Kami telah mengirimkan Polis Anda No. %s namun retur. Mohon hub. CS 021-50609999.", no_polis),
				is_testing = "0";
		String hit_url = String.format("http://paymentcc.sinarmasmsiglife.co.id:%s", (is_testing.equals("1") ? "8989/smstest/api/send" : "8999/sms/api/send"));
		int ljs_id = 49;

		try{
			HttpClient httpClient = new HttpClient();
			JSONObject inputJsonObj = new JSONObject();
			
			inputJsonObj.put("no_hp", no_hp);
			inputJsonObj.put("sms", pesan);
			inputJsonObj.put("no_polis", no_polis);
			inputJsonObj.put("reg_spaj", spaj);
			inputJsonObj.put("ljs_id", ljs_id);
			
			PostMethod  method = new PostMethod(hit_url);
			StringRequestEntity requestEntity = new StringRequestEntity(inputJsonObj.toString(), "application/json", null);
			method.setRequestEntity(requestEntity);
			
			httpClient.executeMethod(method);
			
			if(method.getStatusCode() != HttpStatus.SC_OK){
				//failover hit normal rute
				prosesSMSGlobal(spaj, no_polis, no_hp, pesan, 13, ljs_id, false);
			}
		}catch (Exception ex) {
			result = String.format("\\nPOLIS : [%s], SPAJ : [%s], PHONE : [%s]. ERROR : [%s]", no_polis, spaj, no_hp, ex.getLocalizedMessage());
		}
		
		return result;
	}
	
	//helpdesk [149354] Project SMS Polis Retur
	public String prosesDataPolisRetur(User currentUser, String no_polis, String alasan){
		String kumpul_error = "",
				result_sms = "",
				spaj = "", 
				no_hp = "", 
				no_hp2 = "";
		
		List<Map> spaj_data = selectForSmsPolisReturn(no_polis);
		
		if(spaj_data.size() > 0){
			Map detail_data = (Map)spaj_data.get(0);
			spaj = (String)detail_data.get("REG_SPAJ");
			no_hp = (String)detail_data.get("NO_HP");
			no_hp2 = (String)detail_data.get("NO_HP2");

			if(no_hp == null){
				if(no_hp2 == null){
					kumpul_error += String.format("\\nPOLIS: [%s]. ERROR : [tidak ada no telepon valid]", no_polis);
				}else{
					no_hp = no_hp2;
				}
			}

			if(no_hp != null){
				result_sms = prosesSMSPolisReturByApi(spaj, no_polis, no_hp);
				if(result_sms.length() < 1){
					alasan = String.format("sms retur dikirim - %s", alasan);
					uwDao.insertMstPositionSpaj(currentUser.getLus_id(), alasan, spaj, 0);
				}else{
					kumpul_error += result_sms;
				}
			}
		}else{
			kumpul_error += String.format("\\nPOLIS: [%s]. ERROR : [status belum inforce]", no_polis);
		}
		
		return kumpul_error;
	}
	
	//Ticket [SD-572] tambah validasi agent expired pada saat submit spaj
	public List selectDaftarAgentBlacklist() throws DataAccessException{
		Map p = new HashMap();
		
		return query("selectDaftarAgentBlacklist", p);
	}
	
	//helpdesk [148055]
	public void updateMstAdditionalDescTemp(String no_temp, String reg_spaj) {
		Map map = new HashMap();
		map.put("no_temp", no_temp);		
		map.put("spaj", reg_spaj);
		update("updateMstAdditionalDescTemp", map);		
	}
}	
