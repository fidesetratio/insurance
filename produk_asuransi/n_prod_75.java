package produk_asuransi;
import com.ekalife.utils.f_hit_umur;

/**
 * @author HEMILDA
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_75 extends n_prod{

	public n_prod_75()
	{
//		SK No. 007/EL-SK/IV/99 Super invest
//		us dollars
		ii_bisnis_id = 75;
		ii_contract_period = 8;
		ii_age_from = 1;
		ii_age_to = 67;  // tadinya 65

		indeks_is_forex=1;
		is_forex=new String[indeks_is_forex];
		is_forex[0]="02";

//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 0;   //sekaligus

//		untuk hitung end date ( 79 - issue_date )
		ii_end_from = 8;
		ib_flag_end_age = false;
		idec_min_up02 = 500;
		idec_min_up01 = 0;

		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;

		indeks_idec_list_premi=101;
		idec_list_premi = new double[indeks_idec_list_premi];
		for(int i = 0 ;i < 100; i++)
		{
			idec_list_premi[i] = 400 + ( 100 * i );
		}
		usia_nol = 0;
		flag_uppremi=1;
	}

	public int of_get_conperiod(int number_bisnis)
	{
		li_cp=0;

		li_cp = ii_contract_period;
		/*if (ii_contract_period + ii_age > 70)
		{
			li_cp = ii_end_from;
		}*/
		if (70-ii_age<8)
		{
			li_cp =70-ii_age;
		}
		//ii_contract_period=li_cp;
		return li_cp;		
	}

	public void of_set_age()
	{
		if (ib_flag_pp)
		{
			ii_age = ii_usia_pp;
		}else{
			ii_age = ii_usia_tt;
		}	
//		Ditambah buat nge-cek usia pertanggungan hanya sampai 70 th (Hm)

	  if (ii_age + ii_contract_period > 70)
	  {
		ii_end_from = 70 - ii_age;
	  }
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
