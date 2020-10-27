package com.ekalife.utils.scheduler;

import java.net.InetAddress;
import java.util.Date;

import com.ekalife.elions.process.Sequence;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.parent.ParentScheduler;

public class PendingSmilePrioritas extends ParentScheduler {

	/**
	 * @param args
	 * @author lufi
	 * @since 9 Jan 2014
	 */
	
	protected Sequence sequence;
	//main method
	public void main() throws Exception {
		if(jdbcName.equals("eka8i") && 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				) {
					
					String err="";
			
					try{
						bacManager.schedulerPendingSmilePrioritas();
					}catch(Exception e){
						err=e.getLocalizedMessage();
						EmailPool.send(bacManager.selectSeqEmailId(),"SMiLe E-Lions", 1, 1, 0, 0, 
								null, 0, 0, elionsManager.selectSysdateSimple(), null, true, "ajsjava@sinarmasmsiglife.co.id",												
								new String[]{"ryan@sinarmasmsiglife.co.id"}, 
								null, 
								null, 
								"Error Scheduler Pending Smile Prioritas", 
								"Scheduler Pending Smile Prioritas Gak jalan Tolong dicek", 
								null,15);
						logger.error("ERROR :", e);
					}
			
		}

	}

}
