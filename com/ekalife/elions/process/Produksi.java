package com.ekalife.elions.process;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;

import produk_asuransi.n_prod;

import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.Upp;
import com.ekalife.elions.model.User;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.cek_data;
import com.ekalife.utils.parent.ParentDao;

public class Produksi extends ParentDao {
	protected final Log logger = LogFactory.getLog( getClass() );

	private boolean wf_check_prod(String reg_spaj, int prod_ke){
		int li_return = 1;
		boolean lb_return = false;

		int lsbs = Integer.parseInt(uwDao.selectBusinessId(reg_spaj));
		int lscb = uwDao.selectCaraBayarFromSpaj(reg_spaj);
		
		if((lsbs == 183 || lsbs == 189 || lsbs == 193 ) && lscb == 6) li_return = 0;
		
		if(li_return == 1) return true;

		li_return = 0;

		li_return = uwDao.selectSumProd(reg_spaj, prod_ke);

		if(li_return == 12) return true;

		return lb_return;
	}
	
	public void prosesUlinkDetBill(String reg_spaj, Date prodDate) throws Exception{
		List ulinkDetBill = new ArrayList();
		String li_bisnis="";
		double total_premi=0;
		boolean lb_link = false;
		List billInfo = this.uwDao.selectBillingInfoForTransfer(reg_spaj, 1, 1);
		
		for(int i=0; i<billInfo.size(); i++){
			Map tmp = (HashMap) billInfo.get(i);
			Double premi = new Double(0);
			Double ld_msdb_premium = new Double(tmp.get("MSDB_PREMIUM").toString());
			String lsbs_id = FormatString.rpad("0", tmp.get("LSBS_ID").toString(), 3);
			if(Integer.valueOf(lsbs_id)<300) li_bisnis = lsbs_id;
			
			//BIAYA ASURANSI
			if(products.unitLinkNew(lsbs_id) || lsbs_id.equals("134") || lsbs_id.equals("166")){ //unitlinknew / platinumlink / amanah link (Yusuf 03/01/2008)
				lb_link = true;
				premi = this.uwDao.selectBiayaUlink(reg_spaj, 1, 2);
				if(premi==null) premi= new Double(0);
				total_premi += premi.doubleValue();
				ulinkDetBill = tambahUlinkDetBill(ulinkDetBill, reg_spaj, new Integer(1), new Integer(1), lsbs_id, tmp.get("LSDBS_NUMBER").toString(), premi);
			}else if(Integer.valueOf(lsbs_id)>300 && Integer.valueOf(lsbs_id)<900 && !li_bisnis.equals("134") && !li_bisnis.equals("166")){
				premi = this.uwDao.selectBiayaUlinkRider(reg_spaj, lsbs_id, tmp.get("LSDBS_NUMBER").toString());
				if(premi==null) premi= new Double(0);
				else ld_msdb_premium = premi;
				total_premi += premi.doubleValue();
				ulinkDetBill = tambahUlinkDetBill(ulinkDetBill, reg_spaj, new Integer(1), new Integer(1), lsbs_id, tmp.get("LSDBS_NUMBER").toString(), premi);
			}
//			if(Integer.valueOf(lsbs_id)>300) {
//				this.uwDao.insertMst_detail_production(reg_spaj, prodDate, lsbs_id, 
//						tmp.get("LSDBS_NUMBER").toString(), new Integer(1), new Integer(1), new Integer(1), 
//						ld_msdb_premium,
//						new Double(tmp.get("MSDB_DISCOUNT").toString()), 
//						new Double(tmp.get("MSPR_TSI").toString()));
//			}
		}
		
		if(lb_link){
			//BIAYA ADMINISTRASI
			Double premi = this.uwDao.selectBiayaUlink(reg_spaj, 1, 3);
			if(premi==null) premi= new Double(0);
			total_premi += premi.doubleValue();
			ulinkDetBill = tambahUlinkDetBill(ulinkDetBill, reg_spaj, new Integer(1), new Integer(1), "899", "1", premi);
			
			//BIAYA POKOK untuk PLATINUM LINK dan AMANAH LINK - Yusuf 03/01/2008
			if(li_bisnis.equals("134") || li_bisnis.equals("166") || li_bisnis.equals("213")) {
				Double foundationFee = this.uwDao.selectBiayaUlink(reg_spaj, 1, 12);
				if(foundationFee==null) foundationFee= new Double(0);
				total_premi = total_premi + foundationFee.doubleValue();
				ulinkDetBill = tambahUlinkDetBill(ulinkDetBill, reg_spaj, new Integer(1), new Integer(1), "898", "1", foundationFee);
			}
			
			//rekap billing ulink (tagihan bulanan)
			Date ldt_bdate = (Date) ((HashMap)billInfo.get(0)).get("MSBI_BEG_DATE");
			Date ldt_next = uwDao.selectNextBill(ldt_bdate);
			
			//gak perlu pake hitungcekendbill (Yusuf - 14/09/2006)
			//Integer li_ins_period = (Integer) ((HashMap)billInfo.get(0)).get("MSPO_INS_PERIOD");
			//ldt_next = transferPolis.hitungCekEndBill(ldt_next, ldt_bdate, li_ins_period * 12, errors); 

			//tgl_prod diganti dari prodDate ke ldt_bdate (request dari regina, yusuf - 05/09/2006)
			this.uwDao.insertMst_ulink_bill(reg_spaj, new Integer(1), new Integer(1), ldt_bdate, new Integer(1), 
					ldt_bdate, new Integer(0), new Integer(1), ldt_next, "0", new Integer(1), new Double(total_premi));

			//insert detail ulink bill
			for(int i=0; i<ulinkDetBill.size(); i++) {
				this.uwDao.insertMst_ulink_det_billing((Map) ulinkDetBill.get(i));
			}
			
			// *Andhika(28/01/2013) - Apa bila Produk Syariah dan premi potong > 0 maka update colom tabarru dan ujrah
			Integer syariah = uwDao.selectSyariah(reg_spaj);
			if(syariah == null){
				syariah = 0;
			}
			
			if(syariah != 0){
				List UlinkBill = this.uwDao.selectMst_Ulink_Bill(reg_spaj);
			
				for(int i=0; i<UlinkBill.size(); i++){
					Map temp = (HashMap) UlinkBill.get(i);
					Integer tahun = (Integer) temp.get("TAHUN");
					Integer pot_ke = (Integer) temp.get("POT_KE");
			
					List InfoTabaru = this.uwDao.selectInfoTabaru(reg_spaj, tahun, pot_ke);
						Map tu = (HashMap) InfoTabaru.get(0);
							Double tabarru = new Double(tu.get("TABARRU").toString());
							Double ujrah = new Double(tu.get("UJRAH").toString());
			
							if(total_premi > 0){
								this.uwDao.updateMst_Ulink_Bill(reg_spaj, new Integer(1), new Integer(1), tabarru, ujrah);
							}
				}
			}
//-----						
		}
	}
	
