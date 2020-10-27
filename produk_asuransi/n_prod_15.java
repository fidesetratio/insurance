// n_prod_15 DWIGUNA BERTAHAP IDEAL                                                                                                                                
package produk_asuransi;
/*
 * Created on Jul 26, 2005
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
public class n_prod_15 extends n_prod{
	Query query = new Query();
	public n_prod_15()
	{
//		SK No. ? Dwiguna Bertahap Ideal
		ii_bisnis_id = 15;
		ii_contract_period = 18;
		ii_age_from = 20;
		ii_age_to = 47;

		indeks_is_forex=2;
		is_forex = new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";
		ib_flag_pp = true;
		indeks_ii_pmode_list=4;
		indeks_ii_lama_bayar=1;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_pmode_list[1] = 1 ;  //triwulanan
		ii_pmode_list[2] = 2 ;  //semesteran
		ii_pmode_list[3] = 3 ;  //tahunan

		ii_lama_bayar[0] = 18;

		ib_flag_end_age = false;

		flag_uppremi=0;
		ii_end_from = 18;
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		usia_nol = 0;
	}

	
	public static void main(String[] args) {

		
	}
}
