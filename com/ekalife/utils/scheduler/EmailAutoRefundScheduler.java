package com.ekalife.utils.scheduler;

import java.net.InetAddress;
import java.util.Date;

import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.parent.ParentScheduler;

/**
 * @spring.bean
 * 
 * @author MAnta
 * @since 
 */

public class EmailAutoRefundScheduler extends ParentScheduler{
	//main method
	public void main() throws Exception{
	
		if(jdbcName.equals("eka8i") && 
			(	InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
				InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
				InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
			)
		{
			
			/**
			 * Scheduler permintaan refund premi (H+1)
			 * @author MANTA (17/03/2014)
			 */
			try{
				bacManager.schedulerRefundPremiAuto();
			}catch(Exception e){
				String pesan = "Scheduler Permintaan Refund Premi Auto tidak berjalan";
				EmailPool.send("SMilE E-Lions", 1, 1, 0, 0, 
						null, 0, 0, new Date(), null, 
						true, "ajsjava@sinarmasmsiglife.co.id", 
					new String[]{"antasari@sinarmasmsiglife.co.id;deddy@sinarmasmsiglife.co.id"}, null, null, 
					"ERROR SCHEDULER REFUND PREMI AUTO",  InetAddress.getLocalHost().getHostName().toString().trim() + pesan + Common.getRootCause(e).getMessage(), null, null);
			
			}
		}
	}
}
