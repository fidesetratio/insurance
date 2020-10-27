package com.ekalife.elions.web.bac;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.ContactPerson;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DetilTopUp;
import com.ekalife.elions.model.Employee;
import com.ekalife.elions.model.History;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Personal;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.bac.support.Editbacvalidator;
import com.ekalife.elions.web.bac.support.Editduvalidator;
import com.ekalife.elions.web.bac.support.hit_biaya_powersave;
import com.ekalife.utils.Common;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_replace;
import com.ekalife.utils.f_validasi;
import com.ekalife.utils.parent.ParentWizardController;

import id.co.sinarmaslife.std.spring.util.Email;
import produk_asuransi.n_prod;

/**
 * @author HEMILDA
 * Controller untuk input bac
 */
public class EditBacControllerDMTM extends ParentWizardController {
	
	private long accessTime;
	protected final Log logger = LogFactory.getLog(getClass());
	private Email email;
	
	public void setEmail(Email email) {
		this.email = email;
	}
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors, int page) throws Exception {
		Cmdeditbac cmd = (Cmdeditbac) command;
	}
	
	protected void validatePage(Object cmd, Errors err, int page) {
		try {
			logger.debug("EditBacController : validate page " + page);
			//logger.info("VALIDATING PAGE: " + page);
//			Editbacvalidator validator = (Editbacvalidator) this.getValidator();
			Validator[] validators = getValidators();
			Cmdeditbac halaman= (Cmdeditbac)cmd;
			Integer idx =halaman.getPemegang().getIndeks_halaman();
			if (idx == null){
				idx = 1;
				halaman.getPemegang().setIndeks_halaman(idx);
			}
			int index = idx.intValue();
			String reg_spaj = halaman.getPemegang().getReg_spaj();
			if (reg_spaj ==null) {
				// buat kalau back tidak perlu validasi kalau next baru validasi
				Validator validator = validators[0];
				Validator validatordu = validators[1];
				if(page==0) 
					((Editbacvalidator) validator).validatepp(cmd, err);
//					validator.validatepp(cmd, err);
				else if(page==1 && index > page) 
					((Editbacvalidator) validator).validatettg(cmd, err);
//					validator.validatettg(cmd, err);
				else if(page==2 && index > page) 
					((Editduvalidator) validatordu).validateddu(cmd, err);
//					validator.validateddu(cmd, err);
				else if(page==3 && index > page) 
					((Editbacvalidator) validator).validateinvestasi(cmd, err);
//					validator.validateinvestasi(cmd, err);
				else if(page==4 && index > page){
					((Editbacvalidator) validator).validateagen(cmd, err);
					((Editbacvalidator) validator).validateinvestasi(cmd, err);
//					validator.validateagen(cmd, err);
//					validator.validateinvestasi(cmd, err);
				}else if(page==5 && index > page){
					Cmdeditbac detiledit = (Cmdeditbac)cmd;
					//APABILA INPUTAN BANK, MAKA ADA VALIDASI OTORISASI SUPERVISOR DAN KACAB
					if (!detiledit.getPemegang().getCab_bank().equalsIgnoreCase("")){
						((Editbacvalidator) validator).validateOtorisasi(detiledit, err, halaman.getCurrentUser().getJn_bank());
//						validator.validateOtorisasi(detiledit, err, halaman.getCurrentUser().getJn_bank());
					}
					//APABILA POLIS POWERSAVE DAN DIATAS 69 TAHUN, HARUS ADA INFORMASI SPECIAL CASE
					if(products.powerSave(detiledit.getDatausulan().getLsbs_id().toString())){
						if(detiledit.getPemegang().getMste_age().intValue() >= 69) {
							ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.info_special_case", "", "Harap isi keterangan special case");
						}
					}
				}
			}else{
				Validator validator = validators[0];
				Validator validatordu = validators[1];
				if(page==0) 
					((Editbacvalidator) validator).validatepp(cmd, err);
//					validator.validatepp(cmd, err);
				else if(page==1) 
					((Editbacvalidator) validator).validatettg(cmd, err);
//					validator.validatettg(cmd, err);
				else if(page==2) 
					((Editduvalidator) validatordu).validateddu(cmd, err);
//					validator.validateddu(cmd, err);
				else if(page==3) 
					((Editbacvalidator) validator).validateinvestasi(cmd, err);
//					validator.validateinvestasi(cmd, err);
				else if(page==4){
					Cmdeditbac detiledit = (Cmdeditbac)cmd;
					((Editbacvalidator) validator).validateagen(cmd, err);
					((Editbacvalidator) validator).validateinvestasi(cmd, err);
//					validator.validateagen(cmd, err);	
//					validator.validateinvestasi(cmd, err);
					
					if(!detiledit.getCurrentUser().getCab_bank().equals("") && detiledit.getCurrentUser().getJn_bank().intValue() == 2) {
						if (detiledit.getDatausulan().getKodeproduk().equals("164")||detiledit.getDatausulan().getKodeproduk().equals("142")||detiledit.getDatausulan().getKodeproduk().equals("186")){
							if(detiledit.getAgen().getMsag_id()!=("021052") 
									&& ((detiledit.getDatausulan().getKodeproduk().equals("164")
									&& detiledit.getDatausulan().getLsdbs_number().equals("2"))
									|| (detiledit.getDatausulan().getKodeproduk().equals("186")
											&& detiledit.getDatausulan().getLsdbs_number().equals("3"))) 
									 ){
								err.reject("", "KODE PENUTUP harus diisi dengan KODE AGEN 021052");
							}
							if(detiledit.getAgen().getMsag_id()!=("016409") 
									&& detiledit.getDatausulan().getKodeproduk().equals("142")
									&& detiledit.getDatausulan().getLsdbs_number().equals("2")){
								err.reject("", "KODE PENUTUP harus diisi dengan KODE AGEN 016409");
							}
						}	
					}
				}else if (page == 5){
					Cmdeditbac detiledit = (Cmdeditbac)cmd;
					//APABILA INPUTAN BANK, MAKA ADA VALIDASI OTORISASI SUPERVISOR DAN KACAB
					if (!detiledit.getPemegang().getCab_bank().equalsIgnoreCase("")){
						((Editbacvalidator) validator).validateOtorisasi(detiledit, err, halaman.getCurrentUser().getJn_bank());
//						validator.validateOtorisasi(detiledit, err, halaman.getCurrentUser().getJn_bank());
					}

					//APABILA POLIS POWERSAVE DAN DIATAS 69 TAHUN, HARUS ADA INFORMASI SPECIAL CASE
					if(products.powerSave(detiledit.getDatausulan().getLsbs_id().toString())){
						if(detiledit.getPemegang().getMste_age().intValue() >= 69) {
							ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.info_special_case", "", "Harap isi keterangan special case");
						}
					}
				}
			}
		} catch (ServletException e) {
			logger.error("ERROR :", e);	
		} catch (IOException e) {
			logger.error("ERROR :", e);
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
		
		logger.info("page = " + page);
		
	}

	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err, int page)
			throws Exception {
		
		logger.debug("EditBacController : referenceData page " + page);
		Map<String, Object> refData = null;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date tanggal = elionsManager.selectSysdateSimple();
		
		String sysdate = defaultDateFormat.format(new Date());
		
		DateFormat dfh = new SimpleDateFormat("HH");
		DateFormat dfm = new SimpleDateFormat("mm");
		
		String hh = ServletRequestUtils.getStringParameter(request, "hh", dfh.format(tanggal));
		String mm = ServletRequestUtils.getStringParameter(request, "mm", dfm.format(tanggal));
		String tanggalTerimaAdmin;
		
		
		/** Halaman 1 : Pemegang Polis */
		if (page ==0){
			refData = new HashMap<String, Object>();
			Cmdeditbac editBac = (Cmdeditbac) cmd;
			
			editBac.getDatausulan().setJenis_pemegang_polis(0);
			editBac.setPersonal(new Personal());
			editBac.setContactPerson(new ContactPerson());
			editBac.getPersonal().setMcl_first("");
			editBac.getContactPerson().setNama_lengkap("");
						
			refData.put("tanggalTerimaAdmin",  sysdate);
			refData.put("listHH", new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23});
			refData.put("listMM", new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59});
			refData.put("hh", hh);
			refData.put("mm", mm);
