package com.ekalife.utils.test;

import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.ekalife.elions.model.ReinsProd;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ExternalDatabase {
	protected final static Log logger = LogFactory.getLog( ExternalDatabase.class );
	DataSource dataSource;
	PlatformTransactionManager transactionManager;
	
	public ExternalDatabase(String databaseName){
		DataSource ds = getDataSource(databaseName);
		this.dataSource = ds;
		DataSourceTransactionManager tm = new DataSourceTransactionManager();
		tm.setDataSource(ds);
		this.transactionManager = tm;
	}
	
	public static void main(String[] a) throws IOException, PropertyVetoException, ParseException{
		
		BufferedReader in = null;
		SimpleDateFormat tgl=new SimpleDateFormat("MM/dd/yyyy");
//		String[] daftarFile = new String[] {"D:/users/e-lion/test.txt"};
		//String[] daftarFile = new String[] {"D:/users/e-lion/mainRider2.txt"};
		String[] daftarFile = new String[] {"C:/mainRider2.txt"};

		//baca file
		List<String> daftarUpdate = new ArrayList<String>();
		for(String namaFile : daftarFile) {
			try {
				in = new BufferedReader(new FileReader(namaFile));
	
				String str;
				while ((str = in.readLine()) != null) {
					int startIndex = 0;
					int endIndex = str.length();
					if(startIndex>-1) {
						daftarUpdate.add(str.substring(startIndex, endIndex));
					}
				}
			}finally {
				try {
					if(in != null) {
						in.close();
					}
				}catch (Exception e) {
					logger.error("ERROR :", e);
				}
			}
		}
		
		ExternalDatabase koneksi = new ExternalDatabase(null);
		
		for(int d=1;d<daftarUpdate.size();d++){	
			  ReinsProd rp = new ReinsProd();
			  String tmp=daftarUpdate.get(d).toString();
			  String [] splitTemp= tmp.split("\t");
			  for(int e=0;e<splitTemp.length;e++){
				  
				  if(e==0){
					  rp.setReg_spaj(splitTemp[e]);//=splitTemp[e];
//					  logger.info("REG_SPAJ "+e+ " "+splitTemp[e]);
				  }else if (e==1){
					  rp.setMste_insured_no(1);//MSTE_INSURED_NO=;
//					  logger.info("MSTE_INSURED_NO "+e+ " "+splitTemp[e]);
				  }else if (e==2){
					  rp.setLsbs_id(Integer.parseInt(splitTemp[e]));//LSBS_ID=;
//					  logger.info("LSBS_ID "+e+ " "+splitTemp[e]);
				  }else if (e==3){
					  rp.setLsdbs_number(Integer.parseInt(splitTemp[e]));//LSDBS_NUMBER=splitTemp[e];
//					  logger.info("LSDBS_NUMBER "+e+ " "+splitTemp[e]);
				  }else if (e==4){
					  rp.setMsrpr_number(Integer.parseInt(splitTemp[e]));//()=splitTemp[e];
//					  logger.info("MSRPR_NUMBER "+e+ " "+splitTemp[e]);
				  }else if (e==5){
					  rp.setLku_id("01");//LKU_ID=splitTemp[e];
//					  logger.info("LKU_ID "+e+ " "+splitTemp[e]);
//				  }else if (e==6){
					  rp.setMsrpr_simultan(0.0);//=splitTemp[e];
//					  logger.info("MSRPR_SIMULTAN "+e+ " "+splitTemp[e]);
				  }else if (e==6){
					  rp.setMsrpr_tsi((new Double(splitTemp[e].replace(",", "").replace(".00", "").replace("\"", ""))));//MSRPR_TSI=splitTemp[e];
//					  logger.info("MSRPR_TSI "+e+ " "+splitTemp[e]);
				  }else if (e==7){
					  rp.setMsrpr_resiko_awal((new Double(splitTemp[e].replace(",", "").replace(".00", "").replace("\"", ""))));//MSRPR_RESIKO_AWAL=splitTemp[e];
//					  logger.info("MSRPR_RESIKO_AWAL "+e+ " "+splitTemp[e]);
				  }else if (e==8){
					  rp.setMsrpr_retensi((new Double(splitTemp[e].replace(",", "").replace(".00", "").replace("\"", ""))));//MSRPR_RETENSI=splitTemp[e];
//					  logger.info("MSRPR_RETENSI "+e+ " "+splitTemp[e]);
//				  }else if (e==10){
					  rp.setMsrpr_tsi_reas(0.0);//MSRPR_TSI_REAS=splitTemp[e];
//					  logger.info("MSRPR_TSI_REAS "+e+ " "+splitTemp[e]);
//				  }else if (e==11){
					  rp.setMsrpr_pa_class(0);//MSRPR_PA_CLASS=splitTemp[e];
//					  logger.info("MSRPR_PA_CLASS "+e+ " "+splitTemp[e]);
//				  }else if (e==12){
					  rp.setMsrpr_pa_risk("0");//MSRPR_PA_CLASS=;
//					  logger.info("MSRPR_PA_CLASS "+e+ " "+splitTemp[e]);
//				  }else if (e==13){
					  rp.setMsrpr_extra(0.00);//=splitTemp[e];
//					  logger.info("MSRPR_PA_RISK "+e+ " "+splitTemp[e]);
				  }else if (e==9){
					  rp.setMsrpr_beg_date(tgl.parse(splitTemp[e]));
					  //rp.setMsrpr_beg_date(tgl.parse(splitTemp[e]));//MSRPR_BEG_DATE=;
//					  logger.info("MSRPR_BEG_DATE "+e+ " "+splitTemp[e]);
				  }else if (e==10){
					  rp.setMsrpr_end_date(tgl.parse(splitTemp[e]));//MSRPR_END_DATE=;
//					  logger.info("MSRPR_END_DATE "+e+ " "+splitTemp[e]);
//				  }else if (e==16){
					  rp.setMsrpr_pemegang(0);//MSRPR_PEMEGANG=;
					  logger.info("MSRPR_PEMEGANG "+e+ " "+splitTemp[e]);
//				  }else if (e==17){
					  rp.setMsrpr_flag_premi(1);//MSRPR_FLAG_PREMI=splitTemp[e];
//					  logger.info("MSRPR_FLAG_PREMI "+e+ " "+splitTemp[e]);
//				  }else if (e==18){
					  rp.setMsrpr_sar(0.0);//MSRPR_SAR=splitTemp[e];
//					  logger.info("MSRPR_SAR "+e+ " "+splitTemp[e]);
//				  }else if (e==19){
					  rp.setMsrpr_premium(0.0);//MSRPR_PREMIUM=splitTemp[e];
					  logger.info("MSRPR_PREMIUM "+e+ " "+splitTemp[e]);
//				  }else if (e==20){
					  rp.setMsrpr_comm(0.00);//MSRPR_COMM=splitTemp[e];
//					  logger.info("MSRPR_COMM "+e+ " "+splitTemp[e]);
//				  }else if (e==21){
					  rp.setMsrpr_status_reas(0);//MSRPR_STATUS_REAS=splitTemp[e];
//					  logger.info("MSRPR_STATUS_REAS "+e+ " "+splitTemp[e]);
//				  }else if (e==22){
					  rp.setMsrpr_active(0);//MSRPR_ACTIVE=splitTemp[e];
//					  logger.info("MSRPR_ACTIVE "+e+ " "+splitTemp[e]);
//				  }else if (e==23){
					  rp.setMsrpr_convert(0);//MSRPR_CONVERT=splitTemp[e];
//					  logger.info("MSRPR_CONVERT "+e+ " "+splitTemp[e]);
//				  }else if (e==24){
					  rp.setMsrpr_extra_mort(0.0);//MSRPR_EXTRA_MORT=splitTemp[e];
//					  logger.info("MSRPR_EXTRA_MORT "+e+ " "+splitTemp[e]);
//				  }else if (e==25){
					  rp.setMsrpr_extra_hamil(0.0);//MSRPR_EXTRA_HAMIL=splitTemp[e];
//					  logger.info("MSRPR_EXTRA_HAMIL "+e+ " "+splitTemp[e]);
//				  }else if (e==26){
					  rp.setMrspr_extra_pa(0.0);//MRSPR_EXTRA_PA=splitTemp[e];
//					  logger.info("MRSPR_EXTRA_PA "+e+ " "+splitTemp[e]);
//				  }else if (e==27){
					  rp.setMsrpr_extra_pa(0.0);//MSRPR_EXTRA_PA=splitTemp[e];
//					  logger.info("MSRPR_EXTRA_PA "+e+ " "+splitTemp[e]);
//				  }else if (e==28){
					  rp.setMsrpr_extra_risk(0.0);//MSRPR_EXTRA_RISK=splitTemp[e];
//					  logger.info("MSRPR_EXTRA_RISK "+e+ " "+splitTemp[e]);
//				  }else if (e==29){
					  rp.setMsrpr_commision(0.0);//MSRPR_COMMISION=splitTemp[e];
//					  logger.info("MSRPR_COMMISION "+e+ " "+splitTemp[e]);
//				  }else if (e==30){
					  rp.setMsrpr_prm_ext_mort(0.0);//MSRPR_PRM_EXT_MORT=splitTemp[e];
//					  logger.info("MSRPR_PRM_EXT_MORT "+e+ " "+splitTemp[e]);
//				  }else if (e==31){
					  rp.setMsrpr_comm_ext_mort(0.0);//MSRPR_COMM_EXT_MORT=splitTemp[e];
//					  logger.info("MSRPR_COMM_EXT_MORT "+e+ " "+splitTemp[e]);
//				  }else if (e==32){
					  rp.setMsrpr_prm_ext_risk(0.0);//MSRPR_PRM_EXT_RISK=splitTemp[e];
//					  logger.info("MSRPR_PRM_EXT_RISK "+e+ " "+splitTemp[e]);
//				  }else if (e==33){
					  rp.setMsrpr_comm_ext_risk(0.0);//MSRPR_COMM_EXT_RISK=splitTemp[e];
//					  logger.info("MSRPR_COMM_EXT_RISK "+e+ " "+splitTemp[e]);
//				  }else if (e==34){
					  rp.setMsrpr_contract_year(null);//MSRPR_CONTRACT_YEAR=splitTemp[e];
//					  logger.info("MSRPR_CONTRACT_YEAR "+e+ " "+splitTemp[e]);
//				  }
//				  logger.info("kolomke "+e+ " "+splitTemp[e]);
				  }
			  }
			  //insertMstReinsProduct(rp);
			  
			  List query = koneksi.doQuery(
				"SELECT * FROM eka.mst_reins_product " +
				"WHERE reg_spaj = ? AND MSTE_INSURED_NO = 1 " +
				"AND LSBS_ID = ? AND LSDBS_NUMBER = ? AND MSRPR_NUMBER = ?",
				new Object[] {rp.getReg_spaj(), rp.getLsbs_id(), rp.getLsdbs_number(), rp.getMsrpr_number()}
			  );
			  
			  if(query.size() == 0) {
//				  koneksi.doInsert("INSERT INTO eka.mst_testes (a, b) VALUES (?,?)", 
//						  new Object[] {1, "123"}

//			  	  koneksi.doInsert("INSERT INTO eka.mst_testes (a, b,c,d,e,f,g," +
//			  	  		"h,i,j,k,l,m,n," +
//			  	  		"o,p,r,s,t," +
//			  	  		"u,v,w,x,y,z,aa,bb," +
//			  	  		"cc,dd,ee,ff," +
//			  	  		"gg,hh,ii) " +
//			  	  		"VALUES (?,?,?,?,?,?,?," +
//			  	  		"?,?,?,?,?,?,?," +
//			  	  		"?,?,?,?,?,?," +
//			  	  		"?,?,?,?,?,?,?," +
//			  	  		"?,?,?,?," +
//			  	  		"?,?,?)",
				  koneksi.doInsert("insert into eka.mst_reins_product"+ 
							" (reg_spaj	,mste_insured_no,	lsbs_id	,lsdbs_number	,msrpr_number,"+
									   "lku_id,	msrpr_simultan,	msrpr_tsi,	msrpr_resiko_awal,	msrpr_retensi,"+
									   "msrpr_tsi_reas,	msrpr_pa_class,	msrpr_pa_risk,	msrpr_extra,	msrpr_beg_date, "+
									   "msrpr_end_date,	msrpr_pemegang,	msrpr_flag_premi,	msrpr_sar,	msrpr_premium,"+
									   "msrpr_comm,	msrpr_status_reas,	msrpr_active,	msrpr_convert,	msrpr_extra_mort,"+	
									   "msrpr_extra_hamil,	mrspr_extra_pa,	msrpr_extra_pa,	msrpr_extra_risk,	"+
									   "msrpr_commision,	msrpr_prm_ext_mort,	msrpr_comm_ext_mort,	msrpr_prm_ext_risk,	"+
									   "msrpr_comm_ext_risk,	msrpr_contract_year) "+
									   "VALUES (?,?,?,?,?,?,?," +
							  	  		"?,?,?,?,?,?,?," +
							  	  		"?,?,?,?,?,?," +
							  	  		"?,?,?,?,?,?,?," +
							  	  		"?,?,?,?," +
							  	  		"?,?,?,?)",
						new Object[] {
						  rp.getReg_spaj(), 1,rp.getLsbs_id(),rp.getLsdbs_number(),rp.getMsrpr_number(),
						  rp.getLku_id(),rp.getMsrpr_simultan(), rp.getMsrpr_tsi(), rp.getMsrpr_resiko_awal(), rp.getMsrpr_retensi(),
						  0,null,null,null, rp.getMsrpr_beg_date(), 
						  rp.getMsrpr_end_date(),rp.getMsrpr_pemegang(),null,0,	rp.getMsrpr_premium(),
			  			  null,	null,	null,null,	null,
			  			  0, 0, 0, rp.getMsrpr_extra_risk(), 
			  			  rp.getMsrpr_commision(), rp.getMsrpr_prm_ext_mort(), rp.getMsrpr_comm_ext_mort(), rp.getMsrpr_prm_ext_risk(), 
			  			  rp.getMsrpr_comm_ext_risk(), rp.getMsrpr_contract_year()}
//				  koneksi.doInsert("insert into eka.mst_reins_product"+ 
//			" (reg_spaj	,mste_insured_no,	lsbs_id	,lsdbs_number	,msrpr_number,"+
//					   "lku_id,	msrpr_simultan,	msrpr_tsi,	msrpr_resiko_awal,	msrpr_retensi,"+
//					   "msrpr_tsi_reas,	msrpr_pa_class,	msrpr_pa_risk,	msrpr_extra,	msrpr_beg_date, "+
//					   "msrpr_end_date,	msrpr_pemegang,	msrpr_flag_premi,	msrpr_sar,	msrpr_premium,"+
//					   "msrpr_comm,	msrpr_status_reas,	msrpr_active,	msrpr_convert,	msrpr_extra_mort,"+	
//					   "msrpr_extra_hamil,	mrspr_extra_pa,	msrpr_extra_pa,	msrpr_extra_risk,	"+
//					   "msrpr_commision,	msrpr_prm_ext_mort,	msrpr_comm_ext_mort,	msrpr_prm_ext_risk,	"+
//					   "msrpr_comm_ext_risk,	msrpr_contract_year) "+
//					   "VALUES (?,?,?,?,?,?,?," +
//			  	  		"?,?,?,?,?,?,?," +
//			  	  		"?,?,?,?,?,?," +
//			  	  		"?,?,?,?,?,?,?," +
//			  	  		"?,?,?,?," +
//			  	  		"?,?,?)",
//						new Object[] {rp.getReg_spaj(), 1,rp.getLsbs_id(),rp.getLsdbs_number(),rp.getMsrpr_number(),rp.getLku_id(),rp.getMsrpr_simultan(),
//			  			rp.getMsrpr_tsi(),rp.getMsrpr_resiko_awal(),	rp.getMsrpr_retensi(),0.0,null,null,null,
//			  			rp.getMsrpr_beg_date(), rp.getMsrpr_end_date(),rp.getMsrpr_pemegang(),null,0.0,	rp.getMsrpr_premium(),
//			  			null,	null,	null,null,	null,0.0,	0.0,
//			  			rp.getMsrpr_extra_risk(),rp.getMsrpr_commision(),	rp.getMsrpr_prm_ext_mort(),	rp.getMsrpr_comm_ext_mort(),
//			  			 rp.getMsrpr_prm_ext_risk(),rp.getMsrpr_comm_ext_risk(),rp.getMsrpr_contract_year()}
				  );
			  }
		}
		
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
	
	public void doInsert(String sql, Object[] params) {
		DefaultTransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		td.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
		td.setTimeout(10);
		TransactionStatus status = transactionManager.getTransaction(td);
		
		try {
			JdbcTemplate jt = new JdbcTemplate(dataSource);
			jt.update(sql, params);
			transactionManager.commit(status);
		} catch (DataAccessException e) {
			logger.error("ERROR :", e);
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
	
	private static ComboPooledDataSource getDataSource(String databaseName){
		
		ComboPooledDataSource ds=null;
		
		try {
			//ambil dulu properties database nya
			Properties props;
			props = new Properties();
			FileInputStream in = new FileInputStream("C:\\EkaWeb\\jdbc_properties\\jdbc.properties");
			props.load(in);
			
			//setting datasourcenya
			ds = new ComboPooledDataSource();
			ds.setDriverClass(props.getProperty("oracle.driver"));
			if(databaseName != null) {
				ds.setJdbcUrl(props.getProperty(databaseName+".jdbc.url"));
				ds.setUser(props.getProperty(databaseName+".jdbc.user"));
				ds.setPassword(props.getProperty(databaseName+".jdbc.password"));
			}else {
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

}
