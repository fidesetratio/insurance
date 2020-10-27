package com.ekalife.elions.web.bac.support;

import id.co.sinarmaslife.std.spring.util.Email;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import produk_asuransi.n_prod;

import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Hcp;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.PesertaPlus;
import com.ekalife.elions.model.PesertaPlus_mix;
import com.ekalife.elions.model.Simas;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.Products;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.f_validasi;

/**
 * @author HEMILDA
 * validator editbacController
 * untuk validasi penginputan bac
 */

public class Editduvalidator implements Validator{

	protected final Log logger = LogFactory.getLog(getClass());
	
	private ElionsManager elionsManager;
	private UwManager uwManager;
	private BacManager bacManager;
	private DateFormat defaultDateFormat;
	private Email email;
	private Products products;
	
	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	public void setEmail(Email email) {
		this.email = email;
	}
	
	public void setDefaultDateFormat(DateFormat defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
	}

	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}
	
	public void setUwManager(UwManager uwManager) {
		this.uwManager = uwManager;
	}
	
	public void setBacManager(BacManager bacManager) {
		this.bacManager = bacManager;
	}

	public boolean supports(Class data) {
		return Cmdeditbac.class.isAssignableFrom(data);
	}

	public void validate(Object cmd, Errors err) {
	}
	
	//-------------------------data usulan v.2----------------------------------------
	public void validateddu2(Object cmd, Errors err, Integer sp)  throws ServletException,IOException,Exception
	{
		logger.debug("EditBacValidator : validate page validateddu2");
		Cmdeditbac edit= (Cmdeditbac) cmd;
		f_validasi data = new f_validasi();
		NumberFormat f = new DecimalFormat("#,##0.00;(#,##0.00)");
		String hasil_kombinasi;
		int pmode_id = edit.getDatausulan().getLscb_id();
		if(!edit.getCurrentUser().getCab_bank().equals("") && edit.getCurrentUser().getJn_bank().intValue() == 2) {
			if((edit.getDatausulan().getLsbs_id().intValue()==142&& edit.getDatausulan().getLsdbs_number().intValue()==2) || (edit.getDatausulan().getLsbs_id().intValue()==158 && edit.getDatausulan().getLsdbs_number().intValue()==6)){
				edit.getAgen().setMsag_id("016409");
			}else if(edit.getDatausulan().getLsbs_id().intValue()==164&& (edit.getDatausulan().getLsdbs_number().intValue()==2 || edit.getDatausulan().getLsdbs_number().intValue()==11)){
				edit.getAgen().setMsag_id("021052");
			}else if(edit.getDatausulan().getLsbs_id().intValue()==120&& edit.getDatausulan().getLsdbs_number().intValue()==2){
				edit.getAgen().setMsag_id("901327");
			}else if(edit.getDatausulan().getLsbs_id().intValue()==111&& edit.getDatausulan().getLsdbs_number().intValue()==2){
				edit.getAgen().setMsag_id("901328");
			}
		}
		
		if(edit.getDatausulan().isPsave || edit.getPowersave().getMsl_spaj_lama()!=null || edit.getDatausulan().isPlatinumSave){
			String lsbs_id_kopi = uwManager.selectLsbsId(edit.getPowersave().getMsl_spaj_lama());
			String lsdbs_number_kopi = uwManager.selectLsdbsNumber(edit.getPowersave().getMsl_spaj_lama());
			if(products.stableLink(edit.getDatausulan().getLsbs_id().toString())){
				if(products.powerSave(lsbs_id_kopi)){
					edit.getDatausulan().setConvert(1);
				}
			}else if(!products.stableLink(edit.getDatausulan().getLsbs_id().toString())){
				if(edit.getDatausulan().getLsbs_id()==155 || (edit.getDatausulan().getLsbs_id()==158 && (edit.getDatausulan().getLsdbs_number()==5 || (edit.getDatausulan().getLsdbs_number()>=8 && edit.getDatausulan().getLsdbs_number()<=12) ) ) ){
					if(lsbs_id_kopi.equals("155") || (lsbs_id_kopi.equals("158") && (lsdbs_number_kopi.equals("5") || (Integer.parseInt(lsdbs_number_kopi)>=8 && Integer.parseInt(lsdbs_number_kopi)<=12)))){
						edit.getDatausulan().setConvert(2);
					}else{
						edit.getDatausulan().setConvert(0);
					}
				}else{
					edit.getDatausulan().setConvert(0);
				}
			}
		}
		
		if(edit.getDatausulan().getLsbs_id().intValue()==186&& edit.getDatausulan().getLsdbs_number().intValue()==3){
			edit.getDatausulan().setMspr_ins_period(5);
			edit.getDatausulan().setMspo_pay_period(5);
		}
		//logger.info(edit.getPowersave().getMsl_employee());
		if(!Common.isEmpty(edit.getPowersave().getMsl_employee())){  			
			if(edit.getPowersave().getMsl_employee()==1){
				logger.info(edit.getPowersave().getMsl_employee());
				edit.getPowersave().setMps_employee(1);
				logger.info(edit.getPowersave().getMsl_employee());
			}
		}
		
		if(edit.getCurrentUser().getLca_id().equals("58")){
			if(edit.getDatausulan().getMste_flag_cc()==null){
				edit.getDatausulan().setMste_flag_cc(1);
			}
		}
		
		if(edit.getTertanggung().getMste_dth()==null){
			edit.getTertanggung().setMste_dth(0);
		}
		
		//khusus cerdas mall packet
		if((edit.getDatausulan().getLsbs_id().intValue()==129 || edit.getDatausulan().getLsbs_id().intValue()==120) && (edit.getDatausulan().getFlag_paket()!=0)){
			if("7,8,9,10,11,12,13,14,15,16,17,18".indexOf(edit.getDatausulan().getFlag_paket().toString())>-1 ){
				if(("C,F".indexOf(edit.getDatausulan().getKombinasi().toString())<0)){ // *Kombinasi untuk packet cerdas Pension
					hasil_kombinasi="Kombinasi premi untuk paket hanya boleh 80% PP-20% PTB dan 50% PP-50% PTB.";
					edit.getDatausulan().setTotal_premi_kombinasi(new Double(0));
					err.rejectValue("datausulan.kombinasi", "", "HALAMAN DATA USULAN : "+hasil_kombinasi);
				}
			}else if("24,25,26".indexOf(edit.getDatausulan().getFlag_paket().toString())>-1 ){
				if(("C".indexOf(edit.getDatausulan().getKombinasi().toString())<0)){ // *Kombinasi untuk packet SP
					hasil_kombinasi="Kombinasi premi untuk paket hanya boleh 80% PP-20% PTB.";
					edit.getDatausulan().setTotal_premi_kombinasi(new Double(0));
					err.rejectValue("datausulan.kombinasi", "", "HALAMAN DATA USULAN : "+hasil_kombinasi);
				}
			}
		}
		
		//LUFI--validasi up untuk Simpol protector
		if("24,25,26".indexOf(edit.getDatausulan().getFlag_paket().toString())>-1){
			if(edit.getDatausulan().getFlag_paket().intValue()==24 && edit.getDatausulan().getMspr_tsi().doubleValue()!=100000000){
				err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Uang Pertanggungan Untuk Simpol Protector Gold adalah 100 Juta" );
			}else if(edit.getDatausulan().getFlag_paket().intValue()==25 &&  edit.getDatausulan().getMspr_tsi().doubleValue()!=200000000){
				err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :Uang Pertanggungan Untuk Simpol Protector Titanium adalah 200 Juta" );
			}else if(edit.getDatausulan().getFlag_paket().intValue()==26 &&  edit.getDatausulan().getMspr_tsi().doubleValue()!=300000000){
				err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :Uang Pertanggungan Untuk Simpol Protector Platinum adalah 300 Juta" );
			}
		}
		
		if("24,25,26".indexOf(edit.getDatausulan().getFlag_paket().toString())>-1){
			if(edit.getDatausulan().getFlag_paket().intValue()==24 && edit.getInvestasiutama().getTotal_premi_sementara()!=100000000){
				err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Premi Untuk Simpol Protector Gold adalah 100 Juta" );
			}else if(edit.getDatausulan().getFlag_paket().intValue()==25 && edit.getInvestasiutama().getTotal_premi_sementara()!=200000000){
				err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :Premi Untuk Simpol Protector Titanium adalah 200 Juta" );
			}else if(edit.getDatausulan().getFlag_paket().intValue()==26 && edit.getInvestasiutama().getTotal_premi_sementara()!=300000000){
				err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :Premi Untuk Simpol Protector Platinum adalah 300 Juta" );
			}
		}
		
		//(Deddy)Untuk Ekasarjana mandiri dan ProLife : Cara Bayar Bulanan, harus melalui pemotongan gaji(PAYROLL)
		if(edit.getDatausulan().getLsbs_id().intValue() == 173 || edit.getDatausulan().getLsbs_id().intValue() == 179){
			
			if(pmode_id==6 && edit.getDatausulan().getMste_flag_cc()!=3){
				err.rejectValue("datausulan.mste_flag_cc","","HALAMAN DATA USULAN : Untuk cara Bayar Bulanan, bentuk pembayaran premi dilakukan melalui pemotongan gaji(PAYROLL).");
			}
		}
		//untuk cara bayar bulanan, harus autodebet/kartu kredit -ryan		
		if(edit.getDatausulan().getLscb_id().intValue()==6){
			if(!edit.getCurrentUser().getLus_id().equals("2326")){
				if(edit.getDatausulan().getMste_flag_cc()==null || edit.getDatausulan().getMste_flag_cc()==0 && sp!=1 && !(edit.getDatausulan().getLsbs_id()==190 && "5,6".indexOf(edit.getDatausulan().getLsdbs_number().toString())>0) ){
					err.rejectValue("datausulan.mste_flag_cc","","HALAMAN DATA USULAN : Untuk cara Bayar Bulanan, bentuk pembayaran premi harus AUTODEBET/KARTU KREDIT.");
				}
			}
		}
		//Chandra A - 20180427 - Copy - untuk cara bayar bulanan, harus autodebet/kartu kredit -ryan		
		if(edit.getDatausulan().getLscb_id().intValue()==6){
			if(!edit.getCurrentUser().getLus_id().equals("2326")){
				if(edit.getDatausulan().getMste_flag_cc()==null || edit.getDatausulan().getMste_flag_cc()==0 && sp!=1 && !(edit.getDatausulan().getLsbs_id()==212 && edit.getDatausulan().getLsdbs_number()==9) ){
					err.rejectValue("datausulan.mste_flag_cc","","HALAMAN DATA USULAN : Untuk cara Bayar Bulanan, bentuk pembayaran premi harus AUTODEBET/KARTU KREDIT.");
				}
			}
		}
		//Khusus produk Multi Invest bsim cara pembayaran hanya autodebet/tabungan--lufi
		if(edit.getDatausulan().getLsbs_id().intValue()==182 && (edit.getDatausulan().getLsdbs_number()==13 ||edit.getDatausulan().getLsdbs_number()==14 || edit.getDatausulan().getLsdbs_number()==15 && sp!=1)){
			if(edit.getDatausulan().getMste_flag_cc()!=2 && edit.getDatausulan().getMste_flag_cc()!=1 && sp!=1 ){
				err.rejectValue("datausulan.mste_flag_cc","","HALAMAN DATA USULAN : Untuk Produk ini bentuk pembayaran harus auto debet ");
			}
		}	
		
		
		//Anta - Untuk PowerSave Syariah BSIM hanya dapat dilakukan dalam bentuk pembayaran TUNAI
		//dan juga untuk seluruh Produk Powersave dan Stable 
		//if(kode_produk.intValue() == 175 && number_produk == 2){
		if((products.powerSave(edit.getDatausulan().getLsbs_id().toString()) || products.stableLink(edit.getDatausulan().getLsbs_id().toString())) && edit.getDatausulan().getLsbs_id().intValue() != 186){
			String x="";
			if (edit.getDatausulan().getMste_flag_cc()==null){
				x="99";
			}
			
			
			if( (x.equals("99")|| edit.getDatausulan().getMste_flag_cc() != 0 ) && edit.getDatausulan().getMste_flag_el()!=1 && sp!=1 && edit.getDatausulan().getLsbs_id()!=177)
				err.rejectValue("datausulan.mste_flag_cc","","HALAMAN DATA USULAN : Untuk PLAN ini, bentuk pembayaran premi hanya dapat dilakukan secara TUNAI.");
			
		}
		
		//Anta - Untuk Exccellent Link untuk cara bayar BUL,TRI,SEM,&TAH dilakukan dalam bentuk pembayaran TABUNGAN/SAVEBAYARLINK
		if(edit.getDatausulan().getLsbs_id().intValue() == 202){
			if(pmode_id==6 || pmode_id==1 || pmode_id==2 || pmode_id==3){
				if(edit.getDatausulan().getMste_flag_cc()==null || (edit.getDatausulan().getMste_flag_cc() != 2 && edit.getDatausulan().getMste_flag_cc() != 4 && sp!=1 ))
					err.rejectValue("datausulan.mste_flag_cc","","HALAMAN DATA USULAN : Untuk PLAN ini, bentuk pembayaran premi hanya dapat dilakukan secara TABUNGAN/SAVE BAYAR LINK.");
			}
		}
		
		//Deddy 3 Dec 2012 - Khusus Plan Produk Smile Link/Eka Link, harus memilih Dropdown paket,selain itu, diset Null saja.
		//Deddy 4 Jan 2013 - Tidak berdasarkan Khusus Plan Produk Smile Link/Eka Link saja, Dropdown akan otomatis muncul berdasarkan di bacDao.select_listPaket(lsbs_id),selain itu, diset 0(None).
		if(edit.getDatausulan().getLsbs_id()==159){
			if(edit.getDatausulan().getFlag_paket()==null){
				err.rejectValue("datausulan.flag_paket", "", "HALAMAN DATA USULAN : KHUSUS PLAN PRODUK EKA LINK/ SMILE LINK, Silakan memilih salah satu paket. Apabila tidak ada, silakan memilih NONE.");
			}
		}
		
		if(edit.getDatausulan().getLsbs_id()==120 && ((edit.getDatausulan().getLsdbs_number()>=10 &&edit.getDatausulan().getLsdbs_number()<=12) ||(edit.getDatausulan().getLsdbs_number()>=22 && edit.getDatausulan().getLsdbs_number()<=24))){//validasi tambahan khusus simpol untuk pengecekan Program SimasPrima - Simpol
			if(edit.getDatausulan().getFlag_paket()!=null){//cek apakah Simpol ini merupakan produk paket Program SimasPrima - Simpol
				Date SatuJanDuaRibuTigaBelas = defaultDateFormat.parse("31/12/2012");
				Date DuaDelapanFebDuaRibuTigaBelas = defaultDateFormat.parse("1/3/2013");
				if(edit.getDatausulan().getFlag_paket()==6 && (edit.getDatausulan().getMste_beg_date().before(SatuJanDuaRibuTigaBelas) && edit.getDatausulan().getMste_beg_date().after(DuaDelapanFebDuaRibuTigaBelas) ) ){
					err.rejectValue("datausulan.flag_paket", "", "HALAMAN DATA USULAN : Untuk Paket Program SimasPrima-Simpol, hanya berlaku dari tanggal 1 Januari 2013 - 28 Februari 2013.");
				}
				
				if(edit.getDatausulan().getFlag_paket()==6){
					if(edit.getDatausulan().getSpaj_paket()!=null){//cek dahulu apakah ada isi kolom No SPAJ/Polis.
						String reg_spaj_packet = elionsManager.selectGetSpaj(edit.getDatausulan().getSpaj_paket());//Cek apakah SPAJ/Polis tersebut exist
						if(reg_spaj_packet==null){//cek apabila No Polis/SPAJ yang dimasukkan ada atau tidak.
							err.rejectValue("datausulan.flag_paket", "", "HALAMAN DATA USULAN : Untuk Paket Program SimasPrima-Simpol, No Polis/SPAJ yang dimasukkan tidak terdaftar. Silakan Dipastikan dahulu.");
						}else{
							if(!reg_spaj_packet.equals("")){
								//Validasi pertama, SPAJ/Polis yang dmasukkan, harus simas Prima Manfaat Bulanan dan Non Manfaat Bulanan
								Datausulan produk_pp_sp = this.elionsManager.selectDataUsulanutama(reg_spaj_packet);
								if((produk_pp_sp.getLsbs_id()==142 && produk_pp_sp.getLsdbs_number()==2) || (produk_pp_sp.getLsbs_id()==158 && produk_pp_sp.getLsdbs_number()==6)){
//									err.rejectValue("datausulan.flag_paket", "", "HALAMAN DATA USULAN : Untuk Paket Program SimasPrima-Simpol, Rate +1% hanya bisa untuk Simas Prima Manfaat Bulanan dan Non Manfaat Bulanan.");
								}
								//Validate kedua, Nama PP SimasPrima dan  nama PP Simpol yang diinput harus sama.
								Pemegang data_pp_sp = this.elionsManager.selectpp(reg_spaj_packet);
								String nama_pp_sp = data_pp_sp.getMcl_first();
								if(!edit.getPemegang().getMcl_first().toUpperCase().equals(nama_pp_sp.toUpperCase())){
									err.rejectValue("datausulan.flag_paket", "", "HALAMAN DATA USULAN : Untuk Paket Program SimasPrima-Simpol, Nama Pemegang Polis di Simas Power Link dengan Simas Prima Tidak sama.");
								}
								//Validasi ketiga, Polis Simas Prima tidak boleh dalam kondisi sudah melakukan pinjaman.
								if(uwManager.selectCountPowerSaveSudahPinjaman(reg_spaj_packet)>0){
									err.rejectValue("datausulan.flag_paket", "", "HALAMAN DATA USULAN : Untuk Paket Program SimasPrima-Simpol, Simas Prima sudah Pernah Melakukan Proses Pinjaman, Jadi tidak bisa mengambil Program ini.");
								}
								//Validasi keempat, Polis Simas Prima tidak boleh dalam kondisi dalam pengajuan pencairan.
								if(uwManager.selectCountPowersaveCair(reg_spaj_packet)>0){
									err.rejectValue("datausulan.flag_paket", "", "HALAMAN DATA USULAN : Untuk Paket Program SimasPrima-Simpol, Simas Prima sedang dalam Proses Pencairan.");
								}
								//Pengesetan Boolean NewPlan, apabila beg_date Simpol dan SimasPrima Sama, berarti New (True), selain itu existing(False)
								if(edit.getDatausulan().getMste_beg_date()==produk_pp_sp.getMste_beg_date()){
									edit.setNewPlan(true);
								}else{
									edit.setNewPlan(false);}
							}else{
								err.rejectValue("datausulan.flag_paket", "", "HALAMAN DATA USULAN : Untuk Paket Program SimasPrima-Simpol, No Polis/SPAJ yang dimasukkan tidak terdaftar. Silakan Dipastikan dahulu.");
							}
						}
					}else{//apabila tidak ada, harus input SPAJ SIMAS PRIMA nya terlebih dahulu
						err.rejectValue("datausulan.flag_paket", "", "HALAMAN DATA USULAN : Untuk Paket Program SimasPrima-Simpol, Silakan Mengisi No Polis/SPAJ Simas Prima Terlebih dahulu.");
					}	
				}
			}
		}		

		//ryan, cek simultan simpol sebelum input, biar ga double
		if (edit.getCurrentUser().getLca_id().equals("01") && !edit.getCurrentUser().getLde_id().equals("11") ){
			if (edit.getDatausulan().getLsbs_id()==120 && ((edit.getDatausulan().getLsdbs_number()>=10 &&edit.getDatausulan().getLsdbs_number()<=12) ||(edit.getDatausulan().getLsdbs_number()>=22 && edit.getDatausulan().getLsdbs_number()<=24))){
				String mcl_first_pp = edit.getPemegang().getMcl_first();
				String mcl_first_tt = edit.getTertanggung().getMcl_first();
				Date tgl_lahir_pp = edit.getPemegang().getMspe_date_birth();
				Date tgl_lahir_tt = edit.getTertanggung().getMspe_date_birth();
				Integer cek_simultan_simpol = (Integer) this.uwManager.selectExistingSimpol(mcl_first_pp, mcl_first_tt, defaultDateFormat.format(tgl_lahir_pp) ,defaultDateFormat.format(tgl_lahir_tt));
				
				if (cek_simultan_simpol.intValue() > 0)
				{
					edit.getDatausulan().setFlag_simpol(1);	//err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Nasabah ini sudah memilik polis SIMAS POWELINK sebelumnya, Mohon dicek karena tidak bisa diinput 2x");
				}else
				{
					edit.getDatausulan().setFlag_simpol(0);	
				}
			}
		}
		
		if(edit.getDatausulan().getDaftaRiderAddOn()>0)	{
			ArrayList test2 = edit.getDatausulan().getDaftarplus_mix(); //FIXIN
			Integer daftarMix = new Integer(test2.size());
//		
				for (int i=0; i<daftarMix.intValue(); i++)
				{
					PesertaPlus_mix ppm = (PesertaPlus_mix)test2.get(i);
					Integer i_flagJenis = ppm.getFlag_jenis_peserta();
					ArrayList add=edit.getDatausulan().getDaftaRiderAddOnTtg();
					for(int j=0;j<add.size(); j++){
						Datarider rider=(Datarider)add.get(j);
						if(rider.getJenis()==i_flagJenis){
							String nama_rider ="produk_asuransi.n_prod_"+rider.getLsbs_id();	
							Class aClass1 = Class.forName(nama_rider);
							n_prod produkRider= (n_prod)aClass1.newInstance();
							produkRider.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
							produkRider.ii_usia_tt=ppm.getUmur();
							produkRider.of_set_bisnis_no(rider.getLsdbs_number());
							Integer tanggal1= edit.getDatausulan().getMste_beg_date().getDate();
							Integer bulan1 = (edit.getDatausulan().getMste_beg_date().getMonth())+1;
							Integer tahun1 = (edit.getDatausulan().getMste_beg_date().getYear())+1900;
							
							//tgl lahir ttg
							Integer tanggal2=edit.getTertanggung().getMspe_date_birth().getDate();
							Integer bulan2=(edit.getTertanggung().getMspe_date_birth().getMonth())+1;
							Integer tahun2=(edit.getTertanggung().getMspe_date_birth().getYear())+1900;
							String hasil_rider=produkRider.of_check_usia_kesehatan(i_flagJenis, ppm.getLsre_id(), tahun2.intValue(), bulan2.intValue(), tanggal2.intValue(), tahun1.intValue(), bulan1.intValue(), tanggal1.intValue(), 1, rider.getLsbs_id(), rider.getLsdbs_number());
							if (hasil_rider.trim().length()!=0)
							{
								err.rejectValue("datausulan.flag_paket","","HALAMAN DATA USULAN :" +hasil_rider);
							}
						}
					}
					
				}
					
		}
		
		Double up=edit.getDatausulan().getMspr_tsi();
		Double premi=edit.getDatausulan().getMspr_premium();
		if (up==null)
		{
			up=new Double(0);
		}
		edit.getDatausulan().setMspr_tsi(up);
		if (premi==null)
		{
			premi= new Double(0);
		}
		edit.getDatausulan().setMspr_premium(premi);
		String spaj = edit.getPemegang().getReg_spaj();
		if (spaj == null)
		{
			spaj ="";
		}
		//cek no blanko sudah pernah dipakai  atau belum
		if (!spaj.equalsIgnoreCase(""))
		{
			Integer kode = edit.getDatausulan().getLsbs_id();
			Integer number = edit.getDatausulan().getLsdbs_number();
			String no_blanko = edit.getPemegang().getMspo_no_blanko();
			if(!StringUtil.isEmpty(no_blanko)){
				no_blanko = no_blanko.replace(" ", "");
				edit.getPemegang().setMspo_no_blanko(no_blanko);
			}else{
				no_blanko = "-";
			}
			Integer jumlah_blanko = (Integer) this.elionsManager.count_no_blanko_perspaj(Integer.toString(kode), Integer.toString(number), no_blanko, spaj);
			if (jumlah_blanko == null)
			{
				jumlah_blanko = new Integer(0);
			}
			if (jumlah_blanko.intValue() > 0)
			{
				edit.getPemegang().setCek_blanko(new Integer(1));
			}else{
				edit.getPemegang().setCek_blanko(new Integer(0));
			}
			
			String ktp = edit.getPemegang().getMspe_no_identity();
			if (ktp == null)
			{
				ktp ="";
			}
			String lsbs_id = edit.getDatausulan().getLsbs_id().toString();
			if (lsbs_id== null)
			{
				lsbs_id ="";
			}
			String lsdbs_number = edit.getDatausulan().getLsdbs_number().toString();
			if (lsdbs_number == null)
			{
				lsdbs_number = "";
			}
			up = edit.getDatausulan().getMspr_tsi();
			if (up ==null)
			{
				up = new Double(0);
			}
			
			//cek ktp sama (orang yang sama)
			Integer jumlah_ktp = (Integer) this.elionsManager.cek_data_sama(spaj, ktp, lsbs_id, lsdbs_number, up);
			if (jumlah_ktp.intValue() > 0)
			{
				edit.getDatausulan().setFlag_ktp(new Integer(1));
				List data_spaj = this.elionsManager.cek_spaj_sama1(spaj, ktp, lsbs_id, lsdbs_number, up);
				if (data_spaj != null)
				{
					Map m = (Map)data_spaj.get(0);
					edit.getDatausulan().setSpaj_ktp(m.get("REG_SPAJ").toString());
				}else{
					edit.getDatausulan().setSpaj_ktp("");
				}
			}else{
				edit.getDatausulan().setFlag_ktp(new Integer(0));
				edit.getDatausulan().setSpaj_ktp("");
			}	
			

		}else{
			Integer kode = edit.getDatausulan().getLsbs_id();
			Integer number = edit.getDatausulan().getLsdbs_number();
			String no_blanko = edit.getPemegang().getMspo_no_blanko();
			if(!StringUtil.isEmpty(no_blanko)){
				no_blanko = no_blanko.replace(" ", "");
				edit.getPemegang().setMspo_no_blanko(no_blanko);
			}else{
				no_blanko = "-";
			}
			Integer jumlah_blanko = (Integer) this.elionsManager.count_no_blanko(Integer.toString(kode), Integer.toString(number), no_blanko);
			if (jumlah_blanko == null)
			{
				jumlah_blanko = new Integer(0);
			}
			if (jumlah_blanko.intValue() > 0)
			{
				edit.getPemegang().setCek_blanko(new Integer(1));
			}else{
				edit.getPemegang().setCek_blanko(new Integer(0));
			}
			
			String ktp = edit.getPemegang().getMspe_no_identity();
			if (ktp == null)
			{
				ktp ="";
			}
			String lsbs_id = edit.getDatausulan().getLsbs_id().toString();
			if (lsbs_id== null)
			{
				lsbs_id ="";
			}
			String lsdbs_number = edit.getDatausulan().getLsdbs_number().toString();
			if (lsdbs_number == null)
			{
				lsdbs_number = "";
			}
			up = edit.getDatausulan().getMspr_tsi();
			if (up ==null)
			{
				up = new Double(0);
			}
			Integer jumlah_ktp = (Integer) this.elionsManager.cek_data_baru_sama(ktp, lsbs_id, lsdbs_number, up);
			if (jumlah_ktp.intValue() > 0)
			{
				edit.getDatausulan().setFlag_ktp(new Integer(1));
				List data_spaj = this.elionsManager.cek_spaj_sama(ktp, lsbs_id, lsdbs_number, up);
				if (data_spaj != null)
				{
					Map m = (Map)data_spaj.get(0);
					edit.getDatausulan().setSpaj_ktp(m.get("REG_SPAJ").toString());					
				}else{
					edit.getDatausulan().setSpaj_ktp("");
				}
			}else{
				edit.getDatausulan().setFlag_ktp(new Integer(0));
				edit.getDatausulan().setSpaj_ktp("");
			}
			
			Integer flag_uppremi = edit.getDatausulan().getFlag_uppremi();
			if (flag_uppremi.intValue()==0)
			{
				Date tgl_lahir_ttg = edit.getTertanggung().getMspe_date_birth();
				String tgl = (Integer.toString(tgl_lahir_ttg.getYear()+1900))+(FormatString.rpad("0",Integer.toString(tgl_lahir_ttg.getMonth()+1),2))+(FormatString.rpad("0",Integer.toString(tgl_lahir_ttg.getDate()),2));
				String nama = edit.getTertanggung().getMcl_first();
				Double p = edit.getDatausulan().getMspr_tsi();
				if (spaj.equalsIgnoreCase(""))
				{
					Integer jumlah_dobel = (Integer) this.elionsManager.cek_polis_dobel_tsi(nama, tgl, lsbs_id, lsdbs_number, p);
					if (jumlah_dobel.intValue() > 0)
					{
						edit.getDatausulan().setPolis_dobel(new Integer(1));
						
					}else{
						edit.getDatausulan().setPolis_dobel(new Integer(0));
					}
				}
			}else{
				Date tgl_lahir_ttg = edit.getTertanggung().getMspe_date_birth();
				String tgl = (Integer.toString(tgl_lahir_ttg.getYear()+1900))+(FormatString.rpad("0",Integer.toString(tgl_lahir_ttg.getMonth()+1),2))+(FormatString.rpad("0",Integer.toString(tgl_lahir_ttg.getDate()),2));
				String nama = edit.getTertanggung().getMcl_first();
//				String site = edit.getAgen().getLca_id().toString().concat(edit.getAgen().getLwk_id().toString().concat(edit.getAgen().getLsrg_id().toString()));
				
				if (spaj.equalsIgnoreCase(""))
				{
					Integer jumlah_dobel = (Integer) this.elionsManager.cek_polis_dobel(nama, tgl, lsbs_id, lsdbs_number, premi,null);
					if (jumlah_dobel.intValue() > 0)
					{
						edit.getDatausulan().setPolis_dobel(new Integer(1));
						
					}else{
						edit.getDatausulan().setPolis_dobel(new Integer(0));
					}
				}
			}
		}
		
		String kurs_rek = edit.getRekening_client().getMrc_kurs();
		if (kurs_rek == null)
		{
			kurs_rek ="";
		}
		if (kurs_rek.equalsIgnoreCase(""))
		{
			String kurs_utama = edit.getDatausulan().getKurs_p();
			kurs_rek = kurs_utama;
		}
		edit.getRekening_client().setMrc_kurs(kurs_rek);
		
		if(products.powerSave(edit.getDatausulan().getLsbs_id().toString()) ||
				products.stableLink(edit.getDatausulan().getLsbs_id().toString()) ||
				products.stableSave(edit.getDatausulan().getLsbs_id(), edit.getDatausulan().getLsdbs_number()) ||
				products.stableSavePremiBulanan(edit.getDatausulan().getLsbs_id().toString())){
			edit.getDatausulan().setFlag_paymode_rider(1);
		}
		
		//LUFI--Set No Va
		if(edit.getPemegang().getMspo_flag_spaj()>0){
			String s_nomorVa="8006";
			if(edit.getPemegang().getFlag_upload()==0){
				if((edit.getDatausulan().getLsbs_id()==190 && "5,6".indexOf(edit.getDatausulan().getLsdbs_number().toString())>=0 )||
				   (edit.getDatausulan().getLsbs_id()==163 && "6,7,8,9,10".indexOf(edit.getDatausulan().getLsdbs_number().toString())>=0 )||
				   	edit.getDatausulan().getLsbs_id()==120 && "25,26,27".indexOf(edit.getDatausulan().getLsdbs_number().toString())>=0 ){
					 	edit.getTertanggung().setMste_no_vacc("");
				}else{
					if(bacManager.selectLineBusLstBisnis(edit.getDatausulan().getLsbs_id().toString())==3){
						 s_nomorVa ="8076";
					}
					
					edit.getTertanggung().setMste_no_vacc((s_nomorVa+edit.getPemegang().getMspo_no_blanko()).trim());	
					Integer no_spaj_vaccExist=bacManager.selectno_virtual_Exist(edit.getTertanggung().getMste_no_vacc().toString());
					if(no_spaj_vaccExist<=0){
						err.rejectValue("tertanggung.mste_no_vacc","","HALAMAN DATA USULAN: No Virtual Account tidak terdaftar silahkan cek nomor yang terdapat pada form SPAJ dan No BLANKO/SERI yang diinput");
				    }
					if(edit.getTertanggung().getMste_no_vacc().length()<16){
						err.rejectValue("tertanggung.mste_no_vacc","","HALAMAN DATA USULAN: Ada Kesalahan dalam generate nomor virtual account");
					}
				 }
			}
		 }else{
			 edit.getTertanggung().setMste_no_vacc("");
		 }
	}
	
	//-------------------------data usulan ----------------------------------------
	public void validateddu(Object cmd, Errors err)  throws ServletException,IOException,Exception
	{
		try {
			logger.debug("EditBacValidator : validate page validateddu");
			Integer hasil=new Integer(0);
			Boolean cek_pmode=new Boolean(false);
			Boolean cek_kurs_up=new Boolean(false);
			Boolean cek_kurs_premi = new Boolean(false);
			String hasil_kurs_up="";
			String hasil_cek_pmode="";
			Integer tanggal=new Integer(0);
			Integer bulan=new Integer(0);
			Integer tahun=new Integer(0);
									
			Integer li_umur_ttg=new Integer(0);
			Integer li_umur_pp=new Integer(0);
			Integer li_umur_pic=new Integer(0);
			String hasil_kurs_premi="";
			Double min_up=new Double(0);
			String hasil_min_up="";
			String hasil_premi="";
			Integer umur_beasiswa=null;
			String hasil_beasiswa="";
			Integer ins_period=new Integer(0);
			Integer pay_period=new Integer(0);
			String hasil_up="";
			String kurs="";
			String hasil_cek_usia="";
			String tgl_end="";
			String bln_end="";
			String thn_end="";
			Integer tanggal1=new Integer(0);
			Integer bulan1=new Integer(0);
			Integer tahun1=new Integer(0);
			Integer tanggal2=new Integer(0);
			Integer bulan2=new Integer(0);
			Integer tahun2=new Integer(0);	
			Integer tanggal3=new Integer(0);
			Integer bulan3=new Integer(0);
			Integer tahun3=new Integer(0);	
			Integer tanggal4=new Integer(0);
			Integer bulan4=new Integer(0);
			Integer tahun4=new Integer(0);	
			Integer ii_class=new Integer(0);
			Integer klas= new Integer(0);
			String relation_ttg="";
			String hasil_klas="";		
			Integer age=new Integer(0);
			Integer jmlh_rider=new Integer(0);
			Double total_premi_sementara=new Double(0);
			Double rate_plan=new Double(0);
			Integer flag_account=new Integer(0);
			Integer flag_powersave = new Integer(0);
			Boolean isBungaSimponi=new Boolean(false);
			Boolean isBonusTahapan=new Boolean(false);
			Integer flag_as=new Integer(0);
			Integer faktor=new Integer(0);			
			Boolean flag_bao=new Boolean(false);
			String hasil_beg_date="";
			Integer flag_worksite = new Integer(0);
			Integer flag_endowment = new Integer(0);
			Integer flag_bao1=new Integer(0);
			Cmdeditbac edit= (Cmdeditbac) cmd;
			f_validasi data = new f_validasi();
			NumberFormat f = new DecimalFormat("#,##0.00;(#,##0.00)");
			form_data_usulan a =new form_data_usulan();
			a.setProducts(products);
			Integer kode_flag = new Integer(0);
			Integer flag_rider = new Integer(0);
			Integer pmode_id = null;
			Double li_pct_biaya = new Double(0); 
			//cek nama bank autodebet kalau sudah pernah diisi
			String bank_pp=edit.getAccount_recur().getLbn_id();	
			String nama_bank_autodebet="";
			Integer flag_ekalink = new Integer(0);
			String hasil_rider = "";
			Integer flag_hcp = new Integer(0);	
			edit.getDatausulan().setFlag_hcp(new Integer(0));
			Integer number_utama=new Integer(0);
			String status = edit.getPemegang().getStatus();
			Integer flag_karyawan = edit.getDatausulan().getMste_flag_el();
			Integer flag_gmit = edit.getDatausulan().getMste_gmit();
			Integer hubung = edit.getDatausulan().getLsre_id();
			if (flag_gmit==null){
				flag_gmit= new Integer(0);
				edit.getDatausulan().setMste_gmit(flag_gmit);
			}
			if (flag_karyawan==null)
			{
				flag_karyawan= new Integer(0);
				edit.getDatausulan().setMste_flag_el(flag_karyawan);
			}
			String reg_spaj = edit.getDatausulan().getReg_spaj();
			
			if (status==null)
			{
				status= "input";
			}
			Integer flag_rider_hcp = edit.getDatausulan().getFlag_rider_hcp();
			if (flag_rider_hcp == null)
			{
				flag_rider_hcp = new Integer(0);
			}
			
			Integer alertEkaSehat = edit.getDatausulan().getAlertEkaSehat();
			if(alertEkaSehat == null){
				alertEkaSehat = new Integer(0);
			}
			
			Integer cek_flag_hcp = edit.getDatausulan().getCek_flag_hcp();
			if (cek_flag_hcp == null)
			{
				cek_flag_hcp = new Integer(0);
			}
			
			if(bank_pp!=null)
			{
				Map data1= (HashMap) this.elionsManager.select_bank2(bank_pp);

				if (data1!=null)
				{		
					nama_bank_autodebet = (String)data1.get("BANK_NAMA");
				}
			}else{
				bank_pp="";
			}
			
			//tambahan Yusuf - 5 des 2008 - khusus user bank sinarmas, bila stable link, blokir dobel input bila nama nasabah + tgl lahir sama
			//Yusuf - 7 Sept 2009, revisi aturan dari Aktuaria (Email Achmad)
			/*
			From: 	Achmad Anwarudin  
			Sent:	Thursday, September 03, 2009 11:34 AM
			To:	Yusuf
			Cc:	Dian Natalia; Novie; Neni Ferina; Puti Reno; David Husaini
			Subject:	FW: tdk bisa input stabil link dgn kondisi pemegang polis sama
			
			Dear Yusuf,
			untuk produk Simas Stabil Link atau Stable Link diberlakukan Ketentuan sbb :
			1. Bila sudah punya Polis Simas Stabil Link dan masuk lagi dengan Pemegang Polis dan Tertanggung sama maka harus top up ( tidak Terbit polis baru)
			2. Bila sudah punya Polis Simas Stabil Link dan masuk lagi dengan Pemegang Polis yang berbeda tapi Tertangggung sama maka akan diterbitkan polis baru. 
			    ini artinya 1 tertanggung yang sama bisa mempunyai lebih dari 1 Polis selama Pemegang Polis Berbeda.
			3. Bila sudah punya Polis Simas Stabil Link dan masuk lagi dengan Tertanggung yang berbeda sekalipun  Pemegang Polisnya berbeda otomatis akan diterbitkan polis baru. 
			    ==> Bila masuk dengan Tertanggung yang berbeda justru hal ini tidak ada issue risiko di AJS 
			
			Contoh :
			     Bila Pemegang Polis x sudah Punya Polis dengan kondisi :
			         - Tertanggung : x
			         - Pemegang Polis : x
			    dan bila mau beli polis dengan kondisi :
			    a)  - Tertanggung : x
			         - Pemegang Polis : x 
			         ==> maka harus Top Up ( karena Tertanggung dan Pemegang Polis  sama)
			    b)  - Tertanggung : x
			         - Pemegang Polis : y
			         ==> maka akan diterbitkan Polis baru ( karena Tertanggung dan Pemegang Polis  berbeda)
			
			 Note : Jadi tidak benar 1 Pemegang Polis hanya boleh 1 Polis Stable Link karena  1 Pemegang Polis bisa mempunyai banyak Polis selama Tertanggungnya berbeda-beda.
			
			
			Salam,
			achmad
			 */
			
			// (Andhika) 27-09-2013
			Integer sp = Integer.parseInt(edit.getFlag_special_case());
			if (sp==null)sp=0;
			
			//cek nama bank rek client kalau sudah pernah diisi
			edit.getAccount_recur().setLbn_nama(nama_bank_autodebet);
			String bank_pp1=edit.getRekening_client().getLsbp_id();
			String nama_bank_rekclient="";
			if(bank_pp1!=null)
			{
				Map data1= (HashMap) this.elionsManager.select_bank1(bank_pp1);

				if (data1!=null)
				{		
					nama_bank_rekclient = (String)data1.get("BANK_NAMA");
				}
			}else{
				bank_pp1="";
			}
			edit.getRekening_client().setLsbp_nama(nama_bank_rekclient);
			
			//cek nama perusahaan kalau sudah dipilih
			String kode_perusahaan = edit.getPemegang().getMspo_customer();
			String nama_perusahaan="";
			if(kode_perusahaan!=null)
			{
				Map data1= (HashMap) this.elionsManager.select_namacompany(kode_perusahaan);
				if (data1!=null)
				{		
					nama_perusahaan = (String)data1.get("COMPANY_NAMA");
				}
			}else{
				kode_perusahaan="";
			}
			edit.getPemegang().setMspo_customer_nama(nama_perusahaan);
			
			// cek cabang dari user id
			String lca_id = edit.getPemegang().getCbg_lus_id();
			if (lca_id.trim().length()==1)
			{
				lca_id="0"+lca_id;
			}
			edit.getPemegang().setLca_id(lca_id);
			
			if (edit.getDatausulan().getTipeproduk()==null)
			{
				edit.getDatausulan().setTipeproduk(new Integer(1));
			}
			
			Integer flag_special = edit.getPowersave().getFlag_special();
			if (status.equalsIgnoreCase("edit"))
			{
				flag_special = uwManager.getFlagSpecial(edit.getDatausulan().getReg_spaj());
			}
			
			if (edit.getCurrentUser().getJn_bank()==2 || edit.getCurrentUser().getJn_bank()==16)
			{
				sp = bacManager.selectLusSpecial(edit.getCurrentUser().getLus_id());
			}
			if(flag_special==null){
				flag_special= new Integer(0);
			}
			edit.getPowersave().setFlag_special(flag_special);
			
			//plan
			String bisnis=edit.getDatausulan().getPlan();
			if (bisnis==null)
			{
				bisnis="0~X0";
			}
			String hasil_bisnis = a.bisnis(bisnis);
			if(hasil_bisnis.trim().length()!=0)
			{
				err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN :" +hasil_bisnis);
			}
	
	
			int letak=0;
			letak=bisnis.indexOf("X");
			Integer kode_produk=null;
			Integer number_produk=null;
			kode_produk=new Integer(Integer.parseInt(bisnis.substring(0,letak-1)));
			number_produk=new Integer(Integer.parseInt(bisnis.substring(letak+1,bisnis.length())));
			edit.getDatausulan().setLsbs_id(kode_produk);
			edit.getDatausulan().setLsdbs_number(number_produk);
	
			if (status.equalsIgnoreCase("input"))
			{
				if ((kode_produk.intValue() == 162) && (number_produk.intValue() <=2))
				{
					err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN :Sejak tanggal 30 Juli 2007, Arthalink manfaat lama sudah tidak berlaku, silahkan memilih Arthalink manfaat baru (NEW)");
				}
			}
			
			Date tgl_beg_date = edit.getDatausulan().getMste_beg_date();
			
			
			String kurs_up=edit.getDatausulan().getLku_id();
			if (kurs_up==null)
			{
				kurs_up="";
			}
			String kurs_premi=edit.getDatausulan().getKurs_p();
			if (kurs_premi==null)
			{
				kurs_premi="";
			}
			Double up=edit.getDatausulan().getMspr_tsi();
			Double premi=edit.getDatausulan().getMspr_premium();
			Double diskon_karyawan = 1.0;
			if (up==null)
			{
				up=new Double(0);
			}
			edit.getDatausulan().setMspr_tsi(up);
			if (premi==null)
			{
				premi= new Double(0);
			}
			edit.getDatausulan().setMspr_premium(premi);
			
			String nama_produk="";
			if(hasil_bisnis.trim().length()==0)
			{
				if (Integer.toString(kode_produk.intValue()).trim().length()==1)
				{
					nama_produk="produk_asuransi.n_prod_0"+kode_produk;	
				}else{
					nama_produk="produk_asuransi.n_prod_"+kode_produk;	
				}
	
				try{
					Class aClass = Class.forName( nama_produk );
					n_prod produk = (n_prod)aClass.newInstance();
					produk.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
					produk.cek_flag_agen(kode_produk.intValue(),number_produk.intValue(), edit.getPowersave().getFlag_bulanan());
					
					Integer flag_horison = new Integer(produk.flag_horison);
					edit.getDatausulan().setFlag_horison(flag_horison);
					
					Integer flag_simas = new Integer(produk.simas);
					edit.getDatausulan().setFlag_simas(flag_simas);
					
					int flag_platinum = produk.flag_platinumlink;
					int flag_cutipremi = produk.flag_cuti_premi; 
					//kode produk
					produk.of_set_bisnis_no(number_produk.intValue());
					produk.ii_bisnis_no=(number_produk.intValue());
					produk.ii_bisnis_id=(kode_produk.intValue());
					flag_ekalink = produk.flag_ekalink;
					if(edit.getPemegang().getLsre_id()==1){
						produk.samePPTT=1;
					}else{
						produk.samePPTT=0;
					}
					edit.getDatausulan().setFlag_ekalink(flag_ekalink);
					
					Integer flag_bulanan = new Integer(produk.flag_powersavebulanan);
					edit.getDatausulan().setFlag_bulanan(flag_bulanan);
					
					produk.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
					flag_powersave = new Integer(produk.of_get_bisnis_no(edit.getPowersave().getFlag_bulanan()));
					if (produk.flag_powersavebulanan == 1)
					{
						produk.cek_flag_agen(kode_produk.intValue(), number_produk.intValue(), edit.getPowersave().getFlag_bulanan());
						flag_powersave = new Integer(produk.flag_powersave);
					}
					edit.getPowersave().setFlag_powersave(flag_powersave);
					

					//flag
					flag_account=new Integer(produk.flag_account);
					
					isBungaSimponi=new Boolean(produk.isBungaSimponi);
					flag_worksite = new Integer(produk.flag_worksite);
					flag_endowment = new Integer(produk.flag_endowment);
					
					Integer flag_bungasimponi=new Integer(0);
					if (isBungaSimponi.booleanValue()==true)
					{
						flag_bungasimponi=new Integer(1);
					}else{
						flag_bungasimponi=new Integer(0);
					}
						
					isBonusTahapan=new Boolean(produk.isBonusTahapan);
					Integer flag_bonustahapan = new Integer(0);
					if (isBonusTahapan.booleanValue()==true)
					{
						flag_bonustahapan=new Integer(1);
					}else{
						flag_bonustahapan=new Integer(0);
					}
					flag_as=new Integer(produk.flag_as);
					edit.getDatausulan().setFlag_as(flag_as);
					edit.getDatausulan().setIsBonusTahapan(flag_bonustahapan);
					edit.getDatausulan().setIsBungaSimponi(flag_bungasimponi);
					edit.getDatausulan().setFlag_worksite(flag_worksite);
					edit.getDatausulan().setFlag_endowment(flag_endowment);
					
					//klas untuk ekaproteksisimas
					if (produk.flag_class==1)
					{
						klas=edit.getDatausulan().getMspr_class();
						if (klas==null)
						{
							ii_class=new Integer(1);
							klas=new Integer(1);
							produk.get_class(ii_class.intValue());
						}else{
							if(klas.intValue()==0)
							{
								err.rejectValue("datausulan.mspr_class","","HALAMAN DATA USULAN : Silahkan isi terlebih dahulu klas minimal 1");
							}else{
								hasil_klas = a.cek_klas(Integer.toString(klas.intValue()));
								if (hasil_klas.trim().length()!=0)
								{
									err.rejectValue("datausulan.mspr_class","","HALAMAN DATA USULAN :" +hasil_klas);
								}
							}
						}

						edit.getDatausulan().setMspr_class(klas);
						produk.ii_class = klas.intValue();
					}else{
						klas=new Integer(0);
						ii_class=new Integer(0);
						edit.getDatausulan().setMspr_class(klas);
						produk.ii_class = klas.intValue();
					}
					
					Date tgl_spaj = edit.getPemegang().getMspo_spaj_date();
					if (tgl_spaj==null)
					{
						Calendar tgl_sekarang = Calendar.getInstance(); 
						String  v_strInputDate = FormatString.rpad("0",Integer.toString(tgl_sekarang.get(Calendar.DATE)),2);
						v_strInputDate = v_strInputDate.concat("/");
						v_strInputDate = v_strInputDate.concat(FormatString.rpad("0",Integer.toString(tgl_sekarang.get(Calendar.MONTH)+1),2));
						v_strInputDate = v_strInputDate.concat("/");
						v_strInputDate = v_strInputDate.concat(Integer.toString(tgl_sekarang.get(Calendar.YEAR)));	
						String v_strDateNow = v_strInputDate;
						tgl_spaj = defaultDateFormat.parse(v_strDateNow);
						edit.getPemegang().setMspo_spaj_date(tgl_spaj);
					}
					
					Date tgl_begin_polis = edit.getDatausulan().getMste_beg_date();
					if (tgl_begin_polis==null)
					{
						Calendar tgl_sekarang = Calendar.getInstance(); 
						String  v_strInputDate = FormatString.rpad("0",Integer.toString(tgl_sekarang.get(Calendar.DATE)),2);
						v_strInputDate = v_strInputDate.concat("/");
						v_strInputDate = v_strInputDate.concat(FormatString.rpad("0",Integer.toString(tgl_sekarang.get(Calendar.MONTH)+1),2));
						v_strInputDate = v_strInputDate.concat("/");
						v_strInputDate = v_strInputDate.concat(Integer.toString(tgl_sekarang.get(Calendar.YEAR)));	
						String v_strDateNow = v_strInputDate;
						
						tgl_begin_polis = defaultDateFormat.parse(v_strDateNow);
						edit.getDatausulan().setMste_beg_date(tgl_begin_polis);
					}
					
					//yusuf - stable link
					//bila konversi psave ke slink, begdatenya <> tgl begdate polis, melainkan memakai tgl ro terbaru
					//yg sudah di set di edit bac controller
					if(products.stableLink(String.valueOf(produk.ii_bisnis_id)) && !edit.getDatausulan().isPsave) {
						edit.getPowersave().setBegdate_topup(tgl_begin_polis);
					}//Deddy - stable save
					 // sama dengan kondisi di atas
					else if(products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no) && !edit.getDatausulan().isPsave){
						edit.getDatausulan().setConvert(2);
						edit.getPowersave().setBegdate_topup(tgl_begin_polis);
					}
					if( edit.getDatausulan().getFlag_as()==2 && (produk.ii_bisnis_id==183 || produk.ii_bisnis_id==189 || produk.ii_bisnis_id==193 || produk.ii_bisnis_id==195 || produk.ii_bisnis_id==204)){
						diskon_karyawan=0.7;
					}
					
					//end date polis	
					//tgl beg date
					tanggal1= edit.getDatausulan().getMste_beg_date().getDate();
					bulan1 = (edit.getDatausulan().getMste_beg_date().getMonth())+1;
					tahun1 = (edit.getDatausulan().getMste_beg_date().getYear())+1900;
					
					//tgl lahir ttg
					tanggal2=edit.getTertanggung().getMspe_date_birth().getDate();
					bulan2=(edit.getTertanggung().getMspe_date_birth().getMonth())+1;
					tahun2=(edit.getTertanggung().getMspe_date_birth().getYear())+1900;
	
					//tgl lahir pp
					tanggal3=edit.getPemegang().getMspe_date_birth().getDate();
					bulan3=(edit.getPemegang().getMspe_date_birth().getMonth())+1;
					tahun3=(edit.getPemegang().getMspe_date_birth().getYear())+1900;
					
					//tgl lahir pic
					if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
						tanggal4=edit.getContactPerson().getDate_birth().getDate();
						bulan4=(edit.getContactPerson().getDate_birth().getMonth())+1;
						tahun4=(edit.getContactPerson().getDate_birth().getYear())+1900;
					}
									
					//hit umur ttg, pp, pic
					f_hit_umur umr= new f_hit_umur();
					li_umur_ttg =  umr.umur(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);
					li_umur_pp = umr.umur(tahun3,bulan3,tanggal3,tahun1,bulan1,tanggal1);
					if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
						li_umur_pic = umr.umur(tahun4,bulan4,tanggal4,tahun1,bulan1,tanggal1);
					}
					if ( produk.usia_nol == 1)
					{
						if ((li_umur_ttg.intValue() == 0) && produk.ii_bisnis_id != 208)
						{
							li_umur_ttg = 1;
						}
					}
					if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
						li_umur_pp=li_umur_pic;
					}
					//set umur ttg dan pp ke n_prod
					produk.of_set_usia_tt(li_umur_ttg.intValue());
					produk.of_set_usia_pp(li_umur_pp.intValue());
					//if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
						//produk.of_set_usia_pic(li_umur_pic.intValue());
					//}
					int flag_plan = produk.flag_jenis_plan;
					edit.getDatausulan().setFlag_jenis_plan(flag_plan);
					edit.getPemegang().setMste_age(li_umur_pp);
					edit.getTertanggung().setMste_age(li_umur_ttg);
					if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
						edit.getContactPerson().setMste_age(li_umur_pic);
					}
					
					//umur beasiswa produk eka sarjana mandiri
					relation_ttg = Integer.toString((edit.getPemegang().getLsre_id()).intValue());
					if (relation_ttg.equalsIgnoreCase("1"))
					{
						if ( kode_produk.intValue() == 162 && number_produk.intValue() <= 4 )
						{
							if (li_umur_pp.intValue() > 70)
							{
								err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Usia Pemegang Polis dengan relasi diri sendiri maximum : 70 Tahun"); 
							}
						}
					}
					
					//umur beasiswa khusu produk eka sarjana mandiri
					hasil_beasiswa=produk.cek_hubungan(relation_ttg);
					if (hasil_beasiswa.trim().length()!=0)
					{
						err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN :" +hasil_beasiswa);
					}else{
	
						if (kode_produk.intValue() == 63 || kode_produk.intValue() == 173)
						{
							umur_beasiswa = edit.getTertanggung().getMspo_umur_beasiswa();
		
							if (umur_beasiswa!=null)
							{
								li_umur_ttg=umur_beasiswa ;
								hasil_beasiswa=produk.cek_umur_beasiswa(umur_beasiswa.intValue(),number_produk.intValue());
								if (hasil_beasiswa.trim().length()!=0)
								{
									err.rejectValue("datausulan.mste_beg_date","","HALAMAN DATA USULAN :" +hasil_beasiswa);
								}
							}else{
								li_umur_ttg=0;
								hasil_beasiswa="Untuk Plan ini , umur beasiswa harus diisi, Silahkan kembali ke halaman tertanggung untuk mengisinya.";
								err.rejectValue("datausulan.mste_beg_date","","HALAMAN DATA USULAN :" +hasil_beasiswa);
							}
							
							pmode_id = edit.getDatausulan().getLscb_id();
							
//							if(pmode_id==6 && edit.getDatausulan().getMste_flag_cc()!=3)
//								err.rejectValue("datausulan.mste_flag_cc","","HALAMAN DATA USULAN : Untuk cara Bayar Bulanan, bentuk pembayaran premi dilakukan melalui pemotongan gaji(PAYROLL).");
							
						} 		
						
						
					}
					
					edit.getDatausulan().setFlag_rider(new Integer(produk.flag_rider));
					flag_rider = new Integer(produk.flag_rider);
					//cek usia
					produk.of_set_usia_tt(li_umur_ttg.intValue());
					produk.of_set_usia_pp(li_umur_pp.intValue());
					produk.of_set_age();
					
					age=new Integer(produk.ii_age);
					
					Integer jumlah_cancel = edit.getPemegang().getJumlah_cancel();
					Integer flag_gutri = edit.getDatausulan().getFlag_gutri();
					Integer mste_flag_guthrie = edit.getPemegang().getMste_flag_guthrie();
					
					// kalau gutri tidak cek umur, karena polis lama diperbaharui otomatis umurnya tidak memenuhi syarat n_prod
					// kalau pemegang adalah individu, cek umur
					if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
						if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
						{
							if (li_umur_pp.intValue() > 999)
							{
								err.rejectValue("datausulan.mste_beg_date","","HALAMAN DATA USULAN :Umur pemegang polis (Badan Usaha) maximal 999 tahun");
											
							}
						}
					}else{
						if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0)
						{
							if(produk.ii_bisnis_id==164){
								produk.is_kurs_id = edit.getDatausulan().getKurs_premi();
								if (edit.getDatausulan().getTotal_premi_kombinasi() == null)
								{
									edit.getDatausulan().setTotal_premi_kombinasi(new Double(0));
									err.rejectValue("datausulan.total_premi_kombinasi","","HALAMAN DATA USULAN : Untuk cara premi tersebut , Silahkan masukkan total premi terlebih dahulu.");
								}
								hasil_cek_usia=produk.of_check_usia_case_premi(tahun2.intValue(),bulan2.intValue(),tanggal2.intValue(),tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),pay_period.intValue(),number_produk.intValue(), edit.getDatausulan().getTotal_premi_kombinasi());
							}else{
								if(produk.ii_bisnis_id==129)pay_period=edit.getDatausulan().getMspo_pay_period();
								hasil_cek_usia=produk.of_check_usia(tahun2.intValue(),bulan2.intValue(),tanggal2.intValue(),tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),pay_period.intValue(),number_produk.intValue());
							}
							if (hasil_cek_usia.trim().length()!=0 && sp != 1){
								err.rejectValue("datausulan.mste_beg_date","","HALAMAN DATA USULAN :" +hasil_cek_usia);
							}
						}
						//ryan - usia minimum PA Resiko A
						if(produk.ii_bisnis_no==8) {
							if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0){
								if (li_umur_pp.intValue()<17 && sp != 1){
									err.rejectValue("datausulan.mste_beg_date","","HALAMAN DATA USULAN :Umur pemegang polis minimum 18 tahun");
								}
							}
						}
						else if (jumlah_cancel.intValue()==0 && flag_gutri.intValue() ==0 && mste_flag_guthrie.intValue() == 0){
							if (li_umur_pp.intValue()<17 && sp != 1){
								err.rejectValue("datausulan.mste_beg_date","","HALAMAN DATA USULAN :Umur pemegang polis minimum 17 tahun");
							}
						}
					}
					
					if(produk.ii_bisnis_id==177 || produk.ii_bisnis_id==207 || (produk.ii_bisnis_id==186 && produk.ii_bisnis_no!=3) ){
						ins_period = edit.getDatausulan().getMspr_ins_period();
						//ins_period = new Integer(produk.of_get_conperiod(number_produk.intValue()));
						produk.ii_contract_period = ins_period;
					}else{
						ins_period = new Integer(produk.of_get_conperiod(number_produk.intValue()));
					}
					
					if(produk.ii_bisnis_id==186){
						if(ins_period<5 || ins_period>20){
							if(ins_period!=79){
								err.rejectValue("datausulan.mspr_ins_period","","HALAMAN DATA USULAN : Untuk produk Progressive Link, masa Asuransi yg diperbolehkan hanya 5-20 Tahun dan Seumur Hidup(79 Tahun)");
							}
						}
					}else if(produk.ii_bisnis_id==177 || produk.ii_bisnis_id==207 ){
						if(produk.ii_bisnis_no==1){
							if(ins_period<4 || ins_period>20){
								err.rejectValue("datausulan.mspr_ins_period","","HALAMAN DATA USULAN : Untuk produk Progressive Save, masa Asuransi yg diperbolehkan hanya 4-20 Tahun");
							}
						}
					}
					
					
					// cari tgl akhir masa berlaku polis -- Main product
					produk.of_set_begdate(tahun1.intValue(),bulan1.intValue(),tanggal1.intValue());
					tanggal= new Integer(produk.idt_end_date.getTime().getDate());
					bulan= new Integer(produk.idt_end_date.getTime().getMonth()+1);
					tahun= new Integer(produk.idt_end_date.getTime().getYear()+1900);
					
					tgl_end=Integer.toString(tanggal.intValue());
					bln_end = Integer.toString(bulan.intValue());
					thn_end = Integer.toString(tahun.intValue());
					if ((tgl_end.equalsIgnoreCase("0")==true) || (bln_end.equalsIgnoreCase("0")==true) || (thn_end.equalsIgnoreCase("0")==true))
					{
						edit.getDatausulan().setMste_end_date(null);
					}else{
						String tanggal_end_date = FormatString.rpad("0",tgl_end,2)+"/"+FormatString.rpad("0",bln_end,2)+"/"+thn_end;
						edit.getDatausulan().setMste_end_date(defaultDateFormat.parse(tanggal_end_date));
					}
					edit.getDatausulan().setLi_umur_ttg((li_umur_ttg));
					edit.getDatausulan().setLi_umur_pp((li_umur_pp));
					edit.getPemegang().setUsiapp((li_umur_pp));
					edit.getTertanggung().setUsiattg((li_umur_ttg));
						
					if (produk.flag_endowment ==  1)
					{
						String hasil_endowmen = a.cek_tanggal_endow(Integer.toString(tahun1.intValue()),Integer.toString(bulan1.intValue()),Integer.toString(tanggal1.intValue()),kode_produk);
						if (hasil_endowmen.trim().length() != 0)
						{
							err.rejectValue("datausulan.mste_beg_date","","HALAMAN DATA USULAN :" + hasil_endowmen);

						}
					}					
					
					Double persen = new Double(0);
					//persentase dplk khusus produk horizon
					 if (flag_horison.intValue() == 1)
					 {
						 persen = edit.getPemegang().getMste_pct_dplk();
						 if(persen == null)
						 {
							 persen = new Double(0);
						 }
						 String hsl_persen_dplk = a.cek_persentase_dplk(persen);
						 if (hsl_persen_dplk.trim().length() != 0)
						 {
							 err.rejectValue("pemegang.mste_pct_dplk", "", "HALAMAN DATA USULAN :" +hsl_persen_dplk);
						 }else{
							 edit.getPemegang().setMste_pct_dplk(persen);
						 }
					 }else{
						 edit.getPemegang().setMste_pct_dplk(new Double(0));
					 }
					produk.cek_guthrie(mste_flag_guthrie);
					
					//cek kurs up dan premi 
					if (produk.flag_uppremi==0){
						kurs=kurs_up;
						for ( int m=0 ; m< produk.indeks_is_forex; m++ )
						{
							if (kurs_up.equalsIgnoreCase(produk.is_forex[m]))
							{
								cek_kurs_up=new Boolean(true);
							}
						}
						if (cek_kurs_up.booleanValue()==false)
						{
							hasil_kurs_up="Plan ini tidak bisa dengan kurs tersebut, Silahkan memilih dengan kurs up lain";
							hasil_kurs_premi="";
							err.rejectValue("datausulan.lku_id","","HALAMAN DATA USULAN :" +hasil_kurs_up);
						}
					}
							
					if (produk.flag_uppremi==1){
						kurs=kurs_premi;
						for ( int n=0 ; n< produk.indeks_is_forex; n++ )
						{
							if (kurs_premi.equalsIgnoreCase(produk.is_forex[n]))
							{
								cek_kurs_premi=new Boolean(true);
							}
						}
						if (cek_kurs_premi.booleanValue()==false)
						{
							hasil_kurs_premi="Plan ini tidak bisa dengan kurs tersebut, Silahkan memilih dengan kurs premi lain";
							hasil_kurs_up="";	
							err.rejectValue("datausulan.lku_id","","HALAMAN DATA USULAN :" +hasil_kurs_premi);	
						}
					}			
					
					f_hit_umur n_date=  new f_hit_umur();
					Date tgl_input = edit.getPemegang().getMspo_input_date();
					Integer t1 = tgl_input.getDate();
					Integer b1 = tgl_input.getMonth()+1;
					Integer th1 = tgl_input.getYear()+1900;
					Integer hari = n_date.hari1( 2007,12, 31,th1, b1, t1);
					
					// 2 VALIDASI MAXI DEPOSIT DIBAWAH DIPINDAH KE TRANS BAC TO UW
					
					//tgl 31 Oktober 2007. Produk Maxi Deposit Rp  tidak boleh diinput lagi
