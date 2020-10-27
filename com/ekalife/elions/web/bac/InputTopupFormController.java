package com.ekalife.elions.web.bac;

import id.co.sinarmaslife.std.model.vo.DropDown;
import org.apache.commons.lang.StringUtils;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import produk_asuransi.n_prod;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.InputTopup;
import com.ekalife.elions.model.PowersaveCair;
import com.ekalife.elions.model.Scan;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.bac.support.hit_biaya_powersave;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.LazyConverter;
import com.ekalife.utils.f_validasi;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Controller untuk Input Topup di tempat untuk Bank Sinarmas dan Sinarmas Sekuritas
 * Khusus produk stable link
 * 
 * @author Yusuf
 * @since Jan 15, 2009 (1:40:06 PM)
 */
public class InputTopupFormController extends ParentFormController {
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		Command cmd = new Command();
		String reg_spaj = ServletRequestUtils.getStringParameter(request, "reg_spaj", "");
		if(request.getParameter("pesan") != null) {
			//tarik datanya untuk refresh setelah melakukan save
			
			if(!reg_spaj.equals("")) {
				cmd.setReg_spaj(reg_spaj);
				cmd.setTrans(elionsManager.selectEntryTransStableLink(reg_spaj));
				cmd.setDaftarTopup(elionsManager.selectEntryTopupStableLink(reg_spaj, 45)); //45 dulu, gitu ditrans, baru jadi 73
			}
		}else {
			cmd.setTrans(new InputTopup());
			cmd.setReg_spaj(reg_spaj);
		}
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		cmd.setValid1(elionsManager.selectUserName(currentUser.getValid_bank_1()));
		cmd.setValid2(elionsManager.selectUserName(currentUser.getValid_bank_2()));
		
		
		return cmd;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map ref = new HashMap();
		Command cmd=(Command) command;
		
		List<DropDown> daftarRo = new ArrayList<DropDown>();
		daftarRo.add(new DropDown("1", "Rollover All"));
		daftarRo.add(new DropDown("2", "Rollover Premi"));
		daftarRo.add(new DropDown("3", "Autobreak"));
		ref.put("daftarRo", daftarRo);
		
		List<DropDown> daftarMti = new ArrayList<DropDown>();
		daftarMti.add(new DropDown("1", "1"));
		daftarMti.add(new DropDown("3", "3"));
		daftarMti.add(new DropDown("6", "6"));
		daftarMti.add(new DropDown("12", "12"));
		daftarMti.add(new DropDown("24", "24"));
		daftarMti.add(new DropDown("36", "36"));
		ref.put("daftarMti", daftarMti);
		
		List detail = uwManager.selectDetailBisnis(cmd.getReg_spaj());
		if(!detail.isEmpty()){
			Map det = (HashMap) detail.get(0);
			if(!detail.isEmpty()) {
				ref.put("nama_produk", (String) det.get("LSDBS_NAME"));
			}
		}
		
		return ref;
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Command cmd = (Command) command;
		String tmpspaj = cmd.getReg_spaj().trim().replace(".", "");
		if(tmpspaj.length() == 14) cmd.setReg_spaj(elionsManager.selectSpajFromPolis(tmpspaj));
		else if(tmpspaj.length() == 11) cmd.setReg_spaj(tmpspaj);
		String lsbs_id = uwManager.selectBusinessId(cmd.getReg_spaj());
		String lsdbs_number = uwManager.selectLsdbsNumber(cmd.getReg_spaj());
		//tampilkan data spaj
		if(request.getParameter("show") != null) {
			
			//reset values dulu
			cmd.setTrans(new InputTopup());
			cmd.setDaftarTopup(null);
			int jn_bank = currentUser.getJn_bank().intValue();
			//validasi harus masukkan nomor polis/spaj
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "reg_spaj", "", "Harap masukkan No. Polis / Register SPAJ");
			
			
			int size = cmd.getReg_spaj().trim().replace(".", "").length();
			//validasi harus masukkan nomor yang benar (cek ke db)
			if(!errors.hasErrors() && size != 11 && size != 14) {
				errors.reject("","Harap masukkan No. Polis / Register SPAJ yang benar");
			}
			
			
			if(!errors.hasErrors()) {
				String tmp = cmd.getReg_spaj().trim().replace(".", "");
				if(tmp.length() == 14) cmd.setReg_spaj(elionsManager.selectSpajFromPolis(tmp));
				else if(tmp.length() == 11) cmd.setReg_spaj(tmp);
				else errors.rejectValue("reg_spaj", "", "Harap masukkan No. Polis / Register SPAJ yang benar");
				
				cmd.setLsbsId(Integer.parseInt(lsbs_id));
				cmd.setLsdbs_number(Integer.parseInt(lsdbs_number));
				
				if(cmd.getReg_spaj() == null){
					errors.rejectValue("reg_spaj", "", "No. Polis / Register SPAJ tidak dikenali. Harap cek ulang nomor yang dimasukkan.");
				}
			}
			
