package com.ekalife.elions.process;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.jasperreports.JasperReportsUtils;
import org.springframework.util.StringUtils;

import produk_asuransi.n_prod;

import com.ekalife.elions.model.User;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.AngkaTerbilang;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Class yang digunakan untuk print surat unit link
 * @author Yusuf
 * @since Jan 4, 2006
 */
public class SuratUnitLink extends ParentDao{
	protected final Log logger = LogFactory.getLog( getClass() );
	public static final int ULINK_RINCIAN = 1;
	public static final int ULINK_RINGKASAN = 2;
	public static final int ULINK_ALOKASI_INVESTASI = 4;
	public static final int ULINK_CATATAN = 8;
	public static final int ULINK_ALOKASI_BIAYA = 16;
	public static final int ULINK_ALOKASI_BIAYA_AKUISISI = 32;
	
	public Double getBiayaAkuisisi(int cara_bayar, String spaj, String businessId, Integer businessNumber, int payPeriod, int tahunKe, n_prod produk) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NumberFormatException, SQLException {
		
		//Yusuf (24/10/2007) -> kalo karyawan ekalife, bia akuisisi lain (revisi, sekarang ada kolom flag di MST_INSURED)
		Integer prod_karyawan= 0;
		
		if(spaj.length()>1){//kalau length hanya 1, brarti value spaj diambil dari mste_flag_el, selain itu ambil dari reg_spaj
			Integer mste_flag_el = bacDao.selectIsKaryawanEkalife(spaj);
			if(mste_flag_el==1){//Cek Ulang mste_flag_el dari SPAJ 
				prod_karyawan= 1;
			}else{
				prod_karyawan= 0;
			}
		}else if(spaj.length()==1){
			if(spaj.equals("1")){
				prod_karyawan= 1;
			}else{
				prod_karyawan= 0;
			}
		}
		
		if(prod_karyawan==1) {
			//Deddy (30/01/2012) -> Untuk karyawan sinarmasmsiglife, bisa diliat dari mste_flag_el, apabila 1 maka biaya akuisisi nol
//			if (Integer.valueOf(businessId).intValue() == 141) {
//				return produk.f_get_bia_akui_ekalife(payPeriod,1);
//			}else{
//				return produk.f_get_bia_akui(payPeriod,1);
//			}
			return 0.;
		}else {
			
			//Apabila ada Hardcoding ketentuan untuk plan produk, diinfokan juga ke Team Rudy Hermawan
			
			Integer byr = payPeriod;
			
			if ((Integer.valueOf(businessId).intValue() == 162) || (Integer.valueOf(businessId).intValue() == 140) || (Integer.valueOf(businessId).intValue() == 116) || 
					(Integer.valueOf(businessId).intValue() == 118) || (Integer.valueOf(businessId).intValue() == 159) || (Integer.valueOf(businessId).intValue() == 160) || 
					(Integer.valueOf(businessId).intValue() == 153) || (Integer.valueOf(businessId).intValue() == 199)) {
				byr = produk.ii_contract_period;
			}else if(Integer.valueOf(businessId).intValue() == 138 || Integer.valueOf(businessId).intValue() == 190 || Integer.valueOf(businessId).intValue() == 191 || Integer.valueOf(businessId).intValue() == 200|| Integer.valueOf(businessId).intValue() == 213
					|| Integer.valueOf(businessId).intValue() == 216 || Integer.valueOf(businessId).intValue() == 217 || Integer.valueOf(businessId).intValue() == 218 || Integer.valueOf(businessId).intValue() == 220 || Integer.valueOf(businessId).intValue() == 224) {
				byr = 80;
			}
			Integer number = businessNumber;
			if (Integer.valueOf(businessId).intValue() == 162 ) {
				if (businessNumber.intValue() == 3 || businessNumber.intValue() == 5 || businessNumber.intValue() == 7) {
					number = new Integer(1);
				}else if (businessNumber.intValue() == 4 || businessNumber.intValue() == 6 || businessNumber.intValue() == 8) {
					number = new Integer(2);
				}
			}
			if (cara_bayar==0) {
				byr=new Integer(1);
			}else if ( cara_bayar == 1 ||  cara_bayar == 2 ||  cara_bayar == 6 ) {
				 cara_bayar = 3;
			}
			
			return uwDao.select_biaya_akuisisi(Integer.valueOf(businessId).intValue(), number.intValue(), cara_bayar, tahunKe, byr.intValue());
			// return produk.f_get_bia_akui(payPeriod, tahunKe);
		}

	}
	
	public Map<String, Object> cetakSuratEndorsemen(List view, String spaj, boolean flagUpdate, int judulId, int pilihView,int tu_ke) throws Exception{
		if(view.isEmpty()) return new HashMap<String, Object>();
		Map surat;
		
		surat = settingEndorsemen(spaj, judulId, pilihView,tu_ke);
		surat.put("view", view);
		
		return surat;
	}
	
	public Map<String, Object> prosesCetakSuratSimasCard(String spaj, String lus_id, HttpServletRequest request) throws Exception{
		Map params = new HashMap();
		params.put("reportPath", "/WEB-INF/classes/" + props.get("report.surat_simcard"));
		return params;
	}
	
