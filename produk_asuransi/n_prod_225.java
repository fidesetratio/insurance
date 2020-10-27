package produk_asuransi;
import java.math.BigDecimal;

import produk_asuransi.n_prod.Query;

import com.ekalife.utils.f_hit_umur;

//n_prod_225 EKA SMiLe MEDICAL PLUS RI untuk Bank BUKOPIN
/*
 * Created on Okto 27, 2017
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Ridhaal
 * SMiLe MEDICAL PLUS 
 * 225 (1-6) - SMiLe MEDICAL PLUS RI (TERTANGGUNG UTAMA)
 * 846 - RIDER SMiLe MEDICAL PLUS RI (TERTANGGUNG I(UTAMA) - V)
 * 847 - RIDER SMiLe MEDICAL PLUS RJ,RG,RB,PK (TERTANGGUNG I(UTAMA) - V)
 * produk RG/RB/PK bisa di ambil jika mengambil RJ
 * Tertangung tambahan harus mengambil sama dengan yang di ambil tertanggung utama
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */


public class n_prod_225 extends n_prod {
	Query query = new Query();
	f_hit_umur umr = new f_hit_umur();
	public n_prod_225()
	{
		ii_bisnis_id = 225;
		ii_contract_period = 1;
		ii_age_from = 1;
		ii_age_to = 65;		
//		flag_uppremi = 1;
//		flag_default_up = 1;
		
		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";	
		
		indeks_ii_lama_bayar=6;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 1;
		ii_lama_bayar[2] = 1;
		ii_lama_bayar[3] = 1;
		ii_lama_bayar[4] = 1;
		ii_lama_bayar[5] = 1;
		
		
		
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 3;   //tahunan
		ii_pmode_list[2] = 2;   //semester
		ii_pmode_list[3] = 1;   //Tri		
		ii_pmode_list[4] = 6;   //bulanan	

		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.35; // pmode 1
		idec_pct_list[2] = 0.65; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.12; // pmode 6	
		ii_end_from = 61;
		li_sd = 75;
		simas = 1;
		//flag_worksite=1;
		//flag_as=3;
		flag_account=2;
		
		indeks_rider_list=2;
		ii_rider[0]=846;
		ii_rider[1]=847;
		
	}

	public void of_set_begdate(int thn, int bln, int tgl)
	{
		int li_month=0;

		if (ib_flag_end_age)
		{
			ii_end_age = ii_age;
		}
		li_month = 12;

		adt_bdate.set(thn,bln-1,tgl);
		
		idt_beg_date.set(thn,bln-1,tgl);		
		
		ldt_end.set(thn,bln-1,tgl);	
		ldt_end.add(idt_beg_date.MONTH,li_month);

		idt_end_date.set(thn,bln-1,tgl);			
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		idt_end_date.add(ldt_end.DAY_OF_MONTH , -1);

		ii_contract_period = ii_end_from - ii_end_age;
	}

//	of_set_usia_tt
	 public void of_set_usia_tt(int ai_tt)
	 {
		 ii_usia_tt = ai_tt;
	 }
	 
	 public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
		{
			f_hit_umur umr =new f_hit_umur();
			int hari=umr.hari(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
			hsl="";
			if (hari < 15)
			{
				hsl="Usia Masuk Plan ini minimum : 15 hari";
			}
			if (ii_age > ii_age_to)
			{
				hsl="Usia Masuk Plan ini maximum : " + ii_age_to+" tahun";
			}
			
			if (ii_usia_pp > 85) {
				hsl = "Usia Pemegang Polis maximum : 85 Tahun !!!"; 
			} else if (ii_usia_pp < 17) {
				hsl = "Usia Pemegang Polis minimum : 17 Tahun !!!";
			}
			if(ii_age==0 && hari>15){
				
				ii_age=1;
			}
			return hsl;		
		}
	 
	  public void of_set_bisnis_no(int ai_no)
	  {
		 ii_bisnis_no = ai_no;

		 ii_bisnis_no_utama = ii_bisnis_no;
		 ii_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		 of_set_pmode(ii_pmode_list[1]);
		  
		 

		indeks_li_pmode=indeks_ii_pmode_list;
		li_pmode = new int[indeks_li_pmode];
		
		for (int i =1 ; i<indeks_li_pmode;i++)
		{
			li_pmode[i] = ii_pmode_list[i];
			
		}		
	  }
	  
	  public String of_alert_min_up( double up1)
		{
			//do nothing hanya memberikan informasi tidak ada pesan error // up di set secara otomatis
		  	String hasil_min_up="";
			
			return hasil_min_up;
		}
	  
	  
	
	public void of_hit_premi()
	{
		hsl="";
		err="";
		if(ii_age==0)ii_age=1;
		try {
			
			Double result = query.selectPremiSuperSehat(ii_bisnis_id, ii_bisnis_no, ii_age, is_kurs_id);
			
			 if(result != null) {
				idec_premi   = result.doubleValue();
				
				if (ii_pmode == 1)
				{
					//triwulan
					idec_premi = idec_premi * idec_pct_list[1];
				}else{
					if (ii_pmode == 2)
					{
						//semesteran
						idec_premi = idec_premi * idec_pct_list[2];
					}else{
						if (ii_pmode == 6)
						{
							//bulanan
							idec_premi = idec_premi * idec_pct_list[6];
						}
					}
				}
				
				of_set_up(idec_premi);
				
				idec_premi_main = idec_premi;				
				of_set_up(idec_premi_main);
				
				idec_rate = 1; //idec_up * ldec_rate * idec_add_pct / ii_permil
				
			 }else{
				hsl="Tidak ada data rate";
			 }
			 
			 
		}
		catch (Exception e) {
			err=e.toString();
		} 						
	}
	
		
		
	public void of_set_up(double adec_up)
	{
		if(ii_bisnis_no%15 == 1){//plan Smile Medical Plus Cooper
			idec_up = 400000;
		}else if(ii_bisnis_no%15 == 2){//plan Smile Medical Plus Bronze
			idec_up = 800000;
		}else if(ii_bisnis_no%15 == 3){//plan Smile Medical Plus Silver
			idec_up = 1000000;
		}else if(ii_bisnis_no%15 == 4){//plan Smile Medical Plus Shappire
			idec_up = 1200000;
		}else if(ii_bisnis_no%15 == 5){//plan Smile Medical Plus Diamond
			idec_up = 1600000;
		}else if(ii_bisnis_no%15 == 6){//plan Smile Medical Plus Platinum
			idec_up = 2000000;
		}
	}
		
	public static void main(String[] args) {
	}
}

