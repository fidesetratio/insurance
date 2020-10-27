package produk_asuransi;

/**
/* n_prod_170 Produk Bank Muamalat - PA Term Syariah (Ikhlas)
 * 
 * @author Yusuf
 * @since Nov 27, 2008 (10:08:36 AM)
 */
public class n_prod_170 extends n_prod{

	public n_prod_170(){
		
		ii_bisnis_id = 170;
		ii_contract_period = 1;
		ii_age_from = 17;
		ii_age_to = 59;

		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";

		//1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 3;   //tahunan

		//untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 1;
		ib_flag_end_age = false;

		indeks_ii_lama_bayar=4;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 1;
		ii_lama_bayar[2] = 1;
		ii_lama_bayar[3] = 1;
		idec_min_up01 = 15000000;  //Rp
		idec_max_up01 = 15000000;
		flag_uppremi=0;
		flag_jenis_plan = 5;
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		flag_as = 3;

		flag_account = 3; //harus isi account recur
	}

	public void of_hit_premi()	{
		idec_rate = 0.01; //1 per mil
		idec_premi = 150000; //150 ribu
		idec_premi_main = idec_premi;
		//idec_premi = (idec_up * idec_rate) / ii_permil;
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
	
	public String cek_hubungan(String hub){
		hsl="" ;
		if (Integer.parseInt(hub)!=1){
			hsl="Khusus produk IKHLAS, hubungan Pemegang Polis dengan Peserta harus DIRI SENDIRI";
		}
		return hsl;
	}
	
}