	public BindException prosesProduksi(String reg_spaj, BindException errors, int bulanRK, List billInfo, User currentUser, String businessId, n_prod produk, ElionsManager elionsManager, UwManager uwManager) throws Exception{
		if(logger.isDebugEnabled())logger.debug("PROSES: prosesProduksi");
	
		String lca_id = uwDao.selectCabangFromSpaj(reg_spaj);
		String lsdbs = uwDao.selectLsdbsNumber(reg_spaj);		
		boolean lb_link = false;
		String s_agent_1="";
		String s_agent_2="";
		String s_agent_3="";
		String s_agent_4="";
		String s_agent_5="";
		//Date defaultDate = this.uwDao.selectMst_default(1);
		Date inputDate = this.commonDao.selectSysdateTruncated(0);
		List topUpList = this.uwDao.selectTopUp(reg_spaj, 1, 1, "desc");
		ArrayList agentProd=Common.serializableList(this.uwDao.selectAgentsFromSpaj(reg_spaj));
		HashMap<String,Object> mapAgent=new HashMap<String,Object>();
		for(int i=0;i<agentProd.size();i++){
			mapAgent= (HashMap<String, Object>)agentProd.get(i);
			BigDecimal lsle_id= (BigDecimal) mapAgent.get("LSLE_ID");
			Integer flag=lsle_id.intValue();
			if(agentProd.size()<5){
				if(flag==4){
					s_agent_4=(String) mapAgent.get("MSAG_ID");
				}else if(flag==3){
					s_agent_3=(String) mapAgent.get("MSAG_ID");
				}else if(flag==2){
					s_agent_2=(String) mapAgent.get("MSAG_ID");
				}else if(flag==1){
					s_agent_1=(String) mapAgent.get("MSAG_ID");
				}
					
			}else{
				if(flag==5){
					s_agent_5=(String) mapAgent.get("MSAG_ID");
				}else if(flag==4){
					s_agent_4=(String) mapAgent.get("MSAG_ID");
				}else if(flag==3){
					s_agent_3=(String) mapAgent.get("MSAG_ID");
				}else if(flag==2){
					s_agent_2=(String) mapAgent.get("MSAG_ID");
				}else if(flag==1){
					s_agent_1=(String) mapAgent.get("MSAG_ID");
				}
			}
				
		}		
		
		if(topUpList.size()==0 && (!(businessId.equals("217") && lsdbs.equals("2")))) { //ERBE Package belum ada Payment jadi lewati validasi ini
			errors.reject("payment.rkFailed");
			throw new Exception(errors);
		}
//		Date rkDate = ((TopUp) topUpList.get(0)).getMspa_date_book(); 
		String param = "individu";
		if(products.unitLinkNew(businessId)){
			param = "ulink";
		}
		Date rkDate = this.uwDao.selectMaxRkDate(reg_spaj, 1, 1, param);
		
		Date prodDate = inputDate;
		//Date rkLastDate = this.uwDao.selectMst_default(15);
		
		Map map = (HashMap) billInfo.get(0);
		String lku_id = map.get("LKU_ID").toString(); 
		
		if(!lku_id.equals("01") && (!(businessId.equals("217") && lsdbs.equals("2"))) ){ //ERBE Package belum ada Payment jadi lewati validasi ini
			if(this.uwDao.validationDailyCurrency(lku_id, rkDate)==0){
				errors.reject("payment.noDailyCurrency");
				throw new Exception(errors);
			}
		}

		
		//Yusuf - Start 28 Aug 2009, tgl produksi menggunakan procedure di oracle -> eka.get_tgl_prod(:tglrk, :cab_mst_policy)
		Integer jenis = 1;
		if((businessId.equals("190") && lsdbs.equals("9")) || (businessId.equals("200") && lsdbs.equals("7"))) jenis = 3;
		prodDate = commonDao.selectTanggalProduksiUntukProsesProduksi(rkDate, lca_id, jenis);
		if(bulanRK==2){
			prodDate = FormatDate.add(prodDate, Calendar.DATE, 1);
		}
		
		if (businessId.equals("217") && lsdbs.equals("2")){ //tgl produksi untuk Produk ERBE Package = sysdate karena blom ada tgl RK
			prodDate = this.commonDao.selectSysdateTruncated(0);
			rkDate = prodDate;
			//Mark Valentino 20190830 - Perbaikan tanggal produksi (Request Tim PB)
			prodDate = commonDao.selectTanggalProduksiUntukProsesProduksi(rkDate, "73", 1);
			rkDate = prodDate;
		}else if(prodDate == null){
			errors.reject("", "Harap cek fungsi selectTanggalProduksiUntukProsesProduksi (return NULL)");
			throw new Exception(errors);
		}
	
		/** full autosales project start **/
		String flagAutoSalesCek = bacDao.selectFullAutoSalesFromMstPolicy(reg_spaj);
		
		if (flagAutoSalesCek != null && flagAutoSalesCek.equals("4")){
			prodDate = commonDao.selectTanggalProduksiUntukProsesProduksi(rkDate, lca_id, 4);
			rkDate = prodDate;
		}
		/** full autosales project end **/
		
		/*
		//bila tgl RK <= tgl produksi di mst default
		if(rkDate.compareTo(defaultDate)<=0){
			//bila tgl input >= tgl produksi di mst default
			if(inputDate.compareTo(defaultDate)>=0){
				//tgl produksi = tgl produksi di mst_default
				prodDate = defaultDate;
			}
		//bila tgl RK > tgl produksi di mst default
		}else{
			prodDate = defaultDate;
			
			//bila tgl RK > tgl RK terakhir
			if(rkDate.after(rkLastDate)){
				//tgl prod = tgl RK
				prodDate = rkDate;
			}else{
				//khusus bancass, gak usah tanya ini itu
				String lca_id = uwDao.selectCabangFromSpaj(reg_spaj);
				
				if(lca_id.equals("09")){
					prodDate = inputDate;
				}else{
					if(bulanRK==0){
//						errors.reject("payment.confirmRK", new Object[]{defaultDateFormat.format(prodDate), defaultDateFormat.format(rkLastDate), defaultDateFormat.format(rkDate)}, "Tgl Produksi: {0}<br>- Tgl RK Terakhir: {1}<br>- Tgl RK Polis ini: {2}.");
//						throw new Exception(errors);
					}else if(bulanRK==2){
						prodDate = rkDate;
					}
				}
			}
		}
		*/
		
		//Yusuf - 2 Aug 2011 - Tambahan validasi agar tidak insert production double!!!
		int count =  uwDao.selectValidasiProduksiDouble(reg_spaj, 1);
		if(count > 0){
			errors.reject("", "(Yusuf) Harap cek datanya karena PRODUKSI DOUBLE!!!");
			throw new Exception(errors);
		}
		
		this.uwDao.insertMst_production(rkDate, prodDate, reg_spaj, 1, 1, 1, 0,s_agent_1,s_agent_2,s_agent_3,s_agent_4,s_agent_5);
		
		//update tgl produksi di slink dengan tanggal sysdate
		this.bacDao.updateProdDateSlink(reg_spaj,prodDate);
		
		//update tanggal next premium date dengan tgl produksi 
		//(cek dulu msre_last_policy_age=0 ada di query updatenya)
		this.uwDao.updateMstReinsMsreNextPrmDate(reg_spaj,prodDate,1);
		
		List ulinkDetBill = new ArrayList();
		String li_bisnis="";
		double total_premi=0;
		List<Product> listProductExtra = uwDao.selectMstProductInsuredExtra(reg_spaj);
		
		for(int i=0; i<billInfo.size(); i++){
			Map tmp = (HashMap) billInfo.get(i);
			Double premi = new Double(0);
			Double ld_msdb_premium = new Double(tmp.get("MSDB_PREMIUM").toString());
			String lsbs_id = FormatString.rpad("0", tmp.get("LSBS_ID").toString(), 3);
			String lsdbs_number = FormatString.rpad("0", tmp.get("LSDBS_NUMBER").toString(), 3);
			if(Integer.valueOf(lsbs_id)<300) li_bisnis = lsbs_id;
			
			//BIAYA ASURANSI
			if(products.unitLinkNew(lsbs_id) || lsbs_id.equals("134") || lsbs_id.equals("166")){ //unitlinknew / platinumlink / amanahlink (Yusuf 03/01/2008)
				lb_link = true;
				premi = this.uwDao.selectBiayaUlink(reg_spaj, 1, 2);
				if(premi==null) premi= new Double(0);
				total_premi += premi.doubleValue();
				ulinkDetBill = tambahUlinkDetBill(ulinkDetBill, reg_spaj, new Integer(1), new Integer(1), lsbs_id, tmp.get("LSDBS_NUMBER").toString(), premi);
			}else if(Integer.valueOf(lsbs_id)>300 && Integer.valueOf(lsbs_id)<900 && !li_bisnis.equals("134") && !li_bisnis.equals("166")){
				premi = this.uwDao.selectBiayaUlinkRider(reg_spaj, lsbs_id, tmp.get("LSDBS_NUMBER").toString());
				if(premi==null) premi= new Double(0);
				else ld_msdb_premium = premi;
				total_premi += premi.doubleValue();
				ulinkDetBill = tambahUlinkDetBill(ulinkDetBill, reg_spaj, new Integer(1), new Integer(1), lsbs_id, tmp.get("LSDBS_NUMBER").toString(), premi);
			}else if(Integer.valueOf(lsbs_id)>=900 && lca_id.equals("42") && products.unitLink(lsbs_id)){
				if(listProductExtra.size()>0){
					Double premiExtra = 0.0;
					for(int j=0; j<listProductExtra.size();j++){
						Product productExtra = (Product) listProductExtra.get(j);
						premiExtra += productExtra.getMspr_premium();
					}
					total_premi += premiExtra.doubleValue();
					ulinkDetBill = tambahUlinkDetBill(ulinkDetBill, reg_spaj, new Integer(1), new Integer(1), lsbs_id, tmp.get("LSDBS_NUMBER").toString(), premi);
				}
			}
			this.uwDao.insertMst_detail_production(reg_spaj, prodDate, lsbs_id, 
					tmp.get("LSDBS_NUMBER").toString(), new Integer(1), new Integer(1), new Integer(1), 
					ld_msdb_premium,
					new Double(tmp.get("MSDB_DISCOUNT").toString()), 
					new Double(tmp.get("MSPR_TSI").toString()));
			
			//referensi point(tambang emas) -> Premi Pokok
			logger.info("Referensi");
			Integer cek = uwDao.seleckCekRef(reg_spaj,"2");
			if(cek>0){
				cek_data ck = new cek_data();
				int point = ck.getPoint(ld_msdb_premium,Integer.valueOf(lsbs_id),Integer.valueOf(lsdbs_number), null, null);
				//String id_trx = StringUtils.leftPad(bacDao.selectIdTrx(null,null,"3"), 10, '0');
				String id_trx = bacDao.selectIdTrx(null,reg_spaj,"2");
				bacDao.insertPwrDTrx(id_trx, props.getProperty("id.item.point"), reg_spaj, ld_msdb_premium, point, currentUser.getLus_id());
			}
			//end referensi
		}
		
		if(lb_link){
			//BIAYA ADMINISTRASI
			Double premi = this.uwDao.selectBiayaUlink(reg_spaj, 1, 3);
			if(premi==null) premi= new Double(0);
			total_premi += premi.doubleValue();
			ulinkDetBill = tambahUlinkDetBill(ulinkDetBill, reg_spaj, new Integer(1), new Integer(1), "899", "1", premi);
			
			//BIAYA POKOK untuk PLATINUM LINK dan AMANAH LINK
			if(li_bisnis.equals("134") || li_bisnis.equals("166") || li_bisnis.equals("213")) { //(Yusuf 03/01/2008)
				Double foundationFee = this.uwDao.selectBiayaUlink(reg_spaj, 1, 12);
				if(foundationFee==null) foundationFee= new Double(0);
				total_premi = total_premi + foundationFee.doubleValue();
				ulinkDetBill = tambahUlinkDetBill(ulinkDetBill, reg_spaj, new Integer(1), new Integer(1), "898", "1", foundationFee);
			}
			
			//rekap billing ulink (tagihan bulanan)
			Date ldt_bdate = (Date) ((HashMap)billInfo.get(0)).get("MSBI_BEG_DATE");
			Date ldt_next = uwDao.selectNextBill(ldt_bdate);
			
			//gak perlu pake hitungcekendbill (Yusuf - 14/09/2006)
			//Integer li_ins_period = (Integer) ((HashMap)billInfo.get(0)).get("MSPO_INS_PERIOD");
			//ldt_next = transferPolis.hitungCekEndBill(ldt_next, ldt_bdate, li_ins_period * 12, errors); 

			//tgl_prod diganti dari prodDate ke ldt_bdate (request dari regina, yusuf - 05/09/2006)
			this.uwDao.insertMst_ulink_bill(reg_spaj, new Integer(1), new Integer(1), ldt_bdate, new Integer(1), 
					ldt_bdate, new Integer(0), new Integer(1), ldt_next, currentUser.getLus_id(), new Integer(1), new Double(total_premi));

			//insert detail ulink bill
			for(int i=0; i<ulinkDetBill.size(); i++) {
				this.uwDao.insertMst_ulink_det_billing((Map) ulinkDetBill.get(i));
			}
			
			// *Andhika(28/01/2013) - Apa bila Produk Syariah dan premi potong > 0 maka update colom tabarru dan ujrah
			Integer syariah = uwDao.selectSyariah(reg_spaj);
			if(syariah == null){
				syariah = 0;
			}
			
			if(syariah != 0){
				List UlinkBill = this.uwDao.selectMst_Ulink_Bill(reg_spaj);
			
				for(int i=0; i<UlinkBill.size(); i++){
					Map temp = (HashMap) UlinkBill.get(i);
					Integer tahun = (Integer) temp.get("TAHUN");
					Integer pot_ke = (Integer) temp.get("POT_KE");
			
					List InfoTabaru = this.uwDao.selectInfoTabaru(reg_spaj, 1, 1);
						Map tu = (HashMap) InfoTabaru.get(0);
							Double tabarru = (Double) tu.get("TABARRU");
							Double ujrah = (Double) tu.get("UJRAH");
							
						if(syariah != 0 && total_premi > 0){
							this.uwDao.updateMst_Ulink_Bill(reg_spaj, new Integer(1), new Integer(1), tabarru, ujrah);
						}
				}
			}
//-----			
		}
		
		
		Integer lspd_id = 6;
		
		//untuk Produk ERBE Package tgl RK/msbi_paid_date di set null karena blom ada tgl RK/payment
		//Billing di 4 karena belom ada pembayaran
		if (businessId.equals("217") && lsdbs.equals("2")){ 
			rkDate = null;
			lspd_id = 4;
		}
		
		this.uwDao.updateMst_billing(reg_spaj, null, null, null, new Integer(1), new Integer(1), lspd_id, rkDate, null);
		
		if(this.uwDao.selectProduksiPertamaAgent(reg_spaj)>0){
			this.uwDao.updateProduksi_pertama(prodDate, reg_spaj);
		}
		
		//hitung dulu upp evaluasi + upp kontes, apabila selain bancass & worksite
		//Yusuf (start 1 oct 09) - worksite ikut dihitung
		if("08, 09".indexOf(uwDao.selectCabangFromSpaj(reg_spaj))==-1) {
			
			Upp upp = this.uwDao.selectAgenBuatHitungUppEvaluasi(reg_spaj, 1);
			upp.setLstb_id(1);
			
			//(ys 2009.10.13) using table not hardcode no more, yay!
			/*
			JasperFunctionReport jasp = new JasperFunctionReport();
	
			//tarik koef upp eva nya 
			Double koef = jasp.f_koef_upp_evaluasi(upp.getLsgb_id(), upp.getLsbs_id(), upp.getLsdbs_number(), upp.getLscb_id(), 
					upp.getMspro_jn_prod(), upp.getMspro_prod_ke(), upp.getMsbi_tahun_ke(), upp.getReg_spaj(), upp.getMsbi_premi_ke(), elionsManager, uwManager);
			
			//hitung upp eva
			if(upp.getLsgb_id().intValue() == 17 && upp.getMspro_jn_prod().intValue() == 1) { //if produk link
				upp.setUpp_eva(((upp.getPremi_pokok() * koef) + upp.getPremi_rider()) * upp.getMspro_nilai_kurs());
			}else { //selaen produk link
				upp.setUpp_eva((upp.getPremi_pokok() + upp.getPremi_rider()) * koef * upp.getMspro_nilai_kurs());
			}

			int flag_topup = uwDao.selectIsTopUp(upp.getReg_spaj(), upp.getMspro_prod_ke());
			
			upp.setUpp_kontes(0.);
						
			//tambahan Yosep (15/08/08), sebelum menghitung UPP kontes
			Double koef_upp_kontes = jasp.f_koef_upp_kontes(upp.getLsgb_id(), upp.getLsbs_id(), upp.getLsdbs_number(), upp.getLscb_id(), upp.getLca_id(), upp.getLsbs_jenis(), flag_topup, upp.getLscb_id());

			if(upp.getLca_id().equals("46") && upp.getLsbs_id().intValue()!=162) {
				if(!(
						"161,163,164,167".indexOf(upp.getLsbs_id().toString()) >- 1 ||
						(upp.getLsbs_id().intValue() == 137 && (upp.getLsdbs_number().intValue() == 4 || upp.getLsdbs_number().intValue() == 5)) ||
						(upp.getLsbs_id().intValue() == 114 && upp.getLsdbs_number().intValue() == 2 ) ||
						upp.getLsbs_id().intValue() >= 170
					)) {
					
					if(upp.getLsgb_id().intValue() == 17) {
						upp.setUpp_kontes(((koef * upp.getPremi_pokok()) / 0.7) + upp.getPremi_rider());
					}else {
						upp.setUpp_kontes((upp.getPremi_pokok() + upp.getPremi_rider()) * koef / 0.7);
					}
					
				}else {
					//upp kontes = upp eva
					upp.setUpp_kontes(upp.getUpp_eva());
				}
			}else {
				//hitung upp kontes
				upp.setUpp_kontes(koef_upp_kontes * upp.getUpp_eva());
			}			
			
			//yusuf (09/09/2008) 
			//Cup sori nih ada revisi lagi,
			//di window w_payment_succ function wf_produksi line 268 sebelumnya ldec_kontes = ldec_upp_eva diubah jadi ldec_kontes = (ldec_premi + ldec_rider)
			 if(upp.getLca_id().equals("46")) {
				if(upp.getLsbs_id().intValue() == 162) {
					if(upp.getLscb_id().intValue() == 0 || flag_topup > 0) {
					}else {
						upp.setUpp_kontes(upp.getPremi_pokok() + upp.getPremi_rider());
					}
				}
			}
			
			//yusuf - 23/07/08 - (permintaan yosep)
			//khusus untuk ekalink 88+ ridernya tidak dikali koefisien, jadi upp_eva nya dihitung ulang dikali 100/35 + rider 
			List detail = uwDao.selectDetailBisnis(reg_spaj);
			Map det = (HashMap) detail.get(0);
			if(!detail.isEmpty()) {
				String lsbs = (String) det.get("BISNIS");
				String lsdbs = (String) det.get("DETBISNIS");
				if((lsbs.equals("162") && lsdbs.equals("007")) || (lsbs.equals("162") && lsdbs.equals("008"))){
					upp.setUpp_kontes(((koef_upp_kontes * (koef * upp.getPremi_pokok())) + upp.getPremi_rider()) * upp.getMspro_nilai_kurs());
				}
			}
			
			//yusuf - 19/08/08 - (permintaan yosep)
			//untuk semua link yang sekaligus atau topup, maka upp kontes = 10% premi pokok + premi rider
			if(upp.getLsgb_id().intValue() == 17 && (upp.getLscb_id().intValue() == 0 || uwDao.selectIsTopUp(upp.getReg_spaj(), upp.getMspro_prod_ke()) > 0)) {
				upp.setUpp_kontes((0.1 * upp.getPremi_pokok() + upp.getPremi_rider()) * upp.getMspro_nilai_kurs());
			}
						
			//yusuf - 15/08/08 - upp lain
			if(upp.getLsbs_id().intValue() == 183 && upp.getLscb_id().intValue() == 6){
				//Yusuf (27/7/09) bila eka sehat dan cara bayar bulanan, maka upp eva, kontes, lain = 0
				upp.setUpp_lain(0.);
			}else if(upp.getLca_id().equals("46")) {
//				if(upp.getLsbs_id().intValue() == 162) {
//					upp.setUpp_lain(upp.getUpp_kontes().doubleValue());
//				}else {
//					upp.setUpp_lain(upp.getUpp_eva().doubleValue());
//				}
				//Req Yosep 8 Sep 09
				upp.setUpp_lain(upp.getUpp_kontes().doubleValue());
			}else {
//				if(upp.getLsgb_id().intValue() == 17) {
//					if(upp.getLsbs_id().intValue() == 162) {
//						upp.setUpp_lain(upp.getUpp_kontes().doubleValue());
//					}else {
//						upp.setUpp_lain(1.25 * upp.getUpp_eva().doubleValue());
//					}
//				}else {
//					upp.setUpp_lain(upp.getUpp_eva().doubleValue());
//				}
				//Req Yosep 8 Sep 09
				upp.setUpp_lain(upp.getUpp_kontes().doubleValue());
			}
			
			//TAMBAHAN OLEH YUSUF UNTUK PRODUK2 SINGLE (IM No. 132/IM-DIR/XII/2007) - 16/01/2008
			//UPP EVA, HANYA DAPAT 35% NYA!
			if(this.komisi.isLinkSingle(upp.getReg_spaj(), false)){
				int bulanProd = Integer.parseInt(uwDao.selectBulanProduksi(upp.getReg_spaj()));
				if(bulanProd < 200904){
					upp.setUpp_eva(((upp.getPremi_pokok() * koef * 0.35) + upp.getPremi_rider()) * upp.getMspro_nilai_kurs());
				}else{
					upp.setUpp_eva(((upp.getPremi_pokok() * koef) + upp.getPremi_rider()) * upp.getMspro_nilai_kurs());
				}
				//upp.setUpp_eva(upp.getUpp_eva().doubleValue() * 0.35);
				//if(upp.getLsbs_id().intValue() != 162) upp.setUpp_lain(1.25 * upp.getUpp_eva().doubleValue());
			//}else if(this.komisi.isEkalink88Plus(upp.getReg_spaj(), false)) {
			//	upp.setUpp_eva(upp.getUpp_eva().doubleValue() * 0.7);
			}

			//Tambahan Yusuf - 28 Aug 08 
			//Re-count UPP Evaluasi for Stable Link (mak jang banyak banget ketentuannya rese)
			Date ldt_promo_beg = defaultDateFormat.parse("01/09/2008");
			Date ldt_promo_end = defaultDateFormat.parse("31/12/2008");	
			
			if(products.stableLink(upp.getLsbs_id().toString()) && prodDate.compareTo(ldt_promo_beg) >= 0 && prodDate.compareTo(ldt_promo_end) <= 0) {
				int li_jamin = uwManager.selectMasaGaransiInvestasi(reg_spaj, 1, 1);
				double ldec_upp_eva = 0;
				
				if(li_jamin == 3) {
					ldec_upp_eva = 0.2;
				}else if(li_jamin == 6) {
					ldec_upp_eva = 0.4;
				}else if(li_jamin == 12) {
					ldec_upp_eva = 1;
				}else if(li_jamin == 24) {
					ldec_upp_eva = 1.25;
				}else if(li_jamin == 36) {
					ldec_upp_eva = 1.5;
				}else {
					ldec_upp_eva = 0;
				}
				upp.setUpp_eva((upp.getPremi_pokok() + upp.getPremi_rider()) * upp.getMspro_nilai_kurs() * ldec_upp_eva / 100);
			}
			//END OF Re-count UPP Evaluasi for Stable Link (mak jang banyak banget ketentuannya rese)

			//yusuf - 7/11/08
			Date ldt_promo_beg2 = defaultDateFormat.parse("01/11/2008");
			Date ldt_promo_end2 = defaultDateFormat.parse("30/06/2009");	

			if(upp.getLsbs_jenis().intValue() == 1 
					&& prodDate.compareTo(ldt_promo_beg2) >= 0 && prodDate.compareTo(ldt_promo_end2) <= 0
					&& !(upp.getLsbs_id().intValue() == 143 && upp.getLsdbs_number().intValue() == 4)) {
				int li_jamin = -1;
				double ldec_temp = 0;
				
				li_jamin = uwManager.selectMasaGaransiInvestasi(reg_spaj, 1, 1);
				
				if(3 == li_jamin) {
					ldec_temp = 0.2;
				}else if(6 == li_jamin) {
					ldec_temp = 0.4;
				}else if(12 == li_jamin) {
					ldec_temp = 1;
				}else if(24 == li_jamin) {
					ldec_temp = 1.25;
				}else if(36 == li_jamin) {
					ldec_temp = 1.5;
				}else {
					ldec_temp = 0;
				}
				
				upp.setUpp_kontes(koef_upp_kontes * upp.getUpp_eva());
				
				upp.setUpp_eva(upp.getUpp_eva() + ((upp.getPremi_pokok() + upp.getPremi_rider()) * upp.getMspro_nilai_kurs() * ldec_temp / 100));
				upp.setUpp_kontes(upp.getUpp_kontes() + ((upp.getPremi_pokok() + upp.getPremi_rider()) * upp.getMspro_nilai_kurs() * ldec_temp / 100));
				upp.setUpp_lain(upp.getUpp_lain() + ((upp.getPremi_pokok() + upp.getPremi_rider()) * upp.getMspro_nilai_kurs() * ldec_temp / 100));
				upp.setUpp_bonus(((upp.getPremi_pokok() + upp.getPremi_rider()) * upp.getMspro_nilai_kurs() * ldec_temp / 100));
			}

			String lsbs = FormatString.rpad("0", String.valueOf(upp.getLsbs_id()), 3);
			String lsdbs = FormatString.rpad("0", String.valueOf(upp.getLsdbs_number()), 3);
			
			//UPP Kontes = UPP Eva * 125% only in May - June 2009	(ys 2009.05.25)
			//If Pos('163,172,173', String(li_bisnis_main)) > 0 And (Date(ldt_prod_date) >= Date(2009, 5, 1) And Date(ldt_prod_date) <= Date(2009, 6, 30)) Then
			//edit +- a few more codes (ys 2009.09.15)
			if("027,179,020,180,038,048,052,062,078,085,024,031,033,070,071,072,172".indexOf(lsbs) > -1){
				upp.setUpp_kontes(upp.getUpp_eva() * 1.25);
				upp.setUpp_lain(upp.getUpp_eva() * 1.25);
			}		
			
			if(!wf_check_prod(upp.getReg_spaj(), upp.getMspro_prod_ke())){
				upp.setUpp_eva(0.);
				upp.setUpp_kontes(0.);
				upp.setUpp_lain(0.);
				upp.setUpp_bonus(0.);
			}
			
			//(ys 2009.10.2) upp kontes for stable save
			if("14304,14305,14306,14404,15813,15815,15816,17701".indexOf(lsbs+lsdbs) > -1 || lsbs.equals("184")){
				upp.setUpp_eva(((upp.getPremi_pokok() + upp.getPremi_rider()) * upp.getMspro_nilai_kurs() * 0.015));
				upp.setUpp_kontes(((upp.getPremi_pokok() + upp.getPremi_rider()) * upp.getMspro_nilai_kurs() * 0.07));
				upp.setUpp_lain(upp.getUpp_kontes());
			}
			
			//update mst_production dengan nilai upp_eva + upp_kontes
			this.uwDao.updateMst_productionUpp(upp);
			*/

			//(ys 2009.10.13) using table not hardcode no more, yay!
			upp.setUpp_bonus(0.);
			upp.setMspro_prod_date(prodDate);
			prosesPerhitunganUpp(upp);
			
			if(!wf_check_prod(upp.getReg_spaj(), upp.getMspro_prod_ke())){
				upp.setUpp_eva(0.);
				upp.setUpp_kontes(0.);
				upp.setUpp_lain(0.);
				upp.setUpp_bonus(0.);
			}
			
			this.uwDao.updateMst_productionUpp(upp);
			//uwDao.insertUpp(upp);
		}
		
		return errors;
	}

