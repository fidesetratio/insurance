package com.ekalife.utils.jasper;
 
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;
import net.sf.jasperreports.engine.fill.JRFillParameter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.utils.AngkaTerbilang;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_hit_umur;
import com.ibatis.common.resources.Resources;

/**
 * Class ini berfungsi untuk disertakan dalam jasperreports sebagai scriptlet utama,
 * dimana didalamnya sudah dimasukkan fungsi2 kecil yang berguna untuk reporting
 * apabila ingin menambahkan fungsi yang spesifik untuk sebuah report tertentu, lebih baik
 * class ini di-extend saja sebagai parent.
 * 
 * @author Yusuf
 * @since 20051208
 */
public class JasperScriptlet extends JRDefaultScriptlet {
 
	protected static final Log logger = LogFactory.getLog( JasperScriptlet.class );
	
	public void beforeReportInit() throws JRScriptletException {
		JRFillParameter fillParameter = (JRFillParameter) parametersMap.get("REPORT_PARAMETERS_MAP");
		Map parameterMap = (Map) fillParameter.getValue();
		
//		logger.info("===============================================================");
//		
//		for(Object s : parameterMap.keySet()) {
//			Object a = parameterMap.get((String) s);
//			if(!(a instanceof Properties)) logger.info(s + " = " + parameterMap.get((String) s));
//		}
		
//		if(logger.isDebugEnabled()) {
//			JRFillParameter fillParameter = (JRFillParameter) parametersMap.get("REPORT_PARAMETERS_MAP");
//			Map parameterMap = (Map) fillParameter.getValue();
//			logger.debug(parameterMap.get("reportPath"));
//		}
	}
	
	public String formatUpperCase(String kata){
		return kata.toUpperCase();
	}
	
	public String rpad(String karakter, String kata, int panjang) {
		return FormatString.rpad(karakter, kata, panjang);
	}
	
	public String formatTerbilang(BigDecimal amount, String lku){
		return AngkaTerbilang.indonesian(amount.toString(), lku);
	}
	
	public Integer hitungUmur(Date bod, Date sysdate){
		f_hit_umur a=new f_hit_umur();
		String[]bodnya=(new SimpleDateFormat("dd/MM/yyyy").format(bod)).split("/");
		String[]sysdatenya=(new SimpleDateFormat("dd/MM/yyyy").format(sysdate)).split("/");
		Integer umur=a.umur(Integer.parseInt(bodnya[2]), Integer.parseInt(bodnya[1]), Integer.parseInt(bodnya[1]), Integer.parseInt(sysdatenya[2]),Integer.parseInt( sysdatenya[1]), Integer.parseInt(sysdatenya[0]));
		return umur;
	}
	
	public String formatDateIndonesian(Object tgl) throws JRScriptletException {
		return FormatDate.toIndonesian((Date) tgl);
	}

	public String formatDateIndonesian2(String tgl) throws JRScriptletException {
		return FormatDate.toIndonesian(tgl);
	}
	public String formatDayInWeekIndonesia(Date tgl)throws JRScriptletException {
		return FormatDate.getDayInWeekIndonesia(tgl);
	}
	
	public String formatMonthYear(String tgl)throws JRScriptletException {
		
		return  (tgl.substring(0, 2).equals("01") ? " Jan-" : tgl
						.substring(0, 2).equals("02") ? " Feb-" : tgl
						.substring(0, 2).equals("03") ? " Mar-" : tgl
						.substring(0, 2).equals("04") ? " Apr-" : tgl
						.substring(0, 2).equals("05") ? " May-" : tgl 
						.substring(0, 2).equals("06") ? " Jun-" : tgl
						.substring(0, 2).equals("07") ? " Jul-" : tgl 
						.substring(0, 2).equals("08") ? " Aug-" : tgl
						.substring(0, 2).equals("09") ? " Sept-" : tgl
						.substring(0, 2).equals("10") ? " Okt-" : tgl
						.substring(0, 2).equals("11") ? " Nov-" : tgl
						.substring(0, 2).equals("12") ? " Dec-" : "")
				+ tgl.substring(2, 6);
	}
	