	public Map<String, Object> cetakSuratUnitLink(List view, String spaj, boolean flagUpdate, int judulId, int pilihView,int tu_ke) throws Exception{
		if(view.isEmpty()) return new HashMap<String, Object>();
		Map surat;
//		lstr_ulink.reg_spaj = is_reg_spaj
//		lstr_ulink.flag_update = True
//		lstr_ulink.JUDUL_ID = 1
//		lstr_ulink.pilih_view = 1
		
		if(pilihView==1) {
			//cetak new business / pertama kali
			surat = setting1(spaj, judulId, pilihView);
			surat.put("view", view);
			settingAkhir(surat);
//		}else if(pilihView==2) {
			//TODO (Yusuf) -> view transaksi yg dipilih selain yg nb
//		}else if(pilihView==3) {
			//TODO (Yusuf) -> view SEMUA transaksi
//		}else if(pilihView==4) {
			//TODO (Yusuf) -> view ilustrasi
/*
		If Isvalid(w_viewer_data_ulink) Then
			li_jenis_trans = w_viewer_data_ulink.dw_3.GetItemNumber(istr_ulink.row_ke, 'lt_id')
			If li_jenis_trans = 1 or li_jenis_trans = 2 Then
				//view ilustrasi pembayaran / top-up
				wf_setting4()
			Elseif li_jenis_trans = 3 Then
				//view ilustrasi w/d
				wf_setting5()
			Else
				//view ilustrasi switching
				wf_setting6()
			End if
		End if
 */
		}else if(pilihView==10	) {
			surat = settingSlinkTopUp(spaj, judulId, pilihView,tu_ke);
			surat.put("view", view);
			settingAkhirSLinkTopUp(surat);
		}else if(pilihView==11	) {
			surat = settingPsaveTopUp(spaj, judulId, pilihView,tu_ke);
			surat.put("view", view);
			settingAkhirSLinkTopUp(surat);
		
		}else {
		
			surat = null;
			surat.put("view", view);
			settingAkhir(surat);
		}
		
		
		return surat;
	}
	
	private void settingAkhir(Map map) {
		//YANG DISETTING DISINI : 
		//- RINCIAN TRANSAKSI
		//- RINGKASAN TRANSAKSI
		//- ALOKASI BIAYA
		String spaj = (String) map.get("spaj");
		Date mtu_tgl_trans = (Date) ((Map) ((List) map.get("view")).get(0)).get("MU_TGL_TRANS");

		List alokasiBiaya = null;
		String lsbs_id = uwDao.selectBusinessId(spaj);
		Integer lsdbs_number = uwDao.selectBusinessNumber(spaj);
		
		if("116,159,190,213,217".indexOf(lsbs_id)>-1) {
			map.put("alokasiBiaya", props.getProperty("subreport.alokasi_biaya_ekalink") + ".jasper");
			alokasiBiaya = this.uwDao.selectSuratUnitLinkAlokasiBiayaEkalink(spaj, mtu_tgl_trans);
		}else if(products.stableLink(lsbs_id)) {
//			logger.info("STABLE LINK ");
//			map.put("alokasiBiaya", props.getProperty("subreport.alokasi_biaya_slink") + ".jasper");
//			alokasiBiaya = this.uwDao.selectSuratStableLinkAlokasiBiaya(spaj, mtu_tgl_trans);
		}else if(products.syariah(lsbs_id,lsdbs_number.toString())) {
			if(lsbs_id.equals("202")){
				map.put("alokasiBiaya", props.getProperty("subreport.alokasi_biaya_exclink_syariah") + ".jasper");
				alokasiBiaya = this.uwDao.selectSuratExcelLinkAlokasiBiayaSyariah(spaj, mtu_tgl_trans);	
			}else{
				map.put("alokasiBiaya", props.getProperty("subreport.alokasi_biaya_syariah") + ".jasper");
				alokasiBiaya = this.uwDao.selectSuratUnitLinkAlokasiBiayaSyariah(spaj, mtu_tgl_trans);	
			}

		}else if(lsbs_id.equals("165")) {
			alokasiBiaya = this.uwDao.selectSuratInvestimaxAlokasiBiaya(spaj, mtu_tgl_trans);
		}else {
			alokasiBiaya = this.uwDao.selectSuratUnitLinkAlokasiBiaya(spaj, mtu_tgl_trans);
		}
		
		//MANTA - Khusus Produk SMiLe LINK MEDIVEST (140)
		//Cek apakah ada additional unitnya
		if(lsbs_id.equals("140")) {
			List additionalUnit = null;
			additionalUnit = this.uwDao.selectSuratUnitLinkAdditionalUnit(spaj, mtu_tgl_trans);
			if(additionalUnit != null){
				alokasiBiaya.addAll(additionalUnit);
			}
		}
		//logger.info("ALOKASI BIAYA = " + map.get("alokasiBiaya"));
		
		List rincian;
		rincian = this.uwDao.selectSuratUnitLinkRincianTrans(spaj, mtu_tgl_trans);
		
		List rider;
		rider = this.uwDao.selectSuratUnitLinkRider(spaj);
		
		
		map.put("sdsRincian", JasperReportsUtils.convertReportData(rincian));
		map.put("sdsRingkasan", JasperReportsUtils.convertReportData(rincian));
		if(alokasiBiaya != null) map.put("sdsAlokasiBiaya", JasperReportsUtils.convertReportData(alokasiBiaya));
		
		String subReportDatakeys = null;
		subReportDatakeys = StringUtils.arrayToDelimitedString((String[]) map.get("subReportDatakeys"), ",");
		
		subReportDatakeys += ",sdsRincian,sdsRingkasan,sdsAlokasiBiaya";
		
		if(rider.size()>0 && products.stableLink(lsbs_id)){
			map.put("sdsRider", JasperReportsUtils.convertReportData(rider));
			subReportDatakeys += ",sdsRider";
		}
		
		map.put("subReportDatakeys", 
				StringUtils.delimitedListToStringArray(subReportDatakeys, ","));
		
		int flags = ((Integer) map.get("flags"));
		flags |= ULINK_ALOKASI_BIAYA | ULINK_RINCIAN | ULINK_RINGKASAN; 
		
		map.put("flags", new Integer(flags));
	}
	