			if(currentUser.getFlag_approve().intValue()!=0 && jn_bank==3){
				if(uwManager.selectCountOtorisasiSpaj(cmd.getReg_spaj())<=0){
					errors.reject("", "Maaf, Anda tidak dapat memproses polis/spaj ini karena belum diotorisasi SPV cabang.");
				}
			}
			
			if(!errors.hasErrors() && ((jn_bank!=3 && jn_bank!=2) && jn_bank!=-1)) {
				errors.reject("", "Menu ini hanya dapat digunakan oleh user Bank Sinarmas/Sekuritas.");
			}
			
			//validasi khusus bank sinarmas, harus punya akses terhadap suatu polis
			if(!errors.hasErrors() && jn_bank==2) {
				if(elionsManager.selectIsUserYangInputBank(cmd.getReg_spaj(), Integer.valueOf(currentUser.getLus_id())) <= 0 && !currentUser.getCab_bank().trim().equals("SSS")) {
					if(currentUser.getJn_bank().intValue() == 2){
						errors.reject("","Anda tidak mempunyai akses terhadap Polis ini. Polis ini hanya dapat diakses oleh cabang " + 
							elionsManager.selectCabangBiiPolis(cmd.getReg_spaj()));
					}
				}
			}
//			validasi khusus sekuritas, harus punya akses terhadap suatu polis
			if(!errors.hasErrors() && jn_bank==3) {
				if(elionsManager.selectIsUserYangInputSekuritas(cmd.getReg_spaj(), Integer.valueOf(currentUser.getLus_id())) <= 0 && !currentUser.getCab_bank().trim().equals("M35")) {
					if(currentUser.getJn_bank().intValue() == 3){
						errors.reject("","Anda tidak mempunyai akses terhadap Polis ini.");
					}
				}else if(elionsManager.selectIsUserYangInputSekuritas(cmd.getReg_spaj(), Integer.valueOf(currentUser.getLus_id())) <=0 && currentUser.getCab_bank().trim().equals("M35")) {
					if(currentUser.getJn_bank().intValue() == 3){
						if(uwManager.selectCountOtorisasiSpaj(cmd.getReg_spaj())<=0){
							errors.reject("","Polis Ini belum diotorisasi SPV yang bersangkutan.");
						}
					}
				}
			}
			
			//validasi harus produk stable link
			if(!errors.hasErrors()) {
				
				if(!products.stableLink(lsbs_id) && Integer.parseInt(lsbs_id)!=188) {
					errors.rejectValue("reg_spaj", "", "Polis ini bukan Polis STABLE LINK/POWERSAVE BARU. Harap pastikan nomor yang Anda masukkan benar.");
				}
			}

			//validasi lspd_id
			//Yusuf (22/06/2010)
			if(!errors.hasErrors()) {
				PowersaveCair pc = elionsManager.selectRolloverData(cmd.getReg_spaj());
				if(pc.lspd_id.intValue() == 95) {
					errors.rejectValue("reg_spaj", "", "Polis ini sudah dibatalkan. Anda tidak bisa melanjutkan. Harap konfirmasi dengan dept UNDERWRITING melalui perwakilan BANCASSURANCE kami.");
				}
			}
			
