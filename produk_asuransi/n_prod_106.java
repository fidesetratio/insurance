package produk_asuransi;
import com.ekalife.utils.f_hit_umur;

/**
 * @author HEMILDA
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_106 extends n_prod{

	public n_prod_106()
	{
//		smart save (= power save 94)
		ii_bisnis_id = 106;
		ii_contract_period = 4;
		ii_age_from = 1;
		ii_age_to = 68;

		indeks_is_forex=2;
		is_forex = new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";

//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 0;   //sekaligus

		indeks_ii_lama_bayar=1;		
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		idec_min_up01=10000000;
		idec_min_up02=1000;
		flag_account =2;
		ib_flag_end_age = false;
		ii_end_from = 4;
		flag_uppremi=1;
		isProductBancass = true; 
		usia_nol = 0;
	}

	public double of_get_rate()
	{
		return idec_rate;	
	}
	
	public void of_set_kurs(String as_kurs)
	{
		is_kurs_id = as_kurs;
		indeks_idec_list_premi=17;
		idec_list_premi = new double[indeks_idec_list_premi];
		if (as_kurs == "02")
		{
			for (int i = 0 ; i <16 ; i++)
			{
				idec_list_premi[i] = 1000 + ( 100 * (i -1) );
			}
		}else{
			for (int i = 0 ; i <16 ; i++)
			{
				idec_list_premi[i] = 10000000 + ( 1000000 * (i - 1) );
			}
		}
	}	

	public void of_set_premi(double adec_premi)
	{
		idec_premi = adec_premi;
		idec_rate = 1000;
		of_set_up(adec_premi);
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
		if (ii_usia_pp > 85)
		{
			hsl="Usia Pemegang Polis maximum : 85 Tahun !!!"; // permintaan Achmad Anwarudin  November 05, 2007 12:44 PM
		}
		if (ii_usia_pp < 17)
		{
			hsl="Usia Pemegang Polis minimum : 17 Tahun !!!";
		}
		
		return hsl;		
	}

	public static void main(String[] args) {
		
	}
}
