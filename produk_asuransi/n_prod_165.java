//n_prod_165 Investimax
package produk_asuransi;
import java.math.BigDecimal;

import com.ekalife.utils.f_check_end_aktif;
import com.ekalife.utils.f_hit_umur;

/*
 * Created on Sep 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Hemilda
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_165 extends n_prod{

	public n_prod_165()
	{
		ii_bisnis_id = 165;
		ii_contract_period = 5;
		ii_age_from = 1;
		ii_age_to = 65;

		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0] = "01";
		
//		  1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 0 ;  //sekaligus

		indeks_li_pmode=6;
		li_pmode = new int[indeks_li_pmode];

//		  untuk hitung end date ( 79 - issue_date )
		ii_end_from = 5;
		ib_flag_end_age = false;
	  	idec_min_up01 = 15000000;
		idec_max_up01 = 100000000;

		indeks_ii_lama_bayar=5;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;

		indeks_idec_list_premi=20;
		idec_list_premi= new double[indeks_idec_list_premi];
		for (int i = 0 ; i < 13 ;i++)
		{
			idec_list_premi[i] = 3000000 + ( 1000000 * (i - 1) );
		}
		
		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.26; // pmode 1
		idec_pct_list[2] = 0.51; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.09; // pmode 6
		
		flag_uppremi=1;
		
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		isProductBancass = true; 
		flag_account =2;
		flag_rider=1;												

		isInvestasi = true;
		usia_nol = 0;
		
		   indeks_kombinasi = 19;
		   kombinasi = new String[indeks_kombinasi];
		   kombinasi[0] = "K"; // pp 95% - ptb 5%
		   kombinasi[1] = "B"; // pp 90% - ptb 10%
		   kombinasi[2] = "L"; // pp 85% - ptb 15%
		   kombinasi[3] = "C"; // pp 80% - ptb 20%
		   kombinasi[4] = "M"; // pp 75% - ptb 25%
		   kombinasi[5] = "D"; // pp 70% - ptb 30%
		   kombinasi[6] = "N"; // pp 65% - ptb 35%
		   kombinasi[7] = "E"; // pp 60% - ptb 40%
		   kombinasi[8] = "O"; // pp 55% - ptb 45%
		   kombinasi[9] = "F"; // pp 50% - ptb 50%
		   kombinasi[10] = "P"; // pp 45% - ptb 55%
		   kombinasi[11] = "G"; // pp 40% - ptb 60%
		   kombinasi[12] = "Q"; // pp 35% - ptb 65%
		   kombinasi[13] = "H"; // pp 30% - ptb 70%
		   kombinasi[14] = "R"; // pp 25% - ptb 75%
		   kombinasi[15] = "I"; // pp 20% - ptb 80%
		   kombinasi[16] = "S"; // pp 15% - ptb 85%
		   kombinasi[17] = "J"; // pp 10% - ptb 90%
		   kombinasi[18] = "T"; // pp 5% - ptb 95%
		   kode_flag=12;
		   flag_as = 0;
		   flag_jenis_plan=8;
		   flag_cuti_premi = 1;
		   
		   indeks_rider_list=5;
			ii_rider = new int[indeks_rider_list];
			ii_rider[0]=820;
			ii_rider[1]=822;
			ii_rider[2]=823;
			ii_rider[3]=825;
			ii_rider[4]=832;
	}
	
	
	public double of_get_max_up()
	{
		double ldec_1=0;
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			ldec_1 = idec_max_up01;
		}else{
			ldec_1 = idec_max_up02;
		}
		if (ii_age >=61 && ii_age <=65) // Achmad Anwarudin  
		{
			ldec_1 = 50000000;
		}
		return ldec_1;
	}	
	
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr =new f_hit_umur();
		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);

		hsl="";
		if (bln < 6)
		{
			hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
		}
		if (ii_age > ii_age_to)
		{
			hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
		}
		if (ii_usia_pp < 17)
		{
			hsl="Usia Pemegang Polis minimum : 17 Tahun !!!";
		}
			
		if (ii_usia_pp > 80)
		{
			hsl="Usia Pemegang Polis maximum : 80 Tahun !!!";
		}
		
		return hsl;		
	}	
	public double of_get_fltpersen(Integer pmode_id)
	{
		double fltpersen =1;
		if (pmode_id.intValue()==1){
			fltpersen = 4; 
		}else{
			if (pmode_id.intValue()==2){
				fltpersen = 2; 
			}else{
				if (pmode_id.intValue()==3){
					fltpersen = 1; 
				}else{
					if (pmode_id.intValue()==6){
						fltpersen = 12; 
					}
				}
			}
		}
			return fltpersen ;
	}
	

	
	public boolean of_check_premi(double ad_premi)
	{
	
		return true;
	}		
	
	public int of_get_conperiod(int number_bisnis)
	{
		li_cp=0;
		li_cp =ii_contract_period;
		
		return li_cp;		
	}	
	
//	get pay period
	  public int of_get_payperiod()
	  {
		  if (ib_single_premium)
		  {
				ii_lama_bayar[0] = 1;
		  }else{
				ii_lama_bayar[0] = (ii_end_from - ii_usia_tt);
		  }
		ii_pay_period = ii_lama_bayar[0];
		return ii_lama_bayar[0];
	  }	

//	get rate
	 public double of_get_rate()
	 {
		 return idec_rate;	
	 }

	public void of_set_begdate(int thn, int bln, int tgl)
	{
		int li_month=0;

		if (ib_flag_end_age)
		{
			ii_end_age = ii_age;
		}
		li_month = ( ii_end_from - ii_end_age ) * 12;

		adt_bdate.set(thn,bln-1,tgl);

		idt_beg_date.set(thn,bln-1,tgl);		

		idt_end_date.set(thn,bln-1,tgl);			
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		//idt_end_date.add(idt_end_date.DAY_OF_MONTH , -1);
		
		f_check_end_aktif a = new f_check_end_aktif();
		a.end_aktif(idt_end_date.YEAR, idt_end_date.MONTH, idt_end_date.DAY_OF_MONTH, idt_beg_date.YEAR , idt_beg_date.MONTH, idt_beg_date.DAY_OF_MONTH);
		ii_contract_period = ii_end_from - ii_end_age;
	}

	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;

		if (ii_bisnis_id < 800)
		{
			if (ai_no == 1 || ai_no == 2)
			{
				indeks_li_pmode=2;
				li_pmode[1] = 0;
//			}else{
//				indeks_li_pmode=5;
//				li_pmode[1] = 3;
//				li_pmode[2] = 2;
//				li_pmode[3] = 1;
//				li_pmode[4] = 6;
			}
			ii_pmode_list = li_pmode;
			indeks_ii_pmode_list=indeks_li_pmode;
			ii_bisnis_no_utama = ii_bisnis_no;
			ii_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			of_set_pmode(ii_pmode_list[1]);
		}
	}

//	of_set_kurs
	 public void of_set_kurs(String as_kurs)
	 {
		is_kurs_id = as_kurs;
		if (ii_bisnis_no == 1 || ii_bisnis_no == 3|| ii_bisnis_no == 5)
		{
			indeks_idec_list_premi=17;
			for (int i = 0; i< 16 ; i++)
			{
				idec_list_premi[i] = 900 + ( 100 * i );
			}
		}else{
			indeks_idec_list_premi=19;
			for (int i = 0 ; i< 18; i++)
			{
				idec_list_premi[i] = 200 + ( 100 * i );
			}
		}
	 }

	public void of_set_premi(double adec_premi)
	{
		idec_premi_main = adec_premi;
		idec_premi = adec_premi;
		
		idec_rate = 1250;
		
		idec_up = adec_premi * idec_rate / 1000;
		
		of_set_up(idec_up);
	}	 
	 	
	public double f_get_bia_akui(int ar_lb, int ar_ke)
	{
		double ld_bia=0;
		if (ar_lb==1)
		{
			if (ar_ke==1)
			{
				ld_bia=0.1;
			}
		}else{
			if (ar_lb>1)
			{
				if (ar_ke==1)
				{
					ld_bia=1;	
				}else if (ar_ke==2)
					{
						ld_bia=0.55;
					}else if (ar_ke==3)
						{
							ld_bia=0.35;
						}else if (ar_ke==4)
						{
							ld_bia=0.15;
						}else if (ar_ke ==5)
							{
								ld_bia=0.05;
							}else if (ar_ke >=6)
							{
								ld_bia=0;
							}

			}
		}
		return ld_bia;	
	}		 	

	public double cek_bia_akui(int kode_produk,int number_produk,int cara_bayar,int period,int ke)
	{
		if ( cara_bayar == 1 ||  cara_bayar == 2 ||  cara_bayar == 6 )
		{
			 cara_bayar = 3;
		}
		if (cara_bayar != 0)
		{
			period = 88;	
		}
		
		double ld_bia=0;
		try {
			
			Double result = query.select_biaya_akuisisi(kode_produk, number_produk, cara_bayar, ke, period);
			if(result != null) {
				ld_bia= result.doubleValue();
			}
		   }
		 catch (Exception e) {
			   err=e.toString();
		 } 			
		return ld_bia;
	}
	
	public double cek_awal()
	{
		String err="";
		double hasil=0;
		if (is_kurs_id.equalsIgnoreCase("02") ) 
		{
			hasil = new Double(5);
		}else{
			hasil= new Double(30000);
		}
	
		return hasil; 
	}

	public double cek_rate(int li_bisnis,int li_pmode,int pperiod,int li_insper,int umur_tt , String kurs,int insperiod)
	{
		String err="";
		double hasil=0;
		try {
			Double result = query.selectNilai(1, 115, "01", 3, 80, 80, 1, umur_tt);
			
			if(result != null) {
				hasil=result.doubleValue();
			}
		   }
		 catch (Exception e) {
			   err=e.toString();
		 } 		
		 hasil= new Double(0);
		return hasil; 
	}
	
	public void biaya2(double ld_bia_ass, double premi, int ldec_pct, double up)
	{
		mbu_jumlah2=((up/1000) * ld_bia_ass) * 0.1;
		int decimalPlace = 3;
		BigDecimal bd = new BigDecimal(mbu_jumlah2);
		bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		mbu_jumlah2 = bd.doubleValue();		
		mbu_persen2=ld_bia_ass/10;
		BigDecimal jm = new BigDecimal(mbu_persen2);
		jm= jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);		
		mbu_persen2=jm.doubleValue();			
	}

	public void biaya3(double ldec_awal)
	{
		mbu_jumlah3=ldec_awal;
		int decimalPlace = 3;
		BigDecimal bd = new BigDecimal(mbu_jumlah3);
		bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		mbu_jumlah3=bd.doubleValue();		
	}
	
	
	public String min_total_premi(Integer pmode_id, double premi , String kurs)
	{
		String hsl="";
		double total_premi = premi ;
		if (total_premi < 50000000)
		{
			hsl="Minimum Total Premi Sekaligus : Rp. 50.000.000,-";
		}
	return hsl;
	}
	
	
	public String min_topup(Integer pmode_id, double premi , double premi_topup, String kurs, int jenis_topup)
	{
		String hsl="";
		if (premi_topup < 35000000)
		{
			hsl="Minimum Top-Up Tunggal : Rp. 35.000.000.";
		}
		
		double total_premi = premi + premi_topup;
		if (total_premi < 50000000)
		{
			hsl="Minimum Total Premi Sekaligus : Rp. 50.000.000,-";
		}
		return hsl;
	}
	
	public String cek_rider_number(int nomor_produk,int kode_produk,int umurttg, int umurpp, double up, double premi, int pmode,int hub, String kurs, int pay_period)
	{
		String hsl="";

		return hsl;
	}
	

	public static void main(String[] args) {

	}
}
