// 207 PROGRESSIVE SAVE SYARIAH
package produk_asuransi;

import java.math.BigDecimal;

import com.ekalife.utils.f_check_end_aktif;
import com.ekalife.utils.f_hit_umur;

public class n_prod_207 extends n_prod {

	public n_prod_207() {
		
		// SK 098/AJS-SK/X/201
		ii_bisnis_id = 207;
		ii_contract_period = 1;
		ii_age_from = 1;
		ii_age_to = 68;

		indeks_is_forex = 1;
		is_forex = new String[indeks_is_forex];
		is_forex[0] = "01";
		//is_forex[1] = "02";

		indeks_ii_pmode_list = 5;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 3 ;  //tahunan
		ii_pmode_list[2] = 2 ;  //semesteran
		ii_pmode_list[3] = 1 ;  //triwulanan
		ii_pmode_list[4] = 6 ;  //bulanan

		// untuk hitung end date ( 79 - issue_date )
		ii_end_from = 4;
		ib_flag_end_age = false;

		idec_min_up01 = 5000000;
		idec_min_up02 = 500;

		indeks_ii_lama_bayar = 3;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 4;
		ii_lama_bayar[1] = 4;
		ii_lama_bayar[2] = 4;

		indeks_idec_list_premi = 17;
		idec_list_premi = new double[indeks_idec_list_premi];
		usia_nol = 0;
		flag_uppremi = 1;
		kode_flag = 1;
		flag_account = 2;

		indeks_mgi=5;
		mgi = new int[indeks_mgi];
		mgi[0]= 1;
		mgi[1]= 3;
		mgi[2]= 6;
		mgi[3]= 12;
		
		indeks_rider_list=8;
		ii_rider = new int[indeks_rider_list];
		ii_rider[0]=813;
		ii_rider[1]=818;
		ii_rider[2]=819;
		ii_rider[3]=820;
		ii_rider[4]=822;
		ii_rider[5]=823;
		ii_rider[6]=825;
		ii_rider[7]=832;
	}
	
	public void of_set_begdate(int thn, int bln, int tgl)
	{
		int li_month=0;

		if (ib_flag_end_age)
		{
			ii_end_age = ii_age;
		}
		li_month = ( ii_contract_period ) * 12;

		adt_bdate.set(thn,bln-1,tgl);

		idt_beg_date.set(thn,bln-1,tgl);		

		idt_end_date.set(thn,bln-1,tgl);			
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		idt_end_date.add(idt_end_date.DAY_OF_MONTH , -1);
		
		f_check_end_aktif a = new f_check_end_aktif();
		a.end_aktif(idt_end_date.YEAR, idt_end_date.MONTH, idt_end_date.DAY_OF_MONTH, idt_beg_date.YEAR , idt_beg_date.MONTH, idt_beg_date.DAY_OF_MONTH);
		ii_contract_period = ii_contract_period;
	}

	@Override
	public void cek_flag_agen(int kode_produk, int no_produk, int flag_bulanan){
		flag_powersave = 0;
		switch (no_produk) {
		case 1:
			flag_as = 0; //regional
			flag_powersave = f_flag_rate_powersave(ii_bisnis_id, 1, flag_bulanan);
			//isProductBancass = true;
			break;
		case 3:
			isProductBancass=true;
			flag_powersave = f_flag_rate_powersave(ii_bisnis_id, 1, flag_bulanan);
			//isProductBancass = true;
			break;
		}
	}

	// get bisnis no
	@Override
	public int of_get_bisnis_no(int flag_bulanan){
		int flag_powersave = 0;
		switch (ii_bisnis_no) {
		case 1:
			flag_as = 0; //regional
			flag_powersave = f_flag_rate_powersave(ii_bisnis_id, 1, flag_bulanan);
			//isProductBancass = true;
			break;
		case 3:
			isProductBancass = true;
			flag_powersave = f_flag_rate_powersave(ii_bisnis_id, 1, flag_bulanan);
			//isProductBancass = true;
			break;
		}
		return flag_powersave;
	}

//	public double of_get_min_up() {
//		double ldec_1 = new Double("99999999999");
//		if (is_kurs_id.equalsIgnoreCase("01")) {
//			if (ii_bisnis_no == 1) {
//				ldec_1 = 500000;
//			}
//		} else {
//			if (ii_bisnis_no == 1) {
//				ldec_1 = 50;
//			}
//		}
//		return ldec_1;
//	}
	
