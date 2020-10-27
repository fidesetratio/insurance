package produk_asuransi;
import java.math.BigDecimal;

import com.ekalife.utils.f_hit_umur;

//n_prod_204 HCP PROVIDER SYARIAH
/*
 * Created on May 23, 2012
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Andhika
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_204 extends n_prod{
	Query query = new Query();
	f_hit_umur umr = new f_hit_umur();
	public n_prod_204()
	{
		ii_bisnis_id = 204;
		ii_contract_period = 1;
		ii_age_from = 2;
		ii_age_to = 64;		
		
		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";	
		
		indeks_ii_lama_bayar=49;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 1;
		ii_lama_bayar[2] = 1;
		ii_lama_bayar[3] = 1;
		ii_lama_bayar[4] = 1;
		ii_lama_bayar[5] = 1;
		ii_lama_bayar[6] = 1;
		ii_lama_bayar[7] = 1;
		ii_lama_bayar[8] = 1;
		ii_lama_bayar[9] = 1;
		ii_lama_bayar[10] = 1;
		ii_lama_bayar[11] = 1;
		ii_lama_bayar[12] = 1;
		ii_lama_bayar[13] = 1;
		ii_lama_bayar[14] = 1;
		ii_lama_bayar[15] = 1;
		ii_lama_bayar[16] = 1;
		ii_lama_bayar[17] = 1;
		ii_lama_bayar[18] = 1;
		ii_lama_bayar[19] = 1;
		ii_lama_bayar[20] = 1;
		ii_lama_bayar[21] = 1;
		ii_lama_bayar[22] = 1;
		ii_lama_bayar[23] = 1;
		ii_lama_bayar[24] = 1;
		ii_lama_bayar[25] = 1;
		ii_lama_bayar[26] = 1;
		ii_lama_bayar[27] = 1;
		ii_lama_bayar[28] = 1;
		ii_lama_bayar[29] = 1;
		ii_lama_bayar[30] = 1;
		ii_lama_bayar[31] = 1;
		ii_lama_bayar[32] = 1;
		ii_lama_bayar[33] = 1;
		ii_lama_bayar[34] = 1;
		ii_lama_bayar[35] = 1;
		ii_lama_bayar[36] = 1;
		ii_lama_bayar[37] = 1;
		ii_lama_bayar[38] = 1;
		ii_lama_bayar[39] = 1;
		ii_lama_bayar[40] = 1;
		ii_lama_bayar[41] = 1;
		ii_lama_bayar[42] = 1;
		ii_lama_bayar[43] = 1;
		ii_lama_bayar[44] = 1;
		ii_lama_bayar[45] = 1;
		ii_lama_bayar[46] = 1;
		ii_lama_bayar[47] = 1;
		ii_lama_bayar[48] = 1;
		
		
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 3;   //tahunan
		ii_pmode_list[2] = 2;   //semester
		ii_pmode_list[3] = 1;   //Tri		
		ii_pmode_list[4] = 6;   //bulanan	

		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1; // pmode 6	
		ii_end_from = 61;
		li_sd = 60;
		simas = 1;
		//flag_worksite=1;
		//flag_as=3;
		flag_account=2;
		
		indeks_rider_list=1;
		ii_rider[0]=826;
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
			int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
			int hari=umr.hari(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);

			hsl="";
			if(nomor_produk>=25 && nomor_produk<=36){
				if (hari < 15)
				{
					hsl="Usia Masuk Plan ini minimum : " + ii_age_from+" tahun";
				}
			}else{
				if (bln < 12)
				{
					hsl="Usia Masuk Plan ini minimum : " + ii_age_from+" tahun";
				}
			}
			
			if (ii_age > ii_age_to)
			{
				hsl="Usia Masuk Plan ini maximum : " + ii_age_to+" tahun";
			}
			
			if (ii_usia_pp > 90) {
				hsl = "Usia Pemegang Polis maximum : 90 Tahun !!!"; 
			} else if (ii_usia_pp < 17) {
				hsl = "Usia Pemegang Polis minimum : 17 Tahun !!!";
			}
			
			return hsl;		
		}
	 
	  public void of_set_bisnis_no(int ai_no)
	  {
		  ii_bisnis_no = ai_no;

		  if (ii_bisnis_id < 800){
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
		 

		indeks_li_pmode=indeks_ii_pmode_list;
		li_pmode = new int[indeks_li_pmode];
		
		for (int i =1 ; i<indeks_li_pmode;i++)
		{
			li_pmode[i] = ii_pmode_list[i];
			
		}		
	  }
	  
	  public String of_alert_min_up( double up1)
		{
			double min_up=of_get_min_up();
			String hasil_min_up="";
			String min="";
			BigDecimal bd = new BigDecimal(min_up);
			min=bd.setScale(2,0).toString();
			
			if(ii_bisnis_no%12  == 1){//plan R-100
				idec_up = 100000;
			}else if(ii_bisnis_no%12  == 2){//plan R-200
				idec_up = 200000;
			}else if(ii_bisnis_no%12  == 3){//plan R-300
				idec_up = 300000;
			}else if(ii_bisnis_no%12  == 4){//plan R-400
				idec_up = 400000;
			}else if(ii_bisnis_no%12  == 5){//plan R-500
				idec_up = 500000;
			}else if(ii_bisnis_no%12  == 6){//plan R-600
				idec_up = 600000;
			}else if(ii_bisnis_no%12  == 7){//plan R-700
				idec_up = 700000;
			}else if(ii_bisnis_no%12  == 8){//plan R-800
				idec_up = 800000;
			}else if(ii_bisnis_no%12  == 9){//plan R-900
				idec_up = 900000;
			}else if(ii_bisnis_no%12  == 10){//plan R-1000
				idec_up = 1000000;
			}else if(ii_bisnis_no%12  == 11){//plan R-1000
				idec_up = 1500000;
			}else if(ii_bisnis_no%12  == 0){//plan R-1000
				idec_up = 2000000;
			}
			up1= idec_up;
			if (min_up > up1)
			{
				hasil_min_up=" UP Minimum untuk plan ini : "+ min;	
			}
			
			return hasil_min_up;
		}
	  
	  public double of_get_min_up()
		{
			double ldec_1=0;
			if (is_kurs_id.equalsIgnoreCase("01") )
			{
				if(ii_bisnis_no%12 == 1){//plan R-100
					idec_up = 100000;
				}else if(ii_bisnis_no%12 == 2){//plan R-200
					idec_up = 200000;
				}else if(ii_bisnis_no %12 == 3){//plan R-300
					idec_up = 300000;
				}else if(ii_bisnis_no%12  == 4){//plan R-400
					idec_up = 400000;
				}else if(ii_bisnis_no%12  == 5){//plan R-500
					idec_up = 500000;
				}else if(ii_bisnis_no%12  == 6){//plan R-600
					idec_up = 600000;
				}else if(ii_bisnis_no%12 == 7){//plan R-700
					idec_up = 700000;
				}else if(ii_bisnis_no%12  == 8){//plan R-800
					idec_up = 800000;
				}else if(ii_bisnis_no%12  == 9){//plan R-900
					idec_up = 900000;
				}else if(ii_bisnis_no%12  == 10){//plan R-1000
					idec_up = 1000000;
				}else if(ii_bisnis_no%12  == 11){//plan R-1000
					idec_up = 1500000;
				}else if(ii_bisnis_no%12  == 0){//plan R-1000
					idec_up = 2000000;
				}
				ldec_1 = idec_up;
				of_set_up(idec_up);
			}else{
				ldec_1 = idec_min_up02;
			}
			return ldec_1;
		}
	
	public void of_hit_premi()
	{
		hsl="";
		err="";
		
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
				
				idec_rate = 1; //idec_up * ldec_rate * idec_add_pct / ii_permil

				if (ii_bisnis_id < 800)
				{
					idec_premi_main = idec_premi;				
				}
			 }else{
				hsl="Tidak ada data rate";
			 }
			 
			if(ii_bisnis_no%12 == 1){//plan R-100
				idec_up = 100000;
			}else if(ii_bisnis_no%12 == 2){//plan R-200
					idec_up = 200000;
			}else if(ii_bisnis_no%12 == 3){//plan R-300
					idec_up = 300000;
			}else if(ii_bisnis_no%12 == 4){//plan R-400
				idec_up = 400000;
			}else if(ii_bisnis_no%12 == 5){//plan R-500
				idec_up = 500000;
			}else if(ii_bisnis_no%12 == 6){//plan R-600
				idec_up = 600000;
			}else if(ii_bisnis_no%12 == 7){//plan R-700
				idec_up = 700000;
			}else if(ii_bisnis_no%12 == 8){//plan R-800
				idec_up = 800000;
			}else if(ii_bisnis_no%12 == 9){//plan R-900
				idec_up = 900000;
			}else if(ii_bisnis_no%12 == 10){//plan R-1000
				idec_up = 1000000;
			}else if(ii_bisnis_no%12 == 11){//plan R-1000
				idec_up = 1500000;
			}else if(ii_bisnis_no%12 == 0){//plan R-1000
				idec_up = 2000000;
			}
				of_set_up(idec_up);
			 
		}
		catch (Exception e) {
			err=e.toString();
		} 						
	}
	
	public static void main(String[] args) {
	}
}