			/* 
			//validasi lssp_id
			 * Yusuf (13 Aug 09) - Disabled, request dari BSM / Iwen / Pak CK, ini agar bisa input topup barengan dengan input new business
			if(!errors.hasErrors()) {
				PowersaveCair pc = elionsManager.selectRolloverData(cmd.getReg_spaj());
				if(pc.lssp_id.intValue() != 1 && pc.lssp_id.intValue() != 6) {
					errors.rejectValue("reg_spaj", "", "Status Polis saat ini bukan INFORCE/DEATH CLAIM. Anda tidak bisa melanjutkan. Harap konfirmasi dengan dept LIFE BENEFIT melalui perwakilan BANCASSURANCE kami.");
				}
			}
			//validasi sudah masuk produksi pertama/belum
			if(!errors.hasErrors()) {
				List produksi = uwManager.selectProduksiKe(cmd.getReg_spaj(), 1);
				if(produksi.isEmpty()) {
					errors.rejectValue("reg_spaj", "", "Polis ini belum diAKSEPTASI. Harap konfirmasi dengan dept UNDERWRITING melalui perwakilan BANCASSURANCE kami.");
				}
			}
			*/
			
			//validasi tidak boleh ada di pinjaman (alias udah cair)
			if(!errors.hasErrors()) {
				int hitung = elionsManager.selectValidasiPinjaman(cmd.getReg_spaj());
				if(hitung > 0) {
					//errors.rejectValue("reg_spaj", "", "Polis yang Anda masukkan sudah melakukan pencairan. Harap dikonfirmasi dengan dept LIFE BENEFIT melalui perwakilan BANCASSURANCE kami.");
					if(jn_bank==2){
						errors.rejectValue("reg_spaj", "", "Polis yang Anda masukkan sudah melakukan pencairan. Harap dikonfirmasi dengan dept LIFE BENEFIT melalui perwakilan BANCASSURANCE kami.");
					}else{
						errors.rejectValue("reg_spaj", "", "Polis yang Anda masukkan sudah melakukan pencairan. Harap dikonfirmasi dengan dept LIFE BENEFIT melalui SPV yang bersangkutan.");
					}
					
				}
			}
			
			//** bila tembus semua validasi, baru tarik datanya **//
			if(!errors.hasErrors()) {
				cmd.setTrans(elionsManager.selectEntryTransStableLink(cmd.getReg_spaj()));
				cmd.setDaftarTopup(elionsManager.selectEntryTopupStableLink(cmd.getReg_spaj(), 45)); //45 dulu, gitu ditrans, baru jadi 73
				errors.reject("", "Silahkan lanjutkan penginputan.");
			}
			
