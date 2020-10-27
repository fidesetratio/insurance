package com.ekalife.utils.parent;

import id.co.sinarmaslife.std.spring.util.Email;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.ekalife.elions.service.AjaxManager;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.Products;

/**
 * <p>
 * Merupakan parent dari semua formcontroller, dimana controller jenis ini
 * mempunyai tingkat kompleksitas yang lebih tinggi. Controller ini digunakan
 * untuk halaman2 form, misalnya form input data, Dan controller ini mempunyai
 * fungsi2 seperti validasi, customeditor, dan fitur2 lainnya
 * </p>
 * @author Yusuf
 * @since 07/08/2006
 * @see abstract parent beans pada applicationContext.xml
 */
public abstract class ParentFormController extends SimpleFormController {

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
	protected CustomNumberEditor decimalEditor;
	protected CustomDateEditor dateEditor;

	//Formatter
	protected DateFormat completeDateFormat;
	protected DateFormat defaultDateFormat;
	protected DateFormat defaultDateFormatStripes;
	protected NumberFormat twoDecimalNumberFormat;
	protected NumberFormat fiveDecimalNumberFormat;
	
	public AjaxManager getAjaxManager() {
		return ajaxManager;
	}

	public void setAjaxManager(AjaxManager ajaxManager) {
		this.ajaxManager = ajaxManager;
	}

	public NumberFormat getFiveDecimalNumberFormat() {
		return fiveDecimalNumberFormat;
	}

	public void setFiveDecimalNumberFormat(NumberFormat fiveDecimalNumberFormat) {
		this.fiveDecimalNumberFormat = fiveDecimalNumberFormat;
	}

	public NumberFormat getTwoDecimalNumberFormat() {
		return twoDecimalNumberFormat;
	}

	public void setTwoDecimalNumberFormat(NumberFormat twoDecimalNumberFormat) {
		this.twoDecimalNumberFormat = twoDecimalNumberFormat;
	}

	public DateFormat getDefaultDateFormat() {
		return defaultDateFormat;
	}

	public void setDefaultDateFormat(DateFormat defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
	}

	public Log getLogger() {
		return logger;
	}

	public void setDefaultDateFormatStripes(DateFormat defaultDateFormatStripes) {
		this.defaultDateFormatStripes = defaultDateFormatStripes;
	}

	public DateFormat getDefaultDateFormatStripes() {
		return defaultDateFormatStripes;
	}

	public UwManager getUwManager() {
		return uwManager;
	}

	public void setUwManager(UwManager uwManager) {
		this.uwManager = uwManager;
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

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public CustomNumberEditor getDecimalEditor() {
		return decimalEditor;
	}

	public void setDecimalEditor(CustomNumberEditor decimalEditor) {
		this.decimalEditor = decimalEditor;
	}

	public void setCompleteDateFormat(DateFormat completeDateFormat) {
		this.completeDateFormat = completeDateFormat;
	}

	public DateFormat getCompleteDateFormat() {
		return completeDateFormat;
	}
	public BacManager getBacManager() {
		return bacManager;
	}

	public void setBacManager(BacManager bacManager) {
		this.bacManager = bacManager;
	}
	
}