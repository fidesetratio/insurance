package com.ekalife.utils.scheduler;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ekalife.elions.dao.SchedulerDao;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentScheduler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CetakPolisMaScheduler extends ParentScheduler {
	protected final Log logger = LogFactory.getLog( getClass() );
	//main method
	public void main() throws Exception {
		if(jdbcName.equals("eka8i") && 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				) {
		logger.info("Cetak Polis Mall Scheduller. Host : "+InetAddress.getLocalHost().getHostName());
		//if(jdbcName.equals("eka8i")) {
		uwManager.schedulercetakPolisMa();
		}
	}

}
