// n_prod_81 Maxi Care
package produk_asuransi;
/*
 * Created on Jul 29, 2005
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
public class n_prod_81 extends n_prod {
	Query query = new Query();
	public n_prod_81()
	{
//		SK No. Sehat
		ii_bisnis_id = 81;
		ii_contract_period = 10;
		ii_age_from = 1;
		ii_age_to = 50;
		
		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 3  ; //tahunan
		
//		untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 10;
		ib_flag_end_age = false;

		indeks_ii_lama_bayar=10;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 1;
		ii_lama_bayar[2] = 1;
		ii_lama_bayar[3] = 1;
		ii_lama_bayar[4] = 1;
		ii_lama_bayar[5] = 5;
		ii_lama_bayar[6] = 5;
		ii_lama_bayar[7] = 5;
		ii_lama_bayar[8] = 5;
		ii_lama_bayar[9] = 5;
		
		flag_uppremi=0;
		flag_reff_bii = 1;
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		usia_nol = 0;
		//Yusuf - 20050203
		isProductBancass = true; 

	}

	public void of_hit_premi()
	{
		hsl="";
		err="";
		try {
			
			Double result = query.selectPremiSuperSehat(ii_bisnis_id, ii_bisnis_no, ii_age, is_kurs_id);
			
			 if(result != null) {
				idec_premi   = result.doubleValue();
				idec_rate = 1; //idec_up * ldec_rate * idec_add_pct / ii_permil

				if (ii_bisnis_id < 800)
				{
					idec_premi_main = idec_premi;				
				}
			 }else{
				hsl="Tidak ada data rate";

			 }
			}
		  catch (Exception e) {
				err=e.toString();
		  } 						
	}
	
	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
		indeks_li_pmode_list=2;
		li_pmode_list = new int[indeks_li_pmode_list];
		if (ai_no <= 5)    // lsdbs_number = 1 Sekaligus
		{
			li_pmode_list[1] = 0;
		}else{
			li_pmode_list[1] = 3;   //tahunan
		}
		ii_pmode_list = li_pmode_list;
		indeks_ii_pmode_list=indeks_li_pmode_list;

		ii_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		of_set_pmode(ii_pmode_list[1]);
		of_set_age();

		if ((ai_no - 5) > 0)
		{
			ai_no = ai_no - 5;
		}

		idec_min_up01 = 100000000 * ai_no;
		idec_min_up02 = 10000 * ai_no;
		
		li_pmode = new int[indeks_li_pmode];
		indeks_li_pmode=indeks_ii_pmode_list;
		li_pmode = new int[indeks_li_pmode];
		
		for (int i =1 ; i<indeks_li_pmode;i++)
		{
			li_pmode[i] = ii_pmode_list[i];
			
		}	
		of_set_up(idec_min_up01); 
	}

	public void of_set_kurs(String as_kurs)
	{
		is_kurs_id = as_kurs;
		ldec_up=0;
		int li_no=0;

		li_no = ii_bisnis_no;

		if ((li_no - 5) > 0)
		{
			li_no = li_no - 5;
		}	
	
		if (as_kurs == "01")
		{
			idec_min_up01 = 100000000 * li_no;
			of_set_up(idec_min_up01);
		}else{
			idec_min_up02 = 10000 * li_no;
			of_set_up(idec_min_up02);
		}
	}
	
	public static void main(String[] args) {

		
	}
}
