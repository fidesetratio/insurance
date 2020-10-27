package com.ekalife.elions.web.bac;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.util.FileUtil;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.CommandPowersaveCair;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.PowersaveCair;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentFormController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Controller untuk Sistem Pencairan di tempat untuk Bank Sinarmas dan Sinarmas Sekuritas
 * Khusus produk2 powersave dan stable link
 * 
 * Pencairan stabil link ditambahkan sejak 21 Jul 09 (Yusuf)
 * Pencairan Power Save new (Yg bisa ditopup) ditambahkan sejak 5 Okt 11 (Yusuf)
 * 
 * @author Yusuf
 * @since Dec 22, 2008 (3:34:07 PM)
 */
public class PowersaveCairFormController extends ParentFormController {
	protected final Log logger = LogFactory.getLog( getClass() );

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		CommandPowersaveCair cpc = new CommandPowersaveCair();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		cpc.valid1 = elionsManager.selectUserName(currentUser.getValid_bank_1());
		cpc.valid2 = elionsManager.selectUserName(currentUser.getValid_bank_2());
		
		return cpc;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		List<DropDown> jenisNoncash = new ArrayList<DropDown>();
		jenisNoncash.add(new DropDown("", "")); 
		jenisNoncash.add(new DropDown("1", "Surrender Endorsement"));
		jenisNoncash.add(new DropDown("0", "Lainnya"));
		
		Map<String, List<DropDown>> map = new HashMap<String, List<DropDown>>();
		map.put("jenisNoncash", jenisNoncash);
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
		//VALIDASI DISINI TERBAGI 2, YAITU SAAT USER MENEKAN TOMBOL SHOW (SAAT BARU TAMPILKAN) UNTUK VALIDASI PER POLIS SECARA KESELURUHAN, 
		//DAN SAAT USER MENEKAN TOMBOL SAVE UNTUK VALIDASI PER TRANSAKSINYA
		CommandPowersaveCair cpc = (CommandPowersaveCair) command;
		cpc.reg_spaj = cpc.reg_spaj.trim().replace(".", "");
		int size = cpc.reg_spaj.length();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		int jn_bank = currentUser.getJn_bank().intValue();
		//Date sysdate = elionsManager.selectSysdate();
		
