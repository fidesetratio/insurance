package com.ekalife.utils;

import java.util.Date;

import com.ekalife.elions.service.UwManager;

public class F_hit_premi_ke {
	private static UwManager uwManager;

	/**
	 * @author Berto
	 * @since Jun 27, 2008 4:00:58 PM
	 * @param args 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static Integer hitPremiKe(Date nextBill, Date begDate, Integer pmode){
		Integer premi_ke=0;
		Integer li_sel=0;
		
		if(pmode!=1){
			premi_ke=(Integer.valueOf((int)FormatDate.dateDifference(begDate, nextBill, true))/30)/pmode;			
		}else{
			if(Integer.valueOf((int)FormatDate.dateDifference(begDate, nextBill, true))/30>0){
				premi_ke=(Integer.valueOf((int)FormatDate.dateDifference(begDate, nextBill, true))/30);
			}else{
				premi_ke=(Integer.valueOf((int)FormatDate.dateDifference(begDate, nextBill, true))/30)+1;
			}
		}
		
		return premi_ke;
	}

	public void setUwManager(UwManager UwManager) {
		this.uwManager = UwManager;
	}

}
