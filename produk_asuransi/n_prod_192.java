// n_prod_169 ekawaktu
package produk_asuransi;
import java.math.BigDecimal;

import com.ekalife.utils.f_hit_umur;

/**
 * @author HEMILDA
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_192 extends n_prod{
	Query query = new Query();
	public n_prod_192()
	{
		
		ii_bisnis_id = 192;
		ii_contract_period = 80;
		ii_age_from = 1;
		ii_age_to = 60;

		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		li_id = 0;


		
		ib_flag_end_age = false;
	  	idec_min_up01 = 50000000;

		indeks_ii_lama_bayar=3;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 5;
		ii_lama_bayar[1] = 10;
		ii_lama_bayar[2] = 1;

		indeks_idec_list_premi=14;
		idec_list_premi = new double[indeks_idec_list_premi];
		for( int i = 0 ; i< 13 ; i++)
		{
			idec_list_premi[i] = 3000000 + ( 1000000 * (i - 1) );
		}
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=6;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 3 ;  //tahunan
		ii_pmode_list[2] = 2 ;  //semester
		ii_pmode_list[3] = 1 ;  //Tri
		ii_pmode_list[4] = 6 ;  //Bulanan
		ii_pmode_list[5] = 0 ;  //sekaligus
		
		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1 ;// pmode 6		

		flag_account = 2;
		flag_uppremi= 0;
	}

	public int of_get_conperiod(int number_bisnis)
	{
		li_cp=0;
		li_cp =ii_contract_period-ii_age;
		ii_contract_period=ii_contract_period-ii_age;
		ii_end_from=li_cp;
		return li_cp;		
	}
	
	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
		indeks_li_pmode_list=6;
		li_pmode_list = new int[indeks_li_pmode_list];			

		if (ai_no ==3)
		{
			indeks_li_pmode_list=2;
			li_pmode_list[1] = 0 ;	
			ib_single_premium = true;
		}else{
			indeks_li_pmode_list=5;
			li_pmode_list[1] = 3;
			li_pmode_list[2] = 1;
			li_pmode_list[3] = 2;
			li_pmode_list[4] = 6;
		}
		ii_pmode_list = li_pmode_list;
		indeks_ii_pmode_list=indeks_li_pmode_list;

		if (ii_bisnis_id < 800){
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
	
	public void of_hit_premi()
	{
		hsl="";
		err="";
		String ls_sql;
		li_lbayar=0;
		li_cp = 0;
		ldec_rate=0;

		if (ib_single_premium )
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
//		   Kalau triwulan, semester, bulanan, jadiin tahunan
		if (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6 )
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
			err=(e.toString());
	  } 
	}

	public void of_set_premi(double adec_premi)
	{
		idec_premi_main = adec_premi;
		idec_premi = adec_premi;

		idec_rate = 1000;
		idec_up = adec_premi * idec_rate / 1000;
		of_set_up(idec_up);
	}

	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr =new f_hit_umur();
		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);

		if (bln<12 )
		{
			hsl="Usia Masuk Plan ini minimum : " + ii_age_from + " Tahun";
		}
		if(ii_age>ii_age_to){
			hsl="Usia Masuk Plan ini maximum : " + ii_age_to + " Tahun";
		}
		if (ii_usia_pp < 18)
		{
			hsl="Usia Pemegang Polis minimum : 18 Tahun !!!";
		}
		if(ii_usia_pp > 85)
		{
			hsl="Usia Pemegang Polis maximum : 85 Tahun !!!";
		}
		
		return hsl;		
	}

	public double of_get_min_up()
	{
		double ldec_1=0;
		
		if(this.is_kurs_id.equals("01")) { //RUPIAH
			if(ii_bisnis_no==1 || ii_bisnis_no==2){
				idec_min_up01=75000000;
			}
			else{
				idec_min_up01=50000000;
			}
		}
		
		ldec_1 = idec_min_up01;
		return ldec_1;
	}
	
	public double cek_awal()
	{
		if (is_kurs_id.equalsIgnoreCase("02") ) 
			li_id = 19;
		String err="";
		double hasil=0;
		try {
			Double result = query.selectDefault(li_id);
			
			if(result != null) {
				hasil = result.doubleValue();
			}
		   }
		 catch (Exception e) {
			   err=e.toString();
		 } 		
		return hasil; 
	}

	//pa
	public void count_rate_810(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp1=0;
		ldec_rate=0;
		try {
			Double result = query.selectRateRider("01", 0, klas, 810, nomor_produk);
			
			if(result != null) {
				hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi PA Tidak Ada !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 ldec_temp1 = ((unit * up / 1000) * hasil) * 0.1;
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp1);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp1=bd.doubleValue();	
			 ldec_rate1 = (hasil);
			 BigDecimal jm = new BigDecimal(ldec_rate1);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate1=jm.doubleValue();		
	}
	
	
	public void count_rate_813(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		double hasilPremi=0;
		ldec_temp4=0;
		ldec_rate4=0;

		try {
			Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, nomor_produk);
//			Double resultPremi = query.selectResultPremi(42, reg_spaj);
			
			if(result != null) {
				hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi CI Tidak Ada  !!!!";
				}
//			if(resultPremi != null) {
//				hasilPremi = resultPremi.doubleValue();
//				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 double factor_x=0;
			 
//			 if (persenUp==1){
//				 factor_x = 0.5;
//			 }else if(persenUp==2){
//				 factor_x = 0.6;
//			 }else if(persenUp==3){
//				 factor_x = 0.7;
//			 }else if(persenUp==4){
//				 factor_x = 0.8;
//			 }else if(persenUp==5){
//				 factor_x = 0.9;
//			 }else if(persenUp==6){
//				 factor_x = 1.0;
//			 }
			 
//			 if(persenUp != 0){
				 ldec_temp4 = (( up / 1000) * hasil ) * 0.1; // FIXIN last
//			 }else{
//				ldec_temp4 = hasilPremi;
//			 }
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp4);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp4=bd.doubleValue();	
			 ldec_rate4 =hasil;
			 BigDecimal jm = new BigDecimal(ldec_rate4);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate4=jm.doubleValue();			

	}	
	

	public void count_rate_814(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp5=0;
		ldec_rate5=0;
		

			int li_kali = 1;
			switch (pmode)
			{
				case 1 :
					li_kali = 4;
					break;	
				case 2 :
					li_kali = 2;
					break;
				case 6 :
					li_kali = 12 ;
					break;
			}	
			int li_umur_pp = 0;
	
			try {
				Double result = query.selectRateRider(kurs, umurttg, 1, kode_produk, nomor_produk);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Waiver TPD Tidak Ada !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 ldec_temp5 = ((premi * li_kali / 1000) * hasil) * 0.1;
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp5);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp5=bd.doubleValue();	
			 ldec_rate5 =hasil;
			 BigDecimal jm = new BigDecimal(ldec_rate5);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate5=jm.doubleValue();		

	}	
	
	public static void main(String[] args) {
	}
}
