// n_prod_83 Eka Simponi (new)
package produk_asuransi;
import java.math.BigDecimal;

import com.ekalife.utils.f_hit_umur;

/**
 * @author HEMILDA
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_83 extends n_prod{

	public n_prod_83()
	{
//		SK No. 011/EL-SK/VII/99 Eka Simponi
		ii_bisnis_id = 83;
		ii_contract_period = 5;
		ii_age_from = 1;
		ii_age_to = 67;
		
		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 0;   //sekaligus
		
//		untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 5;
		ib_flag_end_age = false;

		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];		
		ii_lama_bayar[0] = 1;
		


		indeks_idec_list_premi=101;
		idec_list_premi = new double[indeks_idec_list_premi];
		for (int i = 0; i < 100 ; i++)
		{
			idec_list_premi[i] = 1000000 * i;
		}
		flag_uppremi=1;
	}

	public double of_get_rate()
	{
		return idec_rate;	
	}

	public void of_set_premi(double adec_premi)
	{
		ldec_rate=0;

		idec_premi = adec_premi;
		ldec_rate = 666.667;
		idec_up =(idec_premi / ( ldec_rate)) ;
		int decimalPlace = 0;
		BigDecimal bd = new BigDecimal(idec_up);
		bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		idec_up = bd.doubleValue();
		idec_up = idec_up * ii_permil;
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
