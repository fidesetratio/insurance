// n_prod_157 Endowment 20
package produk_asuransi;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_check_end_aktif;

/**
 * @author HEMILDA
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_157 extends n_prod {
	Query query = new Query();
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	public n_prod_157()
	{

		ii_bisnis_id = 157;
		ii_contract_period = 20;
		ii_age_from = 20;
		ii_age_to = 55;

		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";

//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 0;   //sekaligus

//		untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 20;
		ib_flag_end_age = false;
	  
		indeks_ii_lama_bayar=5;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 1;
		ii_lama_bayar[2] = 1;
		ii_lama_bayar[3] = 1;
		ii_lama_bayar[4] = 1;

		isProductBancass = true;
		flag_endowment = 1;
		idec_min_up01 = 1;  //Rp
		flag_uppremi=0;
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		
		indeks_discount = 6;
		discount = new double[indeks_discount];
		discount[1] = 0.25;
		discount[2] = 0;
		discount[3] = 0;
		discount[4] = 0.25;
		discount[5] = 0;
		
		 indeks_perusahaan =6;
		 perusahaan = new String[indeks_perusahaan];
		 perusahaan[1] = "KSO.PERKASA ABADI";
		 perusahaan[2] = "MUTIARA MATAHARI MAKMUR SEJAHTERA";
		 perusahaan[3] = "MUTIARA MATAHARI MAKMUR SEJAHTERA";
		 perusahaan[4] = "TIMURJAYA TELADAN";
		 perusahaan[5] = "FAMILY INTI SEJATI";

		   
	}

	public int of_get_conperiod(int number_bisnis)
	{
		li_cp=0;
		if (number_bisnis != 4)
		{
			li_cp =	ii_end_from ;
		}else{
			li_cp =	25 ;
			ii_end_from = 25;
			ii_contract_period =25;
		}
		return li_cp;		
	}
	
	public void of_hit_premi()
	{
		hsl="";
		err="";
		double rate =0;
		/*logger.info("adt_bdate " + adt_bdate);
		logger.info("ii_bisnis_no "+ ii_bisnis_no);
		logger.info("ii_bisnis_id "+ ii_bisnis_id);*/
		Integer tanggal1= new Integer(adt_bdate.getTime().getDate());
		Integer bulan1 = new Integer(adt_bdate.getTime().getMonth()+1);
		Integer tahun1 = new Integer(adt_bdate.getTime().getYear()+1900);
		String tgl_beg_date1 = FormatString.rpad("0",Integer.toString(tanggal1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan1.intValue()),2)+"/"+Integer.toString(tahun1.intValue());
	
		
		//idec_premi = idec_up * 0.15;	
		try {
			
			Double result = query.select_rate_endow(new Integer(ii_bisnis_id),new Integer(ii_bisnis_no),df.parse(tgl_beg_date1));
			
			if(result != null) 
			{
				rate = result.doubleValue();
				idec_premi = idec_up * rate / 100;	
			}else{
				rate = 0;
				hsl = "Tidak ada rate";
			}
		
		}catch (Exception e) {
			err=e.toString();
		}
		
		idec_rate = rate;
		idec_premi_main = idec_premi;	
	}	
	
	public double of_get_rate()
	{
		of_hit_premi();
		return idec_rate;	
	}	

	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;

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

	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{

		hsl="";
		if (nomor_produk == 1)
		{
			if (ii_age < ii_age_from)
			{
				hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
			}
			if (ii_age > ii_age_to)
			{
				hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
			}
		}else{
			if (ii_age < 15)
			{
				hsl="Usia Masuk Plan ini minimum : 15 tahun";
			}
			if (ii_age > 55)
			{
				hsl="Usia Masuk Plan ini maximum : 55 tahun";
			}
		}
		if (ii_usia_pp < 17)
		{
			hsl="Usia Pemegang Polis minimum : 17 Tahun !!!";
		}
		
		return hsl;		
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
		
		if (adt_bdate.DAY_OF_MONTH  == ldt_end.DAY_OF_MONTH )
		{
			idt_end_date.add(ldt_end.DAY_OF_MONTH , -1);
		}

		f_check_end_aktif a = new f_check_end_aktif();
		a.end_aktif(idt_end_date.YEAR, idt_end_date.MONTH, idt_end_date.DAY_OF_MONTH, idt_beg_date.YEAR , idt_beg_date.MONTH, idt_beg_date.DAY_OF_MONTH);

	}

	public static void main(String[] args) {

	}
}