package produk_asuransi;
//n_prod_59 JangkaWarsa
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
public class n_prod_59 extends n_prod{
	
	public n_prod_59()
	{
//		SK No. 029/EL-SK/X/96 Prolife
		ii_bisnis_id = 59;
		ii_contract_period = 5;
		ii_age_from = 1;
		ii_age_to = 55;
		
		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";		
		
//		  1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 0 ;  //sekaligus

//		1..4 : bisnis_no
		indeks_ii_lama_bayar=4;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 1;
		ii_lama_bayar[2] = 1;
		ii_lama_bayar[3] = 1;
		flag_uppremi=0;
	}	

	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
		if (ii_bisnis_id < 800)
		{
			ii_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			of_set_pmode(ii_pmode_list[1]);
		}else{
			if (ii_bisnis_id == 802 || ii_bisnis_id == 804) //PC or WPD
			{
				ii_age = ii_usia_pp;
				of_set_up(idec_premi_main);

			}else{
				of_set_age();
				of_set_up(idec_up);
			}
		}

		switch(ai_no)
		{
			case 1 :
				ii_contract_period = 5;
				break;
			case 2 :
				ii_contract_period = 10;
				break;
			case 3 :
				ii_contract_period = 15;
				break;		
			case 4 :
				ii_contract_period = 20;
				break;						
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
