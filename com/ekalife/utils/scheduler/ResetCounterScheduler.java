package com.ekalife.utils.scheduler;

import java.net.InetAddress;

import com.ekalife.utils.parent.ParentScheduler;

/**
 * @spring.bean Class ini untuk scheduling e-mail sender otomatis setiap bulan (00.30)
 * 
 * @author Andy
 * @since Dec 10, 2010 (2:39:19 PM)
 */
public class ResetCounterScheduler extends ParentScheduler{
 
	//main method
	public void main() throws Exception{
		if(jdbcName.equals("eka8i") && 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				) {
			uwManager.schedulerResetCounter();
		}
	}
}