// n_prod_202 Excellent Link Syariah
	package produk_asuransi;
	import java.math.BigDecimal;

import com.ekalife.utils.f_hit_umur;

	/**
	 * @author HEMILDA
	 *
	 * To change the template for this generated type comment go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 */
public class n_prod_202 extends n_prod{
	
		Query query = new Query();
		public n_prod_202()
		{
			
			flag_warning_autodebet = 1;//1 seharusnya autodebet, tapi tidak diblokir bila pilihannya TUNAI, hanya diberikan warning
			
			ii_bisnis_id = 202;
			ii_contract_period = 100;
			ii_age_from = 1;
			ii_age_to = 70;

			indeks_is_forex=2;
			is_forex= new String[indeks_is_forex];
			is_forex[0]="01";
			is_forex[1]="02";


			
//			untuk hitung end date ( 79 - issue_date )
		  	ii_end_from = 100;
//			ib_flag_end_age = False
		  	idec_min_up01 = 1000000;

			indeks_ii_lama_bayar=12;
			ii_lama_bayar = new int[indeks_ii_lama_bayar];
			ii_lama_bayar[0] = 5;
			ii_lama_bayar[1] = 10;
			ii_lama_bayar[2] = 1;
			ii_lama_bayar[3] = 5;
			ii_lama_bayar[4] = 10;
			ii_lama_bayar[5] = 1;
			ii_lama_bayar[6] = 5;
			ii_lama_bayar[7] = 10;
			ii_lama_bayar[8] = 1;
			ii_lama_bayar[9] = 5;
			ii_lama_bayar[10] = 10;
			ii_lama_bayar[11] = 1;
			
			indeks_idec_list_premi=14;
			idec_list_premi = new double[indeks_idec_list_premi];
			for( int i = 0 ; i< 13 ; i++)
			{
				idec_list_premi[i] = 3000000 + ( 1000000 * (i - 1) );
			}
			
//			1..4 cuma id, 0..3 di lst_cara_bayar
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

			flag_uppremi=1;
			kode_flag=7;	
			flag_rider=1;
			flag_account =2;
			flag_jenis_plan=2;
			flag_reff_bii = 1;
			flag_uppremiopen=1;
			flag_cerdas_global=1;
			//Yusuf - 20050203
			isProductBancass = true; 
			isInvestasi = true;
			flag_debet = 1;
			usia_nol = 1;
			

		   indeks_kombinasi = 10;
		   kombinasi = new String[indeks_kombinasi];
		   kombinasi[0] = "A"; // pp 100% 
		   kombinasi[1] = "C"; // pp 80% - ptb 20%
		   kombinasi[2] = "E"; // pp 60% - ptb 40%
		   kombinasi[3] = "F"; // pp 50% - ptb 50%
		   kombinasi[4] = "G"; // pp 40% - ptb 60%
		   kombinasi[5] = "I"; // pp 20% - ptb 80%
		   kombinasi[6] = "D"; // pp 70% - ptb 30%
		   kombinasi[7] = "B"; // pp 90% - ptb 10%
		   kombinasi[8] = "J"; // pp 10% - ptb 90%
		   kombinasi[9] = "H"; // pp 30% - ptb 70%
		  /* indeks_rider_list=1;
			ii_rider = new int[indeks_rider_list];
			ii_rider[0]=825;*/
		   indeks_rider_list=20;
			ii_rider = new int[indeks_rider_list];
			ii_rider[0]=810;
			ii_rider[1]=811;
			ii_rider[2]=812;
			ii_rider[3]=813;
			//ii_rider[4]=814;
			ii_rider[4]=815;
			ii_rider[5]=816;
			ii_rider[6]=817;
			ii_rider[7]=818;	
			ii_rider[8]=819;
			ii_rider[9]=821;
			ii_rider[10]=822;
			ii_rider[11]=823;
			ii_rider[12]=825;
			ii_rider[13]=826;
			ii_rider[14]=827;
			ii_rider[15]=828;
			ii_rider[16]=830;
			ii_rider[17]=831;
			ii_rider[18]=832;
			ii_rider[18]=836;
			
		}
		