	private Map setting1(String spaj, int judulId, int pilihView) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParseException{
		int flags=0;
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("judul_id", String.valueOf(judulId));
		params.put("fisibel", Boolean.FALSE);
		
		String lsbs = FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 3);
		int lsdbs_number = uwDao.selectBusinessNumber(spaj);
		
		if(products.unitLinkNew(lsbs)) {
			
			if(products.unitLinkSyariah(lsbs)){
				if(lsbs.equals("216")){
					params.put("reportPath", "/WEB-INF/classes/" + props.get("report.alokasi_dana4"));
				}else{
					params.put("reportPath", "/WEB-INF/classes/" + props.get("report.alokasi_dana2_syariah"));
				}
			}else if(lsbs.equals("191")){
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.alokasi_dana"));
			}else if(lsbs.equals("202")){
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.alokasi_dana_exclink_syariah"));
			}else if(lsbs.equals("213") || (lsbs.equals("217") && lsdbs_number == 2)){
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.alokasi_dana4"));
			}else{
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.alokasi_dana2"));
			}
			
			//ALOKASI INVESTASI 
			if("119,122,120,121,127,128,129,139,153,159,160,190,200,202,218".indexOf(lsbs)>-1 || //141
					(lsbs.equalsIgnoreCase("215") && lsdbs_number == 4) || //helpdesk [133899], produk B Smile Insurance Syariah 215-4 dan B Smile Protection Syariah 224-3
					(lsbs.equalsIgnoreCase("224") && lsdbs_number == 3)) { 
				flags |= ULINK_ALOKASI_INVESTASI;
				List alokasiInvestasi = this.uwDao.selectSuratUnitLinkAlokasiInvestasi(spaj);
				params.put("sdsAlokasiInvestasi", JasperReportsUtils.convertReportData(alokasiInvestasi));
				
				String subReportDatakeys = null;
				subReportDatakeys = StringUtils.arrayToDelimitedString((String[]) params.get("subReportDatakeys"), ",");
				subReportDatakeys += ",sdsAlokasiInvestasi";
				
				params.put("subReportDatakeys", StringUtils.delimitedListToStringArray(subReportDatakeys, ","));
			}
			
			//FOOTER
			flags |= ULINK_CATATAN;			
			List temp = new ArrayList();
			if(lsbs.equals("202")){
				Map m = new HashMap();
				m.put("MONYONG", "- Biaya Asuransi Syariah tersebut hanya pada saat usia masuk. Untuk tahun-tahun selanjutnya, biaya Asuransi Syariah akan berubah dari tahun ke tahun sesuai dengan usia yang dicapai saat tahun berjalan dan Uang Pertanggungan.");
				temp.add(m);
				m = new HashMap();
				m.put("MONYONG", "- Prosentase Alokasi Investasi belum termasuk Biaya Asuransi Syariah dan Biaya Administrasi, sesuai dengan ketentuan pada Syarat-Syarat Umum Polis pasal 19 ayat 2.");
				temp.add(m);
			}else if(lsbs.equals("213") || lsbs.equals("216")){
				/*Map m = new HashMap();
				m.put("MONYONG", "- Nilai Polis diatas diperhitungkan berdasarkan pada Harga Jual dan Harga Beli Unit pada Tanggal Perhitungan Unit.");
				temp.add(m);
				m = new HashMap();
				m.put("MONYONG", "- Pernyataan Transaksi ini merupakan Tanda Terima Pembayaran Anda.");
				temp.add(m);
				m = new HashMap();
				m.put("MONYONG", "- Laporan Alokasi Dana Awal ini dicetak secara komputerisasi sehingga tidak memerlukan tanda tangan.");
				temp.add(m);
				m = new HashMap();
				m.put("MONYONG", "- Terdapat selisih harga jual dan beli sebesar 5%.");
				temp.add(m);*/
				Map m = new HashMap();
				m.put("MONYONG", "- Kinerja investasi dalam 5 (lima) tahun terakhir dari fund sejenis. Grafik di bawah ini hanya sebagai referensi dan bukan merupakan fund yang dipasarkan untuk produk ini.");
				temp.add(m);
				m = new HashMap();
				m.put("MONYONG", "- Terdapat selisih harga jual dan harga beli sebesar 5%.");
				temp.add(m);
			}else if(lsbs.equals("217") && lsdbs_number == 2){
				Map m = new HashMap();
				m.put("MONYONG", "- Nilai Polis diatas diperhitungkan berdasarkan pada Harga Unit pada Tanggal Perhitungan Unit.");
				temp.add(m);	
				m = new HashMap();
				m.put("MONYONG", "- Pernyataan Transaksi ini merupakan Tanda Terima Pembayaran Anda.");
				temp.add(m);	
				m = new HashMap();
				m.put("MONYONG", "- Pernyataan Transaksi ini dicetak secara komputerisasi sehingga tidak memerlukan tanda tangan.");
				temp.add(m);	
				m = new HashMap();
				m.put("MONYONG", "- Grafik di bawah ini merupakan kinerja investasi dari fund dalam 5 (lima) tahun terakhir yang dapat dijadikan sebagai referensi.");
				temp.add(m);				
			}else{
			
				Map m = new HashMap();
				m.put("MONYONG", "- Nilai Polis di atas diperhitungkan berdasarkan pada Harga Unit pada Tanggal Perhitungan Unit.");
				temp.add(m);
				m = new HashMap();
				m.put("MONYONG", "- Laporan Alokasi Dana Awal ini dicetak secara komputerisasi sehingga tidak memerlukan tanda tangan.");
				temp.add(m);
			}
			params.put("sdsFooter", JasperReportsUtils.convertReportData(temp));
			params.put("subReportDatakeys", new String[] {"sdsFooter"});
			
			Integer businessNumber = this.uwDao.selectBusinessNumber(spaj);
			Date begDate = this.uwDao.selectBegDate(spaj);
			Date xSmile = defaultDateFormat.parse("01/12/2012"); // smile
			if(begDate.before(xSmile)){
				String namaPlan = this.uwDao.selectNamaBusiness(Integer.valueOf(lsbs), businessNumber);
				params.put("namaplan", namaPlan);
			}else{
				String namaPlanNew = this.uwDao.selectNamaBusinessSmile(Integer.valueOf(lsbs), businessNumber);
				params.put("namaplan", namaPlanNew);
			}
			
			//BIAYA AKUISISI
			Double biayaAkuisisi[] = new Double[0];
			
			Integer payMode = this.uwDao.selectPayMode(spaj);
			Integer payPeriod = this.uwDao.selectPayPeriod(spaj);
			
			if(payMode==0) {
				String nama_produk = "produk_asuransi.n_prod_"+FormatString.rpad("0", Integer.valueOf(lsbs).toString(), 2);
				Class aClass = Class.forName( nama_produk );
				n_prod produk = (n_prod)aClass.newInstance();				
				biayaAkuisisi = new Double[] {this.getBiayaAkuisisi(payMode, spaj, lsbs, businessNumber, payPeriod, 1, produk)};
			}else {
				int thnBayar=0;
				if("116,118,138,140,152,153,190,200,213,216,217,218".indexOf(lsbs)>-1) thnBayar=6;
				else if("119,122,139".indexOf(lsbs)>-1) {
					if(businessNumber==2 || businessNumber==5) thnBayar=3;
					else if(businessNumber==3 || businessNumber==6) thnBayar=6;
				}
				else if("120,127,128,129,141,202".indexOf(lsbs)>-1) thnBayar=3;
				else if("121".indexOf(lsbs)>-1)thnBayar=4;
				else if("159,160,162".indexOf(lsbs)>-1) thnBayar=6;
				if(thnBayar!=0) {
					biayaAkuisisi = new Double[thnBayar];
					String nama_produk = "produk_asuransi.n_prod_"+FormatString.rpad("0", Integer.valueOf(lsbs).toString(), 2);
					Class aClass = Class.forName( nama_produk );
					n_prod produk = (n_prod)aClass.newInstance();
					logger.info("BIAYA AKUISIS = " + biayaAkuisisi.length);
					for(int i=0; i<biayaAkuisisi.length; i++) {
						biayaAkuisisi[i] = getBiayaAkuisisi(payMode, spaj, lsbs, businessNumber, payPeriod, (i+1), produk);
						if(lsbs.equals("121") && i==3){
							if(biayaAkuisisi[i]==null){
								biayaAkuisisi[i]=0.;
							}
						}
					}
				}
			}
			
			DecimalFormat format1digit = new DecimalFormat("#,##0.0;(#,##0.0)");
			if(lsbs.equals("202")){
				format1digit = new DecimalFormat("#,##0;(#,##0)");
			}
			List al = new ArrayList();
			Map ak;
			String lsbs_id = uwDao.selectBusinessId(spaj);
//			int lsdbs_number = uwDao.selectBusinessNumber(spaj);
			
			for(int i=0; i<biayaAkuisisi.length; i++) {
				logger.info(i + " = " + biayaAkuisisi[i]);
				if(biayaAkuisisi[i] == null) {
					biayaAkuisisi[i] = biayaAkuisisi[i-1];
				}
				ak = new HashMap();
				ak.put("TAHUN", (payMode==0?"Premi Sekaligus":("Premi Pokok Tahun ke-"+(i+1)+(
						("115,116,117,118,138,140,152,153,159,213,216,217,218".indexOf(lsbs)>-1 && i==5)?" dst":
							("162".indexOf(lsbs)>-1 && i==5)?" dst":
								("120,127,128,129,141,202".indexOf(lsbs)>-1 && i==2)?" dst":
									("121".indexOf(lsbs)>-1&& i==3)?" dst":
										("160".indexOf(lsbs)>-1 && i==5)?" dst":""
				))));
				ak.put("INVES", format1digit.format((1-biayaAkuisisi[i])*100) + " %");
				ak.put("AKUI", format1digit.format(biayaAkuisisi[i]*100) + " %");
				if(products.syariah(lsbs_id, Integer.toString(lsdbs_number))) ak.put("SYARIAH", true); else ak.put("SYARIAH", false);
				if("115,116,117,118,138,159,190,217,218".indexOf(lsbs)>-1 ) ak.put("EKALINK", true); else ak.put("EKALINK", false);
				if(products.muamalat(Integer.valueOf(lsbs_id), lsdbs_number)) ak.put("MUAMALAT", true); else ak.put("MUAMALAT", false);
				if(lsbs_id.equals("121"))ak.put("TAHUNAN CERDAS", true);else ak.put("TAHUNAN CERDAS", false);
				if(lsbs_id.equals("202") || lsbs_id.equals("216"))ak.put("EXCL", "0");
				al.add(ak);
			}
			
			//Anta - Khusus Excellink Syariah
			if(lsbs.equals("202")){
				Integer biayaTU = this.uwDao.selectBiayaTU(spaj);
					if(biayaTU != null)	{
						for(int i=0; i<2; i++){
							Map ec = new HashMap();
							ec.put("TAHUN", (i==1?"Premi Top Up Berkala":"Premi Top Up Tunggal"));
							ec.put("INVES", format1digit.format(100-biayaTU) + " %");
							ec.put("AKUI", format1digit.format(biayaTU) + " %");
							ec.put("SYARIAH", true);
							ec.put("EKALINK", false);
							ec.put("MUAMALAT", false);
							ec.put("TAHUNAN CERDAS", false);
							ec.put("EXCL", "1");
							al.add(ec);
						}
					}
			}
			
			//Yusuf (9 Juli 2009), Tambahan untuk produk2 ulink syariah, istilah "Premi" dirubah menjadi "Kontribusi/Premi"
			if(products.unitLinkSyariah(lsbs_id)){
				for(int i=0; i< al.size(); i++){
					Map tmp = (HashMap) al.get(i);
					String haha = (String) tmp.get("TAHUN");
					tmp.put("TAHUN", haha.replaceAll("Premi", "Kontribusi"));
				}
			}
			
			logger.info("---------------");
			logger.info(al);
			logger.info("---------------");
			
			//ALOKASI BIAYA AKUISISI
			if(!al.isEmpty()) {
				flags |= ULINK_ALOKASI_BIAYA_AKUISISI;
				params.put("sdsAkuisisi", JasperReportsUtils.convertReportData(al));

				String subReportDatakeys = null;
				subReportDatakeys = StringUtils.arrayToDelimitedString((String[]) params.get("subReportDatakeys"), ",");
				
				subReportDatakeys += ",sdsAkuisisi";
				
				params.put("subReportDatakeys", 
						StringUtils.delimitedListToStringArray(subReportDatakeys, ","));
			}
			
		}else {
			//FOOTER
			flags |= ULINK_CATATAN;

			List temp = new ArrayList();
			Map m = new HashMap();
			m.put("MONYONG", "- Nilai Polis di atas diperhitungkan berdasarkan pada Harga Unit pada Tanggal Perhitungan Unit.");
			temp.add(m);
//			int lsdbs_number = uwDao.selectBusinessNumber(spaj);
			
			if(products.stableLink(lsbs)) {
				
				List detail = uwDao.selectDetailBisnis(spaj);
				Map det = (HashMap) detail.get(0);
				if(!products.progressiveLink(lsbs)){
					if(!detail.isEmpty()) {
						if(lsdbs_number!=2){
						if(lsbs.equals("174")){
							params.put("nama_produk", "STABLE LINK SYARIAH");
						}else{
							params.put("nama_produk", "STABLE LINK");
						}
						}else params.put("nama_produk", "SIMAS STABIL LINK");
//						params.put("nama_produk", (String) det.get("LSDBS_NAME"));
					}
				}else{
					if(lsdbs_number==3){
						if(!detail.isEmpty()) {
							params.put("nama_produk", "SIMAS PROGRESSIVE LINK");
						}
					}else{
						if(!detail.isEmpty()) {
							params.put("nama_produk", "PROGRESSIVE LINK");
//							params.put("nama_produk", (String) det.get("LSDBS_NAME"));
						}
					}
				}
				
				
				temp.clear();
				if(products.progressiveLink(lsbs)){
					params.put("reportPath", "/WEB-INF/classes/" + props.get("report.alokasi_dana_prog_slink"));
				}else{
					params.put("reportPath", "/WEB-INF/classes/" + props.get("report.alokasi_dana_slink"));
				}
				
				
				
				List<Map> infoStableLink = uwDao.selectInfoStableLink(spaj);
				Map info = infoStableLink.get(0);
				Date dateNab = (Date) info.get("MSL_TGL_NAB");
				Date bdate = (Date) info.get("MSL_BDATE");
				Date tglStart = new SimpleDateFormat("dd/MM/yyyy").parse("31/1/2012"); 
				
				double msl_nab = (double) 0;
				double msl_unit = (double) 0;
				double msl_mgi = (double)0; 
				double komisi_bank = (double)0; 
				
				String lku_id = (String) info.get("LKU_ID");
				String lku_symbol = (String) info.get("LKU_SYMBOL");
				msl_mgi = ((BigDecimal) info.get("MSL_MGI")).doubleValue();
				double komisi = 0.;
				if(bdate.after(tglStart)){
					if(lku_id.equals("01")){
						komisi = 50000.;
					}else{
						komisi = 5.;
					}
				}else{
					if(lku_id.equals("01")){
						komisi = 20000.;
					}else{
						komisi = 2.;
					}
				}
				komisi_bank = komisi * msl_mgi ;
				
				for(Map s : infoStableLink) {
					if(s.get("MSL_NAB")!=null){
						msl_nab = ((BigDecimal) s.get("MSL_NAB")).doubleValue();
						msl_unit += ((BigDecimal) s.get("MSL_UNIT")).doubleValue();
					}
				}
				
				DecimalFormat df4 = new DecimalFormat("#,##0.0000;(#,##0.0000)");
				DecimalFormat df5 = new DecimalFormat("#,##0.00;(#,##0.00)");
				
				m = new HashMap();
				//m.put("MONYONG", "- Harga Unit yang digunakan adalah Harga Unit pada tanggal t-3 yaitu Harga Unit pada tanggal 3 (tiga) hari kerja sebelum tanggal pengajuan New Business/Top Up Lanjutan/Penarikan.");
				//m.put("MONYONG", "- Harga Unit yang digunakan adalah Harga Unit pada tanggal t-3 yaitu Harga Unit pada tanggal 3 (tiga) hari kerja sebelum uang diterima di rekening Penanggung ("+FormatDate.toIndonesian(dateNab)+")");
				//m.put("MONYONG", "- Harga Unit yang digunakan adalah Harga Unit pada tanggal t-3 yaitu Harga Unit pada tanggal 3 (tiga) hari kerja sebelum uang diterima");
				m.put("MONYONG", "- Harga Unit pada Tanggal Perhitungan Unit adalah sebesar " + df4.format(msl_nab) + " dan Total Unit pada Tanggal Perhitungan Unit adalah");
				temp.add(m);
				m = new HashMap();
				m.put("MONYONG", "  sebesar " + df4.format(msl_unit) + ".");
				temp.add(m);
				if(lsdbs_number!=11){
					m = new HashMap();
					m.put("MONYONG", "- Harga Unit yang digunakan adalah Harga Unit pada tanggal t-3 yaitu Harga Unit pada tanggal 3 (tiga) hari kerja sebelum uang diterima");
					temp.add(m);
					m = new HashMap();
					m.put("MONYONG", "  di rekening Penanggung ("+FormatDate.toIndonesian(dateNab)+").");
					temp.add(m);
				}
				m = new HashMap();
				m.put("MONYONG", "- Harga Unit digunakan sebagai dasar pengenaan Biaya Investasi");
//				if((lsbs.equals("164") && lsdbs_number==2) || (lsbs.equals("186") && lsdbs_number==3)){
//					m = new HashMap();
//					m.put("MONYONG", "- Komisi Bank : " +lku_symbol+ df5.format(komisi_bank));
//					temp.add(m);
//				}
			}else if(lsbs.equals("165") || lsbs.equals("166")) {
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.alokasi_dana"));
				m = new HashMap();
				m.put("MONYONG", "- Laporan Alokasi Dana Awal ini dicetak secara komputerisasi sehingga tidak memerlukan tanda tangan.");
				temp.add(m);
			}else if(lsbs.equals("215")){
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.alokasi_dana_primelink_syariah"));
				m = new HashMap();
				m.put("MONYONG", "- Pernyataan Transaksi ini dicetak secara komputerisasi sehingga tidak memerlukan tanda tangan.");
				temp.add(m);
			}else {
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.alokasi_dana"));
				
				m = new HashMap();
				m.put("MONYONG", "- Pernyataan Transaksi ini dicetak secara komputerisasi sehingga tidak memerlukan tanda tangan.");
				temp.add(m);
			}
			
			
			params.put("sdsFooter", JasperReportsUtils.convertReportData(temp));
			params.put("subReportDatakeys", new String[] {"sdsFooter"});		
		}
		
