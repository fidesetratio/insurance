package com.ekalife.utils;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.Thread.State;
import java.util.Date;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import com.ekalife.utils.beans.Email;

import common.Logger;

public class SessionAttributeTracker implements HttpSessionAttributeListener {
	/**
	 *@author Deddy
	 *@since Aug 26, 2014
	 *@description TODO
	 */
	static Logger logger = Logger.getLogger(CommonExceptionResolver.class);
	private Email email;
	public void attributeRemoved(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void attributeReplaced(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	public void attributeAdded(HttpSessionBindingEvent sessionBindingEvent){
		Object obj = sessionBindingEvent.getValue();
		
		if(!isSerializable(obj)){
			String logAttr="Attribute : "+ sessionBindingEvent.getName()+ " "+
							"Source : "+ sessionBindingEvent.getSource()+" "+
							"Session : "+ sessionBindingEvent.getSession()+
					" added to session with Not Serializable Object: "+
					sessionBindingEvent.getValue();
			String logThread="Thread Stack for Not Serializable Object:\n"+ThreadDump();
//			logger.error(logAttr);
//			logger.error(logThread);
			//penambahan pooling
			try{
				EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, new Date(), null, true, 
						"ajsjava@sinarmasmsiglife.co.id",  new String[] { "andy@sinarmasmsiglife.co.id;deddy@sinarmasmsiglife.co.id;ridhaal@sinarmasmsiglife.co.id"}, null, null, 
						"ERROR pada E-Lions", logAttr+"\n\n"+logThread, null, null);
			}catch(Exception ex){
				ex.getLocalizedMessage();
			}
			
		}
	}
	
	private boolean isSerializable(Object obj){
		boolean ret = false;
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try{
			oos = new ObjectOutputStream(out);
			oos.writeObject(obj);
			ret = true;
		}catch (Exception e) {
			logger.info("Exception during serialization: " + e); 
			return ret;
		}finally{
			try{
				oos.close();
			}catch (Exception e) {}
		}
		return ret;
	}
	
	private String ThreadDump(){
		StringBuffer fullThreadDump = new StringBuffer();
		Thread t = Thread.currentThread();
		State state = t.getState();
		String tName = t.getName();
		if(state!=null){
			fullThreadDump
			.append("   ")
			.append(tName)
			.append(": ")
			.append(state)
			.append("\n");
		}
		
		StackTraceElement[] stes = t.getStackTrace();
		for(int i =0;i< stes.length;++i){
			StackTraceElement trace = stes[i];
			fullThreadDump
			.append("       at ")
			.append(trace)
			.append("\n");
		}
		return fullThreadDump.toString();
	}

}
