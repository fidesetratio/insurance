package produk_asuransi;

import java.math.BigDecimal;

import com.ekalife.utils.f_hit_umur;

/**
 * @author RYan
 * @since Sept 15, 2016 (10:06:46 PM)
 */
public class n_prod_219 extends n_prod
{
	Query query = new Query();
	public n_prod_219()
	{
		ii_bisnis_id = 219;
		ii_contract_period = 10;
		ii_age_from = 1;
		ii_age_to = 22;
		idec_min_up01 = 10000000;
//		if (ii_bisnis_no==1||ii_bisnis_no==2||ii_bisnis_no==3||ii_bisnis_no==4)	idec_up = 20000000;
		if (ii_bisnis_no >= 1 && ii_bisnis_no <= 8)	idec_up = 20000000; //helpdesk [138638] produk baru SPP Syariah (219-5~8)
//		idec_up = 20000000;
		usia_nol = 0;
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

		indeks_ii_lama_bayar=8;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 5;
		ii_lama_bayar[1] = 5;
		ii_lama_bayar[2] = 5;
		ii_lama_bayar[3] = 5;
		ii_lama_bayar[4] = 5;
		ii_lama_bayar[5] = 5;
		ii_lama_bayar[6] = 5;
		ii_lama_bayar[7] = 5;
		// idec_min_up01=300000;
		ii_end_from = 65;
		//flag_uppremi = 1;
		simas=1;
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
		
		/*indeks_rider_list=1;
		ii_rider = new int[indeks_rider_list];
		ii_rider[0]=836;*/
		
	}

	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr =new f_hit_umur();
		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
		int hari1=umr.hari1(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
		hsl="";
		
		//usia tertanggung
		
		if(ii_bisnis_no==1){
			if (ii_age > 3) hsl = " Usia Masuk Plan ini maximum : 3";
		}else if(ii_bisnis_no==2){
			if (ii_age > 9) hsl = " Usia Masuk Plan ini maximum : 9";
		}else if(ii_bisnis_no==3){
			if (ii_age > 10) hsl = " Usia Masuk Plan ini maximum : 10";
		}else if(ii_bisnis_no==4){
			if (ii_age > 10) hsl = " Usia Masuk Plan ini maximum : 10";
		}else if(ii_bisnis_no == 5){ //helpdesk [138638] produk baru SPP Syariah (219-5~8)
			if (ii_age > 3) hsl = " Usia Masuk Plan ini maximum : 3";
		}else if(ii_bisnis_no == 6){
			if (ii_age > 9) hsl = " Usia Masuk Plan ini maximum : 9";
		}else if(ii_bisnis_no == 7){
			if (ii_age > 10) hsl = " Usia Masuk Plan ini maximum : 10";
		}else if(ii_bisnis_no == 8){
			if (ii_age > 10) hsl = " Usia Masuk Plan ini maximum : 10";
		}else{
			if (ii_age > 22) hsl = " Usia Masuk Plan ini maximum : 22";
		}
		
//		if (ii_bisnis_no==1||ii_bisnis_no==2||ii_bisnis_no==3||ii_bisnis_no==4){
		if (ii_bisnis_no >= 1 && ii_bisnis_no <= 8){ //helpdesk [138638] produk baru SPP Syariah (219-5~8)
			if (hari1 < 30) hsl = " Usia Masuk Plan ini Minimal 30 hari";
			
			//ambil kondisi jika usia  jika < 180 usia 0 , agar bisa dpt rate 0
			if (hari1 < 180){
				ii_age=0;
			}
		}
		
		if (ii_usia_pp < 20)
		{
			hsl="Usia Pemegang Polis minimum : 20 Tahun !!!";
		}
		
		if (ii_usia_pp > 59)
		{
			hsl="Usia Pemegang Polis Maksimum : 59 Tahun !!!";
		}
		
		return hsl;		
	}
	
	public Double set_default_up(Double up){
		Double upx = up;
//		if (ii_bisnis_no==1||ii_bisnis_no==2||ii_bisnis_no==3||ii_bisnis_no==4)	idec_up = 20000000;
		if (ii_bisnis_no >= 1 && ii_bisnis_no <= 8) idec_up = 20000000; //helpdesk [138638] produk baru SPP Syariah (219-5~8)
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			if(up > idec_up){
				upx = up;
			}else{
				upx=idec_up;
			}
//			if(ii_bisnis_no == 1 || ii_bisnis_no == 17  || ii_bisnis_no == 33){//SM-1 5 tahun
//				idec_up = 10000000;
		}
		up = upx;
		idec_up = up;
		return up;
	}
	
	public double of_get_rate()
	{
		of_hit_premi();
		return idec_rate;	
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
					idec_premi=(idec_premi * idec_up / 1000) * 0.1; 
				}else if (ii_pmode == 1){ //triwulan
					idec_premi=(idec_premi * idec_up / 1000) * 0.27;
				}else if (ii_pmode == 2){ //semesteran
					idec_premi=(idec_premi * idec_up / 1000) * 0.525;
				}else if (ii_pmode == 3){ //semesteran
					idec_premi=(idec_premi * idec_up / 1000) * 1;
				}																								

				if(idec_premi>0){
					idec_premi = idec_premi/10000;
					if(!(ii_bisnis_no >= 5 && ii_bisnis_no <= 8)) idec_premi = Math.ceil(idec_premi);
					idec_premi =  idec_premi*10000;
					BigDecimal bd = new BigDecimal(idec_premi);
					idec_premi = bd.doubleValue();
				};

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
	
	public void of_set_bisnis_no(int ai_no){
		ii_bisnis_no = ai_no;
		
		if(ii_bisnis_no >= 5 && ii_bisnis_no <= 8){ //helpdesk [138638] produk baru SPP Syariah (219-5~8)
			indeks_li_pmode_list=5;
			li_pmode_list = new int[indeks_li_pmode_list];
			li_pmode_list[1] = 3;
			li_pmode_list[2] = 2;
			li_pmode_list[3] = 1;
			li_pmode_list[4] = 6;	
			ii_pmode_list = li_pmode_list;
			indeks_ii_pmode_list=indeks_li_pmode_list;
		} else {
//		if (ai_no == 5){    // lsdbs_number = 5 Sekaligus
//			indeks_li_pmode_list=2;
//			li_pmode_list = new int[indeks_li_pmode_list];
//			li_pmode_list[1]=0;
//			ii_pmode_list = li_pmode_list;
//			indeks_ii_pmode_list=indeks_li_pmode_list;
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
		
	public double count_rate_836(int li_class, int flag_jenis_plan, int nomor_bisnis, int umurttg, int umurpp){
		String err="";
		rate_rider=0;
		Double result = (double) 0;
		try {
			if (nomor_bisnis == 1){
				 result = (double) 1332000;
			}else if (nomor_bisnis == 2){
				 result = (double) 2603000;
			}else {
				 result = (double) 4217000;
			}
			if(result != null) {
				rate_rider = result.doubleValue();		  	
			}
		}
	  catch (Exception e) {
			err=(e.toString());
	  } 		  	

		return rate_rider;	
	}
	
	public int of_get_conperiod(int number_bisnis)
	{	li_cp=0;
			ii_contract_period= 22 - ii_age;
		
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
			
			if (!(ii_bisnis_no >= 5 && ii_bisnis_no <= 8))
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
				if (ii_bisnis_no >= 5 && ii_bisnis_no <= 8) //helpdesk [138638] produk baru SPP Syariah (219-5~8)
					idec_up = 20000000; 
				else
					idec_up = 10000000;
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
}