		//A. User menekan tombol show, validasi yang ada disini, hanyalah validasi terhadap polis secara keseluruhan,
		//	 bukan validasi terhadap setiap rollover yg mau dicairkan 
		if(request.getParameter("show") != null) {
			
			//A.1. Validasi menu, hanya bisa dipake user BSM / SMS
			if(!errors.hasErrors() && jn_bank != 2 && jn_bank != 3 && jn_bank != 16) {
				errors.reject("", "- Menu ini hanya dapat digunakan oleh Bank Sinarmas / Sinarmas Sekuritas / Bank Sinarmas Syariah.");
			}

			//A.2. Validasi panjang karakter untuk SPAJ / Polis harus benar
			if(!errors.hasErrors() && size != 11 && size != 14) {
				cpc.powersaveCair = null;
				errors.reject("","- Harap masukkan nomor NOMOR POLIS atau NOMOR REGISTER SPAJ yang benar");
			}

			//A.3. Validasi apakah data ada di sistem, sekaligus tarik lsbs_id
			if(!errors.hasErrors()) {
				if(size == 14) cpc.reg_spaj = elionsManager.selectSpajFromPolis(cpc.reg_spaj); //cari dulu no spaj nya bila yg dimasukkan nomor polis
				String tmp = uwManager.selectBusinessId(cpc.reg_spaj);
				if(tmp != null) cpc.lsbs_id = Integer.valueOf(tmp); //tarik lsbs_id nya
				else errors.reject("", "- Data tidak ada. Harap pastikan nomor yang dimasukkan benar.");
			}
			
			//A.4. Validasi akses user terhadap spaj, khusus BSM & SEKURITAS saja
			if(!errors.hasErrors()) {
				if(elionsManager.selectIsUserYangInputBank(cpc.reg_spaj, Integer.valueOf(currentUser.getLus_id())) <= 0 && (!currentUser.getCab_bank().trim().equals("SSS") || !currentUser.getCab_bank().trim().equals("M35"))) {
					if(currentUser.getJn_bank().intValue() == 2 || currentUser.getJn_bank().intValue() == 16){ 
						errors.reject("","- Anda tidak mempunyai akses terhadap Polis ini. Polis ini hanya dapat diakses oleh cabang " + 
							elionsManager.selectCabangBiiPolis(cpc.reg_spaj));
					}else if(currentUser.getJn_bank().intValue() == 3){
						errors.reject("","- Anda tidak mempunyai akses terhadap Polis ini. Polis ini hanya dapat diakses oleh cabang " + 
								elionsManager.selectCabangBiiPolis(cpc.reg_spaj));
					}else if(currentUser.getJn_bank().intValue() == 16){
						errors.reject("","- Anda tidak mempunyai akses terhadap Polis ini. Polis ini hanya dapat diakses oleh cabang " + 
								elionsManager.selectCabangBiiPolis(cpc.reg_spaj));
					}
				}
			}
			
			//A.5. Validasi produk, hanya boleh powersave / stable link
			if(!errors.hasErrors() && !(products.powerSave(cpc.lsbs_id.toString()) || products.stableLink(cpc.lsbs_id.toString()))){
				errors.reject("","- Polis yang Anda masukkan bukan produk SAVE maupun STABLE LINK.");
			}

			//A.6. Validasi inforce dan batal polis, sekaligus menarik data polis secara keseluruhan (bukan per transaksi ya)
			if(!errors.hasErrors()) {
				cpc.powersaveCair = elionsManager.selectRolloverData(cpc.reg_spaj);
				
				if(cpc.powersaveCair == null) {
					errors.reject("", "- Data tidak ada. Harap pastikan nomor yang dimasukkan benar.");
				}else if(cpc.powersaveCair.lssp_id.intValue() != 1) {
					String pesan = "UNDERWRITING";
					if(cpc.powersaveCair.lssp_id.intValue() == 5 || cpc.powersaveCair.lssp_id.intValue() == 7) pesan = "LIFE BENEFIT"; //CCV, CWCV
					errors.reject("", "- Status Polis bukan INFORCE melainkan " + cpc.powersaveCair.lssp_status + ". Harap dikonfirmasi dengan dept " + pesan);
				}else if(cpc.powersaveCair.lspd_id.intValue() == 95) {
					errors.reject("", "- Polis yang Anda masukkan sudah dibatalkan. Harap dikonfirmasi dengan dept UNDERWRITING.");
				}else{
					//tambahkan sedikit detail untuk informasi rekening
					cpc.powersaveCair.mpc_note = 
						"TRANSFER KE REKENING " + cpc.powersaveCair.lsbp_nama + 
						" CAB. " + cpc.powersaveCair.mrc_cabang + 
						"-" + cpc.powersaveCair.mrc_kota + 
						", A/C." + cpc.powersaveCair.mrc_no_ac + 
						"(" + cpc.powersaveCair.lku_symbol + 
						"), A/N. " + cpc.powersaveCair.mrc_nama;
				}
			}
			
			//A.7. Validasi tidak boleh ada di pinjaman (alias udah cair)
			if(!errors.hasErrors()) {
				int hitung = elionsManager.selectValidasiPinjaman(cpc.reg_spaj);
				if(hitung > 0) {
					errors.reject("", "- Polis yang Anda masukkan pencairannya sudah diproses dipusat. Harap dikonfirmasi dengan departemen Life Benefit AJS.");
				}
			}
			
			//A.8. Validasi status harus AKSEP bila pencairan full
			if(!errors.hasErrors()) {
				int lssa_id = elionsManager.selectStatusAksep(cpc.reg_spaj);
				
				//untuk powersave, karena preminya hanya satu, jadi hanya boleh pencairan apabila sudah AKSEPTASI 
				if((products.powerSave(cpc.lsbs_id.toString()) || products.stableLink(cpc.lsbs_id.toString())) && cpc.lsbs_id.intValue() != 188 && lssa_id != 5) {
					String produk_bsm = "SIMAS PRIMA";
					if(currentUser.getJn_bank().intValue()==3){
						produk_bsm = "DANAMAS PRIMA";
					}else if(currentUser.getJn_bank().intValue()==16){
						produk_bsm = "POWERSAVE SYARIAH BSM";
					}
					errors.reject("", "- Polis "+produk_bsm+" yang dapat dicairkan hanya polis yang sudah di-AKSEPTASI. Harap dikonfirmasi dengan departemen Underwriting AJS.");
				}
			}
			
			//A.9. Yusuf (24/02/12) Request Rudy, validasi apakah ada pinjaman konvensional, bila ada tidak boleh diinput pencairan (harus proses dipusat)
			if(!errors.hasErrors()) {
				int hitung = uwManager.selectValidasiPinjamanKonvensional(cpc.reg_spaj);
				if(hitung > 0) {
					errors.reject("", "- Polis yang Anda masukkan mempunyai pinjaman konvensional. Pencairan hanya bisa diproses pada kantor pusat AJS. Harap dikonfirmasi dengan departemen Life Benefit AJS.");
				}
			}
			
			//reset sebelum tarik data
			cpc.daftarPremi = null;
			cpc.daftarPremiSusulan = null;
			
			//A.10. Validasi ada data Save/Stable, sekaligus tarik data detail per transaksi premi 
			if(!errors.hasErrors()) {

				// pengecekan powersave new (Yusuf) 188 baru sampai sini
				
				//cari yang max deposit date (normal) atau yg flag_aktif = 1 untuk slink
				
				// Adrian: pencairan Simas Prima cair dari rekening escrow periode Libur Idul Fitri 2019
				String mulai_liburan="";
				String akhir_liburan="";
				mulai_liburan = props.getProperty( "mulai_liburan" );
				akhir_liburan = props.getProperty( "akhir_liburan" );
				Date sysdate = elionsManager.selectSysdate(0);
				
				if(sysdate.after( defaultDateFormat.parse(mulai_liburan) ) && sysdate.before( defaultDateFormat.parse(akhir_liburan) ) ){
					cpc.daftarPremi = bacManager.selectRolloverPeriodeLibur(cpc.reg_spaj);  
				}else{
					//tgl normal
					cpc.daftarPremi = uwManager.selectRolloverNormal(cpc.reg_spaj);  
				}
				
				//bila gak ada data / null
				if(cpc.daftarPremi == null) {
					errors.reject("", "- Data tidak ada. Harap pastikan nomor yang dimasukkan benar.");
				}else if(cpc.daftarPremi.isEmpty()) {
					errors.reject("", "- Data tidak ada. Harap pastikan nomor yang dimasukkan benar.");
				
				//bila ada data, maka tambahkan perhitungan2 penting dibawah
				}else {
					//untuk setiap transaksi lakukan hal2 dibawah
					for(PowersaveCair p : cpc.daftarPremi){
						//centang dulu, bila cuman satu yg keluar
						if(cpc.daftarPremi.size() == 1) p.centang = true;
						
						//beri warna untuk flag_posisi dari powersave cair
						p.warna = elionsManager.selectPosisiPowersaveCair(p.reg_spaj, p.mpc_urut, p.mpc_bdate);
						
						//tarik no reg nya
						p.mpc_reg = uwManager.selectNoRegPencairan(p.reg_spaj, p.mpc_urut, p.mpc_bdate);

						//tandai apakah pencairan ini non cash
						p.mpc_noncash = elionsManager.selectNonCashPowersaveCair(p.reg_spaj, p.mpc_urut, p.mpc_bdate);
						
						//harus cek udah ada bayar premi belum, kalo udah, set bunga nya 0, tapi bunga tambahan harian tetap ada (bila ada)
						if(p.mpr_bayar_prm.doubleValue() > 0) {
							p.mpc_bunga = 0.;
						}
						
						//apabila ada di MST_PROSAVE_BAYAR/MST_SLINK_BAYAR, berarti ada bayar bunga, ini udah digabung di query selectRolloverPowersaveXXX
						//disini hanya untuk ngecek apakah query tsb nilai nya null apa gak
						if(p.mpc_kurang == null) 		p.mpc_kurang = 0.;
						if(p.mpc_bunga == null) 		p.mpc_bunga = 0.;
						if(p.mpc_tambah == null) 		p.mpc_tambah = 0.;
						if(p.mpc_rider_total == null) 	p.mpc_rider_total = 0.;
						
						//START OF PERHITUNGAN BONUS PERFORMANCE (BP) khusus slink
						//perhitungan BP ini, hanya dijalankan apabila NAB H-3 sudah ada, bila tidak ada tetap kosong
						List<Powersave> daftarRo = uwManager.selectStableLinkUntukPerhitunganBP(cpc.reg_spaj, p.mpc_tu_ke);
						if(!daftarRo.isEmpty()){
							Powersave roTerakhir = daftarRo.get(daftarRo.size()-1);
							
							//Yusuf (03/12/09) - request Rudy, bila bunga manfaat bulanan terakhir belum diproses,maka harus ditambahkan ke bunganya disini
							//Yusuf (14/12/09) - request Rudy, menyamakan kondisi antara bukan manf bulanan dgn manf bulanan, manf bulanan itu bunganya diambil dari slink bayar
							if(p.flag_bulanan.intValue() == 1){
								HashMap temp = uwManager.selectJumlahBayarManfaatBulananTerakhir(p.reg_spaj, p.mpc_urut, p.mpc_edate);
								if(temp != null){
									BigDecimal bungaTambahan = ((BigDecimal) temp.get("MSLB_JUM_BAYAR"));
									double flag_proses = ((BigDecimal) temp.get("FLAG_PROSES")).doubleValue();
									if(bungaTambahan == null) bungaTambahan = new BigDecimal(0);
									
									p.mpc_bunga = bungaTambahan.doubleValue();
									roTerakhir.msl_bunga = bungaTambahan.doubleValue();
									p.mpr_bayar_prm = flag_proses;
									if(flag_proses == 1) {
										p.mpc_bunga = 0.;
									}
								}
								
								//Yusuf (01/02/2010) - request Rudy, bila manfaat bulanan, mpc_kurang nya diambil dari tambahan bunga manfaat bulanan terakhir di slink bayar
								Map temp2 = uwManager.selectPengurangManfaatBulananTerakhir(p.reg_spaj, p.mpc_urut, p.mpc_edate);
								if(temp2 != null){
									BigDecimal pengurang = ((BigDecimal) temp.get("MSLB_TAMBAH"));
									if(pengurang != null) p.mpc_kurang = pengurang.doubleValue();
								}
							}
														
							//Pertama, tarik dulu NAB dari EDATE - 3
							Map data_nab = (HashMap) elionsManager.select_tminus(p.lji_id, p.mpc_edate, 3);
							if (data_nab != null){
								p.mpc_tgl_nab_bp	= (Date)	data_nab.get("LNU_TGL");
								p.mpc_nab_bp		= (Double)	data_nab.get("LNU_NILAI");

								//Kedua, hitung dulu NILAI POLIS = NILAI UNIT AWAL x NAB BP
								p.mpc_nilai_polis 	= roTerakhir.msl_unit * p.mpc_nab_bp;
								
								//Ketiga, tambahkan premi dan bunga yang didapat
								Double premiBunga	= roTerakhir.msl_premi + p.mpc_bunga;

								//Keempat, hitung selisih antara nilai polis dgn total dari (premi + bunga)
								Double selisih		= p.mpc_nilai_polis - (roTerakhir.msl_premi + roTerakhir.msl_bunga);
								
								//Kelima, dari selisih tersebut, dikalikan dengan persentase BP
								//awalnya, hitung dulu bpRate(bpRate ini adalah persentase yg didapat PT (BP PT), bukan yg didapat nasabah (BP)
								double bpRate 		= uwManager.getBPrate(p.mpc_bdate, p.mgi, roTerakhir.flag_bulanan);

								//Yusuf - 10 Aug 09 - Request Rudy, bila sudah bayar BP, maka BP = 0%, BP PT = 100%
								if(roTerakhir.msl_bayar_bp.intValue() == 1){
									bpRate = 100;
								}
								
								if(selisih > 0){
									//khusus MGI 1 bulan, bila kelipatan 3 baru dihitung BP nya, kalo gak, gak dihitung
									if(p.mgi.intValue() == 1){
										//kalau kelipatan 3x rollover
										if(daftarRo.size()%3 == 0){
											p.mpc_bp 	= (100 - bpRate) / 100 * selisih;
											p.mpc_bp_pt	= bpRate / 100 * selisih;

											//Yusuf - 10 Aug 09 - Request Rudy
											//kalo udah bayar bunga maka tambah biaya investasi nya dgn bunga
											if(p.mpr_bayar_prm.doubleValue() > 0) {
												p.mpc_bp_pt = p.mpc_bp_pt + roTerakhir.msl_bunga;
											}
											//kalo udah bayar BP maka tambah lagi biaya investasi nya dgn BP
											if(roTerakhir.msl_bayar_bp.intValue() == 1) {
												p.mpc_bp_pt = p.mpc_bp_pt + p.mpc_bp;
												p.mpc_bp = 0.;
											}
										//	
										}else{
											//BP tidak dihitung karena belum kelipatan 3 (jadi BP PT = 100%)
											p.mpc_bp 	= 0.;
											
											//kalo sudah bayar bunga, maka BP PT = selisihnya + bunga
											if(p.mpr_bayar_prm.doubleValue() > 0) {
												p.mpc_bp_pt	= selisih + roTerakhir.msl_bunga; // (100%)	+ bunga
											//kalo belom bayar bunga, maka BP PT = selisihnya + bunga
											}else{
												p.mpc_bp_pt	= selisih; // (100%)
											}
										}
									//untuk MGI selain 1 bulan
									}else{
										p.mpc_bp 		= (100 - bpRate) / 100 * selisih;
										p.mpc_bp_pt		= bpRate / 100 * selisih;

										//Yusuf - 10 Aug 09 - Request Rudy
										
										//kalo udah bayar bunga maka tambah biaya investasi nya dgn bunga
										if(p.mpr_bayar_prm.doubleValue() > 0) {
											p.mpc_bp_pt = p.mpc_bp_pt + roTerakhir.msl_bunga;
										}
										//kalo udah bayar BP maka tambah lagi biaya investasi nya dgn BP
										if(roTerakhir.msl_bayar_bp.intValue() == 1) {
											p.mpc_bp_pt = p.mpc_bp_pt + p.mpc_bp;
											p.mpc_bp = 0.;
										}
									}
								//bila ternyata nilainya lebih kecil, maka nilai polis adalah premi + bunga saja
								}else{
									p.mpc_bp 			= 0.;
									p.mpc_bp_pt			= 0.;
									p.mpc_nilai_polis 	= premiBunga;
								}
								
								//only for testing
								/*
								logger.info("PREMI KE = " + p.mpc_tu_ke);
								logger.info("TGL NAB	BP = " + defaultDateFormat.format(p.mpc_tgl_nab_bp));
								logger.info("NAB BP (A) = " + twoDecimalNumberFormat.format(p.mpc_nab_bp));
								logger.info("UNIT AWAL (B) = " + twoDecimalNumberFormat.format(roTerakhir.msl_unit));
								logger.info("NILAI POLIS (C=AxB) = " + twoDecimalNumberFormat.format(roTerakhir.msl_unit * p.mpc_nab_bp));
								logger.info("PREMI (D) = " + twoDecimalNumberFormat.format(roTerakhir.msl_premi));
								logger.info("BUNGA (E) = " + twoDecimalNumberFormat.format(roTerakhir.msl_bunga));
								logger.info("PREMI dan BUNGA (F=D+E)	= " + twoDecimalNumberFormat.format(roTerakhir.msl_premi + roTerakhir.msl_bunga));
								logger.info("SELISIH (G=C-F) = " + twoDecimalNumberFormat.format((roTerakhir.msl_unit * p.mpc_nab_bp) - (roTerakhir.msl_premi + roTerakhir.msl_bunga)));
								logger.info("BP RATE = " + twoDecimalNumberFormat.format(bpRate));
								logger.info("BP = " + twoDecimalNumberFormat.format((100 - bpRate) / 100 * ((roTerakhir.msl_unit * p.mpc_nab_bp) - (roTerakhir.msl_premi + roTerakhir.msl_bunga))));
								logger.info("BP PT = " + twoDecimalNumberFormat.format(bpRate / 100 * ((roTerakhir.msl_unit * p.mpc_nab_bp) - (roTerakhir.msl_premi + roTerakhir.msl_bunga))));
								logger.info("----------------------------");
								logger.info("MPC_TGL_NAB_BP = " + defaultDateFormat.format(p.mpc_tgl_nab_bp));
								logger.info("MPC_NAB_BP = " + twoDecimalNumberFormat.format(p.mpc_nab_bp));
								logger.info("MPC_NILAI_POLIS = " + twoDecimalNumberFormat.format(p.mpc_nilai_polis));
								logger.info("MPC_BP = " + twoDecimalNumberFormat.format(p.mpc_bp));
								logger.info("MPC_BP_PT = " + twoDecimalNumberFormat.format(p.mpc_bp_pt));
								*/
								//only for testing
								
							}
							
						}
						//END OF PERHITUNGAN BONUS PERFORMANCE (BP)
												
						//(DEDDY): Tambahan nilai premi rider powersave dan melakukan pengecekan
						//(YUSUF) bagian ini baru implementasi untuk powersave, belum untuk stabil link -> premi rider nempel di premi utama, jadi kalo premi utamanya cair, ridernya inaktif
						Integer cekTotalRider = uwManager.selectCountTotalRider(p.reg_spaj);
						if(products.powerSave(cpc.lsbs_id.toString()) && cekTotalRider != 0){
							for(int i=0;i<cekTotalRider;i++){
								List<Map> daftar_premi_rider = uwManager.selectPremiRiderInMstRiderSave(p.reg_spaj);
								Map hasil_daftar = daftar_premi_rider.get(i);
								BigDecimal premi_rider = (BigDecimal) hasil_daftar.get("MRS_PREMI");
								p.mpc_rider_total = p.mpc_rider_total + premi_rider.doubleValue();
							}
							//apabila ada rider, maka dicek apakah new business ato tidak
							//(pake cara ini, karena klo dicek di ekatest msbi_premi_ke ada yg tidak diisi, jadi biar tidak ragu,cek saja countnya ada berapa, 1=new business dan >1 bukan new business)
							Integer cekBusiness = uwManager.selectCountNewBusiness(p.reg_spaj);
							if(cekBusiness==1){
								Map mappingRiderSave = uwManager.select_rider_save(p.reg_spaj);
								if(mappingRiderSave!=null){
									BigDecimal caraBayar = (BigDecimal) mappingRiderSave.get("MRS_RIDER_CB");
									if(caraBayar==null){
										p.total_bunga = p.mpc_bunga + p.mpc_tambah - p.mpc_kurang + p.mpc_bp;
									}else {
										//jika cara bayar 0(bayar langsung) || 2(bayar langsung sekaligus)
										if(caraBayar.intValue()==0 || caraBayar.intValue()==2){
											p.total_bunga = p.mpc_bunga + p.mpc_tambah - p.mpc_kurang + p.mpc_bp;
										}
										//jika cara bayar 1(potongan bunga)
										else if(caraBayar.intValue()==1){
											p.total_bunga = p.mpc_bunga + p.mpc_tambah - p.mpc_kurang-p.mpc_rider_total + p.mpc_bp;
										}
									}
								}
								
							}else if(cekBusiness>1){
								p.total_bunga = p.mpc_bunga + p.mpc_tambah - p.mpc_kurang-p.mpc_rider_total + p.mpc_bp;
							}
						}else {
							//terakhir, hitung ulang total bunga, buat jaga2 aja
							//untuk yg slink, bunga harap dipisah bunga dan BP (buat baru, mpc_bp)
							p.total_bunga = p.mpc_bunga + p.mpc_tambah - p.mpc_kurang + p.mpc_bp;
						}
						
					}
					
					if(jn_bank == 2){
						errors.reject("", "- Silahkan melanjutkan dengan memasukkan USERNAME dan PASSWORD otorisasi, kemudian menekan tombol SIMPAN");
					}else if(jn_bank == 3){
						errors.reject("", "- Silahkan melanjutkan dengan memilih rollover yang ingin dicairkan, kemudian menekan tombol SIMPAN");
					}else if(jn_bank == 16){
						errors.reject("", "- Silahkan melanjutkan dengan memasukkan USERNAME dan PASSWORD otorisasi, kemudian menekan tombol SIMPAN");
					}
				}
				
			}
			if(!errors.hasErrors()) {
				Integer line_bus = uwManager.selectLineBusLstBisnis(cpc.lsbs_id.toString());//jika line_bus 3 berarti syariah.
				if(line_bus==3){
					cpc.kategori_company="Syariah";
				}else{
					cpc.kategori_company="";
				}
			}
		
		//B. User menekan tombol save, disinilah banyak validasi2 data dilakukan per item transaksi
		}else if(request.getParameter("save") != null) {
			
			//B.1. validasi password SPV dan KACAB diperlukan untuk input pencairan
			if(jn_bank == 2){
				String userotor = ServletRequestUtils.getStringParameter(request, "userotor", "");
				String passotor = ServletRequestUtils.getStringParameter(request, "passotor", "");
				if(userotor.trim().equals("") || passotor.trim().equals("")){
					errors.reject("", "- Silahkan lengkapi USERNAME dan PASSWORD otorisasi");
				}else{
					if(userotor.trim().toUpperCase().equals(cpc.valid1) || userotor.trim().toUpperCase().equals(cpc.valid2)){
						if(!passotor.toUpperCase().equals(uwManager.selectPasswordFromUsername(userotor))){
							errors.reject("", "- Password otorisasi yang dimasukkan salah. Harap ulangi kembali");
						}else{
							// Adrian: Upd lus_online LST_USER untuk SPV Authorization user login BSM
							Date sysdate = elionsManager.selectSysdateSimple();	
							if( userotor.trim().toUpperCase().equals(cpc.valid1) ){
								elionsManager.updateUserOnlineStatus(sysdate, currentUser.getValid_bank_1().toString() );
							}else if( userotor.trim().toUpperCase().equals(cpc.valid2) ){
								elionsManager.updateUserOnlineStatus(sysdate, currentUser.getValid_bank_2().toString() );
							}							
						}
					}else{
						errors.reject("", "- Username yang dimasukkan tidak terdaftar sebagai SPV / Pincab Anda. Mohon dicek kembali");
					}
				}
			}
			/*if(jn_bank == 16){
				String userotor = ServletRequestUtils.getStringParameter(request, "userotor", "");
				String passotor = ServletRequestUtils.getStringParameter(request, "passotor", "");
				if(userotor.trim().equals("") || passotor.trim().equals("")){
					errors.reject("", "- Silahkan lengkapi USERNAME dan PASSWORD otorisasi");
				}else{
					if(userotor.trim().toUpperCase().equals(cpc.valid1) || userotor.trim().toUpperCase().equals(cpc.valid2)){
						if(!passotor.toUpperCase().equals(uwManager.selectPasswordFromUsername(userotor))){
							errors.reject("", "- Password otorisasi yang dimasukkan salah. Harap ulangi kembali");
						}
					}else{
						errors.reject("", "- Username yang dimasukkan tidak terdaftar sebagai SPV / Pincab Anda. Mohon dicek kembali");
					}
				}
			}*/
			//Validasi2 per item transaksi
			if(!errors.hasErrors()) {
				int jmlCentang = 0;
				//NumberFormat nf = NumberFormat.getNumberInstance();
				
				for(PowersaveCair p : cpc.daftarPremi) {
					//lakukan validasi hanya untuk yang dipilih saja
					if(p.centang) {
						jmlCentang++;
						String premi_ke = (p.mpc_tu_ke.intValue() == 0 ? "PREMI POKOK" : ("PREMI TOP-UP KE "+p.mpc_tu_ke));
						
						//B.1.1. tidak boleh ada di mst_powersave_cair, alias udah pernah diproses sebelumnya
						if(!elionsManager.selectValidasiPowersaveCair(p.reg_spaj, p.mpc_urut, p.mpc_bdate)) {
							errors.reject("", "- " + premi_ke + ": Input pencairan sudah pernah dilakukan. Data tidak bisa disimpan");
						}
						
						//B.1.2. Bila Pencairan Non Cash, harus pilih jenis non cash nya
						if(!elionsManager.selectValidasiPowersaveCair(p.reg_spaj, p.mpc_urut, p.mpc_bdate)) {
							if(p.mpc_noncash != null){
								if(p.mpc_noncash.intValue() == 0){
									if(p.mpc_jenis_noncash == null){
										errors.reject("", "- " + premi_ke + ": Untuk pencairan NONCASH silahkan pilih JENIS NONCASH.");
									}
								}
							}
						}
						
						//B.2. Validasi2 waktu ada disini semua, disini bagian validasi yg krusial
						/*
						 * Contoh hasil EKA.WORKDAYS = H+1 = 2, H = 1, H-3 = -3, H-7 = -7 
						 * 
						 * Pengertian istilah di sistem AJS :
						 * - Tanggal JATUH TEMPO 	= EDATE atau MATURE DATE atau (BATAS DATE - 1)
						 * - Tanggal CAIR AKTUAL	= (EDATE+1) atau (MATURE DATE+1) dimana +1 disini adalah +1 hari kerja
						 * 
						 * Pengertian Bank Sinarmas : 
						 * - Tanggal JATUH TEMPO	= (EDATE+1) atau (MATURE DATE+1) dimana +1 disini adalah +1 hari kerja
						 * 
						 * Validasi2 di sistem untuk BSM saat ini, masih menggunakan tanggal JATUH TEMPO pengertian sistem AJS 
						 */

						//only for testing
						/*
						logger.info("EDATE = " + defaultDateFormat.format(p.mpc_edate));
						logger.info("SYSDATE = " + defaultDateFormat.format(new Date()));
						logger.info("SELISIH_HARI = " + p.selisih_hari);
						*/
						//only for testing
						
						if(!errors.hasErrors()) {
							//Date haMinTiga 		= uwManager.selectAddWorkdays(p.mpc_edate, -3);
							Date haMinTujuh 	= uwManager.selectAddWorkdays(p.mpc_edate, -7);
							Date haPlusTujuh 	= uwManager.selectAddWorkdays(p.mpc_edate, 7);

							//B.2.1. Validasi2 waktu untuk Stable Link 
							if(products.stableLink(cpc.lsbs_id.toString())){
								
								//Yusuf (27/07/09) - Request Himmia : NAB H-3 tidak perlu divalidasi, bila ada dihitung, bila tidak ya tidak dihitung BP nya

								//cek dulu nab EDATE - 3 hari kerja, ada gak tgl NAB nya
								//Map data_nab = (HashMap) elionsManager.select_tminus(p.lji_id, p.mpc_edate, 3);
								
								//if(data_nab == null){
								//	errors.reject("", "- " + premi_ke + ": NAB (" + defaultDateFormat.format(haMinTiga) + ") tidak ada. Pencairan hanya bisa diinput H-3 (" + defaultDateFormat.format(haMinTiga) + ") sampai dengan H-0 (" + defaultDateFormat.format(p.mpc_edate) + ") dari tanggal JATUH TEMPO (" + defaultDateFormat.format(p.mpc_edate) + ").");
								//}

								//Yusuf (15/08/2012) - Karena admin SMS semuanya idiot piece of shit, dan sudah dibilangin tapi gak pernah ngerti juga, as of now validasi waktu dibuka saja.
								if(jn_bank != 3 && jn_bank != 16){
									if(p.selisih_hari > 7 || p.selisih_hari < 1){ //bila belum H-7 (masih kepagian) atau sudah lewat dari tanggal JATUH TEMPO (H-0)
										errors.reject("", "- " + premi_ke + ": Pencairan hanya bisa diinput H-7 (" + defaultDateFormat.format(haMinTujuh) + ") sampai dengan H-0 dari tanggal JATUH TEMPO (" + defaultDateFormat.format(p.mpc_edate) + ").");
									}
								}
	
							//B.2.2. Validasi2 waktu untuk PowerSave 
							}else if(products.powerSave(cpc.lsbs_id.toString())){
								//Yusuf (15/08/2012) - Karena admin SMS semuanya idiot piece of shit, dan sudah dibilangin tapi gak pernah ngerti juga, as of now validasi waktu dibuka saja.
								if(jn_bank != 3 && jn_bank!=16){
									if(p.mpc_ro.intValue() == 3 && (p.selisih_hari > 7 || p.selisih_hari < -7)){ //khusus AUTOBREAK : bila belum H-7 (masih kepagian) atau lewat 7 hari dari tanggal JATUH TEMPO (H+7) 
										errors.reject("", "- " + premi_ke + ": Untuk jenis rollover AUTOBREAK, pencairan hanya bisa diinput H-7 (" + defaultDateFormat.format(haMinTujuh) + ") sampai dengan H+7 (" + defaultDateFormat.format(haPlusTujuh) + ") dari tanggal JATUH TEMPO (" + defaultDateFormat.format(p.mpc_edate) + ").");
									}else if(p.selisih_hari > 7 || p.selisih_hari < 1){ //bila belum H-7 (masih kepagian) atau sudah lewat dari tanggal JATUH TEMPO (H-0)
										errors.reject("", "- " + premi_ke + ": Pencairan hanya bisa diinput H-7 (" + defaultDateFormat.format(haMinTujuh) + ") sampai dengan H-0 dari tanggal JATUH TEMPO (" + defaultDateFormat.format(p.mpc_edate) + ").");
									}
								}
	
							//B.2.3. Bila bukan dua2nya
							}else{
								errors.reject("","- Polis yang Anda masukkan bukan produk SAVE maupun STABLE LINK.");
							}
							
							//B.2.4. Validasi Tambahan saja, untuk memastikan ulang bahwa pencairan tdk bisa diinput apabila sudah lewat JT
							Date sysdate = elionsManager.selectSysdate(0);
							if(sysdate.after(p.mpc_edate)){
								errors.reject("", "- " + premi_ke + ": Pencairan hanya bisa diinput maksimal sampai dengan tanggal JATUH TEMPO (" + defaultDateFormat.format(p.mpc_edate) + ").");
							}
							
							//B.2.5. Yusuf (27/01/10) Validasi bila topup, maka harus posisi 78 (req Rudy)
							if(p.msl_new != null){
								if(p.msl_new.intValue() == 2 && p.msl_posisi.intValue() != 78){
									errors.reject("", "- " + premi_ke + ": Pencairan hanya bisa diinput untuk Top-up yang sudah diproses oleh Dept Finance AJS.");
								}
							}
							
							//B.2.6. Yusuf (24/02/10) Request Rudy, semua pencairan bsm hanya boleh kalo udah payment, jadi harus cek paid = 1 di billing nya
							//BUKAN PAKE TAHUN KE PREMI KE TERAKHIR, MELAINKAN PAKAI YANG MPS KODE NYA 5
							int msbi_paid = uwManager.selectStatusPaidBilling(p.reg_spaj, p.mpc_tu_ke);
							if(msbi_paid != 1){
								errors.reject("", "- " + premi_ke + ": Pencairan hanya bisa diinput untuk Transaksi yang data pembayarannya sudah selesai. Silahkan hubungi Dept Finance AJS.");
							}
							
						}
						
					}
				}
				//B.3. Validasi harus pilih salah satu
				if(jmlCentang == 0) {
					errors.reject("", "- Harap pilih/centang minimal satu premi/rollover pada kolom \"INFORMASI ROLLOVER TERAKHIR\" untuk dicairkan.");
				}
				
				//B.4. Validasi untuk stable link, boleh pencairan sebagian, tapi gak boleh pencairan full
				boolean cairAll = true;
				if(products.stableLink(cpc.lsbs_id.toString())){
					int lssa_id = elionsManager.selectStatusAksep(cpc.reg_spaj);
					Map data = uwManager.selectDataUsulan(cpc.reg_spaj);
					Integer lsdbs_number = (Integer) data.get("LSDBS_NUMBER");
					List<Map> sisaPremiStableLink = uwManager.selectSisaPremiStableLink(cpc.reg_spaj);
					
					if(sisaPremiStableLink.size() <= jmlCentang) cairAll = true; else cairAll = false;
					//Req Budi LB - 84814 , Produk 164-11 (New Simas Stabil Link) tidak boleh diinput cair dari cabang.
					if(cpc.lsbs_id==164 && lsdbs_number==11){
						cairAll = false;
						errors.reject("", "- Khusus New Simas Stabil Link ( 164 -11 ), Pencairan hanya dapat dilakukan di pusat. Hubungi departemen Life Benefit AJS Kamarudinsyah - Kamarudinsyah@sinarmasmsiglife.co.id, Dwi Budiaji - Budiaji@sinarmasmsiglife.co.id, Ety Herianti  - Ety@sinarmasmsiglife.co.id, Taufik Riyadi - taufik_r@sinarmasmsiglife.co.id");
					}
					
					//bila semua sudah cair, maka gak bisa 
					if(sisaPremiStableLink.size() == 0){
						errors.reject("", "- Polis ini sudah dicairkan sepenuhnya. Harap dikonfirmasi dengan departemen Life Benefit AJS.");
					//bila mau dicairkan semuanya (jml yg dicairkan = jml premi yg ada) harus statusnya = AKSEPTASI
					}else if(cairAll && lssa_id != 5){
						errors.reject("", "- Polis SIMAS STABIL LINK yang dapat dicairkan seluruhnya hanya polis yang sudah di-AKSEPTASI. Harap dikonfirmasi dengan departemen Underwriting AJS.");
					//selain itu, boleh cair hanya bila status aksep normal / khusus
					}else if(lssa_id != 5 && lssa_id != 10){
						errors.reject("", "- Polis SIMAS STABIL LINK yang dapat dicairkan hanya polis yang sudah di-AKSEPTASI atau AKSEPTASI KHUSUS. Harap dikonfirmasi dengan departemen Underwriting AJS.");
					}
				}
				
				//B.5. Yusuf (15 Sep 2011). Req mba Ety: Pencairan seluruhnya melalui jalur PINJAMAN (via inputan BSM) hanya bisa dilakukan bila kondisinya adalah sebagai berikut:
				//1. Nasabah mau menandatangani SPH (MST_POLICY.MSPO_FLAG_SPH = 1), atau
				//2. Nasabah tidak mau menandatangani SPH (MST_POLICY.MSPO_FLAG_SPH = 0), tetapi umur polis sudah >= 3 tahun.
				//Selain 2 kondisi diatas TIDAK BISA dilakukan pencairan melalui jalur PINJAMAN, harus via jalur SURRENDER (via LB), dan dikenai pajak
				int flagSPH = uwManager.selectFlagSPH(cpc.reg_spaj);
				String cabang = elionsManager.selectCabangFromSpaj(cpc.reg_spaj);
			    boolean existSPH=false;
				
				List<DropDown> userDir = FileUtil.listFilesInDirectory(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+cpc.reg_spaj);
				for(DropDown d : userDir) {
					if(d.getKey().contains("SPH")){
						existSPH=true;
					}
				}
				
				cpc.flag_update_sph=0;
				if(cairAll){
					if(flagSPH == 0){
						Date bdate = uwManager.selectBegDateInsured(cpc.reg_spaj);
						int tahun = FormatDate.dateDifferenceInYears(bdate, new Date());
						if(tahun < 3){
							if(!existSPH){ ////Ryan - req feri 79290 - SPH ditiadakan
								//errors.reject("", "- Polis yang dapat diinput pencairan seluruhnya hanya polis yang sudah ditandatangani SPH. Selain itu pencairan HARUS melalui kantor pusat AJS MSIG (Dept Life Benefit) dan dikenai pajak apabila pencairan dibawah 3 tahun sejak tanggal berlaku Polis.");
							}else{
								//Deddy (11 dec 2012) - Bagian ini untuk cek, apabila user pada saat input spaj tidak menandai nasabah mau menandatangani SPH, namun ada file  SPH yg diupload.System akan otomatis update flag_sph di proses pencairan.
								cpc.flag_update_sph=1;
							}
						}
					}
				}
				
				//REQ FROM YUSUF(10 Sept 2012). Apabila user tidak upload SPH, namun ditandain flag_sph saat input, maka munculkan validasi tidak bisa melakukan penginputan pencairan.
				//TAMBAHAN FROM YUSUF (13 Sept 2012). Efektif dilakukan pengecekan validasi di bawah setelah bulan sept 2012 (tanggal input new business).
				Date tigasatuagustusduaribuduabelas = defaultDateFormat.parse("31/08/2012");
				Pemegang pemegang = elionsManager.selectpp(cpc.reg_spaj);
				
				if(!existSPH){
					logger.info("ga ada cuy");
				}
				
				//Deddy (22 Jan 2013) - Permintaan Yusuf agar khusus tiga SPAJ ini tidak boleh proses pencairan di system.Akan dilakukan di pusat sesuai instruksi dari Evi (via Email).
				List spaj_khusus = new ArrayList();
				spaj_khusus.add(0, "09201205220");
				spaj_khusus.add(1, "09201209008");
				spaj_khusus.add(2, "09201209627");
				//Ryan - req feri 79290 - SPH ditiadakan
				if(!existSPH && flagSPH==1 && pemegang.getMspo_input_date().after(tigasatuagustusduaribuduabelas)){
					//errors.reject("","Input pencairan tidak dapat dilakukan karena masih ada pending upload SPH, silahkan konfirmasi dengan AJS.");
				}
				
				for(int i=0;i<spaj_khusus.size();i++){
					logger.info(spaj_khusus.get(0));
					if(cpc.reg_spaj.equals(spaj_khusus.get(i))){
						errors.reject("","Input pencairan tidak dapat dilakukan untuk No Polis/SPAJ ini, silahkan konfirmasi dengan AJS.");
					}
				}
				
			}
			
		//C. User menekan tombol delete
		}else if(request.getParameter("delete") != null) {
			
			//C.1. validasi password SPV dan KACAB diperlukan untuk input pencairan
			if(jn_bank == 2){
				String userotor = ServletRequestUtils.getStringParameter(request, "userotor", "");
				String passotor = ServletRequestUtils.getStringParameter(request, "passotor", "");
				if(userotor.trim().equals("") || passotor.trim().equals("")){
					errors.reject("", "- Silahkan lengkapi USERNAME dan PASSWORD otorisasi");
				}else{
					if(userotor.trim().toUpperCase().equals(cpc.valid1) || userotor.trim().toUpperCase().equals(cpc.valid2)){
						if(!passotor.toUpperCase().equals(uwManager.selectPasswordFromUsername(userotor))){
							errors.reject("", "- Password Otorisasi yang dimasukkan salah. Harap ulangi kembali");
						}
					}else{
						errors.reject("", "- Username yang dimasukkan tidak terdaftar sebagai SPV / Pincab Anda. Mohon dicek kembali");
					}
				}
			}
			
			//C.2. Validasi harus ada di mst_powersave_cair, alias udah pernah diproses sebelumnya
			if(!errors.hasErrors()) {
				int jmlCentang = 0;
				
				for(PowersaveCair p : cpc.daftarPremi) {
					if(p.centang) {
						jmlCentang++;
						String premi_ke = (p.mpc_tu_ke.intValue() == 0 ? "PREMI POKOK" : ("PREMI TOP-UP KE "+p.mpc_tu_ke));

						if(!elionsManager.selectValidasiHapusPowersaveCair(p.reg_spaj, p.mpc_urut, p.mpc_bdate)) {
							errors.reject("", "- " + premi_ke + ": Sudah diproses oleh dept LIFE BENEFIT. Data tidak bisa dihapus");
						}
					}
				}
				//C.2. Validasi harus pilih salah satu
				if(jmlCentang == 0) {
					errors.reject("", "- Harap pilih/centang minimal satu premi/rollover pada kolom \"INFORMASI ROLLOVER TERAKHIR\" untuk dicairkan.");
				}
			}
			
		}
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
	
