// n_prod_97 Fast Excellink
package produk_asuransi;

import com.ekalife.utils.f_hit_umur;
/*
 * Created on Aug 1, 2005
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
public class n_prod_97 extends n_prod{

	Query query = new Query();
	public n_prod_97()
	{
		ii_bisnis_id = 97;
		ii_contract_period = 10;
		ii_age_from = 1;
		ii_age_to = 65;
		
		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";

//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 0 ;  //sekaligus
		ii_pmode_list[2] = 3 ;  //tahunan
		ii_pmode_list[3] = 2 ;  //semester
		ii_pmode_list[4] = 1 ;  //Tri

//		untuk hitung end date ( 79 - issue_date )
		ii_end_from = 10;
		ib_flag_end_age = false;
		idec_min_up01 = 15000000;
		idec_max_up01 = 1000000000;
		indeks_ii_lama_bayar=2;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 5;

		indeks_idec_list_premi=14;
		idec_list_premi = new double[indeks_idec_list_premi];
		for (int i =0 ; i< 13 ; i++)
		{	
			idec_list_premi[i] = 3000000 + ( 1000000 * (i - 1) );
		}
		
		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.26 ;// pmode 1
		idec_pct_list[2] = 0.51; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.09; // pmode 6
		flag_account =2;
		flag_uppremi=1;
		kode_flag=2;
		flag_cerdas_siswa=3;
		
		indeks_rider_list=4;
		ii_rider = new int[indeks_rider_list];
		ii_rider[0]=801;
		ii_rider[1]=802;
		ii_rider[2]=804;
		ii_rider[3]=822;
		
		flag_as=1;
		li_insper = 10;
		usia_nol = 1;
		//Yusuf - 20050203
		isInvestasi = true;
		   indeks_kombinasi = 6;
		   kombinasi = new String[indeks_kombinasi];
		   kombinasi[0] = "A"; // pp 100% 
		   kombinasi[1] = "C"; // pp 80% - ptb 20%
		   kombinasi[2] = "E"; // pp 60% - ptb 40%
		   kombinasi[3] = "F"; // pp 50% - ptb 50%
		   kombinasi[4] = "G"; // pp 40% - ptb 60%
		   kombinasi[5] = "I"; // pp 20% - ptb 80%
	}
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr =new f_hit_umur();
		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);

		hsl="";
		if (bln < 1)
		{
			hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
		}
		if (ii_age > ii_age_to)
		{
			hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
		}
		if (ii_usia_pp > 85)
		{
			hsl="Usia Pemegang Polis maximum : 85 Tahun !!!"; // permintaan Achmad Anwarudin Tuesday, November 06, 2007 11:03 AM
		}
		if (ii_usia_pp < 17)
		{
			hsl="Usia Pemegang Polis minimum : 17 Tahun !!!";
		}
		
		return hsl;		
	}	
	public boolean of_check_premi(double ad_premi)
	{
		if (ii_bisnis_no == 1)
		{
			if (ad_premi < 10000000)
			{
				return false;
			}
		}else{
			if (ii_bisnis_no == 2 || ii_bisnis_no == 3 )
			{
				switch (ii_pmode)
				{
					case 3:
						if (ad_premi < 3000000)
						{
							return false;
						}
						break;
					case 2:
						if (ad_premi < 1575000)
						{
							return false;
						}
						break;
					case 1:
						if (ad_premi < 810000)
						{
							return false;
						}
				}
			}
		}
		return true;
	}	

	public int of_get_conperiod(int number_bisnis)
	{
		return ii_contract_period;	
	}

	public double of_get_rate()
	{
		hsl="";
		err="";
		if (ii_bisnis_id >= 800)
		{
			int li_lbayar=0;
			li_cp=0;
			ldec_rate=0;
	
			if (ib_single_premium)
			{ 
				li_lbayar = 1 ;
			}else{ 
				li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
				if (ii_bisnis_id >= 800)
				{ 
					li_lbayar = ii_lbayar;
					ii_contract_period = li_lbayar;
				}
			}
			
			li_cp = ii_pmode;
			// Kalau triwulan, semester, bulanan, jadiin tahunan
			if (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6)
			{
				li_cp = 3;
			}

			try {
				Double result = query.selectNilai(ii_jenis, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, 10, ii_tahun_ke, ii_age);

				 if(result != null) {
					ldec_rate = result.doubleValue();

				   if (ii_bisnis_id == 802 || ii_bisnis_id == 804)
				   { 
					   //kalau PC atau WPD, jangan dikali idec_add_pct, karna premi udah dikali idec_add_pct
					   idec_premi = idec_up * ldec_rate / ii_permil;
				   }else{
					   idec_premi = idec_up * ldec_rate * idec_add_pct / ii_permil;
				   }
				   idec_rate = ldec_rate;
				}else{	
					hsl="Tidak ada data rate";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 
		}
		if (ii_bisnis_id < 800)
		{
			 idec_premi_main = idec_premi;
		}
		return idec_rate;		
	}	

	public void of_set_bisnis_no(int ai_no)
	{
		ll_premi=0;
		indeks_li_pmode=4;
		li_pmode = new int[indeks_li_pmode];	

		ii_bisnis_no = ai_no;

		if (ii_bisnis_id < 800){
			if (ai_no == 1)
			{
				indeks_li_pmode=2;
				li_pmode[1] = 0;
			}else{
				indeks_li_pmode=4;
				li_pmode[1] = 3;
				li_pmode[2] = 2;
				li_pmode[3] = 1;
			}
			ii_pmode_list = li_pmode;
			indeks_ii_pmode_list=indeks_li_pmode;
			
			ii_bisnis_no_utama = ii_bisnis_no;
			ii_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			of_set_pmode(ii_pmode_list[1]);
			
			
		}else{
			 if (ii_bisnis_id == 802 || ii_bisnis_id == 804)//PC or WPD
				{
					ii_age = ii_usia_pp;
					of_set_up(idec_premi_main);	
				}else{
					of_set_age();
					of_set_up(idec_up);
				}
		}

		switch(ai_no){
			case 1:
				ll_premi = 5000000;
				break;
			case 2:
				ll_premi = 3000000;
				break;
			case 3:
				ll_premi = 3000000;
				break;
		}
		indeks_idec_list_premi=14;
		idec_list_premi = new double[indeks_idec_list_premi];	
		for (int i = 0 ; i < 13 ; i++)
		{
			idec_list_premi[i] = ll_premi + ( 1000000 * (i - 1) );
		}
	}

	public String of_alert_min_premi( double premi)
	{
		String hasil_min_premi="";
		boolean cek_premi=of_check_premi(premi);
		if (cek_premi==false)
		{
			hasil_min_premi="Premi Minimum untuk Plan ini : Sekaligus : 10.000.000,- ~ Tahunan : 3.000.000,- ~ Semesteran : 1.575.000,- ~ Triwulanan : 825.000,-" ;
		}
		return hasil_min_premi;
	}

	public void of_set_premi(double adec_premi)
	{
		ldec_up=0;
		ldec_sisa=0;

		if (ii_pmode == 0)
		{
			idec_up = (adec_premi / idec_add_pct ) * 1.5;
			idec_rate = 1500;
		}else{
			idec_up = (adec_premi / idec_add_pct ) * 5;
			idec_rate = 5000;
			
		}

		ldec_sisa = ( idec_up % 50 );
		if (ldec_sisa != 0) 
		{
			ldec_up = ldec_up - ldec_sisa;
		}
		of_set_up(idec_up);
		idec_premi_main = adec_premi;
	}

	public double f_get_bia_akui(int ar_lb, int ar_ke)
	{
		double ld_bia=0;
		switch (ar_lb)
		{
			case 1:
				if (ar_ke == 1)
				{
					ld_bia = 0.175;
				}
			break;
			case 5:
				if (ar_ke == 1) 
				{
					ld_bia = 0.7;
				}else{ 
					ld_bia = 0.05;
				}
			break;
		}
		
		return ld_bia;	
	}
	
	public String min_total_premi(Integer pmode_id, double premi , String kurs)
	{
		String hsl="";
		double total_premi = premi ;
		switch (pmode_id.intValue())
		{
			case 0:
				if (total_premi < 10000000)
				{
					hsl="Minimum Total Premi Sekaligus : Rp. 10.000.000";
				}
				break;
			case 1:
				if (total_premi < 75000)
				{
					hsl="Minimum Total Premi Triwulanan (3 bulanan ) : Rp. 75.000,00";
				}
				break;
			case 2:
				if (total_premi < 150000)
				{
					hsl="Minimum Total Premi Semesteran (6 bulanan ) : Rp 150.000,00";
				}
				break;
			case 3:
				if (total_premi < 3000000)
				{
					hsl="Minimum Total Premi Tahunan : Rp. 3.000.000";
				}
				break;
		}
	return hsl;
	}
	
	public String min_topup(Integer pmode_id, double premi , double premi_topup, String kurs, int jenis_topup)
	{
		String hsl="";
		if (jenis_topup==2)
		{
			if (kurs.equalsIgnoreCase("01"))
			{
				if (premi_topup < 1000000)
				{
					hsl="Minimum Top-Up Tunggal : Rp. 1.000.000.";
				}
			}else{
				if (premi_topup < 100)
				{
					hsl="Minimum Top-Up Tunggal : US$ 100.";
				}	
			}
		}
		
		if (premi_topup > 1e10)
		{
			hsl="Maksimum Top-Up Tunggal dan Berkala sebesar Rp. 10.000.000.000. untuk satu tahun Polis";
		}
		
		double total_premi = premi + premi_topup;
			switch (pmode_id.intValue())
			{
				case 0:
					if (total_premi < 10000000)
					{
						hsl="Minimum Total Premi Sekaligus : Rp. 10.000.000";
					}
					break;
				case 1:
					if (total_premi < 75000)
					{
						hsl="Minimum Total Premi Triwulanan (3 bulanan ) : Rp. 75.000,00";
					}
					break;
				case 2:
					if (total_premi < 150000)
					{
						hsl="Minimum Total Premi Semesteran (6 bulanan ) : Rp 150.000,00";
					}
					break;
				case 3:
					if (total_premi < 3000000)
					{
						hsl="Minimum Total Premi Tahunan : Rp. 3.000.000";
					}
					break;
			}
		return hsl;
	}
	
	
	public static void main(String[] args) {
	}
}