		params.put("flags", new Integer(flags));
		
		return params;
	}
	
	private Map settingSlinkTopUp(String spaj, int judulId, int pilihView,int tu_ke) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParseException{
		int flags=0;
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("judul_id", String.valueOf(judulId));
		params.put("fisibel", Boolean.FALSE);
		params.put("tu_ke", ""+tu_ke);
		
		String lsbs = FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 3);
		int lsdbs_number = uwDao.selectBusinessNumber(spaj);
	
		//FOOTER
		flags |= ULINK_CATATAN;
	
		List temp = new ArrayList();
		Map m = new HashMap();
		m.put("MONYONG", "- Nilai Polis di atas diperhitungkan berdasarkan pada Harga Unit pada Tanggal Perhitungan Unit.");
		temp.add(m);
		
			
		List detail = uwDao.selectDetailBisnis(spaj);
		
		
		
		temp.clear();
		
		params.put("reportPath", "/WEB-INF/classes/" + props.get("report.alokasi_dana_slink_topup"));
		List<Map> infoStableLink = uwDao.selectInfoStableLinkTopUp(spaj, tu_ke);
		Map info = infoStableLink.get(0);
		Date dateNab = (Date) info.get("MSL_TGL_NAB");
		Date bdate = (Date) info.get("MSL_BDATE");
		String flag_bulanan= info.get("FLAG_BULANAN")==null?"":info.get("FLAG_BULANAN").equals("0")?"":"";
		Date tglStart = new SimpleDateFormat("dd/MM/yyyy").parse("31/1/2012"); 
		
		if(!detail.isEmpty()) {
			Map det = (HashMap) detail.get(0);
			params.put("nama_produk", ((String) det.get("LSDBS_NAME"))+flag_bulanan);
		}
		
		double msl_nab = (double) 0;
		double msl_unit = (double) 0;
		double msl_mgi = (double)0; 
		double komisi_bank = (double)0; 
		
		String lku_id = (String) info.get("LKU_ID");
		String lku_symbol = (String) info.get("LKU_SYMBOL");
		msl_mgi = ((BigDecimal) info.get("MSL_MGI")).doubleValue();
		double komisi = 0.;
		if(bdate.after(tglStart)){
			if(lku_id.equals("01")){
				komisi = 50000.;
			}else{
				komisi = 5.;
			}
		}else{
			if(lku_id.equals("01")){
				komisi = 20000.;
			}else{
				komisi = 2.;
			}
		}
		komisi_bank = komisi * msl_mgi ;
		
		for(Map s : infoStableLink) {
			msl_nab = ((BigDecimal) s.get("MSL_NAB")).doubleValue();
			msl_unit += ((BigDecimal) s.get("MSL_UNIT")).doubleValue();
		}
		
		DecimalFormat df4 = new DecimalFormat("#,##0.0000;(#,##0.0000)");
		DecimalFormat df5 = new DecimalFormat("#,##0.00;(#,##0.00)");
		
		m = new HashMap();
		//m.put("MONYONG", "- Harga Unit yang digunakan adalah Harga Unit pada tanggal t-3 yaitu Harga Unit pada tanggal 3 (tiga) hari kerja sebelum tanggal pengajuan New Business/Top Up Lanjutan/Penarikan.");
		//m.put("MONYONG", "- Harga Unit yang digunakan adalah Harga Unit pada tanggal t-3 yaitu Harga Unit pada tanggal 3 (tiga) hari kerja sebelum uang diterima di rekening Penanggung ("+FormatDate.toIndonesian(dateNab)+")");
		//m.put("MONYONG", "- Harga Unit yang digunakan adalah Harga Unit pada tanggal t-3 yaitu Harga Unit pada tanggal 3 (tiga) hari kerja sebelum uang diterima");
		m.put("MONYONG", "- Harga Unit pada Tanggal Perhitungan Unit adalah sebesar " + df4.format(msl_nab) + " dan Total Unit pada Tanggal Perhitungan Unit adalah");
		temp.add(m);
		m = new HashMap();
		m.put("MONYONG", "  sebesar " + df4.format(msl_unit) + ".");
		temp.add(m);
		m = new HashMap();
		m.put("MONYONG", "- Harga Unit yang digunakan adalah Harga Unit pada tanggal t-3 yaitu Harga Unit pada tanggal 3 (tiga) hari kerja sebelum uang diterima");
		temp.add(m);
		m = new HashMap();
		m.put("MONYONG", "  di rekening Penanggung ("+FormatDate.toIndonesian(dateNab)+").");
		temp.add(m);
		m = new HashMap();
		m.put("MONYONG", "- Harga Unit digunakan sebagai dasar pengenaan Biaya Investasi");
		//m.put("MONYONG", "- Harga Unit digunakan sebagai dasar pengenaan Biaya Investasi sebagaimana yang tercantum pada Syarat-Syarat Umum Polis Pasal 11.");
		temp.add(m);
