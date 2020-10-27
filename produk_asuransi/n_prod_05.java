// n_prod_05 DWIGUNA HARI TUA                                        
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
public class n_prod_05 extends n_prod{
	Query query = new Query();
	public n_prod_05()
	{
//		SK No. ? Dwiguna Hari Tua
		ii_bisnis_id = 5;
		ii_contract_period = 60;
		ii_end_age = 50;
		ii_age_from = 20;
		ii_age_to = 55;

		indeks_is_forex=1;
		is_forex = new String[indeks_is_forex];
		is_forex[0]="01";


		indeks_ii_pmode_list=4;
		indeks_ii_lama_bayar=3;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_pmode_list[1] = 1 ;  //triwulanan
		ii_pmode_list[2] = 2 ;  //semesteran
		ii_pmode_list[3] = 3 ;  //tahunan


		ii_lama_bayar[0] = 10;
		ii_lama_bayar[1] = 15;
		ii_lama_bayar[2] = 20;
		//
		ib_flag_end_age = false;

		flag_uppremi=0;
		ii_end_from = 60;
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		usia_nol = 0;
	}



	public static void main(String[] args) {

		
	}
}
