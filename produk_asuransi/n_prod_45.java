// n_prod_45 Super Protection
package produk_asuransi;

import com.ekalife.utils.FormatNumber;
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
public class n_prod_45 extends n_prod {

	public n_prod_45()
	{
//		SK No. 006/EL-SK/VII/98 Simas Super Protection
		ii_bisnis_id = 45;
		ii_contract_period = 1;
		ii_age_from = 1;
		ii_age_to = 60;  
		flag_class=1;
		
		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";
		
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
		ii_lama_bayar[0] = 1;
		
		idec_min_up01 = 15000000;  //Rp
		idec_min_up02 = 10000;     //US$	
		flag_uppremi=0;		
		
		idec_max_up01 = 1000000000;	//Rp
		idec_max_up02 = 100000;		//US$
		
		ii_class=1;

		flag_jenis_plan=4;
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];		

		flag_account = 2;// 0 untuk umum  1 untuk account recur 2 untuk rek client 3 untuk account recur dan rek client
	
	}

	public void get_class(int iiclass)
	{
		ii_class=iiclass;
	}

	public void of_hit_premi()
	{
		ldec_rate=0;
		switch(ii_class)
		{
			case 1 :
				ldec_rate = 3.8;
				break;
			case 2 :
				ldec_rate = 5.5;
				break;
			case 3 :
				ldec_rate = 7.1;
				break;
			case 4 :
				ldec_rate = 11.1;
				break;
			default :
				ldec_rate = 0;
				break;
		}

		if (ii_bisnis_id == 802 || ii_bisnis_id == 804)
		{
			//kalau PC atau WPD, jangan dikali idec_add_pct, karna premi udah dikali idec_add_pct
			idec_premi = idec_up * ldec_rate / ii_permil;
		}else{
			idec_premi = idec_up * ldec_rate * idec_add_pct / ii_permil;
		}
		
		idec_premi = FormatNumber.round(idec_premi, 0);
		
		idec_rate = ldec_rate;

		if (ii_bisnis_id < 800)
		{
			idec_premi_main = idec_premi;
		}
			
	}

	public static void main(String[] args) {

	}
}
