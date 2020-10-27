package produk_asuransi;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.ekalife.utils.f_hit_umur;

/**
 * @author Andhika
 * @since Sept 21, 2012 (10:06:46 PM)
 */
public class n_prod_208 extends n_prod
{
	Query query = new Query();
	public n_prod_208()
	{
		ii_bisnis_id = 208;
		ii_contract_period = 10;
		ii_age_from = 1;
		ii_age_to = 22;
		idec_min_up01 = 10000000;
//		if (ii_bisnis_no==5||ii_bisnis_no==6||ii_bisnis_no==7||ii_bisnis_no==8) idec_min_up01 = 20000000;
//		idec_up = 20000000;

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

		indeks_ii_lama_bayar=80;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 5;
		ii_lama_bayar[1] = 5;
		ii_lama_bayar[2] = 5;
		ii_lama_bayar[3] = 5;
		ii_lama_bayar[4] = 5;
		ii_lama_bayar[5] = 5;
		ii_lama_bayar[6] = 5;
		ii_lama_bayar[7] = 5;
		ii_lama_bayar[8] = 5;
		ii_lama_bayar[9] = 5;
		ii_lama_bayar[10] = 5;
		ii_lama_bayar[11] = 5;
		ii_lama_bayar[12] = 5;
		ii_lama_bayar[13] = 5;
		ii_lama_bayar[14] = 5;
		ii_lama_bayar[15] = 5;
		ii_lama_bayar[16] = 5;
		ii_lama_bayar[17] = 5;
		ii_lama_bayar[18] = 5;
		ii_lama_bayar[19] = 5;
		ii_lama_bayar[20] = 5;
		ii_lama_bayar[21] = 5;
		ii_lama_bayar[22] = 5;
		ii_lama_bayar[23] = 5;
		ii_lama_bayar[24] = 5;
		ii_lama_bayar[25] = 5;
		ii_lama_bayar[26] = 5;
		ii_lama_bayar[27] = 5;
		
		
		/** 
		 * PATAR TIMOTIUS
		 * 2018 - 08 -07
		 * DMTM BTN SMARTKID >= 29-1 N <= 32 -1**/
		ii_lama_bayar[28] = 5;
		ii_lama_bayar[29] = 5;
		ii_lama_bayar[30] = 5;
		ii_lama_bayar[31] = 5;
		
		
		/** 
		 * PATAR TIMOTIUS
		 * 2018 - 08 -08
		 * DMTM SIMASKID BSIM >= 33-1 N <= 36 -1
		 * 
		 * **/
		ii_lama_bayar[32] = 5;
		ii_lama_bayar[33] = 5;
		ii_lama_bayar[34] = 5;
		ii_lama_bayar[35] = 5;
		
		
		

		/** 
		 * PATAR TIMOTIUS
		 * 2018 - 08 -08
		 * DMTM SMILE KID BJB >= 45-1 N <= 48 -1
		 * 
		 * **/
		ii_lama_bayar[44] = 5;
		ii_lama_bayar[45] = 5;
		ii_lama_bayar[46] = 5;
		ii_lama_bayar[47] = 5;
		
		// idec_min_up01=300000;
		ii_end_from = 65;
		//flag_uppremi = 1;
		simas=1;
//		flag_worksite =1;
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
		
		indeks_rider_list=4;
		ii_rider[0]=821;
		ii_rider[1]=822;
		ii_rider[2]=829;
		ii_rider[3]=836; // khusus dan satu-satunya rider untuk SIMAS KID (lsdbs 5 s/d 8)
		
	}

	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr =new f_hit_umur();
//		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
		int hari1=umr.hari1(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
		hsl="";
		
		//usia tertanggung
		
		if(ii_bisnis_no==5 || ii_bisnis_no==9 || ii_bisnis_no==13 || ii_bisnis_no==17 || ii_bisnis_no==21 || ii_bisnis_no==25 || ii_bisnis_no==37 || ii_bisnis_no==41){
			if (ii_age > 3) hsl = " Usia Masuk Plan ini maximum : 3";
		}else if(ii_bisnis_no==6 || ii_bisnis_no==10 || ii_bisnis_no==14 || ii_bisnis_no==18 || ii_bisnis_no==22 || ii_bisnis_no==26 || ii_bisnis_no==38){
			if (ii_age > 9) hsl = " Usia Masuk Plan ini maximum : 9";
		}else if(ii_bisnis_no==7 || ii_bisnis_no==11 || ii_bisnis_no==15 || ii_bisnis_no==19 || ii_bisnis_no==23 || ii_bisnis_no==27 || ii_bisnis_no==39 || ii_bisnis_no==40){
			if (ii_age > 10) hsl = " Usia Masuk Plan ini maximum : 10";
		}else if(ii_bisnis_no==8 || ii_bisnis_no==12 || ii_bisnis_no==16 || ii_bisnis_no==20 || ii_bisnis_no==24 || ii_bisnis_no==28){
			if (ii_age > 10) hsl = " Usia Masuk Plan ini maximum : 10";
		}else if(ii_bisnis_no==42 || ii_bisnis_no==43 || ii_bisnis_no==44){
			if (ii_age > 7) hsl = " Usia Masuk Plan ini maximum : 7";
		}else{
			if (ii_age > 22) hsl = " Usia Masuk Plan ini maximum : 22";
		}
		/** BTN DMTM SMARTKID 
		 *  Patar Timotius 02/08/2018
		 *  29,30,31,32
		 * **/
		
		boolean isDMTMSmartKid = (ii_bisnis_no >= 29 && ii_bisnis_no <= 32);
		
		/** BSIM DMTM SIMASKID 
		 *  Patar Timotius 08/08/2018
		 *  33,34,35,36
		 * **/
		
		boolean isDMTMBSIMSimasKid = (ii_bisnis_no >= 33 && ii_bisnis_no <= 36);
		
		/** SMiLe KID New Agency 
		 *  Mark Valentino 08/09/2019
		 *  37,38,39,40,41,42,43,44
		 * **/		
		boolean isSmileKidAgency = (ii_bisnis_no>=9 && ii_bisnis_no<=12) || (ii_bisnis_no>=37 && ii_bisnis_no<=44);
		
		
		boolean isSmileKidBJBDMTM = (ii_bisnis_no>=45 && ii_bisnis_no<=48);
		
		
		if(isDMTMSmartKid || isSmileKidBJBDMTM ){
			hsl="";
			if(!(ii_usia_pp >= 20 && ii_usia_pp <= 59))
			{
				hsl = "Usia Pemegang Polis Harus : 20 - 59 Tahun !!!";
			}
			
			/**
			 * cek usia tertanggung untuk BTN DMTM SMART KID
			 * Patar Timotius
			 */
			if(hari1<30){
				hsl = " Usia Masuk Plan ini minimal : 30 hari";
						
			}
			if(ii_bisnis_no == 29 || ii_bisnis_no == 45){
				if (ii_age > 3) hsl = " Usia Masuk Plan ini maximum : 3";
			}
			if(ii_bisnis_no == 30  || ii_bisnis_no == 46){
				if (ii_age > 9) hsl = " Usia Masuk Plan ini maximum : 9";
			}
			if(ii_bisnis_no == 31 || ii_bisnis_no == 47){
				if (ii_age > 10) hsl = " Usia Masuk Plan ini maximum : 10";
			}
			if(ii_bisnis_no == 32 || ii_bisnis_no == 48){
				if (ii_age > 10) hsl = " Usia Masuk Plan ini maximum : 10";
			}
		}
		
		if(isDMTMBSIMSimasKid){
			hsl="";
			if(!(ii_usia_pp >= 20 && ii_usia_pp <= 59))
			{
				hsl = "Usia Pemegang Polis Harus : 20 - 59 Tahun !!!";
			}
			
			/**
			 * cek usia tertanggung untuk BSIM DMTM SIMAS KID
			 * Patar Timotius
			 */
			if(hari1<30){
				hsl = " Usia Masuk Plan ini minimal : 30 hari";
						
			}
			if(ii_bisnis_no == 29){
				if (ii_age > 3) hsl = " Usia Masuk Plan ini maximum : 3";
			}
			if(ii_bisnis_no == 30){
				if (ii_age > 9) hsl = " Usia Masuk Plan ini maximum : 9";
			}
			if(ii_bisnis_no == 31){
				if (ii_age > 10) hsl = " Usia Masuk Plan ini maximum : 10";
			}
			if(ii_bisnis_no == 32){
				if (ii_age > 10) hsl = " Usia Masuk Plan ini maximum : 10";
			}
		}
		
		
		if(isSmileKidAgency){
			hsl="";
			if(!(ii_usia_pp >= 20 && ii_usia_pp <= 59))
			{
				hsl = "Usia Pemegang Polis Harus : 20 - 59 Tahun !!!";
			}
			
			/**
			 * cek usia tertanggung untuk BSIM DMTM SIMAS KID
			 * Mark Valentino
			 */
			if(hari1<30){
				hsl = " Usia Masuk Plan ini minimal : 30 hari";						
			}
			if (hari1 < 180){
				ii_age=0;
			}			
		}		
		
		
		
		if (ii_bisnis_no >=5 &&  ii_bisnis_no <=28){
			if (hari1 < 30) hsl = " Usia Masuk Plan ini Minimal 30 hari";
			
			if(ii_usia_pp > 59){
				hsl = "Usia Pemegang Polis maximum : 59 Tahun !!!"; 
			} else if(ii_usia_pp < 20){
				hsl = "Usia Pemegang Polis minimum : 20 Tahun !!!";
			}
			
			//ambil kondisi jika usia  jika < 180 usia 0 , agar bisa dpt rate 0
			if (hari1 < 180){
				ii_age=0;
			}
		}
		
		return hsl;		
	}
	
