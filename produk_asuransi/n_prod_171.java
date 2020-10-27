package produk_asuransi;

/**
/* n_prod_171 
 * Produk HCP Stand Alone
 * 1. Produk Bank Muamalat - HCP PLUS BMI (SAKINAH)
 * 2. Bancass 
 * 
 * @author Yusuf
 * @since Nov 27, 2008 (10:08:36 AM)
 */
public class n_prod_171 extends n_prod{

	public n_prod_171(){
		
		ii_bisnis_id = 171;
		ii_contract_period = 1;
		ii_age_from = 17;
		ii_age_to = 59;

		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";

		//1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 6;   //bulanan

		//untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 1;
		ib_flag_end_age = false;

		indeks_ii_lama_bayar=4;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 1;
		ii_lama_bayar[2] = 1;
		ii_lama_bayar[3] = 1;
		idec_min_up01 = 25000000;  //Rp
		idec_max_up01 = 25000000;
		flag_uppremi=0;
		flag_jenis_plan = 5;
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		flag_as = 3;
		isProductBancass = false;

		flag_account = 2; //harus isi account recur
	}

	public double of_get_min_up(){
		double ldec_1=0;
		if (ii_bisnis_no == 1){
			if (is_kurs_id.equalsIgnoreCase("01")){
				ldec_1 = 25000000;
			}else{
				ldec_1 = 2500;
			}
		}else if (ii_bisnis_no == 2){
			if (is_kurs_id.equalsIgnoreCase("01")){
					ldec_1 = 500000;
				}else{
					ldec_1 = 50;
				}
		}
		return ldec_1;
	}
	
	public void of_hit_premi()	{
		if (ii_bisnis_no == 1){
		idec_premi = 200000; //200 ribu
		idec_premi_main = idec_premi;
		//idec_premi = (idec_up * idec_rate) / ii_permil;
		}else if(ii_bisnis_no == 2){
			idec_premi = 1080000; //1080000
			idec_premi_main = idec_premi;
		}
	}
	
	//cek usia	
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk){
		hsl="";
		if (nomor_produk == 1){
	
			if (ii_age < ii_age_from){
				hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
			}
	
			if (ii_age > ii_age_to){
				hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
			}
	
			if (ii_usia_pp < 17){
				hsl="Usia Pemegang Polis mininum : 17 Tahun !!!";
			}
		}
		return hsl;		
	}
	
	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
		indeks_li_pmode_list=6;
		li_pmode_list = new int[indeks_li_pmode_list];			

		if (ai_no == 1)
		{
			indeks_li_pmode_list=2;
			li_pmode_list[1] = 6 ;	
			flag_account = 3;
		}else{
			indeks_li_pmode_list=2;
			li_pmode_list[1] = 3;
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
		
		if(ai_no == 1){
			flag_as = 3;
			isProductBancass = false;
		}else if(ai_no == 2){
			flag_as = 0;
			isProductBancass = true;
		}
	}
	
	public String cek_hubungan(String hub){
		hsl="" ;
		if (ii_bisnis_no == 1 && Integer.parseInt(hub)!=1){
			hsl="Khusus produk SAKINAH, hubungan Pemegang Polis dengan Peserta harus DIRI SENDIRI";
		}
		return hsl;
	}
	
}