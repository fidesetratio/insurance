package com.ekalife.elions.dao;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import com.ekalife.elions.model.Deduct;
import com.ekalife.elions.model.Reksadana;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.tts.CaraBayar;
import com.ekalife.elions.model.tts.PolicyTts;
import com.ekalife.elions.model.tts.Tahapan;
import com.ekalife.elions.model.tts.Tts;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.parent.ParentDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings("unchecked")
public class FinanceDao extends ParentDao {
	protected final Log logger = LogFactory.getLog( getClass() );
	
	protected void initDao() throws DataAccessException{
		this.statementNameSpace = "elions.finance.";
	}
	
	public Map prosesIlustrasiDiscountMi(String kurs, double premi, Date tgl_trans, Date jt_tempo, int kali) throws DataAccessException{
		
		int bln_disc 	= Math.abs(selectMonthsBetWeen(commonDao.selectBeginOfMonth(tgl_trans), commonDao.selectBeginOfMonth(jt_tempo)));
		
		double pct_disc = selectDiscountRate(kali, bln_disc, kurs);
		double total	= premi * kali;
		double disc 	= total * (pct_disc/100);
		double bayar 	= total * (1 - (pct_disc/100));

		Map result = new HashMap();
		result.put("bln_disc", bln_disc);
		result.put("pct_disc", pct_disc);
		result.put("total", total);
		result.put("disc", disc);
		result.put("bayar", bayar);
		
		return result;
	}
	
	public HashMap selectLstPersenSyariah(int lsbs, int lsdbs) throws DataAccessException{
		Map m = new HashMap();
		m.put("lsbs", lsbs);
		m.put("lsdbs", lsdbs);
		return (HashMap) querySingle("selectLstPersenSyariah", m);
	}
	
	public double selectDiscountRate(int xbayar, int bulan, String lku_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("xbayar", xbayar);
		m.put("bulan", bulan);
		m.put("lku_id", lku_id);
		return (Double) querySingle("selectDiscountRate", m);
	}
	
	public int selectIsAgenKaryawan(String msag_id) throws DataAccessException{
		Integer result = (Integer) querySingle("selectIsAgenKaryawan", msag_id);
		return result == null ? 0 : result;
	}
	
	public int selectIsAgenCorporate(String msag_id) throws DataAccessException{
		Integer result = (Integer) querySingle("selectIsAgenCorporate", msag_id);
		return result == null ? 0 : result;
	}
	
	public String selectTglJurnalFromPre (String nopre) throws DataAccessException{
		return (String) querySingle("selectTglJurnalFromPre", nopre);
	}
	
	public int selectFlagBulananStableLinkStableSave(String reg_spaj) throws DataAccessException{
		Integer result = (Integer) querySingle("selectFlagBulananStableLinkStableSave", reg_spaj);
		return (result == null ? 0 : result);
	}
	
	public String selectVirtualAccountSpaj(String reg_spaj) throws DataAccessException{
		return (String) querySingle("selectVirtualAccountSpaj", reg_spaj);
	}
	
	public Map selectDataAverageCostReksadana(String ire_reksa_no, Date irt_trans_date) throws DataAccessException{
		Map m = new HashMap();
		m.put("ire_reksa_no", ire_reksa_no);
		m.put("irt_trans_date", irt_trans_date);
		return (Map) querySingle("selectDataAverageCostReksadana", m);
	}
	
	public Map selectDataAverageCostBuyingReksadana(String ire_reksa_no, Date irt_trans_date) throws DataAccessException{
		Map m = new HashMap();
		m.put("ire_reksa_no", ire_reksa_no);
		m.put("irt_trans_date", irt_trans_date);
		return (Map) querySingle("selectDataAverageCostBuyingReksadana", m);
	}
	
	public int selectCekSahamPerTanggal(String symbol, Date tanggal) {
		Map m = new HashMap();
		m.put("symbol", symbol);
		m.put("tanggal", tanggal);
		return (Integer) querySingle("selectCekSahamPerTanggal", m);
	}
	
	public int selectCekBondsPerTanggal(String invcode, String kode_bond, Date ibp_month) {
		Map m = new HashMap();
		m.put("invcode", invcode);
		m.put("kode_bond", kode_bond);
		m.put("ibp_month", ibp_month);
		return (Integer) querySingle("selectCekBondsPerTanggal", m);
	}
	
	public String selectKursReksadana(String ire_reksa_no) {
		return (String) querySingle("selectKursReksadana", ire_reksa_no);
	}
	
	public Double selectUnitTerakhir(String ire_reksa_no, Date irt_trans_date) {
		Map p = new HashMap();
		p.put("ire_reksa_no", ire_reksa_no);
		p.put("irt_trans_date", irt_trans_date);
		return (Double) querySingle("selectUnitTerakhir", p);
	}
	
	public Double selectDataSubscribeUnitReksadana(String ire_reksa_no, String irt_rtrans_jn, Date irt_trans_date) {
		Map p = new HashMap();
		p.put("ire_reksa_no", ire_reksa_no);
		p.put("irt_rtrans_jn", irt_rtrans_jn);
		p.put("irt_trans_date", irt_trans_date);
		return (Double) querySingle("selectDataSubscribeUnitReksadana", p);
	}
	
