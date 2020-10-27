// n_prod_66 Procare
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
public class n_prod_67 extends n_prod{
	Query query = new Query();
	public n_prod_67()
	{
//		SK No. 022/EL-SK/XI/99 Super Sehat Plus
		ii_bisnis_id = 67;
		ii_contract_period = 4;
		ii_age_from = 1;
		ii_age_to = 56;

		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 0;   //sekaligus

		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];		
		ii_lama_bayar[0] = 1;

		ii_end_from = 4;
		ib_flag_end_age = false;

		idec_min_up01 = 100000;  //Rp
		idec_min_up02 = 50 ;    //US$
		
		flag_uppremi=0;
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

		  if (ii_bisnis_id < 800){
			  ii_bisnis_no_utama = ii_bisnis_no;
			  ii_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			  of_set_pmode(ii_pmode_list[1]);
		  }else{
			   if (ii_bisnis_id == 802 || ii_bisnis_id == 804)//PC or WPD
				  {
					  ii_age = ii_usia_pp;
					  of_set_up(idec_premi_main);	
				  }else{
					  of_set_age();
					  of_set_up(idec_up);
				  }
		  }
		idec_min_up01 = 100000 * ai_no;
		idec_max_up01 = 100000 * ai_no;
		idec_min_up02 = 50 * ai_no;		
		idec_max_up02 = 50 * ai_no;  
		
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
