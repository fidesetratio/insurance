package com.ekalife.utils.iso8583;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.packager.GenericPackager;

public class PackagerFactory {
	
	protected static final Log logger = LogFactory.getLog( PackagerFactory.class );
	
	public static ISOPackager getPackager() {
		 ISOPackager packager = null;  
         try {  
             String filename = "iso87ascii.xml";  
             InputStream is = PackagerFactory.class.getResourceAsStream(filename);  
             packager = new GenericPackager(is);  
         }   
         catch (ISOException e) {  
             logger.error("ERROR :", e);  
         }  
         return packager;		
	}
}