//					if (kode_produk.intValue() == 103 || kode_produk.intValue() == 137)
//					{
//						if (hari > 0)
//						{
//							err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN :Produk Maxi Deposit Rp dapat di proses oleh bagian Undw adalah SPAJ yang diterima oleh AJS paling lambat tgl 31 Desember 2007.");	
//						}
//					}
						
					//  Produk Maxi Deposit Rp dapat dijual smp dengan Tgl 31 Oktober 2007 tidak boleh diinput lagi
//					Integer harii = n_date.hari1( 2007,12, 31,tahun1, bulan1, tanggal1);
//					if (kode_produk.intValue() == 103 || kode_produk.intValue() == 137)
//					{
//						if (harii > 0)
//						{
//							err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN :Produk Maxi Deposit Rp dapat dijual smp dengan Tgl 31 Desember 2007.");	
//						}
//					}
					
					//flag
					produk.of_set_kurs(kurs);
					edit.getDatausulan().setKurs_premi(kurs);
					edit.getDatausulan().setKurs_p(kurs);
					kode_flag=new Integer(produk.kode_flag);
					edit.getDatausulan().setKode_flag(kode_flag);
					flag_bao=new Boolean(produk.isProductBancass);
					
					if (flag_bao.booleanValue()==true)
					{
						flag_bao1=new Integer(1);
					}else{
						flag_bao1=new Integer(0);
					}
					
					Double total_premi_tu = new Double(0);
					edit.getDatausulan().setFlag_bao(flag_bao1);
					Integer cara_premi = edit.getDatausulan().getCara_premi();
					edit.getDatausulan().setCara_premi(cara_premi);
					if (cara_premi == null)
					{
						cara_premi = new Integer(0);
					}
					pmode_id = edit.getDatausulan().getLscb_id();
					
					//khusus untuk excellink 80 plus syariah
					/*if (((kode_produk.intValue() == 153)||(kode_produk.intValue() == 116) || (kode_produk.intValue() == 118)||(kode_produk.intValue() == 153)||(kode_produk.intValue() == 159)|| (kode_produk.intValue() == 160)) && (pmode_id.intValue() == 6) )
					{
						if (kurs.equalsIgnoreCase("01"))
						{
							if ((premi.doubleValue() >=150000) && (premi.doubleValue() < 250000))
							{
								String pil_kombinasi = edit.getDatausulan().getKombinasi();
								if (pil_kombinasi==null)
								{
									pil_kombinasi = "D";
									edit.getDatausulan().setKombinasi(pil_kombinasi);
									Double totalpremi_kombinasi = premi;
									edit.getDatausulan().setTotal_premi_kombinasi(totalpremi_kombinasi);
									cara_premi = new Integer(1);
									edit.getDatausulan().setCara_premi(cara_premi);
								}
							}
						}else{
							if ((premi.doubleValue() >=15) && (premi.doubleValue() < 25))
							{
								String pil_kombinasi = edit.getDatausulan().getKombinasi();
								if (pil_kombinasi==null)
								{
									pil_kombinasi = "D";
									edit.getDatausulan().setKombinasi(pil_kombinasi);
									Double totalpremi_kombinasi = premi;
									edit.getDatausulan().setTotal_premi_kombinasi(totalpremi_kombinasi);
									cara_premi = new Integer(1);
									edit.getDatausulan().setCara_premi(cara_premi);
								}
							}
						}
					}*/
					// untuk produk link
					if (kode_flag.intValue() > 1 )
					{
						if (cara_premi.intValue()==1)
						{
							String pil_kombinasi = edit.getDatausulan().getKombinasi();
							Double premi_pokok = new Double(0);
							Double premi_tp = new Double(0);
							Double totalpremi_kombinasi = edit.getDatausulan().getTotal_premi_kombinasi();
							//pilihan kombinasi persentase antara premi pokok dengan premi top up
							if (pil_kombinasi!=null)
							{
								if (totalpremi_kombinasi == null)
								{
									totalpremi_kombinasi =new Double(0);
									edit.getDatausulan().setTotal_premi_kombinasi(totalpremi_kombinasi);
									err.rejectValue("datausulan.total_premi_kombinasi","","HALAMAN DATA USULAN : Untuk cara premi tersebut , Silahkan masukkan total premi terlebih dahulu.");
								}//else{
								
								// khusus untuk stable link - revisi Yusuf (28/04/08)
								if (kode_flag.intValue() == 11 || kode_flag.intValue() == 15 || kode_flag.intValue() == 16){
									//RUPIAH
									if (kurs.equalsIgnoreCase("01")){
										if(produk.ii_bisnis_id==164 || produk.ii_bisnis_id==174){
											if (totalpremi_kombinasi.doubleValue() > 20000000){
												premi_pokok = new Double(20000000);
												premi_tp = new Double(totalpremi_kombinasi.doubleValue() - 20000000);
											}else {
												premi_pokok = totalpremi_kombinasi;
											}
										}else if(produk.ii_bisnis_id==186){
											if(edit.getDatausulan().getLscb_id()==3){
												if (totalpremi_kombinasi.doubleValue() > 2000000){
													premi_pokok = new Double(2000000);
													premi_tp = new Double(totalpremi_kombinasi.doubleValue() - 2000000);
												}else {
													premi_pokok = totalpremi_kombinasi;
												}
											}else if(edit.getDatausulan().getLscb_id()==2){
												Double faktor_premi = new Double(2000000/2);
												if (totalpremi_kombinasi.doubleValue() > faktor_premi){
													premi_pokok = new Double(faktor_premi);
													premi_tp = new Double(totalpremi_kombinasi.doubleValue() - faktor_premi);
												}else {
													premi_pokok = totalpremi_kombinasi;
												}
											}else if(edit.getDatausulan().getLscb_id()==1){
												Double faktor_premi = new Double(2000000/4);
												if (totalpremi_kombinasi.doubleValue() > faktor_premi){
													premi_pokok = new Double(faktor_premi);
													premi_tp = new Double(totalpremi_kombinasi.doubleValue() - faktor_premi);
												}else {
													premi_pokok = totalpremi_kombinasi;
												}
											}else if(edit.getDatausulan().getLscb_id()==6){
												Double faktor_premi = new Double(2000000/12);
												if (totalpremi_kombinasi.doubleValue() > faktor_premi){
													premi_pokok = new Double(faktor_premi);
													premi_tp = new Double(totalpremi_kombinasi.doubleValue() - faktor_premi);
												}else {
													premi_pokok = totalpremi_kombinasi;
												}
											}
											
										}
										
									//DOLLAR
									}else{
										if(produk.ii_bisnis_id==164 || produk.ii_bisnis_id==174){
											if (totalpremi_kombinasi.doubleValue() > 2000){
												premi_pokok = new Double(2000);
												premi_tp = new Double(totalpremi_kombinasi.doubleValue() - 2000);
											}else {
												premi_pokok = totalpremi_kombinasi;
											}
										}else if(produk.ii_bisnis_id==186){
											if(edit.getDatausulan().getLscb_id()==3){
												if (totalpremi_kombinasi.doubleValue() > 200){
													premi_pokok = new Double(200);
													premi_tp = new Double(totalpremi_kombinasi.doubleValue() - 200);
												}else {
													premi_pokok = totalpremi_kombinasi;
												}
											}else if(edit.getDatausulan().getLscb_id()==2){
												Double faktor_premi = new Double(200/2);
												if (totalpremi_kombinasi.doubleValue() > 200){
													premi_pokok = new Double(faktor_premi);
													premi_tp = new Double(totalpremi_kombinasi.doubleValue() - faktor_premi);
												}else {
													premi_pokok = totalpremi_kombinasi;
												}
											}else if(edit.getDatausulan().getLscb_id()==1){
												Double faktor_premi = new Double(200/4);
												if (totalpremi_kombinasi.doubleValue() > 200){
													premi_pokok = new Double(faktor_premi);
													premi_tp = new Double(totalpremi_kombinasi.doubleValue() - faktor_premi);
												}else {
													premi_pokok = totalpremi_kombinasi;
												}
											}else if(edit.getDatausulan().getLscb_id()==6){
												Double faktor_premi = new Double(200/12);
												if (totalpremi_kombinasi.doubleValue() > 200){
													premi_pokok = new Double(faktor_premi);
													premi_tp = new Double(totalpremi_kombinasi.doubleValue() - faktor_premi);
												}else {
													premi_pokok = totalpremi_kombinasi;
												}
											}
											
											if (totalpremi_kombinasi.doubleValue() > 200){
												premi_pokok = new Double(200);
												premi_tp = new Double(totalpremi_kombinasi.doubleValue() - 200);
											}else {
												premi_pokok = totalpremi_kombinasi;
											}
										}
										
									}
									if(edit.getDatausulan().getLsbs_id()==186){
										edit.getDatausulan().setCara_premi(0);
									}else{
										edit.getDatausulan().setCara_premi(1);
									}
									
									edit.getDatausulan().setMspr_premium(premi_pokok);
									edit.getInvestasiutama().getDaftartopup().setPremi_berkala(0.);
									edit.getInvestasiutama().getDaftartopup().setPil_berkala(null);
									edit.getInvestasiutama().getDaftartopup().setPremi_tunggal(premi_tp);
									if(premi_tp > 0) edit.getInvestasiutama().getDaftartopup().setPil_tunggal(2);
									total_premi_tu = premi_tp;
									premi = premi_pokok;
									Double total_seluruhnya = new Double(premi_pokok.doubleValue()+ premi_tp.doubleValue());
									edit.getInvestasiutama().setTotal_premi_sementara(total_seluruhnya);
									edit.getDatausulan().setTotal_premi_sementara(total_seluruhnya);
									
								}else{
									//pengecekan kombinasi berapa saja yang diperbolehkan
									String hasil_kombinasi="";
									if("129,127,120,128".indexOf(edit.getDatausulan().getLsbs_id().toString())>-1){
										produk.f_kombinasi();
									}
									int indeks_kombinasi=produk.indeks_kombinasi;
									Integer ok = new Integer(0);
									
									for (int j=0 ; j <indeks_kombinasi; j++)
									{
										if(ok==1) break;
										String lcaId_x = edit.getCurrentUser().getLca_id();
										Integer lsbsid_x = edit.getDatausulan().getLsbs_id();
										if(lcaId_x.equals("37") || lcaId_x.equals("52") || lcaId_x.equals("63")){ // *agency
											if (pil_kombinasi.equalsIgnoreCase(produk.kombinasi[j]))
											{
												ok = new Integer(1);
											}
										}else{
											if(("K,L,N,M,O,P,Q,R,S,T,U".indexOf(pil_kombinasi)>-1)){ // *Kombinasi ganjil
												ok = new Integer(1);
											}else if (pil_kombinasi.equalsIgnoreCase(produk.kombinasi[j])){
												ok = new Integer(1);
											}else{
												ok = new Integer(0);
											}
										}
									}
									if (ok.intValue() == 0)
									{
										if(produk.ii_bisnis_id==191){
											//apabila tidak dipilih, default 80 PP : 20 PT
											premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 80 /100);
											premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 20 /100);
											edit.getDatausulan().setMspr_premium(premi_pokok);
											edit.getInvestasiutama().getDaftartopup().setPremi_berkala(premi_tp);
											edit.getInvestasiutama().getDaftartopup().setPil_berkala(1);
											total_premi_tu = premi_tp;
											premi = premi_pokok;
											Double total_seluruhnya = new Double(premi_pokok.doubleValue()+ premi_tp.doubleValue());
											edit.getInvestasiutama().setTotal_premi_sementara(total_seluruhnya);
										}else{
											hasil_kombinasi="Kombinasi premi tersebut tidak boleh untuk plan tersebut.";
											edit.getDatausulan().setTotal_premi_kombinasi(new Double(0));
											err.rejectValue("datausulan.kombinasi", "", "HALAMAN DATA USULAN : "+hasil_kombinasi);
										}
									}									
									
									if(produk.ii_bisnis_id==191 && Common.isEmpty(edit.getDatausulan().getMspo_installment())){
										err.rejectValue("datausulan.mspo_installment", "", "HALAMAN DATA USULAN : Untuk Produk Rencana Cerdas, harap mengisi Cuti Premi Untuk masa Pembayaran yang diinginkan.");
									}
																
									if (pil_kombinasi.equalsIgnoreCase("A")){
										premi_pokok = totalpremi_kombinasi;
									}else if  (pil_kombinasi.equalsIgnoreCase("B")){
											premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 90 /100);
											premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 10 /100);
										}else if  (pil_kombinasi.equalsIgnoreCase("C")){
												premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 80 /100);
												premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 20 /100);
											}else if  (pil_kombinasi.equalsIgnoreCase("D")){
													premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 70 /100);
													premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 30 /100);
												}else  if  (pil_kombinasi.equalsIgnoreCase("E")){
														premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 60 /100);
														premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 40 /100);
													}else if  (pil_kombinasi.equalsIgnoreCase("F")){
															premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 50 /100);
															premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 50 /100);
														}else if  (pil_kombinasi.equalsIgnoreCase("G")){
																premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 40 /100);
																premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 60 /100);
															}else if  (pil_kombinasi.equalsIgnoreCase("H")){
																	premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 30 /100);
																	premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 70 /100);
																}else if  (pil_kombinasi.equalsIgnoreCase("I")){
																		premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 20 /100);
																		premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 80 /100);
																	}else if  (pil_kombinasi.equalsIgnoreCase("J")){
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 10 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 90 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("K")){
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 95 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 5 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("L")){
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 85 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 15 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("M")){
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 75 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 25 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("N")){
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 65 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 35 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("O")){
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 55 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 45 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("P")){
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 45 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 55 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("Q")){
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 35 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 65 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("R")){
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 25 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 75 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("S")){
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 15 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 85 /100);
																		}else if  (pil_kombinasi.equalsIgnoreCase("T")){
																			premi_pokok = new Double(totalpremi_kombinasi.doubleValue() * 5 /100);
																			premi_tp = new Double(totalpremi_kombinasi.doubleValue() * 95 /100);
																		}
										if("24,25,26".indexOf(edit.getDatausulan().getFlag_paket().toString())>-1){
											edit.getDatausulan().setMspr_premium(premi_pokok);
											edit.getInvestasiutama().getDaftartopup().setPremi_tunggal(premi_tp);
											edit.getInvestasiutama().getDaftartopup().setPil_tunggal(2);
											total_premi_tu = premi_tp;
											premi = premi_pokok;										
										}else{
											edit.getDatausulan().setMspr_premium(premi_pokok);
											edit.getInvestasiutama().getDaftartopup().setPremi_berkala(premi_tp);
											edit.getInvestasiutama().getDaftartopup().setPil_berkala(1);
											total_premi_tu = premi_tp;
											premi = premi_pokok;										
										}
										Double total_seluruhnya = new Double(premi_pokok.doubleValue()+ premi_tp.doubleValue());
										edit.getInvestasiutama().setTotal_premi_sementara(total_seluruhnya);
									}
								//}
							}
						}
					}else{
						if (cara_premi.intValue() == 1)
						{
							err.rejectValue("datausulan.cara_premi","","HALAMAN DATA USULAN : Produk ini bukan produk investasi ,Silahkan pilih cara premi yang hanya mengisi premi pokok .");
						}
						edit.getDatausulan().setTotal_premi_kombinasi(new Double(0));
						edit.getInvestasiutama().setTotal_premi_sementara(new Double(0));
						edit.getDatausulan().setKombinasi(null);
					}
					
					//tambahan Yusuf - cara premi untuk stable link harus isi total premi, bukan isi premi pokok saja
					if((products.stableLink(String.valueOf(produk.ii_bisnis_id)))) {
//							|| produk.ii_bisnis_id==186) && 1 != cara_premi) {
						if((products.stableLink(String.valueOf(produk.ii_bisnis_id)) && produk.ii_bisnis_id!=186 && 1 != cara_premi)){
							err.rejectValue("datausulan.cara_premi","","HALAMAN DATA USULAN : Khusus STABLE LINK, harus pilih ISI TOTAL PREMI DAN PILIH KOMBINASI (kombinasinya tidak perlu diisi).");
						}
					}
					
					if(produk.ii_bisnis_id==186 && 1==cara_premi){
						err.rejectValue("datausulan.cara_premi","","HALAMAN DATA USULAN : Untuk PROGRESSIVE LINK, harus pilih ISI PREMI.");
					}

					//if(pmode_id==6 && edit.getDatausulan().getMste_flag_cc()==0)
					//	err.rejectValue("datausulan.mste_flag_cc","","Cara Bayar Bulanan Harus AutoDebet");
						
					produk.ii_pmode=pmode_id.intValue();	
