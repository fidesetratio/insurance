package com.ekalife.elions.web.bac;

import java.io.IOException;
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
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.History;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.f_validasi;
import com.ekalife.utils.parent.ParentWizardController;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.spring.util.Email;

/**
 *
 */
public class EditBacSimpleController extends ParentWizardController {
	
	private long accessTime;
	protected final Log logger = LogFactory.getLog(getClass());
	private Email email;
	
	public void setEmail(Email email) {
		this.email = email;
	}
	
	protected void initBinder(HttpServletRequest arg0, ServletRequestDataBinder binder) throws Exception {
		logger.debug("EditBacController : initBinder");
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		String lde_id = currentUser.getLde_id();
		String spaj = request.getParameter("showSPAJ");
		
		Cmdeditbac bac = (Cmdeditbac) session.getAttribute("dataInputSpajSimple");
		
		if(request.getParameter("data_baru") != null) {
			session.removeAttribute("dataInputSpajSimple");
			bac = null;
		}
		
		if(bac != null) { // SUMBER DATA : DARI GAGAL SUBMIT SEBELUMNYA
			request.setAttribute("adaData", "adaData");
		}else{
			bac = new Cmdeditbac();
			
			//INIT
			bac.setPemegang(new Pemegang());
			bac.setTertanggung(new Tertanggung());
			bac.setAddressbilling(new AddressBilling());
			bac.setDatausulan(new Datausulan());
			bac.setDatarider(new Datarider());
			bac.setAgen(new Agen());
			bac.setRekening_client(new Rekening_client());
			bac.setAccount_recur(new Account_recur());
			bac.setPowersave(new Powersave());
			bac.setHistory(new History());
			//
			
			/** EDIT SPAJ */
			if(spaj!=null) {
				
				bac.setPemegang((Pemegang)this.elionsManager.selectpp(spaj));
				//bac.getPemegang().setSpv(spv);
				bac.getPemegang().setDaftarKyc(uwManager.selectDaftar_kyc(spaj));
				bac.getPemegang().setDaftarKyc2(uwManager.selectDaftar_kyc2(spaj));
				
				Map sumber_bisnis = elionsManager.select_sumberBisnis(bac.getPemegang().getSumber_id());
				if(sumber_bisnis != null){
					String nama_sumber = (String) sumber_bisnis.get("NAMA_SUMBER");
					bac.getPemegang().setNama_sumber(nama_sumber);
				}
				
				bac.setNo_pb(uwManager.selectNoPB(spaj));
				bac.setAddressbilling((AddressBilling)this.elionsManager.selectAddressBilling(spaj));
				bac.setDatausulan((Datausulan)this.elionsManager.selectDataUsulanutama(spaj));
				bac.getDatausulan().setLde_id(lde_id);
				bac.setTertanggung((Tertanggung)this.elionsManager.selectttg(spaj));
				
				bac.getDatausulan().setJenis_pemegang_polis(0);
				
				bac.setRekening_client(this.elionsManager.select_rek_client(spaj));
				bac.setAccount_recur(this.elionsManager.select_account_recur(spaj));
				bac.setAgen(this.elionsManager.select_detilagen(spaj));
				/**
				 * TAMBAHAN DARI BERTHO UNTUK SPLIT NO REKENING
				 */			
				bac.getRekening_client().setMrc_no_ac_split(FormatString.splitWordToCharacter(bac.getRekening_client().getMrc_no_ac(),21));
				bac.getAccount_recur().setMar_acc_no_split(FormatString.splitWordToCharacter(bac.getAccount_recur().getMar_acc_no(),21));
				////--------------END OF SPLIT REKENING--------------------////////////
			
				String kode_agen = bac.getAgen().getMsag_id();
				String nama_agent="";
				if (kode_agen.equalsIgnoreCase("000000")){
					nama_agent = (String) this.elionsManager.select_agent_temp(spaj);
				}
				bac.getAgen().setMcl_first(nama_agent);
				bac.setEmployee(this.elionsManager.select_detilemployee(spaj));
				
				bac.setHistory(new History());
				bac.getDatausulan().setDaftahcp(this.elionsManager.select_hcp(spaj));
				bac.getDatausulan().setDaftapeserta(this.elionsManager.select_semua_mst_peserta(spaj));
				bac.getDatausulan().setDaftarplus(this.uwManager.select_all_mst_peserta(spaj));
				
				Integer flag_gutri = 0;
				
				bac.getDatausulan().setFlag_gutri(flag_gutri);
				bac.getPemegang().setMste_flag_guthrie(bac.getDatausulan().getFlag_gutri());
				
			/** INSERT SPAJ BARU */
			}else{
				
				//TODO: DAPAT DIMODIFIKASI APABILA DIPERLUKAN UNTUK KASUS TERDAPAT PILIHAN RIDER
				//SET PRODUCT 
				bac.getDatausulan().setLsbs_id(196);//TERM INSURANCE
				bac.getDatausulan().setLsdbs_number(2);//TERM INSURANCE (SMS)
				bac.getDatausulan().setPlan(bac.getDatausulan().getLsbs_id() + "~X" + bac.getDatausulan().getLsdbs_number());//0~X0
				Map product = check_product(bac.getDatausulan().getLsbs_id().toString(),bac.getDatausulan().getLsdbs_number().toString());
				bac.getDatausulan().setMspr_tsi(new Double(product.get("UP").toString()));//TERM INSURANCE
				bac.getDatausulan().setMspr_premium(new Double(product.get("PREMI").toString()));//TERM INSURANCE
				bac.getDatausulan().setMspr_discount(new Double(0));
				//
				
				Date begDate = elionsManager.selectSysdate();
				Date endDate = (Date) begDate.clone();
				endDate.setYear(endDate.getYear()+1);
				endDate.setDate(endDate.getDate()-1);
				
				bac.getDatausulan().setMste_beg_date(begDate);
				bac.getDatausulan().setJenis_pemegang_polis(0);
				
				//TODO: DAPAT DIMODIFIKASI APABILA DIPERLUKAN UNTUK KASUS TERDAPAT PILIHAN RIDER
				//SET RIDER 
				Datarider rider = new Datarider();
				rider.setLsbs_id(834);//HOSPITAL CASH PLAN INCLUDE
				rider.setLsdbs_number(1);//HCP R-250
				rider.setPlan_rider(rider.getLsbs_id() + "~X" + rider.getLsdbs_number());//0~X0
				
				Map sub_product = check_product(rider.getLsbs_id().toString(),rider.getLsdbs_number().toString());
				rider.setMspr_tsi(new Double(sub_product.get("UP").toString()));//HOSPITAL CASH PLAN INCLUDE
				rider.setMspr_premium(new Double(sub_product.get("PREMI").toString()));//HOSPITAL CASH PLAN INCLUDE
				rider.setMspr_beg_date(begDate);
				rider.setMspr_end_date(endDate);
				bac.getDatausulan().setDaftaRider(new ArrayList<Datarider>());
				bac.getDatausulan().getDaftaRider().add(rider);
				//
				
				
				//SET AGEN
				//kata mba inge penutupnya 1 org aja, namanya NATALIA LISTIAWATI (023091)
				String agen_penutup = "023091";
				Map agentMap = elionsManager.selectagenpenutup(agen_penutup);
				String regionid = (String) agentMap.get("REGIONID");
				bac.getAgen().setMsag_id((String) agentMap.get("ID"));
				bac.getAgen().setMcl_first((String) agentMap.get("NAMA"));
				bac.getAgen().setKode_regional(regionid);
				bac.getAgen().setLca_id(regionid.substring(0, 2));
				bac.getAgen().setLwk_id(regionid.substring(2, 4));
				bac.getAgen().setLsrg_id(regionid.substring(4, 6));
				//referensi(tambang emas) agar jenis_ref tidak null
				bac.getAgen().setJenis_ref(2);
				
				bac.getAddressbilling().setRegion(regionid);
				//
			}
		}
		
		return bac;
	}
	
