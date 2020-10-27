package com.ekalife.utils.scheduler;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.WordUtils;

import com.ekalife.utils.EmailPool;
import com.ekalife.utils.parent.ParentScheduler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Scheduler Email Notifikasi Dashboard New Business
 * 
 * @author : Daru
 * @since : Jul 30, 2015
 */
public class DashboardNotifScheduler extends ParentScheduler {
	protected final Log logger = LogFactory.getLog( getClass() );

	public void main() throws Exception {
		if(jdbcName.equals("eka8i") && 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				) {
//			logger.info("* * * SCHEDULER NOTIFIKASI DASHBOARD * * *");
			bacManager.schedulerNotifDashboard();
//			logger.info("* * * END SCHEDULER NOTIFIKASI DASHBOARD * * *");
		}
	}
	
}
