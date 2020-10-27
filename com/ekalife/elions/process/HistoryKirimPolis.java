package com.ekalife.elions.process;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ui.jasperreports.JasperReportsUtils;
import org.springframework.util.StringUtils;

import produk_asuransi.n_prod;

import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentDao;
import com.google.gwt.http.client.Request;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Class yang digunakan untuk print surat unit link
 * @author Yusuf
 * @since Jan 4, 2006
 */
public class HistoryKirimPolis extends ParentDao{
	protected final Log logger = LogFactory.getLog( getClass() );
	
	public Map<String, Object> cetakHistoryKirimPolis(List view, String startDate, String endDate, int kondisi, boolean flagUpdate, int judulId, int pilihView) throws Exception{
		if(view.isEmpty()) return new HashMap<String, Object>();
		Map surat;
		
		if(pilihView==1) {
			surat = settingAkhir(startDate, endDate, kondisi, judulId, pilihView);
		}else {
			surat = null;
		}
		surat.put("view", view);
		return surat;
	}
	
	
	private Map settingAkhir(String startDate, String endDate, int kondisi, int judulId, int pilihView) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		//YANG DISETTING DISINI : 
		//- RINCIAN TRANSAKSI
		//- RINGKASAN TRANSAKSI
		//- ALOKASI BIAYA
		Map map = new HashMap();
		//startDate = (String) map.get("startDate");
		//endDate = (String) map.get("endDate");
		//int kondisi = (Integer)map.get("kondisi");
		//Date mtu_tgl_trans = (Date) ((Map) ((List) map.get("view")).get(0)).get("MU_TGL_TRANS");

		List daftar;
		//map.put("daftar", props.getProperty("subreport.uw.list_pengiriman_polis") + ".jasper");
		daftar = this.uwDao.selectListPengirimanPolis(startDate, endDate, kondisi);

		logger.info("Daftar = " + map.get("daftar"));
		map.put("sdsKirimPolis", JasperReportsUtils.convertReportData(daftar));
		//logger.info("Daftar = " + map.get("view"));
		//map.put("sdsKirimPolis", JasperReportsUtils.convertReportData(view));
		map.put("subReportDatakeys", new String[]{"sdsKirimPolis"}); 
		//logger.info("Daftar = " + map.get("view"));
		return map;
	}
	

}
