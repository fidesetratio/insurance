// n_prod_80 PRIVASI
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
public class n_prod_80 extends n_prod{
	double ii_class;
	Query query = new Query();
	
	public n_prod_80()
	{
//		April 2003
//		privasi
		ii_class = 2;  //dipakai buat simpan jumlah unit
		ii_bisnis_id = 80;
		ii_contract_period = 20;
		ii_age_from = 17;
		ii_age_to = 50;

		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";

//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 6;   //bulanan
		ii_pmode_list[2] = 2;   //semester
		ii_pmode_list[3] = 1;   //tri
		ii_pmode_list[4] = 3;   //tahunan
			
//		untuk hitung end date ( 79 - issue_date )
		ii_end_from = 20;
		ib_flag_end_age = false;

		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 20;
		
		idec_kelipatan_up01=100000;
		flag_reff_bii = 1;
		flag_uppremi=1;
		flag_account =1;
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		flag_debet = 1;
		//Yusuf - 20050203
		isProductBancass = true; 
		
	}
	
	public boolean of_check_premi(double ad_premi)
	{
		return true;
	}

	public int of_get_conperiod(int number_bisnis)
	{
		return ii_contract_period;
	}	
	
	public double of_get_rate()
	{
		if (ii_bisnis_id >= 800)
		{
			li_lbayar=0;
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

	public void of_set_class(int ai_class)
	{
		ii_class = ai_class;
	}	
	
	public void of_set_premi(double adec_premi)
	{
		hsl="";
		err="";
		int li_kali = 1;
		idec_premi = adec_premi;
		double ldec_pct = idec_add_pct;
		if (ii_pmode == 6)
		{
			li_kali = 1;
			ldec_pct = 1;
		}
//		ii_class = adec_premi / ( 100000 * li_kali * ldec_pct)
//		idec_premi = ii_class * 100000 * li_kali * ldec_pct
		idec_premi = adec_premi;
		try {

			Double result = query.selectNilai(ii_jenis, ii_bisnis_id, is_kurs_id, 3, ii_contract_period, ii_contract_period, ii_tahun_ke, ii_age);
			
			if(result != null) {
				idec_rate= result.doubleValue();
				idec_up = (adec_premi / ldec_pct * 12/li_kali * 1000 / idec_rate);
				of_set_up(idec_up);
		   }

		} catch (Exception e) {
			   err=e.toString();
		 } 
	}	

	
	public static void main(String[] args) {
	}
}
