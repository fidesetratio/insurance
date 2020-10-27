package produk_asuransi;


// n_prod_20 Prosaver
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
public class n_prod_20 extends n_prod {

	public n_prod_20()
	{
//		SK No. 008/PL-08/X/94 Pro Saver
		ii_bisnis_id = 20;
		ii_contract_period = 99;
		ii_age_from = 1;
		ii_age_to = 60;

		indeks_is_forex=2;
		is_forex = new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";

//		1..4 cuma id, 0..3 di lst_cara_bayar
	  indeks_ii_pmode_list=6;
	  ii_pmode_list = new int[indeks_ii_pmode_list];
	  ii_pmode_list[1] = 0 ;  //sekaligus
	  ii_pmode_list[2] = 1 ;  //triwulanan
	  ii_pmode_list[3] = 2 ;  //semesteran
	  ii_pmode_list[4] = 3 ;  //tahunan
	  ii_pmode_list[5] = 4 ;  //5 tahunan

	  indeks_ii_lama_bayar=3;
	  ii_lama_bayar = new int[indeks_ii_lama_bayar];
	  ii_lama_bayar[0] = 10;
	  ii_lama_bayar[1] = 15;
	  ii_lama_bayar[2] = 20;
//
	  ib_flag_end_age = false;
	  flag_uppremi=0;
	}

	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
		ii_end_from = ii_lama_bayar[ai_no-1];
		
		if (ii_bisnis_id < 800) 
		{
			ii_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			of_set_pmode(ii_pmode_list[1]);			 
		}else{
			if (ii_bisnis_id == 802 || ii_bisnis_id == 804) //PC & WPD
			{
				ii_age = ii_usia_pp;
				of_set_up(idec_premi_main);			
			}else{
				of_set_age();
				of_set_up(idec_up)	;			
			}
		}
		indeks_li_pmode=indeks_ii_pmode_list;
		li_pmode = new int[indeks_li_pmode];
		
		for (int i =1 ; i<indeks_li_pmode;i++)
		{
			li_pmode[i] = ii_pmode_list[i];
			
		}		
	}


	
	public static void main(String[] args) {
	}
}
