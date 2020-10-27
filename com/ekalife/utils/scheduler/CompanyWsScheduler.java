package com.ekalife.utils.scheduler;

import java.net.InetAddress;

import com.ekalife.utils.parent.ParentScheduler;

/**
 * @spring.bean Class ini untuk scheduling e-mail sender otomatis setiap bulan (00.30)
 * 
 * @author Fadly
 * @since Dec 10, 2010 (2:39:19 PM)
 */
public class CompanyWsScheduler extends ParentScheduler{
 
	//main method
	public void main() throws Exception{
		if(jdbcName.equals("eka8i")) {
			//Scheduler summary tagihan renewal payroll ke admin ws
			uwManager.schedulerSummaryCompanyWs();
		}
	}
}