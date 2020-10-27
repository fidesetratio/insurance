package produk_asuransi;
import java.sql.Date;

import com.ekalife.utils.f_hit_umur;

//n_prod_801 (PK)
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
public class n_prod_801 extends n_prod{
	Query query = new Query();
	f_hit_umur umr = new f_hit_umur();
	public n_prod_801()
	{
		ii_bisnis_id = 801;
		ii_contract_period = 1;
		ii_age_from = 17;
		ii_age_to = 55;		
		
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

		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1; 	// pmode 6	
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
		if(nomor_rider == 3 ){
			ii_age_to = 50;
		}else if(nomor_rider == 4 ){
			ii_age_to = 45;
		}else if( nomor_rider == 5 ){
			ii_age_to = 40;
		}
		
		if (ii_usia_tt  < ii_age_from)
		{
			hsl="Usia Masuk  untuk PK minimum : " + ii_age_from +" tahun";
		}
		if (ii_usia_tt > ii_age_to)
		{
			hsl="Usia Masuk  untuk PK maximum : " + ii_age_to +" tahun";
		}
		return hsl;		
	}	
	
	public double of_get_rate1( int li_class, int flag_jenis_plan, int nomor_bisnis, int umurttg, int umurpp)
	{

		String err="";
		rate_rider=0;
		try {
//			Double result = query.selectNilai(1, 801, is_kurs_id, ii_pmode, li_lbayar, li_lbayar, 1, ii_usia_tt);
			Double result = query.selectRateRider(is_kurs_id, umurttg, 0, 801, nomor_bisnis);
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
		li_insured = lama_bayar;
		//ldt_edate.set(tahun_1,bulan_1,tanggal_1);
		Date tgls = umr.f_add_months(tahun,bulan,tanggal,( li_insured * 12) - 1);
		ldt_edate.set(tgls.getYear()+1900,tgls.getMonth()+1,tgls.getDate());
		ldt_edate.add(ldt_edate.DAY_OF_MONTH , -1);
		tanggal_sementara1=umr.f_add_months(tahun,bulan,tanggal,( li_insured * 12) - 1);
		ldt_epay=null;
		
	}
	
	/**
	 * Yusuf (10/04/08) UP rider = 1/2 UP plan pokok
	 */
	public double of_get_up(double idec_premi,double idec_up ,int li_unit, int flag_jenis_plan, int kode_bisnis, int nomor_bisnis,int flag)
	{
		double sum=0;
		if (li_unit==0)
		{
			li_unit=1;
		}
		ii_class=1;
		sum=idec_up/2;
		up_pa=0;
		up_pb=0;
		up_pc=0;
		up_pd=0;
		up_pm=0;		
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
	
	public void of_set_pmode(int ai_pmode)
	{
		ii_pmode = ai_pmode;
		if (ii_pmode == 0){
			ii_pmode = 0 ;
		}else{
			ii_pmode = 3;
		}
	}		

	public double cek_maks_up_rider (Double up_rider, String kurs)
	{
		if (kurs.equalsIgnoreCase("01"))
		{
			if (up_rider.doubleValue() > 500000000)
			{
				up_rider = new Double(500000000);
			}
		}else{
			if (up_rider.doubleValue() > 50000)
			{
				up_rider = new Double(50000);
			}
		}
		return up_rider;
	}
	
	public static void main(String[] args) {

	
	}
}
