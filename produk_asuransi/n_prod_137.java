//137 maxi deposit(new)
package produk_asuransi;
import com.ekalife.utils.f_hit_umur;

public class n_prod_137 extends n_prod{

	/**
	 * @param args
	 */

	public n_prod_137()
	{
//		MAXI DEPOSIT (New)
		ii_bisnis_id = 137;
		ii_contract_period = 5;
		ii_age_from = 1;
		ii_age_to = 60;

		indeks_is_forex=1;
		is_forex = new String[indeks_is_forex];
		is_forex[0]="01";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 0;   //sekaligus
		
//		untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 5;
		ib_flag_end_age = false;

		indeks_ii_lama_bayar=7;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 1;
		ii_lama_bayar[2] = 1;// ini untuk maxi_deposit yang lsdbs_number nya 3
		ii_lama_bayar[3] = 1;
		ii_lama_bayar[4] = 1;
		ii_lama_bayar[5] = 1;
		ii_lama_bayar[6] = 1;// ini untuk maxi_deposit yang lsdbs_number nya 3
		
		

		idec_min_up01 = 25000000;  //Rp 25 juta s/d Rp. 1 milyar
		idec_max_up01 = 1000000000;
		flag_uppremi=1;
		usia_nol = 0;
		//Yusuf - 20050203
		//isProductBancass = true; 
		
	}	

	public double of_get_rate()
	{
		return idec_rate;	
	}
	
	public void of_set_premi(double adec_premi)
	{
		ldec_rate = 0;

		idec_premi = adec_premi;
		idec_up = adec_premi;
		ldec_rate = 1000;

		idec_rate = ldec_rate;

		of_set_up(idec_up);
	}	
	
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr =new f_hit_umur();
		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);

		hsl="";
		if (bln < 12)
		{
			hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
		}
		if (ii_age > ii_age_to)
		{
			hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
		}
		if (ii_usia_pp < 17)
		{
			hsl="Usia Pemegang Polis minimum : 17 Tahun !!!";
		}
		
		return hsl;		
	}
	
	@Override
	public void cek_flag_agen(int kode_produk, int no_produk, int flag_bulanan){
		
		switch (no_produk)
		{
			case 1:
				isProductBancass = true;
				break;
			case 2:
				isProductBancass = true;
				break;	
			case 3:
				isProductBancass = true;
				break;	
			case 4:
				isProductBancass = false;
				flag_as = 3;
				break;
			case 5:
				isProductBancass = true;
				break;	
			case 6:
				isProductBancass = true;
				break;	
			case 7:
				isProductBancass = false;
				flag_as = 3;
				break;
		}
	}
	
	public static void main(String[] args) {

	}

}
