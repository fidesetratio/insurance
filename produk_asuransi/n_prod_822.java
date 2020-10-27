package produk_asuransi;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.utils.f_hit_umur;

//n_prod_822 (Swine Flu Rider alias Flu Babi)
/*
 * Created on Aug 5, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Deddy
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_822 extends n_prod{
	
	protected final Log logger = LogFactory.getLog( getClass() );

	f_hit_umur umr = new f_hit_umur();
	public n_prod_822()
	{
		ii_bisnis_id = 822;
		ii_contract_period = 1;
		ii_age_from = 1;
		ii_age_to = 60;		
		
		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";	
		
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 1; // triwulan
		ii_pmode_list[2] = 2; // semesteran
		ii_pmode_list[3] = 3; // tahunan
		ii_pmode_list[4] = 6; // bulanan


		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1; // pmode 6	
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
		f_hit_umur umr =new f_hit_umur();
		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);

		hsl="";
//		if (bln < 12)
//		{
//			hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
//		}
//		if (ii_usia_tt  < ii_age_from)
//		{
//			hsl="Usia Masuk Rider ini minimum : " + ii_age_from +" tahun";
//		}
//		if (ii_usia_tt > ii_age_to)
//		{
//			hsl="Usia Masuk Rider ini maximum : " + ii_age_to+ " tahun";
//		}
		return hsl;		
	}	
	
	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
		
		if(ii_bisnis_no == 1) {
			lsr_jenis = 1; //apabila swine rider yang include = 1, apabila yg bukan = 2
			ii_age_to = 60; //apabila swine rider yang include = 60, apabila yg bukan = 70
			li_sd = 60;
		}
		
	}
	
	public double of_get_rate1( int li_class, int flag_jenis_plan, int nomor_bisnis, int umurttg, int umurpp)
	{
		String err="";
		rate_rider=0;
		try {
			Double result = query.selectRateRider(is_kurs_id, umurttg, 0, 822, lsr_jenis);
			if(result != null) {
				rate_rider = result.doubleValue();		  	
			}
		}
	  catch (Exception e) {
			err=(e.toString());
	  } 		  	
		return rate_rider;	
	}	
	
	public String range_unit(int unit)
	{
		String hsl="";
		return hsl;
	}
	
	public void wf_set_premi(int tahun,int bulan,int tanggal, int cara_bayar,int tahun_1,int bulan_1,int tanggal_1,int insperiod, int flag_jenis_plan, int ii_age, int lama_bayar ,int flag_cerdas_siswa, int umurpp,int kode_bisnis,int number_bisnis)
	{
		if(ii_bisnis_no == 1) {
			//li_insured = 1;
			li_insured = 1;
		}else if(ii_bisnis_no == 2){
			li_insured = 1;
		}
		
		//ld_temp = Relativedate(date(f_add_months(istr_polis.beg_date, li_insured * 12)), -1)
		tanggal_sementara = umr.f_add_months(tahun,bulan,tanggal,li_insured * 12);
		ldt_edate.set(tanggal_sementara.getYear()+1900,tanggal_sementara.getMonth(),tanggal_sementara.getDate());
		ldt_edate.add(ldt_edate.DATE,-1);
	
		tanggal_sementara1=umr.f_add_months(tahun,bulan,tanggal,( li_insured * 12)-1 );
		//ldt_epay = f_add_months(istr_polis.beg_date, ( li_insured * 12) - 1)
		ldt_epay.set(tanggal_sementara1.getYear()+1900,tanggal_sementara1.getMonth(),tanggal_sementara1.getDate());

	}

	public double of_get_up(double idec_premi,double idec_up ,int li_unit, int flag_jenis_plan, int kode_bisnis, int nomor_bisnis,int flag)
	{
		double sum=0;
//		if(ii_bisnis_no == 1) {
//			
//			sum = new Double(2000);
//		}else {
			sum=idec_up;
//		}
		
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
			if(nomor_produk==1){
				mbu_jumlah = 0.0;
			}else if(nomor_produk==2){
				mbu_jumlah =  new Double(0.05) * up;
			}
			mbu_rate = 0.0;
			//mbu_persen = mbu_rate / 10 ;
			mbu_persen = 0.0 ;
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
		if (kurs.equalsIgnoreCase("01"))
			{
				if (up_rider.doubleValue() > new Double(20000000))
				{
					up_rider = new Double(20000000);
				}
			}else{
				if (up_rider.doubleValue() > new Double(2000))
				{
					up_rider = new Double(2000);
				}
			}
		
			
		return up_rider;
	}
			
	public static void main(String[] args) {
	}
}
