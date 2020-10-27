package com.ekalife.gwt.server;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.gwt.client.DbService;
import com.ekalife.gwt.client.model.Polis;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class DbServiceImpl extends RemoteServiceServlet implements DbService {
	
	protected final Log logger = LogFactory.getLog( getClass() );

	private SqlMapClient sql;

	private static SqlMapClient initSqlMap() throws IOException{
		//long before = System.currentTimeMillis();
		Reader reader = Resources.getResourceAsReader("com/ekalife/gwt/server/sql/sql-map-config.xml");		
		SqlMapClient result = SqlMapClientBuilder.buildSqlMapClient(reader);
		//logger.info("SQL Map initialized in " + (System.currentTimeMillis() - before)/1000 + " seconds");
		return result;
	}

    public String dummy(String dummy) {
        return dummy;
    }
    
	public List getDaftarPolis() {
		List result = null;
		try {
			if(this.sql == null) this.sql = initSqlMap();
			result = sql.queryForList("eka8i.selectDaftarPolis", null);
		} catch (IOException e) {
			logger.error("ERROR :", e);
		} catch (SQLException e) {
			logger.error("ERROR :", e);
		}
		return result;
	}
	
	public int updateDaftarPolis(List daftarPolis) {
		int counter = 0;
		try {
			if(this.sql == null) this.sql = initSqlMap();
			sql.startTransaction();
			for(int i=0; i<daftarPolis.size(); i++) {
				Polis p = (Polis) daftarPolis.get(i);
				if(p.update == 1) {
					sql.update("eka8i.updateDaftarPolis", p.reg_spaj);
					counter++;
				}
			}
			sql.commitTransaction();
		} catch (IOException e) {
			logger.error("ERROR :", e);
		} catch (SQLException e) {
			logger.error("ERROR :", e);
		} finally {
			try {
				sql.endTransaction();
			} catch (SQLException e) {
				logger.error("ERROR :", e);
			}
		}
		return counter;
	}
	
}