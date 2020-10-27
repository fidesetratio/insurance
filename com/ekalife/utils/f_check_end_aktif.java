package com.ekalife.utils;
import java.util.*;
/*
 * Created on Aug 3, 2005
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
public class f_check_end_aktif {

	public void end_aktif(int thn1, int bln1, int tgl1, int thn2 , int bln2, int tgl2) {
		int li_bdate=0;
		int li_eaktif = 0;
		Calendar adt_end_aktif = Calendar.getInstance(); 
		adt_end_aktif.set(thn1,bln1-1,tgl1);
		Calendar adt_beg_date = Calendar.getInstance(); 
		adt_beg_date.set(thn2,bln2-1,tgl2);

		if (adt_beg_date.get(Calendar.DAY_OF_MONTH) < 28) 
		{
			return;
		}
		if (adt_end_aktif == null) 
		{
			return;
		}

		do
		{
			li_bdate = adt_beg_date.get(Calendar.DAY_OF_MONTH);
			li_eaktif = adt_end_aktif.get(Calendar.DAY_OF_MONTH);
			if (li_eaktif >= li_bdate)
			{
				adt_end_aktif.add(Calendar.DAY_OF_MONTH , -1);
			}
		}
		while (li_bdate <= li_eaktif);

	}
	//dian dipake htg bunga tunggakan
	public static void end_aktif(Date endDate, Date begDate) {
		if(Integer.parseInt(FormatDate.getDay(begDate))<28)return;
		if(endDate==null)return;
		Integer li_bdate=0;
		Integer li_eaktif=0;
		do {
			li_bdate=Integer.parseInt(FormatDate.getDay(begDate));
			li_eaktif=Integer.parseInt(FormatDate.getDay(endDate));
			if(li_eaktif>=li_bdate){
				endDate=FormatDate.add(endDate, Calendar.DAY_OF_MONTH, -1);
			}
		} while (li_bdate>li_eaktif);

	}


	public static void main(String[] args) {
	}
}
