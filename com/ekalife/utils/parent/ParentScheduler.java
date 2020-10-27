package com.ekalife.utils.parent;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;

import id.co.sinarmaslife.std.spring.util.Email;

/**
 * @spring.bean Class ini parent untuk semua scheduler
 * 
 * @author Yusuf Sutarko
 * @since 11 Mar 2010
 */
public abstract class ParentScheduler{
	
	protected final Log logger = LogFactory.getLog( getClass() );
 
	protected ElionsManager elionsManager;
	protected UwManager uwManager;
	protected BacManager bacManager;
	protected Email email;
	protected NumberFormat numberFormat;
	protected Properties props;
	protected String jdbcName;
	protected DateFormat dateFormat;
	//protected Connection connection = null;
	
	public void setJdbcName(String jdbcName) {this.jdbcName = jdbcName;}	
	public void setDateFormat(DateFormat dateFormat) {this.dateFormat = dateFormat;}
	public void setEmail(Email email) {this.email = email;}
	public void setProps(Properties props) {this.props = props;}
	public void setNumberFormat(NumberFormat numberFormat) {this.numberFormat = numberFormat;}
	public void setElionsManager(ElionsManager elionsManager) {this.elionsManager = elionsManager;}
	public void setUwManager(UwManager uwManager) {this.uwManager = uwManager;}
	public void setBacManager(BacManager bacManager) {this.bacManager = bacManager;}

	public ParentScheduler() {
		this.dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
	}
	
	//Singleton method untuk menyimpan objek koneksi
	/*
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

}