package produk_asuransi;

// n_prod_19 Eka Waktu
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
public class n_prod_19 extends n_prod{
	
	public n_prod_19()
	{
//		SK No. ? Eka Waktu
	  ii_bisnis_id = 19;
	  ii_contract_period = 51;
	  ii_age_from = 1;
	  ii_age_to = 55;

	  indeks_is_forex=2;
	  is_forex = new String[indeks_is_forex];
	  is_forex[0]="01";
	  is_forex[1]="02";
	  
//		1..4 cuma id, 0..3 di lst_cara_bayar
	  indeks_ii_pmode_list=5;
	  ii_pmode_list = new int[indeks_ii_pmode_list];
	  ii_pmode_list[1] = 1;   //triwulanan
	  ii_pmode_list[2] = 2;   //semesteran
	  ii_pmode_list[3] = 3;   //tahunan
	  ii_pmode_list[4] = 4;   //5 tahunan

	  indeks_ii_lama_bayar=5;
	  ii_lama_bayar = new int[indeks_ii_lama_bayar];
	  ii_lama_bayar[0] = 5;
	  ii_lama_bayar[1] = 10;
	  ii_lama_bayar[2] = 15;
	  ii_lama_bayar[3] = 20;

	  ib_flag_end_age = false;
	  flag_uppremi=0;
	  
	  indeks_rider_list=4;
	  ii_rider = new int[indeks_rider_list];
	  ii_rider[0]=800;
	  ii_rider[1]=801;
	  ii_rider[2]=804;
	  ii_rider[3]=822;
	  
	  flag_as = 3;
	  
	}

	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
	
		if (ii_bisnis_id < 800)
		{
			ii_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			of_set_pmode(ii_pmode_list[1]);
			ii_age = ii_usia_tt;
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
		indeks_li_pmode=indeks_ii_pmode_list;
		li_pmode = new int[indeks_li_pmode];
		
		for (int i =1 ; i<indeks_li_pmode;i++)
		{
			li_pmode[i] = ii_pmode_list[i];
			
		}		
	}

	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		hsl="";
		of_get_payperiod();

		switch(li_lama)
		{
			case 5:
				if (ii_age <20)
				{
					hsl="Usia Masuk Plan ini minimum : 20";
				}else{
					if (ii_age >60)
					{
						hsl="Usia Masuk Plan ini maximum : 60";
					}
				}
				break;
			case 10:
				if (ii_age <20)
				{
					hsl="Usia Masuk Plan ini minimum : 20";
				}else{
					if (ii_age >55)
					{
						hsl="Usia Masuk Plan ini maximum : 55";
					}
				}
				break;	
			case 15:
				if (ii_age <20)
				{
					hsl="Usia Masuk Plan ini minimum : 20";
				}else{
					if (ii_age >55)
					{
						hsl="Usia Masuk Plan ini maximum : 55";
					}
				}
				break;						
		}
	
		return hsl;		
	}

	public int of_get_payperiod()
	{
		li_lama=0;
		li_lama = ii_lama_bayar[ii_bisnis_no-1];
		return li_lama;
	}

	public int of_get_conperiod(int number_bisnis)
	{
		li_cp=0;
		li_cp = ii_lama_bayar[ii_bisnis_no-1];

		return li_cp;		
	}
	
	public void of_set_begdate(int thn, int bln, int tgl)
	{
		int li_month = 0;

		adt_bdate.set(thn,bln-1,tgl);

		idt_beg_date.set(thn,bln-1,tgl);		

		if (ib_flag_end_age)
		{
			ii_end_age = ii_age;
		}
		li_month = ( li_cp ) * 12;
		
		idt_end_date.set(thn,bln-1,tgl);		
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		idt_end_date.add(idt_end_date.DAY_OF_MONTH , -1);

		//ii_contract_period = ii_end_from - ii_end_age;	
	}
	public static void main(String[] args) {
	}
}
