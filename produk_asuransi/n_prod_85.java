// n_prod_85 Eka Proteksi Rupiah
package produk_asuransi;
import com.ekalife.utils.f_hit_umur;

/**
 * @author HEMILDA
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_85 extends n_prod{

	public n_prod_85()
	{
//		sk No. 014/EL-SK/VII/03
		ii_bisnis_id = 85;
		ii_contract_period = 60;
		ii_age_from = 1;
		ii_age_to = 48;
		
		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";

//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=3;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 3;   //tahunan
		ii_pmode_list[2] = 0;  //sekaligus
		
		indeks_ii_lama_bayar=4;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 5;
		ii_lama_bayar[2] = 10;

		ii_end_from = 60;
		idec_min_up01 = 7000000;
		flag_uppremi=0;
		
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		usia_nol = 1;
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
		
		idt_end_date.set(thn,bln-1,tgl);			
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		idt_end_date.add(idt_end_date.DAY_OF_MONTH , -1);
		ii_contract_period = ii_end_from - ii_end_age;
	}	
	
	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
		indeks_li_pmode_list=4;
		li_pmode_list = new int[indeks_li_pmode_list];
		if (ai_no == 1)    // lsdbs_number = 5 Sekaligus
		{
			indeks_li_pmode=2;
			li_pmode_list[1] = 0;
		}else{
			indeks_li_pmode=4;
			li_pmode_list[1] = 3;
			li_pmode_list[2] = 1;
			li_pmode_list[3] = 2;
		}
		ii_pmode_list = li_pmode_list;
		indeks_ii_pmode_list=indeks_li_pmode_list;

		of_set_age();
		of_set_up(idec_up);
		of_set_pmode(li_pmode_list[1]);
		
		li_pmode = new int[indeks_li_pmode];
		indeks_li_pmode=indeks_ii_pmode_list;
		li_pmode = new int[indeks_li_pmode];
		
		for (int i =1 ; i<indeks_li_pmode;i++)
		{
			li_pmode[i] = ii_pmode_list[i];
			
		}
		
	}	

	public int of_get_conperiod(int number_bisnis)
	{
		li_cp=0;
		li_cp =ii_contract_period-ii_age;
		return li_cp;		
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
	
	public static void main(String[] args) {
				n_prod_85 a= new n_prod_85();
				a.of_set_bisnis_no(1);
	}
}
