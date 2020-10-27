package com.ekalife.utils.jasper;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView;

import net.sf.jasperreports.engine.JRExporter;

/**
 * Class pembantu saja, untuk view report jasperreports dalam bentuk PDF
 * 
 * @author Yusuf Sutarko
 */
public class JasperReportsCustomView extends
		AbstractJasperReportsSingleFormatView implements Serializable {
	
	private Map expParams;
	private String spaj;
	private String fileName;
	private Properties props;
	private String cabang;
	private boolean isSilentPrint;
	private HttpServletResponse response;
	private String va;
	private boolean isViewer;
	
	public void setViewer(boolean isViewer) {
		this.isViewer = isViewer;
	}

	public void setVa(String va) {
		this.va = va;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setCabang(String cabang) {
		this.cabang = cabang;
	}

	public void setSilentPrint(boolean isSilentPrint) {
		this.isSilentPrint = isSilentPrint;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public void setExpParams(Map expParams) {
		this.expParams = expParams;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setSpaj(String spaj) {
		this.spaj = spaj;
	}
	
	public JasperReportsCustomView(HttpServletResponse response, Properties props, Map expParams, 
			String spaj, String cabang, String fileName, boolean isSilentPrint, String va,
			boolean isViewer) {
		if(logger.isDebugEnabled())logger.debug("Instantiating JasperReportsCustomView");
		this.response = response;
		this.props = props;
		this.expParams = expParams;
		this.spaj = spaj;
		this.cabang = cabang;
		this.fileName = fileName;
		this.va = va;
		this.isViewer = isViewer;
		
		setContentType("application/pdf"); 
	}
	
	protected JRExporter createExporter() {
		return new PdfFileExporter(response, props, expParams, spaj, cabang, fileName, isSilentPrint, va, isViewer); 
	}

	protected boolean useWriter() {
		return false;
	}

} 