		//new topup
		}else if(request.getParameter("new") != null) {
			if(cmd.getTrans() != null) {
				
				InputTopup baru = elionsManager.selectInputTopupBaruStableLink(cmd.getReg_spaj());
				
				if(cmd.getLsbsId()==188){
					baru = uwManager.selectInputTopupBaruPowerSave(cmd.getReg_spaj());
					baru.msl_premi = (double) 10000000;
				}else{
					if(baru.lku_id.equals("01")) {
						baru.msl_premi = (double) 20000000;
					}else{
						baru.msl_premi = (double) 2000;					
					}
				}
				
				cmd.getDaftarTopup().add(baru);
			}
			errors.reject("", "Silahkan lanjutkan penginputan.");
		
		//simpan data
		}else if( (cmd.getTrans().getSimpan_lji_id() != null && cmd.getTrans().getSimpan_mode() != null && cmd.getTrans().getSimpan_msl_no() != null) || (Integer.parseInt(lsbs_id)==188 && cmd.getTrans().getSimpan_mode() != null && cmd.getTrans().getSimpan_msl_no() != null) ) {
			InputTopup trans = cmd.getTrans();
			String proses = ServletRequestUtils.getStringParameter(request, "proses", "");
			if (proses==null)proses="0";			
			for(int i=0; i<cmd.getDaftarTopup().size(); i++) {
				errors.setNestedPath("daftarTopup["+i+"]");
				InputTopup tmp = (InputTopup) cmd.getDaftarTopup().get(i);
				List cekDouble= elionsManager.selectTransDobleTU(trans.getReg_spaj(),tmp.msl_bdate,tmp.msl_premi);
				Integer posisi= tmp.getMsl_posisi();
				if(posisi==null)posisi=0;
				if (posisi.equals(45)){
					cmd.setTudoub(0);
					tmp.setPosisi(0);
					cmd.getDaftarTopup().get(i).setPosisi(0);
					cmd.getDaftarTopup().get(i).setNo_trx10(tmp.getNo_trx10());
					cmd.setPosisi(0);
				}else if(posisi.equals(0)){
					cmd.setTudoub(1);
					cmd.setPosisi(1);
					cmd.getDaftarTopup().get(i).setPosisi(1);
					cmd.getDaftarTopup().get(i).setNo_trx10(tmp.getNo_trx10());
					tmp.setPosisi(1);
				}
				
				if(currentUser.getJn_bank().intValue()==3){
					tmp.setFlag_new(2);
				}
				if((!proses.equals("1"))&& tmp.getFlag_new().equals(1)&&(cekDouble.size()>0)){
					
//					cmd.set
					if((cekDouble.size()>0)&& (posisi.equals(0))){
						String warning =(String)cekDouble.get(0);
						cmd.setWarning(warning);
						tmp.setFlag_new(2);
//						request.setAttribute("confirmMessage1",warning + " JIKA TETAP INGIN MENYIMPAN/TRANSFER SILAHKAN CLICK KEMBALI TOMBOL SIMPAN/TRANSFER");
						errors.reject("", cekDouble.toArray(), warning + " JIKA TETAP INGIN MENYIMPAN/TRANSFER SILAHKAN CLICK KEMBALI TOMBOL SIMPAN/TRANSFER");
					}
				}else if((proses=="1")||proses.equals("1")||(trans.simpan_mode.equals("insert") || trans.simpan_mode.equals("update"))){
				//cari dulu yg mana yg mau di insert/update/delete, itu yg divalidasi
				if(cmd.getLsbsId()==188){// diset null untuk lji ID krn powersave tidak ada.
					trans.simpan_lji_id="";
					tmp.lji_id="";
				}
				if(trans.reg_spaj.equals(tmp.reg_spaj) && trans.simpan_lji_id.equals(tmp.lji_id) && trans.simpan_msl_no.equals(tmp.msl_no)) {
					
					//validasi untuk semua jenis edit
					if(tmp.getMsl_posisi() != null){
						if(tmp.getMsl_posisi().intValue() != 45){
							if(currentUser.getJn_bank()==2){
								errors.rejectValue("msl_posisi","", "Transaksi Topup Ke-" + tmp.getMsl_tu_ke() + " tidak dapat dirubah/dibatalkan karena sudah diproses. Harap konfirmasi dengan perwakilan BANCASSURANCE kami.");
							}else if(currentUser.getJn_bank()==3){
							errors.rejectValue("msl_posisi","", "Transaksi Topup Ke-" + tmp.getMsl_tu_ke() + " tidak dapat dirubah/dibatalkan karena sudah diproses. Harap konfirmasi dengan SPV yang bersangkutan.");
							}
						}
					}
					
					//validasi untuk insert dan update
					if(trans.simpan_mode.equals("insert") || trans.simpan_mode.equals("update")) {
//						errors.reject("", "hehehe");
						//validasi field-field yg harus diisi
						if(currentUser.getJn_bank().intValue()!=3){
							ValidationUtils.rejectIfEmptyOrWhitespace(errors, "no_trx10", 	"", "Mohon Isi No Transaksi");
						}
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "msl_premi", 	"", "Mohon Isi JUMLAH TOP UP");
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "msl_mgi", 	"", "Mohon Pilih MTI");
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "msl_bdate", 	"", "Mohon Isi Tanggal PERIODE MTI");
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "msl_ro", 	"", "Mohon Pilih JENIS ROLLOVER");
						
						//validasi password SPV dan KACAB diperlukan untuk input pencairan
//						if(tmp.userotor.trim().equals("") || tmp.passotor.trim().equals("")){
//							errors.reject("", "Silahkan lengkapi USERNAME dan PASSWORD otorisasi");
//						}else{
//							if(tmp.userotor.trim().toUpperCase().equals(cmd.getValid1()) || tmp.userotor.trim().toUpperCase().equals(cmd.getValid2())){
//								if(!tmp.passotor.toUpperCase().equals(uwManager.selectPasswordFromUsername(tmp.userotor))){
//									errors.reject("", "Password Otorisasi yang dimasukkan salah. Harap ulangi kembali");
//								}
//							}else{
//								errors.reject("", "Username yang dimasukkan tidak terdaftar sebagai SPV / Pincab Anda. Mohon dicek kembali");
//							}
//						}
						

						
						//Yusuf (10 Sept 2009) : Semua Stable Link < 10 September, tidak boleh input topup, harus issue polis baru
						if(!errors.hasErrors()|| cekDouble.size()>0) {
							Date sepuluhSeptemberDuaRibuSembilan = defaultDateFormat.parse("10/09/2009");
							
							//bila begdate topupnya < 10 sept, masih boleh, tapi bila > 10 sudah tidak boleh
							//Yusuf (24/02/2010) - Req Rudy, aturan baru : bila begdate slink yg msl_no = 1 < 10 sep, tdk boleh TU 
							//int flag_special = uwManager.getFlagSpecial(cmd.getReg_spaj());
							//if(flag_special == 1 && tmp.msl_bdate.after(sepuluhSeptemberDuaRibuSembilan) && cmd.getTrans().mste_beg_date.before(sepuluhSeptemberDuaRibuSembilan)){
							if(cmd.getLsbsId()!=188){
								Date bdateslink = uwManager.selectBegDateSlinkPertama(cmd.getReg_spaj());
								if(bdateslink.before(sepuluhSeptemberDuaRibuSembilan)){
									errors.rejectValue("reg_spaj", "", "Untuk Polis dengan Tanggal Mulai Pertanggungan di bawah 10 September 2009, sudah tidak dapat melakukan penambahan Top-Up melainkan harus input Polis baru.");
								}
							}
						}
						
						//(Deddy) 8 sep 2011 - Powersave New 188 tidak ada pilihan manfaat bulanan.
						if(cmd.getLsbsId()==188 && tmp.flag_bulanan.intValue() == 1){
							errors.rejectValue("flag_bulanan", "","Untuk Jenis Produk POWERSAVE, tidak ada pilihan manfaat bulanan.");
						}
												
						//Khusus Manfaat Bulanan, tidak boleh pilih Rollover All, dan tidak boleh pilih MGI 1 bulan
						if(!errors.hasErrors()|| cekDouble.size()>0){
							if(tmp.flag_bulanan.intValue() == 1){
								if(tmp.msl_ro.intValue() == 1){
									errors.rejectValue("msl_ro", "","Untuk Jenis Produk MANFAAT BULANAN tidak boleh memilih Jenis Rollover ALL.");
								}else if(tmp.msl_mgi.intValue() == 1){
									errors.rejectValue("msl_mgi", "","Untuk Jenis Produk MANFAAT BULANAN tidak boleh memilih MTI 1 bulan.");
								}
							}
						}
						
						//validasi no transaksi
						if(!errors.hasErrors()|| cekDouble.size()>0) {
							//cek format
							/**
							 * harus ada FT
							 * Counter harus 10 digit
							 * diabaikan
							 * 3 digit kode cab wajib
							 * ex: FT0832212439\BKL
							 * UPDATE TGL 4 Agustus 2009 Request by EDI KOHAR ==> validasi hanya cek sudah pernah digunakan atau belum
							 */
							if(currentUser.getJn_bank().intValue()!=3){
								if(tmp.getNo_trx10().length()!=10){
									errors.rejectValue("no_trx10", "","No. transaksi harus sepuluh digit");
								}else if(!f_validasi.f_validasi_numerik(tmp.getNo_trx10().substring(0,5))){
									errors.rejectValue("no_trx10", "","5 Digit Pertama No. transaksi harus berupa Angka");
								}
							
								
//								if(!errors.hasErrors()){
//									//cek apakah FT sudha pernah ada atau belum
//								}
								
//								String kode=tmp.getNo_trx().replace("\\", "").toUpperCase();
								
//								String kodeCab=kode.substring(kode.length()-3, kode.length());
//								String kodeTrans=kode.substring(0, kode.length()-3);
								
//								if(!kode.substring(0, 2).equals("FT")){
////									errors.rejectValue("no_trx", "","No. Transaksi tidak valid");
//								}
								
//								if(kodeTrans.length()!=12){
////									errors.rejectValue("no_trx", "","No. Transaksi tidak valid");
//								}
								
	//							validasi cabang
//								HashMap cekCabang=uwManager.selectCabBsm(kodeCab);
//								if(cekCabang==null){
////									errors.rejectValue("no_trx", "","Kode Cabang pada No. Transaksi tidak tercatat di sistem kami");
//								}
								
//								kode=kodeTrans+"\\"+kodeCab;
								//cek apakah no transaksi sudah ada di SLink
								Integer countTrans=uwManager.selectNoTranSLink(tmp.getNo_trx10(),tmp.getMsl_tu_ke());
								if(cmd.getLsbsId()==188){
									countTrans=uwManager.selectNoTranPsave(tmp.getNo_trx10(),tmp.getMsl_tu_ke());
								}
								if(countTrans!=0){
									errors.rejectValue("no_trx10", "","No. Transaksi sudah pernah digunakan");
								}
								if(!errors.hasErrors()) {
//									tmp.setNo_trx(kode);
								}
							}
//							}else{
//								errors.rejectValue("no_trx", "","No. Transaksi tidak valid");
//							}
								
						}
						
						//validasi minimum top up
						if(!errors.hasErrors()|| cekDouble.size()>0) {
							/**
							 * buka validasi 20 juta jadi 10 juta jika premi awalnya lebih dari 100juta / USD10000
							 * request by Email Yusuf
							 * create by Bertho Rafitya
							 * @since 15 Oct 2009 08:30:59
							 */ 
							Double sumPremiLama=uwManager.selectSumPremiSlinkLama(cmd.getReg_spaj());//sudah diunion antara mst_slink dengan mst_psave
							if(tmp.lku_id.equals("01")) {
								if(cmd.getLsbsId()==188){
									if(tmp.msl_premi.doubleValue() < 10000000) errors.rejectValue("msl_premi", "", "Minimum Top Up adalah Rp. 10.000.000,-");
								}else{
									if(sumPremiLama>=100000000){
										if(tmp.msl_premi.doubleValue() < 10000000) errors.rejectValue("msl_premi", "", "Minimum Top Up adalah Rp. 10.000.000,-");
									}else{
										if(tmp.msl_premi.doubleValue() < 20000000) errors.rejectValue("msl_premi", "", "Minimum Top Up adalah Rp. 20.000.000,-");
									}
								}
							}else {
								if(cmd.getLsbsId()==188){
									if(tmp.msl_premi.doubleValue() < 1000) errors.rejectValue("msl_premi", "", "Minimum Top Up adalah US$ 1.000,-");
								}else{
									if(sumPremiLama>=10000){
										if(tmp.msl_premi.doubleValue() < 1000) errors.rejectValue("msl_premi", "", "Minimum Top Up adalah US$ 1.000,-");
									}else{
										if(tmp.msl_premi.doubleValue() < 2000) errors.rejectValue("msl_premi", "", "Minimum Top Up adalah US$ 2.000,-");
									}
								}
							}
						}
						
						//validasi rate ada / tidak, sekaligus melakukan perhitungan investasi
						if(!errors.hasErrors()|| cekDouble.size()>0) {
							Class aClass = Class.forName("produk_asuransi.n_prod_" + FormatString.rpad("0", trans.lsbs_id.toString(), 2));
							n_prod produk = (n_prod) aClass.newInstance();
							produk.ii_bisnis_no = trans.lsdbs_number;
							produk.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
							produk.flag_powersave = produk.of_get_bisnis_no(tmp.flag_bulanan);
							
							hit_biaya_powersave h = new hit_biaya_powersave();
							Map data =  elionsManager.selectbungaprosave(
									tmp.lku_id,
									String.valueOf(h.hit_jangka_invest(String.valueOf(tmp.msl_mgi))),
									tmp.msl_premi,
									tmp.msl_bdate,
									produk.flag_powersave, 0);
							if(data != null) {
								tmp.msl_edate		= FormatDate.add(FormatDate.add(tmp.msl_bdate, Calendar.MONTH, tmp.msl_mgi), Calendar.DATE, -1); //begdate + bulan mgi - 1 hari
								tmp.msl_next_date	= FormatDate.add(FormatDate.add(tmp.msl_bdate, Calendar.MONTH, 3), Calendar.DATE, -1); //begdate + 3 bulan - 1 hari -> belum kepake (rudy)
								if(cmd.getLsbsId()==188){
									tmp.msl_next_date	= FormatDate.add(tmp.msl_edate, Calendar.DATE, 1);//untuk set nilai mps_ro_date di mst_psave
								}
								tmp.msl_hari		= (int) FormatDate.dateDifference(tmp.msl_bdate, tmp.msl_edate, true) + 1; //(enddate - begdate) + 1
								tmp.msl_rate 		= Double.parseDouble(data.get("LPR_RATE").toString()); //rate
//								if(tmp.lku_id.equals("01")){
//									tmp.msl_bunga 		= FormatNumber.round(tmp.msl_premi * (tmp.msl_rate / 100) * (tmp.msl_hari.doubleValue() / 365), 0);
//								}else if(tmp.lku_id.equals("02")){
									tmp.msl_bunga 		= FormatNumber.round(tmp.msl_premi * (tmp.msl_rate / 100) * (tmp.msl_hari.doubleValue() / 365), 2);
//								}
								
								tmp.msl_tax			= (double) 0;
							}else {
								errors.rejectValue("msl_bdate", "", "Rate untuk produk ini belum ada. Harap dikonfirmasi dengan dept INVESTMENT melalui perwakilan BANCASSURANCE kami.");
							}
						}
						
						//validasi untuk NAB hari T-3, sekaligus melakukan perhitungan nab dan unitnya
						if(cmd.getLsbsId()!=188 && (cmd.getLsbsId()==164 && cmd.getLsdbs_number()!=11)){
							if(!errors.hasErrors()|| cekDouble.size()>0) {
								DateFormat df = new SimpleDateFormat("dd MMM yyyy");
								Map data_nab = (HashMap) elionsManager.select_tminus(tmp.lji_id, tmp.msl_bdate, 3);
								if (data_nab != null){
									tmp.msl_tgl_nab 	= (Date)data_nab.get("LNU_TGL");
									tmp.msl_nab 		= (Double)data_nab.get("LNU_NILAI");
									tmp.msl_unit 		= FormatNumber.round(tmp.msl_premi / tmp.msl_nab, 4);
									tmp.msl_saldo_unit 	= tmp.msl_unit; //samain aja kayak unit -> belum kepake (rudy)
								}else {
									errors.rejectValue("msl_bdate", "", "Nilai NAB (" + df.format(FormatDate.add(tmp.msl_bdate, Calendar.DATE, -3)) + ") tidak ada. Harap konfirmasi melalui perwakilan BANCASSURANCE kami");
								}
							}
						}
						
						//ryan -> Validasi Upload Form Top Up 
						if(!errors.hasErrors()) {
							// valiadasi cek upload form Top Up
							if (lsbs_id.equals("164") && lsdbs_number.equals("11")){
									Upload upload = new Upload();
									ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
									binder.bind(request);
									String angkaString=null;
									String lca_id = elionsManager.selectCabangFromSpaj(cmd.getReg_spaj());
									String dir = props.getProperty("pdf.dir.export") + "\\" + lca_id;
									Integer counter=uwManager.selectMaxSlink(cmd.getReg_spaj());
									String dest=dir+"\\"+cmd.getReg_spaj()+"\\";
									List<DropDown> dokumen = FileUtils.listFilesInDirectoryStartsWith(dest, cmd.getReg_spaj()+"FTUP"+tmp.getMsl_tu_ke());
									//List<DropDown> dokumen = FileUtils.listFilesInDirectory(dest);
								if(!upload.getFile1().isEmpty()){
									Integer size = dokumen.size();
									Integer index  = size+1;
									String nilai = StringUtils.leftPad(index.toString(), 3, "0");
										String destOut =dest+cmd.getReg_spaj()+"FTUP"+tmp.getMsl_tu_ke()+" "+nilai+".pdf";
										File outputFile = new File(destOut);
										FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
										}
								else{
								/*	File userDir = new File(dest);
									 if(!userDir.exists()) {*/
										 errors.reject("", "Khusus New Simas Stabil Link : Silakan mengupload Form Top Up Terlebih Dahulu.");
									     //  }
										
										}
							}
						}
						
					//validasi untuk delete
					}else if(trans.simpan_mode.equals("delete")) {
						//yg boleh dihapus, yg posisi 45 saja
						if(tmp.getMsl_posisi().intValue() != 45){
							errors.reject("", "Anda tidak dapat melakukan penghapusan terhadap Top-Up ini. Harap konfirmasi dengan AJS.");
						}
						
//					//validasi untuk transfer
//					}else if(trans.simpan_mode.equals("transfer")) {
//						// validasi jika user adalah bank dan blm di otorisasi
//						if( currentUser.getJn_bank() != null && currentUser.getJn_bank() == 2 )
//						{
//							if( !"11".equals( currentUser.getLde_id() ) )
//							{
//								List selectMslTuKeMstPositionSpajList = elionsManager.selectMslTuKeMstPositionSpajList(trans.getReg_spaj());
//								String success = null;
//								for( int j = 0 ; j < selectMslTuKeMstPositionSpajList.size() ; j ++ )
//								{
//									Map temp = (Map) selectMslTuKeMstPositionSpajList.get(j);
//									Object mslTuKe = temp.get("MSL_TU_KE");
//									
//									if( mslTuKe != null && !"".equals( mslTuKe ) )
//									{
//										if( LazyConverter.toInt(mslTuKe.toString()) == trans.getSimpan_msl_tu_ke() )
//										{
//											success = "success";
//										}
//										
//									}
//								}
//								if( !"success".equals( success ) ){
//									errors.reject("", "Top Up ke-"+ trans.getSimpan_msl_tu_ke() +" belum diotorisasi oleh spv/Pincab anda. mohon untuk diotorisasi terlebih dahulu");
//								}
//							}
//						}
						
						//validasi, kalo mau transfer harus ada FILE nya
//						String cabang 		= elionsManager.selectCabangFromSpaj(tmp.reg_spaj);
//						String directory 	= props.getProperty("pdf.dir.rollover") + "\\StableLink\\" + cabang + "\\" + tmp.reg_spaj;
//						DateFormat df 		= new SimpleDateFormat("yyyyMMdd");
//						File f 				= new File(directory + "\\" + "TOP_UP_" + tmp.msl_tu_ke + "_" + elionsManager.selectPolicyNumberFromSpaj(tmp.reg_spaj) + "_" + df.format(tmp.msl_bdate) + ".PDF");
//						if(!f.exists()) errors.reject("", "Harap lakukan pencetakan Top-Up terlebih dahulu.");
						
					}
					
				}
			}
