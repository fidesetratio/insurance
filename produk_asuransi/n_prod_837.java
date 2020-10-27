package produk_asuransi;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.utils.Products;
import com.ekalife.utils.f_hit_umur;

//n_prod_837 (SMiLe Early Stage Critical Illness 99 � Critical Illness Rider)
/*
 * Created on May 8, 2014
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Andy
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_837 extends n_prod{
	
	protected final Log logger = LogFactory.getLog( getClass() );
	
	Query query = new Query();
	f_hit_umur umr = new f_hit_umur();
	public n_prod_837()
	{
		ii_bisnis_id = 837;
		ii_contract_period = 1;
		ii_age_from = 16; 
		ii_age_to = 65;		
		
		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";	
		
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 3;   //tahunan
		ii_pmode_list[2] = 2;   //semester
		ii_pmode_list[3] = 1;   //Tri		
		ii_pmode_list[4] = 6;   //bulanan	

		//pembayaran premi
		indeks_idec_pct_list=8;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1; // pmode 6
		idec_pct_list[7] = 1; // pmode 0

		flag_rider_baru=1;
		flag_rider=1;
		usia_nol = 0;
	}


//	of_set_usia_pp
	 public void of_set_usia_pp(int ai_pp)
	 {
		 ii_usia_pp = ai_pp;
	 }

//	of_set_usia_tt
	 public void of_set_usia_tt(int ai_tt)
	 {
		 ii_usia_tt = ai_tt;
	 }

	public String of_check_usia_rider(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk, int nomor_rider) 
	{
		
		hsl="";
		if (ii_usia_tt  < ii_age_from)
		{
			hsl="Usia Masuk Rider ini minimum : " + ii_age_from +" tahun";
		}
		if (ii_usia_tt > ii_age_to)
		{
			hsl="Usia Masuk Rider ini maximum : " + ii_age_to+ " tahun";
		}
		/*if (ii_usia_tt > (60-lm_byr))
		{
			hsl="Usia Masuk Rider ini maximum : " + (60-lm_byr) + " tahun";
		}*/
		return hsl;		
	}	
	//jenis = subbisnis
	public double of_get_rate1( int li_class, int flag_jenis_plan, int nomor_bisnis, int umurttg, int umurpp, int sex)
	{
		String err="";
		rate_rider=0;
		try {
			int jenis = 1;
			if(sex == 0){
				jenis = 2;
			}
			Double result = query.selectRateRider(is_kurs_id, umurttg, 0, 837, jenis);
			if(result != null) {
				rate_rider = result.doubleValue();		  	
			}
		}
	  catch (Exception e) {
			err=(e.toString());
	  } 		  	
 		  	
		return rate_rider;	
	}	
	
	
	public void wf_set_premi(int tahun,int bulan,int tanggal, int cara_bayar,int tahun_1,int bulan_1,int tanggal_1,int insperiod, int flag_jenis_plan, int ii_age, int lama_bayar, int flag_cerdas_siswa, int umurpp,int kode_bisnis,int number_bisnis)
	{
//		li_insured = lama_bayar;
		li_sd = 99;	
		
		Integer ltesci = li_sd - ii_age;		//hitung lama tertanggung Produk rider ESCI
		
		//by ridhaal
		//jika  masa lama tertanggung produk utama (insperiod) lebih besar dari perhitungan lama tertanggung ESCI99(ltesci) maka masa tertanggung rider ESCI mengambil perhitungan ( 99 - usia ) , karena max lama ESCI adalah 99
		//jika  masa lama tertanggung produk utama sama besar ato lebih kecil dari produk rider ESCI99 (lama tertanggung <=99) maka masa tertanggung rider mengambil masa tertanggung produk utama (masa tertanggung ESCI = masa tertanggung produk utama)
			if ( ltesci > insperiod){
				li_insured = insperiod;
			}else{
				li_insured = li_sd-ii_age;
			}
		
		//ld_temp = Relativedate(date(f_add_months(istr_polis.beg_date, li_insured * 12)), -1)
		tanggal_sementara = umr.f_add_months(tahun,bulan,tanggal,li_insured * 12);
		ldt_edate.set(tanggal_sementara.getYear()+1900,tanggal_sementara.getMonth(),tanggal_sementara.getDate());
		ldt_edate.add(ldt_edate.DATE,-1);
	
		tanggal_sementara1=umr.f_add_months(tahun,bulan,tanggal,( li_insured * 12) - 1);
		//ldt_epay = f_add_months(istr_polis.beg_date, ( li_insured * 12) - 1)
		ldt_epay.set(tanggal_sementara1.getYear()+1900,tanggal_sementara1.getMonth(),tanggal_sementara1.getDate());

	}
	
	public double of_get_up_with_limit(double idec_premi, double idec_up, double up_lowest, double up_highest ,int li_unit, int flag_jenis_plan, int kode_bisnis, int nomor_bisnis,int flag, int factor_up)
	{
		double up = idec_up;
		if (is_kurs_id.equalsIgnoreCase("01"))
		{
			up_highest = 2000000000;
		}else{
			up_highest = 200000;
		}
		
		if(up < up_lowest){
			up = up_lowest;
		}else if(up > up_highest){
			up = up_highest;
		}
		
		return up;
	}

