//n_prod_215 SIMAS PRIME LINK SYARIAH
package produk_asuransi;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.ekalife.utils.f_check_end_aktif;
import com.ekalife.utils.f_hit_umur;

/*
 * Created on Jul 27, 2016
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class n_prod_215 extends n_prod{
	f_hit_umur umr = new f_hit_umur();
	DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
	
	public n_prod_215()
	{
				
		ii_bisnis_id = 215;
		ii_contract_period = 100;
		ii_age_from = 1;
		ii_age_to = 70;

		indeks_is_forex = 1;
		is_forex = new String[indeks_is_forex];
		is_forex[0] = "01";
		
		indeks_ii_pmode_list = 2;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 0; //Sekaligus
		
		indeks_li_pmode = 2;
		li_pmode = new int[indeks_li_pmode];
		li_pmode[1] = 0;
		
		indeks_idec_pct_list = 1;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1; //Pmode 0
		
		idec_add_pct = idec_pct_list[0];

		ib_single_premium = true ;
		
		ii_end_from = 100;
	  	idec_min_up01 = 10000000;
	  	
		indeks_ii_lama_bayar = 3;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 1;
		ii_lama_bayar[2] = 1;
		ii_lbayar = ii_lama_bayar[0];
		
		
		indeks_idec_list_premi = 20;
		idec_list_premi = new double[indeks_idec_list_premi];
		for(int i=0; i<13; i++)
		{
			idec_list_premi[i] = 3000000 + ( 1000000 * (i - 1) );
		}
		
		flag_uppremi = 1;
		flag_uppremiopen = 1;
		li_pct_biaya = 0;
		
		indeks_kombinasi = 3;
		kombinasi = new String[indeks_kombinasi];
		kombinasi[0] = "A"; //PP 100%
		kombinasi[1] = "B"; //pp 90% - ptb 10%
		kombinasi[2] = "C"; //pp 80% - ptb 20%
		
		indeks_rider_list = 2;
		ii_rider = new int[indeks_rider_list];
		ii_rider[0] = 812;
		ii_rider[1] = 813;
		
		flag_account = 2;
		flag_rider = 1;												
		flag_excell80plus = 1;
		isInvestasi = true;
		isProductBancass = true;
		usia_nol = 1;
		kode_flag = 7;
	}
	
	public double of_get_fltpersen(Integer pmode_id)
	{
		double fltpersen = 1;
		return fltpersen;
	}
		
	public double of_get_min_up()
	{
		double ldec_1 = 15000000;
		return ldec_1;
	}
	
	public boolean of_check_premi(double ad_premi)
	{
		boolean hsl = false;
		if (ad_premi >= 10000000){
			hsl = true;
		}
		return hsl;
	}		
	
	public int of_get_conperiod(int number_bisnis)
	{
		li_cp = 0;
		li_cp = ii_contract_period-ii_age;
		return li_cp;		
	}	
	
	public int of_get_payperiod()
	{
		ii_pay_period = ii_lama_bayar[0];
		return ii_pay_period;
	}	
	
	public void of_set_pmode(int ai_pmode)
	{
		ii_pmode = ai_pmode;
		idec_add_pct = idec_pct_list[ii_pmode];
	}	  

	public double of_get_rate()
	{
		return idec_rate;	
	}

	public void of_set_begdate(int thn, int bln, int tgl)
	{
		int li_month = 0;

		if(ib_flag_end_age){
			ii_end_age = ii_age;
		}
		li_month = ( ii_end_from - ii_end_age ) * 12;

		adt_bdate.set(thn,bln-1,tgl);
		idt_beg_date.set(thn,bln-1,tgl);		
		idt_end_date.set(thn,bln-1,tgl);			
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		idt_end_date.add(idt_end_date.DAY_OF_MONTH , -1);
		
		f_check_end_aktif a = new f_check_end_aktif();
		a.end_aktif(idt_end_date.YEAR, idt_end_date.MONTH, idt_end_date.DAY_OF_MONTH, idt_beg_date.YEAR , idt_beg_date.MONTH, idt_beg_date.DAY_OF_MONTH);
		ii_contract_period = ii_end_from - ii_end_age;
	}

	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
		ii_bisnis_no_utama = ii_bisnis_no;
		if (ii_bisnis_no==2) isProductBancass = false;
	}
	
	public void of_set_premi(double adec_premi)
	{
		idec_premi_main = adec_premi;
		idec_premi = adec_premi;
		idec_rate = 1250;
		idec_up = adec_premi * idec_rate / 1000;
		if(idec_up < 15000000) idec_up = 15000000;
	}
	 	
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr = new f_hit_umur();
		int bln = umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
		int hari =umr.hari(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
//		int hari2 =umr.hari(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);
//		int hari3 =umr.hari1(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
//		int hari4 =umr.hari1(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);
		
		hsl = "";
		
		if (ii_bisnis_no == 1){ 
			ii_age_to = 80;
		}
		
		if (ii_bisnis_no == 2 || ii_bisnis_no == 3){
			if(hari <= 15){
				hsl = "Usia Masuk Plan ini minimum : 15 hari";
			}
			if(ii_age > ii_age_to){
				hsl = "Usia Masuk Plan ini maximum : " + ii_age_to;
			}
		}else{
			if(bln < 1){
				hsl = "Usia Masuk Plan ini minimum : " + ii_age_from;
			}
			if(ii_age > ii_age_to){
				hsl = "Usia Masuk Plan ini maximum : " + ii_age_to;
			}
		}

		
		if(ii_usia_pp > 85){
			hsl = "Usia Pemegang Polis maximum : 85 Tahun !!!";
		}else if(ii_usia_pp < 17){
			hsl = "Usia Pemegang Polis minimum : 17 Tahun !!!";
		}
		
		return hsl;		
	}
	
	public double f_get_bia_akui(int ar_lb, int ar_ke)
	{
		double ld_bia = 0;
		if(ar_lb>1){
			if(ar_ke==1){
				ld_bia = 0.7;	
			}else if(ar_ke==2){
				ld_bia = 0.2;
			}else if(ar_ke >=3 && ar_ke <=5){
				ld_bia = 0.05;
			}
		}
		return ld_bia;	
	}		 	

	public double cek_awal()
	{
		double hasil = 0;
		hasil = 27500;
		return hasil;
	}

	public double cek_rate(int li_bisnis,int li_pmode,int pperiod,int li_insper,int umur_tt , String kurs,int insperiod)
	{
		String err = "";
		Double result;
		double hasil = 0;
		try{
			result = query.selectNilai(1, 190, "01", 3, 80, 80, 1, umur_tt);
			if(result != null) hasil = result.doubleValue();
			
		} catch(Exception e) {
			err = e.toString();
		} 		
		return hasil; 
	}
	
	//Biaya akuisisi
	public void biaya1(double ld_bia_akui, double premi)
	{
		mbu_jumlah1 = 0;	
		mbu_persen1 = 0;	
	}
	
	//Biaya asuransi
	public void biaya2(double ld_bia_ass, double premi, int ldec_pct, double up)
	{
		mbu_jumlah2 = ((up/1000) * ld_bia_ass) * 0.1;
		int decimalPlace = 2;
		BigDecimal bd = new BigDecimal(mbu_jumlah2);
		bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		mbu_jumlah2 = bd.doubleValue();		
		mbu_persen2 = ld_bia_ass / 10;
		BigDecimal jm = new BigDecimal(mbu_persen2);
		jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);		
		mbu_persen2 = jm.doubleValue();
	}

	//Biaya administrasi
	public void biaya3(double ldec_awal)
	{
		mbu_jumlah3 = ldec_awal;
		Integer decimalPlace = 2;
		BigDecimal bd = new BigDecimal(mbu_jumlah3);
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		mbu_jumlah3 = bd.doubleValue();
	}	
	
	public String cek_min_up(Double premi, Double up, String kurs, Integer pmode_id)
	{
		String hasil_up = "";
		double min_up1 = 0;
		of_set_premi(premi.doubleValue());
		min_up1 = idec_up;
				
		if(up.doubleValue() < min_up1){
			hasil_up = "Up Minimum untuk plan ini "+ f.format(min_up1);
		}
		return hasil_up;
	}
	
	public void wf_set_rider(int tahun,int bulan,int tanggal, int cuti_premi,int li_umur_ttg, int li_umur_pp)
	{
		int li_month = 0;
		int li_bulan = 0;
		
		li_kali = 1000;
		
		if(ii_pmode == 0){
			li_insured = ( 60 - li_umur_ttg - 2);
			li_month = (( 60 - li_umur_ttg - 2) * 12);
		}
		
		ldt_edate.set(tahun,bulan-1,tanggal);
		ldt_epay.set(tahun,bulan-1,tanggal);		
		adt_bdate.set(tahun,bulan-1,tanggal);
		ldt_edate.add(adt_bdate.MONTH,li_month);
		ldt_epay.add(adt_bdate.MONTH,li_bulan);

		if (ii_pmode == 0){
			ldt_edate.set(tahun,bulan-1,tanggal);
			ldt_epay.set(tahun,bulan-1,tanggal);		
			adt_bdate.set(tahun,bulan-1,tanggal);
			
			ldt_edate.add(adt_bdate.YEAR,2);
			ldt_edate.add(ldt_edate.MONTH,li_month);
			
			ldt_epay.add(adt_bdate.YEAR,2);
			ldt_epay.add(ldt_epay.MONTH,li_month);
			
			adt_bdate.add(adt_bdate.YEAR,2);						
			
			if(adt_bdate.DAY_OF_MONTH == ldt_edate.DAY_OF_MONTH ){
				ldt_edate.add(ldt_edate.DAY_OF_MONTH , -1);
			}
			ldt_epay.add(ldt_edate.MONTH,-1);
		}
	}
	
	public String cek_rider_number(int nomor_produk, int kode_produk, int umurttg, int umurpp, double up, double premi, int pmode, int hub, String kurs, int pay_period)
	{
		String hsl = "";

		if( !((kode_produk == 812 && nomor_produk == 7) || (kode_produk == 813 && nomor_produk == 8)) ){
			hsl = " Untuk Produk ini hanya bisa menambahkan rider SMiLe Total Permanent Disability (new) dan SMiLe Critical Illness (new).";
		}
		return hsl;
	}
	
	public String of_alert_min_premi(double premi)
	{
		String hasil_min_premi = "";
		boolean cek_premi = of_check_premi(premi);
		if(cek_premi == false){
			hasil_min_premi = " Premi Minimum untuk Plan ini adalah Rp. 10.000.000,00";
		}
		return hasil_min_premi;
	}
	
	public String min_topup(Integer pmode_id, double premi , double premi_topup, String kurs, int jenis_topup)
	{
		String hsl = "";
		if(jenis_topup == 2){
			if (premi_topup < 1000000){
				hsl = "Minimum Top-Up Tunggal : Rp. 1.000.000,00";
			}
		}
		
		if(premi_topup > 1e10){
			hsl = "Maksimum Top-Up Tunggal dan Berkala sebesar Rp. 10.000.000.000,00 untuk satu tahun Polis";
		}
		return hsl;
	}
	
	public void count_rate_812(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		double hasil = 0;
		double ldec_pct1 = 0.1;
		int li_kali1 = 1;
		int li_usia = umurttg + 1 - 1;
		String err = "";
		String err1 = "";
		ldec_temp3 = 0;
		ldec_rate3 = 0;
		
		try{
			Double result = query.selectRateRider(kurs, li_usia, 0, kode_produk, nomor_produk);
				
			if(result != null) {
				hasil = result.doubleValue();
			}else{
				err1 = "Rate Asuransi SMiLe TPD Tidak Ada  !!!!";
			}
		} catch (Exception e) {
			err = e.toString();
		}
		
		ldec_temp3 = (( up / 1000) * hasil) * ldec_pct1;
		int decimalPlace = 2;
		
		BigDecimal bd = new BigDecimal(ldec_temp3);
		bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		ldec_temp3 = bd.doubleValue();	
		ldec_rate3 = hasil;
		
		BigDecimal jm = new BigDecimal(ldec_rate3);
		jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		ldec_rate3 = jm.doubleValue();

	}	

	public void count_rate_813(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		double hasil = 0;
		double hasilPremi = 0;
		double ldec_pct1 = 0.1;
		int li_kali1 = 1;
		int li_usia = umurttg + 1 - 1;
		String err = "";
		String err1 = "";
		ldec_temp4 = 0;
		ldec_rate4 = 0;
		
		try{
			Double result = query.selectRateRider(kurs, li_usia, 0, kode_produk, nomor_produk);
				
			if(result != null) {
				hasil = result.doubleValue();
			}else{
				err1 = "Rate Asuransi SMiLe CI Tidak Ada  !!!!";
			}
		} catch (Exception e) {
			err = e.toString();
		}
		
		ldec_temp4 = (( up / 1000) * hasil ) * ldec_pct1;

		int decimalPlace = 2;
		BigDecimal bd = new BigDecimal(ldec_temp4);
		bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		ldec_temp4 = bd.doubleValue();	
		ldec_rate4 = hasil;
		
		BigDecimal jm = new BigDecimal(ldec_rate4);
		jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		ldec_rate4 = jm.doubleValue();			
	}
	
	public static void main(String[] args) {

	}
}