//					Integer flag_excell80plus = new Integer(produk.flag_excell80plus);
//					int flag_debet = produk.flag_debet;
//					String hasil_autodebet=a.autodebet(produk.ii_bisnis_id, produk.ii_bisnis_no, Integer.toString(autodebet.intValue()),lca_id,flag_account.intValue(),pmode_id.intValue(),flag_debet,flag_excell80plus.intValue());
//					if (hasil_autodebet.trim().length()!=0)
//					{
//						edit.setPesan("PERHATIAN!!! MOHON DIPERHATIKAN APAKAH BENAR CARA BAYAR TUNAI, KARENA SEHARUSNYA AUTODEBET!");
////						err.rejectValue("datausulan.mste_flag_cc","","HALAMAN DATA USULAN :" +hasil_autodebet);
//					}
					
					// untuk khusus worksite wajib pilih autodebet
					/*if (produk.flag_worksite == 1)
					{
						if (autodebet.intValue() == 0 && pmode_id.intValue() != 0 && produk.flag_medivest!=1)
						{
							err.rejectValue("datausulan.mste_flag_cc","","HALAMAN DATA USULAN : Silahkan pilih jenis autodebet terlebih dahulu");
						}
					}*/
					
					if (produk.flag_mediplan == 1)
					{
						rate_plan=new Double(produk.of_get_rate());
						premi=new Double(produk.idec_premi_main);
						up = new Double(produk.idec_up);
						edit.getDatausulan().setMspr_tsi(up);
					}
					
					//up
					if (hasil_up.trim().length()==0)
					{
												
						if (produk.flag_uppremi==0 && produk.flag_default_up==0)
						{
							hasil_up="";
							//edit.getDatausulan().setMspr_tsi(up);
							//edit.getDatausulan().setMspr_premium(null);
							hasil_up=produk.of_alert_min_up(up.doubleValue());
							
							if (hasil_up.trim().length()!=0 && sp != 1)
							{
								err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :" +hasil_up);
							}else{
								hasil_up=produk.of_alert_max_up(up.doubleValue());	
								if (hasil_up.trim().length()!=0 && sp != 1)
								{
									err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :" +hasil_up);
								}else{
									hasil_up=produk.cek_kelipatan_up(up.doubleValue());	
									if (hasil_up.trim().length()!=0 && sp != 1)
									{
										err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :" +hasil_up);
									}
								}	
							}
							
							produk.of_set_up(up.doubleValue());	
						}
						
						if(produk.flag_default_up==1){
							up = produk.set_default_up(up.doubleValue());
						}
				
					//premi
					if (hasil_premi.trim().length()==0)
					{
						if (produk.flag_uppremi==1)
						{
							hasil_premi="";
							//edit.getDatausulan().setMspr_tsi(null);
							//edit.getDatausulan().setMspr_premium(premi);
							if (mste_flag_guthrie == 1)
							{
								produk.idec_min_up01 = 50000;
							}
							
							if(edit.getDatausulan().getLsbs_id()==186 && edit.getDatausulan().getLsdbs_number()==5){//succesful family save
								hasil_premi=produk.of_alert_min_premi_With_flag_bulanan(premi.doubleValue(), flag_bulanan);
							}else if(edit.getDatausulan().getLsbs_id()==121 || (edit.getDatausulan().getLsbs_id()==120 && edit.getDatausulan().getLsdbs_number()>=13 && edit.getDatausulan().getLsdbs_number()<=15) && sp != 1){
								hasil_premi=produk.of_alert_min_premi(total_premi_tu!=null?premi.doubleValue()+total_premi_tu.doubleValue():premi.doubleValue());
							}else if(edit.getDatausulan().getLsbs_id()==141){
								hasil_premi=produk.of_alert_min_premi(up.doubleValue());
							}else if(edit.getDatausulan().getLsbs_id()==120 && edit.getDatausulan().getLsdbs_number()>=10 && edit.getDatausulan().getLsdbs_number()<=11 && sp != 1){ // *simas power link 5 dan 10 
								hasil_premi=produk.of_alert_min_premi_with_topup(edit.getInvestasiutama().getTotal_premi_sementara());
							}else{
								if(edit.getDatausulan().getLsbs_id()==164 && "1,4".indexOf(edit.getDatausulan().getMste_flag_el().toString())>-1){
									hasil_premi=produk.of_alert_min_premi_karyawan(premi.doubleValue());
								}else{
									if(edit.getInvestasiutama().getTotal_premi_sementara()!=null){
										if(edit.getInvestasiutama().getTotal_premi_sementara()>premi){
											hasil_premi=produk.of_alert_min_premi(edit.getInvestasiutama().getTotal_premi_sementara());
										}else{
											hasil_premi=produk.of_alert_min_premi(premi.doubleValue());
										}
									}else{
										hasil_premi=produk.of_alert_min_premi(premi.doubleValue());
									}
								}
							}
	
							if (hasil_premi.trim().length()!=0 && sp != 1)
							{
								err.rejectValue("datausulan.mspr_premium","","HALAMAN DATA USULAN :" +hasil_premi);
							}else{
								if(edit.getDatausulan().getLsbs_id()==141){
									hasil_premi=produk.of_alert_max_premi(premi.doubleValue(), up.doubleValue()); // *khusus EDUVEST, isian berupa premi, namun dicek untuk max dan min UP nya serta di set UP nya.
								}else{
									hasil_premi=produk.of_alert_max_premi(premi.doubleValue(), up.doubleValue());
								}
								if (hasil_premi.trim().length()!=0 && sp != 1)
								{
									err.rejectValue("datausulan.mspr_premium","","HALAMAN DATA USULAN :" +hasil_premi);
								}else{
									hasil_premi=produk.cek_kelipatan_premi(premi.doubleValue());	
								}
							}							
						}
					}		
	
					//cek cara bayar
					cek_pmode=new Boolean(false);
					for ( int i=1 ; i<produk.indeks_ii_pmode_list; i++ )
					{
						if (Integer.toString(pmode_id.intValue()).equalsIgnoreCase(Integer.toString(produk.ii_pmode_list[i])))
						{
							cek_pmode=new Boolean(true);
						}
					}
					
					if(products.progressiveLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id))){
						if(products.progressiveLink(Integer.toString(produk.ii_bisnis_id)) ){
							edit.getPowersave().setMsl_bp_rate(new Double(90));
						}
						if(products.progressiveLink(Integer.toString(produk.ii_bisnis_id)) && produk.ii_bisnis_no==3 ){
							if(pmode_id==6){//bulanan
								edit.getPowersave().setMsl_mgi(12);
								edit.getPowersave().setMps_jangka_inv("12");
							}
						}else{
							if(pmode_id==3){//tahunan
								edit.getPowersave().setMsl_mgi(12);
								edit.getPowersave().setMps_jangka_inv("12");
							}else if(pmode_id==2){//semesteran
								edit.getPowersave().setMsl_mgi(6);
								edit.getPowersave().setMps_jangka_inv("6");
							}else if(pmode_id==1){//triwulanan
								edit.getPowersave().setMsl_mgi(3);
								edit.getPowersave().setMps_jangka_inv("3");
							}else if(pmode_id==6){//bulanan
								edit.getPowersave().setMsl_mgi(1);
								edit.getPowersave().setMps_jangka_inv("1");
							}
						}
					}
	
					if (cek_pmode.booleanValue()==false){
						hasil_cek_pmode="Cara bayar tersebut tidak bisa untuk plan ini.";
						err.rejectValue("datausulan.lscb_id","","HALAMAN DATA USULAN :" +hasil_cek_pmode);
					}
	
					if (hasil_cek_pmode.trim().length()==0){
						produk.of_set_pmode(pmode_id.intValue());
						if(produk.ii_bisnis_id==177 || (produk.ii_bisnis_id==186 && produk.ii_bisnis_no!=3) || produk.ii_bisnis_id==191 ){
							pay_period = edit.getDatausulan().getMspo_pay_period();
						}else if(produk.ii_bisnis_id==207){
							pay_period = edit.getDatausulan().getMspr_ins_period();
						}else{
							pay_period = new Integer(produk.of_get_payperiod());
						}
						
						if (produk.flag_uppremi==0){
							rate_plan=new Double(produk.of_get_rate());
							if (rate_plan.doubleValue()==0 && produk.flag_endowment == 1){
								err.rejectValue("datausulan.kodeproduk","","HALAMAN DATA USULAN :Rate Premi untuk plan ini belum ada.");
							}

							premi=new Double(produk.idec_premi_main);
							
							if (produk.flag_mediplan == 1){
								up = new Double(produk.idec_up);
								edit.getDatausulan().setMspr_tsi(up);
							}
							if (premi.doubleValue() == 0){
								err.rejectValue("datausulan.mspr_premium","","HALAMAN DATA USULAN :Premi masih nol, silahkan cek rate, dan up nya lagi.");
							}
							if(kode_produk.intValue()==130 || kode_produk.intValue()==45){
								if(kurs.equals("01")){
									if(premi.doubleValue() > new Double(1000000000) && sp != 1){
										err.rejectValue("datausulan.mspr_premium","","HALAMAN DATA USULAN :Premi maximum Rp.1.000.000.000,-");
									}
								}else{
									if(premi.doubleValue() > new Double(100000) && sp != 1){
										err.rejectValue("datausulan.mspr_premium","","HALAMAN DATA USULAN :Premi maximum $100.000,-");
									}
								}
							}
						}
						//ryan
						if(kode_produk.intValue()==142 && number_produk.intValue()==10||kode_produk.intValue()==164 && number_produk.intValue()==10){
							edit.getTertanggung().setMste_flag_hadiah(1);
							if(kurs.equals("01")){
								if(premi.doubleValue() > new Double(100000000) && sp != 1){
									err.rejectValue("datausulan.mspr_premium","","HALAMAN DATA USULAN :Premi maximum Rp.100.000.000,-");
								}
							}else{
								if(premi.doubleValue() > new Double(10000) && sp != 1){
									err.rejectValue("datausulan.mspr_premium","","HALAMAN DATA USULAN :Premi maximum $10.000,-");
								}
							}
						}
						
						if(kode_produk.intValue()==184 && number_produk.intValue()==6){
							edit.getTertanggung().setMste_flag_hadiah(1);
						}
						double fltpersen=0;
						edit.getDatausulan().setFlag_uppremi(produk.flag_uppremi);
						//lufi--
						if(kode_produk.intValue()==120 && "19,20,22,23".indexOf(edit.getDatausulan().getLsdbs_number().toString())>-1||kode_produk.intValue()==202 && "4,5".indexOf(edit.getDatausulan().getLsdbs_number().toString())>-1)produk.flag_uppremiopen=1;//produk ini up maksnya hasil perkalian faktor jadi tidak perlu set up
						if (produk.flag_uppremi==1){
							if ((produk.flag_uppremiopen==1 && pmode_id.intValue()!=0 )|| (flag_ekalink.intValue() == 1) || (produk.flag_artha == 1) || (produk.flag_cerdas_global ==1)){
								fltpersen = produk.of_get_fltpersen(pmode_id);
	
								if (Double.toString(up.doubleValue()).equalsIgnoreCase("0.0") && (produk.flag_cerdas_global !=1)){
									produk.of_set_premi(premi.doubleValue());
									up=new Double(produk.idec_up);
									if (flag_horison.intValue() == 1){
										up = new Double((100 - persen.doubleValue())/100 * up.doubleValue());
									}
									if (up.doubleValue() == 0){
										err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :Up masih nol, silahkan cek rate, dan premi nya lagi.");
									}
								}else{
									hasil_up = produk.cek_min_up(premi,up,kurs,pmode_id);
									if (hasil_up.trim().length()!=0){
										if (produk.flag_worksite == 1 || (edit.getCurrentUser().getLca_id().equals("58") || produk.ii_bisnis_id==191)){
											up = produk.min_up(premi, up, kurs, pmode_id);
										}else if(sp != 1){
											err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :" +hasil_up);
										}
									}
									
									hasil_up = produk.cek_max_up(li_umur_ttg,kode_produk,premi,up,new Double(fltpersen),pmode_id,kurs);
									if (hasil_up.trim().length()!=0 && sp != 1){
										err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :" +hasil_up);
									}
									
								}
								
								// untuk worksite yang bisa ubah up
								if (produk.flag_worksite == 1 && produk.flag_uppremiopen==1 ){	
									hasil_up = produk.cek_min_up(premi,up,kurs,pmode_id);
									if (hasil_up.trim().length()!=0){
										up = produk.min_up(premi, up, kurs, pmode_id);
									}
								
									hasil_up = produk.cek_max_up(li_umur_ttg,kode_produk,premi,up,new Double(fltpersen),pmode_id,kurs);
									if (hasil_up.trim().length()!=0 && sp != 1){
										err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :" +hasil_up);
									}
								}
								
							}else{
								if(edit.getDatausulan().getLsbs_id()==164 && "1,4".indexOf(edit.getDatausulan().getMste_flag_el().toString())>-1 ){
									produk.of_set_premi_karyawan(premi.doubleValue());
								}else if (edit.getDatausulan().getLsbs_id()==184 &&edit.getDatausulan().getLsdbs_number()==6&& li_umur_pp>=69){
									produk.of_set_premi_50(premi.doubleValue());
								}else{
									produk.of_set_premi(premi.doubleValue());
								}
								
								up = new Double(produk.idec_up);
								//khusus produk horizon yang mengisi persentase dplk
								if (flag_horison.intValue() == 1){
									up = new Double((100 - persen.doubleValue())/100 * up.doubleValue());
								}
								if (pmode_id.intValue()==0 && produk.kode_flag > 1 && produk.kode_flag !=11  && produk.kode_flag != 15 && produk.kode_flag !=16 && produk.flag_worksite == 0){
									if (kurs.equalsIgnoreCase("01")){
										if (up < 15000000){
											up = new Double(15000000);
										}
									}else{
										if (up < 1500){
											up = new Double(1500);
										}
									}
								}
								
								if (produk.flag_worksite == 1){
									hasil_up = produk.cek_min_up(premi,up,kurs,pmode_id);
									if (hasil_up.trim().length()!=0){
										if (produk.flag_worksite == 1){
											up = produk.min_up(premi, up, kurs, pmode_id);
										}else if(sp != 1){
											err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN :" +hasil_up);
									
										}
									}
								}
							}
						}
					}
				}
					
				 if("183,189,195,204".indexOf(kode_produk.toString())>-1){
					edit.getDatausulan().setMspo_provider(2);
				 }				
				  
				//(Deddy)- Untuk Cerdas, Upnya maksimal tergantung pada usia tertanggung, rumus : factor * UP yang diinput	
				if(kode_produk.intValue() == 127){
					logger.info(edit.getDatausulan().getMspr_tsi());
					if(edit.getDatausulan().getMspr_tsi() < up && sp != 1){
						BigDecimal upstring= new BigDecimal(up);
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Up minimum : "+ upstring);
					}else{
						Double factor = 0.0;
						if(li_umur_ttg >= 1 && li_umur_ttg<=19){
							factor = 50.0;
						}else if(li_umur_ttg >= 20 && li_umur_ttg <= 29){
							factor = 40.0;
						}else if(li_umur_ttg >= 30 && li_umur_ttg <= 39){
							factor = 30.0;
						}else if(li_umur_ttg >= 40 && li_umur_ttg <= 49){
							factor = 20.0;
						}else if(li_umur_ttg >= 50 && li_umur_ttg <= 60){
							factor = 10.0;
						}
						Double factor_premi = 1.0;
						if(edit.getDatausulan().getLscb_id()==6){//bulanan
							factor_premi = 12.0;
						}else if(edit.getDatausulan().getLscb_id()==1){//triwulanan
							factor_premi = 4.0;
						}else if(edit.getDatausulan().getLscb_id()==2){//semesteran
							factor_premi = 2.0;
						}
						Double up_sementara = factor * (edit.getDatausulan().getMspr_premium() * factor_premi);
						BigDecimal upstringlagi = new BigDecimal(up_sementara);
						if(edit.getDatausulan().getMspr_tsi()> up_sementara && sp != 1){
							err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Up maximum : "+ upstringlagi);
						}else up = edit.getDatausulan().getMspr_tsi();
					}
				}
				
				//MANTA - CEK TOTAL PREMI DAN TOP UP UNTUK PRODUK STABLE LINK
				if(products.stableLink(edit.getDatausulan().getLsbs_id().toString()) && edit.getDatausulan().getLsbs_id()!=186){
					Double inpremi= new Double(0); Double jmlpremi = new Double(0);
					Double premitmp = new Double(0); Double subpremi= new Double(0);
					List daftar = uwManager.selectPunyaStableLinkAll(
							edit.getPemegang().getMspe_date_birth(), edit.getPemegang().getMcl_first(),
							edit.getTertanggung().getMspe_date_birth(), edit.getTertanggung().getMcl_first());
					if(!daftar.isEmpty()){
						for(int i=0;i<daftar.size();i++){
							Map m = (Map) daftar.get(i);
							String spaj = (String) m.get("REG_SPAJ");							
							Map tmp = uwManager.selectPremiStableLink(spaj);
							if(tmp != null && !tmp.get("PREMI").toString().equals("0")){
								premitmp = Double.parseDouble(tmp.get("PREMI").toString());
								if(tmp.get("KURS").toString().equals("02")){
									premitmp = premitmp * 10000;
								}
								subpremi = subpremi + premitmp;
							}
						}
					}
					if(edit.getDatausulan().getTotal_premi_kombinasi() != null && edit.getDatausulan().getTotal_premi_kombinasi().doubleValue() != 0.0){
						inpremi = Double.parseDouble(edit.getDatausulan().getTotal_premi_kombinasi().toString());
					}else{
						inpremi = new Double(edit.getDatausulan().getMspr_premium());
					}
					if(kurs.equalsIgnoreCase("02")) inpremi = inpremi * 10000;
					
					jmlpremi = inpremi + subpremi;
					if(jmlpremi > 25000000000.0){
						err.rejectValue("datausulan.mspr_premium","","HALAMAN DATA USULAN : PP sudah memiliki produk Stable Link dengan Total Premi dan Top Up sudah lebih dari 25 Milyar Rupiah / 2,5 Juta US Dollar");
					}
				}
				
				//MANTA - Tambahan validasi untuk produk PowerSave dan StableSave
				if("142,143,144,158,175,184".indexOf(edit.getDatausulan().getLsbs_id().toString())>-1){
					Double inpremi= new Double(0); Double jmlpremi = new Double(0);
					Double premitmp = new Double(0); Double subpremi= new Double(0);
					List daftar = uwManager.selectPunyaPowStabSaveAll(
							edit.getPemegang().getMspe_date_birth(), edit.getPemegang().getMcl_first(),
							edit.getTertanggung().getMspe_date_birth(), edit.getTertanggung().getMcl_first());
					if(!daftar.isEmpty()){
						for(int i=0;i<daftar.size();i++){
							Map m = (Map) daftar.get(i);
							String spaj = (String) m.get("REG_SPAJ");							
							Map tmp = uwManager.selectPremiPowStabSave(spaj);
							if(tmp != null && !tmp.get("PREMI").toString().equals("0")){
								premitmp = Double.parseDouble(tmp.get("PREMI").toString());
								if(tmp.get("KURS").toString().equals("02")){
									premitmp = premitmp * 10000;
								}
								subpremi = subpremi + premitmp;
							}
						}
					}
					if(edit.getDatausulan().getTotal_premi_kombinasi() != null && edit.getDatausulan().getTotal_premi_kombinasi().doubleValue() != 0.0){
						inpremi = Double.parseDouble(edit.getDatausulan().getTotal_premi_kombinasi().toString());
					}else{
						inpremi = new Double(edit.getDatausulan().getMspr_premium());
					}
					if(kurs.equalsIgnoreCase("02")) inpremi = inpremi * 10000;
					
					jmlpremi = inpremi + subpremi;
					if(jmlpremi > 25000000000.0 && sp != 1){
						err.rejectValue("datausulan.mspr_premium","","HALAMAN DATA USULAN : PP sudah memiliki produk PowerSave atau StableSave dengan Total Premi dan Top Up sudah lebih dari 25 Milyar Rupiah / 2,5 Juta US Dollar");
					}
				}
				
				if (kode_produk.intValue() == 63 || kode_produk.intValue() == 173){
					li_umur_ttg=li_umur_pp ;
					edit.getTertanggung().setUsiattg((li_umur_ttg));
				}
				
				if(kode_produk.intValue() == 183 || kode_produk.intValue() == 201 || kode_produk.intValue() == 189 || kode_produk.intValue() == 193|| produk.ii_bisnis_id==195 || produk.ii_bisnis_id==204){// *UP Untuk Eka Sehat dan HCP
					up = produk.idec_up;
				}
