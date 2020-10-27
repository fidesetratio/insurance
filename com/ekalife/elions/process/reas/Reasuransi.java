package com.ekalife.elions.process.reas;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.List;

import com.ekalife.utils.parent.ParentDao;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Proses reas terbaru, men-deprecate versi lama
 * (com.ekalife.elions.process.uw.ReasIndividu)
 * 
 * @author Yusuf
 * @since May 15, 2008 (6:23:40 PM)
 */
public class Reasuransi extends ParentDao {
	protected final static Log logger = LogFactory.getLog( Reasuransi.class );

	public static void main(String[] args) throws IOException, SQLException{
		Reader reader = Resources.getResourceAsReader("com/ekalife/elions/process/reas/config.xml");
		SqlMapClient sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
		try {
			sqlMap.startTransaction();
			/** START PROSES REAS */

			double simultan, tsi, sar, retensi, reas; //variabel2 utama penampung data utama
			List<Reas> daftarReas = sqlMap.queryForList("selectSimultan", "11200700210");
			
			for(Reas r : daftarReas) logger.info(r.reg_spaj);;
			
			/** END PROSES REAS */
			//sqlMap.commitTransaction(); tidak pernah di-commit
		}catch(Exception e) {
			logger.error("ERROR :", e);
		} finally {
			sqlMap.endTransaction();
		}

	}

}