//			elionsManager.prosesEditTanggalSpajAdmin(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan);
//			refData.put("success", "Berhasil Update Tanggal Terima Admin");
			
			if (editBac.getPemegang().getKota_rumah()==null ){
				editBac.getPemegang().setKota_rumah("");
			}
			
			if (editBac.getPemegang().getKota_kantor()==null ){
				editBac.getPemegang().setKota_kantor("");
			}
			
			if (editBac.getAddressbilling().getKota_tgh()==null ){
				editBac.getAddressbilling().setKota_tgh("");				
			}
			
			if(editBac.getDatausulan().isPsave){
				String lsbs_id_kopi = uwManager.selectLsbsId(editBac.getDatausulan().getKopiSPAJ());
				if(!products.powerSave(lsbs_id_kopi) ){
					//err.reject("", "Anda tidak dapat melakukan pengkonversian. Silakan pilih CANCEL apabila mengkonversi stable link lama menjadi stable link baru.Terima kasih.");
				}
			}
			
			if(editBac.getDatausulan().getKurs_premi()==null){
				editBac.getDatausulan().setKurs_premi("");
			}
				
				
			if(editBac.getDatausulan().isPsave){
				String lsbs_id_kopi = uwManager.selectLsbsId(editBac.getDatausulan().getKopiSPAJ());
				if(uwManager.selectCountMstSurrender(editBac.getDatausulan().getKopiSPAJ())==0){
					if(products.powerSave(lsbs_id_kopi) ){
						err.reject("", "Polis ini belum dilakukan proses Surrender, sehingga tidak dapat dilakukan pengkonversian, Harap hubungi Departemen Life Benefit.");
					}
				}
			}
			
			//dari tabel
			refData.put("select_identitas", elionsManager.selectDropDown("eka.lst_identity", "lside_id", "lside_name", "", "lside_id", null));
			//refData.put("select_identitas",DroplistManager.getInstance().get("IDENTITY.xml","ID",request));
			refData.put("select_negara", elionsManager.selectDropDown("eka.lst_negara", "lsne_id", "lsne_note", "", "lsne_urut", null));
			//refData.put("select_negara",DroplistManager.getInstance().get("WARGANEGARA.xml","ID",request));
			refData.put("select_agama", elionsManager.selectDropDown("eka.lst_agama", "lsag_id", "lsag_name", "", "lsag_name", null));
			//refData.put("select_agama",DroplistManager.getInstance().get("AGAMA.xml","ID",request));
			refData.put("select_pendidikan", elionsManager.selectDropDown("eka.lst_education", "lsed_id", "lsed_name", "", "lsed_name", null));
			//refData.put("select_pendidikan",DroplistManager.getInstance().get("PENDIDIKAN.xml","ID",request));

			// dari xml, semua pindah ke tabel kalo bisa
			refData.put("select_gelar",DroplistManager.getInstance().get("GELAR.xml","ID",request));
			refData.put("select_industri",DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","",request));
			refData.put("select_pekerjaan",DroplistManager.getInstance().get("KLASIFIKASI_PEKERJAAN.xml","",request));
			refData.put("select_medis",DroplistManager.getInstance().get("medis.xml","ID",request));
			refData.put("select_marital",DroplistManager.getInstance().get("MARITAL.xml","ID",request));
			refData.put("select_tujuan",DroplistManager.getInstance().get("TUJUAN_ASR.xml","",request));
			refData.put("select_penghasilan",DroplistManager.getInstance().get("PENGHASILAN.xml","",request));
			refData.put("select_dana",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","",request));

			//apabila inputan bank
			if(!currentUser.getCab_bank().equals("") && !currentUser.getName().contains("SIMASPRIMA")) {
				refData.put("select_relasi",DroplistManager.getInstance().get("RELATION_BANCASS.xml","",request));
			}else {
				refData.put("select_relasi",DroplistManager.getInstance().get("RELATION.xml","",request));
			}
			
			Integer kode_produk = editBac.getDatausulan().getLsbs_id();
 			Integer number_produk = editBac.getDatausulan().getLsdbs_number();
			String nama_produk="";
			if (kode_produk!=null && number_produk !=null){
				
				nama_produk="produk_asuransi.n_prod_"+FormatString.rpad("0", kode_produk.toString(), 2);
				
				try{
					Class aClass = Class.forName( nama_produk );
					n_prod produk = (n_prod) aClass.newInstance();
					produk.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
					
					produk.cek_flag_agen(kode_produk, number_produk, 0);
					editBac.getDatausulan().setFlag_platinumlink(produk.flag_platinumlink);

					if (produk.isProductBancass){
						editBac.getDatausulan().setFlag_bao(1);
					}else{
						editBac.getDatausulan().setFlag_bao(0);
					}
					
					produk.of_set_bisnis_no(number_produk);
					produk.ii_bisnis_no=(number_produk);
					produk.ii_bisnis_id=(kode_produk);
					
					produk.of_set_kurs(editBac.getDatausulan().getKurs_premi());
					int kode_flag = produk.kode_flag;
					editBac.getDatausulan().setKode_flag(kode_flag);
					if (produk.isBungaSimponi){
						editBac.getDatausulan().setIsBungaSimponi(1);	
					}else{
						editBac.getDatausulan().setIsBungaSimponi(0);	
					}
					if (produk.isBonusTahapan){
						editBac.getDatausulan().setIsBonusTahapan(1);
					}else{
						editBac.getDatausulan().setIsBonusTahapan(0);
					}
					
					editBac.getDatausulan().setFlag_account(produk.flag_account);
					editBac.getDatausulan().setFlag_as(produk.flag_as);
					editBac.getDatausulan().setFlag_worksite(produk.flag_worksite);
					editBac.getDatausulan().setFlag_endowment(produk.flag_endowment);
					editBac.getDatausulan().setFlag_horison(produk.flag_horison);
					
					if (editBac.getDatausulan().getFlag_worksite().intValue() == 1){
						String kode_perusahaan = editBac.getPemegang().getMspo_customer();
						String nama_perusahaan="";
						if(kode_perusahaan!=null){
							Map data1= (HashMap) this.elionsManager.select_namacompany(kode_perusahaan);
							if (data1!=null){		
								nama_perusahaan = (String) data1.get("COMPANY_NAMA");
							}
						}
						if (nama_perusahaan.toUpperCase().contains("EKA LIFE") || nama_perusahaan.toUpperCase().contains("EKALIFE")){
							editBac.getDatausulan().setFlag_worksite_ekalife(1);
							editBac.getDatausulan().setMste_flag_el(1);
						}
					}
					//tutup try produk rider included
				} catch (ClassNotFoundException e){
					logger.error("ERROR :", e);
				} catch (InstantiationException e) {
					logger.error("ERROR :", e);
				} catch (IllegalAccessException e) {
					logger.error("ERROR :", e);
				}
			}
			editBac.getDatausulan().setLde_id(currentUser.getLde_id());

			//bank rekening autodebet
			String bank_pp=editBac.getAccount_recur().getLbn_id();	
			String nama_bank_autodebet="";
			if(bank_pp!=null){
				Map data1= (HashMap) this.elionsManager.select_bank2(bank_pp);
				if (data1!=null){		
					nama_bank_autodebet = (String) data1.get("BANK_NAMA");
					//cek nama bank rek client kalau sudah pernah diisi
					editBac.getAccount_recur().setLbn_nama(nama_bank_autodebet);
				}
			}
			
			//bank rekening client
			String bank_pp1=editBac.getRekening_client().getLsbp_id();
			String nama_bank_rekclient="";
			if(bank_pp1!=null){
				Map data1= (HashMap) this.elionsManager.select_bank1(bank_pp1);
				if (data1!=null){		
					nama_bank_rekclient = (String)data1.get("BANK_NAMA");
					editBac.getRekening_client().setLsbp_nama(nama_bank_rekclient);
				}
			}
			
			//cek nama perusahaan kalau sudah dipilih
			String kode_perusahaan = editBac.getPemegang().getMspo_customer();
			String nama_perusahaan="";
			if(kode_perusahaan!=null){
				Map data1= (HashMap) this.elionsManager.select_namacompany(kode_perusahaan);
				if (data1!=null){		
					nama_perusahaan = (String)data1.get("COMPANY_NAMA");
					editBac.getPemegang().setMspo_customer_nama(nama_perusahaan);
				}
			}
			
		/** Halaman 2 : Tertanggung Polis */
		}else if (page ==1){
			refData = new HashMap<String, Object>();
			Cmdeditbac editBac = (Cmdeditbac) cmd;
			if (editBac.getTertanggung().getKota_rumah()==null ){
				editBac.getTertanggung().setKota_rumah("");
			}
			
			if (editBac.getTertanggung().getKota_kantor()==null  ){
				editBac.getTertanggung().setKota_kantor("");
			}
			
			if (editBac.getAddressbilling().getKota_tgh()==null){
				editBac.getAddressbilling().setKota_tgh("");				
			}
			
			refData.put("select_gelar",DroplistManager.getInstance().get("GELAR.xml","ID",request));
			
			// REFDATA UNTUK HALAMAN LAMA
			refData.put("select_identitas",DroplistManager.getInstance().get("IDENTITY.xml","ID",request));
			refData.put("select_negara",DroplistManager.getInstance().get("WARGANEGARA.xml","ID",request));
			refData.put("select_industri",DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","",request));
			refData.put("select_pekerjaan",DroplistManager.getInstance().get("KLASIFIKASI_PEKERJAAN.xml","",request));
			refData.put("select_medis",DroplistManager.getInstance().get("medis.xml","ID",request));
			refData.put("select_marital",DroplistManager.getInstance().get("MARITAL.xml","ID",request));
			refData.put("select_agama",DroplistManager.getInstance().get("AGAMA.xml","ID",request));
			refData.put("select_pendidikan",DroplistManager.getInstance().get("PENDIDIKAN.xml","ID",request));
			refData.put("select_tujuan",DroplistManager.getInstance().get("TUJUAN_ASR.xml","",request));
			refData.put("select_penghasilan",DroplistManager.getInstance().get("PENGHASILAN.xml","",request));
			refData.put("select_dana",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","",request));
			
			// REFDATA UNTUK HALAMAN BARU
//			refData.put("select_identitas", elionsManager.selectDropDown("eka.lst_identity", "lside_id", "lside_name", "lside_id", null));
//			refData.put("select_negara", elionsManager.selectDropDown("eka.lst_negara", "lsne_id", "lsne_note", "lsne_id", null));
//			refData.put("select_agama", elionsManager.selectDropDown("eka.lst_agama", "lsag_id", "lsag_name", "lsag_id", null));
//			refData.put("select_pendidikan", elionsManager.selectDropDown("eka.lst_education", "lsed_id", "lsed_name", "lsed_id", null));
			
			//apabila inputan bank
			if(!currentUser.getCab_bank().equals("") && !currentUser.getName().contains("SIMASPRIMA")) {
				refData.put("select_relasi",DroplistManager.getInstance().get("RELATION_BANCASS.xml","",request));
			}else {
				refData.put("select_relasi",DroplistManager.getInstance().get("RELATION.xml","",request));
			}

		/** Halaman 3 : Data Usulan Asuransi */
		}else if (page ==2){
			refData = new HashMap<String, Object>();
			
			// REFDATA UNTUK HALAMAN LAMA
			if(currentUser.getJn_bank().intValue() == 0 || currentUser.getJn_bank().intValue() == 1){ //BII
				refData.put("listprodukutama", this.elionsManager.select_produkutama_platinumbii());
				refData.put("listtipeproduk",this.elionsManager.select_tipeproduk_platinumbii());
			}else if(currentUser.getJn_bank().intValue() == 2){ //BANK SINARMAS
				refData.put("listprodukutama", this.elionsManager.select_produkutama_banksinarmas());
				refData.put("listtipeproduk",this.elionsManager.select_tipeproduk_banksinarmas());
			}else if(currentUser.getJn_bank().intValue() == 3){ //sinarmas sekuritas
				refData.put("listprodukutama", this.elionsManager.select_produkutama_sekuritas());
				refData.put("listtipeproduk",this.elionsManager.select_tipeproduk_sekuritas());
			}else {
				refData.put("listprodukutama", this.elionsManager.select_produkutama());
				refData.put("listtipeproduk",this.elionsManager.select_tipeproduk());
			}

			// REFDATA UNTUK HALAMAN BARU
//			if(currentUser.getCab_bank() == null) {
//				refData.put("listprodukutama", this.elionsManager.select_produkutama());
//				refData.put("listtipeproduk", elionsManager.selectDropDown("eka.lst_type_produk", "lstp_id", "lstp_produk", "lstp_id", null));
//			}else if(currentUser.getJn_bank().intValue() == 0 || currentUser.getJn_bank().intValue() == 1){ //BII
//				refData.put("listprodukutama", this.elionsManager.select_produkutama_platinumbii());
//				refData.put("listtipeproduk", elionsManager.selectDropDown("eka.lst_type_produk", "lstp_id", "lstp_produk", "lstp_id", "lstp_id = 7"));
//			}else if(currentUser.getJn_bank().intValue() == 2){ //BANK SINARMAS
//				refData.put("listprodukutama", this.elionsManager.select_produkutama_banksinarmas());
//				refData.put("listtipeproduk", elionsManager.selectDropDown("eka.lst_type_produk", "lstp_id", "lstp_produk", "lstp_id", "lstp_id = 5"));
//			}
			
			refData.put("select_autodebet",DroplistManager.getInstance().get("AUTODEBET.xml","ID",request));
			refData.put("select_insrider",DroplistManager.getInstance().get("INSRIDER.xml","ID",request));
			refData.put("select_rider",DroplistManager.getInstance().get("PLANRIDER.xml","BISNIS_ID",request));
			
			Cmdeditbac editBac = (Cmdeditbac) cmd;
			if(editBac.getDatausulan().getLsbs_id() != null) {
				refData.put("select_rider", elionsManager.list_rider(editBac.getDatausulan().getLsbs_id().toString(), editBac.getDatausulan().getLsdbs_number().toString()));
			}
			
			refData.put("select_dplk",DroplistManager.getInstance().get("DPLK.xml","ID",request));
			refData.put("select_kombinasi",DroplistManager.getInstance().get("KOMBINASI_PREMI.XML","ID",request));
			
		/** Halaman 4 : Detail Investasi */
		}else if (page==3){
			refData = new HashMap<String, Object>();
			refData.put("select_jns_top_up",DroplistManager.getInstance().get("TOPUP.xml","ID",request));	
			refData.put("select_jenis_nasabah",DroplistManager.getInstance().get("jenis_nasabah.xml","ID",request));
			refData.put("select_jenis_tabungan",DroplistManager.getInstance().get("jenis_tabungan.xml","ID",request));
			refData.put("select_jangka_invest",DroplistManager.getInstance().get("JNSTOPUP.xml","ID",request));
			
			Cmdeditbac editBac = (Cmdeditbac) cmd;
			Integer kode_produk = editBac.getDatausulan().getLsbs_id();
			
			//bila keluarga stable save
			if(products.stableSavePremiBulanan(kode_produk.toString()) || products.stableSave(kode_produk, editBac.getDatausulan().getLsdbs_number()) || products.progressiveLink(kode_produk.toString())){
				if(products.stableSavePremiBulanan(kode_produk.toString()) || products.progressiveLink(kode_produk.toString())){
					refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER_STABSAVE_PREMI.xml","ID",request));
				}else{
					refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER_STABSAVE.xml","ID",request));
				}
			//bila polisinputan bank sinarmas
			}else if(!currentUser.getCab_bank().equals("") && currentUser.getJn_bank().intValue() == 2) {
				refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER_BANCASS.xml","ID",request));
				//if(currentUser.getName().contains("SIMASPRIMA")) refData.put("select_jenisbunga",DroplistManager.getInstance().get("jenisbunga.xml","",request));
			//bila powersave bulanan, hanya buka rollover premi
			}else if(kode_produk.intValue() == 158 || kode_produk.intValue() == 176 ){
				refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER_BULANAN.xml","ID",request));
			}else {
				refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER.xml","ID",request));
			}

			refData.put("select_jenisbunga",DroplistManager.getInstance().get("jenisbunga_bancass.xml","",request));
			//hanya cabang BAS / UW yang bisa input special rate, selain itu hanya normal saja
			if(currentUser.getLde_id().equals("11") || currentUser.getLde_id().equals("29") || currentUser.getJn_bank().intValue() == 3){
				refData.put("select_jenisbunga",DroplistManager.getInstance().get("jenisbunga.xml","",request));
			}
			
			if(!currentUser.getCab_bank().equals("") && !currentUser.getName().contains("SIMASPRIMA")) {
				refData.put("select_hubungan",DroplistManager.getInstance().get("RELATION_BANCASS_BENEF.xml","",request));
			}else {
				refData.put("select_hubungan",DroplistManager.getInstance().get("RELATION.xml","",request));
			}
			
			refData.put("select_karyawan",DroplistManager.getInstance().get("karyawan.xml","",request));
			refData.put("select_kurs",DroplistManager.getInstance().get("KURS.xml","ID",request));

		/** Halaman 5 : Detail Agen*/
		}else if (page ==4){
			if(currentUser.getJn_bank().intValue() == 2 || currentUser.getJn_bank().intValue() == 3){ //BANK SINARMAS
				Cmdeditbac editBac = (Cmdeditbac) cmd;
				editBac.getAddressbilling().setRegion("010100");
			}
			refData = new HashMap<String, Object>();
			refData.put("select_regional", elionsManager.selectDropDown("eka.lst_region", "(lca_id||lwk_id||lsrg_id)", "lsrg_nama", "", "lsrg_nama", null));
			//refData.put("select_regional", DroplistManager.getInstance().get("region.xml","ID",request));
			
		/** Halaman 6 : Konfirmasi */
		}else if (page==5)	{
			Cmdeditbac editBac = (Cmdeditbac) cmd;
			String kode=editBac.getDatausulan().getPlan();
			refData = new HashMap<String, Object>();
			List tmp = this.elionsManager.select_namaproduk(kode);
			refData.put("listprodukutama",tmp);
			
			HttpSession session = request.getSession();
			String res = session.getServletContext().getResource("/xml/").toString();
			
			refData.put("jenis_id_pp", Common.searchXml(res+"IDENTITY.xml", "ID", "TDPENGENAL", editBac.getPemegang().getLside_id()));
			refData.put("negara_pp", Common.searchXml(res+"WARGANEGARA.xml", "ID", "NEGARA", editBac.getPemegang().getLsne_id()));
			refData.put("status_pp", Common.searchXml(res+"MARITAL.xml", "ID", "MARITAL", editBac.getPemegang().getMspe_sts_mrt()));
			refData.put("agama_pp", Common.searchXml(res+"AGAMA.xml", "ID", "AGAMA", editBac.getPemegang().getLsag_id()));
			refData.put("pendidikan_pp", Common.searchXml(res+"PENDIDIKAN.xml", "ID", "PENDIDIKAN", editBac.getPemegang().getLsed_id()));

			if(!currentUser.getCab_bank().equals("") && !currentUser.getName().contains("SIMASPRIMA")) {
				refData.put("relation_ttg", Common.searchXml(res+"RELATION.xml", "ID", "RELATION_BANCASS", editBac.getPemegang().getLsre_id()));
			}else {
				refData.put("relation_ttg", Common.searchXml(res+"RELATION.xml", "ID", "RELATION", editBac.getPemegang().getLsre_id()));
			}

			refData.put("jenis_id_ttg1", Common.searchXml(res+"IDENTITY.xml", "ID", "TDPENGENAL", editBac.getTertanggung().getLside_id()));
			refData.put("negara_ttg1", Common.searchXml(res+"WARGANEGARA.xml", "ID", "NEGARA", editBac.getTertanggung().getLsne_id()));
			refData.put("status_ttg1", Common.searchXml(res+"MARITAL.xml", "ID", "MARITAL", editBac.getTertanggung().getMspe_sts_mrt()));
			refData.put("agama_ttg1", Common.searchXml(res+"AGAMA.xml", "ID", "AGAMA", editBac.getTertanggung().getLsag_id()));
			refData.put("pendidikan_ttg1", Common.searchXml(res+"PENDIDIKAN.xml", "ID", "PENDIDIKAN", editBac.getTertanggung().getLsed_id()));

			refData.put("kurs_premi", Common.searchXml(res+"KURS.xml", "ID", "SYMBOL", editBac.getDatausulan().getKurs_p()));
			refData.put("kurs_up", Common.searchXml(res+"KURS.xml", "ID", "SYMBOL", editBac.getDatausulan().getKurs_p()));
			refData.put("pmode", Common.searchXml(res+"CARABAYAR.xml", "ID", "PAYMODE", editBac.getDatausulan().getLscb_id()));
			refData.put("auto_debet", Common.searchXml(res+"AUTODEBET.xml", "ID", "AUTODEBET", editBac.getDatausulan().getMste_flag_cc()));

			refData.put("regional", Common.searchXml(res+"region.xml", "ID", "REGION", editBac.getAddressbilling().getRegion()));
			refData.put("roll", Common.searchXml(res+"ROLLOVER.xml", "ID", "ROLLOVER", editBac.getPowersave().getMps_roll_over()));
			refData.put("tab",Common.searchXml(res+"jenis_tabungan.xml", "ID", "AUTODEBET", editBac.getRekening_client().getMrc_jenis()));
			refData.put("nsbh", Common.searchXml(res+"jenis_nasabah.xml", "ID", "JENIS", editBac.getRekening_client().getMrc_jn_nasabah()));
			refData.put("jnstopup", Common.searchXml(res+"TOPUP.xml", "ID", "JENIS", editBac.getInvestasiutama().getDaftartopup().getPil_berkala()));
			refData.put("jk", Common.searchXml(res+"JNSTOPUP.xml", "ID", "JANGKAWAKTU", editBac.getPowersave().getMps_jangka_inv()));
			refData.put("jns", Common.searchXml(res+"jenisbunga.xml", "ID", "JENIS", editBac.getPowersave().getMps_jenis_plan()));
			refData.put("kry", Common.searchXml(res+"karyawan.xml", "ID", "KRY", editBac.getPowersave().getMps_employee()));
			refData.put("kurs_rek", Common.searchXml(res+"KURS.xml", "ID", "SYMBOL", editBac.getRekening_client().getMrc_kurs()));
			
			refData.put("select_insrider",DroplistManager.getInstance().get("INSRIDER.xml","ID",request));

			refData.put("select_rider",DroplistManager.getInstance().get("PLANRIDER.xml","BISNIS_ID",request));
			if(editBac.getDatausulan().getLsbs_id() != null) {
				refData.put("select_rider", elionsManager.list_rider(editBac.getDatausulan().getLsbs_id().toString(), editBac.getDatausulan().getLsdbs_number().toString()));
			}

			if(!currentUser.getCab_bank().equals("") && !currentUser.getName().contains("SIMASPRIMA")) {
				refData.put("select_hubungan",DroplistManager.getInstance().get("RELATION_BANCASS.xml","ID",request));
			}else {
				refData.put("select_hubungan",DroplistManager.getInstance().get("RELATION.xml","ID",request));
			}
			
			if(!currentUser.getCab_bank().equals("")) {
				if(currentUser.getJn_bank().intValue() == 2) {
					String spv=request.getParameter("spv");
					
					String valid1=elionsManager.selectUserName(currentUser.getValid_bank_1());
					String valid2=elionsManager.selectUserName(currentUser.getValid_bank_2());
					String pass1 = editBac.getPemegang().getPass1();
					String pass2 = editBac.getPemegang().getPass2();
					String password1 = editBac.getPemegang().getPassword1();
					String password2 = editBac.getPemegang().getPassword2();
					refData.put("valid1", elionsManager.selectUserName(currentUser.getValid_bank_1()));
					refData.put("valid2", elionsManager.selectUserName(currentUser.getValid_bank_2()));
					if (spv!=null){
						spv= spv.toUpperCase();
						editBac.getPemegang().setSpv(spv);
						refData.put("spv", "-");
						if (!(spv.equalsIgnoreCase(valid1) || spv.equalsIgnoreCase(valid2)) && !err.hasErrors()){
							err.rejectValue("pemegang.pass1","","Password SPV/PINCAB yang anda masukkan salah.");
						}
					}
				}else if(currentUser.getJn_bank().intValue() == 3) {
					refData.put("valid1", "-");
					refData.put("daftarOtor", elionsManager.selectDropDown(
							"EKA.LST_USER", "LUS_ID", "LUS_LOGIN_NAME", "", "LUS_LOGIN_NAME", 
							"cab_bank = '"+currentUser.getCab_bank()+"' AND jn_bank = 3 AND flag_approve = 1 AND lus_id <> " + currentUser.getLus_id()));
				}
			}				
		}
		
		return refData;
	}
	
	protected void initBinder(HttpServletRequest arg0, ServletRequestDataBinder binder) throws Exception {
		logger.debug("EditBacController : initBinder");
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		logger.debug("EditBacController : formBackingObject");
        this.accessTime = System.currentTimeMillis();
        HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		String lde_id = currentUser.getLde_id();
		String spv=request.getParameter("spv");
		String spaj = request.getParameter("showSPAJ");

		Cmdeditbac detiledit = (Cmdeditbac) session.getAttribute("dataInputSpaj");
		if(request.getParameter("data_baru") != null) {
			session.removeAttribute("dataInputSpaj");
			detiledit = null;
		}
		
		if(detiledit != null) { // SUMBER DATA : DARI GAGAL SUBMIT SEBELUMNYA
			request.setAttribute("adaData", "adaData");
		}else {
			detiledit = new Cmdeditbac();
			
			String kopiSPAJ = ServletRequestUtils.getStringParameter(request, "kopiSPAJ", "");
			
			/** EDIT SPAJ */
			if(spaj!=null) {
				int v = this.elionsManager.selectValidasiEditUnitLink(spaj);
				if(v > 0) {
					detiledit.setPesan("PERHATIAN!!! Proses FUND sudah dilakukan untuk polis ini. Anda tidak bisa mengedit detail Unit-Link.");
				}
				
				detiledit.setPemegang((Pemegang)this.elionsManager.selectpp(spaj));
				detiledit.getPemegang().setSpv(spv);
				
				Map sumber_bisnis = elionsManager.select_sumberBisnis(detiledit.getPemegang().getSumber_id());
				if(sumber_bisnis != null){
					String nama_sumber = (String) sumber_bisnis.get("NAMA_SUMBER");
					detiledit.getPemegang().setNama_sumber(nama_sumber);
				}
				
				detiledit.setNo_pb(uwManager.selectNoPB(spaj));
				detiledit.setAddressbilling((AddressBilling)this.elionsManager.selectAddressBilling(spaj));
				detiledit.setDatausulan((Datausulan)this.elionsManager.selectDataUsulanutama(spaj));
				detiledit.getDatausulan().setLde_id(lde_id);
				detiledit.setTertanggung((Tertanggung)this.elionsManager.selectttg(spaj));

				Powersave data_pwr= new Powersave();
				String spaj_conversi = uwManager.selectCekSpajSebelumSurrender(spaj);
				if(spaj_conversi!=null){
					data_pwr.setMsl_spaj_lama(spaj_conversi);
				}
				if (products.stableLink(detiledit.getDatausulan().getLsbs_id().toString())) {
					data_pwr = (Powersave)this.elionsManager.select_slink(spaj);
					Powersave dt = (Powersave)this.elionsManager.select_slink_topup(spaj);
					if(data_pwr.getFlag_rider()==1){//ada rider
						Map mappingRiderSave = uwManager.select_rider_save(spaj);
						BigDecimal mpr_cb_rider = (BigDecimal) mappingRiderSave.get("MRS_RIDER_CB");
						BigDecimal lscb_id_rider = (BigDecimal) mappingRiderSave.get("LSCB_ID_RIDER");
						data_pwr.setMpr_cara_bayar_rider(mpr_cb_rider.intValue());
						detiledit.getDatausulan().setLscb_id_rider(lscb_id_rider.intValue());
					}
					if (dt == null) {
						InvestasiUtama inv = new InvestasiUtama();
						
						// DAFTAR INVESTASI UNTUK HALAMAN LAMA						
						inv.setDaftarinvestasi(this.elionsManager.selectDetailInvestasi(null));
						inv.setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(null));
						
						detiledit.setInvestasiutama(inv);
						DetilTopUp  detiltopup = new DetilTopUp();
						inv.setDaftartopup(detiltopup);
						data_pwr.setBegdate_topup(null);
						data_pwr.setEnddate_topup(null);
						detiledit.getDatausulan().setCara_premi(1);
					}else{
						InvestasiUtama inv = new InvestasiUtama();

						// DAFTAR INVESTASI UNTUK HALAMAN LAMA						
						inv.setDaftarinvestasi(this.elionsManager.selectDetailInvestasi(null));
						inv.setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(null));
						
						detiledit.setInvestasiutama(inv);
						DetilTopUp  detiltopup = new DetilTopUp();
						inv.setDaftartopup(detiltopup);
						detiledit.getDatausulan().setCara_premi(1);
						detiledit.getDatausulan().setTotal_premi_kombinasi(data_pwr.getMps_prm_deposit() + dt.getMps_prm_deposit());
						inv.getDaftartopup().setPil_tunggal(2);
						//inv.getDaftartopup().setPremi_tunggal(dt.getMps_prm_deposit());
						data_pwr.setBegdate_topup(dt.getMsl_bdate());
						data_pwr.setEnddate_topup(dt.getMsl_edate());
						if(spaj_conversi!=null){
							Powersave roTerakhir = uwManager.selectRolloverPowersaveTerakhir(spaj_conversi);
							data_pwr.setBegdate_topup(roTerakhir.mpr_mature_date);
							detiledit.getDatausulan().setPsave(true);
						}
					}
	
				}else{
					data_pwr = (Powersave) this.elionsManager.select_powersaver(spaj);
					if(data_pwr != null){
						if(data_pwr.getFlag_rider() != null){
							if(data_pwr.getFlag_rider()==1){//ada rider
								Map mappingRiderSave = uwManager.select_rider_save(spaj);
								BigDecimal mpr_cb_rider = (BigDecimal) mappingRiderSave.get("MRS_RIDER_CB");
								BigDecimal lscb_id_rider = (BigDecimal) mappingRiderSave.get("LSCB_ID_RIDER");
								data_pwr.setMpr_cara_bayar_rider(mpr_cb_rider.intValue());
								detiledit.getDatausulan().setLscb_id_rider(lscb_id_rider.intValue());
							}
						}
					}
					InvestasiUtama investasi  = (InvestasiUtama)this.elionsManager.selectinvestasiutama(spaj);
					if (investasi == null) {
						InvestasiUtama inv = new InvestasiUtama();

						// DAFTAR INVESTASI UNTUK HALAMAN LAMA						
						inv.setDaftarinvestasi(this.elionsManager.selectDetailInvestasi(null));
						inv.setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(null));
						
						detiledit.setInvestasiutama(inv);
						DetilTopUp  detiltopup = new DetilTopUp();
						inv.setDaftartopup(detiltopup);
					}else{
						detiledit.setInvestasiutama(investasi);
						DetilTopUp detiltopup = (DetilTopUp)this.elionsManager.select_detil_topup(spaj);
						if (detiltopup == null) {
							detiltopup = new DetilTopUp();
							detiledit.getInvestasiutama().setDaftartopup(detiltopup);
						}else{
							detiledit.getInvestasiutama().setDaftartopup(detiltopup);
						}
					}
				}
				
				if (data_pwr==null) {
					detiledit.setPowersave(new Powersave());
				}else{
					detiledit.setPowersave(data_pwr);
				}

				//START OF fee based income = rate powersave + mst_default (Yusuf 3/4/2008)
				Map info = elionsManager.selectInformasiPowersaveUntukFeeBased(spaj);
				if(info != null) {
					String nama_produk="produk_asuransi.n_prod_"+FormatString.rpad("0", ((BigDecimal) info.get("LSBS_ID")).toString(), 2);
					Class aClass = Class.forName( nama_produk );
					n_prod produk = (n_prod) aClass.newInstance();
					produk.ii_bisnis_no = ((BigDecimal) info.get("LSDBS_NUMBER")).intValue();
					produk.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
					
					if(detiledit.getPowersave().getFlag_bulanan() != null){
						produk.of_get_bisnis_no(detiledit.getPowersave().getFlag_bulanan());
					}else{
						produk.of_get_bisnis_no(0);
					}
					
					hit_biaya_powersave h = new hit_biaya_powersave();
					
					int flag_breakable = 0;
					if(detiledit.getPowersave().getMpr_breakable() != null) flag_breakable = detiledit.getPowersave().getMpr_breakable();  
					
					Map data =  elionsManager.selectbungaprosave(
							(String) info.get("LKU_ID"),
							String.valueOf(h.hit_jangka_invest(((BigDecimal) info.get("MPR_JANGKA_INVEST")).toString())),
							((BigDecimal) info.get("MPR_DEPOSIT")).doubleValue(),
							(Date) info.get("MSTE_BEG_DATE"),
							produk.flag_powersave, flag_breakable);
					
					if(data != null) {
						double rate = Double.parseDouble(data.get("LPR_RATE").toString());
						double feebased = elionsManager.selectMst_default_numeric(31);
						detiledit.getPowersave().setFee_based_income((rate + feebased) - detiledit.getPowersave().getMps_rate());
					}
				}else {
					detiledit.getPowersave().setFee_based_income(0.);
				}
				//END OF fee based income = rate powersave + mst_default (Yusuf 3/4/2008)

				detiledit.setRekening_client(this.elionsManager.select_rek_client(spaj));
				detiledit.setAccount_recur(this.elionsManager.select_account_recur(spaj));
				detiledit.setAgen(this.elionsManager.select_detilagen(spaj));
				/**
				 * TAMBAHAN DARI BERTHO UNTUK SPLIT NO REKENING
				 */			
				detiledit.getRekening_client().setMrc_no_ac_split(FormatString.splitWordToCharacter(detiledit.getRekening_client().getMrc_no_ac(),21));
				detiledit.getAccount_recur().setMar_acc_no_split(FormatString.splitWordToCharacter(detiledit.getAccount_recur().getMar_acc_no(),21));
				////--------------END OF SPLIT REKENING--------------------////////////
			
				String kode_agen = detiledit.getAgen().getMsag_id();
				String nama_agent="";
				if (kode_agen.equalsIgnoreCase("000000")){
					nama_agent = (String) this.elionsManager.select_agent_temp(spaj);
				}
				detiledit.getAgen().setMcl_first(nama_agent);
				detiledit.setEmployee(this.elionsManager.select_detilemployee(spaj));
				
				detiledit.setHistory(new History());
				detiledit.getDatausulan().setDaftahcp(this.elionsManager.select_hcp(spaj));
				detiledit.getDatausulan().setDaftapeserta(this.elionsManager.select_semua_mst_peserta(spaj));

			/** INSERT SPAJ BARU */
			}else{
				
				detiledit.setPemegang(new Pemegang());
				detiledit.setAddressbilling(new AddressBilling());
				detiledit.setDatausulan(new Datausulan());
				detiledit.setTertanggung(new Tertanggung());
				InvestasiUtama inv = new InvestasiUtama();

				// DAFTAR INVESTASI UNTUK HALAMAN LAMA						
				inv.setDaftarinvestasi(this.elionsManager.selectDetailInvestasi(null));
				inv.setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(null));
				
				detiledit.setInvestasiutama(inv);
				inv.setDaftartopup(new DetilTopUp());
				detiledit.setRekening_client(new Rekening_client());
				detiledit.setAccount_recur(new Account_recur());
				detiledit.setPowersave(new Powersave());

				//fee based income = rate powersave + mst_default (Yusuf 3/4/2008)
				detiledit.getPowersave().setFee_based_income(elionsManager.selectMst_default_numeric(31));
				
				detiledit.setAgen(new Agen());
				detiledit.setHistory(new History());
				detiledit.setEmployee(new Employee());
				
				//Deddy : Khusus DMTM apabila melakukan copy SPAJ, maka dicek ke table mst_temp_dmtm agar SPAJ tidak digenerate ulang kembali disini.
				if(!kopiSPAJ.trim().equals("")) {
					Integer flag_gmanual_dmtm = uwManager.selectCountMstTempDMTM(kopiSPAJ);
					if(flag_gmanual_dmtm>=1){
						detiledit.setFlag_gmanual_spaj(flag_gmanual_dmtm);
						Map detil_spaj = uwManager.selectRegSpajMstTempDMTM(kopiSPAJ);
						String reg_spaj_manual = (String) detil_spaj.get("REG_SPAJ_MANUAL");
						String reg_spaj_lama = (String) detil_spaj.get("REG_SPAJ");
						
						Pemegang p = (Pemegang) this.elionsManager.selectpp(reg_spaj_lama);
						if(p != null) {
							detiledit.setPemegang(p);
						}
						//data addr billing
						AddressBilling a = (AddressBilling) this.elionsManager.selectAddressBilling(reg_spaj_lama);
						if(a != null) {
							detiledit.setAddressbilling(a);
						}
						//data tertanggung
						Tertanggung t = (Tertanggung) this.elionsManager.selectttg(reg_spaj_lama);
						if(t != null) {
							detiledit.setTertanggung(t);
						}
						//data rekening client
						Rekening_client rc = this.elionsManager.select_rek_client(reg_spaj_lama);
						if(rc != null) {
							detiledit.setRekening_client(rc);
							detiledit.getRekening_client().setMrc_no_ac_split(FormatString.splitWordToCharacter(detiledit.getRekening_client().getMrc_no_ac(),21));
						}
						//data ahli waris
						detiledit.getDatausulan().setDaftabenef(elionsManager.select_benef(reg_spaj_lama));
						detiledit.getPemegang().setReg_spaj(reg_spaj_manual);
						detiledit.getAddressbilling().setReg_spaj(reg_spaj_manual);
						detiledit.getTertanggung().setReg_spaj(reg_spaj_manual);
					}else{
//						harus punya hak akses untuk mengkopi data
						List akses = currentUser.getAksesCabang();
						kopiSPAJ = elionsManager.selectGetSpaj(kopiSPAJ);
						String region = elionsManager.selectCabangFromSpaj_lar(kopiSPAJ);
						if (akses.contains(region) || currentUser.getLca_id().equals("01")) {
							//data pemegang
							Pemegang p = (Pemegang) this.elionsManager.selectpp(kopiSPAJ);
							if(p != null) {
								detiledit.setPemegang(p);
								p.setReg_spaj(null);
							}
							//data addr billing
							AddressBilling a = (AddressBilling) this.elionsManager.selectAddressBilling(kopiSPAJ);
							if(a != null) {
								detiledit.setAddressbilling(a);
								a.setReg_spaj(null);
							}
							//data tertanggung
							Tertanggung t = (Tertanggung) this.elionsManager.selectttg(kopiSPAJ);
							if(t != null) {
								t.setReg_spaj(null);
								detiledit.setTertanggung(t);
							}
							//data rekening client
							Rekening_client rc = this.elionsManager.select_rek_client(kopiSPAJ);
							if(rc != null) {
								detiledit.setRekening_client(rc);
								detiledit.getRekening_client().setMrc_no_ac_split(FormatString.splitWordToCharacter(detiledit.getRekening_client().getMrc_no_ac(),21));
							}
							//data ahli waris
							detiledit.getDatausulan().setDaftabenef(elionsManager.select_benef(kopiSPAJ));
							
						}
					}
				}
				
				//Yusuf - Apabila inputan bank sinarmas, set beberapa nilai default
				if(!currentUser.getCab_bank().equals("") && currentUser.getJn_bank().intValue() == 2) {
					//detiledit.getDatausulan().setLsbs_id(142); //lsbs : powersave 
					//detiledit.getDatausulan().setLsdbs_number(2); //lsdbs : powersave bank sinarmas
					//detiledit.getDatausulan().setTipeproduk(5); //tipe produk : powersave
					//detiledit.getAgen().setMsag_id("016409"); //kode penutup : bank sinarmas
					//req NOVIE : UNTUK BANK SINARMAS< DEFAULT KODE AGENTNYA KE 021052
					detiledit.getDatausulan().setLsbs_id(164); //lsbs : simas stabil link 
					detiledit.getDatausulan().setLsdbs_number(2); //lsdbs : stabil link bank sinarmas
					detiledit.getDatausulan().setTipeproduk(5); //tipe produk : powersave
					detiledit.getAgen().setMsag_id("021052"); //kode penutup : bank sinarmas
					detiledit.getPemegang().setMspo_ao("021052");
					//detiledit.getPemegang().setMspo_ao("016409");
					if(currentUser.getMsag_id_ao() != null) {
						if(!currentUser.getMsag_id_ao().equals("")) {
							detiledit.getPemegang().setMspo_ao(currentUser.getMsag_id_ao());
						}
					}

				//Yusuf - Apabila inputan BII, set beberapa nilai default
				}else if(!currentUser.getCab_bank().equals("") && (currentUser.getJn_bank().intValue() == 0 || currentUser.getJn_bank().intValue() == 1)) {
					detiledit.getDatausulan().setTipeproduk(7); //tipe produk : BII
				//Yusuf - Apabila inputan sinarmas sekuritas, set beberapa nilai default
				}else if(!currentUser.getCab_bank().equals("") && currentUser.getJn_bank().intValue() == 3) {
					detiledit.getDatausulan().setLsbs_id(142); //lsbs : powersave 
					detiledit.getDatausulan().setLsdbs_number(9); //lsdbs : danamas prima
					detiledit.getDatausulan().setTipeproduk(5); //tipe produk : powersave
					detiledit.getAgen().setMsag_id("016374"); //kode penutup : SIMAS SEKURITAS
					
					detiledit.getPemegang().setMspo_ao("016374");
					if(currentUser.getMsag_id_ao() != null) {
						if(!currentUser.getMsag_id_ao().equals("")) {
							detiledit.getPemegang().setMspo_ao(currentUser.getMsag_id_ao());
						}
					}
				}
				
				//Deddy(25 Sep 2009) - proses konversi dicek apakah kopiSpaj merupakan produk powersave
				String lsbs_id_kopi = uwManager.selectLsbsId(kopiSPAJ);
				boolean isPsaveToSlink = false;
				Integer convertPsaveToSlink = 0;
				if(products.powerSave(lsbs_id_kopi)){
					isPsaveToSlink = true;
					convertPsaveToSlink = 1;
				}
				
				//Yusuf (31 Aug 2009) - Bagian dari proses Konversi Psave ke Slink
				if(isPsaveToSlink){
					if(uwManager.selectCountMstSurrender(kopiSPAJ)>0){
						detiledit.setDatausulan((Datausulan)this.elionsManager.selectDataUsulanutama(kopiSPAJ));
						detiledit.getDatausulan().setPsave(isPsaveToSlink);
						detiledit.getDatausulan().setKopiSPAJ(kopiSPAJ);
						detiledit.getDatausulan().setConvert(convertPsaveToSlink);
						//Yusuf (31 Aug 2009) - Bagian dari proses Konversi Psave ke Slink
						//Apakah Spaj ini merupakan konversi produk Powersave / Simas Prima ke Stable Link / Simas Stabil Link?
						//detiledit.getDatausulan().setConvertPsaveToSlink(convertPsaveToSlink);
						
						if(currentUser.getJn_bank().intValue() == 2){
							detiledit.getDatausulan().setLsbs_id(164); //lsbs : slink 
							detiledit.getDatausulan().setLsdbs_number(2); //lsdbs : simas stabil link
							detiledit.getDatausulan().setTipeproduk(5); //tipe produk : powersave
							detiledit.getAgen().setMsag_id("021052"); //kode penutup : bank sinarmas
						}else{
							detiledit.getDatausulan().setLsbs_id(164); //lsbs : slink 
							detiledit.getDatausulan().setLsdbs_number(1); //lsdbs : slink
							detiledit.getDatausulan().setKodeproduk("164");
							detiledit.getDatausulan().setLsdbs_name("STABLE LINK(I)");
							detiledit.getDatausulan().setPlan("164~X1");
							
						}
						
						detiledit.getDatausulan().setCara_premi(1); //ISI TOTAL PREMI DAN PILIH KOMBINASI
						
						//total premi = premi + bunga rollover terakhir
						Powersave roTerakhir = uwManager.selectRolloverPowersaveTerakhir(kopiSPAJ);
						if(roTerakhir!=null){
							if(roTerakhir.mpr_bayar_prm.intValue() == 1){
								detiledit.getDatausulan().setTotal_premi_kombinasi(roTerakhir.mpr_deposit);
							}else{
								detiledit.getDatausulan().setTotal_premi_kombinasi(roTerakhir.mpr_deposit + roTerakhir.mpr_interest);
							}
						}
	
						//data powersave
						Powersave data_pwr = (Powersave) this.elionsManager.select_powersaver(kopiSPAJ);
						if(data_pwr != null){
							if(data_pwr.getFlag_rider() != null){
								if(data_pwr.getFlag_rider()==1){//ada rider
									Map mappingRiderSave = uwManager.select_rider_save(spaj);
									BigDecimal mpr_cb_rider = (BigDecimal) mappingRiderSave.get("MRS_RIDER_CB");
									BigDecimal lscb_id_rider = (BigDecimal) mappingRiderSave.get("LSCB_ID_RIDER");
									data_pwr.setMpr_cara_bayar_rider(mpr_cb_rider.intValue());
									detiledit.getDatausulan().setLscb_id_rider(lscb_id_rider.intValue());
								}
							}
							detiledit.setPowersave(data_pwr);
							detiledit.getPowersave().setMps_rate(0.);
							detiledit.getPowersave().setMpr_note("");
							detiledit.getPowersave().setBegdate_topup(roTerakhir.mpr_mature_date);
							detiledit.getPowersave().setBegdateTerbaru(roTerakhir.mpr_mature_date);
							detiledit.getPowersave().setMsl_spaj_lama(kopiSPAJ);
						}
					}
				}
				
				//DATA UNTUK TESTING
				/*
				//pemegang, tertanggung
				detiledit.getPemegang().setMspo_nasabah_dcif("12345");
				detiledit.getPemegang().setMcl_first("JON TOR");
				detiledit.getPemegang().setMspe_mother("MAMAK JONI");
				detiledit.getPemegang().setMspe_no_identity("09.1234.126578.9874");
				detiledit.getPemegang().setMspe_date_birth(defaultDateFormat.parse("09/11/1981"));
				detiledit.getPemegang().setMspe_place_birth("JAKARTA");
				detiledit.getPemegang().setAlamat_rumah("GANG BURUNG");
				detiledit.getPemegang().setKota_rumah("BATAVIA");
				detiledit.getPemegang().setArea_code_rumah("021");
				detiledit.getPemegang().setTelpon_rumah("580");
				detiledit.getAddressbilling().setTagih("1");
				detiledit.getAddressbilling().setMsap_address(detiledit.getPemegang().getAlamat_rumah());
				detiledit.getAddressbilling().setKota(detiledit.getPemegang().getKota_rumah());
				detiledit.getAddressbilling().setKota_tgh(detiledit.getPemegang().getKota_rumah());
				detiledit.getPemegang().setTujuana("MU");
				detiledit.getPemegang().setDanaa("RB");
				detiledit.getPemegang().setDanaa2("M");
				detiledit.getPemegang().setIndustria("P");
				detiledit.getPemegang().setKerjaa("G");
				detiledit.getPemegang().setLsre_id(1);
				*/
				//END OF DATA UNTUK TESTING
				
			}
			
			detiledit.getPemegang().setLus_id(Integer.parseInt(currentUser.getLus_id()));
			Map data_valid = (HashMap) this.elionsManager.select_validbank(Integer.parseInt(currentUser.getLus_id()));
			if (data_valid != null){
				detiledit.getPemegang().setCab_bank((String)data_valid.get("CAB_BANK"));
				detiledit.getPemegang().setJn_bank((Integer) data_valid.get("JN_BANK"));
				detiledit.getPemegang().setPassword1((String)data_valid.get("PASS1"));
				detiledit.getPemegang().setPassword2((String)data_valid.get("PASS2"));
			}else{
				detiledit.getPemegang().setCab_bank("");
				detiledit.getPemegang().setJn_bank(-1);
				detiledit.getPemegang().setPassword1("");
				detiledit.getPemegang().setPassword2("");
			}

			Integer jumlah_rate;
			Integer jumlah_rate_bulanan;
			Integer jumlah_rate_stabil;

			//APABILA BANK SINARMAS
			if (detiledit.getPemegang().getJn_bank().intValue() == 2){
				jumlah_rate = this.elionsManager.selectcount_rate(2); //simas prima
				//jumlah_rate_bulanan = this.elionsManager.selectcount_rate(5); //simas prima manfaat bulanan
				jumlah_rate_stabil = this.elionsManager.selectcount_rate(13); //simas stabil link
				
				//APABILA KOSONG, EMAIL!
				if(jumlah_rate.intValue() == 0){// || jumlah_rate_bulanan == 0) {
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
					Date tgl = elionsManager.selectSysdate();
					this.email.send(false, props.getProperty("admin.ajsjava"),
							new String[] {"sandy@sinarmasmsiglife.co.id"}, 
							null, null,
							"[System Warning] Rate Powersave/Stable Link Per Tanggal " + df.format(tgl), 
							"Kepada Yth.\n"+
							"Bagian Investment di tempat.\n\n"+
							"Mohon bantuannya untuk melakukan proses upload rate per tanggal " + df.format(tgl) +" untuk produk Simas Prima / Simas Stabil Link, karena ada Inputan SPAJ di Cabang Bank Sinarmas."+
							"\n\nTerima kasih.", null);
				}
				
				detiledit.getPemegang().setRate_1(jumlah_rate);
				//detiledit.getPemegang().setRate_2(jumlah_rate_bulanan);
			//APABILA LAINNYA
			}else {
				jumlah_rate = 0;
				jumlah_rate_bulanan = 0;
				detiledit.getPemegang().setRate_1(jumlah_rate);
				detiledit.getPemegang().setRate_2(jumlah_rate_bulanan);
			}
			
		}
		detiledit.setCurrentUser(currentUser);
		
		return detiledit;
	}

	protected void onBind(HttpServletRequest request, Object cmd, BindException errors) throws Exception {
		
		logger.debug("EditBacController : onBind");
		
		NumberFormat nf = NumberFormat.getNumberInstance(); 
		f_replace konteks = new f_replace();
		int halaman = ServletRequestUtils.getIntParameter(request, "hal", 1);
		
		request.getSession().setAttribute("dataInputSpaj", (Cmdeditbac) cmd);
		
		/** HALAMAN PEMEGANG */
		if (halaman==1){
			Cmdeditbac a = (Cmdeditbac) cmd;
			if (a.getDatausulan().getJmlrider() == null) {
				a.getDatausulan().setJmlrider(0);
			}
			
		/** HALAMAN TERTANGGUNG */
		}else if (halaman==2){
			//rider
			int jmlh_rider = Integer.parseInt(request.getParameter("mn"));
			
			String kode_rider;
			Integer kd_rd;
			String number_rider;
			Integer nm_rd = 0;
			String unit ;
			Integer unt = 0;
			String klas;
			Integer kls = 0;
			String tsi;
			Double ts = 0.;
			String premi;
			Double prm = 0.;
			Integer in = 0;
			String ins;
			String beg_date;
			String end_date;
			String end_pay;
			String rate;
			Double rt = 0.;
			String insured;
			Integer isr = 0;
			String bisnis;
			int letak = 0;
			List<Datarider> b = new ArrayList<Datarider>();
			Cmdeditbac a = (Cmdeditbac) cmd;
			a.getDatausulan().setJmlrider(jmlh_rider);
				
			if (jmlh_rider>0){
				for (int k = 1 ;k <= jmlh_rider; k++)	{
					bisnis = request.getParameter("ride.plan_rider1"+k);
					if (bisnis == null){
						bisnis="0~X0";
					}
					letak = 0;
					letak = bisnis.indexOf("X");
					kode_rider = bisnis.substring(0,letak-1);
					kd_rd = Integer.parseInt(kode_rider);
					number_rider = bisnis.substring(letak+1,bisnis.length());
					nm_rd = Integer.parseInt(number_rider);
					unit = request.getParameter("ride.mspr_unit"+k);
					if (unit == null){
						unit ="";
					}
					if (unit.trim().length()==0){
						unit="0";
					}else{
						boolean cekk1= f_validasi.f_validasi_numerik(unit);	
						if (cekk1==false){
							unit = "0";
						}
					}
					unt = nf.parse(unit).intValue();
					klas = request.getParameter("ride.mspr_class"+k);
					if (klas == null){
						klas ="";
					}
					if (klas.trim().length()==0){
						klas="0";
					}else{
						boolean cekk1 = f_validasi.f_validasi_numerik(klas);
						if (cekk1==false){
							klas="0";
						}
					}
					kls = nf.parse(klas).intValue();
					tsi = request.getParameter("ride.mspr_tsi"+k);
					if (tsi == null){
						tsi ="";
					}
					if (tsi.trim().length()==0){
						tsi="0";
					}
					ts = nf.parse(tsi).doubleValue();
					premi = request.getParameter("ride.mspr_premium"+k);
					if (premi == null){
						premi = "";
					}
					if (premi.trim().length()==0){
						premi= "0";
					}
					prm = nf.parse(premi).doubleValue();
					ins = request.getParameter("ride.mspr_ins_period"+k);
					if (ins == null){
						ins = "";
					}
					if (ins.trim().length()==0){
						ins="0";
					}
					in = nf.parse(ins).intValue();
					beg_date = request.getParameter("mspr_beg_date"+k);
					Date begin_date;	
					if (beg_date == null){
						beg_date ="";
					}
					if (beg_date.trim().length()==0) {
						begin_date=null;
					}else{
						begin_date=defaultDateFormat.parse(beg_date);
					}
					end_date = request.getParameter("mspr_end_date"+k);
					Date ending_date;
					if (end_date == null){
						end_date = "";
					}
					if (end_date.trim().length()==0){
						ending_date=null;
					}else{
						ending_date=defaultDateFormat.parse(end_date);
					}
					end_pay = request.getParameter("mspr_end_pay"+k);
					Date ending_pay;
					if (end_pay == null){
						end_pay = "";
					}
					if (end_pay.trim().length()==0){
						ending_pay=null;
					}else{
						ending_pay=defaultDateFormat.parse(end_pay);
					}

					rate = request.getParameter("ride.mspr_rate"+k);
					if (rate == null){
						rate = "";
					}
					if (rate.trim().length()==0){
						rate="0";
					}
					rt = nf.parse(rate).doubleValue();
					
					insured = request.getParameter("ride.mspr_tt1"+k);
					if (insured == null)	{
						insured = "";
					}
					if (insured.trim().length()==0){
						insured="0";
					}
					isr = nf.parse(insured).intValue();
					
					String flag_include= "0";
					flag_include = request.getParameter("ride.flag_include"+k);
					if (flag_include == null){
						flag_include ="";
					}
					if (flag_include == null){
						flag_include = "0";
					}
	
					Datarider rd = new Datarider();
					rd.setLsbs_id(kd_rd);
					rd.setLsdbs_number(nm_rd);
					rd.setPlan_rider(bisnis);
					rd.setMspr_unit(unt);
					rd.setMspr_class(kls);
					rd.setMspr_ins_period(in);
					rd.setMspr_tt(isr);
					rd.setMspr_rate(rt);
					rd.setMspr_beg_date(begin_date);
					rd.setMspr_end_date(ending_date);
					rd.setMspr_end_pay(ending_pay);
					rd.setMspr_tsi(ts);
					rd.setMspr_premium(prm);
					//rd.setFlag_include(Integer.parseInt(flag_include));
					b.add(rd);
				}
			}
			
			a.getDatausulan().setDaftaRider(b);
			if (a.getDatausulan().getJml_benef()==null){
				a.getDatausulan().setJml_benef(0);
			}

		/** HALAMAN DATA USULAN */
		}else if (halaman==3){
			
			Cmdeditbac a = (Cmdeditbac) cmd;
			int jmlh_penerima = Integer.parseInt(request.getParameter("jmlpenerima"));
				
			String msaw_first;
			String tgllhr;
			String blnhr;
			String thnhr;
			String lsre_id;
			String msaw_persen;
			List<Benefeciary> b = new ArrayList<Benefeciary>();
			a.getDatausulan().setJml_benef(jmlh_penerima);
			
			//TAMBAHAN dari BERTHO.. untuk mengatasi ahli waris yang hilang bila next kopy spaj 05/05/2009
			if(a.getDatausulan().getDaftabenef()!=null){
				if(jmlh_penerima==0 && !a.getDatausulan().getDaftabenef().isEmpty()){
					jmlh_penerima=a.getDatausulan().getDaftabenef().size();
				}
			}
			if (jmlh_penerima>0){
				for (int k = 1 ;k <= jmlh_penerima; k++){
					msaw_first = request.getParameter("benef.msaw_first"+k);
					tgllhr = request.getParameter("tgllhr"+k);
					blnhr = request.getParameter("blnhr"+k);
					thnhr = request.getParameter("thnhr"+k);
					lsre_id =request.getParameter("benef.lsre_id"+k);
					String tanggal_lahir = null;
					if (msaw_first==null){
						msaw_first="";
					}
					
					tanggal_lahir = FormatString.rpad("0",tgllhr,2)+"/"+FormatString.rpad("0",blnhr,2)+"/"+thnhr;
					if ((tgllhr.trim().length()==0)||(blnhr.trim().length()==0)||(thnhr.trim().length()==0)){
						tanggal_lahir=null;
					}else{
						boolean cekk1= f_validasi.f_validasi_numerik(tgllhr);	
						boolean cekk2= f_validasi.f_validasi_numerik(blnhr);
						boolean cekk3= f_validasi.f_validasi_numerik(thnhr);		
						if ((cekk1==false) ||(cekk2==false) || (cekk3==false)){
							tanggal_lahir=null;
						}
					}
						
					Date tanggallahir = null;
					if (tanggal_lahir != null){
						tanggallahir = defaultDateFormat.parse(tanggal_lahir);
					}
					
					if (lsre_id==null){
						lsre_id="1";
					}
					msaw_persen=request.getParameter("benef.msaw_persen"+k);
					if (msaw_persen.trim().length()==0){
						msaw_persen="0";
					}else{
						msaw_persen=konteks.f_replace_persen(msaw_persen);

						boolean cekk1 = f_validasi.f_validasi_numerik1(msaw_persen);
						if (cekk1 == false){
							msaw_persen="0";
						}
					}
	
					Benefeciary bf = new Benefeciary();
					bf.setMsaw_first(msaw_first);
					bf.setMsaw_birth(tanggallahir);
					bf.setLsre_id(Integer.parseInt(lsre_id));
					bf.setMsaw_persen(Double.parseDouble(msaw_persen));
					b.add(bf);
				}
			}
			
			
				a.getDatausulan().setDaftabenef(b);
		
			
			String nama_produk="produk_asuransi.n_prod_"+FormatString.rpad("0", a.getDatausulan().getLsbs_id().toString(), 2);
			
			/**
			 * GABUNGKAN NO REKENING YANG DISPLIT
			 * bertho
			 */
			String mrc_no="";
			for (int i = 0; i < a.getRekening_client().getMrc_no_ac_split().length; i++) {
				if(a.getRekening_client().getMrc_no_ac_split()[i]!=null){
					mrc_no=mrc_no+a.getRekening_client().getMrc_no_ac_split()[i];
				}
			}
			a.getRekening_client().setMrc_no_ac(mrc_no);
			logger.info("MRC NO = "+a.getRekening_client().getMrc_no_ac());
			
			String mar_acc_no="";
			for (int i = 0; i < a.getAccount_recur().getMar_acc_no_split().length; i++) {
				if(a.getAccount_recur().getMar_acc_no_split()[i]!=null){
					mar_acc_no=mar_acc_no+a.getAccount_recur().getMar_acc_no_split()[i];
				}
			}
			a.getAccount_recur().setMar_acc_no(mar_acc_no);
			logger.info("MAR AC NO = "+a.getAccount_recur().getMar_acc_no());
			
			
			// DETAIL INVESTASI HALAMAN BARU
//			try{
//				Class aClass = Class.forName( nama_produk );
//				n_prod produk = (n_prod) aClass.newInstance();
//				a.getInvestasiutama().setDaftarinvestasi(this.elionsManager.selectDetailInvestasi(produk.kode_flag));
//				a.getInvestasiutama().setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(produk.kode_flag));				
//			} catch (ClassNotFoundException e){
//				logger.error("ERROR :", e);
//			} catch (InstantiationException e) {
//				logger.error("ERROR :", e);
//			} catch (IllegalAccessException e) {
//				logger.error("ERROR :", e);
//			}
		
			Cmdeditbac detiledit = (Cmdeditbac) cmd;
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			
			//khusus produk HCP sekuritas
			if(detiledit.getDatausulan().getLsbs_id().intValue() == 171 && detiledit.getDatausulan().getLsdbs_number().intValue() == 2){
				detiledit.getDatausulan().setTipeproduk(12); //sinarmas sekuritas
				detiledit.getAgen().setMsag_id("016374"); //kode penutup : SIMAS SEKURITAS
				detiledit.getPemegang().setMspo_ao("016374");
				if(currentUser.getMsag_id_ao() != null) {
					if(!currentUser.getMsag_id_ao().equals("")) {
						detiledit.getPemegang().setMspo_ao(currentUser.getMsag_id_ao());
					}
				}
			}							
		if(currentUser.getJn_bank().intValue() == 2){
				if(detiledit.getDatausulan().getKodeproduk().equals("164")
						&& detiledit.getDatausulan().getLsdbs_number()==2){
					detiledit.getAgen().setMsag_id("021052"); //kode penutup : SIMAS STABIL LINK					
				}
				if(detiledit.getDatausulan().getKodeproduk().equals("142")
						&& detiledit.getDatausulan().getLsdbs_number()==2){
					detiledit.getAgen().setMsag_id("016409"); //kode penutup : SIMAS PRIMA					
				}
			}
		}else if (halaman==4){
			Cmdeditbac detiledit = (Cmdeditbac) cmd;
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			if(currentUser.getJn_bank().intValue() == 2&!(currentUser.getCab_bank()==null?"":currentUser.getCab_bank()).trim().equals("SSS")){
				if (detiledit.getDatausulan().getKodeproduk().equals("164")||detiledit.getDatausulan().getKodeproduk().equals("142")){
					if(!detiledit.getAgen().getMsag_id().equals("021052") 
							&& detiledit.getDatausulan().getKodeproduk().equals("164")
							&& detiledit.getDatausulan().getLsdbs_number()==2){
						errors.reject("", "KODE PENUTUP harus diisi dengan KODE AGEN 021052");
					}
					if(!detiledit.getAgen().getMsag_id().equals("016409") 
							&& detiledit.getDatausulan().getKodeproduk().equals("142")
							&& detiledit.getDatausulan().getLsdbs_number()==2){
						errors.reject("", "KODE PENUTUP harus diisi dengan KODE AGEN 016409");
					}
				}
			}	
		}

	}
	
	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object cmd, BindException err)
			throws Exception {
		logger.debug("EditBacController : processCancel");
		return new ModelAndView("bac/editpemegang","cmd",this.formBackingObject(request));
	}

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object cmd, BindException err)
			throws Exception {
		
		logger.debug("EditBacController : processFinish");
		
		Cmdeditbac detiledit = (Cmdeditbac)cmd;
			
			String spaj="";
			String status=detiledit.getPemegang().getStatus();
			String keterangan = detiledit.getDatausulan().getKeterangan_fund();
			//detiledit.getDatausulan().setIndeks_validasi(1);
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			
			String status_submit = detiledit.getDatausulan().getStatus_submit();
			if (status_submit == null)
			{
				status_submit="";
			}
			if (status.equalsIgnoreCase("edit"))
			{
				if (status_submit.equalsIgnoreCase("ulang"))
				{
					status="input";
					detiledit.getPemegang().setStatus(status);
				}
			}
			
			if (status.equalsIgnoreCase("input"))
			{
				detiledit=this.elionsManager.savingspaj(cmd,err,"input",currentUser);
				spaj = detiledit.getPemegang().getReg_spaj();
//				uwDao.updateMstInsuredTglAdmin(spaj, insuredNo, tgl, show);
//				elionsManager.prosesEditTanggalSpajAdmin(spaj,1,tanggalTerimaAdmin,keterangan);
////				params.put("success", "Berhasil Update Tanggal Terima Admin");
			}else{
				/**
				 * @author HEMILDA
				 * edit
				 * untuk yang sudah aksep tidak bisa diedit lagi
				 */
				if(detiledit.getFlag_gmanual_spaj()<=0){
					Integer status_polis = this.elionsManager.selectPositionSpaj(detiledit.getPemegang().getReg_spaj());
					//if(status_polis==null) status_polis = 5;
					if (status_polis.intValue() != 5)
					{
						detiledit=this.elionsManager.savingspaj(cmd,err,"edit",currentUser);
						spaj = detiledit.getPemegang().getReg_spaj();
						if(spaj.equals("")) {
							request.getSession().setAttribute("dataInputSpaj", detiledit);
						}
					}else{
						err.rejectValue("pemegang.reg_spaj","","Spaj in sudah diaksep, tidak bisa diedit.");
					}
				}else if(detiledit.getFlag_gmanual_spaj()>=1){
					detiledit=this.elionsManager.savingspaj(cmd,err,"input",currentUser);
					spaj = detiledit.getPemegang().getReg_spaj();
					if(spaj.equals("")) {
						request.getSession().setAttribute("dataInputSpaj", detiledit);
					}
				}
				
			}
		
			keterangan = detiledit.getDatausulan().getKeterangan_fund();
			Integer status_polis = detiledit.getHistory().getStatus_polis();
			Integer lspd_id = detiledit.getDatausulan().getLspd_id();
			Integer kodebisnis = detiledit.getDatausulan().getLsbs_id();
			Integer jml_peserta = detiledit.getDatausulan().getJml_peserta();
			
			Map<String, Comparable> m =new HashMap<String, Comparable>();
			m.put("nomorspaj",spaj);
			m.put("status",status);
			m.put("keterangan",keterangan);
			m.put("status_polis",status_polis);
			m.put("lspd_id",lspd_id);
			m.put("kodebisnis", kodebisnis);
			m.put("jml_peserta", jml_peserta);
			m.put("status_submit", status_submit);
			
			if(logger.isDebugEnabled())logger.debug("Input SPAJ: " + spaj + " dengan waktu " + ((System.currentTimeMillis()-this.accessTime)/1000) + " detik.");

			return new ModelAndView("bac/editsubmit",m);

	}

}