		CommandPowersaveCair cpc = (CommandPowersaveCair) command;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String pesan = "";
		
		if(request.getParameter("save") != null) {
			if(cpc.daftarPremi.get(0).getMpc_rider_total()==null){
				cpc.daftarPremi.get(0).setMpc_rider_total(0.0);
			}
			String noreg = uwManager.insertMstPowersaveCair(cpc, currentUser);
			pesan = "Data Input Pencairan Untuk Polis Nomor " + cpc.powersaveCair.mspo_policy_no_format + " dengan nomor registrasi ("+noreg+") Berhasil Disimpan. Selanjutnya mohon lakukan proses TRANSFER pada menu DAFTAR INPUT PENCAIRAN.";
		}else if(request.getParameter("delete") != null) {
			//hapus row nya!
			String noreg = uwManager.deleteMstPowersaveCair(cpc, currentUser);
			//elionsManager.updateMstPowersaveCairPosisi(cpc, currentUser, 0); //0 = hapus
			pesan = "Data Input Pencairan Untuk Polis Nomor " + cpc.powersaveCair.mspo_policy_no_format + " dengan nomor registrasi ("+noreg+") Berhasil Dihapus.";
		}
		
		return new ModelAndView(new RedirectView("cair.htm")).addObject("pesan", pesan);
	}
	
}