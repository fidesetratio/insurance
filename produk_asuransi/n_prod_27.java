// n_prod_27 Prolife
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
public class n_prod_27 extends n_prod{
	Query query = new Query();
	public n_prod_27()
	{
//		SK No. 029/EL-SK/X/96 Prolife
		ii_bisnis_id = 27;
		ii_contract_period = 99;
		ii_age_from = 1;
		ii_age_to = 55;
		
		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 1;   //triwulanan
		ii_pmode_list[2] = 2;   //semesteran
		ii_pmode_list[3] = 3;   //tahunan
		ii_pmode_list[4] = 0;   //tahunan

//		1..3 : bisnis_no
		indeks_ii_lama_bayar=3;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 10;  //Prolife 10
		ii_lama_bayar[1] = 15;  //Prolife 15
		ii_lama_bayar[2] = 20;  //Prolife 20
		
		ib_flag_end_age = false;
		
		idec_min_up02 = 5000;
		flag_uppremi=0;
	}

	public void of_hit_premi()
	{
		hsl="";
		err="";
		li_lbayar=0;
		li_cp=0;
		ldec_rate=0;

		if (ib_single_premium)
		{
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		}else{ 
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			if (ii_bisnis_id >= 800)
			{
				li_lbayar = ii_lbayar;
				ii_contract_period = li_lbayar;			 
			}
		}
		
		switch(ii_bisnis_id)
		{
			case 19:
				ii_contract_period = ii_lama_bayar[ii_bisnis_no-1];
				break;
			case 20:	
				ii_contract_period = ii_lama_bayar[ii_bisnis_no-1];
				break;
		}

		li_cp = ii_pmode;
//		   Kalau triwulan, semester, bulanan, jadiin tahunan
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

	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;

		if (ai_no == 5){    // lsdbs_number = 5 Sekaligus
			indeks_li_pmode_list=2;
			li_pmode_list = new int[indeks_li_pmode_list];
			li_pmode_list[1]=0;
			ii_pmode_list = li_pmode_list;
			indeks_ii_pmode_list=indeks_li_pmode_list;
		}

		if (ii_bisnis_id < 800) 
		{
			ii_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			of_set_pmode(ii_pmode_list[1]);			 
		}else{
			if (ii_bisnis_id == 802 || ii_bisnis_id == 804) //PC & WPD
			{
				ii_age = ii_usia_pp;
				of_set_up(idec_premi_main);			
			}else{
				of_set_age();
				of_set_up(idec_up)	;			
			}
		}
		indeks_li_pmode=indeks_ii_pmode_list;
		li_pmode = new int[indeks_li_pmode];
		
		for (int i =1 ; i<indeks_li_pmode;i++)
		{
			li_pmode[i] = ii_pmode_list[i];
			
		}		
	}
	
	public static void main(String[] args) {
		/*testing of_hit_premi()			
		n_prod_27 b = new n_prod_27();
		b.ib_single_premium=false;
		b.ii_bisnis_no=2;
		b.ii_bisnis_id=802;
		b.is_kurs_id="01";
		b.ii_jenis=1;
		b.ii_tahun_ke=1;
		b.ii_age=33;
		b.ii_permil=1000;
		b.ii_pmode=3;
		b.ii_lbayar = 5;
		b.idec_add_pct=1;
		b.idec_up=10;
		b.of_hit_premi();*/		
	}
}
