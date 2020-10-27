package com.ekalife.utils.scheduler;

import java.net.InetAddress;

import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentScheduler;

/**
 * Email Scheduler List SPAJ Posisi Waiting Proses NB dan SPAJ Gagal Proses
 * 
 * @author : Randy
 * @since : Mar 21, 2016
 */
public class WaitingProses extends ParentScheduler {

	//main method
	public void main() throws Exception {
		if(jdbcName.equals("eka8i") && 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				) {
			//Scheduler WAITING PROSES NB DAN GAGAL PROSES
			try{
				bacManager.schedulerWaitingProses();
			}catch(Exception e){
				String pesan = "AUTOMAIL REPORT SPAJ WAITING PROSES tidak jalan";
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
					new String[]{"randy@sinarmasmsiglife.co.id"}, new String[]{"ryan@sinarmasmsiglife.co.id"}, null, 
					"ERROR SCHEDULER SPAJ WAITING PROSES",  pesan + Common.getRootCause(e).getMessage(), null);
			}
		}
	}
	
}
