package com.ekalife.utils.scheduler;

import java.net.InetAddress;
import com.ekalife.utils.parent.ParentScheduler;

/**
 * @spring.bean Class ini untuk memberitahukan Summary Akseptasi Bancass Syariah perbulan
 * 
 * @author Canpri
 * @since 
 */

public class SummaryAkseptasiBancassSyariahScheduler extends ParentScheduler{
	//main method
	public void main() throws Exception{
		if(jdbcName.equals("eka8i") && 
			(	InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
				InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
				InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
			)
		{
			uwManager.schedulerSummaryAkseptasiBancassSyariah();
		}
	}
}