		public void of_set_kurs(String as_kurs)
		{
			is_kurs_id = as_kurs;
			if (as_kurs.equalsIgnoreCase("02"))
			{
				kode_flag=8;
			}else{
				kode_flag=7;
			}
		}
		
		public void of_set_bisnis_no(int ai_no)
		{
			ii_bisnis_no = ai_no;
			indeks_li_pmode_list=6;
			li_pmode_list = new int[indeks_li_pmode_list];			

			if (ai_no == 3 || ai_no == 6 || ai_no == 9|| ai_no == 12)
			{
				indeks_li_pmode_list=2;
				li_pmode_list[1] = 0 ;			 
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

		public boolean of_check_premi(double ad_premi)
		{
			boolean hsl=false;
				if(this.is_kurs_id.equals("01")) { //RUPIAH
					if (ii_pmode == 3){
						if (ad_premi >= 1000000){
							hsl = true;
						}
					}else if (ii_pmode == 2){
						if (ad_premi >= 525000){
							hsl = true;
						}
					}else if (ii_pmode == 1){
						if (ad_premi >= 270000){ 
							hsl = false;
						}
					}else if (ii_pmode == 6){
						if (ad_premi >= 100000){
							hsl = true;
						}
					}
				}else if(this.is_kurs_id.equals("02")){ //DOLLAR
					if (ii_pmode == 3){
						if (ad_premi >= 100){
							hsl = true;
						}
					}else if (ii_pmode == 2){
						if (ad_premi >= 52.5){
							hsl = true;
						}
					}else if (ii_pmode == 1){
						if (ad_premi >= 27)
						{ 
							hsl = false;
						}
					}else if (ii_pmode == 6){
						if (ad_premi >= 10){
							hsl = true;
						}
					}
				}
			
			return hsl;
		}	

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
			
			ldt_end.set(thn,bln-1,tgl);	
			ldt_end.add(idt_beg_date.MONTH,li_month);

			idt_end_date.set(thn,bln-1,tgl);			
			idt_end_date.add(idt_beg_date.MONTH,li_month);
			idt_end_date.add(ldt_end.DAY_OF_MONTH , -1);

			ii_contract_period = ii_end_from - ii_end_age;
		}

		public void of_set_premi(double adec_premi)
		{	
			idec_rate = 5000;
			double min_up=7500000;
			if(ii_bisnis_no%3==0 && ii_bisnis_no!=15){
				idec_rate = 1250;
				adec_premi = adec_premi / idec_pct_list[0];
				idec_up = adec_premi * idec_rate / 1000;
			}else if (ii_bisnis_no==4||ii_bisnis_no==5||ii_bisnis_no==6){
				if(ii_bisnis_no==5||ii_bisnis_no==6){
					if(is_kurs_id.equals("01")){
						min_up=15000000;
					}else{
						min_up=1500;
					}
					idec_up=adec_premi*1.25;
				}
				
				
			}else{
				idec_premi_main = adec_premi;
				idec_premi = adec_premi;
				if(ii_bisnis_no==7 || ii_bisnis_no==8 || ii_bisnis_no==13 || ii_bisnis_no==14 || ii_bisnis_no==15){
					if(this.is_kurs_id.equals("02")){
					
						min_up=750;
						idec_rate = 0.5;
					}
					double fkt=1;

					if (ii_pmode == 1)
					{
						fkt = 0.25;
					}else if (ii_pmode==2)
					{
							fkt = 0.5;
					}else if (ii_pmode == 6)
						{
							fkt= 0.083;
						}
					//min_up1 = fkt*min_up1;
					adec_premi =  adec_premi * fkt;
				}else{
					if (ii_pmode == 1)
					{
						//triwulan
						adec_premi = adec_premi / idec_pct_list[1];
					}else{
						if (ii_pmode == 2)
						{
							//semesteran
							adec_premi = adec_premi / idec_pct_list[2];
						}else{
							if (ii_pmode == 6)
							{
								//bulanan
								adec_premi = adec_premi / idec_pct_list[6];
							}
						}
					}
				}

				idec_up = adec_premi * idec_rate / 1000;
				
					if(idec_up < min_up) 
						{
						idec_up = min_up;
						}
			}	

			if(idec_up < min_up) 
				{
				idec_up = min_up;
				}
			of_set_up(idec_up);
		}

		public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
		{
			f_hit_umur umr =new f_hit_umur();
			int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);

			if (bln < 1)
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
			
			if (ii_usia_pp > ii_age_to)
			{
				//hsl="Usia Pemegang Polis maximum : "+ii_age_to+" Tahun !!!"; // permintaan Achmad Anwarudin Tuesday, November 06, 2007 11:03 AM
			}
			
			return hsl;		
		}
		