	public static String formatMonthYear2(Date date) {
		
		DateFormat df = new SimpleDateFormat("ddMMyyyy");
		String temp = df.format(date);
		return  (temp.substring(2, 4).equals("01") ? " Jan-" : temp
						.substring(2, 4).equals("02") ? " Feb-" : temp
						.substring(2, 4).equals("03") ? " Mar-" : temp
						.substring(2, 4).equals("04") ? " Apr-" : temp
						.substring(2, 4).equals("05") ? " Mei-" : temp
						.substring(2, 4).equals("06") ? " Jun-" : temp
						.substring(2, 4).equals("07") ? " Jul-" : temp
						.substring(2, 4).equals("08") ? " Agu-" : temp
						.substring(2, 4).equals("09") ? " Sep-" : temp
						.substring(2, 4).equals("10") ? " Okt-" : temp
						.substring(2, 4).equals("11") ? " Nov-" : temp
						.substring(2, 4).equals("12") ? " Des-" : "")
				+ temp.substring(4);
	}
	
	public String formatDateIndonesian1(String tgl) throws JRScriptletException {
		DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
		Date tanggal=null;
		try {
			tanggal = df1.parse(tgl);
		} catch (ParseException e) {
			logger.error("ERROR :", e);
		}
		return FormatDate.toIndonesian((Date) tanggal);
	}

