// n_prod_01 Ekawarsa
package produk_asuransi;
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
public class n_prod_01 extends n_prod{
	Query query = new Query();
	public n_prod_01()
	{
//		SK No. ? Eka Warsa
		ii_bisnis_id = 1;
		ii_contract_period = 1;
		ii_age_from = 17;
		ii_age_to = 65;

		indeks_is_forex=2;
		is_forex = new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";

		indeks_ii_pmode_list=2;
		indeks_ii_lama_bayar=1;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_pmode_list[1] = 0;   //sekaligus  (ii_pmode)

//		di lst_det_bisnis, lsdbs_number cuma 1, 15pay life
		ii_lama_bayar[0] = 1;
		ii_end_from = 1;
		ib_flag_end_age = false;
		flag_uppremi=0;
		
		indeks_rider_list=1;
		ii_rider = new int[indeks_rider_list];
		ii_rider[0]=800;
		usia_nol = 0;
	}

	public void of_hit_premi()
	{
		li_lbayar=0;
		li_cp=0;
		li_umur = 0;
		ldec_rate=0;
		hsl="";
		err="";

		if (ib_single_premium)
		{
			li_lbayar = 1; 
		}else{ 
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			if (ii_bisnis_id >= 800){ 
				li_lbayar = ii_lbayar;
				ii_contract_period = li_lbayar;
			}
		}

		switch(ii_bisnis_id)
		{
		case 19 :
			ii_contract_period = ii_lama_bayar[ii_bisnis_no-1];
			break;
		case 20 :
			ii_contract_period = ii_lama_bayar[ii_bisnis_no-1];
			break;
		}

//		   Kalau triwulan, semester, bulanan, jadiin tahunan
		li_cp = 3;
		li_umur = ii_age;
		if (li_umur < 20)
		{
			li_umur = 20;
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
			err=(e.toString());
	  } 
	}

	public static void main(String[] args) {

		
	}
}
