//n_prod 167 - Hidup Bahagia
package produk_asuransi;

import java.math.BigDecimal;

/**
 * Hidup Bahagia
 * @author Yusuf Sutarko
 * @since Feb 20, 2008 (8:40:03 AM)
 */
public class n_prod_167 extends n_prod {
	
	Query query = new Query();
	
	public n_prod_167(){
		
		//SK. Direksi No. 001/AJS-SK/I/2008
		ii_bisnis_id = 167;
		ii_contract_period = 70;
		ii_age_from = 1;
		ii_age_to = 54;
		
		flag_jenis_plan = 9;

		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		
		// 1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=6;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 1; // triwulan
		ii_pmode_list[2] = 2; // semesteran
		ii_pmode_list[3] = 3; // tahunan
		ii_pmode_list[4] = 6; // bulanan
		ii_pmode_list[5] = 0; // sekaligus

		indeks_ii_lama_bayar=10;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 5;
		ii_lama_bayar[1] = 10;
		ii_lama_bayar[2] = 15;
		ii_lama_bayar[3] = 20;
		ii_lama_bayar[4] = 1;
		ii_lama_bayar[5] = 5;
		ii_lama_bayar[6] = 10;
		ii_lama_bayar[7] = 15;
		ii_lama_bayar[8] = 20;
		ii_lama_bayar[9] = 1;
		
		// idec_min_up01=300000;
		ii_end_from = 70;
		flag_uppremi=0;
		flag_rider =1;
		flag_as = 3;
		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1; // pmode 6
		
		indeks_rider_list=7;
		ii_rider = new int[indeks_rider_list];
		//ii_rider[0]=814;
		ii_rider[0]=815;
		ii_rider[1]=822;
		ii_rider[2]=823;
		ii_rider[3]=825;
		ii_rider[4]=827;
		ii_rider[5]=828;
		ii_rider[6]=832;
	}
	
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) {
		//f_hit_umur umr =new f_hit_umur();
		hsl="";
		
		lm_byr = of_get_payperiod();
		
		if(lm_byr == 5) { //lama bayar 5 tahun
			ii_age_to = 50;
		}else if(lm_byr == 10) { //lama bayar 10 tahun
			ii_age_to = 45;
		}else if(lm_byr == 15) { //lama bayar 15 tahun
			ii_age_to = 40;
		}else if(lm_byr == 20) { //lama bayar 20 tahun
			ii_age_to = 35;			
		}else if(lm_byr == 1) { //lama bayar sekaligus
			ii_age_to = 54;
		}
		if (ii_age > ii_age_to) {
			hsl = "Usia Masuk Plan ini maximum : " + ii_age_to;
		}
		//
		if (ii_usia_pp > 85) {
			hsl = "Usia Pemegang Polis maximum : 85 Tahun !!!"; // permintaan Achmad Anwarudin Tuesday, November 06, 2007 11:03 AM
		} else if (ii_usia_pp < 18) {
			hsl = "Usia Pemegang Polis minimum : 18 Tahun !!!";
		}
		return hsl;		
	}	
	
	public void of_hit_premi()	{
		hsl="";
		err="";
		li_lbayar=0;
		li_cp=0;
		li_ltanggung=0;
		ldec_rate=0;

		if (ib_single_premium)	{
			li_lbayar = 1; 
		}else{ 
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		}
		li_ltanggung = ii_end_from;
		
		if (ii_bisnis_id >= 800){ 
			li_lbayar = ii_lbayar;
			// kalo rider maka ambil dari contract period
			li_ltanggung = li_lbayar;
		}

		li_cp = ii_pmode;
		//Kalau triwulan, semester, bulanan, jadiin tahunan
		if (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6){
			 li_cp = 3;
		}

		try {
			//Double result = query.selectNilai(ii_jenis, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, li_ltanggung, ii_tahun_ke, ii_age);
			Double result = query.selectNilaiNew(ii_jenis, ii_bisnis_id, ii_bisnis_no, is_kurs_id, li_cp, li_lbayar, 0, ii_tahun_ke, ii_age);	
			
			if (result != null) {
				ldec_rate = result.doubleValue();
				if (ii_bisnis_id == 814 || ii_bisnis_id == 815) {
					// kalau PC atau WPD, jangan dikali idec_add_pct, karna
					// premi udah dikali idec_add_pct
					idec_premi = idec_up * ldec_rate / ii_permil;
				} else {
					idec_premi = idec_up * ldec_rate * idec_add_pct / ii_permil;
				}
				idec_rate = ldec_rate;

				if (ii_bisnis_id < 800) {
					idec_premi_main = idec_premi;
				}

			} else {
				hsl = "Tidak ada data rate";
			}
		} catch (Exception e) {
			err = e.toString();
		} 
		
	}

	public double of_hit_premi_netto(){
		li_lbayar=0;
		li_cp=0;
		li_ltanggung=0;
		li_jenis=0;
		ldec_rate=0;

		li_jenis = 8;
		if (ib_single_premium){ 
			li_lbayar = 1 ;
		}else{ 
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		}

		li_ltanggung = ii_end_from;
		if (ii_bisnis_id >= 800){ 
			li_lbayar = ii_lbayar;
			// kalo rider maka ambil dari contract period
			li_ltanggung = li_lbayar;
		}
		li_cp = ii_pmode;
//		   Kalau triwulan, semester, bulanan, jadiin tahunan
		if  (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6){
			li_cp = 3;
		}

		try {
			//Double result = query.selectNilai(li_jenis, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, li_ltanggung, ii_tahun_ke, ii_age);
			Double result = query.selectNilaiNew(ii_jenis, ii_bisnis_id, ii_bisnis_no, is_kurs_id, li_cp, li_lbayar, 0, ii_tahun_ke, ii_age);
			
			if (result != null) {
				ldec_rate = result.doubleValue();
				idec_premi = idec_up * ldec_rate * idec_add_pct / ii_permil;

				idec_rate = ldec_rate;

				if (ii_bisnis_id < 800) {
					idec_premi_main = idec_premi;
				}
			} else {
				hsl = "Tidak ada data rate";
			}
		} catch (Exception e) {
			err = e.toString();
		} 

		return idec_premi;
	}

	public void of_set_begdate(int thn, int bln, int tgl){
		int li_month = 0;

		adt_bdate.set(thn,bln-1,tgl);

		idt_beg_date.set(thn,bln-1,tgl);		

		if (ib_flag_end_age){
			ii_end_age = ii_age;
		}
		li_month = ( ii_end_from - ii_end_age ) * 12;
		
		idt_end_date.set(thn,bln-1,tgl);		
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		idt_end_date.add(idt_end_date.DAY_OF_MONTH , -1);

		ii_contract_period = ii_end_from - ii_end_age;	
	}
	
	public void of_set_bisnis_no(int ai_no){
		ii_bisnis_no = ai_no;

		if (ai_no == 5 || ai_no == 10){    // lsdbs_number = 5 Sekaligus
			indeks_li_pmode_list=2;
			li_pmode_list = new int[indeks_li_pmode_list];
			li_pmode_list[1]=0;
			ii_pmode_list = li_pmode_list;
			indeks_ii_pmode_list=indeks_li_pmode_list;
		}

		if (ii_bisnis_id < 800) {
			ii_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			of_set_pmode(ii_pmode_list[1]);			 
		}else{
			of_set_age();
			of_set_up(idec_up)	;			
		}
		indeks_li_pmode=indeks_ii_pmode_list;
		li_pmode = new int[indeks_li_pmode];
		
		for (int i =1 ; i<indeks_li_pmode;i++){
			li_pmode[i] = ii_pmode_list[i];
		}		
	}
	
	public int of_get_conperiod(int number_bisnis){	
		li_cp=0;
		li_cp=ii_contract_period-ii_age;
		return li_cp;	
	}
	
	public double of_get_min_up(){
		return 19999999;
	}
		
	
	
	public void count_rate_814(int klas, int unit, int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period, int payperiod) {
		ldec_temp5 = 0;
		ldec_rate5 = 0;
		String err = "";
		String err1 = "";
		double hasil = 0;

		int li_kali = 1;
		switch (pmode) {
		case 1:
			li_kali = 4;
			break;
		case 2:
			li_kali = 2;
			break;
		case 6:
			li_kali = 12;
			break;
		}
		int li_umur_pp = 0;
		try {
			Double result = query.selectRateRider(kurs, umurttg, li_umur_pp, kode_produk, nomor_produk);
			if (result != null) {
				hasil = result.doubleValue();
			} else {
				err1 = "Rate Asuransi Waiver Tidak Ada!";
			}
		} catch (Exception e) {
			err = e.toString();
		}
		ldec_temp5 = ((premi * li_kali / 1000) * hasil) * 0.1;
		int decimalPlace = 2;
		BigDecimal bd = new BigDecimal(ldec_temp5);
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		ldec_temp5 = bd.doubleValue();
		ldec_rate5 = hasil;
		BigDecimal jm = new BigDecimal(ldec_rate5);
		jm = jm.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		ldec_rate5 = jm.doubleValue();
	}	
	
	public void count_rate_815(int klas, int unit, int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period, int payperiod) {
		String err = "";
		String err1 = "";
		double hasil = 0;
		ldec_temp6 = 0;
		ldec_rate6 = 0;

		double ldec_pct1 = 1;
		int li_kali = 1;
		switch (pmode) {
		case 1:// triwulan
			li_kali = 4;
			ldec_pct1 = 0.27;
			break;
		case 2: // semesteran
			li_kali = 2;
			ldec_pct1 = 0.525;
			break;
		case 6:// BULANAN
			li_kali = 12;
			ldec_pct1 = 0.1;
			break;
		}

		int li_umur_tt = 0;
		try {
			Double result = query.selectRateRider(kurs, 0, umurpp, kode_produk, nomor_produk);

			if (result != null) {
				hasil = result.doubleValue();
			} else {
				err1 = "Rate Asuransi Payor's Clause Tidak Ada!";
			}
		} catch (Exception e) {
			err = e.toString();
		}			 if(!err1.equals("")) throw new RuntimeException(err1);
		 else if(!err.equals("")) throw new RuntimeException(err);


		ldec_temp6 = ((premi * li_kali / 1000) * hasil) * 0.1;
		int decimalPlace = 2;
		BigDecimal bd = new BigDecimal(ldec_temp6);
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		ldec_temp6 = bd.doubleValue();
		ldec_rate6 = hasil;
		BigDecimal jm = new BigDecimal(ldec_rate6);
		jm = jm.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		ldec_rate6 = jm.doubleValue();
	}	
	
	public void count_rate_827(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
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
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, nomor_produk);
				
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
		
	

	public String cek_rider_number(int nomor_produk,int kode_produk,int umurttg, int umurpp, double up, double premi, int pmode,int hub, String kurs, int pay_period) {
		String hsl="";

		if (kode_produk != 814 && kode_produk != 815) {
			hsl = "Plan ini hanya dapat mengambil Payor's Clause / Waiver of Premium Disability";
		}else {
			if(hub != 1) { //jika PP <> TT
				//a. dan Pemegang Polis berusia 17 <= x <= (60-MPP) tahun, maka diwajibkan mengikuti asuransi tambahan (Rider) Payor’s Clause
				if(17 <= umurpp && umurpp <= (60 - pay_period)) {
					// HANYA BOLEH DAN WAJIB IKUT PAYOR'S CLAUSE
					//if(kode_produk == 815 && (nomor_produk >=7 && nomor produk <= 10))
				//b. dan Pemegang Polis berusia >(60-MPP) tahun, maka tidak dapat mengikuti asuransi tambahan (Rider) Payor’s Clause
				}else if(umurpp > (60 - pay_period)) {
					// TIDAK BOLEH IKUT PAYOR'S CLAUSE
					if(kode_produk==815){
						hsl = "Untuk Pemegang Polis berusia > "+(60 - pay_period)+"tahun,tidak dapat mengambil Payor's Clause / Waiver of Premium Disability";
					}
				}
			}else { //jika PP = TT
				// dan Pemegang Polis berusia 17 <= x <= (55-MPP) tahun, maka diwajibkan mengikuti asuransi tambahan (Rider) Waiver of Premium Disability
				if(17 <= umurpp && umurpp <= (55 - pay_period)) {
					// WAJIB IKUT WPD
				}
			}
		}
	
		return hsl;
	}
	
	public static void main(String[] args) {}
	
}