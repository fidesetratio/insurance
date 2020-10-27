package produk_asuransi;

import com.ekalife.utils.f_hit_umur;
// n_prod_187 Personal Accident Sinarmaslife(PAS)
/*
 * Created on Jul 29, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author HEMILDA
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_209 extends n_prod{

	public n_prod_209()
	{
//		PAS
		ii_bisnis_id = 209;
		ii_contract_period = 1;
		ii_age_from = 1;
		ii_age_to = 65;

		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";

//		1..4 cuma id, 0..3 di lst_cara_bayar
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

//
//		untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 1;
		ib_flag_end_age = false;
//
		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		idec_min_up01 = 2000000;  //Rp
		idec_max_up01 = 10000000;
		flag_uppremi=2;
		flag_jenis_plan = 5;
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		flag_as = 1;
	}

	public void of_hit_premi()
	{
		idec_rate = 1;
		if (ii_bisnis_no == 1)
		{
			idec_premi = 50000;
			idec_rate = 1;		
		}
		
		idec_premi = (idec_premi * idec_add_pct) / ii_permil;
			//idec_premi_main = idec_premi;
		idec_premi_main = idec_premi;
	}
	

	
	public double of_get_max_up()
	{
		double ldec_1=0;
		if (ii_bisnis_no == 1)
		{
			if (is_kurs_id.equalsIgnoreCase("01") )
			{
				idec_max_up01=10000000;
				ldec_1 = idec_max_up01;
			}
		}
		return ldec_1;
	}		
	
	public double of_get_min_up()
	{
		double ldec_1=0;
		if (ii_bisnis_no == 1)
		{
			if (is_kurs_id.equalsIgnoreCase("01") )
			{
				ldec_1 = 2000000;
			}
		}
		return ldec_1;
	}

//	cek usia	
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr =new f_hit_umur();
		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
		
		hsl="";
		if (nomor_produk <= 4)
		{
			if (bln < 6)
			{
				hsl="Usia Masuk Plan ini minimum : 6 Bulan";
			}
	
			if (ii_age < ii_age_from)
			{
				hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
			}
	
			if (ii_age > ii_age_to)
			{
				hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
			}
	
			if (ii_usia_pp < 17)
			{
				hsl="Usia Pemegang Polis mininum : 17 Tahun !!!";
			}
		}
		return hsl;		
	}
	
	public static void main(String[] args) {
	}
}
