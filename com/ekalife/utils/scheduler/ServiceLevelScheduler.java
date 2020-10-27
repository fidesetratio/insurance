package com.ekalife.utils.scheduler;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.ekalife.utils.parent.ParentScheduler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Scheduler Service Level
 * 
 * @author : Daru
 * @since : Aug 5, 2015
 */
public class ServiceLevelScheduler extends ParentScheduler {
	protected final Log logger = LogFactory.getLog( getClass() );
	/**
	 * Scheduler untuk menghitung lama proses polis terbit dalam jam
	 * (dari print polis sampai polis dikirimkan).
	 * 
	 * Hasil perhitungan di insert ke kolom LAMA_PROSES_TERBIT
	 * pada table eka.mst_trans_history, untuk kemudian digunakan pada
	 * Report SL Policy Issue (All)
	 * 
	 * @author Daru
	 * @throws UnknownHostException 
	 * @since Aug 5, 2015
	 */
	public void countPolicyIssueHours() throws UnknownHostException {
		if(jdbcName.equals("eka8i") && 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				) {
//			logger.info("* * * BEGIN SCHEDULER COUNT POLICY ISSUE HOURS * * *");
			bacManager.schedulerCountPolicyIssueHours();
//			logger.info("* * * END SCHEDULER COUNT POLICY ISSUE HOURS * * *");
		}
	}
	
}
