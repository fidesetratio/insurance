package com.ekalife.utils.scheduler;

import java.net.InetAddress;
import java.util.Date;

import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.parent.ParentScheduler;

/**
 * @spring.bean
 * 
 * @author Iga
 * @since 
 */

public class EmailPendingPrintSIAP2UScheduler extends ParentScheduler{
	//main method
	public void main() throws Exception{
	
		if(jdbcName.equals("eka8i") && 
				(	InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("IGALAPTOP") || 
					InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
					InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				)
			{
			/**
			 * Scheduler untuk mengirim report  Pending Print SIAP2U
			 * @author Iga
			 */
			try{
				bacManager.schedulerPendingPrintSIAP2U();
			}catch(Exception e){
				String pesan = "Scheduler Permintaan Report Pending Print SIAP2U tidak berjalan";
				EmailPool.send("E-Lions", 1, 0, 0, 0, null, 0, 0, new Date(), null, true, 
						props.getProperty("admin.ajsjava"), 
						new String[] { "iga.ukiarwan@sinarmasmsiglife.co.id" }, 
						null, 
						null, 
						"ERROR SCHEDULER REPORT PENDING PRINT SIAP2U", 
						InetAddress.getLocalHost().getHostName().toString().trim() + pesan + Common.getRootCause(e).getMessage(),
						null, null);
			}
		}
	}
}
