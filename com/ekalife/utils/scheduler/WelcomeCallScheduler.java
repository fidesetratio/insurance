package com.ekalife.utils.scheduler;

import java.net.InetAddress;

import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentScheduler;

public class WelcomeCallScheduler extends ParentScheduler {
	public void main() throws Exception{		
		if(jdbcName.equals("eka8i") && 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1")
				)) {
			try{
				bacManager.schedulerWelcomeCall();						
			}catch(Exception e){
				String pesan = "SCHEDULER PROSES WELCOME CALL TIDAK BERHASIL";
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
					new String[]{"itweb@sinarmasmsiglife.co.id"}, null, null, 
					"ERROR SCHEDULER WELCOME CALL",  pesan + Common.getRootCause(e).getMessage(), null);						
			}
		}
	}
}
