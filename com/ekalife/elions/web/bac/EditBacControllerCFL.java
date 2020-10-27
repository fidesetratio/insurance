package com.ekalife.elions.web.bac;

import java.io.IOException;
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
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.AddressNew;
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
import com.ekalife.elions.model.MedQuest;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Personal;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.bac.support.Editbacvalidatorcfl;
import com.ekalife.utils.Common;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_replace;
import com.ekalife.utils.f_validasi;
import com.ekalife.utils.parent.ParentWizardController;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.spring.util.Email;
import produk_asuransi.n_prod;

/**
 * @author HEMILDA
 * Controller untuk input bac
 */
public class EditBacControllerCFL extends ParentWizardController {
	
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
			Editbacvalidatorcfl validator = (Editbacvalidatorcfl) this.getValidator();
			//logger.info(page);
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
//				F_CopyObjectToFile.serializable(cmd,halaman.getPathFileTemp()+"\\bac.txt");
				
				if(page==0) {
					validator.validatepp(cmd, err);
				}else if(page==1 && index > page){ 
					validator.validatettg(cmd, err);
				}else if(page==2 && index > page){ 
					validator.validateddu(cmd, err);
				}else if(page==3 && index > page){ 
					validator.validateinvestasi(cmd, err);
				}else if(page==4 && index > page){ 
					validator.validatequestionare(cmd, err);
				}else if(page==5 && index > page){
					Cmdeditbac detiledit = (Cmdeditbac)cmd;
				}
			}else{
				if(page==0){ 
					validator.validatepp(cmd, err);
				}else if(page==1){ 
					validator.validatettg(cmd, err);
				}else if(page==2){ 
					validator.validateddu(cmd, err);
				}else if(page==3){ 
					validator.validateinvestasi(cmd, err);
				}else if(page==4){ 
					validator.validatequestionare(cmd, err);
				}else if (page == 5){
					Cmdeditbac detiledit = (Cmdeditbac)cmd;
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
						
			refData.put("tanggalTerimaAdmin",  sysdate);
			refData.put("listHH", new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23});
			refData.put("listMM", new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59});
			refData.put("hh", hh);
			refData.put("mm", mm);
			
			editBac.getDatausulan().setJenis_pemegang_polis(0);
			
			if (editBac.getPemegang().getKota_rumah()==null ){
				editBac.getPemegang().setKota_rumah("");
			}
			
			if (editBac.getPemegang().getKota_kantor()==null ){
				editBac.getPemegang().setKota_kantor("");
			}
			
			if (editBac.getAddressbilling().getKota_tgh()==null ){
				editBac.getAddressbilling().setKota_tgh("");				
			}
			
			if(editBac.getDatausulan().getKurs_premi()==null){
				editBac.getDatausulan().setKurs_premi("");
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
			refData.put("select_aset",DroplistManager.getInstance().get("ASET.xml","",request));
			refData.put("select_dana",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","",request));

			List<DropDown> bidangUsahaList = ((List<DropDown>)elionsManager.selectBidangUsaha(2));
			bidangUsahaList.addAll((List<DropDown>)elionsManager.selectBidangUsaha(1));
			refData.put("bidangUsaha", bidangUsahaList);
			
			refData.put("select_relasi",DroplistManager.getInstance().get("RELATION.xml","",request));
			
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

					editBac.getDatausulan().setFlag_bao(0);
					
					produk.of_set_bisnis_no(number_produk);
					produk.ii_bisnis_no=(number_produk);
					produk.ii_bisnis_id=(kode_produk);
					
					produk.of_set_kurs(editBac.getDatausulan().getKurs_premi());
					int kode_flag = produk.kode_flag;
					editBac.getDatausulan().setKode_flag(kode_flag);
					editBac.getDatausulan().setIsBungaSimponi(0);	
					editBac.getDatausulan().setIsBonusTahapan(0);
					
					editBac.getDatausulan().setFlag_account(produk.flag_account);
					editBac.getDatausulan().setFlag_as(produk.flag_as);
					editBac.getDatausulan().setFlag_worksite(produk.flag_worksite);
					editBac.getDatausulan().setFlag_endowment(produk.flag_endowment);
					editBac.getDatausulan().setFlag_horison(produk.flag_horison);
					
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
			refData.put("select_dana_badan_usaha",DroplistManager.getInstance().get("SUMBER_PENDANAAN_BADAN_USAHA.xml","",request));
			
			// REFDATA UNTUK HALAMAN BARU
//			refData.put("select_identitas", elionsManager.selectDropDown("eka.lst_identity", "lside_id", "lside_name", "lside_id", null));
//			refData.put("select_negara", elionsManager.selectDropDown("eka.lst_negara", "lsne_id", "lsne_note", "lsne_id", null));
//			refData.put("select_agama", elionsManager.selectDropDown("eka.lst_agama", "lsag_id", "lsag_name", "lsag_id", null));
//			refData.put("select_pendidikan", elionsManager.selectDropDown("eka.lst_education", "lsed_id", "lsed_name", "lsed_id", null));
			
			refData.put("select_relasi",DroplistManager.getInstance().get("RELATION.xml","",request));

		/** Halaman 3 : Data Usulan Asuransi */
		}else if (page ==2){
			refData = new HashMap<String, Object>();
			
			refData.put("listprodukutama", this.uwManager.select_produkutamacfl());

			refData.put("select_insrider",DroplistManager.getInstance().get("INSRIDER.xml","ID",request));
			refData.put("select_rider",DroplistManager.getInstance().get("PLANRIDER.xml","BISNIS_ID",request));
			
			Cmdeditbac editBac = (Cmdeditbac) cmd;
			if(editBac.getDatausulan().getLsbs_id() != null && editBac.getDatausulan().getLsbs_id() !=0) {
				if(editBac.getDatausulan().getLsdbs_number()!=null){
					refData.put("select_rider", elionsManager.list_rider(editBac.getDatausulan().getLsbs_id().toString(), editBac.getDatausulan().getLsdbs_number().toString()));
				}		
			}
			
			refData.put("select_kombinasi",DroplistManager.getInstance().get("KOMBINASI_PREMI.XML","ID",request));
			
		/** Halaman 4 : Detail Investasi */
		}else if (page==3){
			refData = new HashMap<String, Object>();
			refData.put("select_jns_top_up",DroplistManager.getInstance().get("TOPUP.xml","ID",request));	
			refData.put("select_jenis_nasabah",DroplistManager.getInstance().get("jenis_nasabah.xml","ID",request));
			refData.put("select_jenis_tabungan",DroplistManager.getInstance().get("jenis_tabungan.xml","ID",request));
			refData.put("select_jangka_invest",DroplistManager.getInstance().get("JNSTOPUP.xml","ID",request));
			refData.put("select_autodebet",DroplistManager.getInstance().get("AUTODEBET.xml","ID",request));
			
			Cmdeditbac editBac = (Cmdeditbac) cmd;
			Integer kode_produk = editBac.getDatausulan().getLsbs_id();
			
			//bila keluarga stable save
			if(products.stableSavePremiBulanan(kode_produk.toString()) || products.stableSave(kode_produk, editBac.getDatausulan().getLsdbs_number()) || products.progressiveLink(kode_produk.toString())){
				if(products.stableSavePremiBulanan(kode_produk.toString()) || products.progressiveLink(kode_produk.toString())){
					refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER_STABSAVE_PREMI.xml","ID",request));
				}else{
					if(products.stableSave(kode_produk, editBac.getDatausulan().getLsdbs_number())){
						if(editBac.getDatausulan().getLsdbs_number()==5){
							if(editBac.getDatausulan().getFlag_bulanan()==1){
								refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER_SUCC_BULANAN.xml","ID",request));
							}else if(editBac.getDatausulan().getFlag_bulanan()==0){
								refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER_SUCC_NON_BULANAN.xml","ID",request));
							}
						}else{
							//refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER_SUCC_NON_BULANAN.xml","ID",request));
							refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER_STABSAVE.xml","ID",request));
						}
					}else{
						refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER_STABSAVE.xml","ID",request));
					}
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
		
		/** Halaman 4 : questionare*/
		}else if (page==4){	
			
			
		/** Halaman 5 : Konfirmasi */
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
			
			refData.put("jenis_id_pic", Common.searchXml(res+"IDENTITY.xml", "ID", "TDPENGENAL", editBac.getContactPerson().getLside_id()));
			refData.put("negara_pic", Common.searchXml(res+"WARGANEGARA.xml", "ID", "NEGARA", editBac.getContactPerson().getLsne_id()));
			refData.put("status_pic", Common.searchXml(res+"MARITAL.xml", "ID", "MARITAL", editBac.getContactPerson().getSts_mrt()));
			refData.put("agama_pic", Common.searchXml(res+"AGAMA.xml", "ID", "AGAMA", editBac.getContactPerson().getLsag_id()));
			refData.put("pendidikan_pic", Common.searchXml(res+"PENDIDIKAN.xml", "ID", "PENDIDIKAN", editBac.getContactPerson().getLsed_id()));

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

		Cmdeditbac detiledit = new Cmdeditbac();
		
		detiledit.setCurrentUser(currentUser);
		detiledit.setPemegang(new Pemegang());
		detiledit.setAddressbilling(new AddressBilling());
		detiledit.setDatausulan(new Datausulan());
		detiledit.setTertanggung(new Tertanggung());
		InvestasiUtama inv = new InvestasiUtama();
		
		detiledit.setPersonal(new Personal());
		detiledit.setContactPerson(new ContactPerson());
		detiledit.getPersonal().setAddress(new AddressNew());
		//==================================

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
		detiledit.setMedQuest(new MedQuest());
		
		/**=======================================================*/
		/* set default*/
		detiledit.getDatausulan().setJenis_pemegang_polis(0);
		detiledit.getDatausulan().setTipeproduk(15);//click for life
		detiledit.getDatausulan().setMste_flag_investasi(0);
		detiledit.getDatausulan().setMste_medical(0);
		detiledit.getAgen().setMsag_id("000000");
		detiledit.getPemegang().setMspo_ao("000000");
		
		
		/* untuk testing*/
//		detiledit.getPemegang().setMcl_first("TEST");
//		detiledit.getPemegang().setAlamat_rumah("DI MATA LANGIT");
//		detiledit.getPemegang().setKd_pos_rumah("123456");
//		detiledit.getPemegang().setKota_rumah("JAKARTA");
//		detiledit.getPemegang().setMspe_mother("MOM TEST");
//		detiledit.getPemegang().setNo_hp("081800112233");
//		detiledit.getPemegang().setLside_id(1);
//		detiledit.getPemegang().setLside_name("8888888888");
//		detiledit.getPemegang().setMspe_place_birth("JAKARTA");
		
//		detiledit.setPathFileTemp(props.getProperty("temp.fileinput.user")+"\\"+detiledit.getCurrentUser().getLus_id());
//		
//		File pathFile = new File(detiledit.getPathFileTemp());
//		File file = new File(detiledit.getPathFileTemp()+"\\bac.txt");
//		if(!pathFile.exists()) {
//			pathFile.mkdirs();
//        }
//		
//		String refreshObject = ServletRequestUtils.getStringParameter(request, "refreshObject", "");
//		if(refreshObject.equals("")){
//			refreshObject = "0";
//		}
//		
//		if(file.exists() && !refreshObject.equals("2")){
//			Cmdeditbac tempdetiledit = detiledit;
//			Object cmd = null;
//			cmd = F_CopyObjectToFile.unserializeable(cmd, detiledit.getPathFileTemp()+"\\bac.txt");
//			if(refreshObject.equals("0")){
//				refreshObject="2";
//			}
//			detiledit= (Cmdeditbac)cmd;
//			detiledit.setFileConfirm("Perhatian, Sebelumnya pernah dilakukan penginputan untuk nasabah " +detiledit.getPemegang().getMcl_first()+ ". Apakah Anda ingin meneruskan penginputannya? tekan YES apabila mau meneruskan, NO apabila menginput data baru");
//			
//			detiledit.setRefreshObject(Integer.parseInt(refreshObject));
//			if(detiledit.getRefreshObject()==1){//1 refresh object
//				file.delete();
//				detiledit.setFileConfirm("");
//				detiledit = tempdetiledit;
//			}
//			
//		}
		
		return detiledit;
	}

	protected void onBind(HttpServletRequest request, Object cmd, BindException errors) throws Exception {
		
		logger.debug("EditBacController : onBind");
		
		NumberFormat nf = NumberFormat.getNumberInstance(); 
		f_replace konteks = new f_replace();
		int halaman = ServletRequestUtils.getIntParameter(request, "hal", 1);
		
		request.getSession().setAttribute("dataInputSpaj", (Cmdeditbac) cmd);
		
		// TAMBAHAN UNTUK PERUSAHAAN
		if (halaman==0){
			Cmdeditbac a = (Cmdeditbac) cmd;
			if (a.getDatausulan().getJmlrider() == null) {
				a.getDatausulan().setJmlrider(0);
			}
			a.getDatausulan().setJenis_pemegang_polis(0);
		}
		//==================================
		
		/** HALAMAN PEMEGANG */
		if (halaman==1){
			Cmdeditbac a = (Cmdeditbac) cmd;
			if (a.getDatausulan().getJmlrider() == null) {
				a.getDatausulan().setJmlrider(0);
			}
			a.getDatausulan().setJenis_pemegang_polis(0);
			
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
			String msaw_sex;
			List<Benefeciary> b = new ArrayList<Benefeciary>();
			a.getDatausulan().setJml_benef(jmlh_penerima);
			
			//TAMBAHAN dari BERTHO.. untuk mengatasi ahli waris yang hilang bila next kopy spaj 05/05/2009
			if(a.getDatausulan().getDaftabenef()!=null && !a.getPemegang().getStatus().equalsIgnoreCase("edit") ){
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
					msaw_sex =request.getParameter("benef.msaw_sex"+k);
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
					bf.setMsaw_sex(Integer.parseInt(msaw_sex));
					b.add(bf);
				}
			}
			
			
				a.getDatausulan().setDaftabenef(b);
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			
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
			
			String mar_acc_no="";
			for (int i = 0; i < a.getAccount_recur().getMar_acc_no_split().length; i++) {
				if(a.getAccount_recur().getMar_acc_no_split()[i]!=null){
					mar_acc_no=mar_acc_no+a.getAccount_recur().getMar_acc_no_split()[i];
				}
			}
			a.getAccount_recur().setMar_acc_no(mar_acc_no);
			
			
			Cmdeditbac detiledit = (Cmdeditbac) cmd;
		
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
				detiledit=this.uwManager.savingspaj(cmd,err,"input",currentUser);
				spaj = detiledit.getPemegang().getReg_spaj();
//				if(!spaj.equals("")) {
//					File file = new File(detiledit.getPathFileTemp()+"\\bac.txt");
//					if(file.exists()){
//						file.delete();
//						detiledit.setFileConfirm("");
//					}
//				}
				
			}else{
				/**
				 * @author HEMILDA
				 * edit
				 * untuk yang sudah aksep tidak bisa diedit lagi
				 */
				Integer status_polis = this.elionsManager.selectPositionSpaj(detiledit.getPemegang().getReg_spaj());
				//if(status_polis==null) status_polis = 5;
				if (status_polis.intValue() != 5)
				{
					detiledit=this.elionsManager.savingspaj(cmd,err,"edit",currentUser);
					spaj = detiledit.getPemegang().getReg_spaj();
					if(spaj.equals("")) {
						request.getSession().setAttribute("dataInputSpaj", detiledit);
					}else{
//						File file = new File(detiledit.getPathFileTemp()+"\\bac.txt");
//						if(file.exists()){
//							file.delete();
//							detiledit.setFileConfirm("");
//						}
					}
				}else{
					err.rejectValue("pemegang.reg_spaj","","Spaj ini sudah diaksep, tidak bisa diedit.");
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