	//TODO:UNTUK PENGEMBANGAN, BAGIAN INI AKAN PERLU MODIFIKASI
	public Cmdeditbac modSet(Cmdeditbac bac, User currentUser, HttpServletRequest request)  throws ServletException,IOException{
		
		f_validasi f_validasi = new f_validasi();
		
		bac.getPemegang().setMcl_first(f_validasi.convert_karakter(bac.getPemegang().getMcl_first()));
		bac.getPemegang().setMspe_mother(f_validasi.convert_karakter(bac.getPemegang().getMspe_mother()));
		bac.getPemegang().setMspe_no_identity(f_validasi.convert_karakter(bac.getPemegang().getMspe_no_identity()));
		bac.getPemegang().setMspe_place_birth(f_validasi.convert_karakter(bac.getPemegang().getMspe_place_birth()));
		
		bac.getPemegang().setNama_si(f_validasi.convert_karakter(bac.getPemegang().getNama_si()));
		bac.getPemegang().setNama_anak1(f_validasi.convert_karakter(bac.getPemegang().getNama_anak1()));
		bac.getPemegang().setNama_anak2(f_validasi.convert_karakter(bac.getPemegang().getNama_anak2()));
		bac.getPemegang().setNama_anak3(f_validasi.convert_karakter(bac.getPemegang().getNama_anak3()));
		
		bac.getPemegang().setMcl_agama(f_validasi.convert_karakter(bac.getPemegang().getMcl_agama()));
		
		bac.getPemegang().setAlamat_rumah(f_validasi.convert_karakter(bac.getPemegang().getAlamat_rumah()));
		bac.getPemegang().setKd_pos_rumah(f_validasi.convert_karakter(bac.getPemegang().getKd_pos_rumah()));
		bac.getPemegang().setArea_code_rumah(f_validasi.convert_karakter(bac.getPemegang().getArea_code_rumah()));
		bac.getPemegang().setTelpon_rumah(f_validasi.convert_karakter(bac.getPemegang().getTelpon_rumah()));
		bac.getPemegang().setAlamat_kantor(f_validasi.convert_karakter(bac.getPemegang().getAlamat_kantor()));
		bac.getPemegang().setKd_pos_kantor(f_validasi.convert_karakter(bac.getPemegang().getKd_pos_kantor()));
		bac.getPemegang().setArea_code_kantor(f_validasi.convert_karakter(bac.getPemegang().getArea_code_kantor()));
		bac.getPemegang().setTelpon_kantor(f_validasi.convert_karakter(bac.getPemegang().getTelpon_kantor()));
		bac.getAddressbilling().setMsap_address(f_validasi.convert_karakter(bac.getAddressbilling().getMsap_address()));
		bac.getAddressbilling().setMsap_zip_code(f_validasi.convert_karakter(bac.getAddressbilling().getMsap_zip_code()));
		bac.getAddressbilling().setMsap_area_code1(f_validasi.convert_karakter(bac.getAddressbilling().getMsap_area_code1()));
		bac.getAddressbilling().setMsap_phone1(f_validasi.convert_karakter(bac.getAddressbilling().getMsap_phone1()));
		
		bac.getPemegang().setNo_hp(f_validasi.convert_karakter(bac.getPemegang().getNo_hp()));
		bac.getPemegang().setEmail(f_validasi.convert_karakter(bac.getPemegang().getEmail()));
		
		int umur = 0;
		if(bac.getPemegang().getMspe_date_birth() != null){
			f_hit_umur umr = new f_hit_umur();
			
			SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
			SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
			SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
			Date sysdate = elionsManager.selectSysdate();
			int tahun2 = Integer.parseInt(sdfYear.format(sysdate));
			int bulan2 = Integer.parseInt(sdfMonth.format(sysdate));
			int tanggal2 = Integer.parseInt(sdfDay.format(sysdate));
			int tahun1 = Integer.parseInt(sdfYear.format(bac.getPemegang().getMspe_date_birth()));
			int bulan1 = Integer.parseInt(sdfMonth.format(bac.getPemegang().getMspe_date_birth()));
			int tanggal1 = Integer.parseInt(sdfDay.format(bac.getPemegang().getMspe_date_birth()));
			umur=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
			
		}
		
		Date begDate = bac.getDatausulan().getMste_beg_date();
		Date endDate = (Date) begDate.clone();
		endDate.setYear(endDate.getYear()+1);
		endDate.setDate(endDate.getDate()-1);
		
		// pemegang
//		bac.getPemegang().setMspo_plan_provider(pas.getMspo_plan_provider());
		bac.getPemegang().setMspo_plan_provider("");
		bac.getPemegang().setNo_hp2("");
		bac.getPemegang().setMspe_email(bac.getPemegang().getEmail());
		bac.getPemegang().setMkl_red_flag(0);
		bac.getPemegang().setMspo_ao(bac.getAgen().getMsag_id());
//		bac.getPemegang().setMspo_pribadi(pas.getPribadi());
		bac.getPemegang().setMspo_pribadi(0);
		if(bac.getPemegang().getMspo_spaj_date() == null){
			bac.getPemegang().setMspo_spaj_date(elionsManager.selectSysdate());
		}
//		bac.getPemegang().setMste_tgl_recur(pas.getMsp_tgl_valid());
		bac.getPemegang().setMste_tgl_recur(null);
		bac.getPemegang().setLus_id(Integer.parseInt(currentUser.getLus_id()));
		bac.getPemegang().setMspo_age(umur);
		bac.getPemegang().setMste_age(umur);
		bac.getPemegang().setUsiapp(umur);
		bac.getPemegang().setLsre_id(1);
		bac.getPemegang().setMkl_tujuan("");
		bac.getPemegang().setTujuana("");
		bac.getPemegang().setMkl_pendanaan("");
		bac.getPemegang().setDanaa("");
		bac.getPemegang().setMkl_smbr_penghasilan("");
		bac.getPemegang().setShasil("");
		bac.getPemegang().setDanaa2("");
		bac.getPemegang().setMkl_kerja("");
		bac.getPemegang().setKerjaa("");
		bac.getPemegang().setKerjab("");
		bac.getPemegang().setMkl_industri("");
		bac.getPemegang().setIndustria("");
		bac.getPemegang().setLca_id(bac.getAgen().getLca_id());
		bac.getPemegang().setLwk_id(bac.getAgen().getLwk_id());
		bac.getPemegang().setLsrg_id(bac.getAgen().getLsrg_id());
		bac.getPemegang().setMkl_kerja("");
		bac.getPemegang().setKerjaa("");
		bac.getPemegang().setKerjab("");
		
		// address billing
		bac.getAddressbilling().setNo_hp(bac.getPemegang().getNo_hp());
		bac.getAddressbilling().setNo_hp2("");
		bac.getAddressbilling().setMsap_contact(bac.getPemegang().getMcl_first());
//		bac.getAddressbilling().setKota(pas.getMsp_city());
		bac.getAddressbilling().setE_mail(bac.getPemegang().getEmail());
//		bac.getAddressbilling().setReg_spaj(pas.getReg_spaj());
//		bac.getAddressbilling().setRegion(regionid);
		bac.getAddressbilling().setLca_id(bac.getAgen().getLca_id());
		bac.getAddressbilling().setLwk_id(bac.getAgen().getLwk_id());
		bac.getAddressbilling().setLsrg_id(bac.getAgen().getLsrg_id());
		
		// rekening client
		bac.getRekening_client().setNotes("");
		
		// tabungan ataupun kartu kredit, kedua rekening harus diisi & untuk tunai, rekening tabungan harus diisi
		// untuk term, pembayarannya tunai, jd tidak perlu rekening
		bac.getRekening_client().setLsbp_id("0");
		bac.getRekening_client().setMrc_nama("-");
		bac.getRekening_client().setMrc_cabang("-");
		bac.getRekening_client().setMrc_kota("-");
		bac.getRekening_client().setMrc_jn_nasabah(0);//none
		bac.getRekening_client().setMrc_kurs("01");//rupiah
		bac.getRekening_client().setNo_account("-");
		bac.getRekening_client().setMrc_no_ac("-");
		bac.getRekening_client().setMrc_no_ac_lama("-");
		bac.getRekening_client().setMrc_jenis(2);// rek client
		
		// account recur
//		bac.getAccount_recur().setLbn_id(pas.getLsbp_id_autodebet());
//		bac.getAccount_recur().setLus_id(pas.getLus_id());
//		bac.getAccount_recur().setMar_number(1);
//		bac.getAccount_recur().setMar_acc_no(pas.getMsp_no_rekening_autodebet());
//		bac.getAccount_recur().setMar_holder(pas.getMsp_rek_nama_autodebet());
//		bac.getAccount_recur().setMar_expired(pas.getMsp_tgl_valid());
//		bac.getAccount_recur().setMar_active(1);
		
		int mspo_pay_period = 1;
	
		bac.getDatausulan().setIsBungaSimponi(0);
		bac.getDatausulan().setIsBonusTahapan(0);
		bac.getDatausulan().setLssp_id(10);// POLICY IS BEING PROCESSED
		bac.getDatausulan().setLssa_id(1);// ENTRY SPAJ
		bac.getDatausulan().setLspd_id(1);// INPUT SPAJ
		bac.getDatausulan().setMspo_age(umur);
		if(bac.getDatausulan().getMspo_spaj_date() == null){
			bac.getDatausulan().setMspo_spaj_date(elionsManager.selectSysdate());
		}
		bac.getDatausulan().setMste_medical(0);
		bac.getDatausulan().setLscb_id(0);//TUNAI
//		bac.getDatausulan().setMspr_tsi(mspr_tsi);
//		bac.getDatausulan().setMspr_premium(mspr_premium);
//		bac.getDatausulan().setMspr_discount(new Double(0));
		bac.getDatausulan().setMste_flag_cc(8);// rek client = 0.tunai, 2.tabungan, 1.CC, 8.securitas
		bac.getDatausulan().setFlag_worksite(0);
		//bac.getDatausulan().setFlag_account(pas.getMsp_flag_cc());
		bac.getDatausulan().setFlag_account(2);// 0 untuk umum  1 untuk account recur 2 untuk rek client 3 untuk account recur dan rek client
		bac.getDatausulan().setKode_flag(0);//default
		bac.getDatausulan().setMspo_beg_date(begDate);
		bac.getDatausulan().setMspo_end_date(endDate);
		bac.getDatausulan().setMste_beg_date(begDate);
		bac.getDatausulan().setMste_end_date(endDate);
		bac.getDatausulan().setMspo_ins_period(1);
		bac.getDatausulan().setMspr_ins_period(1);
		bac.getDatausulan().setMspo_pay_period(mspo_pay_period);
		bac.getDatausulan().setFlag_jenis_plan(0);//SIMPLE BISNIS/ BAC SIMPLE
		bac.getDatausulan().setKurs_p("01");
		bac.getDatausulan().setLku_id("01");
//		bac.getDatausulan().setMspo_nasabah_acc(pas.getNo_kartu());
		bac.getDatausulan().setMspo_nasabah_acc("");
		bac.getDatausulan().setJenis_pemegang_polis(0);
		
		//SET PRODUCT 
		bac.getDatausulan().setPlan(bac.getDatausulan().getLsbs_id() + "~X" + bac.getDatausulan().getLsdbs_number());//0~X0
		Map product = check_product(bac.getDatausulan().getLsbs_id().toString(),bac.getDatausulan().getLsdbs_number().toString());
		bac.getDatausulan().setMspr_tsi(new Double(product.get("UP").toString()));//TERM INSURANCE
		bac.getDatausulan().setMspr_premium(new Double(product.get("PREMI").toString()));//TERM INSURANCE
		bac.getDatausulan().setMspr_discount(new Double(0));
		//
		
		//SET RIDER 
		bac.getDatausulan().setJmlrider(bac.getDatausulan().getDaftaRider().size());
		
		if (bac.getDatausulan().getJmlrider()>0){
			int count = 1;
			for(Object obj : bac.getDatausulan().getDaftaRider()){
				Datarider rider = (Datarider) obj;
				String bisnis = request.getParameter("ride.plan_rider1"+count);
				if (bisnis == null){
					bisnis="834~X1";
				}else{
					rider.setPlan_rider(bisnis);
					String[] r = rider.getPlan_rider().split("~X");
					rider.setLsbs_id(Integer.parseInt(r[0]));
					rider.setLsdbs_number(Integer.parseInt(r[1]));
					Map sub_product = check_product(rider.getLsbs_id().toString(),rider.getLsdbs_number().toString());
					rider.setMspr_tsi(new Double(sub_product.get("UP").toString()));//HOSPITAL CASH PLAN INCLUDE
					rider.setMspr_premium(new Double(sub_product.get("PREMI").toString()));//HOSPITAL CASH PLAN INCLUDE
					rider.setMspr_beg_date(begDate);
					rider.setMspr_end_date(endDate);
				}
				count++;
			}
		}
		
		// tertanggung
		bac.getTertanggung().setDanaa("");
		bac.getTertanggung().setDanaa("");
		bac.getTertanggung().setDanaa2("");
		bac.getTertanggung().setShasil("");
		bac.getTertanggung().setMkl_pendanaan("");
		bac.getTertanggung().setMkl_smbr_penghasilan("");
		bac.getTertanggung().setMkl_kerja("");
		bac.getTertanggung().setMkl_industri("");
		bac.getTertanggung().setKerjaa("");
		bac.getTertanggung().setKerjab("");
		bac.getTertanggung().setIndustria("");
		bac.getTertanggung().setTujuana("");
		bac.getTertanggung().setMkl_tujuan("");
		bac.getTertanggung().setLca_id(bac.getAgen().getLca_id());
		bac.getTertanggung().setLwk_id(bac.getAgen().getLwk_id());
		bac.getTertanggung().setLsrg_id(bac.getAgen().getLsrg_id());
		bac.getTertanggung().setReg_spaj("");
		bac.getTertanggung().setMspo_policy_no("");
		bac.getTertanggung().setLus_id(Integer.parseInt(currentUser.getLus_id()));
		
		if(bac.getPemegang().getLsre_id() == 1){
			bac.getTertanggung().setLti_id(bac.getPemegang().getLti_id());
			bac.getTertanggung().setMspe_mother(bac.getPemegang().getMspe_mother());
			bac.getTertanggung().setNo_hp(bac.getPemegang().getNo_hp());
			bac.getTertanggung().setNo_hp2(bac.getPemegang().getNo_hp2());
			bac.getTertanggung().setMspe_no_identity(bac.getPemegang().getMspe_no_identity());
			bac.getTertanggung().setAlamat_rumah(bac.getPemegang().getAlamat_rumah());
			bac.getTertanggung().setKota_rumah(bac.getPemegang().getKota_rumah());
			bac.getTertanggung().setKd_pos_rumah(bac.getPemegang().getKd_pos_rumah());
			bac.getTertanggung().setMspe_email(bac.getPemegang().getMspe_email());
			bac.getTertanggung().setEmail(bac.getPemegang().getEmail());
			bac.getTertanggung().setMspe_sex(bac.getPemegang().getMspe_sex());
			bac.getTertanggung().setTelpon_rumah(bac.getPemegang().getTelpon_rumah());
			bac.getTertanggung().setArea_code_rumah(bac.getPemegang().getArea_code_rumah());
			bac.getTertanggung().setLside_id(bac.getPemegang().getLside_id());
			bac.getTertanggung().setMspe_place_birth(bac.getPemegang().getMspe_place_birth());
			bac.getTertanggung().setLsne_id(bac.getPemegang().getLsne_id());
			bac.getTertanggung().setMspe_sts_mrt(bac.getPemegang().getMspe_sts_mrt());
			bac.getTertanggung().setLsag_id(bac.getPemegang().getLsag_id());
			bac.getTertanggung().setMcl_agama(bac.getPemegang().getMcl_agama());
			bac.getTertanggung().setLsed_id(bac.getPemegang().getLsed_id());
			bac.getTertanggung().setMkl_kerja(bac.getPemegang().getMkl_kerja());
			bac.getTertanggung().setMspo_plan_provider(bac.getPemegang().getMspo_plan_provider());
			bac.getTertanggung().setMkl_red_flag(bac.getPemegang().getMkl_red_flag());
			bac.getTertanggung().setMcl_first(bac.getPemegang().getMcl_first());
			bac.getTertanggung().setMspe_date_birth(bac.getPemegang().getMspe_date_birth());
			bac.getTertanggung().setMste_age(bac.getPemegang().getMspo_age());
			bac.getTertanggung().setUsiattg(bac.getPemegang().getUsiapp());
			bac.getTertanggung().setKerjaa(bac.getPemegang().getKerjaa());
			bac.getTertanggung().setKerjab(bac.getPemegang().getKerjab());
			bac.getTertanggung().setAlamat_kantor(bac.getPemegang().getAlamat_kantor());
			bac.getTertanggung().setKd_pos_kantor(bac.getPemegang().getKd_pos_kantor());
			bac.getTertanggung().setArea_code_kantor(bac.getPemegang().getArea_code_kantor());
			bac.getTertanggung().setTelpon_kantor(bac.getPemegang().getTelpon_kantor());
			
			bac.getTertanggung().setNama_si(bac.getPemegang().getNama_si());
			bac.getTertanggung().setNama_anak1(bac.getPemegang().getNama_anak1());
			bac.getTertanggung().setNama_anak2(bac.getPemegang().getNama_anak2());
			bac.getTertanggung().setNama_anak3(bac.getPemegang().getNama_anak3());
			bac.getTertanggung().setTgllhr_si(bac.getPemegang().getTgllhr_si());
			bac.getTertanggung().setTgllhr_anak1(bac.getPemegang().getTgllhr_anak1());
			bac.getTertanggung().setTgllhr_anak2(bac.getPemegang().getTgllhr_anak2());
			bac.getTertanggung().setTgllhr_anak3(bac.getPemegang().getTgllhr_anak3());
		}
		
		return bac;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException errors) throws Exception {
		//request.getSession().setAttribute("dataInputSpajSimple", (Cmdeditbac) cmd);
	}
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException err, int page) throws Exception {
		Cmdeditbac cmd = (Cmdeditbac) command;
		
		f_validasi f_validasi = new f_validasi();
		Cmdeditbac bac= (Cmdeditbac)cmd;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		bac = modSet(bac, currentUser, request);
		//List<String> err = new ArrayList<String>();
		
		if(StringUtil.isEmpty(bac.getPemegang().getMspo_no_blanko())){err.reject("", "Silahkan masukkan Nomor Seri/Blanko");}
		
//		if(bac.getPemegang().getLti_id() == null){err.reject("", "TERTANGGUNG: Silahkan masukkan Gelar");}
		if(StringUtil.isEmpty(bac.getPemegang().getMcl_first())){err.reject("", "TERTANGGUNG: Silahkan masukkan Nama");}
		if(StringUtil.isEmpty(bac.getPemegang().getMspe_mother())){err.reject("", "TERTANGGUNG: Silahkan masukkan Nama Ibu Kandung");}
		if(bac.getPemegang().getLside_id() == 0){err.reject("", "TERTANGGUNG: Bukti Identitas harus ada");}
		if(StringUtil.isEmpty(bac.getPemegang().getMspe_no_identity())){err.reject("", "TERTANGGUNG: Silahkan masukkan No. KTP / Identitas lain");}
		if(bac.getPemegang().getMspe_date_birth() == null){err.reject("", "TERTANGGUNG: Silahkan masukkan Tanggal Lahir");}
		if(StringUtil.isEmpty(bac.getPemegang().getMspe_place_birth())){err.reject("", "TERTANGGUNG: Silahkan masukkan Tempat Lahir");}
		
//		if(StringUtil.isEmpty(bac.getPemegang().getNama_si())){err.reject("", "TERTANGGUNG: Silahkan masukkan Nama Suami/Istri");}
//		if(StringUtil.isEmpty(bac.getPemegang().getNama_anak1())){err.reject("", "TERTANGGUNG: Silahkan masukkan Nama Anak 1");}
//		if(StringUtil.isEmpty(bac.getPemegang().getNama_anak2())){err.reject("", "TERTANGGUNG: Silahkan masukkan Nama Anak 2");}
//		if(StringUtil.isEmpty(bac.getPemegang().getNama_anak3())){err.reject("", "TERTANGGUNG: Silahkan masukkan Nama Anak 3");}
		if(!StringUtil.isEmpty(bac.getPemegang().getNama_si()) && bac.getPemegang().getTgllhr_si() == null)
			{err.reject("", "TERTANGGUNG: Silahkan masukkan Tanggal Lahir Suami/Istri");}
		if(!StringUtil.isEmpty(bac.getPemegang().getNama_anak1()) && bac.getPemegang().getTgllhr_anak1() == null)
			{err.reject("", "TERTANGGUNG: Silahkan masukkan Tanggal Lahir Anak 1");}
		if(!StringUtil.isEmpty(bac.getPemegang().getNama_anak2()) && bac.getPemegang().getTgllhr_anak2() == null)
			{err.reject("", "TERTANGGUNG: Silahkan masukkan Tanggal Lahir Anak 2");}
		if(!StringUtil.isEmpty(bac.getPemegang().getNama_anak3()) && bac.getPemegang().getTgllhr_anak3() == null)
			{err.reject("", "TERTANGGUNG: Silahkan masukkan Tanggal Lahir Anak 3");}
		
//		if(bac.getPemegang().getLsag_id() == 0){err.reject("", "TERTANGGUNG: Agama harus ada");}
		if(bac.getPemegang().getLsag_id() == 6 && StringUtil.isEmpty(bac.getPemegang().getMcl_agama()))
			{err.reject("", "TERTANGGUNG: Silahkan masukkan Agama Lainnya");}
		
//		if(bac.getPemegang().getLsed_id() == 0){err.reject("", "TERTANGGUNG: Pendidikan harus ada");}
		
		if(StringUtil.isEmpty(bac.getPemegang().getAlamat_rumah())){err.reject("", "TERTANGGUNG: Silahkan masukkan Alamat Rumah");}
		if(StringUtil.isEmpty(bac.getPemegang().getKd_pos_rumah())){err.reject("", "TERTANGGUNG: Silahkan masukkan Kode Pos Rumah");}
		if(StringUtil.isEmpty(bac.getPemegang().getArea_code_rumah())){err.reject("", "TERTANGGUNG: Silahkan masukkan Kode Area Rumah");}
		if(StringUtil.isEmpty(bac.getPemegang().getTelpon_rumah())){err.reject("", "TERTANGGUNG: Silahkan masukkan Telp Rumah");}
//		if(StringUtil.isEmpty(bac.getPemegang().getAlamat_kantor())){err.reject("", "TERTANGGUNG: Silahkan masukkan Alamat Kantor");}
//		if(StringUtil.isEmpty(bac.getPemegang().getKd_pos_kantor())){err.reject("", "TERTANGGUNG: Silahkan masukkan Kode Pos Kantor");}
//		if(StringUtil.isEmpty(bac.getPemegang().getArea_code_kantor())){err.reject("", "TERTANGGUNG: Silahkan masukkan Kode Area Kantor");}
//		if(StringUtil.isEmpty(bac.getPemegang().getTelpon_kantor())){err.reject("", "TERTANGGUNG: Silahkan masukkan Telp Kantor");}
		if(StringUtil.isEmpty(bac.getAddressbilling().getMsap_address())){err.reject("", "TERTANGGUNG: Silahkan masukkan Alamat Penagihan");}
		if(StringUtil.isEmpty(bac.getAddressbilling().getMsap_zip_code())){err.reject("", "TERTANGGUNG: Silahkan masukkan Kode Pos Penagihan");}
		if(StringUtil.isEmpty(bac.getAddressbilling().getMsap_area_code1())){err.reject("", "TERTANGGUNG: Silahkan masukkan Kode Area Penagihan");}
		if(StringUtil.isEmpty(bac.getAddressbilling().getMsap_phone1())){err.reject("", "TERTANGGUNG: Silahkan masukkan Telp Penagihan");}
		
		if(StringUtil.isEmpty(bac.getPemegang().getNo_hp())){err.reject("", "TERTANGGUNG: Silahkan masukkan No Handphone");}
//		if(StringUtil.isEmpty(bac.getPemegang().getEmail())){err.reject("", "TERTANGGUNG: Silahkan masukkan Email");}
		
		//untuk validasi lainnya
		if(!StringUtil.isEmpty(bac.getPemegang().getEmail())){
			if (f_validasi.f_validasi_email(bac.getPemegang().getEmail())==false){err.reject("", "TERTANGGUNG: Silahkan masukkan Email dalam bentuk numerik,alphabet,@,_");}
		}
		if (f_validasi.f_validasi_numerik(bac.getPemegang().getKd_pos_rumah())==false){err.reject("", "TERTANGGUNG: Silahkan masukkan Kode Pos Rumah dalam bentuk numerik");}
		if (f_validasi.f_validasi_numerik(bac.getPemegang().getArea_code_rumah())==false){err.reject("", "TERTANGGUNG: Silahkan masukkan Kode Area Rumah dalam bentuk numerik");}
		if (f_validasi.f_validasi_numerik(bac.getPemegang().getTelpon_rumah())==false){err.reject("", "TERTANGGUNG: Silahkan masukkan Telp Rumah dalam bentuk numerik");}
		if (f_validasi.f_validasi_numerik(bac.getPemegang().getKd_pos_kantor())==false){err.reject("", "TERTANGGUNG: Silahkan masukkan Kode Pos Kantor dalam bentuk numerik");}
		if (f_validasi.f_validasi_numerik(bac.getPemegang().getArea_code_kantor())==false){err.reject("", "TERTANGGUNG: Silahkan masukkan Kode Area Kantor dalam bentuk numerik");}
		if (f_validasi.f_validasi_numerik(bac.getPemegang().getTelpon_kantor())==false){err.reject("", "TERTANGGUNG: Silahkan masukkan Telp Kantor dalam bentuk numerik");}
		if (f_validasi.f_validasi_numerik(bac.getAddressbilling().getMsap_zip_code())==false){err.reject("", "TERTANGGUNG: Silahkan masukkan Kode Pos Penagihan dalam bentuk numerik");}
		if (f_validasi.f_validasi_numerik(bac.getAddressbilling().getMsap_area_code1())==false){err.reject("", "TERTANGGUNG: Silahkan masukkan Kode Area Penagihan dalam bentuk numerik");}
		if (f_validasi.f_validasi_numerik(bac.getAddressbilling().getMsap_phone1())==false){err.reject("", "TERTANGGUNG: Silahkan masukkan Telp Penagihan dalam bentuk numerik");}
		if (f_validasi.f_validasi_numerik(bac.getPemegang().getNo_hp())==false){err.reject("", "TERTANGGUNG: Silahkan masukkan No Handphone dalam bentuk numerik");}
		//
		
		//validasi usia
		int umur = bac.getPemegang().getUsiapp();
		Map product = check_product(bac.getDatausulan().getLsbs_id().toString(),bac.getDatausulan().getLsdbs_number().toString());
		int p_umur_min = Integer.parseInt(product.get("USIA_MIN").toString());
		int p_umur_max = Integer.parseInt(product.get("USIA_MAX").toString());
		if(!(umur >= p_umur_min && umur <= p_umur_max)){
			err.reject("", "TERTANGGUNG: Batasan umur untuk produk adalah " + p_umur_min + " sd " + p_umur_max + " tahun");
		}
		for(Object obj : bac.getDatausulan().getDaftaRider()){
			Datarider rider = (Datarider) obj;
			String[] r = rider.getPlan_rider().split("~X");
			rider.setLsbs_id(Integer.parseInt(r[0]));
			rider.setLsdbs_number(Integer.parseInt(r[1]));
			Map sub_product = check_product(rider.getLsbs_id().toString(),rider.getLsdbs_number().toString());
			int r_umur_min = Integer.parseInt(product.get("USIA_MIN").toString());
			int r_umur_max = Integer.parseInt(product.get("USIA_MAX").toString());
			if(!(umur >= r_umur_min && umur <= r_umur_max)){
				err.reject("", "TERTANGGUNG: Batasan umur untuk rider adalah " + r_umur_min + " sd " + r_umur_max + " tahun");
			}
		}
		//
		
	}
	

	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err, int page)
			throws Exception {
		Cmdeditbac bac = new Cmdeditbac();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		//LIST
		request.setAttribute("select_gelar",DroplistManager.getInstance().get("GELAR.xml","ID",request));
		request.setAttribute("select_marital",DroplistManager.getInstance().get("MARITAL.xml","ID",request));
		request.setAttribute("select_identitas", elionsManager.selectDropDown("eka.lst_identity", "lside_id", "lside_name", "", "lside_id", null));
		request.setAttribute("select_negara", elionsManager.selectDropDown("eka.lst_negara", "lsne_id", "lsne_note", "", "lsne_urut", null));
		request.setAttribute("select_agama", elionsManager.selectDropDown("eka.lst_agama", "lsag_id", "lsag_name", "", "lsag_name", null));
		request.setAttribute("select_pendidikan", elionsManager.selectDropDown("eka.lst_education", "lsed_id", "lsed_name", "", "lsed_name", null));
		request.setAttribute("select_product_plan", manual_product());
		request.setAttribute("select_sub_product_plan", manual_sub_product());
		request.setAttribute("select_rider_plan", manual_rider());
		request.setAttribute("select_sub_rider_plan", manual_sub_rider());
		//
		
		List lsDaftarSpaj = this.uwManager.selectDaftarSPAJSimple("1",1,null,null,"simple");
		
		request.setAttribute("daftarSPAJ", lsDaftarSpaj);
		
		return null;
		//return new ModelAndView("bac/bac_simple", "cmd", bac);
	}
	
	//CHECK PRODUCT
	public Map check_product(String product, String sub){
		Map<String, String> p = new HashMap<String, String>();
		
		//DEFAULT
		p.put("PREMI", "0");
		p.put("UP", "0");
		
		//PRODUCT
		if(product.equals("196")){//TERM INSURANCE
			if(sub.equals("2")){//TERM INSURANCE (SMS)
				p.put("PREMI", "346000");
				p.put("UP", "100000000");
				//USIA
				p.put("USIA_MIN", "17");
				p.put("USIA_MAX", "55");
			}
		}
		
		//RIDER
		if(product.equals("834")){//HOSPITAL CASH PLAN INCLUDE
			if(sub.equals("1")){//HCP R-250
				p.put("PREMI", "364000");
				p.put("UP", "250000");
				//USIA
				p.put("USIA_MIN", "17");
				p.put("USIA_MAX", "55");
			}else if(sub.equals("2")){//HCP R-500
				p.put("PREMI", "728000");
				p.put("UP", "500000");
				//USIA
				p.put("USIA_MIN", "17");
				p.put("USIA_MAX", "55");
			}
		}
		
		return p;
	}
	
	
	//LIST MANUAL PRODUCT
	
	public List<DropDown> manual_product(){
		List<DropDown> manual_product = new ArrayList<DropDown>();
		//TERM
		manual_product.addAll(elionsManager.selectDropDown("eka.lst_bisnis", "lsbs_id", "lsbs_name", "", "lsbs_id", "lsbs_id = 196"));
		
		return manual_product;
	}
	
	//LIST MANUAL SUB PRODUCT
	
	public List<DropDown> manual_sub_product(){
		List<DropDown> manual_sub_product = new ArrayList<DropDown>();
		//TERM (SMS)
		manual_sub_product.addAll(elionsManager.selectDropDown("eka.lst_det_bisnis", "(lsbs_id||'~X'||lsdbs_number)", "lsdbs_name", "", "lsdbs_number", "lsbs_id = 196 and lsdbs_number = 2"));
		
		return manual_sub_product;
	}
	
	//LIST MANUAL RIDER
	
	public List<DropDown> manual_rider(){
		List<DropDown> manual_rider = new ArrayList<DropDown>();
		//HCP
		manual_rider.addAll(elionsManager.selectDropDown("eka.lst_bisnis", "lsbs_id", "lsbs_name", "", "lsbs_id", "lsbs_id = 834"));
		
		return manual_rider;
	}
		
	//LIST MANUAL SUB RIDER
		
	public List<DropDown> manual_sub_rider(){
		List<DropDown> manual_sub_rider = new ArrayList<DropDown>();
		//HCP
		manual_sub_rider.addAll(elionsManager.selectDropDown("eka.lst_det_bisnis", "(lsbs_id||'~X'||lsdbs_number)", "lsdbs_name", "", "lsdbs_number", "lsbs_id = 834"));
		
		return manual_sub_rider;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object cmd, BindException err)
			throws Exception {
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		Cmdeditbac bac = (Cmdeditbac) cmd;
		
		String spaj="";
		String keterangan = bac.getDatausulan().getKeterangan_fund();
		String status_submit = bac.getDatausulan().getStatus_submit();
		if (bac.getDatausulan().getStatus_submit() == null)
		{
			bac.getDatausulan().setStatus_submit("");
			status_submit = "";
		}
		String status="";
		if ( bac.getPemegang().getReg_spaj() != null && !bac.getPemegang().getReg_spaj().equalsIgnoreCase("") )
		{
			status="edit";
			if (bac.getDatausulan().getStatus_submit().equalsIgnoreCase("ulang"))
			{
				status ="input";
				bac.getPemegang().setReg_spaj("");
			}
			
		}else{
			status ="input";
		}
		
		//SET PRODUCT 
		String[] p = bac.getDatausulan().getPlan().split("~X");
		bac.getDatausulan().setLsbs_id(Integer.parseInt(p[0]));
		bac.getDatausulan().setLsdbs_number(Integer.parseInt(p[1]));
		Map product = check_product(bac.getDatausulan().getLsbs_id().toString(),bac.getDatausulan().getLsdbs_number().toString());
		bac.getDatausulan().setMspr_tsi(new Double(product.get("UP").toString()));//TERM INSURANCE
		bac.getDatausulan().setMspr_premium(new Double(product.get("PREMI").toString()));//TERM INSURANCE
		bac.getDatausulan().setMspr_discount(new Double(0));
		//
			
		//SET RIDER 
		bac.getDatausulan().setJmlrider(bac.getDatausulan().getDaftaRider().size());
		
		if (status.equalsIgnoreCase("input"))
		{
			bac=this.uwManager.savingspajBacSimple(bac,err,"input",currentUser);
			spaj = bac.getPemegang().getReg_spaj();
//			if(spaj.equals("")) {
//				request.getSession().setAttribute("dataInputSpajSimple", bac);
//			}else{
//				File file = new File(bac.getPathFileTemp()+"\\bacsimple.txt");
//				if(file.exists()){
//					file.delete();
//					bac.setFileConfirm("");
//				}
//				request.getSession().removeAttribute("dataInputSpajSimple");
//			}
		}else{
			Integer status_polis = this.elionsManager.selectPositionSpaj(bac.getPemegang().getReg_spaj());
			//if(status_polis==null) status_polis = 5;
			if (status_polis.intValue() != 5)
			{
				bac=this.uwManager.savingspajBacSimple(bac,err,"edit",currentUser);
				spaj = bac.getPemegang().getReg_spaj();
//				if(spaj.equals("")) {
//					request.getSession().setAttribute("dataInputSpajSimple", bac);
//				}else{
//					File file = new File(bac.getPathFileTemp()+"\\bacsimple.txt");
//					if(file.exists()){
//						file.delete();
//						bac.setFileConfirm("");
//					}
//					request.getSession().removeAttribute("dataInputSpajSimple");
//				}
			}else{
				///err.reject("pemegang.reg_spaj","","Spaj ini sudah diaksep, tidak bisa diedit.");
				request.setAttribute("err", "Spaj ini sudah diaksep, tidak bisa diedit.");
			}
		}
		
		keterangan = bac.getDatausulan().getKeterangan_fund();
		Integer status_polis = bac.getHistory().getStatus_polis();
		Integer lspd_id = bac.getDatausulan().getLspd_id();
		Integer kodebisnis = bac.getDatausulan().getLsbs_id();
		Integer jml_peserta = bac.getDatausulan().getJml_peserta();
		
		Map<String, Comparable> m =new HashMap<String, Comparable>();
		m.put("nomorspaj",spaj);
		m.put("status",status);
		m.put("keterangan",keterangan);
		m.put("status_polis",status_polis);
		m.put("lspd_id",lspd_id);
		m.put("kodebisnis", kodebisnis);
		m.put("jml_peserta", jml_peserta);
		m.put("status_submit", status_submit);
		
		return new ModelAndView("bac/edit_bac_simple_finish",m);
	}
	
}
	