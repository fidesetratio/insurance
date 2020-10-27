package com.ekalife.utils.scheduler;

import java.net.InetAddress;
import com.ekalife.utils.parent.ParentScheduler;

/**
 * @spring.bean Class ini untuk memberitahukan Lisensi Agent yang akan Expired setiap tgl 1 jam 3
 * 
 * @author Canpri
 * @since 21 Feb 2014
 */

public class ExpiredLisensiAgentScheduler extends ParentScheduler{
	//main method
	public void main() throws Exception{
		if(jdbcName.equals("eka8i") && 
			(	InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
				InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
				InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
			)
		{
			bacManager.schedulerExpiredLisensiAgent();
		}
	}
}