	public Double selectNavReksadana(String ire_reksa_no, Date ird_trans_date) {
		Map m = new HashMap();
		m.put("ire_reksa_no", ire_reksa_no);
		m.put("ird_trans_date", ird_trans_date);
		return (Double) querySingle("selectNavReksadana", m);
	}
	
	public List<Map> selectInvReksadanaName() {
		return (List<Map>) query("selectInvReksadanaName", null);
	}
	
	public List<DropDown> selectReksadanaByName(String nama) {
		return query("selectReksadanaByName", nama);
	}
	
	public String selectReksadanaTypeByNo(String ire_reksa_no) {
		return (String) querySingle("selectReksadanaTypeByNo", ire_reksa_no);
	}
	
	public int selectCekReksadanaPerTanggal(String ire_reksa_no, Date tanggal) {
		Map m = new HashMap();
		m.put("ire_reksa_no", ire_reksa_no);
		m.put("tanggal", tanggal);
		return (Integer) querySingle("selectCekReksadanaPerTanggal", m);
	}
	
	public int deleteSaham(Date tanggal, Date tanggalAkhir) {
		Map m = new HashMap();
		m.put("tanggal", tanggal);
		m.put("tanggalAkhir", tanggalAkhir);
		return delete("deleteSaham", m);
	}
	
	public int deleteReksadana(Date tanggal, Date tanggalAkhir) {
		Map m = new HashMap();
		m.put("tanggal", tanggal);
		m.put("tanggalAkhir", tanggalAkhir);
		return delete("deleteReksadana", m);
	}
	
	public int deleteBonds(Date tanggal) {
		return delete("deleteBonds", tanggal);
	}

	public void insertBonds(String kode_bond, Date ibp_month, String lus_id, Double ibp_price) {
		Map m = new HashMap();
		m.put("kode_bond", kode_bond);
		m.put("ibp_month", ibp_month);
		m.put("lus_id", lus_id);
		m.put("ibp_price", ibp_price);
		insert("insertBonds", m);
	}
	
	public void insertSaham(String lus_id, Date isp_trans_date, Double isp_price, String ks_stock) {
		Map m = new HashMap();
		m.put("lus_id", lus_id);
		m.put("isp_trans_date", isp_trans_date);
		m.put("isp_price", isp_price);
		m.put("ks_stock", ks_stock);
		insert("insertSaham", m);
	}
	
	public void insertReksadana(String lus_id, Date ird_trans_date, Double ird_nav, Double ird_unit, 
			Double ird_last_30d, Double ird_last_oney, Double ird_last_oneyr, String ire_reksa_no) {
		Map m = new HashMap();
		m.put("lus_id", lus_id);
		m.put("ird_trans_date", ird_trans_date);
		m.put("ird_nav", ird_nav);
		m.put("ird_unit", ird_unit);
		m.put("ird_last_30d", ird_last_30d);
		m.put("ird_last_oney", ird_last_oney);
		m.put("ird_last_oneyr", ird_last_oneyr);
		m.put("ire_reksa_no", ire_reksa_no);
		insert("insertReksadana", m);
	}
	
	public void insertTransaksiReksadana(Reksadana r) throws DataAccessException{
		insert("insertTransaksiReksadana", r);
	}
	
	public List<String> saveUploadSaham(String tanggal, String tanggalAkhir, List<Map> daftar, User currentUser){
		List<String> hasil = new ArrayList<String>();
		
		Date tgl = null, tglAkhir = null;
		try{
			tgl = defaultDateFormat.parse(tanggal);
			tglAkhir = defaultDateFormat.parse(tanggalAkhir);
		}catch(Exception e) {}
		//
		if(tgl == null || tglAkhir == null) {
			hasil.add("Tanggal yang dimasukkan tidak valid");
		}else {
			StringBuffer daftarGagal1 = new StringBuffer(), daftarGagal2 = new StringBuffer(), daftarSukses = new StringBuffer();
			int totalGagal1 = 0, totalGagal2 = 0, totalSukses = 0;
			//
			
			//looping per tanggal 
			do {
				//looping daftar saham
				for(Map m : daftar) {
					String symbol = (String) m.get("SYMBOL");
					double value = (Double) m.get("VALUE");
					//1. validasi 1 = tanggal hari H-1 harus ADA
					if(selectCekSahamPerTanggal(symbol, FormatDate.add(tgl, Calendar.DATE, -1)) == 0) {
						daftarGagal1.append(symbol + ", ");
						totalGagal1++;
					//2. validasi 2 = tanggal hari H harus TIDAK ADA
					}else if(selectCekSahamPerTanggal(symbol, tgl) > 0) {
						daftarGagal2.append(symbol + ", ");
						totalGagal2++;
					//3. simpan
					}else {
						insertSaham(currentUser.getLus_id(), tgl, value, symbol);
						daftarSukses.append(symbol + ", ");
						totalSukses++;
					}
				}
				tgl = FormatDate.add(tgl, Calendar.DATE, 1);
			}while(tgl.compareTo(tglAkhir) <= 0);			
			//
			if(totalGagal1 > 0) hasil.add(totalGagal1 + " GAGAL diproses karena Harga Saham Hari H-1 tidak ada : " + daftarGagal1);
			if(totalGagal2 > 0) hasil.add(totalGagal2 + " GAGAL diproses karena Harga Saham Hari H sudah ada : " + daftarGagal2);
			hasil.add(totalSukses + " SUKSES diproses : " + daftarSukses);
		}
		//
		return hasil;
	}

