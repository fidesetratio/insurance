package com.ekalife.utils.scheduler;

import java.net.InetAddress;

import com.ekalife.utils.parent.ParentScheduler;

/**
 * @spring.bean Class ini untuk scheduling Report Outstanding BSM (Request by Yusuf)
 * 1 sept 2012 - efektif hanya dikirimkan ke 3 cabang BSM 
 * S36 = MAGELANG, S159 = MAGELANG METRO SQUARE, S198 = TEMANGGUNG
 * 
 * @author Deddy
 * @since Sept 1, 2012 (2:39:19 PM)
 */
public class OutstandingBSMScheduler extends ParentScheduler{
 
	//main method
	public void main() throws Exception{
		if(jdbcName.equals("eka8i") 
				&& 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				) {
			//Scheduler Outstanding BSM untuk simas prima, simas stabil link dan simas power link
			uwManager.schedulerOutstandingBSM();
			
		}
	}
}