//		if((lsbs.equals("164") && lsdbs_number==2) || (lsbs.equals("186") && lsdbs_number==3)){
//			m = new HashMap();
//			m.put("MONYONG", "- Komisi Bank : " +lku_symbol+ df5.format(komisi_bank));
//			temp.add(m);
//		}
		
		params.put("sdsFooter", JasperReportsUtils.convertReportData(temp));
		params.put("subReportDatakeys", new String[] {"sdsFooter"});		
		
		params.put("flags", new Integer(flags));
		

		
		return params;
	}
	
	private Map settingPsaveTopUp(String spaj, int judulId, int pilihView,int tu_ke) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParseException{
		int flags=0;
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("judul_id", String.valueOf(judulId));
		params.put("fisibel", Boolean.FALSE);
		params.put("tu_ke", ""+tu_ke);
		
		String lsbs = FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 3);
		int lsdbs_number = uwDao.selectBusinessNumber(spaj);
	
		//FOOTER
//		flags |= ULINK_CATATAN;
	
		List temp = new ArrayList();
		Map m = new HashMap();
//		m.put("MONYONG", "- Nilai Polis di atas diperhitungkan berdasarkan pada Harga Unit pada Tanggal Perhitungan Unit.");
//		temp.add(m);
		
			
		List detail = uwDao.selectDetailBisnis(spaj);
		
		
		
		temp.clear();
		if(lsbs.equals("188")){
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.alokasi_dana_psave_topup"));
		}else{
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.alokasi_dana_slink_topup"));
		}
		
		List<Map> infoStableLink = uwDao.selectInfoStableLinkTopUp(spaj, tu_ke);
		if(lsbs.equals("188")){
			infoStableLink = uwDao.selectInfoPSaveTopUp(spaj, tu_ke);
		}
		Map info = infoStableLink.get(0);
		String flag_bulanan= info.get("FLAG_BULANAN")==null?"":info.get("FLAG_BULANAN").equals("0")?"":"";
		
		if(!detail.isEmpty()) {
			Map det = (HashMap) detail.get(0);
			params.put("nama_produk", ((String) det.get("LSDBS_NAME"))+flag_bulanan);
		}
		
		double msl_nab = (double) 0;
		double msl_unit = (double) 0;
		double msl_mgi = (double)0; 
		double komisi_bank = (double)0; 
		Date bdate = (Date) info.get("MSL_BDATE");
		Date tglStart = new SimpleDateFormat("dd/MM/yyyy").parse("31/1/2012"); 
		
		String lku_id = (String) info.get("LKU_ID");
		String lku_symbol = (String) info.get("LKU_SYMBOL");
		if(lsbs.equals("188")){
			msl_mgi = ((BigDecimal) info.get("MPS_MGI")).doubleValue();
		}else{
			msl_mgi = ((BigDecimal) info.get("MSL_MGI")).doubleValue();
		}
		
		double komisi = 0.;
		if(bdate.after(tglStart)){
			if(lku_id.equals("01")){
				komisi = 50000.;
			}else{
				komisi = 5.;
			}
		}else{
			if(lku_id.equals("01")){
				komisi = 20000.;
			}else{
				komisi = 2.;
			}
		}
		komisi_bank = komisi * msl_mgi ;
		
		DecimalFormat df4 = new DecimalFormat("#,##0.0000;(#,##0.0000)");
		DecimalFormat df5 = new DecimalFormat("#,##0.00;(#,##0.00)");
		
		if(lsbs.equals("164")){
			Date dateNab = (Date) info.get("MSL_TGL_NAB");
			for(Map s : infoStableLink) {
				msl_nab = ((BigDecimal) s.get("MSL_NAB")).doubleValue();
				msl_unit += ((BigDecimal) s.get("MSL_UNIT")).doubleValue();
			}
		
			m = new HashMap();
			//m.put("MONYONG", "- Harga Unit yang digunakan adalah Harga Unit pada tanggal t-3 yaitu Harga Unit pada tanggal 3 (tiga) hari kerja sebelum tanggal pengajuan New Business/Top Up Lanjutan/Penarikan.");
			//m.put("MONYONG", "- Harga Unit yang digunakan adalah Harga Unit pada tanggal t-3 yaitu Harga Unit pada tanggal 3 (tiga) hari kerja sebelum uang diterima di rekening Penanggung ("+FormatDate.toIndonesian(dateNab)+")");
			//m.put("MONYONG", "- Harga Unit yang digunakan adalah Harga Unit pada tanggal t-3 yaitu Harga Unit pada tanggal 3 (tiga) hari kerja sebelum uang diterima");
			m.put("MONYONG", "- Harga Unit pada Tanggal Perhitungan Unit adalah sebesar " + df4.format(msl_nab) + " dan Total Unit pada Tanggal Perhitungan Unit adalah");
			temp.add(m);
			m = new HashMap();
			m.put("MONYONG", "  sebesar " + df4.format(msl_unit) + ".");
			temp.add(m);
			m = new HashMap();
			m.put("MONYONG", "- Harga Unit yang digunakan adalah Harga Unit pada tanggal t-3 yaitu Harga Unit pada tanggal 3 (tiga) hari kerja sebelum uang diterima");
			temp.add(m);
			m = new HashMap();
			m.put("MONYONG", "  di rekening Penanggung ("+FormatDate.toIndonesian(dateNab)+").");
			temp.add(m);
			m = new HashMap();
			m.put("MONYONG", "- Harga Unit digunakan sebagai dasar pengenaan Biaya Investasi");
			//m.put("MONYONG", "- Harga Unit digunakan sebagai dasar pengenaan Biaya Investasi sebagaimana yang tercantum pada Syarat-Syarat Umum Polis Pasal 11.");
			temp.add(m);
		}
