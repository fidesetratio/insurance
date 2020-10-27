package produk_asuransi;
import java.math.RoundingMode;

import com.ekalife.utils.f_check_end_aktif;
import com.ekalife.utils.f_hit_umur;

/**
 * @author MANTA
 * 24/05/2016
 * SMART LIFE CARE PLUS (BTN)
 */
public class n_prod_212 extends n_prod{
	
	private static final long serialVersionUID = 1L;
	Query query = new Query();
	
	public n_prod_212()
	{
		ii_bisnis_id = 212;
		ii_contract_period = 15;
		ii_age_from = 18;
		ii_age_to = 50;

		indeks_is_forex = 1;
		is_forex = new String[indeks_is_forex];
		is_forex[0] = "01";
		
		indeks_ii_lama_bayar = 8;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 20;
		ii_lama_bayar[1] = 15;
		ii_lama_bayar[2] = 15;
		ii_lama_bayar[3] = 15;
		ii_lama_bayar[4] = 15;
		ii_lama_bayar[5] = 10;
		ii_lama_bayar[6] = 20;
		ii_lama_bayar[7] = 20;
		
		indeks_idec_list_premi = 14;
		idec_list_premi = new double[indeks_idec_list_premi];
		for(int i=0; i<13; i++)
		{
			idec_list_premi[i] = 3000000 + ( 1000000 * (i - 1) );
		}
		
		//1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list = 4;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[0] = 3  ; //tahunan
		ii_pmode_list[1] = 2 ;  //semester
		ii_pmode_list[2] = 1 ;  //Tri
		ii_pmode_list[3] = 6 ;  //bulanan
		
		
		indeks_idec_pct_list = 7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1;    // pmode 4
		idec_pct_list[5] = 1;    // pmode 5
		idec_pct_list[6] = 0.1;// pmode 6			

		flag_account = 2;
		flag_uppremi = 1;
	}
	
	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
		indeks_li_pmode_list=6;
		li_pmode_list = new int[indeks_li_pmode_list];			

		if (ai_no == 9 || ai_no == 14) // add 14 (Nana) add smile proteksi 212-14 helpdesk 147672
		{
			indeks_li_pmode_list=5;
			li_pmode_list[1] = 3;
			li_pmode_list[2] = 1;
			li_pmode_list[3] = 2;
			li_pmode_list[4] = 6;			
		}else if(ii_bisnis_no == 6){ //patar request, Smart Life Protection (Bukopin)  Pembayaran ditambahkan jadi ada bulanan dan tahunan //Chandra
			ib_single_premium=true;
			indeks_li_pmode_list=3;
			li_pmode_list[1] = 6;
			li_pmode_list[2] = 3;
		}else{
			ib_single_premium=true;
			indeks_li_pmode_list=2;
			li_pmode_list[1] = 6 ;
		}
		ii_pmode_list = li_pmode_list;
		indeks_ii_pmode_list=indeks_li_pmode_list;

		indeks_li_pmode=indeks_ii_pmode_list;
		li_pmode = new int[indeks_li_pmode];
		
		for (int i =1 ; i<indeks_li_pmode;i++)
		{
			li_pmode[i] = ii_pmode_list[i];
			
		}
	}

	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr = new f_hit_umur();
		int bln = umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
		