	public double of_get_min_up()
	{
		double ldec_1=2000000;
			
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			if (ii_pmode == 1)
			{
				//triwulan
				ldec_1 = new Double(2000000/4); 
			}else{
				if (ii_pmode == 2)
				{
					//semesteran
					ldec_1 = new Double(2000000/2); 
				}else{
					if (ii_pmode == 6)
					{
						//bulanan
						ldec_1 = new Double(2000000/10); 
					}
				}
			}
			
		}else{
			ldec_1=200;
			if (ii_pmode == 1)
			{
				//triwulan
				ldec_1 = 50; 
			}else{
				if (ii_pmode == 2)
				{
					//semesteran
					ldec_1 = 100; 
				}else{
					if (ii_pmode == 6)
					{
						//bulanan
						ldec_1 = new Double(200/10); 
					}
				}
			}
		}
		return ldec_1;
	}
	
//	public double of_get_max_up(){
//		double ldec_1 = new Double("99999999999");
//		if (is_kurs_id.equalsIgnoreCase("01")) {
//			if(ii_bisnis_no == 1){
//				//ldec_1 = Double.parseDouble("25000000000");
//				ldec_1 = Double.parseDouble("75000000");
//			}
//		}else{
//			if(ii_bisnis_no == 1){
//				//ldec_1 = Double.parseDouble("2500000");
//				ldec_1 = Double.parseDouble("7500");
//			}
//		}
//		
//		return ldec_1;
//	}

	public double of_get_rate() {
		return idec_rate;
	}

	public void of_set_kurs(String as_kurs) {
		is_kurs_id = as_kurs;
		if (as_kurs.equalsIgnoreCase("02")) {
			for (int i = 0; i < 16; i++) {
				idec_list_premi[i] = 1000 + (100 * (i - 1));
			}
		} else {
			for (int i = 0; i < 16; i++) {
				idec_list_premi[i] = 10000000 + (1000000 * (i - 1));
			}
		}
	}

	public void of_set_premi(double adec_premi) {
		idec_premi = adec_premi;
		idec_rate = 1000;
		double jumlah_up = 0;
		if (is_kurs_id.equalsIgnoreCase("01")) {
			// diatas Rp 1.000.000.000
			if (idec_premi > 1000000000) {
				jumlah_up = 1000000000;
			} else {
				jumlah_up = idec_premi;
			}
		} else {
			// di atas U$ 100.000
			if (idec_premi > 100000) {
				jumlah_up = 100000;
			} else {
				jumlah_up = idec_premi;
			}
		}
//		//IM no.050/IM-DIR/VI/2009 perubahan batas maksimum premi (mulai tgl 26/6/2009)
//		if (is_kurs_id.equalsIgnoreCase("01")) {
//			// diatas Rp 25.000.000.000
//			if (idec_premi/1000 > 25000000) {
//				jumlah_up = 25000000*1000;
//			} else {
//				jumlah_up = idec_premi;
//			}
//		} else {
//			// di atas U$ 2.500.000
//			if (idec_premi > 2500000) {
//				jumlah_up = 2500000;
//			} else {
//				jumlah_up = idec_premi;
//			}
//		}
		of_set_up(jumlah_up);
	}

	public String of_check_usia(int tahun1, int bulan1, int tanggal1,
			int tahun2, int bulan2, int tanggal2, int lm_byr, int nomor_produk) {
		f_hit_umur umr = new f_hit_umur();
		int bln = umr.bulan(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);

		hsl = "";
		if (bln < 12) {
			hsl = "Usia Masuk Plan ini minimum : " + ii_age_from;
		}
		if (ii_age > ii_age_to) {
			hsl = "Usia Masuk Plan ini maximum : " + ii_age_to;
		}
		if (ii_usia_pp > 85) {
			hsl = "Usia Pemegang Polis maximum : 85 Tahun !!!";
		}
		if (ii_usia_pp < 17) {
			hsl = "Usia Pemegang Polis minimum : 17 Tahun !!!";
		}

		return hsl;
	}

	public static void main(String[] args) {

	}

}