	public List<String> hapusSaham(String tanggal, String tanggalAkhir, User currentUser){
		List<String> hasil = new ArrayList<String>();
		
		Date tgl = null, tglAkhir = null;
		try{
			tgl = defaultDateFormat.parse(tanggal);
			tglAkhir = defaultDateFormat.parse(tanggalAkhir);
		}catch(Exception e) {}
		//
		if(tgl == null || tglAkhir == null) {
			hasil.add("Tanggal yang dimasukkan tidak valid");
		}else {
			hasil.add(deleteSaham(tgl, tglAkhir) + " data SAHAM berhasil dihapus untuk tanggal " + tanggal + " s/d " + tanggalAkhir);
		}
		//
		return hasil;
	}

	public List<String> hapusPooledFunds(String tanggal, String tanggalAkhir, User currentUser){
		List<String> hasil = new ArrayList<String>();
		
		Date tgl = null, tglAkhir = null;
		try{
			tgl = defaultDateFormat.parse(tanggal);
			tglAkhir = defaultDateFormat.parse(tanggalAkhir);
		}catch(Exception e) {}
		//
		if(tgl == null || tglAkhir == null) {
			hasil.add("Tanggal yang dimasukkan tidak valid");
		}else {
			hasil.add(deleteReksadana(tgl, tglAkhir) + " data POOLED FUNDS berhasil dihapus untuk tanggal " + tanggal + " s/d " + tanggalAkhir);
		}
		//
		return hasil;
	}	

	public List<String> saveUploadPooledFunds(String tanggal, String tanggalAkhir, List<Map> daftar, User currentUser,HttpServletRequest request) throws ServletRequestBindingException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		List<String> hasil = new ArrayList<String>();
		