//		if(ii_bisnis_no==1 || ii_bisnis_no==9 || ii_bisnis_no==14) //perubahan sk smile proteksi umur masuk jadi 55 dan mpp jadi 65, helpdesk 128393 //Chandra
//		{
//			ii_age_to = 55;
//		}
		
		if(ii_bisnis_no==1)
		{
			ii_age_to = 55;
		}
		
		//SMiLe Term ROP DMTM Jatim - Mark Valentino 20190820
		if(ii_bisnis_no==12)
			{
				ii_age_to = 50;
			}		
		
		if(ii_age < ii_age_from)
		{
			hsl = "Usia Masuk Plan ini minimum : " + ii_age_from + " tahun";
		}
		if(ii_age > ii_age_to)
		{
			hsl = "Usia Masuk Plan ini maximum : " + ii_age_to + " tahun";
		}
		if(ii_usia_pp < 17)
		{
			hsl = "Usia Pemegang Polis minimum : 17 Tahun !!!";
		}
		if(ii_usia_pp > 85)
		{
			hsl = "Usia Pemegang Polis maximum : 85 Tahun !!!";
		}
		
		return hsl;		
	}
	
	// set beg date
	public void of_set_begdate(int thn, int bln, int tgl)
	{
		int li_month=0;
		
		if(ib_flag_end_age)
		{
			ii_end_age = 60;
		}
		
		li_month = ( ii_contract_period ) * 12;
		adt_bdate.set(thn,bln-1,tgl);
		
		idt_beg_date.set(thn,bln-1,tgl);		
		
		ldt_end.set(thn,bln-1,tgl);	
		ldt_end.add(idt_beg_date.MONTH,li_month);

		idt_end_date.set(thn,bln-1,tgl);			
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		
		if (adt_bdate.DAY_OF_MONTH  == ldt_end.DAY_OF_MONTH )
		{
			idt_end_date.add(ldt_end.DAY_OF_MONTH , -1);
		}

		f_check_end_aktif a = new f_check_end_aktif();
		a.end_aktif(idt_end_date.YEAR, idt_end_date.MONTH, idt_end_date.DAY_OF_MONTH, idt_beg_date.YEAR , idt_beg_date.MONTH, idt_beg_date.DAY_OF_MONTH);

	}

	public int of_get_conperiod(int number_bisnis)
	{
		li_cp = 0;
		li_cp = ii_contract_period;
		
		System.out.println("of get conperiod::"+li_cp);
		return li_cp;		
	}	
	
	public String of_alert_min_premi(double premi)
	{
		String hasil_min_premi = "";
		if(ii_bisnis_no==9 || ii_bisnis_no==14){ // add 14 (Nana) add smile proteksi 212-14 helpdesk 147672
			if(ii_pmode == 3){ //tahunan
				if(premi < 1200000){
					hasil_min_premi = " Premi Minimum untuk Plan ini adalah Rp. 1.200.000,00";
				}
			} else if(ii_pmode == 1){ //triwulan
				if(premi < 300000){
					hasil_min_premi = " Premi Minimum untuk Plan ini adalah Rp. 300.000,00";
				}
			} else if(ii_pmode == 2){ //semesteran
				if(premi < 600000){
					hasil_min_premi = " Premi Minimum untuk Plan ini adalah Rp. 600.000,00";
				}
			} else if(ii_pmode == 6){ //bulanan
				if(premi < 100000){
					hasil_min_premi = " Premi Minimum untuk Plan ini adalah Rp. 100.000,00";
				}
			}
	
		}else if(ii_bisnis_no==10){ // patar timotius
			if(ii_pmode == 6){ //bulanan
				if(premi < 100000){
					hasil_min_premi = " Premi Minimum untuk Plan ini adalah Rp. 100.000,00";
				}
			}
		}else if(ii_bisnis_no==1 ){
			if (premi < 50000){
				hasil_min_premi = " Premi Minimum untuk Plan ini adalah Rp. 50.000,00";
			}			
		}else if(ii_bisnis_no==6){ //Smart Life Protection, tambah cara bayar tahunan //Chandra
			if(ii_pmode == 6){ //bulanan
				if(premi < 100000){
					hasil_min_premi = " Premi Minimum untuk Plan ini adalah Rp. 100.000,00";
				}
			}else if(ii_pmode == 3){ //tahunan
				if(premi < 1200000){
					hasil_min_premi = " Premi Minimum untuk Plan ini adalah Rp. 1.200.000,00";
				}
			}
		}else if(ii_bisnis_no==12){ //SMiLe Term ROP - Mark Valentino 
			if (premi < 100000){
				hasil_min_premi = " Premi Minimum untuk Plan ini adalah Rp. 100.000,00";
			}			
		}else if(ii_bisnis_no==13){ //patar timotius
			if (premi < 100000){
				hasil_min_premi = " Premi Minimum untuk Plan ini adalah Rp. 100.000,00";
			}			
		}else{
			if(premi < 100000){
				hasil_min_premi = " Premi Minimum untuk Plan ini adalah Rp. 100.000,00";
			}
		}
		
		if(ii_bisnis_no==9 || ii_bisnis_no==14){ //Chandra A - 20180523: untuk smile proteksi //add 14 nana add smile proteksi 212-14 helpdesk 147672
			if(ii_pmode == 3){ //tahunan
				if(premi > (2400000 * 12)){
					hasil_min_premi = " Premi Maksimum untuk Plan ini adalah Rp. 28.800.000,00";
				}
			} else if(ii_pmode == 1){ //triwulan
				if(premi > (2400000 * 3)){
					hasil_min_premi = " Premi Maksimum untuk Plan ini adalah Rp. 7.200.000,00";
				}
			} else if(ii_pmode == 2){ //semesteran
				if(premi > (2400000 * 6)){
					hasil_min_premi = " Premi Maksimum untuk Plan ini adalah Rp. 14.400.000,00";
				}
			} else if(ii_pmode == 6){ //bulanan
				if(premi > (2400000 * 1)){
					hasil_min_premi = " Premi Maksimum untuk Plan ini adalah Rp. 2.400.000,00";
				}
			}
		}else if(ii_bisnis_no==10){
			if(premi > 100000){
				hasil_min_premi = " Premi Maksimum untuk Plan ini adalah Rp. 100.000,00";
			}
		}else if(ii_bisnis_no==6){ //Smart Life Protection, tambah cara bayar tahunan //Chandra
			if(ii_pmode == 6){ //bulanan
				if(premi > 2400000){
					hasil_min_premi = " Premi Maksimum untuk Plan ini adalah Rp. 2.400.000,00";
				}
			}else if(ii_pmode == 3){ //tahunan
				if(premi > 28800000){
					hasil_min_premi = " Premi Maksimum untuk Plan ini adalah Rp. 28.800.000,00";
				}
			}
		}else{
			if((ii_bisnis_no==1 ||ii_bisnis_no==3 || ii_bisnis_no==6 || ii_bisnis_no==7 || ii_bisnis_no==8 || ii_bisnis_no==12) && premi > 2400000){
				hasil_min_premi = " Premi Maksimum untuk Plan ini adalah Rp. 2.400.000,00";
			}else if(ii_bisnis_no!=1 && ii_bisnis_no!=3 && ii_bisnis_no!=6 && ii_bisnis_no!=7 && ii_bisnis_no!=9 && ii_bisnis_no!=12 && ii_bisnis_no!=13 && ii_bisnis_no!=14 && premi > 2000000){ // add 14 nana add smile proteksi 212-14 helpdesk 147672
				hasil_min_premi = " Premi Maksimum untuk Plan ini adalah Rp. 2.000.000,00";
			}
		}
		return hasil_min_premi;
	}
	
	public void of_set_premi(double adec_premi)
	{
		//untuk buang digit desimal //chandra
		java.text.DecimalFormat df= new java.text.DecimalFormat("#0.00");
		df.setRoundingMode(RoundingMode.DOWN);
		
		if(ii_bisnis_no == 9 || ii_bisnis_no == 14) { //Chandra A: untuk smile proteksi // add 14 nana add smile proteksi 212-14 helpdesk 147672
			if(ii_pmode == 3){ //tahunan
				idec_premi_main = adec_premi;
				idec_premi = adec_premi;
				idec_up = ((adec_premi * 10) / 12) * 50;
			} else if(ii_pmode == 1){ //triwulan
				idec_premi_main = adec_premi;
				idec_premi = adec_premi;
				idec_up = ((adec_premi * 10) / 3) * 50;
			} else if(ii_pmode == 2){ //semesteran
				idec_premi_main = adec_premi;
				idec_premi = adec_premi;
				idec_up = ((adec_premi * 10) / 6) * 50;
			} else if(ii_pmode == 6){ //bulanan
				idec_premi_main = adec_premi;
				idec_premi = adec_premi;
				idec_up = ((adec_premi * 10) / 1) * 50;
			} else {
				idec_up = 0;
			}
			
			idec_up = Double.parseDouble(df.format(idec_up));
		} else if(ii_bisnis_no==10){ // patar timotius : untuk smile proteksi lkpi
			if(ii_pmode == 6){ // patar timotius bulanan only
				idec_premi_main = adec_premi;
				idec_premi = adec_premi;
				idec_up = ((adec_premi * 10) / 1) * 50;
			}
		} else if(ii_bisnis_no==6){ //untuk perhitungan up slp //chandra
			if(ii_pmode == 6){ //bulanan
				idec_premi_main = adec_premi;
				idec_premi = adec_premi;
				idec_up = ((adec_premi * 10) / 1) * 50;
			} else if(ii_pmode == 3){ //tahunan
				idec_premi_main = adec_premi;
				idec_premi = adec_premi;
				idec_up = ((adec_premi * 10) / 12) * 50;
			}

			idec_up = Double.parseDouble(df.format(idec_up));
		} else {
			idec_premi_main = adec_premi;
			idec_premi = adec_premi;
			idec_up = (adec_premi * 10) * 50;
		}
	}
	
	public double cek_awal()
	{
		Double hasil = new Double(0);
		return hasil; 
	}
	
	public static void main(String[] args) {
		
	}
	
	public int of_get_payperiod()
	{
		li_lama = ii_end_age - ii_age;
		while(li_lama > 15){
			li_lama = li_lama - 1;
		}
		return li_lama;
	}
	