		public double of_get_min_up_with_topup()//premi pokok + topup berkala
		{
			double ldec_1=0;
			if( ii_bisnis_no == 10 || ii_bisnis_no ==11) {
				if(this.is_kurs_id.equals("01")) { //RUPIAH
					switch (ii_pmode){
					case 3:
						idec_min_up01=5000000;
						break;
					case 1:
						idec_min_up01=1250000;
						break;
					case 2:
						idec_min_up01=2500000;
						break;
					case 6:
						idec_min_up01=500000;
						break;
					}
				}else if(this.is_kurs_id.equals("02")){ //DOLLAR
					switch (ii_pmode){
					case 3:
						idec_min_up01=500;
						break;
					case 1:
						idec_min_up01=125;
						break;
					case 2:
						idec_min_up01=250;
						break;
					case 6:
						idec_min_up01=50;
						break;
					}
				}
			}
			
			ldec_1 = idec_min_up01;
			return ldec_1;
		}

		public double of_get_min_up()
		{
		double ldec_1=0;
				if(this.is_kurs_id.equals("01")) { //RUPIAH
					switch (ii_pmode){
					case 3:
						idec_min_up01=5000000;
						break;
					case 1:
						idec_min_up01=1250000;
						break;
					case 2:
						idec_min_up01=2500000;
						break;
					case 6:
						idec_min_up01=100000;
						break;
					case 0:
						idec_min_up01=10000000;
						break;
					}
				}else if(this.is_kurs_id.equals("02")){ //DOLLAR
					switch (ii_pmode){
					case 3:
						idec_min_up01=100;
						break;
					case 1:
						idec_min_up01=27;
						break;
					case 2:
						idec_min_up01=52.5;
						break;
					case 6:
						idec_min_up01=10;
						break;
					case 0:
						idec_min_up01=1000;
					}
				}
			ldec_1 = idec_min_up01;
			return ldec_1;
		}
		
		public int of_get_conperiod(int number_bisnis)
		{
			li_cp=0;
			li_cp =ii_contract_period-ii_age;
			return li_cp;		
		}

		public double f_get_bia_akui(int ar_lb, int ar_ke)
		{
			double ld_bia=0;
			switch (ar_ke)
			{
				 case 1: 
					ld_bia=0.8;
				 break;
				 case 2:
					ld_bia=0.15;
				 break;
			}
			return ld_bia;	
		}

