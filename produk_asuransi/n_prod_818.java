package produk_asuransi;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.utils.f_hit_umur;

//n_prod_818 (TOTAL AND PERMANENT DISABILITY RIDER)
/*
 * Created on Oct 6, 2005
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
public class n_prod_818 extends n_prod{
	
	protected final Log logger = LogFactory.getLog( getClass() );

	f_hit_umur umr = new f_hit_umur();
	public n_prod_818()
	{
		ii_bisnis_id = 818;
		ii_contract_period = 1;
		ii_age_from = 1;
		ii_age_to = 60;		
		
		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";	
		
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 3;   //tahunan


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
		
		hsl="";
		if (ii_usia_tt  < ii_age_from)
		{
			hsl="Usia Masuk Rider ini minimum : " + ii_age_from +" tahun";
		}
		if (ii_usia_tt > ii_age_to)
		{
			hsl="Usia Masuk Rider ini maximum : " + ii_age_to+ " tahun";
		}
		return hsl;		
	}	
	
	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
		
		if(ii_bisnis_no == 2) {
			lsr_jenis = 2; //apabila term rider yang include = 1, apabila yg bukan = 2
			ii_age_to = 70; //apabila term rider yang include = 60, apabila yg bukan = 70
			li_sd = 70;
		}
		
	}
	
	public double of_get_rate1( int li_class, int flag_jenis_plan, int nomor_bisnis, int umurttg, int umurpp)
	{
		String err="";
		rate_rider=0;
		try {
			Double result = query.selectRateRider(is_kurs_id, umurttg, 0, 818, lsr_jenis);
			if(result != null) {
				rate_rider = result.doubleValue();		  	
			}
		}
	  catch (Exception e) {
			err=(e.toString());
	  } 		  	
		return rate_rider;	
	}	
	
	public void wf_set_premi(int tahun,int bulan,int tanggal, int cara_bayar,int tahun_1,int bulan_1,int tanggal_1,int insperiod, int flag_jenis_plan, int ii_age, int lama_bayar ,int flag_cerdas_siswa, int umurpp,int kode_bisnis,int number_bisnis)
	{
		li_insured = li_sd - ii_age;
		if (flag_jenis_plan==1)
		{
			li_insured = lama_bayar;
		}
		if (flag_jenis_plan==2 )
		{
			li_insured = li_sd - ii_age;
		}
		
		if(number_bisnis==3){
			li_insured = 4;
		}
		if(number_bisnis==4){
			li_insured = 4;
		}
		
		if(number_bisnis==5){
			li_insured = 8;
		}
		
		if(number_bisnis==6){
			li_insured = 4;
		}
		
		//ld_temp = Relativedate(date(f_add_months(istr_polis.beg_date, li_insured * 12)), -1)
		tanggal_sementara = umr.f_add_months(tahun,bulan,tanggal,li_insured * 12);
		ldt_edate.set(tanggal_sementara.getYear()+1900,tanggal_sementara.getMonth(),tanggal_sementara.getDate());
		ldt_edate.add(ldt_edate.DATE,-1);
	
		tanggal_sementara1=umr.f_add_months(tahun,bulan,tanggal,( li_insured * 12) - 1);
		//ldt_epay = f_add_months(istr_polis.beg_date, ( li_insured * 12) - 1)
		ldt_epay.set(tanggal_sementara1.getYear()+1900,tanggal_sementara1.getMonth(),tanggal_sementara1.getDate());


	}

	public double of_get_up(double idec_premi,double idec_up ,int li_unit, int flag_jenis_plan, int kode_bisnis, int nomor_bisnis,int flag)
	{
		double sum=0;
		sum=idec_up;
		
		return sum;
	}

	public int set_klas(int klas)
	{
		iiclass=0;
		return iiclass;
	}
	
	public int set_unit(int unit)
	{
		int iiunit=0;
		//if (unit<1)
		//{
			iiunit=1;			
		//}

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
			produk2.ii_bisnis_id_utama=ii_bisnis_id_utama;
			produk2.ii_bisnis_no_utama=ii_bisnis_no_utama;
			produk2.count_rate_818(klas,unit,818,nomor_produk,kurs,umurttg,umurpp,up,premi,pmode,flag,ins_period,payperiod);
			mbu_jumlah =  produk2.ldec_temp9;
			mbu_rate = produk2.ldec_rate9;
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
		if(this.ii_bisnis_no==3 || this.ii_bisnis_no==4){
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
//			if (kurs.equalsIgnoreCase("01"))
//			{
//				if (up_rider.doubleValue() > 100000000)
//				{
//					up_rider = new Double(100000000);
//				}
//			}else{
//				if (up_rider.doubleValue() > 10000)
//				{
//					up_rider = new Double(10000);
//				}
//			}
		}
			
		return up_rider;
	}
			
	public static void main(String[] args) {
	}
}
