package com.ekalife.elions.web.common;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import com.ekalife.utils.parent.ParentController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UploadsaveController extends ParentController {
	protected final Log logger = LogFactory.getLog( getClass() );

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> cmd = new HashMap<String, Object>();
		
		File file = new File("C:\\MyFile.txt");
		    FileInputStream fis = null;
		    BufferedInputStream bis = null;
		    DataInputStream dis = null;

		    try {
		      fis = new FileInputStream(file);

		      // Here BufferedInputStream is added for fast reading.
		      bis = new BufferedInputStream(fis);
		      dis = new DataInputStream(bis);

		      // dis.available() returns 0 if the file does not have more lines.
		      while (dis.available() != 0) {

		      // this statement reads the line from the file and print it to
		        // the console.
		        logger.info(dis.readLine());
		      }

		    } catch (FileNotFoundException e) {
		      logger.error("ERROR :", e);
		    } catch (IOException e) {
		      logger.error("ERROR :", e);
		    }finally {
		    	try {
		    		// dispose all the resources after using them.
		    		if(fis != null) {
		    			fis.close();
		    		}
		    		if(bis != null) {
		    			bis.close();
		    		}
		    		if(dis != null) {
		    			dis.close();
		    		}
		    	}catch (Exception e) {
					logger.error("ERROR :", e);
				}
		    }

		return new ModelAndView("common/uploadsave", cmd);
	}

}