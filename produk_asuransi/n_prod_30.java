// n_prod_30 SUPER WASIAT                          
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
public class n_prod_30 extends n_prod{
	Query query = new Query();
	public n_prod_30()
	{
//		SK No. 009/PL-09/III/95 Super Wasiat
		ii_bisnis_id = 30;
		ii_contract_period = 99;
		ii_age_from = 20;
		ii_age_to = 55;
		
		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=1;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 0 ;  //sekaligus

//		1..3 : bisnis_no
		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;  
		
		ib_flag_end_age = false;
		

		flag_uppremi=0;
	}

	public static void main(String[] args) {
	
	}
}