//				validasi untuk transfer
				if(trans.simpan_mode.equals("transfer")) {
					// validasi jika user adalah bank dan blm di otorisasi
						if( currentUser.getJn_bank() != null && (currentUser.getJn_bank() == 2 || currentUser.getJn_bank() == 3) )
						{
							if( !"11".equals( currentUser.getLde_id() ) )
							{
								List selectMslTuKeMstPositionSpajList = elionsManager.selectMslTuKeMstPositionSpajList(trans.getReg_spaj());
								String success = null;
								for( int j = 0 ; j < selectMslTuKeMstPositionSpajList.size() ; j ++ )
								{
									Map temp = (Map) selectMslTuKeMstPositionSpajList.get(j);
									Object mslTuKe = temp.get("MSL_TU_KE");
									
									if( mslTuKe != null && !"".equals( mslTuKe ) )
									{
										if( LazyConverter.toInt(mslTuKe.toString()) == trans.getSimpan_msl_tu_ke() )
										{
											success = "success";
										}
										
									}
								}
								if(currentUser.getJn_bank().intValue() == 3 && !currentUser.getCab_bank().trim().equals("M35")) {
									if(currentUser.getFlag_approve().intValue()!=1){
										errors.reject("","Anda tidak mempunyai akses Transfer terhadap Polis ini.");
									}
								}
								
								if( !"success".equals( success ) ){
									errors.reject("", "Top Up ke-"+ trans.getSimpan_msl_tu_ke() +" belum diotorisasi oleh spv/Pincab anda. mohon untuk diotorisasi terlebih dahulu");
								}
							}
						}
					}
			errors.setNestedPath("");

			
		}}
		
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Command cmd = (Command) command;
		
		
		//Yusuf (16/04/2010) Rudy - walaupun sudah tidak ada special, tapi flag_special harus tetap diisi 0
		for(InputTopup tmp : cmd.getDaftarTopup()) {
			tmp.flag_special = 0;
			if(currentUser.getJn_bank().intValue()==3){
				tmp.setNo_trx(null);
			}
		}
		
		//save
		String pesan = elionsManager.saveTopupStableLink(cmd.getTrans(), cmd.getDaftarTopup(), currentUser);
		return new ModelAndView(new RedirectView("topup.htm")).addObject("pesan", pesan).addObject("reg_spaj", cmd.getReg_spaj());
	}
	

}