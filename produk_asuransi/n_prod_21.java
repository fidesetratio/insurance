package produk_asuransi;

// n_prod_21 WHOLE LIFE                    
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
public class n_prod_21 extends n_prod{
	
	public n_prod_21()
	{
//		SK No. 008/PL-08/X/94 Whole Life
	  ii_bisnis_id = 21;
	  ii_contract_period = 99;
	  ii_age_from = 1;
	  ii_age_to = 55;

	  indeks_is_forex=2;
	  is_forex = new String[indeks_is_forex];
	  is_forex[0]="01";
	  is_forex[1]="02";
	  
//		1..4 cuma id, 0..3 di lst_cara_bayar
	  indeks_ii_pmode_list=5;
	  ii_pmode_list = new int[indeks_ii_pmode_list];
	  ii_pmode_list[1] = 0 ;  //sekaligus
	  ii_pmode_list[2] = 1 ;  //triwulanan
	  ii_pmode_list[3] = 2  ; //semesteran
	  ii_pmode_list[4] = 3 ;  //tahunan

	  indeks_ii_lama_bayar=1;
	  ii_lama_bayar = new int[indeks_ii_lama_bayar];
	  ii_lama_bayar[0] = 1;
	  flag_uppremi=0;
	  
	  indeks_rider_list=0;
	  ii_rider = new int[indeks_rider_list];
  
	}

//	 of_set_pmode
	public void of_set_pmode(int ai_pmode)
	{
		ii_pmode = ai_pmode;
		if (ii_pmode == 0){
			ib_single_premium = true ;
		}
		idec_add_pct = idec_pct_list[ii_pmode];
		if (ai_pmode != 0)
		{
			ii_lama_bayar[0] = 24;
		}
	}

	public static void main(String[] args) {
	}
}
