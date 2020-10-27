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

public class Editduvalidatorspajnew3 implements Validator{
	protected final Log logger = LogFactory.getLog(getClass());
	
	private ElionsManager elionsManager;
	private UwManager uwManager;
	private BacManager bacManager;
	private DateFormat defaultDateFormat;
	private Products products;

	Editduvalidatorspajnew editValidatehead = new Editduvalidatorspajnew();
	
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
	
	public void validasi1(Cmdeditbac edit, Object cmd, Errors err, String status, String hasil_rider, Integer kode_flag, Double premi, Integer flag_rider_hcp, Integer flag_powersave, Integer flag_rider, Integer pmode_id, Integer flag_as, String kurs, Double up, Integer flag_hcp) throws ServletException,IOException,Exception{
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
	}

	public void cekJumlahRider(List dtrd, Errors err, Integer jmlrider, Integer jumlah_hcpbasic, String hasil_rider, Integer flag_hcp, Integer number_utama) throws ServletException,IOException,Exception{
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
	}

	public void ifNamaRider(Cmdeditbac edit, Datarider rd1, List data_rider_include, String[] nama_rider_i, String[] rider_i, Double[] premi_rider_i, Double[] rate_i, Double[] up_pm_rider_i, Double[] up_pd_rider_i, Double[] sum_i, Double[] up_pc_rider_i, Double[] up_pb_rider_i, Double[] up_pa_rider_i, Integer flag_platinum, Integer index, n_prod produk, Integer number_produk, Integer[] kode_rider_i, Integer li_umur_ttg, Integer li_umur_pp, Double up, Double premi, Integer pmode_id, Integer nomor_produk_include, Integer pay_period, String kurs, Date tgl_beg_date, Integer tahun1, Integer bulan1, Integer tanggal1, Integer tahun, Integer bulan, Integer tanggal, Integer ins_period, Integer age) throws ServletException,IOException,Exception{
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
	
	public void updateEndDatePayDate(n_prod produk1, int mp_rider1, Integer tahun, Integer bulan, Integer tanggal){
		f_hit_umur umr = new f_hit_umur();
		
		Date temp_date = umr.f_add_months(tahun,bulan,tanggal,mp_rider1 * 12);
		produk1.ldt_edate.set(temp_date.getYear()+1900,temp_date.getMonth(),temp_date.getDate());
		produk1.ldt_edate.add(produk1.ldt_edate.DATE,-1);
		
		Date temp_date2 = umr.f_add_months(tahun,bulan,tanggal,( mp_rider1 * 12)-1 );
		produk1.ldt_epay.set(temp_date2.getYear()+1900,temp_date2.getMonth(),temp_date2.getDate());
	}
	
	//helpdesk [145625] tambah rider smile medical extra untuk produk agency
	public String cekUsiaKesehatan(Cmdeditbac edit, n_prod produk1, int indexTertanggung, int tanggungSatu, int hubungin, int tahun2, int bulan2, int tanggal2, int tahun1, int bulan1, int tanggal1, int lama_bayar1, int number_produk, int number_rider){
		String hasil_rider = "";

		int thn, bln, tgl;
		PesertaPlus ttg_tamb = (PesertaPlus)edit.getDatausulan().getDaftarplus().get(indexTertanggung);
		tgl = ttg_tamb.getTgl_lahir().getDate();
		bln = (ttg_tamb.getTgl_lahir().getMonth()) + 1;
		thn = (ttg_tamb.getTgl_lahir().getYear()) + 1900;
		f_hit_umur umr_ttg = new f_hit_umur();
		int umur_ttg_tamb =  umr_ttg.hari(thn, bln, tgl, tahun1, bulan1, tanggal1);
		
		return produk1.of_check_usia_kesehatan2(tanggungSatu, hubungin, umur_ttg_tamb, tahun2, bulan2, tanggal2, tahun1, bulan1, tanggal1, lama_bayar1, number_produk, number_rider);
	}
	
	public int cekSuamiIstriLebihDariSatu(int hubungan){
		int hubunganSuamiIstri = 0;
		
		if(hubungan == 5)
			hubunganSuamiIstri = 1;
		
		return hubunganSuamiIstri;
	}
	
	public void setEndDatePayDate(Cmdeditbac edit, n_prod produk1, int indexPeserta, Integer tahun1, Integer bulan1, Integer tanggal1, Integer pmode_id, Integer tahun, Integer bulan, Integer tanggal, Integer ins_period, int flag_sementara, Integer ageEkaHCP, Integer pay_period, int flag_cerdas_siswa, Integer li_umur_pp){
		PesertaPlus pp = (PesertaPlus)edit.getDatausulan().getDaftarplus().get(indexPeserta);
		produk1.wf_set_premi2(pp.getLsre_id(), pp.getFlag_jenis_peserta(), tahun1, bulan1, tanggal1, pmode_id, tahun, bulan, tanggal, ins_period, flag_sementara, ageEkaHCP, pay_period, flag_cerdas_siswa, li_umur_pp,produk1.ii_bisnis_id, produk1.ii_bisnis_no);
	}
}
