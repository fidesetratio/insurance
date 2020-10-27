// n_prod_111 Investa
package produk_asuransi;
import java.math.BigDecimal;
/*
 * Created on Aug 2, 2005
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
public class n_prod_111 extends n_prod{
	Query query = new Query();
	public n_prod_111()
	{
//		investa
		ii_bisnis_id = 111;
		ii_contract_period = 8;
		ii_age_from = 2;
		ii_age_to = 60;

		indeks_is_forex=1;
		is_forex = new String[indeks_is_forex];
		is_forex[0]="01";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 6 ;  //tahunan
		ii_pmode_list[2] = 2 ;  //tahunan
		ii_pmode_list[3] = 1 ;  //semester
		ii_pmode_list[4] = 3 ; //Tri
		
//		untuk hitung end date ( 79 - issue_date )
		ii_end_from = 8;
		ib_flag_end_age = false;
		idec_min_up01 = 7500000;

		indeks_ii_lama_bayar=2;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 5;
		ii_lama_bayar[1] = 5;

		indeks_idec_list_premi=14;
		idec_list_premi = new double[indeks_idec_list_premi];	
		for (int i =0 ; i < 13 ; i++)
		{
			idec_list_premi[i] = 3000000 + ( 1000000 * (i - 1) );
		}
		
		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1;
		flag_reff_bii = 1;		
		flag_uppremi=1;
		flag_account =2;
		indeks_rider_list=4;
		ii_rider = new int[indeks_rider_list];
		ii_rider[0]=803; //term
		ii_rider[1]=813; //CI
		ii_rider[2]=819; //HCPF
		ii_rider[3]=823; //MEDICAL
		flag_debet = 1;
		//Yusuf - 20050203
		isProductBancass = true; 
		
	}

	public String of_check_pa()
	{
		hsl="Plan Ini Tidak Boleh Ambil Rider PA";
		return hsl;
	}
	
	public String cek_max_up(Integer li_umur_ttg, Integer kode_produk, Double premi, Double up, Double fltpersen, Integer pmode_id,String kurs)
	{
		  String hasil_up="";
		  if(kode_produk==2){
			  double nil=(premi.doubleValue()/idec_pct_list[pmode_id])*2;
			  if(up>nil){
				  hasil_up="Up Maksimum untuk plan ini "+ f.format(nil);
			  }
		  }
		
		  return hasil_up;
	}

	public boolean of_check_premi(double ad_premi)
	{
		
	   if(ii_bisnis_no==1){
			switch (ii_pmode)
			{
			
			  
				case 3 :
					if (ad_premi < 4000000)
					{
						return false;
					}
					break;
				case 2:
					if ( ad_premi < 2625000)
					{
						return false;
					}
					break;
				case 1:
					if (ad_premi < 1350000)
					{
						return false;
					}
					break;
				case 6:
					if (ad_premi < 500000)
					{
						return false;
					}
					break; 	
			}
	   }else{
		   switch (ii_pmode)
			{
			
			  
				case 3 :
					if (ad_premi < 5000000)
					{
						return false;
					}
					break;
				case 2:
					if ( ad_premi < 2625000)
					{
						return false;
					}
					break;
				case 1:
					if (ad_premi < 1500000)
					{
						return false;
					}
					break;
				
			} 
	   }
		return true;
	}

	public String of_alert_min_premi( double premi)
	{
		String hasil_min_premi="";
		boolean cek_premi=of_check_premi(premi);
		if (cek_premi==false)
		{
			if(ii_bisnis_no==1){
				hasil_min_premi="Premi Minimum Plan ini : Bulanan : Rp 500.000 ~ Triwulanan: Rp 1.350.000 ~ Semesteran: Rp 2.625.000 ~ Tahunan : Rp 4.000.000";
			}else{
				hasil_min_premi="Premi Minimum Plan ini :  Triwulanan: Rp 1.500.000 ~ Semesteran: Rp 2.625.000 ~ Tahunan : Rp 5.000.000";
			}
		}
		return hasil_min_premi;
	}

//	public double of_get_max_up()
//	{
//		double ldec_1=0;
//		if(ii_bisnis_no==1){
//				if (ii_age>=2 || ii_age<=19)
//				{
//					ldec_1=150000000/idec_pct_list[ii_pmode];
//				}else{
//					if (ii_age>=20 || ii_age<=55)
//					{
//						ldec_1=225000000/idec_pct_list[ii_pmode];
//					}else{
//						if (ii_age>=56 || ii_age<=60)
//						{
//							ldec_1=150000000/idec_pct_list[ii_pmode];
//						}
//					}
//				}
//		}
//		return ldec_1;
//	}	

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
				Double result = query.selectNilaiNew(ii_jenis, ii_bisnis_id, ii_bisnis_no, is_kurs_id, li_cp, li_lbayar, 0, ii_tahun_ke, ii_age);
				
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
	
	public void of_set_premi(double adec_premi)
	{
		ldec_premi_tahunan=0;
		idec_premi = adec_premi;
		idec_rate = 2000;
		ldec_premi_tahunan = ((adec_premi * 1) / idec_add_pct);
		int decimalPlace = 2;
		BigDecimal bd = new BigDecimal(ldec_premi_tahunan);
		bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		ldec_premi_tahunan = bd.doubleValue();
		idec_up = ldec_premi_tahunan * idec_rate / 1000;
		if(idec_up<idec_min_up01){
			idec_up=7500000;
		}
		of_set_up(idec_up);
	}

	public static void main(String[] args) {
	}
}