	public BindException prosesProduksiTopUp(String reg_spaj, BindException errors, int bulanRK, User currentUser, String businessId, Integer premi_ke, ElionsManager elionsManager, UwManager uwManager) throws Exception{
		if(logger.isDebugEnabled())logger.debug("PROSES: prosesProduksiTopUp");
		String s_agent_1="";
		String s_agent_2="";
		String s_agent_3="";
		String s_agent_4="";
		String s_agent_5="";
		String lca_id = uwDao.selectCabangFromSpaj(reg_spaj);
		String lsdbs = uwDao.selectLsdbsNumber(reg_spaj);	
		List billInfo = this.uwDao.selectBillingInfoForTransfer(reg_spaj, 1, premi_ke);
		ArrayList agentProd=Common.serializableList(this.uwDao.selectAgentsFromSpaj(reg_spaj));
		HashMap<String,Object> mapAgent=new HashMap<String,Object>();
		for(int i=0;i<agentProd.size();i++){
			mapAgent= (HashMap<String, Object>)agentProd.get(i);
			BigDecimal lsle_id= (BigDecimal) mapAgent.get("LSLE_ID");
			Integer flag=lsle_id.intValue();
			if(agentProd.size()<5){
				if(flag==4){
					s_agent_4=(String) mapAgent.get("MSAG_ID");
				}else if(flag==3){
					s_agent_3=(String) mapAgent.get("MSAG_ID");
				}else if(flag==2){
					s_agent_2=(String) mapAgent.get("MSAG_ID");
				}else if(flag==1){
					s_agent_1=(String) mapAgent.get("MSAG_ID");
				}
					
			}else{
				if(flag==5){
					s_agent_5=(String) mapAgent.get("MSAG_ID");
				}else if(flag==4){
					s_agent_4=(String) mapAgent.get("MSAG_ID");
				}else if(flag==3){
					s_agent_3=(String) mapAgent.get("MSAG_ID");
				}else if(flag==2){
					s_agent_2=(String) mapAgent.get("MSAG_ID");
				}else if(flag==1){
					s_agent_1=(String) mapAgent.get("MSAG_ID");
				}
			}
				
		}		
		//Date defaultDate = this.uwDao.selectMst_default(1);
		Date inputDate = this.commonDao.selectSysdateTruncated(0);
//cek lagi apa mesti di komen atau tidak untuk erbe		
		List topupList = this.uwDao.selectTopUp(reg_spaj, 1, premi_ke, "desc");
		if(topupList.isEmpty() && (!(businessId.equals("217") && lsdbs.equals("2"))) ) {
			errors.reject("payment.noBillingTopup");
			throw new Exception(errors);
		}
//		Date rkDate = ((TopUp) topupList.get(0)).getMspa_date_book(); 
		String param = "individu";
		if(products.unitLinkNew(businessId)){
			param = "ulink";
		}
		Date rkDate = this.uwDao.selectMaxRkDate(reg_spaj, 1, premi_ke, param);
		Date prodDate = inputDate;
		//Date rkLastDate = this.uwDao.selectMst_default(15);
		
		Map map = (HashMap) billInfo.get(0);
		String lku_id = map.get("LKU_ID").toString(); 
		
		if(!lku_id.equals("01")&& (!(businessId.equals("217") && lsdbs.equals("2"))) ){ //ERBE Package belum ada Payment jadi lewati validasi ini
			if(this.uwDao.validationDailyCurrency(lku_id, rkDate)==0){
				errors.reject("payment.noDailyCurrency");
				throw new Exception(errors);
			}
		}

		//Yusuf - Start 28 Aug 2009, tgl produksi menggunakan procedure di oracle -> eka.get_tgl_prod(:tglrk, :cab_mst_policy)
		Integer jenis = 1;
		if((businessId.equals("190") && lsdbs.equals("9")) || (businessId.equals("200") && lsdbs.equals("7"))) jenis = 3;
		prodDate = commonDao.selectTanggalProduksiUntukProsesProduksi(rkDate, lca_id, jenis);
		
		if(businessId.equals("217") && lsdbs.equals("2")){ //tgl produksi untuk Produk ERBE Package = sysdate karena blom ada tgl RK
			prodDate = inputDate;
			rkDate = prodDate;
		}else if(prodDate == null){
			errors.reject("", "Harap cek fungsi selectTanggalProduksiUntukProsesProduksi (return NULL)");
			throw new Exception(errors);
		}

		/** full autosales project start **/
		String flagAutoSalesCek = bacDao.selectFullAutoSalesFromMstPolicy(reg_spaj);
		
		if (flagAutoSalesCek != null && flagAutoSalesCek.equals("4")){
			prodDate = commonDao.selectTanggalProduksiUntukProsesProduksi(rkDate, lca_id, 4);
			rkDate = prodDate;
		}
		/** full autosales project end **/
		
		/*
		//bila tgl RK <= tgl produksi di mst default
		if(rkDate.compareTo(defaultDate)<=0){
			//bila tgl input >= tgl produksi di mst default
			if(inputDate.compareTo(defaultDate)>=0){
				//tgl produksi = tgl produksi di mst_default
				prodDate = defaultDate;
			}
		}else{
			prodDate = defaultDate;
			
			//bila tgl RK > tgl RK terakhir
			if(rkDate.after(rkLastDate)){
				//tgl prod = tgl RK
				prodDate = rkDate;
			}else{
				//khusus bancass, gak usah tanya ini itu
				String lca_id = uwDao.selectCabangFromSpaj(reg_spaj);
				
				if(lca_id.equals("09")){
					prodDate = inputDate;
				}else{
					if(bulanRK==0){
//						errors.reject("payment.confirmRK", new Object[]{defaultDateFormat.format(prodDate), defaultDateFormat.format(rkLastDate), defaultDateFormat.format(rkDate)}, "Tgl Produksi: {0}<br>- Tgl RK Terakhir: {1}<br>- Tgl RK Polis ini: {2}.");
//						throw new Exception(errors);
					}else if(bulanRK==2){
						prodDate = rkDate;
					}
				}
			}
		}
		*/
		
		//Yusuf - 2 Aug 2011 - Tambahan validasi agar tidak insert production double!!!
		int count =  uwDao.selectValidasiProduksiDouble(reg_spaj, premi_ke);
		if(count > 0){
			errors.reject("", "(Yusuf) Harap cek datanya karena PRODUKSI DOUBLE!!!");
			throw new Exception(errors);
		}
		
		this.uwDao.insertMst_production(rkDate, prodDate, reg_spaj, 1, premi_ke, premi_ke, 0, s_agent_1, s_agent_2, s_agent_3, s_agent_4, s_agent_5);
		
		//update tgl produksi di slink dengan tanggal sysdate
		this.bacDao.updateProdDateSlink(reg_spaj,prodDate);
		
		for(int i=0; i<billInfo.size(); i++){
			Map tmp = (HashMap) billInfo.get(i);
			this.uwDao.insertMst_detail_production(reg_spaj, prodDate, tmp.get("LSBS_ID").toString(), 
					tmp.get("LSDBS_NUMBER").toString(), premi_ke, new Integer(1), premi_ke, 
					new Double(tmp.get("MSDB_PREMIUM").toString()),
					new Double(tmp.get("MSDB_DISCOUNT").toString()), 
					new Double(tmp.get("MSPR_TSI").toString()));
			
			//referensi point(tambang emas) -> Top Up
			Integer cek = uwDao.seleckCekRef(reg_spaj,"2");//2 = kode_program
			if(cek>0){
				cek_data ck = new cek_data();
				Integer lt_id = uwDao.selectLtId(reg_spaj,new Double(tmp.get("MSDB_PREMIUM").toString()));
				if(lt_id!=null){//jika ada
					if(lt_id==2){//top up tunggal
						
						int point = ck.getPoint(new Double(tmp.get("MSDB_PREMIUM").toString()),Integer.valueOf(tmp.get("LSBS_ID").toString()),Integer.valueOf(tmp.get("LSDBS_NUMBER").toString()), new Double(tmp.get("MSDB_PREMIUM").toString()), null);
						//String id_trx = StringUtils.leftPad(bacDao.selectIdTrx(null,null,"3"), 10, '0');
						String id_trx = bacDao.selectIdTrx(null,reg_spaj,"2");
						bacDao.insertPwrDTrx(id_trx, props.getProperty("id.item.point.topup.tunggal"), reg_spaj, new Double(tmp.get("MSDB_PREMIUM").toString()), point, currentUser.getLus_id());
						
					}
					
					if(lt_id==5){//top up berkala
						
						int point = ck.getPoint(new Double(tmp.get("MSDB_PREMIUM").toString()),Integer.valueOf(tmp.get("LSBS_ID").toString()),Integer.valueOf(tmp.get("LSDBS_NUMBER").toString()), null, new Double(tmp.get("MSDB_PREMIUM").toString()));
						//String id_trx = StringUtils.leftPad(bacDao.selectIdTrx(null,null,"3"), 10, '0');
						String id_trx = bacDao.selectIdTrx(null,reg_spaj,"2");
						bacDao.insertPwrDTrx(id_trx, props.getProperty("id.item.point.topup.berkala"), reg_spaj, new Double(tmp.get("MSDB_PREMIUM").toString()), point, currentUser.getLus_id());
						
					}
				}else{//jka tidak ada, maka cuma top up tunggal
					
					int point = ck.getPoint(new Double(tmp.get("MSDB_PREMIUM").toString()),Integer.valueOf(tmp.get("LSBS_ID").toString()),Integer.valueOf(tmp.get("LSDBS_NUMBER").toString()), new Double(tmp.get("MSDB_PREMIUM").toString()), null);
					//String id_trx = StringUtils.leftPad(bacDao.selectIdTrx(null,null,"3"), 10, '0');
					String id_trx = bacDao.selectIdTrx(null,reg_spaj,"2");
					bacDao.insertPwrDTrx(id_trx, props.getProperty("id.item.point.topup.tunggal"), reg_spaj, new Double(tmp.get("MSDB_PREMIUM").toString()), point, currentUser.getLus_id());
					
				}
			}
			//end referensi
		}
		
		Integer lspd_id = 6;
		
		//untuk Produk ERBE Package tgl RK/msbi_paid_date di set null karena blom ada tgl RK/payment
		//Billing di 4 karena belom ada pembayaran
		if (businessId.equals("217") && lsdbs.equals("2")){ 
			rkDate = null;
			lspd_id = 4;
		}
		
		this.uwDao.updateMst_billing(reg_spaj, null, null, null, new Integer(1), premi_ke, lspd_id, rkDate, null);
		
		//hitung dulu upp eva + upp kontes, apabila selain bancass
		//Yusuf (start 1 oct 09) - worksite ikut dihitung
		if("08, 09".indexOf(uwDao.selectCabangFromSpaj(reg_spaj))==-1) {
			
			Upp upp = this.uwDao.selectAgenBuatHitungUppEvaluasi(reg_spaj, premi_ke);
			upp.setLstb_id(1);
	
			//(ys 2009.10.13) using table not hardcode no more, yay!
			/*
			JasperFunctionReport jasp = new JasperFunctionReport();
			
			//tarik koef upp eva nya 
			Double koef = jasp.f_koef_upp_evaluasi(upp.getLsgb_id(), upp.getLsbs_id(), upp.getLsdbs_number(), upp.getLscb_id(), 
					upp.getMspro_jn_prod(), upp.getMspro_prod_ke(), upp.getMsbi_tahun_ke(), upp.getReg_spaj(), upp.getMsbi_premi_ke(), elionsManager, uwManager);

			//hitung upp eva
			if(upp.getLsgb_id().intValue() == 17 && upp.getMspro_jn_prod().intValue() == 1) { //if produk link
				upp.setUpp_eva(((upp.getPremi_pokok() * koef) + upp.getPremi_rider()) * upp.getMspro_nilai_kurs());
			}else { //selaen produk link
				upp.setUpp_eva((upp.getPremi_pokok() + upp.getPremi_rider()) * koef * upp.getMspro_nilai_kurs());
			}
			
			//tambahan Yosep (15/08/08), sebelum menghitung UPP kontes
			double pembagi = 1;
			if(upp.getLca_id().equals("46") && upp.getLsbs_id().intValue()!=162) {
				if(!(upp.getLsbs_id().intValue() == 161 || 
						(upp.getLsbs_id().intValue() == 162 && "001,002,003,004,007,008".indexOf(FormatString.rpad("0", upp.getLsdbs_number().toString(), 3))>-1) ||
						"163,164,167,172,173,174".indexOf(upp.getLsbs_id().toString())>-1 ||
						(upp.getLsbs_id().intValue() == 137 && "004,005".indexOf(FormatString.rpad("0", upp.getLsdbs_number().toString(), 3))>-1) ||
						(upp.getLsbs_id().intValue() == 114 && "002".indexOf(FormatString.rpad("0", upp.getLsdbs_number().toString(), 3))>-1)
						)) {
					pembagi = 0.7;
				}
			}
			
			int flag_topup = uwDao.selectIsTopUp(upp.getReg_spaj(), upp.getMspro_prod_ke());
			
			//hitung upp kontes
			Double koef_upp_kontes = jasp.f_koef_upp_kontes(upp.getLsgb_id(), upp.getLsbs_id(), upp.getLsdbs_number(), upp.getLscb_id(), upp.getLca_id(), upp.getLsbs_jenis(), flag_topup, upp.getLscb_id());
			
			upp.setUpp_kontes(koef_upp_kontes * upp.getUpp_eva() / pembagi);
			
			if(upp.getLca_id().equals("46") && upp.getLsbs_id().intValue()!=162) {
				if(!(
						"161,163,164,167".indexOf(upp.getLsbs_id().toString()) >- 1 ||
						(upp.getLsbs_id().intValue() == 137 && (upp.getLsdbs_number().intValue() == 4 || upp.getLsdbs_number().intValue() == 5)) ||
						(upp.getLsbs_id().intValue() == 114 && upp.getLsdbs_number().intValue() == 2 ) ||
						upp.getLsbs_id().intValue() >= 170
					)) {
					
					if(upp.getLsgb_id().intValue() == 17) {
						upp.setUpp_kontes(((koef * upp.getPremi_pokok()) / 0.7) + upp.getPremi_rider());
					}else {
						upp.setUpp_kontes((upp.getPremi_pokok() + upp.getPremi_rider()) * koef / 0.7);
					}
					
				}else {
					//upp kontes = upp eva
					upp.setUpp_kontes(upp.getUpp_eva());
				}
			}else {
				//hitung upp kontes
				upp.setUpp_kontes(koef_upp_kontes * upp.getUpp_eva());
			}						
			
			//yusuf (09/09/2008) 
			//Cup sori nih ada revisi lagi,
			//di window w_payment_succ function wf_produksi line 268 sebelumnya ldec_kontes = ldec_upp_eva diubah jadi ldec_kontes = (ldec_premi + ldec_rider)
			 if(upp.getLca_id().equals("46")) {
				if(upp.getLsbs_id().intValue() == 162) {
					if(upp.getLscb_id().intValue() == 0 || flag_topup > 0) {
					}else {
						upp.setUpp_kontes(upp.getPremi_pokok() + upp.getPremi_rider());
					}
				}
			}
			
			//yusuf - 23/07/08 - (permintaan yosep)
			//khusus untuk ekalink 88+ ridernya tidak dikali koefisien, jadi upp_eva nya dihitung ulang dikali 100/35 + rider 
			List detail = uwDao.selectDetailBisnis(reg_spaj);
			Map det = (HashMap) detail.get(0);
			if(!detail.isEmpty()) {
				String lsbs = (String) det.get("BISNIS");
				String lsdbs = (String) det.get("DETBISNIS");
				if((lsbs.equals("162") && lsdbs.equals("007")) || (lsbs.equals("162") && lsdbs.equals("008"))){
					upp.setUpp_kontes(((koef_upp_kontes * (koef * upp.getPremi_pokok())) + upp.getPremi_rider()) * upp.getMspro_nilai_kurs());
				}
			}

			//yusuf - 19/08/08 - (permintaan yosep)
			//untuk semua link yang sekaligus atau topup, maka upp kontes = 10% premi pokok + premi rider
			if(upp.getLsgb_id().intValue() == 17 && (upp.getLscb_id().intValue() == 0 || uwDao.selectIsTopUp(upp.getReg_spaj(), upp.getMspro_prod_ke()) > 0)) {
				upp.setUpp_kontes((0.1 * upp.getPremi_pokok() + upp.getPremi_rider()) * upp.getMspro_nilai_kurs());
			}
			
			//yusuf - 15/08/08 - upp lain
			if(upp.getLca_id().equals("46")) {
//				if(upp.getLsbs_id().intValue() == 162) {
//					upp.setUpp_lain(upp.getUpp_kontes().doubleValue());
//				}else {
//					upp.setUpp_lain(upp.getUpp_eva().doubleValue());
//				}
				//Req Yosep 8 Sep 09
				upp.setUpp_lain(upp.getUpp_kontes().doubleValue());
			}else {
//				if(upp.getLsgb_id().intValue() == 17) {
//					if(upp.getLsbs_id().intValue() == 162) {
//						upp.setUpp_lain(upp.getUpp_kontes().doubleValue());
//					}else {
//						upp.setUpp_lain(1.25 * upp.getUpp_eva().doubleValue());
//					}
//				}else {
//					upp.setUpp_lain(upp.getUpp_eva().doubleValue());
//				}
				//Req Yosep 8 Sep 09
				upp.setUpp_lain(upp.getUpp_kontes().doubleValue());
			}

			//TAMBAHAN OLEH YUSUF UNTUK PRODUK2 SINGLE (IM No. 132/IM-DIR/XII/2007) - 16/01/2008
			//UPP EVA, HANYA DAPAT 35% NYA!
			if(this.komisi.isLinkSingle(upp.getReg_spaj(), true)){
				int bulanProd = Integer.parseInt(uwDao.selectBulanProduksi(upp.getReg_spaj()));
				if(bulanProd < 200904){
					upp.setUpp_eva(((upp.getPremi_pokok() * koef * 0.35) + upp.getPremi_rider()) * upp.getMspro_nilai_kurs());
				}else{
					upp.setUpp_eva(((upp.getPremi_pokok() * koef) + upp.getPremi_rider()) * upp.getMspro_nilai_kurs());
				}
				//upp.setUpp_eva(upp.getUpp_eva().doubleValue() * 0.35);
				//if(upp.getLsbs_id().intValue() != 162) upp.setUpp_lain(1.25 * upp.getUpp_eva().doubleValue());
			}
//			else if(this.komisi.isEkalink88Plus(upp.getReg_spaj(), true)) {
//				upp.setUpp_eva(upp.getUpp_eva().doubleValue() * 0.7);
//			}

			//Tambahan Yusuf - 28 Aug 08 
			//Re-count UPP Evaluasi for Stable Link (mak jang banyak banget ketentuannya rese)
			Date ldt_promo_beg = defaultDateFormat.parse("01/09/2008");
			Date ldt_promo_end = defaultDateFormat.parse("31/12/2008");	
			
			if(products.stableLink(upp.getLsbs_id().toString()) && prodDate.compareTo(ldt_promo_beg) >= 0 && prodDate.compareTo(ldt_promo_end) <= 0) {
				int li_jamin = uwManager.selectMasaGaransiInvestasi(reg_spaj, 1, 1);
				double ldec_upp_eva = 0;
				
				if(li_jamin == 3) {
					ldec_upp_eva = 0.2;
				}else if(li_jamin == 6) {
					ldec_upp_eva = 0.4;
				}else if(li_jamin == 12) {
					ldec_upp_eva = 1;
				}else if(li_jamin == 24) {
					ldec_upp_eva = 1.25;
				}else if(li_jamin == 36) {
					ldec_upp_eva = 1.5;
				}else {
					ldec_upp_eva = 0;
				}
				upp.setUpp_eva((upp.getPremi_pokok() + upp.getPremi_rider()) * upp.getMspro_nilai_kurs() * ldec_upp_eva / 100);
			}
			//END OF Re-count UPP Evaluasi for Stable Link (mak jang banyak banget ketentuannya rese)
			
			//yusuf - 7/11/08
			Date ldt_promo_beg2 = defaultDateFormat.parse("01/11/2008");
			Date ldt_promo_end2 = defaultDateFormat.parse("30/06/2009");	

			if(upp.getLsbs_jenis().intValue() == 1 && prodDate.compareTo(ldt_promo_beg2) >= 0 && prodDate.compareTo(ldt_promo_end2) <= 0) {
				int li_jamin = -1;
				double ldec_temp = 0;
				
				li_jamin = uwManager.selectMasaGaransiInvestasi(reg_spaj, 1, 1);
				
				if(3 == li_jamin) {
					ldec_temp = 0.2;
				}else if(6 == li_jamin) {
					ldec_temp = 0.4;
				}else if(12 == li_jamin) {
					ldec_temp = 1;
				}else if(24 == li_jamin) {
					ldec_temp = 1.25;
				}else if(36 == li_jamin) {
					ldec_temp = 1.5;
				}else {
					ldec_temp = 0;
				}
				
				upp.setUpp_eva(upp.getUpp_eva() + ((upp.getPremi_pokok() + upp.getPremi_rider()) * upp.getMspro_nilai_kurs() * ldec_temp / 100));
				upp.setUpp_kontes(upp.getUpp_kontes() + ((upp.getPremi_pokok() + upp.getPremi_rider()) * upp.getMspro_nilai_kurs() * ldec_temp / 100));
				upp.setUpp_lain(upp.getUpp_lain() + ((upp.getPremi_pokok() + upp.getPremi_rider()) * upp.getMspro_nilai_kurs() * ldec_temp / 100));
				upp.setUpp_bonus(((upp.getPremi_pokok() + upp.getPremi_rider()) * upp.getMspro_nilai_kurs() * ldec_temp / 100));
			}
			
			String lsbs = FormatString.rpad("0", String.valueOf(upp.getLsbs_id()), 3);
			String lsdbs = FormatString.rpad("0", String.valueOf(upp.getLsdbs_number()), 3);
			
			//UPP Kontes = UPP Eva * 125% only in May - June 2009	(ys 2009.05.25)
			//If Pos('163,172,173', String(li_bisnis_main)) > 0 And (Date(ldt_prod_date) >= Date(2009, 5, 1) And Date(ldt_prod_date) <= Date(2009, 6, 30)) Then
			//edit +- a few more codes (ys 2009.09.15)
			if("027,179,020,180,038,048,052,062,078,085,024,031,033,070,071,072,172".indexOf(lsbs) > -1){
				upp.setUpp_kontes(upp.getUpp_eva() * 1.25);
				upp.setUpp_lain(upp.getUpp_eva() * 1.25);
			}		
			
			if(!wf_check_prod(upp.getReg_spaj(), upp.getMspro_prod_ke())){
				upp.setUpp_eva(0.);
				upp.setUpp_kontes(0.);
				upp.setUpp_lain(0.);
				upp.setUpp_bonus(0.);
			}
			
			//(ys 2009.10.2) upp kontes for stable save
			if("14304,14305,14306,14404,15813,15815,15816,17701".indexOf(lsbs+lsdbs) > -1 || lsbs.equals("184")){
				upp.setUpp_eva(((upp.getPremi_pokok() + upp.getPremi_rider()) * upp.getMspro_nilai_kurs() * 0.015));
				upp.setUpp_kontes(((upp.getPremi_pokok() + upp.getPremi_rider()) * upp.getMspro_nilai_kurs() * 0.07));
				upp.setUpp_lain(upp.getUpp_kontes());
			}	
			
			//update mst_production dengan nilai upp_eva+upp_kontes nya
			this.uwDao.updateMst_productionUpp(upp);
			*/
			
			//(ys 2009.10.13) using table not hardcode no more, yay!
			upp.setUpp_bonus(0.);
			upp.setMspro_prod_date(prodDate);
			prosesPerhitunganUpp(upp);
			
			if(!wf_check_prod(upp.getReg_spaj(), upp.getMspro_prod_ke())){
				upp.setUpp_eva(0.);
				upp.setUpp_kontes(0.);
				upp.setUpp_lain(0.);
				upp.setUpp_bonus(0.);
			}
			
			this.uwDao.updateMst_productionUpp(upp);
			//uwDao.insertUpp(upp);
		}
		
		return errors;
	}

