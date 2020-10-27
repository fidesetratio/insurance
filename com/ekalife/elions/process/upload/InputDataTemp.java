package com.ekalife.elions.process.upload;
 

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Biayainvestasi;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.ContactPerson;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DetilInvestasi;
import com.ekalife.elions.model.DetilTopUp;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.PembayarPremi;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Personal;
import com.ekalife.elions.model.PesertaPlus_mix;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_validasi;
import com.ekalife.utils.parent.ParentDao;



public class InputDataTemp  extends ParentDao {	   
	private BacManager bacManager;	
	
	private ElionsManager elionsManager;
	private UwManager uwManager;
	
	public void setBacManager(BacManager bacManager) {
	    this.bacManager = bacManager;
	 }
	
	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}
	
	public void setUwManager(UwManager uwManager) {
		this.uwManager = uwManager;
	}
	SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
	SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
	SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
	SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMyyyy");
		
		
		
	
	public String inputDatatemp(String no_temp, User user) throws ServletException, IOException, ParseException {
		Cmdeditbac detiledit = new Cmdeditbac();
		
		String pesan = "";
		Pemegang pemegangTemp = new Pemegang();
		Tertanggung ttgTemp = new Tertanggung();
		PembayarPremi p_premiTemp = new PembayarPremi();
		Datausulan planUtama = new Datausulan();
		AddressBilling addrBillTemp = new AddressBilling();
		DetilTopUp topUp = new DetilTopUp();
		InvestasiUtama invTemp = new InvestasiUtama();
		Account_recur acrTemp = new Account_recur();
		Rekening_client rekClient = new Rekening_client();
		ArrayList detilInv = new ArrayList();
		ArrayList biayaInv = new ArrayList();

		pemegangTemp = (Pemegang) this.bacDao.selectppTemp(no_temp); //
		ttgTemp = (Tertanggung) this.bacDao.selectttTemp2(no_temp); //
	    p_premiTemp = (PembayarPremi) this.bacDao.selectPemPremiTemp(no_temp); //
		addrBillTemp = (AddressBilling) this.bacDao.selectAddressBillingTemp(no_temp); //
	    planUtama = this.bacDao.selectMstProductTemp(no_temp);//
	    List<Datarider> planRider = this.bacDao.selectMstProductTempRider(no_temp);
	    List<PesertaPlus_mix> peserta_plus = this.bacDao.selectPesertaTemp(no_temp);
 	    topUp = (DetilTopUp) this.bacDao.selectDetilTopupTemp(no_temp);
	    invTemp = (InvestasiUtama) this.bacDao.selectinvTemp(no_temp);
	    rekClient = this.bacDao.selectRekeningNasabahTemp(no_temp);
	    acrTemp = this.bacDao.selectAccRecurTemp(no_temp);
        detilInv = Common.serializableList(this.bacDao.selectDetilInvTemp(no_temp));
 	    biayaInv = Common.serializableList(this.bacDao.selectBiayaInvTemp(no_temp));
	    detiledit.setDatausulan(planUtama);
	    if(detiledit.getDatausulan().getLsbs_id()==120 || detiledit.getDatausulan().getLsbs_id()==163 || pemegangTemp.getMspo_flag_spaj()==4 || pemegangTemp.getMspo_flag_spaj()==5){
	    	pemegangTemp = (Pemegang) this.bacDao.selectppTempNew(no_temp);	
	    	ttgTemp = (Tertanggung) this.bacDao.selectttgTempNew(no_temp);
	    }
		detiledit.setPemegang(pemegangTemp);
		detiledit.setTertanggung(ttgTemp);
		
		detiledit.setNo_temp(no_temp);
		int plan_rider = planRider.size();
		int jmlPsrta = peserta_plus.size();
		detiledit.getDatausulan().setJmlrider(plan_rider);
		detiledit.getDatausulan().setJml_peserta(jmlPsrta);
		detiledit.setAccount_recur(acrTemp);
		detiledit.setFlag_special_case("0");
		
		if(pemegangTemp!=null){
			if(detiledit.getPemegang().getMspo_flag_spaj()==4 || detiledit.getPemegang().getMspo_flag_spaj()==5){
				detiledit.getPemegang().setTujuana(detiledit.getPemegang().getMkl_tujuan());
				detiledit.getPemegang().setDanaa(detiledit.getPemegang().getMkl_pendanaan());
				detiledit.getPemegang().setKerjaa(detiledit.getPemegang().getMkl_kerja());
				detiledit.getPemegang().setIndustria(detiledit.getPemegang().getMkl_industri());
				//String []pendapatanBulan ={detiledit.getPemegang().getBulan_gaji().toString()}
				if(detiledit.getPemegang().getNama_si()==null)detiledit.getPemegang().setNama_si("");
				if(detiledit.getPemegang().getNama_anak1()==null)detiledit.getPemegang().setNama_anak1("");
				if(detiledit.getPemegang().getNama_anak2()==null)detiledit.getPemegang().setNama_anak2("");
				if(detiledit.getPemegang().getNama_anak3()==null)detiledit.getPemegang().setNama_anak3("");
				detiledit.getPemegang().setPemegang_polis("2");
				detiledit.getPemegang().setKerjaNote(detiledit.getPemegang().getMkl_kerja());
				String idPekerjaanPP = this.bacDao.selectIdLstPekerjaan(detiledit.getPemegang().getMkl_kerja());
				detiledit.getPemegang().setMkl_kerja(idPekerjaanPP);
			}else{
				detiledit.getPemegang().setTujuana("-");
				detiledit.getPemegang().setDanaa("-");
				detiledit.getPemegang().setKerjaa("-");
				detiledit.getPemegang().setIndustria("-");
				detiledit.getPemegang().setShasil("-");
				detiledit.getPemegang().setMspe_no_identity_expired(commonDao.selectSysdate());
				detiledit.getPemegang().setMcl_agama("LAIN-LAIN");
				detiledit.getPemegang().setNama_si("");
				detiledit.getPemegang().setNama_anak1("");
				detiledit.getPemegang().setNama_anak2("");
				detiledit.getPemegang().setNama_anak3("");
				detiledit.getPemegang().setMkl_pendanaan("LAINNYA");
				detiledit.getPemegang().setMspo_flag_spaj(0);
			}
			
			detiledit.getPemegang().setFlag_upload(1);
			detiledit.getPemegang().setKyc(0);
			detiledit.getPemegang().setMspo_ref_bii(0);
			detiledit.getPemegang().setMspo_pribadi(0);
			detiledit.getPemegang().setMspo_spaj_date(commonDao.selectSysdateTrunc());
			detiledit.getPemegang().setMste_flag_guthrie(0);
			detiledit.getPemegang().setMkl_dana_from(0);
			detiledit.getPemegang().setMkl_red_flag(0);
			detiledit.getPemegang().setFlag_hadiah(0);			
			detiledit.getPemegang().setMspo_ao("");
			detiledit.getPemegang().setMspo_follow_up(0);
			detiledit.getPemegang().setLus_id(Integer.parseInt(user.getLus_id()));
			detiledit.getPemegang().setMkl_smbr_penghasilan("LAINNYA");			
			detiledit.getPemegang().setJmlkyc(0);
			detiledit.getPemegang().setJmlkyc2(0);
			detiledit.getPemegang().setKerjab("");
			detiledit.getPemegang().setUsiapp(detiledit.getPemegang().getMspo_age());
		}
		
		if(ttgTemp!=null){
		/*	if(detiledit.getDatausulan().getLsbs_id()!=153 && pemegangTemp.getMspo_flag_spaj()!=4){
				detiledit.getTertanggung().setMste_no_vacc("");				
			}*/
			detiledit.setPersonal(new Personal());
			detiledit.setContactPerson(new ContactPerson());
			
			if(detiledit.getPemegang().getLsre_id()==1){
				
				detiledit.setTertanggung(new Tertanggung());
				detiledit.getTertanggung().setAlamat_kantor(detiledit.getPemegang().getAlamat_kantor());
				detiledit.getTertanggung().setAlamat_rumah(detiledit.getPemegang().getAlamat_rumah());
				detiledit.getTertanggung().setArea_code_kantor(detiledit.getPemegang().getArea_code_kantor());
				detiledit.getTertanggung().setArea_code_rumah(detiledit.getPemegang().getArea_code_rumah());
				detiledit.getTertanggung().setDanaa(detiledit.getPemegang().getDanaa());				
				detiledit.getTertanggung().setShasil(detiledit.getPemegang().getShasil());
				detiledit.getTertanggung().setEmail(detiledit.getPemegang().getEmail());
				detiledit.getTertanggung().setIndustria(detiledit.getPemegang().getIndustria());
				detiledit.getTertanggung().setKd_pos_kantor(detiledit.getPemegang().getKd_pos_kantor());
				detiledit.getTertanggung().setKd_pos_rumah(detiledit.getPemegang().getKd_pos_rumah());
				detiledit.getTertanggung().setKerjaa(detiledit.getPemegang().getKerjaa());
				detiledit.getTertanggung().setKerjab(detiledit.getPemegang().getKerjaa());
				detiledit.getTertanggung().setKota_kantor(detiledit.getPemegang().getKota_kantor());
				detiledit.getTertanggung().setKota_rumah(detiledit.getPemegang().getKota_rumah());
				detiledit.getTertanggung().setLgj_id(detiledit.getPemegang().getLgj_id());
				detiledit.getTertanggung().setLgj_note(detiledit.getPemegang().getLgj_note());
				detiledit.getTertanggung().setLjb_id(detiledit.getPemegang().getLjb_id());
				detiledit.getTertanggung().setLsag_name(detiledit.getPemegang().getLsag_name());
				detiledit.getTertanggung().setLscb_id(detiledit.getPemegang().getLscb_id());
				detiledit.getTertanggung().setLsed_id(detiledit.getPemegang().getLsed_id());
				detiledit.getTertanggung().setLsed_name(detiledit.getPemegang().getLsed_name());
				detiledit.getTertanggung().setLside_id(detiledit.getPemegang().getLside_id());
				detiledit.getTertanggung().setLside_name(detiledit.getPemegang().getLside_name());
				detiledit.getTertanggung().setLsne_id(detiledit.getPemegang().getLsne_id());
				detiledit.getTertanggung().setLsne_note(detiledit.getPemegang().getLsne_note());
				detiledit.getTertanggung().setLsre_id(detiledit.getPemegang().getLsre_id());
				detiledit.getTertanggung().setLsre_relation(detiledit.getPemegang().getLsre_relation());
				detiledit.getTertanggung().setLti_id(detiledit.getPemegang().getLti_id());
				detiledit.getTertanggung().setMcl_first(detiledit.getPemegang().getMcl_first());
				detiledit.getTertanggung().setMcl_gelar(detiledit.getPemegang().getMcl_gelar());
				detiledit.getTertanggung().setMcl_id(detiledit.getPemegang().getMcl_id());
				detiledit.getTertanggung().setMkl_industri(detiledit.getPemegang().getMkl_industri());
				detiledit.getTertanggung().setMkl_kerja(detiledit.getPemegang().getMkl_kerja());
				detiledit.getTertanggung().setMkl_pendanaan(detiledit.getPemegang().getMkl_pendanaan());
				detiledit.getTertanggung().setMkl_penghasilan(detiledit.getPemegang().getMkl_penghasilan());
				detiledit.getTertanggung().setLsre_id_premi(detiledit.getPemegang().getLsre_id_premi());
				detiledit.getTertanggung().setMkl_smbr_penghasilan(detiledit.getPemegang().getMkl_smbr_penghasilan());
				detiledit.getTertanggung().setMkl_tujuan(detiledit.getPemegang().getMkl_tujuan());
				detiledit.getTertanggung().setMpn_job_desc(detiledit.getPemegang().getMpn_job_desc());
				detiledit.getTertanggung().setMsag_id(detiledit.getPemegang().getMsag_id());
				detiledit.getTertanggung().setMspe_date_birth(detiledit.getPemegang().getMspe_date_birth());
				detiledit.getTertanggung().setMspe_lama_kerja(detiledit.getPemegang().getMspe_lama_kerja());
				detiledit.getTertanggung().setMspe_mother(detiledit.getPemegang().getMspe_mother());
				detiledit.getTertanggung().setMspe_no_identity(detiledit.getPemegang().getMspe_no_identity());
				detiledit.getTertanggung().setMspe_no_identity_expired(detiledit.getPemegang().getMspe_no_identity_expired());
				detiledit.getTertanggung().setMspe_place_birth(detiledit.getPemegang().getMspe_place_birth());
				detiledit.getTertanggung().setMspe_sex(detiledit.getPemegang().getMspe_sex());
				detiledit.getTertanggung().setMspe_sex2(detiledit.getPemegang().getMspe_sex2());
				detiledit.getTertanggung().setMkl_dana_from(detiledit.getPemegang().getMkl_dana_from());
				detiledit.getTertanggung().setMkl_hasil_from(detiledit.getPemegang().getMkl_hasil_from());
				detiledit.getTertanggung().setMkl_smbr_hasil_from(detiledit.getPemegang().getMkl_smbr_hasil_from());
				detiledit.getTertanggung().setMkl_sumber_premi(detiledit.getPemegang().getMkl_sumber_premi());
				detiledit.getTertanggung().setMspe_sts_mrt(detiledit.getPemegang().getMspe_sts_mrt());
				detiledit.getTertanggung().setMspo_policy_no(detiledit.getPemegang().getMspo_policy_no());
				detiledit.getTertanggung().setMste_age(detiledit.getPemegang().getMste_age());
				detiledit.getTertanggung().setMste_insured_no(detiledit.getPemegang().getMste_insured_no());
				detiledit.getTertanggung().setMste_standard(detiledit.getPemegang().getMste_standard());
				detiledit.getTertanggung().setNo_hp(detiledit.getPemegang().getNo_hp());
				detiledit.getTertanggung().setNo_hp2(detiledit.getPemegang().getNo_hp2());
				detiledit.getTertanggung().setReg_spaj(detiledit.getPemegang().getReg_spaj());
				detiledit.getTertanggung().setTelpon_kantor(detiledit.getPemegang().getTelpon_kantor());
				detiledit.getTertanggung().setTelpon_rumah(detiledit.getPemegang().getTelpon_rumah());
				detiledit.getTertanggung().setTujuana(detiledit.getPemegang().getTujuana());
				detiledit.getTertanggung().setTelpon_kantor2(detiledit.getPemegang().getTelpon_kantor2());
				detiledit.getTertanggung().setArea_code_kantor2(detiledit.getPemegang().getArea_code_kantor2());
				detiledit.getTertanggung().setTelpon_rumah2(detiledit.getPemegang().getTelpon_rumah2());
				detiledit.getTertanggung().setArea_code_rumah2(detiledit.getPemegang().getArea_code_rumah2());
				detiledit.getTertanggung().setNo_fax(detiledit.getPemegang().getNo_fax());
				detiledit.getTertanggung().setArea_code_fax(detiledit.getPemegang().getArea_code_fax());
				detiledit.getDatausulan().setLi_umur_ttg(detiledit.getPemegang().getMste_age());
				detiledit.getTertanggung().setUsiattg(detiledit.getPemegang().getMste_age());
				detiledit.getTertanggung().setLsag_id(detiledit.getPemegang().getLsag_id());				
				detiledit.getTertanggung().setMkl_red_flag(0);
				//detiledit.getTertanggung().setMste_age(detiledit.getPemegang().getMspo_age)
				detiledit.getTertanggung().setLsre_id_premi(detiledit.getPemegang().getLsre_id_premi());
				if(detiledit.getDatausulan().getLsbs_id()==120){
					detiledit.getTertanggung().setMcl_agama(detiledit.getPemegang().getMcl_agama());
					detiledit.getTertanggung().setNama_si(detiledit.getPemegang().getNama_si());
					detiledit.getTertanggung().setNama_anak1(detiledit.getPemegang().getNama_anak1());
					detiledit.getTertanggung().setNama_anak2(detiledit.getPemegang().getNama_anak2());
					detiledit.getTertanggung().setNama_anak3(detiledit.getPemegang().getNama_anak3());
					if (detiledit.getTertanggung().getMste_no_vacc()==null){
						detiledit.getTertanggung().setMste_no_vacc("");}		
					detiledit.getTertanggung().setBulan_gaji(detiledit.getPemegang().getBulan_gaji());
					detiledit.getTertanggung().setBulan_orangtua(detiledit.getPemegang().getBulan_orangtua());
					detiledit.getTertanggung().setBulan_penghasilan(detiledit.getPemegang().getBulan_penghasilan());
					detiledit.getTertanggung().setBulan_usaha(detiledit.getPemegang().getBulan_usaha());
					detiledit.getTertanggung().setBulan_usaha_note(detiledit.getPemegang().getBulan_usaha_note());
					detiledit.getTertanggung().setBulan_laba(detiledit.getPemegang().getBulan_laba());
					detiledit.getTertanggung().setBulan_lainnya(detiledit.getPemegang().getBulan_lainnya());
					detiledit.getTertanggung().setBulan_lainnya_note(detiledit.getPemegang().getBulan_lainnya_note());
					detiledit.getTertanggung().setTujuan_proteksi(detiledit.getPemegang().getTujuan_proteksi());
					detiledit.getTertanggung().setTujuan_pendidikan(detiledit.getPemegang().getTujuan_pendidikan());
					detiledit.getTertanggung().setTujuan_investasi(detiledit.getPemegang().getTujuan_investasi());
					detiledit.getTertanggung().setTujuan_tabungan(detiledit.getPemegang().getTujuan_tabungan());
					detiledit.getTertanggung().setTujuan_pensiun(detiledit.getPemegang().getTujuan_pensiun());
					detiledit.getTertanggung().setTujuan_lainnya(detiledit.getPemegang().getTujuan_lainnya());
				}else{
					detiledit.getTertanggung().setMcl_agama("LAIN-LAIN");
					detiledit.getTertanggung().setNama_si("");
					detiledit.getTertanggung().setNama_anak1("");
					detiledit.getTertanggung().setNama_anak2("");
					detiledit.getTertanggung().setNama_anak3("");	
					if (detiledit.getTertanggung().getMste_no_vacc()==null){
					detiledit.getTertanggung().setMste_no_vacc("");}
				}
				
				detiledit.getTertanggung().setJml_dth(0);
				detiledit.getTertanggung().setUsiattg(detiledit.getTertanggung().getMste_age());
				
			}else{
				if(detiledit.getDatausulan().getLscb_id()==120){
					detiledit.getTertanggung().setTujuana(detiledit.getPemegang().getMkl_tujuan());
					detiledit.getTertanggung().setDanaa(detiledit.getPemegang().getMkl_pendanaan());
					detiledit.getTertanggung().setKerjaa(detiledit.getPemegang().getMkl_kerja());
					detiledit.getTertanggung().setIndustria(detiledit.getPemegang().getMkl_industri());					
					detiledit.getTertanggung().setMkl_smbr_penghasilan("LAINNYA");
					detiledit.getTertanggung().setMkl_pendanaan(detiledit.getPemegang().getMkl_pendanaan());
					if(detiledit.getTertanggung().getNama_si()==null)detiledit.getTertanggung().setNama_si("");
					if(detiledit.getTertanggung().getNama_anak1()==null)detiledit.getTertanggung().setNama_anak1("");
					if(detiledit.getTertanggung().getNama_anak2()==null)detiledit.getTertanggung().setNama_anak2("");
					if(detiledit.getTertanggung().getNama_anak3()==null)detiledit.getTertanggung().setNama_anak3("");
					detiledit.getTertanggung().setShasil("-");
				}else{
					detiledit.getTertanggung().setTujuana("-");
					detiledit.getTertanggung().setDanaa("-");
					detiledit.getTertanggung().setKerjaa("-");
					detiledit.getTertanggung().setIndustria("-");
					detiledit.getTertanggung().setShasil("-");
					detiledit.getTertanggung().setMspe_no_identity_expired(commonDao.selectSysdate());	
					detiledit.getTertanggung().setMcl_agama("LAIN-LAIN");
					detiledit.getTertanggung().setNama_si("");
					detiledit.getTertanggung().setNama_anak1("");
					detiledit.getTertanggung().setNama_anak2("");
					detiledit.getTertanggung().setNama_anak3("");
					detiledit.getTertanggung().setKerjab("");
					detiledit.getTertanggung().setMkl_smbr_penghasilan("LAINNYA");
					if(detiledit.getPemegang().getMspo_flag_spaj()!=5){
						detiledit.getTertanggung().setMkl_pendanaan("LAINNYA");
						detiledit.getTertanggung().setMcl_agama("LAIN-LAIN");
						detiledit.getTertanggung().setLsag_id(6);
					}
				}
				detiledit.getContactPerson().setNama_lengkap("");				
				detiledit.getTertanggung().setMkl_dana_from(0);
				detiledit.getTertanggung().setMkl_red_flag(0);				
				detiledit.getPemegang().setMspo_follow_up(0);
				detiledit.getTertanggung().setLus_id(Integer.parseInt(user.getLus_id()));				
				detiledit.getTertanggung().setJml_dth(0);
				detiledit.getTertanggung().setUsiattg(detiledit.getTertanggung().getMste_age());
				String idPekerjaanTT =this.bacDao.selectIdLstPekerjaan(detiledit.getTertanggung().getMkl_kerja());
				detiledit.getTertanggung().setMkl_kerja(idPekerjaanTT);
				if (detiledit.getTertanggung().getMste_no_vacc()==null){
					detiledit.getTertanggung().setMste_no_vacc("");
				}
			}
		}
		
		Agen agen = new Agen();
	    agen = bacDao.select_detilagen3(detiledit.getPemegang().getMsag_id());
	    detiledit.setAgen(agen);
		detiledit.setAddressbilling(addrBillTemp);		
		if(addrBillTemp!=null){
			if(detiledit.getAddressbilling().getMsap_address().equals(detiledit.getPemegang().getAlamat_rumah())){
				detiledit.getAddressbilling().setTagih("2");
				detiledit.getAddressbilling().setMsap_zip_code(detiledit.getPemegang().getKd_pos_rumah());
			}else if(detiledit.getAddressbilling().getMsap_address().equals(detiledit.getPemegang().getAlamat_kantor())){
				detiledit.getAddressbilling().setTagih("3");
				detiledit.getAddressbilling().setMsap_zip_code(detiledit.getPemegang().getKd_pos_kantor());
			}else{
				detiledit.getAddressbilling().setTagih("1");
				detiledit.getAddressbilling().setMsap_zip_code(detiledit.getPemegang().getKd_pos_rumah());
			}
			detiledit.getAddressbilling().setLca_id(agen.getLca_id());
			detiledit.getAddressbilling().setLwk_id(agen.getLwk_id());
			detiledit.getAddressbilling().setLsrg_id(agen.getLsrg_id());
			detiledit.getAddressbilling().setRegion(agen.getKode_regional());
			detiledit.getAddressbilling().setMsap_zip_code(detiledit.getPemegang().getKd_pos_rumah());
		}
		
		if(planUtama!=null){
			detiledit.getDatausulan().setJenis_pemegang_polis(0);
			detiledit.getDatausulan().setFlag_worksite(0);
			detiledit.getDatausulan().setIsBonusTahapan(0);
			detiledit.getDatausulan().setIsBungaSimponi(0);
			detiledit.getDatausulan().setMste_gmit(0);
			detiledit.getDatausulan().setMste_insured_no(1);
			detiledit.getDatausulan().setMste_medical(0);
			detiledit.getDatausulan().setKode_flag(0);
			detiledit.getDatausulan().setFlag_account(0);
			detiledit.getDatausulan().setFlag_jenis_plan(0);
			detiledit.getDatausulan().setFlag_rider(0);
			detiledit.getDatausulan().setKurs_p(planUtama.getLku_id());
			detiledit.getDatausulan().setFlag_as(0);
			detiledit.getDatausulan().setJml_benef(0);
			if(detiledit.getDatausulan().getLsbs_id()==195 || detiledit.getDatausulan().getLsbs_id()==204){
				detiledit.getDatausulan().setFlag_hcp(1);
//				if(detiledit.getDatausulan().getLsbs_id()==195 && (detiledit.getDatausulan().getLsdbs_number() >= 61 || detiledit.getDatausulan().getLsdbs_number() <= 72)){
//					detiledit.getDatausulan().setFlag_account(2);
//				}
			}
			detiledit.getDatausulan().setLstp_id(13);
			detiledit.getDatausulan().setTipeproduk(13);
			if(detiledit.getDatausulan().getLsbs_id()==163){
				detiledit.getDatausulan().setFlag_rider(1);
			}
			
		/** tambahan dmtm btn SmartKidBTN Patar Timotius
		 * 208         29,30,31,32           SMART KID INSURANCE PLAN A (DMTM BTN)
		 *  **/
			
			boolean isSmartKidBTN = detiledit.getDatausulan().getLsbs_id() == 208 && (detiledit.getDatausulan().getLsdbs_number()==29 || detiledit.getDatausulan().getLsdbs_number()==30 || detiledit.getDatausulan().getLsdbs_number()==31 || detiledit.getDatausulan().getLsdbs_number()==32);
		
			/** tambahan dmtm bsim Simaskid Patar Timotius
			 * 208         33,34,35,36           SIMASKID BSIM (DMTM BSIM)
			 *  **/
				
			boolean isSimasKidBSIM = detiledit.getDatausulan().getLsbs_id() == 208 && (detiledit.getDatausulan().getLsdbs_number()==33 || detiledit.getDatausulan().getLsdbs_number()==34 || detiledit.getDatausulan().getLsdbs_number()==35 || detiledit.getDatausulan().getLsdbs_number()==36);
				
			
			/** tambahan dmtm bjb Smile kid Patar Timotius
			 * 208         45 - 48           SMILE KID BJB (DMTM BJB)
			 *  **/
			
			boolean isSmileKidBJB = detiledit.getDatausulan().getLsbs_id() == 208 && (detiledit.getDatausulan().getLsdbs_number() >= 45 && detiledit.getDatausulan().getLsdbs_number() <= 48) ;
			
			
			
			
			if(detiledit.getDatausulan().getLsbs_id()==120){
				detiledit.getDatausulan().setFlag_rider(1);
				detiledit.getDatausulan().setKode_flag(4);
				detiledit.getDatausulan().setDaftabenef(new ArrayList(bacDao.select_benef_temp(no_temp)));
				detiledit.getDatausulan().setJml_benef(detiledit.getDatausulan().getDaftabenef().size());
				detiledit.getDatausulan().setFlag_account(2);
				detiledit.getDatausulan().setMspo_flag_new(1);
			}else if(detiledit.getDatausulan().getLsbs_id()==163){
				detiledit.getDatausulan().setFlag_rider(0);
				detiledit.getDatausulan().setKode_flag(0);
				detiledit.getDatausulan().setDaftabenef(new ArrayList(bacDao.select_benef_temp(no_temp)));
				detiledit.getDatausulan().setJml_benef(detiledit.getDatausulan().getDaftabenef().size());
				detiledit.getDatausulan().setFlag_account(2);
				detiledit.getDatausulan().setMspo_flag_new(1);
			}else if(detiledit.getDatausulan().getLsbs_id()==212 && 
					(detiledit.getDatausulan().getLsdbs_number()==4 || 
					 detiledit.getDatausulan().getLsdbs_number()==5 || 
					 detiledit.getDatausulan().getLsdbs_number()==7 ||
					 detiledit.getDatausulan().getLsdbs_number()==1 || 		/** Nana Add lsdbs number 1  case tidak menyimpan benefiary */
					 detiledit.getDatausulan().getLsdbs_number()==12 ||     /** Nana Add lsdbs number 12 case tidak menyimpan benefiary */
					 detiledit.getDatausulan().getLsdbs_number()==13 )){    /** Nana Add lsdbs number 13 case tidak menyimpan benefiary */ 
				detiledit.getDatausulan().setFlag_rider(0);
				detiledit.getDatausulan().setKode_flag(0);
				detiledit.getDatausulan().setDaftabenef(new ArrayList(bacDao.select_benef_temp(no_temp)));
				detiledit.getDatausulan().setJml_benef(detiledit.getDatausulan().getDaftabenef().size());
				detiledit.getDatausulan().setFlag_account(2);
				detiledit.getDatausulan().setMspo_flag_new(1);
				/** tambahin sini patar timotius **/
			}else if(detiledit.getDatausulan().getLsbs_id() == 208 && (detiledit.getDatausulan().getLsdbs_number()==1)){
				detiledit.getDatausulan().setFlag_rider(0);
				detiledit.getDatausulan().setKode_flag(0);
				detiledit.getDatausulan().setDaftabenef(new ArrayList(bacDao.select_benef_temp(no_temp)));
				detiledit.getDatausulan().setJml_benef(detiledit.getDatausulan().getDaftabenef().size());
				detiledit.getDatausulan().setFlag_account(2);
				detiledit.getDatausulan().setMspo_flag_new(1);
			}else if(isSmartKidBTN || isSmileKidBJB){
				detiledit.getDatausulan().setFlag_rider(0);
				detiledit.getDatausulan().setKode_flag(0);
				detiledit.getDatausulan().setDaftabenef(new ArrayList(bacDao.select_benef_temp(no_temp)));
				detiledit.getDatausulan().setJml_benef(detiledit.getDatausulan().getDaftabenef().size());
				detiledit.getDatausulan().setFlag_account(2);
				detiledit.getDatausulan().setMspo_flag_new(1);
			}else if(isSimasKidBSIM){
				detiledit.getDatausulan().setFlag_rider(0);
				detiledit.getDatausulan().setKode_flag(0);
				detiledit.getDatausulan().setDaftabenef(new ArrayList(bacDao.select_benef_temp(no_temp)));
				detiledit.getDatausulan().setJml_benef(detiledit.getDatausulan().getDaftabenef().size());
				detiledit.getDatausulan().setFlag_account(2);
				detiledit.getDatausulan().setMspo_flag_new(1);
			}
		}
		
		if(!planRider.isEmpty()){
			detiledit.getDatausulan().setJmlrider(plan_rider);
			detiledit.getDatausulan().setDaftaRider(planRider);
			detiledit.setPowersave(new Powersave());
		}
		
		if(acrTemp!=null){
			detiledit.getAccount_recur().setMar_acc_no_split(FormatString.splitWordToCharacter(detiledit.getAccount_recur().getMar_acc_no(),21));
			detiledit.getAccount_recur().setMar_active(1);
			if(detiledit.getDatausulan().getLsbs_id()!=163){				
				detiledit.getAccount_recur().setFlag_autodebet_nb(0);
				detiledit.getAccount_recur().setFlag_jn_tabungan(0);
				detiledit.getAccount_recur().setFlag_set_auto(0);
			}
		}
		
		//untuk produk BTN Flag_Auto_debet_nb = 1 sehingga bisa proses ke finance
		// bank jatim juga flag auto debet nb = 1 juga
		if(detiledit.getAgen().getLca_id().equals("40") && detiledit.getAgen().getLwk_id().equals("01") && (detiledit.getAgen().getLsrg_id().equals("04")||detiledit.getAgen().getLsrg_id().equals("11") || detiledit.getAgen().getLsrg_id().equals("12") || detiledit.getAgen().getLsrg_id().equals("13") )){
			detiledit.getAccount_recur().setFlag_autodebet_nb(1);
		}
		
		if(peserta_plus!=null){			
			detiledit.getDatausulan().setDaftarplus(peserta_plus);
			detiledit.getDatausulan().setDaftarplus_mix(peserta_plus);
		}
		
		if(agen!=null){
			detiledit.getAgen().setJenis_ref(2);
		}
		
		if(p_premiTemp!=null){
			detiledit.setPembayarPremi(p_premiTemp);
			if(detiledit.getPembayarPremi().getLsre_id_premi().equals("40")){
				detiledit.getPembayarPremi().setAda_pihak_ketiga("0");
				detiledit.getPembayarPremi().setPendapatanBulan(detiledit.getPemegang().getPendapatanBulan());
				detiledit.getPembayarPremi().setTujuanInvestasi(detiledit.getPemegang().getTujuanInvestasi());
				detiledit.getPembayarPremi().setPendapatanTahun(detiledit.getPemegang().getPendapatanBulan());
				detiledit.getPembayarPremi().setTotal_rutin("< RP. 5 JUTA");
				detiledit.getPembayarPremi().setTotal_non_rutin("< RP. 5 JUTA");			
				if(detiledit.getPembayarPremi().getMkl_kerja_other_radio()==null)detiledit.getPembayarPremi().setMkl_kerja_other_radio("0");
			}
		}else{
			detiledit.setPembayarPremi(new PembayarPremi());
		}
		
		if(rekClient!=null){
			detiledit.setRekening_client(rekClient);
		}
		detiledit.setCurrentUser(user);	
		detiledit.setInvestasiutama(new InvestasiUtama());
		if(invTemp!=null){
			detiledit.setInvestasiutama(invTemp);
			ArrayList<Biayainvestasi> daftarBiaya=new ArrayList();
			ArrayList<DetilInvestasi> daftarInvestasi=new ArrayList();
			
			for(int i=0; i<biayaInv.size(); i++) {
				Biayainvestasi bi=new Biayainvestasi();
				HashMap mapBiaya=new HashMap();
				mapBiaya=(HashMap)biayaInv.get(i);
				
				bi.setLjb_biaya((String)mapBiaya.get("LJB_BIAYA"));
				if(((BigDecimal)mapBiaya.get("MU_KE")).toString()!=null)bi.setMu_ke(((BigDecimal)mapBiaya.get("MU_KE")).intValue());
				bi.setLjb_id(((BigDecimal)mapBiaya.get("LJB_ID")).intValue());
				if(((BigDecimal)mapBiaya.get("MBU_JUMLAH"))!=null)bi.setMbu_jumlah(((BigDecimal)mapBiaya.get("MBU_JUMLAH")).doubleValue());
				if(((BigDecimal)mapBiaya.get("MBU_PERSEN"))!=null)bi.setMbu_persen(((BigDecimal)mapBiaya.get("MBU_PERSEN")).doubleValue());
				SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy");
				String tanggal=((String)mapBiaya.get("MBU_END_PAY"));
				if(tanggal!=null)	bi.setMbu_end_pay(df.parse(tanggal));				
				daftarBiaya.add(bi);
			}
			
			detiledit.getInvestasiutama().setDaftartopup(topUp);
			detiledit.getInvestasiutama().setDaftarbiaya(daftarBiaya);
			detiledit.getInvestasiutama().setDaftarinvestasi(daftarInvestasi);
			detiledit.getInvestasiutama().getDaftartopup().setRedFlag_topup_berkala(0);
			if(topUp.getPremi_berkala()>0)detiledit.getDatausulan().setLi_trans_berkala(5);
			if(topUp.getPremi_tunggal()>0)detiledit.getDatausulan().setLi_trans_tunggal(2);
			for(int i=0; i<detilInv.size(); i++) {
				DetilInvestasi di=new DetilInvestasi();
				HashMap mapDetilInvest=new HashMap();
				mapDetilInvest=(HashMap)detilInv.get(i);
				di.setLji_id1((String)mapDetilInvest.get("LJI_ID1"));
				di.setLji_invest1((String)mapDetilInvest.get("LJI_INVEST1"));
				if(((BigDecimal)mapDetilInvest.get("MDU_PERSEN1"))!=null)di.setMdu_persen1(((BigDecimal)mapDetilInvest.get("MDU_PERSEN1")).intValue());
				//if(((BigDecimal)mapDetilInvest.get("MDU_JUMLAH_TOP"))!=null)di.setMdu_jumlah1(((BigDecimal)mapDetilInvest.get("MDU_JUMLAH1")).doubleValue());
				if(((BigDecimal)mapDetilInvest.get("MDU_JUMLAH1"))!=null)di.setMdu_jumlah_top(((BigDecimal)mapDetilInvest.get("MDU_JUMLAH1")).doubleValue());
				//if(((BigDecimal)mapDetilInvest.get("MDU_JUMLAH_TOP_TUNGGAL"))!=null)di.setMdu_jumlah_top_tunggal(((BigDecimal)mapDetilInvest.get("MDU_JUMLAH_TOP_TUNGGAL")).doubleValue());
				if(((BigDecimal)mapDetilInvest.get("MU_KE1"))!=null)di.setMu_ke1(((BigDecimal)mapDetilInvest.get("MU_KE1")).intValue());
				daftarInvestasi.add(di);
			}
			detiledit.getInvestasiutama().setDaftarbiaya(daftarBiaya);
			detiledit.getInvestasiutama().setDaftarinvestasi(daftarInvestasi);
		}
		
		if(detiledit.getPemegang().getMspo_flag_spaj()==4 || detiledit.getPemegang().getMspo_flag_spaj()==5){
			String [] pendapatanRutinBulanPP = {
					f_validasi.gantiKata(detiledit.getPemegang().getBulan_gaji()),
					f_validasi.gantiKata(detiledit.getPemegang().getBulan_penghasilan()),
					f_validasi.gantiKata(detiledit.getPemegang().getBulan_orangtua()),
					f_validasi.gantiKata(detiledit.getPemegang().getBulan_usaha()+";"+f_validasi.gantiKata(detiledit.getPemegang().getBulan_usaha_note())),
					f_validasi.gantiKata(detiledit.getPemegang().getBulan_laba()),
					f_validasi.gantiKata(detiledit.getPemegang().getBulan_lainnya()+";"+f_validasi.gantiKata(detiledit.getPemegang().getBulan_lainnya_note()))
			};
			
			String [] tujuanInvestasiPP = {
					f_validasi.gantiKata(detiledit.getPemegang().getTujuan_proteksi()),
					f_validasi.gantiKata(detiledit.getPemegang().getTujuan_pendidikan()),
					f_validasi.gantiKata(detiledit.getPemegang().getTujuan_investasi()),
					f_validasi.gantiKata(detiledit.getPemegang().getTujuan_tabungan()),
					f_validasi.gantiKata(detiledit.getPemegang().getTujuan_pensiun()),
					f_validasi.gantiKata(detiledit.getPemegang().getTujuan_lainnya()+";"+f_validasi.gantiKata(detiledit.getPemegang().getTujuan_lainnya_note()))
			};
			
			detiledit.getPemegang().setPendapatanBulan(pendapatanRutinBulanPP);
			detiledit.getPemegang().setTujuanInvestasi(tujuanInvestasiPP);
			
			String [] pendapatanRutinBulanTT = {
					f_validasi.gantiKata(detiledit.getTertanggung().getBulan_gaji()),
					f_validasi.gantiKata(detiledit.getTertanggung().getBulan_penghasilan()),
					f_validasi.gantiKata(detiledit.getTertanggung().getBulan_orangtua()),
					f_validasi.gantiKata(detiledit.getTertanggung().getBulan_usaha()+";"+f_validasi.gantiKata(detiledit.getTertanggung().getBulan_usaha_note())),
					f_validasi.gantiKata(detiledit.getTertanggung().getBulan_laba()),
					f_validasi.gantiKata(detiledit.getTertanggung().getBulan_lainnya()+";"+f_validasi.gantiKata(detiledit.getTertanggung().getBulan_lainnya_note()))
			};
			
			String [] tujuanInvestasiTT = {
					f_validasi.gantiKata(detiledit.getTertanggung().getTujuan_proteksi()),
					f_validasi.gantiKata(detiledit.getTertanggung().getTujuan_pendidikan()),
					f_validasi.gantiKata(detiledit.getTertanggung().getTujuan_investasi()),
					f_validasi.gantiKata(detiledit.getTertanggung().getTujuan_tabungan()),
					f_validasi.gantiKata(detiledit.getTertanggung().getTujuan_pensiun()),
					f_validasi.gantiKata(detiledit.getTertanggung().getTujuan_lainnya()+";"+f_validasi.gantiKata(detiledit.getTertanggung().getTujuan_lainnya_note()))
			};
			
			detiledit.getTertanggung().setPendapatanBulan(pendapatanRutinBulanTT);
			detiledit.getTertanggung().setTujuanInvestasi(tujuanInvestasiTT);
			
			if(detiledit.getPembayarPremi().getLsre_id_premi().equals("40")){			
				detiledit.getPembayarPremi().setPendapatanBulan(detiledit.getPemegang().getPendapatanBulan());
				detiledit.getPembayarPremi().setTujuanInvestasi(detiledit.getPemegang().getTujuanInvestasi());
				detiledit.getPembayarPremi().setPendapatanTahun(detiledit.getPemegang().getPendapatanBulan());
			}
		}
		
		detiledit.getPemegang().setMspo_jenis_terbit(0);
		String nama = detiledit.getTertanggung().getMcl_first();
		Date tgl_lahir_ttg=detiledit.getTertanggung().getMspe_date_birth();
		String tgl = (Integer.toString(tgl_lahir_ttg.getYear()+1900))+(FormatString.rpad("0",Integer.toString(tgl_lahir_ttg.getMonth()+1),2))+(FormatString.rpad("0",Integer.toString(tgl_lahir_ttg.getDate()),2));
		Double p = detiledit.getDatausulan().getMspr_premium();
		String lsbs_id = detiledit.getDatausulan().getLsbs_id().toString();
		String lsbs_3digit = FormatString.rpad("0", lsbs_id, 3);
		String lsdbs_number = detiledit.getDatausulan().getLsdbs_number().toString();
		String site = detiledit.getAgen().getLca_id().toString().concat(detiledit.getAgen().getLwk_id().toString().concat(detiledit.getAgen().getLsrg_id().toString()));
		Integer jumlah_dobel = (Integer) this.bacDao.cek_polis_dobel(nama, tgl, lsbs_id, lsdbs_number, p , site);		
		
		
		/**
		 * Smart kid BTN DMTM 
		 * Patar Timotius
		 * 2018/08/08
		 */
		
		
		boolean isSmartKidBTNII = (detiledit.getDatausulan().getLsbs_id()==208 && (detiledit.getDatausulan().getLsdbs_number()==01||detiledit.getDatausulan().getLsdbs_number()==29||detiledit.getDatausulan().getLsdbs_number()==30||detiledit.getDatausulan().getLsdbs_number()==31||detiledit.getDatausulan().getLsdbs_number()==32));
		
		/**
		 * Simas kid BSIM DMTM
		 * Patar Timotius
		 * 2018/08/08
		 */
		
		boolean isSimasKidBSIMII = (detiledit.getDatausulan().getLsbs_id()==208 && (detiledit.getDatausulan().getLsdbs_number()==33||detiledit.getDatausulan().getLsdbs_number()==34||detiledit.getDatausulan().getLsdbs_number()==35||detiledit.getDatausulan().getLsdbs_number()==36));
		
		
		
		if (jumlah_dobel.intValue()<=0){
			if( (( "118,120,163,169,183,190,195,197,212,214,221,225".indexOf(lsbs_3digit)>=0 ||
				(detiledit.getDatausulan().getLsbs_id()==189 && (detiledit.getDatausulan().getLsdbs_number()>=33 && detiledit.getDatausulan().getLsdbs_number()<=47)) ||
				(detiledit.getDatausulan().getLsbs_id()==189 && (detiledit.getDatausulan().getLsdbs_number()>=48 && detiledit.getDatausulan().getLsdbs_number()<=62)) || //helpdesk [133975] produk baru 189 48-62 Smile Medical Syariah BSIM
				(detiledit.getDatausulan().getLsbs_id()==204 && (detiledit.getDatausulan().getLsdbs_number()>=37 && detiledit.getDatausulan().getLsdbs_number()<=48)) ||
				(detiledit.getDatausulan().getLsbs_id()==203 && detiledit.getDatausulan().getLsdbs_number()==4) ||
				(detiledit.getDatausulan().getLsbs_id()==73 && detiledit.getDatausulan().getLsdbs_number()==15) ||
				(detiledit.getDatausulan().getLsbs_id()==163 && (detiledit.getDatausulan().getLsdbs_number()>=21 && detiledit.getDatausulan().getLsdbs_number()<=25)) || //helpdesk [150296] DMTM BSIM 163 21-25 tambah simple questionare SIO+
/** tambahin sini patar timotius
 * BTN 29,30,31,32
 * */				isSmartKidBTNII || isSimasKidBSIMII
				) &&
				detiledit.getPemegang().getMspo_flag_spaj()==4) || detiledit.getPemegang().getMspo_flag_spaj()==5 
				
					
					){
				detiledit=savingBacSpajNew.insertspajbaru(detiledit, user);
			}else{
				detiledit=savingBac.insertspajbaru(detiledit, user);
			}
			
			if(detiledit.getPemegang().getReg_spaj()!=null && !detiledit.getPemegang().getReg_spaj().equals("")){
				List<Map> reff = bacDao.selectReffTm(no_temp);
				if(!reff.isEmpty()){
					Map mapReff= (Map)reff.get(0);
					String no_kerjasama=(String)mapReff.get("TM_CODE");
					String spv=(String)mapReff.get("SPV_CODE");
					String app=(String)mapReff.get("APPLICATION_ID_DMTM");
					String reff_tm=(String)mapReff.get("REFF_TM_ID");
					bacDao.updateReffTm(detiledit.getPemegang().getReg_spaj(),spv,no_kerjasama,app,reff_tm);
					
					if(detiledit.getDatausulan().getLsbs_id()==163){
//					    int setNopol = 0;
//						Akseptasi akseptasi = new Akseptasi();
//						akseptasi.setSpaj(detiledit.getPemegang().getReg_spaj());
//						akseptasi.setLsbsId(detiledit.getDatausulan().getLsbs_id()) ;
//						akseptasi.setLsdbsNumber(detiledit.getDatausulan().getLsdbs_number() );
//						String lca_id = uwDao.selectLcaIdMstPolicyBasedSpaj(detiledit.getPemegang().getReg_spaj());
//						akseptasi.setLcaId(lca_id);
//						setNopol=uwDao.wf_set_nopol(akseptasi, 1);					
//						Policy policy=uwDao.selectDw1Underwriting(akseptasi.getSpaj(),1,1);
//						ArrayList lsDp=Common.serializableList(uwDao.selectMstDepositPremium(akseptasi.getSpaj(),new Integer(1)));
//						BindException err = null;
//						if(uwDao.wf_ins_bill(detiledit.getPemegang().getReg_spaj(),1,new Integer(1),akseptasi.getLsbsId(),akseptasi.getLsdbsNumber(),56,1,lsDp,detiledit.getCurrentUser().getLus_id(),policy,err)){
//							uwDao.updateMstInsured(detiledit.getPemegang().getReg_spaj(),118);
//							uwDao.updateMstPolicy(detiledit.getPemegang().getReg_spaj(),118);	
//							uwDao.insertMstPositionSpaj(detiledit.getCurrentUser().getLus_id(), "Transfer Proses AutoDebet Ke Finance", detiledit.getPemegang().getReg_spaj(), 0);
//							uwDao.saveMstTransHistory(detiledit.getPemegang().getReg_spaj(), "tgl_transfer_autodebet_nb", null, null, null);
//						}
						if(detiledit.getPemegang().getFlag_free_pa() == null)
							detiledit.getPemegang().setFlag_free_pa(0);
							
						if(detiledit.getPemegang().getFlag_free_pa()==1){
							Pas pas = new Pas();
							pas.setPremi("0");
							pas.setMsp_premi("0");
							pas.setInput_type("typeafree");
							pas.setMsp_flag_cc(0);
							pas.setPribadi(0);
							pas.setLscb_id(3);
							pas.setLsbp_id("156");
							pas.setLsbp_nama("BANK SINARMAS");
							pas.setLsbp_nama_autodebet("BANK SINARMAS");
							Date end_date = commonDao.selectSysdate();
							end_date.setYear(end_date.getYear()+1);
							end_date.setDate(end_date.getDate()-1);							
							pas.setMsp_pas_beg_date(commonDao.selectSysdate());
							pas.setMsp_pas_end_date(end_date);
							pas.setLus_id(Integer.parseInt(user.getLus_id()));
							pas.setLus_login_name(user.getLus_full_name());						
							pas.setJenis_pas("PABSM");
							pas.setLsre_id(1);
							pas.setEmailTo(detiledit.getPemegang().getEmail());
							pas.setMcl_first(detiledit.getPemegang().getMcl_first());
							pas.setProduk(1);
							pas.setLsre_id(1);
							pas.setMsp_sex_pp(detiledit.getPemegang().getMspe_sex());
							pas.setMsp_age(detiledit.getPemegang().getMspo_age());
							pas.setMsp_address_1(detiledit.getAddressbilling().getMsap_address());
							pas.setMsp_city(detiledit.getAddressbilling().getKota());
							pas.setMsp_date_of_birth(detiledit.getPemegang().getMspe_date_birth());
							pas.setMsp_full_name(detiledit.getPemegang().getMcl_first());
							pas.setMsp_id("");
							pas.setMsp_identity_no(detiledit.getPemegang().getMspe_no_identity());
							pas.setMsp_identity_no_tt(pas.getMsp_identity_no());
							pas.setMsp_mobile(detiledit.getPemegang().getNo_hp());
							pas.setMsp_no_rekening(detiledit.getRekening_client().getMrc_no_ac());
							pas.setMsp_pas_dob_pp(detiledit.getPemegang().getMspe_date_birth());
							pas.setMsp_pas_email(detiledit.getPemegang().getMspe_email());
							pas.setMsp_pas_nama_pp(detiledit.getPemegang().getMcl_first());
							pas.setMsp_pas_phone_number(detiledit.getAddressbilling().getMsap_phone1());
							pas.setMsp_pas_tmp_lhr_pp(detiledit.getPemegang().getMspe_place_birth());
							pas.setMsp_pas_tmp_lhr_tt(detiledit.getPemegang().getMspe_place_birth());
							pas.setMsp_postal_code(detiledit.getAddressbilling().getMsap_zip_code());
							pas.setMsp_rek_cabang(detiledit.getRekening_client().getMrc_cabang());
							pas.setMsp_rek_kota(detiledit.getRekening_client().getMrc_kota());
							pas.setMsp_rek_nama(detiledit.getRekening_client().getMrc_nama());
							pas.setMsp_up("50000000");
							pas.setPanjang_no_rek(10);
							uwDao.insertPaTemp(pas, user);
						}
					}
				}
			}else{
				pesan = "GAGAL INPUT";
			}
		}else{
			pesan = "DATA INI SUDAH PERNAH DIINPUT";
		}
		return pesan;
	}	
}
	