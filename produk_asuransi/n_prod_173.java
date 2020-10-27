package produk_asuransi;

/**
 * n_prod_63 Eka Sarjana Mandiri yang baru
 *  
 * @author Yusuf
 * @since Nov 6, 2008 (8:30:47 AM)
 */
public class n_prod_173 extends n_prod{

	public n_prod_173()
	{
		ii_bisnis_id = 63;
		ii_contract_period = 25;
		ii_age_from = 17;
		ii_age_to = 55;
		ib_flag_pp = true;
		
		idec_min_up01 = 10000000;
		idec_min_up02 = 1000;
		
		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=3;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 0 ;  //sekaligus
		ii_pmode_list[2] = 3 ;  //TAHUNAN
		
		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.27; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1; // pmode 6

//		untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 25;
		ib_flag_end_age = false;

		indeks_ii_lama_bayar=6;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];		
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 3;
		ii_lama_bayar[2] = 5;
		ii_lama_bayar[3] = 1;
		ii_lama_bayar[4] = 3;
		ii_lama_bayar[5] = 5;
		
		li_usia = new int[3];
		flag_uppremi=0;
		usia_nol = 0;
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		//flag_worksite = 1;

	}

	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk)
	{
		hsl="";

		if (ii_age < ii_age_from)
		{
			hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
		}

		if (ii_age > ii_age_to)
		{
			hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
		}

		return hsl;		
	}
	
	public String cek_umur_beasiswa(int umur,int nomor_produk)
	{
		hsl="";
		int ii_usia_tt;
		int ii_bisnis_no=nomor_produk;
		ii_usia_tt=umur;
		li_usia = new int[6];
		li_usia[0] = 12;
		li_usia[1] = 12;
		li_usia[2] = 9;
		li_usia[3] = 12;
		li_usia[4] = 12;
		li_usia[5] = 9;
//		   Chek Tertanggung Polis
		if (ii_usia_tt < 0)
		{
			hsl="Usia Anak untuk Plan ini minimum : 0 Tahun";
		}

		if (ii_usia_tt > li_usia[ii_bisnis_no-1])
		{
			hsl="Usia Anak untuk Plan ini maximum : " + li_usia[ii_bisnis_no-1];
		}		
		return hsl;
	}	

	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
		indeks_li_pmode_list=5;
		li_pmode_list = new int[indeks_li_pmode_list];			

		if (ai_no == 1)
		{
			indeks_li_pmode_list=2;
			li_pmode_list[1] = 0 ;			 
		}else{
			indeks_li_pmode_list=5;
			li_pmode_list[1] = 3;
			li_pmode_list[2] = 1;
			li_pmode_list[3] = 2;
			li_pmode_list[4] = 6;
		}
		ii_pmode_list = li_pmode_list;
		indeks_ii_pmode_list=indeks_li_pmode_list;

		if (ii_bisnis_id < 800){
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

		indeks_li_pmode=indeks_ii_pmode_list;
		li_pmode = new int[indeks_li_pmode];
		
		for (int i =1 ; i<indeks_li_pmode;i++)
		{
			li_pmode[i] = ii_pmode_list[i];
			
		}
	}
	
//	 of_set_pmode
	public void of_set_pmode(int ai_pmode)
	{
		ii_pmode = ai_pmode;
		if (ii_pmode == 0){
			ib_single_premium = true ;
		}
		idec_add_pct = idec_pct_list[ii_pmode];
		
	}
	
	public void of_set_usia_tt(int ai_tt)
	{
		ii_usia_tt = ai_tt;

		of_set_age();
		ii_contract_period = 25 - ii_usia_tt;
		ii_end_from = ii_contract_period;
	}
	
	public String cek_hubungan(String hub)
	{
		hsl="" ;
		if(ii_bisnis_no!=1 || ii_bisnis_no!=4){
			if (Integer.parseInt(hub)!=1)
			{
				hsl="Khusus plan ini , hubungan pemegang polis dengan tertanggung adalah diri sendiri";
			}
		}
		return hsl;
	}
	
	public static void main(String[] args) {
		
	}
}