	public void f_get_product_coef(Upp upp) {
		if(upp.mgi == null) 		upp.mgi = 0;
		if(upp.flag_topup == null) upp.flag_topup = 0;
		
		//f_get_product_coef(is_reg_spaj, li_th_ke, li_premi_ke, ldt_prod_date, li_jenis_prod)
		double li_mod = 0;
		if(upp.mspro_jn_prod.intValue() == 1) {
			li_mod = 1;
		}else if(upp.mspro_jn_prod.intValue() == 3) {
			li_mod = -1;
		}

		//special-case arthamas closing EKALINK 88
		if(upp.reg_spaj != null){
			if(upp.reg_spaj.equals("46200800280")){
				li_mod = li_mod * 0.9;
			}
		}
		
		//ambil flag top up
		upp.flag_topup = uwDao.selectIsTopUp(upp.reg_spaj, upp.mspro_prod_ke);
		if(upp.flag_topup.intValue() >= 1) {
			upp.lscb_id = 0;
			upp.flag_topup = 1;
		}else {
			upp.flag_topup = 0;
		}
				
		//exception, cannot be inserted in the table
		if(upp.msbi_premi_ke.intValue() > 1) {
			
			if("087, 101, 134".contains(FormatString.rpad("0", upp.lsbs_id.toString(), 3))) {
				upp.coef 				= 0.025   * li_mod;
				upp.mod_hybrid 			= 0.07    * li_mod;
				upp.mod_con 			= 0.03125 * li_mod;
				upp.mod_con_hs 			= 0.025   * li_mod;
				upp.mod_link 			= 1. 	  * li_mod;
				upp.mod_con_rider 		= 1. 	  * li_mod;
				upp.mod_con_rider_hs 	= 1. 	  * li_mod;
				upp.bonus_coef   		= 1.      * li_mod;
				
				if(upp.lscb_id.intValue() == 0 || upp.flag_topup.intValue() >= 1) {
					upp.comm_eva 			= 0.05 	  * li_mod;
					upp.mod_lain          	= 0.0625  * li_mod;
					upp.mod_lain_rider    	= 0.0625  * li_mod;
					upp.mod_lain_hs       	= 0.1     * li_mod;
					upp.mod_lain_rider_hs 	= 1       * li_mod;
				}else {
					upp.comm_eva      	  = 1       * li_mod;
					upp.mod_lain          = 0.03125 * li_mod;
					upp.mod_lain_rider    = 0.03125 * li_mod;
					upp.mod_lain_hs       = 0.0175  * li_mod;
					upp.mod_lain_rider_hs = 1       * li_mod;
				}
				
				return;
				
			}else if("115, 117, 152".contains(FormatString.rpad("0", upp.lsbs_id.toString(), 3))) {
				
				if(upp.msbi_tahun_ke.intValue() >= 3) {
					upp.coef 				= 0.025   * li_mod;
					upp.mod_hybrid 			= 0.07    * li_mod;
					upp.mod_con 			= 0.03125 * li_mod;
					upp.mod_con_hs 			= 0.025   * li_mod;
					upp.mod_link 			= 1		  * li_mod;
					upp.mod_con_rider    	= 1		  * li_mod;
					upp.mod_con_rider_hs 	= 1		  * li_mod;
					upp.bonus_coef   		= 1       * li_mod;
					
					if(upp.lscb_id.intValue() == 0 || upp.flag_topup.intValue() >= 1) {
						upp.comm_eva      	  = 0.05    * li_mod;
						upp.mod_lain          = 0.0625  * li_mod;
						upp.mod_lain_rider    = 0.0625  * li_mod;
						upp.mod_lain_hs       = 0.1     * li_mod;
						upp.mod_lain_rider_hs = 1       * li_mod;
					}else {
						upp.comm_eva     	  = 1       * li_mod;
						upp.mod_lain          = 0.03125 * li_mod;
						upp.mod_lain_rider    = 0.03125 * li_mod;
						upp.mod_lain_hs       = 0.0175  * li_mod;
						upp.mod_lain_rider_hs = 1       * li_mod;
					}
					
					return;		
				}
			}  
		}
		
		if(upp.reg_spaj != null){
			int lsbs_id = Integer.parseInt(uwDao.selectBusinessId(upp.reg_spaj));
			int lsdbs_number = uwDao.selectBusinessNumber(upp.reg_spaj);
	
			if(products.powerSave(String.valueOf(lsbs_id)) 
					|| products.stableLink(String.valueOf(lsbs_id)) 
					|| products.stableSave(lsbs_id, lsdbs_number)){
				upp.mgi = uwDao.selectMasaGaransiInvestasi(upp.reg_spaj, upp.msbi_tahun_ke, upp.msbi_premi_ke);
				if(products.progressiveLink(String.valueOf(lsbs_id)) ){
					upp.mgi=0;
				}
			}
		}
		
		upp.lspc_type = 1;
		
		//Yusuf (16 Oct 2009) - Req Yosep bila worksite tipe nya beda
		if(upp.reg_spaj != null){
			String lca_id = uwDao.selectCabangFromSpaj(upp.reg_spaj);
			if(lca_id.equals("42")) upp.lspc_type = 2;
			if(lca_id.equals("60")) upp.lspc_type = 3;//sengaja dibuat flag 3 biar hasilnya null.
		}
		
		Map koefisien = uwDao.selectKoefisienUpp(upp);
		int bulanProd = Integer.parseInt(uwDao.selectBulanProduksi(upp.reg_spaj));
		
		//UPP Produksi untuk powersave & stable link. New Business bila MTI 1, maka no production
		if(bulanProd<201105){
			if(upp.mgi==1){
				koefisien=null;
			}
		}
		
		if(koefisien == null) {
			upp.coef = 				(double) 0;
			upp.mod_hybrid =		(double) 0;
			upp.mod_con =			(double) 0;
			upp.mod_con_hs =		(double) 0;
			upp.mod_link =			(double) 0;
			upp.mod_con_rider =		(double) 0;
			upp.comm_eva = 			(double) 0;
			upp.mod_lain = 			(double) 0;
			upp.mod_lain_hs = 		(double) 0;
			upp.mod_lain_rider = 	(double) 0;
			upp.mod_lain_rider_hs =	(double) 0;
			upp.bonus_coef = 		(double) 0;
			upp.bonus_comm = 		(double) 0;
		}else {
			upp.coef = 				((BigDecimal) koefisien.get("LSPC_COEF")).doubleValue() * li_mod;
			upp.mod_hybrid = 		((BigDecimal) koefisien.get("LSPC_MOD_HYBRID")).doubleValue() * li_mod;
			upp.mod_con = 			((BigDecimal) koefisien.get("LSPC_MOD_CON")).doubleValue() * li_mod;
			upp.mod_con_hs =		((BigDecimal) koefisien.get("LSPC_MOD_CON_HS")).doubleValue() * li_mod;
			upp.mod_link =			((BigDecimal) koefisien.get("LSPC_MOD_LINK")).doubleValue() * li_mod;
			upp.mod_con_rider =		((BigDecimal) koefisien.get("LSPC_MOD_CON_RIDER")).doubleValue() * li_mod;
			upp.mod_con_rider_hs =	((BigDecimal) koefisien.get("LSPC_MOD_CON_RIDER_HS")).doubleValue() * li_mod;
			upp.comm_eva = 			((BigDecimal) koefisien.get("LSPC_COMM_EVA")).doubleValue() * li_mod;
			upp.mod_lain = 			((BigDecimal) koefisien.get("LSPC_MOD_LAIN")).doubleValue() * li_mod;
			upp.mod_lain_hs = 		((BigDecimal) koefisien.get("LSPC_MOD_LAIN_HS")).doubleValue() * li_mod;
			upp.mod_lain_rider = 	((BigDecimal) koefisien.get("LSPC_MOD_LAIN_RIDER")).doubleValue() * li_mod;
			upp.mod_lain_rider_hs =	((BigDecimal) koefisien.get("LSPC_MOD_LAIN_RIDER_HS")).doubleValue() * li_mod;
			upp.bonus_coef = 		((BigDecimal) koefisien.get("LSPC_BONUS_COEF")).doubleValue() * li_mod;
			upp.bonus_comm = 		((BigDecimal) koefisien.get("LSPC_BONUS_COMM")).doubleValue() * li_mod;
		}
	}
	
