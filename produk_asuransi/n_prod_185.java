// n_prod_185 - Super Sejahtera NEW
package produk_asuransi;

import java.math.BigDecimal;

import com.ekalife.utils.f_hit_umur;
/*
 * Created on Oct 14, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/** @author Deddy
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_185 extends n_prod {
	Query query = new Query();
	public n_prod_185()
	{
//		SK No. 007/EL-SK/I/97
	  ii_bisnis_id = 185;
	  ii_contract_period = 79;
	  ii_age_from = 1;
	  ii_age_to = 55;

	  indeks_is_forex=2;
	  is_forex= new String[indeks_is_forex];
	  is_forex[0]="01";
	  is_forex[1]="02";
	  
//		1..4 cuma id, 0..3 di lst_cara_bayar
	  indeks_ii_pmode_list=5;
      ii_pmode_list = new int[indeks_ii_pmode_list];	
	  ii_pmode_list[1] = 1;   //triwulanan
	  ii_pmode_list[2] = 2;   //semesteran
	  ii_pmode_list[3] = 3;   //tahunan
	  ii_pmode_list[4] = 6; // bulanan
	  
	  idec_min_up01 = 100000000;
	  idec_min_up02 = 10000;
	  //idec_min_up01 = 25000000;
	  //idec_min_up02 = 10000;
	  
	  indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1; // pmode 6
		
	  indeks_ii_lama_bayar=20;
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
	  ii_lama_bayar[10] = 5;
	  ii_lama_bayar[11] = 10;
	  ii_lama_bayar[12] = 15;
	  ii_lama_bayar[13] = 20;
	  ii_lama_bayar[14] = 1;
	  ii_lama_bayar[15] = 5;
	  ii_lama_bayar[16] = 10;
	  ii_lama_bayar[17] = 15;
	  ii_lama_bayar[18] = 20;
	  ii_lama_bayar[19] = 1;

	  ii_end_from = 79;
//		ib_flag_end_age = False
		flag_uppremi=0;
		flag_account=2;
		
		indeks_rider_list=8;
		ii_rider = new int[indeks_rider_list];
		ii_rider[0]=800;
		ii_rider[1]=801;
		ii_rider[2]=802;
		ii_rider[3]=803;
		ii_rider[4]=804;
		ii_rider[5]=822;
//		ii_rider[6]=823;
//		ii_rider[7]=825;
		ii_rider[6]=820;
		ii_rider[7]=832;
	}
	
	public void of_hit_premi()
	{
		hsl="";
		err="";
		li_lbayar=0;
		li_cp =0;
		li_ltanggung=0;
		ldec_rate=0;
	
		if (ib_single_premium)
		{ 
			li_lbayar = 1; 
		}else{ 
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		}

		li_ltanggung = ii_end_from;
		if (ii_bisnis_id >= 800)
		{ 
			li_lbayar = ii_lbayar;
			
			// kalo rider maka ambil dari contract period
			li_ltanggung = li_lbayar;
		}
		li_cp = ii_pmode;
//		   Kalau triwulan, semester, bulanan, jadiin tahunan
		if (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6 ||ii_pmode == 3)
		{
			li_cp = 3;
		}
		

//		   ii_contract_period diganti ii_end_from
//		   karena lama_tanggung di table premi sama, 79 th

		try {
			Double result = query.selectNilaiNew(ii_jenis, ii_bisnis_id,ii_bisnis_no, is_kurs_id, li_cp, li_lbayar, 0, ii_tahun_ke, ii_age);
			
			if(result == null) {
				result = query.selectNilaiNew(ii_jenis, ii_bisnis_id,ii_bisnis_no, is_kurs_id, li_cp, li_lbayar, 0, ii_tahun_ke, ii_age);
			}
			
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
		  err=e.toString();
		} 	
	}

	public double of_hit_premi_netto()
	{
		li_lbayar = 0;
		li_cp = 0;
		li_ltanggung = 0;  
		li_jenis = 0;
		ldec_rate = 0;

		if (ib_single_premium)
		{ 
			li_lbayar = 1; 
		}else{ 
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		}

		li_ltanggung = ii_end_from;
		if (ii_bisnis_id >= 800)  
		{
			li_lbayar = ii_lbayar;
			// kalo rider maka ambil dari contract period
			li_ltanggung = li_lbayar;
		}	
		
		li_cp = ii_pmode;
//		   Kalau triwulan, semester, bulanan, jadiin tahunan
		if (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6)
		{
			li_cp = 3;
		}
		li_jenis = 8 ;
		try {
			Double result = query.selectNilaiNew(li_jenis, ii_bisnis_id,ii_bisnis_no, is_kurs_id, li_cp, li_lbayar, 0, ii_tahun_ke, ii_age);
			
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
		}	else{
			hsl="Tidak ada data rate";
		}
		}
		catch (Exception e) {
		  err=e.toString();
		} 
		return idec_premi;	
	}
	
	public void of_set_begdate(int thn, int bln, int tgl)
	{
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

	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;

		if (ai_no == 5 || ai_no == 10 || ai_no == 15 || ai_no == 20 )
		{    // lsdbs_number = 5 Sekaligus
			indeks_li_pmode_list=2;
			li_pmode_list = new int[indeks_li_pmode_list];
			li_pmode_list[1] = 0;
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

	public int of_get_conperiod(int number_bisnis)
	{	li_cp=0;
		li_cp=ii_contract_period-ii_age;
		return li_cp;	
	}
	
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr =new f_hit_umur();
		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);

		hsl="";
		if (bln < 3)
		{
			hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
		}
		if (ii_age > ii_age_to)
		{
			hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
		}
		if (ii_usia_pp > 85)
		{
			hsl="Usia Pemegang Polis maximum : 85 Tahun !!!"; // permintaan Achmad Anwarudin Tuesday, November 06, 2007 11:03 AM
		}		
		if (ii_usia_pp < 17)
		{
			hsl="Usia Pemegang Polis minimum : 17 Tahun !!!";
		}
			
		return hsl;		
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
					if (total_premi < 405000)
					{
						hsl="Minimum Total Premi Triwulanan (3 bulanan ) : Rp. 405.000,-";
					}
					break;
				case 2:
					if (total_premi < 787500)
					{
						hsl="Minimum Total Premi Semesteran (6 bulanan ) : Rp. 787.500,-";
					}
					break;
				case 3:
					if (total_premi < 1500000)
					{
						hsl="Minimum Total Premi Tahunan : Rp. 1.500.000,-";
					}
					break;
				case 6:
					if (total_premi < 150000)
					{
						hsl="Minimum Total Premi Bulanan (1 bulanan) : Rp. 150.000,-";
					}
					break;
				case 0:
					if (total_premi < 5000000)
					{
						hsl="Minimum Total Premi Sekaligus : Rp. 5.000.000,-";
					}
					break;
			}
		}else if(kurs.equalsIgnoreCase("02"))
		{
			switch (pmode_id.intValue())
			{
	
				case 1:
					if (total_premi < 40.5)
					{
						hsl="Minimum Total Premi Triwulanan (3 bulanan ) : US$ 40,5";
					}
					break;
				case 2:
					if (total_premi < 78.75)
					{
						hsl="Minimum Total Premi Semesteran (6 bulanan ) : US$ 78,75";
					}
					break;
				case 3:
					if (total_premi < 150)
					{
						hsl="Minimum Total Premi Tahunan : US$ 150,-";
					}
					break;
				case 6:
					if (total_premi < 15)
					{
						hsl="Minimum Total Premi Bulanan (1 bulanan) : US$ 15,-";
					}
					break;
				case 0:
					if (total_premi < 500)
					{
						hsl="Minimum Total Premi Sekaligus : US$ 500,-";
					}
					break;
			}
		}
	return hsl;
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
		return hasil_min_up;
	}
	
	
	public static void main(String[] args) {
	
	}
}
