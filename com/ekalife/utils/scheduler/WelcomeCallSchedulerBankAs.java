package com.ekalife.utils.scheduler;

import java.net.InetAddress;

import com.ekalife.elions.process.Sequence;
import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentScheduler;

public class WelcomeCallSchedulerBankAs extends ParentScheduler {
	

	protected Sequence sequence;
	
	
	
	public void main() throws Exception{
		
		if(jdbcName.equals("eka8i") && 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				
				) {
						try{
							bacManager.schedulerWelcomeCallBankAs();
							System.out.println("jalan donk oii");
						}catch(Exception e){
							String pesan = "SCHEDULER PROSES WELCOME CALL BANK AS TIDAK BERHASIL";
							email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
								new String[]{"itweb@sinarmasmsiglife.co.id"}, null, null, 
								"ERROR SCHEDULER WELCOME CALL",  pesan + Common.getRootCause(e).getMessage(), null);
						
						}
					}
	}

}
