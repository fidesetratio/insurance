package com.ekalife.utils.parent;

import java.text.DateFormat;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;

import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.Products;

/**
 * <p>
 * Controller ini diextend oleh wizardcontroller2, dimana sebuah wizardcontroller merupakan
 * versi pengembangan dari formcontroller, namun bisa digunakan untuk form2 wizard, misalnya:
 * <ul>
 * 	<li>Form input yang mengandung beberapa step sebelum sampai halaman terakhir, seperti contohnya
 * 	halaman 1 data pemegang, halaman 2 data tertanggung, halaman 3 selesai</li>
 * </ul>
 * </p>
 * 
 * @author Yusuf Sutarko
 */
public abstract class ParentWizardController extends AbstractWizardFormController {

	//Global Properties
	protected ElionsManager elionsManager;
	protected UwManager uwManager;
	protected BacManager bacManager;
	protected Properties props;
	protected Products products;
	
	//Custom Editors
	protected CustomNumberEditor doubleEditor;
	protected CustomNumberEditor integerEditor;
	protected CustomDateEditor dateEditor;
	
	//Formatter
	protected DateFormat defaultDateFormat;
	protected FormatString formatString;

	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public CustomDateEditor getDateEditor() {
		return dateEditor;
	}

	public void setDateEditor(CustomDateEditor dateEditor) {
		this.dateEditor = dateEditor;
	}

	public DateFormat getDefaultDateFormat() {
		return defaultDateFormat;
	}

	public void setDefaultDateFormat(DateFormat defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
	}

	public CustomNumberEditor getDoubleEditor() {
		return doubleEditor;
	}

	public void setDoubleEditor(CustomNumberEditor doubleEditor) {
		this.doubleEditor = doubleEditor;
	}

	public ElionsManager getElionsManager() {
		return elionsManager;
	}

	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}

	public CustomNumberEditor getIntegerEditor() {
		return integerEditor;
	}

	public void setIntegerEditor(CustomNumberEditor integerEditor) {
		this.integerEditor = integerEditor;
	}

	public Log getLogger() {
		return logger;
	}

	public void setFormatString(FormatString formatString) {
		this.formatString = formatString;
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