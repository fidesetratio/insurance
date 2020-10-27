//n_prod 168 - Smart medicare
package produk_asuransi;

import java.math.BigDecimal;

import com.ekalife.utils.f_hit_umur;

/**
 * End Care ini, kombinasi antara dana sejahtera (nilai tunainya) dan simas sehat (preminya)
 * 
 * @author Yusuf
 * @since Oct 10, 2008 (10:06:46 AM)
 */
public class n_prod_178 extends n_prod
{
	Query query = new Query();
	public n_prod_178()
	{
		ii_bisnis_id = 178;
		ii_contract_period = 10;
		ii_age_from = 1;
		ii_age_to = 65;

		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		
//		  1..4 cuma id, 0..3 di lst_cara_bayar
//		  
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 1; // triwulan
		ii_pmode_list[2] = 2; // semesteran
		ii_pmode_list[3] = 3; // tahunan
		ii_pmode_list[4] = 6; // bulanan

		indeks_ii_lama_bayar=64;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 5;
		ii_lama_bayar[1] = 5;
		ii_lama_bayar[2] = 5;
		ii_lama_bayar[3] = 5;
		ii_lama_bayar[4] = 5;
		ii_lama_bayar[5] = 5;
		ii_lama_bayar[6] = 5;
		ii_lama_bayar[7] = 5;
		ii_lama_bayar[8] = 10;
		ii_lama_bayar[9] = 10;
		ii_lama_bayar[10] = 10;
		ii_lama_bayar[11] = 10;
		ii_lama_bayar[12] = 10;
		ii_lama_bayar[13] = 10;
		ii_lama_bayar[14] = 10;
		ii_lama_bayar[15] = 10;
		ii_lama_bayar[16] = 5;
		ii_lama_bayar[17] = 5;
		ii_lama_bayar[18] = 5;
		ii_lama_bayar[19] = 5;
		ii_lama_bayar[20] = 5;
		ii_lama_bayar[21] = 5;
		ii_lama_bayar[22] = 5;
		ii_lama_bayar[23] = 5;
		ii_lama_bayar[24] = 10;
		ii_lama_bayar[25] = 10;
		ii_lama_bayar[26] = 10;
		ii_lama_bayar[27] = 10;
		ii_lama_bayar[28] = 10;
		ii_lama_bayar[29] = 10;
		ii_lama_bayar[30] = 10;
		ii_lama_bayar[31] = 10;
		ii_lama_bayar[32] = 5;
		ii_lama_bayar[33] = 5;
		ii_lama_bayar[34] = 5;
		ii_lama_bayar[35] = 5;
		ii_lama_bayar[36] = 5;
		ii_lama_bayar[37] = 5;
		ii_lama_bayar[38] = 5;
		ii_lama_bayar[39] = 5;
		ii_lama_bayar[40] = 10;
		ii_lama_bayar[41] = 10;
		ii_lama_bayar[42] = 10;
		ii_lama_bayar[43] = 10;
		ii_lama_bayar[44] = 10;
		ii_lama_bayar[45] = 10;
		ii_lama_bayar[46] = 10;
		ii_lama_bayar[47] = 10;
		ii_lama_bayar[48] = 5;
		ii_lama_bayar[49] = 5;
		ii_lama_bayar[50] = 5;
		ii_lama_bayar[51] = 5;
		ii_lama_bayar[52] = 5;
		ii_lama_bayar[53] = 5;
		ii_lama_bayar[54] = 5;
		ii_lama_bayar[55] = 5;
		ii_lama_bayar[56] = 10;
		ii_lama_bayar[57] = 10;
		ii_lama_bayar[58] = 10;
		ii_lama_bayar[59] = 10;
		ii_lama_bayar[60] = 10;
		ii_lama_bayar[61] = 10;
		ii_lama_bayar[62] = 10;
		ii_lama_bayar[63] = 10;
		// idec_min_up01=300000;
		ii_end_from = 65;
		//flag_uppremi = 1;
		simas=1;
		flag_worksite =1;
		flag_account=2;
		flag_default_up = 1;
		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1; // pmode 6
		
		indeks_rider_list=2;
		ii_rider[0]=821;
		ii_rider[1]=822;
		
	}

	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr =new f_hit_umur();
		
		hsl="";
		
		//usia tertanggung
		
