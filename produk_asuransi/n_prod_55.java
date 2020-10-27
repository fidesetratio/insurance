// n_prod_55 EKA SIMPONI US DOLLAR                          
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
public class n_prod_55 extends n_prod{

	public n_prod_55()
	{
//		SK No. 007/EL-SK/IV/99 Simponi
//		eka simponi dollar
		ii_bisnis_id = 55;
		ii_contract_period = 5;
		ii_age_from = 1;
		ii_age_to = 55;
		
		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="02";
		
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

		flag_uppremi=1;
	}




	public static void main(String[] args) {
	}
}
