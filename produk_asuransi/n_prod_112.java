package produk_asuransi;
import com.ekalife.utils.f_hit_umur;

/**
 * @author HEMILDA
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_112 extends n_prod{

	public n_prod_112()
	{
//		Simponi 8
		ii_bisnis_id = 112;
		ii_contract_period = 8;
		ii_age_from = 1;
		ii_age_to = 57;

		indeks_is_forex=1;
		is_forex = new String[indeks_is_forex];
		is_forex[0]="02";

//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;	
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 0 ;  //sekaligus
		
//		untuk hitung end date ( 79 - issue_date )
		ii_end_from = 8;
		ib_flag_end_age = false;
		idec_min_up02=1000;
		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		flag_uppremi=1;
		isBonusTahapan=true;
		//Yusuf - 20050203
		isInvestasi = true;
		usia_nol = 0;
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
	
	public static void main(String[] args) {
	}
}
