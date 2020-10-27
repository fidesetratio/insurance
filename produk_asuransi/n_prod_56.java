// n_prod_56 EKA SIMPONI US DOLLAR                                  
package produk_asuransi;
/*
 * Created on Jul 29, 2005
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
public class n_prod_56 extends n_prod{

	public n_prod_56()
	{
//		SK No. 007/EL-SK/IV/99 Simponi
//		eka simponi dollar
		ii_bisnis_id = 56;
		ii_contract_period = 8;
		ii_age_from = 1;
		ii_age_to = 66; //P. wahyudi 03/10/2000
		
		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="02";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 0;   //sekaligus
		
//		untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 8;
		ib_flag_end_age = false;

		idec_min_up02 = 1000;  //MARSUDI
		
		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];		
		ii_lama_bayar[0] = 1;

		indeks_idec_list_premi=101;
		idec_list_premi = new double[indeks_idec_list_premi];

		flag_uppremi=1;
	}

//	get period
	public int of_get_conperiod(int number_bisnis)
	{
		hsl="";
		err="";
		li_cp=0;

		li_cp = ii_contract_period;
		if ((ii_contract_period + ii_age) > 70)
		{
			li_cp = ii_end_from;
		}

		return li_cp;

	}

//	 set age
	public void of_set_age()
	{
		if (ib_flag_pp)
		{
			ii_age = ii_usia_pp;
		}else{
			ii_age = ii_usia_tt;
		}

//		Ditambah buat nge-cek usia pertanggungan hanya sampai 70 th (Hm)

		if ((ii_age + ii_contract_period) > 70)
		{
			//ii_contract_period = 70 - ii_age
			ii_end_from = 70 - ii_age;
		}
		
	}
	
	public static void main(String[] args) {
	}
}
