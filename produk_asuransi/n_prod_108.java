// n_prod_108 SMART DEPOSIT RUPIAH
package produk_asuransi;
/*
 * Created on Aug 1, 2005
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
public class n_prod_108 extends n_prod{
	Query query = new Query();
	public n_prod_108()
	{
//		maxi investor SMART individu
		ii_bisnis_id = 108;
		ii_contract_period = 5;
		ii_age_from = 1;
		ii_age_to = 60;
		
		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="02";

//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 0 ;  //sekaligus


//		untuk hitung end date ( 79 - issue_date )
		ii_end_from = 5;
		ib_flag_end_age = false;

		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;

		idec_min_up02 = 1000 ;    //US$
		indeks_idec_list_premi=26;
		idec_list_premi = new double[indeks_idec_list_premi];
		for (int i =0 ; i< 25 ; i++)
		{	
			idec_list_premi[i] =1000 * i;
		}

		flag_uppremi=1;
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
	}
	
	public double of_get_rate1(int li_class, int flag_jenis_plan, int nomor_bisnis, int umurttg, int umurpp)
	{
		return idec_rate;	
	}	
	
	public boolean of_check_premi(double ad_premi)
	{
		if (ii_bisnis_no == 1)
		{
			if (ad_premi < 5000000)
			{
				return false;
			}
		}else{
			if (ii_bisnis_no == 2 || ii_bisnis_no == 3 )
			{
				switch (ii_pmode)
				{
					case 3:
						if (ad_premi < 3000000)
						{
							return false;
						}
						break;
					case 2:
						if (ad_premi < 1575000)
						{
							return false;
						}
						break;
					case 1:
						if (ad_premi < 810000)
						{
							return false;
						}
				}
			}
		}
		return true;
	}	

	public int of_get_conperiod(int number_bisnis)
	{
		return ii_contract_period;	
	}

	public double of_get_rate()
	{
		hsl="";
		err="";
		if (ii_bisnis_id >= 800)
		{
			int li_lbayar=0;
			li_cp=0;
			ldec_rate=0;
	
			if (ib_single_premium)
			{ 
				li_lbayar = 1 ;
			}else{ 
				li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
				if (ii_bisnis_id >= 800)
				{ 
					li_lbayar = ii_lbayar;
					ii_contract_period = li_lbayar;
				}
			}
			
			li_cp = ii_pmode;
			// Kalau triwulan, semester, bulanan, jadiin tahunan
			if (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6)
			{
				li_cp = 3;
			}

			try {
				Double result = query.selectNilai(ii_jenis, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, ii_contract_period, ii_tahun_ke, ii_age);
				
				if(result != null) {
				   ldec_rate = result.doubleValue();
				   if (ii_bisnis_id == 802 || ii_bisnis_id == 804)
				   { 
					   //kalau PC atau WPD, jangan dikali idec_add_pct, karna premi udah dikali idec_add_pct
					   idec_premi = idec_up * ldec_rate / ii_permil;
				   }else{
					   idec_premi = idec_up * ldec_rate * idec_add_pct / ii_permil;
				   }
				   idec_rate = ldec_rate;
				}else{	
					hsl="Tidak ada data rate";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 
		}
		if (ii_bisnis_id < 800)
		{
			 idec_premi_main = idec_premi;
		}
		return idec_rate;		
	}	

	public void of_set_bisnis_no(int ai_no)
	{
		ll_premi=0;
		indeks_li_pmode=4;
		li_pmode = new int[indeks_li_pmode];	

		ii_bisnis_no = ai_no;

		if (ii_bisnis_id < 800){
			if (ai_no == 1)
			{
				indeks_li_pmode=2;
				li_pmode[1] = 0;
			}else{
				indeks_li_pmode=4;
				li_pmode[1] = 3;
				li_pmode[2] = 2;
				li_pmode[3] = 1;
			}
			ii_pmode_list = li_pmode;
			indeks_ii_pmode_list=indeks_li_pmode;
			
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

		switch(ai_no){
			case 1:
				ll_premi = 5000000;
				break;
			case 2:
				ll_premi = 3000000;
				break;
			case 3:
				ll_premi = 3000000;
				break;
		}
		indeks_idec_list_premi=14;
		idec_list_premi = new double[indeks_idec_list_premi];	
		for (int i = 0 ; i < 13 ; i++)
		{
			idec_list_premi[i] = ll_premi + ( 1000000 * (i - 1) );
		}
	}

	public String of_alert_min_premi( double premi)
	{
		String hasil_min_premi="";
		boolean cek_premi=of_check_premi(premi);
		if (cek_premi==false)
		{
			hasil_min_premi="Premi Minimum untuk Plan ini : Sekaligus : 10.000.000,- ~ Tahunan : 3.000.000,- ~ Semesteran : 1.575.000,- ~ Triwulanan : 825.000,-" ;
		}
		return hasil_min_premi;
	}

	public void of_set_premi(double adec_premi)
	{
		ldec_up=0;
		ldec_sisa=0;

		if (ii_pmode == 0)
		{
			idec_up = (adec_premi / idec_add_pct ) * 1.5;
			idec_rate = 1500;
		}else{
			idec_up = (adec_premi / idec_add_pct ) * 5;
			idec_rate = 5000;
			
		}

		ldec_sisa = ( idec_up % 50 );
		if (ldec_sisa != 0) 
		{
			ldec_up = ldec_up - ldec_sisa;
		}
		of_set_up(idec_up);
		idec_premi_main = adec_premi;
	}

	public double f_get_bia_akui(int ar_lb, int ar_ke)
	{
		double ld_bia=0;
		switch (ar_lb)
		{
			case 1:
				if (ar_ke == 1)
				{
					ld_bia = 0.175;
				}
			break;
			case 5:
				if (ar_ke == 1) 
				{
					ld_bia = 0.7;
				}else{ 
					ld_bia = 0.05;
				}
			break;
		}
		
		return ld_bia;	
	}
	
	public static void main(String[] args) {
	}
}