//				if(kode_produk.intValue() == 208 ){
//					logger.info(produk.idec_up);// *UP
//					if(edit.getDatausulan().getMspr_tsi() > 10000000){
//					up = edit.getDatausulan().getMspr_tsi();
//					}
//				}
					
					edit.getDatausulan().setMspr_tsi(up);
					edit.getDatausulan().setMspr_premium(premi * diskon_karyawan);
					edit.getDatausulan().setRate_plan((rate_plan));
					edit.getDatausulan().setFlag_account((flag_account));
					edit.getDatausulan().setMspr_ins_period((ins_period));
					edit.getDatausulan().setMspo_pay_period((pay_period));
					
					Integer platinum = edit.getDatausulan().getMspo_installment();
					if ( produk.flag_platinumlink == 1){
						if (platinum==null){
							edit.getDatausulan().setMspo_installment(pay_period);
						}else{
							if (platinum.intValue()>pay_period.intValue()){
								err.rejectValue("datausulan.mspo_installment","","Cuti Premi masimal sama dengan masa pembayaran.");
							}else{
								if (platinum.intValue()<2){
									err.rejectValue("datausulan.mspo_installment","","Cuti Premi minimal 2.");
								}
							}
						}
					}else{
						if (produk.flag_cuti_premi == 1){
							if (platinum==null){
								edit.getDatausulan().setMspo_installment(null);
							}else{
								if(kode_produk.intValue() == 121){
									if (pmode_id.intValue() !=0){
										if (platinum.intValue()%5!=0){
											err.rejectValue("datausulan.mspo_installment","","Cuti Premi harus kelipatan 5.Contoh : 5tahun, 10 tahun, 20tahun dan seterusnya.");
										}else{
											if (platinum.intValue()<5){
												err.rejectValue("datausulan.mspo_installment","","Cuti Premi minimal 5.");
											}
										}
									}else{
										edit.getDatausulan().setMspo_installment(new Integer(1));
									}
								}else{
									if (pmode_id.intValue() !=0){
										// *(12/07/2012) Validasi maximum cuti premi dibuka
										if (platinum.intValue()>30){
//											err.rejectValue("datausulan.mspo_installment","","Cuti Premi maximal 30.");
										}else{
											if (platinum.intValue()<1){
												err.rejectValue("datausulan.mspo_installment","","Cuti Premi minimal 1.");
											}
										}
									}else{
										edit.getDatausulan().setMspo_installment(new Integer(1));
									}
								}
							}
						}else{
							edit.getDatausulan().setMspo_installment(null);
						}					
					}
					
				//LUFI(24/02/2015)--ID helpdesk 65663 produk save diset menjadi tanpa pemeriksaan kesehatan
				if(products.powerSave(edit.getDatausulan().getLsbs_id().toString())){
					edit.getDatausulan().setMste_medical(0);
				}
					
				/*
				 * rider
				 */
				Integer hubungan = edit.getPemegang().getLsre_id();
				List dtrd = edit.getDatausulan().getDaftaRider();
				Integer valid= edit.getDatausulan().getIndeks_validasi();
				if (valid==null){valid=new Integer(0);}
				edit.getDatausulan().setIndeks_validasi(valid);
				
				//Tambahan oleh Yusuf (22/2/2008) 
				//Hidup Bahagia ada Rider yang otomatis (semacam rider include), tapi ada premi nya
				/**
					815 payor
					7-5
					8-10
					9-15
					10-20
					
					814 waiver
					6-5
					7-10
					8-15
					9-20
				 */
				if(kode_produk.intValue() == 167) {
					dtrd.clear();
					if(hubungan != 1) { //jika PP <> TT
						//a. dan Pemegang Polis berusia 17 <= x <= (60-MPP) tahun, maka diwajibkan mengikuti asuransi tambahan (Rider) Payor’s Clause
						if(17 <= li_umur_pp && li_umur_pp <= (60 - pay_period)) {
							//HANYA BOLEH DAN WAJIB IKUT PAYOR'S CLAUSE
							if(number_produk.intValue() >= 1 && number_produk <= 4) {						
								int number_rider = number_produk+6;
								Datarider rider = new Datarider(815, number_rider, 0, 1, 0, (double) 0, (double) 0, (double) 0, 0, 0, 815+"~X"+number_rider);
								dtrd.add(rider);
							}
						//b. dan Pemegang Polis berusia >(60-MPP) tahun, maka tidak dapat mengikuti asuransi tambahan (Rider) Payor’s Clause
						}else if(li_umur_pp > (60 - pay_period)){
							//TIDAK BOLEH IKUT PAYOR'S CLAUSE
						}
					}else{ //jika PP = TT
						// dan Pemegang Polis berusia 17 <= x <= (55-MPP) tahun, maka diwajibkan mengikuti asuransi tambahan (Rider) Waiver of Premium Disability
						if(17 <= li_umur_pp && li_umur_pp <= (55 - pay_period)) {
							//WAJIB IKUT WPD
							if(number_produk.intValue() >= 1 && number_produk <= 4) {						
								int number_rider = number_produk+5;
								Datarider rider = new Datarider(814, number_rider, 0, 1, 0, (double) 0, (double) 0, (double) 0, 0, 0, 814+"~X"+number_rider);
								dtrd.add(rider);
							}
						}
					}
				}
				//END OF Tambahan oleh Yusuf (22/2/2008)
				
				Integer factor_up =0;
				Integer jumlah_semua_rider=new Integer(0);
				Double total_premi_rider=new Double(0);
				Integer jumlah_r =new Integer(dtrd.size());
				if (jumlah_r==null){jumlah_r=new Integer(0);}
				
				Integer jmlrider=jumlah_r;
				edit.getDatausulan().setJmlrider(new Integer(jmlrider.intValue() + 1));
				
				Integer tgl_awal_rider = new Integer(0);
				Integer bln_awal_rider = new Integer(0);
				Integer thn_awal_rider = new Integer(0);
				String tanggal_awal_rider =null;
				Integer[] unit_i;
				String hasil_unit1_i;
				String hasil_class1_i;
				Integer[] klases_i;
				Integer[] kd_rider_i;
				String[] nama_rider_i;
				String hasil_rider_i;
				String[] rider_i;
				Double[] sum_i;
				String[] sum_1_i;
				Date[] end_date_rider_i;
				Date[] beg_date_rider_i;
				Date[] end_pay_rider_i;
				Double[] rate_i;
				Integer[] percent_i;
				Integer[] ins_rider_i;
				int[] letak_2;
				Integer[] kode_rider_i;
				Integer[] number_rider_i;
				Integer[] rider_single_i;
				Double[] premi_rider_i;
				Double[] up_pa_rider_i;
				Double[] up_pb_rider_i;
				Double[] up_pc_rider_i;
				Double[] up_pd_rider_i;
				Double[] up_pm_rider_i;
				Integer[] insured_i;
				Integer[] flag_include_i;				
				Integer[] unit;
				String hasil_unit1;
				String hasil_class1;
				Integer[] klases;
				Integer[] factor_up1;
				Integer[] kd_rider;
				String[] nama_rider;
				String[] nama_rider2;
				String[] rider;
				Double[] sum;
				String[] sum_1;
				Date[] end_date_rider;
				Date[] beg_date_rider;
				Date[] end_pay_rider;
				Double[] rate;
				Integer[] percent;
				Integer[] ins_rider;
				int[] letak_1;
				Integer[] kode_rider;
				Integer[] number_rider;	
				Integer[] rider_single;
				Integer jumlah_rider=new Integer(0);
				Double[] premi_rider;
				Double[] up_pa_rider;
				Double[] up_pb_rider;
				Double[] up_pc_rider;
				Double[] up_pd_rider;
				Double[] up_pm_rider;
				Integer[] insured;
				Integer[] flag_include;
				Double[] mspr_extra;
				Integer[] factor_x;
				Integer[] jenis;
				
				jenis = new Integer[jmlrider.intValue()+1];
				factor_x = new Integer[jmlrider.intValue()+1];
				unit = new Integer[jmlrider.intValue()+1];
				kd_rider = new Integer[jmlrider.intValue()+1];
				klases =  new Integer[jmlrider.intValue()+1];
				factor_up1 = new Integer[jmlrider.intValue()+1];
				nama_rider= new String[jmlrider.intValue()+1];
				nama_rider2= new String[jmlrider.intValue()+1];
				rider = new String[jmlrider.intValue()+1]; 
				sum = new Double[jmlrider.intValue()+1];
				sum_1=  new String[jmlrider.intValue()+1];
				end_date_rider = new Date[jmlrider.intValue()+1];
				beg_date_rider= new Date[jmlrider.intValue()+1];
				end_pay_rider= new Date[jmlrider.intValue()+1];
				rate= new Double[jmlrider.intValue()+1];
				percent=new Integer[jmlrider.intValue()+1];
				ins_rider= new Integer[jmlrider.intValue()+1];
				letak_1= new int[jmlrider.intValue()+1];
				kode_rider= new Integer[jmlrider.intValue()+1];
				number_rider= new Integer[jmlrider.intValue()+1];
				rider_single=new Integer[jmlrider.intValue()+1];
				premi_rider = new Double[jmlrider.intValue()+1];
				up_pa_rider = new Double[jmlrider.intValue()+1];	
				up_pb_rider = new Double[jmlrider.intValue()+1];	
				up_pc_rider = new Double[jmlrider.intValue()+1];	
				up_pd_rider = new Double[jmlrider.intValue()+1];	
				up_pm_rider = new Double[jmlrider.intValue()+1];	
				insured = new Integer[jmlrider.intValue()+1];	
				flag_include = new Integer[jmlrider.intValue()+1];
				mspr_extra = new Double[jmlrider.intValue()+1];
				hasil_rider="";
				Integer jumlah_flag_include= new Integer(0);
				Integer FlagSwineFlu= 0;
				Date SebelasBelasOktoberDuaRibuSembilan = defaultDateFormat.parse("11/10/2009");
				Date SatuJanuariDuaRibuSepuluh = defaultDateFormat.parse("1/1/2010");
				Date LimaBelasJuniDuaRibuSepuluh = defaultDateFormat.parse("15/6/2010");
				
				produk.riderInclude(number_produk.intValue());

				List dtrd1 = new ArrayList();
				
				if(kode_produk.intValue() == 183 || kode_produk.intValue() == 201 || kode_produk.intValue() == 189 || kode_produk.intValue() == 193|| produk.ii_bisnis_id==195 || produk.ii_bisnis_id==204 || products.stableSave(kode_produk.intValue(), number_produk.intValue()) || products.stableLink(kode_produk.toString()) || 
				   kode_produk.intValue() == 145 ||kode_produk.intValue() == 146 || kode_produk.intValue() == 163 || products.SuperSejahtera(kode_produk.toString()) || kode_produk.intValue()==40 || 
				   products.unitLinkNew(kode_produk.toString()) || (kode_produk.intValue()==143 && (number_produk>=4 && number_produk<=7)) ||
				   (kode_produk.intValue()==144 && number_produk==4) || (kode_produk.intValue()==158 && (number_produk==13 || (number_produk>=15 && number_produk<=16))) ||
				   (kode_produk.intValue()==134 && number_produk==1) || (kode_produk.intValue()==166 && number_produk==1)){
						if(kode_produk.intValue() == 183 || kode_produk.intValue() == 201 || kode_produk.intValue() == 189 || kode_produk.intValue() == 193 || products.unitLinkNew(kode_produk.toString()) || (kode_produk.intValue()==134 && number_produk==1) || (kode_produk.intValue()==166 && number_produk==1)){
							if(jumlah_r>0){
								for (int k =0 ; k <jumlah_r.intValue();k++)
								{
									Datarider rd= (Datarider)dtrd.get(k);
									kode_rider[k]=rd.getLsbs_id();
									jenis[k]=rd.getJenis();
									number_rider[k]=rd.getLsdbs_number();
									if( edit.getDatausulan().getMste_flag_el()==1 && (kode_rider[k].intValue()==823 || kode_rider[k].intValue()==825 || kode_rider[k].intValue()==838)){
										diskon_karyawan=0.7;
									}
									if(((kode_rider[k].intValue()==820 || kode_rider[k].intValue()==825) && (number_rider[k].intValue()>=1 && number_rider[k].intValue()<=15)) 
											|| (kode_produk.intValue()==183 && (number_produk.intValue()>=1 && number_produk.intValue()<=30)) 
											|| (kode_produk.intValue()==201 && (number_produk.intValue()>=1 && number_produk.intValue()<=15)) 
											|| (kode_produk.intValue()==189 && (number_produk.intValue()>=1 && number_produk.intValue()<=15)) 
											|| (kode_produk.intValue()==193 && (number_produk.intValue()>=1 && number_produk.intValue()<=15)) ){
										unit[k] = new Integer(0);
										kd_rider[k] = new Integer(0);
										klases[k] =  new Integer(0);
										factor_up1[k] = new Integer(0);
										nama_rider[k]= "";
										nama_rider2[k]= "";
										rider[k] = ""; 
										sum[k] = new Double(0);
										end_date_rider[k] = null;
										beg_date_rider[k]=null;
										end_pay_rider[k]=null;
										rate[k]= new Double(0);
										percent[k]=new Integer(0);
										ins_rider[k]= new Integer(0);
										kode_rider[k]= new Integer(0);
										number_rider[k]= new Integer(0);
										rider_single[k]= new Integer(0);
										premi_rider[k] = new Double(0);
										up_pa_rider[k] = new Double(0);	
										up_pb_rider[k] = new Double(0);	
										up_pc_rider[k] = new Double(0);	
										up_pd_rider[k] = new Double(0);	
										up_pm_rider[k] = new Double(0);	
										insured[k] = new Integer(0);	
										flag_include[k] = new Integer(0);
										mspr_extra[k] = new Double(0);
	
										unit[k]=rd.getMspr_unit();
										if (unit[k] == null)
										{
											unit[k] = new Integer(0);
											rd.setMspr_unit(new Integer(0));
										}
										klases[k]=rd.getMspr_class();
										if (klases[k] == null)
										{
											klases[k] = new Integer(0);
											rd.setMspr_class(new Integer(0));
										}
										kode_rider[k]=rd.getLsbs_id();
										
										number_rider[k]=rd.getLsdbs_number();
										ins_rider[k]=rd.getMspr_ins_period();
										insured[k] = rd.getMspr_tt();
										rate[k] = rd.getMspr_rate();
										sum[k] = rd.getMspr_tsi();
										premi_rider[k] =rd.getMspr_premium();
										
										beg_date_rider[k]=rd.getMspr_beg_date();
										end_pay_rider[k]=rd.getMspr_end_pay();
										end_date_rider[k]=rd.getMspr_end_date();
										rider[k]=rd.getPlan_rider();	
										flag_include[k] = new Integer(0);
										Double up_rider = new Double(0);
										//include rider swine flu/flu babi 822
										Integer[]kode_rider_swine;
										kode_rider_swine= new Integer[jmlrider.intValue()+1];
										kode_rider_swine[k]=822;
										if ((Integer.toString(kode_rider[k].intValue())).trim().length()==1){
											nama_rider[k]="produk_asuransi.n_prod_0"+kode_rider[k];	
										}else{
											nama_rider[k]="produk_asuransi.n_prod_"+kode_rider[k];	
										}
						
										if (rider[k].equalsIgnoreCase("0~X0")){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Silahkan memilih Plan Rider ke "+(k+1)+" terlebih dahulu.");
										}
										
										if ((Integer.toString(kode_rider_swine[k].intValue())).trim().length()==1){
											nama_rider2[k]="produk_asuransi.n_prod_0"+kode_rider_swine[k];	
										}else{
											nama_rider2[k]="produk_asuransi.n_prod_"+kode_rider_swine[k];	
										}
						
										if (rider[k].equalsIgnoreCase("0~X0")){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Silahkan memilih Plan Rider ke "+(k+1)+" terlebih dahulu.");}
										
										if (!nama_rider[k].equalsIgnoreCase("produk_asuransi.n_prod_00")){
											//cek rider 
											try{
												Class aClass1 = Class.forName(nama_rider[k]);
												n_prod produk1 = (n_prod)aClass1.newInstance();
												Class aClass2 = Class.forName(nama_rider2[k]);
												n_prod produk2 = (n_prod)aClass2.newInstance();
												up_rider = produk1.of_get_up(premi.doubleValue(),up.doubleValue(),unit[k].intValue(),produk.flag_jenis_plan,kode_rider[k].intValue(),number_rider[k].intValue(),flag_platinum);
												
												up_rider = up_rider * 10;
												up_rider = produk2.cek_maks_up_rider(up_rider, kurs);
											}catch (ClassNotFoundException e){
												logger.error("ERROR :", e);
											}catch (InstantiationException e){
												logger.error("ERROR :", e);
											}catch (IllegalAccessException e){
												logger.error("ERROR :", e);
											}
										}
										if(products.stableLink(kode_produk.toString())){
											if(edit.getPowersave().getMsl_spaj_lama()!=null){//apabila surrender endors, maka begdatenya ambil bdate dari slink, bukan dari insured.
												if(edit.getPowersave().getBegdate_topup().after(SebelasBelasOktoberDuaRibuSembilan) && edit.getPowersave().getBegdate_topup().before(SatuJanuariDuaRibuSepuluh)){
													Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, up_rider, 0, 0, 822+"~X"+1);
													if(dtrd.size()!=0){
														dtrd.add(rider_includer);
													}
												}
											}else{
												if(edit.getDatausulan().getMste_beg_date().after(SebelasBelasOktoberDuaRibuSembilan) && edit.getDatausulan().getMste_beg_date().before(SatuJanuariDuaRibuSepuluh)){
													Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, up_rider, 0, 0, 822+"~X"+1);
													if(dtrd.size()!=0){
														dtrd.add(rider_includer);
													}
												}
											}
										}else{
											if(edit.getDatausulan().getMste_beg_date().after(SebelasBelasOktoberDuaRibuSembilan) && edit.getDatausulan().getMste_beg_date().before(SatuJanuariDuaRibuSepuluh) ){
												Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, up_rider, 0, 0, 822+"~X"+1);
												if(dtrd.size()!=0){
													dtrd.add(rider_includer);
												}
											}
										}
									}
								}
							}else{
								if(kode_produk.intValue() == 183 || kode_produk.intValue() == 201 || kode_produk.intValue() == 189 || kode_produk.intValue() == 193){
									Double up_rider = new Double(0);
									up_rider = produk.of_get_min_up();
									up_rider = up_rider * 10;
									String nama_swine;
									Integer kode_rider_swine = 822;
									if ((Integer.toString(kode_rider_swine.intValue())).trim().length()==1){
										nama_swine="produk_asuransi.n_prod_0"+kode_rider_swine;	
									}else{
										nama_swine="produk_asuransi.n_prod_"+kode_rider_swine;	
									}
									
									if (!nama_swine.equalsIgnoreCase("produk_asuransi.n_prod_00")){
										//cek rider  LUFI
										try{
											Class aClass1 = Class.forName(nama_swine);
											n_prod produk1 = (n_prod)aClass1.newInstance();
											
											up_rider = produk1.cek_maks_up_rider(up_rider, kurs);
											if(edit.getDatausulan().getMste_beg_date().after(SebelasBelasOktoberDuaRibuSembilan) && edit.getDatausulan().getMste_beg_date().before(SatuJanuariDuaRibuSepuluh)){
												Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, up_rider, 0, 0, 822+"~X"+1);
												dtrd.add(rider_includer);
											}
										}catch (ClassNotFoundException e){
											logger.error("ERROR :", e);
										}catch (InstantiationException e){
											logger.error("ERROR :", e);
										}catch (IllegalAccessException e){
											logger.error("ERROR :", e);
										}
									}
								}
							}
						}else if(products.stableSave(kode_produk.intValue(),number_produk.intValue()) || products.stableLink(kode_produk.toString()) || (kode_produk.intValue()==143 && (number_produk>=4 && number_produk<=7)) ||
								(kode_produk.intValue()==144 && number_produk==4) || (kode_produk.intValue()==158 && (number_produk==13 || (number_produk>=15 && number_produk<=16)))){
							//if(edit.getCurrentUser().getJn_bank()==2){
								if(edit.getPowersave().getMsl_spaj_lama()!=null){//apabila surrender endors, maka begdatenya ambil bdate dari slink, bukan dari insured.
									if(edit.getPowersave().getBegdate_topup().after(SebelasBelasOktoberDuaRibuSembilan) && edit.getPowersave().getBegdate_topup().before(SatuJanuariDuaRibuSepuluh)){
										if(edit.getDatausulan().getLku_id().equals("01")){
											if(products.stableLink(kode_produk.toString()) && kode_produk!=186){	
												if(edit.getDatausulan().getTotal_premi_sementara()>=100000000){
													Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) 20000000.0, 0, 0, 822+"~X"+1);
													dtrd.add(rider_includer);	
												}
											}else{
												if(kode_produk!=186){
													if(edit.getDatausulan().getMspr_premium()>=100000000){
														Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) 20000000.0, 0, 0, 822+"~X"+1);
														dtrd.add(rider_includer);	
													}
												}
											}
										}else if(edit.getDatausulan().getLku_id().equals("02")){
											if(products.stableLink(kode_produk.toString()) && kode_produk!=186){
												if(edit.getDatausulan().getTotal_premi_sementara()>=10000){
													Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) 2000.0, 0, 0, 822+"~X"+1);
													dtrd.add(rider_includer);	
												}
											}else{
												if(kode_produk!=186){
													if(edit.getDatausulan().getMspr_premium()>=10000){
														Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) 2000.0, 0, 0, 822+"~X"+1);
														dtrd.add(rider_includer);	
													}
												}
											}
										}
									}
								}else{
									if(edit.getDatausulan().getMste_beg_date().after(SebelasBelasOktoberDuaRibuSembilan) && edit.getDatausulan().getMste_beg_date().before(SatuJanuariDuaRibuSepuluh)){
										if(edit.getDatausulan().getLku_id().equals("01")){
											if(products.stableLink(kode_produk.toString()) && kode_produk!=186){	
												if(edit.getDatausulan().getTotal_premi_sementara()>=100000000){
													Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) 20000000.0, 0, 0, 822+"~X"+1);
													dtrd.add(rider_includer);	
												}
											}else{
												if(kode_produk!=186){
													if(edit.getDatausulan().getMspr_premium()>=100000000){
														Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) 20000000.0, 0, 0, 822+"~X"+1);
														dtrd.add(rider_includer);	
													}
												}
											}
											
										}else if(edit.getDatausulan().getLku_id().equals("02")){
											if(products.stableLink(kode_produk.toString()) && kode_produk!=186){
												if(edit.getDatausulan().getTotal_premi_sementara()>=10000){
													Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) 2000.0, 0, 0, 822+"~X"+1);
													dtrd.add(rider_includer);	
												}
											}else{
												if(kode_produk!=186){
													if(edit.getDatausulan().getMspr_premium()>=10000){
														Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) 2000.0, 0, 0, 822+"~X"+1);
														dtrd.add(rider_includer);	
													}
												}
											}
										}
									}
								}
						}else if(kode_produk.intValue() == 145 ||kode_produk.intValue() == 146 || kode_produk.intValue() == 163 || products.SuperSejahtera(kode_produk.toString()) || kode_produk.intValue()==40){
							Double up_rider = new Double(0);
							up_rider = edit.getDatausulan().getMspr_tsi() * 0.1;
							if(edit.getDatausulan().getLku_id().equals("01")){
								if(up_rider>=20000000.0){
									up_rider=20000000.0;
								}
							}else if(edit.getDatausulan().getLku_id().equals("02")){
								if(up_rider>=2000.0){
									up_rider=2000.0;
								}
							}
							
								if(edit.getDatausulan().getMste_beg_date().after(SebelasBelasOktoberDuaRibuSembilan) && edit.getDatausulan().getMste_beg_date().before(SatuJanuariDuaRibuSepuluh)){
									if(edit.getDatausulan().getLku_id().equals("01")){
										if(edit.getDatausulan().getMspr_tsi()>=100000000){
											Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) up_rider, 0, 0, 822+"~X"+1);
											dtrd.add(rider_includer);	
										}
									}else if(edit.getDatausulan().getLku_id().equals("02")){
										if(edit.getDatausulan().getMspr_tsi()>=10000){
											Datarider rider_includer = new Datarider(822, 1, 0, 1, 1, (double) 0, (double) 0, (double) up_rider, 0, 0, 822+"~X"+1);
											dtrd.add(rider_includer);	
										}
									}
								}
						}
						jumlah_r=new Integer(dtrd.size());
						jmlrider=new Integer(dtrd.size());
				}
				
				//Start validation swine Flu free double
				if(dtrd.size()>0){
					for (int k =0 ; k <dtrd.size();k++){
						
						Datarider rd= (Datarider)dtrd.get(k);
						kode_rider[k]=rd.getLsbs_id();
						number_rider[k]=rd.getLsdbs_number();
						if(kode_rider[k].intValue()==822 && number_rider[k].intValue()==1) FlagSwineFlu+=1;
						if(FlagSwineFlu>1){
							dtrd.remove(k-1);
							FlagSwineFlu-=1;
						}
					}
					jumlah_r=new Integer(dtrd.size());
					jmlrider=new Integer(dtrd.size());
					edit.getDatausulan().setJmlrider(jmlrider);
					edit.getDatausulan().setJumlah_seluruh_rider(jmlrider);
				}
				//END validation swine Flu
				
