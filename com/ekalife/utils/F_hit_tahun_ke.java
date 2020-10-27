package com.ekalife.utils;

import java.util.Calendar;
import java.util.Date;

public class F_hit_tahun_ke {

	/**
	 * @author Berto
	 * @since Jun 27, 2008 4:34:14 PM
	 * @param args 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static Integer hitTahunKe(Date nextBill, Date begDate,Integer period){
		int i;
		Date tempDate=null;
		
//		logger.info(FormatDate.dateDifference(begDate, nextBill, false));
		if(period==1 && (FormatDate.dateDifference(begDate, nextBill, true)/365) >1 ) period =99;
		
		
		for (i = 1; i <= period; i++) {
			tempDate=FormatDate.add(begDate, Calendar.MONTH, i*12);
			if(Integer.parseInt(FormatDate.getMonth(tempDate))==2){
				tempDate=F_check_next_bill.check_end_bill(nextBill, begDate, 1);
			}			
			if(nextBill.compareTo(tempDate)<0)break;
		}
		return i;
	}

}