//		if((lsbs.equals("164") && lsdbs_number==2) || (lsbs.equals("186") && lsdbs_number==3) || (lsbs.equals("188") && lsdbs_number==2)){
//			m = new HashMap();
//			m.put("MONYONG", "- Komisi Bank : " +lku_symbol+ df5.format(komisi_bank));
//			temp.add(m);
//		}
		
		params.put("sdsFooter", JasperReportsUtils.convertReportData(temp));
		params.put("subReportDatakeys", new String[] {"sdsFooter"});		
		
		params.put("flags", new Integer(flags));
		

		
		return params;
	}
	
	private Map settingEndorsemen(String spaj, int judulId, int pilihView,int tu_ke) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		int flags=0;
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("judul_id", String.valueOf(judulId));
		params.put("fisibel", Boolean.FALSE);
		params.put("tu_ke", ""+tu_ke);
		
		String lsbs = FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 3);
		
	
		//FOOTER
		flags |= ULINK_CATATAN;
	
		Map m = new HashMap();
		m.put("MONYONG", "- Nilai Polis di atas diperhitungkan berdasarkan pada Harga Unit pada Tanggal Perhitungan Unit.");
		
			
		List detail = uwDao.selectDetailBisnis(spaj);
		
		
		
		
		params.put("reportPath", "/WEB-INF/classes/" + props.get("report.alokasi_dana_slink_topup"));
		
		List<Map> infoStableLink = uwDao.selectInfoStableLinkTopUp(spaj, tu_ke);
		Map info = infoStableLink.get(0);
		Date dateNab = (Date) info.get("MSL_TGL_NAB");
		String flag_bulanan= info.get("FLAG_BULANAN")==null?"":info.get("FLAG_BULANAN").equals("0")?"":"";
		
		if(!detail.isEmpty()) {
			Map det = (HashMap) detail.get(0);
			params.put("nama_produk", ((String) det.get("LSDBS_NAME"))+flag_bulanan);
		}
		
		double msl_nab = (double) 0;
		double msl_unit = (double) 0;
		
		for(Map s : infoStableLink) {
			msl_nab = ((BigDecimal) s.get("MSL_NAB")).doubleValue();
			msl_unit += ((BigDecimal) s.get("MSL_UNIT")).doubleValue();
		}
		
		DecimalFormat df4 = new DecimalFormat("#,##0.0000;(#,##0.0000)");
		
		
		params.put("subReportDatakeys", new String[] {"sdsFooter"});		

		
		return params;
	}
	
	private void settingAkhirSLinkTopUp(Map map) {
		//YANG DISETTING DISINI : 
		//- RINCIAN TRANSAKSI
		//- RINGKASAN TRANSAKSI
		//- ALOKASI BIAYA
		String spaj = (String) map.get("spaj");
		Integer tu_ke = new Integer((String) map.get("tu_ke"));
		Date mtu_tgl_trans = (Date) ((Map) ((List) map.get("view")).get(0)).get("MU_TGL_TRANS");

		List alokasiBiaya = null;
		String lsbs_id = uwDao.selectBusinessId(spaj);
		Integer lsdbs_number = uwDao.selectBusinessNumber(spaj);
		
		
		
		List rincian;
		if(lsbs_id.equals("188")){
			rincian = this.uwDao.selectInfoPSaveTopUp(spaj, tu_ke);
		}else{
			rincian = this.uwDao.selectInfoStableLinkTopUp(spaj, tu_ke);
		}
		
		map.put("sdsRincian", JasperReportsUtils.convertReportData(rincian));
		map.put("sdsRingkasan", JasperReportsUtils.convertReportData(rincian));
		if(alokasiBiaya != null) map.put("sdsAlokasiBiaya", JasperReportsUtils.convertReportData(alokasiBiaya));
		
		String subReportDatakeys = null;
		subReportDatakeys = StringUtils.arrayToDelimitedString((String[]) map.get("subReportDatakeys"), ",");
		
		subReportDatakeys += ",sdsRincian,sdsRingkasan,sdsAlokasiBiaya";
		
		map.put("subReportDatakeys", 
				StringUtils.delimitedListToStringArray(subReportDatakeys, ","));
		
		int flags = ((Integer) map.get("flags"));
		flags |= ULINK_ALOKASI_BIAYA | ULINK_RINCIAN | ULINK_RINGKASAN; 
		
		map.put("flags", new Integer(flags));
	}
	
	

}