	/**
	 * Proses untuk perhitungan com eva, dipanggilnya di perhitungan komisi khusus hybrid system
	 * 
	 * @author Yusuf
	 * @since Sep 5, 2008 (7:35:01 PM)
	 * @param reg_spaj
	 * @param premi_ke
	 */
	public void prosesPerhitunganCommEva(String reg_spaj, int premi_ke) {
		double ldec_com_basic = uwDao.selectKomisiPenutup(reg_spaj, 1, premi_ke);
		if(ldec_com_basic > 0) {
			double ldec_com_rider = uwDao.selectKomisiPenutupRider(reg_spaj, 1, premi_ke);
			Upp upp = this.uwDao.selectAgenBuatHitungUppEvaluasi(reg_spaj, premi_ke); 
			f_get_product_coef(upp);
			upp.com_eva = (ldec_com_basic * ((upp.comm_eva * upp.mod_hybrid * upp.mod_link) + upp.bonus_comm)) + ldec_com_rider;
			uwDao.updateMst_productionCommEva(upp);
		}
	}
	
	public Upp prosesPerhitunganUpp(Upp upp) {
		
		//ambil koefisien
		f_get_product_coef(upp);
		
		//convert sesuai kurs produksi
		upp.premi_pokok = upp.mspro_nilai_kurs * upp.premi_pokok;
		upp.premi_rider = upp.mspro_nilai_kurs * upp.premi_rider;
		
		//hitung upp eva, kontes, lain
		if(upp.getLca_id().equals("46")) {
			if(upp.getLsgb_id().intValue() == 17) { //bila unit link
				upp.upp_eva = 		( upp.premi_pokok * ((upp.coef * upp.mod_hybrid * upp.mod_link) + upp.bonus_coef) ) + upp.premi_rider;
				upp.upp_kontes = 	( upp.premi_pokok * (upp.mod_con_hs + upp.bonus_coef)) + ( upp.premi_rider * (upp.mod_con_rider_hs + upp.bonus_coef));
			}else {
				upp.upp_eva = 		( upp.premi_pokok + upp.premi_rider ) * ((upp.coef * upp.mod_hybrid) + upp.bonus_coef);
				upp.upp_kontes= 	( upp.premi_pokok + upp.premi_rider ) * (upp.mod_con_hs + upp.bonus_coef);
			}
			upp.upp_lain = ( upp.premi_pokok * (upp.mod_lain_hs + upp.bonus_coef) ) + ( upp.premi_rider * (upp.mod_lain_rider_hs + upp.bonus_coef) );
		}else {
			if(upp.getLsgb_id().intValue() == 17) {
				upp.upp_eva = 		( upp.premi_pokok * ((upp.coef * upp.mod_link) + upp.bonus_coef) ) + upp.premi_rider;
				upp.upp_kontes = 	( upp.premi_pokok * (upp.mod_con + upp.bonus_coef)  ) + ( upp.premi_rider * (upp.mod_con_rider + upp.bonus_coef) );
			}else {
				upp.upp_eva = 		( upp.premi_pokok + upp.premi_rider ) * (upp.coef + upp.bonus_coef);
				upp.upp_kontes = 	( upp.premi_pokok + upp.premi_rider ) * (upp.mod_con + upp.bonus_coef);
			}			
			upp.upp_lain = ( upp.premi_pokok * (upp.mod_lain + upp.bonus_coef) ) + ( upp.premi_rider * (upp.mod_lain_rider + upp.bonus_coef) );
		}
		
		//request from wesni, untuk stable save, upp_bonus sebesar upp_eva
		if(upp.reg_spaj != null){
			int lsbs_id = Integer.parseInt(uwDao.selectBusinessId(upp.reg_spaj));
			int lsdbs_number = uwDao.selectBusinessNumber(upp.reg_spaj);
			if(products.stableSave(lsbs_id, lsdbs_number)){
				upp.upp_bonus = upp.upp_eva;
			}
		}
		return upp;
	}
	
	private List tambahUlinkDetBill(List list, String spaj, Integer tahun,
			Integer pot_ke, String lsbs_id, String lsdbs_number, Double premi) {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", tahun);
		params.put("pot_ke", pot_ke);
		params.put("lsbs_id", lsbs_id);
		params.put("lsdbs_number", lsdbs_number);
		params.put("premi", premi);
		list.add(params);
		return list;
	}
	
}
