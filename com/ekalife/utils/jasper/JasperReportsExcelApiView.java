package com.ekalife.utils.jasper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporter;

/**
 * Class ini berfungsi sebagai View yang di-customize untuk melihat report2 jasperreports dalam bentuk excel api
 * karena jasperreports versi ini export excel dalam bentuk POI banyak bug nya, jadi harus pake exporter excel yg satu lagi
 * 
 * @author Yusuf Sutarko
 * @since 4 Aug 2010
 */
public class JasperReportsExcelApiView extends AbstractJasperReportsSingleFormatView {

	public JasperReportsExcelApiView() {
		if(logger.isDebugEnabled())logger.debug("Instantiating JasperReportsExcelApiView");
		setContentType("application/vnd.ms-excel"); 
	}

	@Override
	protected JRExporter createExporter() {
		return new JExcelApiExporter();
	}

	@Override
	protected boolean useWriter() {
		return false;
	}
	
}