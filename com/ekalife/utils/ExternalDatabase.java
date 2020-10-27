package com.ekalife.utils;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ExternalDatabase {
	
	protected static final Log logger = LogFactory.getLog( ExternalDatabase.class );

	DataSource dataSource;
	PlatformTransactionManager transactionManager;
	
	public ExternalDatabase(String databaseName){
		DataSource ds = getDataSource(databaseName);
		this.dataSource = ds;
		DataSourceTransactionManager tm = new DataSourceTransactionManager();
		tm.setDataSource(ds);
		this.transactionManager = tm;
	}
	
	private static ComboPooledDataSource getDataSource(String databaseName){
		ComboPooledDataSource ds = null;
		
		try {
			//Ambil dulu properties database nya
			Properties props;
			props = new Properties();
			FileInputStream in = new FileInputStream("C:\\EkaWeb\\jdbc_properties\\jdbc.properties");
			props.load(in);
			
			//setting datasourcenya
			ds = new ComboPooledDataSource();
			if(databaseName != null) {
				if(databaseName.equals("ajs1_tss")){
					ds.setDriverClass(props.getProperty("sqlserver.driver"));
				}else{
					ds.setDriverClass(props.getProperty("oracle.driver"));
				}
				ds.setJdbcUrl(props.getProperty(databaseName+".jdbc.url"));
				ds.setUser(props.getProperty(databaseName+".jdbc.user"));
				ds.setPassword(props.getProperty(databaseName+".jdbc.password"));
			} else {
				ds.setDriverClass(props.getProperty("oracle.driver"));
				ds.setJdbcUrl(props.getProperty("ajsdb.jdbc.url"));
				ds.setUser(props.getProperty("ajsdb.jdbc.user"));
				ds.setPassword(props.getProperty("ajsdb.jdbc.password"));
			}
			ds.setAcquireIncrement(50);
			ds.setAutoCommitOnClose(false);
			ds.setMaxIdleTime(300);
		} catch (FileNotFoundException e) {
			logger.error("ERROR :", e);
		} catch (IOException e) {
			logger.error("ERROR :", e);
		} catch (PropertyVetoException e) {
			logger.error("ERROR :", e);
		}
		return ds;
	}
	
	public void doUpdate(String sql, Object[] params) {
		DefaultTransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		td.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
		td.setTimeout(10);
		TransactionStatus status = transactionManager.getTransaction(td);
		
		try {
			JdbcTemplate jt = new JdbcTemplate(dataSource);
			jt.update(sql, params);
			transactionManager.commit(status);
		} catch (DataAccessException e) {
			transactionManager.rollback(status);
		}
	}
	
	public List doQuery(String sql, Object[] params) {
		DefaultTransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		td.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
		td.setTimeout(10);
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		return jt.queryForList(sql, params);
	}

	public void doExec(String sql) {
		DefaultTransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		td.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
		td.setTimeout(10);
		TransactionStatus status = transactionManager.getTransaction(td);
		
		try {
			JdbcTemplate jt = new JdbcTemplate(dataSource);
			jt.execute(sql);
			transactionManager.commit(status);
		} catch (DataAccessException e) {
			transactionManager.rollback(status);
		}
	}
	
	/*
	public static void main(String[] a) throws IOException, PropertyVetoException{
		ExternalDatabase update = new ExternalDatabase("dplk8i");
		update.doUpdate(
				"INSERT INTO dplk.dplkworksite (regspaj, tglrk, iuranke, nopolis, netfund, posdoc, flagt, tgltrans) "+
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?)", 
				new Object[] {"1", new Date(), 1, "1", 1000.43, 1, 1, new Date()});
	}*/
	
}