	public Double set_default_up(Double up){
		Double upx = up;
		if (ii_bisnis_no >=5 &&  ii_bisnis_no <=28)	idec_up = 20000000;
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
				}else if (ii_pmode == 3){ //tahunan
					idec_premi=(idec_premi * idec_up / 1000) * 1;
				}																								
				
				if ((ii_bisnis_no >= 9 && ii_bisnis_no <= 12) || (ii_bisnis_no >= 21 && ii_bisnis_no <= 24) || (ii_bisnis_no >= 29 && ii_bisnis_no <= 32) || (ii_bisnis_no >= 37 && ii_bisnis_no <= 48)){ //Produk Smart Plan Insurance tidak ada pembulatan 
					idec_premi = idec_premi/10000;
				}else{
					idec_premi = Math.ceil(idec_premi/10000);
				}		
				
				idec_premi = idec_premi * 10000;
				
				//request untuk perhitungan premi di E-Lions tidak menggunakan 2 digit desimal, helpdesk [126577] //Chandra
				//update, produk smart plan protection tidak ada pembulatan // Chandra
				if(!(ii_bisnis_no >= 21 && ii_bisnis_no <= 24)) {
					java.text.DecimalFormat df= new java.text.DecimalFormat("#0.00");
					df.setRoundingMode(RoundingMode.DOWN);
					idec_up = Double.parseDouble(df.format(idec_up));
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
	
	public void of_set_bisnis_no(int ai_no){
		ii_bisnis_no = ai_no;

//		if (ai_no == 5){    // lsdbs_number = 5 Sekaligus
//			indeks_li_pmode_list=2;
//			li_pmode_list = new int[indeks_li_pmode_list];
//			li_pmode_list[1]=0;
//			ii_pmode_list = li_pmode_list;
//			indeks_ii_pmode_list=indeks_li_pmode_list;
//		}
//
//		if (ii_bisnis_id < 800) 
//		{
//			ii_lbayar = ii_lama_bayar[ii_bisnis_no-1];
//			of_set_pmode(ii_pmode_list[1]);			 
//		}else{
//			if (ii_bisnis_id == 802 || ii_bisnis_id == 804) //PC & WPD
//			{
//				ii_age = ii_usia_pp;
//				of_set_up(idec_premi_main);			
//			}else{
//				of_set_age();
//				of_set_up(idec_up)	;			
//			}
//		}
//		indeks_li_pmode=indeks_ii_pmode_list;
//		li_pmode = new int[indeks_li_pmode];
//		
//		for (int i =1 ; i<indeks_li_pmode;i++)
//		{
//			li_pmode[i] = ii_pmode_list[i];
//		}		
		if (ai_no >= 5  && ai_no <= 28){
			flag_worksite =0;
			
			indeks_li_pmode_list=5;
			li_pmode_list = new int[indeks_li_pmode_list];
			li_pmode_list[1] = 1; // triwulan
			li_pmode_list[2] = 2; // semesteran
			li_pmode_list[3] = 3; // tahunan
			li_pmode_list[4] = 6; // bulanan
			ii_pmode_list = li_pmode_list;
			indeks_ii_pmode_list=indeks_li_pmode_list;
		}else if (ai_no >= 13  && ai_no <= 16){
			flag_default_up = 0;
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
			/**
			 * DMTM SMARTKID
			 * 08/03/2018
			 * Patar Timotius
			 */
			boolean isSmartKidBTNDMTM = (ii_bisnis_no>=29 && ii_bisnis_no<=32);
			
			/**
			 * DMTM SMARTKID
			 * 08/03/2018
			 * Patar Timotius
			 */
			boolean isSmileKidBJBDMTM = (ii_bisnis_no>=45 && ii_bisnis_no<=48);
		
			
			
			/**
			 * DMTM SIMASKIDBSIM
			 * 08/08/2018
			 * Patar Timotius
			 */
			boolean isSimasKidBSIMDMTM = (ii_bisnis_no>=33 && ii_bisnis_no<=36);
			
			if(isSmartKidBTNDMTM || isSmileKidBJBDMTM){
				if (min_up > up1){
					hasil_min_up=" UP Minimum untuk plan ini : "+ min;	
				}
			}else if(isSimasKidBSIMDMTM){
				if (min_up > up1){
					hasil_min_up=" UP Minimum untuk plan ini : "+ min;	
				}
			}else{
				up1= idec_up;
				if (min_up > up1)
				{
					hasil_min_up=" UP Minimum untuk plan ini : "+ min;	
				}
			}
			
			return hasil_min_up;
		}
	  
	  public double of_get_min_up()
		{
			double ldec_1=0;
			
			
			/**
			 * DMTM SMARTKID BTN
			 * Patar Timotius 02/08/2018
			 */
			boolean isSmartKidBTNDMTM = (ii_bisnis_no>=29 && ii_bisnis_no<=32);
			

			/**
			 * DMTM SIMASKID BSIM
			 * Patar Timotius 08/08/2018
			 */
			boolean isSimasKidBSIMDMTM = (ii_bisnis_no>=33 && ii_bisnis_no<=36);
			
			/**
			 * Agency New SMiLe Kid BSIM
			 * Mark Valentino 01/08/2019
			 */
			boolean isSmileKidAgency = (ii_bisnis_no>=9 && ii_bisnis_no<=12) || (ii_bisnis_no>=37 && ii_bisnis_no<=44);	
			
			
			

			/**
			 * DMTM SMILE KID BJB
			 * Patar Timotius 05/09/2019
			 */

			boolean isSmileKidBJB = (ii_bisnis_no >= 45 && ii_bisnis_no <= 48) ;
							
								
			
			if(isSmartKidBTNDMTM || isSmileKidBJB){
				if (is_kurs_id.equalsIgnoreCase("01") )
				{
					idec_up = 20000000; // minimal up 20 juta
					ldec_1 = idec_up;
					of_set_up(idec_up);
				}
				
			}else if(isSimasKidBSIMDMTM){
				if (is_kurs_id.equalsIgnoreCase("01") )
				{
					idec_up = 20000000; // minimal up 20 juta
					ldec_1 = idec_up;
					of_set_up(idec_up);
				}
				
			}else if(isSmileKidAgency){
				if (is_kurs_id.equalsIgnoreCase("01") )
				{
					idec_up = 50000000; // minimal up 50 juta
					ldec_1 = idec_up;
					of_set_up(idec_up);
				}				
				
			}else{
					if (is_kurs_id.equalsIgnoreCase("01") )
					{
						idec_up = 10000000;
						ldec_1 = idec_up;
						of_set_up(idec_up);
					}else{
						ldec_1 = idec_min_up02;
					}
			};
			return ldec_1;
		}
	  
	  public double of_get_max_up(){
			double ldec_1 = new Double("99999999999");

			/**
			 * DMTM SMILE KID BJB
			 * Patar Timotius 05/09/2019
			 */

			boolean isSmileKidBJB = (ii_bisnis_no >= 45 && ii_bisnis_no <= 48) ;
							

			
			
			/**
			 * Patar Timotius
			 * 03-08-2018
			 * DMTMSmartKidDMTM
			 */
			boolean isSmartKidBTNDMTM =(ii_bisnis_no>=29 && ii_bisnis_no<=32);
			
			/**
			 * Patar Timotius
			 * 08-08-2018
			 * DMTMSimasKidBSIM
			 */
			boolean isSimasKidBSIMDMTM =(ii_bisnis_no>=33 && ii_bisnis_no<=36);
			
			
			if (is_kurs_id.equalsIgnoreCase("01")) {
				if((ii_bisnis_no > 12 && ii_bisnis_no < 17)  ){
					ldec_1 = Double.parseDouble("300000000");
				}else if(ii_bisnis_no >= 21 && ii_bisnis_no <= 24){
					ldec_1 = Double.parseDouble("1000000000");
				}else if(isSmartKidBTNDMTM || isSmileKidBJB){
					ldec_1 = Double.parseDouble("300000000");
				}else if(isSimasKidBSIMDMTM){
					ldec_1 = Double.parseDouble("300000000");
				}
			}else{
				if(ii_bisnis_no > 12 && ii_bisnis_no < 17){
					ldec_1 = Double.parseDouble("30000");
				}
			}
			
			return ldec_1;
		}
	
	public void of_set_up(double adec_up){
		idec_up = adec_up;
	}
	
	//get pay period
	public int of_get_payperiod()
	{
		li_lama=0;
		
		boolean isSmileKidAgency = ((ii_bisnis_no>=9 && ii_bisnis_no<=12) || (ii_bisnis_no>=37 && ii_bisnis_no<=44));
		
		if (isSmileKidAgency){
			if(ii_bisnis_no>=9 && ii_bisnis_no<=12) {
				li_lama = 5;
			}else if(ii_bisnis_no>=37 && ii_bisnis_no<=40){
				li_lama = 10;
			}else if(ii_bisnis_no>=41 && ii_bisnis_no<=44){
				li_lama = 15;
			}
		}else{
			li_lama = super.of_get_payperiod();
		}

		return li_lama;
	}
	
	public static void main(String[] args) {
		
	}
		
}