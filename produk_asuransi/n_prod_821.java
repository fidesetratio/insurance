package produk_asuransi;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.utils.f_hit_umur;

//n_prod_821 smart medicare rider(khusus pasangan suami istri)
/*
 * Created on Juli 15, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Hemilda
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_821 extends n_prod{
	
	protected final Log logger = LogFactory.getLog( getClass() );
	
	Query query = new Query();
	f_hit_umur umr = new f_hit_umur();
	public n_prod_821()
	{
		ii_bisnis_id = 821;
		ii_contract_period = 10;
		ii_age_from = 1;
		ii_age_to = 65;	
		
		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 1; // triwulan
		ii_pmode_list[2] = 2; // semesteran
		ii_pmode_list[3] = 3; // tahunan
		ii_pmode_list[4] = 6; // bulanan
		
		indeks_ii_lama_bayar=16;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 5;
		ii_lama_bayar[1] = 5;
		ii_lama_bayar[2] = 5;
		ii_lama_bayar[3] = 5;
		ii_lama_bayar[4] = 5;
		ii_lama_bayar[5] = 5;
		ii_lama_bayar[6] = 5;
		ii_lama_bayar[7] = 5;
		ii_lama_bayar[8] = 10;
		ii_lama_bayar[9] = 10;
		ii_lama_bayar[10] = 10;
		ii_lama_bayar[11] = 10;
		ii_lama_bayar[12] = 10;
		ii_lama_bayar[13] = 10;
		ii_lama_bayar[14] = 10;
		ii_lama_bayar[15] = 10;
		// idec_min_up01=300000;
		ii_end_from = 65;
		
		flag_worksite =1;
		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1; // pmode 6
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
		f_hit_umur umr =new f_hit_umur();
		
		hsl="";
		
		//usia tertanggung
		
		//mpp 5
		if (nomor_produk >= 1 && nomor_produk <= 8){
			if (ii_age > 60) hsl = "Usia Masuk Plan ini maximum : 60";
		
		//mpp 10
		}else if (nomor_produk >= 9 && nomor_produk <= 16){
			if (ii_age > 55) hsl = "Usia Masuk Plan ini maximum : 55";
		}					
		return hsl;
	}	
	
	public double of_get_rate1( int li_class, int flag_jenis_plan, int nomor_bisnis, int umurttg, int umurpp)
	{
		String err="";
		rate_rider=0;

		if (is_kurs_id.equalsIgnoreCase("01"))
		{
			rate_rider = 90000;
		}else{
			rate_rider = 9;
		}
		if (ii_class == 4)	
		{
			rate_rider= 2 * rate_rider;
		}

		rate_rider = rate_rider/100;
		return rate_rider;	
	}	
	
	public void wf_set_premi(int tahun,int bulan,int tanggal, int cara_bayar,int tahun_1,int bulan_1,int tanggal_1,int insperiod, int flag_jenis_plan, int ii_age, int lama_bayar, int flag_cerdas_siswa, int umurpp,int kode_bisnis,int number_bisnis)
	{
		li_insured = li_sd - ii_age;

		//ld_temp = Relativedate(date(f_add_months(istr_polis.beg_date, li_insured * 12)), -1)
		tanggal_sementara = umr.f_add_months(tahun,bulan,tanggal,li_insured * 12);
		ldt_edate.set(tanggal_sementara.getYear()+1900,tanggal_sementara.getMonth(),tanggal_sementara.getDate());
		ldt_edate.add(ldt_edate.DATE,-1);
	
		tanggal_sementara1=umr.f_add_months(tahun,bulan,tanggal,( li_insured * 12)-1 );
		//ldt_epay = f_add_months(istr_polis.beg_date, ( li_insured * 12) - 1)
		ldt_epay.set(tanggal_sementara1.getYear()+1900,tanggal_sementara1.getMonth(),tanggal_sementara1.getDate());

		if (is_kurs_id.equalsIgnoreCase("01"))
		{
			li_kali = 1000;
		}

	}

	public double of_get_up(double idec_premi,double idec_up ,int li_unit, int flag_jenis_plan, int kode_bisnis, int nomor_bisnis,int flag)
	{
		double ldec_1=0;
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			if(ii_bisnis_no == 1){//SM-1 5 tahun
				idec_up = 300000;
			}else if(ii_bisnis_no == 2){//SM-2 5 tahun
				idec_up = 400000;
			}else if(ii_bisnis_no == 3){//SM-3 5 tahun
				idec_up = 500000;
			}else if(ii_bisnis_no == 4){//SM-4 5 tahun
				idec_up = 600000;
			}else if(ii_bisnis_no == 5){//SM-5 5 tahun
				idec_up = 700000;
			}else if(ii_bisnis_no == 6){//SM-6 5 tahun
				idec_up = 800000;
			}else if(ii_bisnis_no == 7){//SM-7 5 tahun
				idec_up = 900000;
			}else if(ii_bisnis_no == 8){//SM-8 5 tahun
				idec_up = 1000000;
			}else if(ii_bisnis_no == 9){//SM-1 10 tahun
				idec_up = 300000;
			}else if(ii_bisnis_no == 10){//SM-2 10 tahun
				idec_up = 400000;
			}else if(ii_bisnis_no == 11){//SM-3 10 tahun
				idec_up = 500000;
			}else if(ii_bisnis_no == 12){//SM-4 10 tahun
				idec_up = 600000;
			}else if(ii_bisnis_no == 13){//SM-5 10 tahun
				idec_up = 700000;
			}else if(ii_bisnis_no == 14){//SM-6 10 tahun
				idec_up = 800000;
			}else if(ii_bisnis_no == 15){//SM-7 10 tahun
				idec_up = 900000;
			}else if(ii_bisnis_no == 16){//SM-8 10 tahun
				idec_up = 1000000;
			}
			ldec_1 = idec_up;
		}else{
			ldec_1 = idec_min_up02;
		}
		return ldec_1;

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
	
	public double hit_premi_rider(double rate,double up,double persen, double premi)
	{
		return premi;	
	}

	public void count_rate(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
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
			produk2.count_rate_821(klas,unit,821,nomor_produk,kurs,umurttg,umurpp,up,premi,pmode,flag,ins_period,payperiod);
			mbu_jumlah =  produk2.ldec_temp2;
			mbu_rate = produk2.ldec_rate2;
			mbu_persen = mbu_rate ;
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
	
	public static void main(String[] args) {
	}
}