//	public int of_get_conperiod(int number_bisnis)
//	{
//		li_cp = ii_end_age - ii_age;
//		while(li_cp > 15){
//			li_cp = li_cp - 1;
//		}
//		return li_cp;
//	}
	
	public void of_hit_premi(){
		hsl="";
		err="";
		String ls_sql;
		li_lbayar=0;
		li_cp = 0;
		ldec_rate=0;
		
		/**
		 * DMTM SMiLe Term ROP
		 * Mark VP 13/08/2019
		 */
		boolean isSmileTermRopDMTM = (ii_bisnis_no == 12);		
		boolean isSmileBJBDMTM = (ii_bisnis_no == 13);		

		if (ib_single_premium )
		{
			li_lbayar = 1;
		}else{
			li_lbayar = of_get_payperiod();		
			if (ii_bisnis_id >= 800)
			{ 
				li_lbayar = ii_lbayar;
				ii_contract_period = li_lbayar;
			}
			ii_contract_period = li_lbayar;
		}
		
//		Mark Valentino 20190827	SmileTermRopDMTM 	
//		Kalau triwulan, semester, tahunan, jadiin bulanan		
//		if (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 3 )
//		{
//			li_cp = 6;	
//		}
		li_cp = ii_pmode;		

		try {
			if(isSmileTermRopDMTM || isSmileBJBDMTM){
				ii_jenis = 2;				
			}
			Double result = query.selectNilaiNew(ii_jenis, ii_bisnis_id, ii_bisnis_no,is_kurs_id, li_cp, li_lbayar, 0, ii_tahun_ke, ii_age);
			
			if (result==null){
				result =query.selectNilaiNew(ii_jenis, ii_bisnis_id, ii_bisnis_no,is_kurs_id, li_cp, li_lbayar, ii_contract_period, ii_tahun_ke, ii_age);
			}
			
/*
			Double result = query.selectNilai(ii_jenis, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, ii_contract_period, ii_tahun_ke, ii_age);
*/
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
}
