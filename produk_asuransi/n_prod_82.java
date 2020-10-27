// n_prod_82 PRO-INVESTOR (new)
package produk_asuransi;
import java.math.BigDecimal;
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
public class n_prod_82 extends n_prod{
	Query query = new Query();
	public n_prod_82()
	{
//		SK Proinvestor baru
//		us dollar
		ii_bisnis_id = 82;
		ii_contract_period = 12;
		ii_age_from = 1;
		ii_age_to = 60;
		
		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=3;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 0;   //sekaligus
		ii_pmode_list[2] = 3;   //triwulanan
		
//		untuk hitung end date ( 79 - issue_date )
		ii_end_from = 8;
		ib_flag_end_age = false;
		idec_min_up02 = 0;
		idec_min_up01 = 0;

		indeks_ii_lama_bayar=3;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 3;
		ii_lama_bayar[2] = 5;

		indeks_idec_list_premi=16;
		idec_list_premi = new double[indeks_idec_list_premi];
	    for (int i = 0 ;i< 15;i++)
		{
			idec_list_premi[i] = 2000000 + ( 1000000 * i );
		}
		flag_uppremi=0;
	}

	public int of_get_conperiod(int number_bisnis)
	{
		return ii_contract_period;
	}

	public double of_get_rate()
	{
		return idec_rate;	
	}

	public double of_get_tunai(int ai_th_ke)
	{
		hsl=""; 
		err="";
		li_lbayar = 0 ;
		li_cp = 0;
		ldec_rate = 0;
		double ldec_tunai = 0;

		if (ib_single_premium)
		{
			li_lbayar = 1; 
		}else{
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			if (ii_bisnis_id >= 800)
			{
				li_lbayar = ii_lbayar;
				ii_contract_period = li_lbayar;
			}
		}

		li_cp = ii_pmode;

		try {
			
			Double result = query.selectNilai(2, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, ii_contract_period, ai_th_ke, ii_age);
			
			if(result != null) {
			   ldec_rate = result.doubleValue();
				ldec_tunai = (ldec_rate * idec_up / ii_permil) / 3;				
			}else{
				hsl="Tidak ada data rate";			
			}
		   }
		 catch (Exception e) {
			   err=e.toString();
		 } 				
		return ldec_tunai;		
	}
	
	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;

		indeks_li_pmode_list=2;
		li_pmode_list = new int[indeks_li_pmode_list];	
		int[] li_kontrak;

		li_kontrak = new int[3];

		li_kontrak[0] = 8;  //sekaligus
		li_kontrak[1] = 10;  //3 tahunan
		li_kontrak[2] = 12; //5 tahunan

		ii_contract_period = li_kontrak[ai_no-1];
		ii_end_from = ii_contract_period;

		if (ai_no == 1)    // lsdbs_number = 1 Sekaligus
		{
			li_pmode_list[1] = 0;
		}else{
			li_pmode_list[1] = 3 ; 
		}
		ii_pmode_list = li_pmode_list;
		indeks_ii_pmode_list=indeks_li_pmode_list;
		ii_pmode = ii_pmode_list[1];

		ii_age_to = 60;
		if (ai_no == 3)
		{
			ii_age_to = 58;
		}

		of_set_age();
		of_set_up(idec_up);

		ii_pay_period = ii_lama_bayar[ai_no-1];

		indeks_li_pmode=indeks_ii_pmode_list;
		li_pmode = new int[indeks_li_pmode];
		
		for (int i =1 ; i<indeks_li_pmode;i++)
		{
			li_pmode[i] = ii_pmode_list[i];
			
		}
		
	}	

	public void of_set_kurs(String as_kurs)
	{
		int ll_lipat;
		indeks_idec_list_premi=16;
		idec_list_premi = new double[indeks_idec_list_premi];	
		if (as_kurs == "02")
		{
			for (int i = 0 ; i< 15;i++)
			{
				idec_list_premi[i] = 400 + ( 100 * i );
			}
		}else{
			for (int i = 1 ; i< 15 ; i++)
			{
				idec_list_premi[i] =  2000000 + ( 1000000 * i );
			}
		}
	}
	
	public void of_set_premi(double adec_premi)
	{
		hsl="";
		err="";
		li_lbayar=0;
		li_cp=0;
		ldec_rate=0;
		li_ltanggung=0;
		double ldec_max;
		li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		li_ltanggung = ii_contract_period;
		
		li_cp = ii_pmode;

		try {
			Double result = query.selectNilai(1, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, li_ltanggung, ii_tahun_ke, ii_age);
			
			if(result != null) {
			   ldec_rate = result.doubleValue();
			   idec_premi = adec_premi;  
			   if (ldec_rate > 0)
			   {
				    idec_up = (3 * idec_premi * ii_permil / ( ldec_rate));	
					int decimalPlace = 0;
					BigDecimal bd = new BigDecimal(idec_up);
					bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
					idec_up = bd.doubleValue();
					
					ldec_max = 100000;
					if (is_kurs_id.equalsIgnoreCase("01"))
					{
						ldec_max = 1000000000;
					}
					idec_up = Math.min(idec_up, ldec_max);
			   }
			   idec_rate = ldec_rate;
			   of_set_up(idec_up)	;			   
			}else{	
				hsl="Tidak ada data rate"; 	
			}
			
		   }
		 catch (Exception e) {
			   err=e.toString();
		 } 
	}	

	public static void main(String[] args) {
	}
}
