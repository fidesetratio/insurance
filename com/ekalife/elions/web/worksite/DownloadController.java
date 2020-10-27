package com.ekalife.elions.web.worksite;

/**
 * User: samuel
 * Date: May 19, 2010
 * Time: 2:25:01 PM
 */

import com.ekalife.utils.FileUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DownloadController implements Controller
{
	protected final Log logger = LogFactory.getLog( getClass() );
    public ModelAndView handleRequest( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "DownloadController.download" );
        String fileName = request.getParameter("fileName");
        if( fileName != null ) fileName = fileName.toLowerCase();
        if("".equals(fileName))fileName=null;
    	if(fileName != null){
            String dirName = "\\\\ebserver\\pdfind\\Report\\undertable\\" + fileName.substring( fileName.length() - 6 - 4, fileName.length() - 4 );
            FileUtils.downloadFile("attachment;", dirName, fileName, response);
            return null;
        }
        else
        {
            return new ModelAndView( "worksite/download" );
        }
    }

}