//	*Untuk mendapatkan UP Rider berdasarkan Persentase UP
	public double of_get_up_with_factor(double idec_premi,double idec_up ,int li_unit, int flag_jenis_plan, int kode_bisnis, int nomor_bisnis,int flag, int factor_up)
	{
		double sum=0;
		double factor_x=0;
		
		if (factor_up==1 || factor_up==50 ){
			 factor_x = 0.5;
		}else if(factor_up==2 || factor_up==60){
			factor_x = 0.6;
		}else if(factor_up==3 || factor_up==70){
			factor_x = 0.7;
		}else if(factor_up==4 || factor_up==80){
			factor_x = 0.8;
		}else if(factor_up==5 || factor_up==90){
			factor_x = 0.9;
		}else if(factor_up==6 || factor_up==100){
			factor_x = 1;
		}
		if (flag_jenis_plan==2)
		{
			sum=idec_up*factor_x;
		}else{
			if (flag_jenis_plan==5)
			{
				sum = idec_up*factor_x;
			}else{
				sum=idec_up*factor_x;
			}
		}
		
		if(this.ii_bisnis_no==4 || this.ii_bisnis_no==5){
			if (is_kurs_id.equalsIgnoreCase("01"))
			{
				if (sum <5000000)
				{
					sum = new Double(5000000);
				}
			}else{
				if (sum < 500)
				{
					sum = new Double(500);
				}
			}
		}else {
			if (is_kurs_id.equalsIgnoreCase("01"))
			{
				if (sum > 2000000000)
				{
					sum = new Double(2000000000);
				}
			}else{
				if (sum > 200000)
				{
					sum = new Double(200000);
				}
			}
		}
		return sum;
	}

	public int set_klas(int klas)
	{
		iiclass=0;
		return iiclass;
	}
	
	public int set_unit(int unit)
	{
		iiunit=0;
		return iiunit;
	}

	public String range_class(int ii_age,int ii_class)
	{
		String hsl="";
		return hsl;
	}
	
	public String range_unit(int unit)
	{
		String hsl="";
		return hsl;
	}

	public void count_rate(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double upForCI, double premi, int pmode, int flag, int ins_period,int payperiod)//FIXIN
	{
		String nama_produk="";
		mbu_jumlah =0;
		mbu_rate=0;
		mbu_persen=0;
		if (Integer.toString(kode_produk).trim().length()==1)
		{
			nama_produk="produk_asuransi.n_prod_0"+Integer.toString(kode_produk);	
		}else{
			nama_produk="produk_asuransi.n_prod_"+Integer.toString(kode_produk);	
		}

		try{
			Class aClass2 = Class.forName( nama_produk );
			n_prod produk2 = (n_prod)aClass2.newInstance();
			produk2.setSqlMap(sqlMap);
			produk2.ii_bisnis_id_utama=ii_bisnis_id_utama;
			produk2.ii_bisnis_no_utama=ii_bisnis_no_utama;
			produk2.count_rate_837(klas, unit,837, nomor_produk, kurs, umurttg, umurpp, upForCI, premi,pmode, flag, ins_period, payperiod);// FIXIN , persenUp second2
			mbu_jumlah =  produk2.ldec_temp4;
			mbu_rate = produk2.ldec_rate4;
			mbu_persen = mbu_rate / 10 ;
		}
		catch (ClassNotFoundException e)
		{
			logger.error("ERROR :", e);
			 throw new NoClassDefFoundError (e.getMessage());
		} catch (InstantiationException e) {
			logger.error("ERROR :", e);
		} catch (IllegalAccessException e) {
			logger.error("ERROR :", e);
		}			
				
	}	
	
	
	public double cek_maks_up_rider (Double up_rider, String kurs)
	{
		if(this.ii_bisnis_no==4 || this.ii_bisnis_no==5){
			if (kurs.equalsIgnoreCase("01"))
			{
				if (up_rider.doubleValue() <5000000)
				{
					up_rider = new Double(5000000);
				}
			}else{
				if (up_rider.doubleValue() < 500)
				{
					up_rider = new Double(500);
				}
			}
		}else {
			if (kurs.equalsIgnoreCase("01"))
			{
				if (up_rider.doubleValue() > 2000000000)
				{
					up_rider = new Double(2000000000);
				}
			}else{
				if (up_rider.doubleValue() > 200000)
				{
					up_rider = new Double(200000);
				}
			}
		}
		
		return up_rider;
	}
	
	public double hit_premi_rider(double rate,double up,double persen, double premi)
	{
		double percentage=0;
		//TODO:
		//Deddy : yg ini aktif per tanggal 1 april 2010
		if(ii_bisnis_no==6){
			percentage=(rate * (up* 0.5) / 1000) * persen; //UP rider=up*0.5
		}else {
			percentage=(rate * up / 1000) * persen; 
		}
//		percentage=(rate * (up* factor_x) / 1000) * persen; //UP rider=up*0.5
		
		return percentage;	
	}
	
	public double hit_premi_rider_with_factor(double rate,double up,double persen, double premi, int factor_up)
	{
		double percentage=0;
		double factor_x=0;
		
		if (factor_up==1){
			 factor_x = 0.5;
		}else if(factor_up==2){
			factor_x = 0.6;
		}else if(factor_up==3){
			factor_x = 0.7;
		}else if(factor_up==4){
			factor_x = 0.8;
		}else if(factor_up==5){
			factor_x = 0.9;
		}else if(factor_up==6){
			factor_x = 1;
		}
		
		//TODO:
		if(ii_bisnis_no==6){
			percentage=(rate * (up * factor_x) / 1000) * persen; 
//			percentage=(rate * (up * 0.5) / 1000) * persen; //UP rider=up*0.5
		}else {
			percentage=(rate * (up * factor_x) / 1000) * persen; 
		}
//		percentage=(rate * (up* factor_x) / 1000) * persen; //UP rider=up*factor
		
		return percentage;	
	}
	
	public static void main(String[] args) {
	}
}