	/**
	 * Fungsi untuk Tambah Tanggal (contoh: FormatDate.add(tanggal, Calendar.DATE, 30) atau add(new Date(), Calendar.MONTH, 1)),
	 * bisa juga menggunakan negatif (untuk mengurangi)
	 * 
	 * @param tanggal
	 *            Tanggal yang ingin ditambahkan
	 * @param kalendar
	 *            Konstanta penambah, sesuai dengan konstanta yang ada di class
	 *            Calendar
	 * @param angka
	 *            Jumlah angka yang ingin ditambahkan ke tanggal bersangkutan
	 * @return Date hasil setelah ditambahkan (atau dikurangi)
	 * @see Date, Calendar
	 */
	public static Date add(Date tanggal, int kalendar, int angka) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(tanggal);
		cal.add(kalendar, angka);
		return cal.getTime();
	}
	
	public static String toMonYearIndonesian(String date) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		
		Date tgl=null;
		try {
			tgl = sdf.parse(date);
		} catch (ParseException e) {
			logger.error("ERROR :", e);
		}
		if (tgl == null) return "";
		DateFormat df = new SimpleDateFormat("ddMMyyyy");
		String temp = df.format(tgl);
		return  (temp.substring(2, 4).equals("01") ? " Januari " : temp
						.substring(2, 4).equals("02") ? " Februari " : temp
						.substring(2, 4).equals("03") ? " Maret " : temp
						.substring(2, 4).equals("04") ? " April " : temp
						.substring(2, 4).equals("05") ? " Mei " : temp
						.substring(2, 4).equals("06") ? " Juni " : temp
						.substring(2, 4).equals("07") ? " Juli " : temp
						.substring(2, 4).equals("08") ? " Agustus " : temp
						.substring(2, 4).equals("09") ? " September " : temp
						.substring(2, 4).equals("10") ? " Oktober " : temp
						.substring(2, 4).equals("11") ? " November " : temp
						.substring(2, 4).equals("12") ? " Desember " : "")
				+ temp.substring(4);
	}

	public static String formatYYYYMM(String yyyymm, int addMonth) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		
		//parse date
		Date tgl = null;
		try {
			tgl = sdf.parse(yyyymm);
		} catch (ParseException e) {
			logger.error("ERROR :", e);
		}
		if (tgl == null) return "";
		
		//tambahkan date dgn var addMonth
		Calendar cal = Calendar.getInstance();
		cal.setTime(tgl);
		cal.add(Calendar.MONTH, addMonth);
		tgl = cal.getTime();
		
		String temp = sdf.format(tgl);
		return  (temp.substring(4).equals("01") ? " Januari " : temp
						.substring(4).equals("02") ? " Februari " : temp
						.substring(4).equals("03") ? " Maret " : temp
						.substring(4).equals("04") ? " April " : temp
						.substring(4).equals("05") ? " Mei " : temp
						.substring(4).equals("06") ? " Juni " : temp
						.substring(4).equals("07") ? " Juli " : temp
						.substring(4).equals("08") ? " Agustus " : temp
						.substring(4).equals("09") ? " September " : temp
						.substring(4).equals("10") ? " Oktober " : temp
						.substring(4).equals("11") ? " November " : temp
						.substring(4).equals("12") ? " Desember " : "")
				+ temp.substring(0, 4);
	}
	
	public static String toMonYearIndonesian(Date tgl) {
		if (tgl == null) return "";
		DateFormat df = new SimpleDateFormat("ddMMyyyy");
		String temp = df.format(tgl);
		return  (temp.substring(2, 4).equals("01") ? " Januari " : temp
						.substring(2, 4).equals("02") ? " Februari " : temp
						.substring(2, 4).equals("03") ? " Maret " : temp
						.substring(2, 4).equals("04") ? " April " : temp
						.substring(2, 4).equals("05") ? " Mei " : temp
						.substring(2, 4).equals("06") ? " Juni " : temp
						.substring(2, 4).equals("07") ? " Juli " : temp
						.substring(2, 4).equals("08") ? " Agustus " : temp
						.substring(2, 4).equals("09") ? " September " : temp
						.substring(2, 4).equals("10") ? " Oktober " : temp
						.substring(2, 4).equals("11") ? " November " : temp
						.substring(2, 4).equals("12") ? " Desember " : "")
				+ temp.substring(4);
	}
	
	public String formatDateStringIndonesian(Object tanggal) {
		if (tanggal == null) return "";
		SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
		String temp = df.format(tanggal);
		return  (temp.substring(0,2) + (temp.substring(2, 4).equals("01") ? " Jan " : temp
						.substring(2, 4).equals("02") ? " Feb " : temp
						.substring(2, 4).equals("03") ? " Mar " : temp
						.substring(2, 4).equals("04") ? " Apr " : temp
						.substring(2, 4).equals("05") ? " Mei " : temp
						.substring(2, 4).equals("06") ? " Jun " : temp
						.substring(2, 4).equals("07") ? " Jul " : temp
						.substring(2, 4).equals("08") ? " Agu " : temp
						.substring(2, 4).equals("09") ? " Sep " : temp
						.substring(2, 4).equals("10") ? " Okt " : temp
						.substring(2, 4).equals("11") ? " Nov " : temp
						.substring(2, 4).equals("12") ? " Des " : "")
				+ temp.substring(4));
	}
	
	public String formatMonth(String temp) throws JRScriptletException{
		temp = FormatString.rpad("0", temp, 2);
		
		return 
			(temp.equals("01") ? " Januari " : temp
					.equals("02") ? " Februari " : temp
					.equals("03") ? " Maret " : temp
					.equals("04") ? " April " : temp
					.equals("05") ? " Mei " : temp
					.equals("06") ? " Juni " : temp
					.equals("07") ? " Juli " : temp
					.equals("08") ? " Agustus " : temp
					.equals("09") ? " September " : temp
					.equals("10") ? " Oktober " : temp
					.equals("11") ? " November " : temp
					.equals("12") ? " Desember " : temp
					.equals("13") ? " Januari " : "");
	}

	public String formatCurrency(String currency, BigDecimal amount) {
		if (amount == null){
			return "-";
		}else{
			return (currency != null ? currency : "") + new DecimalFormat("#,##0.00;(#,##0.00)").format(amount);
		}
	}

	public String formatCurrencyNoDigit(String currency, BigDecimal amount) {
		if (amount == null){
			return "-";
		}else{
			return (currency != null ? currency : "") + new DecimalFormat("#,##0;(#,##0)").format(amount);
		}
	}

	public String format4Digit(BigDecimal amount) {
		if (amount == null){
			return "";
		}else{
			return new DecimalFormat("#,##0.0000;(#,##0.0000)").format(amount);
		}
	}

	public String format3Digit(BigDecimal amount) {
		if (amount == null){
			return "";
		}else{
			return new DecimalFormat("#,##0.000;(#,##0.000)").format(amount);
		}
	}

	public String format2Digit(BigDecimal amount) {
		if (amount == null){
			return "";
		}else{
			return new DecimalFormat("#,##0.00;(#,##0.00)").format(amount);
		}
	}

	public String pembulatan2Digit(BigDecimal d, int decimalPlace){
	    BigDecimal bd = d;
	    bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
	    return new DecimalFormat("#,##0.00;(#,##0.00)").format(bd);
	}

	public String format0Digit(BigDecimal amount) {
		if (amount == null){
			return "";
		}else{
			return new DecimalFormat("#,##0;(#,##0)").format(amount);
		}
	}

	public String formatNumber(BigDecimal amount) {
		if (amount == null)
			return "";
		else if(amount.toString().indexOf(".")==-1)
			return new DecimalFormat("#,##0;(#,##0)").format(amount);
		else
			return new DecimalFormat("#,##0.00;(#,##0.00)").format(amount);
	}

	public String formatDateCreditCard(Object tanggal) {
		if (tanggal == null) return "";
		SimpleDateFormat df = new SimpleDateFormat("MM/yyyy");
		return df.format(tanggal);
	}

	public String formatDateWithTime(Object tanggal) {
		if (tanggal == null) return "";
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return df.format(tanggal);
	}

	public String formatDateWithMinute(Object tanggal) {
		if (tanggal == null) return "";
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return df.format(tanggal);
	}

	public String formatDateNumber(Object tanggal) {
		if (tanggal == null) return "";
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		return df.format(tanggal);
	}

	public String formatDateStripes(Object tanggal) {
		if (tanggal == null) return "";
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		return df.format(tanggal);
	}

	public String formatDateString(Object tanggal) {
		if (tanggal == null) return "";
		SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
		return df.format(tanggal);
	}
	// *VIPcard
	public String formatDateStringFull(Object tanggal) {
		if (tanggal == null) return "";
		SimpleDateFormat df = new SimpleDateFormat("d MMMMM yyyy");
		return df.format(tanggal);
	}
	
	public String formatinvoice(String kata) {
		if (kata == null) {
			return "";
		} else{ 
			return kata.substring(0, 3) + "/" + kata.substring(3,7)+ "/" + kata.substring(7,9);
		} 
	}
	
	public String formatSimasCard(String no_kartu) {
		return no_kartu.substring(0, 4) + " " + no_kartu.substring(4, 8) + " " + no_kartu.substring(8, 12) + " " + no_kartu.substring(12, 16);
	}
	
	public String formatbilling(String kata) {
		if (kata == null) {
			return "";
		} else{ 
			return kata.substring(0, 2) + "." + kata.substring(2,kata.length());
		} 
	}
	
	public Date convertstringdate(String kata){
		Date a=null;
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			a= df.parse(kata.substring(6, 8)+"/"+kata.substring(4, 6)+"/"+kata.substring(0, 4));
		} catch (ParseException e) {
			logger.error("ERROR :", e);
		}
		return a;
	}
	
	public String formatPolis(String kata) {
		if (kata == null) {
			return "";
		} else if (kata.length() == 9) {
			return kata.substring(0, 2) + "." + kata.substring(2);
		} else if (kata.length() == 11) {
			return kata.substring(0, 2) + "." + kata.substring(2, 6) + "."
					+ kata.substring(6);
		} else if (kata.length() == 14) {
			return kata.substring(0, 2) + "." + kata.substring(2, 5) + "."
					+ kata.substring(5, 9) + "." + kata.substring(9);
		} else
			return "";
	}

	public String formatSPAJ(String kata) {
		if (kata == null) {
			return "";
		} else if (kata.length() == 7) {
			return kata;
		} else if (kata.length() == 9) {
			return kata.substring(0, 4) + "." + kata.substring(4);
		} else if (kata.length() == 11) {
			return kata.substring(0, 2) + "." + kata.substring(2, 6) + "."
					+ kata.substring(6);
		} else
			return "";
	}
	
	public String formatNoendors(String kata) {
		if (kata == null) {
			return "";
		} else if (kata.length() == 7) {
			return kata;
		} else if (kata.length() == 9) {
			return kata.substring(0, 4) + "." + kata.substring(4);
		} else if (kata.length() == 12) {
			return kata.substring(0, 2) + "." + kata.substring(2, 6) + "."
					+ kata.substring(6);
		} else
			return "";
	}

	public String formatTTS(String kata) {
		if (kata == null) {
			return "";
		} else{
			return  kata.substring(0,3)+"/"+kata.substring(3,9)+"/"+kata.substring(9,kata.length());
		}
	}

	public String formatMask(Object kt, String format) { //contoh: formatMask("12345678901", "@@.@@@@.@.@@@@");
		if(kt==null || format==null) return "";
		String kata;
		if(!(kt instanceof String)) kata = kt.toString();
		else kata = (String) kt;
		
		String[] temp = format.split("\\.");
		if(temp.length==1) return kata;
		StringBuffer result = new StringBuffer();
		
		try{
			for(int i=0; i<temp.length; i++){
				result.append( (i!=0?".":"") + (kata.length()<temp[i].length()?kata:kata.substring(0, temp[i].length())) );
				kata = kata.substring(temp[i].length());
			}
		}catch(Exception e){}
		
		return result.toString();
	}

	public String isUnitLink(String lsbs_id) throws IOException,
			FileNotFoundException, JRScriptletException {
		String tmp = getProperty("product.unitLinkNoSLink");
		String[] array = tmp.split(",");
		for (int i = 0; i < array.length; i++)
			array[i] = array[i].trim();
		Set set = new HashSet(Arrays.asList(array));
		if (set.contains(FormatString.rpad("0", lsbs_id, 3))) {
			return "1";
		} else
			return "0";		
	}
	
	public String getProperty(String name) throws IOException,
			FileNotFoundException, JRScriptletException {
		Properties props = (Properties) this.getParameterValue("props");
		
//		logger.info("nilai = " + props.getProperty(name));
		
		if (props == null) {
			props = new Properties();
			FileInputStream in = new FileInputStream(Resources.getResourceAsFile("ekalife.properties"));
			//FileInputStream in = new FileInputStream("D:\\WorkspaceMyEclipse5\\E-Lions-server\\JavaSource\\ekalife.properties");
			props.load(in);
			//logger.info(props.getProperty("subreport.surat_slink_footer"));
		}
		return props.getProperty(name);
	}

	public String formatPadding(String karakter, String kata, int panjang) {
		return FormatString.rpad(karakter, kata, panjang);
	}

	public String formatNullValue(String value) {
		if(value==null)
			return "-";
		else return value;
	}
	
	/**Fungsi : Untuk Menampilkan banyaknya tahun,bulan,hari dalam hari yang diinginkan.
	 * 			Mis: 496 hari menjadi 1 tahun, 4 bulan, 16 hari
	 * 			dimana pembulan 1 bulan=30 hari, 1 tahun=12 bulan.
	 * 
	 * @param count
	 * @return
	 * @created by : Ferry Harlim (08/02/2008)
	 */
	public static Map getDay(Integer count){
		Map<String, Integer> map=new HashMap();
		double oneMonth=30;
		Double  month,year = 0.0,day;
		double temp;
		temp=count/oneMonth;
		String result=String.valueOf(temp);
		String arr[]=new String[2];
		result=result.replace(".", ";");
		arr=result.split(";");
		month=new Double(Integer.parseInt(arr[0]));
		day=Double.parseDouble("0."+arr[1]);
		day=day*oneMonth;
		/////
		if(month>=12){
			temp=month/12;
			result=String.valueOf(temp);
			result=result.replace(".", ";");
			arr=result.split(";");
			year=new Double(Integer.parseInt(arr[0]));
			month=month-(year*12);
		}
		map.put("year", year.intValue());
		map.put("month", month.intValue());
		map.put("day", day.intValue());
		return map;
	}
	
	public String formatDayIndonesia(String day) {
		
		return day.trim().equals("monday") ? "Senin" : 
			   day.trim().equals("tuesday") ? "Selasa" : 
			   day.trim().equals("wednesday") ? "Rabu" :
			   day.trim().equals("thursday") ? "Kamis" :
			   day.trim().equals("friday") ? "Jumat" :
			   day.trim().equals("saturday") ? "Sabtu" : "Minggu";
	}

	public int formatDateJumlahHariPerBulan(int bulan,String tahun)throws JRScriptletException {
		int tahun2 = Integer.valueOf(tahun).intValue();
		return FormatDate.getJumlahHariPerBulan(bulan,tahun2);
	}
		
	public String formatDateYearFourDigit(Date tgl) throws JRScriptletException {	
		return FormatDate.getYearFourDigit(tgl);
	}
			
	public String replaceDan(String textInput){
		String textHasil;
		textHasil = textInput.replaceAll("&", "DAN");
		return textHasil;
	}
		
}