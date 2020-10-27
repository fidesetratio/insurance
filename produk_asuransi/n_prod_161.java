// n_prod_161 SIMAS SEHAT
package produk_asuransi;
import java.math.BigDecimal;

import com.ekalife.utils.f_hit_umur;

/**
 * @author HEMILDA
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_161 extends n_prod {
	Query query = new Query();
	public n_prod_161()
	{
//		SK No. 022/EL-SK/XI/99 Super Sehat Plus
		ii_bisnis_id = 161;
		ii_contract_period = 1;
		ii_age_from = 1;
		ii_age_to = 65;

		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";

//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=3;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 3;   //tahunan
		ii_pmode_list[2] = 6;   //bulanan

//		untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from =1;
		ib_flag_end_age = false;

	  
		indeks_ii_lama_bayar=25;
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
		
		idec_min_up01 = 100000;  //Rp
		idec_max_up01 = 25000000;
		idec_min_up02 = 50;     //US$
		idec_max_up02 = 50;
		flag_uppremi=0;
		simas = 1;
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		
		flag_as = 3;
		
	}
	
	public void of_hit_premi()
	{
		hsl="";
		err="";
		try {
			Double result = query.selectPremiSuperSehat(ii_bisnis_id, ii_bisnis_no, ii_age, is_kurs_id);
			
			 if(result != null) {
				idec_premi   = result.doubleValue();
				//idec_rate = 1;
				//idec_up * ldec_rate * idec_add_pct / ii_permil

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
		
		indeks_li_pmode=2;
		li_pmode = new int[indeks_li_pmode];
		if (ii_bisnis_id < 800){
			if (ai_no <= 16)
			{
				indeks_li_pmode=2;
				li_pmode[1] = 3;
			}else{
				indeks_li_pmode=2;
				li_pmode[1] = 6;
			}
			ii_pmode_list = li_pmode;
			indeks_ii_pmode_list=indeks_li_pmode;
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
		idec_min_up01 = 100000 * ai_no;
		idec_max_up01 = 100000 * ai_no;
		idec_min_up02 = 50 * ai_no;		
		idec_max_up02 = 50 * ai_no;  
		
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
		
		if (min_up > up1)
		{
			hasil_min_up=" UP Minimum untuk plan ini : "+ min;	
		}
		/*if ( ii_bisnis_no > 4)
		{
			if (is_kurs_id.equalsIgnoreCase("02"))
			{
				hasil_min_up="SIMAS SEHAT untuk U$ maximum SIMAS SEHAT 4";
			}
		}*/
		return hasil_min_up;
	}

	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr =new f_hit_umur();
		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);

		hsl="";
		/*if (bln < 12)
		{
			hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
		}*/
		if (nomor_produk <= 16)
		{
			if (ii_age > ii_age_to)
			{
				hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
			}
			if (ii_usia_pp < 17)
			{
				hsl="Usia Pemegang Polis minimum : 17 Tahun !!!";
			}
		}else
			if (ii_age < 2)
			{
				hsl="Usia Masuk Plan ini minimum : 2" ;
			}{
			if (ii_age > 60)
			{
				hsl="Usia Masuk Plan ini maximum : 60";
			}
			if (ii_usia_pp < 17)
			{
				hsl="Usia Pemegang Polis minimum : 17 Tahun !!!";
			}
		}
		
		return hsl;		
	}

	public static void main(String[] args) {

	}
}
