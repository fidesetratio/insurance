package com.ekalife.elions.web.bac.support;

import id.co.sinarmaslife.std.spring.util.Email;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
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
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import produk_asuransi.n_prod;

import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.PesertaPlus;
import com.ekalife.elions.model.PesertaPlus_mix;
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

public class Editduvalidatorspajnew2 implements Validator{

	protected final Log logger = LogFactory.getLog(getClass());
	
	private ElionsManager elionsManager;
	private UwManager uwManager;
	private BacManager bacManager;
	private DateFormat defaultDateFormat;
	private Products products;
	
	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
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
	
	public void validatepPremi(Object cmd, Errors err)  throws ServletException,IOException,Exception
	{
		logger.debug("EditDuValidatorSpajNew2 : validate page pembayar premi");
		Cmdeditbac edit= (Cmdeditbac) cmd;
		Boolean errPendapatanBulan = false;
		Boolean errTujuan = false;
		form_pemegang_polis pp =new form_pemegang_polis();
		
		if(edit.getDatausulan().getJenis_pemegang_polis()==1){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.perusahaan","","PEMBAYAR PREMI: Silahkan masukkan nama perusahaan pembayar premi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.alamat_perusahaan","","PEMBAYAR PREMI: Silahkan masukkan alamat pembayar premi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.kdpos_perusahaan","","PEMBAYAR PREMI  Silahkan masukkan kode pos pembayar premi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.prov_perusahaan","","PEMBAYAR PREMI: Silahkan masukkan provinsi pembayar premi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.kota_perusahaan","","PEMBAYAR PREMI  Silahkan masukkan kota pembayar premi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.area_code_perusahaan","","PEMBAYAR PREMI: Silahkan masukkan area code pembayar premi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.telp_perusahaan","","PEMBAYAR PREMI : Silahkan masukkan telpon pembayar premi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.bidang_usaha_pembayar_premi","","PEMBAYAR PREMI: Silahkan masukkan bidang usaha pembayar premi");
		}
		
		String pihakKetiga = edit.getPembayarPremi().getAda_pihak_ketiga();
		String jenisPihakKetiga = edit.getPembayarPremi().getJenis_pihak_ketiga();
		
		if (edit.getPembayarPremi().getLsre_id_premi().equals("0")){
			err.rejectValue("pembayarPremi.lsre_id_premi","","PEMBAYAR PREMI: Harus memilih calon pembayar premi" );
		}
		
		if (!edit.getPembayarPremi().getLsre_id_premi().equals("40") && pihakKetiga.equals("0")){
			err.rejectValue("pembayarPremi.lsre_id_premi","","PEMBAYAR PREMI: Harus memasukkan data pihak ketiga" );
		}
		
		if(edit.getPembayarPremi().getTotal_rutin().equals("")){
			err.rejectValue("pembayarPremi.total_rutin","","PEMBAYAR PREMI: Harus memasukkan pendapatan rutin" );
		}
		
		if(edit.getPembayarPremi().getTotal_non_rutin().equals("")){
			err.rejectValue("pembayarPremi.total_rutin","","PEMBAYAR PREMI: Harus memasukkan pendapatan non rutin" );
		}
		
		if(pihakKetiga.equals("1")){//Ada pihak ketiga
			if (jenisPihakKetiga.equals("1") && !edit.getPembayarPremi().getLsre_id_premi().equals("40")){//individu
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.nama_pihak_ketiga","","PEMBAYAR PREMI INDIVIDU: Silahkan masukkan nama Pihak Ketiga");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.alamat_lengkap","","PEMBAYAR PREMI: Silahkan masukkan alamat lengkap Pihak Ketiga");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.jabatan","","PEMBAYAR PREMI: Silahkan masukkan jabatan Pihak Ketiga");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.instansi","","PEMBAYAR PREMI: Silahkan masukkan instansi Pihak Ketiga");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.no_npwp","","PEMBAYAR PREMI: Silahkan masukkan No NPWP Pihak Ketiga");
					
					if (edit.getPembayarPremi().getLsre_id_payer().equals("0")){
						err.rejectValue("pembayarPremi.lsre_id_payer","","PEMBAYAR PREMI: Silahkan memilih hubungan dengan pemegang polis" );
					}
				
					String telpRumah=edit.getPembayarPremi().getTelp_rumah();
					if (telpRumah==null)
					{
						telpRumah="";
					}
					String hasil_telp_rmh_pp0 = pp.telpon_rmh_pp(telpRumah);
					if (hasil_telp_rmh_pp0.trim().length()!=0)
					{
						edit.getPembayarPremi().setTelp_rumah(f_validasi.convert_karakter(telpRumah));
						if (telpRumah.trim().length()==0)
						{
							telpRumah="-";
							hasil_telp_rmh_pp0 ="";
						}
						edit.getPembayarPremi().setTelp_rumah(f_validasi.convert_karakter(telpRumah));

						if (!hasil_telp_rmh_pp0.equalsIgnoreCase(""))
						{
							err.rejectValue("pembayarPremi.telp_rumah","","PEMBAYAR PREMI:" +hasil_telp_rmh_pp0);
						}
					}

					edit.getPembayarPremi().setTelp_rumah(f_validasi.convert_karakter(telpRumah));
					
					String tujuandana = edit.getPembayarPremi().getTujuan_dana();
					
					if (tujuandana.equals("LAIN - LAIN")){
						ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.tujuan_dana_lain","","PEMBAYAR PREMI: Silahkan dijelaskan tujuan dana lainnya");
						edit.getPembayarPremi().setTujuan_dana(edit.getPembayarPremi().getTujuan_dana_lain());
					}else{
						edit.getPembayarPremi().setTujuan_dana(edit.getPembayarPremi().getTujuan_dana());
					}
					
					String bidang_usaha_individu = edit.getPembayarPremi().getBidang_usaha_individu();
					if (bidang_usaha_individu.equals("LAINNYA")){
						ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.bidang_usaha_individu_lain","","PEMBAYAR PREMI: Silahkan dijelaskan Bidang usaha lainnya");
						edit.getPembayarPremi().setBidang_usaha_individu(edit.getPembayarPremi().getBidang_usaha_individu_lain());
					}else{
						edit.getPembayarPremi().setBidang_usaha_individu(edit.getPembayarPremi().getBidang_usaha_individu());
						edit .getPembayarPremi().setBidang_usaha_individu_lain("");
					}
					
					String sumberdana = edit.getPembayarPremi().getSumber_dana();
					if (sumberdana.equals("LAINNYA")){
						ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.sumber_dana_lain","","PEMBAYAR PREMI: Silahkan dijelaskan Sumber dana lainnya");
						edit.getPembayarPremi().setSumber_dana(edit.getPembayarPremi().getSumber_dana_lain());
					}else{
						edit.getPembayarPremi().setSumber_dana(edit.getPembayarPremi().getSumber_dana());
					}
				
			}else if(edit.getPembayarPremi().getLsre_id_premi().equals("41")){//Badan hukum
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.nama_pihak_ketiga","","PEMBAYAR PREMI BADAN HUKUM: Silahkan masukkan nama Pembayar Premi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.tempat_kedudukan","","PEMBAYAR PREMI: Silahkan masukkan tempat kedudukan badan hukum");

				String telpKantor=edit.getPembayarPremi().getTelp_kantor();
				if (telpKantor==null)
				{
					telpKantor="";
				}
				String hasil_telp_rmh_pp0 = pp.telpon_rmh_pp(telpKantor);
				if (hasil_telp_rmh_pp0.trim().length()!=0)
				{
					edit.getPemegang().setTelpon_rumah(f_validasi.convert_karakter(telpKantor));
					if (telpKantor.trim().length()==0)
					{
						telpKantor="-";
						hasil_telp_rmh_pp0 ="";
					}
					edit.getPemegang().setTelpon_rumah(f_validasi.convert_karakter(telpKantor));

					if (!hasil_telp_rmh_pp0.equalsIgnoreCase(""))
					{
						err.rejectValue("pembayarPremi.telpon_kantor","","PEMBAYAR PREMI:" +hasil_telp_rmh_pp0);
					}
				}
				edit.getPemegang().setTelpon_rumah(f_validasi.convert_karakter(telpKantor));
			}	
			
		}
		/*if(edit.getPembayarPremi().getLsre_id_premi().equals("40")){	
			//PendapatanRutin
			edit.getPembayarPremi().setBulan_gaji(edit.getPemegang().getBulan_gaji());
			edit.getPembayarPremi().setBulan_penghasilan(edit.getPemegang().getBulan_gaji());
			edit.getPembayarPremi().setBulan_orangtua(edit.getPemegang().getBulan_orangtua());
			edit.getPembayarPremi().setBulan_usaha(edit.getPemegang().getBulan_usaha());
			edit.getPembayarPremi().setBulan_usaha_note(edit.getPemegang().getBulan_usaha_note());
			edit.getPembayarPremi().setBulan_investasi(edit.getPemegang().getBulan_investasi());
			edit.getPembayarPremi().setBulan_investasi_note(edit.getPemegang().getBulan_investasi_note());
			edit.getPembayarPremi().setBulan_lainnya(edit.getPemegang().getBulan_lainnya());
			edit.getPembayarPremi().setBulan_lainnya_note(edit.getPemegang().getBulan_lainnya_note());
			//Tujuan Investasi
			edit.getPembayarPremi().setTujuan_proteksi(edit.getPemegang().getTujuan_proteksi());
			edit.getPembayarPremi().setTujuan_pendidikan(edit.getPemegang().getTujuan_pendidikan());
			edit.getPembayarPremi().setTujuan_investasi(edit.getPemegang().getTujuan_investasi());
			edit.getPembayarPremi().setTujuan_tabungan(edit.getPemegang().getTujuan_tabungan());
			edit.getPembayarPremi().setTujuan_pensiun(edit.getPemegang().getTujuan_pensiun());
			edit.getPembayarPremi().setTujuan_lainnya(edit.getPemegang().getTujuan_lainnya());
			edit.getPembayarPremi().setTujuan_lainnya_note(edit.getPemegang().getTujuan_lainnya_note());
		}*/
		
		//validasi untuk pekerjaan diluar pekerjaan utama
		if (edit.getPembayarPremi().getMkl_kerja_other_radio().equals("1")){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.mkl_kerja_other","","PEMBAYAR PREMI: Silahkan masukkan pekerjaan/usaha/bisnis diluar pekerjaan utama");
		}
		edit.getPembayarPremi().setMkl_kerja_other(f_validasi.convert_karakter(edit.getPembayarPremi().getMkl_kerja_other()));
		
		//Ridhaal - Note Sumber pendapatan rutin per bulan  	
		String hasilUsaha = edit.getPembayarPremi().getBulan_usaha_note();		
		if (edit.getPembayarPremi().getBulan_usaha_note()==null) hasilUsaha="";
		if (hasilUsaha.equals("")){
			edit.getPembayarPremi().setBulan_usaha("");
		}else{
			edit.getPembayarPremi().setBulan_usaha("HASIL USAHA");
		}
		
		String investt = edit.getPembayarPremi().getBulan_investasi_note();		
		if (edit.getPembayarPremi().getBulan_investasi_note()==null) investt="";
		if (investt.equals("")){
			edit.getPembayarPremi().setBulan_investasi("");
		}else{
			edit.getPembayarPremi().setBulan_investasi("HASIL INVESTASI");
		}
		
		String bulanLainya = edit.getPembayarPremi().getBulan_lainnya_note();		
		if (edit.getPembayarPremi().getBulan_lainnya_note()==null) bulanLainya="";
		if (bulanLainya.equals("")){
			edit.getPembayarPremi().setBulan_lainnya("");
		}else{
			edit.getPembayarPremi().setBulan_lainnya("LAINNYA");
		}
	
		String [] pendapatanRutinBulan = {
				f_validasi.gantiKata(edit.getPembayarPremi().getBulan_gaji()),
				f_validasi.gantiKata(edit.getPembayarPremi().getBulan_penghasilan()),
				f_validasi.gantiKata(edit.getPembayarPremi().getBulan_orangtua()),
				f_validasi.gantiKata(edit.getPembayarPremi().getBulan_usaha()+";"+f_validasi.gantiKata(edit.getPembayarPremi().getBulan_usaha_note())),
				f_validasi.gantiKata(edit.getPembayarPremi().getBulan_investasi()+";" +f_validasi.gantiKata(edit.getPembayarPremi().getBulan_investasi_note())),
				f_validasi.gantiKata(edit.getPembayarPremi().getBulan_laba()),
				f_validasi.gantiKata(edit.getPembayarPremi().getBulan_lainnya()+";"+f_validasi.gantiKata(edit.getPembayarPremi().getBulan_lainnya_note()))
		};
		
		//Ridhaal - Tujuan Investasi jika melakukan perubahan  	
		String spaj = edit.getPemegang().getReg_spaj();
		if (spaj == null){
			spaj ="";
		}
		
		if (!spaj.equalsIgnoreCase("")){
			String TujInvest = bacManager.selectKycDesc(spaj,4,5,0);			
			if (TujInvest != null){
				
				if (TujInvest.equalsIgnoreCase(";INVESTASI")){
					edit.getPembayarPremi().setTujuan_investasi("INVESTASI");				
				}
				if (TujInvest.equalsIgnoreCase(";PROTEKSI")){
					edit.getPembayarPremi().setTujuan_proteksi("PROTEKSI");				
				}
				if (TujInvest.equalsIgnoreCase(";PENDIDIKAN")){
					edit.getPembayarPremi().setTujuan_pendidikan("PENDIDIKAN");				
				}
				if (TujInvest.equalsIgnoreCase(";TABUNGAN")){
					edit.getPembayarPremi().setTujuan_tabungan("TABUNGAN");				
				}
				if (TujInvest.equalsIgnoreCase(";DANA PENSIUN")){
					edit.getPembayarPremi().setTujuan_pensiun("DANA PENSIUN ");				
				}
				if (TujInvest.equalsIgnoreCase(";INVESTASI DAN TABUNGAN")){
					edit.getPembayarPremi().setTujuan_pensiun("DANA PENSIUN ");	
					edit.getPembayarPremi().setTujuan_tabungan("TABUNGAN");	
				}
				if (TujInvest.equalsIgnoreCase(";PROTEKSI DAN INVESTASI")){
					edit.getPembayarPremi().setTujuan_proteksi("PROTEKSI");	
					edit.getPembayarPremi().setTujuan_investasi("INVESTASI");	
				}				
			}
		}
			
		
		String tujuanLainya = edit.getPembayarPremi().getTujuan_lainnya_note();		
		if (edit.getPembayarPremi().getTujuan_lainnya_note()==null) tujuanLainya="";
		if (tujuanLainya.equals("")){
			edit.getPembayarPremi().setTujuan_lainnya("");
		}else{
			edit.getPembayarPremi().setTujuan_lainnya("LAINNYA");
		}		
		
		String [] tujuanInvestasi = {
				f_validasi.gantiKata(edit.getPembayarPremi().getTujuan_proteksi()),
				f_validasi.gantiKata(edit.getPembayarPremi().getTujuan_pendidikan()),
				f_validasi.gantiKata(edit.getPembayarPremi().getTujuan_investasi()),
				f_validasi.gantiKata(edit.getPembayarPremi().getTujuan_tabungan()),
				f_validasi.gantiKata(edit.getPembayarPremi().getTujuan_pensiun()),
				f_validasi.gantiKata(edit.getPembayarPremi().getTujuan_lainnya()+";"+f_validasi.gantiKata(edit.getPembayarPremi().getTujuan_lainnya_note()))
		};
		
		//Ridhaal - Note Sumber pendapatan Non rutin per Tahun 
		String investthun = edit.getPembayarPremi().getTahun_investasi_note();		
		if (edit.getPembayarPremi().getTahun_investasi_note()==null) investthun="";
		if (investthun.equals("")){
			edit.getPembayarPremi().setTahun_investasi("");
		}else{
			edit.getPembayarPremi().setTahun_investasi("HASIL INVESTASI");
		}
		
		String tahunLainya = edit.getPembayarPremi().getTahun_lainnya_note();		
		if (edit.getPembayarPremi().getTahun_lainnya_note()==null) tahunLainya="";
		if (tahunLainya.equals("")){
			edit.getPembayarPremi().setTahun_lainnya("");
		}else{
			edit.getPembayarPremi().setTahun_lainnya("LAINNYA");
		}
		
		String [] pendapatanNonTahun = {
				f_validasi.gantiKata(edit.getPembayarPremi().getTahun_bonus()),
				f_validasi.gantiKata(edit.getPembayarPremi().getTahun_komisi()),
				f_validasi.gantiKata(edit.getPembayarPremi().getTahun_aset()),
				f_validasi.gantiKata(edit.getPembayarPremi().getTahun_investasi()+";"+f_validasi.gantiKata(edit.getPembayarPremi().getTahun_investasi_note())),
				f_validasi.gantiKata(edit.getPembayarPremi().getTahun_hadiah()),
				f_validasi.gantiKata(edit.getPembayarPremi().getTahun_lainnya()+";"+f_validasi.gantiKata(edit.getPembayarPremi().getTahun_lainnya_note()))
		};
		
		edit.getPembayarPremi().setAlasan(f_validasi.gantiKata(edit.getPembayarPremi().getAlasan()));
		edit.getPembayarPremi().setPendapatanBulan(pendapatanRutinBulan);
		edit.getPembayarPremi().setTujuanInvestasi(tujuanInvestasi);
		edit.getPembayarPremi().setPendapatanTahun(pendapatanNonTahun);
		edit.getPembayarPremi().setMspe_date_birth_3rd(edit.getPembayarPremi().getMspe_date_birth_3rd());
		edit.getPembayarPremi().setMspe_date_birth_3rd_pendirian(edit.getPembayarPremi().getMspe_date_birth_3rd_pendirian());
	
		if(edit.getPembayarPremi().getLsre_id_payer().equals("0")){
			edit.getPembayarPremi().setLsre_id_payer(edit.getPembayarPremi().getLsre_id_premi());
		}
		
		if(edit.getDatausulan().getJenis_pemegang_polis()==1){
			if(edit.getPembayarPremi().getBidang_usaha_pembayar_premi().equals("LAINNYA")){
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "pembayarPremi.bidang_usaha_premi_lainnya","","PEMBAYAR PREMI: Silahkan masukkan bidang usaha pembayar premi lainnya");
				edit.getPembayarPremi().setBidang_usaha_pembayar_premi(edit.getPembayarPremi().getBidang_usaha_premi_lainnya());
			}else{
				edit.getPembayarPremi().setBidang_usaha_pembayar_premi(edit.getPembayarPremi().getBidang_usaha_pembayar_premi());
				edit.getPembayarPremi().setBidang_usaha_premi_lainnya("");
			}
		}
		
		for(int i=0;i<pendapatanRutinBulan.length;i++){
			logger.info(pendapatanRutinBulan[i]);
			if(pendapatanRutinBulan[i].contains("-")){				
				errPendapatanBulan = true;
			}else{
				errPendapatanBulan = false;
				break;
			}
		}
			
		if(errPendapatanBulan){
			err.rejectValue("pembayarPremi.bulan_gaji","","PEMBAYAR PREMI: Harus memilih sumber pendapatan rutin perbulan" );
		}else if(!f_validasi.gantiKata(edit.getPembayarPremi().getBulan_usaha()).equals("-") && f_validasi.gantiKata(edit.getPembayarPremi().getBulan_usaha_note()).equals("-")){
			err.rejectValue("pembayarPremi.bulan_usaha","","PEMBAYAR PREMI: Jika memilih hasil usaha,harap dijelaskan hasil usahanya" );
		}else if(!f_validasi.gantiKata(edit.getPembayarPremi().getBulan_investasi()).equals("-") && f_validasi.gantiKata(edit.getPembayarPremi().getBulan_investasi_note()).equals("-")){
			err.rejectValue("pembayarPremi.bulan_investasi","","PEMBAYAR PREMI: Jika memilih hasil investasi,harap dijelaskan hasil investasinya" );
		}else if(!f_validasi.gantiKata(edit.getPembayarPremi().getBulan_lainnya()).equals("-") && f_validasi.gantiKata(edit.getPembayarPremi().getBulan_lainnya_note()).equals("-")){
			err.rejectValue("pembayarPremi.bulan_lainnya","","PEMBAYAR PREMI: Jika memilih lainnya,harap dijelaskan" );
		}
		
		//validasi sumber pendapatan rutin 
		if(!f_validasi.gantiKata(edit.getPembayarPremi().getTahun_investasi()).equals("-") && f_validasi.gantiKata(edit.getPembayarPremi().getTahun_investasi_note()).equals("-")){
			err.rejectValue("pembayarPremi.tahun_investasi","","PEMBAYAR PREMI(Sumber Pendapatan rutin per tahun): Jika memilih hasil investasi,harap dijelaskan hasil investasinya" );
		}else if(!f_validasi.gantiKata(edit.getPembayarPremi().getTahun_lainnya()).equals("-") && f_validasi.gantiKata(edit.getPembayarPremi().getTahun_lainnya_note()).equals("-")){
			err.rejectValue("pembayarPremi.tahun_lainnya","","PEMBAYAR PREMI(Sumber Pendapatan rutin per tahun): Jika memilih lainnya,harap dijelaskan" );
		}
			
		//validasi total pendapatan rutin perbulan
		String totalRutin = edit.getPembayarPremi().getTotal_rutin();
		if (totalRutin==null){
			err.rejectValue("pembayarPremi.total_rutin","","PEMBAYAR PREMI: Harus memilih sumber pendapatan total rutin perbulan" );
		}
		
		//validasi tujuan pengajuan asuransi
		for(int i=0;i<tujuanInvestasi.length;i++){
			if(tujuanInvestasi[i].contains("-") ){
				errTujuan = true;
			}else{
				errTujuan = false;
				break;
			}
		}
		
		if(errTujuan){
			err.rejectValue("pembayarPremi.tujuan_lainnya","","PEMBAYAR PREMI: Harus memilih tujuan pengajuan asuransi" );
		}
		
	}	
	
	public void validateDuNew(Object cmd, Errors err)  throws ServletException,IOException,Exception
	{

		logger.debug("EditBacValidator : validate page validatedduNew");
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
		Boolean flag_bao=new Boolean(false);
		Integer flag_worksite = new Integer(0);
		Integer flag_endowment = new Integer(0);
		Integer flag_bao1=new Integer(0);
		Cmdeditbac edit= (Cmdeditbac) cmd;
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
		if (flag_gmit==null){
			flag_gmit= new Integer(0);
			edit.getDatausulan().setMste_gmit(flag_gmit);
		}
		if (flag_karyawan==null)
		{
			flag_karyawan= new Integer(0);
			edit.getDatausulan().setMste_flag_el(flag_karyawan);
		}
		
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
		
		
		// (Andhika) 27-09-2013
		Integer sp=0;
		if(!edit.getFlag_special_case().equals("")){
		sp = Integer.parseInt(edit.getFlag_special_case());
		if (sp==null)sp=0;}
		
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
		
		// cek tgl begdate maks ke depan + belakang 120 hari
		Date haMin120 	= uwManager.selectAddWorkdays(elionsManager.selectSysdate(), -120);
		Date haPlus120 	= uwManager.selectAddWorkdays( elionsManager.selectSysdate(), 120);
		if(edit.getDatausulan().getMste_beg_date().after(haPlus120) || edit.getDatausulan().getMste_beg_date().before(haMin120))
		{
			err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Harap Cek Tanggal Mulai Berlaku Pertanggungan , Yang ada Pilih Melebihi 120 hari / kurang dari 120 hari dari hari Sekarang");
		}

		int letak=0;
		letak=bisnis.indexOf("X");
		Integer kode_produk=null;
		Integer number_produk=null;
		kode_produk=new Integer(Integer.parseInt(bisnis.substring(0,letak-1)));
		number_produk=new Integer(Integer.parseInt(bisnis.substring(letak+1,bisnis.length())));
		edit.getDatausulan().setLsbs_id(kode_produk);
		edit.getDatausulan().setLsdbs_number(number_produk);
				
		if(kode_produk==217 && number_produk==2)
		{
			edit.getDatausulan().setTipeproduk(25);
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
		
		String flag_paket_2digit = FormatString.rpad("0", edit.getDatausulan().getFlag_paket().toString(), 2);
		
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
				
				if(li_umur_ttg==0 && kode_produk==208){
					int hari1=umr.hari1(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);
					if (hari1 >= 183){
						li_umur_ttg=1;
					}
				}
				
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
				
				
				relation_ttg = Integer.toString((edit.getPemegang().getLsre_id()).intValue());			
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
						 if(produk.ii_bisnis_id==217 && produk.ii_bisnis_no == 2){			
							pay_period=edit.getDatausulan().getMspo_pay_period();
							hasil_cek_usia=produk.of_check_usia_case_paket(tahun2.intValue(),bulan2.intValue(),tanggal2.intValue(),tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),pay_period.intValue(),number_produk.intValue(),edit.getDatausulan().getFlag_paket().intValue());
						}else{
							hasil_cek_usia=produk.of_check_usia(tahun2.intValue(),bulan2.intValue(),tanggal2.intValue(),tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),pay_period.intValue(),number_produk.intValue());
						}
						 
						if (hasil_cek_usia.trim().length()!=0 && sp != 1){
							 err.rejectValue("datausulan.mste_beg_date","","HALAMAN DATA USULAN :" +hasil_cek_usia);
						}
					}
					
					if (jumlah_cancel.intValue()==0 && flag_gutri.intValue()==0 && mste_flag_guthrie.intValue()==0){
						if (li_umur_pp.intValue()<17 && sp != 1){
							err.rejectValue("datausulan.mste_beg_date","","HALAMAN DATA USULAN :Umur pemegang polis minimum 17 tahun");
						}
					}
				}

				if(produk.ii_bisnis_id==212){
					ins_period = edit.getDatausulan().getMspr_ins_period();
					produk.ii_contract_period = ins_period;
				}else{
					ins_period = new Integer(produk.of_get_conperiod(number_produk.intValue()));
				}
				
				if(produk.ii_bisnis_id==212){
					if(produk.ii_bisnis_no==9 || produk.ii_bisnis_no==14){ // add lsdbs_number = 14 (Nana) add smile proteksi 212-14 helpdesk 147672
						if(ins_period<10 || ins_period>20){
							err.rejectValue("datausulan.mspr_ins_period","","HALAMAN DATA USULAN : Untuk produk ini, masa Asuransi yg diperbolehkan hanya 10-20 Tahun");
						} else if(produk.ii_age + ins_period > 65){ //perubahan sk smile proteksi umur masuk jadi 55 dan mpp jadi 65, helpdesk 128393 //Chandra
							err.rejectValue("datausulan.mspr_ins_period","","HALAMAN DATA USULAN : Untuk produk ini, Usia tertanggung + masa pertanggungan maksimal = 65");
						}
					} else if(produk.ii_bisnis_no == 10){
						if(ins_period != 10){
							err.rejectValue("datausulan.mspr_ins_period","","HALAMAN DATA USULAN : Untuk produk ini, masa Asuransi yg diperbolehkan hanya 10 Tahun");
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
					if (hasil_endowmen.trim().length() != 0){
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
						hasil_kurs_up="Plan ini tidak bisa dengan kurs tersebut, silahkan memilih dengan kurs up lain";
						hasil_kurs_premi="";
						err.rejectValue("datausulan.lku_id","","HALAMAN DATA USULAN : " +hasil_kurs_up);
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
						hasil_kurs_premi="Plan ini tidak bisa dengan kurs tersebut, silahkan memilih dengan kurs premi lain";
						hasil_kurs_up="";	
						err.rejectValue("datausulan.lku_id","","HALAMAN DATA USULAN : " +hasil_kurs_premi);	
					}
				}			
				
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
							}
							
							
								//pengecekan kombinasi berapa saja yang diperbolehkan
								String hasil_kombinasi="";
								if("120,128,127,129,134,190,200,213,216,220,224".indexOf(edit.getDatausulan().getLsbs_id().toString())>-1){
									produk.f_kombinasi();
								}
								int indeks_kombinasi=produk.indeks_kombinasi;
								Integer ok = new Integer(0);
								
								for (int j=0 ; j <indeks_kombinasi; j++)
								{
									if(ok==1) break;
									String lcaId_x = edit.getCurrentUser().getLca_id();
									if(lcaId_x.equals("37") || lcaId_x.equals("52") || lcaId_x.equals("63")){ // *agency
										if (pil_kombinasi.equalsIgnoreCase(produk.kombinasi[j]))
										{
											ok = new Integer(1);
										}
									}else{
										/*if(("K,L,N,M,O,P,Q,R,S,T,U".indexOf(pil_kombinasi)>-1)){ // *Kombinasi ganjil
											ok = new Integer(1);
										}else*/ if (pil_kombinasi.equalsIgnoreCase(produk.kombinasi[j])){
											ok = new Integer(1);
										}else{
											ok = new Integer(0);
										}
									}
								}
								if (ok.intValue() == 0)
								{
									hasil_kombinasi="Kombinasi premi tersebut tidak boleh untuk plan tersebut.";
									edit.getDatausulan().setTotal_premi_kombinasi(new Double(0));
									err.rejectValue("datausulan.kombinasi", "", "HALAMAN DATA USULAN : "+hasil_kombinasi);
								}									
								
								//ERBE Package produk Pro100 hanya bisa ambil kombinasi F (50% PP + 50% TB)
								if(produk.ii_bisnis_id==217 && produk.ii_bisnis_no==2 && !pil_kombinasi.equalsIgnoreCase("F") ){
									err.rejectValue("datausulan.mspo_installment", "", "HALAMAN DATA USULAN : Untuk Produk Smile Link Pro 100 ERBE PACKAGE, harap mengisi Kombinasi Premi 50% PP - 50% PTB.");
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
									
									edit.getDatausulan().setMspr_premium(premi_pokok);
									edit.getInvestasiutama().getDaftartopup().setPremi_berkala(premi_tp);
									edit.getInvestasiutama().getDaftartopup().setPil_berkala(1);
									total_premi_tu = premi_tp;
									premi = premi_pokok;										
									
									Double total_seluruhnya = new Double(premi_pokok.doubleValue()+ premi_tp.doubleValue());
									edit.getInvestasiutama().setTotal_premi_sementara(total_seluruhnya);
								
							//}
						}
					}
				}else{
					if (cara_premi.intValue() == 1)
					{
						err.rejectValue("datausulan.cara_premi","","HALAMAN DATA USULAN : Produk ini bukan produk investasi, silahkan pilih cara premi yang hanya mengisi premi pokok .");
					}
					edit.getDatausulan().setTotal_premi_kombinasi(new Double(0));
					edit.getInvestasiutama().setTotal_premi_sementara(new Double(0));
					edit.getDatausulan().setKombinasi(null);
				}
				
				produk.ii_pmode=pmode_id.intValue();	
				
				//up
				if (hasil_up.trim().length()==0)
				{
											
					if (produk.flag_uppremi==0 && produk.flag_default_up==0)
					{
						hasil_up="";
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
						
						if(edit.getDatausulan().getLsbs_id()==212 && (edit.getDatausulan().getLsdbs_number()==9 || edit.getDatausulan().getLsdbs_number()==10 || edit.getDatausulan().getLsdbs_number()==14) ){ // add lsdbs_number = 14 Nana add smile proteksi 212-14 helpdesk 147672
							hasil_premi=produk.of_alert_min_premi(premi.doubleValue());
						}
						
						if (hasil_premi.trim().length()!=0 && sp != 1)
						{
							err.rejectValue("datausulan.mspr_premium","","HALAMAN DATA USULAN :" +hasil_premi);
						}else{
							hasil_premi=produk.of_alert_max_premi(premi.doubleValue(), up.doubleValue());
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
				
								
				if (cek_pmode.booleanValue()==false){
					hasil_cek_pmode="Cara bayar tersebut tidak bisa untuk plan ini.";
					err.rejectValue("datausulan.lscb_id","","HALAMAN DATA USULAN :" +hasil_cek_pmode);
				}

				if (hasil_cek_pmode.trim().length()==0){
					produk.of_set_pmode(pmode_id.intValue());
					if(produk.ii_bisnis_id==212){
						pay_period = edit.getDatausulan().getMspr_ins_period();
					}else{
						pay_period = edit.getDatausulan().getMspo_pay_period();
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
						
					}
					
					
					double fltpersen=0;
					edit.getDatausulan().setFlag_uppremi(produk.flag_uppremi);
					//lufi--
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
										err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : " +hasil_up);
									}
								}
								
								hasil_up = produk.cek_max_up(li_umur_ttg,kode_produk,premi,up,new Double(fltpersen),pmode_id,kurs);
								if (hasil_up.trim().length()!=0 && sp != 1){
									err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : " +hasil_up);
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
							produk.of_set_premi(premi.doubleValue());
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
							err.rejectValue("datausulan.mspo_installment", "", "Cuti Premi maksimal sama dengan masa pembayaran.");
						}else{
							if (platinum.intValue()<2 && !(produk.ii_bisnis_no >= 5)){
								err.rejectValue("datausulan.mspo_installment", "", "Cuti Premi minimal 2.");
							}
						}
					}
				}else{
					if (produk.flag_cuti_premi == 1){
						if (platinum==null){
							edit.getDatausulan().setMspo_installment(null);
						}else{
							
								if (pmode_id.intValue() !=0){
									// *(12/07/2012) Validasi maximum cuti premi dibuka
									if (platinum.intValue()>30){
//										err.rejectValue("datausulan.mspo_installment","","Cuti Premi maximal 30.");
									}else{
										if (platinum.intValue()<1){
											err.rejectValue("datausulan.mspo_installment","","Cuti Premi minimal 1.");
										}
									}
								}else{
									edit.getDatausulan().setMspo_installment(new Integer(1));
								}
						}
					}else{
						edit.getDatausulan().setMspo_installment(null);
					}					
				}
			
			if(produk.ii_bisnis_id==217 && produk.ii_bisnis_no==2){
				edit.getDatausulan().setMspo_installment(5); //set cuti premi 5
			}
			
			//randy - Magna Link/Syariah & (Smile Link 88/Syariah, Smile Link 99/Syariah, Smile Bridge Link/Syariah), Eduvest, Medivest wajib isi cuti premi (97840)
			if(produk.ii_bisnis_id!=212){
				if(Common.isEmpty(edit.getDatausulan().getMspo_installment())){
					err.rejectValue("datausulan.mspo_installment", "", "HALAMAN DATA USULAN : Harap Mengisi Cuti Premi.");
				}
			}
		
			//Ridhaal - Validasi Cuti Premi Setelah tahun ke
			if(!Common.isEmpty(edit.getDatausulan().getMspo_installment())){
				if(edit.getDatausulan().getMspo_installment() > edit.getDatausulan().getMspo_pay_period() ){
					err.rejectValue("datausulan.mspo_installment", "", "HALAMAN DATA USULAN : Cuti Premi tidak boleh lebih besar dari pada masa pembayaran");
				}
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
			
			Double total_premi_rider=new Double(0);
			Integer jumlah_r =new Integer(dtrd.size());
			if (jumlah_r==null){jumlah_r=new Integer(0);}
			
			Integer jmlrider=jumlah_r;
			edit.getDatausulan().setJmlrider(new Integer(jmlrider.intValue() + 1));
			
			Integer tgl_awal_rider = new Integer(0);
			Integer bln_awal_rider = new Integer(0);
			Integer thn_awal_rider = new Integer(0);
			String tanggal_awal_rider =null;
			Integer[] kd_rider_i;
			String[] nama_rider_i;
			String[] rider_i;
			Double[] sum_i;
			Double[] rate_i;
			Integer[] kode_rider_i;
			Double[] premi_rider_i;
			Double[] up_pa_rider_i;
			Double[] up_pb_rider_i;
			Double[] up_pc_rider_i;
			Double[] up_pd_rider_i;
			Double[] up_pm_rider_i;
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
			Date[] end_date_rider;
			Date[] beg_date_rider;
			Date[] end_pay_rider;
			Double[] rate;
			Integer[] percent;
			Integer[] ins_rider;
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
			Integer anak = 0;
			Integer hamil = 0;
			
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
			end_date_rider = new Date[jmlrider.intValue()+1];
			beg_date_rider= new Date[jmlrider.intValue()+1];
			end_pay_rider= new Date[jmlrider.intValue()+1];
			rate= new Double[jmlrider.intValue()+1];
			percent=new Integer[jmlrider.intValue()+1];
			ins_rider= new Integer[jmlrider.intValue()+1];
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
			produk.riderInclude(number_produk.intValue());

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
			
//			(deddy)- REQ ASRI : JIKA USIA _TT =50 tahun, dan (mengambil eka sehat stand alone atau (produk link dan ada rider eka sehat) maka diberikan warning dan set harus pemeriksaan kesehatanl )
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
				
			//Ridhaal - paket SMiLe Link Pro100 ERBE PACKAGE
			if(produk.ii_bisnis_id==217 && produk.ii_bisnis_no==2 && edit.getDatausulan().getFlag_paket()==0){
				err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Untuk SMiLe Link Pro 100 ERBE PACKAGE harus memilih salah satu paket yang telah disediakan");
			}	
			
			//Lufi--  Validasi Paket SMiLe  Education
			if( ((products.unitLinkNew(Integer.toString(produk.ii_bisnis_id)))) && edit.getDatausulan().getFlag_paket()!=0 && produk.ii_bisnis_id!=120){
				//ERBE PACKAGE PRO 100
				if(produk.ii_bisnis_id==217 && produk.ii_bisnis_no==2 && ("39,40".indexOf(flag_paket_2digit)>-1)){
					if(dtrd.size()==0) err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Untuk produk paket ERBE, harus memilih rider (Term Rider dan SMiLe Critical Illness (new)) sesuai dengan paket yang dipilih");
					// close for erbe requirement
					//if(dtrd.size()!=2) err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Untuk produk paket ERBE, harus memilih rider Term Rider dan SMiLe Critical Illness (new) sesuai dengan paket yang dipilih");
					  
					
					
					for (int x = 0; x < dtrd.size(); x++){						
						Datarider rd1 = (Datarider)dtrd.get(x);
						kode_rider[x] = rd1.getLsbs_id();
						number_rider[x] = rd1.getLsdbs_number();
						
						if((kode_rider[x] == 818 && number_rider[x] == 2)){
							jumlah_rider_paket = jumlah_rider_paket + 1;
						}
							
						if((kode_rider[x] == 813 && number_rider[x] == 8)){
							jumlah_rider_paket = jumlah_rider_paket + 1;
						}
						
						
					}
					/** Erbe Patar Timotius 2017 12 2018 **/
					//	if(jumlah_rider_paket != 2) err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih (Term Rider dan SMiLe Critical Illness (new)");
					//  patar timotius erbe jg
					
					if(produk.ii_age>=17){
						if(jumlah_rider_paket != 2) err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih (Term Rider dan SMiLe Critical Illness (new)");
					}else{
						
						if(dtrd.size() == 1){
							Datarider rd1 = (Datarider)dtrd.get(0);
							if(!(rd1.getLsbs_id() == 818 && rd1.getLsdbs_number() == 2)){
								 err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Untuk usia < 17 tahun mohon memilih Term Rider (new)");
							}
						}else{
							 err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Untuk usia < 17 tahun mohon memilih Term Rider (new)");
										
						}
						
					/*	
						if(jumlah_rider_paket <= 0 && jumlah_rider_paket > 1) err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih (Term Rider dan SMiLe Critical Illness (new)");
						if(dtrd.size()==1){
							Datarider rd1 = (Datarider)dtrd.get(0);
							if(!(rd1.getLsbs_id() == 818 && rd1.getLsdbs_number() == 2)){
								 err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Pilihan rider tidak sesuai dengan paket yang dipilih (Term Rider dan SMiLe Critical Illness (new)");
							}
						}*/
					};
					
					
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
				
				//untuk paket ERBE diset Term Rider unit 2 dan CI 100 persen up -- Ridhaal
				if(produk.ii_bisnis_id==217 && produk.ii_bisnis_no==2 && edit.getDatausulan().getFlag_paket()!=0){
					if(kode_rider[k].intValue()==818){
						rd.setMspr_unit(1);
						rd.setMspr_tsi(up);
					}
					if(kode_rider[k].intValue()==813)rd.setPersenUp(6);
				}
				
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
							
							jumlah_rider=jmlrider;
	
							//cek rider boleh dipilih atau ga
							for ( int i=0 ; i< produk.indeks_rider_list; i++ )
							{
								if (kode_rider[k].intValue()==produk.ii_rider[i])
								{
									type_rider=new Boolean(true);
								}
							}
							
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
								Integer kod_x = 0;
								Integer kod_x2 = kode_rider[k].intValue();
								Integer numb_x = 0;
								Integer numb_x2 = number_rider[k].intValue();
								Integer JenisRiderPAK_x =0;

								li_umur_ttg =  umr.umur(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);
							
							
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
							  }
							}
																
							//cari rate rider , premi rider, persentase rider
								rate[k] = produk1.of_get_rate1(klases[k],produk.flag_jenis_plan,number_rider[k],li_umur_ttg,li_umur_pp);	
								
								Double up_sementara= up;
								
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
								
								if(produk1.ii_bisnis_id == 813 || produk1.ii_bisnis_id == 830){
									if(factor_x[k] != 0){
										premi_rider[k] = produk1.hit_premi_rider_with_factor(rate[k], up_sementara, produk1.idec_pct_list[pmode_id], premi, rd.getPersenUp());
									}else{
										premi_rider[k] = premi_rider[k];
									}
								// Andhika (09/12/2013) - Premi Payor diganti dari premi pokok menjadi total premi
								}else{
									premi_rider[k] = produk1.hit_premi_rider(rate[k], up_sementara,produk1.idec_pct_list[pmode_id],premi);
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
								
								int flag_sementara = produk.flag_jenis_plan;
									
									// *CEK UMUR
									Integer ageEkaHCP = age;
									
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
								
								
								List ptx_mix = edit.getDatausulan().getDaftarplus_mix();
								if(tahun_akhir_polis1_tertanggung_I==null){
									tahun_akhir_polis1_tertanggung_I = 0;
								}
								if(jenis[k] == 0){ // *Tertanggung utama
									tahun_akhir_polis1_tertanggung_I = tahun_akhir_polis1;
									edit.getDatausulan().setTgl_tertanggung_I(tahun_akhir_polis1_tertanggung_I);
									if(kode_rider[k]==838 || kode_rider[k]==840){
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
//									if(produk1.ii_bisnis_id!=820 && produk1.ii_bisnis_id!=823 && produk1.ii_bisnis_id!=820 && produk1.ii_bisnis_id!=825 && produk1.ii_bisnis_id!=826 ||
//								       produk1.ii_bisnis_id!=819 && produk1.ii_bisnis_id!=832 && produk1.ii_bisnis_id!=833){
									if("819,820,823,825,826,832,833,838,839,840,841,842,843".indexOf(kode_rider[k].toString())<=-1 ){
										edit.getDatausulan().setTgl_tertanggung_I(tahun_akhir_polis1);
									}
									//daltest1
									if ("840".indexOf(kode_rider[k].toString())>=-1){
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
																		
									if((tahun_akhir_polis1>edit.getDatausulan().getTgl_tertanggung_I()) && (kode_rider[k]!=839 || kode_rider[k]!=840 || kode_rider[k]!=841 || kode_rider[k]!=842 || kode_rider[k]!=843 )){
										tahun_akhir_polis1=edit.getDatausulan().getTgl_tertanggung_I();
										if(bulan_akhir_polis1>bulan){
											bulan_akhir_polis1= bulan;
										}
										tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis1.intValue()),2)+"/"+Integer.toString(tahun_akhir_polis1.intValue());
									}
									
									if((tahun_akhir_bayar1>edit.getDatausulan().getTgl_tertanggung_I()) && (kode_rider[k]!=839 || kode_rider[k]!=840 || kode_rider[k]!=841 || kode_rider[k]!=842 || kode_rider[k]!=843)){
										tahun_akhir_bayar1=edit.getDatausulan().getTgl_tertanggung_I();
										if(bulan_akhir_bayar1>bulan-1){
											bulan_akhir_bayar1= bulan-1;
										}
										tgl_akhir_bayar1=Integer.toString(tanggal_akhir_bayar1.intValue())+"/"+Integer.toString(bulan_akhir_bayar1.intValue())+"/"+Integer.toString(tahun_akhir_bayar1.intValue());
										tanggal_1_1=FormatString.rpad("0",Integer.toString(tanggal_akhir_bayar1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_bayar1.intValue()),2)+"/"+Integer.toString(tahun_akhir_bayar1.intValue())	;	
									}
								}
								
								if((kode_rider[k]==839) && jenis[k]!=0){
									
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
								    produk1.ii_bisnis_id==819 || produk1.ii_bisnis_id==838 || produk1.ii_bisnis_id==839 ||produk1.ii_bisnis_id==840 ||  produk1.ii_bisnis_id==841 ||  produk1.ii_bisnis_id==842 || produk1.ii_bisnis_id==843) && (produk1.ii_bisnis_id!=829)){
									
										akhir_polis1 = tahun_akhir_polis1;
										akhir_bayar1 = tahun_akhir_bayar1;
										edit.getDatausulan().setTahun_polis(akhir_polis1);
										edit.getDatausulan().setTahun_bayar(akhir_bayar1);
										edit.getDatausulan().setAkhir_polis1(tgl_akhir_polis1);// *endDate
										edit.getDatausulan().setAkhir_bayar1(tanggal_1_1); // *endPay
								}
								
								if((produk1.ii_bisnis_id==820 || produk1.ii_bisnis_id==823 || produk1.ii_bisnis_id==825 ||
								    produk1.ii_bisnis_id==826 || produk1.ii_bisnis_id==819 || produk1.ii_bisnis_id==832 ||
								    produk1.ii_bisnis_id==833 || produk1.ii_bisnis_id==838 || produk1.ii_bisnis_id==840) && produk1.ii_bisnis_id!=829){
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
								if((produk1.ii_bisnis_id==838 || produk1.ii_bisnis_id==840) && "1,2,3,4".indexOf(number_rider[k].toString())>=0 ){
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
									//daltest2
										if(produk.ii_bisnis_id==183 || produk.ii_bisnis_id==189 || produk.ii_bisnis_id==193 || produk.ii_bisnis_id==204 || produk.ii_bisnis_id==214){
											edit.getDatausulan().setMasa_tanggung_I(mp);
										}else if("819,820,823,825,826,832,833,838".indexOf(kode_rider[k].toString())<=-1 ){
										edit.getDatausulan().setMasa_tanggung_I(mp);
									}
								}
								
								// VIP HOSPITAL PLAN
								if(produk.ii_bisnis_id==195 && (produk.ii_bisnis_no>=49 && produk.ii_bisnis_no<=60)){
									if("826".indexOf(kode_rider[k].toString())>=0){
										edit.getDatausulan().setMasa_tanggung_I(mp);
									}
								}
								
								if((produk1.ii_bisnis_id==820)||(produk1.ii_bisnis_id==823)||
								   (produk1.ii_bisnis_id==825)||(produk1.ii_bisnis_id==826)||
								   (produk1.ii_bisnis_id==832)||(produk1.ii_bisnis_id==833)||
								   (produk1.ii_bisnis_id==819) ||(produk1.ii_bisnis_id==838) || (produk1.ii_bisnis_id==840) ){
										mp_main = new Integer(produk1.li_insured);
										edit.getDatausulan().setMp_main(mp_main);
								}
								if((produk1.ii_bisnis_id==839 || produk1.ii_bisnis_id==841 || produk1.ii_bisnis_id==842 || produk1.ii_bisnis_id==843 ) && jenis[k] != 0){
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
										produk1.ii_bisnis_id==832 || produk1.ii_bisnis_id==833 || produk1.ii_bisnis_id==838 || produk1.ii_bisnis_id==840
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
								 if (produk.flag_platinumlink==1 && !(kode_produk.intValue()==134 && number_produk>=5))
								 {
									 rd.setMspr_ins_period(new Integer(produk.li_insured));
								 }else{
									 // *Set masa pertanggungan
									 if((produk.ii_bisnis_id==183 || produk.ii_bisnis_id==189 || produk.ii_bisnis_id==193 || produk.ii_bisnis_id==204) && (produk1.ii_bisnis_id==820 || produk1.ii_bisnis_id==823 || produk1.ii_bisnis_id==825 ||produk1.ii_bisnis_id==826 || produk1.ii_bisnis_id==829 || produk1.ii_bisnis_id==832 || produk1.ii_bisnis_id==833 || produk1.ii_bisnis_id==838 || produk1.ii_bisnis_id==840)){
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
									    
										//req Titis untuk Hubungan PP dan TT
										if("815,817,828".indexOf(Integer.toString(produk1.ii_bisnis_id)) >-1 && hubungan == 1){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Rider PAYOR hubungan pemagang polis dengan tertanggung tidak boleh diri sendiri");
										}else if("814,816,827".indexOf(Integer.toString(produk1.ii_bisnis_id)) >-1  && hubungan != 1){
											err.rejectValue("datausulan.daftaRider["+k+"].plan_rider","","HALAMAN DATA USULAN : Untuk Rider WAIVER hubungan pemagang polis dengan tertanggung harus diri sendiri");
										}
										
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
//										edit.getPowersave().setMpr_cara_bayar_rider(2);
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
									if (produk.flag_platinumlink == 0 || (produk.ii_bisnis_id == 134 && produk.ii_bisnis_no >= 5))
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
											Double mbu_jumlah=new Double(produk1.mbu_jumlah);
											if(produk.ii_bisnis_id!=166){
												mbu_jumlah = premi_rider[k];
											}
											BigDecimal mbu1 = new BigDecimal(mbu_jumlah.doubleValue());
											mbu1=mbu1.setScale(0,BigDecimal.ROUND_HALF_UP);
											mbu_jumlah=new Double(mbu1.doubleValue());
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
											rd.setMspr_rate(mbu_jumlah);
									}
								}else{
//									 khusus platinum link tidak ada biaya
									rd.setMspr_premium(premi_rider[k]);
									// *FIXIN randy
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
												if(edit.getDatausulan().getLsbs_id()==208 && produk1.ii_bisnis_id == 836){
//													premi_rider[k] = rate[k];
													int umur = pps.getUmur();
													int relasi = pps.getLsre_id();
													if (relasi != 39){
														if (umur<18 || umur>40)
														err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN : Untuk Calon ibu, Usia masuk minimal 18 tahun dan maksimum 40 tahun.");
													}
												}
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
					premi_rider_i = new Double[jmlh_rider.intValue()+1];
					rider_i = new String[jmlh_rider.intValue()+1];
					nama_rider_i = new String[jmlh_rider.intValue()+1];
					up_pa_rider_i = new Double[jmlh_rider.intValue()+1];
					up_pb_rider_i = new Double[jmlh_rider.intValue()+1];	
					up_pc_rider_i = new Double[jmlh_rider.intValue()+1];
					up_pd_rider_i = new Double[jmlh_rider.intValue()+1];	
					up_pm_rider_i = new Double[jmlh_rider.intValue()+1];
					if (jumlah_r.intValue()>0)
					{

						for (int m=0;m<jumlah_r.intValue();m++)
						{

							kode_rider_i[m]=kode_rider[m];

							kd_rider_i[m] = kd_rider[m];
						}
					}
					int nomor_produk_include=0;
					if (produk.indeks_rider_include>1)
					{
						Integer index = jumlah_r;
						List data_rider_include = new ArrayList();

						for (int k=0 ; k <produk.indeks_rider_include; k++)
						{
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
									if (produk2.ldt_epay!=null)
									{
										tanggal_akhir_bayar2=produk2.ldt_epay.getTime().getDate();
										bulan_akhir_bayar2=produk2.ldt_epay.getTime().getMonth()+1;
										tahun_akhir_bayar2=produk2.ldt_epay.getTime().getYear()+1900;
										tgl_akhir_bayar2=FormatString.rpad("0",Integer.toString(tanggal_akhir_bayar2),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_bayar2),2)+"/"+Integer.toString(tahun_akhir_bayar2);
									}else{
										tgl_akhir_bayar2=null;					
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
								}
							}
						}
					}
			}
		
		List daftaHcp = edit.getDatausulan().getDaftahcp();
		Integer jumlah_hcpbasic = new Integer(0);
		if (jmlrider.intValue() > 0){
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
					
					if ((flag_hcp.intValue() == 0) && ((rider1.getLsdbs_number().intValue() >=21 && rider1.getLsdbs_number().intValue() <= 140) || (rider1.getLsdbs_number().intValue() >=161 && rider1.getLsdbs_number().intValue() <= 280) || (rider1.getLsdbs_number().intValue() >=301 && rider1.getLsdbs_number().intValue() <=380) || (rider1.getLsdbs_number().intValue() >=391 && rider1.getLsdbs_number().intValue() <=430)))
					{
						hasil_rider="Rider SMiLe HOSPITAL PROTECTION FAMILY ke "+ (i+1)+" tidak bisa dipilih kalau tidak mengambil SMiLe HOSPITAL PROTECTION FAMILY (TERTANGGUNG I). ";
						err.rejectValue("datausulan.daftaRider["+i+"].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);
					}
				}
			}
			if (jumlah_hcpbasic.intValue() > 1){
				hasil_rider="Rider SMiLe HOSPITAL PROTECTION FAMILY (TERTANGGUNG I) tidak boleh lebih dari satu. ";
				err.rejectValue("datausulan.daftaRider[0].plan_rider","","HALAMAN DATA USULAN :" +hasil_rider);

			}

			//list rider termasuk hcpf
			if (dtrd == null)
			{
				index_dtrd = new Integer(0);
			}else{
				index_dtrd = new Integer(dtrd.size());
			}
	}
		
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
	try {
			
				
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
		
			validateddu2New(cmd, err,sp);
			
		} catch (ParseException e) {
			logger.error("ERROR :", e);
		}
	}	
	
	//-------------------------data usulan v.2----------------------------------------
	public void validateddu2New(Object cmd, Errors err, Integer sp)  throws ServletException,IOException,Exception
	{

		logger.debug("EditBacValidator : validate page validateddu2New");
		Cmdeditbac edit= (Cmdeditbac) cmd;
		String hasil_kombinasi;
		String lsdbs_3digit = FormatString.rpad("0", edit.getDatausulan().getLsdbs_number().toString(), 3);
		String flag_paket_2digit = FormatString.rpad("0", edit.getDatausulan().getFlag_paket().toString(), 2);
		int pmode_id = edit.getDatausulan().getLscb_id();
		
		//logger.info(edit.getPowersave().getMsl_employee());
		if(!Common.isEmpty(edit.getPowersave().getMsl_employee())){  			
			if(edit.getPowersave().getMsl_employee()==1){
				logger.info(edit.getPowersave().getMsl_employee());
				edit.getPowersave().setMps_employee(1);
				logger.info(edit.getPowersave().getMsl_employee());
			}
		}
		
		if(edit.getTertanggung().getMste_dth()==null){
			edit.getTertanggung().setMste_dth(0);
		}
		
		//Ridhaal- SMiLe Link PRO 100 ERBE PACKAGE - SET UP and PREMI
		if(edit.getDatausulan().getLsbs_id().intValue()==217 && edit.getDatausulan().getLsdbs_number()==2){
			if("39,40".indexOf(flag_paket_2digit)>-1){
				
				Double premipokok = 0.0;
				Double premiberkala = 0.0;
				edit.getDatausulan().setMste_flag_cc(0); // set tunai untuk bentuk pembayaran ERBE
				
				if( edit.getDatausulan().getMspr_premium() == null){
					premipokok = 0.0;
				}else{
					premipokok = edit.getDatausulan().getMspr_premium().doubleValue();
				}
				
				if(edit.getInvestasiutama().getDaftartopup().getPremi_berkala() == null){
					premiberkala = 0.0;
				}else{
					premiberkala = edit.getInvestasiutama().getDaftartopup().getPremi_berkala().doubleValue();
				}
								
				Double totalPremiPPPB = premipokok + premiberkala ;
				
				if(edit.getDatausulan().getFlag_paket().intValue()==39){
					if(edit.getDatausulan().getMspr_tsi().doubleValue()!=50000000){
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Uang Pertanggungan Untuk SMiLe Link PRO100 PACKAGE ERBE SMILE LINK 3 adalah Rp 50.000.000,00" );
					}
				
					//Double totalPremiPPPB = edit.getDatausulan().getMspr_premium().doubleValue() + edit.getInvestasiutama().getDaftartopup().getPremi_berkala().doubleValue();
					
					if(pmode_id == 6 && premipokok !=150000){
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Premi Pokok harus Rp 150.000 dengan Total Premi Kombinasi (50%PP + 50%PTB) Untuk SMiLe Link PRO100 PACKAGE ERBE SMILE LINK 3 (Bulanan) adalah Rp 300.000,00" );
					}else if(pmode_id == 1 && premipokok !=450000){
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Premi Pokok harus Rp 450.000 dengan Total Premi Kombinasi (50%PP + 50%PTB) Untuk SMiLe Link PRO100 PACKAGE ERBE SMILE LINK 3 (Triwulan) adalah Rp 900.000,00" );
					}else if(pmode_id == 2 && premipokok !=900000){
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Premi Pokok harus Rp 900.000 dengan Total Premi Kombinasi (50%PP + 50%PTB) Untuk SMiLe Link PRO100 PACKAGE ERBE SMILE LINK 3 (Semesteran) adalah Rp 1.800.000,00" );
					}else if(pmode_id == 3 && premipokok !=1800000){
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Premi Pokok harus Rp 1.800.000 dengan Total Premi Kombinasi (50%PP + 50%PTB) Untuk SMiLe Link PRO100 PACKAGE ERBE SMILE LINK 3 (Tahunan) adalah Rp 3.600.000,00" );
					}
					
					if(pmode_id == 6 && totalPremiPPPB !=300000){
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Total Premi Kombinasi (50%PP + 50%PTB) Untuk SMiLe Link PRO100 PACKAGE ERBE SMILE LINK 3 (Bulanan) adalah Rp 300.000,00" );
					}else if(pmode_id == 1 && totalPremiPPPB !=900000){
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Total Premi Kombinasi (50%PP + 50%PTB) Untuk SMiLe Link PRO100 PACKAGE ERBE SMILE LINK 3 (Triwulan) adalah Rp 900.000,00" );
					}else if(pmode_id == 2 && totalPremiPPPB !=1800000){
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Total Premi Kombinasi (50%PP + 50%PTB) Untuk SMiLe Link PRO100 PACKAGE ERBE SMILE LINK 3 (Semesteran) adalah Rp 1.800.000,00" );
					}else if(pmode_id == 3 && totalPremiPPPB !=3600000){
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Total Premi Kombinasi (50%PP + 50%PTB) Untuk SMiLe Link PRO100 PACKAGE ERBE SMILE LINK 3 (Tahunan) adalah Rp 3.600.000,00" );
					}
					
				}else if(edit.getDatausulan().getFlag_paket().intValue()==40){
					if(edit.getDatausulan().getMspr_tsi().doubleValue()!=150000000){
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Uang Pertanggungan Untuk SMiLe Link PRO100 PACKAGE ERBE SMILE LINK 12 adalah Rp 150.000.000,00" );
					}
					
					//Double totalPremiPPPB = edit.getDatausulan().getMspr_premium().doubleValue() + edit.getInvestasiutama().getDaftartopup().getPremi_berkala().doubleValue();
					
					if(pmode_id == 6 && premipokok !=500000){
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Premi Pokok harus Rp 500.000 dengan Total Premi Kombinasi (50%PP + 50%PTB) Untuk SMiLe Link PRO100 PACKAGE ERBE SMILE LINK 12 (Bulanan) adalah Rp 1.000.000,00" );
					}else if(pmode_id == 1 && premipokok !=1500000){
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Premi Pokok harus Rp 1.500.000 dengan Total Premi Kombinasi (50%PP + 50%PTB) Untuk SMiLe Link PRO100 PACKAGE ERBE SMILE LINK 12 (Triwulan) adalah Rp 3.000.000,00" );
					}else if(pmode_id == 2 && premipokok !=3000000){
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Premi Pokok harus Rp 3.000.000 dengan Total Premi Kombinasi (50%PP + 50%PTB) Untuk SMiLe Link PRO100 PACKAGE ERBE SMILE LINK 12 (Semesteran) adalah Rp 6.000.000,00" );
					}else if(pmode_id == 3 && premipokok !=6000000){
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Premi Pokok harus Rp 6.000.000 dengan Total Premi Kombinasi (50%PP + 50%PTB) Untuk SMiLe Link PRO100 PACKAGE ERBE SMILE LINK 12 (Tahunan) adalah Rp 12.000.000,00" );
					}
					
					if(pmode_id == 6 && totalPremiPPPB !=1000000){
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Total Premi Kombinasi (50%PP + 50%PTB) Untuk SMiLe Link PRO100 PACKAGE ERBE SMILE LINK 12 (Bulanan) adalah Rp 1.000.000,00" );
					}else if(pmode_id == 1 && totalPremiPPPB !=3000000){
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Total Premi Kombinasi (50%PP + 50%PTB) Untuk SMiLe Link PRO100 PACKAGE ERBE SMILE LINK 12 (Triwulan) adalah Rp 3.000.000,00" );
					}else if(pmode_id == 2 && totalPremiPPPB !=6000000){
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Total Premi Kombinasi (50%PP + 50%PTB) Untuk SMiLe Link PRO100 PACKAGE ERBE SMILE LINK 12 (Semesteran) adalah Rp 6.000.000,00" );
					}else if(pmode_id == 3 && totalPremiPPPB !=12000000){
						err.rejectValue("datausulan.mspr_tsi","","HALAMAN DATA USULAN : Total Premi Kombinasi (50%PP + 50%PTB) Untuk SMiLe Link PRO100 PACKAGE ERBE SMILE LINK 12 (Tahunan) adalah Rp 12.000.000,00" );
					}
				}
			}

		}
		
		//untuk cara bayar bulanan, harus autodebet/kartu kredit	
		if(edit.getDatausulan().getLscb_id().intValue()==6){
			if (edit.getDatausulan().getLsbs_id()==212 && (edit.getDatausulan().getLsdbs_number()==9 || edit.getDatausulan().getLsdbs_number()==14) ){  //Chandra A - 20180427 // add lsdbs_number = 14 (Nana) add smile proteksi 212-14 helpdesk 147672
				if((edit.getDatausulan().getMste_flag_cc()==null || (edit.getDatausulan().getMste_flag_cc()<1 || edit.getDatausulan().getMste_flag_cc()>2)) && sp!=1){
					err.rejectValue("datausulan.mste_flag_cc","","HALAMAN DATA USULAN : Untuk cara Bayar Bulanan, bentuk pembayaran premi harus AUTODEBET(KARTU KREDIT/TABUNGAN).");
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
					Integer jumlah_dobel = (Integer) this.elionsManager.cek_polis_dobel(nama, tgl, lsbs_id, lsdbs_number, premi, null);
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
		
		//ERBE tidak perlu set no VA
		if(edit.getDatausulan().getLsbs_id()==217 && edit.getDatausulan().getLsdbs_number()==2){				
			edit.getTertanggung().setMste_no_vacc("");
		};
	}
		
	
}