		Date tgl = null, tglAkhir = null;
		try{
			tgl = defaultDateFormat.parse(tanggal);
			tglAkhir = defaultDateFormat.parse(tanggalAkhir);
		}catch(Exception e) {}
		//
		if(tgl == null || tglAkhir == null) {
			hasil.add("Tanggal yang dimasukkan tidak valid");
		}else {
			StringBuffer 
					daftarGagal1 = new StringBuffer(), daftarGagal2 = new StringBuffer(), 
					daftarGagal3 = new StringBuffer(), daftarGagal4 = new StringBuffer(), 
					daftarSukses = new StringBuffer();
			int totalGagal1 = 0, totalGagal2 = 0, totalGagal3 = 0, totalGagal4 = 0, totalSukses = 0;
			//

			//looping per tanggal 
			/*
			do {
				//looping daftar saham
				
				for(Map m : daftar) {
					String fund = (String) m.get("FUND");
					double nab = (Double) m.get("NAB");
					
					String ire_reksa_no = selectReksadanaByName(fund);
					
					//1. validasi 1 = ada gak reksadananya di sistem
					if(ire_reksa_no == null) {
						daftarGagal1.append(fund + ", ");
						totalGagal1++;
					//2. validasi 2 = tanggal hari H-1 harus ADA
					}else if(selectCekReksadanaPerTanggal(ire_reksa_no, FormatDate.add(tgl, Calendar.DATE, -1)) == 0) {
						daftarGagal2.append(fund + ", ");
						totalGagal2++;
					//3. validasi 3 = tanggal hari H harus TIDAK ADA
					}else if(selectCekReksadanaPerTanggal(ire_reksa_no, tgl) > 0) {
						daftarGagal3.append(fund + ", ");
						totalGagal3++;
					//4. validasi 4 = nab nya gak boleh 0
					}else if(nab == 0) {
						daftarGagal4.append(fund + ", ");
						totalGagal4++;
					//5. simpan
					}else {
						Double nav30 = selectNavReksadana(ire_reksa_no, FormatDate.add(tgl, Calendar.DATE, -30));
						Double nav365 = selectNavReksadana(ire_reksa_no, FormatDate.add(tgl, Calendar.DATE, -365));
						Double unit = selectUnitTerakhir(ire_reksa_no, tgl);
						String lku_id = selectKursReksadana(ire_reksa_no);
						
						int pembulat = 4; //default 4 angka belakang koma untuk rupiah
						if(lku_id.equals("02")) pembulat = 6; //6 angka belakang koma untuk dollar 
						
						Double last30d = null, last1y = null, last1yr = null;
						nab = FormatNumber.round(nab, pembulat);
						
						//hitung last 30d bila ada data historis 30 hari kebelakang
						if(nav30 != null) {
							last30d = FormatNumber.round(((nab - nav30.doubleValue()) / nav30.doubleValue()) * 12 * 100, pembulat);
						}
						//hitung last 1y bila ada data historis 365 hari kebelakang
						if(nav365 != null) {
							last1y = FormatNumber.round((nab - nav365.doubleValue()) / nav365.doubleValue() * 100, pembulat);
							last1yr = FormatNumber.round((nab - nav365.doubleValue()) / nav365.doubleValue() * 100, pembulat);
						}
						
						insertReksadana(currentUser.getLus_id(), tgl, nab, unit, last30d, last1y, last1yr, ire_reksa_no);
						daftarSukses.append(fund + ", ");
						totalSukses++;
					}
				}
				tgl = FormatDate.add(tgl, Calendar.DATE, 1);
			}while(tgl.compareTo(tglAkhir) <= 0);			
			*/
			//
			
			//looping daftar saham
			
			for(Map m : daftar) {

				//reset tanggal
				try {
					tgl = defaultDateFormat.parse(tanggal);
					tglAkhir = defaultDateFormat.parse(tanggalAkhir);
				} catch (ParseException e) {
					logger.error("ERROR :", e);
				}

				String fund = (String) m.get("FUND");
				double nab = (Double) m.get("NAB");
				
				//String ire_reksa_no = selectReksadanaByName(fund);
				String ire_reksa_no = ServletRequestUtils.getStringParameter(request, fund+"_type");
				logger.info("fund : "+fund+ ", no : "+ire_reksa_no);
				
				//1. validasi 1 = ada gak reksadananya di sistem
				if(ire_reksa_no == null) {
					daftarGagal1.append(fund + ", ");
					totalGagal1++;
				//2. validasi 2 = tanggal hari H-1 harus ADA
				}else if(selectCekReksadanaPerTanggal(ire_reksa_no, FormatDate.add(tgl, Calendar.DATE, -1)) == 0) {
					daftarGagal2.append(fund + ", ");
					totalGagal2++;
				//3. validasi 3 = tanggal hari H harus TIDAK ADA
				}else if(selectCekReksadanaPerTanggal(ire_reksa_no, tgl) > 0) {
					daftarGagal3.append(fund + ", ");
					totalGagal3++;
				//4. validasi 4 = nab nya gak boleh 0
				}else if(nab == 0) {
					daftarGagal4.append(fund + ", ");
					totalGagal4++;
				//5. simpan
				}else {
					Double nav30 = selectNavReksadana(ire_reksa_no, FormatDate.add(tgl, Calendar.DATE, -30));
					Double nav365 = selectNavReksadana(ire_reksa_no, FormatDate.add(tgl, Calendar.DATE, -365));
					Double unit = selectUnitTerakhir(ire_reksa_no, tgl);
					String lku_id = selectKursReksadana(ire_reksa_no);
					String ire_reksa_type = selectReksadanaTypeByNo(ire_reksa_no);//get Type reksadana
					
					int pembulat = 4; //default 4 angka belakang koma untuk rupiah
					if(lku_id.equals("02")) pembulat = 6; //6 angka belakang koma untuk dollar 
					
					if(lku_id.equals("01") && ire_reksa_type.equals("STABLE LINK")){ //Untuk Stable Link dan Rupiah NAB/1000
						nab = nab/1000;
					}
					
					Double last30d = null, last1y = null, last1yr = null;
					nab = FormatNumber.round(nab, pembulat);
					
					//hitung last 30d bila ada data historis 30 hari kebelakang
					if(nav30 != null) {
						// request dari David Investment
						//last30d = FormatNumber.round(((nab - nav30.doubleValue()) / nav30.doubleValue()) * 12 * 100, pembulat);
						last30d = FormatNumber.round(((nab - nav30.doubleValue()) / nav30.doubleValue()) * 100, pembulat);
					}
					//hitung last 1y bila ada data historis 365 hari kebelakang
					if(nav365 != null) {
						last1y = FormatNumber.round((nab - nav365.doubleValue()) / nav365.doubleValue() * 100, pembulat);
						last1yr = FormatNumber.round((nab - nav365.doubleValue()) / nav365.doubleValue() * 100, pembulat);
					}
					
					//looping per tanggal, insertnya sama semua, karena biasanya upload berhari2 gini buat hari libur (nab nya sama semua, yieldnya sama semua, gakl berubah)
					do {
						insertReksadana(currentUser.getLus_id(), tgl, nab, unit, last30d, last1y, last1yr, ire_reksa_no);
						tgl = FormatDate.add(tgl, Calendar.DATE, 1);
					}while(tgl.compareTo(tglAkhir) <= 0);
					
					daftarSukses.append(fund + ", ");
					totalSukses++;
				}
			}
			//
			
			if(totalGagal1 > 0) hasil.add(totalGagal1 + " GAGAL diproses karena Nama Fund Tidak Ditemukan di Sistem : " + daftarGagal1);
			if(totalGagal2 > 0) hasil.add(totalGagal2 + " GAGAL diproses karena Harga Fund Hari H-1 tidak ada : " + daftarGagal2);
			if(totalGagal3 > 0) hasil.add(totalGagal3 + " GAGAL diproses karena Harga Fund Hari H sudah ada : " + daftarGagal3);
			if(totalGagal4 > 0) hasil.add(totalGagal4 + " GAGAL diproses karena NAB 0 : " + daftarGagal4);
			hasil.add(totalSukses + " SUKSES diproses : " + daftarSukses);
		}
		//
		return hasil;
	}
	
	public List<String> saveUploadBonds(String tanggal, List<Map> daftar, User currentUser){
		List<String> hasil = new ArrayList<String>();
		
		Date tgl = null;
		try{
			tgl = defaultDateFormat.parse(tanggal);
		}catch(Exception e) {}
		//
		if(tgl == null) {
			hasil.add("Tanggal yang dimasukkan tidak valid");
		}else {
			StringBuffer daftarGagal1 = new StringBuffer(), daftarGagal2 = new StringBuffer(), daftarSukses = new StringBuffer();
			int totalGagal1 = 0, totalGagal2 = 0, totalSukses = 0;
			//
			
			//looping daftar bonds
			for(Map m : daftar) {
				String kode = (String) m.get("KODE");
				String seri = (String) m.get("SERI");
				double price = (Double) m.get("PRICE");
				
				//1. validasi 1 = tanggal hari H-1 harus ADA
				if(selectCekBondsPerTanggal("02", kode, FormatDate.add(tgl, Calendar.DATE, -1)) == 0) {
					daftarGagal1.append(kode + ", ");
					totalGagal1++;
				//2. validasi 2 = tanggal hari H harus TIDAK ADA
				}else if(selectCekBondsPerTanggal("02", kode, tgl) > 0) {
					daftarGagal2.append(kode + ", ");
					totalGagal2++;
				//3. simpan
				}else {
					insertBonds(kode, tgl, currentUser.getLus_id(), price);
					daftarSukses.append(kode + ", ");
					totalSukses++;
				}
			}
			//
			if(totalGagal1 > 0) hasil.add(totalGagal1 + " GAGAL diproses karena Harga Bond Hari H-1 tidak ada : " + daftarGagal1);
			if(totalGagal2 > 0) hasil.add(totalGagal2 + " GAGAL diproses karena Harga Bond Hari H sudah ada : " + daftarGagal2);
			hasil.add(totalSukses + " SUKSES diproses : " + daftarSukses);
		}
		//
		return hasil;
	}

	public List<String> hapusBonds(String tanggal, User currentUser){
		List<String> hasil = new ArrayList<String>();
		
		Date tgl = null;
		try{
			tgl = defaultDateFormat.parse(tanggal);
		}catch(Exception e) {}
		//
		if(tgl == null) {
			hasil.add("Tanggal yang dimasukkan tidak valid");
		}else {
			hasil.add(deleteBonds(tgl) + " data BONDS berhasil dihapus untuk tanggal " + tanggal);
		}
		//
		return hasil;
	}	
	
	public List<String> saveUploadTransaksiReksadana(List<Reksadana> daftar, User currentUser){
		List<String> hasil = new ArrayList<String>();

		StringBuffer daftarSukses = new StringBuffer();
		int totalSukses = 0;
		//
		
		Date sysdate = commonDao.selectSysdate();
		
		//looping daftar trans reksadana
		for(Reksadana r : daftar) {
			r.setIrt_input_date(sysdate);
			insertTransaksiReksadana(r);
			daftarSukses.append(r.getIre_reksa_no() + ", ");
			totalSukses++;
		}
		//
		hasil.add(totalSukses + " SUKSES diproses : " + daftarSukses);
		//
		return hasil;
	}
	
	public List selectListBayarKomisi(String startDate, String endDate) throws DataAccessException{
		Map map = new HashMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return query("selectListBayarKomisi", map);
	}
	public List warning_ttp() throws DataAccessException{
		Map map = new HashMap();
		return query("warning_ttp", map);
	}
	
	public List warning_ttp_komisigaDiproses() throws DataAccessException{
		Map map = new HashMap();
		return query("warning_ttp_komisigaDiproses", map);
	}
	
	public int count_ttp_komisigaDiproses(String spaj) throws DataAccessException{
		Map map = new HashMap();
		map.put("spaj", spaj);
		return (Integer) querySingle("count_ttp_komisigaDiproses", map);
	}
	
	public int count_sdhlewatKom(String spaj) throws DataAccessException{
		Map map = new HashMap();
		map.put("spaj", spaj);
		return (Integer) querySingle("count_sdhlewatKom", map);
	}
	
	public List selectDetailReksadanaByDate(Reksadana r) throws DataAccessException {
		return query("selectDetailReksadanaByDate", r); 
	}
	
	public List<Reksadana> selectReksadana(String nama) throws DataAccessException {
		return query("selectReksadana", nama);
	}
	
	public void insertDetailReksadana(Reksadana r) throws DataAccessException {
		insert("insertDetailReksadana", r);
	}
	
	public int selectCekExistDetailReksadana(String ire_reksa_no, Date ird_trans_date) throws DataAccessException {
		Map params = new HashMap();
		params.put("ire_reksa_no", ire_reksa_no);
		params.put("ird_trans_date", ird_trans_date);
		return (Integer) querySingle("selectCekExistDetailReksadana", params);
	}
	
	public List selectJenisDeduct() throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("elions.uw.selectJenisDeduct", null);
	}
	
	public boolean validationBillingPosition(String spaj, int posisi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("posisi", new Integer(posisi));
		Integer popopo = (Integer) querySingle("validationBillingPosition", params);
		if(popopo==null) return false;
		if(popopo > 0) return true;
		else return false;
	}
	
	public List selectKomisiAgen(String spaj, int tahun, int premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		return query("selectKomisiAgen", params);
	}
	
	public List selectDaftarKomisiAgen(String spaj) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lspd", new Integer(8));
		return query("selectDaftarKomisiAgen", params);
	}
	/**
	 * Fungsi:	Untuk Menampilkan data pada EKA.MST.DEDUCT
	 * @param 	String msco
	 * @return	Deduct
	 * @throws 	DataAccessException
	 * @author 	Ferry Harlim
	 */
	public Deduct selectMstDeduct(String msco){
		return (Deduct)querySingle("selectMstDeduct", msco);
	}
	
	public List selectDeductKomisiAgen(String msco) throws DataAccessException {
		return query("selectDeductKomisiAgen", msco);
	}

	public void saveDeduct(HttpServletRequest request, User currentUser) throws DataAccessException, ParseException{
		NumberFormat nf = NumberFormat.getNumberInstance();
		String msco_id = request.getParameter("msco_id");
		int lsjd_id[] = ServletRequestUtils.getIntParameters(request, "LSJD_ID");
		String msdd_date[] = request.getParameterValues("MSDD_DATE");
		String msdd_deduct[] = request.getParameterValues("MSDD_DEDUCT");
		String msdd_desc[] = request.getParameterValues("MSDD_DESC");
		String flag[] = request.getParameterValues("FLAG");
		int msdd_number[] = ServletRequestUtils.getIntParameters(request, "MSDD_NUMBER");
		for(int i=0; i<flag.length; i++) {
			if(flag[i].equals("U")) updateDeduct(msco_id, 
					new Integer(msdd_number[i]), new Integer(lsjd_id[i]), defaultDateFormat.parse(msdd_date[i]), nf.parse(msdd_deduct[i]).doubleValue(), msdd_desc[i], currentUser.getLus_id(), null);
			else if(flag[i].equals("I")) insertDeduct(msco_id, 
					new Integer(lsjd_id[i]), defaultDateFormat.parse(msdd_date[i]), new Double(msdd_deduct[i]), null, msdd_desc[i], currentUser.getLus_id());
		}
	}
	
	public void insertDeduct(String msco_id, Integer lsjd_id, Date msdd_date, Double msdd_deduct, Double msdd_tax, String msdd_desc, String lus_id) throws DataAccessException {
		Map params = new HashMap();
		params.put("msco_id", msco_id);
		params.put("lsjd_id", lsjd_id);
		params.put("msdd_date", msdd_date);
		params.put("msdd_deduct", msdd_deduct);
		params.put("msdd_tax", msdd_tax);
		params.put("msdd_desc", msdd_desc);
		params.put("lus_id", lus_id);
		insert("insertDeduct", params);
	}

	public void updateDeduct(String msco_id, Integer msdd_number, Integer lsjd_id, Date msdd_date, Double msdd_deduct, String msdd_desc, String lus_id, Double msdd_tax) throws DataAccessException {
		Map params = new HashMap();
		params.put("msco_id", msco_id);
		params.put("msdd_number", msdd_number);
		params.put("msdd_tax", msdd_tax);
		params.put("lsjd_id", lsjd_id);
		params.put("msdd_date", msdd_date);
		params.put("msdd_deduct", msdd_deduct);
		params.put("msdd_desc", msdd_desc);
		params.put("lus_id", lus_id);
		update("updateDeduct", params);
	}
	
	public Map selectInfoTransferFilling(String spaj, int tahun, int premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		return (Map) querySingle("selectInfoTransferFilling", params);
	}
	
	public void updatePayDate(String lus_id, String co_id) throws DataAccessException {
		Map params = new HashMap();
		params.put("lus_id", lus_id);
		params.put("co_id", co_id);
		update("updatePayDate", params);
	}
	
	public int validasiRekeningBank(String ag_id) throws DataAccessException {
		return ((Integer) querySingle("validasiRekeningBank", ag_id));
	}
	
	public void deleteUpload(String co_id) throws DataAccessException {
		delete("deleteUpload", co_id);
	}
	
	public void deleteUploadFa(String co_id) throws DataAccessException {
		delete("deleteUploadFa", co_id);
	}
	
	public void deleteUploadLippo(String co_id) throws DataAccessException {
		delete("deleteUploadLippo", co_id);
	}
	
	public void deleteUploadNon(String co_id) throws DataAccessException {
		delete("deleteUploadNon", co_id);
	}
	
	public void insertUpload(String co_id, String no_cek, String ag_id, String acc_agent, double ldec_kom) throws DataAccessException {
		Map params = new HashMap();
		params.put("co_id", co_id);
		params.put("no_cek", no_cek);
		params.put("ag_id", ag_id);
		params.put("acc_agent", acc_agent);
		params.put("ldec_kom", ldec_kom);
		params.put("pt", "0001");
		params.put("acc_pt", "2160000828");
		insert("insertUpload", params);
	}
	
	public void insertUploadFa(String co_id, String no_cek, String ag_id, String acc_agent, double ldec_kom) throws DataAccessException {
		Map params = new HashMap();
		params.put("co_id", co_id);
		params.put("no_cek", no_cek);
		params.put("ag_id", ag_id);
		params.put("acc_agent", acc_agent);
		params.put("ldec_kom", ldec_kom);
		params.put("pt", "0001");
		params.put("acc_pt", "2160000828");
		insert("insertUploadFa", params);
	}
	
	public void insertUploadLippo(String co_id, String ag_id, String acc_agent, double ldec_kom) throws DataAccessException {
		Map params = new HashMap();
		params.put("co_id", co_id);
		params.put("ag_id", ag_id);
		params.put("acc_agent", acc_agent);
		params.put("ldec_kom", ldec_kom);
		insert("insertUploadLippo", params);
	}
	
	public void insertUploadNon(String co_id, String ag_id, double ldec_kom) throws DataAccessException {
		Map params = new HashMap();
		params.put("co_id", co_id);
		params.put("ag_id", ag_id);
		params.put("ldec_kom", ldec_kom);
		insert("insertUploadNon", params);
	}
	
	public void insertDplk(String sekuens, String co_id, String spaj, String ag_id, String mcl_first, Integer lsle_id, double dplk, double dplk_pt) throws DataAccessException {
		Map params = new HashMap();
		params.put("sekuens", new Long(sekuens));
		params.put("co_id", co_id);
		params.put("reg_spaj", spaj);
		params.put("ag_id", ag_id);
		params.put("mcl_first", mcl_first);
		params.put("lsle_id", lsle_id);
		params.put("dplk", new Double(dplk));
		params.put("dplk_pt", new Double(dplk_pt));
		insert("insertDplk", params);
	}
	
	public Integer selectJumlahReward(String spaj, int tahun, int premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		return (Integer) querySingle("selectJumlahReward", params);
	}
	
	public Integer selectBankReward(String spaj, int tahun, int premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		return (Integer) querySingle("selectBankReward", params);
	}
	
	public void updateRewards(int flagUpload, String spaj, int tahun, int premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("flagUpload", new Integer(flagUpload));
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		update("updateRewards", params);
	}
	
	public Integer validationTopupBilling(String spaj) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lspd", new Integer(8));
		params.put("topup", new Integer(1));
		return (Integer) querySingle("validationTopUpBilling", params);
	}
	
	public List selectAllMstTts(String value,String tipe,String filter,String lcaId){
		Map param=new HashMap();
			param.put("tipe",tipe);
			if(value!=null)
				value=value.toUpperCase();
			if(filter==null)
				param.put("kata"," like '%' ||'"+value+"'|| '%'");
			else if(filter.equalsIgnoreCase("LIKE%"))
				param.put("kata"," like '"+value+"'|| '%'");
			else if(filter.equalsIgnoreCase("%LIKE"))
				param.put("kata"," like '%'||'"+value+"' ");
			else
				param.put("kata"," like '%' ||'"+value+"'|| '%'");
			param.put("lca_id", lcaId);
			//logger.info(param);
			return query("cariNomor",param);
	}
	
	public List selectMstPolicyTts(String nomor,String kurs){
		Map map=new HashMap();
		map.put("mstNo",nomor);
		map.put("kurs", kurs);
		return query("selectMstPolicyTts",map);
	}
	
	public List selectMstCaraByr(String nomor){
		return query("selectMstCaraByr",nomor);
	}
	
	public Map selectMaxNomorMstTtsMstNo(){
		return (HashMap) querySingle("elions.finance.selectMstTts.MstNo",null);
	}
	
	public List selectAllLstPaymentType(){
		return query("selectAllLstPaymentType",null);
	}
	
	public List selectInLstPaymentType(List value){
		return query("selectInLstPaymentType",value);
	}

	public void insertMstTts(Tts tts){
		  insert("insertMstTts",tts);
	}
	  
	public void insertMstPolicyTts(PolicyTts policyTts){
		  insert("insertMstPolicyTts",policyTts);
	}
	  
	public void insertMstCaraBayar(CaraBayar caraBayar){
		  insert("insertMstCaraBayar",caraBayar);
	}
	
	public Map selectPolicyTtsJumBayar(String mst_no){
		return (HashMap)querySingle("selectPolicyTtsJumBayar",mst_no);
	}
	
	public void updateMstTtsFlagPrint(String mstNo,Integer flag){
		Map map=new HashMap();
		map.put("mst_no",mstNo);
		map.put("flag",flag);
		update("updateMstTtsFlagPrint",map);
	}
	
	public void updateMstTtsFlagPrintAndKet(String mstNo,Integer flag,String desc){
		Map map=new HashMap();
		map.put("mst_no",mstNo);
		map.put("desc",desc);
		map.put("flag",flag);
		update("updateMstTtsFlagPrintAndKet",map);
	}

	public void deleteMstPolicyTts(String mstNo){
		delete("delete.mst_policy_tts",mstNo);
	}
	public void deleteMstCaraByr(String mstNo){
		delete("delete.mst_cara_byr",mstNo);
	}
	public void deleteMstTts(String mstNo){
		delete("delete.mst_tts",mstNo);
	}
	
	public Integer selectCekCounterTtsMonthAndYear(Integer aplikasi, String cabang) throws DataAccessException {
		Map params = new HashMap();
		params.put("aplikasi", aplikasi);
		params.put("cabang", cabang);
		return (Integer) querySingle("selectCekCounterTtsMonthAndYear", params);
	}

	public Map selectGetCounterTts(int aplikasi, String cabang) throws DataAccessException {
		Map params = new HashMap();
		params.put("aplikasi", new Integer(aplikasi));
		params.put("cabang", cabang);
		return (HashMap) querySingle("selectGetCounterTts", params);
	}

	public void updateMstCounterTts(Double ld_cnt,int ai_id,String ls_cabang){
		Map param=new HashMap();
		param.put("nilai",ld_cnt);
		param.put("aplikasi",new Integer(ai_id));
		param.put("cabang",ls_cabang);
		update("update.mst_counter_tts",param);
	}
	
	public List selectAllBillingNotPaid(String noPolis){
		return query("selectAllPolisNotPaid",noPolis);
	}

	public List selectAllBillingNotPaidNew(String noPolis){
		return query("selectAllPolisNotPaidNew",noPolis);
	}
	
	
	public void insertLstHistoryPrint(Integer ke,String mstNo,String kdCab,String ketPrint){
		Map map=new HashMap();
		map.put("ke",ke);
		map.put("mst_no",mstNo);
		map.put("kd_cab",kdCab);
		map.put("ket_print",ketPrint);
		insert("insertLstHistoryPrintTts",map);
	}
	
	public Integer selectMaxLstHistoryPrintTts(String mstNo){
		return (Integer)querySingle("selectMaxLstHistoryPrintTts",mstNo);
	}

	public Double selectLstBungaLsbunBungaTts(Integer lsbunJenis,String lkuId){
		Map map = new HashMap();
		map.put("lsbun_jenis",lsbunJenis);
		map.put("lku_id",lkuId);
		return (Double)querySingle("selectLstBungaLsbunBunga",map);
	}
	
	public Map selectMstPolicyLkudIdNLsbsId(String nopolis){
		return (HashMap) querySingle("selectMstPolicyLkuIdNLsbsId",nopolis);
	}
	
	public String selectLstCabangNamaCabang(String lcaId){
		return (String)querySingle("selectNamaCabang",lcaId);
	}
	
	public Map selectMstPolicy(String nopolis){
		return (HashMap)querySingle("selectMstPolicy",nopolis);
	}
	
	public Integer selectLstPayModeLscbTtlMonth(Integer lscbId){
		return (Integer)querySingle("selectLstPayModeLsCbTtlMonth",lscbId);
	}

	public Integer selectMonthsBetWeen(Date awal, Date akhir){
		Map map=new HashMap();
		map.put("awal",awal);
		map.put("akhir",akhir);
		return (Integer)querySingle("selectMonthsBetween",map);
	}

	public Double selectGetDiscountPlan(Integer lsbsId, Integer lsdbsNumber,Integer tahunKe){
		Map map=new HashMap();
		map.put("lsbsId",lsbsId);
		map.put("lsdbsNumber",lsdbsNumber);
		map.put("tahunKe",tahunKe);
		return (Double)querySingle("selectLstDiscountLsdisPersen",map);
	}
	
	public List selectLstHistoryPrintTts(String mstNo){
		return query("selectLstHistoryPrintTts",mstNo);
	}
	
	public void insertLstHistoryTts(String mstNo,Integer lusId,String desc){
		Map map=new HashMap();
		map.put("mst_no", mstNo);
		map.put("lus_id", lusId);
		map.put("mst_desc", desc);
		insert("insertMstHistoryTts", map);
	}
	
	public void updateMstTtsTglSetor(String mstNo,Date tglSetor){
		Map map=new HashMap();
		map.put("mst_no",mstNo );
		map.put("tgl_setor",tglSetor );
		update("update.MstTtsTglSetor", map);
	}
	
	public List selectMstHistoryTts(String mstNo){
		return query("selectMstHistoryTts", mstNo);		
	}
	
	public void updateMstTtsBatal(String mstNo,String mstNoBtl,String mstNoNew,Integer flag){
		Map map=new HashMap();
		map.put("mst_no", mstNo);
		map.put("mst_no_batal", mstNoBtl );
		map.put("mst_no_new", mstNoNew );
		map.put("flag", flag);
		update("update.MstTtsBatal", map);
	}
	public Tahapan selectMstTahapan(String nopolis,Integer lspdId){
		Map map=new HashMap();
		map.put("nopolis", nopolis);
		map.put("lspd_id", lspdId);
		return (Tahapan)querySingle("selectMstTahapan", map);
	}
	
	public List selectMstPolicyTtsNopolis(String mstNo){
		return query("selectMstPolicyTtsNopolis",mstNo );
	}
	
	public List selectViewerTts(String lusId,String lcaId,String flagPrint){
		Map map=new HashMap();
		map.put("lusId", lusId);
		map.put("lcaId", lcaId);
		map.put("flagPrint", flagPrint);
		return query("selectViewerTts",map);
	}
	
	public Date selectMstTtsTglSetor(String mstNo){
		return (Date)querySingle("selectMstTtsTglSetor", mstNo);
	}
	
	public void updateResetMstCounterTts(Integer aplikasi,String cabang){
		Map map=new HashMap();
		map.put("cabang", cabang);
		map.put("aplikasi", aplikasi);
		update("updateResetMstCounterTts", map);
	}
	
	public List selectListNab(){
		return query("selectListNab", null);
	}
	
	public String selectJenisInvestNab(String id){
		return (String) querySingle("selectJenisInvestNab", id);
	}

	public void insertLstNabUlink(String lji_id, Integer lnu_type, Date lnu_tgl, Double lnu_nilai) throws DataAccessException {
		Map m = new HashMap();
		m.put("lji_id", lji_id);
		m.put("lnu_type", lnu_type);
		m.put("lnu_tgl", lnu_tgl);
		m.put("lnu_nilai", lnu_nilai);
		insert("insertLstNabUlink", m);
	}
	
	public Double selectGetPremiSyariah(String reg_spaj, Integer tahun_ke, Integer premi_ke, Integer flag){
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("tahun_ke", tahun_ke);
		m.put("premi_ke", premi_ke);
		m.put("flag", flag);//flag : 1 = tabaru, 2 = ujrah, 3 = wakalah, 4 = mudarobah
		return (Double) querySingle("selectGetPremiSyariah", m);
	}
	
	public List selectKomisiAgenErbePackage(String spaj, int tahun, int premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("reg_spaj", spaj);
		params.put("tahun_ke", new Integer(tahun));
		params.put("premi_ke", new Integer(premi));
		return query("selectKomisiAgenErbePackage", params);
	}
	
}
