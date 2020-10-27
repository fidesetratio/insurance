package com.ekalife.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FCheck {
	private static SimpleDateFormat dd=new SimpleDateFormat("dd");
	private static SimpleDateFormat mm=new SimpleDateFormat("MM");
	private static SimpleDateFormat yyyy=new SimpleDateFormat("yyyy");
	//private static SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	
	public static Date getEndBill(Date adtNext,Date adtBegDate,int aiMonth){
		Date ldtNext,ldtTmp,ld;
		int liCount;
		//logger.info(sdf.format(adtNext));
		//logger.info(sdf.format(adtBegDate));
		ldtNext=FormatDate.add(adtNext,Calendar.DATE,1);
		//logger.info(sdf.format(ldtNext));
		ld = ldtNext;
		liCount= Integer.parseInt(dd.format(adtBegDate));
		Calendar calAwal=new GregorianCalendar(1900,00,01);
		
		if(Integer.parseInt(dd.format(ldtNext)) != liCount){
			do{
				if(aiMonth== 12){
					Calendar calTmp=new GregorianCalendar(Integer.parseInt(yyyy.format(ld)),
							Integer.parseInt(mm.format(adtBegDate))-1,liCount);
					ldtTmp=calTmp.getTime();
					//logger.info(sdf.format(ldtTmp));
					//ldtTmp = Datetime(Date( String(ld, 'yyyy') + '-' + String(adt_beg_date, 'mm') + '-' + string(li_count)))
				}else{
					Calendar calTmp=new GregorianCalendar(Integer.parseInt(yyyy.format(ld)),
							Integer.parseInt(mm.format(adtNext))-1,liCount);
					ldtTmp=calTmp.getTime();
					//logger.info(sdf.format(ldtTmp));
					//ldt_tmp = Datetime(Date( String(ld, 'yyyy') + '-' + String(adt_next, 'mm') + '-' + string(li_count)))
				}
				liCount--;
				if(liCount< 1 )
					liCount= 31;
			}while(ldtTmp.compareTo(calAwal.getTime())!=0);//Until String(ldt_tmp, 'yyyymmdd') <> '19000101'
			ldtNext= ldtTmp;
			//logger.info(sdf.format(ldtNext));
		}
		ldtTmp= FormatDate.add(adtBegDate,Calendar.MONTH,aiMonth);
		//logger.info(""+sdf.format(ldtTmp));
		//logger.info(""+sdf.format(ldtNext));
		if(ldtNext.compareTo(ldtTmp)==0 || ldtNext.compareTo(ldtTmp)> 0){
			ldtNext=null;
		}else{
			/*
			//Himmia 30/01/2001
			If Integer(string(ldt_next, 'dd')) > Integer(string(adt_beg_date, 'dd')) Then
				ldt_next = Datetime(Relativedate(date(ldt_next), -1))
			End if */
		}
		
		return ldtNext;
	}
	
	public static Date getEndAktif(Date adtEndAktif,Date adtBegDate){
		int liBdate, liEaktif;
		
		if(Integer.parseInt(dd.format(adtBegDate))<28)
			return adtEndAktif;
		
		if (adtEndAktif==null) 
			return adtEndAktif;

		do{
			liBdate = Integer.parseInt(dd.format(adtBegDate));
			liEaktif = Integer.parseInt(dd.format(adtEndAktif));
			if(liEaktif>= liBdate){
				adtEndAktif=FormatDate.add(adtEndAktif,Calendar.DATE,-1); 
			}
		}while (liBdate < liEaktif);

		return adtEndAktif;
	}
	
	public static Date getNextBilll(Date adtNext,Date adtBegDate,int aiMonth){
		Date ldtNext,ldtTmp;
		Date ld;
		int liCount;

		ldtNext= FormatDate.add(adtNext,Calendar.DATE,1) ;
		ld = ldtNext;
		liCount= Integer.parseInt(dd.format(adtBegDate));
		Calendar calAwal=new GregorianCalendar(1900,00,01);
		
		if(Integer.parseInt(dd.format(ldtNext)) != liCount){
			do{
				if(aiMonth== 12){
					Calendar calTmp=new GregorianCalendar(Integer.parseInt(yyyy.format(ld)),
							Integer.parseInt(mm.format(adtBegDate))-1,liCount);
					ldtTmp=calTmp.getTime();
					//ldtTmp = Datetime(Date( String(ld, 'yyyy') + '-' + String(adt_beg_date, 'mm') + '-' + string(li_count)))
				}else{
					Calendar calTmp;
					if(aiMonth==1 && Integer.parseInt(yyyy.format(ld))>Integer.parseInt(yyyy.format(adtNext))){
						calTmp=new GregorianCalendar(Integer.parseInt(yyyy.format(adtNext)),
								Integer.parseInt(mm.format(adtNext))-1,liCount);
					}else{
						calTmp=new GregorianCalendar(Integer.parseInt(yyyy.format(ld)),
								Integer.parseInt(mm.format(adtNext))-1,liCount);
					}
					
					ldtTmp=calTmp.getTime();
					//ldt_tmp = Datetime(Date( String(ld, 'yyyy') + '-' + String(adt_next, 'mm') + '-' + string(li_count)))
				}
				liCount--;
				if(liCount< 1)
					liCount= 31;
				
			}while(ldtTmp.compareTo(calAwal.getTime())!=0);//Until String(ldt_tmp, 'yyyymmdd') <> '19000101'
			ldtNext=ldtTmp;
		}
		return ldtNext;
	}
	
}
