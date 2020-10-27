package com.ekalife.utils.scheduler;

import java.net.InetAddress;
import com.ekalife.utils.parent.ParentScheduler;

/**
 * Scheduler untuk mengirimkan polis2 SIMPOL yang sudah expired (90 hari(dari tanggal spaj) tidak diproses & di posisi further)
 * @author ryan
 * @since 
 */

public class NeonBox extends ParentScheduler{
	//main method
	public void main() throws Exception{
	
		if(jdbcName.equals("eka8i") && 
			(	InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
				InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
				InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
			)
		{
			bacManager.schedulerNeonBox();
		
		}
	}
}
