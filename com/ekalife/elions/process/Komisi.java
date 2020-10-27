package com.ekalife.elions.process;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;

import com.ekalife.elions.model.AgentTax;
import com.ekalife.elions.model.Billing;
import com.ekalife.elions.model.CommRider;
import com.ekalife.elions.model.Commission;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Rekruter;
import com.ekalife.elions.model.SpajBill;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.TopUp;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentDao;

public class Komisi extends ParentDao {
	protected final Log logger = LogFactory.getLog( getClass() );
	
	private enum tingkatIndividu2007{RM, SBM, BM, UM, ME}; //lihat LST_LEVEL_AGENT, kecuali RM (LSLE_ID=0)

	/**
	 * Fungsi untuk menarik rate cashback untuk produk2 save
	 * 
	 * @author Yusuf
	 * @since Sep 8, 2008 (8:03:03 PM)
	 * @return
	 * @throws ParseException 
	 */
	/*public double getCashBackPowersave(String reg_spaj) throws ParseException {
		
		//hasil
		double cashBack = 0;
		
		//variabel2 yang diperlukan
		Integer referal = uwDao.selectJenisPenutupBII(reg_spaj);
		if(referal == null) referal = -1;
		int jenisReferal = referal;
		Date tglProd = uwDao.selectProductionDate(reg_spaj, 1, 1);
		Date tglBayar = uwDao.selectTanggalBayar(reg_spaj);
		List detBisnis = uwDao.selectDetailBisnis(reg_spaj);
		String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
		String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
		int mgi = uwDao.selectMasaGaransiInvestasi(reg_spaj);
		int mti = uwDao.selectMGIStableLink(1, 1, reg_spaj);
		String lku_id = "";
		double premi = 0.;
		
		//tanggal2 batas
		Date d20080801 = completeDateFormatStripes.parse("01-08-2008 00:00");
		Date d20080905 = completeDateFormatStripes.parse("05-09-2008 00:00");
		Date d20080815 = completeDateFormatStripes.parse("15-08-2008 00:00");
		
		//angka2 batas
		BigDecimal seratusJuta 		= new BigDecimal("100000000");
		BigDecimal satuMilyar 		= new BigDecimal("1000000000");
		BigDecimal limaRatusRibu 	= new BigDecimal("500000");
		BigDecimal limaPuluhRibu 	= new BigDecimal("50000");
		
		//Yusuf - 01/08/2008 - per tgl bayar agustus, Untuk powersave bank sinarmas, 
		//ada % cashback dan diinsert dengan mcr_flag = 4, JENIS REFERRAL YG OTHERS GAK DAPET
		if(jenisReferal != 8 && tglBayar.compareTo(d20080801) >= 0) {

			//SIMAS PRIMA dan SIMAS PRIMA BULANAN
			if((lsbs.equals("142") && lsdbs.equals("002")) || (lsbs.equals("158") && lsdbs.equals("006"))) {
				//UNTUK RUPIAH
				if(lku_id.equals("01")) {
					
					cashBack = 0;
					
					if(premi >= seratusJuta.doubleValue()) {
						if(premi < satuMilyar.doubleValue()) {
							if(mgi == 3) cashBack = 0.1875;	//0.75/4;
							else if(mgi == 6) cashBack = 0.5;
							else if(mgi == 12) cashBack = 1;
						}else {
							if(mgi == 3) cashBack = 0.25;
							else if(mgi == 6) cashBack = 0.625;	//1.25/2;
							else if(mgi == 12) cashBack = 1.25;
						}
					}
					
					//(yusuf - 4 sept 08) per tanggal 5 september, untuk rupiah, ada perubahan cashback
					if(tglBayar.compareTo(d20080905) >= 0) {
						if(premi >= seratusJuta.doubleValue() && premi < satuMilyar.doubleValue()) {
							if(mgi == 3) cashBack = 0.25; //1 p.a.
							else if(mgi == 6) cashBack = 0.625; //1.25 p.a.
							else if (mgi == 12) cashBack = 1.25;//1.25 p.a.
						}else if(premi >= satuMilyar.doubleValue()) {
							if(mgi == 3) cashBack = 0.3125; //1.25 p.a.
							else if(mgi == 6) cashBack = 0.75; //1.5 p.a.
							else if (mgi == 12) cashBack = 1.5;//1.5 p.a.
						}
					}

				//UNTUK DOLLAR, BERLAKUNYA MULAI 15 AGUSTUS 2008
				}else if(lku_id.equals("02")) {
					if(tglBayar.compareTo(d20080815) >= 0) {
						
						if(premi >= limaPuluhRibu.doubleValue()) {
							if(premi < limaRatusRibu.doubleValue()) {
								if(mgi == 3) cashBack = 0.125;
								else if(mgi == 6) cashBack = 0.375;
								else if(mgi == 12) cashBack = 0.75;
							}else{
								if(mgi == 3) cashBack = 0.1875;	//0.75/4;
								else if(mgi == 6) cashBack = 0.5;
								else if(mgi == 12) cashBack = 1;
							}
						}
					}
				}
			//SIMAS STABIL LINK
			}else if(lsbs.equals("164") && lsdbs.equals("002")) {
				//UNTUK RUPIAH
				if(lku_id.equals("01") && premi >= limaRatusRibu.doubleValue()) {
					
					cashBack = 0;
					
					if(mti == 3) cashBack = 0.1875; //0.75 p.a.
					else if(mti == 6) cashBack = 0.5; //1 p.a.
					else if (mti == 12) cashBack = 1;//1 p.a.

					//(yusuf - 4 sept 08) per tanggal 5 september, untuk rupiah, ada perubahan cashback
					if(tglBayar.compareTo(d20080905) >= 0) {

						if(premi >= seratusJuta.doubleValue() && premi < satuMilyar.doubleValue()) {
							if(mti == 3) cashBack = 0.25; //1 p.a.
							else if(mti == 6) cashBack = 0.625; //1.25 p.a.
							else if (mti == 12) cashBack = 1.25;//1.25 p.a.
						}else if(premi >= satuMilyar.doubleValue()) {
							if(mti == 3) cashBack = 0.3125; //1.25 p.a.
							else if(mti == 6) cashBack = 0.75; //1.5 p.a.
							else if (mti == 12) cashBack = 1.5;//1.5 p.a.
						}
					}

				//UNTUK DOLLAR
				}else if(lku_id.equals("02") && premi >= limaPuluhRibu.doubleValue()) {
					if(mti == 3) cashBack = 0.25; //0.5 p.a.
					else if(mti == 6) cashBack = 0.375; //0.75 p.a.
					else if (mti == 12) cashBack = 0.75;//0.75 p.a.
				}				
			}
		}
		
		return cashBack;
	}*/
	
	public boolean isEkalink88Plus(String spaj, boolean isTopup) {
		//ekjalink 88 plus, OR hanya 70 %
		List detail = uwDao.selectDetailBisnis(spaj);
		Map det = (HashMap) detail.get(0);
		if(!detail.isEmpty()) {
			String lsbs = (String) det.get("BISNIS");
			String lsdbs = (String) det.get("DETBISNIS");
			if((lsbs.equals("162") && lsdbs.equals("007")) || (lsbs.equals("162") && lsdbs.equals("008"))){
				//hanya yang sekaligus atau yang top up
				if((lsbs.equals("162") && lsdbs.equals("007")) || isTopup){
					return true;							
				}
			}
		}
		return false;
	}
	
	//YUSUF (16/01/2008) - UNTUK PRODUK2 SINGLE (IM No. 132/IM-DIR/XII/2007), KOMISI OVERRIDING, HANYA DAPAT 35% NYA!
	//YUSUF (23/03/2009) - KETENTUAN DIATAS HANYA BERLAKU S/D 31 MAR 2009
	private Double cekLinkSingle(String spaj, Double ldec_komisi, Integer lev_kom, boolean isTopup) {
		
		Date prodDate = uwDao.selectProductionDate(spaj, 1, 1);
		
		Date batas = null;
		try { batas = defaultDateFormat.parse("01/04/2009");
		} catch (ParseException e) {logger.error("ERROR :", e);}
		
		if(lev_kom.intValue() != 4) {
			if(isLinkSingle(spaj, isTopup) && prodDate.before(batas)) {
				ldec_komisi = ldec_komisi * 0.35;
			}else {
				if(isEkalink88Plus(spaj, isTopup)) {
					ldec_komisi = ldec_komisi * 0.7;
				}
			}
		}
		return ldec_komisi;
	}
	
	public boolean isLinkSingle(String spaj, boolean isTopup) {
		List detail = uwDao.selectDetailBisnis(spaj);
		Map det = (HashMap) detail.get(0);
		if(!detail.isEmpty()) {
			String lsbs = (String) det.get("BISNIS");
			String lsdbs = (String) det.get("DETBISNIS");
			if(!isTopup) { //apabila bukan topup, hanya yang SINGLE dari produk2 ini: ekalink 88, ekalink fam, ekalink fam syariah, ekalink 80+, ekalink 80+ syariah
				if(		(lsbs.equals("116") && lsdbs.equals("001")) || (lsbs.equals("116") && lsdbs.equals("003")) ||
						(lsbs.equals("118") && lsdbs.equals("001")) || (lsbs.equals("118") && lsdbs.equals("003")) || 
						(lsbs.equals("153") && lsdbs.equals("001")) || (lsbs.equals("153") && lsdbs.equals("003")) || 
						(lsbs.equals("159") && lsdbs.equals("001")) || 
						(lsbs.equals("160") && lsdbs.equals("001")) || 
						(lsbs.equals("162") && lsdbs.equals("005"))) {//|| (lsbs.equals("162") && lsdbs.equals("007"))) {
					return true;
				}
			}else { //apabila topup, SINGLE dan REGULER dari produk2 ini: ekalink 88, ekalink fam, ekalink fam syariah, ekalink 80+, ekalink 80+ syariah, Smile link 100 , Smile link 100 syariah
				if(		(lsbs.equals("116") && lsdbs.equals("001")) || (lsbs.equals("116") && lsdbs.equals("002")) ||
						(lsbs.equals("116") && lsdbs.equals("003")) || (lsbs.equals("116") && lsdbs.equals("004")) ||
						(lsbs.equals("118") && lsdbs.equals("001")) || (lsbs.equals("118") && lsdbs.equals("002")) ||
						(lsbs.equals("118") && lsdbs.equals("003")) || (lsbs.equals("118") && lsdbs.equals("004")) ||
						(lsbs.equals("153") && lsdbs.equals("001")) || (lsbs.equals("153") && lsdbs.equals("002")) ||
						(lsbs.equals("153") && lsdbs.equals("003")) || (lsbs.equals("153") && lsdbs.equals("004")) ||
						(lsbs.equals("159") && lsdbs.equals("001")) || (lsbs.equals("159") && lsdbs.equals("002")) ||
						(lsbs.equals("159") && lsdbs.equals("003")) || (lsbs.equals("160") && lsdbs.equals("001")) || 
						(lsbs.equals("160") && lsdbs.equals("002")) || (lsbs.equals("162") && lsdbs.equals("005")) || 
						(lsbs.equals("162") && lsdbs.equals("006")) ||
						(lsbs.equals("217")) || (lsbs.equals("218")) ) {// ||
						//(lsbs.equals("162") && lsdbs.equals("007")) || (lsbs.equals("162") && lsdbs.equals("008"))) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Fungsi untuk proses komisi rider
	 * 
	 * @author Yusuf 
	 * @since 29/04/08
	 * 
	 * @throws Exception
	 */
	private void prosesKomisiRider(Commission komisi, int i, double persenOR) throws Exception{
		//mulai 1 may 2008
		long selisih = FormatDate.dateDifference(defaultDateFormat.parse("01/05/2008"), commonDao.selectSysdate(), false); 
		if(selisih >= 0) {
			Map pribadi = this.uwDao.selectInfoPribadi(komisi.getReg_spaj());
			String ls_region = (String) pribadi.get("REGION");
			String lca_id = ls_region.substring(0,2);
			
			List<Map> daftarRider = uwDao.selectDetBillingRider(komisi.getReg_spaj(), 1, 1);
			
			for(Map rider : daftarRider) {
				if("183,189,193,195,201,204".indexOf(komisi.getBisnis_id().toString())>-1 ){
					break;
				}
				int lsbs_id = ((BigDecimal) rider.get("LSBS_ID")).intValue();
				int lsdbs_number = ((BigDecimal) rider.get("LSDBS_NUMBER")).intValue();
				int det_ke = ((BigDecimal) rider.get("MSDB_DET_KE")).intValue();
				double premiRider = ((BigDecimal) rider.get("PREMI")).doubleValue();
				if(products.unitLink(komisi.getBisnis_id().toString()) && !products.stableLink(uwDao.selectBusinessId(komisi.getReg_spaj()))){
//					premiRider = uwDao.selectBiayaUlinkRider(komisi.getReg_spaj(), Integer.toString(lsbs_id), Integer.toString(lsdbs_number));
					//REQ HIMMIA : UNTUK RIDER PADA PLAN UTAMA LINK, TIDAK PERLU INSERT KE MST_COMM_RIDER
					premiRider = 0.;
				}
				
				//khusus rider 801 dan lsdbs >= 2
				
				if((lsbs_id == 801 && lsdbs_number >= 2) || (lsbs_id == 813 && lsdbs_number >=4) || (lsbs_id == 813 && lsdbs_number !=8)|| (lsbs_id == 818 && lsdbs_number >=3)|| (lsbs_id == 819 && (lsdbs_number >=281 && lsdbs_number <= 430)) || (lsbs_id == 820 || lsbs_id == 823 || lsbs_id == 825 || lsbs_id == 826)) {
					CommRider cr = new CommRider();
					
					premiRider = premiRider * komisi.getNilai_kurs();
					
					if(i == 0) { //komisi penutup
						if(lsbs_id == 820 || lsbs_id == 823 || lsbs_id == 825){
							cr.setKomisi(0.15 * premiRider);
						}else {
							cr.setKomisi(0.1 * premiRider);
						}
					}else { //komisi OR
						if(!lca_id.equals("42")) cr.setKomisi((persenOR/100) * 0.1 * premiRider);
						else cr.setKomisi(0.);
					}
					
					Date sysdate = commonDao.selectSysdateTruncated(0);
					cr.setPajak(f_load_tax(cr.getKomisi(), sysdate, komisi.getAgent_id()));
					//cr.setPajak(hitungPajakKomisi(cr.getKomisi(), sysdate, komisi.getAgent_id()));

					//pembulatan
					cr.setKomisi(FormatNumber.round(cr.getKomisi(), 0));
					cr.setPajak(FormatNumber.rounding(cr.getPajak(), true, 25));
					
					//apabila > 0
					if(cr.getKomisi().doubleValue() - cr.getPajak().doubleValue() > 0) {
						//apabila masih aktif dan dapat komisi
						if(komisi.getSts_aktif().intValue() == 1 && komisi.getFlag_komisi().intValue() == 1) {
							//cek degradasi agen - DISABLED, u/ new business tidak perlu
							//if(uwDao.selectCekDegradasiAgen(komisi.getAgent_id(), komisi.getTgl_kom())) {
								//
								cr.setReg_spaj(komisi.getReg_spaj());
								cr.setMsbi_tahun_ke(komisi.getMsbi_tahun_ke());
								cr.setMsbi_premi_ke(komisi.getMsbi_premi_ke());
								cr.setMsdb_det_ke(det_ke);
								cr.setLsbs_id(lsbs_id);
								cr.setLsdbs_number(lsdbs_number);
								cr.setLevel_id(komisi.getLev_kom());
								cr.setFlag_reward(0);
								cr.setMsag_id(komisi.getAgent_id());
								cr.setFlag_bayar(0);
								cr.setTgl_bayar(null);
								cr.setNilai_kurs(1);
								//tarik data bank account agen
								Map info = uwDao.selectInfoAgenRider(komisi.getAgent_id());
								cr.setNama(((String) info.get("NAMA")).trim());
								cr.setNo_account((String) info.get("NO_TAB"));
								cr.setLbn_id(((BigDecimal) info.get("LBN_ID")).intValue());							
								//insert
								//Jika tidak ada overriding, masukkan kode rider yg tidak ada overriding tersebut.
								if(lsbs_id == 820 || lsbs_id == 823){
									if(cr.getLevel_id()==4){
										uwDao.insertMstCommRider(cr);
									}
								}else{
									uwDao.insertMstCommRider(cr);
								}
							//}
						}
					}
					
				}
			}
		}
	}
	
	public void prosesKomisiBancAss(String spaj, User currentUser, BindException errors) throws Exception{ //of_comm_bancass()
		if(logger.isDebugEnabled())logger.debug("PROSES: prosesKomisiBancAss (of_comm_bancass)");
		StringBuffer emailMessage = new StringBuffer();
		emailMessage.append("[SPAJ  ] " + spaj + "\n");
		NumberFormat nf = NumberFormat.getInstance();
		List detail = uwDao.selectDetailBisnis(spaj);
		Map det = (HashMap) detail.get(0);
		if(!detail.isEmpty()) {
			emailMessage.append(
					"[PRODUK] (" + det.get("BISNIS") + ") " + det.get("LSBS_NAME") + " - " + 
					"(" + det.get("DETBISNIS") + ") " + det.get("LSDBS_NAME") + "\n");
		}
		
		String businessId = FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 2);
		Class.forName("produk_asuransi.n_prod_"+businessId).newInstance();
		
		int flag_telemarket = uwDao.selectgetFlagTelemarketing(spaj);
		
		double ldec_kurs; 
		Map pribadi = this.uwDao.selectInfoPribadi(spaj);
		
		List lds_kom = this.uwDao.selectViewKomisiAsli(spaj);
		if(!lds_kom.isEmpty()) {
			Commission kms = (Commission) lds_kom.get(0);
			if(kms.getKurs_id().equals("02")){
				ldec_kurs = this.uwDao.selectLastCurrency(spaj, 1, 1);
				if(ldec_kurs<0){
					errors.reject("payment.noLastCurrency");
					return;
				}
			}else ldec_kurs = 1;
			
			double ldec_premi = 0;
			Double ldec_komisi = null;

			for(int i=0; i<lds_kom.size(); i++) {
				//awal for
				Commission komisi = (Commission) lds_kom.get(i);
				String lsbs = FormatString.rpad("0", komisi.getBisnis_id().toString(), 3);
				komisi.setFlag_mess(Boolean.TRUE);
				if("183,189,193,195,201,204".indexOf(komisi.getBisnis_id().toString()) >-1){
					komisi.setPremi(bacDao.selectTotalKomisiEkaSehat(spaj));
				}
				if(i==0) ldec_premi = FormatNumber.round(komisi.getPremi() * ldec_kurs, 2);
				int li_cb_id = komisi.getCb_id();
				int li_cb_id_asli = komisi.getCb_asli();
				if(!products.progressiveLink(komisi.getBisnis_id().toString()) && !products.stableSavePremiBulanan(komisi.getBisnis_id().toString()) && !products.healthProductStandAlone(komisi.getBisnis_id().toString())){
					if( li_cb_id==1 || li_cb_id==2 || li_cb_id==6 ) komisi.setCb_id(3);
				}
				komisi.setCb_id(new Integer(li_cb_id));
				if(lsbs.equals("064")) komisi.setBisnis_id(56);
				else if(lsbs.equals("067")) komisi.setBisnis_id(54);
				komisi.setLsco_jenis(new Integer(2)); //Commission FA (bagian dr bancass)
				
				getLamaTanggung(komisi);
				
				if("079, 091, 092".indexOf(lsbs)>-1 && komisi.getCb_id()==0 && i==0) {
					ldec_premi = hitungPremiTahunan(spaj, errors);
				}
				
				if(f_get_komisi(komisi, errors, 1, 1, komisi.getKurs_id(), komisi.getPremi(), 4)==true){
					Double jmlKom = komisi.getKomisi()==null? 0 : komisi.getKomisi();
					if(jmlKom==0){
//						if(!(komisi.getLev_kom()==4 && lsbs.equals("073"))){
//							continue;
//						}
					}

					if(pribadi.get("PRIBADI").toString().equals("1")){
						komisi.setKomisi(new Double(0));
						ldec_komisi = new Double(0);
					}else{
						if(li_cb_id_asli==3) ldec_komisi = new Double(ldec_premi * (komisi.getKomisi()+komisi.getBonus()) / 100);
						else ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
					}

					if(ldec_komisi==0 && !pribadi.get("PRIBADI").toString().equals("1")) continue;

					//kalau masih aktif
					if(komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
						
						String lsdbs = (String) det.get("DETBISNIS");
						
						//TAMBAHAN OLEH YUSUF UNTUK PRODUK2 SINGLE (IM No. 132/IM-DIR/XII/2007) - 16/01/2008
						ldec_komisi = cekLinkSingle(spaj, ldec_komisi, komisi.getLev_kom(), false);
						
						komisi.setCo_id(sequenceMst_commission(11));
						komisi.setTax(new Double(ldec_komisi * komisi.getTax() / 100));
						Date sysdate = commonDao.selectSysdateTruncated(0);
						if(komisi.getTax() > 0){
							komisi.setTax(f_load_tax(ldec_komisi, sysdate, komisi.getAgent_id()));
							//komisi.setTax(hitungPajakKomisi(ldec_komisi, sysdate, komisi.getAgent_id()));
						}
						
						double ad_sisa;
						ad_sisa = komisi.getTax()%25;
						if(ad_sisa!=0){
							
						}
						komisi.setTax(FormatNumber.rounding(komisi.getTax() , true, 25));
						ldec_komisi  = FormatNumber.round(ldec_komisi ,  0);
						if(komisi.getTax()==null) komisi.setTax(0.0);
						komisi.setKomisi(ldec_komisi);
						komisi.setNilai_kurs(ldec_kurs);
						komisi.setLus_id(currentUser.getLus_id());
						komisi.setMsbi_tahun_ke(1);
						komisi.setMsbi_premi_ke(1);
						if(komisi.getKomisi()>0) {
							this.uwDao.insertMst_commission(komisi, sysdate);
							emailMessage.append("[KOMISI] " + komisi.getLev_kom() + " : " + komisi.getAgent_id() + " : " + 
									nf.format(komisi.getKomisi()) + "\n");
						}
						if(i==0){
							Map temp = this.uwDao.selectBillingRemain(komisi.getReg_spaj());
							double ldec_sisa = Double.parseDouble(temp.get("MSBI_REMAIN").toString());
							int li_flag = Integer.parseInt(temp.get("MSBI_FLAG_SISA").toString());
							if(li_flag==2){
								emailMessage.append("[DEDUCT] " + komisi.getCo_id() + " : " + 
										nf.format(ldec_sisa) + "\n");
								this.uwDao.insertMst_deduct(komisi.getCo_id(), ldec_sisa, currentUser.getLus_id(), 1, 3, "PREMI KURANG BAYAR");
							}
						}
					}
					
					if( i == 0 ) ldec_premi = ldec_komisi;
					
					if(flag_telemarket==1){
						komisi.setKomisi(ldec_komisi/0.5);
					}
				}
				//end for

				/** UNTUK DEBUGGING SAJA */
//				email.send(
//						new String[] {props.getProperty("admin.yusuf")},null, 
//						"Proses Komisi Bancassurance [" + spaj + "]", emailMessage.toString(), currentUser);
				
			} 
		}
			
	}
	
	public void prosesKomisiMallAssuranceSystem(String spaj, User currentUser, BindException errors, String lca_id) throws Exception{
		if(logger.isDebugEnabled())logger.debug("PROSES: prosesKomisiMallAssuranceSystem");
		StringBuffer emailMessage = new StringBuffer();
		emailMessage.append("[SPAJ  ] " + spaj + "\n");
		NumberFormat nf = NumberFormat.getInstance();
		
		boolean lb_deduct=false;
		
		String businessId = this.uwDao.selectBusinessId(spaj);
		Class.forName("produk_asuransi.n_prod_"+businessId).newInstance();
		businessId = FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 3);
		
		double ldec_kurs; 
		Map pribadi = this.uwDao.selectInfoPribadi(spaj);
		String ls_region = (String) pribadi.get("REGION");
		String prodDate = (String) pribadi.get("PROD_DATE"); //yyyy (tahun produksi)
		emailMessage.append("[PRODUCTION DATE] " + pribadi.get("MSPRO_PROD_DATE") + "\n");
		int bulanProd = Integer.parseInt(uwDao.selectBulanProduksi(spaj));
		
		//prodDate = "2009";
		
		List lds_prod = null;
		List lds_kom = null;
		
		lds_kom = this.uwDao.selectViewKomisiAsli(spaj);
		lds_prod = this.uwDao.selectAgentsFromSpaj(spaj);
		
		
		if(!lds_kom.isEmpty()) {
			Commission kms = (Commission) lds_kom.get(0);
			if(kms.getKurs_id().equals("02")){
				ldec_kurs = this.uwDao.selectLastCurrency(spaj, 1, 1);
				if(ldec_kurs<0){
					errors.reject("payment.noLastCurrency");
					return;
				}
			}else ldec_kurs = 1;

			String ls_agent[] = new String[4];
			
			Double li_or[] = {0., 1., 0.5, 0.};

			Double li_persen[] = new Double[4];
			int maxCount = 4;
			
			
			int ll_find;
			boolean ketemu = false;
			
			for(int i=1; i<lds_kom.size(); i++){
				int k=2;
				Commission temp = (Commission) lds_kom.get(i);
				if(temp.getBisnis_id()==178 || temp.getBisnis_id()==187 ){
					li_or = new Double[]{0., 9., 6.5, 0.};
				}
				if(temp.getBisnis_id()==208 || temp.getBisnis_id()==208 ){
					li_or = new Double[]{0., 9., 6.5, 0.};
				}
				ls_agent[i] = temp.getAgent_id();
				li_persen[i] = 0.;
				ll_find = 0;
				do{
					ketemu=false;
					for(int j = ll_find; j<lds_prod.size(); j++){
						String msag_id = ((HashMap) lds_prod.get(j)).get("MSAG_ID").toString(); 
						if(msag_id.equals(ls_agent[i])){ 
							ll_find=j; ketemu=true; break; 
						}
					}
					if(ketemu){
						int lsle_id = ((BigDecimal) ((HashMap) lds_prod.get((int)ll_find)).get("LSLE_ID")).intValue(); 
						if(lsle_id != maxCount){
							li_persen[i] += li_or[k-1];
							k++;
						}
					}
					ll_find++;
				}while(ll_find <= maxCount);
				
				
			}			

			double ldec_premi = 0;
			double persen = 0;
			Double ldec_komisi = null;
			
			for(int i=0; i<lds_kom.size(); i++) {
				/** AWAL DARI LOOPING KOMISI **/
				Commission komisi = (Commission) lds_kom.get(i);
				komisi.setFlag_mess(Boolean.TRUE);
				if("183,189,193,195,201,204".indexOf(komisi.getBisnis_id().toString()) >-1){
					komisi.setPremi(bacDao.selectTotalKomisiEkaSehat(spaj));
				}
				if(i==0) ldec_premi = FormatNumber.round(komisi.getPremi() * ldec_kurs, 2);
				int li_cb_id = komisi.getCb_id();
				int li_cb_id_asli = komisi.getCb_asli();
				if(!products.progressiveLink(komisi.getBisnis_id().toString()) && !products.stableSavePremiBulanan(komisi.getBisnis_id().toString()) && !products.healthProductStandAlone(komisi.getBisnis_id().toString())){
					if( li_cb_id==1 || li_cb_id==2 || li_cb_id==6 ) komisi.setCb_id(3);
				}
				komisi.setCb_id(new Integer(li_cb_id));
				
				komisi.setLsco_jenis(new Integer(1)); //Commission MallAssurance system
				getLamaTanggung(komisi);
				
				if(f_get_komisi(komisi, errors, 1, 1, komisi.getKurs_id(), komisi.getPremi(), maxCount)==true){
					
					if(products.powerSave(komisi.getBisnis_id().toString())){
						int mgi = this.uwDao.selectMgiNewBusiness(spaj);
						TopUp t = (TopUp) this.uwDao.selectTopUp(spaj, 1, 1, "desc").get(0);
						Date ldt_tgl_rk = t.getMspa_date_book();
						if(ldt_tgl_rk.compareTo(defaultDateFormatReversed.parse("20111116"))< 0 ){//request TRI, berdasarkan tgl RK apabila tgl RK di bawah tgl 16 nov 2011, ambil komisi lama
							komisi.setTgl_kom(ldt_tgl_rk);
						}
						Map komisiPowersave = uwDao.selectKomisiPowersave(1, 
								komisi.getBisnis_id(), komisi.getBisnis_no(), 
								mgi, komisi.getKurs_id(), komisi.getLev_kom(), 1, komisi.getPremi(), 0, komisi.getTgl_kom());

						if(komisiPowersave == null) {
							errors.reject("payment.noCommission", 
									new Object[]{
									   defaultDateFormat.format(komisi.getTgl_kom()),
									   komisi.getBisnis_id()+"-"+komisi.getBisnis_no(),
									   komisi.getCb_id(), 
									   komisi.getIns_period(), 
									   komisi.getKurs_id(), 
									   komisi.getLev_kom(),
									   komisi.getTh_kom(),
									   komisi.getLsco_jenis()
							           }, 
									"Commission POWERSAVE Tidak Ada.<br>- Tanggal: {0}<br>-Kode Bisnis: {1}<br>-Cara Bayar: {2}<br>-Ins_per: {3}<br>-Kurs: {4}<br>-Level: {5}<br>-Tahun Ke{6}<br>-Jenis: {7}");
							throw new Exception(errors);
						}else {
							if(komisiPowersave.get("LSCP_COMM")!=null) komisi.setKomisi(Double.valueOf(komisiPowersave.get("LSCP_COMM").toString()));
							if(komisiPowersave.get("LSCP_BONUS")!=null) komisi.setBonus(Double.valueOf(komisiPowersave.get("LSCP_BONUS").toString()));
						}
					}
					
					/** komisi = persentase komisi * premi **/
					if(pribadi.get("PRIBADI").toString().equals("1")){
						komisi.setKomisi(0.0);
						ldec_komisi = 0.0;
					}else{
						if(i>0) komisi.setKomisi(li_persen[i]);
						ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
					}
					if(ldec_komisi==0 && !pribadi.get("PRIBADI").toString().equals("1"))continue;

					//kalau masih aktif
					if(komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
						komisi.setCo_id(sequenceMst_commission(11));

						ldec_komisi = cekLinkSingle(spaj, ldec_komisi, komisi.getLev_kom(), false);
						
						Date sysdate = commonDao.selectSysdateTruncated(0);
						
						komisi.setTax(new Double(ldec_komisi * komisi.getTax() / 100));
						if(komisi.getTax() > 0){
							komisi.setTax(f_load_tax(ldec_komisi, sysdate, komisi.getAgent_id()));

							komisi.setTax(FormatNumber.rounding(komisi.getTax() , true, 25));
							ldec_komisi  = FormatNumber.round(ldec_komisi , 0);
							if(komisi.getTax()==null)komisi.setTax(new Double(0));
							persen = komisi.getKomisi();
							komisi.setKomisi(ldec_komisi);
							komisi.setNilai_kurs(new Double(ldec_kurs));
							komisi.setLus_id(currentUser.getLus_id());
							komisi.setMsbi_tahun_ke(new Integer(1));
							komisi.setMsbi_premi_ke(new Integer(1));
							if(komisi.getKomisi()>0) {
								if(komisi.getKomisi()>0) {
									String tipeKomisi = "KOMISI";
									if(komisi.getLev_kom()==3 || komisi.getLev_kom()==4){
										if(komisi.getLev_kom().intValue()!=4) tipeKomisi = "OR    ";
//										this.uwDao.insertMst_commission(komisi, sysdate);//mall tidak perlu proses komisi individu.pakai system seno(komisi per bulan)
										emailMessage.append("["+tipeKomisi+"] " + komisi.getLev_kom() + " : " + komisi.getAgent_id() + " : " + 
												nf.format(ldec_premi) + " * " + nf.format(persen) + "% = " + nf.format(komisi.getKomisi()) + "\n");

								}
							}
						}
						
						//tambahan : Yusuf (29/04/08) ada proses komisi rider, mulai 1 may 2008
//						prosesKomisiRider(komisi, i, persen);
						
					}
				}
				/** AKHIR DARI LOOPING KOMISI **/
			} 
			
			
			/** UNTUK DEBUGGING SAJA */
//			email.send(
//					new String[] {props.getProperty("admin.yusuf")},null, 
//					"Proses Komisi Agency System [" + spaj + "]", emailMessage.toString(), currentUser);

		}
		}
		
		
	}
	
	public void prosesKomisiAgencySystem(String spaj, User currentUser, BindException errors, String lca_id) throws Exception{ //of_komisi_asys()
		if(logger.isDebugEnabled())logger.debug("PROSES: prosesKomisiAgencySystem (of_komisi_asys)");
		StringBuffer emailMessage = new StringBuffer();
		emailMessage.append("[SPAJ  ] " + spaj + "\n");
		NumberFormat nf = NumberFormat.getInstance();
		
		boolean lb_deduct=false;
		
		int flag_telemarket = uwDao.selectgetFlagTelemarketing(spaj);
		
		String businessId = this.uwDao.selectBusinessId(spaj);
		Class.forName("produk_asuransi.n_prod_"+businessId).newInstance();
		businessId = FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 3);
		
		double ldec_kurs; 
		Map pribadi = this.uwDao.selectInfoPribadi(spaj);
		String ls_region = (String) pribadi.get("REGION");
		String prodDate = (String) pribadi.get("PROD_DATE"); //yyyy (tahun produksi)
		emailMessage.append("[PRODUCTION DATE] " + pribadi.get("MSPRO_PROD_DATE") + "\n");
		int bulanProd = Integer.parseInt(uwDao.selectBulanProduksi(spaj));
		
		//prodDate = "2009";
		
		List lds_prod = null;
		List lds_kom = null;
		
		//Yusuf - 5/1/09 - Mulai 2009, struktur komisi arthamas ada 6 tingkat, bukan 4 lagi
		if(Integer.parseInt(prodDate) > 2008 && lca_id.equals("46")) {
			lds_kom = this.uwDao.selectViewKomisiHybrid2009(spaj);
			lds_prod = this.uwDao.selectAgentsFromSpajHybrid2009(spaj);
		}else {
			lds_kom = this.uwDao.selectViewKomisiAsli(spaj);
			lds_prod = this.uwDao.selectAgentsFromSpaj(spaj);
		}
		
		if(!lds_kom.isEmpty()) {
			Commission kms = (Commission) lds_kom.get(0);
			String[] to = new String[] {"Karunia@sinarmasmsiglife.co.id;jelita@sinarmasmsiglife.co.id","Iriana@sinarmasmsiglife.co.id"};
			String[] cc = new String[] {"tri.handini@sinarmasmsiglife.co.id"};
			if( lca_id.indexOf("08,42,62,68") >=0){//request Wesni, apabila worksite
				to = new String[] {"wesni@sinarmasmsiglife.co.id"};
				cc= null;
			}
			
			if(kms.getLev_kom()==3){
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, new Date(), null, 
						false,props.getProperty("admin.ajsjava"),to,cc ,new String[] {"deddy@sinarmasmsiglife.co.id"},
						"[System Warning] Pengecekan level Agent untuk SPAJ " + spaj, 
						"Kepada Yth.\n"+
						"Bagian Agency di tempat.\n\n"+
						"Mohon dilakukan pengecekan pada SPAJ ini, karena untuk Komisi Utamanya tidak diberikan ke agent penutup langsung. Namun diberikan kepada kode Agent Leadernya yakni :"+kms.getAgent_id()+
						"\n\nTerima kasih.",  null, spaj);
			}
						
			if(kms.getKurs_id().equals("02")){
				ldec_kurs = this.uwDao.selectLastCurrency(spaj, 1, 1);
				if(ldec_kurs<0){
					errors.reject("payment.noLastCurrency");
					return;
				}
			}else ldec_kurs = 1;

			String ls_agent[] = new String[4];
			
			//ini rate OR untuk 37 (agency)
			int li_or[] = {0, 40, 30, 20};
			//ini rate OR untuk 46 (arthamas)
			if(lca_id.equals("46")) {
				li_or = new int[]{0, 35, 25, 20};
			}
			//ini rate OR untuk SURYAMAS AGUNG AGENCY
			if(kms.getBancass().equals("37B9")){
				li_or = new int[]{0, 40, 0, 120};
			}

			int li_persen[] = new int[4];
			int maxCount = 4;
			
			if(lds_kom.size()>4){
				ls_agent = new String[5];
				li_persen = new int[5];
			}
			
			//Yusuf - 5/1/09 - Mulai 2009, struktur komisi arthamas ada 6 tingkat, bukan 4 lagi
			if(Integer.parseInt(prodDate) > 2008 && lca_id.equals("46")) {
				ls_agent = new String[6];
				li_or = new int[]{0, 35, 25, 20, 5, 1}; //FC - SM - BM - DM - RM - RD
				li_persen = new int[6];
				maxCount = 6;
			}			
			
			int k=2;
			int ll_find;
			boolean ketemu = false;
			
			for(int i=1; i<lds_kom.size(); i++){
				Commission temp = (Commission) lds_kom.get(i);
				ls_agent[i] = temp.getAgent_id();
				li_persen[i] = 0;
				ll_find = 0;
				do{
					ketemu=false;
					for(int j = ll_find; j<lds_prod.size(); j++){
						String msag_id = ((HashMap) lds_prod.get(j)).get("MSAG_ID").toString(); 
						if(msag_id.equals(ls_agent[i])){ 
							ll_find=j; ketemu=true; break; 
						}
					}
					if(ketemu){
						int lsle_id = ((BigDecimal) ((HashMap) lds_prod.get((int)ll_find)).get("LSLE_ID")).intValue(); 
						if(lsle_id != maxCount){
							li_persen[i] += li_or[k-1];
							k++;
						}
					}
					ll_find++;
				}while(ll_find <= maxCount);
				
				
			}			

//			for(int x : li_persen) {
//				logger.info(x);
//			}
			
			double ldec_premi = 0;
			double persen = 0;
			Double ldec_komisi = null;
			
			for(int i=0; i<lds_kom.size(); i++) {
				/** AWAL DARI LOOPING KOMISI **/
				Commission komisi = (Commission) lds_kom.get(i);
				komisi.setLsbs_linebus(bacDao.selectLineBusLstBisnis(komisi.getBisnis_id().toString()));
				komisi.setFlag_mess(Boolean.TRUE);
				if("183,189,193,195,201,204".indexOf(komisi.getBisnis_id().toString()) >-1){
					komisi.setPremi(bacDao.selectTotalKomisiEkaSehat(spaj));
				}
				if(i==0) ldec_premi = FormatNumber.round(komisi.getPremi() * ldec_kurs, 2);
				int li_cb_id = komisi.getCb_id();
				int li_cb_id_asli = komisi.getCb_asli();
				if(!products.progressiveLink(komisi.getBisnis_id().toString()) && !products.stableSavePremiBulanan(komisi.getBisnis_id().toString()) && !products.healthProductStandAlone(komisi.getBisnis_id().toString())){
					if( li_cb_id==1 || li_cb_id==2 || li_cb_id==6 ) komisi.setCb_id(3);
				}
				komisi.setCb_id(new Integer(li_cb_id));
				if(komisi.getBisnis_id() == 64 ) komisi.setBisnis_id(new Integer(56));
				else if(komisi.getBisnis_id() == 67 ) komisi.setBisnis_id(new Integer(54));
				
				if("37,52,67".indexOf(lca_id)>-1) { //Agency
					komisi.setLsco_jenis(new Integer(3)); //Commission agency system
				}else if(lca_id.equals("46")) {
					komisi.setLsco_jenis(new Integer(4)); //Commission Arthamas
				}
				
				getLamaTanggung(komisi);
				
				if(f_get_komisi(komisi, errors, 1, 1, komisi.getKurs_id(), komisi.getPremi(), maxCount)==true){
					//Double jmlKom = komisi.getKomisi()==null? 0 : komisi.getKomisi();
//					if(jmlKom==0){
//						if(!(komisi.getLev_kom()==4 && komisi.getBisnis_id()==73)){
//							continue;
//						}
//					}
					int li_dplk=0;
					int li_club=0;
	
					Map prestige = this.uwDao.selectPrestigeClub(komisi.getAgent_id(), komisi.getTgl_kom());
					if(prestige!=null){
						li_club = Integer.parseInt(prestige.get("MSAC_AKTIF").toString());
						li_dplk = Integer.parseInt(prestige.get("MSAC_DPLK").toString());
					}
					
					if(komisi.getLev_kom().intValue() == maxCount){
						
						String kurs = komisi.getKurs_id();
						
						if(products.powerSave(komisi.getBisnis_id().toString())){
							int mgi = this.uwDao.selectMgiNewBusiness(spaj);
							TopUp t = (TopUp) this.uwDao.selectTopUp(spaj, 1, 1, "desc").get(0);
							Date ldt_tgl_rk = t.getMspa_date_book();
							if(ldt_tgl_rk.compareTo(defaultDateFormatReversed.parse("20111116"))< 0 ){//request TRI, berdasarkan tgl RK apabila tgl RK di bawah tgl 16 nov 2011, ambil komisi lama
								komisi.setTgl_kom(ldt_tgl_rk);
							}
							Map komisiPowersave = uwDao.selectKomisiPowersave(1, 
									komisi.getBisnis_id(), komisi.getBisnis_no(), 
									mgi, komisi.getKurs_id(), komisi.getLev_kom(), 1, ldec_premi, 0, komisi.getTgl_kom());

							if(komisiPowersave == null) {
								errors.reject("payment.noCommission", 
										new Object[]{
										   defaultDateFormat.format(komisi.getTgl_kom()),
										   komisi.getBisnis_id()+"-"+komisi.getBisnis_no(),
										   komisi.getCb_id(), 
										   komisi.getIns_period(), 
										   komisi.getKurs_id(), 
										   komisi.getLev_kom(),
										   komisi.getTh_kom(),
										   komisi.getLsco_jenis()
								           }, 
										"Commission POWERSAVE Tidak Ada.<br>- Tanggal: {0}<br>-Kode Bisnis: {1}<br>-Cara Bayar: {2}<br>-Ins_per: {3}<br>-Kurs: {4}<br>-Level: {5}<br>-Tahun Ke{6}<br>-Jenis: {7}");
								throw new Exception(errors);
							}else {
								if(komisiPowersave.get("LSCP_COMM")!=null) komisi.setKomisi(Double.valueOf(komisiPowersave.get("LSCP_COMM").toString()));
								if(komisiPowersave.get("LSCP_BONUS")!=null) komisi.setBonus(Double.valueOf(komisiPowersave.get("LSCP_BONUS").toString()));
							}
						}else{
							//Deddy - semua stable save, komisi diset 0.58
							if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())){
								komisi.setKomisi(0.58);
							}
							//POWERSAVE AS
							else if(komisi.getBisnis_id()==144) {
								int li_jwaktu = this.uwDao.selectMgiNewBusiness(spaj);
								//if(li_jwaktu==1) komisi.setKomisi(new Double(0.02)); hanya saat rollover, bukan new business
								if(li_jwaktu==3) komisi.setKomisi(0.05);
								else if(li_jwaktu==6) komisi.setKomisi(0.1);
								else if(li_jwaktu==12) komisi.setKomisi(0.2);
								else if(li_jwaktu==36) komisi.setKomisi(0.6);
								
//								(Deddy 28/7/2009) SK Direksi No.079/AJS-SK/VII/2009
								if(komisi.getBisnis_no()==1){
									if(li_jwaktu==3) komisi.setKomisi(0.063);
									else if(li_jwaktu==6) komisi.setKomisi(0.125);
									else if(li_jwaktu==12) komisi.setKomisi(0.25);
									else if(li_jwaktu==36) komisi.setKomisi(0.75);
									// 027/IM-DIR/IV/2011 Tertanggal 11 April 2011 perubahan komisi untuk powersave dan stable link.
									if(bulanProd >= 201105){
										if(li_jwaktu==3) komisi.setKomisi(0.15);
										else if(li_jwaktu==6) komisi.setKomisi(0.3);
										else if(li_jwaktu==12) komisi.setKomisi(0.6);
										else if(li_jwaktu==36) komisi.setKomisi(1.35);
									}
								}
								else if(komisi.getBisnis_no()==4){//stable save
									komisi.setKomisi(0.58);
								}else komisi.setKomisi(0.0);
								
								
							//POWERSAVE BULANAN YANG WORKSITE & BUKAN BANK Sinarmas (YUSUF - 25/08/2006) 
							}else if(komisi.getBisnis_id()==158 && komisi.getBisnis_no() < 5 && ls_region.startsWith("42")) {
								int li_jwaktu = uwDao.selectMgiNewBusiness(spaj);
								double ldec_temp = komisi.getPremi();
								if(li_jwaktu==3) komisi.setKomisi(0.12);
								else if(li_jwaktu==6) komisi.setKomisi(0.24);
								else if(li_jwaktu==12) komisi.setKomisi(0.48);
								else if(li_jwaktu==36) komisi.setKomisi(1.18);
								else komisi.setKomisi(0.0);
								
							//POWERSAVE BULANAN YANG BUKAN FINANCIAL PLANNER & BUKAN BANK Sinarmas (YUSUF - 25/08/2006)
							}else if(komisi.getBisnis_id()==158 && komisi.getBisnis_no() < 5 && !ls_region.startsWith("0916")) {
								int li_jwaktu = uwDao.selectMgiNewBusiness(spaj);
								double ldec_temp = komisi.getPremi();
								if(li_jwaktu==3) komisi.setKomisi(0.12);
								else if(li_jwaktu==6) komisi.setKomisi(0.24);
								else if(li_jwaktu==12) komisi.setKomisi(0.48);
								else if(li_jwaktu==36) komisi.setKomisi(1.18);
								else komisi.setKomisi(0.0);
								
							//HORISON, KOMISI DIHITUNG DARI (100 - MST_INSURED.MSTE_PCT_DPLK) * MST_PRODUCT_INSURED.MSPR_PREMIUM
							//KARENA MSPR_PREMIUM = TOTAL DARI BIAYA DPLK + PREMI, DAN PERSENTASENYA ADA DI MST_INSURED.MSTE_PCT_DPLK
							//(YUSUF - 22/09/2006)
							}else if(komisi.getBisnis_id()==149) {
								Map dplk = uwDao.selectPersentaseDplk(spaj);
								ldec_premi = (Double) dplk.get("premi");
							}else if(komisi.getBisnis_id()==73 && komisi.getBisnis_no()!=8) {
								//TESTING BUAT LSDBS_NUMBER 8
								//Yusuf (19 Feb 2007) PA Stand Alone
								komisi.setKomisi(12.5);
								
							//POWERSAVE SYARIAH (YUSUF - 28/01/09)
							}else if(komisi.getBisnis_id() == 175) {
								int li_jwaktu = this.uwDao.selectMgiNewBusiness(spaj);

								if(li_jwaktu==3) komisi.setKomisi(0.05);
								else if(li_jwaktu==6) komisi.setKomisi(0.1);
								else if(li_jwaktu==12) komisi.setKomisi(0.2);
								else if(li_jwaktu==36) komisi.setKomisi(0.6);
								else komisi.setKomisi(0.0);

							//POWERSAVE BULANAN SYARIAH (YUSUF - 28/01/09)
							}else if(komisi.getBisnis_id() == 176) {
								int li_jwaktu = this.uwDao.selectMgiNewBusiness(spaj);

								if(li_jwaktu==3) komisi.setKomisi(0.12);
								else if(li_jwaktu==6) komisi.setKomisi(0.24);
								else if(li_jwaktu==12) komisi.setKomisi(0.48);
								else if(li_jwaktu==36) komisi.setKomisi(1.18);
								else komisi.setKomisi(new Double(0));
							
							//STABLE SAVE (YUSUF - 30/01/09)
							}else if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())) {
//								(Deddy) Perubahan perhitungan komisi sesuai  SK.Direksi No. 068/AJS-SK/VI/2009
//								perhitungan yang lama berdasarkan MGI.
								
//								int mgi = uwDao.selectMasaGaransiInvestasi(spaj);
//								
//								if(mgi == 1) 		komisi.setKomisi(0.0167);
//								else if(mgi == 3) 	komisi.setKomisi(0.05);
//								else if(mgi == 6) 	komisi.setKomisi(0.1);
//								else if(mgi == 12) 	komisi.setKomisi(0.2);

//								if(komisi.getBisnis_no().intValue()==1){ //individu
//								komisi.setKomisi(0.58);
//							}else if(komisi.getBisnis_no().intValue()==2) { //BII
//								//komisi.setKomisi(2.8);// KHUSUS BII (SK. Direksi No. 060/AJS-SK/V/2009)
//								komisi.setKomisi(2.5);//(Deddy - 28/7/2009)berlaku tgl : 1 agustus 2009 berdasarkan SK. Direksi No. 088/AJS-SK/VII/2009
//							}
								
								if((komisi.getBisnis_id() == 184 && komisi.getBisnis_no().intValue()==2 ) ||
								   (komisi.getBisnis_id() == 158 && komisi.getBisnis_no().intValue()==15) ||
								   (komisi.getBisnis_id() == 143 && komisi.getBisnis_no().intValue()==5)){
//									komisi.setKomisi(2.8);// KHUSUS BII (SK. Direksi No. 060/AJS-SK/V/2009)
									komisi.setKomisi(2.5);//(Deddy - 28/7/2009)berlaku tgl : 1 agustus 2009 berdasarkan SK. Direksi No. 088/AJS-SK/VII/2009
								}else{
									komisi.setKomisi(0.58);
								}

								
							//STABLE SAVE PREMI BULANAN (YUSUF - 31/04/09)
							}else if(products.stableSavePremiBulanan(komisi.getBisnis_id().toString())) {
								//komisi.setKomisi(0.0167); //karena mgi nya cuman 1 bulan aja
//								komisi.setKomisi(0.5); //(Deddy) -  SK.Direksi No. 087/AJS-SK/VII/2009
								int li_jwaktu = this.uwDao.selectMasaGaransiInvestasi(spaj,1,1); // SK.Direksi No. 140/AJS-SK/XII/2009
								
								if(li_jwaktu==3) komisi.setKomisi(0.125);
								else if(li_jwaktu==6) komisi.setKomisi(0.25);
								else if(li_jwaktu==12) komisi.setKomisi(0.5);
								else if(li_jwaktu==1) komisi.setKomisi(0.042);
							}else if(products.progressiveLink(komisi.getBisnis_id().toString()) && ( bulanProd >= 201102 && bulanProd <=201104 ) ){
								
								int li_jwaktu = this.uwDao.selectMasaGaransiInvestasi(spaj,1,1);
								
								if(li_jwaktu==3) komisi.setKomisi(0.126);
								else if(li_jwaktu==6) komisi.setKomisi(0.375);
								else if(li_jwaktu==12) komisi.setKomisi(0.75);
								else if(li_jwaktu==1) komisi.setKomisi(1.5);
							}
						}

					}
					
					/** komisi = persentase komisi * premi **/
					if(pribadi.get("PRIBADI").toString().equals("1")){
						komisi.setKomisi(0.0);
						ldec_komisi = 0.0;
					}else{
						if(i>0) komisi.setKomisi(new Double(li_persen[i]));
						
						if(i>0 && products.stableLink(komisi.getBisnis_id().toString()) && komisi.getBisnis_id() != 186) {
							if(bulanProd >= 200809 && bulanProd <= 200812) {
								komisi.setKomisi(new Double(li_persen[i] * 0.1));
							}else {
								komisi.setKomisi(0.);
							}
						}
						
						if(komisi.getBisnis_id().intValue()==167 && i==0) {
							ldec_komisi = new Double(ldec_premi * (komisi.getKomisi()) / 100);
						}else if(komisi.getBisnis_id().intValue()==96 && komisi.getBisnis_no().intValue()>=13&& i==0){
							ldec_komisi = new Double(ldec_premi * (komisi.getKomisi()) / 100);
						}else if(li_cb_id_asli == 3 && i==0){ //kalau cara bayar tahunan, ada bonusnya
							ldec_komisi = new Double(ldec_premi * (komisi.getKomisi() + komisi.getBonus()) / 100);
						}else{
							ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
						}
						//Eka proteksi sekaligus
						//Jika cara bayar sekaligus (Himmia) 8/11/00
						if((komisi.getBisnis_id()==52 || komisi.getBisnis_id()==51) && komisi.getCb_asli()==0){
							ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
						
						//Khusus eka sarjana mandiri RP 3 & 5
						// Kalo premi RP > 10jt atau prestige club
						}else if(komisi.getBisnis_id()==63 && komisi.getKurs_id().equals("01") && (komisi.getBisnis_no()==2 || komisi.getBisnis_no()==3)){
							if(i==0 && (ldec_premi >=10000000 || li_club==1)){
								//3 tahun
								if(komisi.getBisnis_no()==2){
									komisi.setKomisi(new Double(7.5));
									if(komisi.getCb_asli()==3)komisi.setKomisi(12.5);
								}else{
									//5 tahun
									komisi.setKomisi(new Double(25));
									if(komisi.getCb_asli()==3)komisi.setKomisi(new Double(30));
								}
								ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
							}
						//Khusus eka sarjana mandiri yang baru (173)
						}else if(komisi.getBisnis_id()==173){
														
							if(i==0){
								komisi.setKomisi(5.);
															
								int autodebet = bacDao.selectFlagCC(spaj);
								//bila autodebet payroll
								if(autodebet == 4){
									komisi.setKomisi(7.5);
								}else{									
									if(komisi.getBisnis_no()==1){
										komisi.setKomisi(5.);
									}else if(komisi.getBisnis_no()==2 || komisi.getBisnis_no()==3){
										komisi.setKomisi(10.);
									}
								}
								
								ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
							}
						// Untuk Produk Ekasiswa Emas yang baru (172)
						}else if(komisi.getBisnis_id()==172){
							if(i==0){
								Integer li_age = this.uwDao.selectAgeFromSPAJ(komisi.getReg_spaj());
								//bila cara bayar bulanan
								if(komisi.getCb_asli().intValue() == 6) {
									if(komisi.getKurs_id().equals("01") ){
										if(li_age >=1 && li_age <= 8){
											komisi.setKomisi(new Double(22.5));
										}else if(li_age>=9 && li_age <= 12){
											komisi.setKomisi(new Double(17.5));
										}
									}else if(komisi.getKurs_id().equals("02")){
										if(li_age >=1 && li_age <= 5){
											komisi.setKomisi(new Double(22.5));
										}else if(li_age>=6 && li_age <= 8){
											komisi.setKomisi(new Double(17.5));
										}else if(li_age>=9 && li_age <= 12){
											komisi.setKomisi(new Double(12.5));
										}
									}
									
								//cara bayar lainnya
								}else{
									if(komisi.getKurs_id().equals("01") ){
										if(li_age >=1 && li_age <= 8){
											komisi.setKomisi(new Double(25));
										}else if(li_age>=9 && li_age <= 12){
											komisi.setKomisi(new Double(20));
										}
									}else if(komisi.getKurs_id().equals("02")){
										if(li_age >=1 && li_age <= 5){
											komisi.setKomisi(new Double(25));
										}else if(li_age>=6 && li_age <= 8){
											komisi.setKomisi(new Double(20));
										}else if(li_age>=9 && li_age <= 12){
											komisi.setKomisi(new Double(15));
										}
									}
								}
								ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
							}
						}else if(komisi.getBisnis_id()==180){//untuk produk prosaver (180)
							if(i==0){
								komisi.setKomisi(5.);
								int autodebet = bacDao.selectFlagCC(spaj);
								//bila autodebet payroll
								if(autodebet == 4){
									if(komisi.getBisnis_no().intValue()==1){
										komisi.setKomisi(17.5);
									}else if(komisi.getBisnis_no().intValue()==2){
										komisi.setKomisi(22.5);
									}else if(komisi.getBisnis_no().intValue()==3){
										komisi.setKomisi(22.5);
									}
								}else{									
									if(komisi.getBisnis_no().intValue()==1){
										komisi.setKomisi(20.0);
									}else if(komisi.getBisnis_no().intValue()==2){
										komisi.setKomisi(25.0);
									}else if(komisi.getBisnis_no().intValue()==3){
										komisi.setKomisi(25.0);
									}
									if(komisi.getCb_asli().intValue()==0){
										komisi.setKomisi(5.0);
									}
								}
							}
						}

					}
					// Khusus plan pa pti
					//if(komisi.getLev_kom()==4 && komisi.getBisnis_id()==73)ldec_komisi = new Double(10000);
					if(ldec_komisi==0 && !pribadi.get("PRIBADI").toString().equals("1"))continue;

					//kalau masih aktif
					if(komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
						if(("183,189,193,201".indexOf(komisi.getBisnis_id().toString()) >-1)){
							komisi.setCo_id(sequenceMst_commission(11));
						}else if("183,189,193,201".indexOf(komisi.getBisnis_id().toString()) <=-1){
							komisi.setCo_id(sequenceMst_commission(11));
						}
						

						//TAMBAHAN OLEH YUSUF UNTUK PRODUK2 SINGLE (IM No. 132/IM-DIR/XII/2007) - 16/01/2008
						ldec_komisi = cekLinkSingle(spaj, ldec_komisi, komisi.getLev_kom(), false);
						
						Date sysdate = commonDao.selectSysdateTruncated(0);
						
						
						if(komisi.getAgent_id().equals("500001")) komisi.setTax(new Double(ldec_komisi * 0.15));
						else {
							komisi.setTax(new Double(ldec_komisi * komisi.getTax() / 100));
							if(komisi.getTax() > 0){
								komisi.setTax(f_load_tax(ldec_komisi, sysdate, komisi.getAgent_id()));
								//komisi.setTax(hitungPajakKomisi(ldec_komisi, sysdate, komisi.getAgent_id()));
							}
						}

						komisi.setTax(FormatNumber.rounding(komisi.getTax() , true, 25));
						ldec_komisi  = FormatNumber.round(ldec_komisi ,  0);
						if(komisi.getTax()==null)komisi.setTax(new Double(0));
						
						persen = komisi.getKomisi();
						komisi.setKomisi(ldec_komisi);
						komisi.setNilai_kurs(new Double(ldec_kurs));
						komisi.setLus_id(currentUser.getLus_id());
						komisi.setMsbi_tahun_ke(new Integer(1));
						komisi.setMsbi_premi_ke(new Integer(1));
						if(komisi.getLev_kom().intValue() < 4 && (products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no()) || komisi.getBisnis_id()==203)) {
							komisi.setKomisi(0.);
						}
						if(komisi.getKomisi()>0) {

							//Yusuf - 5/1/09 - Mulai 2009, struktur komisi arthamas ada 6 tingkat, bukan 4 lagi, tapi diinsertnya bukan dari 6, tetep dari 4
							if(Integer.parseInt(prodDate) > 2008 && lca_id.equals("46")) {
								if(komisi.getLev_kom().intValue() > 2) komisi.setLev_kom(komisi.getLev_kom().intValue() - 2);
								else komisi.setLev_kom(0);
							}
							if(komisi.getKomisi()>0) {
								String tipeKomisi = "KOMISI";
								if( komisi.getLev_kom()<3 && (ls_region.startsWith("37A8") || ls_region.startsWith("37C7"))){
									if(komisi.getBisnis_id().toString().equals("096") && (komisi.getBisnis_no()>=13 && komisi.getBisnis_no()<=15)){
										this.uwDao.insertMst_commission(komisi, sysdate);
									}else{
//										List listAgent = bacDao.selectKetAgent(komisi.getAgent_id());
//										Map detilAgent = (Map)listAgent.get(0);
//										DateFormat df3 = new SimpleDateFormat("yyyyMM");
//										String periode = df3.format(sysdate);
//										komisi.setMsco_comm_kud(komisi.getKomisi());
//										komisi.setMsco_tax_kud(0.);
//										komisi.setPeriode_kud(periode);
//										komisi.setMcl_id_kud((String) detilAgent.get("MCL_ID"));
//										komisi.setCreate_date_kud(sysdate);
//										if(ls_region.startsWith("37A8")){
//											komisi.setJenis_kud(9);
//										}else{
//											komisi.setJenis_kud(10);
//										}
//										this.uwDao.insertMst_diskon_perusahaan(komisi);
									}
								}else{
									if(products.healthProductStandAlone(komisi.getBisnis_id().toString()) ){// inidiedit
										//Khusus Smile medical, hanya agent penutup saja yg dapat komisi.
										//FIX ME: RYAN, Sekarang , SMiLe Medical, semua level dpt komisi , asal terdaftar di mst_agent_comm
										if(("183,189,193,201".indexOf(komisi.getBisnis_id().toString()) >-1) ){
										//	if(komisi.getLev_kom()==4){
												this.uwDao.insertMst_commission(komisi, sysdate);
												emailMessage.append("["+tipeKomisi+"] " + komisi.getLev_kom() + " : " + komisi.getAgent_id() + " : " + 
														nf.format(ldec_premi) + " * " + nf.format(persen) + "% = " + nf.format(komisi.getKomisi()) + "\n");
											//}
										}else{
											this.uwDao.insertMst_commission(komisi, sysdate);
										}
									}else if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())){
										if(komisi.getLev_kom()==4){
											this.uwDao.insertMst_commission(komisi, sysdate);
											emailMessage.append("["+tipeKomisi+"] " + komisi.getLev_kom() + " : " + komisi.getAgent_id() + " : " + 
													nf.format(ldec_premi) + " * " + nf.format(persen) + "% = " + nf.format(komisi.getKomisi()) + "\n");
										}
									}else if((komisi.getBisnis_id()==170 && komisi.getAgent_id().equals("022902")) || (komisi.getBisnis_id()==187 && "5,6".indexOf(komisi.getBisnis_no().toString()) >-1)){
										
									}else this.uwDao.insertMst_commission(komisi, sysdate);
								}
								tipeKomisi = "KOMISI";
								if(komisi.getLev_kom().intValue()!=4) tipeKomisi = "OR    ";
								emailMessage.append("["+tipeKomisi+"] " + komisi.getLev_kom() + " : " + komisi.getAgent_id() + " : " + 
										nf.format(ldec_premi) + " * " + nf.format(persen) + "% = " + nf.format(komisi.getKomisi()) + "\n");
							}
							
							//Yusuf - 08/04/09 - Start 13 April, Bonus Komisi untuk Produk Save + StableLink dipisah ke comm_bonus
							if(!pribadi.get("PRIBADI").toString().equals("1")){
								if(i==0) {
									if(komisi.getBisnis_id()==170 && komisi.getAgent_id().equals("022902") ){
										
									}else{
										prosesBonusKomisi(
												"agency", ls_region, komisi.getReg_spaj(), komisi.getAgent_id(), komisi.getCo_id(), 
												komisi.getKurs_id(), ldec_premi, ldec_kurs, currentUser.getLus_id(), komisi.getKomisi(), komisi.getTax(),
												komisi.getMsbi_tahun_ke(), komisi.getMsbi_premi_ke(), komisi.getLev_kom(), komisi.getLsbs_linebus());
									}
								}else if(i==2 && kms.getBancass().equals("37B9") ){
									prosesBonusKomisi(
											"agency", ls_region, komisi.getReg_spaj(), komisi.getAgent_id(), komisi.getCo_id(), 
											komisi.getKurs_id(), ldec_premi, ldec_kurs, currentUser.getLus_id(), komisi.getKomisi(), komisi.getTax(),
											komisi.getMsbi_tahun_ke(), komisi.getMsbi_premi_ke(), komisi.getLev_kom(), komisi.getLsbs_linebus());
								}
							}
							//
						}
						
						//tambahan : Yusuf (29/04/08) ada proses komisi rider, mulai 1 may 2008
						prosesKomisiRider(komisi, i, persen);
						
						if(i==0){
							Map temp = this.uwDao.selectBillingRemain(komisi.getReg_spaj());
							if(temp != null) {
								double ldec_sisa = Double.parseDouble(temp.get("MSBI_REMAIN").toString());
								int li_flag = Integer.parseInt(temp.get("MSBI_FLAG_SISA").toString());
								if(li_flag==2){
									this.uwDao.insertMst_deduct(komisi.getCo_id(), ldec_sisa, currentUser.getLus_id(), 1, 3, "PREMI KURANG BAYAR");
									lb_deduct=true;
								}
							}
						}

						//Jika masuk prestige club, pot buat dplk
						if(li_club==1 && li_dplk==1){
							int li_deduct_no = 1;
							if(i==0 && lb_deduct)li_deduct_no=2;
							Double ldec_dplk = Double.valueOf(prestige.get("LPC_DPLK_K").toString());
							ldec_dplk = new Double((ldec_komisi - komisi.getTax()) * ldec_dplk / 100);
							ldec_dplk = FormatNumber.round(ldec_dplk, 0);
							this.uwDao.insertMst_deduct(komisi.getCo_id(), ldec_dplk, currentUser.getLus_id(), li_deduct_no, 5, "POTONGAN DPLK /U " + prestige.get("LPC_CLUB").toString() );
						}
						
						//(Yusuf - 27/12/2006) untuk agency system, sekarang ada reward 10% -> SK No. 110/EL-SK/XII/2006
						//Req Himmia (3 Apr 2013) : Untuk setiap proses reward, dicek terlebih dahulu apakah rekruter tersebut di mst_agent masih terdaftar aktif dan mendapatkan komisi ga. Apabila 1, maka dapat reward.
						if(i==0 && Integer.parseInt(prodDate) > 2006 && komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1) {
							
							boolean isReward = false;
							if("37,52,67".indexOf(lca_id)>-1) { //Agency
								isReward = true;
							}else if(lca_id.equals("46")) { //arthamas
								isReward = false;
							}else if(lca_id.equals("42")) { // worksite
								isReward = false;
							}
							
							if(products.stableLink(komisi.getBisnis_id().toString())) {
								isReward = false;
								if("08,09,42,40,46".indexOf(lca_id) == -1 && bulanProd >= 200809 && bulanProd <= 200812) {
									isReward = true;
								}
							}
							
							if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())){
								isReward = false;
							}
							
							if(products.productKhususSuryamasAgency(komisi.getBisnis_id(), komisi.getBisnis_no())){
								isReward = false;
							}
							
							String rekruter_id = uwDao.selectRekruterAgencySystem(komisi.getAgent_id()); //tarik rekruternya, jenis=4
							Rekruter rekruter = uwDao.selectRekruterFromAgen(rekruter_id); //tarik rekruter dari mst_agent, ada gak?, jenis=3
							if(rekruter==null && rekruter_id!=null) rekruter = uwDao.selectRekruterFromAgenSys(rekruter_id); //kalo gak ada, tarik dari mst_agensys
							if(rekruter!=null && !products.powerSave(komisi.getBisnis_id().toString()) && isReward) { //kalo ada, baru insert rewardnya 10%
								
								if(rekruter.getMsag_bay() == null) rekruter.setMsag_bay(0);
								
								if(rekruter.getSts_aktif().intValue() == 1 && rekruter.getMsag_bay().intValue() != 1) { //Yusuf (22/12/2010) hanya bila rekruternya aktif dan bukan agen bayangan baru dikasih reward
									//cek dulu, ada nomer rekeningnya gak
									if(rekruter.getNo_tab()==null) {
										errors.reject("payment.noRecruiter", new Object[] {rekruter.getAgent_id()}, "-");
										return;
									} else if(rekruter.getNo_tab().trim().equals("")) {
										errors.reject("payment.noRecruiter", new Object[] {rekruter.getAgent_id()}, "-");
										return;
									} else {
										rekruter.setNo_tab(StringUtils.replace(rekruter.getNo_tab(), "-",""));
										double pengali = 1;
										
										//Yusuf (22/12/2010) bila business partner, maka dapat reward hanya 5%
										if(rekruter.getLcalwk().startsWith("61")) pengali = 0.5;
										//Yusuf (23/03/09) - Per 1 April OR dan Reward tidak 35% lagi tapi full
										else if(isLinkSingle(spaj, false) && bulanProd < 200904) pengali = 0.35;
										else if(isEkalink88Plus(spaj, false)) pengali = 0.7;
										else if(products.stableLink(komisi.getBisnis_id().toString())) pengali = 0.1;
										
										double reward = FormatNumber.round((ldec_komisi * 0.1 * pengali), 0);
										
										if(rekruter.getMsag_komisi()!=null){
											if(rekruter.getMsag_komisi()==1){
												if("183,189,193,195,201,204".indexOf(komisi.getBisnis_id().toString())<=-1){
													this.uwDao.insertMst_reward(spaj, 1, 1, rekruter.getJenis_rekrut(), rekruter_id,
															rekruter.getNama(), rekruter.getNo_tab(), 
															rekruter.getLbn_id().toString(), 
															reward, 
															FormatNumber.rounding(f_load_tax(reward, sysdate, rekruter_id), true, 25),
															new Double(ldec_kurs), currentUser.getLus_id(), komisi.getLsbs_linebus());
													
													emailMessage.append("[RECRUITER REWARD] " + rekruter_id + " : " + nf.format(reward)+ "\n");
												}
											}
										}
									}
								}								
							}
						}
						
					}
					
					/*
					//Proses Bonus Penjualan (EKA.MST_COMM_BONUS) 
					//- khusus produk EKALINK REGULER (159-2) - (Yusuf - 15/09/2006)
					//- dan khusus produk IKHLAS & SAQINAH (MUAMALAT) - (Yusuf - 03/03/2009)
					if(!pribadi.get("PRIBADI").toString().equals("1")){
						if(i==0) {
							
							//EKALINK DAN SAKINAH, ADA BONUS PENJUALAN 5%
							if((
								(komisi.getBisnis_id() == 159 || komisi.getBisnis_id() == 160) && komisi.getBisnis_no() == 2) || 
								(komisi.getBisnis_id() == 171 && komisi.getBisnis_no().intValue() == 1)
							) {
								Double bonus = FormatNumber.rounding(0.05 * ldec_premi, false, 25); //bonus penjualan 5%
								Date sysdate = commonDao.selectSysdateTruncated(0);

								Double temp = komisi.getKomisi() + bonus;
								Double bonus_tax = hitungPajakKomisi(temp, sysdate, komisi.getAgent_id());
								bonus_tax -= komisi.getTax();
								bonus_tax = FormatNumber.rounding(bonus_tax, false, 25);
								
								Map infoBonus = uwDao.selectBonusAgen(komisi.getAgent_id());
								this.uwDao.insertMst_comm_bonus( //tipe = 1 (bonus penjualan)
										spaj, 1, 1, 1, komisi.getAgent_id(), 
										(String) infoBonus.get("MCL_FIRST"), (String) infoBonus.get("MSAG_TABUNGAN"), 
										infoBonus.get("LBN_ID").toString(), komisi.getCo_id(), bonus, bonus_tax, ldec_kurs, currentUser.getLus_id());
							}
							
							//IKHLAS DAN SAKINAH, ADA LAGI BONUS TABUNGAN
							if(komisi.getBisnis_id() == 170 || (komisi.getBisnis_id() == 171 && komisi.getBisnis_no().intValue() == 1)){
								Double bonus = (double) 35000; //bonus penjualan Rp. 35 ribu
								Date sysdate = commonDao.selectSysdateTruncated(0);

								Double bonus_tax = hitungPajakKomisi(bonus, sysdate, komisi.getAgent_id());
								bonus_tax = FormatNumber.rounding(bonus_tax, false, 25);
								
								Map infoBonus = uwDao.selectBonusAgen(komisi.getAgent_id());
								this.uwDao.insertMst_comm_bonus( //tipe = 2 (bonus tabungan)
										spaj, 1, 1, 2, komisi.getAgent_id(), 
										(String) infoBonus.get("MCL_FIRST"), (String) infoBonus.get("MSAG_TABUNGAN"), 
										infoBonus.get("LBN_ID").toString(), komisi.getCo_id(), bonus, bonus_tax, ldec_kurs, currentUser.getLus_id());
							}
						}
					}
					*/
					if( i == 0 ) {
						ldec_premi = ldec_komisi;
					}
					
					if(flag_telemarket==1){
						komisi.setKomisi(ldec_komisi/0.5);
					}
				}
				/** AKHIR DARI LOOPING KOMISI **/
			} 
			
			/** PROSES TAMBAHAN UNTUK INSERT AAKM DAN BPJ (Yusuf - 6/12/2007) KHUSUS AGENSYS 37 */
			/*
			if(lca_id.equals("37")) {
				//Reserved (AAKM) & (BPJ)
				Map infoAakmBpj = uwDao.selectFlagAAKMdanBPJ(spaj, 1, 1); 
				int li_lsbs = ((BigDecimal) infoAakmBpj.get("LSBS_ID")).intValue();
				int li_lsdbs = ((BigDecimal) infoAakmBpj.get("LSDBS_NUMBER")).intValue();
				int li_flag = 0;
				int li_ps = ((BigDecimal) infoAakmBpj.get("LSBS_JENIS")).intValue();
				double ldec_comm_4 = ((BigDecimal) infoAakmBpj.get("MSCO_COMM")).doubleValue();
				double ldec_percent, ldec_bonus, ldec_tax;
				
				Map infoAakm = uwDao.selectAAKM(spaj);
				String ls_lead_id = (String) infoAakm.get("MSAG_ID");
				li_flag = ((BigDecimal) infoAakm.get("MSAG_AAKM")).intValue();
				String ls_down_id = null;
				
				if(li_flag == 1) {
					//check for upper AD for AAKM
					ls_down_id = uwDao.selectLeadAAKM(ls_lead_id);

					//if upper AAKM AD found, set AAKM receiver to the upper one
					if(ls_down_id.equals("")) ls_down_id = ls_lead_id;
					
					//wf_get_agen
					Map infoAgenAakm = uwDao.selectInfoAgenAAKM(ls_down_id);
					String ls_nama = (String) infoAgenAakm.get("MCL_FIRST"); 
					int li_bank = ((BigDecimal) infoAgenAakm.get("LBN_ID")).intValue();
					String ls_acct = (String) infoAgenAakm.get("MSAG_TABUNGAN");
					ls_acct = ls_acct.replace("-", "").replace(".", "");
		
					//Ekalink Family Syariah and Ekalink 88 gets 30% AAKM
					if(li_lsbs == 159 || ( li_lsbs == 162 && (li_lsdbs == 5 || li_lsdbs == 6 || li_lsdbs == 7 || li_lsdbs == 8))) {
						ldec_percent = 0.3;
					}else if(li_ps == 1) { //powersave
						li_flag = uwDao.selectMasaGaransiInvestasi(spaj);
						if(li_flag == 36) {
							ldec_percent = 0.0006;
						} else if(li_flag == 12) {
							ldec_percent = 0.0002;
						} else {
							if(li_lsbs == 158) { // Powersave Bulanan doesn't get AAKM bonus if less than 12 months
								ldec_percent = 0;
							}else {
								if(li_flag == 6) {
									ldec_percent = 0.0001;
								}else if(li_flag == 3) {
									ldec_percent = 0.0005;
								}else {
									ldec_percent = 0;
								}
							}
						}
					}else {
						ldec_percent = 0.25;
					}
					
					ldec_bonus = FormatNumber.round(ldec_comm_4 * ldec_percent, 0);
					ldec_tax = FormatNumber.round(hitungPajakKomisi(ldec_bonus), 0);
					
					if((ldec_bonus - ldec_tax) > 0) {
						uwDao.insertMstAgencyBonus(spaj, 1, 1, 1, 
								1, ls_down_id, ls_nama, ls_acct, 
								li_bank, ldec_bonus, ldec_tax, null, 0, 1, null, null, Integer.valueOf(currentUser.getLus_id()), (new Double(ldec_kurs)).intValue(), 0);
						emailMessage.append("[AAKM] " + ls_nama + " : " + ls_down_id + "\n");
					}
				}
				
				//BPJ exclude Power Save
				if(li_ps == 0) {
					
					boolean lb_flag = false;
					
					//Check for BPJ for AD get AD
					ls_down_id = uwDao.selectRekruterAgenSys(ls_lead_id);
					if(ls_down_id == null) ls_down_id = "";
					
					if(ls_down_id.equals("")) { //AD get AD info not found, go for promotion bonus
						for(int i=3; i>=1; i--) {
							Map kopiDong = uwDao.selectHistoryAgen(spaj, i);
							if(kopiDong != null) {
								ls_down_id = (String) kopiDong.get("MSAG_ID");
								li_flag = ((BigDecimal) kopiDong.get("LSAS_ID")).intValue();
								//found promotion history and has not been longer than 1 year, give the BPJ bonus
								if(li_flag == 2) {
									lb_flag = true;
									break;
								}
							}
						}
					}else {
						//recruit BPJ bonus only for 1st year production
						lb_flag = true; 
					}
					
					//Process BPJ Bonus
					if(lb_flag) {
	
						//wf_get_agen
						Map kopiInstan = uwDao.selectInfoAgenAAKM(ls_down_id);
						String ls_nama = (String) kopiInstan.get("MCL_FIRST"); 
						int li_bank = ((BigDecimal) kopiInstan.get("LBN_ID")).intValue();
						String ls_acct = (String) kopiInstan.get("MSAG_TABUNGAN");
						ls_acct = ls_acct.replace("-", "").replace(".", "");
						
						ldec_bonus = FormatNumber.round(ldec_comm_4 * 0.1, 0);
						ldec_tax = FormatNumber.round(hitungPajakKomisi(ldec_bonus), 0);
						
						if((ldec_bonus - ldec_tax) > 0) {
							uwDao.insertMstAgencyBonus(spaj, 1, 1, 1, 
									2, ls_down_id, ls_nama, ls_acct, 
									li_bank, ldec_bonus, ldec_tax, null, 0, 1, null, null, Integer.valueOf(currentUser.getLus_id()), (new Double(ldec_kurs)).intValue(), 0);
							emailMessage.append("[BPJ] " + ls_nama + " : " + ls_down_id + "\n");
						}
	
					}
					
				}
			}*/
			
			/** UNTUK DEBUGGING SAJA */
//			email.send(
//					new String[] {props.getProperty("admin.yusuf")},null, 
//					"Proses Komisi Agency System [" + spaj + "]", emailMessage.toString(), currentUser);

		}
			
		//Setelah selesai proses komisi, hitung kom eva
		if(lca_id.equals("46") || (lca_id.equals("42") && (businessId.toString().equals("159") || businessId.toString().equals("160")))) {
			this.produksi.prosesPerhitunganCommEva(spaj, 1);
		}
		
	}
	
	public boolean prosesKomisiNewAgencySystem(String spaj, User currentUser, BindException errors, int premiKe) throws Exception{
		Integer hasil=null;
		hasil =uwDao.procKomNewAgency(spaj, 1, premiKe);
		if(hasil==0){
			errors.reject("Mohon Maaf, terjadi kesalahan pada saat proses kompensasi, Silakan hubungi IT.");
			throw new Exception(errors);
		}
		return true;
	}
	
	public boolean prosesKomisiMNC(String spaj, User currentUser, BindException errors, int premiKe) throws Exception{
		Date sysdate = commonDao.selectSysdateTruncated(0);
		DateFormat df3 = new SimpleDateFormat("yyyyMM");
		String periode = df3.format(sysdate);
		
		List detBisnis = uwDao.selectDetailBisnis(spaj);
		String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
		String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
		if("140,141,159".indexOf(lsbs) >=0){
			Tertanggung tertanggung = bacDao.selectttg(spaj);
			Pemegang pemegang = bacDao.selectpp(spaj);
			Datausulan dataUsulan = bacDao.selectDataUsulanutama(spaj);
			if(tertanggung.getMste_flag_special_offer()==2){
				Double premi_special_offer = dataUsulan.getMspr_premium() * 0.4;
				if("140,141".indexOf(lsbs)>=0){
					premi_special_offer = dataUsulan.getMspr_premium() * 0.3;
				}
				Commission komisi = new Commission();
				komisi.setKomisi(premi_special_offer);
				komisi.setMsco_comm_kud(premi_special_offer);
				komisi.setMsco_comm_kud(FormatNumber.round(komisi.getMsco_comm_kud(), 0));
				komisi.setReg_spaj(spaj);
				//komisi.setMsco_tax_kud(bonus_tax);
				komisi.setMsco_tax_kud(0.);
				komisi.setPeriode_kud(periode);
				komisi.setMcl_id_kud(pemegang.getMcl_id());
				komisi.setCreate_date_kud(sysdate);
				komisi.setLus_id(currentUser.getLus_id());
				komisi.setMsbi_tahun_ke(1);
				komisi.setMsbi_premi_ke(1);
				komisi.setJenis_kud(11);//mnc
				
				this.uwDao.insertMst_diskon_perusahaan(komisi);
			}else if(tertanggung.getMste_flag_special_offer()==0){
//				asd
			}
		}
		return true;
	}
	
	public boolean prosesRewardIndividu(String spaj, User currentUser, BindException errors) throws Exception{ //of_komisi_new_lg()
		if(logger.isDebugEnabled())logger.debug("PROSES: prosesKomisi (of_komisi_new_lg)");
		StringBuffer emailMessage = new StringBuffer();
		emailMessage.append("[SPAJ  ] " + spaj + "\n");
		NumberFormat nf = NumberFormat.getInstance();
		List detail = uwDao.selectDetailBisnis(spaj);
		if(!detail.isEmpty()) {
			Map det = (HashMap) detail.get(0);
			emailMessage.append(
					"[PRODUK] (" + det.get("BISNIS") + ") " + det.get("LSBS_NAME") + " - " + 
					"(" + det.get("DETBISNIS") + ") " + det.get("LSDBS_NAME") + "\n");
		}

		boolean lb_deduct=false;
		String businessId = FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 2);
		Class.forName("produk_asuransi.n_prod_"+businessId).newInstance();
		double ldec_kurs = 1; 
		Map pribadi = this.uwDao.selectInfoPribadi(spaj);
		String ls_region = (String) pribadi.get("REGION");
		int li_fo = ((Integer) pribadi.get("FOLLOW_UP"));
		String prodDate = (String) pribadi.get("PROD_DATE");
		prodDate = (prodDate == null ? "0" : prodDate);
		Date ldt_tgl_aktif = null;
		
		int li_dplk=0;
		int li_club=0;
		int li_rekrut=0;
		
		String lca_id = ls_region.substring(0,2);
		
		int flag_telemarket = uwDao.selectgetFlagTelemarketing(spaj);
		
		if("37,46,52".indexOf(lca_id)>-1){ //Agency) { //37 agency, 46 arthamas, 52 Agency Arthamas (Deddy 22 Feb 2012)
			Pemegang pp = bacDao.selectpp(spaj);
			if ((Integer) pp.getMsag_asnew()==1){
//				prosesKomisiNewAgencySystem(spaj,currentUser,errors,1);
			}else{
				prosesRewardAgencySystem(spaj, currentUser, errors, lca_id);
			}
			return false;
		//Yusuf (8/7/08) Proses Komisi Cross-Selling
		}else if(lca_id.equals("55")) {
			return true; //true menandakan > 2007
		}else if(lca_id.equals("58")){
			return true;
		}else if(lca_id.equals("60")) {
			return false; //true menandakan > 2007
		}else if(lca_id.equals("62")){
			return false;
		}else if(lca_id.equals("68")){//akan diupdate memakai flag, dan lca_id tetap seperti agency existing
			return false;
		}else if(Integer.parseInt(prodDate) > 2006 && !ls_region.startsWith("42")) {
			prosesRewardIndividu2007(spaj, currentUser, errors);
			return true; //true menandakan > 2007
		}
		
		List lds_kom = this.uwDao.selectViewKomisiAsli(spaj);
		if(!lds_kom.isEmpty()) {
			Commission kms = (Commission) lds_kom.get(0);
			if(kms.getKurs_id().equals("02")){
				ldec_kurs = this.uwDao.selectLastCurrency(spaj, 1, 1);
				if(ldec_kurs<0){
					errors.reject("payment.noLastCurrency");
					return false;
				}
			}else ldec_kurs = 1;

			double ldec_premi = 0;
			Double ldec_komisi = null;
			Map<String, Object> rekruter = null;
			Double ldec_kom_temp = null;
			
			for(int i=0; i<lds_kom.size(); i++) {
				//awal for
				li_dplk=0;
				li_club=0;
				
				Commission komisi = (Commission) lds_kom.get(i);
				komisi.setLsbs_linebus(bacDao.selectLineBusLstBisnis(komisi.getBisnis_id().toString()));
				komisi.setFlag_mess(Boolean.TRUE);
				if("183,189,193,195,201,204".indexOf(komisi.getBisnis_id().toString()) >-1){
					komisi.setPremi(bacDao.selectTotalKomisiEkaSehat(spaj));
				}
				if(i==0) ldec_premi = FormatNumber.round(komisi.getPremi() * ldec_kurs, 2);
				int li_cb_id = komisi.getCb_id();
				if(!products.progressiveLink(komisi.getBisnis_id().toString()) && !products.stableSavePremiBulanan(komisi.getBisnis_id().toString()) && !products.healthProductStandAlone(komisi.getBisnis_id().toString())){
					if( li_cb_id==1 || li_cb_id==2 || li_cb_id==6 ) komisi.setCb_id(3);
				}
				komisi.setCb_id(new Integer(li_cb_id));
				if(komisi.getBisnis_id() == 64 ) komisi.setBisnis_id(new Integer(56));
				else if(komisi.getBisnis_id() == 67 ) komisi.setBisnis_id(new Integer(54));
				komisi.setLsco_jenis(new Integer(1)); //Commission individu
				int bulanProd = Integer.parseInt(uwDao.selectBulanProduksi(spaj));
				getLamaTanggung(komisi);
				
				if(f_get_komisi(komisi, errors, 1, 1, komisi.getKurs_id(), komisi.getPremi(), 4)==true){
					Double jmlKom = komisi.getKomisi()==null?new Double(0):komisi.getKomisi();
					if(jmlKom==0){
//						if(!(komisi.getLev_kom()==4 && komisi.getBisnis_id()==73)){
//							continue;
//						}
					}

					Map prestige = this.uwDao.selectPrestigeClub(komisi.getAgent_id(), komisi.getTgl_kom());
					if(prestige!=null){
						li_club = Integer.parseInt(prestige.get("MSAC_AKTIF").toString());
						li_dplk = Integer.parseInt(prestige.get("MSAC_DPLK").toString());
					}
					
					if(komisi.getLev_kom()==4){
						
						String kurs = komisi.getKurs_id();
						
						rekruter = this.uwDao.selectRekruter(komisi.getAgent_id());
						if(rekruter!=null){//rekrutan
							//O.R 50%
							if(rekruter.get("MSRK_JENIS_REKRUT").toString().equals("3") ||
									rekruter.get("MSRK_JENIS_REKRUT").toString().equals("4"))li_rekrut=2;
							ldt_tgl_aktif = this.uwDao.selectAgentActiveDate(komisi.getAgent_id());

							//Untuk tambang emas th 2004, kalo masih aktif; or 50% semua, kalo tdk um 12.5, 10% reward buat um terakhir
							// u/ tambang emas baru per 1 mar 05, or um = 12.5%, yg lain sama
							if(ldt_tgl_aktif.compareTo(defaultDateFormatReversed.parse("20050301"))>=0)
								li_rekrut=3;
							else {
								//te 2004, masih berlaku, or 50% semua
								if(FormatDate.dateDifference(ldt_tgl_aktif, komisi.getTgl_kom(), true)<=365)li_rekrut = 2;
								else li_rekrut = 0;
							}
						}
						
						if(products.powerSave(komisi.getBisnis_id().toString())){
							int mgi = this.uwDao.selectMgiNewBusiness(spaj);
							TopUp t = (TopUp) this.uwDao.selectTopUp(spaj, 1, 1, "desc").get(0);
							Date ldt_tgl_rk = t.getMspa_date_book();
							if(ldt_tgl_rk.compareTo(defaultDateFormatReversed.parse("20111116"))< 0 ){//request TRI, berdasarkan tgl RK apabila tgl RK di bawah tgl 16 nov 2011, ambil komisi lama
								komisi.setTgl_kom(ldt_tgl_rk);
							}
							Map komisiPowersave = uwDao.selectKomisiPowersave(1, 
									komisi.getBisnis_id(), komisi.getBisnis_no(), 
									mgi, komisi.getKurs_id(), komisi.getLev_kom(), 1, komisi.getPremi(), 0, komisi.getTgl_kom());

							if(komisiPowersave == null) {
								errors.reject("payment.noCommission", 
										new Object[]{
										   defaultDateFormat.format(komisi.getTgl_kom()),
										   komisi.getBisnis_id()+"-"+komisi.getBisnis_no(),
										   komisi.getCb_id(), 
										   komisi.getIns_period(), 
										   komisi.getKurs_id(), 
										   komisi.getLev_kom(),
										   komisi.getTh_kom(),
										   komisi.getLsco_jenis()
								           }, 
										"Commission POWERSAVE Tidak Ada.<br>- Tanggal: {0}<br>-Kode Bisnis: {1}<br>-Cara Bayar: {2}<br>-Ins_per: {3}<br>-Kurs: {4}<br>-Level: {5}<br>-Tahun Ke{6}<br>-Jenis: {7}");
								throw new Exception(errors);
							}else {
								if(komisiPowersave.get("LSCP_COMM")!=null) komisi.setKomisi(Double.valueOf(komisiPowersave.get("LSCP_COMM").toString()));
								if(komisiPowersave.get("LSCP_BONUS")!=null) komisi.setBonus(Double.valueOf(komisiPowersave.get("LSCP_BONUS").toString()));
							}
						}else{
							// li_rekrut = 0 -> tdk aktif
							// li_rekrut = 1 -> um 12.5%, reward um 10%
							// li_rekrut = 2 -> or = 50%
							// li_rekrut = 3 -> um 12.5%, reward rekruter 10%
							
							if(komisi.getBisnis_id()==123 && komisi.getKurs_id().equals("01")){
								TopUp t = (TopUp) this.uwDao.selectTopUp(spaj, 1, 1, "desc").get(0);
								Date ldt_tgl_rk = t.getMspa_date_book();
								if(ldt_tgl_rk.compareTo(defaultDateFormatReversed.parse("20050908"))>=0 && ldt_tgl_rk.compareTo(defaultDateFormatReversed.parse("20051115"))<=0){
									int li_waktu = 0;
									Integer wkt = this.uwDao.selectMgiNewBusiness(spaj);
									if(wkt!=null) li_waktu = wkt;
									if(li_waktu==6)komisi.setKomisi(new Double(0.2));
									else if(li_waktu==12 || li_waktu==24)komisi.setKomisi(new Double(0.4));
									else komisi.setKomisi(new Double(0));
								}
							}
//							Deddy - Semua produk stable save, komisinya diset 0.58
							if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())){
								komisi.setKomisi(0.58);
							}
							//POWERSAVE INDIVIDU
							else if(komisi.getBisnis_id()==143) {
								int li_jwaktu = this.uwDao.selectMgiNewBusiness(spaj);
								//if(li_jwaktu==1) komisi.setKomisi(new Double(0.02)); hanya saat rollover, bukan new business
								
								if(li_jwaktu==3) komisi.setKomisi(new Double(0.05));
								else if(li_jwaktu==6) komisi.setKomisi(new Double(0.1));
								else if(li_jwaktu==12) komisi.setKomisi(new Double(0.2));
								else if(li_jwaktu==36) komisi.setKomisi(new Double(0.6));
								else komisi.setKomisi(new Double(0));
								
								//(Deddy 28/7/2009) SK Direksi No.079/AJS-SK/VII/2009
								if(komisi.getBisnis_no().intValue()==1 || komisi.getBisnis_no().intValue()==2 ||komisi.getBisnis_no().intValue()==3){
									if(li_jwaktu==3) komisi.setKomisi(new Double(0.063));
									else if(li_jwaktu==6) komisi.setKomisi(new Double(0.125));
									else if(li_jwaktu==12) komisi.setKomisi(new Double(0.25));
									else if(li_jwaktu==36) komisi.setKomisi(new Double(0.75));
									else komisi.setKomisi(new Double(0));
									// 027/IM-DIR/IV/2011 Tertanggal 11 April 2011 perubahan komisi untuk powersave dan stable link.
									if(bulanProd>=201105){
										if(li_jwaktu==1) komisi.setKomisi(0.05);
										else if(li_jwaktu==3) komisi.setKomisi(0.15);
										else if(li_jwaktu==6) komisi.setKomisi(0.3);
										else if(li_jwaktu==12) komisi.setKomisi(0.6);
										else if(li_jwaktu==36) komisi.setKomisi(1.35);
										else komisi.setKomisi(new Double(0));
									}
									
								}

//								(Deddy) Perubahan perhitungan komisi sesuai  SK.Direksi No. 068/AJS-SK/VI/2009
//								perhitungan yang lama berdasarkan MGI.
								if(komisi.getBisnis_no().intValue()==4){
									komisi.setKomisi(0.58);
								}else if(komisi.getBisnis_no().intValue()==5){
									//komisi.setKomisi(2.8); // KHUSUS BII (SK. Direksi No. 060/AJS-SK/V/2009)
									komisi.setKomisi(2.5);//(Deddy - 28/7/2009)berlaku tgl : 1 agustus 2009 berdasarkan SK. Direksi No. 088/AJS-SK/VII/2009
								}
								
								
							//POWERSAVE BULANAN YANG WORKSITE & BUKAN BANK Sinarmas (YUSUF - 25/08/2006)
							}else if(komisi.getBisnis_id()==158 && komisi.getBisnis_no() < 5 && ls_region.startsWith("42")) {
								int li_jwaktu = uwDao.selectMgiNewBusiness(spaj);
								
								if(li_jwaktu==3) komisi.setKomisi(0.12);
								else if(li_jwaktu==6) komisi.setKomisi(0.24);
								else if(li_jwaktu==12) komisi.setKomisi(0.48);
								else if(li_jwaktu==36) komisi.setKomisi(1.18);
								else komisi.setKomisi(0.0);
								
							//POWERSAVE BULANAN YANG BUKAN FINANCIAL PLANNER & BUKAN BANK Sinarmas (YUSUF - 25/08/2006)
							}else if(komisi.getBisnis_id()==158 && komisi.getBisnis_no() < 5 && !ls_region.startsWith("0916")) {
								int li_jwaktu = uwDao.selectMgiNewBusiness(spaj);

								if(li_jwaktu==3) komisi.setKomisi(0.12);
								else if(li_jwaktu==6) komisi.setKomisi(0.24);
								else if(li_jwaktu==12) komisi.setKomisi(0.48);
								else if(li_jwaktu==36) komisi.setKomisi(1.18);
								else komisi.setKomisi(0.0);
								
							//HORISON, KOMISI DIHITUNG DARI (100 - MST_INSURED.MSTE_PCT_DPLK) * MST_PRODUCT_INSURED.MSPR_PREMIUM
							//KARENA MSPR_PREMIUM = TOTAL DARI BIAYA DPLK + PREMI, DAN PERSENTASENYA ADA DI MST_INSURED.MSTE_PCT_DPLK
							//(YUSUF - 22/09/2006)
							}else if(komisi.getBisnis_id()==158 && (komisi.getBisnis_no().intValue()==13 || komisi.getBisnis_no().intValue()==16 )) {
//								int li_jwaktu = uwDao.selectMgiNewBusiness(spaj);
//								double ldec_temp = komisi.getPremi();
//								if(li_jwaktu==3) komisi.setKomisi(0.05);
//								else if(li_jwaktu==6) komisi.setKomisi(0.1);
//								else if(li_jwaktu==12) komisi.setKomisi(0.2);
//								else komisi.setKomisi(0.0);
								
//								SK. Direksi No. 086/AJS-SK/VII/2009
								komisi.setKomisi(0.58);
							}else if(komisi.getBisnis_id()==149) {
								Map dplk = uwDao.selectPersentaseDplk(spaj);
								ldec_premi = (Double) dplk.get("premi");
							}
						}
						
					}
					
					/** komisi = persentase komisi * premi **/
					if(pribadi.get("PRIBADI").toString().equals("1")){
						komisi.setKomisi(new Double(0));
						ldec_komisi = new Double(0);
					}else{
						if(ls_region.substring(0,2).equals("09") && komisi.getBisnis_id()!=69){
							if(komisi.getBisnis_id()==51 && !ls_region.substring(0,4).equals("0905")){
								//jika cara bayar sekaligus
								if(komisi.getCb_asli()==0)komisi.setKomisi(new Double(50));
								else{
									komisi.setKomisi(new Double(50));
									if(pribadi.get("MSPO_KOMISI_BII").toString().equals("3"))komisi.setKomisi(new Double(25));
								}
							}else if(komisi.getBisnis_id()==51 && ls_region.substring(0,4).equals("0905")){
								li_fo = Integer.parseInt(pribadi.get("MSPO_FOLLOW_UP").toString());
								if(li_fo==2){
									if(komisi.getLev_kom()==4){
										komisi.setKomisi(new Double(35 * 0.25));
									}else if(komisi.getLev_kom()==3){
										komisi.setKomisi(new Double(22.5));
									}else{
										komisi.setKomisi(new Double(0));
									}
								}else if(li_fo==3){
									if(komisi.getLev_kom()==4){
										komisi.setKomisi(new Double(10));
									}else{
										komisi.setKomisi(new Double(0));
									}
								}else if(li_fo==1){
									komisi.setKomisi(new Double(0));
								}
							}else{
								//Ganti per 22 Oct 2001, Memo Yimmy Lesmana
								if(komisi.getCb_asli()==3)
									komisi.setKomisi(new Double(komisi.getKomisi()+komisi.getBonus()));
							}
							
							ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
							
						}else{
							//selain bancass, masuk sini
							
							// untuk Financial Planner (cab = '43') -- 19/04/2006 (RG)
							if(ls_region.startsWith("43")) {
								if(komisi.getLev_kom()==1 || komisi.getLev_kom()==2)
									komisi.setKomisi(new Double(0));
								else if(komisi.getLev_kom()==3)
									komisi.setKomisi(new Double(20));
							}
							
							//	komisi OR agent get agent 28/01/04 (hm)		
							if(i>0 && li_rekrut==2)
								komisi.setKomisi(new Double(komisi.getKomisi()/2));
							if(i==1 && li_rekrut==3)
								komisi.setKomisi(new Double(12.5));
							if(komisi.getCb_asli()==3){
								ldec_komisi = new Double(ldec_premi * (komisi.getKomisi() + komisi.getBonus()) / 100);
							}else{
								ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
							}
							
							
							if(ls_region.substring(0,2).equals("42")){
								if(financeDao.selectIsAgenCorporate(komisi.getAgent_id())==1 && komisi.getLev_kom().intValue()<3){
									ldec_komisi = 0.;
								}
								if(financeDao.selectIsAgenCorporate(komisi.getAgent_id())==1 && komisi.getLev_kom().intValue()==3){
									if(products.stableLink(komisi.getBisnis_id().toString()) || products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no()) || products.powerSave(komisi.getBisnis_id().toString()) ){
										ldec_komisi = 0.;
									}else{
										ldec_komisi =ldec_premi * 0.15;
									}
									
								}
								
								if(financeDao.selectIsAgenCorporate(komisi.getAgent_id())==0 && komisi.getLev_kom().intValue()==3 && financeDao.selectIsAgenKaryawan(komisi.getAgent_id())!=1 ){
									if(products.stableLink(komisi.getBisnis_id().toString()) || products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no()) || products.powerSave(komisi.getBisnis_id().toString()) ){
										ldec_komisi = 0.;
									}else{
										ldec_komisi =ldec_premi * 0.1;
									}
									
								}
								
								
								if(bulanProd >= 201002){ // Mulai bulan feb 2010 && merupakan worksite && msag_kry=1, maka OR tidak diberlakukan.
									if(komisi.getLev_kom().intValue() < 4 && financeDao.selectIsAgenKaryawan(komisi.getAgent_id())==1){
										ldec_komisi = 0.;
									}
								}
							}
						}

						//logger.info(ldec_komisi + " = " + ldec_premi + " * " + komisi.getKomisi() + " / 100"); 
						
						//Eka proteksi sekaligus
						//Jika cara bayar sekaligus (Himmia) 8/11/00
						if((komisi.getBisnis_id()==52 || komisi.getBisnis_id()==51) && komisi.getCb_asli()==0){
							ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
						
						//Khusus eka sarjana mandiri RP 3 & 5
						// Kalo premi RP > 10jt atau prestige club
						}else if(komisi.getBisnis_id()==63 && komisi.getKurs_id().equals("01") && (komisi.getBisnis_no()==2 || komisi.getBisnis_no()==3)){
							if(i==0 && (ldec_premi >=10000000 || li_club==1)){
								//3 tahun
								if(komisi.getBisnis_no()==2){
									komisi.setKomisi(new Double(7.5));
									if(komisi.getCb_asli()==3)komisi.setKomisi(new Double(12.5));
								}else{
									//5 tahun
									komisi.setKomisi(new Double(25));
									if(komisi.getCb_asli()==3)komisi.setKomisi(new Double(30));
								}
								ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
							}
						//Khusus eka sarjana mandiri yang baru (173)
						}else if(komisi.getBisnis_id()==173){
							if(i==0){
								komisi.setKomisi(5.);
								
								int autodebet = bacDao.selectFlagCC(spaj);
								//bila autodebet payroll
								if(autodebet == 4){
									komisi.setKomisi(7.5);
								}else{									
									if(komisi.getBisnis_no()==1){
										komisi.setKomisi(5.);
									}else if(komisi.getBisnis_no()==2 || komisi.getBisnis_no()==3){
										komisi.setKomisi(10.);
									}
								}
								
								ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
							}
						}else if(komisi.getBisnis_id()==180){//untuk produk prosaver (180)
							if(i==0){
								komisi.setKomisi(5.);
								int autodebet = bacDao.selectFlagCC(spaj);
								//bila autodebet payroll
								if(autodebet == 4){
									if(komisi.getBisnis_no().intValue()==1){
										komisi.setKomisi(17.5);
									}else if(komisi.getBisnis_no().intValue()==2){
										komisi.setKomisi(22.5);
									}else if(komisi.getBisnis_no().intValue()==3){
										komisi.setKomisi(22.5);
									}
								}else{									
									if(komisi.getBisnis_no().intValue()==1){
										komisi.setKomisi(20.0);
									}else if(komisi.getBisnis_no().intValue()==2){
										komisi.setKomisi(25.0);
									}else if(komisi.getBisnis_no().intValue()==3){
										komisi.setKomisi(25.0);
									}
									if(komisi.getCb_asli().intValue()==0){
										komisi.setKomisi(5.0);
									}
								}
							}
						}
						
						//untuk franchise
						if(komisi.getLev_kom()==4 && ls_region.substring(0,2).equals("14"))
							ldec_kom_temp = ldec_komisi;
						else if(komisi.getLev_kom()==3 && ls_region.substring(0,2).equals("14"))
							ldec_komisi = ldec_kom_temp;
						else if(ls_region.substring(0,2).equals("14"))
							ldec_komisi = new Double(0);
						
						//untuk call center
						if(ls_region.substring(0,2).equals("16")){
							if(komisi.getLev_kom()==4)ldec_kom_temp = ldec_komisi;
							ldec_komisi = new Double(ldec_komisi/2);
						}
					}
					// Khusus plan pa pti
					//if(komisi.getLev_kom()==4 && komisi.getBisnis_id()==73)ldec_komisi = new Double(10000);
					if(ldec_komisi==0 && !pribadi.get("PRIBADI").toString().equals("1") && komisi.getBisnis_id()!=203)continue;

					//kalau masih aktif
					if(komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
						
						//TAMBAHAN OLEH YUSUF UNTUK PRODUK2 SINGLE (IM No. 132/IM-DIR/XII/2007) - 16/01/2008
						ldec_komisi = cekLinkSingle(spaj, ldec_komisi, komisi.getLev_kom(), false);
						
						komisi.setTax(new Double(ldec_komisi * komisi.getTax() / 100));
						Date sysdate = commonDao.selectSysdateTruncated(0);
						if(komisi.getTax() > 0){
							komisi.setTax(f_load_tax(ldec_komisi, sysdate, komisi.getAgent_id()));
							//komisi.setTax(hitungPajakKomisi(ldec_komisi, sysdate, komisi.getAgent_id()));
						}
						double ad_sisa;
						ad_sisa = komisi.getTax()%25;
						if(ad_sisa!=0){
							
						}
						if(ls_region.substring(0,2).equals("42")){
							if(financeDao.selectIsAgenCorporate(komisi.getAgent_id())==1 && komisi.getLev_kom().intValue()<3){
								ldec_komisi = 0.;
							}
							if(financeDao.selectIsAgenCorporate(komisi.getAgent_id())==1 && komisi.getLev_kom().intValue()==3){
								if(products.stableLink(komisi.getBisnis_id().toString()) || products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no()) || products.powerSave(komisi.getBisnis_id().toString()) ){
									ldec_komisi = 0.;
								}else{
									ldec_komisi =ldec_premi * 0.15;
								}
							}
							if(bulanProd >= 201002){ // Mulai bulan feb 2010 && merupakan worksite && msag_kry=1, maka OR tidak diberlakukan.
								if(komisi.getLev_kom().intValue() < 4 && financeDao.selectIsAgenKaryawan(komisi.getAgent_id())==1){
									ldec_komisi = 0.;
								}
							}
						}
						
						komisi.setTax(FormatNumber.rounding(komisi.getTax() , true, 25));
						ldec_komisi  = FormatNumber.round(ldec_komisi , 0);
						if(komisi.getTax()==null)komisi.setTax(new Double(0));
						double persen = komisi.getKomisi();
						
						komisi.setKomisi(ldec_komisi);
						komisi.setNilai_kurs(new Double(ldec_kurs));
						komisi.setLus_id(currentUser.getLus_id());
						komisi.setMsbi_tahun_ke(new Integer(1));
						komisi.setMsbi_premi_ke(new Integer(1));
						komisi.setMsco_flag(new Integer(li_club));
						
						if(flag_telemarket==1){
							komisi.setKomisi(ldec_komisi/0.5);
						}
						
						//Proses rewards
						if(i==1 && komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
							Double ldec_reward;
							boolean lb_ada_reward=false;
							
							if(li_rekrut == 1){
							}else if(li_rekrut == 3) {
								if(rekruter != null) {//errors.reject("payment.noRecruiter", new Object[] {rekruter.get("MSRK_ID")}, "-"); throw new Exception(errors);}
									if(rekruter.get("NO_ACCOUNT")==null && rekruter.get("MSRK_AKTIF").toString().equals("1")) {errors.reject("payment.noRecruiter", new Object[] {rekruter.get("MSRK_ID")}, "-"); throw new Exception(errors);}
									else if(rekruter.get("NO_ACCOUNT").toString().equals("") && rekruter.get("MSRK_AKTIF").toString().equals("1")) {errors.reject("payment.noRecruiter", new Object[] {rekruter.get("MSRK_ID")}, "-"); throw new Exception(errors);}
									if(rekruter.get("MSRK_AKTIF").toString().equals("1")){
										lb_ada_reward=true;
									}
								}
							}
							
							if(products.stableLink(komisi.getBisnis_id().toString())) {
								lb_ada_reward = false;
							}
							
							if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())){
								lb_ada_reward = false;
							}
							
							if(ls_region.substring(0,2).equals("42")){
								lb_ada_reward = false;
							}
							
							if(komisi.getBisnis_id()==203){
								lb_ada_reward = true;
							}
							
							if(lb_ada_reward){
								Date ldt_prod = this.uwDao.selectProductionDate(spaj, new Integer(1), new Integer(1));
								if(FormatDate.isDateBetween(ldt_prod, defaultDateFormatReversed.parse("20050401"), defaultDateFormatReversed.parse("20050630")))
									ldec_reward = new Double(ldec_premi * 0.2);
								else if(ldt_tgl_aktif.compareTo(defaultDateFormatReversed.parse("20050701"))>=0 && FormatDate.isDateBetween(ldt_prod, defaultDateFormatReversed.parse("20050701"), defaultDateFormatReversed.parse("20050630")))
									ldec_reward = new Double(ldec_premi * 0.2);
								else if(ldt_tgl_aktif.compareTo(defaultDateFormatReversed.parse("20051001"))>=0 && FormatDate.isDateBetween(ldt_prod, defaultDateFormatReversed.parse("20051001"), defaultDateFormatReversed.parse("20051231")))
									ldec_reward = new Double(ldec_premi * 0.2);
								else if(ldt_tgl_aktif.compareTo(defaultDateFormatReversed.parse("20060101"))>=0 && FormatDate.isDateBetween(ldt_prod, defaultDateFormatReversed.parse("20060101"), defaultDateFormatReversed.parse("20060630")))
									ldec_reward = new Double(ldec_premi * 0.2);
								else
									ldec_reward = new Double(ldec_premi * 0.1);
								
								Rekruter rek = uwDao.selectRekruterFromAgen(rekruter.get("MSRK_ID").toString());
								if(rek.getMsag_bay() == null) rek.setMsag_bay(0);
								
								double pengali = 1;
								
								//Yusuf (22/12/2010) bila business partner, maka dapat reward hanya 5%
								if(rek.getLcalwk().startsWith("61")) pengali = 0.5;
								//Yusuf (23/03/09) - Per 1 April OR dan Reward tidak 35% lagi tapi full
								else if(isLinkSingle(spaj, false) && bulanProd < 200904) pengali = 0.35;
								else if(isEkalink88Plus(spaj, false)) pengali = 0.7;
								ldec_reward *= pengali;
								
								komisi.setTax(f_load_tax(ldec_reward, sysdate, rekruter.get("MSRK_ID").toString()));
								
								ldec_reward = FormatNumber.round(ldec_reward, 0);
								komisi.setTax(FormatNumber.rounding(komisi.getTax(), true, 25));
								rekruter.put("NO_ACCOUNT", StringUtils.replace(rekruter.get("NO_ACCOUNT").toString(), "-",""));
								
								if("183,189,193,195,201,204".indexOf(komisi.getBisnis_id().toString()) <=-1){
									if(rekruter.get("MSAG_KOMISI")!=null){
										if(rekruter.get("MSAG_KOMISI").toString().equals("1") && rek.getMsag_bay().intValue() != 1){
											if(uwDao.selectbacCekAgen(rekruter.get("MSRK_ID").toString())!=null){
												List<Billing> billing = akseptasiDao.selectMstBilling(spaj, 1, 1);
												if(billing.get(0).getLspd_id() == 99){
													Date reward_pay_date = commonDao.selectSysdateTruncated(1);
													String msco_no = this.uwDao.selectMscoNoByMonth(rekruter.get("MSRK_ID").toString().trim(), "092014");
													if(Common.isEmpty(msco_no)){
														msco_no = sequence.sequenceMscoNoCommission(101, "01");
													}
													this.uwDao.insertMst_rewardKetinggalan(spaj, 1, 1, rekruter.get("MSRK_JENIS_REKRUT").toString(), rekruter.get("MSRK_ID").toString(),
															rekruter.get("MSRK_NAME").toString(), rekruter.get("NO_ACCOUNT").toString(), 
															rekruter.get("LBN_ID").toString(), ldec_reward, komisi.getTax(), new Double(ldec_kurs), currentUser.getLus_id(), komisi.getLsbs_linebus(),reward_pay_date,reward_pay_date,msco_no);
												}else{
													this.uwDao.insertMst_reward(spaj, 1, 1, rekruter.get("MSRK_JENIS_REKRUT").toString(), rekruter.get("MSRK_ID").toString(),
															rekruter.get("MSRK_NAME").toString(), rekruter.get("NO_ACCOUNT").toString(), 
															rekruter.get("LBN_ID").toString(), ldec_reward, komisi.getTax(), new Double(ldec_kurs), currentUser.getLus_id(), komisi.getLsbs_linebus());
													emailMessage.append("[REWARD] " + spaj + " : " + nf.format(ldec_reward)+ "\n");
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean prosesKomisiIndividu(String spaj, User currentUser, BindException errors) throws Exception{ //of_komisi_new_lg()
		if(logger.isDebugEnabled())logger.debug("PROSES: prosesKomisi (of_komisi_new_lg)");
		StringBuffer emailMessage = new StringBuffer();
		emailMessage.append("[SPAJ  ] " + spaj + "\n");
		NumberFormat nf = NumberFormat.getInstance();
		List detail = uwDao.selectDetailBisnis(spaj);
		if(!detail.isEmpty()) {
			Map det = (HashMap) detail.get(0);
			emailMessage.append(
					"[PRODUK] (" + det.get("BISNIS") + ") " + det.get("LSBS_NAME") + " - " + 
					"(" + det.get("DETBISNIS") + ") " + det.get("LSDBS_NAME") + "\n");
		}

		boolean lb_deduct=false;
		String businessId = FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 2);
		Class.forName("produk_asuransi.n_prod_"+businessId).newInstance();
		double ldec_kurs = 1; 
		Map pribadi = this.uwDao.selectInfoPribadi(spaj);
		String ls_region = (String) pribadi.get("REGION");
		int li_fo = ((Integer) pribadi.get("FOLLOW_UP"));
		String prodDate = (String) pribadi.get("PROD_DATE");
		prodDate = (prodDate == null ? "0" : prodDate);
		Date ldt_tgl_aktif = null;
		
		int li_dplk=0;
		int li_club=0;
		int li_rekrut=0;
		
		String lca_id = ls_region.substring(0,2);
		String lwk_id = ls_region.substring(2,4);
		
		int flag_telemarket = uwDao.selectgetFlagTelemarketing(spaj);
		
		/* Pembagian cara perhitungan komisi berdasarkan distribusi masing2 */
		if(ls_region.startsWith("0914")) {
			prosesKomisiBancAss(spaj, currentUser, errors);
			return false;
		}else if("37,46,52,67".indexOf(lca_id)>-1){ //Agency) { //37 agency, 46 arthamas, 52 Agency Arthamas (Deddy 22 Feb 2012), 67 Worksite Agency (Ryan 16 Apr 2015)
			Pemegang pp = bacDao.selectpp(spaj);
			if ((Integer) pp.getMsag_asnew()==1){
				prosesKomisiNewAgencySystem(spaj,currentUser,errors,1);
			}else{
				prosesKomisiAgencySystem(spaj, currentUser, errors, lca_id);
			}
			return false;
		//Yusuf (8/7/08) Proses Komisi Cross-Selling
		}else if(lca_id.equals("55")) {
			prosesKomisiCrossSelling(spaj, currentUser, errors, 1);
			return true; //true menandakan > 2007
		}else if(lca_id.equals("58")){
			prosesKomisiMallAssuranceSystem(spaj, currentUser, errors, lca_id);
			return true;
		}else if(lca_id.equals("60")) {
			prosesKomisiBridge(spaj, currentUser, errors, 1);
			return false; //true menandakan > 2007
		}else if(lca_id.equals("62")){
//			prosesKomisiMNC(spaj,currentUser,errors,1);
			return false;
		}else if(lca_id.equals("68")){//akan diupdate memakai flag, dan lca_id tetap seperti agency existing
			prosesKomisiNewAgencySystem(spaj,currentUser,errors,1);
			return false;
		}else if(lca_id.equals("73")) {// ERBE			
//			Integer li_row = this.bacDao.selectBillingCount(spaj, 1);			
//			for(int premke= 1; premke<= li_row; premke++){
//				prosesKomisiErbePackageSystem(spaj, currentUser, errors , 1, premke , 0);			
//							
//			}	
			return true; //true menandakan > 2007					
		}else if(Integer.parseInt(prodDate) > 2006 && !ls_region.startsWith("42")) {
			//}else if(Integer.parseInt(prodDate) > 1980 || Integer.parseInt(prodDate) == 0) {
				prosesKomisiIndividu2007(spaj, currentUser, errors);
				return true; //true menandakan > 2007
		}
		
		List lds_kom = this.uwDao.selectViewKomisiAsli(spaj);
		if(!lds_kom.isEmpty()) {
			Commission kms = (Commission) lds_kom.get(0);
			if(kms.getKurs_id().equals("02")){
				ldec_kurs = this.uwDao.selectLastCurrency(spaj, 1, 1);
				if(ldec_kurs<0){
					errors.reject("payment.noLastCurrency");
					return false;
				}
			}else ldec_kurs = 1;

			double ldec_premi = 0;
			Double ldec_komisi = null;
			Map<String, Object> rekruter = null;
			Double ldec_kom_temp = null;
			
			for(int i=0; i<lds_kom.size(); i++) {
				//awal for
				li_dplk=0;
				li_club=0;
				
				Commission komisi = (Commission) lds_kom.get(i);
				komisi.setLsbs_linebus(bacDao.selectLineBusLstBisnis(komisi.getBisnis_id().toString()));
				komisi.setFlag_mess(Boolean.TRUE);
				if("183,189,193,195,201,204".indexOf(komisi.getBisnis_id().toString()) >-1){
					komisi.setPremi(bacDao.selectTotalKomisiEkaSehat(spaj));
				}
				if(i==0) ldec_premi = FormatNumber.round(komisi.getPremi() * ldec_kurs, 2);
				int li_cb_id = komisi.getCb_id();
				if(!products.progressiveLink(komisi.getBisnis_id().toString()) && !products.stableSavePremiBulanan(komisi.getBisnis_id().toString()) && !products.healthProductStandAlone(komisi.getBisnis_id().toString())){
					if( li_cb_id==1 || li_cb_id==2 || li_cb_id==6 ) komisi.setCb_id(3);
				}
				komisi.setCb_id(new Integer(li_cb_id));
				if(komisi.getBisnis_id() == 64 ) komisi.setBisnis_id(new Integer(56));
				else if(komisi.getBisnis_id() == 67 ) komisi.setBisnis_id(new Integer(54));
				komisi.setLsco_jenis(new Integer(1)); //Commission individu
				int bulanProd = Integer.parseInt(uwDao.selectBulanProduksi(spaj));
				getLamaTanggung(komisi);
				
				if(f_get_komisi(komisi, errors, 1, 1, komisi.getKurs_id(), komisi.getPremi(), 4)==true){
					Double jmlKom = komisi.getKomisi()==null?new Double(0):komisi.getKomisi();
					if(jmlKom==0){
//						if(!(komisi.getLev_kom()==4 && komisi.getBisnis_id()==73)){
//							continue;
//						}
					}

					Map prestige = this.uwDao.selectPrestigeClub(komisi.getAgent_id(), komisi.getTgl_kom());
					if(prestige!=null){
						li_club = Integer.parseInt(prestige.get("MSAC_AKTIF").toString());
						li_dplk = Integer.parseInt(prestige.get("MSAC_DPLK").toString());
					}
					
					if(komisi.getLev_kom()==4){
						
						String kurs = komisi.getKurs_id();
						
						rekruter = this.uwDao.selectRekruter(komisi.getAgent_id());
						if(rekruter!=null){//rekrutan
							//O.R 50%
							if(rekruter.get("MSRK_JENIS_REKRUT").toString().equals("3") ||
									rekruter.get("MSRK_JENIS_REKRUT").toString().equals("4"))li_rekrut=2;
							ldt_tgl_aktif = this.uwDao.selectAgentActiveDate(komisi.getAgent_id());

							//Untuk tambang emas th 2004, kalo masih aktif; or 50% semua, kalo tdk um 12.5, 10% reward buat um terakhir
							// u/ tambang emas baru per 1 mar 05, or um = 12.5%, yg lain sama
							if(ldt_tgl_aktif.compareTo(defaultDateFormatReversed.parse("20050301"))>=0)
								li_rekrut=3;
							else {
								//te 2004, masih berlaku, or 50% semua
								if(FormatDate.dateDifference(ldt_tgl_aktif, komisi.getTgl_kom(), true)<=365)li_rekrut = 2;
								else li_rekrut = 0;
							}
						}
						
						if(products.powerSave(komisi.getBisnis_id().toString())){
							int mgi = this.uwDao.selectMgiNewBusiness(spaj);
							TopUp t = (TopUp) this.uwDao.selectTopUp(spaj, 1, 1, "desc").get(0);
							Date ldt_tgl_rk = t.getMspa_date_book();
							if(ldt_tgl_rk.compareTo(defaultDateFormatReversed.parse("20111116"))< 0 ){//request TRI, berdasarkan tgl RK apabila tgl RK di bawah tgl 16 nov 2011, ambil komisi lama
								komisi.setTgl_kom(ldt_tgl_rk);
							}
							Map komisiPowersave = uwDao.selectKomisiPowersave(1, 
									komisi.getBisnis_id(), komisi.getBisnis_no(), 
									mgi, komisi.getKurs_id(), komisi.getLev_kom(), 1, komisi.getPremi(), 0, komisi.getTgl_kom());

							if(komisiPowersave == null) {
								errors.reject("payment.noCommission", 
										new Object[]{
										   defaultDateFormat.format(komisi.getTgl_kom()),
										   komisi.getBisnis_id()+"-"+komisi.getBisnis_no(),
										   komisi.getCb_id(), 
										   komisi.getIns_period(), 
										   komisi.getKurs_id(), 
										   komisi.getLev_kom(),
										   komisi.getTh_kom(),
										   komisi.getLsco_jenis()
								           }, 
										"Commission POWERSAVE Tidak Ada.<br>- Tanggal: {0}<br>-Kode Bisnis: {1}<br>-Cara Bayar: {2}<br>-Ins_per: {3}<br>-Kurs: {4}<br>-Level: {5}<br>-Tahun Ke{6}<br>-Jenis: {7}");
								throw new Exception(errors);
							}else {
								if(komisiPowersave.get("LSCP_COMM")!=null) komisi.setKomisi(Double.valueOf(komisiPowersave.get("LSCP_COMM").toString()));
								if(komisiPowersave.get("LSCP_BONUS")!=null) komisi.setBonus(Double.valueOf(komisiPowersave.get("LSCP_BONUS").toString()));
							}
						}else{
							// li_rekrut = 0 -> tdk aktif
							// li_rekrut = 1 -> um 12.5%, reward um 10%
							// li_rekrut = 2 -> or = 50%
							// li_rekrut = 3 -> um 12.5%, reward rekruter 10%
							
							if(komisi.getBisnis_id()==123 && komisi.getKurs_id().equals("01")){
								TopUp t = (TopUp) this.uwDao.selectTopUp(spaj, 1, 1, "desc").get(0);
								Date ldt_tgl_rk = t.getMspa_date_book();
								if(ldt_tgl_rk.compareTo(defaultDateFormatReversed.parse("20050908"))>=0 && ldt_tgl_rk.compareTo(defaultDateFormatReversed.parse("20051115"))<=0){
									int li_waktu = 0;
									Integer wkt = this.uwDao.selectMgiNewBusiness(spaj);
									if(wkt!=null) li_waktu = wkt;
									if(li_waktu==6)komisi.setKomisi(new Double(0.2));
									else if(li_waktu==12 || li_waktu==24)komisi.setKomisi(new Double(0.4));
									else komisi.setKomisi(new Double(0));
								}
							}
//							Deddy - Semua produk stable save, komisinya diset 0.58
							if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())){
								komisi.setKomisi(0.58);
							}
							//POWERSAVE INDIVIDU
							else if(komisi.getBisnis_id()==143) {
								int li_jwaktu = this.uwDao.selectMgiNewBusiness(spaj);
								//if(li_jwaktu==1) komisi.setKomisi(new Double(0.02)); hanya saat rollover, bukan new business
								
								if(li_jwaktu==3) komisi.setKomisi(new Double(0.05));
								else if(li_jwaktu==6) komisi.setKomisi(new Double(0.1));
								else if(li_jwaktu==12) komisi.setKomisi(new Double(0.2));
								else if(li_jwaktu==36) komisi.setKomisi(new Double(0.6));
								else komisi.setKomisi(new Double(0));
								
								//(Deddy 28/7/2009) SK Direksi No.079/AJS-SK/VII/2009
								if(komisi.getBisnis_no().intValue()==1 || komisi.getBisnis_no().intValue()==2 ||komisi.getBisnis_no().intValue()==3){
									if(li_jwaktu==3) komisi.setKomisi(new Double(0.063));
									else if(li_jwaktu==6) komisi.setKomisi(new Double(0.125));
									else if(li_jwaktu==12) komisi.setKomisi(new Double(0.25));
									else if(li_jwaktu==36) komisi.setKomisi(new Double(0.75));
									else komisi.setKomisi(new Double(0));
									// 027/IM-DIR/IV/2011 Tertanggal 11 April 2011 perubahan komisi untuk powersave dan stable link.
									if(bulanProd>=201105){
										if(li_jwaktu==1) komisi.setKomisi(0.05);
										else if(li_jwaktu==3) komisi.setKomisi(0.15);
										else if(li_jwaktu==6) komisi.setKomisi(0.3);
										else if(li_jwaktu==12) komisi.setKomisi(0.6);
										else if(li_jwaktu==36) komisi.setKomisi(1.35);
										else komisi.setKomisi(new Double(0));
									}
									
								}

//								(Deddy) Perubahan perhitungan komisi sesuai  SK.Direksi No. 068/AJS-SK/VI/2009
//								perhitungan yang lama berdasarkan MGI.
								if(komisi.getBisnis_no().intValue()==4){
									komisi.setKomisi(0.58);
								}else if(komisi.getBisnis_no().intValue()==5){
									//komisi.setKomisi(2.8); // KHUSUS BII (SK. Direksi No. 060/AJS-SK/V/2009)
									komisi.setKomisi(2.5);//(Deddy - 28/7/2009)berlaku tgl : 1 agustus 2009 berdasarkan SK. Direksi No. 088/AJS-SK/VII/2009
								}
								
								
							//POWERSAVE BULANAN YANG WORKSITE & BUKAN BANK Sinarmas (YUSUF - 25/08/2006)
							}else if(komisi.getBisnis_id()==158 && komisi.getBisnis_no() < 5 && ls_region.startsWith("42")) {
								int li_jwaktu = uwDao.selectMgiNewBusiness(spaj);
								
								if(li_jwaktu==3) komisi.setKomisi(0.12);
								else if(li_jwaktu==6) komisi.setKomisi(0.24);
								else if(li_jwaktu==12) komisi.setKomisi(0.48);
								else if(li_jwaktu==36) komisi.setKomisi(1.18);
								else komisi.setKomisi(0.0);
								
							//POWERSAVE BULANAN YANG BUKAN FINANCIAL PLANNER & BUKAN BANK Sinarmas (YUSUF - 25/08/2006)
							}else if(komisi.getBisnis_id()==158 && komisi.getBisnis_no() < 5 && !ls_region.startsWith("0916")) {
								int li_jwaktu = uwDao.selectMgiNewBusiness(spaj);

								if(li_jwaktu==3) komisi.setKomisi(0.12);
								else if(li_jwaktu==6) komisi.setKomisi(0.24);
								else if(li_jwaktu==12) komisi.setKomisi(0.48);
								else if(li_jwaktu==36) komisi.setKomisi(1.18);
								else komisi.setKomisi(0.0);
								
							//HORISON, KOMISI DIHITUNG DARI (100 - MST_INSURED.MSTE_PCT_DPLK) * MST_PRODUCT_INSURED.MSPR_PREMIUM
							//KARENA MSPR_PREMIUM = TOTAL DARI BIAYA DPLK + PREMI, DAN PERSENTASENYA ADA DI MST_INSURED.MSTE_PCT_DPLK
							//(YUSUF - 22/09/2006)
							}else if(komisi.getBisnis_id()==158 && (komisi.getBisnis_no().intValue()==13 || komisi.getBisnis_no().intValue()==16 )) {
//								int li_jwaktu = uwDao.selectMgiNewBusiness(spaj);
//								double ldec_temp = komisi.getPremi();
//								if(li_jwaktu==3) komisi.setKomisi(0.05);
//								else if(li_jwaktu==6) komisi.setKomisi(0.1);
//								else if(li_jwaktu==12) komisi.setKomisi(0.2);
//								else komisi.setKomisi(0.0);
								
//								SK. Direksi No. 086/AJS-SK/VII/2009
								komisi.setKomisi(0.58);
							}else if(komisi.getBisnis_id()==149) {
								Map dplk = uwDao.selectPersentaseDplk(spaj);
								ldec_premi = (Double) dplk.get("premi");
							}
						}
						
					}
					
					/** komisi = persentase komisi * premi **/
					if(pribadi.get("PRIBADI").toString().equals("1")){
						komisi.setKomisi(new Double(0));
						ldec_komisi = new Double(0);
					}else{
						if(ls_region.substring(0,2).equals("09") && komisi.getBisnis_id()!=69){
							if(komisi.getBisnis_id()==51 && !ls_region.substring(0,4).equals("0905")){
								//jika cara bayar sekaligus
								if(komisi.getCb_asli()==0)komisi.setKomisi(new Double(50));
								else{
									komisi.setKomisi(new Double(50));
									if(pribadi.get("MSPO_KOMISI_BII").toString().equals("3"))komisi.setKomisi(new Double(25));
								}
							}else if(komisi.getBisnis_id()==51 && ls_region.substring(0,4).equals("0905")){
								li_fo = Integer.parseInt(pribadi.get("MSPO_FOLLOW_UP").toString());
								if(li_fo==2){
									if(komisi.getLev_kom()==4){
										komisi.setKomisi(new Double(35 * 0.25));
									}else if(komisi.getLev_kom()==3){
										komisi.setKomisi(new Double(22.5));
									}else{
										komisi.setKomisi(new Double(0));
									}
								}else if(li_fo==3){
									if(komisi.getLev_kom()==4){
										komisi.setKomisi(new Double(10));
									}else{
										komisi.setKomisi(new Double(0));
									}
								}else if(li_fo==1){
									komisi.setKomisi(new Double(0));
								}
							}else{
								//Ganti per 22 Oct 2001, Memo Yimmy Lesmana
								if(komisi.getCb_asli()==3)
									komisi.setKomisi(new Double(komisi.getKomisi()+komisi.getBonus()));
							}
							
							ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
							
						}else{
							//selain bancass, masuk sini
							
							// untuk Financial Planner (cab = '43') -- 19/04/2006 (RG)
							if(ls_region.startsWith("43")) {
								if(komisi.getLev_kom()==1 || komisi.getLev_kom()==2)
									komisi.setKomisi(new Double(0));
								else if(komisi.getLev_kom()==3)
									komisi.setKomisi(new Double(20));
							}
							
							//	komisi OR agent get agent 28/01/04 (hm)		
							if(i>0 && li_rekrut==2)
								komisi.setKomisi(new Double(komisi.getKomisi()/2));
							if(i==1 && li_rekrut==3)
								komisi.setKomisi(new Double(12.5));
							if(komisi.getCb_asli()==3){
								ldec_komisi = new Double(ldec_premi * (komisi.getKomisi() + komisi.getBonus()) / 100);
							}else{
								ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
							}
							
							
							if(ls_region.substring(0,2).equals("42")){
								if(financeDao.selectIsAgenCorporate(komisi.getAgent_id())==1 && komisi.getLev_kom().intValue()<3){
									ldec_komisi = 0.;
								}
								if(financeDao.selectIsAgenCorporate(komisi.getAgent_id())==1 && komisi.getLev_kom().intValue()==3){
									if(products.stableLink(komisi.getBisnis_id().toString()) || products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no()) || products.powerSave(komisi.getBisnis_id().toString()) ){
										ldec_komisi = 0.;
									}else{
										ldec_komisi =ldec_premi * 0.15;
									}
									
								}
								
								if(financeDao.selectIsAgenCorporate(komisi.getAgent_id())==0 && komisi.getLev_kom().intValue()==3 && financeDao.selectIsAgenKaryawan(komisi.getAgent_id())!=1 ){
									if(products.stableLink(komisi.getBisnis_id().toString()) || products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no()) || products.powerSave(komisi.getBisnis_id().toString()) ){
										ldec_komisi = 0.;
									}else{
										ldec_komisi =ldec_premi * 0.1;
									}
									
								}
								
								
								if(bulanProd >= 201002){ // Mulai bulan feb 2010 && merupakan worksite && msag_kry=1, maka OR tidak diberlakukan.
									if(komisi.getLev_kom().intValue() < 4 && financeDao.selectIsAgenKaryawan(komisi.getAgent_id())==1){
										ldec_komisi = 0.;
									}
								}
							}
						}

						//logger.info(ldec_komisi + " = " + ldec_premi + " * " + komisi.getKomisi() + " / 100"); 
						
						//Eka proteksi sekaligus
						//Jika cara bayar sekaligus (Himmia) 8/11/00
						if((komisi.getBisnis_id()==52 || komisi.getBisnis_id()==51) && komisi.getCb_asli()==0){
							ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
						
						//Khusus eka sarjana mandiri RP 3 & 5
						// Kalo premi RP > 10jt atau prestige club
						}else if(komisi.getBisnis_id()==63 && komisi.getKurs_id().equals("01") && (komisi.getBisnis_no()==2 || komisi.getBisnis_no()==3)){
							if(i==0 && (ldec_premi >=10000000 || li_club==1)){
								//3 tahun
								if(komisi.getBisnis_no()==2){
									komisi.setKomisi(new Double(7.5));
									if(komisi.getCb_asli()==3)komisi.setKomisi(new Double(12.5));
								}else{
									//5 tahun
									komisi.setKomisi(new Double(25));
									if(komisi.getCb_asli()==3)komisi.setKomisi(new Double(30));
								}
								ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
							}
						//Khusus eka sarjana mandiri yang baru (173)
						}else if(komisi.getBisnis_id()==173){
							if(i==0){
								komisi.setKomisi(5.);
								
								int autodebet = bacDao.selectFlagCC(spaj);
								//bila autodebet payroll
								if(autodebet == 4){
									komisi.setKomisi(7.5);
								}else{									
									if(komisi.getBisnis_no()==1){
										komisi.setKomisi(5.);
									}else if(komisi.getBisnis_no()==2 || komisi.getBisnis_no()==3){
										komisi.setKomisi(10.);
									}
								}
								
								ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
							}
						}else if(komisi.getBisnis_id()==180){//untuk produk prosaver (180)
							if(i==0){
								komisi.setKomisi(5.);
								int autodebet = bacDao.selectFlagCC(spaj);
								//bila autodebet payroll
								if(autodebet == 4){
									if(komisi.getBisnis_no().intValue()==1){
										komisi.setKomisi(17.5);
									}else if(komisi.getBisnis_no().intValue()==2){
										komisi.setKomisi(22.5);
									}else if(komisi.getBisnis_no().intValue()==3){
										komisi.setKomisi(22.5);
									}
								}else{									
									if(komisi.getBisnis_no().intValue()==1){
										komisi.setKomisi(20.0);
									}else if(komisi.getBisnis_no().intValue()==2){
										komisi.setKomisi(25.0);
									}else if(komisi.getBisnis_no().intValue()==3){
										komisi.setKomisi(25.0);
									}
									if(komisi.getCb_asli().intValue()==0){
										komisi.setKomisi(5.0);
									}
								}
							}
						}
						
						//untuk franchise
						if(komisi.getLev_kom()==4 && ls_region.substring(0,2).equals("14"))
							ldec_kom_temp = ldec_komisi;
						else if(komisi.getLev_kom()==3 && ls_region.substring(0,2).equals("14"))
							ldec_komisi = ldec_kom_temp;
						else if(ls_region.substring(0,2).equals("14"))
							ldec_komisi = new Double(0);
						
						//untuk call center
						if(ls_region.substring(0,2).equals("16")){
							if(komisi.getLev_kom()==4)ldec_kom_temp = ldec_komisi;
							ldec_komisi = new Double(ldec_komisi/2);
						}
					}
					// Khusus plan pa pti
					//if(komisi.getLev_kom()==4 && komisi.getBisnis_id()==73)ldec_komisi = new Double(10000);
					if(ldec_komisi==0 && !pribadi.get("PRIBADI").toString().equals("1") && komisi.getBisnis_id()!=203)continue;

					//kalau masih aktif
					if(komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
						
						komisi.setCo_id(sequenceMst_commission(11));
						
						//TAMBAHAN OLEH YUSUF UNTUK PRODUK2 SINGLE (IM No. 132/IM-DIR/XII/2007) - 16/01/2008
						ldec_komisi = cekLinkSingle(spaj, ldec_komisi, komisi.getLev_kom(), false);
						
						komisi.setTax(new Double(ldec_komisi * komisi.getTax() / 100));
						Date sysdate = commonDao.selectSysdateTruncated(0);
						if(komisi.getTax() > 0){
							komisi.setTax(f_load_tax(ldec_komisi, sysdate, komisi.getAgent_id()));
							//komisi.setTax(hitungPajakKomisi(ldec_komisi, sysdate, komisi.getAgent_id()));
						}
						double ad_sisa;
						ad_sisa = komisi.getTax()%25;
						if(ad_sisa!=0){
							
						}
						if(ls_region.substring(0,2).equals("42")){
							if(financeDao.selectIsAgenCorporate(komisi.getAgent_id())==1 && komisi.getLev_kom().intValue()<3){
								ldec_komisi = 0.;
							}
							if(financeDao.selectIsAgenCorporate(komisi.getAgent_id())==1 && komisi.getLev_kom().intValue()==3){
								if(products.stableLink(komisi.getBisnis_id().toString()) || products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no()) || products.powerSave(komisi.getBisnis_id().toString()) ){
									ldec_komisi = 0.;
								}else{
									ldec_komisi =ldec_premi * 0.15;
								}
							}
							if(bulanProd >= 201002){ // Mulai bulan feb 2010 && merupakan worksite && msag_kry=1, maka OR tidak diberlakukan.
								if(komisi.getLev_kom().intValue() < 4 && financeDao.selectIsAgenKaryawan(komisi.getAgent_id())==1){
									ldec_komisi = 0.;
								}
							}
						}
						
						komisi.setTax(FormatNumber.rounding(komisi.getTax() , true, 25));
						ldec_komisi  = FormatNumber.round(ldec_komisi , 0);
						if(komisi.getTax()==null)komisi.setTax(new Double(0));
						double persen = komisi.getKomisi();
						
						//referensi point(tambang emas) -> Premi Pokok
						/*logger.info("Referensi");
						Integer cek = uwDao.seleckCekRef(spaj,"1");
						if(cek>0){
							String lsbs_id = uwDao.selectLsbsId(spaj);
							if(lca_id.equals("42") && lsbs_id.equals("140")){
								double komisi_ref = ldec_komisi/2;
								komisi.setKomisi(ldec_komisi);//komisi tetap 100%
								double rate = uwDao.selectRatePoint(lsbs_id);
								int point = (int) (komisi_ref/rate);
								String id_trx = bacDao.selectIdTrx(null,spaj,"2");
								bacDao.insertPwrDTrx(id_trx, props.getProperty("id.item.point"), spaj, komisi_ref, point, currentUser.getLus_id());
								//insert ke mst_deduct 50% nya
							}else{
								komisi.setKomisi(ldec_komisi);
							}
						}else{
							komisi.setKomisi(ldec_komisi);
						}*/
						Integer cek = uwDao.seleckCekRef(spaj,"1");
						if(cek>0){
							String lsbs_id = uwDao.selectLsbsId(spaj);
							if(lca_id.equals("42") && lsbs_id.equals("140")){
								double komisi_ref = ldec_komisi/2;
								double rate = uwDao.selectRatePoint(lsbs_id);
								int point = (int) (komisi_ref/rate);
								String id_trx = bacDao.selectIdTrx(null,spaj,"2");
								bacDao.insertPwrDTrx(id_trx, props.getProperty("id.item.point"), spaj, komisi_ref, point, currentUser.getLus_id());
								//insert ke mst_deduct 50% nya
								this.uwDao.insertMst_deduct(komisi.getCo_id(), komisi_ref, currentUser.getLus_id(), 1, 10, "Komisi program MGM");
							}
						}
						//end referensi
						
						komisi.setKomisi(ldec_komisi);
						komisi.setNilai_kurs(new Double(ldec_kurs));
						komisi.setLus_id(currentUser.getLus_id());
						komisi.setMsbi_tahun_ke(new Integer(1));
						komisi.setMsbi_premi_ke(new Integer(1));
						komisi.setMsco_flag(new Integer(li_club));
						
						if(flag_telemarket==1){
							komisi.setKomisi(ldec_komisi/0.5);
						}
						
						if(komisi.getKomisi()>0) {
							if(komisi.getKomisi()>0) {
								//FIX ME: RYAN, Sekarang , SMiLe Medical, semua level dpt komisi , asal terdaftar di mst_agent_comm
								if(/*(("183,189,193,201".indexOf(komisi.getBisnis_id().toString())>-1) && komisi.getCb_asli().intValue() != 6) ||*/ ( komisi.getBisnis_id()==203)){//inidiedit
									if(komisi.getLev_kom()==4){
										this.uwDao.insertMst_commission(komisi, sysdate);
										emailMessage.append("[KOMISI] " + komisi.getLev_kom() + " : " + komisi.getAgent_id() + " : " + 
												nf.format(komisi.getKomisi()) + "\n");
									}else {
										
									}
								}else {
									this.uwDao.insertMst_commission(komisi, sysdate);
									emailMessage.append("[KOMISI] " + komisi.getLev_kom() + " : " + komisi.getAgent_id() + " : " + 
											nf.format(komisi.getKomisi()) + "\n");
								}
							}
							
							//Yusuf - 08/04/09 - Start 13 April, Bonus Komisi untuk Produk Save + StableLink dipisah ke comm_bonus
							if(!pribadi.get("PRIBADI").toString().equals("1")){
								if(i==0) {
									prosesBonusKomisi(
											"worksite", ls_region, komisi.getReg_spaj(), komisi.getAgent_id(), komisi.getCo_id(), 
											komisi.getKurs_id(), ldec_premi, ldec_kurs, currentUser.getLus_id(), komisi.getKomisi(), komisi.getTax(),
											komisi.getMsbi_tahun_ke(), komisi.getMsbi_premi_ke(), komisi.getLev_kom(), komisi.getLsbs_linebus());
								}
							}
							
						}
						
						//tambahan : Yusuf (29/04/08) ada proses komisi rider, mulai 1 may 2008
						prosesKomisiRider(komisi, i, persen);
						
						if(i==0){
							Map temp = this.uwDao.selectBillingRemain(komisi.getReg_spaj());
							double ldec_sisa = Double.parseDouble(temp.get("MSBI_REMAIN").toString());
							Integer li_flag = (Integer) temp.get("MSBI_FLAG_SISA");
							if(li_flag!=null){
								if(li_flag==2) {
									this.uwDao.insertMst_deduct(komisi.getCo_id(), ldec_sisa, currentUser.getLus_id(), 1, 3, "PREMI KURANG BAYAR");
									emailMessage.append("[DEDUCT] " + komisi.getCo_id() + " : " + nf.format(ldec_sisa)+ "\n");
									lb_deduct=true;
								}
							}
						}

						//Jika masuk prestige club, pot buat dplk
						if(li_club==1 && li_dplk==1){
							int li_deduct_no = 1;
							if(i==0 && lb_deduct)li_deduct_no=2;
							Double ldec_dplk = Double.valueOf(prestige.get("LPC_DPLK_K").toString());
							ldec_dplk = new Double((ldec_komisi - komisi.getTax()) * ldec_dplk / 100);
							ldec_dplk = FormatNumber.round(ldec_dplk,  0);
							this.uwDao.insertMst_deduct(komisi.getCo_id(), ldec_dplk, currentUser.getLus_id(), li_deduct_no, 5, "POTONGAN DPLK /U " + prestige.get("LPC_CLUB").toString() );
							emailMessage.append("[DEDUCT DPLK] " + komisi.getCo_id() + " : " + nf.format(ldec_dplk)+ "\n");
						}
						
						//Proses rewards
						if(i==1 && komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
							Double ldec_reward;
							boolean lb_ada_reward=false;
							
							if(li_rekrut == 1){
							}else if(li_rekrut == 3) {
								if(rekruter != null) {//errors.reject("payment.noRecruiter", new Object[] {rekruter.get("MSRK_ID")}, "-"); throw new Exception(errors);}
									if(rekruter.get("NO_ACCOUNT")==null && rekruter.get("MSRK_AKTIF").toString().equals("1")) {errors.reject("payment.noRecruiter", new Object[] {rekruter.get("MSRK_ID")}, "-"); throw new Exception(errors);}
									else if(rekruter.get("NO_ACCOUNT").toString().equals("") && rekruter.get("MSRK_AKTIF").toString().equals("1")) {errors.reject("payment.noRecruiter", new Object[] {rekruter.get("MSRK_ID")}, "-"); throw new Exception(errors);}
									if(rekruter.get("MSRK_AKTIF").toString().equals("1")){
										lb_ada_reward=true;
									}
								}
							}
							
							if(products.stableLink(komisi.getBisnis_id().toString())) {
								lb_ada_reward = false;
							}
							
							if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())){
								lb_ada_reward = false;
							}
							
							if(ls_region.substring(0,2).equals("42")){
								lb_ada_reward = false;
							}
							
							if(komisi.getBisnis_id()==203){
								lb_ada_reward = true;
							}
							
							if(lb_ada_reward){
								Date ldt_prod = this.uwDao.selectProductionDate(spaj, new Integer(1), new Integer(1));
								if(FormatDate.isDateBetween(ldt_prod, defaultDateFormatReversed.parse("20050401"), defaultDateFormatReversed.parse("20050630")))
									ldec_reward = new Double(ldec_premi * 0.2);
								else if(ldt_tgl_aktif.compareTo(defaultDateFormatReversed.parse("20050701"))>=0 && FormatDate.isDateBetween(ldt_prod, defaultDateFormatReversed.parse("20050701"), defaultDateFormatReversed.parse("20050630")))
									ldec_reward = new Double(ldec_premi * 0.2);
								else if(ldt_tgl_aktif.compareTo(defaultDateFormatReversed.parse("20051001"))>=0 && FormatDate.isDateBetween(ldt_prod, defaultDateFormatReversed.parse("20051001"), defaultDateFormatReversed.parse("20051231")))
									ldec_reward = new Double(ldec_premi * 0.2);
								else if(ldt_tgl_aktif.compareTo(defaultDateFormatReversed.parse("20060101"))>=0 && FormatDate.isDateBetween(ldt_prod, defaultDateFormatReversed.parse("20060101"), defaultDateFormatReversed.parse("20060630")))
									ldec_reward = new Double(ldec_premi * 0.2);
								else
									ldec_reward = new Double(ldec_premi * 0.1);
								
								Rekruter rek = uwDao.selectRekruterFromAgen(rekruter.get("MSRK_ID").toString());
								if(rek.getMsag_bay() == null) rek.setMsag_bay(0);
								
								double pengali = 1;
								
								//Yusuf (22/12/2010) bila business partner, maka dapat reward hanya 5%
								if(rek.getLcalwk().startsWith("61")) pengali = 0.5;
								//Yusuf (23/03/09) - Per 1 April OR dan Reward tidak 35% lagi tapi full
								else if(isLinkSingle(spaj, false) && bulanProd < 200904) pengali = 0.35;
								else if(isEkalink88Plus(spaj, false)) pengali = 0.7;
								ldec_reward *= pengali;
								
								komisi.setTax(f_load_tax(ldec_reward, sysdate, rekruter.get("MSRK_ID").toString()));
								
								ldec_reward = FormatNumber.round(ldec_reward, 0);
								komisi.setTax(FormatNumber.rounding(komisi.getTax(), true, 25));
								rekruter.put("NO_ACCOUNT", StringUtils.replace(rekruter.get("NO_ACCOUNT").toString(), "-",""));
								
								if("183,189,193,195,201,204".indexOf(komisi.getBisnis_id().toString()) <=-1){
									if(rekruter.get("MSAG_KOMISI")!=null){
										if(rekruter.get("MSAG_KOMISI").toString().equals("1") && rek.getMsag_bay().intValue() != 1){
											if(uwDao.selectbacCekAgen(rekruter.get("MSRK_ID").toString())!=null){
												this.uwDao.insertMst_reward(spaj, 1, 1, rekruter.get("MSRK_JENIS_REKRUT").toString(), rekruter.get("MSRK_ID").toString(),
														rekruter.get("MSRK_NAME").toString(), rekruter.get("NO_ACCOUNT").toString(), 
														rekruter.get("LBN_ID").toString(), ldec_reward, komisi.getTax(), new Double(ldec_kurs), currentUser.getLus_id(), komisi.getLsbs_linebus());
												emailMessage.append("[REWARD] " + spaj + " : " + nf.format(ldec_reward)+ "\n");
											}
										}
									}
								}
							}
						}
						
						//Untuk call center
						if(ls_region.substring(0,2).equals("16")) ldec_komisi = ldec_kom_temp;
						
					}
					
					if( i == 0 ) {
						ldec_premi = ldec_komisi;
					}
					
				}
				//end for
			}
			/** UNTUK DEBUGGING SAJA */
//			email.send(
//					new String[] {props.getProperty("admin.yusuf")}, null,
//					"Proses Komisi Individu [" + spaj + "]", emailMessage.toString(), currentUser);
		}
		
		return false;
	}
	
	private Double hitungPremiTahunan(String reg_spaj, BindException errors) throws Exception{
		if(logger.isDebugEnabled())logger.debug("PROSES: hitungPremiTahunan");
	
		Map map = this.uwDao.selectInsuredInfo(reg_spaj);
		Double hasil = Double.valueOf(map.get("MSPR_PREMIUM").toString());
		if(map.get("LSBS_ID").toString().equals("92")){
			hasil = new Double(Double.parseDouble(map.get("MSPR_PREMIUM").toString()) / 146 * 12);
		}else if(map.get("LSBS_ID").toString().equals("91")){
			Integer li_age = this.uwDao.selectAgeFromSPAJ(reg_spaj);
			Integer li_number = new Integer(Integer.valueOf(map.get("LSDBS_NUMBER").toString())+2);
			hasil = this.uwDao.selectPremiTahunan(map.get("LSBS_ID").toString(), li_number, li_age);
			if(hasil==null){
				errors.reject("payment.noAnnualPremium");
			}
		}
		return hasil;
	}

	/**
	 * Load Agent Tax, fungsi adopsi dari yosep punya f_load_agen_tax di PB
	 * 
	 * @param agentTax
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ParseException 
	 */
	private boolean f_load_agent_tax(AgentTax agentTax){
		AgentTax astr_tax = basDao.selectAgentTax(agentTax); 
		if(astr_tax == null){
			//int tot = basDao.selectCountAgentTax(agentTax);
			//if(tot > 0){
			//	agentTax.adt_date = uwDao.selectAddMonths(defaultDateFormat.format(agentTax.adt_date), -1);
			//	return f_load_agent_tax(agentTax);
			//}
			Date ldt_beg_year;
			Date ldt_beg_mon, ldt_end_mon;
			SimpleDateFormat df = new SimpleDateFormat();
			
			ldt_beg_year = commonDao.selectBeginOfYear(agentTax.adt_date);
			ldt_beg_mon  = agentTax.adt_date;
			try {
				ldt_beg_mon  =  commonDao.selectBeginOfMonth( df.parse(ldt_beg_mon.toString()));
			} catch (DataAccessException e1) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e1);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e1);
			} //f_bom
			ldt_end_mon  = agentTax.adt_date;
			
			agentTax = basDao.selectTotalAgentTax(agentTax, ldt_beg_mon, ldt_beg_year, ldt_end_mon);
			
			double ldec_ptkp = 0;
			
			ldec_ptkp = basDao.selectSumPtkp(agentTax.as_msag, ldt_beg_mon, ldt_beg_year);
			if(agentTax.adec_pkp_year==null){
				agentTax.adec_pkp_year=0.;
			}
			if(agentTax.adec_comm_month==null){
				agentTax.adec_comm_month=0.;
			}
			if(agentTax.adec_comm_year ==null){
				agentTax.adec_comm_year =0.;
			}
			if(agentTax.adec_pkp_month==null){
				agentTax.adec_pkp_month=0.;
			}
			
			
			agentTax.adec_pkp_year = agentTax.adec_pkp_year - ldec_ptkp;
			
			//(ys 2009.10.21) new tax calc, bruto count from 50% of commission, begin date 1 nov 2009
			Date nov2009 = null;
			try {
				nov2009 = defaultDateFormat.parse("01/11/2009");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			}
			if(agentTax.adt_date.compareTo(nov2009) >= 0){
				agentTax.adec_comm_month = agentTax.adec_comm_month * 0.5;
				agentTax.adec_comm_year  = agentTax.adec_comm_year  * 0.5;
				agentTax.adec_pkp_year   = agentTax.adec_pkp_year   * 0.5;				
				agentTax.adec_pkp_month  = agentTax.adec_pkp_month  * 0.5;
			}
			
		}else{
			try {
				PropertyUtils.copyProperties(agentTax, astr_tax);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			} //dest, orig
		}
		return true;
	}
	
	/**
	 * Fungsi untuk menghitung pajak komisi, diadopsi dari f_load_tax buatan Yosep untuk efektif per tgl 1 Nov 2009
	 * 
	 * @author Yusuf
	 * @since 16 Oct 2009
	 * @param adec_value Jumlah Nominal Komisi 
	 * @param adt_date Tanggal Komisi
	 * @param as_msag Kode Agen
	 * @return
	 * @throws Exception
	 */
	public double f_load_tax(double adec_value, Date adt_date, String as_msag){
		
		//*****************************************************************
		//-Usage :
		// Get Tax from spesific value based by year, and agent's npwp
		//
		//*****************************************************************
		double adec_tax			= 0;
		double ldec_pkp_year	= 0;
		double ldec_acum_last	= 0;
		double ldec_ptkp		= 0;
		double ldec_pkp			= 0;
		double ldec_tax			= 0;
		
		AgentTax lstr_tax = new AgentTax();
		SimpleDateFormat df = new SimpleDateFormat();
		try {
			adt_date  =  commonDao.selectBeginOfMonth( df.parse(df.format(adt_date)));
		} catch (DataAccessException e1) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e1);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e1);
		} //f_bom
		//adt_date = commonDao.selectBeginOfMonth(adt_date); //f_bom
		
		//HARUS - (ys 2009.10.16) new tax calc, bruto count from 50% of commission, begin date 1 nov 2009
		Date nov2009 = null;
		try {
			nov2009 = defaultDateFormat.parse("01/11/2009");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e);
		}
		
		//HARUS - 
		if(adt_date.compareTo(nov2009) >= 0){
			//(ys 2009.11.11) karyawan tidak kali 50%
			int li_counter = 1;
			li_counter = financeDao.selectIsAgenKaryawan(as_msag);
			if(li_counter == 0) li_counter = 2;
			adec_value = adec_value / li_counter;			
		}
		
		lstr_tax.adt_date = adt_date;
		lstr_tax.as_msag  = as_msag;
/*	
		if(!f_load_agent_tax(lstr_tax)) return 0.;

		ldec_ptkp = basDao.selectPtkp(lstr_tax);
			
		//set ptkp to monthly
		ldec_ptkp = ldec_ptkp / 12;
		
		//set remaining ptkp
		lstr_tax.adec_ptkp=lstr_tax.adec_ptkp==null?0:lstr_tax.adec_ptkp;
		ldec_ptkp = ldec_ptkp - lstr_tax.adec_ptkp;
		//set pkp
		ldec_pkp  = adec_value - ldec_ptkp;
		//no pkp, no tax, return zero tax
		if(ldec_pkp <= 0) return 0.;
		
		ldec_acum_last = lstr_tax.adec_pkp_year;
		ldec_pkp_year  = lstr_tax.adec_pkp_year  + ldec_pkp;

		double ldec_pkp_value = 0;
		double ldec_pkp_count;
*/
		double[] ldec_range;
		double[] ldec_range_tax;
		int li_counter = 1;
		
		//HARUS - tax for year 2008 and below
		Date tahun2009 = null;
		try {
			tahun2009 = defaultDateFormat.parse("01/01/2009");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e);
		}
		
		//HARUS - 
		if(adt_date.before(tahun2009)){
			ldec_range = new double[]{200000000, 100000000, 50000000, 25000000, 0};
			ldec_range_tax = new double[]{0.35, 0.25, 0.15, 0.10, 0.05};
		//tax for year 2009 and above
		}else{
			ldec_range = new double[]{500000000, 250000000, 50000000, 0};
			ldec_range_tax = new double[]{0.30, 0.25, 0.15, 0.05};  
		}
		
		boolean until = true;
		while(until){
			if(adec_value >= ldec_range[li_counter-1]){
				adec_tax = adec_value * ldec_range_tax[li_counter-1];
				until = false;
			}else{
				li_counter++;
			}
/*	
			if(li_counter >= 2){
				if(ldec_acum_last > ldec_range[li_counter - 2]) break;
			}
		
			if(ldec_pkp_year > ldec_range[li_counter-1]){			
				ldec_pkp_value = ldec_pkp_year - ldec_range[li_counter-1]; 
				
				if(ldec_pkp_value <= ldec_pkp){	
					ldec_pkp_count = ldec_pkp_value;
				}else{
					ldec_pkp_count = ldec_pkp;
				}
				
				ldec_pkp       = ldec_pkp - ldec_pkp_count;			
				ldec_pkp_year  = ldec_range[li_counter-1];
				ldec_tax       = ldec_range_tax[li_counter-1];
			}else{
				ldec_pkp_count = 0;
				ldec_tax       = 0;
			}
			
			if(ldec_pkp_count > 0){
				adec_tax += ldec_pkp_count * ldec_tax;
			}
			li_counter++;
			
			if(ldec_pkp_year <= 0 || li_counter > ldec_range.length) until = false;
*/
		}
		
		//HARUS - fine tax 20% for year 2009 and above
		if(!adt_date.before(tahun2009)){
			//kalo agen gak punya NPWP, tax nya dipotong 20% lagi
			if(as_msag != null) {
				String npwp = uwDao.selectNpwpAgen(as_msag);
				if(npwp == null) adec_tax *= 1.2;
				else if(npwp.replace(".", "").replace("-", "").trim().equals("")) adec_tax *= 1.2;
			}
		}			

		//logger.info("adec_tax = " + twoDecimalNumberFormat.format(adec_tax));
		//logger.info("ldec_pkp_year = " + twoDecimalNumberFormat.format(ldec_pkp_year));
		//logger.info("ldec_acum_last = " + twoDecimalNumberFormat.format(ldec_acum_last));
		//logger.info("ldec_ptkp = " + twoDecimalNumberFormat.format(ldec_ptkp));
		//logger.info("ldec_pkp = " + twoDecimalNumberFormat.format(ldec_pkp));
		//logger.info("ldec_tax = " + twoDecimalNumberFormat.format(ldec_tax));
		
		return adec_tax;
	}
	
	/**
	 * 
	 * @author Yusuf
	 * @since May 12, 2008 (11:04:50 AM)
	 * @param komisi
	 * @return
	 * @throws Exception
	 */
	public Double hitungPajakKomisi(Double komisi, Date tgl_komisi, String msag_id){
		if(logger.isDebugEnabled())logger.debug("PROSES: hitungPajakKomisi");
		
		//Perhitungan pajak komisi, berbeda antara tahun 2008 kebawah, dan tahun 2009 keatas
		double kom = komisi;
		double tax = 0;
		
		Date d2009 = null;
		try {
			d2009 = defaultDateFormat.parse("01/01/2009");
		} catch (ParseException e) {
			logger.error("ERROR :", e);
		}
		
		//dibawah tahun 2009
		if(tgl_komisi.before(d2009)) {
			
			if(kom == 0) 				tax = 0.;
			else if(kom <=  25000000) 	tax = kom 				* 0.05;
			else if(kom <=  50000000) 	tax = (kom-25000000)	* 0.1 	+  1250000;
			else if(kom <= 100000000) 	tax = (kom-50000000) 	* 0.15 	+  3750000;
			else if(kom <= 200000000) 	tax = (kom-100000000)	* 0.25 	+ 11250000;
			else 						tax = (kom-200000000) 	* 0.35 	+ 36250000;
			
		//tahun 2009 dst
		}else {
			
			if(kom == 0) 				tax = 0.;
			else if (kom <=  50000000) 	tax =  kom * 0.05;           
			else if (kom <= 250000000) 	tax = (kom -  50000000) * 0.15 +  2500000;
			else if (kom <= 500000000) 	tax = (kom - 250000000) * 0.25 + 32500000;
			else 						tax = (kom - 500000000) * 0.30 + 95000000;
			
			//kalo agen gak punya NPWP, tax nya dipotong 20% lagi
			if(msag_id != null) {
				String npwp = uwDao.selectNpwpAgen(msag_id);
				if(npwp == null) tax *= 1.2;
				else if(npwp.replace(".", "").replace("-", "").trim().equals("")) tax *= 1.2;
			}
			
		}
		
		return tax;
	}

	public String sequenceMst_commission(int aplikasi) {
		//(12 May 2014) Deddy - Dinonaktifkan karenag memakai seq dari oracle.
//		String sekuens = this.uwDao.selectGetCounter(aplikasi, "01");
//		this.uwDao.updateCounter(sekuens, aplikasi, "01");
//		String hasil = FormatString.rpad("0", sekuens, 12);
//		if(logger.isDebugEnabled())logger.debug("SEQUENCE: untuk EKA.MST_COMMISSION terakhir adalah: " + hasil);
		String hasil = uwDao.selectSeqCommId();
		return hasil;
	}
	
	private boolean f_get_komisi(Commission komisi, BindException err, int year, int premi_ke, String lku_id, double premi, int maxCount) throws Exception{ //f_get_komisi
		if(logger.isDebugEnabled())logger.debug("PROSES: f_get_komisi");
	
		int bulanProd = Integer.valueOf(uwDao.selectBulanProduksi(komisi.getReg_spaj()));
		
		//bulanProd = 200901;
		
		//KALO PRODUCT SAVE DAN PRODUK STABLE LINK DAN SSE, GAK ADA OR!!! (Yusuf) 2008
		if((products.powerSave(komisi.getBisnis_id().toString()) 
				|| komisi.getBisnis_id().intValue()==161
				|| (products.stableLink(komisi.getBisnis_id().toString()) && (bulanProd < 200809 || bulanProd > 200812))
				) 
				&& komisi.getLev_kom() < maxCount) {
			
			komisi.setKomisi(0.);
			return true;
		}
		
		//stable link, ada or sept - dec 2008, seliain itu tidak ada
		if(komisi.getLev_kom().intValue() < 4 && products.stableLink(komisi.getBisnis_id().toString()) && (bulanProd < 200809 || bulanProd > 200812)) {
			komisi.setKomisi(0.);
		}
		
		Map pribadi = this.uwDao.selectInfoPribadi(komisi.getReg_spaj());
		String ls_region = (String) pribadi.get("REGION");
		//Yusuf (12/09/2006) - untuk CAM DPLK, bisa menutup produk Horison (worksite) tetapi tidak mendapat komisi 
		if(ls_region.startsWith("08") && ("149,183,193,195,201,204".indexOf(komisi.getBisnis_id().toString())>-1 )) {
			return false;
		}
		
		//SPECIAL CASES
		Commission temp = new Commission(); 
		PropertyUtils.copyProperties(temp, komisi);
		
		//hybrid 2009
		if(komisi.getRegion().startsWith("46") && bulanProd > 200900) {
			if(temp.getLev_kom() != null) {
				temp.setLev_kom(temp.getLev_kom().intValue() - 2);
			}
		}else {
			//untuk cara bayar selain sekaligus, diset ke tahunan(3)
			if(temp.getCb_id().intValue()!=0){
				temp.setCb_id(3);
			}
			
			//1. SSE SSG (161), karena det bisnisnya banyak, dipatok lsdbs_number = 1 saja saat penarikan
			if(temp.getBisnis_id().intValue() == 161) {
				temp.setBisnis_no(1);
			//2. HIDUP BAHAGIA (167), dipatok LSCB_ID = 0, LSCO_INSPER = 70
			}else if(temp.getBisnis_id().intValue() == 167) {
				temp.setCb_id(0);
				temp.setIns_period(70);
			//3. DANA SEJAHTERA NEW (163), dipatok LSCO_INSPER = 99
			}else if(temp.getBisnis_id().intValue() == 163) {
				temp.setIns_period(99);
			//4.a. E-CARE (168), dipatok LSCO_INSPER = 99
			}else if(temp.getBisnis_id().intValue() == 168) {
				temp.setCb_id(3);
				temp.setIns_period(99);
			//5. EkaWaktu, masa asuransinya ada 4 jenis : 5, 10, 15, dan 20 tahun
			}else if(temp.getBisnis_id().intValue() == 169){
				//masa asuransi 5 tahun
				if(temp.getBisnis_no()==1 || temp.getBisnis_no() == 5 || temp.getBisnis_no() == 23 ){
					temp.setIns_period(5);
					if(temp.getBisnis_no()==1 || temp.getBisnis_no()==23){
						temp.setCb_id(3);
					}else temp.setCb_id(0);
				}//masa asuransi 10 tahun
				else if(temp.getBisnis_no()==2 || temp.getBisnis_no()==6 || temp.getBisnis_no()==24){
					temp.setIns_period(10);
					if(temp.getBisnis_no()==2 || temp.getBisnis_no()==24 ){
						temp.setCb_id(3);
					}else temp.setCb_id(0);
				}//masa asuransi 15 tahun
				else if(temp.getBisnis_no()==3 || temp.getBisnis_no()==7){
					temp.setIns_period(15);
					if(temp.getBisnis_no()==3){
						temp.setCb_id(3);
					}else temp.setCb_id(0);
				}//masa asuransi 20 tahun
				else {
					temp.setIns_period(20);
					if(temp.getBisnis_no()==8){
						temp.setCb_id(0);
					}else temp.setCb_id(3);
				}
			//6. prolife dipatok LSCO_INSPER = 99
			}else if(temp.getBisnis_id().intValue() == 179) {
				temp.setIns_period(99);
			}else if(temp.getBisnis_id().intValue() == 172) {
				temp.setIns_period(23);
			}else if(temp.getBisnis_id().intValue() == 180) {
				if(temp.getBisnis_no()==1){
					temp.setIns_period(10);
				}else if(temp.getBisnis_no()==2){
					temp.setIns_period(15);
				}else if(temp.getBisnis_no()==3){
					temp.setIns_period(20);
				}
				
			}else if(temp.getBisnis_id().intValue() == 185){
				if(temp.getCb_id().intValue() !=0){
					temp.setCb_id(3);
				}
				
				int autodebet = bacDao.selectFlagCC(komisi.getReg_spaj());
				//bila autodebet payroll
				if(autodebet == 4){
					temp.setCb_id(6);
				}
			}else if(temp.getBisnis_id().intValue() == 208 ){
				//Adrian: SMiLe KID kompensasi thn_pertama 10% 
				//penjagaan lst_comm_new krn Trigger (Otbie tdk ada)
				if( temp.getBisnis_no()==9 ){
					temp.setIns_period(1);
				}
			}
			
		}
		// untuk arco , diset 8
		if( (temp.getBisnis_id().intValue() == 183 &&( temp.getBisnis_no()>=31 && temp.getBisnis_no()<=45)) ||
		    (temp.getBisnis_id().intValue() == 189 &&( temp.getBisnis_no()>=16 && temp.getBisnis_no()<=30))	||
		    (temp.getBisnis_id().intValue() == 204 &&( temp.getBisnis_no()>=13 && temp.getBisnis_no()<=36))){
			
				temp.setLsco_jenis(8);
		}
		
		if(ls_region.startsWith("62")){//jika tutupan MNC set dua type(non special offer dan  special offer)
			Tertanggung tertanggung = bacDao.selectttg(komisi.getReg_spaj());
			if(tertanggung.getMste_flag_special_offer()==0){
				temp.setLsco_jenis(9);
			}else if(tertanggung.getMste_flag_special_offer()==1 || tertanggung.getMste_flag_special_offer()==2){
				temp.setLsco_jenis(10);
			}
		}
		
		
		
		
		temp.setLsco_year(year);
		temp.setTh_kom(year);
		Date ldt_temp = this.uwDao.selectLastComissionDate(temp);
		Map tmp = this.uwDao.selectCommisionAndBonus(ldt_temp, temp);
		if(tmp!=null){
			if(tmp.get("LSCO_COMM")!=null) komisi.setKomisi(Double.valueOf(tmp.get("LSCO_COMM").toString()));
			if(tmp.get("LSCO_BONUS")!=null) komisi.setBonus(Double.valueOf(tmp.get("LSCO_BONUS").toString()));
		}
		
		//4.b. E-CARE, harus di hardcoding, karena ada umur2an
		if(temp.getBisnis_id().intValue() == 168) {
			int lsdbs = temp.getBisnis_no().intValue();
			int umurTT = uwDao.selectUsiaTertanggung(komisi.getReg_spaj());
			
			//mpp 5
			if(lsdbs >= 1 && lsdbs <= 5) {
				if(umurTT <= 45) {
					komisi.setKomisi(15.);
				}else {
					komisi.setKomisi(5.);
				}
			//mpp 10
			}else if(lsdbs >= 6 && lsdbs <= 10) {
				komisi.setKomisi(15.);
			//mpp 15
			}else if(lsdbs >= 11 && lsdbs <= 15) {
				komisi.setKomisi(15.);
			//mpp 55-x
			}else if(lsdbs >= 16 && lsdbs <= 20) {
				if(umurTT <= 45) {
					komisi.setKomisi(15.);
				}else {
					komisi.setKomisi(5.);
				}
			}else {
				komisi.setKomisi(0.);
			}
			
		//5. STABLE LINK (Yusuf - 1/5/08)
		}else if(temp.getBisnis_id().toString().equals("164")) {
			List<Map> stable = uwDao.selectInfoStableLink(komisi.getReg_spaj());
			Double totalPremi = uwDao.selectTotalPremiNewBusiness(komisi.getReg_spaj());
			for(Map m : stable) {
				int msl_no = ((BigDecimal) m.get("MSL_NO")).intValue();
				if(premi_ke == msl_no) {
					int mgi = ((BigDecimal) m.get("MSL_MGI")).intValue();
					double bonus_promosi = (double) 0;
					Date bdate = (Date) m.get("MSL_BDATE");
					//periode sampai desember 2008, ada bonus promosi
					if(bulanProd >= 201105){
						if(mgi == 1) {
							komisi.setKomisi(0.05);
						}else if(mgi == 3) {
							komisi.setKomisi(0.15);
						}else if(mgi == 6) {
							komisi.setKomisi(0.3);
						}else if(mgi == 12) {
							komisi.setKomisi(0.6);
						}else if(mgi == 24) {
							komisi.setKomisi(1.2);
						}else if(mgi == 36) {
							komisi.setKomisi(1.35);
						}else {
							throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
						}
					}else{
						if(bdate.compareTo(defaultDateFormat.parse("01/01/2009")) < 0) {
							if(mgi == 1) {
								bonus_promosi = 0.033;
							}else if(mgi == 3) {
								bonus_promosi = 0.1;
							}else if(mgi == 6) {
								bonus_promosi = 0.2;
							}else if(mgi == 12) {
								bonus_promosi = 0.4;
							}else if(mgi == 24) {
								bonus_promosi = 0.8;
							}else if(mgi == 36) {
								bonus_promosi = 1.2;
							}
						}
						//perhitungan bonus penjualan, dihitung dari TOTAL PREMI bukan dari PREMI POKOK saja
						
						if(mgi == 1) {
							komisi.setKomisi(0.017 + bonus_promosi);
						}else if(mgi == 3) {
							komisi.setKomisi(0.05 + bonus_promosi);
						}else if(mgi == 6) {
							komisi.setKomisi(0.1 + bonus_promosi);
						}else if(mgi == 12) {
							komisi.setKomisi(0.2 + bonus_promosi);
						}else if(mgi == 24) {
							komisi.setKomisi(0.4 + bonus_promosi);
						}else if(mgi == 36) {
							komisi.setKomisi(0.6 + bonus_promosi);
						}else {
							throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
						}
					}
					break;
				}
			}
		//6. STABLE LINK SYARIAH (Yusuf - 28/01/09)
		}else if(temp.getBisnis_id().toString().equals("174")) {
			
			List<Map> stable = uwDao.selectInfoStableLink(komisi.getReg_spaj());
			Double totalPremi = uwDao.selectTotalPremiNewBusiness(komisi.getReg_spaj());
			for(Map m : stable) {
				int msl_no = ((BigDecimal) m.get("MSL_NO")).intValue();
				if(premi_ke == msl_no) {
					int mgi = ((BigDecimal) m.get("MSL_MGI")).intValue();
					double bonus_promosi = (double) 0;
					Date bdate = (Date) m.get("MSL_BDATE");

					//perhitungan bonus penjualan, dihitung dari TOTAL PREMI bukan dari PREMI POKOK saja
					
					if(mgi == 1) {
						komisi.setKomisi(0.05);
					}else if(mgi == 3) {
						komisi.setKomisi(0.15);
					}else if(mgi == 6) {
						komisi.setKomisi(0.3);
					}else if(mgi == 12) {
						komisi.setKomisi(0.6);
					}else if(mgi == 24) {
						komisi.setKomisi(1.2);
					}else if(mgi == 36) {
						komisi.setKomisi(1.35);
					}else {
						throw new RuntimeException("Harap cek perhitungan komisi Stable Link Syariah, mgi = " + mgi);
					}
				
					break;
				}
			}		
		
		}/*else if("189,193,201".indexOf(komisi.getBisnis_id().toString())>-1 || komisi.getBisnis_id()==183 && komisi.getBisnis_no() < 30 ){//Eka Sehat
			komisi.setKomisi(15.0);
			//Yusuf (27/7/09), request email yosep, bila cara bayar bulanan, komisi tidak dibayar di awal
			if(komisi.getCb_id().intValue() == 6) komisi.setKomisi(0.);  
		} menggunakan lsco_comm - Ryan  */
		else if(komisi.getBisnis_id().intValue()==195 || komisi.getBisnis_id().intValue()==204){
			komisi.setKomisi(10.0);
		}else if(komisi.getBisnis_id()==178){//Smart Medicare
			komisi.setKomisi(5.0);
		}else if(komisi.getBisnis_id()==208){// *Smart Kid
			komisi.setKomisi(5.0);
		}else if(komisi.getBisnis_id().intValue()==180){//Pro Saver
			if(komisi.getBisnis_no().intValue()==1){
				komisi.setKomisi(20.0);
			}else if(komisi.getBisnis_no().intValue()==2){
				komisi.setKomisi(25.0);
			}else if(komisi.getBisnis_no().intValue()==3){
				komisi.setKomisi(25.0);
			}
			if(komisi.getCb_id().intValue()==0){
				komisi.setKomisi(5.0);
			}
		}
		
		String bancAss = komisi.getBancass();
		if(bancAss==null) bancAss="-";
		
		if(bancAss.equals("-") || !bancAss.equals("0914")) {
		}else {
			if(komisi.getLev_kom()==maxCount && komisi.getTh_kom()==1){
				//sehat, privasi baru, per 1 juni 2004
				if(komisi.getBisnis_id()==91 || komisi.getBisnis_id()==92){
					if(komisi.getCb_asli()==6)komisi.setKomisi(new Double(3));//bulanan
					else if(komisi.getCb_asli()==1)komisi.setKomisi(new Double(5));//triwulan
					else if(komisi.getCb_asli()==2)komisi.setKomisi(new Double(8.5));//semester
					else if(komisi.getCb_asli()==0)komisi.setKomisi(new Double(12.5));//sekaligus
				}
			}
		}

		if(tmp==null && komisi.getLev_kom()==maxCount){
			if(komisi.getFlag_mess().booleanValue()==true){
				err.reject("payment.noCommission", 
						new Object[]{
						   defaultDateFormat.format(temp.getTgl_kom()),
				           temp.getBisnis_id()+"-"+temp.getBisnis_no(),
				           temp.getCb_id(), 
				           temp.getIns_period(), 
				           temp.getKurs_id(), 
				           temp.getLev_kom(),
				           temp.getTh_kom(),
				           temp.getLsco_jenis()
				           }, 
						"Commission Tidak Ada.<br>- Tanggal: {0}<br>-Kode Bisnis: {1}<br>-Cara Bayar: {2}<br>-Ins_per: {3}<br>-Kurs: {4}<br>-Level: {5}<br>-Tahun Ke{6}<br>-Jenis: {7}");
			}
			throw new Exception(err);
		}
		
		//hybrid 2009
		if(komisi.getRegion().startsWith("46") && bulanProd > 200900) {
			if(komisi.getLev_kom()==0){
				komisi.setKomisi(new Double(5));
			}else if(komisi.getLev_kom() == -1){
				komisi.setKomisi(new Double(1));
			}
		}else {
			if(komisi.getLev_kom()==1 && komisi.getFlag_sbm()==1){
				if(komisi.getKomisi()>0)komisi.setKomisi(new Double(1.5));
			}else if(komisi.getLev_kom()==1 && komisi.getFlag_sbm()==0){
				if(komisi.getKomisi()>0)komisi.setKomisi(new Double(2.5));
			}
		}
		
		return true;
	}
	
	public BindException hitungKomisiReffBII(SpajBill spajBill, BindException errors, User currentUser) throws Exception{
		if(logger.isDebugEnabled())logger.debug("PROSES: hitungKomisiReffBII");
	
		StringBuffer msg = new StringBuffer();
		NumberFormat nf = NumberFormat.getNumberInstance();
		
		double li_pct_kom, li_pct_refund=0, li_pct_mkt_alloc=0;
		if(spajBill.getTahun_ke()!=1)return errors;
		double sisaRefund=0;

		Map reg = bacDao.selectregion(spajBill.getNo_spaj());
		String region = (String) reg.get("LSRG_NAMA");
		
		Map map = null;

		//cek referral khusus bancassurance
		Map mapNasabah=uwDao.selectReferrerBII(spajBill.getNo_spaj());
		Map mReffBii=uwDao.selectReferrerBIINew(spajBill.getNo_spaj());
		
		if(mapNasabah != null){
			map = mapNasabah;
		}else if(mReffBii != null){
			map = mReffBii;
		}else{
			errors.reject("","Harap Input Referral Bank Terlebih Dahulu.");
			throw new Exception(errors);
		}
		
		spajBill.setLku_id((String) map.get("LKU_ID"));
		String businessId = FormatString.rpad("0", map.get("LSBS_ID").toString(), 3);
		int lsdbs_number = ((Integer) map.get("LSDBS_NUMBER"));
		String li_cb_id = map.get("LSCB_ID").toString();
		spajBill.setLdec_premi((Double) map.get("MSPR_PREMIUM"));
		Integer li_reff = (Integer) map.get("LRB_ID");
		Integer li_aktif = (Integer) map.get("FLAG_AKTIF");
		if (li_reff==null) li_reff = new Integer(-1);
		if (li_aktif==null) li_aktif = new Integer(-1);
		
		if(spajBill.getFlag_topup()==1 || spajBill.getFlag_topup()==2){
			spajBill.setLdec_premi(this.uwDao.selectPremiTopUpUnitLink(spajBill.getNo_spaj(), spajBill.getPremi_ke()));
			if(spajBill.getLdec_premi()==null){
				errors.reject("payment.noTopUpPremium");
				throw new Exception(errors);
			}
		}

		if(spajBill.getLku_id().equals("02")){
			spajBill.setLdec_kurs(new Double(this.uwDao.selectLastCurrency(spajBill.getNo_spaj(), spajBill.getTahun_ke(), spajBill.getPremi_ke())));
			if(spajBill.getLdec_kurs()==null){
				errors.reject("payment.noLastCurrency");
				throw new Exception(errors);
			}
		}else spajBill.setLdec_kurs(new Double(1));
		spajBill.setLdt_prod(this.uwDao.selectProductionDate(spajBill.getNo_spaj(), new Integer(spajBill.getTahun_ke()), new Integer(spajBill.getPremi_ke())));
		if(spajBill.getLdt_prod()==null){
			errors.reject("payment.noProductionDate");
			throw new Exception(errors);
		}

		if((businessId.equals("080") || businessId.equals("092")) && li_cb_id.equals("0")){
			spajBill.setLdec_premi(hitungPremiTahunan(spajBill.getNo_spaj(), errors));
		}
		
		Date ldt_beg_date = (Date) map.get("MSPR_BEG_DATE");
		
		// mcr 1 = komisi fee based income untuk bank
		// mcr 2 = komisi untuk referral
		// mcr 3 = komisi untuk marketing
		
		/** START PROSES PERTAMA : INSERT UNTUK MCR 2 (REFERRAL) DAN MCR 3 (MARKETING) */
		
		double premiNB = spajBill.getLdec_premi();
		if(products.stableLink(businessId)) {
			spajBill.setLdec_premi(uwDao.selectTotalPremiNewBusiness(spajBill.getNo_spaj()));
		}
		
		//mulai 1 jun'05 komisi reff bii tdk ada, kms utk premi lanjutan yg dulu msh terus
		if(li_aktif==1 &&
				li_reff!=2 &&
				("150, 151, 155, 142, 158, 164, 165, 175".indexOf(businessId)<0)){  //Investa + Cerdas tidak ada komisi Reff (marketing)
			if(FormatDate.dateDifference(ldt_beg_date, new GregorianCalendar(2005,10,1).getTime(), false)>0) {
				li_pct_kom=5;
				spajBill.setLdec_komisi(new Double(spajBill.getLdec_premi() * spajBill.getLdec_kurs() * li_pct_kom / 100));
				Date sysdate = commonDao.selectSysdateTruncated(0);
				spajBill.setLdec_tax(hitungPajakKomisi(spajBill.getLdec_komisi(), sysdate, null));
				
				spajBill.setLdec_komisi(FormatNumber.round(spajBill.getLdec_komisi(), 0));
				spajBill.setLdec_tax(FormatNumber.rounding(spajBill.getLdec_tax(), true, 25));
				spajBill.setMcr_flag(new Integer(2));
				//flag = 2 untuk reff
				this.uwDao.insertMst_comm_reff_bii(spajBill);
				msg.append("Premi = " + nf.format(spajBill.getLdec_premi()) + ", Komisi Marketing (2) = " + nf.format(spajBill.getLdec_komisi() - spajBill.getLdec_tax()));
			}else if(FormatDate.dateDifference(ldt_beg_date, new GregorianCalendar(2005,10,1).getTime(), false)<=0) {
				li_pct_kom=1.5;
				spajBill.setLdec_komisi(new Double(spajBill.getLdec_premi() * spajBill.getLdec_kurs() * li_pct_kom / 100));
				spajBill.setLdec_tax(new Double(0));
				spajBill.setLdec_komisi(FormatNumber.round(spajBill.getLdec_komisi(), 0));
				spajBill.setMcr_flag(new Integer(3));
				this.uwDao.insertMst_comm_reff_bii(spajBill);
				msg.append("Premi = " + nf.format(spajBill.getLdec_premi()) + ", Komisi Marketing (3) = " + nf.format(spajBill.getLdec_komisi() - spajBill.getLdec_tax()));
			}
		}
		
		// (Yusuf) mulai juli 2007, selain fee based, ada komisi reff BII khusus produk SPECTA
		if(li_aktif==1 && li_reff!=2 && 
				((businessId.equals("155") && lsdbs_number == 2) || (businessId.equals("158") && lsdbs_number == 8))){
			int li_mgi = uwDao.selectMasaGaransiInvestasi(spajBill.getNo_spaj(), 1, 1);
			li_pct_kom = 0;
			if(li_mgi == 3) {
				li_pct_kom = 0.02;
			} else if(li_mgi == 6) {
				li_pct_kom = 0.04;
			} else if(li_mgi == 12) {
				li_pct_kom = 0.08;
			}
			if(li_pct_kom > 0) {
				spajBill.setLdec_komisi(new Double(spajBill.getLdec_premi() * spajBill.getLdec_kurs() * li_pct_kom / 100));
				spajBill.setLdec_tax(new Double(0));
				spajBill.setLdec_komisi(FormatNumber.round(spajBill.getLdec_komisi(),  0));
				spajBill.setMcr_flag(new Integer(3));
				this.uwDao.insertMst_comm_reff_bii(spajBill);
				msg.append("Premi Khusus Specta = " + nf.format(spajBill.getLdec_premi()) + ", Komisi Marketing (3) = " + nf.format(spajBill.getLdec_komisi() - spajBill.getLdec_tax()));
			}
		}else if(businessId.equals("165")) {
			//(Yusuf - 29/10/2007) - INVESTIMAX
			if(lsdbs_number == 1 || lsdbs_number == 2) {
				li_pct_kom = 1;
				spajBill.setLdec_komisi(new Double(spajBill.getLdec_premi() * spajBill.getLdec_kurs() * li_pct_kom / 100));
				spajBill.setLdec_tax(new Double(0));
				spajBill.setLdec_komisi(FormatNumber.round(spajBill.getLdec_komisi(),  0));
				spajBill.setMcr_flag(new Integer(3));
				this.uwDao.insertMst_comm_reff_bii(spajBill);					
				msg.append("Premi Khusus Investimax = " + nf.format(spajBill.getLdec_premi()) + ", Komisi Marketing (3) = " + nf.format(spajBill.getLdec_komisi() - spajBill.getLdec_tax()));
			}
		}
		
		spajBill.setLdec_premi(premiNB);
		
		/** START PROSES KEDUA : INSERT UNTUK MCR 1 (FEE BASED INCOME UNTUK BANK) */
		
		//apabila sama seno udah pasti, gunakan rate default untuk fee based produk2 powersave
//		if(products.powerSave(businessId)) {
//			List feeBased = uwDao.selectFeeBasedPowersave(Integer.valueOf(businessId), lsdbs_number, spajBill.getLku_id(), uwDao.selectMasaGaransiInvestasi(spajBill.getNo_spaj()));
//			//apabila ada ratenya di tabel, gunakan data tabel, bila tidak ada, balik ke hardcoding lagi
//			if(feeBased != null) {
//				int flagPremi = ((BigDecimal) feeBased.get("FLAG_PREMI")).intValue();
//				double batasanPremi = ((BigDecimal) feeBased.get("PREMI")).doubleValue();
//				double persenDefault = ((BigDecimal) feeBased.get("PERSEN_DEFAULT")).doubleValue();
//				if(flagPremi == 0) { //0 gak ada range
//					
//				}else if(flagPremi == 1) { //1 lebih kecil dari batasan premi
//					
//				}else if(flagPremi == 2) { //2 lebih besar sama dengan batasan premi
//					
//				}
//			}else {
//				//balik ke hardcoding lagi
//			}
//		}
		
		
		li_pct_kom = 0;
		
		/*
		//1. MAXI CARE
		if(businessId.equals("081")) li_pct_kom=20;
		//2. INVESTA
		else if(businessId.equals("111")) li_pct_kom=30;
		//3. PRIVASI DAN SEHAT
		else if(businessId.equals("079") || businessId.equals("091") || businessId.equals("080") || businessId.equals("092")) li_pct_kom=30;
		//4. CERDAS
		else if(products.cerdas(businessId)){
			//PREMI TOP-UP
			if(spajBill.getFlag_topup()==1 || spajBill.getFlag_topup()==2 )
				li_pct_kom=2;
			//PREMI POKOK
			else {
				//YUSUF (14/02/2007) - GOLD LINK, KOMISI 31%, 6% DI-REFUND/CASHBACK KE NASABAH, 
				//DENGAN MAX. 1.5 JUTA, sisanya masukkin lagi ke komisi
				if(uwDao.validationCerdas(spajBill.getNo_spaj(), 3)>0) {
					li_pct_kom=31;
					li_pct_refund=6;
					//untuk refund, dihitung dari premi dibulatkan kebawah per 5 juta, misalnya premi 34 juta, maka perhitungannya:
					//6% * 30 juta (dimana 30 juta didapat dari 34 juta dibulatkan kebawah per 5 juta)
					spajBill.setRefund(new Double(FormatNumber.rounding(spajBill.getLdec_premi() * spajBill.getLdec_kurs(), false, 5000000) * li_pct_refund / 100));
					if(spajBill.getRefund()>1500000) {
						sisaRefund = spajBill.getRefund()-1500000;
						spajBill.setRefund((double) 1500000);
					}
				//YUSUF (15/03/2007) - PRO LINK, sama seperti gold link, tapi tidak ada maksimum refund 1,5 jt
				} else if(uwDao.validationCerdas(spajBill.getNo_spaj(), 4)>0) {
					li_pct_kom=31;
					li_pct_refund=6;
					//untuk refund, dihitung dari premi dibulatkan kebawah per 5 juta, misalnya premi 34 juta, maka perhitungannya:
					//6% * 30 juta (dimana 30 juta didapat dari 34 juta dibulatkan kebawah per 5 juta)
					spajBill.setRefund(new Double(FormatNumber.rounding(spajBill.getLdec_premi() * spajBill.getLdec_kurs(), false, 5000000) * li_pct_refund / 100));
				}else {
					
					//divisi penjual cerdas - Yusuf (22/04/08)
					Integer divisi = uwDao.selectCerdasSalesOrOperation(spajBill.getNo_spaj());
					
					if(divisi == null) {
						li_pct_kom = 0;
					}else if(divisi == 1) {
						li_pct_kom = 50; //SALES
					}else if(divisi == 2) {
						li_pct_kom = 37; //OPERATION
					}else {
						errors.reject("", "Maaf tetapi ada kesalahan di divisi cerdas.");
						throw new Exception(errors);
					}
					
				}
			}
		//5. SIMPONI DAN SECURED INVEST
		}else if(businessId.equals("150") || businessId.equals("151")) {
			if(lsdbs_number == 1) li_pct_kom = 1.5; // Simponi Bank Sinarmas (RG) 07/02/2006
			else if(lsdbs_number == 2) li_pct_kom = 3; // Secured Invest Deluxe $ dan Rp. (Yusuf) 18/01/2007
		//6. PLATINUM SAVE
		}else if(businessId.equals("155")) {
			
			li_pct_kom = 0; // Platinum Save (YUSUF) 15/11/2006
			int li_mgi = uwDao.selectMasaGaransiInvestasi(spajBill.getNo_spaj());
			Map mapNsb = uwDao.selectLeadNasabahFromSpaj(spajBill.getNo_spaj());
			Integer jenisBedebah = null;
			if(mapNsb != null) jenisBedebah = (Integer) mapNsb.get("JN_NASABAH");
			if(jenisBedebah == null) {
				jenisBedebah = -1;
				errors.reject("", "Maaf tetapi referral bank nya salah. (seharusnya tersimpan di lead)");
				throw new Exception(errors);
			}
			
			//6.1. PLATINUM SAVE BIASA
			if(lsdbs_number == 1) {
				if(spajBill.getLku_id().equals("01")) { //RUPIAH
					if(li_mgi == 3) {
						//li_pct_mkt_alloc = 0.0200;
						li_pct_kom = 0.2500;
					} else if(li_mgi == 6) {
						//li_pct_mkt_alloc = 0.0400;
						li_pct_kom = 0.5000;
					} else if(li_mgi == 12) {
						//li_pct_mkt_alloc = 0.0800;
						li_pct_kom = 1.0000;
					} else if(li_mgi == 36) {
						//li_pct_mkt_alloc = 0.0800;
						li_pct_kom = 2.2500;
					}

				}else if(spajBill.getLku_id().equals("02")){ //DOLLAR
					if(li_mgi == 3) {
						//li_pct_mkt_alloc = 0.0450;
						li_pct_kom = 0.2500;
					} else if(li_mgi == 6) {
						//li_pct_mkt_alloc = 0.0900;
						li_pct_kom = 0.5000;
					} else if(li_mgi == 12) {
						//li_pct_mkt_alloc = 0.1800;
						li_pct_kom = 1.0000;
					} else if(li_mgi == 36) {
						//li_pct_mkt_alloc = 0.1800;
						li_pct_kom = 2.0000;
					}
				}
			//6.2. SPECTA SAVE DAN SMART INVEST
			}else if(lsdbs_number == 2 || lsdbs_number == 3) {
				//(Yusuf) 8 Nov 2007 - udah gak ada marketing alloc
				li_pct_mkt_alloc = 0;
				
				//6.2.1. SPECTA SAVE
				if(lsdbs_number == 2) {
					if(spajBill.getLku_id().equals("01")) { //RUPIAH
						if(li_mgi == 3) {
							//li_pct_mkt_alloc = 0.0200;
							li_pct_kom = 0.1875;
						} else if(li_mgi == 6) {
							//li_pct_mkt_alloc = 0.0400;
							li_pct_kom = 0.3750;
						} else if(li_mgi == 12) {
							//li_pct_mkt_alloc = 0.0800;
							li_pct_kom = 0.7500;
						} else if(li_mgi == 36) {
							//li_pct_mkt_alloc = 0.0800;
							li_pct_kom = 2.0000;
						}
					}else if(spajBill.getLku_id().equals("02")){ //DOLLAR
						if(li_mgi == 3) {
							//li_pct_mkt_alloc = 0.0450;
							li_pct_kom = 0.2500;
						} else if(li_mgi == 6) {
							//li_pct_mkt_alloc = 0.0900;
							li_pct_kom = 0.5000;
						} else if(li_mgi == 12) {
							//li_pct_mkt_alloc = 0.1800;
							li_pct_kom = 1.0000;
						} else if(li_mgi == 36) {
							//li_pct_mkt_alloc = 0.1800;
							li_pct_kom = 2.0000;
						}
					}
				//6.2.2. SMART INVEST
				}else if(lsdbs_number == 3) {
					if(spajBill.getLku_id().equals("01")) { //RUPIAH
						if(li_mgi == 3) {
							//li_pct_mkt_alloc = 0.0200;
							li_pct_kom = 0.2500;
						} else if(li_mgi == 6) {
							//li_pct_mkt_alloc = 0.0400;
							li_pct_kom = 0.5000;
						} else if(li_mgi == 12) {
							//li_pct_mkt_alloc = 0.0800;
							li_pct_kom = 1.0000;
						} else if(li_mgi == 36) {
							//li_pct_mkt_alloc = 0.0800;
							li_pct_kom = 2.2500;
						}
					}else if(spajBill.getLku_id().equals("02")){ //DOLLAR
						if(li_mgi == 3) {
							//li_pct_mkt_alloc = 0.0450;
							li_pct_kom = 0.2500;
						} else if(li_mgi == 6) {
							//li_pct_mkt_alloc = 0.0900;
							li_pct_kom = 0.5000;
						} else if(li_mgi == 12) {
							//li_pct_mkt_alloc = 0.1800;
							li_pct_kom = 1.0000;
						} else if(li_mgi == 36) {
							//li_pct_mkt_alloc = 0.1800;
							li_pct_kom = 2.0000;
						}
					}
				}

			}
		//7. POWERSAVE BIASA
		}else if(businessId.equals("142")) {
			//powersave bank Sinarmas ada tambahan (rg - 09/06/2006)
			int li_mgi = uwDao.selectMasaGaransiInvestasi(spajBill.getNo_spaj());
			int jenisReferal = uwDao.selectJenisPenutupBII(spajBill.getNo_spaj());

			//7.1. Secured Invest ABC (Yusuf - 18/01/2007) -> jenisreferal = 9
			if(lsdbs_number == 3) {
				if(li_mgi == 3) {li_pct_kom = 0.31;
				}else if(li_mgi == 6) {li_pct_kom = 0.62;
				}else if(li_mgi == 12) {li_pct_kom = 1.25;
				}
			//7.2.a. Powersave Mayapada - SK-079 2007 (Yusuf - 18/10/2007) atau Privilege Save (UOB Buana)
				//Surat Keputusan No.081/AJS-SK/X/2007 - (Yusuf - 30/10/2007) 
			}else if(lsdbs_number == 4 || lsdbs_number == 5 || lsdbs_number == 6) {
				if(jenisReferal == 8) {
					if(li_mgi == 1) {li_pct_kom = 0.025;
					}else if(li_mgi == 3) {li_pct_kom = 0.1;
					}else if(li_mgi == 6) {li_pct_kom = 0.2;
					}else if(li_mgi == 12) {li_pct_kom = 0.4;}
				}else if(jenisReferal == 9 || jenisReferal == 10) {
					if(li_mgi == 3) {li_pct_kom = 0.1875;
					}else if(li_mgi == 6) {li_pct_kom = 0.3750;
					}else if(li_mgi == 12) {li_pct_kom = 0.7500;
					}else if(li_mgi == 36) {li_pct_kom = 2;
					}				
				}
			//7.2.b. Powersave Bung Maxi - Yusuf - 06/06/2008 - SK-060-2008
			}else if(lsdbs_number == 7) {
				if(li_mgi == 3) {li_pct_kom = 0.1875;
				}else if(li_mgi == 6) {li_pct_kom = 0.3750;
				}else if(li_mgi == 12) {li_pct_kom = 0.7500;
				}else if(li_mgi == 24) {li_pct_kom = 1.3750;
				}else if(li_mgi == 36) {li_pct_kom = 2;
				}				

			//7.3. Bank Sinarmas
			} else	if(jenisReferal == 2) {
				
				Date tgl_bayar = uwDao.selectTanggalBayar(spajBill.getNo_spaj());
				Date des1008 = defaultDateFormat.parse("10/12/2008");
				Date apr0108 = defaultDateFormat.parse("01/04/2008");
				Date apr2009 = defaultDateFormat.parse("20/04/2009");
				
				//fee based 142-2 (simas prima)
				
				//1. before 1 apr 2008 -> dolar 1%, rupiah 2%
				if(tgl_bayar.before(apr0108)){
					if(spajBill.getLku_id().equals("02")){ //DOLLAR
						if(li_mgi == 3) {li_pct_kom = 0.25;
						}else if(li_mgi == 6) {li_pct_kom = 0.5;
						}else if(li_mgi == 12) {li_pct_kom = 1;
						}else if(li_mgi == 36) {li_pct_kom = 2;}
					}else { //RUPIAH
						if(li_mgi == 3) {li_pct_kom = 0.5;
						}else if(li_mgi == 6) {li_pct_kom = 1;
						}else if(li_mgi == 12) {li_pct_kom = 2;
						}else if(li_mgi == 36) {li_pct_kom = 4;}
					}					
				//2. 1 apr 2008 - 09 des 2008 -> dolar 1%, rupiah 3%
				}else if(tgl_bayar.before(des1008)){
					if(spajBill.getLku_id().equals("02")){ //DOLLAR
						if(li_mgi == 3) {li_pct_kom = 0.25;
						}else if(li_mgi == 6) {li_pct_kom = 0.5;
						}else if(li_mgi == 12) {li_pct_kom = 1;
						}else if(li_mgi == 36) {li_pct_kom = 2;}
					}else { //RUPIAH
						if(li_mgi == 3) {li_pct_kom = 0.75;
						}else if(li_mgi == 6) {li_pct_kom = 1.5;
						}else if(li_mgi == 12) {li_pct_kom = 3;
						}else if(li_mgi == 36) {li_pct_kom = 5;}
					}					
				//3. 10 des 2008 - 19 apr 2009 -> dolar 2%, rupiah 1.5%
				}else if(tgl_bayar.before(des1008)){
					if(spajBill.getLku_id().equals("02")){ //DOLLAR
						if(li_mgi == 3) {li_pct_kom = 0.5;
						}else if(li_mgi == 6) {li_pct_kom = 1;
						}else if(li_mgi == 12) {li_pct_kom = 2;}
					}else { //RUPIAH
						if(li_mgi == 3) {li_pct_kom = 0.375;
						}else if(li_mgi == 6) {li_pct_kom = 0.75;
						}else if(li_mgi == 12) {li_pct_kom = 1.5;}
					}					
				//4. 20 apr 2009 dst -> dolar 2%, rupiah 1%
				}else{
					if(spajBill.getLku_id().equals("02")){ //DOLLAR
						if(li_mgi == 3) {li_pct_kom = 0.5;
						}else if(li_mgi == 6) {li_pct_kom = 1;
						}else if(li_mgi == 12) {li_pct_kom = 2;}
					}else { //RUPIAH
						if(li_mgi == 3) {li_pct_kom = 0.25;
						}else if(li_mgi == 6) {li_pct_kom = 0.5;
						}else if(li_mgi == 12) {li_pct_kom = 1;}
					}					
				}
				
			//7.4. Sinarmas Sekuritas
			} else	if(jenisReferal == 3) {
				if(li_mgi == 1) {li_pct_kom = 0.05;
				}else if(li_mgi == 3) {li_pct_kom = 0.15;
				}else if(li_mgi == 6) {li_pct_kom = 0.3;
				}else if(li_mgi == 12) {li_pct_kom = 0.6;
				}else if(li_mgi == 36) {li_pct_kom = 1.3;}
			//7.5. Yusuf, 139/IM-DIR/XI/2006, komisi referal dirubah, khusus yang others
			} else	if(jenisReferal == 8) {
				if(li_mgi == 1) {li_pct_kom = 0.025;
				}else if(li_mgi == 3) {li_pct_kom = 0.1;
				}else if(li_mgi == 6) {li_pct_kom = 0.2;
				}else if(li_mgi == 12) {li_pct_kom = 0.4;}
			//(Deddy 28/7/2009) SK.Direksi No.079/AJS-SK/VII/2009
			} else if(lsdbs_number == 1){
				if(li_mgi == 3) {li_pct_kom = 0.063;
				}else if(li_mgi == 6) {li_pct_kom = 0.125;
				}else if(li_mgi == 12) {li_pct_kom = 0.25;
				}else if(li_mgi == 36) {li_pct_kom = 0.75;}
			}
		//8. PLATINUM LINK ATAU AMANAH LINK
		}else if(businessId.equals("134") || businessId.equals("166")) {
			//(Yusuf - 3/1/2008)
			if(lsdbs_number == 1) {
				if(spajBill.getFlag_topup()==1 || spajBill.getFlag_topup()==2) { //topup 2%
					li_pct_kom=2;
				}else { //pokok 40%
					li_pct_kom = 40;
				}
			}
		//9. POWERSAVE BULANAN
		}else if(businessId.equals("158")) {
			int li_mgi = uwDao.selectMasaGaransiInvestasi(spajBill.getNo_spaj());

			//9.1. PLATINUMSAVE BULANAN (YUSUF - 9/1/2007)
			if(lsdbs_number == 5) {
				if(spajBill.getLku_id().equals("01")) {
					if(li_mgi == 12) {li_pct_kom = 0.75;
					}else if(li_mgi == 36) {li_pct_kom = 2;}
				} else if(spajBill.getLku_id().equals("02")) {
					if(li_mgi == 12) {li_pct_kom = 1;
					}else if(li_mgi == 36) {li_pct_kom = 2;}
				}
			
			//9.2. POWERSAVE BULANAN BANK Sinarmas (YUSUF - 9/1/2007) / danamas prima bulanan
			}else if(lsdbs_number == 6 || lsdbs_number == 14) {
				
				Date tgl_bayar = uwDao.selectTanggalBayar(spajBill.getNo_spaj());
				Date des1008 = defaultDateFormat.parse("10/12/2008");
				Date apr0108 = defaultDateFormat.parse("01/04/2008");
				Date apr2009 = defaultDateFormat.parse("20/04/2009");
				
				//fee based 158-6 (simas prima manf bul)
				
				//1. before 1 apr 2008 -> dolar 1%, rupiah 2%
				if(tgl_bayar.before(apr0108)){
					if(spajBill.getLku_id().equals("02")){ //DOLLAR
						if(li_mgi == 3) {li_pct_kom = 0.25;
						}else if(li_mgi == 6) {li_pct_kom = 0.5;
						}else if(li_mgi == 12) {li_pct_kom = 1;
						}else if(li_mgi == 36) {li_pct_kom = 2;}
					}else { //RUPIAH
						if(li_mgi == 3) {li_pct_kom = 0.5;
						}else if(li_mgi == 6) {li_pct_kom = 1;
						}else if(li_mgi == 12) {li_pct_kom = 2;
						}else if(li_mgi == 36) {li_pct_kom = 4;}
					}					
				//2. 1 apr 2008 - 09 des 2008 -> dolar 1%, rupiah 3%
				}else if(tgl_bayar.before(des1008)){
					if(spajBill.getLku_id().equals("02")){ //DOLLAR
						if(li_mgi == 3) {li_pct_kom = 0.25;
						}else if(li_mgi == 6) {li_pct_kom = 0.5;
						}else if(li_mgi == 12) {li_pct_kom = 1;
						}else if(li_mgi == 36) {li_pct_kom = 2;}
					}else { //RUPIAH
						if(li_mgi == 3) {li_pct_kom = 0.75;
						}else if(li_mgi == 6) {li_pct_kom = 1.5;
						}else if(li_mgi == 12) {li_pct_kom = 3;
						}else if(li_mgi == 36) {li_pct_kom = 5;}
					}					
				//3. 10 des 2008 - 19 apr 2009 -> dolar 2%, rupiah 1.5%
				}else if(tgl_bayar.before(des1008)){
					if(spajBill.getLku_id().equals("02")){ //DOLLAR
						if(li_mgi == 3) {li_pct_kom = 0.5;
						}else if(li_mgi == 6) {li_pct_kom = 1;
						}else if(li_mgi == 12) {li_pct_kom = 2;}
					}else { //RUPIAH
						if(li_mgi == 3) {li_pct_kom = 0.375;
						}else if(li_mgi == 6) {li_pct_kom = 0.75;
						}else if(li_mgi == 12) {li_pct_kom = 1.5;}
					}					
				//4. 20 apr 2009 dst -> dolar 2%, rupiah 1%
				}else{
					if(spajBill.getLku_id().equals("02")){ //DOLLAR
						if(li_mgi == 3) {li_pct_kom = 0.5;
						}else if(li_mgi == 6) {li_pct_kom = 1;
						}else if(li_mgi == 12) {li_pct_kom = 2;}
					}else { //RUPIAH
						if(li_mgi == 3) {li_pct_kom = 0.25;
						}else if(li_mgi == 6) {li_pct_kom = 0.5;
						}else if(li_mgi == 12) {li_pct_kom = 1;}
					}					
				}
				
			//9.3. SPECTA SAVE BULANAN DAN SMART INVEST BULANAN (Yusuf) 6 juli 2007
			}else if(lsdbs_number == 8 || lsdbs_number == 9) {
				
				Map mapNsb = uwDao.selectLeadNasabahFromSpaj(spajBill.getNo_spaj());
				Integer jenisBedebah = null;
				if(mapNsb != null) jenisBedebah = (Integer) mapNsb.get("JN_NASABAH");
				if(jenisBedebah == null) {
					jenisBedebah = -1;
					errors.reject("", "Maaf tetapi referral bank nya salah. (seharusnya tersimpan di lead)");
					throw new Exception(errors);
				}
				
				li_pct_mkt_alloc = 0;
				li_pct_kom = 0;
				
				//9.3.1. SPECTA SAVE BULANAN
				if(lsdbs_number == 8) {
					if(spajBill.getLku_id().equals("01")) { //RUPIAH
						if(li_mgi == 12) {
							//li_pct_mkt_alloc = 0.0800;
							li_pct_kom = 0.7500;
						} else if(li_mgi == 36) {
							//li_pct_mkt_alloc = 0.0800;
							li_pct_kom = 2.0000;
						}
					}else if(spajBill.getLku_id().equals("02")){ //DOLLAR
						if(li_mgi == 12) {
							//li_pct_mkt_alloc = 0.1800;
							li_pct_kom = 1.0000;
						} else if(li_mgi == 36) {
							//li_pct_mkt_alloc = 0.1800;
							li_pct_kom = 2.0000;
						}
					}
				//9.3.2. SMART INVEST BULANAN
				} else if(lsdbs_number == 9) {
					if(spajBill.getLku_id().equals("01")) { //RUPIAH
						if(li_mgi == 12) {
							//li_pct_mkt_alloc = 0.0800;
							li_pct_kom = 0.7500;
						} else if(li_mgi == 36) {
							//li_pct_mkt_alloc = 0.0800;
							li_pct_kom = 2.0000;
						}
					}else if(spajBill.getLku_id().equals("02")){ //DOLLAR
						if(li_mgi == 12) {
							//li_pct_mkt_alloc = 0.1800;
							li_pct_kom = 1.0000;
						} else if(li_mgi == 36) {
							//li_pct_mkt_alloc = 0.1800;
							li_pct_kom = 2.0000;
						}
					}
				}
			//9.4. STABLE SAVE BULANAN BII (YUSUF - 30/01/09)
			}else if(lsdbs_number == 15) {
				//li_pct_kom = 3.0000;
				//li_pct_kom = 2.8000; //(Deddy - 10/06/09) SK. Direksi No. 067/AJS-SK/VI/2009
				li_pct_kom = 2.5000; //(Deddy - 28/07/09) berlaku tgl 1 agustus 2009 SK. Direksi No. 089/AJS-SK/VII/2009
			}
			
		//10. INVESTIMAX
		}else if(businessId.equals("165")) {
			//(Yusuf - 29/10/2007) - INVESTIMAX
			li_pct_kom = 4;
			
		//11. STABLE LINK dkk
		}else if(products.stableLink(businessId)) {
			li_pct_kom = 0;
			int mti = uwDao.selectMGIStableLink(spajBill.getTahun_ke(), spajBill.getPremi_ke(), spajBill.getNo_spaj());
			
			//11.1. SIMAS STABIL LINK
			if(businessId.equals("164") && lsdbs_number == 2) {
				Date tgl_bayar = uwDao.selectTanggalBayar(spajBill.getNo_spaj());
				Date des1008 = defaultDateFormat.parse("10/12/2008");
				Date apr0108 = defaultDateFormat.parse("01/04/2008");
				Date apr2009 = defaultDateFormat.parse("20/04/2009");
				
				//fee based 164-2 (simas stabil link)
				
				//1. before 1 apr 2008 -> dolar 1%, rupiah 2%
				if(tgl_bayar.before(apr0108)){
					if(spajBill.getLku_id().equals("02")){ //DOLLAR
						if(mti == 3) {li_pct_kom = 0.25;
						}else if(mti == 6) {li_pct_kom = 0.5;
						}else if(mti == 12) {li_pct_kom = 1;
						}else if(mti == 36) {li_pct_kom = 2;}
					}else { //RUPIAH
						if(mti == 3) {li_pct_kom = 0.5;
						}else if(mti == 6) {li_pct_kom = 1;
						}else if(mti == 12) {li_pct_kom = 2;
						}else if(mti == 36) {li_pct_kom = 4;}
					}					
				//2. 1 apr 2008 - 09 des 2008 -> dolar 1%, rupiah 3%
				}else if(tgl_bayar.before(des1008)){
					if(spajBill.getLku_id().equals("02")){ //DOLLAR
						if(mti == 3) {li_pct_kom = 0.25;
						}else if(mti == 6) {li_pct_kom = 0.5;
						}else if(mti == 12) {li_pct_kom = 1;
						}else if(mti == 36) {li_pct_kom = 2;}
					}else { //RUPIAH
						if(mti == 3) {li_pct_kom = 0.75;
						}else if(mti == 6) {li_pct_kom = 1.5;
						}else if(mti == 12) {li_pct_kom = 3;
						}else if(mti == 36) {li_pct_kom = 5;}
					}					
				//3. 10 des 2008 - 19 apr 2009 -> dolar 2%, rupiah 1.5%
				}else if(tgl_bayar.before(des1008)){
					if(spajBill.getLku_id().equals("02")){ //DOLLAR
						if(mti == 3) {li_pct_kom = 0.5;
						}else if(mti == 6) {li_pct_kom = 1;
						}else if(mti == 12) {li_pct_kom = 2;}
					}else { //RUPIAH
						if(mti == 3) {li_pct_kom = 0.375;
						}else if(mti == 6) {li_pct_kom = 0.75;
						}else if(mti == 12) {li_pct_kom = 1.5;}
					}					
				//4. 20 apr 2009 dst -> dolar 2%, rupiah 1%
				}else{
					if(spajBill.getLku_id().equals("02")){ //DOLLAR
						if(mti == 3) {li_pct_kom = 0.5;
						}else if(mti == 6) {li_pct_kom = 1;
						}else if(mti == 12) {li_pct_kom = 2;}
					}else { //RUPIAH
						if(mti == 3) {li_pct_kom = 0.25;
						}else if(mti == 6) {li_pct_kom = 0.5;
						}else if(mti == 12) {li_pct_kom = 1;}
					}					
				}
				
			//11.2. STABLE LINK SYARIAH (28/01/09)
			}else if(businessId.equals("174") && lsdbs_number == 1) {
				
				if((spajBill.getLku_id().equals("01") && spajBill.getLdec_premi().doubleValue() >= 100000000) || (spajBill.getLku_id().equals("02") && spajBill.getLdec_premi().doubleValue() >= 10000)) {
					if(mti == 3) {
						li_pct_kom = 0.15;
					}else if(mti == 6) {
						li_pct_kom = 0.3;
					}else if(mti == 12) {
						li_pct_kom = 0.6;
					}else if(mti == 24) {
						li_pct_kom = 0.95;
					}else if(mti == 36) {
						li_pct_kom = 1.3;
					}
				}else if(spajBill.getLku_id().equals("02")) {
					if(mti == 3) {
						li_pct_kom = 0.05;
					}else if(mti == 6) {
						li_pct_kom = 0.1;
					}else if(mti == 12) {
						li_pct_kom = 0.2;
					}else if(mti == 24) {
						li_pct_kom = 0.4;
					}else if(mti == 36) {
						li_pct_kom = 0.6;
					}
				}
			}
			
		//13. STABLE SAVE (YUSUF - 30/01/09)
		}else if(businessId.equals("143") && (lsdbs_number == 4 || lsdbs_number == 5)) {
//			(Deddy) Perubahan perhitungan komisi sesuai  SK.Direksi No. 068/AJS-SK/VI/2009
//			perhitungan yang lama berdasarkan MGI.
////			int mgi = uwDao.selectMasaGaransiInvestasi(spajBill.getNo_spaj());
////			
////			if(mgi == 1) 		li_pct_kom = 0.0167;
////			else if(mgi == 3) 	li_pct_kom = 0.05;
////			else if(mgi == 6) 	li_pct_kom = 0.1;
////			else if(mgi == 12) 	li_pct_kom = 0.2;
//			
////			if(	(spajBill.getLku_id().equals("01") && spajBill.getLdec_premi().doubleValue() >= 100000000) || 
////				(spajBill.getLku_id().equals("02") && spajBill.getLdec_premi().doubleValue() >= 10000)) {
////				if(mgi == 1) 		li_pct_kom = 0.05;
////				else if(mgi == 3) 	li_pct_kom = 0.15;
////				else if(mgi == 6) 	li_pct_kom = 0.3;
////				else if(mgi == 12) 	li_pct_kom = 0.6;
////			}
			if(lsdbs_number==4){
				li_pct_kom = 0.58;
			}else if(lsdbs_number==5){
				//li_pct_kom = 2.8; // KHUSUS BII (SK. Direksi No. 060/AJS-SK/V/2009)
				li_pct_kom = 2.5; //(Deddy - 28/7/2009)berlaku tgl : 1 agustus 2009 berdasarkan SK. Direksi No. 088/AJS-SK/VII/2009
			}
		//14. STABLE SAVE PREMI BULANAN (YUSUF - 31/04/09)
		}else if(products.stableSave(businessId)) {
			li_pct_kom = 0.0167; //karena mgi nya cuman 1 bulan aja
		}
		*/
		
		//18 Aug 09 (Yusuf) - Start now, semua hardcoding diatas udah gak perlu, pake query dari seno,
		//jadi tidak ada hardcoding lagi (khusus untuk perhitungan rate komisi fee based (MCR_FLAG = 1)
		/*
		Double feeBase = bacDao.selectRateKomisiFeeBasedBancassurance(spajBill.getNo_spaj()); // hub seno kl ada error
		if(feeBase == null){
			errors.reject("", "Harap cek perhitungan fee based income. Silahkan hubungi Kuseno (IT)");
			throw new Exception(errors);
		}else{
			li_pct_kom = feeBase;
		}*/
		
		//12 Mar 2010 (Yusuf) - Start now, query diatas pun tidak perlu lagi, pake function oracle yg dibuat oleh seno
		//(khusus untuk perhitungan rate komisi fee based (MCR_FLAG = 1)
		Double persenKomisi = bacDao.selectPersenKomisiReffBii(spajBill.getNo_spaj());
		persenKomisi = 0.;
//		Double persenInsentif = bacDao.selectPersenInsentifReffBii(spajBill.getNo_spaj());
		if(persenKomisi == null){
			errors.reject("", "Harap cek perhitungan fee based income. Silahkan hubungi Kuseno (IT)");
			throw new Exception(errors);
		}else{
			li_pct_kom = persenKomisi;
		}
		
		double cashBack = 0;
		
		Integer referal = uwDao.selectJenisPenutupBII(spajBill.getNo_spaj());
		if(referal == null) referal = -1;
		int jenisReferal = referal;
		
		Date tglProd = uwDao.selectProductionDate(spajBill.getNo_spaj(), spajBill.getTahun_ke(), spajBill.getPremi_ke());
		Date tglBayar = uwDao.selectTanggalBayar(spajBill.getNo_spaj());
			
		//Yusuf - 01/08/2008 - per tgl bayar agustus, Untuk powersave bank sinarmas, 
		//ada % cashback dan diinsert dengan mcr_flag = 4, JENIS REFERRAL YG OTHERS GAK DAPET
		if(jenisReferal != 8 && tglBayar.compareTo(completeDateFormatStripes.parse("01-08-2008 00:00")) >= 0) {
			//SIMAS PRIMA dan SIMAS PRIMA BULANAN
			if((businessId.equals("142") && lsdbs_number == 2) || (businessId.equals("175") && lsdbs_number == 2) || (businessId.equals("158") && lsdbs_number == 6)) {
				//UNTUK RUPIAH
				if(spajBill.getLku_id().equals("01")) {
					
					cashBack = 0;
					
					BigDecimal batasBawah = new BigDecimal("100000000");
					BigDecimal batas = new BigDecimal("1000000000");
					int mgi = uwDao.selectMasaGaransiInvestasi(spajBill.getNo_spaj(), 1, 1);
					
					if(spajBill.getLdec_premi().doubleValue() >= batasBawah.doubleValue()) {
						
						if(spajBill.getLdec_premi().doubleValue() < batas.doubleValue()) {
							if(mgi == 3) cashBack = 0.1875;	//0.75/4;
							else if(mgi == 6) cashBack = 0.5;
							else if(mgi == 12) cashBack = 1;
						}else {
							if(mgi == 3) cashBack = 0.25;
							else if(mgi == 6) cashBack = 0.625;	//1.25/2;
							else if(mgi == 12) cashBack = 1.25;
						}
					}

					BigDecimal seratus_juta = new BigDecimal("100000000");
					BigDecimal satu_milyar = new BigDecimal("1000000000");
					
					//yusuf - 5 - 17 sept 2008, untuk rupiah, ada cashback
					if(tglBayar.compareTo(completeDateFormatStripes.parse("05-09-2008 00:00")) >= 0 
							&& tglBayar.compareTo(completeDateFormatStripes.parse("17-09-2008 00:00")) <= 0) {
						double premi = spajBill.getLdec_premi().doubleValue();
						if(premi >= seratus_juta.doubleValue() && premi < satu_milyar.doubleValue()) {
							if(mgi == 3) cashBack = 0.25; //1 p.a.
							else if(mgi == 6) cashBack = 0.625; //1.25 p.a.
							else if (mgi == 12) cashBack = 1.25;//1.25 p.a.
						}else if(premi >= satu_milyar.doubleValue()) {
							if(mgi == 3) cashBack = 0.3125; //1.25 p.a.
							else if(mgi == 6) cashBack = 0.75; //1.5 p.a.
							else if (mgi == 12) cashBack = 1.5;//1.5 p.a.
						}
					
					//yusuf - 18 - 30 september 2008, untuk rupiah, cashback 2% dan 2.25% bila diatas 100 jt
					}else if(tglBayar.compareTo(completeDateFormatStripes.parse("18-09-2008 00:00")) >= 0 
							&& tglBayar.compareTo(completeDateFormatStripes.parse("30-09-2008 00:00")) <= 0) {
						double premi = spajBill.getLdec_premi().doubleValue();
						if(premi >= seratus_juta.doubleValue() && premi < satu_milyar.doubleValue()) {
							if(mgi == 3) cashBack = 0.5; //2 p.a.
							else if(mgi == 6) cashBack = 1.0; //2 p.a.
							else if (mgi == 12) cashBack = 2.0;//2 p.a.
						}else if(premi >= satu_milyar.doubleValue()) {
							if(mgi == 3) cashBack = 0.5; //2 p.a.
							else if(mgi == 6) cashBack = 1.125; //2.25 p.a.
							else if (mgi == 12) cashBack = 2.25;//2.25 p.a.
						}

					//yusuf - 1 - 20 oktober 2008, untuk rupiah, cashback 2%
					}else if(tglBayar.compareTo(completeDateFormatStripes.parse("01-10-2008 00:00")) >= 0 
							&& tglBayar.compareTo(completeDateFormatStripes.parse("20-10-2008 00:00")) <= 0) {
						double premi = spajBill.getLdec_premi().doubleValue();
						if(premi >= seratus_juta.doubleValue()) {
							if(mgi == 3) cashBack = 0.5; //2 p.a.
							else if(mgi == 6) cashBack = 1.0; //2 p.a.
							else if (mgi == 12) cashBack = 2.0;//2 p.a.
						}

					//yusuf - 21 - 29 oktober 2008, untuk rupiah, cashback 2%
					}else if(tglBayar.compareTo(completeDateFormatStripes.parse("21-10-2008 00:00")) >= 0 
							&& tglBayar.compareTo(completeDateFormatStripes.parse("29-10-2008 00:00")) <= 0) {
						double premi = spajBill.getLdec_premi().doubleValue();
						if(premi >= seratus_juta.doubleValue()) {
							if(mgi == 3) cashBack = 0.375; //1.5 p.a.
							else if(mgi == 6) cashBack = 0.75; //1.5 p.a.
							else if (mgi == 12) cashBack = 1.5;//1.5 p.a.
						}

					//yusuf - diatas 30 oktober 2008, cashback = 0
					}else if(tglBayar.compareTo(completeDateFormatStripes.parse("30-10-2008 00:00")) >= 0) {
						cashBack = 0.;
					}

					li_pct_kom -= cashBack;

				//UNTUK DOLLAR, BERLAKUNYA MULAI 15 AGUSTUS 2008
				}else if(spajBill.getLku_id().equals("02")) {
					if(tglBayar.compareTo(completeDateFormatStripes.parse("15-08-2008 00:00")) >= 0
							&& tglBayar.compareTo(completeDateFormatStripes.parse("29-10-2008 00:00")) <= 0) {
						int li_mgi = uwDao.selectMasaGaransiInvestasi(spajBill.getNo_spaj(), 1, 1);
						if(spajBill.getLdec_premi().doubleValue() >= 50000) {
							if(spajBill.getLdec_premi().doubleValue() < 500000) {
								if(li_mgi == 3) cashBack = 0.125;
								else if(li_mgi == 6) cashBack = 0.375;
								else if(li_mgi == 12) cashBack = 0.75;
							}else{
								if(li_mgi == 3) cashBack = 0.1875;	//0.75/4;
								else if(li_mgi == 6) cashBack = 0.5;
								else if(li_mgi == 12) cashBack = 1;
							}
							li_pct_kom -= cashBack;
						}
					//yusuf - diatas 30 oktober 2008, cashback = 0
					}else if(tglBayar.compareTo(completeDateFormatStripes.parse("30-10-2008 00:00")) >= 0) {
						cashBack = 0.;
					}
				}
			//SIMAS STABIL LINK
			}else if(businessId.equals("164") && lsdbs_number == 2) {
				
				double premi = uwDao.selectTotalPremiNewBusiness(spajBill.getNo_spaj());
				
				//UNTUK RUPIAH
				if(spajBill.getLku_id().equals("01") && premi >= 500000) {
					int mti = uwDao.selectMasaGaransiInvestasi(spajBill.getNo_spaj(), spajBill.getTahun_ke(), spajBill.getPremi_ke());
					
					cashBack = 0;
					
					if(mti == 3) cashBack = 0.1875; //0.75 p.a.
					else if(mti == 6) cashBack = 0.5; //1 p.a.
					else if (mti == 12) cashBack = 1;//1 p.a.

					BigDecimal seratus_juta = new BigDecimal("100000000");
					BigDecimal satu_milyar = new BigDecimal("1000000000");

					//yusuf - 5 - 17 sept 2008, untuk rupiah, ada cashback
					if(tglBayar.compareTo(completeDateFormatStripes.parse("05-09-2008 00:00")) >= 0 
							&& tglBayar.compareTo(completeDateFormatStripes.parse("17-09-2008 00:00")) <= 0) {
						if(premi >= seratus_juta.doubleValue() && premi < satu_milyar.doubleValue()) {
							if(mti == 3) cashBack = 0.25; //1 p.a.
							else if(mti == 6) cashBack = 0.625; //1.25 p.a.
							else if (mti == 12) cashBack = 1.25;//1.25 p.a.
						}else if(premi >= satu_milyar.doubleValue()) {
							if(mti == 3) cashBack = 0.3125; //1.25 p.a.
							else if(mti == 6) cashBack = 0.75; //1.5 p.a.
							else if (mti == 12) cashBack = 1.5;//1.5 p.a.
						}
					
					//yusuf - 18 - 30 september 2008, untuk rupiah, cashback 2% dan 2.25% bila diatas 100 jt
					}else if(tglBayar.compareTo(completeDateFormatStripes.parse("18-09-2008 00:00")) >= 0 
							&& tglBayar.compareTo(completeDateFormatStripes.parse("30-09-2008 00:00")) <= 0) {
						if(premi >= seratus_juta.doubleValue() && premi < satu_milyar.doubleValue()) {
							if(mti == 3) cashBack = 0.5; //2 p.a.
							else if(mti == 6) cashBack = 1.0; //2 p.a.
							else if (mti == 12) cashBack = 2.0;//2 p.a.
						}else if(premi >= satu_milyar.doubleValue()) {
							if(mti == 3) cashBack = 0.5; //2 p.a.
							else if(mti == 6) cashBack = 1.125; //2.25 p.a.
							else if (mti == 12) cashBack = 2.25;//2.25 p.a.
						}

					//yusuf - 1 - 20 oktober 2008, untuk rupiah, cashback 2%
					}else if(tglBayar.compareTo(completeDateFormatStripes.parse("01-10-2008 00:00")) >= 0 
							&& tglBayar.compareTo(completeDateFormatStripes.parse("20-10-2008 00:00")) <= 0) {
						if(premi >= seratus_juta.doubleValue()) {
							if(mti == 3) cashBack = 0.5; //2 p.a.
							else if(mti == 6) cashBack = 1.0; //2 p.a.
							else if (mti == 12) cashBack = 2.0;//2 p.a.
						}

					//yusuf - 21 - 29 oktober 2008, untuk rupiah, cashback 2%
					}else if(tglBayar.compareTo(completeDateFormatStripes.parse("21-10-2008 00:00")) >= 0 
							&& tglBayar.compareTo(completeDateFormatStripes.parse("29-10-2008 00:00")) <= 0) {
						if(premi >= seratus_juta.doubleValue()) {
							if(mti == 3) cashBack = 0.375; //1.5 p.a.
							else if(mti == 6) cashBack = 0.75; //1.5 p.a.
							else if (mti == 12) cashBack = 1.5;//1.5 p.a.
						}

					//yusuf - diatas 30 oktober 2008, cashback = 0
					}else if(tglBayar.compareTo(completeDateFormatStripes.parse("30-10-2008 00:00")) >= 0) {
						cashBack = 0.;
					}

					li_pct_kom -= cashBack;

				//UNTUK DOLLAR
				}else if(spajBill.getLku_id().equals("02") && premi >= 50000) {
					int mti = uwDao.selectMasaGaransiInvestasi(spajBill.getNo_spaj(), spajBill.getTahun_ke(), spajBill.getPremi_ke());
					
					//yusuf - diatas 30 oktober 2008, cashback = 0
					if(tglBayar.compareTo(completeDateFormatStripes.parse("30-10-2008 00:00")) >= 0) cashBack = 0.;
					else if(mti == 3) cashBack = 0.25; //0.5 p.a.
					else if(mti == 6) cashBack = 0.375; //0.75 p.a.
					else if (mti == 12) cashBack = 0.75;//0.75 p.a.
					li_pct_kom -= cashBack;
				}
				
			}
		}
		
		double temp = spajBill.getLdec_premi();
		if(products.stableLink(businessId)) {
			spajBill.setLdec_premi(uwDao.selectTotalPremiNewBusiness(spajBill.getNo_spaj()));
		}
		
		spajBill.setLdec_komisi(new Double((spajBill.getLdec_premi() * spajBill.getLdec_kurs() * li_pct_kom / 100) + sisaRefund));
		spajBill.setMkt_allocation(new Double((spajBill.getLdec_premi() * spajBill.getLdec_kurs() * li_pct_mkt_alloc / 100))); //Yusuf - Marketing allocation
		spajBill.setLdec_tax(new Double(0)); //tidak ada tax
		spajBill.setLdec_komisi(FormatNumber.round(spajBill.getLdec_komisi(),  0));
		spajBill.setMcr_flag(new Integer(1));
//		spajBill.setJlh_insentif((persenInsentif/100) * spajBill.getLdec_premi() * spajBill.getLdec_kurs()); //eka.persen_insentif buatan seno
		//flag = 1 untuk Bank
		
		this.uwDao.insertMst_comm_reff_bii(spajBill);
		
		msg.append("Premi = " + nf.format(spajBill.getLdec_premi()) + ", Fee Based BII (1) = " + nf.format(spajBill.getLdec_komisi() - spajBill.getLdec_tax()));
		msg.append("\n\nMGI = " + uwDao.selectMasaGaransiInvestasi(spajBill.getNo_spaj(), 1, 1));
		msg.append("\nKomisi = " + ((spajBill.getLdec_komisi()/spajBill.getLdec_premi())*100) + "%");
		msg.append("\nMkt Alloc = " + ((spajBill.getMkt_allocation()/spajBill.getLdec_premi())*100) + "%");
		msg.append("\nProduk = " + businessId + "-" + lsdbs_number);

		//Yusuf - 01/08/2008 - per produksi agustus, Untuk powersave bank sinarmas, ada % cashback dan diinsert dengan mcr_flag = 4, JENIS REFERRAL YG OTHERS GAK DAPET
		if(jenisReferal != 8 && uwDao.selectTanggalBayar(spajBill.getNo_spaj()).compareTo(completeDateFormatStripes.parse("01-08-2008 00:00")) >= 0) {
			if((businessId.equals("142") && lsdbs_number == 2) || (businessId.equals("175") && lsdbs_number == 2) || (businessId.equals("158") && lsdbs_number == 6)) {
				//baik dollar maupun rupiah, harus diatas batas bawahnya baru dapet
				//bila dollar, harus diatas 15 agustus baru dapet
				if ((spajBill.getLku_id().equals("01") && spajBill.getLdec_premi().doubleValue() >= 100000000) || 
					(spajBill.getLku_id().equals("02") && (uwDao.selectTanggalBayar(spajBill.getNo_spaj()).compareTo(completeDateFormatStripes.parse("15-08-2008 00:00")) >= 0) && spajBill.getLdec_premi().doubleValue() >= 50000)) {

					//premi tidak di RUPIAH kan, karena cashback ini balik ke nasabah dalam kurs awalnya
					//double premi = spajBill.getLdec_premi() * spajBill.getLdec_kurs();
					double premi = spajBill.getLdec_premi();
					
					//1. insert comm reff bii dgn mcr flag 4 sebesar % cashback
					spajBill.setLdec_komisi(new Double((premi * cashBack / 100) + sisaRefund));
					spajBill.setMkt_allocation(new Double((premi * li_pct_mkt_alloc / 100)));
					spajBill.setLdec_tax(new Double(0)); //tidak ada tax
					spajBill.setLdec_komisi(FormatNumber.round(spajBill.getLdec_komisi(),0));
					spajBill.setMcr_flag(new Integer(4));
					//flag = 4 untuk cashback bank sinarmas
					this.uwDao.insertMst_comm_reff_bii(spajBill);
					
					//2. insert pasangannya sebesar % cashback juga
					Rekening_client rek = this.uwDao.selectRekeningNasabah(spajBill.getNo_spaj());
					boolean lolos = true;
					String pesan = "Maaf, tetapi informasi rekening nasabah polis ini tidak lengkap.";
					if(rek==null) {
						lolos=false;
					}else if(rek.getNo_account()==null) {
						lolos=false;
					}else if(rek.getNo_account().length()!=10) {
						pesan = "Maaf, tetapi nomor rekening nasabah ["+rek.getNo_account()+"] tidak sesuai ("+rek.getNo_account().length()+" karakter). Harap cek ulang rekening nasabah";
						lolos=false;
					}
					if(!lolos) {
						errors.reject("", pesan);
						throw new Exception(errors);
					}else {
						spajBill.setNo_account(rek.getNo_account());
						spajBill.setRefund(spajBill.getLdec_komisi());
						spajBill.setJn_bank(2);
						spajBill.setTgl_refund(spajBill.getLdt_prod());
						//insert refund dgn flag = 4, dengan jumlah yg sama dengan komm reff bii = 1%
						this.uwDao.insertUploadRefund(spajBill);
					}				
				}
				
			}else if(businessId.equals("164") && lsdbs_number == 2) {
				
				//premi tidak di RUPIAH kan, karena cashback ini balik ke nasabah dalam kurs awalnya
				//double premi = spajBill.getLdec_premi() * spajBill.getLdec_kurs();
				double premi = spajBill.getLdec_premi();
				
				//1. insert comm reff bii dgn mcr flag 4 sebesar % cashback
				spajBill.setLdec_komisi(new Double((premi * cashBack / 100) + sisaRefund));
				spajBill.setMkt_allocation(new Double((premi * li_pct_mkt_alloc / 100)));
				spajBill.setLdec_tax(new Double(0)); //tidak ada tax
				spajBill.setLdec_komisi(FormatNumber.round(spajBill.getLdec_komisi(), 0));
				spajBill.setMcr_flag(new Integer(4));
				//flag = 4 untuk cashback bank sinarmas
				this.uwDao.insertMst_comm_reff_bii(spajBill);
				
				//2. insert pasangannya sebesar % cashback juga
				Rekening_client rek = this.uwDao.selectRekeningNasabah(spajBill.getNo_spaj());
				boolean lolos = true;
				String pesan = "Maaf, tetapi informasi rekening nasabah polis ini tidak lengkap.";
				if(rek==null) {
					lolos=false;
				}else if(rek.getNo_account()==null) {
					lolos=false;
				}else if(rek.getNo_account().length()!=10) {
					pesan = "Maaf, tetapi nomor rekening nasabah ["+rek.getNo_account()+"] tidak sesuai ("+rek.getNo_account().length()+" karakter). Harap cek ulang rekening nasabah";
					lolos=false;
				}
				if(!lolos) {
					errors.reject("", pesan);
					throw new Exception(errors);
				}else {
					spajBill.setNo_account(rek.getNo_account());
					spajBill.setRefund(spajBill.getLdec_komisi());
					spajBill.setJn_bank(2);
					spajBill.setTgl_refund(spajBill.getLdt_prod());
					//insert refund dgn flag = 4, dengan jumlah yg sama dengan komm reff bii = % cashback
					this.uwDao.insertUploadRefund(spajBill);
				}				
			}
		}
		
		
//		email.send(
//		new String[] {props.getProperty("admin.yusuf")},null, 
//		"Proses Komisi Bancassurance [" + spajBill.getNo_spaj() + "]", msg.toString(), currentUser);
		
		/** START PROSES KETIGA : INSERT REFUND, TAPI HANYA UNTUK PRODUKSI DIBAWAH MARET 2008 */
		
		if(spajBill.getRefund()!=null && spajBill.getFlag_topup()!=1 && spajBill.getFlag_topup()!=2) {
			if(spajBill.getRefund()>0) {
				Rekening_client rek = this.uwDao.selectRekeningNasabah(spajBill.getNo_spaj());
				boolean lolos = true;
				String pesan = "Maaf, tetapi informasi rekening nasabah polis ini tidak lengkap.";
				if(rek==null) {
					lolos=false;
				}else if(rek.getNo_account()==null) {
					lolos=false;
				}else if(rek.getNo_account().length()!=10) {
					pesan = "Maaf, tetapi nomor rekening nasabah ["+rek.getNo_account()+"] tidak sesuai ("+rek.getNo_account().length()+" karakter). Harap cek ulang rekening nasabah";
					lolos=false;
				}
				if(!lolos) {
					errors.reject("", pesan);
					throw new Exception(errors);
				}else {
					spajBill.setNo_account(rek.getNo_account());
					
					//Yusuf - Mulai Produksi Maret 2008, Refund tidak dihitung harian (tidak diinsert kemari)
					//tapi diproses bulanan oleh orang bancass
					if(Integer.valueOf(this.uwDao.selectBulanProduksi(spajBill.getNo_spaj())) < 200803) {
						this.uwDao.insertUploadRefund(spajBill);
					}
				}
			}
		}
		
		spajBill.setLdec_premi(temp);		
		
		return errors;
	}
	
	public boolean prosesKomisiTopUpIndividu(String spaj, BindException errors, User currentUser, Integer premi_ke) throws Exception{ //of_komisi_tu_new()
			if(logger.isDebugEnabled())logger.debug("PROSES: prosesKomisiTopUpIndividu (of_komisi_tu_new())");
			
			List lds_kom;
			int i; double ldec_premi; double ldec_komisi=0; Double ldec_kurs = new Double(1); 
			double[] ldec_pct_kom = {0,3, 8.5, 22.5, 2};String ls_region; String ls_jenis;
			String ls_agent_rekruter; String ls_acct; String ls_nama; 
			String li_bisnis; int li_rekrut=0;
			Date ldt_tgl_aktif; Date ldt_prod; 
			Map rekruter = new HashMap();
			
			Map pribadi = this.uwDao.selectInfoPribadi(spaj);
			ls_region = (String) pribadi.get("REGION");
			String lca_id = ls_region.substring(0,2); 
			if("37,46,52".indexOf(lca_id)>-1){ //Agency) { //37 agency, 46 arthamas, 52 Agency Arthamas (Deddy 22 Feb 2012)
				Pemegang pp = bacDao.selectpp(spaj);
				if ((Integer) pp.getMsag_asnew()==1){
					return prosesKomisiNewAgencySystem(spaj,currentUser,errors,premi_ke);
				}else{
					return prosesKomisiTopUpAgencySystem(spaj, errors, currentUser, premi_ke, lca_id);
				}
			}else if(ls_region.substring(0,2).equals("42")) ldec_pct_kom = new double[]{0, 0, 5, 10, 2};
			
			lds_kom = this.uwDao.selectViewKomisiAsli(spaj);
			li_bisnis = FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 3);
			ldec_premi = this.uwDao.selectPremiTopUpUnitLink(spaj, premi_ke);
			int bulanProd = Integer.valueOf(uwDao.selectBulanProduksi(spaj));
			
			if("087, 101, 115, 152".indexOf(li_bisnis) > -1) 
				ldec_pct_kom[4] = 0.75;
			else if("116, 119, 140, 153, 159, 160, 199,218".indexOf(li_bisnis) > -1) 
				ldec_pct_kom[4] = 1.5;
			else if(products.cerdas(li_bisnis)) 
				ldec_pct_kom[4] = 1;
			else if(isEkalink88Plus(spaj, true)) {
				ldec_pct_kom[4] = 2;
			}else if(isLinkSingle(spaj, true)) {
				ldec_pct_kom[4] = 3;
			}

			if(!lds_kom.isEmpty()) {
				Commission tmp = (Commission) lds_kom.get(0);
				if(tmp.getKurs_id().equals("02")){
					ldec_kurs = new Double(this.uwDao.selectLastCurrency(spaj, 1, 1));
					if(ldec_kurs==null){
						errors.reject("payment.noLastCurrency");
						throw new Exception(errors);
					}
				}
				
				for(i = 0; i<lds_kom.size(); i++){
					Commission komisi = (Commission) lds_kom.get(i);
					komisi.setLsbs_linebus(bacDao.selectLineBusLstBisnis(komisi.getBisnis_id().toString()));
					if(i==0) ldec_premi *= ldec_kurs;
					if(komisi.getLev_kom()==4){
						rekruter = this.uwDao.selectRekruter(komisi.getAgent_id());
						if(rekruter!=null){
							ls_jenis = (String) rekruter.get("MSRK_JENIS_REKRUT");
							
		//					 O.R. 50%
							if(ls_jenis.equals("3") || ls_jenis.equals("4"))
								li_rekrut=2;
							ldt_tgl_aktif = this.uwDao.selectAgentActiveDate(komisi.getAgent_id());
							//Untuk tambang emas th 2004, kalo masih aktif; or 50% semua, kalo tdk um 12.5, 10% reward buat um terakhir
							// u/ tambang emas baru per 1 mar 05, or um = 12.5%, yg lain sama
							
							if(ldt_tgl_aktif.compareTo(defaultDateFormatReversed.parse("20050301"))>=0){
								li_rekrut = 3;
							}else if(FormatDate.dateDifference(ldt_tgl_aktif, komisi.getTgl_kom(), true)<=365) {
								//te 2004, masih berlaku, or 50% semua
								li_rekrut = 2;
							}
	
							// li_rekrut = 0 -> tdk aktif
							// li_rekrut = 1 -> um 12.5%, reward um 10%
							// li_rekrut = 2 -> or = 50%
							// li_rekrut = 3 -> um 12.5%, reward rekruter 10%
	
						}
					}
					
					//Kalo masih aktif
					if(komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
						//TAMBAHAN OLEH YUSUF UNTUK PRODUK2 SINGLE (IM No. 132/IM-DIR/XII/2007) - 16/01/2008
						ldec_komisi = cekLinkSingle(spaj, ldec_komisi, komisi.getLev_kom(), true);
						//(Deddy) perhitungan rate komisi stabil link diambil lgsg dari lst_comm_new
						Commission temp = new Commission(); 
						PropertyUtils.copyProperties(temp, komisi);
						if(komisi.getBisnis_id().toString().equals("164")){//
							temp.setLsco_year(1);
							temp.setLsco_jenis(1);
							Date ldt_temp = this.uwDao.selectLastComissionDate(temp);
							Map commstabil = this.uwDao.selectCommisionAndBonus(ldt_temp, temp);
							if(commstabil!=null){
								if(commstabil.get("LSCO_COMM")!=null) komisi.setKomisi(Double.valueOf(commstabil.get("LSCO_COMM").toString()));
								if(commstabil.get("LSCO_BONUS")!=null) komisi.setBonus(Double.valueOf(commstabil.get("LSCO_BONUS").toString()));
							}
							Integer mgi = uwDao.selectMgiNewBusiness(spaj);
							if(bulanProd>=201105){
								if(mgi == 1) {
									komisi.setKomisi(0.05);
								}else if(mgi == 3) {
									komisi.setKomisi(0.15);
								}else if(mgi == 6) {
									komisi.setKomisi(0.3);  
								}else if(mgi == 12) {
									komisi.setKomisi(0.6);
								}else if(mgi == 24) {
									komisi.setKomisi(1.2);
								}else if(mgi == 36) {
									komisi.setKomisi(1.35);
								}else {
									throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
								}
							}else{
								if(mgi == 1) {
									komisi.setKomisi(0.017);
								}else if(mgi == 3) {
									komisi.setKomisi(0.05);
								}else if(mgi == 6) {
									komisi.setKomisi(0.1);  
								}else if(mgi == 12) {
									komisi.setKomisi(0.2);
								}else if(mgi == 24) {
									komisi.setKomisi(0.4);
								}else if(mgi == 36) {
									komisi.setKomisi(0.6);
								}else {
									throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
								}
							}
							
						}else if(komisi.getBisnis_id().toString().equals("174")){
							Integer mgi = uwDao.selectMgiNewBusiness(spaj);
							if(mgi == 1) {
								komisi.setKomisi(0.05);
							}else if(mgi == 3) {
								komisi.setKomisi(0.15);
							}else if(mgi == 6) {
								komisi.setKomisi(0.3);
							}else if(mgi == 12) {
								komisi.setKomisi(0.6);
							}else if(mgi == 24) {
								komisi.setKomisi(1.2);
							}else if(mgi == 36) {
								komisi.setKomisi(1.35);
							}else {
								throw new RuntimeException("Harap cek perhitungan komisi Stable Link Syariah, mgi = " + mgi);
							}
						}else {
							komisi.setKomisi(new Double(ldec_pct_kom[komisi.getLev_kom()]));
						}
						
						
						//komisi OR agent get agent 28/01/04 (hm)
						if(i>0 && li_rekrut==2) komisi.setKomisi(new Double(komisi.getKomisi()/2));
						if(i==1 && li_rekrut==3) komisi.setKomisi(new Double(12.5));
						
						//stable link, ada or sept - dec 2008, seliain itu tidak ada
						
						if(komisi.getLev_kom().intValue() < 4 && products.stableLink(komisi.getBisnis_id().toString()) && (bulanProd < 200809 || bulanProd > 200812)) {
							komisi.setKomisi(0.);
						}						
						
						ldec_komisi = (ldec_premi * komisi.getKomisi() / 100);
						//logger.info("ldec_komisi="+ldec_komisi+"\nldec_premi="+ldec_premi+"\npct_kom="+komisi.getKomisi());
						komisi.setTax(new Double(ldec_komisi * komisi.getTax()/100));
						Date sysdate = commonDao.selectSysdateTruncated(0);
						if(komisi.getTax()>0)komisi.setTax(f_load_tax(new Double(ldec_komisi), sysdate, komisi.getAgent_id()));
						komisi.setTax(FormatNumber.rounding(komisi.getTax(), true, 25));
						ldec_komisi = FormatNumber.round(new Double(ldec_komisi),  0);
						if(komisi.getTax()==null)komisi.setTax(new Double(0));
						komisi.setCo_id(sequenceMst_commission(11));
						komisi.setReg_spaj(spaj);
						
						
						//referensi point(tambang emas) -> Top Up
						Integer cek = uwDao.seleckCekRef(spaj,"1");//1 = kode_program
						if(cek>0){
							String lsbs_id = uwDao.selectLsbsId(spaj);
							if(lca_id.equals("42") && lsbs_id.equals("140")){
								double komisi_ref = new Double(ldec_komisi)/2;
								komisi.setKomisi(ldec_komisi);//komisi tetap 100%
								Integer lt_id = uwDao.selectLtId(spaj,ldec_premi);
								if(lt_id!=null){//jika ada
									if(lt_id==2){//top up tunggal
										double rate = uwDao.selectRatePoint(lsbs_id);
										int point = (int) (komisi_ref/rate);
										//int point = ck.getPoint(new Double(tmp.get("MSDB_PREMIUM").toString()),Integer.valueOf(tmp.get("LSBS_ID").toString()),Integer.valueOf(tmp.get("LSDBS_NUMBER").toString()), new Double(tmp.get("MSDB_PREMIUM").toString()), null);
										String id_trx = bacDao.selectIdTrx(null,spaj,"2");
										bacDao.insertPwrDTrx(id_trx, props.getProperty("id.item.point.topup.tunggal"), spaj, ldec_premi, point, currentUser.getLus_id());
										//insert ke mst_deduct
										this.uwDao.insertMst_deduct(komisi.getCo_id(), komisi_ref, currentUser.getLus_id(), 1, 10, "Komisi program MGM Top Up Tunggal");
									}
									
									if(lt_id==5){//top up berkala
										double rate = uwDao.selectRatePoint(lsbs_id);
										int point = (int) (komisi_ref/rate);
										String id_trx = bacDao.selectIdTrx(null,spaj,"2");
										bacDao.insertPwrDTrx(id_trx, props.getProperty("id.item.point.topup.berkala"), spaj, ldec_premi, point, currentUser.getLus_id());
										//insert ke mst_deduct
										this.uwDao.insertMst_deduct(komisi.getCo_id(), komisi_ref, currentUser.getLus_id(), 1, 10, "Komisi program MGM Top Up Berkala");
									}
								}else{//jka tidak ada, maka cuma top up tunggal
									double rate = uwDao.selectRatePoint(lsbs_id);
									int point = (int) (komisi_ref/rate);
									String id_trx = bacDao.selectIdTrx(null,spaj,"2");
									bacDao.insertPwrDTrx(id_trx, props.getProperty("id.item.point.topup.tunggal"), spaj, ldec_premi, point, currentUser.getLus_id());
									//insert ke mst_deduct
									this.uwDao.insertMst_deduct(komisi.getCo_id(), komisi_ref, currentUser.getLus_id(), 1, 10, "Komisi program MGM Top Up Tunggal");
								}
							}else{
								komisi.setKomisi(new Double(ldec_komisi));
							}
						}else{
							komisi.setKomisi(new Double(ldec_komisi));
						}
						//end referensi
							
						komisi.setKomisi(new Double(ldec_komisi));
						komisi.setNilai_kurs(ldec_kurs);
						komisi.setLus_id(currentUser.getLus_id());
						komisi.setMsbi_tahun_ke(new Integer(1));
						komisi.setMsbi_premi_ke(premi_ke);
						if(komisi.getKomisi()>0) {
							this.uwDao.insertMst_commission(komisi, sysdate);
							
							//Yusuf - 10/07/09 - Bonus Komisi untuk TopUp StableLink dipisah ke comm_bonus
							if(!pribadi.get("PRIBADI").toString().equals("1") && products.stableLink(li_bisnis)){
								if(i==0) {
									prosesBonusKomisi(
											"individu", ls_region, komisi.getReg_spaj(), komisi.getAgent_id(), komisi.getCo_id(), 
											komisi.getKurs_id(), ldec_premi, ldec_kurs, currentUser.getLus_id(), komisi.getKomisi(), komisi.getTax(),
											komisi.getMsbi_tahun_ke(), komisi.getMsbi_premi_ke(), komisi.getLev_kom(), komisi.getLsbs_linebus());
								}
							}						
							
						}
					}
					
					//proses rewards
					if(i==1){
						double ldec_reward; double ldec_tax;
						boolean lb_ada_reward = false;
						if(li_rekrut==3){
							if(rekruter!=null) {
								if((rekruter.get("NO_ACCOUNT")==null || rekruter.get("NO_ACCOUNT").toString().equals(""))&& rekruter.get("MSRK_AKTIF").toString().equals("1")){
									errors.reject("payment.noRecruiter", new Object[] {rekruter.get("MSRK_ID")}, "-");
									throw new Exception(errors);
								}	
								if(rekruter.get("MSRK_AKTIF").toString().equals("1")){
									lb_ada_reward=true;
								}
							}
						}
						
						if(products.stableLink(komisi.getBisnis_id().toString())) {
							lb_ada_reward = false;
						}
						
						if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())) {
							lb_ada_reward = false;
						}
						
						if(lca_id.equals("42")){
							lb_ada_reward = false;
						}
						
						if(lb_ada_reward){
							ldt_prod = this.uwDao.selectProductionDate(spaj, new Integer(1), new Integer(2));
							if(FormatDate.isDateBetween(ldt_prod, defaultDateFormatReversed.parse("20050401"), defaultDateFormatReversed.parse("20050630")))
								ldec_reward = ldec_premi * 0.2;
							else
								ldec_reward = ldec_premi * 0.1;
							
							Rekruter rek = uwDao.selectRekruterFromAgen(rekruter.get("MSRK_ID").toString());
							if(rek.getMsag_bay() == null) rek.setMsag_bay(0);
							
							double pengali = 1;
							
							//Yusuf (22/12/2010) bila business partner, maka dapat reward hanya 5%
							if(rek.getLcalwk().startsWith("61")) pengali = 0.5;
							else if(isLinkSingle(spaj, true) && bulanProd < 200904) pengali = 0.35;
							
							ldec_reward *= pengali;
							
							Date sysdate = commonDao.selectSysdateTruncated(0);
							ls_agent_rekruter = (String) rekruter.get("MSRK_ID");

							ldec_tax = f_load_tax(new Double(ldec_reward), sysdate, ls_agent_rekruter);
							ldec_reward = FormatNumber.round(new Double(ldec_reward),  0);
							ldec_tax = FormatNumber.rounding(new Double(ldec_tax), true, 25);
							
							ls_nama = (String) rekruter.get("MSRK_NAME");
							ls_jenis = (String) rekruter.get("MSRK_JENIS_REKRUT");
							ls_acct = StringUtils.replace(rekruter.get("NO_ACCOUNT").toString(), "-","");
							
							if(rekruter.get("MSAG_KOMISI")!=null && rek.getMsag_bay().intValue() != 1){
								if(rekruter.get("MSAG_KOMISI").toString().equals("1")){
									if(uwDao.selectbacCekAgen(ls_agent_rekruter)!=null){
										this.uwDao.insertMst_reward(spaj, 1, premi_ke, ls_jenis, ls_agent_rekruter,
												ls_nama, ls_acct, 
												rekruter.get("LBN_ID").toString(), new Double(ldec_reward), 
												new Double(ldec_tax), ldec_kurs, currentUser.getLus_id(), komisi.getLsbs_linebus());
									}
								}
							}
						}
					}
					
					if( i ==0 )ldec_premi = ldec_komisi;
					
				}
			}
			
			this.uwDao.updateMst_billingTopup(spaj);
			
			return true;
	}
	
	public boolean prosesKomisiTopUpAgencySystem(String spaj, BindException errors, User currentUser, Integer premi_ke, String lca_id) throws Exception{ //of_komisi_tu_as()
		if(logger.isDebugEnabled())logger.debug("PROSES: prosesKomisiTopUpAgencySystem (of_komisi_tu_as()) ");
		StringBuffer emailMessage = new StringBuffer();
		emailMessage.append("[SPAJ  ] " + spaj + "\n");
		NumberFormat nf = NumberFormat.getInstance();

		List lds_kom;
		List lds_prod;

		Map pribadi = this.uwDao.selectInfoPribadi(spaj);
		String prodDate = (String) pribadi.get("PROD_DATE");
		emailMessage.append("[PRODUCTION DATE] " + pribadi.get("MSPRO_PROD_DATE") + "\n");
		
		//prodDate = "2009";
		
		int i; double ldec_premi; double ldec_komisi=0; Double ldec_kurs = new Double(1); 
		double[] ldec_pct_kom = {0,3, 8.5, 22.5, 2};
		String li_bisnis; 

		//Yusuf - 5/1/09 - Mulai 2009, struktur komisi arthamas ada 6 tingkat, bukan 4 lagi
		if(Integer.parseInt(prodDate) > 2008 && lca_id.equals("46")) {
			ldec_pct_kom = new double[]{0, 0, 0, 0, 0, 0, 0};			
			lds_kom = this.uwDao.selectViewKomisiHybrid2009(spaj);
			lds_prod = this.uwDao.selectAgentsFromSpajHybrid2009(spaj);
			
		}else {
			lds_kom = this.uwDao.selectViewKomisiAsli(spaj);
			lds_prod = this.uwDao.selectAgentsFromSpaj(spaj);
		}
		
		if(!lds_kom.isEmpty()) {
			Commission tmp = (Commission) lds_kom.get(0);
			if(tmp.getKurs_id().equals("02")){
				ldec_kurs = new Double(this.uwDao.selectLastCurrency(spaj, 1, 1));
				if(ldec_kurs==null){
					errors.reject("payment.noLastCurrency");
					throw new Exception(errors);
				}
			}
			
			String ls_agent[] = new String[4];

			//ini rate OR untuk 37&52 (agency)
			int li_or[] = {0, 40, 30, 20};
			//ini rate OR untuk 46 (arthamas)
			if(lca_id.equals("46")) {
				li_or = new int[]{0, 35, 25, 20};
			}
			
			int li_persen[] = new int[4];
			int maxCount = 4;
			
			if(lds_kom.size()>4){
				ls_agent = new String[5];
				li_persen = new int[5];
			}
			
			//Yusuf - 5/1/09 - Mulai 2009, struktur komisi arthamas ada 6 tingkat, bukan 4 lagi
			if(Integer.parseInt(prodDate) > 2008 && lca_id.equals("46")) {
				ls_agent = new String[6];
				li_or = new int[]{0, 35, 25, 20, 5, 1}; //FC - SM - BM - DM - RM - RD
				li_persen = new int[6];
				maxCount = 6;
			}			
			
			int k=2;
			int ll_find;
			boolean ketemu = false;
			double persen=0;
			
			for(i=1; i<lds_kom.size(); i++){
				Commission temp = (Commission) lds_kom.get(i);
				temp.setLsbs_linebus(bacDao.selectLineBusLstBisnis(temp.getBisnis_id().toString()));
				ls_agent[i] = temp.getAgent_id();
				li_persen[i] = 0;
				ll_find = 0;
				do{
					ketemu=false;
					
					for(int j = ll_find; j<lds_prod.size(); j++){
						String msag_id = ((HashMap) lds_prod.get(j)).get("MSAG_ID").toString(); 
						if(msag_id.equals(ls_agent[i])){ 
							ll_find=j; ketemu=true; break; 
						}
					}
					if(ketemu){
						int lsle_id = ((BigDecimal) ((HashMap) lds_prod.get((int)ll_find)).get("LSLE_ID")).intValue(); 
						if(lsle_id != maxCount){
							li_persen[i] += li_or[k-1];
							k++;
						}
					}
					
					ll_find++;
				}while(ll_find <= maxCount);
			}			
			
			li_bisnis = FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 3);
			ldec_premi = this.uwDao.selectPremiTopUpUnitLink(spaj, premi_ke);
			
			List detBisnis = uwDao.selectDetailBisnis(spaj);
			String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
			
//			//komisi top up di hardcode
//			if("087, 101, 117, 152".indexOf(li_bisnis) > -1) ldec_pct_kom[4] = 0.75;
//			else if("118, 153".indexOf(li_bisnis) > -1) ldec_pct_kom[4] = 1.65;
//			else if("159, 160, 162".indexOf(li_bisnis) > -1) ldec_pct_kom[4] = 1.5;
//
//			//komisi top up untuk ekalink 88 plus = 2%
//			if(lsbs.equals("162") && (lsdbs.equals("007") || lsdbs.equals("008"))) {
//				ldec_pct_kom[4] = 2;
//			}
//			
//			if(isLinkSingle(spaj, true)) {
//				ldec_pct_kom[4] = 3;
//			}
			
			int bulanProd = Integer.parseInt(uwDao.selectBulanProduksi(spaj));
			
			for(i = 0; i<lds_kom.size(); i++){
				Commission komisi = (Commission) lds_kom.get(i);
				komisi.setLsbs_linebus(bacDao.selectLineBusLstBisnis(komisi.getBisnis_id().toString()));
				if(i==0) ldec_premi *= ldec_kurs;
				
				komisi.setFlag_mess(Boolean.TRUE);
				int li_cb_id = komisi.getCb_id();
				int li_cb_id_asli = komisi.getCb_asli();
				if(!products.progressiveLink(komisi.getBisnis_id().toString()) && !products.stableSavePremiBulanan(komisi.getBisnis_id().toString()) && !products.healthProductStandAlone(komisi.getBisnis_id().toString())){
					if( li_cb_id==1 || li_cb_id==2 || li_cb_id==6 ) komisi.setCb_id(3);
				}
				komisi.setCb_id(new Integer(li_cb_id));
				if(komisi.getBisnis_id() == 64 ) komisi.setBisnis_id(new Integer(56));
				else if(komisi.getBisnis_id() == 67 ) komisi.setBisnis_id(new Integer(54));
				
				if("37,52".indexOf(lca_id)>-1){ //Agency) { //37 agency, 46 arthamas, 52 Agency Arthamas (Deddy 22 Feb 2012)
					komisi.setLsco_jenis(new Integer(3)); //Commission agency system
				}else if(lca_id.equals("46")) {
					komisi.setLsco_jenis(new Integer(4)); //Commission Arthamas
				}
				
				getLamaTanggung(komisi);
				
				if(f_get_komisi(komisi, errors, 0, premi_ke, komisi.getKurs_id(), ldec_premi, maxCount)==true){
					ldec_pct_kom[maxCount] = komisi.getKomisi();
				}else {
					throw new RuntimeException("Harap cek perhitungan topup komisi agency system");
				}
				
	//			if(komisi.getLev_kom()==4){
	//				Map temp = this.uwDao.selectRekruter(komisi.getAgent_id());
	//				if(temp!=null){
						//Kalo masih aktif
				if(komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
					
					//Yusuf - 1/5/08 - stable link
					if(products.stableLink(String.valueOf(li_bisnis)) && i>0) { //OR stable link hanya ada sept - dec 2008
						if(bulanProd >= 200809 && bulanProd <= 200812) {
							komisi.setKomisi(new Double(li_persen[i] * 0.1)); //hanya 10% dari OR normal
						}else {
							komisi.setKomisi(0.); //selain sept - dec, tidak ada OR
						}
					}else if(products.stableLink(String.valueOf(li_bisnis)) && i==0) {
						List<Map> stable = uwDao.selectInfoStableLink(komisi.getReg_spaj());
						Double totalPremi = uwDao.selectTotalPremiNewBusiness(komisi.getReg_spaj());
						
						//stable link
						// bonus promosi stable link, masuk ke comm_bonus juga gak? gak tau deh
						if(li_bisnis.equals("164")) {
							for(Map m : stable) {
								int msl_no = ((BigDecimal) m.get("MSL_NO")).intValue();
								if(premi_ke == msl_no) {
									int mgi = ((BigDecimal) m.get("MSL_MGI")).intValue();
									double bonus_promosi = (double) 0;
									Date bdate = (Date) m.get("MSL_BDATE");
									if(bulanProd>=201105){
										if(mgi == 1) {
											komisi.setKomisi(0.05);
										}else if(mgi == 3) {
											komisi.setKomisi(0.15);
										}else if(mgi == 6) {
											komisi.setKomisi(0.3);
										}else if(mgi == 12) {
											komisi.setKomisi(0.6);
										}else if(mgi == 24) {
											komisi.setKomisi(1.2);
										}else if(mgi == 36) {
											komisi.setKomisi(1.35);
										}else {
											throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
										}
									}else{
										if(bdate.compareTo(defaultDateFormat.parse("01/01/2009")) < 0) {
											if(mgi == 1) {
												bonus_promosi = 0.033;
											}else if(mgi == 3) {
												bonus_promosi = 0.1;
											}else if(mgi == 6) {
												bonus_promosi = 0.2;
											}else if(mgi == 12) {
												bonus_promosi = 0.4;
											}else if(mgi == 24) {
												bonus_promosi = 0.8;
											}else if(mgi == 36) {
												bonus_promosi = 1.2;
											}
										}
										//perhitungan bonus penjualan, dihitung dari TOTAL PREMI bukan dari PREMI POKOK saja
										if(mgi == 1) {
											komisi.setKomisi(0.017 + bonus_promosi);
										}else if(mgi == 3) {
											komisi.setKomisi(0.05 + bonus_promosi);
										}else if(mgi == 6) {
											komisi.setKomisi(0.1 + bonus_promosi);
										}else if(mgi == 12) {
											komisi.setKomisi(0.2 + bonus_promosi);
										}else if(mgi == 24) {
											komisi.setKomisi(0.4 + bonus_promosi);
										}else if(mgi == 36) {
											komisi.setKomisi(0.6 + bonus_promosi);
										}else {
											throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
										}
									}
									
									break;
								}
							}
							
						//stable link syariah
						}else if(li_bisnis.equals("174")) {
							for(Map m : stable) {
								int msl_no = ((BigDecimal) m.get("MSL_NO")).intValue();
								if(premi_ke == msl_no) {
									int mgi = ((BigDecimal) m.get("MSL_MGI")).intValue();
									double bonus_promosi = (double) 0;
									Date bdate = (Date) m.get("MSL_BDATE");
									//perhitungan bonus penjualan, dihitung dari TOTAL PREMI bukan dari PREMI POKOK saja
									if(mgi == 1) {
										komisi.setKomisi(0.05);
									}else if(mgi == 3) {
										komisi.setKomisi(0.15);
									}else if(mgi == 6) {
										komisi.setKomisi(0.3);
									}else if(mgi == 12) {
										komisi.setKomisi(0.6);
									}else if(mgi == 24) {
										komisi.setKomisi(1.2);
									}else if(mgi == 36) {
										komisi.setKomisi(1.35);
									}else {
										throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
									}
									
									break;
								}
							}
						}
					}else {
						komisi.setKomisi(new Double(ldec_pct_kom[komisi.getLev_kom()]));
						if( i > 0) komisi.setKomisi(new Double(li_persen[i]));
					}
					ldec_komisi = (ldec_premi * komisi.getKomisi() / 100);

					//TAMBAHAN OLEH YUSUF UNTUK PRODUK2 SINGLE (IM No. 132/IM-DIR/XII/2007) - 16/01/2008
					ldec_komisi = cekLinkSingle(spaj, ldec_komisi, komisi.getLev_kom(), true);
					
					Date sysdate = commonDao.selectSysdateTruncated(0);
					if(komisi.getAgent_id().equals("500001"))komisi.setTax(new Double(ldec_komisi * 0.15));
					else{
						komisi.setTax(new Double(ldec_komisi * komisi.getTax() / 100));
						if(komisi.getTax()>0) komisi.setTax(f_load_tax(new Double(ldec_komisi), sysdate, komisi.getAgent_id()));
					}
					
					persen = komisi.getKomisi();
					komisi.setTax(FormatNumber.rounding(komisi.getTax(), true, 25));

					ldec_komisi = FormatNumber.round(new Double(ldec_komisi), 0);
					if(komisi.getTax()==null)komisi.setTax(new Double(0));
					komisi.setCo_id(sequenceMst_commission(11));
					komisi.setReg_spaj(spaj);
					komisi.setKomisi(new Double(ldec_komisi));
					komisi.setNilai_kurs(ldec_kurs);
					komisi.setLus_id(currentUser.getLus_id());
					komisi.setMsbi_tahun_ke(new Integer(1));
					komisi.setMsbi_premi_ke(premi_ke);
					
					//Yusuf - 5/1/09 - Mulai 2009, struktur komisi arthamas ada 6 tingkat, bukan 4 lagi, tapi diinsertnya bukan dari 6, tetep dari 4
					if(Integer.parseInt(prodDate) > 2008 && lca_id.equals("46")) {
						if(komisi.getLev_kom().intValue() > 2) komisi.setLev_kom(komisi.getLev_kom().intValue() - 2);
						else komisi.setLev_kom(0);
					}
					
					if(komisi.getKomisi()>0) {
						this.uwDao.insertMst_commission(komisi, sysdate);
						emailMessage.append("[KOMISI] " + komisi.getLev_kom() + " : " + komisi.getAgent_id() + " : " + 
								nf.format(ldec_premi) + " * " + nf.format(persen) + "% = " + nf.format(komisi.getKomisi()) + "\n");
						
						//Yusuf - 10/07/09 - Bonus Komisi untuk TopUp StableLink dipisah ke comm_bonus
						if(!pribadi.get("PRIBADI").toString().equals("1") && products.stableLink(li_bisnis)){
							if(i==0) {
								String ls_region = (String) pribadi.get("REGION");
								prosesBonusKomisi(
										"agency", ls_region, komisi.getReg_spaj(), komisi.getAgent_id(), komisi.getCo_id(), 
										komisi.getKurs_id(), ldec_premi, ldec_kurs, currentUser.getLus_id(), komisi.getKomisi(), komisi.getTax(),
										komisi.getMsbi_tahun_ke(), komisi.getMsbi_premi_ke(), komisi.getLev_kom(), komisi.getLsbs_linebus());
							}
						}						
					}
					
					//(Yusuf - 27/12/2006) untuk agency system, sekarang ada reward 10% -> SK No. 110/EL-SK/XII/2006
					if(i==0 && Integer.parseInt(prodDate) > 2006) {
						String rekruter_id = uwDao.selectRekruterAgencySystem(komisi.getAgent_id()); //tarik rekruternya
						boolean isReward = false;
						if("37,52".indexOf(lca_id)>-1){ //Agency)  //37 agency, 52 Agency Arthamas (Deddy 22 Feb 2012)
							isReward = true;
						}else if(lca_id.equals("46")) { //arthamas
							isReward = false;
						}else if(lca_id.equals("42")) { //worksite
							isReward = false;
						}

						//Yusuf - 12 Sep 08 - Stable Link agency dan regional ada reward khusus sept - dec, selain itu tidak ada
						if(products.stableLink(komisi.getBisnis_id().toString())) {
							isReward = false;
							if("08,09,42,40,46".indexOf(lca_id) == -1 && bulanProd >= 200809 && bulanProd <= 200812) {
								isReward = true;
							}
						}
						
						if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())) {
							isReward = false;
						}

						if(rekruter_id!=null && isReward) {//untuk newbusiness, hanya kalo ada recruiter, baru reward
							Rekruter rekruter = uwDao.selectRekruterFromAgen(rekruter_id); //tarik rekruter dari mst_agent, ada gak?, jenis=3
							if(rekruter==null) rekruter = uwDao.selectRekruterFromAgenSys(rekruter_id); //kalo gak ada, tarik dari mst_agensys, jenis=4
							if(rekruter!=null && !products.powerSave(komisi.getBisnis_id().toString()) && isReward) { //kalo ada, baru insert rewardnya 10%
								if(rekruter.getMsag_bay() == null) rekruter.setMsag_bay(0);
								if(rekruter.getSts_aktif().intValue() == 1  && rekruter.getMsag_bay().intValue() != 1) { //hanya bila rekruternya aktif baru dikasih reward
									//cek dulu, ada nomer rekeningnya gak
									if(rekruter.getNo_tab()==null) {
										errors.reject("payment.noRecruiter", new Object[] {rekruter.getAgent_id()}, "-");
										return false;
									} else if(rekruter.getNo_tab().trim().equals("")) {
										errors.reject("payment.noRecruiter", new Object[] {rekruter.getAgent_id()}, "-");
										return false;
									} else {
										rekruter.setNo_tab(StringUtils.replace(rekruter.getNo_tab(), "-",""));
										
										if(rekruter.getMsag_bay() == null) rekruter.setMsag_bay(0);
										
										double pengali = 1;
										
										//Yusuf (22/12/2010) bila business partner, maka dapat reward hanya 5%
										if(rekruter.getLcalwk().startsWith("61")) pengali = 0.5;
										//Yusuf (23/03/09) - Per 1 April OR dan Reward tidak 35% lagi tapi full
										else if(isLinkSingle(spaj, false) && bulanProd < 200904) pengali = 0.35;
										else if(isEkalink88Plus(spaj, true)) pengali = 0.7;
										else if(products.stableLink(komisi.getBisnis_id().toString())) pengali = 0.1;
										
										double reward = FormatNumber.round((ldec_komisi * 0.1 * pengali),  0);
										if(rekruter.getMsag_komisi()!=null){
											if(rekruter.getMsag_komisi().toString().equals("1") && rekruter.getMsag_bay().intValue() != 1){
												this.uwDao.insertMst_reward(spaj, 1, premi_ke, rekruter.getJenis_rekrut(), rekruter_id,
														rekruter.getNama(), rekruter.getNo_tab(), 
														rekruter.getLbn_id().toString(), reward, FormatNumber.rounding(f_load_tax(reward, sysdate, rekruter_id), true, 25), new Double(ldec_kurs), currentUser.getLus_id(), komisi.getLsbs_linebus());
												emailMessage.append("[RECRUITER REWARD] " + rekruter_id + " : " + nf.format(reward)+ "\n");
											}
										}
									}
								}
						}
							
						}
					}
					
				}
				
				if( i == 0 )ldec_premi = ldec_komisi;
	//				}
	//			}
			}
			
			/** PROSES TAMBAHAN UNTUK INSERT AAKM DAN BPJ (Yusuf - 6/12/2007) KHUSUS AGENSYS 37 */
			/*
			if(lca_id.equals("37")) {
				//Reserved (AAKM) & (BPJ)
				Map kopiTubruk = uwDao.selectFlagAAKMdanBPJ(spaj, 1, 1); 
				int li_lsbs = ((BigDecimal) kopiTubruk.get("LSBS_ID")).intValue();
				int li_lsdbs = ((BigDecimal) kopiTubruk.get("LSDBS_NUMBER")).intValue();
				int li_flag = 0;
				int li_ps = ((BigDecimal) kopiTubruk.get("LSBS_JENIS")).intValue();
				double ldec_comm_4 = ((BigDecimal) kopiTubruk.get("MSCO_COMM")).doubleValue();
				double ldec_percent, ldec_bonus, ldec_tax;
				
				Map kopiRobusta = uwDao.selectAAKM(spaj);
				String ls_lead_id = (String) kopiRobusta.get("MSAG_ID");
				li_flag = ((BigDecimal) kopiRobusta.get("MSAG_AAKM")).intValue();
				String ls_down_id = null;
				
				if(li_flag == 1) {
					//check for upper AD for AAKM
					ls_down_id = uwDao.selectLeadAAKM(ls_lead_id);

					//if upper AAKM AD found, set AAKM receiver to the upper one
					if(ls_down_id.equals("")) ls_down_id = ls_lead_id;
					
					//wf_get_agen
					Map kopiInstan = uwDao.selectInfoAgenAAKM(ls_down_id);
					String ls_nama = (String) kopiInstan.get("MCL_FIRST"); 
					int li_bank = ((BigDecimal) kopiInstan.get("LBN_ID")).intValue();
					String ls_acct = (String) kopiInstan.get("MSAG_TABUNGAN");
					ls_acct = ls_acct.replace("-", "").replace(".", "");
		
					//Ekalink Family Syariah and Ekalink 88 gets 30% AAKM
					if(li_lsbs == 159 || ( li_lsbs == 162 && (li_lsdbs == 5 || li_lsdbs == 6 || li_lsdbs == 7 || li_lsdbs == 8))) {
						ldec_percent = 0.3;
					}else if(li_ps == 1) { //powersave
						li_flag = uwDao.selectMasaGaransiInvestasi(spaj);
						if(li_flag == 36) {
							ldec_percent = 0.0006;
						} else if(li_flag == 12) {
							ldec_percent = 0.0002;
						} else {
							if(li_lsbs == 158) { // Powersave Bulanan doesn't get AAKM bonus if less than 12 months
								ldec_percent = 0;
							}else {
								if(li_flag == 6) {
									ldec_percent = 0.0001;
								}else if(li_flag == 3) {
									ldec_percent = 0.0005;
								}else {
									ldec_percent = 0;
								}
							}
						}
					}else {
						ldec_percent = 0.25;
					}
					
					ldec_bonus = FormatNumber.round(ldec_comm_4 * ldec_percent, 0);
					ldec_tax = FormatNumber.round(hitungPajakKomisi(ldec_bonus), 0);
					
					if((ldec_bonus - ldec_tax) > 0) {
						uwDao.insertMstAgencyBonus(spaj, 1, premi_ke, 1, 
								1, ls_down_id, ls_nama, ls_acct, 
								li_bank, ldec_bonus, ldec_tax, null, 0, 1, null, null, Integer.valueOf(currentUser.getLus_id()), (new Double(ldec_kurs)).intValue(), 0);
						emailMessage.append("[AAKM] " + ls_nama + " : " + ls_down_id + "\n");
					}
				}
				
				//BPJ exclude Power Save
				if(li_ps == 0) {
					
					boolean lb_flag = false;
					
					//Check for BPJ for AD get AD
					ls_down_id = uwDao.selectRekruterAgenSys(ls_lead_id);
					if(ls_down_id == null) ls_down_id = "";
					
					if(ls_down_id.equals("")) { //AD get AD info not found, go for promotion bonus
						for(int j=3; j>=1; j--) {
							Map kopiDong = uwDao.selectHistoryAgen(spaj, j);
							if(kopiDong != null) {
								ls_down_id = (String) kopiDong.get("MSAG_ID");
								li_flag = ((BigDecimal) kopiDong.get("LSAS_ID")).intValue();
								//found promotion history and has not been longer than 1 year, give the BPJ bonus
								if(li_flag == 2) {
									lb_flag = true;
									break;
								}
							}
						}
					}else {
						//recruit BPJ bonus only for 1st year production
						lb_flag = true; 
					}
					
					//Process BPJ Bonus
					if(lb_flag) {
	
						//wf_get_agen
						Map kopiInstan = uwDao.selectInfoAgenAAKM(ls_down_id);
						String ls_nama = (String) kopiInstan.get("MCL_FIRST"); 
						int li_bank = ((BigDecimal) kopiInstan.get("LBN_ID")).intValue();
						String ls_acct = (String) kopiInstan.get("MSAG_TABUNGAN");
						ls_acct = ls_acct.replace("-", "").replace(".", "");
						
						ldec_bonus = FormatNumber.round(ldec_comm_4 * 0.1, 0);
						ldec_tax = FormatNumber.round(hitungPajakKomisi(ldec_bonus), 0);
						
						if((ldec_bonus - ldec_tax) > 0) {
							uwDao.insertMstAgencyBonus(spaj, 1, premi_ke, 1, 
									2, ls_down_id, ls_nama, ls_acct, 
									li_bank, ldec_bonus, ldec_tax, null, 0, 1, null, null, Integer.valueOf(currentUser.getLus_id()), (new Double(ldec_kurs)).intValue(), 0);
							emailMessage.append("[BPJ] " + ls_nama + " : " + ls_down_id + "\n");
						}
	
					}
					
				}
			}*/

//			email.send(
//				new String[] {props.getProperty("admin.yusuf")},null, 
//				"Proses Komisi Topup Agency System [" + spaj + "]", emailMessage.toString(), currentUser);

		}
		
		this.uwDao.updateMst_billingTopup(spaj);
		
		//Setelah selesai proses komisi, hitung kom eva
		if(lca_id.equals("46")) {
			this.produksi.prosesPerhitunganCommEva(spaj, premi_ke);
		}
		
		return true;
	}

	private void getLamaTanggung(Commission komisi) {
		
		String lsbs_id = FormatString.rpad("0", komisi.getBisnis_id().toString(), 3);
		String a = "";
		if("097".indexOf(lsbs_id)>-1) {
			komisi.setIns_period(10);
		}else if("099".indexOf(lsbs_id)>-1) {
			komisi.setCb_id(3);
		}else if("021, 022, 027, 029, 032, 035, 039, 040, 060, 089, 120, 121, 127, 128, 129, 141, 145, 146, 147, 179, 185, 186".indexOf(lsbs_id)>-1) {
			komisi.setIns_period(99);
		}else if("051, 052, 062".indexOf(lsbs_id)>-1 && komisi.getKurs_id().equals("02")) {
			komisi.setIns_period(65);
		}else if("051, 052, 078, 062".indexOf(lsbs_id)>-1) {
			komisi.setIns_period(59);
		}else if("056, 068, 075".indexOf(lsbs_id)>-1) {
			komisi.setIns_period(8);
		}else if("046".indexOf(lsbs_id)>-1) {
			komisi.setIns_period(75);
		}else if("057,065".indexOf(lsbs_id)>-1) {
			komisi.setIns_period(79);
		}else if("063,173".indexOf(lsbs_id)>-1) { //eka sarjana mandiri
			komisi.setIns_period(25);
		}else if("066".indexOf(lsbs_id)>-1) {
			komisi.setIns_period(5);
		}else if("054, 132, 148".indexOf(lsbs_id)>-1 && komisi.getCb_id()==0) {
			komisi.setIns_period(1);
			komisi.setCb_id(3);
		}else if("085,181".indexOf(lsbs_id)>-1) {
			komisi.setIns_period(60);
		}else if("090".indexOf(lsbs_id)>-1) {
			komisi.setIns_period(30);
		}else if("115, 116, 117, 118, 140, 152, 153, 159, 160, 199".indexOf(lsbs_id)>-1) {
			if("159".indexOf(lsbs_id)>-1 && (komisi.getCb_id()==1 || komisi.getCb_id()==2 || komisi.getCb_id()==6)){
				komisi.setCb_id(3);
			}
			komisi.setIns_period(80);
		}else if("119, 122".indexOf(lsbs_id)>-1) {
			komisi.setIns_period(18);
		}else if("079, 091, 149".indexOf(lsbs_id)>-1) { //horison 55
			komisi.setIns_period(55);
		}else if("162".indexOf(lsbs_id)>-1) { //arthalink
			komisi.setIns_period(88);
		}else if("177".indexOf(lsbs_id)>-1) {
			komisi.setIns_period(4);
		}else if("190,200".indexOf(lsbs_id)>-1) {
			komisi.setIns_period(99);
		}else if("191".indexOf(lsbs_id)>-1) {
			komisi.setIns_period(80);
		}else if("217,218".indexOf(lsbs_id)>-1) {
			komisi.setIns_period(99);
		}
		
		if(komisi.getCb_id()==1 || komisi.getCb_id()==2 || komisi.getCb_id()==6){
			komisi.setCb_id(3);
		}
		
	}

	public void prosesKomisiIndividu2007(String spaj, User currentUser, BindException errors) throws Exception{ //of_komisi_new_lg untuk 2007
		StringBuffer emailMessage = new StringBuffer();
		emailMessage.append("[SPAJ  ] " + spaj + "\n");
		NumberFormat nf = NumberFormat.getInstance();
		List detail = uwDao.selectDetailBisnis(spaj);
		if(!detail.isEmpty()) {
			Map det = (HashMap) detail.get(0);
			emailMessage.append(
					"[PRODUK] (" + det.get("BISNIS") + ") " + det.get("LSBS_NAME") + " - " + 
					"(" + det.get("DETBISNIS") + ") " + det.get("LSDBS_NAME") + "\n");
		}
		
		//Yusuf - PERSENTASE OVERRIDING BARU - 21/12/2006 - SK No. 110/EL-SK/XII/2006
		BigDecimal persentase[] = new BigDecimal[] {
			new BigDecimal("1"), 
			new BigDecimal("2"), 
			new BigDecimal("8"), 
			new BigDecimal("14"), 
			new BigDecimal("100")};
		
		double ldec_kurs = 1; 
		int li_dplk=0;
		int li_club=0;
		tingkatIndividu2007 current=null;
		tingkatIndividu2007 agen = null;
		boolean lb_deduct=false;
		double kom_tmp = 0;
		double tax_tmp = 0;
		
		//Informasi2 tambahan
		Map pribadi = this.uwDao.selectInfoPribadi(spaj);
		String ls_region = (String) pribadi.get("REGION");
		int li_pribadi = (Integer) pribadi.get("PRIBADI");
		
		if(ls_region.startsWith("67")) {
			persentase = new BigDecimal[] {
					new BigDecimal("0"), 
					new BigDecimal("20"), 
					new BigDecimal("30"), 
					new BigDecimal("40"), 
					new BigDecimal("100")};
		}
		
		emailMessage.append("[PRODUCTION DATE] " + pribadi.get("MSPRO_PROD_DATE") + "\n");
		int bulanProd = Integer.parseInt(uwDao.selectBulanProduksi(spaj));

		//KALAU POLIS TUTUPAN PRIBADI, TIDAK MENDAPAT KOMISI
		if(li_pribadi == 1) return; 
		
		int flag_telemarket = uwDao.selectgetFlagTelemarketing(spaj);
		
		List lds_kom = this.uwDao.selectViewKomisiAsli(spaj);
		if(!lds_kom.isEmpty()) {

			//CEK DULU PAKE ROW PERTAMA, KALO KURS DOLLAR, ADA KURSNYA GAK
			Commission komisiME = (Commission) lds_kom.get(0);
			if(komisiME.getKurs_id().equals("02")){
				ldec_kurs = this.uwDao.selectLastCurrency(spaj, 1, 1);
				if(ldec_kurs<0){
					errors.reject("payment.noLastCurrency");
					return;
				}
			}
			
			/** LOOPING UTAMA UNTUK MENGHITUNG KOMISI DARI V_GET_KOM (lds_kom) */			
			for(int i=0; i<lds_kom.size(); i++) {
				li_dplk=0;
				li_club=0;
				Commission komisi = (Commission) lds_kom.get(i);
				komisi.setLsbs_linebus(bacDao.selectLineBusLstBisnis(komisi.getBisnis_id().toString()));
				komisi.setFlag_mess(Boolean.TRUE);
				//kurs * premi
				double premi_asli = komisi.getPremi();
				if("183,189,193,195,201,204".indexOf(komisi.getBisnis_id().toString()) >-1){
					komisi.setPremi(bacDao.selectTotalKomisiEkaSehat(spaj));
				}
				komisi.setPremi(FormatNumber.round(komisi.getPremi() * ldec_kurs, 2));
				//pukul rata cara bayar (lscb_id)
				int li_cb_id = komisi.getCb_id();
				if(!products.progressiveLink(komisi.getBisnis_id().toString()) && !products.stableSavePremiBulanan(komisi.getBisnis_id().toString()) && !products.healthProductStandAlone(komisi.getBisnis_id().toString())){
					if( li_cb_id==1 || li_cb_id==2 || li_cb_id==6 ) komisi.setCb_id(3);
				}
				//Commission individu
				komisi.setLsco_jenis(new Integer(1));
				//ambil lama tanggung
				getLamaTanggung(komisi);
				//tarik persentase komisinya, OR-nya di hardcode
				if(f_get_komisi(komisi, errors, 1, 1, komisi.getKurs_id(), komisi.getPremi(), 4)) {
					
					/** SPECIAL CASES */
					Map prestige = this.uwDao.selectPrestigeClub(komisi.getAgent_id(), komisi.getTgl_kom());
					if(prestige!=null){
						li_club = Integer.parseInt(prestige.get("MSAC_AKTIF").toString());
						li_dplk = Integer.parseInt(prestige.get("MSAC_DPLK").toString());
					}
					/** (DI LEVEL 4) */
					if(komisi.getLev_kom()==4) {
						if(products.powerSave(komisi.getBisnis_id().toString())){
							int mgi = this.uwDao.selectMgiNewBusiness(spaj);
							TopUp t = (TopUp) this.uwDao.selectTopUp(spaj, 1, 1, "desc").get(0);
							Date ldt_tgl_rk = t.getMspa_date_book();
							if(ldt_tgl_rk.compareTo(defaultDateFormatReversed.parse("20111116"))< 0 ){//request TRI, berdasarkan tgl RK apabila tgl RK di bawah tgl 16 nov 2011, ambil komisi lama
								komisi.setTgl_kom(ldt_tgl_rk);
							}
							Map komisiPowersave = uwDao.selectKomisiPowersave(1, 
									komisi.getBisnis_id(), komisi.getBisnis_no(), 
									mgi, komisi.getKurs_id(), komisi.getLev_kom(), 1, komisi.getPremi(), 0, komisi.getTgl_kom());

							if(komisiPowersave == null) {
								errors.reject("payment.noCommission", 
										new Object[]{
										   defaultDateFormat.format(komisi.getTgl_kom()),
										   komisi.getBisnis_id()+"-"+komisi.getBisnis_no(),
										   komisi.getCb_id(), 
										   komisi.getIns_period(), 
										   komisi.getKurs_id(), 
										   komisi.getLev_kom(),
										   komisi.getTh_kom(),
										   komisi.getLsco_jenis()
								           }, 
										"Commission POWERSAVE Tidak Ada.<br>- Tanggal: {0}<br>-Kode Bisnis: {1}<br>-Cara Bayar: {2}<br>-Ins_per: {3}<br>-Kurs: {4}<br>-Level: {5}<br>-Tahun Ke{6}<br>-Jenis: {7}");
								throw new Exception(errors);
							}else {
								if(komisiPowersave.get("LSCP_COMM")!=null) komisi.setKomisi(Double.valueOf(komisiPowersave.get("LSCP_COMM").toString()));
								if(komisiPowersave.get("LSCP_BONUS")!=null) komisi.setBonus(Double.valueOf(komisiPowersave.get("LSCP_BONUS").toString()));
							}
						}else{
//							Deddy - Semua stable save komisi diset 0.58
							if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())){
								komisi.setKomisi(0.58);
							}
							
							//POWERSAVE INDIVIDU
							else if(komisi.getBisnis_id()==143) {
								int li_jwaktu = this.uwDao.selectMgiNewBusiness(spaj);
								if(li_jwaktu==3) komisi.setKomisi(new Double(0.05));
								else if(li_jwaktu==6) komisi.setKomisi(new Double(0.1));
								else if(li_jwaktu==12) komisi.setKomisi(new Double(0.2));
								else if(li_jwaktu==36) komisi.setKomisi(new Double(0.6));
								else komisi.setKomisi(new Double(0));
								
//								(Deddy 28/7/2009) SK Direksi No.079/AJS-SK/VII/2009
								if(komisi.getBisnis_no().intValue()==1 || komisi.getBisnis_no().intValue()==2 || komisi.getBisnis_no().intValue()==3){
									if(li_jwaktu==3) komisi.setKomisi(new Double(0.063));
									else if(li_jwaktu==6) komisi.setKomisi(new Double(0.125));
									else if(li_jwaktu==12) komisi.setKomisi(new Double(0.25));
									else if(li_jwaktu==36) komisi.setKomisi(new Double(0.75));
									else komisi.setKomisi(new Double(0));
									// 027/IM-DIR/IV/2011 Tertanggal 11 April 2011 perubahan komisi untuk powersave dan stable link.
									if(bulanProd>=201105){
										if(li_jwaktu==1) komisi.setKomisi(0.05);
										else if(li_jwaktu==3) komisi.setKomisi(0.15);
										else if(li_jwaktu==6) komisi.setKomisi(0.3);
										else if(li_jwaktu==12) komisi.setKomisi(0.6);
										else if(li_jwaktu==36) komisi.setKomisi(1.35);
										else komisi.setKomisi(new Double(0));
									}
								}
								
								if(komisi.getBisnis_no().intValue()==4){
									komisi.setKomisi(0.58);
								}else if(komisi.getBisnis_no().intValue()==5){
									//komisi.setKomisi(2.8); // KHUSUS BII (SK. Direksi No. 060/AJS-SK/V/2009)
									komisi.setKomisi(2.5);//(Deddy - 28/7/2009)berlaku tgl : 1 agustus 2009 berdasarkan SK. Direksi No. 088/AJS-SK/VII/2009
								}
								
								
							//POWERSAVE BULANAN YANG WORKSITE & BUKAN BANK Sinarmas (YUSUF - 25/08/2006)
							}else if(komisi.getBisnis_id()==158 && komisi.getBisnis_no() < 5 && ls_region.startsWith("42")) {
								int li_jwaktu = this.uwDao.selectMgiNewBusiness(spaj);
								if(li_jwaktu==3) komisi.setKomisi(0.12);
								else if(li_jwaktu==6) komisi.setKomisi(0.24);
								else if(li_jwaktu==12) komisi.setKomisi(0.48);
								else if(li_jwaktu==36) komisi.setKomisi(1.18);
								else komisi.setKomisi(0.0);

							//POWERSAVE BULANAN YANG BUKAN FINANCIAL PLANNER & BUKAN BANK Sinarmas (YUSUF - 25/08/2006)
							}else if(komisi.getBisnis_id()==158 && komisi.getBisnis_no() < 5 && !ls_region.startsWith("0916")) {
								int li_jwaktu = uwDao.selectMgiNewBusiness(spaj);
								if(li_jwaktu==3) komisi.setKomisi(0.12);
								else if(li_jwaktu==6) komisi.setKomisi(0.24);
								else if(li_jwaktu==12) komisi.setKomisi(0.48);
								else if(li_jwaktu==36) komisi.setKomisi(1.18);
								else komisi.setKomisi(0.0);
							//HORISON, KOMISI DIHITUNG DARI (100 - MST_INSURED.MSTE_PCT_DPLK) * MST_PRODUCT_INSURED.MSPR_PREMIUM
							//KARENA MSPR_PREMIUM = TOTAL DARI BIAYA DPLK + PREMI, DAN PERSENTASENYA ADA DI MST_INSURED.MSTE_PCT_DPLK
							//(YUSUF - 22/09/2006)
							}else if(komisi.getBisnis_id()==158 && (komisi.getBisnis_no().intValue()==13 || komisi.getBisnis_no().intValue()==16 )) {
//								int li_jwaktu = uwDao.selectMgiNewBusiness(spaj);
//								double ldec_temp = komisi.getPremi();
//								if(li_jwaktu==3) komisi.setKomisi(0.05);
//								else if(li_jwaktu==6) komisi.setKomisi(0.1);
//								else if(li_jwaktu==12) komisi.setKomisi(0.2);
//								else komisi.setKomisi(0.0);
								
//								SK. Direksi No. 086/AJS-SK/VII/2009
								komisi.setKomisi(0.58);
							}else if(komisi.getBisnis_id()==149) {
								Map dplk = uwDao.selectPersentaseDplk(spaj);
								komisi.setPremi((Double) dplk.get("premi"));
							
							//PA Stand Alone, 10 %
							}else if(komisi.getBisnis_id()==73) {
								//Yusuf (19 Feb 2007) PA Stand Alone
								komisi.setKomisi(new Double(10));
								
							//POWERSAVE SYARIAH (YUSUF - 28/01/09)
							}else if(komisi.getBisnis_id() == 175) {
								int li_jwaktu = this.uwDao.selectMgiNewBusiness(spaj);

								if(li_jwaktu==3) komisi.setKomisi(0.05);
								else if(li_jwaktu==6) komisi.setKomisi(0.1);
								else if(li_jwaktu==12) komisi.setKomisi(0.2);
								else if(li_jwaktu==36) komisi.setKomisi(0.6);
								else komisi.setKomisi(0.0);
								
							//POWERSAVE BULANAN SYARIAH (YUSUF - 28/01/09)
							}else if(komisi.getBisnis_id() == 176) {
								int li_jwaktu = this.uwDao.selectMgiNewBusiness(spaj);

								if(li_jwaktu==3) komisi.setKomisi(0.12);
								else if(li_jwaktu==6) komisi.setKomisi(0.24);
								else if(li_jwaktu==12) komisi.setKomisi(0.48);
								else if(li_jwaktu==36) komisi.setKomisi(1.18);
								else komisi.setKomisi(new Double(0));
								
							//STABLE SAVE (YUSUF - 30/01/09)
							}else if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())) {
//								(Deddy) Perubahan perhitungan komisi sesuai  SK.Direksi No. 068/AJS-SK/VI/2009
//								perhitungan yang lama berdasarkan MGI.
//								int mgi = uwDao.selectMasaGaransiInvestasi(spaj);
//								
//								if(mgi == 1) 		komisi.setKomisi(0.0167);
//								else if(mgi == 3) 	komisi.setKomisi(0.05);
//								else if(mgi == 6) 	komisi.setKomisi(0.1);
//								else if(mgi == 12) 	komisi.setKomisi(0.2);
								if(komisi.getBisnis_no().intValue()==1){ //individu
									komisi.setKomisi(0.58);
								}else if(komisi.getBisnis_no().intValue()==2){ //bii
									//komisi.setKomisi(2.8); // KHUSUS BII (SK. Direksi No. 060/AJS-SK/V/2009)
									komisi.setKomisi(2.5);//(Deddy - 28/7/2009)berlaku tgl : 1 agustus 2009 berdasarkan SK. Direksi No. 088/AJS-SK/VII/2009
								}

							//STABLE SAVE PREMI BULANAN (YUSUF - 31/04/09)
							}else if(products.stableSavePremiBulanan(komisi.getBisnis_id().toString())) {
								//komisi.setKomisi(0.0167); //karena mgi nya cuman 1 bulan aja
//								komisi.setKomisi(0.5); //(Deddy) -  SK.Direksi No. 087/AJS-SK/VII/2009
								int li_jwaktu = this.uwDao.selectMasaGaransiInvestasi(spaj,1,1); // SK.Direksi No. 140/AJS-SK/XII/2009

								if(li_jwaktu==3) komisi.setKomisi(0.125);
								else if(li_jwaktu==6) komisi.setKomisi(0.25);
								else if(li_jwaktu==12) komisi.setKomisi(0.5);
								else if(li_jwaktu==1) komisi.setKomisi(0.042);
							}else if(products.progressiveLink(komisi.getBisnis_id().toString()) && ( bulanProd >= 201102 && bulanProd <=201104 ) ){
								
								int li_jwaktu = this.uwDao.selectMasaGaransiInvestasi(spaj,1,1);
								
								if(li_jwaktu==3) komisi.setKomisi(0.126);
								else if(li_jwaktu==6) komisi.setKomisi(0.375);
								else if(li_jwaktu==12) komisi.setKomisi(0.75);
								else if(li_jwaktu==1) komisi.setKomisi(1.5);
							}
						}
						
//						
					}
					
					/** LEV_COMM <> 4 */
					// untuk Financial Planner (cab = '43') -- 19/04/2006 (RG)
					if(ls_region.startsWith("43")) {
						if(komisi.getLev_kom()==1 || komisi.getLev_kom()==2)
							komisi.setKomisi(new Double(0));
						else if(komisi.getLev_kom()==3)
							komisi.setKomisi(new Double(20));
					}
					
					//Ganti per 22 Oct 2001, Memo Yimmy Lesmana - cara bayar 3, dapet bonus
					if(komisi.getCb_asli()==3) {
						komisi.setKomisi(komisi.getKomisi()+komisi.getBonus());
					}

					//Khusus eka sarjana mandiri RP 3 & 5
					// Kalo premi RP > 10jt atau prestige club
					if(komisi.getLev_kom()==4) {
						if(komisi.getBisnis_id()==63 && komisi.getKurs_id().equals("01") && (komisi.getBisnis_no()==2 || komisi.getBisnis_no()==3)){
							if(komisi.getPremi() >=10000000 || li_club==1){
								//3 tahun
								if(komisi.getBisnis_no()==2){
									komisi.setKomisi(7.5);
									if(komisi.getCb_asli()==3)komisi.setKomisi(12.5);
								}else{
									//5 tahun
									komisi.setKomisi((double) 25);
									if(komisi.getCb_asli()==3)komisi.setKomisi((double) 30);
								}
							}
						//Khusus eka sarjana mandiri yang baru (173)
						}else if(komisi.getBisnis_id()==173){
							if(i==0){
//								komisi.setKomisi(5.);
								
								int autodebet = bacDao.selectFlagCC(spaj);
								//bila autodebet payroll
								if(autodebet == 4){
									komisi.setKomisi(7.5);
								}else{									
									if(komisi.getBisnis_no()==1){
										komisi.setKomisi(5.);
									}else if(komisi.getBisnis_no()==2 || komisi.getBisnis_no()==3){
										komisi.setKomisi(10.);
									}
								}
							}
						}else if(komisi.getBisnis_id()==172){
							if(i==0){
								Integer li_age = this.uwDao.selectAgeFromSPAJ(komisi.getReg_spaj());
								//bila cara bayar bulanan
								if(komisi.getCb_asli().intValue() == 6) {
									if(komisi.getKurs_id().equals("01") ){
										if(li_age >=1 && li_age <= 8){
											komisi.setKomisi(new Double(22.5));
										}else if(li_age>=9 && li_age <= 12){
											komisi.setKomisi(new Double(17.5));
										}
									}else if(komisi.getKurs_id().equals("02")){
										if(li_age >=1 && li_age <= 5){
											komisi.setKomisi(new Double(22.5));
										}else if(li_age>=6 && li_age <= 8){
											komisi.setKomisi(new Double(17.5));
										}else if(li_age>=9 && li_age <= 12){
											komisi.setKomisi(new Double(12.5));
										}
									}
									
								//cara bayar lainnya
								}else{
									if(komisi.getKurs_id().equals("01") ){
										if(li_age >=1 && li_age <= 8){
											komisi.setKomisi(new Double(25));
										}else if(li_age>=9 && li_age <= 12){
											komisi.setKomisi(new Double(20));
										}
									}else if(komisi.getKurs_id().equals("02")){
										if(li_age >=1 && li_age <= 5){
											komisi.setKomisi(new Double(25));
										}else if(li_age>=6 && li_age <= 8){
											komisi.setKomisi(new Double(20));
										}else if(li_age>=9 && li_age <= 12){
											komisi.setKomisi(new Double(15));
										}
									}
								}
							}
							//prolife(179)
						}else if(komisi.getBisnis_id()==179){
							if(i==0){
								komisi.setKomisi(5.);
								int autodebet = bacDao.selectFlagCC(spaj);
								//bila autodebet payroll
								if(autodebet == 4){
									if(komisi.getBisnis_no().intValue()==1){
										komisi.setKomisi(17.5);
									}else if(komisi.getBisnis_no().intValue()==2){
										komisi.setKomisi(22.5);
									}else if(komisi.getBisnis_no().intValue()==3){
										komisi.setKomisi(22.5);
									}
								}else{									
									if(komisi.getBisnis_no().intValue()==1){
										komisi.setKomisi(20.0);
									}else if(komisi.getBisnis_no().intValue()==2){
										komisi.setKomisi(25.0);
									}else if(komisi.getBisnis_no().intValue()==3){
										komisi.setKomisi(25.0);
									}
									if(komisi.getCb_asli().intValue()==0){
										komisi.setKomisi(5.0);
									}
								}
							}
							//Khusus prosaver (180)
						}else if(komisi.getBisnis_id()==180){
							if(i==0){
//								komisi.setKomisi(5.);
								int autodebet = bacDao.selectFlagCC(spaj);
								//bila autodebet payroll
								if(autodebet == 4){
									if(komisi.getBisnis_no().intValue()==1){
										komisi.setKomisi(17.5);
									}else if(komisi.getBisnis_no().intValue()==2){
										komisi.setKomisi(22.5);
									}else if(komisi.getBisnis_no().intValue()==3){
										komisi.setKomisi(22.5);
									}
								}else{									
									if(komisi.getBisnis_no().intValue()==1){
										komisi.setKomisi(20.0);
									}else if(komisi.getBisnis_no().intValue()==2){
										komisi.setKomisi(25.0);
									}else if(komisi.getBisnis_no().intValue()==3){
										komisi.setKomisi(25.0);
									}
									if(komisi.getCb_asli().intValue()==0){
										komisi.setKomisi(5.0);
									}
								}
							}
						}
					}
					
					/** END SPECIAL CASES */
					
					/** START PERHITUNGAN OR (UNTUK LEVEL != 4) */
					//cari tahu dulu, nih agen apaan? 
					//4 ME
					//3 UM
					//2 & MSAG_FLAG_BM=0 -> AM / 2 & MSAG_FLAG_BM=1 -> BM 
					//1 & MSAG_SBM=1 -> SBM
					//1 & MSAG_SBM=0 -> RM
					if(komisi.getLsle_id()==4) agen = tingkatIndividu2007.ME;
					else if(komisi.getLsle_id()==3) agen = tingkatIndividu2007.UM;
					else if(komisi.getLsle_id()==2) agen = tingkatIndividu2007.BM;
					else if(komisi.getLsle_id()==1 && komisi.getFlag_sbm()==1) agen = tingkatIndividu2007.SBM;
					else if(komisi.getLsle_id()==1 && komisi.getFlag_sbm()==0) agen = tingkatIndividu2007.RM;
					
					if(komisi.getLev_kom()<4) {
						//karena OR baru, persentasenya dihardcode, gak ngambil dari database
						komisi.setKomisi((double) 0);
						String msag_id_leader = commonDao.selectMsagIdLeader(komisi.getAgent_id());
						if(komisi.getBisnis_id()==170 && komisi.getAgent_id().equals(msag_id_leader)){
							if(komisi.getLev_kom()==3){
//								kalau yang nutup ME, ORnya gulung dari atas (RM)
								BigDecimal persentase2[] = new BigDecimal[] {
										new BigDecimal("0"), 
										new BigDecimal("0"), 
										new BigDecimal("0"), 
										new BigDecimal("125"), 
										new BigDecimal("100")};
								if(current.ordinal()>agen.ordinal()) {
									for(int j = current.ordinal(); j>agen.ordinal(); j--) {
										komisi.setKomisi(persentase2[j-1].add(new BigDecimal(komisi.getKomisi())).doubleValue());
									}
								//kalo yang nutup bukan ME, ORnya gulung dari bawah (ME)
								}else if(current.equals(agen)) {
									for(int j = current.ordinal(); j<tingkatIndividu2007.ME.ordinal(); j++) {
										komisi.setKomisi(persentase2[j].add(new BigDecimal(komisi.getKomisi())).doubleValue());
									}
								}
							}
						}else{
//							kalau yang nutup ME, ORnya gulung dari atas (RM)
							if(current.ordinal()>agen.ordinal()) {
								for(int j = current.ordinal(); j>agen.ordinal(); j--) {
									komisi.setKomisi(persentase[j-1].add(new BigDecimal(komisi.getKomisi())).doubleValue());
								}
							//kalo yang nutup bukan ME, ORnya gulung dari bawah (ME)
							}else if(current.equals(agen)) {
								for(int j = current.ordinal(); j<tingkatIndividu2007.ME.ordinal(); j++) {
									komisi.setKomisi(persentase[j].add(new BigDecimal(komisi.getKomisi())).doubleValue());
								}
							}
						}
						
						if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())){
							komisi.setKomisi(0.0);
						}
					}
					current = agen;
					/** END PERHITUNGAN OR */
					
					//stable link, ada or sept - dec 2008, seliain itu tidak ada
					if(komisi.getLev_kom().intValue() < 4 && products.stableLink(komisi.getBisnis_id().toString())) {
						if(bulanProd >= 200809 && bulanProd <= 200812) {
							komisi.setKomisi(0.1 * komisi.getKomisi());
						}else {
							komisi.setKomisi(0.);
						}
					}
					
					if(komisi.getLev_kom().intValue() < 4 && komisi.getBisnis_id()==203){
						komisi.setKomisi(0.);
					}
					
					
					if(ls_region.substring(0,2).equals("42")){
						if(financeDao.selectIsAgenCorporate(komisi.getAgent_id())==1 && komisi.getLev_kom().intValue()<3){
							komisi.setKomisi(0.);
						}
						if(financeDao.selectIsAgenCorporate(komisi.getAgent_id())==1 && komisi.getLev_kom().intValue()==3){
							if(products.stableLink(komisi.getBisnis_id().toString()) || products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no()) || products.powerSave(komisi.getBisnis_id().toString()) ){
								komisi.setKomisi(0.);
							}else{
								komisi.setKomisi(komisi.getKomisi()*0.15);
							}
						}
						if(bulanProd >= 201002){ // Mulai bulan feb 2010 && merupakan worksite && msag_kry=1, maka OR tidak diberlakukan.
							if(komisi.getLev_kom().intValue() < 4 && financeDao.selectIsAgenKaryawan(komisi.getAgent_id())==1){
								komisi.setKomisi(0.);
							}
						}
					}
					
					/** START INSERTING COMMISSION */
					if(komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
						double persen = komisi.getKomisi();
						if(komisi.getLev_kom()==4) {
							emailMessage.append("[PREMI ] " + nf.format(komisi.getPremi()) + "\n");
							emailMessage.append("[KOMISI] " + komisi.getLev_kom() + " : " + komisi.getAgent_id() + " : " + 
									nf.format(komisi.getPremi()) + " * " + nf.format(komisi.getKomisi()) + "% = ");
							komisi.setKomisi(komisi.getPremi() * komisi.getKomisi() / 100); //komisi = premi * persen komisi
							emailMessage.append(nf.format(komisi.getKomisi()) + "\n");
						} else {
							emailMessage.append("[OR    ] " + komisi.getLev_kom() + " : " + komisi.getAgent_id() + " : " + 
									nf.format(komisiME.getKomisi()) + " * " + nf.format(komisi.getKomisi()) + "% = ");
							komisi.setKomisi(komisiME.getKomisi() * komisi.getKomisi() / 100); //OR = komisi ME * persen komisi
							emailMessage.append(nf.format(komisi.getKomisi()) + "\n");
						}
						
						//TAMBAHAN OLEH YUSUF UNTUK PRODUK2 SINGLE (IM No. 132/IM-DIR/XII/2007) - 16/01/2008
						komisi.setKomisi(cekLinkSingle(spaj, komisi.getKomisi(), komisi.getLev_kom(), false));
						
						if(flag_telemarket==1){
							komisi.setKomisi(komisi.getKomisi()/0.5);
						}
						
						komisi.setCo_id(sequenceMst_commission(11));
						komisi.setTax(new Double(komisi.getKomisi() * komisi.getTax() / 100));
						Date sysdate = commonDao.selectSysdateTruncated(0);
						if(komisi.getTax() > 0)komisi.setTax(f_load_tax(komisi.getKomisi(), sysdate, komisi.getAgent_id()));
						double ad_sisa;
						ad_sisa = komisi.getTax()%25;
						if(ad_sisa!=0){
							
						}
						komisi.setTax(FormatNumber.rounding(komisi.getTax() , true, 25));
						komisi.setKomisi(FormatNumber.round(komisi.getKomisi(), 0));
						if(komisi.getTax()==null)komisi.setTax(new Double(0));
						
						//SPECIAL CASE JUGA, KELUPAAN DITAMBAHIN DARI PROSES LAMA, untuk franchise
						if(ls_region.substring(0,2).equals("14")) {
							if(komisi.getLev_kom()==4) {
								kom_tmp = komisi.getKomisi();
								tax_tmp = komisi.getTax();
							} else if(komisi.getLev_kom()==3) {
								komisi.setKomisi(kom_tmp);
								komisi.setTax(tax_tmp);
							} else {
								komisi.setKomisi(new Double(0));					
								komisi.setTax(new Double(0));
							}
						}
						
						komisi.setNilai_kurs(new Double(ldec_kurs));
						komisi.setLus_id(currentUser.getLus_id());
						komisi.setMsbi_tahun_ke(1);
						komisi.setMsbi_premi_ke(1);
						komisi.setMsco_flag(li_club);
						if(komisi.getKomisi()>0) {
							if(products.healthProductStandAlone(komisi.getBisnis_id().toString()) ){
								//Khusus Smile medical, hanya agent penutup saja yg dapat komisi.
								if(("183,189,193,201".indexOf(komisi.getBisnis_id().toString()) >-1) ){//inidiedit
								//FIX ME: RYAN, Sekarang , SMiLe Medical, semua level dpt komisi , asal terdaftar di mst_agent_comm
								//	if(komisi.getLev_kom()==4){
										this.uwDao.insertMst_commission(komisi, sysdate);
									//}
								}else{
									this.uwDao.insertMst_commission(komisi, sysdate);
								}
							}else {
								if(komisi.getBisnis_id()==170 && komisi.getMsag_id().equals("022902")){
									
								}
								else this.uwDao.insertMst_commission(komisi, sysdate);
							}
						}
						
						//tambahan : Yusuf (29/04/08) ada proses komisi rider, mulai 1 may 2008
						prosesKomisiRider(komisi, i, persen);
						
						//Yusuf - 08/04/09 - Start 13 April, Bonus Komisi untuk Produk Save + StableLink dipisah ke comm_bonus
						if(!pribadi.get("PRIBADI").toString().equals("1")){
							if(i==0) {
								if(komisi.getBisnis_id()==170 && komisi.getMsag_id().equals("022902")){
									
								}else
								prosesBonusKomisi(
										"individu", ls_region, komisi.getReg_spaj(), komisi.getAgent_id(), komisi.getCo_id(), 
										komisi.getKurs_id(), komisi.getPremi(), ldec_kurs, currentUser.getLus_id(), komisi.getKomisi(), komisi.getTax(),
										komisi.getMsbi_tahun_ke(), komisi.getMsbi_premi_ke(), komisi.getLev_kom(), komisi.getLsbs_linebus());
							}
						}
						//						
					}
					/** END INSERTING COMMISSION */
					
					/** START INSERT KE TABEL2 TAMBAHAN */
					
					//Yusuf - 12 Sep 08 - Stable Link agency dan regional ada reward khusus sept - dec, selain itu tidak ada
					boolean isRewardStableLink = true; 
					if(products.stableLink(komisi.getBisnis_id().toString())) {
						String lca_id = uwDao.selectCabangFromSpaj(spaj);
						if("08,09,42,40,46".indexOf(lca_id) == -1 && bulanProd >= 200809 && bulanProd <= 200812) {
							isRewardStableLink = true;
						}else {
							isRewardStableLink = false;
						}
					}else if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())){
						isRewardStableLink = false;
					}else if(ls_region.substring(0,2).equals("42")) {
						isRewardStableLink = false;
					}
					
					//proses insert reward untuk recruiter (pukul rata 10%, hanya apabila ada recruiter dan statusnya aktif)
					//Req Himmia (3 Apr 2013) : Untuk setiap proses reward, dicek terlebih dahulu apakah rekruter tersebut di mst_agent masih terdaftar aktif dan mendapatkan komisi ga. Apabila 1, maka dapat reward.
					if(komisi.getLev_kom()==4 && isRewardStableLink && komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
						Map<String, Object> rekruter = this.uwDao.selectRekruter(komisi.getAgent_id());
						
						if(rekruter != null && !products.powerSave(komisi.getBisnis_id().toString()) && rekruter.get("MSRK_AKTIF").toString().equals("1")) {
							if(rekruter.get("NO_ACCOUNT")==null && rekruter.get("MSRK_AKTIF").toString().equals("1")) {
								errors.reject("payment.noRecruiter", new Object[] {rekruter.get("MSRK_ID")}, "-"); throw new Exception(errors);
							} else if(rekruter.get("NO_ACCOUNT").toString().equals("")) {
								errors.reject("payment.noRecruiter", new Object[] {rekruter.get("MSRK_ID")}, "-"); throw new Exception(errors);
							} else {
								int jenis_rekrut = Integer.valueOf(((String) rekruter.get("MSRK_JENIS_REKRUT")));
								//kalau jenis_rekrut = 2 (rekrut langsung), cek status aktifnya dari mst_agent
								//kalau jenis_rekrut lainnya, cek status aktifnya dari mst_rekruter
								if((((jenis_rekrut == 2 || jenis_rekrut==3) && rekruter.get("MSAG_ACTIVE").toString().equals("1")) || 
										(!(jenis_rekrut == 2 || jenis_rekrut==3) && rekruter.get("MSRK_AKTIF").toString().equals("1"))) && rekruter.get("MSAG_KOMISI").toString().equals("1")) {
									//req Pak Him : pengecekan terakhir dari rekruter apakah ada di mst_agent.
									if(uwDao.selectbacCekAgen(rekruter.get("MSRK_ID").toString())!=null ){
										
										Rekruter rek = uwDao.selectRekruterFromAgen(rekruter.get("MSRK_ID").toString());
										if(rek.getMsag_bay() == null) rek.setMsag_bay(0);
										
										double pengali = 1;
										
										//Yusuf (22/12/2010) bila business partner, maka dapat reward hanya 5%
										if(rek.getLcalwk().startsWith("61")) pengali = 0.5;
										//Yusuf (23/03/09) - Per 1 April OR dan Reward tidak 35% lagi tapi full
										else if(isLinkSingle(spaj, false) && bulanProd < 200904) pengali = 0.35;
										else if(isEkalink88Plus(spaj, false)) pengali = 0.7;
										else if(products.stableLink(komisi.getBisnis_id().toString())) pengali = 0.1;
										Double reward = FormatNumber.round((komisi.getKomisi() * 0.1 * pengali), 0);
										Date sysdate = commonDao.selectSysdateTruncated(0);
										Double tax = FormatNumber.rounding(f_load_tax(reward, sysdate, rekruter.get("MSRK_ID").toString()), true, 25);
										rekruter.put("NO_ACCOUNT", StringUtils.replace(rekruter.get("NO_ACCOUNT").toString(), "-",""));
										if("183,189,193,195,201,204".indexOf(komisi.getBisnis_id().toString())<=-1 && rek.getMsag_bay().intValue() != 1 ){
											if((komisi.getBisnis_id()==187 && "5,6".indexOf(komisi.getBisnis_no().toString())>-1) ){
												
											}else
											this.uwDao.insertMst_reward(spaj, 1, 1, rekruter.get("MSRK_JENIS_REKRUT").toString(), rekruter.get("MSRK_ID").toString(),
													rekruter.get("MSRK_NAME").toString(), rekruter.get("NO_ACCOUNT").toString(), 
													rekruter.get("LBN_ID").toString(), reward, tax, new Double(ldec_kurs), currentUser.getLus_id(), komisi.getLsbs_linebus());
											emailMessage.append("[RECRUITER REWARD] " + rekruter.get("MSRK_ID").toString() + " : " + nf.format(reward)+ "\n");
										}
									}
								}
							}
						}
					}
					
					//Premi Kurang Bayar
					if(komisi.getLev_kom()==4){
						Map temp = this.uwDao.selectBillingRemain(komisi.getReg_spaj());
						double ldec_sisa = Double.parseDouble(temp.get("MSBI_REMAIN").toString());
						Integer li_flag = (Integer) temp.get("MSBI_FLAG_SISA");
						if(li_flag!=null){
							if(li_flag==2) {
								this.uwDao.insertMst_deduct(komisi.getCo_id(), ldec_sisa, currentUser.getLus_id(), 1, 3, "PREMI KURANG BAYAR");
								emailMessage.append("[POTONGAN KOMISI] " + komisi.getCo_id() +" : " + nf.format(ldec_sisa)+ "\n");
								lb_deduct=true;
							}
						}
					}
					
					//Jika masuk prestige club, pot buat dplk
					if(li_club==1 && li_dplk==1){
						int li_deduct_no = 1;
						if(i==0 && lb_deduct)li_deduct_no=2;
						Double ldec_dplk = Double.valueOf(prestige.get("LPC_DPLK_K").toString());
						ldec_dplk = new Double((komisi.getKomisi() - komisi.getTax()) * ldec_dplk / 100);
						ldec_dplk = FormatNumber.round(ldec_dplk,0);
						this.uwDao.insertMst_deduct(komisi.getCo_id(), ldec_dplk, currentUser.getLus_id(), li_deduct_no, 5, 
								"POTONGAN DPLK /U " + prestige.get("LPC_CLUB").toString() );
						emailMessage.append("[POTONGAN PRESTIGE CLUB] " + komisi.getCo_id() +" : " + nf.format(ldec_dplk)+ "\n");
					}
					
					/*
					//Proses Bonus Penjualan (EKA.MST_COMM_BONUS) 
					//- khusus produk EKALINK REGULER (159-2) - (Yusuf - 15/09/2006)
					//- dan khusus produk IKHLAS & SAQINAH (MUAMALAT) - (Yusuf - 03/03/2009)
					if(komisi.getLev_kom()==4) {
						if((komisi.getBisnis_id() == 159 || komisi.getBisnis_id() == 160) && komisi.getBisnis_no() == 2) {
							Double bonus = FormatNumber.rounding(0.05 * komisi.getPremi(), false, 25); //bonus penjualan 5%
							Map infoBonus = uwDao.selectBonusAgen(komisi.getAgent_id());

							Double temp = komisi.getKomisi() + bonus;
							Date sysdate = commonDao.selectSysdateTruncated(0);
							Double bonus_tax = hitungPajakKomisi(temp, sysdate, komisi.getAgent_id());
							bonus_tax -= komisi.getTax();
							bonus_tax = FormatNumber.rounding(bonus_tax, false, 25);
							
							this.uwDao.insertMst_comm_bonus(
									spaj, 1, 1, 1, komisi.getAgent_id(), 
									(String) infoBonus.get("MCL_FIRST"), (String) infoBonus.get("MSAG_TABUNGAN"), 
									infoBonus.get("LBN_ID").toString(), komisi.getCo_id(), bonus, bonus_tax, ldec_kurs, currentUser.getLus_id());
							emailMessage.append("[BONUS EKALINK] " + komisi.getAgent_id() +" : " + nf.format(bonus)+ "\n");
						}else if(komisi.getBisnis_id() == 170 || (komisi.getBisnis_id() == 171 && komisi.getBisnis_no().intValue() == 1)){
							Double bonus = (double) 35000; //bonus penjualan Rp. 35 ribu
							Date sysdate = commonDao.selectSysdateTruncated(0);

							Double bonus_tax = hitungPajakKomisi(bonus, sysdate, komisi.getAgent_id());
							bonus_tax = FormatNumber.rounding(bonus_tax, false, 25);
							
							Map infoBonus = uwDao.selectBonusAgen(komisi.getAgent_id());
							this.uwDao.insertMst_comm_bonus(
									spaj, 1, 1, 1, komisi.getAgent_id(), 
									(String) infoBonus.get("MCL_FIRST"), (String) infoBonus.get("MSAG_TABUNGAN"), 
									infoBonus.get("LBN_ID").toString(), komisi.getCo_id(), bonus, bonus_tax, ldec_kurs, currentUser.getLus_id());
						}
					}*/
					
					/** END INSERT KE TABEL2 TAMBAHAN*/
				}
				//if(f_get_kom)
			}
			/** END LOOPING UTAMA */
			
			/** UNTUK DEBUGGING SAJA */
//			email.send(
//					new String[] {props.getProperty("admin.yusuf")},null,
//					"Proses Komisi 2007 [" + spaj + "]", emailMessage.toString(), currentUser);
			
		}
		
	}
	
	public boolean prosesKomisiTopUpIndividu2007(String spaj, BindException errors, User currentUser, Integer premi_ke) throws Exception{
		if(logger.isDebugEnabled()) logger.debug("PROSES: prosesKomisiTopUpIndividu2007 (of_komisi_tu_new())");
		StringBuffer emailMessage = new StringBuffer();
		emailMessage.append("[SPAJ        ] " + spaj + "\n");
		NumberFormat nf = NumberFormat.getInstance();
		List detail = uwDao.selectDetailBisnis(spaj);
		if(!detail.isEmpty()) {
			Map det = (HashMap) detail.get(0);
			emailMessage.append(
					"[PRODUK      ] (" + det.get("BISNIS") + ") " + det.get("LSBS_NAME") + " - " + 
					"(" + det.get("DETBISNIS") + ") " + det.get("LSDBS_NAME") + "\n");
		}

		//Yusuf - PERSENTASE OVERRIDING BARU - 21/12/2006 - SK No. 110/EL-SK/XII/2006
		BigDecimal persentase[] = new BigDecimal[] {
			new BigDecimal("1"), 
			new BigDecimal("2"), 
			new BigDecimal("8"), 
			new BigDecimal("14"), 
			new BigDecimal("100")};

		tingkatIndividu2007 current=null;
		tingkatIndividu2007 agen = null;

		//Informasi2 tambahan
		Map pribadi = this.uwDao.selectInfoPribadi(spaj);
		String ls_region = (String) pribadi.get("REGION");
		if(ls_region.startsWith("67")) {
			persentase = new BigDecimal[] {
					new BigDecimal("0"), 
					new BigDecimal("20"), 
					new BigDecimal("30"), 
					new BigDecimal("40"), 
					new BigDecimal("100")};
		}
		
		int li_pribadi = (Integer) pribadi.get("PRIBADI");
		emailMessage.append("[PRODUCTION DATE] " + pribadi.get("MSPRO_PROD_DATE") + "\n");

		Double premiTopup = this.uwDao.selectPremiTopUpUnitLink(spaj, premi_ke);
		double ldec_kurs = 1;

		//KALAU POLIS TUTUPAN PRIBADI, TIDAK MENDAPAT KOMISI
		if(li_pribadi == 1) return false; 
		String lca_id = ls_region.substring(0,2);
		String lwk_id = ls_region.substring(2,4);
		if("37,46,52".indexOf(lca_id)>-1){  //37 agency, 46 arthamas, 52 Agency Arthamas (Deddy 22 Feb 2012)
			return prosesKomisiTopUpAgencySystem(spaj, errors, currentUser, premi_ke, lca_id);
		//Yusuf (8/7/08) Proses Komisi Cross-Selling
		}else if(lca_id.equals("55")) {
			return prosesKomisiCrossSelling(spaj, currentUser, errors, premi_ke);
		}
		
		List lds_kom = this.uwDao.selectViewKomisiAsli(spaj);
		if(!lds_kom.isEmpty()) {
			//CEK DULU PAKE ROW PERTAMA, KALO KURS DOLLAR, ADA KURSNYA GAK
			Commission komisiME = (Commission) lds_kom.get(0);
			if(komisiME.getKurs_id().equals("02")){
				ldec_kurs = this.uwDao.selectLastCurrency(spaj, 1, 1);
				if(ldec_kurs<0){
					errors.reject("payment.noLastCurrency");
					return false;
				}
			}
			
			/** LOOPING UTAMA UNTUK MENGHITUNG KOMISI DARI V_GET_KOM (lds_kom) */			
			for(int i=0; i<lds_kom.size(); i++) {
				
				Commission komisi = (Commission) lds_kom.get(i);
				komisi.setLsbs_linebus(bacDao.selectLineBusLstBisnis(komisi.getBisnis_id().toString()));
				//
				komisi.setFlag_mess(Boolean.TRUE);
				komisi.setPremi(premiTopup);
				//kurs * premi
				komisi.setPremi(FormatNumber.round(komisi.getPremi() * ldec_kurs, 2));
				//pukul rata cara bayar (lscb_id)
				int li_cb_id = komisi.getCb_id();
				if(!products.progressiveLink(komisi.getBisnis_id().toString()) && !products.stableSavePremiBulanan(komisi.getBisnis_id().toString()) && !products.healthProductStandAlone(komisi.getBisnis_id().toString())){
					if( li_cb_id==1 || li_cb_id==2 || li_cb_id==6 ) komisi.setCb_id(3);
				}
				//Commission individu
				komisi.setLsco_jenis(new Integer(1));
				//ambil lama tanggung
				getLamaTanggung(komisi);
				//PENTING! untuk topup, LSCO_YEAR=0
				komisi.setTh_kom(0); 
				//tarik persentase komisinya, OR-nya di hardcode
				if(f_get_komisi(komisi, errors, 0, premi_ke, komisi.getKurs_id(), komisi.getPremi(), 4)) {
					/** START PERHITUNGAN OR (UNTUK LEVEL != 4) */
					//cari tahu dulu, nih agen apaan? 
					//4 ME
					//3 UM
					//2 & MSAG_FLAG_BM=0 -> AM / 2 & MSAG_FLAG_BM=1 -> BM 
					//1 & MSAG_SBM=1 -> SBM
					//1 & MSAG_SBM=0 -> RM
					if(komisi.getLsle_id()==4) agen = tingkatIndividu2007.ME;
					else if(komisi.getLsle_id()==3) agen = tingkatIndividu2007.UM;
					else if(komisi.getLsle_id()==2) agen = tingkatIndividu2007.BM;
					else if(komisi.getLsle_id()==1 && komisi.getFlag_sbm()==1) agen = tingkatIndividu2007.SBM;
					else if(komisi.getLsle_id()==1 && komisi.getFlag_sbm()==0) agen = tingkatIndividu2007.RM;
					
					if(komisi.getLev_kom()<4) {
						//karena OR baru, persentasenya dihardcode, gak ngambil dari database
						komisi.setKomisi((double) 0);
						//kalau yang nutup ME, ORnya gulung dari atas (RM)
						if(current.ordinal()>agen.ordinal()) {
							for(int j = current.ordinal(); j>agen.ordinal(); j--) {
								komisi.setKomisi(persentase[j-1].add(new BigDecimal(komisi.getKomisi())).doubleValue());
							}
						//kalo yang nutup bukan ME, ORnya gulung dari bawah (ME)
						}else if(current.equals(agen)) {
							for(int j = current.ordinal(); j<tingkatIndividu2007.ME.ordinal(); j++) {
								komisi.setKomisi(persentase[j].add(new BigDecimal(komisi.getKomisi())).doubleValue());
							}
						}
					}
					current = agen;
					/** END PERHITUNGAN OR */
					
					//stable link, ada or sept - dec 2008, seliain itu tidak ada
					int bulanProd = Integer.parseInt(uwDao.selectBulanProduksi(spaj));
					if(ls_region.substring(0,2).equals("42")){
						if(bulanProd >= 201002){ // Mulai bulan feb 2010 && merupakan worksite && msag_kry=1, maka OR tidak diberlakukan.
							if(komisi.getLev_kom().intValue() < 4 && financeDao.selectIsAgenKaryawan(komisi.getAgent_id())==1){
								komisi.setKomisi(0.);
							}
						}
					}
					
					if(komisi.getBisnis_id().toString().equals("164")){//
						Integer mgi = uwDao.selectMgiNewBusiness(spaj);
						if(bulanProd>=201105){
							if(mgi == 1) {
								komisi.setKomisi(0.05);
							}else if(mgi == 3) {
								komisi.setKomisi(0.15);
							}else if(mgi == 6) {
								komisi.setKomisi(0.3);
							}else if(mgi == 12) {
								komisi.setKomisi(0.6);
							}else if(mgi == 24) {
								komisi.setKomisi(1.2);
							}else if(mgi == 36) {
								komisi.setKomisi(1.35);
							}else {
								throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
							}
						}else{
							if(mgi == 1) {
								komisi.setKomisi(0.017); //Tidak ada MGI 1 bulan untuk stable link
							}else if(mgi == 3) {
								komisi.setKomisi(0.05);
							}else if(mgi == 6) {
								komisi.setKomisi(0.1);  
							}else if(mgi == 12) {
								komisi.setKomisi(0.2);
							}else if(mgi == 24) {
								komisi.setKomisi(0.4);
							}else if(mgi == 36) {
								komisi.setKomisi(0.6);
							}else {
								throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
							}
						}
					}else if(komisi.getBisnis_id().toString().equals("174")){
						Integer mgi = uwDao.selectMgiNewBusiness(spaj);
						if(mgi == 1) {
							komisi.setKomisi(0.05);
						}else if(mgi == 3) {
							komisi.setKomisi(0.15);
						}else if(mgi == 6) {
							komisi.setKomisi(0.3);
						}else if(mgi == 12) {
							komisi.setKomisi(0.6);
						}else if(mgi == 24) {
							komisi.setKomisi(1.2);
						}else if(mgi == 36) {
							komisi.setKomisi(1.35);
						}else {
							throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
						}
					}
					
					if(komisi.getLev_kom().intValue() < 4 && products.stableLink(komisi.getBisnis_id().toString())) {
						if(bulanProd >= 200809 && bulanProd <= 200812) {
							komisi.setKomisi(0.1 * komisi.getKomisi());
						}else {
							komisi.setKomisi(0.);
						}
					}
					
					/** START INSERTING COMMISSION */
					if(komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){

						if(komisi.getLev_kom()==4) {
							emailMessage.append("[PREMI TOPUP ] " + nf.format(komisi.getPremi()) + "\n");
							emailMessage.append("[KOMISI TOPUP] " + komisi.getLev_kom() + " : " + komisi.getAgent_id() + " : " + 
									nf.format(komisi.getPremi()) + " * " + nf.format(komisi.getKomisi()) + "% = ");
							komisi.setKomisi(komisi.getPremi() * komisi.getKomisi() / 100); //komisi = premi * persen komisi
							emailMessage.append(nf.format(komisi.getKomisi()) + "\n");
						} else {
							emailMessage.append("[OR TOPUP    ] " + komisi.getLev_kom() + " : " + komisi.getAgent_id() + " : " + 
									nf.format(komisiME.getKomisi()) + " * " + nf.format(komisi.getKomisi()) + "% = ");
							komisi.setKomisi(komisiME.getKomisi() * komisi.getKomisi() / 100); //OR = komisi ME * persen komisi
							emailMessage.append(nf.format(komisi.getKomisi()) + "\n");
						}
						
						//TAMBAHAN OLEH YUSUF UNTUK PRODUK2 SINGLE (IM No. 132/IM-DIR/XII/2007) - 16/01/2008
						komisi.setKomisi(cekLinkSingle(spaj, komisi.getKomisi(), komisi.getLev_kom(), true));
						
						komisi.setCo_id(sequenceMst_commission(11));
						komisi.setTax(new Double(komisi.getKomisi() * komisi.getTax() / 100));
						Date sysdate = commonDao.selectSysdateTruncated(0);
						if(komisi.getTax() > 0)komisi.setTax(f_load_tax(komisi.getKomisi(), sysdate, komisi.getAgent_id()));
						double ad_sisa;
						ad_sisa = komisi.getTax()%25;
						if(ad_sisa!=0){
							
						}
						komisi.setTax(FormatNumber.rounding(komisi.getTax() , true, 25));
						komisi.setKomisi(FormatNumber.round(komisi.getKomisi(), 0));
						if(komisi.getTax()==null)komisi.setTax(new Double(0));
						komisi.setNilai_kurs(new Double(ldec_kurs));
						komisi.setLus_id(currentUser.getLus_id());
						komisi.setMsbi_tahun_ke(1);
						komisi.setMsbi_premi_ke(premi_ke);
						if(komisi.getKomisi()>0) {
							this.uwDao.insertMst_commission(komisi, sysdate);
							
							//Yusuf - 10/07/09 - Bonus Komisi untuk TopUp StableLink dipisah ke comm_bonus
							if(!pribadi.get("PRIBADI").toString().equals("1") && products.stableLink(uwDao.selectBusinessId(komisi.getReg_spaj()))){
								if(i==0) {
									prosesBonusKomisi(
											"individu", ls_region, komisi.getReg_spaj(), komisi.getAgent_id(), komisi.getCo_id(), 
											komisi.getKurs_id(), komisi.getPremi(), ldec_kurs, currentUser.getLus_id(), komisi.getKomisi(), komisi.getTax(),
											komisi.getMsbi_tahun_ke(), komisi.getMsbi_premi_ke(), komisi.getLev_kom(), komisi.getLsbs_linebus());
								}
							}						
							
						}
					}
					/** END INSERTING COMMISSION */
				
					/** START INSERT KE TABEL2 TAMBAHAN */
					
					//Yusuf - 12 Sep 08 - Stable Link agency dan regional ada reward khusus sept - dec, selain itu tidak ada
					boolean isRewardStableLink = true;
					if(products.stableLink(komisi.getBisnis_id().toString())) {
						if("08,09,42,40,46".indexOf(lca_id) == -1 && bulanProd >= 200809 && bulanProd <= 200812) {
							isRewardStableLink = true;
						}else {
							isRewardStableLink = false;
						}
					}
					
					if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())) {
						isRewardStableLink = false;
					}
					
					if(lca_id.equals("42")){
						isRewardStableLink = false;
					}
					
					//proses insert reward untuk recruiter (pukul rata 10%, hanya apabila ada recruiter dan statusnya aktif)
					if(komisi.getLev_kom()==4 && isRewardStableLink){
						Map<String, Object> rekruter = this.uwDao.selectRekruter(komisi.getAgent_id());
						
						if(rekruter != null && !products.powerSave(komisi.getBisnis_id().toString()) &&  rekruter.get("MSRK_AKTIF").toString().equals("1")) {
							if(rekruter.get("NO_ACCOUNT")==null) {
								errors.reject("payment.noRecruiter", new Object[] {rekruter.get("MSRK_ID")}, "-"); throw new Exception(errors);
							} else if(rekruter.get("NO_ACCOUNT").toString().equals("")) {
								errors.reject("payment.noRecruiter", new Object[] {rekruter.get("MSRK_ID")}, "-"); throw new Exception(errors);
							} else {
								int jenis_rekrut = Integer.valueOf((String) rekruter.get("MSRK_JENIS_REKRUT"));
								//kalau jenis_rekrut = 2 (rekrut langsung), cek status aktifnya dari mst_agent
								//kalau jenis_rekrut lainnya, cek status aktifnya dari mst_rekruter
								if((((jenis_rekrut == 2 || jenis_rekrut==3) && rekruter.get("MSAG_ACTIVE").toString().equals("1")) || 
										(!(jenis_rekrut == 2 || jenis_rekrut==3) && rekruter.get("MSRK_AKTIF").toString().equals("1"))) && rekruter.get("MSAG_KOMISI").toString().equals("1")) {
									if(uwDao.selectbacCekAgen(rekruter.get("MSRK_ID").toString())!=null){
										
										Rekruter rek = uwDao.selectRekruterFromAgen(rekruter.get("MSRK_ID").toString());
										if(rek.getMsag_bay() == null) rek.setMsag_bay(0);
										
										double pengali = 1;
										
										//Yusuf (22/12/2010) bila business partner, maka dapat reward hanya 5%
										if(rek.getLcalwk().startsWith("61")) pengali = 0.5;
										//Yusuf (23/03/09) - Per 1 April OR dan Reward tidak 35% lagi tapi full
										else if(isLinkSingle(spaj, false) && bulanProd < 200904) pengali = 0.35;
										else if(isEkalink88Plus(spaj, true)) pengali = 0.7;
										else if(products.stableLink(komisi.getBisnis_id().toString())) pengali = 0.1;
										
										Double reward = FormatNumber.round((komisi.getKomisi() * 0.1 * pengali),0);
										
										Date sysdate = commonDao.selectSysdateTruncated(0);
										Double tax = FormatNumber.rounding(f_load_tax(reward, sysdate, rekruter.get("MSRK_ID").toString()), true, 25);
										rekruter.put("NO_ACCOUNT", StringUtils.replace(rekruter.get("NO_ACCOUNT").toString(), "-",""));
										
										if(rek.getMsag_bay().intValue() != 1){
											this.uwDao.insertMst_reward(spaj, 1, premi_ke, rekruter.get("MSRK_JENIS_REKRUT").toString(), rekruter.get("MSRK_ID").toString(),
													rekruter.get("MSRK_NAME").toString(), rekruter.get("NO_ACCOUNT").toString(), 
													rekruter.get("LBN_ID").toString(), reward, tax, new Double(ldec_kurs), currentUser.getLus_id(), komisi.getLsbs_linebus());
										}
										
										emailMessage.append("[RECRUITER REWARD] " + rekruter.get("MSRK_ID").toString() + " : " + nf.format(reward)+ "\n");
									}
								}
							}
						}
					}
					/** END INSERT KE TABEL2 TAMBAHAN */
					
				}
				//
			}
			/** END LOOPING UTAMA */			
			
		}
		
		/** UNTUK DEBUGGING SAJA */
//		email.send(
//				new String[] {props.getProperty("admin.yusuf")}, null,
//				"Proses Komisi Topup 2007 [" + spaj + "]", emailMessage.toString(), currentUser);
		if(!(lca_id.equals("73") && lwk_id.equals("01"))){
			this.uwDao.updateMst_billingTopup(spaj);
		}
				
		return true;
		
	}
	
	public boolean prosesKomisiBridge(String spaj, User currentUser, BindException errors, int premiKe) throws Exception{
		double kurs = 1;
		List lds_kom = this.uwDao.selectViewKomisiAsli(spaj);
		List detBisnis = uwDao.selectDetailBisnis(spaj);
		String elesbees = (String) ((Map) detBisnis.get(0)).get("BISNIS");
		String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
		
		if(!lds_kom.isEmpty() || (lds_kom.isEmpty() && (products.bridge(elesbees) ) ) ) {
			//CEK DULU PAKE ROW PERTAMA, KALO KURS DOLLAR, ADA KURSNYA GAK
			Commission komisiME = null;
			if(!lds_kom.isEmpty()){
				komisiME = (Commission) lds_kom.get(0);
				if(komisiME.getKurs_id().equals("02")){
					kurs = this.uwDao.selectLastCurrency(spaj, 1, premiKe);
					if(kurs<0){
						errors.reject("payment.noLastCurrency");
						return false;
					}
				}
			}
			
			if(products.bridge(elesbees)){
				Date sysdate = commonDao.selectSysdateTruncated(0);
				DateFormat df3 = new SimpleDateFormat("yyyyMM");
				String periode = df3.format(sysdate);
				
				for(int i=0; i<lds_kom.size(); i++) {
					Commission komisi = null;
					if(!lds_kom.isEmpty()){
						komisi = (Commission) lds_kom.get(i);
					}
					String mcl_id_kud = uwDao.selectMclIdfromAgent(uwDao.selectRekruterAgencySystem(komisi.getAgent_id())); //tarik rekruternya, jenis=4
					komisi.setFlag_mess(Boolean.TRUE);
					if(premiKe != 1) {
						komisi.setPremi(uwDao.selectPremiTopUpUnitLink(spaj, premiKe));
					}
					double premi_asli = komisi.getPremi();
					komisi.setPremi(FormatNumber.round(komisi.getPremi() * kurs, 2));
					//pukul rata cara bayar (lscb_id)
					int li_cb_id = komisi.getCb_id();
					if(!products.progressiveLink(komisi.getBisnis_id().toString()) && !products.stableSavePremiBulanan(komisi.getBisnis_id().toString()) && !products.healthProductStandAlone(komisi.getBisnis_id().toString())){
						if( li_cb_id==1 || li_cb_id==2 || li_cb_id==6 ) komisi.setCb_id(3);
					}
					//Commission bridge ke mst_diskon_perusahaan
					komisi.setLsco_jenis(6);
					//ambil lama tanggung
					getLamaTanggung(komisi);
					//tarik persentase komisinya, OR-nya di hardcode
					int tahunKe = 1;
					if(premiKe > 1) tahunKe = 0;
					if(f_get_komisi(komisi, errors, tahunKe, premiKe, komisi.getKurs_id(), komisi.getPremi(), 4)) {
						komisi.setMsco_comm_kud(komisi.getPremi() * komisi.getKomisi() / 100);
						komisi.setMsco_comm_kud(FormatNumber.round(komisi.getMsco_comm_kud(),0));
						komisi.setReg_spaj(spaj);
						//komisi.setMsco_tax_kud(bonus_tax);
						komisi.setMsco_tax_kud(0.);
						komisi.setPeriode_kud(periode);
						komisi.setMcl_id_kud(mcl_id_kud);
						komisi.setCreate_date_kud(sysdate);
						komisi.setLus_id(currentUser.getLus_id());
						komisi.setMsbi_tahun_ke(1);
						komisi.setMsbi_premi_ke(1);
						komisi.setJenis_kud(7);//khusus bridge
						
						if(komisi.getLev_kom()==4){
							this.uwDao.insertMst_diskon_perusahaan(komisi);
						}
					}
					
				}
			}
			
			
			if(!products.stableLink(elesbees) && !elesbees.toString().equals("096")){
				/** LOOPING UTAMA UNTUK MENGHITUNG KOMISI DARI V_GET_KOM (lds_kom) */			
				for(int i=0; i<lds_kom.size(); i++) {
					Commission komisi = null;
					if(!lds_kom.isEmpty()){
						komisi = (Commission) lds_kom.get(i);
					}
					komisi.setFlag_mess(Boolean.TRUE);
					if(premiKe != 1) {
						komisi.setPremi(uwDao.selectPremiTopUpUnitLink(spaj, premiKe));
					}
					//kurs * premi
					double premi_asli = komisi.getPremi();
					komisi.setPremi(FormatNumber.round(komisi.getPremi() * kurs, 2));
					//pukul rata cara bayar (lscb_id)
					int li_cb_id = komisi.getCb_id();
					if(!products.progressiveLink(komisi.getBisnis_id().toString()) && !products.stableSavePremiBulanan(komisi.getBisnis_id().toString()) && !products.healthProductStandAlone(komisi.getBisnis_id().toString())){
						if( li_cb_id==1 || li_cb_id==2 || li_cb_id==6 ) komisi.setCb_id(3);
					}
					//Commission individu
					komisi.setLsco_jenis(6);
					//ambil lama tanggung
					getLamaTanggung(komisi);
					//tarik persentase komisinya, OR-nya di hardcode
					int tahunKe = 1;
					if(premiKe > 1) tahunKe = 0;
					
					if(f_get_komisi(komisi, errors, tahunKe, premiKe, komisi.getKurs_id(), komisi.getPremi(), 4)) {
						
						//Yusuf - 9/7/08 - Proses untuk menarik data komisi powersave
						if(products.powerSave(komisi.getBisnis_id().toString()) || products.stableLink(komisi.getBisnis_id().toString()) || komisi.getBisnis_id().toString().equals("096")) {
							int mgi =0;
							double premi =0.;
							Date sysdate = commonDao.selectSysdateTruncated(0);
							DateFormat df3 = new SimpleDateFormat("yyyyMM");
							String periode = df3.format(sysdate);
							String mcl_id_kud = uwDao.selectMclIDPemegangPolis(spaj);
							if(products.stableLink(komisi.getBisnis_id().toString())) {
								premi = uwDao.selectTotalPremiNewBusiness(spaj); //khusus stable, angka premi untuk menghitung bonus penjualan dari total premi new business
								mgi = this.uwDao.selectMasaGaransiInvestasi(spaj, 1, 1);
							}else if(products.powerSave(komisi.getBisnis_id().toString())){
								premi = komisi.getPremi(); //lainnya, angka premi hanya dari premi bersangkutan
								mgi = this.uwDao.selectMasaGaransiInvestasi(spaj, 1, 1);
							}
							
							if(products.powerSave(komisi.getBisnis_id().toString())){
								TopUp t = (TopUp) this.uwDao.selectTopUp(spaj, 1, 1, "desc").get(0);
								Date ldt_tgl_rk = t.getMspa_date_book();
								if(ldt_tgl_rk.compareTo(defaultDateFormatReversed.parse("20111116"))< 0 ){//request TRI, berdasarkan tgl RK apabila tgl RK di bawah tgl 16 nov 2011, ambil komisi lama
									komisi.setTgl_kom(ldt_tgl_rk);
								}
								Map komisiPowersave = uwDao.selectKomisiPowersave(1, 
										komisi.getBisnis_id(), komisi.getBisnis_no(), 
										mgi, komisi.getKurs_id(), komisi.getLev_kom(), 1, premi, 0, komisi.getTgl_kom());
	
								if(komisiPowersave == null) {
									errors.reject("payment.noCommission", 
											new Object[]{
											   defaultDateFormat.format(komisi.getTgl_kom()),
											   komisi.getBisnis_id()+"-"+komisi.getBisnis_no(),
											   komisi.getCb_id(), 
											   komisi.getIns_period(), 
											   komisi.getKurs_id(), 
											   komisi.getLev_kom(),
											   komisi.getTh_kom(),
											   komisi.getLsco_jenis()
									           }, 
											"Commission POWERSAVE Tidak Ada.<br>- Tanggal: {0}<br>-Kode Bisnis: {1}<br>-Cara Bayar: {2}<br>-Ins_per: {3}<br>-Kurs: {4}<br>-Level: {5}<br>-Tahun Ke{6}<br>-Jenis: {7}");
									throw new Exception(errors);
								}else {
									if(komisiPowersave.get("LSCP_COMM")!=null) komisi.setKomisi(Double.valueOf(komisiPowersave.get("LSCP_COMM").toString()));
									if(komisiPowersave.get("LSCP_BONUS")!=null) komisi.setBonus(Double.valueOf(komisiPowersave.get("LSCP_BONUS").toString()));
								}
							}else if(komisi.getBisnis_id().toString().equals("164") || komisi.getBisnis_id().toString().equals("096")){//
								if(komisi.getBisnis_id().toString().equals("164")){
									if(mgi == 1) {
										komisi.setKomisi(0.017); //Tidak ada MGI 1 bulan untuk stable link
									}else if(mgi == 3) {
										komisi.setKomisi(0.05);
									}else if(mgi == 6) {
										komisi.setKomisi(0.1);  
									}else if(mgi == 12) {
										komisi.setKomisi(0.2);
									}else if(mgi == 24) {
										komisi.setKomisi(0.4);
									}else if(mgi == 36) {
										komisi.setKomisi(0.6);
									}else {
										throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
									}
								}else if(komisi.getBisnis_id().toString().equals("096")){
									komisi.setKomisi(0.2);
								}
								komisi.setMsco_comm_kud(premi * komisi.getKomisi() / 100);
								komisi.setMsco_comm_kud(FormatNumber.round(komisi.getKomisi(), 0));
								komisi.setReg_spaj(spaj);
								//komisi.setMsco_tax_kud(bonus_tax);
								komisi.setMsco_tax_kud(0.);
								komisi.setPeriode_kud(periode);
								komisi.setMcl_id_kud("'010000036426'");
								komisi.setCreate_date_kud(sysdate);
								komisi.setLus_id(currentUser.getLus_id());
								komisi.setMsbi_tahun_ke(1);
								komisi.setMsbi_premi_ke(1);
								komisi.setJenis_kud(6);
								
								if(komisi.getLev_kom()==4){
									this.uwDao.insertMst_diskon_perusahaan(komisi);
								}
								//Yusuf - 10/07/09 - Bonus Komisi untuk TopUp StableLink dipisah ke comm_bonus
								Map pribadi = this.uwDao.selectInfoPribadi(spaj);
								if(!pribadi.get("PRIBADI").toString().equals("1")){
									if(i==0) {
										String ls_region = this.uwDao.selectCabangFromSpaj(spaj);
										Double ldec_kurs = new Double(1); 
										if(komisi.getKurs_id().equals("02")){
											ldec_kurs = new Double(this.uwDao.selectLastCurrency(spaj, 1, 1));
											if(ldec_kurs==null){
												errors.reject("payment.noLastCurrency");
												throw new Exception(errors);
											}
										}
										prosesBonusKomisi(
												"crossselling", ls_region, komisi.getReg_spaj(), komisi.getAgent_id(), komisi.getCo_id(), 
												komisi.getKurs_id(), premi, ldec_kurs, currentUser.getLus_id(), komisi.getKomisi(), komisi.getTax(),
												komisi.getMsbi_tahun_ke(), komisi.getMsbi_premi_ke(), komisi.getLev_kom(), komisi.getLsbs_linebus());
									}
								}
							}
							
						}
						
						if(products.powerSave(komisi.getBisnis_id().toString())){
							/** (DI LEVEL 4) */
							if(komisi.getLev_kom()==4) {
								/** START INSERTING COMMISSION */
								if(komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
									double persen = komisi.getKomisi();
									if("183,189,193,195,201,204".indexOf(komisi.getBisnis_id().toString())>-1 ){
										komisi.setPremi(bacDao.selectTotalKomisiEkaSehat(spaj));
									}
									komisi.setKomisi(komisi.getPremi() * komisi.getKomisi() / 100); //komisi = premi * persen komisi
									
									komisi.setCo_id(sequenceMst_commission(11));
									komisi.setTax(new Double(komisi.getKomisi() * komisi.getTax() / 100));
									Date sysdate = commonDao.selectSysdateTruncated(0);
									if(komisi.getTax() > 0)komisi.setTax(f_load_tax(komisi.getKomisi(), sysdate, komisi.getAgent_id()));
									double ad_sisa;
									ad_sisa = komisi.getTax()%25;
									if(ad_sisa!=0){
										
									}
									komisi.setTax(FormatNumber.rounding(komisi.getTax() , true, 25));
									komisi.setKomisi(FormatNumber.round(komisi.getKomisi(),  0));
									if(komisi.getTax()==null)komisi.setTax(new Double(0));
									
									komisi.setNilai_kurs(new Double(kurs));
									komisi.setLus_id(currentUser.getLus_id());
									komisi.setMsbi_tahun_ke(1);
									komisi.setMsbi_premi_ke(premiKe);
									komisi.setMsco_flag(0);
									if(komisi.getKomisi()>0) {
										this.uwDao.insertMst_commission(komisi, sysdate);
									}
									
									//tambahan : Yusuf (29/04/08) ada proses komisi rider, mulai 1 may 2008
									prosesKomisiRider(komisi, i, persen);						
								}
								/** END INSERTING COMMISSION */
								
							/** Komisi OR, hanya untuk level 3 saja */
							}else if(komisi.getLev_kom()==3) {
								/** START INSERTING COMMISSION */
								if(komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
									komisi.setKomisi((double) 50); //OR untuk cross selling, fix 50%
									double persen = komisi.getKomisi();
									komisi.setKomisi(komisiME.getKomisi() * komisi.getKomisi() / 100); //OR = komisi ME * persen komisi
									
									komisi.setCo_id(sequenceMst_commission(11));
									komisi.setTax(new Double(komisi.getKomisi() * (4.5 / 100))); //komisi OR untuk cross selling, tax nya fix 4.5%
									double ad_sisa;
									ad_sisa = komisi.getTax()%25;
									if(ad_sisa!=0){
										
									}
									komisi.setTax(FormatNumber.rounding(komisi.getTax() , true, 25));
									komisi.setKomisi(FormatNumber.round(komisi.getKomisi(),  0));
									if(komisi.getTax()==null)komisi.setTax(new Double(0));
									
									komisi.setNilai_kurs(new Double(kurs));
									komisi.setLus_id(currentUser.getLus_id());
									komisi.setMsbi_tahun_ke(1);
									komisi.setMsbi_premi_ke(premiKe);
									komisi.setMsco_flag(0);
									if(komisi.getKomisi()>0) {
										Date sysdate = commonDao.selectSysdateTruncated(0);
										this.uwDao.insertMst_commission(komisi, sysdate);
									}
									
									//tambahan : Yusuf (29/04/08) ada proses komisi rider, mulai 1 may 2008
									prosesKomisiRider(komisi, i, persen);						
								}
								/** END INSERTING COMMISSION */
							}
						}
						
						
					}
				}
			}else{
				for(int i=0; i<=lds_kom.size(); i++) {
					Commission komisi = new Commission();
					if(!lds_kom.isEmpty()){
						komisi = (Commission) lds_kom.get(i);
					}
					komisi.setFlag_mess(Boolean.TRUE);
					if(premiKe != 1) {
						komisi.setPremi(uwDao.selectPremiTopUpUnitLink(spaj, premiKe));
					}
					komisi.setBisnis_id(Integer.parseInt(elesbees));
					
					Map premi_utama = uwDao.selectPremiProdukUtama(spaj);
					BigDecimal premibikinribet = (BigDecimal) premi_utama.get("MSPR_PREMIUM");
					Double premi = premibikinribet.doubleValue();
					komisi.setPremi(premi);
					//kurs * premi
					double premi_asli = komisi.getPremi();
					komisi.setPremi(FormatNumber.round(komisi.getPremi() * kurs, 2));
					//pukul rata cara bayar (lscb_id)
					komisi.setCb_id(3);
					
					//Commission individu
					komisi.setLsco_jenis(1);
					//ambil lama tanggung
					getLamaTanggung(komisi);
					//tarik persentase komisinya, OR-nya di hardcode
					int tahunKe = 1;
					if(premiKe > 1) tahunKe = 0;
					komisi.setLev_kom(4);
					
						//Yusuf - 9/7/08 - Proses untuk menarik data komisi powersave
						if(products.stableLink(komisi.getBisnis_id().toString()) || komisi.getBisnis_id().toString().equals("096")) {
							int mgi =0;
							Date sysdate = commonDao.selectSysdateTruncated(0);
							DateFormat df3 = new SimpleDateFormat("yyyyMM");
							String periode = df3.format(sysdate);
							String mcl_id_kud = uwDao.selectMclIDPemegangPolis(spaj);
							if(products.stableLink(komisi.getBisnis_id().toString())) {
								premi = uwDao.selectTotalPremiNewBusiness(spaj); //khusus stable, angka premi untuk menghitung bonus penjualan dari total premi new business
								mgi = this.uwDao.selectMasaGaransiInvestasi(spaj, 1, 1);
							}
							
							if(komisi.getBisnis_id().toString().equals("164") || komisi.getBisnis_id().toString().equals("096")){//
								if(komisi.getBisnis_id().toString().equals("164")){
									if(mgi == 1) {
										komisi.setKomisi(0.017); //Tidak ada MGI 1 bulan untuk stable link
									}else if(mgi == 3) {
										komisi.setKomisi(0.05);
									}else if(mgi == 6) {
										komisi.setKomisi(0.1);  
									}else if(mgi == 12) {
										komisi.setKomisi(0.2);
									}else if(mgi == 24) {
										komisi.setKomisi(0.4);
									}else if(mgi == 36) {
										komisi.setKomisi(0.6);
									}else {
										throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
									}
								}else if(komisi.getBisnis_id().toString().equals("096")){
									komisi.setKomisi(0.2);
								}
								komisi.setMsco_comm_kud(premi * komisi.getKomisi() / 100);
								komisi.setMsco_comm_kud(FormatNumber.round(komisi.getKomisi(),  0));
								komisi.setReg_spaj(spaj);
								//komisi.setMsco_tax_kud(bonus_tax);
								komisi.setMsco_tax_kud(0.);
								komisi.setPeriode_kud(periode);
								komisi.setMcl_id_kud("010000036426");
								komisi.setCreate_date_kud(sysdate);
								komisi.setLus_id(currentUser.getLus_id());
								komisi.setMsbi_tahun_ke(1);
								komisi.setMsbi_premi_ke(1);
								komisi.setJenis_kud(6);
								
								if(komisi.getLev_kom()==4){
									this.uwDao.insertMst_diskon_perusahaan(komisi);
								}
								//Yusuf - 10/07/09 - Bonus Komisi untuk TopUp StableLink dipisah ke comm_bonus
//								Map pribadi = this.uwDao.selectInfoPribadi(spaj);
//								if(!pribadi.get("PRIBADI").toString().equals("1")){
//									if(i==0) {
//										String ls_region = this.uwDao.selectCabangFromSpaj(spaj);
//										Double ldec_kurs = new Double(1); 
//										if(komisi.getKurs_id().equals("02")){
//											ldec_kurs = new Double(this.uwDao.selectLastCurrency(spaj, 1, 1));
//											if(ldec_kurs==null){
//												errors.reject("payment.noLastCurrency");
//												throw new Exception(errors);
//											}
//										}
//										prosesBonusKomisi(
//												"crossselling", ls_region, komisi.getReg_spaj(), komisi.getAgent_id(), komisi.getCo_id(), 
//												komisi.getKurs_id(), premi, ldec_kurs, currentUser.getLus_id(), komisi.getKomisi(), komisi.getTax(),
//												komisi.getMsbi_tahun_ke(), komisi.getMsbi_premi_ke(), komisi.getLev_kom());
//									}
//								}
							}
							
						}
				}
			}
		}
		return true;		
	}
	
	/**
	 * Proses komisi untuk agen cross-selling, yaitu agen luar yang menjual produk AJS, misalnya dari agen ASM
	 * Aturan:
	 * - LCA_ID = 55
	 * - Struktur hanya terdiri dari 2 agen, yaitu:
	 * 		- ME (agen ini mendapat komisi sesuai persentasi produk, dan memakai rate komisi REGIONAL)
	 * 		- RM (bukan berupa agen, melainkan badan/PT. Untuk produk yg mendapat OR, OR nya adalah 50% dengan pajak 4.5%)
	 * 
	 * Proses ini digunakan untuk premi pokok maupun topup
	 * 
	 * @author Yusuf
	 * @since Jul 8, 2008 (6:23:52 PM)
	 * @param spaj
	 * @param currentUser
	 * @param errors
	 * @throws Exception
	 */
	public boolean prosesKomisiCrossSelling(String spaj, User currentUser, BindException errors, int premiKe) throws Exception{
		double kurs = 1;
		List lds_kom = this.uwDao.selectViewKomisiAsli(spaj);
		List detBisnis = uwDao.selectDetailBisnis(spaj);
		String elesbees = (String) ((Map) detBisnis.get(0)).get("BISNIS");
		String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
		
		if(!lds_kom.isEmpty() || (lds_kom.isEmpty() && (products.stableLink(elesbees) || elesbees.toString().equals("096")) ) ) {
			//CEK DULU PAKE ROW PERTAMA, KALO KURS DOLLAR, ADA KURSNYA GAK
			Commission komisiME = null;
			if(!lds_kom.isEmpty()){
				komisiME = (Commission) lds_kom.get(0);
				if(komisiME.getKurs_id().equals("02")){
					kurs = this.uwDao.selectLastCurrency(spaj, 1, premiKe);
					if(kurs<0){
						errors.reject("payment.noLastCurrency");
						return false;
					}
				}
			}
			
			if(!products.stableLink(elesbees) && !elesbees.toString().equals("096")){
				/** LOOPING UTAMA UNTUK MENGHITUNG KOMISI DARI V_GET_KOM (lds_kom) */			
				for(int i=0; i<lds_kom.size(); i++) {
					Commission komisi = null;
					if(!lds_kom.isEmpty()){
						komisi = (Commission) lds_kom.get(i);
					}
					komisi.setFlag_mess(Boolean.TRUE);
					if(premiKe != 1) {
						komisi.setPremi(uwDao.selectPremiTopUpUnitLink(spaj, premiKe));
					}
					//kurs * premi
					double premi_asli = komisi.getPremi();
					komisi.setPremi(FormatNumber.round(komisi.getPremi() * kurs, 2));
					//pukul rata cara bayar (lscb_id)
					int li_cb_id = komisi.getCb_id();
					if(!products.progressiveLink(komisi.getBisnis_id().toString()) && !products.stableSavePremiBulanan(komisi.getBisnis_id().toString()) && !products.healthProductStandAlone(komisi.getBisnis_id().toString())){
						if( li_cb_id==1 || li_cb_id==2 || li_cb_id==6 ) komisi.setCb_id(3);
					}
					//Commission individu
					komisi.setLsco_jenis(1);
					//ambil lama tanggung
					getLamaTanggung(komisi);
					//tarik persentase komisinya, OR-nya di hardcode
					int tahunKe = 1;
					if(premiKe > 1) tahunKe = 0;
					
					if(f_get_komisi(komisi, errors, tahunKe, premiKe, komisi.getKurs_id(), komisi.getPremi(), 4)) {
						
						//Yusuf - 9/7/08 - Proses untuk menarik data komisi powersave
						if(products.powerSave(komisi.getBisnis_id().toString()) || products.stableLink(komisi.getBisnis_id().toString()) || komisi.getBisnis_id().toString().equals("096")) {
							int mgi =0;
							double premi =0.;
							Date sysdate = commonDao.selectSysdateTruncated(0);
							DateFormat df3 = new SimpleDateFormat("yyyyMM");
							String periode = df3.format(sysdate);
							String mcl_id_kud = uwDao.selectMclIDPemegangPolis(spaj);
							if(products.stableLink(komisi.getBisnis_id().toString())) {
								premi = uwDao.selectTotalPremiNewBusiness(spaj); //khusus stable, angka premi untuk menghitung bonus penjualan dari total premi new business
								mgi = this.uwDao.selectMasaGaransiInvestasi(spaj, 1, 1);
							}else if(products.powerSave(komisi.getBisnis_id().toString())){
								premi = komisi.getPremi(); //lainnya, angka premi hanya dari premi bersangkutan
								mgi = this.uwDao.selectMasaGaransiInvestasi(spaj, 1, 1);
							}
							
							if(products.powerSave(komisi.getBisnis_id().toString())){
								TopUp t = (TopUp) this.uwDao.selectTopUp(spaj, 1, 1, "desc").get(0);
								Date ldt_tgl_rk = t.getMspa_date_book();
								if(ldt_tgl_rk.compareTo(defaultDateFormatReversed.parse("20111116"))< 0 ){//request TRI, berdasarkan tgl RK apabila tgl RK di bawah tgl 16 nov 2011, ambil komisi lama
									komisi.setTgl_kom(ldt_tgl_rk);
								}
								Map komisiPowersave = uwDao.selectKomisiPowersave(1, 
										komisi.getBisnis_id(), komisi.getBisnis_no(), 
										mgi, komisi.getKurs_id(), komisi.getLev_kom(), 1, premi, 0, komisi.getTgl_kom());
	
								if(komisiPowersave == null) {
									errors.reject("payment.noCommission", 
											new Object[]{
											   defaultDateFormat.format(komisi.getTgl_kom()),
											   komisi.getBisnis_id()+"-"+komisi.getBisnis_no(),
											   komisi.getCb_id(), 
											   komisi.getIns_period(), 
											   komisi.getKurs_id(), 
											   komisi.getLev_kom(),
											   komisi.getTh_kom(),
											   komisi.getLsco_jenis()
									           }, 
											"Commission POWERSAVE Tidak Ada.<br>- Tanggal: {0}<br>-Kode Bisnis: {1}<br>-Cara Bayar: {2}<br>-Ins_per: {3}<br>-Kurs: {4}<br>-Level: {5}<br>-Tahun Ke{6}<br>-Jenis: {7}");
									throw new Exception(errors);
								}else {
									if(komisiPowersave.get("LSCP_COMM")!=null) komisi.setKomisi(Double.valueOf(komisiPowersave.get("LSCP_COMM").toString()));
									if(komisiPowersave.get("LSCP_BONUS")!=null) komisi.setBonus(Double.valueOf(komisiPowersave.get("LSCP_BONUS").toString()));
								}
							}else if(komisi.getBisnis_id().toString().equals("164") || komisi.getBisnis_id().toString().equals("096")){//
								if(komisi.getBisnis_id().toString().equals("164")){
									if(mgi == 1) {
										komisi.setKomisi(0.017); //Tidak ada MGI 1 bulan untuk stable link
									}else if(mgi == 3) {
										komisi.setKomisi(0.05);
									}else if(mgi == 6) {
										komisi.setKomisi(0.1);  
									}else if(mgi == 12) {
										komisi.setKomisi(0.2);
									}else if(mgi == 24) {
										komisi.setKomisi(0.4);
									}else if(mgi == 36) {
										komisi.setKomisi(0.6);
									}else {
										throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
									}
								}else if(komisi.getBisnis_id().toString().equals("096")){
									komisi.setKomisi(0.2);
								}
								komisi.setMsco_comm_kud(premi * komisi.getKomisi() / 100);
								komisi.setMsco_comm_kud(FormatNumber.round(komisi.getKomisi(), 0));
								komisi.setReg_spaj(spaj);
								//komisi.setMsco_tax_kud(bonus_tax);
								komisi.setMsco_tax_kud(0.);
								komisi.setPeriode_kud(periode);
								komisi.setMcl_id_kud("'010000036426'");
								komisi.setCreate_date_kud(sysdate);
								komisi.setLus_id(currentUser.getLus_id());
								komisi.setMsbi_tahun_ke(1);
								komisi.setMsbi_premi_ke(1);
								komisi.setJenis_kud(6);
								
								if(komisi.getLev_kom()==4){
									this.uwDao.insertMst_diskon_perusahaan(komisi);
								}
								//Yusuf - 10/07/09 - Bonus Komisi untuk TopUp StableLink dipisah ke comm_bonus
								Map pribadi = this.uwDao.selectInfoPribadi(spaj);
								if(!pribadi.get("PRIBADI").toString().equals("1")){
									if(i==0) {
										String ls_region = this.uwDao.selectCabangFromSpaj(spaj);
										Double ldec_kurs = new Double(1); 
										if(komisi.getKurs_id().equals("02")){
											ldec_kurs = new Double(this.uwDao.selectLastCurrency(spaj, 1, 1));
											if(ldec_kurs==null){
												errors.reject("payment.noLastCurrency");
												throw new Exception(errors);
											}
										}
										prosesBonusKomisi(
												"crossselling", ls_region, komisi.getReg_spaj(), komisi.getAgent_id(), komisi.getCo_id(), 
												komisi.getKurs_id(), premi, ldec_kurs, currentUser.getLus_id(), komisi.getKomisi(), komisi.getTax(),
												komisi.getMsbi_tahun_ke(), komisi.getMsbi_premi_ke(), komisi.getLev_kom(), komisi.getLsbs_linebus());
									}
								}
							}
							
						}
						
						if(products.powerSave(komisi.getBisnis_id().toString())){
							/** (DI LEVEL 4) */
							if(komisi.getLev_kom()==4) {
								/** START INSERTING COMMISSION */
								if(komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
									double persen = komisi.getKomisi();
									if("183,189,193,195,201,204".indexOf(komisi.getBisnis_id().toString())>-1 ){
										komisi.setPremi(bacDao.selectTotalKomisiEkaSehat(spaj));
									}
									komisi.setKomisi(komisi.getPremi() * komisi.getKomisi() / 100); //komisi = premi * persen komisi
									
									komisi.setCo_id(sequenceMst_commission(11));
									komisi.setTax(new Double(komisi.getKomisi() * komisi.getTax() / 100));
									Date sysdate = commonDao.selectSysdateTruncated(0);
									if(komisi.getTax() > 0)komisi.setTax(f_load_tax(komisi.getKomisi(), sysdate, komisi.getAgent_id()));
									double ad_sisa;
									ad_sisa = komisi.getTax()%25;
									if(ad_sisa!=0){
										
									}
									komisi.setTax(FormatNumber.rounding(komisi.getTax() , true, 25));
									komisi.setKomisi(FormatNumber.round(komisi.getKomisi(),0));
									if(komisi.getTax()==null)komisi.setTax(new Double(0));
									
									komisi.setNilai_kurs(new Double(kurs));
									komisi.setLus_id(currentUser.getLus_id());
									komisi.setMsbi_tahun_ke(1);
									komisi.setMsbi_premi_ke(premiKe);
									komisi.setMsco_flag(0);
									if(komisi.getKomisi()>0) {
										this.uwDao.insertMst_commission(komisi, sysdate);
									}
									
									//tambahan : Yusuf (29/04/08) ada proses komisi rider, mulai 1 may 2008
									prosesKomisiRider(komisi, i, persen);						
								}
								/** END INSERTING COMMISSION */
								
							/** Komisi OR, hanya untuk level 3 saja */
							}else if(komisi.getLev_kom()==3) {
								/** START INSERTING COMMISSION */
								if(komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
									komisi.setKomisi((double) 50); //OR untuk cross selling, fix 50%
									double persen = komisi.getKomisi();
									komisi.setKomisi(komisiME.getKomisi() * komisi.getKomisi() / 100); //OR = komisi ME * persen komisi
									
									komisi.setCo_id(sequenceMst_commission(11));
									komisi.setTax(new Double(komisi.getKomisi() * (4.5 / 100))); //komisi OR untuk cross selling, tax nya fix 4.5%
									double ad_sisa;
									ad_sisa = komisi.getTax()%25;
									if(ad_sisa!=0){
										
									}
									komisi.setTax(FormatNumber.rounding(komisi.getTax() , true, 25));
									komisi.setKomisi(FormatNumber.round(komisi.getKomisi(),0));
									if(komisi.getTax()==null)komisi.setTax(new Double(0));
									
									komisi.setNilai_kurs(new Double(kurs));
									komisi.setLus_id(currentUser.getLus_id());
									komisi.setMsbi_tahun_ke(1);
									komisi.setMsbi_premi_ke(premiKe);
									komisi.setMsco_flag(0);
									if(komisi.getKomisi()>0) {
										Date sysdate = commonDao.selectSysdateTruncated(0);
										this.uwDao.insertMst_commission(komisi, sysdate);
									}
									
									//tambahan : Yusuf (29/04/08) ada proses komisi rider, mulai 1 may 2008
									prosesKomisiRider(komisi, i, persen);						
								}
								/** END INSERTING COMMISSION */
							}
						}
						
						
					}
				}
			}else{
				for(int i=0; i<=lds_kom.size(); i++) {
					Commission komisi = new Commission();
					if(!lds_kom.isEmpty()){
						komisi = (Commission) lds_kom.get(i);
					}
					komisi.setFlag_mess(Boolean.TRUE);
					if(premiKe != 1) {
						komisi.setPremi(uwDao.selectPremiTopUpUnitLink(spaj, premiKe));
					}
					komisi.setBisnis_id(Integer.parseInt(elesbees));
					
					Map premi_utama = uwDao.selectPremiProdukUtama(spaj);
					BigDecimal premibikinribet = (BigDecimal) premi_utama.get("MSPR_PREMIUM");
					Double premi = premibikinribet.doubleValue();
					komisi.setPremi(premi);
					//kurs * premi
					double premi_asli = komisi.getPremi();
					komisi.setPremi(FormatNumber.round(komisi.getPremi() * kurs, 2));
					//pukul rata cara bayar (lscb_id)
					komisi.setCb_id(3);
					
					//Commission individu
					komisi.setLsco_jenis(1);
					//ambil lama tanggung
					getLamaTanggung(komisi);
					//tarik persentase komisinya, OR-nya di hardcode
					int tahunKe = 1;
					if(premiKe > 1) tahunKe = 0;
					komisi.setLev_kom(4);
					
						//Yusuf - 9/7/08 - Proses untuk menarik data komisi powersave
						if(products.stableLink(komisi.getBisnis_id().toString()) || komisi.getBisnis_id().toString().equals("096")) {
							int mgi =0;
							Date sysdate = commonDao.selectSysdateTruncated(0);
							DateFormat df3 = new SimpleDateFormat("yyyyMM");
							String periode = df3.format(sysdate);
							String mcl_id_kud = uwDao.selectMclIDPemegangPolis(spaj);
							if(products.stableLink(komisi.getBisnis_id().toString())) {
								premi = uwDao.selectTotalPremiNewBusiness(spaj); //khusus stable, angka premi untuk menghitung bonus penjualan dari total premi new business
								mgi = this.uwDao.selectMasaGaransiInvestasi(spaj, 1, 1);
							}
							
							if(komisi.getBisnis_id().toString().equals("164") || komisi.getBisnis_id().toString().equals("096")){//
								if(komisi.getBisnis_id().toString().equals("164")){
									if(mgi == 1) {
										komisi.setKomisi(0.017); //Tidak ada MGI 1 bulan untuk stable link
									}else if(mgi == 3) {
										komisi.setKomisi(0.05);
									}else if(mgi == 6) {
										komisi.setKomisi(0.1);  
									}else if(mgi == 12) {
										komisi.setKomisi(0.2);
									}else if(mgi == 24) {
										komisi.setKomisi(0.4);
									}else if(mgi == 36) {
										komisi.setKomisi(0.6);
									}else {
										throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
									}
								}else if(komisi.getBisnis_id().toString().equals("096")){
									komisi.setKomisi(0.2);
								}
								komisi.setMsco_comm_kud(premi * komisi.getKomisi() / 100);
								komisi.setMsco_comm_kud(FormatNumber.round(komisi.getKomisi(), 0));
								komisi.setReg_spaj(spaj);
								//komisi.setMsco_tax_kud(bonus_tax);
								komisi.setMsco_tax_kud(0.);
								komisi.setPeriode_kud(periode);
								komisi.setMcl_id_kud("010000036426");
								komisi.setCreate_date_kud(sysdate);
								komisi.setLus_id(currentUser.getLus_id());
								komisi.setMsbi_tahun_ke(1);
								komisi.setMsbi_premi_ke(1);
								komisi.setJenis_kud(6);
								
								if(komisi.getLev_kom()==4){
									this.uwDao.insertMst_diskon_perusahaan(komisi);
								}
								//Yusuf - 10/07/09 - Bonus Komisi untuk TopUp StableLink dipisah ke comm_bonus
//								Map pribadi = this.uwDao.selectInfoPribadi(spaj);
//								if(!pribadi.get("PRIBADI").toString().equals("1")){
//									if(i==0) {
//										String ls_region = this.uwDao.selectCabangFromSpaj(spaj);
//										Double ldec_kurs = new Double(1); 
//										if(komisi.getKurs_id().equals("02")){
//											ldec_kurs = new Double(this.uwDao.selectLastCurrency(spaj, 1, 1));
//											if(ldec_kurs==null){
//												errors.reject("payment.noLastCurrency");
//												throw new Exception(errors);
//											}
//										}
//										prosesBonusKomisi(
//												"crossselling", ls_region, komisi.getReg_spaj(), komisi.getAgent_id(), komisi.getCo_id(), 
//												komisi.getKurs_id(), premi, ldec_kurs, currentUser.getLus_id(), komisi.getKomisi(), komisi.getTax(),
//												komisi.getMsbi_tahun_ke(), komisi.getMsbi_premi_ke(), komisi.getLev_kom());
//									}
//								}
							}
							
						}
				}
			}
		}
		return true;		
	}	

	/**
		Yusuf - 15/09/06 - khusus produk EKALINK REGULER (159-2)
		Yusuf - 03/03/09 - dan khusus produk IKHLAS & SAQINAH (MUAMALAT)
	 	Yusuf - 08/04/09 - Start 13 April, Insert Bonus Penjualan Powersave dipisah ke comm_bonus
	 	Yusuf - 10/06/09 - Insert Bonus Penjualan Hidup Bahagia dipisah ke comm_bonus
	 	Yusuf - 10/07/09 - Bonus penjualan untuk Stable Link (efektif 13 jul 09)
	 	Deddy - 21/07/09 - Bonus penjualan untuk Smart Medicare
	 	lufi - 1/04/2014 - Bonus penjualan untuk SMile Link 99
	 	
	 	Catatan : Bonus Tax, karena pajak progresif, harus dihitung dari keseluruhan komisi + bonus
	 	makanya saat perhitungan tax adalah sbb : 
	 	tax dari total komisi dan bonus dihitung lebih dahulu, lalu dikurangi tax dari komisi saja
	 	selisihnya adalah = tax dari bonus
	 */
	private void prosesBonusKomisi(
			String distribusi, String region, String reg_spaj, String msag_id, String msco_id, 
			String lku_id, double premi, double kurs, String lus_id, double komisi_awal, double tax_awal, 
			int tahun_ke, int premi_ke, int lev_comm, int lsbs_linebus){
		
		String lsbs 	= FormatString.rpad("0", uwDao.selectBusinessId(reg_spaj), 3);
		String lsdbs 	= FormatString.rpad("0", uwDao.selectBusinessNumber(reg_spaj).toString(), 3);
		String lca 		= uwDao.selectCabangFromSpaj(reg_spaj);
		String lwk		= uwDao.selectCabangFromSpaj_lar(reg_spaj).substring(2,4);
		int bulanProd = Integer.parseInt(uwDao.selectBulanProduksi(reg_spaj));
		
		//kondisi untuk bethanny
		if(region.substring(0, 4).equals("37A8") || region.substring(0, 4).equals("37C7")){
			Date sysdate = commonDao.selectSysdateTruncated(0);
			DateFormat df3 = new SimpleDateFormat("yyyyMM");
			String periode = df3.format(sysdate);
			//Untuk Holyland Invest Bethany 
			if(lsbs.equals("096") && (Integer.parseInt(lsdbs)>=13 && Integer.parseInt(lsdbs)<=15)){
				Double bonus = FormatNumber.round(0.025 * premi,  0); //bonus penjualan 5%
				
				//perhitungan pajak progresif dari TOTAL komisi dan bonus
				Double temp = komisi_awal + bonus;
				Double bonus_tax = f_load_tax(temp, sysdate, msag_id);
				bonus_tax -= tax_awal;
				bonus_tax = FormatNumber.rounding(bonus_tax, false, 25);
				Map infoBonus = uwDao.selectBonusAgen(msag_id);
				if(bonus > 0){
					this.uwDao.insertMst_comm_bonus( //tipe = 1 (bonus penjualan)
							reg_spaj, 1, 1, 1, msag_id, 
							(String) infoBonus.get("MCL_FIRST"), (String) infoBonus.get("MSAG_TABUNGAN"), 
							infoBonus.get("LBN_ID").toString(), msco_id, bonus, bonus_tax, kurs, lus_id, commonDao.selectSysdateTruncated(0), lsbs_linebus);
				}
				//String mcl_id_kud = uwDao.selectMclIDPemegangPolis(reg_spaj);
				//REQ HIMMIA : Untuk MCL_ID, ambil dari rekruter 
				String mcl_id_kud = uwDao.selectMclIdfromAgent(uwDao.selectRekruterAgencySystem(msag_id)); //tarik rekruternya, jenis=4
			
				
				//Untuk persembahan umum(nominal), khusus(pkhusus), dan kasih(pkasih dan pakm)
				Commission komisi = new Commission();
				komisi.setReg_spaj(reg_spaj);
				komisi.setMsco_comm_kud(FormatNumber.round(0.03 * premi,0)); // 3% premi
				komisi.setPkhusus(FormatNumber.rounding(0.1 * komisi_awal, false, 25)); // 10% komisi
				//komisi.setMsco_tax_kud(bonus_tax);
				komisi.setMsco_tax_kud(0.);
				komisi.setPeriode_kud(periode);
				komisi.setMcl_id_kud(mcl_id_kud);
				komisi.setCreate_date_kud(sysdate);
				komisi.setLus_id(lus_id);
				komisi.setMsbi_tahun_ke(1);
				komisi.setMsbi_premi_ke(1);
				if(region.substring(2, 4).equals("A8")){
					komisi.setPkasih(FormatNumber.round(0.7 * komisi_awal,0)); // 70% komisi
					komisi.setPakm(FormatNumber.round(0.35 * komisi_awal,0)); // 35% komisi
					komisi.setJenis_kud(3);
				}else if(region.substring(2, 4).equals("A9")){
					//REQ HIMMIA (FROM TRI HANDINI): AGENCY yg A9 DAN B1 sudah menjadi agency biasa, jadi diccoment saja bagian ini 
//					komisi.setPkasih(0.); 
//					komisi.setPakm(FormatNumber.rounding(0.35 * komisi_awal, false, 25)); 
//					komisi.setJenis_kud(4);
				}else if(region.substring(2, 4).equals("B1")){
					//REQ HIMMIA (FROM TRI HANDINI): AGENCY yg A9 DAN B1 sudah menjadi agency biasa, jadi diccoment saja bagian ini 
//					komisi.setPkasih(0.); 
//					komisi.setPakm(0.); 
//					komisi.setJenis_kud(5);
				}
				if(lev_comm==4){
					this.uwDao.insertMst_diskon_perusahaan(komisi);
				}
			}else{
				Commission komisi = new Commission();
				komisi.setReg_spaj(reg_spaj);
				komisi.setMsco_tax_kud(0.);
				komisi.setPeriode_kud(periode);
				komisi.setCreate_date_kud(sysdate);
				komisi.setLus_id(lus_id);
				komisi.setMsbi_tahun_ke(1);
				komisi.setMsbi_premi_ke(1);
				//selain plan holyland, insert 2 kali ke diskon perusahaan (1 untuk level 2, 1 lagi untuk level 1)
				Map listAgent1 = bacDao.selectAgentBySpajnLevelAgent(reg_spaj,2);
				komisi.setMsco_comm_kud(FormatNumber.round(0.3 * komisi_awal,0)); // 30% komisi tutupan
				komisi.setMcl_id_kud((String) listAgent1.get("MCL_ID"));
				komisi.setJenis_kud(9);
				this.uwDao.insertMst_diskon_perusahaan(komisi);
				Map listAgent2 = bacDao.selectAgentBySpajnLevelAgent(reg_spaj,1);
				komisi.setMsco_comm_kud(FormatNumber.round(0.2 * komisi_awal, 0)); // 20% komisi tutupan
				komisi.setMcl_id_kud((String) listAgent2.get("MCL_ID"));
				komisi.setJenis_kud(10);
				this.uwDao.insertMst_diskon_perusahaan(komisi);
			}
		}
		
		
		/** Bagian ini untuk Ekalink, Ikhlas, dan Sakinah */
		//EKALINK, SAKINAH, SMARTMEDICARE, ADA BONUS PENJUALAN 5%
		if((
			(lsbs.equals("159") || lsbs.equals("160")) && lsdbs.equals("002")) || 
			(lsbs.equals("171") && lsdbs.equals("001") || (lsbs.equals("178")) || (lsbs.equals("208")) ||
			((lsbs.equals("159") && lsdbs.equals("003"))		)
		)) {
			List<Map> billingKUD = null;
			
			Double bonus = FormatNumber.round(0.05 * premi,0); //bonus penjualan 5%
			Date sysdate = commonDao.selectSysdateTruncated(0);
			
			//perhitungan pajak progresif dari TOTAL komisi dan bonus
			Double temp = komisi_awal + bonus;
			Double bonus_tax = f_load_tax(temp, sysdate, msag_id);
			bonus_tax -= tax_awal;
			bonus_tax = FormatNumber.rounding(bonus_tax, false, 25);

//			TAMBAHAN UNTUK EKALINK KUD (KOMISI BONUS 5% MASUK KE KOPERASI)
			if(lsbs.equals("159") && lsdbs.equals("003")){
				if(lsbs.equals("159") && lsdbs.equals("003")){
					billingKUD = uwDao.selectMstBillingKUD(reg_spaj);
				}
				if(billingKUD!=null){
					Map dataKUD = new HashMap();
					for(int i =0; i<billingKUD.size();i++){
						dataKUD = billingKUD.get(i);
						Double premiKUD = ((BigDecimal) dataKUD.get("MSDB_PREMIUM")).doubleValue();
						Integer msbi_premi_ke = ((BigDecimal) dataKUD.get("MSBI_PREMI_KE")).intValue();
						Integer msbi_tahun_ke = ((BigDecimal) dataKUD.get("MSBI_TAHUN_KE")).intValue();
						bonus = FormatNumber.round(0.05 * premiKUD,0);
						
						DateFormat df3 = new SimpleDateFormat("yyyyMM");
						String periode = df3.format(sysdate);
						String mcl_id_kud = uwDao.selectMclIDPemegangPolis(reg_spaj);
						
						Commission komisi = new Commission();
						komisi.setReg_spaj(reg_spaj);
						komisi.setMsco_comm_kud(bonus);
						//komisi.setMsco_tax_kud(bonus_tax);
						komisi.setMsco_tax_kud(0.);
						komisi.setPeriode_kud(periode);
						komisi.setJenis_kud(2);
						komisi.setMcl_id_kud(mcl_id_kud);
						komisi.setCreate_date_kud(sysdate);
						komisi.setLus_id(lus_id);
						komisi.setMsbi_tahun_ke(msbi_tahun_ke);
						komisi.setMsbi_premi_ke(msbi_premi_ke);
						
						if(lev_comm==4){
							this.uwDao.insertMst_diskon_perusahaan(komisi);
						}
						
					}
				}
				
			}else{
				Map infoBonus = uwDao.selectBonusAgen(msag_id);
				if(bonus > 0){
					this.uwDao.insertMst_comm_bonus( //tipe = 1 (bonus penjualan)
							reg_spaj, 1, 1, 1, msag_id, 
							(String) infoBonus.get("MCL_FIRST"), (String) infoBonus.get("MSAG_TABUNGAN"), 
							infoBonus.get("LBN_ID").toString(), msco_id, bonus, bonus_tax, kurs, lus_id, commonDao.selectSysdateTruncated(0), lsbs_linebus);
				}
			}
			//Double bonus = FormatNumber.rounding(0.05 * premi, false, 25); //bonus penjualan 5%
			//Date sysdate = commonDao.selectSysdateTruncated(0);

			//Double bonus_tax = hitungPajakKomisi(bonus, sysdate, msag_id);
			//bonus_tax = FormatNumber.rounding(bonus_tax, false, 25);
			
		}
		
		//IKHLAS DAN SAKINAH, ADA LAGI BONUS TABUNGAN
		if(lsbs.equals("170") || (lsbs.equals("171") && lsdbs.equals("001"))){
			Double bonus = (double) 35000; //bonus penjualan Rp. 35 ribu
			Date sysdate = commonDao.selectSysdateTruncated(0);

			Double bonus_tax = f_load_tax(bonus, sysdate, msag_id);
			bonus_tax = FormatNumber.rounding(bonus_tax, false, 25);
			
			Map infoBonus = uwDao.selectBonusAgen(msag_id);
			this.uwDao.insertMst_comm_bonus( //tipe = 2 (bonus tabungan)
					reg_spaj, 1, 1, 2, msag_id, 
					(String) infoBonus.get("MCL_FIRST"), (String) infoBonus.get("MSAG_TABUNGAN"), 
					infoBonus.get("LBN_ID").toString(), msco_id, bonus, bonus_tax, kurs, lus_id, commonDao.selectSysdateTruncated(0), lsbs_linebus);
		}
		
		/** Bagian ini untuk Hidup Bahagia */
		if(lsbs.equals("167")){
			
			int lsco_jenis = 1;
			if("37,52".indexOf(lca)>-1){ 
				lsco_jenis = 3; //Commission agency system
			}else if(lca.equals("46")) {
				lsco_jenis = 4; //Commission Arthamas
			}
			Double persen = uwDao.selectBonus(lsco_jenis, 4, 1, Integer.valueOf(lsbs), Integer.valueOf(lsdbs));
			
			if(persen == null) throw new RuntimeException("Bonus Hidup Bahagia ERROR");
			
			Double bonus = FormatNumber.round(persen/100 * premi,0); //bonus penjualan
			Date sysdate = commonDao.selectSysdateTruncated(0);

			//perhitungan pajak progresif dari TOTAL komisi dan bonus
			Double temp = komisi_awal + bonus;
			Double bonus_tax = f_load_tax(temp, sysdate, msag_id);
			bonus_tax -= tax_awal;
			bonus_tax = FormatNumber.rounding(bonus_tax, false, 25);

			//Double bonus_tax = hitungPajakKomisi(bonus, sysdate, msag_id);
			//bonus_tax = FormatNumber.rounding(bonus_tax, false, 25);
			
			Map infoBonus = uwDao.selectBonusAgen(msag_id);
			if(bonus > 0){
				this.uwDao.insertMst_comm_bonus( //tipe = 1 (bonus penjualan)
						reg_spaj, 1, 1, 1, msag_id, 
						(String) infoBonus.get("MCL_FIRST"), (String) infoBonus.get("MSAG_TABUNGAN"), 
						infoBonus.get("LBN_ID").toString(), msco_id, bonus, bonus_tax, kurs, lus_id, commonDao.selectSysdateTruncated(0), lsbs_linebus);
			}
		}
		
		/** Bagian ini untuk Powersave dan Stable Link */
		
//		//fungsi ini hanya untuk powersave dan stablelink
//		if(!products.powerSave(lsbs) && !products.stableLink(lsbs) && !products.stableSave(Integer.parseInt(lsbs), Integer.parseInt(lsdbs))) {
//			if(lsbs.equals("73") && lsdbs.equals("008")){
//				if(lev_comm!=2){
//					return;
//				}
//			}else{
//				return;
//			}
//			
//	}		
		if(!products.powerSave(lsbs) && !products.stableLink(lsbs) && !products.stableSave(Integer.parseInt(lsbs), Integer.parseInt(lsdbs)) && (!lsbs.equals("190"))  && (!lsbs.equals("200")) ) {
			if(products.productChannel88(Integer.parseInt(lsbs), Integer.parseInt(lsdbs)) ) {
				if(lev_comm!=2){
					return;
				}
			}else{
				return;
			}
			
		}		
			
		Map infoBonus 	= uwDao.selectBonusAgen(msag_id);
		Date sysdate 	= commonDao.selectSysdateTruncated(0);
		double persen	= 0;
		Integer mgi 	= uwDao.selectMasaGaransiInvestasi(reg_spaj, 1, 1);
		Integer flag_bulanan = financeDao.selectFlagBulananStableLinkStableSave(reg_spaj);

		//BONUS PENJUALAN UNTUK STABLE LINK, STABLE LINK SYARIAH
		if(products.stableLink(lsbs) & !products.progressiveLink(lsbs)){
			//untuk stable link, dari distribusi manapun (individu, agency, worksite) sama semua nilainya
			//double totalPremi = uwDao.selectTotalPremiNewBusiness(reg_spaj);
			double totalPremi = uwDao.selectTotalPremiStableLink(reg_spaj);
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
				if(bulanProd>=201105){
					persen = 0.0;
				}
			}
			
		//BONUS PENJUALAN UNTUK KELUARGA PRODUK POWERSAVE
		}else{
		
			//PROSES KOMISI AGENCY
			if("agency".equals(distribusi)){
				if(products.stableSave(Integer.parseInt(lsbs), Integer.parseInt(lsdbs))) {
					if(flag_bulanan==1){
						if(	(lku_id.equals("01") && premi >= 500000000) || (lku_id.equals("02") && premi >= 50000)) {
//								if(mgi==3) persen = 0.07;
//								else if(mgi==6) persen = 0.14;
//								else if(mgi==12) persen = 0.28;
								persen = 1.17;
						}else{
							if(	(lku_id.equals("01") && premi < 500000000) || (lku_id.equals("02") && premi < 50000)) {
//								if(mgi==3) persen = 0.07;
//								else if(mgi==6) persen = 0.14;
//								else if(mgi==12) persen = 0.28;
								persen =0.82;
							}
						}
					}else{
						if(	(lku_id.equals("01") && premi >= 100000000) || (lku_id.equals("02") && premi >= 10000)) {
//							if(mgi==3) persen = 0.07;
//							else if(mgi==6) persen = 0.14;
//							else if(mgi==12) persen = 0.28;
							persen = 1.17;
						}
					}
					
				}//POWERSAVE AS			
				else if(lsbs.equals("144")) { 
					if((lku_id.equals("01") && premi >= 100000000) || (lku_id.equals("02") && premi >= 10000)) {
						if(mgi==3) persen = 0.1;
						else if(mgi==6) persen = 0.2;
						else if(mgi==12) persen = 0.4;
						else if(mgi==36) persen = 0.7;
					}
					//SK. Direksi No.079/AJS-SK/VII/2009 tidak ada bonus untuk powersave
					if("001".contains(lsdbs)){
						persen = 0.0;
					}
					// 027/IM-DIR/IV/2011 Tertanggal 11 April 2011 perubahan komisi untuk powersave dan stable link.
					if(bulanProd>=201105){
						persen = 0.0;
					}
				//POWERSAVE BULANAN WORKSITE
				}else if(lsbs.equals("158") && Integer.parseInt(lsdbs) < 5) { 
					// SK 083/AJS-SK/VII/2009 tidak ada bonus komisi
//					if((lku_id.equals("01") && premi >= 500000000) || (lku_id.equals("02") && premi >= 50000)) {
//						if(mgi==3) persen = 0.03;
//						else if(mgi==6) persen = 0.06;
//						else if(mgi==12) persen = 0.12;
//						else if(mgi==36) persen = 0.12;
//					}
//					POWERSAVE SYARIAH
				}else if(lsbs.equals("175")) {
					// SK 125/AJS-SK/X/2009 tidak ada bonus komisi
//					if((lku_id.equals("01") && premi >= 100000000) || (lku_id.equals("02") && premi >= 10000)) {
//						if(mgi==3) persen = 0.1;
//						else if(mgi==6) persen = 0.2;
//						else if(mgi==12) persen = 0.4;
//						else if(mgi==36) persen = 0.7;
//					}
				
				//POWERSAVE BULANAN SYARIAH
				}else if(lsbs.equals("176")) {
					// SK 125/AJS-SK/X/2009 tidak ada bonus komisi
//					if((lku_id.equals("01") && premi >= 500000000) || (lku_id.equals("02") && premi >= 50000)) {
//						if(mgi==3) persen = 0.03;
//						else if(mgi==6) persen = 0.06;
//						else if(mgi==12) persen = 0.12;
//						else if(mgi==36) persen = 0.12;
//					}
				}else if (products.productChannel88(Integer.parseInt(lsbs), Integer.parseInt(lsdbs)) ) {
					persen = 40;
				}else if((lsbs.equals("190") && (lsdbs.equals("004") || lsdbs.equals("009"))) || (lsbs.equals("200") && ( lsdbs.equals("004") || lsdbs.equals("007")))  ) {
					  persen=5;
				}
				
				//STABLE SAVE PREMI BULANAN
				//(Deddy) - Bonus komisi tidak ada sesuai SK.Direksi No. 087/AJS-SK/VII/2009
//				else if(products.stableSavePremiBulanan(lsbs)) {
//					if(	(lku_id.equals("01") && premi >= 100000000) || (lku_id.equals("02") && premi >= 10000)) {
//						persen = 0.033; //karena mgi nya cuman 1 bulan aja
//					}
//				}
			
			//PROSES KOMISI INDIVIDU YG LAMA (HANYA UNTUK WORKSITE)
			}else if("worksite".equals(distribusi)){
				if(products.stableSave(Integer.parseInt(lsbs), Integer.parseInt(lsdbs))) {
					if(flag_bulanan==1){
						if(	(lku_id.equals("01") && premi >= 500000000) || (lku_id.equals("02") && premi >= 50000)) {
//								if(mgi==3) persen = 0.07;
//								else if(mgi==6) persen = 0.14;
//								else if(mgi==12) persen = 0.28;
								persen = 1.17;
						}else{
							if(	(lku_id.equals("01") && premi < 500000000) || (lku_id.equals("02") && premi < 10000)) {
//								if(mgi==3) persen = 0.07;
//								else if(mgi==6) persen = 0.14;
//								else if(mgi==12) persen = 0.28;
								persen =0.82;
							}
						}
					}else{
						if(	(lku_id.equals("01") && premi >= 100000000) || (lku_id.equals("02") && premi >= 10000)) {
//							if(mgi==3) persen = 0.07;
//							else if(mgi==6) persen = 0.14;
//							else if(mgi==12) persen = 0.28;
							persen = 1.17;
						}
					}
					
				}
				//POWERSAVE INDIVIDU WORKSITE
//				else if(lsbs.equals("143") && lsdbs.equals("002")) {
//					if(mgi==3) persen = 0.1;
//					else if(mgi==6) persen = 0.2;
//					else if(mgi==12) persen = 0.4;
//					else if(mgi==36) persen = 0.7;
//					else persen = 0;
//				//POWERSAVE BULANAN YANG WORKSITE & BUKAN BANK Sinarmas (YUSUF - 25/08/2006)
//				}
				else if(lsbs.equals("158") && Integer.parseInt(lsdbs) < 5 && region.startsWith("42")) {
					// SK 083/AJS-SK/VII/2009 tidak ada bonus komisi
//					if((lku_id.equals("01") && premi >= 500000000) || (lku_id.equals("02") && premi >= 50000)) {
//						if(mgi==3) persen = 0.03;
//						else if(mgi==6) persen = 0.06;
//						else if(mgi==12) persen = 0.12;
//						else if(mgi==36) persen = 0.12;
//						else persen = 0;
//					}
				
				//STABLE SAVE PREMI BULANAN
				}//(Deddy) - Bonus komisi tidak ada sesuai SK.Direksi No. 087/AJS-SK/VII/2009
//				else if(products.stableSavePremiBulanan(lsbs)) {
//					if(	(lku_id.equals("01") && premi >= 100000000) || (lku_id.equals("02") && premi >= 10000)) {
//						persen = 0.033; //karena mgi nya cuman 1 bulan aja
//					}
//				}
				
			//PROSES KOMISI INDIVIDU 2007
			}else if("individu".equals(distribusi)){
				if(products.stableSave(Integer.parseInt(lsbs), Integer.parseInt(lsdbs))) {
					if(flag_bulanan==1){
						if(	(lku_id.equals("01") && premi >= 500000000) || (lku_id.equals("02") && premi >= 50000)) {
//								if(mgi==3) persen = 0.07;
//								else if(mgi==6) persen = 0.14;
//								else if(mgi==12) persen = 0.28;
								persen = 1.17;
						}else{
							if(	(lku_id.equals("01") && premi < 500000000) || (lku_id.equals("02") && premi < 10000)) {
//								if(mgi==3) persen = 0.07;
//								else if(mgi==6) persen = 0.14;
//								else if(mgi==12) persen = 0.28;
								persen =0.82;
							}
						}
					}else{
						if(	(lku_id.equals("01") && premi >= 100000000) || (lku_id.equals("02") && premi >= 10000)) {
//							if(mgi==3) persen = 0.07;
//							else if(mgi==6) persen = 0.14;
//							else if(mgi==12) persen = 0.28;
							persen = 1.17;
						}
					}
				}
				//POWERSAVE INDIVIDU
				else if(lsbs.equals("143") ) {
					if("001, 002, 003".contains(lsdbs)){
//					if((lku_id.equals("01") && premi >= 100000000) || (lku_id.equals("02") && premi >= 10000)){ 
//						if(mgi==3) persen = 0.1;
//						else if(mgi==6) persen = 0.2;
//						else if(mgi==12) persen = 0.4;
//						else if(mgi==36) persen = 0.7;
//					}
//					SK. Direksi No.079/AJS-SK/VII/2009 tidak ada bonus untuk powersave
						persen = 0.0;
					}else {
						if(	(lku_id.equals("01") && premi >= 100000000) || (lku_id.equals("02") && premi >= 10000)) {
							persen = 1.17;
						}
					}
					
				//POWERSAVE BULANAN YANG WORKSITE & BUKAN BANK Sinarmas
				}else if(lsbs.equals("158") && Integer.parseInt(lsdbs) < 5 && region.startsWith("42")) {
					// SK 083/AJS-SK/VII/2009 tidak ada bonus komisi
//					if((lku_id.equals("01") && premi >= 500000000) || (lku_id.equals("02") && premi >= 50000)) {
//						if(mgi==3) premi = 0.03;
//						else if(mgi==6) premi = 0.06;
//						else if(mgi==12) premi = 0.12;
//						else if(mgi==36) premi = 0.12;
//					}
				//POWERSAVE BULANAN YANG BUKAN FINANCIAL PLANNER & BUKAN BANK Sinarmas
				}else if(lsbs.equals("158") && Integer.parseInt(lsdbs) < 5 && !region.startsWith("0916")) {
					// SK 083/AJS-SK/VII/2009 tidak ada bonus komisi
//					if((lku_id.equals("01") && premi >= 500000000) || (lku_id.equals("02") && premi >= 50000)) {
//						if(mgi==3) persen = 0.03;
//						else if(mgi==6) persen = 0.06;
//						else if(mgi==12) persen = 0.12;
//						else if(mgi==36) persen = 0.12;
//					}
				//POWERSAVE SYARIAH
				}else if(lsbs.equals("175")) {
					// SK 125/AJS-SK/X/2009 tidak ada bonus komisi
//					if((lku_id.equals("01") && premi >= 100000000) || (lku_id.equals("02") && premi >= 10000)) {
//						if(mgi==3) persen = 0.1;
//						else if(mgi==6) persen = 0.2;
//						else if(mgi==12) persen = 0.4;
//						else if(mgi==36) persen = 0.7;
//					}
				//POWERSAVE BULANAN SYARIAH 
				}else if(lsbs.equals("176")) {
					if((lku_id.equals("01") && premi >= 500000000) || (lku_id.equals("02") && premi >= 50000)) {
						if(mgi==3) persen = 0.03;
						else if(mgi==6) persen = 0.06;
						else if(mgi==12) persen = 0.12;
						else if(mgi==36) persen = 1.12;
					}
				}else if((lsbs.equals("190") && (lsdbs.equals("004") || lsdbs.equals("009"))) || (lsbs.equals("200") && ( lsdbs.equals("004") ||lsdbs.equals("007")))  ) {
					  persen=5;
				}
				//STABLE SAVE PREMI BULANAN
				//(Deddy) - Bonus komisi tidak ada sesuai SK.Direksi No. 087/AJS-SK/VII/2009
//				else if(products.stableSavePremiBulanan(lsbs)) {
//					if(	(lku_id.equals("01") && premi >= 100000000) || (lku_id.equals("02") && premi >= 10000)) {
//						persen = 0.033; //karena mgi nya cuman 1 bulan aja
//					}
//				}
			}
		}
		double bonus = FormatNumber.round(persen/100 * premi,0);
		//
		if(("crossselling").equals(distribusi)){
			if(lca.equals("55")){
				DateFormat df3 = new SimpleDateFormat("yyyyMM");
				String periode = df3.format(sysdate);
				String mcl_id_kud = uwDao.selectMclIDPemegangPolis(reg_spaj);
				Commission komisi = new Commission();
				komisi.setMsco_comm_kud(bonus);
				komisi.setMsco_comm_kud(FormatNumber.round(bonus,0));
				komisi.setReg_spaj(reg_spaj);
				//komisi.setMsco_tax_kud(bonus_tax);
				komisi.setMsco_tax_kud(0.);
				komisi.setPeriode_kud(periode);
				komisi.setMcl_id_kud(mcl_id_kud);
				komisi.setCreate_date_kud(sysdate);
				komisi.setLus_id(lus_id);
				komisi.setMsbi_tahun_ke(1);
				komisi.setMsbi_premi_ke(1);
				komisi.setJenis_kud(6);
				
				if(lev_comm==4){
					this.uwDao.insertMst_diskon_perusahaan(komisi);
				}
			}
		}else{
			

			//perhitungan pajak progresif dari TOTAL komisi dan bonus
			Double temp = komisi_awal + bonus;
			Double bonus_tax = f_load_tax(temp, sysdate, msag_id);
			bonus_tax -= tax_awal;
			bonus_tax = FormatNumber.rounding(bonus_tax, false, 25);
			
			if(lsbs.equals("159") && lsdbs.equals("003")){
				
			}else{
				if(bonus > 0){
					//TIPE
					//1 = bonus unit link, powersave, stable link
					//2 = bonus tabungan
					this.uwDao.insertMst_comm_bonus(
							reg_spaj, tahun_ke, premi_ke, 1, msag_id, 
							(String) infoBonus.get("MCL_FIRST"), (String) infoBonus.get("MSAG_TABUNGAN"), 
							infoBonus.get("LBN_ID").toString(), msco_id, bonus, bonus_tax, kurs, lus_id, commonDao.selectSysdateTruncated(0), lsbs_linebus);
				}
			}
		}
		
		
		
	}
	
	public boolean prosesKomisiArco(String spaj, User currentUser, BindException errors, int premiKe) throws Exception{
		double kurs = 1;
		List lds_kom = this.uwDao.selectViewKomisiAsli(spaj);
		List detBisnis = uwDao.selectDetailBisnis(spaj);
		int faktor=1;
		String elesbees = (String) ((Map) detBisnis.get(0)).get("BISNIS");
		String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
		if(!lds_kom.isEmpty() || (lds_kom.isEmpty() ) ) {
			//CEK DULU PAKE ROW PERTAMA, KALO KURS DOLLAR, ADA KURSNYA GAK
				Date sysdate = commonDao.selectSysdateTruncated(0);
				DateFormat df3 = new SimpleDateFormat("yyyyMM");
				String periode = df3.format(sysdate);
				
			for(int i=0; i<lds_kom.size(); i++) {
					Commission komisi = null;
					if(!lds_kom.isEmpty()){
						komisi = (Commission) lds_kom.get(i);
					}
					String mcl_id_kud = uwDao.selectMclIDPemegangPolis(spaj);
					komisi.setFlag_mess(Boolean.TRUE);
					if(premiKe != 1) {
						komisi.setPremi(uwDao.selectPremiTopUpUnitLink(spaj, premiKe));
					}
					double premi_asli = komisi.getPremi();
					komisi.setPremi(FormatNumber.round(komisi.getPremi() * kurs, 2));
					int li_cb_id = komisi.getCb_id();
					if (li_cb_id ==3){
						faktor =1;
					}else if (li_cb_id ==2){
						faktor =2;
					}else if (li_cb_id ==1){
						faktor =4;
					}else if (li_cb_id ==6){
						faktor=12;
					}
					//Commission Arco ke mst_diskon_perusahaan
					komisi.setLsco_jenis(8);
					//ambil lama tanggung
					getLamaTanggung(komisi);
					int tahunKe = 1;
				if(premiKe > 1) tahunKe = 0;
				if(komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){	
						if(f_get_komisi(komisi, errors, tahunKe, premiKe, komisi.getKurs_id(), komisi.getPremi(), 4)) {
							komisi.setMsco_comm_kud(komisi.getPremi() * faktor * komisi.getKomisi() / 100);
							komisi.setMsco_comm_kud(FormatNumber.round(komisi.getMsco_comm_kud(),0));
							komisi.setReg_spaj(spaj);
							//komisi.setMsco_tax_kud(bonus_tax);
							komisi.setMsco_tax_kud(0.);
							komisi.setPeriode_kud(periode);
							komisi.setMcl_id_kud(mcl_id_kud);
							komisi.setCreate_date_kud(sysdate);
						
							komisi.setLus_id(currentUser.getLus_id());
							komisi.setMsbi_tahun_ke(1);
							komisi.setMsbi_premi_ke(1);
							komisi.setJenis_kud(8);//khusus SMiLe Medical ARCO
							
							if(komisi.getLev_kom()==4){
								this.uwDao.insertMst_diskon_perusahaan(komisi);
							}
						
					}
				}
			  }
		}
		return true;		
	}
	
	public void prosesRewardAgencySystem(String spaj, User currentUser, BindException errors, String lca_id) throws Exception{ //of_komisi_asys()
		if(logger.isDebugEnabled())logger.debug("PROSES: prosesKomisiAgencySystem (of_komisi_asys)");
		StringBuffer emailMessage = new StringBuffer();
		emailMessage.append("[SPAJ  ] " + spaj + "\n");
		NumberFormat nf = NumberFormat.getInstance();
		
		boolean lb_deduct=false;
		
		int flag_telemarket = uwDao.selectgetFlagTelemarketing(spaj);
		
		String businessId = this.uwDao.selectBusinessId(spaj);
		Class.forName("produk_asuransi.n_prod_"+businessId).newInstance();
		businessId = FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 3);
		
		double ldec_kurs; 
		Map pribadi = this.uwDao.selectInfoPribadi(spaj);
		String ls_region = (String) pribadi.get("REGION");
		String prodDate = (String) pribadi.get("PROD_DATE"); //yyyy (tahun produksi)
		emailMessage.append("[PRODUCTION DATE] " + pribadi.get("MSPRO_PROD_DATE") + "\n");
		int bulanProd = Integer.parseInt(uwDao.selectBulanProduksi(spaj));
		
		//prodDate = "2009";
		
		List lds_prod = null;
		List lds_kom = null;
		
		//Yusuf - 5/1/09 - Mulai 2009, struktur komisi arthamas ada 6 tingkat, bukan 4 lagi
		if(Integer.parseInt(prodDate) > 2008 && lca_id.equals("46")) {
			lds_kom = this.uwDao.selectViewKomisiHybrid2009(spaj);
			lds_prod = this.uwDao.selectAgentsFromSpajHybrid2009(spaj);
		}else {
			lds_kom = this.uwDao.selectViewKomisiAsli(spaj);
			lds_prod = this.uwDao.selectAgentsFromSpaj(spaj);
		}
		
		if(!lds_kom.isEmpty()) {
			Commission kms = (Commission) lds_kom.get(0);
			String[] to = new String[] {"Karunia@sinarmasmsiglife.co.id;jelita@sinarmasmsiglife.co.id","Iriana@sinarmasmsiglife.co.id"};
			String[] cc = new String[] {"tri.handini@sinarmasmsiglife.co.id"};
			if( lca_id.indexOf("08,42,62,68") >=0){//request Wesni, apabila worksite
				to = new String[] {"wesni@sinarmasmsiglife.co.id"};
				cc= null;
			}
			
			if(kms.getLev_kom()==3){
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, new Date(), null, 
						false,props.getProperty("admin.ajsjava"),to,cc ,new String[] {"deddy@sinarmasmsiglife.co.id"},
						"[System Warning] Pengecekan level Agent untuk SPAJ " + spaj, 
						"Kepada Yth.\n"+
						"Bagian Agency di tempat.\n\n"+
						"Mohon dilakukan pengecekan pada SPAJ ini, karena untuk Komisi Utamanya tidak diberikan ke agent penutup langsung. Namun diberikan kepada kode Agent Leadernya yakni :"+kms.getAgent_id()+
						"\n\nTerima kasih.",  null, spaj);
			}
						
			if(kms.getKurs_id().equals("02")){
				ldec_kurs = this.uwDao.selectLastCurrency(spaj, 1, 1);
				if(ldec_kurs<0){
					errors.reject("payment.noLastCurrency");
					return;
				}
			}else ldec_kurs = 1;

			String ls_agent[] = new String[4];
			
			//ini rate OR untuk 37 (agency)
			int li_or[] = {0, 40, 30, 20};
			//ini rate OR untuk 46 (arthamas)
			if(lca_id.equals("46")) {
				li_or = new int[]{0, 35, 25, 20};
			}
			//ini rate OR untuk SURYAMAS AGUNG AGENCY
			if(kms.getBancass().equals("37B9")){
				li_or = new int[]{0, 40, 0, 120};
			}

			int li_persen[] = new int[4];
			int maxCount = 4;
			
			if(lds_kom.size()>4){
				ls_agent = new String[5];
				li_persen = new int[5];
			}
			
			//Yusuf - 5/1/09 - Mulai 2009, struktur komisi arthamas ada 6 tingkat, bukan 4 lagi
			if(Integer.parseInt(prodDate) > 2008 && lca_id.equals("46")) {
				ls_agent = new String[6];
				li_or = new int[]{0, 35, 25, 20, 5, 1}; //FC - SM - BM - DM - RM - RD
				li_persen = new int[6];
				maxCount = 6;
			}			
			
			int k=2;
			int ll_find;
			boolean ketemu = false;
			
			for(int i=1; i<lds_kom.size(); i++){
				Commission temp = (Commission) lds_kom.get(i);
				ls_agent[i] = temp.getAgent_id();
				li_persen[i] = 0;
				ll_find = 0;
				do{
					ketemu=false;
					for(int j = ll_find; j<lds_prod.size(); j++){
						String msag_id = ((HashMap) lds_prod.get(j)).get("MSAG_ID").toString(); 
						if(msag_id.equals(ls_agent[i])){ 
							ll_find=j; ketemu=true; break; 
						}
					}
					if(ketemu){
						int lsle_id = ((BigDecimal) ((HashMap) lds_prod.get((int)ll_find)).get("LSLE_ID")).intValue(); 
						if(lsle_id != maxCount){
							li_persen[i] += li_or[k-1];
							k++;
						}
					}
					ll_find++;
				}while(ll_find <= maxCount);
				
				
			}			
			
			double ldec_premi = 0;
			double persen = 0;
			Double ldec_komisi = null;
			
			for(int i=0; i<lds_kom.size(); i++) {
				/** AWAL DARI LOOPING KOMISI **/
				Commission komisi = (Commission) lds_kom.get(i);
				komisi.setLsbs_linebus(bacDao.selectLineBusLstBisnis(komisi.getBisnis_id().toString()));
				komisi.setFlag_mess(Boolean.TRUE);
				if("183,189,193,195,201,204".indexOf(komisi.getBisnis_id().toString()) >-1){
					komisi.setPremi(bacDao.selectTotalKomisiEkaSehat(spaj));
				}
				if(i==0) ldec_premi = FormatNumber.round(komisi.getPremi() * ldec_kurs, 2);
				int li_cb_id = komisi.getCb_id();
				int li_cb_id_asli = komisi.getCb_asli();
				if(!products.progressiveLink(komisi.getBisnis_id().toString()) && !products.stableSavePremiBulanan(komisi.getBisnis_id().toString()) && !products.healthProductStandAlone(komisi.getBisnis_id().toString())){
					if( li_cb_id==1 || li_cb_id==2 || li_cb_id==6 ) komisi.setCb_id(3);
				}
				komisi.setCb_id(new Integer(li_cb_id));
				if(komisi.getBisnis_id() == 64 ) komisi.setBisnis_id(new Integer(56));
				else if(komisi.getBisnis_id() == 67 ) komisi.setBisnis_id(new Integer(54));
				
				if("37,52".indexOf(lca_id)>-1) { //Agency
					komisi.setLsco_jenis(new Integer(3)); //Commission agency system
				}else if(lca_id.equals("46")) {
					komisi.setLsco_jenis(new Integer(4)); //Commission Arthamas
				}
				
				getLamaTanggung(komisi);
				
				if(f_get_komisi(komisi, errors, 1, 1, komisi.getKurs_id(), komisi.getPremi(), maxCount)==true){
					//Double jmlKom = komisi.getKomisi()==null? 0 : komisi.getKomisi();
//					if(jmlKom==0){
//						if(!(komisi.getLev_kom()==4 && komisi.getBisnis_id()==73)){
//							continue;
//						}
//					}
					int li_dplk=0;
					int li_club=0;
	
					Map prestige = this.uwDao.selectPrestigeClub(komisi.getAgent_id(), komisi.getTgl_kom());
					if(prestige!=null){
						li_club = Integer.parseInt(prestige.get("MSAC_AKTIF").toString());
						li_dplk = Integer.parseInt(prestige.get("MSAC_DPLK").toString());
					}
					
					if(komisi.getLev_kom().intValue() == maxCount){
						
						String kurs = komisi.getKurs_id();
						
						if(products.powerSave(komisi.getBisnis_id().toString())){
							int mgi = this.uwDao.selectMgiNewBusiness(spaj);
							TopUp t = (TopUp) this.uwDao.selectTopUp(spaj, 1, 1, "desc").get(0);
							Date ldt_tgl_rk = t.getMspa_date_book();
							if(ldt_tgl_rk.compareTo(defaultDateFormatReversed.parse("20111116"))< 0 ){//request TRI, berdasarkan tgl RK apabila tgl RK di bawah tgl 16 nov 2011, ambil komisi lama
								komisi.setTgl_kom(ldt_tgl_rk);
							}
							Map komisiPowersave = uwDao.selectKomisiPowersave(1, 
									komisi.getBisnis_id(), komisi.getBisnis_no(), 
									mgi, komisi.getKurs_id(), komisi.getLev_kom(), 1, ldec_premi, 0, komisi.getTgl_kom());

							if(komisiPowersave == null) {
								errors.reject("payment.noCommission", 
										new Object[]{
										   defaultDateFormat.format(komisi.getTgl_kom()),
										   komisi.getBisnis_id()+"-"+komisi.getBisnis_no(),
										   komisi.getCb_id(), 
										   komisi.getIns_period(), 
										   komisi.getKurs_id(), 
										   komisi.getLev_kom(),
										   komisi.getTh_kom(),
										   komisi.getLsco_jenis()
								           }, 
										"Commission POWERSAVE Tidak Ada.<br>- Tanggal: {0}<br>-Kode Bisnis: {1}<br>-Cara Bayar: {2}<br>-Ins_per: {3}<br>-Kurs: {4}<br>-Level: {5}<br>-Tahun Ke{6}<br>-Jenis: {7}");
								throw new Exception(errors);
							}else {
								if(komisiPowersave.get("LSCP_COMM")!=null) komisi.setKomisi(Double.valueOf(komisiPowersave.get("LSCP_COMM").toString()));
								if(komisiPowersave.get("LSCP_BONUS")!=null) komisi.setBonus(Double.valueOf(komisiPowersave.get("LSCP_BONUS").toString()));
							}
						}else{
							//Deddy - semua stable save, komisi diset 0.58
							if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())){
								komisi.setKomisi(0.58);
							}
							//POWERSAVE AS
							else if(komisi.getBisnis_id()==144) {
								int li_jwaktu = this.uwDao.selectMgiNewBusiness(spaj);
								//if(li_jwaktu==1) komisi.setKomisi(new Double(0.02)); hanya saat rollover, bukan new business
								if(li_jwaktu==3) komisi.setKomisi(0.05);
								else if(li_jwaktu==6) komisi.setKomisi(0.1);
								else if(li_jwaktu==12) komisi.setKomisi(0.2);
								else if(li_jwaktu==36) komisi.setKomisi(0.6);
								
//								(Deddy 28/7/2009) SK Direksi No.079/AJS-SK/VII/2009
								if(komisi.getBisnis_no()==1){
									if(li_jwaktu==3) komisi.setKomisi(0.063);
									else if(li_jwaktu==6) komisi.setKomisi(0.125);
									else if(li_jwaktu==12) komisi.setKomisi(0.25);
									else if(li_jwaktu==36) komisi.setKomisi(0.75);
									// 027/IM-DIR/IV/2011 Tertanggal 11 April 2011 perubahan komisi untuk powersave dan stable link.
									if(bulanProd >= 201105){
										if(li_jwaktu==3) komisi.setKomisi(0.15);
										else if(li_jwaktu==6) komisi.setKomisi(0.3);
										else if(li_jwaktu==12) komisi.setKomisi(0.6);
										else if(li_jwaktu==36) komisi.setKomisi(1.35);
									}
								}
								else if(komisi.getBisnis_no()==4){//stable save
									komisi.setKomisi(0.58);
								}else komisi.setKomisi(0.0);
								
								
							//POWERSAVE BULANAN YANG WORKSITE & BUKAN BANK Sinarmas (YUSUF - 25/08/2006) 
							}else if(komisi.getBisnis_id()==158 && komisi.getBisnis_no() < 5 && ls_region.startsWith("42")) {
								int li_jwaktu = uwDao.selectMgiNewBusiness(spaj);
								double ldec_temp = komisi.getPremi();
								if(li_jwaktu==3) komisi.setKomisi(0.12);
								else if(li_jwaktu==6) komisi.setKomisi(0.24);
								else if(li_jwaktu==12) komisi.setKomisi(0.48);
								else if(li_jwaktu==36) komisi.setKomisi(1.18);
								else komisi.setKomisi(0.0);
								
							//POWERSAVE BULANAN YANG BUKAN FINANCIAL PLANNER & BUKAN BANK Sinarmas (YUSUF - 25/08/2006)
							}else if(komisi.getBisnis_id()==158 && komisi.getBisnis_no() < 5 && !ls_region.startsWith("0916")) {
								int li_jwaktu = uwDao.selectMgiNewBusiness(spaj);
								double ldec_temp = komisi.getPremi();
								if(li_jwaktu==3) komisi.setKomisi(0.12);
								else if(li_jwaktu==6) komisi.setKomisi(0.24);
								else if(li_jwaktu==12) komisi.setKomisi(0.48);
								else if(li_jwaktu==36) komisi.setKomisi(1.18);
								else komisi.setKomisi(0.0);
								
							//HORISON, KOMISI DIHITUNG DARI (100 - MST_INSURED.MSTE_PCT_DPLK) * MST_PRODUCT_INSURED.MSPR_PREMIUM
							//KARENA MSPR_PREMIUM = TOTAL DARI BIAYA DPLK + PREMI, DAN PERSENTASENYA ADA DI MST_INSURED.MSTE_PCT_DPLK
							//(YUSUF - 22/09/2006)
							}else if(komisi.getBisnis_id()==149) {
								Map dplk = uwDao.selectPersentaseDplk(spaj);
								ldec_premi = (Double) dplk.get("premi");
							}else if(komisi.getBisnis_id()==73 && komisi.getBisnis_no()!=8) {
								//TESTING BUAT LSDBS_NUMBER 8
								//Yusuf (19 Feb 2007) PA Stand Alone
								komisi.setKomisi(12.5);
								
							//POWERSAVE SYARIAH (YUSUF - 28/01/09)
							}else if(komisi.getBisnis_id() == 175) {
								int li_jwaktu = this.uwDao.selectMgiNewBusiness(spaj);

								if(li_jwaktu==3) komisi.setKomisi(0.05);
								else if(li_jwaktu==6) komisi.setKomisi(0.1);
								else if(li_jwaktu==12) komisi.setKomisi(0.2);
								else if(li_jwaktu==36) komisi.setKomisi(0.6);
								else komisi.setKomisi(0.0);

							//POWERSAVE BULANAN SYARIAH (YUSUF - 28/01/09)
							}else if(komisi.getBisnis_id() == 176) {
								int li_jwaktu = this.uwDao.selectMgiNewBusiness(spaj);

								if(li_jwaktu==3) komisi.setKomisi(0.12);
								else if(li_jwaktu==6) komisi.setKomisi(0.24);
								else if(li_jwaktu==12) komisi.setKomisi(0.48);
								else if(li_jwaktu==36) komisi.setKomisi(1.18);
								else komisi.setKomisi(new Double(0));
							
							//STABLE SAVE (YUSUF - 30/01/09)
							}else if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())) {
								if((komisi.getBisnis_id() == 184 && komisi.getBisnis_no().intValue()==2 ) ||
								   (komisi.getBisnis_id() == 158 && komisi.getBisnis_no().intValue()==15) ||
								   (komisi.getBisnis_id() == 143 && komisi.getBisnis_no().intValue()==5)){
									komisi.setKomisi(2.5);//(Deddy - 28/7/2009)berlaku tgl : 1 agustus 2009 berdasarkan SK. Direksi No. 088/AJS-SK/VII/2009
								}else{
									komisi.setKomisi(0.58);
								}

								
							//STABLE SAVE PREMI BULANAN (YUSUF - 31/04/09)
							}else if(products.stableSavePremiBulanan(komisi.getBisnis_id().toString())) {
								int li_jwaktu = this.uwDao.selectMasaGaransiInvestasi(spaj,1,1); // SK.Direksi No. 140/AJS-SK/XII/2009
								
								if(li_jwaktu==3) komisi.setKomisi(0.125);
								else if(li_jwaktu==6) komisi.setKomisi(0.25);
								else if(li_jwaktu==12) komisi.setKomisi(0.5);
								else if(li_jwaktu==1) komisi.setKomisi(0.042);
							}else if(products.progressiveLink(komisi.getBisnis_id().toString()) && ( bulanProd >= 201102 && bulanProd <=201104 ) ){
								
								int li_jwaktu = this.uwDao.selectMasaGaransiInvestasi(spaj,1,1);
								
								if(li_jwaktu==3) komisi.setKomisi(0.126);
								else if(li_jwaktu==6) komisi.setKomisi(0.375);
								else if(li_jwaktu==12) komisi.setKomisi(0.75);
								else if(li_jwaktu==1) komisi.setKomisi(1.5);
							}
						}

					}
					
					/** komisi = persentase komisi * premi **/
					if(pribadi.get("PRIBADI").toString().equals("1")){
						komisi.setKomisi(0.0);
						ldec_komisi = 0.0;
					}else{
						if(i>0) komisi.setKomisi(new Double(li_persen[i]));
						
						if(i>0 && products.stableLink(komisi.getBisnis_id().toString()) && komisi.getBisnis_id() != 186) {
							if(bulanProd >= 200809 && bulanProd <= 200812) {
								komisi.setKomisi(new Double(li_persen[i] * 0.1));
							}else {
								komisi.setKomisi(0.);
							}
						}
						
						if(komisi.getBisnis_id().intValue()==167 && i==0) {
							ldec_komisi = new Double(ldec_premi * (komisi.getKomisi()) / 100);
						}else if(komisi.getBisnis_id().intValue()==96 && komisi.getBisnis_no().intValue()>=13&& i==0){
							ldec_komisi = new Double(ldec_premi * (komisi.getKomisi()) / 100);
						}else if(li_cb_id_asli == 3 && i==0){ //kalau cara bayar tahunan, ada bonusnya
							ldec_komisi = new Double(ldec_premi * (komisi.getKomisi() + komisi.getBonus()) / 100);
						}else{
							ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
						}
						//Eka proteksi sekaligus
						//Jika cara bayar sekaligus (Himmia) 8/11/00
						if((komisi.getBisnis_id()==52 || komisi.getBisnis_id()==51) && komisi.getCb_asli()==0){
							ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
						
						//Khusus eka sarjana mandiri RP 3 & 5
						// Kalo premi RP > 10jt atau prestige club
						}else if(komisi.getBisnis_id()==63 && komisi.getKurs_id().equals("01") && (komisi.getBisnis_no()==2 || komisi.getBisnis_no()==3)){
							if(i==0 && (ldec_premi >=10000000 || li_club==1)){
								//3 tahun
								if(komisi.getBisnis_no()==2){
									komisi.setKomisi(new Double(7.5));
									if(komisi.getCb_asli()==3)komisi.setKomisi(12.5);
								}else{
									//5 tahun
									komisi.setKomisi(new Double(25));
									if(komisi.getCb_asli()==3)komisi.setKomisi(new Double(30));
								}
								ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
							}
						//Khusus eka sarjana mandiri yang baru (173)
						}else if(komisi.getBisnis_id()==173){
														
							if(i==0){
								komisi.setKomisi(5.);
															
								int autodebet = bacDao.selectFlagCC(spaj);
								//bila autodebet payroll
								if(autodebet == 4){
									komisi.setKomisi(7.5);
								}else{									
									if(komisi.getBisnis_no()==1){
										komisi.setKomisi(5.);
									}else if(komisi.getBisnis_no()==2 || komisi.getBisnis_no()==3){
										komisi.setKomisi(10.);
									}
								}
								
								ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
							}
						// Untuk Produk Ekasiswa Emas yang baru (172)
						}else if(komisi.getBisnis_id()==172){
							if(i==0){
								Integer li_age = this.uwDao.selectAgeFromSPAJ(komisi.getReg_spaj());
								//bila cara bayar bulanan
								if(komisi.getCb_asli().intValue() == 6) {
									if(komisi.getKurs_id().equals("01") ){
										if(li_age >=1 && li_age <= 8){
											komisi.setKomisi(new Double(22.5));
										}else if(li_age>=9 && li_age <= 12){
											komisi.setKomisi(new Double(17.5));
										}
									}else if(komisi.getKurs_id().equals("02")){
										if(li_age >=1 && li_age <= 5){
											komisi.setKomisi(new Double(22.5));
										}else if(li_age>=6 && li_age <= 8){
											komisi.setKomisi(new Double(17.5));
										}else if(li_age>=9 && li_age <= 12){
											komisi.setKomisi(new Double(12.5));
										}
									}
									
								//cara bayar lainnya
								}else{
									if(komisi.getKurs_id().equals("01") ){
										if(li_age >=1 && li_age <= 8){
											komisi.setKomisi(new Double(25));
										}else if(li_age>=9 && li_age <= 12){
											komisi.setKomisi(new Double(20));
										}
									}else if(komisi.getKurs_id().equals("02")){
										if(li_age >=1 && li_age <= 5){
											komisi.setKomisi(new Double(25));
										}else if(li_age>=6 && li_age <= 8){
											komisi.setKomisi(new Double(20));
										}else if(li_age>=9 && li_age <= 12){
											komisi.setKomisi(new Double(15));
										}
									}
								}
								ldec_komisi = new Double(ldec_premi * komisi.getKomisi() / 100);
							}
						}else if(komisi.getBisnis_id()==180){//untuk produk prosaver (180)
							if(i==0){
								komisi.setKomisi(5.);
								int autodebet = bacDao.selectFlagCC(spaj);
								//bila autodebet payroll
								if(autodebet == 4){
									if(komisi.getBisnis_no().intValue()==1){
										komisi.setKomisi(17.5);
									}else if(komisi.getBisnis_no().intValue()==2){
										komisi.setKomisi(22.5);
									}else if(komisi.getBisnis_no().intValue()==3){
										komisi.setKomisi(22.5);
									}
								}else{									
									if(komisi.getBisnis_no().intValue()==1){
										komisi.setKomisi(20.0);
									}else if(komisi.getBisnis_no().intValue()==2){
										komisi.setKomisi(25.0);
									}else if(komisi.getBisnis_no().intValue()==3){
										komisi.setKomisi(25.0);
									}
									if(komisi.getCb_asli().intValue()==0){
										komisi.setKomisi(5.0);
									}
								}
							}
						}

					}
					// Khusus plan pa pti
					//if(komisi.getLev_kom()==4 && komisi.getBisnis_id()==73)ldec_komisi = new Double(10000);
					if(ldec_komisi==0 && !pribadi.get("PRIBADI").toString().equals("1"))continue;

					//kalau masih aktif
					if(komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
						if(("183,189,193,201".indexOf(komisi.getBisnis_id().toString()) >-1)){
							komisi.setCo_id(sequenceMst_commission(11));
						}else if("183,189,193,201".indexOf(komisi.getBisnis_id().toString()) <=-1){
							komisi.setCo_id(sequenceMst_commission(11));
						}
						

						//TAMBAHAN OLEH YUSUF UNTUK PRODUK2 SINGLE (IM No. 132/IM-DIR/XII/2007) - 16/01/2008
						ldec_komisi = cekLinkSingle(spaj, ldec_komisi, komisi.getLev_kom(), false);
						
						Date sysdate = commonDao.selectSysdateTruncated(0);
						
						
						if(komisi.getAgent_id().equals("500001")) komisi.setTax(new Double(ldec_komisi * 0.15));
						else {
							komisi.setTax(new Double(ldec_komisi * komisi.getTax() / 100));
							if(komisi.getTax() > 0){
								komisi.setTax(f_load_tax(ldec_komisi, sysdate, komisi.getAgent_id()));
							}
						}

						komisi.setTax(FormatNumber.rounding(komisi.getTax() , true, 25));
						ldec_komisi  = FormatNumber.round(ldec_komisi ,  0);
						if(komisi.getTax()==null)komisi.setTax(new Double(0));
						
						persen = komisi.getKomisi();
						komisi.setKomisi(ldec_komisi);
						komisi.setNilai_kurs(new Double(ldec_kurs));
						komisi.setLus_id(currentUser.getLus_id());
						komisi.setMsbi_tahun_ke(new Integer(1));
						komisi.setMsbi_premi_ke(new Integer(1));
						if(komisi.getLev_kom().intValue() < 4 && (products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no()) || komisi.getBisnis_id()==203)) {
							komisi.setKomisi(0.);
						}
						if(komisi.getKomisi()>0) {

							//Yusuf - 5/1/09 - Mulai 2009, struktur komisi arthamas ada 6 tingkat, bukan 4 lagi, tapi diinsertnya bukan dari 6, tetep dari 4
							if(Integer.parseInt(prodDate) > 2008 && lca_id.equals("46")) {
								if(komisi.getLev_kom().intValue() > 2) komisi.setLev_kom(komisi.getLev_kom().intValue() - 2);
								else komisi.setLev_kom(0);
							}
							
						}
						
						if(i==0){
							Map temp = this.uwDao.selectBillingRemain(komisi.getReg_spaj());
							if(temp != null) {
								double ldec_sisa = Double.parseDouble(temp.get("MSBI_REMAIN").toString());
								int li_flag = Integer.parseInt(temp.get("MSBI_FLAG_SISA").toString());
								if(li_flag==2){
//									this.uwDao.insertMst_deduct(komisi.getCo_id(), ldec_sisa, currentUser.getLus_id(), 1, 3, "PREMI KURANG BAYAR");
									lb_deduct=true;
								}
							}
						}

						//Jika masuk prestige club, pot buat dplk
						if(li_club==1 && li_dplk==1){
							int li_deduct_no = 1;
							if(i==0 && lb_deduct)li_deduct_no=2;
							Double ldec_dplk = Double.valueOf(prestige.get("LPC_DPLK_K").toString());
							ldec_dplk = new Double((ldec_komisi - komisi.getTax()) * ldec_dplk / 100);
							ldec_dplk = FormatNumber.round(ldec_dplk, 0);
//							this.uwDao.insertMst_deduct(komisi.getCo_id(), ldec_dplk, currentUser.getLus_id(), li_deduct_no, 5, "POTONGAN DPLK /U " + prestige.get("LPC_CLUB").toString() );
						}
						
						//(Yusuf - 27/12/2006) untuk agency system, sekarang ada reward 10% -> SK No. 110/EL-SK/XII/2006
						//Req Himmia (3 Apr 2013) : Untuk setiap proses reward, dicek terlebih dahulu apakah rekruter tersebut di mst_agent masih terdaftar aktif dan mendapatkan komisi ga. Apabila 1, maka dapat reward.
						if(i==0 && Integer.parseInt(prodDate) > 2006 && komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1) {
							
							boolean isReward = false;
							if("37,52".indexOf(lca_id)>-1) { //Agency
								isReward = true;
							}else if(lca_id.equals("46")) { //arthamas
								isReward = false;
							}else if(lca_id.equals("42")) { // worksite
								isReward = false;
							}
							
							if(products.stableLink(komisi.getBisnis_id().toString())) {
								isReward = false;
								if("08,09,42,40,46".indexOf(lca_id) == -1 && bulanProd >= 200809 && bulanProd <= 200812) {
									isReward = true;
								}
							}
							
							if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())){
								isReward = false;
							}
							
							if(products.productKhususSuryamasAgency(komisi.getBisnis_id(), komisi.getBisnis_no())){
								isReward = false;
							}
							
							String rekruter_id = uwDao.selectRekruterAgencySystem(komisi.getAgent_id()); //tarik rekruternya, jenis=4
							Rekruter rekruter = uwDao.selectRekruterFromAgen(rekruter_id); //tarik rekruter dari mst_agent, ada gak?, jenis=3
							if(rekruter==null && rekruter_id!=null) rekruter = uwDao.selectRekruterFromAgenSys(rekruter_id); //kalo gak ada, tarik dari mst_agensys
							if(rekruter!=null && !products.powerSave(komisi.getBisnis_id().toString()) && isReward) { //kalo ada, baru insert rewardnya 10%
								
								if(rekruter.getMsag_bay() == null) rekruter.setMsag_bay(0);
								
								if(rekruter.getSts_aktif().intValue() == 1 && rekruter.getMsag_bay().intValue() != 1) { //Yusuf (22/12/2010) hanya bila rekruternya aktif dan bukan agen bayangan baru dikasih reward
									//cek dulu, ada nomer rekeningnya gak
									if(rekruter.getNo_tab()==null) {
										errors.reject("payment.noRecruiter", new Object[] {rekruter.getAgent_id()}, "-");
										return;
									} else if(rekruter.getNo_tab().trim().equals("")) {
										errors.reject("payment.noRecruiter", new Object[] {rekruter.getAgent_id()}, "-");
										return;
									} else {
										rekruter.setNo_tab(StringUtils.replace(rekruter.getNo_tab(), "-",""));
										double pengali = 1;
										
										//Yusuf (22/12/2010) bila business partner, maka dapat reward hanya 5%
										if(rekruter.getLcalwk().startsWith("61")) pengali = 0.5;
										//Yusuf (23/03/09) - Per 1 April OR dan Reward tidak 35% lagi tapi full
										else if(isLinkSingle(spaj, false) && bulanProd < 200904) pengali = 0.35;
										else if(isEkalink88Plus(spaj, false)) pengali = 0.7;
										else if(products.stableLink(komisi.getBisnis_id().toString())) pengali = 0.1;
										
										double reward = FormatNumber.round((ldec_komisi * 0.1 * pengali), 0);
										
										if(rekruter.getMsag_komisi()!=null){
											if(rekruter.getMsag_komisi()==1){
												if("183,189,193,195,201,204".indexOf(komisi.getBisnis_id().toString())<=-1){
													List<Billing> billing = akseptasiDao.selectMstBilling(spaj, 1, 1);
													if(billing.get(0).getLspd_id() == 99){
														Date reward_pay_date = commonDao.selectSysdateTruncated(1);
														String msco_no = this.uwDao.selectMscoNoByMonth(rekruter_id, "092014");
														if(Common.isEmpty(msco_no)){
															msco_no = sequence.sequenceMscoNoCommission(101, "01");
														}
														this.uwDao.insertMst_rewardKetinggalan(spaj, 1, 1, rekruter.getJenis_rekrut(), rekruter_id,
																rekruter.getNama(), rekruter.getNo_tab(), 
																rekruter.getLbn_id().toString(), 
																reward, 
																FormatNumber.rounding(f_load_tax(reward, sysdate, rekruter_id), true, 25),
																new Double(ldec_kurs), currentUser.getLus_id(), komisi.getLsbs_linebus(),reward_pay_date,reward_pay_date,msco_no);
													}else{
														this.uwDao.insertMst_reward(spaj, 1, 1, rekruter.getJenis_rekrut(), rekruter_id,
																rekruter.getNama(), rekruter.getNo_tab(), 
																rekruter.getLbn_id().toString(), 
																reward, 
																FormatNumber.rounding(f_load_tax(reward, sysdate, rekruter_id), true, 25),
																new Double(ldec_kurs), currentUser.getLus_id(), komisi.getLsbs_linebus());
													}
													
													
													emailMessage.append("[RECRUITER REWARD] " + rekruter_id + " : " + nf.format(reward)+ "\n");
												}
											}
										}
									}
								}								
							}
						}
					}
				}
			} 
		}
	}
	
	public void prosesRewardIndividu2007(String spaj, User currentUser, BindException errors) throws Exception{ //of_komisi_new_lg untuk 2007
		StringBuffer emailMessage = new StringBuffer();
		emailMessage.append("[SPAJ  ] " + spaj + "\n");
		NumberFormat nf = NumberFormat.getInstance();
		List detail = uwDao.selectDetailBisnis(spaj);
		if(!detail.isEmpty()) {
			Map det = (HashMap) detail.get(0);
			emailMessage.append(
					"[PRODUK] (" + det.get("BISNIS") + ") " + det.get("LSBS_NAME") + " - " + 
					"(" + det.get("DETBISNIS") + ") " + det.get("LSDBS_NAME") + "\n");
		}
		
		//Yusuf - PERSENTASE OVERRIDING BARU - 21/12/2006 - SK No. 110/EL-SK/XII/2006
		BigDecimal persentase[] = new BigDecimal[] {
			new BigDecimal("1"), 
			new BigDecimal("2"), 
			new BigDecimal("8"), 
			new BigDecimal("14"), 
			new BigDecimal("100")};
		
		double ldec_kurs = 1; 
		int li_dplk=0;
		int li_club=0;
		tingkatIndividu2007 current=null;
		tingkatIndividu2007 agen = null;
		boolean lb_deduct=false;
		double kom_tmp = 0;
		double tax_tmp = 0;
		
		//Informasi2 tambahan
		Map pribadi = this.uwDao.selectInfoPribadi(spaj);
		String ls_region = (String) pribadi.get("REGION");
		int li_pribadi = (Integer) pribadi.get("PRIBADI");
		
		if(ls_region.startsWith("67")) {
			persentase = new BigDecimal[] {
					new BigDecimal("0"), 
					new BigDecimal("20"), 
					new BigDecimal("30"), 
					new BigDecimal("40"), 
					new BigDecimal("100")};
		}
		
		emailMessage.append("[PRODUCTION DATE] " + pribadi.get("MSPRO_PROD_DATE") + "\n");
		int bulanProd = Integer.parseInt(uwDao.selectBulanProduksi(spaj));

		//KALAU POLIS TUTUPAN PRIBADI, TIDAK MENDAPAT KOMISI
		if(li_pribadi == 1) return; 
		
		int flag_telemarket = uwDao.selectgetFlagTelemarketing(spaj);
		
		List lds_kom = this.uwDao.selectViewKomisiAsli(spaj);
		if(!lds_kom.isEmpty()) {

			//CEK DULU PAKE ROW PERTAMA, KALO KURS DOLLAR, ADA KURSNYA GAK
			Commission komisiME = (Commission) lds_kom.get(0);
			if(komisiME.getKurs_id().equals("02")){
				ldec_kurs = this.uwDao.selectLastCurrency(spaj, 1, 1);
				if(ldec_kurs<0){
					errors.reject("payment.noLastCurrency");
					return;
				}
			}
			
			/** LOOPING UTAMA UNTUK MENGHITUNG KOMISI DARI V_GET_KOM (lds_kom) */			
			for(int i=0; i<lds_kom.size(); i++) {
				li_dplk=0;
				li_club=0;
				Commission komisi = (Commission) lds_kom.get(i);
				komisi.setLsbs_linebus(bacDao.selectLineBusLstBisnis(komisi.getBisnis_id().toString()));
				komisi.setFlag_mess(Boolean.TRUE);
				//kurs * premi
				double premi_asli = komisi.getPremi();
				if("183,189,193,195,201,204".indexOf(komisi.getBisnis_id().toString()) >-1){
					komisi.setPremi(bacDao.selectTotalKomisiEkaSehat(spaj));
				}
				komisi.setPremi(FormatNumber.round(komisi.getPremi() * ldec_kurs, 2));
				//pukul rata cara bayar (lscb_id)
				int li_cb_id = komisi.getCb_id();
				if(!products.progressiveLink(komisi.getBisnis_id().toString()) && !products.stableSavePremiBulanan(komisi.getBisnis_id().toString()) && !products.healthProductStandAlone(komisi.getBisnis_id().toString())){
					if( li_cb_id==1 || li_cb_id==2 || li_cb_id==6 ) komisi.setCb_id(3);
				}
				//Commission individu
				komisi.setLsco_jenis(new Integer(1));
				//ambil lama tanggung
				getLamaTanggung(komisi);
				//tarik persentase komisinya, OR-nya di hardcode
				if(f_get_komisi(komisi, errors, 1, 1, komisi.getKurs_id(), komisi.getPremi(), 4)) {
					
					/** SPECIAL CASES */
					Map prestige = this.uwDao.selectPrestigeClub(komisi.getAgent_id(), komisi.getTgl_kom());
					if(prestige!=null){
						li_club = Integer.parseInt(prestige.get("MSAC_AKTIF").toString());
						li_dplk = Integer.parseInt(prestige.get("MSAC_DPLK").toString());
					}
					/** (DI LEVEL 4) */
					if(komisi.getLev_kom()==4) {
						if(products.powerSave(komisi.getBisnis_id().toString())){
							int mgi = this.uwDao.selectMgiNewBusiness(spaj);
							TopUp t = (TopUp) this.uwDao.selectTopUp(spaj, 1, 1, "desc").get(0);
							Date ldt_tgl_rk = t.getMspa_date_book();
							if(ldt_tgl_rk.compareTo(defaultDateFormatReversed.parse("20111116"))< 0 ){//request TRI, berdasarkan tgl RK apabila tgl RK di bawah tgl 16 nov 2011, ambil komisi lama
								komisi.setTgl_kom(ldt_tgl_rk);
							}
							Map komisiPowersave = uwDao.selectKomisiPowersave(1, 
									komisi.getBisnis_id(), komisi.getBisnis_no(), 
									mgi, komisi.getKurs_id(), komisi.getLev_kom(), 1, komisi.getPremi(), 0, komisi.getTgl_kom());

							if(komisiPowersave == null) {
								errors.reject("payment.noCommission", 
										new Object[]{
										   defaultDateFormat.format(komisi.getTgl_kom()),
										   komisi.getBisnis_id()+"-"+komisi.getBisnis_no(),
										   komisi.getCb_id(), 
										   komisi.getIns_period(), 
										   komisi.getKurs_id(), 
										   komisi.getLev_kom(),
										   komisi.getTh_kom(),
										   komisi.getLsco_jenis()
								           }, 
										"Commission POWERSAVE Tidak Ada.<br>- Tanggal: {0}<br>-Kode Bisnis: {1}<br>-Cara Bayar: {2}<br>-Ins_per: {3}<br>-Kurs: {4}<br>-Level: {5}<br>-Tahun Ke{6}<br>-Jenis: {7}");
								throw new Exception(errors);
							}else {
								if(komisiPowersave.get("LSCP_COMM")!=null) komisi.setKomisi(Double.valueOf(komisiPowersave.get("LSCP_COMM").toString()));
								if(komisiPowersave.get("LSCP_BONUS")!=null) komisi.setBonus(Double.valueOf(komisiPowersave.get("LSCP_BONUS").toString()));
							}
						}else{
//							Deddy - Semua stable save komisi diset 0.58
							if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())){
								komisi.setKomisi(0.58);
							}
							
							//POWERSAVE INDIVIDU
							else if(komisi.getBisnis_id()==143) {
								int li_jwaktu = this.uwDao.selectMgiNewBusiness(spaj);
								if(li_jwaktu==3) komisi.setKomisi(new Double(0.05));
								else if(li_jwaktu==6) komisi.setKomisi(new Double(0.1));
								else if(li_jwaktu==12) komisi.setKomisi(new Double(0.2));
								else if(li_jwaktu==36) komisi.setKomisi(new Double(0.6));
								else komisi.setKomisi(new Double(0));
								
//								(Deddy 28/7/2009) SK Direksi No.079/AJS-SK/VII/2009
								if(komisi.getBisnis_no().intValue()==1 || komisi.getBisnis_no().intValue()==2 || komisi.getBisnis_no().intValue()==3){
									if(li_jwaktu==3) komisi.setKomisi(new Double(0.063));
									else if(li_jwaktu==6) komisi.setKomisi(new Double(0.125));
									else if(li_jwaktu==12) komisi.setKomisi(new Double(0.25));
									else if(li_jwaktu==36) komisi.setKomisi(new Double(0.75));
									else komisi.setKomisi(new Double(0));
									// 027/IM-DIR/IV/2011 Tertanggal 11 April 2011 perubahan komisi untuk powersave dan stable link.
									if(bulanProd>=201105){
										if(li_jwaktu==1) komisi.setKomisi(0.05);
										else if(li_jwaktu==3) komisi.setKomisi(0.15);
										else if(li_jwaktu==6) komisi.setKomisi(0.3);
										else if(li_jwaktu==12) komisi.setKomisi(0.6);
										else if(li_jwaktu==36) komisi.setKomisi(1.35);
										else komisi.setKomisi(new Double(0));
									}
								}
								
								if(komisi.getBisnis_no().intValue()==4){
									komisi.setKomisi(0.58);
								}else if(komisi.getBisnis_no().intValue()==5){
									komisi.setKomisi(2.5);//(Deddy - 28/7/2009)berlaku tgl : 1 agustus 2009 berdasarkan SK. Direksi No. 088/AJS-SK/VII/2009
								}
								
								
							//POWERSAVE BULANAN YANG WORKSITE & BUKAN BANK Sinarmas (YUSUF - 25/08/2006)
							}else if(komisi.getBisnis_id()==158 && komisi.getBisnis_no() < 5 && ls_region.startsWith("42")) {
								int li_jwaktu = this.uwDao.selectMgiNewBusiness(spaj);
								if(li_jwaktu==3) komisi.setKomisi(0.12);
								else if(li_jwaktu==6) komisi.setKomisi(0.24);
								else if(li_jwaktu==12) komisi.setKomisi(0.48);
								else if(li_jwaktu==36) komisi.setKomisi(1.18);
								else komisi.setKomisi(0.0);

							//POWERSAVE BULANAN YANG BUKAN FINANCIAL PLANNER & BUKAN BANK Sinarmas (YUSUF - 25/08/2006)
							}else if(komisi.getBisnis_id()==158 && komisi.getBisnis_no() < 5 && !ls_region.startsWith("0916")) {
								int li_jwaktu = uwDao.selectMgiNewBusiness(spaj);
								if(li_jwaktu==3) komisi.setKomisi(0.12);
								else if(li_jwaktu==6) komisi.setKomisi(0.24);
								else if(li_jwaktu==12) komisi.setKomisi(0.48);
								else if(li_jwaktu==36) komisi.setKomisi(1.18);
								else komisi.setKomisi(0.0);
							}else if(komisi.getBisnis_id()==158 && (komisi.getBisnis_no().intValue()==13 || komisi.getBisnis_no().intValue()==16 )) {
								komisi.setKomisi(0.58);
							}else if(komisi.getBisnis_id()==149) {
								Map dplk = uwDao.selectPersentaseDplk(spaj);
								komisi.setPremi((Double) dplk.get("premi"));
							
							//PA Stand Alone, 10 %
							}else if(komisi.getBisnis_id()==73) {
								//Yusuf (19 Feb 2007) PA Stand Alone
								komisi.setKomisi(new Double(10));
								
							//POWERSAVE SYARIAH (YUSUF - 28/01/09)
							}else if(komisi.getBisnis_id() == 175) {
								int li_jwaktu = this.uwDao.selectMgiNewBusiness(spaj);

								if(li_jwaktu==3) komisi.setKomisi(0.05);
								else if(li_jwaktu==6) komisi.setKomisi(0.1);
								else if(li_jwaktu==12) komisi.setKomisi(0.2);
								else if(li_jwaktu==36) komisi.setKomisi(0.6);
								else komisi.setKomisi(0.0);
								
							//POWERSAVE BULANAN SYARIAH (YUSUF - 28/01/09)
							}else if(komisi.getBisnis_id() == 176) {
								int li_jwaktu = this.uwDao.selectMgiNewBusiness(spaj);

								if(li_jwaktu==3) komisi.setKomisi(0.12);
								else if(li_jwaktu==6) komisi.setKomisi(0.24);
								else if(li_jwaktu==12) komisi.setKomisi(0.48);
								else if(li_jwaktu==36) komisi.setKomisi(1.18);
								else komisi.setKomisi(new Double(0));
								
							//STABLE SAVE (YUSUF - 30/01/09)
							}else if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())) {
								if(komisi.getBisnis_no().intValue()==1){ //individu
									komisi.setKomisi(0.58);
								}else if(komisi.getBisnis_no().intValue()==2){ //bii
									komisi.setKomisi(2.5);//(Deddy - 28/7/2009)berlaku tgl : 1 agustus 2009 berdasarkan SK. Direksi No. 088/AJS-SK/VII/2009
								}

							//STABLE SAVE PREMI BULANAN (YUSUF - 31/04/09)
							}else if(products.stableSavePremiBulanan(komisi.getBisnis_id().toString())) {
								int li_jwaktu = this.uwDao.selectMasaGaransiInvestasi(spaj,1,1); // SK.Direksi No. 140/AJS-SK/XII/2009

								if(li_jwaktu==3) komisi.setKomisi(0.125);
								else if(li_jwaktu==6) komisi.setKomisi(0.25);
								else if(li_jwaktu==12) komisi.setKomisi(0.5);
								else if(li_jwaktu==1) komisi.setKomisi(0.042);
							}else if(products.progressiveLink(komisi.getBisnis_id().toString()) && ( bulanProd >= 201102 && bulanProd <=201104 ) ){
								
								int li_jwaktu = this.uwDao.selectMasaGaransiInvestasi(spaj,1,1);
								
								if(li_jwaktu==3) komisi.setKomisi(0.126);
								else if(li_jwaktu==6) komisi.setKomisi(0.375);
								else if(li_jwaktu==12) komisi.setKomisi(0.75);
								else if(li_jwaktu==1) komisi.setKomisi(1.5);
							}
						}
						
//						
					}
					
					/** LEV_COMM <> 4 */
					// untuk Financial Planner (cab = '43') -- 19/04/2006 (RG)
					if(ls_region.startsWith("43")) {
						if(komisi.getLev_kom()==1 || komisi.getLev_kom()==2)
							komisi.setKomisi(new Double(0));
						else if(komisi.getLev_kom()==3)
							komisi.setKomisi(new Double(20));
					}
					
					//Ganti per 22 Oct 2001, Memo Yimmy Lesmana - cara bayar 3, dapet bonus
					if(komisi.getCb_asli()==3) {
						komisi.setKomisi(komisi.getKomisi()+komisi.getBonus());
					}

					//Khusus eka sarjana mandiri RP 3 & 5
					// Kalo premi RP > 10jt atau prestige club
					if(komisi.getLev_kom()==4) {
						if(komisi.getBisnis_id()==63 && komisi.getKurs_id().equals("01") && (komisi.getBisnis_no()==2 || komisi.getBisnis_no()==3)){
							if(komisi.getPremi() >=10000000 || li_club==1){
								//3 tahun
								if(komisi.getBisnis_no()==2){
									komisi.setKomisi(7.5);
									if(komisi.getCb_asli()==3)komisi.setKomisi(12.5);
								}else{
									//5 tahun
									komisi.setKomisi((double) 25);
									if(komisi.getCb_asli()==3)komisi.setKomisi((double) 30);
								}
							}
						//Khusus eka sarjana mandiri yang baru (173)
						}else if(komisi.getBisnis_id()==173){
							if(i==0){
								int autodebet = bacDao.selectFlagCC(spaj);
								//bila autodebet payroll
								if(autodebet == 4){
									komisi.setKomisi(7.5);
								}else{									
									if(komisi.getBisnis_no()==1){
										komisi.setKomisi(5.);
									}else if(komisi.getBisnis_no()==2 || komisi.getBisnis_no()==3){
										komisi.setKomisi(10.);
									}
								}
							}
						}else if(komisi.getBisnis_id()==172){
							if(i==0){
								Integer li_age = this.uwDao.selectAgeFromSPAJ(komisi.getReg_spaj());
								//bila cara bayar bulanan
								if(komisi.getCb_asli().intValue() == 6) {
									if(komisi.getKurs_id().equals("01") ){
										if(li_age >=1 && li_age <= 8){
											komisi.setKomisi(new Double(22.5));
										}else if(li_age>=9 && li_age <= 12){
											komisi.setKomisi(new Double(17.5));
										}
									}else if(komisi.getKurs_id().equals("02")){
										if(li_age >=1 && li_age <= 5){
											komisi.setKomisi(new Double(22.5));
										}else if(li_age>=6 && li_age <= 8){
											komisi.setKomisi(new Double(17.5));
										}else if(li_age>=9 && li_age <= 12){
											komisi.setKomisi(new Double(12.5));
										}
									}
									
								//cara bayar lainnya
								}else{
									if(komisi.getKurs_id().equals("01") ){
										if(li_age >=1 && li_age <= 8){
											komisi.setKomisi(new Double(25));
										}else if(li_age>=9 && li_age <= 12){
											komisi.setKomisi(new Double(20));
										}
									}else if(komisi.getKurs_id().equals("02")){
										if(li_age >=1 && li_age <= 5){
											komisi.setKomisi(new Double(25));
										}else if(li_age>=6 && li_age <= 8){
											komisi.setKomisi(new Double(20));
										}else if(li_age>=9 && li_age <= 12){
											komisi.setKomisi(new Double(15));
										}
									}
								}
							}
							//prolife(179)
						}else if(komisi.getBisnis_id()==179){
							if(i==0){
								komisi.setKomisi(5.);
								int autodebet = bacDao.selectFlagCC(spaj);
								//bila autodebet payroll
								if(autodebet == 4){
									if(komisi.getBisnis_no().intValue()==1){
										komisi.setKomisi(17.5);
									}else if(komisi.getBisnis_no().intValue()==2){
										komisi.setKomisi(22.5);
									}else if(komisi.getBisnis_no().intValue()==3){
										komisi.setKomisi(22.5);
									}
								}else{									
									if(komisi.getBisnis_no().intValue()==1){
										komisi.setKomisi(20.0);
									}else if(komisi.getBisnis_no().intValue()==2){
										komisi.setKomisi(25.0);
									}else if(komisi.getBisnis_no().intValue()==3){
										komisi.setKomisi(25.0);
									}
									if(komisi.getCb_asli().intValue()==0){
										komisi.setKomisi(5.0);
									}
								}
							}
							//Khusus prosaver (180)
						}else if(komisi.getBisnis_id()==180){
							if(i==0){
//								komisi.setKomisi(5.);
								int autodebet = bacDao.selectFlagCC(spaj);
								//bila autodebet payroll
								if(autodebet == 4){
									if(komisi.getBisnis_no().intValue()==1){
										komisi.setKomisi(17.5);
									}else if(komisi.getBisnis_no().intValue()==2){
										komisi.setKomisi(22.5);
									}else if(komisi.getBisnis_no().intValue()==3){
										komisi.setKomisi(22.5);
									}
								}else{									
									if(komisi.getBisnis_no().intValue()==1){
										komisi.setKomisi(20.0);
									}else if(komisi.getBisnis_no().intValue()==2){
										komisi.setKomisi(25.0);
									}else if(komisi.getBisnis_no().intValue()==3){
										komisi.setKomisi(25.0);
									}
									if(komisi.getCb_asli().intValue()==0){
										komisi.setKomisi(5.0);
									}
								}
							}
						}
					}
					
					/** END SPECIAL CASES */
					
					/** START PERHITUNGAN OR (UNTUK LEVEL != 4) */
					//cari tahu dulu, nih agen apaan? 
					//4 ME
					//3 UM
					//2 & MSAG_FLAG_BM=0 -> AM / 2 & MSAG_FLAG_BM=1 -> BM 
					//1 & MSAG_SBM=1 -> SBM
					//1 & MSAG_SBM=0 -> RM
					if(komisi.getLsle_id()==4) agen = tingkatIndividu2007.ME;
					else if(komisi.getLsle_id()==3) agen = tingkatIndividu2007.UM;
					else if(komisi.getLsle_id()==2) agen = tingkatIndividu2007.BM;
					else if(komisi.getLsle_id()==1 && komisi.getFlag_sbm()==1) agen = tingkatIndividu2007.SBM;
					else if(komisi.getLsle_id()==1 && komisi.getFlag_sbm()==0) agen = tingkatIndividu2007.RM;
					
					if(komisi.getLev_kom()<4) {
						//karena OR baru, persentasenya dihardcode, gak ngambil dari database
						komisi.setKomisi((double) 0);
						String msag_id_leader = commonDao.selectMsagIdLeader(komisi.getAgent_id());
						if(komisi.getBisnis_id()==170 && komisi.getAgent_id().equals(msag_id_leader)){
							if(komisi.getLev_kom()==3){
//								kalau yang nutup ME, ORnya gulung dari atas (RM)
								BigDecimal persentase2[] = new BigDecimal[] {
										new BigDecimal("0"), 
										new BigDecimal("0"), 
										new BigDecimal("0"), 
										new BigDecimal("125"), 
										new BigDecimal("100")};
								if(current.ordinal()>agen.ordinal()) {
									for(int j = current.ordinal(); j>agen.ordinal(); j--) {
										komisi.setKomisi(persentase2[j-1].add(new BigDecimal(komisi.getKomisi())).doubleValue());
									}
								//kalo yang nutup bukan ME, ORnya gulung dari bawah (ME)
								}else if(current.equals(agen)) {
									for(int j = current.ordinal(); j<tingkatIndividu2007.ME.ordinal(); j++) {
										komisi.setKomisi(persentase2[j].add(new BigDecimal(komisi.getKomisi())).doubleValue());
									}
								}
							}
						}else{
//							kalau yang nutup ME, ORnya gulung dari atas (RM)
							if(current.ordinal()>agen.ordinal()) {
								for(int j = current.ordinal(); j>agen.ordinal(); j--) {
									komisi.setKomisi(persentase[j-1].add(new BigDecimal(komisi.getKomisi())).doubleValue());
								}
							//kalo yang nutup bukan ME, ORnya gulung dari bawah (ME)
							}else if(current.equals(agen)) {
								for(int j = current.ordinal(); j<tingkatIndividu2007.ME.ordinal(); j++) {
									komisi.setKomisi(persentase[j].add(new BigDecimal(komisi.getKomisi())).doubleValue());
								}
							}
						}
						
						if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())){
							komisi.setKomisi(0.0);
						}
					}
					current = agen;
					/** END PERHITUNGAN OR */
					
					//stable link, ada or sept - dec 2008, seliain itu tidak ada
					if(komisi.getLev_kom().intValue() < 4 && products.stableLink(komisi.getBisnis_id().toString())) {
						if(bulanProd >= 200809 && bulanProd <= 200812) {
							komisi.setKomisi(0.1 * komisi.getKomisi());
						}else {
							komisi.setKomisi(0.);
						}
					}
					
					if(komisi.getLev_kom().intValue() < 4 && komisi.getBisnis_id()==203){
						komisi.setKomisi(0.);
					}
					
					
					if(ls_region.substring(0,2).equals("42")){
						if(financeDao.selectIsAgenCorporate(komisi.getAgent_id())==1 && komisi.getLev_kom().intValue()<3){
							komisi.setKomisi(0.);
						}
						if(financeDao.selectIsAgenCorporate(komisi.getAgent_id())==1 && komisi.getLev_kom().intValue()==3){
							if(products.stableLink(komisi.getBisnis_id().toString()) || products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no()) || products.powerSave(komisi.getBisnis_id().toString()) ){
								komisi.setKomisi(0.);
							}else{
								komisi.setKomisi(komisi.getKomisi()*0.15);
							}
						}
						if(bulanProd >= 201002){ // Mulai bulan feb 2010 && merupakan worksite && msag_kry=1, maka OR tidak diberlakukan.
							if(komisi.getLev_kom().intValue() < 4 && financeDao.selectIsAgenKaryawan(komisi.getAgent_id())==1){
								komisi.setKomisi(0.);
							}
						}
					}
					
					/** START INSERTING COMMISSION */
					if(komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
						double persen = komisi.getKomisi();
						if(komisi.getLev_kom()==4) {
							emailMessage.append("[PREMI ] " + nf.format(komisi.getPremi()) + "\n");
							emailMessage.append("[KOMISI] " + komisi.getLev_kom() + " : " + komisi.getAgent_id() + " : " + 
									nf.format(komisi.getPremi()) + " * " + nf.format(komisi.getKomisi()) + "% = ");
							komisi.setKomisi(komisi.getPremi() * komisi.getKomisi() / 100); //komisi = premi * persen komisi
							emailMessage.append(nf.format(komisi.getKomisi()) + "\n");
						} else {
							emailMessage.append("[OR    ] " + komisi.getLev_kom() + " : " + komisi.getAgent_id() + " : " + 
									nf.format(komisiME.getKomisi()) + " * " + nf.format(komisi.getKomisi()) + "% = ");
							komisi.setKomisi(komisiME.getKomisi() * komisi.getKomisi() / 100); //OR = komisi ME * persen komisi
							emailMessage.append(nf.format(komisi.getKomisi()) + "\n");
						}
						
						//TAMBAHAN OLEH YUSUF UNTUK PRODUK2 SINGLE (IM No. 132/IM-DIR/XII/2007) - 16/01/2008
						komisi.setKomisi(cekLinkSingle(spaj, komisi.getKomisi(), komisi.getLev_kom(), false));
						
						if(flag_telemarket==1){
							komisi.setKomisi(komisi.getKomisi()/0.5);
						}
						
						komisi.setCo_id(sequenceMst_commission(11));
						komisi.setTax(new Double(komisi.getKomisi() * komisi.getTax() / 100));
						Date sysdate = commonDao.selectSysdateTruncated(0);
						if(komisi.getTax() > 0)komisi.setTax(f_load_tax(komisi.getKomisi(), sysdate, komisi.getAgent_id()));
						double ad_sisa;
						ad_sisa = komisi.getTax()%25;
						if(ad_sisa!=0){
							
						}
						komisi.setTax(FormatNumber.rounding(komisi.getTax() , true, 25));
						komisi.setKomisi(FormatNumber.round(komisi.getKomisi(), 0));
						if(komisi.getTax()==null)komisi.setTax(new Double(0));
						
						//SPECIAL CASE JUGA, KELUPAAN DITAMBAHIN DARI PROSES LAMA, untuk franchise
						if(ls_region.substring(0,2).equals("14")) {
							if(komisi.getLev_kom()==4) {
								kom_tmp = komisi.getKomisi();
								tax_tmp = komisi.getTax();
							} else if(komisi.getLev_kom()==3) {
								komisi.setKomisi(kom_tmp);
								komisi.setTax(tax_tmp);
							} else {
								komisi.setKomisi(new Double(0));					
								komisi.setTax(new Double(0));
							}
						}
						
						komisi.setNilai_kurs(new Double(ldec_kurs));
						komisi.setLus_id(currentUser.getLus_id());
						komisi.setMsbi_tahun_ke(1);
						komisi.setMsbi_premi_ke(1);
						komisi.setMsco_flag(li_club);
					}
					
					//Yusuf - 12 Sep 08 - Stable Link agency dan regional ada reward khusus sept - dec, selain itu tidak ada
					boolean isRewardStableLink = true; 
					if(products.stableLink(komisi.getBisnis_id().toString())) {
						String lca_id = uwDao.selectCabangFromSpaj(spaj);
						if("08,09,42,40,46".indexOf(lca_id) == -1 && bulanProd >= 200809 && bulanProd <= 200812) {
							isRewardStableLink = true;
						}else {
							isRewardStableLink = false;
						}
					}else if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())){
						isRewardStableLink = false;
					}else if(ls_region.substring(0,2).equals("42")) {
						isRewardStableLink = false;
					}
					
					//proses insert reward untuk recruiter (pukul rata 10%, hanya apabila ada recruiter dan statusnya aktif)
					//Req Himmia (3 Apr 2013) : Untuk setiap proses reward, dicek terlebih dahulu apakah rekruter tersebut di mst_agent masih terdaftar aktif dan mendapatkan komisi ga. Apabila 1, maka dapat reward.
					if(komisi.getLev_kom()==4 && isRewardStableLink && komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
						Map<String, Object> rekruter = this.uwDao.selectRekruter(komisi.getAgent_id());
						
						if(rekruter != null && !products.powerSave(komisi.getBisnis_id().toString()) && rekruter.get("MSRK_AKTIF").toString().equals("1")) {
							if(rekruter.get("NO_ACCOUNT")==null && rekruter.get("MSRK_AKTIF").toString().equals("1")) {
								errors.reject("payment.noRecruiter", new Object[] {rekruter.get("MSRK_ID")}, "-"); throw new Exception(errors);
							} else if(rekruter.get("NO_ACCOUNT").toString().equals("")) {
								errors.reject("payment.noRecruiter", new Object[] {rekruter.get("MSRK_ID")}, "-"); throw new Exception(errors);
							} else {
								int jenis_rekrut = Integer.valueOf(((String) rekruter.get("MSRK_JENIS_REKRUT")));
								//kalau jenis_rekrut = 2 (rekrut langsung), cek status aktifnya dari mst_agent
								//kalau jenis_rekrut lainnya, cek status aktifnya dari mst_rekruter
								if((((jenis_rekrut == 2 || jenis_rekrut==3) && rekruter.get("MSAG_ACTIVE").toString().equals("1")) || 
										(!(jenis_rekrut == 2 || jenis_rekrut==3) && rekruter.get("MSRK_AKTIF").toString().equals("1"))) && rekruter.get("MSAG_KOMISI").toString().equals("1")) {
									//req Pak Him : pengecekan terakhir dari rekruter apakah ada di mst_agent.
									if(uwDao.selectbacCekAgen(rekruter.get("MSRK_ID").toString())!=null ){
										
										Rekruter rek = uwDao.selectRekruterFromAgen(rekruter.get("MSRK_ID").toString());
										if(rek.getMsag_bay() == null) rek.setMsag_bay(0);
										
										double pengali = 1;
										
										//Yusuf (22/12/2010) bila business partner, maka dapat reward hanya 5%
										if(rek.getLcalwk().startsWith("61")) pengali = 0.5;
										//Yusuf (23/03/09) - Per 1 April OR dan Reward tidak 35% lagi tapi full
										else if(isLinkSingle(spaj, false) && bulanProd < 200904) pengali = 0.35;
										else if(isEkalink88Plus(spaj, false)) pengali = 0.7;
										else if(products.stableLink(komisi.getBisnis_id().toString())) pengali = 0.1;
										Double reward = FormatNumber.round((komisi.getKomisi() * 0.1 * pengali), 0);
										Date sysdate = commonDao.selectSysdateTruncated(0);
										Double tax = FormatNumber.rounding(f_load_tax(reward, sysdate, rekruter.get("MSRK_ID").toString()), true, 25);
										rekruter.put("NO_ACCOUNT", StringUtils.replace(rekruter.get("NO_ACCOUNT").toString(), "-",""));
										if("183,189,193,195,201,204".indexOf(komisi.getBisnis_id().toString())<=-1 && rek.getMsag_bay().intValue() != 1 ){
											if((komisi.getBisnis_id()==187 && "5,6".indexOf(komisi.getBisnis_no().toString())>-1) ){
												
											}else{
												List<Billing> billing = akseptasiDao.selectMstBilling(spaj, 1, 1);
												if(billing.get(0).getLspd_id() == 99){
													Date reward_pay_date = commonDao.selectSysdateTruncated(1);
													String msco_no = this.uwDao.selectMscoNoByMonth(rekruter.get("MSRK_ID").toString().trim(), "092014");
													if(Common.isEmpty(msco_no)){
														msco_no = sequence.sequenceMscoNoCommission(101, "01");
													}
													this.uwDao.insertMst_rewardKetinggalan(spaj, 1, 1, rekruter.get("MSRK_JENIS_REKRUT").toString(), rekruter.get("MSRK_ID").toString(),
															rekruter.get("MSRK_NAME").toString(), rekruter.get("NO_ACCOUNT").toString(), 
															rekruter.get("LBN_ID").toString(), reward, tax, new Double(ldec_kurs), currentUser.getLus_id(), komisi.getLsbs_linebus(),reward_pay_date,reward_pay_date,msco_no);
												}else{
													this.uwDao.insertMst_reward(spaj, 1, 1, rekruter.get("MSRK_JENIS_REKRUT").toString(), rekruter.get("MSRK_ID").toString(),
															rekruter.get("MSRK_NAME").toString(), rekruter.get("NO_ACCOUNT").toString(), 
															rekruter.get("LBN_ID").toString(), reward, tax, new Double(ldec_kurs), currentUser.getLus_id(), komisi.getLsbs_linebus());
												}
											}
											
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public boolean prosesRewardTopUpIndividu2007(String spaj, BindException errors, User currentUser, Integer premi_ke) throws Exception{
		if(logger.isDebugEnabled()) logger.debug("PROSES: prosesRewardTopUpIndividu2007 (of_komisi_tu_new())");
		StringBuffer emailMessage = new StringBuffer();
		emailMessage.append("[SPAJ        ] " + spaj + "\n");
		NumberFormat nf = NumberFormat.getInstance();
		List detail = uwDao.selectDetailBisnis(spaj);
		if(!detail.isEmpty()) {
			Map det = (HashMap) detail.get(0);
			emailMessage.append(
					"[PRODUK      ] (" + det.get("BISNIS") + ") " + det.get("LSBS_NAME") + " - " + 
					"(" + det.get("DETBISNIS") + ") " + det.get("LSDBS_NAME") + "\n");
		}

		//Yusuf - PERSENTASE OVERRIDING BARU - 21/12/2006 - SK No. 110/EL-SK/XII/2006
		BigDecimal persentase[] = new BigDecimal[] {
			new BigDecimal("1"), 
			new BigDecimal("2"), 
			new BigDecimal("8"), 
			new BigDecimal("14"), 
			new BigDecimal("100")};

		tingkatIndividu2007 current=null;
		tingkatIndividu2007 agen = null;

		//Informasi2 tambahan
		Map pribadi = this.uwDao.selectInfoPribadi(spaj);
		String ls_region = (String) pribadi.get("REGION");
		if(ls_region.startsWith("67")) {
			persentase = new BigDecimal[] {
					new BigDecimal("0"), 
					new BigDecimal("20"), 
					new BigDecimal("30"), 
					new BigDecimal("40"), 
					new BigDecimal("100")};
		}
		
		int li_pribadi = (Integer) pribadi.get("PRIBADI");
		emailMessage.append("[PRODUCTION DATE] " + pribadi.get("MSPRO_PROD_DATE") + "\n");

		Double premiTopup = this.uwDao.selectPremiTopUpUnitLink(spaj, premi_ke);
		double ldec_kurs = 1;

		//KALAU POLIS TUTUPAN PRIBADI, TIDAK MENDAPAT KOMISI
		if(li_pribadi == 1) return false; 
		String lca_id = ls_region.substring(0,2);
		if("37,46,52".indexOf(lca_id)>-1){  //37 agency, 46 arthamas, 52 Agency Arthamas (Deddy 22 Feb 2012)
			return prosesRewardTopUpAgencySystem(spaj, errors, currentUser, premi_ke, lca_id);
		//Yusuf (8/7/08) Proses Komisi Cross-Selling
		}else if(lca_id.equals("55")) {
			return true;
		}
		
		List lds_kom = this.uwDao.selectViewKomisiAsli(spaj);
		if(!lds_kom.isEmpty()) {
			//CEK DULU PAKE ROW PERTAMA, KALO KURS DOLLAR, ADA KURSNYA GAK
			Commission komisiME = (Commission) lds_kom.get(0);
			if(komisiME.getKurs_id().equals("02")){
				ldec_kurs = this.uwDao.selectLastCurrency(spaj, 1, 1);
				if(ldec_kurs<0){
					errors.reject("payment.noLastCurrency");
					return false;
				}
			}
			
			/** LOOPING UTAMA UNTUK MENGHITUNG KOMISI DARI V_GET_KOM (lds_kom) */			
			for(int i=0; i<lds_kom.size(); i++) {
				
				Commission komisi = (Commission) lds_kom.get(i);
				komisi.setLsbs_linebus(bacDao.selectLineBusLstBisnis(komisi.getBisnis_id().toString()));
				//
				komisi.setFlag_mess(Boolean.TRUE);
				komisi.setPremi(premiTopup);
				//kurs * premi
				komisi.setPremi(FormatNumber.round(komisi.getPremi() * ldec_kurs, 2));
				//pukul rata cara bayar (lscb_id)
				int li_cb_id = komisi.getCb_id();
				if(!products.progressiveLink(komisi.getBisnis_id().toString()) && !products.stableSavePremiBulanan(komisi.getBisnis_id().toString()) && !products.healthProductStandAlone(komisi.getBisnis_id().toString())){
					if( li_cb_id==1 || li_cb_id==2 || li_cb_id==6 ) komisi.setCb_id(3);
				}
				//Commission individu
				komisi.setLsco_jenis(new Integer(1));
				//ambil lama tanggung
				getLamaTanggung(komisi);
				//PENTING! untuk topup, LSCO_YEAR=0
				komisi.setTh_kom(0); 
				//tarik persentase komisinya, OR-nya di hardcode
				if(f_get_komisi(komisi, errors, 0, premi_ke, komisi.getKurs_id(), komisi.getPremi(), 4)) {
					/** START PERHITUNGAN OR (UNTUK LEVEL != 4) */
					//cari tahu dulu, nih agen apaan? 
					//4 ME
					//3 UM
					//2 & MSAG_FLAG_BM=0 -> AM / 2 & MSAG_FLAG_BM=1 -> BM 
					//1 & MSAG_SBM=1 -> SBM
					//1 & MSAG_SBM=0 -> RM
					if(komisi.getLsle_id()==4) agen = tingkatIndividu2007.ME;
					else if(komisi.getLsle_id()==3) agen = tingkatIndividu2007.UM;
					else if(komisi.getLsle_id()==2) agen = tingkatIndividu2007.BM;
					else if(komisi.getLsle_id()==1 && komisi.getFlag_sbm()==1) agen = tingkatIndividu2007.SBM;
					else if(komisi.getLsle_id()==1 && komisi.getFlag_sbm()==0) agen = tingkatIndividu2007.RM;
					
					if(komisi.getLev_kom()<4) {
						//karena OR baru, persentasenya dihardcode, gak ngambil dari database
						komisi.setKomisi((double) 0);
						//kalau yang nutup ME, ORnya gulung dari atas (RM)
						if(current.ordinal()>agen.ordinal()) {
							for(int j = current.ordinal(); j>agen.ordinal(); j--) {
								komisi.setKomisi(persentase[j-1].add(new BigDecimal(komisi.getKomisi())).doubleValue());
							}
						//kalo yang nutup bukan ME, ORnya gulung dari bawah (ME)
						}else if(current.equals(agen)) {
							for(int j = current.ordinal(); j<tingkatIndividu2007.ME.ordinal(); j++) {
								komisi.setKomisi(persentase[j].add(new BigDecimal(komisi.getKomisi())).doubleValue());
							}
						}
					}
					current = agen;
					/** END PERHITUNGAN OR */
					
					//stable link, ada or sept - dec 2008, seliain itu tidak ada
					int bulanProd = Integer.parseInt(uwDao.selectBulanProduksi(spaj));
					if(ls_region.substring(0,2).equals("42")){
						if(bulanProd >= 201002){ // Mulai bulan feb 2010 && merupakan worksite && msag_kry=1, maka OR tidak diberlakukan.
							if(komisi.getLev_kom().intValue() < 4 && financeDao.selectIsAgenKaryawan(komisi.getAgent_id())==1){
								komisi.setKomisi(0.);
							}
						}
					}
					
					if(komisi.getBisnis_id().toString().equals("164")){//
						Integer mgi = uwDao.selectMgiNewBusiness(spaj);
						if(bulanProd>=201105){
							if(mgi == 1) {
								komisi.setKomisi(0.05);
							}else if(mgi == 3) {
								komisi.setKomisi(0.15);
							}else if(mgi == 6) {
								komisi.setKomisi(0.3);
							}else if(mgi == 12) {
								komisi.setKomisi(0.6);
							}else if(mgi == 24) {
								komisi.setKomisi(1.2);
							}else if(mgi == 36) {
								komisi.setKomisi(1.35);
							}else {
								throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
							}
						}else{
							if(mgi == 1) {
								komisi.setKomisi(0.017); //Tidak ada MGI 1 bulan untuk stable link
							}else if(mgi == 3) {
								komisi.setKomisi(0.05);
							}else if(mgi == 6) {
								komisi.setKomisi(0.1);  
							}else if(mgi == 12) {
								komisi.setKomisi(0.2);
							}else if(mgi == 24) {
								komisi.setKomisi(0.4);
							}else if(mgi == 36) {
								komisi.setKomisi(0.6);
							}else {
								throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
							}
						}
					}else if(komisi.getBisnis_id().toString().equals("174")){
						Integer mgi = uwDao.selectMgiNewBusiness(spaj);
						if(mgi == 1) {
							komisi.setKomisi(0.05);
						}else if(mgi == 3) {
							komisi.setKomisi(0.15);
						}else if(mgi == 6) {
							komisi.setKomisi(0.3);
						}else if(mgi == 12) {
							komisi.setKomisi(0.6);
						}else if(mgi == 24) {
							komisi.setKomisi(1.2);
						}else if(mgi == 36) {
							komisi.setKomisi(1.35);
						}else {
							throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
						}
					}
					
					if(komisi.getLev_kom().intValue() < 4 && products.stableLink(komisi.getBisnis_id().toString())) {
						if(bulanProd >= 200809 && bulanProd <= 200812) {
							komisi.setKomisi(0.1 * komisi.getKomisi());
						}else {
							komisi.setKomisi(0.);
						}
					}
					
					/** START INSERTING COMMISSION */
					if(komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){

						if(komisi.getLev_kom()==4) {
							emailMessage.append("[PREMI TOPUP ] " + nf.format(komisi.getPremi()) + "\n");
							emailMessage.append("[KOMISI TOPUP] " + komisi.getLev_kom() + " : " + komisi.getAgent_id() + " : " + 
									nf.format(komisi.getPremi()) + " * " + nf.format(komisi.getKomisi()) + "% = ");
							komisi.setKomisi(komisi.getPremi() * komisi.getKomisi() / 100); //komisi = premi * persen komisi
							emailMessage.append(nf.format(komisi.getKomisi()) + "\n");
						} else {
							emailMessage.append("[OR TOPUP    ] " + komisi.getLev_kom() + " : " + komisi.getAgent_id() + " : " + 
									nf.format(komisiME.getKomisi()) + " * " + nf.format(komisi.getKomisi()) + "% = ");
							komisi.setKomisi(komisiME.getKomisi() * komisi.getKomisi() / 100); //OR = komisi ME * persen komisi
							emailMessage.append(nf.format(komisi.getKomisi()) + "\n");
						}
						
						//TAMBAHAN OLEH YUSUF UNTUK PRODUK2 SINGLE (IM No. 132/IM-DIR/XII/2007) - 16/01/2008
						komisi.setKomisi(cekLinkSingle(spaj, komisi.getKomisi(), komisi.getLev_kom(), true));
						
						komisi.setCo_id(sequenceMst_commission(11));
						komisi.setTax(new Double(komisi.getKomisi() * komisi.getTax() / 100));
						Date sysdate = commonDao.selectSysdateTruncated(0);
						if(komisi.getTax() > 0)komisi.setTax(f_load_tax(komisi.getKomisi(), sysdate, komisi.getAgent_id()));
						double ad_sisa;
						ad_sisa = komisi.getTax()%25;
						if(ad_sisa!=0){
							
						}
						komisi.setTax(FormatNumber.rounding(komisi.getTax() , true, 25));
						komisi.setKomisi(FormatNumber.round(komisi.getKomisi(), 0));
						if(komisi.getTax()==null)komisi.setTax(new Double(0));
						komisi.setNilai_kurs(new Double(ldec_kurs));
						komisi.setLus_id(currentUser.getLus_id());
						komisi.setMsbi_tahun_ke(1);
						komisi.setMsbi_premi_ke(premi_ke);
					}
					/** START INSERT KE TABEL2 TAMBAHAN */
					
					//Yusuf - 12 Sep 08 - Stable Link agency dan regional ada reward khusus sept - dec, selain itu tidak ada
					boolean isRewardStableLink = true;
					if(products.stableLink(komisi.getBisnis_id().toString())) {
						if("08,09,42,40,46".indexOf(lca_id) == -1 && bulanProd >= 200809 && bulanProd <= 200812) {
							isRewardStableLink = true;
						}else {
							isRewardStableLink = false;
						}
					}
					
					if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())) {
						isRewardStableLink = false;
					}
					
					if(lca_id.equals("42")){
						isRewardStableLink = false;
					}
					
					//proses insert reward untuk recruiter (pukul rata 10%, hanya apabila ada recruiter dan statusnya aktif)
					if(komisi.getLev_kom()==4 && isRewardStableLink){
						Map<String, Object> rekruter = this.uwDao.selectRekruter(komisi.getAgent_id());
						
						if(rekruter != null && !products.powerSave(komisi.getBisnis_id().toString()) &&  rekruter.get("MSRK_AKTIF").toString().equals("1")) {
							if(rekruter.get("NO_ACCOUNT")==null) {
								errors.reject("payment.noRecruiter", new Object[] {rekruter.get("MSRK_ID")}, "-"); throw new Exception(errors);
							} else if(rekruter.get("NO_ACCOUNT").toString().equals("")) {
								errors.reject("payment.noRecruiter", new Object[] {rekruter.get("MSRK_ID")}, "-"); throw new Exception(errors);
							} else {
								int jenis_rekrut = Integer.valueOf((String) rekruter.get("MSRK_JENIS_REKRUT"));
								//kalau jenis_rekrut = 2 (rekrut langsung), cek status aktifnya dari mst_agent
								//kalau jenis_rekrut lainnya, cek status aktifnya dari mst_rekruter
								if((((jenis_rekrut == 2 || jenis_rekrut==3) && rekruter.get("MSAG_ACTIVE").toString().equals("1")) || 
										(!(jenis_rekrut == 2 || jenis_rekrut==3) && rekruter.get("MSRK_AKTIF").toString().equals("1"))) && rekruter.get("MSAG_KOMISI").toString().equals("1")) {
									if(uwDao.selectbacCekAgen(rekruter.get("MSRK_ID").toString())!=null){
										
										Rekruter rek = uwDao.selectRekruterFromAgen(rekruter.get("MSRK_ID").toString());
										if(rek.getMsag_bay() == null) rek.setMsag_bay(0);
										
										double pengali = 1;
										
										//Yusuf (22/12/2010) bila business partner, maka dapat reward hanya 5%
										if(rek.getLcalwk().startsWith("61")) pengali = 0.5;
										//Yusuf (23/03/09) - Per 1 April OR dan Reward tidak 35% lagi tapi full
										else if(isLinkSingle(spaj, false) && bulanProd < 200904) pengali = 0.35;
										else if(isEkalink88Plus(spaj, true)) pengali = 0.7;
										else if(products.stableLink(komisi.getBisnis_id().toString())) pengali = 0.1;
										
										Double reward = FormatNumber.round((komisi.getKomisi() * 0.1 * pengali),0);
										
										Date sysdate = commonDao.selectSysdateTruncated(0);
										Double tax = FormatNumber.rounding(f_load_tax(reward, sysdate, rekruter.get("MSRK_ID").toString()), true, 25);
										rekruter.put("NO_ACCOUNT", StringUtils.replace(rekruter.get("NO_ACCOUNT").toString(), "-",""));
										
										if(rek.getMsag_bay().intValue() != 1){
											List<Billing> billing = akseptasiDao.selectMstBilling(spaj, 1, premi_ke);
											if(billing.get(0).getLspd_id() == 99){
												Date reward_pay_date = commonDao.selectSysdateTruncated(1);
												String msco_no = this.uwDao.selectMscoNoByMonth(rekruter.get("MSRK_ID").toString().trim(), "092014");
												if(Common.isEmpty(msco_no)){
													msco_no = sequence.sequenceMscoNoCommission(101, "01");
												}
												this.uwDao.insertMst_rewardKetinggalan(spaj, 1, premi_ke, rekruter.get("MSRK_JENIS_REKRUT").toString(), rekruter.get("MSRK_ID").toString(),
														rekruter.get("MSRK_NAME").toString(), rekruter.get("NO_ACCOUNT").toString(), 
														rekruter.get("LBN_ID").toString(), reward, tax, new Double(ldec_kurs), currentUser.getLus_id(), komisi.getLsbs_linebus(),reward_pay_date,reward_pay_date,msco_no);
											}else{
												this.uwDao.insertMst_reward(spaj, 1, premi_ke, rekruter.get("MSRK_JENIS_REKRUT").toString(), rekruter.get("MSRK_ID").toString(),
														rekruter.get("MSRK_NAME").toString(), rekruter.get("NO_ACCOUNT").toString(), 
														rekruter.get("LBN_ID").toString(), reward, tax, new Double(ldec_kurs), currentUser.getLus_id(), komisi.getLsbs_linebus());
											}
										}
										
										emailMessage.append("[RECRUITER REWARD] " + rekruter.get("MSRK_ID").toString() + " : " + nf.format(reward)+ "\n");
									}
								}
							}
						}
					}
					/** END INSERT KE TABEL2 TAMBAHAN */
				}
			}
		}
		return true;
	}
	
	public boolean prosesRewardTopUpAgencySystem(String spaj, BindException errors, User currentUser, Integer premi_ke, String lca_id) throws Exception{ //of_komisi_tu_as()
		if(logger.isDebugEnabled())logger.debug("PROSES: prosesRewardTopUpAgencySystem (of_komisi_tu_as()) ");
		StringBuffer emailMessage = new StringBuffer();
		emailMessage.append("[SPAJ  ] " + spaj + "\n");
		NumberFormat nf = NumberFormat.getInstance();

		List lds_kom;
		List lds_prod;

		Map pribadi = this.uwDao.selectInfoPribadi(spaj);
		String prodDate = (String) pribadi.get("PROD_DATE");
		emailMessage.append("[PRODUCTION DATE] " + pribadi.get("MSPRO_PROD_DATE") + "\n");
		
		int i; double ldec_premi; double ldec_komisi=0; Double ldec_kurs = new Double(1); 
		double[] ldec_pct_kom = {0,3, 8.5, 22.5, 2};
		String li_bisnis; 

		//Yusuf - 5/1/09 - Mulai 2009, struktur komisi arthamas ada 6 tingkat, bukan 4 lagi
		if(Integer.parseInt(prodDate) > 2008 && lca_id.equals("46")) {
			ldec_pct_kom = new double[]{0, 0, 0, 0, 0, 0, 0};			
			lds_kom = this.uwDao.selectViewKomisiHybrid2009(spaj);
			lds_prod = this.uwDao.selectAgentsFromSpajHybrid2009(spaj);
			
		}else {
			lds_kom = this.uwDao.selectViewKomisiAsli(spaj);
			lds_prod = this.uwDao.selectAgentsFromSpaj(spaj);
		}
		
		if(!lds_kom.isEmpty()) {
			Commission tmp = (Commission) lds_kom.get(0);
			if(tmp.getKurs_id().equals("02")){
				ldec_kurs = new Double(this.uwDao.selectLastCurrency(spaj, 1, 1));
				if(ldec_kurs==null){
					errors.reject("payment.noLastCurrency");
					throw new Exception(errors);
				}
			}
			
			String ls_agent[] = new String[4];

			//ini rate OR untuk 37&52 (agency)
			int li_or[] = {0, 40, 30, 20};
			//ini rate OR untuk 46 (arthamas)
			if(lca_id.equals("46")) {
				li_or = new int[]{0, 35, 25, 20};
			}
			
			int li_persen[] = new int[4];
			int maxCount = 4;
			
			if(lds_kom.size()>4){
				ls_agent = new String[5];
				li_persen = new int[5];
			}
			
			//Yusuf - 5/1/09 - Mulai 2009, struktur komisi arthamas ada 6 tingkat, bukan 4 lagi
			if(Integer.parseInt(prodDate) > 2008 && lca_id.equals("46")) {
				ls_agent = new String[6];
				li_or = new int[]{0, 35, 25, 20, 5, 1}; //FC - SM - BM - DM - RM - RD
				li_persen = new int[6];
				maxCount = 6;
			}			
			
			int k=2;
			int ll_find;
			boolean ketemu = false;
			double persen=0;
			
			for(i=1; i<lds_kom.size(); i++){
				Commission temp = (Commission) lds_kom.get(i);
				temp.setLsbs_linebus(bacDao.selectLineBusLstBisnis(temp.getBisnis_id().toString()));
				ls_agent[i] = temp.getAgent_id();
				li_persen[i] = 0;
				ll_find = 0;
				do{
					ketemu=false;
					
					for(int j = ll_find; j<lds_prod.size(); j++){
						String msag_id = ((HashMap) lds_prod.get(j)).get("MSAG_ID").toString(); 
						if(msag_id.equals(ls_agent[i])){ 
							ll_find=j; ketemu=true; break; 
						}
					}
					if(ketemu){
						int lsle_id = ((BigDecimal) ((HashMap) lds_prod.get((int)ll_find)).get("LSLE_ID")).intValue(); 
						if(lsle_id != maxCount){
							li_persen[i] += li_or[k-1];
							k++;
						}
					}
					
					ll_find++;
				}while(ll_find <= maxCount);
			}			
			
			li_bisnis = FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 3);
			ldec_premi = this.uwDao.selectPremiTopUpUnitLink(spaj, premi_ke);
			
			List detBisnis = uwDao.selectDetailBisnis(spaj);
			String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
			
			int bulanProd = Integer.parseInt(uwDao.selectBulanProduksi(spaj));
			
			for(i = 0; i<lds_kom.size(); i++){
				Commission komisi = (Commission) lds_kom.get(i);
				komisi.setLsbs_linebus(bacDao.selectLineBusLstBisnis(komisi.getBisnis_id().toString()));
				if(i==0) ldec_premi *= ldec_kurs;
				
				komisi.setFlag_mess(Boolean.TRUE);
				int li_cb_id = komisi.getCb_id();
				int li_cb_id_asli = komisi.getCb_asli();
				if(!products.progressiveLink(komisi.getBisnis_id().toString()) && !products.stableSavePremiBulanan(komisi.getBisnis_id().toString()) && !products.healthProductStandAlone(komisi.getBisnis_id().toString())){
					if( li_cb_id==1 || li_cb_id==2 || li_cb_id==6 ) komisi.setCb_id(3);
				}
				komisi.setCb_id(new Integer(li_cb_id));
				if(komisi.getBisnis_id() == 64 ) komisi.setBisnis_id(new Integer(56));
				else if(komisi.getBisnis_id() == 67 ) komisi.setBisnis_id(new Integer(54));
				
				if("37,52".indexOf(lca_id)>-1){ //Agency) { //37 agency, 46 arthamas, 52 Agency Arthamas (Deddy 22 Feb 2012)
					komisi.setLsco_jenis(new Integer(3)); //Commission agency system
				}else if(lca_id.equals("46")) {
					komisi.setLsco_jenis(new Integer(4)); //Commission Arthamas
				}
				
				getLamaTanggung(komisi);
				
				if(f_get_komisi(komisi, errors, 0, premi_ke, komisi.getKurs_id(), ldec_premi, maxCount)==true){
					ldec_pct_kom[maxCount] = komisi.getKomisi();
				}else {
					throw new RuntimeException("Harap cek perhitungan topup komisi agency system");
				}
				
				if(komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
					
					//Yusuf - 1/5/08 - stable link
					if(products.stableLink(String.valueOf(li_bisnis)) && i>0) { //OR stable link hanya ada sept - dec 2008
						if(bulanProd >= 200809 && bulanProd <= 200812) {
							komisi.setKomisi(new Double(li_persen[i] * 0.1)); //hanya 10% dari OR normal
						}else {
							komisi.setKomisi(0.); //selain sept - dec, tidak ada OR
						}
					}else if(products.stableLink(String.valueOf(li_bisnis)) && i==0) {
						List<Map> stable = uwDao.selectInfoStableLink(komisi.getReg_spaj());
						Double totalPremi = uwDao.selectTotalPremiNewBusiness(komisi.getReg_spaj());
						
						//stable link
						// bonus promosi stable link, masuk ke comm_bonus juga gak? gak tau deh
						if(li_bisnis.equals("164")) {
							for(Map m : stable) {
								int msl_no = ((BigDecimal) m.get("MSL_NO")).intValue();
								if(premi_ke == msl_no) {
									int mgi = ((BigDecimal) m.get("MSL_MGI")).intValue();
									double bonus_promosi = (double) 0;
									Date bdate = (Date) m.get("MSL_BDATE");
									if(bulanProd>=201105){
										if(mgi == 1) {
											komisi.setKomisi(0.05);
										}else if(mgi == 3) {
											komisi.setKomisi(0.15);
										}else if(mgi == 6) {
											komisi.setKomisi(0.3);
										}else if(mgi == 12) {
											komisi.setKomisi(0.6);
										}else if(mgi == 24) {
											komisi.setKomisi(1.2);
										}else if(mgi == 36) {
											komisi.setKomisi(1.35);
										}else {
											throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
										}
									}else{
										if(bdate.compareTo(defaultDateFormat.parse("01/01/2009")) < 0) {
											if(mgi == 1) {
												bonus_promosi = 0.033;
											}else if(mgi == 3) {
												bonus_promosi = 0.1;
											}else if(mgi == 6) {
												bonus_promosi = 0.2;
											}else if(mgi == 12) {
												bonus_promosi = 0.4;
											}else if(mgi == 24) {
												bonus_promosi = 0.8;
											}else if(mgi == 36) {
												bonus_promosi = 1.2;
											}
										}
										//perhitungan bonus penjualan, dihitung dari TOTAL PREMI bukan dari PREMI POKOK saja
										if(mgi == 1) {
											komisi.setKomisi(0.017 + bonus_promosi);
										}else if(mgi == 3) {
											komisi.setKomisi(0.05 + bonus_promosi);
										}else if(mgi == 6) {
											komisi.setKomisi(0.1 + bonus_promosi);
										}else if(mgi == 12) {
											komisi.setKomisi(0.2 + bonus_promosi);
										}else if(mgi == 24) {
											komisi.setKomisi(0.4 + bonus_promosi);
										}else if(mgi == 36) {
											komisi.setKomisi(0.6 + bonus_promosi);
										}else {
											throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
										}
									}
									break;
								}
							}
							
						//stable link syariah
						}else if(li_bisnis.equals("174")) {
							for(Map m : stable) {
								int msl_no = ((BigDecimal) m.get("MSL_NO")).intValue();
								if(premi_ke == msl_no) {
									int mgi = ((BigDecimal) m.get("MSL_MGI")).intValue();
									double bonus_promosi = (double) 0;
									Date bdate = (Date) m.get("MSL_BDATE");
									//perhitungan bonus penjualan, dihitung dari TOTAL PREMI bukan dari PREMI POKOK saja
									if(mgi == 1) {
										komisi.setKomisi(0.05);
									}else if(mgi == 3) {
										komisi.setKomisi(0.15);
									}else if(mgi == 6) {
										komisi.setKomisi(0.3);
									}else if(mgi == 12) {
										komisi.setKomisi(0.6);
									}else if(mgi == 24) {
										komisi.setKomisi(1.2);
									}else if(mgi == 36) {
										komisi.setKomisi(1.35);
									}else {
										throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
									}
									
									break;
								}
							}
						}
					}else {
						komisi.setKomisi(new Double(ldec_pct_kom[komisi.getLev_kom()]));
						if( i > 0) komisi.setKomisi(new Double(li_persen[i]));
					}
					ldec_komisi = (ldec_premi * komisi.getKomisi() / 100);

					//TAMBAHAN OLEH YUSUF UNTUK PRODUK2 SINGLE (IM No. 132/IM-DIR/XII/2007) - 16/01/2008
					ldec_komisi = cekLinkSingle(spaj, ldec_komisi, komisi.getLev_kom(), true);
					
					Date sysdate = commonDao.selectSysdateTruncated(0);
					if(komisi.getAgent_id().equals("500001"))komisi.setTax(new Double(ldec_komisi * 0.15));
					else{
						komisi.setTax(new Double(ldec_komisi * komisi.getTax() / 100));
						if(komisi.getTax()>0) komisi.setTax(f_load_tax(new Double(ldec_komisi), sysdate, komisi.getAgent_id()));
					}
					
					persen = komisi.getKomisi();
					komisi.setTax(FormatNumber.rounding(komisi.getTax(), true, 25));

					ldec_komisi = FormatNumber.round(new Double(ldec_komisi), 0);
					if(komisi.getTax()==null)komisi.setTax(new Double(0));
					komisi.setCo_id(sequenceMst_commission(11));
					komisi.setReg_spaj(spaj);
					komisi.setKomisi(new Double(ldec_komisi));
					komisi.setNilai_kurs(ldec_kurs);
					komisi.setLus_id(currentUser.getLus_id());
					komisi.setMsbi_tahun_ke(new Integer(1));
					komisi.setMsbi_premi_ke(premi_ke);
					
					//Yusuf - 5/1/09 - Mulai 2009, struktur komisi arthamas ada 6 tingkat, bukan 4 lagi, tapi diinsertnya bukan dari 6, tetep dari 4
					if(Integer.parseInt(prodDate) > 2008 && lca_id.equals("46")) {
						if(komisi.getLev_kom().intValue() > 2) komisi.setLev_kom(komisi.getLev_kom().intValue() - 2);
						else komisi.setLev_kom(0);
					}
					
					//(Yusuf - 27/12/2006) untuk agency system, sekarang ada reward 10% -> SK No. 110/EL-SK/XII/2006
					if(i==0 && Integer.parseInt(prodDate) > 2006) {
						String rekruter_id = uwDao.selectRekruterAgencySystem(komisi.getAgent_id()); //tarik rekruternya
						boolean isReward = false;
						if("37,52".indexOf(lca_id)>-1){ //Agency)  //37 agency, 52 Agency Arthamas (Deddy 22 Feb 2012)
							isReward = true;
						}else if(lca_id.equals("46")) { //arthamas
							isReward = false;
						}else if(lca_id.equals("42")) { //worksite
							isReward = false;
						}

						//Yusuf - 12 Sep 08 - Stable Link agency dan regional ada reward khusus sept - dec, selain itu tidak ada
						if(products.stableLink(komisi.getBisnis_id().toString())) {
							isReward = false;
							if("08,09,42,40,46".indexOf(lca_id) == -1 && bulanProd >= 200809 && bulanProd <= 200812) {
								isReward = true;
							}
						}
						
						if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())) {
							isReward = false;
						}

						if(rekruter_id!=null && isReward) {//untuk newbusiness, hanya kalo ada recruiter, baru reward
							Rekruter rekruter = uwDao.selectRekruterFromAgen(rekruter_id); //tarik rekruter dari mst_agent, ada gak?, jenis=3
							if(rekruter==null) rekruter = uwDao.selectRekruterFromAgenSys(rekruter_id); //kalo gak ada, tarik dari mst_agensys, jenis=4
							if(rekruter!=null && !products.powerSave(komisi.getBisnis_id().toString()) && isReward) { //kalo ada, baru insert rewardnya 10%
								if(rekruter.getMsag_bay() == null) rekruter.setMsag_bay(0);
								if(rekruter.getSts_aktif().intValue() == 1  && rekruter.getMsag_bay().intValue() != 1) { //hanya bila rekruternya aktif baru dikasih reward
									//cek dulu, ada nomer rekeningnya gak
									if(rekruter.getNo_tab()==null) {
										errors.reject("payment.noRecruiter", new Object[] {rekruter.getAgent_id()}, "-");
										return false;
									} else if(rekruter.getNo_tab().trim().equals("")) {
										errors.reject("payment.noRecruiter", new Object[] {rekruter.getAgent_id()}, "-");
										return false;
									} else {
										rekruter.setNo_tab(StringUtils.replace(rekruter.getNo_tab(), "-",""));
										
										if(rekruter.getMsag_bay() == null) rekruter.setMsag_bay(0);
										
										double pengali = 1;
										
										//Yusuf (22/12/2010) bila business partner, maka dapat reward hanya 5%
										if(rekruter.getLcalwk().startsWith("61")) pengali = 0.5;
										//Yusuf (23/03/09) - Per 1 April OR dan Reward tidak 35% lagi tapi full
										else if(isLinkSingle(spaj, false) && bulanProd < 200904) pengali = 0.35;
										else if(isEkalink88Plus(spaj, true)) pengali = 0.7;
										else if(products.stableLink(komisi.getBisnis_id().toString())) pengali = 0.1;
										
										double reward = FormatNumber.round((ldec_komisi * 0.1 * pengali),  0);
										if(rekruter.getMsag_komisi()!=null){
											if(rekruter.getMsag_komisi().toString().equals("1") && rekruter.getMsag_bay().intValue() != 1){
												List<Billing> billing = akseptasiDao.selectMstBilling(spaj, 1, premi_ke);
												if(billing.get(0).getLspd_id() == 99){
													Date reward_pay_date = commonDao.selectSysdateTruncated(1);
													String msco_no = this.uwDao.selectMscoNoByMonth(rekruter_id.trim(), "092014");
													if(Common.isEmpty(msco_no)){
														msco_no = sequence.sequenceMscoNoCommission(101, "01");
													}
													this.uwDao.insertMst_rewardKetinggalan(spaj, 1, premi_ke, rekruter.getJenis_rekrut(), rekruter_id,
															rekruter.getNama(), rekruter.getNo_tab(), 
															rekruter.getLbn_id().toString(), reward, 
															FormatNumber.rounding(f_load_tax(reward, sysdate, rekruter_id), true, 25), new Double(ldec_kurs), currentUser.getLus_id(), komisi.getLsbs_linebus(),reward_pay_date,reward_pay_date,msco_no);
												}else{
													this.uwDao.insertMst_reward(spaj, 1, premi_ke, rekruter.getJenis_rekrut(), rekruter_id,
															rekruter.getNama(), rekruter.getNo_tab(), 
															rekruter.getLbn_id().toString(), reward, FormatNumber.rounding(f_load_tax(reward, sysdate, rekruter_id), true, 25), new Double(ldec_kurs), currentUser.getLus_id(), komisi.getLsbs_linebus());
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return true;
	}
	
	public boolean prosesRewardTopUpIndividu(String spaj, BindException errors, User currentUser, Integer premi_ke) throws Exception{ //of_komisi_tu_new()
		if(logger.isDebugEnabled())logger.debug("PROSES: prosesRewardTopUpIndividu (of_komisi_tu_new())");
		
		List lds_kom;
		int i; double ldec_premi; double ldec_komisi=0; Double ldec_kurs = new Double(1); 
		double[] ldec_pct_kom = {0,3, 8.5, 22.5, 2};String ls_region; String ls_jenis;
		String ls_agent_rekruter; String ls_acct; String ls_nama; 
		String li_bisnis; int li_rekrut=0;
		Date ldt_tgl_aktif; Date ldt_prod; 
		Map rekruter = new HashMap();
		
		Map pribadi = this.uwDao.selectInfoPribadi(spaj);
		ls_region = (String) pribadi.get("REGION");
		String lca_id = ls_region.substring(0,2); 
		if("37,46,52".indexOf(lca_id)>-1){ //Agency) { //37 agency, 46 arthamas, 52 Agency Arthamas (Deddy 22 Feb 2012)
			Pemegang pp = bacDao.selectpp(spaj);
			if ((Integer) pp.getMsag_asnew()==1){
				return true;
			}else{
				return prosesRewardTopUpAgencySystem(spaj, errors, currentUser, premi_ke, lca_id);
			}
		}else if(ls_region.substring(0,2).equals("42")) ldec_pct_kom = new double[]{0, 0, 5, 10, 2};
		
		lds_kom = this.uwDao.selectViewKomisiAsli(spaj);
		li_bisnis = FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 3);
		ldec_premi = this.uwDao.selectPremiTopUpUnitLink(spaj, premi_ke);
		int bulanProd = Integer.valueOf(uwDao.selectBulanProduksi(spaj));
		
		if("087, 101, 115, 152".indexOf(li_bisnis) > -1) 
			ldec_pct_kom[4] = 0.75;
		else if("116, 119, 140, 153, 159, 160, 199,218".indexOf(li_bisnis) > -1) 
			ldec_pct_kom[4] = 1.5;
		else if(products.cerdas(li_bisnis)) 
			ldec_pct_kom[4] = 1;
		else if(isEkalink88Plus(spaj, true)) {
			ldec_pct_kom[4] = 2;
		}else if(isLinkSingle(spaj, true)) {
			ldec_pct_kom[4] = 3;
		}

		if(!lds_kom.isEmpty()) {
			Commission tmp = (Commission) lds_kom.get(0);
			if(tmp.getKurs_id().equals("02")){
				ldec_kurs = new Double(this.uwDao.selectLastCurrency(spaj, 1, 1));
				if(ldec_kurs==null){
					errors.reject("payment.noLastCurrency");
					throw new Exception(errors);
				}
			}
			
			for(i = 0; i<lds_kom.size(); i++){
				Commission komisi = (Commission) lds_kom.get(i);
				komisi.setLsbs_linebus(bacDao.selectLineBusLstBisnis(komisi.getBisnis_id().toString()));
				if(i==0) ldec_premi *= ldec_kurs;
				if(komisi.getLev_kom()==4){
					rekruter = this.uwDao.selectRekruter(komisi.getAgent_id());
					if(rekruter!=null){
						ls_jenis = (String) rekruter.get("MSRK_JENIS_REKRUT");
						
	//					 O.R. 50%
						if(ls_jenis.equals("3") || ls_jenis.equals("4"))
							li_rekrut=2;
						ldt_tgl_aktif = this.uwDao.selectAgentActiveDate(komisi.getAgent_id());
						//Untuk tambang emas th 2004, kalo masih aktif; or 50% semua, kalo tdk um 12.5, 10% reward buat um terakhir
						// u/ tambang emas baru per 1 mar 05, or um = 12.5%, yg lain sama
						
						if(ldt_tgl_aktif.compareTo(defaultDateFormatReversed.parse("20050301"))>=0){
							li_rekrut = 3;
						}else if(FormatDate.dateDifference(ldt_tgl_aktif, komisi.getTgl_kom(), true)<=365) {
							//te 2004, masih berlaku, or 50% semua
							li_rekrut = 2;
						}
						// li_rekrut = 0 -> tdk aktif
						// li_rekrut = 1 -> um 12.5%, reward um 10%
						// li_rekrut = 2 -> or = 50%
						// li_rekrut = 3 -> um 12.5%, reward rekruter 10%
					}
				}
				
				//Kalo masih aktif
				if(komisi.getSts_aktif()==1 && komisi.getFlag_komisi()==1){
					//TAMBAHAN OLEH YUSUF UNTUK PRODUK2 SINGLE (IM No. 132/IM-DIR/XII/2007) - 16/01/2008
					ldec_komisi = cekLinkSingle(spaj, ldec_komisi, komisi.getLev_kom(), true);
					//(Deddy) perhitungan rate komisi stabil link diambil lgsg dari lst_comm_new
					Commission temp = new Commission(); 
					PropertyUtils.copyProperties(temp, komisi);
					if(komisi.getBisnis_id().toString().equals("164")){//
						temp.setLsco_year(1);
						temp.setLsco_jenis(1);
						Date ldt_temp = this.uwDao.selectLastComissionDate(temp);
						Map commstabil = this.uwDao.selectCommisionAndBonus(ldt_temp, temp);
						if(commstabil!=null){
							if(commstabil.get("LSCO_COMM")!=null) komisi.setKomisi(Double.valueOf(commstabil.get("LSCO_COMM").toString()));
							if(commstabil.get("LSCO_BONUS")!=null) komisi.setBonus(Double.valueOf(commstabil.get("LSCO_BONUS").toString()));
						}
						Integer mgi = uwDao.selectMgiNewBusiness(spaj);
						if(bulanProd>=201105){
							if(mgi == 1) {
								komisi.setKomisi(0.05);
							}else if(mgi == 3) {
								komisi.setKomisi(0.15);
							}else if(mgi == 6) {
								komisi.setKomisi(0.3);  
							}else if(mgi == 12) {
								komisi.setKomisi(0.6);
							}else if(mgi == 24) {
								komisi.setKomisi(1.2);
							}else if(mgi == 36) {
								komisi.setKomisi(1.35);
							}else {
								throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
							}
						}else{
							if(mgi == 1) {
								komisi.setKomisi(0.017);
							}else if(mgi == 3) {
								komisi.setKomisi(0.05);
							}else if(mgi == 6) {
								komisi.setKomisi(0.1);  
							}else if(mgi == 12) {
								komisi.setKomisi(0.2);
							}else if(mgi == 24) {
								komisi.setKomisi(0.4);
							}else if(mgi == 36) {
								komisi.setKomisi(0.6);
							}else {
								throw new RuntimeException("Harap cek perhitungan komisi Stable Link, mgi = " + mgi);
							}
						}
						
					}else if(komisi.getBisnis_id().toString().equals("174")){
						Integer mgi = uwDao.selectMgiNewBusiness(spaj);
						if(mgi == 1) {
							komisi.setKomisi(0.05);
						}else if(mgi == 3) {
							komisi.setKomisi(0.15);
						}else if(mgi == 6) {
							komisi.setKomisi(0.3);
						}else if(mgi == 12) {
							komisi.setKomisi(0.6);
						}else if(mgi == 24) {
							komisi.setKomisi(1.2);
						}else if(mgi == 36) {
							komisi.setKomisi(1.35);
						}else {
							throw new RuntimeException("Harap cek perhitungan komisi Stable Link Syariah, mgi = " + mgi);
						}
					}else {
						komisi.setKomisi(new Double(ldec_pct_kom[komisi.getLev_kom()]));
					}
					
					
					//komisi OR agent get agent 28/01/04 (hm)
					if(i>0 && li_rekrut==2) komisi.setKomisi(new Double(komisi.getKomisi()/2));
					if(i==1 && li_rekrut==3) komisi.setKomisi(new Double(12.5));
					
					//stable link, ada or sept - dec 2008, seliain itu tidak ada
					
					if(komisi.getLev_kom().intValue() < 4 && products.stableLink(komisi.getBisnis_id().toString()) && (bulanProd < 200809 || bulanProd > 200812)) {
						komisi.setKomisi(0.);
					}						
					
					ldec_komisi = (ldec_premi * komisi.getKomisi() / 100);
					//logger.info("ldec_komisi="+ldec_komisi+"\nldec_premi="+ldec_premi+"\npct_kom="+komisi.getKomisi());
					komisi.setTax(new Double(ldec_komisi * komisi.getTax()/100));
					Date sysdate = commonDao.selectSysdateTruncated(0);
					if(komisi.getTax()>0)komisi.setTax(f_load_tax(new Double(ldec_komisi), sysdate, komisi.getAgent_id()));
					komisi.setTax(FormatNumber.rounding(komisi.getTax(), true, 25));
					ldec_komisi = FormatNumber.round(new Double(ldec_komisi),  0);
					if(komisi.getTax()==null)komisi.setTax(new Double(0));
					komisi.setCo_id(sequenceMst_commission(11));
					komisi.setReg_spaj(spaj);
					
					
					//referensi point(tambang emas) -> Top Up
					Integer cek = uwDao.seleckCekRef(spaj,"1");//1 = kode_program
					if(cek>0){
						String lsbs_id = uwDao.selectLsbsId(spaj);
						if(lca_id.equals("42") && lsbs_id.equals("140")){
							double komisi_ref = new Double(ldec_komisi)/2;
							komisi.setKomisi(ldec_komisi);//komisi tetap 100%
							Integer lt_id = uwDao.selectLtId(spaj,ldec_premi);
						}else{
							komisi.setKomisi(new Double(ldec_komisi));
						}
					}else{
						komisi.setKomisi(new Double(ldec_komisi));
					}
					//end referensi
						
					komisi.setKomisi(new Double(ldec_komisi));
					komisi.setNilai_kurs(ldec_kurs);
					komisi.setLus_id(currentUser.getLus_id());
					komisi.setMsbi_tahun_ke(new Integer(1));
					komisi.setMsbi_premi_ke(premi_ke);
				}
				
				//proses rewards
				if(i==1){
					double ldec_reward; double ldec_tax;
					boolean lb_ada_reward = false;
					if(li_rekrut==3){
						if(rekruter!=null) {
							if((rekruter.get("NO_ACCOUNT")==null || rekruter.get("NO_ACCOUNT").toString().equals(""))&& rekruter.get("MSRK_AKTIF").toString().equals("1")){
								errors.reject("payment.noRecruiter", new Object[] {rekruter.get("MSRK_ID")}, "-");
								throw new Exception(errors);
							}	
							if(rekruter.get("MSRK_AKTIF").toString().equals("1")){
								lb_ada_reward=true;
							}
						}
					}
					
					if(products.stableLink(komisi.getBisnis_id().toString())) {
						lb_ada_reward = false;
					}
					
					if(products.stableSave(komisi.getBisnis_id(), komisi.getBisnis_no())) {
						lb_ada_reward = false;
					}
					
					if(lca_id.equals("42")){
						lb_ada_reward = false;
					}
					
					if(lb_ada_reward){
						ldt_prod = this.uwDao.selectProductionDate(spaj, new Integer(1), new Integer(2));
						if(FormatDate.isDateBetween(ldt_prod, defaultDateFormatReversed.parse("20050401"), defaultDateFormatReversed.parse("20050630")))
							ldec_reward = ldec_premi * 0.2;
						else
							ldec_reward = ldec_premi * 0.1;
						
						Rekruter rek = uwDao.selectRekruterFromAgen(rekruter.get("MSRK_ID").toString());
						if(rek.getMsag_bay() == null) rek.setMsag_bay(0);
						
						double pengali = 1;
						
						//Yusuf (22/12/2010) bila business partner, maka dapat reward hanya 5%
						if(rek.getLcalwk().startsWith("61")) pengali = 0.5;
						else if(isLinkSingle(spaj, true) && bulanProd < 200904) pengali = 0.35;
						
						ldec_reward *= pengali;
						
						Date sysdate = commonDao.selectSysdateTruncated(0);
						ls_agent_rekruter = (String) rekruter.get("MSRK_ID");

						ldec_tax = f_load_tax(new Double(ldec_reward), sysdate, ls_agent_rekruter);
						ldec_reward = FormatNumber.round(new Double(ldec_reward),  0);
						ldec_tax = FormatNumber.rounding(new Double(ldec_tax), true, 25);
						
						ls_nama = (String) rekruter.get("MSRK_NAME");
						ls_jenis = (String) rekruter.get("MSRK_JENIS_REKRUT");
						ls_acct = StringUtils.replace(rekruter.get("NO_ACCOUNT").toString(), "-","");
						
						if(rekruter.get("MSAG_KOMISI")!=null && rek.getMsag_bay().intValue() != 1){
							if(rekruter.get("MSAG_KOMISI").toString().equals("1")){
								if(uwDao.selectbacCekAgen(ls_agent_rekruter)!=null){
									List<Billing> billing = akseptasiDao.selectMstBilling(spaj, 1, premi_ke);
									if(billing.get(0).getLspd_id() == 99){
										Date reward_pay_date = commonDao.selectSysdateTruncated(1);
										String msco_no = this.uwDao.selectMscoNoByMonth(ls_agent_rekruter.trim(), "092014");
										if(Common.isEmpty(msco_no)){
											msco_no = sequence.sequenceMscoNoCommission(101, "01");
										}
										this.uwDao.insertMst_rewardKetinggalan(spaj, 1, premi_ke, ls_jenis, ls_agent_rekruter,
												ls_nama, ls_acct, 
												rekruter.get("LBN_ID").toString(), new Double(ldec_reward), 
												new Double(ldec_tax), ldec_kurs, currentUser.getLus_id(), komisi.getLsbs_linebus(),reward_pay_date,reward_pay_date,msco_no);
									}else{
										this.uwDao.insertMst_reward(spaj, 1, premi_ke, ls_jenis, ls_agent_rekruter,
												ls_nama, ls_acct, 
												rekruter.get("LBN_ID").toString(), new Double(ldec_reward), 
												new Double(ldec_tax), ldec_kurs, currentUser.getLus_id(), komisi.getLsbs_linebus());
									}
									
								}
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	public boolean prosesKomisiErbePackageSystem(String spaj, User currentUser, BindException errors , int tahunKe , int premiKe , int reproc  ) throws Exception{
		Integer hasil=null;
		hasil =bacDao.prosesKomisiErbePackageSystem(spaj, tahunKe, premiKe,reproc);		
			if(hasil==0){
				errors.reject("Mohon Maaf, terjadi kesalahan pada saat proses kompensasi, Silakan hubungi IT.");
				throw new Exception(errors);
			}
		
		return true;
	}
}