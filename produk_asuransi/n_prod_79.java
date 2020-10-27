// n_prod_79 SEHAT
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
public class n_prod_79 extends n_prod{

	Query query = new Query();
	public n_prod_79()
	{
//		SK No. Sehat
		indeks_idec_pct_list=8;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[7] = 0.09; // pmode 6
		ii_bisnis_id = 79;
		ii_contract_period = 55;
		ii_age_from = 1;
		ii_age_to = 50;

		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";

//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 3 ;  //tahunan
		
//		untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 55;

		indeks_ii_lama_bayar=12;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];		
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 3;
		ii_lama_bayar[2] = 5;
		ii_lama_bayar[3] = 1;
		ii_lama_bayar[4] = 3;
		ii_lama_bayar[5] = 5;
		ii_lama_bayar[6] = 1;
		ii_lama_bayar[7] = 3;
		ii_lama_bayar[8] = 5;
		ii_lama_bayar[9] = 1;
		ii_lama_bayar[10] = 3;
		ii_lama_bayar[11] = 5;
		flag_reff_bii = 1;
		flag_uppremi=0;
		flag_account =1;
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];

		//Yusuf - 20050203
		isProductBancass = true; 
		flag_debet = 1;
	}

	public boolean of_check_premi(double ad_premi)
	{
		if (ii_pmode == 3)  // TAHUNAN
		{
			if (ad_premi < 5000000)
			{
				return false;
			}
		}else if  (ii_pmode == 6) // BULANAN
			{
			if (ad_premi < 500000)
			{
				return false;
			}
		}
		return true;
	}
	
	public void of_hit_premi()
	{
		hsl="";
		err="";
		int li_bagi =0;
		double li_sisa =0;
		try {

			Double result = query.selectPremiSuperSehat(ii_bisnis_id, ii_bisnis_no, ii_age, is_kurs_id);
			
			 if(result != null) {
				idec_premi   = result.doubleValue();
				idec_rate = 1;
				if (ii_pmode != 0 || ii_pmode != 3) 
				{
					li_bagi = 1000;
					if (ii_pmode != 6)
					{
						li_bagi = 100;
					}
					idec_premi = Math.ceil(idec_premi * idec_add_pct);	
					li_sisa = (idec_premi % li_bagi);
					if (li_sisa > 0)
					{
						idec_premi = idec_premi - li_sisa + li_bagi;
					}
					idec_premi = Math.max(idec_premi, 200000);
				}


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

	public void of_set_begdate(int thn, int bln, int tgl)
	{
		int li_month=0;
		ii_end_age = ii_age;

		li_month = ( ii_end_from - ii_end_age ) * 12;

		adt_bdate.set(thn,bln-1,tgl);
		
		idt_beg_date.set(thn,bln-1,tgl);		
		
		ldt_end.set(thn,bln-1,tgl);			
		ldt_end.add(idt_beg_date.MONTH,li_month);
		
		idt_end_date.set(thn,bln-1,tgl);			
		idt_end_date.add(idt_beg_date.MONTH,li_month);

		if (adt_bdate.DAY_OF_MONTH == ldt_end.DAY_OF_MONTH)
		{
			idt_end_date.add(ldt_end.DAY_OF_MONTH , -1);
		}
		ii_contract_period = ii_end_from - ii_end_age;
	}

	public void of_set_bisnis_no(int ai_no)
	{
		indeks_li_pmode_list=5;
		li_pmode_list= new int[indeks_li_pmode_list];	

		ii_bisnis_no = ai_no;

		if (ai_no == 1 || ai_no == 4 || ai_no == 7 || ai_no == 10)    // lsdbs_number = 1 Sekaligus
		{
			indeks_li_pmode_list=2;
			li_pmode_list[1] = 0;
		}else{
			indeks_li_pmode_list=5;
			li_pmode_list[1] = 6;   //bulanan
			li_pmode_list[2] = 1;   //tri
			li_pmode_list[3] = 2;   //semester
			li_pmode_list[4] = 3;   //tahunan
		}
		ii_pmode_list = li_pmode_list;
		indeks_ii_pmode_list=indeks_li_pmode_list;

		ii_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		of_set_pmode(ii_pmode_list[1]);
		of_set_age();

		if (ai_no<=3)
		{
			idec_min_up01 = 10000000;
		}else{
			if (ai_no <= 6)
			{
				idec_min_up01 = 25000000;
			}else{
				if (ai_no <= 9)
				{
					idec_min_up01 = 50000000;
				}else{
					idec_min_up01 = 100000000;
				}
			}
		}
		li_pmode = new int[indeks_li_pmode];
		indeks_li_pmode=indeks_ii_pmode_list;
		li_pmode = new int[indeks_li_pmode];
		
		for (int i =1 ; i<indeks_li_pmode;i++)
		{
			li_pmode[i] = ii_pmode_list[i];
			
		}		
		of_set_up(idec_min_up01);
	}


	
	public static void main(String[] args) {
	}
}