//				(deddy)- REQ ASRI : JIKA USIA _TT =50 tahun, dan (mengambil eka sehat stand alone atau (produk link dan ada rider eka sehat) maka diberikan warning dan set harus pemeriksaan kesehatanl )
				if((products.unitLink(Integer.toString(produk.ii_bisnis_id)) || produk.ii_bisnis_id==183 || produk.ii_bisnis_id==189 || produk.ii_bisnis_id==193) && edit.getTertanggung().getMste_age()==50  ){
					if(produk.ii_bisnis_id==183 || produk.ii_bisnis_id==189 || produk.ii_bisnis_id==193){
						edit.getDatausulan().setAlertEkaSehat(0);
						edit.getDatausulan().setMste_medical(0);
					}else{
						for(int i=0;i<jumlah_rider;i++){
							Datarider banding1 = (Datarider)dtrd.get(i);
							if((banding1.getLsbs_id()==820 || banding1.getLsbs_id()==823) && banding1.getLsdbs_number()<16){
								edit.getDatausulan().setAlertEkaSehat(1);
								edit.getDatausulan().setMste_medical(1);
							}
						}
					}
				}
				//(LUFI)Start Validation packet untuk cerdas mall 129 dan 120
				//Validasi Paket Pension
				int jumlah_rider_paket=0;
				if(produk.ii_bisnis_id==129 && edit.getDatausulan().getFlag_paket()!=0){					
					if(dtrd.size()==0)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Untuk  Produk paket harus memilih rider sesuai paket yang  dipilih");
					for (int x =0 ; x < dtrd.size();x++){						
						Datarider rd1= (Datarider)dtrd.get(x);
						kode_rider[x]=rd1.getLsbs_id();
						number_rider[x]=rd1.getLsdbs_number();
						if(edit.getDatausulan().getFlag_paket()==12 || edit.getDatausulan().getFlag_paket()==15){
							jumlah_rider_paket=2;
							if("810,814".indexOf(kode_rider[x].toString()) ==-1)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");
						}else if(edit.getDatausulan().getFlag_paket()==13 || edit.getDatausulan().getFlag_paket()==16){//	
							jumlah_rider_paket=4;
							if("810,814,813,816".indexOf(kode_rider[x].toString()) ==-1)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");
						}else if(edit.getDatausulan().getFlag_paket()==14 || edit.getDatausulan().getFlag_paket()==17){
							jumlah_rider_paket=5;
							if("810,814,813,816,823".indexOf(kode_rider[x].toString())== -1)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");
						}						
					}
					if(dtrd.size()<jumlah_rider_paket)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");
				}	
				//validasi paket ladies
				if(produk.ii_bisnis_id==120 && edit.getDatausulan().getFlag_paket()!=0 && ("1,2,3,4,5".indexOf(edit.getDatausulan().getFlag_paket().toString()) > -1)){
					if(dtrd.size()==0)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Untuk  Produk paket harus memilih rider sesuai paket yang  dipilih");
					for (int x = 0 ; x < dtrd.size();x++){						
						Datarider rd1= (Datarider)dtrd.get(x);
						kode_rider[x]=rd1.getLsbs_id();
						number_rider[x]=rd1.getLsdbs_number();
						if(edit.getDatausulan().getFlag_paket()==1){
							jumlah_rider_paket=2;
							if("830,832".indexOf(kode_rider[x].toString()) ==-1)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");							
						}else if(edit.getDatausulan().getFlag_paket()==2){
							jumlah_rider_paket=1;
							if("832".indexOf(kode_rider[x].toString()) ==-1)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");							
						}else if(edit.getDatausulan().getFlag_paket()==3){
							jumlah_rider_paket=1;
							if("831".indexOf(kode_rider[x].toString()) ==-1)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");							
						}else if(edit.getDatausulan().getFlag_paket()==4){
							jumlah_rider_paket=4;
							if("810,813,830,823".indexOf(kode_rider[x].toString()) ==-1)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider ke- "+x+" tidak sesuai dengan paket yang dipilih");							
						}else if(edit.getDatausulan().getFlag_paket()==5){
							jumlah_rider_paket=2;
							if("813,830".indexOf(kode_rider[x].toString()) ==-1)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");							
						}
					}
					if(dtrd.size()<jumlah_rider_paket)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");
				}//end validation for packet pension and ladies
				
				//Lufi--  Validasi Paket SMiLe  Education
				if( ((products.unitLinkNew(Integer.toString(produk.ii_bisnis_id)))) && edit.getDatausulan().getFlag_paket()!=0 && produk.ii_bisnis_id!=120){
					if("27,28,29,30".indexOf(edit.getDatausulan().getFlag_paket().toString())<0){
						if(dtrd.size()==0)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Untuk  Produk paket harus memilih rider sesuai paket yang  dipilih");
					}
					for (int x = 0 ; x < dtrd.size();x++){						
						Datarider rd1= (Datarider)dtrd.get(x);
						kode_rider[x]=rd1.getLsbs_id();
						number_rider[x]=rd1.getLsdbs_number();
						if(edit.getDatausulan().getFlag_paket()==21){
							jumlah_rider_paket=2;
							if("810,828".indexOf(kode_rider[x].toString()) ==-1)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");							
						}else if(edit.getDatausulan().getFlag_paket()==22){
							jumlah_rider_paket=3;
							if("810,828,835".indexOf(kode_rider[x].toString()) ==-1)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");							
						}else if(edit.getDatausulan().getFlag_paket()==23){
							jumlah_rider_paket=4;
							if("810,811,828,835,823".indexOf(kode_rider[x].toString()) ==-1)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");							
						}
						if(dtrd.size()<jumlah_rider_paket)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");
					}
				}
			// Andhika (15/11/2013) req : Novie - Validasi paket education simpol	
			if(produk.ii_bisnis_id==120 && edit.getDatausulan().getFlag_paket()!=0 && ("21,22,23".indexOf(edit.getDatausulan().getFlag_paket().toString()) > -1)){
					if(dtrd.size()==0)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Untuk  Produk paket harus memilih rider sesuai paket yang  dipilih");
					for (int x = 0 ; x < dtrd.size();x++){						
						Datarider rd1= (Datarider)dtrd.get(x);
						kode_rider[x]=rd1.getLsbs_id();
						number_rider[x]=rd1.getLsdbs_number();
						if(edit.getDatausulan().getFlag_paket()==21){
							jumlah_rider_paket=2;
							if(produk.ii_bisnis_no==22){
								if((kode_rider[x].intValue() != 819 && number_rider[x].intValue() != 143) && kode_rider[x].intValue() != 815){
									err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");}
							}else if(produk.ii_bisnis_no==23){
								if((kode_rider[x].intValue() != 819 && number_rider[x].intValue() != 145) && kode_rider[x].intValue() != 815){
									err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");}
							}
						}else if(edit.getDatausulan().getFlag_paket()==22){
							jumlah_rider_paket=2;
							if(produk.ii_bisnis_no==22){
								if((kode_rider[x].intValue() != 819 && number_rider[x].intValue() != 143) && kode_rider[x].intValue() != 815){
									err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");}
							}else if(produk.ii_bisnis_no==23){
								if((kode_rider[x].intValue() != 819 && number_rider[x].intValue() != 145) && kode_rider[x].intValue() != 815){
									err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");}
							}							
						}else if(edit.getDatausulan().getFlag_paket()==23){
							jumlah_rider_paket=2;
							if(produk.ii_bisnis_no==22){
								if((kode_rider[x].intValue() != 819 && number_rider[x].intValue() != 143) && kode_rider[x].intValue() != 815){
									err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");}
							}else if(produk.ii_bisnis_no==23){
								if((kode_rider[x].intValue() != 819 && number_rider[x].intValue() != 145) && kode_rider[x].intValue() != 815){
									err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");}
							}}}
					if(dtrd.size()<jumlah_rider_paket)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Jumlah rider yang dipilih tidak sesuai dengan paket yang dipilih");
				}
			//Lufi--  Validasi Paket Simpol Protector
			if( ("24,25,26".indexOf(edit.getDatausulan().getFlag_paket().toString()) > -1) && produk.ii_bisnis_id==120){
				if(dtrd.size()==0)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Untuk  Produk paket harus memilih rider sesuai paket yang  dipilih");
				for (int x = 0 ; x < dtrd.size();x++){						
					Datarider rd1= (Datarider)dtrd.get(x);
					kode_rider[x]=rd1.getLsbs_id();
					number_rider[x]=rd1.getLsdbs_number();					
						jumlah_rider_paket=4;
						if("810,812,813,818".indexOf(kode_rider[x].toString()) ==-1)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");							
					
					if(dtrd.size()<jumlah_rider_paket)err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih");
				}
			}
				//ambil data dari list
				for (int k =0 ; k <jumlah_r.intValue();k++){
					unit[k] = new Integer(0);
					kd_rider[k] = new Integer(0);
					klases[k] =  new Integer(0);
					nama_rider[k]= "";
					rider[k] = ""; 
					factor_x[k] = new Integer(0);
					sum[k] = new Double(0);
					end_date_rider[k] = null;
					beg_date_rider[k]=null;
					end_pay_rider[k]=null;
					rate[k]= new Double(0);
					percent[k]=new Integer(0);
					ins_rider[k]= new Integer(0);
					kode_rider[k]= new Integer(0);
					number_rider[k]= new Integer(0);
					rider_single[k]= new Integer(0);
					premi_rider[k] = new Double(0);
					up_pa_rider[k] = new Double(0);	
					up_pb_rider[k] = new Double(0);	
					up_pc_rider[k] = new Double(0);	
					up_pd_rider[k] = new Double(0);	
					up_pm_rider[k] = new Double(0);	
					insured[k] = new Integer(0);	
					flag_include[k] = new Integer(0);
					mspr_extra[k] = new Double(0);
					jenis[k] = new Integer(0);

					Datarider rd= (Datarider)dtrd.get(k);
					kode_rider[k]=rd.getLsbs_id();
					//untuk paket Pension Cerdas Mall untuk PA diset unit 2 dan CI 50 persen up 
					if(produk.ii_bisnis_id==129 && edit.getDatausulan().getFlag_paket()!=0){
						if(kode_rider[k].intValue()==810)rd.setMspr_unit(2);
						if(kode_rider[k].intValue()==813)rd.setPersenUp(1);
					}
					if(kode_rider[k].intValue()==835)rd.setFlag_jenis_up(1);
					number_rider[k]=rd.getLsdbs_number();
                    
					unit[k]=rd.getMspr_unit();
					if (unit[k] == null)
					{
						unit[k] = new Integer(0);
						rd.setMspr_unit(new Integer(0));
					}
					klases[k]=rd.getMspr_class();
					if (klases[k] == null)
					{
						klases[k] = new Integer(0);
						rd.setMspr_class(new Integer(0));
					}
					ins_rider[k]=rd.getMspr_ins_period();
					insured[k] = rd.getMspr_tt();
					rate[k] = rd.getMspr_rate();
					sum[k] = rd.getMspr_tsi();
					premi_rider[k] =rd.getMspr_premium();
					
					beg_date_rider[k]=rd.getMspr_beg_date();
					end_pay_rider[k]=rd.getMspr_end_pay();
					end_date_rider[k]=rd.getMspr_end_date();
					rider[k]=rd.getPlan_rider();	
					flag_include[k] = new Integer(0);
					factor_x[k] = rd.getPersenUp();
					jenis[k] = rd.getJenis();			
					
					if (rd.getJenis()==null){// antisipasi getJenis()==null, ga tau kenapa, terkadang suka null
						jenis[k]=dtrd.size();
					}
					// untuk RIDER
					if (kode_rider[k].intValue() <900 )
					{
						if ((Integer.toString(kode_rider[k].intValue())).trim().length()==1)
						{
							nama_rider[k]="produk_asuransi.n_prod_0"+kode_rider[k];	
						}else{
							nama_rider[k]="produk_asuransi.n_prod_"+kode_rider[k];	
						}
		
						if (rider[k].equalsIgnoreCase("0~X0"))
						{
							err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Silahkan memilih Plan Rider ke "+(k+1)+" terlebih dahulu.");
						}
						// *Cek persentase UP Rider CI dan Ladies insured (07062012)
						if ((kode_rider[k].intValue()==813 && factor_x[k].intValue() == 0)||(kode_rider[k].intValue()==830 && factor_x[k].intValue() == 0))
						{
							if(sum[k] == 0 || sum[k] == null)
							{
								err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Silahkan Pilih dahulu Persentase UP Rider terlebih dahulu.");
							}						
							
						}
						if("24,25,26".indexOf(edit.getDatausulan().getFlag_paket().toString())>-1 && kode_rider[k].intValue()==813){
							if(factor_x[k] !=1){
								err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN :Presentase Up Rider CI untuk SImpol Protector adalah 50%.");
							}
						}
						
						if (!nama_rider[k].equalsIgnoreCase("produk_asuransi.n_prod_00"))
						{
							//cek rider 
							try{
								Class aClass1 = Class.forName(nama_rider[k]);
								n_prod produk1 = (n_prod)aClass1.newInstance();
								produk1.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
	
								Boolean type_rider=new Boolean(false);
								produk1.li_lama=(ins_rider[k].intValue());
								//logger.info(produk1.li_lama);
								//int lama_bayar =(ins_rider[k].intValue());
								kd_rider[k]=new Integer(produk1.flag_rider_baru);
								li_pct_biaya = new Double(produk1.li_pct_biaya); 
								if (produk.flag_rider==0)
								{
									klases[k] = new Integer(produk1.set_klas(klases[k].intValue()));
									unit[k] = new Integer(produk1.set_unit(unit[k].intValue()));
								}
								
								if ((kode_rider[k].intValue()==819) && 
										((number_rider[k].intValue()>=1 && number_rider[k].intValue()<=20) || (number_rider[k].intValue()>=141 && number_rider[k].intValue()<=160) 
												|| (number_rider[k].intValue()>=281 && number_rider[k].intValue()<=300) || (number_rider[k].intValue()>=381 && number_rider[k].intValue()<=390)))
								{
									edit.getDatausulan().setFlag_hcp(new Integer(1));
									flag_hcp = new Integer(1);
									number_utama = number_rider[k];
								}
								
								//helpdesk [133899], produk B Smile Insurance Syariah 215-4 dan B Smile Protection Syariah 224-3
								if(products.provider(Integer.toString(produk.ii_bisnis_id))){
									if( kode_rider[k].intValue()==823 || 
											kode_rider[k].intValue()==825 || 
											kode_rider[k].intValue()==826 ||
											kode_rider[k].intValue()==832 ||
											kode_rider[k].intValue()==833 ||
											kode_rider[k].intValue()==838 ||
											kode_rider[k].intValue()==839 ||
											kode_rider[k].intValue()==844 ||
											kode_rider[k].intValue()==848){
										edit.getDatausulan().setMspo_provider(2);
									}
								}
								
//								if(edit.getDatausulan().getMste_beg_date().before(LimaBelasJuniDuaRibuSepuluh)&& kode_rider[k].intValue()==823){
//									err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk tanggal Mulai < 15 Juni 2010, silakan memilih rider Eka Sehat Biasa dan memberikan tanda secara manual apakah Eka Sehat Provider Atau Non Provider" );
//								}
//								
//								if(edit.getDatausulan().getMste_beg_date().after(LimaBelasJuniDuaRibuSepuluh)&& kode_rider[k].intValue()==820){
//									err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk tanggal Mulai > 15 Juni 2010, silakan memilih rider Eka Sehat yang terbaru(Eka Sehat Provider)" );
//								}
								
								//lufi validasi up scholarship sekalian penegsetan jenis up
								if(kode_rider[k].intValue()==835){						
									
									if(rd.getMspr_tsi().intValue()!=5000000 && rd.getMspr_tsi().intValue()!=10000000 && rd.getMspr_tsi().intValue()!=20000000 && rd.getMspr_tsi().intValue()!=30000000 && rd.getMspr_tsi().intValue()!=40000000 && rd.getMspr_tsi().intValue()!=50000000){
										// ||rd.getMspr_tsi().intValue()!=10000000 ||rd.getMspr_tsi().intValue()!=20000000 || rd.getMspr_tsi().intValue()!=30000000 || rd.getMspr_tsi().intValue()!=40000000 || rd.getMspr_tsi().intValue()!=50000000){
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : UP untuk rider SMiLe Scholarship adalah 5jt,10jt, 20jt, 30jt,40jt atau 50jt.");
									}
									
								}		
								// validasi smile baby usia 20-30 minggu - ryan (hanya validasi)
//								if(kode_rider[k].intValue()==836){			
//										Integer jumlah_h=new Integer(0);
//										//tgl lahir ttg
//										Integer tanggal_h=edit.getTertanggung().getMspe_date_birth().getDate();
//										Integer bulan_h=(edit.getTertanggung().getMspe_date_birth().getMonth())+1;
//										Integer tahun_h=(edit.getTertanggung().getMspe_date_birth().getYear())+1900;
//						
//										//tgl sekarang
//										Date sysdate=elionsManager.selectSysdate();
//										Integer tanggal_s=sysdate.getDate();
//										Integer bulan_s=(sysdate.getMonth())+1;
//										Integer tahun_s=(sysdate.getYear())+1900;
//										jumlah_h =  umr.hari1(tahun_h,bulan_h,tanggal_h,tahun_s,bulan_s,tanggal_s);
//										if(jumlah_h <= 140 || jumlah_h >= 224 ){
//										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : usia tertanggung untuk rider smile baby ketentuan SK-nya dari usia kandungan 20-32 minggu");}
//									
//								}	
								
								jumlah_rider=jmlrider;
		
								//cek rider boleh dipilih atau ga
								for ( int i=0 ; i< produk.indeks_rider_list; i++ )
								{
									if (kode_rider[k].intValue()==produk.ii_rider[i])
									{
										type_rider=new Boolean(true);
									}
								}
								
								// khusus 183,189,193, rider nya di hardcode , karena ada req , eka sehat stand alone bisa milih BIASA or PROVIDER - ryan
								if("183,189,193".indexOf(edit.getDatausulan().getLsbs_id().toString())>-1)
								{
									type_rider=new Boolean(true);
								}
								
								//produk number 6 atau 3 
								//Yusuf - 27/07/09 - request dari BAS, ini boleh koq
								/*
								if (produk.flag_jenis_plan==1 && type_rider.booleanValue()==true)
								{
									if (number_rider[k].intValue()!= number_produk.intValue())
									{
										type_rider=new Boolean(false);
										if ((number_produk.intValue() == 2 || number_produk.intValue() == 5) && number_rider[k].intValue() == 2)
										{
											type_rider=new Boolean(true);
										}
										if ((number_produk.intValue() == 3 || number_produk.intValue() == 6) && number_rider[k].intValue() == 3)
										{
											type_rider=new Boolean(true);
										}
										
									}
								}*/
								
								//(Deddy) validasi kalau untuk kode_rider 813 dan 818, untuk powersave, stabil link, dan stable save  (813 CI) number_rider 4&5 dan (818 TERM) number_rider 3&4
								// untuk multiinvest (813 CI) hanya number_rider 6 dan (818 TERM) hanya number_rider 5
								String kod = Integer.toString(produk.ii_bisnis_id);
								String numb = Integer.toString(produk.ii_bisnis_no);
								if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.multiInvest(Integer.toString(produk.ii_bisnis_id)) 
										|| products.stableLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) 
										|| products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no) ){
									if((products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) 
											|| products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no))  ){
										if(kode_rider[k].intValue()==813){
											if(number_rider[k].intValue() != 4 && number_rider[k].intValue() != 5){
												if(products.bsm(kod.toString(), numb.toString())){ // BSM
												}else{
														err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Plan Produk ini, hanya bisa memilih yang CI - 4" );
												}
											}
										}else if(kode_rider[k].intValue()==818){
											if(number_rider[k].intValue() != 3 && number_rider[k].intValue() != 4){
												if((produk.ii_bisnis_id==186 && produk.ii_bisnis_no==3)){
													
												}else if(products.bsm(kod.toString(), numb.toString())){
													
												}else{
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Plan Produk ini, hanya bisa memilih yang TERM - 4" );
												}
											}
										}
									}else if(products.multiInvest(Integer.toString(produk.ii_bisnis_id))){
										if(kode_rider[k].intValue()==813){
											if(number_rider[k].intValue() != 6){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Plan Produk ini, hanya bisa memilih yang CI - 8" );
											}
										}else if(kode_rider[k].intValue()==818){
											if(number_rider[k].intValue() != 5){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Plan Produk ini, hanya bisa memilih yang TERM - 8" );
											}
										}
									}
								}
								
								//TODO:DEDDY -> Validasi ini berlaku per tanggal 1 april 2010
								//Validasi untuk produk Platinum Link, harus PA resiko ABD; HCP Family semua plan
//								if(produk.ii_bisnis_id==134){
//									if(kode_rider[k].intValue()==810 && number_rider[k].intValue()!=3){
//										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Platinum Link, jenis Rider PA hanya bisa mengambil yang risiko ABD" );
//									}
//								}
								
								if(products.SuperSejahtera(Integer.toString(produk.ii_bisnis_id)) && produk.ii_bisnis_no==5){
									if(kode_rider[k].intValue()== 802){
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Super Sejahtera Sekaligus, tidak dapat mengambil Plan Rider Payor" );
									}
									if(kode_rider[k].intValue()== 804){
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Super Sejahtera Sekaligus, tidak dapat mengambil Plan Rider Waiver" );
									}
								}
								
						/*		//Ryan, Term harus mengambil HCP 
								if(produk.ii_bisnis_id==196){
									if(kode_rider[k].intValue()== 204 && (number_rider[k].intValue()==1 && number_rider[k].intValue()==2)){
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Produk Term ini , Rider HCP Yang dapat diambil hanya plan R-100 dan R-200" );
									}
								}
								*/
								//(Deddy) validasi untuk Eka Sehat Utama(183) : apabila produk utamanya Eka Sehat, maka rider jenis Spouse dan Child baru dapat diambil setelah proses input bac selesai.
								//validasi untuk Eka Sehat Rider(820) : apabila produk utama bukan Eka Sehat, maka rider yg dapat diambil di input BAC hanya yang basic.
								//Untuk spouse dan Child dapat ditambah di tombol Eka Sehat/HCP Family.
								if(!status.toString().equals("edit")){
									if(produk.ii_bisnis_id==183 || produk.ii_bisnis_id==189 || produk.ii_bisnis_id==193){
										if(kode_rider[k].intValue()==820 || kode_rider[k].intValue()==825){
											if(number_rider[k].intValue() > 15){
//												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk jenis Spouse dan Child, tidak dapat diinput disini.Lakukan penginputan Spouse dan Child di tombol SM/Eka Sehat/HCP Family di atas." );
											}else if(number_rider[k].intValue() <= 15){
//												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Produk Utama merupakan jenis BASIC, rider Eka Sehat BASIC tidak dapat diambil." );
											}
										}
									}else if(produk.ii_bisnis_id!=183 && produk.ii_bisnis_id!=189 && produk.ii_bisnis_id!=193 ){
										if(kode_rider[k].intValue()==820 || kode_rider[k].intValue()==825){
											if(number_rider[k].intValue() > 15 && number_rider[k].intValue() < 91){
//												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Eka Sehat yang dapat diambil saat ini hanya jenis BASIC. Lakukan penginputan Spouse dan Child di table Peserta Asuransi Tambahan." );
											}
										}
									}
									
									//(Deddy) validasi untuk Smart Medicare(178)
									if(produk.ii_bisnis_id==178){
										if(kode_rider[k].intValue()==821){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Penambahan Tertanggung Tambahan dapat dilakukan setelah proses submit dengan menekan tombol SM/Eka Sehat/HCP Family di atas." );
										}
									}
								}
								
								
								
								//(Deddy)validasi untuk produk utama EkaWaktu, apabila pembayaran SEKALIGUS maka tidak dapat mengambil rider Waiver
								if(produk.ii_bisnis_id==169){
									if(produk.ib_single_premium){
										if(kode_rider[k].intValue()==814){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Cara Bayar Sekaligus, tidak bisa memilih Rider Waiver" );
										}
									}
									
								}
								
								if(products.unitLink(Integer.toString(produk.ii_bisnis_id))){
									if(kode_rider[k].intValue()==820){
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Plan Unit Link, rider Eka Sehat yang diambil harus Provider" );
									}
									if(kode_rider[k].intValue()==830){
										if (number_rider[k].intValue() ==1 || number_rider[k].intValue() == 3){
											if(edit.getTertanggung().getMspe_sex()==1){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk plan "+rd.getNamaRiderNya()+" rider Ladies Insurance ini, hanya bisa untuk berjenis kelamin 'WANITA'");
										}
									}
								}}
								
								//(Deddy)validasi HCP untuk produk dasar powersave, stabil link,  dan stable save
								if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) 
										|| products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no) ){
									if(kode_rider[k].intValue()==819){
										if(number_rider[k].intValue() <=280 && number_rider[k].intValue() >= 381){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Plan Produk ini, hanya bisa memilih HCPF 4" );
										}
									}
								}else if(products.multiInvest(Integer.toString(produk.ii_bisnis_id))){
									if(kode_rider[k].intValue()==819){
										if(number_rider[k].intValue() <=380 || number_rider[k].intValue() >= 431){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Plan Produk ini, hanya bisa memilih HCPF 8" );
										}
									}
								}
								
								//(Deddy)validasi untuk rider di produk cerdas care
								if(produk.ii_bisnis_id == 120 && edit.getDatausulan().getFlag_paket() == 0){
									
									//khusus produk mall paket ladies exclusive
									if((produk.ii_bisnis_no == 1 || produk.ii_bisnis_no == 2 || produk.ii_bisnis_no == 7 
										|| produk.ii_bisnis_no == 8|| produk.ii_bisnis_no ==16 || produk.ii_bisnis_no ==17
										|| produk.ii_bisnis_no ==18) && edit.getDatausulan().getFlag_paket()!=0){
											
											if(produk1.ii_bisnis_id == 813 &&( number_rider[k].intValue() != 8)){
												
													err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider yang dapat dipilih hanya Critical Illness(new)");
											}
											if(produk1.ii_bisnis_id ==810 && number_rider[k].intValue()!=1){
											     	err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih PA jenis A");
											}										
									}else{
											if(produk.ii_bisnis_no == 1 || produk.ii_bisnis_no == 4 || produk.ii_bisnis_no == 10 || produk.ii_bisnis_no == 13){
												if(produk1.ii_bisnis_id == 814){
													if("10,11,12,19,20,21".indexOf(Integer.toString(produk.ii_bisnis_no))<=-1){
														err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Waiver TPD/CI");
													}else{
														if(number_rider[k].intValue() != 14 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider WAIVER yang dapat dipilih hanya 5 TPD(NEW)");
														 }
													}
												}else if(produk1.ii_bisnis_id == 815){
													if("10,11,12,19,20,21".indexOf(Integer.toString(produk.ii_bisnis_no))<=-1){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Payor Yang  dapat dipilih hanya Waiver TPD/CI/Death");
													}else{
														if(number_rider[k].intValue() != 14 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider PAYOR yang dapat dipilih hanya 5 TPD (New)");
													    }
												    }
												}else if(produk1.ii_bisnis_id == 816){
													if("10,11,12,19,20,21".indexOf(Integer.toString(produk.ii_bisnis_no))<=-1){
														err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Waiver TPD/CI");
													}else{
														if(number_rider[k].intValue() != 2){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider WAIVER yang dapat dipilih hanya 5 CI");
														 }
													}
												}else if(produk1.ii_bisnis_id == 817){
													if("10,11,12,19,20,21".indexOf(Integer.toString(produk.ii_bisnis_no))<=-1){
														err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Payor Yang  dapat dipilih hanya Waiver TPD/CI/Death");
													}else{
														if(number_rider[k].intValue() != 2){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider PAYOR yang dapat dipilih hanya 5 CI");
														}
												    }	
												}else if(produk1.ii_bisnis_id == 827){													
													if(number_rider[k].intValue() != 4 ){
														   err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver yang dapat dipilih hanya  5 TPD/CI");
													 }
												}else if(produk1.ii_bisnis_id == 828){													
													if(number_rider[k].intValue() != 2 ){
													      err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider PAYOR yang dapat dipilih hanya 5 TPD/CI/Death");
													}
											    }											   
											}else if(produk.ii_bisnis_no != 15){
												if(produk1.ii_bisnis_id == 814){
													if("19,20,21".indexOf(Integer.toString(produk.ii_bisnis_no))>-1){//SmiLe Link Satu												
														if(produk.ii_bisnis_no==19 && number_rider[k].intValue() != 4 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Waiver 5 TPD");
														}
														if(produk.ii_bisnis_no==20 && number_rider[k].intValue() != 5 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Waiver 10 TPD");
														}
														if(produk.ii_bisnis_no==21){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Produk ini tidak boleh Mengambil Rider Waiver");
														}	
													}
													if("22,23,24".indexOf(Integer.toString(produk.ii_bisnis_no))>-1){//SIMPOL NEW	Lufi													
														if(produk.ii_bisnis_no==22 && number_rider[k].intValue() != 4 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Waiver 5 TPD");
														}
														if(produk.ii_bisnis_no==23 && number_rider[k].intValue() != 5 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Waiver 10 TPD");
														}
														if(produk.ii_bisnis_no==24){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Produk ini tidak boleh Mengambil Rider Waiver");
														}	
													}
												    	
												}else if(produk1.ii_bisnis_id == 815){
													if("19,20,21".indexOf(Integer.toString(produk.ii_bisnis_no))>-1){//SmiLe Link Satu												
														if(produk.ii_bisnis_no==19 && number_rider[k].intValue() != 4 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Waiver 5 TPD");
														}
														if(produk.ii_bisnis_no==20 && number_rider[k].intValue() != 5 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Waiver 10 TPD");
														}
														if(produk.ii_bisnis_no==21){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Produk ini tidak boleh Mengambil Rider Waiver");
														}														
													}else if("22,23,24".indexOf(Integer.toString(produk.ii_bisnis_no))>-1){//SIMPOL NEW	Lufi													
														if(produk.ii_bisnis_no==22 && number_rider[k].intValue() != 4 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya PAYOR 5 TPD");
														}
														if(produk.ii_bisnis_no==23 && number_rider[k].intValue() != 5 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya PAYOR 10 TPD");
														}
														if(produk.ii_bisnis_no==24){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Produk ini tidak boleh Mengambil Rider PAYOR");
														}	
													}else{
														if( number_rider[k].intValue() != 15){														
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider PAYOR yang dapat dipilih hanya 10 TPD (New)");
														}
													}
												}else if(produk1.ii_bisnis_id == 816){
													if("19,20,21".indexOf(Integer.toString(produk.ii_bisnis_no))>-1){//SmiLe Link Satu												
														if(produk.ii_bisnis_no==19 &&  "2,4".indexOf(Integer.toString(number_rider[k]))<1  ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Waiver 5 TPD");
														}
														if(produk.ii_bisnis_no==20 &&  "3,5".indexOf(Integer.toString(number_rider[k]))<11  ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Waiver 10 TPD");
														}
														if(produk.ii_bisnis_no==21){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Produk ini tidak boleh Mengambil Rider Waiver");
														}													
													}else if("22,23,24".indexOf(Integer.toString(produk.ii_bisnis_no))>-1){//SIMPOL NEW	Lufi													
														if(produk.ii_bisnis_no==22 && number_rider[k].intValue() != 2 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Waiver 5 CI");
														}
														if(produk.ii_bisnis_no==23 && number_rider[k].intValue() != 3 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Waiver 10 CI");
														}
														if(produk.ii_bisnis_no==24){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Produk ini tidak boleh Mengambil Rider Waiver5 atau 10");
														}	
													}else{
														if(number_rider[k].intValue() != 3){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider WAIVER yang dapat dipilih hanya 10 CI");
														}
													}	
												}else if(produk1.ii_bisnis_id == 817){
													if("19,20,21".indexOf(Integer.toString(produk.ii_bisnis_no))>-1){//SmiLe Link Satu												
														if(produk.ii_bisnis_no==19 &&  "2,5".indexOf(Integer.toString(number_rider[k]))<1 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Payor 5 TPD");
														}
														if(produk.ii_bisnis_no==20 &&  "3,6".indexOf(Integer.toString(number_rider[k]))<1 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Payor 10 TPD");
														}
														if(produk.ii_bisnis_no==21){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Produk ini tidak boleh Mengambil Rider Payor 5 Atau 10");
														}													
													}else if("22,23,24".indexOf(Integer.toString(produk.ii_bisnis_no))>-1){//SIMPOL NEW	Lufi													
														if(produk.ii_bisnis_no==22 && number_rider[k].intValue() != 2 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Payor 5 CI");
														}
														if(produk.ii_bisnis_no==23 && number_rider[k].intValue() != 3 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Payor 10 CI");
														}
														if(produk.ii_bisnis_no==24){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Produk ini tidak boleh Mengambil RiderPayor CI");
														}	
													}else{
														if(number_rider[k].intValue() != 3){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider WAIVER yang dapat dipilih hanya 10 CI");
														}
													}	
												}else if(produk1.ii_bisnis_id == 827){
													if("22,23,24".indexOf(Integer.toString(produk.ii_bisnis_no))>-1){//SIMPOL NEW	Lufi
														err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Produk utama ini tidak bisa mengambil rider ini");
													}else if("19,20,21".indexOf(Integer.toString(produk.ii_bisnis_no))>-1){//SmiLe Link Satu												
														if(produk.ii_bisnis_no==19 && number_rider[k].intValue() != 4 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Waiver 5 TPD");
														}
														if(produk.ii_bisnis_no==20 && number_rider[k].intValue() != 5 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Waiver 10 TPD");
														}
														if(produk.ii_bisnis_no==21 && "1,2,3".indexOf(Integer.toString(number_rider[k]))<1){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Produk ini tidak boleh Mengambil Rider Waiver 5 atau 10");
														}				
													}
											    		
												}else if(produk1.ii_bisnis_id == 828){
													if("22,23,24".indexOf(Integer.toString(produk.ii_bisnis_no))>-1){//SIMPOL NEW	Lufi
														err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Produk utama ini tidak bisa mengambil rider ini");
													}else if("19,20,21".indexOf(Integer.toString(produk.ii_bisnis_no))>-1){//SmiLe Link Satu												
														if(produk.ii_bisnis_no==19 && number_rider[k].intValue() != 2 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Payor 5 TPD/CI");
														}
														if(produk.ii_bisnis_no==20 && number_rider[k].intValue() != 3 ){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver Yang  dapat dipilih hanya Payor 10 TPD/CI");
														}
														if(produk.ii_bisnis_no==21 && "1".indexOf(Integer.toString(number_rider[k]))<1){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Produk ini tidak boleh Mengambil Rider Payor 5 atau 10");
														}				
													}
											    }else if(produk1.ii_bisnis_id == 812){
													if("22,23,24,19,20,21".indexOf(Integer.toString(produk.ii_bisnis_no))>-1){//SIMPOL NEW	Lufi
														if(number_rider[k].intValue() != 7){ // *TPD (new)
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider TPD yg bisa dipilih hanya selain yg TPD(New)");
														}
													}
											    }else if(produk1.ii_bisnis_id == 818){
													if("22,23,24,19,20,21".indexOf(Integer.toString(produk.ii_bisnis_no))>-1){//SIMPOL NEW	Lufi
														if(number_rider[k].intValue() != 2){
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini tidak bisa memilih jenis TERM 4 atau TERM 8");
														}
													}
											    }
												
											}else if(produk.ii_bisnis_no == 15) {
												if(produk1.ii_bisnis_id == 814 || produk1.ii_bisnis_id == 815 || produk1.ii_bisnis_id == 816 || produk1.ii_bisnis_id == 817){													
													err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider Waiver dan payor Tidak Dapat dipilih");
												}
											
											
											if(produk1.ii_bisnis_id == 812){
												if(number_rider[k].intValue() != 7){ // *TPD (new)
													err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Rider TPD yg bisa dipilih hanya selain yg TPD(Cerdas Incl)");
												}
											}else if(produk1.ii_bisnis_id == 813){
												if(number_rider[k].intValue() > 3 && number_rider[k].intValue() != 8){ // *CI new
													err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini tidak bisa memilih jenis CI 4 atau CI 8");
												}
											}else if(produk1.ii_bisnis_id == 818){
												if(number_rider[k].intValue() != 2){
													err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini tidak bisa memilih jenis TERM 4 atau TERM 8");
												}
											}else if(produk1.ii_bisnis_id == 819){
												if(number_rider[k].intValue() >= 281 && number_rider[k].intValue() <= 430){
													err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini tidak bisa memilih jenis HCPF 4 atau HCPF 8");
												}
											}
										}	
									}
								}
								//Cerdas New
								if(produk.ii_bisnis_id==121){
									if(produk.ii_bisnis_no==1 || produk.ii_bisnis_no==2){
										//PA ABD
										if(produk1.ii_bisnis_id ==810 && number_rider[k].intValue()!=3){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih PA jenis ABD");
										}
										if(produk1.ii_bisnis_id==812 && number_rider[k].intValue()==2){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih TPD Biasa,bukan Include");
										}
										//Only for HCP Family
										if(produk1.ii_bisnis_id==819 && number_rider[k].intValue()>=281){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih HCP Family");
										}else if(produk1.ii_bisnis_id==819 && number_rider[k].intValue()<281){
											if(!(number_rider[k].intValue()>=141 && number_rider[k].intValue()<=160)){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Plan HCP Family, silakan memilih yg type BASIC");
											}
										}
										// *CI
										if((produk1.ii_bisnis_id==813 && number_rider[k].intValue()!=1) || (produk1.ii_bisnis_id==813 && number_rider[k].intValue()!=8)){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih jenis Critical Illness biasa atau Critical Illness (New)");
										}
										//Waiver 60TPD
										if(produk1.ii_bisnis_id==814 && number_rider[k].intValue()!=1){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih Waiver 60 TPD");
										}
										//Payor 25 TPD/Death
										if(produk1.ii_bisnis_id==815 && number_rider[k].intValue()!=1){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih Payor 25 TPD/Death");
										}
										//Waiver 60 CI
										if(produk1.ii_bisnis_id==816 && number_rider[k].intValue()!=1){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih Waiver 60 CI");
										}
										//Payor 25 CI
										if(produk1.ii_bisnis_id==817 && number_rider[k].intValue()!=4){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih Payor 25 CI");
										}
									}
								}
								
								//(lufi)Khusus Cerdas Paket Pension--//
								if(produk.ii_bisnis_id==129 && edit.getDatausulan().getFlag_paket()!=0){
									if(produk.ii_bisnis_no==5 || produk.ii_bisnis_no==6 || produk.ii_bisnis_no==11 || produk.ii_bisnis_no==12){										 
										 // PA Resiko A
										 if(produk1.ii_bisnis_id ==810 && number_rider[k].intValue()!=1){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih PA jenis A");
											}										 
										 //waiver TPD
										 if(produk1.ii_bisnis_id ==814 ){
											  if(produk.ii_bisnis_no==5 || produk.ii_bisnis_no==11){
												  if(number_rider[k].intValue()!=14){
													  err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih Waiver 5 TPD(new)");
												  }
											  }else if(produk.ii_bisnis_no==6 || produk.ii_bisnis_no==12){
												  if(number_rider[k].intValue()!=15){
													  err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih Waiver 10 TPD(new)");
												  }
											  }												
										  }
										 //Critical illness
										 if(produk1.ii_bisnis_id ==813 && number_rider[k].intValue()!=8){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih Critical Illness (new)");
											}				
										 //waiver CI
										 if(produk1.ii_bisnis_id ==816){
											  if(produk.ii_bisnis_no==5 || produk.ii_bisnis_no==11){
												  if(number_rider[k].intValue()!=4){
													  err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih Waiver 5 CI(new)");
												  }
											  }else if(produk.ii_bisnis_no==6 || produk.ii_bisnis_no==12){
												  if(number_rider[k].intValue()!=5){
													  err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih Waiver 10 CI(new)");
												  }
											  }												
										  }
										 if(produk1.ii_bisnis_id ==823 && number_rider[k].intValue()!=8){
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih SMILE MEDICAL AC PLAN H(TERTANGGUNG 1)");
										  }		
									  }								
								  }	
								
								
								//(lufi)Khusus Paket SMiLe Education--//
								if(products.unitLinkNew(Integer.toString(produk.ii_bisnis_id)) &&"21,22,23".indexOf(edit.getDatausulan().getFlag_paket().toString())>-1) {																	 
										 // PA Resiko A
									 if(produk1.ii_bisnis_id ==810 && number_rider[k].intValue()!=1){
										 	err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih PA jenis A");
									 }	
									 //Payor TPD/CI/DEATH
									 if(produk1.ii_bisnis_id ==828 && number_rider[k].intValue()!=1){
										 	err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Plan ini hanya bisa memilih PAYOR 25 TPD/CI/DEATH");
									 }	
									
								}	
								//(Deddy)Khusus Powersave, stabil dan stable save, untuk ridernya apabila bayar single, maka rider selanjutnya harus single juga(kecuali rider HCP)
								if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) 
										|| products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no) ){
									if(jumlah_rider>1){
										int jmlh_error_single = 0;
										for(int i=0;i<jumlah_rider;i++){
											for(int j = i+1; j<(jumlah_rider-1);j++){
												Datarider banding1 = (Datarider)dtrd.get(i);
												Datarider banding2 = (Datarider)dtrd.get(j);
												if(banding1.getLsbs_id()==813 || banding1.getLsbs_id()==818){
													if(banding2.getLsbs_id()==813 || banding2.getLsbs_id()==818){
														if((banding1.getLsbs_id()==813 && banding1.getLsdbs_number()==5) && !(banding2.getLsbs_id()==818 && banding2.getLsdbs_number()==4)){
															jmlh_error_single = jmlh_error_single + 1;
														}else if(!(banding1.getLsbs_id()==813 && banding1.getLsdbs_number()==5) && (banding2.getLsbs_id()==818 && banding2.getLsdbs_number()==4)){
															jmlh_error_single = jmlh_error_single + 1;
														}else if((banding1.getLsbs_id()==818 && banding1.getLsdbs_number()==4) && !(banding2.getLsbs_id()==813 && banding2.getLsdbs_number()==5)){
															jmlh_error_single = jmlh_error_single + 1;
														}else if(!(banding1.getLsbs_id()==818 && banding1.getLsdbs_number()==4) && (banding2.getLsbs_id()==813 && banding2.getLsdbs_number()==5)){
															jmlh_error_single = jmlh_error_single + 1;
														}
													}
												}
												
												
											}
										}
										if(jmlh_error_single>0){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Rider TERM dan CI, bila salah satu Single, maka yang lainnya harus single");
										}
									}
								}
								
								
								//(7/5/08) validasi tambahan oleh yusuf untuk dana sejahtera new (163), apabila rider 801 harus sesuai mppnya, misalnya dana sejahtera 5, harus penyakit kritis mpp 5
								if(produk.ii_bisnis_id == 163 && produk1.ii_bisnis_id == 801) {
									if(number_produk.intValue() == 1 && number_rider[k].intValue() != 2) {
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Harap Pilih Rider Penyakit Kritis dengan MPP = 5");
									}else if(number_produk.intValue() == 2 && number_rider[k].intValue() != 3) {
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Harap Pilih Rider Penyakit Kritis dengan MPP = 10");
									}else if(number_produk.intValue() == 3 && number_rider[k].intValue() != 4) {
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Harap Pilih Rider Penyakit Kritis dengan MPP = 15");
									}else if(number_produk.intValue() == 4 && number_rider[k].intValue() != 5) {
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Harap Pilih Rider Penyakit Kritis dengan MPP = 20");
									}else if(number_produk.intValue() == 5 && number_rider[k].intValue() != 1) {
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Harap Pilih Rider Penyakit Kritis tanpa MPP");
									}
								}							
					/*			// ryan , iseng
								if("183,189,193".indexOf(edit.getDatausulan().getLsbs_id().toString())>-1)
								{
									if (edit.getDatausulan().getMspo_provider()==2 && edit.getDatausulan().getLsbs_id()!=823 ){
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Produk Utama Plan PROVIDER, harus memilih rider plan PROVIDER");
									}a
									if (edit.getDatausulan().getMspo_provider()==0 && edit.getDatausulan().getLsbs_id()!=820 ){
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Produk Utama Plan BIASA, harus memilih rider plan BIASA");
									}
								}*/
								
								if (type_rider.booleanValue()==false)
								{
									hasil_rider="Untuk Plan utama ini tidak bisa memilih rider ke "+(k+1);
									err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
								}else{
									
									hasil_rider = produk.cek_rider_number(number_rider[k].intValue(),kode_rider[k].intValue(),li_umur_ttg.intValue(),li_umur_pp.intValue(),up.doubleValue(),premi.doubleValue(),pmode_id.intValue(),hubungan.intValue(),kurs, pay_period);
									if (hasil_rider.trim().length()!=0)
									{
										err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
									}
									// *Andhika - CEK UMUR PESERTA TAMBAHAN
									String basic;
									String kode_x;
									Integer kod_x = 0;
									Integer kod_x2 = kode_rider[k].intValue();
									String number_x;
									Integer numb_x = 0;
									Integer numb_x2 = number_rider[k].intValue();
									Date tgl_x;

								if("819,820,823,825,826,831,832,833,838".indexOf(kod_x2.toString())>-1){
									basic = uwManager.selectBasicName(kod_x2, numb_x2);
									if(basic == null || basic == ""){
										List ptx = edit.getDatausulan().getDaftarplus();
										Integer pesertax = edit.getDatausulan().getJml_peserta();
									
										for (int i=0; i<pesertax.intValue(); i++)
										{
											PesertaPlus plus = (PesertaPlus)ptx.get(i);
											kod_x = plus.getLsbs_id();
											numb_x = plus.getLsdbs_number();
											tgl_x = plus.getTgl_lahir();
//											if(x_jenis.compareTo(flag_jenis_peserta) == 0)
												if(kod_x.compareTo(kod_x2) == 0 && numb_x.compareTo(numb_x2) == 0){
														li_umur_ttg = plus.getUmur();
														Integer tanggal2x = plus.getTgl_lahir().getDate();
														Integer bulan2x = (plus.getTgl_lahir().getMonth())+1;
														Integer tahun2x = (plus.getTgl_lahir().getYear())+1900;
												}
										}
									}else if(basic != null || basic != ""){
										li_umur_ttg = edit.getTertanggung().getMste_age();
									}
								}else{
									li_umur_ttg =  umr.umur(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);
								}
									produk1.ii_bisnis_no=number_rider[k].intValue();
									produk1.of_set_bisnis_no(produk1.ii_bisnis_no);
									produk1.ii_bisnis_id=kode_rider[k].intValue();
									produk1.ii_bisnis_id_utama=kode_rider[k].intValue();
									produk1.of_set_usia_tt(li_umur_ttg.intValue());
									produk1.of_set_usia_pp(li_umur_pp.intValue());
									produk1.of_set_pmode(pmode_id.intValue());
									produk1.li_lbayar=pay_period.intValue();
									produk1.is_kurs_id=kurs;
									if (unit[k]==null)
									{
										unit[k]=new Integer(0);
									}
									if (unit[k].intValue()==0)
									{
										produk1.set_unit(unit[k].intValue());
										unit[k]=new Integer(produk1.iiunit);
									}
									if (klases[k]==null)
									{
										klases[k] = new Integer(0);
									}
									if (klases[k].intValue()==0)
									{
										produk1.set_klas(klases[k].intValue());
										klases[k] = new Integer(produk1.iiclass);
									}
									if(edit.getPowersave().getMsl_spaj_lama()!=null){
										String lsbs_id_kopi = uwManager.selectLsbsId(edit.getPowersave().getMsl_spaj_lama());
										if(products.powerSave(lsbs_id_kopi)){
											if(kode_rider[k].intValue()==822){
												beg_date_rider[k]=edit.getPowersave().getBegdate_topup();
												end_date_rider[k]=FormatDate.add(FormatDate.add(beg_date_rider[k], Calendar.YEAR, 1), Calendar.DATE, -1);
											}
										}else{
											end_date_rider[k]=defaultDateFormat.parse(FormatString.rpad("0",Integer.toString(tanggal.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan.intValue()),2)+"/"+tahun.intValue());
											beg_date_rider[k]=edit.getDatausulan().getMste_beg_date();
										}
									}else{
										end_date_rider[k]=defaultDateFormat.parse(FormatString.rpad("0",Integer.toString(tanggal.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan.intValue()),2)+"/"+tahun.intValue());
										beg_date_rider[k]=edit.getDatausulan().getMste_beg_date();
									}
									
									if (edit.getPemegang().getStatus().equalsIgnoreCase("edit"))
									{
										// pengecekan kalau ada penambahan rider tidak boleh sama dengan rider include
										int idx_rider1=produk.indeks_rider_include;
										
										for (int j=0 ; j <idx_rider1; j++)
										{
											if (kode_rider[k].intValue()==produk.rider_include[j])
											{
												flag_include[k]=new Integer(1);		
												jumlah_flag_include = new Integer(jumlah_flag_include.intValue() + 1);
											}
										}
									}
										
								if (valid.intValue()==0)
								{
										//rider pilihan tidak boleh sama dengan rider include
										if (hasil_rider.trim().length()==0)
										{
											int idx_rider1=produk.indeks_rider_include;
											for (int j=0 ; j <idx_rider1; j++)
											{
													if (kode_rider[k].intValue()==produk.rider_include[j])
													{
														
														if (flag_include[k].intValue()==0)
														{
															hasil_rider="Rider ke "+(k+1)+" tidak bisa dipilih karena merupakan rider include.";
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
														}
													}
											}
										}
										if(edit.getDatausulan().getCekCompare() == null){
											edit.getDatausulan().setCekCompare(0);
										}
									if(edit.getDatausulan().getCekCompare() == 0 ){
									//validasari umur, hubungan , class, unit rider
									if (flag_include[k].intValue()==0)
									{
										if (hasil_rider.trim().length()==0)
										{
											int lama_bayar1 = ins_rider[k].intValue();
											// *Andhika - Product Kesehatan (07/06/2012)
											Integer hubungin = 1 ;
											Integer tanggungSatu = 0;
											if("819,820,823,825,826,831,832,833,838".indexOf(kode_rider[k].toString()) >-1){
												
												List ptx_mix = edit.getDatausulan().getDaftarplus_mix();
												List dr_x = edit.getDatausulan().getDaftaRider();
												
												Integer jumPtx_mix = ptx_mix.size();
												Integer tanggung_I;
												
												if(jumPtx_mix > 0){
													for (int i=0; i<jumPtx_mix.intValue(); i++){
														PesertaPlus_mix plus2 = (PesertaPlus_mix)ptx_mix.get(i);
														Integer kodex = plus2.getLsbs_id();
														Integer numbex = plus2.getLsdbs_number();
														String namax = plus2.getNama();
														String namaTanggung = edit.getTertanggung().getMcl_first();
														Integer jenix = plus2.getFlag_jenis_peserta();
														
														// *Compare nama tertanggung tambahan I
														if(kodex != 832 && jenix == 0){
															if(!namax.equalsIgnoreCase(namaTanggung)){
																err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Peserta Utama / (Tertanggung I) harus sama dengan Tertanggung Utama");
															return;
															}
														}
														
														if(kodex == kode_rider[k] || numbex == number_rider[k]){
															hubungin = plus2.getLsre_id();
															tanggungSatu = plus2.getFlag_jenis_peserta();
														}
													}
												}
												hasil_rider=produk1.of_check_usia_kesehatan(tanggungSatu.intValue(), hubungin.intValue(), tahun2.intValue(), bulan2.intValue(), tanggal2.intValue(), tahun1.intValue(), bulan1.intValue(), tanggal1.intValue(), lama_bayar1, number_produk.intValue(), number_rider[k].intValue());
											}else{
//												if(edit.getDatausulan().getFlag_paket()==null){
												if((edit.getDatausulan().getFlag_paket()==null || edit.getDatausulan().getFlag_paket()==0) &&(kode_rider[k]!=839)){
													hasil_rider=produk1.of_check_usia_rider(tahun2.intValue(),bulan2.intValue(),tanggal2.intValue(),tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),lama_bayar1,number_produk.intValue(),number_rider[k].intValue());
												}
											}
											if (hasil_rider.trim().length()!=0)
											{
												err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
											}else{
												if (flag_gutri.intValue() == 0 && edit.getPemegang().getMste_flag_guthrie().intValue() == 0)
												{
													hasil_rider=produk1.cek_hubungan(relation_ttg);
												}
													if (hasil_rider.trim().length()!=0)
													{
														err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
													}else{
														if (kode_produk.intValue() != 1)
														{
														hasil_rider=produk.cek_rider(pmode_id.intValue(),kode_rider[k].intValue(),produk1.flag_rider,produk.flag_jenis_plan) ;
														}
														if (hasil_rider.trim().length()!=0)
														{
															hasil_rider=hasil_rider +(k+1);
															err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
														}else{
															hasil_class1=produk1.range_class(li_umur_ttg.intValue(),klases[k].intValue());
															if (hasil_class1.trim().length()!=0)
															{
																String hasil_class=hasil_class1+" "+(k+1);
																err.rejectValue("datausulan.daftaRider["+k+"].mspr_class","","HALAMAN DATA USULAN :" +hasil_class);
															}
												
															hasil_unit1=produk1.range_unit(unit[k].intValue());
															if (hasil_unit1.trim().length()!=0)
															{
																hasil_unit1=hasil_unit1+" "+(k+1);
																err.rejectValue("datausulan.daftaRider["+k+"].mspr_unit","","HALAMAN DATA USULAN :" +hasil_unit1);
															}
															
													}
										         }
											}
										}
									 }
									if("24,25,26".indexOf(edit.getDatausulan().getFlag_paket().toString())>-1 && kode_rider[k].intValue()==810){
										if(unit[k] !=2){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN :Unit Rider PA untuk SImpol Protector adalah 2.");
										}
									}
								   }
								}
										
								//cari rate rider , premi rider, persentase rider
								//Yusuf (25/02/2008) - Hidup Bahagia
									if(produk.ii_bisnis_id == 167) {
										if(produk1.ii_bisnis_id == 814) { //dengan rider 814, tarik rate nya usia PP = 0
											rate[k] = elionsManager.selectRateRider(produk1.is_kurs_id, li_umur_ttg, 0, produk1.ii_bisnis_id, produk1.ii_bisnis_no);
										}else if(produk1.ii_bisnis_id == 815) { //dengan rider 815, tarik rate nya usia TT = 0
											rate[k] = elionsManager.selectRateRider(produk1.is_kurs_id, 0, li_umur_pp, produk1.ii_bisnis_id, produk1.ii_bisnis_no);
										}
									//(Deddy) rate rider untuk produk dasar powersave, stabil link, stable save dan multiinvest
									}else if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.multiInvest(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no) ){
										 
										if(produk1.ii_bisnis_id == 813 || produk1.ii_bisnis_id == 818){
											rate[k] = elionsManager.selectRateRider(produk1.is_kurs_id, li_umur_ttg, 0, produk1.ii_bisnis_id, produk1.ii_bisnis_no);
										}else rate[k] = produk1.of_get_rate1(klases[k],produk.flag_jenis_plan,number_rider[k],li_umur_ttg,li_umur_pp);
										
									}else if(products.SuperSejahtera(Integer.toString(produk.ii_bisnis_id))){
										if(produk1.ii_bisnis_id == 801){
											rate[k] = elionsManager.selectRateRider(produk1.is_kurs_id, li_umur_ttg, 0, produk1.ii_bisnis_id, produk1.ii_bisnis_no);
										}else{
											rate[k] = produk1.of_get_rate1(klases[k],produk.flag_jenis_plan,number_rider[k],li_umur_ttg,li_umur_pp);	
										}
									}else {
										rate[k] = produk1.of_get_rate1(klases[k],produk.flag_jenis_plan,number_rider[k],li_umur_ttg,li_umur_pp);	
									}
									
									Double up_sementara= up;
									//PENYAKIT KRITIS, rate premi dikali dari UP rider 
									if(produk1.ii_bisnis_id == 801 || produk1.ii_bisnis_id == 835) {
										if(rd.getMspr_tsi()==0.)rd.setMspr_tsi(produk1.of_get_up(premi.doubleValue(),up.doubleValue(),unit[k].intValue(),produk.flag_jenis_plan,kode_rider[k].intValue(),number_rider[k].intValue(),flag_platinum));
										up_sementara= rd.getMspr_tsi();
									//untuk menentukan UP rider TERM 4 dan CI 4 dari produk powersave, stabil link , dan stable save
									//(Deddy) untuk UP rider CI 4 & SINGLE dan TERM 4 & SINGLE, user input sendiri UP nya. 
									}else if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no) ){
										if(produk1.ii_bisnis_id == 813 || produk1.ii_bisnis_id == 818){
											if(produk1.ii_bisnis_id == 813){
//												if(produk1.ii_bisnis_no==4 || produk1.ii_bisnis_no==5){
													up_sementara= up;
//												}else up_sementara= up;
											}else if(produk1.ii_bisnis_id == 818){
												if(produk1.ii_bisnis_no==3 || produk1.ii_bisnis_no==4){
													up_sementara= rd.getMspr_tsi();
												}else up_sementara= up;
											}
											
										}
										up_sementara=up;
										if(produk1.ii_bisnis_id== 822 && produk1.ii_bisnis_no==1){
											if(edit.getDatausulan().getLku_id().equals("01")){
												up_sementara=20000000.0;
											}else if(edit.getDatausulan().getLku_id().equals("02")){
												up_sementara=2000.0;
											}
										}
									}else {
										up_sementara= up;
									}
									
									if(produk1.ii_bisnis_id==822){
										if(produk1.ii_bisnis_no==1){
											if(edit.getDatausulan().getLku_id().equals("01")){
												up_sementara=20000000.0;
											}else if(edit.getDatausulan().getLku_id().equals("02")){
												up_sementara=2000.0;
											}
										}else if(produk1.ii_bisnis_no==2){
											if(products.stableSave(produk.ii_bisnis_id,produk.ii_bisnis_no) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || (produk.ii_bisnis_id==143 && (produk.ii_bisnis_no>=4 && produk.ii_bisnis_no<=6)) ||
											  (produk.ii_bisnis_id==144 && produk.ii_bisnis_no==4) || (produk.ii_bisnis_id==158 && (produk.ii_bisnis_no>=15 && produk.ii_bisnis_no<=16))){
												if(edit.getDatausulan().getLku_id().equals("01")){
													up_sementara=20000000.0;
												}else if(edit.getDatausulan().getLku_id().equals("02")){
													up_sementara=2000.0;
												}
											}else {
												up_sementara=0.1 * up;
											}
											
										}
									}
									
									up_sementara=	produk1.cek_maks_up_rider(up_sementara.doubleValue(),produk1.is_kurs_id);
									
									Double rate_rider = new Double(1.);
									if(edit.getDatausulan().getLscb_id()==3){
										rate_rider = new Double(1.);
									}else if(edit.getDatausulan().getLscb_id()==6){
										rate_rider = new Double(0.12);
									}else if(edit.getDatausulan().getLscb_id()==2){
										rate_rider = new Double(0.65);
									}else if(edit.getDatausulan().getLscb_id()==1){
										rate_rider = new Double(0.35);
									}
									//Yusuf (25/02/2008) - Hidup Bahagia, Premi Ridernya tidak 0, rate dikali dari premi
									if(produk.ii_bisnis_id == 167) {
										premi_rider[k] = produk1.hit_premi_rider(rate[k], premi, 1, premi);
									//(Deddy) premi rider untuk mginya dijadikan 0 bila belum memilih mgi
									String tes;
									}else if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no)){
											Double max_up_rider = up; //zzz
											if(up_sementara> max_up_rider){
												up_sementara = max_up_rider;									
										}else if(produk1.ii_bisnis_id == 818 && (produk1.ii_bisnis_no==3 || produk1.ii_bisnis_no==4)){
											if(up_sementara>up){
												up_sementara = up;
											}
										}else if(produk1.ii_bisnis_id == 813){
											max_up_rider = up;
											if(up_sementara> max_up_rider){
												up_sementara = max_up_rider;
											}
										}
										rd.setMspr_tsi(up_sementara);
										String mgi_number = edit.getPowersave().getMps_jangka_inv();
										if(mgi_number==null || mgi_number.equals("")){
											mgi_number = "0";
										}
										Double mgi = Double.parseDouble(mgi_number);
										if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) ){
									//		klo powersave bulanan, untuk faktornya langsung tembak 0.1 aj
											// *UP CI
											if (produk.flag_powersavebulanan == 1){
												if(kode_rider[k] == 813){
													if(factor_x[k] != 0){
														premi_rider[k] = produk1.hit_premi_rider_with_factor(rate[k], up_sementara, 0.1, premi, rd.getPersenUp());
													}else{
														premi_rider[k] = premi_rider[k];
													}
												}else{
													premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara, 0.1, premi);
												}
											//untuk powersave non bulanan, faktornya tergantung pada MGI.
											//MGI 3 = 0.275, 6 = 0.525, 12 = 1
											}else {
												Double factor=0.0;
												Double diskon = 0.0;
												Double premi_tampung = premi;
												if(mgi==3){
													factor = 0.270;
												}else if(mgi==6){
													factor = 0.525;
												}else if(mgi==1){
													factor = 0.1;
												}else factor = 1.0;
												if(kode_rider[k]==820){//untuk rider eka sehat,tidak perlu dikalikan faktor mgi nya.
													factor = 1.0;
												}
												// *EKASHT
												if((kode_rider[k]==820 && ((number_rider[k]>=1 || number_rider[k]<=15) || (number_rider[k]>=91 || number_rider[k]<=105))) ||
												   (kode_rider[k]==823 && number_rider[k]<=15) ||
												   (kode_rider[k]==825 && number_rider[k]<=15)){
														premi = elionsManager.selectRateRider(kurs, li_umur_ttg, 0, kode_rider[k], number_rider[k]);
														//premi = uwManager.selectPremiSuperSehat(kurs, li_umur_ttg, kode_rider[k], number_rider[k]);
												}else if((kode_rider[k]==820 && (number_rider[k]>=16 && number_rider[k]<=90 && number_rider[k]>=106))||
													(kode_rider[k]==823 && number_rider[k]>=16) || 
													(kode_rider[k]==825 && number_rider[k]>=16)){
														diskon = 0.975;//diskon sebesar 2.5% EKAX
														premi = elionsManager.selectRateRider(kurs, li_umur_ttg, 0, kode_rider[k], number_rider[k]);
														//premi = uwManager.selectPremiSuperSehat(kurs, li_umur_ttg, kode_rider[k], number_rider[k]);
														premi = premi * diskon;
												}else if(kode_rider[k] == 813|| kode_rider[k] == 830){
														if(factor_x[k] != 0){
															premi_rider[k] = produk1.hit_premi_rider_with_factor(rate[k], up_sementara, factor, premi, rd.getPersenUp());
														}else{
															premi_rider[k] = premi_rider[k];
														}
												}else{
												premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara, factor, premi);
												//premi = premi_tampung;
												}
											}
										}else if(products.stableLink(Integer.toString(produk.ii_bisnis_id)) ){
											Double factor=0.0;
											if(mgi==3){
												factor = 0.270;
											}else if(mgi==6){
												factor = 0.525;
											}else if(mgi==1){
												factor = 0.1;
											}else factor = 1.0;
											
											if(produk1.ii_bisnis_id == 813 || produk1.ii_bisnis_id == 830 ){
												if(factor_x[k] != 0){
													premi_rider[k] = produk1.hit_premi_rider_with_factor(rate[k], up_sementara, factor, premi, rd.getPersenUp());
												}else{
													premi_rider[k] = premi_rider[k];
												}
											}else{
												premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara, factor, premi);
											}
										}else if(products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no)){
											if(products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id))){
												premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara, 0.1, premi);
											}else {
												premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara, 1, premi);
											}
										}
										
										//(Deddy) Khusus HCP powersave/ stable save, ditampilkan nilai preminya.
										if(produk1.ii_bisnis_id == 819){
											Double disc = 0.;
											if((produk1.ii_bisnis_no >= 161 && produk1.ii_bisnis_no <= 280) || (produk1.ii_bisnis_no >= 301 && produk1.ii_bisnis_no <= 380) || (produk1.ii_bisnis_no >= 391 && produk1.ii_bisnis_no <= 430) || (produk1.ii_bisnis_no >= 451 && produk1.ii_bisnis_no <= 530)){
												disc = 0.975;
												premi = elionsManager.selectRateRider(produk1.is_kurs_id, li_umur_ttg, 0, produk1.ii_bisnis_id, produk1.ii_bisnis_no);
												premi_rider[k] = disc * premi;
											}else{
												premi_rider[k] = elionsManager.selectRateRider(produk1.is_kurs_id, li_umur_ttg, 0, produk1.ii_bisnis_id, produk1.ii_bisnis_no);
											}
										}
										
										//premi_tahunan[k] = produk1.hit_premi_rider(rate[k], up_sementara, 1, premi);
									//else, premi dikali dari up rider 
									}else if(produk.ii_bisnis_id==182 && (produk.ii_bisnis_no>=1 && produk.ii_bisnis_no<=15)){
										if(produk1.ii_bisnis_id == 819){//hcpf8 untuk multi invest syariah
											premi_rider[k] = elionsManager.selectRateRider(produk1.is_kurs_id, li_umur_ttg, 0, produk1.ii_bisnis_id, produk1.ii_bisnis_no);
										}else if(produk1.ii_bisnis_id == 813 || produk1.ii_bisnis_id == 830){// *CI
											if(factor_x[k] != 0){
												premi_rider[k] = produk1.hit_premi_rider_with_factor(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi, rd.getPersenUp());
											}else{
												premi_rider[k] = premi_rider[k];
											}
										}else if(produk1.ii_bisnis_id == 820 || produk1.ii_bisnis_id == 823){
											rate[k] = elionsManager.selectRateRider(produk1.is_kurs_id, li_umur_ttg, 0, produk1.ii_bisnis_id, produk1.ii_bisnis_no);
											premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi);
										}else{
											premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi);
										}
									}else if(produk.ii_bisnis_id==183 || produk.ii_bisnis_id==189 || produk.ii_bisnis_id==193 || products.multiInvest(Integer.toString(produk.ii_bisnis_id))){//eka sehat
										Double diskon = 0.0;
										Double premi_tampung = premi;
										if((kode_rider[k]==820 || kode_rider[k]==823 || kode_rider[k]==825) && ((number_rider[k]>=1 && number_rider[k]<=15) || (number_rider[k]>=91 && number_rider[k]<=105))){
											premi = elionsManager.selectRateRider(kurs, li_umur_ttg, 0, kode_rider[k], number_rider[k]);
											//premi = uwManager.selectPremiSuperSehat(kurs, li_umur_ttg, kode_rider[k], number_rider[k]);
											premi = premi * diskon_karyawan * rate_rider;
										}else if((kode_rider[k]==820 || kode_rider[k]==823 || kode_rider[k]==825) && ((number_rider[k]>=16 && number_rider[k]<=90) || (number_rider[k]>=106 && number_rider[k]<=195) )){
											diskon = 0.975;//diskon sebesar 2.5%
											premi = elionsManager.selectRateRider(kurs, li_umur_ttg, 0, kode_rider[k], number_rider[k]);
											//premi = uwManager.selectPremiSuperSehat(kurs, li_umur_ttg, kode_rider[k], number_rider[k]);
											premi = premi * diskon * diskon_karyawan;
										} 
										premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi);
										if(produk1.ii_bisnis_id == 819){//hcpf8 untuk multi invest syariah
											premi_rider[k] = elionsManager.selectRateRider(produk1.is_kurs_id, li_umur_ttg, 0, produk1.ii_bisnis_id, produk1.ii_bisnis_no);
											Integer lscb_id = edit.getDatausulan().getLscb_id();
											Double factor = new Double(1.0);
											if(lscb_id==1){
												factor = 0.27;
											}else if(lscb_id==2){
												factor = 0.525;
											}else if(lscb_id==6){
												factor = 0.1;
											}else factor = 1.0;
											premi_rider[k] = factor.doubleValue() *premi_rider[k];
										}
										//premi = premi_tampung;
									}else if(produk.ii_bisnis_id==178){//smart medicare
										Double premi_tampung = premi;
										if(kode_rider[k]==821 && (number_rider[k]>=16)){
											premi = elionsManager.selectRateRider(kurs, li_umur_ttg, 0, kode_rider[k], number_rider[k]);
										}else if(kode_rider[k]==829){
											premi = elionsManager.selectRateRider(kurs, li_umur_ttg, 0, kode_rider[k], number_rider[k]);
										}
										premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi);
										premi = premi_tampung;
									}else if(produk.ii_bisnis_id==208){// *smart KID
										Double premi_tampung = premi;
										premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi);
										premi = premi_tampung;
									}
									else if(produk.ii_bisnis_id==169){
										if(kode_rider[k]==810){
											premi_rider[k] =  (up_sementara/1000) * 2 * produk1.idec_pct_list[pmode_id];
										}else{
											premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi);
										}
									}else if(!products.unitLink(Integer.toString(produk.ii_bisnis_id))){
										if(produk1.ii_bisnis_id == 823){
											premi_rider[k] = elionsManager.selectRateRider(kurs, li_umur_ttg, 0, kode_rider[k], number_rider[k]);
											premi_rider[k] = premi_rider[k] * rate_rider;
										}else if(produk1.ii_bisnis_id == 813 || produk1.ii_bisnis_id == 830){
											if(factor_x[k] != 0){
												premi_rider[k] = produk1.hit_premi_rider_with_factor(rate[k], up_sementara, produk1.idec_pct_list[pmode_id], premi, rd.getPersenUp());
											}else{
												premi_rider[k] = premi_rider[k];
											}
										}else{
											premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi);
										}
									}else if(produk.ii_bisnis_id==191){//rencana cerdas
										if(produk1.ii_bisnis_id == 813 || produk.ii_bisnis_id== 830){
											Double max_up_rider = up; //zzz
											if(up_sementara> max_up_rider){
												up_sementara = max_up_rider;
											}
											if(factor_x[k] != 0){
												premi_rider[k] = produk1.hit_premi_rider_with_factor(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi, rd.getPersenUp());
											}else{
												premi_rider[k] = premi_rider[k];
											}
										}else if(produk1.ii_bisnis_id == 811){
											premi_rider[k] = produk1.rate_rider * produk1.idec_pct_list[pmode_id];
										}else if(produk1.ii_bisnis_id == 814 || produk1.ii_bisnis_id == 827){
											Double factor = 1.;
											if(edit.getDatausulan().getLscb_id()==1){
												factor = 4.;
											}else if(edit.getDatausulan().getLscb_id()==2){
												factor = 2.;
											}else if(edit.getDatausulan().getLscb_id()==6){
												factor = 12.;
											}else factor = 1.0;
											Double premi_tahunan = premi * factor;
											premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi_tahunan);
										}else if(produk1.ii_bisnis_id == 819){
											if((number_rider[k] > 20 && number_rider[k] < 141) || (number_rider[k] > 160 && number_rider[k] < 281)){	
												double diskon = 0.9;
												premi_rider[k] = elionsManager.selectRateRider(produk1.is_kurs_id, li_umur_ttg, 0, produk1.ii_bisnis_id, produk1.ii_bisnis_no);
												premi_rider[k] = premi_rider[k] * rate_rider * diskon;
											}else{
												premi_rider[k] = elionsManager.selectRateRider(produk1.is_kurs_id, li_umur_ttg, 0, produk1.ii_bisnis_id, produk1.ii_bisnis_no);
												premi_rider[k] = premi_rider[k] * rate_rider;
											}
										}else if(produk1.ii_bisnis_id == 823){
											if(number_rider[k]>=16){
												double diskon = 0.975;
												premi_rider[k] = elionsManager.selectRateRider(kurs, li_umur_ttg, 0, kode_rider[k], number_rider[k]);
												premi_rider[k] = premi_rider[k] * rate_rider * diskon;
											}else{
												premi_rider[k] = elionsManager.selectRateRider(kurs, li_umur_ttg, 0, kode_rider[k], number_rider[k]);
												premi_rider[k] = premi_rider[k] * rate_rider;
											}																		
										}else{
											premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi);
										}
									}else{
										if(produk1.ii_bisnis_id == 813 || produk1.ii_bisnis_id == 830){
											if(factor_x[k] != 0){
												premi_rider[k] = produk1.hit_premi_rider_with_factor(rate[k], up_sementara, produk1.idec_pct_list[pmode_id], premi, rd.getPersenUp());
											}else{
												premi_rider[k] = premi_rider[k];
											}
										// Andhika (09/12/2013) - Premi Payor diganti dari premi pokok menjadi total premi
										}else if(produk1.ii_bisnis_id == 815 || produk1.ii_bisnis_id == 817){
											premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],edit.getInvestasiutama().getTotal_premi_sementara());
										}else{
											premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi);
										}
									}
									
									if(kode_rider[k]==822){
										if(number_rider[k]==1){
											premi_rider[k]= 0.0;
										}else if(number_rider[k]==2){
											Integer lscb_id = edit.getDatausulan().getLscb_id();
											Double factor = new Double(1.0);
											if(lscb_id==1){
												factor = 0.27;
											}else if(lscb_id==2){
												factor = 0.525;
											}else if(lscb_id==6){
												factor = 0.1;
											}else factor = 1.0;
											if(products.stableSave(produk.ii_bisnis_id,produk.ii_bisnis_no) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || (produk.ii_bisnis_id==143 && (produk.ii_bisnis_no>=4 && produk.ii_bisnis_no<=6)) ||
											   (produk.ii_bisnis_id==144 && produk.ii_bisnis_no==4) || (produk.ii_bisnis_id==158 && (produk.ii_bisnis_no>=15 && produk.ii_bisnis_no<=16)) ||
											   (kode_produk.intValue() == 145 ||kode_produk.intValue() == 146 || kode_produk.intValue() == 163)){
												if(kurs.equals("01")){
													if(premi<100000000.0){
														premi_rider[k] = factor.doubleValue() * 0.05 * rd.getMspr_tsi();
													}
												}else if(kurs.equals("02")){
													if(premi<10000.0){
														premi_rider[k] = factor.doubleValue() * 0.05 * rd.getMspr_tsi();
													}
												}
											}else{
												premi_rider[k] = factor.doubleValue() * 0.05 * rd.getMspr_tsi();
											}
											 
										}
									}
									
									percent[k]=new Integer(0);
			
									Integer tanggal_akhir_bayar1=new Integer(0);
									Integer bulan_akhir_bayar1 = new Integer(0);
									Integer tahun_akhir_bayar1=new Integer(0);
									String tgl_akhir_bayar1 ="";
									String tanggal_1_1="";
									
									Integer tanggal_akhir_polis1=null;
									Integer bulan_akhir_polis1=null;
									Integer tahun_akhir_polis1 = null;
									Integer tahun_akhir_polis1_tertanggung_I = null;
									String tgl_akhir_polis1=null;
									Integer flag_platinumlink = new Integer(produk.flag_platinumlink);
									if (flag_platinumlink == null)
									{
										flag_platinumlink = new Integer(0);
									}
									edit.getDatausulan().setFlag_platinumlink(flag_platinumlink);
									if (produk.flag_platinumlink==1 )
									{
										//rider platinum link tanggal end date berdasarkan cuti premi
										int cuti_premi = edit.getDatausulan().getMspo_installment().intValue();
										produk.wf_set_rider(tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(), cuti_premi,li_umur_ttg.intValue(),li_umur_pp.intValue());
										tanggal_akhir_polis1=new Integer(produk.ldt_edate.getTime().getDate());
										bulan_akhir_polis1=new Integer(produk.ldt_edate.getTime().getMonth()+1);
										tahun_akhir_polis1=new Integer(produk.ldt_edate.getTime().getYear()+1900);
										tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis1.intValue()),2)+"/"+Integer.toString(tahun_akhir_polis1.intValue());
										tgl_awal_rider = new Integer(produk.adt_bdate.getTime().getDate());
										bln_awal_rider = new Integer(produk.adt_bdate.getTime().getMonth()+1);
										thn_awal_rider = new Integer(produk.adt_bdate.getTime().getYear()+1900);
										tanggal_awal_rider = FormatString.rpad("0",Integer.toString(tgl_awal_rider.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bln_awal_rider.intValue()),2)+"/"+Integer.toString(thn_awal_rider.intValue());
										
										if ( number_produk.intValue()==2)
										{
											beg_date_rider[k]=defaultDateFormat.parse(tanggal_awal_rider);
										}
										rd.setMspr_beg_date(beg_date_rider[k]);
										
										if (produk1.ldt_epay!=null)
										{
											tanggal_akhir_bayar1=new Integer(produk.ldt_epay.getTime().getDate());
											bulan_akhir_bayar1=new Integer(produk.ldt_epay.getTime().getMonth()+1);
											tahun_akhir_bayar1=new Integer(produk.ldt_epay.getTime().getYear()+1900);
											tgl_akhir_bayar1=Integer.toString(tanggal_akhir_bayar1.intValue())+"/"+Integer.toString(bulan_akhir_bayar1.intValue())+"/"+Integer.toString(tahun_akhir_bayar1.intValue());
											tanggal_1_1=FormatString.rpad("0",Integer.toString(tanggal_akhir_bayar1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_bayar1.intValue()),2)+"/"+Integer.toString(tahun_akhir_bayar1.intValue())	;					
										}else{
											tgl_akhir_bayar1="";
											tanggal_1_1="";
										}
									
									}else{
										int flag_sementara = produk.flag_jenis_plan;
										if (produk.flag_worksite == 1)
										{
											/*if (produk.flag_medivest == 1)
											{
												flag_sementara = 5;
											}*/
										}
										// *CEK UMUR
										Integer ageEkaHCP = age;
										if((produk1.ii_bisnis_id==820 && (produk1.ii_bisnis_no>=106 && produk1.ii_bisnis_no<=195))||
										   (produk1.ii_bisnis_id==823 && (produk1.ii_bisnis_no>=16 && produk1.ii_bisnis_no<=105))||
										   (produk1.ii_bisnis_id==825 && (produk1.ii_bisnis_no>=16 && produk1.ii_bisnis_no<=105))||
										   (produk1.ii_bisnis_id==826 && (produk1.ii_bisnis_no>=13 && produk1.ii_bisnis_no<=72))||
										   (produk1.ii_bisnis_id==819 && (produk1.ii_bisnis_no>=161 && produk1.ii_bisnis_no<=280))||
										   (produk1.ii_bisnis_id==819 && (produk1.ii_bisnis_no>=301 && produk1.ii_bisnis_no<=380))||
										   (produk1.ii_bisnis_id==819 && (produk1.ii_bisnis_no>=391 && produk1.ii_bisnis_no<=430))||
										   (produk1.ii_bisnis_id==831 && (produk1.ii_bisnis_no>=25))||
										   (produk1.ii_bisnis_id==832 && (produk1.ii_bisnis_no>=8 && produk1.ii_bisnis_no<=35))||
										   (produk1.ii_bisnis_id==833 && (produk1.ii_bisnis_no>=8 && produk1.ii_bisnis_no<=35))||
										   (produk1.ii_bisnis_id==829) ||  (produk1.ii_bisnis_id==838)){
											
											List test2 = edit.getDatausulan().getDaftarplus_mix(); //FIXIN
											Integer daftarMix = new Integer(test2.size());
										
												for (int n=0; n<daftarMix.intValue(); n++)
												{
													PesertaPlus_mix ppm = (PesertaPlus_mix)test2.get(n);
													Integer lsbsKod = ppm.getLsbs_id();
													Integer lsdbsNum = ppm.getLsdbs_number();
													Integer umurPeserta = ppm.getUmur();
													Integer i_flagJenis = ppm.getFlag_jenis_peserta();
														if((lsbsKod.compareTo(kode_rider[k]) == 0) && (lsdbsNum.compareTo(number_rider[k]) == 0)){
															ageEkaHCP = umurPeserta;
														}
														
												}
										}else{
											ageEkaHCP = age;
										}
										if(edit.getDatausulan().getDaftaRiderAddOn()>0)	{
											List peserta = edit.getDatausulan().getDaftarplus_mix(); //FIXIN
											Integer daftarMix = new Integer(peserta.size());
											for (int z=0; z<daftarMix.intValue(); z++)
											{
												PesertaPlus_mix ppm = (PesertaPlus_mix)peserta.get(z);
												Integer i_flagJenis = ppm.getFlag_jenis_peserta();
												ArrayList add=edit.getDatausulan().getDaftaRiderAddOnTtg();
												for(int x=0;x<add.size(); x++){
													Datarider riderMed=(Datarider)add.get(x);
													if((i_flagJenis==riderMed.getJenis()) && (riderMed.getLsbs_id().compareTo(kode_rider[k]) == 0 && riderMed.getLsdbs_number().compareTo(number_rider[k]) == 0) ){
														ageEkaHCP = ppm.getUmur();
											        }
											    }
										     }		
										}
										// *ENDATE dan PAYDATE (RIDER)//UPI
										produk1.wf_set_premi(tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),pmode_id.intValue(),tahun.intValue(),bulan.intValue(),tanggal.intValue(),ins_period.intValue(),flag_sementara,ageEkaHCP.intValue(),pay_period.intValue(),produk.flag_cerdas_siswa, li_umur_pp.intValue(),produk1.ii_bisnis_id,produk1.ii_bisnis_no);
										tanggal_akhir_polis1=new Integer(produk1.ldt_edate.getTime().getDate());
										bulan_akhir_polis1=new Integer(produk1.ldt_edate.getTime().getMonth()+1);
										tahun_akhir_polis1=new Integer(produk1.ldt_edate.getTime().getYear()+1900);
										tanggal= new Integer(produk.idt_end_date.getTime().getDate());
										bulan= new Integer(produk.idt_end_date.getTime().getMonth()+1);
										tahun= new Integer(produk.idt_end_date.getTime().getYear()+1900);
									
										tgl_end = Integer.toString(tanggal.intValue());
										bln_end = Integer.toString(bulan.intValue());
										thn_end = Integer.toString(tahun.intValue());
										
										if(produk1.ii_bisnis_id==829){
											tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan.intValue()),2)+"/"+Integer.toString(tahun.intValue());
										}else{
											tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis1.intValue()),2)+"/"+Integer.toString(tahun_akhir_polis1.intValue());
										}
										
										// *PROLITA & PROSETA (RIDER)
										if(produk1.ii_bisnis_id==829){
											produk.of_set_begdate(tahun1.intValue(),bulan1.intValue(),tanggal1.intValue());
											tanggal= new Integer(produk.idt_end_date.getTime().getDate());
											bulan= new Integer(produk.idt_end_date.getTime().getMonth()+1);
											tahun= new Integer(produk.idt_end_date.getTime().getYear()+1900);
										
											tgl_end=Integer.toString(tanggal.intValue());
											bln_end = Integer.toString(bulan.intValue());
											thn_end = Integer.toString(tahun.intValue());
											
											String tgl_akhir_polis2 = FormatString.rpad("0",tgl_end,2)+"/"+FormatString.rpad("0",bln_end,2)+"/"+thn_end;
											
											if (tgl_akhir_polis2.trim().length()!=0)
											{
												rd.setMspr_end_date(defaultDateFormat.parse(tgl_akhir_polis2));
											}else{
												rd.setMspr_end_date(null);
											}
										}
										
										if (produk1.ldt_epay!=null)
										{
											tanggal_akhir_bayar1=new Integer(produk1.ldt_epay.getTime().getDate());
											bulan_akhir_bayar1=new Integer(produk1.ldt_epay.getTime().getMonth()+1);
											tahun_akhir_bayar1=new Integer(produk1.ldt_epay.getTime().getYear()+1900);
											tgl_akhir_bayar1=Integer.toString(tanggal_akhir_bayar1.intValue())+"/"+Integer.toString(bulan_akhir_bayar1.intValue())+"/"+Integer.toString(tahun_akhir_bayar1.intValue());
											tanggal_1_1=FormatString.rpad("0",Integer.toString(tanggal_akhir_bayar1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_bayar1.intValue()),2)+"/"+Integer.toString(tahun_akhir_bayar1.intValue())	;					
											/*if (produk.flag_jenis_plan == 6)
											{
												tgl_akhir_bayar1=tgl_akhir_polis1;
												tanggal_1_1=tgl_akhir_polis1;
											}*/
										}else{
											tgl_akhir_bayar1="";
											tanggal_1_1="";
										}
										rd.setMspr_beg_date(tgl_beg_date );
									}
									
									List ptx_mix = edit.getDatausulan().getDaftarplus_mix();
									List dr_x = edit.getDatausulan().getDaftaRider();
									Integer jumPtx_mix = ptx_mix.size();