		//mpp 5
		if ((nomor_produk >= 1 && nomor_produk <= 8) || (nomor_produk >= 17 && nomor_produk <= 24)){
			if (ii_age > 60) hsl = "Usia Masuk Plan ini maximum : 60";
		
		//mpp 10
		}else if (nomor_produk >= 9 && nomor_produk <= 16){
			if (ii_age > 55) hsl = "Usia Masuk Plan ini maximum : 55";
		}					
		return hsl;		
	}	
	
	public void of_hit_premi(){
		hsl="";
		err="";
		
		try {
			
			Double result = query.selectPremiSuperSehat(ii_bisnis_id, ii_bisnis_no, ii_age, is_kurs_id);
			 if(result != null) {
				idec_premi   = result.doubleValue();
				idec_rate = 1; //idec_up * ldec_rate * idec_add_pct / ii_permil

				if (ii_pmode == 6){ //bulanan
					idec_premi = (idec_premi) / 12.0;
				}else if (ii_pmode == 1){ //triwulan
					idec_premi = (idec_premi) / 4.0;
				}else if (ii_pmode == 2){ //semesteran
					idec_premi = (idec_premi) / 2.0;
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

	public void of_set_begdate(int thn, int bln, int tgl)
	{
		int li_month = 0;

		adt_bdate.set(thn,bln-1,tgl);

		idt_beg_date.set(thn,bln-1,tgl);		

		if (ib_flag_end_age)
		{
			ii_end_age = ii_age;
		}
		li_month = ( ii_contract_period ) * 12;
		
		idt_end_date.set(thn,bln-1,tgl);		
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		idt_end_date.add(idt_end_date.DAY_OF_MONTH , -1);

	}
	
	/*public void of_set_bisnis_no(int ai_no){
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
	}*/
		
	public int of_get_conperiod(int number_bisnis)
	{	li_cp=0;
		if((ii_bisnis_no >= 1 && ii_bisnis_no<=8) || (ii_bisnis_no >= 17 && ii_bisnis_no<=24) || (ii_bisnis_no >= 49 && ii_bisnis_no<=56) ){//SM-1 sampai SM-8 5 tahun
			ii_contract_period=5;
		}else ii_contract_period=10; // SM-9 sampai SM-16 10 tahun
		
		li_cp=ii_contract_period;
		return li_cp;	
	}
	
	 public String of_alert_min_up( double up1)
		{
			double min_up=of_get_min_up();
			String hasil_min_up="";
			String min="";
			BigDecimal bd = new BigDecimal(min_up);
			min=bd.setScale(2,0).toString();
			
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
				if(ii_bisnis_no == 1 || ii_bisnis_no == 17  || ii_bisnis_no == 33 || ii_bisnis_no == 49){//SM-1 5 tahun
					idec_up = 300000;
				}else if(ii_bisnis_no == 2 || ii_bisnis_no == 18 || ii_bisnis_no == 34 || ii_bisnis_no == 50){//SM-2 5 tahun
					idec_up = 400000;
				}else if(ii_bisnis_no == 3 || ii_bisnis_no == 19 || ii_bisnis_no == 35 || ii_bisnis_no == 51){//SM-3 5 tahun
					idec_up = 500000;
				}else if(ii_bisnis_no == 4 || ii_bisnis_no == 20 || ii_bisnis_no == 36 || ii_bisnis_no == 52) {//SM-4 5 tahun
					idec_up = 600000;
				}else if(ii_bisnis_no == 5 || ii_bisnis_no == 21 || ii_bisnis_no == 37 || ii_bisnis_no == 53){//SM-5 5 tahun
					idec_up = 700000;
				}else if(ii_bisnis_no == 6 || ii_bisnis_no == 22 || ii_bisnis_no == 38 || ii_bisnis_no == 54){//SM-6 5 tahun
					idec_up = 800000;
				}else if(ii_bisnis_no == 7 || ii_bisnis_no == 23 || ii_bisnis_no == 39 || ii_bisnis_no == 55){//SM-7 5 tahun
					idec_up = 900000;
				}else if(ii_bisnis_no == 8 || ii_bisnis_no == 24 || ii_bisnis_no == 40 || ii_bisnis_no == 56){//SM-8 5 tahun
					idec_up = 1000000;
				}else if(ii_bisnis_no == 9 || ii_bisnis_no == 25 || ii_bisnis_no == 41 || ii_bisnis_no == 57){//SM-1 10 tahun
					idec_up = 300000;
				}else if(ii_bisnis_no == 10 || ii_bisnis_no == 26 || ii_bisnis_no == 42 || ii_bisnis_no == 58){//SM-2 10 tahun
					idec_up = 400000;
				}else if(ii_bisnis_no == 11 || ii_bisnis_no == 27 || ii_bisnis_no == 43 || ii_bisnis_no == 59){//SM-3 10 tahun
					idec_up = 500000;
				}else if(ii_bisnis_no == 12 || ii_bisnis_no == 28 || ii_bisnis_no == 44 || ii_bisnis_no == 60){//SM-4 10 tahun
					idec_up = 600000;
				}else if(ii_bisnis_no == 13 || ii_bisnis_no == 29 || ii_bisnis_no == 45 || ii_bisnis_no == 61){//SM-5 10 tahun
					idec_up = 700000;
				}else if(ii_bisnis_no == 14 || ii_bisnis_no == 30 || ii_bisnis_no == 46 || ii_bisnis_no == 62){//SM-6 10 tahun
					idec_up = 800000;
				}else if(ii_bisnis_no == 15 || ii_bisnis_no == 31 || ii_bisnis_no == 47 || ii_bisnis_no == 63){//SM-7 10 tahun
					idec_up = 900000;
				}else if(ii_bisnis_no == 16 || ii_bisnis_no == 32 || ii_bisnis_no == 48 || ii_bisnis_no == 64){//SM-8 10 tahun
					idec_up = 1000000;
				}
				ldec_1 = idec_up;
				of_set_up(idec_up);
			}else{
				ldec_1 = idec_min_up02;
			}
			return ldec_1;
		}
	
	public void of_set_up(double adec_up){
		idec_up = adec_up;
	}
	
	public static void main(String[] args) {
		
	}
	
	public Double set_default_up(Double up){
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			if(ii_bisnis_no == 1 || ii_bisnis_no == 17  || ii_bisnis_no == 33 || ii_bisnis_no == 49){//SM-1 5 tahun
				idec_up = 300000;
			}else if(ii_bisnis_no == 2 || ii_bisnis_no == 18 || ii_bisnis_no == 34 || ii_bisnis_no == 50){//SM-2 5 tahun
				idec_up = 400000;
			}else if(ii_bisnis_no == 3 || ii_bisnis_no == 19 || ii_bisnis_no == 35 || ii_bisnis_no == 51){//SM-3 5 tahun
				idec_up = 500000;
			}else if(ii_bisnis_no == 4 || ii_bisnis_no == 20 || ii_bisnis_no == 36 || ii_bisnis_no == 52) {//SM-4 5 tahun
				idec_up = 600000;
			}else if(ii_bisnis_no == 5 || ii_bisnis_no == 21 || ii_bisnis_no == 37 || ii_bisnis_no == 53){//SM-5 5 tahun
				idec_up = 700000;
			}else if(ii_bisnis_no == 6 || ii_bisnis_no == 22 || ii_bisnis_no == 38 || ii_bisnis_no == 54){//SM-6 5 tahun
				idec_up = 800000;
			}else if(ii_bisnis_no == 7 || ii_bisnis_no == 23 || ii_bisnis_no == 39 || ii_bisnis_no == 55){//SM-7 5 tahun
				idec_up = 900000;
			}else if(ii_bisnis_no == 8 || ii_bisnis_no == 24 || ii_bisnis_no == 40 || ii_bisnis_no == 56){//SM-8 5 tahun
				idec_up = 1000000;
			}else if(ii_bisnis_no == 9 || ii_bisnis_no == 25 || ii_bisnis_no == 41 || ii_bisnis_no == 57){//SM-1 10 tahun
				idec_up = 300000;
			}else if(ii_bisnis_no == 10 || ii_bisnis_no == 26 || ii_bisnis_no == 42 || ii_bisnis_no == 58){//SM-2 10 tahun
				idec_up = 400000;
			}else if(ii_bisnis_no == 11 || ii_bisnis_no == 27 || ii_bisnis_no == 43 || ii_bisnis_no == 59){//SM-3 10 tahun
				idec_up = 500000;
			}else if(ii_bisnis_no == 12 || ii_bisnis_no == 28 || ii_bisnis_no == 44 || ii_bisnis_no == 60){//SM-4 10 tahun
				idec_up = 600000;
			}else if(ii_bisnis_no == 13 || ii_bisnis_no == 29 || ii_bisnis_no == 45 || ii_bisnis_no == 61){//SM-5 10 tahun
				idec_up = 700000;
			}else if(ii_bisnis_no == 14 || ii_bisnis_no == 30 || ii_bisnis_no == 46 || ii_bisnis_no == 62){//SM-6 10 tahun
				idec_up = 800000;
			}else if(ii_bisnis_no == 15 || ii_bisnis_no == 31 || ii_bisnis_no == 47 || ii_bisnis_no == 63){//SM-7 10 tahun
				idec_up = 900000;
			}else if(ii_bisnis_no == 16 || ii_bisnis_no == 32 || ii_bisnis_no == 48 || ii_bisnis_no == 64){//SM-8 10 tahun
				idec_up = 1000000;
			}
		}
		up = idec_up;
		return up;
	}
	
}