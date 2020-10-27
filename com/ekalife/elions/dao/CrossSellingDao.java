package com.ekalife.elions.dao;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;

import com.ekalife.elions.model.CabangBii;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.cross_selling.AgentCs;
import com.ekalife.elions.model.cross_selling.CrossSelling;
import com.ekalife.elions.model.cross_selling.Fat;
import com.ekalife.elions.model.cross_selling.FatHistory;
import com.ekalife.elions.model.cross_selling.FatLetter;
import com.ekalife.elions.model.cross_selling.PolicyCs;
import com.ekalife.elions.model.cross_selling.PositionCs;
import com.ekalife.elions.web.cross_selling.FatBacMultiController;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentDao;

@SuppressWarnings("unchecked")
public class CrossSellingDao extends ParentDao{

	protected void initDao() throws DataAccessException{
		this.statementNameSpace = "elions.cross_selling.";
	}	
	
	public List selectTopSellerBSM(String lcb) throws DataAccessException{
		return query("selectTopSellerBSM", lcb);
	}
	
	public List selectPolisPending(String lus_id) throws DataAccessException{
		return query("selectPolisPending", lus_id);
	}
	
	public Integer selectValidasiDoubleNoPolASM(String reg_spaj, String pol) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("pol", pol);
		return (Integer) querySingle("selectValidasiDoubleNoPolASM", m);
	}
	
	public List<CabangBii> selectAllCabangBsm(Integer flag_aktif) throws DataAccessException{
		return query("selectAllCabangBsm", flag_aktif);
	}
	
	public List<Map> selectRMdanRD(String msag_id) throws DataAccessException{
		return query("selectRMdanRD", msag_id);
	}
	
	public String selectEmailCabangSpajCrossSelling(String reg_spaj) throws DataAccessException{
		return (String) querySingle("selectEmailCabangSpajCrossSelling", reg_spaj);
	}
	
	public String selectEmailAgenSpajCrossSelling(String reg_spaj) throws DataAccessException{
		return (String) querySingle("selectEmailAgenCrossSelling", reg_spaj);
	}
	
	public Double selectKursBySpaj(String jenis_kurs, String lku_id, String reg_spaj) throws DataAccessException{
		if(lku_id.equals("01")) return (double) 1;
		
		Map params = new HashMap();
		params.put("jenis_kurs", jenis_kurs); //lkh_currency, lkh_kurs_jual, lkh_kurs_beli
		params.put("lku_id", lku_id);
		params.put("reg_spaj", reg_spaj);
		return (Double) querySingle("selectKursBySpaj", params);
	}
	
	public Map selectLstCommNew(int lsco_jenis, int lsbs_id, int lsdbs_number, int lscb_id, int lsco_insper, String lku_id, int lev_comm, int lsco_year) throws DataAccessException{
		Map params = new HashMap();
		params.put("lsco_jenis", lsco_jenis);
		params.put("lsbs_id", lsbs_id);
		params.put("lsdbs_number", lsdbs_number);
		params.put("lscb_id", lscb_id);
		params.put("lsco_insper", lsco_insper);
		params.put("lku_id", lku_id);
		params.put("lev_comm", lev_comm);
		params.put("lsco_year", lsco_year);
		return (Map) querySingle("selectLstCommNew", params);
	}
	
	public void insertMstPolicyCs(PolicyCs policyCs) throws DataAccessException{
		insert("insertMstPolicyCs", policyCs);
	}
	
	public void insertMstAgentCs(AgentCs agentCs) throws DataAccessException{
		insert("insertMstAgentCs", agentCs);
	}
	
	public void insertMstPositionCs(PositionCs positionCs) throws DataAccessException{
		insert("insertMstPositionCs", positionCs);
	}
	
	public PolicyCs selectMstPolicyCsBySpaj(String reg_spaj) throws DataAccessException{
		return (PolicyCs) querySingle("selectMstPolicyCsBySpaj", reg_spaj);
	}
	
	public List<AgentCs> selectMstAgentCsBySpaj(String reg_spaj) throws DataAccessException{
		return query("selectMstAgentCsBySpaj", reg_spaj);
	}
	
	public List<PositionCs> selectMstPositionCsBySpaj(String reg_spaj) throws DataAccessException{
		return query("selectMstPositionCsBySpaj", reg_spaj);
	}
	
	public List<Map> selectCariCrossSelling(String tipe, String kata) throws DataAccessException{
		Map map = new HashMap();
		map.put("tipe", tipe);
		map.put("kata", kata);
		return query("selectCariCrossSelling", map);
	}
	
	public PositionCs selectPosisiDokumenBySpaj(String reg_spaj) throws DataAccessException{
		return (PositionCs) querySingle("selectPosisiDokumenBySpaj", reg_spaj);
	}
	
	public int updateMstPolicyCs(PolicyCs policyCs) throws DataAccessException{
		return update("updateMstPolicyCs", policyCs);
	}

	public int updateMstAgentCs(AgentCs agentCs) throws DataAccessException{
		return update("updateMstAgentCs", agentCs);
	}
	
	public void deleteMstAgentCs(String reg_spaj) throws DataAccessException{
		delete("deleteMstAgentCs", reg_spaj);
	}
	
	public List<PolicyCs> selectDaftarCrossSelling(Integer lspd_id, String reg_spaj, String mscs_holder, String startDate, String endDate) throws DataAccessException{
		Map m = new HashMap();
		m.put("lspd_id", lspd_id);
		m.put("reg_spaj", reg_spaj);
		m.put("mscs_holder", mscs_holder);
		m.put("startDate", startDate);
		m.put("endDate", endDate);
		return query("selectDaftarCrossSelling", m);
	}

	//MORE COMPLICATED FUNCTIONS
	
	/**
	 * Insert SPAJ Baru (New Business)
	 */
	public String insertSpajCrossSelling(PolicyCs policyCs, int lus_id, Integer flag_lanjutan) throws DataAccessException{
		Map regionalAgen = bacDao.selectRegionalAgen(policyCs.daftarAgent.get(0).msag_id);
		Date sysdate = commonDao.selectSysdate();
		//1. INSERT MST_POLICY_CS 
		policyCs.reg_spaj 			= sequence.sequenceMst_policy(regionalAgen);
		policyCs.mscs_input_date 	= sysdate;
		policyCs.lspd_id 			= 79;
		policyCs.lus_id 			= lus_id;
		policyCs.lca_id 			= (String) regionalAgen.get("STRBRANCH");
		policyCs.lwk_id 			= (String) regionalAgen.get("STRWILAYAH");
		policyCs.lsrg_id 			= (String) regionalAgen.get("STRREGION");
		policyCs.mscs_pay_count		= 0;
		policyCs.mscs_policy_type	= flag_lanjutan;
		crossSellingDao.insertMstPolicyCs(policyCs);
		
		//2. INSERT MST_AGENT_CS
		for(AgentCs agentCs : policyCs.daftarAgent) {
			agentCs.reg_spaj = policyCs.reg_spaj;
			crossSellingDao.insertMstAgentCs(agentCs);
		}
		
		//3. BILA HYBRID, INSERT STRUKTUR AGEN YG LEVEL 0 NYA (RM DAN RD NYA)
		if(policyCs.lca_id.equals("46")) {
			List<Map> daftarRMdanRD = crossSellingDao.selectRMdanRD(policyCs.daftarAgent.get(0).msag_id);
			for(Map m : daftarRMdanRD) {
				int lsle_id = ((BigDecimal) m.get("LSLE_ID")).intValue();
				if(lsle_id == 0) {
					AgentCs tmp = new AgentCs();
					tmp.reg_spaj = policyCs.reg_spaj;
					tmp.msag_id = (String) m.get("MSAG_ID");
					tmp.lsle_id = lsle_id;
					tmp.lev_comm = null;
					tmp.flag_sbm = 0; //((BigDecimal) m.get("MSAG_SBM")).intValue();
					crossSellingDao.insertMstAgentCs(tmp);
				}
			}
		}
		
		//4. INSERT MST_POSITION_CS
		PositionCs positionCs = new PositionCs();
		positionCs.reg_spaj 	= policyCs.reg_spaj;
		positionCs.mspc_date 	= sysdate;
		positionCs.lspd_id 		= policyCs.lspd_id;
		positionCs.lus_id 		= lus_id;
		positionCs.mspc_desc 	= "INPUT SPAJ";
		crossSellingDao.insertMstPositionCs(positionCs);
		
		return policyCs.reg_spaj;		
	}
	
	/**
	 * Update SPAJ Baru (New Business)
	 */
	public String updateSpajCrossSelling(PolicyCs policyCs, int lus_id) throws DataAccessException{
		if(policyCs.lspd_id.intValue() != 79) {
			throw new RuntimeException("POSISI DOKUMEN "+policyCs.reg_spaj+" = "+policyCs.lspd_id+". TIDAK BISA UPDATE DATA!");
		}
		
		Date sysdate = commonDao.selectSysdate();

		//1. UPDATE MST_POLICY_CS
		Map regionalAgen = bacDao.selectRegionalAgen(policyCs.daftarAgent.get(0).msag_id);
		policyCs.lca_id 			= (String) regionalAgen.get("STRBRANCH");
		policyCs.lwk_id 			= (String) regionalAgen.get("STRWILAYAH");
		policyCs.lsrg_id 			= (String) regionalAgen.get("STRREGION");
		crossSellingDao.updateMstPolicyCs(policyCs);
		
		//2. UPDATE MST_AGENT_CS
		crossSellingDao.deleteMstAgentCs(policyCs.reg_spaj);
		for(AgentCs agentCs : policyCs.daftarAgent) {
			agentCs.reg_spaj = policyCs.reg_spaj;
			crossSellingDao.insertMstAgentCs(agentCs);
		}
		
		//3. BILA HYBRID, INSERT STRUKTUR AGEN YG LEVEL 0 NYA (RM DAN RD NYA)
		if(policyCs.lca_id.equals("46")) {
			List<Map> daftarRMdanRD = crossSellingDao.selectRMdanRD(policyCs.daftarAgent.get(0).msag_id);
			for(Map m : daftarRMdanRD) {
				int lsle_id = ((BigDecimal) m.get("LSLE_ID")).intValue();
				if(lsle_id == 0) {
					AgentCs tmp = new AgentCs();
					tmp.reg_spaj = policyCs.reg_spaj;
					tmp.msag_id = (String) m.get("MSAG_ID");
					tmp.lsle_id = lsle_id;
					tmp.lev_comm = null;
					tmp.flag_sbm = 0; //((BigDecimal) m.get("MSAG_SBM")).intValue();
					crossSellingDao.insertMstAgentCs(tmp);
				}
			}
		}
		
		//4. INSERT MST_POSITION_CS
		PositionCs positionCs = new PositionCs();
		positionCs.reg_spaj 	= policyCs.reg_spaj;
		positionCs.mspc_date 	= sysdate;
		positionCs.lspd_id 		= policyCs.lspd_id;
		positionCs.lus_id 		= lus_id;
		positionCs.mspc_desc 	= "EDIT SPAJ";
		crossSellingDao.insertMstPositionCs(positionCs);

		return policyCs.reg_spaj;
	}
		
	/**
	 * Save Nopol ASM & Terima TTP
	 */
	public String saveNomorPolisCrossSelling(CrossSelling crossSelling, int lus_id) {
		if(crossSelling.policyCs.lspd_id.intValue() != 80) {
			throw new RuntimeException("POSISI DOKUMEN "+crossSelling.policyCs.reg_spaj+" = "+crossSelling.policyCs.lspd_id+". TIDAK BISA UPDATE NOPOL!");
		}

		Date sysdate = commonDao.selectSysdate();
		
		//1. SAVE NOMOR POLIS & TERIMA TTP
		PolicyCs pc 		= new PolicyCs();
		pc.reg_spaj 		= crossSelling.policyCs.reg_spaj;
		pc.mscs_policy_no 	= crossSelling.policyCs.mscs_policy_no;
		pc.mscs_ttp_date 	= crossSelling.policyCs.mscs_ttp_date;
		crossSellingDao.updateMstPolicyCs(pc);
		
		//2. INSERT HISTORY 
		PositionCs positionCs = new PositionCs();
		positionCs.reg_spaj 	= crossSelling.policyCs.reg_spaj;
		positionCs.mspc_date 	= sysdate;
		positionCs.lspd_id 		= crossSelling.policyCs.lspd_id;
		positionCs.lus_id 		= lus_id;
		positionCs.mspc_desc 	= "UPDATE NOMOR POLIS & TGL TTP";
		crossSellingDao.insertMstPositionCs(positionCs);
		
		return "Nomor Polis " + pc.mscs_policy_no + " dan Tgl TTP berhasil disimpan untuk SPAJ "+ crossSelling.policyCs.reg_spaj;		
	}
	
	/**
	 * Bukan Save Tanggal TTP, tapi Save Tanggal Bayar / BSB
	 */
	public String saveTtpCrossSelling(CrossSelling crossSelling, int lus_id) {
		
		if(crossSelling.policyCs.lspd_id.intValue() != 81) {
			throw new RuntimeException("POSISI DOKUMEN "+crossSelling.policyCs.reg_spaj+" = "+crossSelling.policyCs.lspd_id+". TIDAK BISA UPDATE TGL BAYAR/BSB!");
		}

		Date sysdate = commonDao.selectSysdate();
		
		//1. SAVE TGL BAYAR
		PolicyCs pc 		= new PolicyCs();
		pc.reg_spaj 		= crossSelling.policyCs.reg_spaj;
		pc.mscs_pay_date 	= crossSelling.policyCs.mscs_pay_date;
		crossSellingDao.updateMstPolicyCs(pc);
		
		//2. INSERT HISTORY 
		PositionCs positionCs = new PositionCs();
		positionCs.reg_spaj 	= crossSelling.policyCs.reg_spaj;
		positionCs.mspc_date 	= sysdate;
		positionCs.lspd_id 		= crossSelling.policyCs.lspd_id;
		positionCs.lus_id 		= lus_id;
		positionCs.mspc_desc 	= "UPDATE TGL BAYAR/BSB";
		crossSellingDao.insertMstPositionCs(positionCs);
		
		return "Tanggal Bayar/BSB berhasil disimpan untuk SPAJ " + crossSelling.policyCs.reg_spaj;		
	}
	
	/**
	 * Save Tanggal Bayar, Hitung komisi, produksi, dan lainnya, terakhir ditransfer ke proses agent komisi
	 */
	public String saveKonfirmasiCrossSelling(List<PolicyCs> daftarSpaj, int lus_id) throws DataAccessException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		int count = 0;
		Date sysdate = commonDao.selectSysdate();
		
		for(PolicyCs pc : daftarSpaj) {
			if(pc.selected!=null) {
				if(pc.selected.intValue() == 1) {
					count++;
					
					//tarik informasi polis
					Date asm_date = pc.mscs_tgl_terima_asm;
					if(asm_date == null) throw new RuntimeException("Tanggal Terima ASM "+pc.reg_spaj+" tidak diisi.");
					
					pc = selectMstPolicyCsBySpaj(pc.reg_spaj);
					if(pc.lspd_id.intValue() != 82) throw new RuntimeException("POSISI DOKUMEN "+pc.reg_spaj+" = "+pc.lspd_id+". TIDAK BISA DITRANSFER!");

					//looping untuk mencari agen penutup polis ini
					AgentCs ac = null;
					List<AgentCs> daftarAgen = selectMstAgentCsBySpaj(pc.reg_spaj);
					for(AgentCs a : daftarAgen) {
						if(a.lsle_id.intValue() == 4) {
							ac = a;
							break;
						}
					}
					if(ac == null) throw new RuntimeException("Agen Penutup "+pc.reg_spaj+" = "+pc.lspd_id+". Tidak Ditemukan!");

					//tentukan lsco_jenis (1=regional, 3=agency, 4=hybrid)
					/*
					int lsco_jenis = 0;
					if(ac.lca_id.equals("37")) 
						lsco_jenis = 3;
					else if(ac.lca_id.equals("46")) 
						lsco_jenis = 4;
					else if("08, 09, 42".indexOf(lsco_jenis) == -1) {
						lsco_jenis = 1;
					}
					if(lsco_jenis == 0) throw new RuntimeException("Agen Penutup "+pc.reg_spaj+" = "+pc.lspd_id+" bukan agen Regional, Agency, maupun Hybrid.");
					*/
					
					int lsco_jenis = 1; //lsco_jenis dipatok selalu regional, bila ternyata dikemudian hari berbeda2 antara distribusi, gunakan comment diatas
					
					//special cases
					if(pc.lsbs_id.intValue() == 161) pc.lsdbs_number = 1;
					
					//tarik rate komisi
					//Yusuf (19/11/2010) LSCB_ID dipatok 3 (tahunan)
					//Map infoKomisi = selectLstCommNew(lsco_jenis, pc.lsbs_id, pc.lsdbs_number, pc.lscb_id, pc.mscs_ins_period, pc.lku_id, 4, 1);
					Map infoKomisi = selectLstCommNew(lsco_jenis, pc.lsbs_id, pc.lsdbs_number, 3, pc.mscs_ins_period, pc.lku_id, 4, 1);
					if(infoKomisi == null) throw new RuntimeException("Rate komisi "+pc.reg_spaj+" = "+pc.lspd_id+" tidak ditemukan.");
					
					//tarik kurs 
					Double kurs = selectKursBySpaj("lkh_currency", pc.lku_id, pc.reg_spaj);
					if(kurs == null) throw new RuntimeException("Kurs komisi "+pc.reg_spaj+" = "+pc.lspd_id+" tidak ditemukan.");
					
					int pengali = 1; //pengali premi tahunan
					//0 sekaligus
					//1 triwulanan
					//2 semesteran
					//3 tahunan
					//6 bulanan
					//8 kwartalan
					if(pc.lscb_id.intValue() == 0 || pc.lscb_id.intValue() == 3) pengali = 1;
					else if(pc.lscb_id.intValue() == 1) pengali = 4;
					else if(pc.lscb_id.intValue() == 2) pengali = 2;
					else if(pc.lscb_id.intValue() == 6) pengali = 12;
					else if(pc.lscb_id.intValue() == 8) pengali = 3;
					
					//perhitungan komisi dan pajaknya
					double rateKomisi 	= ((BigDecimal) infoKomisi.get("LSCO_COMM")).doubleValue() + ((BigDecimal) infoKomisi.get("LSCO_BONUS")).doubleValue(); //komisi + bonus
					double premi		= FormatNumber.round(pc.mscs_premium * kurs * pengali, 2); //dirupiahkan, dan dijadikan premi tahunan
					double komisi 		= premi * rateKomisi / 100;
					double pajak 		= this.komisi.f_load_tax(komisi, sysdate, ac.msag_id); //pajak progresif
					komisi 				= FormatNumber.rounding(komisi, false, 25); //bulatkan kebawah per 25
					pajak 				= FormatNumber.rounding(pajak, true, 25); //bulatkan keatas per 25
					
					//perhitungan tanggal produksi
					Date inputDate = this.commonDao.selectSysdateTruncated(0);
					Date prodDate = inputDate;
					Date rkDate = asm_date;
					Date rkLastDate = this.uwDao.selectMst_default(15);
					Date defaultDate = this.uwDao.selectMst_default(1);
					if(rkDate.compareTo(defaultDate)<=0){
						if(inputDate.compareTo(defaultDate)>=0){
							prodDate = defaultDate;
						}
					}else{
						prodDate = defaultDate;
						if(rkDate.after(rkLastDate)){
							prodDate = rkDate;
						}
					}
					
					//perhitungan produksi dan upp, dirupiahkan, dan dijadikan premi tahunan, dikali lagi 10%, karena pengakuan upp hanya 10%
					double upp_eva	= pc.mscs_premium * kurs * pengali * 0.1;
					double upp_kontes = pc.mscs_premium * kurs * pengali * 0.1;
					double upp_lain = pc.mscs_premium * kurs * pengali * 0.1;
					
					//2. UPDATE DATA MST_POLICY_CS, TERMASUK DATA KOMISI, PRODUKSI, UPP, LSPD_ID, dan PAY_DATE
					
					PolicyCs temp 		= new PolicyCs();
					temp.reg_spaj 		= pc.reg_spaj;
					temp.mscs_pay_count = pc.mscs_pay_count + 1;
					temp.mscs_tgl_terima_asm = asm_date;
					
					//bila bukan premi sekaligus / tahunan, data2 produksi dan komisi hanya diupdate setelah kelipatan setahun, 
					//dan datanya tidak ditransfer ke agent komisi
					//dengan kata lain, pengakuan produksi dan pembayaran komisi hanya dapat dijalankan setelah mencapai 1 tahun
					if(pc.lscb_id.intValue() != 0 && pc.lscb_id.intValue() != 3 && temp.mscs_pay_count % pengali != 0) {
						//data lain tidak diupdate
						temp.lspd_id 		= pc.lspd_id;
					}else {
						temp.lspd_id 		= 8;
	
						//Yusuf (12/04/2012) polis lanjutan tetap ada komisi dan produksi
						
//						if( pc.mscs_policy_type != null && pc.mscs_policy_type.intValue() == 1 ){ // jika polis merupakan polis lanjutan
//							
//						}else{// jika polis bukan polis lanjutan
							//data2 komisi
							temp.mscs_comm 		= komisi;
							temp.mscs_comm_kurs = kurs;
							temp.mscs_comm_paid = 0;
							temp.mscs_comm_tax 	= pajak;
							//data2 produksi
							temp.mscs_prod_date = prodDate;
							temp.mscs_prod_kurs = kurs;
							temp.mscs_upp_eva	= upp_eva;
							temp.mscs_upp_kontes= upp_kontes;
							temp.mscs_upp_lain 	= upp_lain;
							
							//Yusuf - 4 Dec 09 - tgl komisi
							temp.mscs_comm_pay_date = commonDao.selectSysdateTruncated(0);
//						}
						
						//bila insert tanggal produksi, berarti mba murni boleh proses komisinya - Yusuf
						try {
//							email.send(false, 
//									props.getProperty("admin.ajsjava"),
//									new String[] {"murni@sinarmasmsiglife.co.id"},
////									"yusuf@sinarmasmsiglife.co.id",
////									new String[] {"yusuf@sinarmasmsiglife.co.id"},
//									null,
//									null, "Komisi Cross Selling Untuk Polis " + pc.mscs_policy_no + " Sudah Dapat Diproses", 
//									"Komisi Cross Selling Untuk Polis " + pc.mscs_policy_no + " Sudah Dapat Diproses" + "\n" +
//									"Tanggal Produksi : " + defaultDateFormat.format(prodDate), null);
							EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
									null, 0, 0, sysdate, null, 
									true, "ajsjava@sinarmasmsiglife.co.id", 
									new String[] {"murni@sinarmasmsiglife.co.id"},
									null,
									null, "Komisi Cross Selling Untuk Polis " + pc.mscs_policy_no + " Sudah Dapat Diproses", 
									"Komisi Cross Selling Untuk Polis " + pc.mscs_policy_no + " Sudah Dapat Diproses" + "\n" +
									"Tanggal Produksi : " + defaultDateFormat.format(prodDate), null, temp.reg_spaj);
						} catch (MailException e) {
							logger.error("ERROR :", e);
						}
						
						//bila mba murni sudah dinotifikasi, admin cabang dan agen penutupnya juga dinotifikasi via email
						boolean emailCabangValid = false;
						boolean emailAgenValid = false;

						//tarik email cabang dan email agen
						String emailCabang = selectEmailCabangSpajCrossSelling(pc.reg_spaj);
						
						String emailAgen = selectEmailAgenSpajCrossSelling(pc.reg_spaj);
						
						String alamat = props.getProperty("admin.yusuf");
						
						//validasi email agen
						if(emailAgen!=null) {
							if(!emailAgen.trim().equals("")) {
								if(emailAgen.toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
									emailAgenValid = true;
									alamat += ";" + emailAgen;
								}
							}
						}
						//validasi email cabang
						if(emailCabang!=null) {
							if(!emailCabang.trim().equals("")) {
								if(emailCabang.toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
									emailCabangValid = true;
									alamat += ";" + emailCabang;
								}
							}
						}
						//Deddy (25-sep-2012) Request via helpdesk by Yusy.
						alamat +=";"+"Yusy@sinarmasmsiglife.co.id";
						
						if(alamat != null) {
							if(!alamat.trim().equals("")) {
								try {
//									email.send(true, 
//											props.getProperty("admin.ajsjava"), 
//											alamat.split(";"),
////											"yusuf@sinarmasmsiglife.co.id",
////											new String[] {"yusuf@sinarmasmsiglife.co.id"},
//											null,
//											null, 
//											"Komisi Cross Selling Untuk Polis " + pc.mscs_policy_no + " Sedang Diproses oleh Bagian Finance", 
//											"Komisi Cross Selling Untuk Polis " + pc.mscs_policy_no + " Sedang Diproses oleh Bagian Finance" + "\n" +
//											"Tanggal Produksi : " + defaultDateFormat.format(prodDate), null);
									EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
											null, 0, 0, sysdate, null, 
											true, "ajsjava@sinarmasmsiglife.co.id", 
											alamat.split(";"),
											null,
											null, "Komisi Cross Selling Untuk Polis " + pc.mscs_policy_no + " Sudah Dapat Diproses", 
											"Komisi Cross Selling Untuk Polis " + pc.mscs_policy_no + " Sudah Dapat Diproses" + "\n" +
											"Tanggal Produksi : " + defaultDateFormat.format(prodDate), null, temp.reg_spaj);
								}catch(Exception e) {}
							}
						}
						
					}
					
					crossSellingDao.updateMstPolicyCs(temp);
					
					//3. INSERT MST_POSITION_CS
					PositionCs positionCs = new PositionCs();
					positionCs.reg_spaj 	= temp.reg_spaj;
					positionCs.mspc_date 	= sysdate;
					positionCs.lspd_id 		= temp.lspd_id;
					positionCs.lus_id 		= lus_id;
					positionCs.mspc_desc 	= "TRANSFER SPAJ (INPUT TGL PAY KE-" + temp.mscs_pay_count + ": " + defaultDateFormat.format(temp.mscs_tgl_terima_asm) + ")";
					crossSellingDao.insertMstPositionCs(positionCs);
					
				}
			}
		}
		return count + " SPAJ berhasil diproses. Harap perhatikan bahwa pengakuan produksi berlaku TAHUNAN.";
	}
		
	/**
	 * Transfer polis cross selling ke posisi berikutnya (khusus untuk posisi 79, 80, 81 saja, yang lainnya di proses lain) 
	 */
	public String transferCrossSelling(PolicyCs policyCs, int lus_id) throws DataAccessException{
		Date sysdate = commonDao.selectSysdate();
	
		/*
		LAMA
	     79 INPUT SPAJ (CROSS-SELLING)              
	     80 INPUT NOPOL ASM (CROSS-SELLING)         
	     81 TERIMA TTP (CROSS-SELLING)              
	     82 KONFIRMASI PAYMENT (CROSS-SELLING)      
	      8 AGENT COMMISSION                        
	     99 FILLING         

		BARU
	     79 INPUT SPAJ (CROSS-SELLING)              
	     80 INPUT NOPOL & TERIMA TTP (CROSS-SELLING)         
	     81 INPUT TGL PAYMENT/BSB (CROSS-SELLING)              
	     82 KONFIRMASI ASM (CROSS-SELLING)      
	      8 AGENT COMMISSION                        
	     99 FILLING                                 		
		 */		

		//1. UPDATE POSISI MST_POLICY_CS
		PolicyCs pc = new PolicyCs();
		pc.reg_spaj = policyCs.reg_spaj;
		pc.lspd_id = (
				policyCs.lspd_id.intValue() == 79 ? 80 : 
				policyCs.lspd_id.intValue() == 80 ? 81 : 
				policyCs.lspd_id.intValue() == 81 ? 82 : 0);
		
		if(pc.lspd_id.intValue() == 0) { //gak bisa ditransfer disini
			return "Polis ini sudah di posisi " + selectPosisiDokumenBySpaj(policyCs.reg_spaj) + ". Anda tidak bisa mentransfer melalui program ini.";
		}
		
		crossSellingDao.updateMstPolicyCs(pc);
		
		//2. INSERT MST_POSITION_CS
		PositionCs positionCs = new PositionCs();
		positionCs.reg_spaj 	= pc.reg_spaj;
		positionCs.mspc_date 	= sysdate;
		positionCs.lspd_id 		= pc.lspd_id;
		positionCs.lus_id 		= lus_id;
		positionCs.mspc_desc 	= "TRANSFER SPAJ";
		crossSellingDao.insertMstPositionCs(positionCs);
		
		return "Polis berhasil ditransfer ke " + selectPosisiDokumenBySpaj(policyCs.reg_spaj).lspd_position;
	}
	
	//Fungsi2 query untuk proses JATIS (PRATA)
	
	public List<String> selectDaftarKolomTabel(String tableName) throws DataAccessException{
		return query("selectDaftarKolomTabel", tableName);
	}
	
	public List<Map> selectDataJatis(String queryName, String incremental) throws DataAccessException{
		return query("select" + queryName, incremental);
	}
	
	//untuk testing saja
	/*
	public static void main(String[] args) throws FileNotFoundException, IOException {
		CrossSellingDao csd = new CrossSellingDao();
		csd.defaultDateFormatReversed = new SimpleDateFormat("yyyyMMdd");
		csd.props = new Properties();
		csd.props.load(new FileInputStream("D:\\Workspace\\E-Lions\\JavaSource\\ekalife.properties"));
		//
		csd.prosesGenerateTextFileJatis();
	}*/
		
	public void kirimUlangEmailJatis() throws DataAccessException{
		String nowString 	= "20100929010137";
		String now			= "29 Sep 2010";
		String dir 			= props.getProperty("pdf.dir.report") + "\\prata\\" + nowString;

		//email hasilnya
		File dirFile = new File(dir + "\\split");
        String[] zipList = dirFile.list();
        List<File> attachments = null;
        int count = 0;
        
        for(int i=1; i<=zipList.length; i++) {
        	File attachment = new File(dir + "\\split\\" + zipList[i-1]);
        	if(attachment.isFile()) count++;
        }
        
        //untuk setiap file, di email sekali
		for(int i=1; i<=zipList.length; i++) {
			File attachment = new File(dir + "\\split\\" + zipList[i-1]);
			if(attachment.isFile()){ //yg diemail file nya saja, yg directory jgn
				attachments = new ArrayList<File>();
				attachments.add(attachment);
				
				try {
					email.send(false, 
							props.getProperty("admin.ajsjava"),
							new String[] {"SYusuf@bankbii.com", "swahyuni@bankbii.com", "nsetiawan@bankbii.com"},
							//new String[] {props.getProperty("admin.yusuf")},
							new String[] {"yusufsutarko@yahoo.com", "himmia@yahoo.com"},
							//null,
							new String[] {props.getProperty("admin.yusuf")},
							"[Sinarmas MSIG Life] Data BII-Jatis Tanggal " +now+ " ("+i+"/"+count+")", 
							"Dear All,\n" +
							"Berikut data AJS. Mohon maaf atas keterlambatan pengirimannya.", 
							attachments);
				} catch (MailException e) {
					logger.error("ERROR :", e);
				} catch (MessagingException e) {
					logger.error("ERROR :", e);
				}
			}
		}
	}

	public List<Fat> selectListFat(Date begdate, Date enddate, String nama, Integer posisi, String lcb_no) throws DataAccessException{
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("begdate", begdate);
		m.put("enddate", enddate);
		m.put("nama", nama);
		if(posisi != null) if(posisi.intValue() > 0) m.put("posisi", posisi);
		if(lcb_no != null) if(!lcb_no.trim().toUpperCase().equals("SSS")) m.put("lcb_no", lcb_no);
		return query("selectListFat", m);
	}
	
	public Fat selectFat(String fatid) throws DataAccessException{
		return (Fat) querySingle("selectFat", fatid);
	}

	public List<FatHistory> selectFatHistory(String fatid) throws DataAccessException{
		return query("selectFatHistory", fatid);
	}

	public String saveFat(Fat fat, User user) throws DataAccessException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		String result = "";
		
		Date sysdate = commonDao.selectSysdate();
		
		//insert baru
		if(fat.fatid.equals("")){
			//tarik sequence terbaru, bila sudah mencapai 99999, reset ke 1 lagi
			int urut = (Integer) querySingle("selectSequenceLstFat", fat.ket_jbt);
			if(urut > 99999) urut = 1;

			DateFormat yymm = new SimpleDateFormat("yyMM");
			
			//insert fat
			fat.fatid = yymm.format(fat.act_date) + fat.ket_jbt + FormatString.rpad("0", String.valueOf(urut), 5);
			for(DropDown position : FatBacMultiController.getListJabatan()) 
				if(position.getKey().equals(fat.getKet_jbt())) fat.position = position.getValue();
			fat.getno = urut;
			fat.tgl_update = sysdate;
			fat.lus_id = Integer.valueOf(user.getLus_id());
			fat.create_date = sysdate;
			fat.posisi = 2;
			insert("insertLstFat", fat);
			
			//insert history
			FatHistory fh = new FatHistory(fat.fatid, fat.posisi, Integer.parseInt(user.getLus_id()), null, sysdate, "INPUT PENGAJUAN BST");
			insert("insertLstFatHistory", fh);
			
			result = "Data dengan id " + fat.fatid + " berhasil disimpan. Langkah berikutnya adalah OTORISASI.";
			
		//update data
		}else{
			//bila terminasi, maka pesannya sedikit berbeda
			if(fat.is_active != null){
				if(fat.is_active.intValue() == 0){
					//update fat, pindahkan posisi menjadi 6
					fat.posisi = 6;
					update("updateLstFat", fat);
					
					//insert history
					FatHistory fh = new FatHistory(fat.fatid, fat.posisi, Integer.parseInt(user.getLus_id()), null, sysdate, "TERMINASI PENGAJUAN BST");
					insert("insertLstFatHistory", fh);
					
					result = "Data dengan id " + fat.fatid + " berhasil di TERMINASI.";
					return result;
				}
			}
			
			//update fat
			update("updateLstFat", fat);
			
			//insert history
			FatHistory fh = new FatHistory(fat.fatid, fat.posisi, Integer.parseInt(user.getLus_id()), null, sysdate, "UPDATE PENGAJUAN BST");
			insert("insertLstFatHistory", fh);
			result = "Data dengan id " + fat.fatid + " berhasil diperbarui. Langkah berikutnya adalah OTORISASI.";
		}
		
		return result;
	}

	public void saveFatLetter(String fatid, User user) throws DataAccessException{
		
		Fat fat = selectFat(fatid);
		String roman = (String) querySingle("selectRomanMonthYear", null);
		Date sysdate = commonDao.selectSysdate();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat df2 = new SimpleDateFormat("MM");
		DateFormat df3 = new SimpleDateFormat("yyyy");
		
		//pertama cek dulu, apakah sudah ada row nya di tabel LST_FAT_LETTER
		int count = (Integer) querySingle("selectCountFatLetter", fatid);
		
		//bila tidak ada row, maka insert
		if(count == 0){
			FatLetter fl = new FatLetter();
			fl.fatid = fatid;
			fl.no_surat_generate = (Integer) querySingle("selectSequenceLstFatLetter", null);
			fl.generate_surat = 
				FormatString.rpad("0", String.valueOf(fl.no_surat_generate), 4) + "/" +
				"AJSMSIG-" + fat.ket_jbt + "/" + roman;
			fl.date_create = sysdate;
			fl.freq_view = 1;
			fl.list_date = df.format(sysdate);
			fl.bulan = df2.format(sysdate);
			fl.tahun = df3.format(sysdate);
			insert("insertLstFatLetter", fl);
		
		//bila sudah ada rownya, maka update saja
		}else{
			update("updateLstFatLetterPrint", fatid);
			
		}
		
		//insert history
		FatHistory fh = new FatHistory(fat.fatid, fat.posisi, Integer.parseInt(user.getLus_id()), null, sysdate, "CETAK PENGAJUAN BST");
		insert("insertLstFatHistory", fh);
		
	}
	
	public String otorisasiFat(List<Fat> listFat, User user) throws DataAccessException{
		int count = 0;
		Date sysdate = commonDao.selectSysdate();
		StringBuffer result = new StringBuffer();
		
		for(Fat fat : listFat){
			if(fat.isChecked()){
				count++;
				result.append("- [" + fat.fatid + "] " + fat.nama + "\n");
				
				//otorisasi
				Fat upd = new Fat();
				upd.fatid = fat.fatid;
				upd.posisi = 3; //dari otorisasi pindah ke posisi konfirmasi oleh hisar
				update("updateLstFat", upd);
				
				//insert history
				FatHistory fh = new FatHistory(upd.fatid, upd.posisi, Integer.parseInt(user.getLus_id()), null, sysdate, "OTORISASI PENGAJUAN BST");
				insert("insertLstFatHistory", fh);
			}
		}
		
		//setelah selesai proses, kirim dalam 1 buah email ke Hisar
		try {
			email.send(false, 
					props.getProperty("admin.ajsjava"),
					new String[] {props.getProperty("bancassurance.email.fat")},
					//new String[] {"yusuf@sinarmasmsiglife.co.id"},
					null,
					new String[] {"yusuf@sinarmasmsiglife.co.id"}, "[Pengajuan BST] Harap konfirmasi pengajuan BST", 
					"Harap lakukan proses KONFIRMASI pengajuan BST dibawah ini melalui menu ENTRY > UW > INPUT PENGAJUAN BST/BAC:\n\n" + result.toString(), null);
		} catch (MailException e) {
			logger.error("ERROR :", e);
		} catch (MessagingException e) {
			logger.error("ERROR :", e);
		}
		
		return count + " data berhasil di OTORISASI. Silahkan tunggu email konfirmasi dari pihak BANCASSURANCE SUPPORT kami untuk proses pencetakan surat.";
	}
	
	public String konfirmasiFat(List<Fat> listFat, User user) throws DataAccessException{
		int count = 0;
		Date sysdate = commonDao.selectSysdate();
		StringBuffer result = new StringBuffer();
		
		for(Fat fat : listFat){
			if(fat.isChecked()){
				count++;
				result.append("- [" + fat.fatid + "] " + fat.nama + "\n");
				
				//konfirmasi
				Fat upd = new Fat();
				upd.fatid = fat.fatid;
				upd.posisi = 4; //dari konfirmasi pindah ke posisi print
				update("updateLstFat", upd);
				
				//insert history
				FatHistory fh = new FatHistory(upd.fatid, upd.posisi, Integer.parseInt(user.getLus_id()), null, sysdate, "KONFIRMASI PENGAJUAN BST");
				insert("insertLstFatHistory", fh);
			}
		}
		
		//setelah selesai proses, kirim dalam 1 buah email dari Hisar ke orang cabang bersangkutan
		List<String> listEmail = selectSeluruhEmailCabBsm(user.getCab_bank());
		if(!listEmail.isEmpty()){
			try {
				email.send(false, 
						props.getProperty("admin.ajsjava"),
						(String[]) listEmail.toArray(),
						//new String[] {"yusuf@sinarmasmsiglife.co.id"},
						new String[] {"yusuf@sinarmasmsiglife.co.id"},
						null, "[Pengajuan BST] Silahkan cetak surat pengajuan BST", 
						"Harap lakukan proses pencetakan pengajuan BST dibawah ini melalui menu ENTRY > UW > INPUT PENGAJUAN BST/BAC:\n\n" + result.toString(), null);
			} catch (MailException e) {
				logger.error("ERROR :", e);
			} catch (MessagingException e) {
				logger.error("ERROR :", e);
			}
		}
		
		return count + " data berhasil di KONFIRMASI. Email konfirmasi kepada pihak BSM sudah dikirimkan untuk dapat melakukan pencetakan surat.";
	}
	
	public String fillingFat(List<Fat> listFat, User user) throws DataAccessException{
		int count = 0;
		Date sysdate = commonDao.selectSysdate();
		StringBuffer result = new StringBuffer();
		
		for(Fat fat : listFat){
			if(fat.isChecked()){
				count++;
				result.append("- [" + fat.fatid + "] " + fat.nama + "\n");
				
				//konfirmasi
				Fat upd = new Fat();
				upd.fatid = fat.fatid;
				upd.posisi = 5; //pindah ke posisi filling
				update("updateLstFat", upd);
				
				//insert history
				FatHistory fh = new FatHistory(upd.fatid, upd.posisi, Integer.parseInt(user.getLus_id()), null, sysdate, "FILLING PENGAJUAN BST");
				insert("insertLstFatHistory", fh);
			}
		}

		return count + " data berhasil di FILLING.";
	}
	
	public String terminasiFat(List<Fat> listFat, User user, String[] ket_term) throws DataAccessException{
		int count = 0;
		Date sysdate = commonDao.selectSysdate();
		StringBuffer result = new StringBuffer();
		
//		for(Fat fat : listFat){
		for(int i=0; i<listFat.size(); i++){
			Fat fat = listFat.get(i);
			if(fat.isChecked()){
				count++;
				result.append("- [" + fat.fatid + "] " + fat.nama + "\n\t\tKeterangan Terminasi : "+ ket_term[i] + "\n");
				
				//terminasi
				Fat upd = new Fat();
				upd.fatid = fat.fatid;
				upd.posisi = 6; //pindah ke posisi terminated
				upd.ket_term = ket_term[i];//isi field keterangan terminasi
				update("updateLstFat", upd);
				
				//insert history
				FatHistory fh = new FatHistory(upd.fatid, upd.posisi, Integer.parseInt(user.getLus_id()), null, sysdate, "TERMINASI PENGAJUAN BST");
				insert("insertLstFatHistory", fh);
			}
		}
		
		//setelah selesai proses, kirim email
		try {
			email.send(false, 
					props.getProperty("admin.ajsjava"),
					new String[] {props.getProperty("bancassurance.email.fat")},
					null,
					new String[] {"deddy@sinarmasmsiglife.co.id;antasari@sinarmasmsiglife.co.id"}, "[Pengajuan BST] Harap konfirmasi TERMINASI pengajuan BST", 
					"Harap lakukan proses konfirmasi terhadap TERMINASI pengajuan BST dibawah ini melalui menu ENTRY > UW > INPUT PENGAJUAN BST/BAC:\n\n" + result.toString(), null);
		} catch (MailException e) {
			logger.error("ERROR :", e);
		} catch (MessagingException e) {
			logger.error("ERROR :", e);
		}
		
		return count + " permintaan TERMINASI data telah berhasil dikirim.";		
	}
	
	//MANTA
	public String konftermFat(List<Fat> listFat, User user) throws DataAccessException{
		int count = 0;
		Date sysdate = commonDao.selectSysdate();
		
		for(Fat fat : listFat){
			if(fat.isChecked()){
				count++;
				
				//konfirmasi
				Fat upd = new Fat();
				upd.fatid = fat.fatid;
				upd.posisi = 7; //pindah ke posisi konfirmasi terminasi
				upd.is_active = 0; //set menjadi nonaktif
				update("updateLstFat", upd);
				
				//insert history
				FatHistory fh = new FatHistory(upd.fatid, upd.posisi, Integer.parseInt(user.getLus_id()), null, sysdate, "KONFIRMASI TERMINASI PENGAJUAN BST");
				insert("insertLstFatHistory", fh);
			}
		}
		
		return count + " data TERMINATED berhasil di KONFIRMASI.";
	}
	
	public List<String> selectSeluruhEmailCabBsm(String lcb_no) throws DataAccessException{
		return query("selectSeluruhEmailCabBsm", lcb_no);
	}
	
	public String selectValidasiInputFatDouble(String nama, Date tgl_lahir) throws DataAccessException{
		Map m = new HashMap();
		m.put("nama", nama);
		m.put("tgl_lahir", tgl_lahir);
		return (String) querySingle("selectValidasiInputFatDouble", m);
	}
	
	public void insertLstFatHistory(FatHistory fh) throws DataAccessException{
		insert("insertLstFatHistory", fh);
	}
	
	public Integer selectCountFatCetakHist(String fatid) throws DataAccessException{
		return (Integer) querySingle("selectCountFatCetakHist", fatid);
	}
	
	public Integer selectCountFatNameTagHist(String fatid) throws DataAccessException{
		return (Integer) querySingle("selectCountFatNameTagHist", fatid);
	}
	
}