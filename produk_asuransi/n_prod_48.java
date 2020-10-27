// n_prod_48 EKALIFE PROTEKSI                                     
package produk_asuransi;
/*
 * 
 * Created on Jul 27, 2005
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
public class n_prod_48 extends n_prod {

	public n_prod_48()
	{
//		SK No. 007/EL-SK/IV/99 Simponi
		ii_bisnis_id = 48;
		ii_contract_period = 99;
		ii_age_from = 1;
		ii_age_to = 55;  

		
		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";

		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 3 ;  //tahunan
		
//		untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 100;

//		di lst_det_bisnis, lsdbs_number cuma 1, 5pay life
		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 20;
		idec_min_up01 = 0;  //Rp

		indeks_idec_list_premi=3;
		idec_list_premi= new double[indeks_idec_list_premi];
		idec_list_premi[0] = 1000000;
		idec_list_premi[1] = 2000000;
		idec_list_premi[2] = 3000000;
		
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];		
	}

//	 get rate
	public double of_get_rate()
	{
		return idec_rate;	
	}
	
	public static void main(String[] args) {

	}
}
