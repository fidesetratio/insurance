// n_prod_181 Eka Proteksi
package produk_asuransi;
import java.math.BigDecimal;
import java.util.Calendar;
/*
 * Created on Jul 27, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import com.ekalife.utils.Common;
import com.ekalife.utils.f_hit_umur;

/**
 * @author HEMILDA
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_181 extends n_prod{
	Query query = new Query();
	public n_prod_181()
	{
//		SK No. 23/EL-SK/XI/99 Eka Proteksi
		ii_bisnis_id = 181;
		ii_contract_period = 60;
		ii_age_from = 1;
		ii_age_to = 48; 
		
		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=6;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 1; // triwulan
		ii_pmode_list[2] = 2; // semesteran
		ii_pmode_list[3] = 3; // tahunan
		ii_pmode_list[4] = 6; // bulanan
		ii_pmode_list[5] = 0 ;  //sekaligus

//		untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 60;

		indeks_ii_lama_bayar=6;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 5;
		ii_lama_bayar[2] = 10;
		ii_lama_bayar[3] = 1;
		ii_lama_bayar[4] = 5;
		ii_lama_bayar[5] = 10;
		
		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1 ;// pmode 6	
		

		idec_min_up01 = 15000000;
	  	idec_min_up02 = 2500;
		
		flag_uppremi=0;
		flag_as=3;
		
		indeks_rider_list=16;
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
		ii_rider[9]=820;
		ii_rider[10]=821;
		ii_rider[11]=823;
		ii_rider[12]=825;
		ii_rider[13]=827;
		ii_rider[14]=828;
		ii_rider[15]=832;
	}
	
	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
		indeks_li_pmode_list=6;
		li_pmode_list = new int[indeks_li_pmode_list];			

		if (ai_no == 1 || ai_no == 4)
		{
			ib_single_premium=true;
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

	public double of_get_rate()
	{
		of_hit_premi();
		return idec_rate;
	}
	
	public void of_hit_premi()
	{
		hsl="";
		err="";
		li_lbayar=0;
		li_cp = 0;
		ldec_rate=0;

		if (ib_single_premium )
		{
			li_lbayar = 1;
		}else{
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			if (ii_bisnis_id >= 800)
			{ 
				li_lbayar = ii_lbayar;
				ii_contract_period = li_lbayar;
			}
		}
		
		switch (ii_bisnis_id)
		{
			case 19 :
				ii_contract_period = ii_lama_bayar[ii_bisnis_no-1];
				break;
			case 20 :
				ii_contract_period = ii_lama_bayar[ii_bisnis_no-1];
				break;
		}
		
		li_cp = ii_pmode;
		//Kalau triwulan, semester, bulanan, jadiin tahunan
		if (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6 )
		{
			li_cp = 3;	
		}

		try {
			//Double result = query.selectNilai(ii_jenis, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, ii_contract_period, ii_tahun_ke, ii_age);
			Double result = query.selectNilaiNew(ii_jenis, ii_bisnis_id, ii_bisnis_no, is_kurs_id, li_cp, li_lbayar, 0, ii_tahun_ke, ii_age);	
			
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
				
				if(!ib_single_premium){
					if(samePPTT == 1){
						Integer jenis_rider = 16;
						if(ii_bisnis_no == 6) jenis_rider=17;
						Double resultpremiInclude = 0.;
						if(ii_usia_pp <= 48){
							resultpremiInclude = query.selectRateRider(is_kurs_id, ii_usia_tt, 0, 814, jenis_rider);
						}
						ldec_rate_include += resultpremiInclude;
						
						//diulang kembali untuk perhitungan premi payor clausenya(berdasarkan premi pokok)
						if (ii_bisnis_id == 802 || ii_bisnis_id == 804)
						{
							//kalau PC atau WPD, jangan dikali idec_add_pct, karna premi udah dikali idec_add_pct
							idec_premi += ldec_rate_include * ldec_rate / ii_permil;
						}else{
							idec_premi += idec_up * ldec_rate_include * idec_add_pct  / ii_permil;
						}
					}else{
						if(ii_age < 17){
							Integer jenis_rider = 16;
							if(ii_bisnis_no == 6) jenis_rider = 17;
							Double resultpremiInclude = 0.;
							if(ii_usia_pp <= 48){
								resultpremiInclude = query.selectRateRider(is_kurs_id, 0, ii_usia_pp, 815, jenis_rider);
							}
							ldec_rate_include += resultpremiInclude;
						}
						
						//diulang kembali untuk perhitungan premi payor clausenya(berdasarkan premi pokok)
						if (ii_bisnis_id == 802 || ii_bisnis_id == 804)
						{
							//kalau PC atau WPD, jangan dikali idec_add_pct, karna premi udah dikali idec_add_pct
							idec_premi += ldec_rate_include * ldec_rate / ii_permil;
						}else{
							idec_premi += idec_premi * ldec_rate_include  / ii_permil;
						}
					}
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
			err=(e.toString());
		} 
	}
	
	public void of_set_begdate(int thn, int bln, int tgl)
	{
		int li_month = 0;
		
		Calendar adt_bdate = Calendar.getInstance();
		adt_bdate.set(thn,bln-1,tgl);

		idt_beg_date.set(thn,bln-1,tgl);				

		ii_end_age = ii_age;

		li_month = ( ii_end_from - ii_end_age ) * 12;

		idt_end_date.set(thn,bln-1,tgl);		
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		
		Calendar ldt_end = Calendar.getInstance();		
		ldt_end.set(thn,bln-1,tgl);		
		ldt_end.add(idt_beg_date.MONTH,li_month);		
		
		if (adt_bdate.DAY_OF_MONTH == ldt_end.DAY_OF_MONTH)
		{
			idt_end_date.add(ldt_end.DAY_OF_MONTH,-1);
		}	

		ii_contract_period = ii_end_from - ii_end_age;
	}
	
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr =new f_hit_umur();
		int hari=umr.hari_powersave(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);

		hsl="";
		if (hari < 30)
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
		
		return hsl;		
	}

	public void of_set_kurs(String as_kurs)
	{
		is_kurs_id = as_kurs;
		ii_contract_period = ii_end_from - ii_end_age;
		of_set_premi(of_get_premi());		
	}

	public int of_get_conperiod(int number_bisnis)
	{
		li_cp=0;
		li_cp =ii_contract_period-ii_age;
		return li_cp;		
	}

	public double of_get_min_up()
	{
		double ldec_1=0;
			
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			ldec_1 = idec_min_up01; 
		}else{
			ldec_1 =idec_min_up02; 
		}
		return ldec_1;
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
	
	public static void main(String[] args) {

				
	}
}
