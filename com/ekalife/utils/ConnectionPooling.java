package com.ekalife.utils;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS;
import org.apache.commons.dbcp.datasources.SharedPoolDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class yang digunakan untuk mengatur connection pooling pada jdbc biasa
 * 
 * @author Yusuf
 * @since 01/11/2005
 */
public class ConnectionPooling{
	protected final Log logger = LogFactory.getLog( getClass() );
	private static final long serialVersionUID = -2473243694529319421L;
	private static DataSource ds;

	/**
	 * Constructor ConnectionPooling, menginisialisasi pooling untuk koneksi
	 * apabila belum ada
	 */
	public ConnectionPooling() {
		if (ds == null)
			try {
				DriverAdapterCPDS cpds = new DriverAdapterCPDS();
//				Properties props = Resources.getResourceAsProperties("jdbc.properties");
				Properties props = new Properties();// Resources.getResourceAsProperties("jdbc.properties");
				
				props.load(new FileInputStream("C:\\EkaWeb\\jdbc_properties\\jdbc.properties"));
				
				cpds.setDriver(props.getProperty("orcle.driver"));
				cpds.setUrl(props.getProperty("ajsdb.jdbc.url"));
				cpds.setUser(props.getProperty("ajsdb.jdbc.user"));
				cpds.setPassword(props.getProperty("ajsdb.jdbc.password"));
				SharedPoolDataSource ppds = new SharedPoolDataSource();
				ppds.setConnectionPoolDataSource(cpds);
				ppds.setMaxActive(0);
				ppds.setMaxWait(50);
				ds = ppds;
			} catch (Exception e) {
				logger.info(e.toString());
			}
	}

	/**
	 * Fungsi untuk mengambil koneksi dari ConnectionPooling (lebih baik
	 * daripada DriverManager.getConnection(..))
	 * 
	 * @return Object Connection yang sudah aktif
	 * @see Connection
	 */
	public Connection getConnection() throws SQLException {
		Connection conn = ds.getConnection();
		if (conn == null)
			throw new SQLException("Maximum Connection Reached");
		else
			return conn;
	}

	/**
	 * Fungsi untuk mengambil datasource
	 * 
	 * @return DataSource
	 * @see DataSource
	 */
	public DataSource getDs() throws SQLException {
		return ds;
	}

}
