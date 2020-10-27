package com.ekalife.utils.jasper;

import java.io.ObjectOutputStream;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Class ini berfungsi sebagai View yang di-customize untuk melihat report2 jasperreports
 * dalam bentuk appletviewer. Pada dasarnya isi report di-stream ke servletoutput, 
 * dan selanjutnya stream tersebut diterjemahkan oleh applet2 jasperreports yang tersedia di folder include/applets (client side)
 * @author Yusuf Sutarko
 * @since Feb 1, 2007 (11:19:53 AM)
 */
public class JasperReportsAppletView extends 
		AbstractJasperReportsSingleFormatView {

	public JasperReportsAppletView() {
		if(logger.isDebugEnabled())logger.debug("Instantiating JasperReportsAppletView");
		setContentType("application/octet-stream"); 
	}
	
	protected void renderReport(JasperPrint jasperPrint, Map parameters, HttpServletResponse response) throws Exception {

		if(logger.isDebugEnabled())logger.debug("Rendering JasperReportsAppletView");
		ServletOutputStream ouputStream = null;
		ObjectOutputStream oos = null;
		try {
			ouputStream = response.getOutputStream();
			
			oos = new ObjectOutputStream(ouputStream);
			oos.writeObject(jasperPrint);
			
		}finally {
			try {
				if(oos != null) {
					oos.flush();
					oos.close();
				}
				if(ouputStream != null) {
					ouputStream.flush();
					ouputStream.close();
				}
			}catch (Exception e) {
				logger.error("ERROR :", e);
			}
		}
	}

	protected JRExporter createExporter() {
		return null;
	}

	protected boolean useWriter() {
		return false;
	}

}