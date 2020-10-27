package com.ekalife.utils;


/*
 * Created on Aug 15, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author HEMILDA
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class f_cek_tanggal {
	int[] MonthDays = {31,29,31,30,31,30,31,31,30,31,30,31};
					
	public boolean cek(int thn, int bln, int tgl)
	{

		if ((bln>12) || (bln<=0)) {
			return false;
		}
		if ((tgl > (MonthDays[bln-1])) || (tgl<=0))  {
			
			return false;
		}

		if ((bln==2)&& (tgl==MonthDays[1])&&(thn%4>0) ) 
		{
			return false;
		}	
		if (Integer.toString(thn).length()!=4 ) 
		{
			return false;
		}	
		return true;
	}

	public static void main(String[] args) {
	
	}
}
