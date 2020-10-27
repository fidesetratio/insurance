	package com.ekalife.utils.scheduler;


	import java.net.InetAddress;
	import java.util.Date;
	
	import com.ekalife.utils.Common;
	import com.ekalife.utils.EmailPool;
	import com.ekalife.utils.parent.ParentScheduler;

	/**
	 * @spring.bean Email otomatis untuk Follow Up Status Polis yang masih Further Requirement
	 * 
	 * @author MAnta
	 * @since 27/06/2014
	 */

	public class FollowUpFRScheduler extends ParentScheduler{
		//main method
		public void main() throws Exception{
		
			if(jdbcName.equals("eka8i") && 
				(	InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
					InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
					InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				)
			{
				try{
					bacManager.schedulerFollowUpFR();
				}catch(Exception e){
					String pesan = "Scheduler Pengiriman Follow Up SPAJ Further Requirement tidak berjalan";
					EmailPool.send("SMilE E-Lions", 1, 1, 0, 0, 
							null, 0, 0, new Date(), null, 
							true, "ajsjava@sinarmasmsiglife.co.id", 
						new String[]{"antasari@sinarmasmsiglife.co.id;deddy@sinarmasmsiglife.co.id"}, null, null, 
						"ERROR SCHEDULER FOLLOW UP SPAJ FR (E-LIONS)",  pesan + Common.getRootCause(e).getMessage(), null, null);
				
				}
			}
		}
	}
