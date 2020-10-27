package com.ekalife.utils.scheduler;

import java.net.InetAddress;

import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentScheduler;

/**
 * @spring.bean Class ini untuk scheduling e-mail sender otomatis setiap bulan (00.30)
 * 
 * @author Andy
 * @since Dec 10, 2010 (2:39:19 PM)
 */
public class DanamasPrimaScheduler extends ParentScheduler{
 
	//main method
	public void main() throws Exception{
		if(jdbcName.equals("eka8i") && 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				) {
			//Scheduler Penerimaan & Klaim Danamas Prima
			try{
				uwManager.schedulerPenerimaanDanKlaimDanamasPrima();
			}catch(Exception e){
				String pesan = "Scheduler Penerimaan Dan Klaim Danamas Prima tidak berjalan.\nTolong CEK ULANG Schedulernya!";
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
						new String[]{"deddy@sinarmasmsiglife.co.id"}, null, null, 
						"ERROR SCHEDULER PENERIMAAN DAN KLAIM DANAMAS PRIMA",  pesan + Common.getRootCause(e).getMessage(), null);
				
			}
			try{
				uwManager.schedulerOutstandingBSM();
			}catch(Exception e){
				String pesan = "Scheduler Pengiriman Outstanding BSM tidak berjalan.\nTolong CEK ULANG Schedulernya!";
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
						new String[]{"deddy@sinarmasmsiglife.co.id"}, null, null, 
						"ERROR SCHEDULER OUTSTANDING BSM",  pesan + Common.getRootCause(e).getMessage(), null);
				
			}
			
		}
	}
}