//									if(jumPtx_mix > 0){
//										for (int i=0; i<jumPtx_mix.intValue(); i++){
//											PesertaPlus_mix plus2 = (PesertaPlus_mix)ptx_mix.get(i);
//												Integer kodex = plus2.getLsbs_id();
//												Integer numbex = plus2.getLsdbs_number();
//												Integer jenix = plus2.getFlag_jenis_peserta();
//									edit.getDatausulan().setTgl_tertanggung_I(tahun_akhir_polis1_tertanggung_I);
									if(tahun_akhir_polis1_tertanggung_I==null){
										tahun_akhir_polis1_tertanggung_I = 0;
									}
									if(jenis[k] == 0){ // *Tertanggung utama
										tahun_akhir_polis1_tertanggung_I = tahun_akhir_polis1;
										edit.getDatausulan().setTgl_tertanggung_I(tahun_akhir_polis1_tertanggung_I);
										if(kode_rider[k]==838){
											edit.getDatausulan().setTgl_tertanggung_I_RI(tahun_akhir_polis1_tertanggung_I);
										}
										if(tahun_akhir_polis1>tahun){
											tahun_akhir_polis1=tahun;
											if(bulan_akhir_polis1>bulan){
												bulan_akhir_polis1= bulan;
											}
										tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis1.intValue()),2)+"/"+Integer.toString(tahun_akhir_polis1.intValue());
										}
									
										if(tahun_akhir_bayar1>tahun ){ // *tahun = endPay prod utama
											tahun_akhir_bayar1=tahun;
											if(bulan_akhir_bayar1>bulan-1){
												bulan_akhir_bayar1= bulan-1;
											}
											tgl_akhir_bayar1=Integer.toString(tanggal_akhir_bayar1.intValue())+"/"+Integer.toString(bulan_akhir_bayar1.intValue())+"/"+Integer.toString(tahun_akhir_bayar1.intValue());
											tanggal_1_1=FormatString.rpad("0",Integer.toString(tanggal_akhir_bayar1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_bayar1.intValue()),2)+"/"+Integer.toString(tahun_akhir_bayar1.intValue())	;	
										}
									}else{
//										if(produk1.ii_bisnis_id!=820 && produk1.ii_bisnis_id!=823 && produk1.ii_bisnis_id!=820 && produk1.ii_bisnis_id!=825 && produk1.ii_bisnis_id!=826 ||
//									       produk1.ii_bisnis_id!=819 && produk1.ii_bisnis_id!=832 && produk1.ii_bisnis_id!=833){
										if("819,820,823,825,826,832,833,838,839".indexOf(kode_rider[k].toString())<=-1 ){
											edit.getDatausulan().setTgl_tertanggung_I(tahun_akhir_polis1);
										}
										if(produk.ii_bisnis_id==183 || produk.ii_bisnis_id==189 || produk.ii_bisnis_id==193 || produk.ii_bisnis_id==204){
											tahun_akhir_polis1_tertanggung_I = tahun_akhir_polis1;
											edit.getDatausulan().setTgl_tertanggung_I(tahun_akhir_polis1_tertanggung_I);
											if(tahun_akhir_polis1>tahun){
												tahun_akhir_polis1=tahun;
												if(bulan_akhir_polis1>bulan){
													bulan_akhir_polis1= bulan;
												}
											tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis1.intValue()),2)+"/"+Integer.toString(tahun_akhir_polis1.intValue());
											}
											if(tahun_akhir_bayar1>tahun ){
												tahun_akhir_bayar1=tahun;
												if(bulan_akhir_bayar1>bulan-1){
													bulan_akhir_bayar1= bulan-1;
												}
												tgl_akhir_bayar1=Integer.toString(tanggal_akhir_bayar1.intValue())+"/"+Integer.toString(bulan_akhir_bayar1.intValue())+"/"+Integer.toString(tahun_akhir_bayar1.intValue());
												tanggal_1_1=FormatString.rpad("0",Integer.toString(tanggal_akhir_bayar1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_bayar1.intValue()),2)+"/"+Integer.toString(tahun_akhir_bayar1.intValue())	;	
											}
										}
										
																			
										if((tahun_akhir_polis1>edit.getDatausulan().getTgl_tertanggung_I()) && kode_rider[k]!=839){
											tahun_akhir_polis1=edit.getDatausulan().getTgl_tertanggung_I();
											if(bulan_akhir_polis1>bulan){
												bulan_akhir_polis1= bulan;
											}
											tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis1.intValue()),2)+"/"+Integer.toString(tahun_akhir_polis1.intValue());
										}
										
										if((tahun_akhir_bayar1>edit.getDatausulan().getTgl_tertanggung_I()) && kode_rider[k]!=839){
											tahun_akhir_bayar1=edit.getDatausulan().getTgl_tertanggung_I();
											if(bulan_akhir_bayar1>bulan-1){
												bulan_akhir_bayar1= bulan-1;
											}
											tgl_akhir_bayar1=Integer.toString(tanggal_akhir_bayar1.intValue())+"/"+Integer.toString(bulan_akhir_bayar1.intValue())+"/"+Integer.toString(tahun_akhir_bayar1.intValue());
											tanggal_1_1=FormatString.rpad("0",Integer.toString(tanggal_akhir_bayar1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_bayar1.intValue()),2)+"/"+Integer.toString(tahun_akhir_bayar1.intValue())	;	
										}
									}
									
									if(kode_rider[k]==839 && jenis[k]!=0){
										
											if((tahun_akhir_polis1>edit.getDatausulan().getTgl_tertanggung_I_RI())){
												tahun_akhir_polis1=edit.getDatausulan().getTgl_tertanggung_I_RI();
												if(bulan_akhir_polis1>bulan){
													bulan_akhir_polis1= bulan;
												}
												tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis1.intValue()),2)+"/"+Integer.toString(tahun_akhir_polis1.intValue());
											}
											
											if((tahun_akhir_bayar1>edit.getDatausulan().getTgl_tertanggung_I_RI()) ){
												tahun_akhir_bayar1=edit.getDatausulan().getTgl_tertanggung_I_RI();
												if(bulan_akhir_bayar1>bulan-1){
													bulan_akhir_bayar1= bulan-1;
												}
												tgl_akhir_bayar1=Integer.toString(tanggal_akhir_bayar1.intValue())+"/"+Integer.toString(bulan_akhir_bayar1.intValue())+"/"+Integer.toString(tahun_akhir_bayar1.intValue());
												tanggal_1_1=FormatString.rpad("0",Integer.toString(tanggal_akhir_bayar1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_bayar1.intValue()),2)+"/"+Integer.toString(tahun_akhir_bayar1.intValue())	;	
											}
										
									}
									Integer akhir_polis1 = new Integer(0);
									Integer akhir_bayar1 = new Integer(0);
									if((produk1.ii_bisnis_id==820||
										produk.ii_bisnis_id== 183 || produk.ii_bisnis_id==189 || produk.ii_bisnis_id==193 ||
										produk.ii_bisnis_id== 195 || produk.ii_bisnis_id==201 || produk.ii_bisnis_id==204 ||
									    produk1.ii_bisnis_id==823 || produk1.ii_bisnis_id==825|| produk1.ii_bisnis_id==826||
									    produk1.ii_bisnis_id==832 || produk1.ii_bisnis_id==833||
									    produk1.ii_bisnis_id==819 || produk1.ii_bisnis_id==838 || produk1.ii_bisnis_id==839) && (produk1.ii_bisnis_id!=829)){
										
											akhir_polis1 = tahun_akhir_polis1;
											akhir_bayar1 = tahun_akhir_bayar1;
											edit.getDatausulan().setTahun_polis(akhir_polis1);
											edit.getDatausulan().setTahun_bayar(akhir_bayar1);
											edit.getDatausulan().setAkhir_polis1(tgl_akhir_polis1);// *endDate
											edit.getDatausulan().setAkhir_bayar1(tanggal_1_1); // *endPay
									}
									
									if((produk1.ii_bisnis_id==820 || produk1.ii_bisnis_id==823 || produk1.ii_bisnis_id==825 ||
									    produk1.ii_bisnis_id==826 || produk1.ii_bisnis_id==819 || produk1.ii_bisnis_id==832 ||
									    produk1.ii_bisnis_id==833 || produk1.ii_bisnis_id==838) && produk1.ii_bisnis_id!=829){
										// *CEK END DATE RIDER
										Integer getTahunPolis = edit.getDatausulan().getTahun_polis();
										if(getTahunPolis == null){
											getTahunPolis = 0;
										}
										if(tahun_akhir_polis1 > getTahunPolis){
											tgl_akhir_polis1 = edit.getDatausulan().getAkhir_polis1();
										}
										// *CEK END PAY RIDER
										Integer tahunBayar = edit.getDatausulan().getTahun_bayar();
										if(tahunBayar == null){
											tahunBayar = 0;
										}
										if(tahun_akhir_bayar1 > tahunBayar){
											tanggal_1_1=edit.getDatausulan().getAkhir_bayar1();
											tgl_akhir_bayar1=edit.getDatausulan().getAkhir_bayar1();
										}
									}
									
									// *CEK MASA PERTANGGUNGAN
									Integer mp = new Integer(0);
									Integer mp_main = new Integer(0);
									if(produk1.ii_bisnis_id==838 && "1,2,3,4".indexOf(number_rider[k].toString())>=0 ){
										edit.getDatausulan().setFlag_riderAddon(produk1.li_insured);
									}
									if(produk1.ii_bisnis_id==829){
										mp = new Integer(produk1.of_get_conperiod(number_produk.intValue()));
									}else{
										if(produk.ii_bisnis_id==183 || produk.ii_bisnis_id==189 || produk.ii_bisnis_id==193){
											mp = new Integer(ins_period);
										}else{
											mp = new Integer(produk1.li_insured);
										}
									}
									
									// *Andhika&LUFI - Set MAX Masa Pertanggungan Tertanggung Tambahan 
									if(jenis[k] == 0){
										edit.getDatausulan().setMasa_tanggung_I(mp);
									}else{
											if(produk.ii_bisnis_id==183 || produk.ii_bisnis_id==189 || produk.ii_bisnis_id==193 || produk.ii_bisnis_id==204){
												edit.getDatausulan().setMasa_tanggung_I(mp);
											}else if("819,820,823,825,826,832,833,838".indexOf(kode_rider[k].toString())<=-1 ){
											edit.getDatausulan().setMasa_tanggung_I(mp);
										}
									}
									
									if((produk1.ii_bisnis_id==820)||(produk1.ii_bisnis_id==823)||
									   (produk1.ii_bisnis_id==825)||(produk1.ii_bisnis_id==826)||
									   (produk1.ii_bisnis_id==832)||(produk1.ii_bisnis_id==833)||
									   (produk1.ii_bisnis_id==819) ||(produk1.ii_bisnis_id==838) ){
											mp_main = new Integer(produk1.li_insured);
											edit.getDatausulan().setMp_main(mp_main);
									}
									if(produk1.ii_bisnis_id==839 && jenis[k] != 0){
										mp_main = new Integer(edit.getDatausulan().getFlag_riderAddon().intValue());
										mp = new Integer(edit.getDatausulan().getFlag_riderAddon().intValue());
										edit.getDatausulan().setMp_main(mp_main);
									}
									
										Integer xMain = edit.getDatausulan().getMp_main();
										if(xMain == null){
											xMain = 0;}
									
										if(mp > edit.getDatausulan().getMasa_tanggung_I()){
											mp = edit.getDatausulan().getMasa_tanggung_I();}
										
										if((produk1.ii_bisnis_id==820 || produk1.ii_bisnis_id==823 || produk1.ii_bisnis_id==825 ||
											produk1.ii_bisnis_id==826 || produk1.ii_bisnis_id==819 ||
											produk1.ii_bisnis_id==832 || produk1.ii_bisnis_id==833 || produk1.ii_bisnis_id==838
											) && produk1.ii_bisnis_id!=829){
										if(mp > xMain){
											mp = xMain;}
									}
									
									if ( tgl_akhir_polis1.trim().length()!=0)
									{
										rd.setMspr_end_date(defaultDateFormat.parse(tgl_akhir_polis1));
									}else{
										rd.setMspr_end_date(null);
									}
									 if (tgl_akhir_bayar1.trim().length()!=0)
									 {
										 rd.setMspr_end_pay(defaultDateFormat.parse(tgl_akhir_bayar1));
									 }else{
										 rd.setMspr_end_pay(null);
									 }
									 if (produk.flag_platinumlink==1)
									 {
										 int cuti_premi = edit.getDatausulan().getMspo_installment().intValue();
										 rd.setMspr_ins_period(new Integer(produk.li_insured));
									 }else{
										 // *Set masa pertanggungan
										 if((produk.ii_bisnis_id==183 || produk.ii_bisnis_id==189 || produk.ii_bisnis_id==193 || produk.ii_bisnis_id==204) && (produk1.ii_bisnis_id==820 || produk1.ii_bisnis_id==823 || produk1.ii_bisnis_id==825 ||produk1.ii_bisnis_id==826 || produk1.ii_bisnis_id==829 || produk1.ii_bisnis_id==832 || produk1.ii_bisnis_id==833 || produk1.ii_bisnis_id==838)){
											 rd.setMspr_ins_period(new Integer(ins_period));
										 }else{	 
											 rd.setMspr_ins_period(mp);
										 }
										 
									 }
									 if(edit.getPowersave().getMsl_spaj_lama()!=null){
										 String lsbs_id_kopi = uwManager.selectLsbsId(edit.getPowersave().getMsl_spaj_lama());
										 if(products.powerSave(lsbs_id_kopi)){
											 rd.setMspr_beg_date(beg_date_rider[k]);
											 rd.setMspr_end_date(end_date_rider[k]);
										 }
									 }
								}
											
								//(Deddy)hitung up rider berdasarkan nilai up yg diinput user (untuk produk utama powersave, stabil link dan stablesave)
								if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id,produk.ii_bisnis_no)){
									if(produk1.ii_bisnis_id == 813 || produk1.ii_bisnis_id == 818 || produk1.ii_bisnis_id == 822){
										if(produk1.ii_bisnis_id == 813){
											// *(Andhika)Hitung UP Rider berdasarkan persentase yang dipilih (Critical Illness)
											if(factor_x[k] != 0){
												sum[k]= new Double(produk1.of_get_up_with_factor(premi.doubleValue(), rd.getMspr_tsi().doubleValue(), unit[k].intValue(), 5, kode_rider[k].intValue(), number_rider[k].intValue(), flag_platinum, rd.getPersenUp()));
											}else{
												sum[k]= sum[k];
											}
										}else if(produk1.ii_bisnis_id == 818){
											if(produk1.ii_bisnis_no==3 || produk1.ii_bisnis_no==4 || produk1.ii_bisnis_no==6){
												sum[k]= new Double(produk1.of_get_up(premi.doubleValue(),rd.getMspr_tsi().doubleValue(),unit[k].intValue(),produk.flag_jenis_plan,kode_rider[k].intValue(),number_rider[k].intValue(),flag_platinum));
											}
										}else if(produk1.ii_bisnis_id==822){
											sum[k]= new Double(produk1.of_get_up(premi.doubleValue(),rd.getMspr_tsi().doubleValue(),unit[k].intValue(),produk.flag_jenis_plan,kode_rider[k].intValue(),number_rider[k].intValue(),flag_platinum));
										}
									}else sum[k]= new Double(produk1.of_get_up(premi.doubleValue(),up.doubleValue(),unit[k].intValue(),produk.flag_jenis_plan,kode_rider[k].intValue(),number_rider[k].intValue(),flag_platinum));
								}else {
									// *(Andhika)hitung up rider
									if(produk1.ii_bisnis_id == 813 || produk1.ii_bisnis_id == 830 || produk1.ii_bisnis_id==837){
										// *Hitung UP Rider berdasarkan persentase yang dipilih
										if(factor_x[k] != 0){
											sum[k]= new Double(produk1.of_get_up_with_factor(premi.doubleValue(), up.doubleValue(), unit[k].intValue(), 5, kode_rider[k].intValue(), number_rider[k].intValue(), flag_platinum, rd.getPersenUp() ));
										}else{
											sum[k]= sum[k];
										}
									}else if(produk1.ii_bisnis_id==822){
										sum[k]= new Double(produk1.of_get_up(premi.doubleValue(),rd.getMspr_tsi().doubleValue(),unit[k].intValue(),produk.flag_jenis_plan,kode_rider[k].intValue(),number_rider[k].intValue(),flag_platinum));
									
									}else if("814,815,816,817,827,828".indexOf(Integer.toString(produk1.ii_bisnis_id)) >-1){ //Req Novi payor & Waiver menggunakan PP+ptb (lufi)
										    if(produk1.ii_bisnis_id==815 && (produk1.ii_bisnis_no==6|| produk1.ii_bisnis_no==13) ){
										    	sum[k]= new Double(produk1.of_get_up(premi.doubleValue(),up.doubleValue(),unit[k].intValue(),produk.flag_jenis_plan,kode_rider[k].intValue(),number_rider[k].intValue(),flag_platinum));
										    }else{										
											    Double premi_tunggal=edit.getInvestasiutama().getDaftartopup().getPremi_tunggal();
											    if(products.unitLinkNew(Integer.toString(produk.ii_bisnis_id))){
											    	sum[k]= new Double(produk1.of_get_up(edit.getInvestasiutama().getTotal_premi_sementara()-premi_tunggal,up.doubleValue(),unit[k].intValue(),produk.flag_jenis_plan,kode_rider[k].intValue(),number_rider[k].intValue(),flag_platinum));	
											    }else{
											    	sum[k]= new Double(produk1.of_get_up(premi.doubleValue(),up.doubleValue(),unit[k].intValue(),produk.flag_jenis_plan,kode_rider[k].intValue(),number_rider[k].intValue(),flag_platinum));
											    }
										    }
									}else sum[k]= new Double(produk1.of_get_up(premi.doubleValue(),up.doubleValue(),unit[k].intValue(),produk.flag_jenis_plan,kode_rider[k].intValue(),number_rider[k].intValue(),flag_platinum));
								}
								if(produk1.ii_bisnis_id != 813){
									sum[k] = produk1.cek_maks_up_rider(sum[k],produk1.is_kurs_id);
								}
								up_pa_rider[k] = new Double(produk1.up_pa);	
								up_pb_rider[k] = new Double(produk1.up_pb);	
								up_pc_rider[k] = new Double(produk1.up_pc);	
								up_pd_rider[k] = new Double(produk1.up_pd);	
								up_pm_rider[k] = new Double(produk1.up_pm);	
								
								//(Deddy)
								if(products.powerSave(Integer.toString(produk.ii_bisnis_id)) || products.stableLink(Integer.toString(produk.ii_bisnis_id)) || products.stableSavePremiBulanan(Integer.toString(produk.ii_bisnis_id)) || products.stableSave(produk.ii_bisnis_id, produk.ii_bisnis_no) ){
									if((produk1.ii_bisnis_id == 813 && produk1.ii_bisnis_no == 5) || (produk1.ii_bisnis_id == 818 && produk1.ii_bisnis_no == 4)){
										if(edit.getPowersave().getMpr_cara_bayar_rider()!=null){
											edit.getPowersave().setMpr_cara_bayar_rider(edit.getPowersave().getMpr_cara_bayar_rider());
											edit.getDatausulan().setLscb_id_rider(3);
										}else{
//											edit.getPowersave().setMpr_cara_bayar_rider(2);
											edit.getPowersave().setMpr_cara_bayar_rider(0);
										}
									}
								}
								
								//Yusuf - Term Rider, UP nya bisa dirubah, jadi tidak diset
								if((produk1.ii_bisnis_id == 818 && produk1.ii_bisnis_no == 2) || produk1.ii_bisnis_id == 835 ) {
									if(rd.getMspr_tsi()==0){
										rd.setMspr_tsi(sum[k]);
									}
								}else {
									rd.setMspr_tsi(sum[k]);
								}
								
								rd.setLku_id(kurs);
								rd.setMspr_rate(rate[k]);
	
								if (flag_include[k].intValue()==1)
								{
									rd.setMspr_premium(new Double(0));
									rd.setMspr_persen(new Integer(0));
									rd.setMspr_tt(new Integer(0));
								}else{
										
									if (produk.flag_rider==1)
									{
										if (produk.flag_platinumlink ==0)
										{
											//Yusuf (25/02/2008) Khusus Hidup Bahagia, Dana Sejahtera New dan simas maxi save , ridernya ada preminya!
											if(produk.ii_bisnis_id == 167 || produk.ii_bisnis_id == 163 || produk.ii_bisnis_id==191 || produk.ii_bisnis_id == 188 || produk.ii_bisnis_id==194) { 
												BigDecimal bulat = new BigDecimal(premi_rider[k]);
												bulat = bulat.setScale(0,BigDecimal.ROUND_HALF_UP);
												rd.setMspr_premium(bulat.doubleValue());
												// *Ambil nilai premi peserta tambahan
												List test2 = edit.getDatausulan().getDaftarplus_mix();//FIXIN
												Integer jumlah_mix = new Integer(test2.size());
												if(jumlah_mix > 0){
													for (int m=0 ; m<jumlah_mix.intValue(); m++)
													{
														PesertaPlus_mix pps = (PesertaPlus_mix)test2.get(m);
														Integer x_lsbsId = pps.getLsbs_id();
														Integer x_lsdbsNumber = pps.getLsdbs_number();
														if(x_lsbsId.compareTo(rd.getLsbs_id()) == 0 && x_lsdbsNumber.compareTo(rd.getLsdbs_number()) == 0){
															pps.setMspr_premium(bulat.doubleValue());
														}
													}
												}
											}else{
												rd.setMspr_premium(new Double(0));
											}
											rd.setMspr_persen(percent[k]);
											rd.setMspr_tt(insured[k]);
										}else{
												produk1.count_rate(klases[k].intValue(),unit[k].intValue(),kode_produk.intValue(),number_rider[k].intValue(),kurs,li_umur_ttg.intValue(),li_umur_pp.intValue(),up.doubleValue(),premi.doubleValue(),pmode_id.intValue(),1,ins_period.intValue(),pay_period.intValue());// FIXIN, factor_x[k].intValue()
												String mbu_jml="";
												Double mbu_jumlah=new Double(produk1.mbu_jumlah);
												if(produk.ii_bisnis_id!=166){
													mbu_jumlah = premi_rider[k];
												}
												BigDecimal mbu1 = new BigDecimal(mbu_jumlah.doubleValue());
												mbu1=mbu1.setScale(0,BigDecimal.ROUND_HALF_UP);
												mbu_jumlah=new Double(mbu1.doubleValue());
												mbu_jml=Double.toString(mbu_jumlah.doubleValue());
												premi_rider[k] = mbu_jumlah;
												rd.setMspr_premium(premi_rider[k]);
												// *FIXIN
												List test2 = edit.getDatausulan().getDaftarplus_mix();//FIXIN
												Integer jumlah_mix = new Integer(test2.size());
												if(jumlah_mix > 0){
													for (int m=0 ; m<jumlah_mix.intValue(); m++)
													{
														PesertaPlus_mix pps = (PesertaPlus_mix)test2.get(m);
														Integer x_lsbsId = pps.getLsbs_id();
														Integer x_lsdbsNumber = pps.getLsdbs_number();
														if(x_lsbsId.compareTo(rd.getLsbs_id()) == 0 && x_lsdbsNumber.compareTo(rd.getLsdbs_number()) == 0){
															pps.setMspr_premium(premi_rider[k]);
														}
													}
												}
												
												rd.setMspr_persen(percent[k]);
												rd.setMspr_tt(insured[k]);		
												
												mbu_jumlah=new Double(produk1.mbu_persen);
												mbu_jumlah= rate[k];
												mbu1 = new BigDecimal(mbu_jumlah.doubleValue());
												mbu1=mbu1.setScale(3,BigDecimal.ROUND_HALF_UP);
												mbu_jumlah=new Double(mbu1.doubleValue());
												mbu_jml=Double.toString(mbu_jumlah.doubleValue());
												rd.setMspr_rate(mbu_jumlah);
										}
									}else{
//										 khusus platinum link tidak ada biaya
										rd.setMspr_premium(premi_rider[k]);
										// *FIXIN
										List test2 = edit.getDatausulan().getDaftarplus_mix();//FIXIN
										Integer jumlah_mix = new Integer(test2.size());
										if(jumlah_mix > 0){
											for (int m=0 ; m<jumlah_mix.intValue(); m++)
											{
												PesertaPlus_mix pps = (PesertaPlus_mix)test2.get(m);
												Integer x_lsbsId = pps.getLsbs_id();
												Integer x_lsdbsNumber = pps.getLsdbs_number();
												logger.info(x_lsdbsNumber);
												if(x_lsbsId.compareTo(rd.getLsbs_id()) == 0 && x_lsdbsNumber.compareTo(rd.getLsdbs_number()) == 0){
													pps.setMspr_premium(premi_rider[k]);
													logger.info(premi_rider[k]);
												}
											}
										}
										
										rd.setMspr_persen(percent[k]);
										rd.setMspr_tt(insured[k]);	
									}
								}
								total_premi_rider=new Double(total_premi_rider.doubleValue() + rd.getMspr_premium().doubleValue());
								rd.setMspr_unit(unit[k]);
								rd.setMspr_class(klases[k]);
								rd.setMspr_tsi_pa_a(up_pa_rider[k]);
								rd.setMspr_tsi_pa_b(up_pb_rider[k]);
								rd.setMspr_tsi_pa_c(up_pc_rider[k]);
								rd.setMspr_tsi_pa_d(up_pd_rider[k]);
								rd.setMspr_tsi_pa_m(up_pm_rider[k]);
								rd.setLsbs_id(kode_rider[k]);
								rd.setLsdbs_number(number_rider[k]);
								rd.setPlan_rider(rider[k]);
								rd.setFlag_include(flag_include[k]);
								rd.setMspr_extra(mspr_extra[k]);
								logger.info(premi_rider[k]);
						}	//tutup try produk rider
						
						catch (ClassNotFoundException e)
						{
							logger.error("ERROR :", e);
						} catch (InstantiationException e) {
							logger.error("ERROR :", e);
						} catch (IllegalAccessException e) {
							logger.error("ERROR :", e);
						}	
					}
				}else{
					if (!edit.getPemegang().getReg_spaj().equalsIgnoreCase(""))
					{
						Datarider data_rider = (Datarider) this.elionsManager.selectrider_perkode(edit.getPemegang().getReg_spaj(), Integer.toString(kode_rider[k]));
						if (data_rider != null)
						{
							rd = data_rider;
						}
					}
				}
			}// *end of for ambil dari list
				edit.getDatausulan().setJumlah_seluruh_rider((jmlrider));
				edit.getDatausulan().setJmlrider_include(new Integer(0));
				edit.getDatausulan().setJmlrider((jmlrider));
				
				
				//RIDER INCLUDE
				/*
				 * define variable untuk menyimpan rider include , sekedar menampilkan saja
				 */
				Integer jm_s  = new Integer(0);		
				//kalau rider include belum diinsert ke model n pertama kali input. pada saat sudah submit tidak dijalankan lagi
			if (( jumlah_flag_include.intValue()!=produk.indeks_rider_include) )
			{
				int flag_sement = produk.flag_jenis_plan;
				edit.getDatausulan().setFlag_jenis_plan(flag_sement );
				if (valid.intValue()==0)
				{		
				if (produk.indeks_rider_include>1)
				{
					jm_s =new Integer(produk.indeks_rider_include);
					if (jm_s.intValue()==1)
					{
						jm_s=new Integer(0);
					}
				}
					jmlh_rider=new Integer(jumlah_r.intValue()+jm_s.intValue());

					edit.getDatausulan().setJumlah_seluruh_rider((jmlh_rider));
					edit.getDatausulan().setJmlrider_include(jm_s);
					edit.getDatausulan().setJmlrider((jmlrider));

						kode_rider_i = new Integer[jmlh_rider.intValue()+1];
						kd_rider_i = new Integer[jmlh_rider.intValue()+1];
						sum_i = new Double[jmlh_rider.intValue()+1];
						rate_i = new Double[jmlh_rider.intValue()+1];
						percent_i = new Integer[jmlh_rider.intValue()+1];
						premi_rider_i = new Double[jmlh_rider.intValue()+1];
						rider_i = new String[jmlh_rider.intValue()+1];
						nama_rider_i = new String[jmlh_rider.intValue()+1];
						up_pa_rider_i = new Double[jmlh_rider.intValue()+1];
						up_pb_rider_i = new Double[jmlh_rider.intValue()+1];	
						up_pc_rider_i = new Double[jmlh_rider.intValue()+1];
						up_pd_rider_i = new Double[jmlh_rider.intValue()+1];	
						up_pm_rider_i = new Double[jmlh_rider.intValue()+1];
						number_rider_i = new Integer[jmlh_rider.intValue()+1];
						end_date_rider_i = new Date[jmlh_rider.intValue()+1];
						beg_date_rider_i = new Date[jmlh_rider.intValue()+1];
						klases_i = new Integer[jmlh_rider.intValue()+1];
						unit_i = new Integer[jmlh_rider.intValue()+1];
						insured_i = new Integer[jmlh_rider.intValue()+1];
						end_pay_rider_i = new Date[jmlh_rider.intValue()+1];
						flag_include_i = new Integer[jmlh_rider.intValue()+1];
						if (jumlah_r.intValue()>0)
						{

							for (int m=0;m<jumlah_r.intValue();m++)
							{

								kode_rider_i[m]=kode_rider[m];

								kd_rider_i[m] = kd_rider[m];
							}
						}
						int nomor_produk_include=0;
						int j_rider=0;
						if (produk.indeks_rider_include>1)
						{
							Integer index = jumlah_r;
							List data_rider_include = new ArrayList();
	
							for (int k=0 ; k <produk.indeks_rider_include; k++)
							{
								Datarider dr1 = new Datarider();
								if (index.intValue() == 0 && k==0)
								{
									index=new Integer(0);
								}else{
									if (index.intValue() > 0 && k==0)
									{
										index=new Integer(index.intValue());
									}else{
										index=new Integer(index.intValue()+1);
										
									}
								}
								Datarider rd1= new Datarider();
	
								kode_rider_i[index.intValue()]=new Integer(produk.rider_include[k]);
								kd_rider_i[index.intValue()]=new Integer(produk.flag_rider_baru);
								
								if ((Integer.toString(kode_rider_i[index.intValue()].intValue())).trim().length()==1)
								{
									nama_rider_i[index.intValue()]="produk_asuransi.n_prod_0"+kode_rider_i[index.intValue()];	
								}else{
									nama_rider_i[index.intValue()]="produk_asuransi.n_prod_"+kode_rider_i[index.intValue()];	
								}
								
								if (!nama_rider_i[index.intValue()].equalsIgnoreCase("produk_asuransi.n_prod_00"))
								{
									//cek rider 
									try{
										Class aClass2 = Class.forName( nama_rider_i[index.intValue()] );
										n_prod produk2 = (n_prod)aClass2.newInstance();
										produk2.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());

										boolean type_rider=false;
										produk.cek_rider_include(number_produk.intValue(),kode_rider_i[index.intValue()].intValue(),li_umur_ttg.intValue(),li_umur_pp.intValue(),up.doubleValue(),premi.doubleValue(),pmode_id.intValue());
										nomor_produk_include=produk.nomor_rider_include;
										//set ke n_prod
										produk2.ii_bisnis_no=nomor_produk_include;
										produk2.ii_bisnis_id=kode_rider_i[index.intValue()].intValue();
										produk2.ii_bisnis_id_utama=kode_rider_i[index.intValue()].intValue();
										produk2.of_set_usia_tt(li_umur_ttg.intValue());
										produk2.of_set_usia_pp(li_umur_pp.intValue());
										produk2.of_set_pmode(pmode_id.intValue());
										produk2.li_lbayar=pay_period.intValue();
										produk2.is_kurs_id=kurs;	
										
										int flag_sementara = produk.flag_jenis_plan;
										edit.getDatausulan().setFlag_jenis_plan(flag_sementara );

										if (produk.flag_worksite == 1)
										{
											if (produk.flag_medivest == 1)
											{
												flag_sementara = 5;
											}
										}
										
										//cari end date  berlaku dan end pay  rider
										produk2.wf_set_premi(tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),pmode_id.intValue(),tahun.intValue(),bulan.intValue(),tanggal.intValue(),ins_period.intValue(),flag_sementara,age.intValue(),pay_period.intValue(),produk.flag_cerdas_siswa, li_umur_pp.intValue(),produk2.ii_bisnis_id,produk2.ii_bisnis_no);
										int tanggal_akhir_polis2=produk2.ldt_edate.getTime().getDate();
										int bulan_akhir_polis2=produk2.ldt_edate.getTime().getMonth()+1;
										int tahun_akhir_polis2=produk2.ldt_edate.getTime().getYear()+1900;
										String tgl_akhir_polis2=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis2),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis2),2)+"/"+Integer.toString(tahun_akhir_polis2);
									
										int tanggal_akhir_bayar2=0;
										int bulan_akhir_bayar2=0;
										int tahun_akhir_bayar2=0;
										String tgl_akhir_bayar2="";
										String tanggal_2_2="";
										if (produk2.ldt_epay!=null)
										{
											tanggal_akhir_bayar2=produk2.ldt_epay.getTime().getDate();
											bulan_akhir_bayar2=produk2.ldt_epay.getTime().getMonth()+1;
											tahun_akhir_bayar2=produk2.ldt_epay.getTime().getYear()+1900;
											tgl_akhir_bayar2=FormatString.rpad("0",Integer.toString(tanggal_akhir_bayar2),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_bayar2),2)+"/"+Integer.toString(tahun_akhir_bayar2);
											tanggal_2_2=FormatString.rpad("0",Integer.toString(tanggal_akhir_bayar2),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_bayar2),2)+"/"+Integer.toString(tahun_akhir_bayar2)	;					
		
										}else{
											tgl_akhir_bayar2=null;
											tanggal_2_2=null;					
										}
										
										//cari up
										sum_i[index.intValue()]=new Double(produk2.of_get_up(premi.doubleValue(),up.doubleValue(),produk.units,produk.flag_jenis_plan,kode_rider_i[index.intValue()].intValue(),nomor_produk_include,flag_platinum));
										sum_i[index.intValue()] = produk2.cek_maks_up_rider(sum_i[index.intValue()],produk2.is_kurs_id);
										up_pa_rider_i[index.intValue()] = new Double(produk2.up_pa);	
										up_pb_rider_i[index.intValue()] = new Double(produk2.up_pb);	
										up_pc_rider_i[index.intValue()] = new Double(produk2.up_pc);	
										up_pd_rider_i[index.intValue()] = new Double(produk2.up_pd);	
										up_pm_rider_i[index.intValue()] = new Double(produk2.up_pm);	
		
										rate_i[index.intValue()]=new Double(produk2.of_get_rate1(produk.klases,produk.flag_jenis_plan,nomor_produk_include,li_umur_ttg.intValue(),li_umur_pp.intValue() ));	
										rider_i[index.intValue()]=kode_rider_i[index.intValue()]+"~X"+Integer.toString(nomor_produk_include);
										
										rd1.setMspr_tt(new Integer(0));
										rd1.setPlan_rider(rider_i[index.intValue()]);
										rd1.setLsbs_id(kode_rider_i[index.intValue()]);
										rd1.setLsdbs_number(new Integer(nomor_produk_include));
										if(produk.ii_bisnis_id==169){
											if(rd1.getLsbs_id()==810){
												premi_rider_i[index.intValue()]=new Double((up.doubleValue()/1000) * 2);
											}
										}else{
											premi_rider_i[index.intValue()]=new Double(produk2.hit_premi_rider(rate_i[index.intValue()].doubleValue(),up.doubleValue(),produk2.idec_pct_list[pmode_id.intValue()],premi.doubleValue()));	
										}
										rd1.setMspr_tsi(sum_i[index.intValue()]);
										rd1.setMspr_unit(new Integer(produk.units));
										rd1.setMspr_class(new Integer(produk.klases));
										rd1.setPersenUp(new Integer(0));
										rd1.setMspr_ins_period(new Integer(produk2.li_insured));
										//asdasad
										if(edit.getPowersave().getMsl_spaj_lama()!=null){
											String lsbs_id_kopi = uwManager.selectLsbsId(edit.getPowersave().getMsl_spaj_lama());
											if(products.powerSave(lsbs_id_kopi)){
												rd1.setMspr_end_date(edit.getPowersave().getEnddate_topup());
												rd1.setMspr_beg_date(edit.getPowersave().getBegdate_topup());
											}else{
												rd1.setMspr_end_date(defaultDateFormat.parse(tgl_akhir_polis2));
												rd1.setMspr_beg_date(tgl_beg_date );
											}
										}else{
											rd1.setMspr_end_date(defaultDateFormat.parse(tgl_akhir_polis2));
											rd1.setMspr_beg_date(tgl_beg_date );
										}
										if(rd1.getLsbs_id()==822){
											rd1.setMspr_end_pay(null);
										}else{
											rd1.setMspr_end_pay(defaultDateFormat.parse(tgl_akhir_bayar2));
										}
										rd1.setMspr_end_pay(defaultDateFormat.parse(tgl_akhir_bayar2));
										rd1.setLku_id(kurs);
										rd1.setMspr_rate(rate_i[index.intValue()]);
										rd1.setMspr_persen(new Integer(0));
										rd1.setMspr_premium(new Double(0));
										rd1.setMspr_tsi_pa_a(up_pa_rider_i[index.intValue()]);
										rd1.setMspr_tsi_pa_b(up_pb_rider_i[index.intValue()]);
										rd1.setMspr_tsi_pa_c(up_pc_rider_i[index.intValue()]);
										rd1.setMspr_tsi_pa_d(up_pd_rider_i[index.intValue()]);
										rd1.setMspr_tsi_pa_m(up_pm_rider_i[index.intValue()]);
										rd1.setFlag_include(new Integer(1));
										
										rd1.setMspr_tt_include(new Integer(0));
										rd1.setPlan_rider_include(rider_i[index.intValue()]);
										rd1.setLsbs_id_include(kode_rider_i[index.intValue()]);
										rd1.setLsdbs_number_include(new Integer(nomor_produk_include));
										rd1.setMspr_tsi_include(sum_i[index.intValue()]);
										rd1.setMspr_unit_include(new Integer(produk.units));
										rd1.setMspr_class_include(new Integer(produk.klases));
										rd1.setMspr_ins_period_include(new Integer(produk2.li_insured));
										rd1.setMspr_end_date_include(defaultDateFormat.parse(tgl_akhir_polis2));
										rd1.setMspr_end_pay_include(defaultDateFormat.parse(tgl_akhir_bayar2));
										rd1.setMspr_beg_date_include(tgl_beg_date );
										
										rd1.setLku_id_include(kurs);
										rd1.setMspr_rate_include(rate_i[index.intValue()]);
										rd1.setMspr_persen_include(new Integer(0));
										rd1.setMspr_premium_include(new Double(0));
										rd1.setMspr_tsi_pa_a_include(up_pa_rider_i[index.intValue()]);
										rd1.setMspr_tsi_pa_b_include(up_pb_rider_i[index.intValue()]);
										rd1.setMspr_tsi_pa_c_include(up_pc_rider_i[index.intValue()]);
										rd1.setMspr_tsi_pa_d_include(up_pd_rider_i[index.intValue()]);
										rd1.setMspr_tsi_pa_m_include(up_pm_rider_i[index.intValue()]);	
										
										data_rider_include.add(rd1);
								//tutup try produk rider included
									}
									catch (ClassNotFoundException e)
									{
										logger.error("ERROR :", e);
									} catch (InstantiationException e) {
										logger.error("ERROR :", e);
									} catch (IllegalAccessException e) {
										logger.error("ERROR :", e);
									}
								}
							}
							edit.getDatausulan().setDaftaRider(data_rider_include);
						}
						//edit.getDatausulan().setDaftaRider(dtrd1);
						//dtrd = edit.getDatausulan().getDaftaRider();
						
						//pengecekan untuk lsbs_id sama , lsdbs_number tidak boleh ada yang sama
							if (jmlh_rider.intValue()>0)
							{
								for (int r=0 ; r <(jmlh_rider.intValue()); r++)
								{
									for (int w=(r+1); w <=(jmlh_rider.intValue()-1); w++)
									{
										if ((kode_rider_i[r].intValue())==kode_rider_i[w].intValue())
										{
											if ((kode_rider_i[r].intValue() != 819) && (kode_rider_i[r].intValue() != 820))
											{
												hasil_rider="Rider tidak boleh ada yang sama";
												//err.rejectValue("datausulan.daftaRider["+w+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
											}
										}
										if ((kode_rider_i[r].intValue() == 819 && kode_rider_i[w].intValue()==811) ||(kode_rider_i[r].intValue() == 811 && kode_rider_i[w].intValue()==819))
										{
											hasil_rider="Tidak boleh ambil Rider HCP dan HCP Family bersamaan.";
											err.rejectValue("datausulan.daftaRider["+w+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
										}
//										if (hasil_rider.trim().length()==0)
//										{
//											if (kd_rider_i[r].intValue()>0 && kd_rider_i[w].intValue()>0)
//											{
//												if (kd_rider_i[r].intValue()==kd_rider_i[w].intValue())
//												{
//													hasil_rider="Rider Critical Illness hanya bisa diambil SATU & Rider TPD hanya bisa diambil SATU";
//													err.rejectValue("datausulan.daftaRider["+w+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
//												}
//											}
//										}
										
									}
								}
							}
						
//							if (jmlh_rider.intValue()>0)
//							{//pengecekan apabila rider waiver/payor sudah diambil, tidak bisa ambil rider waiver/payor kembali
//								Integer flagSudahAmbilRiderFreePremi=0;
//								for (int r=0 ; r <(jmlh_rider.intValue()); r++)
//								{
//									if(kode_rider_i[r].intValue() == 814 || kode_rider_i[r].intValue() == 815 || kode_rider_i[r].intValue() == 816 || kode_rider_i[r].intValue() == 817){
//										flagSudahAmbilRiderFreePremi=flagSudahAmbilRiderFreePremi+1;
//									}
//								}
//								if(flagSudahAmbilRiderFreePremi>1){
//									err.rejectValue("datausulan.daftaRider[0].plan_rider","","HALAMAN DATA USULAN : Apabila sudah mengambil rider Waiver/Payor, tidak dapat mengambil rider waiver/payor kembali" );
//								}
//							}
							
						}
				}
			
			List daftaHcp = edit.getDatausulan().getDaftahcp();
			Integer index_hcp = new Integer(0);
			if (daftaHcp != null)
			{
				index_hcp = new Integer(daftaHcp.size());
			}
			Integer banyak_hcp = new Integer(0);
			Boolean hcp_up = false;

			List peserta = edit.getDatausulan().getDaftapeserta();
			Integer jumlah_hcpbasic = new Integer(0);
			if (jmlrider.intValue() > 0)
			{
				Integer index_dtrd = new Integer(0);
				if (dtrd != null)
				{
					index_dtrd = new Integer(dtrd.size());
				}
				
				//pengecekan rider tidak boleh ada  yang sama dalam 1 lsbs_id.
				//validasi hcpf
				for (int i = 0 ; i <index_dtrd.intValue() ; i++)
				{
					Datarider rider1= (Datarider)dtrd.get(i);
					if (rider1.getLsbs_id().intValue() == 819)
					{
						if ((rider1.getLsdbs_number().intValue() >= 1 && rider1.getLsdbs_number().intValue() <= 20) || (rider1.getLsdbs_number().intValue() >= 141 && rider1.getLsdbs_number().intValue() <= 160) || (rider1.getLsdbs_number().intValue() >= 281 && rider1.getLsdbs_number().intValue() <= 300) || (rider1.getLsdbs_number().intValue() >= 381 && rider1.getLsdbs_number().intValue() <= 390))
						{
							jumlah_hcpbasic = new Integer(jumlah_hcpbasic.intValue() + 1);
						}
						for (int j =(i+1); j <=index_dtrd.intValue() - 1 ; j++)
						{
							Datarider rider2= (Datarider)dtrd.get(j);
							if ((rider1.getLsbs_id().intValue() == rider2.getLsbs_id().intValue())&&(rider1.getLsdbs_number().intValue() == rider2.getLsdbs_number().intValue()))
							{
								hasil_rider="Rider tidak boleh ada yang sama";
								err.rejectValue("datausulan.daftaRider["+j+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
							}
	
						}
						if ((rider1.getLsdbs_number().intValue() >=21 && rider1.getLsdbs_number().intValue() <=140) || (rider1.getLsdbs_number().intValue() >=161 && rider1.getLsdbs_number().intValue() <=280) || (rider1.getLsdbs_number().intValue() >=301 && rider1.getLsdbs_number().intValue() <=380) || (rider1.getLsdbs_number().intValue() >=391 && rider1.getLsdbs_number().intValue() <=400))
						{
							int jumlah_sementara =  rider1.getLsdbs_number().intValue() - number_utama.intValue();
							int hasil_mod = jumlah_sementara % 20;
							if (hasil_mod  > 0)
							{
								hasil_rider="Rider SMiLe HOSPITAL PROTECTION FAMILY tidak boleh berbeda jenis";
								err.rejectValue("datausulan.daftaRider["+i+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
							}
						}
						if (status.equalsIgnoreCase("input") || flag_rider_hcp.intValue() == 0 )
						{
//							if ((rider1.getLsbs_id().intValue() == 819) && ((rider1.getLsdbs_number().intValue() >=21 && rider1.getLsdbs_number().intValue() <= 140) || (rider1.getLsdbs_number().intValue() >=161 && rider1.getLsdbs_number().intValue() <= 280) || (rider1.getLsdbs_number().intValue() >=301 && rider1.getLsdbs_number().intValue() <=380) || (rider1.getLsdbs_number().intValue() >=391 && rider1.getLsdbs_number().intValue() <=430)))
//							{
//								hasil_rider="Rider HCPF SPOUSE dan CHILD ke "+ (i+1)+" tidak bisa dipilih.Silahkan melakukan penambahan di menu HCP FAMILY. ";
//								err.rejectValue("datausulan.daftaRider["+i+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
//							}
						}
						if ((flag_hcp.intValue() == 0) && ((rider1.getLsdbs_number().intValue() >=21 && rider1.getLsdbs_number().intValue() <= 140) || (rider1.getLsdbs_number().intValue() >=161 && rider1.getLsdbs_number().intValue() <= 280) || (rider1.getLsdbs_number().intValue() >=301 && rider1.getLsdbs_number().intValue() <=380) || (rider1.getLsdbs_number().intValue() >=391 && rider1.getLsdbs_number().intValue() <=430)))
						{
							hasil_rider="Rider SMiLe HOSPITAL PROTECTION FAMILY ke "+ (i+1)+" tidak bisa dipilih kalau tidak mengambil SMiLe HOSPITAL PROTECTION FAMILY (TERTANGGUNG I). ";
							err.rejectValue("datausulan.daftaRider["+i+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
						}
					}
				}
				if (jumlah_hcpbasic.intValue() > 1)
				{
					hasil_rider="Rider SMiLe HOSPITAL PROTECTION FAMILY (TERTANGGUNG I) tidak boleh lebih dari satu. ";
					err.rejectValue("datausulan.daftaRider[0].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
	
				}
	
				List data_sementara = new ArrayList();
				List data_sementara1 = new ArrayList();
				Integer jumlah_hcp = new Integer(0);
				
				if (hasil_rider.trim().length() == 0)
				{	
					if (flag_rider_hcp.intValue() == 0 && cek_flag_hcp.intValue() == 0 )
					{
						// rider semuanya termasuk hcpf
						if (dtrd == null)
						{
							index_dtrd = new Integer(0);
						}else{
							index_dtrd = new Integer(dtrd.size());
						}
						for (int i=0;i<index_dtrd.intValue();i++)
						{
							Datarider rider1= (Datarider)dtrd.get(i);
							Datarider dr =  new Datarider();
							dr = rider1;
							data_sementara.add(dr);
						}
					}else{
						for (int i=0;i<index_dtrd.intValue();i++)
						{
							// rider tanpa hcpf
							Datarider rider1= (Datarider)dtrd.get(i);
							if (rider1.getLsbs_id().intValue() != 819 )
							{
								Datarider dr =  new Datarider();
								dr = rider1;
								data_sementara.add(dr);
							}else{
								jumlah_hcp = new Integer(jumlah_hcp.intValue() + 1);
								Datarider dr =  new Datarider();
								dr = rider1;
								data_sementara1.add(dr);
								edit.getDatausulan().setDaftahcp(data_sementara1);
								daftaHcp= edit.getDatausulan().getDaftahcp();
							}
						}
					}
				}

				// tidak boleh ada perubahan hcpf selain basic
				if (flag_rider_hcp.intValue() == 1)
				{
					List j_peserta = edit.getDatausulan().getDaftapeserta();
					Integer index_peserta = new Integer(0);
					if (j_peserta != null)
					{
						index_peserta = new Integer(j_peserta.size());
					}
					if (index_peserta.intValue() <jumlah_hcp.intValue())
					{
//						hasil_rider="Penambahan Rider HCPF selain Basic harus di menu HCP Family, tidak bisa dilakukan di window utama penginputan spaj.Silahkan tambahkan HCPF setelah mensubmit spaj ini. ";
//						err.rejectValue("datausulan.daftaRider[0].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
					}
				}
			
				//list hcpf aja
				if (daftaHcp != null)
				{
					index_hcp = new Integer(daftaHcp.size());
				}else{
					index_hcp = new Integer(0);	
				}
				//list rider termasuk hcpf
				if (dtrd == null)
				{
					index_dtrd = new Integer(0);
				}else{
					index_dtrd = new Integer(dtrd.size());
				}
				int selisih = 0;
				
				//membandingkan list hcpf saja dengan list hcpf dalam  list rider semua dengan memisahkan hcpf dalam list semua rider
			if (hasil_rider.trim().length() == 0)
			{		
				edit.getDatausulan().setCek_flag_hcp(new Integer(1));
				if(!status.equals("edit")){
					for (int i=0 ; i < index_hcp.intValue() ; i++ )
					{
						Hcp bf1= (Hcp)daftaHcp.get(i);
						for (int j = 0 ; j <index_dtrd.intValue() ; j ++)
						{
							Datarider rider1= (Datarider)dtrd.get(j);
							if ((rider1.getLsbs_id().intValue() == 819))
							{
								//selisih karena child, spouse lsdbs_number berlaku kelipatan
								if (rider1.getLsdbs_number().intValue()>= bf1.getLsdbs_number().intValue())
								{
									selisih = rider1.getLsdbs_number().intValue() - bf1.getLsdbs_number().intValue();
								}else{
									selisih = bf1.getLsdbs_number().intValue() - rider1.getLsdbs_number().intValue();
								}
	//							set rider ke hcp (kalau ganti plan)
								if ((bf1.getLsdbs_number().intValue()>=1 && bf1.getLsdbs_number().intValue() <=20) || (bf1.getLsdbs_number().intValue()>=141 && bf1.getLsdbs_number().intValue() <=160) || (bf1.getLsdbs_number().intValue() >= 281 && bf1.getLsdbs_number().intValue() <= 300) || (bf1.getLsdbs_number().intValue() >= 381 && bf1.getLsdbs_number().intValue() <= 390))
								{
									if ((rider1.getLsdbs_number().intValue() >= 1 && rider1.getLsdbs_number().intValue() <= 20) || (rider1.getLsdbs_number().intValue() >= 141 && rider1.getLsdbs_number().intValue() <= 160) || (rider1.getLsdbs_number().intValue() >= 281 && rider1.getLsdbs_number().intValue() <= 300) || (rider1.getLsdbs_number().intValue() >= 381 && rider1.getLsdbs_number().intValue() <= 390))//basic
									{
											banyak_hcp = new Integer (banyak_hcp.intValue() + 1);
	
											Hcp data1 = new Hcp();
											Datarider data2 = new Datarider();
											data1 = bf1;
											data1.setLku_id(rider1.getLku_id());
											data1.setLsbs_id(rider1.getLsbs_id());
											data1.setLsdbs_number(rider1.getLsdbs_number());
											data1.setMspr_beg_date(rider1.getMspr_beg_date());
											data1.setMspr_class(rider1.getMspr_class());
											data1.setMspr_end_date(rider1.getMspr_end_date());
											data1.setMspr_end_pay(rider1.getMspr_end_pay());
											data1.setMspr_extra(rider1.getMspr_extra());
											data1.setMspr_ins_period(rider1.getMspr_ins_period());
											data1.setMspr_persen(rider1.getMspr_persen());
											data1.setMspr_premium(rider1.getMspr_premium());
											data1.setMspr_rate(rider1.getMspr_rate());
											data1.setMspr_tsi(rider1.getMspr_tsi());
											data1.setMspr_tsi_pa_a(rider1.getMspr_tsi_pa_a());
											data1.setMspr_tsi_pa_b(rider1.getMspr_tsi_pa_b());
											data1.setMspr_tsi_pa_c(rider1.getMspr_tsi_pa_c());
											data1.setMspr_tsi_pa_d(rider1.getMspr_tsi_pa_d());
											data1.setMspr_tsi_pa_m(rider1.getMspr_tsi_pa_m());
											data1.setMspr_tt(rider1.getMspr_tt());
											data1.setMspr_unit(rider1.getMspr_unit());
											data1.setPlan_rider(rider1.getPlan_rider());
											data_sementara1.add(data1);
											
											data2.setLku_id(rider1.getLku_id());
											data2.setLsbs_id(rider1.getLsbs_id());
											data2.setLsdbs_number(rider1.getLsdbs_number());
											data2.setMspr_beg_date(rider1.getMspr_beg_date());
											data2.setMspr_class(rider1.getMspr_class());
											data2.setMspr_end_date(rider1.getMspr_end_date());
											data2.setMspr_end_pay(rider1.getMspr_end_pay());
											data2.setMspr_extra(rider1.getMspr_extra());
											data2.setMspr_ins_period(rider1.getMspr_ins_period());
											data2.setMspr_persen(rider1.getMspr_persen());
											data2.setMspr_premium(rider1.getMspr_premium());
											data2.setMspr_rate(rider1.getMspr_rate());
											data2.setMspr_tsi(rider1.getMspr_tsi());
											data2.setMspr_tsi_pa_a(rider1.getMspr_tsi_pa_a());
											data2.setMspr_tsi_pa_b(rider1.getMspr_tsi_pa_b());
											data2.setMspr_tsi_pa_c(rider1.getMspr_tsi_pa_c());
											data2.setMspr_tsi_pa_d(rider1.getMspr_tsi_pa_d());
											data2.setMspr_tsi_pa_m(rider1.getMspr_tsi_pa_m());
											data2.setMspr_tt(rider1.getMspr_tt());
											data2.setMspr_unit(rider1.getMspr_unit());
											data2.setPlan_rider(rider1.getPlan_rider());
											data_sementara.add(data2);
											
											j = index_dtrd.intValue();
	
									}
								} else if (((bf1.getLsdbs_number().intValue() >=21) && (bf1.getLsdbs_number().intValue() <=40)) || ((bf1.getLsdbs_number().intValue() >=161) && (bf1.getLsdbs_number().intValue() <=180)) || ((bf1.getLsdbs_number().intValue() >=301) && (bf1.getLsdbs_number().intValue() <=320)) || ((bf1.getLsdbs_number().intValue() >=390) && (bf1.getLsdbs_number().intValue() <=400)))//spouse
										{
									 		if((rider1.getLsdbs_number().intValue() >=21 && rider1.getLsdbs_number().intValue() <=40) || (rider1.getLsdbs_number().intValue() >=161 && rider1.getLsdbs_number().intValue() <=180) || (rider1.getLsdbs_number().intValue() >=301 && rider1.getLsdbs_number().intValue() <=320) || (rider1.getLsdbs_number().intValue() >=390 && rider1.getLsdbs_number().intValue() <=400) )
									 		{
												banyak_hcp = new Integer (banyak_hcp.intValue() + 1);
	
												Hcp data1 = new Hcp();
												Datarider data2 = new Datarider();
												data1 = bf1;
												data1.setLku_id(rider1.getLku_id());
												data1.setLsbs_id(rider1.getLsbs_id());
												data1.setLsdbs_number(rider1.getLsdbs_number());
												data1.setMspr_beg_date(rider1.getMspr_beg_date());
												data1.setMspr_class(rider1.getMspr_class());
												data1.setMspr_end_date(rider1.getMspr_end_date());
												data1.setMspr_end_pay(rider1.getMspr_end_pay());
												data1.setMspr_extra(rider1.getMspr_extra());
												data1.setMspr_ins_period(rider1.getMspr_ins_period());
												data1.setMspr_persen(rider1.getMspr_persen());
												data1.setMspr_premium(rider1.getMspr_premium());
												data1.setMspr_rate(rider1.getMspr_rate());
												data1.setMspr_tsi(rider1.getMspr_tsi());
												data1.setMspr_tsi_pa_a(rider1.getMspr_tsi_pa_a());
												data1.setMspr_tsi_pa_b(rider1.getMspr_tsi_pa_b());
												data1.setMspr_tsi_pa_c(rider1.getMspr_tsi_pa_c());
												data1.setMspr_tsi_pa_d(rider1.getMspr_tsi_pa_d());
												data1.setMspr_tsi_pa_m(rider1.getMspr_tsi_pa_m());
												data1.setMspr_tt(rider1.getMspr_tt());
												data1.setMspr_unit(rider1.getMspr_unit());
												data1.setPlan_rider(rider1.getPlan_rider());
												data_sementara1.add(data1);
												
												data2.setLku_id(rider1.getLku_id());
												data2.setLsbs_id(rider1.getLsbs_id());
												data2.setLsdbs_number(rider1.getLsdbs_number());
												data2.setMspr_beg_date(rider1.getMspr_beg_date());
												data2.setMspr_class(rider1.getMspr_class());
												data2.setMspr_end_date(rider1.getMspr_end_date());
												data2.setMspr_end_pay(rider1.getMspr_end_pay());
												data2.setMspr_extra(rider1.getMspr_extra());
												data2.setMspr_ins_period(rider1.getMspr_ins_period());
												data2.setMspr_persen(rider1.getMspr_persen());
												data2.setMspr_premium(rider1.getMspr_premium());
												data2.setMspr_rate(rider1.getMspr_rate());
												data2.setMspr_tsi(rider1.getMspr_tsi());
												data2.setMspr_tsi_pa_a(rider1.getMspr_tsi_pa_a());
												data2.setMspr_tsi_pa_b(rider1.getMspr_tsi_pa_b());
												data2.setMspr_tsi_pa_c(rider1.getMspr_tsi_pa_c());
												data2.setMspr_tsi_pa_d(rider1.getMspr_tsi_pa_d());
												data2.setMspr_tsi_pa_m(rider1.getMspr_tsi_pa_m());
												data2.setMspr_tt(rider1.getMspr_tt());
												data2.setMspr_unit(rider1.getMspr_unit());
												data2.setPlan_rider(rider1.getPlan_rider());
												data_sementara.add(data2);
												j = index_dtrd.intValue();
											}
										}else if ((bf1.getLsdbs_number().intValue() >= 41 && bf1.getLsdbs_number().intValue() <= 140) || (bf1.getLsdbs_number().intValue() >= 181 && bf1.getLsdbs_number().intValue() <= 280) || (bf1.getLsdbs_number().intValue() >= 321 && bf1.getLsdbs_number().intValue() <= 380) || (bf1.getLsdbs_number().intValue() >= 401 && bf1.getLsdbs_number().intValue() <= 430))//child
											{
												if ((rider1.getLsdbs_number().intValue() >= 41 && rider1.getLsdbs_number().intValue() <= 140) || (rider1.getLsdbs_number().intValue() >= 181 && rider1.getLsdbs_number().intValue() <= 280) || (rider1.getLsdbs_number().intValue() >= 321 && rider1.getLsdbs_number().intValue() <= 380) || (rider1.getLsdbs_number().intValue() >= 401 && rider1.getLsdbs_number().intValue() <= 430))
												{
													if (selisih <=19)
													{
														banyak_hcp = new Integer (banyak_hcp.intValue() + 1);
		
														Hcp data1 = new Hcp();
														Datarider data2 = new Datarider();
														data1 = bf1;
														data1.setLku_id(rider1.getLku_id());
														data1.setLsbs_id(rider1.getLsbs_id());
														data1.setLsdbs_number(rider1.getLsdbs_number());
														data1.setMspr_beg_date(rider1.getMspr_beg_date());
														data1.setMspr_class(rider1.getMspr_class());
														data1.setMspr_end_date(rider1.getMspr_end_date());
														data1.setMspr_end_pay(rider1.getMspr_end_pay());
														data1.setMspr_extra(rider1.getMspr_extra());
														data1.setMspr_ins_period(rider1.getMspr_ins_period());
														data1.setMspr_persen(rider1.getMspr_persen());
														data1.setMspr_premium(rider1.getMspr_premium());
														data1.setMspr_rate(rider1.getMspr_rate());
														data1.setMspr_tsi(rider1.getMspr_tsi());
														data1.setMspr_tsi_pa_a(rider1.getMspr_tsi_pa_a());
														data1.setMspr_tsi_pa_b(rider1.getMspr_tsi_pa_b());
														data1.setMspr_tsi_pa_c(rider1.getMspr_tsi_pa_c());
														data1.setMspr_tsi_pa_d(rider1.getMspr_tsi_pa_d());
														data1.setMspr_tsi_pa_m(rider1.getMspr_tsi_pa_m());
														data1.setMspr_tt(rider1.getMspr_tt());
														data1.setMspr_unit(rider1.getMspr_unit());
														data1.setPlan_rider(rider1.getPlan_rider());
														data_sementara1.add(data1);
														
														data2.setLku_id(rider1.getLku_id());
														data2.setLsbs_id(rider1.getLsbs_id());
														data2.setLsdbs_number(rider1.getLsdbs_number());
														data2.setMspr_beg_date(rider1.getMspr_beg_date());
														data2.setMspr_class(rider1.getMspr_class());
														data2.setMspr_end_date(rider1.getMspr_end_date());
														data2.setMspr_end_pay(rider1.getMspr_end_pay());
														data2.setMspr_extra(rider1.getMspr_extra());
														data2.setMspr_ins_period(rider1.getMspr_ins_period());
														data2.setMspr_persen(rider1.getMspr_persen());
														data2.setMspr_premium(rider1.getMspr_premium());
														data2.setMspr_rate(rider1.getMspr_rate());
														data2.setMspr_tsi(rider1.getMspr_tsi());
														data2.setMspr_tsi_pa_a(rider1.getMspr_tsi_pa_a());
														data2.setMspr_tsi_pa_b(rider1.getMspr_tsi_pa_b());
														data2.setMspr_tsi_pa_c(rider1.getMspr_tsi_pa_c());
														data2.setMspr_tsi_pa_d(rider1.getMspr_tsi_pa_d());
														data2.setMspr_tsi_pa_m(rider1.getMspr_tsi_pa_m());
														data2.setMspr_tt(rider1.getMspr_tt());
														data2.setMspr_unit(rider1.getMspr_unit());
														data2.setPlan_rider(rider1.getPlan_rider());
														data_sementara.add(data2);
														j = index_dtrd.intValue();
													}
												}
											}
							}
						}
					}
				}
				
				//bandingkan list hcpf hasil pemisahan list rider semuanya, dengan list hcpf aja
				Integer index_sementara = new Integer(0);
				if (data_sementara1 != null)
				{
					index_sementara = new Integer(data_sementara1.size());
				}
				if (index_sementara.intValue() != index_hcp)
				{
					hasil_rider="Perubahan tipe HCPF Child harus sama child ke nya untuk memudahkan pengupdatean. ";
					//err.rejectValue("datausulan.daftaRider[0].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);

				}

				// pengabungan list hcpf aja dengan list selain hcpf dalam array baru
				if (hasil_rider.trim().length() == 0)
				{
				
					edit.getDatausulan().setDaftahcp(data_sementara1);
					edit.getDatausulan().setDaftaRider(data_sementara);
					if (data_sementara.isEmpty()){
						edit.getDatausulan().setDaftaRider(data_sementara1);
					}
						
					if (dtrd == null)
					{
						index_dtrd = new Integer(0);
					}else{
						index_dtrd = new Integer(dtrd.size());
					}
					if (flag_rider_hcp.intValue() == 0 && cek_flag_hcp.intValue() == 1)
					{
						for (int i = 0 ; i < index_dtrd.intValue() ; i++)
						{
							Datarider rider1= (Datarider)dtrd.get(i);
							if (rider1.getLsbs_id().intValue() == 819)
							{
								Hcp data1 = new Hcp();
								Datarider data2 = new Datarider();
								data1.setLku_id(rider1.getLku_id());
								data1.setLsbs_id(rider1.getLsbs_id());
								data1.setLsdbs_number(rider1.getLsdbs_number());
								data1.setMspr_beg_date(rider1.getMspr_beg_date());
								data1.setMspr_class(rider1.getMspr_class());
								data1.setMspr_end_date(rider1.getMspr_end_date());
								data1.setMspr_end_pay(rider1.getMspr_end_pay());
								data1.setMspr_extra(rider1.getMspr_extra());
								data1.setMspr_ins_period(rider1.getMspr_ins_period());
								data1.setMspr_persen(rider1.getMspr_persen());
								data1.setMspr_premium(rider1.getMspr_premium());
								data1.setMspr_rate(rider1.getMspr_rate());
								data1.setMspr_tsi(rider1.getMspr_tsi());
								data1.setMspr_tsi_pa_a(rider1.getMspr_tsi_pa_a());
								data1.setMspr_tsi_pa_b(rider1.getMspr_tsi_pa_b());
								data1.setMspr_tsi_pa_c(rider1.getMspr_tsi_pa_c());
								data1.setMspr_tsi_pa_d(rider1.getMspr_tsi_pa_d());
								data1.setMspr_tsi_pa_m(rider1.getMspr_tsi_pa_m());
								data1.setMspr_tt(rider1.getMspr_tt());
								data1.setMspr_unit(rider1.getMspr_unit());
								data1.setPlan_rider(rider1.getPlan_rider());
								
								data1.setDiscount(new Double(0));
								data1.setKelamin(edit.getTertanggung().getMspe_sex());
								data1.setLsre_id(edit.getTertanggung().getLsre_id());
								data1.setNama(edit.getTertanggung().getMcl_first());
								data1.setNo_urut(new Integer(1));
								data1.setPlan_rider(rider1.getPlan_rider());
								data1.setPremi(rider1.getMspr_premium());
								data1.setReg_spaj(edit.getTertanggung().getReg_spaj());
								data1.setTgl_lahir(edit.getTertanggung().getMspe_date_birth());
								data1.setUmur(edit.getTertanggung().getMste_age());
								data_sementara1.add(data1);
								
								data2.setLku_id(rider1.getLku_id());
								data2.setLsbs_id(rider1.getLsbs_id());
								data2.setLsdbs_number(rider1.getLsdbs_number());
								data2.setMspr_beg_date(rider1.getMspr_beg_date());
								data2.setMspr_class(rider1.getMspr_class());
								data2.setMspr_end_date(rider1.getMspr_end_date());
								data2.setMspr_end_pay(rider1.getMspr_end_pay());
								data2.setMspr_extra(rider1.getMspr_extra());
								data2.setMspr_ins_period(rider1.getMspr_ins_period());
								data2.setMspr_persen(rider1.getMspr_persen());
								data2.setMspr_premium(rider1.getMspr_premium());
								data2.setMspr_rate(rider1.getMspr_rate());
								data2.setMspr_tsi(rider1.getMspr_tsi());
								data2.setMspr_tsi_pa_a(rider1.getMspr_tsi_pa_a());
								data2.setMspr_tsi_pa_b(rider1.getMspr_tsi_pa_b());
								data2.setMspr_tsi_pa_c(rider1.getMspr_tsi_pa_c());
								data2.setMspr_tsi_pa_d(rider1.getMspr_tsi_pa_d());
								data2.setMspr_tsi_pa_m(rider1.getMspr_tsi_pa_m());
								data2.setMspr_tt(rider1.getMspr_tt());
								data2.setMspr_unit(rider1.getMspr_unit());
								data2.setPlan_rider(rider1.getPlan_rider());
								data_sementara.add(data2);
								i = index_dtrd.intValue();
								
								
							}
							
						}
						edit.getDatausulan().setDaftahcp(data_sementara1);
						edit.getDatausulan().setDaftaRider(data_sementara);
						if (data_sementara.isEmpty()){
							edit.getDatausulan().setDaftaRider(data_sementara1);
						}
					}
					
					//pengesetan  khusus produk simas
					List data_sementara2 = new ArrayList();
					Integer index_peserta  = new Integer(0);
					if (peserta == null)
					{
						index_peserta = new Integer(0);
					}else{
						index_peserta = new Integer(peserta.size());
					}
//					for (int i = 0 ; i < index_hcp.intValue() ; i++)
//					{
//						Hcp bf1= (Hcp)daftaHcp.get(i);
//						
//						for (int j = 0 ; j <index_peserta.intValue() ; j ++)
//						{
//							//set hcp ke peserta ( kalau ganti plan)
//							Simas simas= (Simas)peserta.get(j);
//							if (bf1.getNo_urut().intValue() == simas.getNo_urut().intValue())
//							{
//								Simas data1 = new Simas();
//								data1.setDiscount(bf1.getDiscount());
//								data1.setKelamin(bf1.getKelamin());
//								data1.setLsbs_id(bf1.getLsbs_id());
//								data1.setLsdbs_number(bf1.getLsdbs_number());
//								data1.setLsre_id(bf1.getLsre_id());
//								data1.setNama(bf1.getNama());
//								data1.setNo_urut(bf1.getNo_urut());
//								data1.setPlan_rider(bf1.getPlan_rider());
//								data1.setPremi(bf1.getPremi());
//								data1.setReg_spaj(bf1.getReg_spaj());
//								data1.setTgl_lahir(bf1.getTgl_lahir());
//								data1.setUmur(bf1.getUmur());
//								data_sementara2.add(data1);
//							}
//						}
//					}
//					edit.getDatausulan().setDaftapeserta(data_sementara2);
	
					//hitung ulang umur peserta
//					if (flag_rider_hcp.intValue() == 1)
//					{
//						Map data_rider = this.elionsManager.validbac(edit, err, "hcp","windowutama");
//					}
				}
			}
		}
			
//		if (flag_simas.intValue() == 1)
//		{
//			Map data_rider = this.elionsManager.validbac(edit, err, "simas","windowutama");
//		}
		
		if(kode_flag.intValue()>1)
		{
			// link
			total_premi_sementara=new Double(total_premi_rider.doubleValue()+premi.doubleValue() + total_premi_tu.doubleValue());
			edit.getDatausulan().setTotal_premi_rider((total_premi_rider));
			edit.getDatausulan().setTotal_premi_sementara((total_premi_sementara));
			edit.getInvestasiutama().setTotal_premi_sementara(total_premi_sementara);
		}else{
			//selain link
			edit.getDatausulan().setTotal_premi_rider(new Double(0));
			edit.getDatausulan().setTotal_premi_sementara(new Double(0));
			edit.getInvestasiutama().setTotal_premi_sementara(new Double(0));
		}
		}catch (ClassNotFoundException e)
		{
			logger.error("ERROR :", e);
		} catch (InstantiationException e) {
			logger.error("ERROR :", e);
		} catch (IllegalAccessException e) {
			logger.error("ERROR :", e);
		}
	}

			if (!(status.equalsIgnoreCase("input")) && hasil_rider.trim().length() == 0)
			{
				if( edit.getDatausulan().getIndeks_validasi().intValue()==0)
				{
					Double premi_tunggal = new Double(0);
					premi_tunggal = edit.getInvestasiutama().getDaftartopup().getPremi_tunggal();
					if (premi_tunggal == null)
					{
						premi_tunggal = new Double(0);
						edit.getInvestasiutama().getDaftartopup().setPremi_tunggal(premi_tunggal);
					}
					Integer pil_tunggal = edit.getInvestasiutama().getDaftartopup().getPil_tunggal();
					if (pil_tunggal == null)
					{
						pil_tunggal = new Integer(0);
						edit.getInvestasiutama().getDaftartopup().setPil_tunggal(pil_tunggal);
					}
					
					Double premi_berkala = new Double(0);
					premi_berkala = edit.getInvestasiutama().getDaftartopup().getPremi_berkala();
					if (premi_berkala == null)
					{
						premi_berkala = new Double(0);
						edit.getInvestasiutama().getDaftartopup().setPremi_berkala(premi_berkala);
					}
					if(edit.getDatausulan().getLku_id().equals("01")){//Rupiah
						if(premi_berkala > 100000000){
						edit.getPemegang().setMkl_red_flag(1);
						edit.getTertanggung().setMkl_red_flag(1);
						edit.getInvestasiutama().getDaftartopup().setRedFlag_topup_berkala(1);
						}else{
						edit.getInvestasiutama().getDaftartopup().setRedFlag_topup_berkala(0);}
					}
					if(edit.getDatausulan().getLku_id().equals("02")){//Dolar
						if(premi_berkala > 10000){
						edit.getPemegang().setMkl_red_flag(1);
						edit.getTertanggung().setMkl_red_flag(1);
						edit.getInvestasiutama().getDaftartopup().setRedFlag_topup_berkala(1);
						}else{
						edit.getInvestasiutama().getDaftartopup().setRedFlag_topup_berkala(0);}
					}
					
					Integer pil_berkala = new Integer(0);
					pil_berkala = edit.getInvestasiutama().getDaftartopup().getPil_berkala();
					if (pil_berkala == null)
					{
						pil_berkala = new Integer(0);
						edit.getInvestasiutama().getDaftartopup().setPil_berkala(pil_berkala);
					}
					edit.getDatausulan().setTotal_premi_kombinasi(premi+premi_berkala);
					// hitung biaya, minus, fund
					if (kode_flag.intValue() == 1  || kode_flag.intValue()== 11  || kode_flag.intValue()==15 || kode_flag.intValue()== 16 )
					{
						Integer flag_bulanan = edit.getDatausulan().getFlag_bulanan();
						if(status.equals("edit")){
							edit.getPowersave().setMsl_spaj_lama(uwManager.selectCekSpajSebelumSurrender(edit.getDatausulan().getReg_spaj()));
						}
	//					Map data1 = this.elionsManager.hitbac(cmd,err,kurs,up,flag_account ,Common.isEmpty(edit.getAgen().getLca_id())?lca_id:edit.getAgen().getLca_id() ,autodebet,flag_bao1, null,null,null,premi,null, null,null,null, null, null ,null,null,null,flag_powersave,flag_bulanan ,"bungapowersave");
						edit.getInvestasiutama().setJmlh_biaya(new Integer(0));
						edit.getInvestasiutama().setDaftarbiaya(new ArrayList());
					}else if (kode_flag.intValue()>1 && kode_flag.intValue()!= 11 && kode_flag.intValue()!=15 && kode_flag.intValue()!=16)
						{
							Map data2 = null;
							if (flag_hcp.intValue() == 0 || flag_rider_hcp.intValue() == 0)
							{
								data2 = this.elionsManager.hitbac(cmd,err,kurs,up,null ,null ,null,null, kode_flag,flag_rider,pmode_id,premi,premi_berkala,premi_tunggal, flag_as,pil_berkala,pil_tunggal,null,null,null ,null,null,null,"biayaexcell");
							}else if(products.unitLinkNew(edit.getDatausulan().getLsbs_id().toString()) && (flag_hcp.intValue() == 1|| flag_rider_hcp.intValue() == 1)){								
								//FIX ME Sementara untuk unitLInk yg ngambil rider hcp
								//data2 = this.elionsManager.hitbac(cmd,err,kurs,up,null ,null ,null,null, kode_flag,flag_rider,pmode_id,premi,premi_berkala,premi_tunggal, flag_as,pil_berkala,pil_tunggal,null,null,null ,null,null,null,"hcp");
								data2 = this.elionsManager.hitbac(cmd,err,kurs,up,null ,null ,null,null, kode_flag,flag_rider,pmode_id,premi,premi_berkala,premi_tunggal, flag_as,pil_berkala,pil_tunggal,null,null,null ,null,null,null,"biayaexcell");
							}else{
								data2 = this.elionsManager.hitbac(cmd,err,kurs,up,null ,null ,null,null, kode_flag,flag_rider,pmode_id,premi,premi_berkala,premi_tunggal, flag_as,pil_berkala,pil_tunggal,null,null,null ,null,null,null,"hcp");
							}
							Double ld_premi_invest = (Double)data2.get("ld_premi_invest");
							Boolean flag_minus1 = (Boolean)data2.get("flag_minus");
							Double jumlah_minus = (Double)data2.get("jumlah_minus");
							Map data3 = this.elionsManager.hitbac(cmd,err,null,null,null ,null ,null,null, kode_flag,null,null,null,premi_berkala,premi_tunggal,null,null,null,li_pct_biaya,ld_premi_invest,flag_minus1,jumlah_minus,null,null,"fundexcell1");
						}else{
							edit.getInvestasiutama().setJmlh_biaya(new Integer(0));
							edit.getInvestasiutama().setDaftarbiaya(new ArrayList());
						}
				}
			}else{
				if (flag_powersave.intValue() == 6)
				{
					if (edit.getPowersave().getMps_roll_over()==null)
					{
						edit.getPowersave().setMps_roll_over(new Integer(3));
					}
				}
			}
		
			validateddu2(cmd, err,sp);
			
		} catch (ParseException e) {
			logger.error("ERROR :", e);
		}
	
	}
	
}