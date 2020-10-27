package com.ekalife.utils.parent;

import java.text.DateFormat;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.ekalife.elions.service.AjaxManager;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.Products;

import id.co.sinarmaslife.std.spring.util.Email;

/**
 * <p>
 * Parent dari semua controller yang paling simple. Controller merupakan bagian C dari konsep MVC
 * dimana controller2 menjadi pusat pengatur lalu lintas di dalam aplikasi, misalnya sbb:
 * </p>
 * <ul>
 * 	<li>Pada saat user menekan tombol, arahkan kemana?</li>
 * 	<li>Setelah selesai isi form, simpan datanya ke database</li>
 * 	<li>Apabila user menekan link, arahkan ke halaman lain</li>
 * </ul> 
 * 
 * @author Yusuf
 * @since 07/08/2006
 * @see abstract parent beans pada applicationContext.xml
 */
public abstract class ParentController implements Controller {

	protected final Log logger = LogFactory.getLog(getClass());
	
	protected ElionsManager elionsManager;
	protected UwManager uwManager;
	protected BacManager bacManager;

	protected AjaxManager ajaxManager;
	protected Properties props;
	protected Products products;
	protected DateFormat completeDateFormat;
	protected DateFormat defaultDateFormat;
	protected DateFormat defaultDateFormatStripes;
	protected Email email;
	
	/*
	protected Connection connection = null;

	protected Connection getConnection() {
		if(this.connection==null) {
			try {
				this.connection = this.elionsManager.getUwDao().getDataSource().getConnection();
			} catch (SQLException e) {
				logger.error("ERROR :", e);
			}
		}
		return this.connection;
	}
	*/

	public AjaxManager getAjaxManager() {
		return ajaxManager;
	}

	public void setAjaxManager(AjaxManager ajaxManager) {
		this.ajaxManager = ajaxManager;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
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

	public DateFormat getCompleteDateFormat() {
		return completeDateFormat;
	}

	public void setCompleteDateFormat(DateFormat completeDateFormat) {
		this.completeDateFormat = completeDateFormat;
	}

	public DateFormat getDefaultDateFormatStripes() {
		return defaultDateFormatStripes;
	}

	public void setDefaultDateFormatStripes(DateFormat defaultDateFormatStripes) {
		this.defaultDateFormatStripes = defaultDateFormatStripes;
	}

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

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}