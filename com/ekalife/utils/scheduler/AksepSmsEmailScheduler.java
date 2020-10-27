	package com.ekalife.utils.scheduler;


	import java.net.InetAddress;
	import java.util.Date;
	
	import com.ekalife.utils.Common;
	import com.ekalife.utils.EmailPool;
	import com.ekalife.utils.parent.ParentScheduler;

	/**
	 * @spring.bean Memberitahukan status akseptasi melalui SMS dan Email
	 * 
	 * @author Anta
	 * @since 
	 */

	public class AksepSmsEmailScheduler extends ParentScheduler{
		//main method
		public void main() throws Exception{
		
			if(jdbcName.equals("eka8i") && 
				(	InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
					InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
					InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				)
			{
				try{
					uwManager.schedulerAksepSmsEmail();
				}catch(Exception e){
					String pesan = "Scheduler Pengiriman SMS dan EMAIL Pemberitahuan Akseptasi tidak berjalan";
					EmailPool.send("SMilE E-Lions", 1, 1, 0, 0, null, 0, 0, new Date(), null, true,
							"ajsjava@sinarmasmsiglife.co.id", 
							new String[]{"antasari@sinarmasmsiglife.co.id"},
							null,
							null, 
							"ERROR SCHEDULER AKSEP SMS EMAIL",  pesan + Common.getRootCause(e).getMessage(), null, null);
				
				}
			}
		}
	}
