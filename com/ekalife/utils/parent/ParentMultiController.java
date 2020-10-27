package com.ekalife.utils.parent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.ekalife.elions.service.AjaxManager;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.Products;

import id.co.sinarmaslife.std.spring.util.Email;

/**
 * <p>Merupakan controller yang merupakan parent dari semua controller yang 
 * mempunyai fungsi kelebihan yaitu satu buah controller dapat meng-handle banyak
 * fungsi, contoh penggunaannya seperti ini:</p>
 * <ul>
 * 	<li>Satu buah controller untuk meng-handle semua halaman search, misalnya
 * search spaj, search nomor proposal, search data karyawan</li>
 * </ul>
 * @author Yusuf
 * @since 07/08/2006
 * @see abstract parent beans pada applicationContext.xml
 */
public abstract class ParentMultiController extends MultiActionController {

	//Global Properties
	protected ElionsManager elionsManager;
	protected UwManager uwManager;
	protected BacManager bacManager;
	protected AjaxManager ajaxManager;
	protected Properties props;
	protected Products products;
	protected Email email;
	
	//Custom Editors
	protected CustomNumberEditor doubleEditor;
	protected CustomNumberEditor integerEditor;
	protected CustomDateEditor dateEditor;
	protected CustomDateEditor completeDateEditor;
	
	//Formatter
	protected DateFormat defaultDateFormat;
	protected DateFormat defaultDateFormatStripes;
	protected DateFormat defaultDateFormatReversed;
	protected DateFormat completeDateFormat;
	
	/*
	protected DataSource reportDataSource;
	
	public DataSource getDataSource()
    {
        return this.elionsManager.getUwDao().getDataSource();
    }
	
	public DataSource getReportDataSource() {
		return this.reportDataSource;
	}

	public void setReportDataSource(DataSource reportDataSource) {
		this.reportDataSource = reportDataSource;
	}

	protected Connection getReportConnection() {
		Connection conn = null;
		try {
			conn =  this.getReportDataSource().getConnection();
		} catch (SQLException e) {
			logger.error("ERROR", e);
		}
		return conn;
	}
	
	protected Connection getConnection() {
		Connection conn = null;
		try {
			conn = this.elionsManager.getUwDao().getDataSource().getConnection();
		} catch (SQLException e) {
			logger.error("ERROR :", e);
		}
		return conn;
	}
	*/
	
	protected void closeConnection(Connection conn){
		try {
            conn.close();
        } catch (SQLException e) { /* ignored */}
	}

	protected ModelAndView noReportData(HttpServletResponse response, String message) throws IOException{
		ServletOutputStream out = response.getOutputStream();
		message = (message==null) ? "Halaman tidak ada. Harap cek kembali data yang bersangkutan.":message;
		out.println("<script>alert('"+message+"');</script>");
		out.flush();
		return null;
	}
	
	protected BindingResult bindAndValidate(HttpServletRequest request, Object command, boolean isValidated) throws Exception {
		logger.debug("Binding request parameters onto MultiActionController command and Validating the results");
		ServletRequestDataBinder binder = createBinder(request, command);
		binder.bind(request);
		if(isValidated) {
			Validator[] validators = getValidators();
			if (validators != null) {
				for (int i = 0; i < validators.length; i++) {
					if (validators[i].supports(command.getClass())) {
						ValidationUtils.invokeValidator(validators[i], command, binder.getBindingResult());
					}
				}
			}
		}
		//binder.closeNoCatch();
		return binder.getBindingResult();
	}
	
	public void setCompleteDateEditor(CustomDateEditor completeDateEditor) {
		this.completeDateEditor = completeDateEditor;
	}

	public DateFormat getCompleteDateFormat() {
		return completeDateFormat;
	}

	public void setCompleteDateFormat(DateFormat completeDateFormat) {
		this.completeDateFormat = completeDateFormat;
	}

	public AjaxManager getAjaxManager() {
		return ajaxManager;
	}

	public void setAjaxManager(AjaxManager ajaxManager) {
		this.ajaxManager = ajaxManager;
	}

	public DateFormat getDefaultDateFormatStripes() {
		return defaultDateFormatStripes;
	}

	public void setDefaultDateFormatStripes(DateFormat defaultDateFormatStripes) {
		this.defaultDateFormatStripes = defaultDateFormatStripes;
	}

	public DateFormat getDefaultDateFormat() {
		return defaultDateFormat;
	}

	public void setDefaultDateFormat(DateFormat defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
	}

	public DateFormat getDefaultDateFormatReversed() {
		return defaultDateFormatReversed;
	}

	public void setDefaultDateFormatReversed(DateFormat defaultDateFormatReversed) {
		this.defaultDateFormatReversed = defaultDateFormatReversed;
	}

	public Log getLogger() {
		return logger;
	}

	public CustomDateEditor getDateEditor() {
		return dateEditor;
	}

	public void setDateEditor(CustomDateEditor dateEditor) {
		this.dateEditor = dateEditor;
	}

	public CustomNumberEditor getDoubleEditor() {
		return doubleEditor;
	}

	public void setDoubleEditor(CustomNumberEditor doubleEditor) {
		this.doubleEditor = doubleEditor;
	}

	public CustomNumberEditor getIntegerEditor() {
		return integerEditor;
	}

	public void setIntegerEditor(CustomNumberEditor integerEditor) {
		this.integerEditor = integerEditor;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public Properties getProps() {
		return props;
	}

	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public ElionsManager getElionsManager() {
		return elionsManager;
	}

	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}

	public UwManager getUwManager() {
		return uwManager;
	}

	public void setUwManager(UwManager uwManager) {
		this.uwManager = uwManager;
	}
	
	public BacManager getBacManager() {
		return bacManager;
	}

	public void setBacManager(BacManager bacManager) {
		this.bacManager = bacManager;
	}

}