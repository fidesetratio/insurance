//n_prod_76 Multi Invest
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
public class n_prod_76 extends n_prod{
	long ll_premi;
	Query query = new Query();
	public n_prod_76()
	{
//		SK No. 007/EL-SK/IV/99 multi invest baru
//		us dollar
		ii_bisnis_id = 76;
		ii_contract_period = 8;
		ii_age_from = 1;
		ii_age_to = 60;

		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";

//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=4;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 3;   //tahunan
		ii_pmode_list[2] = 2;   //semester
		ii_pmode_list[3] = 1;   //Tri

//		untuk hitung end date ( 79 - issue_date )
		ii_end_from = 8;
		ib_flag_end_age = false;
		idec_min_up01 = 15000000;
//
		indeks_ii_lama_bayar=3;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 5;
		ii_lama_bayar[1] = 5;
		ii_lama_bayar[2] = 5;

		indeks_idec_list_premi=14;
		idec_list_premi = new double[indeks_idec_list_premi];
	  	for (int i =0 ; i < 13 ; i++)
	  	{
			idec_list_premi[i] = 3000000 + ( 1000000 * (i - 1) );
	  	}
		flag_uppremi=1;
		
		indeks_rider_list=4;
		ii_rider = new int[indeks_rider_list];
		ii_rider[0]=813;
		ii_rider[1]=818;
		ii_rider[2]=819;
		ii_rider[3]=822;
	}

	public String of_check_pa()
	{
		hsl="Plan Ini Tidak Boleh Ambil Rider PA ";
		return hsl;
	}	

	public boolean of_check_premi(double ad_premi)
	{
		/*switch (ii_pmode)
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
				break;
		}*/
		if (ii_pmode==3)
		{		
			switch (ii_bisnis_no)
			{
				case 1: 
					if (ad_premi > 20000000 || ad_premi < 3000000) 
					{
						return false;		
					}
					break;
				case 2:
					if (ad_premi < 20000001 || ad_premi > 75000000)
					{
						return false;				
					}
					break;
				case 3:
					if (ad_premi < 75000001)
					{
						return false;
					}
					break;
			}
		}else{
			if (ii_pmode==1)
			{		
				switch (ii_bisnis_no)
				{
					case 1: 
						if (ad_premi > 5400000 || ad_premi < 810000)
						{
							return false;		
						}
						break;
					case 2:
						if (ad_premi < 5400001 || ad_premi > 20250000)
						{
							return false;				
						}
						break;
					case 3:
						if (ad_premi < 20250001)
						{
							return false;
						}
						break;
				}
			}else{
				if (ii_pmode==2)
				{		
					switch (ii_bisnis_no)
					{
						case 1: 
							if (ad_premi > 10500000 || ad_premi < 1575000)
							{
								return false;		
							}
						break;
						case 2:
							if (ad_premi < 10500001 || ad_premi > 39375000)
							{
								return false;				
							}
						break;
						case 3:
							if (ad_premi < 39375001)
							{
								return false;
							}
						break;
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
		return idec_rate;		
	}	
	
	public void of_set_bisnis_no(int ai_no)
	{
		ll_premi = 0;
		ii_bisnis_no = ai_no;

		if (ii_bisnis_id < 800)
		{
			ii_bisnis_no_utama = ii_bisnis_no;
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

		switch (ai_no)
		{
			case 1:
				ll_premi = 1000000;
				break;
			case 2:
				ll_premi = 21000000;
				break;
			case 3:
				ll_premi = 76000000;
				break;
		}
		indeks_idec_list_premi=14;
		idec_list_premi = new double[indeks_idec_list_premi];	
		for (int i = 0 ; i < 13; i++)
		{
			idec_list_premi[i] = ll_premi + ( 1000000 * (i - 1) );
		}
		
		indeks_li_pmode=indeks_ii_pmode_list;
		li_pmode = new int[indeks_li_pmode];
		
		for (int i =1 ; i<indeks_li_pmode;i++)
		{
			li_pmode[i] = ii_pmode_list[i];
			
		}		
	}	
	
	public void of_set_premi(double adec_premi)
	{
		ldec_up = 0;
		ldec_sisa = 0;

		idec_premi = adec_premi;
		ldec_up = (adec_premi / idec_add_pct ) * 5;

		ldec_sisa = ( ldec_up  % 50 );
		if (ldec_sisa != 0)
		{
			ldec_up = ldec_up - ldec_sisa;
		}
		idec_up = ldec_up;

		idec_rate = 5000;
		of_set_up(idec_up);
		idec_premi_main = adec_premi;		
	}		

	public String of_alert_min_premi( double premi)
	{
		String hasil_min_premi="";
		boolean cek_premi=of_check_premi(premi);
		if (cek_premi==false)
		{
			hasil_min_premi="Premi Minimum untuk Plan ini :Tahunan : 3.000.000,- ~ Semesteran : 2.000.000,- ~ Triwulanan : 1.500.000,- ~ Silver s/d 20jt, Gold s/d 75jt, Platinum > 75jt";
		}
		return hasil_min_premi;
	}

	public static void main(String[] args) {

	}
}
