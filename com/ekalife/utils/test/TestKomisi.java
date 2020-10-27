package com.ekalife.utils.test;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

import com.ekalife.elions.model.Commission;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/*
List<Commission> daftar = new ArrayList<Commission>();
daftar.add(new Commission());
daftar.add(new Commission());
daftar.add(new Commission());
daftar.add(new Commission());
daftar.add(new Commission());
daftar.add(new Commission());

daftar.get(0).setLsla_id(6);
daftar.get(0).setLsla_id(5);
daftar.get(0).setLsla_id(4);
daftar.get(0).setLsla_id(3);
daftar.get(0).setLsla_id(2);
daftar.get(0).setLsla_id(1);
 */

public class TestKomisi {
	protected final static Log logger = LogFactory.getLog( TestKomisi.class );
	private enum tingkatArtha{RD, RM, DM, BM, SM, FC}; //lihat LST_LEVEL_ARTHA

	public static double testKomisiAgencyArthamas(Commission komisi) {
		// list komisi diurut berdasarkan LSLE_ID DESC
		
		tingkatArtha agen = null;
		tingkatArtha current = null;
		BigDecimal persentase[] = new BigDecimal[] {
				new BigDecimal("1"), 
				new BigDecimal("2"), 
				new BigDecimal("8"), 
				new BigDecimal("14"), 
				new BigDecimal("100")};

		/** START PERHITUNGAN OR (UNTUK LEVEL != 4) */
		//FC, lsla_id = 6 / lsle_id = 4
		//SM, lsla_id = 5 / lsle_id = 3 
		//BM, lsla_id = 4 / lsle_id = 2
		//DM, lsla_id = 3 / lsle_id = 1
		//RM, lsla_id = 2 / lsle_id = 0 / flag_sbm = 1 
		//RD, lsla_id = 1 / lsle_id = 0 / flag_sbm = 0
		
		switch(komisi.getLsla_id()) {
			case 6 :
				agen = tingkatArtha.FC;
				break;
			case 5 :
				agen = tingkatArtha.SM;
				break;
			case 4 :
				agen = tingkatArtha.BM;
				break;
			case 3 :
				agen = tingkatArtha.DM;
				break;
			case 2 :
				agen = tingkatArtha.RM;
				break;
			case 1 :
				agen = tingkatArtha.RD;
				break;
		}
		
		komisi.setKomisi((double) 0);
		
		if(komisi.getLev_kom()!=null) {
			if(komisi.getLev_kom()<6) {
				//karena OR baru, persentasenya dihardcode, gak ngambil dari database
				komisi.setKomisi((double) 0);
				//kalau yang nutup ME, ORnya gulung dari atas (RM)
				if(current.ordinal()>agen.ordinal()) {
					for(int j = current.ordinal(); j>agen.ordinal(); j--) {
						komisi.setKomisi(persentase[j-1].add(new BigDecimal(komisi.getKomisi())).doubleValue());
					}
				//kalo yang nutup bukan ME, ORnya gulung dari bawah (ME)
				}else if(current.equals(agen)) {
					for(int j = current.ordinal(); j<tingkatArtha.FC.ordinal(); j++) {
						komisi.setKomisi(persentase[j].add(new BigDecimal(komisi.getKomisi())).doubleValue());
					}
				}
			}
		}
		current = agen;
		/** END PERHITUNGAN OR */
		
		return komisi.getKomisi();
	}
	
	public static void main(String[] args) throws Exception {
		NumberFormat nf = NumberFormat.getNumberInstance();

		SqlMapClient sql = initSqlMap();
		List<Commission> daftar = sql.queryForList("selectKomisiArthamas", "47200700001");
		
		for(Commission komisi : daftar) {
			logger.info(nf.format(testKomisiAgencyArthamas(komisi)));
		}
		
	}
	
	private static SqlMapClient initSqlMap() throws IOException{
		Reader reader = Resources.getResourceAsReader("com/ekalife/utils/test/sql-map-config.xml");		
		return SqlMapClientBuilder.buildSqlMapClient(reader);
	}
	
}