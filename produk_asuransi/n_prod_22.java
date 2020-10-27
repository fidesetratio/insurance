package produk_asuransi;

// n_prod_22 20 PAY LIFE                   
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
public class n_prod_22 extends n_prod{
	
	public n_prod_22()
	{
//		SK No. 024/EL-SK/VII/96 (20 Pay Life)
	  ii_bisnis_id = 22;
	  ii_contract_period = 99;
	  ii_age_from = 1;
	  ii_age_to = 55;

	  indeks_is_forex=1;
	  is_forex = new String[indeks_is_forex];
	  is_forex[0]="02";
	  
//		1..4 cuma id, 0..3 di lst_cara_bayar
	  indeks_ii_pmode_list=4;
	  ii_pmode_list = new int[indeks_ii_pmode_list];
	  ii_pmode_list[1] = 1;   //triwulanan
	  ii_pmode_list[2] = 2;   //semesteran
	  ii_pmode_list[3] = 3;   //tahunan


	  indeks_ii_lama_bayar=4;
	  ii_lama_bayar = new int[indeks_ii_lama_bayar];
	  ii_lama_bayar[0] = 20;

	  ib_flag_end_age = false;
	  flag_uppremi=0;
	  
	  indeks_rider_list=0;
	  ii_rider = new int[indeks_rider_list];
  
	}

	public static void main(String[] args) {
	}
}
