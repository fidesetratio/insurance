package com.ekalife.utils.iso8583;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ManageConnection implements Runnable{
	
	protected final Log logger = LogFactory.getLog( getClass() );
	
	private Iso8583Client client;
	private Thread mct;
	
	public ManageConnection(Iso8583Client isoClient) {
		client = isoClient;
		mct = new Thread(this);
		mct.start();
	}
	
	public void run() {
		while(mct != null) {
			try {
				mct.sleep(300000);
				int check = 0;
				client.setEchoTestSuccess(false);
				while(!client.getEchoTestSuccess() || check < 3) {
					client.sendMssgToServer(client.isoMssgEchoTest());
					check++;
					mct.sleep(30000);
				}
				if(!client.getEchoTestSuccess() && check == 3) {
					client.reconnectServer();
					threadStop();
				}
				
			} catch (InterruptedException e) {
				logger.error("ERROR :", e);
			}			
		}
	}
	
	private void threadStop() {
		mct.stop();
		mct.destroy();
	}
	
}
