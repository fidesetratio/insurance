package com.ekalife.utils.iso8583;

import java.util.ArrayList;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;

public class MessageHandler {
	private static ISOPackager packager = PackagerFactory.getPackager();  

	public String[] process(ISOMsg isomsg) throws Exception {  
		String[] message = new String[128];
	    for (int i=0;i<128;i++){  
	        if (isomsg.hasField(i)){
	        	message[i] = isomsg.getValue(i).toString();
	        }  
	    }  

	    return message;  
	}
	
	public ISOMsg unpackRequest(String message) throws ISOException, Exception {  
	    ISOMsg isoMsg = new ISOMsg();  
	    isoMsg.setPackager(packager);  
	    isoMsg.unpack(message.getBytes());  
	    //isoMsg.dump(System.out, " ");  
	    
	    return isoMsg ;  
	}
}