		public double cek_awal()
		{
			if (is_kurs_id.equalsIgnoreCase("02") ) {
				li_id = 19;
//				if(ii_bisnis_no>=4){
//					if(ii_bisnis_no>=6){
//						li_id = 53;
//					}else{
//						li_id = 34;//cerdas care biaya adminnya $2.5
//					}
//					
//				}
			}else{
				li_id = 18;
//				if(ii_bisnis_no>=4){
//					if(ii_bisnis_no>=6){
//						li_id = 52;
//					}else{
//						li_id = 33;//cerdas care biaya adminnya 25.000
//					}
//					
//				}
			}
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

		public double cek_rate(int li_bisnis,int li_pmode,int pperiod,int li_insper,int umur_tt , String kurs,int insperiod)
		{

			String err="";
			double hasil=0;
			try {
				Double result = query.selectNilai(1, 115, "01", 3, 80, 80, 1, umur_tt);
				
				if(result != null) {
				   hasil = result.doubleValue();
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			return hasil; 
		}
		
		public void biaya2(double ld_bia_ass, double premi, int ldec_pct, double up)
		{
			mbu_jumlah2=((up/1000) * ld_bia_ass) * 0.1;
			int decimalPlace = 2;
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
			int decimalPlace = 2;
			BigDecimal bd = new BigDecimal(mbu_jumlah3);
			bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			mbu_jumlah3=bd.doubleValue();			
		}	
		
		public String min_total_premi(Integer pmode_id, double premi , String kurs)
		{
			String hsl="";
			double total_premi = premi ;
			if(kurs.equalsIgnoreCase("01"))
			{
				switch (pmode_id.intValue())
				{
		
					case 1:
						if (total_premi < 1250000)
						{
							hsl="Minimum Total Premi Triwulanan (3 bulanan ) : Rp. 1.250.000,-";
						}
						break;
					case 2:
						if (total_premi < 2400000)
						{
							hsl="Minimum Total Premi Semesteran (6 bulanan ) : Rp. 2.400.000,-";
						}
						break;
					case 3:
						if (total_premi < 5000000)
						{
							hsl="Minimum Total Premi Tahunan : Rp. 5.000.000,-";
						}
						break;
					case 6:
						if (total_premi < 500000)
						{
							hsl="Minimum Total Premi Bulanan (1 bulanan) : Rp. 500.000,-";
						}
						break;
					case 0:
						if (total_premi < 10000000)
						{
							hsl="Minimum Total Premi Sekaligus : Rp. 10.000.000,-";
						}
						break;
				}
			}else if(kurs.equalsIgnoreCase("02"))
			{
				switch (pmode_id.intValue())
				{
		
					case 1:
						if (total_premi < 125)
						{
							hsl="Minimum Total Premi Triwulanan (3 bulanan ) : US$ 125";
						}
						break;
					case 2:
						if (total_premi < 240)
						{
							hsl="Minimum Total Premi Semesteran (6 bulanan ) : US$ 240,-";
						}
						break;
					case 3:
						if (total_premi < 500)
						{
							hsl="Minimum Total Premi Tahunan : US$ 500,-";
						}
						break;
					case 6:
						if (total_premi < 50)
						{
							hsl="Minimum Total Premi Bulanan (1 bulanan) : US$ 50,-";
						}
						break;
					case 0:
						if (total_premi < 1000)
						{
							hsl="Minimum Total Premi Sekaligus : US$ 1000,-";
						}
						break;
				}
			}
		return hsl;
		}
		
		public String min_topup(Integer pmode_id, double premi , double premi_topup, String kurs, int jenis_topup)
		{
			String hsl="";
			if (jenis_topup==2)
			{
				if(kurs.equalsIgnoreCase("01"))
				{
					if (premi_topup < 1000000)
					{
						hsl="Minimum Top-Up Tunggal dan Berkala : Rp. 1.000.000,-";
					}
					
					if (premi_topup > 1e10)
					{
						hsl="Maksimum Top-Up Tunggal dan Berkala sebesar Rp. 10.000.000.000,- untuk satu tahun Polis";
					}
				}else if(kurs.equalsIgnoreCase("02"))
				{
					if (premi_topup < 100)
					{
						hsl="Minimum Top-Up Tunggal dan Berkala : US$ 100,-";
					}
					
					if (premi_topup > 1e06)
					{
						hsl="Maksimum Top-Up Tunggal dan Berkala sebesar US$ 1.000.000,- untuk satu tahun Polis";
					}
				}
				
			}

			
			double total_premi = premi + premi_topup;
			if(kurs.equalsIgnoreCase("01"))
			{
				switch (pmode_id.intValue())
				{
		
					case 1:
						if (total_premi < 1080000)
						{
							hsl="Minimum Total Premi Triwulanan (3 bulanan ) : Rp. 1.080.000,-";
						}
						break;
					case 2:
						if (total_premi < 2100000)
						{
							hsl="Minimum Total Premi Semesteran (6 bulanan ) : Rp. 2.100.000,-";
						}
						break;
					case 3:
						if (total_premi < 4000000)
						{
							hsl="Minimum Total Premi Tahunan : Rp. 4.000.000,-";
						}
						break;
					case 6:
						if (total_premi < 400000)
						{
							hsl="Minimum Total Premi Bulanan (1 bulanan) : Rp. 400.000,-";
						}
						break;
					case 0:
						if (total_premi < 10000000)
						{
							hsl="Minimum Total Premi Sekaligus : Rp. 10.000.000,-";
						}
						break;
				}
			}else if(kurs.equalsIgnoreCase("02"))
			{
				switch (pmode_id.intValue())
				{
		
					case 1:
						if (total_premi < 108)
						{
							hsl="Minimum Total Premi Triwulanan (3 bulanan ) : US$ 108,-";
						}
						break;
					case 2:
						if (total_premi < 210)
						{
							hsl="Minimum Total Premi Semesteran (6 bulanan ) : US$ 210,-";
						}
						break;
					case 3:
						if (total_premi < 400)
						{
							hsl="Minimum Total Premi Tahunan : US$ 400,-";
						}
						break;
					case 6:
						if (total_premi < 40)
						{
							hsl="Minimum Total Premi Bulanan (1 bulanan) : US$ 40,-";
						}
						break;
					case 0:
						if (total_premi < 1000)
						{
							hsl="Minimum Total Premi Sekaligus : US$ 1000,-";
						}
						break;
				}
			}
			
			return hsl;
		}
		
		public static void main(String[] args) {
		}
	

		//pa
		public void count_rate_810(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
		{
			String err="";
			String err1="";
			double hasil=0;
			ldec_temp1=0;
			ldec_rate1=0;

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
//				 ldec_temp1 = ((unit * up / 1000) * hasil) * 0.1;
				 ldec_temp1 = (( up / 1000) * hasil) * 0.1;
				 int decimalPlace = 2;
				 BigDecimal bd = new BigDecimal(ldec_temp1);
				 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_temp1=bd.doubleValue();	
				 ldec_rate1 = (hasil);
				 BigDecimal jm = new BigDecimal(ldec_rate1);
				 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_rate1=jm.doubleValue();			

		}
		
		public void count_rate_811(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
		{
			String err="";
			String err1="";
			double hasil=0;
			ldec_temp2=0;
			ldec_rate2=0;

				try {
					Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, nomor_produk);
					
					if(result != null) {
						hasil = result.doubleValue();
					}else{
						err1="Rate Asuransi HCP Tidak Ada  !!!!";
					}
				   }
				 catch (Exception e) {
					   err=e.toString();
				 } 		
				 ldec_temp2 = hasil *  0.1;
				 int decimalPlace = 2;
				 BigDecimal bd = new BigDecimal(ldec_temp2);
				 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_temp2=bd.doubleValue();	
				 ldec_rate2 = 0;
				 BigDecimal jm = new BigDecimal(ldec_rate2);
				 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_rate2=jm.doubleValue();			

		}
		
		public void count_rate_812(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
		{
			String err="";
			String err1="";
			double hasil=0;
			ldec_temp3=0;
			ldec_rate3=0;

			try {
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, nomor_produk);
				
				if(result != null) {
					hasil = result.doubleValue();
					}else{
						err1="Rate Asuransi TPD Tidak Ada  !!!!";
					}
				   }
				 catch (Exception e) {
					   err=e.toString();
				 } 		
				 ldec_temp3 = (( up / 1000) * hasil) * 0.1;
				 int decimalPlace = 2;
				 BigDecimal bd = new BigDecimal(ldec_temp3);
				 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_temp3=bd.doubleValue();	
				 ldec_rate3 =hasil;
				 BigDecimal jm = new BigDecimal(ldec_rate3);
				 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_rate3=jm.doubleValue();			

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
//				Double resultPremi = query.selectResultPremi(42, reg_spaj);
				
				if(result != null) {
					hasil = result.doubleValue();
					}else{
						err1="Rate Asuransi CI Tidak Ada  !!!!";
					}
//				if(resultPremi != null) {
//					hasilPremi = resultPremi.doubleValue();
//					}
				   }
				 catch (Exception e) {
					   err=e.toString();
				 } 		
				 double factor_x=0;
				 
//				 if (persenUp==1){
//					 factor_x = 0.5;
//				 }else if(persenUp==2){
//					 factor_x = 0.6;
//				 }else if(persenUp==3){
//					 factor_x = 0.7;
//				 }else if(persenUp==4){
//					 factor_x = 0.8;
//				 }else if(persenUp==5){
//					 factor_x = 0.9;
//				 }else if(persenUp==6){
//					 factor_x = 1.0;
//				 }
//				 
//				 if(persenUp != 0){
					 ldec_temp4 = (( up / 1000) * hasil ) * 0.1; // FIXIN last
//				 }else{
//					ldec_temp4 = hasilPremi;
//				 }
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
			ldec_temp5=0;
			ldec_rate5=0;
			String err="";
			String err1="";
			double hasil=0;

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
					Double result = query.selectRateRider(kurs, umurttg, li_umur_pp, kode_produk, nomor_produk);
					
					if(result != null) {
						hasil = result.doubleValue();
					}else{
						err1="Rate Asuransi Waiver TPD Tidak Ada  !!!!";
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
		
		public void count_rate_815(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
		{
			String err="";
			String err1="";
			double hasil=0;
			ldec_temp6=0;
			ldec_rate6=0;

			double ldec_pct1 = 1;
			int li_kali = 1;
			switch (pmode)
			{
				case 1 ://triwulan
					li_kali = 4;
					ldec_pct1 = 0.27;
					break;	
				case 2 : //semesteran
					li_kali = 2;
					ldec_pct1 = 0.525;
					break;
				case 6 ://BULANAN
					li_kali = 12 ;
					ldec_pct1 = 0.1;
					break;
			}	
			
				int li_umur_pp = umurpp;
				try {
					Double result = query.selectRateRider(kurs, umurttg, li_umur_pp, kode_produk, nomor_produk);
					
					if(result != null) {
						hasil = result.doubleValue();
					}else{
						err1="Rate Asuransi Payor TPD Tidak Ada  !!!!";
					}
				   }
				 catch (Exception e) {
					   err=e.toString();
				 } 		
				 if(!err1.equals("")) throw new RuntimeException(err1);
				 else if(!err.equals("")) throw new RuntimeException(err);
					 ldec_temp6 = ((premi * li_kali / 1000) * hasil) * 0.1;
				 int decimalPlace = 2;
				 BigDecimal bd = new BigDecimal(ldec_temp6);
				 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_temp6=bd.doubleValue();	
				 ldec_rate6 =hasil;
				 BigDecimal jm = new BigDecimal(ldec_rate6);
				 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_rate6=jm.doubleValue();		

		}	
			
		public void count_rate_816(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
		{
			String err="";
			String err1="";
			double hasil=0;
			ldec_temp7=0;
			ldec_rate7=0;

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
					Double result = query.selectRateRider(kurs, umurttg, li_umur_pp, kode_produk, nomor_produk);
					
					if(result != null) {
						hasil = result.doubleValue();
					}else{
						err1="Rate Asuransi Waiver CI Tidak Ada  !!!!";
					}
				   }
				 catch (Exception e) {
					   err=e.toString();
				 } 		
				 ldec_temp7 = ((premi * li_kali / 1000) * hasil) * 0.1;
				 int decimalPlace = 2;
				 BigDecimal bd = new BigDecimal(ldec_temp7);
				 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_temp7=bd.doubleValue();	
				 ldec_rate7 =hasil;
				 BigDecimal jm = new BigDecimal(ldec_rate7);
				 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_rate7=jm.doubleValue();		

		}	

		public void count_rate_817(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
		{
			String err="";
			String err1="";
			double hasil=0;
			ldec_temp8=0;
			ldec_rate8=0;

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
				int li_umur_pp = umurpp;
		
				try {
					Double result = query.selectRateRider(kurs, umurttg, li_umur_pp, kode_produk, nomor_produk);
					
					if(result != null) {
						hasil = result.doubleValue();
					}else{
						err1="Rate Asuransi Payor CI Tidak Ada  !!!!";
					}
				   }
				 catch (Exception e) {
					   err=e.toString();
				 } 		
				 ldec_temp8 = ((premi * li_kali / 1000) * hasil) * 0.1;
				 int decimalPlace = 2;
				 BigDecimal bd = new BigDecimal(ldec_temp8);
				 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_temp8=bd.doubleValue();	
				 ldec_rate8 =hasil;
				 BigDecimal jm = new BigDecimal(ldec_rate8);
				 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_rate8=jm.doubleValue();			

		}	
		
		public void count_rate_818(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
		{
			String err="";
			String err1="";
			double hasil=0;
			ldec_temp9=0;
			ldec_rate9=0;

			try {
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, 2);
				
				if(result != null) {
					hasil = result.doubleValue();
					}else{
						err1="Rate Asuransi Term Tidak Ada !!!!";
					}
				   }
				 catch (Exception e) {
					   err=e.toString();
				 } 		
				 ldec_temp9 = ((up / 1000) * hasil) * 0.1;
				 int decimalPlace = 2;
				 BigDecimal bd = new BigDecimal(ldec_temp9);
				 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_temp9=bd.doubleValue();	
				 ldec_rate9 =hasil;
				 BigDecimal jm = new BigDecimal(ldec_rate9);
				 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_rate9=jm.doubleValue();			

		}	
		
		public void count_rate_819(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
		{
			
			String err="";
			String err1="";
			double hasil=0;
			ldec_temp2=0;
			ldec_rate2=0;
			int fltrt = 0;
			String kd_pd = Integer.toString(new Integer(nomor_produk));
			String kd = kd_pd;
			double rate_pmode = 0.1;
			if(Integer.parseInt(kd_pd) <10)
			{
				kd = kd_pd.substring(kd_pd.length()-1, kd_pd.length());
			}

				try {
					Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, (Integer.parseInt(kd)));
					
					if(result != null) {
						hasil = result.doubleValue();
					}else{
						err1="Rate Asuransi HCP Tidak Ada  !!!!";
					}
				   }
				 catch (Exception e) {
					   err=e.toString();
				 } 		
				 Double disc = new Double(1);
				 if ((nomor_produk > 20 && nomor_produk<141) || (nomor_produk>160 && nomor_produk<281) || (nomor_produk>300 && nomor_produk<381) || (nomor_produk>390 && nomor_produk<431 || (nomor_produk>450 && nomor_produk<=530)))
				 {
					 disc = new Double(0.9);
				 }
				 ldec_temp2 = hasil *  rate_pmode * disc.doubleValue();
				 int decimalPlace = 2;
				 BigDecimal bd = new BigDecimal(ldec_temp2);
				 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_temp2=bd.doubleValue();	
				 ldec_rate2 = 0;
				 BigDecimal jm = new BigDecimal(ldec_rate2);
				 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_rate2=jm.doubleValue();	
				 
		}	
		
		public void count_rate_827(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
		{
			String err="";
			String err1="";
			double hasil=0;
			ldec_temp7=0;
			ldec_rate7=0;
			int usia_pp =0;
			if (nomor_produk==4 || nomor_produk==5 )
			{
				usia_pp = 1;
			}

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
					Double result = query.selectRateRider(kurs, umurttg, usia_pp, kode_produk, nomor_produk);
					
					if(result != null) {
						hasil = result.doubleValue();
					}else{
						err1="Rate Asuransi Waiver CI/TPD Tidak Ada!!!!";
					}
				   }
				 catch (Exception e) {
					   err=e.toString();
				 } 		
				 ldec_temp7 = ((premi * li_kali / 1000) * hasil) * 0.1;
				 int decimalPlace = 2;
				 BigDecimal bd = new BigDecimal(ldec_temp7);
				 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_temp7=bd.doubleValue();	
				 ldec_rate7 =hasil;
				 BigDecimal jm = new BigDecimal(ldec_rate7);
				 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_rate7=jm.doubleValue();			

		}
		
		public void count_rate_828(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
		{
			String err="";
			String err1="";
			double hasil=0;
			ldec_temp6=0;
			ldec_rate6=0;
			double ldec_pct1 = 1;
			int li_kali = 1;
			switch (pmode)
			{
				case 1 ://triwulan
					li_kali = 4;
					ldec_pct1 = 0.27;
					break;	
				case 2 : //semesteran
					li_kali = 2;
					ldec_pct1 = 0.525;
					break;
				case 6 ://BULANAN
					li_kali = 12 ;
					ldec_pct1 = 0.1;
					break;
			}	
				int li_umur_pp = umurpp;
				if (nomor_produk==3 || nomor_produk==2){
					umurttg=1;
				}
				try {
					Double result = query.selectRateRider(kurs, umurttg, li_umur_pp, kode_produk, nomor_produk);
					
					if(result != null) {
						hasil = result.doubleValue();
					}else{
						err1="Rate Asuransi PAYOR 25 TPD/DEATH/CI Tidak Ada  !!!!";
					}
				   }
				 catch (Exception e) {
					   err=e.toString();
				 } 	
				 if(!err1.equals("")) throw new RuntimeException(err1);
				 else if(!err.equals("")) throw new RuntimeException(err);
					 ldec_temp6 = ((premi * li_kali / 1000) * hasil) * 0.1;
				 int decimalPlace = 2;
				 BigDecimal bd = new BigDecimal(ldec_temp6);
				 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_temp6=bd.doubleValue();	
				 ldec_rate6 =hasil;
				 BigDecimal jm = new BigDecimal(ldec_rate6);
				 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_rate6=jm.doubleValue();		

		}	

		public void count_rate_830(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
		{
			String err="";
			String err1="";
			double hasil=0;
			double hasilPremi=0;
			ldec_temp4=0;
			ldec_rate4=0;

			try {
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, nomor_produk);
//				Double resultPremi = query.selectResultPremi(42, reg_spaj);
				
				if(result != null) {
					hasil = result.doubleValue();
					}else{
						err1="Rate Asuransi Ladies Tidak Ada !!!!";
					}
//				if(resultPremi != null) {
//					hasilPremi = resultPremi.doubleValue();
//					}
				   }
				 catch (Exception e) {
					   err=e.toString();
				 } 		
				 double factor_x=0;
				 
			/*	 if (persenUp==1){
					 factor_x = 0.5;
				 }else if(persenUp==2){
					 factor_x = 0.6;
				 }else if(persenUp==3){
					 factor_x = 0.7;
				 }else if(persenUp==4){
					 factor_x = 0.8;
				 }else if(persenUp==5){
					 factor_x = 0.9;
				 }else if(persenUp==6){
					 factor_x = 1.0;
				 }*/
				 
//				 if(persenUp != 0){
					 ldec_temp4 = (( up / 1000) * hasil ) * 0.1; // FIXIN last , 
//				 }else{
//					ldec_temp4 = hasilPremi;
//				 }
				 int decimalPlace = 2;
				 BigDecimal bd = new BigDecimal(ldec_temp4);
				 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_temp4=bd.doubleValue();	
				 ldec_rate4 =hasil;
				 BigDecimal jm = new BigDecimal(ldec_rate4);
				 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				 ldec_rate4=jm.doubleValue();			

		}
		
}
	
