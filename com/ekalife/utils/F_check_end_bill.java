package com.ekalife.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class F_check_end_bill {
	
	protected static final Log logger = LogFactory.getLog( F_check_end_bill.class );

	/**
	 * Fungsi ini di ambil dari PB f_check_end_bill
	 * @author Berto
	 * @since Jun 27, 2008 2:52:24 PM
	 * @param args 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static Date check_end_bill(Date nextBill, Date begDate, Integer month){
		try {
			nextBill= FormatDate.add(nextBill, Calendar.DAY_OF_MONTH, 1);
			Integer li_count=Integer.parseInt(FormatDate.getDay(begDate));
			Date ldtTemp=null;
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		
			if(Integer.parseInt(FormatDate.getDay(begDate))!=li_count){
				do {
					if(month==12){						
						ldtTemp=sdf.parse(FormatDate.getYearFourDigit(nextBill)+FormatDate.getMonth(begDate)+li_count);
					}else{
						ldtTemp=sdf.parse(FormatDate.getYearFourDigit(nextBill)+FormatDate.getMonth(nextBill)+li_count);
					}
					li_count--;
					if(li_count<1)li_count=31;
				} while (!(sdf.format(ldtTemp)).toString().equals("19000101"));
				nextBill=ldtTemp;
			}
			ldtTemp=FormatDate.add(begDate, Calendar.MONTH, month);
			
			if(Integer.parseInt(FormatDate.getMonth(ldtTemp))==2|Integer.parseInt(FormatDate.getDay(ldtTemp))==29){
				ldtTemp=FormatDate.add(ldtTemp, Calendar.DAY_OF_MONTH, -1);
			}
			
			if(nextBill.compareTo(ldtTemp)>=0)nextBill=null;
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e);
		}
		return nextBill;
	}

}
