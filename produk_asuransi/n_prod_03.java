// n_prod_03 SEUMUR HIDUP DENGAN BATASAN     
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
public class n_prod_03 extends n_prod{
	Query query = new Query();
	public n_prod_03()
	{
//		SK No. ? Seumur Hidup Tanpa Batasan
		ii_bisnis_id = 3;
		ii_contract_period = 99;
		ii_age_from = 20;
		ii_age_to = 55;

		indeks_is_forex=2;
		is_forex = new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";

		indeks_ii_pmode_list=4;
		indeks_ii_lama_bayar=4;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_pmode_list[1] = 1 ;  //triwulanan
		ii_pmode_list[2] = 2 ;  //semesteran
		ii_pmode_list[3] = 3 ;  //tahunan


		//di lst_det_bisnis, lsdbs_number cuma 1, 15pay life
//		di lst_det_bisnis, lsdbs_number cuma 1, 15pay life
		ii_lama_bayar[0] = 10;
		ii_lama_bayar[1] = 15;
		ii_lama_bayar[2] = 20;
		ii_lama_bayar[3] = 99;

		flag_uppremi=0;
		ii_end_from = 99;
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		usia_nol = 0;
	}



	public static void main(String[] args) {

		
	}
}
