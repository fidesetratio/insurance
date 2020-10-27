//n_prod_70 EKASISWA EMAS(NEW)
//Latihan dari deddy

package produk_asuransi;

public class n_prod_172 extends n_prod{
	int ii_pay_period=0;
	public n_prod_172(){
//		SK No. 111/AJS-SK/X/2008
		ii_bisnis_id = 172;
		ii_contract_period = 23;
		ii_age_from = 20;
		ii_age_to = 55;
		ib_flag_pp = true;
		
//		Minimum UP untuk rupiah(01) dan dolar(02)
		idec_min_up01 = 10000000;
		idec_min_up02 = 1000;
		
		//flag_as = 3;
		
		indeks_is_forex=2;
		is_forex=new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";

//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=5;//5;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 1 ;  //triwulanan
		ii_pmode_list[2] = 2 ;  //semesteran
		ii_pmode_list[3] = 3 ;  //tahunan
		ii_pmode_list[4] = 6 ; //bulanan
		
		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1; // pmode 6
		
		ii_end_from = 23;
		ib_flag_end_age = false;

		indeks_ii_lama_bayar=2;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 18;
		ii_lama_bayar[1] = 18;
		flag_uppremi=0;
		
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		usia_nol = 1;
		//flag_worksite = 1;
	}
	
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk)
	{
		hsl="";
//		   Chek Pemegang Polis
		if (ii_age < ii_age_from)
		{
			hsl="Usia Pemegang Polis untuk Plan ini minimum : " +  ii_age_from ;
		}

		if (ii_age > ii_age_to)
		{
			hsl="Usia Pemegang Polis untuk Plan ini maximum : " +  ii_age_to  ;
		}

//		   Chek Tertanggung Polis
		if (ii_usia_tt < 1)
		{
			hsl="Usia Anak untuk Plan ini minimum : 1 Tahun"  ;
		}

		if (ii_usia_tt > 12)
		{
			hsl="Usia Anak untuk Plan ini maximum : 12 Tahun" ;
		}
		return hsl;		
	}
	
	
	public int of_get_payperiod()
	{
		if (ib_single_premium)
		{
			ii_lama_bayar[0] = 1;
		}else{
			// 18 - umur anak
			ii_lama_bayar[0] = (18 - ii_usia_tt);	
			ii_lama_bayar[1] = (18 - ii_usia_tt);
		}
		ii_pay_period = ii_lama_bayar[0];

		return ii_lama_bayar[ii_bisnis_no-1]	;	 
	}

	public void of_set_usia_tt(int ai_tt)
	{
		if (ai_tt < 1)
		{
			ii_usia_tt = 1;
		}else{
			ii_usia_tt = ai_tt;
		}
		of_set_age();
//		  ii_contract_period = 23 - ii_usia_pp
		ii_contract_period = 23 - ii_usia_tt;
		ii_end_from = ii_contract_period;

		ii_lama_bayar[0] = (18 - ii_usia_tt);
		ii_lama_bayar[1] = (18 - ii_usia_tt);
		if (ib_single_premium)
		{
			ii_lama_bayar[0] = 1;
			ii_lama_bayar[1] = 1;
		}
	}
	
	public void of_hit_premi()
	{
		hsl="";
		err="";
		String ls_sql;
		li_lbayar=0;
		li_cp = 0;
		ldec_rate=0;

		if (ib_single_premium )
		{
			li_lbayar = 1;
		}else{
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			if (ii_bisnis_id >= 800)
			{ 
				li_lbayar = ii_lbayar;
				ii_contract_period = li_lbayar;
			}
		}
		switch (ii_bisnis_id)
		{
			case 19 :
				ii_contract_period = ii_lama_bayar[ii_bisnis_no-1];
				break;
			case 20 :
				ii_contract_period = ii_lama_bayar[ii_bisnis_no-1];
				break;
		}

		li_cp = ii_pmode;
//		   Kalau triwulan, semester, bulanan, jadiin tahunan
		if (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6 )
		{
			li_cp = 3;	
		}

		try {

			Double result = query.selectNilaiNew(ii_jenis, ii_bisnis_id,ii_bisnis_no, is_kurs_id, li_cp, li_lbayar, 0, ii_tahun_ke, ii_age);
             if (result==null){
            	 result = query.selectNilaiNew(ii_jenis, ii_bisnis_id,ii_bisnis_no, is_kurs_id, li_cp, li_lbayar, ii_contract_period, ii_tahun_ke, ii_age);
             }
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

				if (ii_bisnis_id < 800)
				{
					 idec_premi_main = idec_premi;	
				}					
			 }else{
			 	hsl="Tidak ada data rate";
			 }
		
		}
	  catch (Exception e) {
			err=(e.toString());
	  } 
	}

	public static void main(String[] args) {

		
	}
	
}