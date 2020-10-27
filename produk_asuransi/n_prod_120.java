// n_prod_120 cerdas
package produk_asuransi;
import java.math.BigDecimal;

import com.ekalife.utils.f_hit_umur;

/**
 * @author HEMILDA
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_120 extends n_prod{
	Query query = new Query();
	public n_prod_120()
	{
		
		flag_warning_autodebet = 1;//1 seharusnya autodebet, tapi tidak diblokir bila pilihannya TUNAI, hanya diberikan warning
		
		ii_bisnis_id = 120;
		ii_contract_period = 100;
		ii_age_from = 1;
		ii_age_to = 70;

		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";


		
//		untuk hitung end date ( 79 - issue_date )
	  	ii_end_from = 100;
//		ib_flag_end_age = False
	  	idec_min_up01 = 1000000;

		indeks_ii_lama_bayar=27;
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
		ii_lama_bayar[12] = 5;
		ii_lama_bayar[13] = 10;
		ii_lama_bayar[14] = 99;
		ii_lama_bayar[15] = 5;
		ii_lama_bayar[16] = 10;
		ii_lama_bayar[17] = 1;
		ii_lama_bayar[18] = 5;
		ii_lama_bayar[19] = 10;
		ii_lama_bayar[20] = 1;
		ii_lama_bayar[21] = 5;
		ii_lama_bayar[22] = 10;
		ii_lama_bayar[23] = 1;
		ii_lama_bayar[24] = 5;
		ii_lama_bayar[25] = 10;
		ii_lama_bayar[26] = 1;
		
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

		flag_uppremi=1;
		kode_flag=4;	
		flag_rider=1;
		flag_account =2;
		flag_jenis_plan=2;
		flag_reff_bii = 1;
		//Yusuf - 20050203
		isProductBancass = true; 
		isInvestasi = true;
		flag_debet = 1;
		usia_nol = 1;
		flag_cerdas_global=1;
	   
	   indeks_rider_list=22; // ADD 1 + 21 = 22 
		ii_rider = new int[indeks_rider_list];
		ii_rider[0]=810; //PA Risk A, AB, ABD		
		ii_rider[1]=812; //TPD
		ii_rider[2]=813; //CI
		ii_rider[3]=814; //WAIVER TPD
		ii_rider[4]=815; //PAYOR TPD
		ii_rider[5]=816; //WAIVER CI
		ii_rider[6]=817; //PAYOR CI
		ii_rider[7]=818; //TERM
		ii_rider[8]=819; //HCP Family
//		ii_rider[9]=820; // EKA SEHAT
		ii_rider[9]=822; //SWINE FLU alias FLU BABI
		ii_rider[10]=823;
		ii_rider[11]=825;
		ii_rider[12]=826;
		ii_rider[13]=827;
		ii_rider[14]=828;
		ii_rider[15]=830;
		ii_rider[16]=831;
		ii_rider[17]=832;
		ii_rider[18]=835;
		ii_rider[19]=811; //HCP
		ii_rider[20]=837; //SMiLe ESCI
		ii_rider[21]=848; //Smile Medical Extra 
	}
	
	public void f_kombinasi(){
		if (ii_bisnis_id==120){
			if(ii_bisnis_no==7 || ii_bisnis_no==8 || ii_bisnis_no==13 || ii_bisnis_no==14 || ii_bisnis_no==15 || ii_bisnis_no==16 || ii_bisnis_no==17 ){
				   indeks_kombinasi = 8;
				   kombinasi = new String[indeks_kombinasi];	  
				   kombinasi[0] = "C"; // pp 80% - ptb 20%
				   kombinasi[1] = "D"; // pp 70% - ptb 30%
				   kombinasi[2] = "E"; // pp 60% - ptb 40%
				   kombinasi[3] = "F"; // pp 50% - ptb 50%
				   kombinasi[4] = "G"; // pp 40% - ptb 60%
				   kombinasi[5] = "H"; // pp 30% - ptb 70%
				   kombinasi[6] = "I"; // pp 20% - ptb 80%
				   kombinasi[7] = "J"; // pp 10% - ptb 90%
				}else {
					indeks_kombinasi = 6;
					kombinasi = new String[indeks_kombinasi];
					kombinasi[0] = "A"; // pp 100% 
					kombinasi[1] = "C"; // pp 80% - ptb 20%
					kombinasi[2] = "E"; // pp 60% - ptb 40%
					kombinasi[3] = "F"; // pp 50% - ptb 50%
					kombinasi[4] = "G"; // pp 40% - ptb 60%
				    kombinasi[5] = "I"; // pp 20% - ptb 80%
				}
		}
	}
	
	public void of_set_kurs(String as_kurs)
	{
		is_kurs_id = as_kurs;
		if (as_kurs.equalsIgnoreCase("02"))
		{
			kode_flag=5;
		}else{
			kode_flag=4;
		}
	}
	
	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
		indeks_li_pmode_list=6;
		li_pmode_list = new int[indeks_li_pmode_list];			

		if (ai_no%3==0 && ii_bisnis_no!=15)
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
		if (ai_no ==19 || ai_no==20 || ai_no ==21)
		{
			flag_cerdas_global=0;
		}

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
		if(ii_bisnis_no == 4 || ii_bisnis_no == 5 || ii_bisnis_no == 6 || ii_bisnis_no == 7 || ii_bisnis_no == 8|| ii_bisnis_no == 13 || ii_bisnis_no == 14 || ii_bisnis_no == 15 || ii_bisnis_no == 16 || ii_bisnis_no == 17 || ii_bisnis_no == 18){
			if(this.is_kurs_id.equals("01")) { //RUPIAH
				if(ii_bisnis_no == 7 || ii_bisnis_no == 8 || ii_bisnis_no == 13 || ii_bisnis_no == 14 || ii_bisnis_no == 15  ){
					if (ii_pmode == 3){
						if (ad_premi >= 5000000){
							hsl = true;
						}
					}else if (ii_pmode == 2){
						if (ad_premi >= 2500000){
							hsl = true;
						}
					}else if (ii_pmode == 1){
						if (ad_premi >= 1250000){ 
							hsl = false;
						}
					}else if (ii_pmode == 6){
						if (ad_premi >= 100000){
							hsl = true;
						}
					}
				}else if(ii_bisnis_no == 16 || ii_bisnis_no == 17){	
					if (ii_pmode == 3){
						if (ad_premi >= 2400000){
							hsl = true;
						}
					}else if (ii_pmode == 2){
						if (ad_premi >= 1200000){
							hsl = true;
						}
					}else if (ii_pmode == 1){
						if (ad_premi >= 600000){ 
							hsl = false;
						}
					}else if (ii_pmode == 6){
						if (ad_premi >= 200000){
							hsl = true;
						}
					}
				}else {
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
				}else if(ii_pmode == 0){
					if(ad_premi >= 10000000){
						hsl = true;
					}
					
				}
			}	
			}else if(this.is_kurs_id.equals("02")){ //DOLLAR
				if(ii_bisnis_no == 7 || ii_bisnis_no == 8){
					if (ii_pmode == 3){
						if (ad_premi >= 500){
							hsl = true;
						}
					}else if (ii_pmode == 2){
						if (ad_premi >= 250){
							hsl = true;
						}
					}else if (ii_pmode == 1){
						if (ad_premi >= 125){ 
							hsl = false;
						}
					}else if (ii_pmode == 6){
						if (ad_premi >= 10){
							hsl = true;
						}
					}
				}else if(ii_bisnis_no == 16 || ii_bisnis_no == 17){	
					if (ii_pmode == 3){
						if (ad_premi >= 240){
							hsl = true;
						}
					}else if (ii_pmode == 2){
						if (ad_premi >= 120){
							hsl = true;
						}
					}else if (ii_pmode == 1){
						if (ad_premi >= 60){ 
							hsl = false;
						}
					}else if (ii_pmode == 6){
						if (ad_premi >= 20){
							hsl = true;
						}
					}
				}else{
				
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
				}else if (ii_pmode == 0){
					if(ad_premi >= 1000){
						hsl = true;
					}
				}
			 }
			}
		}else {
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
		
		if(ii_bisnis_no==15){
			ii_contract_period=99;
		}
		ii_contract_period = ii_end_from - ii_end_age;
	}

	public void of_set_premi(double adec_premi)
	{
		
		
		double min_up=7500000;
		if(ii_bisnis_no%3==0 && ii_bisnis_no!=15){
			idec_rate = 1250;
			adec_premi = adec_premi / idec_pct_list[0];
			idec_up = adec_premi * idec_rate / 1000;
		}else if (ii_bisnis_no==19||ii_bisnis_no==20||ii_bisnis_no==21 || ii_bisnis_no==22||ii_bisnis_no==23||ii_bisnis_no==24){
			if (ii_bisnis_no==19 || ii_bisnis_no==20||ii_bisnis_no==22||ii_bisnis_no==23)
			{
				if (ii_pmode == 1)
				{
					//triwulan
				idec_up = adec_premi * 4 * 5;
					}else if (ii_pmode==3){
						idec_up = adec_premi * 5;
							}else{
								if (ii_pmode == 2)
								{
									//semesteran
									idec_up = adec_premi * 2 * 5;
								}else{
									if (ii_pmode == 6)
									{
										//bulanan
										idec_up = adec_premi *12* 5 ;
								}
							}
					}
			}else if(ii_bisnis_no==23||ii_bisnis_no==24){
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
			idec_rate = 5000;
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
		int hari=umr.hari(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
		
		/** Usia Masuk Pemegang Polis adalah 17 tahun sampai dengan 85 tahun **/
		if (nomor_produk == 22 || nomor_produk == 23 || nomor_produk == 24) {
			ii_age_to = 85; 
		}
		
		/* Usia Masuk Tertanggung adalah 15hari sampai dengan 70 tahun */
		if (hari < 15)
		{
				hsl="Usia Anak untuk Plan ini minimum 15 Hari";		
		}
		
		/* Usia Masuk Tertanggung adalah 15hari sampai dengan 70 tahun */
		if (ii_age > 70) {
			    hsl="Usia Anak untuk Plan ini maximum 70 Tahun";		
		}
		
		/* usia untuk minimum tertanggunng 15 hari jika 0 bulan maka masuk ke kondisi dibawah jadi di comment
		else if (bln < 1)
		{
			hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
		} */
		
		if (ii_age > ii_age_to)
		{
			hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
		}
		
		/** Usia Masuk Pemegang Polis adalah 17 tahun **/
		if (ii_usia_pp < 17)
		{
			hsl="Usia Pemegang Polis minimum : 17 Tahun !!!";
		}
		
		if (ii_usia_pp > ii_age_to)
		{
			//hsl="Usia Pemegang Polis maximum : "+ii_age_to+" Tahun !!!"; // permintaan Achmad Anwarudin Tuesday, November 06, 2007 11:03 AM
			  hsl="Usia Pemegang Polis maximum : "+ii_age_to+" Tahun !!!";
		}
		
		return hsl;		
	}
	
	public double of_get_min_up_with_topup()//premi pokok + topup berkala
	{
		double ldec_1=0;
		if( ii_bisnis_no == 10 || ii_bisnis_no ==11||ii_bisnis_no == 22 || ii_bisnis_no ==23) {
			if(this.is_kurs_id.equals("01")) { //RUPIAH
				switch (ii_pmode){
				case 3:
					idec_min_up01=5000000;
					break;
				case 1:
					idec_min_up01=1350000;
					break;
				case 2:
					idec_min_up01=2625000;
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
					idec_min_up01=135;
					break;
				case 2:
					idec_min_up01=262.5;
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
		if(ii_bisnis_no == 4 || ii_bisnis_no == 5 || ii_bisnis_no == 6|| ii_bisnis_no == 21 ){ //*CERDAS CARE 5, 10, SINGLE POWERLINK 5, 6, SINGLE
			if(this.is_kurs_id.equals("01")) { //RUPIAH
				switch (ii_pmode){
				case 3: //*MINIMUM PREMI POKOK TAHUNAN
					idec_min_up01=1000000;
					break;
				case 0: //*MINIMUN PREMI POKOK SEKALIGUS
					idec_min_up01=10000000;
					break;
				}
			}else if(this.is_kurs_id.equals("02")){ //DOLLAR
				switch (ii_pmode){
				case 3: //*MINIMUM PREMI POKOK TAHUNAN
					idec_min_up01=100;
					break;
				case 0: //*MINIMUN PREMI POKOK SEKALIGUS
					idec_min_up01=1000;
				}
			}
		}else if( ii_bisnis_no == 10 || ii_bisnis_no ==11 || ii_bisnis_no == 12 || ii_bisnis_no == 19 || ii_bisnis_no ==20 || ii_bisnis_no == 21 || ii_bisnis_no ==22 || ii_bisnis_no == 23 || ii_bisnis_no == 24  ) {
			if(this.is_kurs_id.equals("01")) { //RUPIAH
				switch (ii_pmode){
				case 3:
					idec_min_up01=5000000;
					break;
				case 1:
					idec_min_up01=1350000;
					break;
				case 2:
					idec_min_up01=2625000;
					break;
				case 6:
					idec_min_up01=500000;
					break;
				case 0:
					idec_min_up01=10000000;
					break;
				}
			}else if(this.is_kurs_id.equals("02")){ //DOLLAR
				switch (ii_pmode){
				case 3:
					idec_min_up01=500;
					break;
				case 1:
					idec_min_up01=130.5;
					break;
				case 2:
					idec_min_up01=26.25;
					break;
				case 6:
					idec_min_up01=50;
					break;
				case 0:
					idec_min_up01=1000;
				}
			}
		}else if(ii_bisnis_no == 7 || ii_bisnis_no ==8 || ii_bisnis_no == 13  || ii_bisnis_no ==14 || ii_bisnis_no == 15 || ii_bisnis_no == 16 || ii_bisnis_no ==17){
			if(this.is_kurs_id.equals("01")) { //RUPIAH
				
				if(ii_bisnis_no == 16 || ii_bisnis_no ==17){//SK NO.15/AJS-SK/2013
					switch (ii_pmode){
					case 3:
						idec_min_up01=2400000;
						break;
					case 1:
						idec_min_up01=600000;
						break;
					case 2:
						idec_min_up01=1200000;
						break;
					case 6:
						idec_min_up01=200000;
						break;
					
					}
				}else{
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
			  }
			}else if(this.is_kurs_id.equals("02")){ //DOLLAR
				if(ii_bisnis_no == 16 || ii_bisnis_no ==17){
					switch (ii_pmode){
					case 3:
						idec_min_up01=240;
						break;
					case 1:
						idec_min_up01=60;
						break;
					case 2:
						idec_min_up01=120;
						break;
					case 6:
						idec_min_up01=20;
						break;
					
					}
				}else{
				
				switch (ii_pmode){
				case 3:
					idec_min_up01=500;
					break;
				case 1:
					idec_min_up01=250;
					break;
				case 2:
					idec_min_up01=125;
					break;
				case 6:
					idec_min_up01=50;
					break;
				
				}
			  }
			}		
		}
		
		else {
			if(this.is_kurs_id.equals("01")) { //RUPIAH
				switch (ii_pmode){
				case 3:
					idec_min_up01=1000000;
					break;
				case 1:
					idec_min_up01=270000;
					break;
				case 2:
					idec_min_up01=525000;
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
		}
		
		ldec_1 = idec_min_up01;
		return ldec_1;
	}
	
	public double of_get_max_up()
	{
		double ldec_1=0;
		
		if(ii_bisnis_no==7 || ii_bisnis_no==8 || ii_bisnis_no==9 || ii_bisnis_no==10 || ii_bisnis_no==16 || ii_bisnis_no==17 || ii_bisnis_no==18|| ii_bisnis_no==19|| ii_bisnis_no==22|| ii_bisnis_no==23){
			if (ii_age >=1 || ii_age <=19 )
			{
				idec_max_up01 = idec_premi * 50;
			}else if(ii_age >=20 || ii_age <=29){
				idec_max_up01 = idec_premi * 40;		
			}else if(ii_age >=30 || ii_age <=39){
				idec_max_up01 = idec_premi * 30;				
			}else if(ii_age >=40 || ii_age <=49){
				idec_max_up01 = idec_premi * 20;				
			}else if(ii_age >=50 || ii_age <=60){
				idec_max_up01 = idec_premi * 10;				
			}
		}
//		if (ii_age <=5 )
//		{
//			idec_max_up01=100000000;
//		}else{
//			idec_max_up01=200000000;
//		}
		ldec_1 = idec_max_up01;
		return ldec_1;
	}		

	public int of_get_conperiod(int number_bisnis)
	{
		li_cp=0;
		if(number_bisnis==15){
			ii_contract_period=99;
		}
		li_cp =ii_contract_period-ii_age;
		return li_cp;		
	}

	public double f_get_bia_akui(int ar_lb, int ar_ke)
	{
		double ld_bia=0;
		switch (ar_ke)
		{
			 case 1: 
				ld_bia=0.05;
			 break;
			 case 2:
				ld_bia=0.15;
			 break;
		}
		return ld_bia;	
	}

	public double cek_awal()
	{
		if(ii_bisnis_no_utama<=12 || (ii_bisnis_no_utama>=16 && ii_bisnis_no_utama<=18)){
			if (is_kurs_id.equalsIgnoreCase("02") ) {
				//li_id = 19;
				li_id = 53;
				if(ii_bisnis_no_utama>=4){
					if(ii_bisnis_no_utama>=7){
						li_id = 19;
					}else{
						li_id = 34;//cerdas care biaya adminnya $2.5
					}
					
				}
			}else{
				if(ii_bisnis_no_utama>=4){
					if(ii_bisnis_no_utama>=7){
						li_id = 18;
					}else{
						li_id = 33;//cerdas care biaya adminnya 25.000
					}
					
				}
			}
		}else if(ii_bisnis_no_utama>12 && ii_bisnis_no_utama<=15){
			if (is_kurs_id.equalsIgnoreCase("02") ) {
				li_id = 19;
			}else{
				li_id = 18;
			}
		}else if(ii_bisnis_no_utama>=22 && ii_bisnis_no_utama<=24){
			if (is_kurs_id.equalsIgnoreCase("02") ) {
				li_id = 19;
			}else{
				li_id = 18;
			}
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
		int decimalPlace = 3; // set 3 decimal dibelakang koma 
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
	
	//pa
	public void count_rate_810(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp1=0;
		ldec_rate=0;
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
		if(umurttg<=16)
		{
			klas=2;
		}
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
			 if(ii_bisnis_no_utama>=22 && ii_bisnis_no_utama<=24){//khusus simas power link, faktor pengali untuk jadi premi tahunan pakai faktor umum. kemudian dijadikan bulanan
				 ldec_temp1 = ((( up / 1000) * hasil) )* 0.1;	
				 
			 }
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
		
		try {
			Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, nomor_produk);
//			Double resultPremi = query.selectResultPremi(42, reg_spaj);
			
			if(result != null) {
				hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi CI Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 
		
		 double factor_x=0;
		 
			 ldec_temp4 = (( up / 1000) * hasil) * 0.1;
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
		double factor_rider_khusus = 0;
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
			 if(ii_bisnis_no_utama>=10 && ii_bisnis_no_utama<=12){//khusus simas power link, faktor pengali untuk jadi premi tahunan pakai faktor umum. kemudian dijadikan bulanan
				 ldec_temp5 = ((premi/ldec_pct1)/1000 )* hasil * 0.1;	
			 }
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
				Double result = query.selectRateRider(kurs, 1, umurpp, kode_produk, nomor_produk);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Payor TPD Tidak Ada !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 if(!err1.equals("")) throw new RuntimeException(err1);
			 else if(!err.equals("")) throw new RuntimeException(err);
			 ldec_temp6 = ((premi * li_kali / 1000) * hasil) * 0.1;
			 if(ii_bisnis_no_utama>=10 && ii_bisnis_no_utama<=12){//khusus simas power link, faktor pengali untuk jadi premi tahunan pakai faktor umum. kemudian dijadikan bulanan
				 ldec_temp6 = (((premi / 1000) * hasil) / ldec_pct1) * 0.1;	
//				 
			 }
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
			int li_umur_pp = 0;
			
			try {
				Double result = query.selectRateRider(kurs, umurttg, 1, kode_produk, nomor_produk);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Waiver CI Tidak Ada!!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 ldec_temp7 = ((premi * li_kali / 1000) * hasil) * 0.1;
			 if(ii_bisnis_no_utama>=10 && ii_bisnis_no_utama<=12){//khusus simas power link, faktor pengali untuk jadi premi tahunan pakai faktor umum. kemudian dijadikan bulanan
				 ldec_temp7 = (((premi / 1000) * hasil) / ldec_pct1)* 0.1;	
			 }
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
				Double result = query.selectRateRider(kurs, 1, li_umur_pp, kode_produk, nomor_produk);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Payor CI Tidak Ada   !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 ldec_temp8 = ((premi * li_kali / 1000) * hasil) * 0.1;
			 if(ii_bisnis_no_utama>=10 && ii_bisnis_no_utama<=12){//khusus simas power link, faktor pengali untuk jadi premi tahunan pakai faktor umum. kemudian dijadikan bulanan
				 ldec_temp8 = (((premi / 1000) * hasil) / ldec_pct1)* 0.1;	
			 }
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

		try {
			Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, nomor_produk);
			
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
				Double result = query.selectRateRider(kurs, umurttg,usia_pp, kode_produk, nomor_produk);
				
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
//			Double resultPremi = query.selectResultPremi(42, reg_spaj);
			
			if(result != null) {
				hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Ladies Tidak Ada !!!!";
				}
//			if(resultPremi != null) {
//				hasilPremi = resultPremi.doubleValue();
//				}
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
			 
//			 if(persenUp != 0){
				 ldec_temp4 = (( up / 1000) * hasil ) * 0.1; // FIXIN last , 
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
	
	public String min_total_premi(Integer pmode_id, double premi , String kurs)
	{
		String hsl="";
		double total_premi = premi ;
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
					if (total_premi < 100000)
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
						hsl="Minimum Total Premi Triwulanan (3 bulanan ) : US$ 108";
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
					if (total_premi < 100000)
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
			 if (nomor_produk >= 160 )
			 {
				 disc = new Double(0.9);
			 }
			 ldec_temp2 = hasil *  0.1 * disc.doubleValue();
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp2);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp2=bd.doubleValue();	
			 ldec_rate2 = 0;
			 BigDecimal jm = new BigDecimal(ldec_rate2);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate2=jm.doubleValue();	

	}	
	
	public void count_rate_832(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp2=0;
		ldec_rate2=0;
		int fltrt = 0;
		String kd_pd = Integer.toString(new Integer(nomor_produk));
		String kd = kd_pd;
		if(Integer.parseInt(kd_pd) <10)
		{
			kd = kd_pd.substring(kd_pd.length()-1, kd_pd.length());
		}

			try {
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, (Integer.parseInt(kd)));
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Ladies Medical Expense Tidak Ada.";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 Double disc = new Double(1);
			 Double rate = new Double(0.12);
			 if (nomor_produk > 7)
			 {
				 disc = new Double(0.975);
			 }
			 ldec_temp2 = hasil * rate.doubleValue() * disc.doubleValue();//xxxcccvvv
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp2);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp2=bd.doubleValue();	
			 ldec_rate2 = 0;
			 BigDecimal jm = new BigDecimal(ldec_rate2);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate2=jm.doubleValue();	

	}
	
	/* simpol validasi up */
	public String cek_min_up(Double premi, Double up, String kurs , Integer pmode_id)
	{
		String hasil_up = "";
		double min_up1 = 0;
		double min_up2 = 0;
		of_set_premi(premi.doubleValue());
		min_up1 = idec_up;
		double rate1 = 1.25; // 125%
		double rate2 = 5;    // 500%
		
		if (pmode_id.intValue() == 0){ /* Sekaligus */
			
			if (kurs.equalsIgnoreCase("01")){
				min_up1 = 15000000;
			}else{
				min_up1 = 1500;              // dolar
			}
			
			min_up2 = rate1 * premi.doubleValue() /100;
			
		}else{
			
			int fkt = 1;
			
			if (kurs.equalsIgnoreCase("01")){
				min_up1 = 7500000;
			}else{
				min_up1 = 750;				 // dolar
			}
			
			if (pmode_id.intValue() == 1){ 			// TRIWULAN
				fkt = 4;
			}else if (pmode_id.intValue() == 2){	// SEMESTER 
				fkt = 2;
			}else if (pmode_id.intValue() == 6){	// BULANAN
				fkt = 12;
			}
			
			min_up2 = rate2 * premi.doubleValue() * fkt;
		}
		
		double min = 0;
		
		if (min_up1 > min_up2){
			min = min_up1;
		}else{
			min = min_up2;
		}
		
		if (up.doubleValue() < min){
			hasil_up="Up Minimum untuk plan ini "+ f.format(min); 
		}
		
		return hasil_up;
	}
	
	public String cek_max_up(Integer li_umur_ttg, Integer kode_produk, Double premi, Double up, Double fltpersen, Integer pmode_id,String kurs)
	{
		String hasil_up="";
		int faktor = 0; 
		Integer result;
		try {
			if(kode_produk==22 || kode_produk==23 || kode_produk==24) {
				result = query.faktorexcell80(li_umur_ttg.intValue(),202);
			}else{
				result = query.faktorexcell80(li_umur_ttg.intValue(),153);
			}
		
			if (result!=null)
			{		
				faktor=result.intValue();
				String ff=Integer.toString(faktor);
				double nil =((premi.doubleValue()) * Double.parseDouble(ff)) * fltpersen.doubleValue();
				if(li_umur_ttg<6){
					if(kurs.equalsIgnoreCase("02")){
						nil = new Double(50000);
					}else{
						nil = new Double(500000000);
					}
				}
	
				if ((up.doubleValue()) > nil)
				{
					hasil_up="Up Maksimum untuk plan ini "+ f.format(nil);
				}
			}else{
				
				hasil_up="Tidak ada data faktor";
			 }
		} catch (Exception e) {
			err=(e.toString());
			}
		  return hasil_up;
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
	
	public static void main(String[] args) {
	}
}
