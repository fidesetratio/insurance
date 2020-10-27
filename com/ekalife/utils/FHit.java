package com.ekalife.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ekalife.elions.dao.FinanceDao;
import com.ekalife.utils.parent.ParentDao;

public class FHit extends ParentDao {
	
	private static SimpleDateFormat mm=new SimpleDateFormat("MM");
	private static SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	private static FinanceDao financeDao;
	
	public static Integer getTahunKe(Date adtNextBill,Date adtBegDate, Integer aiPperiod ){
		int i;
		Date adTemp;
		for(i=1;i<=aiPperiod.intValue();i++){
			adTemp=FormatDate.add(adtBegDate,Calendar.MONTH,i*12);
			//logger.info(""+sdf.format(adTemp));
			//ad_temp = RelativeDate(Date(f_add_months(adt_beg_date, i * 12)), 1)
			if(mm.format(adTemp).equals("2")){
				adTemp=FCheck.getNextBilll(adTemp,adtBegDate,1);
			}
			if(adtNextBill.compareTo(adTemp)< 0)
				break;
		}

		return new Integer(i);
	}
	
	public static Integer getPremiKe(Date adtNextBill,Date adtBegDate, Integer aiPmode){
		Integer liPremiKe;
		if(aiPmode.intValue() != 1 ){
			double tgl=FormatDate.dateDifference(adtBegDate,adtNextBill,false);
			double temp=Math.floor(tgl/30);
			liPremiKe=new Integer((int)Math.round(temp/aiPmode.intValue()));
			//i_premi_ke = Round ( Truncate( DaysAfter(Date(adt_beg_date), Date(adt_next_bill)) / 30, 0) / ai_pmode, 0 )
		}else{
			liPremiKe=financeDao.selectMonthsBetWeen(adtNextBill,adtBegDate);
		}
		return liPremiKe;

	}

	public void setFinanceDao(FinanceDao financeDao) {
		this.financeDao = financeDao;
	}
	
	
}
