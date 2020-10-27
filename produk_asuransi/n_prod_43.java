// n_prod_43 SIMAS SEHAT HARIAN            
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
public class n_prod_43 extends n_prod {

	public n_prod_43()
	{
//		SK No. 006/EL-SK/VII/98 Simas Sehat Harian
		ii_bisnis_id = 43;
		ii_contract_period = 1;
		ii_age_from = 17;
		ii_age_to = 60;  
		
		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 3;   //tahunan
		
//		untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 1;
		ib_flag_end_age = false;		

//		di lst_det_bisnis, lsdbs_number cuma 1, 5pay life
		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
//		di Detail bisnis ada 6, sama semua
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 1;
		ii_lama_bayar[2] = 1;
		ii_lama_bayar[3] = 1;
		ii_lama_bayar[4] = 1;
		ii_lama_bayar[5] = 1;

		flag_uppremi=0;		
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];		
	}

	public void of_hit_premi()
	{
		hsl="";
		err="";

		try {
			Double result = query.selectlst_premi_43(ii_bisnis_no,ii_sex_tt,ii_age);
			
			if(result != null) {
				idec_premi = result.doubleValue();
			}
			}
		  catch (Exception e) {
			err=e.toString();
		  } 
		  idec_rate = 0;

		  if (ii_bisnis_id < 800) 
		  {
			  idec_premi_main = idec_premi;		  
		 }
		  
	}

	
//	set bisnis no 
	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
		if (ii_bisnis_id < 800)
		{
			ii_lbayar = ii_lama_bayar[ii_bisnis_no];
			of_set_pmode(ii_pmode_list[1]);
		}else if  (ii_bisnis_id == 802 || ii_bisnis_id == 804)//PC & WPD
			{
			ii_age = ii_usia_pp;
			of_set_up(idec_premi_main);
			}else{
				of_set_age();
				of_set_up(idec_up);
			}
		//
		switch (ai_no)
		{
			case 1:
				idec_min_up01 = 100000;
				break;
			case 2:
				idec_min_up01 = 150000;
				break;
			case 3:
				idec_min_up01 = 200000;
				break;
			case 4:
				idec_min_up01 = 300000;
				break;
			case 5:
				idec_min_up01 = 400000;
				break;
			case 6:
				idec_min_up01 = 500000;
				break;
		}
		
	}
	
	
	

	public static void main(String[] args) {

	}
}
